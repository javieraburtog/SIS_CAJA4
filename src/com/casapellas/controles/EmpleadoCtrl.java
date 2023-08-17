package com.casapellas.controles;
/**
 * CASA PELLAS S.A.
 * Creado por.........: Juan Carlos Ñamendi Pineda
 * Fecha de Creación..: 22/06/2009
 * Última modificación: 10/10/2011
 * Modificado por.....:	Juan Carlos Ñamendi Pineda
 * 
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.casapellas.entidades.Pago;
import com.casapellas.entidades.Vf0101;
import com.casapellas.entidades.Vf0101Id;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.navegacion.As400Connection;
import com.casapellas.util.Divisas;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.PropertiesSystem;


public class EmpleadoCtrl {
	public Exception error;
	public Exception getError() {
		return error;
	}
	public void setError(Exception error) {
		this.error = error;
	}	
	public Exception errorDetalle;	
	public Exception getErrorDetalle() {
		return errorDetalle;
	}
	public void setErrorDetalle(Exception errorDetalle) {
		this.errorDetalle = errorDetalle;
	}
	
	
	public static int addressNumberParent(int addressNumber){
		try {
		
			String query = " select integer(MAPA8)  from " + PropertiesSystem.JDEDTA +".F0150 where maan8 = " + addressNumber;
			
			LogCajaService.CreateLog("addressNumberParent", "QRY", query);
			
			Integer addressParent = ConsolidadoDepositosBcoCtrl.executeSqlQueryUnique(query, null, true);
			
			if(addressParent == null || addressParent.intValue() == 0 ){
				return addressNumber;
			} 
			
			return addressParent.intValue();
			
		} catch (Exception e) { 
			LogCajaService.CreateLog("addressNumberParent", "ERR", e.getMessage());
			e.printStackTrace();
		}
		return addressNumber;
	}
	
	
	public static String[] validarClienteTasaEspecial(int codcli, String tipopago ){
		String[] dtaTsaCte = null;
 
		
		try {
			
			String sql = " select cast( TASA as varchar(20) ), " +
				"cast(OFICIAL as varchar(1)) , " +
				"cast( MONEDA as varchar(3)) " +
				"from "+PropertiesSystem.ESQUEMA+".TASACAMBIO_CLIENTE " +
				"WHERE CODCLIE = " + codcli +" AND trim(lower(TIPOPAGO)) = '"+
				tipopago.trim().toLowerCase()+ "' AND ACTIVO = 1 ORDER BY FECHAMOD DESC";
			
			LogCajaService.CreateLog("validarClienteTasaEspecial", "QRY", sql);
			
			List<Object[]> dtaCte = ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, null, true);
			
			if(dtaCte == null || dtaCte.isEmpty())
				return null;
			
			dtaTsaCte = new String[]{
					String.valueOf(dtaCte.get(0)[0]),
					String.valueOf(dtaCte.get(0)[1]),
					String.valueOf(dtaCte.get(0)[2])
			};
			
		} catch (Exception e) {
			e.printStackTrace(); 
			dtaTsaCte = null;
			LogCajaService.CreateLog("validarClienteTasaEspecial", "ERR", e.getMessage());
		} 
		return dtaTsaCte;
	}
	
	/***********************************************************************************************/
	public boolean updUltimoPago(Connection cn, int iCodcli, int monto){
		PreparedStatement ps = null;
		boolean bHecho = false;
		String sql = "UPDATE "+PropertiesSystem.JDEDTA+".F0301 SET ";
		FechasUtil fecUtil = new FechasUtil();
		Divisas d = new Divisas();
		try{
			sql = sql + " A5DLP = " + fecUtil.obtenerFechaActualJuliana() + ", A5ALP = " + monto + " WHERE A5AN8 = " + iCodcli;
			ps = cn.prepareStatement(sql);
			int rs = ps.executeUpdate();
			
			if(rs == 1){
				bHecho = true;
			}
		}catch(Exception ex){
			 bHecho = false;
			error = new Exception("@LOGCAJA: No se pudo actualizar la fecha del ultimo pago!!!;Cliente:" + iCodcli +";Monto: " + monto);
			errorDetalle = error;
			System.out.println("Se capturo una excepcion en EmpleadoCtrl.updUltimoPago: " + ex);
		}finally{
			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return bHecho;
	}
	public boolean updUltimoPagoWithSession(Session s, int iCodcli, int monto){		
		boolean bHecho = true;
		String sql = "UPDATE "+PropertiesSystem.JDEDTA+".F0301 SET ";
		FechasUtil fecUtil = new FechasUtil();
		Divisas d = new Divisas();
		try{
			sql = sql + " A5DLP = " + fecUtil.obtenerFechaActualJuliana() + ", A5ALP = " + monto + " WHERE A5AN8 = " + iCodcli;
			
			ConsolidadoDepositosBcoCtrl.executeSqlQueryTx(s, sql);
		}catch(Exception ex){
			 bHecho = false;
			error = new Exception("@LOGCAJA: No se pudo actualizar la fecha del ultimo pago!!!;Cliente:" + iCodcli +";Monto: " + monto);
			errorDetalle = error;
			System.out.println("Se capturo una excepcion en EmpleadoCtrl.updUltimoPago: " + ex);
		}
		return bHecho;
	}
/***********************************************************************************************/
	public Pago getultimoPago(Connection cn,int iCodcli){
		PreparedStatement ps = null;
		String sql = "select A5AN8,A5DLP,A5ALP,CAST(A5ALP/100 AS DECIMAL (12,2)) as PAGO ,(CASE WHEN A5DLP > 0 THEN CAST ((CAST(DATE(CHAR(1900000 + A5DLP)) AS TIMESTAMP)) AS DATE) " +
				"ELSE NULL END) FECHA from "+PropertiesSystem.JDEDTA+".f0301 where a5an8 =" + iCodcli;
		Date fecha = null;
		Pago p = null;
		try{
			ps = cn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = ps.executeQuery();
						
			 if (rs.next()){
				 p = new Pago();
				 p.setFecha(rs.getDate("FECHA"));
				 p.setCodigo(rs.getInt("A5AN8"));
				 p.setMonto(rs.getBigDecimal("PAGO"));
			 }
			 
		}catch(Exception ex){
			System.out.println("Se capturo una excepcion en EmpleadoCtrl.getultimaFechaPago: " + ex);
		}finally{
			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return p;
	}
	
	public Pago getultimoPagoWithSession(Session s,int iCodcli){
		
		String sql = "select A5AN8 AS codigo,CAST(A5ALP/100 AS DECIMAL (12,2)) as monto ,(CASE WHEN A5DLP > 0 THEN CAST ((CAST(DATE(CHAR(1900000 + A5DLP)) AS TIMESTAMP)) AS DATE) " +
				"ELSE NULL END) FECHA from "+PropertiesSystem.JDEDTA+".f0301 where a5an8 =" + iCodcli;
		Date fecha = null;
		Pago p = null;
		try{
			
			p= ConsolidadoDepositosBcoCtrl.executeSqlQueryUnique(sql, Pago.class, true);			
			
			 
		}catch(Exception ex){
			System.out.println("Se capturo una excepcion en EmpleadoCtrl.getultimaFechaPago: " + ex);
		}
		return p;
	}
/***********************************************************************************************/
/***********************************************************************************************/
/** obtiene una lista de empleados que coincidan con el filtro sParámetro por código o nombre **/
	public List buscarEmpleado(String sParametro,boolean bBuscarCodigo){
		List lstEmp = null;
		Session session = HibernateUtilPruebaCn.currentSession();
		
		Vf0101 vf0101 = null;
		String sql;
		
		try{
			if(bBuscarCodigo)
				sql = "from Vf0101 as f where cast(f.id.aban8 as string) like '%"+sParametro+"%'  order by f.id.abalph ";
			else
				sql = "from Vf0101 as f where lower(trim(f.id.abalph)) like '%"+sParametro.toLowerCase().trim()+"%' order by f.id.abalph";
			
					
			lstEmp = session.createQuery(sql).setMaxResults(50).list();
		
			
		}catch(Exception ex){
			System.out.print("==> Excepción capturada en EmpleadoCtrl.buscarPersonaxCodigo: " + ex);
			lstEmp = new ArrayList();
		}finally {			
			session.close();
		}
		return lstEmp;
	}
	
	public List buscarPersona(String sCodigo){
		//String sNombre = null;
		List emp = null;
		Session session = HibernateUtilPruebaCn.currentSession();
		
		Vf0101 vf0101 = null;
		try{
		
			String sql = "from Vf0101 as f where cast(f.id.aban8 as string) like '"+sCodigo+"%'";
			emp = session
			.createQuery(sql)			
			.list();
		
//			
//			for (int i = 0; i < emp.size(); i++){
//				vf0101 = (Vf0101)emp.get(i);
//				vf0101.setCodigo(String.valueOf(vf0101.getId().getAban8()));
//				vf0101.setNombre(vf0101.getId().getAbalph());
//				emp.remove(i);
//				emp.add(i, vf0101);
//			}
//			
			
			//sNombre = emp.getId().getAbalph();
		}catch(Exception ex){
			System.out.print("==> Excepción capturada en buscarPersonaxCodigo: " + ex);
		}finally {			
			session.close();
		}
		
		return emp;
	}
/*********************BUSCAR EMPLEADO POR CODIGO*************************************************/
	public String buscarEmpleadoxCodigo(int iCodigo){
		String sNombre = "";
		Vf0101 emp = null;
		Session session = null;
		
		
		try{
			
			session =  HibernateUtilPruebaCn.currentSession();
			
			
			String sql = "from Vf0101 as f where f.id.aban8 = "+iCodigo;
			emp = (Vf0101) session.createQuery(sql).uniqueResult();
	
			if(emp != null)
				sNombre = emp.getId().getAbalph();
			
			
			
		}catch(Exception ex){
			System.out.println("==> Excepción capturada en EmpleadoCtrl.buscarEmpleadoxCodigo: " + ex);
		}finally {			
			try { session.close(); } catch (Exception e2){}
		}
		return sNombre;
	}
/******************************************************************************************/
/*********************BUSCAR EMPLEADO POR NOMBRE*************************************************/
	public int buscarEmpleadoxNombre(String sNombre){
		int iCodigo = 0;
		Vf0101 emp = null;
		Session session = HibernateUtilPruebaCn.currentSession();
		
		try{
			
			String sql = "from Vf0101 as f where f.id.abalph = '"+sNombre+"'";
			emp = (Vf0101)session
			.createQuery(sql)			
			.uniqueResult();
			
			iCodigo = emp.getId().getAban8();
		}catch(Exception ex){
			System.out.println("==> Excepción capturada en EmpleadoCtrl.buscarEmpleadoxNombre: " + ex);
		}
		
		return iCodigo;
	}
	/*********************BUSCAR EMPLEADO POR CODIGO*************************************************/
	public static Vf0101 buscarEmpleadoxCodigo2(int iCodigo){
		Vf0101 emp = null;
		Session sesion = null; 
		Transaction trans = null;
		boolean newCn = false;
		
		try{			
			
			sesion = HibernateUtilPruebaCn.currentSession();
			trans = (newCn = !(sesion.getTransaction().isActive())) ? sesion
					.beginTransaction() : sesion.getTransaction();
					
			emp = (Vf0101) sesion.createQuery(
					"from Vf0101 as f where f.id.aban8 = " + iCodigo)
					.setMaxResults(1).uniqueResult();
			
		}catch(Exception ex){
			ex.printStackTrace(); 
		}finally{
			if(newCn){
				try {  trans.commit(); } 
				catch (Exception e2) { }
				try {  HibernateUtilPruebaCn.closeSession(sesion); }
				catch (Exception e2) { }
			}
		}
		return emp;
	}
/******************************************************************************************/
/*********************BUSCAR EMPLEADO POR NOMBRE*************************************************/
	public Vf0101 buscarEmpleadoxNombre2(String sNombre){
		//int iCodigo = 0;
		Vf0101 emp = null;
		Session session = HibernateUtilPruebaCn.currentSession();
		
		try{
			
			String sql = "from Vf0101 as f where f.id.abalph = '"+sNombre+"'";
			emp = (Vf0101)session
			.createQuery(sql)			
			.uniqueResult();
			
			//iCodigo = emp.getId().getAban8();
		}catch(Exception ex){
			System.out.print("==> Excepción capturada en EmpleadoCtrl.buscarEmpleadoxNombre2: " + ex);
		}finally {			
			session.close();
		}
		
		return emp;
	}
/*******************DETERMINAR EL TIPO DE USUARIO******************************************************/
	public  static String determinarTipoCliente(int iCodCli){
		String sTipoCliente = "";
		 try{
			 String sql = "select * from "+PropertiesSystem.ESQUEMA+".Vf0101 as f where f.aban8 = "+iCodCli;
			 Vf0101 vf0101 = ConsolidadoDepositosBcoCtrl.executeSqlQueryUnique(sql, Vf0101.class, true);
			 return sTipoCliente = (vf0101 == null) ? "" : vf0101.getId().getAbat1().trim().toUpperCase();
		}catch(Exception ex){
			ex.printStackTrace(); 
		} 
		return sTipoCliente ;
	}
/***************************************************************************************************/
	public String[] buscarPersonaxNombre(String sNomCli){
		String[] sNombres = null;
		List lstNombres = null;
		Session session = null;
		
		try{
			
			session = HibernateUtilPruebaCn.currentSession();
			

			String sql = "select distinct f.id.abalph from Vf0101 as f where f.id.abalph like '"+sNomCli.toUpperCase().trim()+"%'";
			lstNombres = (List)session
			.createQuery(sql)			
			.list();
			
			sNombres = new String[lstNombres.size()];
			for(int i = 0; i < lstNombres.size(); i++){
				sNombres[i] = lstNombres.get(i).toString();
			}
		}catch(Exception ex){
			System.out.println("==> Excepción capturada en EmpleadoCtrl.buscarPersonaxNombre: " + ex);
		}finally {			
			if(session.isOpen()) session.close();
		}
		
		return sNombres;
	}
/**************************************************************************************************************/
	public int[] buscarPersonaxCodigo(String sCodCli){
		int[] iCodigos = null;
		List lstCodigos = null;
		Session session = HibernateUtilPruebaCn.currentSession();
		
		try{
			
			String sql = "select distinct f.id.aban8 from Vf0101 as f where cast(f.id.aban8 as string) like '"+sCodCli.trim()+"%'";
			lstCodigos = (List)session
			.createQuery(sql)			
			.list();
			
			iCodigos = new int[lstCodigos.size()];
			for(int i = 0; i < lstCodigos.size(); i++){
				iCodigos[i] = Integer.parseInt(lstCodigos.get(i).toString());
			}
		}catch(Exception ex){
			System.out.println("==> Excepción capturada en EmpleadoCtrl.buscarPersonaxNombre: " + ex);
		}finally {			
			session.close();
		}
		
		return iCodigos;
	}
}

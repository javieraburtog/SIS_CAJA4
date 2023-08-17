package com.casapellas.controles.tmp;
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
import org.hibernate.criterion.Restrictions;

import com.casapellas.controles.ConsolidadoDepositosBcoCtrl;
import com.casapellas.entidades.Pago;
import com.casapellas.entidades.Vf0101;
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
	/***********************************************************************************************/
	public boolean updUltimoPago(Connection cn, int iCodcli, long monto){
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
			ex.printStackTrace();
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
			ex.printStackTrace();
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
/***********************************************************************************************/
/***********************************************************************************************/
/** obtiene una lista de empleados que coincidan con el filtro sParámetro por código o nombre **/
	public List buscarEmpleado(String sParametro,boolean bBuscarCodigo){
		List lstEmp = null;
		Session session = null;
		Transaction trans = null;
		Vf0101 vf0101 = null;
		String sql;
		
		try{
			 session = HibernateUtilPruebaCn.currentSession();
			
			if(bBuscarCodigo)
				sql = "from Vf0101 as f where cast(f.id.aban8 as string) like '%"+sParametro+"%'  order by f.id.abalph ";
			else
				sql = "from Vf0101 as f where lower(trim(f.id.abalph)) like '%"+sParametro.toLowerCase().trim()+"%' order by f.id.abalph";
			
			trans = session.beginTransaction();			
			lstEmp = session.createQuery(sql).setMaxResults(50).list();
			trans.commit();
			
		}catch(Exception ex){
			ex.printStackTrace(); 
			lstEmp = new ArrayList();
		}finally {			
			try {HibernateUtilPruebaCn.closeSession(session); } catch (Exception e2) {e2.printStackTrace(); }
		}
		return lstEmp;
	}
	
	public List buscarPersona(String sCodigo){
		//String sNombre = null;
		List emp = null;
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = null;
		Vf0101 vf0101 = null;
		try{
			tx = session.beginTransaction();
			String sql = "from Vf0101 as f where cast(f.id.aban8 as string) like '"+sCodigo+"%'";
			emp = session
			.createQuery(sql)			
			.list();
			tx.commit();

		}catch(Exception ex){
			ex.printStackTrace(); 
		}finally {			
			try {HibernateUtilPruebaCn.closeSession(session); } catch (Exception e2) {e2.printStackTrace(); }
		}
		
		return emp;
	}
/*********************BUSCAR EMPLEADO POR CODIGO*************************************************/
	public String buscarEmpleadoxCodigo(int iCodigo){
		String sNombre = "";
		Vf0101 emp = null;
		Session session = null;
		Transaction tx = null;
		
		try{
			
			session =  HibernateUtilPruebaCn.currentSession();
			tx = session.beginTransaction();
			
			String sql = "from Vf0101 as f where f.id.aban8 = "+iCodigo;
			emp = (Vf0101) session.createQuery(sql).uniqueResult();
	
			if(emp != null)
				sNombre = emp.getId().getAbalph();
			
			tx.commit();
			
		}catch(Exception ex){
			ex.printStackTrace(); 
		}finally {			
			try {HibernateUtilPruebaCn.closeSession(session); } catch (Exception e2) {e2.printStackTrace(); }
		}
		return sNombre;
	}
/******************************************************************************************/
/*********************BUSCAR EMPLEADO POR NOMBRE*************************************************/
	public int buscarEmpleadoxNombre(String sNombre){
		int iCodigo = 0;
		Vf0101 emp = null;
		Session session = null;
		Transaction tx = null;
	
		try{
			session = HibernateUtilPruebaCn.currentSession();
			tx = session.beginTransaction();
			String sql = "from Vf0101 as f where f.id.abalph = '"+sNombre+"'";
			emp = (Vf0101)session
			.createQuery(sql)			
			.uniqueResult();
			tx.commit();
			iCodigo = emp.getId().getAban8();
		}catch(Exception ex){
			ex.printStackTrace(); 
		}finally {			
			try {HibernateUtilPruebaCn.closeSession(session); } catch (Exception e2) {e2.printStackTrace(); }
		}
		
		return iCodigo;
	}
	/*********************BUSCAR EMPLEADO POR CODIGO*************************************************/
	public static Vf0101 buscarEmpleadoxCodigo2(int iCodigo){
		Vf0101 emp = null;
	
		try{
		 
			String sql = " from  Vf0101 v where v.id.aban8 =  " + iCodigo ;
			
			LogCajaService.CreateLog("buscarEmpleadoxCodigo2", "QRY", sql);
			
			emp = ConsolidadoDepositosBcoCtrl.executeSqlQueryUnique(sql, Vf0101.class, false);
			
		}catch(Exception ex){
			LogCajaService.CreateLog("buscarEmpleadoxCodigo2", "ERR", ex.getMessage());
		} 
		
		return emp;
	}
/******************************************************************************************/
/*********************BUSCAR EMPLEADO POR NOMBRE*************************************************/
	public Vf0101 buscarEmpleadoxNombre2(String sNombre){
		Vf0101 emp = null;
		Session sesion = null;
		Transaction trans = null;
		boolean bNuevaCn = false;
	
		try{
			sesion = HibernateUtilPruebaCn.currentSession();
			if(sesion.getTransaction().isActive())
				trans = sesion.getTransaction();
			else{
				trans = sesion.beginTransaction();
				bNuevaCn = true;
			}
			
			emp = (Vf0101)sesion.createCriteria(Vf0101.class)
						.add(Restrictions.eq("id.abalph", sNombre))
						.setMaxResults(1).uniqueResult();
			
		}catch(Exception ex){ 
			ex.printStackTrace();
		}finally {	
			if(bNuevaCn){
				try {trans.commit();}catch (Exception e) {e.printStackTrace();}
				try {HibernateUtilPruebaCn.closeSession(sesion); }
				catch (Exception e2) {e2.printStackTrace(); }
			}
		}
		return emp;
	}
	public static String determinarTipoCliente(int iCodCli, Session sesion){
		String sTipoCliente = new String();
		
		try{
			Vf0101 vf = (Vf0101)sesion.createQuery("from Vf0101 as f where " +
								"f.id.aban8 = "+iCodCli).uniqueResult();
			if(vf != null)
				sTipoCliente = vf.getId().getAbat1().trim();
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return sTipoCliente;
	}
	
/*******************DETERMINAR EL TIPO DE USUARIO******************************************************/
	public String determinarTipoCliente(int iCodCli){
		String sTipoCliente = "";
		 PreparedStatement ps = null;
		 Connection cn = null;
		try{
			String sql = "select f.abat1 from "+PropertiesSystem.ESQUEMA+".Vf0101 as f where f.aban8 = "+iCodCli;
			 cn = As400Connection.getJNDIConnection("DSMCAJAR");
			 ps = cn.prepareStatement(sql);
			 ResultSet rs = ps.executeQuery();
			 if (rs.next()){
				 sTipoCliente = rs.getString("abat1");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			try {
				ps.close();
				cn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
		return sTipoCliente.trim();
	}
/***************************************************************************************************/
	public String[] buscarPersonaxNombre(String sNomCli){
		String[] sNombres = null;
		List lstNombres = null;
		Session session = null;
		Transaction tx = null;
		try{
			
			session = HibernateUtilPruebaCn.currentSession();
			tx = session.getTransaction();

			String sql = "select distinct f.id.abalph from Vf0101 as f where f.id.abalph like '"+sNomCli.toUpperCase().trim()+"%'";
			lstNombres = (List)session
			.createQuery(sql)			
			.list();
			tx.commit();
			sNombres = new String[lstNombres.size()];
			for(int i = 0; i < lstNombres.size(); i++){
				sNombres[i] = lstNombres.get(i).toString();
			}
		}catch(Exception ex){
			ex.printStackTrace(); 
		}finally {			
			try {HibernateUtilPruebaCn.closeSession(session); } catch (Exception e2) {e2.printStackTrace(); }
		}
		
		return sNombres;
	}
/**************************************************************************************************************/
	public int[] buscarPersonaxCodigo(String sCodCli){
		int[] iCodigos = null;
		List lstCodigos = null;
		Session session = null;
		Transaction tx = null;
		
		try{
			session = HibernateUtilPruebaCn.currentSession();
			tx = session.beginTransaction();
			String sql = "select distinct f.id.aban8 from Vf0101 as f where cast(f.id.aban8 as string) like '"+sCodCli.trim()+"%'";
			lstCodigos = (List) session.createQuery(sql).list();
			tx.commit();
			iCodigos = new int[lstCodigos.size()];
			for(int i = 0; i < lstCodigos.size(); i++){
				iCodigos[i] = Integer.parseInt(lstCodigos.get(i).toString());
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally {			
			try {HibernateUtilPruebaCn.closeSession(session); } catch (Exception e2) {e2.printStackTrace(); }
		}
		
		return iCodigos;
	}
}

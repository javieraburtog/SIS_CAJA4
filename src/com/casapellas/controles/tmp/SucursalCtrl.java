package com.casapellas.controles.tmp;
/**
 * CASA PELLAS S.A.
 * Creado por.........: Juan Carlos Ñamendi Pineda
 * Fecha de Creación..: 28/02/2009
 * Última modificación: 04/08/2011
 * Modificado por.....:	Juan Carlos Ñamendi Pineda
 * Verificar si existe el numero de contrato
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.casapellas.controles.ConsolidadoDepositosBcoCtrl;
import com.casapellas.entidades.TallerUn;
import com.casapellas.entidades.Unegocio;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.navegacion.As400Connection;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.PropertiesSystem;

public class SucursalCtrl {
/*********************************************************************************************/
	public int existeContrato(String sCodcomp,String sCodsuc,int iCodcli, String sNocontrato){
		int iNocontrato = 0;
		Connection cn = null;
		PreparedStatement ps = null;
		
		String sql = "select numctto from "+PropertiesSystem.QS36F
				+".sotmpc where compancto = '"+ sCodcomp + "' and cliente = " + iCodcli
				+ " and trim(status) in ('001','002') and numctto = " + sNocontrato;
		
		try{
			cn = As400Connection.getJNDIConnection("DSMCAJAR");
			ps = cn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if (rs.next()){
				iNocontrato = rs.getInt("numctto");
			}
		}catch(Exception ex){
			iNocontrato = 0; 
			ex.printStackTrace();
			ex.printStackTrace();
		}finally{
			try{cn.close();}catch(Exception ex2){ex2.printStackTrace();};
		}
		return iNocontrato;
	}
/*********************************************************************************************/
	public TallerUn obtenerTaller(String sCodunineg){
		TallerUn tu = null;
		Session s = null;
		Transaction tx = null;
		try{
			s = HibernateUtilPruebaCn.currentSession();
			tx = s.beginTransaction();
			String sql = "from TallerUn as u where trim(u.id.pmcu1) = '"
					+ sCodunineg + "' and u.id.pmeca = 'M'";
			tu = (TallerUn)s.createQuery(sql).uniqueResult();
			tx.commit();
		}catch(Exception ex){
			System.out.print("==> Excepción capturada en SucursalCtrl.obtenerTaller: " + ex);
		}finally{
			try {HibernateUtilPruebaCn.closeSession(s); } catch (Exception e2) {e2.printStackTrace(); }
		}
		return tu;
	}
/********buscar sucursal por codigo*************************************************************************************/
	public String obtenerSucursalxCodigo(String sCodsuc){
		String sNombreSucursal = null;
		
		
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = null;
		try{
			sCodsuc = sCodsuc.substring(3).trim();
			tx = session.beginTransaction();
			String sql = "select u.id.desc from Unegocio as u where " +
					"substring(u.id.codunineg,10,11) like '%"
					+ sCodsuc + "%' and u.id.tipo = 'BS'";
			sNombreSucursal = (String) session.createQuery(sql).uniqueResult();
			tx.commit();
		}catch(Exception ex){
			System.out.print("==> Excepción capturada en SucursalCtrl.obtenerSucursalxCodigo: " + ex);
		}finally {			
			try {HibernateUtilPruebaCn.closeSession(session); } catch (Exception e2) {e2.printStackTrace(); }
		}
		return sNombreSucursal;
	}
/*********Obtiene una lista de sucursales por codigo de compania**************************************************/
	
	public Unegocio[] obtenerSucursalesxCompania(String sCodsuc){
		Unegocio[] unegocio = null;

		try{
			
			String sql = "from Unegocio as u where u.id.codcomp = '" + sCodsuc.trim() + "' and u.id.tipo = 'BS' order by codunineg";
			
			List<Unegocio> lstResultado = ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, Unegocio.class, false);
			
			if( lstResultado == null  || lstResultado.isEmpty() )
				return null;
			
			unegocio =  new Unegocio[lstResultado.size()];
			unegocio = lstResultado.toArray(unegocio);
			
			
		}catch(Exception ex){
			 LogCajaService.CreateLog("obtenerSucursalesxCompania", "ERR", ex.getMessage());
		} 
		
		
		return unegocio;
	}
/*****************************************************************************************************************/
	public Unegocio[] obtenerUninegxSucursal(String sCodSuc){
		Unegocio[] unegocio = null;
		 
		try{
			
			String sql = "from Unegocio as u where u.id.codsuc = '"+sCodSuc+"' and u.id.tipo in ('IS','EX')";
			sql+= " and (select count(*) from Vf0901 f09 ";
			sql+= " where trim(f09.id.gmmcu) = trim(u.id.codunineg) and f09.id.gmpec not in ('I','N')  ) >0 order by codunineg ";
			
			List<Unegocio> unidadesNeg = ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, Unegocio.class, false);
			
			if( unidadesNeg == null || unidadesNeg.isEmpty() )
				return null;
			
			unegocio =  new Unegocio[unidadesNeg.size()];
			unegocio = unidadesNeg.toArray(unegocio);
			
		}catch(Exception ex){
			ex.printStackTrace(); 
		} 
		
		return unegocio;
	}
}

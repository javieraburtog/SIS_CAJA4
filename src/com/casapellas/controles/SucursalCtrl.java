package com.casapellas.controles;
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

import com.casapellas.entidades.TallerUn;
import com.casapellas.entidades.Unegocio;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.navegacion.As400Connection;
import com.casapellas.util.PropertiesSystem;

public class SucursalCtrl {
/*********************************************************************************************/
	public int existeContrato(String sCodcomp,String sCodsuc,int iCodcli, String sNocontrato){
		int iNocontrato = 0;
		Connection cn = null;
		PreparedStatement ps = null;
		String sql = "select numctto from QS36F.sotmpc where compancto = '"
				+ sCodcomp + "' and sucurscto = '" + sCodsuc
				+ "' and cliente = " + iCodcli
				+ " and trim(status) in ('001','002') and numctto = "
				+ sNocontrato;
		try{
			cn = As400Connection.getJNDIConnection("DSMCAJAR");
			ps = cn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if (rs.next()){
				iNocontrato = rs.getInt("numctto");
			}
		}catch(Exception ex){
			iNocontrato = 0;
			System.out.println("Se capturo una excepcion en SucursalCtrl.existeContrato: " + ex);
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
			s.close();
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
			session.close();
		}
		return sNombreSucursal;
	}
/*********Obtiene una lista de sucursales por codigo de compania**************************************************/	
	public List<String[]> obtenerSucursalesxCompania(String sCodsuc){
		List<String[]>  lstResultado = null;

		try{
			
			String sql = "SELECT * FROM "+ PropertiesSystem.ESQUEMA +".VSUCURSALES WHERE TRIM(CODCOMP) =" + sCodsuc.trim() +" order by CODSUC";
			
			lstResultado = ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, null, true);
			
			if( lstResultado == null  || lstResultado.isEmpty() )
				return null;
		
			
		}catch(Exception ex){
			 ex.printStackTrace(); 
		} 
		
		
		return lstResultado;
	}
	
	/*****************************************************************************************************************/
	public 	List<String[]> obtenerUninegxSucursal(String sCodSuc, String sCodComp){
		List<String[]> unegocio = null;
		 
		try{
			String sql = "SELECT * FROM  "+ PropertiesSystem.ESQUEMA +".VUNINEGOCIOXSUCURSAL WHERE TRIM(CODSUC ) ='" + sCodSuc.trim() + "' AND TRIM(CODCOMP) = '" + sCodComp.trim() + "' order by CODUNINEG";
			
			unegocio = ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, null, true);
			
			if( unegocio == null || unegocio.isEmpty() )
				return null;
			
		}catch(Exception ex){
			ex.printStackTrace(); 
		} 
		
		return unegocio;
	}
	
/*****************************************************************************************************************/
	

}

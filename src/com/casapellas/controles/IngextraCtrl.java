package com.casapellas.controles;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

import com.casapellas.entidades.Vcuentaoperacion;
import com.casapellas.entidades.Vf0901;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.PropertiesSystem;

/**
 * CASA PELLAS S.A.
 * Creado por.........: Carlos Manuel Hernández Morrison
 * Fecha de Creación..: 22/01/2010
 * Última modificación: 23/01/2010
 * Modificado por.....:	Carlos Manuel Hernández Morrison.
 * 
 */

public class IngextraCtrl {
/********************************************************************************************/
/**	 		Validar que exista la cuenta especificada por UN, ob, sub en F0901		       **/
	public Vf0901 validarCuentaF0901(String sUN, String sCobj, String sCsub){
		Vf0901 v = new Vf0901();
		Session sesion = HibernateUtilPruebaCn.currentSession();
		
		String sql;
		
		try {
			sql  = " from Vf0901 v where trim(v.id.gmmcu) = '"+sUN.trim()+"' ";
			sql += " and  trim(v.id.gmobj) = '"+sCobj+"' and trim(v.id.gmsub) = '"+sCsub+"'";
			sql += " and trim(v.id.gmpec) not in ('N', 'M') ";
			
		
			Object ob  = sesion.createQuery(sql).setMaxResults(1).uniqueResult();
		
			
			v = ob!=null?(Vf0901)ob: null;
			
		} catch (Exception error) {
			v = null;
			
			LogCajaService.CreateLog("Error en IngextraCtrl.validarCuentaF0901 ", "ERR", error.getMessage());
		}
		return v;
	}
	
/********************************************************************************************/
/**	 	Obtener una lista con cuentas del f0901 a partir de filtros UN, Cobj y Csub	       **/
	public List filtrarCuentasF0901(String sUN,String sCobj,String sCsub){
		List lstCuentas = new ArrayList();
		Session sesion = HibernateUtilPruebaCn.currentSession();
		
		String sql = "";
		
		try {
			sql = "from Vf0901 v where v.id.gmpec not in ('N','M') ";
			
			if(!sUN.equals(""))
				sql += " and trim(v.id.gmmcu) = '"+sUN.trim()+"'";
			if(!sCobj.equals(""))
				sql += " and trim(v.id.gmobj) like '"+sCobj.trim()+"%'";
			if(!sCsub.equals(""))
				sql += " and trim(v.id.gmsub) like '"+sCsub.trim()+"%'";
			
		
			lstCuentas = sesion.createQuery(sql).setMaxResults(40).list();
		
			
		} catch (Exception error) {
		
			LogCajaService.CreateLog("Error en IngextraCtrl.filtrarCuentasF0901 ", "ERR", error.getMessage());
		} finally {
			try {
				sesion.close();
			} catch (Exception e) {
				
				LogCajaService.CreateLog("Error al cerrar sesion en IngextraCtrl.filtrarCuentasF0901", "ERR", e.getMessage());
			}
		}
		return lstCuentas;		
	}
/********************************************************************************************/
/** 	 Obtiene la lista de cuentas de pago  en f0901 para operación g pura			   **/
	public List<Vcuentaoperacion> obtenerCuentasF0901(String sMoneda, int iTo, String sUnineg){
		List<Vcuentaoperacion> lstCuentas = null;
		 
		try {
	
			String sql = 
			   " from Vcuentaoperacion v where v.id.d6crcd = '"+sMoneda+"' and v.id.d5tid = "+ iTo  
			 + " and trim(v.id.gmmcu) = (case when length(trim(v.id.gmmcu)) = 2 then substring('"+sUnineg+"',0,3) else '"+sUnineg+"' end ) "  
			 + " and v.id.gmpec not in ('N','M')";
 
			lstCuentas = ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, Vcuentaoperacion.class, false);
			
		} catch (Exception error) {
			LogCajaService.CreateLog("obtenerCuentasF0901", "ERR", error.getMessage());
		}  
		return lstCuentas;
	}
	
/********************************************************************************************/
/**	 		Obtiene la lista de cuentas de pago asociadas a un tipo de operación		   **/
	public List obtenerCuentasxOperacion(int iTo, String sMoneda){
		List lstCto = new ArrayList();
		Session sesion = HibernateUtilPruebaCn.currentSession();
		
		String sql = "";
		
		try {
			sql = "from Vcuentaoperacion v where v.id.d5tid = "+iTo+" and v.id.d6crcd = '"+sMoneda+"'";
			
			lstCto = sesion.createQuery(sql).list();
						
		} catch (Exception error) {
			
			LogCajaService.CreateLog("Error en IngextraCtrl.obtenerCuentasxOperacion", "ERR", error.getMessage());
		} finally {
			try {
				sesion.close();
			} catch (Exception e) {
			
				LogCajaService.CreateLog("Error al cerrar sesion en IngextraCtrl.obtenerCuentasxOperacion  ", "ERR", e.getMessage());
			}
		}
		return lstCto;
	}
/********************************************************************************************/
/**	 Obtiene la lista de tipo de operaciones en concepto de ingresos extraordinarios	   **/
	public List<Object[]> obtenerTipoOperaciones(){
		List<Object[]> lstTiposOperacion = null;
		String sql = "";
		
		try {
			
			sql = "SELECT D5TID,CAST(D5DESC AS VARCHAR(45) CCSID 37) D5DESC, CAST(D5RMRK AS VARCHAR(70) CCSID 37) D5RMRK FROM "+PropertiesSystem.ESQUEMA+".F55CA025 ";
			
			lstTiposOperacion = ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, null, true);
			
			
		} catch (Exception error) {
			LogCajaService.CreateLog("obtenerTipoOperaciones", "ERR", error.getMessage());
		}  
		return lstTiposOperacion;
	}
}

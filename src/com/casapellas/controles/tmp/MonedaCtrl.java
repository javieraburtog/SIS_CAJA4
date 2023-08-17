package com.casapellas.controles.tmp;
/**
 * CASA PELLAS S.A.
 * Creado por.........: Juan Carlos Ñamendi Pineda
 * Fecha de Creación..: 28/02/2009
 * Última modificación: 06/04/2010
 * Modificado por.....:	Carlos Manuel Hernández Morrison.
 * 
 */
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.casapellas.controles.ConsolidadoDepositosBcoCtrl;
import com.casapellas.entidades.F55ca014;
import com.casapellas.entidades.F55ca019;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.PropertiesSystem;

public class MonedaCtrl {
	
	/********************************************************************/
	/** Método: Lee la moneda configurada para la linea de negocios.
	 *	Fecha:  23/02/2013
	 *  Nombre: Carlos Manuel Hernández Morrison.
	 *******************************************************************/
	@SuppressWarnings("unchecked")
	public static String leerMonedaxUnegocio(String sLineaNegocio){
		String sMoneda="";
		Session sesion = null;
		try {
			sesion = HibernateUtilPruebaCn.currentSession();
			
			List<F55ca019> lstMonds = (ArrayList<F55ca019>) 
					sesion.createCriteria(F55ca019.class)
						.add(Restrictions.eq("id.castat", "A"))
						.add(Restrictions.sqlRestriction("trim(camcul) = '"
							+sLineaNegocio+"'")).setMaxResults(1).list();
			
			for (F55ca019 f19 : lstMonds) 
				sMoneda = f19.getId().getCacrcd().trim();
			
			lstMonds = null;
			
		} catch (Exception error) {
			error.printStackTrace();
			sMoneda = "";
		}finally{
			try {HibernateUtilPruebaCn.closeSession(sesion); } catch (Exception e2) {e2.printStackTrace(); }
		}
		return sMoneda;
	}
	
	
	public static List<String[]> obtenerMonedasCajaJde(){
		List<String[]> monedasCaja = null;
		try {
			
			String query = " select cvcrcd, cvdl01 from "+PropertiesSystem.JDEDTA+".f0013 m " +
					"where m.cvcrcd in ( select c3crcd from "+PropertiesSystem.ESQUEMA+".f55ca013  where  c3stat = 'A' ) " ;
 
			List<Object[]> dtaMonedas = ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
			
			if(dtaMonedas == null)
				return null;
			
			monedasCaja = new ArrayList<String[]>() ;
			 
			for (Object[] objects : dtaMonedas) {
				
				monedasCaja.add(new String[]{String.valueOf(objects[0]), String.valueOf(objects[1])}) ;
				
			}
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return monedasCaja ;
	}
	
	
/********************************************************************/
/** Método: Obtener las monedas configuradas en JDE
 *	Fecha:  23/08/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 *******************************************************************/
	@SuppressWarnings("unchecked")
	public static List<String[]> obtenerMonedasJDE(){
		List<String[]>lstMonedasJDE = new ArrayList<String[]>();
		Session sesion = null;
		
		String sql = "";
		
		try {
			sesion = HibernateUtilPruebaCn.currentSession();
			
			sql = "select CAST(cvcrcd AS VARCHAR(3) CCSID 37)  AS CVCRCD,";
			sql+= " CAST(CVDL01 AS VARCHAR(30) CCSID 37 ) AS CVDL01	From "+PropertiesSystem.JDEDTA+".f0013";
			sql+= " where cvcrcd<>'' and CVDL01<>'' ";
			
			
			List<Object[]> lstMonedas = sesion.createSQLQuery(sql).list();
			
			
			if(lstMonedas != null && lstMonedas.size()>0){
				for (Object[] sF13 : lstMonedas) {
					String[] sMoneda = new String[2];
					sMoneda[0] = String.valueOf(sF13[0]).trim();
					sMoneda[1] = String.valueOf(sF13[1]).trim();
					lstMonedasJDE.add(sMoneda);
				}
			}
		} catch (Exception e) {
			lstMonedasJDE = null;
			e.printStackTrace();
		}finally{
			try {HibernateUtilPruebaCn.closeSession(sesion); } catch (Exception e2) {e2.printStackTrace(); }
		}
		return lstMonedasJDE;
	}
/**********************OBTIENE LOS CODIGOS DE MONEDAS POR CAJA CONFIGURADAS EN F55CA012**********************************************/	
	public static String[] obtenerMonedasxCajaCompania(int iCaid,String sCodComp){
		String sql="";
		List lstMonedas = null;
		Session session = null;
		String[] sCodMod = null;
		
		try{
			session = HibernateUtilPruebaCn.currentSession();
			
			sql = "select distinct f.id.c2crcd from F55ca012 as f where f.id.c2rp01 = '"+sCodComp+"' and f.id.c2stat = 'A'";
			if(iCaid > 0)
				 sql += " and f.id.c2id = "+iCaid;
			
			lstMonedas = (List)session.createQuery(sql).list();
			if(lstMonedas!=null && lstMonedas.size()>0){
				sCodMod = new String[lstMonedas.size()];
				for(int i = 0; i < lstMonedas.size(); i++)
					sCodMod[i] = (String)lstMonedas.get(i);
			}
			
		}catch(Exception ex){
			lstMonedas = null;
			System.out.print("Excepcion capturada en MonedaCtrl.obtenerMonedasxCaja: " + ex);
		}finally {			
			try {HibernateUtilPruebaCn.closeSession(session); } catch (Exception e2) {e2.printStackTrace(); }
		}
		return sCodMod;
	}
/**********************OBTIENE LOS CODIGOS DE MONEDAS POR CAJA CONFIGURADAS EN F55CA012**********************************************/	
	@SuppressWarnings("unchecked")
	public String[] obtenerMonedasxCaja(int iCaid,String sCodComp){
		List<String> lstMonedas = null;
		Session session = null;
		String[] sCodMod = null;
		boolean bNvaSesionCaja = false;
	
		try{
			session = HibernateUtilPruebaCn.currentSession();	
			if( !session.getTransaction().isActive()){
				session.beginTransaction();
				bNvaSesionCaja = true;
			}
			
			String sql = "select distinct f.id.c2crcd from F55ca012 as f" +
					" where f.id.c2id = " + iCaid + " and f.id.c2rp01 = '"
					+ sCodComp + "' and f.id.c2stat = 'A'";
			
			lstMonedas = (List<String>) session.createQuery(sql).list();
			
			
			if(lstMonedas != null && !lstMonedas.isEmpty())
				sCodMod = (String[])lstMonedas.toArray(
								new String[lstMonedas.size()]);
			
		}catch(Exception ex){
			sCodMod = null;
			System.out.print("Excepcion capturada en MonedaCtrl.obtenerMonedasxCaja: " + ex);
			ex.printStackTrace();
		}finally {			
			try {
				if(bNvaSesionCaja)
					HibernateUtilPruebaCn.closeSession(session);
			} catch (Exception e2) {e2.printStackTrace(); }
		}
		return sCodMod;
	}
/**********************************************************************************************************************/
/********OBTIENE LOS CODIGOS DE MONEDAS POR CAJA Y METODOS DE PAGO CONFIGURADAS EN F55CA012**********************************************/
	@SuppressWarnings("unchecked")
	public static String[] obtenerMonedasxMetodosPago_Caja(int iCaid,String sCodComp,String sCodMet){
		String[] sCodMod = null;
		
		try{
			 
			String sql = "select distinct f.id.c2crcd from F55ca012 as f where f.id.c2id = "+iCaid +" and f.id.c2rp01 = '"+sCodComp+"' and f.id.c2ryin = '"+sCodMet+"' and f.id.c2stat = 'A'";
			
			LogCajaService.CreateLog("obtenerMonedasxMetodosPago_Caja", "QRY", sql);
			
			List<Object> lstMonedas = (ArrayList<Object>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, false, null);
			
			if(lstMonedas == null || lstMonedas.isEmpty() )
				return null;
			
			sCodMod = new String[lstMonedas.size()];
			for(int i = 0; i < lstMonedas.size(); i++){
				sCodMod[i] = String.valueOf( lstMonedas.get(i) ) ; 
			}
			
			
		}catch(Exception ex){
			LogCajaService.CreateLog("obtenerMonedasxMetodosPago_Caja", "ERR", ex.getMessage());			
		} 
		return sCodMod;
	}
/**OBTIENE LAS MONEDAS CONFIGURADAS EN UNA CAJA PARA LA LISTA DE COMPANIAS DADAS****************************************************************/
	@SuppressWarnings("rawtypes")
	public static String[] obtenerMonedasxCaja_Companias(int iCaId, F55ca014[] f55ca014){
		String[] sMonedas = null;
		Session session = null;
		List lstMonedas = null;
		
		try{
			session = HibernateUtilPruebaCn.currentSession();

			String sql = "select distinct f.id.c2crcd from F55ca012 as f where f.id.c2id = "+iCaId +" and f.id.c2stat = 'A' and f.id.c2rp01 in (";
			for (int i = 0; i < f55ca014.length; i++){
				if (i == f55ca014.length - 1){
					sql = sql + "'" + f55ca014[i].getId().getC4rp01().trim() + "'";
				}else{
					sql = sql + "'" +  f55ca014[i].getId().getC4rp01().trim() + "',";
				}
			}
			sql = sql + ")";
			
			LogCajaService.CreateLog("obtenerMonedasxCaja_Companias", "QRY", sql);
			
			lstMonedas = (List)session
						.createQuery(sql)			
						.list();
			sMonedas = new String[lstMonedas.size()];
			for(int i = 0; i < lstMonedas.size(); i++){
				sMonedas[i] = (String)lstMonedas.get(i); 
			}
		}catch(Exception ex){
			LogCajaService.CreateLog("obtenerMonedasxCaja_Companias", "ERR", ex.getMessage());
			ex.printStackTrace();
		}

		return sMonedas;
	}
/***********************************************************************************************************/
/** Obtiene la moneda configurada para pagos en una unidad de negocio.									  **/
	public String obtenerMdaUnineg(String sLineaNegocio){
		String sMoneda="",sConsulta;
		Session sesion = null;
		
		try {
			sesion = HibernateUtilPruebaCn.currentSession();
			
			sConsulta = " select distinct(f.id.cacrcd) from F55ca019 f where f.id.castat='A'";
			sConsulta += " and trim(f.id.camcul)='"+sLineaNegocio+"'";
			
			Object ob = sesion.createQuery(sConsulta).uniqueResult();
			
			sMoneda = ob!=null? ob.toString():"";
			
		} catch (Exception error) {
			error.printStackTrace();
			sMoneda="";
		} finally {
			try {HibernateUtilPruebaCn.closeSession(sesion); } catch (Exception e2) {e2.printStackTrace(); }
		}
		return sMoneda;
	}
}

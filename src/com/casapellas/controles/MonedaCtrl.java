package com.casapellas.controles;
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
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.casapellas.entidades.F55ca012;
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
		
		try {
			Session sesion = HibernateUtilPruebaCn.currentSession();
			
			List<F55ca019> lstMonds = (ArrayList<F55ca019>) 
					sesion.createCriteria(F55ca019.class)
						.add(Restrictions.eq("id.castat", "A"))
						.add(Restrictions.sqlRestriction("trim(camcul) = '"
							+sLineaNegocio+"'")).setMaxResults(1).list();
			
			for (F55ca019 f19 : lstMonds) 
				sMoneda = f19.getId().getCacrcd().trim();
			
			
			lstMonds = null;
			
		} catch (Exception error) {
			System.out.println("Error en MonedaCtrl.obtenerMdaUnineg() " + error);
			sMoneda = "";
		} 
		return sMoneda;
	}
/********************************************************************/
/** Método: Obtener las monedas configuradas en JDE
 *	Fecha:  23/08/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 *******************************************************************/
	
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
	
	public static List<String[]> leerMonedasJde(){
		List<String[]>lstMonedasJDE = null;

		try {
			
			String sql =  "select CAST(cvcrcd AS VARCHAR(3) CCSID 37)  AS CVCRCD,";
			sql+= " CAST(CVDL01 AS VARCHAR(30) CCSID 37 ) AS CVDL01	From "+PropertiesSystem.JDEDTA+".f0013";
			sql+= " where cvcrcd<>'' and CVDL01<>'' ";
			
			@SuppressWarnings("unchecked")
			List<Object[]> lstMonedas =  (ArrayList<Object[]>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, true, null);
			
			if(lstMonedas == null || lstMonedas.isEmpty() )
				return null;
			
			lstMonedasJDE = new ArrayList<String[]>();			
			
			for (Object[] sF13 : lstMonedas) {
				String[] sMoneda = new String[2];
				sMoneda[0] = String.valueOf(sF13[0]).trim();
				sMoneda[1] = String.valueOf(sF13[1]).trim();
				lstMonedasJDE.add(sMoneda);
			}
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return lstMonedasJDE;
	}
	
	
	@SuppressWarnings("unchecked")
	public static List<String[]> obtenerMonedasJDE(){
		List<String[]>lstMonedasJDE = new ArrayList<String[]>();
		Session sesion = null;
		Transaction trans = null;
		boolean newCn = false;
		
		try {
			
			sesion = HibernateUtilPruebaCn.currentSession();
			trans = (newCn = !(sesion.getTransaction().isActive())) ? sesion
					.beginTransaction() : sesion.getTransaction();
			
			String sql =  "select CAST(cvcrcd AS VARCHAR(3) CCSID 37)  AS CVCRCD,";
			sql+= " CAST(CVDL01 AS VARCHAR(30) CCSID 37 ) AS CVDL01	From "+PropertiesSystem.JDEDTA+".f0013";
			sql+= " where cvcrcd<>'' and CVDL01<>'' ";
			
			List<String[]>dta = new ArrayList<String[]>();
			dta.add(new String[]{"cvcrcd","3"});
			String sqlOrCtaConc = ConfirmaDepositosCtrl.constructSqlOrCtaxCon(dta);
			if( !sqlOrCtaConc.isEmpty() ){
				sql += " and "+ sqlOrCtaConc;
			}
			
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
			e.printStackTrace(); 
		}finally{
			if(newCn){
				try {  trans.commit(); } 
				catch (Exception e2) { }
				try {  HibernateUtilPruebaCn.closeSession(sesion); }
				catch (Exception e2) { }
			}
			trans = null;
			sesion = null;

		}
		return lstMonedasJDE;
	}
/**********************OBTIENE LOS CODIGOS DE MONEDAS POR CAJA CONFIGURADAS EN F55CA012**********************************************/	
	public String[] obtenerMonedasxCajaCompania(int iCaid,String sCodComp){
		Session sesion = null;
		Transaction tx = null;
		String[] sCodMod = null;
		boolean newCn = false;
		
		try{
			
			sesion = HibernateUtilPruebaCn.currentSession();
			tx = (newCn = !(sesion.getTransaction().isActive())) ? sesion
					.beginTransaction() : sesion.getTransaction();
			
			Criteria cr = sesion
					.createCriteria(F55ca012.class)
					.add(Restrictions.eq("id.c2rp01", sCodComp))
					.add(Restrictions.eq("id.c2stat", 'A'))
					.setProjection(Projections.distinct(Projections
						.property("id.c2crcd")));
			if(iCaid > 0)
				cr.add(Restrictions.eq("id.c2id", iCaid));
			
			@SuppressWarnings("unchecked")
			List<String>monedas = (ArrayList<String>)cr.list();
			if(monedas == null || monedas.isEmpty())
				return null;
			
			sCodMod = new String[monedas.size()];
			monedas.toArray(sCodMod) ;
			
		}catch(Exception ex){
			ex.printStackTrace(); 
		}finally {			
			if(newCn && tx != null && tx.isActive() ){
				try {  tx.commit(); } 
				catch (Exception e2) { }
				try {  HibernateUtilPruebaCn.closeSession(sesion); }
				catch (Exception e2) { }
			}
		}
		return sCodMod;
	}
/**********************OBTIENE LOS CODIGOS DE MONEDAS POR CAJA CONFIGURADAS EN F55CA012**********************************************/	
	public static String[] obtenerMonedasxCaja(int iCaid,String sCodComp){
 
		String[] sCodMod = null;
		try{
		 					
			String sql = "select distinct f.id.c2crcd from F55ca012 as f where f.id.c2id = "+iCaid +" and f.id.c2rp01 = '"+sCodComp+"' and f.id.c2stat = 'A'";
			List<Object> lstMonedas =  ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, null, false);
			
			sCodMod = new String[lstMonedas.size()];
			for(int i = 0; i < lstMonedas.size(); i++){
				sCodMod[i] = (String)lstMonedas.get(i); 
			}
		}catch(Exception ex){
			sCodMod = null;
			ex.printStackTrace(); 
		} 	
			 
		 
		return sCodMod;
	}
/**********************************************************************************************************************/
/********OBTIENE LOS CODIGOS DE MONEDAS POR CAJA Y METODOS DE PAGO CONFIGURADAS EN F55CA012**********************************************/
	public String[] obtenerMonedasxMetodosPago_Caja(int iCaid,String sCodComp,String sCodMet){
		String[] sCodMod = null;
		
		try{
			
			String sql = "select distinct f.id.c2crcd from F55ca012 as f where f.id.c2id = "+iCaid +" and f.id.c2rp01 = '"+sCodComp+"' and f.id.c2ryin = '"+sCodMet+"' and f.id.c2stat = 'A'";
			List<Object> lstMonedas = ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, null, false);
			
			LogCajaService.CreateLog("obtenerMonedasxMetodosPago_Caja", "QRY", sql);
			
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
	public String[] obtenerMonedasxCaja_Companias(int iCaId, F55ca014[] f55ca014){
		String[] sMonedas = null;
		Session session = HibernateUtilPruebaCn.currentSession();
		
		List lstMonedas = null;
		try{
											
			String sql = "select distinct f.id.c2crcd from F55ca012 as f where f.id.c2id = "+iCaId +" and f.id.c2stat = 'A' and f.id.c2rp01 in (";
			for (int i = 0; i < f55ca014.length; i++){
				if (i == f55ca014.length - 1){
					sql = sql + "'" + f55ca014[i].getId().getC4rp01().trim() + "'";
				}else{
					sql = sql + "'" +  f55ca014[i].getId().getC4rp01().trim() + "',";
				}
			}
			sql = sql + ")";
			lstMonedas = (List)session
						.createQuery(sql)			
						.list();
		
			sMonedas = new String[lstMonedas.size()];
			for(int i = 0; i < lstMonedas.size(); i++){
				sMonedas[i] = (String)lstMonedas.get(i); 
			}
		}catch(Exception ex){
			System.out.println("Se capturo una excepcion en MonedaCtrl.obtenerMonedasxCaja_Companias: " + ex);
		}
		return sMonedas;
	}
/***********************************************************************************************************/
/** Obtiene la moneda configurada para pagos en una unidad de negocio.									  **/
	public String obtenerMdaUnineg(String sLineaNegocio){
		String sMoneda="",sConsulta;
		Session sesion = HibernateUtilPruebaCn.currentSession();
		Transaction trans = null;
		
		try {
			sConsulta = " select distinct(f.id.cacrcd) from F55ca019 f where f.id.castat='A'";
			sConsulta += " and trim(f.id.camcul)='"+sLineaNegocio+"'";
			
			
			Object ob = sesion.createQuery(sConsulta).uniqueResult();
						
			sMoneda = ob!=null? ob.toString():"";
			
		} catch (Exception error) {
			System.out.println("Error en MonedaCtrl.obtenerMdaUnineg() " + error);
			sMoneda="";
		} finally {
			sesion.close();
		}
		return sMoneda;
	}
	
	public String[] obtenerMonedasxMetodosPago_CajaV2(int iCaid,String sCodComp){
		String[] sCodMod = null;
		
		try{
			
			String sql = "select distinct f.id.c2crcd from F55ca012 as f where f.id.c2id = "+iCaid +" and f.id.c2rp01 = '"+sCodComp+"' and f.id.c2stat = 'A'";
			List<Object> lstMonedas = ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, null, false);
			
			LogCajaService.CreateLog("obtenerMonedasxMetodosPago_Caja", "QRY", sql);
			
			if(lstMonedas == null || lstMonedas.isEmpty() )
				return null;
			
			sCodMod = new String[lstMonedas.size()];
			for(int i = 0; i < lstMonedas.size(); i++){
				sCodMod[i] = String.valueOf( lstMonedas.get(i) ) ; 
			}
			
			
		}catch(Exception ex){
			ex.printStackTrace(); 
			LogCajaService.CreateLog("obtenerMonedasxMetodosPago_Caja", "ERR", ex.getMessage());
		} 
		return sCodMod;
	}
}

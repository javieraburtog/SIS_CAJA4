package com.casapellas.controles;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.casapellas.entidades.F55ca014;
import com.casapellas.entidades.Monedaxlinea;
import com.casapellas.entidades.Unegocio;
import com.casapellas.entidades.Vcompania;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.util.AllConectionMngt;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.PropertiesSystem;

public class CompaniaCtrl {
	
	public static F55ca014[] obtenerCompaniasConciliarxCaja(int iCaId){
		F55ca014[] f55ca014 = null;	
		Session sesion = null;
		boolean newCn = false;

		try {

			sesion = HibernateUtilPruebaCn.currentSession();
			
			String sql = "from F55ca014 f where f.id.c4id = "+iCaId ;
			
			List<String[]>dta = new ArrayList<String[]>();
			dta.add(new String[]{"id.c4rp01","2"});
			
			String sqlOrCtaConc = ConfirmaDepositosCtrl.constructSqlOrCtaxCon(dta);
			if( !sqlOrCtaConc.isEmpty() ){
				sql += " and "+ sqlOrCtaConc;
			}		
					
			@SuppressWarnings("unchecked")
			List<F55ca014> result = (ArrayList<F55ca014>) sesion
						.createQuery(sql).list();

			if(result == null || result.isEmpty())
				return null;
			
			f55ca014 = result.toArray(new F55ca014[result.size()]);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return f55ca014;
	}
	
	public static String leerUnidadNegocioPorLineaSucursal(String codsuc, String linea) {
		String codunineg = "";
		
		
		try {

			
			String sql = "SELECT mcmcu "
					+ " FROM "+ PropertiesSystem.JDEDTA	+ ".F0006 "
					+ " WHERE TRIM(CAST(mcco AS VARCHAR(5))) = '"+ codsuc.trim()+"'"
					+ " AND TRIM(CAST(mcrp10 AS VARCHAR(3))) = '" +  linea.trim()+"'";
			

			List<String> lstUnineg = ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, null, true);
			
			if(lstUnineg == null || lstUnineg.isEmpty() )
				return codunineg = "" ;
			
			codunineg = lstUnineg.get(0).toString().trim();

		} catch (Exception e) {		
			LogCajaService.CreateLog("leerUnidadNegocioPorLineaSucursal", "ERR", e.getMessage());
			return codunineg = "" ;
		}
		return codunineg;
	}
	
	
	@SuppressWarnings("unchecked")
	public static String obtenerMonedaLineaN(String sLineaNeg){
		Session sesion = null;
		String sMndxln = new String();
		
		try {
			
			if(sLineaNeg.compareTo("") == 0) return "";

			sesion = HibernateUtilPruebaCn.currentSession();
			sLineaNeg = sLineaNeg.trim();

			List<Monedaxlinea> lstMndxln = sesion
					.createCriteria(Monedaxlinea.class)
					.add(Restrictions.eq("lineaneg", sLineaNeg))
					.setMaxResults(1).list();
			
			if(lstMndxln != null && !lstMndxln.isEmpty())
				sMndxln = lstMndxln.get(0).getMoneda();
			
		} catch (Exception e) {
			sMndxln = "";
			e.printStackTrace();
		}
		return sMndxln;
	}
	
	
	@SuppressWarnings("unchecked")
	public static String leerLineaNegocio(String sCodunineg){
		String sql = "";
		Session sesion = null;
		String linea = new String();
		
		try {
			sCodunineg = sCodunineg.trim();
			sql =" select  cast(mcrp07 as varchar(6) ccsid 37 )" +
					" from "+PropertiesSystem.JDEDTA+".f0006 where trim( cast(mcmcu " +
					" as varchar(12) ccsid 37) ) =:CODUNINEG";
			
			sesion = HibernateUtilPruebaCn.currentSession();
			Query q = sesion.createSQLQuery(sql);
			q.setParameter("CODUNINEG", sCodunineg);
			
			List<String> lstLinea = q.list();
			if(lstLinea != null && !lstLinea.isEmpty())
				linea = lstLinea.get(0);
			
		} catch (Exception e) {
			linea = "";
			e.printStackTrace();
		}
		return linea;
	}
	
/******************************************************************************/
/** Método: Obtener la moneda base de la compania.
 *	Fecha:  01/09/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 **/	
	public static String  obtenerMonedaBasexComp(String sCodcomp){
		String sMonedaBase = "";
		Session sesion = null;
		
		boolean newCn = false;
		
		try {
			
			sesion = HibernateUtilPruebaCn.currentSession();
			
			Criteria cr = sesion.createCriteria(F55ca014.class);
			cr.setProjection(Projections.projectionList()
								.add(Projections.distinct(Projections.property("id.c4rp01")))
								.add(Projections.property("id.c4bcrcd")));
			cr.add(Restrictions.eq("id.c4rp01", sCodcomp));
			Object[] ob = (Object[])cr.uniqueResult();
			
			if(ob!=null)
				sMonedaBase = (String.valueOf(ob[1]));
			
			
		} catch (Exception e) {
			sMonedaBase = "";
//			System.out.println(".CompaniaCtrl(): Excepción capturada en: obtenerMonedaBasexComp() "+e);
			e.printStackTrace();
		}
		return sMonedaBase;
	}
/******************************************************************************/
/** Método: Obtener todas las companias configuradas en jde.
 *	Fecha:  23/08/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 **/	
	public static List<Vcompania> obtenerCompaniasCajaJDE(){
		List<Vcompania> lstCompanias = null;
		
		try {
			String sql = "from Vcompania v where v.id.drky in (select distinct(f.id.c4rp01) from F55ca014 f)"; 
			
			return lstCompanias = ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, Vcompania.class, false);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return lstCompanias;
	}
	/****************OBTENER COMPANIAS POR CODIGO DE COMPANIAS DEL F0005***********************************************/
	public String obtenerCompaniaxCodigo(String sCodComp){
	
//		Session session = HibernateUtilPruebaCn.currentSessionENS();
		Session session = HibernateUtilPruebaCn.currentSession();
				
		String compania = new String();
				
		try{
						
			
			Vcompania vcompania =  (Vcompania)session
			.createQuery("from Vcompania c where trim(c.id.drky) = :pCodComp")
			.setParameter("pCodComp", sCodComp.trim())
			.uniqueResult();
			
			
			if(vcompania != null)
				compania = vcompania.getId().getDrdl01();
			
		}catch(Exception ex){
			compania = new String();
//			System.out.print("Excepcion capturada en CompaniaCtrl.obtenerCompaniasxCodigo: " + ex);
			ex.printStackTrace();
		}
		return compania;
	}
/******************************************************************************/
/** Método: Obtener los datos de una configuración de compañía por caja.
 *	Fecha:  26/10/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 **/
	
	public static F55ca014 obtenerF55ca014(int caid, String codcomp){
		Session sesion = null;		
		boolean newCn = false;
		F55ca014 f14 = null;
		
		try {
			
			sesion = HibernateUtilPruebaCn.currentSession();
			
			f14 = (F55ca014) sesion.createCriteria(F55ca014.class)
					.add(Restrictions.eq("id.c4id", caid))
					.add(Restrictions.eq("id.c4rp01", codcomp))
					.setMaxResults(1).uniqueResult();
			
		} catch (Exception e) {
			LogCajaService.CreateLog("obtenerF55ca014", "ERR", e.getMessage());
			f14 = null;
		}
		return f14;
	}

/********************************************************************************/	
	public static String sacarMonedaBase(F55ca014[] dtaCompania, final String sCodComp){
		String sMonedaBase = null;
		
		try{
			
			F55ca014 f14 = (F55ca014)
			CollectionUtils.find( Arrays.asList(dtaCompania), new Predicate(){
				@Override
				public boolean evaluate(Object o) {
					return   ((F55ca014)o).getId().getC4rp01().trim().compareTo(sCodComp) == 0 ;
				}
			});
			
			if(f14 != null){
				return sMonedaBase = f14.getId().getC4bcrcd();
			}
			 
			if( f14 == null ) {
				String query = "select  distinct( cast( C4BCRCD  as varchar(3) ccsid 37) ) from "
					+PropertiesSystem.ESQUEMA+".f55ca014 where trim(c4rp01) = '"+sCodComp.trim()+"' AND C4STAT = 'A' ";
				
				LogCajaService.CreateLog("sacarMonedaBase", "QRY", query);
				
				@SuppressWarnings("unchecked")
                List<String> monedasCompania = (List<String>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, true, null);
				
				if(monedasCompania == null || monedasCompania.isEmpty() ) {
					return sMonedaBase = "COR";
				}
				
				sMonedaBase = monedasCompania.get(0);
				
			}
			
			
		}catch(Exception ex){
			LogCajaService.CreateLog("sacarMonedaBase", "ERR", ex.getMessage());
		}
		return sMonedaBase;
	}	
/********************* OBTENER NOMBRE DE COMPAÑÍA POR CÓDIGO *****************************************/
	public String obtenerNombreComp(String sCodcomp,int caid, Session sesion,Transaction trans){
		String nombre = "",consulta ="";		
		
		
		try{
			if(sesion ==null){
				sesion = HibernateUtilPruebaCn.currentSession();				
				
			}
			consulta = "select f.id.c4rp01d1 from F55ca014 as f where f.id.c4rp01 = '"+sCodcomp+"' and f.id.c4id ='"+caid+"'";
			Object ob = sesion.createQuery(consulta).uniqueResult();
		
			if(ob!=null)
				nombre = ob.toString();
			else
				nombre = sCodcomp;
			
		}catch(Exception error){

			error.printStackTrace();
		}
		return nombre;
	}
	
/*************OBTENER COMPANIAS POR CAJA DEL F55CA014*************************************************/
	public F55ca014[] obtenerCompaniasxCaja(int iCaId){
		F55ca014[] f55ca014 = null;
		
		try{
 
			String sql = " from F55ca014 f where f.id.c4stat = 'A' and f.id.c4id = " + iCaId  ;
			
			List<F55ca014> companias = ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, F55ca014.class, false);
			
			if(companias == null || companias.isEmpty() )
				return null;
			
			f55ca014 = new F55ca014[companias.size()];
			f55ca014 = companias.toArray(f55ca014);
			
		}catch(Exception ex){
			LogCajaService.CreateLog("obtenerCompaniasxCaja", "ERR", ex.getMessage());
		} 
		return f55ca014;
	}
/*****************************************************************************************************/
/****************OBTENER COMPANIAS POR CODIGO DE COMPANIAS DEL F0005***********************************************/
	public Vcompania[] obtenerCompaniasxCodigo(String sCodComp){
		Vcompania[] vcompania = null;
		

		Session session = HibernateUtilPruebaCn.currentSession();
		
		
		
		try{			
			
			List result = (List)session
					.createQuery("from Vcompania c where c.id.drky = :pCodComp")
					.setParameter("pCodComp", sCodComp).list();
			
			
			vcompania = new Vcompania[result.size()];
			for(int i = 0; i < result.size(); i++){
				vcompania[i] = (Vcompania)result.get(i);
			}
		}catch(Exception ex){
			vcompania = null;
			ex.printStackTrace();
		}
		return vcompania;
	}
/**********OBTENER UNIDADES DE NEGOCIO POR COMPANIA****************************************************************/
	public Unegocio[] obtenerUninegxCompania(String sCodComp){
		Unegocio[] unegocio = null;
		Session session = HibernateUtilPruebaCn.currentSession();
		
		try{
		
			List result = (List)session
			.createQuery("from Unegocio u where trim(u.id.codcomp) = :pCodComp and u.id.tipo in ('IS','EX')")
			.setParameter("pCodComp", sCodComp.trim())
			.list();
		
			unegocio = new Unegocio[result.size()];
			for(int i = 0; i < result.size(); i++){
				unegocio[i] = (Unegocio)result.get(i);
			}
		}catch(Exception ex){
//			System.out.print("Excepcion capturada en CompaniaCtrl.obtenerUninegxCompania: " + ex);
			ex.printStackTrace();
		}
		return unegocio;
	}
/*************************************************************************************/
}

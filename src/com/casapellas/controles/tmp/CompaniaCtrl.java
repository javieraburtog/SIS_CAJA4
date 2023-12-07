package com.casapellas.controles.tmp;
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

import com.casapellas.controles.ConsolidadoDepositosBcoCtrl;

import com.casapellas.entidades.F55ca014;
import com.casapellas.entidades.Monedaxlinea;
import com.casapellas.entidades.Unegocio;
import com.casapellas.entidades.Vcompania;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.PropertiesSystem;

public class CompaniaCtrl {
	
	
	/** ************** filtrar la compania a utilizar de la lista de configuraciones */
	public static F55ca014 filtrarCompania(F55ca014[]compsCnfg,
								final String codcomp){
		F55ca014 dtCompany = null;
		
		try {
			dtCompany = (F55ca014) CollectionUtils.find(
					Arrays.asList(compsCnfg), new Predicate() {
						public boolean evaluate(Object o) {
							return ((F55ca014) o).getId().getC4rp01().trim()
									.compareTo(codcomp.trim()) == 0;
						}
					});
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtCompany;
	}
	/** ************** ******************************************************/
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
			LogCajaService.CreateLog("obtenerMonedaLineaN", "ERR", e.getMessage());
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
			sql =" select  cast(mcrp10 as varchar(6) ccsid 37 )" +
					" from "+PropertiesSystem.JDEDTA+".f0006 where trim( cast(mcmcu " +
					" as varchar(12) ccsid 37) ) =:CODUNINEG";
			
			sesion = HibernateUtilPruebaCn.currentSession();
			Query q = sesion.createSQLQuery(sql);
			q.setParameter("CODUNINEG", sCodunineg);
			
			LogCajaService.CreateLog("leerLineaNegocio", "QRY", sql);
			
			List<String> lstLinea = q.list();
			if(lstLinea != null && !lstLinea.isEmpty())
				linea = lstLinea.get(0);
			
		} catch (Exception e) {
			LogCajaService.CreateLog("leerLineaNegocio", "ERR", e.getMessage());
			linea = ""; 
			
		}

		return linea;
	}
	
/******************************************************************************/
/** Método: Obtener la moneda base de la compania.
 *	Fecha:  01/09/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 **/	
	public String  obtenerMonedaBasexComp(String sCodcomp){
		String sMonedaBase = "";
		Session sesion = null;
		
		try {
			sesion = HibernateUtilPruebaCn.currentSession();
			Transaction trans = sesion.beginTransaction();
			Criteria cr = sesion.createCriteria(F55ca014.class);
			cr.setProjection(Projections.projectionList()
								.add(Projections.distinct(Projections.property("id.c4rp01")))
								.add(Projections.property("id.c4bcrcd")));
			cr.add(Restrictions.eq("id.c4rp01", sCodcomp));
			Object[] ob = (Object[])cr.uniqueResult();
			
			trans.commit();
			
			if(ob!=null)
				sMonedaBase = (String.valueOf(ob[1]));
			
			
		} catch (Exception e) {
			sMonedaBase = ""; 
			e.printStackTrace();
		}finally{
			try {HibernateUtilPruebaCn.closeSession(sesion); } catch (Exception e2) {e2.printStackTrace(); }
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
			lstCompanias = null;
			e.printStackTrace(); 
		} 
		return lstCompanias;
	}
	/****************OBTENER COMPANIAS POR CODIGO DE COMPANIAS DEL F0005***********************************************/
	public String obtenerCompaniaxCodigo(String sCodComp){
		Vcompania vcompania = null;
//		Session session = HibernateUtilPruebaCn.currentSessionENS();
		Session session = HibernateUtilPruebaCn.currentSession();
		
		Transaction tx = null;
		boolean bNuevaSesionENS = false;
		
		try{
			if( session.getTransaction().isActive() )
				tx = session.getTransaction();
			else{
				tx = session.beginTransaction();
				bNuevaSesionENS = true;
			}
			
			vcompania = (Vcompania) session
					.createQuery(
							"from Vcompania c where trim(c.id.drky) = :pCodComp")
					.setParameter("pCodComp", sCodComp.trim()).uniqueResult();
		
			if(bNuevaSesionENS)
				tx.commit();
			
		}catch(Exception ex){
			vcompania = null;
		}finally {			
			try {
				if(bNuevaSesionENS){
					HibernateUtilPruebaCn.closeSession(session);
				}
			}
			catch (Exception e) { e.printStackTrace(); }
		}
		return vcompania.getId().getDrdl01();
	}
/******************************************************************************/
/** Método: Obtener los datos de una configuración de compañía por caja.
 *	Fecha:  26/10/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 **/
	public static F55ca014 obtenerF55ca014xCajaCompania(int iCaid,String sCodcomp){
		F55ca014 f14 = null;
		Session sesion = null;
		Transaction trans = null;
		Criteria cr = null;
		boolean bNvaSesionCaja = false;
		
		try {
		
			sesion = HibernateUtilPruebaCn.currentSession();
			
			if( sesion.getTransaction().isActive() )
				trans = sesion.getTransaction();
			else{
				trans = sesion.beginTransaction();
				bNvaSesionCaja = true;
			}
			
			cr = sesion.createCriteria(F55ca014.class);
			cr.add(Restrictions.eq("id.c4id", iCaid));
			cr.add(Restrictions.eq("id.c4rp01", sCodcomp));
			f14 = (F55ca014)cr.uniqueResult();
			
			if(bNvaSesionCaja)
				try { trans.commit(); } catch (Exception e2) {e2.printStackTrace(); }

		} catch (Exception error) {
			f14 = null;
			error.printStackTrace(); 
		}finally{
			try {
				if (bNvaSesionCaja)
					HibernateUtilPruebaCn.closeSession(sesion);
			} catch (Exception e2) {e2.printStackTrace(); }
		}
		return f14; 
	}
	
/********************************************************************************/	
	public String sacarMonedaBase(F55ca014[] f14,String sCodComp){
		String sMonedaBase = null;
		try{
			for(int i = 0; i < f14.length; i++){
				if(f14[i].getId().getC4rp01().trim().equals(sCodComp.trim())){
					sMonedaBase = f14[i].getId().getC4bcrcd();
					break;
				}
			}
		}catch(Exception ex){
			LogCajaService.CreateLog("sacarMonedaBase", "ERR", ex.getMessage());
		}
		return sMonedaBase;
	}	
/********************* OBTENER NOMBRE DE COMPAÑÍA POR CÓDIGO *****************************************/
	public String obtenerNombreComp(String sCodcomp,int caid, Session sesion,Transaction trans){
		String nombre = "",consulta ="";
		Transaction tx = null;
		boolean bNuevaSesionCaja = false;
		
		try{
			sesion = HibernateUtilPruebaCn.currentSession();
			if( sesion.getTransaction().isActive() )
				tx = sesion.getTransaction();
			else{
				tx = sesion.beginTransaction();
				bNuevaSesionCaja = true;
			}
			consulta = "select f.id.c4rp01d1 from F55ca014 as f where f.id.c4rp01" +
					" = '"+sCodcomp+"' and f.id.c4id ='"+caid+"'";
			Object ob = sesion.createQuery(consulta).uniqueResult();
			nombre = (ob!=null)? ob.toString() : sCodcomp;
			
		}catch(Exception error){
			error.printStackTrace(); 
		}finally{
			if(bNuevaSesionCaja){
				try{tx.commit();}catch(Exception e){e.printStackTrace();}
				try{HibernateUtilPruebaCn.closeSession(sesion);}
				catch(Exception e){e.printStackTrace();}
			}
		}	
		return nombre;
	}
	
/*************OBTENER COMPANIAS POR CAJA DEL F55CA014*************************************************/
	@SuppressWarnings("unchecked")
	public static F55ca014[] obtenerCompaniasxCaja(int iCaId){
		F55ca014[] f55ca014 = null;
		Session session = null;

		try{
			session =  HibernateUtilPruebaCn.currentSession();

			List<F55ca014> result = (List<F55ca014>) session
									.createQuery("from F55ca014 f " +
											"where f.id.c4id = "+iCaId).list();

			f55ca014 = result.toArray(new F55ca014[result.size()]);

		}catch(Exception ex){
			LogCajaService.CreateLog("obtenerCompaniasxCaja", "ERR", ex.getMessage());
		}finally {
			if(f55ca014 == null)
				f55ca014 = new F55ca014[1];
			
		}
		return f55ca014;
	}
/*****************************************************************************************************/
/****************OBTENER COMPANIAS POR CODIGO DE COMPANIAS DEL F0005***********************************************/
	public Vcompania[] obtenerCompaniasxCodigo(String sCodComp){
		Vcompania[] vcompania = null;
		Session session = HibernateUtilPruebaCn.currentSession();
		
		Transaction tx = null;
		boolean bNuevaSesionENS = false;
		
		try{
			if( session.getTransaction().isActive() )
				tx = session.getTransaction();
			else{
				tx = session.beginTransaction();
				bNuevaSesionENS = true;
			}
			
			List result = (List) session
					.createQuery("from Vcompania c where c.id.drky = :pCodComp")
					.setParameter("pCodComp", sCodComp).list();
			
			if(bNuevaSesionENS)	
				tx.commit();
			
			vcompania = new Vcompania[result.size()];
			for(int i = 0; i < result.size(); i++){
				vcompania[i] = (Vcompania)result.get(i);
			}
		}catch(Exception ex){
			vcompania = null;
			ex.printStackTrace(); 
		}finally {			
			try {
				if(bNuevaSesionENS)
					HibernateUtilPruebaCn.closeSession(session);
			}
			catch (Exception e) { e.printStackTrace(); }
		}
		return vcompania;
	}
/**********OBTENER UNIDADES DE NEGOCIO POR COMPANIA****************************************************************/
	public Unegocio[] obtenerUninegxCompania(String sCodComp){
		Unegocio[] unegocio = null;
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			List result = (List)session
			.createQuery("from Unegocio u where trim(u.id.codcomp) = :pCodComp and u.id.tipo in ('IS','EX')")
			.setParameter("pCodComp", sCodComp.trim())
			.list();
			tx.commit();
			unegocio = new Unegocio[result.size()];
			for(int i = 0; i < result.size(); i++){
				unegocio[i] = (Unegocio)result.get(i);
			}
		}catch(Exception ex){
			ex.printStackTrace(); 
		}finally {			
			try {HibernateUtilPruebaCn.closeSession(session); } catch (Exception e2) {e2.printStackTrace(); }
		}
		return unegocio;
	}
/*************************************************************************************/
}

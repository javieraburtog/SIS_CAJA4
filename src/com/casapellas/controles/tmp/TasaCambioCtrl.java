package com.casapellas.controles.tmp;
/**
 * CASA PELLAS S.A.
 * Creado por.........: Juan Carlos Ñamendi Pineda
 * Fecha de Creación..: 28/02/2009
 * Última modificación: 20/11/2009
 * Modificado por.....: Juan Carlos Ñamendi Pineda.
 * 
 */

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.casapellas.controles.ConsolidadoDepositosBcoCtrl;
import com.casapellas.entidades.F55ca021;
import com.casapellas.entidades.F55ca021Id;
import com.casapellas.entidades.Tcambio;
import com.casapellas.entidades.Tpararela;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.navegacion.As400Connection;
import com.casapellas.util.Divisas;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.PropertiesSystem;

public class TasaCambioCtrl {
	
	
	public static BigDecimal tasaCambioOficial(Date fecha, String moneda){
		BigDecimal tasaCambio = BigDecimal.ZERO;
		
		
		try {
			
			String sql = " select * from @BDCAJA.tcambio as t where t.cxcrcd = 'COR' and t.cxeft = '@FECHA' and t.crdc = '@MONEDA' fetch first rows only ";
			
			sql = sql
			.replace("@BDCAJA", PropertiesSystem.ESQUEMA)
			.replace("@FECHA",  FechasUtil.formatDatetoString(fecha, "yyyy-MM-dd") )
			.replace("@MONEDA", moneda);
			
			Tcambio tasa =  ConsolidadoDepositosBcoCtrl.executeSqlQueryUnique(sql, Tcambio.class, true);
			
			return tasaCambio = (tasa == null)?  BigDecimal.ZERO : tasa.getId().getCxcrrd() ;
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return tasaCambio;
	}
	
	
	/*************** obtener el valor de cambio entre dos monedas por fecha *********************************/
	public BigDecimal obtenerTasaJDExFecha(String sMonOrigen, String sMonDestino, Date dtFecha){
		BigDecimal dTCambio = new BigDecimal(0);
		Transaction tx = null;
		String sConsulta = "";
		SimpleDateFormat format;
		String sFecha;
		Session s = null;
		boolean bNuevaSesionENS = false;
		
		try{
//			s = HibernateUtilPruebaCn.currentSessionENS();
			s = HibernateUtilPruebaCn.currentSession();
			
			if( s.getTransaction().isActive() )
				tx = s.getTransaction();
			else{
				tx = s.beginTransaction();
				bNuevaSesionENS = true;
			}
			
			format = new SimpleDateFormat("yyyy-MM-dd");
			sFecha = format.format(dtFecha);	
			
			sConsulta =  "from Tcambio as t where t.id.cxcrcd = '"+sMonOrigen+"' and t.id.cxcrdc = '"+sMonDestino+"'";
			sConsulta += " and t.id.cxeft = '"+sFecha+"'";
						
			Object ob = s.createQuery(sConsulta).uniqueResult();
			if(ob != null)
				dTCambio = ((Tcambio)ob).getId().getCxcrr();			
			
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			
			try { 
				if( bNuevaSesionENS ) {
					tx.commit();
//					HibernateUtilPruebaCn.closeSessionENS();
					HibernateUtilPruebaCn.closeSession(s);
				}
			}
			catch (Exception e) { e.printStackTrace(); }
		}
		return dTCambio;
	}
	
/****************OBTIENE LA TASA DE CAMBIO DE JDE A LA FECHA**********************************************/
	@SuppressWarnings("rawtypes")
	public Tcambio[] obtenerTasaCambioJDExFecha(String sFecha){
		
		Session session = HibernateUtilPruebaCn.currentSession();		
		Tcambio[] tcambio = null;		
		
		try{
			
			String strQuery = "from Tcambio as t where t.id.cxcrcd = 'COR' and '"+sFecha+ "' >= t.id.cxeft and '"+sFecha+"' <= t.id.cxeft";
			LogCajaService.CreateLog("obtenerTasaCambioJDExFecha", "QRY", strQuery);
			
			List result = (List)session
			.createQuery(strQuery)
			.list();
			
			tcambio = new Tcambio[result.size()];
			for(int i = 0; i < result.size(); i++){
				tcambio[i] = (Tcambio)result.get(i);
			}
			if(result.size()==0){
				tcambio =  obtenerTasaJDEMasReciente();
			}
		}catch(Exception ex){
			LogCajaService.CreateLog("obtenerTasaCambioJDExFecha", "ERR", ex.getMessage());
		}

		return tcambio;
	}
	
	public static BigDecimal obtenerTasaOficialPorFecha(Date fecha, String strMonedaOrigen, String strMonedaDestino){
		BigDecimal bdTasaOficial = BigDecimal.ZERO;
		
		
		try {
			
			String strQuery = "select cxcrr from "+PropertiesSystem.ESQUEMA+".tcambio where cxcrcd = '"+
					strMonedaOrigen+"' and cxcrdc = '" + strMonedaDestino + "' and  cxeft = '"+ 
					FechasUtil.formatDatetoString(fecha, "yyyy-MM-dd")+"' fetch first rows only ";
			
			@SuppressWarnings("unchecked")
			List<BigDecimal> tasas = (ArrayList<BigDecimal>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(strQuery, true, null );
			
			if(tasas == null || tasas.isEmpty() )
				return BigDecimal.ZERO;
			
			bdTasaOficial = tasas.get(0).setScale(4, BigDecimal.ROUND_HALF_UP);
			
			
		} catch (Exception e) {
			e.printStackTrace(); 
			return BigDecimal.ZERO;
		}
		
		return bdTasaOficial ;
	}
	
/**************************************************************************************************************************/	
/*************** obtener el valor de cambio entre dos monedas por fecha *********************************/
	public double obtenerTasaJDExFecha(String sMonOrigen, String sMonDestino, Date dtFecha,Session sesion,Transaction trans){
		double dTCambio = 0;
		
		String sConsulta = "";
		SimpleDateFormat format;
		String sFecha;
		boolean bNuevaSesionENS = false;
		
		try{
			
			format = new SimpleDateFormat("yyyy-MM-dd");
			sFecha = format.format(dtFecha);	
			
			
			sConsulta =  "from Tcambio as t where t.id.cxcrcd = '"+sMonOrigen+"' and t.id.cxcrdc = '"+sMonDestino+"'";
			sConsulta += " and t.id.cxeft = '"+sFecha+"'";


			if(trans == null){
//				sesion = HibernateUtilPruebaCn.currentSessionENS();
				sesion = HibernateUtilPruebaCn.currentSession();			
			
			}
			
			LogCajaService.CreateLog("obtenerTasaJDExFecha", "QRY", sConsulta);
			
			Object ob = sesion.createQuery(sConsulta).uniqueResult();
			if(ob != null)
				dTCambio = ((Tcambio)ob).getId().getCxcrr().doubleValue();			
			
		}catch(Exception ex){
			LogCajaService.CreateLog("obtenerTasaJDExFecha", "ERR", ex.getMessage());
			ex.printStackTrace();
		}
		return dTCambio;
	}
	
	
	
	
/****************OBTIENE LA TASA DE CAMBIO DE JDE A LA FECHA**********************************************/
	public Tcambio[] obtenerTasaCambioJDEdelDia(){
		Session session = HibernateUtilPruebaCn.currentSession();
		
		
		Date dFecha = new Date();
		Tcambio[] tcambio = null;
		boolean bNuevaSesionENS = false;
		
		try{

			String sql = "from Tcambio as t " +
					"where t.id.cxcrcd = 'COR'  and current_date >= t.id.cxeft" +
					" and current_date <= t.id.cxeft";
			
			@SuppressWarnings("rawtypes")
			List result = (List) session.createQuery(sql).list();
			
			LogCajaService.CreateLog("obtenerTasaCambioJDEdelDia", "QRY", sql);
			
			
			tcambio = new Tcambio[result.size()];
			for(int i = 0; i < result.size(); i++){
				tcambio[i] = (Tcambio)result.get(i); 
			}
			if(result.size()==0){
				tcambio =  obtenerTasaJDEMasReciente();
			}
		}catch(Exception ex){
			LogCajaService.CreateLog("obtenerTasaCambioJDEdelDia", "ERR", ex.getMessage());
			System.out.print("Se capturo una excepcion en TasaCambioCtrl.obtenerTasaCambioJDE: " + ex);
		}
		
		return tcambio;
	}
/**************************************************************************************************************************/
/****************OBTIENE LA TASA DE CAMBIO PARALELA DE LA CONFIGURACION MAS RECIENTE***************************************/
	public Tpararela[] obtenerTasaCambioParalela(){
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = null;
		Tpararela[] tcambio = null;
		try{
			tx = session.beginTransaction();
			List result =(List) session.createQuery("from Tpararela").list();
			tx.commit();
			
			if(result != null && !result.isEmpty()){
				tcambio = new Tpararela[result.size()];
				for(int i = 0; i < result.size(); i++){
					tcambio[i] = (Tpararela) result.get(0);				
				}
			}
		}catch(Exception ex){
			System.out.print("Se capturo una excepcion en TasaCambioCtrl.obtenerTasaCambioParalelo: " + ex);
		}finally {			
			try {HibernateUtilPruebaCn.closeSession(session); } catch (Exception e2) {e2.printStackTrace(); }
		}
		return tcambio;
	}
	@SuppressWarnings("unchecked")
	public Tpararela[] obtenerTasaCambioParalela(Date dtFecha){
		Session sesion = null;
		Tpararela[] tasas = null;
		
		try {
			sesion = HibernateUtilPruebaCn.currentSession();

			String sql = "select cmono, cmond, tcambiom, tcambiod, direc, fechai, fechaf from (select cmono, cmond, tcambiom, tcambiod, direc, "
					+ "cast((case when length(trim(cast(a.fechai as varchar(8))))=7 then "
					+ "(substring(trim(cast(a.fechai as varchar(8))), 4, length(a.fechai)) || '-' || "
					+ "substring(trim(cast(a.fechai as varchar(8))), 2, 2) || '-0' || substring(trim(cast(a.fechai as varchar(8))), 1, 1)) "
					+ "else (substring(trim(cast(a.fechai as varchar(8))), 5, length(a.fechai)) || '-' || "
					+ "substring(trim(cast(a.fechai as varchar(8))), 3,2) || '-' || "
					+ "substring(trim(cast(a.fechai as varchar(8))), 1, 2)) end ) as date) as fechain, fechai, "
					+ "cast((case when length(trim(cast(a.fechaf as varchar(8))))=7 then "
					+ "(substring(trim(cast(a.fechaf as varchar(8))), 4, length(a.fechaf)) || '-' || "
					+ "substring(trim(cast(a.fechaf as varchar(8))), 2, 2) || '-0' || substring(trim(cast(a.fechaf as varchar(8))), 1, 1)) "
					+ "else (substring(trim(cast(a.fechaf as varchar(8))), 5, length(a.fechaf)) || '-' || "
					+ "substring(trim(cast(a.fechaf as varchar(8))), 3,2) || '-' || "
					+ "substring(trim(cast(a.fechaf as varchar(8))), 1, 2)) end ) as date) as fechafn, fechaf "
					+ "from "+PropertiesSystem.ESQUEMA+".f55ca021 a) t where current date >= fechain order by fechain desc FETCH FIRST 1 ROWS ONLY";
				
			LogCajaService.CreateLog("obtenerTasaCambioParalela", "QRY", sql);
			
			List<Tpararela>lstTasas = (ArrayList<Tpararela>)
							sesion.createSQLQuery(sql)
							.addEntity(Tpararela.class).list();
			
			if(lstTasas == null || lstTasas.isEmpty())
				return new Tpararela[1];
			
			tasas = new Tpararela[lstTasas.size()];
			lstTasas.toArray(tasas);
			
		} catch (Exception e) {
			LogCajaService.CreateLog("obtenerTasaCambioParalela", "ERR", e.getMessage());
		}

		return tasas;
	}	
/**************************************************************************************************************************/
	@SuppressWarnings("rawtypes")
	public Tcambio[] obtenerTasaJDEMasReciente(){

		Session session = HibernateUtilPruebaCn.currentSession();
		Tcambio[] tcambio = null;
		
		try{
			String strQuery = "from Tcambio as t where" +
					" t.id.cxcrcd = 'COR' and t.id.cxeft in " +
					"(select max(t.id.cxeft)from Tcambio as t " +
					"where t.id.cxcrcd = 'COR')";
			
			LogCajaService.CreateLog("obtenerTasaJDEMasReciente", "QRY", strQuery);
			
			List result = (List) session.createQuery(strQuery).list();

			tcambio = new Tcambio[result.size()];
			for(int i = 0; i < result.size(); i++){
				tcambio[i] = (Tcambio)result.get(i);
			}
		}catch(Exception ex){
			LogCajaService.CreateLog("obtenerTasaJDEMasReciente", "ERR", ex.getMessage());
		}

		return tcambio;
	}
	
}

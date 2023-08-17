package com.casapellas.controles;
/**
 * CASA PELLAS S.A.
 * Creado por.........: Juan Carlos Ñamendi Pineda
 * Fecha de Creación..: 28/02/2009
 * Última modificación: 20/11/2009
 * Modificado por.....: Juan Carlos Ñamendi Pineda.
 * 
 */

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.casapellas.entidades.Log;
import com.casapellas.entidades.Tcambio;
import com.casapellas.entidades.Tpararela;
import com.casapellas.entidades.TpararelaId;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.PropertiesSystem;

public class TasaCambioCtrl {
	
	
	public static BigDecimal tasaCambioOficial(Date fecha, String moneda){
		BigDecimal tasaCambio = BigDecimal.ZERO;
		
		
		try {
			
			String sql = " select * from @BDCAJA.tcambio as t where t.cxcrcd = 'COR' and t.cxeft = '@FECHA' and t.cxcrdc = '@MONEDA' fetch first rows only ";
			
			sql = sql
			.replace("@BDCAJA", PropertiesSystem.ESQUEMA)
			.replace("@FECHA",  FechasUtil.formatDatetoString(fecha, "yyyy-MM-dd") )
			.replace("@MONEDA", moneda);
			
			Tcambio tasa =  ConsolidadoDepositosBcoCtrl.executeSqlQueryUnique(sql, Tcambio.class, true);
			
			return tasaCambio = (tasa == null)?  BigDecimal.ZERO : tasa.getId().getCxcrrd() ;
			
			
		} catch (Exception e) {
			LogCajaService.CreateLog("tasaCambioOficial", "ERR", e.getMessage());
		}
		
		return tasaCambio;
	}
	public static BigDecimal tasaCambioOficial(Date fecha, String moneda, Session session){
		BigDecimal tasaCambio = BigDecimal.ZERO;
		
		try {
			
			String sql = " select * from @BDCAJA.tcambio as t where t.cxcrcd = 'COR' and t.cxeft = '@FECHA' and t.cxcrdc = '@MONEDA' fetch first rows only ";
			
			sql = sql
			.replace("@BDCAJA", PropertiesSystem.ESQUEMA)
			.replace("@FECHA",  FechasUtil.formatDatetoString(fecha, "yyyy-MM-dd") )
			.replace("@MONEDA", moneda);
			
			Tcambio tasa =  (Tcambio) session.createSQLQuery(sql).addEntity(Tcambio.class).uniqueResult();
			
			return tasaCambio = (tasa == null)?  BigDecimal.ZERO : tasa.getId().getCxcrrd() ;
			
			
		} catch (Exception e) {
			tasaCambio = BigDecimal.ZERO ; 
			LogCajaService.CreateLog("tasaCambioOficial", "ERR", e.getMessage());
		}
		
		return tasaCambio;
	}
	
	public static BigDecimal tasaOficial(){
		BigDecimal tasa = BigDecimal.ONE;
		try {
			Tcambio[] tcambio ;
			if(CodeUtil.getFromSessionMap("tcambio") == null){
				String fecha = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
				tcambio = new TasaCambioCtrl().obtenerTasaCambioJDExFecha(fecha);
				CodeUtil.putInSessionMap( "tcambio", tcambio);
			}
			tcambio = (Tcambio[])CodeUtil.getFromSessionMap("tcambio");
			for(int l = 0; l < tcambio.length;l++){
				if(tcambio[l].getId().getCxcrcd().equals("COR")){
					tasa = tcambio[l].getId().getCxcrrd();
					break;
				}
			}	
		} catch (Exception e) {
			 tasa = BigDecimal.ONE;
		}
		return tasa;
	}

	public static BigDecimal tasaParalela(String moneda) {
		BigDecimal tasa = BigDecimal.ONE;
		try {
			
			Tpararela[] tpcambio ;
			if( CodeUtil.getFromSessionMap("tpcambio") == null ){
				tpcambio = TasaCambioCtrl.obtenerTasaCambioParalela();
				CodeUtil.putInSessionMap("tpcambio", tpcambio);
			}
			
			tpcambio = (Tpararela[]) CodeUtil.getFromSessionMap("tpcambio");
			for (int t = 0; t < tpcambio.length; t++) {
				if (tpcambio[t].getId().getCmono().equals(moneda)
						|| tpcambio[t].getId().getCmond().equals(moneda)) {
					tasa = tpcambio[t].getId().getTcambiom();
					break;
				}
			}
		} catch (Exception e) {
			tasa = BigDecimal.ONE;
		}
		return tasa;
	}
	/*************** obtener el valor de cambio entre dos monedas por fecha *********************************/
	public static BigDecimal obtenerTasaJDExFecha(String sMonOrigen, String sMonDestino, Date dtFecha){
		BigDecimal bdTasaCambio = BigDecimal.ZERO;
	
		String sConsulta = "";
		SimpleDateFormat format;
		String sFecha;
 
		Session sesion = null;
		
		
		
		try{
			sesion = HibernateUtilPruebaCn.currentSession();
			
			format = new SimpleDateFormat("yyyy-MM-dd");
			sFecha = format.format(dtFecha);	
			
			sConsulta = "from Tcambio as t where t.id.cxcrcd = '" + sMonOrigen
					+ "' and t.id.cxcrdc = '" + sMonDestino
					+ "' and t.id.cxeft = '" + sFecha + "'";
						
			Tcambio tc = (Tcambio)sesion.createQuery(sConsulta).setMaxResults(1).uniqueResult();
			if(tc == null)
				return bdTasaCambio = BigDecimal.ONE;
			
			bdTasaCambio = tc.getId().getCxcrr();			
			
		}catch(Exception ex){
			ex.printStackTrace(); 
		}
		return bdTasaCambio;
	}
	
/****************OBTIENE LA TASA DE CAMBIO DE JDE A LA FECHA**********************************************/
	public Tcambio[] obtenerTasaCambioJDExFecha(String sFecha){
		Session session = HibernateUtilPruebaCn.currentSession();
		
		
		Date dFecha = new Date();
		Tcambio[] tcambio = null;
		
		
		try{
			
			String query="from Tcambio as t where t.id.cxcrcd = 'COR' and '"
					+ sFecha + "' >= t.id.cxeft and '" + sFecha
					+ "' <= t.id.cxeft";
			
			List result = (List) session.createQuery(query).list();
			
			LogCajaService.CreateLog("obtenerTasaCambioJDExFecha", "QRY", query);
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
/**************************************************************************************************************************/	
/*************** obtener el valor de cambio entre dos monedas por fecha *********************************/
	public double obtenerTasaJDExFecha(String sMonOrigen, String sMonDestino, Date dtFecha,Session sesion,Transaction trans){
		double dTCambio = 0;
		
		String sConsulta = "";
		SimpleDateFormat format;
		String sFecha;
		
		
		try{
			
			format = new SimpleDateFormat("yyyy-MM-dd");
			sFecha = format.format(dtFecha);	
			
			sConsulta =  "from Tcambio as t where t.id.cxcrcd = '"+sMonOrigen+"' and t.id.cxcrdc = '"+sMonDestino+"'";
			sConsulta += " and t.id.cxeft = '"+sFecha+"'";

						
			Object ob = sesion.createQuery(sConsulta).uniqueResult();
			if(ob != null)
				dTCambio = ((Tcambio)ob).getId().getCxcrr().doubleValue();			
			
		}catch(Exception ex){
			ex.printStackTrace(); 
		}
		return dTCambio;
	}
	
/****************OBTIENE LA TASA DE CAMBIO DE JDE A LA FECHA**********************************************/
	public Tcambio[] obtenerTasaCambioJDEdelDia(){
//		Session session = HibernateUtilPruebaCn.currentSessionENS();
		Session session = HibernateUtilPruebaCn.currentSession();
		
		
		Date dFecha = new Date();
		Tcambio[] tcambio = null;
		
		
		try{
			
			List result = (List)session
			.createQuery("from Tcambio as t where t.id.cxcrcd = 'COR' " +
					"and current_date >= t.id.cxeft and " +
					"current_date <= t.id.cxeft").list();
		
			tcambio = new Tcambio[result.size()];
			for(int i = 0; i < result.size(); i++){
				tcambio[i] = (Tcambio)result.get(i);
			}
			if(result.size()==0){
				tcambio =  obtenerTasaJDEMasReciente();
			}
		}catch(Exception ex){
			ex.printStackTrace(); 
		}
		return tcambio;
	}
	/**
	 * Crea objeto de tasa paralela con los datos de la tasa oficial
	 * @param oficial: tasa de  cambio oficial
	 * @return Tpararela: Objeto tipo Tparalela con datos de tasa oficial
	 */
	public static Tpararela crearTParalelaDeTOficial(Tcambio oficial){
		Tpararela paralela = null;
		
		try {
			paralela = new  Tpararela();
			TpararelaId paralelaid = new  TpararelaId();
			paralelaid.setCmono(oficial.getId().getCxcrcd());
			paralelaid.setCmond(oficial.getId().getCxcrdc());
			paralelaid.setTcambiom(oficial.getId().getCxcrrd()) ;
			paralelaid.setTcambiod(oficial.getId().getCxcrr() ) ;
			paralelaid.setDirec('C') ;
			
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH ) );
			paralelaid.setFechai( Integer. parseInt( FechasUtil.formatDatetoString(cal.getTime(), "ddMMyyy") ));
			
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum( Calendar.DAY_OF_MONTH ) );
			paralelaid.setFechaf( Integer.parseInt( FechasUtil.formatDatetoString(cal.getTime(), "ddMMyyy") ));
			paralela.setId(paralelaid);
			
		} catch (Exception e) {
			 e.printStackTrace() ;
		}
		return paralela;
	}
	
/**************************************************************************************************************************/
/****************OBTIENE LA TASA DE CAMBIO PARALELA DE LA CONFIGURACION MAS RECIENTE***************************************/
	public static Tpararela[] obtenerTasaCambioParalela(){
		Tpararela[] tcambio = null;
		
		try{
			
			List<Tpararela> result = ConsolidadoDepositosBcoCtrl.executeSqlQuery("from Tpararela", Tpararela.class, false);
			
			if(result == null || result.isEmpty())
				return null;
			
			tcambio = new Tpararela[result.size()];
			result.toArray(tcambio);
				
		}catch(Exception ex){
			ex.printStackTrace(); 
		} 
		
		return tcambio;
	}
	 
/**************************************************************************************************************************/
	public Tcambio[] obtenerTasaJDEMasReciente(){
		Session session = HibernateUtilPruebaCn.currentSession();
		
		
		Tcambio[] tcambio = null;
		
		
		try{
			
			String query="from Tcambio as t where" +
					" t.id.cxcrcd = 'COR' and t.id.cxeft in " +
					"(select max(t.id.cxeft)from Tcambio as t " +
					"where t.id.cxcrcd = 'COR')";
			List result = (List) session.createQuery(query).list();
			
			LogCajaService.CreateLog("obtenerTasaJDEMasReciente", "QRY", query);
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

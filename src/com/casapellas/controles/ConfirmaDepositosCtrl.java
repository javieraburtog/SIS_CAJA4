package com.casapellas.controles;

//import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.context.FacesContext;
//import javax.mail.internet.InternetAddress;
//import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
//import org.apache.commons.mail.EmailAttachment;
//import org.apache.commons.mail.MultiPartEmail;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.casapellas.conciliacion.CoincidenciaDeposito;
import com.casapellas.conciliacion.entidades.Conciliacion;
import com.casapellas.conciliacion.entidades.ConsolidadoCoincidente;
import com.casapellas.conciliacion.entidades.Ajusteconc;
import com.casapellas.conciliacion.entidades.Archivo;
import com.casapellas.conciliacion.entidades.Conciliadet;
import com.casapellas.conciliacion.entidades.Vf0911;
import com.casapellas.entidades.Ctaxdeposito;
import com.casapellas.conciliacion.entidades.Depbancodet;
import com.casapellas.entidades.Deposito;
import com.casapellas.entidades.Deposito_Report;
import com.casapellas.entidades.F55ca023;
import com.casapellas.entidades.F55ca033;
import com.casapellas.entidades.Usuario;
import com.casapellas.entidades.Vdeposito;
import com.casapellas.entidades.Vf0101;
import com.casapellas.conciliacion.entidades.Vwconciliacion;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.navegacion.As400Connection;
import com.casapellas.reportes.Rptmcaja011Xls;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.CustomEmailAddress;
import com.casapellas.util.Divisas;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.MailHelper;
import com.casapellas.util.PropertiesSystem;
/**
 * CASA PELLAS S.A.
 * Creado por.........: Carlos Manuel Hernández Morrison
 * Fecha de Creación..: 16/08/2011
 * Última modificación: Carlos Manuel Hernández Morrison
 * Modificado por.....:	25/10/2011
 * Descripción:.......: Acceso a base de datos para manejo de confirmacion de depositos
 */ 
@SuppressWarnings("unchecked")
public class ConfirmaDepositosCtrl {
	public Exception error;
	public Exception errorDetalle;
	public Exception getError() {
		return error;
	}
	public void setError(Exception error) {
		this.error = error;
	}
	public Exception getErrorDetalle() {
		return errorDetalle;
	}
	public void setErrorDetalle(Exception errorDetalle) {
		this.errorDetalle = errorDetalle;
	}
	
	/**
	 * 
	 * @param cn
	 * @param codsuc
	 * @return int array : [0]mes, [1]anio 
	 */
	public static int[] obtenerPeriodoContableActual(Connection cn,  String codsuc){
		int[] periodo = null;
		boolean nuevacn = false;
		
		try {
			
			if( nuevacn = (cn == null) ){
				cn = As400Connection.getJNDIConnection("DSMCAJA2");
			}
			
			String sAnioFiscal =   FechasUtil.formatDatetoString(new Date(), "yyyy").substring(2, 4);
			int iAnioFiscalActual = Integer.parseInt(sAnioFiscal); 
			Calendar clFecha =  Calendar.getInstance();
			int iMesFiscalActual = clFecha.get(Calendar.MONTH) + 1;
			
			periodo = new int[]{iMesFiscalActual, iAnioFiscalActual};
			
			String sql = "SELECT  CCDFYJ, CCPNC FROM "
					+ PropertiesSystem.JDEDTA + ".F0010 where ccco = '"
					+ codsuc + "' ";
			
			PreparedStatement ps = cn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()){
				periodo = new int[]{rs.getInt(2), rs.getInt(1)};
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(nuevacn){
					cn.commit();
					cn.close();
					cn = null;
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return periodo;
	}
	
	public static Date generarFechaComprobante(Date fechadeposito, String codsuc, Connection cn){
		Date fecha = fechadeposito;
		
		try {
			
			int[] periodo = obtenerPeriodoContableActual(cn, codsuc) ;
			int anioActivo = periodo[1]  ;
			int mesActivo = periodo[0]  ;
			
			Calendar calendar =  Calendar.getInstance();
			calendar.setTime(fechadeposito);
			int iNumMesHoy = calendar.get(Calendar.MONTH) + 1;
			calendar.set(Calendar.MONTH, 0);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			
			int iFechaInicioAnioDep = FechasUtil.dateToJulian(calendar.getTime());
			
			if(iFechaInicioAnioDep != anioActivo || mesActivo != iNumMesHoy ){
				return fecha = new Date();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return fecha = fechadeposito;
		}
		return fecha;
	}
	
	
	public static String constructSubLibroCtaTbanco(int nocuenta, final int codigobanco,
						int caid, final String moneda, final String codcomp){
		String strSubLibroCuenta = "00000000";
		long numcuentabco = nocuenta;
		
		try {
			
			if( numcuentabco == 0){
				
				//&& ===== crear el numero que se va aplicar como sublibro a la cuenta subsidiara.
				List<F55ca023> ctasxbco = ConfirmaDepositosCtrl.obtenerF55ca023xBanco(codigobanco);
				F55ca023 f23 = (F55ca023)CollectionUtils.find(ctasxbco, new Predicate(){
					
					public boolean evaluate(Object o) {
						F55ca023 f23 = (F55ca023)o;
						return
						f23.getId().getD3codb() == codigobanco && 
						f23.getId().getD3crcd().trim().compareTo(moneda.trim()) == 0 &&
						f23.getId().getD3rp01().trim().compareTo(codcomp.trim()) == 0;
					}
				});
				numcuentabco = f23.getId().getD3nocuenta() ;
			}
			
			strSubLibroCuenta = String.valueOf( numcuentabco );
			if(strSubLibroCuenta.length() >= 5){
				strSubLibroCuenta = strSubLibroCuenta.substring(strSubLibroCuenta.length()-5, strSubLibroCuenta.length());
			}
			strSubLibroCuenta = String.valueOf( caid )+ strSubLibroCuenta;
			if(strSubLibroCuenta.length() > 8){
				strSubLibroCuenta = strSubLibroCuenta.substring(
					strSubLibroCuenta.length()-8, strSubLibroCuenta.length());
			}
			strSubLibroCuenta = CodeUtil.pad(strSubLibroCuenta, 8 , "0");   // Divisas.rellenarCadena(strSubLibroCuenta, "0", 8);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strSubLibroCuenta;
	}
	
	/**************************************************
	 *  Método:  com.casapellas.controles / constructSqlOrCtaxCon
	 *  Descrp: genera la sentencia sql para filtrar las cuentas configuradas para un conciliador.
	 *	Fecha:  Nov 26, 2014 
	 *  Autor:  CarlosHernandez
	 ***/
	public static String constructSqlOrCtaxCon(List<String[]>valorPosicion){
		String sqlOr = "";
		
		try {
			
			
			if( CodeUtil.getFromSessionMap("ctaxconciliador") != null ){
				
				List<String[]> ctaxconciliador = (ArrayList<String[]>)CodeUtil.getFromSessionMap("ctaxconciliador");
				
				for (String[] ctaxcon : ctaxconciliador) {
					if(!sqlOr.isEmpty())
						sqlOr+= " or " ;
					
					String sql = "" ;
					for (String[] vp : valorPosicion) {
						
						if(!sql.isEmpty())
							sql+= " and  " ;
						sql += "trim(" +vp[0] + ") = '"+ctaxcon[Integer.parseInt( vp[1] ) ].trim() + "'" ;
					}
					sqlOr += "( "+sql+" )";
				}
				
				sqlOr = "( "+ sqlOr+" )";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			String origen = PropertiesSystem.CONTEXT_NAME+": "
					+ new Exception().getStackTrace()[1].getClassName() +":"
					+ new Exception().getStackTrace()[1].getMethodName() ;
			
//			System.out.println(origen+":: > "+ sqlOr);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return sqlOr;
	}
	
	
	public static String constructSqlOrCtaxCon(List<String[]>valorPosicion, List<String[]> ctaxconciliador){
		String sqlOr = "";
		
		try {
			
			for (String[] ctaxcon : ctaxconciliador) {
				if(!sqlOr.isEmpty())
					sqlOr+= " or " ;
				
				String sql = "" ;
				for (String[] vp : valorPosicion) {
					
					if(!sql.isEmpty())
						sql+= " and  " ;
					sql += "trim(" +vp[0] + ") = '"+ctaxcon[Integer.parseInt( vp[1] ) ].trim() + "'" ;
				}
				sqlOr += "( "+sql+" )";
			}
			
			sqlOr = "( "+ sqlOr+" )";
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			String origen = PropertiesSystem.CONTEXT_NAME+": "
					+ new Exception().getStackTrace()[1].getClassName() +":"
					+ new Exception().getStackTrace()[1].getMethodName() ;
			
//			System.out.println(origen+":: > "+ sqlOr);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return sqlOr;
	}
	
	
	
	public static String addConditionToWhere(String originalQueryWhere, String[] strValorPosicion, String comodin ){
		try {
			
			List<String[]> datapart = new ArrayList<String[]>(strValorPosicion.length);
			for (String valuepos : strValorPosicion) {
				datapart.add(valuepos.split(comodin)) ;
			}

			String whereAdded  = ConfirmaDepositosCtrl.constructSqlOrCtaxCon(datapart);
			
			if( !whereAdded.isEmpty() ){
				originalQueryWhere += " and "+ whereAdded;
			}		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return originalQueryWhere;
		
	}
	
	
	
	/**************************************************
	 *  Método:  com.casapellas.controles / cargarConfiguracionConciliador
	 *  Descrp: Obtiene configuracion de cuentas y cajas asociadas a un conciliador.
	 *	Fecha:  Nov 26, 2014 
	 *  Autor:  CarlosHernandez
	 ***/
	public static void cargarConfiguracionConciliador(int codigocon ){
		List<String[]> ctaxconciliador = null;
		List<String[]> cajasxconciliador = null;
		Session sesion = null; 		
		boolean newCn = false;
		String sql = "";
		
		try{
			
			CodeUtil.removeFromSessionMap(new String[]{"ctaxconciliador", "cajasxconciliador" }) ;
			
			sesion = HibernateUtilPruebaCn.currentSession();
			
			//&& ======== Leer configuracion de cuentas de banco.
			sql = "select cast(codigobanco as varchar(20)),  cast( numerocuentabanco as varchar(20) ), trim(codcomp), moneda " +
					" from "+PropertiesSystem.ESQUEMA+".cuentaxconciliador " +
					" where estado = 1 and codigoconciliador = "+ codigocon ;
			
			List<Object[]> ctsxconc = (ArrayList<Object[]>)sesion.createSQLQuery( sql ).list();
			if(ctsxconc != null && !ctsxconc.isEmpty()){
				ctaxconciliador = new ArrayList<String[]>(ctsxconc.size());
				for (Object[] values : ctsxconc) {
					ctaxconciliador.add(new String[]{
							String.valueOf(values[0]),
							String.valueOf(values[1]),
							String.valueOf(values[2]),
							String.valueOf(values[3]),
							String.valueOf(codigocon)
					});
				}
				 
				CodeUtil.putInSessionMap("ctaxconciliador", ctaxconciliador);
			}
			
			//&& ======== Leer configuracion de cajas por conciliador
			sql = "select cast(caid as varchar(10)), cast(codigoconciliador as varchar(10)) " +
					" from "+PropertiesSystem.ESQUEMA+".cajaxconciliador " +
					" where estado = 1 and codigoconciliador = "+ codigocon ;
		
			List<Object[]> cjsxconc = (ArrayList<Object[]>)sesion.createSQLQuery( sql ).list();
			if(cjsxconc != null && !cjsxconc.isEmpty()){
				cajasxconciliador = new ArrayList<String[]>(cjsxconc.size());
				for (Object[] values : cjsxconc) {
					cajasxconciliador.add(new String[]{
							String.valueOf(values[0]),
							String.valueOf(values[1]),
					});
				}
				CodeUtil.putInSessionMap("cajasxconciliador", cajasxconciliador);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**************************************************
	 *  Método: CRPMCAJA / com.casapellas.controles /notificaCambioReferencia
	 *  Descrp: Envia correo de notificacion de los dep[ositos que se les cambio la referencia.
	 *	Fecha:  Oct 18, 2012 
	 *  Autor:  CarlosHernandez
	 ***/
	public boolean notificaCambioReferencia(List<Conciliacion>lstConcilias,
							List<String>lstRefers, int iCodConciliador) {
		boolean bHecho = true;
		try {
			
			String sCSS = " <style type = \"text/css\"> "
				 +".sEstiloTdhdr{"
				 +"		font: 12px  Arial, Helvetica, sans-serif;"
				 +"		color: white;"
				 +"		text-align: center;"
				 +"		background-color: #5e89b5;"
				 +"}"
				 +".sEstiloTD{"
				 +"		font: 12px Arial, Helvetica, sans-serif;"
				 +"		color: #1a1a1a;"
				 +"		border: 1px dashed silver;"
				 +"}"
				 +".tablapr{"
				 +"		width: 750px;"
				 +"		border: 1px solid silver;"
				 +"		margin: 0 auto;"
				 +"	}"
				 +"</style> ";			
			
			StringBuilder sTblDatos = new StringBuilder();
			
			// && ==== Oct 18, 2012: notificaCambioReferencia: Agregar titulo de la tabla.
			String sTitulos = "Cuenta@Monto@Moneda@Referencia Bco@Referencia Nueva@Contador@Descripcion"; 
			String[] sVlrTitulo = sTitulos.split("@");
			
			sTblDatos.append(sCSS).append("\n");
			
			sTblDatos.append("<table class = \"tablapr\" >");
			sTblDatos.append("<tr>");
			sTblDatos.append("<td class =\"sEstiloTD\" height=\"25px%\" " 
							+ "colspan = \""+sVlrTitulo.length
							+"\"  valign=\"bottom\" align=\"left\"  >");
			sTblDatos.append("<b>Depósitos con cambio de referencia</b>");
			sTblDatos.append("</td>");
			sTblDatos.append("</tr>");
			
			sTblDatos.append("<tr>");
			for (String sTexto : sVlrTitulo) {
							
				sTblDatos.append("<td class = \"sEstiloTdhdr\">");
				sTblDatos.append("<b>"+sTexto+"</b>");
				sTblDatos.append("</td>");
			}
			sTblDatos.append("</tr>");
			
			//&& ===== Oct 18, 2012: notificaCambioReferencia:
			//&& ===== Agregar el  cuerpo de la tabla. 
			String[] sAlign = {
					"right",
					"right",
					"center",
					"right",
					"right",
					"left",
					"left",
			};
			Divisas dv = new Divisas();
			String[] lstValores ;
			Conciliacion c = null;
			for (int i = 0; i < lstConcilias.size(); i++) {
				c = lstConcilias.get(i);
				lstValores = new String[]{
					String.valueOf(c.getIdcuenta()),
					String.valueOf(dv.formatDouble(c.getMonto().doubleValue())),
					c.getMoneda(),
					lstRefers.get(i).split("@")[0],
					lstRefers.get(i).split("@")[1],
					lstRefers.get(i).split("@")[2],
					lstRefers.get(i).split("@")[3],
				};
				
				sTblDatos.append("<tr>");
				for (int j = 0; j < lstValores.length; j++) {
					sTblDatos.append("<td class=\"sEstiloTD\" align=\""+sAlign[j]+"\" >");
					sTblDatos.append(lstValores[j]);
					sTblDatos.append("</td>");
				}
				sTblDatos.append("</tr>");
			}
			
			sTblDatos.append("</table>");
			
			// && =============== Oct 18, 2012: notificaCambioReferencia: Enviar el correo
			String sNombreTo = "", sTo="";
			Vf0101 vf01 =   EmpleadoCtrl.buscarEmpleadoxCodigo2(iCodConciliador);
			
			if(vf01 == null)
				return false;
			
			sNombreTo = Divisas.ponerCadenaenMayuscula( vf01.getId().getAbalph().trim() );
			sTo = vf01.getId().getWwrem1().trim().toUpperCase();
			
			if( !sTo.toUpperCase().matches(PropertiesSystem.REGEXP_EMAIL_ADDRESS) ){
				error = new Exception("@CONCILIACION: Cuenta de correo '"+sTo+"' no válida ");
				return false;
			}
			
			List<CustomEmailAddress> bccCopy = new ArrayList<CustomEmailAddress>();
			for (String dtaCc : PropertiesSystem.MAILBCCSCON) {
				bccCopy.add( new CustomEmailAddress(dtaCc.split(PropertiesSystem.SPLIT_CHAR)[0], dtaCc.split(PropertiesSystem.SPLIT_CHAR)[1] ));
			}
			
			
			MailHelper.SendHtmlEmail(
					new CustomEmailAddress(PropertiesSystem.WEBMASTER_EMAIL_ADRESS, "Módulo de Caja: Confirmación depósitos"),
					new CustomEmailAddress(sNombreTo, sTo.toLowerCase()), 
					bccCopy, 
					"Confirmación de depósitos: Notificación Cambio de referencias de Depósitos", 
					sTblDatos.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bHecho;
	}
	/* ******************** fin de metodo notificaCambioReferencia ****************************/
	
	public List<Ctaxdeposito> obtenerCuentasDeps(int iNoDeposito){
		List<Ctaxdeposito>lstCtas = null;
		try {
			Session sesion = HibernateUtilPruebaCn.currentSession();
						
			Criteria cr = sesion.createCriteria(Ctaxdeposito.class);
			cr.add(Restrictions.eq("deposito.consecutivo", iNoDeposito));
			lstCtas = cr.list();
			
		} catch (Exception e) {
			lstCtas = null;
			e.printStackTrace();

		}
		return lstCtas;
	}
	
	
	public String consultaF0911(){
		String consulta = "";
		
		consulta =
		"SELECT " +	
		"	DISTINCT( CAST(GLAID AS VARCHAR(8) CCSID 37) ) GLAID , "+
		"	GLAA,"+
		"	CAST( GLAA/100 AS DECIMAL(15,2) ) MONTO, "+
		" 	CAST(GLCRCD AS VARCHAR(3) CCSID 37) GLCRCD , "+
		"	CAST(GLANI AS VARCHAR(30) CCSID 37) GLANI ,"+ 
		"	CAST(GLMCU AS VARCHAR(12) CCSID 37) GLMCU, "+
		"	CAST(GLOBJ AS VARCHAR(12) CCSID 37) GLOBJ, "+
		"	CAST(GLSUB AS VARCHAR(12) CCSID 37) GLSUB , "+
		"	CAST(GLSBL AS VARCHAR(12) CCSID 37) GLSBL , "+
		"	GLICU, "+
		"	GLDOC, "+
		"	CAST(GLKCO AS VARCHAR(12) CCSID 37) GLKCO ,"+ 
		"	GLDGJ,"+
		"	GLUPMJ,"+
		"	GLCRR, "+
		"	CAST(GLEXA AS VARCHAR(50) CCSID 37) GLXA ,"+ 
		"	CAST(GLEXR AS VARCHAR(50) CCSID 37) GLEXR ,"+ 
		"	CAST(GLTORG AS VARCHAR(12) CCSID 37) GLTORG,"+ 
		"	CAST(GLDCT AS VARCHAR(3) CCSID 37) GLDCT ,"+ 
		"	CAST(GLPOST AS VARCHAR(1) CCSID 37) GLPOST,"+
		"	(CASE WHEN GLAA < 0 THEN 'Debito' ELSE 'Credito' END ) TIPO_CARGO,"+
		"	(SELECT CAST(DRDL01 AS VARCHAR(30) CCSID 37) FROM GCPPRDCOM.F0005D F5 WHERE F5.DRSY = '98' AND F5.DRRT = 'IC' AND TRIM(F5.DRKY) = F.GLPOST) ESTADO,"+
		"	CAST (DATE(CHAR(1900000 + GLDGJ) ) AS DATE ) FECHABATCH,"+ 
		"	CAST (DATE(CHAR(1900000 + GLUPMJ) ) AS DATE ) FECHAMODBATCH, "+
		"	(SELECT LOWER( CAST(GMDL01 AS VARCHAR(50) CCSID 37) ) FROM GCPPRDDTA.F0901 F3 WHERE F3.GMAID = F.GLAID FETCH FIRST ROWS ONLY ) NOMBRECUENTA,"+ 
		"	(SELECT CAST(ICJOBN AS VARCHAR(15) CCSID 37) ICJOBN FROM GCPPRDDTA.F0011 F4 WHERE F4.ICICU = F.GLICU FETCH FIRST ROWS ONLY ) AS PROGRAMAORIGEN "+
		 
		"FROM @JDEDTA.F0911 F "+
		"where f.gldgj between @FECHAINI and @FECHAFIN AND  f.glaid = '@GLAID' and GLLT = (case when GLCRCD <> 'COR' then 'CA' else 'AA' END) and gldct <> 'AE' "  ;
		
		return consulta ;
		
	}
	
	
	public List<Vf0911> buscarTransaccionesDeCuenta(
			String sUN, String sCtaobj, String sCtasub, 
			BigDecimal bdMontoIni, BigDecimal bdMontoFin,
			Date dtFechaIni, Date dtFechaFin, String sMoneda, 
			String sTipo, String tipo_documento){
		
		List<Vf0911> cuenta = null;
		String str_sqlQuerySelect ;
		
		try {
			
			str_sqlQuerySelect = "select gmaid from @BDMCAJA.VF0901 F where trim(f.gmmcu) = '@GMMCU' and trim(f.gmobj) = '@GMOBJ' and trim(f.gmsub) = '@GMSUB'" ;
			str_sqlQuerySelect = str_sqlQuerySelect
				.replace("@BDMCAJA", PropertiesSystem.ESQUEMA)
				.replace("@GMMCU", sUN.trim() )
				.replace("@GMOBJ", sCtaobj.trim() )
				.replace("@GMSUB", sCtasub.trim() );
			
			String str_gmaid = (String) ConsolidadoDepositosBcoCtrl.executeSqlQuery(str_sqlQuerySelect, true, null).get(0); 
			
			int fecha_ini_jul = FechasUtil.dateToJulian(dtFechaIni);
			int fecha_fin_jul = FechasUtil.dateToJulian(dtFechaFin);
			
			str_sqlQuerySelect = consultaF0911();
			
			if(bdMontoIni.compareTo(BigDecimal.ZERO) != 0){
				str_sqlQuerySelect += " and f.glaa >= " + bdMontoIni.toString().replace(".", "");
			}
			if(bdMontoFin.compareTo(BigDecimal.ZERO) != 0){
				str_sqlQuerySelect += " and f.glaa <= " + bdMontoFin.toString().replace(".", "");
			}	
			
			switch(Integer.valueOf(sTipo)){
				case 1: str_sqlQuerySelect += " and f.glaa > 0 "; break;
				case 2: str_sqlQuerySelect += " and f.glaa < 0 "; break;
				case 3: str_sqlQuerySelect += " and f.glaa <> 0 "; break;
			}
		
			str_sqlQuerySelect = str_sqlQuerySelect
					.replace("@JDEDTA", PropertiesSystem.JDEDTA )
					.replace("@GLAID", str_gmaid )
					.replace("@FECHAINI", Integer.toString( fecha_ini_jul ) )
					.replace("@FECHAFIN", Integer.toString( fecha_fin_jul ) ) ;
				
//			System.out.println("consulta " + str_sqlQuerySelect );
			
			cuenta  = (ArrayList<Vf0911>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(str_sqlQuerySelect, true, Vf0911.class);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return cuenta; 
	}
	
/******************************************************************************************/
/** Método: 
 *	Fecha:  07/11/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public List<Vdeposito> buscarDepositoCtaTrans(String sUN, String sCtaobj, String sCtasub, 
									BigDecimal bdMontoIni, BigDecimal bdMontoFin,
									Date dtFechaIni, Date dtFechaFin, String sMoneda, 
									String sTipo, int iMaxResult){
		List<Vdeposito> lstVdeposito = null;
		
		try{
			Session sesion = HibernateUtilPruebaCn.currentSession();
						
			Criteria cr = sesion.createCriteria(Ctaxdeposito.class);
			
			if(iMaxResult > 0 )
				cr.setMaxResults(300);
//			System.out.println("MaxResult "+iMaxResult);
			
			cr.createAlias("deposito", "dpCj");
			cr.setProjection(Projections.distinct(Projections.property("dpCj.consecutivo")));
			
			cr.add(Restrictions.eq("dpCj.tipodep", "D"));
			cr.add(Restrictions.not(Restrictions.in("dpCj.mpagodep", new String[]{"X"," "})));
					
			if(sUN.trim().compareTo("") != 0)
				cr.add(Restrictions.eq("gmmcu", sUN));
			if(sCtaobj.trim().compareTo("") != 0)
				cr.add(Restrictions.eq("gmobj", sCtaobj));
			if(sCtasub.trim().compareTo("") != 0)
				cr.add(Restrictions.eq("gmsub", sCtasub));
			
			//&& ======== Validaciones para ingresos o egresos a la cuenta.
			if(sTipo.compareTo("01") == 0){
				cr.add(Restrictions.eq("tipomov", "C"));
				cr.add(Restrictions.eq("dpCj.estadocnfr", PropertiesSystem.DP_NOCONFIRMADO /*"SCR"*/));
			}
			else{
				cr.add(Restrictions.eq("dpCj.estadocnfr", PropertiesSystem.DP_CONFIRMADO/*"CFR"*/));
				cr.createAlias("dpCj.conciliadets", "fk_consdet");
				cr.createAlias("fk_consdet.conciliacion", "fk_concs");
				cr.add(Restrictions.eq("fk_concs.estado",46));
			}
			
			//&& ==== Validaciones del monto.
			if(bdMontoFin == null)bdMontoFin = BigDecimal.ZERO;  
			if(bdMontoIni == null)bdMontoIni = BigDecimal.ZERO;
			if(bdMontoFin.compareTo(bdMontoIni) == -1 ){
				String sTmp = bdMontoFin.toString();
				bdMontoFin = new BigDecimal(bdMontoIni.toString());
				bdMontoIni = new BigDecimal(sTmp);
			}
			if(bdMontoFin.compareTo(BigDecimal.ZERO) != 0)
				cr.add(Restrictions.le("monto", bdMontoFin));
			if(bdMontoIni.compareTo(BigDecimal.ZERO) != 0)
				cr.add(Restrictions.ge("monto", bdMontoIni));

			//&& ==== Validaciones de la fecha.			
			if(dtFechaFin != null && dtFechaIni != null && dtFechaFin.compareTo(dtFechaIni) == -1){
				Date dtFechatmp = dtFechaFin;
				dtFechaFin = dtFechaIni;
				dtFechaIni = dtFechatmp;
			}
			if(dtFechaIni != null)
				cr.add(Restrictions.ge("dpCj.fecha", dtFechaIni));
			if(dtFechaFin != null)
				cr.add(Restrictions.le("dpCj.fecha", dtFechaFin));
			
			if(sMoneda.compareTo("") != 0)
				cr.add(Restrictions.eq("dpCj.moneda", sMoneda));
			
			ArrayList<Integer>lstCtaxdeps = (ArrayList<Integer>)cr.list();
			if(lstCtaxdeps != null && lstCtaxdeps.size()>0){
				lstVdeposito = new ArrayList<Vdeposito>();
				
				
				if(lstCtaxdeps.size() > 5000 ){
					List<Integer> lstSegm = null;
					List<Vdeposito> lstDevtmp = null;
					int ini =  0; 
					int fin =  0;
					int iFactor = 5;
					int iPartes = iFactor;
					
					int div  = lstCtaxdeps.size()/ iPartes ;
					int iSeg = lstCtaxdeps.size()/div;
					
					while(div > 3000){
						iPartes += iFactor;
						 div  = lstCtaxdeps.size() / iPartes ;
						 iSeg = lstCtaxdeps.size() / div;
					}
					
					for (int i = 0; i < iSeg; i++) {
						lstSegm = new ArrayList<Integer>(div);
						ini = fin;
						fin = fin + div;
						
						lstSegm = lstCtaxdeps.subList(ini, (( i == iSeg-1)? lstCtaxdeps.size():fin ));
				
						StringBuilder sb = new StringBuilder(lstSegm.toString());
						sb.setCharAt(0, '(');
						sb.setCharAt(sb.length()-1, ')');
						
						sesion.flush();
						cr = sesion.createCriteria(Vdeposito.class);
						cr.add(Restrictions.sqlRestriction(" consecutivo in "+ sb.toString()  ));
						
						cr.addOrder(Order.asc("id.caid"))
									.addOrder(Order.asc("id.codcomp"))
									.addOrder(Order.asc("id.moneda"))
									.addOrder(Order.desc("id.monto"));
						lstDevtmp = cr.list();
						lstVdeposito.addAll(lstDevtmp);
						
					}
				}else{
					cr = sesion.createCriteria(Vdeposito.class);
					cr.add(Restrictions.in("id.consecutivo", lstCtaxdeps));
					cr.addOrder(Order.asc("id.caid"))
								.addOrder(Order.asc("id.codcomp"))
								.addOrder(Order.asc("id.moneda"))
								.addOrder(Order.desc("id.monto"));
					lstVdeposito = cr.list();
				}
			}
			
			
		} catch (Exception e) {	
			lstVdeposito =  null;
//			System.out.println(": Excepción capturada en :buscarDepositoCtaTrans Mensaje:\n "+e);
			e.printStackTrace();
		}
		return lstVdeposito;
	}
/******************************************************************************************/
/** Método: Filtrar los depositos de banco seleccionados desde la pantalla.
 *	Fecha:  07/11/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */	
	public List<Depbancodet> filtrarDepositosBanco(String sConfirmador, String sContador, int iCodBanco,
												int iNoCuenta, int iEstado, String sMoneda, 
												BigDecimal bdMontomin, BigDecimal bdMontomax,
												int iReferenc, Date dtFechaIni, Date dtFechaFin){
		List<Depbancodet>lstDepositosBco = null;
		List<Integer>lstNoDepbsBco = null; 
		Session sesion = null; 		
		boolean newCn = false;
		
		try {
			
			sesion = HibernateUtilPruebaCn.currentSession();
						
			Criteria cr = sesion.createCriteria(Depbancodet.class);
			cr.createAlias("archivo", "archivo");
			cr.addOrder(Order.asc("fechaproceso"));
			cr.add(Restrictions.gt("mtocredito", BigDecimal.ZERO));
			cr.setMaxResults(500);
			
			if(iCodBanco != 0)
				cr.add(Restrictions.eq("archivo.idbanco", iCodBanco));
			if(iNoCuenta != 0)
				cr.add(Restrictions.eq("nocuenta", iNoCuenta));
			if(iEstado != 0 )
				cr.add(Restrictions.eq("idestadocnfr", iEstado));
			if(sMoneda.compareTo("") != 0 )
				cr.add(Restrictions.eq("archivo.moneda", sMoneda));
			if(bdMontomin.compareTo(BigDecimal.ZERO) != 0)
				cr.add(Restrictions.ge("mtocredito", bdMontomin));
			if(bdMontomax.compareTo(BigDecimal.ZERO) != 0)
				cr.add(Restrictions.le("mtocredito", bdMontomax));
			if(iReferenc != 0)
				cr.add(Restrictions.sqlRestriction(" referencia like '%"+iReferenc+"'"));
			
			if(dtFechaIni != null)
				cr.add(Restrictions.ge("fechaproceso", dtFechaIni));
			if(dtFechaFin != null)
				cr.add(Restrictions.le("fechaproceso", dtFechaFin));
			
						
			lstDepositosBco = cr.list();
			
		} catch (Exception e) {
			e.printStackTrace();
			errorDetalle = e; 
			error = new Exception("@DEPBANCODET: Error de sistema al obtener la lista de depositos de banco.");
			
		}
		return lstDepositosBco;
	}
/******************************************************************************************/
/** Método: Obtener depositos de banco filtrados por parametros.
 *	Fecha:  04/11/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */	
	public List<Depbancodet> obtenerDepositosBcoxFecha(Date dtFechaIni, Date dtFechaFin, String sMoneda, 
														int iEstado, int iCodBanco, int iMaxResult){
		List<Depbancodet>lstDepositosBco = null;
		Session sesion = null;		
		
		
		try {
 
			sesion = HibernateUtilPruebaCn.currentSession();
			
			Criteria cr = sesion.createCriteria(Depbancodet.class);
			
			cr.createAlias("archivo", "archivo");
			cr.addOrder(Order.asc("fechaproceso"));
			
			cr.add(Restrictions.ge("mtocredito", BigDecimal.ZERO));
			cr.add(Restrictions.ge("fechaproceso", dtFechaIni));
			cr.add(Restrictions.le("fechaproceso", dtFechaFin));
			if(iEstado != 0 )
				cr.add(Restrictions.eq("idestadocnfr", iEstado));
			if(sMoneda.compareTo("") != 0 )
				cr.add(Restrictions.eq("archivo.moneda", sMoneda));
			if(iCodBanco != 0)
				cr.add(Restrictions.eq("archivo.idbanco", iCodBanco));
			if(iMaxResult != 0 )
				cr.setMaxResults(iMaxResult);
			
			List<String[]>dta = new ArrayList<String[]>();
			dta.add(new String[]{"nocuenta","1"});
			String sqlOrCtaConc = constructSqlOrCtaxCon(dta);
			if( !sqlOrCtaConc.isEmpty() ){
				cr.add( Restrictions.sqlRestriction(sqlOrCtaConc) );
			}
			
			lstDepositosBco = cr.list();

		} catch (Exception e) {
			lstDepositosBco= null;
			errorDetalle = e; 
			error = new Exception("@DEPBANCODET: Error de sistema al obtener la lista de depositos de banco.");
//			System.out.println(":ConfirmaDepositosCtrl:  Excepción capturada en obtenerDepositosBcoxFecha(): "+e);
			e.printStackTrace();
		}
		return lstDepositosBco;
	}
	
/******************************************************************************************/
/** Método: Obtener los depositos de caja registrados, a partir de los filtros.
 *	Fecha:  03/11/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */	
	public List<Vdeposito> obtenerVDepositosCaja(int iCaid, String sCodcomp, String sReferencia, BigDecimal bdMontoIni, 
												BigDecimal bdMontoFin, String sEstado, Date dtFechaIni, Date dtFechaFin,
												String sUsuario, String sMoneda, int iMaxRes){
									
		List<Vdeposito>lstDepositosCaja = null;
		Session sesion = null;
		
		
		try {
 
			sesion = HibernateUtilPruebaCn.currentSession();
			
			Criteria cr = sesion.createCriteria(Vdeposito.class);
			cr.add(Restrictions.eq("id.tipodep", "D"));
			
			if(dtFechaIni != null)
				cr.add(Restrictions.ge("id.fecha", dtFechaIni));
			if(dtFechaFin != null)
				cr.add(Restrictions.le("id.fecha", dtFechaFin));
			
			if(iCaid != 0 )
				cr.add(Restrictions.eq("id.caid", iCaid));
			if(sCodcomp.compareTo("")!=0)
				cr.add(Restrictions.eq("id.codcomp", sCodcomp));
			if(sMoneda.compareTo("")!=0)
				cr.add(Restrictions.eq("id.moneda", sMoneda));
			
			if(sReferencia.compareTo("")!=0)
				cr.add(Restrictions.sqlRestriction(" referencia like '%"+sReferencia+"'"));
			if(sUsuario.compareTo("")!=0)
				cr.add(Restrictions.sqlRestriction(" coduser like '%"+sUsuario+"%'"));
			
			List<String[]>dta = new ArrayList<String[]>();
			dta.add(new String[]{"idbanco","0"});
			dta.add(new String[]{"codcomp","2"});
			dta.add(new String[]{"moneda","3"});
			String sqlOrCtaConc = constructSqlOrCtaxCon(dta);
			if( !sqlOrCtaConc.isEmpty() ){
				cr.add( Restrictions.sqlRestriction(sqlOrCtaConc) );
			}
			
			if(bdMontoIni.compareTo(BigDecimal.ZERO) != 0 )
				cr.add(Restrictions.ge("id.monto", bdMontoIni));
			if(bdMontoFin.compareTo(BigDecimal.ZERO) != 0 )
					cr.add(Restrictions.le("id.monto", bdMontoFin));
			if(sEstado.compareTo("") != 0) 
				cr.add(Restrictions.eq("id.estadocnfr", sEstado));
			if(iMaxRes != 0)
				cr.setMaxResults(iMaxRes);
			
			lstDepositosCaja = cr.list();
 
			
		} catch (Exception e) {
			e.printStackTrace();
			errorDetalle = e; 
			error = new Exception("@VDEPOSITO: Error de sistema al obtener la lista de depositos de caja.");
		}
		return lstDepositosCaja;
	}
	
	
	@SuppressWarnings("serial")
	public static void notificacionBatchConfirmados(String strUsuarioPreconcil, int cantidadBatch, 
			String strResumenConcilia, String rutaFisicaArchivo, List<String>cuentasCorreo) {
		
		try {

			String htlm = "<table>";
			htlm += "<tr>";
			htlm += "<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"100%\" >";
			htlm += "<b>SE HA GENERADO EL REPORTE DE BATCH POR CONFIRMACIÓN DE DEPÓSITOS CAJA-BANCO </b></td>";
			htlm += "</tr>";	
			
			htlm += "<tr>";
			htlm += "<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"100%\" >";
			htlm += "<b> Resumen: "+strResumenConcilia+" </b></td>";
			htlm += "</tr>";
			
			htlm += "<tr>";
			htlm += "<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"100%\" >";
			htlm += "<b>Cantidad: "+cantidadBatch+"</b></td>";
			htlm += "</tr>";
			
			htlm += "<tr>";
			htlm += "<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"100%\" >";
			htlm += "<b>Usuario: "+strUsuarioPreconcil+"</b></td>";
			htlm += "</tr>";
			
			htlm += "<tr>";
			htlm += "<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"100%\" >";
			htlm += "<b>FECHA:  "+new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date())+"</b></td>";
			htlm += "</tr>";
			
			htlm += "<tr>";
			htlm += "<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"100%\" >";
			htlm += "<b><br>SALUDOS</b></td>";
			htlm += "</tr>";
			htlm += "</table>";

			List<CustomEmailAddress> toList = new ArrayList<CustomEmailAddress>();
			for (String cuenta : cuentasCorreo) {
				toList.add(new CustomEmailAddress(cuenta.split("<>")[0].trim(), CodeUtil.capitalize( cuenta.split("<>")[1].trim())));
			}

			MailHelper.SendHtmlEmail(
					new CustomEmailAddress(PropertiesSystem.WEBMASTER_EMAIL_ADRESS, "Módulo de Caja: Notificación de Traslado de Factura"),
					toList, null, 
					new ArrayList<CustomEmailAddress>() { { add(new CustomEmailAddress(PropertiesSystem.MAIL_BOUNCEADDRESS)); } }, 
					"Batchs Preconciliacion generados a la fecha"+new SimpleDateFormat("dd MMMM yyyy",new Locale("es","ES")) .format(new Date()), 
					htlm.toString(), new String[] { rutaFisicaArchivo });
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			try {
			/*	File f = new File( rutaFisicaArchivo  ) ;
				if(f.exists() )
					f.delete();*/
				
			} catch (Exception e2) {
				e2.printStackTrace();
			}
				
		}
	}
	
	
	/******************************************************************************************/
	/** Método: Enviar Correo de notificacion
	 *	Fecha:  25/10/2011
	 *  Nombre: Carlos Manuel Hernández Morrison.
	 */	
		public static boolean enviarCorreoPorConsolidadosProcesados(List<ConsolidadoCoincidente>coincidencias, int codusercrea){
			boolean bHecho = true;
			try {
				
				
				//HttpServletRequest sHttpRqst  = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
				//String sRutaCntx = sHttpRqst.getContextPath() + "/"+PropertiesSystem.CARPETA_DOCUMENTOS_EXPORTAR+"/" ;
				//String sRutaFisica = sHttpRqst.getServletContext().getRealPath("/"+PropertiesSystem.CARPETA_DOCUMENTOS_EXPORTAR+"/");
				
				String sRutaFisica = PropertiesSystem.RUTA_DOCUMENTOS_EXPORTAR ;
				String sufijo =  new SimpleDateFormat("ddMMyyyHHmmss").format(new Date()); 
				String nombreDocumento = "ResultadoProcesoConfirmacion_"+sufijo+".xlsx" ;
				
				Rptmcaja011Xls xls = new Rptmcaja011Xls( null, null,  coincidencias, sRutaFisica,  "",  nombreDocumento, false) ;
				xls.generarConciliados = true;
				bHecho = xls.crearExcel();
				
				if(bHecho){
					
					String strResumenConcilia = "Nivel: " + coincidencias.get(0).getNivelcomparacion() +
							", Cuenta: " + coincidencias.get(0).getNumerocuenta() +
							", Banco: " + coincidencias.get(0).getNombrebanco().trim();
					
					Vf0101 vfUsuarioCrea = EmpleadoCtrl.buscarEmpleadoxCodigo2(codusercrea);
					
					String[] cuentas = new String[]{ 
							vfUsuarioCrea.getId().getWwrem1().trim()+"<>"+vfUsuarioCrea.getId().getAbalph(), 
							"lfonseca@casapellas.com.ni"+"<>"+vfUsuarioCrea.getId().getAbalph()
							};
					
					notificacionBatchConfirmados(PropertiesSystem.USUARIO_PRECONCILIACION, coincidencias.size(), strResumenConcilia,  ( sRutaFisica + nombreDocumento )  , Arrays.asList(cuentas)) ;
					
					return bHecho;
				}
				
				
				Vf0101 vfUsuarioCrea = EmpleadoCtrl.buscarEmpleadoxCodigo2(codusercrea);
				Usuario usrCreacion = UsuarioCtrl.obtenerUsuarioENSxCodigo( codusercrea );
				
				StringBuilder sTituloDet = new StringBuilder();
				StringBuilder sTablaDetalleBatch = new StringBuilder();
				StringBuilder sbTablaCorreo = new StringBuilder();
				
				String sEstiloTdhdr="", sEstiloTD="";
				
				sEstiloTdhdr += "style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color:white; ";
				sEstiloTdhdr += " background-color: #5e89b5;\" align=\"center\" ";
				sEstiloTD += " style=\" font-family: Arial,Helvetica, sans-serif;font-size: 12px;color: #1a1a1a; border-color: silver; ";
				sEstiloTD += " border-style: dashed; border-width: 1px\" ";
				
				sTablaDetalleBatch.append("<table width=\"750px\" style=\"border-color: silver; border-width: 1px; border-style: solid\">");
				sTablaDetalleBatch.append("<tr>");
				sTablaDetalleBatch.append("<td height=\"25px%\" colspan = \"9\" "+sEstiloTD+ " valign=\"bottom\" align=\"left\"  >");
				sTablaDetalleBatch.append("<b>Detalle de los Batch's:  </b>");
				sTablaDetalleBatch.append("</td>");
				sTablaDetalleBatch.append("</tr>");
				
				sTituloDet.append("<tr>");			
				sTituloDet.append("<td "+sEstiloTdhdr+ ">");
				sTituloDet.append("<b>N°</b>");
				sTituloDet.append("</td>");
				
				sTituloDet.append("<td "+sEstiloTdhdr+ ">");
				sTituloDet.append("<b>Batch</b>");
				sTituloDet.append("</td>");
				
				sTituloDet.append("<td "+sEstiloTdhdr+ ">");
				sTituloDet.append("<b>RF Banco</b>");
				sTituloDet.append("</td>");
				
				sTituloDet.append("<td "+sEstiloTdhdr+ ">");
				sTituloDet.append("<b>RF JDE </b>");
				sTituloDet.append("</td>");
				
				sTituloDet.append("<td "+sEstiloTdhdr+ ">");
				sTituloDet.append("<b>Tipo JDE </b>");
				sTituloDet.append("</td>");
				
				sTituloDet.append("<td "+sEstiloTdhdr+ ">");
				sTituloDet.append("<b>Monto Banco</b>");
				sTituloDet.append("</td>");
				
				sTituloDet.append("<td "+sEstiloTdhdr+ ">");
				sTituloDet.append("<b>Monto en JDE</b>");
				sTituloDet.append("</td>");
				
				sTituloDet.append("<td "+sEstiloTdhdr+ ">");
				sTituloDet.append("<b>Moneda</b>");
				sTituloDet.append("</td>");
				
				sTituloDet.append("<td "+sEstiloTdhdr+ ">");
				sTituloDet.append("<b>Fecha</b>");
				sTituloDet.append("</td>");
				
				sTituloDet.append("<td "+sEstiloTdhdr+ ">");
				sTituloDet.append("<b>Creado Por</b>");
				sTituloDet.append("</td>");
				
				sTituloDet.append("<td "+sEstiloTdhdr+ ">");
				sTituloDet.append("<b>Contador</b>");
				sTituloDet.append("</td>");
				sTituloDet.append("</tr>");
				
				sTablaDetalleBatch.append(sTituloDet);
				
				//&& ============= Crear las lineas por cada consolidado procesado.
				for (ConsolidadoCoincidente cd : coincidencias) {
					sTablaDetalleBatch.append("<tr>");
					
					sTablaDetalleBatch.append("<td align=\"right\" " +sEstiloTD+ " >");
					sTablaDetalleBatch.append( cd.getIdresumenbanco() );
					sTablaDetalleBatch.append("</td>");

					sTablaDetalleBatch.append("<td align=\"right\" " +sEstiloTD+ " >");
					sTablaDetalleBatch.append( cd.getNobatch() );
					sTablaDetalleBatch.append("</td>");
					
					sTablaDetalleBatch.append("<td align=\"right\" " +sEstiloTD+ " >");
					sTablaDetalleBatch.append( cd.getReferenciabanco() );
					sTablaDetalleBatch.append("</td>");
					
					sTablaDetalleBatch.append("<td align=\"right\" " +sEstiloTD+ " >");
					sTablaDetalleBatch.append( cd.getReferenciacomprobante() );
					sTablaDetalleBatch.append("</td>");
					
					sTablaDetalleBatch.append("<td align=\"center\" " +sEstiloTD+ "  >");
					sTablaDetalleBatch.append( cd.getTipodocumentojde() ) ;
					sTablaDetalleBatch.append("</td>");
					
					sTablaDetalleBatch.append("<td align=\"right\" " +sEstiloTD+ " >");
					sTablaDetalleBatch.append( String.format("%1$,.2f", cd.getMontoBanco() )	);
					sTablaDetalleBatch.append("</td>");
					
					sTablaDetalleBatch.append("<td align=\"right\" " +sEstiloTD+ " >");
					sTablaDetalleBatch.append( String.format("%1$,.2f", cd.getMontoAjustado() )	);
					sTablaDetalleBatch.append("</td>");
					
					sTablaDetalleBatch.append("<td align=\"center\" " +sEstiloTD+ " >");
					sTablaDetalleBatch.append( cd.getMoneda() );
					sTablaDetalleBatch.append("</td>");
					
					sTablaDetalleBatch.append("<td align=\"center\" " +sEstiloTD+ " >");
					sTablaDetalleBatch.append(  FechasUtil.formatDatetoString( new Date()  , "dd/MM/yyyy"));
					sTablaDetalleBatch.append("</td>");
					
					sTablaDetalleBatch.append("<td align=\"left\" " +sEstiloTD+ " >");
					sTablaDetalleBatch.append( usrCreacion.getLogin().trim() );
					sTablaDetalleBatch.append("</td>");
					
					sTablaDetalleBatch.append("<td align=\"left\" " +sEstiloTD+ " >");
					sTablaDetalleBatch.append( cd.getUsuariocontador() );
					sTablaDetalleBatch.append("</td>");
					
					sTablaDetalleBatch.append("</tr>");
				}
				sTablaDetalleBatch.append("</table>");
				
				//&& ========= Generar el encabezado del correo.
				if(vfUsuarioCrea != null){
					sbTablaCorreo.append( 
						"<div style=\"text-align: left; margin: 10px 0 10px 15%;  width:100%; \"> <b>Sr(a): "+ 
							Divisas.ponerCadenaenMayuscula(vfUsuarioCrea.getId().getAbalph().trim())+" </b>" +
						" </div> ");
				}
				
				sbTablaCorreo.append("<table width=\"800px\" style=\"border: 1px #7a7a7a solid\" align=\"center\" cellspacing=\"0\" cellpadding=\"3\">");
				sbTablaCorreo.append("<tr>"); 
				sbTablaCorreo.append("<th colspan=\"2\" style=\"border-bottom: 1px #7a7a7a solid; background: #3e68a4\">");
				sbTablaCorreo.append("<font face=\"Arial\" size=\"2\" color=\"white\"><b>Notificación de Batch's generados automáticos</b></font>");
				sbTablaCorreo.append("</th>");
				sbTablaCorreo.append("</tr>");
				
				String sEncabezado = "Los siguientes batch's se han generado por preconciliación de depósitos Caja Vs Banco";;
				
				
				sbTablaCorreo.append("<tr>");
				sbTablaCorreo.append("<td align:center colspan=\"2\" style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"90%\">");
				sbTablaCorreo.append(sEncabezado);
				sbTablaCorreo.append(" ==> Fecha: "+new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a").format(new Date()));
				sbTablaCorreo.append("</td>");
				sbTablaCorreo.append("</tr>");
				sbTablaCorreo.append("<tr>");
				sbTablaCorreo.append("<td colspan=\"2\" height=\"20px\">");
				sbTablaCorreo.append("</td>");
				sbTablaCorreo.append("</tr>");
				
				sbTablaCorreo.append("<tr>");
				sbTablaCorreo.append("<td colspan=\"2\" align=\"center\" >");
				sbTablaCorreo.append(sTablaDetalleBatch);
				sbTablaCorreo.append("</td>");
				sbTablaCorreo.append("</tr>");
				
				sbTablaCorreo.append("<tr>");
				sbTablaCorreo.append("<td align=\"center\" colspan=\"2\" style=\"font-family: Arial, Helvetica, sans-serif;font-size: 10px;");
				sbTablaCorreo.append("color: black; border-bottom: 1px ##1a1a1a solid; \">");
				sbTablaCorreo.append("<b>Casa Pellas, S. A. - Módulo de Caja</b>");
				sbTablaCorreo.append("</td>");	
				sbTablaCorreo.append("</tr>");
				sbTablaCorreo.append("</table>");
				
				String sHtml = sbTablaCorreo.toString();
				
				String sNombreTo = Divisas.ponerCadenaenMayuscula(vfUsuarioCrea.getId().getAbalph().trim());
				String sTo	= vfUsuarioCrea.getId().getWwrem1().trim();
				
				if( !sTo.toUpperCase().matches(PropertiesSystem.REGEXP_EMAIL_ADDRESS)){
					return false;
				}
				
				List<CustomEmailAddress> toList = new ArrayList<CustomEmailAddress>();
				
				List<String[]>lstCtaCorreo = new ArrayList<String[]>();
				lstCtaCorreo.add(new String[]{sNombreTo, sTo.toLowerCase()});
			
				lstCtaCorreo.add(new String[]{ vfUsuarioCrea.getId().getAbalph().trim().toLowerCase(), "lfonseca@casapellas.com.ni"});
				
				for (String[] sCtaCorreo : lstCtaCorreo) {
					toList.add(new CustomEmailAddress(sCtaCorreo[1], sCtaCorreo[0]));
				}
				
				MailHelper.SendHtmlEmail(
						new CustomEmailAddress(PropertiesSystem.WEBMASTER_EMAIL_ADRESS, "Módulo de Caja: Confirmación depósitos"),
						toList, 
						null, 
						new CustomEmailAddress(PropertiesSystem.MAIL_BOUNCEADDRESS), 
						"Confirmación de depósitos: Notificación de Generación de Batch's", sHtml);
				
			} catch (Exception e) {
				e.printStackTrace();
				bHecho = false;
			}
			return bHecho;
		}
	
	
/******************************************************************************************/
/** Método: Enviar Correo de notificacion
 *	Fecha:  25/10/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */	
	public boolean bEnviaCorreoBatch(List<Conciliacion>lstConcilia, int usrReversion, int iTipoConfirma){
		boolean bHecho = true;
		try {
			
			Vf0101 vfUsrReversion = EmpleadoCtrl.buscarEmpleadoxCodigo2( usrReversion );
			Vf0101 vfUsrCreacion =  EmpleadoCtrl.buscarEmpleadoxCodigo2( lstConcilia.get(0).getUsrcrea() );
			
			StringBuilder sTituloDet = new StringBuilder();
			StringBuilder sTablaDetalleBatch = new StringBuilder();
			StringBuilder sbTablaCorreo = new StringBuilder();
			
			String sEstiloTdhdr="", sEstiloTD="";
			
			sEstiloTdhdr += "style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color:white; ";
			sEstiloTdhdr += " background-color: #5e89b5;\" align=\"center\" ";
			sEstiloTD += " style=\" font-family: Arial,Helvetica, sans-serif;font-size: 12px;color: #1a1a1a; border-color: silver; ";
			sEstiloTD += " border-style: dashed; border-width: 1px\" ";
			
			sTablaDetalleBatch.append("<table width=\"750px\" style=\"border-color: silver; border-width: 1px; border-style: solid\">");
			sTablaDetalleBatch.append("<tr>");
			sTablaDetalleBatch.append("<td height=\"25px%\" colspan = \"9\" "+sEstiloTD+ " valign=\"bottom\" align=\"left\"  >");
			sTablaDetalleBatch.append("<b>Detalle de los Batch's:  </b>");
			sTablaDetalleBatch.append("</td>");
			sTablaDetalleBatch.append("</tr>");
			
			sTituloDet.append("<tr>");			
			sTituloDet.append("<td "+sEstiloTdhdr+ ">");
			sTituloDet.append("<b>N°</b>");
			sTituloDet.append("</td>");
			
			sTituloDet.append("<td "+sEstiloTdhdr+ ">");
			sTituloDet.append("<b>Batch</b>");
			sTituloDet.append("</td>");
			
			sTituloDet.append("<td "+sEstiloTdhdr+ ">");
			sTituloDet.append("<b>Documento</b>");
			sTituloDet.append("</td>");
			
			sTituloDet.append("<td "+sEstiloTdhdr+ ">");
			sTituloDet.append("<b>Tipo</b>");
			sTituloDet.append("</td>");
			
			sTituloDet.append("<td "+sEstiloTdhdr+ ">");
			sTituloDet.append("<b>Monto</b>");
			sTituloDet.append("</td>");
			
			sTituloDet.append("<td "+sEstiloTdhdr+ ">");
			sTituloDet.append("<b>Moneda</b>");
			sTituloDet.append("</td>");
			
			sTituloDet.append("<td "+sEstiloTdhdr+ ">");
			sTituloDet.append("<b>Fecha</b>");
			sTituloDet.append("</td>");
			
			sTituloDet.append("<td "+sEstiloTdhdr+ ">");
			sTituloDet.append("<b>Creado Por</b>");
			sTituloDet.append("</td>");
			
			sTituloDet.append("<td "+sEstiloTdhdr+ ">");
			sTituloDet.append("<b>Contador</b>");
			sTituloDet.append("</td>");
			sTituloDet.append("</tr>");
			
			sTablaDetalleBatch.append(sTituloDet);
			
			//&& ====== Recorrer los registros de conciliaciones.
			for (Conciliacion cn : lstConcilia) {
				sTablaDetalleBatch.append("<tr>");
				
				sTablaDetalleBatch.append("<td align=\"right\" " +sEstiloTD+ " >");
				sTablaDetalleBatch.append(cn.getIdconciliacion());
				sTablaDetalleBatch.append("</td>");

				sTablaDetalleBatch.append("<td align=\"right\" " +sEstiloTD+ " >");
				sTablaDetalleBatch.append(cn.getNobatch());
				sTablaDetalleBatch.append("</td>");
				
				sTablaDetalleBatch.append("<td align=\"right\" " +sEstiloTD+ " >");
				sTablaDetalleBatch.append(cn.getNoreferencia());
				sTablaDetalleBatch.append("</td>");
				
				sTablaDetalleBatch.append("<td align=\"center\" " +sEstiloTD+ "  >");
				sTablaDetalleBatch.append(cn.getTipodoc());
				sTablaDetalleBatch.append("</td>");
				
				sTablaDetalleBatch.append("<td align=\"right\" " +sEstiloTD+ " >");
				sTablaDetalleBatch.append(new Divisas().formatDouble(cn.getMonto().doubleValue()));
				sTablaDetalleBatch.append("</td>");
				
				sTablaDetalleBatch.append("<td align=\"center\" " +sEstiloTD+ " >");
				sTablaDetalleBatch.append(cn.getMoneda());
				sTablaDetalleBatch.append("</td>");
				
				sTablaDetalleBatch.append("<td align=\"center\" " +sEstiloTD+ " >");
				sTablaDetalleBatch.append(  FechasUtil.formatDatetoString(cn.getFechacrea(), "dd/MM/yyyy"));
				sTablaDetalleBatch.append("</td>");
				
				Usuario usuario = UsuarioCtrl.obtenerUsuarioENSxCodigo(cn.getUsrcrea());
				sTablaDetalleBatch.append("<td align=\"center\" " +sEstiloTD+ " >");
				sTablaDetalleBatch.append( usuario.getLogin() +" || " + cn.getUsrcrea()  );
				sTablaDetalleBatch.append("</td>");
				
				sTablaDetalleBatch.append("<td align=\"center\" " +sEstiloTD+ " >");
				sTablaDetalleBatch.append( cn.getUsuariocomprobante() +" || " + cn.getCodigousrcomprobante()  );
				sTablaDetalleBatch.append("</td>");
				
				sTablaDetalleBatch.append("</tr>");
				
			}
			sTablaDetalleBatch.append("</table>");
			
			//&& ========= Generar el encabezado del correo.
			if(vfUsrReversion != null){
				sbTablaCorreo.append( 
					"<div style=\"text-align: left; margin: 10px 0 10px 15%;  width:100%; \"> <b>Sr(a): "+ 
						Divisas.ponerCadenaenMayuscula(vfUsrReversion.getId().getAbalph().trim())+" </b>" +
					" </div> ");
			}
			
			sbTablaCorreo.append("<table width=\"800px\" style=\"border: 1px #7a7a7a solid\" align=\"center\" cellspacing=\"0\" cellpadding=\"3\">");
			sbTablaCorreo.append("<tr>"); 
			sbTablaCorreo.append("<th colspan=\"2\" style=\"border-bottom: 1px #7a7a7a solid; background: #3e68a4\">");
			sbTablaCorreo.append("<font face=\"Arial\" size=\"2\" color=\"white\"><b>Notificación de Batch's generados automáticos</b></font>");
			sbTablaCorreo.append("</th>");
			sbTablaCorreo.append("</tr>");
			
			String sEncabezado = "Los siguientes batch's se han generado por confirmación de depósitos Caja Vs Banco";;
			switch (iTipoConfirma) {
			case 3: 
				sEncabezado = "Este asiento de diario fue borrado por  anulación de confirmación de depósito";
				break;
			case 4: 
				sEncabezado = "El asiento de diario fue generado por la reversión de confirmación de depósitos";
				break;
			}
			
			sbTablaCorreo.append("<tr>");
			sbTablaCorreo.append("<td align:center colspan=\"2\" style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"90%\">");
			sbTablaCorreo.append(sEncabezado);
			sbTablaCorreo.append(" ==> Fecha: "+new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a").format(new Date()));
			sbTablaCorreo.append("</td>");
			sbTablaCorreo.append("</tr>");
			sbTablaCorreo.append("<tr>");
			sbTablaCorreo.append("<td colspan=\"2\" height=\"20px\">");
			sbTablaCorreo.append("</td>");
			sbTablaCorreo.append("</tr>");
			
			sbTablaCorreo.append("<tr>");
			sbTablaCorreo.append("<td colspan=\"2\" align=\"center\" >");
			sbTablaCorreo.append(sTablaDetalleBatch);
			sbTablaCorreo.append("</td>");
			sbTablaCorreo.append("</tr>");
			
			sbTablaCorreo.append("<tr>");
			sbTablaCorreo.append("<td align=\"center\" colspan=\"2\" style=\"font-family: Arial, Helvetica, sans-serif;font-size: 10px;");
			sbTablaCorreo.append("color: black; border-bottom: 1px ##1a1a1a solid; \">");
			sbTablaCorreo.append("<b>Casa Pellas, S. A. - Módulo de Caja</b>");
			sbTablaCorreo.append("</td>");	
			sbTablaCorreo.append("</tr>");
			sbTablaCorreo.append("</table>");
			
			String sHtml = sbTablaCorreo.toString();
			
			List<CustomEmailAddress> ccList = new ArrayList<CustomEmailAddress>();
			for (String dtaCc : PropertiesSystem.MAILBCCSCON) {
				ccList.add(new CustomEmailAddress(dtaCc.split(PropertiesSystem.SPLIT_CHAR)[0], dtaCc.split(PropertiesSystem.SPLIT_CHAR)[1]));
			}
			
			List<String[]>lstCtaCorreo = new ArrayList<String[]>();
			lstCtaCorreo.add(new String[] {
					vfUsrReversion.getId().getAbalph().trim(),
					vfUsrReversion.getId().getWwrem1().trim().toLowerCase() });
			lstCtaCorreo.add(new String[] {
					vfUsrCreacion.getId().getAbalph().toLowerCase(),
					vfUsrCreacion.getId().getWwrem1().trim().toLowerCase() });
			
			String strListEmail = PropertiesSystem.MAIL_INTERNAL_ADDRESS;
			String[] listEmail = strListEmail.split(",");
			
			List<CustomEmailAddress> toList = new ArrayList<CustomEmailAddress>();
			for (String strEmail : listEmail) {
				toList.add(new CustomEmailAddress(strEmail));
			}
			
			if (toList.size() > 0) {
				MailHelper.SendHtmlEmail(
						new CustomEmailAddress(PropertiesSystem.WEBMASTER_EMAIL_ADRESS, "Módulo de Caja: Confirmación depósitos"),
						toList, ccList, new CustomEmailAddress(PropertiesSystem.MAIL_BOUNCEADDRESS), 
						"Confirmación de depósitos: Notificación de Generación de Batch's", sHtml);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			bHecho = false;
			errorDetalle = e;
			error = new Exception("@CONCILIACION: El correo del contador contiene valores incorrectos! ");
		}
		return bHecho;
	}
/******************************************************************************************/
/** Método: Actualizar el estado para el registro de los ajustes.
 *	Fecha:  25/10/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */	
	public boolean enviarNotificacionBatch(List<Conciliacion>lstConciliaciones, int iTipoConfirma){
		boolean bHecho = true;
		Integer[] iNoConcilias =  new Integer[lstConciliaciones.size()];
		List<Object[]>lstCodContador = null;
		
		Session sesion = null;		
		boolean newCn = false;
		
		
		try {
			
			sesion = HibernateUtilPruebaCn.currentSession();
			
			for (int i = 0; i < lstConciliaciones.size(); i++) 
				iNoConcilias[i] = lstConciliaciones.get(i).getIdconciliacion();				
			
			Criteria cr = sesion.createCriteria(Conciliacion.class);
			cr.setProjection(Projections.distinct(Projections.property("depsCaja.usrcreate")));
			cr.createAlias("conciliadets", "concdts");
			cr.createAlias("conciliadets.deposito", "depsCaja");
			cr.add(Restrictions.in("idconciliacion", iNoConcilias));
			
			//&& ====== Si es confirmacion manual, usar el primer codigo de contador, si no enviar un correo por contador 1.Manual
			if(iTipoConfirma != 2)
				cr.setMaxResults(1);
			
			lstCodContador = cr.list();
			
			if(lstCodContador == null || lstCodContador.isEmpty() ){
				error = new Exception("@CONCILIACION: Error al obtener los codigos de los contadores para notificaciones. ");
				return bHecho = false;
			}
			
			for (int i = 0; i < lstCodContador.size(); i++) {
				
				int iCodigoCont = Integer.parseInt(String.valueOf(lstCodContador.get(i)));
				
				List<Conciliacion>lstConci = (ArrayList<Conciliacion>)
					sesion.createCriteria(Conciliacion.class)
						.createAlias("conciliadets", "concdts")
						.createAlias("conciliadets.deposito", "depsCaja")
						.add(Restrictions.in("idconciliacion", iNoConcilias))
						.add(Restrictions.eq("depsCaja.usrcreate", iCodigoCont)).list();

				if(lstConci == null ||  lstConci.isEmpty() )
					continue;
				
				bEnviaCorreoBatch(lstConci, iCodigoCont, iTipoConfirma);
 
			}
		
  
		} catch (Exception e) {
			e.printStackTrace();
			bHecho = false;
			errorDetalle = e; 
			error = new Exception("@CONCILIACION: Error de sistema al enviar notificaciones de correo a contador. ");
		}
		return bHecho;
	}
	
	
/******************************************************************************************/
/** Método: Actualizar el estado para el registro de los ajustes.
 *	Fecha:  08/10/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public boolean actualizarEstadoAjuste(Conciliacion conciliacion, Session sesionCajaW, int iEstado, int iCodusermod){
		boolean bHecho = true;
		
		try {
			Criteria cr = sesionCajaW.createCriteria(Ajusteconc.class);
			cr.add(Restrictions.eq("conciliacion.idconciliacion", conciliacion.getIdconciliacion()));
			
			Object ob = cr.uniqueResult();
			if(ob!=null){
				Ajusteconc ajusteNuevo = (Ajusteconc)cr.uniqueResult();
				ajusteNuevo.setFechamod(new Date());
				ajusteNuevo.setUsrmod(iCodusermod);
				ajusteNuevo.setEstado(iEstado);
				sesionCajaW.update(ajusteNuevo);
			}
			
		} catch (Exception e) {
			bHecho = false;
			errorDetalle = e; 
			error = new Exception("@AJUSTECONC: Error de sistema al actualizar el estado para el ajuste asociado ");
//			System.out.println(":ConfirmaDepositosCtrl:  Excepción capturada en actualizarEstadoAjuste(): "+e);
			e.printStackTrace();
		}
		return bHecho;
	}
/******************************************************************************************/
/** Método: Actualizar el estado de la confirmacion, marcar como anulada.
 *	Fecha:  07/10/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public boolean anularConciliacionDepositos(Conciliacion conciliacion, int iNoBatchR, int iNodocR, int iCodEstado,
											int iCoduser, Session sesionCajaW){
		boolean bHecho = true;
		try {
			Criteria cr = sesionCajaW.createCriteria(Conciliacion.class);
			cr.add(Restrictions.eq("idconciliacion", conciliacion.getIdconciliacion()));
			Conciliacion conciliaNueva = (Conciliacion)cr.uniqueResult();
			
			conciliaNueva.setEstado(iCodEstado);
			conciliaNueva.setUsrmod(iCoduser);
			conciliaNueva.setFechamod(new Date());
			conciliaNueva.setRnobatch(iNoBatchR);
			conciliaNueva.setRnodoc(iNodocR);
			sesionCajaW.update(conciliaNueva);
			
			cr = sesionCajaW.createCriteria(Conciliadet.class);
			List<Conciliadet>lstCndets = cr.list();
			if(lstCndets!=null && lstCndets.size()>0){
				for (Conciliadet cd : lstCndets) {
					cd.setUsrmod(iCoduser);
					cd.setFechamod(new Date());
					sesionCajaW.update(cd);
				}
			}
		
		} catch (Exception e) {
			bHecho = false;
			errorDetalle = e; 
			error = new Exception("@Conciliacion: Error de sistema al actualizar el estado de la confirmacion de deposito "+conciliacion.getNoreferencia());
//			System.out.println(":ConfirmaDepositosCtrl:  Excepción capturada en anularConciliacionDepositos(): "+e);
			e.printStackTrace();
		}
		return bHecho;
	}
/******************************************************************************************/
/** Método: Obtener el registr de una conciliacion por su id
 *	Fecha:  03/10/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */	
	public Conciliacion obtenerConciliacionxId( int iIdConciliacion ){
		Conciliacion concilia = null;
		
		try {
			
			String sql = "select * from "+PropertiesSystem.ESQUEMA +".conciliacion where idconciliacion = "  + iIdConciliacion ; 
			
//			List<Conciliacion>results = (ArrayList<Conciliacion>)ConsolidadoDepositosBcoCtrl.executeQuery(sql, true, Conciliacion.class) ;
			List<Conciliacion>results = (ArrayList<Conciliacion>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, true, Conciliacion.class) ;
			concilia = results.get(0);
			
 
		} catch (Exception e) {
			e.printStackTrace();
			concilia = null;
			errorDetalle = e; 
			error = new Exception("@Error de sistema al intentar obtener datos de conciliacion  "+iIdConciliacion);
		}
		return concilia;
	}
/******************************************************************************************/
/** Método: Obtiene las lineas para un asiento de diario desde ALTDTA.F0911
 *	Fecha:  03/10/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */		 
	public List<Object[]> obtenerLineasF0911( int iNobatch, int iNodoc ){
		List<Object[]> lstResulF0911 = null;
		String sql = "";
		
		try {
			sql  = "SELECT ";
			sql += " CAST(GLKCO AS VARCHAR (5) CCSID 37), ";
			sql += " CAST(GLDCT AS VARCHAR (2) CCSID 37),";
			sql += " CAST(GLJELN AS  NUMERIC (7,2) ),";
			sql += " CAST(GLCO AS VARCHAR (5) CCSID 37),";
			sql += " CAST(GLANI AS VARCHAR (29) CCSID 37),";
			sql += " CAST(GLAID AS VARCHAR (8) CCSID 37),";
			sql += " CAST(GLMCU AS VARCHAR (12) CCSID 37),";
			sql += " CAST(GLOBJ AS VARCHAR (6) CCSID 37),";
			sql += " CAST(GLSUB AS VARCHAR (8) CCSID 37),";
			sql += " CAST(GLLT AS VARCHAR (2) CCSID 37),";
			sql += " CAST(GLCRCD AS VARCHAR (3) CCSID 37),";
			sql += " CAST(GLCRR AS DECIMAL (15,2) ),";
			sql += " CAST(GLAA AS NUMERIC (15) ),";
			sql += " CAST(GLEXR AS VARCHAR (30) CCSID 37),";
			sql += " CAST(GLCRRM AS VARCHAR (30) CCSID 37),";
			sql += " CAST(  (IFNULL(GLSBL,'UNO'))  AS VARCHAR (8) CCSID 37 ) AS GLSBL, ";
			sql += " CAST(  (IFNULL(GlSBLT,'UNO')) AS VARCHAR (1) CCSID 37 ) AS GlSBLT ";
					
			sql += " FROM ALTDTA.F0911"; 
			sql += " WHERE GLICU =  "+iNobatch +" AND GLDOC ="+iNodoc;
			sql += " AND GLDCT <> 'AE'";
			
//			lstResulF0911 = (ArrayList<Object[]>) ConsolidadoDepositosBcoCtrl.executeQuery(sql, true, null) ;
			lstResulF0911 = (ArrayList<Object[]>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, true, null) ;
			
			if(lstResulF0911 == null || lstResulF0911.isEmpty() ){
				lstResulF0911 = null;
				error = new Exception("@No se ha podido obtener el detalle de asiento de diario para batch # "+iNobatch); 
				errorDetalle = new Exception("@No se ha podido obtener el detalle de asiento de diario para batch # "+iNobatch);
			}
			
		} catch (Exception e) {
			lstResulF0911 = null;
			errorDetalle = e; 
			error = new Exception("@Error de sistema al intentar obtener el detalle de asiento de dario de la confirmacion ");
//			System.out.println(":ConfirmepositosCtrl:  Excepción capturada en obtenerLineasF0911(): "+e);
			e.printStackTrace();
		}
		return lstResulF0911;
	}  
/******************************************************************************************/
/** Método: Filtra confirmaciones de depositos a partir de los parametros.
 *	Fecha:  03/10/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */			
	public List<Vwconciliacion> buscarConfirmacionDepositos(String sIdBanco, String sIdCuenta,String sIdCaja,
										String sCodcomp,BigDecimal bdMontoBanco, String sReferBanco, BigDecimal bdMontoCaja,
										String sReferCaja,String sMoneda, Date dtCajaFechaIni, Date dtCajaFechaFin,
										Date dtBancoFechaIni, Date dtBancoFechaFin, int iRangoMonto,
										Date dtConfirFechaIni,Date dtConfirFechaFin ){
		List<Vwconciliacion>lstVwonciliacion = null;
		List<Object[]>lstResultado = null;
		Session sesion = null;		
		boolean newCn = false;
		
		try {
			
			sesion = HibernateUtilPruebaCn.currentSession();
					
			Criteria cr = sesion.createCriteria(Conciliacion.class);
			cr.setProjection(Projections.distinct(Projections.property("idconciliacion")));
			
			cr.createAlias("archivo", "arch");
			cr.createAlias("conciliadets", "concdts");
			cr.createAlias("conciliadets.deposito", "depsCaja");
			
			//&& ======= campos en la tabla principal conciliacion
			if(sIdCuenta.compareToIgnoreCase("")!=0)
				cr.add(Restrictions.eq("idcuenta", Integer.parseInt(sIdCuenta)));
			if(sMoneda.compareToIgnoreCase("")!=0)
				cr.add(Restrictions.eq("moneda", sMoneda));
			if(sReferBanco.compareToIgnoreCase("")!=0)
				cr.add(Restrictions.eq("noreferencia", Integer.parseInt(sReferBanco)));
			if(dtConfirFechaIni!=null)
				cr.add(Restrictions.sqlRestriction(" cast({alias}.fechacrea as date) >= '"
								+ FechasUtil.formatDatetoString(dtConfirFechaIni, "yyyy-MM-dd")+"'"));
			if(dtConfirFechaFin!=null)
				cr.add(Restrictions.sqlRestriction(" cast({alias}.fechacrea as date) <= '"
						+  FechasUtil.formatDatetoString(dtConfirFechaFin, "yyyy-MM-dd")+"'"));
			
			//&& ===== Monto del deposito de banco.
			if(bdMontoBanco.compareTo(BigDecimal.ZERO)!= 0){
				switch (iRangoMonto) {
				case 1: cr.add(Restrictions.le("monto", bdMontoBanco));					
				break;
				case 2:	cr.add(Restrictions.ge("monto", bdMontoBanco));
				break;
				case 3: cr.add(Restrictions.eq("monto", bdMontoBanco));
				break;
				}
			}
			//&& ======= Asociaciones
			if(sIdBanco.compareToIgnoreCase("")!=0)
				cr.add(Restrictions.eq("arch.idbanco", Integer.parseInt(sIdBanco)));
			if(sIdCaja.compareToIgnoreCase("")!=0)
				cr.add(Restrictions.eq("concdts.caid", Integer.parseInt(sIdCaja)));
			if(sCodcomp.compareToIgnoreCase("")!=0)
				cr.add(Restrictions.eq("concdts.codcomp", sCodcomp));
			if(sReferCaja.compareToIgnoreCase("")!=0)
				cr.add(Restrictions.eq("concdts.referdcaja", sReferCaja));
			if(bdMontoCaja.compareTo(BigDecimal.ZERO)!= 0){
				switch (iRangoMonto) {
				case 1: cr.add(Restrictions.le("depsCaja.monto", bdMontoCaja));					
				break;
				case 2:	cr.add(Restrictions.ge("depsCaja.monto", bdMontoCaja));
				break;
				case 3: cr.add(Restrictions.eq("depsCaja.monto", bdMontoCaja));
				break;
				}
			}
			//&& ====== Filtros de fecha para banco.
			if(dtBancoFechaIni!=null || dtBancoFechaFin!=null){
				cr.createAlias("conciliadets.depbancodet", "depbncodet");
				if(dtBancoFechaIni!=null)
					cr.add(Restrictions.ge("depbncodet.fechaproceso", dtBancoFechaIni));	
				if(dtBancoFechaFin!=null)
					cr.add(Restrictions.le("depbncodet.fechaproceso", dtBancoFechaFin));
			}
			//&& ====== Filtros de fecha para caja.
			if(dtBancoFechaIni!=null)
				cr.add(Restrictions.ge("depsCaja.fecha", dtBancoFechaIni));	
			if(dtBancoFechaFin!=null)
				cr.add(Restrictions.le("depsCaja.fecha", dtBancoFechaFin));

			//&& ============ Restricciones por usuario
			List<String[]>dta = new ArrayList<String[]>();
			dta.add(new String[]{"{alias}.idcuenta","1"});
			dta.add(new String[]{"{alias}.moneda","3"});
			dta.add(new String[]{"{alias}.usrcrea","4"});
		
			
			/*String sqlOrCtaConc = constructSqlOrCtaxCon(dta);
			if( !sqlOrCtaConc.isEmpty() ){
				cr.add( Restrictions.sqlRestriction(sqlOrCtaConc) );
			}*/
			
			lstResultado = cr.list();
			
			//&& ==== Convertir el resultado de la consulta a objetos de tipo Vwconcilacion.
			if(lstResultado != null && lstResultado.size()>0){
//				System.out.println("registros encontrados "+lstResultado.size());
				Integer[] iIdsConcilia = new Integer[lstResultado.size()];
				for (int i = 0; i < lstResultado.size(); i++) 
					iIdsConcilia[i] = Integer.parseInt(String.valueOf(lstResultado.get(i)));
				
				cr = sesion.createCriteria(Vwconciliacion.class);
				cr.add(Restrictions.in("id.idconciliacion", iIdsConcilia));
				lstVwonciliacion = cr.list();
			}
			
		} catch (Exception e) {e.printStackTrace();
			lstVwonciliacion = null;
			errorDetalle = e; 
			error = new Exception("@Error de sistema al intentar obtener los registros de confirmaciones ");
//			System.out.println(":ConfirmepositosCtrl:  Excepción capturada en obtenerConciliadet(): "+e);
			e.printStackTrace();
		}
		return lstVwonciliacion;
	}
/******************************************************************************************/
/** Método: Obtiene el registro de ajuste en caso de que se haya incluido en la confirmacion.
 *	Fecha:  03/10/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */		
	public Ajusteconc obtenerDetalleAjusteConfirma(int iIdConfirmacion){
		Ajusteconc ajuste = null;
		
		try {
			Session sesion = HibernateUtilPruebaCn.currentSession();			
			Criteria cr = sesion.createCriteria(Ajusteconc.class);
			cr.add(Restrictions.eq("conciliacion.idconciliacion", iIdConfirmacion));
			Object ob = cr.uniqueResult();
			
			if(ob==null){
				errorDetalle = new Exception("@No se obtuvo resultado en busqueda de ajustes");; 
				error 		 = new Exception("@No se obtuvo resultado en busqueda de ajustes");;		
			}else{
				ajuste = (Ajusteconc)ob;
			}			
		} catch (Exception e) {
			ajuste = null;
			errorDetalle = e; 
			error = new Exception("@Error de sistema al intentar obtener los registros de confirmaciones ");
//			System.out.println(":ConfirmaDepositosCtrl:  Excepción capturada en obtenerConciliadet(): "+e);
			e.printStackTrace();
		}
		return ajuste;
	}
/******************************************************************************************/
/** Método: Obtiene los depositos de caja asociados a una confirmacion de depositos.
 *	Fecha:  03/10/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */		
	public List<Vdeposito> obtenerDepositosCajaConfirmacion(int iIdConfirmacion){
		List<Vdeposito>lstDepositosCaja = new ArrayList<Vdeposito>();
		Session sesion = null; 
		
		
		
		try {

			sesion = HibernateUtilPruebaCn.currentSession();
			
			Criteria cr = sesion.createCriteria(Conciliadet.class);
			cr.add(Restrictions.eq("conciliacion.idconciliacion", iIdConfirmacion));
			List<Conciliadet>lstConcilia = cr.list();
			
			if(lstConcilia == null)
				return null;
			
			List<Integer> lstIdDepsCaja = new ArrayList<Integer>();
			for (Conciliadet cd : lstConcilia) {
				lstIdDepsCaja.add(cd.getDeposito().getConsecutivo());
			}
			
			lstDepositosCaja = (ArrayList<Vdeposito>) sesion.createCriteria(
							Vdeposito.class).add(Restrictions
							.in("id.consecutivo", lstIdDepsCaja)).list();
			
		} catch (Exception e) {
			e.printStackTrace();
			errorDetalle = e; 
			error = new Exception("@Error de sistema al intentar obtener los registros de confirmaciones ");
//			System.out.println(":ConfirmaDepositosCtrl:  Excepción capturada en obtenerConciliadet(): "+e);
			e.printStackTrace();
		}
		return lstDepositosCaja;
	}
/******************************************************************************************/
/** Método: Obtiene la lista de confirmaciones de depositos registrados.
 *	Fecha:  29/09/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */	
	public static List<Vwconciliacion> obtenerConfirmaciones(int iMaxresult){
		List<Vwconciliacion> lstConfirma = new ArrayList<Vwconciliacion>();
		Session sesion = null;		
		boolean newCn = false;
		
		try {
			
			sesion = HibernateUtilPruebaCn.currentSession();
			
			Criteria cr = sesion.createCriteria(Vwconciliacion.class)
							.add(Restrictions.eq("id.estado", 46));
			
			if(iMaxresult != 0)
				cr.setMaxResults(iMaxresult);
							
			List<String[]>dta = new ArrayList<String[]>();
			dta.add(new String[]{"idcuenta","1"});
			dta.add(new String[]{"moneda","3"});
			dta.add(new String[]{"usrcrea","4"});
		
			String sqlOrCtaConc = constructSqlOrCtaxCon(dta);
			if( !sqlOrCtaConc.isEmpty() ){
				cr.add( Restrictions.sqlRestriction(sqlOrCtaConc) );
			}
			 
			lstConfirma = cr.list();
			cr = null;
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lstConfirma;
	}
	
/******************************************************************************************/
/** Método: Guardar el detalle del ajuste, en caso de haberse generado.
 *	Fecha:  28/09/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public boolean guardarDetalleAjuste(Session sesion, Conciliacion conciliacion,int nobatch, int nodocumento,
				BigDecimal monto, int cargoa,int idcuenta, String tipodoc,int usrcrea, Date dtFecham){
		boolean bHecho = true;
		
		try {
	
			String insert = " insert into "+PropertiesSystem.ESQUEMA+".AJUSTECONC " +
			"(IDAJUSTEC, IDCONCILIACION, NOBATCH, NODOCUMENTO, MONTO, CARGOA, IDCUENTA, " +
			" IDTIPOAJUSTE, TIPODOC, USRCREA, USRMOD, FECHAMOD, FECHACREA, ESTADO)"+ 
			" values " +
			" (default,"+conciliacion.getIdconciliacion()+", "+nobatch+", "+nodocumento
				+", '"+monto.toString()+"', "+cargoa+","+idcuenta+" , "
				+ ((monto.compareTo(BigDecimal.ZERO) == 1 )? 49:48)+",'"
				+tipodoc+"', "+usrcrea+", "+usrcrea+", '"
				+FechasUtil.formatDatetoString(new Date(), "yyyy-MM-dd HH:mm:ss.ssss")+"', '"
				+FechasUtil.formatDatetoString(new Date(), "yyyy-MM-dd HH:mm:ss.ssss")+"', "+46+")";
//			System.out.println("Ajusteconc: "+insert);
			ConsolidadoDepositosBcoCtrl.executeSqlQueryTx(sesion, insert);

		} catch (Exception e) {
			e.printStackTrace();
			errorDetalle = e; 
			error = new Exception("@Error de sistema al tratar de registrar detalle de ajustes en confirmacion depósito "+conciliacion.getNoreferencia());
		}
		return bHecho;
	}
/******************************************************************************************/
/** Método: Obtener una lista de conciliadet a partir de la llave del deposito caja.
 *	Fecha:  23/09/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public Conciliadet obtenerConciliadet(int iConsect, int iCaid, String sCodcomp, String sCodsuc, int iNodeposito){
		Conciliadet cd = null;
		try {
			
			Session sesion = HibernateUtilPruebaCn.currentSession();
			
			Criteria cr = sesion.createCriteria(Conciliadet.class);
			
			cr.add(Restrictions.eq("caid", iCaid));
			cr.add(Restrictions.eq("codcomp", sCodcomp));
			cr.add(Restrictions.eq("codsuc", sCodsuc));
			cr.add(Restrictions.eq("nodeposito", iNodeposito));
			
			Object obCr = cr.uniqueResult();
			if(obCr!=null){
				cd = (Conciliadet)obCr;
				
				//&& ====== Buscar el maestro de la confirmacion
				cr = sesion.createCriteria(Conciliacion.class);
				cr.add(Restrictions.eq("idconciliacion", cd.getConciliacion().getIdconciliacion()));
				Conciliacion conciliacion = (Conciliacion)cr.uniqueResult();
				
				//&& ====== Buscar el archivo asociado a la confirmacion
				cr = sesion.createCriteria(Archivo.class);
				cr.add(Restrictions.eq("idarchivo", conciliacion.getArchivo().getIdarchivo()));
				Archivo archivo  = (Archivo)cr.uniqueResult();
				conciliacion.setArchivo(archivo);
				
				cd.setConciliacion(conciliacion);
				
			}
			else{
				error = new Exception("@No se obtuvo resultados de consulta para detalle conciliacion");
				errorDetalle =  new Exception("@No se obtuvo resultados de consulta para detalle conciliacion");
			}
			
		} catch (Exception e) {
			cd = null;
			errorDetalle = e; 
			error = new Exception("@Error de sistema al obtener el detalle de confirmacion para el deposito ");
//			System.out.println(":ConfirmaDepositosCtrl:  Excepción capturada en obtenerConciliadet(): "+e);
			e.printStackTrace();
		}
		return cd;
	}
	
/******************************************************************************************/
/** Método: Filtrar los depositos de caja coincidentes con los niveles seleccionados.
 *	Fecha:  08/09/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public List<Deposito> filtrarDepositosCaja(int iCaid, boolean bTipoTrans, boolean bRefer, boolean bMonto, boolean bMoneda, 
											Archivo arConfirmar, Depbancodet db, BigDecimal bdMinAjuste,
											BigDecimal bdMaxAjuste, BigDecimal bdMontoDesde, BigDecimal bdMontoHasta, 
											int iCompararComo, String sNoreferencia){
		List<Deposito>lstDepositosCaja = new ArrayList<Deposito>();
		Session sesion = null; 
		
		boolean newCn = false;
		
		try {
			
			sesion = HibernateUtilPruebaCn.currentSession();
			
			//&& ====== Obtener la compania asociada a la cuenta.
			F55ca023 f23 = (F55ca023)sesion.createCriteria(F55ca023.class)
			.add(Restrictions.eq("id.d3stat", "A"))
			.add(Restrictions.eq("id.d3crcd", arConfirmar.getMoneda()))
			.add(Restrictions.eq("id.d3codb", arConfirmar.getIdbanco()))
			.add(Restrictions.eq("id.d3nocuenta", new Long(db.getNocuenta())))
			.setMaxResults(1).uniqueResult();
			
			if(f23 == null){
				error = new Exception("@F55ca023: No se encontró configuración " +
						"de Compañía asociada a la cuenta");
				return null;
			}
			
			CodeUtil.putInSessionMap("cdb_F23CompCta", f23 );
			
			Criteria crCja = sesion.createCriteria(Deposito.class)
			.setMaxResults(60)
			.add(Restrictions.eq("idbanco", arConfirmar.getIdbanco()))
			.add(Restrictions.eq("estadocnfr",PropertiesSystem.DP_NOCONFIRMADO/*"SCR"*/))
			.add(Restrictions.eq("tipoconfr", PropertiesSystem.CFR_AUTO/*"CAM"*/))
			.add(Restrictions.eq("tipodep", "D"))
			.add(Restrictions.eq("codcomp", f23.getId().getD3rp01()))
			.add(Restrictions.not(Restrictions.in("mpagodep", new String[]{"X"," "})));

			if(iCaid != 0)
				crCja.add(Restrictions.eq("caid", iCaid));
			
			//&& ==== Incluir el valor de la referencia como filtro de comparacion
			if(bRefer){
				if(sNoreferencia.compareTo("")==0)
					sNoreferencia = String.valueOf(db.getReferencia());
				
				sNoreferencia = 
					(sNoreferencia.length() > f23.getId().getD3digconc())?
						sNoreferencia.substring(
							sNoreferencia.length() - f23.getId().getD3digconc(),
							sNoreferencia.length()):
						sNoreferencia;
				crCja.add(Restrictions.sqlRestriction(" referencia like '%"+sNoreferencia+"'"));
			}
			//&& ====== Comparar por condiciones de monto.
			if(bMonto){
				if( bdMontoDesde.compareTo(BigDecimal.ZERO) == 0 && bdMontoHasta.compareTo(BigDecimal.ZERO) == 0 )
					crCja.add(Restrictions
								.between("monto",db.getMtocredito().subtract(bdMinAjuste),
												 db.getMtocredito().add(bdMaxAjuste)));
				else{
					if(bdMontoDesde.compareTo(BigDecimal.ZERO) == 1 )
						crCja.add(Restrictions.ge("monto", bdMontoDesde));
					if(bdMontoHasta.compareTo(BigDecimal.ZERO) == 1 )
						crCja.add(Restrictions.le("monto", bdMontoHasta));
				}
			}
			if(bMoneda)
				crCja.add(Restrictions.eq("moneda", arConfirmar.getMoneda()));
			
			lstDepositosCaja = crCja.list();
			
			if(lstDepositosCaja == null)
				lstDepositosCaja = new ArrayList<Deposito>();

		} catch (Exception e) {
			
			e.printStackTrace();
			errorDetalle = e; 
			error = new Exception("@Error de sistema al filtrar los depositos de caja para confirmacion manual ");
			 
		}
		return lstDepositosCaja;
	}
/******************************************************************************************/
/** Método: Obtener archivos por su id
 *	Fecha:  08/09/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public F55ca033 obtenerCtaTransitoriaxBanco(Archivo ar, Depbancodet dpb){
		F55ca033 f33 = null;
		Session sesion = null; 
		
		
		
		try {
			
			sesion = HibernateUtilPruebaCn.currentSession();
			
			//&& ==== Obtener compania asociada al numero de cuenta de banco.
			F55ca023 f23 = (F55ca023) sesion.createCriteria(F55ca023.class)
				.add(Restrictions.eq("id.d3stat", "A"))
				.add(Restrictions.eq("id.d3crcd", ar.getMoneda()))
				.add(Restrictions.eq("id.d3nocuenta",
						new Long(dpb.getNocuenta())))
				.setMaxResults(1).uniqueResult();
			
			if(f23 == null)
				return null;
			
			//&& ==== Obtener el monto minimo y maximo para ajustes de monto en la comparacion.
			f33 = (F55ca033)sesion.createCriteria(F55ca033.class)
			.add(Restrictions.eq("id.b3codb", ar.getIdbanco()))
			.add(Restrictions.eq("id.b3crcd", ar.getMoneda()))
			.add(Restrictions.eq("id.b3rp01", f23.getId().getD3rp01()))
			.setMaxResults(1).uniqueResult();
			
		} catch (Exception e) {
			
			e.printStackTrace();
			error = new Exception("@Error de sistemas al obtener la moneda base por banco y # de cuenta.");
			errorDetalle = e; 
			
		}
		return f33;
	}
/******************************************************************************************/
/** Método: Obtener archivos por su id
 *	Fecha:  08/09/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public Archivo obtenerArchivoxId(int idArchivo){
		Archivo archivo = null;
		Session sesion = null; 
		
		boolean newCn = false;
		
		try {

			sesion = HibernateUtilPruebaCn.currentSession();
			Criteria cr = sesion.createCriteria(Archivo.class); 
			cr.add(Restrictions.eq("idarchivo", idArchivo));
			
			archivo = (Archivo)cr.uniqueResult();
			
		} catch (Exception e) {
			
			e.printStackTrace();
			error = new Exception("@Error de sistemas al obtener archivo por su identificador.");
			errorDetalle = e; 
			
		}
		return archivo;
	}
/******************************************************************************************/
/** Método: Obtener los depositos, detalle de los documentos seleccionados.
 *	Fecha:  07/09/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public List<Depbancodet> obtenerDepositosxArchivos(List<Archivo>lstArchivos, int iCodBanco, int iNorefer, BigDecimal bdMonto){
		List<Depbancodet> lstDeps = new ArrayList<Depbancodet>();
		
		try {
			Session sesion = HibernateUtilPruebaCn.currentSession();
			
			Criteria cr = null;
			
			for (Archivo archivo : lstArchivos) {
				cr = sesion.createCriteria(Depbancodet.class);
				cr.add(Restrictions.eq("archivo.idarchivo", archivo.getIdarchivo()));
				cr.add(Restrictions.eq("idestadocnfr",  PropertiesSystem.ID_DP_NO_CONFIRMADO/*36*/));
				cr.add(Restrictions.eq("idtipoconfirm", PropertiesSystem.ID_CRF_AUTOMATICA/*32*/));
				cr.add(Restrictions.gt("mtocredito", BigDecimal.ZERO));
				cr.add(Restrictions.ne("codtransaccion", "FA"));
				
				if(iCodBanco!=0)
					cr.createCriteria("archivo").add(Restrictions.eq("idbanco", iCodBanco));
				if(iNorefer!=0)
					cr.add(Restrictions.sqlRestriction(" referencia like '%"+iNorefer+"%'"));
				if(bdMonto.compareTo(BigDecimal.ZERO) == 1)
					cr.add(Restrictions.eq("mtocredito", bdMonto));
				
				List<Depbancodet>lstDbTemp = cr.list();
				if(lstDbTemp != null && lstDbTemp.size() > 0){
					lstDeps.addAll(lstDbTemp);
				}
			}
			
		} catch (Exception e) {
			lstDeps = null;
			error = new Exception("@Error de sistemas al obtener el detalle de los depositos de archivos en banco.");
			errorDetalle = e; 
//			System.out.println(":ConfirmaDepositosCtrl:  Excepción capturada en obtenerDepositosxArchivos(): "+e);
			e.printStackTrace();
		}
		return lstDeps;
	}
/******************************************************************************************/
/** Método: Registrar registro de conciliacion y el detalle de la conciliacion.
 *	Fecha:  02/09/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public boolean registrarConciliacion(Archivo archivo, Depbancodet dpBco, List<Deposito>lstDepsCaja, 
										String sTipodoc, int iNobatch, int iNodoc, int iCoduser,Session sesionCajaW){
		boolean bHecho = true;
		Map<String, Object> m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		
		try {
			String insert1 = "insert into " + PropertiesSystem.ESQUEMA+".CONCILIACION "+
			        "(IDCONCILIACION, IDARCHIVO, MONTO, NOBATCH, NOREFERENCIA, MONEDA, " +
			        "TIPODOC, IDCUENTA, ESTADO, USRCREA, USRMOD, FECHAMOD, FECHACREA, " +
			        "RNOBATCH, RNODOC) values " +
			        "(default, "+archivo.getIdarchivo()+","+dpBco.getMtocredito()+","
			        +iNobatch+", "+iNodoc+", '"+archivo.getMoneda()+"','"+sTipodoc+"',"
			        +dpBco.getNocuenta()+", "+46+", "+iCoduser+", "+iCoduser+", '" 
			        +FechasUtil.formatDatetoString(new Date(), "yyyy-MM-dd HH:mm:ss.SSSSSS" )
			        +"', '"+FechasUtil.formatDatetoString(new Date(), "yyyy-MM-dd HH:mm:ss.SSSSSS" )+"', 0 ,0)";
		
			bHecho = ConsolidadoDepositosBcoCtrl.executeSqlQueryTx(sesionCajaW, insert1);
					
//			System.out.println("sql: "+insert1+ " >>" + ps.executeUpdate() ); 	
			
			//&& ==== Obtener el objeto de Base de datos creado para la conciliacion.
			Criteria cr = sesionCajaW.createCriteria(Conciliacion.class);
			cr.add(Restrictions.eq("archivo.idarchivo", archivo.getIdarchivo()));
			cr.add(Restrictions.eq("nobatch", iNobatch));
			cr.add(Restrictions.eq("noreferencia", iNodoc));
			cr.add(Restrictions.eq("monto", dpBco.getMtocredito()));
			cr.add(Restrictions.eq("idcuenta", dpBco.getNocuenta()));
			
			Conciliacion conciliacion = (Conciliacion)cr.uniqueResult();
			if(conciliacion == null){
				error = new Exception("@No se registró la conciliacion para depósito: "+ dpBco.getReferencia());
				return false;
			}
			m.put("cdb_RegistroConciliacion", conciliacion);
			
//			System.out.println("----------------- Depositos -----------------");
			for (Deposito dp : lstDepsCaja) {
				
				insert1 = "insert into " + PropertiesSystem.ESQUEMA+".CONCILIADET "+
				"(IDCONCILIADET, IDDEPOSITOBCO, REFERDCAJA,IDCONCILIACION,  "+
				"NODEPOSITO,CODSUC,CODCOMP,CAID,USRCREA,USRMOD,FECHAMOD, "+
				"FECHACREA,IDDEPOSITOCJA) values "+
				"(default, "+dpBco.getIddepbcodet()+", "+dp.getReferencia()+", "+
				conciliacion.getIdconciliacion()+", "+dp.getNodeposito()+", '"+
				dp.getCodsuc()+"', '"+dp.getCodcomp()+"', "+dp.getCaid()+", "+
				iCoduser+", "+iCoduser+", '"+
				FechasUtil.formatDatetoString(new Date(), "yyyy-MM-dd HH:mm:ss.SSSSSS" )+"', '"+
				FechasUtil.formatDatetoString(new Date(), "yyyy-MM-dd HH:mm:ss.SSSSSS" )+"', "+
				dp.getConsecutivo() +")" ;
				
				ConsolidadoDepositosBcoCtrl.executeSqlQueryTx(sesionCajaW, insert1);
			}
			
		} catch (Exception e) {
			bHecho = false;
			error = new Exception("@Error de sistema al crear Registro de Conciliación, Detalle conciliación, para confirmación de depósitos");
			errorDetalle = e; 
//			System.out.println(":ConfirmaDepositosCtrl:  Excepción capturada en registrarConciliacion(): "+e);
			e.printStackTrace();
		}
		return bHecho;
	}
/******************************************************************************************/
/** Método: Actualizar el estado del deposito de banco.
 *	Fecha:  02/09/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public boolean actualizarEstadoArchivo(Archivo ar, Session sesionCajaW, int iCoduser, Connection cn){
		boolean bHecho = true;
		
		try {
			
			String sqlDepBco = " select count(*) from "
					+ PropertiesSystem.ESQUEMA
					+ ".Depbancodet where idarchivo = " + ar.getIdarchivo()
					+ " and idestadocnfr = 35";
			
			
			
			PreparedStatement ps = cn.prepareStatement(sqlDepBco);
			ResultSet rs = ps.executeQuery();
			
			if(!rs.next()){
				return false;
			}
			
			int cantDepositos = rs.getInt(1);
			int nuevoestado = 0;
			
			if( cantDepositos >= ar.getCantlinea())
				nuevoestado = 43 ;
			if(cantDepositos == 0)
				nuevoestado = 45 ;
			if(cantDepositos > 0 && cantDepositos < ar.getCantlinea() )
				nuevoestado = 44 ;
			
			sqlDepBco = " update "+PropertiesSystem.ESQUEMA 
					+ ".Archivo set estadproc = "+nuevoestado+", usrmodi = " + iCoduser 
					+", fechamod = '"+FechasUtil.formatDatetoString(new Date(), "yyyy-MM-dd HH:mm:ss.SSSSSS" )
					+"' where idarchivo = "+ar.getIdarchivo() ;

			ps = cn.prepareStatement(sqlDepBco);
			ps.executeUpdate();			
			
			
			
			
		} catch (Exception e) {
			bHecho = false;
			error = new Exception("@ARCHIVO: Error al actualizar el archivo de  depositos en banco  "
								+ar.getNombre()+", "+ar.getIdbanco()+" Monto: "+ar.getMoneda());
			errorDetalle = e; 
			e.printStackTrace();
		}
		return bHecho;
	}
/******************************************************************************************/
/** Método: Actualizar el estado del deposito de banco.
 *	Fecha:  01/09/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public boolean confirmarDepositoBanco(Depbancodet dpBco, 
							Session sesionCajaW, int iEstado, int iTipo,
							int iCoduser, int iNuevaRefer, Connection cn){
		boolean bHecho = true;
		String sHistoria = "";
		try {
			
	
			
			if(dpBco.getReferencia()!= iNuevaRefer){
				sHistoria = dpBco.getHistoricomod();
				if(sHistoria==null || sHistoria.compareToIgnoreCase("")==0){
					sHistoria = "1:"+ FechasUtil.formatDatetoString(new Date(), "ddMMyyy")+":"+dpBco.getReferencia()+"@";
				}
				else{
					String[] sHistorico =  dpBco.getHistoricomod().split("@");
					int iConsec = Integer.parseInt(sHistorico[sHistorico.length-1].split(":")[1]);
					iConsec++;
					sHistoria = iConsec+":"+  FechasUtil.formatDatetoString(new Date(), "ddMMyyy")+":"+dpBco.getReferencia()+"@";
				}
			}
			
			String update = " update "+PropertiesSystem.ESQUEMA+"" +
			".Depbancodet set idtipoconfirm = "+iTipo +", idestadocnfr = "+ iEstado 
			+", fechamod = '"+FechasUtil.formatDatetoString(new Date(), "yyyy-MM-dd HH:mm:ss.SSSSSS")
			+"', usrmod = "+iCoduser +", referencia = "+iNuevaRefer 
			+", historicomod ='"+  ( dpBco.getHistoricomod()+sHistoria )
			+ "' where iddepbcodet = "+dpBco.getIddepbcodet()   
			+" and idarchivo = " + dpBco.getArchivo().getIdarchivo() ;
			
			PreparedStatement ps = cn.prepareStatement(update);
			ps.executeUpdate();
			
			
			// sesionCajaW.createCriteria(Depbancodet.class).setMaxResults(1).list();
			//cr = null;
			
		} catch (Exception e) {
			bHecho = false;
			e.printStackTrace();
			error = new Exception("@DEPBANCODET: Error al actualizar el deposito de  banco en marcar Confirmado "
								+dpBco.getIddepbcodet()+", "+dpBco.getReferencia()+", Monto: "+dpBco.getMtocredito());
			errorDetalle = e; 
		}
		return bHecho;
	}
	/******************************************************************************************/
	/** Método: Actualizar el estado del deposito de caja.
	 *	Fecha:  22/05/2023
	 *  Nombre: Milton Moises Pomares M.
	 */
		public boolean actualizarEstadoDeposito(Deposito_Report depCaja,   String sEstado, 
				String sTipoConf, String sReferBco,int iCoduser ){
			boolean bHecho = true;
			
			try {
				
				String update  = " update "+PropertiesSystem.ESQUEMA +".Deposito " 
						+ " set estadocnfr = '" + sEstado + "', tipoconfr = '" +sTipoConf.trim()
						+ "', referdep = '"+sReferBco+"', fechamod = '"
						+FechasUtil.formatDatetoString(new Date(), "yyyy-MM-dd")
						+"', usrconfr = "+iCoduser + ", horamod = '"
						+FechasUtil.formatDatetoString(new Date(), "HH.mm.ss")
						+"' where consecutivo = "+depCaja.getConsecutivo() 
						+" and caid = "+depCaja.getCaid() +" and trim(codcomp) ='"
						+depCaja.getCodcomp().trim()+"' and nodeposito = " + depCaja.getNodeposito();
	 
				//Ajustado el 2020-11-28
				//Por LFonseca
				//Se envia la session como parte del parametros en este caso es null
				bHecho = ConsolidadoDepositosBcoCtrl.executeSqlQueryTx(null,  update) ;
				
//				PreparedStatement ps = cn.prepareStatement(update);
//				ps.executeUpdate();
				
			} catch (Exception e) {
				e.printStackTrace();
				bHecho = false;
				error = new Exception("@DEPOSITO: Error al actualizar el deposito de caja en marcar Confirmado " +depCaja.getNodeposito()+", "+depCaja.getReferencia()+", Monto: "+depCaja.getMonto());
				errorDetalle = e; 
			}
			return bHecho;
		}
		
/******************************************************************************************/
/** Método: Actualizar el estado del deposito de caja.
 *	Fecha:  01/09/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public boolean actualizarEstadoDeposito(Deposito depCaja,   String sEstado, 
			String sTipoConf, String sReferBco,int iCoduser ){
		boolean bHecho = true;
		
		try {
			
			String update  = " update "+PropertiesSystem.ESQUEMA +".Deposito " 
					+ " set estadocnfr = '" + sEstado + "', tipoconfr = '" +sTipoConf.trim()
					+ "', referdep = '"+sReferBco+"', fechamod = '"
					+FechasUtil.formatDatetoString(new Date(), "yyyy-MM-dd")
					+"', usrconfr = "+iCoduser + ", horamod = '"
					+FechasUtil.formatDatetoString(new Date(), "HH.mm.ss")
					+"' where consecutivo = "+depCaja.getConsecutivo() 
					+" and caid = "+depCaja.getCaid() +" and trim(codcomp) ='"
					+depCaja.getCodcomp().trim()+"' and nodeposito = " + depCaja.getNodeposito();
 
			//Ajustado el 2020-11-28
			//Por LFonseca
			//Se envia la session como parte del parametros en este caso es null
			bHecho = ConsolidadoDepositosBcoCtrl.executeSqlQueryTx(null,  update) ;
			
//			PreparedStatement ps = cn.prepareStatement(update);
//			ps.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
			bHecho = false;
			error = new Exception("@DEPOSITO: Error al actualizar el deposito de caja en marcar Confirmado " +depCaja.getNodeposito()+", "+depCaja.getReferencia()+", Monto: "+depCaja.getMonto());
			errorDetalle = e; 
		}
		return bHecho;
	}
/******************************************************************************************/
/** Método: Realizar proceso de comparacion entre depositos de banco vs depositos caja.
 *	Fecha:  25/08/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public List<CoincidenciaDeposito> compararDepositosBancoCaja(int iCaid, Archivo arConfirmar,List<Integer>lstReferenciasExcluidas){
		List<CoincidenciaDeposito>lstAsociacion = new ArrayList<CoincidenciaDeposito>();
		Criteria crDetBco = null;
		Criteria crCja = null;
		List<Integer>lstReferYaUsadas = new ArrayList<Integer>();
		String sReferencia = "";
		Divisas dv = new Divisas();
		
		Session sesion = null;
		Transaction trans = null;
		boolean newCn = false;
		
		
		try {
 
			sesion = HibernateUtilPruebaCn.currentSession();
			trans = (newCn = !(sesion.getTransaction().isActive())) ? sesion
					.beginTransaction() : sesion.getTransaction();

			List<String[]>dta = new ArrayList<String[]>();
			dta.add(new String[]{"nocuenta","1"});
			String sqlOrCtaConc = constructSqlOrCtaxCon(dta);
			 	
			//&& ===== Consultar el detalle del archivo.
			crDetBco = sesion.createCriteria(Depbancodet.class);
			crDetBco.add(Restrictions.eq("archivo.idarchivo", arConfirmar.getIdarchivo()));
			crDetBco.add(Restrictions.eq("idestadocnfr",  PropertiesSystem.ID_DP_NO_CONFIRMADO));
			crDetBco.add(Restrictions.eq("idtipoconfirm", PropertiesSystem.ID_CRF_AUTOMATICA));
			crDetBco.add(Restrictions.ge("mtocredito", BigDecimal.ZERO));
			crDetBco.add(Restrictions.ne("codtransaccion", "FA"));
			
			if( !sqlOrCtaConc.isEmpty() ){
				crDetBco.add( Restrictions.sqlRestriction(sqlOrCtaConc) );
			}
			
			List<Depbancodet>lstDepositosBco = (ArrayList<Depbancodet>)crDetBco.list();
			
			if(lstDepositosBco== null || lstDepositosBco.size()== 0){
//				System.out.println(" no se encontro resultado ");
				error = new Exception("@No se pudo obtener el detalle del archivo: "+arConfirmar.getIdarchivo()+", los depositos notificados por banco ");
				return new ArrayList<CoincidenciaDeposito>();
			}
				
			//&& ==== Obtener compania asociada al numero de cuenta de banco.
			F55ca023 f23 = (F55ca023)sesion.createCriteria(F55ca023.class)
				.add(Restrictions.eq("id.d3stat", "A"))
				.add(Restrictions.eq("id.d3crcd", arConfirmar.getMoneda()))
				.add(Restrictions.eq("id.d3codb", arConfirmar.getIdbanco()))
				.add(Restrictions.eq("id.d3nocuenta", new Long(lstDepositosBco
						.get(0).getNocuenta())))
				.uniqueResult();
			
			if(f23 == null){
				error = new Exception("@F55ca023: No se encontro configuración para compañía asociada al numero de cuenta: "
										+lstDepositosBco.get(0).getNocuenta()+" "+arConfirmar.getMoneda());
				return null;
			}
			
			//&& ==== Obtener el monto minimo y maximo para ajustes de monto en la comparacion.
			 
			F55ca033 f33 = (F55ca033)sesion.createCriteria(F55ca033.class)
				.add(Restrictions.eq("id.b3codb", arConfirmar.getIdbanco()))
				.add(Restrictions.eq("id.b3crcd", arConfirmar.getMoneda()))
				.add(Restrictions.eq("id.b3rp01", f23.getId().getD3rp01()))
				.uniqueResult();
			if(f33 == null){
				error = new Exception("@F55ca033: No se encontro configuración numero de cuenta de banco: "
											+lstDepositosBco.get(0).getNocuenta()+" "+arConfirmar.getMoneda()
											+" por compañia " + f23.getId().getD3rp01() );
				return null;
			}
			
			String sFactor = "";
			int i=0;
			lstReferYaUsadas.addAll(lstReferenciasExcluidas);
			for (Depbancodet db : lstDepositosBco) {
//				System.out.println("===== siguiente "+ (i++) +"=======");
			
				if(i%60==0){
					sesion.flush();
					sesion.clear();
				}
				
				sReferencia = String.valueOf(db.getReferencia());
				sReferencia	= (sReferencia.length()>8)?
									sReferencia.substring(sReferencia.length()-8,sReferencia.length()):
									sReferencia;
//				System.out.println(db.getReferencia()+" vs "+sReferencia);	
				
				crCja = null;
				crCja = sesion.createCriteria(Deposito.class)
				.add(Restrictions.eq("idbanco", arConfirmar.getIdbanco())) 
				.add(Restrictions.eq("moneda", arConfirmar.getMoneda())) 
				.add(Restrictions.eq("codcomp", f23.getId().getD3rp01()))
				.add(Restrictions.eq("referencia", sReferencia)) 
				.add(Restrictions.between("monto",
						db.getMtocredito().subtract(f33.getId().getAjustemin()), 
						db.getMtocredito().add(f33.getId().getAjustemax()))) 
				.add(Restrictions.eq("estadocnfr",PropertiesSystem.DP_NOCONFIRMADO )) 
				.add(Restrictions.eq("tipoconfr", PropertiesSystem.CFR_AUTO )) 
				.add(Restrictions.eq("tipodep", "D"))
				.add(Restrictions.not(Restrictions.in("tipodep", new String[]{"X"," "})));

				if(iCaid != 0)
					crCja.add(Restrictions.eq("caid", iCaid));
				
				if(lstReferYaUsadas.size()>0)
					crCja.add(Restrictions.not(Restrictions.in("consecutivo", lstReferYaUsadas)));
				
				List<Deposito> lstDcCoinciden = crCja.list();
				if(lstDcCoinciden != null && lstDcCoinciden.size()==1){
					
//					System.out.println("Coincidencias: "+lstReferYaUsadas.size()+" Encontro la coincidencia en Banco: "+db.getIddepbcodet() 
//								+" con Caja "+lstDcCoinciden.get(0).getCaid() + " "+lstDcCoinciden.get(0).getConsecutivo());
					
					CoincidenciaDeposito cdep = new CoincidenciaDeposito(); 
					cdep.setArchivo(arConfirmar);
					cdep.setDepbancodet(db);
					cdep.setDeposito(lstDcCoinciden.get(0));
					cdep.setDigitosCompara(f23.getId().getD3digconc());
					lstReferYaUsadas.add(lstDcCoinciden.get(0).getConsecutivo());
					
					//&& ====== factor de ajuste.
					sFactor = "";
					sFactor = "-"+dv.formatDouble(f33.getId().getAjustemin().doubleValue());
					sFactor +="/"+dv.formatDouble(f33.getId().getAjustemax().doubleValue());
					cdep.setRangoajuste(sFactor);
					
					double dAjuste = db.getMtocredito().subtract(lstDcCoinciden.get(0).getMonto()).doubleValue();
					dAjuste = new Divisas().roundDouble(dAjuste);
					
					if(dAjuste==0){
						cdep.setAjuste(false);
						cdep.setMontoXajuste(0.00);
					}else{
						cdep.setAjuste(true);
						cdep.setMontoXajuste(dAjuste);
					}
					lstAsociacion.add(cdep); 
				}
			}
 
		} catch (Exception e) {
			
			e.printStackTrace();
			
			error = new Exception("@Error al realizar la comparación de depósitos caja vs banco.");
			errorDetalle = e;
//			System.out.println(":ConfirmaDepositosCtrl:  Excepción capturada en compararDepositosBancoCaja(): "+e);
			e.printStackTrace();
		}finally{
			
			try {
				if(newCn){
					try {  trans.commit(); } 
					catch (Exception e2) { }
					try {  HibernateUtilPruebaCn.closeSession(sesion); }
					catch (Exception e2) { }
				}
				sesion = null;
				trans = null;
				crCja = null;
				crDetBco = null;
				
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return lstAsociacion;
	}
	/******************************************************************************************/
	/** Método: buscar registros Deposito, de depositos de caja a partir de los parametros obtenidos.
	 *	Fecha:  23/08/2011
	 *  Nombre: Carlos Manuel Hernández Morrison.
	 */
	public List<Deposito> filtrarDepositosCaja(int iCaid, String iNorefer, BigDecimal bdMontoDesde,
												BigDecimal bdMontoHasta,
												String sCodcomp, String sCodsuc,String sMoneda,
												Date dtFechaIni, Date dtFechaFin, String sEstadoDep,
												List<Deposito>lstDpCajaSelect, int iRangoMonto){
		List<Deposito> lstDepositosCaja = null;
		Session sesion = null; 
		
		boolean newCn = false;
		
		try {

			sesion = HibernateUtilPruebaCn.currentSession();
			
			Criteria cr = sesion.createCriteria(Deposito.class) ;

			if(iCaid>0)
				cr.add(Restrictions.eq("caid", iCaid));
			if(!iNorefer.equals(""))
				cr.add(Restrictions.sqlRestriction(" referencia like '%"+iNorefer+"'"));
			
			if(bdMontoDesde.compareTo(BigDecimal.ZERO) == 1 )
				cr.add(Restrictions.ge("monto", bdMontoDesde));
			if(bdMontoHasta.compareTo(BigDecimal.ZERO) == 1 )
				cr.add(Restrictions.le("monto", bdMontoHasta));
			
			if(!sCodcomp.equals(""))
				cr.add(Restrictions.eq("codcomp", sCodcomp));
			if(!sCodsuc.equals(""))
				cr.add(Restrictions.eq("codsuc", sCodsuc));
			if(!sMoneda.equals(""))
				cr.add(Restrictions.eq("moneda", sMoneda));
			if(dtFechaIni != null)
				cr.add(Restrictions.ge("fecha", dtFechaIni));
			if(dtFechaFin != null)
				cr.add(Restrictions.le("fecha", dtFechaFin));
			
			cr.add(Restrictions.eq("tipodep", "D"));
			
			List<String[]>dta = new ArrayList<String[]>();
			dta.add(new String[]{"idbanco","0"});
			dta.add(new String[]{"codcomp","2"});
			dta.add(new String[]{"moneda","3"});
			String sqlOrCtaConc = constructSqlOrCtaxCon(dta);
			if( !sqlOrCtaConc.isEmpty() ){
				cr.add( Restrictions.sqlRestriction(sqlOrCtaConc) );
			}
			
			if(sEstadoDep.compareTo("")!=0){
				cr.add(Restrictions.eq("estadocnfr", sEstadoDep));
				cr.add(Restrictions.not(Restrictions.in("mpagodep", new String[]{"X"," "})));
				
				if(lstDpCajaSelect != null && lstDpCajaSelect.size()>0){
					List<String>lstReferencias = new ArrayList<String>();
					for (Deposito dp : lstDpCajaSelect) {
						lstReferencias.add(dp.getReferencia());
					}
					cr.add(Restrictions.not(Restrictions.in("referencia", lstReferencias)));
				}
			}
			
			lstDepositosCaja =  cr.list();
			
			
		} catch (Exception e) {
			e.printStackTrace();
			lstDepositosCaja = null;
			error = e;
		}
		return lstDepositosCaja;
	}
	/******************************************************************************************/
	/** Método: obtener detalle de deposito de caja.
	 *	Fecha:  17/08/2011
	 *  Nombre: Carlos Manuel Hernández Morrison.
	 */
	public Vdeposito obtenerDetalleDepositoCaja(int iConsecutivoId, int iNodeposito, int iCaid, String sCodsuc, String sCodcomp){
		Vdeposito vdDetalleDepCaja = null;
		
		try {
			Session sesion = HibernateUtilPruebaCn.currentSession();
						
			Criteria cr = sesion.createCriteria(Vdeposito.class);
			cr.add(Restrictions.eq("id.consecutivo", iConsecutivoId));
			cr.add(Restrictions.eq("id.nodeposito", iNodeposito));
			cr.add(Restrictions.eq("id.caid", iCaid));
			cr.add(Restrictions.eq("id.codsuc",sCodsuc ));
			cr.add(Restrictions.eq("id.codcomp", sCodcomp));
			
			Object ob = cr.uniqueResult();
			if(ob==null){
				error = new Exception("No se ha podido obtener información de depósito desde sistema caja");
			}else{
				vdDetalleDepCaja = (Vdeposito)ob;
			}
			
		} catch (Exception e) {
			vdDetalleDepCaja = null;
			error = e;
//			System.out.println(":ConfirmaDepositosCtrl:  Excepción capturada en obtenerDetalleDeposito(): "+e);
			e.printStackTrace();
		}
		return vdDetalleDepCaja;
	}
	/******************************************************************************************/
	/** Método: filtrar archivos de banco a partir de parametros.
	 *	Fecha:  17/08/2011
	 *  Nombre: Carlos Manuel Hernández Morrison.
	 */
	public List<Archivo> filtrarArchivosBanco(String sNombre,String sBancoId, String sCuenta,Integer[] sIdEstados,
											  String sNoReferencia, Date dtFechaIni, Date dtFechaFin,Integer[] sIdsArchivos ){
		List<Archivo> lstarchivos = null;
		Session sesion = null; 
				
		try{
			
			sesion = HibernateUtilPruebaCn.currentSession();
					
			Criteria cr = sesion.createCriteria(Archivo.class).setMaxResults(50);
			if(!sNombre.trim().equals(""))
				cr.add(Restrictions.sqlRestriction(" lower(trim(nombre)) like '%"+sNombre.toLowerCase()+"%'"));
			if(!sBancoId.equals("SB"))
				cr.add(Restrictions.eq("idbanco", Integer.parseInt(sBancoId)));
			if(!sCuenta.trim().equals("SCTA"))
				cr.add(Restrictions.sqlRestriction(" lower(trim(nombre)) like '%"+sCuenta.toLowerCase()+"%'"));
			if( sIdEstados !=null)
				cr.add(Restrictions.in("estadproc", sIdEstados));
			
			if(dtFechaIni != null)
				cr.add(Restrictions.sqlRestriction("cast(datearchivo as date) >= '"+
						  FechasUtil.formatDatetoString(dtFechaIni, "yyyy-MM-dd") +"'"));
			if(dtFechaFin != null)
				cr.add(Restrictions.sqlRestriction("cast(datearchivo as date) <= '"+
						FechasUtil.formatDatetoString(dtFechaFin, "yyyy-MM-dd") +"'"));
			
			if( sIdsArchivos !=null)
				cr.add(Restrictions.not(Restrictions.in("idarchivo", sIdsArchivos)));
			
			if(!sNoReferencia.equals("") && sNoReferencia.matches("^[0-9]+$")){
				cr.createCriteria("depbancodets")
						.add(Restrictions.eq("referencia", Integer.parseInt(sNoReferencia)));
			}
			
			List<String[]>dta = new ArrayList<String[]>();
			dta.add(new String[]{"idbanco","0"});
			dta.add(new String[]{"moneda","3"});
			String sqlOrCtaConc = constructSqlOrCtaxCon(dta);
			if( !sqlOrCtaConc.isEmpty() ){
				cr.add( Restrictions.sqlRestriction(  sqlOrCtaConc  ) );
			}
			
			lstarchivos = cr.list();
			
		} catch (Exception e) {
			lstarchivos = null;
			error = e;
			e.printStackTrace();
		}
		return lstarchivos;
	}
	/******************************************************************************************/
	/** Método: obtener los numero de cuenta de banco confirugados para la conciliacion.
	 *	Fecha:  19/08/2011
	 *  Nombre: Carlos Manuel Hernández Morrison.
	 */
	public static List<F55ca023> obtenerF55ca023xBanco(int iIdBanco){
		List<F55ca023>lstCuentasxBanco = null;
		
		Session sesion = null;
				
		try{
			sesion = HibernateUtilPruebaCn.currentSession();

			Criteria cr = sesion.createCriteria(F55ca023.class);
			cr.add(Restrictions.eq("id.d3codb", iIdBanco));
			cr.add(Restrictions.eq("id.d3stat", "A"));
			cr.add(Restrictions.ne("id.d3nocuenta", new Long(0)));
			
			List<String[]>dta = new ArrayList<String[]>();
			dta.add(new String[]{"d3nocuenta","1"});
			String sqlOrCtaConc = ConfirmaDepositosCtrl.constructSqlOrCtaxCon(dta);
			if( !sqlOrCtaConc.isEmpty() ){
				cr.add(Restrictions.sqlRestriction(sqlOrCtaConc)) ;
			}
			
			lstCuentasxBanco = cr.list();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lstCuentasxBanco;
	}
	/******************************************************************************************/
	/** Método: obtener los depositos contenidos en un archivo a partir de su id
	 *	Fecha:  17/08/2011
	 *  Nombre: Carlos Manuel Hernández Morrison.
	 */
	public List<Depbancodet> obtenerDepositosxArchivo(int iArchivoId){
		List<Depbancodet>lstDepsxArchivo = null;
		try {
			Session sesion = HibernateUtilPruebaCn.currentSession();
			
			
			Criteria cr = sesion.createCriteria(Depbancodet.class);
			cr.add(Restrictions.eq("archivo.idarchivo", iArchivoId));
			cr.addOrder(Order.asc("iddepbcodet"));
			
			lstDepsxArchivo = (ArrayList<Depbancodet>)cr.list();
			
		} catch (Exception e) {
			lstDepsxArchivo = null;
			error = e;
//			System.out.println(":ConfirmaDepositosCtrl:  Excepción capturada en obtenerDepositosxArchivo(): "+e);
			e.printStackTrace();
		}
		return lstDepsxArchivo;
	} 
	/******************************************************************************************/
	/** Método: obtener los depósitos de caja para mostrar en grid principal
	 *	Fecha:  17/08/2011
	 *  Nombre: Carlos Manuel Hernández Morrison.
	 */
	public List<Deposito> obtenerDepositosCaja(Date dtFechaIni, Date dtFechaFin){
		List<Deposito> lstDepositosCaja = null;
		Session sesion = null; 
		
		try {

			sesion = HibernateUtilPruebaCn.currentSession();
					
			Criteria cr = sesion.createCriteria(Deposito.class);

			if(dtFechaIni != null)
				cr.add(Restrictions.ge("fecha", dtFechaIni));
			if(dtFechaFin != null)
				cr.add(Restrictions.le("fecha", dtFechaFin));
			
			cr.add(Restrictions.eq("estadocnfr", PropertiesSystem.DP_NOCONFIRMADO/*"SCR"*/));
			cr.addOrder(Order.desc("fecha"));
			
			List<String[]>dta = new ArrayList<String[]>();
			dta.add(new String[]{"idbanco","0"});
			dta.add(new String[]{"codcomp","2"});
			dta.add(new String[]{"moneda","3"});
			String sqlOrCtaConc = constructSqlOrCtaxCon(dta);
			if( !sqlOrCtaConc.isEmpty() ){
				cr.add( Restrictions.sqlRestriction(sqlOrCtaConc) );
			}
			
			lstDepositosCaja = (ArrayList<Deposito>)cr.list();
			
		} catch (Exception e) {
			lstDepositosCaja = null;
			error = e;
//			System.out.println(":ConfirmaDepositosCtrl:  Excepción capturada en obtenerDepositosCaja(): "+e);
			e.printStackTrace();
		}
		return lstDepositosCaja;
	}
	/******************************************************************************************/
	/** Método: obtener los archivos emitidos por banco en estado "En proceso", "sin procesar"
	 *	Fecha:  16/08/2011
	 *  Nombre: Carlos Manuel Hernández Morrison.
	 */
	public static List<Archivo> obtenerArchivosBanco(){
		List<Archivo>lstArchivos = null;
		Session sesion = null;
		
		boolean newCn = false;

		try {
			sesion = HibernateUtilPruebaCn.currentSession();
					
			Criteria cr = sesion.createCriteria(Archivo.class);
			cr.add(Restrictions.eq("estado", 41));							//=== Estado 41: Archivo activo.
			cr.add(Restrictions.in("estadproc", new Integer[]{44,45}));		//=== Estado 44,45: Sin procesar, en proceso
			cr.setMaxResults(45);
			cr.addOrder(Order.desc("datearchivo"));
			
			List<String[]>dta = new ArrayList<String[]>();
			dta.add(new String[]{"idbanco","0"});
			dta.add(new String[]{"moneda","3"});
			String sqlOrCtaConc = constructSqlOrCtaxCon(dta);
			if( !sqlOrCtaConc.isEmpty() ){
				cr.add( Restrictions.sqlRestriction(sqlOrCtaConc) );
			}
			
			lstArchivos = (ArrayList<Archivo>)cr.list();
 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lstArchivos;
	}
	public static void cargarConfiguracionConciliadorh(int codigocon, HttpSession shttp ){
		List<String[]> ctaxconciliador = null;
		List<String[]> cajasxconciliador = null;
		Session sesion = null; 
		
		String sql = "";
		
		try{
			 shttp.removeAttribute("ctaxconciliador");
			 shttp.removeAttribute("cajasxconciliador");
			
			sesion = HibernateUtilPruebaCn.currentSession();
			
			//&& ======== Leer configuracion de cuentas de banco.
			sql = "select cast(codigobanco as varchar(20)),  cast( numerocuentabanco as varchar(20) ), trim(codcomp), moneda " +
					" from "+PropertiesSystem.ESQUEMA+".cuentaxconciliador " +
					" where estado = 1 and codigoconciliador = "+ codigocon ;    
			
			List<Object[]> ctsxconc = (ArrayList<Object[]>)sesion.createSQLQuery( sql ).list();
			if(ctsxconc != null && !ctsxconc.isEmpty()){
				ctaxconciliador = new ArrayList<String[]>(ctsxconc.size());
				for (Object[] values : ctsxconc) {
					ctaxconciliador.add(new String[]{
							String.valueOf(values[0]),
							String.valueOf(values[1]),
							String.valueOf(values[2]),
							String.valueOf(values[3])
					});
				}
				shttp.setAttribute("ctaxconciliador", ctaxconciliador);
			}
			
			//&& ======== Leer configuracion de cajas por conciliador
			sql = "select cast(caid as varchar(10)), cast(codigoconciliador as varchar(10)) " +
					" from "+PropertiesSystem.ESQUEMA+".cajaxconciliador " +
					" where estado = 1 and codigoconciliador = "+ codigocon ;
		
			List<Object[]> cjsxconc = (ArrayList<Object[]>)sesion.createSQLQuery( sql ).list();
			if(cjsxconc != null && !cjsxconc.isEmpty()){
				cajasxconciliador = new ArrayList<String[]>(cjsxconc.size());
				for (Object[] values : cjsxconc) {
					cajasxconciliador.add(new String[]{
							String.valueOf(values[0]),
							String.valueOf(values[1]),
					});
				}
				shttp.setAttribute("cajasxconciliador", cajasxconciliador);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public boolean bEnviaCorreoBatchExcepcion(List<Object[]>lstObj,int strCode){
		boolean bHecho = true;
		try {
			
			StringBuilder sTituloDet = new StringBuilder();
			StringBuilder sTablaDetalleBatch = new StringBuilder();
			StringBuilder sbTablaCorreo = new StringBuilder();
			
			String sEstiloTdhdr="", sEstiloTD="";
			
			sEstiloTdhdr += "style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color:white; ";
			sEstiloTdhdr += " background-color: #5e89b5;\" align=\"center\" ";
			sEstiloTD += " style=\" font-family: Arial,Helvetica, sans-serif;font-size: 12px;color: #1a1a1a; border-color: silver; ";
			sEstiloTD += " border-style: dashed; border-width: 1px\" ";
			
			sTablaDetalleBatch.append("<table width=\"750px\" style=\"border-color: silver; border-width: 1px; border-style: solid\">");
			sTablaDetalleBatch.append("<tr>");
			sTablaDetalleBatch.append("<td height=\"25px%\" colspan = \"8\" "+sEstiloTD+ " valign=\"bottom\" align=\"left\"  >");
			sTablaDetalleBatch.append("<b>Detalle de los Batch's:  </b>");
			sTablaDetalleBatch.append("</td>");
			sTablaDetalleBatch.append("</tr>");
			
			sTituloDet.append("<tr>");			
			sTituloDet.append("<td "+sEstiloTdhdr+ ">");
			sTituloDet.append("<b>N°</b>");
			sTituloDet.append("</td>");
			
			sTituloDet.append("<td "+sEstiloTdhdr+ ">");
			sTituloDet.append("<b>Batch</b>");
			sTituloDet.append("</td>");
			
			sTituloDet.append("<td "+sEstiloTdhdr+ ">");
			sTituloDet.append("<b>Documento</b>");
			sTituloDet.append("</td>");
			
			sTituloDet.append("<td "+sEstiloTdhdr+ ">");
			sTituloDet.append("<b>Tipo</b>");
			sTituloDet.append("</td>");
			
			sTituloDet.append("<td "+sEstiloTdhdr+ ">");
			sTituloDet.append("<b>Monto</b>");
			sTituloDet.append("</td>");
			
			sTituloDet.append("<td "+sEstiloTdhdr+ ">");
			sTituloDet.append("<b>Moneda</b>");
			sTituloDet.append("</td>");
			
			sTituloDet.append("<td "+sEstiloTdhdr+ ">");
			sTituloDet.append("<b>Fecha</b>");
			sTituloDet.append("</td>");
			
			sTituloDet.append("<td "+sEstiloTdhdr+ ">");
			sTituloDet.append("<b>Usuario</b>");
			sTituloDet.append("</td>");
			sTituloDet.append("</tr>");
			sTablaDetalleBatch.append(sTituloDet);
			
			//&& ====== Recorrer los registros de conciliaciones.
			for (int i=0; i<lstObj.size();i++) {
				Object obj[] =  (Object[]) lstObj.get(i);
				sTablaDetalleBatch.append("<tr>");
				sTablaDetalleBatch.append("<td align=\"right\" " +sEstiloTD+ " >");
				sTablaDetalleBatch.append(i+1);
				sTablaDetalleBatch.append("</td>");

				sTablaDetalleBatch.append("<td align=\"right\" " +sEstiloTD+ " >");
				sTablaDetalleBatch.append(obj[0]);
				sTablaDetalleBatch.append("</td>");
				
				sTablaDetalleBatch.append("<td align=\"right\" " +sEstiloTD+ " >");
				sTablaDetalleBatch.append(obj[1]);
				sTablaDetalleBatch.append("</td>");
				
				sTablaDetalleBatch.append("<td align=\"center\" " +sEstiloTD+ "  >");
				sTablaDetalleBatch.append(obj[2]);
				sTablaDetalleBatch.append("</td>");
				
				sTablaDetalleBatch.append("<td align=\"right\" " +sEstiloTD+ " >");
				sTablaDetalleBatch.append(new Divisas().formatDouble(Double.parseDouble(obj[3]+"")));
				sTablaDetalleBatch.append("</td>");
				
				sTablaDetalleBatch.append("<td align=\"center\" " +sEstiloTD+ " >");
				sTablaDetalleBatch.append(obj[4]);
				sTablaDetalleBatch.append("</td>");
				
				sTablaDetalleBatch.append("<td align=\"center\" " +sEstiloTD+ " >");
				new FechasUtil();
				sTablaDetalleBatch.append(FechasUtil.formatDatetoString(new Date(), "dd/MM/yyyy"));  
				sTablaDetalleBatch.append("</td>");
				
				sTablaDetalleBatch.append("<td align=\"center\" " +sEstiloTD+ " >");
				sTablaDetalleBatch.append(obj[5]);
				sTablaDetalleBatch.append("</td>");
				sTablaDetalleBatch.append("</tr>");
			}
			sTablaDetalleBatch.append("</table>");
			
			//&& ========= Generar el encabezado del correo.
			sbTablaCorreo.append("<table width=\"800px\" style=\"border: 1px #7a7a7a solid\" align=\"center\" cellspacing=\"0\" cellpadding=\"3\">");
			sbTablaCorreo.append("<tr>"); 
			sbTablaCorreo.append("<th colspan=\"2\" style=\"border-bottom: 1px #7a7a7a solid; background: #3e68a4\">");
			sbTablaCorreo.append("<font face=\"Arial\" size=\"2\" color=\"white\"><b>Notificación de Batch's generados automáticos</b></font>");
			sbTablaCorreo.append("</th>");
			sbTablaCorreo.append("</tr>");
			
			String sEncabezado = "Los siguientes batch's se han generado por aplicacion de excepciones caja/banco";
			
			sbTablaCorreo.append("<tr>");
			sbTablaCorreo.append("<td align:center colspan=\"2\" style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"90%\">");
			sbTablaCorreo.append(sEncabezado);
			sbTablaCorreo.append(" ==> Fecha: "+new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a").format(new Date()));
			sbTablaCorreo.append("</td>");
			sbTablaCorreo.append("</tr>");
			sbTablaCorreo.append("<tr>");
			sbTablaCorreo.append("<td colspan=\"2\" height=\"20px\">");
			sbTablaCorreo.append("</td>");
			sbTablaCorreo.append("</tr>");
			
			sbTablaCorreo.append("<tr>");
			sbTablaCorreo.append("<td colspan=\"2\" align=\"center\" >");
			sbTablaCorreo.append(sTablaDetalleBatch);
			sbTablaCorreo.append("</td>");
			sbTablaCorreo.append("</tr>");
			
			sbTablaCorreo.append("<tr>");
			sbTablaCorreo.append("<td align=\"center\" colspan=\"2\" style=\"font-family: Arial, Helvetica, sans-serif;font-size: 10px;");
			sbTablaCorreo.append("color: black; border-bottom: 1px ##1a1a1a solid; \">");
			sbTablaCorreo.append("<b>Casa Pellas, S. A. - Módulo de Caja</b>");
			sbTablaCorreo.append("</td>");	
			sbTablaCorreo.append("</tr>");
			sbTablaCorreo.append("</table>");
			
			String sHtml = sbTablaCorreo.toString();
			
			// MultiPartEmail email = new MultiPartEmail();
			String sFrom="", sNombreFrom="", sNombreTo = "", sTo="";
			
			new EmpleadoCtrl();
			Vf0101 vf01 = EmpleadoCtrl.buscarEmpleadoxCodigo2(strCode);
			if(vf01 != null){
				sNombreTo = vf01.getId().getAbalph().trim().toUpperCase();
				sTo		  = vf01.getId().getWwrem1().trim().toUpperCase();
				new Divisas();
				if(!Divisas.validarCuentaCorreo(sTo)){
					error = new Exception("@CONCILIACION: El correo del contador contiene valores incorrectos! ");
					return false;
				}
			}
			
			List<CustomEmailAddress> ccCopy = new ArrayList<CustomEmailAddress>();
			for (String dtaCc : PropertiesSystem.MAILBCCSCON) {
				ccCopy.add(new CustomEmailAddress(dtaCc.split(PropertiesSystem.SPLIT_CHAR)[0], dtaCc.split(PropertiesSystem.SPLIT_CHAR)[1]));
			}
			
			MailHelper.SendHtmlEmail(
					new CustomEmailAddress(PropertiesSystem.WEBMASTER_EMAIL_ADRESS, "Módulo de Caja: Confirmación depósitos"),
					new CustomEmailAddress(sTo,sNombreTo), ccCopy, null, 
					"Confirmación de depósitos: Notificación de Generación de Batch's", sHtml);
			
		} catch (Exception e) {
			bHecho = false;
			errorDetalle = e;
			error = new Exception("@CONCILIACION: El correo del contador contiene valores incorrectos! ");
//			System.out.println(":ConfirmaDepositosCtrl:  Excepción capturada en bEnviaCorreoBatch(): "+e);
			e.printStackTrace();
		}
		return bHecho;    
	}	
}

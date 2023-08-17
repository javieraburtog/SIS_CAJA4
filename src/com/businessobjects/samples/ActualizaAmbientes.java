package com.businessobjects.samples;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.casapellas.util.CustomEmailAddress;
import com.casapellas.util.MailHelper;
import com.casapellas.util.PropertiesSystem;

public class ActualizaAmbientes {
	static int rows ;
	static int caja_actual;
	static String fechaini;
	static String fechafin;
	static String codcomp = "'E01'";
	static String[] companias =  { "'E01'", "'E02'", "'E03'", "'E08'"   };
	
	static List<Integer> codigosCajaConDatos = new ArrayList<Integer>();
	
	static String strLogFileName = "LogActualiza.txt";
	static PrintWriter writer ;
	
	static Connection cn;
	static boolean systemoutprint = true;
	
	public static void main(String args[]){
		@SuppressWarnings("unused")
		boolean hecho = true;
		
		try {
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date()) ;
			cal.add(Calendar.DATE, -1);
			
			fechaini = "'" + new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()) +"'";
			fechafin = fechaini;
			
			fechaini = "'2015-01-01'";
			fechafin = "'2017-12-31'";
			
			hecho = actualizaE1Gcpmcaja();
			
		} catch (Exception e) {
			 e.printStackTrace();
		}
	}
	
	public static boolean actualizaE1Gcpmcaja() {
		boolean hecho = true;

		try {
			
			strLogFileName = "ProduccionToE1gcpmcaja.txt";
			
			writer = new PrintWriter(strLogFileName, "UTF-8");

			cn = getConnection() ;
			
			@SuppressWarnings("unused")
			ActualizacionE1 ac = new ActualizacionE1(writer, fechaini, fechafin, codcomp, companias, systemoutprint, "E1GCPMCAJA", "GCPMCAJA", cn );
			hecho = ActualizacionE1.runUpdate();
			
			writer.close();
			
			if(hecho){
				cn.commit(); 
			}else{
				cn.rollback();
			}
				
			cn.close();

		} catch (Exception e) {
			e.printStackTrace();
			hecho = false;
		}

		return hecho;
	}
	
	@SuppressWarnings("serial")
	public static void sendMail(String filename){
		File file = new File(filename);
		
		try {		
			MailHelper.SendHtmlEmail(
					new CustomEmailAddress(PropertiesSystem.WEBMASTER_EMAIL_ADRESS, "Módulo de Caja: Notificación de Traslado de Factura"),
					new ArrayList<CustomEmailAddress>() { { add(new CustomEmailAddress(PropertiesSystem.MAIL_BOUNCEADDRESS)); } }, null, null, 
					"Actualizacion de ambientes de Gcpmcaja", "", new String[] { file.getAbsolutePath() });
			
		} catch (Exception e) {
			e.printStackTrace();
			writer.println(stackTraceToString(e));
		}finally{
			
			if(file != null && file.exists())
				file.delete();
		}
	}
	public static String stackTraceToString(Exception error){
		String strStackTraceError = ""; 
		try {
			
			StringWriter errors = new StringWriter();
			error.printStackTrace(new PrintWriter(errors));
			strStackTraceError =  errors.toString();
			
		} catch (Exception e) {
			e.printStackTrace();
			writer.println(e.getMessage());
		}
		return strStackTraceError;
	}
	
	public static Connection getConnection() {
		try {
			String CLASSFORNAME = "com.ibm.as400.access.AS400JDBCDriver";
			String URLSERVER = "jdbc:as400://192.168.1.3;prompt=false";
			Class.forName(CLASSFORNAME);
			cn = DriverManager.getConnection(URLSERVER, "appcp", "appcp1810");
		} catch (Exception e) {
			e.printStackTrace();
			writer.println(stackTraceToString(e));
			cn = null;
			writer.println("Excepción capturada en: getConnection " + e);
		}
		return cn;
	}
}

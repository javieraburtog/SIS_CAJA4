package com.casapellas.conciliacion;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.mail.internet.InternetAddress;

//import org.apache.commons.mail.EmailAttachment;
//import org.apache.commons.mail.MultiPartEmail;

import com.casapellas.util.CustomEmailAddress;
import com.casapellas.util.MailHelper;
import com.casapellas.util.PropertiesSystem;


/** ***********************************************
 *  Clase: Permite notificar via correo los arqueos pendientes de aprobar por contador.
 * 	Fecha: 2012-07-27 (dia de pago :p )
 *  autor: Carlos Hernández Morrison
 *  Modificado: 2012-07-27
 */

public class NotiGnrlArqueo {

	//&& ======== conexion directa en localhost o was local.
	public static String CLASSFORNAME;
	public static String URLSERVER;
	
	//&& ======== Conexion directa en el servidor as400
//	public static String CLASSFORNAME  	= "com.ibm.db2.jdbc.app.DB2Driver";
//	public static String URLSERVER	 	= "jdbc:db2:Systemip";
	
	public static String ipservermail = "" ;
	public static String ESQUEMA;
	public static String USUARIO;
	public static String PASSWRD = "appcp1810";
	public static Exception error;
	public static String sNombrePc = "SERVER";
	public static Date dtFechaProceso = new Date(); 
	static Connection cn = null;
	
	public static InternetAddress webmasteremail;
	public static InternetAddress bouncyAccount;
	public static List<InternetAddress> direccionesCorreosTo ;
	public static List<InternetAddress> direccionesCorreosCc ;
	public static List<InternetAddress> direccionesCorreoBcc;
	public static Properties prop = new Properties();
	
	public static void loadProperties(){
		try {
			prop.load( NotiGnrlArqueo.class.getResourceAsStream("/com/casapellas/conciliacion/NotificacionArqueos.properties") ) ;
		} catch (Exception e) {
			prop = null;
			e.printStackTrace();
		}
	}
	public static void retrieveGenericsProperties(){
		try {
			
			ipservermail = prop.getProperty("arqueoscaja.notificacion.ipservermail"); 
			ESQUEMA = prop.getProperty("arqueoscaja.notificacion.esquema.caja");
			USUARIO = prop.getProperty("arqueoscaja.notificacion.usuario.id") ;
			URLSERVER    = prop.getProperty("arqueoscaja.notificacion.ur_server_as400") ;
			CLASSFORNAME = prop.getProperty("arqueoscaja.notificacion.class_for_name") ;
			
			String comodinCuenta = prop.getProperty("arqueoscaja.notificacion.comodincuenta");
			String comodinDatos  = prop.getProperty("arqueoscaja.notificacion.comodindatos");
			String cuentasCorreoTo = prop.getProperty("arqueoscaja.notificacion.cuentacorreosto");
			String cuentasCorreoCc = prop.getProperty("arqueoscaja.notificacion.cuentacorreoscc");
			String cuentasCorreoBcc = prop.getProperty("arqueoscaja.notificacion.cuentacorreosbcc");
			
			String cuentawebmast = prop.getProperty("arqueoscaja.notificacion.webmasteremail"); 
			String cuentarebotec = prop.getProperty("arqueoscaja.notificacion.bouncyAccount");
			
			direccionesCorreosTo = new  ArrayList<InternetAddress>( cuentasCorreoTo.split(comodinCuenta).length  );
			for (String cuentacorreo : cuentasCorreoTo.split(comodinCuenta)) {
				direccionesCorreosTo.add( new InternetAddress( cuentacorreo.split(comodinDatos)[1], cuentacorreo.split(comodinDatos)[0]) ) ;
			}
			direccionesCorreosCc = new  ArrayList<InternetAddress>( cuentasCorreoCc.split(comodinCuenta).length  );
			for (String cuentacorreo : cuentasCorreoCc.split(comodinCuenta)) {
				direccionesCorreosCc.add( new InternetAddress( cuentacorreo.split(comodinDatos)[1], cuentacorreo.split(comodinDatos)[0]) ) ;
			}
			if(cuentasCorreoBcc != null){
				direccionesCorreoBcc = new  ArrayList<InternetAddress>( cuentasCorreoBcc.split(comodinCuenta).length  );
				for (String cuentacorreo : cuentasCorreoBcc.split(comodinCuenta)) {
					direccionesCorreoBcc.add( new InternetAddress( cuentacorreo.split(comodinDatos)[1], cuentacorreo.split(comodinDatos)[0]) ) ;
				}
			}
			
			webmasteremail = new InternetAddress( cuentawebmast.split(comodinDatos)[1], cuentawebmast.split(comodinDatos)[0]) ;
			bouncyAccount  = new InternetAddress( cuentarebotec.split(comodinDatos)[1], cuentarebotec.split(comodinDatos)[0]) ;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		List<Integer>lstContadores = null;
		
		try {
			
			loadProperties();
			
			if(prop == null)
				return;
		
			retrieveGenericsProperties();
			
			//&& ========= Conexion a la base de datos y propiedades
			cn = getConnection();
			if(cn == null){
				error = new Exception("GCPMCAJA.main(): Error al abrir conexion a base de datos");
				System.exit(0);
			}
			
			//&& ========= Obtener los contadores.
			lstContadores = obtenerContadores(cn);
			if(lstContadores == null || lstContadores.size() == 0) System.exit(0);
			
			//&& ===== Mandar correo general con todos los arqueos pendientes, agrupados por contador.
			enviarNotificacionGeneral(lstContadores);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
/** **********************************************************************
 *  Mandar el correo general, con todos los arqueos agrupados por contador.
 */	
	public static boolean enviarNotificacionGeneral(List<Integer>lstContadores){
		boolean bEnviado = true;
		StringBuilder sbHTML = new StringBuilder();
		String sDetalle = "";
		String sHtmlDetail = "";
		String sHtmlHeader = "";
		List<String[]>lstArqueos = null;
		int iTotalArqueos = 0;
		
		try {
			//&& ===== Encazado de la tabla html.
			sbHTML.append("<table width=\"900px\" style=\"border-color: silver; border-width: 1px; border-style: solid\">");
			
			//&& =========== obtener los arqueos pendientes por contador.
			for (Integer iCodcont : lstContadores) {
				lstArqueos = obtenerArqueosxContador(iCodcont);
				if(lstArqueos == null || lstArqueos.size() == 0 ) continue;
				
				iTotalArqueos += lstArqueos.size(); 
				
				//&& ==== detalle de arqueo por contador.
				sHtmlDetail = crearHeaderRowGeneral();
				if(sHtmlDetail.compareTo("") == 0 ) continue;
				
				for (String[] sDtaArq : lstArqueos) {
					sDetalle = crearDetalleArqueoGeneral(sDtaArq);
					if(sDetalle.compareTo("") != 0)
						sHtmlDetail += sDetalle;
				}
				//&& ========= Preguntar si hay detalle, si lo hay, crear titulo de ese detalle.
				if(sHtmlDetail.compareTo("") != 0)
					sHtmlHeader = crearTituloxContador(iCodcont);
				
				sbHTML.append(sHtmlHeader);
				sbHTML.append(sHtmlDetail);
			}
			
			//&& ========= Aniadir al html total, el grupo de la caja corriendo.
			sbHTML.append("</table>");
			
			
			//&& ===== Crear un archivo con el  contenido del html
			String sNombre = "ArqueosSinAprobar_"+new SimpleDateFormat("dd-MMMM-yyyy",new Locale("ES","es")).format(new Date());
			StringBuilder sbAdjunto = new StringBuilder("");
			sbAdjunto.append("<html>");
			sbAdjunto.append("<head>");
			sbAdjunto.append(" <title> "+sNombre+ "</title>");
			sbAdjunto.append("</head>");
			sbAdjunto.append("<body>");
			sbAdjunto.append(sbHTML.toString());
			sbAdjunto.append("</body>");
			sbAdjunto.append("</html>");

			BufferedWriter out = new BufferedWriter(new FileWriter(sNombre+".html"));
		    out.write(sbAdjunto.toString());
		    out.close();

		    //&& ========= Enviar el correo con el adjunto.
			String html = "<table width=\"100%\">";
			html += "<tr>";
			html += "<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"100%\" >";
			html += "<b> Reporte de arqueos pendientes de aprobación. </b></td>";
			html += "</tr>";	
			
			html += "<tr>";
			html += "<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"100%\" >";
			html += "<b> Adjunto se encuentra el detalle de arqueos que áun se encuentran pendientes de aprobar.</b></td>";
			html += "</tr>";	

			html += "<tr>";
			html += "<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"100%\" >";
			html += "<b>Se han agrupado los registros por Contador, Caja, Compañía y Moneda.</b></td>";
			html += "</tr>";		
			
			html += "<tr>";
			html += "<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"100%\" >";
			html += "<b>Cantidad de Arqueos: "+iTotalArqueos+"</b></td>";
			html += "</tr>";				
			
			html += "<tr>";
			html += "<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"100%\" >";
			html += "<b>Detalle generado a la fecha: "
					+new SimpleDateFormat("dd-MMMM-yyyy hh:mm:ss a",new Locale("ES","es"))
						.format(new Date())+"</b></td>";
			html += "</tr>";
			
			List<String[]> lstDetalle = obtenerResumenArqueos();
			if(lstDetalle != null && lstDetalle.size() > 0){
				html += "<tr>";
				html += "<td align=\"center\"  width=\"100%\" >";
				html += crearTablaDetalleRsm(lstDetalle);
				html += "</td>";
				html += "</tr>";
			}
			
			html += "<tr>";
			html += "<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 11px;color: #1a1a1a;\" width=\"100%\" >";
			html += "<b><br>SALUDOS</b></td>";
			html += "</tr>";
			html += "<tr>";
			html += "<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 11px;color: #1a1a1a;\" width=\"100%\" >";
			html += "<b>Módulo de Caja<br>Casa Pellas - Informática 2012</b></td>";
			html += "</tr>";
			html += "</table>";
		    
		    File archivo = new File(sNombre+".html");
		    
		    List<CustomEmailAddress> toList = new ArrayList<CustomEmailAddress>();
		    for (int i = 0; i < direccionesCorreosTo.size(); i++) {
				InternetAddress address = direccionesCorreosTo.get(i);
				toList.add(new CustomEmailAddress(address.getAddress()));
			}
		    
		    List<CustomEmailAddress> ccList = new ArrayList<CustomEmailAddress>();
		    for (int i = 0; i < direccionesCorreosCc.size(); i++) {
				InternetAddress address = direccionesCorreosCc.get(i);
				ccList.add(new CustomEmailAddress(address.getAddress()));
			}
			
			List<CustomEmailAddress> bccList = new ArrayList<CustomEmailAddress>();
			if(direccionesCorreoBcc != null){
			    for (int i = 0; i < direccionesCorreoBcc.size(); i++) {
					InternetAddress address = direccionesCorreoBcc.get(i);
					bccList.add(new CustomEmailAddress(address.getAddress()));
				}
			}
			
		    MailHelper.SendHtmlEmail(
					new CustomEmailAddress(webmasteremail.getAddress(), webmasteremail.getPersonal()),
					toList, ccList, bccList, 
					"Reporte de Arqueos pendientes de aprobación", html.toString(), new String[] { archivo.getAbsolutePath() });
		    
			if( archivo != null && archivo.exists()) 
				archivo.delete();
		    
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bEnviado;
	}
	public static String[] obtenerDatosContador(int iCodcont){
		String[] sDatosCorreo = null;
		String sql = "";
		
		try {
	
			sql =  " select lower(trim(vf.abalph)), lower(trim(vf.wwrem1))";
			sql += " from "+ESQUEMA+".vf0101 vf where vf.aban8 = "+iCodcont+" and trim(vf.wwrem1) <> ''";
			sql += " fetch first 1 rows only ";
			
			PreparedStatement ps = cn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = ps.executeQuery(); 
			
			if(rs != null){
				rs.beforeFirst();
				if(rs.next()){
					rs.beforeFirst();
					while(rs.next()){
						sDatosCorreo = new String[2];
						sDatosCorreo[0]= rs.getString(1);		//Nombre del usuario
						sDatosCorreo[1]= rs.getString(2);		//Direccion de correo.
						sDatosCorreo[0] = tipoOracion(sDatosCorreo[0]);
					}
				}
			}
			rs.close();
			ps.close();
			cn.commit();
			
		} catch (Exception e) {
			e.printStackTrace();
			sDatosCorreo = null;
		}
		return sDatosCorreo;
	}
	
	
/**************************************************************************/
/** Aplica formato a una cadena de forma que cada palabra comience con mayúscula.
 *  Fecha:  29/06/2010
 *  Hecho: Carlos Manuel Hernández Morrison.
 */	
	public static String tipoOracion(String sCadenaOriginal) {
		String sCadenaNueva = "";
		String sCadena = "";
		String[] sPartesCadena = null;
		try {
			sCadena = sCadenaOriginal;
			if (sCadena != null && !sCadena.equals("") && sCadena.length() > 0) {
				sPartesCadena = sCadena.split(" ");
				if (sPartesCadena != null && sPartesCadena.length > 0) {
					sCadenaNueva = "";
					for (String sParte : sPartesCadena) {
						if (!sParte.equals("")) {
							sCadenaNueva += sParte.substring(0, 1)
									.toUpperCase()
									+ sParte.substring(1, sParte.length())
											.toLowerCase() + " ";
						}
					}
					sCadenaNueva = sCadenaNueva.trim();
				}
			}
		} catch (Exception error) {
			sCadenaNueva = sCadenaOriginal;
			error.printStackTrace();
		}
		return sCadenaNueva;
	}
/** *************************************************************
 *  obtener resumen de cantidad de arques pendientes por contador. 
 */
	public static String crearTablaDetalleRsm(List<String[]>lstDetalle){
		StringBuilder sTabla = new StringBuilder("");
		
		String sEstiloTdhdr = "";
		sEstiloTdhdr += " style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color:white; ";
		sEstiloTdhdr += " background-color: #5e89b5;\" align=\"center\" ";
		
		String sEstiloTD = "";
		sEstiloTD += " style=\" font-family: Arial,Helvetica, sans-serif;font-size: 11px;color: #1a1a1a; border-color: silver; ";
		sEstiloTD += " border-style: dashed; border-width: 1px\" ";
		
		try {
			sTabla.append("<table width=\"600px\" style=\"border-color: silver; border-width: 1px; border-style: solid\">");

			//&& ============== Encabezado de la tabla.
			sTabla.append("<tr>");			
			sTabla.append("<td "+sEstiloTdhdr+ ">");
			sTabla.append("<b>Código</b>");
			sTabla.append("</td>");

			sTabla.append("<td "+sEstiloTdhdr+ ">");
			sTabla.append("<b>Nombre</b>");
			sTabla.append("</td>");
			
			sTabla.append("<td "+sEstiloTdhdr+ ">");
			sTabla.append("<b>Pendientes</b>");
			sTabla.append("</td>");
			
			sTabla.append("<td "+sEstiloTdhdr+ ">");
			sTabla.append("<b> Más antiguo</b>");
			sTabla.append("</td>");
			
			sTabla.append("<td "+sEstiloTdhdr+ ">");
			sTabla.append("<b> Más Reciente</b>");
			sTabla.append("</td>");
			
			sTabla.append("</tr>");

			//&& =============== Cuerpo.
			for (String[] sArqueo : lstDetalle) {
				sTabla.append("<tr>");
				sTabla.append("<td align=\"right\" " +sEstiloTD+ " >");
				sTabla.append(sArqueo[0]);
				sTabla.append("</td>");
	
				sTabla.append("<td align=\"left\" " +sEstiloTD+ " >");
				sTabla.append(tipoOracion(sArqueo[1].trim()));
				sTabla.append("</td>");
				
				sTabla.append("<td align=\"right\" " +sEstiloTD+ " >");
				sTabla.append(sArqueo[2]);
				sTabla.append("</td>");
				
				sTabla.append("<td align=\"center\" " +sEstiloTD+ " >");
				sTabla.append(sArqueo[3]);
				sTabla.append("</td>");
				
				sTabla.append("<td align=\"center\" " +sEstiloTD+ " >");
				sTabla.append(sArqueo[4]);
				sTabla.append("</td>");
				
				sTabla.append("</tr>");
			}
			sTabla.append("</table>");
			
		} catch (Exception e) {
			e.printStackTrace();
			sTabla = new StringBuilder("");
		}	
		return sTabla.toString();
	}
/** *************************************************************
 *  obtener resumen de cantidad de arques pendientes por contador. 
 */
	public static List<String[]> obtenerResumenArqueos(){
		List<String[]> lstRsmxCont = null;
		String sql = "";
		String[] lstDtaArqueo = null;
		PreparedStatement ps = null;
		
		try {
			sql  = " select  f01.cacont, trim(abalph), count(*) total,  min(fecha), MAX(fecha)  ";
			sql += " from "+ESQUEMA+".arqueo v inner join gcpmcaja.F55ca01 ";
			sql += " f01 on f01.caid = v.caid inner join "+ESQUEMA+".VF0101 ";
			sql += " vf1 on f01.cacont = vf1.aban8"; 	
			sql += " where v.estado = 'P' ";	
			sql += " group by f01.cacont, abalph ";
			sql += " order by 3 desc ";
			
			ps = cn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = ps.executeQuery();

			if(rs != null){
				lstRsmxCont = new ArrayList<String[]>();
				rs.beforeFirst();
				while(rs.next()){
					lstDtaArqueo = new String[10];
					lstDtaArqueo[0] = String.valueOf(rs.getString(1));
					lstDtaArqueo[1] = String.valueOf(rs.getString(2));
					lstDtaArqueo[2] = String.valueOf(rs.getString(3));
					lstDtaArqueo[3] = String.valueOf(rs.getDate( 4 ));
					lstDtaArqueo[4] = String.valueOf(rs.getDate( 5 ));
					lstRsmxCont.add(lstDtaArqueo);
				}
			}
			rs.close();
			ps.close();
			cn.commit();
			
		} catch (Exception e) {
			e.printStackTrace();
			lstRsmxCont = null;
		}
		return lstRsmxCont;
	}
/** *************************************************************
 *  Crear titulo de caja. 
 */
    public static String crearTituloxContador(int iCodcont){
    	StringBuilder sRowHeader = new StringBuilder("");
    	String sEstiloTD = "" ;
    	
    	try {
    		//&& ==== obtener datos del contador.
    		String[]sDtaCont = obtenerDatosContador(iCodcont);
    		
    		sEstiloTD += " style=\" font-family: Arial,Helvetica, sans-serif;font-size: 12px;color: #1a1a1a; border-color: silver; ";
			sEstiloTD += " border-style: dashed; border-width: 1px\" ";
    		
			sRowHeader.append("<tr>");
			sRowHeader.append("<td height=\"25px%\" colspan = \"7\" "+sEstiloTD+ " valign=\"bottom\" align=\"left\"  >");
			sRowHeader.append("<b>Contador: "+iCodcont+" "+sDtaCont[0]+"</b>");
			sRowHeader.append("</td>");
			sRowHeader.append("</tr>");
    		
		} catch (Exception e) {
			e.printStackTrace();
			sRowHeader = new StringBuilder("");
		}
    	return sRowHeader.toString();
    }
/** *******************************************************
 *  crear detalle de arqueos para correo general. 
 */ 
    public static String crearDetalleArqueoGeneral(String[]sArqueo){
    	StringBuilder sbDetalle = new StringBuilder();
    	
    	try {
    		String sEstiloTD="";
			sEstiloTD += " style=\" font-family: Arial,Helvetica, sans-serif;font-size: 11px;color: #1a1a1a; border-color: silver; ";
			sEstiloTD += " border-style: dashed; border-width: 1px\" ";

			sbDetalle.append("<tr>");
			sbDetalle.append("<td align=\"left\" " +sEstiloTD+ " >");
			sbDetalle.append(sArqueo[8] +" "+ sArqueo[9]);
			sbDetalle.append("</td>");
			
			sbDetalle.append("<td align=\"right\" " +sEstiloTD+ " >");
			sbDetalle.append(sArqueo[0]);
			sbDetalle.append("</td>");

			sbDetalle.append("<td align=\"center\" " +sEstiloTD+ " >");
			sbDetalle.append( sArqueo[1] );
			sbDetalle.append("</td>");
			
			sbDetalle.append("<td align=\"center\" " +sEstiloTD+ " >");
			sbDetalle.append( sArqueo[2] );
			sbDetalle.append("</td>");
			
			sbDetalle.append("<td align=\"left\" " +sEstiloTD+ "  >");
			sbDetalle.append(sArqueo[3].trim()+" "+sArqueo[4].trim() );
			sbDetalle.append("</td>");
			
			sbDetalle.append("<td align=\"center\" " +sEstiloTD+ " >");
			sbDetalle.append(sArqueo[5]);
			sbDetalle.append("</td>");
			
			sbDetalle.append("<td align=\"left\" " +sEstiloTD+ " >");
			sbDetalle.append(sArqueo[6] + " "+sArqueo[7]);
			sbDetalle.append("</td>");
			sbDetalle.append("</tr>");

		} catch (Exception e) {
			e.printStackTrace();
			sbDetalle = new StringBuilder("");
		}
		return sbDetalle.toString();
    }
/** *******************************************************
 *  Crear el encabezado por caja. 
 */
    public static String crearHeaderRowGeneral(){
    	StringBuilder sRowHeader = new StringBuilder("");
    	try {
    		String sEstiloTdhdr = "style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color:white; ";
			sEstiloTdhdr += " background-color: #5e89b5;\" align=\"center\" ";
    		
			sRowHeader.append("<tr>");			
			sRowHeader.append("<td "+sEstiloTdhdr+ ">");
			sRowHeader.append("<b>Caja</b>");
			sRowHeader.append("</td>");

			sRowHeader.append("<td "+sEstiloTdhdr+ ">");
			sRowHeader.append("<b>Arqueo</b>");
			sRowHeader.append("</td>");
			
			sRowHeader.append("<td "+sEstiloTdhdr+ ">");
			sRowHeader.append("<b>Fecha</b>");
			sRowHeader.append("</td>");
			
			sRowHeader.append("<td "+sEstiloTdhdr+ ">");
			sRowHeader.append("<b>Hora</b>");
			sRowHeader.append("</td>");
			
			sRowHeader.append("<td "+sEstiloTdhdr+ ">");
			sRowHeader.append("<b>Compañía</b>");
			sRowHeader.append("</td>");
			
			sRowHeader.append("<td "+sEstiloTdhdr+ ">");
			sRowHeader.append("<b>Moneda</b>");
			sRowHeader.append("</td>");
			
			sRowHeader.append("<td "+sEstiloTdhdr+ ">");
			sRowHeader.append("<b>Cajero</b>");
			sRowHeader.append("</td>");
			sRowHeader.append("</tr>");
			
		} catch (Exception e) {
			e.printStackTrace();
			sRowHeader = new StringBuilder("");
		}
		return  sRowHeader.toString();
    }
/** ***********************************************************
 *  Obtener los arqueos pendientes por contador.
 */
	public static List<String[]> obtenerArqueosxContador(int iCodcont){
		List<String[]>lstArqueos = null;
		String[] lstDtaArqueo = null;
		PreparedStatement ps = null;
		
		try {
			String sql = "Select";
			sql += " noarqueo, fecha, hora, codcomp,  nombrecomp,";
			sql += " moneda, codcajero, nombrecajero,v.caid, v.caname";
			sql += " from "+ESQUEMA+".varqueo v inner join "+ESQUEMA+".f55ca01 f01";
			sql += " on f01.caid = v.caid ";
			sql += " where f01.cacont  = "+iCodcont+" and v.estado = 'P'";
			sql += " and f01.castat = 'A' order  by v.caid asc ";
			
			ps = cn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = ps.executeQuery();

			if(rs != null){
				lstArqueos = new ArrayList<String[]>();
				rs.beforeFirst();
				while(rs.next()){
					lstDtaArqueo = new String[10];
					lstDtaArqueo[0] = String.valueOf(rs.getString(1));
					lstDtaArqueo[1] = String.valueOf(rs.getString(2));
					lstDtaArqueo[2] = String.valueOf(rs.getString(3));
					lstDtaArqueo[3] = String.valueOf(rs.getString(4));
					lstDtaArqueo[4] = String.valueOf(rs.getString(5));
					lstDtaArqueo[5] = String.valueOf(rs.getString(6));
					lstDtaArqueo[6] = String.valueOf(rs.getString(7));
					lstDtaArqueo[7] = tipoOracion(String.valueOf(rs.getString(8)));
					lstDtaArqueo[8] = String.valueOf(rs.getString(9));
					lstDtaArqueo[9] = String.valueOf(rs.getString(10));
					lstArqueos.add(lstDtaArqueo);
				}
			}
			rs.close();
			ps.close();
			cn.commit();
			
		} catch (Exception e) {
			e.printStackTrace();
			lstArqueos = null;
		}
		return lstArqueos;
	}
	
/** ***********************************************************
 *  Obtener la lista de contadores configurados para las cajas. 
 */
	public static List<Integer> obtenerContadores(Connection cn){
		List<Integer>lstContadores = null;
		PreparedStatement ps = null;
		
		try {

			String sql = "SELECT DISTINCT CACONT FROM ";
			sql += ESQUEMA+".F55CA01 WHERE CASTAT = 'A'";
			
			ps = cn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = ps.executeQuery();

			if(rs != null){
				lstContadores = new ArrayList<Integer>();
				rs.beforeFirst();
				while(rs.next())
					lstContadores.add(rs.getInt(1));
			}
			rs.close();
			ps.close();
			cn.commit();
			
		} catch (Exception e) {
			e.printStackTrace();
			lstContadores = null;
		}
		return lstContadores;
	}
	/** *****************************************
	 *  Conexion con el servidor 
	 */
    public static Connection getConnection(){
    	Connection cn = null;
        try {
            Class.forName(NotiGnrlArqueo.CLASSFORNAME);
            cn = DriverManager.getConnection(NotiGnrlArqueo.URLSERVER, 
            								 NotiGnrlArqueo.USUARIO, 
            								 NotiGnrlArqueo.PASSWRD); 
            if(cn != null){
    			cn.setAutoCommit(false);
    			cn.setReadOnly(true);
    			cn.setTransactionIsolation(0);
            }
            
        }catch(Exception e){
        	cn = null;
        	e.printStackTrace();
        }
        return cn;
    }
}

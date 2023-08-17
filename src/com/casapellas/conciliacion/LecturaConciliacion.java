package com.casapellas.conciliacion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.net.*;

import javax.mail.internet.InternetAddress;
//import org.apache.commons.mail.MultiPartEmail;

import com.casapellas.util.CustomEmailAddress;
import com.casapellas.util.MailHelper;
import com.casapellas.util.PropertiesSystem;
import com.ibm.ws.batch.xJCL.beans.listener;

public class LecturaConciliacion {
	
	public final static String SPLIT_CHAR = "==>";
	public final static String[] MAILBCCSCON = new String[]{
		"lfonseca@casapellas.com.ni"+SPLIT_CHAR+"Luis Fonseca"
 
	};
 
	public static int idtipoconfirm = 32;
	public static int idestadocnfr  = 36;
	public static int arESTADO      = 41;
	public static int arESTADPROC   = 45;
	public static int CODUSUARIO	= 713502;
	
	//&& ======== Conexion directa en el servidor as400
//	public static String CLASSFORNAME  	= "com.ibm.db2.jdbc.app.DB2Driver";
//	public static String URLSERVER	 	= "jdbc:db2:Systemip";

	//&& ======== conexion directa en localhost o was local.
	public static String CLASSFORNAME  	= "com.ibm.as400.access.AS400JDBCDriver";
	public static String URLSERVER	 	= "jdbc:as400://192.168.1.3;prompt=false";
	
	public static String ESQUEMA		= "GCPMCAJA";
	
	public static String USUARIO		= "appcp";
	public static String PASSWRD		= "appcp1810";
	public final static String dbens 	= "ENS";
	//public final static String jdedta 	= "DTA";
	//public final static String jdecom 	= "PRODCTL";
	public final static String ipservermail = "192.168.1.137";	
	public final static String[] usrprofiles = new String[]{
		"P000000041","P000000042"  /*,"P000000025"*/
	};
	public static Exception error;
	public static String sNombrePc = "SERVER";
	public static Date dtFechaLectura =  getFecha();
	public static int idarchivoactual = 0;
	
	
	public static Date getFecha(){
		Date fecha = new Date();
		try {
			  String strFecha = new SimpleDateFormat("yyyy-MM-dd").format( new Date() ) ;
			  fecha = new SimpleDateFormat("yyyy-MM-dd").parse(strFecha) ; 
			  
		} catch (Exception e) {
			fecha = new Date();
		}
		return fecha;
	}
	
	public String getESQUEMA() {
		return ESQUEMA;
	}
	public void setESQUEMA(String esquema) {
		ESQUEMA = esquema;
	}
	public Exception getError() {
		return error;
	}
	public void setError(Exception error) {
		LecturaConciliacion.error = error;
	}
	//TODO: Cifrar los password de ftp y del usuario a _.	
	
	/******************************************************************************************/
	/** Método: Obtener la conexion con el servidor
	 *	Fecha:  18/07/2011
	 *  Nombre: Carlos Manuel Hernández Morrison.
	 */
    public static Connection getConnection(){
    	Connection cn = null;
        try {
        	
            Class.forName(LecturaConciliacion.CLASSFORNAME);
            cn = DriverManager.getConnection(LecturaConciliacion.URLSERVER, 
            								 LecturaConciliacion.USUARIO, 
            								 LecturaConciliacion.PASSWRD); 
        }catch(Exception e){
        	cn = null;
        	e.printStackTrace();
        }
        return cn;
    }
	/******************************************************************************************/
	/** Método: Metodo principal para leer los datos del archivo plano.
	 *	Fecha:  18/07/2011
	 *  Nombre: Carlos Manuel Hernández Morrison.
	 */
	public static void main(String[] args) {
		List<String[]> lstBanco = null;
		boolean bExito = true;
		Date dtHoraInicio = new Date();
		String sMensaje = "";
		int iTipoEv=1;
		Date dtFechaArchivo = new Date();

		try {
		
			dtFechaArchivo = getFecha();
		
			 
//			Thread.sleep(1000);
			Calendar calFechaInicio = Calendar.getInstance();
			calFechaInicio.set(Calendar.YEAR, 2018);
			calFechaInicio.set(Calendar.MONTH, 04);
//			calFechaInicio.set( Calendar.DAY_OF_MONTH, (calFechaInicio.getActualMinimum(Calendar.DAY_OF_MONTH)  ) );
			calFechaInicio.set(Calendar.DAY_OF_MONTH, 23);
			
			Calendar calFechaFin  = Calendar.getInstance();
			calFechaFin.set(Calendar.YEAR, 2018);
			calFechaFin.set(Calendar.MONTH, 04);
//			calFechaFin.set(Calendar.DAY_OF_MONTH, calFechaFin.getActualMaximum(Calendar.DAY_OF_MONTH));
			calFechaFin.set(Calendar.DAY_OF_MONTH, 24);
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			
			Date dtFechaFin = sdf.parse(sdf.format(calFechaFin.getTime()));
			Date dtFechaIni = sdf.parse(sdf.format(calFechaInicio.getTime()));
			
			
			//cargarDatosConsolidado( calFechaInicio.getTime() );
			
			
			boolean b05Marzo = false;
			while (!b05Marzo){
				
				dtFechaIni   = sdf.parse(sdf.format(calFechaInicio.getTime()));
				if(dtFechaFin.compareTo(dtFechaIni) == 0)
					b05Marzo = true;
				
				dtFechaArchivo = dtFechaIni;
				LecturaConciliacion.dtFechaLectura = dtFechaIni;
			  
				
			sMensaje = "<====== INICIO DE LECTURA DE ARCHIVOS DE CONFIRMACION DE DEPOSITOS "+dtFechaArchivo +" ======>";
			rutinaError(sMensaje, LecturaConciliacion.CODUSUARIO, 
						LecturaConciliacion.dtFechaLectura, 
						LecturaConciliacion.dtFechaLectura, 1, "INICIO DE PROCESO");
			
			//&& ==== Leer los bancos configurados para las cajas y obtener su cuenta para armar el nombre del documento
			lstBanco = obtenerBancosConfigurados(dtHoraInicio);
			
			if(lstBanco==null){
				bExito = false;
			}else{
				for (String[] sConfigBco : lstBanco) {
					bExito = leerArchivoBanco(sConfigBco,"",dtHoraInicio);
					String sNombre  = generarNombreArchivo(LecturaConciliacion.dtFechaLectura, sConfigBco[3]);
					
					if(bExito){
						iTipoEv=1;
						sMensaje = "Se ha obtenido correctamente la informacion del archivo de banco: ";
						sMensaje += sConfigBco[0]+" Cuenta: "+sConfigBco[3] +",desde URL:FTP: " +sConfigBco[5]+" "+sConfigBco[4] ;
						sMensaje +=" Fecha: " +new SimpleDateFormat("yyyy-MM-dd").format(dtFechaArchivo);
					}else{
						iTipoEv = 3;
						sMensaje = " ftp "+sConfigBco[5]+" "+sConfigBco[4];
						if(error!=null){
							
							sMensaje +="&&&&&";
							if(error.toString().startsWith("java.io.FileNotFoundException:")){
								sMensaje += "Archivo no encontrado: "+error.getMessage(); 
							}else
							if(error.toString().startsWith("java.net.ConnectException: Connection refused: connect")){
								sMensaje += "Error de conexion con servidor de archivos";
							}else
							if(error.toString().startsWith("java.lang.Exception:")){
								sMensaje += error.getMessage();
							}
							else
							if(error.toString().startsWith("java.net.UnknownHostException:")){
								sMensaje += "Error de conexion al servidor de archivo";
							}else
							if( error.toString().startsWith("java.net.ConnectException: Connection timed out") ||
								error.toString().startsWith("java.net.SocketTimeoutException: connect timed out")||
								error.toString().startsWith("java.net.NoRouteToHostException: Connection timed out") ){
								sMensaje += "Tiempo de espera agotado de conexion al servidor ftp ";
							}else{
								sMensaje += "Error no conocido al correr aplicacion de lectura de archivo";
							}
						}
					}
					rutinaError(sMensaje, LecturaConciliacion.CODUSUARIO, dtHoraInicio, dtFechaArchivo, iTipoEv, 
								sNombre+"&&&&Banco: "+sConfigBco[0]+"&&&&&Cuenta: "+sConfigBco[3] );
				}
			}
			
			boolean bCorreo = notificacionCorreo();
			sMensaje = (bCorreo)?"Correo enviado exitosamente":"Error de envio de correo "+error.getMessage();
			rutinaError(sMensaje, LecturaConciliacion.CODUSUARIO, dtHoraInicio, dtFechaArchivo, 1, "ENVIO DE CORREO");
			
			sMensaje = "FINALIZACION DE PROCESO DE LECTURA DE ARCHIVOS DE CONFIRMACION DE BANCOS ";
			rutinaError(sMensaje, LecturaConciliacion.CODUSUARIO, dtHoraInicio, dtFechaArchivo, 1, "FIN DE PROCESO"); 
			
				
			//&& ========= llenar la tabla con los depositos para el consolidado.
			
//			calFechaInicio.add(Calendar.DAY_OF_MONTH, -1);
			
			cargarDatosConsolidado( calFechaInicio.getTime() );
			
			calFechaInicio.add(Calendar.DAY_OF_MONTH, + 1);
			
//			cargarDatosConsolidado( dtFechaArchivo );
			
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			rutinaError(e.getMessage(), LecturaConciliacion.CODUSUARIO, dtHoraInicio, dtFechaArchivo, iTipoEv, "");
		}
	}
/******************************************************************************************/
/** Método: obtener la lista de correos a quienes se ha de enviar la notificacion.
 *	Fecha:  18/08/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */	
	public static List<String[]>obtenerDireccionesCorreo(){
		List<String[]>lstCorreos = null;
		String sql = "";
	
		try {
			
			String strInProfiles = "";
			for ( String strCod : usrprofiles ) {
				if(!strInProfiles.isEmpty())
					strInProfiles +=", ";
				strInProfiles += "'"+strCod+"'";
			}
			strInProfiles = "( "+ strInProfiles + ")";
			
			sql = "select  lower(trim(d.wwmlnm)),  lower(trim(d.wwrem1)),\n " +
				"pe.codper from "+dbens+".usuario us inner join "+dbens+".usrper up \n " +
				"on  us.coduser = up.coduser inner join "+dbens+".perfil pe \n" +
				"on up.codper = pe.codper inner join "+ PropertiesSystem.JDEDTA+".f0111 D \n" +
				"on D.WWAN8 = us.codreg  where  pe.codapp = 'APP0000002' \n" +
				"and up.activa = 1 and pe.activo = 1 and us.codreg <> 0 \n" +
				"and us.activo = '1' and d.wwidln = 0 \n" +
				"and pe.codper in " + strInProfiles;
			
			Connection cn =  getConnection();
			if(cn==null){
				lstCorreos = null;
				error = new Exception("_.LecturaConciliacion(): Error al abrir conexion a base de datos");
			}
			cn.setAutoCommit(false);
			
			PreparedStatement ps = cn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = ps.executeQuery(); 
			rs.beforeFirst();
			
			if(rs.next()){
				rs.beforeFirst();
				lstCorreos = new ArrayList<String[]>();
				
				while(rs.next()){
					String[] sDatosCorreo = new String[2];
					sDatosCorreo[0]= rs.getString(1);		//Nombre del usuario
					sDatosCorreo[1]= rs.getString(2);		//Direccion de correo.
					
					if(sDatosCorreo[1].trim().toUpperCase().matches("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$" )){
						lstCorreos.add(sDatosCorreo);
					}
				}
			}
			rs.close();
			ps.close();
			cn.commit();
			cn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			rutinaError(e.getMessage(), LecturaConciliacion.CODUSUARIO, new Date(), new Date(), 0, "");
		}
		return lstCorreos;
	}
	
/******************************************************************************************/
/** Método: Enviar el correo de notificacion
 *	Fecha:  8/08/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public static boolean notificacionCorreo(){
		boolean bEnviado = true;
		String sHtmlCuerpo ="";

		try {
			
			sHtmlCuerpo = generarCuerpoCorreo();
			if(sHtmlCuerpo==null){
				error = new Exception("Error al generar el cuerpo HTML del correo");
				return false;
			}
			
			//&& ============ Obtener los usuarios con perfiles para confirmaciones y deben recibir el correo de notificacion.
			List<String[]> lstCtaCorreo = obtenerDireccionesCorreo();
			if(lstCtaCorreo == null  ||  lstCtaCorreo.isEmpty() ){
				error = new Exception("Notificación por correo no completada");
				return false;
			}
		
			List<CustomEmailAddress> toList = new ArrayList<CustomEmailAddress>();
			for (String[] sCtaCorreo : lstCtaCorreo) {
				toList.add(new CustomEmailAddress(sCtaCorreo[1],  sCtaCorreo[0]));
			}
			
			List<CustomEmailAddress> ccList = new ArrayList<CustomEmailAddress>();
			for (String dtaCc : MAILBCCSCON) {
				ccList.add(new CustomEmailAddress(dtaCc.split(SPLIT_CHAR)[0],  dtaCc.split(SPLIT_CHAR)[1]));
			};
			
			MailHelper.SendHtmlEmail(
					new CustomEmailAddress(PropertiesSystem.WEBMASTER_EMAIL_ADRESS, "Módulo de Caja: Confirmación depósitos"),
					toList, ccList, new CustomEmailAddress(MAILBCCSCON[0].split( SPLIT_CHAR)[0]), 
					"Actualización Diaria de Estados de Cuenta", sHtmlCuerpo);
			
			/*
			MultiPartEmail email = new MultiPartEmail();
			email.setHostName(ipservermail);
			email.setFrom("webmaster@casapellas.com.ni","Módulo de Caja: Confirmación depósitos");
			email.setSubject("Actualización Diaria de Estados de Cuenta");
			email.addPart(sHtmlCuerpo, "text/html");
			email.setBounceAddress( MAILBCCSCON[0].split( SPLIT_CHAR)[0] );
			
			for (String dtaCc : MAILBCCSCON) {
				email.addBcc(  dtaCc.split(SPLIT_CHAR)[0],  dtaCc.split(SPLIT_CHAR)[1]  );
			};
		
			
			lstCtaCorreo = new ArrayList<String[]>();
			lstCtaCorreo.add(new String[]{"Luis Fonseca", "lfonseca@casapellas.com.ni"}) ;
			

			for (String[] sCtaCorreo : lstCtaCorreo) {
				
				email.setTo( Arrays.asList( new InternetAddress( sCtaCorreo[1],  sCtaCorreo[0]) ) );
				
				try {
					
//					email.send();
					
				} catch (Exception e) {
					e.printStackTrace();
					rutinaError(e.getMessage(), LecturaConciliacion.CODUSUARIO, new Date(), new Date(), 0, "");
				}
			}
			*/
		} catch (Exception e) {
			error = new Exception("Error de sistema al intentar enviar el correo");
			bEnviado = false;
			e.printStackTrace();
		}
		return bEnviado;
	}
/******************************************************************************************/
/** Método: Metodo principal para leer los datos del archivo plano.
 *	Fecha:  18/07/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public static boolean rutinaError(String sMensajeEv, int idUsuario, Date Hini, Date Hfin, int iTipoEv,String sNombreArchivo){
		boolean bHecho = true;
		String sql = "";
		String sDtFechaEv="";
		String sHoraIni="";
		String sHoraFin="";
		String sNombreServer="";
		
		try {
			sDtFechaEv = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			sHoraIni =  new SimpleDateFormat("HH:mm:ss").format(Hini);
			sHoraFin =  new SimpleDateFormat("HH:mm:ss").format(Hfin);
			sNombreServer  =  InetAddress.getLocalHost().getHostName();
			if(sNombreServer.length()>150){
				sNombreServer = sNombreServer.substring(0,149);
			}
			sql=  " INSERT INTO "+LecturaConciliacion.ESQUEMA+".LOGCONCILIACION";
			sql+= " (IDUSUARIO, EVFECHA, HORAINI, HORAFIN, IDTERMINAL, EVENTO, TIPOEV, NOMBREARCHIVO )";
			sql+= " VALUES("+idUsuario+", '"+sDtFechaEv+"', '"+sHoraIni+"', '"+sHoraFin+"', '"+sNombreServer+"',";
			sql+= "'"+sMensajeEv+"', "+iTipoEv+", '"+sNombreArchivo+"') ";
			
			Connection cn =  getConnection();
			if(cn==null){
				error = new Exception("_.rutinaError(): Error al abrir conexion a base de datos");
				return false;
			}
			PreparedStatement ps = cn.prepareStatement(sql);
			int rs = ps.executeUpdate();
			if (rs != 1 || rs == 0) {
				error = new Exception("Error al tratar de insertar registro de Log.");
			}
			
			ps.close();
			cn.commit();
			cn.close();
			
		} catch (Exception e) {
			bHecho = false;
			e.printStackTrace();
		}
		return bHecho;
	}
	
/******************************************************************************************/
/** Método: Leer los datos del documento que emite el banco en el ftp.
 *	Fecha:  27/07/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public static BufferedReader cargarArchivoDeFTP(String sNombreArchivo,String sUrlArchivoFTP){
		BufferedReader br = null;
		
		try {
			URL url = new URL(sUrlArchivoFTP);
			URLConnection urlc = url.openConnection();
			urlc.setConnectTimeout(30000);
			InputStream is  = urlc.getInputStream();
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(sNombreArchivo));
			int c;
			while ((c = is.read()) != -1)
				bw.write(c);
			
			is.close();
			bw.flush();
			bw.close();
			
			FileReader fr = new FileReader(sNombreArchivo);
			br = new BufferedReader(fr);
			
		} catch (Exception e) {
			br = null;
			error = e;
			e.printStackTrace();
		}
		return br;
	}
/******************************************************************************************/
/** Método: Leer los datos del documento que emite el banco en el ftp.
 *	Fecha:  18/07/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public static boolean leerArchivoBanco(String[] sDtBco, String sNombreArchivo, Date dtHoraInicio){
		boolean bExito=true;
		int iInicio,iFinal,iContador=0;
		int[] iLongitudCampo = {2, 2, 2, 2, 2, 2, 2, 40, 9, 9,16,16,16,3,1}; 
		int[] iEspacio = 	   {1, 1, 1, 1, 1, 1, 0, 0 , 1, 1,1, 1, 1, 1,6};
		String[] sDscrCampo = {
					"DIAV","MESV","ANIOV","DIAP","MESP","ANIOP",
					"CODTRANS","DESCRIPCION","REFERENCIA","CUENTA",
					"DEBITO","CREDITO","BANLANCE","SUCURSAL","TIPOREGISTRO"
					};
		String[] sPartes = new String[iLongitudCampo.length];
		BufferedReader br = null;
		String sLineaDoc="", sMensaje="";
		List<String[]> lstDatosDeps = new ArrayList<String[]>();
		File fArchivoFisico = null;
		int iCodigoBanco = 100001;
		String sFtpArchivo = "";
		List<String>sLineasArchivo = null;
		int cantidadRegistros ;
		
		try {
			
			iCodigoBanco = Integer.parseInt( sDtBco[0] ) ;  
			
			//&& ===== Crear el nombre del archivo y direccion url para el ftp.
			sNombreArchivo = generarNombreArchivo(LecturaConciliacion.dtFechaLectura, sDtBco[3]);
			sFtpArchivo =  "ftp://"+sDtBco[6]+":"+sDtBco[7]+"@"+sDtBco[5]+sDtBco[4]+"/"+sNombreArchivo;
			
			fArchivoFisico = new File(sNombreArchivo);

			//&& ===== Leer el archivo.
			br = cargarArchivoDeFTP(sNombreArchivo,sFtpArchivo);
			if (br == null) {
				sMensaje = " ftp "+sDtBco[5]+" "+sDtBco[4]+"&&&&&"+error.getMessage();
			
				rutinaError(sMensaje, LecturaConciliacion.CODUSUARIO, dtHoraInicio, new Date(), 3,
							sNombreArchivo+"&&&&Banco: "+sDtBco[0]+"&&&&&Cuenta: "+sDtBco[3]);
				return false;
			}
			
			sLineasArchivo = new ArrayList<String>();
			
			while( ( sLineaDoc = br.readLine() ) != null ){
				sLineasArchivo.add(sLineaDoc);
			} 
			
			cantidadRegistros =  sLineasArchivo.size() ;
			
			//&& ==== Recorrer el archivo.
			for (int j = 0; j < sLineasArchivo.size(); j++) {
				sLineaDoc = sLineasArchivo.get(j);
				iInicio = 0;
				iFinal  = 0;
				sPartes = new String[iLongitudCampo.length];
				
				if(j == (sLineasArchivo.size()-1) ){
					for(int i=0; i<iLongitudCampo.length ; i++){
						iInicio = iFinal + iEspacio[i];
						iFinal  = iInicio + iLongitudCampo[i];
						
						
						switch (i) {
						case 6:
							sPartes[i] = "FA";
							break;
						case 7:
							sPartes[i] = "Sin descripcion";
							break;
						case 8:
							sPartes[i] = (sLineaDoc.substring(iInicio, iFinal).trim().isEmpty()? "0": sLineaDoc.substring(iInicio, iFinal).trim() );
							break;
						case 13:
							sPartes[i] = (sLineaDoc.substring(iInicio, iFinal).trim().isEmpty()? "0": sLineaDoc.substring(iInicio, iFinal).trim() );
							break;
							
						default:
							sPartes[i] = sLineaDoc.substring(iInicio, iFinal).trim();
							break;
						}
					}
				}else{
					for(int i=0; i<iLongitudCampo.length ; i++){
						iInicio = iFinal + iEspacio[i];
						iFinal = iInicio+iLongitudCampo[i];
	
						if(sLineaDoc.length()>iInicio &&  sLineaDoc.length()>= iFinal ){
							sPartes[i] = sLineaDoc.substring(iInicio, iFinal).trim();
							if(sPartes[i].length() == 0  ){
								if(i==8){
									sPartes[i] ="Sin Descripcion";
								}else{
									sMensaje = "El valor del campo:  "+sDscrCampo[i]+" en la linea "+(iContador+1) +" No coincide con su formato definido: Valor Cero";
									bExito = false;
									break;
								}
							}else
							if(sPartes[i].length()!= iLongitudCampo[i]){
								if( (i==6||i==16) ||  (sPartes[i].length()> iLongitudCampo[i]) ){
									sMensaje = "El valor del campo:  "+sDscrCampo[i]+" en la linea "+(iContador+1) +" No coincide con su formato definido: Valor fuera de rango";
									bExito = false;
									break;
								}
							}
						}else{
							sMensaje = "El contenido de la linea "+(iContador+1) +" No coincide con su formato definido en el documento";
							bExito = false;
							break;
						}
					}
				}
				lstDatosDeps.add(sPartes);
				sLineaDoc = br.readLine();
				iContador++;
			}
			br.close();
			
			//&& ===== Clasificar y convertir los datos del plano en tipos de datos.
			if(bExito){
				
				Connection cn =  getConnection();
				if(cn==null){
					error = new Exception("_.leerArchivoBanco(): Error al abrir conexion a base de datos");
					return false;
				}
				cn.setAutoCommit(false);
				
				//==== Guardar la cabecera del archivo.
				int iNoMaestroArchivo = guardaMaestroArchivo(cn,fArchivoFisico, iContador, iCodigoBanco, 
														sDtBco[2], LecturaConciliacion.CODUSUARIO, (sDtBco[5]+" "+sDtBco[4]) );
				if(iNoMaestroArchivo == -1){
					sMensaje = "No se puede guardar la cabecera de "+ESQUEMA+".ARCHIVO para "+sNombreArchivo+" obtenido desde "+sFtpArchivo ;
					sMensaje += (error!=null)?error.getMessage():"Error no identificado en la aplicacion";
					bExito = false;
				}else{
					
					
					//&& ================ Guardar el detalle del archivo si trae transacciones
					if(cantidadRegistros > 0){
						
						bExito = guardaDetalleArchivo(cn, iNoMaestroArchivo, sNombreArchivo, lstDatosDeps);
						if(!bExito){
							sMensaje = "No se puede guardar la detalle de "+ESQUEMA+".DEPBANCODET para "+sNombreArchivo+" obtenido desde "+sFtpArchivo ;
							sMensaje += (error == null)? "Generado por un error no identificado en la aplicacion" : error.getMessage();
						}
					}
					
				}
				
				if(bExito){
					cn.commit();
				}else{
					cn.rollback();
					rutinaError(sMensaje, LecturaConciliacion.CODUSUARIO, dtHoraInicio, new Date(), 0, fArchivoFisico.getAbsolutePath());
				}
				cn.close();
				
				
			}else{
				error = new Exception(sMensaje);
				sMensaje += " ===> Para el archivo "+sNombreArchivo +" a la fecha " +new Date();
				rutinaError(sMensaje, LecturaConciliacion.CODUSUARIO, dtHoraInicio, new Date(), 0, fArchivoFisico.getAbsolutePath());
			}
			if(fArchivoFisico.exists()){
				fArchivoFisico.delete();
			}
			
		} catch (Exception e) {
			sMensaje = "Error de Aplicacion al intentar recuperar la informacion de archivo de banco "+sNombreArchivo;
			rutinaError(sMensaje += e.getMessage(), LecturaConciliacion.CODUSUARIO, dtHoraInicio, new Date(), 0, sNombreArchivo);
			error = e;
			e.printStackTrace();
		}
		return bExito;
	}
/******************************************************************************************/
/** Método: Convierte los valores String leidos del archivo plano a Tipos de Datos para BD.
 *	Fecha:  18/07/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public static boolean guardaDetalleArchivo(Connection cn, int iIDARCHIVO,String sNombreArchivo, List<String[]>lstDepositosBCO){
		boolean bHecho = true;
		String sFprocesa, sFvalor;
		Date dtFechaProcesa;
		Date dtFechaValor;
		String codtransaccion;	
		String decripcion;
		String sFechaValor, sFechaProcesa;
		int referencia;	          	                    	                    
		int nocuenta;	            	                    	                    
		BigDecimal mtodebito;	           	                    	                    
		BigDecimal mtocredito;	          	                    	                    
		BigDecimal balance;	             	                    	                    
		int codsucursal;	         	                    	                    
		String tiporegistro;	        	                    	                    
		int idtipoconfirm;	       	                    	                    
		int idestadocnfr;
		String sql ="";
		String sMensaje = "";
		
		try {
			
			int insertsprod = 0 ;
			
			int iContador = 0;
			int iCantRepetidos=0;
			for (String[] sDpBco : lstDepositosBCO) {
				
				//--- Fecha Valor y Fecha Procesa.
				sFprocesa = sDpBco[2]+"-"+sDpBco[1]+"-"+sDpBco[0];
				sFvalor   = sDpBco[5]+"-"+sDpBco[4]+"-"+sDpBco[3];
				dtFechaProcesa = new SimpleDateFormat("yy-MM-dd").parse(sFprocesa);
				dtFechaValor   = new SimpleDateFormat("yy-MM-dd").parse(sFvalor);
				sFechaProcesa  = new SimpleDateFormat("yyyy-MM-dd").format(dtFechaProcesa);
				sFechaValor    = new SimpleDateFormat("yyyy-MM-dd").format(dtFechaValor);
				
				codtransaccion  = sDpBco[6];
				decripcion	    = sDpBco[7];
				referencia		= Integer.parseInt(sDpBco[8]);
				nocuenta		= Integer.parseInt(sDpBco[9]);
				mtodebito		= new BigDecimal(sDpBco[10]);
				mtocredito		= new BigDecimal(sDpBco[11]);
				balance			= new BigDecimal(sDpBco[12]);
				codsucursal		= Integer.parseInt(sDpBco[13]);
				tiporegistro 	= sDpBco[14];       	                    	                    
				idtipoconfirm	= LecturaConciliacion.idtipoconfirm;	       	                    	                    
				idestadocnfr	= LecturaConciliacion.idestadocnfr;
				
				PreparedStatement ps = null;
				
				//&& ===== Buscar si no existe ya el registro, si no existe se guarda como nuevo.
				sql = " SELECT * FROM "+LecturaConciliacion.ESQUEMA+".DEPBANCODET ";
				sql+= " WHERE FECHAPROCESO = '"+sFechaProcesa+"' AND  FECHAVALOR = '"+sFechaValor+"'";
				sql+= " AND CODTRANSACCION = '"+codtransaccion+"' AND DESCRIPCION =  '"+decripcion+"'";
				sql+= " AND REFERENCIA = '"+referencia +"' AND NOCUENTA = "+nocuenta +" and MTODEBITO ="+mtodebito;
				sql+= " AND MTOCREDITO = " +mtocredito +" AND BALANCE = " +balance+ " AND  CODSUCURSAL ='"+codsucursal+"'";
				sql+= " AND TIPOREGISTRO = '"+tiporegistro+"'";

				ps = cn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				ResultSet resSet = ps.executeQuery();
				resSet.beforeFirst();
				
				if(!resSet.next()){
					//&& ===== Guardar en BD los datos del archivo (maestro) y su contenido (detalle)
					sql =  " INSERT INTO "+LecturaConciliacion.ESQUEMA+".DEPBANCODET\n ";
					sql += " (IDARCHIVO, FECHAPROCESO, FECHAVALOR, CODTRANSACCION, DESCRIPCION, REFERENCIA, NOCUENTA,  ";
					sql += "  MTODEBITO, MTOCREDITO, BALANCE, CODSUCURSAL, TIPOREGISTRO, IDTIPOCONFIRM, IDESTADOCNFR) \n" ;
					sql += " VALUES("+iIDARCHIVO+ ", '"+sFechaProcesa+"', '"+sFechaValor+"', '"+codtransaccion+"', ";
					sql += " '"+decripcion+"', '"+referencia +"', " +nocuenta +", " +mtodebito +", " +mtocredito +", \n" +balance;
					sql += ", '"+codsucursal+"', '"+tiporegistro+"', "+idtipoconfirm+", " +idestadocnfr +")";
					
					ps = cn.prepareStatement(sql);
					int rs = ps.executeUpdate();
					if (rs !=1 || rs==0) {
						ps.close();
						bHecho = false;
						sMensaje = " No se pudo registrar el detall de archivo "+iIDARCHIVO +", Linea del documento "+iContador+1;
						sMensaje +=" Sentencia SQL ejecutada: ====>"+sql+"<=====: Resultado devuelto de ps.executeUpdate() : "+rs+" Lineas afectadas";
						error = new Exception(sMensaje);
						break;
					}
					iContador++;
					
				}else{
					iCantRepetidos++;
				}
			}
			//&& ====== si iCantidadRepetidos >0, hay registros repetidos, guardar mensaje.
			if(iCantRepetidos>0){
				error = new Exception("Se han encontrado "+iCantRepetidos+ "Lineas Repetidas en el archivo ====> "+iIDARCHIVO);
				rutinaError("Se han encontrado "+iCantRepetidos+ "Lineas Repetidas en el archivo ====> "+iIDARCHIVO+"<==== " +sNombreArchivo, 
							LecturaConciliacion.CODUSUARIO, new Date(), new Date(),	0, sNombreArchivo);
			}
			//&& ======= si contador es cero, no inserto ningun registro, retornar falso y no guardar el archivo.
			if(iContador==0){
				bHecho = false;
				error = new Exception("Estado de cuenta : "+sNombreArchivo+" No registrado: Depositos ya fueron agregados anteriormente" );
			}
		} catch (Exception e) {
			bHecho = false;
			error = e;
			e.printStackTrace();
		}
		return bHecho;
	}
/******************************************************************************************/
/** Método: Guardar la cabecera del archivo.
 *	Fecha:  18/07/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public static int guardaMaestroArchivo(Connection cn, File fArchivoBanco,int iCantidadLineas,int iIdBanco,String MONEDA,int USRCREA,String sDirFTP ){
		int iNoMaestroArchivo=-1;
		String sMensaje ="";
		
		try {
			String NOMBRE = fArchivoBanco.getName();
			Long  TAMANIO = fArchivoBanco.length() / 1000;
			int CANTLINEA = iCantidadLineas;
			int IDBANCO   = iIdBanco;
			String DIREFTP	= sDirFTP;
			int ESTADO		= LecturaConciliacion.arESTADO;
			int ESTADPROC	= LecturaConciliacion.arESTADPROC;
			
			Date DATEARCHIVO = new SimpleDateFormat("ddMMyy HH:mm:ss")
										.parse(NOMBRE.split("_")[0].substring(3,9)+ " "+
										new SimpleDateFormat("HH:mm:ss").format(new Date()) );
			
			String OBSERVACION = "Sin observacion";
			String sFechaArchivo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(DATEARCHIVO);
			String sFechaCreacion = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( dtFechaLectura );
			
//			sFechaCreacion = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.ssssss").format( new Date() );
			
			String sql = "INSERT INTO "+LecturaConciliacion.ESQUEMA +".ARCHIVO ";
			sql +="\n(NOMBRE, TAMANIO, CANTLINEA, IDBANCO, MONEDA, DIREFTP, ESTADO, ESTADPROC,";
			sql +="\n DATEARCHIVO, FECHACREA, USRCREA, USRMODI, OBSERVACION, DEPOSITOSRESTANTES) VALUES";
			sql +="('"+NOMBRE+"', "+TAMANIO +", " +CANTLINEA  +", "  +IDBANCO +", '" +MONEDA +"', '" +DIREFTP +"', ";
			sql += ESTADO +", "+ ESTADPROC +", '" +sFechaArchivo +"', '" +sFechaCreacion +"', " +USRCREA +", " +USRCREA +", '" +OBSERVACION +"', "+CANTLINEA+" )";
			
			PreparedStatement ps = cn.prepareStatement(sql);
			int rs = ps.executeUpdate();
			if (rs !=1 || rs==0) {
				iNoMaestroArchivo=-1;
				sMensaje = " No se pudo registrar cabecera de "+ESQUEMA+".ARCHIVO " +NOMBRE +" Sentencia SQL ejecutada:";
				sMensaje +="  ====>"+sql+"<=====: Resultado devuelto de ps.executeUpdate() : "+rs+" Lineas afectadas";
				error = new Exception(sMensaje);
			}
			ps.close();
			
			//=== obtener el id generado para el archivo.
			sql =  " SELECT IDARCHIVO FROM "+LecturaConciliacion.ESQUEMA+".ARCHIVO WHERE NOMBRE = '"+NOMBRE+"'";
			sql += " AND TAMANIO = "+TAMANIO + " AND CANTLINEA =" +CANTLINEA +" AND FECHACREA = '"+sFechaCreacion+"'";
			ps = cn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			ResultSet resSet = ps.executeQuery();
			resSet.beforeFirst();
			
			if(resSet.next()){
				iNoMaestroArchivo = resSet.getInt(1);
			}else{
				iNoMaestroArchivo = -1;
				sMensaje = " No se pudo obtener los datos de archivo "+NOMBRE+", desde base de datos";
				sMensaje +=" Sentencia SQL ejecutada: ====>"+sql+"<=====: Resultado devuelto de resSet.next(): FALSE";
				error = new Exception(sMensaje);
			}
			resSet.close();
			ps.close();
			
		} catch (Exception e) {
			iNoMaestroArchivo=-1;
			error = e;
			e.printStackTrace();
		}
		return iNoMaestroArchivo;
	}
/******************************************************************************************/
/** Método: Obtener la cuenta y el Id de la cuenta para los bancos configurados en caja.
 *	Fecha:  18/07/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public static List<String[]>obtenerBancosConfigurados(Date dtHoraInicio){
		List<String[]>lstBancos = new ArrayList<String[]>();
		String sql="";
		PreparedStatement ps = null;
		Connection cn = null;
		
		try {
//			Class.forName(LecturaConciliacion.CLASSFORNAME);
//		    cn = DriverManager.getConnection(LecturaConciliacion.URLSERVER, LecturaConciliacion.USUARIO,LecturaConciliacion.PASSWRD);
			cn =  getConnection();
			if(cn==null){
				error = new Exception("_.obtenerBancosConfigurados(): Error al abrir conexion a base de datos");
				return null;
			}
			cn.setAutoCommit(false);
		    
			sql =  " SELECT D3CODB, D3RP01, D3CRCD, D3NOCUENTA, FTP.FOLDER, FTP.URLINTRA, FTP.USUARIO, FTP.CONTRASENIA";
			sql += " FROM  "+PropertiesSystem.ESQUEMA+".F55CA022 f22 inner join "+PropertiesSystem.ESQUEMA+".f55ca023 f23 on f22.codb = f23.d3codb";
			sql += " INNER JOIN  "+PropertiesSystem.ESQUEMA+".FTPCONCILIACION FTP ON   D3CODB = CODIGOBANCO";
			sql += " WHERE  ESTADO = 'A' AND LEERECBANCO = 1 AND D3NOCUENTA <> 0 ";

			sql += " AND D3CODB = 100001 " ;
			
			ps = cn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = ps.executeQuery();
			rs.beforeFirst();
			
			if(rs!=null){
				while(rs.next()){
					String[] lstDatoBanco = new String[8];
					lstDatoBanco[0]= rs.getString(1); //codigo del banco.
					lstDatoBanco[1]= rs.getString(2); //Compania
					lstDatoBanco[2]= rs.getString(3); //moneda
					lstDatoBanco[3]= rs.getString(4); //cuenta banco
					lstDatoBanco[4]= rs.getString(5); //nombre de la carpeta en el ftp
					lstDatoBanco[5]= rs.getString(6); //configuracion de ip externa
					lstDatoBanco[6]= rs.getString(7); //usuario para conexion.
					lstDatoBanco[7]= rs.getString(8); //contrasenia para conexion.
					lstBancos.add(lstDatoBanco);
				}
			}
			rs.close();
			ps.close();
			cn.commit();
			cn.close();
			
		} catch (Exception e) {
			error = e;
			rutinaError(e.getMessage(), LecturaConciliacion.CODUSUARIO, dtHoraInicio , new Date(), 0, "OBTENER INFORMA BANCO: 'obtenerBancosConfigurados' ");
			e.printStackTrace();
		}
		return lstBancos;
	}
/******************************************************************************************/
/** Método: Enviar el correo de notificacion de archivos de banco leidos a la fecha.
 *	Fecha:  05/08/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public static String generarCuerpoCorreo(){
		String sHtml = "";
		List<String[]> sLeidos = new ArrayList<String[]>();
		List<String[]> sNoLeidos = new ArrayList<String[]>();
		StringBuilder sTituloDet = new StringBuilder();
		StringBuilder sTablaArchivo = new StringBuilder();
		StringBuilder sbTablaCorreo = new StringBuilder();
		
		String sEstiloTdhdr="", sEstiloTD="";
		
		try {
			sEstiloTdhdr += "style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color:white; ";
			sEstiloTdhdr += " background-color: #5e89b5;\" align=\"center\" ";
			sEstiloTD += " style=\" font-family: Arial,Helvetica, sans-serif;font-size: 12px;color: #1a1a1a; border-color: silver; ";
			sEstiloTD += " border-style: dashed; border-width: 1px\" ";
			
			sTablaArchivo.append("<table width=\"750px\" style=\"border-color: silver; border-width: 1px; border-style: solid\">");
			sTablaArchivo.append("<tr>");
			sTablaArchivo.append("<td height=\"25px%\" colspan = \"7\" "+sEstiloTD+ " valign=\"bottom\" align=\"left\"  >");
			sTablaArchivo.append("<b>Archivos leídos correctamente:  </b>");
			sTablaArchivo.append("</td>");
			sTablaArchivo.append("</tr>");
			
			sTituloDet.append("<tr>");			
			sTituloDet.append("<td "+sEstiloTdhdr+ ">");
			sTituloDet.append("<b>N°</b>");
			sTituloDet.append("</td>");
			
			sTituloDet.append("<td "+sEstiloTdhdr+ ">");
			sTituloDet.append("<b>Nombre</b>");
			sTituloDet.append("</td>");
			
			sTituloDet.append("<td "+sEstiloTdhdr+ ">");
			sTituloDet.append("<b>Banco</b>");
			sTituloDet.append("</td>");
			
			sTituloDet.append("<td "+sEstiloTdhdr+ ">");
			sTituloDet.append("<b>Cuenta</b>");
			sTituloDet.append("</td>");
			
			sTituloDet.append("<td "+sEstiloTdhdr+ ">");
			sTituloDet.append("<b>Moneda</b>");
			sTituloDet.append("</td>");
			
			sTituloDet.append("<td "+sEstiloTdhdr+ ">");
			sTituloDet.append("<b>Fecha</b>");
			sTituloDet.append("</td>");
			
			sTituloDet.append("<td "+sEstiloTdhdr+ ">");
			sTituloDet.append("<b>Observación</b>");
			sTituloDet.append("</td>");
			sTituloDet.append("</tr>");
			sTablaArchivo.append(sTituloDet);
			
			//&& ==== obtener la lista de archivos leidos, para generar el cuerpo del correo.
			sLeidos = obtenerArchivosLeidos(  LecturaConciliacion.dtFechaLectura );
			if(sLeidos!=null && sLeidos.size()>0){
				for (String[] sLeidoDet : sLeidos) {
					sTablaArchivo.append("<tr>");
					sTablaArchivo.append("<td align=\"right\" " +sEstiloTD+ " >");
					sTablaArchivo.append(sLeidoDet[0]);
					sTablaArchivo.append("</td>");

					sTablaArchivo.append("<td align=\"left\" " +sEstiloTD+ " >");
					sTablaArchivo.append(sLeidoDet[1]);
					sTablaArchivo.append("</td>");
					
					sTablaArchivo.append("<td align=\"right\" " +sEstiloTD+ " >");
					sTablaArchivo.append(sLeidoDet[2]);
					sTablaArchivo.append("</td>");
					
					sTablaArchivo.append("<td align=\"right\" " +sEstiloTD+ "  >");
					sTablaArchivo.append(sLeidoDet[3]);
					sTablaArchivo.append("</td>");
					
					sTablaArchivo.append("<td align=\"center\" " +sEstiloTD+ " >");
					sTablaArchivo.append(sLeidoDet[4]);
					sTablaArchivo.append("</td>");
					
					sTablaArchivo.append("<td align=\"left\" " +sEstiloTD+ " >");
					sTablaArchivo.append(sLeidoDet[5] + " "+sLeidoDet[6]);
					sTablaArchivo.append("</td>");
					
					sTablaArchivo.append("<td align=\"left\" " +sEstiloTD+ " >");
					sTablaArchivo.append("Registros Contenidos: "+sLeidoDet[7]);
					sTablaArchivo.append("</td>");
					sTablaArchivo.append("</tr>");
				}
			}else{
				sTablaArchivo.append("<tr>");
				sTablaArchivo.append("<td colspan = \"7\" "+sEstiloTdhdr+ " align=\"center\"  >");
				sTablaArchivo.append("<b> No se obtuvo registro de nuevos archivos </b>");
				sTablaArchivo.append("</td>");
				sTablaArchivo.append("</tr>");
			}
			
			//&& ==== obtener la lista de archivos NO leidos, para generar el cuerpo del correo.
			sNoLeidos = obtenerArchivosNoLeidos(LecturaConciliacion.dtFechaLectura);
			if(sNoLeidos!=null && sNoLeidos.size()>0){
				sTablaArchivo.append("<tr>");
				sTablaArchivo.append("<td height=\"25px\" colspan = \"7\" "+sEstiloTD+ " valign=\"bottom\" align=\"left\"  >");
				sTablaArchivo.append("<b>Archivos que no se han podido leer:  </b>");
				sTablaArchivo.append("</td>");
				sTablaArchivo.append("</tr>");
				
				sTablaArchivo.append(sTituloDet.toString());
				
				for (String[] sNoLeidoDet : sNoLeidos) {
					sTablaArchivo.append("<tr>");
					sTablaArchivo.append("<td align=\"right\" " +sEstiloTD+ " >");
					sTablaArchivo.append(sNoLeidoDet[0]);
					sTablaArchivo.append("</td>");

					sTablaArchivo.append("<td align=\"left\" " +sEstiloTD+ " >");
					sTablaArchivo.append(sNoLeidoDet[1]);
					sTablaArchivo.append("</td>");
					
					sTablaArchivo.append("<td align=\"right\" " +sEstiloTD+ " >");
					sTablaArchivo.append(sNoLeidoDet[2]);
					sTablaArchivo.append("</td>");
					
					sTablaArchivo.append("<td align=\"right\" " +sEstiloTD+ "  >");
					sTablaArchivo.append(sNoLeidoDet[3]);
					sTablaArchivo.append("</td>");
					
					sTablaArchivo.append("<td align=\"center\" " +sEstiloTD+ " >");
					sTablaArchivo.append("SM");
					sTablaArchivo.append("</td>");
					
					sTablaArchivo.append("<td align=\"left\" " +sEstiloTD+ " >");
					sTablaArchivo.append(sNoLeidoDet[4] + " "+sNoLeidoDet[5]);
					sTablaArchivo.append("</td>");
					
					sTablaArchivo.append("<td align=\"left\" " +sEstiloTD+ " >");
					sTablaArchivo.append(sNoLeidoDet[6]);
					sTablaArchivo.append("</td>");
					sTablaArchivo.append("</tr>");
				}
			}
			sTablaArchivo.append("</table>");
			
			//&& ========= Generar el encabezado del correo.
			sbTablaCorreo.append("<table width=\"800px\" style=\"border: 1px #7a7a7a solid\" align=\"center\" cellspacing=\"0\" cellpadding=\"3\">");
			sbTablaCorreo.append("<tr>"); 
			sbTablaCorreo.append("<th colspan=\"2\" style=\"border-bottom: 1px #7a7a7a solid; background: #3e68a4\">");
			sbTablaCorreo.append("<font face=\"Arial\" size=\"2\" color=\"white\"><b>Notificación de Proceso para confirmación de depósitos.</b></font>");
			sbTablaCorreo.append("</th>");
			sbTablaCorreo.append("</tr>");
			
			sbTablaCorreo.append("<tr>");
			sbTablaCorreo.append("<td align:center colspan=\"2\" style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"90%\">");
			sbTablaCorreo.append("Se ha finalizado el proceso de lectura de archivo de banco para confirmación de depósitos.");
			sbTablaCorreo.append(" ==> Fecha: "+new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a").format(new Date()));
			sbTablaCorreo.append("</td>");
			sbTablaCorreo.append("</tr>");
			sbTablaCorreo.append("<tr>");
			sbTablaCorreo.append("<td colspan=\"2\" height=\"20px\">");
			sbTablaCorreo.append("</td>");
			sbTablaCorreo.append("</tr>");
			
			sbTablaCorreo.append("<tr>");
			sbTablaCorreo.append("<td colspan=\"2\" align=\"center\" >");
			sbTablaCorreo.append(sTablaArchivo);
			sbTablaCorreo.append("</td>");
			sbTablaCorreo.append("</tr>");
			
			sbTablaCorreo.append("<tr>");
			sbTablaCorreo.append("<td align=\"center\" colspan=\"2\" style=\"font-family: Arial, Helvetica, sans-serif;font-size: 10px;");
			sbTablaCorreo.append("color: black; border-bottom: 1px ##1a1a1a solid; \">");
			sbTablaCorreo.append("<b>Casa Pellas, S. A. - Módulo de Caja</b>");
			sbTablaCorreo.append("</td>");	
			sbTablaCorreo.append("</tr>");
			sbTablaCorreo.append("</table>");
			
			sHtml = sbTablaCorreo.toString();
			
		} catch (Exception e) {
			sHtml = null;
			error = e;
			e.printStackTrace();
		}
		return sHtml;
	}
/******************************************************************************************/
/** Método: Enviar el correo de notificacion de archivos de banco leidos a la fecha.
 *	Fecha:  05/08/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public static List<String[]> obtenerArchivosLeidos(Date dtFecha){
		String sql = "";
		PreparedStatement ps = null;
		List<String[]> lstLeidos = new ArrayList<String[]>();
		String[] lstDetalleArchivo;
		Connection cn = null;
		try {
			//==== conexion con la base de datos.
			cn =  getConnection();
			if(cn==null){
				error = new Exception("_.obtenerArchivosLeidos(): Error al abrir conexion a base de datos");
				return null;
			}
			cn.setAutoCommit(false);
			
			//&& === Archivos leidos Correctamente.
			sql =  " SELECT IDARCHIVO, NOMBRE, IDBANCO, MONEDA, DATEARCHIVO, CANTLINEA ";
			sql += " FROM " +LecturaConciliacion.ESQUEMA +".ARCHIVO";
			sql += " WHERE  (  CAST( FECHACREA AS DATE)   )  =  '"+ new SimpleDateFormat("yyyy-MM-dd").format(dtFecha) +"'"  ;
			sql += " AND ESTADO = "+LecturaConciliacion.arESTADO;
			
			ps = cn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = ps.executeQuery();
			rs.beforeFirst();
			
			if(rs.next()){
				rs.beforeFirst();
				while(rs.next()){
					lstDetalleArchivo= new String[9];
					lstDetalleArchivo[0]= rs.getString(1);		//Id del archivo.
					lstDetalleArchivo[1]= rs.getString(2); 		//Nombre del archivo.
					lstDetalleArchivo[2]= rs.getString(3); 		//Id del banco.
					lstDetalleArchivo[3]= rs.getString(2)
											.split("_")[1]
											.split("\\.")[0]; 	//cuenta banco
					lstDetalleArchivo[4]= rs.getString(4); 		//Moneda de la cuenta.
					lstDetalleArchivo[5]= new SimpleDateFormat("dd/MM/yyyy")
											.format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
											.parse(rs.getString(5)));  //Fecha del archivo. (generada) 
					lstDetalleArchivo[6]= new SimpleDateFormat("hh:mm:ss a")
											.format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
											.parse(rs.getString(5)));   //Hora del archivo. (generada) 
					lstDetalleArchivo[7]= rs.getString(6);				//Cantidad de registros
					lstDetalleArchivo[8]= "1"; 							//Tipo de Lectura: 1:Exito, 2:Fallo.
					lstLeidos.add(lstDetalleArchivo);
				}
			}
			rs.close();
			ps.close();
			cn.commit();
			cn.close();
			
		} catch (Exception e) {
			error = e;
			lstLeidos = null;
			e.printStackTrace();
		}finally{
			try {
				if(!cn.isClosed())
					cn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return lstLeidos;
	}
/******************************************************************************************/
/** Método: Obtener los archivos no que no fueron leídos desde el ftp.
 *	Fecha:  09/08/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public static List<String[]> obtenerArchivosNoLeidos(Date dtFecha){
		String sql = "";
		PreparedStatement ps = null;
		List<String[]> lstNoLeidos = new ArrayList<String[]>();
		String[] lstDetalleArchivo;
		Connection cn = null;
		
		try {
			//==== conexion con la base de datos.
			cn =  getConnection();
			if(cn==null){
				error = new Exception("_.obtenerArchivosNoLeidos(): Error al abrir conexion a base de datos");
				return null;
			}
			cn.setAutoCommit(false);
			
			//&& ==== Buscar los archivos no leidos por no encontrados o por error.
			sql = " SELECT IDLOGCONS, NOMBREARCHIVO, EVENTO, EVFECHA, HORAFIN ";
			sql +=" FROM "+LecturaConciliacion.ESQUEMA+".LOGCONCILIACION ";
			sql +=" WHERE EVFECHA = '"+ new SimpleDateFormat("yyyy-MM-dd").format(dtFecha) +"'";
			sql +=" AND TIPOEV = 3";

			ps = cn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = ps.executeQuery(); 
			rs.beforeFirst();
			
			if(rs.next()){
				rs.beforeFirst();
				while(rs.next()){
					lstDetalleArchivo= new String[8];
					lstDetalleArchivo[0]= rs.getString(1);					//Id del archivo.
					lstDetalleArchivo[1]= rs.getString(2)
											.split("&&&&")[0]; 				//Nombre del archivo.
					lstDetalleArchivo[2]= rs.getString(2)
											.split("&&&&")[1]
					                        .split(":")[1].trim(); 			//Id del banco.
					lstDetalleArchivo[3]= rs.getString(2)
											.split("&&&&")[2]
											.split(":")[1].trim(); 			//cuenta banco
					
					lstDetalleArchivo[4]= new SimpleDateFormat("dd/MM/yyyy")
											.format(new SimpleDateFormat("yyyy-MM-dd")
											.parse(String.valueOf(rs.getDate(4)))); 		//Fecha del archivo. (generada)
					
					
					lstDetalleArchivo[5]= new SimpleDateFormat("hh:mm:ss a")
											.format(new SimpleDateFormat("HH:mm:ss")
											.parse(String.valueOf(rs.getTime(5))));  		//Hora del archivo. (generada) 
					lstDetalleArchivo[6] =  rs.getString(3)
											  .split("&&&&&")[1];			//Mensaje del error generado.	
					lstDetalleArchivo[7]= "0"; 								//Tipo de Lectura: 1:Exito, 2:Fallo.
					lstNoLeidos.add(lstDetalleArchivo);
				}
			}
			rs.close();
			ps.close();
			cn.commit();
			cn.close();
			
		}catch(Exception e){
			error = e;
			lstNoLeidos = null; 
			e.printStackTrace();
		}finally{
			try {
				if(!cn.isClosed())
					cn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return lstNoLeidos;
	}
/******************************************************************************************/
/** Método: generar el nombre del archivo plano a buscar en el ftp
 *	Fecha:  05/08/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public static String generarNombreArchivo(Date dtFecha, String iNoCuenta){
		String sNombre ="";
		try {
			Calendar cFechaAyer = Calendar.getInstance();
			cFechaAyer.setTime(dtFecha);
			cFechaAyer.add(Calendar.DATE, -1);
			sNombre = "EST"+new SimpleDateFormat("ddMMyy").format(cFechaAyer.getTime())+"_"+iNoCuenta+".txt";
		} catch (Exception e) {
			error = e;
			sNombre = "";
			e.printStackTrace();
		}
		return sNombre;
	}
	/******************************************************************************************/
	/** Método: crear datos para tabla de consolidado de depositos de banco.
	 *	Fecha:  12/02/2015
	 *  Nombre: Carlos Manuel Hernández Morrison.
	 *  
	 */
	public static void cargarDatosConsolidado(Date fechaDepositos)  {
		boolean aplicado = true;
		Connection cn =  null;
		PreparedStatement ps = null;
		
		try {
			String fecha = new SimpleDateFormat("yyyy-MM-dd").format(fechaDepositos) ;
			
			String strSubSelectCodcomp = 
					"IFNULL( (SELECT trim(D3RP01) from @ESQUEMA.F55CA023 f23 " +
					"where f23.d3codb = a.idbanco AND f23.d3nocuenta = d.nocuenta " +
					"and d3crcd = a.moneda ), '00' )" ;
			
			
			String sql = "insert into @ESQUEMA.PCD_CONSOLIDADO_DEPOSITOS_BANCO (" +
			" ESTADOCUENTANOMBRE ,NUMEROCUENTA ,CODIGOBANCO ,MONEDA ,FECHADEPOSITO ,REFERENCIAORIGINAL , REFERENCIAJDE ," +
			" MONTOORIGINAL, MONTOAJUSTADO ,DESCRIPCION ,CODIGOTRANSACCIONBCO ,ESTADOCONFIRMACION ,CANTIDADCOMPARACION ," +
			" USUARIOACTUALIZA ,USUARIOULTIMACOMPARACION ,IDDEPBCODET ,TIPOTRANSACCIONJDE ,FECHAGRABADEPOSITO ," +
			" FECHAGRABACONSOLIDA ,FECHAMODCONSOLIDA,  DESCRIPTRANSBANCO, DESCRIPTRANSJDE, NOMBREBANCO, CODCOMP, IDARCHIVO, " +
			" NOMBRECOMPANIA )" +
			
			" ( select" +  
				" a.nombre, d.nocuenta, a.idbanco, a.moneda, d.fechaproceso, d.referencia, d.referencia," +
				" d.mtocredito, d.mtocredito, lower(trim(d.descripcion)), d.codtransaccion, 0, 0, " +
				" 99999999, 99999999,  iddepbcodet," + 
				" IFNULL( (SELECT CODDOCCAJA FROM  @ESQUEMA.EQUIVTIPODOCS WHERE CODIGO = d.codtransaccion FETCH FIRST ROWS ONLY ),d.codtransaccion), " +
				" a.fechacrea, current_timestamp, current_timestamp," +
				" IFNULL( (SELECT NOMBRE FROM  @ESQUEMA.EQUIVTIPODOCS WHERE CODIGO = d.codtransaccion FETCH FIRST ROWS ONLY ), d.codtransaccion)," +
				" IFNULL( (SELECT NOMDOCCAJA FROM  @ESQUEMA.EQUIVTIPODOCS WHERE CODIGO = d.codtransaccion FETCH FIRST ROWS ONLY ), d.codtransaccion) ," +
				" IFNULL( (SELECT lower( trim(banco) ) FROM  @ESQUEMA.F55CA022 WHERE CODB  = a.idbanco), CAST(a.idbanco AS VARCHAR(10)))," +
				" @SQL_FOR_CODCOMP, d.idarchivo, " +
				" IFNULL( (SELECT  LOWER(DRDL01)  FROM "+ PropertiesSystem.JDECOM +".F0005 CO WHERE CO.DRSY = '00' " +
							"AND CO.DRRT = '01' AND CO.DRDL02 = 'F' AND trim(drky) = ( @SQL_FOR_CODCOMP ) ) ,'E00') " +
				
			" from @ESQUEMA.depbancodet d inner join  @ESQUEMA.archivo a on a.idarchivo = d.idarchivo " +
			
//			" where date ( a.fechacrea )  = '"+fecha+"' and mtodebito = 0 and mtocredito  > 0 and d.referencia <> 0" +
			" where idbanco = 100001 and date ( a.fechacrea )  = '"+fecha+"' and mtodebito = 0 and mtocredito  > 0 and d.referencia <> 0  and codtransaccion  <> 'FA'" +
//			" where idbanco = 100002 and date ( a.fechacrea )  = '"+fecha+"' and mtodebito = 0 and mtocredito  > 0  " +
//			" where  date ( a.fechacrea )  = '"+fecha+"' and mtodebito = 0 and mtocredito  > 0 and codtransaccion  <> 'FA' " +
			" )" ;
			
			String insert = sql.replace("@SQL_FOR_CODCOMP", strSubSelectCodcomp).replace("@ESQUEMA", ESQUEMA);
		
			cn =  getConnection();
			cn.setAutoCommit(false);
			ps = cn.prepareStatement(insert);
			
			try {
				 ps.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
				rutinaError(e.getMessage(), LecturaConciliacion.CODUSUARIO, new Date(), new Date(), 3, "error al insertar en tabla consolidado");
			}
			
			//&& ========= Actualizar el monto de depositos pendientes de procesar para el estado de cuenta leido
			String sqlUpdateArchivo = 
				"UPDATE @ESQUEMA.ARCHIVO A SET DEPOSITOSRESTANTES = ( " +
						"select  count(*) from @ESQUEMA.depbancodet d1 " +
						"where D1.idarchivo = A.IDARCHIVO and   mtodebito = 0 " +
						"and mtocredito  > 0 and referencia <> 0 ) "+
				" WHERE DATE(FECHACREA) = CURRENT_DATE " ;
			
			ps = cn.prepareStatement(sqlUpdateArchivo.replace("@ESQUEMA", ESQUEMA));
			try {
				ps.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
				rutinaError(e.getMessage(), LecturaConciliacion.CODUSUARIO, new Date(), new Date(), 3, "error al insertar en tabla consolidado");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			aplicado = false;
			rutinaError(e.getMessage(), LecturaConciliacion.CODUSUARIO, new Date(), new Date(), 3, "error al insertar en tabla consolidado");
		}finally{
			try {
				if(cn != null && !cn.isClosed()){
					if(aplicado)
						cn.commit();
					else
						cn.rollback();
					ps.close() ;
					cn.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
				rutinaError(e2.getMessage(), LecturaConciliacion.CODUSUARIO, new Date(), new Date(), 3, "error al insertar en tabla consolidado");
			}
		}
	}
}


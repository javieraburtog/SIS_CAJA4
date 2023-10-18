package com.casapellas.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.URL;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.casapellas.controles.MetodosPagoCtrl;
import com.casapellas.entidades.MetodosPago;

//import nl.bitwalker.useragentutils.Browser;
import nl.bitwalker.useragentutils.UserAgent;
//import nl.bitwalker.useragentutils.Version;

public class PropertiesSystem {
	
   // Código de Usuario para Conexión a Base de Datos.
	//public final static String CN_USRNAME = "APPCP";
	//public final static String CN_USRPWD  = "APPCP1810";
	public final static String CN_USRNAME = "PRUEBAE2PD";
	public final static String CN_USRPWD  = "password";
	
   // Ambiente de Desarrollo/Pruebas, ESQUEMAS de Base de Datos.
   /*public final static String ESQUEMA = "E1GCPMCAJA";
	public final static String JDEDTA = "CRPDTA"; 
	public final static String JDECOM = "CRPCTL";
	public final static String QS36F = "E1QS36F" ;
	public final static String GCPDES = "GCPCERT";	
	public final static String GCPCXC = "E1GCPCXC";
	public final static String ENS = "E1ENS";
	public final static String GCPSISEVA = "E1GCPSISEVA2";
	public final static String INV = "E1INV";*/
	
	 // Ambiente de Produccion, ESQUEMAS de Base de Datos.
	   public final static String ESQUEMA = "E2GCPMCAJA";
		public final static String JDEDTA = "PRODDTA920"; 
		public final static String JDECOM = "PRODCTL920";
		public final static String QS36F = "E2QS36F" ;
		public final static String GCPDES = "GCPCERT";	
		public final static String GCPCXC = "E2GCPCXC";
		public final static String ENS = "ENS";
		public final static String GCPSISEVA = "GCPSISEVA2";
		public final static String INV = "E2INV";
	
		
   // Configuraciones para obtener cuentas
		public final static String parametroCaja="30";
		public final static String parametroFCVGanancia="11";
		public final static String parametroFCVPerdida="10";
		public final static String parametroNumeracionRU="36";
		//-Contado
		public final static String tipoTrxContado="CO";
		public final static String valuesJDEContado="CONTADO_JDE_INS";
		public final static String valuesJDENumeracion="NUM_JDE_CONF";
		public final static String valuesJDEDevolucionContado="DEV_CONT_JDE_INS";
		public final static String valuesJDECredito="CREDITO_JDE_INS";
		public final static String valuesJDEPrimaReserva="PRIMAS_JDE_INS";
		public final static String valuesJDEFinanciamiento="FINAN_JDE_INS";
		public final static String valuesJDEPMT="PMT_JDE_INS";
		public final static String valuesJDEFCV="FCV_JDE_INS";
				
   // Datos de Servidor de Base de Datos.
	public final static String IPSERVERDB2 = "192.168.1.3";

   // Datos para guardar archivos en el servidor en donde esté publicado el Sistema de CAja
	public final static String JNA_PATHx64 = "E:\\GCPMCAJA\\SocketPos_220x64";
	public final static String JNA_PATHx32 = "E:\\GCPMCAJA\\SocketPos_220x32";
		
   // Datos para Servidor de eMail, Sendiblue
	public final static String MAIL_SMTP_AUTH = "true";
	public final static String MAIL_SMTP_SSL_TRUST = "smtp-relay.sendinblue.com";
	public final static String MAIL_SMTP_SSL_PROTOCOLS = "TLSv1.2";
	public final static String MAIL_SMTP_HOST = "smtp-relay.sendinblue.com";
	public final static String MAIL_SMTP_PORT = "587";
	public final static String MAIL_SMTP_USER = "stinoco@casapellas.com";
	public final static String MAIL_SMTP_PASSWORD = "8tws4v6MYD37AQNy";

	public final static String MAIL_INTERNAL_ADDRESS = "";
	public final static String IPSERVERMAIL = "192.168.1.137";
	public final static String SPLIT_CHAR = "==>";
	public final static String LNS_REPUESTOS ="18,03,44";
	public final static String LNS_TALLER ="25,32,35,36,34";
	public final static String LNS_JDEDWARDS ="22,23,24";
	
	public final static String CONTEXT_NAME = "GCPMCAJA";
	
	public final static String SOCKETPOS_DDLNAME = "sockpos";
	public final static String JNA_SOCKETPOS_LIBNAME = "jna.library.path";

	public final static String[] MAILCCS = new String[]{

	};

	public final static String[] MAILBCCS = new String[]{
 
	};
	
	public final static String AMOUNT_FORMAT_STRING = "%1$,.2f";
	public final static int DIG_ROUNDS = 4;
	public final static RoundingMode ROUND_MODE = RoundingMode.HALF_UP ;
	public final static String REGEXP_8DIGTS ="^[0-9]{1,8}$";
	public final static String REGEXP_NUMBER ="^[0-9]+$";
	public final static String REGEXP_AMOUNT = "^[0-9]+\\.?[0-9]*$";
	public final static String REGEXP_ALFANUMERIC = "[A-Za-z0-9-]+$";
	public final static String REGEXP_DESCRIPTION = "^[ÑA-Zña-z0-9-\\p{Blank}]+$";
	public final static String REGEXP_EMAIL_ADDRESS = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
	
	public final static String WEBMASTER_EMAIL_ADRESS = "informatic@casapellas.com";
	public final static String MAIL_BOUNCEADDRESS = ""; 
	public final static String MAIL_BOUNCEADDRESS_TALLER_PMT = "";
	
	public String URLSIS = getUrlSisCaja();
	public String WEBBROWSER = webbrowser();
	public String IPADRESS_WBCLIENT = getIpRemoteClient();

	public final static String TipoDocJdeRetencionDonacion = "P8";
	public final static String TipoDocJdeComisionDonacion = "P9";
	
	public final static String TIPODOC_JDE_DEP_8 = "ZX";
	public final static String TIPODOC_JDE_DEP_N = "ZX";
	public final static String TIPODOC_JDE_DEP_H = "ZX";
	public final static String TIPODOC_JDE_DEP_5 = "ZX";
	
	public final static String CTA_DEUDORES_VARIOS_UNE01 = "24";  
	public final static String CTA_DEUDORES_VARIOS_UNE02=  "75"; 
	public final static String CTA_DEUDORES_VARIOS_UNE03 = "80"; 
	public final static String CTA_DEUDORES_VARIOS_UNE08 = "60"; 
	public final static String CTA_DEUDORES_VARIOS_UNE11 = "11024";
	public final static String CTA_DEUDORES_VARIOS_OB = "13600"; 
	public final static String CTA_DEUDORES_VARIOS_SB = "";
	
	
	
	//**************************** ServicioByte **************************/
	public final static String GCP_API_KEY = "NTAsNTIsMTAyLDU1LDUwLDEwMiwxMDEsMTAyLDUxLDQ4LDUxLDU3LDU2LDUwLDk5LDk4LDUzLDQ5LDEwMiw1NCw1Niw1MSwxMDIsNDgsNDksNDksOTcsNTcsNTAsNTIsNDgsNTM=";
	public final static String APP_MOD_CODE = "1";
	public final static String APP_CODE = "APP0000019";
	public final static String GCP_TERMINAL_ID = "CAJA-";
	public final static String GCP_APP_ID = "APP0000002";
	public final static String HOST_SERVICIO_BYTE = "http://172.17.17.20:9080/GCPBYTEWSREST/api/";
	public final static String SEGMENTO_FINANCIAMIENTO = "financiamiento";
	public final static String COMPANIA_BYTE = "E12";
	public final static String MONEDA_BYTE = "USD";
	public final static String BYTE_PARAMETRO_CONFIGURACION = "16";
	public final static String BYTE_PARAMETRO_MONEDA = "COD_MONEDA";
	public final static String BYTE_PARAMETRO_LINEA = "COD_LINEA";
	public final static String BYTE_PARAMETRO_SUCURSAL = "COD_SUC";
	public final static String BYTE_PARAMETRO_COMPANIA = "COD_COMPANIA";
	public final static String BYTE_PARAMETRO_CUENTA_TRANSITORIA = "CTA_TRANSITORIA";
	public final static String BYTE_PARAMETRO_CUENTA_POR_COBRAR = "CTA_POR_COBRAR";
	
	//**************************** Nuevos por preconciliacion **************************/ 
	public final static String[] MAILBCCSCON = new String[]{
		"lfonseca@casapellas.com.ni"+SPLIT_CHAR+"Luis Fonseca",
	};
	public final static String[] TipodocJdeTipoDocCaja = {
		MetodosPagoCtrl.EFECTIVO  + SPLIT_CHAR + TIPODOC_JDE_DEP_5,
		MetodosPagoCtrl.TARJETA + SPLIT_CHAR + TIPODOC_JDE_DEP_H,
		MetodosPagoCtrl.TRANSFERENCIA + SPLIT_CHAR + TIPODOC_JDE_DEP_8,
		MetodosPagoCtrl.DEPOSITO + SPLIT_CHAR + TIPODOC_JDE_DEP_N,
	};
	
	public final static int ID_DP_CONFIRMADO     =  35; 
	public final static int ID_DP_NO_CONFIRMADO  =  36; 
	public final static int ID_CRF_AUTOMATICA 	 =  32; 
	public final static String CFR_AUTO          =  "CAM"; 
	public final static String CFR_MANUAL        =  "CMN"; 
	public final static String DP_CONFIRMADO     =  "CFR"; 
	public final static String DP_NOCONFIRMADO   =  "SCR"; 
	
	public final static String TIPODOC_REFER_P9   =  "P9"; 
	public final static String TIPODOC_REFER_ZX   =  "ZX"; 
	public final static String TIPODOC_REFER_ZZ   =  "ZZ"; 
	public final static String TIPODOC_REFER_XG   =  "XG"; 
	
	public final static String MONEDA_BASE 		  =  "COR";  
	public final static String UNIDAD_NEGOCIO_BASE =  "2499" ;
	
	public final static String CODIGO_TIPO_AUXILIAR_CT  = "Z" ;
	public final static String CODIGO_TIPO_AUXILIAR_FE  = "A" ; 
	public final static String CODIGO_CREDOMATIC  = "00062651";
	
	public final static String CTA_OTROS_INGRESOS_OB = "65100"; 
	public final static String CTA_OTROS_INGRESOS_SB = "10";
	
	public final static String CTA_GASTOS_DIVERSOS_OB = "65200"; 
	public final static String CTA_GASTOS_DIVERSOS_SB = "08";
	
	public final static String ENS_ADMINISTRADOR_CAJA = "P000000004"; 
	public final static String ENS_CONCILIADOR_PRINCIPAL = "P000000042";
	public final static String ENS_CONCILIADOR_SUPERVISOR = "P000000041";
	
	public final static String USUARIO_PRECONCILIACION = "PRE_CONCIL";
	public final static String CTA_PRECONCILIA_AJUSTE_UN = "24"; 
	public final static String CTA_PRECONCILIA_AJUSTE_OB = "11101"; 
	public final static String CTA_PRECONCILIA_AJUSTE_SB = "95";
	
	public final static String CTA_PRECONCILIA_AJUSTE_ID = "( 20105318 )";  
	public final static String CONTABLIZA_PROGRAMA_ID = "P09800";
	public final static String CONTABILIZA_PROGRAMA_VERSION = "PRE_CONCIL";
	
	public final static String CARPETA_DOCUMENTOS_EXPORTAR = "DocumentsExport";
	public final static String RUTA_DOCUMENTOS_EXPORTAR = "E:\\GCPMCAJA\\filestoexport\\";
	
	public final static BigDecimal FALTANTE_PERMITIDO_COR = BigDecimal.ONE;
	public final static BigDecimal FALTANTE_PERMITIDO_USD = BigDecimal.ONE;
	
	public final static String DIFCAMB_CUENTA_OBJETO = "66000";
	public final static String DIFCAMB_CUENTA_SUBSID = "01";
	
	public final static String CODIGO_AUTORIZACION_REIMPRIMIR_RECIBOS = "A000000133";
	
	public final static String[][] MONTOS_FALTANTES_PERMITIDOS = {
		{ "USD",  "1.00" },
		{ "COR",  "1.00" },
	};
	
	public final static int ID_CUENTA_BANCO_PV = 20000103;
	
	public final static List<MetodosPago> formasPagoConfiguradas = MetodosPagoCtrl.formasPagoConfiguradas();
	
	//**********************************************************************************/
	
	
	public static String getDataFromPcClient(){
		String pcinfo = "";
		
		try {
			
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
			 URL reconstructedURL = new URL(request.getScheme(), request.getServerName(), request.getServerPort(), "");
			
			String clientip     = request.getRemoteAddr(); 
			String ipreceived   = request.getLocalAddr();
			String clientpcname = InetAddress.getByName( clientip ).getHostName();
			String urlsiscaja   = reconstructedURL.toString() + request.getContextPath();
			String webbrowser   = UserAgent.parseUserAgentString(request.getHeader("user-agent")).getBrowser() + " " 
								+ UserAgent.parseUserAgentString(request.getHeader("user-agent")).getBrowserVersion(); 
			
			pcinfo = "ctpc: -" + clientpcname + "-, cltip: -" + clientip +"-, iprcv: -" +  ipreceived +"-, url: -" + urlsiscaja  +"-, brws: -" +   webbrowser +"- " ;
			
			
		} catch (Exception e) {
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
			pcinfo = null;
		}finally{
			
			if(pcinfo == null){
				pcinfo = getIpRemoteClient() +", " + webbrowser() +", " + getUrlSisCaja();
			}
			
		}
		return pcinfo;
		
	}
	
	private static String getIpRemoteClient() {
		String ipAdress = "127.0.0.1";
		try {
			HttpServletRequest request = (HttpServletRequest) FacesContext
					.getCurrentInstance().getExternalContext().getRequest();
			ipAdress = request.getHeader("X-FORWARDED-FOR");

			if (ipAdress == null)
				ipAdress = request.getRemoteAddr();
		} catch (Exception e) {
			ipAdress = "127.0.0.1";
		}
		return ipAdress;
	}
	
	private static String webbrowser(){
		String webbrowsername = new String();
		try {
			HttpServletRequest r = (HttpServletRequest) FacesContext
					.getCurrentInstance().getExternalContext().getRequest();
			String userAgent = r.getHeader("user-agent");
			UserAgent ua = UserAgent.parseUserAgentString(userAgent);
			webbrowsername = ua.getBrowser() + " " + ua.getBrowserVersion(); 

		} catch (Exception e) {
			webbrowsername = "NO DEFINIDO";
		}
		return webbrowsername;
	}
	private static String getUrlSisCaja() {
		String urlsis = new String("");
		try {
			HttpServletRequest r = (HttpServletRequest) FacesContext
					.getCurrentInstance().getExternalContext().getRequest();
		   URL reconstructedURL = new URL(r.getScheme(), r.getServerName(), r.getServerPort(), "");
		   urlsis = reconstructedURL.toString() + r.getContextPath();
		   
		} catch (Exception e) {
			urlsis = CONTEXT_NAME;
		}
		return urlsis;
	}
 
	public static String UrlSisCaja(){
		return getUrlSisCaja();
	}
	
}

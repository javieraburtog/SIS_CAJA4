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
		
		public final static Boolean Is_Debug_Mode = true;
	
		
		// Configuraciones para obtener cuentas
		public final static String parametroCaja="30";
		public final static String parametroFCVGanancia="11";
		public final static String parametroFCVPerdida="10";
		public final static String parametroNumeracionRU="36";
		public final static String parametroCuentaSalida="38";
		public final static String parametroConciliacion="39";
		public final static String parametroCierre="34";
		public final static String parametroCtaDeudores="40";
		public final static String parametroCompania="41";
		
		//Cierre
		public final static String CIERRE_FALTANTE_TIPODOC = "GENENOTADEB_TIPODOC";
		
		//-Facturacion
		public final static String tipoTrxContado="CO";
		public final static String valuesJDEContado="CONTADO_JDE_INS";
		public final static String valuesJDENumeracion="NUM_JDE_CONF";
		public final static String valuesJDEDevolucionContado="DEV_CONT_JDE_INS";
		public final static String valuesJDECredito="CREDITO_JDE_INS";
		public final static String valuesJDEPrimaReserva="PRIMAS_JDE_INS";
		public final static String valuesJDEFinanciamiento="FINAN_JDE_INS";
		public final static String valuesJDEPMT="PMT_JDE_INS";
		public final static String valuesJDEFCV="FCV_JDE_INS";
		
		//Conciliacion
		public final static String IDDPCONFIRMADO="ID_DP_CONFIRMADO";
		public final static String IDDPNOCONFIRMADO="ID_DP_NO_CONFIRMADO";
		public final static String IDCRFAUTOMATICA="ID_CRF_AUTOMATICA";
		public final static String CFRAUTO="CFR_AUTO";
		public final static String CFRMANUAL="CFR_MANUAL";
		public final static String DPCONFIRMADO="DP_CONFIRMADO";
		public final static String DPNOCONFIRMADO="DP_NOCONFIRMADO";
		public final static String TIPODOCREFERP9="TIPODOC_REFER_P9";//not working
		public final static String TIPODOCREFERZX="TIPODOC_REFER_ZX";
		public final static String TIPODOCREFERZZ="TIPODOC_REFER_ZZ";
		public final static String TIPODOCREFERXG="TIPODOC_REFER_XG";//Not Working
		public final static String MONEDABASE="MONEDA_BASE";
		public final static String UNIDADNEGOCIOBASE="UNIDAD_NEGOCIO_BASE";
		public final static String CODIGOTIPOAUXILIARCT="CODIGO_TIPO_AUX_CT";
		public final static String CODIGOTIPOAUXILIARFE="CODIGO_TIPO_AUX_FE";
		public final static String CODIGOCREDOMATIC="CODIGO_CREDOMATIC";
		public final static String USUARIOPRECONCILIACION="USUARIO_PRECON";
		public final static String CTAOTROSINGRESOS="CTA_OTROS_INGR_OB";
		public final static String CTAGASTOSDIVERSOS="CTA_GTOS_DIV_OB";
		
		
		public final static String CTADEUDORESVARIOSOB="CTA_DEUDO_VAR_OB";
		public final static String CTADEUDORESVARIOSSB="CTA_DEUDO_VAR_SB";
		
		//Login
		public final static String ENSADMINISTRADORCAJA ="ENS_ADM_CAJA";
		public final static String ENSCONCILIADORPRINCIPAL="ENS_CON_PRIN";
		public final static String ENSCONCILIADORSUPERVISOR="ENS_CON_SUP";
		
		//Compania
		public final static String companiaCASAPELLAS ="CASAPELLAS";
		public final static String companiaALPESA ="ALPESA";
		public final static String companiaKIPESA ="KIPESA";
		public final static String companiaCAPESA ="CAPESA";
		
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

	public final static String TIPODOC_JDE_DEP_8 = "ZX";
	public final static String TIPODOC_JDE_DEP_N = "ZX";
	public final static String TIPODOC_JDE_DEP_H = "ZX";
	public final static String TIPODOC_JDE_DEP_5 = "ZX";

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
		"byron.canales@ahinko.com"+SPLIT_CHAR+"Byron Canales",
	};
	public final static String[] TipodocJdeTipoDocCaja = {
		MetodosPagoCtrl.EFECTIVO  + SPLIT_CHAR + TIPODOC_JDE_DEP_5,
		MetodosPagoCtrl.TARJETA + SPLIT_CHAR + TIPODOC_JDE_DEP_H,
		MetodosPagoCtrl.TRANSFERENCIA + SPLIT_CHAR + TIPODOC_JDE_DEP_8,
		MetodosPagoCtrl.DEPOSITO + SPLIT_CHAR + TIPODOC_JDE_DEP_N,
	};
	
	public final static String CARPETA_DOCUMENTOS_EXPORTAR = "DocumentsExport";
	//Produccion
	public final static String RUTA_DOCUMENTOS_EXPORTAR = "E:\\GCPMCAJA\\filestoexport\\";
	//Para Prueba
	//public final static String RUTA_DOCUMENTOS_EXPORTAR = "C:\\GCPMCAJA\\filestoexport\\";
	
	public final static BigDecimal FALTANTE_PERMITIDO_COR = BigDecimal.ONE;
	public final static BigDecimal FALTANTE_PERMITIDO_USD = BigDecimal.ONE;
	
	public final static String DIFCAMB_CUENTA_OBJETO = "66000";
	public final static String DIFCAMB_CUENTA_SUBSID = "01";
	
	public final static String CODIGO_AUTORIZACION_REIMPRIMIR_RECIBOS = "A000000133";
	
	public final static String[][] MONTOS_FALTANTES_PERMITIDOS = {
		{ "USD",  "1.00" },
		{ "COR",  "1.00" },
	};
	
	
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

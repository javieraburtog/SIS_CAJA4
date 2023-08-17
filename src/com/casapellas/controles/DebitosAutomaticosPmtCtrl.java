package com.casapellas.controles;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

//import org.apache.commons.collections.CollectionUtils;
//import org.apache.commons.collections.Predicate;
//import org.apache.commons.mail.MultiPartEmail;
import org.hibernate.Session;
import org.hibernate.Transaction;
 
import com.casapellas.controles.tmp.CompaniaCtrl;
import com.casapellas.entidades.B64strfile;
import com.casapellas.entidades.ResumenCobroAutomatico;
import com.casapellas.entidades.Vf55ca01;
import com.casapellas.entidades.pmt.Vwbitacoracobrospmt;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.reportes.ArqueoCajaR;
import com.casapellas.reportes.ArqueoSocketPos;
import com.casapellas.socketpos.TransaccionTerminal;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.CustomEmailAddress;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.MailHelper;
import com.casapellas.util.PropertiesSystem;
import com.casapellas.entidades.ens.Vautoriz;
import com.crystaldecisions.report.web.viewer.ReportExportControl;
import com.crystaldecisions.reports.sdk.ReportClientDocument;
import com.crystaldecisions.sdk.occa.report.application.OpenReportOptions;
import com.crystaldecisions.sdk.occa.report.exportoptions.ExportOptions;
import com.crystaldecisions.sdk.occa.report.exportoptions.PDFExportFormatOptions;
import com.crystaldecisions.sdk.occa.report.exportoptions.ReportExportFormat;
import com.ibm.icu.util.Calendar;


public class DebitosAutomaticosPmtCtrl {
	
	@SuppressWarnings("unchecked")
	public static  List<Integer> cuentasDeNotificacionTaller() {
		
		List<Integer> codigosEmpleadoCorreos = new ArrayList<Integer>();
		
		try {
			
			String query =  
					" SELECT  distinct(wwan8) FROM "+PropertiesSystem.JDEDTA+".F0111 where wwidln = 0 and lower(trim(wwrem1)) in " +
					" ( " +
					" select  ifnull(  lower(trim(tbdesc)), '' ) from "
						+PropertiesSystem.QS36F+".sottab " +
						" where tbcodi = 229 and tbclas = 'PMT'  " +
					" ) " ;
			
			List<Integer> codigosEmpleadoCorreosTmp = (ArrayList<Integer>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, true, null);
			
			if(codigosEmpleadoCorreosTmp == null)
				return codigosEmpleadoCorreos;
			
			List<String> codigos = new ArrayList<String>();
			 
			for(int i=0; i < codigosEmpleadoCorreosTmp.size(); i++) 
				 codigos.add( String.valueOf( codigosEmpleadoCorreosTmp.get(i) )) ;
			
			for(int i=0; i < codigos.size(); i++) 
				 codigosEmpleadoCorreos.add( Integer.parseInt(codigos.get(i) ) );
			
			
		}catch (Exception e) {
			 e.printStackTrace();
		}
		return codigosEmpleadoCorreos;
	}
	
	public static void grabarReporteResumenCobrosAutomaticos(String rutaReporte, int idemision, String parentrowid){
	    Session sesion = null;
		Transaction trans = null;
		boolean newCn = false;
		
		try {
			
			sesion = HibernateUtilPruebaCn.currentSession();
			trans = (newCn = !(sesion.getTransaction().isActive())) ? sesion
					.beginTransaction() : sesion.getTransaction();
			
			File rptRecibos = new File( rutaReporte );
			String[] strParts = ArqueoCajaCtrl.crearMinutaStringArray(rptRecibos);
			
			if(strParts == null)
				strParts = new String[]{"","",""};
			
			for (int i = 0; i < strParts.length; i++) {
				B64strfile b64StrPart = new B64strfile(strParts[i], i,  idemision, 174, parentrowid);
				try {
					sesion.save(b64StrPart);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			
		} catch (Exception e) {
			 e.printStackTrace(); 
		}finally{
			if(newCn){
				try {  trans.commit(); } 
				catch (Exception e2) { }
				try {  HibernateUtilPruebaCn.closeSession(sesion); }
				catch (Exception e2) { }
			}
			sesion = null;
			trans = null;
		}
	}
	
	
	public static void notificacionCobroNoAplicado( List<Vwbitacoracobrospmt> cuotasNoAplicadas) {
		List<String> codesExclude = Arrays.asList(new String[] {"00", "XX","DB", "19", ""} );
		
		String strHtml = tablaDetalleCobroNoAplicado()  ;
		String body = "" ;
		
	
		try {
			  
			
			List<Vwbitacoracobrospmt> cuotas = cuotasNoAplicadas.stream()
					.filter( cuota -> !codesExclude.contains( cuota.getResponseCode() )  )
					.collect(Collectors.toList());
			
		
			cuotas = cuotas.stream()
					.filter( cuota -> !cuota.getCorreocliente().trim().isEmpty()  )
					.collect(Collectors.toList());
			
			cuotas = cuotas.stream()
					.filter( cuota -> ! CodeUtil.validate_email_address(cuota.getCorreocliente())  )
					.collect(Collectors.toList());
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			
			for (Vwbitacoracobrospmt v : cuotas) {
				
				List<CustomEmailAddress> toList = new ArrayList<CustomEmailAddress>();

				toList.add(new CustomEmailAddress(v.getCorreocliente().trim(), CodeUtil.capitalize( v.getClientenombre().trim() )));
				
				//&& =========== validacion de correo configurado para el envio
				
				//&& ===========  cuerpo del correo
				String msg =" Estimado Cliente, el siguiente intento de cobro no fue posible de aplicar: "  +  v.getCorreocliente().trim() ;
				body  = msg +" <br>" ; 
				
				body += strHtml
					.replace("@CODIGO", String.valueOf(v.getMpbcli() ) )
					.replace("@NOMBRE", CodeUtil.capitalize( v.getClientenombre().toLowerCase() )  )
					.replace("@CONTRATO", String.valueOf( v.getMpbnctto() ) )
					.replace("@CUOTA", String.valueOf( v.getMpbnpag() ))
					.replace("@MONTO", String.format("%1$,.2f", v.getMontocobrarentarjeta() ) )
					.replace("@MONEDA", v.getMpbmon() )
					.replace("@FECHACUOTA", sdf.format(v.getMpbfcgnr() ) )
					.replace("@TARJETA",  String.valueOf( v.getNumerotarjeta4d() ))
					.replace("@FECHAVENCETC", String.valueOf( v.getMpbvtrj() ) )
					.replace("@NUMEROAFILIADO", v.getCodigoafiliado().trim() )
					.replace("@RESPUESTABCO", v.getResponseCode() + ": " + v.getResponseCodeDescription()  )
					.replace("@VENDEDORPMT",  CodeUtil.capitalize(v.getNombrevendedor() ) )
					 ;
				
				MailHelper.SendHtmlEmail(
						new CustomEmailAddress(PropertiesSystem.WEBMASTER_EMAIL_ADRESS, "Talleres Casa Pellas"),
						toList, null, new CustomEmailAddress(PropertiesSystem.MAIL_BOUNCEADDRESS), 
						"Cobro automático No Aplicado, Contrato PMT", body);
			}

			
		}catch(Exception e) {
			e.printStackTrace(); 
		}
	}
	
	public static void notificacionCorreoCuotaAplicada(List<Vwbitacoracobrospmt> cuotaAplicada, List<String[]> cuentasNotificaciones ){
		try {
			
			
			String strHtml = tablaDetalleCobroCuota();
			String body ;
			
			List<CustomEmailAddress> bbcList = new ArrayList<CustomEmailAddress>();
			
		
			if( cuentasNotificaciones != null ){
				
				for (String[] dtaCta  : cuentasNotificaciones) {
					bbcList.add(new CustomEmailAddress(dtaCta[0].trim(), dtaCta[1].trim()));
				}
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
						
			for (Vwbitacoracobrospmt v : cuotaAplicada) {

				List<CustomEmailAddress> toList = new ArrayList<CustomEmailAddress>();
				
				//&& =========== validacion de correo configurado para el envio  
				toList.add(new CustomEmailAddress(v.getCorreocliente().trim(), CodeUtil.capitalize( v.getClientenombre().trim() )));
				
				body = strHtml
						.replace("@CODIGO", String.valueOf(v.getMpbcli() ) )
						.replace("@NOMBRE", CodeUtil.capitalize( v.getClientenombre().toLowerCase() )  )
						.replace("@CONTRATO",  String.valueOf( v.getMpbnctto() ) )
						.replace("@CUOTA",  String.valueOf( v.getMpbnpag() ))
						.replace("@RECIBO",  String.valueOf( v.getMpbnumrec() ))
						.replace("@COMPROBANTE",  String.valueOf(v.getNumerobatchpago() ) )
						.replace("@TARJETA",  String.valueOf( v.getNumerotarjeta4d() ))
						.replace("@MONTO",  String.format("%1$,.2f", v.getMontocobrarentarjeta() ) )
						.replace("@MONEDA", v.getMpbmon() )
						
						.replace("@AUTORIZADO", v.getAuthorizationNumber() )
						.replace("@REFERENCIA", v.getReferenceNumber().trim() )
						.replace("@TERMINALID", v.getCodigoterminal().trim() )
						.replace("@NUMEROAFILIADO", v.getCodigoafiliado().trim() )
						.replace("@FACTURA", v.getInvoicenumber().trim() )
						
						.replace("@FECHACUOTA", sdf.format(v.getMpbfcgnr() ) )
						.replace("@FECHAAPLICADA", sdf.format( new Date() ) ) ;
				
				MailHelper.SendHtmlEmail(
						new CustomEmailAddress(PropertiesSystem.WEBMASTER_EMAIL_ADRESS, "Talleres Casa Pellas"),
						toList, bbcList, new CustomEmailAddress(PropertiesSystem.MAIL_BOUNCEADDRESS), 
						"Notificación de Cobros Automaticos", body);
				
				v.setNotificado(true);
				
			}
			
		} catch (Exception e) {
			 e.printStackTrace(); 
		}
	}
	
	public static String tablaDetalleCobroNoAplicado(){
		String html = "";
		try {
			
			StringBuilder sbTablaDetalleCuota = new StringBuilder("");
			String sEstiloTdhdr =  
			  " style=\" " +
			  " font-family: Arial, Helvetica, sans-serif;" +
			  " font-size: 12px; " +
			  " color:white;" +
			  " padding: 2px 10px; " +
			  " background-color: #5e89b5;" +
			  " text-align: center; \" " ;
			
			String sEstiloTD =  
				" style=\" " +
				" font-family: Arial, Helvetica, sans-serif; " +
				" font-size: 11px;" +
				" color: #1a1a1a; " +
				" text-align: center; " +
				" border: 1px dashed silver; \" ";
			
			sbTablaDetalleCuota.append("<table style=\"width: auto; margin-top: 15px; border: 1px solid silever; \">");
			
			String[] titulosTabla = {
					"Codigo", "Nombre", "Contrato", 
					"Cuota", "Monto", "Moneda",
					"Fecha Pago", "Tarjeta", "Vencimiento",
					"Afiliado", "Razón"
					 };

			sbTablaDetalleCuota.append("<tr>");			
			for (String titulo : titulosTabla) {
				sbTablaDetalleCuota.append("<td "+sEstiloTdhdr+ ">");
				sbTablaDetalleCuota.append("<b>"+titulo+"</b>");
				sbTablaDetalleCuota.append("</td>");
			}
			sbTablaDetalleCuota.append("</tr>");
			
			String[] detalleTabla = { 
					"@CODIGO", "@NOMBRE", "@CONTRATO",
					"@CUOTA", "@MONTO", "@MONEDA",
					"@FECHACUOTA", "@TARJETA", "@FECHAVENCETC",
					"@NUMEROAFILIADO", "@RESPUESTABCO",  
					 };
			
			sbTablaDetalleCuota.append("<tr>");			
			for (String detalle : detalleTabla) {
				sbTablaDetalleCuota.append("<td align=\"right\" " +sEstiloTD+ " >");
				sbTablaDetalleCuota.append( detalle );
				sbTablaDetalleCuota.append("</td>");
			}
			sbTablaDetalleCuota.append("</table>");
			
			
			//&& ================== tabla que contiene el detalle de la cuota 
			html = "<table style=\"width: 100%\">";
			
			html += "<tr>";
			html += "<td style =\" width:100%; text-align:center; \" >";
			html += sbTablaDetalleCuota;
			html += "</td>";
			html += "</tr>";
			
			html += "<tr>";
			html += "<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 11px;color: #1a1a1a; width:100% \" >";
			html += "<b>Favor contactarse lo antes posibles con Casa Pellas<br><br>Tel: 2255-4444 </b></td>";
			html += "</tr>";
			
			html += "<tr>";
			html += "<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 11px;color: #1a1a1a; width:100% \" >";
			html += "<b>Vendedor:</b>@VENDEDORPMT</td>";
			html += "</tr>";
			
			  
			html += "<tr>";
			html += "<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 11px;color: #1a1a1a; width:100% \" >";
			html += "<b><br>SALUDOS</b></td>";
			html += "</tr>";
			
			html += "<tr>";
			html += "<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 11px;color: #1a1a1a;  width= 100%; \" >";
			html += "<b> Talleres <br> Casa Pellas  "+new SimpleDateFormat("yyyy").format( new Date() )+" </b></td>";
			html += "</tr>";
			html += "</table>";
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return html;
	}
	 
	public static String tablaDetalleCobroCuota(){
		String html = "";
		try {
			
			StringBuilder sbTablaDetalleCuota = new StringBuilder("");
			String sEstiloTdhdr =  
			  " style=\" " +
			  " font-family: Arial, Helvetica, sans-serif;" +
			  " font-size: 12px; " +
			  " color:white;" +
			  " padding: 2px 10px; " +
			  " background-color: #5e89b5;" +
			  " text-align: center; \" " ;
			
			String sEstiloTD =  
				" style=\" " +
				" font-family: Arial, Helvetica, sans-serif; " +
				" font-size: 11px;" +
				" color: #1a1a1a; " +
				" text-align: center; " +
				" border: 1px dashed silver; \" ";
			
			sbTablaDetalleCuota.append("<table style=\"width: auto; margin-top: 15px; border: 1px solid silever; \">");
			
			String[] titulosTabla = {
					"Codigo", "Nombre", "Contrato", "Cuota",
					"Recibo",  "Tarjeta", "Monto",
					"Moneda", "Autorizado", "Referencia","Terminal ID",
					"Afiliado", "Factura", 
					"Fecha Cuota", "Fecha Aplicada" };

			sbTablaDetalleCuota.append("<tr>");			
			for (String titulo : titulosTabla) {
				sbTablaDetalleCuota.append("<td "+sEstiloTdhdr+ ">");
				sbTablaDetalleCuota.append("<b>"+titulo+"</b>");
				sbTablaDetalleCuota.append("</td>");
			}
			sbTablaDetalleCuota.append("</tr>");
			
			String[] detalleTabla = { "@CODIGO", "@NOMBRE", "@CONTRATO",
					"@CUOTA", "@RECIBO", "@TARJETA", "@MONTO",
					"@MONEDA", "@AUTORIZADO", "@REFERENCIA", "@TERMINALID", 
					"@NUMEROAFILIADO","@FACTURA", 
					"@FECHACUOTA", "@FECHAAPLICADA" };
			
			sbTablaDetalleCuota.append("<tr>");			
			for (String detalle : detalleTabla) {
				sbTablaDetalleCuota.append("<td align=\"right\" " +sEstiloTD+ " >");
				sbTablaDetalleCuota.append( detalle );
				sbTablaDetalleCuota.append("</td>");
			}
			sbTablaDetalleCuota.append("</table>");
			
			
			//&& ================== tabla que contiene el detalle de la cuota 
			html = "<table style=\"width: 100%\">";
			
			html += "<tr>";
			html += "<td style =\" width:100%; text-align:center; \" >";
			html += sbTablaDetalleCuota;
			html += "</td>";
			html += "</tr>";
			
			html += "<tr>";
			html += "<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 11px;color: #1a1a1a; width:100% \" >";
			html += "<b><br>SALUDOS</b></td>";
			html += "</tr>";
			
			html += "<tr>";
			html += "<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 11px;color: #1a1a1a;  width= 100%; \" >";
			html += "<b> Talleres <br> Casa Pellas  "+new SimpleDateFormat("yyyy").format( new Date() )+" </b></td>";
			html += "</tr>";
			html += "</table>";
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return html;
	}
	
	@SuppressWarnings("serial")
	public static void notificacionResumenProcesoCobros(String nombrecaja, int cantidadCuotas, List<String[]>cuentasCorreo, String rutaExcel, List<String[]>MontosMonedas ){
		try {
			
			File reporte = new File(rutaExcel);
			if(!reporte.exists()){
				return;
			}
			
			String htlm = "<table>";
			htlm += "<tr>";
			htlm += "<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"100%\" >";
			htlm += "<b> ADJUNTO SE ENCUENTRA REPORTE RESUMEN DE TRANSACCIONES POR COBROS AUTOMATICOS </b></td>";
			htlm += "</tr>";	
			
			htlm += "<tr>";
			htlm += "<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"100%\" >";
			htlm += "<b>CAJA "+ nombrecaja +" </b></td>";
			htlm += "</tr>";
			
			htlm += "<tr>";
			htlm += "<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"100%\" >";
			htlm += "<b>Cantidad Cuotas  "+cantidadCuotas +"</b></td>";
			htlm += "</tr>";
			
			
			htlm += "<tr><td><br></td></tr> ";
			
			if( MontosMonedas != null && !MontosMonedas.isEmpty()  ) {
				
				for (String[] montoMoneda : MontosMonedas) {
					htlm += "<tr>";
					htlm += "<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 14px;color: #1a1a1a;\" width=\"100%\" >";
					htlm += "<b>Monto Pendiente a Cobrar en : " + montoMoneda[0].toUpperCase() +": " + montoMoneda[1] +"</b></td>";
					htlm += "</tr>";
				}
			}
			htlm += "<tr><td><br></td></tr> ";
			
			htlm += "<tr>";
			htlm += "<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"100%\" >";
			htlm += "<b>FECHA "+ new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date() )+"</b></td>";
			htlm += "</tr>";
			
			htlm += "<tr>";
			htlm += "<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"100%\" >";
			htlm += "<b><br>SALUDOS</b></td>";
			htlm += "</tr>";
			htlm += "</table>";
			
			String subject = ( "Resumen de Proceso de Cobros automáticos por PMT" );
			
			List<CustomEmailAddress> toList = new ArrayList<CustomEmailAddress>();
			for (String[] sCtaCorreo : cuentasCorreo) {
				toList.add(new CustomEmailAddress(sCtaCorreo[0], sCtaCorreo[1]));
			}
			
			MailHelper.SendHtmlEmail(
					new CustomEmailAddress(PropertiesSystem.WEBMASTER_EMAIL_ADRESS, "Módulo de caja"),
					toList, 
					new ArrayList<CustomEmailAddress>() { { add(new CustomEmailAddress(PropertiesSystem.MAIL_BOUNCEADDRESS)); } } , 
					new ArrayList<CustomEmailAddress>() { { add(new CustomEmailAddress(PropertiesSystem.MAIL_BOUNCEADDRESS)); } }, 
					subject, htlm, new String[] { rutaExcel });
			
		} catch (Exception e) {
			e.printStackTrace(); 
		} 
	}
	
	public  static List<String[]> cuentasEnvioCorreos( Integer[] enviarCorreoA ){
		List<String[]> cuentas = null;
				
		try {
			

			List<Integer> lstEnviarCorreo = Arrays.asList(enviarCorreoA);
			
			String sql = " select lower(trim(d.wwrem1)), lower(trim(d.wwmlnm)) from  " 
					+ PropertiesSystem.JDEDTA +".f0111 d where trim(d.wwrem1) <> '' and wwidln = 0 and  wwan8 in " +
					lstEnviarCorreo.toString().replace("[", "(").replace("]", ")");
			
			@SuppressWarnings("unchecked")
			List<Object[]>lstCtaCorreo = (ArrayList<Object[]>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, true, null) ;
			
			if(lstCtaCorreo == null )
				return null;
			
			List<String> correos = new ArrayList<String>();
			cuentas = new ArrayList<String[]>(lstCtaCorreo.size());
		
			for (Object[] sCtaCorreo : lstCtaCorreo) {
				
				if(correos.contains(String.valueOf(sCtaCorreo[0])))
					continue;
				
				correos.add(String.valueOf(sCtaCorreo[0]) ) ;
				
				cuentas.add( new String[] {
						String.valueOf(sCtaCorreo[0]),
						String.valueOf(sCtaCorreo[1])
					});
			}
			
		} catch (Exception e) {
			cuentas = null;
			e.printStackTrace(); 
		}
		return cuentas;
	}
	
	
	
	@SuppressWarnings("serial")
	public static void enviarCierreSocketCajero(TransaccionTerminal terminal, List<String[]> cuentas){
		String htlm =  new String("");
		String subject = "";
		String cssFuente = "font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;";
		
		try {
			
			subject = "Reporte de cierre de Terminal "
					+ terminal.getTerminalid() + ", Caja " 
					+ terminal.getCaid() + ",  " + new SimpleDateFormat("dd MMMM yyyy",
							new Locale("es", "ES")).format(new Date());
			
			htlm += "<br><br><span style=\"" + cssFuente
					+ "\" ><b> Adjunto se encuentra reporte de liquidación"
					+ " de transacciones de SocketPos </b> </span>";
			
			htlm += "<br><br><span style=\"" + cssFuente
					+ "\" ><b>	Terminal:</b> " + terminal.getTerminalid() + " /  "
					+ terminal.getNombreterminal().toLowerCase();
			
			htlm += "<br><span style=\"" + cssFuente + "\" > <b> Monto de Cierre:</b> "
					+ new DecimalFormat("#,##0.00").format(terminal
						.getMto_Creditos()) + " </span>";
			
			htlm += "<br><span style=\"" + cssFuente
					+ "\" ><b>Transacciones aprobadas:</b> "
					+ terminal.getCant_Creditos() + " </span>";
			htlm += "<br><br><span style=\"" + cssFuente + "\" > " +
					" Detalle generado a la fecha: " + new SimpleDateFormat(
						"dd-MMMM-yyyy hh:mm:ss a", new Locale("ES", "es"))
						.format(new Date());
			
			List<CustomEmailAddress> toList = new ArrayList<CustomEmailAddress>();
			
			for (String[] sCtaCorreo : cuentas) {
				toList.add(new CustomEmailAddress(sCtaCorreo[0], sCtaCorreo[1]));
			}
			
			MailHelper.SendHtmlEmail(
					new CustomEmailAddress(PropertiesSystem.WEBMASTER_EMAIL_ADRESS, "Módulo de caja"),
					toList, 
					new ArrayList<CustomEmailAddress>() { { add(new CustomEmailAddress(PropertiesSystem.MAIL_BOUNCEADDRESS)); } } , 
					new ArrayList<CustomEmailAddress>() { { add(new CustomEmailAddress(PropertiesSystem.MAIL_BOUNCEADDRESS)); } }, 
					subject, htlm, new String[] { terminal.getRutarealreporte() });
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			 htlm =  null;
			 subject = null;
			 cssFuente = null;
		}
	}

	public static void generarReporteArqueoCaja(Vf55ca01 vf55ca01, Vautoriz vaut, 
			String moneda, String codcomp, int noarqueo, 
			BigDecimal bdTotalPagos, Date fecha,
			List<String[]> cuentasNotificaciones,
			String rutaReporte){
		
		try {
			
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
			String sCajero = CodeUtil.capitalize( vaut.getId().getEmpnombre().trim() ) ; 
			String sCaja = CodeUtil.capitalize( vf55ca01.getId().getCaname() );
			String sUbicacion = CodeUtil.capitalize( vf55ca01.getId().getCaco().trim() + " " + vf55ca01.getId().getCaconom().trim() ) ;
			
			String sNombrecomp = CodeUtil.capitalize( new CompaniaCtrl().obtenerNombreComp(codcomp, vf55ca01.getId().getCaid(), null, null) );
			
			if(moneda.compareTo("COR") == 0) moneda = "Córdobas";
			if(moneda.compareTo("USD") == 0) moneda = "Dólares";
			
			ArqueoCajaR rptmcaja = new ArqueoCajaR();
			rptmcaja.setCompania(sNombrecomp);
			rptmcaja.setNoarqueo(noarqueo);
			rptmcaja.setFechahora( format.format( fecha ) );
			rptmcaja.setNocaja( vf55ca01.getId().getCaid() );
			rptmcaja.setNombrecaja(sCaja);
			rptmcaja.setNombrecajero(sCajero);
			rptmcaja.setMoneda(moneda);
			rptmcaja.setSucursal(sUbicacion);
			
			double dbZero = 0;
			
			rptmcaja.setVentastotales( dbZero );
			rptmcaja.setDevoluciones( dbZero );
			rptmcaja.setVentascredito(dbZero);
			rptmcaja.setVentasnetas(dbZero);
			rptmcaja.setAbonos(dbZero);
			rptmcaja.setPrimasreservas( bdTotalPagos.doubleValue() );
			rptmcaja.setIngpagosdifmonedas( dbZero );
			rptmcaja.setOtrosingresos( dbZero);
			rptmcaja.setTotalingresos( bdTotalPagos.doubleValue() );
			 
			rptmcaja.setVentaspagbanco( dbZero );
			rptmcaja.setAbonospagbanco( dbZero );
			rptmcaja.setPrimaspagbanco( bdTotalPagos.doubleValue() );
			rptmcaja.setOtrosegresos( dbZero );
			rptmcaja.setTotalegresos( dbZero );
			rptmcaja.setEfecnetorecibido( dbZero );
			rptmcaja.setMinimoencaja( dbZero );	
			 
			rptmcaja.setDepositosugerido( dbZero );
			rptmcaja.setEfecencaja( dbZero );
			rptmcaja.setFaltantesobrante( dbZero );
			rptmcaja.setDepositofinal( dbZero );

			rptmcaja.setTefectivo( dbZero );
			rptmcaja.setTcheque( dbZero );
			rptmcaja.setTtcredito(dbZero );
			rptmcaja.setTtcreditom( dbZero );
			rptmcaja.setTtcreditosktpos( bdTotalPagos.doubleValue()  );
			
			rptmcaja.setTdepositobanco(dbZero);
			rptmcaja.setTtransfbanco(dbZero);
			rptmcaja.setTformaspago(bdTotalPagos.doubleValue() );
			
			//---- Nuevos Campos del reporte.
			rptmcaja.setSPagoIngresosExt( Double.toString( dbZero ));
			rptmcaja.setSPagoFinanciamiento( Double.toString(dbZero  ));
			rptmcaja.setSCambioOtraMoneda( Double.toString( dbZero ));
			rptmcaja.setSFinanciamientosBanco( Double.toString( dbZero  ));
			rptmcaja.setSIngresosExtBanco( Double.toString( dbZero ));
			rptmcaja.setSReferenciaDeposito( Double.toString( dbZero  ));
			rptmcaja.setSTipoImpresionRpt("");
			
			List<ArqueoCajaR> lstAcr = new ArrayList<ArqueoCajaR>();
			lstAcr.add( rptmcaja );
			
			enviarArqueoAlCajero(vf55ca01, vaut, codcomp, lstAcr, null, fecha, cuentasNotificaciones, rutaReporte);
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}
	 
	
	@SuppressWarnings("serial")
	public static void enviarArqueoAlCajero(Vf55ca01 dataCaja, Vautoriz vaut, String sCodcomp, 
					List<ArqueoCajaR> lstAcr, List<ArqueoSocketPos> lstSocktPOS, Date dtFechaProcesa, 
					List<String[]> cuentasNotificaciones, String rutaReporte){
		File flRptFisico = null;
		
		try {

			
		    ReportClientDocument rpt = new ReportClientDocument();
		    rpt.open("reportes/rptmcaja002.rpt", OpenReportOptions._openAsReadOnly);
			rpt.getDatabaseController().setDataSource(lstAcr,  ArqueoCajaR.class, "ArqueoCajaR", "ArqueoCajaR");
			
			if(lstSocktPOS == null)lstSocktPOS = new ArrayList<ArqueoSocketPos>(1);
			rpt.getSubreportController()
			 			.getSubreport("cierreSocketPos")
			 			.getDatabaseController()
			 			.setDataSource(lstSocktPOS, ArqueoSocketPos.class,"ArqueoSocketPos","ArqueoSocketPos");
			
			//&& ======= Propiedades del reporte.
			ReportExportControl exportControl = new ReportExportControl();
		    ExportOptions exportOptions = new ExportOptions();
		    exportOptions.setExportFormatType(ReportExportFormat.PDF);

		    PDFExportFormatOptions PDFExpOpts = new PDFExportFormatOptions();
		    PDFExpOpts.setStartPageNumber(1); 
		    PDFExpOpts.setEndPageNumber(100);
		    
		    exportOptions.setFormatOptions(PDFExpOpts);
		    exportControl.setExportOptions(exportOptions);
		    exportControl.setExportAsAttachment(true);
		    exportControl.setReportSource(rpt.getReportSource());
		    
		  //&& ====== Crear el archivo fisico.
		    ArqueoCajaR ar = lstAcr.get(0);
		  			
			String rutaf = PropertiesSystem.RUTA_DOCUMENTOS_EXPORTAR;
		    
			String sNomFile = "Arqueo" + ar.getNocaja() + "" + sCodcomp.trim()
					+ ar.getMoneda() + "_"+ new SimpleDateFormat
					("ddMMyyyyHHmmss").format(dtFechaProcesa)+ "_" 
					+ vaut.getId().getCodreg() + ".pdf";
			
		    
		    flRptFisico = new File(rutaf + sNomFile);
		    
		    byte[] data = new byte[1024];
		    OutputStream os = new FileOutputStream(flRptFisico.getAbsolutePath());
		    InputStream is  = new BufferedInputStream(rpt.getPrintOutputController().export(exportOptions));
		    
		    while (is.read(data) > -1) {
                os.write(data);
            }
		    os.close();

		    //&& ========= Guardar el arqueo de caja en base64.
		    Session sesion = null;
			Transaction trans = null;
			boolean newCn = false;
		   
			try {
				
		    	String parentrowid = ar.getNocaja() +"" +ar.getNoarqueo() + FechasUtil.formatDatetoString(dtFechaProcesa, "ddMMyyyyHHmmss");
		    	int idemision = Integer.parseInt( (ar.getNocaja() +""+ ar.getNoarqueo()) ) ;
		    	
				sesion = HibernateUtilPruebaCn.currentSession();
				trans = (newCn = !(sesion.getTransaction().isActive())) ? sesion
						.beginTransaction() : sesion.getTransaction();
				
				String[] strParts = ArqueoCajaCtrl.crearMinutaStringArray(flRptFisico);
				if(strParts == null)
					strParts = new String[]{"","",""};			
				for (int i = 0; i < strParts.length; i++) {
					B64strfile b64StrPart = new B64strfile(strParts[i], i,(int) idemision, 71, parentrowid);
					try {
						sesion.save(b64StrPart);
					} catch (Exception e) {
						e.printStackTrace(); 
					}
				}
				
				//&& =================== Grabar reporte de Excel con los recibos del dia.	
				if( rutaReporte != null && !rutaReporte.trim().isEmpty() ){
				
					File rptRecibos = new File( rutaReporte );
					strParts = ArqueoCajaCtrl.crearMinutaStringArray(rptRecibos);
					
					if(strParts == null)
						strParts = new String[]{"","",""};
					
					for (int i = 0; i < strParts.length; i++) {
						B64strfile b64StrPart = new B64strfile(strParts[i], i, (int) idemision, 74, parentrowid);
						try {
							sesion.save(b64StrPart);
						} catch (Exception e) {
							e.printStackTrace(); 
						}
					}
				}
						
			} catch (Exception e) {
				e.printStackTrace(); 
			}finally{
				if(newCn){
					try {  trans.commit(); } 
					catch (Exception e2) { }
					try {  HibernateUtilPruebaCn.closeSession(sesion); }
					catch (Exception e2) { }
				}
				sesion = null;
				trans = null;
			}
		    //=====================================================
		    
		    //&& ========= Enviar el correo con el adjunto.
			String htlm = "<table>";
			htlm += "<tr>";
			htlm += "<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"100%\" >";
			htlm += "<b>SE HA GENERADO EL REPORTE DE ARQUEO #"+ar.getNoarqueo()+"</b></td>";
			htlm += "</tr>";	
			
			htlm += "<tr>";
			htlm += "<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"100%\" >";
			htlm += "<b>CAJA "+ar.getNocaja()+" "+ar.getNombrecaja().toUpperCase()+" </b></td>";
			htlm += "</tr>";
			
			htlm += "<tr>";
			htlm += "<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"100%\" >";
			htlm += "<b>"+ar.getCompania().toUpperCase()+"</b></td>";
			htlm += "</tr>";
			
			htlm += "<tr>";
			htlm += "<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"100%\" >";
			htlm += "<b>MONEDA "+ar.getMoneda().toUpperCase()+"</b></td>";
			htlm += "</tr>";
			
			htlm += "<tr>";
			htlm += "<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"100%\" >";
			htlm += "<b>FECHA "+ar.getFechahora().toUpperCase()+"</b></td>";
			htlm += "</tr>";
			
			htlm += "<tr>";
			htlm += "<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"100%\" >";
			htlm += "<b><br>SALUDOS</b></td>";
			htlm += "</tr>";
			htlm += "</table>";
			
			String subject = ("Reporte de arqueo de caja a la fecha:" + new SimpleDateFormat("dd MMMM yyyy",new Locale("es","ES")).format( dtFechaProcesa ) );
			
			List<CustomEmailAddress> toList = new ArrayList<CustomEmailAddress>();
			for (String[] sCtaCorreo : cuentasNotificaciones) {
				toList.add(new CustomEmailAddress(sCtaCorreo[0], sCtaCorreo[1]));
			}
			
			if( rutaReporte != null && !rutaReporte.trim().isEmpty() ){
				MailHelper.SendHtmlEmail(
						new CustomEmailAddress(PropertiesSystem.WEBMASTER_EMAIL_ADRESS, "Módulo de caja"),
						toList, 
						new ArrayList<CustomEmailAddress>() { { add(new CustomEmailAddress(PropertiesSystem.MAIL_BOUNCEADDRESS)); } } , 
						new ArrayList<CustomEmailAddress>() { { add(new CustomEmailAddress(PropertiesSystem.MAIL_BOUNCEADDRESS)); } }, 
						subject, htlm, new String[] { flRptFisico.getAbsolutePath(), rutaReporte });
			} else {
				MailHelper.SendHtmlEmail(
						new CustomEmailAddress(PropertiesSystem.WEBMASTER_EMAIL_ADRESS, "Módulo de caja"),
						toList, 
						new ArrayList<CustomEmailAddress>() { { add(new CustomEmailAddress(PropertiesSystem.MAIL_BOUNCEADDRESS)); } } , 
						new ArrayList<CustomEmailAddress>() { { add(new CustomEmailAddress(PropertiesSystem.MAIL_BOUNCEADDRESS)); } }, 
						subject, htlm, new String[] { flRptFisico.getAbsolutePath()});
			}
		} catch (Exception e) {e.printStackTrace();
			e.printStackTrace(); 
		}
	}
	
	/**
	 * Actualizar los datos de la cuota en la tabla de cuotas pendientes de pago
	 * 20 de Junio 216
	 */
	public static String actualizarRegistroCuota(Session sesion, Vwbitacoracobrospmt v ) {
		String strMensaje = "";

		try {
			
			String strSqlWhere = " where mpbcli = @CODCLI and mpbnctto = @NUMCONTRATO and mpbcia = '@MPBCIA' and mpbsuc = '@MPBSUC' and mpbnpag = @NUMCUOTA  and mpbfpag = '@MPBFPAG'" ;
			strSqlWhere = strSqlWhere
					.replace("@CODCLI",  Integer.toString( v.getMpbcli() ) )
					.replace("@NUMCONTRATO", Integer.toString( v.getMpbnctto() ) )
					.replace("@MPBCIA",  v.getMpbcia() )
					.replace("@MPBSUC",  v.getMpbsuc() )
					.replace("@NUMCUOTA", Integer.toString( v.getMpbnpag() ) )
					.replace("@MPBFPAG", new SimpleDateFormat("yyyy-MM-dd").format( v.getMpbfpag() ) );
			
			String strSql = " select mpbcli from " +PropertiesSystem.QS36F + ".sotmpba " + strSqlWhere; 
			LogCajaService.CreateLog("actualizarRegistroCuota", "QRY", strSql);
			
			@SuppressWarnings("unchecked")
			List<Integer> rowfetch = (List<Integer>)sesion.createSQLQuery(strSql).list();
			
			if(rowfetch == null || rowfetch.isEmpty() ){
				return strMensaje = "No se ha encontrado el registro de la cuota en SOTMPBA para "  + strSqlWhere ; 
			}
			if( rowfetch.size() > 1  ){
				return strMensaje = "se han encontrado mas de un registro de la cuota en SOTMPBA para "  + strSqlWhere ; 
			}
			
			strSql  = "update @QS36F.sotmpba  set " 
					+ "  MPBCODCOMP = '@MPBCODCOMP' "
					+ ", MPBCODSUC  = '@MPBCODSUC'  "
					+ ", MPBUNINEG  = '@MPBUNINEG'  "
					+ ", MPBCAID    = '@MPBCAID'  "
					+ ", MPBNUMREC  =  @MPBNUMREC  "
					+ ", MPBTIPOREC = '@MPBTIPOREC' "
					+ ", MPBFECHA   = '@MPBFECHA'   "
					+ ", MPBFECAM   = '@MPBFECAM'   "
					+ ", MPBHRCAM   = '@MPBHRCAM'   "
					+ ", MPBPACAM   = '@MPBPACAM'   "
					+ ", MPBPRCAM   = '@MPBPRCAM'   "
					+ ", MPBHECAM   = '@MPBHECAM'   "
					+ ", MPBNINT   =  (MPBNINT + 1)  "
					+ ", MPBINTR   =  (MPBINTR + 1)  "
					+ ", MPBSTS1   =  '@MPBSTS1' "
					+ ", MPBSTS2   =  '@MPBSTS2' "
					+ strSqlWhere ;
			
			strSql = strSql
			.replace("@QS36F", PropertiesSystem.QS36F)
			.replace("@MPBCODCOMP", v.getMpbcodcomp()  )
			.replace("@MPBCODSUC", v.getMpbcodsuc()    )
			.replace("@MPBUNINEG", v.getMpbunineg()    )
			.replace("@MPBCAID",  Integer.toString( v.getMpbcaid() )  )
			.replace("@MPBNUMREC",  Integer.toString( v.getMpbnumrec() )  )
			.replace("@MPBTIPOREC", v.getMpbtiporec()  )
			.replace("@MPBFECHA", new SimpleDateFormat("yyyy-MM-dd").format( v.getMpbfecha() ) )
			.replace("@MPBFECAM", new SimpleDateFormat("yyyy-MM-dd").format( v.getMpbfecam() ) )
			.replace("@MPBHRCAM", new SimpleDateFormat("HH:mm:ss").format( v.getMpbhrcam() ) )
			.replace("@MPBPACAM", v.getMpbpacam() )
			.replace("@MPBPRCAM", v.getMpbprcam() ) 
			.replace("@MPBHECAM", v.getMpbhecam() )
			.replace("@MPBSTS1", "A" )
			.replace("@MPBSTS2", "A" ) ;
			
			LogCajaService.CreateLog("actualizarRegistroCuota", "QRY", strSql);
			
			int rowsafected = sesion.createSQLQuery(strSql).executeUpdate();
			
			if(rowsafected == 0 ){
				return strMensaje = "No han afectado registros de cuota en la actualizacion para  "  + strSqlWhere ; 
			}
			if(rowsafected > 1 ){
				return strMensaje = "en la actualizacion se han afectado mas de una cuota para  "  + strSqlWhere ; 
			}
			
			//&& ================== Actualizar SOTMPIDA.
			strSql = "update @QS36F.SOTMPIDA set " +
					
					" IDANCUOTAAPL = (IDANCUOTAAPL + 1) ," +
					" IDAVCUOTAAPL = (IDAVCUOTAAPL + @MONTOCUOTAAPLICADA )," +
					" IDAVSALDO  = ( IDATTDB - ( IDAVCUOTAAPL + @MONTOCUOTAAPLICADA )  ), " +
					" IDAHECAM = '@IDAHECAM'," +
					" IDAFECAM = '@IDAFECAM', " +
					" IDAHRCAM = '@IDAHRCAM', " +
					" IDAPACAM = '@IDAPCAM', " +
					" IDAPRCAM = '@IDAPRCAM', " +
					" IDAFUCAPL = '@IDAFECAM', " + 
					" IDAHUCAPL = '@IDAHRCAM', " +
					" IDANUCAPL = '@IDANUCAPL', " +
					" IDAVUCAPL = '@MONTOCUOTAAPLICADA', " +
					" IDAOUCAPL = '@IDAOUCAPL'  " +
					
					" where trim(IDACIA) = '@IDACIA' and trim(IDASUC) = '@IDASUC'  " +
					" and IDANCTTO = @IDANCTTO and IDACLI = @IDACLI  and trim(IDACHAS) = '@IDACHAS' " +
					" and IDANTRJ = @IDANTRJ and IDAMON = '@IDAMON' AND IDASTSTRJ = 'A' " ;
					 
			strSql = strSql
				.replace("@QS36F", PropertiesSystem.QS36F)
				.replace("@IDACIA", v.getMpbcia().trim() )
				.replace("@IDASUC", v.getMpbsuc().trim()  )
				.replace("@IDANCTTO", v.getMpbnctto().toString()  )
				.replace("@IDACLI",  v.getMpbcli().toString() )
				.replace("@IDACHAS", v.getMpbchas().trim() )
				.replace("@IDANTRJ", v.getMpbntrj().toString() )
				.replace("@IDAMON",  v.getMpbmon() )
				.replace("@MONTOCUOTAAPLICADA",  v.getMpbvpag().toString() )
				.replace("@IDAHECAM", v.getMpbhecam() )
				.replace("@IDAFECAM", new SimpleDateFormat("yyyy-MM-dd").format( v.getMpbfecam() ) )
				.replace("@IDAHRCAM", new SimpleDateFormat("HH:mm:ss").format( v.getMpbhrcam() ) )
				.replace("@IDAPCAM",  v.getMpbpacam() ) 
				.replace("@IDAPRCAM", v.getMpbprcam()  )
				
				.replace("@IDANUCAPL", Integer.toString(v.getMpbnpag().intValue() ) )
				.replace("@IDAVUCAPL",  v.getMpbvpag().toString()  )
				.replace("@IDAOUCAPL", Integer.toString( v.getNumerobatchpago() )  )
				;
			
			LogCajaService.CreateLog("actualizarRegistroCuota", "QRY", strSql);
			
			rowsafected = sesion.createSQLQuery(strSql).executeUpdate();
			
			if(rowsafected > 1 ){
				return strMensaje = "Mas de un registro afectado en la actualizacion para historico de cuotas SOTMPIDA " ; 
			}
			
			//&& ================== Historico de incidencias
			rowsafected = crearIncidenciaPagosCuotas(sesion, v, "00");
			
			
		} catch (Exception e) {
			strMensaje = "el registro no ha podido ser actualizado ";
			LogCajaService.CreateLog("actualizarRegistroCuota", "ERR", e.getMessage());
			e.printStackTrace();
		}
		return strMensaje;
	}
	
	public static int crearIncidenciaPagosCuotas(Session sesion, Vwbitacoracobrospmt v, String codigoIncidencia){
		int rowsafected = 0;
		String tipoIncidencia = "COBRO";
		
		try {
			
			if(codigoIncidencia.compareTo("00") == 0){
				tipoIncidencia = "COBRO";
			}
			if(codigoIncidencia.compareTo("01") == 0){
				tipoIncidencia = "ERROR";
			}
			
			String strSql = "insert into @QS36F.sotmpi " +
			"( MPICIA, MPISUC, MPINCTTO, MPICLI, MPICHAS , MPICINC , MPIDINC , MPISEC, MPIINC, MPIOAD, MPIHEREG, MPIFEREG, MPIHOREG, MPIPAREG, MPIPRREG  )" +
			" values " +
			"( '@MPICIA', '@MPISUC', @MPINCTTO, @MPICLI, '@MPICHAS', '@MPICINC', '@MPIDINC', @MPISEC, '@MPIINC', '@MPIOAD', '@MPIHEREG', '@MPIFEREG', '@MPIHOREG', '@MPIPAREG', '@MPIPRREG' )" ;
			
			strSql = strSql 
			.replace("@QS36F"   , PropertiesSystem.QS36F )        
			.replace("@MPICIA"   , v.getMpbcia() )        
			.replace("@MPISUC"   , v.getMpbsuc())        
			.replace("@MPINCTTO" , Integer.toString( v.getMpbnctto() ) )        
			.replace("@MPICLI"   , Integer.toString( v.getMpbcli()  ) )        
			.replace("@MPICHAS"  , v.getMpbchas() )        
			.replace("@MPICINC"  , codigoIncidencia )        
			.replace("@MPIDINC"  , tipoIncidencia)        
			.replace("@MPISEC"   , "0" )        
			.replace("@MPIINC"   , v.getResponseCode() )        
			.replace("@MPIOAD"   , v.getResponseCodeDescription().trim().length()>39 ? v.getResponseCodeDescription().trim().substring(0, 39) : v.getResponseCodeDescription().trim() )        
			.replace("@MPIHEREG" , v.getMpbhecam())        
			
			.replace("@MPIFEREG" , new SimpleDateFormat("yyyy-MM-dd").format( new Date() ) )        
			.replace("@MPIHOREG" , new SimpleDateFormat("HH:mm:ss").format( new Date() ))    
			
			.replace("@MPIPAREG" , v.getMpbpacam() )        
			.replace("@MPIPRREG" , v.getMpbprcam() ) ;        
			   
			if(sesion == null){				
				String[] query = {strSql};
				ConsolidadoDepositosBcoCtrl.executeSqlQueries(Arrays.asList(query));
				rowsafected = ConsolidadoDepositosBcoCtrl.getAffectedRowsOnExecute();
				
			}else{
				rowsafected = sesion.createSQLQuery(strSql).executeUpdate();
			}
			
		} catch (Exception e) {
			e.printStackTrace(); 
		} 
		
		
		return rowsafected;
	}
	
	
	public static List<ResumenCobroAutomatico> generarResumenTransaccionesProcesar(int caid){
		 List<ResumenCobroAutomatico> lstresumen = null;
		 
		try {
			
			String sqlCompaniaxCaja = " ( select distinct(trim( c4rp01 ) ) from @BDCAJA.F55CA014 where c4id = @CAID ) "; 
			
			String strSql =  
			" select codcomp, nomcomp, mpbmon " +
			
		",  ifnull( " +		
			"  (select (select term_id  from @BDCAJA.term_afl where cast(card_acq_id as integer) = cxcafi and status = 'A'fetch first rows only ) " +
			"  	from @BDCAJA.cafiliados c where c6id = @CAID and c6rp01 = v.codcomp  and d7crcd =  v.mpbmon fetch first rows only  " +
			"  ), '0' ) terminal " +

//			", 'ABC' terminal" +

			", ifnull( " +	
			"  (select  cxcafi  from @BDCAJA.cafiliados c where c6id = @CAID and c6rp01 = v.codcomp  and d7crcd =  v.mpbmon fetch first rows only " +
			"  ) , '0' ) afiliado" +
			
			", ifnull( " +	
			" (select  lower(cxdcafi ) from @BDCAJA.cafiliados c where c6id = @CAID  and c6rp01 = v.codcomp  and d7crcd =  v.mpbmon fetch first rows only  " +
			" ), '' ) nombreafiliado " +
			
			", sum(mpbvpag)" +
			", count(*)  " +
			" from @BDCAJA.Vwbitacoracobrospmt v" +
			" where MPBSTS1 = '' and trim(codcomp) in  " + sqlCompaniaxCaja +
			" group by  codcomp, nomcomp, mpbmon " ;
             
			strSql = strSql
					.replace("@BDCAJA", PropertiesSystem.ESQUEMA)
					.replace("@CAID", String.valueOf(caid));
			
			@SuppressWarnings("unchecked")
			List<Object[]> lstdtaResumen = (ArrayList<Object[]>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSql, true, null);
			
			if(lstdtaResumen == null)
				return null;
			
			lstresumen = new ArrayList<ResumenCobroAutomatico>();
			for (Object[] dta : lstdtaResumen) {
				lstresumen.add( 
				new ResumenCobroAutomatico(
						String.valueOf(dta[0]),
						String.valueOf(dta[1]),
						String.valueOf(dta[2]),
						String.valueOf(dta[3]),
						String.valueOf(dta[4]),
						String.valueOf(dta[5]),
						(BigDecimal) dta[6],
						( Integer) dta[7],
						0,0,
						caid,
						String.valueOf(dta[4])
						)
				);
			}
			
		} catch (Exception e) {
			lstresumen = null;
			e.printStackTrace(); 
		} 
		return lstresumen;
				
	}
	
	@SuppressWarnings("unchecked")
	public static List<Vwbitacoracobrospmt> cuotasPendientesCobro() {
		List<Vwbitacoracobrospmt> cuotasPendientes = null;

		try {

			String strlSql = " select * from "
					+ PropertiesSystem.ESQUEMA
					+ ".Vwbitacoracobrospmt where MPBSTS1 = '' order by mpbfcgnr, mpbfpag,  MPBCLI ";

			return cuotasPendientes = (ArrayList<Vwbitacoracobrospmt>) ConsolidadoDepositosBcoCtrl
					.executeSqlQuery(strlSql, true, Vwbitacoracobrospmt.class);

		} catch (Exception e) {
			cuotasPendientes = null;
			e.printStackTrace(); 
		}
		return cuotasPendientes;
	}
	@SuppressWarnings("unchecked")
	public static List<Vwbitacoracobrospmt> cuotasPendientesCobroCaja(int caid) {
		List<Vwbitacoracobrospmt> cuotasPendientes = new ArrayList<Vwbitacoracobrospmt>();

		try {			
			String sqlCompaniaxCaja = " ( select distinct(trim( cast( c4rp01 as varchar(3) ccsid 37 ) )) from " + PropertiesSystem.ESQUEMA +".F55CA014 where c4id = " + caid +" ) "; 
			
			String strlSql = " select * from "
					+ PropertiesSystem.ESQUEMA
					+ ".Vwbitacoracobrospmt where MPBSTS1 = '' " +
					" and trim(codcomp) in  " + sqlCompaniaxCaja +
			"	order by codcomp, mpbfcgnr desc, mpbfpag,  MPBCLI   ";
			
			LogCajaService.CreateLog("cuotasPendientesCobroCaja", "QRY", strlSql);
			
			cuotasPendientes = (ArrayList<Vwbitacoracobrospmt>) ConsolidadoDepositosBcoCtrl
					.executeSqlQuery(strlSql, true, Vwbitacoracobrospmt.class);

			return cuotasPendientes;
			
		} catch (Exception e) {
			LogCajaService.CreateLog("cuotasPendientesCobroCaja", "ERR", e.getMessage());
			cuotasPendientes = null;
			e.printStackTrace(); 
		}
		return cuotasPendientes;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Vwbitacoracobrospmt> cuotasPendientesPorClienteContrato(int numeroContrato, int codigocliente, Date fechaActual){
		List<Vwbitacoracobrospmt> cuotasPendientes = null;
		
		try {
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(fechaActual);
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH) );
			
			String fecha = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime() ) ;
			
			String strlSql = 
					" select * from @BDCAJA.Vwbitacoracobrospmt " +
					" where MPBSTS1 = '' and mpbcli = @CODCLI and mpbnctto = @NOCONTRATO " +
					//" and mpbfcgnr <= '@FECHACUOTA' " +
					" order by mpbfcgnr  ";
			
			strlSql = strlSql 
					.replace("@BDCAJA", PropertiesSystem.ESQUEMA )
					.replace("@CODCLI", String.valueOf(codigocliente) )
					.replace("@NOCONTRATO", String.valueOf(numeroContrato) );
//					.replace("@FECHACUOTA", fecha) ;
			System.out.println(strlSql);
			return cuotasPendientes = (ArrayList<Vwbitacoracobrospmt>) ConsolidadoDepositosBcoCtrl
					.executeSqlQuery(strlSql, true, Vwbitacoracobrospmt.class);
			
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		
		return cuotasPendientes;
		
	}
	
	public static Vwbitacoracobrospmt informacionDeCuota(int numeroContrato, int codigocliente, int numerocuota){
		 Vwbitacoracobrospmt v = null;
		
		try {
			
			String strlSql = 
					" select * from @BDCAJA.Vwbitacoracobrospmt " +
					" where mpbcli = @CODCLI and mpbnctto = @NOCONTRATO " +
					" and mpbnpag = @NUMEROCUOTA   "  ;
			
			strlSql = strlSql 
					.replace("@BDCAJA", PropertiesSystem.ESQUEMA )
					.replace("@CODCLI", String.valueOf(codigocliente) )
					.replace("@NOCONTRATO", String.valueOf(numeroContrato) )
					.replace("@NUMEROCUOTA", String.valueOf(numerocuota) ) ;
			
			List<Vwbitacoracobrospmt> cuotas = (ArrayList<Vwbitacoracobrospmt>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(strlSql, true, Vwbitacoracobrospmt.class);
			if(cuotas == null  ||  cuotas.isEmpty() )
				return null;
			
			return v =  cuotas.get(0);
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		
		return v;
		
	}
	
}

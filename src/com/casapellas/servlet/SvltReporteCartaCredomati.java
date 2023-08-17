package com.casapellas.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.mail.internet.InternetAddress;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.casapellas.entidades.Vsolecheque;
import com.casapellas.reportes.CartaCredomaticPdf;
import com.casapellas.reportes.Rptmcaja008;
import com.casapellas.util.CustomEmailAddress;
import com.casapellas.util.Divisas;
import com.casapellas.util.MailHelper;
import com.casapellas.util.NumeroEnLetras;
import com.casapellas.util.PropertiesSystem;
import com.ibm.icu.util.Calendar;

/**
 * Servlet implementation class SvltReporteCartaCredomati
 */
public class SvltReporteCartaCredomati extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    public SvltReporteCartaCredomati() {
        super();
    }
    
    protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
    	PrintWriter out = response.getWriter();
		response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Cache-Control", "no-store");
	    response.setHeader("Cache-Control", "must-revalidate");
	    response.setHeader("Pragma", "no-cache");
	    response.setDateHeader("Expires", 0); 
    
		String sNombreTo;
		String sNombreFrom ;
		String sNombreClie;
		String iNoTarjeta;
		String moneda; 
		String strNombreDelPDF;
		Date dtFechaFactura ;
		Date dtFechaDevolucion;
		int iautorizado;
		int iCodigoPOS;
		int iNoFactura;
		int iNodevolucion;
		BigDecimal bdMonto;
		
		try {
			
			
			Divisas dv = new Divisas();
			Vsolecheque v = (Vsolecheque)request.getSession().getAttribute("rsc_solicitudSeleccionada");
			sNombreTo    = String.valueOf(request.getSession().getAttribute("rsc_DestinoCarta"));
			sNombreFrom  = String.valueOf(request.getSession().getAttribute("rsc_OrigenCarta"));	
			sNombreClie  = v.getId().getCliente();
			
			List<String[]>cuentasEnvios = (ArrayList<String[]>) request.getSession().getAttribute("rsc_CuentasCorreo") ;
			
			sNombreTo =   dv.ponerCadenaenMayuscula(sNombreTo);
			sNombreFrom = dv.ponerCadenaenMayuscula(sNombreFrom);
			sNombreClie = dv.ponerCadenaenMayuscula(sNombreClie);
			
			dtFechaFactura = v.getId().getFechafac();
			dtFechaDevolucion = v.getId().getFechadev();
			iautorizado =  (v.getId().getCodautoriz().trim().compareTo("") == 0)?
							0: Integer.parseInt(v.getId().getCodautoriz().trim());
			iCodigoPOS  = Integer.parseInt(v.getId().getIdafiliado().trim());
			iNoFactura     = v.getId().getNofactoriginal();
			iNodevolucion  = v.getId().getNumfac();
			iNoTarjeta     = "******"+v.getId().getNotarjeta().trim();
			moneda         = v.getId().getMoneda();
			bdMonto        = v.getId().getMonto();
			
			
			int iSufijo = Integer.parseInt( (int)Math.round(Math.random() * 100 ) +""+(int)Math.round(Math.random() * 10 ) );
			
			@SuppressWarnings("deprecation")
			String sRutaCarpeta = request.getRealPath(File.separatorChar+"Confirmacion"+File.separatorChar);
			
			strNombreDelPDF = sRutaCarpeta+"" + iSufijo + "_" +"ReversionPagoTC_"
								+v.getId().getNotarjeta()+".pdf"; 
			
			CartaCredomaticPdf carta = new CartaCredomaticPdf(sNombreTo, 
					sNombreFrom, sNombreClie,
					dtFechaFactura, dtFechaDevolucion, 
					iCodigoPOS, iNoFactura, iNoTarjeta, 
					bdMonto, iautorizado, moneda, strNombreDelPDF, iNodevolucion);
			carta.crearCarta1();
			
			if(cuentasEnvios != null && !cuentasEnvios.isEmpty() ){
				enviarCorreoCarta( cuentasEnvios, strNombreDelPDF );
			}
			
			
			String sRutaWeb = request.getContextPath()+"/Confirmacion/"
						+iSufijo + "_" +"ReversionPagoTC_"
						+v.getId().getNotarjeta()+".pdf";
			
			out.println(sRutaWeb);
			
			System.out.println("Ruta web " + sRutaWeb);
			System.out.println("Ruta Fisica: "+ strNombreDelPDF);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    

    public void enviarCorreoCarta(List<String[]>cuentasEnvios, String rutaFisica ){
    	try {
    		File pdfcarta = new File(rutaFisica);
    		
    		List<CustomEmailAddress> toList = new ArrayList<CustomEmailAddress>();
    		for (String[] sCtaCorreo : cuentasEnvios) {
    			toList.add(new CustomEmailAddress(sCtaCorreo[0], sCtaCorreo[1]));
			}
    		
    		MailHelper.SendHtmlEmail(
					new CustomEmailAddress(PropertiesSystem.WEBMASTER_EMAIL_ADRESS, "Módulo de Caja"),
					toList, null, null, 
					"Carta Solicitud Reversión Credomatic por devolución de contado", "", new String[] { pdfcarta.getAbsolutePath() });
    		   		
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    
    @Override
	protected void doGet(HttpServletRequest request,  HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
	@Override
	protected void doPost(HttpServletRequest request,
					HttpServletResponse restponse) throws ServletException, IOException {
			processRequest(request, restponse);
	}
	@Override
	public String getServletInfo() {
		return "Short description";
	}

}

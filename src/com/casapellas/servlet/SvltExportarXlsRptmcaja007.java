package com.casapellas.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.casapellas.entidades.Vreporterecibos;
import com.casapellas.entidades.pmt.Vwbitacoracobrospmt;
import com.casapellas.reportes.Rptmcaja014_RecibosCaja;
import com.casapellas.reportes.Rptmcaja015_CuotasPMT;
import com.casapellas.util.PropertiesSystem;

/**
 * Servlet implementation class SvltExportarXlsRptmcaja007
 */
public class SvltExportarXlsRptmcaja007 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@SuppressWarnings("unchecked")
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = null;

		try {
			out = response.getWriter();
			response.setContentType("text/html;charset=UTF-8");
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Cache-Control", "no-store");
			response.setHeader("Cache-Control", "must-revalidate");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
			
			String relativepath = request.getContextPath() + "/" + PropertiesSystem.CARPETA_DOCUMENTOS_EXPORTAR  + "/" ;
			String absolutePath = request.getServletContext().getRealPath( PropertiesSystem.CARPETA_DOCUMENTOS_EXPORTAR );
			String nombreArchivo = "RecibosCaja_"+new SimpleDateFormat("ddMMyyyyHHmmss").format( new Date() )+".xlsx" ;
			
			
			int tipodocumento = Integer.parseInt( request.getParameter("tipoDocumento") );
			
			// && ================== Descargar el reporte de cuotas pendientes de debitos automaticos 
			if(tipodocumento == 10){
				
				List<Vwbitacoracobrospmt> lstCuotasPendientesDebitos =  (ArrayList<Vwbitacoracobrospmt>) request.getSession().getAttribute("debatm_lstCuotasPendientesDebitos");
				
				if(lstCuotasPendientesDebitos == null || lstCuotasPendientesDebitos .isEmpty() ){
					out.println("");
					return;
				}
				
				String nombreDoc = "DebitosAutomaticos_"+new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date())+".xlsx";
				
				Rptmcaja015_CuotasPMT r = new  Rptmcaja015_CuotasPMT();
				r.mensajeProceso = "" ;
				r.cuotasPMT = lstCuotasPendientesDebitos;
				r.rutafisica = absolutePath + File.separatorChar  + nombreDoc;
				r.tituloReporte = "Débitos Automáticos Generados  " ;
				r.titulo2Reporte = "Cuotas pendientes de Cobro" ;
				r.crearExcelTransaccionesCuotas();
				
				if( r.mensajeProceso != null && !r.mensajeProceso.trim().isEmpty() ){
					out.println("");
					return;
				}
				
				out.print( relativepath +  nombreDoc );
				return ;
				
			}
			
			
			List<Vreporterecibos> recibos = null; 
			
			if(tipodocumento == 1) {
				recibos	= (List<Vreporterecibos>)  request.getSession().getAttribute("rrc_lstConsultaReporteRecibos");
			}
			if(tipodocumento == 2) {
				recibos	= (List<Vreporterecibos>)  request.getSession().getAttribute("rrc_lstConsultaReporteRecibos");
			}
			if(tipodocumento == 3) {
				recibos	= (List<Vreporterecibos>)  request.getSession().getAttribute("rrc_lstConsultaReporteRecibos");
			}
			
			if(recibos == null || recibos.isEmpty() ){
				out.println("");
				return;
			}
			
			String sRutaFisica = absolutePath + File.separatorChar  + nombreArchivo ;
			
			Rptmcaja014_RecibosCaja rpt = new Rptmcaja014_RecibosCaja();
			rpt.agregarfirmaContador = false;
			rpt.recibosCaja = recibos;
			rpt.rutafisica = sRutaFisica ; //absolutePath + File.separatorChar  + nombreArchivo ;
			
			if(tipodocumento == 3) {
				rpt.reporteRecibosSinFormato();
			}else{
				rpt.generarReporteRecibos();
			}
			
			
			File reporte = new File ( sRutaFisica );
			Long size = reporte.length();
			
			int retry = 0;
			while( size == 0 || retry > 2  ){
				reporte = null;
				
//				LogCrtl.imprimirError("Generacion proxima de reporte debitos automaticos por crearse con tamanio cero > " + nombreArchivo);
				
				int delay  = Integer.parseInt( new Random().nextInt(2) + ""+ new Random().nextInt(1000) ); 
				Thread.currentThread();
				Thread.sleep(delay);
				
				retry ++ ;
				
				nombreArchivo = "DebitosAutomaticos_"+new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date())+".xlsx";
				sRutaFisica = absolutePath + File.separatorChar  + nombreArchivo ;
				sRutaFisica = PropertiesSystem.RUTA_DOCUMENTOS_EXPORTAR + nombreArchivo;
				
				rpt = new Rptmcaja014_RecibosCaja();
				rpt.agregarfirmaContador = false;
				rpt.recibosCaja = recibos;
				rpt.rutafisica = sRutaFisica ; 
				rpt.reporteRecibosSinFormato();
				
				reporte = new File (sRutaFisica);
				size = reporte.length();
				
			}
			
			if( size == 0 && retry > 2  ){
//				LogCrtl.imprimirError("No se pudo crear el reporte de debitos, archivo siempre generado con tamanio cero ");
				return;
			}
			
			
			/*
			File doc = new File ( "C:\\GCPMCAJA\\filestoexport\\DebitosAutomaticos_08062017_073345.xlsx");// rpt.rutafisica );
			if( doc.length() == 0){
				System.out.println("Zero");
				
				int delay  = Integer.parseInt( new Random().nextInt(3) + ""+ new Random().nextInt(1000) ); 
				Thread.currentThread();
				Thread.sleep(delay);
				
				rpt = new Rptmcaja014_RecibosCaja();
				rpt.agregarfirmaContador = false;
				rpt.recibosCaja = recibos;
				
				nombreArchivo = "RecibosCaja_"+new SimpleDateFormat("ddMMyyyyHHmmss").format( new Date() )+".xlsx" ;
				rpt.rutafisica =  "C:\\GCPMCAJA\\filestoexport\\"  + nombreArchivo ;// absolutePath + File.separatorChar  + nombreArchivo ;
				rpt.reporteRecibosSinFormato();
				
			}
			*/
			

			if( rpt.getMensajeProceso() != null && !rpt.getMensajeProceso().trim().isEmpty() ){
				out.println("");
				return;
			}
			
			out.print( relativepath +  nombreArchivo );
			

		} catch (Exception e) {
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
//			out.println("");
		} finally {
			out.close();
		}
	}
	
    public SvltExportarXlsRptmcaja007() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

}

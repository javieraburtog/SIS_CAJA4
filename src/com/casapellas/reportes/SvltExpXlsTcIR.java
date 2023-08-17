package com.casapellas.reportes;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.casapellas.entidades.Vdeposito;

/**
 * Servlet implementation class SvltExpXlsTcIR
 */
public class SvltExpXlsTcIR extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings({ "unchecked", "deprecation" })
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	PrintWriter out = null;
    	
	    try {
			response.setContentType("text/html;charset=UTF-8");
	        response.setHeader("Cache-Control", "no-cache");
	        response.setHeader("Cache-Control", "no-store");
		    response.setHeader("Cache-Control", "must-revalidate");
		    response.setHeader("Pragma", "no-cache");
		    response.setDateHeader("Expires", 0); 
		    out = response.getWriter();
		    
		    HttpSession ses = request.getSession();	
		    ArrayList<ReporteRetencionIR> lstDtaXls 
		    		= (ArrayList<ReporteRetencionIR>)
		    			ses.getAttribute("rpt10_lstTransacTcIrs");
		    String sNombreComp = String.valueOf(
		    						ses.getAttribute("rpt10_Compania"));
//		    String sFechas =  String.valueOf(
//										ses.getAttribute("rpt10_Fechas")); //&& cadena con valor 'ddmmyy_ddmmyy'
		    
			int iSufijo = Integer.parseInt((int)Math.round(Math.random()
							* 100 ) +""+(int)Math.round(Math.random() * 10 ) );
//			String sNombre = iSufijo+"TransaccionesTcredito_"+sFechas+".xlsx";
			String sNombre = iSufijo+"_TransaccionesTcredito.xlsx";
			
			String sRutaFisica = request.getRealPath(File.separatorChar	
								+ "Confirmacion") +  File.separatorChar + sNombre;
			
		    
		    if(lstDtaXls == null || lstDtaXls.isEmpty()){
		    	out.println("");
		    	return;
		    }
		    
			Rptmcaja010Xls xls = new Rptmcaja010Xls(lstDtaXls, sNombreComp, sRutaFisica );
			boolean bHecho =  xls.crearXlsTransaccionesIR();
		    
			if(!bHecho) {
				out.println("");
				return;
			}
			String sRutaDescarga = request.getContextPath()
								+"/Confirmacion/"+ sNombre;
			out.println(sRutaDescarga);
			
			
	    }catch (Exception e) {
	    	out.println("");
		}
    }	
	
	//&& ===================================================================================================//
	//&& ===================================================================================================//
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
	public SvltExpXlsTcIR() {
        super();
    }
}

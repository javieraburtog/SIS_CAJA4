package com.casapellas.conciliacion;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class SvltExpPdfMinutaDp
 */
public class SvltExpPdfMinutaDp extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SvltExpPdfMinutaDp() {
        super();
    }
    @SuppressWarnings({ "deprecation" })
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
		    
		    if(ses.getAttribute("ac_RutaMinutaPDF") == null){
		    	out.println("@No pudo acceder al recurso: Ubicación " +
		    			"relativa inválida para archivo");
		    	return;
		    }
		    
		    String sNombAr =  String.valueOf(ses.getAttribute("ac_RutaMinutaPDF"));
		    String sFolder = sNombAr.split("@")[0];
		    String sNombre = sNombAr.split("@")[1];
		    
		    String sRutaDsc = request.getContextPath() 
		    					+"/"+sFolder+"/"+sNombre;
		    
		    File minuta = new File( request.getRealPath(File.separatorChar
		    					+ sFolder + File.separatorChar + sNombre));
		    
		    if(!minuta.exists()){
		    	out.println("@No pudo acceder al recurso: El archivo " +
    							"especificado no existe");

		    	//TODO consultar el blob y generar de nuevo el documento.
		    	
		    	return;
		    }
		    
		    out.println(sRutaDsc);
		    
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
}

package com.casapellas.conciliacion.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/SvltConsolidadoDepositosOpcn")
public class SvltConsolidadoDepositosOpcn extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	
	public void refrescarConsolidados(){
		try {
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		
		try {
			
			response.setContentType("text/html;charset=UTF-8");
	        response.setHeader("Cache-Control", "no-cache");
	        response.setHeader("Cache-Control", "no-store");
		    response.setHeader("Cache-Control", "must-revalidate");
		    response.setHeader("Pragma", "no-cache");
		    response.setDateHeader("Expires", 0); 
		    
		    String codigoaplicacion = request.getParameter("opcion");
		    
		    if(codigoaplicacion.compareTo("1") == 0){
		    	
		    }
		    
		    
		    
		} catch (Exception e) {
			e.printStackTrace();
		}
		 out.println("");
		return;
        
	}
	
    public SvltConsolidadoDepositosOpcn() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
}

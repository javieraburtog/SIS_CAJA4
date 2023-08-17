package com.casapellas.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;

import com.casapellas.entidades.Vsolecheque;
import com.casapellas.socketpos.TransaccionTerminal;

/**
 * Servlet implementation class SvltReporteCierreSocketPos
 */
@WebServlet("/SvltReporteCierreSocketPos")
public class SvltReporteCierreSocketPos extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
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
		    
		    
		    TransaccionTerminal terminal = (TransaccionTerminal) request
					.getSession().getAttribute("ac_TerminalSeleccionada");
			int iSufijo = Integer.parseInt(
					(int) Math.round(Math.random() * 100) + ""
					+ (int) Math.round(Math.random() * 10));
			
			@SuppressWarnings("deprecation")
			String absolutePath = request.getRealPath(File.separatorChar
					+ "Confirmacion" + File.separatorChar);

			String filename = terminal.getNombrereporte();
			
			String relativepath = request.getContextPath() + "/Confirmacion/" + filename;
		    
			FileOutputStream fos = new FileOutputStream(absolutePath + filename);
			fos.write(Base64.decodeBase64(  terminal.getDtaReporteCierre()   ));
			fos.close();
			
			if( new File(absolutePath + filename).exists() )
				out.println(relativepath);
		    
		} catch (Exception e) {
			e.printStackTrace();
			 out.println("");
		}
	}
	
	
	public SvltReporteCierreSocketPos() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

}

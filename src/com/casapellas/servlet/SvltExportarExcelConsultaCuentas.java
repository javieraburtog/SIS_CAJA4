package com.casapellas.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.casapellas.conciliacion.entidades.Vf0911;
import com.casapellas.reportes.Rptmcaja013XlsCuentaTransitoria;

/**
 * Servlet implementation class SvltExportarExcelConsultaCuentas
 */
public class SvltExportarExcelConsultaCuentas extends HttpServlet {
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
			
			String nombreArchivo = "ConsultaCuenta_"+request.hashCode()+".xlsx";
			String relativepath = request.getContextPath() + "/Confirmacion/"  ;
			String absolutePath = request.getServletContext().getRealPath("/Confirmacion/");
			
			@SuppressWarnings("unchecked")
			List<Vf0911>lstTransaccionesF0911 = (ArrayList<Vf0911>)request.getSession().getAttribute("cct_lstTransaccionesF0911_Todas");
			
			Rptmcaja013XlsCuentaTransitoria rpt = new Rptmcaja013XlsCuentaTransitoria();
			rpt.transacciones_cuenta = lstTransaccionesF0911;
			rpt.rutafisica = absolutePath + nombreArchivo;
			boolean generado = rpt.generarExcelTransaccionesCuenta();
			
			if(!generado){
				out.println("");
				return;
			}
			
			out.print( relativepath +  nombreArchivo );
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
//			out.println("");
		}finally{
			out.close();
		}
	}
  
    public SvltExportarExcelConsultaCuentas() {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
}

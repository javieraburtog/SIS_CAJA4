package com.casapellas.conciliacion;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.casapellas.controles.EmpleadoCtrl;
import com.casapellas.conciliacion.entidades.Depbancodet;
import com.casapellas.entidades.Vdeposito;
import com.casapellas.entidades.Vf0101;
import com.casapellas.entidades.ens.Vautoriz;

/**
 * Servlet implementation class SvltExpXlsDepsNc
 */
public class SvltExpXlsDepsNc extends HttpServlet {
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
		    
		    if(ses.getAttribute("cdb_ProcesoCnfAuto") == null){
		    	out.println("@Primero ejecute el proceso de comparación de depósitos");
		    	return;
		    }
		    
		    ArrayList<Vdeposito>lstDepsCaja = (ArrayList<Vdeposito>)ses.getAttribute("cdb_DepsCajaNc");
		    ArrayList<Depbancodet>lstDepsBco = (ArrayList<Depbancodet>)ses.getAttribute("cdb_DepsBancoNc");
		    
		    if(lstDepsCaja == null && lstDepsBco == null){
		    	out.println("");
		    	return;
		    }
		    
		    Vautoriz vaut = ((Vautoriz[])ses.getAttribute("sevAut"))[0];
		    Vf0101 vf01 = new EmpleadoCtrl().buscarEmpleadoxCodigo2(vaut.getId().getCodreg());
		    
		    String sRutaDescrg = request.getContextPath()+"/Confirmacion/";
		    String sRutaFisica = request.getRealPath(File.separatorChar+"Confirmacion");
		    XlsrptDpsCaja rpt = new XlsrptDpsCaja();
		   
		    String sNombreArchivo = rpt.generarExcelDepsCaja(lstDepsCaja,lstDepsBco, "Confirmación automática",
					    					vf01.getId().getAbalph().trim(),
					    					new Date(), sRutaFisica);
		    
		    sRutaDescrg = (sNombreArchivo.compareTo("") == 0 )? "" 
		    					: sRutaDescrg + sNombreArchivo;
			
		    out.println(sRutaDescrg);
		    
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
    public SvltExpXlsDepsNc() {
        super();
    }


}

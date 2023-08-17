package com.casapellas.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;

import com.casapellas.controles.RevisionArqueoCtrl;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.PropertiesSystem;

/**
 * Servlet implementation class SvltExportarPropuestaAjustePDF
 */
public class SvltExportarPropuestaAjustePDF extends HttpServlet {
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
			
			int idPropuestaAjuste = Integer.parseInt( request.getParameter("idajusteexcepcion") );
			
			String b64strfile  =  RevisionArqueoCtrl.getBase64StringFromFile(idPropuestaAjuste, 74, String.valueOf( idPropuestaAjuste) );
			
			String strFechaDoc =   "_"+ new SimpleDateFormat("ddMMyyy").format(new Date()) ;
			
			String nombredoc = "Propuesta_Ajuste_Excepcion_" + CodeUtil.pad(Long.toString( idPropuestaAjuste ) , 3, "0")+strFechaDoc+".pdf" ;
			String relativepath = request.getContextPath() + "/"+PropertiesSystem.CARPETA_DOCUMENTOS_EXPORTAR+"/"  ;
			String absolutePath = request.getServletContext().getRealPath("/"+PropertiesSystem.CARPETA_DOCUMENTOS_EXPORTAR+"/");
			
			FileOutputStream fos = new FileOutputStream(absolutePath + nombredoc);
			fos.write(Base64.decodeBase64(  b64strfile.getBytes("UTF-8") ));
			fos.close();
			
			if( new File(absolutePath + nombredoc).exists() )
				out.println(relativepath + nombredoc);
			
		} catch (Exception e) {
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
			out.println("");
		}finally{
			out.close();
		}
	}
	
    public SvltExportarPropuestaAjustePDF() {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}


 
}

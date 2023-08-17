package com.casapellas.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.casapellas.controles.RevisionArqueoCtrl;
import com.casapellas.entidades.Arqueo;
import com.casapellas.entidades.CierreSpos;
import com.casapellas.entidades.Minsemitidas;
import com.casapellas.entidades.Varqueo;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.PropertiesSystem;

/**
 * Servlet implementation class SvltExportDocumentosCierre
 */
@WebServlet("/SvltExportDocumentosCierre")
public class SvltExportDocumentosCierre extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = null;
		Session sesion = null;
		Transaction trans = null;
		boolean newCn = false;

		try {
			out = response.getWriter();
			response.setContentType("text/html;charset=UTF-8");
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Cache-Control", "no-store");
			response.setHeader("Cache-Control", "must-revalidate");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
			
			@SuppressWarnings("deprecation")
			String absolutePath = request.getRealPath(File.separatorChar
					+ "Confirmacion" + File.separatorChar);
			
			Varqueo va = (Varqueo) request.getSession().getAttribute("rva_ArqueoParaDocumentos");
			
			sesion = HibernateUtilPruebaCn.currentSession();
			trans = (newCn = !(sesion.getTransaction().isActive())) ? sesion
					.beginTransaction() : sesion.getTransaction();
			
			String prefixfiletodelete = va.getId().getCaid() +""+ va.getId().getNoarqueo() +"_";
			String filename = "" ;
			int tipodocumento = Integer.parseInt( request.getParameter("documento") );
			String b64strfile = "";
			
			//&& ========== Descarga minuta de deposito.
			if(tipodocumento == 3){
				
				String sql = " select * from "+PropertiesSystem.ESQUEMA
						+".Minsemitidas where caid = "+ va.getId().getCaid()
						+" and codcomp = '"+va.getId().getCodcomp()+"' and noarqueo = "+ va.getId().getNoarqueo();
				
				Minsemitidas me = (Minsemitidas)sesion.createSQLQuery(sql)
							.addEntity(Minsemitidas.class).uniqueResult();
				
				if(me == null){
					out.println("");
					return;
				}
				
				filename = prefixfiletodelete + me.getNombredoc();
				
				b64strfile =  RevisionArqueoCtrl.getBase64StringFromFile(
									me.getIdemision(), me.getItemtype(), 
									String.valueOf(me.getIdemision()));
			}
			//&& ========== Descarga de arqueo de caja.
			if(tipodocumento == 1){
				
				int idemision = Integer.parseInt( (va.getId().getCaid() +""+ va.getId().getNoarqueo() ) ) ;
				
				String parentrowid = va.getId().getCaid() +"" + va.getId().getNoarqueo() 
						+ FechasUtil.formatDatetoString(va.getId().getFecha(), "ddMMyyyy")
		    			+ FechasUtil.formatDatetoString(va.getId().getHora(), "HHmmss");
				
				b64strfile =  RevisionArqueoCtrl.getBase64StringFromFile(
						idemision, 71, parentrowid);
				
				filename = "Arqueo" + va.getId().getCaid() + "" 
						+ va.getId().getCodcomp().trim() + va.getId().getMoneda() 
						+ "_"+ FechasUtil.formatDatetoString(va.getId().getFecha(), "ddMMyyyyHHmmss")  
						+ "_"+ va.getId().getCodcajero() + ".pdf";
				
				filename = prefixfiletodelete + filename;
				
			}
			//&& ========== Cierre de Socket Pos (descargar todos los cierres socket de ese dia para caja y fecha)
			if(tipodocumento == 2){
			
				String sql = "select * from "+PropertiesSystem.ESQUEMA
						+".arqueo where caid = "+ va.getId().getCaid()
						+" and estado <> 'R' and fecha = '"
						+FechasUtil.formatDatetoString(va.getId().getFecha(), "yyyy-MM-dd")+"' ";
				
				@SuppressWarnings("unchecked")
				List<Arqueo> cierressocket = (ArrayList<Arqueo>) 
						sesion.createSQLQuery(sql).addEntity(Arqueo.class).list();
				
				if(cierressocket == null || cierressocket.isEmpty()){
					out.println("");
					return;
				}
				
				List<String> socketfilesnames = new ArrayList<String>();
				for (int i = 0; i < cierressocket.size(); i++) {
				
					Arqueo me = cierressocket.get(i);
					
					int idemision = Integer.parseInt( (me.getId().getCaid() +""+ me.getId().getNoarqueo() ) ) ;
					String parentrowid = me.getId().getCaid() +"" + me.getId().getNoarqueo() 
							+ FechasUtil.formatDatetoString(me.getId().getFecha(), "ddMMyyyy")
			    			+ FechasUtil.formatDatetoString(me.getId().getHora(), "HHmmss");
					
					b64strfile =  RevisionArqueoCtrl.getBase64StringFromFile( idemision, 71, parentrowid ) ;
					
					if(b64strfile.isEmpty())
						continue;
					
					filename = prefixfiletodelete + ("Arqueo_" + me.getId().getNoarqueo()+"_Caja_"+(me.getId().getCaid()  )+".pdf");
					socketfilesnames.add(filename);
					
					FileOutputStream fos = new FileOutputStream(absolutePath +  filename);
					fos.write(Base64.decodeBase64(  b64strfile.getBytes("UTF-8") ));
					fos.close();
					
					//&& ================ Reporte de recibos de caja.
					b64strfile =  RevisionArqueoCtrl.getBase64StringFromFile( idemision, 74, parentrowid ) ;
					
					if(b64strfile.isEmpty())
						continue;
					
					filename = prefixfiletodelete +  "RecibosCaja_"+me.getId().getCaid()+"_Arqueo_" 
							+ me.getId().getNoarqueo() +"_" 
							+ new SimpleDateFormat("ddMMyyyy").format(me.getId().getFecha())
							+ new SimpleDateFormat("ddMMyyyy").format(me.getId().getHora() ) 
							+".xlsx";
					socketfilesnames.add(filename);
					
					fos = new FileOutputStream(absolutePath +  filename);
					fos.write(Base64.decodeBase64(  b64strfile.getBytes("UTF-8") ));
					fos.close();
					
					
				}
				
				//&& ================ archivos para la minuta de depositos.
				sql = " select * from "+PropertiesSystem.ESQUEMA+".Minsemitidas where caid = " + va.getId().getCaid() +
					  " and fechaemision = '" +FechasUtil.formatDatetoString(va.getId().getFecha(), "yyyy-MM-dd") + "' " ;
				
				@SuppressWarnings("unchecked")
				List<Minsemitidas> minutasCierre = (ArrayList<Minsemitidas>) sesion.createSQLQuery(sql).addEntity(Minsemitidas.class).list();
				
				for (int i = 0; i < minutasCierre.size(); i++) {
					
					Minsemitidas me = minutasCierre.get(i);
					
					b64strfile =  RevisionArqueoCtrl.getBase64StringFromFile( me.getIdemision(), me.getItemtype(), String.valueOf( me.getIdemision() ) );
					
					if(b64strfile.isEmpty())
						continue;
					
					socketfilesnames.add( me.getNombredoc() );
					
					FileOutputStream fos = new FileOutputStream( absolutePath +  me.getNombredoc());
					fos.write( Base64.decodeBase64(  b64strfile.getBytes("UTF-8") ) );
					fos.close();
					
				}
				
				/*
				String sql = "select * from "+PropertiesSystem.ESQUEMA
						+".cierre_spos where caid = "+va.getId().getCaid() 
						+" and date(date_closing) = '"
						+ FechasUtil.formatDatetoString(va.getId().getFecha(), "yyyy-MM-dd") +"'";
				
				@SuppressWarnings("unchecked")
				List<CierreSpos>  cierressocket = (ArrayList<CierreSpos>) 
						sesion.createSQLQuery(sql).addEntity(CierreSpos.class).list();
					
				if(cierressocket == null || cierressocket.isEmpty()){
					out.println("");
					return;
				}		
				
				List<String> socketfilesnames = new ArrayList<String>();
				for (int i = 0; i < cierressocket.size(); i++) {
				
					CierreSpos me = cierressocket.get(i);
					b64strfile = RevisionArqueoCtrl.getBase64StringFromFile( me.getIdcierrespos(), 
							68, String.valueOf(  me.getIdcierrespos() ) );
					
					if(b64strfile.isEmpty())
						continue;
					
					filename = ( prefixfiletodelete + "CierreSocketPos_"+(i+1)+".pdf");
					socketfilesnames.add(filename);
					
					FileOutputStream fos = new FileOutputStream(absolutePath +  filename);
					fos.write(Base64.decodeBase64(  b64strfile.getBytes("UTF-8") ));
					fos.close();
				}
				*/
				
				String socketzipname = prefixfiletodelete + "DocumentosCierresCaja.zip";
				FileOutputStream fos = new FileOutputStream( absolutePath + socketzipname );
				ZipOutputStream zos = new ZipOutputStream(fos);
				for (String name : socketfilesnames) {
					
					File file = new File(absolutePath + name);
					FileInputStream fis = new FileInputStream(file);
					ZipEntry zipEntry = new ZipEntry(name);
					zos.putNextEntry(zipEntry);

					byte[] bytes = new byte[1024];
					int length;
					while ((length = fis.read(bytes)) >= 0) {
						zos.write(bytes, 0, length);
					}
					zos.closeEntry();
					fis.close();
				}
				zos.close();
				fos.close();
				
				String relativepath = request.getContextPath() + "/Confirmacion/" + socketzipname;
				
				if( !new File(absolutePath + socketzipname).exists() )
					relativepath = "";
				
				out.println(relativepath);
				return;
			}
			
			//&& ============================== crear el archivo.	
			
			if(b64strfile.trim().compareTo("") == 0){
				out.println("");
				return;
			}
			
			FileOutputStream fos = new FileOutputStream(absolutePath + filename);
			fos.write(Base64.decodeBase64(  b64strfile.getBytes("UTF-8") ));
			fos.close();
			
			String relativepath = request.getContextPath() + "/Confirmacion/" + filename;
			
			if( new File(absolutePath + filename).exists() )
				out.println(relativepath);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			out.println("");
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
	
    public SvltExportDocumentosCierre() {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
}

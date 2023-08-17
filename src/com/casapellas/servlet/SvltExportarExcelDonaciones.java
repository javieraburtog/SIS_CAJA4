package com.casapellas.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;

import com.casapellas.controles.ConsolidadoDepositosBcoCtrl;
import com.casapellas.donacion.entidades.DncDonacion;
import com.casapellas.donacion.entidades.Vdonacion;
import com.casapellas.reportes.ReporteDonacionesXls;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.PropertiesSystem;

/**
 * Servlet implementation class SvltExportarExcelDonaciones
 */
public class SvltExportarExcelDonaciones extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@SuppressWarnings({ "unchecked", "deprecation" })
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
			
			
			List<DncDonacion>donaciones = (ArrayList<DncDonacion>) request.getSession().getAttribute("dnc_lstMainDonacionesRegistradas");
			
			if(donaciones  == null  || donaciones.isEmpty() ){
				return;
			}
			
			List<Integer>idDonaciones = (ArrayList<Integer>)CodeUtil.selectPropertyListFromEntity(donaciones, "iddonacion", true);
			
			String ids = idDonaciones.toString().replace("[", "(").replace("]", ")");
			String sql = "select * from "+PropertiesSystem.ESQUEMA+".Vdonacion where iddonacion in " + ids ;
					
			List<Vdonacion>lstVdonaciones = (ArrayList<Vdonacion>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, true, Vdonacion.class ); 
			
			if(lstVdonaciones == null || lstVdonaciones.isEmpty() ){
				out.println("");
				return ;
			}
			
			//&& ====================== Ordenar por fecha, beneficiario, forma de pago, moneda
			Collections.sort(lstVdonaciones, new Comparator<Vdonacion>() {

				public int compare(Vdonacion v1, Vdonacion v2) {
					int  equals = 
						(v1.getId().getFecha().before(v2.getId().getFecha())) ? -1 :
						(v1.getId().getFecha().after(v2.getId().getFecha())) ? 1 : 0;
					
					if(equals == 0 ){
						
						equals =
							v1.getId().getBeneficiariocodigo() > v2.getId().getBeneficiariocodigo() ? 1 : 
							v1.getId().getBeneficiariocodigo() < v2.getId().getBeneficiariocodigo() ? -1 : 0; 
						
						 if(equals == 0 ){
							equals = 
							v1.getId().getMpago().compareTo(v2.getId().getMpago() ) > 0 ? 1 : 
							v1.getId().getMpago().compareTo(v2.getId().getMpago() ) < 0 ? -1 : 0 ;
						 }
					}
					return equals;
				}
			});
			
			CollectionUtils.forAllDo(lstVdonaciones, new Closure() {
				public void execute(Object o) {
					Vdonacion dnc = (Vdonacion)o;
					switch ( Integer.parseInt( dnc.getId().getRestado() ) ) {
					case 0:
						dnc.getId().setDescripcionEstado( "Anulada");
						break;
					case 1:
						dnc.getId().setDescripcionEstado("Pendiente");
						break;
					case 2:
						dnc.getId().setDescripcionEstado("Devolución");
						break;
					case 3:
						dnc.getId().setDescripcionEstado("Procesado");
						break;
					default:
						break;
					}
				}
			});
			
			
			
        	String sRutaDescrg = request.getContextPath()+"/Confirmacion/";
        	String sRutaFisica = request.getRealPath(File.separatorChar+"Confirmacion");
			
			
			ReporteDonacionesXls rdx = new ReporteDonacionesXls(lstVdonaciones, sRutaFisica) ;
			String nombrearchivo = rdx.crearExcelDonaciones();
			
			sRutaDescrg = (nombrearchivo.isEmpty() )? "" : sRutaDescrg + nombrearchivo;
        	out.println(sRutaDescrg);
        	
			
		} catch (Exception e) {
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
		}finally{
			out.close();
		}
	}
	
    public SvltExportarExcelDonaciones() {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}


}

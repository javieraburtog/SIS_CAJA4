package com.casapellas.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import com.casapellas.conciliacion.ConsolidadoDepsBcoDAO;
import com.casapellas.conciliacion.entidades.ConsolidadoCoincidente;
import com.casapellas.conciliacion.entidades.PcdConsolidadoDepositosBanco;
import com.casapellas.entidades.Deposito;
import com.casapellas.entidades.Deposito_Report;
import com.casapellas.reportes.Rptmcaja011Xls;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.PropertiesSystem;
 
 
public class SvltReporteConsolidadoDepositos extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unchecked")
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = null;
		boolean hecho = true;
		String nombreDocumento = "" ;
		String relativepath = "" ;
		String absolutePath = "";
		
		try {
			
			out = response.getWriter();
			response.setContentType("text/html;charset=UTF-8");
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Cache-Control", "no-store");
			response.setHeader("Cache-Control", "must-revalidate");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
	
			 
//			relativepath = request.getContextPath() + "/Confirmacion/"  ;
//			absolutePath = request.getServletContext().getRealPath("/Confirmacion/");
			
			relativepath = request.getContextPath() + "/"+PropertiesSystem.CARPETA_DOCUMENTOS_EXPORTAR+"/"  ;
			absolutePath = request.getServletContext().getRealPath("/"+PropertiesSystem.CARPETA_DOCUMENTOS_EXPORTAR+"/");
			
			int tipodocumento = Integer.parseInt( request.getParameter("documento") );
			
			String sufijo =  new SimpleDateFormat("ddMMyyyHHmmss").format(new Date()); 
			
			//&& ================ 1 solo pendientes ( antes de comparar ) 
			if(tipodocumento == 1) {
				
				nombreDocumento = "Depositos_A_Comparar_"+sufijo+".xlsx" ;
				
				List<PcdConsolidadoDepositosBanco> lstDepositosBanco =   (List<PcdConsolidadoDepositosBanco>)request.getSession().getAttribute(ConsolidadoDepsBcoDAO.lstConsolidadoDpsTodos );
				List<Deposito_Report> lstDepositosCaja = (List<Deposito_Report>) request.getSession().getAttribute(ConsolidadoDepsBcoDAO.lstTodosDepositosCaja);
			
				if(lstDepositosBanco == null || lstDepositosBanco == null){
					hecho = false;
					return;
				}
				
//				System.out.println("inicia creacion excel " + new SimpleDateFormat("HH:mm:ss.sss").format(new Date()));
				Rptmcaja011Xls xls = new Rptmcaja011Xls( lstDepositosBanco, lstDepositosCaja, null, absolutePath,  relativepath,  nombreDocumento, false) ;
				xls.generarSoloProcesados = false;
				hecho = xls.crearExcel();
				
//				System.out.println("termina creacion excel " + new SimpleDateFormat("HH:mm:ss.sss").format(new Date()));
				
				return;
			}
			
			//&& ================  2 solo pendientes mas coincidentes 
			if(tipodocumento == 2) {
				 
				nombreDocumento = "ResultadoComparacion_"+sufijo+".xlsx" ;
				
				List<PcdConsolidadoDepositosBanco> lstDepositosBanco = (List<PcdConsolidadoDepositosBanco>) request.getSession().getAttribute("pcd_lstDepositosBancoNoCoincidentes");
				List<Deposito_Report> lstDepositosCaja = (List<Deposito_Report>) request.getSession().getAttribute("pcd_lstDepositosCajaNoCoincidentes");
				List<ConsolidadoCoincidente> lstDepositoEnCoincidencia = (ArrayList<ConsolidadoCoincidente>) request.getSession().getAttribute("sesmapvarDepsCoincidencias");
				
				if(lstDepositosBanco == null || lstDepositosBanco == null || lstDepositoEnCoincidencia == null ){
					hecho = false;
					return;
				}
				
//				System.out.println("inicia creacion excel " + new SimpleDateFormat("HH:mm:ss.sss").format(new Date()));
				Rptmcaja011Xls xls = new Rptmcaja011Xls( lstDepositosBanco, lstDepositosCaja, lstDepositoEnCoincidencia, absolutePath,  relativepath,  nombreDocumento, true) ;
				xls.generarSoloProcesados = false;
				hecho = xls.crearExcel();
				
//				System.out.println("termina creacion excel " + new SimpleDateFormat("HH:mm:ss.sss").format(new Date()));
				
				return;
				
			}
			
			//&& ============== 3 solo coincidencias, agrupadas e individuales
			if(tipodocumento == 3) {
				
				nombreDocumento = "CoincidenciaPorNivel_"+sufijo+".xlsx" ;
				
				List<PcdConsolidadoDepositosBanco> lstDepositosBanco = new ArrayList<PcdConsolidadoDepositosBanco>() ;
				List<Deposito_Report> lstDepositosCaja = new ArrayList<Deposito_Report>();
				
				List<PcdConsolidadoDepositosBanco> todosDepositosBanco =   (List<PcdConsolidadoDepositosBanco>)request.getSession().getAttribute(ConsolidadoDepsBcoDAO.lstConsolidadoDpsTodos );
				
				List<ConsolidadoCoincidente> lstDepositoEnCoincidencia = (ArrayList<ConsolidadoCoincidente>) request.getSession().getAttribute("pcd_lstProcesarDepositosCoincidentes");
				
				for (final ConsolidadoCoincidente cc : lstDepositoEnCoincidencia) {
					
					PcdConsolidadoDepositosBanco db = 
						(PcdConsolidadoDepositosBanco) CollectionUtils.find(todosDepositosBanco, new Predicate(){
							public boolean evaluate(Object o) {
								return ((PcdConsolidadoDepositosBanco)o).getIdresumenbanco() == cc.getIdresumenbanco() ;
							}
						});
					
					db.setReferenciascaja(cc.getReferencesdepscaja());
					db.setConsecutivoscaja(cc.getIdsdepscaja());
					db.setCantidaddepscaja(cc.getCantdepositoscaja());
					
					lstDepositosBanco.add( db );
					
					lstDepositosCaja.addAll( cc.getDepositoscaja() );
				}
				
//				System.out.println("inicia creacion excel " + new SimpleDateFormat("HH:mm:ss.sss").format(new Date()));
				
				Rptmcaja011Xls xls = new Rptmcaja011Xls( lstDepositosBanco, lstDepositosCaja,  lstDepositoEnCoincidencia, absolutePath,  relativepath,  nombreDocumento, true) ;
				xls.generarSoloProcesados = true;
				hecho = xls.crearExcel();
				
//				System.out.println("termina creacion excel " + new SimpleDateFormat("HH:mm:ss.sss").format(new Date()));
				
				return;
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
			hecho = false;
		}finally{
		 
			if(hecho){
				out.println( relativepath + nombreDocumento);
			}else{
				out.println("");
			}
		}
		return;
	}
			
 
    public SvltReporteConsolidadoDepositos() {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
 

}

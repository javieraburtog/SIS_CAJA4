package com.casapellas.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.casapellas.entidades.Varqueo;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.reportes.Rptmcaja004Xls;
import com.ibm.icu.util.Calendar;

/**
 * Servlet implementation class svltReporteCierres
 */
//@WebServlet(displayName="svltReporteCierres", name = "svltReporteCierres", urlPatterns = {"/svltReporteCierres"})

public class svltReporteCierres extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
	public void generarReporte(){
		try {
			
		} catch (Exception e) {
			
		}
	}
	
	
	//&& =========== Consultar los datos de los cierres.
	@SuppressWarnings("unchecked")
	public List<Varqueo> obtenerArqueos(List<Integer> iCaids, String sCodcomp, 
						String sMoneda, Date dtFechaIni, Date dtFechaFin,int iCodcajero){
		List<Varqueo> lstArqueos = null;
		boolean bNuevaSesion = false;
		Session sesion = null;
		Transaction trans = null;
		
		try {
			
			sesion = HibernateUtilPruebaCn.currentSession();

			if(sesion.getTransaction().isActive())
				trans = sesion.getTransaction();
			else{
				bNuevaSesion = true;
				trans = sesion.beginTransaction();
			}
			
			Criteria cr = sesion.createCriteria(Varqueo.class)
			.add(Restrictions.between("id.fecha", dtFechaIni, dtFechaFin));
			
			if(iCaids.size() > 0 && iCaids.get(0) != 0 )
				cr.add(Restrictions.in("id.caid", iCaids));
			
			if(sCodcomp.compareTo("SC") != 0)
				cr.add(Restrictions.eq("id.codcomp", sCodcomp));
			
			if(sMoneda.compareTo("SM") != 0)
				cr.add(Restrictions.eq("id.moneda", sMoneda));
			
			if(iCodcajero != 0)
				cr.add(Restrictions.eq("id.codcajero", iCodcajero));
			
			lstArqueos = (ArrayList<Varqueo>)cr.list();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(bNuevaSesion){
				try{trans.commit();}catch(Exception e){if(trans.isActive())trans.rollback();}
				try{HibernateUtilPruebaCn.closeSession(sesion);}catch(Exception e){}
			}
		}
		return lstArqueos;
	}
	
	
	
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		
		int iCodcajero = 0;
		String sCajas = request.getParameter("caja");
		String compania = request.getParameter("compania");
        String moneda = request.getParameter("moneda");
        String fecha1 = request.getParameter("fecha1");
        String fecha2 = request.getParameter("fecha2");
        String cajero = request.getParameter("cajero");
        
        Date dtFechaFin = new Date();
        Date dtFechaIni = new Date();
        
    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.MONTH, -1);
    	dtFechaIni = cal.getTime();
        
		response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Cache-Control", "no-store");
	    response.setHeader("Cache-Control", "must-revalidate");
	    response.setHeader("Pragma", "no-cache");
	    response.setDateHeader("Expires", 0); 

        List<Integer> lstIdsCajas = new ArrayList<Integer>();
        
        try {
        	
        	if(sCajas == null || sCajas.compareTo("") == 0) sCajas = "0,";
        	
        	if(sCajas.trim().indexOf(',') == -1)
        		sCajas += ",";
        
        	String [] sIdsCaja = sCajas.split(",");
        	for (String sId : sIdsCaja) 
        		lstIdsCajas.add(Integer.parseInt(sId.trim()));
        	
        	if(fecha1 != null && fecha1.trim().compareTo("") != 0 )
        		dtFechaIni = new SimpleDateFormat("dd/MM/yyyy").parse(fecha1);
        	if(fecha2 != null && fecha2.trim().compareTo("") != 0 )
        		dtFechaFin = new SimpleDateFormat("dd/MM/yyyy").parse(fecha2);

        	if(cajero != null && cajero.compareTo("") != 0 && cajero.compareTo("SCC") != 0)
        		iCodcajero = Integer.valueOf(cajero);
        	
        	List<Varqueo> lstArqueos = obtenerArqueos(lstIdsCajas, compania, 
        									moneda, dtFechaIni, dtFechaFin, iCodcajero);
        	
        	if(lstArqueos == null || lstArqueos.isEmpty()){
        		out.println("");
        		return;
        	}
        	
        	String sRutaDescrg = request.getContextPath()+"/Confirmacion/";
        	String sRutaFisica = request.getRealPath(File.separatorChar+"Confirmacion");
        	
			Collections.sort(lstArqueos, new Comparator<Varqueo>() {
				public int compare(Varqueo v1, Varqueo v2) {
				
					int iCompFecha = ( v1.getId().getCaid() < v2.getId()
								.getCaid() )? -1 : ( v1.getId().getCaid() > 
								v2.getId().getCaid() )? 1 : 0;
					if (iCompFecha == 0)		
						iCompFecha = (v1.getId().getFecha().before(v2.getId()
							.getFecha())) ? -1 : (v1.getId().getFecha()
							.after(v2.getId().getFecha())) ? 1 : 0;
					if (iCompFecha == 0)
						iCompFecha = (v1.getId().getCodcomp().trim().compareTo(
							v2.getId().getCodcomp().trim()) < 0 ) ? -1 :
							(v1.getId().getCodcomp().trim().compareTo(
							v2.getId().getCodcomp().trim()) > 0 )? 1 : 0;
					return iCompFecha;
				}
			});
        	
        	Rptmcaja004Xls rpt = new Rptmcaja004Xls(lstArqueos, sRutaFisica);
        	String nombrearchivo = rpt.crearRptmcaja004();
        	
        	sRutaDescrg = (nombrearchivo.compareTo("") == 0 )? "" 
					: sRutaDescrg + nombrearchivo;

        	out.println(sRutaDescrg);
        	
        }catch(Exception e){	
        } finally {
           out.close();
        }
	}

    public svltReporteCierres() {
        super();
    }
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

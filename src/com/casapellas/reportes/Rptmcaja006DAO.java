package com.casapellas.reportes;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import com.casapellas.controles.CompaniaCtrl;
import com.casapellas.controles.CtrlCajas;
import com.casapellas.controles.MonedaCtrl;
import com.casapellas.entidades.F55ca014;
import com.casapellas.entidades.Vcompania;
import com.casapellas.entidades.Vf55ca01;
import com.casapellas.entidades.Vrptmcaja006Id;
import com.casapellas.util.FechasUtil;
import com.infragistics.faces.input.component.html.HtmlDateChooser;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
/**
 * CASA PELLAS S.A.
 * Creado por.........: Carlos Manuel Hernández Morrison
 * Fecha de Creación..: 06/04/2010
 * Última modificación: Carlos Manuel Hernández Morrison
 * Modificado por.....:	08/04/2010
 * Descripcion:.......: Administración del Reporte de Recibos de Caja (rptmcaja006)
 * 
 */
public class Rptmcaja006DAO {
	Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	private HtmlDropDownList  ddlFiltroCompania, ddlFiltroMoneda;
	private List<SelectItem>  lstFiltroCompania, lstFiltroMoneda;
	private List<SelectItem> lstFiltroCajas;
	private HtmlDropDownList ddlFiltroCajas;
	private HtmlDateChooser dcFechaFinal,dcFechaInicio;
	private Date fechaactual1,fechaactual2;
	
/******************************************************************************/
/** Método: Filtrar las compañías configuradas para la caja seleccionada.
 *	Fecha:  23/12/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 **/
	@SuppressWarnings("unchecked")
	public void filtrarCompaniaxCaja(ValueChangeEvent ev){
		String sCaid ="";

		try {
			lstFiltroCompania = new ArrayList<SelectItem>();
			lstFiltroCompania.add(new SelectItem("SCO","Compañías","Seleccione para no filtrar por la compañía"));
			
			sCaid = ddlFiltroCajas.getValue().toString().trim();
			
			if(sCaid.trim().compareTo("SCA") == 0 ){
				m.remove("rpt006_lstFiltroCompania");
				getLstFiltroCompania();
				ddlFiltroCompania.dataBind();
				return;
			}
			
			F55ca014[] lstComxCaja  =  new CompaniaCtrl().obtenerCompaniasxCaja( Integer.parseInt(sCaid) );		
			if(lstComxCaja!=null && lstComxCaja.length > 0){
				for (F55ca014 f14 : lstComxCaja) {
					SelectItem si = new SelectItem();
					si.setValue(f14.getId().getC4rp01().trim());
					si.setLabel(f14.getId().getC4rp01d1().trim());
					si.setDescription(f14.getId().getC4rp01().trim() +": " +f14.getId().getC4rp01d1().trim());
					lstFiltroCompania.add(si);
				}
			}
			lstFiltroMoneda = new ArrayList<SelectItem>();
			m.remove("rpt006_lstFiltroMoneda");
			m.put("rpt006_lstFiltroCompania", lstFiltroCompania);
			ddlFiltroCompania.dataBind();
			ddlFiltroMoneda.dataBind();
			
		} catch (Exception error) {
			System.out.println("Error en Rptmcaja006DAO.filtrarCompaniaxCaja  " + error);
		}		
	}
	
/*********************************************************************************/
/**    Validar los datos de los filtros y generar el reporte rptmcaja005		**/	
	@SuppressWarnings("unchecked")
	public void  generarReporteMcaja006(ActionEvent ev){
		int iCaid = 0;
		String sCaid,sCodcomp,sMoneda,sMensaje="";
		String sNomcomp="", sFechaReporte, sFechaIni,sFechaFin;
		boolean bValido = true;
		FechasUtil f = new FechasUtil();
		List<RptmcajaHeader> lstRptmcaja005Hdr = new ArrayList<RptmcajaHeader>(1);
		
		try {
			sCaid    = ddlFiltroCajas.getValue().toString();
			sCodcomp = ddlFiltroCompania.getValue().toString();
			sMoneda  = ddlFiltroMoneda.getValue().toString();
			
			
			if(!sCodcomp.equals("SCO")){
				if(sMoneda.equals("SMO")){
					bValido = false;
					sMensaje = "Seleccione el valor de la Moneda a utilizar";
				}
			}else{
				bValido = false;
				sMensaje = "Seleccione el valor de la compañía a utilizar";
			}
			
			if(bValido){
				if(!sCaid.trim().equals("SCA")){
					iCaid = Integer.parseInt(sCaid);
				}
				
				//------------ Manejar las fechas del reporte.
				Date dtInicio=new Date(), dtFin=new Date();				
				if(dcFechaInicio.getValue()!=null)
					dtInicio = (Date)dcFechaInicio.getValue();
				if(dcFechaFinal.getValue()!=null)
					dtFin = (Date)dcFechaFinal.getValue();
				if(dtInicio.compareTo(dtFin) >0){
					dtInicio = (Date)dcFechaFinal.getValue();
					dtFin    = (Date)dcFechaInicio.getValue();
				}
				dcFechaInicio.setValue(dtInicio);
				dcFechaFinal.setValue(dtFin);
				
				//---------- Leer nombre de caja y nombre de compañía.
				SelectItem siFiltros[] = null;
				siFiltros = ddlFiltroCompania.getSelectItems();
				for(int i=0; i<siFiltros.length; i++){
					if(siFiltros[i].getValue().toString().trim().equals(sCodcomp)){
						sNomcomp = siFiltros[i].getLabel().trim();
						break;
					}
				}
				//------------- LLenar los datos para el encabezado del reporte.
				RptmcajaHeader rh = new RptmcajaHeader();				
				rh.setCodcomp(sCodcomp);
				rh.setMoneda(sMoneda);
				rh.setNombrecomp(sNomcomp);
				sFechaIni = FechasUtil.formatDatetoString(dtInicio, "dd/MM/yyyy");
				sFechaFin = FechasUtil.formatDatetoString(dtFin,	"dd/MM/yyyy");
				sFechaReporte = FechasUtil.formatDatetoString(new Date(), "dd/MM/yyyy hh:mm:ss a");
				rh.setSfechainicial(sFechaIni);
				rh.setSfechafinal(sFechaFin);
				rh.setSfechareporte(sFechaReporte);
				lstRptmcaja005Hdr.add(rh);
					
				//------------ Obtener los recibos para el cuerpo del reporte.
				CtrlCajas cc = new CtrlCajas();
				List<Vrptmcaja006Id> lstRec = cc.obtieneRecibosrpt006(iCaid,sCodcomp, sMoneda, dtInicio, dtFin);
				lstRec = (lstRec == null)? new ArrayList<Vrptmcaja006Id>(): lstRec;
				
				//--------- Objetos sesión de reporte.
				m.put("rptmcaja006_hd", lstRptmcaja005Hdr);
				m.put("rptmcaja006_bd", lstRec);
				
				//realizar la navegación sobre la misma página aplicar filtros al reporte.
				FacesContext fcInicio = FacesContext.getCurrentInstance();		
				NavigationHandler nhInicio = fcInicio.getApplication().getNavigationHandler();		
				nhInicio.handleNavigation(fcInicio, null, "rptmcaja006");
				
			}
			
		} catch (Exception error) {
			System.out.println("Error en Rptmcaja006.generarReporteMcaja006 " + error);
		}
	}
	
	
/*********************************************************************************/
/**  Cargar las monedas configuradas para la caja y compañía Seleccionada 		**/
	public void obtenerMonedasxCompania(ValueChangeEvent ev){
		String sCodcomp = "";
		try{
			sCodcomp = ddlFiltroCompania.getValue().toString();
			m.remove("rpt006_lstFiltroMoneda");
			
			if(!sCodcomp.equals("SCO")){
				MonedaCtrl moCtrl = new MonedaCtrl();
				String[] monedas = moCtrl.obtenerMonedasxCajaCompania(0, sCodcomp);
				List lstFiltro = new ArrayList();
				lstFiltro.add(new SelectItem("SMO","Moneda","Selección de monedas"));
				if(monedas!=null){
					for(int i=0; i<monedas.length;i++)
						lstFiltro.add(new SelectItem(monedas[i].toString(),monedas[i].toString(),"Tipo moneda "+(i+1)));
				}
				lstFiltroMoneda = lstFiltro;
				m.put("rpt006_lstFiltroMoneda", lstFiltro);				
			}
			ddlFiltroMoneda.dataBind();
		}catch(Exception error){
			System.out.println("Error en rpt006_lstFiltroMoneda.obtenerMonedasxCompania() " +error);
		}
	}
//--------------- GETTERS Y SETTERS DEL REPORTE --------------------//
	public HtmlDateChooser getDcFechaFinal() {
		return dcFechaFinal;
	}
	public void setDcFechaFinal(HtmlDateChooser dcFechaFinal) {
		this.dcFechaFinal = dcFechaFinal;
	}
	public HtmlDateChooser getDcFechaInicio() {
		return dcFechaInicio;
	}
	public void setDcFechaInicio(HtmlDateChooser dcFechaInicio) {
		this.dcFechaInicio = dcFechaInicio;
	}
	public HtmlDropDownList getDdlFiltroCompania() {
		return ddlFiltroCompania;
	}
	public void setDdlFiltroCompania(HtmlDropDownList ddlFiltroCompania) {
		this.ddlFiltroCompania = ddlFiltroCompania;
	}
	public HtmlDropDownList getDdlFiltroMoneda() {
		return ddlFiltroMoneda;
	}
	public void setDdlFiltroMoneda(HtmlDropDownList ddlFiltroMoneda) {
		this.ddlFiltroMoneda = ddlFiltroMoneda;
	}
	public Date getFechaactual1() {
		if(m.get("rpt006_fechaactual1") == null){
			fechaactual1 = new Date();
			m.put("rpt006_fechaactual1", fechaactual1);
		}
		return fechaactual1;
	}
	public void setFechaactual1(Date fechaactual1) {
		this.fechaactual1 = fechaactual1;
	}
	public Date getFechaactual2() {
		if(m.get("rpt006_fechaactual2")==null){
			fechaactual2 = new Date();
			m.put("rpt006_fechaactual2", fechaactual2);
		}
		return fechaactual2;
	}
	public void setFechaactual2(Date fechaactual2) {
		this.fechaactual2 = fechaactual2;
	}
	@SuppressWarnings("unchecked")
	public List getLstFiltroCompania() {
		try{
			if(m.get("rpt006_lstFiltroCompania") == null){
				lstFiltroCompania = new ArrayList<SelectItem>();
				lstFiltroCompania.add(new SelectItem("SCO","Compañías","Seleccione para no filtrar por la compañía"));
				 
				List<Vcompania>lstCompanias = CompaniaCtrl.obtenerCompaniasCajaJDE();
				if(lstCompanias!=null)
					for (Vcompania vc : lstCompanias) 
						lstFiltroCompania.add(new SelectItem(vc.getId().getDrky().trim(),
											vc.getId().getDrky().trim()+" "+vc.getId().getDrdl01().trim(),
											vc.getId().getDrky().trim()+" "+vc.getId().getDrdl01().trim()));
				m.put("rpt006_lstFiltroCompania", lstFiltroCompania);
			}		
			else
				lstFiltroCompania = (ArrayList<SelectItem>)m.get("rpt006_lstFiltroCompania");
		
		}catch(Exception error){
			lstFiltroCompania = new ArrayList<SelectItem>();
			lstFiltroCompania.add(new SelectItem("SCO","Compañías","Seleccione para no filtrar por la compañía"));
			System.out.println("Error en Rptmcaja006DAO.getlstFiltroCompania " +error);
		}
		return lstFiltroCompania;
	}
	public void setLstFiltroCompania(List lstFiltroCompania) {
		this.lstFiltroCompania = lstFiltroCompania;
	}
	public List getLstFiltroMoneda() {
		try{
			if(m.get("rpt006_lstFiltroMoneda")==null){
				lstFiltroMoneda  = new ArrayList();			
				lstFiltroMoneda.add(new SelectItem("SMO","Moneda","Selección de monedas"));
				m.put("rpt006_lstFiltroMoneda", lstFiltroMoneda);
			}		
			else
				lstFiltroMoneda = (ArrayList)m.get("rpt006_lstFiltroMoneda");
		}catch(Exception error){
			System.out.println("Error en Rptmcaja004DAO.getLstFiltroMoneda " +error);
		}
		return lstFiltroMoneda;
	}
	public void setLstFiltroMoneda(List lstFiltroMoneda) {
		this.lstFiltroMoneda = lstFiltroMoneda;
	}
	@SuppressWarnings("unchecked")
	public List<SelectItem> getLstFiltroCajas() {
		
		try {
			if(m.get("rpt006_lstFiltroCajas")==null){

				lstFiltroCajas = new ArrayList<SelectItem>();
				lstFiltroCajas.add(new SelectItem("SCA","Todas","Seleccione la Caja para filtrar"));
				
				List<Vf55ca01> lstCaContador =  new CtrlCajas().getAllCajas();
				if(lstCaContador!=null && lstCaContador.size()>0){
					for (Vf55ca01 v : lstCaContador) 
						lstFiltroCajas.add(new SelectItem(v.getId().getCaid()+"",
											v.getId().getCaid()+":  "+v.getId().getCaname().trim(),
											v.getId().getCaid()+ ": " +v.getId().getCaname().trim()
												+": "+v.getId().getCacatinom().trim()  ));
					m.put("rdta_lstFiltroCaja", lstFiltroCajas);
				}
			}else{
				lstFiltroCajas =(ArrayList)m.get("rpt006_lstFiltroCajas"); 
			}
		} catch (Exception error) {
			lstFiltroCajas = new ArrayList<SelectItem>();
			lstFiltroCajas.add(new SelectItem("SCA","TODAS","Seleccione la Caja para filtrar"));
			System.out.println("Error en Rptmcaja006DAO.getLstFiltroCajas  " + error);
		}
		return lstFiltroCajas;
	}
	public void setLstFiltroCajas(List lstFiltroCajas) {
		this.lstFiltroCajas = lstFiltroCajas;
	}
	public HtmlDropDownList getDdlFiltroCajas() {
		return ddlFiltroCajas;
	}
	public void setDdlFiltroCajas(HtmlDropDownList ddlFiltroCajas) {
		this.ddlFiltroCajas = ddlFiltroCajas;
	}
}

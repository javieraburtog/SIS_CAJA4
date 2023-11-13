package com.casapellas.reportes;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import com.casapellas.controles.CtrlCajas;
import com.casapellas.controles.MonedaCtrl;
import com.casapellas.entidades.Vf55ca01;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.PropertiesSystem;
import com.infragistics.faces.input.component.html.HtmlDateChooser;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
/**
 * CASA PELLAS S.A.
 * Creado por.........: Carlos Manuel Hernández Morrison
 * Fecha de Creación..: 31/03/2010
 * Última modificación: Carlos Manuel Hernández Morrison
 * Modificado por.....:	06/04/2010
 * Descripcion:.......: Administración del Reporte de Emisión de Recibos de Caja (rptmcaja005)
 * 
 */
public class Rptmcaja005DAO {
	Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	private HtmlDropDownList ddlFiltroCaja, ddlFiltroCompania, ddlFiltroMoneda;
	private List lstFiltroCaja, lstFiltroCompania, lstFiltroMoneda;
	private HtmlDateChooser dcFechaFinal,dcFechaInicio;
	private HtmlOutputText lblMsjRptTransjde;
	private Date fechaactual1,fechaactual2;
	
/*********************************************************************************/
/**    Validar los datos de los filtros y generar el reporte rptmcaja005		**/	
	public void  generarReporteMcaja005(ActionEvent ev){
		String sCaid, sCodcomp,sMoneda,sMensaje="";
		String sNomcaid="", sNomcomp="", sFechaReporte, sFechaIni,sFechaFin;
		boolean bValido = true;
	 
		List lstRptmcaja005Hdr = new ArrayList(1), lstRecibos = null;
		
		try {
			sCaid	 = ddlFiltroCaja.getValue().toString();
			sCodcomp = ddlFiltroCompania.getValue().toString();
			sMoneda  = ddlFiltroMoneda.getValue().toString();
			
			if(!sCaid.equals("SCA")){
				if(!sCodcomp.equals("SCO")){
					if(sMoneda.equals("SMO")){
						bValido = false;
						sMensaje = "Seleccione el valor de la Moneda a utilizar";
					}
				}else{
					bValido = false;
					sMensaje = "Seleccione el valor de la compañía a utilizar";
				}
			}else{
				bValido = false;
				sMensaje = "Seleccione el valor de la caja a utilizar";
			}
			//------- Generar el Reporte. 
			if(bValido){
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
				siFiltros = ddlFiltroCaja.getSelectItems();
				for(int i=0; i<siFiltros.length; i++){
					if(siFiltros[i].getValue().toString().trim().equals(sCaid)){
						sNomcaid = siFiltros[i].getLabel().trim();
						break;
					}
				}
				siFiltros = ddlFiltroCompania.getSelectItems();
				for(int i=0; i<siFiltros.length; i++){
					if(siFiltros[i].getValue().toString().trim().equals(sCodcomp)){
						sNomcomp = siFiltros[i].getLabel().trim();
						break;
					}
				}
				//---------- LLenar datos del encabezado del reporte.
				RptmcajaHeader rh = new RptmcajaHeader();
				rh.setCaid(Integer.parseInt(sCaid));
				rh.setCodcomp(sCodcomp);
				rh.setMoneda(sMoneda);
				rh.setNombrecaja(sNomcaid);
				rh.setNombrecomp(sNomcomp);
				sFechaIni = FechasUtil.formatDatetoString(dtInicio, "dd/MM/yyyy");
				sFechaFin = FechasUtil.formatDatetoString(dtFin,	"dd/MM/yyyy");
				sFechaReporte = FechasUtil.formatDatetoString(new Date(), "dd/MM/yyyy hh:mm:ss a");
				rh.setSfechainicial(sFechaIni);
				rh.setSfechafinal(sFechaFin);
				rh.setSfechareporte(sFechaReporte);
				lstRptmcaja005Hdr.add(rh);
					
				CtrlCajas cc = new CtrlCajas();
				List lstRec = cc.obtieneRecibosrpt005(Integer.parseInt(sCaid), sCodcomp, sMoneda, dtInicio, dtFin);
				lstRecibos = lstRec==null? new ArrayList(1):lstRec;
				
				//--------- Objetos sesión de reporte.
				m.put("rptmcaja005_hd", lstRptmcaja005Hdr);
				m.put("rptmcaja005_bd", lstRecibos);
				
				//realizar la navegación sobre la misma página aplicar filtros al reporte.
				FacesContext.getCurrentInstance().getExternalContext().redirect("/"+ PropertiesSystem.CONTEXT_NAME + "/reportes/rptmcaja005.faces");
			}
			
		} catch (Exception error) {
			error.printStackTrace();
		}
	}

/*********************************************************************************/
/**  Cargar las monedas configuradas para la caja y compañía Seleccionada 		**/
	public void obtenerMonedasxCompania(ValueChangeEvent ev){
		String sCodcomp = "",sCaid="";
		
		try{			
			sCaid    = ddlFiltroCaja.getValue().toString();
			sCodcomp = ddlFiltroCompania.getValue().toString();
			m.remove("rpt005_lstFiltroMoneda");
			
			if(!sCodcomp.equals("SCO")){
				 
				String[] monedas = MonedaCtrl.obtenerMonedasxCaja(Integer.parseInt(sCaid), sCodcomp);
				List lstFiltro = new ArrayList();
				lstFiltro.add(new SelectItem("SMO","Moneda","Selección de monedas"));
				for(int i=0; i<monedas.length;i++)
					lstFiltro.add(new SelectItem(monedas[i].toString(),monedas[i].toString(),"Tipo moneda "+(i+1)));
				
				lstFiltroMoneda = lstFiltro;
				m.put("rpt005_lstFiltroMoneda", lstFiltro);				
			}
			ddlFiltroMoneda.dataBind();
		}catch(Exception error){
			System.out.println("Error en Rptmcaja005.obtenerMonedasxCompania() " +error);
		}
	}	
/*********************************************************************************/
/** actualizar la lista de compañías disponibles por caja para el contador 		**/
	public void alCambiarValorCaja(ValueChangeEvent ev){
		String sCaid;
		
		try {
			sCaid = ddlFiltroCaja.getValue().toString();
			m.remove("rpt005_lstFiltroCompania");
			m.remove("rpt005_lstFiltroMoneda");
			m.remove("rpt005_fechaactual1");
			m.remove("rpt005_fechaactual2");

 
			List<SelectItem> lstComp = new ArrayList<SelectItem>();
			List lstcaja = (ArrayList)m.get("lstCajas");
			Vf55ca01 caja = (Vf55ca01)lstcaja.get(0);
		
			lstComp.add(new SelectItem("SCO","Todas","Selección de compañía"));
			List<Object[]> lstCcomp = CtrlCajas.cargarCompaniaxContador(caja.getId().getCacont(), Integer.parseInt(sCaid));
			if(lstCcomp!=null && lstCcomp.size()>0){
				for(int i=0; i<lstCcomp.size();i++){							
					Object[] cajas		= (Object[]) lstCcomp.get(i);
					String sCompnombre  = cajas[0].toString().trim();
					String sCodcomp 	= cajas[1].toString().trim();
					lstComp.add(new SelectItem(sCodcomp,sCompnombre,"Compañía "+sCodcomp));
				}
				m.put("rtj_lstFiltroCompania", lstComp);
				lstFiltroCompania = lstComp;
			}
			ddlFiltroCompania.dataBind();
			ddlFiltroMoneda.dataBind();
		} catch (Exception error) {
			System.out.println("Error en Rptmcaja005.alCambiarValorCaja" + error);
		}
	}	
	//------------------------ GETTERS Y SETTERS -------------------------------//
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
	public HtmlDropDownList getDdlFiltroCaja() {
		return ddlFiltroCaja;
	}
	public void setDdlFiltroCaja(HtmlDropDownList ddlFiltroCaja) {
		this.ddlFiltroCaja = ddlFiltroCaja;
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
		if(m.get("rpt005_fechaactual1") == null){
			fechaactual1 = new Date();
			m.put("rpt005_fechaactual1", fechaactual1);
		}
		return fechaactual1;
	}
	public void setFechaactual1(Date fechaactual1) {
		this.fechaactual1 = fechaactual1;
	}
	public Date getFechaactual2() {
		if(m.get("rpt005_fechaactual2")==null){
			fechaactual2 = new Date();
			m.put("rpt005_fechaactual2", fechaactual2);
		}
		return fechaactual2;
	}
	public void setFechaactual2(Date fechaactual2) {
		this.fechaactual2 = fechaactual2;
	}
	public HtmlOutputText getLblMsjRptTransjde() {
		return lblMsjRptTransjde;
	}
	public void setLblMsjRptTransjde(HtmlOutputText lblMsjRptTransjde) {
		this.lblMsjRptTransjde = lblMsjRptTransjde;
	}
	public List getLstFiltroCaja() {
		try{
			if(m.get("rpt005_lstFiltroCaja")==null){
			 
				List lstCacomp = new ArrayList();
				List lstcaja = (ArrayList)m.get("lstCajas");
				Vf55ca01 caja = (Vf55ca01)lstcaja.get(0);
				List lstCaContador = CtrlCajas.obtenerCajasxContador(caja.getId().getCacont());
				if(lstCaContador!=null && lstCaContador.size()>0){
					for(int i=0;i<lstCaContador.size();i++){
						Vf55ca01 v = (Vf55ca01)lstCaContador.get(i);
						lstCacomp.add(new SelectItem(v.getId().getCaid()+"",v.getId().getCaname().trim(),""));
					}
					m.put("rpt005_lstFiltroCaja", lstCacomp);
					lstFiltroCaja = lstCacomp;
				}else
					lstCacomp.add(new SelectItem("SCA","Caja", "Sin Caja Configurada"));
			}else
				lstFiltroCaja = (ArrayList)m.get("rpt005_lstFiltroCaja");
		} catch (Exception error) {
			System.out.println("Error en RpttransjdeDAO.getLstCajas " + error);
		}	
		return lstFiltroCaja;
	}
	public void setLstFiltroCaja(List lstFiltroCaja) {
		this.lstFiltroCaja = lstFiltroCaja;
	}
	public List getLstFiltroCompania() {
		try{
			if(m.get("rpt005_lstFiltroCompania")==null){
				List lstcaja = (ArrayList)m.get("lstCajas");
				Vf55ca01 caja = (Vf55ca01)lstcaja.get(0);
				List lstComp = new ArrayList();
			
				lstComp.add(new SelectItem("SCO","Compañía","Selección de compañía"));
				List lstCcomp = CtrlCajas.cargarCompaniaxContador(caja.getId().getCacont(), Integer.parseInt(ddlFiltroCaja.getValue().toString()));
				if(lstCcomp!=null && lstCcomp.size()>0){
					for(int i=0; i<lstCcomp.size();i++){							
						Object[] cajas		= (Object[]) lstCcomp.get(i);
						String sCompnombre  = cajas[0].toString().trim();
						String sCodcomp 	= cajas[1].toString().trim();
						lstComp.add(new SelectItem(sCodcomp,sCompnombre,"Compañía "+sCodcomp));
					}
					m.put("rpt005_lstFiltroCompania", lstComp);
					lstFiltroCompania = lstComp;
				}		
			}
			else
				lstFiltroCompania = (ArrayList)m.get("rpt005_lstFiltroCompania");
		}catch(Exception error){
			System.out.println("Error en Rptmcaja005DAO.getlstFiltroCompania " +error);
		}
		return lstFiltroCompania;
	}
	public void setLstFiltroCompania(List lstFiltroCompania) {
		this.lstFiltroCompania = lstFiltroCompania;
	}
	public List getLstFiltroMoneda() {
		try{
			if(m.get("rpt005_lstFiltroMoneda")==null){
				lstFiltroMoneda  = new ArrayList();			
				lstFiltroMoneda.add(new SelectItem("SMO","Moneda","Selección de monedas"));
				m.put("rpt005_lstFiltroMoneda", lstFiltroMoneda);
			}		
			else
				lstFiltroMoneda = (ArrayList)m.get("rpt005_lstFiltroMoneda");
		}catch(Exception error){
			System.out.println("Error en Rptmcaja004DAO.getLstFiltroMoneda " +error);
		}
		return lstFiltroMoneda;
	}
	public void setLstFiltroMoneda(List lstFiltroMoneda) {
		this.lstFiltroMoneda = lstFiltroMoneda;
	}
}

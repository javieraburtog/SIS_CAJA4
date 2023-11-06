package com.casapellas.dao;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.application.NavigationHandler;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import com.casapellas.controles.CompaniaCtrl;
import com.casapellas.controles.CtrlCajas;
import com.casapellas.entidades.Vf55ca01;
import com.casapellas.entidades.Vtransaccionesjde;
import com.casapellas.reportes.TransJdeDetalleR;
import com.casapellas.reportes.TransJdeHeaderR;
import com.casapellas.util.PropertiesSystem;
import com.infragistics.faces.input.component.html.HtmlDateChooser;
import com.infragistics.faces.input.component.html.HtmlDropDownList;

/**
 * CASA PELLAS S.A.
 * Creado por.........: Carlos Manuel Hernández Morrison
 * Fecha de Creación..: 16/11/2009
 * Última modificación: Carlos Manuel Hernández Morrison
 * Modificado por.....:	02/03/2010
 * Descripción:.......: manejo de datos para el reporte de transacciones JDE. 
 * Modificado Por: Daniel Sebastian Cordero
 * Fecha Modificacion: 03 Nov 2023
 * 
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class RpttransjdeDAO {
	Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	
	private HtmlDropDownList ddlCajas,ddlFiltroCompania;
	private List lstCajasCont,lstFiltroCompania;
	private HtmlDateChooser dcFechaFinal,dcFechaInicio;
	private HtmlOutputText lblMsjRptTransjde;
	private Date fechaactual1,fechaactual2;
	
	
/*********************************************************************************/
/** restablecer los valores de los filtros utilzados paral búsqueda				**/
	public void limpiarFiltros(ActionEvent ev){
		try {
			limpiarVarSes();
			lblMsjRptTransjde.setValue("");
			ddlCajas.dataBind();
			ddlFiltroCompania.dataBind();
			
			//realizar la navegación sobre la misma página aplicar los cambios al reporte
			FacesContext fcInicio = FacesContext.getCurrentInstance();		
			NavigationHandler nhInicio = fcInicio.getApplication().getNavigationHandler();		
			nhInicio.handleNavigation(fcInicio, null, "rptmcaja003");
			
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
	

	
/*********************************************************************************/
/** actualizar la lista de compañías disponibles por caja para el contador 		**/
	public void alCambiarValorCaja(ValueChangeEvent ev){
		String sCaid;
		
		try {
			sCaid = ddlCajas.getValue().toString();
			if(sCaid.equals("CAJA")){
				m.remove("rtj_lstFiltroCompania");
				m.remove("rtj_fechaactual");
			}else{
				CtrlCajas cc = new CtrlCajas();
				List lstComp = new ArrayList();
				List lstcaja = (ArrayList)m.get("lstCajas");
				Vf55ca01 caja = (Vf55ca01)lstcaja.get(0);
				
				lstComp.add(new SelectItem("COMP","Todas","Lista de compañías asingadas al contador"));
				List lstCcomp = cc.cargarCompaniaxContador(caja.getId().getCacont(), Integer.parseInt(sCaid));
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
			}
			ddlFiltroCompania.dataBind();
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
	public void ejecutar(ActionEvent ev){
	}
/*********************************************************************************/
/** Validar y ejecutar la búsqueda a partir de los valores de los filtros       **/
	public void buscarTransJDE(ActionEvent ev){
		String sCaid,sCodcomp,sNomcomp, sCodSuc;
		Date dtFechaini,dtFechafin;
		List lstTransJde= new ArrayList(),lstTransCO = new ArrayList(),lstTransCR   = new ArrayList();
		List lstTransPR = new ArrayList(),lstTrasDep = new ArrayList(),lstHeaderRpt = new ArrayList();
		List lstTransS  = new ArrayList(),lstTransIex  = new ArrayList();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		
		CtrlCajas cc = new CtrlCajas();
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		TransJdeHeaderR rHeader;
		TransJdeDetalleR rDetalleTr;
		String sMensaje="";
		boolean bHecho=true;
	
		try {
			List lstcaja = (ArrayList)m.get("lstCajas");
			Vf55ca01 caja = (Vf55ca01)lstcaja.get(0);
			
			sCaid 	  = ddlCajas.getValue().toString();
			sCodcomp  = ddlFiltroCompania.getValue().toString();
			
			if(!sCaid.equals("CAJA")){
				if(!sCodcomp.equals("COMP")){
					if(dcFechaInicio.getValue()!=null){
						if(dcFechaFinal.getValue()!=null){
							
							lblMsjRptTransjde.setValue("");
							dtFechaini = (Date)dcFechaInicio.getValue();
							dtFechafin = (Date)dcFechaFinal.getValue();
							
							if(dtFechaini.compareTo(dtFechafin) >0){
								dtFechaini = (Date)dcFechaFinal.getValue();
								dtFechafin = (Date)dcFechaInicio.getValue();
							}
							dcFechaFinal.setValue(dtFechafin);
							dcFechaInicio.setValue(dtFechaini);
							
							//------- obtener todas las transacciones jde por los filtros .
							lstTransJde = cc.obtenerTransaccionesJDE(Integer.parseInt(sCaid), sCodcomp, dtFechaini, dtFechafin);
							
							if(lstTransJde != null && lstTransJde.size()>0){
								rHeader  = new TransJdeHeaderR();				
																
								//--------- Datos para el encabezado del reporte.
								sNomcomp   = cCtrl.obtenerNombreComp(sCodcomp, caja.getId().getCaid(), null, null);
								rHeader.setCaid(Integer.parseInt(sCaid));
								rHeader.setCodcomp(sCodcomp);
								rHeader.setSfechainicial(format.format(dtFechaini));
								rHeader.setSfechafinal(format.format(dtFechafin));
								rHeader.setCodsuc(caja.getId().getCasucur());
								rHeader.setNombrecaja(caja.getId().getCaname());
								rHeader.setNombresuc(caja.getId().getCasucurname());
								sCodSuc = caja.getId().getCasucur();
								rHeader.setNombrecomp(sNomcomp);
								lstHeaderRpt.add(rHeader);
								
								for(int i=0; i<lstTransJde.size();i++){					
									Vtransaccionesjde v = (Vtransaccionesjde)lstTransJde.get(i);										
									String sTiporec = v.getId().getTiporec();					
									String sFechatran= format.format(v.getId().getFecharec());

									rDetalleTr = new TransJdeDetalleR();
									rDetalleTr.setCaid(v.getId().getCaid());
									rDetalleTr.setCodcomp(sCodcomp);
									rDetalleTr.setCodsuc(sCodSuc);
									rDetalleTr.setNumrecdep(v.getId().getNumrecdep());
									rDetalleTr.setMonto(v.getId().getMonto().doubleValue());
									rDetalleTr.setNobatch(v.getId().getNobatch());
									rDetalleTr.setNodocumento(v.getId().getNodocumento());
									rDetalleTr.setNumfac(v.getId().getNumfac());
									rDetalleTr.setSfechatrans(sFechatran);
									rDetalleTr.setTiporec(v.getId().getTiporec());

									//clasificar las transacciones, contado,devolucion de contado, crédito, primas y depósitos.
									if(sTiporec.equals("CO") || sTiporec.equals("DCO")){
										lstTransCO.add(rDetalleTr);
									}else
									if(sTiporec.equals("CR")){
										rDetalleTr.setTipodocumento(v.getId().getTipodocumento());
										lstTransCR.add(rDetalleTr);
									}else
									if(sTiporec.equals("PR")){
										rDetalleTr.setTipodocumento(v.getId().getTipodocumento());
										lstTransPR.add(rDetalleTr);
									}else
									if(sTiporec.equals("D")){
										rDetalleTr.setNumrefer(v.getId().getNumrefer());
										lstTrasDep.add(rDetalleTr);						
									}else 
									if(sTiporec.equals("EX")){
										rDetalleTr.setTipodocumento(v.getId().getTipodocumento());
										lstTransIex.add(rDetalleTr);
									}else 
									if(sTiporec.equals("S")){
										rDetalleTr.setTipodocumento(v.getId().getTipodocumento());
										lstTransS.add(rDetalleTr);
									}
								}
							}else{
								sMensaje="No hay transacciones en el período de tiempo";
							}
						}else{
							sMensaje="Seleccione la Fecha Fin";
							bHecho=false;
						}
					}else{
						sMensaje="Seleccione la Fecha inicio";
						bHecho=false;
					}
				}else{
					sMensaje="Seleccione la Compañía";
					bHecho=false;
				}
			}else{
				sMensaje="Seleccione la caja";
				bHecho=false;
			}
			
			//validar si los filtros están correctos.
			if(!bHecho){
				lblMsjRptTransjde.setValue(sMensaje);
			}else{
				m.put("rtj_lstHdrptmcaja003",   lstHeaderRpt);
				m.put("rtj_lstDtcorptmcaja003", lstTransCO);
				m.put("rtj_lstDtcrrptmcaja003", lstTransCR);
				m.put("rtj_lstDtderptmcaja003", lstTrasDep);
				m.put("rtj_lstDtprrptmcaja003", lstTransPR);
				m.put("rtj_lstDtsarptmcaja003", lstTransS);
				m.put("rtj_lstDtierptmcaja003", lstTransIex);
				
				//realizar la navegación sobre la misma página aplicar filtros al reporte.
				
				FacesContext.getCurrentInstance().getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/reportes/rptmcaja003.faces"); 
				
			}
		} catch (Exception error) {
			error.printStackTrace();
		}		
	}
/*********************************************************************************/
	public void limpiarVarSes(){
		try {
			m.remove("rtj_fechaactual1");
			m.remove("rtj_fechaactual2");
			m.remove("rtj_lstCajas");
			m.remove("rtj_lstFiltroCompania");
			m.remove("rtj_lstHdrptmcaja003");
			m.remove("rtj_lstDtcorptmcaja003");
			m.remove("rtj_lstDtcrrptmcaja003");
			m.remove("rtj_lstDtdeprptmcaja003");
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
/*********************************************************************************/
/************************ GETTERS Y SETTERS **************************************/
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
	public HtmlDropDownList getDdlCajas() {
		return ddlCajas;
	}
	public void setDdlCajas(HtmlDropDownList ddlCajas) {
		this.ddlCajas = ddlCajas;
	}
	public HtmlOutputText getLblMsjRptTransjde() {
		return lblMsjRptTransjde;
	}
	public void setLblMsjRptTransjde(HtmlOutputText lblMsjRptTransjde) {
		this.lblMsjRptTransjde = lblMsjRptTransjde;
	}
	public HtmlDropDownList getDdlFiltroCompania() {
		return ddlFiltroCompania;
	}
	public void setDdlFiltroCompania(HtmlDropDownList ddlFiltroCompania) {
		this.ddlFiltroCompania = ddlFiltroCompania;
	}
	public List getLstCajasCont() {
		try{
			if(m.get("rtj_lstCajas")==null){
				CtrlCajas cc = new CtrlCajas();
				List lstCacomp = new ArrayList();
				List lstcaja = (ArrayList)m.get("lstCajas");
				Vf55ca01 caja = (Vf55ca01)lstcaja.get(0);
				
				lstCacomp.add(new SelectItem("CAJA","Caja","Lista de cajas asingadas al contador"));
				List lstCaContador = cc.obtenerCajasxContador(caja.getId().getCacont());
				if(lstCaContador!=null && lstCaContador.size()>0){
					for(int i=0;i<lstCaContador.size();i++){
						Vf55ca01 v = (Vf55ca01)lstCaContador.get(i);
						lstCacomp.add(new SelectItem(v.getId().getCaid()+"",v.getId().getCaname().trim(),""));
					}
					m.put("rtj_lstCajas", lstCacomp);
					lstCajasCont = lstCacomp;
					ddlCajas.dataBind();
				}
			}else
				lstCajasCont = (ArrayList)m.get("rtj_lstCajas");
		} catch (Exception error) {
			error.printStackTrace();
		}	
		return lstCajasCont;
	}
	public void setLstCajasCont(List lstCajasCont) {
		this.lstCajasCont = lstCajasCont;
	}
	public List getLstFiltroCompania() {
		try {			
			if(m.get("rtj_lstFiltroCompania")==null){
				CtrlCajas cc = new CtrlCajas();
				List lstComp = new ArrayList();
				List lstcaja = (ArrayList)m.get("lstCajas");
				Vf55ca01 caja = (Vf55ca01)lstcaja.get(0);
				
				lstComp.add(new SelectItem("COMP","Compañía","Lista de compañías asingadas al contador"));
				List lstCcomp=cc.cargarCompaniaxContador(caja.getId().getCacont(), 0);
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
			}else
				lstFiltroCompania = (ArrayList)m.get("rtj_lstFiltroCompania");
			
		} catch (Exception error) {
			error.printStackTrace();
		}
		return lstFiltroCompania;
	}
	public void setLstFiltroCompania(List lstFiltroCompania) {
		this.lstFiltroCompania = lstFiltroCompania;
	}
	public Date getFechaactual1() {
		if(m.get("rtj_fechaactual1") == null){
			fechaactual1 = new Date();
			m.put("rtj_fechaactual1", fechaactual1);
		}
		return fechaactual1;
	}
	public void setFechaactual1(Date fechaactual1) {
		this.fechaactual1 = fechaactual1;
	}
	public Date getFechaactual2() {
		if(m.get("rtj_fechaactual2")==null){
			fechaactual2 = new Date();
			m.put("rtj_fechaactual2", fechaactual2);
		}
		return fechaactual2;
	}
	public void setFechaactual2(Date fechaactual2) {
		this.fechaactual2 = fechaactual2;
	}
	
}

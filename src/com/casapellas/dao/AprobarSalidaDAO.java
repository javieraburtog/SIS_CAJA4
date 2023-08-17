package com.casapellas.dao;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlInputTextarea;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import com.casapellas.controles.CompaniaCtrl;
import com.casapellas.controles.CtrlCajas;
import com.casapellas.controles.EmpleadoCtrl;
import com.casapellas.controles.MetodosPagoCtrl;
import com.casapellas.controles.MonedaCtrl;
import com.casapellas.controles.ReciboCtrl;
import com.casapellas.controles.SalidasCtrl;
import com.casapellas.entidades.Aplicacion;
import com.casapellas.entidades.F55ca014;
import com.casapellas.entidades.F55ca020;
import com.casapellas.entidades.Recibojde;
import com.casapellas.entidades.Salida;
import com.casapellas.entidades.SalidaId;
import com.casapellas.entidades.Valorcatalogo;
import com.casapellas.entidades.ens.Vautoriz;
import com.casapellas.entidades.Vf0101;
import com.casapellas.entidades.Vf55ca01;
import com.casapellas.entidades.Vsalida;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.Divisas;
import com.casapellas.util.PropertiesSystem;
import com.ibm.faces.component.html.HtmlJspPanel;
import com.infragistics.faces.grid.component.RowItem;
import com.infragistics.faces.grid.component.html.HtmlGridView;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
import com.infragistics.faces.shared.smartrefresh.SmartRefreshManager;
import com.infragistics.faces.window.component.html.HtmlDialogWindow;

/**
 * CASA PELLAS S.A.
 * Creado por.........: Carlos Manuel Hernández Morrison
 * Fecha de Creación..: 22/02/2010
 * Última modificación: 25/02/2010
 * Modificado por.....:	Carlos Manuel Hernández Morrison.
 * 
 */
public class AprobarSalidaDAO {
	Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	
	//-------- Búsqueda de salidas, filtros, y grid principal.
	private HtmlInputText txtParametro;
	private List<SelectItem>lstFiltroMoneda;
	private List lstTipoBusquedaCliente,lstFiltroCompanias,lstFiltroEstado,lstSalidas;
	private HtmlDropDownList ddlTipoBusqueda,ddlFiltroCompanias,ddlFiltroMonedas,ddlFiltroEstado;
	private HtmlOutputText lblMsgExistSalida;
	private String lblMsgExistSalida1;
	private HtmlGridView gvSalidas;
	
	//------- Ventana de Detalle de Salida.
	private HtmlOutputText lbldsFsalida, lbldsNoSalida, lbldsCodSol,lbldsMoneda,lbldsNombreSol;
	private HtmlOutputText lbldsCompania, lbldsOperacion, lbldsMonto, lbldsEstado,lbldsTasa;
	private HtmlJspPanel jsppSalidaCheques,jpPanelContabds;
	private HtmlOutputText lbldsNocheque, lbldsBanco, lbldsPortador, lbldsEmisor,lbldsNobatch,lbldsNodoc;
	private HtmlInputTextarea txtdsConceptoSalida;
	private HtmlDialogWindow dwProcesa,dwDetalleSalida;
	
	//------- Cambiar de estado la solicitud, aprobar o denegar.
	private HtmlOutputText lblcesNombreSol,lblcesNomCompania,lblcesFechasol,lblcesOperacion;
	private HtmlOutputText lblcesMonto,lblConfirmCamEstado,lblMsgValidaAprSalidas;
	private HtmlDialogWindow dwCambioEstadoSalida;

/*******************************************************************************/
/**				Cambiar el estado de salida, a Denegada o Procesada	  		  **/
	public void cambiarEstadoSalida(ActionEvent ev){
		String sEstado = "";
		Vsalida v = null;
		Salida s = new Salida();
		SalidaId sID = new SalidaId();
		SalidasCtrl sCtrl = new SalidasCtrl();
		boolean bHecho = true;
		String sHeaderCorreo = "",sFooterCorreo="";
		
		try {
			dwCambioEstadoSalida.setWindowState("hidden");
			
			if(m.get("aps_CambiarEstadoS")!=null && m.get("aps_CEsalida")!=null){
				sEstado = m.get("aps_CambiarEstadoS").toString();
				v = (Vsalida)m.get("aps_CEsalida");
				
				//------ Llenar los datos de la Salida.
				sID.setCaid(v.getId().getCaid());
				sID.setCodcomp(v.getId().getCodcomp());
				sID.setCodsuc(v.getId().getCodsuc());
				sID.setNumsal(v.getId().getNumsal());
				s.setId(sID);
				s.setCodapr(v.getId().getCodapr());
				s.setCodcaj(v.getId().getCodcaj());
				s.setCodsol(v.getId().getCodsol());
				s.setConcepto(v.getId().getConcepto());
				s.setEquiv(v.getId().getEquiv());
				s.setFaproba(v.getId().getFaproba());
				s.setFproceso(v.getId().getFproceso());
				s.setFsolicitud(v.getId().getFsolicitud());
				s.setMoneda(v.getId().getMoneda());
				s.setMonto(v.getId().getMonto());
				s.setOperacion(v.getId().getOperacion());
				s.setRefer1(v.getId().getRefer1());
				s.setRefer2(v.getId().getRefer2());
				s.setRefer3(v.getId().getRefer3());
				s.setRefer4(v.getId().getRefer4());
				s.setTasa(v.getId().getTasa());
				
				if(sEstado.equals("1")){ //----- Denegar Salida					
					s.setEstado("D");
					sHeaderCorreo = "Notificación de rechazo de Salida de Caja";
					sFooterCorreo =  "Esta solicitud de Salida ha sido denegada";
				}else if(sEstado.equals("2")){ //----- Aprobar Salida
					s.setEstado("A");
					s.setFaproba(new Date());
					sHeaderCorreo = "Notificación de Aprobación de Salida de Caja";
					sFooterCorreo = "Esta solicitud de Salida ha sido aprobada";
				}
				bHecho = sCtrl.guardarActualizarSalida(s, false);
				if(!bHecho){
					dwProcesa.setWindowState("normal");
					lblMsgValidaAprSalidas.setValue("No se ha actualizado la salida, ocurrió un error durante el proceso");
				}else{
					restablecerVistaSalidas();
					
					//--------Mandar Correo de Notificación al cajero.
					Divisas dv = new Divisas();
					List lstCajas = (List)m.get("lstCajas");
					Vautoriz[] vAut = (Vautoriz[])m.get("sevAut");
					Vf0101 f01 = null;
		    		Vf55ca01 f5 = ((Vf55ca01)lstCajas.get(0));
		    		String sUbicacion, sTo, sFrom, sCc,sCc1, sSubject,sTelefono,sNombreFrom="";
		    		EmpleadoCtrl ec = new EmpleadoCtrl();
		    		
		    		f01 = EmpleadoCtrl.buscarEmpleadoxCodigo2(vAut[0].getId().getCodreg());
		    		if(f01!=null){
		    			sNombreFrom = f01.getId().getAbalph().trim();
		    			sFrom = f01.getId().getWwrem1().trim().toUpperCase();
		    			if(!Divisas.validarCuentaCorreo(sFrom))
		    				sFrom = "webmaster@casapellas.com.ni";
		    			else 
		    				if(sEstado.equals("2")) 
		    					sCc1 = sFrom;
		    		}else {
		    			sFrom = "webmaster@casapellas.com.ni";
		    			sNombreFrom = f5.getId().getCacatinom().trim();
		    		}
		    		
//		    		sFrom =  f5.getId().getCaautimail().trim().toUpperCase();
		    		sTo = f5.getId().getCacatimail().trim().toUpperCase();
					sCc = f5.getId().getCaan8mail().trim().toUpperCase();
					sCc1= ""; 
					sSubject = "Respuesta a solicitud de Salida de Caja";
					sUbicacion = f5.getId().getCaco() + "  " +f5.getId().getCaconom().trim();

					//---- Validar el destino: autorizador, sino,supervisor, sino: no se envía correo.
					boolean bCorreo = true;
					if(!Divisas.validarCuentaCorreo(sTo)){
						if(!Divisas.validarCuentaCorreo(sCc))
							bCorreo = false;
						else
							sTo = sCc;
					}	
					//-------------- Obtener Telefono de caja.
					 
					F55ca020 f = CtrlCajas.obtenerInfoCaja(v.getId().getCodsuc().trim(),v.getId().getCodcomp().trim());
					if(f==null)
						sTelefono = " ##### ";
					else
						sTelefono = f.getId().getTelefono() +" Ext " + f.getId().getExtension();
					
					//--- Obtener Dirección URL de la aplicación.					
					String sUrl = dv.obtenerURL();
					if(sUrl==null || sUrl.equals("")){
						Aplicacion ap = dv.obtenerAplicacion(vAut[0].getId().getCodapp());
						sUrl = (ap==null)?""+PropertiesSystem.CONTEXT_NAME+"":ap.getUrl().trim();
					}
					if(bCorreo){
						bCorreo = sCtrl.enviarCorreo(sHeaderCorreo, sFooterCorreo, v.getId().getToperacion(),
											sTo, sFrom,sNombreFrom, sCc, sCc1, sSubject, v.getId().getCodsol(), v.getId().getNombresol(),
											f5.getId().getCaid(),f5.getId().getCaname().trim(),sTelefono,
											v.getId().getCodcomp() +" / " + v.getId().getNombrecomp(), 
											sUbicacion, v.getId().getNumsal(),v.getMonto(), v.getId().getMoneda(),
											sUrl, v.getId().getFsolicitud());
					}
				}
			}
			m.remove("aps_CEsalida");
			m.remove("aps_CambiarEstadoS");
		} catch (Exception error) {
			error.printStackTrace();
		}
	}	
/*******************************************************************************/
/**		Mostrar ventana de confirmación para cambiar de estado la salida	  **/
	public void denegarSolSalida(ActionEvent ev){
		try {
			mostrarVentanaCambioEstado(ev,1);
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
	public void aprobarSolSalida(ActionEvent ev){
		try {
			mostrarVentanaCambioEstado(ev,2);
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
	public void mostrarVentanaCambioEstado(ActionEvent ev, int iEstado){
		RowItem ri = null;
		Vsalida v = new Vsalida();
		Divisas dv = new Divisas();
		
		try {
			ri = (RowItem)ev.getComponent().getParent().getParent();
			v = (Vsalida)gvSalidas.getDataRow(ri);
			m.put("aps_CEsalida",v);
			
			if(v.getId().getEstado().equals("E")){
				dwCambioEstadoSalida.setWindowState("normal");
				lblcesNombreSol.setValue(v.getId().getNombresol().trim());
				lblcesNomCompania.setValue(v.getId().getCodcomp().trim()+" "+v.getId().getNombrecomp());
				lblcesFechasol.setValue(v.getId().getFsolicitud());
				lblcesOperacion.setValue(v.getId().getToperacion().trim());
				lblcesMonto.setValue(dv.formatDouble(v.getMonto().doubleValue())+ " " + v.getId().getMoneda());
				
				if(iEstado == 1){
					lblConfirmCamEstado.setValue("¿Confirma Denegar la Solicitud?");
					m.put("aps_CambiarEstadoS", "1");
				}
				else if(iEstado == 2){
					lblConfirmCamEstado.setValue("¿Confirma Aprobar la Solicitud?");
					m.put("aps_CambiarEstadoS", "2");
				}
			}else{
				dwProcesa.setWindowState("normal");
				lblMsgValidaAprSalidas.setValue("Solo puede Aprobar o Denegar solicitudes en estado 'PENDIENTE'");
			}
			
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
/*******************************************************************************/
/**				 Mostrar el detalle de la salida de caja					  **/	
	public void mostrarDetalleSalida(ActionEvent ev){
		RowItem ri = null;
		Vsalida v = new Vsalida();
		
		try {
			ri = (RowItem)ev.getComponent().getParent().getParent();
			v = (Vsalida)gvSalidas.getDataRow(ri);
			
			//---------- Establecer valores de la ventana de detalle.
			jsppSalidaCheques.setRendered(false);
			lbldsFsalida.setValue(v.getId().getFsolicitud());
			lbldsNoSalida.setValue(v.getId().getNumsal());
			lbldsCodSol.setValue(v.getId().getCodsol());
			lbldsMoneda.setValue(v.getId().getMoneda());
			lbldsNombreSol.setValue(v.getId().getNombresol());
			lbldsTasa.setValue(v.getId().getTasa().toString().substring(0,7));

			lbldsCompania.setValue(v.getId().getNombrecomp().trim());
			lbldsOperacion.setValue(v.getId().getToperacion());
			lbldsMonto.setValue(v.getId().getMonto());
			lbldsEstado.setValue(v.getId().getSestado());
			txtdsConceptoSalida.setValue(v.getId().getConcepto());
			
			if(v.getId().getOperacion().equals(MetodosPagoCtrl.CHEQUE)){
				jsppSalidaCheques.setRendered(true);
				lbldsNocheque.setValue(v.getId().getRefer2());
				lbldsBanco.setValue(v.getId().getNbanco());
				lbldsPortador.setValue(v.getId().getRefer3());
				lbldsEmisor.setValue(v.getId().getRefer4());
			}
			if(v.getId().getEstado().equals("P")){
				ReciboCtrl rcCtrl = new ReciboCtrl();
				Recibojde rj = new Recibojde();
				List<Recibojde> lstRecibojde = rcCtrl.getEnlaceReciboJDE(v.getId().getCaid(), v.getId().getCodsuc(),
														v.getId().getCodcomp(),v.getId().getNumsal(), "S");
				if(lstRecibojde!=null && lstRecibojde.size()>0){
					rj = (Recibojde)lstRecibojde.get(0);
					jpPanelContabds.setRendered(true);
					lbldsNobatch.setValue(rj.getId().getNobatch());
					lbldsNodoc.setValue(rj.getId().getRecjde());
				}
			}
			dwDetalleSalida.setWindowState("normal");
			
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
	
/************************************************************************************/
/******		 RESTABLECER LOS VALORES INICIALES DE LA PANTALLA DE SALIDAS 	*********/	
	public void restablecerVistaSalidas(ActionEvent ev){
		restablecerVistaSalidas();
	}
	public void restablecerVistaSalidas(){
		try {
			m.remove("aps_lstSalidas");
			ddlFiltroCompanias.dataBind();
			ddlFiltroEstado.dataBind();
			ddlFiltroMonedas.dataBind();
			ddlTipoBusqueda.dataBind();			
			gvSalidas.dataBind();
			getLstSalidas();
			if(lstSalidas == null || lstSalidas.size()==0)
				lblMsgExistSalida.setValue("No se han encontrado registro de salidas para esta caja");
			else
				lblMsgExistSalida.setValue("");
			
			SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
			srm.addSmartRefreshId(ddlFiltroCompanias.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(ddlFiltroEstado.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(ddlFiltroMonedas.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(ddlTipoBusqueda.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(gvSalidas.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblMsgExistSalida.getClientId(FacesContext.getCurrentInstance()));
			
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
	
/************************************************************************************/
/******		FILTRAR LAS SALIDAS A PARTIR DE LOS PARAMETROS EN PANTALLA		*********/
	public void filtrarSalidas(ValueChangeEvent ev){
		filtrarSalidas();
	}
	public void filtrarSalidas(ActionEvent ev){
		filtrarSalidas();
	}
	public void filtrarSalidas(){
		String sParametro,sCompania,sMoneda = "",sEstado = "",sBusqueda, sConSQL;
		Pattern pNum = null;

		boolean bLimSql = true;
		
		try {
			List lstCajas = (List)m.get("lstCajas");
    		Vf55ca01 f5 = ((Vf55ca01)lstCajas.get(0));
    		pNum = Pattern.compile("^[0-9]+$");
    		lblMsgExistSalida.setValue("");
    		
			sBusqueda  = ddlTipoBusqueda.getValue().toString();
			sParametro = txtParametro.getValue().toString().trim();
			sCompania  = ddlFiltroCompanias.getValue().toString();
			sMoneda    = ddlFiltroMonedas.getValue().toString();
			sEstado	   = ddlFiltroEstado.getValue().toString();
			
			//---- Consulta Básica.
			sConSQL =  " from Vsalida v where v.id.caid = "+f5.getId().getCaid();
			sConSQL += " and trim(v.id.codcomp) = '"+sCompania.trim()+"' and trim(v.id.codsuc)= '"+f5.getId().getCaco().trim()+"'";
			
			//----- Agregar filtro por tipo de búsqueda.
			if(!sParametro.trim().equals("")){
				if(sBusqueda.equals("3")){
					if(pNum.matcher(sParametro).matches()){
						sConSQL += " and v.id.numsal = "+sParametro;
						bLimSql = false;
					}
				}else{
					if(sParametro.length()>3 && sParametro.contains("=>")){
						String sSolic[] = sParametro.split("=>");
						if(sSolic.length>0 && pNum.matcher(sSolic[0].trim()).matches()){
							sConSQL += " and v.id.codsol = "+sSolic[0];
							bLimSql = false;
						}
					}
				}
			}
			//---- Agregar filtro de salidas por Moneda y por estado.
			if(!sMoneda.equals("SM")){
				sConSQL += " and v.id.moneda = '"+sMoneda+"'";
				bLimSql = false;
			}
			if(!sEstado.equals("SE")){
				sConSQL += " and v.id.estado = '"+sEstado+"'";
				bLimSql = false;
			}
			
			sConSQL += " order by v.id.fsolicitud desc";			
			int iMaxResult = bLimSql ? 40:0;
			
			lstSalidas = SalidasCtrl.obtenerSalidas(sConSQL, iMaxResult);
			
			if(lstSalidas!=null && lstSalidas.size()>0){
				for(int i=0;i<lstSalidas.size();i++){
					Vsalida v = (Vsalida)lstSalidas.get(i);
					v.setMonto(v.getId().getMonto());
					lstSalidas.remove(i);
					lstSalidas.add(i,v);
				}
			}else{
				lstSalidas = new ArrayList();
				lblMsgExistSalida.setValue("No se han encontrado registro de salidas para esta caja");
			}
			m.put("aps_lstSalidas", lstSalidas);
			gvSalidas.setPageIndex(0);
			gvSalidas.dataBind();
			
		} catch (Exception error) {
			error.printStackTrace();
		} 
	}
/************************************************************************************/
/************* 		ESTABLECER EL FILTRO PARA BUSCAR AL SOLICITANTE		*************/	
		public void settipoBusquedaCliente(ValueChangeEvent e){
			try {			
				String strBusqueda = ddlTipoBusqueda.getValue().toString();
				m.put("pr_strBusquedaPrima", strBusqueda);
			} catch (Exception error) {
				error.printStackTrace();
			}
		}
/***************************************************************************************/
/************************ Cerrar las ventanas emergentes  ******************************/
	public void cerrarProcesa(ActionEvent ev){
		dwProcesa.setWindowState("hidden");
	}
	public void cerrarDetalleSalida(ActionEvent ev){
		dwDetalleSalida.setWindowState("hidden");
	}
	public void cancelarCambiarEstadoSalida(ActionEvent ev){
		m.remove("aps_CEsalida");
		m.remove("aps_CambiarEstadoS");
		dwCambioEstadoSalida.setWindowState("hidden");
	}
	public void cerrarValidaAprSalida(ActionEvent ev){
		dwProcesa.setWindowState("hidden");
	}
	
	//-------------------- GETTERS Y SETTERS -------------------------//
	public HtmlDropDownList getDdlFiltroCompanias() {
		return ddlFiltroCompanias;
	}
	public void setDdlFiltroCompanias(HtmlDropDownList ddlFiltroCompanias) {
		this.ddlFiltroCompanias = ddlFiltroCompanias;
	}
	public HtmlDropDownList getDdlTipoBusqueda() {
		return ddlTipoBusqueda;
	}
	public void setDdlTipoBusqueda(HtmlDropDownList ddlTipoBusqueda) {
		this.ddlTipoBusqueda = ddlTipoBusqueda;
	}
	public HtmlGridView getGvSalidas() {
		return gvSalidas;
	}
	public void setGvSalidas(HtmlGridView gvSalidas) {
		this.gvSalidas = gvSalidas;
	}
	public HtmlOutputText getLblMsgExistSalida() {
		return lblMsgExistSalida;
	}
	public void setLblMsgExistSalida(HtmlOutputText lblMsgExistSalida) {
		this.lblMsgExistSalida = lblMsgExistSalida;
	}
	public String getLblMsgExistSalida1() {
		try {
			lblMsgExistSalida1 = m.get("aps_MsgExistSalidas")== null? "": m.get("aps_MsgExistSalidas").toString();
		} catch (Exception error) {
			error.printStackTrace();
		}
		return lblMsgExistSalida1;
	}
	public void setLblMsgExistSalida1(String lblMsgExistSalida1) {
		this.lblMsgExistSalida1 = lblMsgExistSalida1;
	}
	public List getLstFiltroCompanias() {
		try {
			if(m.get("aps_lstFiltroCompanias") == null) {			
				lstFiltroCompanias = new ArrayList();
				List lstCajas = (List)m.get("lstCajas");
				CompaniaCtrl compCtrl = new CompaniaCtrl();
				F55ca014[] f55ca014 = null;
				
				f55ca014 = compCtrl.obtenerCompaniasxCaja(((Vf55ca01)lstCajas.get(0)).getId().getCaid());
				for (int i = 0; i < f55ca014.length; i ++){	
					lstFiltroCompanias.add(new SelectItem(f55ca014[i].getId().getC4rp01(),f55ca014[i].getId().getC4rp01d1()));
				}
				m.put("aps_lstFiltroCompanias",lstFiltroCompanias);			
			} else {
				lstFiltroCompanias = (List)m.get("aps_lstFiltroCompanias");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return lstFiltroCompanias;
	}
	public void setLstFiltroCompanias(List lstFiltroCompanias) {
		this.lstFiltroCompanias = lstFiltroCompanias;
	}
	public List getLstSalidas() {
		try {
			if(m.get("aps_lstSalidas") == null){
				SalidasCtrl sCtrl = new SalidasCtrl();
				List lstSal = new ArrayList();
				List lstCajas = (List)m.get("lstCajas");
	    		Vf55ca01 f5 = ((Vf55ca01)lstCajas.get(0));
	    		String sCodcomp = "";
	    		
	    		lstSalidas = new ArrayList();
	    		
	    		if(lstFiltroCompanias==null){
	    			getLstFiltroCompanias();
	    		}
	    		SelectItem si = (SelectItem)lstFiltroCompanias.get(0);
    			sCodcomp = si.getValue().toString();
	    		
	    		lstSal = sCtrl.obtenerSolicitudSalidas(f5.getId().getCaid(), sCodcomp,	f5.getId().getCaco(),0,0,"","E",40,null,null,null);
	    		if(lstSal != null && lstSal.size()>0){
	    			 m.remove("aps_MsgExistSalidas");
					for(int i=0;i<lstSal.size();i++){
						Vsalida v = (Vsalida)lstSal.get(i);						
						v.setMonto(v.getId().getMonto());
						lstSalidas.add(v);
					}
				}else{
					 m.put("aps_MsgExistSalidas","No se han encontrado registro de salidas para esta caja");
				}
				m.put("aps_lstSalidas", lstSalidas);
			}else
				lstSalidas = (ArrayList)m.get("aps_lstSalidas");
		} catch (Exception error) {
			error.printStackTrace();
		} 
		return lstSalidas;
	}
	public void setLstSalidas(List lstSalidas) {
		this.lstSalidas = lstSalidas;
	}
	public List getLstTipoBusquedaCliente() {
		try{
			if(lstTipoBusquedaCliente == null){
				lstTipoBusquedaCliente = new ArrayList();	
				lstTipoBusquedaCliente.add(new SelectItem("1","Nombre Solicitante","Búsqueda por nombre de cliente"));
				lstTipoBusquedaCliente.add(new SelectItem("2","Código Solicitante","Búsqueda por código de cliente"));
				lstTipoBusquedaCliente.add(new SelectItem("3","No de Salida","Búsqueda el número de salida"));
			}
		}catch(Exception error){
			error.printStackTrace();
		}
		return lstTipoBusquedaCliente;
	}
	public void setLstTipoBusquedaCliente(List lstTipoBusquedaCliente) {
		this.lstTipoBusquedaCliente = lstTipoBusquedaCliente;
	}
	public HtmlInputText getTxtParametro() {
		return txtParametro;
	}
	public void setTxtParametro(HtmlInputText txtParametro) {
		this.txtParametro = txtParametro;
	}
	public HtmlDropDownList getDdlFiltroEstado() {
		return ddlFiltroEstado;
	}
	public void setDdlFiltroEstado(HtmlDropDownList ddlFiltroEstado) {
		this.ddlFiltroEstado = ddlFiltroEstado;
	}
	public HtmlDropDownList getDdlFiltroMonedas() {
		return ddlFiltroMonedas;
	}
	public void setDdlFiltroMonedas(HtmlDropDownList ddlFiltroMonedas) {
		this.ddlFiltroMonedas = ddlFiltroMonedas;
	}
	public List getLstFiltroEstado() {
		try {
			if(m.get("aps_lstFiltroEstado")== null){
				lstFiltroEstado  = new ArrayList();
				SalidasCtrl sCtrl = new SalidasCtrl();
				List lstEstados = sCtrl.leerValorCatalogo(7);
				
				lstFiltroEstado.add(new SelectItem("SE","Estados","Seleccione el estado de las solicitudes"));
				if(lstEstados !=null && lstEstados.size()>0){
					for(int i=0; i<lstEstados.size();i++){
						Valorcatalogo v = (Valorcatalogo)lstEstados.get(i);
						lstFiltroEstado.add(new SelectItem(v.getCodigointerno(),
										v.getDescripcion(),v.getDescripcion()));
					}
				}
				m.put("aps_lstFiltroEstado", lstFiltroEstado);
			}else
				lstFiltroEstado = (ArrayList)m.get("aps_lstFiltroEstado");
		} catch (Exception error) {
			error.printStackTrace();
		}		
		return lstFiltroEstado;
	}
	public void setLstFiltroEstado(List lstFiltroEstado) {
		this.lstFiltroEstado = lstFiltroEstado;
	}
	
	@SuppressWarnings("unchecked")
	public List<SelectItem> getLstFiltroMoneda() {

		try{	
			
			if(CodeUtil.getFromSessionMap("aps_lstFiltroMoneda") != null)
				return (ArrayList<SelectItem>)CodeUtil.getFromSessionMap("aps_lstFiltroMoneda");
			
			lstFiltroMoneda = new ArrayList<SelectItem>();
			lstFiltroMoneda.add(new SelectItem("SM","Moneda","Seleccione la moneda para filtrar Salidas de caja"));
			
			List<String[]> lstMon = MonedaCtrl.obtenerMonedasCajaJde();
			
			if( lstMon == null || lstMon.isEmpty())
				return lstFiltroMoneda;
			
			for (String[] moneda : lstMon) {
				lstFiltroMoneda.add(new SelectItem(moneda[0], moneda[1], moneda[0] +" "+ moneda[1]));
			}
				 
	 
		}catch(Exception error){
			error.printStackTrace(); 
		}finally{
			
			CodeUtil.putInSessionMap("aps_lstFiltroMoneda", lstFiltroMoneda) ;
			
		}
		return lstFiltroMoneda;
	}
	public void setLstFiltroMoneda(List<SelectItem> lstFiltroMoneda) {
		this.lstFiltroMoneda = lstFiltroMoneda;
	}
	public HtmlJspPanel getJsppSalidaCheques() {
		return jsppSalidaCheques;
	}
	public void setJsppSalidaCheques(HtmlJspPanel jsppSalidaCheques) {
		this.jsppSalidaCheques = jsppSalidaCheques;
	}
	public HtmlOutputText getLbldsBanco() {
		return lbldsBanco;
	}
	public void setLbldsBanco(HtmlOutputText lbldsBanco) {
		this.lbldsBanco = lbldsBanco;
	}
	public HtmlOutputText getLbldsCodSol() {
		return lbldsCodSol;
	}
	public void setLbldsCodSol(HtmlOutputText lbldsCodSol) {
		this.lbldsCodSol = lbldsCodSol;
	}
	public HtmlOutputText getLbldsCompania() {
		return lbldsCompania;
	}
	public void setLbldsCompania(HtmlOutputText lbldsCompania) {
		this.lbldsCompania = lbldsCompania;
	}
	public HtmlOutputText getLbldsEmisor() {
		return lbldsEmisor;
	}
	public void setLbldsEmisor(HtmlOutputText lbldsEmisor) {
		this.lbldsEmisor = lbldsEmisor;
	}
	public HtmlOutputText getLbldsEstado() {
		return lbldsEstado;
	}
	public void setLbldsEstado(HtmlOutputText lbldsEstado) {
		this.lbldsEstado = lbldsEstado;
	}
	public HtmlOutputText getLbldsFsalida() {
		return lbldsFsalida;
	}
	public void setLbldsFsalida(HtmlOutputText lbldsFsalida) {
		this.lbldsFsalida = lbldsFsalida;
	}
	public HtmlOutputText getLbldsMoneda() {
		return lbldsMoneda;
	}
	public void setLbldsMoneda(HtmlOutputText lbldsMoneda) {
		this.lbldsMoneda = lbldsMoneda;
	}
	public HtmlOutputText getLbldsMonto() {
		return lbldsMonto;
	}
	public void setLbldsMonto(HtmlOutputText lbldsMonto) {
		this.lbldsMonto = lbldsMonto;
	}
	public HtmlOutputText getLbldsNocheque() {
		return lbldsNocheque;
	}
	public void setLbldsNocheque(HtmlOutputText lbldsNocheque) {
		this.lbldsNocheque = lbldsNocheque;
	}
	public HtmlOutputText getLbldsNombreSol() {
		return lbldsNombreSol;
	}
	public void setLbldsNombreSol(HtmlOutputText lbldsNombreSol) {
		this.lbldsNombreSol = lbldsNombreSol;
	}
	public HtmlOutputText getLbldsNoSalida() {
		return lbldsNoSalida;
	}
	public void setLbldsNoSalida(HtmlOutputText lbldsNoSalida) {
		this.lbldsNoSalida = lbldsNoSalida;
	}
	public HtmlOutputText getLbldsOperacion() {
		return lbldsOperacion;
	}
	public void setLbldsOperacion(HtmlOutputText lbldsOperacion) {
		this.lbldsOperacion = lbldsOperacion;
	}
	public HtmlOutputText getLbldsPortador() {
		return lbldsPortador;
	}
	public void setLbldsPortador(HtmlOutputText lbldsPortador) {
		this.lbldsPortador = lbldsPortador;
	}
	public HtmlOutputText getLbldsTasa() {
		return lbldsTasa;
	}
	public void setLbldsTasa(HtmlOutputText lbldsTasa) {
		this.lbldsTasa = lbldsTasa;
	}
	public HtmlInputTextarea getTxtdsConceptoSalida() {
		return txtdsConceptoSalida;
	}
	public void setTxtdsConceptoSalida(HtmlInputTextarea txtdsConceptoSalida) {
		this.txtdsConceptoSalida = txtdsConceptoSalida;
	}
	public HtmlDialogWindow getDwDetalleSalida() {
		return dwDetalleSalida;
	}
	public void setDwDetalleSalida(HtmlDialogWindow dwDetalleSalida) {
		this.dwDetalleSalida = dwDetalleSalida;
	}
	public HtmlDialogWindow getDwProcesa() {
		return dwProcesa;
	}
	public void setDwProcesa(HtmlDialogWindow dwProcesa) {
		this.dwProcesa = dwProcesa;
	}
	public HtmlDialogWindow getDwCambioEstadoSalida() {
		return dwCambioEstadoSalida;
	}
	public void setDwCambioEstadoSalida(HtmlDialogWindow dwCambioEstadoSalida) {
		this.dwCambioEstadoSalida = dwCambioEstadoSalida;
	}
	public HtmlOutputText getLblcesFechasol() {
		return lblcesFechasol;
	}
	public void setLblcesFechasol(HtmlOutputText lblcesFechasol) {
		this.lblcesFechasol = lblcesFechasol;
	}
	public HtmlOutputText getLblcesMonto() {
		return lblcesMonto;
	}
	public void setLblcesMonto(HtmlOutputText lblcesMonto) {
		this.lblcesMonto = lblcesMonto;
	}
	public HtmlOutputText getLblcesNombreSol() {
		return lblcesNombreSol;
	}
	public void setLblcesNombreSol(HtmlOutputText lblcesNombreSol) {
		this.lblcesNombreSol = lblcesNombreSol;
	}
	public HtmlOutputText getLblcesNomCompania() {
		return lblcesNomCompania;
	}
	public void setLblcesNomCompania(HtmlOutputText lblcesNomCompania) {
		this.lblcesNomCompania = lblcesNomCompania;
	}
	public HtmlOutputText getLblcesOperacion() {
		return lblcesOperacion;
	}
	public void setLblcesOperacion(HtmlOutputText lblcesOperacion) {
		this.lblcesOperacion = lblcesOperacion;
	}
	public HtmlOutputText getLblConfirmCamEstado() {
		return lblConfirmCamEstado;
	}
	public void setLblConfirmCamEstado(HtmlOutputText lblConfirmCamEstado) {
		this.lblConfirmCamEstado = lblConfirmCamEstado;
	}
	public HtmlOutputText getLblMsgValidaAprSalidas() {
		return lblMsgValidaAprSalidas;
	}
	public void setLblMsgValidaAprSalidas(HtmlOutputText lblMsgValidaAprSalidas) {
		this.lblMsgValidaAprSalidas = lblMsgValidaAprSalidas;
	}
	public HtmlJspPanel getJpPanelContabds() {
		return jpPanelContabds;
	}
	public void setJpPanelContabds(HtmlJspPanel jpPanelContabds) {
		this.jpPanelContabds = jpPanelContabds;
	}
	public HtmlOutputText getLbldsNobatch() {
		return lbldsNobatch;
	}
	public void setLbldsNobatch(HtmlOutputText lbldsNobatch) {
		this.lbldsNobatch = lbldsNobatch;
	}
	public HtmlOutputText getLbldsNodoc() {
		return lbldsNodoc;
	}
	public void setLbldsNodoc(HtmlOutputText lbldsNodoc) {
		this.lbldsNodoc = lbldsNodoc;
	}
}
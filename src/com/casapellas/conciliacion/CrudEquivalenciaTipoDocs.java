package com.casapellas.conciliacion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlInputTextarea;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import com.casapellas.controles.BancoCtrl;
import com.casapellas.controles.CrudEquivalenciaCtrl;
import com.casapellas.controles.MetodosPagoCtrl;
import com.casapellas.entidades.Equivtipodocs;
import com.casapellas.entidades.F55ca022;
import com.casapellas.entidades.Metpago;
import com.infragistics.faces.grid.component.RowItem;
import com.infragistics.faces.grid.component.html.HtmlGridView;
import com.infragistics.faces.input.component.html.HtmlCheckBox;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
import com.infragistics.faces.shared.component.html.HtmlLink;
import com.infragistics.faces.shared.smartrefresh.SmartRefreshManager;
import com.infragistics.faces.window.component.html.HtmlDialogWindow;
import com.infragistics.faces.window.component.html.HtmlDialogWindowHeader;
import com.casapellas.entidades.ens.Vautoriz;

/**
 * CASA PELLAS S.A.
 * Creado por.........: Carlos Manuel Hernández Morrison
 * Fecha de Creación..: 11/08/2011
 * Última modificación: Carlos Manuel Hernández Morrison
 * Modificado por.....:	11/08/2011
 * Descripción:.......: Configuraciones de tipos de documentos de banco 
 * 						con tipos de documentos de caja. 
 */
public class CrudEquivalenciaTipoDocs {
	Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	private List<Equivtipodocs> lstEquivalenciasTdocs;
	private HtmlGridView gvEquivalenciasDocs;
	private HtmlDialogWindow dwAgregarNuevaEquiv;
	private HtmlDialogWindowHeader hdNuevaConfigEquiv;
	private HtmlInputTextarea txtDescripBco;
	private HtmlInputText txtCodigoBco, txtNombreCodigo;
	private HtmlDropDownList ddlCodigoCaja, ddlBancos;
	private List<SelectItem>lstCodigosCaja, lstBancosDisp;
	private HtmlOutputText lblMsgNuevaconf;
	private HtmlLink lnkEditarEquivalencia,lnkAgregarEquivalencia;
	private HtmlCheckBox chkInactivarConfig;
	private HtmlOutputText lbletInactivarConfig;
/******************************************************************************************/
/** Método: Refrescar la vista de los datos en pantalla.
 *	Fecha:  15/08/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */	
	public void refrescarDatosPantalla(ActionEvent ev){
		try {
			
			m.remove("ceq_lstEquivalenciasTdocs");
			getLstEquivalenciasTdocs();
			gvEquivalenciasDocs.dataBind();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
/******************************************************************************************/
/** Método: Validar los datos a usar en la configuracion
 *	Fecha:  15/08/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */	
	public String validarDatosConfiguracion(String sNombre, String sCodigo,String sIdBanco,
											String sDescripBco, String sIdEquivCaja, String sNomDocCaja){
		String sMensaje = "";
		try {
			if(!sCodigo.toUpperCase().matches("^[A-Z]{1,3}$")){
				return "Se requiere valor de 1 a 3 carácteres para el campo código";
			}
			if(!sNombre.toUpperCase().matches("^[A-Za-z0-9-.;,\\p{Blank}]{1,100}$")){
				return "El nombre debe contener de 1 a 100 Caracteres";
			}
			if(!sDescripBco.toUpperCase().matches("^[A-Z0-9-.;,\\p{Blank}]{1,150}$")){
				return "Campo Descripción debe contener de 1 a 150 Caracteres" ;
			}
			if(sIdBanco.toUpperCase().equals("SB")){
				return "Seleccione el banco emisor del codigo";
			}
			if(sIdEquivCaja.toUpperCase().equals("SM")){
				return "Seleccione la equivalencia en sistema de caja";
			}

			//=== Validar la configuracion contra las ya existentes.
			Equivtipodocs eq = null;
			if(m.get("etc_EditarEquivtipodocs")!=null){
				Equivtipodocs ect = (Equivtipodocs)m.get("etc_EditarEquivtipodocs");
				if(!ect.getCodigo().trim().toLowerCase().equals(sCodigo.trim().toLowerCase()) &&
				    ect.getIdbanco() != Integer.parseInt(sIdBanco) ){
					eq = new CrudEquivalenciaCtrl().buscarEquivalenciaxId(sCodigo, Integer.parseInt(sIdBanco));
				}
			}else{
				eq = new CrudEquivalenciaCtrl().buscarEquivalenciaxId(sCodigo, Integer.parseInt(sIdBanco));
			}
			if(eq!=null){
				return "Ya existe configuración activa de código para el banco, #"+eq.getCodigo();
			}
			
		} catch (Exception e) {
			sMensaje = "Error de aplicación al validar datos configuración";
			e.printStackTrace();
		}
		return sMensaje;
	}
/******************************************************************************************/
/** Método: Editar los datos de la configuraci[on de equivalencias.
 *	Fecha:  15/08/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */	
	public void editarDatosConfigEquiv(ActionEvent ev){
		String sCodigo, sNombre,sDescripBco;
		String sIdBanco,sIdEquivCaja, sNomDocCaja;
		CrudEquivalenciaCtrl cec = new CrudEquivalenciaCtrl();
		Equivtipodocs etd = null;
		boolean bActivo=true;
		
		try {
			etd =	(Equivtipodocs) m.get("etc_EditarEquivtipodocs");
			sCodigo 	 = txtCodigoBco.getValue().toString().trim();
			sNombre		 = txtNombreCodigo.getValue().toString().trim();
			sDescripBco  = txtDescripBco.getValue().toString().trim();
			sIdBanco	 = ddlBancos.getValue().toString().trim();
			sIdEquivCaja = ddlCodigoCaja.getValue().toString().split("@")[0];
			sNomDocCaja  = ddlCodigoCaja.getValue().toString().split("@")[1];
			bActivo		 = chkInactivarConfig.isChecked();	
			
			lblMsgNuevaconf.setStyle("color:red");
			String sMensaje = validarDatosConfiguracion(sNombre, sCodigo, sIdBanco, sDescripBco, sIdEquivCaja, sNomDocCaja);
			if(!sMensaje.equals("")){
				lblMsgNuevaconf.setValue(sMensaje);
				return;
			}
			
			boolean bHecho = cec.editarConfiguracionEquiv(etd, sNombre, sCodigo, Integer.parseInt(sIdBanco), 
														sDescripBco, sIdEquivCaja, sNomDocCaja, 
														((Vautoriz[])m.get("sevAut"))[0].getId().getCodreg(),bActivo);
			if(bHecho){
				m.remove("etc_EditarEquivtipodocs");
				dwAgregarNuevaEquiv.setWindowState("hidden");
				m.remove("ceq_lstEquivalenciasTdocs");
				getLstEquivalenciasTdocs();
				gvEquivalenciasDocs.dataBind();
			}else{
				lblMsgNuevaconf.setStyle("color:red");
				lblMsgNuevaconf.setValue("No se pudo realizar petición: "+ ((cec.getError()!=null)? 
											cec.getError().getCause():" Error desconocido de aplicación"));
			}
			
		} catch (Exception e) {
			dwAgregarNuevaEquiv.setWindowState("hidden");
			e.printStackTrace();
		}
	}
/******************************************************************************************/
/** Método: Validar los datos y agregar la nueva configuracion de equivalencias.
 *	Fecha:  15/08/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */	
	public void agregarNuevaConfigEquiv(ActionEvent ev){
		String sCodigo, sNombre,sDescripBco;
		String sIdBanco,sIdEquivCaja, sNomDocCaja;
		CrudEquivalenciaCtrl cec = new CrudEquivalenciaCtrl();
		
		try {
			sCodigo 	 = txtCodigoBco.getValue().toString().trim();
			sNombre		 = txtNombreCodigo.getValue().toString().trim();
			sDescripBco  = txtDescripBco.getValue().toString().trim();
			sIdBanco	 = ddlBancos.getValue().toString().trim();
			sIdEquivCaja = ddlCodigoCaja.getValue().toString().split("@")[0];
			sNomDocCaja  = ddlCodigoCaja.getValue().toString().split("@")[1];
			
			lblMsgNuevaconf.setStyle("color:red");
			String sMensaje = validarDatosConfiguracion(sNombre, sCodigo, sIdBanco, sDescripBco, sIdEquivCaja, sNomDocCaja);
			if(!sMensaje.equals("")){
				lblMsgNuevaconf.setValue(sMensaje);
				return;
			}
			
			boolean bHecho = cec.agregarEquivalencia(sNombre, sCodigo, Integer.parseInt(sIdBanco), 
													sDescripBco, sIdEquivCaja, sNomDocCaja,
													((Vautoriz[])m.get("sevAut"))[0].getId().getCodreg());
			if(bHecho){
				lblMsgNuevaconf.setStyle("color:green");
				lblMsgNuevaconf.setValue("Se ha guardado correctamente el registro");
				m.remove("ceq_lstEquivalenciasTdocs");
				getLstEquivalenciasTdocs();
				gvEquivalenciasDocs.dataBind();
			}else{
				lblMsgNuevaconf.setStyle("color:red");
				lblMsgNuevaconf.setValue("No se pudo realizar petición: "+ ((cec.getError()!=null)? 
											cec.getError().getMessage():" Error desconocido de aplicación"));
			}
		} catch (Exception e) {
			lblMsgNuevaconf.setStyle("color:red");
			lblMsgNuevaconf.setValue("Error de aplicación: "+e.getMessage());
			e.printStackTrace();
		}
	}
	/******************************************************************************************/
	/** Método: Mostrar la ventana emergente para agregar nuevas configuraciones.
	 *	Fecha:  25/06/2010
	 *  Nombre: Carlos Manuel Hernández Morrison.
	 */
	public void limpiarDatosConfig(){
		SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
		try {
			hdNuevaConfigEquiv.setCaptionAlignment("left");
			hdNuevaConfigEquiv.setCaptionText("Agregar nueva configuración de equivalencia");
			
			txtDescripBco.setStyle("frmInput2");
			txtCodigoBco.setValue("frmInput2ddl");
			txtNombreCodigo.setValue("frmInput2ddl");
			
			txtDescripBco.setValue("");
			txtCodigoBco.setValue("");
			txtNombreCodigo.setValue("");
			ddlCodigoCaja.dataBind();
			ddlBancos.dataBind();
			lblMsgNuevaconf.setValue("");
			lblMsgNuevaconf.setStyle("color:green");
			
			srm.addSmartRefreshId(txtDescripBco.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(txtCodigoBco.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(txtNombreCodigo.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(ddlCodigoCaja.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(ddlBancos.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblMsgNuevaconf.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(hdNuevaConfigEquiv.getClientId(FacesContext.getCurrentInstance()));
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/******************************************************************************************/
	/** Método: Mostrar la ventana emergente para agregar nuevas configuraciones.
	 *	Fecha:  25/06/2010
	 *  Nombre: Carlos Manuel Hernández Morrison.
	 */
	public void mostrarEditarConfiguracion(ActionEvent ev){
		try {
			RowItem ri = (RowItem)ev.getComponent().getParent().getParent();
			Equivtipodocs etd =	(Equivtipodocs)gvEquivalenciasDocs.getDataRow(ri);
			
			hdNuevaConfigEquiv.setCaptionAlignment("left");
			hdNuevaConfigEquiv.setCaptionText("Editar configuración "+etd.getNombre().trim());
			dwAgregarNuevaEquiv.setWindowState("normal");
			lnkEditarEquivalencia.setStyle("display:inline");
			lnkAgregarEquivalencia.setStyle("display:none");
			chkInactivarConfig.setStyle("display:inline");
			lbletInactivarConfig.setStyle("display:inline");
			
			txtCodigoBco.setValue(etd.getCodigo());
			txtNombreCodigo.setValue(etd.getNombre());
			txtDescripBco.setValue(etd.getDescripbco());
			ddlBancos.setValue(etd.getIdbanco());
			ddlCodigoCaja.setValue(etd.getCoddoccaja()+"@"+etd.getNomdoccaja());
			lblMsgNuevaconf.setValue("");
			m.put("etc_EditarEquivtipodocs", etd);
			
		} catch (Exception e) {
			dwAgregarNuevaEquiv.setWindowState("hidden");
			e.printStackTrace();
		}
	}
	/******************************************************************************************/
	/** Método: Mostrar la ventana emergente para agregar nuevas configuraciones.
	 *	Fecha:  25/06/2010
	 *  Nombre: Carlos Manuel Hernández Morrison.
	 */
	public void mostrarAgregaNvaConfig(ActionEvent ev){
		try {
			limpiarDatosConfig();
			lnkEditarEquivalencia.setStyle("display:none");
			lnkAgregarEquivalencia.setStyle("display:inline");
			chkInactivarConfig.setStyle("display:none");
			lbletInactivarConfig.setStyle("display:none");
			dwAgregarNuevaEquiv.setWindowState("normal");
		} catch (Exception e) {
			dwAgregarNuevaEquiv.setWindowState("hidden");
			e.printStackTrace();
		}
	}
	
	//&& ==============  CERRAR LAS VENTANAS EMERGENTES ============== &&//
	public void cerrarAgregarNuevaConfig(ActionEvent ev){
		dwAgregarNuevaEquiv.setWindowState("hidden");
		m.remove("etc_EditarEquivtipodocs");
	}
	//&& ==============  GETTERS Y SETTERS ============== &&//
	@SuppressWarnings("unchecked")
	public List<Equivtipodocs> getLstEquivalenciasTdocs() {
		if(m.get("ceq_lstEquivalenciasTdocs")==null){
			CrudEquivalenciaCtrl ceq = new CrudEquivalenciaCtrl();
			List<Equivtipodocs> lstTipoDocs = ceq.obtenerConfigEquivalencias();
			lstEquivalenciasTdocs = (lstTipoDocs==null)? new ArrayList<Equivtipodocs>(1):lstTipoDocs;
		}else{
			lstEquivalenciasTdocs = (ArrayList<Equivtipodocs>)m.get("ceq_lstEquivalenciasTdocs");
		}		
		return lstEquivalenciasTdocs;
	}
	public void setLstEquivalenciasTdocs(List<Equivtipodocs> lstEquivalenciasTdocs) {
		this.lstEquivalenciasTdocs = lstEquivalenciasTdocs;
	}
	public HtmlGridView getGvEquivalenciasDocs() {
		return gvEquivalenciasDocs;
	}
	public void setGvEquivalenciasDocs(HtmlGridView gvEquivalenciasDocs) {
		this.gvEquivalenciasDocs = gvEquivalenciasDocs;
	}
	public HtmlDialogWindow getDwAgregarNuevaEquiv() {
		return dwAgregarNuevaEquiv;
	}
	public void setDwAgregarNuevaEquiv(HtmlDialogWindow dwAgregarNuevaEquiv) {
		this.dwAgregarNuevaEquiv = dwAgregarNuevaEquiv;
	}
	public HtmlDialogWindowHeader getHdNuevaConfigEquiv() {
		return hdNuevaConfigEquiv;
	}
	public void setHdNuevaConfigEquiv(HtmlDialogWindowHeader hdNuevaConfigEquiv) {
		this.hdNuevaConfigEquiv = hdNuevaConfigEquiv;
	}
	public HtmlInputTextarea getTxtDescripBco() {
		return txtDescripBco;
	}
	public void setTxtDescripBco(HtmlInputTextarea txtDescripBco) {
		this.txtDescripBco = txtDescripBco;
	}
	public HtmlInputText getTxtCodigoBco() {
		return txtCodigoBco;
	}
	public void setTxtCodigoBco(HtmlInputText txtCodigoBco) {
		this.txtCodigoBco = txtCodigoBco;
	}
	public HtmlInputText getTxtNombreCodigo() {
		return txtNombreCodigo;
	}
	public void setTxtNombreCodigo(HtmlInputText txtNombreCodigo) {
		this.txtNombreCodigo = txtNombreCodigo;
	}
	public HtmlDropDownList getDdlCodigoCaja() {
		return ddlCodigoCaja;
	}
	public void setDdlCodigoCaja(HtmlDropDownList ddlCodigoCaja) {
		this.ddlCodigoCaja = ddlCodigoCaja;
	}
	public HtmlDropDownList getDdlBancos() {
		return ddlBancos;
	}
	public void setDdlBancos(HtmlDropDownList ddlBancos) {
		this.ddlBancos = ddlBancos;
	}
	@SuppressWarnings("unchecked")
	public List<SelectItem> getLstCodigosCaja() {
		try {
			if(m.get("ceq_lstCodigosCaja")==null){
				lstCodigosCaja = new ArrayList<SelectItem>();
				lstCodigosCaja.add(new SelectItem("SM@00","Metodo","Selección de método de pago"));
				List<Metpago> lstMetodos = new MetodosPagoCtrl().obtenerMetodosPagoCaja();
				if(lstMetodos!=null){
					for (Metpago pago : lstMetodos) 
						lstCodigosCaja.add(new SelectItem(pago.getId().getCodigo().toString()+"@"+pago.getId().getMpago().trim(),
														  pago.getId().getMpago().trim(),
														  pago.getId().getCodigo()+" "+pago.getId().getMpago().trim()));
				}
				m.put("ceq_lstCodigosCaja", lstCodigosCaja);
			}else{
				lstCodigosCaja = (ArrayList<SelectItem>)(m.get("ceq_lstCodigosCaja"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lstCodigosCaja;
	}
	public void setLstCodigosCaja(List<SelectItem> lstCodigosCaja) {
		this.lstCodigosCaja = lstCodigosCaja;
	}
	@SuppressWarnings("unchecked")
	public List<SelectItem> getLstBancosDisp() {
		try {
			if(m.get("ceq_lstBancosDisp")==null){
				lstBancosDisp = new ArrayList<SelectItem>();
				lstBancosDisp.add(new SelectItem("SB","Banco","Selección de banco"));
				F55ca022[] banco = new BancoCtrl().obtenerBancosConciliar();
				if(banco!=null){
					for (F55ca022 bco : banco) 
						lstBancosDisp.add(new SelectItem(String.valueOf(bco.getId().getCodb()),
									bco.getId().getBanco(),	String.valueOf(bco.getId().getCodb()) +": "+bco.getId().getBanco()));
				}
				m.put("ceq_lstBancosDisp", lstBancosDisp);
			}else{
				lstBancosDisp = (ArrayList<SelectItem>)(m.get("ceq_lstBancosDisp"));
			}
		} catch (Exception e) {
			lstBancosDisp = new ArrayList<SelectItem>();
			e.printStackTrace();
		}
		return lstBancosDisp;
	}
	public void setLstBancosDisp(List<SelectItem> lstBancosDisp) {
		this.lstBancosDisp = lstBancosDisp;
	}
	public HtmlOutputText getLblMsgNuevaconf() {
		return lblMsgNuevaconf;
	}
	public void setLblMsgNuevaconf(HtmlOutputText lblMsgNuevaconf) {
		this.lblMsgNuevaconf = lblMsgNuevaconf;
	}
	public HtmlLink getLnkEditarEquivalencia() {
		return lnkEditarEquivalencia;
	}
	public void setLnkEditarEquivalencia(HtmlLink lnkEditarEquivalencia) {
		this.lnkEditarEquivalencia = lnkEditarEquivalencia;
	}
	public HtmlLink getLnkAgregarEquivalencia() {
		return lnkAgregarEquivalencia;
	}
	public void setLnkAgregarEquivalencia(HtmlLink lnkAgregarEquivalencia) {
		this.lnkAgregarEquivalencia = lnkAgregarEquivalencia;
	}
	public HtmlCheckBox getChkInactivarConfig() {
		return chkInactivarConfig;
	}
	public void setChkInactivarConfig(HtmlCheckBox chkInactivarConfig) {
		this.chkInactivarConfig = chkInactivarConfig;
	}
	public HtmlOutputText getLbletInactivarConfig() {
		return lbletInactivarConfig;
	}
	public void setLbletInactivarConfig(HtmlOutputText lbletInactivarConfig) {
		this.lbletInactivarConfig = lbletInactivarConfig;
	}
}

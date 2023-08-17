package com.casapellas.conciliacion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import com.casapellas.conciliacion.entidades.Vf0911;
import com.casapellas.controles.ConfirmaDepositosCtrl;
import com.casapellas.controles.MonedaCtrl;
import com.casapellas.entidades.Ctaxdeposito;
import com.casapellas.entidades.Vdeposito;
import com.casapellas.reportes.Rptmcaja013XlsCuentaTransitoria;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.Divisas;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.PropertiesSystem;
import com.ibm.icu.util.Calendar;
import com.infragistics.faces.grid.component.RowItem;
import com.infragistics.faces.grid.component.html.HtmlGridView;
import com.infragistics.faces.input.component.html.HtmlCheckBox;
import com.infragistics.faces.input.component.html.HtmlDateChooser;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
import com.infragistics.faces.window.component.html.HtmlDialogWindow;
import com.infragistics.faces.window.component.html.HtmlDialogWindowHeader;
/**
 * CASA PELLAS S.A.
 * Creado por.........: Carlos Manuel Hernández Morrison
 * Fecha de Creación..: 04/06/2012
 * Última modificación: Carlos Manuel Hernández Morrison
 * Modificado por.....:	04/06/2012
 * Descripción:.......: Consultas sobre cuenta transitoria de banco.y 
 */
public class ConsultaCtaTrans {
	Map<String, Object> m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	
	
	private List<Vf0911> lstTransaccionesF0911 ;
	private List<Vdeposito>lstDepositosCaja;
	
	
	
	private List<Ctaxdeposito>lstCtaxdeposito;
	private List<SelectItem>  lstfcEstadoDep;
	private HtmlGridView gvDepositosCaja;
	private HtmlGridView gvCtaxdeposito;
	private List<SelectItem> lstfcMoneda;
	private List<SelectItem> lstfcTipoMov;
	private Date dtFechaIni, dtFechaFin;
	private HtmlDateChooser txtdcFechaIni,txtdcFechaFin;
	private HtmlDropDownList ddlfctipomov, ddlcmfcmoneda;
	private HtmlInputText txtFunegocio, txtFcobjeto, txtFcsubsid;
	private HtmlInputText txtmontodesde, txtmontohasta;
	private HtmlCheckBox chkMostrarTodos;
	private HtmlDialogWindow dwDetalleDeposito;
	private HtmlDialogWindowHeader hdrDetalleDeps;
	private HtmlOutputText ddCaja, ddFechadeps, ddCajero;
	private HtmlOutputText ddNobatch,ddContador,ddDocumento;
	private HtmlOutputText ddCompania,ddMoneda;

	
	private HtmlDialogWindow dwMensajeValidacion ;
	private HtmlOutputText lblMensajeValidacionConsultaCuentas ;
	
	public void dwDetalleDeposito(ActionEvent ev){
		dwDetalleDeposito.setWindowState("hidden");
	}
	
	
	public void cerrarMensajeValidacion(ActionEvent ev) {
		dwMensajeValidacion.setWindowState("hidden");
	} 
	
/******************************************************************************************/
/** Método: Mostrar el detalle del deposito y movimientos de cuenta.
 *	Fecha:  10/06/2012
 *  Nombre: Carlos Manuel Hernández Morrison.
 */	
	@SuppressWarnings({"static-access"})
	public void mostrarDetalleDeps(ActionEvent ev){
		
		try{
			dwDetalleDeposito.setWindowState("normal");
			lstCtaxdeposito = null;
			
			RowItem ri = (RowItem)ev.getComponent().getParent().getParent();
			Vdeposito dp = (Vdeposito)gvDepositosCaja.getDataRow(ri);
			
			ddCaja.setValue(dp.getId().getCaid()+" "+dp.getId().getNomcaja().trim());
			ddFechadeps.setValue(new FechasUtil().formatDatetoString(dp.getId().getFecha(), "dd/MM/yyyy"));
			ddCajero.setValue(dp.getId().getCodcajero()+" "
						+new Divisas().ponerCadenaenMayuscula(dp.getId().getCajero().trim()));
			ddNobatch.setValue(dp.getId().getNobatch());
			ddContador.setValue(dp.getId().getUsrcreate()+" "
						+new Divisas().ponerCadenaenMayuscula(dp.getId().getUsrcreacion().trim()));
			ddDocumento.setValue(dp.getId().getRecjde());
			ddCompania.setValue(dp.getId().getCodcomp().trim()+" "+dp.getId().getNombrecomp().trim());
			ddMoneda.setValue(dp.getId().getMoneda());
			
			List<Ctaxdeposito>lstCtas = new ConfirmaDepositosCtrl().obtenerCuentasDeps(dp.getId().getConsecutivo());
			if(lstCtas == null)
				lstCtas = new ArrayList<Ctaxdeposito>();
			
			lstCtaxdeposito = lstCtas;
			gvCtaxdeposito.dataBind();
			
		} catch (Exception e) {	
			dwDetalleDeposito.setWindowState("hidden");
			e.printStackTrace();
		}
	}
	/******************************************************************************************/
	/** Método: Buscar sobre la cuenta.
	 *	Fecha:  21/09/2011
	 *  Nombre: Carlos Manuel Hernández Morrison.
	 */	
	public void buscarDepositosCuenta(ActionEvent ev){
		boolean valido = true;
		String strMensaje = "" ;
		
		try{
			
			CodeUtil.removeFromSessionMap(new String[]{"cct_lstTransaccionesF0911_Todas", "cct_lstTransaccionesF0911"}) ;
			
			String sUnineg = txtFunegocio.getValue().toString();
			String sCtaObj = txtFcobjeto.getValue().toString();
			String sCtaSub = txtFcsubsid.getValue().toString();
			String TipoMov = ddlfctipomov.getValue().toString();
			String sMoneda = ddlcmfcmoneda.getValue().toString();
			Date dtFechaIni  = (txtdcFechaIni.getValue() == null)? null:(Date)txtdcFechaIni.getValue();
			Date dtFechaFin  = (txtdcFechaFin.getValue() == null)? null:(Date)txtdcFechaFin.getValue();
			
			BigDecimal bdMontoIni = new BigDecimal("0");
			BigDecimal bdMontoFin = new BigDecimal("0");
			String sMontoIni  = txtmontodesde.getValue().toString().trim().replace(",", "");
			String sMontoFin  = txtmontohasta.getValue().toString().trim().replace(",", "");
			
			if(sMontoIni.matches( PropertiesSystem.REGEXP_AMOUNT ) ) bdMontoIni = new BigDecimal(sMontoIni);
			if(sMontoFin.matches( PropertiesSystem.REGEXP_AMOUNT ) ) bdMontoFin = new BigDecimal(sMontoFin);
			
			//&& =========== restricciones de fechas
			if(sUnineg.trim().isEmpty()  || sCtaObj.trim().isEmpty()){
				strMensaje = "Ingrese valores para unidad de negocios y cuenta objeto";
				valido = false ;
				return;
			}
			if(dtFechaIni == null || dtFechaFin == null ){
				valido = false ;
				strMensaje = "Seleccione rango de fechas ";
				return;
			}
			
			if(sMoneda.compareTo("SMDC") == 0) sMoneda = "";
			
			ConfirmaDepositosCtrl cdc = new ConfirmaDepositosCtrl();
			
			lstTransaccionesF0911 = cdc.buscarTransaccionesDeCuenta(sUnineg, sCtaObj, sCtaSub, bdMontoIni, bdMontoFin, dtFechaIni, dtFechaFin, sMoneda, TipoMov , "ZX");
			
			if(lstTransaccionesF0911 == null)
				lstTransaccionesF0911 = new ArrayList<Vf0911>();
			
			CodeUtil.putInSessionMap("cct_lstTransaccionesF0911_Todas", lstTransaccionesF0911);
			
			if(lstTransaccionesF0911.size() > 500 ){
				lstTransaccionesF0911  = new ArrayList<Vf0911>( lstTransaccionesF0911.subList(0, 500) );
			}
			
			CodeUtil.putInSessionMap("cct_lstTransaccionesF0911", lstTransaccionesF0911);
			gvDepositosCaja.dataBind();
			
			
			Rptmcaja013XlsCuentaTransitoria rpt = new Rptmcaja013XlsCuentaTransitoria();
			rpt.transacciones_cuenta = lstTransaccionesF0911;
			rpt.rutafisica="c:\\documento-"+ev.hashCode()+".xlsx";
			rpt.generarExcelTransaccionesCuenta();
			
			 
		} catch (Exception e) {	
			lstDepositosCaja = new ArrayList<Vdeposito>();
			m.put("cct_lstVdeposito", lstDepositosCaja);
			gvDepositosCaja.dataBind();
			 
		}finally{
			
			if(!valido){
				lblMensajeValidacionConsultaCuentas.setValue(strMensaje);
				dwMensajeValidacion.setWindowState("normal");
				CodeUtil.refreshIgObjects(dwMensajeValidacion);
			}
			
		}
	}
	//&&  =================== GETTERS Y SETTERS 
	public List<Vdeposito> getLstDepositosCaja() {
		
		if(m.get("cct_lstVdeposito") == null){
			lstDepositosCaja = new ArrayList<Vdeposito>();
		}else{
			lstDepositosCaja = (ArrayList<Vdeposito>)m.get("cct_lstVdeposito");
		}
		return lstDepositosCaja;
	}
	public void setLstDepositosCaja(List<Vdeposito> lstDepositosCaja) {
		this.lstDepositosCaja = lstDepositosCaja;
	}
	public List<SelectItem> getLstfcEstadoDep() {
		return lstfcEstadoDep;
	}
	public void setLstfcEstadoDep(List<SelectItem> lstfcEstadoDep) {
		this.lstfcEstadoDep = lstfcEstadoDep;
	}
	public HtmlGridView getGvDepositosCaja() {
		return gvDepositosCaja;
	}
	public void setGvDepositosCaja(HtmlGridView gvDepositosCaja) {
		this.gvDepositosCaja = gvDepositosCaja;
	}
	public List<SelectItem> getLstfcMoneda() {
		try {
			if(m.get("cdep_lstfcMoneda") == null){
				lstfcMoneda = new ArrayList<SelectItem>();
//				lstfcMoneda.add(new SelectItem("SMDC","Moneda", "Seleccione no filtrar por moneda!"));
				List<String[]>lstMonedas = MonedaCtrl.leerMonedasJde();
				if(lstMonedas!=null)
					for (String[] moneda : lstMonedas) 
						lstfcMoneda.add(new SelectItem(moneda[0],moneda[0],moneda[0]+" "+moneda[1]));
				m.put("cdep_lstfcMoneda", lstfcMoneda);
			}else{
				lstfcMoneda = (ArrayList<SelectItem>)(m.get("cdep_lstfcMoneda"));
			}
		} catch (Exception e) {
			lstfcMoneda = new ArrayList<SelectItem>();
			lstfcMoneda.add(new SelectItem("SMDC","Moneda","Seleccione no filtrar por moneda!"));
			e.printStackTrace();
		}
		return lstfcMoneda;
	}
	public void setLstfcMoneda(List<SelectItem> lstfcMoneda) {
		this.lstfcMoneda = lstfcMoneda;
	}
	public List<SelectItem> getLstfcTipoMov() {
		if(lstfcTipoMov == null){
			lstfcTipoMov = new ArrayList<SelectItem>(2);
			lstfcTipoMov.add(new SelectItem("01","Ingresos","Despósitos acreditados a la cuenta"));
			lstfcTipoMov.add(new SelectItem("02","Egresos","Despósitos debitados a la cuenta"));
			lstfcTipoMov.add(new SelectItem("03","Todos","Despósitos debitados a la cuenta"));
		}
		return lstfcTipoMov;
	}
	public void setLstfcTipoMov(List<SelectItem> lstfcTipoMov) {
		this.lstfcTipoMov = lstfcTipoMov;
	}
	public Date getDtFechaFin() {
		if( m.get("cct_dtFechaFin") == null){
			dtFechaFin = new Date();
			m.put("cct_dtFechaFin", dtFechaFin);
		}
		return dtFechaFin;
	}
	public Date getDtFechaIni() {
		if( m.get("cct_dtFechaIni") == null){
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, -7);
			dtFechaIni = cal.getTime();
			m.put("cct_dtFechaIni", dtFechaIni);
		}
		return dtFechaIni;
	}
	public HtmlDateChooser getTxtdcFechaIni() {
		return txtdcFechaIni;
	}
	public void setTxtdcFechaIni(HtmlDateChooser txtdcFechaIni) {
		this.txtdcFechaIni = txtdcFechaIni;
	}
	public HtmlDateChooser getTxtdcFechaFin() {
		return txtdcFechaFin;
	}
	public void setTxtdcFechaFin(HtmlDateChooser txtdcFechaFin) {
		this.txtdcFechaFin = txtdcFechaFin;
	}
	public void setDtFechaIni(Date dtFechaIni) {
		this.dtFechaIni = dtFechaIni;
	}
	public void setDtFechaFin(Date dtFechaFin) {
		this.dtFechaFin = dtFechaFin;
	}
	public HtmlDropDownList getDdlfctipomov() {
		return ddlfctipomov;
	}
	public void setDdlfctipomov(HtmlDropDownList ddlfctipomov) {
		this.ddlfctipomov = ddlfctipomov;
	}
	public HtmlDropDownList getDdlcmfcmoneda() {
		return ddlcmfcmoneda;
	}
	public void setDdlcmfcmoneda(HtmlDropDownList ddlcmfcmoneda) {
		this.ddlcmfcmoneda = ddlcmfcmoneda;
	}
	public HtmlInputText getTxtFunegocio() {
		return txtFunegocio;
	}
	public void setTxtFunegocio(HtmlInputText txtFunegocio) {
		this.txtFunegocio = txtFunegocio;
	}
	public HtmlInputText getTxtFcobjeto() {
		return txtFcobjeto;
	}
	public void setTxtFcobjeto(HtmlInputText txtFcobjeto) {
		this.txtFcobjeto = txtFcobjeto;
	}
	public HtmlInputText getTxtFcsubsid() {
		return txtFcsubsid;
	}
	public void setTxtFcsubsid(HtmlInputText txtFcsubsid) {
		this.txtFcsubsid = txtFcsubsid;
	}
	public HtmlInputText getTxtmontodesde() {
		return txtmontodesde;
	}
	public void setTxtmontodesde(HtmlInputText txtmontodesde) {
		this.txtmontodesde = txtmontodesde;
	}
	public HtmlInputText getTxtmontohasta() {
		return txtmontohasta;
	}
	public void setTxtmontohasta(HtmlInputText txtmontohasta) {
		this.txtmontohasta = txtmontohasta;
	}
	public HtmlCheckBox getChkMostrarTodos() {
		return chkMostrarTodos;
	}
	public void setChkMostrarTodos(HtmlCheckBox chkMostrarTodos) {
		this.chkMostrarTodos = chkMostrarTodos;
	}
	public HtmlDialogWindow getDwDetalleDeposito() {
		return dwDetalleDeposito;
	}
	public void setDwDetalleDeposito(HtmlDialogWindow dwDetalleDeposito) {
		this.dwDetalleDeposito = dwDetalleDeposito;
	}
	public HtmlDialogWindowHeader getHdrDetalleDeps() {
		return hdrDetalleDeps;
	}
	public void setHdrDetalleDeps(HtmlDialogWindowHeader hdrDetalleDeps) {
		this.hdrDetalleDeps = hdrDetalleDeps;
	}
	public HtmlOutputText getDdCaja() {
		return ddCaja;
	}
	public void setDdCaja(HtmlOutputText ddCaja) {
		this.ddCaja = ddCaja;
	}
	public HtmlOutputText getDdFechadeps() {
		return ddFechadeps;
	}
	public void setDdFechadeps(HtmlOutputText ddFechadeps) {
		this.ddFechadeps = ddFechadeps;
	}
	public HtmlOutputText getDdCajero() {
		return ddCajero;
	}
	public void setDdCajero(HtmlOutputText ddCajero) {
		this.ddCajero = ddCajero;
	}
	public HtmlOutputText getDdNobatch() {
		return ddNobatch;
	}
	public void setDdNobatch(HtmlOutputText ddNobatch) {
		this.ddNobatch = ddNobatch;
	}
	public HtmlOutputText getDdContador() {
		return ddContador;
	}
	public void setDdContador(HtmlOutputText ddContador) {
		this.ddContador = ddContador;
	}
	public HtmlOutputText getDdDocumento() {
		return ddDocumento;
	}
	public void setDdDocumento(HtmlOutputText ddDocumento) {
		this.ddDocumento = ddDocumento;
	}
	public HtmlOutputText getDdCompania() {
		return ddCompania;
	}
	public void setDdCompania(HtmlOutputText ddCompania) {
		this.ddCompania = ddCompania;
	}
	public HtmlOutputText getDdMoneda() {
		return ddMoneda;
	}
	public void setDdMoneda(HtmlOutputText ddMoneda) {
		this.ddMoneda = ddMoneda;
	}
	public List<Ctaxdeposito> getLstCtaxdeposito() {
		if(lstCtaxdeposito == null)
			lstCtaxdeposito = new ArrayList<Ctaxdeposito>();
		return lstCtaxdeposito;
	}
	public void setLstCtaxdeposito(List<Ctaxdeposito> lstCtaxdeposito) {
		this.lstCtaxdeposito = lstCtaxdeposito;
	}
	public HtmlGridView getGvCtaxdeposito() {
		return gvCtaxdeposito;
	}
	public void setGvCtaxdeposito(HtmlGridView gvCtaxdeposito) {
		this.gvCtaxdeposito = gvCtaxdeposito;
	}
	public List<Vf0911> getLstTransaccionesF0911() {
		
		if(CodeUtil.getFromSessionMap("cct_lstTransaccionesF0911") == null){
			lstTransaccionesF0911 = new ArrayList<Vf0911>();
		}else{
			lstTransaccionesF0911 = (ArrayList<Vf0911>)CodeUtil.getFromSessionMap("cct_lstTransaccionesF0911");
		}
		
		return lstTransaccionesF0911;
	}
	public void setLstTransaccionesF0911(List<Vf0911> lstTransaccionesF0911) {
		this.lstTransaccionesF0911 = lstTransaccionesF0911;
	}
	public HtmlDialogWindow getDwMensajeValidacion() {
		return dwMensajeValidacion;
	}
	public void setDwMensajeValidacion(HtmlDialogWindow dwMensajeValidacion) {
		this.dwMensajeValidacion = dwMensajeValidacion;
	}
	public HtmlOutputText getLblMensajeValidacionConsultaCuentas() {
		return lblMensajeValidacionConsultaCuentas;
	}
	public void setLblMensajeValidacionConsultaCuentas(
			HtmlOutputText lblMensajeValidacionConsultaCuentas) {
		this.lblMensajeValidacionConsultaCuentas = lblMensajeValidacionConsultaCuentas;
	}
	
}

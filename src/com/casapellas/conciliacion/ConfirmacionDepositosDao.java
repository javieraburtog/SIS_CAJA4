package com.casapellas.conciliacion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlInputTextarea;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.casapellas.conciliacion.entidades.Archivo;
import com.casapellas.conciliacion.entidades.Conciliacion;
import com.casapellas.conciliacion.entidades.Depbancodet;
import com.casapellas.controles.BancoCtrl;
import com.casapellas.controles.CompaniaCtrl;
import com.casapellas.controles.ConfirmaDepositosCtrl;
import com.casapellas.controles.CtrlCajas;
import com.casapellas.controles.EmpleadoCtrl;
import com.casapellas.controles.MonedaCtrl;
import com.casapellas.controles.SalidasCtrl;
import com.casapellas.controles.SucursalCtrl;
import com.casapellas.controles.tmp.LecturaConciliacion;
import com.casapellas.conciliacion.entidades.Conciliadet;
import com.casapellas.entidades.Deposito;
import com.casapellas.entidades.F55ca014;
import com.casapellas.entidades.F55ca022;
import com.casapellas.entidades.F55ca023;
import com.casapellas.entidades.F55ca033;
import com.casapellas.entidades.Unegocio;
import com.casapellas.entidades.Valorcatalogo;
import com.casapellas.entidades.Vcompania;
import com.casapellas.entidades.Vdeposito;
import com.casapellas.entidades.Vf55ca01;
import com.casapellas.entidades.ens.Vautoriz;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.navegacion.As400Connection;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.Divisas;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.PropertiesSystem;
import com.infragistics.faces.grid.component.Cell;
import com.infragistics.faces.grid.component.RowItem;
import com.infragistics.faces.grid.component.html.HtmlGridView;
import com.infragistics.faces.input.component.html.HtmlCheckBox;
import com.infragistics.faces.input.component.html.HtmlDateChooser;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
import com.infragistics.faces.shared.component.DataRepeater;
import com.infragistics.faces.shared.component.html.HtmlLink;
import com.infragistics.faces.shared.smartrefresh.SmartRefreshManager;
import com.infragistics.faces.window.component.html.HtmlDialogWindow;
import com.infragistics.faces.window.component.html.HtmlDialogWindowHeader;

/**
 * CASA PELLAS S.A.
 * Creado por.........: Carlos Manuel Hernández Morrison
 * Fecha de Creación..: 24/08/2011
 * Última modificación: Carlos Manuel Hernández Morrison
 * Modificado por.....:	24/08/2010
 * Descripción:.......: Incluir ventana para solicitar datos de confirmacion automatica. 
 */
@SuppressWarnings({"unchecked"})
public class ConfirmacionDepositosDao {
	
	Map<String, Object> m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	private List<Archivo> lstAchivosDepsBco;
	private List<Deposito>lstAchivosDepsCaja;
	private HtmlGridView gvArchivosDepsBanco,gvArchivosDepsCaja;
	private HtmlDialogWindow dwFiltrarArchivosBco;
	private HtmlInputText txtfbNombreArchivo, txtfbReferenciaDep;
	private List<SelectItem>lstfbBancos, lstfbCuentaxBanco,lstfbEstadosArchivo;
	private HtmlDateChooser txtfbFechaIni, txtfbFechaFin;
	private HtmlDropDownList ddlfbBancos, ddlfbCuentaxBanco, ddlfbEstadoArchivo;
	private HtmlOutputText lblMsgResultBusquedaBco;
	private List<Depbancodet>lstDetalleArchivoBanco;
	private HtmlGridView gvDetalleArchivoBanco;
	private HtmlDialogWindowHeader hdDwDetalleArchivo;
	private HtmlDialogWindow dwDetalleArchivoBanco, dwDetalleDepositoCaja;
	
	private HtmlOutputText lblfcCaja,lblfcReferCaja,lblfcCodSuc;
	private HtmlOutputText lblfcMontoDep,lblfcCodComp,lblfcTipoPago,lblfcEstatConfirm;
	private HtmlOutputText lblfcNobatch,lblfcArchivo,lblfcNoDocs;
	
	private HtmlDialogWindow dwFiltrarDepsCaja;	
	private List<SelectItem> lstfcCajas,lstfcCompania,lstfcSucursal,lstfcMoneda;
	private HtmlDropDownList ddlfcCajas,ddlfcCompania,ddlfcSucursal,ddlfcMoneda;
	private HtmlDateChooser txtfcFechaIni,txtfcFechaFin;
	private HtmlInputText txtfcNoReferencia,txtfcMontoDep;
	private HtmlOutputText lblMsgResultBusquedaDepCaja;
	
	//------------------------------------
	
	private HtmlDialogWindow dwImportarFTPBancos;	
	private HtmlInputText txtNumeroCuenta;
	private HtmlDateChooser txtFechaEstadoCuenta;
	private HtmlInputText txtBancoEstadoCuenta;
	private HtmlOutputText lblMsgResultImportarFTPBancos;
//	private HtmlDropDownList ddlfbBancosImportarFTPBancos;
	
	//------------------------------------
	
//	public HtmlDropDownList getDdlfbBancosImportarFTPBancos() {
//		return ddlfbBancosImportarFTPBancos;
//	}
//	public void setDdlfbBancosImportarFTPBancos(HtmlDropDownList ddlfbBancosImportarFTPBancos) {
//		this.ddlfbBancosImportarFTPBancos = ddlfbBancosImportarFTPBancos;
//	}
	public HtmlInputText getTxtNumeroCuenta() {
		return txtNumeroCuenta;
	}
	public void setTxtNumeroCuenta(HtmlInputText txtNumeroCuenta) {
		this.txtNumeroCuenta = txtNumeroCuenta;
	}
	public HtmlDateChooser getTxtFechaEstadoCuenta() {
		return txtFechaEstadoCuenta;
	}
	public void setTxtFechaEstadoCuenta(HtmlDateChooser txtFechaEstadoCuenta) {
		this.txtFechaEstadoCuenta = txtFechaEstadoCuenta;
	}
	public HtmlOutputText getLblMsgResultImportarFTPBancos() {
		return lblMsgResultImportarFTPBancos;
	}
	public void setLblMsgResultImportarFTPBancos(HtmlOutputText lblMsgResultImportarFTPBancos) {
		this.lblMsgResultImportarFTPBancos = lblMsgResultImportarFTPBancos;
	}
	public HtmlInputText getTxtBancoEstadoCuenta() {
		return txtBancoEstadoCuenta;
	}
	public void setTxtBancoEstadoCuenta(HtmlInputText txtBancoEstadoCuenta) {
		this.txtBancoEstadoCuenta = txtBancoEstadoCuenta;
	}
	private HtmlDialogWindow dwDatosProcesoAutomatico, dwResultadoConfirmAuto;
	private HtmlGridView gvArchivoDispConfirm, gvArchivoAConfirmar, gvResultadoConfirmAuto,gvCaDepositosExcluidos;
	private List<Archivo> lstArchivoDispConfirm, lstArchivosAConfirmar;
	private HtmlOutputText lstMsgSelArchivoConfirm, lstMsgSelecDepsConfirmCa, lblCnfAutoFecha;
	private HtmlDateChooser dcCnfAutoFechaConfirma;
	
	private List<SelectItem> lstCaFtBancos;
	private HtmlDropDownList ddlCaFtBancos;
	private HtmlDateChooser dcCaFtFechaIni,dcCaFtFechaFin; 
	
	private HtmlLink lnkMostrarResultadosCA, lnkMostrarResumenCA, lnkConfirmarDepsSelec;	
	private List<CoincidenciaDeposito>lstResultadoConfirmAuto,lstCaDepositosExcluidos;
	private HtmlDialogWindow dwResumenConciliacion;
	private HtmlGridView gvResumenConciliaCA;
	private List<CoincidenciaDeposito> lstResumenConciliaCA;
	
	private HtmlDialogWindow dwDatosConfirmaManual;
	private HtmlGridView gvCmDepositosCaja, gvCmDepositosBco ;
	private List<Deposito> lstCmDepositosCaja;
	private List<Depbancodet> lstCmDepositosBco;
	private HtmlInputText txtCmfbReferenciaDep,txtCmfbMontoDepBco;
	private HtmlDropDownList ddlCmfbBancos;
	private HtmlOutputText lblCmRsmCaMonto,lblCmRsmCaCantDeps,lblCmRsmCaDifer,lblCmRsmCaRngoAjst;
	private HtmlInputTextarea txtCmRsmCaRefers;
	private HtmlCheckBox chkCmNivelReferencia,chkCmNivelMoneda;
	private HtmlCheckBox chkCmNivelTipoTransaccion,chkCmNivelMonto;
	private HtmlOutputText lblCmRsmBcoMonto, lblCmRsmBcoFecha,lblCmRsmBcoMoneda;
	private HtmlOutputText lblCmRsmArchivo, lblCmRsmBcoReferencia, lblCmRsmBcoBancoCuenta;
	private HtmlOutputText lblCnfManualFecha;
	private HtmlDateChooser dcCnfManualFConfirma;
	private HtmlLink lnkCmDcIncluirDcConfirMan, lnkCmDcExcluirDcConfirMan;
	
	private HtmlDialogWindow dwValidacionConfirmaManual, dwNotificacionErrorConfirmManual;
	private HtmlOutputText lblMsgConfirmacionManualDeps, lblMsgNotificaErrorCMD,lblMsgFinConfirmacionManual;
	private HtmlInputText txtCmfcMontoDepCaja,txtCmfcNoReferencia,txtCmfcMontoDesdeDepCaja;
	private HtmlDropDownList ddlCmFcRangoMonto;
	private List<SelectItem> lstCmFcRangoMonto;
	
	private HtmlDialogWindow dwCmAgregarDepositosCaja;
	private HtmlDropDownList ddlCmfcCaja, ddlCmfcCompania;
	private HtmlInputText txtCmAdfcNoReferencia, txtCmfcMontoDep,txtCmfcMontoDepMaxim;
	private HtmlDateChooser txtCmfbFechaIni,txtCmfbFechaFin;
	private HtmlGridView gvCmFcAgregarDpsCaja;
	private List<Deposito> lstCmFcAgregarDpsCaja;
	private HtmlOutputText lblCmAgDcMensaje;
	private HtmlOutputText lblDtFechaDepsC, lblDtDcNoDocoConf;
	private HtmlOutputText lblDtDcNoreferBanco, lblDtDcFechaConfirma;
	private HtmlOutputText lblDtDcNoBatchConf, lblDtDcMontoBanco;
	private HtmlDropDownList ddlCmfcRangoMonto;
	private HtmlOutputText lblCmRsmBcoHistorico;
	private HtmlInputTextarea txtCmRsmBcoHistorico;
	private HtmlDropDownList ddlCaFtrCjaConfirma;
	private HtmlDropDownList ddlCaFtrCjaConfirmaMan;
	
/******************************************************************************************/
/** Método: Usar filtros de busqueda para depositos de caja y agregar mas a confirmacion manual.
 *	Fecha:  21/09/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */	
	public void agregarDepositoCajaConfirmMan(ActionEvent ev){
		try {
			RowItem ri = (RowItem)ev.getComponent().getParent().getParent();

//			Deposito dpSelect = (Deposito)gvCmFcAgregarDpsCaja.getDataRow(ri);
			Deposito dpSelect = (Deposito)DataRepeater.getDataRow(ri);
			
			//&& ======= Borrar de la lista mostrada el deposito seleccionado.
			lstCmFcAgregarDpsCaja = (ArrayList<Deposito>)m.get("cdb_lstCmFcAgregarDpsCaja");
			for (Deposito dpDisponible : lstCmFcAgregarDpsCaja) {
				if(dpDisponible.getConsecutivo() == dpSelect.getConsecutivo()){
					lstCmFcAgregarDpsCaja.remove(dpDisponible);
					break;
				}
			}
			m.put("cdb_lstCmFcAgregarDpsCaja", lstCmFcAgregarDpsCaja);
			gvCmFcAgregarDpsCaja.dataBind();
			
			//&& ======== Actualizar el grid que contiene los depositos de caja en la confirmacion manual.
			lstCmDepositosCaja =  (m.get("cdb_lstCmDepositosCaja")==null)?
									new ArrayList<Deposito>():
									(ArrayList<Deposito>)m.get("cdb_lstCmDepositosCaja");
			lstCmDepositosCaja.add(dpSelect);
			m.put("cdb_lstCmDepositosCaja", lstCmDepositosCaja);
			gvCmDepositosCaja.dataBind();
			
			//&& ======== Verificar los estilos de los links del grid.
			if(m.get("cbd_lstCmDcLstDepsSelect") != null){
				SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
				List<Deposito>lstCmDcLstDepsSelect = (ArrayList<Deposito>)m.get("cbd_lstCmDcLstDepsSelect");
				List filasGrid = gvCmDepositosCaja.getRows();
				for(int i=0; i<filasGrid.size();i++){
					ri = (RowItem)filasGrid.get(i);
					Deposito dpGridActual = (Deposito)gvCmDepositosCaja.getDataRow(ri);
					
					for (Deposito dpListaSeleccion : lstCmDcLstDepsSelect) {
						if(dpGridActual.getConsecutivo() == dpListaSeleccion.getConsecutivo()){
							List lstCeldas = ri.getCells();
							Cell celda = (Cell)lstCeldas.get(0);
							HtmlLink hLnkIncluir = (HtmlLink)celda.getChildren().get(0);
							HtmlLink hLnkExcluir = (HtmlLink)celda.getChildren().get(1);
							hLnkExcluir.setStyle("display:inline");
							hLnkIncluir.setStyle("display:none");
							srm.addSmartRefreshId(hLnkExcluir.getClientId(FacesContext.getCurrentInstance()));
							srm.addSmartRefreshId(hLnkIncluir.getClientId(FacesContext.getCurrentInstance()));
							break;
						}
					}
				}
			}
			lblCmAgDcMensaje.setStyle("color:green");
			lblCmAgDcMensaje.setValue("Se han incluido correctamente el depósito!");
			
		} catch (Exception e) {
			lblCmAgDcMensaje.setStyle("color:red");
			lblCmAgDcMensaje.setValue("No se ha podido incluir el depósito, error de aplicación!");
			e.printStackTrace();
		}
	}
/******************************************************************************************/
/** Método: Usar filtros de busqueda para depositos de caja y agregar mas a confirmacion manual.
 *	Fecha:  21/09/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */	
	public void filtrarDepositosCajaConfirMan(ActionEvent ev){
		try {
			int iCaid = 0;
			int iRangoMonto=0;
			String sCaid		= ddlCmfcCaja.getValue().toString().trim();
			String sCodcomp 	= ddlCmfcCompania.getValue().toString().trim();
			String sReferencia 	= txtCmAdfcNoReferencia.getValue().toString().trim();
			String sMontoDesde 	= txtCmfcMontoDep.getValue().toString().trim();
			String sMontoHasta  = txtCmfcMontoDepMaxim.getValue().toString();
			Date  dtFechaIni = null, dtFechaFin = null;
			BigDecimal bdMontoDesde = BigDecimal.ZERO;
			BigDecimal bdMontoHasta = BigDecimal.ZERO;
			Archivo arSelec = (Archivo)m.get("cdb_ArchivoPadreDbco");
			SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
			
			if(sCaid.compareTo("NC")!=0)
				iCaid = Integer.parseInt(sCaid);
			if(sCodcomp.compareTo("NCP") == 0)
				sCodcomp = "";
			if(!sReferencia.matches("^[0-9]+$"))
				sReferencia = "";
			if(!sMontoDesde.matches("^[0-9]*\\,?[0-9]+\\.?[0-9]*$")){
				sMontoDesde = "0.00";
			}
			if(!sMontoHasta.matches("^[0-9]*\\,?[0-9]+\\.?[0-9]*$") ||  sMontoHasta.compareTo("0.00") == 0 ){
				if(sMontoDesde.compareTo("0.00") == 0){
					Depbancodet dpbSelect = (Depbancodet)m.get("cbd_DepositoBcoSelecc");
					sMontoHasta = String.valueOf(dpbSelect.getMtocredito());
				}else{
					sMontoHasta = "0.00";
				}
			}
			bdMontoDesde = new BigDecimal(String.valueOf(new Divisas().formatStringToDouble(sMontoDesde)));
			bdMontoHasta = new BigDecimal(String.valueOf(new Divisas().formatStringToDouble(sMontoHasta)));
			if(bdMontoDesde.compareTo(bdMontoHasta) == 1){
				BigDecimal bdTemp = bdMontoDesde;
				bdMontoDesde = bdMontoHasta;
				bdMontoHasta = bdTemp;
			}
			
			txtCmfcMontoDep.setValue(new Divisas().formatDouble(bdMontoDesde.doubleValue()));
			txtCmfcMontoDepMaxim.setValue(new Divisas().formatDouble(bdMontoHasta.doubleValue()));
			srm.addSmartRefreshId(txtCmfcMontoDep.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(txtCmfcMontoDepMaxim.getClientId(FacesContext.getCurrentInstance()));
			iRangoMonto = Integer.parseInt(ddlCmfcRangoMonto.getValue().toString().split("@")[0]); 
			
			//&& =========== Filtros de fecha
			if(txtCmfbFechaIni.getValue()!=null)
				dtFechaIni = (Date)txtCmfbFechaIni.getValue();
			if(txtCmfbFechaFin.getValue()!=null)
				dtFechaFin = (Date)txtCmfbFechaFin.getValue();
			if(dtFechaIni!=null && dtFechaFin!=null && dtFechaIni.compareTo(dtFechaFin) >0 ){
				dtFechaFin = (Date)txtCmfbFechaIni.getValue();
				dtFechaIni = (Date)txtCmfbFechaFin.getValue();
				txtCmfbFechaIni.setValue(dtFechaIni);
				txtCmfbFechaFin.setValue(dtFechaFin);
				srm.addSmartRefreshId(txtCmfbFechaIni.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(txtCmfbFechaFin.getClientId(FacesContext.getCurrentInstance()));
			}
			lstCmDepositosCaja =  (m.get("cdb_lstCmDepositosCaja")==null)?
										new ArrayList<Deposito>():
										(ArrayList<Deposito>)m.get("cdb_lstCmDepositosCaja");
			
			lstCmFcAgregarDpsCaja = new ConfirmaDepositosCtrl()
											.filtrarDepositosCaja(iCaid, sReferencia, bdMontoDesde, bdMontoHasta, sCodcomp, "", 
													arSelec.getMoneda(), dtFechaIni, dtFechaFin, 
													"SCR",lstCmDepositosCaja,iRangoMonto);
			if(lstCmFcAgregarDpsCaja == null)
				lstCmFcAgregarDpsCaja = new ArrayList<Deposito>();
			
			lblCmAgDcMensaje.setStyle("color:green");
			lblCmAgDcMensaje.setValue("Se han encontrado: "+lstCmFcAgregarDpsCaja.size() +" resultados ");
			
			m.put("cdb_lstCmFcAgregarDpsCaja", lstCmFcAgregarDpsCaja);
			gvCmFcAgregarDpsCaja.dataBind();
			gvCmFcAgregarDpsCaja.setPageIndex(0);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
/******************************************************************************************/
/** Método: filtrar y agregar depositos de caja a la confirmacion.
 *	Fecha:  16/09/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */	
	public void agregarDepositosCajaConfirMan(ActionEvent ev){
		try {
			
			if(m.get("cbd_DepositoBcoSelecc") == null || m.get("cdb_ArchivoPadreDbco") == null){
				dwNotificacionErrorConfirmManual.setWindowState("normal");
				lblMsgNotificaErrorCMD.setValue("Debe seleccionar un depósito de banco para confirmar");
				return;
			}
			ddlCmfcRangoMonto.dataBind();
			lblCmAgDcMensaje.setValue("");			
			txtCmAdfcNoReferencia.setValue("");
			
			Depbancodet dbSelec = (Depbancodet)m.get("cbd_DepositoBcoSelecc");
			txtCmfcMontoDep.setValue("0.00");
			txtCmfcMontoDepMaxim.setValue(new Divisas().formatDouble(dbSelec.getMtocredito().doubleValue()));
			
			
			txtCmfbFechaIni.setValue(new Date());
			txtCmfbFechaFin.setValue(new Date());
			
			//&& ====== Obtener las cajas configuradas.
			lstfcCajas = new ArrayList<SelectItem>();
			lstfcCajas.add(new SelectItem("NC","Cajas","Seleccione la caja a utilizar en filtro"));
			List<Vf55ca01>lstCajas = new CtrlCajas().getAllCajas();
			if(lstCajas!=null){
				for (Vf55ca01 f5 : lstCajas) {
					lstfcCajas.add(new SelectItem(f5.getId().getCaid()+"",
												  f5.getId().getCaid()+" "+ f5.getId().getCaname().trim().toLowerCase(),
												  f5.getId().getCaid()+" "+ f5.getId().getCaname().trim().toLowerCase()));
				}
			}
			m.put("cdb_lstfcCajas", lstfcCajas);
			ddlCmfcCaja.dataBind();
			
			//&& ====== Obtener las companias.
			lstfcCompania = new ArrayList<SelectItem>();
			lstfcCompania.add(new SelectItem("NCP","Compañía","Seleccione la compañía a utilizar en filtros"));
			List<Vcompania>lstCompanias = new CompaniaCtrl().obtenerCompaniasCajaJDE();
			if(lstCompanias!=null){
				for (Vcompania vc : lstCompanias) {
					lstfcCompania.add(new SelectItem(vc.getId().getDrky().trim(),
										vc.getId().getDrky().trim()+" "+vc.getId().getDrdl01().trim(),
										vc.getId().getDrky().trim()+" "+vc.getId().getDrdl01().trim()));
				}
			}
			
			m.put("cdb_lstfcCompania", lstfcCompania);
			ddlCmfcCompania.dataBind();
			
			lstCmFcAgregarDpsCaja = new ArrayList<Deposito>();
			m.put("cdb_lstCmFcAgregarDpsCaja", lstCmFcAgregarDpsCaja);
			gvCmFcAgregarDpsCaja.dataBind();
			gvCmFcAgregarDpsCaja.setPageIndex(0);
			
			dwCmAgregarDepositosCaja.setWindowState("normal");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
/******************************************************************************************/
/** Método: Mostrar / ocultar valores para filtrar depositos de caja por nivel de monto.
 *	Fecha:  16/09/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */	
	public void seleccionFiltroMontoDeposito(ValueChangeEvent ev){
		try {
			if(chkCmNivelMonto.isChecked()){
				txtCmfcMontoDesdeDepCaja.setDisabled(false);
				txtCmfcMontoDepCaja.setDisabled(false);
				ddlCmFcRangoMonto.setDisabled(false);
				ddlCmFcRangoMonto.dataBind();
				txtCmfcMontoDepCaja.setValue("0.00");
				txtCmfcMontoDesdeDepCaja.setValue("0.00");
			}else{
				txtCmfcMontoDesdeDepCaja.setDisabled(true);
				txtCmfcMontoDepCaja.setDisabled(true);
				ddlCmFcRangoMonto.setDisabled(true);
				ddlCmFcRangoMonto.dataBind();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void seleccionarFiltroReferencia(ValueChangeEvent ev){
		try {
			if(chkCmNivelReferencia.isChecked()){
				txtCmfcNoReferencia.setDisabled(false);
				txtCmfcNoReferencia.setValue("");
			}else{
				txtCmfcNoReferencia.setDisabled(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
/******************************************************************************************/
/** Método: Realizar los movientos contables para confirmar los depositos manualmente.
 *	Fecha:  09/09/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public void confirmarDepositoManual(ActionEvent ev){
		ProcesoConfirmacion pca = new ProcesoConfirmacion();
		String sTipoEmpleado ="";
		List<Deposito>lstDepsCaja = null;
		boolean bHecho = true;
		ConfirmaDepositosCtrl cdc = new ConfirmaDepositosCtrl();
		double dMontoAjuste = 0;
		Date dtFechaConfirma = new Date(); 
		
		try {
			Vautoriz vaut = ((Vautoriz[]) m.get("sevAut"))[0];
			sTipoEmpleado = new EmpleadoCtrl().determinarTipoCliente(vaut.getId().getCodreg());
			Depbancodet dbp = (Depbancodet)m.get("cbd_DepositoBcoSelecc");
			lstDepsCaja = (ArrayList<Deposito>)m.get("cbd_lstCmDcLstDepsSelect");
			Archivo archivo = cdc.obtenerArchivoxId(dbp.getArchivo().getIdarchivo());			
			dMontoAjuste = new Divisas().formatStringToDouble(lblCmRsmCaDifer.getValue().toString());
			
			
			/*if(m.get("cdb_UsarFechaUsuarioConfirm") != null &&  dcCnfManualFConfirma.getValue() != null)
					dtFechaConfirma = (Date)dcCnfManualFConfirma.getValue();*/
			
			//&& ======= Conexiones para guardar los datos en bd
			Session session = HibernateUtilPruebaCn.currentSession();
			//Session sesionCajaW = HibernateUtil.getSessionFactoryMCAJA();
			
			Transaction transCaja = session.beginTransaction();
			
			Connection cn = As400Connection.getJNDIConnection("DSMCAJA2");
			cn.setAutoCommit(false);

			m.remove("pcd_Nobatch");
			m.remove("cdb_CambioReferencia");
			
			F55ca023 f23 = (F55ca023)m.get("cdb_F23CompCta");
			
			
			//&& =============== Ordenar la lista para obtener la fecha del deposito mas reciente
			Collections.sort(lstDepsCaja,
				new Comparator<Deposito>() {
					 
					public int compare(Deposito d1, Deposito d2) {
						return d2.getFecha().compareTo(d1.getFecha()) ; 
					}
				});
			dtFechaConfirma = ConfirmaDepositosCtrl.generarFechaComprobante( 
							lstDepsCaja.get(0).getFecha(),  
							lstDepsCaja.get(0).getCodsuc(), cn  );
			
			bHecho = pca.realizarConfirmacionDepositos(archivo, lstDepsCaja,
							dbp, session, session, 
							cn, vaut, sTipoEmpleado,
							dMontoAjuste, 34, 35, 
							dtFechaConfirma, 
							f23.getId().getD3digconc() );

			int iNobatch = Integer.parseInt(String.valueOf(m.get("pcd_Nobatch")));
			
			if(bHecho){
				lblMsgFinConfirmacionManual.setStyle("color:green");
				lblMsgFinConfirmacionManual.setValue("Se ha realizado la confirmacion: Batch: "+iNobatch);
				
				String sDta = "@";
				for (Deposito dp : lstDepsCaja) 
					sDta += dp.getCoduser().trim()+"/";
				sDta += "@" + dbp.getDescripcion().trim();
				
				cn.commit();
				transCaja.commit();
				
				//&& ======= Enviar el correo de notificacion al contador.
				Conciliacion conciliacion = (Conciliacion)m.get("cdb_RegistroConciliacion");
				List<Conciliacion> lstConciliaRegis = new ArrayList<Conciliacion>(1);
				lstConciliaRegis.add(conciliacion);
				
				bHecho = cdc.enviarNotificacionBatch(lstConciliaRegis,1);
				if(!bHecho){
					dwNotificacionErrorConfirmManual.setWindowState("normal");
					lblMsgNotificaErrorCMD.setValue("No se envío el correo al contador, notifique sus batch's");
				}
				
				// && =============== Oct 22, 2012: 
				// && Enviar notificacion de cambio de referencias.
				
				if(m.get("cdb_CambioReferencia") != null){
					List<Conciliacion>lstCncCambioRfr = new ArrayList<Conciliacion>();
					List<String>lstCambiosRefer = new ArrayList<String>();
					
					lstCncCambioRfr.add(conciliacion);
					lstCambiosRefer.add(String.valueOf(
								m.get("cdb_CambioReferencia")) + sDta);
					m.remove("cdb_CambioReferencia");
					
					cdc.notificaCambioReferencia(lstCncCambioRfr, 
								lstCambiosRefer,vaut.getId().getCodreg());

				}
				
				//&& ======= depositos de caja filtrados como coincidentes.
				lstCmDepositosCaja = new ArrayList<Deposito>();
				m.put("cdb_lstCmDepositosCaja", lstCmDepositosCaja);
				gvCmDepositosCaja.dataBind();
				gvCmDepositosCaja.setPageIndex(0);
				
				//&& ======= remover del grid depositos banco el deposito confirmado.
				lstCmDepositosBco = (ArrayList<Depbancodet>)m.get("cdb_lstCmDepositosBco");
				for (Depbancodet dbBcoGrid : lstCmDepositosBco) {
					if(dbp.getIddepbcodet() == dbBcoGrid.getIddepbcodet()){
						lstCmDepositosBco.remove(dbBcoGrid);
						break;
					}
				}		
				m.put("cdb_lstCmDepositosBco", lstCmDepositosBco);
				gvCmDepositosBco.dataBind();
				
				//&& ======== Restablecer los valores por defecto de los componentes.
				restablecerCmResumenBanco();
				txtCmfbReferenciaDep.setValue("");
				txtCmfbMontoDepBco.setValue("");
				ddlCmfbBancos.dataBind();			
				chkCmNivelMoneda.setChecked(false);
				chkCmNivelMonto.setChecked(true);
				chkCmNivelReferencia.setChecked(false);
				chkCmNivelTipoTransaccion.setChecked(false);
				
				txtCmfcNoReferencia.setValue("");
				txtCmfcNoReferencia.setDisabled(true);
				txtCmfcMontoDesdeDepCaja.setDisabled(false);
				txtCmfcMontoDesdeDepCaja.setValue("0.00");
				txtCmfcMontoDepCaja.setDisabled(false);
				txtCmfcMontoDepCaja.setValue("0.00");
				
				SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
				srm.addSmartRefreshId(txtCmfcMontoDepCaja.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(txtCmfcMontoDesdeDepCaja.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(txtCmfbReferenciaDep.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(txtCmfbMontoDepBco.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(ddlCmfbBancos.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(chkCmNivelMoneda.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(chkCmNivelMonto.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(chkCmNivelReferencia.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(chkCmNivelTipoTransaccion.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(gvCmDepositosBco.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(gvCmDepositosCaja.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(txtCmfcNoReferencia.getClientId(FacesContext.getCurrentInstance()));
				
				
				
			}else{
				lblMsgFinConfirmacionManual.setValue("");
				dwNotificacionErrorConfirmManual.setWindowState("normal");
				lblMsgNotificaErrorCMD.setValue("No se realiza la confirmación :" +pca.getError().toString().split("@")[1]);
				
				cn.rollback();
				transCaja.rollback();
			
			}
			session.close();			
			if(!cn.isClosed()){
				cn.close();
			}
			dwValidacionConfirmaManual.setWindowState("hidden");
			
		} catch (Exception e) {
			lblMsgFinConfirmacionManual.setStyle("color:red");
			lblMsgFinConfirmacionManual.setValue("No se realiza la confirmacion: Error de programa: "+e.getMessage());
			e.printStackTrace();
		}
	}
/******************************************************************************************/
/** Método: validaciones para permitir que se genere la confirmacion de depositos.
 *	Fecha:  09/09/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public void confirmarAsocionDeDepositos(ActionEvent ev){
		double dDiferencia = 0;
		double dMonto = 0;
		String sMensaje = "";
		boolean bHecho = true;
		
		try {
			Depbancodet dbp = (Depbancodet)m.get("cbd_DepositoBcoSelecc");
			F55ca033 f33 = (m.get("cdb_F33Ajuste")==null)? null: (F55ca033)m.get("cdb_F33Ajuste");
			dDiferencia = Double.parseDouble(lblCmRsmCaDifer.getValue().toString());
			dMonto = Double.parseDouble(lblCmRsmCaMonto.getValue().toString());
			if(dMonto == 0){
				sMensaje = "El monto a consolidar debe ser mayor a cero";
				bHecho = false;
			} 
			if( dDiferencia < 0 && (dDiferencia*-1) > f33.getId().getAjustemin().doubleValue() ){
				bHecho = false;
				sMensaje = "La diferencia entre montos de depósitos excede el monto configurado por faltantes.";
			}
			if(dDiferencia > 0 && dDiferencia > f33.getId().getAjustemax().doubleValue() ){
				bHecho = false;
				sMensaje = "La diferencia entre montos de depósitos excede el monto configurado por sobrantes.";
			}
			
			/*
			//&& ======= Validar el uso de fecha definida por usuario para la confirmacion.
			if(m.get("cdb_UsarFechaUsuarioConfirm") != null){
				lblMsgFinConfirmacionManual.setStyle("color:red");
				
				if(dcCnfManualFConfirma.getValue()!=null){
					Date dtFechaConfirma = (Date)dcCnfManualFConfirma.getValue();
					
					//&& ========= Fijar la ultima fecha del mes anterior.
					Calendar calFechaPrimerDiaMesAnt = Calendar.getInstance();
					calFechaPrimerDiaMesAnt.add(Calendar.MONTH, -1);
					calFechaPrimerDiaMesAnt.set(calFechaPrimerDiaMesAnt.get(Calendar.YEAR),
												calFechaPrimerDiaMesAnt.get(Calendar.MONTH),
												calFechaPrimerDiaMesAnt.getActualMinimum(Calendar.DAY_OF_MONTH),
												0,0,0);
					Date dtIniMesAnterior =  calFechaPrimerDiaMesAnt.getTime();
					if( dtFechaConfirma.compareTo(new Date()) > 0){
						sMensaje = "La fecha no puede ser mayor a la actual";
						bHecho = false;
					}
					if( dtFechaConfirma.compareTo(dtIniMesAnterior) < 0){
						sMensaje = "La fecha esta en mes anterior no válido";
						bHecho = false;
					}
					
				}else{
					sMensaje = "El valor de Fecha es requerido";
					bHecho = false;
				}				
			}
			*/
			//&& ====== validar la confirmacion de los depositos.
			if(bHecho){
				sMensaje = "Desea confirmar el depósito # "+dbp.getReferencia() +" por "+lblCmRsmBcoMonto.getValue().toString();
				sMensaje += " en la cuenta "+dbp.getNocuenta();
				dwValidacionConfirmaManual.setWindowState("normal");
				lblMsgConfirmacionManualDeps.setValue(sMensaje);
				
			}else{
				dwNotificacionErrorConfirmManual.setWindowState("normal");
				lblMsgNotificaErrorCMD.setValue(sMensaje);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
/******************************************************************************************/
/** Método: Actualizar el resumen de caja al seleccionar el deposito de caja
 *	Fecha:  09/09/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public void generarResumenCaja(List<Deposito>lstDepositosCaja, Depbancodet dpBanco){
		try {
			String sReferencias = "";
			BigDecimal bdMontoTotal = BigDecimal.ZERO;
			BigDecimal bdDiferencia = BigDecimal.ZERO;
			
			for (int i = 0; i < lstDepositosCaja.size(); i++) {
				Deposito dpCaja = lstDepositosCaja.get(i);
				sReferencias+= dpCaja.getReferencia()+ (((i+1)%2 == 0)?"\n":"; ");
				bdMontoTotal = bdMontoTotal.add(dpCaja.getMonto());
			}
			
			bdDiferencia = dpBanco.getMtocredito().subtract(bdMontoTotal);
			bdDiferencia = Divisas.roundBigDecimal(bdDiferencia);
			bdMontoTotal = Divisas.roundBigDecimal(bdMontoTotal);
			
			txtCmRsmCaRefers.setValue(sReferencias);
			lblCmRsmCaMonto.setValue(bdMontoTotal);
			lblCmRsmCaCantDeps.setValue(lstDepositosCaja.size());
			lblCmRsmCaDifer.setValue(bdDiferencia);
			
			switch (bdDiferencia.compareTo(BigDecimal.ZERO)) {
			case 0:
				lblCmRsmCaDifer.setStyle("color:black");
				break;
			case 1:
				lblCmRsmCaDifer.setStyle("color:blue");
				break;
			case -1:
				lblCmRsmCaDifer.setStyle("color:red");
				break;
			}
			SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
			srm.addSmartRefreshId(txtCmRsmCaRefers.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblCmRsmCaMonto.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblCmRsmCaCantDeps.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblCmRsmCaDifer.getClientId(FacesContext.getCurrentInstance()));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
/******************************************************************************************/
/** Método: movientos realizados para incluir deposito de caja en la confirmacion manual.
 *	Fecha:  09/09/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public void incluirDepsCajaConfrMan(ActionEvent ev){
		HtmlLink hLnkIncluir = null;
		HtmlLink hLnkExcluir = null;
		SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
		try {
			//&& ===== actualizar los links que controlan la inclusion y exclusion.
			RowItem ri = (RowItem)ev.getComponent().getParent().getParent();
			List lstCeldas = ri.getCells();
			Cell celda = (Cell)lstCeldas.get(0);
			hLnkIncluir = (HtmlLink)celda.getChildren().get(0);
			hLnkExcluir = (HtmlLink)celda.getChildren().get(1);
			hLnkExcluir.setStyle("display:inline");
			hLnkIncluir.setStyle("display:none");
			srm.addSmartRefreshId(hLnkExcluir.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(hLnkIncluir.getClientId(FacesContext.getCurrentInstance()));
			
			//&& ===== guardar en la lista, el nuevo deposito seleccionado.
			List<Deposito>lstCmDcLstDepsSelect = (m.get("cbd_lstCmDcLstDepsSelect")==null)?
												new ArrayList<Deposito>():(ArrayList<Deposito>)m.get("cbd_lstCmDcLstDepsSelect");
			
//			Deposito dpSelect = (Deposito)gvCmDepositosCaja.getDataRow(ri);									
			Deposito dpSelect = (Deposito)DataRepeater.getDataRow(ri);									
			lstCmDcLstDepsSelect.add(dpSelect);
			m.put("cbd_lstCmDcLstDepsSelect",lstCmDcLstDepsSelect);
			
			//&& ===== Generar Resumen de Depositos seleccionados en caja.
			Depbancodet dpBco = (Depbancodet)m.get("cbd_DepositoBcoSelecc");
			generarResumenCaja(lstCmDcLstDepsSelect,dpBco);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void excluirDepsCajaConfrMan(ActionEvent ev){
		HtmlLink hLnkIncluir = null;
		HtmlLink hLnkExcluir = null;
		SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
		try {
			RowItem ri = (RowItem)ev.getComponent().getParent().getParent();
			
//			Deposito dpSelect = (Deposito)gvCmDepositosCaja.getDataRow(ri);
			Deposito dpSelect = (Deposito)DataRepeater.getDataRow(ri);
			
			List lstCeldas = ri.getCells();
			Cell celda = (Cell)lstCeldas.get(0);
			hLnkIncluir = (HtmlLink)celda.getChildren().get(0);
			hLnkExcluir = (HtmlLink)celda.getChildren().get(1);
			hLnkExcluir.setStyle("display:none");
			hLnkIncluir.setStyle("display:inline");
			srm.addSmartRefreshId(hLnkExcluir.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(hLnkIncluir.getClientId(FacesContext.getCurrentInstance()));
			
			//&& ======== Verificar la lista de seleccion, y si se encuentra, sacarlo de lista.
			List<Deposito>lstCmDcLstDepsSelect = (m.get("cbd_lstCmDcLstDepsSelect")==null)?
													new ArrayList<Deposito>():
													(ArrayList<Deposito>)m.get("cbd_lstCmDcLstDepsSelect");
			for (Deposito deposito : lstCmDcLstDepsSelect) {
				if(deposito.getConsecutivo() == dpSelect.getConsecutivo()){
					lstCmDcLstDepsSelect.remove(deposito);
					break;
				}
			}										
			m.put("cbd_lstCmDcLstDepsSelect", lstCmDcLstDepsSelect);

			//&& ======= Actualizar el resumen de depositos Caja.
			Depbancodet dpBco = (Depbancodet)m.get("cbd_DepositoBcoSelecc");
			generarResumenCaja(lstCmDcLstDepsSelect,dpBco);
			
		} catch (Exception e) {
			lnkCmDcExcluirDcConfirMan.setStyle("display:inline");
			lnkCmDcIncluirDcConfirMan.setStyle("display:none");
			e.printStackTrace();
		}
	}
	
/******************************************************************************************/
/** Método: sacar el deposito de caja de la lista de depositos para incluir en confirmacion automatica.
 *	Fecha:  09/09/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public void removerDepsCajaConfrMan(ActionEvent ev){
		try {
			SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
			RowItem ri = (RowItem)ev.getComponent().getParent().getParent();
			
//			Deposito dpSelect = (Deposito)gvCmDepositosCaja.getDataRow(ri);
			Deposito dpSelect = (Deposito)DataRepeater.getDataRow(ri);

			//&& ======= Remover el objeto de la lista y actualizar el grid.
			lstCmDepositosCaja = (ArrayList<Deposito>)m.get("cdb_lstCmDepositosCaja");
			for (Deposito depsLista : lstCmDepositosCaja) {
				if(depsLista.getConsecutivo() == dpSelect.getConsecutivo()){
					lstCmDepositosCaja.remove(depsLista);
					break;
				}
			}							
			m.put("cdb_lstCmDepositosCaja", lstCmDepositosCaja);
			gvCmDepositosCaja.dataBind();
			
			//&& ======== Verificar la lista de seleccion, y si se encuentra, sacarlo de lista.
			List<Deposito>lstCmDcLstDepsSelect = (m.get("cbd_lstCmDcLstDepsSelect")==null)?
													new ArrayList<Deposito>():
													(ArrayList<Deposito>)m.get("cbd_lstCmDcLstDepsSelect");
			for (Deposito deposito : lstCmDcLstDepsSelect) {
				if(deposito.getConsecutivo() == dpSelect.getConsecutivo()){
					lstCmDcLstDepsSelect.remove(deposito);
					break;
				}
			}										
			m.put("cbd_lstCmDcLstDepsSelect", lstCmDcLstDepsSelect);									
			
			//&& ======== Verificar los estilos de los links del grid.
			List filasGrid = gvCmDepositosCaja.getRows();
			for(int i=0; i<filasGrid.size();i++){
				ri = (RowItem)filasGrid.get(i);
				
//				Deposito dpGridActual = (Deposito)gvCmDepositosCaja.getDataRow(ri);
				Deposito dpGridActual = (Deposito)DataRepeater.getDataRow(ri);
				
				for (Deposito dpListaSeleccion : lstCmDcLstDepsSelect) {
					if(dpGridActual.getConsecutivo() == dpListaSeleccion.getConsecutivo()){
						List lstCeldas = ri.getCells();
						Cell celda = (Cell)lstCeldas.get(0);
						HtmlLink hLnkIncluir = (HtmlLink)celda.getChildren().get(0);
						HtmlLink hLnkExcluir = (HtmlLink)celda.getChildren().get(1);
						hLnkExcluir.setStyle("display:inline");
						hLnkIncluir.setStyle("display:none");
						srm.addSmartRefreshId(hLnkExcluir.getClientId(FacesContext.getCurrentInstance()));
						srm.addSmartRefreshId(hLnkIncluir.getClientId(FacesContext.getCurrentInstance()));
						break;
					}
				}
			}
			
			
			//&& ======= Actualizar el resumen de depositos Caja.
			Depbancodet dpBco = (Depbancodet)m.get("cbd_DepositoBcoSelecc");
			generarResumenCaja(lstCmDcLstDepsSelect,dpBco);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
/******************************************************************************************/
/** Método: actualiza los datos mostrados en el resumen de archivo de banco seleccionado.
 *	Fecha:  08/09/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public void actualizarResumenBanco(Depbancodet dbp, Archivo ar){
		lblCmRsmBcoMonto.setValue(dbp.getMtocredito());
		lblCmRsmBcoFecha.setValue(dbp.getFechaproceso());
		lblCmRsmBcoMoneda.setValue(ar.getMoneda());
		lblCmRsmArchivo.setValue(ar.getNombre());
		lblCmRsmBcoReferencia.setValue(dbp.getReferencia());
		lblCmRsmBcoBancoCuenta.setValue(ar.getIdbanco()+" / "+dbp.getNocuenta());
		
		if(dbp.getHistoricomod()==null || dbp.getHistoricomod().trim().compareTo("")==0){
			lblCmRsmBcoHistorico.setStyle("display:none");
			txtCmRsmBcoHistorico.setStyle("display:none");
		}else{
			lblCmRsmBcoHistorico.setStyle("display:inline");
			txtCmRsmBcoHistorico.setStyle("display:inline");
		
			String sHistoriaRefer = "";
			String[] sHistorico =  dbp.getHistoricomod().split("@");
			for (String sHistoria : sHistorico) {
				String[] sDetalle = sHistoria.split(":");
				sHistoriaRefer += "#"+sDetalle[0]+" "+sDetalle[2] +", ";
				try {
					sHistoriaRefer +=  new SimpleDateFormat("dd/MM/yyyy")
											.format(new SimpleDateFormat("ddMMyyyy")
												.parse(sDetalle[1])) +"\n";
				} catch (ParseException e) {
					lblCmRsmBcoHistorico.setStyle("display:none");
					txtCmRsmBcoHistorico.setStyle("display:none");
					e.printStackTrace();
					break;
				}
			}
			txtCmRsmBcoHistorico.setValue(sHistoriaRefer);
		}
		SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
		srm.addSmartRefreshId(lblCmRsmBcoMonto.getClientId(FacesContext.getCurrentInstance()));
		srm.addSmartRefreshId(lblCmRsmBcoFecha.getClientId(FacesContext.getCurrentInstance()));
		srm.addSmartRefreshId(lblCmRsmBcoMoneda.getClientId(FacesContext.getCurrentInstance()));
		srm.addSmartRefreshId(lblCmRsmArchivo.getClientId(FacesContext.getCurrentInstance()));
		srm.addSmartRefreshId(lblCmRsmBcoReferencia.getClientId(FacesContext.getCurrentInstance()));
		srm.addSmartRefreshId(lblCmRsmBcoBancoCuenta.getClientId(FacesContext.getCurrentInstance()));
		srm.addSmartRefreshId(lblCmRsmBcoHistorico.getClientId(FacesContext.getCurrentInstance()));
		srm.addSmartRefreshId(txtCmRsmBcoHistorico.getClientId(FacesContext.getCurrentInstance()));
	}
/******************************************************************************************/
/** Método: Buscar los depositos de caja coincidentes con los depositos bancos y usando los niveles.
 *	Fecha:  08/09/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public void buscarCoincidenciasCaja(ActionEvent ev){
		try {
			int iCondicionMonto=3;
			String sMontoCondicion = "";
			String sNoreferencia="";
			BigDecimal bdMontoDesdeCond = BigDecimal.ZERO;
			BigDecimal bdMontoHastaCond = BigDecimal.ZERO;
			
			boolean bRefer, bMoneda, bMonto, bTipoTrans;
			ConfirmaDepositosCtrl cdc = new ConfirmaDepositosCtrl();
			
			bTipoTrans  = chkCmNivelTipoTransaccion.isChecked();
			bRefer  = chkCmNivelReferencia.isChecked();
			bMoneda = chkCmNivelMoneda.isChecked();
			bMonto  = chkCmNivelMonto.isChecked();
			
			//&& ========= Confirmar para la caja seleccionada.
			int iCodigoCaja = (ddlCaFtrCjaConfirmaMan.getValue().toString().compareTo("NC") == 0)?
									0:Integer.parseInt(ddlCaFtrCjaConfirmaMan.getValue().toString());
			
			restablecerCmResumenBanco();
			RowItem ri = (RowItem)ev.getComponent().getParent().getParent();
		
//			Depbancodet dbp = (Depbancodet)gvCmDepositosBco.getDataRow(ri);
			Depbancodet dbp = (Depbancodet)DataRepeater.getDataRow(ri);
			Archivo ar = cdc.obtenerArchivoxId(dbp.getArchivo().getIdarchivo());
			
			//&& ====== Fija los valores para el resumen de banco.
			actualizarResumenBanco(dbp, ar);
			lblMsgFinConfirmacionManual.setValue("");
		
			//&& ====== Obtener los montos minimos y maximos de ajustes.
			F55ca033 f33 = cdc.obtenerCtaTransitoriaxBanco(ar, dbp);
			m.put("cdb_F33Ajuste", f33);
			if(f33!=null)
				lblCmRsmCaRngoAjst.setValue("-"+f33.getId().getAjustemin().doubleValue()+" / +"
											+f33.getId().getAjustemax().doubleValue());
			else{
				lblCmRsmCaRngoAjst.setValue("-0.00 / +0.00");
			}

			//&& ======= Usar filtro por monto entre los depositos.
			if(bMonto){
				iCondicionMonto = Integer.parseInt(ddlCmFcRangoMonto.getValue().toString().split("@")[0]);				
				sMontoCondicion = txtCmfcMontoDepCaja.getValue().toString().trim();
				if(sMontoCondicion.matches("^[0-9]*\\,?[0-9]+\\.?[0-9]*$"))
					bdMontoHastaCond = new BigDecimal(String.valueOf(new Divisas().formatStringToDouble(sMontoCondicion)));
				else
					txtCmfcMontoDepCaja.setValue("0.00");
				
				sMontoCondicion = txtCmfcMontoDesdeDepCaja.getValue().toString().trim();
				if(sMontoCondicion.matches("^[0-9]*\\,?[0-9]+\\.?[0-9]*$")){
					bdMontoDesdeCond = new BigDecimal(String.valueOf(new Divisas().formatStringToDouble(sMontoCondicion)));
				}				
				if(bdMontoDesdeCond.compareTo(bdMontoHastaCond) == 1){
					BigDecimal bdTemp = bdMontoDesdeCond;
					bdMontoDesdeCond = bdMontoHastaCond;
					bdMontoHastaCond = bdTemp;
				}
				
				txtCmfcMontoDesdeDepCaja.setValue(new Divisas().formatDouble(bdMontoDesdeCond.doubleValue()));
				txtCmfcMontoDepCaja.setValue(new Divisas().formatDouble(bdMontoHastaCond.doubleValue()));
				
				SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
				srm.addSmartRefreshId(txtCmfcMontoDepCaja.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(txtCmfcMontoDesdeDepCaja.getClientId(FacesContext.getCurrentInstance()));
				
			}
			//&& ====== Usar el filtro de la referencia;
			if(bRefer){
				sNoreferencia = txtCmfcNoReferencia.getValue().toString().trim();
				if(!sNoreferencia.matches("^[0-9]+$"))
					sNoreferencia = "";
			}
			
			//&& ====== Realizar las comparaciones contra los depositos de caja y mostrar los resultados obtenidos.
			List<Deposito>lstDepsCajaCoinc = cdc.filtrarDepositosCaja(iCodigoCaja, bTipoTrans, bRefer, bMonto, bMoneda,
													ar, dbp, f33.getId().getAjustemin(), f33.getId().getAjustemax(),
													bdMontoDesdeCond, bdMontoHastaCond, iCondicionMonto, sNoreferencia);
			if(lstDepsCajaCoinc==null){
				lblMsgFinConfirmacionManual.setStyle("color:red");
				lblMsgFinConfirmacionManual.setValue("No se han encontrado coincidencias para el depósito");
				lstDepsCajaCoinc = new ArrayList<Deposito>();
			}else{
				lblMsgFinConfirmacionManual.setStyle("color:red");
				lblMsgFinConfirmacionManual.setValue("Se han encontrado "+lstDepsCajaCoinc.size()+" coincidencias");
			}
			m.remove("cdb_ArchivoPadreDbco");
			m.remove("cbd_DepositoBcoSelecc");
			m.put("cbd_DepositoBcoSelecc", dbp);
			m.put("cdb_ArchivoPadreDbco", ar);
			
			lstCmDepositosCaja = lstDepsCajaCoinc;
			m.put("cdb_lstCmDepositosCaja", lstDepsCajaCoinc);
			gvCmDepositosCaja.dataBind();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
/******************************************************************************************/
/** Método: Filtrar los depositos de banco coincidentes, sobre la lista mostrada en el grid.
 *	Fecha:  08/09/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public void cmFiltrarDepositosBanco(ActionEvent ev){
		try {
			String sNoReferencia = txtCmfbReferenciaDep.getValue().toString();
			String sMonto = txtCmfbMontoDepBco.getValue().toString();
			String sBancoId = ddlCmfbBancos.getValue().toString();
			int iCodBanco = 0;
			int iNorefer = 0;
			BigDecimal bdMonto = BigDecimal.ZERO;
			
			if(sNoReferencia.trim().equals("") && sMonto.trim().equals("") && sBancoId.equals("SB")){
				lstCmDepositosBco = new ArrayList<Depbancodet>();
				lstCmDepositosBco.addAll((ArrayList<Depbancodet>)m.get("cdb_lstCmDepositosBcoBckUp"));
				m.put("cdb_lstCmDepositosBco", lstCmDepositosBco);
				gvCmDepositosBco.dataBind();
				gvCmDepositosBco.setPageIndex(0);
				return;
			}
			if(!sNoReferencia.trim().equals("")  )
				iNorefer = Integer.parseInt(sNoReferencia);
			if(!sMonto.trim().equals(""))
				bdMonto = new BigDecimal(new Divisas().formatStringToDouble(sMonto)); 
			if(!sBancoId.equals("SB"))
				iCodBanco = Integer.parseInt(sBancoId);
			
			lstAchivosDepsBco = (ArrayList<Archivo>)m.get("cdb_lstAchivosDepsBco");
			List<Depbancodet>lstDeps = new ConfirmaDepositosCtrl()
										.obtenerDepositosxArchivos(lstAchivosDepsBco,iCodBanco, iNorefer, bdMonto);
			if(lstDeps == null)
				lstDeps = new ArrayList<Depbancodet>();
			
			m.put("cdb_lstCmDepositosBco", lstDeps);
			gvCmDepositosBco.dataBind();
			gvCmDepositosBco.setPageIndex(0);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
/******************************************************************************************/
/** Método: Restablecer los valores de los objetos de resumen de confirmacion manual.
 *	Fecha:  08/09/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public void restablecerCmResumenBanco( ){
		try {
			m.remove("cbd_lstCmDcLstDepsSelect");
			
			lblCmRsmBcoMonto.setValue("0.00");
			lblCmRsmBcoFecha.setValue("dd/MM/yyyy");
			lblCmRsmBcoMoneda.setValue("Moneda");
			lblCmRsmArchivo.setValue("Archivo");
			lblCmRsmBcoReferencia.setValue("00000000");
			lblCmRsmBcoBancoCuenta.setValue("Banco / #########");
			
			txtCmRsmCaRefers.setValue("");
			lblCmRsmCaMonto.setValue("0.00");
			lblCmRsmCaCantDeps.setValue("0");
			lblCmRsmCaDifer.setValue("0.00");
			lblCmRsmCaRngoAjst.setValue("-0.00 / +0.00");
			lblCmRsmBcoHistorico.setStyle("display:none");
			txtCmRsmBcoHistorico.setStyle("display:none");
			
			SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
			srm.addSmartRefreshId(lblCmRsmBcoMonto.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblCmRsmBcoFecha.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblCmRsmBcoMoneda.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblCmRsmArchivo.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblCmRsmBcoReferencia.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblCmRsmBcoBancoCuenta.getClientId(FacesContext.getCurrentInstance()));
			
			srm.addSmartRefreshId(txtCmRsmCaRefers.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblCmRsmCaMonto.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblCmRsmCaCantDeps.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblCmRsmCaDifer.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblCmRsmCaRngoAjst.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblCmRsmBcoHistorico.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(txtCmRsmBcoHistorico.getClientId(FacesContext.getCurrentInstance()));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
/******************************************************************************************/
/** Método: Cargar los datos para la confirmacion manual de depositos
 *	Fecha:  06/09/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public void llamarConfirmacionManual(ActionEvent ev){
		try {
			
			lstAchivosDepsBco = (ArrayList<Archivo>)m.get("cdb_lstAchivosDepsBco");
			if(lstAchivosDepsBco==null || lstAchivosDepsBco.size()==0){
				dwNotificacionErrorConfirmManual.setWindowState("normal");
				lblMsgNotificaErrorCMD.setValue("Seleccione al menos un archivo para la confirmación manual");
				return;
			}
			
			m.remove("cdb_ArchivoPadreDbco");
			m.remove("cbd_DepositoBcoSelecc");
			
			List<Depbancodet>lstDepsBanco = new ConfirmaDepositosCtrl().obtenerDepositosxArchivos(lstAchivosDepsBco,0,0, BigDecimal.ZERO);
			m.put("cdb_lstCmDepositosBco", lstDepsBanco);
			gvCmDepositosBco.dataBind();
			gvCmDepositosBco.setPageIndex(0);
			
			lstCmDepositosCaja = new ArrayList<Deposito>();
			m.put("cdb_lstCmDepositosCaja", lstCmDepositosCaja);
			gvCmDepositosCaja.dataBind();
			gvCmDepositosCaja.setPageIndex(0);
			
			restablecerCmResumenBanco();
			
			txtCmfbReferenciaDep.setValue("");
			txtCmfbMontoDepBco.setValue("");
			ddlCmfbBancos.dataBind();			
			
			
			chkCmNivelMoneda.setChecked(false);
			chkCmNivelMonto.setChecked(true);
			chkCmNivelReferencia.setChecked(false);
			chkCmNivelTipoTransaccion.setChecked(false);
			
			chkCmNivelReferencia.setChecked(false);
			chkCmNivelTipoTransaccion.setChecked(true);
			
			txtCmfcNoReferencia.setValue("");
			txtCmfcNoReferencia.setDisabled(true);
			txtCmfcMontoDepCaja.setValue("0.00");
			txtCmfcMontoDepCaja.setDisabled(false);
			txtCmfcMontoDesdeDepCaja.setValue("0.00");
			txtCmfcMontoDesdeDepCaja.setDisabled(false);
			ddlCmFcRangoMonto.setDisabled(true);
			lblMsgFinConfirmacionManual.setValue("");

			dwDatosConfirmaManual.setWindowState("normal");
			
		} catch (Exception e) {
			dwDatosConfirmaManual.setWindowState("hidden");
			e.printStackTrace();
		}
	}
/******************************************************************************************/
/** Método: Incluir todos los archivos mostrados como disponibles a confirmar.
 *	Fecha:  02/09/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public void removerTodosArchivos(ActionEvent ev){
		try {
			
			lstArchivosAConfirmar = (ArrayList<Archivo>)m.get("cdb_lstArchivosAConfirmar");	
			lstArchivoDispConfirm = (m.get("cdb_lstArchivoDispConfirm")==null)?
										new ArrayList<Archivo>():
									    (ArrayList<Archivo>)m.get("cdb_lstArchivoDispConfirm");
										
			lstArchivoDispConfirm.addAll(lstArchivosAConfirmar);
			m.put("cdb_lstArchivoDispConfirm", lstArchivoDispConfirm);
			gvArchivoDispConfirm.dataBind();
			
			lstArchivosAConfirmar = new ArrayList<Archivo>();
		    m.put("cdb_lstArchivosAConfirmar",lstArchivosAConfirmar);
			gvArchivoAConfirmar.dataBind();
			
			 SmartRefreshManager.getCurrentInstance().addSmartRefreshId(gvArchivoDispConfirm.getClientId(FacesContext.getCurrentInstance()));
			 SmartRefreshManager.getCurrentInstance().addSmartRefreshId(gvArchivoAConfirmar.getClientId(FacesContext.getCurrentInstance()));
			
		} catch (Exception e) {
			lstMsgSelArchivoConfirm.setStyle("color:red");
			lstMsgSelArchivoConfirm.setValue("No se han podido remover los archivos " );
			e.printStackTrace();
		}
	}
/******************************************************************************************/
/** Método: Incluir todos los archivos mostrados como disponibles a confirmar.
 *	Fecha:  02/09/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public void incluirTodosArchivos(ActionEvent ev){
		try {
			
			lstArchivoDispConfirm = (ArrayList<Archivo>)m.get("cdb_lstArchivoDispConfirm");
			lstArchivosAConfirmar = (m.get("cdb_lstArchivosAConfirmar")==null)?
										new ArrayList<Archivo>():
									    (ArrayList<Archivo>)m.get("cdb_lstArchivosAConfirmar");	
		    lstArchivosAConfirmar.addAll(lstArchivoDispConfirm);
		    m.put("cdb_lstArchivosAConfirmar",lstArchivosAConfirmar);
			gvArchivoAConfirmar.dataBind();
			
		    lstArchivoDispConfirm = new ArrayList<Archivo>();
		    m.put("cdb_lstArchivoDispConfirm", lstArchivoDispConfirm);
			gvArchivoDispConfirm.dataBind();
			
			 SmartRefreshManager.getCurrentInstance().addSmartRefreshId(gvArchivoDispConfirm.getClientId(FacesContext.getCurrentInstance()));
			 SmartRefreshManager.getCurrentInstance().addSmartRefreshId(gvArchivoAConfirmar.getClientId(FacesContext.getCurrentInstance()));
			
			
		} catch (Exception e) {
			lstMsgSelArchivoConfirm.setStyle("color:red");
			lstMsgSelArchivoConfirm.setValue("No se han podido agregar los archivos : Error de programa "+e);
			e.printStackTrace();
		}
	}
/******************************************************************************************/
/** Método: Mostrar el resultado de la confirmacion automatica.
 *	Fecha:  02/09/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public void mostrarResumenConfirmacionCa(ActionEvent ev){
		try {
			lstResumenConciliaCA = (ArrayList<CoincidenciaDeposito>)m.get("cdb_lstResumenConciliaCA");			
			gvResumenConciliaCA.dataBind();
			gvResumenConciliaCA.setPageIndex(0);
			
			dwResultadoConfirmAuto.setWindowState("hidden");
			dwResumenConciliacion.setWindowState("normal");
			lnkMostrarResumenCA.setStyle("display:none");
			
		} catch (Exception e) {
			lstMsgSelecDepsConfirmCa.setStyle("color:red");
			lstMsgSelecDepsConfirmCa.setValue("No se ha podido mostrar el resumen de confirmaciones");
			e.printStackTrace();
		}
	}
/******************************************************************************************/
/** Método: Realizar los traslados entre cuentas para confirmacion de depositos.
 *	Fecha:  30/08/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public void confirmarAutoDepositosCajaBanco(ActionEvent ev){
		ProcesoConfirmacion pca = new ProcesoConfirmacion();
		String sTipoEmpleado ="";
		List<Deposito>lstDepsCaja = null;
		boolean bHecho = true;
		List<CoincidenciaDeposito>lstResumenConcilia = new ArrayList<CoincidenciaDeposito>();
		int iConfirmado = 0, iNoConfirm =0;
		Date dtFechaUsoConfirma = new Date();
		List<Conciliacion>lstConciliaRegis = new ArrayList<Conciliacion>();
			
		List<Conciliacion>lstCncCambioRfr = new ArrayList<Conciliacion>();
		List<String>lstCambiosRefer = new ArrayList<String>();
		
		Transaction transCaja = null;
		Connection cn = null;

		Session sesionCajaR = null;
		Session sesionCajaW = null;
		
		
		try {
			lstMsgSelecDepsConfirmCa.setValue("");
			
			/*if(m.get("cdb_UsarFechaUsuarioConfirm") != null){
				lstMsgSelecDepsConfirmCa.setStyle("color:red");
				
				if(dcCnfAutoFechaConfirma.getValue()!=null){
					dtFechaUsoConfirma = (Date)dcCnfAutoFechaConfirma.getValue();
					
					//&& ========= Fijar la ultima fecha del mes anterior.
					Calendar calFechaPrimerDiaMesAnt = Calendar.getInstance();
					calFechaPrimerDiaMesAnt.add(Calendar.MONTH, -1);
					calFechaPrimerDiaMesAnt.set(calFechaPrimerDiaMesAnt.get(Calendar.YEAR),
												calFechaPrimerDiaMesAnt.get(Calendar.MONTH),
												calFechaPrimerDiaMesAnt.getActualMinimum(Calendar.DAY_OF_MONTH),
												0,0,0);
					Date dtIniMesAnterior = calFechaPrimerDiaMesAnt.getTime();
					if( dtFechaUsoConfirma.compareTo(new Date()) > 0){
						lstMsgSelecDepsConfirmCa.setValue("La fecha no puede ser mayor a la actual");
						return;
					}
					if( dtFechaUsoConfirma.compareTo(dtIniMesAnterior) < 0){
						lstMsgSelecDepsConfirmCa.setValue("La fecha esta en mes anterior no válido");
						return;
					}
				}else{
					lstMsgSelecDepsConfirmCa.setValue("El valor de Fecha es requerido");
					return;
				}				
			}*/
			
			Vautoriz vaut = ((Vautoriz[]) m.get("sevAut"))[0];
			sTipoEmpleado = new EmpleadoCtrl().determinarTipoCliente(vaut.getId().getCodreg());
			lstResultadoConfirmAuto  =  (ArrayList<CoincidenciaDeposito>)m.get("cdb_lstResultadoConfirmAuto");
			
			//&& ======= Recorrer la lista de coincidencias y realizar los movientos contables para cada una.
//			Transaction transCaja = null;
//			Connection cn = null;
//			As400Connection as400connection = new As400Connection();
//			Session sesionCajaR = HibernateUtil.getSessionFactoryMCAJAR().openSession();
//			Session sesionCajaW = HibernateUtil.getSessionFactoryMCAJA().openSession();
			
//			  sesionCajaR = HibernateUtil.getSessionFactoryMCAJAR().openSession();
//			  sesionCajaW = HibernateUtil.getSessionFactoryMCAJA().openSession();
			
			sesionCajaR = HibernateUtilPruebaCn.currentSession();
			sesionCajaW = HibernateUtilPruebaCn.currentSession();
			cn = As400Connection.getJNDIConnection("DSMCAJA2");
			
			for (CoincidenciaDeposito cdb : lstResultadoConfirmAuto) {
				
				transCaja = sesionCajaW.beginTransaction();
				cn.setAutoCommit(false);
				
				lstDepsCaja = new ArrayList<Deposito>();	
				lstDepsCaja.add(cdb.getDeposito());
				

				//&& =============== Ordenar la lista para obtener la fecha del deposito mas reciente
				Collections.sort(lstDepsCaja,
						new Comparator<Deposito>() {
						
							public int compare(Deposito d1, Deposito d2) {
								return d2.getFecha().compareTo(d1.getFecha()) ; 
							}
						});
				//&& ===============utilizar la fecha del deposito de caja.
				dtFechaUsoConfirma = ConfirmaDepositosCtrl.generarFechaComprobante( 
									cdb.getDeposito().getFecha(), 
									lstDepsCaja.get(0).getCodsuc(), cn );
				
				bHecho = pca.realizarConfirmacionDepositos(cdb.getArchivo(), 
								lstDepsCaja, cdb.getDepbancodet(), 
								sesionCajaR, sesionCajaW, cn, vaut, 
								sTipoEmpleado, cdb.getMontoXajuste(), 
								32, 35, dtFechaUsoConfirma,
								cdb.getDigitosCompara());
				
				if(bHecho){
					iConfirmado++;
					cdb.setNobatch(Integer.parseInt(String.valueOf(m.get("pcd_Nobatch"))));
					cdb.setObservacion("");
					
					String sDta = "@"+ cdb.getDeposito().getCoduser()+"@"
									 + cdb.getDepbancodet()
									 	.getDescripcion().trim();
					
					cn.commit();
					transCaja.commit();
					
					Conciliacion conciliacion = (Conciliacion)m.get("cdb_RegistroConciliacion");
					lstConciliaRegis.add(conciliacion);
					
					// && =============== Oct 22, 2012: Recuperar valores por cambio de referencia.
					// && ===== valor asignado en ProcesoConfirmacion
					if(m.get("cdb_CambioReferencia") != null){
						lstCncCambioRfr.add(conciliacion);
						lstCambiosRefer.add(String.valueOf(
									m.get("cdb_CambioReferencia")) + sDta);
						m.remove("cdb_CambioReferencia");
					}
					
				}else{
					iNoConfirm++;
					cdb.setNobatch(Integer.parseInt(String.valueOf(m.get("pcd_Nobatch"))));
					cdb.setObservacion(pca.getError().toString().split("@")[1]);

					cn.rollback();
					transCaja.rollback();
					
				}
				lstResumenConcilia.add(cdb);
			}
			
			
			
			//&& ====== Enviar los correos de notificacion
			ConfirmaDepositosCtrl cdc = new ConfirmaDepositosCtrl();
			if(lstConciliaRegis.size()>0){
				bHecho = cdc.enviarNotificacionBatch(lstConciliaRegis,2);

			}
			//&& =============== Oct 17, 2012: confirmarAutoDepositosCajaBanco
			//&& ======= Enviar notificacion de cambio de referencias. 
			if(lstCambiosRefer.size() > 0){
				cdc.notificaCambioReferencia(lstCncCambioRfr, lstCambiosRefer,
									vaut.getId().getCodreg());

			}
			
			lstMsgSelecDepsConfirmCa.setStyle("color:green");
			lstMsgSelecDepsConfirmCa.setValue("Finaliza el proceso: "+iConfirmado +" Confirmados, "+iNoConfirm+" No confirmados");
			lnkMostrarResumenCA.setStyle("display:inline");
			lnkConfirmarDepsSelec.setStyle("display:none");
			
			lstResumenConciliaCA = lstResumenConcilia;
			m.put("cdb_lstResumenConciliaCA", lstResumenConcilia);			
			
			
		} catch (Exception e) {e.printStackTrace();
			lstMsgSelecDepsConfirmCa.setStyle("color:red");
			lstMsgSelecDepsConfirmCa.setValue("Error al realizar procedimiento confirmacion automatica.");
			e.printStackTrace();
		}finally{
		 
			try {  HibernateUtilPruebaCn.closeSession(sesionCajaR); }
			catch (Exception e2) { }
			
			try {
				if (cn!= null && !cn.isClosed())
					cn.close();
			} catch (Exception e2) {}
			
		}
	}	
	
/******************************************************************************************/
/** Método: Permitir que un deposito que ha sido excluido regresar a lista de incluidos.
 *	Fecha:  30/08/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public void restaurarDepositoConfirmaAuto(ActionEvent ev){
		try {
			RowItem ri = (RowItem)ev.getComponent().getParent().getParent();
			
//			CoincidenciaDeposito cd = (CoincidenciaDeposito)gvResultadoConfirmAuto.getDataRow(ri);
			CoincidenciaDeposito cd = (CoincidenciaDeposito)DataRepeater.getDataRow(ri);
			
			lstCaDepositosExcluidos  = (ArrayList<CoincidenciaDeposito>)m.get("cdb_lstCaDepositosExcluidos");
			lstResultadoConfirmAuto  =  (m.get("cdb_lstResultadoConfirmAuto")==null)?
											new ArrayList<CoincidenciaDeposito>():
											(ArrayList<CoincidenciaDeposito>)m.get("cdb_lstResultadoConfirmAuto");
			lstResultadoConfirmAuto.add(cd);
			m.put("cdb_lstResultadoConfirmAuto",lstResultadoConfirmAuto);
			gvResultadoConfirmAuto.dataBind();
			
			for (CoincidenciaDeposito cdAincluir : lstCaDepositosExcluidos ) {
				if(cdAincluir.equals(cd)){
					lstCaDepositosExcluidos.remove(cdAincluir);
					break;
				}
			}
			m.put("cdb_lstCaDepositosExcluidos",lstCaDepositosExcluidos);
			gvCaDepositosExcluidos.dataBind();
											
		} catch (Exception e) {
			lstMsgSelecDepsConfirmCa.setStyle("color:red");
			lstMsgSelecDepsConfirmCa.setValue("Error de aplicación al intentar incluir depósito de lista");
			e.printStackTrace();
		}
	}
/******************************************************************************************/
/** Método: Excluir un registro de coincidencia de depositos para confirmar.
 *	Fecha:  30/08/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public void removerDepositoConfirmaAuto(ActionEvent ev){
		try {
			
			RowItem ri = (RowItem)ev.getComponent().getParent().getParent();
			CoincidenciaDeposito cd = (CoincidenciaDeposito)DataRepeater.getDataRow(ri);
			lstResultadoConfirmAuto = (ArrayList<CoincidenciaDeposito>)m.get("cdb_lstResultadoConfirmAuto");			
			lstCaDepositosExcluidos =  (m.get("cdb_lstCaDepositosExcluidos")==null)?
											new ArrayList<CoincidenciaDeposito>():
											(ArrayList<CoincidenciaDeposito>)m.get("cdb_lstCaDepositosExcluidos");
			lstCaDepositosExcluidos.add(cd);
			m.put("cdb_lstCaDepositosExcluidos",lstCaDepositosExcluidos);
			gvCaDepositosExcluidos.dataBind();
			gvCaDepositosExcluidos.setPageIndex(0);
			
			for (CoincidenciaDeposito cdIncluido : lstResultadoConfirmAuto) {
				if(cdIncluido.equals(cd)){
					lstResultadoConfirmAuto.remove(cd);
					break;
				}
			}
			m.put("cdb_lstResultadoConfirmAuto",lstResultadoConfirmAuto);
			gvResultadoConfirmAuto.dataBind();
			gvResultadoConfirmAuto.setPageIndex(0);
			
		} catch (Exception e) {
			lstMsgSelecDepsConfirmCa.setStyle("color:red");
			lstMsgSelecDepsConfirmCa.setValue("Error de aplicación al intentar remover depósito de lista");
			e.printStackTrace();
		}
	}
/******************************************************************************************/
/** Método: Regresar a la ventana de comparaci[on automatica.
 *	Fecha:  29/08/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public void regresarComparacionAutoDep(ActionEvent ev){
		try {
			lstResultadoConfirmAuto = null;			
			lstCaDepositosExcluidos = null;
			m.remove("cdb_CoincidenciasDepositos");
			m.remove("cdb_lstResultadoConfirmAuto");
			m.remove("cdb_lstCaDepositosExcluidos");
			
			lblCnfAutoFecha.setStyle("display:none");
			dcCnfAutoFechaConfirma.setStyle("display:none");
			m.remove("cdb_UsarFechaUsuarioConfirm");
			
			dwResultadoConfirmAuto.setWindowState("hidden");
			lnkMostrarResultadosCA.setStyle("display:none");
			lnkMostrarResumenCA.setStyle("display:none");
			
			mostrarInvocarProcesoAutomatico(ev);
			
		} catch (Exception e) {
			dwResultadoConfirmAuto.setWindowState("hidden");
			e.printStackTrace();
		}
	}
/******************************************************************************************/
/** Método: Mostrar los resultados de la comparación automática.
 *	Fecha:  29/08/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public void mostrarResultadosConfirmAuto(ActionEvent ev){
		try {
			
			dwDatosProcesoAutomatico.setWindowState("hidden");
			dwResultadoConfirmAuto.setWindowState("normal");
			m.remove("cdb_lstResultadoConfirmAuto");
			m.remove("cdb_lstCaDepositosExcluidos");
			gvCaDepositosExcluidos.dataBind();
			gvCaDepositosExcluidos.setPageIndex(0);
			lstMsgSelecDepsConfirmCa.setValue("");
			lnkMostrarResumenCA.setStyle("display:none");
			lnkConfirmarDepsSelec.setStyle("display:inline");
			
			/*
			//&& ======== Determinar si es necesario que el usuario seleccione la fecha a conciliar.
			Calendar calFecha =  Calendar.getInstance();
			int iNumDiaHoy = calFecha.get(Calendar.DAY_OF_MONTH);
			int iNumDiaIni = calFecha.getActualMinimum(Calendar.DAY_OF_MONTH);
			int iNumDiaFin = calFecha.getActualMaximum(Calendar.DAY_OF_MONTH);
			
			//&& ======== Verificacion de 5 dias antes del fin de mes y 5 dias despues del cierre de mes
			if(  ((iNumDiaHoy - iNumDiaIni) <= 5) || ((iNumDiaFin - iNumDiaHoy) <= 5 )  ){
				lblCnfAutoFecha.setStyle("display:inline");
				dcCnfAutoFechaConfirma.setStyle("display:inline");
				dcCnfAutoFechaConfirma.setValue(new Date());
				m.put("cdb_UsarFechaUsuarioConfirm",1);
			}else{
				m.remove("cdb_UsarFechaUsuarioConfirm");
				lblCnfAutoFecha.setStyle("display:none");
				dcCnfAutoFechaConfirma.setStyle("display:none");
			}
			*/
			
			lstResultadoConfirmAuto = (ArrayList<CoincidenciaDeposito>)m.get("cdb_CoincidenciasDepositos");
			m.put("cdb_lstResultadoConfirmAuto",lstResultadoConfirmAuto);
			gvResultadoConfirmAuto.dataBind();
			gvResultadoConfirmAuto.setPageIndex(0);
			
		} catch (Exception e) {
			dwResultadoConfirmAuto.setWindowState("hidden");
			e.printStackTrace();
		}
	}
/******************************************************************************************/
/** Método: Validar e inciar el proceso de confirmacion automatica.
 *	Fecha:  25/08/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public void iniciarConfirmAuto(ActionEvent ev){
		try {
//			gvArchivoAConfirmar.getRows().size();
			m.remove("cdb_DepsCajaNc");
			m.remove("cdb_DepsBancoNc"); 
			
			//&&========= Validar Listas.
			lstArchivosAConfirmar = null;
			if(m.containsKey("cdb_lstArchivosAConfirmar"))
				lstArchivosAConfirmar = (List<Archivo>) m.get("cdb_lstArchivosAConfirmar");
			
			if( lstArchivosAConfirmar == null || lstArchivosAConfirmar.isEmpty() ){
				lstMsgSelArchivoConfirm.setStyle("color:red");
				lstMsgSelArchivoConfirm.setValue("Debe seleccionar al menos un archivo para confirmar");
				return;
			}
			
			
			// && =============== inicarConfirmAuto: Valores para obtener depositos del reporte.
			String lstBcosArchivo = "";
			String lstMonedasAr =  "" ;
			String lstConscDbdt =  "";
			String lstIdArchivo =  "";
			
			//&& ========= Confirmar para la caja seleccionada.
			int iCodigoCaja = (ddlCaFtrCjaConfirma.getValue().toString().compareTo("NC") == 0)?
									0:Integer.parseInt(ddlCaFtrCjaConfirma.getValue().toString());
			
			List<Integer>lstReferenciasExcluidas = new ArrayList<Integer>();
			List<CoincidenciaDeposito> lstResultados = new ArrayList<CoincidenciaDeposito>();
				
			List<Archivo> lstArchivosAConfirmar = (ArrayList<Archivo>)m.get("cdb_lstArchivosAConfirmar");
			ConfirmaDepositosCtrl cdc = new ConfirmaDepositosCtrl();
			
			for (Archivo arSelect : lstArchivosAConfirmar) {
				
				// && =============== inicarConfirmAuto: Valores 'IN' de la consulta sobre vdeposito para Xls
				if(!lstBcosArchivo.contains(String.valueOf(arSelect.getIdbanco())))
					lstBcosArchivo += String.valueOf(arSelect.getIdbanco())+"@";
				if(!lstMonedasAr.contains(String.valueOf(arSelect.getMoneda())))
					lstMonedasAr += String.valueOf(arSelect.getMoneda()+"@");
				
				lstIdArchivo += String.valueOf(arSelect.getIdarchivo())+"@";
				
				List<CoincidenciaDeposito> lstAsociacion = cdc.compararDepositosBancoCaja(iCodigoCaja, arSelect,lstReferenciasExcluidas);
				if(lstAsociacion!=null && lstAsociacion.size()>0){
					lstResultados.addAll(lstAsociacion);
					
					for (CoincidenciaDeposito cd : lstAsociacion) {
						lstReferenciasExcluidas.add(cd.getDeposito().getConsecutivo());
					}
				}else{
					if( lstAsociacion == null && cdc.getError() != null){ 
						lstMsgSelArchivoConfirm.setStyle("color:red");
						lstMsgSelArchivoConfirm.setValue(cdc.getError().toString().split("@")[1]);
						return;
					}
				}
				
			}
			
			//&& ====== Mostrar los resultados encontrados en la comparacion.
			m.put("cdb_ProcesoCnfAuto", true);
			
			if(lstResultados.isEmpty()){
				lstMsgSelArchivoConfirm.setStyle("color:red");
				lstMsgSelArchivoConfirm.setValue("No se encontraron coincidencias en la comparación ");
			}else{
				lnkMostrarResultadosCA.setStyle("display:inline");
				lstMsgSelArchivoConfirm.setStyle("color:green");
				lstMsgSelArchivoConfirm.setValue("Se han encontrado "+lstResultados.size()+" depósitos coincidentes");
				m.put("cdb_CoincidenciasDepositos", lstResultados);
			}
			
			// && =============== inicarConfirmAuto: Cargar los resultados de depositos no coincidentes.
			String[]sBcos = lstBcosArchivo.split("@");
			List<Integer>lstBcos = new ArrayList<Integer>();
			for (int i = 0; i < sBcos.length ; i++) 
				lstBcos.add(Integer.parseInt(sBcos[i]));
			
			Session sesion = HibernateUtilPruebaCn.currentSession();
			
			
			Criteria cr = sesion.createCriteria(Vdeposito.class).setMaxResults(500);
			if(iCodigoCaja != 0) 
				cr.add(Restrictions.eq("id.caid",iCodigoCaja));
			
			cr.add(Restrictions.in("id.idbanco",lstBcos ));
			cr.add(Restrictions.in("id.moneda", lstMonedasAr.split("@")));
			cr.add(Restrictions.eq("id.estadocnfr",PropertiesSystem.DP_NOCONFIRMADO /*"SCR"*/));
			cr.add(Restrictions.eq("id.tipoconfr", PropertiesSystem.CFR_AUTO /*"CAM"*/));
			cr.add(Restrictions.eq("id.tipodep", "D"));
			cr.add(Restrictions.not(Restrictions.in("id.mpagodep", new String[]{"X"," "})));
			cr.addOrder(Order.asc("id.caid"));
			cr.addOrder(Order.asc("id.fecha"));
			
			if(iCodigoCaja != 0)
				cr.add(Restrictions.eq("id.caid", iCodigoCaja));
			if(lstReferenciasExcluidas.size()>0)
				cr.add(Restrictions.not(Restrictions.in("id.consecutivo", lstReferenciasExcluidas)));
			List<Vdeposito>lstDeps = cr.list();
			m.put("cdb_DepsCajaNc", lstDeps);
			
			// && =============== inicarConfirmAuto: obtener depositos de banco no coincidentes.
			sBcos = lstIdArchivo.split("@");
			lstBcos = new ArrayList<Integer>();
			for (int i = 0; i < sBcos.length ; i++) 
				lstBcos.add(Integer.parseInt(sBcos[i]));
			
			cr = sesion.createCriteria(Depbancodet.class).setMaxResults(1000);
			cr.add(Restrictions.in("archivo.idarchivo",lstBcos ));
			cr.add(Restrictions.eq("idestadocnfr",  PropertiesSystem.ID_DP_NO_CONFIRMADO /*36*/));
			cr.add(Restrictions.eq("idtipoconfirm", PropertiesSystem.ID_CRF_AUTOMATICA /*32*/));
			cr.add(Restrictions.gt("mtocredito", BigDecimal.ZERO));
			cr.add(Restrictions.ne("codtransaccion", "FA"));
			
			if(lstConscDbdt.trim().compareTo("") != 0){
				List<Integer>lstDpbco = new ArrayList<Integer>();
				sBcos =  lstConscDbdt.split("@");
				
				for (int i = 0; i < sBcos.length; i++) 
					lstDpbco.add(Integer.parseInt(sBcos[i]));
				
				cr.add(Restrictions.not(Restrictions
						.in("iddepbcodet", lstDpbco)));				
			}
			
			m.remove("cdb_DepsBancoNc");
			List<Depbancodet>lstDepsBco = cr.list();
			String[] sHistoricoMod = new String[lstDepsBco.size()]; 
			
			if(lstDepsBco != null  && lstDepsBco.size() > 0  ){
				Archivo ar = null;
				Depbancodet dp = null;
				
				for (int i = 0; i < lstDepsBco.size(); i++) {
					ar = lstDepsBco.get(i).getArchivo();
					sHistoricoMod[i] = ar.getMoneda()+"@"+ar.getIdbanco()
											+"@"+ar.getNombre().trim();
				}
				
				for (int i = 0; i < lstDepsBco.size(); i++) {
					dp = lstDepsBco.get(i);
					dp.setHistoricomod(sHistoricoMod[i]);
					lstDepsBco.set(i, dp);
				}
				
				m.put("cdb_DepsBancoNc", lstDepsBco);
			}

			
		} catch (Exception e) {
			lstMsgSelArchivoConfirm.setStyle("color:red");
			lstMsgSelArchivoConfirm.setValue("No se ha podido realizar filtro de busqueda: Error de programa ");
			e.printStackTrace();
		}
	}
/******************************************************************************************/
/** Método: Filtrar archivos de banco disponibles para confirmación.
 *	Fecha:  25/08/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public void buscarArchivosDisponibles(ActionEvent ev){
		try {
			String sCodBanco ="";
			Date dtFechaIni = null, dtFechaFin=null;
			sCodBanco = ddlCaFtBancos.getValue().toString();
			Integer lstIdsArchivosSelect[] = null;
			
			//&& ====== Filtros de fecha
			if(dcCaFtFechaIni.getValue()!=null)
				dtFechaIni = (Date)dcCaFtFechaIni.getValue();
			if(dcCaFtFechaFin.getValue()!=null)
				dtFechaFin = (Date)dcCaFtFechaFin.getValue();
			if(dtFechaIni!=null && dtFechaFin!=null && dtFechaIni.compareTo(dtFechaFin) >0 ){
				dtFechaFin = (Date)dcCaFtFechaIni.getValue();
				dtFechaIni = (Date)dcCaFtFechaFin.getValue();
				dcCaFtFechaIni.setValue(dtFechaIni);
				dcCaFtFechaFin.setValue(dtFechaFin);
			}
		
			//&& ===== Recuperar los id's de archivos que esten seleccionados para no incluirlos en consulta.
			if(m.get("cdb_lstIdsArchivosSelect")!=null)
				lstIdsArchivosSelect = (Integer[])m.get("cdb_lstIdsArchivosSelect");
			
			ConfirmaDepositosCtrl cdc = new ConfirmaDepositosCtrl();
			lstArchivoDispConfirm  = cdc.filtrarArchivosBanco("",sCodBanco, "SCTA",new Integer[]{45,43}, 
															  "", dtFechaIni, dtFechaFin,lstIdsArchivosSelect) ;
			if(lstArchivoDispConfirm == null || lstArchivoDispConfirm.size()==0){
				lstMsgSelArchivoConfirm.setStyle("color:red");
				lstMsgSelArchivoConfirm.setValue("No se han obtenido resultados ");
			}else{
				lstMsgSelArchivoConfirm.setStyle("color:green");
				lstMsgSelArchivoConfirm.setValue(lstArchivoDispConfirm.size() +" Resultados encontrados");
				m.put("cdb_lstArchivoDispConfirm", lstArchivoDispConfirm);
				gvArchivoDispConfirm.dataBind();
			}
			
		} catch (Exception e) {
			lstMsgSelArchivoConfirm.setStyle("color:red");
			lstMsgSelArchivoConfirm.setValue("No se ha podido realizar filtro de busqueda: Error de programa ");
			e.printStackTrace();
		}
	}
/******************************************************************************************/
/** Método: Mostrar ventana que solicitar los archivos para incluir en confirmacion automatica.
 *	Fecha:  24/08/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */	
	public void removerArchivoConfirmacion(ActionEvent ev){
		try {
			
			RowItem ri = (RowItem)ev.getComponent().getParent().getParent();
			Archivo arSel = (Archivo)DataRepeater.getDataRow(ri);
			
			//&& ======== Actualizar el grid de archivos a incluir en confirmacion.
			lstArchivosAConfirmar = (ArrayList<Archivo>)m.get("cdb_lstArchivosAConfirmar");
			for (Archivo arTmp : lstArchivosAConfirmar) {
				if(arTmp.getIdarchivo() == arSel.getIdarchivo()){
					lstArchivosAConfirmar.remove(arTmp);
					break;
				}
			}
			//&& ===== Actualizar los id's de los archivos seleccionados.
			m.remove("cdb_lstIdsArchivosSelect");
			if(lstArchivosAConfirmar.size()>0){
				Integer iIdsArchivosSel[] =  new Integer[lstArchivosAConfirmar.size()];
				for (int i = 0; i < lstArchivosAConfirmar.size(); i++) {
					Archivo ar = lstArchivosAConfirmar.get(i);
					iIdsArchivosSel[i] = ar.getIdarchivo();
				}
				m.put("cdb_lstIdsArchivosSelect", iIdsArchivosSel);
			}
			
			m.put("cdb_lstArchivosAConfirmar",lstArchivosAConfirmar);
			gvArchivoAConfirmar.dataBind();
			
			//&& ======== Actualizar el grid de archivos disponibles.
			lstArchivoDispConfirm = (ArrayList<Archivo>)m.get("cdb_lstArchivoDispConfirm");
			lstArchivoDispConfirm.add(arSel);
			m.put("cdb_lstArchivoDispConfirm", lstArchivoDispConfirm);
			gvArchivoDispConfirm.dataBind();
			
			 SmartRefreshManager.getCurrentInstance().addSmartRefreshId(gvArchivoDispConfirm.getClientId(FacesContext.getCurrentInstance()));
			 SmartRefreshManager.getCurrentInstance().addSmartRefreshId(gvArchivoAConfirmar.getClientId(FacesContext.getCurrentInstance()));
			
			
		} catch (Exception e) {
			lstMsgSelArchivoConfirm.setStyle("color:red");
			lstMsgSelArchivoConfirm.setValue("No se ha podido remover el archivo seleccionado: Error de programa ");
			e.printStackTrace();
		}
	}
/******************************************************************************************/
/** Método: Mostrar ventana que solicitar los archivos para incluir en confirmacion automatica.
 *	Fecha:  23/08/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */	
	public void incluirArchivoParaConfirmar(ActionEvent ev){
		try {
			RowItem ri = (RowItem)ev.getComponent().getParent().getParent();
			Archivo arSel = (Archivo)DataRepeater.getDataRow(ri);
			
			lstArchivosAConfirmar = (m.get("cdb_lstArchivosAConfirmar")==null)?
										new ArrayList<Archivo>():
									    (ArrayList<Archivo>)m.get("cdb_lstArchivosAConfirmar");	
			lstArchivosAConfirmar.add(arSel);
			m.put("cdb_lstArchivosAConfirmar",lstArchivosAConfirmar);
			
			gvArchivoAConfirmar.dataBind();
			gvArchivoAConfirmar.getRows().size();
//			
			
			//&& ===== Conservar los id's de los archivos seleccionados.
			m.remove("cdb_lstIdsArchivosSelect");
			if(lstArchivosAConfirmar.size()>0){
				Integer iIdsArchivosSel[] =  new Integer[lstArchivosAConfirmar.size()];
				for (int i = 0; i < lstArchivosAConfirmar.size(); i++) {
					Archivo ar = lstArchivosAConfirmar.get(i);
					iIdsArchivosSel[i] = ar.getIdarchivo();
				}
				m.put("cdb_lstIdsArchivosSelect", iIdsArchivosSel);
			}
			
			//&& ======= Actualizar la lista de los dos grid's
			lstArchivoDispConfirm = (ArrayList<Archivo>)m.get("cdb_lstArchivoDispConfirm");
			for (Archivo arTmp : lstArchivoDispConfirm) {
				if(arTmp.getIdarchivo() == arSel.getIdarchivo()){
					lstArchivoDispConfirm.remove(arTmp);
					break;
				}
			}
			m.put("cdb_lstArchivoDispConfirm", lstArchivoDispConfirm);
			gvArchivoDispConfirm.dataBind();
//			SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
//			srm.addSmartRefreshId(gvArchivoAConfirmar.getClientId(FacesContext.getCurrentInstance()));
//			srm.addSmartRefreshId(gvArchivoDispConfirm.getClientId(FacesContext.getCurrentInstance()));
			
			 SmartRefreshManager.getCurrentInstance().addSmartRefreshId(gvArchivoDispConfirm.getClientId(FacesContext.getCurrentInstance()));
			 SmartRefreshManager.getCurrentInstance().addSmartRefreshId(gvArchivoAConfirmar.getClientId(FacesContext.getCurrentInstance()));
//			 gvArchivoDispConfirm, gvArchivoAConfirmar
			
		} catch (Exception e) {
			lstMsgSelArchivoConfirm.setStyle("color:red");
			lstMsgSelArchivoConfirm.setValue("No se ha podido agregar el archivo seleccionado: Error de programa ");
			e.printStackTrace();
		}
	}
/******************************************************************************************/
/** Método: Mostrar ventana que solicitar los archivos para incluir en confirmacion automatica.
 *	Fecha:  23/08/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */	
	public void mostrarInvocarProcesoAutomatico(ActionEvent ev){
		int iCantReg = 30;

		try {
			m.remove("cdb_lstArchivosAConfirmar");
			m.remove("cdb_lstArchivoDispConfirm");
			m.remove("cdb_CoincidenciasDepositos");
			lnkMostrarResultadosCA.setStyle("display:none");
			
			//&& ====== Filtros de busqueda.
			dcCaFtFechaFin.setValue(new Date());
			dcCaFtFechaIni.setValue(new Date());
			m.remove("cdb_lstfbBancos");
			lstCaFtBancos = getLstfbBancos();
			m.put("cdb_lstCaFtBancos", lstCaFtBancos);
			ddlCaFtBancos.dataBind();
			lstMsgSelArchivoConfirm.setValue("");
			
			lstArchivoDispConfirm = new ArrayList<Archivo>();
			lstAchivosDepsBco = getLstAchivosDepsBco();
			
			if(lstAchivosDepsBco==null || lstAchivosDepsBco.size()==0){
				dwNotificacionErrorConfirmManual.setWindowState("normal");
				lblMsgNotificaErrorCMD.setValue("Seleccione al menos un archivo para la confirmación automática");
				return;
			}
			if(lstAchivosDepsBco.size() <= iCantReg)
				iCantReg = lstAchivosDepsBco.size();
			for (int i = 0; i < iCantReg; i++) {
				Archivo ar = lstAchivosDepsBco.get(i);
				if(ar.getEstadproc() != 43)
					lstArchivoDispConfirm.add(ar);
			}
			m.put("cdb_lstArchivoDispConfirm", lstArchivoDispConfirm);
			gvArchivoDispConfirm.dataBind();
			
			dwDatosProcesoAutomatico.setWindowState("normal");
			ddlCaFtrCjaConfirma.dataBind();
			
		} catch (Exception e) {
			dwDatosProcesoAutomatico.setWindowState("hidden");
			e.printStackTrace();
		}
	}
	
/******************************************************************************************/
/** Método: realizar busqueda de depositos de caja.
 *	Fecha:  23/08/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */		
	public void importarFTPBancos_Caja(ActionEvent ev){
		Date dtFechaEstadoCuenta = null;
		String sNumeroCuenta="";
		String sBancoEstadoCuenta="";
		
		if(txtFechaEstadoCuenta.getValue()!=null)
			dtFechaEstadoCuenta = (Date)txtFechaEstadoCuenta.getValue();
		
		if(dtFechaEstadoCuenta==null)
		{
			lblMsgResultImportarFTPBancos.setStyle("color:red");
			lblMsgResultImportarFTPBancos.setValue("La fecha de estado de cuenta no puede ser mayor que la fecha de hoy");
			
			return;
		}
		
		if(dtFechaEstadoCuenta.compareTo(new Date())>0)
		{
			lblMsgResultImportarFTPBancos.setStyle("color:red");
			lblMsgResultImportarFTPBancos.setValue("La fecha de estado de cuenta no puede ser mayor que la fecha de hoy");
			
			return;
		}
		
		if(txtBancoEstadoCuenta.getValue()!=null)
			sBancoEstadoCuenta = txtBancoEstadoCuenta.getValue().toString();
		
		
		if(sBancoEstadoCuenta.equals(""))
		{
			lblMsgResultImportarFTPBancos.setStyle("color:red");
			lblMsgResultImportarFTPBancos.setValue("El campo de Bancos no puede estar vacio");
			
			return;
		}
		
		if(txtNumeroCuenta.getValue()!=null)
			sNumeroCuenta = txtNumeroCuenta.getValue().toString();
		
		
		if(sNumeroCuenta.equals(""))
		{
			lblMsgResultImportarFTPBancos.setStyle("color:red");
			lblMsgResultImportarFTPBancos.setValue("El campo de Numero de cuenta no puede estar vacio");
			
			return;
		}
		
		LecturaConciliacion.leerConciliacionFTP(FechasUtil.addDaysToDate(dtFechaEstadoCuenta, 1) , sNumeroCuenta, sBancoEstadoCuenta);
	}
	
/******************************************************************************************/
/** Método: realizar busqueda de depositos de caja.
 *	Fecha:  23/08/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */	
	public void buscarDepositosCaja(ActionEvent ev){
		String sCaid, sCodcomp, sCodsuc,sNoReferencia,sMonto,sMoneda;
		Date dtFechaIni = null, dtFechaFin=null;
		int iCaid=0;
		BigDecimal bdMonto = BigDecimal.ZERO;
		ConfirmaDepositosCtrl cdc = new ConfirmaDepositosCtrl();
		
		
		try {
			//&& ====== Filtros de fecha
			if(txtfcFechaIni.getValue()!=null)
				dtFechaIni = (Date)txtfcFechaIni.getValue();
			if(txtfcFechaFin.getValue()!=null)
				dtFechaFin = (Date)txtfcFechaFin.getValue();
			if(dtFechaIni!=null && dtFechaFin!=null && dtFechaIni.compareTo(dtFechaFin) >0 ){
				dtFechaFin = (Date)txtfcFechaIni.getValue();
				dtFechaIni = (Date)txtfcFechaFin.getValue();
				txtfcFechaIni.setValue(dtFechaIni);
				txtfcFechaFin.setValue(dtFechaFin);
			}
			
			sCaid		 = ddlfcCajas.getValue().toString();
			sCodcomp	 = ddlfcCompania.getValue().toString();
			sCodsuc		 = ddlfcSucursal.getValue().toString();
			sNoReferencia = txtfcNoReferencia.getValue().toString().trim();
			sMonto		 = txtfcMontoDep.getValue().toString().trim();
			sMoneda 	 = ddlfcMoneda.getValue().toString();
			
			if(!sCaid.equals("NC"))
				iCaid = Integer.parseInt(sCaid);
			if(sCodcomp.equals("NCP"))
				sCodcomp = "";
			if(sCodsuc.equals("NSUC"))
				sCodsuc = "";
			if(sMoneda.equals("NMND"))
				sMoneda = "";
			 
			if(!sNoReferencia.matches("^[0-9]+$")){
				txtfcNoReferencia.setValue("");
				sNoReferencia = "";
			}
			if(!sMonto.matches("^[0-9]+\\.?[0-9]*$")){
				sMonto = "0.00";
				txtfcMontoDep.setValue("0.00");
			}
			
			txtfcMontoDep.setValue(new Divisas().formatDouble(Double.parseDouble(sMonto)));
			bdMonto = new BigDecimal(sMonto);
			 
			List<Deposito>lstDepositos = cdc.filtrarDepositosCaja(iCaid, sNoReferencia, bdMonto, bdMonto, sCodcomp,
																sCodsuc, sMoneda, dtFechaIni, dtFechaFin, "", null,2);
			 if(lstDepositos!=null && lstDepositos.size()>0){
				 lblMsgResultBusquedaDepCaja.setStyle("color:green");
				 lblMsgResultBusquedaDepCaja.setValue("Se han encontrado "+lstDepositos.size()+" resultados");
				 
				 lstAchivosDepsCaja = lstDepositos;
				 m.put("cdb_lstAchivosDepsCaja",lstDepositos);
				 gvArchivosDepsCaja.dataBind();
				 
			 }else{
				 lblMsgResultBusquedaDepCaja.setStyle("color:red");
				 lblMsgResultBusquedaDepCaja.setValue("No se han encontrado resultados coincidentes");
			 }
			
		} catch (Exception e) {
			lblMsgResultBusquedaDepCaja.setStyle("color:red");
			lblMsgResultBusquedaDepCaja.setValue("Error de aplicación al realizar filtros de búsqueda");
			e.printStackTrace();
		}
	}
/******************************************************************************************/
/** Método: obtener las sucursales configuradas para la compania.
 *	Fecha:  23/08/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public void cargarSucursalxCompania(ValueChangeEvent ev){
		try {
			String sCodcomp = ddlfcCompania.getValue().toString().trim();
			lstfcSucursal = new ArrayList<SelectItem>();
			lstfcSucursal.add(new SelectItem("NSUC","Sucursal","Seleccione la sucursal a utilizar en filtros"));
			List<String[]>  sucursales = null;
			
			if(!sCodcomp.equals("NCP")){
				 sucursales = new SucursalCtrl().obtenerSucursalesxCompania(sCodcomp);
				
				 
				 if(sucursales!=null){
					 
					 for (Object[] sucursal : sucursales) {
						
						 String unineg = String.valueOf(sucursal[0]);
							
							if(unineg.length() < 5 ) {
								unineg = CodeUtil.pad(unineg, 5, "0");
							}
							lstfcSucursal.add(new SelectItem(String.valueOf(sucursal[0]),
									unineg +": "+ String.valueOf(sucursal[2]).trim(),
											String.valueOf(sucursal[2]).trim()));
							
									 
					 }
					 
				}
				
			}
			m.put("cdb_lstfcSucursal", lstfcSucursal);
			ddlfcSucursal.dataBind();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
/******************************************************************************************/
/** Método: obtener las companias configuradas para la caja
 *	Fecha:  23/08/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public void cargarCompaniasxCaja(ValueChangeEvent ev){
		try {
			lstfcSucursal = new ArrayList<SelectItem>();
			lstfcSucursal.add(new SelectItem("NSUC","Sucursal","Seleccione la sucursal a utilizar en filtros"));
			m.put("cdb_lstfcSucursal", lstfcSucursal);
			ddlfcSucursal.dataBind();
			
			String sCaid = ddlfcCajas.getValue().toString();
			lstfcCompania = new ArrayList<SelectItem>();
			lstfcCompania.add(new SelectItem("NCP","Compañía","Seleccione la compañía a utilizar en filtros"));
			if(!sCaid.equals("NC")){
				F55ca014 f14[] = new CompaniaCtrl().obtenerCompaniasxCaja(Integer.parseInt(sCaid));
				if(f14!=null){
					for (F55ca014 comp : f14)
						lstfcCompania.add(new SelectItem(comp.getId().getC4rp01().trim(),
										comp.getId().getC4rp01().trim()+" "+comp.getId().getC4rp01d1().trim(),
										comp.getId().getC4rp01().trim()+" "+comp.getId().getC4rp01d1().trim()));
				}
			}else{
				List<Vcompania>lstCompanias = new CompaniaCtrl().obtenerCompaniasCajaJDE();
				if(lstCompanias!=null){
					for (Vcompania vc : lstCompanias) {
						lstfcCompania.add(new SelectItem(vc.getId().getDrky().trim(),
											vc.getId().getDrky().trim()+" "+vc.getId().getDrdl01().trim(),
											vc.getId().getDrky().trim()+" "+vc.getId().getDrdl01().trim()));
					}
				}
			}
			m.put("cdb_lstfcCompania", lstfcCompania);
			ddlfcCompania.dataBind();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	
/******************************************************************************************/
/** Método: Mostrar ventana con el detalle de los depositos contenidos en el archivo banco
 *	Fecha:  23/08/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	 
	public void mostrarBusquedaDepsCaja(ActionEvent ev){
		try {

			txtfcFechaIni.setValue(new Date());
			txtfcFechaFin.setValue(new Date());
			lblMsgResultBusquedaDepCaja.setValue("");
			txtfcNoReferencia.setValue("");
			txtfcMontoDep.setValue("");
			
			//&& ====== Obtener las cajas configuradas.
			lstfcCajas = new ArrayList<SelectItem>();
			lstfcCajas.add(new SelectItem("NC","Cajas","Seleccione la caja a utilizar en filtro"));
			List<Vf55ca01>lstCajas = new CtrlCajas().getAllCajas();
			if(lstCajas!=null){
				for (Vf55ca01 f5 : lstCajas) {
					lstfcCajas.add(new SelectItem(f5.getId().getCaid()+"",
												  f5.getId().getCaid()+" "+ f5.getId().getCaname().trim().toLowerCase(),
												  f5.getId().getCaid()+" "+ f5.getId().getCaname().trim().toLowerCase()));
				}
			}
			m.put("cdb_lstfcCajas", lstfcCajas);
			ddlfcCajas.dataBind();
			
			//&& ====== Obtener las companias.
			lstfcCompania = new ArrayList<SelectItem>();
			lstfcCompania.add(new SelectItem("NCP","Compañía","Seleccione la compañía a utilizar en filtros"));
			List<Vcompania>lstCompanias = new CompaniaCtrl().obtenerCompaniasCajaJDE();
			if(lstCompanias!=null){
				for (Vcompania vc : lstCompanias) {
					lstfcCompania.add(new SelectItem(vc.getId().getDrky().trim(),
										vc.getId().getDrky().trim()+" "+vc.getId().getDrdl01().trim(),
										vc.getId().getDrky().trim()+" "+vc.getId().getDrdl01().trim()));
				}
			}
			
			m.put("cdb_lstfcCompania", lstfcCompania);
			ddlfcCompania.dataBind();
			
			//&& ====== Obtener las sucursales.
			lstfcSucursal = new ArrayList<SelectItem>();
			lstfcSucursal.add(new SelectItem("NSUC","Sucursal","Seleccione la sucursal a utilizar en filtros"));
			m.put("cdb_lstfcSucursal", lstfcSucursal);
			ddlfcSucursal.dataBind();
			
			
			//&& ====== Obtener las monedas.
			lstfcMoneda = new ArrayList<SelectItem>();
			lstfcMoneda.add(new SelectItem("NMND","Moneda","Seleccione la Moneda a utilizar en filtros"));
			List<String[]>lstMonedas =  MonedaCtrl.obtenerMonedasJDE();
			if(lstMonedas!=null){
				for (String[] moneda : lstMonedas) {
					lstfcMoneda.add(new SelectItem(moneda[0],moneda[0],moneda[0]+" "+moneda[1]));
				}
			}
			m.put("cdb_lstfcMoneda", lstfcMoneda);
			ddlfcMoneda.dataBind();
			
			dwFiltrarDepsCaja.setWindowState("normal");
			
		} catch (Exception e) {
			dwFiltrarDepsCaja.setWindowState("hidden");
			e.printStackTrace();
		}
	}
	
/******************************************************************************************/
/** Método: Mostrar ventana Para pedir información de importar FTP
 *	Fecha:  19/03/2020
 *  Nombre: Luis Alberto Fonseca Mendez
 */
	 
	public void importarFTPBancos(ActionEvent ev){
		try {

			txtFechaEstadoCuenta.setValue(new Date());
			lblMsgResultImportarFTPBancos.setValue("");
			txtNumeroCuenta.setValue("");
			txtBancoEstadoCuenta.setValue("");
			
			dwImportarFTPBancos.setWindowState("normal");
			
		} catch (Exception e) {
			e.printStackTrace();
			dwImportarFTPBancos.setWindowState("hidden");
			e.printStackTrace();
		}
	}
		
/******************************************************************************************/
/** Método: Mostrar ventana con el detalle de los depositos realizados por sistema de caja.
 *	Fecha:  18/08/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */	
	public void mostrarDetDepositoCaja(ActionEvent ev){
		Deposito dc = null;
		ConfirmaDepositosCtrl cdc = new ConfirmaDepositosCtrl();
		
		try {
			RowItem ri = (RowItem)ev.getComponent().getParent().getParent();
			dc = (Deposito)DataRepeater.getDataRow(ri);
			Vdeposito dCaja = cdc.obtenerDetalleDepositoCaja(dc.getConsecutivo(),dc.getNodeposito(), dc.getCaid(),
															  dc.getCodsuc(), dc.getCodcomp()); 
			if(dCaja==null){
				return;
			}
			
			lblfcCaja.setValue(dCaja.getId().getCaid()+" "+dCaja.getId().getNomcaja().trim()); 
			lblfcReferCaja.setValue(dCaja.getId().getReferencia());
			lblfcCodSuc.setValue(dCaja.getId().getCodsuc().trim()+" "+dCaja.getId().getNombresuc().trim());
			lblfcMontoDep.setValue(dCaja.getId().getMoneda()+" "+new Divisas().formatDouble(dCaja.getId().getMonto().doubleValue()));
			lblfcCodComp.setValue(dCaja.getId().getCodcomp().trim()+" "+dCaja.getId().getNombrecomp().trim());
			lblfcTipoPago.setValue(dCaja.getId().getMpagodep().trim()+" "+dCaja.getId().getMetododesc().trim());
			lblfcEstatConfirm.setValue(dCaja.getId().getEstadocnfr().trim()+" "+dCaja.getId().getEstado().trim());
			lblfcNobatch.setValue(dCaja.getId().getNobatch());
			lblfcNoDocs.setValue(dCaja.getId().getRecjde());
			lblDtFechaDepsC.setValue(dCaja.getId().getFecha());
			
			//&& ======= Archivo confirmado.
			lblfcArchivo.setValue("");
			lblDtDcNoDocoConf.setValue("");
			lblDtDcNoreferBanco.setValue("");
			lblDtDcFechaConfirma.setValue("");
			lblDtDcNoBatchConf.setValue("");
			lblDtDcMontoBanco.setValue("");
			
			if(dCaja.getId().getEstadocnfr().compareTo("CFR")==0){
				Conciliadet cd = new ConfirmaDepositosCtrl()
									.obtenerConciliadet(dCaja.getId().getConsecutivo(), dCaja.getId().getCaid(), 
											dCaja.getId().getCodcomp(), dCaja.getId().getCodsuc(), dCaja.getId().getNodeposito());
				if(cd!=null){
					lblfcArchivo.setValue(cd.getConciliacion().getArchivo().getNombre());
					lblDtFechaDepsC.setValue(dCaja.getId().getFecha());
					lblDtDcNoDocoConf.setValue(cd.getConciliacion().getNoreferencia());
					lblDtDcNoreferBanco.setValue(cd.getConciliacion().getNoreferencia());
					lblDtDcFechaConfirma.setValue(cd.getConciliacion().getFechacrea());
					lblDtDcNoBatchConf.setValue(cd.getConciliacion().getNobatch());
					lblDtDcMontoBanco.setValue(cd.getConciliacion().getMonto());
				}
			}
			
			dwDetalleDepositoCaja.setWindowState("normal");
			
		} catch (Exception e) {
			dwDetalleDepositoCaja.setWindowState("hidden");
			e.printStackTrace();
		}finally{
			cdc = null;
		}
	}
/******************************************************************************************/
/** Método: Mostrar ventana con el detalle de los depositos contenidos en el archivo banco
 *	Fecha:  18/08/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public void mostrarDetalleArchivo(ActionEvent ev){
		
		try {
		
			RowItem ri = (RowItem)ev.getComponent().getParent().getParent();
			Archivo arBanco =  (Archivo)DataRepeater.getDataRow(ri);
			
			lstDetalleArchivoBanco = new ConfirmaDepositosCtrl()
					.obtenerDepositosxArchivo(arBanco.getIdarchivo());
			
			if(lstDetalleArchivoBanco == null){
				dwDetalleArchivoBanco.setWindowState("hidden");
				return;
			}
			
			List<Depbancodet> confirmado = (ArrayList<Depbancodet>) CollectionUtils
				.select(lstDetalleArchivoBanco, new Predicate(){
				
					public boolean evaluate(Object o) {
						return ((Depbancodet)o ).getIdestadocnfr() 
							 == PropertiesSystem.ID_DP_CONFIRMADO;
					}
			}) ;
			if(confirmado == null )confirmado = new ArrayList<Depbancodet>();
			
			List<Depbancodet> noconfirmado = (ArrayList<Depbancodet>)CollectionUtils
					.subtract(lstDetalleArchivoBanco, confirmado);
			if(noconfirmado == null )noconfirmado = new ArrayList<Depbancodet>();
			
			String caption = "Fecha: "+ FechasUtil.formatDatetoString(arBanco.getDatearchivo(), "dd/MM/yyyy")+", "+
					" Depósitos: "+lstDetalleArchivoBanco.size()+", Confirmados: "
						+confirmado.size()+", No confirmados: "+noconfirmado.size();
			
			hdDwDetalleArchivo.setCaptionText(caption);
			m.put("cdb_lstDetalleArchivoBanco", lstDetalleArchivoBanco);
			gvDetalleArchivoBanco.dataBind();
			gvDetalleArchivoBanco.setPageIndex(0);
			dwDetalleArchivoBanco.setWindowState("normal");
			
		} catch (Exception e) {
			dwDetalleArchivoBanco.setWindowState("hidden");
			e.printStackTrace();
		}finally{
			 
		}
	}
/******************************************************************************************/
/** Método: Buscar las cuentas de banco asociadas al banco para conciliar. 
 *	Fecha:  17/08/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */	
	public void cargarCuentasDeBanco(ValueChangeEvent ev){
		List<F55ca023>lstCuentasxBanco = null;
		int iCodigoBanco = 0;
		
		try {
			if(ddlfbBancos.getValue().toString().equals("SB")){
				m.remove("cdb_lstfbCuentaxBanco");
				ddlfbCuentaxBanco.dataBind();
				return;
			}
			iCodigoBanco = Integer.parseInt(ddlfbBancos.getValue().toString());
			lstCuentasxBanco =   ConfirmaDepositosCtrl.obtenerF55ca023xBanco(iCodigoBanco);
			if(lstCuentasxBanco==null || lstCuentasxBanco.size()==0){
				m.remove("cdb_lstfbCuentaxBanco");
				lblMsgResultBusquedaBco.setStyle("color:red");
				lblMsgResultBusquedaBco.setValue("No se encontraron números de cuenta asociadas al banco");
			}else{
				lstfbCuentaxBanco = new ArrayList<SelectItem>(lstCuentasxBanco.size()+1);
				lstfbCuentaxBanco.add(new SelectItem("SCTA","Cuenta","Selección el banco para las cuentas"));
				for (F55ca023 f023 : lstCuentasxBanco) {
					SelectItem si = new SelectItem();
					si.setValue(f023.getId().getD3nocuenta()+"");
					si.setLabel(f023.getId().getD3nocuenta()+"");
					si.setDescription(f023.getId().getD3crcd()+" "+f023.getId().getD3mcu().trim()+
									"."+f023.getId().getD3obj().trim()+"."+f023.getId().getD3sub());
					lstfbCuentaxBanco.add(si);
				}
				m.put("cdb_lstfbCuentaxBanco",lstfbCuentaxBanco);
				ddlfbCuentaxBanco.dataBind();
			}
		} catch (Exception e) {
			lblMsgResultBusquedaBco.setStyle("color:red");
			lblMsgResultBusquedaBco.setValue("Error de sistema al realizar obtener cuentas de banco "+e.getMessage());
			e.printStackTrace();
		}
	}
/******************************************************************************************/
/** Método: ejecutar filtro de busqueda de archivos de banco
 *	Fecha:  17/08/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */	
	public void buscarArchivosDepsBco(ActionEvent ev){
		Date dtFechaIni=null, dtFechaFin=null;
		String sNombre, sBancoId, sCuenta;
		String sNoReferencia;
		String sIdEstado;
		
		try {
			if(txtfbFechaIni.getValue()!=null)
				dtFechaIni = (Date)txtfbFechaIni.getValue();
			if(txtfbFechaFin.getValue()!=null)
				dtFechaFin = (Date)txtfbFechaFin.getValue();
			if(dtFechaIni!=null && dtFechaFin!=null && dtFechaIni.compareTo(dtFechaFin) >0 ){
				dtFechaFin = (Date)txtfbFechaIni.getValue();
				dtFechaIni = (Date)txtfbFechaFin.getValue();
				txtfbFechaIni.setValue(dtFechaIni);
				txtfbFechaFin.setValue(dtFechaFin);
			}
			sNombre   = txtfbNombreArchivo.getValue().toString().trim();
			sBancoId  = ddlfbBancos.getValue().toString();
			sCuenta   = ddlfbCuentaxBanco.getValue().toString();
			sIdEstado = ddlfbEstadoArchivo.getValue().toString();
			sNoReferencia = txtfbReferenciaDep.getValue().toString().trim();
			
			Integer iEstado[] = null;
			if(!sIdEstado.equals("SEA"))
				iEstado = new Integer[]{Integer.parseInt(sIdEstado)};
			
			
			ConfirmaDepositosCtrl cdc = new ConfirmaDepositosCtrl();
			List<Archivo>lstArchivos = cdc.filtrarArchivosBanco(sNombre, sBancoId, sCuenta, 
											iEstado, sNoReferencia, dtFechaIni, dtFechaFin,null) ;
			if(lstArchivos == null){
				lblMsgResultBusquedaBco.setStyle("color:red");
				lblMsgResultBusquedaBco.setValue("No se han encontrado resultados");
			}else{
				lblMsgResultBusquedaBco.setStyle("color:green");
				lblMsgResultBusquedaBco.setValue("Se han encontrado "+lstArchivos.size());
				lstAchivosDepsBco = lstArchivos;
				m.put("cdb_lstAchivosDepsBco", lstAchivosDepsBco);
				gvArchivosDepsBanco.dataBind();
			}
		} catch (Exception e) {
			lblMsgResultBusquedaBco.setStyle("color:red");
			lblMsgResultBusquedaBco.setValue("Error de sistema al realizar búsqueda "+e.getMessage());
			e.printStackTrace();
		}
	}
/******************************************************************************************/
/** Método: Mostrar ventanas con filtros para buscar archivos de depositos en banco
 *	Fecha:  15/08/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */	
	public void mostrarFiltrarDepsbco(ActionEvent ev){
		try {
			txtfbFechaIni.setValue(new Date());
			txtfbFechaFin.setValue(new Date());
			txtfbNombreArchivo.setValue("");
			txtfbReferenciaDep.setValue("");
			ddlfbBancos.dataBind();
			ddlfbCuentaxBanco.dataBind();
			ddlfbEstadoArchivo.dataBind();
			lblMsgResultBusquedaBco.setValue("");
			
			dwFiltrarArchivosBco.setWindowState("normal");
		} catch (Exception e) {
			dwFiltrarArchivosBco.setWindowState("hidden");
			e.printStackTrace();
		}
	}
	//&& ===========  CERRAR VENTANAS  ============== &&//
	public void cerrarAgregarDpCajaConfirmMan(ActionEvent ev){
		dwCmAgregarDepositosCaja.setWindowState("hidden");
	}
	public void cerrarNotificaErrorConfirM(ActionEvent ev){
		dwNotificacionErrorConfirmManual.setWindowState("hidden");
	}
	public void cerrarConfirmarDepositoManual(ActionEvent ev){
		dwValidacionConfirmaManual.setWindowState("hidden");
	}
	public void cerrarConfirmaManual(ActionEvent ev){
		dwDatosConfirmaManual.setWindowState("hidden");
	}
	public void cerrarResumenCa(ActionEvent ev){
		m.remove("cdb_UsarFechaUsuarioConfirm");
		dwResumenConciliacion.setWindowState("hidden");
	}
	public void cancelarConfirmaAutomatica(ActionEvent ev){
		m.remove("cdb_lstCaDepositosExcluidos");
		m.remove("cdb_lstResultadoConfirmAuto");
		m.remove("cdb_CoincidenciasDepositos");
		m.remove("cdb_UsarFechaUsuarioConfirm");
		dwResultadoConfirmAuto.setWindowState("hidden");
	}
	public void cancelarProcesoAutomatico(ActionEvent ev){
		m.remove("cdb_lstArchivosAConfirmar");
		m.remove("cdb_lstArchivoDispConfirm");
		m.remove("cdb_CoincidenciasDepositos");
		dwDatosProcesoAutomatico.setWindowState("hidden");
	}
	public void cerrarBuscarDepsCaja(ActionEvent ev){
		dwFiltrarDepsCaja.setWindowState("hidden");
	}
	
	public void cerrarImportarFTPBancos(ActionEvent ev){
		dwImportarFTPBancos.setWindowState("hidden");
	}
	
	
	
	public void cerrarBuscarDepsBco(ActionEvent ev){
		dwFiltrarArchivosBco.setWindowState("hidden");
	}
	public void cerrarMostrarDetalleArchivoBco(ActionEvent ev){
		dwDetalleArchivoBanco.setWindowState("hidden");
	}	
	public void cerrarDetalleDepsCaja(ActionEvent ev){
		dwDetalleDepositoCaja.setWindowState("hidden");
	}	
	//&& =========== GETTERS Y SETTERS ============== &&//
	public List<Archivo> getLstAchivosDepsBco() {
		try {
			if(m.get("cdb_lstAchivosDepsBco") == null){
				lstAchivosDepsBco = ConfirmaDepositosCtrl.obtenerArchivosBanco();
				if(lstAchivosDepsBco==null){
					lstAchivosDepsBco = new ArrayList<Archivo>();
				}
				m.put("cdb_lstAchivosDepsBco", lstAchivosDepsBco);
			}else{
				lstAchivosDepsBco = (ArrayList<Archivo>)m.get("cdb_lstAchivosDepsBco");
			}
		} catch (Exception e) {
			lstAchivosDepsBco = new ArrayList<Archivo>();
			e.printStackTrace();
		}
		return lstAchivosDepsBco;
	}
	public void setLstAchivosDepsBco(List<Archivo> lstAchivosDepsBco) {
		this.lstAchivosDepsBco = lstAchivosDepsBco;
	}
	public HtmlGridView getGvArchivosDepsBanco() {
		return gvArchivosDepsBanco;
	}
	public List<Deposito> getLstAchivosDepsCaja() {
		try {
			if(m.get("cdb_lstAchivosDepsCaja") == null){
				lstAchivosDepsCaja = new ConfirmaDepositosCtrl()
									.obtenerDepositosCaja( FechasUtil.quitarAgregarDiasFecha(-2, new Date()), new Date());
				if(lstAchivosDepsCaja==null){
					lstAchivosDepsCaja = new ArrayList<Deposito>();
				}
				m.put("cdb_lstAchivosDepsCaja", lstAchivosDepsCaja);
			}else{
				lstAchivosDepsCaja = (ArrayList<Deposito>)m.get("cdb_lstAchivosDepsCaja");
			}
		} catch (Exception e) {
			lstAchivosDepsCaja = new ArrayList<Deposito>();
			e.printStackTrace();
		}
		return lstAchivosDepsCaja;
	}
	public void setLstAchivosDepsCaja(List<Deposito> lstAchivosDepsCaja) {
		this.lstAchivosDepsCaja = lstAchivosDepsCaja;
	}
	public HtmlGridView getGvArchivosDepsCaja() {
		return gvArchivosDepsCaja;
	}
	public void setGvArchivosDepsCaja(HtmlGridView gvArchivosDepsCaja) {
		this.gvArchivosDepsCaja = gvArchivosDepsCaja;
	}
	public void setGvArchivosDepsBanco(HtmlGridView gvArchivosDepsBanco) {
		this.gvArchivosDepsBanco = gvArchivosDepsBanco;
	}
	public HtmlDialogWindow getDwFiltrarArchivosBco() {
		return dwFiltrarArchivosBco;
	}
	public void setDwFiltrarArchivosBco(HtmlDialogWindow dwFiltrarArchivosBco) {
		this.dwFiltrarArchivosBco = dwFiltrarArchivosBco;
	}
	public HtmlInputText getTxtfbNombreArchivo() {
		return txtfbNombreArchivo;
	}
	public void setTxtfbNombreArchivo(HtmlInputText txtfbNombreArchivo) {
		this.txtfbNombreArchivo = txtfbNombreArchivo;
	}
	public HtmlInputText getTxtfbReferenciaDep() {
		return txtfbReferenciaDep;
	}
	public void setTxtfbReferenciaDep(HtmlInputText txtfbReferenciaDep) {
		this.txtfbReferenciaDep = txtfbReferenciaDep;
	}
	public List<SelectItem> getLstfbBancos() {
		try {
			if(m.get("cdb_lstfbBancos")==null){
				lstfbBancos = new ArrayList<SelectItem>();
				lstfbBancos.add(new SelectItem("SB","Banco","Selección de banco"));
				F55ca022[] banco =   BancoCtrl.obtenerBancosConciliar();
				if(banco!=null){
					for (F55ca022 bco : banco) 
						lstfbBancos.add(new SelectItem(String.valueOf(bco.getId().getCodb()),
									bco.getId().getBanco(),	String.valueOf(bco.getId().getCodb()) +": "+bco.getId().getBanco()));
				}
				m.put("cdb_lstfbBancos", lstfbBancos);
			}else{
				lstfbBancos = (ArrayList<SelectItem>)(m.get("cdb_lstfbBancos"));
			}
		} catch (Exception e) {
			lstfbBancos = new ArrayList<SelectItem>();
			e.printStackTrace();
		}
		return lstfbBancos;
	}
	public void setLstfbBancos(List<SelectItem> lstfbBancos) {
		this.lstfbBancos = lstfbBancos;
	}
	public List<SelectItem> getLstfbCuentaxBanco() {
		try {
			if(m.get("cdb_lstfbCuentaxBanco")==null){
				lstfbCuentaxBanco = new ArrayList<SelectItem>();
				lstfbCuentaxBanco.add(new SelectItem("SCTA","Cuenta","Selección el banco para las cuentas"));
				m.put("cdb_lstfbCuentaxBanco", lstfbCuentaxBanco);
			}else{
				lstfbCuentaxBanco = (ArrayList<SelectItem>)m.get("cdb_lstfbCuentaxBanco");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lstfbCuentaxBanco;
	}
	public void setLstfbCuentaxBanco(List<SelectItem> lstfbCuentaxBanco) {
		this.lstfbCuentaxBanco = lstfbCuentaxBanco;
	}
	 
	public List<SelectItem> getLstfbEstadosArchivo() {
		try {
			
			if(m.get("cdb_lstfbEstadosArchivo")==null){
				lstfbEstadosArchivo = new ArrayList<SelectItem>();
				lstfbEstadosArchivo.add(new SelectItem("SEA","Estados","Selección de estados"));
				
				List<Valorcatalogo>lstEstado = new SalidasCtrl().leerValorCatalogo(16);
				if(lstEstado != null && lstEstado.size()>0){
					for (Valorcatalogo vc : lstEstado) {
						lstfbEstadosArchivo.add(new SelectItem(vc.getCodvalorcatalogo()+"",
											   vc.getDescripcion().toLowerCase(),
											   vc.getCodvalorcatalogo()+" "+vc.getDescripcion().toLowerCase()));
					}
				}
				m.put("cdb_lstfbEstadosArchivo", lstfbEstadosArchivo);
			}else{
				lstfbEstadosArchivo = (ArrayList<SelectItem>)m.get("cdb_lstfbEstadosArchivo");
			}
		} catch (Exception e) {
			lstfbEstadosArchivo = new ArrayList<SelectItem>();
			e.printStackTrace();
		}
		return lstfbEstadosArchivo;
	}
	public void setLstfbEstadosArchivo(List<SelectItem> lstfbEstadosArchivo) {
		this.lstfbEstadosArchivo = lstfbEstadosArchivo;
	}
	public HtmlDateChooser getTxtfbFechaIni() {
		return txtfbFechaIni;
	}
	public void setTxtfbFechaIni(HtmlDateChooser txtfbFechaIni) {
		this.txtfbFechaIni = txtfbFechaIni;
	}
	public HtmlDateChooser getTxtfbFechaFin() {
		return txtfbFechaFin;
	}
	public void setTxtfbFechaFin(HtmlDateChooser txtfbFechaFin) {
		this.txtfbFechaFin = txtfbFechaFin;
	}
	public HtmlDropDownList getDdlfbBancos() {
		return ddlfbBancos;
	}
	public void setDdlfbBancos(HtmlDropDownList ddlfbBancos) {
		this.ddlfbBancos = ddlfbBancos;
	}
	public HtmlDropDownList getDdlfbCuentaxBanco() {
		return ddlfbCuentaxBanco;
	}
	public void setDdlfbCuentaxBanco(HtmlDropDownList ddlfbCuentaxBanco) {
		this.ddlfbCuentaxBanco = ddlfbCuentaxBanco;
	}
	public HtmlDropDownList getDdlfbEstadoArchivo() {
		return ddlfbEstadoArchivo;
	}
	public void setDdlfbEstadoArchivo(HtmlDropDownList ddlfbEstadoArchivo) {
		this.ddlfbEstadoArchivo = ddlfbEstadoArchivo;
	}
	public HtmlOutputText getLblMsgResultBusquedaBco() {
		return lblMsgResultBusquedaBco;
	}
	public void setLblMsgResultBusquedaBco(HtmlOutputText lblMsgResultBusquedaBco) {
		this.lblMsgResultBusquedaBco = lblMsgResultBusquedaBco;
	}
	public List<Depbancodet> getLstDetalleArchivoBanco() {
		if(m.get("cdb_lstDetalleArchivoBanco")==null)
			lstDetalleArchivoBanco = new ArrayList<Depbancodet>();
		else
			lstDetalleArchivoBanco = (ArrayList<Depbancodet>)m.get("cdb_lstDetalleArchivoBanco");
		return lstDetalleArchivoBanco;
	}
	public void setLstDetalleArchivoBanco(List<Depbancodet> lstDetalleArchivoBanco) {
		this.lstDetalleArchivoBanco = lstDetalleArchivoBanco;
	}
	public HtmlGridView getGvDetalleArchivoBanco() {
		return gvDetalleArchivoBanco;
	}
	public void setGvDetalleArchivoBanco(HtmlGridView gvDetalleArchivoBanco) {
		this.gvDetalleArchivoBanco = gvDetalleArchivoBanco;
	}
	public HtmlDialogWindowHeader getHdDwDetalleArchivo() {
		return hdDwDetalleArchivo;
	}
	public void setHdDwDetalleArchivo(HtmlDialogWindowHeader hdDwDetalleArchivo) {
		this.hdDwDetalleArchivo = hdDwDetalleArchivo;
	}
	public HtmlDialogWindow getDwDetalleArchivoBanco() {
		return dwDetalleArchivoBanco;
	}
	public void setDwDetalleArchivoBanco(HtmlDialogWindow dwDetalleArchivoBanco) {
		this.dwDetalleArchivoBanco = dwDetalleArchivoBanco;
	}
	public HtmlDialogWindow getDwDetalleDepositoCaja() {
		return dwDetalleDepositoCaja;
	}
	public void setDwDetalleDepositoCaja(HtmlDialogWindow dwDetalleDepositoCaja) {
		this.dwDetalleDepositoCaja = dwDetalleDepositoCaja;
	}
	public HtmlOutputText getLblfcCaja() {
		return lblfcCaja;
	}
	public void setLblfcCaja(HtmlOutputText lblfcCaja) {
		this.lblfcCaja = lblfcCaja;
	}
	public HtmlOutputText getLblfcReferCaja() {
		return lblfcReferCaja;
	}
	public void setLblfcReferCaja(HtmlOutputText lblfcReferCaja) {
		this.lblfcReferCaja = lblfcReferCaja;
	}
	public HtmlOutputText getLblfcCodSuc() {
		return lblfcCodSuc;
	}
	public void setLblfcCodSuc(HtmlOutputText lblfcCodSuc) {
		this.lblfcCodSuc = lblfcCodSuc;
	}
	public HtmlOutputText getLblfcMontoDep() {
		return lblfcMontoDep;
	}
	public void setLblfcMontoDep(HtmlOutputText lblfcMontoDep) {
		this.lblfcMontoDep = lblfcMontoDep;
	}
	public HtmlOutputText getLblfcCodComp() {
		return lblfcCodComp;
	}
	public void setLblfcCodComp(HtmlOutputText lblfcCodComp) {
		this.lblfcCodComp = lblfcCodComp;
	}
	public HtmlOutputText getLblfcTipoPago() {
		return lblfcTipoPago;
	}
	public void setLblfcTipoPago(HtmlOutputText lblfcTipoPago) {
		this.lblfcTipoPago = lblfcTipoPago;
	}
	public HtmlOutputText getLblfcEstatConfirm() {
		return lblfcEstatConfirm;
	}
	public void setLblfcEstatConfirm(HtmlOutputText lblfcEstatConfirm) {
		this.lblfcEstatConfirm = lblfcEstatConfirm;
	}
	public HtmlOutputText getLblfcNobatch() {
		return lblfcNobatch;
	}
	public void setLblfcNobatch(HtmlOutputText lblfcNobatch) {
		this.lblfcNobatch = lblfcNobatch;
	}
	public HtmlOutputText getLblfcArchivo() {
		return lblfcArchivo;
	}
	public void setLblfcArchivo(HtmlOutputText lblfcArchivo) {
		this.lblfcArchivo = lblfcArchivo;
	}
	public HtmlOutputText getLblfcNoDocs() {
		return lblfcNoDocs;
	}
	public void setLblfcNoDocs(HtmlOutputText lblfcNoDocs) {
		this.lblfcNoDocs = lblfcNoDocs;
	}
	public HtmlDialogWindow getDwImportarFTPBancos() {
		return dwImportarFTPBancos;
	}
	
	public HtmlDialogWindow getDwFiltrarDepsCaja() {
		return dwFiltrarDepsCaja;
	}
	
	
	public void setDwFiltrarDepsCaja(HtmlDialogWindow dwFiltrarDepsCaja) {
		this.dwFiltrarDepsCaja = dwFiltrarDepsCaja;
	}
	
	public void setDwImportarFTPBancos(HtmlDialogWindow dwImportarFTPBancos) {
		this.dwImportarFTPBancos = dwImportarFTPBancos;
	}
	
	
	public List<SelectItem> getLstfcCajas() {
		try{
			
			if(m.get("cdb_lstfcCajas") == null ){
				
				lstfcCajas = new ArrayList<SelectItem>();
				lstfcCajas.add(new SelectItem("NC","Todas","Seleccione la caja a utilizar en filtro"));
				List<Vf55ca01>lstCajas = new CtrlCajas().getAllCajas();
				if(lstCajas!=null){
					for (Vf55ca01 f5 : lstCajas) {
						lstfcCajas.add(new SelectItem(f5.getId().getCaid()+"",
													  f5.getId().getCaid()+" "+ CodeUtil.capitalize(f5.getId().getCaname()) ,
													  f5.getId().getCaid()+" "+ f5.getId().getCaname().trim().toLowerCase()));
					}
				}
				m.put("cdb_lstfcCajas",lstfcCajas);
				
			}else{
				lstfcCajas = (ArrayList<SelectItem>)m.get("cdb_lstfcCajas");
			}
		} catch (Exception e) {	
			lstfcCajas = new ArrayList<SelectItem>();
			lstfcCajas.add(new SelectItem("NC","Cajas","Seleccione la caja a utilizar en filtro"));
			e.printStackTrace();
		}
		return lstfcCajas;
	}
	


	
	
	public void setLstfcCajas(List<SelectItem> lstfcCajas) {
		this.lstfcCajas = lstfcCajas;
	}
	 
	public List<SelectItem> getLstfcCompania() {
		lstfcCompania = (m.get("cdb_lstfcCompania")==null)?
							new ArrayList<SelectItem>():
							(ArrayList<SelectItem>)m.get("cdb_lstfcCompania");
		return lstfcCompania;
	}
	public void setLstfcCompania(List<SelectItem> lstfcCompania) {
		this.lstfcCompania = lstfcCompania;
	}
	public List<SelectItem> getLstfcSucursal() {
		lstfcSucursal = (m.get("cdb_lstfcSucursal")==null)?
				new ArrayList<SelectItem>():
				(ArrayList<SelectItem>)m.get("cdb_lstfcSucursal");
		return lstfcSucursal;
	}
	public void setLstfcSucursal(List<SelectItem> lstfcSucursal) {
		this.lstfcSucursal = lstfcSucursal;
	}
	public HtmlDropDownList getDdlfcCajas() {
		return ddlfcCajas;
	}
	public void setDdlfcCajas(HtmlDropDownList ddlfcCajas) {
		this.ddlfcCajas = ddlfcCajas;
	}
	public HtmlDropDownList getDdlfcCompania() {
		return ddlfcCompania;
	}
	public void setDdlfcCompania(HtmlDropDownList ddlfcCompania) {
		this.ddlfcCompania = ddlfcCompania;
	}
	public HtmlDropDownList getDdlfcSucursal() {
		return ddlfcSucursal;
	}
	public void setDdlfcSucursal(HtmlDropDownList ddlfcSucursal) {
		this.ddlfcSucursal = ddlfcSucursal;
	}
	public HtmlDateChooser getTxtfcFechaIni() {
		return txtfcFechaIni;
	}
	public void setTxtfcFechaIni(HtmlDateChooser txtfcFechaIni) {
		this.txtfcFechaIni = txtfcFechaIni;
	}
	public HtmlDateChooser getTxtfcFechaFin() {
		return txtfcFechaFin;
	}
	public void setTxtfcFechaFin(HtmlDateChooser txtfcFechaFin) {
		this.txtfcFechaFin = txtfcFechaFin;
	}
	public HtmlInputText getTxtfcNoReferencia() {
		return txtfcNoReferencia;
	}
	public void setTxtfcNoReferencia(HtmlInputText txtfcNoReferencia) {
		this.txtfcNoReferencia = txtfcNoReferencia;
	}
	public HtmlInputText getTxtfcMontoDep() {
		return txtfcMontoDep;
	}
	public void setTxtfcMontoDep(HtmlInputText txtfcMontoDep) {
		this.txtfcMontoDep = txtfcMontoDep;
	}
	public HtmlOutputText getLblMsgResultBusquedaDepCaja() {
		return lblMsgResultBusquedaDepCaja;
	}
	public void setLblMsgResultBusquedaDepCaja(
			HtmlOutputText lblMsgResultBusquedaDepCaja) {
		this.lblMsgResultBusquedaDepCaja = lblMsgResultBusquedaDepCaja;
	}
	public List<SelectItem> getLstfcMoneda() {
		lstfcMoneda = (m.get("cdb_lstfcMoneda")==null)?
						new ArrayList<SelectItem>():
						(ArrayList<SelectItem>)m.get("cdb_lstfcMoneda");	
		return lstfcMoneda;
	}
	public void setLstfcMoneda(List<SelectItem> lstfcMoneda) {
		this.lstfcMoneda = lstfcMoneda;
	}
	public HtmlDropDownList getDdlfcMoneda() {
		return ddlfcMoneda;
	}
	public void setDdlfcMoneda(HtmlDropDownList ddlfcMoneda) {
		this.ddlfcMoneda = ddlfcMoneda;
	}
	public HtmlDialogWindow getDwDatosProcesoAutomatico() {
		return dwDatosProcesoAutomatico;
	}
	public void setDwDatosProcesoAutomatico(
			HtmlDialogWindow dwDatosProcesoAutomatico) {
		this.dwDatosProcesoAutomatico = dwDatosProcesoAutomatico;
	}
	public HtmlGridView getGvArchivoDispConfirm() {
		return gvArchivoDispConfirm;
	}
	public void setGvArchivoDispConfirm(HtmlGridView gvArchivoDispConfirm) {
		this.gvArchivoDispConfirm = gvArchivoDispConfirm;
	}
	public List<Archivo> getLstArchivoDispConfirm() {
		lstArchivoDispConfirm = (m.get("cdb_lstArchivoDispConfirm")==null)?
								new ArrayList<Archivo>():
								(ArrayList<Archivo>)m.get("cdb_lstArchivoDispConfirm");
		return lstArchivoDispConfirm;
	}
	public void setLstArchivoDispConfirm(List<Archivo> lstArchivoDispConfirm) {
		this.lstArchivoDispConfirm = lstArchivoDispConfirm;
	}
	public HtmlGridView getGvArchivoAConfirmar() {
		return gvArchivoAConfirmar;
	}
	public void setGvArchivoAConfirmar(HtmlGridView gvArchivoAConfirmar) {
		this.gvArchivoAConfirmar = gvArchivoAConfirmar;
	}
	public List<Archivo> getLstArchivosAConfirmar() {
		lstArchivosAConfirmar = (m.get("cdb_lstArchivosAConfirmar")==null)?
				new ArrayList<Archivo>():
				(ArrayList<Archivo>)m.get("cdb_lstArchivosAConfirmar");
		return lstArchivosAConfirmar;
	}
	public void setLstArchivosAConfirmar(List<Archivo> lstArchivosAConfirmar) {
		this.lstArchivosAConfirmar = lstArchivosAConfirmar;
	}
	public HtmlOutputText getLstMsgSelArchivoConfirm() {
		return lstMsgSelArchivoConfirm;
	}
	public void setLstMsgSelArchivoConfirm(HtmlOutputText lstMsgSelArchivoConfirm) {
		this.lstMsgSelArchivoConfirm = lstMsgSelArchivoConfirm;
	}
	public List<SelectItem> getLstCaFtBancos() {
		lstCaFtBancos = (m.get("cdb_lstCaFtBancos")==null)?
							new ArrayList<SelectItem>():
							(ArrayList<SelectItem>)m.get("cdb_lstCaFtBancos");
		return lstCaFtBancos;
	}
	public void setLstCaFtBancos(List<SelectItem> lstCaFtBancos) {
		this.lstCaFtBancos = lstCaFtBancos;
	}
	public HtmlDropDownList getDdlCaFtBancos() {
		return ddlCaFtBancos;
	}
	public void setDdlCaFtBancos(HtmlDropDownList ddlCaFtBancos) {
		this.ddlCaFtBancos = ddlCaFtBancos;
	}
	public HtmlDateChooser getDcCaFtFechaIni() {
		return dcCaFtFechaIni;
	}
	public void setDcCaFtFechaIni(HtmlDateChooser dcCaFtFechaIni) {
		this.dcCaFtFechaIni = dcCaFtFechaIni;
	}
	public HtmlDateChooser getDcCaFtFechaFin() {
		return dcCaFtFechaFin;
	}
	public void setDcCaFtFechaFin(HtmlDateChooser dcCaFtFechaFin) {
		this.dcCaFtFechaFin = dcCaFtFechaFin;
	}
	public HtmlDialogWindow getDwResultadoConfirmAuto() {
		return dwResultadoConfirmAuto;
	}
	public void setDwResultadoConfirmAuto(HtmlDialogWindow dwResultadoConfirmAuto) {
		this.dwResultadoConfirmAuto = dwResultadoConfirmAuto;
	}
	public HtmlGridView getGvResultadoConfirmAuto() {
		return gvResultadoConfirmAuto;
	}
	public void setGvResultadoConfirmAuto(HtmlGridView gvResultadoConfirmAuto) {
		this.gvResultadoConfirmAuto = gvResultadoConfirmAuto;
	}
	public List<CoincidenciaDeposito> getLstResultadoConfirmAuto() {
		lstResultadoConfirmAuto = (m.get("cdb_lstResultadoConfirmAuto")==null)?
									new ArrayList<CoincidenciaDeposito>():
									(ArrayList<CoincidenciaDeposito>)m.get("cdb_lstResultadoConfirmAuto");
		return lstResultadoConfirmAuto;
	}
	public void setLstResultadoConfirmAuto(
			List<CoincidenciaDeposito> lstResultadoConfirmAuto) {
		this.lstResultadoConfirmAuto = lstResultadoConfirmAuto;
	}
	public HtmlLink getLnkMostrarResultadosCA() {
		return lnkMostrarResultadosCA;
	}
	public void setLnkMostrarResultadosCA(HtmlLink lnkMostrarResultadosCA) {
		this.lnkMostrarResultadosCA = lnkMostrarResultadosCA;
	}
	public HtmlGridView getGvCaDepositosExcluidos() {
		return gvCaDepositosExcluidos;
	}
	public void setGvCaDepositosExcluidos(HtmlGridView gvCaDepositosExcluidos) {
		this.gvCaDepositosExcluidos = gvCaDepositosExcluidos;
	}
	public List<CoincidenciaDeposito> getLstCaDepositosExcluidos() {
		lstCaDepositosExcluidos = (m.get("cdb_lstCaDepositosExcluidos")==null)?
						new ArrayList<CoincidenciaDeposito>():
						(ArrayList<CoincidenciaDeposito>)m.get("cdb_lstCaDepositosExcluidos");
		return lstCaDepositosExcluidos;
	}
	public void setLstCaDepositosExcluidos(
			List<CoincidenciaDeposito> lstCaDepositosExcluidos) {
		this.lstCaDepositosExcluidos = lstCaDepositosExcluidos;
	}
	public HtmlOutputText getLstMsgSelecDepsConfirmCa() {
		return lstMsgSelecDepsConfirmCa;
	}
	public void setLstMsgSelecDepsConfirmCa(HtmlOutputText lstMsgSelecDepsConfirmCa) {
		this.lstMsgSelecDepsConfirmCa = lstMsgSelecDepsConfirmCa;
	}
	public HtmlDialogWindow getDwResumenConciliacion() {
		return dwResumenConciliacion;
	}
	public void setDwResumenConciliacion(HtmlDialogWindow dwResumenConciliacion) {
		this.dwResumenConciliacion = dwResumenConciliacion;
	}
	public HtmlGridView getGvResumenConciliaCA() {
		return gvResumenConciliaCA;
	}
	public void setGvResumenConciliaCA(HtmlGridView gvResumenConciliaCA) {
		this.gvResumenConciliaCA = gvResumenConciliaCA;
	}
	public List<CoincidenciaDeposito> getLstResumenConciliaCA() {
		lstResumenConciliaCA = (m.get("cdb_lstResumenConciliaCA")==null)?
							new ArrayList<CoincidenciaDeposito>():
							(ArrayList<CoincidenciaDeposito>)m.get("cdb_lstResumenConciliaCA");
		return lstResumenConciliaCA;
	}
	public void setLstResumenConciliaCA(
			List<CoincidenciaDeposito> lstResumenConciliaCA) {
		this.lstResumenConciliaCA = lstResumenConciliaCA;
	}
	public HtmlLink getLnkMostrarResumenCA() {
		return lnkMostrarResumenCA;
	}
	public void setLnkMostrarResumenCA(HtmlLink lnkMostrarResumenCA) {
		this.lnkMostrarResumenCA = lnkMostrarResumenCA;
	}
	public HtmlLink getLnkConfirmarDepsSelec() {
		return lnkConfirmarDepsSelec;
	}
	public void setLnkConfirmarDepsSelec(HtmlLink lnkConfirmarDepsSelec) {
		this.lnkConfirmarDepsSelec = lnkConfirmarDepsSelec;
	}
	public HtmlDialogWindow getDwDatosConfirmaManual() {
		return dwDatosConfirmaManual;
	}
	public void setDwDatosConfirmaManual(HtmlDialogWindow dwDatosConfirmaManual) {
		this.dwDatosConfirmaManual = dwDatosConfirmaManual;
	}
	public HtmlGridView getGvCmDepositosCaja() {
		return gvCmDepositosCaja;
	}
	public void setGvCmDepositosCaja(HtmlGridView gvCmDepositosCaja) {
		this.gvCmDepositosCaja = gvCmDepositosCaja;
	}
	public HtmlGridView getGvCmDepositosBco() {
		return gvCmDepositosBco;
	}
	public void setGvCmDepositosBco(HtmlGridView gvCmDepositosBco) {
		this.gvCmDepositosBco = gvCmDepositosBco;
	}
	public List<Deposito> getLstCmDepositosCaja() {
		lstCmDepositosCaja = (m.get("cdb_lstCmDepositosCaja")==null)?
							new ArrayList<Deposito>():
							(ArrayList<Deposito>)m.get("cdb_lstCmDepositosCaja");
		return lstCmDepositosCaja;
	}
	public void setLstCmDepositosCaja(List<Deposito> lstCmDepositosCaja) {
		this.lstCmDepositosCaja = lstCmDepositosCaja;
	}
	public List<Depbancodet> getLstCmDepositosBco() {
		lstCmDepositosBco = (m.get("cdb_lstCmDepositosBco")==null)?
								new ArrayList<Depbancodet>():
								(ArrayList<Depbancodet>)m.get("cdb_lstCmDepositosBco");
		return lstCmDepositosBco;
	}
	public void setLstCmDepositosBco(List<Depbancodet> lstCmDepositosBco) {
		this.lstCmDepositosBco = lstCmDepositosBco;
	}
	public HtmlInputText getTxtCmfbReferenciaDep() {
		return txtCmfbReferenciaDep;
	}
	public void setTxtCmfbReferenciaDep(HtmlInputText txtCmfbReferenciaDep) {
		this.txtCmfbReferenciaDep = txtCmfbReferenciaDep;
	}
	public HtmlDropDownList getDdlCmfbBancos() {
		return ddlCmfbBancos;
	}
	public void setDdlCmfbBancos(HtmlDropDownList ddlCmfbBancos) {
		this.ddlCmfbBancos = ddlCmfbBancos;
	}
	public HtmlOutputText getLblCmRsmCaMonto() {
		return lblCmRsmCaMonto;
	}
	public void setLblCmRsmCaMonto(HtmlOutputText lblCmRsmCaMonto) {
		this.lblCmRsmCaMonto = lblCmRsmCaMonto;
	}
	public HtmlOutputText getLblCmRsmCaCantDeps() {
		return lblCmRsmCaCantDeps;
	}
	public void setLblCmRsmCaCantDeps(HtmlOutputText lblCmRsmCaCantDeps) {
		this.lblCmRsmCaCantDeps = lblCmRsmCaCantDeps;
	}
	public HtmlOutputText getLblCmRsmCaEtDifer() {
		return lblCmRsmCaDifer;
	}
	public void setLblCmRsmCaEtDifer(HtmlOutputText lblCmRsmCaDifer) {
		this.lblCmRsmCaDifer = lblCmRsmCaDifer;
	}
	public HtmlOutputText getLblCmRsmCaRngoAjst() {
		return lblCmRsmCaRngoAjst;
	}
	public void setLblCmRsmCaRngoAjst(HtmlOutputText lblCmRsmCaRngoAjst) {
		this.lblCmRsmCaRngoAjst = lblCmRsmCaRngoAjst;
	}
	public HtmlInputTextarea getTxtCmRsmCaRefers() {
		return txtCmRsmCaRefers;
	}
	public void setTxtCmRsmCaRefers(HtmlInputTextarea txtCmRsmCaRefers) {
		this.txtCmRsmCaRefers = txtCmRsmCaRefers;
	}
	public HtmlCheckBox getChkCmNivelReferencia() {
		return chkCmNivelReferencia;
	}
	public void setChkCmNivelReferencia(HtmlCheckBox chkCmNivelReferencia) {
		this.chkCmNivelReferencia = chkCmNivelReferencia;
	}
	public HtmlCheckBox getChkCmNivelMoneda() {
		return chkCmNivelMoneda;
	}
	public void setChkCmNivelMoneda(HtmlCheckBox chkCmNivelMoneda) {
		this.chkCmNivelMoneda = chkCmNivelMoneda;
	}
	public HtmlCheckBox getChkCmNivelTipoTransaccion() {
		return chkCmNivelTipoTransaccion;
	}
	public void setChkCmNivelTipoTransaccion(HtmlCheckBox chkCmNivelTipoTransaccion) {
		this.chkCmNivelTipoTransaccion = chkCmNivelTipoTransaccion;
	}
	public HtmlCheckBox getChkCmNivelMonto() {
		return chkCmNivelMonto;
	}
	public void setChkCmNivelMonto(HtmlCheckBox chkCmNivelMonto) {
		this.chkCmNivelMonto = chkCmNivelMonto;
	}
	public HtmlOutputText getLblCmRsmBcoMonto() {
		return lblCmRsmBcoMonto;
	}
	public void setLblCmRsmBcoMonto(HtmlOutputText lblCmRsmBcoMonto) {
		this.lblCmRsmBcoMonto = lblCmRsmBcoMonto;
	}
	public HtmlOutputText getLblCmRsmBcoFecha() {
		return lblCmRsmBcoFecha;
	}
	public void setLblCmRsmBcoFecha(HtmlOutputText lblCmRsmBcoFecha) {
		this.lblCmRsmBcoFecha = lblCmRsmBcoFecha;
	}
	public HtmlOutputText getLblCmRsmBcoMoneda() {
		return lblCmRsmBcoMoneda;
	}
	public void setLblCmRsmBcoMoneda(HtmlOutputText lblCmRsmBcoMoneda) {
		this.lblCmRsmBcoMoneda = lblCmRsmBcoMoneda;
	}
	public HtmlOutputText getLblCmRsmArchivo() {
		return lblCmRsmArchivo;
	}
	public void setLblCmRsmArchivo(HtmlOutputText lblCmRsmArchivo) {
		this.lblCmRsmArchivo = lblCmRsmArchivo;
	}
	public HtmlOutputText getLblCmRsmBcoReferencia() {
		return lblCmRsmBcoReferencia;
	}
	public void setLblCmRsmBcoReferencia(HtmlOutputText lblCmRsmBcoReferencia) {
		this.lblCmRsmBcoReferencia = lblCmRsmBcoReferencia;
	}
	public HtmlOutputText getLblCmRsmBcoBancoCuenta() {
		return lblCmRsmBcoBancoCuenta;
	}
	public void setLblCmRsmBcoBancoCuenta(HtmlOutputText lblCmRsmBcoBancoCuenta) {
		this.lblCmRsmBcoBancoCuenta = lblCmRsmBcoBancoCuenta;
	}
	public HtmlOutputText getLblCmRsmCaDifer() {
		return lblCmRsmCaDifer;
	}
	public void setLblCmRsmCaDifer(HtmlOutputText lblCmRsmCaDifer) {
		this.lblCmRsmCaDifer = lblCmRsmCaDifer;
	}
	public HtmlInputText getTxtCmfbMontoDepBco() {
		return txtCmfbMontoDepBco;
	}
	public void setTxtCmfbMontoDepBco(HtmlInputText txtCmfbMontoDepBco) {
		this.txtCmfbMontoDepBco = txtCmfbMontoDepBco;
	}
	public HtmlLink getLnkCmDcIncluirDcConfirMan() {
		return lnkCmDcIncluirDcConfirMan;
	}
	public void setLnkCmDcIncluirDcConfirMan(HtmlLink lnkCmDcIncluirDcConfirMan) {
		this.lnkCmDcIncluirDcConfirMan = lnkCmDcIncluirDcConfirMan;
	}
	public HtmlLink getLnkCmDcExcluirDcConfirMan() {
		return lnkCmDcExcluirDcConfirMan;
	}
	public void setLnkCmDcExcluirDcConfirMan(HtmlLink lnkCmDcExcluirDcConfirMan) {
		this.lnkCmDcExcluirDcConfirMan = lnkCmDcExcluirDcConfirMan;
	}
	public HtmlDialogWindow getDwValidacionConfirmaManual() {
		return dwValidacionConfirmaManual;
	}
	public void setDwValidacionConfirmaManual(
			HtmlDialogWindow dwValidacionConfirmaManual) {
		this.dwValidacionConfirmaManual = dwValidacionConfirmaManual;
	}
	public HtmlOutputText getLblMsgConfirmacionManualDeps() {
		return lblMsgConfirmacionManualDeps;
	}
	public void setLblMsgConfirmacionManualDeps(
			HtmlOutputText lblMsgConfirmacionManualDeps) {
		this.lblMsgConfirmacionManualDeps = lblMsgConfirmacionManualDeps;
	}
	public HtmlDialogWindow getDwNotificacionErrorConfirmManual() {
		return dwNotificacionErrorConfirmManual;
	}
	public void setDwNotificacionErrorConfirmManual(
			HtmlDialogWindow dwNotificacionErrorConfirmManual) {
		this.dwNotificacionErrorConfirmManual = dwNotificacionErrorConfirmManual;
	}
	public HtmlOutputText getLblMsgNotificaErrorCMD() {
		return lblMsgNotificaErrorCMD;
	}
	public void setLblMsgNotificaErrorCMD(HtmlOutputText lblMsgNotificaErrorCMD) {
		this.lblMsgNotificaErrorCMD = lblMsgNotificaErrorCMD;
	}
	public HtmlOutputText getLblMsgFinConfirmacionManual() {
		return lblMsgFinConfirmacionManual;
	}
	public void setLblMsgFinConfirmacionManual(
			HtmlOutputText lblMsgFinConfirmacionManual) {
		this.lblMsgFinConfirmacionManual = lblMsgFinConfirmacionManual;
	}
	public HtmlInputText getTxtCmfcMontoDepCaja() {
		return txtCmfcMontoDepCaja;
	}
	public void setTxtCmfcMontoDepCaja(HtmlInputText txtCmfcMontoDepCaja) {
		this.txtCmfcMontoDepCaja = txtCmfcMontoDepCaja;
	}
	public HtmlDropDownList getDdlCmFcRangoMonto() {
		return ddlCmFcRangoMonto;
	}
	public void setDdlCmFcRangoMonto(HtmlDropDownList ddlCmFcRangoMonto) {
		this.ddlCmFcRangoMonto = ddlCmFcRangoMonto;
	}
	public List<SelectItem> getLstCmFcRangoMonto() {
		try {
			lstCmFcRangoMonto = new ArrayList<SelectItem>();
			lstCmFcRangoMonto.add(new SelectItem("1@CMME","Menor que","Filtra depositos de caja cuando su monto es menor al deposito de banco o valor ingresado"));
			lstCmFcRangoMonto.add(new SelectItem("2@CMMA","Mayor que","Filtra depositos de caja cuando su monto es mayor al deposito de banco o valor ingresado"));
			lstCmFcRangoMonto.add(new SelectItem("3@CMI","Igual que","Filtra depositos de caja cuando su monto es igual al deposito de banco o valor ingresado"));
		} catch (Exception e) {
			lstCmFcRangoMonto = new ArrayList<SelectItem>();
			e.printStackTrace();
		}
		return lstCmFcRangoMonto;
	}
	public void setLstCmFcRangoMonto(List<SelectItem> lstCmFcRangoMonto) {
		this.lstCmFcRangoMonto = lstCmFcRangoMonto;
	}
	public HtmlInputText getTxtCmfcNoReferencia() {
		return txtCmfcNoReferencia;
	}
	public void setTxtCmfcNoReferencia(HtmlInputText txtCmfcNoReferencia) {
		this.txtCmfcNoReferencia = txtCmfcNoReferencia;
	}
	public HtmlDialogWindow getDwCmAgregarDepositosCaja() {
		return dwCmAgregarDepositosCaja;
	}
	public void setDwCmAgregarDepositosCaja(
			HtmlDialogWindow dwCmAgregarDepositosCaja) {
		this.dwCmAgregarDepositosCaja = dwCmAgregarDepositosCaja;
	}
	public HtmlDropDownList getDdlCmfcCaja() {
		return ddlCmfcCaja;
	}
	public void setDdlCmfcCaja(HtmlDropDownList ddlCmfcCaja) {
		this.ddlCmfcCaja = ddlCmfcCaja;
	}
	public HtmlDropDownList getDdlCmfcCompania() {
		return ddlCmfcCompania;
	}
	public void setDdlCmfcCompania(HtmlDropDownList ddlCmfcCompania) {
		this.ddlCmfcCompania = ddlCmfcCompania;
	}
	public HtmlInputText getTxtCmAdfcNoReferencia() {
		return txtCmAdfcNoReferencia;
	}
	public void setTxtCmAdfcNoReferencia(HtmlInputText txtCmAdfcNoReferencia) {
		this.txtCmAdfcNoReferencia = txtCmAdfcNoReferencia;
	}
	public HtmlInputText getTxtCmfcMontoDep() {
		return txtCmfcMontoDep;
	}
	public void setTxtCmfcMontoDep(HtmlInputText txtCmfcMontoDep) {
		this.txtCmfcMontoDep = txtCmfcMontoDep;
	}
	public HtmlDateChooser getTxtCmfbFechaIni() {
		return txtCmfbFechaIni;
	}
	public void setTxtCmfbFechaIni(HtmlDateChooser txtCmfbFechaIni) {
		this.txtCmfbFechaIni = txtCmfbFechaIni;
	}
	public HtmlDateChooser getTxtCmfbFechaFin() {
		return txtCmfbFechaFin;
	}
	public void setTxtCmfbFechaFin(HtmlDateChooser txtCmfbFechaFin) {
		this.txtCmfbFechaFin = txtCmfbFechaFin;
	}
	public HtmlGridView getGvCmFcAgregarDpsCaja() {
		return gvCmFcAgregarDpsCaja;
	}
	public void setGvCmFcAgregarDpsCaja(HtmlGridView gvCmFcAgregarDpsCaja) {
		this.gvCmFcAgregarDpsCaja = gvCmFcAgregarDpsCaja;
	}
	public List<Deposito> getLstCmFcAgregarDpsCaja() {
		lstCmFcAgregarDpsCaja = (m.get("cdb_lstCmFcAgregarDpsCaja")==null)?
				new ArrayList<Deposito>():
				(ArrayList<Deposito>)m.get("cdb_lstCmFcAgregarDpsCaja");
		return lstCmFcAgregarDpsCaja;
	}
	public void setLstCmFcAgregarDpsCaja(List<Deposito> lstCmFcAgregarDpsCaja) {
		this.lstCmFcAgregarDpsCaja = lstCmFcAgregarDpsCaja;
	}
	public HtmlOutputText getLblCmAgDcMensaje() {
		return lblCmAgDcMensaje;
	}
	public void setLblCmAgDcMensaje(HtmlOutputText lblCmAgDcMensaje) {
		this.lblCmAgDcMensaje = lblCmAgDcMensaje;
	}
	public HtmlOutputText getLblDtFechaDepsC() {
		return lblDtFechaDepsC;
	}
	public void setLblDtFechaDepsC(HtmlOutputText lblDtFechaDepsC) {
		this.lblDtFechaDepsC = lblDtFechaDepsC;
	}
	public HtmlOutputText getLblDtDcNoDocoConf() {
		return lblDtDcNoDocoConf;
	}
	public void setLblDtDcNoDocoConf(HtmlOutputText lblDtDcNoDocoConf) {
		this.lblDtDcNoDocoConf = lblDtDcNoDocoConf;
	}
	public HtmlOutputText getLblDtDcNoreferBanco() {
		return lblDtDcNoreferBanco;
	}
	public void setLblDtDcNoreferBanco(HtmlOutputText lblDtDcNoreferBanco) {
		this.lblDtDcNoreferBanco = lblDtDcNoreferBanco;
	}
	public HtmlOutputText getLblDtDcFechaConfirma() {
		return lblDtDcFechaConfirma;
	}
	public void setLblDtDcFechaConfirma(HtmlOutputText lblDtDcFechaConfirma) {
		this.lblDtDcFechaConfirma = lblDtDcFechaConfirma;
	}
	public HtmlOutputText getLblDtDcNoBatchConf() {
		return lblDtDcNoBatchConf;
	}
	public void setLblDtDcNoBatchConf(HtmlOutputText lblDtDcNoBatchConf) {
		this.lblDtDcNoBatchConf = lblDtDcNoBatchConf;
	}
	public HtmlOutputText getLblDtDcMontoBanco() {
		return lblDtDcMontoBanco;
	}
	public void setLblDtDcMontoBanco(HtmlOutputText lblDtDcMontoBanco) {
		this.lblDtDcMontoBanco = lblDtDcMontoBanco;
	}
	public HtmlDropDownList getDdlCmfcRangoMonto() {
		return ddlCmfcRangoMonto;
	}
	public void setDdlCmfcRangoMonto(HtmlDropDownList ddlCmfcRangoMonto) {
		this.ddlCmfcRangoMonto = ddlCmfcRangoMonto;
	}
	public HtmlOutputText getLblCmRsmBcoHistorico() {
		return lblCmRsmBcoHistorico;
	}
	public void setLblCmRsmBcoHistorico(HtmlOutputText lblCmRsmBcoHistorico) {
		this.lblCmRsmBcoHistorico = lblCmRsmBcoHistorico;
	}
	public HtmlInputTextarea getTxtCmRsmBcoHistorico() {
		return txtCmRsmBcoHistorico;
	}
	public void setTxtCmRsmBcoHistorico(HtmlInputTextarea txtCmRsmBcoHistorico) {
		this.txtCmRsmBcoHistorico = txtCmRsmBcoHistorico;
	}
	public HtmlInputText getTxtCmfcMontoDesdeDepCaja() {
		return txtCmfcMontoDesdeDepCaja;
	}
	public void setTxtCmfcMontoDesdeDepCaja(HtmlInputText txtCmfcMontoDesdeDepCaja) {
		this.txtCmfcMontoDesdeDepCaja = txtCmfcMontoDesdeDepCaja;
	}
	public HtmlInputText getTxtCmfcMontoDepMaxim() {
		return txtCmfcMontoDepMaxim;
	}
	public void setTxtCmfcMontoDepMaxim(HtmlInputText txtCmfcMontoDepMaxim) {
		this.txtCmfcMontoDepMaxim = txtCmfcMontoDepMaxim;
	}
	public HtmlOutputText getLblCnfAutoFecha() {
		return lblCnfAutoFecha;
	}
	public void setLblCnfAutoFecha(HtmlOutputText lblCnfAutoFecha) {
		this.lblCnfAutoFecha = lblCnfAutoFecha;
	}
	public HtmlDateChooser getDcCnfAutoFechaConfirma() {
		return dcCnfAutoFechaConfirma;
	}
	public void setDcCnfAutoFechaConfirma(HtmlDateChooser dcCnfAutoFechaConfirma) {
		this.dcCnfAutoFechaConfirma = dcCnfAutoFechaConfirma;
	}
	public HtmlOutputText getLblCnfManualFecha() {
		return lblCnfManualFecha;
	}
	public void setLblCnfManualFecha(HtmlOutputText lblCnfManualFecha) {
		this.lblCnfManualFecha = lblCnfManualFecha;
	}
	public HtmlDateChooser getDcCnfManualFConfirma() {
		return dcCnfManualFConfirma;
	}
	public void setDcCnfManualFConfirma(HtmlDateChooser dcCnfManualFConfirma) {
		this.dcCnfManualFConfirma = dcCnfManualFConfirma;
	}
	public HtmlDropDownList getDdlCaFtrCjaConfirma() {
		return ddlCaFtrCjaConfirma;
	}
	public void setDdlCaFtrCjaConfirma(HtmlDropDownList ddlCaFtrCjaConfirma) {
		this.ddlCaFtrCjaConfirma = ddlCaFtrCjaConfirma;
	}
	public HtmlDropDownList getDdlCaFtrCjaConfirmaMan() {
		return ddlCaFtrCjaConfirmaMan;
	}
	public void setDdlCaFtrCjaConfirmaMan(HtmlDropDownList ddlCaFtrCjaConfirmaMan) {
		this.ddlCaFtrCjaConfirmaMan = ddlCaFtrCjaConfirmaMan;
	}

}
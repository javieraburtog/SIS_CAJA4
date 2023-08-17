/**
 * 
 */
package pagecode.tilesContent;

import pagecode.PageCodeBase;
import com.infragistics.faces.menu.component.html.HtmlMenu;
import com.infragistics.faces.menu.component.html.HtmlMenuItem;
import javax.faces.component.html.HtmlInputText;
import com.infragistics.faces.shared.component.html.HtmlLink;
import javax.faces.component.html.HtmlOutputText;
import com.infragistics.faces.window.component.html.HtmlDialogWindowClientEvents;
import com.ibm.faces.component.html.HtmlJspPanel;
import com.ibm.faces.component.html.HtmlGraphicImageEx;
import com.infragistics.faces.window.component.html.HtmlDialogWindow;
import com.infragistics.faces.window.component.html.HtmlDialogWindowRoundedCorners;
import com.infragistics.faces.window.component.html.HtmlDialogWindowContentPane;
import com.infragistics.faces.window.component.html.HtmlDialogWindowAutoPostBackFlags;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
import com.infragistics.faces.input.component.html.HtmlCheckBox;
import com.infragistics.faces.grid.component.html.HtmlColumn;
import javax.faces.component.UINamingContainer;
import com.ibm.faces.component.html.HtmlScriptCollector;
import javax.faces.component.html.HtmlForm;
import com.ibm.faces.component.html.HtmlPanelSection;
import com.ibm.faces.component.html.HtmlInputHelperTypeahead;
import com.infragistics.faces.input.component.html.HtmlDateChooser;
import com.infragistics.faces.grid.component.html.HtmlGridView;
import com.infragistics.faces.grid.component.html.HtmlColumnSelectRow;
import javax.faces.component.html.HtmlPanelGrid;
import com.infragistics.faces.window.component.html.HtmlDialogWindowHeader;
import javax.faces.component.html.HtmlInputSecret;
import javax.faces.component.html.HtmlInputTextarea;
import com.infragistics.faces.grid.component.html.HtmlGridEditing;
import com.infragistics.faces.input.component.html.HtmlDropDownListClientEvents;
import com.infragistics.faces.window.component.html.HtmlDialogButtonCloseBox;
import com.infragistics.faces.input.component.html.HtmlDateChooserClientEvents;
import com.infragistics.faces.grid.component.html.HtmlGridAgFunction;

/**
 * @author Juan Ñamendi
 *
 */
public class PagoFacCredito2__bodyarea extends PageCodeBase {

	protected HtmlMenu menu1Cred;
	protected HtmlMenuItem item0Cred;
	protected HtmlMenuItem item1Cred;
	protected HtmlMenuItem item2Cred;
	protected HtmlLink lnkSearchCredito;
	protected HtmlDialogWindowClientEvents cledwCargando;
	protected HtmlJspPanel jspdwCargando;
	protected HtmlGraphicImageEx imagenCargando;
	protected HtmlDialogWindow dwCargando;
	protected HtmlDialogWindowRoundedCorners cledwCargando22;
	protected HtmlDialogWindowContentPane cpdwCargando;
	protected HtmlDialogWindowAutoPostBackFlags apbdwCargando;
	protected HtmlDropDownList ddlBanco;
	protected HtmlOutputText lblBanco;
	protected HtmlOutputText lblReferencia2;
	protected HtmlInputText txtReferencia2;
	protected HtmlOutputText lblReferencia3;
	protected HtmlInputText txtReferencia3;
	protected HtmlLink lnkRegistrarPago;
	protected HtmlOutputText lblMensajeCargando;
	protected HtmlOutputText lblTotalDetalleReciboA;
	protected HtmlColumn coTotalDetalleReciboA;
	protected HtmlOutputText lblTotalDetalleReciboA2;
	protected UINamingContainer vfCredito;
	protected HtmlScriptCollector scCredito;
	protected HtmlCheckBox chkSobranteDifrl;
	protected HtmlOutputText lblMarcaSobrDifer;
	protected HtmlForm frmCredito;
	protected HtmlOutputText lblTitulo1Contado;
	protected HtmlPanelSection section1;
	protected HtmlJspPanel jspPanel7;
	protected HtmlGraphicImageEx imageEx4;
	protected HtmlJspPanel jspPanel6;
	protected HtmlGraphicImageEx imageEx3;
	protected HtmlOutputText lblTipoBusqueda;
	protected HtmlDropDownList dropBusqueda;
	protected HtmlInputText txtParametro;
	protected HtmlInputHelperTypeahead thCredito;
	protected HtmlOutputText lblFiltroFechas;
	protected HtmlDateChooser dcFechaInicial;
	protected HtmlDateChooser dcrFechaFinal;
	protected HtmlGraphicImageEx imgLoader;
	protected HtmlOutputText txtMensaje;
	protected HtmlGridView gvFacsCredito;
	protected HtmlColumnSelectRow columnSelectRowRendererCred;
	protected HtmlLink lnkDetalleCredito;
	protected HtmlOutputText lblDetalleCredito;
	protected HtmlOutputText lblNofacturaCredito;
	protected HtmlOutputText lblNofacturaCredito2;
	protected HtmlOutputText lblTipofactura1Cred;
	protected HtmlOutputText lblTipofactura2Cred;
	protected HtmlOutputText lblPartida1;
	protected HtmlOutputText lblPartida2;
	protected HtmlOutputText lblNomcliCredito;
	protected HtmlOutputText lblNomcliCredito2;
	protected HtmlOutputText lblUninegCredito;
	protected HtmlOutputText lblUninegCredito2;
	protected HtmlOutputText lblUniNegCred3;
	protected HtmlOutputText lblTotalCredito;
	protected HtmlOutputText lblTotalCredito2;
	protected HtmlOutputText lblMonedaCredito;
	protected HtmlOutputText lblMonedaCredito2;
	protected HtmlOutputText lblFechaFacturaCredito;
	protected HtmlOutputText lblFechaFacturaCredito2;
	protected HtmlOutputText lblFechaFacturaVecto;
	protected HtmlOutputText lblFechaFacturaVecto2;
	protected HtmlCheckBox chkIngresoManual;
	protected HtmlOutputText lblNoTarjeta;
	protected HtmlInputText txtNoTarjeta;
	protected HtmlOutputText lblFechaVenceT;
	protected HtmlInputText txtFechaVenceT;
	protected HtmlOutputText lblBanda3;
	protected HtmlInputSecret track;
	protected HtmlGridView gvMetodosPago;
	protected HtmlColumn coEliminarPago;
	protected HtmlLink lnkEliminarDetalle;
	protected HtmlOutputText lblEliminarPago;
	protected HtmlOutputText lblMetodo;
	protected HtmlOutputText lblMetodo2;
	protected HtmlOutputText lblMoneda;
	protected HtmlOutputText lblMoneda22;
	protected HtmlOutputText lblMonto22;
	protected HtmlOutputText lblMonto222;
	protected HtmlOutputText lblTasa;
	protected HtmlOutputText lblTasa2;
	protected HtmlOutputText lblEquivDetalle;
	protected HtmlOutputText lblEquivDetalle2;
	protected HtmlOutputText lblReferencia29;
	protected HtmlOutputText lblReferencia19;
	protected HtmlOutputText lblReferencia222;
	protected HtmlOutputText lblReferencia22;
	protected HtmlOutputText lblReferencia322;
	protected HtmlOutputText lblReferencia32;
	protected HtmlOutputText lblReferencia323;
	protected HtmlOutputText lblReferencia33;
	protected HtmlOutputText lblTasaCambio;
	protected HtmlOutputText lblTasaJDE;
	protected HtmlLink lnkMostrarFijarTasaCambio;
	protected HtmlGridView gvFacCredito;
	protected HtmlColumn coEliminarFactura;
	protected HtmlLink lnkEliminarFactura;
	protected HtmlOutputText lblEliminarFactura;
	protected HtmlLink lnkDetalleFact;
	protected HtmlOutputText lblDetalleFacturaRecibo;
	protected HtmlOutputText lblNofacturaDetalle;
	protected HtmlOutputText lblNofacturaDetalle2;
	protected HtmlOutputText lblPartidaDetalleRecibo;
	protected HtmlOutputText lblPartidaDetalleRecibo2;
	protected HtmlOutputText lblTotalDetalleReciboC;
	protected HtmlOutputText lblTotalDetalleReciboC2;
	protected HtmlOutputText lblFechaDetalleRecibo;
	protected HtmlOutputText lblFechaDetalleRecibo2;
	protected HtmlOutputText lblTotalDetalleRecibo;
	protected HtmlOutputText lblTotalDetalleRecibo2;
	protected HtmlOutputText lblSubtotalCred;
	protected HtmlOutputText lblSubtotalCred2;
	protected HtmlOutputText lblIvaCred;
	protected HtmlOutputText lblIvaCred2;
	protected HtmlLink lnkAgregarFactura;
	protected HtmlOutputText lblConcepto;
	protected HtmlInputTextarea txtConcepto;
	protected HtmlOutputText lblMontoAplicar;
	protected HtmlOutputText lblMontoAplicar2;
	protected HtmlOutputText lblMontoRecibido;
	protected HtmlOutputText lblMontoRecibido2;
	protected HtmlOutputText lblCambio;
	protected HtmlPanelGrid grCambio;
	protected HtmlInputText txtCambioForaneo;
	protected HtmlOutputText lblPendienteDom;
	protected HtmlOutputText txtPendienteDom;
	protected HtmlOutputText lblCambioDomestico;
	protected HtmlOutputText txtCambioDomestico;
	protected HtmlLink lnkProcesarRecibo;
	protected HtmlDialogWindowHeader hdFijarTasaCambio;
	protected HtmlJspPanel jsPnlFijarTasaCambio;
	protected HtmlOutputText lblEtNuevaTasa;
	protected HtmlOutputText lblEtMotivoCambio;
	protected HtmlInputTextarea txtMotivoCambioTasa;
	protected HtmlOutputText lblMsgErrorCambioTasa;
	protected HtmlLink lnkFijarTasa;
	protected HtmlOutputText lblTituloContado80;
	protected HtmlOutputText text5;
	protected HtmlOutputText text4;
	protected HtmlJspPanel jspPanel100;
	protected HtmlColumn coDetalleCredito;
	protected HtmlColumn coNofacturaCredito;
	protected HtmlColumn coTipoFacturaCred;
	protected HtmlColumn coPartida;
	protected HtmlColumn coNomcliCredito;
	protected HtmlColumn coUninegCredito;
	protected HtmlColumn coTotalCredito;
	protected HtmlColumn coMonedaCredito;
	protected HtmlColumn coFechaFacturaCredito;
	protected HtmlColumn coFechaFacturaVecto;
	protected HtmlColumn coMetodo;
	protected HtmlColumn coMonedaDetalle;
	protected HtmlColumn coMonto;
	protected HtmlColumn coTasa;
	protected HtmlColumn coEquivalente;
	protected HtmlColumn coReferencia;
	protected HtmlColumn coReferencia2;
	protected HtmlColumn coReferencia3;
	protected HtmlColumn coReferencia4;
	protected HtmlGridEditing gdeId3;
	protected HtmlOutputText lblTasaCambio2;
	protected HtmlOutputText lblTasaJDE2;
	protected HtmlColumn coDetalleFacturaRecibo;
	protected HtmlColumn coNofactura;
	protected HtmlColumn coPartidaDetalleRecibo;
	protected HtmlColumn coTotalDetalleReciboC;
	protected HtmlColumn coFechaDetalleRecibo;
	protected HtmlColumn coTotalDetalleRecibo;
	protected HtmlColumn coSubtotalCred;
	protected HtmlColumn coIvaCred;
	protected HtmlGridEditing geActDetalle;
	protected HtmlOutputText lblTotalSeleccionado1;
	protected HtmlOutputText lblTotalSeleccionadoDomestico;
	protected HtmlOutputText lblTotalSeleccionadoForaneo;
	protected HtmlOutputText lblTotalFaltante1;
	protected HtmlOutputText lblTotalFaltanteDomestico;
	protected HtmlOutputText lblTotalFaltanteForaneo;
	protected HtmlOutputText lblSeleccionadosDet1;
	protected HtmlOutputText lblSeleccionadosDet;
	protected HtmlLink lnkCambio;
	protected HtmlOutputText txtCambio;
	protected HtmlLink lnkCancelarRecibo;
	protected HtmlDialogWindowAutoPostBackFlags apbReciboCredito100;
	protected HtmlDialogWindow dwFijarTasaCambio;
	protected HtmlDialogWindowClientEvents cleFijarTasaCambio;
	protected HtmlDialogWindowRoundedCorners rcFijarTasaCambio;
	protected HtmlDialogWindowContentPane cpFijarTasaCambio;
	protected HtmlInputText txtNuevaTasaCambio;
	protected HtmlJspPanel jsPnlFijarTasaCambio2;
	protected HtmlLink lnkCancelarFijarTasa;
	protected HtmlOutputText txtSeleccionados;
	protected HtmlLink lnkProcesarRecibo2;
	protected HtmlPanelGrid grdFiltrosBusqueda;
	protected HtmlDropDownListClientEvents cleddlComapaniaCre;
	protected HtmlDropDownListClientEvents cleddlUninegCre;
	protected HtmlDropDownList ddlComapaniaCre;
	protected HtmlDropDownList ddlSucursalCre;
	protected HtmlDropDownList ddlUninegCre;
	protected HtmlDialogWindowHeader hdAgregarFactura;
	protected HtmlJspPanel jspAgregarFactura;
	protected HtmlOutputText lblRangosNoF2;
	protected HtmlGridView gvAgregarFactura;
	protected HtmlColumnSelectRow columnSelectRowRendererCredAgregar;
	protected HtmlOutputText lblNofacturaAgregarFactura;
	protected HtmlOutputText lblNofacturaAgregarFactura2;
	protected HtmlOutputText lblTipofactura1AgregarFactura;
	protected HtmlOutputText lblTipofactura2AgregarFactura;
	protected HtmlOutputText lblPartida1AgregarFactura;
	protected HtmlOutputText lblPartida2AgregarFactura;
	protected HtmlOutputText lblUniNeg3AgregarFactura;
	protected HtmlOutputText lblUninegCreditoAdd;
	protected HtmlOutputText lblUninegCredito2Add;
	protected HtmlOutputText lblTotalAgregarFactura;
	protected HtmlOutputText lblTotalAgregarFactura2;
	protected HtmlOutputText lblFechaAgregarFactura;
	protected HtmlOutputText lblFechaAgregarFactura2;
	protected HtmlOutputText lblFechaFacturaVectoAgregarFactura;
	protected HtmlOutputText lblFechaFacturaVectoAgregarFactura2;
	protected HtmlLink lnkDetalleAgregarFactura;
	protected HtmlDialogWindow dwAgregarFactura;
	protected HtmlDialogWindowClientEvents cleAgregarFactura;
	protected HtmlDialogWindowRoundedCorners rcAgregarFactura;
	protected HtmlDialogWindowContentPane cpAgregarFactura;
	protected HtmlInputText txtNofacturaDesde;
	protected HtmlInputText txtNofacturaHasta;
	protected HtmlLink lnkBuscarAgregarFatura;
	protected HtmlColumn coNofacturaAgregarFactura;
	protected HtmlColumn coTipoFacturaAgregarFactura;
	protected HtmlColumn coPartidaAgregarFactura;
	protected HtmlColumn coUninegCreditoAdd;
	protected HtmlColumn coTotalAgregarFactura;
	protected HtmlColumn coFechaAgregarFactura;
	protected HtmlColumn coFechaFacturaVectoAgregarFactura;
	protected HtmlLink lnkAceptarAgregarFatura;
	protected HtmlDialogWindowAutoPostBackFlags apbAgregarFactura;
	protected HtmlDialogWindowHeader hdValida;
	protected HtmlJspPanel jspPane20;
	protected HtmlPanelGrid grdValida;
	protected HtmlGraphicImageEx imgValida;
	protected HtmlLink lnkCerrarValida;
	protected HtmlDialogWindow dwValidaContado;
	protected HtmlDialogWindowClientEvents cleValida;
	protected HtmlDialogWindowRoundedCorners rcValida;
	protected HtmlDialogWindowContentPane cpValida;
	protected HtmlOutputText lblValida;
	protected HtmlDialogWindowAutoPostBackFlags apbReciboContado2;
	protected HtmlDialogWindowHeader hdProcesaRecibo;
	protected HtmlJspPanel jspProcesa;
	protected HtmlPanelGrid grdProces;
	protected HtmlGraphicImageEx imgProcesa;
	protected HtmlLink lnkCerrarPagoMensaje;
	protected HtmlDialogWindow dwProcesa;
	protected HtmlDialogWindowClientEvents cleProcesaRecibo;
	protected HtmlDialogWindowRoundedCorners rcProcesaRecibo;
	protected HtmlDialogWindowContentPane cpProcesaRecibo;
	protected HtmlOutputText lblMensajeValidacionPrima;
	protected HtmlDialogWindowAutoPostBackFlags apbProcesaRecibo;
	protected HtmlDialogWindowHeader hdImprime;
	protected HtmlJspPanel jspPanel3;
	protected HtmlPanelGrid grid100;
	protected HtmlGraphicImageEx imageEx2Imprime;
	protected HtmlDialogWindow dwImprime;
	protected HtmlDialogWindowClientEvents cleImprime;
	protected HtmlDialogWindowRoundedCorners rcImprime;
	protected HtmlDialogWindowContentPane cpImprime;
	protected HtmlOutputText lblConfirmPrint;
	protected HtmlLink lnkCerrarMensaje13;
	protected HtmlLink lnkCerrarMensaje14;
	protected HtmlDialogWindowAutoPostBackFlags apbImprime;
	protected HtmlDialogWindowHeader hdMensajeDetalleCredito;
	protected HtmlJspPanel jspPanel5DetalleCredito;
	protected HtmlPanelGrid grid4DetalleCredito;
	protected HtmlGraphicImageEx imageEx10DetalleCredito;
	protected HtmlLink lnkAcptarDetalleCredito;
	protected HtmlDialogWindow dwMensajeDetalleCredito;
	protected HtmlDialogWindowClientEvents cleMensajeDetalleCredito;
	protected HtmlDialogWindowRoundedCorners rcMensajeDetalleCredito;
	protected HtmlDialogWindowContentPane cpMensajeDetalleContado;
	protected HtmlOutputText lblAlertDetalleContado;
	protected HtmlDialogWindowAutoPostBackFlags apbMensajeDetalleCredito;
	protected HtmlDialogWindowHeader hdAskCancel;
	protected HtmlJspPanel jspPanel3AskCancel;
	protected HtmlPanelGrid gridAskCancel;
	protected HtmlGraphicImageEx imageEx2AskCancel;
	protected HtmlLink lnkCerrarMensajeAskCancel;
	protected HtmlDialogWindow dwAskCancel;
	protected HtmlDialogWindowClientEvents cleAskCancel;
	protected HtmlDialogWindowRoundedCorners rcAskCancel;
	protected HtmlDialogWindowContentPane cpAskCancel;
	protected HtmlOutputText lblConfirmCancel;
	protected HtmlLink lnkCerrarAskCancel;
	protected HtmlDialogWindowAutoPostBackFlags apbAskCancel;
	protected HtmlDialogWindowHeader hdSolicitarAutorizacion;
	protected HtmlDialogButtonCloseBox clbAutoriza;
	protected HtmlJspPanel jspPanel23;
	protected HtmlOutputText lblMensajeAutorizacion;
	protected HtmlOutputText lblReferencia4;
	protected HtmlLink lnkProcesarSolicitud;
	protected HtmlDialogWindow dwAutoriza;
	protected HtmlDialogWindowClientEvents cleAutorizaContado;
	protected HtmlDialogWindowRoundedCorners rcAutorizaContado;
	protected HtmlDialogWindowContentPane cpAutorizaContado;
	protected HtmlPanelGrid grid2;
	protected HtmlInputText txtReferencia;
	protected HtmlOutputText lblAut;
	protected HtmlDropDownList ddlAutoriza;
	protected HtmlOutputText text2;
	protected HtmlDateChooser txtFecha;
	protected HtmlOutputText lblConcepto4;
	protected HtmlInputTextarea txtObs;
	protected HtmlLink lnkCancelarSolicitud;
	protected HtmlDialogWindowAutoPostBackFlags apbReciboContado;
	protected HtmlDialogWindowHeader hdDetalleFacturaCredito;
	protected HtmlJspPanel jspPanel4;
	protected HtmlOutputText text18;
	protected HtmlOutputText text20;
	protected HtmlOutputText lblCodigo23;
	protected HtmlOutputText text1;
	protected HtmlOutputText txtNomCliente;
	protected HtmlOutputText text3;
	protected HtmlOutputText lblCompania;
	protected HtmlOutputText txtCompania;
	protected HtmlOutputText text22;
	protected HtmlOutputText text23;
	protected HtmlOutputText lblFechalm;
	protected HtmlOutputText txtFechalm;
	protected HtmlOutputText lblFechaVenc;
	protected HtmlOutputText txtFechaVenc;
	protected HtmlOutputText lblNoOrden;
	protected HtmlOutputText txtNoOrden;
	protected HtmlOutputText lblTipoOrden;
	protected HtmlOutputText txtTipoOrden;
	protected HtmlOutputText lblObservaciones;
	protected HtmlOutputText txtObservaciones;
	protected HtmlOutputText lblReferenciaFactura;
	protected HtmlOutputText txtReferenciaFactura;
	protected HtmlOutputText lblNoBatch;
	protected HtmlOutputText txtNoBatch;
	protected HtmlOutputText lblFechaBatch;
	protected HtmlOutputText txtFechaBatch;
	protected HtmlOutputText lblFechaImp;
	protected HtmlOutputText txtFechaImp;
	protected HtmlOutputText lblCompens;
	protected HtmlOutputText txtCompens;
	protected HtmlOutputText lblFechaVenDecto;
	protected HtmlOutputText txtFechaVenDecto;
	protected HtmlOutputText lblCondicion;
	protected HtmlOutputText txtCondicion;
	protected HtmlOutputText lblDescuento;
	protected HtmlOutputText txtDescuento;
	protected HtmlOutputText lblDescuentoAplicado;
	protected HtmlOutputText txtDescuentoAplicado;
	protected HtmlOutputText text24;
	protected HtmlOutputText txtSubtotalDetCredito;
	protected HtmlOutputText text28;
	protected HtmlOutputText txtIvaDetCredito;
	protected HtmlOutputText text30;
	protected HtmlOutputText txtTotalDetCredito;
	protected HtmlOutputText text230;
	protected HtmlOutputText txtPendienteDetCredito;
	protected HtmlGridView gvDetalleFacCredito;
	protected HtmlColumn coCoditem;
	protected HtmlOutputText lblCoditem1;
	protected HtmlOutputText lblCoditem2;
	protected HtmlOutputText lblDescitem1;
	protected HtmlOutputText lblDescitem2;
	protected HtmlOutputText lblCantDetalle1;
	protected HtmlOutputText lblCantDetalle2;
	protected HtmlOutputText lblPrecionunitDetalle1;
	protected HtmlOutputText lblPrecionunitDetalle2;
	protected HtmlOutputText lblImpuestoDetalle1;
	protected HtmlOutputText lblImpuestoDetalle2;
	protected HtmlLink lnkAceptarDetalleFacturaCredito;
	protected HtmlDialogWindow dgwDetalleFacturaCredito;
	protected HtmlDialogWindowClientEvents cleDetalleFacturaCredito;
	protected HtmlDialogWindowRoundedCorners rcDetalleFacturaCredito;
	protected HtmlDialogWindowContentPane cpDetalleFacturaCredito;
	protected HtmlOutputText txtFechaFactura;
	protected HtmlOutputText txtNoFactura;
	protected HtmlOutputText lblTipoFactura;
	protected HtmlOutputText txtTipoFactura;
	protected HtmlOutputText txtCodigoCliente;
	protected HtmlDropDownList ddlDetalleContado;
	protected HtmlOutputText text3333;
	protected HtmlColumn coDescitemCont;
	protected HtmlColumn coCant;
	protected HtmlColumn coPreciounit;
	protected HtmlColumn coImpuesto;
	protected HtmlDialogWindowAutoPostBackFlags apbDetalleFacturaCredito;
	protected HtmlDialogWindowHeader hdReciboContado;
	protected HtmlJspPanel jspPanel1;
	protected HtmlOutputText lblFechaReciboCont;
	protected HtmlOutputText txtFechaRecibo;
	protected HtmlDateChooser txtFecham;
	protected HtmlDateChooserClientEvents dcClientEvents;
	protected HtmlOutputText lblNumRecibo;
	protected HtmlPanelGrid grid5;
	protected HtmlOutputText lblNumeroRecibo;
	protected HtmlOutputText lblCliente;
	protected HtmlOutputText lblCodigoSearch;
	protected HtmlOutputText lblTipoRecibo;
	protected HtmlDropDownList ddlTipoRecibo;
	protected HtmlOutputText lblMetodosPago;
	protected HtmlDropDownList ddlMetodoPago;
	protected HtmlOutputText lbletVouchermanual;
	protected HtmlCheckBox chkVoucherManual;
	protected HtmlOutputText lblMonto;
	protected HtmlInputText txtMonto;
	protected HtmlOutputText lblAfiliado;
	protected HtmlDropDownList ddlAfiliado;
	protected HtmlOutputText lblReferencia1;
	protected HtmlInputText txtReferencia1;
	protected HtmlDialogWindow dwRecibosCredito;
	protected HtmlDialogWindowClientEvents cleReciboConbtado;
	protected HtmlDialogWindowRoundedCorners rcREciboContado;
	protected HtmlDialogWindowContentPane cpReciboContado;
	protected HtmlOutputText lblNumRecm;
	protected HtmlInputText txtNumRec;
	protected HtmlOutputText lblCod;
	protected HtmlOutputText lblNombreSearch;
	protected HtmlOutputText lblNom;
	protected HtmlDropDownList ddlMoneda;
	protected HtmlDropDownListClientEvents clecmbFiltroMonedas;
	protected HtmlDropDownList cmbFiltroMonedas;
	protected HtmlOutputText lblComapaniaCre;
	protected HtmlOutputText lblSucursalCre;
	protected HtmlOutputText lblUninegCre;
	protected HtmlOutputText lblFiltroMoneda;
	protected HtmlGridAgFunction agFnContarDis;
	protected HtmlMenu getMenu1Cred() {
		if (menu1Cred == null) {
			menu1Cred = (HtmlMenu) findComponentInRoot("menu1Cred");
		}
		return menu1Cred;
	}

	protected HtmlMenuItem getItem0Cred() {
		if (item0Cred == null) {
			item0Cred = (HtmlMenuItem) findComponentInRoot("item0Cred");
		}
		return item0Cred;
	}

	protected HtmlMenuItem getItem1Cred() {
		if (item1Cred == null) {
			item1Cred = (HtmlMenuItem) findComponentInRoot("item1Cred");
		}
		return item1Cred;
	}

	protected HtmlMenuItem getItem2Cred() {
		if (item2Cred == null) {
			item2Cred = (HtmlMenuItem) findComponentInRoot("item2Cred");
		}
		return item2Cred;
	}

	protected HtmlLink getLnkSearchCredito() {
		if (lnkSearchCredito == null) {
			lnkSearchCredito = (HtmlLink) findComponentInRoot("lnkSearchCredito");
		}
		return lnkSearchCredito;
	}

	protected HtmlDialogWindowClientEvents getCledwCargando() {
		if (cledwCargando == null) {
			cledwCargando = (HtmlDialogWindowClientEvents) findComponentInRoot("cledwCargando");
		}
		return cledwCargando;
	}

	protected HtmlJspPanel getJspdwCargando() {
		if (jspdwCargando == null) {
			jspdwCargando = (HtmlJspPanel) findComponentInRoot("jspdwCargando");
		}
		return jspdwCargando;
	}

	protected HtmlGraphicImageEx getImagenCargando() {
		if (imagenCargando == null) {
			imagenCargando = (HtmlGraphicImageEx) findComponentInRoot("imagenCargando");
		}
		return imagenCargando;
	}

	protected HtmlDialogWindow getDwCargando() {
		if (dwCargando == null) {
			dwCargando = (HtmlDialogWindow) findComponentInRoot("dwCargando");
		}
		return dwCargando;
	}

	protected HtmlDialogWindowRoundedCorners getCledwCargando22() {
		if (cledwCargando22 == null) {
			cledwCargando22 = (HtmlDialogWindowRoundedCorners) findComponentInRoot("cledwCargando22");
		}
		return cledwCargando22;
	}

	protected HtmlDialogWindowContentPane getCpdwCargando() {
		if (cpdwCargando == null) {
			cpdwCargando = (HtmlDialogWindowContentPane) findComponentInRoot("cpdwCargando");
		}
		return cpdwCargando;
	}

	protected HtmlDialogWindowAutoPostBackFlags getApbdwCargando() {
		if (apbdwCargando == null) {
			apbdwCargando = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbdwCargando");
		}
		return apbdwCargando;
	}

	protected HtmlDropDownList getDdlBanco() {
		if (ddlBanco == null) {
			ddlBanco = (HtmlDropDownList) findComponentInRoot("ddlBanco");
		}
		return ddlBanco;
	}

	protected HtmlOutputText getLblBanco() {
		if (lblBanco == null) {
			lblBanco = (HtmlOutputText) findComponentInRoot("lblBanco");
		}
		return lblBanco;
	}

	protected HtmlOutputText getLblReferencia2() {
		if (lblReferencia2 == null) {
			lblReferencia2 = (HtmlOutputText) findComponentInRoot("lblReferencia2");
		}
		return lblReferencia2;
	}

	protected HtmlInputText getTxtReferencia2() {
		if (txtReferencia2 == null) {
			txtReferencia2 = (HtmlInputText) findComponentInRoot("txtReferencia2");
		}
		return txtReferencia2;
	}

	protected HtmlOutputText getLblReferencia3() {
		if (lblReferencia3 == null) {
			lblReferencia3 = (HtmlOutputText) findComponentInRoot("lblReferencia3");
		}
		return lblReferencia3;
	}

	protected HtmlInputText getTxtReferencia3() {
		if (txtReferencia3 == null) {
			txtReferencia3 = (HtmlInputText) findComponentInRoot("txtReferencia3");
		}
		return txtReferencia3;
	}

	protected HtmlLink getLnkRegistrarPago() {
		if (lnkRegistrarPago == null) {
			lnkRegistrarPago = (HtmlLink) findComponentInRoot("lnkRegistrarPago");
		}
		return lnkRegistrarPago;
	}

	protected HtmlOutputText getLblMensajeCargando() {
		if (lblMensajeCargando == null) {
			lblMensajeCargando = (HtmlOutputText) findComponentInRoot("lblMensajeCargando");
		}
		return lblMensajeCargando;
	}

	protected HtmlOutputText getLblTotalDetalleReciboA() {
		if (lblTotalDetalleReciboA == null) {
			lblTotalDetalleReciboA = (HtmlOutputText) findComponentInRoot("lblTotalDetalleReciboA");
		}
		return lblTotalDetalleReciboA;
	}

	protected HtmlColumn getCoTotalDetalleReciboA() {
		if (coTotalDetalleReciboA == null) {
			coTotalDetalleReciboA = (HtmlColumn) findComponentInRoot("coTotalDetalleReciboA");
		}
		return coTotalDetalleReciboA;
	}

	protected HtmlOutputText getLblTotalDetalleReciboA2() {
		if (lblTotalDetalleReciboA2 == null) {
			lblTotalDetalleReciboA2 = (HtmlOutputText) findComponentInRoot("lblTotalDetalleReciboA2");
		}
		return lblTotalDetalleReciboA2;
	}

	protected UINamingContainer getVfCredito() {
		if (vfCredito == null) {
			vfCredito = (UINamingContainer) findComponentInRoot("vfCredito");
		}
		return vfCredito;
	}

	protected HtmlScriptCollector getScCredito() {
		if (scCredito == null) {
			scCredito = (HtmlScriptCollector) findComponentInRoot("scCredito");
		}
		return scCredito;
	}

	protected HtmlCheckBox getChkSobranteDifrl() {
		if (chkSobranteDifrl == null) {
			chkSobranteDifrl = (HtmlCheckBox) findComponentInRoot("chkSobranteDifrl");
		}
		return chkSobranteDifrl;
	}

	protected HtmlOutputText getLblMarcaSobrDifer() {
		if (lblMarcaSobrDifer == null) {
			lblMarcaSobrDifer = (HtmlOutputText) findComponentInRoot("lblMarcaSobrDifer");
		}
		return lblMarcaSobrDifer;
	}

	protected HtmlForm getFrmCredito() {
		if (frmCredito == null) {
			frmCredito = (HtmlForm) findComponentInRoot("frmCredito");
		}
		return frmCredito;
	}

	protected HtmlOutputText getLblTitulo1Contado() {
		if (lblTitulo1Contado == null) {
			lblTitulo1Contado = (HtmlOutputText) findComponentInRoot("lblTitulo1Contado");
		}
		return lblTitulo1Contado;
	}

	protected HtmlPanelSection getSection1() {
		if (section1 == null) {
			section1 = (HtmlPanelSection) findComponentInRoot("section1");
		}
		return section1;
	}

	protected HtmlJspPanel getJspPanel7() {
		if (jspPanel7 == null) {
			jspPanel7 = (HtmlJspPanel) findComponentInRoot("jspPanel7");
		}
		return jspPanel7;
	}

	protected HtmlGraphicImageEx getImageEx4() {
		if (imageEx4 == null) {
			imageEx4 = (HtmlGraphicImageEx) findComponentInRoot("imageEx4");
		}
		return imageEx4;
	}

	protected HtmlJspPanel getJspPanel6() {
		if (jspPanel6 == null) {
			jspPanel6 = (HtmlJspPanel) findComponentInRoot("jspPanel6");
		}
		return jspPanel6;
	}

	protected HtmlGraphicImageEx getImageEx3() {
		if (imageEx3 == null) {
			imageEx3 = (HtmlGraphicImageEx) findComponentInRoot("imageEx3");
		}
		return imageEx3;
	}

	protected HtmlOutputText getLblTipoBusqueda() {
		if (lblTipoBusqueda == null) {
			lblTipoBusqueda = (HtmlOutputText) findComponentInRoot("lblTipoBusqueda");
		}
		return lblTipoBusqueda;
	}

	protected HtmlDropDownList getDropBusqueda() {
		if (dropBusqueda == null) {
			dropBusqueda = (HtmlDropDownList) findComponentInRoot("dropBusqueda");
		}
		return dropBusqueda;
	}

	protected HtmlInputText getTxtParametro() {
		if (txtParametro == null) {
			txtParametro = (HtmlInputText) findComponentInRoot("txtParametro");
		}
		return txtParametro;
	}

	protected HtmlInputHelperTypeahead getThCredito() {
		if (thCredito == null) {
			thCredito = (HtmlInputHelperTypeahead) findComponentInRoot("thCredito");
		}
		return thCredito;
	}

	protected HtmlOutputText getLblFiltroFechas() {
		if (lblFiltroFechas == null) {
			lblFiltroFechas = (HtmlOutputText) findComponentInRoot("lblFiltroFechas");
		}
		return lblFiltroFechas;
	}

	protected HtmlDateChooser getDcFechaInicial() {
		if (dcFechaInicial == null) {
			dcFechaInicial = (HtmlDateChooser) findComponentInRoot("dcFechaInicial");
		}
		return dcFechaInicial;
	}

	protected HtmlDateChooser getDcrFechaFinal() {
		if (dcrFechaFinal == null) {
			dcrFechaFinal = (HtmlDateChooser) findComponentInRoot("dcrFechaFinal");
		}
		return dcrFechaFinal;
	}

	protected HtmlGraphicImageEx getImgLoader() {
		if (imgLoader == null) {
			imgLoader = (HtmlGraphicImageEx) findComponentInRoot("imgLoader");
		}
		return imgLoader;
	}

	protected HtmlOutputText getTxtMensaje() {
		if (txtMensaje == null) {
			txtMensaje = (HtmlOutputText) findComponentInRoot("txtMensaje");
		}
		return txtMensaje;
	}

	protected HtmlGridView getGvFacsCredito() {
		if (gvFacsCredito == null) {
			gvFacsCredito = (HtmlGridView) findComponentInRoot("gvFacsCredito");
		}
		return gvFacsCredito;
	}

	protected HtmlColumnSelectRow getColumnSelectRowRendererCred() {
		if (columnSelectRowRendererCred == null) {
			columnSelectRowRendererCred = (HtmlColumnSelectRow) findComponentInRoot("columnSelectRowRendererCred");
		}
		return columnSelectRowRendererCred;
	}

	protected HtmlLink getLnkDetalleCredito() {
		if (lnkDetalleCredito == null) {
			lnkDetalleCredito = (HtmlLink) findComponentInRoot("lnkDetalleCredito");
		}
		return lnkDetalleCredito;
	}

	protected HtmlOutputText getLblDetalleCredito() {
		if (lblDetalleCredito == null) {
			lblDetalleCredito = (HtmlOutputText) findComponentInRoot("lblDetalleCredito");
		}
		return lblDetalleCredito;
	}

	protected HtmlOutputText getLblNofacturaCredito() {
		if (lblNofacturaCredito == null) {
			lblNofacturaCredito = (HtmlOutputText) findComponentInRoot("lblNofacturaCredito");
		}
		return lblNofacturaCredito;
	}

	protected HtmlOutputText getLblNofacturaCredito2() {
		if (lblNofacturaCredito2 == null) {
			lblNofacturaCredito2 = (HtmlOutputText) findComponentInRoot("lblNofacturaCredito2");
		}
		return lblNofacturaCredito2;
	}

	protected HtmlOutputText getLblTipofactura1Cred() {
		if (lblTipofactura1Cred == null) {
			lblTipofactura1Cred = (HtmlOutputText) findComponentInRoot("lblTipofactura1Cred");
		}
		return lblTipofactura1Cred;
	}

	protected HtmlOutputText getLblTipofactura2Cred() {
		if (lblTipofactura2Cred == null) {
			lblTipofactura2Cred = (HtmlOutputText) findComponentInRoot("lblTipofactura2Cred");
		}
		return lblTipofactura2Cred;
	}

	protected HtmlOutputText getLblPartida1() {
		if (lblPartida1 == null) {
			lblPartida1 = (HtmlOutputText) findComponentInRoot("lblPartida1");
		}
		return lblPartida1;
	}

	protected HtmlOutputText getLblPartida2() {
		if (lblPartida2 == null) {
			lblPartida2 = (HtmlOutputText) findComponentInRoot("lblPartida2");
		}
		return lblPartida2;
	}

	protected HtmlOutputText getLblNomcliCredito() {
		if (lblNomcliCredito == null) {
			lblNomcliCredito = (HtmlOutputText) findComponentInRoot("lblNomcliCredito");
		}
		return lblNomcliCredito;
	}

	protected HtmlOutputText getLblNomcliCredito2() {
		if (lblNomcliCredito2 == null) {
			lblNomcliCredito2 = (HtmlOutputText) findComponentInRoot("lblNomcliCredito2");
		}
		return lblNomcliCredito2;
	}

	protected HtmlOutputText getLblUninegCredito() {
		if (lblUninegCredito == null) {
			lblUninegCredito = (HtmlOutputText) findComponentInRoot("lblUninegCredito");
		}
		return lblUninegCredito;
	}

	protected HtmlOutputText getLblUninegCredito2() {
		if (lblUninegCredito2 == null) {
			lblUninegCredito2 = (HtmlOutputText) findComponentInRoot("lblUninegCredito2");
		}
		return lblUninegCredito2;
	}

	protected HtmlOutputText getLblUniNegCred3() {
		if (lblUniNegCred3 == null) {
			lblUniNegCred3 = (HtmlOutputText) findComponentInRoot("lblUniNegCred3");
		}
		return lblUniNegCred3;
	}

	protected HtmlOutputText getLblTotalCredito() {
		if (lblTotalCredito == null) {
			lblTotalCredito = (HtmlOutputText) findComponentInRoot("lblTotalCredito");
		}
		return lblTotalCredito;
	}

	protected HtmlOutputText getLblTotalCredito2() {
		if (lblTotalCredito2 == null) {
			lblTotalCredito2 = (HtmlOutputText) findComponentInRoot("lblTotalCredito2");
		}
		return lblTotalCredito2;
	}

	protected HtmlOutputText getLblMonedaCredito() {
		if (lblMonedaCredito == null) {
			lblMonedaCredito = (HtmlOutputText) findComponentInRoot("lblMonedaCredito");
		}
		return lblMonedaCredito;
	}

	protected HtmlOutputText getLblMonedaCredito2() {
		if (lblMonedaCredito2 == null) {
			lblMonedaCredito2 = (HtmlOutputText) findComponentInRoot("lblMonedaCredito2");
		}
		return lblMonedaCredito2;
	}

	protected HtmlOutputText getLblFechaFacturaCredito() {
		if (lblFechaFacturaCredito == null) {
			lblFechaFacturaCredito = (HtmlOutputText) findComponentInRoot("lblFechaFacturaCredito");
		}
		return lblFechaFacturaCredito;
	}

	protected HtmlOutputText getLblFechaFacturaCredito2() {
		if (lblFechaFacturaCredito2 == null) {
			lblFechaFacturaCredito2 = (HtmlOutputText) findComponentInRoot("lblFechaFacturaCredito2");
		}
		return lblFechaFacturaCredito2;
	}

	protected HtmlOutputText getLblFechaFacturaVecto() {
		if (lblFechaFacturaVecto == null) {
			lblFechaFacturaVecto = (HtmlOutputText) findComponentInRoot("lblFechaFacturaVecto");
		}
		return lblFechaFacturaVecto;
	}

	protected HtmlOutputText getLblFechaFacturaVecto2() {
		if (lblFechaFacturaVecto2 == null) {
			lblFechaFacturaVecto2 = (HtmlOutputText) findComponentInRoot("lblFechaFacturaVecto2");
		}
		return lblFechaFacturaVecto2;
	}

	protected HtmlCheckBox getChkIngresoManual() {
		if (chkIngresoManual == null) {
			chkIngresoManual = (HtmlCheckBox) findComponentInRoot("chkIngresoManual");
		}
		return chkIngresoManual;
	}

	protected HtmlOutputText getLblNoTarjeta() {
		if (lblNoTarjeta == null) {
			lblNoTarjeta = (HtmlOutputText) findComponentInRoot("lblNoTarjeta");
		}
		return lblNoTarjeta;
	}

	protected HtmlInputText getTxtNoTarjeta() {
		if (txtNoTarjeta == null) {
			txtNoTarjeta = (HtmlInputText) findComponentInRoot("txtNoTarjeta");
		}
		return txtNoTarjeta;
	}

	protected HtmlOutputText getLblFechaVenceT() {
		if (lblFechaVenceT == null) {
			lblFechaVenceT = (HtmlOutputText) findComponentInRoot("lblFechaVenceT");
		}
		return lblFechaVenceT;
	}

	protected HtmlInputText getTxtFechaVenceT() {
		if (txtFechaVenceT == null) {
			txtFechaVenceT = (HtmlInputText) findComponentInRoot("txtFechaVenceT");
		}
		return txtFechaVenceT;
	}

	protected HtmlOutputText getLblBanda3() {
		if (lblBanda3 == null) {
			lblBanda3 = (HtmlOutputText) findComponentInRoot("lblBanda3");
		}
		return lblBanda3;
	}

	protected HtmlInputSecret getTrack() {
		if (track == null) {
			track = (HtmlInputSecret) findComponentInRoot("track");
		}
		return track;
	}

	protected HtmlGridView getGvMetodosPago() {
		if (gvMetodosPago == null) {
			gvMetodosPago = (HtmlGridView) findComponentInRoot("gvMetodosPago");
		}
		return gvMetodosPago;
	}

	protected HtmlColumn getCoEliminarPago() {
		if (coEliminarPago == null) {
			coEliminarPago = (HtmlColumn) findComponentInRoot("coEliminarPago");
		}
		return coEliminarPago;
	}

	protected HtmlLink getLnkEliminarDetalle() {
		if (lnkEliminarDetalle == null) {
			lnkEliminarDetalle = (HtmlLink) findComponentInRoot("lnkEliminarDetalle");
		}
		return lnkEliminarDetalle;
	}

	protected HtmlOutputText getLblEliminarPago() {
		if (lblEliminarPago == null) {
			lblEliminarPago = (HtmlOutputText) findComponentInRoot("lblEliminarPago");
		}
		return lblEliminarPago;
	}

	protected HtmlOutputText getLblMetodo() {
		if (lblMetodo == null) {
			lblMetodo = (HtmlOutputText) findComponentInRoot("lblMetodo");
		}
		return lblMetodo;
	}

	protected HtmlOutputText getLblMetodo2() {
		if (lblMetodo2 == null) {
			lblMetodo2 = (HtmlOutputText) findComponentInRoot("lblMetodo2");
		}
		return lblMetodo2;
	}

	protected HtmlOutputText getLblMoneda() {
		if (lblMoneda == null) {
			lblMoneda = (HtmlOutputText) findComponentInRoot("lblMoneda");
		}
		return lblMoneda;
	}

	protected HtmlOutputText getLblMoneda22() {
		if (lblMoneda22 == null) {
			lblMoneda22 = (HtmlOutputText) findComponentInRoot("lblMoneda22");
		}
		return lblMoneda22;
	}

	protected HtmlOutputText getLblMonto22() {
		if (lblMonto22 == null) {
			lblMonto22 = (HtmlOutputText) findComponentInRoot("lblMonto22");
		}
		return lblMonto22;
	}

	protected HtmlOutputText getLblMonto222() {
		if (lblMonto222 == null) {
			lblMonto222 = (HtmlOutputText) findComponentInRoot("lblMonto222");
		}
		return lblMonto222;
	}

	protected HtmlOutputText getLblTasa() {
		if (lblTasa == null) {
			lblTasa = (HtmlOutputText) findComponentInRoot("lblTasa");
		}
		return lblTasa;
	}

	protected HtmlOutputText getLblTasa2() {
		if (lblTasa2 == null) {
			lblTasa2 = (HtmlOutputText) findComponentInRoot("lblTasa2");
		}
		return lblTasa2;
	}

	protected HtmlOutputText getLblEquivDetalle() {
		if (lblEquivDetalle == null) {
			lblEquivDetalle = (HtmlOutputText) findComponentInRoot("lblEquivDetalle");
		}
		return lblEquivDetalle;
	}

	protected HtmlOutputText getLblEquivDetalle2() {
		if (lblEquivDetalle2 == null) {
			lblEquivDetalle2 = (HtmlOutputText) findComponentInRoot("lblEquivDetalle2");
		}
		return lblEquivDetalle2;
	}

	protected HtmlOutputText getLblReferencia29() {
		if (lblReferencia29 == null) {
			lblReferencia29 = (HtmlOutputText) findComponentInRoot("lblReferencia29");
		}
		return lblReferencia29;
	}

	protected HtmlOutputText getLblReferencia19() {
		if (lblReferencia19 == null) {
			lblReferencia19 = (HtmlOutputText) findComponentInRoot("lblReferencia19");
		}
		return lblReferencia19;
	}

	protected HtmlOutputText getLblReferencia222() {
		if (lblReferencia222 == null) {
			lblReferencia222 = (HtmlOutputText) findComponentInRoot("lblReferencia222");
		}
		return lblReferencia222;
	}

	protected HtmlOutputText getLblReferencia22() {
		if (lblReferencia22 == null) {
			lblReferencia22 = (HtmlOutputText) findComponentInRoot("lblReferencia22");
		}
		return lblReferencia22;
	}

	protected HtmlOutputText getLblReferencia322() {
		if (lblReferencia322 == null) {
			lblReferencia322 = (HtmlOutputText) findComponentInRoot("lblReferencia322");
		}
		return lblReferencia322;
	}

	protected HtmlOutputText getLblReferencia32() {
		if (lblReferencia32 == null) {
			lblReferencia32 = (HtmlOutputText) findComponentInRoot("lblReferencia32");
		}
		return lblReferencia32;
	}

	protected HtmlOutputText getLblReferencia323() {
		if (lblReferencia323 == null) {
			lblReferencia323 = (HtmlOutputText) findComponentInRoot("lblReferencia323");
		}
		return lblReferencia323;
	}

	protected HtmlOutputText getLblReferencia33() {
		if (lblReferencia33 == null) {
			lblReferencia33 = (HtmlOutputText) findComponentInRoot("lblReferencia33");
		}
		return lblReferencia33;
	}

	protected HtmlOutputText getLblTasaCambio() {
		if (lblTasaCambio == null) {
			lblTasaCambio = (HtmlOutputText) findComponentInRoot("lblTasaCambio");
		}
		return lblTasaCambio;
	}

	protected HtmlOutputText getLblTasaJDE() {
		if (lblTasaJDE == null) {
			lblTasaJDE = (HtmlOutputText) findComponentInRoot("lblTasaJDE");
		}
		return lblTasaJDE;
	}

	protected HtmlLink getLnkMostrarFijarTasaCambio() {
		if (lnkMostrarFijarTasaCambio == null) {
			lnkMostrarFijarTasaCambio = (HtmlLink) findComponentInRoot("lnkMostrarFijarTasaCambio");
		}
		return lnkMostrarFijarTasaCambio;
	}

	protected HtmlGridView getGvFacCredito() {
		if (gvFacCredito == null) {
			gvFacCredito = (HtmlGridView) findComponentInRoot("gvFacCredito");
		}
		return gvFacCredito;
	}

	protected HtmlColumn getCoEliminarFactura() {
		if (coEliminarFactura == null) {
			coEliminarFactura = (HtmlColumn) findComponentInRoot("coEliminarFactura");
		}
		return coEliminarFactura;
	}

	protected HtmlLink getLnkEliminarFactura() {
		if (lnkEliminarFactura == null) {
			lnkEliminarFactura = (HtmlLink) findComponentInRoot("lnkEliminarFactura");
		}
		return lnkEliminarFactura;
	}

	protected HtmlOutputText getLblEliminarFactura() {
		if (lblEliminarFactura == null) {
			lblEliminarFactura = (HtmlOutputText) findComponentInRoot("lblEliminarFactura");
		}
		return lblEliminarFactura;
	}

	protected HtmlLink getLnkDetalleFact() {
		if (lnkDetalleFact == null) {
			lnkDetalleFact = (HtmlLink) findComponentInRoot("lnkDetalleFact");
		}
		return lnkDetalleFact;
	}

	protected HtmlOutputText getLblDetalleFacturaRecibo() {
		if (lblDetalleFacturaRecibo == null) {
			lblDetalleFacturaRecibo = (HtmlOutputText) findComponentInRoot("lblDetalleFacturaRecibo");
		}
		return lblDetalleFacturaRecibo;
	}

	protected HtmlOutputText getLblNofacturaDetalle() {
		if (lblNofacturaDetalle == null) {
			lblNofacturaDetalle = (HtmlOutputText) findComponentInRoot("lblNofacturaDetalle");
		}
		return lblNofacturaDetalle;
	}

	protected HtmlOutputText getLblNofacturaDetalle2() {
		if (lblNofacturaDetalle2 == null) {
			lblNofacturaDetalle2 = (HtmlOutputText) findComponentInRoot("lblNofacturaDetalle2");
		}
		return lblNofacturaDetalle2;
	}

	protected HtmlOutputText getLblPartidaDetalleRecibo() {
		if (lblPartidaDetalleRecibo == null) {
			lblPartidaDetalleRecibo = (HtmlOutputText) findComponentInRoot("lblPartidaDetalleRecibo");
		}
		return lblPartidaDetalleRecibo;
	}

	protected HtmlOutputText getLblPartidaDetalleRecibo2() {
		if (lblPartidaDetalleRecibo2 == null) {
			lblPartidaDetalleRecibo2 = (HtmlOutputText) findComponentInRoot("lblPartidaDetalleRecibo2");
		}
		return lblPartidaDetalleRecibo2;
	}

	protected HtmlOutputText getLblTotalDetalleReciboC() {
		if (lblTotalDetalleReciboC == null) {
			lblTotalDetalleReciboC = (HtmlOutputText) findComponentInRoot("lblTotalDetalleReciboC");
		}
		return lblTotalDetalleReciboC;
	}

	protected HtmlOutputText getLblTotalDetalleReciboC2() {
		if (lblTotalDetalleReciboC2 == null) {
			lblTotalDetalleReciboC2 = (HtmlOutputText) findComponentInRoot("lblTotalDetalleReciboC2");
		}
		return lblTotalDetalleReciboC2;
	}

	protected HtmlOutputText getLblFechaDetalleRecibo() {
		if (lblFechaDetalleRecibo == null) {
			lblFechaDetalleRecibo = (HtmlOutputText) findComponentInRoot("lblFechaDetalleRecibo");
		}
		return lblFechaDetalleRecibo;
	}

	protected HtmlOutputText getLblFechaDetalleRecibo2() {
		if (lblFechaDetalleRecibo2 == null) {
			lblFechaDetalleRecibo2 = (HtmlOutputText) findComponentInRoot("lblFechaDetalleRecibo2");
		}
		return lblFechaDetalleRecibo2;
	}

	protected HtmlOutputText getLblTotalDetalleRecibo() {
		if (lblTotalDetalleRecibo == null) {
			lblTotalDetalleRecibo = (HtmlOutputText) findComponentInRoot("lblTotalDetalleRecibo");
		}
		return lblTotalDetalleRecibo;
	}

	protected HtmlOutputText getLblTotalDetalleRecibo2() {
		if (lblTotalDetalleRecibo2 == null) {
			lblTotalDetalleRecibo2 = (HtmlOutputText) findComponentInRoot("lblTotalDetalleRecibo2");
		}
		return lblTotalDetalleRecibo2;
	}

	protected HtmlOutputText getLblSubtotalCred() {
		if (lblSubtotalCred == null) {
			lblSubtotalCred = (HtmlOutputText) findComponentInRoot("lblSubtotalCred");
		}
		return lblSubtotalCred;
	}

	protected HtmlOutputText getLblSubtotalCred2() {
		if (lblSubtotalCred2 == null) {
			lblSubtotalCred2 = (HtmlOutputText) findComponentInRoot("lblSubtotalCred2");
		}
		return lblSubtotalCred2;
	}

	protected HtmlOutputText getLblIvaCred() {
		if (lblIvaCred == null) {
			lblIvaCred = (HtmlOutputText) findComponentInRoot("lblIvaCred");
		}
		return lblIvaCred;
	}

	protected HtmlOutputText getLblIvaCred2() {
		if (lblIvaCred2 == null) {
			lblIvaCred2 = (HtmlOutputText) findComponentInRoot("lblIvaCred2");
		}
		return lblIvaCred2;
	}

	protected HtmlLink getLnkAgregarFactura() {
		if (lnkAgregarFactura == null) {
			lnkAgregarFactura = (HtmlLink) findComponentInRoot("lnkAgregarFactura");
		}
		return lnkAgregarFactura;
	}

	protected HtmlOutputText getLblConcepto() {
		if (lblConcepto == null) {
			lblConcepto = (HtmlOutputText) findComponentInRoot("lblConcepto");
		}
		return lblConcepto;
	}

	protected HtmlInputTextarea getTxtConcepto() {
		if (txtConcepto == null) {
			txtConcepto = (HtmlInputTextarea) findComponentInRoot("txtConcepto");
		}
		return txtConcepto;
	}

	protected HtmlOutputText getLblMontoAplicar() {
		if (lblMontoAplicar == null) {
			lblMontoAplicar = (HtmlOutputText) findComponentInRoot("lblMontoAplicar");
		}
		return lblMontoAplicar;
	}

	protected HtmlOutputText getLblMontoAplicar2() {
		if (lblMontoAplicar2 == null) {
			lblMontoAplicar2 = (HtmlOutputText) findComponentInRoot("lblMontoAplicar2");
		}
		return lblMontoAplicar2;
	}

	protected HtmlOutputText getLblMontoRecibido() {
		if (lblMontoRecibido == null) {
			lblMontoRecibido = (HtmlOutputText) findComponentInRoot("lblMontoRecibido");
		}
		return lblMontoRecibido;
	}

	protected HtmlOutputText getLblMontoRecibido2() {
		if (lblMontoRecibido2 == null) {
			lblMontoRecibido2 = (HtmlOutputText) findComponentInRoot("lblMontoRecibido2");
		}
		return lblMontoRecibido2;
	}

	protected HtmlOutputText getLblCambio() {
		if (lblCambio == null) {
			lblCambio = (HtmlOutputText) findComponentInRoot("lblCambio");
		}
		return lblCambio;
	}

	protected HtmlPanelGrid getGrCambio() {
		if (grCambio == null) {
			grCambio = (HtmlPanelGrid) findComponentInRoot("grCambio");
		}
		return grCambio;
	}

	protected HtmlInputText getTxtCambioForaneo() {
		if (txtCambioForaneo == null) {
			txtCambioForaneo = (HtmlInputText) findComponentInRoot("txtCambioForaneo");
		}
		return txtCambioForaneo;
	}

	protected HtmlOutputText getLblPendienteDom() {
		if (lblPendienteDom == null) {
			lblPendienteDom = (HtmlOutputText) findComponentInRoot("lblPendienteDom");
		}
		return lblPendienteDom;
	}

	protected HtmlOutputText getTxtPendienteDom() {
		if (txtPendienteDom == null) {
			txtPendienteDom = (HtmlOutputText) findComponentInRoot("txtPendienteDom");
		}
		return txtPendienteDom;
	}

	protected HtmlOutputText getLblCambioDomestico() {
		if (lblCambioDomestico == null) {
			lblCambioDomestico = (HtmlOutputText) findComponentInRoot("lblCambioDomestico");
		}
		return lblCambioDomestico;
	}

	protected HtmlOutputText getTxtCambioDomestico() {
		if (txtCambioDomestico == null) {
			txtCambioDomestico = (HtmlOutputText) findComponentInRoot("txtCambioDomestico");
		}
		return txtCambioDomestico;
	}

	protected HtmlLink getLnkProcesarRecibo() {
		if (lnkProcesarRecibo == null) {
			lnkProcesarRecibo = (HtmlLink) findComponentInRoot("lnkProcesarRecibo");
		}
		return lnkProcesarRecibo;
	}

	protected HtmlDialogWindowHeader getHdFijarTasaCambio() {
		if (hdFijarTasaCambio == null) {
			hdFijarTasaCambio = (HtmlDialogWindowHeader) findComponentInRoot("hdFijarTasaCambio");
		}
		return hdFijarTasaCambio;
	}

	protected HtmlJspPanel getJsPnlFijarTasaCambio() {
		if (jsPnlFijarTasaCambio == null) {
			jsPnlFijarTasaCambio = (HtmlJspPanel) findComponentInRoot("jsPnlFijarTasaCambio");
		}
		return jsPnlFijarTasaCambio;
	}

	protected HtmlOutputText getLblEtNuevaTasa() {
		if (lblEtNuevaTasa == null) {
			lblEtNuevaTasa = (HtmlOutputText) findComponentInRoot("lblEtNuevaTasa");
		}
		return lblEtNuevaTasa;
	}

	protected HtmlOutputText getLblEtMotivoCambio() {
		if (lblEtMotivoCambio == null) {
			lblEtMotivoCambio = (HtmlOutputText) findComponentInRoot("lblEtMotivoCambio");
		}
		return lblEtMotivoCambio;
	}

	protected HtmlInputTextarea getTxtMotivoCambioTasa() {
		if (txtMotivoCambioTasa == null) {
			txtMotivoCambioTasa = (HtmlInputTextarea) findComponentInRoot("txtMotivoCambioTasa");
		}
		return txtMotivoCambioTasa;
	}

	protected HtmlOutputText getLblMsgErrorCambioTasa() {
		if (lblMsgErrorCambioTasa == null) {
			lblMsgErrorCambioTasa = (HtmlOutputText) findComponentInRoot("lblMsgErrorCambioTasa");
		}
		return lblMsgErrorCambioTasa;
	}

	protected HtmlLink getLnkFijarTasa() {
		if (lnkFijarTasa == null) {
			lnkFijarTasa = (HtmlLink) findComponentInRoot("lnkFijarTasa");
		}
		return lnkFijarTasa;
	}

	protected HtmlOutputText getLblTituloContado80() {
		if (lblTituloContado80 == null) {
			lblTituloContado80 = (HtmlOutputText) findComponentInRoot("lblTituloContado80");
		}
		return lblTituloContado80;
	}

	protected HtmlOutputText getText5() {
		if (text5 == null) {
			text5 = (HtmlOutputText) findComponentInRoot("text5");
		}
		return text5;
	}

	protected HtmlOutputText getText4() {
		if (text4 == null) {
			text4 = (HtmlOutputText) findComponentInRoot("text4");
		}
		return text4;
	}

	protected HtmlJspPanel getJspPanel100() {
		if (jspPanel100 == null) {
			jspPanel100 = (HtmlJspPanel) findComponentInRoot("jspPanel100");
		}
		return jspPanel100;
	}

	protected HtmlColumn getCoDetalleCredito() {
		if (coDetalleCredito == null) {
			coDetalleCredito = (HtmlColumn) findComponentInRoot("coDetalleCredito");
		}
		return coDetalleCredito;
	}

	protected HtmlColumn getCoNofacturaCredito() {
		if (coNofacturaCredito == null) {
			coNofacturaCredito = (HtmlColumn) findComponentInRoot("coNofacturaCredito");
		}
		return coNofacturaCredito;
	}

	protected HtmlColumn getCoTipoFacturaCred() {
		if (coTipoFacturaCred == null) {
			coTipoFacturaCred = (HtmlColumn) findComponentInRoot("coTipoFacturaCred");
		}
		return coTipoFacturaCred;
	}

	protected HtmlColumn getCoPartida() {
		if (coPartida == null) {
			coPartida = (HtmlColumn) findComponentInRoot("coPartida");
		}
		return coPartida;
	}

	protected HtmlColumn getCoNomcliCredito() {
		if (coNomcliCredito == null) {
			coNomcliCredito = (HtmlColumn) findComponentInRoot("coNomcliCredito");
		}
		return coNomcliCredito;
	}

	protected HtmlColumn getCoUninegCredito() {
		if (coUninegCredito == null) {
			coUninegCredito = (HtmlColumn) findComponentInRoot("coUninegCredito");
		}
		return coUninegCredito;
	}

	protected HtmlColumn getCoTotalCredito() {
		if (coTotalCredito == null) {
			coTotalCredito = (HtmlColumn) findComponentInRoot("coTotalCredito");
		}
		return coTotalCredito;
	}

	protected HtmlColumn getCoMonedaCredito() {
		if (coMonedaCredito == null) {
			coMonedaCredito = (HtmlColumn) findComponentInRoot("coMonedaCredito");
		}
		return coMonedaCredito;
	}

	protected HtmlColumn getCoFechaFacturaCredito() {
		if (coFechaFacturaCredito == null) {
			coFechaFacturaCredito = (HtmlColumn) findComponentInRoot("coFechaFacturaCredito");
		}
		return coFechaFacturaCredito;
	}

	protected HtmlColumn getCoFechaFacturaVecto() {
		if (coFechaFacturaVecto == null) {
			coFechaFacturaVecto = (HtmlColumn) findComponentInRoot("coFechaFacturaVecto");
		}
		return coFechaFacturaVecto;
	}

	protected HtmlColumn getCoMetodo() {
		if (coMetodo == null) {
			coMetodo = (HtmlColumn) findComponentInRoot("coMetodo");
		}
		return coMetodo;
	}

	protected HtmlColumn getCoMonedaDetalle() {
		if (coMonedaDetalle == null) {
			coMonedaDetalle = (HtmlColumn) findComponentInRoot("coMonedaDetalle");
		}
		return coMonedaDetalle;
	}

	protected HtmlColumn getCoMonto() {
		if (coMonto == null) {
			coMonto = (HtmlColumn) findComponentInRoot("coMonto");
		}
		return coMonto;
	}

	protected HtmlColumn getCoTasa() {
		if (coTasa == null) {
			coTasa = (HtmlColumn) findComponentInRoot("coTasa");
		}
		return coTasa;
	}

	protected HtmlColumn getCoEquivalente() {
		if (coEquivalente == null) {
			coEquivalente = (HtmlColumn) findComponentInRoot("coEquivalente");
		}
		return coEquivalente;
	}

	protected HtmlColumn getCoReferencia() {
		if (coReferencia == null) {
			coReferencia = (HtmlColumn) findComponentInRoot("coReferencia");
		}
		return coReferencia;
	}

	protected HtmlColumn getCoReferencia2() {
		if (coReferencia2 == null) {
			coReferencia2 = (HtmlColumn) findComponentInRoot("coReferencia2");
		}
		return coReferencia2;
	}

	protected HtmlColumn getCoReferencia3() {
		if (coReferencia3 == null) {
			coReferencia3 = (HtmlColumn) findComponentInRoot("coReferencia3");
		}
		return coReferencia3;
	}

	protected HtmlColumn getCoReferencia4() {
		if (coReferencia4 == null) {
			coReferencia4 = (HtmlColumn) findComponentInRoot("coReferencia4");
		}
		return coReferencia4;
	}

	protected HtmlGridEditing getGdeId3() {
		if (gdeId3 == null) {
			gdeId3 = (HtmlGridEditing) findComponentInRoot("gdeId3");
		}
		return gdeId3;
	}

	protected HtmlOutputText getLblTasaCambio2() {
		if (lblTasaCambio2 == null) {
			lblTasaCambio2 = (HtmlOutputText) findComponentInRoot("lblTasaCambio2");
		}
		return lblTasaCambio2;
	}

	protected HtmlOutputText getLblTasaJDE2() {
		if (lblTasaJDE2 == null) {
			lblTasaJDE2 = (HtmlOutputText) findComponentInRoot("lblTasaJDE2");
		}
		return lblTasaJDE2;
	}

	protected HtmlColumn getCoDetalleFacturaRecibo() {
		if (coDetalleFacturaRecibo == null) {
			coDetalleFacturaRecibo = (HtmlColumn) findComponentInRoot("coDetalleFacturaRecibo");
		}
		return coDetalleFacturaRecibo;
	}

	protected HtmlColumn getCoNofactura() {
		if (coNofactura == null) {
			coNofactura = (HtmlColumn) findComponentInRoot("coNofactura");
		}
		return coNofactura;
	}

	protected HtmlColumn getCoPartidaDetalleRecibo() {
		if (coPartidaDetalleRecibo == null) {
			coPartidaDetalleRecibo = (HtmlColumn) findComponentInRoot("coPartidaDetalleRecibo");
		}
		return coPartidaDetalleRecibo;
	}

	protected HtmlColumn getCoTotalDetalleReciboC() {
		if (coTotalDetalleReciboC == null) {
			coTotalDetalleReciboC = (HtmlColumn) findComponentInRoot("coTotalDetalleReciboC");
		}
		return coTotalDetalleReciboC;
	}

	protected HtmlColumn getCoFechaDetalleRecibo() {
		if (coFechaDetalleRecibo == null) {
			coFechaDetalleRecibo = (HtmlColumn) findComponentInRoot("coFechaDetalleRecibo");
		}
		return coFechaDetalleRecibo;
	}

	protected HtmlColumn getCoTotalDetalleRecibo() {
		if (coTotalDetalleRecibo == null) {
			coTotalDetalleRecibo = (HtmlColumn) findComponentInRoot("coTotalDetalleRecibo");
		}
		return coTotalDetalleRecibo;
	}

	protected HtmlColumn getCoSubtotalCred() {
		if (coSubtotalCred == null) {
			coSubtotalCred = (HtmlColumn) findComponentInRoot("coSubtotalCred");
		}
		return coSubtotalCred;
	}

	protected HtmlColumn getCoIvaCred() {
		if (coIvaCred == null) {
			coIvaCred = (HtmlColumn) findComponentInRoot("coIvaCred");
		}
		return coIvaCred;
	}

	protected HtmlGridEditing getGeActDetalle() {
		if (geActDetalle == null) {
			geActDetalle = (HtmlGridEditing) findComponentInRoot("geActDetalle");
		}
		return geActDetalle;
	}

	protected HtmlOutputText getLblTotalSeleccionado1() {
		if (lblTotalSeleccionado1 == null) {
			lblTotalSeleccionado1 = (HtmlOutputText) findComponentInRoot("lblTotalSeleccionado1");
		}
		return lblTotalSeleccionado1;
	}

	protected HtmlOutputText getLblTotalSeleccionadoDomestico() {
		if (lblTotalSeleccionadoDomestico == null) {
			lblTotalSeleccionadoDomestico = (HtmlOutputText) findComponentInRoot("lblTotalSeleccionadoDomestico");
		}
		return lblTotalSeleccionadoDomestico;
	}

	protected HtmlOutputText getLblTotalSeleccionadoForaneo() {
		if (lblTotalSeleccionadoForaneo == null) {
			lblTotalSeleccionadoForaneo = (HtmlOutputText) findComponentInRoot("lblTotalSeleccionadoForaneo");
		}
		return lblTotalSeleccionadoForaneo;
	}

	protected HtmlOutputText getLblTotalFaltante1() {
		if (lblTotalFaltante1 == null) {
			lblTotalFaltante1 = (HtmlOutputText) findComponentInRoot("lblTotalFaltante1");
		}
		return lblTotalFaltante1;
	}

	protected HtmlOutputText getLblTotalFaltanteDomestico() {
		if (lblTotalFaltanteDomestico == null) {
			lblTotalFaltanteDomestico = (HtmlOutputText) findComponentInRoot("lblTotalFaltanteDomestico");
		}
		return lblTotalFaltanteDomestico;
	}

	protected HtmlOutputText getLblTotalFaltanteForaneo() {
		if (lblTotalFaltanteForaneo == null) {
			lblTotalFaltanteForaneo = (HtmlOutputText) findComponentInRoot("lblTotalFaltanteForaneo");
		}
		return lblTotalFaltanteForaneo;
	}

	protected HtmlOutputText getLblSeleccionadosDet1() {
		if (lblSeleccionadosDet1 == null) {
			lblSeleccionadosDet1 = (HtmlOutputText) findComponentInRoot("lblSeleccionadosDet1");
		}
		return lblSeleccionadosDet1;
	}

	protected HtmlOutputText getLblSeleccionadosDet() {
		if (lblSeleccionadosDet == null) {
			lblSeleccionadosDet = (HtmlOutputText) findComponentInRoot("lblSeleccionadosDet");
		}
		return lblSeleccionadosDet;
	}

	protected HtmlLink getLnkCambio() {
		if (lnkCambio == null) {
			lnkCambio = (HtmlLink) findComponentInRoot("lnkCambio");
		}
		return lnkCambio;
	}

	protected HtmlOutputText getTxtCambio() {
		if (txtCambio == null) {
			txtCambio = (HtmlOutputText) findComponentInRoot("txtCambio");
		}
		return txtCambio;
	}

	protected HtmlLink getLnkCancelarRecibo() {
		if (lnkCancelarRecibo == null) {
			lnkCancelarRecibo = (HtmlLink) findComponentInRoot("lnkCancelarRecibo");
		}
		return lnkCancelarRecibo;
	}

	protected HtmlDialogWindowAutoPostBackFlags getApbReciboCredito100() {
		if (apbReciboCredito100 == null) {
			apbReciboCredito100 = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbReciboCredito100");
		}
		return apbReciboCredito100;
	}

	protected HtmlDialogWindow getDwFijarTasaCambio() {
		if (dwFijarTasaCambio == null) {
			dwFijarTasaCambio = (HtmlDialogWindow) findComponentInRoot("dwFijarTasaCambio");
		}
		return dwFijarTasaCambio;
	}

	protected HtmlDialogWindowClientEvents getCleFijarTasaCambio() {
		if (cleFijarTasaCambio == null) {
			cleFijarTasaCambio = (HtmlDialogWindowClientEvents) findComponentInRoot("cleFijarTasaCambio");
		}
		return cleFijarTasaCambio;
	}

	protected HtmlDialogWindowRoundedCorners getRcFijarTasaCambio() {
		if (rcFijarTasaCambio == null) {
			rcFijarTasaCambio = (HtmlDialogWindowRoundedCorners) findComponentInRoot("rcFijarTasaCambio");
		}
		return rcFijarTasaCambio;
	}

	protected HtmlDialogWindowContentPane getCpFijarTasaCambio() {
		if (cpFijarTasaCambio == null) {
			cpFijarTasaCambio = (HtmlDialogWindowContentPane) findComponentInRoot("cpFijarTasaCambio");
		}
		return cpFijarTasaCambio;
	}

	protected HtmlInputText getTxtNuevaTasaCambio() {
		if (txtNuevaTasaCambio == null) {
			txtNuevaTasaCambio = (HtmlInputText) findComponentInRoot("txtNuevaTasaCambio");
		}
		return txtNuevaTasaCambio;
	}

	protected HtmlJspPanel getJsPnlFijarTasaCambio2() {
		if (jsPnlFijarTasaCambio2 == null) {
			jsPnlFijarTasaCambio2 = (HtmlJspPanel) findComponentInRoot("jsPnlFijarTasaCambio2");
		}
		return jsPnlFijarTasaCambio2;
	}

	protected HtmlLink getLnkCancelarFijarTasa() {
		if (lnkCancelarFijarTasa == null) {
			lnkCancelarFijarTasa = (HtmlLink) findComponentInRoot("lnkCancelarFijarTasa");
		}
		return lnkCancelarFijarTasa;
	}

	protected HtmlOutputText getTxtSeleccionados() {
		if (txtSeleccionados == null) {
			txtSeleccionados = (HtmlOutputText) findComponentInRoot("txtSeleccionados");
		}
		return txtSeleccionados;
	}

	protected HtmlLink getLnkProcesarRecibo2() {
		if (lnkProcesarRecibo2 == null) {
			lnkProcesarRecibo2 = (HtmlLink) findComponentInRoot("lnkProcesarRecibo2");
		}
		return lnkProcesarRecibo2;
	}

	protected HtmlPanelGrid getGrdFiltrosBusqueda() {
		if (grdFiltrosBusqueda == null) {
			grdFiltrosBusqueda = (HtmlPanelGrid) findComponentInRoot("grdFiltrosBusqueda");
		}
		return grdFiltrosBusqueda;
	}

	protected HtmlDropDownListClientEvents getCleddlComapaniaCre() {
		if (cleddlComapaniaCre == null) {
			cleddlComapaniaCre = (HtmlDropDownListClientEvents) findComponentInRoot("cleddlComapaniaCre");
		}
		return cleddlComapaniaCre;
	}

	protected HtmlDropDownListClientEvents getCleddlUninegCre() {
		if (cleddlUninegCre == null) {
			cleddlUninegCre = (HtmlDropDownListClientEvents) findComponentInRoot("cleddlUninegCre");
		}
		return cleddlUninegCre;
	}

	protected HtmlDropDownList getDdlComapaniaCre() {
		if (ddlComapaniaCre == null) {
			ddlComapaniaCre = (HtmlDropDownList) findComponentInRoot("ddlComapaniaCre");
		}
		return ddlComapaniaCre;
	}

	protected HtmlDropDownList getDdlSucursalCre() {
		if (ddlSucursalCre == null) {
			ddlSucursalCre = (HtmlDropDownList) findComponentInRoot("ddlSucursalCre");
		}
		return ddlSucursalCre;
	}

	protected HtmlDropDownList getDdlUninegCre() {
		if (ddlUninegCre == null) {
			ddlUninegCre = (HtmlDropDownList) findComponentInRoot("ddlUninegCre");
		}
		return ddlUninegCre;
	}

	protected HtmlDialogWindowHeader getHdAgregarFactura() {
		if (hdAgregarFactura == null) {
			hdAgregarFactura = (HtmlDialogWindowHeader) findComponentInRoot("hdAgregarFactura");
		}
		return hdAgregarFactura;
	}

	protected HtmlJspPanel getJspAgregarFactura() {
		if (jspAgregarFactura == null) {
			jspAgregarFactura = (HtmlJspPanel) findComponentInRoot("jspAgregarFactura");
		}
		return jspAgregarFactura;
	}

	protected HtmlOutputText getLblRangosNoF2() {
		if (lblRangosNoF2 == null) {
			lblRangosNoF2 = (HtmlOutputText) findComponentInRoot("lblRangosNoF2");
		}
		return lblRangosNoF2;
	}

	protected HtmlGridView getGvAgregarFactura() {
		if (gvAgregarFactura == null) {
			gvAgregarFactura = (HtmlGridView) findComponentInRoot("gvAgregarFactura");
		}
		return gvAgregarFactura;
	}

	protected HtmlColumnSelectRow getColumnSelectRowRendererCredAgregar() {
		if (columnSelectRowRendererCredAgregar == null) {
			columnSelectRowRendererCredAgregar = (HtmlColumnSelectRow) findComponentInRoot("columnSelectRowRendererCredAgregar");
		}
		return columnSelectRowRendererCredAgregar;
	}

	protected HtmlOutputText getLblNofacturaAgregarFactura() {
		if (lblNofacturaAgregarFactura == null) {
			lblNofacturaAgregarFactura = (HtmlOutputText) findComponentInRoot("lblNofacturaAgregarFactura");
		}
		return lblNofacturaAgregarFactura;
	}

	protected HtmlOutputText getLblNofacturaAgregarFactura2() {
		if (lblNofacturaAgregarFactura2 == null) {
			lblNofacturaAgregarFactura2 = (HtmlOutputText) findComponentInRoot("lblNofacturaAgregarFactura2");
		}
		return lblNofacturaAgregarFactura2;
	}

	protected HtmlOutputText getLblTipofactura1AgregarFactura() {
		if (lblTipofactura1AgregarFactura == null) {
			lblTipofactura1AgregarFactura = (HtmlOutputText) findComponentInRoot("lblTipofactura1AgregarFactura");
		}
		return lblTipofactura1AgregarFactura;
	}

	protected HtmlOutputText getLblTipofactura2AgregarFactura() {
		if (lblTipofactura2AgregarFactura == null) {
			lblTipofactura2AgregarFactura = (HtmlOutputText) findComponentInRoot("lblTipofactura2AgregarFactura");
		}
		return lblTipofactura2AgregarFactura;
	}

	protected HtmlOutputText getLblPartida1AgregarFactura() {
		if (lblPartida1AgregarFactura == null) {
			lblPartida1AgregarFactura = (HtmlOutputText) findComponentInRoot("lblPartida1AgregarFactura");
		}
		return lblPartida1AgregarFactura;
	}

	protected HtmlOutputText getLblPartida2AgregarFactura() {
		if (lblPartida2AgregarFactura == null) {
			lblPartida2AgregarFactura = (HtmlOutputText) findComponentInRoot("lblPartida2AgregarFactura");
		}
		return lblPartida2AgregarFactura;
	}

	protected HtmlOutputText getLblUniNeg3AgregarFactura() {
		if (lblUniNeg3AgregarFactura == null) {
			lblUniNeg3AgregarFactura = (HtmlOutputText) findComponentInRoot("lblUniNeg3AgregarFactura");
		}
		return lblUniNeg3AgregarFactura;
	}

	protected HtmlOutputText getLblUninegCreditoAdd() {
		if (lblUninegCreditoAdd == null) {
			lblUninegCreditoAdd = (HtmlOutputText) findComponentInRoot("lblUninegCreditoAdd");
		}
		return lblUninegCreditoAdd;
	}

	protected HtmlOutputText getLblUninegCredito2Add() {
		if (lblUninegCredito2Add == null) {
			lblUninegCredito2Add = (HtmlOutputText) findComponentInRoot("lblUninegCredito2Add");
		}
		return lblUninegCredito2Add;
	}

	protected HtmlOutputText getLblTotalAgregarFactura() {
		if (lblTotalAgregarFactura == null) {
			lblTotalAgregarFactura = (HtmlOutputText) findComponentInRoot("lblTotalAgregarFactura");
		}
		return lblTotalAgregarFactura;
	}

	protected HtmlOutputText getLblTotalAgregarFactura2() {
		if (lblTotalAgregarFactura2 == null) {
			lblTotalAgregarFactura2 = (HtmlOutputText) findComponentInRoot("lblTotalAgregarFactura2");
		}
		return lblTotalAgregarFactura2;
	}

	protected HtmlOutputText getLblFechaAgregarFactura() {
		if (lblFechaAgregarFactura == null) {
			lblFechaAgregarFactura = (HtmlOutputText) findComponentInRoot("lblFechaAgregarFactura");
		}
		return lblFechaAgregarFactura;
	}

	protected HtmlOutputText getLblFechaAgregarFactura2() {
		if (lblFechaAgregarFactura2 == null) {
			lblFechaAgregarFactura2 = (HtmlOutputText) findComponentInRoot("lblFechaAgregarFactura2");
		}
		return lblFechaAgregarFactura2;
	}

	protected HtmlOutputText getLblFechaFacturaVectoAgregarFactura() {
		if (lblFechaFacturaVectoAgregarFactura == null) {
			lblFechaFacturaVectoAgregarFactura = (HtmlOutputText) findComponentInRoot("lblFechaFacturaVectoAgregarFactura");
		}
		return lblFechaFacturaVectoAgregarFactura;
	}

	protected HtmlOutputText getLblFechaFacturaVectoAgregarFactura2() {
		if (lblFechaFacturaVectoAgregarFactura2 == null) {
			lblFechaFacturaVectoAgregarFactura2 = (HtmlOutputText) findComponentInRoot("lblFechaFacturaVectoAgregarFactura2");
		}
		return lblFechaFacturaVectoAgregarFactura2;
	}

	protected HtmlLink getLnkDetalleAgregarFactura() {
		if (lnkDetalleAgregarFactura == null) {
			lnkDetalleAgregarFactura = (HtmlLink) findComponentInRoot("lnkDetalleAgregarFactura");
		}
		return lnkDetalleAgregarFactura;
	}

	protected HtmlDialogWindow getDwAgregarFactura() {
		if (dwAgregarFactura == null) {
			dwAgregarFactura = (HtmlDialogWindow) findComponentInRoot("dwAgregarFactura");
		}
		return dwAgregarFactura;
	}

	protected HtmlDialogWindowClientEvents getCleAgregarFactura() {
		if (cleAgregarFactura == null) {
			cleAgregarFactura = (HtmlDialogWindowClientEvents) findComponentInRoot("cleAgregarFactura");
		}
		return cleAgregarFactura;
	}

	protected HtmlDialogWindowRoundedCorners getRcAgregarFactura() {
		if (rcAgregarFactura == null) {
			rcAgregarFactura = (HtmlDialogWindowRoundedCorners) findComponentInRoot("rcAgregarFactura");
		}
		return rcAgregarFactura;
	}

	protected HtmlDialogWindowContentPane getCpAgregarFactura() {
		if (cpAgregarFactura == null) {
			cpAgregarFactura = (HtmlDialogWindowContentPane) findComponentInRoot("cpAgregarFactura");
		}
		return cpAgregarFactura;
	}

	protected HtmlInputText getTxtNofacturaDesde() {
		if (txtNofacturaDesde == null) {
			txtNofacturaDesde = (HtmlInputText) findComponentInRoot("txtNofacturaDesde");
		}
		return txtNofacturaDesde;
	}

	protected HtmlInputText getTxtNofacturaHasta() {
		if (txtNofacturaHasta == null) {
			txtNofacturaHasta = (HtmlInputText) findComponentInRoot("txtNofacturaHasta");
		}
		return txtNofacturaHasta;
	}

	protected HtmlLink getLnkBuscarAgregarFatura() {
		if (lnkBuscarAgregarFatura == null) {
			lnkBuscarAgregarFatura = (HtmlLink) findComponentInRoot("lnkBuscarAgregarFatura");
		}
		return lnkBuscarAgregarFatura;
	}

	protected HtmlColumn getCoNofacturaAgregarFactura() {
		if (coNofacturaAgregarFactura == null) {
			coNofacturaAgregarFactura = (HtmlColumn) findComponentInRoot("coNofacturaAgregarFactura");
		}
		return coNofacturaAgregarFactura;
	}

	protected HtmlColumn getCoTipoFacturaAgregarFactura() {
		if (coTipoFacturaAgregarFactura == null) {
			coTipoFacturaAgregarFactura = (HtmlColumn) findComponentInRoot("coTipoFacturaAgregarFactura");
		}
		return coTipoFacturaAgregarFactura;
	}

	protected HtmlColumn getCoPartidaAgregarFactura() {
		if (coPartidaAgregarFactura == null) {
			coPartidaAgregarFactura = (HtmlColumn) findComponentInRoot("coPartidaAgregarFactura");
		}
		return coPartidaAgregarFactura;
	}

	protected HtmlColumn getCoUninegCreditoAdd() {
		if (coUninegCreditoAdd == null) {
			coUninegCreditoAdd = (HtmlColumn) findComponentInRoot("coUninegCreditoAdd");
		}
		return coUninegCreditoAdd;
	}

	protected HtmlColumn getCoTotalAgregarFactura() {
		if (coTotalAgregarFactura == null) {
			coTotalAgregarFactura = (HtmlColumn) findComponentInRoot("coTotalAgregarFactura");
		}
		return coTotalAgregarFactura;
	}

	protected HtmlColumn getCoFechaAgregarFactura() {
		if (coFechaAgregarFactura == null) {
			coFechaAgregarFactura = (HtmlColumn) findComponentInRoot("coFechaAgregarFactura");
		}
		return coFechaAgregarFactura;
	}

	protected HtmlColumn getCoFechaFacturaVectoAgregarFactura() {
		if (coFechaFacturaVectoAgregarFactura == null) {
			coFechaFacturaVectoAgregarFactura = (HtmlColumn) findComponentInRoot("coFechaFacturaVectoAgregarFactura");
		}
		return coFechaFacturaVectoAgregarFactura;
	}

	protected HtmlLink getLnkAceptarAgregarFatura() {
		if (lnkAceptarAgregarFatura == null) {
			lnkAceptarAgregarFatura = (HtmlLink) findComponentInRoot("lnkAceptarAgregarFatura");
		}
		return lnkAceptarAgregarFatura;
	}

	protected HtmlDialogWindowAutoPostBackFlags getApbAgregarFactura() {
		if (apbAgregarFactura == null) {
			apbAgregarFactura = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbAgregarFactura");
		}
		return apbAgregarFactura;
	}

	protected HtmlDialogWindowHeader getHdValida() {
		if (hdValida == null) {
			hdValida = (HtmlDialogWindowHeader) findComponentInRoot("hdValida");
		}
		return hdValida;
	}

	protected HtmlJspPanel getJspPane20() {
		if (jspPane20 == null) {
			jspPane20 = (HtmlJspPanel) findComponentInRoot("jspPane20");
		}
		return jspPane20;
	}

	protected HtmlPanelGrid getGrdValida() {
		if (grdValida == null) {
			grdValida = (HtmlPanelGrid) findComponentInRoot("grdValida");
		}
		return grdValida;
	}

	protected HtmlGraphicImageEx getImgValida() {
		if (imgValida == null) {
			imgValida = (HtmlGraphicImageEx) findComponentInRoot("imgValida");
		}
		return imgValida;
	}

	protected HtmlLink getLnkCerrarValida() {
		if (lnkCerrarValida == null) {
			lnkCerrarValida = (HtmlLink) findComponentInRoot("lnkCerrarValida");
		}
		return lnkCerrarValida;
	}

	protected HtmlDialogWindow getDwValidaContado() {
		if (dwValidaContado == null) {
			dwValidaContado = (HtmlDialogWindow) findComponentInRoot("dwValidaContado");
		}
		return dwValidaContado;
	}

	protected HtmlDialogWindowClientEvents getCleValida() {
		if (cleValida == null) {
			cleValida = (HtmlDialogWindowClientEvents) findComponentInRoot("cleValida");
		}
		return cleValida;
	}

	protected HtmlDialogWindowRoundedCorners getRcValida() {
		if (rcValida == null) {
			rcValida = (HtmlDialogWindowRoundedCorners) findComponentInRoot("rcValida");
		}
		return rcValida;
	}

	protected HtmlDialogWindowContentPane getCpValida() {
		if (cpValida == null) {
			cpValida = (HtmlDialogWindowContentPane) findComponentInRoot("cpValida");
		}
		return cpValida;
	}

	protected HtmlOutputText getLblValida() {
		if (lblValida == null) {
			lblValida = (HtmlOutputText) findComponentInRoot("lblValida");
		}
		return lblValida;
	}

	protected HtmlDialogWindowAutoPostBackFlags getApbReciboContado2() {
		if (apbReciboContado2 == null) {
			apbReciboContado2 = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbReciboContado2");
		}
		return apbReciboContado2;
	}

	protected HtmlDialogWindowHeader getHdProcesaRecibo() {
		if (hdProcesaRecibo == null) {
			hdProcesaRecibo = (HtmlDialogWindowHeader) findComponentInRoot("hdProcesaRecibo");
		}
		return hdProcesaRecibo;
	}

	protected HtmlJspPanel getJspProcesa() {
		if (jspProcesa == null) {
			jspProcesa = (HtmlJspPanel) findComponentInRoot("jspProcesa");
		}
		return jspProcesa;
	}

	protected HtmlPanelGrid getGrdProces() {
		if (grdProces == null) {
			grdProces = (HtmlPanelGrid) findComponentInRoot("grdProces");
		}
		return grdProces;
	}

	protected HtmlGraphicImageEx getImgProcesa() {
		if (imgProcesa == null) {
			imgProcesa = (HtmlGraphicImageEx) findComponentInRoot("imgProcesa");
		}
		return imgProcesa;
	}

	protected HtmlLink getLnkCerrarPagoMensaje() {
		if (lnkCerrarPagoMensaje == null) {
			lnkCerrarPagoMensaje = (HtmlLink) findComponentInRoot("lnkCerrarPagoMensaje");
		}
		return lnkCerrarPagoMensaje;
	}

	protected HtmlDialogWindow getDwProcesa() {
		if (dwProcesa == null) {
			dwProcesa = (HtmlDialogWindow) findComponentInRoot("dwProcesa");
		}
		return dwProcesa;
	}

	protected HtmlDialogWindowClientEvents getCleProcesaRecibo() {
		if (cleProcesaRecibo == null) {
			cleProcesaRecibo = (HtmlDialogWindowClientEvents) findComponentInRoot("cleProcesaRecibo");
		}
		return cleProcesaRecibo;
	}

	protected HtmlDialogWindowRoundedCorners getRcProcesaRecibo() {
		if (rcProcesaRecibo == null) {
			rcProcesaRecibo = (HtmlDialogWindowRoundedCorners) findComponentInRoot("rcProcesaRecibo");
		}
		return rcProcesaRecibo;
	}

	protected HtmlDialogWindowContentPane getCpProcesaRecibo() {
		if (cpProcesaRecibo == null) {
			cpProcesaRecibo = (HtmlDialogWindowContentPane) findComponentInRoot("cpProcesaRecibo");
		}
		return cpProcesaRecibo;
	}

	protected HtmlOutputText getLblMensajeValidacionPrima() {
		if (lblMensajeValidacionPrima == null) {
			lblMensajeValidacionPrima = (HtmlOutputText) findComponentInRoot("lblMensajeValidacionPrima");
		}
		return lblMensajeValidacionPrima;
	}

	protected HtmlDialogWindowAutoPostBackFlags getApbProcesaRecibo() {
		if (apbProcesaRecibo == null) {
			apbProcesaRecibo = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbProcesaRecibo");
		}
		return apbProcesaRecibo;
	}

	protected HtmlDialogWindowHeader getHdImprime() {
		if (hdImprime == null) {
			hdImprime = (HtmlDialogWindowHeader) findComponentInRoot("hdImprime");
		}
		return hdImprime;
	}

	protected HtmlJspPanel getJspPanel3() {
		if (jspPanel3 == null) {
			jspPanel3 = (HtmlJspPanel) findComponentInRoot("jspPanel3");
		}
		return jspPanel3;
	}

	protected HtmlPanelGrid getGrid100() {
		if (grid100 == null) {
			grid100 = (HtmlPanelGrid) findComponentInRoot("grid100");
		}
		return grid100;
	}

	protected HtmlGraphicImageEx getImageEx2Imprime() {
		if (imageEx2Imprime == null) {
			imageEx2Imprime = (HtmlGraphicImageEx) findComponentInRoot("imageEx2Imprime");
		}
		return imageEx2Imprime;
	}

	protected HtmlDialogWindow getDwImprime() {
		if (dwImprime == null) {
			dwImprime = (HtmlDialogWindow) findComponentInRoot("dwImprime");
		}
		return dwImprime;
	}

	protected HtmlDialogWindowClientEvents getCleImprime() {
		if (cleImprime == null) {
			cleImprime = (HtmlDialogWindowClientEvents) findComponentInRoot("cleImprime");
		}
		return cleImprime;
	}

	protected HtmlDialogWindowRoundedCorners getRcImprime() {
		if (rcImprime == null) {
			rcImprime = (HtmlDialogWindowRoundedCorners) findComponentInRoot("rcImprime");
		}
		return rcImprime;
	}

	protected HtmlDialogWindowContentPane getCpImprime() {
		if (cpImprime == null) {
			cpImprime = (HtmlDialogWindowContentPane) findComponentInRoot("cpImprime");
		}
		return cpImprime;
	}

	protected HtmlOutputText getLblConfirmPrint() {
		if (lblConfirmPrint == null) {
			lblConfirmPrint = (HtmlOutputText) findComponentInRoot("lblConfirmPrint");
		}
		return lblConfirmPrint;
	}

	protected HtmlLink getLnkCerrarMensaje13() {
		if (lnkCerrarMensaje13 == null) {
			lnkCerrarMensaje13 = (HtmlLink) findComponentInRoot("lnkCerrarMensaje13");
		}
		return lnkCerrarMensaje13;
	}

	protected HtmlLink getLnkCerrarMensaje14() {
		if (lnkCerrarMensaje14 == null) {
			lnkCerrarMensaje14 = (HtmlLink) findComponentInRoot("lnkCerrarMensaje14");
		}
		return lnkCerrarMensaje14;
	}

	protected HtmlDialogWindowAutoPostBackFlags getApbImprime() {
		if (apbImprime == null) {
			apbImprime = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbImprime");
		}
		return apbImprime;
	}

	protected HtmlDialogWindowHeader getHdMensajeDetalleCredito() {
		if (hdMensajeDetalleCredito == null) {
			hdMensajeDetalleCredito = (HtmlDialogWindowHeader) findComponentInRoot("hdMensajeDetalleCredito");
		}
		return hdMensajeDetalleCredito;
	}

	protected HtmlJspPanel getJspPanel5DetalleCredito() {
		if (jspPanel5DetalleCredito == null) {
			jspPanel5DetalleCredito = (HtmlJspPanel) findComponentInRoot("jspPanel5DetalleCredito");
		}
		return jspPanel5DetalleCredito;
	}

	protected HtmlPanelGrid getGrid4DetalleCredito() {
		if (grid4DetalleCredito == null) {
			grid4DetalleCredito = (HtmlPanelGrid) findComponentInRoot("grid4DetalleCredito");
		}
		return grid4DetalleCredito;
	}

	protected HtmlGraphicImageEx getImageEx10DetalleCredito() {
		if (imageEx10DetalleCredito == null) {
			imageEx10DetalleCredito = (HtmlGraphicImageEx) findComponentInRoot("imageEx10DetalleCredito");
		}
		return imageEx10DetalleCredito;
	}

	protected HtmlLink getLnkAcptarDetalleCredito() {
		if (lnkAcptarDetalleCredito == null) {
			lnkAcptarDetalleCredito = (HtmlLink) findComponentInRoot("lnkAcptarDetalleCredito");
		}
		return lnkAcptarDetalleCredito;
	}

	protected HtmlDialogWindow getDwMensajeDetalleCredito() {
		if (dwMensajeDetalleCredito == null) {
			dwMensajeDetalleCredito = (HtmlDialogWindow) findComponentInRoot("dwMensajeDetalleCredito");
		}
		return dwMensajeDetalleCredito;
	}

	protected HtmlDialogWindowClientEvents getCleMensajeDetalleCredito() {
		if (cleMensajeDetalleCredito == null) {
			cleMensajeDetalleCredito = (HtmlDialogWindowClientEvents) findComponentInRoot("cleMensajeDetalleCredito");
		}
		return cleMensajeDetalleCredito;
	}

	protected HtmlDialogWindowRoundedCorners getRcMensajeDetalleCredito() {
		if (rcMensajeDetalleCredito == null) {
			rcMensajeDetalleCredito = (HtmlDialogWindowRoundedCorners) findComponentInRoot("rcMensajeDetalleCredito");
		}
		return rcMensajeDetalleCredito;
	}

	protected HtmlDialogWindowContentPane getCpMensajeDetalleContado() {
		if (cpMensajeDetalleContado == null) {
			cpMensajeDetalleContado = (HtmlDialogWindowContentPane) findComponentInRoot("cpMensajeDetalleContado");
		}
		return cpMensajeDetalleContado;
	}

	protected HtmlOutputText getLblAlertDetalleContado() {
		if (lblAlertDetalleContado == null) {
			lblAlertDetalleContado = (HtmlOutputText) findComponentInRoot("lblAlertDetalleContado");
		}
		return lblAlertDetalleContado;
	}

	protected HtmlDialogWindowAutoPostBackFlags getApbMensajeDetalleCredito() {
		if (apbMensajeDetalleCredito == null) {
			apbMensajeDetalleCredito = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbMensajeDetalleCredito");
		}
		return apbMensajeDetalleCredito;
	}

	protected HtmlDialogWindowHeader getHdAskCancel() {
		if (hdAskCancel == null) {
			hdAskCancel = (HtmlDialogWindowHeader) findComponentInRoot("hdAskCancel");
		}
		return hdAskCancel;
	}

	protected HtmlJspPanel getJspPanel3AskCancel() {
		if (jspPanel3AskCancel == null) {
			jspPanel3AskCancel = (HtmlJspPanel) findComponentInRoot("jspPanel3AskCancel");
		}
		return jspPanel3AskCancel;
	}

	protected HtmlPanelGrid getGridAskCancel() {
		if (gridAskCancel == null) {
			gridAskCancel = (HtmlPanelGrid) findComponentInRoot("gridAskCancel");
		}
		return gridAskCancel;
	}

	protected HtmlGraphicImageEx getImageEx2AskCancel() {
		if (imageEx2AskCancel == null) {
			imageEx2AskCancel = (HtmlGraphicImageEx) findComponentInRoot("imageEx2AskCancel");
		}
		return imageEx2AskCancel;
	}

	protected HtmlLink getLnkCerrarMensajeAskCancel() {
		if (lnkCerrarMensajeAskCancel == null) {
			lnkCerrarMensajeAskCancel = (HtmlLink) findComponentInRoot("lnkCerrarMensajeAskCancel");
		}
		return lnkCerrarMensajeAskCancel;
	}

	protected HtmlDialogWindow getDwAskCancel() {
		if (dwAskCancel == null) {
			dwAskCancel = (HtmlDialogWindow) findComponentInRoot("dwAskCancel");
		}
		return dwAskCancel;
	}

	protected HtmlDialogWindowClientEvents getCleAskCancel() {
		if (cleAskCancel == null) {
			cleAskCancel = (HtmlDialogWindowClientEvents) findComponentInRoot("cleAskCancel");
		}
		return cleAskCancel;
	}

	protected HtmlDialogWindowRoundedCorners getRcAskCancel() {
		if (rcAskCancel == null) {
			rcAskCancel = (HtmlDialogWindowRoundedCorners) findComponentInRoot("rcAskCancel");
		}
		return rcAskCancel;
	}

	protected HtmlDialogWindowContentPane getCpAskCancel() {
		if (cpAskCancel == null) {
			cpAskCancel = (HtmlDialogWindowContentPane) findComponentInRoot("cpAskCancel");
		}
		return cpAskCancel;
	}

	protected HtmlOutputText getLblConfirmCancel() {
		if (lblConfirmCancel == null) {
			lblConfirmCancel = (HtmlOutputText) findComponentInRoot("lblConfirmCancel");
		}
		return lblConfirmCancel;
	}

	protected HtmlLink getLnkCerrarAskCancel() {
		if (lnkCerrarAskCancel == null) {
			lnkCerrarAskCancel = (HtmlLink) findComponentInRoot("lnkCerrarAskCancel");
		}
		return lnkCerrarAskCancel;
	}

	protected HtmlDialogWindowAutoPostBackFlags getApbAskCancel() {
		if (apbAskCancel == null) {
			apbAskCancel = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbAskCancel");
		}
		return apbAskCancel;
	}

	protected HtmlDialogWindowHeader getHdSolicitarAutorizacion() {
		if (hdSolicitarAutorizacion == null) {
			hdSolicitarAutorizacion = (HtmlDialogWindowHeader) findComponentInRoot("hdSolicitarAutorizacion");
		}
		return hdSolicitarAutorizacion;
	}

	protected HtmlDialogButtonCloseBox getClbAutoriza() {
		if (clbAutoriza == null) {
			clbAutoriza = (HtmlDialogButtonCloseBox) findComponentInRoot("clbAutoriza");
		}
		return clbAutoriza;
	}

	protected HtmlJspPanel getJspPanel23() {
		if (jspPanel23 == null) {
			jspPanel23 = (HtmlJspPanel) findComponentInRoot("jspPanel23");
		}
		return jspPanel23;
	}

	protected HtmlOutputText getLblMensajeAutorizacion() {
		if (lblMensajeAutorizacion == null) {
			lblMensajeAutorizacion = (HtmlOutputText) findComponentInRoot("lblMensajeAutorizacion");
		}
		return lblMensajeAutorizacion;
	}

	protected HtmlOutputText getLblReferencia4() {
		if (lblReferencia4 == null) {
			lblReferencia4 = (HtmlOutputText) findComponentInRoot("lblReferencia4");
		}
		return lblReferencia4;
	}

	protected HtmlLink getLnkProcesarSolicitud() {
		if (lnkProcesarSolicitud == null) {
			lnkProcesarSolicitud = (HtmlLink) findComponentInRoot("lnkProcesarSolicitud");
		}
		return lnkProcesarSolicitud;
	}

	protected HtmlDialogWindow getDwAutoriza() {
		if (dwAutoriza == null) {
			dwAutoriza = (HtmlDialogWindow) findComponentInRoot("dwAutoriza");
		}
		return dwAutoriza;
	}

	protected HtmlDialogWindowClientEvents getCleAutorizaContado() {
		if (cleAutorizaContado == null) {
			cleAutorizaContado = (HtmlDialogWindowClientEvents) findComponentInRoot("cleAutorizaContado");
		}
		return cleAutorizaContado;
	}

	protected HtmlDialogWindowRoundedCorners getRcAutorizaContado() {
		if (rcAutorizaContado == null) {
			rcAutorizaContado = (HtmlDialogWindowRoundedCorners) findComponentInRoot("rcAutorizaContado");
		}
		return rcAutorizaContado;
	}

	protected HtmlDialogWindowContentPane getCpAutorizaContado() {
		if (cpAutorizaContado == null) {
			cpAutorizaContado = (HtmlDialogWindowContentPane) findComponentInRoot("cpAutorizaContado");
		}
		return cpAutorizaContado;
	}

	protected HtmlPanelGrid getGrid2() {
		if (grid2 == null) {
			grid2 = (HtmlPanelGrid) findComponentInRoot("grid2");
		}
		return grid2;
	}

	protected HtmlInputText getTxtReferencia() {
		if (txtReferencia == null) {
			txtReferencia = (HtmlInputText) findComponentInRoot("txtReferencia");
		}
		return txtReferencia;
	}

	protected HtmlOutputText getLblAut() {
		if (lblAut == null) {
			lblAut = (HtmlOutputText) findComponentInRoot("lblAut");
		}
		return lblAut;
	}

	protected HtmlDropDownList getDdlAutoriza() {
		if (ddlAutoriza == null) {
			ddlAutoriza = (HtmlDropDownList) findComponentInRoot("ddlAutoriza");
		}
		return ddlAutoriza;
	}

	protected HtmlOutputText getText2() {
		if (text2 == null) {
			text2 = (HtmlOutputText) findComponentInRoot("text2");
		}
		return text2;
	}

	protected HtmlDateChooser getTxtFecha() {
		if (txtFecha == null) {
			txtFecha = (HtmlDateChooser) findComponentInRoot("txtFecha");
		}
		return txtFecha;
	}

	protected HtmlOutputText getLblConcepto4() {
		if (lblConcepto4 == null) {
			lblConcepto4 = (HtmlOutputText) findComponentInRoot("lblConcepto4");
		}
		return lblConcepto4;
	}

	protected HtmlInputTextarea getTxtObs() {
		if (txtObs == null) {
			txtObs = (HtmlInputTextarea) findComponentInRoot("txtObs");
		}
		return txtObs;
	}

	protected HtmlLink getLnkCancelarSolicitud() {
		if (lnkCancelarSolicitud == null) {
			lnkCancelarSolicitud = (HtmlLink) findComponentInRoot("lnkCancelarSolicitud");
		}
		return lnkCancelarSolicitud;
	}

	protected HtmlDialogWindowAutoPostBackFlags getApbReciboContado() {
		if (apbReciboContado == null) {
			apbReciboContado = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbReciboContado");
		}
		return apbReciboContado;
	}

	protected HtmlDialogWindowHeader getHdDetalleFacturaCredito() {
		if (hdDetalleFacturaCredito == null) {
			hdDetalleFacturaCredito = (HtmlDialogWindowHeader) findComponentInRoot("hdDetalleFacturaCredito");
		}
		return hdDetalleFacturaCredito;
	}

	protected HtmlJspPanel getJspPanel4() {
		if (jspPanel4 == null) {
			jspPanel4 = (HtmlJspPanel) findComponentInRoot("jspPanel4");
		}
		return jspPanel4;
	}

	protected HtmlOutputText getText18() {
		if (text18 == null) {
			text18 = (HtmlOutputText) findComponentInRoot("text18");
		}
		return text18;
	}

	protected HtmlOutputText getText20() {
		if (text20 == null) {
			text20 = (HtmlOutputText) findComponentInRoot("text20");
		}
		return text20;
	}

	protected HtmlOutputText getLblCodigo23() {
		if (lblCodigo23 == null) {
			lblCodigo23 = (HtmlOutputText) findComponentInRoot("lblCodigo23");
		}
		return lblCodigo23;
	}

	protected HtmlOutputText getText1() {
		if (text1 == null) {
			text1 = (HtmlOutputText) findComponentInRoot("text1");
		}
		return text1;
	}

	protected HtmlOutputText getTxtNomCliente() {
		if (txtNomCliente == null) {
			txtNomCliente = (HtmlOutputText) findComponentInRoot("txtNomCliente");
		}
		return txtNomCliente;
	}

	protected HtmlOutputText getText3() {
		if (text3 == null) {
			text3 = (HtmlOutputText) findComponentInRoot("text3");
		}
		return text3;
	}

	protected HtmlOutputText getLblCompania() {
		if (lblCompania == null) {
			lblCompania = (HtmlOutputText) findComponentInRoot("lblCompania");
		}
		return lblCompania;
	}

	protected HtmlOutputText getTxtCompania() {
		if (txtCompania == null) {
			txtCompania = (HtmlOutputText) findComponentInRoot("txtCompania");
		}
		return txtCompania;
	}

	protected HtmlOutputText getText22() {
		if (text22 == null) {
			text22 = (HtmlOutputText) findComponentInRoot("text22");
		}
		return text22;
	}

	protected HtmlOutputText getText23() {
		if (text23 == null) {
			text23 = (HtmlOutputText) findComponentInRoot("text23");
		}
		return text23;
	}

	protected HtmlOutputText getLblFechalm() {
		if (lblFechalm == null) {
			lblFechalm = (HtmlOutputText) findComponentInRoot("lblFechalm");
		}
		return lblFechalm;
	}

	protected HtmlOutputText getTxtFechalm() {
		if (txtFechalm == null) {
			txtFechalm = (HtmlOutputText) findComponentInRoot("txtFechalm");
		}
		return txtFechalm;
	}

	protected HtmlOutputText getLblFechaVenc() {
		if (lblFechaVenc == null) {
			lblFechaVenc = (HtmlOutputText) findComponentInRoot("lblFechaVenc");
		}
		return lblFechaVenc;
	}

	protected HtmlOutputText getTxtFechaVenc() {
		if (txtFechaVenc == null) {
			txtFechaVenc = (HtmlOutputText) findComponentInRoot("txtFechaVenc");
		}
		return txtFechaVenc;
	}

	protected HtmlOutputText getLblNoOrden() {
		if (lblNoOrden == null) {
			lblNoOrden = (HtmlOutputText) findComponentInRoot("lblNoOrden");
		}
		return lblNoOrden;
	}

	protected HtmlOutputText getTxtNoOrden() {
		if (txtNoOrden == null) {
			txtNoOrden = (HtmlOutputText) findComponentInRoot("txtNoOrden");
		}
		return txtNoOrden;
	}

	protected HtmlOutputText getLblTipoOrden() {
		if (lblTipoOrden == null) {
			lblTipoOrden = (HtmlOutputText) findComponentInRoot("lblTipoOrden");
		}
		return lblTipoOrden;
	}

	protected HtmlOutputText getTxtTipoOrden() {
		if (txtTipoOrden == null) {
			txtTipoOrden = (HtmlOutputText) findComponentInRoot("txtTipoOrden");
		}
		return txtTipoOrden;
	}

	protected HtmlOutputText getLblObservaciones() {
		if (lblObservaciones == null) {
			lblObservaciones = (HtmlOutputText) findComponentInRoot("lblObservaciones");
		}
		return lblObservaciones;
	}

	protected HtmlOutputText getTxtObservaciones() {
		if (txtObservaciones == null) {
			txtObservaciones = (HtmlOutputText) findComponentInRoot("txtObservaciones");
		}
		return txtObservaciones;
	}

	protected HtmlOutputText getLblReferenciaFactura() {
		if (lblReferenciaFactura == null) {
			lblReferenciaFactura = (HtmlOutputText) findComponentInRoot("lblReferenciaFactura");
		}
		return lblReferenciaFactura;
	}

	protected HtmlOutputText getTxtReferenciaFactura() {
		if (txtReferenciaFactura == null) {
			txtReferenciaFactura = (HtmlOutputText) findComponentInRoot("txtReferenciaFactura");
		}
		return txtReferenciaFactura;
	}

	protected HtmlOutputText getLblNoBatch() {
		if (lblNoBatch == null) {
			lblNoBatch = (HtmlOutputText) findComponentInRoot("lblNoBatch");
		}
		return lblNoBatch;
	}

	protected HtmlOutputText getTxtNoBatch() {
		if (txtNoBatch == null) {
			txtNoBatch = (HtmlOutputText) findComponentInRoot("txtNoBatch");
		}
		return txtNoBatch;
	}

	protected HtmlOutputText getLblFechaBatch() {
		if (lblFechaBatch == null) {
			lblFechaBatch = (HtmlOutputText) findComponentInRoot("lblFechaBatch");
		}
		return lblFechaBatch;
	}

	protected HtmlOutputText getTxtFechaBatch() {
		if (txtFechaBatch == null) {
			txtFechaBatch = (HtmlOutputText) findComponentInRoot("txtFechaBatch");
		}
		return txtFechaBatch;
	}

	protected HtmlOutputText getLblFechaImp() {
		if (lblFechaImp == null) {
			lblFechaImp = (HtmlOutputText) findComponentInRoot("lblFechaImp");
		}
		return lblFechaImp;
	}

	protected HtmlOutputText getTxtFechaImp() {
		if (txtFechaImp == null) {
			txtFechaImp = (HtmlOutputText) findComponentInRoot("txtFechaImp");
		}
		return txtFechaImp;
	}

	protected HtmlOutputText getLblCompens() {
		if (lblCompens == null) {
			lblCompens = (HtmlOutputText) findComponentInRoot("lblCompens");
		}
		return lblCompens;
	}

	protected HtmlOutputText getTxtCompens() {
		if (txtCompens == null) {
			txtCompens = (HtmlOutputText) findComponentInRoot("txtCompens");
		}
		return txtCompens;
	}

	protected HtmlOutputText getLblFechaVenDecto() {
		if (lblFechaVenDecto == null) {
			lblFechaVenDecto = (HtmlOutputText) findComponentInRoot("lblFechaVenDecto");
		}
		return lblFechaVenDecto;
	}

	protected HtmlOutputText getTxtFechaVenDecto() {
		if (txtFechaVenDecto == null) {
			txtFechaVenDecto = (HtmlOutputText) findComponentInRoot("txtFechaVenDecto");
		}
		return txtFechaVenDecto;
	}

	protected HtmlOutputText getLblCondicion() {
		if (lblCondicion == null) {
			lblCondicion = (HtmlOutputText) findComponentInRoot("lblCondicion");
		}
		return lblCondicion;
	}

	protected HtmlOutputText getTxtCondicion() {
		if (txtCondicion == null) {
			txtCondicion = (HtmlOutputText) findComponentInRoot("txtCondicion");
		}
		return txtCondicion;
	}

	protected HtmlOutputText getLblDescuento() {
		if (lblDescuento == null) {
			lblDescuento = (HtmlOutputText) findComponentInRoot("lblDescuento");
		}
		return lblDescuento;
	}

	protected HtmlOutputText getTxtDescuento() {
		if (txtDescuento == null) {
			txtDescuento = (HtmlOutputText) findComponentInRoot("txtDescuento");
		}
		return txtDescuento;
	}

	protected HtmlOutputText getLblDescuentoAplicado() {
		if (lblDescuentoAplicado == null) {
			lblDescuentoAplicado = (HtmlOutputText) findComponentInRoot("lblDescuentoAplicado");
		}
		return lblDescuentoAplicado;
	}

	protected HtmlOutputText getTxtDescuentoAplicado() {
		if (txtDescuentoAplicado == null) {
			txtDescuentoAplicado = (HtmlOutputText) findComponentInRoot("txtDescuentoAplicado");
		}
		return txtDescuentoAplicado;
	}

	protected HtmlOutputText getText24() {
		if (text24 == null) {
			text24 = (HtmlOutputText) findComponentInRoot("text24");
		}
		return text24;
	}

	protected HtmlOutputText getTxtSubtotalDetCredito() {
		if (txtSubtotalDetCredito == null) {
			txtSubtotalDetCredito = (HtmlOutputText) findComponentInRoot("txtSubtotalDetCredito");
		}
		return txtSubtotalDetCredito;
	}

	protected HtmlOutputText getText28() {
		if (text28 == null) {
			text28 = (HtmlOutputText) findComponentInRoot("text28");
		}
		return text28;
	}

	protected HtmlOutputText getTxtIvaDetCredito() {
		if (txtIvaDetCredito == null) {
			txtIvaDetCredito = (HtmlOutputText) findComponentInRoot("txtIvaDetCredito");
		}
		return txtIvaDetCredito;
	}

	protected HtmlOutputText getText30() {
		if (text30 == null) {
			text30 = (HtmlOutputText) findComponentInRoot("text30");
		}
		return text30;
	}

	protected HtmlOutputText getTxtTotalDetCredito() {
		if (txtTotalDetCredito == null) {
			txtTotalDetCredito = (HtmlOutputText) findComponentInRoot("txtTotalDetCredito");
		}
		return txtTotalDetCredito;
	}

	protected HtmlOutputText getText230() {
		if (text230 == null) {
			text230 = (HtmlOutputText) findComponentInRoot("text230");
		}
		return text230;
	}

	protected HtmlOutputText getTxtPendienteDetCredito() {
		if (txtPendienteDetCredito == null) {
			txtPendienteDetCredito = (HtmlOutputText) findComponentInRoot("txtPendienteDetCredito");
		}
		return txtPendienteDetCredito;
	}

	protected HtmlGridView getGvDetalleFacCredito() {
		if (gvDetalleFacCredito == null) {
			gvDetalleFacCredito = (HtmlGridView) findComponentInRoot("gvDetalleFacCredito");
		}
		return gvDetalleFacCredito;
	}

	protected HtmlColumn getCoCoditem() {
		if (coCoditem == null) {
			coCoditem = (HtmlColumn) findComponentInRoot("coCoditem");
		}
		return coCoditem;
	}

	protected HtmlOutputText getLblCoditem1() {
		if (lblCoditem1 == null) {
			lblCoditem1 = (HtmlOutputText) findComponentInRoot("lblCoditem1");
		}
		return lblCoditem1;
	}

	protected HtmlOutputText getLblCoditem2() {
		if (lblCoditem2 == null) {
			lblCoditem2 = (HtmlOutputText) findComponentInRoot("lblCoditem2");
		}
		return lblCoditem2;
	}

	protected HtmlOutputText getLblDescitem1() {
		if (lblDescitem1 == null) {
			lblDescitem1 = (HtmlOutputText) findComponentInRoot("lblDescitem1");
		}
		return lblDescitem1;
	}

	protected HtmlOutputText getLblDescitem2() {
		if (lblDescitem2 == null) {
			lblDescitem2 = (HtmlOutputText) findComponentInRoot("lblDescitem2");
		}
		return lblDescitem2;
	}

	protected HtmlOutputText getLblCantDetalle1() {
		if (lblCantDetalle1 == null) {
			lblCantDetalle1 = (HtmlOutputText) findComponentInRoot("lblCantDetalle1");
		}
		return lblCantDetalle1;
	}

	protected HtmlOutputText getLblCantDetalle2() {
		if (lblCantDetalle2 == null) {
			lblCantDetalle2 = (HtmlOutputText) findComponentInRoot("lblCantDetalle2");
		}
		return lblCantDetalle2;
	}

	protected HtmlOutputText getLblPrecionunitDetalle1() {
		if (lblPrecionunitDetalle1 == null) {
			lblPrecionunitDetalle1 = (HtmlOutputText) findComponentInRoot("lblPrecionunitDetalle1");
		}
		return lblPrecionunitDetalle1;
	}

	protected HtmlOutputText getLblPrecionunitDetalle2() {
		if (lblPrecionunitDetalle2 == null) {
			lblPrecionunitDetalle2 = (HtmlOutputText) findComponentInRoot("lblPrecionunitDetalle2");
		}
		return lblPrecionunitDetalle2;
	}

	protected HtmlOutputText getLblImpuestoDetalle1() {
		if (lblImpuestoDetalle1 == null) {
			lblImpuestoDetalle1 = (HtmlOutputText) findComponentInRoot("lblImpuestoDetalle1");
		}
		return lblImpuestoDetalle1;
	}

	protected HtmlOutputText getLblImpuestoDetalle2() {
		if (lblImpuestoDetalle2 == null) {
			lblImpuestoDetalle2 = (HtmlOutputText) findComponentInRoot("lblImpuestoDetalle2");
		}
		return lblImpuestoDetalle2;
	}

	protected HtmlLink getLnkAceptarDetalleFacturaCredito() {
		if (lnkAceptarDetalleFacturaCredito == null) {
			lnkAceptarDetalleFacturaCredito = (HtmlLink) findComponentInRoot("lnkAceptarDetalleFacturaCredito");
		}
		return lnkAceptarDetalleFacturaCredito;
	}

	protected HtmlDialogWindow getDgwDetalleFacturaCredito() {
		if (dgwDetalleFacturaCredito == null) {
			dgwDetalleFacturaCredito = (HtmlDialogWindow) findComponentInRoot("dgwDetalleFacturaCredito");
		}
		return dgwDetalleFacturaCredito;
	}

	protected HtmlDialogWindowClientEvents getCleDetalleFacturaCredito() {
		if (cleDetalleFacturaCredito == null) {
			cleDetalleFacturaCredito = (HtmlDialogWindowClientEvents) findComponentInRoot("cleDetalleFacturaCredito");
		}
		return cleDetalleFacturaCredito;
	}

	protected HtmlDialogWindowRoundedCorners getRcDetalleFacturaCredito() {
		if (rcDetalleFacturaCredito == null) {
			rcDetalleFacturaCredito = (HtmlDialogWindowRoundedCorners) findComponentInRoot("rcDetalleFacturaCredito");
		}
		return rcDetalleFacturaCredito;
	}

	protected HtmlDialogWindowContentPane getCpDetalleFacturaCredito() {
		if (cpDetalleFacturaCredito == null) {
			cpDetalleFacturaCredito = (HtmlDialogWindowContentPane) findComponentInRoot("cpDetalleFacturaCredito");
		}
		return cpDetalleFacturaCredito;
	}

	protected HtmlOutputText getTxtFechaFactura() {
		if (txtFechaFactura == null) {
			txtFechaFactura = (HtmlOutputText) findComponentInRoot("txtFechaFactura");
		}
		return txtFechaFactura;
	}

	protected HtmlOutputText getTxtNoFactura() {
		if (txtNoFactura == null) {
			txtNoFactura = (HtmlOutputText) findComponentInRoot("txtNoFactura");
		}
		return txtNoFactura;
	}

	protected HtmlOutputText getLblTipoFactura() {
		if (lblTipoFactura == null) {
			lblTipoFactura = (HtmlOutputText) findComponentInRoot("lblTipoFactura");
		}
		return lblTipoFactura;
	}

	protected HtmlOutputText getTxtTipoFactura() {
		if (txtTipoFactura == null) {
			txtTipoFactura = (HtmlOutputText) findComponentInRoot("txtTipoFactura");
		}
		return txtTipoFactura;
	}

	protected HtmlOutputText getTxtCodigoCliente() {
		if (txtCodigoCliente == null) {
			txtCodigoCliente = (HtmlOutputText) findComponentInRoot("txtCodigoCliente");
		}
		return txtCodigoCliente;
	}

	protected HtmlDropDownList getDdlDetalleContado() {
		if (ddlDetalleContado == null) {
			ddlDetalleContado = (HtmlDropDownList) findComponentInRoot("ddlDetalleContado");
		}
		return ddlDetalleContado;
	}

	protected HtmlOutputText getText3333() {
		if (text3333 == null) {
			text3333 = (HtmlOutputText) findComponentInRoot("text3333");
		}
		return text3333;
	}

	protected HtmlColumn getCoDescitemCont() {
		if (coDescitemCont == null) {
			coDescitemCont = (HtmlColumn) findComponentInRoot("coDescitemCont");
		}
		return coDescitemCont;
	}

	protected HtmlColumn getCoCant() {
		if (coCant == null) {
			coCant = (HtmlColumn) findComponentInRoot("coCant");
		}
		return coCant;
	}

	protected HtmlColumn getCoPreciounit() {
		if (coPreciounit == null) {
			coPreciounit = (HtmlColumn) findComponentInRoot("coPreciounit");
		}
		return coPreciounit;
	}

	protected HtmlColumn getCoImpuesto() {
		if (coImpuesto == null) {
			coImpuesto = (HtmlColumn) findComponentInRoot("coImpuesto");
		}
		return coImpuesto;
	}

	protected HtmlDialogWindowAutoPostBackFlags getApbDetalleFacturaCredito() {
		if (apbDetalleFacturaCredito == null) {
			apbDetalleFacturaCredito = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbDetalleFacturaCredito");
		}
		return apbDetalleFacturaCredito;
	}

	protected HtmlDialogWindowHeader getHdReciboContado() {
		if (hdReciboContado == null) {
			hdReciboContado = (HtmlDialogWindowHeader) findComponentInRoot("hdReciboContado");
		}
		return hdReciboContado;
	}

	protected HtmlJspPanel getJspPanel1() {
		if (jspPanel1 == null) {
			jspPanel1 = (HtmlJspPanel) findComponentInRoot("jspPanel1");
		}
		return jspPanel1;
	}

	protected HtmlOutputText getLblFechaReciboCont() {
		if (lblFechaReciboCont == null) {
			lblFechaReciboCont = (HtmlOutputText) findComponentInRoot("lblFechaReciboCont");
		}
		return lblFechaReciboCont;
	}

	protected HtmlOutputText getTxtFechaRecibo() {
		if (txtFechaRecibo == null) {
			txtFechaRecibo = (HtmlOutputText) findComponentInRoot("txtFechaRecibo");
		}
		return txtFechaRecibo;
	}

	protected HtmlDateChooser getTxtFecham() {
		if (txtFecham == null) {
			txtFecham = (HtmlDateChooser) findComponentInRoot("txtFecham");
		}
		return txtFecham;
	}

	protected HtmlDateChooserClientEvents getDcClientEvents() {
		if (dcClientEvents == null) {
			dcClientEvents = (HtmlDateChooserClientEvents) findComponentInRoot("dcClientEvents");
		}
		return dcClientEvents;
	}

	protected HtmlOutputText getLblNumRecibo() {
		if (lblNumRecibo == null) {
			lblNumRecibo = (HtmlOutputText) findComponentInRoot("lblNumRecibo");
		}
		return lblNumRecibo;
	}

	protected HtmlPanelGrid getGrid5() {
		if (grid5 == null) {
			grid5 = (HtmlPanelGrid) findComponentInRoot("grid5");
		}
		return grid5;
	}

	protected HtmlOutputText getLblNumeroRecibo() {
		if (lblNumeroRecibo == null) {
			lblNumeroRecibo = (HtmlOutputText) findComponentInRoot("lblNumeroRecibo");
		}
		return lblNumeroRecibo;
	}

	protected HtmlOutputText getLblCliente() {
		if (lblCliente == null) {
			lblCliente = (HtmlOutputText) findComponentInRoot("lblCliente");
		}
		return lblCliente;
	}

	protected HtmlOutputText getLblCodigoSearch() {
		if (lblCodigoSearch == null) {
			lblCodigoSearch = (HtmlOutputText) findComponentInRoot("lblCodigoSearch");
		}
		return lblCodigoSearch;
	}

	protected HtmlOutputText getLblTipoRecibo() {
		if (lblTipoRecibo == null) {
			lblTipoRecibo = (HtmlOutputText) findComponentInRoot("lblTipoRecibo");
		}
		return lblTipoRecibo;
	}

	protected HtmlDropDownList getDdlTipoRecibo() {
		if (ddlTipoRecibo == null) {
			ddlTipoRecibo = (HtmlDropDownList) findComponentInRoot("ddlTipoRecibo");
		}
		return ddlTipoRecibo;
	}

	protected HtmlOutputText getLblMetodosPago() {
		if (lblMetodosPago == null) {
			lblMetodosPago = (HtmlOutputText) findComponentInRoot("lblMetodosPago");
		}
		return lblMetodosPago;
	}

	protected HtmlDropDownList getDdlMetodoPago() {
		if (ddlMetodoPago == null) {
			ddlMetodoPago = (HtmlDropDownList) findComponentInRoot("ddlMetodoPago");
		}
		return ddlMetodoPago;
	}

	protected HtmlOutputText getLbletVouchermanual() {
		if (lbletVouchermanual == null) {
			lbletVouchermanual = (HtmlOutputText) findComponentInRoot("lbletVouchermanual");
		}
		return lbletVouchermanual;
	}

	protected HtmlCheckBox getChkVoucherManual() {
		if (chkVoucherManual == null) {
			chkVoucherManual = (HtmlCheckBox) findComponentInRoot("chkVoucherManual");
		}
		return chkVoucherManual;
	}

	protected HtmlOutputText getLblMonto() {
		if (lblMonto == null) {
			lblMonto = (HtmlOutputText) findComponentInRoot("lblMonto");
		}
		return lblMonto;
	}

	protected HtmlInputText getTxtMonto() {
		if (txtMonto == null) {
			txtMonto = (HtmlInputText) findComponentInRoot("txtMonto");
		}
		return txtMonto;
	}

	protected HtmlOutputText getLblAfiliado() {
		if (lblAfiliado == null) {
			lblAfiliado = (HtmlOutputText) findComponentInRoot("lblAfiliado");
		}
		return lblAfiliado;
	}

	protected HtmlDropDownList getDdlAfiliado() {
		if (ddlAfiliado == null) {
			ddlAfiliado = (HtmlDropDownList) findComponentInRoot("ddlAfiliado");
		}
		return ddlAfiliado;
	}

	protected HtmlOutputText getLblReferencia1() {
		if (lblReferencia1 == null) {
			lblReferencia1 = (HtmlOutputText) findComponentInRoot("lblReferencia1");
		}
		return lblReferencia1;
	}

	protected HtmlInputText getTxtReferencia1() {
		if (txtReferencia1 == null) {
			txtReferencia1 = (HtmlInputText) findComponentInRoot("txtReferencia1");
		}
		return txtReferencia1;
	}

	protected HtmlDialogWindow getDwRecibosCredito() {
		if (dwRecibosCredito == null) {
			dwRecibosCredito = (HtmlDialogWindow) findComponentInRoot("dwRecibosCredito");
		}
		return dwRecibosCredito;
	}

	protected HtmlDialogWindowClientEvents getCleReciboConbtado() {
		if (cleReciboConbtado == null) {
			cleReciboConbtado = (HtmlDialogWindowClientEvents) findComponentInRoot("cleReciboConbtado");
		}
		return cleReciboConbtado;
	}

	protected HtmlDialogWindowRoundedCorners getRcREciboContado() {
		if (rcREciboContado == null) {
			rcREciboContado = (HtmlDialogWindowRoundedCorners) findComponentInRoot("rcREciboContado");
		}
		return rcREciboContado;
	}

	protected HtmlDialogWindowContentPane getCpReciboContado() {
		if (cpReciboContado == null) {
			cpReciboContado = (HtmlDialogWindowContentPane) findComponentInRoot("cpReciboContado");
		}
		return cpReciboContado;
	}

	protected HtmlOutputText getLblNumRecm() {
		if (lblNumRecm == null) {
			lblNumRecm = (HtmlOutputText) findComponentInRoot("lblNumRecm");
		}
		return lblNumRecm;
	}

	protected HtmlInputText getTxtNumRec() {
		if (txtNumRec == null) {
			txtNumRec = (HtmlInputText) findComponentInRoot("txtNumRec");
		}
		return txtNumRec;
	}

	protected HtmlOutputText getLblCod() {
		if (lblCod == null) {
			lblCod = (HtmlOutputText) findComponentInRoot("lblCod");
		}
		return lblCod;
	}

	protected HtmlOutputText getLblNombreSearch() {
		if (lblNombreSearch == null) {
			lblNombreSearch = (HtmlOutputText) findComponentInRoot("lblNombreSearch");
		}
		return lblNombreSearch;
	}

	protected HtmlOutputText getLblNom() {
		if (lblNom == null) {
			lblNom = (HtmlOutputText) findComponentInRoot("lblNom");
		}
		return lblNom;
	}

	protected HtmlDropDownList getDdlMoneda() {
		if (ddlMoneda == null) {
			ddlMoneda = (HtmlDropDownList) findComponentInRoot("ddlMoneda");
		}
		return ddlMoneda;
	}

	protected HtmlDropDownListClientEvents getClecmbFiltroMonedas() {
		if (clecmbFiltroMonedas == null) {
			clecmbFiltroMonedas = (HtmlDropDownListClientEvents) findComponentInRoot("clecmbFiltroMonedas");
		}
		return clecmbFiltroMonedas;
	}

	protected HtmlDropDownList getCmbFiltroMonedas() {
		if (cmbFiltroMonedas == null) {
			cmbFiltroMonedas = (HtmlDropDownList) findComponentInRoot("cmbFiltroMonedas");
		}
		return cmbFiltroMonedas;
	}

	protected HtmlOutputText getLblComapaniaCre() {
		if (lblComapaniaCre == null) {
			lblComapaniaCre = (HtmlOutputText) findComponentInRoot("lblComapaniaCre");
		}
		return lblComapaniaCre;
	}

	protected HtmlOutputText getLblSucursalCre() {
		if (lblSucursalCre == null) {
			lblSucursalCre = (HtmlOutputText) findComponentInRoot("lblSucursalCre");
		}
		return lblSucursalCre;
	}

	protected HtmlOutputText getLblUninegCre() {
		if (lblUninegCre == null) {
			lblUninegCre = (HtmlOutputText) findComponentInRoot("lblUninegCre");
		}
		return lblUninegCre;
	}

	protected HtmlOutputText getLblFiltroMoneda() {
		if (lblFiltroMoneda == null) {
			lblFiltroMoneda = (HtmlOutputText) findComponentInRoot("lblFiltroMoneda");
		}
		return lblFiltroMoneda;
	}

	protected HtmlGridAgFunction getAgFnContarDis() {
		if (agFnContarDis == null) {
			agFnContarDis = (HtmlGridAgFunction) findComponentInRoot("agFnContarDis");
		}
		return agFnContarDis;
	}

}
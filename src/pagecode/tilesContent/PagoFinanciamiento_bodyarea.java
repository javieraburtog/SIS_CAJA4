/**
 * 
 */
package pagecode.tilesContent;

import pagecode.PageCodeBase;
import javax.faces.component.html.HtmlOutputText;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
import com.infragistics.faces.input.component.html.HtmlCheckBox;
import javax.faces.component.html.HtmlInputText;
import com.infragistics.faces.shared.component.html.HtmlLink;
import com.infragistics.faces.window.component.html.HtmlDialogWindowClientEvents;
import com.ibm.faces.component.html.HtmlJspPanel;
import com.ibm.faces.component.html.HtmlGraphicImageEx;
import com.infragistics.faces.window.component.html.HtmlDialogWindow;
import com.infragistics.faces.window.component.html.HtmlDialogWindowRoundedCorners;
import com.infragistics.faces.window.component.html.HtmlDialogWindowContentPane;
import com.infragistics.faces.window.component.html.HtmlDialogWindowAutoPostBackFlags;
import com.infragistics.faces.input.component.html.HtmlDropDownListClientEvents;
import com.infragistics.faces.grid.component.html.HtmlColumn;
import javax.faces.component.html.HtmlForm;
import com.infragistics.faces.menu.component.html.HtmlMenu;
import com.infragistics.faces.menu.component.html.HtmlMenuItem;
import com.ibm.faces.component.html.HtmlPanelSection;
import com.ibm.faces.component.html.HtmlInputHelperTypeahead;
import com.infragistics.faces.grid.component.html.HtmlGridView;
import javax.faces.component.html.HtmlPanelGrid;
import com.infragistics.faces.window.component.html.HtmlDialogWindowHeader;
import com.infragistics.faces.input.component.html.HtmlDateChooser;
import com.infragistics.faces.input.component.html.HtmlDateChooserClientEvents;
import javax.faces.component.html.HtmlInputSecret;
import javax.faces.component.html.HtmlInputTextarea;
import javax.faces.component.UINamingContainer;
import com.ibm.faces.component.html.HtmlScriptCollector;
import com.infragistics.faces.grid.component.html.HtmlColumnSelectRow;

/**
 * @author Juan Ñamendi
 *
 */
public class PagoFinanciamiento_bodyarea extends PageCodeBase {

	protected HtmlOutputText lblMensajeError;
	protected HtmlDropDownList ddlBanco;
	protected HtmlDropDownList ddlAfiliado;
	protected HtmlDropDownList ddlMetodoPago;
	protected HtmlOutputText lbletVouchermanual;
	protected HtmlCheckBox chkVoucherManual;
	protected HtmlOutputText lblMonto;
	protected HtmlInputText txtMonto;
	protected HtmlDropDownList ddlMoneda;
	protected HtmlOutputText lblAfiliado;
	protected HtmlOutputText lblReferencia1;
	protected HtmlInputText txtReferencia1;
	protected HtmlOutputText lblBanco;
	protected HtmlOutputText lblReferencia2;
	protected HtmlInputText txtReferencia2;
	protected HtmlOutputText lblReferencia3;
	protected HtmlInputText txtReferencia3;
	protected HtmlLink lnkRegistrarPago;
	protected HtmlInputText txtMontoAplicar;
	protected HtmlLink lnkFijarMontoaplicado;
	protected HtmlLink lnkSearchContado;
	protected HtmlDialogWindowClientEvents cledwCargando;
	protected HtmlJspPanel jspdwCargando;
	protected HtmlGraphicImageEx imagenCargando;
	protected HtmlDialogWindow dwCargando;
	protected HtmlDialogWindowRoundedCorners cledwCargando22;
	protected HtmlDialogWindowContentPane cpdwCargando;
	protected HtmlDialogWindowAutoPostBackFlags apbdwCargando;
	protected HtmlLink lnkProcesarRecibo2;
	protected HtmlLink lnkAceptarProcesaRecibo;
	protected HtmlLink lnkCerrarProcesaRecibo;
	protected HtmlDropDownListClientEvents cleddlComapaniaCre;
	protected HtmlDropDownListClientEvents clecmbFiltroMonedas;
	protected HtmlForm frmFinan;
	protected HtmlMenu menu1;
	protected HtmlMenuItem item0;
	protected HtmlMenuItem item1;
	protected HtmlMenuItem item2;
	protected HtmlOutputText lblTitulo1Contado;
	protected HtmlPanelSection secFinan1;
	protected HtmlJspPanel jspPanel7;
	protected HtmlGraphicImageEx imageEx4Cont;
	protected HtmlJspPanel jspPanel6;
	protected HtmlGraphicImageEx imageEx3;
	protected HtmlOutputText lblTipoBusqueda;
	protected HtmlDropDownList dropBusqueda;
	protected HtmlInputText txtParametro;
	protected HtmlInputHelperTypeahead tphContado;
	protected HtmlGraphicImageEx imgLoader;
	protected HtmlOutputText txtMensaje;
	protected HtmlGridView gvFacsFinan;
	protected HtmlOutputText lblHeader;
	protected HtmlOutputText lblSelecctedGrande;
	protected HtmlLink lnkDetalleFacturaContado;
	protected HtmlOutputText lblDetalleFacturaGrande;
	protected HtmlOutputText lblNoSol1Grande;
	protected HtmlOutputText lblNoSol2Grande;
	protected HtmlOutputText lblTipofactura1Grande;
	protected HtmlOutputText lblTipofactura2Grande;
	protected HtmlOutputText lblnomcli1Grande;
	protected HtmlOutputText lblnomcli2Grande;
	protected HtmlOutputText lblUnineg1Grande;
	protected HtmlOutputText lblUnineg2Grande;
	protected HtmlOutputText lblTotal1Grande;
	protected HtmlOutputText lblTotal2Grande;
	protected HtmlOutputText lblMoneda1Grande;
	protected HtmlOutputText lblMoneda2Grande;
	protected HtmlOutputText lblFechaVence1Grande;
	protected HtmlOutputText lblFechaVence2Grande;
	protected HtmlPanelGrid grid1;
	protected HtmlOutputText lblFiltroMoneda;
	protected HtmlDialogWindowHeader hdDetalleSol;
	protected HtmlJspPanel jspDetalleSolictud;
	protected HtmlOutputText text18;
	protected HtmlOutputText lblNosol23;
	protected HtmlOutputText lblCodigo23;
	protected HtmlOutputText text1;
	protected HtmlOutputText txtNomCliente;
	protected HtmlOutputText text3;
	protected HtmlGridView gvDetalleProductoSol;
	protected HtmlColumn coCoditem;
	protected HtmlOutputText lblCoditem1;
	protected HtmlOutputText lblCoditem2;
	protected HtmlOutputText lblDescitem1;
	protected HtmlOutputText lblDescitem2;
	protected HtmlOutputText lblMarca1;
	protected HtmlOutputText lblMarca2;
	protected HtmlOutputText lblModelo1;
	protected HtmlOutputText lblModelo2;
	protected HtmlOutputText lblDescripcion1;
	protected HtmlOutputText lblDescripcion2;
	protected HtmlOutputText lblCant1;
	protected HtmlOutputText lblCant2;
	protected HtmlOutputText lblPrecionunitDetalle1;
	protected HtmlOutputText lblPrecionunitDetalle2;
	protected HtmlGridView gvDetallesol;
	protected HtmlColumn coNocuota;
	protected HtmlOutputText lblnocuota1;
	protected HtmlOutputText lblnocuota2;
	protected HtmlOutputText lblFechaVenc1;
	protected HtmlOutputText lblFechaVenc2;
	protected HtmlOutputText lblPrincipal1;
	protected HtmlOutputText lblPrincipal2;
	protected HtmlOutputText lblInteres1;
	protected HtmlOutputText lblInteres2;
	protected HtmlOutputText lblImpuesto1;
	protected HtmlOutputText lblImpuesto2;
	protected HtmlOutputText lblMonto2;
	protected HtmlOutputText lblMoraDet1;
	protected HtmlOutputText lblMoraDet2;
	protected HtmlOutputText lblmontopend1;
	protected HtmlOutputText lblMontopend2;
	protected HtmlOutputText lblPrincipalDet;
	protected HtmlOutputText txtPrincipalDet;
	protected HtmlOutputText lblInteresDet;
	protected HtmlOutputText txtInteresDet;
	protected HtmlOutputText lblImpuestoDet;
	protected HtmlOutputText txtImpuestoDet;
	protected HtmlOutputText lblMoraDet22;
	protected HtmlOutputText txtMoraDet22;
	protected HtmlOutputText lblTotalDet22;
	protected HtmlOutputText txtTotalDet22;
	protected HtmlOutputText lblPendienteDet22;
	protected HtmlOutputText txtPendienteDet22;
	protected HtmlLink lnkCerrarDetalleContado;
	protected HtmlDialogWindowHeader hdReciboFinan;
	protected HtmlJspPanel jspReciboFinan;
	protected HtmlOutputText lblFechaReciboFinan;
	protected HtmlOutputText txtFechaRecibo;
	protected HtmlDateChooser txtFecham;
	protected HtmlDateChooserClientEvents cleReciboContado;
	protected HtmlOutputText lblNumRecibo;
	protected HtmlPanelGrid grid5;
	protected HtmlOutputText lblNumeroRecibo;
	protected HtmlOutputText lblCliente;
	protected HtmlOutputText lblCodigoSearch;
	protected HtmlOutputText lblTipoRecibo;
	protected HtmlDropDownList ddlTipoRecibo;
	protected HtmlOutputText lblMetodosPago;
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
	protected HtmlGridView gvFacsSel;
	protected HtmlColumn coDetalleFacturaRecibo;
	protected HtmlLink lnkDetalleFact;
	protected HtmlOutputText lblDetalleFacturaRecibo;
	protected HtmlOutputText lblTipofactura1Detalle;
	protected HtmlOutputText lblTipofactura2Detalle;
	protected HtmlOutputText lblCuotafaTipoctura1Detalle;
	protected HtmlOutputText lblCuotaTipofactura2Detalle;
	protected HtmlOutputText lblCuotafactura1Detalle;
	protected HtmlOutputText lblCuotafactura2Detalle;
	protected HtmlOutputText lblFechaDetalleRecibo;
	protected HtmlOutputText lblFechaDetalleRecibo2;
	protected HtmlOutputText lblTotalDetalleRecibo;
	protected HtmlOutputText lblTotalDetalleRecibo2;
	protected HtmlOutputText lblTotalDetalleReciboA2;
	protected HtmlLink lnkAgregarFactura;
	protected HtmlOutputText lblConcepto;
	protected HtmlInputTextarea txtConcepto;
	protected HtmlOutputText lblMontoAplicar;
	protected HtmlOutputText lblMontoRecibido;
	protected HtmlOutputText txtMontoRecibido;
	protected HtmlOutputText lblCambio;
	protected HtmlPanelGrid grCambio;
	protected HtmlInputText txtCambioForaneo;
	protected HtmlOutputText lblPendienteDom;
	protected HtmlOutputText txtPendienteDom;
	protected HtmlOutputText lblCambioDomestico;
	protected HtmlOutputText txtCambioDomestico;
	protected HtmlLink lnkProcesarRecibo;
	protected HtmlDialogWindowHeader hdMensajeError;
	protected HtmlPanelGrid grdMensajeError;
	protected HtmlGraphicImageEx imgMensajeError;
	protected HtmlLink lnkMensajeError;
	protected HtmlDialogWindowHeader hdSolicitarAutorizacion;
	protected HtmlOutputText lblMensajeAutorizacion;
	protected HtmlOutputText lblReferencia4;
	protected HtmlLink lnkProcesarSolicitud;
	protected HtmlDialogWindowHeader hdDetalleCuota;
	protected HtmlJspPanel jspDetalleCuota;
	protected HtmlOutputText lblFechaCuotaDet;
	protected HtmlOutputText lblNoCuota23;
	protected HtmlOutputText lblCompaniaCuotaDet;
	protected HtmlOutputText lblMonedaCuotaDet;
	protected HtmlOutputText lblSucursalCuotaDet;
	protected HtmlOutputText lblTasaCuotaDet;
	protected HtmlOutputText lblprincipalCuotaDet;
	protected HtmlOutputText txtprincipalCuotaDet;
	protected HtmlOutputText lblinteresCuotaDet;
	protected HtmlOutputText txtinteresCuotaDet;
	protected HtmlOutputText lblimpuestoCuotaDet;
	protected HtmlOutputText txtimpuestoCuotaDet;
	protected HtmlOutputText lbltotalCuotaDet;
	protected HtmlOutputText txttotalCuotaDet;
	protected HtmlOutputText lblpendienteCuotaDet;
	protected HtmlOutputText txtpendienteCuotaDet;
	protected HtmlOutputText lbldiasVenCuotaDet;
	protected HtmlOutputText txtdiasVenCuotaDet;
	protected HtmlOutputText lbldiasAcumCuotaDet;
	protected HtmlOutputText txtdiasAcumCuotaDet;
	protected HtmlOutputText lbltotalDiasCuotaDet;
	protected HtmlOutputText txttotalDiasCuotaDet;
	protected HtmlOutputText lblmoraCuotaDet;
	protected HtmlOutputText txtmoraCuotaDet;
	protected HtmlLink lnkCerrarDetalleCuota;
	protected HtmlDialogWindowHeader hdAgregarFactura;
	protected HtmlJspPanel jspAgregarFactura;
	protected HtmlGridView gvAgregarFactura;
	protected HtmlColumn coDetalleAgregarFactura;
	protected HtmlLink lnkDetalleAgregarFactura;
	protected HtmlOutputText lblDetalleAgregarFactura1;
	protected HtmlOutputText lblNofacturaAgregarFactura;
	protected HtmlOutputText lblNofacturaAgregarFactura2;
	protected HtmlOutputText lblPartida1AgregarFactura;
	protected HtmlOutputText lblPartida2AgregarFactura;
	protected HtmlOutputText lblUniNeg3AgregarFactura;
	protected HtmlOutputText lblUninegCreditoAdd;
	protected HtmlOutputText lblUninegCredito2Add;
	protected HtmlOutputText lblTotalAgregarFactura;
	protected HtmlOutputText lblTotalAgregarFactura2;
	protected HtmlLink lnkAceptarAgregarFatura;
	protected HtmlDialogWindowHeader hdProcesaRecibo;
	protected HtmlPanelGrid gridProcesaRecibo;
	protected HtmlGraphicImageEx imageEx2ProcesaRecibo;
	protected HtmlDialogWindowHeader hdAskCancel;
	protected HtmlPanelGrid gridAskCancel;
	protected HtmlGraphicImageEx imageEx2AskCancel;
	protected HtmlLink lnkCerrarMensajeAskCancel;
	protected UINamingContainer vfFinan;
	protected HtmlScriptCollector scFinan;
	protected HtmlOutputText lblTituloContado80;
	protected HtmlOutputText txtBusquedaContado;
	protected HtmlOutputText txtBusquedaContado2;
	protected HtmlJspPanel jspPanel100;
	protected HtmlColumnSelectRow columnSelectRowRenderer1;
	protected HtmlColumn coLnkDetalle;
	protected HtmlColumn coNoSolicitud;
	protected HtmlColumn coTipoFactura;
	protected HtmlColumn coNomcli;
	protected HtmlColumn coUniNeg;
	protected HtmlColumn coTotal;
	protected HtmlColumn coMoneda;
	protected HtmlColumn coFechaVence;
	protected HtmlDropDownList cmbFiltroMonedas;
	protected HtmlOutputText lblFiltroFactura;
	protected HtmlDropDownList cmbFiltroFacturas;
	protected HtmlDialogWindow dwDetalleSol;
	protected HtmlDialogWindowClientEvents clDetalleSolicitud;
	protected HtmlDialogWindowRoundedCorners crDetalleSolicitud;
	protected HtmlDialogWindowContentPane cnpDetalleSolicitud;
	protected HtmlOutputText txtFechaFactura;
	protected HtmlOutputText txtNosolDet;
	protected HtmlOutputText lblTiposol23;
	protected HtmlOutputText txtTiposolDet;
	protected HtmlOutputText txtCodigoCliente;
	protected HtmlOutputText txtMonedaDetalleSol1;
	protected HtmlDropDownList ddlMonedaDet;
	protected HtmlOutputText text3333;
	protected HtmlColumn coDescitemSol;
	protected HtmlColumn coMarca;
	protected HtmlColumn coModelo;
	protected HtmlColumn coDescripcion;
	protected HtmlColumn coCant;
	protected HtmlColumn coPreciounit;
	protected HtmlColumn coFechaVenc;
	protected HtmlColumn coPrincipalSol;
	protected HtmlColumn coInteres;
	protected HtmlColumn coImpuesto;
	protected HtmlColumn coMonto;
	protected HtmlColumn coMoraDet1;
	protected HtmlColumn coMontopend;
	protected HtmlDialogWindowAutoPostBackFlags apbDetalleSolicitud;
	protected HtmlDialogWindow dwRecibo;
	protected HtmlDialogWindowClientEvents cleReciboFinan;
	protected HtmlDialogWindowRoundedCorners rcReciboFinan;
	protected HtmlDialogWindowContentPane cpReciboFinan;
	protected HtmlOutputText lblNumRecm;
	protected HtmlInputText txtNumRec;
	protected HtmlOutputText lblCod;
	protected HtmlOutputText lblNombreSearch;
	protected HtmlOutputText lblNom;
	protected HtmlCheckBox chkImprimir;
	protected HtmlColumn coMetodo;
	protected HtmlColumn coMonedaDetalle;
	protected HtmlColumn coTasa;
	protected HtmlColumn coEquivalente;
	protected HtmlColumn coReferencia;
	protected HtmlColumn coReferencia2;
	protected HtmlColumn coReferencia3;
	protected HtmlColumn coReferencia4;
	protected HtmlOutputText lblTasaCambio2;
	protected HtmlOutputText lblTasaJDE2;
	protected HtmlColumn coTipoFacturaDtalle;
	protected HtmlColumn coCuotaTipoFacturaDtalle;
	protected HtmlColumn coCuotaFacturaDtalle;
	protected HtmlColumn coFechaDetalleRecibo;
	protected HtmlColumn coTotalDetalleRecibo;
	protected HtmlColumn coTotalDetalleReciboA;
	protected HtmlOutputText lblTotalDetalleReciboA;
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
	protected HtmlDialogWindowAutoPostBackFlags apbReciboFinan100;
	protected HtmlDialogWindow dwMensajeError;
	protected HtmlDialogWindowClientEvents cleMensajeError;
	protected HtmlDialogWindowRoundedCorners rcMensajeError;
	protected HtmlDialogWindowContentPane cpMensajeError;
	protected HtmlJspPanel jspMensajeError;
	protected HtmlDialogWindowAutoPostBackFlags apbMensajeError;
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
	protected HtmlJspPanel jspPanel23;
	protected HtmlLink lnkCancelarSolicitud;
	protected HtmlDialogWindowAutoPostBackFlags apbReciboContado;
	protected HtmlDialogWindow dwDetalleCuota;
	protected HtmlDialogWindowClientEvents clDetalleCuota;
	protected HtmlDialogWindowRoundedCorners crDetalleCuota;
	protected HtmlDialogWindowContentPane cnpDetalleCuota;
	protected HtmlOutputText txtFechaCuotaDet;
	protected HtmlOutputText txtNoCuotaDet;
	protected HtmlOutputText txtCompaniaDetalleCuota1;
	protected HtmlOutputText txtMonedaDetalleCuota1;
	protected HtmlOutputText txtSucursalDetalleCuota1;
	protected HtmlOutputText txtTasaCuotaDet;
	protected HtmlDialogWindowAutoPostBackFlags apbDetalleCuota;
	protected HtmlDialogWindow dwAgregarFactura;
	protected HtmlDialogWindowClientEvents cleAgregarFactura;
	protected HtmlDialogWindowRoundedCorners rcAgregarFactura;
	protected HtmlDialogWindowContentPane cpAgregarFactura;
	protected HtmlColumn coNofacturaAgregarFactura;
	protected HtmlColumn coPartidaAgregarFactura;
	protected HtmlColumn coUninegCreditoAdd;
	protected HtmlColumn coTotalAgregarFactura;
	protected HtmlDialogWindowAutoPostBackFlags apbAgregarFactura;
	protected HtmlDialogWindow dwProcesaRecibo;
	protected HtmlDialogWindowClientEvents cleProcesaRecibo;
	protected HtmlDialogWindowRoundedCorners rcProcesaRecibo;
	protected HtmlDialogWindowContentPane cpProcesaRecibo;
	protected HtmlOutputText lblConfirmPrint;
	protected HtmlJspPanel jspProcesaRecibo;
	protected HtmlDialogWindowAutoPostBackFlags apbProcesaRecibo;
	protected HtmlDialogWindow dwAskCancel;
	protected HtmlDialogWindowClientEvents cleAskCancel;
	protected HtmlDialogWindowRoundedCorners rcAskCancel;
	protected HtmlDialogWindowContentPane cpAskCancel;
	protected HtmlOutputText lblConfirmCancel;
	protected HtmlJspPanel jspPanel3AskCancel;
	protected HtmlLink lnkCerrarAskCancel;
	protected HtmlDialogWindowAutoPostBackFlags apbAskCancel;
	protected HtmlOutputText getLblMensajeError() {
		if (lblMensajeError == null) {
			lblMensajeError = (HtmlOutputText) findComponentInRoot("lblMensajeError");
		}
		return lblMensajeError;
	}

	protected HtmlDropDownList getDdlBanco() {
		if (ddlBanco == null) {
			ddlBanco = (HtmlDropDownList) findComponentInRoot("ddlBanco");
		}
		return ddlBanco;
	}

	protected HtmlDropDownList getDdlAfiliado() {
		if (ddlAfiliado == null) {
			ddlAfiliado = (HtmlDropDownList) findComponentInRoot("ddlAfiliado");
		}
		return ddlAfiliado;
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

	protected HtmlDropDownList getDdlMoneda() {
		if (ddlMoneda == null) {
			ddlMoneda = (HtmlDropDownList) findComponentInRoot("ddlMoneda");
		}
		return ddlMoneda;
	}

	protected HtmlOutputText getLblAfiliado() {
		if (lblAfiliado == null) {
			lblAfiliado = (HtmlOutputText) findComponentInRoot("lblAfiliado");
		}
		return lblAfiliado;
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

	protected HtmlInputText getTxtMontoAplicar() {
		if (txtMontoAplicar == null) {
			txtMontoAplicar = (HtmlInputText) findComponentInRoot("txtMontoAplicar");
		}
		return txtMontoAplicar;
	}

	protected HtmlLink getLnkFijarMontoaplicado() {
		if (lnkFijarMontoaplicado == null) {
			lnkFijarMontoaplicado = (HtmlLink) findComponentInRoot("lnkFijarMontoaplicado");
		}
		return lnkFijarMontoaplicado;
	}

	protected HtmlLink getLnkSearchContado() {
		if (lnkSearchContado == null) {
			lnkSearchContado = (HtmlLink) findComponentInRoot("lnkSearchContado");
		}
		return lnkSearchContado;
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

	protected HtmlLink getLnkProcesarRecibo2() {
		if (lnkProcesarRecibo2 == null) {
			lnkProcesarRecibo2 = (HtmlLink) findComponentInRoot("lnkProcesarRecibo2");
		}
		return lnkProcesarRecibo2;
	}

	protected HtmlLink getLnkAceptarProcesaRecibo() {
		if (lnkAceptarProcesaRecibo == null) {
			lnkAceptarProcesaRecibo = (HtmlLink) findComponentInRoot("lnkAceptarProcesaRecibo");
		}
		return lnkAceptarProcesaRecibo;
	}

	protected HtmlLink getLnkCerrarProcesaRecibo() {
		if (lnkCerrarProcesaRecibo == null) {
			lnkCerrarProcesaRecibo = (HtmlLink) findComponentInRoot("lnkCerrarProcesaRecibo");
		}
		return lnkCerrarProcesaRecibo;
	}

	protected HtmlDropDownListClientEvents getCleddlComapaniaCre() {
		if (cleddlComapaniaCre == null) {
			cleddlComapaniaCre = (HtmlDropDownListClientEvents) findComponentInRoot("cleddlComapaniaCre");
		}
		return cleddlComapaniaCre;
	}

	protected HtmlDropDownListClientEvents getClecmbFiltroMonedas() {
		if (clecmbFiltroMonedas == null) {
			clecmbFiltroMonedas = (HtmlDropDownListClientEvents) findComponentInRoot("clecmbFiltroMonedas");
		}
		return clecmbFiltroMonedas;
	}

	protected HtmlForm getFrmFinan() {
		if (frmFinan == null) {
			frmFinan = (HtmlForm) findComponentInRoot("frmFinan");
		}
		return frmFinan;
	}

	protected HtmlMenu getMenu1() {
		if (menu1 == null) {
			menu1 = (HtmlMenu) findComponentInRoot("menu1");
		}
		return menu1;
	}

	protected HtmlMenuItem getItem0() {
		if (item0 == null) {
			item0 = (HtmlMenuItem) findComponentInRoot("item0");
		}
		return item0;
	}

	protected HtmlMenuItem getItem1() {
		if (item1 == null) {
			item1 = (HtmlMenuItem) findComponentInRoot("item1");
		}
		return item1;
	}

	protected HtmlMenuItem getItem2() {
		if (item2 == null) {
			item2 = (HtmlMenuItem) findComponentInRoot("item2");
		}
		return item2;
	}

	protected HtmlOutputText getLblTitulo1Contado() {
		if (lblTitulo1Contado == null) {
			lblTitulo1Contado = (HtmlOutputText) findComponentInRoot("lblTitulo1Contado");
		}
		return lblTitulo1Contado;
	}

	protected HtmlPanelSection getSecFinan1() {
		if (secFinan1 == null) {
			secFinan1 = (HtmlPanelSection) findComponentInRoot("secFinan1");
		}
		return secFinan1;
	}

	protected HtmlJspPanel getJspPanel7() {
		if (jspPanel7 == null) {
			jspPanel7 = (HtmlJspPanel) findComponentInRoot("jspPanel7");
		}
		return jspPanel7;
	}

	protected HtmlGraphicImageEx getImageEx4Cont() {
		if (imageEx4Cont == null) {
			imageEx4Cont = (HtmlGraphicImageEx) findComponentInRoot("imageEx4Cont");
		}
		return imageEx4Cont;
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

	protected HtmlInputHelperTypeahead getTphContado() {
		if (tphContado == null) {
			tphContado = (HtmlInputHelperTypeahead) findComponentInRoot("tphContado");
		}
		return tphContado;
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

	protected HtmlGridView getGvFacsFinan() {
		if (gvFacsFinan == null) {
			gvFacsFinan = (HtmlGridView) findComponentInRoot("gvFacsFinan");
		}
		return gvFacsFinan;
	}

	protected HtmlOutputText getLblHeader() {
		if (lblHeader == null) {
			lblHeader = (HtmlOutputText) findComponentInRoot("lblHeader");
		}
		return lblHeader;
	}

	protected HtmlOutputText getLblSelecctedGrande() {
		if (lblSelecctedGrande == null) {
			lblSelecctedGrande = (HtmlOutputText) findComponentInRoot("lblSelecctedGrande");
		}
		return lblSelecctedGrande;
	}

	protected HtmlLink getLnkDetalleFacturaContado() {
		if (lnkDetalleFacturaContado == null) {
			lnkDetalleFacturaContado = (HtmlLink) findComponentInRoot("lnkDetalleFacturaContado");
		}
		return lnkDetalleFacturaContado;
	}

	protected HtmlOutputText getLblDetalleFacturaGrande() {
		if (lblDetalleFacturaGrande == null) {
			lblDetalleFacturaGrande = (HtmlOutputText) findComponentInRoot("lblDetalleFacturaGrande");
		}
		return lblDetalleFacturaGrande;
	}

	protected HtmlOutputText getLblNoSol1Grande() {
		if (lblNoSol1Grande == null) {
			lblNoSol1Grande = (HtmlOutputText) findComponentInRoot("lblNoSol1Grande");
		}
		return lblNoSol1Grande;
	}

	protected HtmlOutputText getLblNoSol2Grande() {
		if (lblNoSol2Grande == null) {
			lblNoSol2Grande = (HtmlOutputText) findComponentInRoot("lblNoSol2Grande");
		}
		return lblNoSol2Grande;
	}

	protected HtmlOutputText getLblTipofactura1Grande() {
		if (lblTipofactura1Grande == null) {
			lblTipofactura1Grande = (HtmlOutputText) findComponentInRoot("lblTipofactura1Grande");
		}
		return lblTipofactura1Grande;
	}

	protected HtmlOutputText getLblTipofactura2Grande() {
		if (lblTipofactura2Grande == null) {
			lblTipofactura2Grande = (HtmlOutputText) findComponentInRoot("lblTipofactura2Grande");
		}
		return lblTipofactura2Grande;
	}

	protected HtmlOutputText getLblnomcli1Grande() {
		if (lblnomcli1Grande == null) {
			lblnomcli1Grande = (HtmlOutputText) findComponentInRoot("lblnomcli1Grande");
		}
		return lblnomcli1Grande;
	}

	protected HtmlOutputText getLblnomcli2Grande() {
		if (lblnomcli2Grande == null) {
			lblnomcli2Grande = (HtmlOutputText) findComponentInRoot("lblnomcli2Grande");
		}
		return lblnomcli2Grande;
	}

	protected HtmlOutputText getLblUnineg1Grande() {
		if (lblUnineg1Grande == null) {
			lblUnineg1Grande = (HtmlOutputText) findComponentInRoot("lblUnineg1Grande");
		}
		return lblUnineg1Grande;
	}

	protected HtmlOutputText getLblUnineg2Grande() {
		if (lblUnineg2Grande == null) {
			lblUnineg2Grande = (HtmlOutputText) findComponentInRoot("lblUnineg2Grande");
		}
		return lblUnineg2Grande;
	}

	protected HtmlOutputText getLblTotal1Grande() {
		if (lblTotal1Grande == null) {
			lblTotal1Grande = (HtmlOutputText) findComponentInRoot("lblTotal1Grande");
		}
		return lblTotal1Grande;
	}

	protected HtmlOutputText getLblTotal2Grande() {
		if (lblTotal2Grande == null) {
			lblTotal2Grande = (HtmlOutputText) findComponentInRoot("lblTotal2Grande");
		}
		return lblTotal2Grande;
	}

	protected HtmlOutputText getLblMoneda1Grande() {
		if (lblMoneda1Grande == null) {
			lblMoneda1Grande = (HtmlOutputText) findComponentInRoot("lblMoneda1Grande");
		}
		return lblMoneda1Grande;
	}

	protected HtmlOutputText getLblMoneda2Grande() {
		if (lblMoneda2Grande == null) {
			lblMoneda2Grande = (HtmlOutputText) findComponentInRoot("lblMoneda2Grande");
		}
		return lblMoneda2Grande;
	}

	protected HtmlOutputText getLblFechaVence1Grande() {
		if (lblFechaVence1Grande == null) {
			lblFechaVence1Grande = (HtmlOutputText) findComponentInRoot("lblFechaVence1Grande");
		}
		return lblFechaVence1Grande;
	}

	protected HtmlOutputText getLblFechaVence2Grande() {
		if (lblFechaVence2Grande == null) {
			lblFechaVence2Grande = (HtmlOutputText) findComponentInRoot("lblFechaVence2Grande");
		}
		return lblFechaVence2Grande;
	}

	protected HtmlPanelGrid getGrid1() {
		if (grid1 == null) {
			grid1 = (HtmlPanelGrid) findComponentInRoot("grid1");
		}
		return grid1;
	}

	protected HtmlOutputText getLblFiltroMoneda() {
		if (lblFiltroMoneda == null) {
			lblFiltroMoneda = (HtmlOutputText) findComponentInRoot("lblFiltroMoneda");
		}
		return lblFiltroMoneda;
	}

	protected HtmlDialogWindowHeader getHdDetalleSol() {
		if (hdDetalleSol == null) {
			hdDetalleSol = (HtmlDialogWindowHeader) findComponentInRoot("hdDetalleSol");
		}
		return hdDetalleSol;
	}

	protected HtmlJspPanel getJspDetalleSolictud() {
		if (jspDetalleSolictud == null) {
			jspDetalleSolictud = (HtmlJspPanel) findComponentInRoot("jspDetalleSolictud");
		}
		return jspDetalleSolictud;
	}

	protected HtmlOutputText getText18() {
		if (text18 == null) {
			text18 = (HtmlOutputText) findComponentInRoot("text18");
		}
		return text18;
	}

	protected HtmlOutputText getLblNosol23() {
		if (lblNosol23 == null) {
			lblNosol23 = (HtmlOutputText) findComponentInRoot("lblNosol23");
		}
		return lblNosol23;
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

	protected HtmlGridView getGvDetalleProductoSol() {
		if (gvDetalleProductoSol == null) {
			gvDetalleProductoSol = (HtmlGridView) findComponentInRoot("gvDetalleProductoSol");
		}
		return gvDetalleProductoSol;
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

	protected HtmlOutputText getLblMarca1() {
		if (lblMarca1 == null) {
			lblMarca1 = (HtmlOutputText) findComponentInRoot("lblMarca1");
		}
		return lblMarca1;
	}

	protected HtmlOutputText getLblMarca2() {
		if (lblMarca2 == null) {
			lblMarca2 = (HtmlOutputText) findComponentInRoot("lblMarca2");
		}
		return lblMarca2;
	}

	protected HtmlOutputText getLblModelo1() {
		if (lblModelo1 == null) {
			lblModelo1 = (HtmlOutputText) findComponentInRoot("lblModelo1");
		}
		return lblModelo1;
	}

	protected HtmlOutputText getLblModelo2() {
		if (lblModelo2 == null) {
			lblModelo2 = (HtmlOutputText) findComponentInRoot("lblModelo2");
		}
		return lblModelo2;
	}

	protected HtmlOutputText getLblDescripcion1() {
		if (lblDescripcion1 == null) {
			lblDescripcion1 = (HtmlOutputText) findComponentInRoot("lblDescripcion1");
		}
		return lblDescripcion1;
	}

	protected HtmlOutputText getLblDescripcion2() {
		if (lblDescripcion2 == null) {
			lblDescripcion2 = (HtmlOutputText) findComponentInRoot("lblDescripcion2");
		}
		return lblDescripcion2;
	}

	protected HtmlOutputText getLblCant1() {
		if (lblCant1 == null) {
			lblCant1 = (HtmlOutputText) findComponentInRoot("lblCant1");
		}
		return lblCant1;
	}

	protected HtmlOutputText getLblCant2() {
		if (lblCant2 == null) {
			lblCant2 = (HtmlOutputText) findComponentInRoot("lblCant2");
		}
		return lblCant2;
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

	protected HtmlGridView getGvDetallesol() {
		if (gvDetallesol == null) {
			gvDetallesol = (HtmlGridView) findComponentInRoot("gvDetallesol");
		}
		return gvDetallesol;
	}

	protected HtmlColumn getCoNocuota() {
		if (coNocuota == null) {
			coNocuota = (HtmlColumn) findComponentInRoot("coNocuota");
		}
		return coNocuota;
	}

	protected HtmlOutputText getLblnocuota1() {
		if (lblnocuota1 == null) {
			lblnocuota1 = (HtmlOutputText) findComponentInRoot("lblnocuota1");
		}
		return lblnocuota1;
	}

	protected HtmlOutputText getLblnocuota2() {
		if (lblnocuota2 == null) {
			lblnocuota2 = (HtmlOutputText) findComponentInRoot("lblnocuota2");
		}
		return lblnocuota2;
	}

	protected HtmlOutputText getLblFechaVenc1() {
		if (lblFechaVenc1 == null) {
			lblFechaVenc1 = (HtmlOutputText) findComponentInRoot("lblFechaVenc1");
		}
		return lblFechaVenc1;
	}

	protected HtmlOutputText getLblFechaVenc2() {
		if (lblFechaVenc2 == null) {
			lblFechaVenc2 = (HtmlOutputText) findComponentInRoot("lblFechaVenc2");
		}
		return lblFechaVenc2;
	}

	protected HtmlOutputText getLblPrincipal1() {
		if (lblPrincipal1 == null) {
			lblPrincipal1 = (HtmlOutputText) findComponentInRoot("lblPrincipal1");
		}
		return lblPrincipal1;
	}

	protected HtmlOutputText getLblPrincipal2() {
		if (lblPrincipal2 == null) {
			lblPrincipal2 = (HtmlOutputText) findComponentInRoot("lblPrincipal2");
		}
		return lblPrincipal2;
	}

	protected HtmlOutputText getLblInteres1() {
		if (lblInteres1 == null) {
			lblInteres1 = (HtmlOutputText) findComponentInRoot("lblInteres1");
		}
		return lblInteres1;
	}

	protected HtmlOutputText getLblInteres2() {
		if (lblInteres2 == null) {
			lblInteres2 = (HtmlOutputText) findComponentInRoot("lblInteres2");
		}
		return lblInteres2;
	}

	protected HtmlOutputText getLblImpuesto1() {
		if (lblImpuesto1 == null) {
			lblImpuesto1 = (HtmlOutputText) findComponentInRoot("lblImpuesto1");
		}
		return lblImpuesto1;
	}

	protected HtmlOutputText getLblImpuesto2() {
		if (lblImpuesto2 == null) {
			lblImpuesto2 = (HtmlOutputText) findComponentInRoot("lblImpuesto2");
		}
		return lblImpuesto2;
	}

	protected HtmlOutputText getLblMonto2() {
		if (lblMonto2 == null) {
			lblMonto2 = (HtmlOutputText) findComponentInRoot("lblMonto2");
		}
		return lblMonto2;
	}

	protected HtmlOutputText getLblMoraDet1() {
		if (lblMoraDet1 == null) {
			lblMoraDet1 = (HtmlOutputText) findComponentInRoot("lblMoraDet1");
		}
		return lblMoraDet1;
	}

	protected HtmlOutputText getLblMoraDet2() {
		if (lblMoraDet2 == null) {
			lblMoraDet2 = (HtmlOutputText) findComponentInRoot("lblMoraDet2");
		}
		return lblMoraDet2;
	}

	protected HtmlOutputText getLblmontopend1() {
		if (lblmontopend1 == null) {
			lblmontopend1 = (HtmlOutputText) findComponentInRoot("lblmontopend1");
		}
		return lblmontopend1;
	}

	protected HtmlOutputText getLblMontopend2() {
		if (lblMontopend2 == null) {
			lblMontopend2 = (HtmlOutputText) findComponentInRoot("lblMontopend2");
		}
		return lblMontopend2;
	}

	protected HtmlOutputText getLblPrincipalDet() {
		if (lblPrincipalDet == null) {
			lblPrincipalDet = (HtmlOutputText) findComponentInRoot("lblPrincipalDet");
		}
		return lblPrincipalDet;
	}

	protected HtmlOutputText getTxtPrincipalDet() {
		if (txtPrincipalDet == null) {
			txtPrincipalDet = (HtmlOutputText) findComponentInRoot("txtPrincipalDet");
		}
		return txtPrincipalDet;
	}

	protected HtmlOutputText getLblInteresDet() {
		if (lblInteresDet == null) {
			lblInteresDet = (HtmlOutputText) findComponentInRoot("lblInteresDet");
		}
		return lblInteresDet;
	}

	protected HtmlOutputText getTxtInteresDet() {
		if (txtInteresDet == null) {
			txtInteresDet = (HtmlOutputText) findComponentInRoot("txtInteresDet");
		}
		return txtInteresDet;
	}

	protected HtmlOutputText getLblImpuestoDet() {
		if (lblImpuestoDet == null) {
			lblImpuestoDet = (HtmlOutputText) findComponentInRoot("lblImpuestoDet");
		}
		return lblImpuestoDet;
	}

	protected HtmlOutputText getTxtImpuestoDet() {
		if (txtImpuestoDet == null) {
			txtImpuestoDet = (HtmlOutputText) findComponentInRoot("txtImpuestoDet");
		}
		return txtImpuestoDet;
	}

	protected HtmlOutputText getLblMoraDet22() {
		if (lblMoraDet22 == null) {
			lblMoraDet22 = (HtmlOutputText) findComponentInRoot("lblMoraDet22");
		}
		return lblMoraDet22;
	}

	protected HtmlOutputText getTxtMoraDet22() {
		if (txtMoraDet22 == null) {
			txtMoraDet22 = (HtmlOutputText) findComponentInRoot("txtMoraDet22");
		}
		return txtMoraDet22;
	}

	protected HtmlOutputText getLblTotalDet22() {
		if (lblTotalDet22 == null) {
			lblTotalDet22 = (HtmlOutputText) findComponentInRoot("lblTotalDet22");
		}
		return lblTotalDet22;
	}

	protected HtmlOutputText getTxtTotalDet22() {
		if (txtTotalDet22 == null) {
			txtTotalDet22 = (HtmlOutputText) findComponentInRoot("txtTotalDet22");
		}
		return txtTotalDet22;
	}

	protected HtmlOutputText getLblPendienteDet22() {
		if (lblPendienteDet22 == null) {
			lblPendienteDet22 = (HtmlOutputText) findComponentInRoot("lblPendienteDet22");
		}
		return lblPendienteDet22;
	}

	protected HtmlOutputText getTxtPendienteDet22() {
		if (txtPendienteDet22 == null) {
			txtPendienteDet22 = (HtmlOutputText) findComponentInRoot("txtPendienteDet22");
		}
		return txtPendienteDet22;
	}

	protected HtmlLink getLnkCerrarDetalleContado() {
		if (lnkCerrarDetalleContado == null) {
			lnkCerrarDetalleContado = (HtmlLink) findComponentInRoot("lnkCerrarDetalleContado");
		}
		return lnkCerrarDetalleContado;
	}

	protected HtmlDialogWindowHeader getHdReciboFinan() {
		if (hdReciboFinan == null) {
			hdReciboFinan = (HtmlDialogWindowHeader) findComponentInRoot("hdReciboFinan");
		}
		return hdReciboFinan;
	}

	protected HtmlJspPanel getJspReciboFinan() {
		if (jspReciboFinan == null) {
			jspReciboFinan = (HtmlJspPanel) findComponentInRoot("jspReciboFinan");
		}
		return jspReciboFinan;
	}

	protected HtmlOutputText getLblFechaReciboFinan() {
		if (lblFechaReciboFinan == null) {
			lblFechaReciboFinan = (HtmlOutputText) findComponentInRoot("lblFechaReciboFinan");
		}
		return lblFechaReciboFinan;
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

	protected HtmlDateChooserClientEvents getCleReciboContado() {
		if (cleReciboContado == null) {
			cleReciboContado = (HtmlDateChooserClientEvents) findComponentInRoot("cleReciboContado");
		}
		return cleReciboContado;
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

	protected HtmlGridView getGvFacsSel() {
		if (gvFacsSel == null) {
			gvFacsSel = (HtmlGridView) findComponentInRoot("gvFacsSel");
		}
		return gvFacsSel;
	}

	protected HtmlColumn getCoDetalleFacturaRecibo() {
		if (coDetalleFacturaRecibo == null) {
			coDetalleFacturaRecibo = (HtmlColumn) findComponentInRoot("coDetalleFacturaRecibo");
		}
		return coDetalleFacturaRecibo;
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

	protected HtmlOutputText getLblTipofactura1Detalle() {
		if (lblTipofactura1Detalle == null) {
			lblTipofactura1Detalle = (HtmlOutputText) findComponentInRoot("lblTipofactura1Detalle");
		}
		return lblTipofactura1Detalle;
	}

	protected HtmlOutputText getLblTipofactura2Detalle() {
		if (lblTipofactura2Detalle == null) {
			lblTipofactura2Detalle = (HtmlOutputText) findComponentInRoot("lblTipofactura2Detalle");
		}
		return lblTipofactura2Detalle;
	}

	protected HtmlOutputText getLblCuotafaTipoctura1Detalle() {
		if (lblCuotafaTipoctura1Detalle == null) {
			lblCuotafaTipoctura1Detalle = (HtmlOutputText) findComponentInRoot("lblCuotafaTipoctura1Detalle");
		}
		return lblCuotafaTipoctura1Detalle;
	}

	protected HtmlOutputText getLblCuotaTipofactura2Detalle() {
		if (lblCuotaTipofactura2Detalle == null) {
			lblCuotaTipofactura2Detalle = (HtmlOutputText) findComponentInRoot("lblCuotaTipofactura2Detalle");
		}
		return lblCuotaTipofactura2Detalle;
	}

	protected HtmlOutputText getLblCuotafactura1Detalle() {
		if (lblCuotafactura1Detalle == null) {
			lblCuotafactura1Detalle = (HtmlOutputText) findComponentInRoot("lblCuotafactura1Detalle");
		}
		return lblCuotafactura1Detalle;
	}

	protected HtmlOutputText getLblCuotafactura2Detalle() {
		if (lblCuotafactura2Detalle == null) {
			lblCuotafactura2Detalle = (HtmlOutputText) findComponentInRoot("lblCuotafactura2Detalle");
		}
		return lblCuotafactura2Detalle;
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

	protected HtmlOutputText getLblTotalDetalleReciboA2() {
		if (lblTotalDetalleReciboA2 == null) {
			lblTotalDetalleReciboA2 = (HtmlOutputText) findComponentInRoot("lblTotalDetalleReciboA2");
		}
		return lblTotalDetalleReciboA2;
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

	protected HtmlOutputText getLblMontoRecibido() {
		if (lblMontoRecibido == null) {
			lblMontoRecibido = (HtmlOutputText) findComponentInRoot("lblMontoRecibido");
		}
		return lblMontoRecibido;
	}

	protected HtmlOutputText getTxtMontoRecibido() {
		if (txtMontoRecibido == null) {
			txtMontoRecibido = (HtmlOutputText) findComponentInRoot("txtMontoRecibido");
		}
		return txtMontoRecibido;
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

	protected HtmlDialogWindowHeader getHdMensajeError() {
		if (hdMensajeError == null) {
			hdMensajeError = (HtmlDialogWindowHeader) findComponentInRoot("hdMensajeError");
		}
		return hdMensajeError;
	}

	protected HtmlPanelGrid getGrdMensajeError() {
		if (grdMensajeError == null) {
			grdMensajeError = (HtmlPanelGrid) findComponentInRoot("grdMensajeError");
		}
		return grdMensajeError;
	}

	protected HtmlGraphicImageEx getImgMensajeError() {
		if (imgMensajeError == null) {
			imgMensajeError = (HtmlGraphicImageEx) findComponentInRoot("imgMensajeError");
		}
		return imgMensajeError;
	}

	protected HtmlLink getLnkMensajeError() {
		if (lnkMensajeError == null) {
			lnkMensajeError = (HtmlLink) findComponentInRoot("lnkMensajeError");
		}
		return lnkMensajeError;
	}

	protected HtmlDialogWindowHeader getHdSolicitarAutorizacion() {
		if (hdSolicitarAutorizacion == null) {
			hdSolicitarAutorizacion = (HtmlDialogWindowHeader) findComponentInRoot("hdSolicitarAutorizacion");
		}
		return hdSolicitarAutorizacion;
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

	protected HtmlDialogWindowHeader getHdDetalleCuota() {
		if (hdDetalleCuota == null) {
			hdDetalleCuota = (HtmlDialogWindowHeader) findComponentInRoot("hdDetalleCuota");
		}
		return hdDetalleCuota;
	}

	protected HtmlJspPanel getJspDetalleCuota() {
		if (jspDetalleCuota == null) {
			jspDetalleCuota = (HtmlJspPanel) findComponentInRoot("jspDetalleCuota");
		}
		return jspDetalleCuota;
	}

	protected HtmlOutputText getLblFechaCuotaDet() {
		if (lblFechaCuotaDet == null) {
			lblFechaCuotaDet = (HtmlOutputText) findComponentInRoot("lblFechaCuotaDet");
		}
		return lblFechaCuotaDet;
	}

	protected HtmlOutputText getLblNoCuota23() {
		if (lblNoCuota23 == null) {
			lblNoCuota23 = (HtmlOutputText) findComponentInRoot("lblNoCuota23");
		}
		return lblNoCuota23;
	}

	protected HtmlOutputText getLblCompaniaCuotaDet() {
		if (lblCompaniaCuotaDet == null) {
			lblCompaniaCuotaDet = (HtmlOutputText) findComponentInRoot("lblCompaniaCuotaDet");
		}
		return lblCompaniaCuotaDet;
	}

	protected HtmlOutputText getLblMonedaCuotaDet() {
		if (lblMonedaCuotaDet == null) {
			lblMonedaCuotaDet = (HtmlOutputText) findComponentInRoot("lblMonedaCuotaDet");
		}
		return lblMonedaCuotaDet;
	}

	protected HtmlOutputText getLblSucursalCuotaDet() {
		if (lblSucursalCuotaDet == null) {
			lblSucursalCuotaDet = (HtmlOutputText) findComponentInRoot("lblSucursalCuotaDet");
		}
		return lblSucursalCuotaDet;
	}

	protected HtmlOutputText getLblTasaCuotaDet() {
		if (lblTasaCuotaDet == null) {
			lblTasaCuotaDet = (HtmlOutputText) findComponentInRoot("lblTasaCuotaDet");
		}
		return lblTasaCuotaDet;
	}

	protected HtmlOutputText getLblprincipalCuotaDet() {
		if (lblprincipalCuotaDet == null) {
			lblprincipalCuotaDet = (HtmlOutputText) findComponentInRoot("lblprincipalCuotaDet");
		}
		return lblprincipalCuotaDet;
	}

	protected HtmlOutputText getTxtprincipalCuotaDet() {
		if (txtprincipalCuotaDet == null) {
			txtprincipalCuotaDet = (HtmlOutputText) findComponentInRoot("txtprincipalCuotaDet");
		}
		return txtprincipalCuotaDet;
	}

	protected HtmlOutputText getLblinteresCuotaDet() {
		if (lblinteresCuotaDet == null) {
			lblinteresCuotaDet = (HtmlOutputText) findComponentInRoot("lblinteresCuotaDet");
		}
		return lblinteresCuotaDet;
	}

	protected HtmlOutputText getTxtinteresCuotaDet() {
		if (txtinteresCuotaDet == null) {
			txtinteresCuotaDet = (HtmlOutputText) findComponentInRoot("txtinteresCuotaDet");
		}
		return txtinteresCuotaDet;
	}

	protected HtmlOutputText getLblimpuestoCuotaDet() {
		if (lblimpuestoCuotaDet == null) {
			lblimpuestoCuotaDet = (HtmlOutputText) findComponentInRoot("lblimpuestoCuotaDet");
		}
		return lblimpuestoCuotaDet;
	}

	protected HtmlOutputText getTxtimpuestoCuotaDet() {
		if (txtimpuestoCuotaDet == null) {
			txtimpuestoCuotaDet = (HtmlOutputText) findComponentInRoot("txtimpuestoCuotaDet");
		}
		return txtimpuestoCuotaDet;
	}

	protected HtmlOutputText getLbltotalCuotaDet() {
		if (lbltotalCuotaDet == null) {
			lbltotalCuotaDet = (HtmlOutputText) findComponentInRoot("lbltotalCuotaDet");
		}
		return lbltotalCuotaDet;
	}

	protected HtmlOutputText getTxttotalCuotaDet() {
		if (txttotalCuotaDet == null) {
			txttotalCuotaDet = (HtmlOutputText) findComponentInRoot("txttotalCuotaDet");
		}
		return txttotalCuotaDet;
	}

	protected HtmlOutputText getLblpendienteCuotaDet() {
		if (lblpendienteCuotaDet == null) {
			lblpendienteCuotaDet = (HtmlOutputText) findComponentInRoot("lblpendienteCuotaDet");
		}
		return lblpendienteCuotaDet;
	}

	protected HtmlOutputText getTxtpendienteCuotaDet() {
		if (txtpendienteCuotaDet == null) {
			txtpendienteCuotaDet = (HtmlOutputText) findComponentInRoot("txtpendienteCuotaDet");
		}
		return txtpendienteCuotaDet;
	}

	protected HtmlOutputText getLbldiasVenCuotaDet() {
		if (lbldiasVenCuotaDet == null) {
			lbldiasVenCuotaDet = (HtmlOutputText) findComponentInRoot("lbldiasVenCuotaDet");
		}
		return lbldiasVenCuotaDet;
	}

	protected HtmlOutputText getTxtdiasVenCuotaDet() {
		if (txtdiasVenCuotaDet == null) {
			txtdiasVenCuotaDet = (HtmlOutputText) findComponentInRoot("txtdiasVenCuotaDet");
		}
		return txtdiasVenCuotaDet;
	}

	protected HtmlOutputText getLbldiasAcumCuotaDet() {
		if (lbldiasAcumCuotaDet == null) {
			lbldiasAcumCuotaDet = (HtmlOutputText) findComponentInRoot("lbldiasAcumCuotaDet");
		}
		return lbldiasAcumCuotaDet;
	}

	protected HtmlOutputText getTxtdiasAcumCuotaDet() {
		if (txtdiasAcumCuotaDet == null) {
			txtdiasAcumCuotaDet = (HtmlOutputText) findComponentInRoot("txtdiasAcumCuotaDet");
		}
		return txtdiasAcumCuotaDet;
	}

	protected HtmlOutputText getLbltotalDiasCuotaDet() {
		if (lbltotalDiasCuotaDet == null) {
			lbltotalDiasCuotaDet = (HtmlOutputText) findComponentInRoot("lbltotalDiasCuotaDet");
		}
		return lbltotalDiasCuotaDet;
	}

	protected HtmlOutputText getTxttotalDiasCuotaDet() {
		if (txttotalDiasCuotaDet == null) {
			txttotalDiasCuotaDet = (HtmlOutputText) findComponentInRoot("txttotalDiasCuotaDet");
		}
		return txttotalDiasCuotaDet;
	}

	protected HtmlOutputText getLblmoraCuotaDet() {
		if (lblmoraCuotaDet == null) {
			lblmoraCuotaDet = (HtmlOutputText) findComponentInRoot("lblmoraCuotaDet");
		}
		return lblmoraCuotaDet;
	}

	protected HtmlOutputText getTxtmoraCuotaDet() {
		if (txtmoraCuotaDet == null) {
			txtmoraCuotaDet = (HtmlOutputText) findComponentInRoot("txtmoraCuotaDet");
		}
		return txtmoraCuotaDet;
	}

	protected HtmlLink getLnkCerrarDetalleCuota() {
		if (lnkCerrarDetalleCuota == null) {
			lnkCerrarDetalleCuota = (HtmlLink) findComponentInRoot("lnkCerrarDetalleCuota");
		}
		return lnkCerrarDetalleCuota;
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

	protected HtmlGridView getGvAgregarFactura() {
		if (gvAgregarFactura == null) {
			gvAgregarFactura = (HtmlGridView) findComponentInRoot("gvAgregarFactura");
		}
		return gvAgregarFactura;
	}

	protected HtmlColumn getCoDetalleAgregarFactura() {
		if (coDetalleAgregarFactura == null) {
			coDetalleAgregarFactura = (HtmlColumn) findComponentInRoot("coDetalleAgregarFactura");
		}
		return coDetalleAgregarFactura;
	}

	protected HtmlLink getLnkDetalleAgregarFactura() {
		if (lnkDetalleAgregarFactura == null) {
			lnkDetalleAgregarFactura = (HtmlLink) findComponentInRoot("lnkDetalleAgregarFactura");
		}
		return lnkDetalleAgregarFactura;
	}

	protected HtmlOutputText getLblDetalleAgregarFactura1() {
		if (lblDetalleAgregarFactura1 == null) {
			lblDetalleAgregarFactura1 = (HtmlOutputText) findComponentInRoot("lblDetalleAgregarFactura1");
		}
		return lblDetalleAgregarFactura1;
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

	protected HtmlLink getLnkAceptarAgregarFatura() {
		if (lnkAceptarAgregarFatura == null) {
			lnkAceptarAgregarFatura = (HtmlLink) findComponentInRoot("lnkAceptarAgregarFatura");
		}
		return lnkAceptarAgregarFatura;
	}

	protected HtmlDialogWindowHeader getHdProcesaRecibo() {
		if (hdProcesaRecibo == null) {
			hdProcesaRecibo = (HtmlDialogWindowHeader) findComponentInRoot("hdProcesaRecibo");
		}
		return hdProcesaRecibo;
	}

	protected HtmlPanelGrid getGridProcesaRecibo() {
		if (gridProcesaRecibo == null) {
			gridProcesaRecibo = (HtmlPanelGrid) findComponentInRoot("gridProcesaRecibo");
		}
		return gridProcesaRecibo;
	}

	protected HtmlGraphicImageEx getImageEx2ProcesaRecibo() {
		if (imageEx2ProcesaRecibo == null) {
			imageEx2ProcesaRecibo = (HtmlGraphicImageEx) findComponentInRoot("imageEx2ProcesaRecibo");
		}
		return imageEx2ProcesaRecibo;
	}

	protected HtmlDialogWindowHeader getHdAskCancel() {
		if (hdAskCancel == null) {
			hdAskCancel = (HtmlDialogWindowHeader) findComponentInRoot("hdAskCancel");
		}
		return hdAskCancel;
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

	protected UINamingContainer getVfFinan() {
		if (vfFinan == null) {
			vfFinan = (UINamingContainer) findComponentInRoot("vfFinan");
		}
		return vfFinan;
	}

	protected HtmlScriptCollector getScFinan() {
		if (scFinan == null) {
			scFinan = (HtmlScriptCollector) findComponentInRoot("scFinan");
		}
		return scFinan;
	}

	protected HtmlOutputText getLblTituloContado80() {
		if (lblTituloContado80 == null) {
			lblTituloContado80 = (HtmlOutputText) findComponentInRoot("lblTituloContado80");
		}
		return lblTituloContado80;
	}

	protected HtmlOutputText getTxtBusquedaContado() {
		if (txtBusquedaContado == null) {
			txtBusquedaContado = (HtmlOutputText) findComponentInRoot("txtBusquedaContado");
		}
		return txtBusquedaContado;
	}

	protected HtmlOutputText getTxtBusquedaContado2() {
		if (txtBusquedaContado2 == null) {
			txtBusquedaContado2 = (HtmlOutputText) findComponentInRoot("txtBusquedaContado2");
		}
		return txtBusquedaContado2;
	}

	protected HtmlJspPanel getJspPanel100() {
		if (jspPanel100 == null) {
			jspPanel100 = (HtmlJspPanel) findComponentInRoot("jspPanel100");
		}
		return jspPanel100;
	}

	protected HtmlColumnSelectRow getColumnSelectRowRenderer1() {
		if (columnSelectRowRenderer1 == null) {
			columnSelectRowRenderer1 = (HtmlColumnSelectRow) findComponentInRoot("columnSelectRowRenderer1");
		}
		return columnSelectRowRenderer1;
	}

	protected HtmlColumn getCoLnkDetalle() {
		if (coLnkDetalle == null) {
			coLnkDetalle = (HtmlColumn) findComponentInRoot("coLnkDetalle");
		}
		return coLnkDetalle;
	}

	protected HtmlColumn getCoNoSolicitud() {
		if (coNoSolicitud == null) {
			coNoSolicitud = (HtmlColumn) findComponentInRoot("coNoSolicitud");
		}
		return coNoSolicitud;
	}

	protected HtmlColumn getCoTipoFactura() {
		if (coTipoFactura == null) {
			coTipoFactura = (HtmlColumn) findComponentInRoot("coTipoFactura");
		}
		return coTipoFactura;
	}

	protected HtmlColumn getCoNomcli() {
		if (coNomcli == null) {
			coNomcli = (HtmlColumn) findComponentInRoot("coNomcli");
		}
		return coNomcli;
	}

	protected HtmlColumn getCoUniNeg() {
		if (coUniNeg == null) {
			coUniNeg = (HtmlColumn) findComponentInRoot("coUniNeg");
		}
		return coUniNeg;
	}

	protected HtmlColumn getCoTotal() {
		if (coTotal == null) {
			coTotal = (HtmlColumn) findComponentInRoot("coTotal");
		}
		return coTotal;
	}

	protected HtmlColumn getCoMoneda() {
		if (coMoneda == null) {
			coMoneda = (HtmlColumn) findComponentInRoot("coMoneda");
		}
		return coMoneda;
	}

	protected HtmlColumn getCoFechaVence() {
		if (coFechaVence == null) {
			coFechaVence = (HtmlColumn) findComponentInRoot("coFechaVence");
		}
		return coFechaVence;
	}

	protected HtmlDropDownList getCmbFiltroMonedas() {
		if (cmbFiltroMonedas == null) {
			cmbFiltroMonedas = (HtmlDropDownList) findComponentInRoot("cmbFiltroMonedas");
		}
		return cmbFiltroMonedas;
	}

	protected HtmlOutputText getLblFiltroFactura() {
		if (lblFiltroFactura == null) {
			lblFiltroFactura = (HtmlOutputText) findComponentInRoot("lblFiltroFactura");
		}
		return lblFiltroFactura;
	}

	protected HtmlDropDownList getCmbFiltroFacturas() {
		if (cmbFiltroFacturas == null) {
			cmbFiltroFacturas = (HtmlDropDownList) findComponentInRoot("cmbFiltroFacturas");
		}
		return cmbFiltroFacturas;
	}

	protected HtmlDialogWindow getDwDetalleSol() {
		if (dwDetalleSol == null) {
			dwDetalleSol = (HtmlDialogWindow) findComponentInRoot("dwDetalleSol");
		}
		return dwDetalleSol;
	}

	protected HtmlDialogWindowClientEvents getClDetalleSolicitud() {
		if (clDetalleSolicitud == null) {
			clDetalleSolicitud = (HtmlDialogWindowClientEvents) findComponentInRoot("clDetalleSolicitud");
		}
		return clDetalleSolicitud;
	}

	protected HtmlDialogWindowRoundedCorners getCrDetalleSolicitud() {
		if (crDetalleSolicitud == null) {
			crDetalleSolicitud = (HtmlDialogWindowRoundedCorners) findComponentInRoot("crDetalleSolicitud");
		}
		return crDetalleSolicitud;
	}

	protected HtmlDialogWindowContentPane getCnpDetalleSolicitud() {
		if (cnpDetalleSolicitud == null) {
			cnpDetalleSolicitud = (HtmlDialogWindowContentPane) findComponentInRoot("cnpDetalleSolicitud");
		}
		return cnpDetalleSolicitud;
	}

	protected HtmlOutputText getTxtFechaFactura() {
		if (txtFechaFactura == null) {
			txtFechaFactura = (HtmlOutputText) findComponentInRoot("txtFechaFactura");
		}
		return txtFechaFactura;
	}

	protected HtmlOutputText getTxtNosolDet() {
		if (txtNosolDet == null) {
			txtNosolDet = (HtmlOutputText) findComponentInRoot("txtNosolDet");
		}
		return txtNosolDet;
	}

	protected HtmlOutputText getLblTiposol23() {
		if (lblTiposol23 == null) {
			lblTiposol23 = (HtmlOutputText) findComponentInRoot("lblTiposol23");
		}
		return lblTiposol23;
	}

	protected HtmlOutputText getTxtTiposolDet() {
		if (txtTiposolDet == null) {
			txtTiposolDet = (HtmlOutputText) findComponentInRoot("txtTiposolDet");
		}
		return txtTiposolDet;
	}

	protected HtmlOutputText getTxtCodigoCliente() {
		if (txtCodigoCliente == null) {
			txtCodigoCliente = (HtmlOutputText) findComponentInRoot("txtCodigoCliente");
		}
		return txtCodigoCliente;
	}

	protected HtmlOutputText getTxtMonedaDetalleSol1() {
		if (txtMonedaDetalleSol1 == null) {
			txtMonedaDetalleSol1 = (HtmlOutputText) findComponentInRoot("txtMonedaDetalleSol1");
		}
		return txtMonedaDetalleSol1;
	}

	protected HtmlDropDownList getDdlMonedaDet() {
		if (ddlMonedaDet == null) {
			ddlMonedaDet = (HtmlDropDownList) findComponentInRoot("ddlMonedaDet");
		}
		return ddlMonedaDet;
	}

	protected HtmlOutputText getText3333() {
		if (text3333 == null) {
			text3333 = (HtmlOutputText) findComponentInRoot("text3333");
		}
		return text3333;
	}

	protected HtmlColumn getCoDescitemSol() {
		if (coDescitemSol == null) {
			coDescitemSol = (HtmlColumn) findComponentInRoot("coDescitemSol");
		}
		return coDescitemSol;
	}

	protected HtmlColumn getCoMarca() {
		if (coMarca == null) {
			coMarca = (HtmlColumn) findComponentInRoot("coMarca");
		}
		return coMarca;
	}

	protected HtmlColumn getCoModelo() {
		if (coModelo == null) {
			coModelo = (HtmlColumn) findComponentInRoot("coModelo");
		}
		return coModelo;
	}

	protected HtmlColumn getCoDescripcion() {
		if (coDescripcion == null) {
			coDescripcion = (HtmlColumn) findComponentInRoot("coDescripcion");
		}
		return coDescripcion;
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

	protected HtmlColumn getCoFechaVenc() {
		if (coFechaVenc == null) {
			coFechaVenc = (HtmlColumn) findComponentInRoot("coFechaVenc");
		}
		return coFechaVenc;
	}

	protected HtmlColumn getCoPrincipalSol() {
		if (coPrincipalSol == null) {
			coPrincipalSol = (HtmlColumn) findComponentInRoot("coPrincipalSol");
		}
		return coPrincipalSol;
	}

	protected HtmlColumn getCoInteres() {
		if (coInteres == null) {
			coInteres = (HtmlColumn) findComponentInRoot("coInteres");
		}
		return coInteres;
	}

	protected HtmlColumn getCoImpuesto() {
		if (coImpuesto == null) {
			coImpuesto = (HtmlColumn) findComponentInRoot("coImpuesto");
		}
		return coImpuesto;
	}

	protected HtmlColumn getCoMonto() {
		if (coMonto == null) {
			coMonto = (HtmlColumn) findComponentInRoot("coMonto");
		}
		return coMonto;
	}

	protected HtmlColumn getCoMoraDet1() {
		if (coMoraDet1 == null) {
			coMoraDet1 = (HtmlColumn) findComponentInRoot("coMoraDet1");
		}
		return coMoraDet1;
	}

	protected HtmlColumn getCoMontopend() {
		if (coMontopend == null) {
			coMontopend = (HtmlColumn) findComponentInRoot("coMontopend");
		}
		return coMontopend;
	}

	protected HtmlDialogWindowAutoPostBackFlags getApbDetalleSolicitud() {
		if (apbDetalleSolicitud == null) {
			apbDetalleSolicitud = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbDetalleSolicitud");
		}
		return apbDetalleSolicitud;
	}

	protected HtmlDialogWindow getDwRecibo() {
		if (dwRecibo == null) {
			dwRecibo = (HtmlDialogWindow) findComponentInRoot("dwRecibo");
		}
		return dwRecibo;
	}

	protected HtmlDialogWindowClientEvents getCleReciboFinan() {
		if (cleReciboFinan == null) {
			cleReciboFinan = (HtmlDialogWindowClientEvents) findComponentInRoot("cleReciboFinan");
		}
		return cleReciboFinan;
	}

	protected HtmlDialogWindowRoundedCorners getRcReciboFinan() {
		if (rcReciboFinan == null) {
			rcReciboFinan = (HtmlDialogWindowRoundedCorners) findComponentInRoot("rcReciboFinan");
		}
		return rcReciboFinan;
	}

	protected HtmlDialogWindowContentPane getCpReciboFinan() {
		if (cpReciboFinan == null) {
			cpReciboFinan = (HtmlDialogWindowContentPane) findComponentInRoot("cpReciboFinan");
		}
		return cpReciboFinan;
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

	protected HtmlCheckBox getChkImprimir() {
		if (chkImprimir == null) {
			chkImprimir = (HtmlCheckBox) findComponentInRoot("chkImprimir");
		}
		return chkImprimir;
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

	protected HtmlColumn getCoTipoFacturaDtalle() {
		if (coTipoFacturaDtalle == null) {
			coTipoFacturaDtalle = (HtmlColumn) findComponentInRoot("coTipoFacturaDtalle");
		}
		return coTipoFacturaDtalle;
	}

	protected HtmlColumn getCoCuotaTipoFacturaDtalle() {
		if (coCuotaTipoFacturaDtalle == null) {
			coCuotaTipoFacturaDtalle = (HtmlColumn) findComponentInRoot("coCuotaTipoFacturaDtalle");
		}
		return coCuotaTipoFacturaDtalle;
	}

	protected HtmlColumn getCoCuotaFacturaDtalle() {
		if (coCuotaFacturaDtalle == null) {
			coCuotaFacturaDtalle = (HtmlColumn) findComponentInRoot("coCuotaFacturaDtalle");
		}
		return coCuotaFacturaDtalle;
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

	protected HtmlColumn getCoTotalDetalleReciboA() {
		if (coTotalDetalleReciboA == null) {
			coTotalDetalleReciboA = (HtmlColumn) findComponentInRoot("coTotalDetalleReciboA");
		}
		return coTotalDetalleReciboA;
	}

	protected HtmlOutputText getLblTotalDetalleReciboA() {
		if (lblTotalDetalleReciboA == null) {
			lblTotalDetalleReciboA = (HtmlOutputText) findComponentInRoot("lblTotalDetalleReciboA");
		}
		return lblTotalDetalleReciboA;
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

	protected HtmlDialogWindowAutoPostBackFlags getApbReciboFinan100() {
		if (apbReciboFinan100 == null) {
			apbReciboFinan100 = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbReciboFinan100");
		}
		return apbReciboFinan100;
	}

	protected HtmlDialogWindow getDwMensajeError() {
		if (dwMensajeError == null) {
			dwMensajeError = (HtmlDialogWindow) findComponentInRoot("dwMensajeError");
		}
		return dwMensajeError;
	}

	protected HtmlDialogWindowClientEvents getCleMensajeError() {
		if (cleMensajeError == null) {
			cleMensajeError = (HtmlDialogWindowClientEvents) findComponentInRoot("cleMensajeError");
		}
		return cleMensajeError;
	}

	protected HtmlDialogWindowRoundedCorners getRcMensajeError() {
		if (rcMensajeError == null) {
			rcMensajeError = (HtmlDialogWindowRoundedCorners) findComponentInRoot("rcMensajeError");
		}
		return rcMensajeError;
	}

	protected HtmlDialogWindowContentPane getCpMensajeError() {
		if (cpMensajeError == null) {
			cpMensajeError = (HtmlDialogWindowContentPane) findComponentInRoot("cpMensajeError");
		}
		return cpMensajeError;
	}

	protected HtmlJspPanel getJspMensajeError() {
		if (jspMensajeError == null) {
			jspMensajeError = (HtmlJspPanel) findComponentInRoot("jspMensajeError");
		}
		return jspMensajeError;
	}

	protected HtmlDialogWindowAutoPostBackFlags getApbMensajeError() {
		if (apbMensajeError == null) {
			apbMensajeError = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbMensajeError");
		}
		return apbMensajeError;
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

	protected HtmlJspPanel getJspPanel23() {
		if (jspPanel23 == null) {
			jspPanel23 = (HtmlJspPanel) findComponentInRoot("jspPanel23");
		}
		return jspPanel23;
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

	protected HtmlDialogWindow getDwDetalleCuota() {
		if (dwDetalleCuota == null) {
			dwDetalleCuota = (HtmlDialogWindow) findComponentInRoot("dwDetalleCuota");
		}
		return dwDetalleCuota;
	}

	protected HtmlDialogWindowClientEvents getClDetalleCuota() {
		if (clDetalleCuota == null) {
			clDetalleCuota = (HtmlDialogWindowClientEvents) findComponentInRoot("clDetalleCuota");
		}
		return clDetalleCuota;
	}

	protected HtmlDialogWindowRoundedCorners getCrDetalleCuota() {
		if (crDetalleCuota == null) {
			crDetalleCuota = (HtmlDialogWindowRoundedCorners) findComponentInRoot("crDetalleCuota");
		}
		return crDetalleCuota;
	}

	protected HtmlDialogWindowContentPane getCnpDetalleCuota() {
		if (cnpDetalleCuota == null) {
			cnpDetalleCuota = (HtmlDialogWindowContentPane) findComponentInRoot("cnpDetalleCuota");
		}
		return cnpDetalleCuota;
	}

	protected HtmlOutputText getTxtFechaCuotaDet() {
		if (txtFechaCuotaDet == null) {
			txtFechaCuotaDet = (HtmlOutputText) findComponentInRoot("txtFechaCuotaDet");
		}
		return txtFechaCuotaDet;
	}

	protected HtmlOutputText getTxtNoCuotaDet() {
		if (txtNoCuotaDet == null) {
			txtNoCuotaDet = (HtmlOutputText) findComponentInRoot("txtNoCuotaDet");
		}
		return txtNoCuotaDet;
	}

	protected HtmlOutputText getTxtCompaniaDetalleCuota1() {
		if (txtCompaniaDetalleCuota1 == null) {
			txtCompaniaDetalleCuota1 = (HtmlOutputText) findComponentInRoot("txtCompaniaDetalleCuota1");
		}
		return txtCompaniaDetalleCuota1;
	}

	protected HtmlOutputText getTxtMonedaDetalleCuota1() {
		if (txtMonedaDetalleCuota1 == null) {
			txtMonedaDetalleCuota1 = (HtmlOutputText) findComponentInRoot("txtMonedaDetalleCuota1");
		}
		return txtMonedaDetalleCuota1;
	}

	protected HtmlOutputText getTxtSucursalDetalleCuota1() {
		if (txtSucursalDetalleCuota1 == null) {
			txtSucursalDetalleCuota1 = (HtmlOutputText) findComponentInRoot("txtSucursalDetalleCuota1");
		}
		return txtSucursalDetalleCuota1;
	}

	protected HtmlOutputText getTxtTasaCuotaDet() {
		if (txtTasaCuotaDet == null) {
			txtTasaCuotaDet = (HtmlOutputText) findComponentInRoot("txtTasaCuotaDet");
		}
		return txtTasaCuotaDet;
	}

	protected HtmlDialogWindowAutoPostBackFlags getApbDetalleCuota() {
		if (apbDetalleCuota == null) {
			apbDetalleCuota = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbDetalleCuota");
		}
		return apbDetalleCuota;
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

	protected HtmlColumn getCoNofacturaAgregarFactura() {
		if (coNofacturaAgregarFactura == null) {
			coNofacturaAgregarFactura = (HtmlColumn) findComponentInRoot("coNofacturaAgregarFactura");
		}
		return coNofacturaAgregarFactura;
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

	protected HtmlDialogWindowAutoPostBackFlags getApbAgregarFactura() {
		if (apbAgregarFactura == null) {
			apbAgregarFactura = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbAgregarFactura");
		}
		return apbAgregarFactura;
	}

	protected HtmlDialogWindow getDwProcesaRecibo() {
		if (dwProcesaRecibo == null) {
			dwProcesaRecibo = (HtmlDialogWindow) findComponentInRoot("dwProcesaRecibo");
		}
		return dwProcesaRecibo;
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

	protected HtmlOutputText getLblConfirmPrint() {
		if (lblConfirmPrint == null) {
			lblConfirmPrint = (HtmlOutputText) findComponentInRoot("lblConfirmPrint");
		}
		return lblConfirmPrint;
	}

	protected HtmlJspPanel getJspProcesaRecibo() {
		if (jspProcesaRecibo == null) {
			jspProcesaRecibo = (HtmlJspPanel) findComponentInRoot("jspProcesaRecibo");
		}
		return jspProcesaRecibo;
	}

	protected HtmlDialogWindowAutoPostBackFlags getApbProcesaRecibo() {
		if (apbProcesaRecibo == null) {
			apbProcesaRecibo = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbProcesaRecibo");
		}
		return apbProcesaRecibo;
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

	protected HtmlJspPanel getJspPanel3AskCancel() {
		if (jspPanel3AskCancel == null) {
			jspPanel3AskCancel = (HtmlJspPanel) findComponentInRoot("jspPanel3AskCancel");
		}
		return jspPanel3AskCancel;
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

}
/**
 * 
 */
package pagecode.tilesContent;

import pagecode.PageCodeBase;
import javax.faces.component.html.HtmlForm;
import com.infragistics.faces.menu.component.html.HtmlMenu;
import com.infragistics.faces.menu.component.html.HtmlMenuItem;
import javax.faces.component.html.HtmlOutputText;
import com.ibm.faces.component.html.HtmlPanelSection;
import com.ibm.faces.component.html.HtmlJspPanel;
import com.ibm.faces.component.html.HtmlGraphicImageEx;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
import javax.faces.component.html.HtmlInputText;
import com.ibm.faces.component.html.HtmlInputHelperTypeahead;
import com.infragistics.faces.shared.component.html.HtmlLink;
import com.infragistics.faces.grid.component.html.HtmlGridView;
import javax.faces.component.html.HtmlPanelGrid;
import com.infragistics.faces.window.component.html.HtmlDialogWindowHeader;
import com.infragistics.faces.grid.component.html.HtmlColumn;
import com.infragistics.faces.window.component.html.HtmlDialogButtonCloseBox;
import com.infragistics.faces.input.component.html.HtmlCheckBox;
import com.infragistics.faces.input.component.html.HtmlDateChooser;
import javax.faces.component.html.HtmlInputTextarea;
import com.infragistics.faces.input.component.html.HtmlDateChooserClientEvents;
import javax.faces.component.UINamingContainer;
import com.ibm.faces.component.html.HtmlScriptCollector;
import com.infragistics.faces.grid.component.html.HtmlColumnSelectRow;
import com.infragistics.faces.window.component.html.HtmlDialogWindow;
import com.infragistics.faces.window.component.html.HtmlDialogWindowClientEvents;
import com.infragistics.faces.window.component.html.HtmlDialogWindowRoundedCorners;
import com.infragistics.faces.window.component.html.HtmlDialogWindowContentPane;
import com.infragistics.faces.window.component.html.HtmlDialogWindowAutoPostBackFlags;
import com.infragistics.faces.grid.component.html.HtmlGridEditing;
import javax.faces.component.html.HtmlInputSecret;

/**
 * @author Juan Ñamendi
 *
 */
public class PagoFacContado3_bodyarea extends PageCodeBase {

	protected HtmlForm frmContado;
	protected HtmlMenu menu1;
	protected HtmlMenuItem item0;
	protected HtmlMenuItem item1;
	protected HtmlMenuItem item2;
	protected HtmlOutputText lblTitulo1Contado;
	protected HtmlPanelSection secContado1;
	protected HtmlJspPanel jspPanel7;
	protected HtmlGraphicImageEx imageEx4Cont;
	protected HtmlJspPanel jspPanel6;
	protected HtmlGraphicImageEx imageEx3;
	protected HtmlOutputText lblTipoBusqueda;
	protected HtmlDropDownList dropBusqueda;
	protected HtmlInputText txtParametro;
	protected HtmlInputHelperTypeahead tphContado;
	protected HtmlLink lnkSearchContado;
	protected HtmlGraphicImageEx imgLoader;
	protected HtmlOutputText lblResultadoRec;
	protected HtmlGridView gvHfacturasContado;
	protected HtmlOutputText lblHeader;
	protected HtmlOutputText lblSelecctedGrande;
	protected HtmlLink lnkDetalleFacturaContado;
	protected HtmlOutputText lblDetalleFacturaGrande;
	protected HtmlOutputText lblnofactura1Grande;
	protected HtmlOutputText lblnofactura2Grande;
	protected HtmlOutputText lblTipofactura1Grande;
	protected HtmlOutputText lblTipofactura2Grande;
	protected HtmlOutputText lblNomCli1Grande;
	protected HtmlOutputText lblNomCli2Grande;
	protected HtmlOutputText lblUnineg1Grande;
	protected HtmlOutputText lblUnineg2Grande;
	protected HtmlOutputText lblTotal1Grande;
	protected HtmlOutputText lblTotal2Grande;
	protected HtmlOutputText lblMoneda1Grande;
	protected HtmlOutputText lblMoneda2Grande;
	protected HtmlOutputText lblFecha1Grande;
	protected HtmlOutputText lblFecha2Grande;
	protected HtmlLink lnkProcesarRecibo2;
	protected HtmlLink lnkRefrescarVistaContado;
	protected HtmlLink lnkTraslasdar2;
	protected HtmlLink lnkImportar2;
	protected HtmlLink lnkTraslado2;
	protected HtmlPanelGrid grid1;
	protected HtmlOutputText lblFiltroMoneda;
	protected HtmlDialogWindowHeader hdDetalleFactura;
	protected HtmlJspPanel jspPanel4;
	protected HtmlOutputText text18;
	protected HtmlOutputText text20;
	protected HtmlOutputText lblCodigo23;
	protected HtmlOutputText txtMonedaContado1;
	protected HtmlOutputText txtNomCliente;
	protected HtmlOutputText lblTasaDetalleCont;
	protected HtmlOutputText lblUninegDetalleCont;
	protected HtmlOutputText lblVendedorCont;
	protected HtmlGridView gvDetalleFacContado;
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
	protected HtmlOutputText lblSubtotalDetalleContado;
	protected HtmlOutputText txtSubtotalDetalle;
	protected HtmlOutputText text28;
	protected HtmlOutputText txtIvaDetalle;
	protected HtmlOutputText lblTotalDetCont;
	protected HtmlOutputText txtTotalDetalle;
	protected HtmlLink lnkCerrarDetalleContado;
	protected HtmlDialogWindowHeader hdValida;
	protected HtmlJspPanel jspPane20;
	protected HtmlGraphicImageEx imgValida;
	protected HtmlOutputText lblValida;
	protected HtmlLink lnkCerrarValida;
	protected HtmlDialogWindowHeader hdSolicitarAutorizacion;
	protected HtmlDialogButtonCloseBox clbAutoriza;
	protected HtmlOutputText lblMensajeAutorizacion;
	protected HtmlOutputText lblReferencia4;
	protected HtmlLink lnkProcesarSolicitud;
	protected HtmlDialogWindowHeader hdProcesaRecibo;
	protected HtmlPanelGrid grdProces;
	protected HtmlGraphicImageEx imgProcesa;
	protected HtmlLink lnkCerrarPagoMensaje;
	protected HtmlDialogWindowHeader hdImprime;
	protected HtmlPanelGrid grid100;
	protected HtmlGraphicImageEx imageEx2Imprime;
	protected HtmlCheckBox chkImprimir;
	protected HtmlLink lnkCerrarMensaje13;
	protected HtmlDialogWindowHeader hdProcesaDevolucion;
	protected HtmlPanelGrid gridProcesaDevolucion;
	protected HtmlGraphicImageEx imageEx2ProcesaDevolucion;
	protected HtmlCheckBox chkImprimirProcesaDevolucion;
	protected HtmlLink lnkCerrarProcesaDevolucion;
	protected HtmlDialogWindowHeader hdAskCancel;
	protected HtmlPanelGrid gridAskCancel;
	protected HtmlGraphicImageEx imageEx2AskCancel;
	protected HtmlLink lnkCerrarMensajeAskCancel;
	protected HtmlDialogWindowHeader hdMensajeDetalleContado;
	protected HtmlPanelGrid grid4DetalleContado;
	protected HtmlGraphicImageEx imageEx10DetalleContado;
	protected HtmlLink lnkAcptarDetalleContado;
	protected HtmlDialogWindowHeader hdAskCancelDev;
	protected HtmlPanelGrid gridAskCancelDev;
	protected HtmlGraphicImageEx imageEx2AskCancelDev;
	protected HtmlLink lnkCerrarMensajeAskCancelDev;
	protected HtmlDialogWindowHeader hdDevolucion;
	protected HtmlJspPanel jspDevolucion;
	protected HtmlOutputText lblFechaReciboContDev;
	protected HtmlOutputText txtFechaReciboDev;
	protected HtmlDateChooser txtFechaManualDev;
	protected HtmlOutputText lblNum;
	protected HtmlPanelGrid grid5Dev;
	protected HtmlOutputText lblNumeroReciboDev;
	protected HtmlOutputText lblClienteDev;
	protected HtmlOutputText lblCodigoSearchDev;
	protected HtmlOutputText lblTipoReciboDev;
	protected HtmlDropDownList ddlTipoReciboDev;
	protected HtmlLink lnkDetalleFactDev;
	protected HtmlOutputText lblNodoco;
	protected HtmlOutputText lblTipodoco1;
	protected HtmlOutputText lblTipodoco;
	protected HtmlOutputText lblMonedaOdev1;
	protected HtmlOutputText lblMonedaOdev;
	protected HtmlOutputText lblMontoOdev1;
	protected HtmlOutputText lblMontoOdev;
	protected HtmlOutputText lblFechadev1;
	protected HtmlOutputText lblFechadev;
	protected HtmlOutputText lblNoReciboOdev1;
	protected HtmlOutputText lblNoReciboOdev;
	protected HtmlOutputText lblHoraReciboDev1;
	protected HtmlOutputText lblHoraReciboDev;
	protected HtmlGridView gvDetalleReciboOriginal;
	protected HtmlColumn coMetodoOdev;
	protected HtmlOutputText lblMetodoOdev;
	protected HtmlOutputText lblMetodo2Odev;
	protected HtmlOutputText lblMonedaOdev22;
	protected HtmlOutputText lblMoneda22Odev;
	protected HtmlOutputText lblMonto22Odev;
	protected HtmlOutputText lblMonto222Odev;
	protected HtmlOutputText lblTasaOdev;
	protected HtmlOutputText lblTasa2Odev;
	protected HtmlOutputText lblEquivDetalleOdev;
	protected HtmlOutputText lblEquivDetalle2Odev;
	protected HtmlOutputText lblReferencia29Odev;
	protected HtmlOutputText lblReferencia19Odev;
	protected HtmlOutputText lblReferencia222Odev;
	protected HtmlOutputText lblReferencia22Odev;
	protected HtmlOutputText lblReferencia322Odev;
	protected HtmlOutputText lblReferencia32Odev;
	protected HtmlOutputText lblReferencia323Odev;
	protected HtmlOutputText lblReferencia33Odev;
	protected HtmlLink lnkDetalleFactFact;
	protected HtmlOutputText lblNoFactDev;
	protected HtmlOutputText lblTipoDocFactDev1;
	protected HtmlOutputText lblTipoDocFactDev;
	protected HtmlOutputText lblMonedaFactDev1;
	protected HtmlOutputText lblMonedaFactDev;
	protected HtmlOutputText lblMontoFactDev1;
	protected HtmlOutputText lblMontoFactDev;
	protected HtmlOutputText lblMetodosPagoFacDev1;
	protected HtmlDropDownList cmbMetodosPagoFacDev;
	protected HtmlOutputText lblMontoFacDev;
	protected HtmlInputText txtMontoFacDev;
	protected HtmlLink lnkRegistrarPagoFacDev;
	protected HtmlGridView gvDetalleReciboFactDev;
	protected HtmlColumn coEliminarPagoDev;
	protected HtmlLink lnkEliminarDetalleDev;
	protected HtmlOutputText lblEliminarPagoDev;
	protected HtmlOutputText lblMetodoFactDev;
	protected HtmlOutputText lblMetodo2FactDev;
	protected HtmlOutputText lblMonedaFactDev22;
	protected HtmlOutputText lblMoneda22FactDev;
	protected HtmlOutputText lblMonto22FactDev;
	protected HtmlOutputText lblMonto222FactDev;
	protected HtmlOutputText lblTasaFactDev;
	protected HtmlOutputText lblTasa2FactDev;
	protected HtmlOutputText lblEquivDetalleFactDev;
	protected HtmlOutputText lblEquivDetalle2FactDev;
	protected HtmlOutputText lblReferencia29FactDev;
	protected HtmlOutputText lblReferencia19FactDev;
	protected HtmlOutputText lblReferencia222FactDev;
	protected HtmlOutputText lblReferencia22FactDev;
	protected HtmlOutputText lblReferencia322FactDev;
	protected HtmlOutputText lblReferencia32FactDev;
	protected HtmlOutputText lblReferencia323FactDev;
	protected HtmlOutputText lblReferencia33FactDev;
	protected HtmlOutputText lblTasaCambioDev1;
	protected HtmlOutputText lblTasaJDEDev1;
	protected HtmlOutputText lblConceptoDev;
	protected HtmlInputTextarea txtConceptoDev;
	protected HtmlOutputText lblMontoAplicarDev;
	protected HtmlOutputText lblMontoAplicar2Dev;
	protected HtmlOutputText lblMontoProcesado;
	protected HtmlOutputText lblMontoProcesado2;
	protected HtmlOutputText lblFaltante;
	protected HtmlOutputText lblFaltante2;
	protected HtmlLink lnkProcesarReciboFacDev;
	protected HtmlDialogWindowHeader hdReciboContado;
	protected HtmlJspPanel jspReciboCont;
	protected HtmlOutputText lblFechaReciboCont;
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
	protected HtmlDropDownList ddlMetodoPago;
	protected HtmlOutputText lblMonto;
	protected HtmlInputText txtMonto;
	protected HtmlOutputText lblAfiliado;
	protected HtmlOutputText lblReferencia1;
	protected HtmlInputText txtReferencia1;
	protected HtmlOutputText lblBanco;
	protected HtmlDropDownList ddlBanco;
	protected HtmlOutputText lblReferencia2;
	protected HtmlInputText txtReferencia2;
	protected HtmlOutputText lblReferencia3;
	protected HtmlInputText txtReferencia3;
	protected HtmlLink lnkRegistrarPago;
	protected HtmlGridView metodosGrid;
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
	protected HtmlColumn coEliminarFactura;
	protected HtmlLink lnkEliminarFactura;
	protected HtmlOutputText lblEliminarFactura;
	protected HtmlLink lnkDetalleFact;
	protected HtmlOutputText lblDetalleFacturaRecibo;
	protected HtmlOutputText lblNofacturaDetalle;
	protected HtmlOutputText lblNofacturaDetalle2;
	protected HtmlOutputText lblTipofactura1Detalle;
	protected HtmlOutputText lblTipofactura2Detalle;
	protected HtmlOutputText lblFechaDetalleRecibo;
	protected HtmlOutputText lblFechaDetalleRecibo2;
	protected HtmlOutputText lblTotalDetalleRecibo;
	protected HtmlOutputText lblTotalDetalleRecibo2;
	protected HtmlOutputText lblMonedaDetalleRecibo;
	protected HtmlOutputText lblMonedaDetalleRecibo2;
	protected HtmlOutputText lblEquivDetalleRecibo;
	protected HtmlOutputText lblEquivDetalleRecibo2;
	protected HtmlOutputText lblConcepto;
	protected HtmlInputTextarea txtConcepto;
	protected HtmlOutputText lblMontoAplicar;
	protected HtmlOutputText lblMontoAplicar2;
	protected HtmlOutputText lblMontoRecibido;
	protected HtmlOutputText lblMontoRecibido2;
	protected HtmlOutputText lblCambio;
	protected HtmlPanelGrid grCambio;
	protected HtmlInputText txtCambioForaneo;
	protected HtmlOutputText lblPendienteDomestico;
	protected HtmlOutputText txtPendienteDomestico;
	protected HtmlOutputText lblCambioDomestico;
	protected HtmlOutputText txtCambioDomestico;
	protected HtmlLink lnkProcesarRecibo;
	protected HtmlDialogWindowHeader hdConfirmaEmisionCheque;
	protected HtmlJspPanel jspPMensaje;
	protected HtmlOutputText lblEtdwConfirmarsolecheque;
	protected HtmlOutputText lblMsgValidaSolecheque;
	protected HtmlLink lnkSolicitaEmisionCkOk;
	protected HtmlDialogWindowHeader hdCajaTraslado;
	protected HtmlJspPanel jpEnviarFactura;
	protected HtmlPanelGrid pgEnvioFactura1;
	protected HtmlOutputText lblEtFiltroCaja;
	protected UINamingContainer vfContado;
	protected HtmlScriptCollector scContado;
	protected HtmlOutputText lblTituloContado80;
	protected HtmlOutputText txtBusquedaContado;
	protected HtmlOutputText txtBusquedaContado2;
	protected HtmlJspPanel jspPanel100;
	protected HtmlOutputText txtMensaje;
	protected HtmlColumnSelectRow columnSelectRowRenderer1777;
	protected HtmlColumn coLnkDetalle;
	protected HtmlColumn coNoFactura;
	protected HtmlColumn coTipoFactura;
	protected HtmlColumn coNomCli;
	protected HtmlColumn coUniNeg;
	protected HtmlColumn coTotal;
	protected HtmlColumn coMoneda;
	protected HtmlColumn coFecha;
	protected HtmlDropDownList cmbFiltroMonedas;
	protected HtmlOutputText lblFiltroFactura;
	protected HtmlDropDownList cmbFiltroFacturas;
	protected HtmlDialogWindow dgwDetalleFactura;
	protected HtmlDialogWindowClientEvents clDetalleContado;
	protected HtmlDialogWindowRoundedCorners crDetalle;
	protected HtmlDialogWindowContentPane cnpDetalle;
	protected HtmlOutputText txtFechaFactura;
	protected HtmlOutputText txtNoFactura;
	protected HtmlOutputText txtCodigoCliente;
	protected HtmlDropDownList ddlDetalleContado;
	protected HtmlOutputText text3333;
	protected HtmlOutputText txtCodUnineg;
	protected HtmlOutputText text23;
	protected HtmlOutputText txtVendedorCont;
	protected HtmlColumn coDescitemCont;
	protected HtmlColumn coCant;
	protected HtmlColumn coPreciounit;
	protected HtmlColumn coImpuesto;
	protected HtmlDialogWindowAutoPostBackFlags apbDetalle;
	protected HtmlDialogWindow dwValidaContado;
	protected HtmlDialogWindowClientEvents cleValida;
	protected HtmlDialogWindowRoundedCorners rcValida;
	protected HtmlDialogWindowContentPane cpValida;
	protected HtmlDialogWindowAutoPostBackFlags apbReciboContado2;
	protected HtmlDialogWindow dwSolicitud;
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
	protected HtmlDialogWindow dwProcesa;
	protected HtmlDialogWindowClientEvents cleProcesaRecibo;
	protected HtmlDialogWindowRoundedCorners rcProcesaRecibo;
	protected HtmlDialogWindowContentPane cpProcesaRecibo;
	protected HtmlOutputText lblMensajeValidacionPrima;
	protected HtmlJspPanel jspProcesa;
	protected HtmlDialogWindowAutoPostBackFlags apbProcesaRecibo;
	protected HtmlDialogWindow dwImprimeContado;
	protected HtmlDialogWindowClientEvents cleImprime;
	protected HtmlDialogWindowRoundedCorners rcImprime;
	protected HtmlDialogWindowContentPane cpImprime;
	protected HtmlOutputText lblConfirmPrint;
	protected HtmlJspPanel jspPanel3;
	protected HtmlLink lnkCerrarMensaje14;
	protected HtmlDialogWindowAutoPostBackFlags apbImprime;
	protected HtmlDialogWindow dwProcesaDevolucion;
	protected HtmlDialogWindowClientEvents cleProcesaDevolucion;
	protected HtmlDialogWindowRoundedCorners rcProcesaDevolucion;
	protected HtmlDialogWindowContentPane cpProcesaDevolucion;
	protected HtmlOutputText lblConfirmProcesaDevolucion;
	protected HtmlJspPanel jspProcesaDevolucion;
	protected HtmlLink lnkProcesaDevolucion;
	protected HtmlDialogWindowAutoPostBackFlags apbProcesaDevolucion;
	protected HtmlDialogWindow dwAskCancel;
	protected HtmlDialogWindowClientEvents cleAskCancel;
	protected HtmlDialogWindowRoundedCorners rcAskCancel;
	protected HtmlDialogWindowContentPane cpAskCancel;
	protected HtmlOutputText lblConfirmCancel;
	protected HtmlJspPanel jspPanel3AskCancel;
	protected HtmlLink lnkCerrarAskCancel;
	protected HtmlDialogWindowAutoPostBackFlags apbAskCancel;
	protected HtmlDialogWindow dwMensajeDetalleContado;
	protected HtmlDialogWindowClientEvents cleMensajeDetalleContado;
	protected HtmlDialogWindowRoundedCorners rcMensajeDetalleContado;
	protected HtmlDialogWindowContentPane cpMensajeDetalleContado;
	protected HtmlOutputText lblAlertDetalleContado;
	protected HtmlJspPanel jspPanel5DetalleContado;
	protected HtmlDialogWindowAutoPostBackFlags apbMensajeDetalleContado;
	protected HtmlDialogWindow dwCancelarDev;
	protected HtmlDialogWindowClientEvents cleAskCancelDev;
	protected HtmlDialogWindowRoundedCorners rcAskCancelDev;
	protected HtmlDialogWindowContentPane cpAskCancelDev;
	protected HtmlOutputText lblConfirmCancelDev;
	protected HtmlJspPanel jspPanel3AskCancelDev;
	protected HtmlLink lnkCerrarAskCancelDev;
	protected HtmlDialogWindowAutoPostBackFlags apbAskCancelDev;
	protected HtmlDialogWindow dwDevolucion;
	protected HtmlDialogWindowClientEvents cleDevolucion;
	protected HtmlDialogWindowRoundedCorners rcDevolucion;
	protected HtmlDialogWindowContentPane cpDevolucion;
	protected HtmlOutputText lblNumeroReciboManualDev;
	protected HtmlInputText txtNumeroReciboManulDev;
	protected HtmlOutputText lblCodDev;
	protected HtmlOutputText lblNombreSearchDev;
	protected HtmlOutputText lblNomDev;
	protected HtmlOutputText lblNodoco1;
	protected HtmlColumn coMonedaDetalleOdev;
	protected HtmlColumn coMontoOdev;
	protected HtmlColumn coTasaOdev;
	protected HtmlColumn coEquivalenteOdev;
	protected HtmlColumn coReferenciaOdev;
	protected HtmlColumn coReferencia2Odev;
	protected HtmlColumn coReferencia3Odev;
	protected HtmlColumn coReferencia4Odev;
	protected HtmlOutputText lblNoFactDev1;
	protected HtmlOutputText lblCodsucDev;
	protected HtmlOutputText lblCoduninegDev;
	protected HtmlDropDownList cmbMonedaFacDev;
	protected HtmlColumn coMetodoFactDev;
	protected HtmlColumn coMonedaDetalleFactDev;
	protected HtmlColumn coMontoFactDev;
	protected HtmlColumn coTasaFactDev;
	protected HtmlColumn coEquivalenteFactDev;
	protected HtmlColumn coReferenciaFactDev;
	protected HtmlColumn coReferencia2FactDev;
	protected HtmlColumn coReferencia3FactDev;
	protected HtmlColumn coReferencia4FactDev;
	protected HtmlOutputText lblTasaCambioDev;
	protected HtmlOutputText lblTasaJDEDev;
	protected HtmlLink lnkCancelarReciboFacDev;
	protected HtmlDialogWindowAutoPostBackFlags apbDevolucion;
	protected HtmlDialogWindow dwRecibos;
	protected HtmlDialogWindowClientEvents cleReciboConbtado;
	protected HtmlDialogWindowRoundedCorners rcREciboContado;
	protected HtmlDialogWindowContentPane cpReciboContado;
	protected HtmlOutputText lblNumRecm;
	protected HtmlInputText txtNumRec;
	protected HtmlOutputText lblCod;
	protected HtmlOutputText lblNombreSearch;
	protected HtmlOutputText lblNom;
	protected HtmlDropDownList ddlMoneda;
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
	protected HtmlColumn coTipoFacturaDtalle;
	protected HtmlColumn coFechaDetalleRecibo;
	protected HtmlColumn coTotalDetalleRecibo;
	protected HtmlColumn coMonedaDetalleRecibo;
	protected HtmlColumn coEquivDetalleRecibo;
	protected HtmlLink lnkCambio;
	protected HtmlOutputText txtCambio;
	protected HtmlLink lnkCancelarRecibo;
	protected HtmlDialogWindowAutoPostBackFlags apbReciboContado100;
	protected HtmlDialogWindow dwConfirmaEmisionCheque;
	protected HtmlDialogWindowClientEvents cleConfirmaEmisionCheque;
	protected HtmlDialogWindowRoundedCorners rcConfirmaEmisionCheque;
	protected HtmlDialogWindowContentPane cpConfirmaEmisionCheque;
	protected HtmlJspPanel jspPanelSolicitaEmisionCk;
	protected HtmlLink lnkCerrarSolicitaEmisionCk;
	protected HtmlDialogWindowAutoPostBackFlags apbSolicitaEmisionCk;
	protected HtmlDialogWindow dwCajasParaTraslado;
	protected HtmlDialogWindowClientEvents cleCajaTraslado;
	protected HtmlDialogWindowRoundedCorners rcCajaTraslado;
	protected HtmlDialogWindowContentPane cpCajaTraslado;
	protected HtmlDropDownList ddlFiltroCaja;
	protected HtmlInputText txtParamCajaEnvio;
	protected HtmlGridView gvCajasDisponibleEnvio;
	protected HtmlColumn coNumcaja;
	protected HtmlOutputText lblCoNumcaja;
	protected HtmlColumn coNombrecaja;
	protected HtmlOutputText lbletNocaja;
	protected HtmlOutputText lblCoNomcaja;
	protected HtmlOutputText lblEtNomcaja;
	protected HtmlColumn coCodsuc;
	protected HtmlOutputText lblCoCodsuc;
	protected HtmlOutputText lblEtCodsuc;
	protected HtmlColumn coNomsuc;
	protected HtmlColumn coCajero;
	protected HtmlOutputText lblCoNomsuc;
	protected HtmlOutputText lblEtNomsuc;
	protected HtmlOutputText lblCoCajero;
	protected HtmlOutputText lblEtCajero;
	protected HtmlColumn coContador;
	protected HtmlOutputText lblCoContador;
	protected HtmlOutputText lblEtContador;
	protected HtmlLink lnkFiltrarCajasParaEnvio;
	protected HtmlColumnSelectRow csrprueba;
	protected HtmlLink lnkAceptarEnvioFac;
	protected HtmlLink lnkCancelarEnvioFac;
	protected HtmlOutputText lblValidaEnvioFac;
	protected HtmlLink lnkCrearNotaCreditoCkOk;
	protected HtmlDialogWindow dwCrearNotaCredito;
	protected HtmlJspPanel jspdwCrearNotaCreditoMensaje;
	protected HtmlDialogWindowClientEvents cleCrearNotaCredito;
	protected HtmlDialogWindowHeader hdCrearNotaCredito;
	protected HtmlDialogWindowRoundedCorners rcCrearNotaCredito;
	protected HtmlDialogWindowContentPane cpCrearNotaCredito;
	protected HtmlDialogWindowAutoPostBackFlags apbCrearNotaCredito;
	protected HtmlLink lnkCerrarCrearNotaCredito;
	protected HtmlJspPanel jspPanelCrearNotaCredito;
	protected HtmlOutputText msgCodigoCliente;
	protected HtmlInputText txtCodigoCliente1;
	protected HtmlLink lnkDwCrearNotaCredito;
	protected HtmlLink lnkDwActInfoCliente;
	protected HtmlOutputText strInfoCliente;
	protected HtmlOutputText lblValidaTraerFac;
	protected HtmlOutputText lbletVouchermanual;
	protected HtmlCheckBox chkVoucherManual;
	protected HtmlDialogWindowClientEvents cledwCargando;
	protected HtmlJspPanel jspdwCargando;
	protected HtmlGraphicImageEx imagenCargando;
	protected HtmlDialogWindow dwCargando;
	protected HtmlDialogWindowRoundedCorners cledwCargando22;
	protected HtmlDialogWindowContentPane cpdwCargando;
	protected HtmlDialogWindowAutoPostBackFlags apbdwCargando;
	protected HtmlOutputText lblMensajeCargando;
	protected HtmlOutputText lblEtTrasladoPOS;
	protected HtmlCheckBox chkTrasladarPOS;
	protected HtmlLink lnkAceptarTraerFac;
	protected HtmlLink lnkCancelarTraerFac;
	protected HtmlOutputText lblCodigoCliente;
	protected HtmlOutputText lblCodigoClienteMSG1;
	protected HtmlGridView gvClienteFacsRM;
	protected HtmlColumnSelectRow columnSelectRowRendererCred;
	protected HtmlOutputText lblPartidaDetalleRecibo;
	protected HtmlOutputText lblPartidaDetalleRecibo2;
	protected HtmlOutputText lblTotalDetalleReciboC;
	protected HtmlOutputText lblTotalDetalleReciboC2;
	protected HtmlOutputText lblSubtotalCred;
	protected HtmlOutputText lblSubtotalCred2;
	protected HtmlOutputText lblIvaCred;
	protected HtmlOutputText lblIvaCred2;
	protected HtmlColumn coPartidaDetalleRecibo;
	protected HtmlColumn coTotalDetalleReciboC;
	protected HtmlColumn coSubtotalCred;
	protected HtmlColumn coIvaCred;
	protected HtmlDropDownList ddlAfiliado;
	protected HtmlCheckBox chkIngresoManual;
	protected HtmlOutputText lblNoTarjeta;
	protected HtmlInputText txtNoTarjeta;
	protected HtmlOutputText lblFechaVenceT;
	protected HtmlInputText txtFechaVenceT;
	protected HtmlOutputText lblBanda3;
	protected HtmlInputSecret track;
	protected HtmlDialogWindowHeader hdFacTraslado;
	protected HtmlJspPanel jpTraerFactura;
	protected HtmlPanelGrid pgTraeFactura;
	protected HtmlOutputText lblEtFiltroFacs;
	protected HtmlColumnSelectRow csrSelFac;
	protected HtmlOutputText lblCoNumFac;
	protected HtmlOutputText lblEtNumFac;
	protected HtmlOutputText lblCoTipoFac;
	protected HtmlOutputText lblEtTipoFac;
	protected HtmlOutputText lblCoClienteFac;
	protected HtmlOutputText lblEtClienteFac;
	protected HtmlOutputText lblCoFacUnieg;
	protected HtmlOutputText lblEtFacUnineg;
	protected HtmlOutputText lblCoFacTotal;
	protected HtmlOutputText lblEtFacTotal;
	protected HtmlOutputText lblCoFacMoneda;
	protected HtmlOutputText lblEtFacMoneda;
	protected HtmlColumnSelectRow columnSelectRowRenderer1;
	protected HtmlDialogWindow dwFacturasTraslado;
	protected HtmlDialogWindowClientEvents cleFacTraslado;
	protected HtmlDialogWindowRoundedCorners rcFacTraslado;
	protected HtmlDialogWindowContentPane cpFacTraslado;
	protected HtmlDropDownList ddlFiltroFacturaTras;
	protected HtmlInputText txtParamFiltrofac;
	protected HtmlOutputText lblEtFcajas;
	protected HtmlDropDownList ddlFiltroCajasTras;
	protected HtmlLink lnkFiltrarFacturaParaEnvio;
	protected HtmlGridView gvFacturasParaTraslado;
	protected HtmlColumn coNumFac;
	protected HtmlColumn coTipoFac;
	protected HtmlColumn coClienteFac;
	protected HtmlColumn coFacUnineg;
	protected HtmlColumn coFacTotal;
	protected HtmlColumn coFacMoneda;
	protected HtmlForm getFrmContado() {
		if (frmContado == null) {
			frmContado = (HtmlForm) findComponentInRoot("frmContado");
		}
		return frmContado;
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

	protected HtmlPanelSection getSecContado1() {
		if (secContado1 == null) {
			secContado1 = (HtmlPanelSection) findComponentInRoot("secContado1");
		}
		return secContado1;
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

	protected HtmlLink getLnkSearchContado() {
		if (lnkSearchContado == null) {
			lnkSearchContado = (HtmlLink) findComponentInRoot("lnkSearchContado");
		}
		return lnkSearchContado;
	}

	protected HtmlGraphicImageEx getImgLoader() {
		if (imgLoader == null) {
			imgLoader = (HtmlGraphicImageEx) findComponentInRoot("imgLoader");
		}
		return imgLoader;
	}

	protected HtmlOutputText getLblResultadoRec() {
		if (lblResultadoRec == null) {
			lblResultadoRec = (HtmlOutputText) findComponentInRoot("lblResultadoRec");
		}
		return lblResultadoRec;
	}

	protected HtmlGridView getGvHfacturasContado() {
		if (gvHfacturasContado == null) {
			gvHfacturasContado = (HtmlGridView) findComponentInRoot("gvHfacturasContado");
		}
		return gvHfacturasContado;
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

	protected HtmlOutputText getLblnofactura1Grande() {
		if (lblnofactura1Grande == null) {
			lblnofactura1Grande = (HtmlOutputText) findComponentInRoot("lblnofactura1Grande");
		}
		return lblnofactura1Grande;
	}

	protected HtmlOutputText getLblnofactura2Grande() {
		if (lblnofactura2Grande == null) {
			lblnofactura2Grande = (HtmlOutputText) findComponentInRoot("lblnofactura2Grande");
		}
		return lblnofactura2Grande;
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

	protected HtmlOutputText getLblNomCli1Grande() {
		if (lblNomCli1Grande == null) {
			lblNomCli1Grande = (HtmlOutputText) findComponentInRoot("lblNomCli1Grande");
		}
		return lblNomCli1Grande;
	}

	protected HtmlOutputText getLblNomCli2Grande() {
		if (lblNomCli2Grande == null) {
			lblNomCli2Grande = (HtmlOutputText) findComponentInRoot("lblNomCli2Grande");
		}
		return lblNomCli2Grande;
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

	protected HtmlOutputText getLblFecha1Grande() {
		if (lblFecha1Grande == null) {
			lblFecha1Grande = (HtmlOutputText) findComponentInRoot("lblFecha1Grande");
		}
		return lblFecha1Grande;
	}

	protected HtmlOutputText getLblFecha2Grande() {
		if (lblFecha2Grande == null) {
			lblFecha2Grande = (HtmlOutputText) findComponentInRoot("lblFecha2Grande");
		}
		return lblFecha2Grande;
	}

	protected HtmlLink getLnkProcesarRecibo2() {
		if (lnkProcesarRecibo2 == null) {
			lnkProcesarRecibo2 = (HtmlLink) findComponentInRoot("lnkProcesarRecibo2");
		}
		return lnkProcesarRecibo2;
	}

	protected HtmlLink getLnkRefrescarVistaContado() {
		if (lnkRefrescarVistaContado == null) {
			lnkRefrescarVistaContado = (HtmlLink) findComponentInRoot("lnkRefrescarVistaContado");
		}
		return lnkRefrescarVistaContado;
	}

	protected HtmlLink getLnkTraslasdar2() {
		if (lnkTraslasdar2 == null) {
			lnkTraslasdar2 = (HtmlLink) findComponentInRoot("lnkTraslasdar2");
		}
		return lnkTraslasdar2;
	}

	protected HtmlLink getLnkImportar2() {
		if (lnkImportar2 == null) {
			lnkImportar2 = (HtmlLink) findComponentInRoot("lnkImportar2");
		}
		return lnkImportar2;
	}

	protected HtmlLink getLnkTraslado2() {
		if (lnkTraslado2 == null) {
			lnkTraslado2 = (HtmlLink) findComponentInRoot("lnkTraslado2");
		}
		return lnkImportar2;
	}
	
//	lnkTraslado2
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

	protected HtmlDialogWindowHeader getHdDetalleFactura() {
		if (hdDetalleFactura == null) {
			hdDetalleFactura = (HtmlDialogWindowHeader) findComponentInRoot("hdDetalleFactura");
		}
		return hdDetalleFactura;
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

	protected HtmlOutputText getTxtMonedaContado1() {
		if (txtMonedaContado1 == null) {
			txtMonedaContado1 = (HtmlOutputText) findComponentInRoot("txtMonedaContado1");
		}
		return txtMonedaContado1;
	}

	protected HtmlOutputText getTxtNomCliente() {
		if (txtNomCliente == null) {
			txtNomCliente = (HtmlOutputText) findComponentInRoot("txtNomCliente");
		}
		return txtNomCliente;
	}

	protected HtmlOutputText getLblTasaDetalleCont() {
		if (lblTasaDetalleCont == null) {
			lblTasaDetalleCont = (HtmlOutputText) findComponentInRoot("lblTasaDetalleCont");
		}
		return lblTasaDetalleCont;
	}

	protected HtmlOutputText getLblUninegDetalleCont() {
		if (lblUninegDetalleCont == null) {
			lblUninegDetalleCont = (HtmlOutputText) findComponentInRoot("lblUninegDetalleCont");
		}
		return lblUninegDetalleCont;
	}

	protected HtmlOutputText getLblVendedorCont() {
		if (lblVendedorCont == null) {
			lblVendedorCont = (HtmlOutputText) findComponentInRoot("lblVendedorCont");
		}
		return lblVendedorCont;
	}

	protected HtmlGridView getGvDetalleFacContado() {
		if (gvDetalleFacContado == null) {
			gvDetalleFacContado = (HtmlGridView) findComponentInRoot("gvDetalleFacContado");
		}
		return gvDetalleFacContado;
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

	protected HtmlOutputText getLblSubtotalDetalleContado() {
		if (lblSubtotalDetalleContado == null) {
			lblSubtotalDetalleContado = (HtmlOutputText) findComponentInRoot("lblSubtotalDetalleContado");
		}
		return lblSubtotalDetalleContado;
	}

	protected HtmlOutputText getTxtSubtotalDetalle() {
		if (txtSubtotalDetalle == null) {
			txtSubtotalDetalle = (HtmlOutputText) findComponentInRoot("txtSubtotalDetalle");
		}
		return txtSubtotalDetalle;
	}

	protected HtmlOutputText getText28() {
		if (text28 == null) {
			text28 = (HtmlOutputText) findComponentInRoot("text28");
		}
		return text28;
	}

	protected HtmlOutputText getTxtIvaDetalle() {
		if (txtIvaDetalle == null) {
			txtIvaDetalle = (HtmlOutputText) findComponentInRoot("txtIvaDetalle");
		}
		return txtIvaDetalle;
	}

	protected HtmlOutputText getLblTotalDetCont() {
		if (lblTotalDetCont == null) {
			lblTotalDetCont = (HtmlOutputText) findComponentInRoot("lblTotalDetCont");
		}
		return lblTotalDetCont;
	}

	protected HtmlOutputText getTxtTotalDetalle() {
		if (txtTotalDetalle == null) {
			txtTotalDetalle = (HtmlOutputText) findComponentInRoot("txtTotalDetalle");
		}
		return txtTotalDetalle;
	}

	protected HtmlLink getLnkCerrarDetalleContado() {
		if (lnkCerrarDetalleContado == null) {
			lnkCerrarDetalleContado = (HtmlLink) findComponentInRoot("lnkCerrarDetalleContado");
		}
		return lnkCerrarDetalleContado;
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

	protected HtmlGraphicImageEx getImgValida() {
		if (imgValida == null) {
			imgValida = (HtmlGraphicImageEx) findComponentInRoot("imgValida");
		}
		return imgValida;
	}

	protected HtmlOutputText getLblValida() {
		if (lblValida == null) {
			lblValida = (HtmlOutputText) findComponentInRoot("lblValida");
		}
		return lblValida;
	}

	protected HtmlLink getLnkCerrarValida() {
		if (lnkCerrarValida == null) {
			lnkCerrarValida = (HtmlLink) findComponentInRoot("lnkCerrarValida");
		}
		return lnkCerrarValida;
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

	protected HtmlDialogWindowHeader getHdProcesaRecibo() {
		if (hdProcesaRecibo == null) {
			hdProcesaRecibo = (HtmlDialogWindowHeader) findComponentInRoot("hdProcesaRecibo");
		}
		return hdProcesaRecibo;
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

	protected HtmlDialogWindowHeader getHdImprime() {
		if (hdImprime == null) {
			hdImprime = (HtmlDialogWindowHeader) findComponentInRoot("hdImprime");
		}
		return hdImprime;
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

	protected HtmlCheckBox getChkImprimir() {
		if (chkImprimir == null) {
			chkImprimir = (HtmlCheckBox) findComponentInRoot("chkImprimir");
		}
		return chkImprimir;
	}

	protected HtmlLink getLnkCerrarMensaje13() {
		if (lnkCerrarMensaje13 == null) {
			lnkCerrarMensaje13 = (HtmlLink) findComponentInRoot("lnkCerrarMensaje13");
		}
		return lnkCerrarMensaje13;
	}

	protected HtmlDialogWindowHeader getHdProcesaDevolucion() {
		if (hdProcesaDevolucion == null) {
			hdProcesaDevolucion = (HtmlDialogWindowHeader) findComponentInRoot("hdProcesaDevolucion");
		}
		return hdProcesaDevolucion;
	}

	protected HtmlPanelGrid getGridProcesaDevolucion() {
		if (gridProcesaDevolucion == null) {
			gridProcesaDevolucion = (HtmlPanelGrid) findComponentInRoot("gridProcesaDevolucion");
		}
		return gridProcesaDevolucion;
	}

	protected HtmlGraphicImageEx getImageEx2ProcesaDevolucion() {
		if (imageEx2ProcesaDevolucion == null) {
			imageEx2ProcesaDevolucion = (HtmlGraphicImageEx) findComponentInRoot("imageEx2ProcesaDevolucion");
		}
		return imageEx2ProcesaDevolucion;
	}

	protected HtmlCheckBox getChkImprimirProcesaDevolucion() {
		if (chkImprimirProcesaDevolucion == null) {
			chkImprimirProcesaDevolucion = (HtmlCheckBox) findComponentInRoot("chkImprimirProcesaDevolucion");
		}
		return chkImprimirProcesaDevolucion;
	}

	protected HtmlLink getLnkCerrarProcesaDevolucion() {
		if (lnkCerrarProcesaDevolucion == null) {
			lnkCerrarProcesaDevolucion = (HtmlLink) findComponentInRoot("lnkCerrarProcesaDevolucion");
		}
		return lnkCerrarProcesaDevolucion;
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

	protected HtmlDialogWindowHeader getHdMensajeDetalleContado() {
		if (hdMensajeDetalleContado == null) {
			hdMensajeDetalleContado = (HtmlDialogWindowHeader) findComponentInRoot("hdMensajeDetalleContado");
		}
		return hdMensajeDetalleContado;
	}

	protected HtmlPanelGrid getGrid4DetalleContado() {
		if (grid4DetalleContado == null) {
			grid4DetalleContado = (HtmlPanelGrid) findComponentInRoot("grid4DetalleContado");
		}
		return grid4DetalleContado;
	}

	protected HtmlGraphicImageEx getImageEx10DetalleContado() {
		if (imageEx10DetalleContado == null) {
			imageEx10DetalleContado = (HtmlGraphicImageEx) findComponentInRoot("imageEx10DetalleContado");
		}
		return imageEx10DetalleContado;
	}

	protected HtmlLink getLnkAcptarDetalleContado() {
		if (lnkAcptarDetalleContado == null) {
			lnkAcptarDetalleContado = (HtmlLink) findComponentInRoot("lnkAcptarDetalleContado");
		}
		return lnkAcptarDetalleContado;
	}

	protected HtmlDialogWindowHeader getHdAskCancelDev() {
		if (hdAskCancelDev == null) {
			hdAskCancelDev = (HtmlDialogWindowHeader) findComponentInRoot("hdAskCancelDev");
		}
		return hdAskCancelDev;
	}

	protected HtmlPanelGrid getGridAskCancelDev() {
		if (gridAskCancelDev == null) {
			gridAskCancelDev = (HtmlPanelGrid) findComponentInRoot("gridAskCancelDev");
		}
		return gridAskCancelDev;
	}

	protected HtmlGraphicImageEx getImageEx2AskCancelDev() {
		if (imageEx2AskCancelDev == null) {
			imageEx2AskCancelDev = (HtmlGraphicImageEx) findComponentInRoot("imageEx2AskCancelDev");
		}
		return imageEx2AskCancelDev;
	}

	protected HtmlLink getLnkCerrarMensajeAskCancelDev() {
		if (lnkCerrarMensajeAskCancelDev == null) {
			lnkCerrarMensajeAskCancelDev = (HtmlLink) findComponentInRoot("lnkCerrarMensajeAskCancelDev");
		}
		return lnkCerrarMensajeAskCancelDev;
	}

	protected HtmlDialogWindowHeader getHdDevolucion() {
		if (hdDevolucion == null) {
			hdDevolucion = (HtmlDialogWindowHeader) findComponentInRoot("hdDevolucion");
		}
		return hdDevolucion;
	}

	protected HtmlJspPanel getJspDevolucion() {
		if (jspDevolucion == null) {
			jspDevolucion = (HtmlJspPanel) findComponentInRoot("jspDevolucion");
		}
		return jspDevolucion;
	}

	protected HtmlOutputText getLblFechaReciboContDev() {
		if (lblFechaReciboContDev == null) {
			lblFechaReciboContDev = (HtmlOutputText) findComponentInRoot("lblFechaReciboContDev");
		}
		return lblFechaReciboContDev;
	}

	protected HtmlOutputText getTxtFechaReciboDev() {
		if (txtFechaReciboDev == null) {
			txtFechaReciboDev = (HtmlOutputText) findComponentInRoot("txtFechaReciboDev");
		}
		return txtFechaReciboDev;
	}

	protected HtmlDateChooser getTxtFechaManualDev() {
		if (txtFechaManualDev == null) {
			txtFechaManualDev = (HtmlDateChooser) findComponentInRoot("txtFechaManualDev");
		}
		return txtFechaManualDev;
	}

	protected HtmlOutputText getLblNum() {
		if (lblNum == null) {
			lblNum = (HtmlOutputText) findComponentInRoot("lblNum");
		}
		return lblNum;
	}

	protected HtmlPanelGrid getGrid5Dev() {
		if (grid5Dev == null) {
			grid5Dev = (HtmlPanelGrid) findComponentInRoot("grid5Dev");
		}
		return grid5Dev;
	}

	protected HtmlOutputText getLblNumeroReciboDev() {
		if (lblNumeroReciboDev == null) {
			lblNumeroReciboDev = (HtmlOutputText) findComponentInRoot("lblNumeroReciboDev");
		}
		return lblNumeroReciboDev;
	}

	protected HtmlOutputText getLblClienteDev() {
		if (lblClienteDev == null) {
			lblClienteDev = (HtmlOutputText) findComponentInRoot("lblClienteDev");
		}
		return lblClienteDev;
	}

	protected HtmlOutputText getLblCodigoSearchDev() {
		if (lblCodigoSearchDev == null) {
			lblCodigoSearchDev = (HtmlOutputText) findComponentInRoot("lblCodigoSearchDev");
		}
		return lblCodigoSearchDev;
	}

	protected HtmlOutputText getLblTipoReciboDev() {
		if (lblTipoReciboDev == null) {
			lblTipoReciboDev = (HtmlOutputText) findComponentInRoot("lblTipoReciboDev");
		}
		return lblTipoReciboDev;
	}

	protected HtmlDropDownList getDdlTipoReciboDev() {
		if (ddlTipoReciboDev == null) {
			ddlTipoReciboDev = (HtmlDropDownList) findComponentInRoot("ddlTipoReciboDev");
		}
		return ddlTipoReciboDev;
	}

	protected HtmlLink getLnkDetalleFactDev() {
		if (lnkDetalleFactDev == null) {
			lnkDetalleFactDev = (HtmlLink) findComponentInRoot("lnkDetalleFactDev");
		}
		return lnkDetalleFactDev;
	}

	protected HtmlOutputText getLblNodoco() {
		if (lblNodoco == null) {
			lblNodoco = (HtmlOutputText) findComponentInRoot("lblNodoco");
		}
		return lblNodoco;
	}

	protected HtmlOutputText getLblTipodoco1() {
		if (lblTipodoco1 == null) {
			lblTipodoco1 = (HtmlOutputText) findComponentInRoot("lblTipodoco1");
		}
		return lblTipodoco1;
	}

	protected HtmlOutputText getLblTipodoco() {
		if (lblTipodoco == null) {
			lblTipodoco = (HtmlOutputText) findComponentInRoot("lblTipodoco");
		}
		return lblTipodoco;
	}

	protected HtmlOutputText getLblMonedaOdev1() {
		if (lblMonedaOdev1 == null) {
			lblMonedaOdev1 = (HtmlOutputText) findComponentInRoot("lblMonedaOdev1");
		}
		return lblMonedaOdev1;
	}

	protected HtmlOutputText getLblMonedaOdev() {
		if (lblMonedaOdev == null) {
			lblMonedaOdev = (HtmlOutputText) findComponentInRoot("lblMonedaOdev");
		}
		return lblMonedaOdev;
	}

	protected HtmlOutputText getLblMontoOdev1() {
		if (lblMontoOdev1 == null) {
			lblMontoOdev1 = (HtmlOutputText) findComponentInRoot("lblMontoOdev1");
		}
		return lblMontoOdev1;
	}

	protected HtmlOutputText getLblMontoOdev() {
		if (lblMontoOdev == null) {
			lblMontoOdev = (HtmlOutputText) findComponentInRoot("lblMontoOdev");
		}
		return lblMontoOdev;
	}

	protected HtmlOutputText getLblFechadev1() {
		if (lblFechadev1 == null) {
			lblFechadev1 = (HtmlOutputText) findComponentInRoot("lblFechadev1");
		}
		return lblFechadev1;
	}

	protected HtmlOutputText getLblFechadev() {
		if (lblFechadev == null) {
			lblFechadev = (HtmlOutputText) findComponentInRoot("lblFechadev");
		}
		return lblFechadev;
	}

	protected HtmlOutputText getLblNoReciboOdev1() {
		if (lblNoReciboOdev1 == null) {
			lblNoReciboOdev1 = (HtmlOutputText) findComponentInRoot("lblNoReciboOdev1");
		}
		return lblNoReciboOdev1;
	}

	protected HtmlOutputText getLblNoReciboOdev() {
		if (lblNoReciboOdev == null) {
			lblNoReciboOdev = (HtmlOutputText) findComponentInRoot("lblNoReciboOdev");
		}
		return lblNoReciboOdev;
	}

	protected HtmlOutputText getLblHoraReciboDev1() {
		if (lblHoraReciboDev1 == null) {
			lblHoraReciboDev1 = (HtmlOutputText) findComponentInRoot("lblHoraReciboDev1");
		}
		return lblHoraReciboDev1;
	}

	protected HtmlOutputText getLblHoraReciboDev() {
		if (lblHoraReciboDev == null) {
			lblHoraReciboDev = (HtmlOutputText) findComponentInRoot("lblHoraReciboDev");
		}
		return lblHoraReciboDev;
	}

	protected HtmlGridView getGvDetalleReciboOriginal() {
		if (gvDetalleReciboOriginal == null) {
			gvDetalleReciboOriginal = (HtmlGridView) findComponentInRoot("gvDetalleReciboOriginal");
		}
		return gvDetalleReciboOriginal;
	}

	protected HtmlColumn getCoMetodoOdev() {
		if (coMetodoOdev == null) {
			coMetodoOdev = (HtmlColumn) findComponentInRoot("coMetodoOdev");
		}
		return coMetodoOdev;
	}

	protected HtmlOutputText getLblMetodoOdev() {
		if (lblMetodoOdev == null) {
			lblMetodoOdev = (HtmlOutputText) findComponentInRoot("lblMetodoOdev");
		}
		return lblMetodoOdev;
	}

	protected HtmlOutputText getLblMetodo2Odev() {
		if (lblMetodo2Odev == null) {
			lblMetodo2Odev = (HtmlOutputText) findComponentInRoot("lblMetodo2Odev");
		}
		return lblMetodo2Odev;
	}

	protected HtmlOutputText getLblMonedaOdev22() {
		if (lblMonedaOdev22 == null) {
			lblMonedaOdev22 = (HtmlOutputText) findComponentInRoot("lblMonedaOdev22");
		}
		return lblMonedaOdev22;
	}

	protected HtmlOutputText getLblMoneda22Odev() {
		if (lblMoneda22Odev == null) {
			lblMoneda22Odev = (HtmlOutputText) findComponentInRoot("lblMoneda22Odev");
		}
		return lblMoneda22Odev;
	}

	protected HtmlOutputText getLblMonto22Odev() {
		if (lblMonto22Odev == null) {
			lblMonto22Odev = (HtmlOutputText) findComponentInRoot("lblMonto22Odev");
		}
		return lblMonto22Odev;
	}

	protected HtmlOutputText getLblMonto222Odev() {
		if (lblMonto222Odev == null) {
			lblMonto222Odev = (HtmlOutputText) findComponentInRoot("lblMonto222Odev");
		}
		return lblMonto222Odev;
	}

	protected HtmlOutputText getLblTasaOdev() {
		if (lblTasaOdev == null) {
			lblTasaOdev = (HtmlOutputText) findComponentInRoot("lblTasaOdev");
		}
		return lblTasaOdev;
	}

	protected HtmlOutputText getLblTasa2Odev() {
		if (lblTasa2Odev == null) {
			lblTasa2Odev = (HtmlOutputText) findComponentInRoot("lblTasa2Odev");
		}
		return lblTasa2Odev;
	}

	protected HtmlOutputText getLblEquivDetalleOdev() {
		if (lblEquivDetalleOdev == null) {
			lblEquivDetalleOdev = (HtmlOutputText) findComponentInRoot("lblEquivDetalleOdev");
		}
		return lblEquivDetalleOdev;
	}

	protected HtmlOutputText getLblEquivDetalle2Odev() {
		if (lblEquivDetalle2Odev == null) {
			lblEquivDetalle2Odev = (HtmlOutputText) findComponentInRoot("lblEquivDetalle2Odev");
		}
		return lblEquivDetalle2Odev;
	}

	protected HtmlOutputText getLblReferencia29Odev() {
		if (lblReferencia29Odev == null) {
			lblReferencia29Odev = (HtmlOutputText) findComponentInRoot("lblReferencia29Odev");
		}
		return lblReferencia29Odev;
	}

	protected HtmlOutputText getLblReferencia19Odev() {
		if (lblReferencia19Odev == null) {
			lblReferencia19Odev = (HtmlOutputText) findComponentInRoot("lblReferencia19Odev");
		}
		return lblReferencia19Odev;
	}

	protected HtmlOutputText getLblReferencia222Odev() {
		if (lblReferencia222Odev == null) {
			lblReferencia222Odev = (HtmlOutputText) findComponentInRoot("lblReferencia222Odev");
		}
		return lblReferencia222Odev;
	}

	protected HtmlOutputText getLblReferencia22Odev() {
		if (lblReferencia22Odev == null) {
			lblReferencia22Odev = (HtmlOutputText) findComponentInRoot("lblReferencia22Odev");
		}
		return lblReferencia22Odev;
	}

	protected HtmlOutputText getLblReferencia322Odev() {
		if (lblReferencia322Odev == null) {
			lblReferencia322Odev = (HtmlOutputText) findComponentInRoot("lblReferencia322Odev");
		}
		return lblReferencia322Odev;
	}

	protected HtmlOutputText getLblReferencia32Odev() {
		if (lblReferencia32Odev == null) {
			lblReferencia32Odev = (HtmlOutputText) findComponentInRoot("lblReferencia32Odev");
		}
		return lblReferencia32Odev;
	}

	protected HtmlOutputText getLblReferencia323Odev() {
		if (lblReferencia323Odev == null) {
			lblReferencia323Odev = (HtmlOutputText) findComponentInRoot("lblReferencia323Odev");
		}
		return lblReferencia323Odev;
	}

	protected HtmlOutputText getLblReferencia33Odev() {
		if (lblReferencia33Odev == null) {
			lblReferencia33Odev = (HtmlOutputText) findComponentInRoot("lblReferencia33Odev");
		}
		return lblReferencia33Odev;
	}

	protected HtmlLink getLnkDetalleFactFact() {
		if (lnkDetalleFactFact == null) {
			lnkDetalleFactFact = (HtmlLink) findComponentInRoot("lnkDetalleFactFact");
		}
		return lnkDetalleFactFact;
	}

	protected HtmlOutputText getLblNoFactDev() {
		if (lblNoFactDev == null) {
			lblNoFactDev = (HtmlOutputText) findComponentInRoot("lblNoFactDev");
		}
		return lblNoFactDev;
	}

	protected HtmlOutputText getLblTipoDocFactDev1() {
		if (lblTipoDocFactDev1 == null) {
			lblTipoDocFactDev1 = (HtmlOutputText) findComponentInRoot("lblTipoDocFactDev1");
		}
		return lblTipoDocFactDev1;
	}

	protected HtmlOutputText getLblTipoDocFactDev() {
		if (lblTipoDocFactDev == null) {
			lblTipoDocFactDev = (HtmlOutputText) findComponentInRoot("lblTipoDocFactDev");
		}
		return lblTipoDocFactDev;
	}

	protected HtmlOutputText getLblMonedaFactDev1() {
		if (lblMonedaFactDev1 == null) {
			lblMonedaFactDev1 = (HtmlOutputText) findComponentInRoot("lblMonedaFactDev1");
		}
		return lblMonedaFactDev1;
	}

	protected HtmlOutputText getLblMonedaFactDev() {
		if (lblMonedaFactDev == null) {
			lblMonedaFactDev = (HtmlOutputText) findComponentInRoot("lblMonedaFactDev");
		}
		return lblMonedaFactDev;
	}

	protected HtmlOutputText getLblMontoFactDev1() {
		if (lblMontoFactDev1 == null) {
			lblMontoFactDev1 = (HtmlOutputText) findComponentInRoot("lblMontoFactDev1");
		}
		return lblMontoFactDev1;
	}

	protected HtmlOutputText getLblMontoFactDev() {
		if (lblMontoFactDev == null) {
			lblMontoFactDev = (HtmlOutputText) findComponentInRoot("lblMontoFactDev");
		}
		return lblMontoFactDev;
	}

	protected HtmlOutputText getLblMetodosPagoFacDev1() {
		if (lblMetodosPagoFacDev1 == null) {
			lblMetodosPagoFacDev1 = (HtmlOutputText) findComponentInRoot("lblMetodosPagoFacDev1");
		}
		return lblMetodosPagoFacDev1;
	}

	protected HtmlDropDownList getCmbMetodosPagoFacDev() {
		if (cmbMetodosPagoFacDev == null) {
			cmbMetodosPagoFacDev = (HtmlDropDownList) findComponentInRoot("cmbMetodosPagoFacDev");
		}
		return cmbMetodosPagoFacDev;
	}

	protected HtmlOutputText getLblMontoFacDev() {
		if (lblMontoFacDev == null) {
			lblMontoFacDev = (HtmlOutputText) findComponentInRoot("lblMontoFacDev");
		}
		return lblMontoFacDev;
	}

	protected HtmlInputText getTxtMontoFacDev() {
		if (txtMontoFacDev == null) {
			txtMontoFacDev = (HtmlInputText) findComponentInRoot("txtMontoFacDev");
		}
		return txtMontoFacDev;
	}

	protected HtmlLink getLnkRegistrarPagoFacDev() {
		if (lnkRegistrarPagoFacDev == null) {
			lnkRegistrarPagoFacDev = (HtmlLink) findComponentInRoot("lnkRegistrarPagoFacDev");
		}
		return lnkRegistrarPagoFacDev;
	}

	protected HtmlGridView getGvDetalleReciboFactDev() {
		if (gvDetalleReciboFactDev == null) {
			gvDetalleReciboFactDev = (HtmlGridView) findComponentInRoot("gvDetalleReciboFactDev");
		}
		return gvDetalleReciboFactDev;
	}

	protected HtmlColumn getCoEliminarPagoDev() {
		if (coEliminarPagoDev == null) {
			coEliminarPagoDev = (HtmlColumn) findComponentInRoot("coEliminarPagoDev");
		}
		return coEliminarPagoDev;
	}

	protected HtmlLink getLnkEliminarDetalleDev() {
		if (lnkEliminarDetalleDev == null) {
			lnkEliminarDetalleDev = (HtmlLink) findComponentInRoot("lnkEliminarDetalleDev");
		}
		return lnkEliminarDetalleDev;
	}

	protected HtmlOutputText getLblEliminarPagoDev() {
		if (lblEliminarPagoDev == null) {
			lblEliminarPagoDev = (HtmlOutputText) findComponentInRoot("lblEliminarPagoDev");
		}
		return lblEliminarPagoDev;
	}

	protected HtmlOutputText getLblMetodoFactDev() {
		if (lblMetodoFactDev == null) {
			lblMetodoFactDev = (HtmlOutputText) findComponentInRoot("lblMetodoFactDev");
		}
		return lblMetodoFactDev;
	}

	protected HtmlOutputText getLblMetodo2FactDev() {
		if (lblMetodo2FactDev == null) {
			lblMetodo2FactDev = (HtmlOutputText) findComponentInRoot("lblMetodo2FactDev");
		}
		return lblMetodo2FactDev;
	}

	protected HtmlOutputText getLblMonedaFactDev22() {
		if (lblMonedaFactDev22 == null) {
			lblMonedaFactDev22 = (HtmlOutputText) findComponentInRoot("lblMonedaFactDev22");
		}
		return lblMonedaFactDev22;
	}

	protected HtmlOutputText getLblMoneda22FactDev() {
		if (lblMoneda22FactDev == null) {
			lblMoneda22FactDev = (HtmlOutputText) findComponentInRoot("lblMoneda22FactDev");
		}
		return lblMoneda22FactDev;
	}

	protected HtmlOutputText getLblMonto22FactDev() {
		if (lblMonto22FactDev == null) {
			lblMonto22FactDev = (HtmlOutputText) findComponentInRoot("lblMonto22FactDev");
		}
		return lblMonto22FactDev;
	}

	protected HtmlOutputText getLblMonto222FactDev() {
		if (lblMonto222FactDev == null) {
			lblMonto222FactDev = (HtmlOutputText) findComponentInRoot("lblMonto222FactDev");
		}
		return lblMonto222FactDev;
	}

	protected HtmlOutputText getLblTasaFactDev() {
		if (lblTasaFactDev == null) {
			lblTasaFactDev = (HtmlOutputText) findComponentInRoot("lblTasaFactDev");
		}
		return lblTasaFactDev;
	}

	protected HtmlOutputText getLblTasa2FactDev() {
		if (lblTasa2FactDev == null) {
			lblTasa2FactDev = (HtmlOutputText) findComponentInRoot("lblTasa2FactDev");
		}
		return lblTasa2FactDev;
	}

	protected HtmlOutputText getLblEquivDetalleFactDev() {
		if (lblEquivDetalleFactDev == null) {
			lblEquivDetalleFactDev = (HtmlOutputText) findComponentInRoot("lblEquivDetalleFactDev");
		}
		return lblEquivDetalleFactDev;
	}

	protected HtmlOutputText getLblEquivDetalle2FactDev() {
		if (lblEquivDetalle2FactDev == null) {
			lblEquivDetalle2FactDev = (HtmlOutputText) findComponentInRoot("lblEquivDetalle2FactDev");
		}
		return lblEquivDetalle2FactDev;
	}

	protected HtmlOutputText getLblReferencia29FactDev() {
		if (lblReferencia29FactDev == null) {
			lblReferencia29FactDev = (HtmlOutputText) findComponentInRoot("lblReferencia29FactDev");
		}
		return lblReferencia29FactDev;
	}

	protected HtmlOutputText getLblReferencia19FactDev() {
		if (lblReferencia19FactDev == null) {
			lblReferencia19FactDev = (HtmlOutputText) findComponentInRoot("lblReferencia19FactDev");
		}
		return lblReferencia19FactDev;
	}

	protected HtmlOutputText getLblReferencia222FactDev() {
		if (lblReferencia222FactDev == null) {
			lblReferencia222FactDev = (HtmlOutputText) findComponentInRoot("lblReferencia222FactDev");
		}
		return lblReferencia222FactDev;
	}

	protected HtmlOutputText getLblReferencia22FactDev() {
		if (lblReferencia22FactDev == null) {
			lblReferencia22FactDev = (HtmlOutputText) findComponentInRoot("lblReferencia22FactDev");
		}
		return lblReferencia22FactDev;
	}

	protected HtmlOutputText getLblReferencia322FactDev() {
		if (lblReferencia322FactDev == null) {
			lblReferencia322FactDev = (HtmlOutputText) findComponentInRoot("lblReferencia322FactDev");
		}
		return lblReferencia322FactDev;
	}

	protected HtmlOutputText getLblReferencia32FactDev() {
		if (lblReferencia32FactDev == null) {
			lblReferencia32FactDev = (HtmlOutputText) findComponentInRoot("lblReferencia32FactDev");
		}
		return lblReferencia32FactDev;
	}

	protected HtmlOutputText getLblReferencia323FactDev() {
		if (lblReferencia323FactDev == null) {
			lblReferencia323FactDev = (HtmlOutputText) findComponentInRoot("lblReferencia323FactDev");
		}
		return lblReferencia323FactDev;
	}

	protected HtmlOutputText getLblReferencia33FactDev() {
		if (lblReferencia33FactDev == null) {
			lblReferencia33FactDev = (HtmlOutputText) findComponentInRoot("lblReferencia33FactDev");
		}
		return lblReferencia33FactDev;
	}

	protected HtmlOutputText getLblTasaCambioDev1() {
		if (lblTasaCambioDev1 == null) {
			lblTasaCambioDev1 = (HtmlOutputText) findComponentInRoot("lblTasaCambioDev1");
		}
		return lblTasaCambioDev1;
	}

	protected HtmlOutputText getLblTasaJDEDev1() {
		if (lblTasaJDEDev1 == null) {
			lblTasaJDEDev1 = (HtmlOutputText) findComponentInRoot("lblTasaJDEDev1");
		}
		return lblTasaJDEDev1;
	}

	protected HtmlOutputText getLblConceptoDev() {
		if (lblConceptoDev == null) {
			lblConceptoDev = (HtmlOutputText) findComponentInRoot("lblConceptoDev");
		}
		return lblConceptoDev;
	}

	protected HtmlInputTextarea getTxtConceptoDev() {
		if (txtConceptoDev == null) {
			txtConceptoDev = (HtmlInputTextarea) findComponentInRoot("txtConceptoDev");
		}
		return txtConceptoDev;
	}

	protected HtmlOutputText getLblMontoAplicarDev() {
		if (lblMontoAplicarDev == null) {
			lblMontoAplicarDev = (HtmlOutputText) findComponentInRoot("lblMontoAplicarDev");
		}
		return lblMontoAplicarDev;
	}

	protected HtmlOutputText getLblMontoAplicar2Dev() {
		if (lblMontoAplicar2Dev == null) {
			lblMontoAplicar2Dev = (HtmlOutputText) findComponentInRoot("lblMontoAplicar2Dev");
		}
		return lblMontoAplicar2Dev;
	}

	protected HtmlOutputText getLblMontoProcesado() {
		if (lblMontoProcesado == null) {
			lblMontoProcesado = (HtmlOutputText) findComponentInRoot("lblMontoProcesado");
		}
		return lblMontoProcesado;
	}

	protected HtmlOutputText getLblMontoProcesado2() {
		if (lblMontoProcesado2 == null) {
			lblMontoProcesado2 = (HtmlOutputText) findComponentInRoot("lblMontoProcesado2");
		}
		return lblMontoProcesado2;
	}

	protected HtmlOutputText getLblFaltante() {
		if (lblFaltante == null) {
			lblFaltante = (HtmlOutputText) findComponentInRoot("lblFaltante");
		}
		return lblFaltante;
	}

	protected HtmlOutputText getLblFaltante2() {
		if (lblFaltante2 == null) {
			lblFaltante2 = (HtmlOutputText) findComponentInRoot("lblFaltante2");
		}
		return lblFaltante2;
	}

	protected HtmlLink getLnkProcesarReciboFacDev() {
		if (lnkProcesarReciboFacDev == null) {
			lnkProcesarReciboFacDev = (HtmlLink) findComponentInRoot("lnkProcesarReciboFacDev");
		}
		return lnkProcesarReciboFacDev;
	}

	protected HtmlDialogWindowHeader getHdReciboContado() {
		if (hdReciboContado == null) {
			hdReciboContado = (HtmlDialogWindowHeader) findComponentInRoot("hdReciboContado");
		}
		return hdReciboContado;
	}

	protected HtmlJspPanel getJspReciboCont() {
		if (jspReciboCont == null) {
			jspReciboCont = (HtmlJspPanel) findComponentInRoot("jspReciboCont");
		}
		return jspReciboCont;
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

	protected HtmlDropDownList getDdlMetodoPago() {
		if (ddlMetodoPago == null) {
			ddlMetodoPago = (HtmlDropDownList) findComponentInRoot("ddlMetodoPago");
		}
		return ddlMetodoPago;
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

	protected HtmlDropDownList getDdlBanco() {
		if (ddlBanco == null) {
			ddlBanco = (HtmlDropDownList) findComponentInRoot("ddlBanco");
		}
		return ddlBanco;
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

	protected HtmlGridView getMetodosGrid() {
		if (metodosGrid == null) {
			metodosGrid = (HtmlGridView) findComponentInRoot("metodosGrid");
		}
		return metodosGrid;
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

	protected HtmlOutputText getLblMonedaDetalleRecibo() {
		if (lblMonedaDetalleRecibo == null) {
			lblMonedaDetalleRecibo = (HtmlOutputText) findComponentInRoot("lblMonedaDetalleRecibo");
		}
		return lblMonedaDetalleRecibo;
	}

	protected HtmlOutputText getLblMonedaDetalleRecibo2() {
		if (lblMonedaDetalleRecibo2 == null) {
			lblMonedaDetalleRecibo2 = (HtmlOutputText) findComponentInRoot("lblMonedaDetalleRecibo2");
		}
		return lblMonedaDetalleRecibo2;
	}

	protected HtmlOutputText getLblEquivDetalleRecibo() {
		if (lblEquivDetalleRecibo == null) {
			lblEquivDetalleRecibo = (HtmlOutputText) findComponentInRoot("lblEquivDetalleRecibo");
		}
		return lblEquivDetalleRecibo;
	}

	protected HtmlOutputText getLblEquivDetalleRecibo2() {
		if (lblEquivDetalleRecibo2 == null) {
			lblEquivDetalleRecibo2 = (HtmlOutputText) findComponentInRoot("lblEquivDetalleRecibo2");
		}
		return lblEquivDetalleRecibo2;
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

	protected HtmlOutputText getLblPendienteDomestico() {
		if (lblPendienteDomestico == null) {
			lblPendienteDomestico = (HtmlOutputText) findComponentInRoot("lblPendienteDomestico");
		}
		return lblPendienteDomestico;
	}

	protected HtmlOutputText getTxtPendienteDomestico() {
		if (txtPendienteDomestico == null) {
			txtPendienteDomestico = (HtmlOutputText) findComponentInRoot("txtPendienteDomestico");
		}
		return txtPendienteDomestico;
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

	protected HtmlDialogWindowHeader getHdConfirmaEmisionCheque() {
		if (hdConfirmaEmisionCheque == null) {
			hdConfirmaEmisionCheque = (HtmlDialogWindowHeader) findComponentInRoot("hdConfirmaEmisionCheque");
		}
		return hdConfirmaEmisionCheque;
	}

	protected HtmlJspPanel getJspPMensaje() {
		if (jspPMensaje == null) {
			jspPMensaje = (HtmlJspPanel) findComponentInRoot("jspPMensaje");
		}
		return jspPMensaje;
	}

	protected HtmlOutputText getLblEtdwConfirmarsolecheque() {
		if (lblEtdwConfirmarsolecheque == null) {
			lblEtdwConfirmarsolecheque = (HtmlOutputText) findComponentInRoot("lblEtdwConfirmarsolecheque");
		}
		return lblEtdwConfirmarsolecheque;
	}

	protected HtmlOutputText getLblMsgValidaSolecheque() {
		if (lblMsgValidaSolecheque == null) {
			lblMsgValidaSolecheque = (HtmlOutputText) findComponentInRoot("lblMsgValidaSolecheque");
		}
		return lblMsgValidaSolecheque;
	}

	protected HtmlLink getLnkSolicitaEmisionCkOk() {
		if (lnkSolicitaEmisionCkOk == null) {
			lnkSolicitaEmisionCkOk = (HtmlLink) findComponentInRoot("lnkSolicitaEmisionCkOk");
		}
		return lnkSolicitaEmisionCkOk;
	}

	protected HtmlDialogWindowHeader getHdCajaTraslado() {
		if (hdCajaTraslado == null) {
			hdCajaTraslado = (HtmlDialogWindowHeader) findComponentInRoot("hdCajaTraslado");
		}
		return hdCajaTraslado;
	}

	protected HtmlJspPanel getJpEnviarFactura() {
		if (jpEnviarFactura == null) {
			jpEnviarFactura = (HtmlJspPanel) findComponentInRoot("jpEnviarFactura");
		}
		return jpEnviarFactura;
	}

	protected HtmlPanelGrid getPgEnvioFactura1() {
		if (pgEnvioFactura1 == null) {
			pgEnvioFactura1 = (HtmlPanelGrid) findComponentInRoot("pgEnvioFactura1");
		}
		return pgEnvioFactura1;
	}

	protected HtmlOutputText getLblEtFiltroCaja() {
		if (lblEtFiltroCaja == null) {
			lblEtFiltroCaja = (HtmlOutputText) findComponentInRoot("lblEtFiltroCaja");
		}
		return lblEtFiltroCaja;
	}

	protected UINamingContainer getVfContado() {
		if (vfContado == null) {
			vfContado = (UINamingContainer) findComponentInRoot("vfContado");
		}
		return vfContado;
	}

	protected HtmlScriptCollector getScContado() {
		if (scContado == null) {
			scContado = (HtmlScriptCollector) findComponentInRoot("scContado");
		}
		return scContado;
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

	protected HtmlOutputText getTxtMensaje() {
		if (txtMensaje == null) {
			txtMensaje = (HtmlOutputText) findComponentInRoot("txtMensaje");
		}
		return txtMensaje;
	}

	protected HtmlColumnSelectRow getColumnSelectRowRenderer1777() {
		if (columnSelectRowRenderer1777 == null) {
			columnSelectRowRenderer1777 = (HtmlColumnSelectRow) findComponentInRoot("columnSelectRowRenderer1777");
		}
		return columnSelectRowRenderer1777;
	}

	protected HtmlColumn getCoLnkDetalle() {
		if (coLnkDetalle == null) {
			coLnkDetalle = (HtmlColumn) findComponentInRoot("coLnkDetalle");
		}
		return coLnkDetalle;
	}

	protected HtmlColumn getCoNoFactura() {
		if (coNoFactura == null) {
			coNoFactura = (HtmlColumn) findComponentInRoot("coNoFactura");
		}
		return coNoFactura;
	}

	protected HtmlColumn getCoTipoFactura() {
		if (coTipoFactura == null) {
			coTipoFactura = (HtmlColumn) findComponentInRoot("coTipoFactura");
		}
		return coTipoFactura;
	}

	protected HtmlColumn getCoNomCli() {
		if (coNomCli == null) {
			coNomCli = (HtmlColumn) findComponentInRoot("coNomCli");
		}
		return coNomCli;
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

	protected HtmlColumn getCoFecha() {
		if (coFecha == null) {
			coFecha = (HtmlColumn) findComponentInRoot("coFecha");
		}
		return coFecha;
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

	protected HtmlDialogWindow getDgwDetalleFactura() {
		if (dgwDetalleFactura == null) {
			dgwDetalleFactura = (HtmlDialogWindow) findComponentInRoot("dgwDetalleFactura");
		}
		return dgwDetalleFactura;
	}

	protected HtmlDialogWindowClientEvents getClDetalleContado() {
		if (clDetalleContado == null) {
			clDetalleContado = (HtmlDialogWindowClientEvents) findComponentInRoot("clDetalleContado");
		}
		return clDetalleContado;
	}

	protected HtmlDialogWindowRoundedCorners getCrDetalle() {
		if (crDetalle == null) {
			crDetalle = (HtmlDialogWindowRoundedCorners) findComponentInRoot("crDetalle");
		}
		return crDetalle;
	}

	protected HtmlDialogWindowContentPane getCnpDetalle() {
		if (cnpDetalle == null) {
			cnpDetalle = (HtmlDialogWindowContentPane) findComponentInRoot("cnpDetalle");
		}
		return cnpDetalle;
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

	protected HtmlOutputText getTxtCodUnineg() {
		if (txtCodUnineg == null) {
			txtCodUnineg = (HtmlOutputText) findComponentInRoot("txtCodUnineg");
		}
		return txtCodUnineg;
	}

	protected HtmlOutputText getText23() {
		if (text23 == null) {
			text23 = (HtmlOutputText) findComponentInRoot("text23");
		}
		return text23;
	}

	protected HtmlOutputText getTxtVendedorCont() {
		if (txtVendedorCont == null) {
			txtVendedorCont = (HtmlOutputText) findComponentInRoot("txtVendedorCont");
		}
		return txtVendedorCont;
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

	protected HtmlDialogWindowAutoPostBackFlags getApbDetalle() {
		if (apbDetalle == null) {
			apbDetalle = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbDetalle");
		}
		return apbDetalle;
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

	protected HtmlDialogWindowAutoPostBackFlags getApbReciboContado2() {
		if (apbReciboContado2 == null) {
			apbReciboContado2 = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbReciboContado2");
		}
		return apbReciboContado2;
	}

	protected HtmlDialogWindow getDwSolicitud() {
		if (dwSolicitud == null) {
			dwSolicitud = (HtmlDialogWindow) findComponentInRoot("dwSolicitud");
		}
		return dwSolicitud;
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

	protected HtmlJspPanel getJspProcesa() {
		if (jspProcesa == null) {
			jspProcesa = (HtmlJspPanel) findComponentInRoot("jspProcesa");
		}
		return jspProcesa;
	}

	protected HtmlDialogWindowAutoPostBackFlags getApbProcesaRecibo() {
		if (apbProcesaRecibo == null) {
			apbProcesaRecibo = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbProcesaRecibo");
		}
		return apbProcesaRecibo;
	}

	protected HtmlDialogWindow getDwImprimeContado() {
		if (dwImprimeContado == null) {
			dwImprimeContado = (HtmlDialogWindow) findComponentInRoot("dwImprimeContado");
		}
		return dwImprimeContado;
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

	protected HtmlJspPanel getJspPanel3() {
		if (jspPanel3 == null) {
			jspPanel3 = (HtmlJspPanel) findComponentInRoot("jspPanel3");
		}
		return jspPanel3;
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

	protected HtmlDialogWindow getDwProcesaDevolucion() {
		if (dwProcesaDevolucion == null) {
			dwProcesaDevolucion = (HtmlDialogWindow) findComponentInRoot("dwProcesaDevolucion");
		}
		return dwProcesaDevolucion;
	}

	protected HtmlDialogWindowClientEvents getCleProcesaDevolucion() {
		if (cleProcesaDevolucion == null) {
			cleProcesaDevolucion = (HtmlDialogWindowClientEvents) findComponentInRoot("cleProcesaDevolucion");
		}
		return cleProcesaDevolucion;
	}

	protected HtmlDialogWindowRoundedCorners getRcProcesaDevolucion() {
		if (rcProcesaDevolucion == null) {
			rcProcesaDevolucion = (HtmlDialogWindowRoundedCorners) findComponentInRoot("rcProcesaDevolucion");
		}
		return rcProcesaDevolucion;
	}

	protected HtmlDialogWindowContentPane getCpProcesaDevolucion() {
		if (cpProcesaDevolucion == null) {
			cpProcesaDevolucion = (HtmlDialogWindowContentPane) findComponentInRoot("cpProcesaDevolucion");
		}
		return cpProcesaDevolucion;
	}

	protected HtmlOutputText getLblConfirmProcesaDevolucion() {
		if (lblConfirmProcesaDevolucion == null) {
			lblConfirmProcesaDevolucion = (HtmlOutputText) findComponentInRoot("lblConfirmProcesaDevolucion");
		}
		return lblConfirmProcesaDevolucion;
	}

	protected HtmlJspPanel getJspProcesaDevolucion() {
		if (jspProcesaDevolucion == null) {
			jspProcesaDevolucion = (HtmlJspPanel) findComponentInRoot("jspProcesaDevolucion");
		}
		return jspProcesaDevolucion;
	}

	protected HtmlLink getLnkProcesaDevolucion() {
		if (lnkProcesaDevolucion == null) {
			lnkProcesaDevolucion = (HtmlLink) findComponentInRoot("lnkProcesaDevolucion");
		}
		return lnkProcesaDevolucion;
	}

	protected HtmlDialogWindowAutoPostBackFlags getApbProcesaDevolucion() {
		if (apbProcesaDevolucion == null) {
			apbProcesaDevolucion = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbProcesaDevolucion");
		}
		return apbProcesaDevolucion;
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

	protected HtmlDialogWindow getDwMensajeDetalleContado() {
		if (dwMensajeDetalleContado == null) {
			dwMensajeDetalleContado = (HtmlDialogWindow) findComponentInRoot("dwMensajeDetalleContado");
		}
		return dwMensajeDetalleContado;
	}

	protected HtmlDialogWindowClientEvents getCleMensajeDetalleContado() {
		if (cleMensajeDetalleContado == null) {
			cleMensajeDetalleContado = (HtmlDialogWindowClientEvents) findComponentInRoot("cleMensajeDetalleContado");
		}
		return cleMensajeDetalleContado;
	}

	protected HtmlDialogWindowRoundedCorners getRcMensajeDetalleContado() {
		if (rcMensajeDetalleContado == null) {
			rcMensajeDetalleContado = (HtmlDialogWindowRoundedCorners) findComponentInRoot("rcMensajeDetalleContado");
		}
		return rcMensajeDetalleContado;
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

	protected HtmlJspPanel getJspPanel5DetalleContado() {
		if (jspPanel5DetalleContado == null) {
			jspPanel5DetalleContado = (HtmlJspPanel) findComponentInRoot("jspPanel5DetalleContado");
		}
		return jspPanel5DetalleContado;
	}

	protected HtmlDialogWindowAutoPostBackFlags getApbMensajeDetalleContado() {
		if (apbMensajeDetalleContado == null) {
			apbMensajeDetalleContado = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbMensajeDetalleContado");
		}
		return apbMensajeDetalleContado;
	}

	protected HtmlDialogWindow getDwCancelarDev() {
		if (dwCancelarDev == null) {
			dwCancelarDev = (HtmlDialogWindow) findComponentInRoot("dwCancelarDev");
		}
		return dwCancelarDev;
	}

	protected HtmlDialogWindowClientEvents getCleAskCancelDev() {
		if (cleAskCancelDev == null) {
			cleAskCancelDev = (HtmlDialogWindowClientEvents) findComponentInRoot("cleAskCancelDev");
		}
		return cleAskCancelDev;
	}

	protected HtmlDialogWindowRoundedCorners getRcAskCancelDev() {
		if (rcAskCancelDev == null) {
			rcAskCancelDev = (HtmlDialogWindowRoundedCorners) findComponentInRoot("rcAskCancelDev");
		}
		return rcAskCancelDev;
	}

	protected HtmlDialogWindowContentPane getCpAskCancelDev() {
		if (cpAskCancelDev == null) {
			cpAskCancelDev = (HtmlDialogWindowContentPane) findComponentInRoot("cpAskCancelDev");
		}
		return cpAskCancelDev;
	}

	protected HtmlOutputText getLblConfirmCancelDev() {
		if (lblConfirmCancelDev == null) {
			lblConfirmCancelDev = (HtmlOutputText) findComponentInRoot("lblConfirmCancelDev");
		}
		return lblConfirmCancelDev;
	}

	protected HtmlJspPanel getJspPanel3AskCancelDev() {
		if (jspPanel3AskCancelDev == null) {
			jspPanel3AskCancelDev = (HtmlJspPanel) findComponentInRoot("jspPanel3AskCancelDev");
		}
		return jspPanel3AskCancelDev;
	}

	protected HtmlLink getLnkCerrarAskCancelDev() {
		if (lnkCerrarAskCancelDev == null) {
			lnkCerrarAskCancelDev = (HtmlLink) findComponentInRoot("lnkCerrarAskCancelDev");
		}
		return lnkCerrarAskCancelDev;
	}

	protected HtmlDialogWindowAutoPostBackFlags getApbAskCancelDev() {
		if (apbAskCancelDev == null) {
			apbAskCancelDev = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbAskCancelDev");
		}
		return apbAskCancelDev;
	}

	protected HtmlDialogWindow getDwDevolucion() {
		if (dwDevolucion == null) {
			dwDevolucion = (HtmlDialogWindow) findComponentInRoot("dwDevolucion");
		}
		return dwDevolucion;
	}

	protected HtmlDialogWindowClientEvents getCleDevolucion() {
		if (cleDevolucion == null) {
			cleDevolucion = (HtmlDialogWindowClientEvents) findComponentInRoot("cleDevolucion");
		}
		return cleDevolucion;
	}

	protected HtmlDialogWindowRoundedCorners getRcDevolucion() {
		if (rcDevolucion == null) {
			rcDevolucion = (HtmlDialogWindowRoundedCorners) findComponentInRoot("rcDevolucion");
		}
		return rcDevolucion;
	}

	protected HtmlDialogWindowContentPane getCpDevolucion() {
		if (cpDevolucion == null) {
			cpDevolucion = (HtmlDialogWindowContentPane) findComponentInRoot("cpDevolucion");
		}
		return cpDevolucion;
	}

	protected HtmlOutputText getLblNumeroReciboManualDev() {
		if (lblNumeroReciboManualDev == null) {
			lblNumeroReciboManualDev = (HtmlOutputText) findComponentInRoot("lblNumeroReciboManualDev");
		}
		return lblNumeroReciboManualDev;
	}

	protected HtmlInputText getTxtNumeroReciboManulDev() {
		if (txtNumeroReciboManulDev == null) {
			txtNumeroReciboManulDev = (HtmlInputText) findComponentInRoot("txtNumeroReciboManulDev");
		}
		return txtNumeroReciboManulDev;
	}

	protected HtmlOutputText getLblCodDev() {
		if (lblCodDev == null) {
			lblCodDev = (HtmlOutputText) findComponentInRoot("lblCodDev");
		}
		return lblCodDev;
	}

	protected HtmlOutputText getLblNombreSearchDev() {
		if (lblNombreSearchDev == null) {
			lblNombreSearchDev = (HtmlOutputText) findComponentInRoot("lblNombreSearchDev");
		}
		return lblNombreSearchDev;
	}

	protected HtmlOutputText getLblNomDev() {
		if (lblNomDev == null) {
			lblNomDev = (HtmlOutputText) findComponentInRoot("lblNomDev");
		}
		return lblNomDev;
	}

	protected HtmlOutputText getLblNodoco1() {
		if (lblNodoco1 == null) {
			lblNodoco1 = (HtmlOutputText) findComponentInRoot("lblNodoco1");
		}
		return lblNodoco1;
	}

	protected HtmlColumn getCoMonedaDetalleOdev() {
		if (coMonedaDetalleOdev == null) {
			coMonedaDetalleOdev = (HtmlColumn) findComponentInRoot("coMonedaDetalleOdev");
		}
		return coMonedaDetalleOdev;
	}

	protected HtmlColumn getCoMontoOdev() {
		if (coMontoOdev == null) {
			coMontoOdev = (HtmlColumn) findComponentInRoot("coMontoOdev");
		}
		return coMontoOdev;
	}

	protected HtmlColumn getCoTasaOdev() {
		if (coTasaOdev == null) {
			coTasaOdev = (HtmlColumn) findComponentInRoot("coTasaOdev");
		}
		return coTasaOdev;
	}

	protected HtmlColumn getCoEquivalenteOdev() {
		if (coEquivalenteOdev == null) {
			coEquivalenteOdev = (HtmlColumn) findComponentInRoot("coEquivalenteOdev");
		}
		return coEquivalenteOdev;
	}

	protected HtmlColumn getCoReferenciaOdev() {
		if (coReferenciaOdev == null) {
			coReferenciaOdev = (HtmlColumn) findComponentInRoot("coReferenciaOdev");
		}
		return coReferenciaOdev;
	}

	protected HtmlColumn getCoReferencia2Odev() {
		if (coReferencia2Odev == null) {
			coReferencia2Odev = (HtmlColumn) findComponentInRoot("coReferencia2Odev");
		}
		return coReferencia2Odev;
	}

	protected HtmlColumn getCoReferencia3Odev() {
		if (coReferencia3Odev == null) {
			coReferencia3Odev = (HtmlColumn) findComponentInRoot("coReferencia3Odev");
		}
		return coReferencia3Odev;
	}

	protected HtmlColumn getCoReferencia4Odev() {
		if (coReferencia4Odev == null) {
			coReferencia4Odev = (HtmlColumn) findComponentInRoot("coReferencia4Odev");
		}
		return coReferencia4Odev;
	}

	protected HtmlOutputText getLblNoFactDev1() {
		if (lblNoFactDev1 == null) {
			lblNoFactDev1 = (HtmlOutputText) findComponentInRoot("lblNoFactDev1");
		}
		return lblNoFactDev1;
	}

	protected HtmlOutputText getLblCodsucDev() {
		if (lblCodsucDev == null) {
			lblCodsucDev = (HtmlOutputText) findComponentInRoot("lblCodsucDev");
		}
		return lblCodsucDev;
	}

	protected HtmlOutputText getLblCoduninegDev() {
		if (lblCoduninegDev == null) {
			lblCoduninegDev = (HtmlOutputText) findComponentInRoot("lblCoduninegDev");
		}
		return lblCoduninegDev;
	}

	protected HtmlDropDownList getCmbMonedaFacDev() {
		if (cmbMonedaFacDev == null) {
			cmbMonedaFacDev = (HtmlDropDownList) findComponentInRoot("cmbMonedaFacDev");
		}
		return cmbMonedaFacDev;
	}

	protected HtmlColumn getCoMetodoFactDev() {
		if (coMetodoFactDev == null) {
			coMetodoFactDev = (HtmlColumn) findComponentInRoot("coMetodoFactDev");
		}
		return coMetodoFactDev;
	}

	protected HtmlColumn getCoMonedaDetalleFactDev() {
		if (coMonedaDetalleFactDev == null) {
			coMonedaDetalleFactDev = (HtmlColumn) findComponentInRoot("coMonedaDetalleFactDev");
		}
		return coMonedaDetalleFactDev;
	}

	protected HtmlColumn getCoMontoFactDev() {
		if (coMontoFactDev == null) {
			coMontoFactDev = (HtmlColumn) findComponentInRoot("coMontoFactDev");
		}
		return coMontoFactDev;
	}

	protected HtmlColumn getCoTasaFactDev() {
		if (coTasaFactDev == null) {
			coTasaFactDev = (HtmlColumn) findComponentInRoot("coTasaFactDev");
		}
		return coTasaFactDev;
	}

	protected HtmlColumn getCoEquivalenteFactDev() {
		if (coEquivalenteFactDev == null) {
			coEquivalenteFactDev = (HtmlColumn) findComponentInRoot("coEquivalenteFactDev");
		}
		return coEquivalenteFactDev;
	}

	protected HtmlColumn getCoReferenciaFactDev() {
		if (coReferenciaFactDev == null) {
			coReferenciaFactDev = (HtmlColumn) findComponentInRoot("coReferenciaFactDev");
		}
		return coReferenciaFactDev;
	}

	protected HtmlColumn getCoReferencia2FactDev() {
		if (coReferencia2FactDev == null) {
			coReferencia2FactDev = (HtmlColumn) findComponentInRoot("coReferencia2FactDev");
		}
		return coReferencia2FactDev;
	}

	protected HtmlColumn getCoReferencia3FactDev() {
		if (coReferencia3FactDev == null) {
			coReferencia3FactDev = (HtmlColumn) findComponentInRoot("coReferencia3FactDev");
		}
		return coReferencia3FactDev;
	}

	protected HtmlColumn getCoReferencia4FactDev() {
		if (coReferencia4FactDev == null) {
			coReferencia4FactDev = (HtmlColumn) findComponentInRoot("coReferencia4FactDev");
		}
		return coReferencia4FactDev;
	}

	protected HtmlOutputText getLblTasaCambioDev() {
		if (lblTasaCambioDev == null) {
			lblTasaCambioDev = (HtmlOutputText) findComponentInRoot("lblTasaCambioDev");
		}
		return lblTasaCambioDev;
	}

	protected HtmlOutputText getLblTasaJDEDev() {
		if (lblTasaJDEDev == null) {
			lblTasaJDEDev = (HtmlOutputText) findComponentInRoot("lblTasaJDEDev");
		}
		return lblTasaJDEDev;
	}

	protected HtmlLink getLnkCancelarReciboFacDev() {
		if (lnkCancelarReciboFacDev == null) {
			lnkCancelarReciboFacDev = (HtmlLink) findComponentInRoot("lnkCancelarReciboFacDev");
		}
		return lnkCancelarReciboFacDev;
	}

	protected HtmlDialogWindowAutoPostBackFlags getApbDevolucion() {
		if (apbDevolucion == null) {
			apbDevolucion = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbDevolucion");
		}
		return apbDevolucion;
	}

	protected HtmlDialogWindow getDwRecibos() {
		if (dwRecibos == null) {
			dwRecibos = (HtmlDialogWindow) findComponentInRoot("dwRecibos");
		}
		return dwRecibos;
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

	protected HtmlColumn getCoTipoFacturaDtalle() {
		if (coTipoFacturaDtalle == null) {
			coTipoFacturaDtalle = (HtmlColumn) findComponentInRoot("coTipoFacturaDtalle");
		}
		return coTipoFacturaDtalle;
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

	protected HtmlColumn getCoMonedaDetalleRecibo() {
		if (coMonedaDetalleRecibo == null) {
			coMonedaDetalleRecibo = (HtmlColumn) findComponentInRoot("coMonedaDetalleRecibo");
		}
		return coMonedaDetalleRecibo;
	}

	protected HtmlColumn getCoEquivDetalleRecibo() {
		if (coEquivDetalleRecibo == null) {
			coEquivDetalleRecibo = (HtmlColumn) findComponentInRoot("coEquivDetalleRecibo");
		}
		return coEquivDetalleRecibo;
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

	protected HtmlDialogWindowAutoPostBackFlags getApbReciboContado100() {
		if (apbReciboContado100 == null) {
			apbReciboContado100 = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbReciboContado100");
		}
		return apbReciboContado100;
	}

	protected HtmlDialogWindow getDwConfirmaEmisionCheque() {
		if (dwConfirmaEmisionCheque == null) {
			dwConfirmaEmisionCheque = (HtmlDialogWindow) findComponentInRoot("dwConfirmaEmisionCheque");
		}
		return dwConfirmaEmisionCheque;
	}

	protected HtmlDialogWindowClientEvents getCleConfirmaEmisionCheque() {
		if (cleConfirmaEmisionCheque == null) {
			cleConfirmaEmisionCheque = (HtmlDialogWindowClientEvents) findComponentInRoot("cleConfirmaEmisionCheque");
		}
		return cleConfirmaEmisionCheque;
	}

	protected HtmlDialogWindowRoundedCorners getRcConfirmaEmisionCheque() {
		if (rcConfirmaEmisionCheque == null) {
			rcConfirmaEmisionCheque = (HtmlDialogWindowRoundedCorners) findComponentInRoot("rcConfirmaEmisionCheque");
		}
		return rcConfirmaEmisionCheque;
	}

	protected HtmlDialogWindowContentPane getCpConfirmaEmisionCheque() {
		if (cpConfirmaEmisionCheque == null) {
			cpConfirmaEmisionCheque = (HtmlDialogWindowContentPane) findComponentInRoot("cpConfirmaEmisionCheque");
		}
		return cpConfirmaEmisionCheque;
	}

	protected HtmlJspPanel getJspPanelSolicitaEmisionCk() {
		if (jspPanelSolicitaEmisionCk == null) {
			jspPanelSolicitaEmisionCk = (HtmlJspPanel) findComponentInRoot("jspPanelSolicitaEmisionCk");
		}
		return jspPanelSolicitaEmisionCk;
	}

	protected HtmlLink getLnkCerrarSolicitaEmisionCk() {
		if (lnkCerrarSolicitaEmisionCk == null) {
			lnkCerrarSolicitaEmisionCk = (HtmlLink) findComponentInRoot("lnkCerrarSolicitaEmisionCk");
		}
		return lnkCerrarSolicitaEmisionCk;
	}

	protected HtmlDialogWindowAutoPostBackFlags getApbSolicitaEmisionCk() {
		if (apbSolicitaEmisionCk == null) {
			apbSolicitaEmisionCk = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbSolicitaEmisionCk");
		}
		return apbSolicitaEmisionCk;
	}

	protected HtmlDialogWindow getDwCajasParaTraslado() {
		if (dwCajasParaTraslado == null) {
			dwCajasParaTraslado = (HtmlDialogWindow) findComponentInRoot("dwCajasParaTraslado");
		}
		return dwCajasParaTraslado;
	}

	protected HtmlDialogWindowClientEvents getCleCajaTraslado() {
		if (cleCajaTraslado == null) {
			cleCajaTraslado = (HtmlDialogWindowClientEvents) findComponentInRoot("cleCajaTraslado");
		}
		return cleCajaTraslado;
	}

	protected HtmlDialogWindowRoundedCorners getRcCajaTraslado() {
		if (rcCajaTraslado == null) {
			rcCajaTraslado = (HtmlDialogWindowRoundedCorners) findComponentInRoot("rcCajaTraslado");
		}
		return rcCajaTraslado;
	}

	protected HtmlDialogWindowContentPane getCpCajaTraslado() {
		if (cpCajaTraslado == null) {
			cpCajaTraslado = (HtmlDialogWindowContentPane) findComponentInRoot("cpCajaTraslado");
		}
		return cpCajaTraslado;
	}

	protected HtmlDropDownList getDdlFiltroCaja() {
		if (ddlFiltroCaja == null) {
			ddlFiltroCaja = (HtmlDropDownList) findComponentInRoot("ddlFiltroCaja");
		}
		return ddlFiltroCaja;
	}

	protected HtmlInputText getTxtParamCajaEnvio() {
		if (txtParamCajaEnvio == null) {
			txtParamCajaEnvio = (HtmlInputText) findComponentInRoot("txtParamCajaEnvio");
		}
		return txtParamCajaEnvio;
	}

	protected HtmlGridView getGvCajasDisponibleEnvio() {
		if (gvCajasDisponibleEnvio == null) {
			gvCajasDisponibleEnvio = (HtmlGridView) findComponentInRoot("gvCajasDisponibleEnvio");
		}
		return gvCajasDisponibleEnvio;
	}

	protected HtmlColumn getCoNumcaja() {
		if (coNumcaja == null) {
			coNumcaja = (HtmlColumn) findComponentInRoot("coNumcaja");
		}
		return coNumcaja;
	}

	protected HtmlOutputText getLblCoNumcaja() {
		if (lblCoNumcaja == null) {
			lblCoNumcaja = (HtmlOutputText) findComponentInRoot("lblCoNumcaja");
		}
		return lblCoNumcaja;
	}

	protected HtmlColumn getCoNombrecaja() {
		if (coNombrecaja == null) {
			coNombrecaja = (HtmlColumn) findComponentInRoot("coNombrecaja");
		}
		return coNombrecaja;
	}

	protected HtmlOutputText getLbletNocaja() {
		if (lbletNocaja == null) {
			lbletNocaja = (HtmlOutputText) findComponentInRoot("lbletNocaja");
		}
		return lbletNocaja;
	}

	protected HtmlOutputText getLblCoNomcaja() {
		if (lblCoNomcaja == null) {
			lblCoNomcaja = (HtmlOutputText) findComponentInRoot("lblCoNomcaja");
		}
		return lblCoNomcaja;
	}

	protected HtmlOutputText getLblEtNomcaja() {
		if (lblEtNomcaja == null) {
			lblEtNomcaja = (HtmlOutputText) findComponentInRoot("lblEtNomcaja");
		}
		return lblEtNomcaja;
	}

	protected HtmlColumn getCoCodsuc() {
		if (coCodsuc == null) {
			coCodsuc = (HtmlColumn) findComponentInRoot("coCodsuc");
		}
		return coCodsuc;
	}

	protected HtmlOutputText getLblCoCodsuc() {
		if (lblCoCodsuc == null) {
			lblCoCodsuc = (HtmlOutputText) findComponentInRoot("lblCoCodsuc");
		}
		return lblCoCodsuc;
	}

	protected HtmlOutputText getLblEtCodsuc() {
		if (lblEtCodsuc == null) {
			lblEtCodsuc = (HtmlOutputText) findComponentInRoot("lblEtCodsuc");
		}
		return lblEtCodsuc;
	}

	protected HtmlColumn getCoNomsuc() {
		if (coNomsuc == null) {
			coNomsuc = (HtmlColumn) findComponentInRoot("coNomsuc");
		}
		return coNomsuc;
	}

	protected HtmlColumn getCoCajero() {
		if (coCajero == null) {
			coCajero = (HtmlColumn) findComponentInRoot("coCajero");
		}
		return coCajero;
	}

	protected HtmlOutputText getLblCoNomsuc() {
		if (lblCoNomsuc == null) {
			lblCoNomsuc = (HtmlOutputText) findComponentInRoot("lblCoNomsuc");
		}
		return lblCoNomsuc;
	}

	protected HtmlOutputText getLblEtNomsuc() {
		if (lblEtNomsuc == null) {
			lblEtNomsuc = (HtmlOutputText) findComponentInRoot("lblEtNomsuc");
		}
		return lblEtNomsuc;
	}

	protected HtmlOutputText getLblCoCajero() {
		if (lblCoCajero == null) {
			lblCoCajero = (HtmlOutputText) findComponentInRoot("lblCoCajero");
		}
		return lblCoCajero;
	}

	protected HtmlOutputText getLblEtCajero() {
		if (lblEtCajero == null) {
			lblEtCajero = (HtmlOutputText) findComponentInRoot("lblEtCajero");
		}
		return lblEtCajero;
	}

	protected HtmlColumn getCoContador() {
		if (coContador == null) {
			coContador = (HtmlColumn) findComponentInRoot("coContador");
		}
		return coContador;
	}

	protected HtmlOutputText getLblCoContador() {
		if (lblCoContador == null) {
			lblCoContador = (HtmlOutputText) findComponentInRoot("lblCoContador");
		}
		return lblCoContador;
	}

	protected HtmlOutputText getLblEtContador() {
		if (lblEtContador == null) {
			lblEtContador = (HtmlOutputText) findComponentInRoot("lblEtContador");
		}
		return lblEtContador;
	}

	protected HtmlLink getLnkFiltrarCajasParaEnvio() {
		if (lnkFiltrarCajasParaEnvio == null) {
			lnkFiltrarCajasParaEnvio = (HtmlLink) findComponentInRoot("lnkFiltrarCajasParaEnvio");
		}
		return lnkFiltrarCajasParaEnvio;
	}

	protected HtmlColumnSelectRow getCsrprueba() {
		if (csrprueba == null) {
			csrprueba = (HtmlColumnSelectRow) findComponentInRoot("csrprueba");
		}
		return csrprueba;
	}

	protected HtmlLink getLnkAceptarEnvioFac() {
		if (lnkAceptarEnvioFac == null) {
			lnkAceptarEnvioFac = (HtmlLink) findComponentInRoot("lnkAceptarEnvioFac");
		}
		return lnkAceptarEnvioFac;
	}

	protected HtmlLink getLnkCancelarEnvioFac() {
		if (lnkCancelarEnvioFac == null) {
			lnkCancelarEnvioFac = (HtmlLink) findComponentInRoot("lnkCancelarEnvioFac");
		}
		return lnkCancelarEnvioFac;
	}

	protected HtmlOutputText getLblValidaEnvioFac() {
		if (lblValidaEnvioFac == null) {
			lblValidaEnvioFac = (HtmlOutputText) findComponentInRoot("lblValidaEnvioFac");
		}
		return lblValidaEnvioFac;
	}

	protected HtmlLink getLnkCrearNotaCreditoCkOk() {
		if (lnkCrearNotaCreditoCkOk == null) {
			lnkCrearNotaCreditoCkOk = (HtmlLink) findComponentInRoot("lnkCrearNotaCreditoCkOk");
		}
		return lnkCrearNotaCreditoCkOk;
	}

	protected HtmlDialogWindow getDwCrearNotaCredito() {
		if (dwCrearNotaCredito == null) {
			dwCrearNotaCredito = (HtmlDialogWindow) findComponentInRoot("dwCrearNotaCredito");
		}
		return dwCrearNotaCredito;
	}

	protected HtmlJspPanel getJspdwCrearNotaCreditoMensaje() {
		if (jspdwCrearNotaCreditoMensaje == null) {
			jspdwCrearNotaCreditoMensaje = (HtmlJspPanel) findComponentInRoot("jspdwCrearNotaCreditoMensaje");
		}
		return jspdwCrearNotaCreditoMensaje;
	}

	protected HtmlDialogWindowClientEvents getCleCrearNotaCredito() {
		if (cleCrearNotaCredito == null) {
			cleCrearNotaCredito = (HtmlDialogWindowClientEvents) findComponentInRoot("cleCrearNotaCredito");
		}
		return cleCrearNotaCredito;
	}

	protected HtmlDialogWindowHeader getHdCrearNotaCredito() {
		if (hdCrearNotaCredito == null) {
			hdCrearNotaCredito = (HtmlDialogWindowHeader) findComponentInRoot("hdCrearNotaCredito");
		}
		return hdCrearNotaCredito;
	}

	protected HtmlDialogWindowRoundedCorners getRcCrearNotaCredito() {
		if (rcCrearNotaCredito == null) {
			rcCrearNotaCredito = (HtmlDialogWindowRoundedCorners) findComponentInRoot("rcCrearNotaCredito");
		}
		return rcCrearNotaCredito;
	}

	protected HtmlDialogWindowContentPane getCpCrearNotaCredito() {
		if (cpCrearNotaCredito == null) {
			cpCrearNotaCredito = (HtmlDialogWindowContentPane) findComponentInRoot("cpCrearNotaCredito");
		}
		return cpCrearNotaCredito;
	}

	protected HtmlDialogWindowAutoPostBackFlags getApbCrearNotaCredito() {
		if (apbCrearNotaCredito == null) {
			apbCrearNotaCredito = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbCrearNotaCredito");
		}
		return apbCrearNotaCredito;
	}

	protected HtmlLink getLnkCerrarCrearNotaCredito() {
		if (lnkCerrarCrearNotaCredito == null) {
			lnkCerrarCrearNotaCredito = (HtmlLink) findComponentInRoot("lnkCerrarCrearNotaCredito");
		}
		return lnkCerrarCrearNotaCredito;
	}

	protected HtmlJspPanel getJspPanelCrearNotaCredito() {
		if (jspPanelCrearNotaCredito == null) {
			jspPanelCrearNotaCredito = (HtmlJspPanel) findComponentInRoot("jspPanelCrearNotaCredito");
		}
		return jspPanelCrearNotaCredito;
	}

	protected HtmlOutputText getMsgCodigoCliente() {
		if (msgCodigoCliente == null) {
			msgCodigoCliente = (HtmlOutputText) findComponentInRoot("mCodigoCliente");
		}
		return msgCodigoCliente;
	}

	protected HtmlInputText getTxtCodigoCliente1() {
		if (txtCodigoCliente1 == null) {
			txtCodigoCliente1 = (HtmlInputText) findComponentInRoot("txtCodigoCliente1");
		}
		return txtCodigoCliente1;
	}

	protected HtmlLink getLnkDwCrearNotaCredito() {
		if (lnkDwCrearNotaCredito == null) {
			lnkDwCrearNotaCredito = (HtmlLink) findComponentInRoot("lnkDwCrearNotaCredito");
		}
		return lnkDwCrearNotaCredito;
	}

	protected HtmlLink getLnkDwActInfoCliente() {
		if (lnkDwActInfoCliente == null) {
			lnkDwActInfoCliente = (HtmlLink) findComponentInRoot("lnkDwActInfoCliente");
		}
		return lnkDwActInfoCliente;
	}

	protected HtmlOutputText getStrInfoCliente() {
		if (strInfoCliente == null) {
			strInfoCliente = (HtmlOutputText) findComponentInRoot("strInfoCliente");
		}
		return strInfoCliente;
	}

	protected HtmlOutputText getLblValidaTraerFac() {
		if (lblValidaTraerFac == null) {
			lblValidaTraerFac = (HtmlOutputText) findComponentInRoot("lblValidaTraerFac");
		}
		return lblValidaTraerFac;
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

	protected HtmlOutputText getLblMensajeCargando() {
		if (lblMensajeCargando == null) {
			lblMensajeCargando = (HtmlOutputText) findComponentInRoot("lblMensajeCargando");
		}
		return lblMensajeCargando;
	}

	protected HtmlOutputText getLblEtTrasladoPOS() {
		if (lblEtTrasladoPOS == null) {
			lblEtTrasladoPOS = (HtmlOutputText) findComponentInRoot("lblEtTrasladoPOS");
		}
		return lblEtTrasladoPOS;
	}

	protected HtmlCheckBox getChkTrasladarPOS() {
		if (chkTrasladarPOS == null) {
			chkTrasladarPOS = (HtmlCheckBox) findComponentInRoot("chkTrasladarPOS");
		}
		return chkTrasladarPOS;
	}

	protected HtmlLink getLnkAceptarTraerFac() {
		if (lnkAceptarTraerFac == null) {
			lnkAceptarTraerFac = (HtmlLink) findComponentInRoot("lnkAceptarTraerFac");
		}
		return lnkAceptarTraerFac;
	}

	protected HtmlLink getLnkCancelarTraerFac() {
		if (lnkCancelarTraerFac == null) {
			lnkCancelarTraerFac = (HtmlLink) findComponentInRoot("lnkCancelarTraerFac");
		}
		return lnkCancelarTraerFac;
	}

	protected HtmlOutputText getLblCodigoCliente() {
		if (lblCodigoCliente == null) {
			lblCodigoCliente = (HtmlOutputText) findComponentInRoot("lblCodigoCliente");
		}
		return lblCodigoCliente;
	}

	protected HtmlOutputText getLblCodigoClienteMSG1() {
		if (lblCodigoClienteMSG1 == null) {
			lblCodigoClienteMSG1 = (HtmlOutputText) findComponentInRoot("lblCodigoClienteMSG1");
		}
		return lblCodigoClienteMSG1;
	}

	protected HtmlGridView getGvClienteFacsRM() {
		if (gvClienteFacsRM == null) {
			gvClienteFacsRM = (HtmlGridView) findComponentInRoot("gvClienteFacsRM");
		}
		return gvClienteFacsRM;
	}

	protected HtmlColumnSelectRow getColumnSelectRowRendererCred() {
		if (columnSelectRowRendererCred == null) {
			columnSelectRowRendererCred = (HtmlColumnSelectRow) findComponentInRoot("columnSelectRowRendererCred");
		}
		return columnSelectRowRendererCred;
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

	protected HtmlDropDownList getDdlAfiliado() {
		if (ddlAfiliado == null) {
			ddlAfiliado = (HtmlDropDownList) findComponentInRoot("ddlAfiliado");
		}
		return ddlAfiliado;
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

	protected HtmlDialogWindowHeader getHdFacTraslado() {
		if (hdFacTraslado == null) {
			hdFacTraslado = (HtmlDialogWindowHeader) findComponentInRoot("hdFacTraslado");
		}
		return hdFacTraslado;
	}

	protected HtmlJspPanel getJpTraerFactura() {
		if (jpTraerFactura == null) {
			jpTraerFactura = (HtmlJspPanel) findComponentInRoot("jpTraerFactura");
		}
		return jpTraerFactura;
	}

	protected HtmlPanelGrid getPgTraeFactura() {
		if (pgTraeFactura == null) {
			pgTraeFactura = (HtmlPanelGrid) findComponentInRoot("pgTraeFactura");
		}
		return pgTraeFactura;
	}

	protected HtmlOutputText getLblEtFiltroFacs() {
		if (lblEtFiltroFacs == null) {
			lblEtFiltroFacs = (HtmlOutputText) findComponentInRoot("lblEtFiltroFacs");
		}
		return lblEtFiltroFacs;
	}

	protected HtmlColumnSelectRow getCsrSelFac() {
		if (csrSelFac == null) {
			csrSelFac = (HtmlColumnSelectRow) findComponentInRoot("csrSelFac");
		}
		return csrSelFac;
	}

	protected HtmlOutputText getLblCoNumFac() {
		if (lblCoNumFac == null) {
			lblCoNumFac = (HtmlOutputText) findComponentInRoot("lblCoNumFac");
		}
		return lblCoNumFac;
	}

	protected HtmlOutputText getLblEtNumFac() {
		if (lblEtNumFac == null) {
			lblEtNumFac = (HtmlOutputText) findComponentInRoot("lblEtNumFac");
		}
		return lblEtNumFac;
	}

	protected HtmlOutputText getLblCoTipoFac() {
		if (lblCoTipoFac == null) {
			lblCoTipoFac = (HtmlOutputText) findComponentInRoot("lblCoTipoFac");
		}
		return lblCoTipoFac;
	}

	protected HtmlOutputText getLblEtTipoFac() {
		if (lblEtTipoFac == null) {
			lblEtTipoFac = (HtmlOutputText) findComponentInRoot("lblEtTipoFac");
		}
		return lblEtTipoFac;
	}

	protected HtmlOutputText getLblCoClienteFac() {
		if (lblCoClienteFac == null) {
			lblCoClienteFac = (HtmlOutputText) findComponentInRoot("lblCoClienteFac");
		}
		return lblCoClienteFac;
	}

	protected HtmlOutputText getLblEtClienteFac() {
		if (lblEtClienteFac == null) {
			lblEtClienteFac = (HtmlOutputText) findComponentInRoot("lblEtClienteFac");
		}
		return lblEtClienteFac;
	}

	protected HtmlOutputText getLblCoFacUnieg() {
		if (lblCoFacUnieg == null) {
			lblCoFacUnieg = (HtmlOutputText) findComponentInRoot("lblCoFacUnieg");
		}
		return lblCoFacUnieg;
	}

	protected HtmlOutputText getLblEtFacUnineg() {
		if (lblEtFacUnineg == null) {
			lblEtFacUnineg = (HtmlOutputText) findComponentInRoot("lblEtFacUnineg");
		}
		return lblEtFacUnineg;
	}

	protected HtmlOutputText getLblCoFacTotal() {
		if (lblCoFacTotal == null) {
			lblCoFacTotal = (HtmlOutputText) findComponentInRoot("lblCoFacTotal");
		}
		return lblCoFacTotal;
	}

	protected HtmlOutputText getLblEtFacTotal() {
		if (lblEtFacTotal == null) {
			lblEtFacTotal = (HtmlOutputText) findComponentInRoot("lblEtFacTotal");
		}
		return lblEtFacTotal;
	}

	protected HtmlOutputText getLblCoFacMoneda() {
		if (lblCoFacMoneda == null) {
			lblCoFacMoneda = (HtmlOutputText) findComponentInRoot("lblCoFacMoneda");
		}
		return lblCoFacMoneda;
	}

	protected HtmlOutputText getLblEtFacMoneda() {
		if (lblEtFacMoneda == null) {
			lblEtFacMoneda = (HtmlOutputText) findComponentInRoot("lblEtFacMoneda");
		}
		return lblEtFacMoneda;
	}

	protected HtmlColumnSelectRow getColumnSelectRowRenderer1() {
		if (columnSelectRowRenderer1 == null) {
			columnSelectRowRenderer1 = (HtmlColumnSelectRow) findComponentInRoot("columnSelectRowRenderer1");
		}
		return columnSelectRowRenderer1;
	}

	protected HtmlDialogWindow getDwFacturasTraslado() {
		if (dwFacturasTraslado == null) {
			dwFacturasTraslado = (HtmlDialogWindow) findComponentInRoot("dwFacturasTraslado");
		}
		return dwFacturasTraslado;
	}

	protected HtmlDialogWindowClientEvents getCleFacTraslado() {
		if (cleFacTraslado == null) {
			cleFacTraslado = (HtmlDialogWindowClientEvents) findComponentInRoot("cleFacTraslado");
		}
		return cleFacTraslado;
	}

	protected HtmlDialogWindowRoundedCorners getRcFacTraslado() {
		if (rcFacTraslado == null) {
			rcFacTraslado = (HtmlDialogWindowRoundedCorners) findComponentInRoot("rcFacTraslado");
		}
		return rcFacTraslado;
	}

	protected HtmlDialogWindowContentPane getCpFacTraslado() {
		if (cpFacTraslado == null) {
			cpFacTraslado = (HtmlDialogWindowContentPane) findComponentInRoot("cpFacTraslado");
		}
		return cpFacTraslado;
	}

	protected HtmlDropDownList getDdlFiltroFacturaTras() {
		if (ddlFiltroFacturaTras == null) {
			ddlFiltroFacturaTras = (HtmlDropDownList) findComponentInRoot("ddlFiltroFacturaTras");
		}
		return ddlFiltroFacturaTras;
	}

	protected HtmlInputText getTxtParamFiltrofac() {
		if (txtParamFiltrofac == null) {
			txtParamFiltrofac = (HtmlInputText) findComponentInRoot("txtParamFiltrofac");
		}
		return txtParamFiltrofac;
	}

	protected HtmlOutputText getLblEtFcajas() {
		if (lblEtFcajas == null) {
			lblEtFcajas = (HtmlOutputText) findComponentInRoot("lblEtFcajas");
		}
		return lblEtFcajas;
	}

	protected HtmlDropDownList getDdlFiltroCajasTras() {
		if (ddlFiltroCajasTras == null) {
			ddlFiltroCajasTras = (HtmlDropDownList) findComponentInRoot("ddlFiltroCajasTras");
		}
		return ddlFiltroCajasTras;
	}

	protected HtmlLink getLnkFiltrarFacturaParaEnvio() {
		if (lnkFiltrarFacturaParaEnvio == null) {
			lnkFiltrarFacturaParaEnvio = (HtmlLink) findComponentInRoot("lnkFiltrarFacturaParaEnvio");
		}
		return lnkFiltrarFacturaParaEnvio;
	}

	protected HtmlGridView getGvFacturasParaTraslado() {
		if (gvFacturasParaTraslado == null) {
			gvFacturasParaTraslado = (HtmlGridView) findComponentInRoot("gvFacturasParaTraslado");
		}
		return gvFacturasParaTraslado;
	}

	protected HtmlColumn getCoNumFac() {
		if (coNumFac == null) {
			coNumFac = (HtmlColumn) findComponentInRoot("coNumFac");
		}
		return coNumFac;
	}

	protected HtmlColumn getCoTipoFac() {
		if (coTipoFac == null) {
			coTipoFac = (HtmlColumn) findComponentInRoot("coTipoFac");
		}
		return coTipoFac;
	}

	protected HtmlColumn getCoClienteFac() {
		if (coClienteFac == null) {
			coClienteFac = (HtmlColumn) findComponentInRoot("coClienteFac");
		}
		return coClienteFac;
	}

	protected HtmlColumn getCoFacUnineg() {
		if (coFacUnineg == null) {
			coFacUnineg = (HtmlColumn) findComponentInRoot("coFacUnineg");
		}
		return coFacUnineg;
	}

	protected HtmlColumn getCoFacTotal() {
		if (coFacTotal == null) {
			coFacTotal = (HtmlColumn) findComponentInRoot("coFacTotal");
		}
		return coFacTotal;
	}

	protected HtmlColumn getCoFacMoneda() {
		if (coFacMoneda == null) {
			coFacMoneda = (HtmlColumn) findComponentInRoot("coFacMoneda");
		}
		return coFacMoneda;
	}

}
/**
 * 
 */
package pagecode.cierre;

import pagecode.PageCodeBase;
import com.ibm.faces.component.html.HtmlScriptCollector;
import javax.faces.component.html.HtmlForm;
import com.infragistics.faces.menu.component.html.HtmlMenu;
import com.infragistics.faces.menu.component.html.HtmlMenuItem;
import javax.faces.component.html.HtmlOutputText;
import com.infragistics.faces.shared.component.html.HtmlLink;
import com.infragistics.faces.input.component.html.HtmlDateChooser;
import com.infragistics.faces.grid.component.html.HtmlGridView;
import com.infragistics.faces.grid.component.html.HtmlColumn;
import javax.faces.component.UINamingContainer;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
import com.infragistics.faces.window.component.html.HtmlDialogWindowHeader;
import com.ibm.faces.component.html.HtmlJspPanel;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlPanelGrid;
import com.infragistics.faces.window.component.html.HtmlDialogWindow;
import com.infragistics.faces.window.component.html.HtmlDialogWindowClientEvents;
import com.infragistics.faces.window.component.html.HtmlDialogWindowRoundedCorners;
import com.infragistics.faces.window.component.html.HtmlDialogWindowContentPane;
import com.infragistics.faces.window.component.html.HtmlDialogWindowAutoPostBackFlags;
import javax.faces.component.html.HtmlInputTextarea;
import com.ibm.faces.component.html.HtmlGraphicImageEx;
import com.infragistics.faces.input.component.html.HtmlCheckBox;
import javax.faces.component.html.HtmlPanelGroup;
import com.infragistics.faces.grid.component.html.HtmlGridAgFunction;

/**
 * @author Juan Ñamendi
 *
 */
public class RevisionArqueo extends PageCodeBase {

	protected HtmlScriptCollector scriptCollector1;
	protected HtmlForm frmRevisionArqueo;
	protected HtmlMenu menu1;
	protected HtmlMenuItem item0;
	protected HtmlMenuItem item1;
	protected HtmlMenuItem item2;
	protected HtmlOutputText lblTitRevisionArqueo0;
	protected HtmlLink lnkFiltrarxFecha;
	protected HtmlDateChooser dcFechaArqueo;
	protected HtmlOutputText lblMensaje;
	protected HtmlOutputText lblHeader;
	protected UINamingContainer vfRevisionArqueo;
	protected HtmlOutputText lblTitRevisionArqueo1;
	protected HtmlGridView gvArqueosPendRev;
	protected HtmlColumn coLnkDetalle;
	protected HtmlLink lnkDetalleArqueoCaja;
	protected HtmlOutputText lblDetalleArqueoCaja;
	protected HtmlOutputText lblcaid0;
	protected HtmlOutputText lblcaid1;
	protected HtmlColumn coNoCaja;
	protected HtmlOutputText lblcodcajero0;
	protected HtmlOutputText lblcodcajero1;
	protected HtmlColumn coCajeroId;
	protected HtmlOutputText lblcodcomp0;
	protected HtmlOutputText lblcodcomp1;
	protected HtmlColumn coCodcomp;
	protected HtmlOutputText lblnoarqueo0;
	protected HtmlOutputText lblnoarqueo1;
	protected HtmlColumn coNoarqueo;
	protected HtmlColumn cofecha;
	protected HtmlColumn cohora;
	protected HtmlOutputText lblmoneda0;
	protected HtmlOutputText lblmoneda1;
	protected HtmlColumn coMoneda;
	protected HtmlOutputText lbldsugerido0;
	protected HtmlOutputText lbldsugerido1;
	protected HtmlOutputText lblfooter0;
	protected HtmlColumn coDepSug;
	protected HtmlOutputText lbldsf0;
	protected HtmlOutputText lblsf1;
	protected HtmlColumn coSobFal;
	protected HtmlLink lnkRefrescarVistaContado;
	protected HtmlOutputText lblFiltroCaja;
	protected HtmlDropDownList ddlFiltroCaja;
	protected HtmlOutputText lblFiltroComp;
	protected HtmlDropDownList ddlFiltroCompania;
	protected HtmlOutputText lblFiltroMoneda;
	protected HtmlDropDownList ddlFiltroMoneda;
	protected HtmlOutputText lblFiltroEsado;
	protected HtmlDropDownList ddlFiltroEstado;
	protected HtmlDialogWindowHeader hdDetArqueo;
	protected HtmlJspPanel jspDetArqueo;
	protected HtmlOutputText lbletnocaja;
	protected HtmlOutputText lblnocaja;
	protected HtmlOutputText lbletsucursal;
	protected HtmlOutputText lblsucursal;
	protected HtmlOutputText lbletnoarqueo;
	protected HtmlOutputText lblnoarqueo;
	protected HtmlOutputText lbletcajero;
	protected HtmlOutputText lblcajero;
	protected HtmlOutputText lbletCompania;
	protected HtmlOutputText lblCompania;
	protected HtmlOutputText lbletMoneda;
	protected HtmlOutputText lblMoneda;
	protected HtmlOutputText lblTitIngresos;
	protected HtmlLink lnkDetalleVtasDia;
	protected HtmlOutputText lblEtiqVentasTotales;
	protected HtmlOutputText lblVentasTotales;
	protected HtmlLink lnkDetalleFactDevoluciones;
	protected HtmlOutputText lblEtiqDevoluciones;
	protected HtmlOutputText lblTotalDevoluciones;
	protected HtmlLink lnkDetalleVtasCredito;
	protected HtmlOutputText lblEtiqVtsCredito;
	protected HtmlOutputText lblTotalVtsCredito;
	protected HtmlOutputText lblEtiqVtasNetas;
	protected HtmlOutputText lblTotalVentasNetas;
	protected HtmlLink lnkDetalleAbonos;
	protected HtmlOutputText lblEtiqAbonos;
	protected HtmlOutputText lblTotalAbonos;
	protected HtmlLink lnkDetalleFinanciamiento;
	protected HtmlOutputText lblEtiqFinan;
	protected HtmlOutputText lblTotalFinan;
	protected HtmlLink lnkDetallePrimasReservas;
	protected HtmlOutputText lblEtiqPrimas;
	protected HtmlOutputText lblTotalPrimas;
	protected HtmlLink lnkDetalleIngresosEx;
	protected HtmlOutputText lblEtiqIngEx;
	protected HtmlOutputText lblTotalIngex;
	protected HtmlLink lnkDetIngxRecPagMonEx;
	protected HtmlOutputText lblEtIngRecxmonex;
	protected HtmlOutputText lblTotalIngRecxmonex;
	protected HtmlLink lnkDetalleOtrosIngresos;
	protected HtmlOutputText lblEtiqOtrosIngresos;
	protected HtmlOutputText lblCambioOtraMoneda;
	protected HtmlOutputText lblEtiTotaIngresos;
	protected HtmlOutputText lblTotaIngresos;
	protected HtmlOutputText lblTitEgresos;
	protected HtmlLink lnkvtspbanco;
	protected HtmlOutputText lbletvtspagbanco;
	protected HtmlOutputText lblTotalVtsPagBanco;
	protected HtmlLink lnkabonospbanco;
	protected HtmlOutputText lbletabonospagbanco;
	protected HtmlOutputText lblTotalAbonoPagBanco;
	protected HtmlLink lnkFinanpbanco;
	protected HtmlOutputText lbletFinanpagbanco;
	protected HtmlOutputText lblTotalFinanPagBanco;
	protected HtmlLink lnkprimaspbanco;
	protected HtmlOutputText lbletprimaspagbanco;
	protected HtmlOutputText lblTotalPrimasPagBanco;
	protected HtmlLink lnkingresosexpbanco;
	protected HtmlOutputText lbletIngExpagbanco;
	protected HtmlOutputText lblTotalIexPagBanco;
	protected HtmlLink lnkOtrosEgresosCaja;
	protected HtmlOutputText lbletDetOtrosEgresos;
	protected HtmlOutputText lblTotalOtrosEgresos;
	protected HtmlOutputText lblEtTotalEgresos;
	protected HtmlOutputText lblTotalEgresos;
	protected HtmlOutputText lblTitCalculoDepCaja;
	protected HtmlOutputText lblet_efectNetoCaja;
	protected HtmlOutputText lblCDC_efectnetoRec;
	protected HtmlOutputText lblet_efectCaja;
	protected HtmlOutputText lblbCDC_efectivoenCaja;
	protected HtmlOutputText lblet_pagochk;
	protected HtmlOutputText lblCDC_pagochks;
	protected HtmlOutputText lblet_SobranteFaltante;
	protected HtmlOutputText lblCDC_SobranteFaltante;
	protected HtmlOutputText lblet_montominimo;
	protected HtmlOutputText lblCDC_montominimo;
	protected HtmlOutputText lblet_depositoFinal;
	protected HtmlOutputText lblCDC_depositoFinal;
	protected HtmlOutputText lblet_DepositoSug;
	protected HtmlOutputText lblCDC_depositoSug;
	protected HtmlOutputText lblet_ReferenciaDeposito;
	protected HtmlInputText txtCDC_ReferDeposito;
	protected HtmlLink lnkCalDepCaja;
	protected HtmlOutputText lblac_AyudaPagosRec;
	protected HtmlLink lnkACDetfp_Efectivo;
	protected HtmlOutputText lblACDetfp_etEfectivo;
	protected HtmlOutputText lblACDetfp_Efectivo;
	protected HtmlLink lnkACDetfp_Cheques;
	protected HtmlOutputText lblACDetfp_etCheques;
	protected HtmlOutputText lblACDetfp_Cheques;
	protected HtmlLink lnkACDetfp_TarCred;
	protected HtmlOutputText lblACDetfp_etTarCredito;
	protected HtmlOutputText lblACDetfp_TarCred;
	protected HtmlLink lnkACDetfp_TarCredManual;
	protected HtmlOutputText lblACDetfp_etTarCreditoManual;
	protected HtmlOutputText lblACDetfp_TCmanual;
	protected HtmlLink lnkACDetfp_DepDbanco;
	protected HtmlOutputText lblACDetfp_etDepDbanco;
	protected HtmlOutputText lblACDetfp_DepDbanco;
	protected HtmlLink lnkACDetfp_TransBanco;
	protected HtmlOutputText lblACDetfp_etTransBanco;
	protected HtmlOutputText lblACDetfp_TransBanco;
	protected HtmlOutputText lblACDetfp_etTotal;
	protected HtmlOutputText lblACDetfp_Total;
	protected HtmlOutputText lblEtaprobacionArqueo;
	protected HtmlOutputText lbletDepSugerido;
	protected HtmlOutputText lblaprobDepSugerido;
	protected HtmlPanelGrid hpgOpciones1;
	protected HtmlLink lnkAprobarArqueo;
	protected HtmlLink lnkAsignarIdPOS;
	protected HtmlDialogWindow dwDetalleArqueo;
	protected HtmlDialogWindowClientEvents ceDetArqueo;
	protected HtmlDialogWindowRoundedCorners crDetArqueo;
	protected HtmlDialogWindowContentPane cnpDetArqueo;
	protected HtmlLink lnkponerReferenciaPOS;
	protected HtmlLink lnkReimpresionRptArqueo;
	protected HtmlPanelGrid hpgOpciones2;
	protected HtmlLink lnkCerrarDetalleArqueo;
	protected HtmlDialogWindowHeader hdFactura;
	protected HtmlGridView rv_gvFacturaRegistradas;
	protected HtmlLink lnkDetalleFactura;
	protected HtmlOutputText lblNoFactura1;
	protected HtmlOutputText lblNoFactura2sd3;
	protected HtmlOutputText lblEtCantFacC0;
	protected HtmlOutputText lblTipofactura1;
	protected HtmlOutputText lblTipofactura2;
	protected HtmlOutputText lblEtCantFacCr;
	protected HtmlOutputText lblTipoPagofact1;
	protected HtmlOutputText lblTipopagofact2;
	protected HtmlOutputText lblEtTotalFacCo;
	protected HtmlOutputText lblUnineg1;
	protected HtmlOutputText lblUnineg2;
	protected HtmlOutputText lblEtTotalFacCr;
	protected HtmlOutputText lblPartida22;
	protected HtmlOutputText lblPartida23;
	protected HtmlOutputText lblFecha22;
	protected HtmlOutputText lblFecha23;
	protected HtmlLink lnkCerrarFacturasReg;
	protected HtmlDialogWindow rv_dwFacturas;
	protected HtmlDialogWindowClientEvents clFacturasRegistradas;
	protected HtmlDialogWindowRoundedCorners crFacturas;
	protected HtmlDialogWindowContentPane cnpFacturas;
	protected HtmlColumn coNoFactura2sdf33;
	protected HtmlOutputText lblCantFacCO;
	protected HtmlColumn coTipofactura2;
	protected HtmlOutputText lblCantFacCr;
	protected HtmlColumn coTipoPagofact;
	protected HtmlOutputText lblTotalFacCo;
	protected HtmlColumn coUnineg2;
	protected HtmlOutputText lblTotalFacCr;
	protected HtmlColumn coTotal;
	protected HtmlColumn coFecha2;
	protected HtmlPanelGrid hpgFacturasReg1;
	protected HtmlDialogWindowHeader hdDetTrecxMetPago;
	protected HtmlGridView gvRecibosxTipoyMetodopago;
	protected HtmlOutputText lblRcTmp_numrec;
	protected HtmlOutputText lblRcTmp_tiporec;
	protected HtmlOutputText lblRcTmp_cliente;
	protected HtmlOutputText lblRcTmp_monto;
	protected HtmlOutputText lblRcTmp_montoapl;
	protected HtmlOutputText lblRcTmp_hora;
	protected HtmlOutputText lblRcTmp_Refer1;
	protected HtmlOutputText lblRcTmp_Refer2;
	protected HtmlOutputText lblRcTmp_refer4;
	protected HtmlLink lnkCerrarDetRecxtipoyMetpago;
	protected HtmlDialogWindow rv_dwReciboxtipoMetPago;
	protected HtmlDialogWindowClientEvents clDetTrecxMetPago;
	protected HtmlDialogWindowRoundedCorners crDetTrecxMetPago;
	protected HtmlDialogWindowContentPane cnpDetTrecxMetPago;
	protected HtmlDialogWindowHeader hdRecxTipoIng;
	protected HtmlGridView gvRecibosIngresos;
	protected HtmlLink lnkDetalleRecibo;
	protected HtmlOutputText lblVNC_numrec;
	protected HtmlOutputText lblVNC_cliente0;
	protected HtmlOutputText lblVNC_cliente1;
	protected HtmlOutputText lblVNC_hora0;
	protected HtmlOutputText lblVNC_hora1;
	protected HtmlOutputText lblVNC_monto0;
	protected HtmlOutputText lblVNC_monto1;
	protected HtmlLink lnkCerrarDetRecxTipoIng;
	protected HtmlDialogWindow dwRecibosxTipoIngreso;
	protected HtmlDialogWindowClientEvents ceRecxTipoIng;
	protected HtmlDialogWindowRoundedCorners crRecxTipoIng;
	protected HtmlDialogWindowContentPane cnpRecxTipoIng;
	protected HtmlColumn coHora;
	protected HtmlPanelGrid hpgDetRecibosxTipoIng;
	protected HtmlDialogWindowHeader hdDetalleFactura;
	protected HtmlJspPanel jspPanel4;
	protected HtmlOutputText text18;
	protected HtmlOutputText text20;
	protected HtmlOutputText lblCodigo23;
	protected HtmlOutputText txtMonedaContado1;
	protected HtmlOutputText txtNomCliente;
	protected HtmlOutputText lblTasaDetalleCont;
	protected HtmlOutputText lblUninegDetalleCont;
	protected HtmlGridView gvDfacturasDiario;
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
	protected HtmlOutputText txtSubtotal;
	protected HtmlOutputText text28;
	protected HtmlOutputText txtIva;
	protected HtmlOutputText lblTotalDetCont;
	protected HtmlOutputText txtTotal;
	protected HtmlLink lnkCerrarDetalleContado;
	protected HtmlDialogWindow rv_dwDetalleFactura;
	protected HtmlDialogWindowClientEvents clDetalleFacturaCon;
	protected HtmlDialogWindowRoundedCorners crDetalleFacturaCon;
	protected HtmlDialogWindowContentPane cnpDetalleFacturaCon;
	protected HtmlOutputText txtFechaFactura;
	protected HtmlOutputText txtNoFactura;
	protected HtmlOutputText txtCodigoCliente;
	protected HtmlDropDownList ddlDetalleFacCon;
	protected HtmlOutputText text3333;
	protected HtmlOutputText txtCodUnineg;
	protected HtmlOutputText text23;
	protected HtmlColumn coDescitemCont;
	protected HtmlColumn coCant;
	protected HtmlColumn coPreciounit;
	protected HtmlColumn coImpuesto;
	protected HtmlDialogWindowAutoPostBackFlags apbDetalleFactura;
	protected HtmlDialogWindowHeader hdDetalleRecibo;
	protected HtmlJspPanel jspPanel5;
	protected HtmlOutputText lblCodCli;
	protected HtmlOutputText txtDRMonedaRecibo;
	protected HtmlOutputText txtDRNomCliente;
	protected HtmlGridView rv_gvDetalleRecibo;
	protected HtmlColumn coDRCoditem;
	protected HtmlOutputText lblDRCoditem1;
	protected HtmlOutputText lblDRCoditem2;
	protected HtmlOutputText lblDRDescitem1;
	protected HtmlOutputText lblDRDescitem2;
	protected HtmlOutputText lblDRCantDetalle1;
	protected HtmlOutputText lblDRCantDetalle2;
	protected HtmlOutputText lblDRPrecionunitDetalle1;
	protected HtmlOutputText lblDRPrecionunitDetalle2;
	protected HtmlOutputText lblDRImpuestoDetalle1;
	protected HtmlOutputText lblDRImpuestoDetalle2;
	protected HtmlOutputText lblRefer11;
	protected HtmlOutputText lblRefer12;
	protected HtmlOutputText lblRefer21;
	protected HtmlOutputText lblRefer22;
	protected HtmlOutputText lblRefer31;
	protected HtmlOutputText lblRefer3;
	protected HtmlOutputText lblRefer32;
	protected HtmlOutputText lblRefer4;
	protected HtmlGridView rv_gvFacturasRecibo;
	protected HtmlOutputText lblDRNoFactura1;
	protected HtmlOutputText lblDRTipofactura1;
	protected HtmlOutputText lblDRTipofactura2;
	protected HtmlOutputText lblDRUnineg1;
	protected HtmlOutputText lblDRUnineg2;
	protected HtmlOutputText lblDRMoneda1;
	protected HtmlOutputText lblDRMoneda2;
	protected HtmlOutputText lblDRFecha22;
	protected HtmlOutputText lblDRFecha23;
	protected HtmlOutputText lblDRPartida22;
	protected HtmlOutputText lblDRPartida23;
	protected HtmlOutputText lblConcepto;
	protected HtmlInputTextarea txtConcepto;
	protected HtmlOutputText lblDRSubtotalDetalleContado;
	protected HtmlOutputText txtDRSubtotalDetalle;
	protected HtmlOutputText txtDRIvaDetalle;
	protected HtmlOutputText lblDRTotalDetCont;
	protected HtmlOutputText txtDRTotalDetalle;
	protected HtmlLink lnkCerrarDetalleRecibo;
	protected HtmlDialogWindow rv_dwDetalleRecibo;
	protected HtmlDialogWindowClientEvents clDetalleContado;
	protected HtmlDialogWindowRoundedCorners crDetalle;
	protected HtmlDialogWindowContentPane cnpDetalle;
	protected HtmlOutputText txtHoraRecibo;
	protected HtmlOutputText txtNoRecibo;
	protected HtmlOutputText txtDRCodCli;
	protected HtmlOutputText txtNoBatch;
	protected HtmlColumn coDRDescitemCont;
	protected HtmlColumn coDRCant;
	protected HtmlColumn coDRPreciounit;
	protected HtmlColumn coDRImpuesto;
	protected HtmlColumn coRefer1;
	protected HtmlColumn coRefer2;
	protected HtmlColumn coRefer3;
	protected HtmlColumn coRefer4;
	protected HtmlColumn coDRTipofactura2;
	protected HtmlColumn coDRUnineg2;
	protected HtmlColumn coDRMoneda2;
	protected HtmlColumn coDRFecha2;
	protected HtmlColumn coPartida2;
	protected HtmlDialogWindowAutoPostBackFlags apbDetalle;
	protected HtmlDialogWindowHeader hdVarqueo;
	protected HtmlJspPanel jspVarqueo0;
	protected HtmlGraphicImageEx imgVarqueo;
	protected HtmlOutputText lblValidarAprobacion;
	protected HtmlLink lnkCerrarVarqueo;
	protected HtmlDialogWindow dwValidarAprobacionArqueo;
	protected HtmlDialogWindowClientEvents ceVarqueo;
	protected HtmlDialogWindowRoundedCorners rcVarqueo;
	protected HtmlDialogWindowContentPane cpVarqueo;
	protected HtmlDialogWindowAutoPostBackFlags apbReciboContado2;
	protected HtmlDialogWindowHeader hdAskCancel;
	protected HtmlPanelGrid gridAskCancel;
	protected HtmlGraphicImageEx imageEx2AskCancel;
	protected HtmlLink lnkCerrarAprobArqueoSi;
	protected HtmlDialogWindow rv_dwCancelarAprArqueo;
	protected HtmlDialogWindowClientEvents cleAskCancel;
	protected HtmlDialogWindowRoundedCorners rcAskCancel;
	protected HtmlDialogWindowContentPane cpAskCancel;
	protected HtmlOutputText lblConfirmCancel;
	protected HtmlJspPanel jspPanel3AskCancel;
	protected HtmlLink lnkCerrarAprobArqueoNo;
	protected HtmlDialogWindowAutoPostBackFlags apbAskCancel;
	protected HtmlDialogWindowHeader hdAprobarArqueo;
	protected HtmlPanelGrid gridAprobarArqueo;
	protected HtmlGraphicImageEx imageEx2AprobarArqueo;
	protected HtmlDialogWindow rv_dwConfirmarProcesarArq;
	protected HtmlDialogWindowClientEvents cleAprobarArqueo;
	protected HtmlDialogWindowRoundedCorners rcAprobarArqueo;
	protected HtmlDialogWindowContentPane cpAprobarArqueo;
	protected HtmlOutputText lblAprobarArqueo;
	protected HtmlJspPanel jspPanelAprobarArqueo;
	protected HtmlLink lnkAprobArqueoNo;
	protected HtmlDialogWindowAutoPostBackFlags apbAprobarArqueo;
	protected HtmlDialogWindowHeader hdRechazarArqueo;
	protected HtmlPanelGrid gridRechazarArqueo;
	protected HtmlGraphicImageEx imageExRechazarArqueo;
	protected HtmlOutputText lblMotivoRechazo;
	protected HtmlDialogWindow rv_dwRechazarArqueoCaja;
	protected HtmlDialogWindowClientEvents cleRechazarArqueo;
	protected HtmlDialogWindowRoundedCorners rcRechazarArqueo;
	protected HtmlDialogWindowContentPane cpRechazarArqueo;
	protected HtmlOutputText lblRechazarArqueo;
	protected HtmlPanelGrid gridRechazarArqueo1;
	protected HtmlInputTextarea txtMotivoRechazoArqueo;
	protected HtmlLink lnkAprobArqueoSi;
	protected HtmlLink lnkLiquidacionChk;
	protected HtmlOutputText lblNoFactura2;
	protected HtmlColumn coNoFactura2;
	protected HtmlOutputText lblRcTmp_refer3;
	protected HtmlLink lnkACDetfp_Tarsocketpos;
	protected HtmlOutputText lblACDetfp_etTarCsocketpos;
	protected HtmlOutputText lblACDetfp_TCsocketpos;
	protected HtmlLink lnkAnularArqueoCaja;
	protected HtmlOutputText lblMsgMotivoRechazo;
	protected HtmlLink lnkRechazarArqueoSi;
	protected HtmlJspPanel jspPanelRechazarArqueo;
	protected HtmlLink lnkARechazarArqueoNo;
	protected HtmlDialogWindowAutoPostBackFlags apbRechazarArqueo;
	protected HtmlDialogWindowHeader hdDetrecpagmonEx;
	protected HtmlGridView gvDetRecpagMonEx;
	protected HtmlOutputText lblRcpagMex_numrec;
	protected HtmlOutputText lblRcpagMex_tiporec;
	protected HtmlOutputText lblRcpagMex_cliente;
	protected HtmlOutputText lblRcpagMex_mpago;
	protected HtmlOutputText lblRcpagMex_monto;
	protected HtmlOutputText lblRcpagMex_equiv;
	protected HtmlOutputText lblRcpagMex_tasa;
	protected HtmlOutputText lblRcpagMex_Refer1;
	protected HtmlOutputText lblRcpagMex_Refer2;
	protected HtmlOutputText lblRcpagMex_refer3;
	protected HtmlOutputText lblRcpagMex_refer4;
	protected HtmlLink lnkCerrarDetRecPagMonEx;
	protected HtmlDialogWindow dwDetallerecpagmonEx;
	protected HtmlDialogWindowClientEvents clDetRecpagMonEx;
	protected HtmlDialogWindowRoundedCorners crDetRecpagMonEx;
	protected HtmlDialogWindowContentPane cnpDetRecpagMonEx;
	protected HtmlDialogWindowHeader hdEgrxmetp;
	protected HtmlJspPanel jspAPB3_1;
	protected HtmlOutputText lblDetalleMptc;
	protected HtmlOutputText lblDetalleMpdp;
	protected HtmlOutputText lblDetalleMptb;
	protected HtmlLink lnkDetpagoTarjetaCr;
	protected HtmlOutputText lblEtAbonoTCredito1;
	protected HtmlOutputText lblPagoTarjetaCredito;
	protected HtmlLink lnkDetpagoDepBanco;
	protected HtmlOutputText lblEtAbonoDDbanco1;
	protected HtmlOutputText lblPagoDepBanco;
	protected HtmlLink lnkDetpagoTransBanco;
	protected HtmlOutputText lblEtAbonoTransBanc1;
	protected HtmlOutputText lblPagoTransBanco;
	protected HtmlLink lnkCerrarVentana;
	protected HtmlDialogWindow dwEgresosxMetPago;
	protected HtmlDialogWindowClientEvents ceEgrxmetp;
	protected HtmlDialogWindowRoundedCorners crEgrxmetp;
	protected HtmlDialogWindowContentPane cnpEgrxmetp;
	protected HtmlDialogWindowHeader hdDetotrosE;
	protected HtmlJspPanel jspDetOtrosEgr;
	protected HtmlOutputText lblDetalleOtrosEgre;
	protected HtmlOutputText lblDetOegTipo;
	protected HtmlOutputText lblDetOeMonto;
	protected HtmlLink lnkDeOtrosEcambios;
	protected HtmlOutputText lblEtOegCambios;
	protected HtmlOutputText lblOEcambios;
	protected HtmlLink lnkDet;
	protected HtmlOutputText lnkDetalleOepagosOtraMon;
	protected HtmlOutputText lblTotalEgrRecxmonex;
	protected HtmlLink lnkDetOtEgSalidas;
	protected HtmlOutputText lnkDetalleOtEgSalidas;
	protected HtmlOutputText lblOEsalidas;
	protected HtmlLink lnkCerrarDetOtrosEgresos;
	protected HtmlDialogWindow dwDetOtrosEgresos;
	protected HtmlDialogWindowClientEvents ceDetotrosE;
	protected HtmlDialogWindowRoundedCorners crDetotrosE;
	protected HtmlDialogWindowContentPane cnpDetotrosE;
	protected HtmlDialogWindowHeader hdDetalleCam;
	protected HtmlGridView gvDetalleCambios;
	protected HtmlLink lnkDetalleRecibosxCambios;
	protected HtmlOutputText lblDetDev_numrec;
	protected HtmlOutputText lblDetDev_cliente;
	protected HtmlOutputText lblDetDev_montoapl;
	protected HtmlOutputText lblDetDev_montorec;
	protected HtmlOutputText lblDetDev_cambio;
	protected HtmlOutputText lblDetDev_hora;
	protected HtmlLink lnkCerrarDetalleCambios;
	protected HtmlDialogWindow dwDetalleCambios;
	protected HtmlDialogWindowClientEvents clDetalleCam;
	protected HtmlDialogWindowRoundedCorners crDetalleCam;
	protected HtmlDialogWindowContentPane cnpDetalleCam;
	protected HtmlDialogWindowHeader hdDetOI;
	protected HtmlJspPanel jspOtrosIng_1;
	protected HtmlOutputText lblhdDetalle;
	protected HtmlOutputText lblhdConcepto;
	protected HtmlOutputText lblhdMonto;
	protected HtmlLink lnkdetOiningext;
	protected HtmlOutputText lblEtIngExtraOr;
	protected HtmlOutputText lblIngresosExtraOrd;
	protected HtmlLink lnkDetCamOtrMoneda;
	protected HtmlOutputText lblEtCamOtrMoneda;
	protected HtmlLink lnkCerrarDetOtrosIng;
	protected HtmlDialogWindow dwDetalleOtrosIngresos;
	protected HtmlDialogWindowClientEvents ceOtrosIng;
	protected HtmlDialogWindowRoundedCorners crOtrosIng;
	protected HtmlDialogWindowContentPane cnpOtrosIng;
	protected HtmlDialogWindowHeader hdDetSalida;
	protected HtmlGridView gvDetalleSalidas;
	protected HtmlOutputText lblds_numsal;
	protected HtmlOutputText lblds_solicitante;
	protected HtmlOutputText lblds_monto;
	protected HtmlOutputText lblds_toperacion;
	protected HtmlOutputText lblds_hora;
	protected HtmlOutputText lblds_Refer1;
	protected HtmlOutputText lblds_Refer2;
	protected HtmlOutputText lblRcTmp_refer3der33;
	protected HtmlOutputText lblds_refer4;
	protected HtmlLink lnkCerrarDetalleSalidas;
	protected HtmlDialogWindow dwDetalleSalidas;
	protected HtmlDialogWindowClientEvents clDetSalida;
	protected HtmlDialogWindowRoundedCorners crDetSalida;
	protected HtmlDialogWindowContentPane cnpDetSalida;
	protected HtmlDialogWindowHeader hdCargarReferenciasPOS;
	protected HtmlJspPanel jspPanelGridPOS;
	protected HtmlOutputText lblMsgValidaReferencia;
	protected HtmlGridView gvReferenciaPos;
	protected HtmlColumn coReferpos;
	protected HtmlInputText lblreferpos;
	protected HtmlOutputText lblCoreferencia;
	protected HtmlOutputText lblCodigoPos;
	protected HtmlOutputText lblcodpos;
	protected HtmlOutputText lblnombrePos;
	protected HtmlOutputText lblnombrepos;
	protected HtmlOutputText lblTotalPos;
	protected HtmlOutputText lblTotalpos;
	protected HtmlOutputText lblComisPos;
	protected HtmlOutputText lblComispos;
	protected HtmlOutputText lblMtoComisPos;
	protected HtmlOutputText lblMtoComispos;
	protected HtmlOutputText lblmontoRetencion;
	protected HtmlOutputText lblmontoRetencion2;
	protected HtmlOutputText lblMtoNetoPos;
	protected HtmlOutputText lblMtoNetopos;
	protected HtmlOutputText lblVmanual;
	protected HtmlOutputText lblvmanualpos;
	protected HtmlCheckBox chkPagoTransitorio;
	protected HtmlOutputText lblPagoTransitoriopos;
	protected HtmlLink lnkReferPosSI;
	protected HtmlDialogWindow dwCargarReferenciasPOS;
	protected HtmlDialogWindowClientEvents ceReferenciasPos;
	protected HtmlDialogWindowRoundedCorners crReferenciasPos;
	protected HtmlDialogWindowContentPane cnpReferenciasPos;
	protected HtmlColumn coCodPos;
	protected HtmlColumn coNombre;
	protected HtmlColumn coTotalpos;
	protected HtmlColumn coComision;
	protected HtmlColumn coMtoComis;
	protected HtmlColumn coMontoRetencion;
	protected HtmlColumn coMtoNeto;
	protected HtmlColumn coVmanual;
	protected HtmlColumn copagoTransitorio;
	protected HtmlJspPanel jspPanelOpcionReferPos;
	protected HtmlLink lnkReferPosNo;
	protected HtmlDialogWindowHeader hdRePrintRpt;
	protected HtmlPanelGrid gridRePrintRpt;
	protected HtmlGraphicImageEx imageExRePrintRpt;
	protected HtmlLink lnkRePrintRptSi;
	protected HtmlDialogWindow rv_dwConfirmarReimpresionRpt;
	protected HtmlDialogWindowClientEvents cleRePrintRpt;
	protected HtmlDialogWindowRoundedCorners rcRePrintRpt;
	protected HtmlDialogWindowContentPane cpRePrintRpt;
	protected HtmlOutputText lblRePrintRpt;
	protected HtmlJspPanel jspPanelRePrintRpt;
	protected HtmlLink lnkRePrintRptNo;
	protected HtmlDialogWindowAutoPostBackFlags apbRePrintRpt;
	protected HtmlDialogWindowHeader hdEditarRecibosIdPos;
	protected HtmlJspPanel jpEditarRecibosIdPos;
	protected HtmlGridView rv_gvEditarRecibosIdPos;
	protected HtmlOutputText lblEidpos_numrec;
	protected HtmlOutputText lblEidpos_tiporec;
	protected HtmlPanelGroup hpgrCantidad;
	protected HtmlGridAgFunction gaf_funcion;
	protected HtmlOutputText lblEidpos_cliente;
	protected HtmlOutputText lblEidpos_monto;
	protected HtmlOutputText lblEidpos_montoapl;
	protected HtmlOutputText lblEidpos_Refer1;
	protected HtmlOutputText lblEidpos_Refer2;
	protected HtmlOutputText lblEidpos_refer3;
	protected HtmlOutputText lblMsgErrorCambioRefer;
	protected HtmlLink lnkCerrarActualizarIdPOS;
	protected HtmlDialogWindow rv_dwEditarRecibosIdPos;
	protected HtmlDialogWindowClientEvents clEditarRecibosIdPos;
	protected HtmlDialogWindowRoundedCorners crEditarRecibosIdPos;
	protected HtmlDialogWindowContentPane cnpEditarRecibosIdPos;
	protected HtmlColumn coEidposRefer1;
	protected HtmlLink lnkActualizarIdPOS;
	protected HtmlColumn coEidposRefer2;
	protected HtmlColumn coEidposRefer3;
	protected HtmlColumn coEidposRefer3hidden;
	protected HtmlDialogWindowHeader hdAsignarReferenciaChk;
	protected HtmlJspPanel idGridAgregarReferChk;
	protected HtmlGridView rv_gvAsignarReferenciaCheque;
	protected HtmlOutputText lblAsiReferCkc_nombreBanco;
	protected HtmlPanelGrid pgrFooter1;
	protected HtmlOutputText lblAsiReferCkc_codigoBanco;
	protected HtmlOutputText lblAsiReferCkc_cantidadCheque;
	protected HtmlOutputText lblAsiReferCkc_bTotalBanco;
	protected HtmlOutputText lblAsiReferCkc_nommoneda;
	protected HtmlInputText lblreferchk;
	protected HtmlOutputText lblCoreferenciaChk;
	protected HtmlOutputText lblMsgErrorAsignarReferChk;
	protected HtmlLink lnkReferChkSI;
	protected HtmlLink lnkReferChkNo;
	protected HtmlDialogWindow rv_dwAsignarReferCheque;
	protected HtmlDialogWindowClientEvents clAsignarReferenciaCheque;
	protected HtmlDialogWindowRoundedCorners crAsignarReferenciaCheque;
	protected HtmlDialogWindowContentPane cnpAsignarReferenciaCheque;
	protected HtmlGridAgFunction archk_funcion;
	protected HtmlGridAgFunction archk_funcion1;
	protected HtmlColumn coReferChk;
	protected HtmlDialogWindowClientEvents cledwCargando;
	protected HtmlJspPanel jspdwCargando;
	protected HtmlOutputText lblMensajeCargando;
	protected HtmlGraphicImageEx imagenCargando;
	protected HtmlDialogWindow dwCargando;
	protected HtmlDialogWindowRoundedCorners cledwCargando22;
	protected HtmlDialogWindowContentPane cpdwCargando;
	protected HtmlDialogWindowAutoPostBackFlags apbdwCargando;
	protected HtmlOutputText lblMtoComisPosIva;
	protected HtmlOutputText lblMtoComisposIva;
	protected HtmlColumn coMtoComisIva;
	protected HtmlDialogWindowHeader hdReciboMPago8N;
	protected HtmlGridView gvRecibosxMpago8N;
	protected HtmlOutputText lblMsgCambioReferMPago;
	protected HtmlLink lnkCerrarReciboxMPago8N;
	protected HtmlDialogWindow dwRecibosxMpago8N;
	protected HtmlDialogWindowContentPane cnpRecibosxMpago8N;
	protected HtmlLink lnkCambiarReferPago;
	protected HtmlColumn coReferOculta;
	protected HtmlJspPanel jspPmensaje;
	protected HtmlOutputText lblet_montoMinReint;
	protected HtmlOutputText lblet_montoMinajust;
	protected HtmlOutputText lblCDC_montoMinReint;
	protected HtmlOutputText lblCDC_montoMinAjust;
	protected HtmlScriptCollector getScriptCollector1() {
		if (scriptCollector1 == null) {
			scriptCollector1 = (HtmlScriptCollector) findComponentInRoot("scriptCollector1");
		}
		return scriptCollector1;
	}

	protected HtmlForm getFrmRevisionArqueo() {
		if (frmRevisionArqueo == null) {
			frmRevisionArqueo = (HtmlForm) findComponentInRoot("frmRevisionArqueo");
		}
		return frmRevisionArqueo;
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

	protected HtmlOutputText getLblTitRevisionArqueo0() {
		if (lblTitRevisionArqueo0 == null) {
			lblTitRevisionArqueo0 = (HtmlOutputText) findComponentInRoot("lblTitRevisionArqueo0");
		}
		return lblTitRevisionArqueo0;
	}

	protected HtmlLink getLnkFiltrarxFecha() {
		if (lnkFiltrarxFecha == null) {
			lnkFiltrarxFecha = (HtmlLink) findComponentInRoot("lnkFiltrarxFecha");
		}
		return lnkFiltrarxFecha;
	}

	protected HtmlDateChooser getDcFechaArqueo() {
		if (dcFechaArqueo == null) {
			dcFechaArqueo = (HtmlDateChooser) findComponentInRoot("dcFechaArqueo");
		}
		return dcFechaArqueo;
	}

	protected HtmlOutputText getLblMensaje() {
		if (lblMensaje == null) {
			lblMensaje = (HtmlOutputText) findComponentInRoot("lblMensaje");
		}
		return lblMensaje;
	}

	protected HtmlOutputText getLblHeader() {
		if (lblHeader == null) {
			lblHeader = (HtmlOutputText) findComponentInRoot("lblHeader");
		}
		return lblHeader;
	}

	protected UINamingContainer getVfRevisionArqueo() {
		if (vfRevisionArqueo == null) {
			vfRevisionArqueo = (UINamingContainer) findComponentInRoot("vfRevisionArqueo");
		}
		return vfRevisionArqueo;
	}

	protected HtmlOutputText getLblTitRevisionArqueo1() {
		if (lblTitRevisionArqueo1 == null) {
			lblTitRevisionArqueo1 = (HtmlOutputText) findComponentInRoot("lblTitRevisionArqueo1");
		}
		return lblTitRevisionArqueo1;
	}

	protected HtmlGridView getGvArqueosPendRev() {
		if (gvArqueosPendRev == null) {
			gvArqueosPendRev = (HtmlGridView) findComponentInRoot("gvArqueosPendRev");
		}
		return gvArqueosPendRev;
	}

	protected HtmlColumn getCoLnkDetalle() {
		if (coLnkDetalle == null) {
			coLnkDetalle = (HtmlColumn) findComponentInRoot("coLnkDetalle");
		}
		return coLnkDetalle;
	}

	protected HtmlLink getLnkDetalleArqueoCaja() {
		if (lnkDetalleArqueoCaja == null) {
			lnkDetalleArqueoCaja = (HtmlLink) findComponentInRoot("lnkDetalleArqueoCaja");
		}
		return lnkDetalleArqueoCaja;
	}

	protected HtmlOutputText getLblDetalleArqueoCaja() {
		if (lblDetalleArqueoCaja == null) {
			lblDetalleArqueoCaja = (HtmlOutputText) findComponentInRoot("lblDetalleArqueoCaja");
		}
		return lblDetalleArqueoCaja;
	}

	protected HtmlOutputText getLblcaid0() {
		if (lblcaid0 == null) {
			lblcaid0 = (HtmlOutputText) findComponentInRoot("lblcaid0");
		}
		return lblcaid0;
	}

	protected HtmlOutputText getLblcaid1() {
		if (lblcaid1 == null) {
			lblcaid1 = (HtmlOutputText) findComponentInRoot("lblcaid1");
		}
		return lblcaid1;
	}

	protected HtmlColumn getCoNoCaja() {
		if (coNoCaja == null) {
			coNoCaja = (HtmlColumn) findComponentInRoot("coNoCaja");
		}
		return coNoCaja;
	}

	protected HtmlOutputText getLblcodcajero0() {
		if (lblcodcajero0 == null) {
			lblcodcajero0 = (HtmlOutputText) findComponentInRoot("lblcodcajero0");
		}
		return lblcodcajero0;
	}

	protected HtmlOutputText getLblcodcajero1() {
		if (lblcodcajero1 == null) {
			lblcodcajero1 = (HtmlOutputText) findComponentInRoot("lblcodcajero1");
		}
		return lblcodcajero1;
	}

	protected HtmlColumn getCoCajeroId() {
		if (coCajeroId == null) {
			coCajeroId = (HtmlColumn) findComponentInRoot("coCajeroId");
		}
		return coCajeroId;
	}

	protected HtmlOutputText getLblcodcomp0() {
		if (lblcodcomp0 == null) {
			lblcodcomp0 = (HtmlOutputText) findComponentInRoot("lblcodcomp0");
		}
		return lblcodcomp0;
	}

	protected HtmlOutputText getLblcodcomp1() {
		if (lblcodcomp1 == null) {
			lblcodcomp1 = (HtmlOutputText) findComponentInRoot("lblcodcomp1");
		}
		return lblcodcomp1;
	}

	protected HtmlColumn getCoCodcomp() {
		if (coCodcomp == null) {
			coCodcomp = (HtmlColumn) findComponentInRoot("coCodcomp");
		}
		return coCodcomp;
	}

	protected HtmlOutputText getLblnoarqueo0() {
		if (lblnoarqueo0 == null) {
			lblnoarqueo0 = (HtmlOutputText) findComponentInRoot("lblnoarqueo0");
		}
		return lblnoarqueo0;
	}

	protected HtmlOutputText getLblnoarqueo1() {
		if (lblnoarqueo1 == null) {
			lblnoarqueo1 = (HtmlOutputText) findComponentInRoot("lblnoarqueo1");
		}
		return lblnoarqueo1;
	}

	protected HtmlColumn getCoNoarqueo() {
		if (coNoarqueo == null) {
			coNoarqueo = (HtmlColumn) findComponentInRoot("coNoarqueo");
		}
		return coNoarqueo;
	}

	protected HtmlColumn getCofecha() {
		if (cofecha == null) {
			cofecha = (HtmlColumn) findComponentInRoot("cofecha");
		}
		return cofecha;
	}

	protected HtmlColumn getCohora() {
		if (cohora == null) {
			cohora = (HtmlColumn) findComponentInRoot("cohora");
		}
		return cohora;
	}

	protected HtmlOutputText getLblmoneda0() {
		if (lblmoneda0 == null) {
			lblmoneda0 = (HtmlOutputText) findComponentInRoot("lblmoneda0");
		}
		return lblmoneda0;
	}

	protected HtmlOutputText getLblmoneda1() {
		if (lblmoneda1 == null) {
			lblmoneda1 = (HtmlOutputText) findComponentInRoot("lblmoneda1");
		}
		return lblmoneda1;
	}

	protected HtmlColumn getCoMoneda() {
		if (coMoneda == null) {
			coMoneda = (HtmlColumn) findComponentInRoot("coMoneda");
		}
		return coMoneda;
	}

	protected HtmlOutputText getLbldsugerido0() {
		if (lbldsugerido0 == null) {
			lbldsugerido0 = (HtmlOutputText) findComponentInRoot("lbldsugerido0");
		}
		return lbldsugerido0;
	}

	protected HtmlOutputText getLbldsugerido1() {
		if (lbldsugerido1 == null) {
			lbldsugerido1 = (HtmlOutputText) findComponentInRoot("lbldsugerido1");
		}
		return lbldsugerido1;
	}

	protected HtmlOutputText getLblfooter0() {
		if (lblfooter0 == null) {
			lblfooter0 = (HtmlOutputText) findComponentInRoot("lblfooter0");
		}
		return lblfooter0;
	}

	protected HtmlColumn getCoDepSug() {
		if (coDepSug == null) {
			coDepSug = (HtmlColumn) findComponentInRoot("coDepSug");
		}
		return coDepSug;
	}

	protected HtmlOutputText getLbldsf0() {
		if (lbldsf0 == null) {
			lbldsf0 = (HtmlOutputText) findComponentInRoot("lbldsf0");
		}
		return lbldsf0;
	}

	protected HtmlOutputText getLblsf1() {
		if (lblsf1 == null) {
			lblsf1 = (HtmlOutputText) findComponentInRoot("lblsf1");
		}
		return lblsf1;
	}

	protected HtmlColumn getCoSobFal() {
		if (coSobFal == null) {
			coSobFal = (HtmlColumn) findComponentInRoot("coSobFal");
		}
		return coSobFal;
	}

	protected HtmlLink getLnkRefrescarVistaContado() {
		if (lnkRefrescarVistaContado == null) {
			lnkRefrescarVistaContado = (HtmlLink) findComponentInRoot("lnkRefrescarVistaContado");
		}
		return lnkRefrescarVistaContado;
	}

	protected HtmlOutputText getLblFiltroCaja() {
		if (lblFiltroCaja == null) {
			lblFiltroCaja = (HtmlOutputText) findComponentInRoot("lblFiltroCaja");
		}
		return lblFiltroCaja;
	}

	protected HtmlDropDownList getDdlFiltroCaja() {
		if (ddlFiltroCaja == null) {
			ddlFiltroCaja = (HtmlDropDownList) findComponentInRoot("ddlFiltroCaja");
		}
		return ddlFiltroCaja;
	}

	protected HtmlOutputText getLblFiltroComp() {
		if (lblFiltroComp == null) {
			lblFiltroComp = (HtmlOutputText) findComponentInRoot("lblFiltroComp");
		}
		return lblFiltroComp;
	}

	protected HtmlDropDownList getDdlFiltroCompania() {
		if (ddlFiltroCompania == null) {
			ddlFiltroCompania = (HtmlDropDownList) findComponentInRoot("ddlFiltroCompania");
		}
		return ddlFiltroCompania;
	}

	protected HtmlOutputText getLblFiltroMoneda() {
		if (lblFiltroMoneda == null) {
			lblFiltroMoneda = (HtmlOutputText) findComponentInRoot("lblFiltroMoneda");
		}
		return lblFiltroMoneda;
	}

	protected HtmlDropDownList getDdlFiltroMoneda() {
		if (ddlFiltroMoneda == null) {
			ddlFiltroMoneda = (HtmlDropDownList) findComponentInRoot("ddlFiltroMoneda");
		}
		return ddlFiltroMoneda;
	}

	protected HtmlOutputText getLblFiltroEsado() {
		if (lblFiltroEsado == null) {
			lblFiltroEsado = (HtmlOutputText) findComponentInRoot("lblFiltroEsado");
		}
		return lblFiltroEsado;
	}

	protected HtmlDropDownList getDdlFiltroEstado() {
		if (ddlFiltroEstado == null) {
			ddlFiltroEstado = (HtmlDropDownList) findComponentInRoot("ddlFiltroEstado");
		}
		return ddlFiltroEstado;
	}

	protected HtmlDialogWindowHeader getHdDetArqueo() {
		if (hdDetArqueo == null) {
			hdDetArqueo = (HtmlDialogWindowHeader) findComponentInRoot("hdDetArqueo");
		}
		return hdDetArqueo;
	}

	protected HtmlJspPanel getJspDetArqueo() {
		if (jspDetArqueo == null) {
			jspDetArqueo = (HtmlJspPanel) findComponentInRoot("jspDetArqueo");
		}
		return jspDetArqueo;
	}

	protected HtmlOutputText getLbletnocaja() {
		if (lbletnocaja == null) {
			lbletnocaja = (HtmlOutputText) findComponentInRoot("lbletnocaja");
		}
		return lbletnocaja;
	}

	protected HtmlOutputText getLblnocaja() {
		if (lblnocaja == null) {
			lblnocaja = (HtmlOutputText) findComponentInRoot("lblnocaja");
		}
		return lblnocaja;
	}

	protected HtmlOutputText getLbletsucursal() {
		if (lbletsucursal == null) {
			lbletsucursal = (HtmlOutputText) findComponentInRoot("lbletsucursal");
		}
		return lbletsucursal;
	}

	protected HtmlOutputText getLblsucursal() {
		if (lblsucursal == null) {
			lblsucursal = (HtmlOutputText) findComponentInRoot("lblsucursal");
		}
		return lblsucursal;
	}

	protected HtmlOutputText getLbletnoarqueo() {
		if (lbletnoarqueo == null) {
			lbletnoarqueo = (HtmlOutputText) findComponentInRoot("lbletnoarqueo");
		}
		return lbletnoarqueo;
	}

	protected HtmlOutputText getLblnoarqueo() {
		if (lblnoarqueo == null) {
			lblnoarqueo = (HtmlOutputText) findComponentInRoot("lblnoarqueo");
		}
		return lblnoarqueo;
	}

	protected HtmlOutputText getLbletcajero() {
		if (lbletcajero == null) {
			lbletcajero = (HtmlOutputText) findComponentInRoot("lbletcajero");
		}
		return lbletcajero;
	}

	protected HtmlOutputText getLblcajero() {
		if (lblcajero == null) {
			lblcajero = (HtmlOutputText) findComponentInRoot("lblcajero");
		}
		return lblcajero;
	}

	protected HtmlOutputText getLbletCompania() {
		if (lbletCompania == null) {
			lbletCompania = (HtmlOutputText) findComponentInRoot("lbletCompania");
		}
		return lbletCompania;
	}

	protected HtmlOutputText getLblCompania() {
		if (lblCompania == null) {
			lblCompania = (HtmlOutputText) findComponentInRoot("lblCompania");
		}
		return lblCompania;
	}

	protected HtmlOutputText getLbletMoneda() {
		if (lbletMoneda == null) {
			lbletMoneda = (HtmlOutputText) findComponentInRoot("lbletMoneda");
		}
		return lbletMoneda;
	}

	protected HtmlOutputText getLblMoneda() {
		if (lblMoneda == null) {
			lblMoneda = (HtmlOutputText) findComponentInRoot("lblMoneda");
		}
		return lblMoneda;
	}

	protected HtmlOutputText getLblTitIngresos() {
		if (lblTitIngresos == null) {
			lblTitIngresos = (HtmlOutputText) findComponentInRoot("lblTitIngresos");
		}
		return lblTitIngresos;
	}

	protected HtmlLink getLnkDetalleVtasDia() {
		if (lnkDetalleVtasDia == null) {
			lnkDetalleVtasDia = (HtmlLink) findComponentInRoot("lnkDetalleVtasDia");
		}
		return lnkDetalleVtasDia;
	}

	protected HtmlOutputText getLblEtiqVentasTotales() {
		if (lblEtiqVentasTotales == null) {
			lblEtiqVentasTotales = (HtmlOutputText) findComponentInRoot("lblEtiqVentasTotales");
		}
		return lblEtiqVentasTotales;
	}

	protected HtmlOutputText getLblVentasTotales() {
		if (lblVentasTotales == null) {
			lblVentasTotales = (HtmlOutputText) findComponentInRoot("lblVentasTotales");
		}
		return lblVentasTotales;
	}

	protected HtmlLink getLnkDetalleFactDevoluciones() {
		if (lnkDetalleFactDevoluciones == null) {
			lnkDetalleFactDevoluciones = (HtmlLink) findComponentInRoot("lnkDetalleFactDevoluciones");
		}
		return lnkDetalleFactDevoluciones;
	}

	protected HtmlOutputText getLblEtiqDevoluciones() {
		if (lblEtiqDevoluciones == null) {
			lblEtiqDevoluciones = (HtmlOutputText) findComponentInRoot("lblEtiqDevoluciones");
		}
		return lblEtiqDevoluciones;
	}

	protected HtmlOutputText getLblTotalDevoluciones() {
		if (lblTotalDevoluciones == null) {
			lblTotalDevoluciones = (HtmlOutputText) findComponentInRoot("lblTotalDevoluciones");
		}
		return lblTotalDevoluciones;
	}

	protected HtmlLink getLnkDetalleVtasCredito() {
		if (lnkDetalleVtasCredito == null) {
			lnkDetalleVtasCredito = (HtmlLink) findComponentInRoot("lnkDetalleVtasCredito");
		}
		return lnkDetalleVtasCredito;
	}

	protected HtmlOutputText getLblEtiqVtsCredito() {
		if (lblEtiqVtsCredito == null) {
			lblEtiqVtsCredito = (HtmlOutputText) findComponentInRoot("lblEtiqVtsCredito");
		}
		return lblEtiqVtsCredito;
	}

	protected HtmlOutputText getLblTotalVtsCredito() {
		if (lblTotalVtsCredito == null) {
			lblTotalVtsCredito = (HtmlOutputText) findComponentInRoot("lblTotalVtsCredito");
		}
		return lblTotalVtsCredito;
	}

	protected HtmlOutputText getLblEtiqVtasNetas() {
		if (lblEtiqVtasNetas == null) {
			lblEtiqVtasNetas = (HtmlOutputText) findComponentInRoot("lblEtiqVtasNetas");
		}
		return lblEtiqVtasNetas;
	}

	protected HtmlOutputText getLblTotalVentasNetas() {
		if (lblTotalVentasNetas == null) {
			lblTotalVentasNetas = (HtmlOutputText) findComponentInRoot("lblTotalVentasNetas");
		}
		return lblTotalVentasNetas;
	}

	protected HtmlLink getLnkDetalleAbonos() {
		if (lnkDetalleAbonos == null) {
			lnkDetalleAbonos = (HtmlLink) findComponentInRoot("lnkDetalleAbonos");
		}
		return lnkDetalleAbonos;
	}

	protected HtmlOutputText getLblEtiqAbonos() {
		if (lblEtiqAbonos == null) {
			lblEtiqAbonos = (HtmlOutputText) findComponentInRoot("lblEtiqAbonos");
		}
		return lblEtiqAbonos;
	}

	protected HtmlOutputText getLblTotalAbonos() {
		if (lblTotalAbonos == null) {
			lblTotalAbonos = (HtmlOutputText) findComponentInRoot("lblTotalAbonos");
		}
		return lblTotalAbonos;
	}

	protected HtmlLink getLnkDetalleFinanciamiento() {
		if (lnkDetalleFinanciamiento == null) {
			lnkDetalleFinanciamiento = (HtmlLink) findComponentInRoot("lnkDetalleFinanciamiento");
		}
		return lnkDetalleFinanciamiento;
	}

	protected HtmlOutputText getLblEtiqFinan() {
		if (lblEtiqFinan == null) {
			lblEtiqFinan = (HtmlOutputText) findComponentInRoot("lblEtiqFinan");
		}
		return lblEtiqFinan;
	}

	protected HtmlOutputText getLblTotalFinan() {
		if (lblTotalFinan == null) {
			lblTotalFinan = (HtmlOutputText) findComponentInRoot("lblTotalFinan");
		}
		return lblTotalFinan;
	}

	protected HtmlLink getLnkDetallePrimasReservas() {
		if (lnkDetallePrimasReservas == null) {
			lnkDetallePrimasReservas = (HtmlLink) findComponentInRoot("lnkDetallePrimasReservas");
		}
		return lnkDetallePrimasReservas;
	}

	protected HtmlOutputText getLblEtiqPrimas() {
		if (lblEtiqPrimas == null) {
			lblEtiqPrimas = (HtmlOutputText) findComponentInRoot("lblEtiqPrimas");
		}
		return lblEtiqPrimas;
	}

	protected HtmlOutputText getLblTotalPrimas() {
		if (lblTotalPrimas == null) {
			lblTotalPrimas = (HtmlOutputText) findComponentInRoot("lblTotalPrimas");
		}
		return lblTotalPrimas;
	}

	protected HtmlLink getLnkDetalleIngresosEx() {
		if (lnkDetalleIngresosEx == null) {
			lnkDetalleIngresosEx = (HtmlLink) findComponentInRoot("lnkDetalleIngresosEx");
		}
		return lnkDetalleIngresosEx;
	}

	protected HtmlOutputText getLblEtiqIngEx() {
		if (lblEtiqIngEx == null) {
			lblEtiqIngEx = (HtmlOutputText) findComponentInRoot("lblEtiqIngEx");
		}
		return lblEtiqIngEx;
	}

	protected HtmlOutputText getLblTotalIngex() {
		if (lblTotalIngex == null) {
			lblTotalIngex = (HtmlOutputText) findComponentInRoot("lblTotalIngex");
		}
		return lblTotalIngex;
	}

	protected HtmlLink getLnkDetIngxRecPagMonEx() {
		if (lnkDetIngxRecPagMonEx == null) {
			lnkDetIngxRecPagMonEx = (HtmlLink) findComponentInRoot("lnkDetIngxRecPagMonEx");
		}
		return lnkDetIngxRecPagMonEx;
	}

	protected HtmlOutputText getLblEtIngRecxmonex() {
		if (lblEtIngRecxmonex == null) {
			lblEtIngRecxmonex = (HtmlOutputText) findComponentInRoot("lblEtIngRecxmonex");
		}
		return lblEtIngRecxmonex;
	}

	protected HtmlOutputText getLblTotalIngRecxmonex() {
		if (lblTotalIngRecxmonex == null) {
			lblTotalIngRecxmonex = (HtmlOutputText) findComponentInRoot("lblTotalIngRecxmonex");
		}
		return lblTotalIngRecxmonex;
	}

	protected HtmlLink getLnkDetalleOtrosIngresos() {
		if (lnkDetalleOtrosIngresos == null) {
			lnkDetalleOtrosIngresos = (HtmlLink) findComponentInRoot("lnkDetalleOtrosIngresos");
		}
		return lnkDetalleOtrosIngresos;
	}

	protected HtmlOutputText getLblEtiqOtrosIngresos() {
		if (lblEtiqOtrosIngresos == null) {
			lblEtiqOtrosIngresos = (HtmlOutputText) findComponentInRoot("lblEtiqOtrosIngresos");
		}
		return lblEtiqOtrosIngresos;
	}

	protected HtmlOutputText getLblCambioOtraMoneda() {
		if (lblCambioOtraMoneda == null) {
			lblCambioOtraMoneda = (HtmlOutputText) findComponentInRoot("lblCambioOtraMoneda");
		}
		return lblCambioOtraMoneda;
	}

	protected HtmlOutputText getLblEtiTotaIngresos() {
		if (lblEtiTotaIngresos == null) {
			lblEtiTotaIngresos = (HtmlOutputText) findComponentInRoot("lblEtiTotaIngresos");
		}
		return lblEtiTotaIngresos;
	}

	protected HtmlOutputText getLblTotaIngresos() {
		if (lblTotaIngresos == null) {
			lblTotaIngresos = (HtmlOutputText) findComponentInRoot("lblTotaIngresos");
		}
		return lblTotaIngresos;
	}

	protected HtmlOutputText getLblTitEgresos() {
		if (lblTitEgresos == null) {
			lblTitEgresos = (HtmlOutputText) findComponentInRoot("lblTitEgresos");
		}
		return lblTitEgresos;
	}

	protected HtmlLink getLnkvtspbanco() {
		if (lnkvtspbanco == null) {
			lnkvtspbanco = (HtmlLink) findComponentInRoot("lnkvtspbanco");
		}
		return lnkvtspbanco;
	}

	protected HtmlOutputText getLbletvtspagbanco() {
		if (lbletvtspagbanco == null) {
			lbletvtspagbanco = (HtmlOutputText) findComponentInRoot("lbletvtspagbanco");
		}
		return lbletvtspagbanco;
	}

	protected HtmlOutputText getLblTotalVtsPagBanco() {
		if (lblTotalVtsPagBanco == null) {
			lblTotalVtsPagBanco = (HtmlOutputText) findComponentInRoot("lblTotalVtsPagBanco");
		}
		return lblTotalVtsPagBanco;
	}

	protected HtmlLink getLnkabonospbanco() {
		if (lnkabonospbanco == null) {
			lnkabonospbanco = (HtmlLink) findComponentInRoot("lnkabonospbanco");
		}
		return lnkabonospbanco;
	}

	protected HtmlOutputText getLbletabonospagbanco() {
		if (lbletabonospagbanco == null) {
			lbletabonospagbanco = (HtmlOutputText) findComponentInRoot("lbletabonospagbanco");
		}
		return lbletabonospagbanco;
	}

	protected HtmlOutputText getLblTotalAbonoPagBanco() {
		if (lblTotalAbonoPagBanco == null) {
			lblTotalAbonoPagBanco = (HtmlOutputText) findComponentInRoot("lblTotalAbonoPagBanco");
		}
		return lblTotalAbonoPagBanco;
	}

	protected HtmlLink getLnkFinanpbanco() {
		if (lnkFinanpbanco == null) {
			lnkFinanpbanco = (HtmlLink) findComponentInRoot("lnkFinanpbanco");
		}
		return lnkFinanpbanco;
	}

	protected HtmlOutputText getLbletFinanpagbanco() {
		if (lbletFinanpagbanco == null) {
			lbletFinanpagbanco = (HtmlOutputText) findComponentInRoot("lbletFinanpagbanco");
		}
		return lbletFinanpagbanco;
	}

	protected HtmlOutputText getLblTotalFinanPagBanco() {
		if (lblTotalFinanPagBanco == null) {
			lblTotalFinanPagBanco = (HtmlOutputText) findComponentInRoot("lblTotalFinanPagBanco");
		}
		return lblTotalFinanPagBanco;
	}

	protected HtmlLink getLnkprimaspbanco() {
		if (lnkprimaspbanco == null) {
			lnkprimaspbanco = (HtmlLink) findComponentInRoot("lnkprimaspbanco");
		}
		return lnkprimaspbanco;
	}

	protected HtmlOutputText getLbletprimaspagbanco() {
		if (lbletprimaspagbanco == null) {
			lbletprimaspagbanco = (HtmlOutputText) findComponentInRoot("lbletprimaspagbanco");
		}
		return lbletprimaspagbanco;
	}

	protected HtmlOutputText getLblTotalPrimasPagBanco() {
		if (lblTotalPrimasPagBanco == null) {
			lblTotalPrimasPagBanco = (HtmlOutputText) findComponentInRoot("lblTotalPrimasPagBanco");
		}
		return lblTotalPrimasPagBanco;
	}

	protected HtmlLink getLnkingresosexpbanco() {
		if (lnkingresosexpbanco == null) {
			lnkingresosexpbanco = (HtmlLink) findComponentInRoot("lnkingresosexpbanco");
		}
		return lnkingresosexpbanco;
	}

	protected HtmlOutputText getLbletIngExpagbanco() {
		if (lbletIngExpagbanco == null) {
			lbletIngExpagbanco = (HtmlOutputText) findComponentInRoot("lbletIngExpagbanco");
		}
		return lbletIngExpagbanco;
	}

	protected HtmlOutputText getLblTotalIexPagBanco() {
		if (lblTotalIexPagBanco == null) {
			lblTotalIexPagBanco = (HtmlOutputText) findComponentInRoot("lblTotalIexPagBanco");
		}
		return lblTotalIexPagBanco;
	}

	protected HtmlLink getLnkOtrosEgresosCaja() {
		if (lnkOtrosEgresosCaja == null) {
			lnkOtrosEgresosCaja = (HtmlLink) findComponentInRoot("lnkOtrosEgresosCaja");
		}
		return lnkOtrosEgresosCaja;
	}

	protected HtmlOutputText getLbletDetOtrosEgresos() {
		if (lbletDetOtrosEgresos == null) {
			lbletDetOtrosEgresos = (HtmlOutputText) findComponentInRoot("lbletDetOtrosEgresos");
		}
		return lbletDetOtrosEgresos;
	}

	protected HtmlOutputText getLblTotalOtrosEgresos() {
		if (lblTotalOtrosEgresos == null) {
			lblTotalOtrosEgresos = (HtmlOutputText) findComponentInRoot("lblTotalOtrosEgresos");
		}
		return lblTotalOtrosEgresos;
	}

	protected HtmlOutputText getLblEtTotalEgresos() {
		if (lblEtTotalEgresos == null) {
			lblEtTotalEgresos = (HtmlOutputText) findComponentInRoot("lblEtTotalEgresos");
		}
		return lblEtTotalEgresos;
	}

	protected HtmlOutputText getLblTotalEgresos() {
		if (lblTotalEgresos == null) {
			lblTotalEgresos = (HtmlOutputText) findComponentInRoot("lblTotalEgresos");
		}
		return lblTotalEgresos;
	}

	protected HtmlOutputText getLblTitCalculoDepCaja() {
		if (lblTitCalculoDepCaja == null) {
			lblTitCalculoDepCaja = (HtmlOutputText) findComponentInRoot("lblTitCalculoDepCaja");
		}
		return lblTitCalculoDepCaja;
	}

	protected HtmlOutputText getLblet_efectNetoCaja() {
		if (lblet_efectNetoCaja == null) {
			lblet_efectNetoCaja = (HtmlOutputText) findComponentInRoot("lblet_efectNetoCaja");
		}
		return lblet_efectNetoCaja;
	}

	protected HtmlOutputText getLblCDC_efectnetoRec() {
		if (lblCDC_efectnetoRec == null) {
			lblCDC_efectnetoRec = (HtmlOutputText) findComponentInRoot("lblCDC_efectnetoRec");
		}
		return lblCDC_efectnetoRec;
	}

	protected HtmlOutputText getLblet_efectCaja() {
		if (lblet_efectCaja == null) {
			lblet_efectCaja = (HtmlOutputText) findComponentInRoot("lblet_efectCaja");
		}
		return lblet_efectCaja;
	}

	protected HtmlOutputText getLblbCDC_efectivoenCaja() {
		if (lblbCDC_efectivoenCaja == null) {
			lblbCDC_efectivoenCaja = (HtmlOutputText) findComponentInRoot("lblbCDC_efectivoenCaja");
		}
		return lblbCDC_efectivoenCaja;
	}

	protected HtmlOutputText getLblet_pagochk() {
		if (lblet_pagochk == null) {
			lblet_pagochk = (HtmlOutputText) findComponentInRoot("lblet_pagochk");
		}
		return lblet_pagochk;
	}

	protected HtmlOutputText getLblCDC_pagochks() {
		if (lblCDC_pagochks == null) {
			lblCDC_pagochks = (HtmlOutputText) findComponentInRoot("lblCDC_pagochks");
		}
		return lblCDC_pagochks;
	}

	protected HtmlOutputText getLblet_SobranteFaltante() {
		if (lblet_SobranteFaltante == null) {
			lblet_SobranteFaltante = (HtmlOutputText) findComponentInRoot("lblet_SobranteFaltante");
		}
		return lblet_SobranteFaltante;
	}

	protected HtmlOutputText getLblCDC_SobranteFaltante() {
		if (lblCDC_SobranteFaltante == null) {
			lblCDC_SobranteFaltante = (HtmlOutputText) findComponentInRoot("lblCDC_SobranteFaltante");
		}
		return lblCDC_SobranteFaltante;
	}

	protected HtmlOutputText getLblet_montominimo() {
		if (lblet_montominimo == null) {
			lblet_montominimo = (HtmlOutputText) findComponentInRoot("lblet_montominimo");
		}
		return lblet_montominimo;
	}

	protected HtmlOutputText getLblCDC_montominimo() {
		if (lblCDC_montominimo == null) {
			lblCDC_montominimo = (HtmlOutputText) findComponentInRoot("lblCDC_montominimo");
		}
		return lblCDC_montominimo;
	}

	protected HtmlOutputText getLblet_depositoFinal() {
		if (lblet_depositoFinal == null) {
			lblet_depositoFinal = (HtmlOutputText) findComponentInRoot("lblet_depositoFinal");
		}
		return lblet_depositoFinal;
	}

	protected HtmlOutputText getLblCDC_depositoFinal() {
		if (lblCDC_depositoFinal == null) {
			lblCDC_depositoFinal = (HtmlOutputText) findComponentInRoot("lblCDC_depositoFinal");
		}
		return lblCDC_depositoFinal;
	}

	protected HtmlOutputText getLblet_DepositoSug() {
		if (lblet_DepositoSug == null) {
			lblet_DepositoSug = (HtmlOutputText) findComponentInRoot("lblet_DepositoSug");
		}
		return lblet_DepositoSug;
	}

	protected HtmlOutputText getLblCDC_depositoSug() {
		if (lblCDC_depositoSug == null) {
			lblCDC_depositoSug = (HtmlOutputText) findComponentInRoot("lblCDC_depositoSug");
		}
		return lblCDC_depositoSug;
	}

	protected HtmlOutputText getLblet_ReferenciaDeposito() {
		if (lblet_ReferenciaDeposito == null) {
			lblet_ReferenciaDeposito = (HtmlOutputText) findComponentInRoot("lblet_ReferenciaDeposito");
		}
		return lblet_ReferenciaDeposito;
	}

	protected HtmlInputText getTxtCDC_ReferDeposito() {
		if (txtCDC_ReferDeposito == null) {
			txtCDC_ReferDeposito = (HtmlInputText) findComponentInRoot("txtCDC_ReferDeposito");
		}
		return txtCDC_ReferDeposito;
	}

	protected HtmlLink getLnkCalDepCaja() {
		if (lnkCalDepCaja == null) {
			lnkCalDepCaja = (HtmlLink) findComponentInRoot("lnkCalDepCaja");
		}
		return lnkCalDepCaja;
	}

	protected HtmlOutputText getLblac_AyudaPagosRec() {
		if (lblac_AyudaPagosRec == null) {
			lblac_AyudaPagosRec = (HtmlOutputText) findComponentInRoot("lblac_AyudaPagosRec");
		}
		return lblac_AyudaPagosRec;
	}

	protected HtmlLink getLnkACDetfp_Efectivo() {
		if (lnkACDetfp_Efectivo == null) {
			lnkACDetfp_Efectivo = (HtmlLink) findComponentInRoot("lnkACDetfp_Efectivo");
		}
		return lnkACDetfp_Efectivo;
	}

	protected HtmlOutputText getLblACDetfp_etEfectivo() {
		if (lblACDetfp_etEfectivo == null) {
			lblACDetfp_etEfectivo = (HtmlOutputText) findComponentInRoot("lblACDetfp_etEfectivo");
		}
		return lblACDetfp_etEfectivo;
	}

	protected HtmlOutputText getLblACDetfp_Efectivo() {
		if (lblACDetfp_Efectivo == null) {
			lblACDetfp_Efectivo = (HtmlOutputText) findComponentInRoot("lblACDetfp_Efectivo");
		}
		return lblACDetfp_Efectivo;
	}

	protected HtmlLink getLnkACDetfp_Cheques() {
		if (lnkACDetfp_Cheques == null) {
			lnkACDetfp_Cheques = (HtmlLink) findComponentInRoot("lnkACDetfp_Cheques");
		}
		return lnkACDetfp_Cheques;
	}

	protected HtmlOutputText getLblACDetfp_etCheques() {
		if (lblACDetfp_etCheques == null) {
			lblACDetfp_etCheques = (HtmlOutputText) findComponentInRoot("lblACDetfp_etCheques");
		}
		return lblACDetfp_etCheques;
	}

	protected HtmlOutputText getLblACDetfp_Cheques() {
		if (lblACDetfp_Cheques == null) {
			lblACDetfp_Cheques = (HtmlOutputText) findComponentInRoot("lblACDetfp_Cheques");
		}
		return lblACDetfp_Cheques;
	}

	protected HtmlLink getLnkACDetfp_TarCred() {
		if (lnkACDetfp_TarCred == null) {
			lnkACDetfp_TarCred = (HtmlLink) findComponentInRoot("lnkACDetfp_TarCred");
		}
		return lnkACDetfp_TarCred;
	}

	protected HtmlOutputText getLblACDetfp_etTarCredito() {
		if (lblACDetfp_etTarCredito == null) {
			lblACDetfp_etTarCredito = (HtmlOutputText) findComponentInRoot("lblACDetfp_etTarCredito");
		}
		return lblACDetfp_etTarCredito;
	}

	protected HtmlOutputText getLblACDetfp_TarCred() {
		if (lblACDetfp_TarCred == null) {
			lblACDetfp_TarCred = (HtmlOutputText) findComponentInRoot("lblACDetfp_TarCred");
		}
		return lblACDetfp_TarCred;
	}

	protected HtmlLink getLnkACDetfp_TarCredManual() {
		if (lnkACDetfp_TarCredManual == null) {
			lnkACDetfp_TarCredManual = (HtmlLink) findComponentInRoot("lnkACDetfp_TarCredManual");
		}
		return lnkACDetfp_TarCredManual;
	}

	protected HtmlOutputText getLblACDetfp_etTarCreditoManual() {
		if (lblACDetfp_etTarCreditoManual == null) {
			lblACDetfp_etTarCreditoManual = (HtmlOutputText) findComponentInRoot("lblACDetfp_etTarCreditoManual");
		}
		return lblACDetfp_etTarCreditoManual;
	}

	protected HtmlOutputText getLblACDetfp_TCmanual() {
		if (lblACDetfp_TCmanual == null) {
			lblACDetfp_TCmanual = (HtmlOutputText) findComponentInRoot("lblACDetfp_TCmanual");
		}
		return lblACDetfp_TCmanual;
	}

	protected HtmlLink getLnkACDetfp_DepDbanco() {
		if (lnkACDetfp_DepDbanco == null) {
			lnkACDetfp_DepDbanco = (HtmlLink) findComponentInRoot("lnkACDetfp_DepDbanco");
		}
		return lnkACDetfp_DepDbanco;
	}

	protected HtmlOutputText getLblACDetfp_etDepDbanco() {
		if (lblACDetfp_etDepDbanco == null) {
			lblACDetfp_etDepDbanco = (HtmlOutputText) findComponentInRoot("lblACDetfp_etDepDbanco");
		}
		return lblACDetfp_etDepDbanco;
	}

	protected HtmlOutputText getLblACDetfp_DepDbanco() {
		if (lblACDetfp_DepDbanco == null) {
			lblACDetfp_DepDbanco = (HtmlOutputText) findComponentInRoot("lblACDetfp_DepDbanco");
		}
		return lblACDetfp_DepDbanco;
	}

	protected HtmlLink getLnkACDetfp_TransBanco() {
		if (lnkACDetfp_TransBanco == null) {
			lnkACDetfp_TransBanco = (HtmlLink) findComponentInRoot("lnkACDetfp_TransBanco");
		}
		return lnkACDetfp_TransBanco;
	}

	protected HtmlOutputText getLblACDetfp_etTransBanco() {
		if (lblACDetfp_etTransBanco == null) {
			lblACDetfp_etTransBanco = (HtmlOutputText) findComponentInRoot("lblACDetfp_etTransBanco");
		}
		return lblACDetfp_etTransBanco;
	}

	protected HtmlOutputText getLblACDetfp_TransBanco() {
		if (lblACDetfp_TransBanco == null) {
			lblACDetfp_TransBanco = (HtmlOutputText) findComponentInRoot("lblACDetfp_TransBanco");
		}
		return lblACDetfp_TransBanco;
	}

	protected HtmlOutputText getLblACDetfp_etTotal() {
		if (lblACDetfp_etTotal == null) {
			lblACDetfp_etTotal = (HtmlOutputText) findComponentInRoot("lblACDetfp_etTotal");
		}
		return lblACDetfp_etTotal;
	}

	protected HtmlOutputText getLblACDetfp_Total() {
		if (lblACDetfp_Total == null) {
			lblACDetfp_Total = (HtmlOutputText) findComponentInRoot("lblACDetfp_Total");
		}
		return lblACDetfp_Total;
	}

	protected HtmlOutputText getLblEtaprobacionArqueo() {
		if (lblEtaprobacionArqueo == null) {
			lblEtaprobacionArqueo = (HtmlOutputText) findComponentInRoot("lblEtaprobacionArqueo");
		}
		return lblEtaprobacionArqueo;
	}

	protected HtmlOutputText getLbletDepSugerido() {
		if (lbletDepSugerido == null) {
			lbletDepSugerido = (HtmlOutputText) findComponentInRoot("lbletDepSugerido");
		}
		return lbletDepSugerido;
	}

	protected HtmlOutputText getLblaprobDepSugerido() {
		if (lblaprobDepSugerido == null) {
			lblaprobDepSugerido = (HtmlOutputText) findComponentInRoot("lblaprobDepSugerido");
		}
		return lblaprobDepSugerido;
	}

	protected HtmlPanelGrid getHpgOpciones1() {
		if (hpgOpciones1 == null) {
			hpgOpciones1 = (HtmlPanelGrid) findComponentInRoot("hpgOpciones1");
		}
		return hpgOpciones1;
	}

	protected HtmlLink getLnkAprobarArqueo() {
		if (lnkAprobarArqueo == null) {
			lnkAprobarArqueo = (HtmlLink) findComponentInRoot("lnkAprobarArqueo");
		}
		return lnkAprobarArqueo;
	}

	protected HtmlLink getLnkAsignarIdPOS() {
		if (lnkAsignarIdPOS == null) {
			lnkAsignarIdPOS = (HtmlLink) findComponentInRoot("lnkAsignarIdPOS");
		}
		return lnkAsignarIdPOS;
	}

	protected HtmlDialogWindow getDwDetalleArqueo() {
		if (dwDetalleArqueo == null) {
			dwDetalleArqueo = (HtmlDialogWindow) findComponentInRoot("dwDetalleArqueo");
		}
		return dwDetalleArqueo;
	}

	protected HtmlDialogWindowClientEvents getCeDetArqueo() {
		if (ceDetArqueo == null) {
			ceDetArqueo = (HtmlDialogWindowClientEvents) findComponentInRoot("ceDetArqueo");
		}
		return ceDetArqueo;
	}

	protected HtmlDialogWindowRoundedCorners getCrDetArqueo() {
		if (crDetArqueo == null) {
			crDetArqueo = (HtmlDialogWindowRoundedCorners) findComponentInRoot("crDetArqueo");
		}
		return crDetArqueo;
	}

	protected HtmlDialogWindowContentPane getCnpDetArqueo() {
		if (cnpDetArqueo == null) {
			cnpDetArqueo = (HtmlDialogWindowContentPane) findComponentInRoot("cnpDetArqueo");
		}
		return cnpDetArqueo;
	}

	protected HtmlLink getLnkponerReferenciaPOS() {
		if (lnkponerReferenciaPOS == null) {
			lnkponerReferenciaPOS = (HtmlLink) findComponentInRoot("lnkponerReferenciaPOS");
		}
		return lnkponerReferenciaPOS;
	}

	protected HtmlLink getLnkReimpresionRptArqueo() {
		if (lnkReimpresionRptArqueo == null) {
			lnkReimpresionRptArqueo = (HtmlLink) findComponentInRoot("lnkReimpresionRptArqueo");
		}
		return lnkReimpresionRptArqueo;
	}

	protected HtmlPanelGrid getHpgOpciones2() {
		if (hpgOpciones2 == null) {
			hpgOpciones2 = (HtmlPanelGrid) findComponentInRoot("hpgOpciones2");
		}
		return hpgOpciones2;
	}

	protected HtmlLink getLnkCerrarDetalleArqueo() {
		if (lnkCerrarDetalleArqueo == null) {
			lnkCerrarDetalleArqueo = (HtmlLink) findComponentInRoot("lnkCerrarDetalleArqueo");
		}
		return lnkCerrarDetalleArqueo;
	}

	protected HtmlDialogWindowHeader getHdFactura() {
		if (hdFactura == null) {
			hdFactura = (HtmlDialogWindowHeader) findComponentInRoot("hdFactura");
		}
		return hdFactura;
	}

	protected HtmlGridView getRv_gvFacturaRegistradas() {
		if (rv_gvFacturaRegistradas == null) {
			rv_gvFacturaRegistradas = (HtmlGridView) findComponentInRoot("rv_gvFacturaRegistradas");
		}
		return rv_gvFacturaRegistradas;
	}

	protected HtmlLink getLnkDetalleFactura() {
		if (lnkDetalleFactura == null) {
			lnkDetalleFactura = (HtmlLink) findComponentInRoot("lnkDetalleFactura");
		}
		return lnkDetalleFactura;
	}

	protected HtmlOutputText getLblNoFactura1() {
		if (lblNoFactura1 == null) {
			lblNoFactura1 = (HtmlOutputText) findComponentInRoot("lblNoFactura1");
		}
		return lblNoFactura1;
	}

	protected HtmlOutputText getLblNoFactura2sd3() {
		if (lblNoFactura2sd3 == null) {
			lblNoFactura2sd3 = (HtmlOutputText) findComponentInRoot("lblNoFactura2sd3");
		}
		return lblNoFactura2sd3;
	}

	protected HtmlOutputText getLblEtCantFacC0() {
		if (lblEtCantFacC0 == null) {
			lblEtCantFacC0 = (HtmlOutputText) findComponentInRoot("lblEtCantFacC0");
		}
		return lblEtCantFacC0;
	}

	protected HtmlOutputText getLblTipofactura1() {
		if (lblTipofactura1 == null) {
			lblTipofactura1 = (HtmlOutputText) findComponentInRoot("lblTipofactura1");
		}
		return lblTipofactura1;
	}

	protected HtmlOutputText getLblTipofactura2() {
		if (lblTipofactura2 == null) {
			lblTipofactura2 = (HtmlOutputText) findComponentInRoot("lblTipofactura2");
		}
		return lblTipofactura2;
	}

	protected HtmlOutputText getLblEtCantFacCr() {
		if (lblEtCantFacCr == null) {
			lblEtCantFacCr = (HtmlOutputText) findComponentInRoot("lblEtCantFacCr");
		}
		return lblEtCantFacCr;
	}

	protected HtmlOutputText getLblTipoPagofact1() {
		if (lblTipoPagofact1 == null) {
			lblTipoPagofact1 = (HtmlOutputText) findComponentInRoot("lblTipoPagofact1");
		}
		return lblTipoPagofact1;
	}

	protected HtmlOutputText getLblTipopagofact2() {
		if (lblTipopagofact2 == null) {
			lblTipopagofact2 = (HtmlOutputText) findComponentInRoot("lblTipopagofact2");
		}
		return lblTipopagofact2;
	}

	protected HtmlOutputText getLblEtTotalFacCo() {
		if (lblEtTotalFacCo == null) {
			lblEtTotalFacCo = (HtmlOutputText) findComponentInRoot("lblEtTotalFacCo");
		}
		return lblEtTotalFacCo;
	}

	protected HtmlOutputText getLblUnineg1() {
		if (lblUnineg1 == null) {
			lblUnineg1 = (HtmlOutputText) findComponentInRoot("lblUnineg1");
		}
		return lblUnineg1;
	}

	protected HtmlOutputText getLblUnineg2() {
		if (lblUnineg2 == null) {
			lblUnineg2 = (HtmlOutputText) findComponentInRoot("lblUnineg2");
		}
		return lblUnineg2;
	}

	protected HtmlOutputText getLblEtTotalFacCr() {
		if (lblEtTotalFacCr == null) {
			lblEtTotalFacCr = (HtmlOutputText) findComponentInRoot("lblEtTotalFacCr");
		}
		return lblEtTotalFacCr;
	}

	protected HtmlOutputText getLblPartida22() {
		if (lblPartida22 == null) {
			lblPartida22 = (HtmlOutputText) findComponentInRoot("lblPartida22");
		}
		return lblPartida22;
	}

	protected HtmlOutputText getLblPartida23() {
		if (lblPartida23 == null) {
			lblPartida23 = (HtmlOutputText) findComponentInRoot("lblPartida23");
		}
		return lblPartida23;
	}

	protected HtmlOutputText getLblFecha22() {
		if (lblFecha22 == null) {
			lblFecha22 = (HtmlOutputText) findComponentInRoot("lblFecha22");
		}
		return lblFecha22;
	}

	protected HtmlOutputText getLblFecha23() {
		if (lblFecha23 == null) {
			lblFecha23 = (HtmlOutputText) findComponentInRoot("lblFecha23");
		}
		return lblFecha23;
	}

	protected HtmlLink getLnkCerrarFacturasReg() {
		if (lnkCerrarFacturasReg == null) {
			lnkCerrarFacturasReg = (HtmlLink) findComponentInRoot("lnkCerrarFacturasReg");
		}
		return lnkCerrarFacturasReg;
	}

	protected HtmlDialogWindow getRv_dwFacturas() {
		if (rv_dwFacturas == null) {
			rv_dwFacturas = (HtmlDialogWindow) findComponentInRoot("rv_dwFacturas");
		}
		return rv_dwFacturas;
	}

	protected HtmlDialogWindowClientEvents getClFacturasRegistradas() {
		if (clFacturasRegistradas == null) {
			clFacturasRegistradas = (HtmlDialogWindowClientEvents) findComponentInRoot("clFacturasRegistradas");
		}
		return clFacturasRegistradas;
	}

	protected HtmlDialogWindowRoundedCorners getCrFacturas() {
		if (crFacturas == null) {
			crFacturas = (HtmlDialogWindowRoundedCorners) findComponentInRoot("crFacturas");
		}
		return crFacturas;
	}

	protected HtmlDialogWindowContentPane getCnpFacturas() {
		if (cnpFacturas == null) {
			cnpFacturas = (HtmlDialogWindowContentPane) findComponentInRoot("cnpFacturas");
		}
		return cnpFacturas;
	}

	protected HtmlColumn getCoNoFactura2sdf33() {
		if (coNoFactura2sdf33 == null) {
			coNoFactura2sdf33 = (HtmlColumn) findComponentInRoot("coNoFactura2sdf33");
		}
		return coNoFactura2sdf33;
	}

	protected HtmlOutputText getLblCantFacCO() {
		if (lblCantFacCO == null) {
			lblCantFacCO = (HtmlOutputText) findComponentInRoot("lblCantFacCO");
		}
		return lblCantFacCO;
	}

	protected HtmlColumn getCoTipofactura2() {
		if (coTipofactura2 == null) {
			coTipofactura2 = (HtmlColumn) findComponentInRoot("coTipofactura2");
		}
		return coTipofactura2;
	}

	protected HtmlOutputText getLblCantFacCr() {
		if (lblCantFacCr == null) {
			lblCantFacCr = (HtmlOutputText) findComponentInRoot("lblCantFacCr");
		}
		return lblCantFacCr;
	}

	protected HtmlColumn getCoTipoPagofact() {
		if (coTipoPagofact == null) {
			coTipoPagofact = (HtmlColumn) findComponentInRoot("coTipoPagofact");
		}
		return coTipoPagofact;
	}

	protected HtmlOutputText getLblTotalFacCo() {
		if (lblTotalFacCo == null) {
			lblTotalFacCo = (HtmlOutputText) findComponentInRoot("lblTotalFacCo");
		}
		return lblTotalFacCo;
	}

	protected HtmlColumn getCoUnineg2() {
		if (coUnineg2 == null) {
			coUnineg2 = (HtmlColumn) findComponentInRoot("coUnineg2");
		}
		return coUnineg2;
	}

	protected HtmlOutputText getLblTotalFacCr() {
		if (lblTotalFacCr == null) {
			lblTotalFacCr = (HtmlOutputText) findComponentInRoot("lblTotalFacCr");
		}
		return lblTotalFacCr;
	}

	protected HtmlColumn getCoTotal() {
		if (coTotal == null) {
			coTotal = (HtmlColumn) findComponentInRoot("coTotal");
		}
		return coTotal;
	}

	protected HtmlColumn getCoFecha2() {
		if (coFecha2 == null) {
			coFecha2 = (HtmlColumn) findComponentInRoot("coFecha2");
		}
		return coFecha2;
	}

	protected HtmlPanelGrid getHpgFacturasReg1() {
		if (hpgFacturasReg1 == null) {
			hpgFacturasReg1 = (HtmlPanelGrid) findComponentInRoot("hpgFacturasReg1");
		}
		return hpgFacturasReg1;
	}

	protected HtmlDialogWindowHeader getHdDetTrecxMetPago() {
		if (hdDetTrecxMetPago == null) {
			hdDetTrecxMetPago = (HtmlDialogWindowHeader) findComponentInRoot("hdDetTrecxMetPago");
		}
		return hdDetTrecxMetPago;
	}

	protected HtmlGridView getGvRecibosxTipoyMetodopago() {
		if (gvRecibosxTipoyMetodopago == null) {
			gvRecibosxTipoyMetodopago = (HtmlGridView) findComponentInRoot("gvRecibosxTipoyMetodopago");
		}
		return gvRecibosxTipoyMetodopago;
	}

	protected HtmlOutputText getLblRcTmp_numrec() {
		if (lblRcTmp_numrec == null) {
			lblRcTmp_numrec = (HtmlOutputText) findComponentInRoot("lblRcTmp_numrec");
		}
		return lblRcTmp_numrec;
	}

	protected HtmlOutputText getLblRcTmp_tiporec() {
		if (lblRcTmp_tiporec == null) {
			lblRcTmp_tiporec = (HtmlOutputText) findComponentInRoot("lblRcTmp_tiporec");
		}
		return lblRcTmp_tiporec;
	}

	protected HtmlOutputText getLblRcTmp_cliente() {
		if (lblRcTmp_cliente == null) {
			lblRcTmp_cliente = (HtmlOutputText) findComponentInRoot("lblRcTmp_cliente");
		}
		return lblRcTmp_cliente;
	}

	protected HtmlOutputText getLblRcTmp_monto() {
		if (lblRcTmp_monto == null) {
			lblRcTmp_monto = (HtmlOutputText) findComponentInRoot("lblRcTmp_monto");
		}
		return lblRcTmp_monto;
	}

	protected HtmlOutputText getLblRcTmp_montoapl() {
		if (lblRcTmp_montoapl == null) {
			lblRcTmp_montoapl = (HtmlOutputText) findComponentInRoot("lblRcTmp_montoapl");
		}
		return lblRcTmp_montoapl;
	}

	protected HtmlOutputText getLblRcTmp_hora() {
		if (lblRcTmp_hora == null) {
			lblRcTmp_hora = (HtmlOutputText) findComponentInRoot("lblRcTmp_hora");
		}
		return lblRcTmp_hora;
	}

	protected HtmlOutputText getLblRcTmp_Refer1() {
		if (lblRcTmp_Refer1 == null) {
			lblRcTmp_Refer1 = (HtmlOutputText) findComponentInRoot("lblRcTmp_Refer1");
		}
		return lblRcTmp_Refer1;
	}

	protected HtmlOutputText getLblRcTmp_Refer2() {
		if (lblRcTmp_Refer2 == null) {
			lblRcTmp_Refer2 = (HtmlOutputText) findComponentInRoot("lblRcTmp_Refer2");
		}
		return lblRcTmp_Refer2;
	}

	protected HtmlOutputText getLblRcTmp_refer4() {
		if (lblRcTmp_refer4 == null) {
			lblRcTmp_refer4 = (HtmlOutputText) findComponentInRoot("lblRcTmp_refer4");
		}
		return lblRcTmp_refer4;
	}

	protected HtmlLink getLnkCerrarDetRecxtipoyMetpago() {
		if (lnkCerrarDetRecxtipoyMetpago == null) {
			lnkCerrarDetRecxtipoyMetpago = (HtmlLink) findComponentInRoot("lnkCerrarDetRecxtipoyMetpago");
		}
		return lnkCerrarDetRecxtipoyMetpago;
	}

	protected HtmlDialogWindow getRv_dwReciboxtipoMetPago() {
		if (rv_dwReciboxtipoMetPago == null) {
			rv_dwReciboxtipoMetPago = (HtmlDialogWindow) findComponentInRoot("rv_dwReciboxtipoMetPago");
		}
		return rv_dwReciboxtipoMetPago;
	}

	protected HtmlDialogWindowClientEvents getClDetTrecxMetPago() {
		if (clDetTrecxMetPago == null) {
			clDetTrecxMetPago = (HtmlDialogWindowClientEvents) findComponentInRoot("clDetTrecxMetPago");
		}
		return clDetTrecxMetPago;
	}

	protected HtmlDialogWindowRoundedCorners getCrDetTrecxMetPago() {
		if (crDetTrecxMetPago == null) {
			crDetTrecxMetPago = (HtmlDialogWindowRoundedCorners) findComponentInRoot("crDetTrecxMetPago");
		}
		return crDetTrecxMetPago;
	}

	protected HtmlDialogWindowContentPane getCnpDetTrecxMetPago() {
		if (cnpDetTrecxMetPago == null) {
			cnpDetTrecxMetPago = (HtmlDialogWindowContentPane) findComponentInRoot("cnpDetTrecxMetPago");
		}
		return cnpDetTrecxMetPago;
	}

	protected HtmlDialogWindowHeader getHdRecxTipoIng() {
		if (hdRecxTipoIng == null) {
			hdRecxTipoIng = (HtmlDialogWindowHeader) findComponentInRoot("hdRecxTipoIng");
		}
		return hdRecxTipoIng;
	}

	protected HtmlGridView getGvRecibosIngresos() {
		if (gvRecibosIngresos == null) {
			gvRecibosIngresos = (HtmlGridView) findComponentInRoot("gvRecibosIngresos");
		}
		return gvRecibosIngresos;
	}

	protected HtmlLink getLnkDetalleRecibo() {
		if (lnkDetalleRecibo == null) {
			lnkDetalleRecibo = (HtmlLink) findComponentInRoot("lnkDetalleRecibo");
		}
		return lnkDetalleRecibo;
	}

	protected HtmlOutputText getLblVNC_numrec() {
		if (lblVNC_numrec == null) {
			lblVNC_numrec = (HtmlOutputText) findComponentInRoot("lblVNC_numrec");
		}
		return lblVNC_numrec;
	}

	protected HtmlOutputText getLblVNC_cliente0() {
		if (lblVNC_cliente0 == null) {
			lblVNC_cliente0 = (HtmlOutputText) findComponentInRoot("lblVNC_cliente0");
		}
		return lblVNC_cliente0;
	}

	protected HtmlOutputText getLblVNC_cliente1() {
		if (lblVNC_cliente1 == null) {
			lblVNC_cliente1 = (HtmlOutputText) findComponentInRoot("lblVNC_cliente1");
		}
		return lblVNC_cliente1;
	}

	protected HtmlOutputText getLblVNC_hora0() {
		if (lblVNC_hora0 == null) {
			lblVNC_hora0 = (HtmlOutputText) findComponentInRoot("lblVNC_hora0");
		}
		return lblVNC_hora0;
	}

	protected HtmlOutputText getLblVNC_hora1() {
		if (lblVNC_hora1 == null) {
			lblVNC_hora1 = (HtmlOutputText) findComponentInRoot("lblVNC_hora1");
		}
		return lblVNC_hora1;
	}

	protected HtmlOutputText getLblVNC_monto0() {
		if (lblVNC_monto0 == null) {
			lblVNC_monto0 = (HtmlOutputText) findComponentInRoot("lblVNC_monto0");
		}
		return lblVNC_monto0;
	}

	protected HtmlOutputText getLblVNC_monto1() {
		if (lblVNC_monto1 == null) {
			lblVNC_monto1 = (HtmlOutputText) findComponentInRoot("lblVNC_monto1");
		}
		return lblVNC_monto1;
	}

	protected HtmlLink getLnkCerrarDetRecxTipoIng() {
		if (lnkCerrarDetRecxTipoIng == null) {
			lnkCerrarDetRecxTipoIng = (HtmlLink) findComponentInRoot("lnkCerrarDetRecxTipoIng");
		}
		return lnkCerrarDetRecxTipoIng;
	}

	protected HtmlDialogWindow getDwRecibosxTipoIngreso() {
		if (dwRecibosxTipoIngreso == null) {
			dwRecibosxTipoIngreso = (HtmlDialogWindow) findComponentInRoot("dwRecibosxTipoIngreso");
		}
		return dwRecibosxTipoIngreso;
	}

	protected HtmlDialogWindowClientEvents getCeRecxTipoIng() {
		if (ceRecxTipoIng == null) {
			ceRecxTipoIng = (HtmlDialogWindowClientEvents) findComponentInRoot("ceRecxTipoIng");
		}
		return ceRecxTipoIng;
	}

	protected HtmlDialogWindowRoundedCorners getCrRecxTipoIng() {
		if (crRecxTipoIng == null) {
			crRecxTipoIng = (HtmlDialogWindowRoundedCorners) findComponentInRoot("crRecxTipoIng");
		}
		return crRecxTipoIng;
	}

	protected HtmlDialogWindowContentPane getCnpRecxTipoIng() {
		if (cnpRecxTipoIng == null) {
			cnpRecxTipoIng = (HtmlDialogWindowContentPane) findComponentInRoot("cnpRecxTipoIng");
		}
		return cnpRecxTipoIng;
	}

	protected HtmlColumn getCoHora() {
		if (coHora == null) {
			coHora = (HtmlColumn) findComponentInRoot("coHora");
		}
		return coHora;
	}

	protected HtmlPanelGrid getHpgDetRecibosxTipoIng() {
		if (hpgDetRecibosxTipoIng == null) {
			hpgDetRecibosxTipoIng = (HtmlPanelGrid) findComponentInRoot("hpgDetRecibosxTipoIng");
		}
		return hpgDetRecibosxTipoIng;
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

	protected HtmlGridView getGvDfacturasDiario() {
		if (gvDfacturasDiario == null) {
			gvDfacturasDiario = (HtmlGridView) findComponentInRoot("gvDfacturasDiario");
		}
		return gvDfacturasDiario;
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

	protected HtmlOutputText getTxtSubtotal() {
		if (txtSubtotal == null) {
			txtSubtotal = (HtmlOutputText) findComponentInRoot("txtSubtotal");
		}
		return txtSubtotal;
	}

	protected HtmlOutputText getText28() {
		if (text28 == null) {
			text28 = (HtmlOutputText) findComponentInRoot("text28");
		}
		return text28;
	}

	protected HtmlOutputText getTxtIva() {
		if (txtIva == null) {
			txtIva = (HtmlOutputText) findComponentInRoot("txtIva");
		}
		return txtIva;
	}

	protected HtmlOutputText getLblTotalDetCont() {
		if (lblTotalDetCont == null) {
			lblTotalDetCont = (HtmlOutputText) findComponentInRoot("lblTotalDetCont");
		}
		return lblTotalDetCont;
	}

	protected HtmlOutputText getTxtTotal() {
		if (txtTotal == null) {
			txtTotal = (HtmlOutputText) findComponentInRoot("txtTotal");
		}
		return txtTotal;
	}

	protected HtmlLink getLnkCerrarDetalleContado() {
		if (lnkCerrarDetalleContado == null) {
			lnkCerrarDetalleContado = (HtmlLink) findComponentInRoot("lnkCerrarDetalleContado");
		}
		return lnkCerrarDetalleContado;
	}

	protected HtmlDialogWindow getRv_dwDetalleFactura() {
		if (rv_dwDetalleFactura == null) {
			rv_dwDetalleFactura = (HtmlDialogWindow) findComponentInRoot("rv_dwDetalleFactura");
		}
		return rv_dwDetalleFactura;
	}

	protected HtmlDialogWindowClientEvents getClDetalleFacturaCon() {
		if (clDetalleFacturaCon == null) {
			clDetalleFacturaCon = (HtmlDialogWindowClientEvents) findComponentInRoot("clDetalleFacturaCon");
		}
		return clDetalleFacturaCon;
	}

	protected HtmlDialogWindowRoundedCorners getCrDetalleFacturaCon() {
		if (crDetalleFacturaCon == null) {
			crDetalleFacturaCon = (HtmlDialogWindowRoundedCorners) findComponentInRoot("crDetalleFacturaCon");
		}
		return crDetalleFacturaCon;
	}

	protected HtmlDialogWindowContentPane getCnpDetalleFacturaCon() {
		if (cnpDetalleFacturaCon == null) {
			cnpDetalleFacturaCon = (HtmlDialogWindowContentPane) findComponentInRoot("cnpDetalleFacturaCon");
		}
		return cnpDetalleFacturaCon;
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

	protected HtmlDropDownList getDdlDetalleFacCon() {
		if (ddlDetalleFacCon == null) {
			ddlDetalleFacCon = (HtmlDropDownList) findComponentInRoot("ddlDetalleFacCon");
		}
		return ddlDetalleFacCon;
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

	protected HtmlDialogWindowAutoPostBackFlags getApbDetalleFactura() {
		if (apbDetalleFactura == null) {
			apbDetalleFactura = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbDetalleFactura");
		}
		return apbDetalleFactura;
	}

	protected HtmlDialogWindowHeader getHdDetalleRecibo() {
		if (hdDetalleRecibo == null) {
			hdDetalleRecibo = (HtmlDialogWindowHeader) findComponentInRoot("hdDetalleRecibo");
		}
		return hdDetalleRecibo;
	}

	protected HtmlJspPanel getJspPanel5() {
		if (jspPanel5 == null) {
			jspPanel5 = (HtmlJspPanel) findComponentInRoot("jspPanel5");
		}
		return jspPanel5;
	}

	protected HtmlOutputText getLblCodCli() {
		if (lblCodCli == null) {
			lblCodCli = (HtmlOutputText) findComponentInRoot("lblCodCli");
		}
		return lblCodCli;
	}

	protected HtmlOutputText getTxtDRMonedaRecibo() {
		if (txtDRMonedaRecibo == null) {
			txtDRMonedaRecibo = (HtmlOutputText) findComponentInRoot("txtDRMonedaRecibo");
		}
		return txtDRMonedaRecibo;
	}

	protected HtmlOutputText getTxtDRNomCliente() {
		if (txtDRNomCliente == null) {
			txtDRNomCliente = (HtmlOutputText) findComponentInRoot("txtDRNomCliente");
		}
		return txtDRNomCliente;
	}

	protected HtmlGridView getRv_gvDetalleRecibo() {
		if (rv_gvDetalleRecibo == null) {
			rv_gvDetalleRecibo = (HtmlGridView) findComponentInRoot("rv_gvDetalleRecibo");
		}
		return rv_gvDetalleRecibo;
	}

	protected HtmlColumn getCoDRCoditem() {
		if (coDRCoditem == null) {
			coDRCoditem = (HtmlColumn) findComponentInRoot("coDRCoditem");
		}
		return coDRCoditem;
	}

	protected HtmlOutputText getLblDRCoditem1() {
		if (lblDRCoditem1 == null) {
			lblDRCoditem1 = (HtmlOutputText) findComponentInRoot("lblDRCoditem1");
		}
		return lblDRCoditem1;
	}

	protected HtmlOutputText getLblDRCoditem2() {
		if (lblDRCoditem2 == null) {
			lblDRCoditem2 = (HtmlOutputText) findComponentInRoot("lblDRCoditem2");
		}
		return lblDRCoditem2;
	}

	protected HtmlOutputText getLblDRDescitem1() {
		if (lblDRDescitem1 == null) {
			lblDRDescitem1 = (HtmlOutputText) findComponentInRoot("lblDRDescitem1");
		}
		return lblDRDescitem1;
	}

	protected HtmlOutputText getLblDRDescitem2() {
		if (lblDRDescitem2 == null) {
			lblDRDescitem2 = (HtmlOutputText) findComponentInRoot("lblDRDescitem2");
		}
		return lblDRDescitem2;
	}

	protected HtmlOutputText getLblDRCantDetalle1() {
		if (lblDRCantDetalle1 == null) {
			lblDRCantDetalle1 = (HtmlOutputText) findComponentInRoot("lblDRCantDetalle1");
		}
		return lblDRCantDetalle1;
	}

	protected HtmlOutputText getLblDRCantDetalle2() {
		if (lblDRCantDetalle2 == null) {
			lblDRCantDetalle2 = (HtmlOutputText) findComponentInRoot("lblDRCantDetalle2");
		}
		return lblDRCantDetalle2;
	}

	protected HtmlOutputText getLblDRPrecionunitDetalle1() {
		if (lblDRPrecionunitDetalle1 == null) {
			lblDRPrecionunitDetalle1 = (HtmlOutputText) findComponentInRoot("lblDRPrecionunitDetalle1");
		}
		return lblDRPrecionunitDetalle1;
	}

	protected HtmlOutputText getLblDRPrecionunitDetalle2() {
		if (lblDRPrecionunitDetalle2 == null) {
			lblDRPrecionunitDetalle2 = (HtmlOutputText) findComponentInRoot("lblDRPrecionunitDetalle2");
		}
		return lblDRPrecionunitDetalle2;
	}

	protected HtmlOutputText getLblDRImpuestoDetalle1() {
		if (lblDRImpuestoDetalle1 == null) {
			lblDRImpuestoDetalle1 = (HtmlOutputText) findComponentInRoot("lblDRImpuestoDetalle1");
		}
		return lblDRImpuestoDetalle1;
	}

	protected HtmlOutputText getLblDRImpuestoDetalle2() {
		if (lblDRImpuestoDetalle2 == null) {
			lblDRImpuestoDetalle2 = (HtmlOutputText) findComponentInRoot("lblDRImpuestoDetalle2");
		}
		return lblDRImpuestoDetalle2;
	}

	protected HtmlOutputText getLblRefer11() {
		if (lblRefer11 == null) {
			lblRefer11 = (HtmlOutputText) findComponentInRoot("lblRefer11");
		}
		return lblRefer11;
	}

	protected HtmlOutputText getLblRefer12() {
		if (lblRefer12 == null) {
			lblRefer12 = (HtmlOutputText) findComponentInRoot("lblRefer12");
		}
		return lblRefer12;
	}

	protected HtmlOutputText getLblRefer21() {
		if (lblRefer21 == null) {
			lblRefer21 = (HtmlOutputText) findComponentInRoot("lblRefer21");
		}
		return lblRefer21;
	}

	protected HtmlOutputText getLblRefer22() {
		if (lblRefer22 == null) {
			lblRefer22 = (HtmlOutputText) findComponentInRoot("lblRefer22");
		}
		return lblRefer22;
	}

	protected HtmlOutputText getLblRefer31() {
		if (lblRefer31 == null) {
			lblRefer31 = (HtmlOutputText) findComponentInRoot("lblRefer31");
		}
		return lblRefer31;
	}

	protected HtmlOutputText getLblRefer3() {
		if (lblRefer3 == null) {
			lblRefer3 = (HtmlOutputText) findComponentInRoot("lblRefer3");
		}
		return lblRefer3;
	}

	protected HtmlOutputText getLblRefer32() {
		if (lblRefer32 == null) {
			lblRefer32 = (HtmlOutputText) findComponentInRoot("lblRefer32");
		}
		return lblRefer32;
	}

	protected HtmlOutputText getLblRefer4() {
		if (lblRefer4 == null) {
			lblRefer4 = (HtmlOutputText) findComponentInRoot("lblRefer4");
		}
		return lblRefer4;
	}

	protected HtmlGridView getRv_gvFacturasRecibo() {
		if (rv_gvFacturasRecibo == null) {
			rv_gvFacturasRecibo = (HtmlGridView) findComponentInRoot("rv_gvFacturasRecibo");
		}
		return rv_gvFacturasRecibo;
	}

	protected HtmlOutputText getLblDRNoFactura1() {
		if (lblDRNoFactura1 == null) {
			lblDRNoFactura1 = (HtmlOutputText) findComponentInRoot("lblDRNoFactura1");
		}
		return lblDRNoFactura1;
	}

	protected HtmlOutputText getLblDRTipofactura1() {
		if (lblDRTipofactura1 == null) {
			lblDRTipofactura1 = (HtmlOutputText) findComponentInRoot("lblDRTipofactura1");
		}
		return lblDRTipofactura1;
	}

	protected HtmlOutputText getLblDRTipofactura2() {
		if (lblDRTipofactura2 == null) {
			lblDRTipofactura2 = (HtmlOutputText) findComponentInRoot("lblDRTipofactura2");
		}
		return lblDRTipofactura2;
	}

	protected HtmlOutputText getLblDRUnineg1() {
		if (lblDRUnineg1 == null) {
			lblDRUnineg1 = (HtmlOutputText) findComponentInRoot("lblDRUnineg1");
		}
		return lblDRUnineg1;
	}

	protected HtmlOutputText getLblDRUnineg2() {
		if (lblDRUnineg2 == null) {
			lblDRUnineg2 = (HtmlOutputText) findComponentInRoot("lblDRUnineg2");
		}
		return lblDRUnineg2;
	}

	protected HtmlOutputText getLblDRMoneda1() {
		if (lblDRMoneda1 == null) {
			lblDRMoneda1 = (HtmlOutputText) findComponentInRoot("lblDRMoneda1");
		}
		return lblDRMoneda1;
	}

	protected HtmlOutputText getLblDRMoneda2() {
		if (lblDRMoneda2 == null) {
			lblDRMoneda2 = (HtmlOutputText) findComponentInRoot("lblDRMoneda2");
		}
		return lblDRMoneda2;
	}

	protected HtmlOutputText getLblDRFecha22() {
		if (lblDRFecha22 == null) {
			lblDRFecha22 = (HtmlOutputText) findComponentInRoot("lblDRFecha22");
		}
		return lblDRFecha22;
	}

	protected HtmlOutputText getLblDRFecha23() {
		if (lblDRFecha23 == null) {
			lblDRFecha23 = (HtmlOutputText) findComponentInRoot("lblDRFecha23");
		}
		return lblDRFecha23;
	}

	protected HtmlOutputText getLblDRPartida22() {
		if (lblDRPartida22 == null) {
			lblDRPartida22 = (HtmlOutputText) findComponentInRoot("lblDRPartida22");
		}
		return lblDRPartida22;
	}

	protected HtmlOutputText getLblDRPartida23() {
		if (lblDRPartida23 == null) {
			lblDRPartida23 = (HtmlOutputText) findComponentInRoot("lblDRPartida23");
		}
		return lblDRPartida23;
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

	protected HtmlOutputText getLblDRSubtotalDetalleContado() {
		if (lblDRSubtotalDetalleContado == null) {
			lblDRSubtotalDetalleContado = (HtmlOutputText) findComponentInRoot("lblDRSubtotalDetalleContado");
		}
		return lblDRSubtotalDetalleContado;
	}

	protected HtmlOutputText getTxtDRSubtotalDetalle() {
		if (txtDRSubtotalDetalle == null) {
			txtDRSubtotalDetalle = (HtmlOutputText) findComponentInRoot("txtDRSubtotalDetalle");
		}
		return txtDRSubtotalDetalle;
	}

	protected HtmlOutputText getTxtDRIvaDetalle() {
		if (txtDRIvaDetalle == null) {
			txtDRIvaDetalle = (HtmlOutputText) findComponentInRoot("txtDRIvaDetalle");
		}
		return txtDRIvaDetalle;
	}

	protected HtmlOutputText getLblDRTotalDetCont() {
		if (lblDRTotalDetCont == null) {
			lblDRTotalDetCont = (HtmlOutputText) findComponentInRoot("lblDRTotalDetCont");
		}
		return lblDRTotalDetCont;
	}

	protected HtmlOutputText getTxtDRTotalDetalle() {
		if (txtDRTotalDetalle == null) {
			txtDRTotalDetalle = (HtmlOutputText) findComponentInRoot("txtDRTotalDetalle");
		}
		return txtDRTotalDetalle;
	}

	protected HtmlLink getLnkCerrarDetalleRecibo() {
		if (lnkCerrarDetalleRecibo == null) {
			lnkCerrarDetalleRecibo = (HtmlLink) findComponentInRoot("lnkCerrarDetalleRecibo");
		}
		return lnkCerrarDetalleRecibo;
	}

	protected HtmlDialogWindow getRv_dwDetalleRecibo() {
		if (rv_dwDetalleRecibo == null) {
			rv_dwDetalleRecibo = (HtmlDialogWindow) findComponentInRoot("rv_dwDetalleRecibo");
		}
		return rv_dwDetalleRecibo;
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

	protected HtmlOutputText getTxtHoraRecibo() {
		if (txtHoraRecibo == null) {
			txtHoraRecibo = (HtmlOutputText) findComponentInRoot("txtHoraRecibo");
		}
		return txtHoraRecibo;
	}

	protected HtmlOutputText getTxtNoRecibo() {
		if (txtNoRecibo == null) {
			txtNoRecibo = (HtmlOutputText) findComponentInRoot("txtNoRecibo");
		}
		return txtNoRecibo;
	}

	protected HtmlOutputText getTxtDRCodCli() {
		if (txtDRCodCli == null) {
			txtDRCodCli = (HtmlOutputText) findComponentInRoot("txtDRCodCli");
		}
		return txtDRCodCli;
	}

	protected HtmlOutputText getTxtNoBatch() {
		if (txtNoBatch == null) {
			txtNoBatch = (HtmlOutputText) findComponentInRoot("txtNoBatch");
		}
		return txtNoBatch;
	}

	protected HtmlColumn getCoDRDescitemCont() {
		if (coDRDescitemCont == null) {
			coDRDescitemCont = (HtmlColumn) findComponentInRoot("coDRDescitemCont");
		}
		return coDRDescitemCont;
	}

	protected HtmlColumn getCoDRCant() {
		if (coDRCant == null) {
			coDRCant = (HtmlColumn) findComponentInRoot("coDRCant");
		}
		return coDRCant;
	}

	protected HtmlColumn getCoDRPreciounit() {
		if (coDRPreciounit == null) {
			coDRPreciounit = (HtmlColumn) findComponentInRoot("coDRPreciounit");
		}
		return coDRPreciounit;
	}

	protected HtmlColumn getCoDRImpuesto() {
		if (coDRImpuesto == null) {
			coDRImpuesto = (HtmlColumn) findComponentInRoot("coDRImpuesto");
		}
		return coDRImpuesto;
	}

	protected HtmlColumn getCoRefer1() {
		if (coRefer1 == null) {
			coRefer1 = (HtmlColumn) findComponentInRoot("coRefer1");
		}
		return coRefer1;
	}

	protected HtmlColumn getCoRefer2() {
		if (coRefer2 == null) {
			coRefer2 = (HtmlColumn) findComponentInRoot("coRefer2");
		}
		return coRefer2;
	}

	protected HtmlColumn getCoRefer3() {
		if (coRefer3 == null) {
			coRefer3 = (HtmlColumn) findComponentInRoot("coRefer3");
		}
		return coRefer3;
	}

	protected HtmlColumn getCoRefer4() {
		if (coRefer4 == null) {
			coRefer4 = (HtmlColumn) findComponentInRoot("coRefer4");
		}
		return coRefer4;
	}

	protected HtmlColumn getCoDRTipofactura2() {
		if (coDRTipofactura2 == null) {
			coDRTipofactura2 = (HtmlColumn) findComponentInRoot("coDRTipofactura2");
		}
		return coDRTipofactura2;
	}

	protected HtmlColumn getCoDRUnineg2() {
		if (coDRUnineg2 == null) {
			coDRUnineg2 = (HtmlColumn) findComponentInRoot("coDRUnineg2");
		}
		return coDRUnineg2;
	}

	protected HtmlColumn getCoDRMoneda2() {
		if (coDRMoneda2 == null) {
			coDRMoneda2 = (HtmlColumn) findComponentInRoot("coDRMoneda2");
		}
		return coDRMoneda2;
	}

	protected HtmlColumn getCoDRFecha2() {
		if (coDRFecha2 == null) {
			coDRFecha2 = (HtmlColumn) findComponentInRoot("coDRFecha2");
		}
		return coDRFecha2;
	}

	protected HtmlColumn getCoPartida2() {
		if (coPartida2 == null) {
			coPartida2 = (HtmlColumn) findComponentInRoot("coPartida2");
		}
		return coPartida2;
	}

	protected HtmlDialogWindowAutoPostBackFlags getApbDetalle() {
		if (apbDetalle == null) {
			apbDetalle = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbDetalle");
		}
		return apbDetalle;
	}

	protected HtmlDialogWindowHeader getHdVarqueo() {
		if (hdVarqueo == null) {
			hdVarqueo = (HtmlDialogWindowHeader) findComponentInRoot("hdVarqueo");
		}
		return hdVarqueo;
	}

	protected HtmlJspPanel getJspVarqueo0() {
		if (jspVarqueo0 == null) {
			jspVarqueo0 = (HtmlJspPanel) findComponentInRoot("jspVarqueo0");
		}
		return jspVarqueo0;
	}

	protected HtmlGraphicImageEx getImgVarqueo() {
		if (imgVarqueo == null) {
			imgVarqueo = (HtmlGraphicImageEx) findComponentInRoot("imgVarqueo");
		}
		return imgVarqueo;
	}

	protected HtmlOutputText getLblValidarAprobacion() {
		if (lblValidarAprobacion == null) {
			lblValidarAprobacion = (HtmlOutputText) findComponentInRoot("lblValidarAprobacion");
		}
		return lblValidarAprobacion;
	}

	protected HtmlLink getLnkCerrarVarqueo() {
		if (lnkCerrarVarqueo == null) {
			lnkCerrarVarqueo = (HtmlLink) findComponentInRoot("lnkCerrarVarqueo");
		}
		return lnkCerrarVarqueo;
	}

	protected HtmlDialogWindow getDwValidarAprobacionArqueo() {
		if (dwValidarAprobacionArqueo == null) {
			dwValidarAprobacionArqueo = (HtmlDialogWindow) findComponentInRoot("dwValidarAprobacionArqueo");
		}
		return dwValidarAprobacionArqueo;
	}

	protected HtmlDialogWindowClientEvents getCeVarqueo() {
		if (ceVarqueo == null) {
			ceVarqueo = (HtmlDialogWindowClientEvents) findComponentInRoot("ceVarqueo");
		}
		return ceVarqueo;
	}

	protected HtmlDialogWindowRoundedCorners getRcVarqueo() {
		if (rcVarqueo == null) {
			rcVarqueo = (HtmlDialogWindowRoundedCorners) findComponentInRoot("rcVarqueo");
		}
		return rcVarqueo;
	}

	protected HtmlDialogWindowContentPane getCpVarqueo() {
		if (cpVarqueo == null) {
			cpVarqueo = (HtmlDialogWindowContentPane) findComponentInRoot("cpVarqueo");
		}
		return cpVarqueo;
	}

	protected HtmlDialogWindowAutoPostBackFlags getApbReciboContado2() {
		if (apbReciboContado2 == null) {
			apbReciboContado2 = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbReciboContado2");
		}
		return apbReciboContado2;
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

	protected HtmlLink getLnkCerrarAprobArqueoSi() {
		if (lnkCerrarAprobArqueoSi == null) {
			lnkCerrarAprobArqueoSi = (HtmlLink) findComponentInRoot("lnkCerrarAprobArqueoSi");
		}
		return lnkCerrarAprobArqueoSi;
	}

	protected HtmlDialogWindow getRv_dwCancelarAprArqueo() {
		if (rv_dwCancelarAprArqueo == null) {
			rv_dwCancelarAprArqueo = (HtmlDialogWindow) findComponentInRoot("rv_dwCancelarAprArqueo");
		}
		return rv_dwCancelarAprArqueo;
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

	protected HtmlLink getLnkCerrarAprobArqueoNo() {
		if (lnkCerrarAprobArqueoNo == null) {
			lnkCerrarAprobArqueoNo = (HtmlLink) findComponentInRoot("lnkCerrarAprobArqueoNo");
		}
		return lnkCerrarAprobArqueoNo;
	}

	protected HtmlDialogWindowAutoPostBackFlags getApbAskCancel() {
		if (apbAskCancel == null) {
			apbAskCancel = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbAskCancel");
		}
		return apbAskCancel;
	}

	protected HtmlDialogWindowHeader getHdAprobarArqueo() {
		if (hdAprobarArqueo == null) {
			hdAprobarArqueo = (HtmlDialogWindowHeader) findComponentInRoot("hdAprobarArqueo");
		}
		return hdAprobarArqueo;
	}

	protected HtmlPanelGrid getGridAprobarArqueo() {
		if (gridAprobarArqueo == null) {
			gridAprobarArqueo = (HtmlPanelGrid) findComponentInRoot("gridAprobarArqueo");
		}
		return gridAprobarArqueo;
	}

	protected HtmlGraphicImageEx getImageEx2AprobarArqueo() {
		if (imageEx2AprobarArqueo == null) {
			imageEx2AprobarArqueo = (HtmlGraphicImageEx) findComponentInRoot("imageEx2AprobarArqueo");
		}
		return imageEx2AprobarArqueo;
	}

	protected HtmlDialogWindow getRv_dwConfirmarProcesarArq() {
		if (rv_dwConfirmarProcesarArq == null) {
			rv_dwConfirmarProcesarArq = (HtmlDialogWindow) findComponentInRoot("rv_dwConfirmarProcesarArq");
		}
		return rv_dwConfirmarProcesarArq;
	}

	protected HtmlDialogWindowClientEvents getCleAprobarArqueo() {
		if (cleAprobarArqueo == null) {
			cleAprobarArqueo = (HtmlDialogWindowClientEvents) findComponentInRoot("cleAprobarArqueo");
		}
		return cleAprobarArqueo;
	}

	protected HtmlDialogWindowRoundedCorners getRcAprobarArqueo() {
		if (rcAprobarArqueo == null) {
			rcAprobarArqueo = (HtmlDialogWindowRoundedCorners) findComponentInRoot("rcAprobarArqueo");
		}
		return rcAprobarArqueo;
	}

	protected HtmlDialogWindowContentPane getCpAprobarArqueo() {
		if (cpAprobarArqueo == null) {
			cpAprobarArqueo = (HtmlDialogWindowContentPane) findComponentInRoot("cpAprobarArqueo");
		}
		return cpAprobarArqueo;
	}

	protected HtmlOutputText getLblAprobarArqueo() {
		if (lblAprobarArqueo == null) {
			lblAprobarArqueo = (HtmlOutputText) findComponentInRoot("lblAprobarArqueo");
		}
		return lblAprobarArqueo;
	}

	protected HtmlJspPanel getJspPanelAprobarArqueo() {
		if (jspPanelAprobarArqueo == null) {
			jspPanelAprobarArqueo = (HtmlJspPanel) findComponentInRoot("jspPanelAprobarArqueo");
		}
		return jspPanelAprobarArqueo;
	}

	protected HtmlLink getLnkAprobArqueoNo() {
		if (lnkAprobArqueoNo == null) {
			lnkAprobArqueoNo = (HtmlLink) findComponentInRoot("lnkAprobArqueoNo");
		}
		return lnkAprobArqueoNo;
	}

	protected HtmlDialogWindowAutoPostBackFlags getApbAprobarArqueo() {
		if (apbAprobarArqueo == null) {
			apbAprobarArqueo = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbAprobarArqueo");
		}
		return apbAprobarArqueo;
	}

	protected HtmlDialogWindowHeader getHdRechazarArqueo() {
		if (hdRechazarArqueo == null) {
			hdRechazarArqueo = (HtmlDialogWindowHeader) findComponentInRoot("hdRechazarArqueo");
		}
		return hdRechazarArqueo;
	}

	protected HtmlPanelGrid getGridRechazarArqueo() {
		if (gridRechazarArqueo == null) {
			gridRechazarArqueo = (HtmlPanelGrid) findComponentInRoot("gridRechazarArqueo");
		}
		return gridRechazarArqueo;
	}

	protected HtmlGraphicImageEx getImageExRechazarArqueo() {
		if (imageExRechazarArqueo == null) {
			imageExRechazarArqueo = (HtmlGraphicImageEx) findComponentInRoot("imageExRechazarArqueo");
		}
		return imageExRechazarArqueo;
	}

	protected HtmlOutputText getLblMotivoRechazo() {
		if (lblMotivoRechazo == null) {
			lblMotivoRechazo = (HtmlOutputText) findComponentInRoot("lblMotivoRechazo");
		}
		return lblMotivoRechazo;
	}

	protected HtmlDialogWindow getRv_dwRechazarArqueoCaja() {
		if (rv_dwRechazarArqueoCaja == null) {
			rv_dwRechazarArqueoCaja = (HtmlDialogWindow) findComponentInRoot("rv_dwRechazarArqueoCaja");
		}
		return rv_dwRechazarArqueoCaja;
	}

	protected HtmlDialogWindowClientEvents getCleRechazarArqueo() {
		if (cleRechazarArqueo == null) {
			cleRechazarArqueo = (HtmlDialogWindowClientEvents) findComponentInRoot("cleRechazarArqueo");
		}
		return cleRechazarArqueo;
	}

	protected HtmlDialogWindowRoundedCorners getRcRechazarArqueo() {
		if (rcRechazarArqueo == null) {
			rcRechazarArqueo = (HtmlDialogWindowRoundedCorners) findComponentInRoot("rcRechazarArqueo");
		}
		return rcRechazarArqueo;
	}

	protected HtmlDialogWindowContentPane getCpRechazarArqueo() {
		if (cpRechazarArqueo == null) {
			cpRechazarArqueo = (HtmlDialogWindowContentPane) findComponentInRoot("cpRechazarArqueo");
		}
		return cpRechazarArqueo;
	}

	protected HtmlOutputText getLblRechazarArqueo() {
		if (lblRechazarArqueo == null) {
			lblRechazarArqueo = (HtmlOutputText) findComponentInRoot("lblRechazarArqueo");
		}
		return lblRechazarArqueo;
	}

	protected HtmlPanelGrid getGridRechazarArqueo1() {
		if (gridRechazarArqueo1 == null) {
			gridRechazarArqueo1 = (HtmlPanelGrid) findComponentInRoot("gridRechazarArqueo1");
		}
		return gridRechazarArqueo1;
	}

	protected HtmlInputTextarea getTxtMotivoRechazoArqueo() {
		if (txtMotivoRechazoArqueo == null) {
			txtMotivoRechazoArqueo = (HtmlInputTextarea) findComponentInRoot("txtMotivoRechazoArqueo");
		}
		return txtMotivoRechazoArqueo;
	}

	protected HtmlLink getLnkAprobArqueoSi() {
		if (lnkAprobArqueoSi == null) {
			lnkAprobArqueoSi = (HtmlLink) findComponentInRoot("lnkAprobArqueoSi");
		}
		return lnkAprobArqueoSi;
	}

	protected HtmlLink getLnkLiquidacionChk() {
		if (lnkLiquidacionChk == null) {
			lnkLiquidacionChk = (HtmlLink) findComponentInRoot("lnkLiquidacionChk");
		}
		return lnkLiquidacionChk;
	}

	protected HtmlOutputText getLblNoFactura2() {
		if (lblNoFactura2 == null) {
			lblNoFactura2 = (HtmlOutputText) findComponentInRoot("lblNoFactura2");
		}
		return lblNoFactura2;
	}

	protected HtmlColumn getCoNoFactura2() {
		if (coNoFactura2 == null) {
			coNoFactura2 = (HtmlColumn) findComponentInRoot("coNoFactura2");
		}
		return coNoFactura2;
	}

	protected HtmlOutputText getLblRcTmp_refer3() {
		if (lblRcTmp_refer3 == null) {
			lblRcTmp_refer3 = (HtmlOutputText) findComponentInRoot("lblRcTmp_refer3");
		}
		return lblRcTmp_refer3;
	}

	protected HtmlLink getLnkACDetfp_Tarsocketpos() {
		if (lnkACDetfp_Tarsocketpos == null) {
			lnkACDetfp_Tarsocketpos = (HtmlLink) findComponentInRoot("lnkACDetfp_Tarsocketpos");
		}
		return lnkACDetfp_Tarsocketpos;
	}

	protected HtmlOutputText getLblACDetfp_etTarCsocketpos() {
		if (lblACDetfp_etTarCsocketpos == null) {
			lblACDetfp_etTarCsocketpos = (HtmlOutputText) findComponentInRoot("lblACDetfp_etTarCsocketpos");
		}
		return lblACDetfp_etTarCsocketpos;
	}

	protected HtmlOutputText getLblACDetfp_TCsocketpos() {
		if (lblACDetfp_TCsocketpos == null) {
			lblACDetfp_TCsocketpos = (HtmlOutputText) findComponentInRoot("lblACDetfp_TCsocketpos");
		}
		return lblACDetfp_TCsocketpos;
	}

	protected HtmlLink getLnkAnularArqueoCaja() {
		if (lnkAnularArqueoCaja == null) {
			lnkAnularArqueoCaja = (HtmlLink) findComponentInRoot("lnkAnularArqueoCaja");
		}
		return lnkAnularArqueoCaja;
	}

	protected HtmlOutputText getLblMsgMotivoRechazo() {
		if (lblMsgMotivoRechazo == null) {
			lblMsgMotivoRechazo = (HtmlOutputText) findComponentInRoot("lblMsgMotivoRechazo");
		}
		return lblMsgMotivoRechazo;
	}

	protected HtmlLink getLnkRechazarArqueoSi() {
		if (lnkRechazarArqueoSi == null) {
			lnkRechazarArqueoSi = (HtmlLink) findComponentInRoot("lnkRechazarArqueoSi");
		}
		return lnkRechazarArqueoSi;
	}

	protected HtmlJspPanel getJspPanelRechazarArqueo() {
		if (jspPanelRechazarArqueo == null) {
			jspPanelRechazarArqueo = (HtmlJspPanel) findComponentInRoot("jspPanelRechazarArqueo");
		}
		return jspPanelRechazarArqueo;
	}

	protected HtmlLink getLnkARechazarArqueoNo() {
		if (lnkARechazarArqueoNo == null) {
			lnkARechazarArqueoNo = (HtmlLink) findComponentInRoot("lnkARechazarArqueoNo");
		}
		return lnkARechazarArqueoNo;
	}

	protected HtmlDialogWindowAutoPostBackFlags getApbRechazarArqueo() {
		if (apbRechazarArqueo == null) {
			apbRechazarArqueo = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbRechazarArqueo");
		}
		return apbRechazarArqueo;
	}

	protected HtmlDialogWindowHeader getHdDetrecpagmonEx() {
		if (hdDetrecpagmonEx == null) {
			hdDetrecpagmonEx = (HtmlDialogWindowHeader) findComponentInRoot("hdDetrecpagmonEx");
		}
		return hdDetrecpagmonEx;
	}

	protected HtmlGridView getGvDetRecpagMonEx() {
		if (gvDetRecpagMonEx == null) {
			gvDetRecpagMonEx = (HtmlGridView) findComponentInRoot("gvDetRecpagMonEx");
		}
		return gvDetRecpagMonEx;
	}

	protected HtmlOutputText getLblRcpagMex_numrec() {
		if (lblRcpagMex_numrec == null) {
			lblRcpagMex_numrec = (HtmlOutputText) findComponentInRoot("lblRcpagMex_numrec");
		}
		return lblRcpagMex_numrec;
	}

	protected HtmlOutputText getLblRcpagMex_tiporec() {
		if (lblRcpagMex_tiporec == null) {
			lblRcpagMex_tiporec = (HtmlOutputText) findComponentInRoot("lblRcpagMex_tiporec");
		}
		return lblRcpagMex_tiporec;
	}

	protected HtmlOutputText getLblRcpagMex_cliente() {
		if (lblRcpagMex_cliente == null) {
			lblRcpagMex_cliente = (HtmlOutputText) findComponentInRoot("lblRcpagMex_cliente");
		}
		return lblRcpagMex_cliente;
	}

	protected HtmlOutputText getLblRcpagMex_mpago() {
		if (lblRcpagMex_mpago == null) {
			lblRcpagMex_mpago = (HtmlOutputText) findComponentInRoot("lblRcpagMex_mpago");
		}
		return lblRcpagMex_mpago;
	}

	protected HtmlOutputText getLblRcpagMex_monto() {
		if (lblRcpagMex_monto == null) {
			lblRcpagMex_monto = (HtmlOutputText) findComponentInRoot("lblRcpagMex_monto");
		}
		return lblRcpagMex_monto;
	}

	protected HtmlOutputText getLblRcpagMex_equiv() {
		if (lblRcpagMex_equiv == null) {
			lblRcpagMex_equiv = (HtmlOutputText) findComponentInRoot("lblRcpagMex_equiv");
		}
		return lblRcpagMex_equiv;
	}

	protected HtmlOutputText getLblRcpagMex_tasa() {
		if (lblRcpagMex_tasa == null) {
			lblRcpagMex_tasa = (HtmlOutputText) findComponentInRoot("lblRcpagMex_tasa");
		}
		return lblRcpagMex_tasa;
	}

	protected HtmlOutputText getLblRcpagMex_Refer1() {
		if (lblRcpagMex_Refer1 == null) {
			lblRcpagMex_Refer1 = (HtmlOutputText) findComponentInRoot("lblRcpagMex_Refer1");
		}
		return lblRcpagMex_Refer1;
	}

	protected HtmlOutputText getLblRcpagMex_Refer2() {
		if (lblRcpagMex_Refer2 == null) {
			lblRcpagMex_Refer2 = (HtmlOutputText) findComponentInRoot("lblRcpagMex_Refer2");
		}
		return lblRcpagMex_Refer2;
	}

	protected HtmlOutputText getLblRcpagMex_refer3() {
		if (lblRcpagMex_refer3 == null) {
			lblRcpagMex_refer3 = (HtmlOutputText) findComponentInRoot("lblRcpagMex_refer3");
		}
		return lblRcpagMex_refer3;
	}

	protected HtmlOutputText getLblRcpagMex_refer4() {
		if (lblRcpagMex_refer4 == null) {
			lblRcpagMex_refer4 = (HtmlOutputText) findComponentInRoot("lblRcpagMex_refer4");
		}
		return lblRcpagMex_refer4;
	}

	protected HtmlLink getLnkCerrarDetRecPagMonEx() {
		if (lnkCerrarDetRecPagMonEx == null) {
			lnkCerrarDetRecPagMonEx = (HtmlLink) findComponentInRoot("lnkCerrarDetRecPagMonEx");
		}
		return lnkCerrarDetRecPagMonEx;
	}

	protected HtmlDialogWindow getDwDetallerecpagmonEx() {
		if (dwDetallerecpagmonEx == null) {
			dwDetallerecpagmonEx = (HtmlDialogWindow) findComponentInRoot("dwDetallerecpagmonEx");
		}
		return dwDetallerecpagmonEx;
	}

	protected HtmlDialogWindowClientEvents getClDetRecpagMonEx() {
		if (clDetRecpagMonEx == null) {
			clDetRecpagMonEx = (HtmlDialogWindowClientEvents) findComponentInRoot("clDetRecpagMonEx");
		}
		return clDetRecpagMonEx;
	}

	protected HtmlDialogWindowRoundedCorners getCrDetRecpagMonEx() {
		if (crDetRecpagMonEx == null) {
			crDetRecpagMonEx = (HtmlDialogWindowRoundedCorners) findComponentInRoot("crDetRecpagMonEx");
		}
		return crDetRecpagMonEx;
	}

	protected HtmlDialogWindowContentPane getCnpDetRecpagMonEx() {
		if (cnpDetRecpagMonEx == null) {
			cnpDetRecpagMonEx = (HtmlDialogWindowContentPane) findComponentInRoot("cnpDetRecpagMonEx");
		}
		return cnpDetRecpagMonEx;
	}

	protected HtmlDialogWindowHeader getHdEgrxmetp() {
		if (hdEgrxmetp == null) {
			hdEgrxmetp = (HtmlDialogWindowHeader) findComponentInRoot("hdEgrxmetp");
		}
		return hdEgrxmetp;
	}

	protected HtmlJspPanel getJspAPB3_1() {
		if (jspAPB3_1 == null) {
			jspAPB3_1 = (HtmlJspPanel) findComponentInRoot("jspAPB3_1");
		}
		return jspAPB3_1;
	}

	protected HtmlOutputText getLblDetalleMptc() {
		if (lblDetalleMptc == null) {
			lblDetalleMptc = (HtmlOutputText) findComponentInRoot("lblDetalleMptc");
		}
		return lblDetalleMptc;
	}

	protected HtmlOutputText getLblDetalleMpdp() {
		if (lblDetalleMpdp == null) {
			lblDetalleMpdp = (HtmlOutputText) findComponentInRoot("lblDetalleMpdp");
		}
		return lblDetalleMpdp;
	}

	protected HtmlOutputText getLblDetalleMptb() {
		if (lblDetalleMptb == null) {
			lblDetalleMptb = (HtmlOutputText) findComponentInRoot("lblDetalleMptb");
		}
		return lblDetalleMptb;
	}

	protected HtmlLink getLnkDetpagoTarjetaCr() {
		if (lnkDetpagoTarjetaCr == null) {
			lnkDetpagoTarjetaCr = (HtmlLink) findComponentInRoot("lnkDetpagoTarjetaCr");
		}
		return lnkDetpagoTarjetaCr;
	}

	protected HtmlOutputText getLblEtAbonoTCredito1() {
		if (lblEtAbonoTCredito1 == null) {
			lblEtAbonoTCredito1 = (HtmlOutputText) findComponentInRoot("lblEtAbonoTCredito1");
		}
		return lblEtAbonoTCredito1;
	}

	protected HtmlOutputText getLblPagoTarjetaCredito() {
		if (lblPagoTarjetaCredito == null) {
			lblPagoTarjetaCredito = (HtmlOutputText) findComponentInRoot("lblPagoTarjetaCredito");
		}
		return lblPagoTarjetaCredito;
	}

	protected HtmlLink getLnkDetpagoDepBanco() {
		if (lnkDetpagoDepBanco == null) {
			lnkDetpagoDepBanco = (HtmlLink) findComponentInRoot("lnkDetpagoDepBanco");
		}
		return lnkDetpagoDepBanco;
	}

	protected HtmlOutputText getLblEtAbonoDDbanco1() {
		if (lblEtAbonoDDbanco1 == null) {
			lblEtAbonoDDbanco1 = (HtmlOutputText) findComponentInRoot("lblEtAbonoDDbanco1");
		}
		return lblEtAbonoDDbanco1;
	}

	protected HtmlOutputText getLblPagoDepBanco() {
		if (lblPagoDepBanco == null) {
			lblPagoDepBanco = (HtmlOutputText) findComponentInRoot("lblPagoDepBanco");
		}
		return lblPagoDepBanco;
	}

	protected HtmlLink getLnkDetpagoTransBanco() {
		if (lnkDetpagoTransBanco == null) {
			lnkDetpagoTransBanco = (HtmlLink) findComponentInRoot("lnkDetpagoTransBanco");
		}
		return lnkDetpagoTransBanco;
	}

	protected HtmlOutputText getLblEtAbonoTransBanc1() {
		if (lblEtAbonoTransBanc1 == null) {
			lblEtAbonoTransBanc1 = (HtmlOutputText) findComponentInRoot("lblEtAbonoTransBanc1");
		}
		return lblEtAbonoTransBanc1;
	}

	protected HtmlOutputText getLblPagoTransBanco() {
		if (lblPagoTransBanco == null) {
			lblPagoTransBanco = (HtmlOutputText) findComponentInRoot("lblPagoTransBanco");
		}
		return lblPagoTransBanco;
	}

	protected HtmlLink getLnkCerrarVentana() {
		if (lnkCerrarVentana == null) {
			lnkCerrarVentana = (HtmlLink) findComponentInRoot("lnkCerrarVentana");
		}
		return lnkCerrarVentana;
	}

	protected HtmlDialogWindow getDwEgresosxMetPago() {
		if (dwEgresosxMetPago == null) {
			dwEgresosxMetPago = (HtmlDialogWindow) findComponentInRoot("dwEgresosxMetPago");
		}
		return dwEgresosxMetPago;
	}

	protected HtmlDialogWindowClientEvents getCeEgrxmetp() {
		if (ceEgrxmetp == null) {
			ceEgrxmetp = (HtmlDialogWindowClientEvents) findComponentInRoot("ceEgrxmetp");
		}
		return ceEgrxmetp;
	}

	protected HtmlDialogWindowRoundedCorners getCrEgrxmetp() {
		if (crEgrxmetp == null) {
			crEgrxmetp = (HtmlDialogWindowRoundedCorners) findComponentInRoot("crEgrxmetp");
		}
		return crEgrxmetp;
	}

	protected HtmlDialogWindowContentPane getCnpEgrxmetp() {
		if (cnpEgrxmetp == null) {
			cnpEgrxmetp = (HtmlDialogWindowContentPane) findComponentInRoot("cnpEgrxmetp");
		}
		return cnpEgrxmetp;
	}

	protected HtmlDialogWindowHeader getHdDetotrosE() {
		if (hdDetotrosE == null) {
			hdDetotrosE = (HtmlDialogWindowHeader) findComponentInRoot("hdDetotrosE");
		}
		return hdDetotrosE;
	}

	protected HtmlJspPanel getJspDetOtrosEgr() {
		if (jspDetOtrosEgr == null) {
			jspDetOtrosEgr = (HtmlJspPanel) findComponentInRoot("jspDetOtrosEgr");
		}
		return jspDetOtrosEgr;
	}

	protected HtmlOutputText getLblDetalleOtrosEgre() {
		if (lblDetalleOtrosEgre == null) {
			lblDetalleOtrosEgre = (HtmlOutputText) findComponentInRoot("lblDetalleOtrosEgre");
		}
		return lblDetalleOtrosEgre;
	}

	protected HtmlOutputText getLblDetOegTipo() {
		if (lblDetOegTipo == null) {
			lblDetOegTipo = (HtmlOutputText) findComponentInRoot("lblDetOegTipo");
		}
		return lblDetOegTipo;
	}

	protected HtmlOutputText getLblDetOeMonto() {
		if (lblDetOeMonto == null) {
			lblDetOeMonto = (HtmlOutputText) findComponentInRoot("lblDetOeMonto");
		}
		return lblDetOeMonto;
	}

	protected HtmlLink getLnkDeOtrosEcambios() {
		if (lnkDeOtrosEcambios == null) {
			lnkDeOtrosEcambios = (HtmlLink) findComponentInRoot("lnkDeOtrosEcambios");
		}
		return lnkDeOtrosEcambios;
	}

	protected HtmlOutputText getLblEtOegCambios() {
		if (lblEtOegCambios == null) {
			lblEtOegCambios = (HtmlOutputText) findComponentInRoot("lblEtOegCambios");
		}
		return lblEtOegCambios;
	}

	protected HtmlOutputText getLblOEcambios() {
		if (lblOEcambios == null) {
			lblOEcambios = (HtmlOutputText) findComponentInRoot("lblOEcambios");
		}
		return lblOEcambios;
	}

	protected HtmlLink getLnkDet() {
		if (lnkDet == null) {
			lnkDet = (HtmlLink) findComponentInRoot("lnkDet");
		}
		return lnkDet;
	}

	protected HtmlOutputText getLnkDetalleOepagosOtraMon() {
		if (lnkDetalleOepagosOtraMon == null) {
			lnkDetalleOepagosOtraMon = (HtmlOutputText) findComponentInRoot("lnkDetalleOepagosOtraMon");
		}
		return lnkDetalleOepagosOtraMon;
	}

	protected HtmlOutputText getLblTotalEgrRecxmonex() {
		if (lblTotalEgrRecxmonex == null) {
			lblTotalEgrRecxmonex = (HtmlOutputText) findComponentInRoot("lblTotalEgrRecxmonex");
		}
		return lblTotalEgrRecxmonex;
	}

	protected HtmlLink getLnkDetOtEgSalidas() {
		if (lnkDetOtEgSalidas == null) {
			lnkDetOtEgSalidas = (HtmlLink) findComponentInRoot("lnkDetOtEgSalidas");
		}
		return lnkDetOtEgSalidas;
	}

	protected HtmlOutputText getLnkDetalleOtEgSalidas() {
		if (lnkDetalleOtEgSalidas == null) {
			lnkDetalleOtEgSalidas = (HtmlOutputText) findComponentInRoot("lnkDetalleOtEgSalidas");
		}
		return lnkDetalleOtEgSalidas;
	}

	protected HtmlOutputText getLblOEsalidas() {
		if (lblOEsalidas == null) {
			lblOEsalidas = (HtmlOutputText) findComponentInRoot("lblOEsalidas");
		}
		return lblOEsalidas;
	}

	protected HtmlLink getLnkCerrarDetOtrosEgresos() {
		if (lnkCerrarDetOtrosEgresos == null) {
			lnkCerrarDetOtrosEgresos = (HtmlLink) findComponentInRoot("lnkCerrarDetOtrosEgresos");
		}
		return lnkCerrarDetOtrosEgresos;
	}

	protected HtmlDialogWindow getDwDetOtrosEgresos() {
		if (dwDetOtrosEgresos == null) {
			dwDetOtrosEgresos = (HtmlDialogWindow) findComponentInRoot("dwDetOtrosEgresos");
		}
		return dwDetOtrosEgresos;
	}

	protected HtmlDialogWindowClientEvents getCeDetotrosE() {
		if (ceDetotrosE == null) {
			ceDetotrosE = (HtmlDialogWindowClientEvents) findComponentInRoot("ceDetotrosE");
		}
		return ceDetotrosE;
	}

	protected HtmlDialogWindowRoundedCorners getCrDetotrosE() {
		if (crDetotrosE == null) {
			crDetotrosE = (HtmlDialogWindowRoundedCorners) findComponentInRoot("crDetotrosE");
		}
		return crDetotrosE;
	}

	protected HtmlDialogWindowContentPane getCnpDetotrosE() {
		if (cnpDetotrosE == null) {
			cnpDetotrosE = (HtmlDialogWindowContentPane) findComponentInRoot("cnpDetotrosE");
		}
		return cnpDetotrosE;
	}

	protected HtmlDialogWindowHeader getHdDetalleCam() {
		if (hdDetalleCam == null) {
			hdDetalleCam = (HtmlDialogWindowHeader) findComponentInRoot("hdDetalleCam");
		}
		return hdDetalleCam;
	}

	protected HtmlGridView getGvDetalleCambios() {
		if (gvDetalleCambios == null) {
			gvDetalleCambios = (HtmlGridView) findComponentInRoot("gvDetalleCambios");
		}
		return gvDetalleCambios;
	}

	protected HtmlLink getLnkDetalleRecibosxCambios() {
		if (lnkDetalleRecibosxCambios == null) {
			lnkDetalleRecibosxCambios = (HtmlLink) findComponentInRoot("lnkDetalleRecibosxCambios");
		}
		return lnkDetalleRecibosxCambios;
	}

	protected HtmlOutputText getLblDetDev_numrec() {
		if (lblDetDev_numrec == null) {
			lblDetDev_numrec = (HtmlOutputText) findComponentInRoot("lblDetDev_numrec");
		}
		return lblDetDev_numrec;
	}

	protected HtmlOutputText getLblDetDev_cliente() {
		if (lblDetDev_cliente == null) {
			lblDetDev_cliente = (HtmlOutputText) findComponentInRoot("lblDetDev_cliente");
		}
		return lblDetDev_cliente;
	}

	protected HtmlOutputText getLblDetDev_montoapl() {
		if (lblDetDev_montoapl == null) {
			lblDetDev_montoapl = (HtmlOutputText) findComponentInRoot("lblDetDev_montoapl");
		}
		return lblDetDev_montoapl;
	}

	protected HtmlOutputText getLblDetDev_montorec() {
		if (lblDetDev_montorec == null) {
			lblDetDev_montorec = (HtmlOutputText) findComponentInRoot("lblDetDev_montorec");
		}
		return lblDetDev_montorec;
	}

	protected HtmlOutputText getLblDetDev_cambio() {
		if (lblDetDev_cambio == null) {
			lblDetDev_cambio = (HtmlOutputText) findComponentInRoot("lblDetDev_cambio");
		}
		return lblDetDev_cambio;
	}

	protected HtmlOutputText getLblDetDev_hora() {
		if (lblDetDev_hora == null) {
			lblDetDev_hora = (HtmlOutputText) findComponentInRoot("lblDetDev_hora");
		}
		return lblDetDev_hora;
	}

	protected HtmlLink getLnkCerrarDetalleCambios() {
		if (lnkCerrarDetalleCambios == null) {
			lnkCerrarDetalleCambios = (HtmlLink) findComponentInRoot("lnkCerrarDetalleCambios");
		}
		return lnkCerrarDetalleCambios;
	}

	protected HtmlDialogWindow getDwDetalleCambios() {
		if (dwDetalleCambios == null) {
			dwDetalleCambios = (HtmlDialogWindow) findComponentInRoot("dwDetalleCambios");
		}
		return dwDetalleCambios;
	}

	protected HtmlDialogWindowClientEvents getClDetalleCam() {
		if (clDetalleCam == null) {
			clDetalleCam = (HtmlDialogWindowClientEvents) findComponentInRoot("clDetalleCam");
		}
		return clDetalleCam;
	}

	protected HtmlDialogWindowRoundedCorners getCrDetalleCam() {
		if (crDetalleCam == null) {
			crDetalleCam = (HtmlDialogWindowRoundedCorners) findComponentInRoot("crDetalleCam");
		}
		return crDetalleCam;
	}

	protected HtmlDialogWindowContentPane getCnpDetalleCam() {
		if (cnpDetalleCam == null) {
			cnpDetalleCam = (HtmlDialogWindowContentPane) findComponentInRoot("cnpDetalleCam");
		}
		return cnpDetalleCam;
	}

	protected HtmlDialogWindowHeader getHdDetOI() {
		if (hdDetOI == null) {
			hdDetOI = (HtmlDialogWindowHeader) findComponentInRoot("hdDetOI");
		}
		return hdDetOI;
	}

	protected HtmlJspPanel getJspOtrosIng_1() {
		if (jspOtrosIng_1 == null) {
			jspOtrosIng_1 = (HtmlJspPanel) findComponentInRoot("jspOtrosIng_1");
		}
		return jspOtrosIng_1;
	}

	protected HtmlOutputText getLblhdDetalle() {
		if (lblhdDetalle == null) {
			lblhdDetalle = (HtmlOutputText) findComponentInRoot("lblhdDetalle");
		}
		return lblhdDetalle;
	}

	protected HtmlOutputText getLblhdConcepto() {
		if (lblhdConcepto == null) {
			lblhdConcepto = (HtmlOutputText) findComponentInRoot("lblhdConcepto");
		}
		return lblhdConcepto;
	}

	protected HtmlOutputText getLblhdMonto() {
		if (lblhdMonto == null) {
			lblhdMonto = (HtmlOutputText) findComponentInRoot("lblhdMonto");
		}
		return lblhdMonto;
	}

	protected HtmlLink getLnkdetOiningext() {
		if (lnkdetOiningext == null) {
			lnkdetOiningext = (HtmlLink) findComponentInRoot("lnkdetOiningext");
		}
		return lnkdetOiningext;
	}

	protected HtmlOutputText getLblEtIngExtraOr() {
		if (lblEtIngExtraOr == null) {
			lblEtIngExtraOr = (HtmlOutputText) findComponentInRoot("lblEtIngExtraOr");
		}
		return lblEtIngExtraOr;
	}

	protected HtmlOutputText getLblIngresosExtraOrd() {
		if (lblIngresosExtraOrd == null) {
			lblIngresosExtraOrd = (HtmlOutputText) findComponentInRoot("lblIngresosExtraOrd");
		}
		return lblIngresosExtraOrd;
	}

	protected HtmlLink getLnkDetCamOtrMoneda() {
		if (lnkDetCamOtrMoneda == null) {
			lnkDetCamOtrMoneda = (HtmlLink) findComponentInRoot("lnkDetCamOtrMoneda");
		}
		return lnkDetCamOtrMoneda;
	}

	protected HtmlOutputText getLblEtCamOtrMoneda() {
		if (lblEtCamOtrMoneda == null) {
			lblEtCamOtrMoneda = (HtmlOutputText) findComponentInRoot("lblEtCamOtrMoneda");
		}
		return lblEtCamOtrMoneda;
	}

	protected HtmlLink getLnkCerrarDetOtrosIng() {
		if (lnkCerrarDetOtrosIng == null) {
			lnkCerrarDetOtrosIng = (HtmlLink) findComponentInRoot("lnkCerrarDetOtrosIng");
		}
		return lnkCerrarDetOtrosIng;
	}

	protected HtmlDialogWindow getDwDetalleOtrosIngresos() {
		if (dwDetalleOtrosIngresos == null) {
			dwDetalleOtrosIngresos = (HtmlDialogWindow) findComponentInRoot("dwDetalleOtrosIngresos");
		}
		return dwDetalleOtrosIngresos;
	}

	protected HtmlDialogWindowClientEvents getCeOtrosIng() {
		if (ceOtrosIng == null) {
			ceOtrosIng = (HtmlDialogWindowClientEvents) findComponentInRoot("ceOtrosIng");
		}
		return ceOtrosIng;
	}

	protected HtmlDialogWindowRoundedCorners getCrOtrosIng() {
		if (crOtrosIng == null) {
			crOtrosIng = (HtmlDialogWindowRoundedCorners) findComponentInRoot("crOtrosIng");
		}
		return crOtrosIng;
	}

	protected HtmlDialogWindowContentPane getCnpOtrosIng() {
		if (cnpOtrosIng == null) {
			cnpOtrosIng = (HtmlDialogWindowContentPane) findComponentInRoot("cnpOtrosIng");
		}
		return cnpOtrosIng;
	}

	protected HtmlDialogWindowHeader getHdDetSalida() {
		if (hdDetSalida == null) {
			hdDetSalida = (HtmlDialogWindowHeader) findComponentInRoot("hdDetSalida");
		}
		return hdDetSalida;
	}

	protected HtmlGridView getGvDetalleSalidas() {
		if (gvDetalleSalidas == null) {
			gvDetalleSalidas = (HtmlGridView) findComponentInRoot("gvDetalleSalidas");
		}
		return gvDetalleSalidas;
	}

	protected HtmlOutputText getLblds_numsal() {
		if (lblds_numsal == null) {
			lblds_numsal = (HtmlOutputText) findComponentInRoot("lblds_numsal");
		}
		return lblds_numsal;
	}

	protected HtmlOutputText getLblds_solicitante() {
		if (lblds_solicitante == null) {
			lblds_solicitante = (HtmlOutputText) findComponentInRoot("lblds_solicitante");
		}
		return lblds_solicitante;
	}

	protected HtmlOutputText getLblds_monto() {
		if (lblds_monto == null) {
			lblds_monto = (HtmlOutputText) findComponentInRoot("lblds_monto");
		}
		return lblds_monto;
	}

	protected HtmlOutputText getLblds_toperacion() {
		if (lblds_toperacion == null) {
			lblds_toperacion = (HtmlOutputText) findComponentInRoot("lblds_toperacion");
		}
		return lblds_toperacion;
	}

	protected HtmlOutputText getLblds_hora() {
		if (lblds_hora == null) {
			lblds_hora = (HtmlOutputText) findComponentInRoot("lblds_hora");
		}
		return lblds_hora;
	}

	protected HtmlOutputText getLblds_Refer1() {
		if (lblds_Refer1 == null) {
			lblds_Refer1 = (HtmlOutputText) findComponentInRoot("lblds_Refer1");
		}
		return lblds_Refer1;
	}

	protected HtmlOutputText getLblds_Refer2() {
		if (lblds_Refer2 == null) {
			lblds_Refer2 = (HtmlOutputText) findComponentInRoot("lblds_Refer2");
		}
		return lblds_Refer2;
	}

	protected HtmlOutputText getLblRcTmp_refer3der33() {
		if (lblRcTmp_refer3der33 == null) {
			lblRcTmp_refer3der33 = (HtmlOutputText) findComponentInRoot("lblRcTmp_refer3der33");
		}
		return lblRcTmp_refer3der33;
	}

	protected HtmlOutputText getLblds_refer4() {
		if (lblds_refer4 == null) {
			lblds_refer4 = (HtmlOutputText) findComponentInRoot("lblds_refer4");
		}
		return lblds_refer4;
	}

	protected HtmlLink getLnkCerrarDetalleSalidas() {
		if (lnkCerrarDetalleSalidas == null) {
			lnkCerrarDetalleSalidas = (HtmlLink) findComponentInRoot("lnkCerrarDetalleSalidas");
		}
		return lnkCerrarDetalleSalidas;
	}

	protected HtmlDialogWindow getDwDetalleSalidas() {
		if (dwDetalleSalidas == null) {
			dwDetalleSalidas = (HtmlDialogWindow) findComponentInRoot("dwDetalleSalidas");
		}
		return dwDetalleSalidas;
	}

	protected HtmlDialogWindowClientEvents getClDetSalida() {
		if (clDetSalida == null) {
			clDetSalida = (HtmlDialogWindowClientEvents) findComponentInRoot("clDetSalida");
		}
		return clDetSalida;
	}

	protected HtmlDialogWindowRoundedCorners getCrDetSalida() {
		if (crDetSalida == null) {
			crDetSalida = (HtmlDialogWindowRoundedCorners) findComponentInRoot("crDetSalida");
		}
		return crDetSalida;
	}

	protected HtmlDialogWindowContentPane getCnpDetSalida() {
		if (cnpDetSalida == null) {
			cnpDetSalida = (HtmlDialogWindowContentPane) findComponentInRoot("cnpDetSalida");
		}
		return cnpDetSalida;
	}

	protected HtmlDialogWindowHeader getHdCargarReferenciasPOS() {
		if (hdCargarReferenciasPOS == null) {
			hdCargarReferenciasPOS = (HtmlDialogWindowHeader) findComponentInRoot("hdCargarReferenciasPOS");
		}
		return hdCargarReferenciasPOS;
	}

	protected HtmlJspPanel getJspPanelGridPOS() {
		if (jspPanelGridPOS == null) {
			jspPanelGridPOS = (HtmlJspPanel) findComponentInRoot("jspPanelGridPOS");
		}
		return jspPanelGridPOS;
	}

	protected HtmlOutputText getLblMsgValidaReferencia() {
		if (lblMsgValidaReferencia == null) {
			lblMsgValidaReferencia = (HtmlOutputText) findComponentInRoot("lblMsgValidaReferencia");
		}
		return lblMsgValidaReferencia;
	}

	protected HtmlGridView getGvReferenciaPos() {
		if (gvReferenciaPos == null) {
			gvReferenciaPos = (HtmlGridView) findComponentInRoot("gvReferenciaPos");
		}
		return gvReferenciaPos;
	}

	protected HtmlColumn getCoReferpos() {
		if (coReferpos == null) {
			coReferpos = (HtmlColumn) findComponentInRoot("coReferpos");
		}
		return coReferpos;
	}

	protected HtmlInputText getLblreferpos() {
		if (lblreferpos == null) {
			lblreferpos = (HtmlInputText) findComponentInRoot("lblreferpos");
		}
		return lblreferpos;
	}

	protected HtmlOutputText getLblCoreferencia() {
		if (lblCoreferencia == null) {
			lblCoreferencia = (HtmlOutputText) findComponentInRoot("lblCoreferencia");
		}
		return lblCoreferencia;
	}

	protected HtmlOutputText getLblCodigoPos() {
		if (lblCodigoPos == null) {
			lblCodigoPos = (HtmlOutputText) findComponentInRoot("lblCodigoPos");
		}
		return lblCodigoPos;
	}

	protected HtmlOutputText getLblcodpos() {
		if (lblcodpos == null) {
			lblcodpos = (HtmlOutputText) findComponentInRoot("lblcodpos");
		}
		return lblcodpos;
	}

	protected HtmlOutputText getLblnombrePos() {
		if (lblnombrePos == null) {
			lblnombrePos = (HtmlOutputText) findComponentInRoot("lblnombrePos");
		}
		return lblnombrePos;
	}

	protected HtmlOutputText getLblnombrepos() {
		if (lblnombrepos == null) {
			lblnombrepos = (HtmlOutputText) findComponentInRoot("lblnombrepos");
		}
		return lblnombrepos;
	}

	protected HtmlOutputText getLblTotalPos() {
		if (lblTotalPos == null) {
			lblTotalPos = (HtmlOutputText) findComponentInRoot("lblTotalPos");
		}
		return lblTotalPos;
	}

	protected HtmlOutputText getLblTotalpos() {
		if (lblTotalpos == null) {
			lblTotalpos = (HtmlOutputText) findComponentInRoot("lblTotalpos");
		}
		return lblTotalpos;
	}

	protected HtmlOutputText getLblComisPos() {
		if (lblComisPos == null) {
			lblComisPos = (HtmlOutputText) findComponentInRoot("lblComisPos");
		}
		return lblComisPos;
	}

	protected HtmlOutputText getLblComispos() {
		if (lblComispos == null) {
			lblComispos = (HtmlOutputText) findComponentInRoot("lblComispos");
		}
		return lblComispos;
	}

	protected HtmlOutputText getLblMtoComisPos() {
		if (lblMtoComisPos == null) {
			lblMtoComisPos = (HtmlOutputText) findComponentInRoot("lblMtoComisPos");
		}
		return lblMtoComisPos;
	}

	protected HtmlOutputText getLblMtoComispos() {
		if (lblMtoComispos == null) {
			lblMtoComispos = (HtmlOutputText) findComponentInRoot("lblMtoComispos");
		}
		return lblMtoComispos;
	}

	protected HtmlOutputText getLblmontoRetencion() {
		if (lblmontoRetencion == null) {
			lblmontoRetencion = (HtmlOutputText) findComponentInRoot("lblmontoRetencion");
		}
		return lblmontoRetencion;
	}

	protected HtmlOutputText getLblmontoRetencion2() {
		if (lblmontoRetencion2 == null) {
			lblmontoRetencion2 = (HtmlOutputText) findComponentInRoot("lblmontoRetencion2");
		}
		return lblmontoRetencion2;
	}

	protected HtmlOutputText getLblMtoNetoPos() {
		if (lblMtoNetoPos == null) {
			lblMtoNetoPos = (HtmlOutputText) findComponentInRoot("lblMtoNetoPos");
		}
		return lblMtoNetoPos;
	}

	protected HtmlOutputText getLblMtoNetopos() {
		if (lblMtoNetopos == null) {
			lblMtoNetopos = (HtmlOutputText) findComponentInRoot("lblMtoNetopos");
		}
		return lblMtoNetopos;
	}

	protected HtmlOutputText getLblVmanual() {
		if (lblVmanual == null) {
			lblVmanual = (HtmlOutputText) findComponentInRoot("lblVmanual");
		}
		return lblVmanual;
	}

	protected HtmlOutputText getLblvmanualpos() {
		if (lblvmanualpos == null) {
			lblvmanualpos = (HtmlOutputText) findComponentInRoot("lblvmanualpos");
		}
		return lblvmanualpos;
	}

	protected HtmlCheckBox getChkPagoTransitorio() {
		if (chkPagoTransitorio == null) {
			chkPagoTransitorio = (HtmlCheckBox) findComponentInRoot("chkPagoTransitorio");
		}
		return chkPagoTransitorio;
	}

	protected HtmlOutputText getLblPagoTransitoriopos() {
		if (lblPagoTransitoriopos == null) {
			lblPagoTransitoriopos = (HtmlOutputText) findComponentInRoot("lblPagoTransitoriopos");
		}
		return lblPagoTransitoriopos;
	}

	protected HtmlLink getLnkReferPosSI() {
		if (lnkReferPosSI == null) {
			lnkReferPosSI = (HtmlLink) findComponentInRoot("lnkReferPosSI");
		}
		return lnkReferPosSI;
	}

	protected HtmlDialogWindow getDwCargarReferenciasPOS() {
		if (dwCargarReferenciasPOS == null) {
			dwCargarReferenciasPOS = (HtmlDialogWindow) findComponentInRoot("dwCargarReferenciasPOS");
		}
		return dwCargarReferenciasPOS;
	}

	protected HtmlDialogWindowClientEvents getCeReferenciasPos() {
		if (ceReferenciasPos == null) {
			ceReferenciasPos = (HtmlDialogWindowClientEvents) findComponentInRoot("ceReferenciasPos");
		}
		return ceReferenciasPos;
	}

	protected HtmlDialogWindowRoundedCorners getCrReferenciasPos() {
		if (crReferenciasPos == null) {
			crReferenciasPos = (HtmlDialogWindowRoundedCorners) findComponentInRoot("crReferenciasPos");
		}
		return crReferenciasPos;
	}

	protected HtmlDialogWindowContentPane getCnpReferenciasPos() {
		if (cnpReferenciasPos == null) {
			cnpReferenciasPos = (HtmlDialogWindowContentPane) findComponentInRoot("cnpReferenciasPos");
		}
		return cnpReferenciasPos;
	}

	protected HtmlColumn getCoCodPos() {
		if (coCodPos == null) {
			coCodPos = (HtmlColumn) findComponentInRoot("coCodPos");
		}
		return coCodPos;
	}

	protected HtmlColumn getCoNombre() {
		if (coNombre == null) {
			coNombre = (HtmlColumn) findComponentInRoot("coNombre");
		}
		return coNombre;
	}

	protected HtmlColumn getCoTotalpos() {
		if (coTotalpos == null) {
			coTotalpos = (HtmlColumn) findComponentInRoot("coTotalpos");
		}
		return coTotalpos;
	}

	protected HtmlColumn getCoComision() {
		if (coComision == null) {
			coComision = (HtmlColumn) findComponentInRoot("coComision");
		}
		return coComision;
	}

	protected HtmlColumn getCoMtoComis() {
		if (coMtoComis == null) {
			coMtoComis = (HtmlColumn) findComponentInRoot("coMtoComis");
		}
		return coMtoComis;
	}

	protected HtmlColumn getCoMontoRetencion() {
		if (coMontoRetencion == null) {
			coMontoRetencion = (HtmlColumn) findComponentInRoot("coMontoRetencion");
		}
		return coMontoRetencion;
	}

	protected HtmlColumn getCoMtoNeto() {
		if (coMtoNeto == null) {
			coMtoNeto = (HtmlColumn) findComponentInRoot("coMtoNeto");
		}
		return coMtoNeto;
	}

	protected HtmlColumn getCoVmanual() {
		if (coVmanual == null) {
			coVmanual = (HtmlColumn) findComponentInRoot("coVmanual");
		}
		return coVmanual;
	}

	protected HtmlColumn getCopagoTransitorio() {
		if (copagoTransitorio == null) {
			copagoTransitorio = (HtmlColumn) findComponentInRoot("copagoTransitorio");
		}
		return copagoTransitorio;
	}

	protected HtmlJspPanel getJspPanelOpcionReferPos() {
		if (jspPanelOpcionReferPos == null) {
			jspPanelOpcionReferPos = (HtmlJspPanel) findComponentInRoot("jspPanelOpcionReferPos");
		}
		return jspPanelOpcionReferPos;
	}

	protected HtmlLink getLnkReferPosNo() {
		if (lnkReferPosNo == null) {
			lnkReferPosNo = (HtmlLink) findComponentInRoot("lnkReferPosNo");
		}
		return lnkReferPosNo;
	}

	protected HtmlDialogWindowHeader getHdRePrintRpt() {
		if (hdRePrintRpt == null) {
			hdRePrintRpt = (HtmlDialogWindowHeader) findComponentInRoot("hdRePrintRpt");
		}
		return hdRePrintRpt;
	}

	protected HtmlPanelGrid getGridRePrintRpt() {
		if (gridRePrintRpt == null) {
			gridRePrintRpt = (HtmlPanelGrid) findComponentInRoot("gridRePrintRpt");
		}
		return gridRePrintRpt;
	}

	protected HtmlGraphicImageEx getImageExRePrintRpt() {
		if (imageExRePrintRpt == null) {
			imageExRePrintRpt = (HtmlGraphicImageEx) findComponentInRoot("imageExRePrintRpt");
		}
		return imageExRePrintRpt;
	}

	protected HtmlLink getLnkRePrintRptSi() {
		if (lnkRePrintRptSi == null) {
			lnkRePrintRptSi = (HtmlLink) findComponentInRoot("lnkRePrintRptSi");
		}
		return lnkRePrintRptSi;
	}

	protected HtmlDialogWindow getRv_dwConfirmarReimpresionRpt() {
		if (rv_dwConfirmarReimpresionRpt == null) {
			rv_dwConfirmarReimpresionRpt = (HtmlDialogWindow) findComponentInRoot("rv_dwConfirmarReimpresionRpt");
		}
		return rv_dwConfirmarReimpresionRpt;
	}

	protected HtmlDialogWindowClientEvents getCleRePrintRpt() {
		if (cleRePrintRpt == null) {
			cleRePrintRpt = (HtmlDialogWindowClientEvents) findComponentInRoot("cleRePrintRpt");
		}
		return cleRePrintRpt;
	}

	protected HtmlDialogWindowRoundedCorners getRcRePrintRpt() {
		if (rcRePrintRpt == null) {
			rcRePrintRpt = (HtmlDialogWindowRoundedCorners) findComponentInRoot("rcRePrintRpt");
		}
		return rcRePrintRpt;
	}

	protected HtmlDialogWindowContentPane getCpRePrintRpt() {
		if (cpRePrintRpt == null) {
			cpRePrintRpt = (HtmlDialogWindowContentPane) findComponentInRoot("cpRePrintRpt");
		}
		return cpRePrintRpt;
	}

	protected HtmlOutputText getLblRePrintRpt() {
		if (lblRePrintRpt == null) {
			lblRePrintRpt = (HtmlOutputText) findComponentInRoot("lblRePrintRpt");
		}
		return lblRePrintRpt;
	}

	protected HtmlJspPanel getJspPanelRePrintRpt() {
		if (jspPanelRePrintRpt == null) {
			jspPanelRePrintRpt = (HtmlJspPanel) findComponentInRoot("jspPanelRePrintRpt");
		}
		return jspPanelRePrintRpt;
	}

	protected HtmlLink getLnkRePrintRptNo() {
		if (lnkRePrintRptNo == null) {
			lnkRePrintRptNo = (HtmlLink) findComponentInRoot("lnkRePrintRptNo");
		}
		return lnkRePrintRptNo;
	}

	protected HtmlDialogWindowAutoPostBackFlags getApbRePrintRpt() {
		if (apbRePrintRpt == null) {
			apbRePrintRpt = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbRePrintRpt");
		}
		return apbRePrintRpt;
	}

	protected HtmlDialogWindowHeader getHdEditarRecibosIdPos() {
		if (hdEditarRecibosIdPos == null) {
			hdEditarRecibosIdPos = (HtmlDialogWindowHeader) findComponentInRoot("hdEditarRecibosIdPos");
		}
		return hdEditarRecibosIdPos;
	}

	protected HtmlJspPanel getJpEditarRecibosIdPos() {
		if (jpEditarRecibosIdPos == null) {
			jpEditarRecibosIdPos = (HtmlJspPanel) findComponentInRoot("jpEditarRecibosIdPos");
		}
		return jpEditarRecibosIdPos;
	}

	protected HtmlGridView getRv_gvEditarRecibosIdPos() {
		if (rv_gvEditarRecibosIdPos == null) {
			rv_gvEditarRecibosIdPos = (HtmlGridView) findComponentInRoot("rv_gvEditarRecibosIdPos");
		}
		return rv_gvEditarRecibosIdPos;
	}

	protected HtmlOutputText getLblEidpos_numrec() {
		if (lblEidpos_numrec == null) {
			lblEidpos_numrec = (HtmlOutputText) findComponentInRoot("lblEidpos_numrec");
		}
		return lblEidpos_numrec;
	}

	protected HtmlOutputText getLblEidpos_tiporec() {
		if (lblEidpos_tiporec == null) {
			lblEidpos_tiporec = (HtmlOutputText) findComponentInRoot("lblEidpos_tiporec");
		}
		return lblEidpos_tiporec;
	}

	protected HtmlPanelGroup getHpgrCantidad() {
		if (hpgrCantidad == null) {
			hpgrCantidad = (HtmlPanelGroup) findComponentInRoot("hpgrCantidad");
		}
		return hpgrCantidad;
	}

	protected HtmlGridAgFunction getGaf_funcion() {
		if (gaf_funcion == null) {
			gaf_funcion = (HtmlGridAgFunction) findComponentInRoot("gaf_funcion");
		}
		return gaf_funcion;
	}

	protected HtmlOutputText getLblEidpos_cliente() {
		if (lblEidpos_cliente == null) {
			lblEidpos_cliente = (HtmlOutputText) findComponentInRoot("lblEidpos_cliente");
		}
		return lblEidpos_cliente;
	}

	protected HtmlOutputText getLblEidpos_monto() {
		if (lblEidpos_monto == null) {
			lblEidpos_monto = (HtmlOutputText) findComponentInRoot("lblEidpos_monto");
		}
		return lblEidpos_monto;
	}

	protected HtmlOutputText getLblEidpos_montoapl() {
		if (lblEidpos_montoapl == null) {
			lblEidpos_montoapl = (HtmlOutputText) findComponentInRoot("lblEidpos_montoapl");
		}
		return lblEidpos_montoapl;
	}

	protected HtmlOutputText getLblEidpos_Refer1() {
		if (lblEidpos_Refer1 == null) {
			lblEidpos_Refer1 = (HtmlOutputText) findComponentInRoot("lblEidpos_Refer1");
		}
		return lblEidpos_Refer1;
	}

	protected HtmlOutputText getLblEidpos_Refer2() {
		if (lblEidpos_Refer2 == null) {
			lblEidpos_Refer2 = (HtmlOutputText) findComponentInRoot("lblEidpos_Refer2");
		}
		return lblEidpos_Refer2;
	}

	protected HtmlOutputText getLblEidpos_refer3() {
		if (lblEidpos_refer3 == null) {
			lblEidpos_refer3 = (HtmlOutputText) findComponentInRoot("lblEidpos_refer3");
		}
		return lblEidpos_refer3;
	}

	protected HtmlOutputText getLblMsgErrorCambioRefer() {
		if (lblMsgErrorCambioRefer == null) {
			lblMsgErrorCambioRefer = (HtmlOutputText) findComponentInRoot("lblMsgErrorCambioRefer");
		}
		return lblMsgErrorCambioRefer;
	}

	protected HtmlLink getLnkCerrarActualizarIdPOS() {
		if (lnkCerrarActualizarIdPOS == null) {
			lnkCerrarActualizarIdPOS = (HtmlLink) findComponentInRoot("lnkCerrarActualizarIdPOS");
		}
		return lnkCerrarActualizarIdPOS;
	}

	protected HtmlDialogWindow getRv_dwEditarRecibosIdPos() {
		if (rv_dwEditarRecibosIdPos == null) {
			rv_dwEditarRecibosIdPos = (HtmlDialogWindow) findComponentInRoot("rv_dwEditarRecibosIdPos");
		}
		return rv_dwEditarRecibosIdPos;
	}

	protected HtmlDialogWindowClientEvents getClEditarRecibosIdPos() {
		if (clEditarRecibosIdPos == null) {
			clEditarRecibosIdPos = (HtmlDialogWindowClientEvents) findComponentInRoot("clEditarRecibosIdPos");
		}
		return clEditarRecibosIdPos;
	}

	protected HtmlDialogWindowRoundedCorners getCrEditarRecibosIdPos() {
		if (crEditarRecibosIdPos == null) {
			crEditarRecibosIdPos = (HtmlDialogWindowRoundedCorners) findComponentInRoot("crEditarRecibosIdPos");
		}
		return crEditarRecibosIdPos;
	}

	protected HtmlDialogWindowContentPane getCnpEditarRecibosIdPos() {
		if (cnpEditarRecibosIdPos == null) {
			cnpEditarRecibosIdPos = (HtmlDialogWindowContentPane) findComponentInRoot("cnpEditarRecibosIdPos");
		}
		return cnpEditarRecibosIdPos;
	}

	protected HtmlColumn getCoEidposRefer1() {
		if (coEidposRefer1 == null) {
			coEidposRefer1 = (HtmlColumn) findComponentInRoot("coEidposRefer1");
		}
		return coEidposRefer1;
	}

	protected HtmlLink getLnkActualizarIdPOS() {
		if (lnkActualizarIdPOS == null) {
			lnkActualizarIdPOS = (HtmlLink) findComponentInRoot("lnkActualizarIdPOS");
		}
		return lnkActualizarIdPOS;
	}

	protected HtmlColumn getCoEidposRefer2() {
		if (coEidposRefer2 == null) {
			coEidposRefer2 = (HtmlColumn) findComponentInRoot("coEidposRefer2");
		}
		return coEidposRefer2;
	}

	protected HtmlColumn getCoEidposRefer3() {
		if (coEidposRefer3 == null) {
			coEidposRefer3 = (HtmlColumn) findComponentInRoot("coEidposRefer3");
		}
		return coEidposRefer3;
	}

	protected HtmlColumn getCoEidposRefer3hidden() {
		if (coEidposRefer3hidden == null) {
			coEidposRefer3hidden = (HtmlColumn) findComponentInRoot("coEidposRefer3hidden");
		}
		return coEidposRefer3hidden;
	}

	protected HtmlDialogWindowHeader getHdAsignarReferenciaChk() {
		if (hdAsignarReferenciaChk == null) {
			hdAsignarReferenciaChk = (HtmlDialogWindowHeader) findComponentInRoot("hdAsignarReferenciaChk");
		}
		return hdAsignarReferenciaChk;
	}

	protected HtmlJspPanel getIdGridAgregarReferChk() {
		if (idGridAgregarReferChk == null) {
			idGridAgregarReferChk = (HtmlJspPanel) findComponentInRoot("idGridAgregarReferChk");
		}
		return idGridAgregarReferChk;
	}

	protected HtmlGridView getRv_gvAsignarReferenciaCheque() {
		if (rv_gvAsignarReferenciaCheque == null) {
			rv_gvAsignarReferenciaCheque = (HtmlGridView) findComponentInRoot("rv_gvAsignarReferenciaCheque");
		}
		return rv_gvAsignarReferenciaCheque;
	}

	protected HtmlOutputText getLblAsiReferCkc_nombreBanco() {
		if (lblAsiReferCkc_nombreBanco == null) {
			lblAsiReferCkc_nombreBanco = (HtmlOutputText) findComponentInRoot("lblAsiReferCkc_nombreBanco");
		}
		return lblAsiReferCkc_nombreBanco;
	}

	protected HtmlPanelGrid getPgrFooter1() {
		if (pgrFooter1 == null) {
			pgrFooter1 = (HtmlPanelGrid) findComponentInRoot("pgrFooter1");
		}
		return pgrFooter1;
	}

	protected HtmlOutputText getLblAsiReferCkc_codigoBanco() {
		if (lblAsiReferCkc_codigoBanco == null) {
			lblAsiReferCkc_codigoBanco = (HtmlOutputText) findComponentInRoot("lblAsiReferCkc_codigoBanco");
		}
		return lblAsiReferCkc_codigoBanco;
	}

	protected HtmlOutputText getLblAsiReferCkc_cantidadCheque() {
		if (lblAsiReferCkc_cantidadCheque == null) {
			lblAsiReferCkc_cantidadCheque = (HtmlOutputText) findComponentInRoot("lblAsiReferCkc_cantidadCheque");
		}
		return lblAsiReferCkc_cantidadCheque;
	}

	protected HtmlOutputText getLblAsiReferCkc_bTotalBanco() {
		if (lblAsiReferCkc_bTotalBanco == null) {
			lblAsiReferCkc_bTotalBanco = (HtmlOutputText) findComponentInRoot("lblAsiReferCkc_bTotalBanco");
		}
		return lblAsiReferCkc_bTotalBanco;
	}

	protected HtmlOutputText getLblAsiReferCkc_nommoneda() {
		if (lblAsiReferCkc_nommoneda == null) {
			lblAsiReferCkc_nommoneda = (HtmlOutputText) findComponentInRoot("lblAsiReferCkc_nommoneda");
		}
		return lblAsiReferCkc_nommoneda;
	}

	protected HtmlInputText getLblreferchk() {
		if (lblreferchk == null) {
			lblreferchk = (HtmlInputText) findComponentInRoot("lblreferchk");
		}
		return lblreferchk;
	}

	protected HtmlOutputText getLblCoreferenciaChk() {
		if (lblCoreferenciaChk == null) {
			lblCoreferenciaChk = (HtmlOutputText) findComponentInRoot("lblCoreferenciaChk");
		}
		return lblCoreferenciaChk;
	}

	protected HtmlOutputText getLblMsgErrorAsignarReferChk() {
		if (lblMsgErrorAsignarReferChk == null) {
			lblMsgErrorAsignarReferChk = (HtmlOutputText) findComponentInRoot("lblMsgErrorAsignarReferChk");
		}
		return lblMsgErrorAsignarReferChk;
	}

	protected HtmlLink getLnkReferChkSI() {
		if (lnkReferChkSI == null) {
			lnkReferChkSI = (HtmlLink) findComponentInRoot("lnkReferChkSI");
		}
		return lnkReferChkSI;
	}

	protected HtmlLink getLnkReferChkNo() {
		if (lnkReferChkNo == null) {
			lnkReferChkNo = (HtmlLink) findComponentInRoot("lnkReferChkNo");
		}
		return lnkReferChkNo;
	}

	protected HtmlDialogWindow getRv_dwAsignarReferCheque() {
		if (rv_dwAsignarReferCheque == null) {
			rv_dwAsignarReferCheque = (HtmlDialogWindow) findComponentInRoot("rv_dwAsignarReferCheque");
		}
		return rv_dwAsignarReferCheque;
	}

	protected HtmlDialogWindowClientEvents getClAsignarReferenciaCheque() {
		if (clAsignarReferenciaCheque == null) {
			clAsignarReferenciaCheque = (HtmlDialogWindowClientEvents) findComponentInRoot("clAsignarReferenciaCheque");
		}
		return clAsignarReferenciaCheque;
	}

	protected HtmlDialogWindowRoundedCorners getCrAsignarReferenciaCheque() {
		if (crAsignarReferenciaCheque == null) {
			crAsignarReferenciaCheque = (HtmlDialogWindowRoundedCorners) findComponentInRoot("crAsignarReferenciaCheque");
		}
		return crAsignarReferenciaCheque;
	}

	protected HtmlDialogWindowContentPane getCnpAsignarReferenciaCheque() {
		if (cnpAsignarReferenciaCheque == null) {
			cnpAsignarReferenciaCheque = (HtmlDialogWindowContentPane) findComponentInRoot("cnpAsignarReferenciaCheque");
		}
		return cnpAsignarReferenciaCheque;
	}

	protected HtmlGridAgFunction getArchk_funcion() {
		if (archk_funcion == null) {
			archk_funcion = (HtmlGridAgFunction) findComponentInRoot("archk_funcion");
		}
		return archk_funcion;
	}

	protected HtmlGridAgFunction getArchk_funcion1() {
		if (archk_funcion1 == null) {
			archk_funcion1 = (HtmlGridAgFunction) findComponentInRoot("archk_funcion1");
		}
		return archk_funcion1;
	}

	protected HtmlColumn getCoReferChk() {
		if (coReferChk == null) {
			coReferChk = (HtmlColumn) findComponentInRoot("coReferChk");
		}
		return coReferChk;
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

	protected HtmlOutputText getLblMensajeCargando() {
		if (lblMensajeCargando == null) {
			lblMensajeCargando = (HtmlOutputText) findComponentInRoot("lblMensajeCargando");
		}
		return lblMensajeCargando;
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

	protected HtmlOutputText getLblMtoComisPosIva() {
		if (lblMtoComisPosIva == null) {
			lblMtoComisPosIva = (HtmlOutputText) findComponentInRoot("lblMtoComisPosIva");
		}
		return lblMtoComisPosIva;
	}

	protected HtmlOutputText getLblMtoComisposIva() {
		if (lblMtoComisposIva == null) {
			lblMtoComisposIva = (HtmlOutputText) findComponentInRoot("lblMtoComisposIva");
		}
		return lblMtoComisposIva;
	}

	protected HtmlColumn getCoMtoComisIva() {
		if (coMtoComisIva == null) {
			coMtoComisIva = (HtmlColumn) findComponentInRoot("coMtoComisIva");
		}
		return coMtoComisIva;
	}

	protected HtmlDialogWindowHeader getHdReciboMPago8N() {
		if (hdReciboMPago8N == null) {
			hdReciboMPago8N = (HtmlDialogWindowHeader) findComponentInRoot("hdReciboMPago8N");
		}
		return hdReciboMPago8N;
	}

	protected HtmlGridView getGvRecibosxMpago8N() {
		if (gvRecibosxMpago8N == null) {
			gvRecibosxMpago8N = (HtmlGridView) findComponentInRoot("gvRecibosxMpago8N");
		}
		return gvRecibosxMpago8N;
	}

	protected HtmlOutputText getLblMsgCambioReferMPago() {
		if (lblMsgCambioReferMPago == null) {
			lblMsgCambioReferMPago = (HtmlOutputText) findComponentInRoot("lblMsgCambioReferMPago");
		}
		return lblMsgCambioReferMPago;
	}

	protected HtmlLink getLnkCerrarReciboxMPago8N() {
		if (lnkCerrarReciboxMPago8N == null) {
			lnkCerrarReciboxMPago8N = (HtmlLink) findComponentInRoot("lnkCerrarReciboxMPago8N");
		}
		return lnkCerrarReciboxMPago8N;
	}

	protected HtmlDialogWindow getDwRecibosxMpago8N() {
		if (dwRecibosxMpago8N == null) {
			dwRecibosxMpago8N = (HtmlDialogWindow) findComponentInRoot("dwRecibosxMpago8N");
		}
		return dwRecibosxMpago8N;
	}

	protected HtmlDialogWindowContentPane getCnpRecibosxMpago8N() {
		if (cnpRecibosxMpago8N == null) {
			cnpRecibosxMpago8N = (HtmlDialogWindowContentPane) findComponentInRoot("cnpRecibosxMpago8N");
		}
		return cnpRecibosxMpago8N;
	}

	protected HtmlLink getLnkCambiarReferPago() {
		if (lnkCambiarReferPago == null) {
			lnkCambiarReferPago = (HtmlLink) findComponentInRoot("lnkCambiarReferPago");
		}
		return lnkCambiarReferPago;
	}

	protected HtmlColumn getCoReferOculta() {
		if (coReferOculta == null) {
			coReferOculta = (HtmlColumn) findComponentInRoot("coReferOculta");
		}
		return coReferOculta;
	}

	protected HtmlJspPanel getJspPmensaje() {
		if (jspPmensaje == null) {
			jspPmensaje = (HtmlJspPanel) findComponentInRoot("jspPmensaje");
		}
		return jspPmensaje;
	}

	protected HtmlOutputText getLblet_montoMinReint() {
		if (lblet_montoMinReint == null) {
			lblet_montoMinReint = (HtmlOutputText) findComponentInRoot("lblet_montoMinReint");
		}
		return lblet_montoMinReint;
	}

	protected HtmlOutputText getLblet_montoMinajust() {
		if (lblet_montoMinajust == null) {
			lblet_montoMinajust = (HtmlOutputText) findComponentInRoot("lblet_montoMinajust");
		}
		return lblet_montoMinajust;
	}

	protected HtmlOutputText getLblCDC_montoMinReint() {
		if (lblCDC_montoMinReint == null) {
			lblCDC_montoMinReint = (HtmlOutputText) findComponentInRoot("lblCDC_montoMinReint");
		}
		return lblCDC_montoMinReint;
	}

	protected HtmlOutputText getLblCDC_montoMinAjust() {
		if (lblCDC_montoMinAjust == null) {
			lblCDC_montoMinAjust = (HtmlOutputText) findComponentInRoot("lblCDC_montoMinAjust");
		}
		return lblCDC_montoMinAjust;
	}

}
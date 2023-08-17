/**
 * 
 */
package pagecode.cierre;

import pagecode.PageCodeBase;
import com.infragistics.faces.shared.component.html.HtmlLink;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGrid;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
import com.ibm.faces.component.html.HtmlPanelSection;
import com.ibm.faces.component.html.HtmlJspPanel;
import com.ibm.faces.component.html.HtmlGraphicImageEx;
import com.infragistics.faces.window.component.html.HtmlDialogWindow;
import com.infragistics.faces.window.component.html.HtmlDialogWindowHeader;
import com.infragistics.faces.window.component.html.HtmlDialogWindowClientEvents;
import com.infragistics.faces.window.component.html.HtmlDialogWindowRoundedCorners;
import com.infragistics.faces.window.component.html.HtmlDialogWindowContentPane;
import com.infragistics.faces.grid.component.html.HtmlGridView;
import com.infragistics.faces.grid.component.html.HtmlColumn;
import com.infragistics.faces.window.component.html.HtmlDialogWindowAutoPostBackFlags;
import com.infragistics.faces.input.component.html.HtmlCheckBox;
import com.infragistics.faces.input.component.html.HtmlDateChooser;
import com.infragistics.faces.input.component.html.HtmlDropDownListClientEvents;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlInputTextarea;
import com.ibm.faces.component.html.HtmlBehaviorKeyPress;

/**
 * @author Juan Ñamendi
 *
 */
public class ArqueoCaja extends PageCodeBase {

	protected HtmlOutputText lblFiltroCompania;
	protected HtmlDropDownList ddlFiltroCompania;
	protected HtmlOutputText lblFiltroMoneda;
	protected HtmlDropDownList ddlFiltroMoneda;
	protected HtmlOutputText lblEthoraArqueo;
	protected HtmlOutputText lblFechaHoraArqueo;
	protected HtmlOutputText lblMsgNoRegistros;
	protected HtmlOutputText lbletMsgArqueoPrevio;
	protected HtmlOutputText lblMsgArqueoPrevio;
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
	protected HtmlLink lnkDetalleFinancimiento;
	protected HtmlOutputText lblEtPagoFinan;
	protected HtmlOutputText lblTotalFinan;
	protected HtmlLink lnkDetallePrimasReservas;
	protected HtmlOutputText lblEtiqPrimas;
	protected HtmlOutputText lblTotalPrimas;
	protected HtmlLink lnkDetalleIngresosEx;
	protected HtmlOutputText lblEtIngresosEx;
	protected HtmlOutputText lblTotalOtrosIngresos;
	protected HtmlLink lnkDetIngxRecPagMonEx;
	protected HtmlOutputText lblEtIngRecxmonex;
	protected HtmlOutputText lblTotalIngRecxmonex;
	protected HtmlLink lnkIngCambiosOtMon;
	protected HtmlOutputText lblEtCambiosOtrMon;
	protected HtmlOutputText lblCambiosOtraMon;
	protected HtmlOutputText lblEtiTotaIngresos;
	protected HtmlOutputText lblTotaIngresos;
	protected HtmlOutputText lblTitEgresos;
	protected HtmlPanelSection psVtasPagBanco;
	protected HtmlJspPanel jspAbono_0;
	protected HtmlOutputText lblPsAbono1;
	protected HtmlJspPanel jspAbono_1;
	protected HtmlOutputText lblPsAbono0;
	protected HtmlJspPanel jspVtsPagBanco;
	protected HtmlLink lnkDetVtsTarjCred;
	protected HtmlOutputText lblEtVtsTCredito;
	protected HtmlOutputText lblVtsTCredito;
	protected HtmlLink lnkDetVtsDepBanco;
	protected HtmlOutputText lblEtVtsDDbanco;
	protected HtmlOutputText lblVtsDDbanco;
	protected HtmlLink lnkDetVtsTransBanc;
	protected HtmlOutputText lblEtVtsTransBanc;
	protected HtmlOutputText lblVtsTransBanc;
	protected HtmlOutputText lblTotalVtsPagBanco;
	protected HtmlPanelSection psAbonoPagBanco;
	protected HtmlJspPanel jspAPB0;
	protected HtmlOutputText lblPsapb;
	protected HtmlJspPanel jspAPB2;
	protected HtmlOutputText lblPsapb1;
	protected HtmlJspPanel jspAPB3;
	protected HtmlLink lnkDetAbonoTCred;
	protected HtmlOutputText lblEtAbonoTCredito;
	protected HtmlOutputText lblAbonoTCredito;
	protected HtmlLink lnkDetAbonoDepBanco;
	protected HtmlOutputText lblEtAbonoDDbanco;
	protected HtmlOutputText lblAbonoDDbanco;
	protected HtmlLink lnkDetAbonoTransBanc;
	protected HtmlOutputText lblEtAbonoTransBanc;
	protected HtmlOutputText lblAbonoTransBanc;
	protected HtmlOutputText lblTotalAbonoPagBanco;
	protected HtmlPanelSection psFinanPagBanco;
	protected HtmlJspPanel jspFNPB0;
	protected HtmlOutputText lblPsFNpb;
	protected HtmlJspPanel jspFNPB1;
	protected HtmlOutputText lblPsFNpb2;
	protected HtmlJspPanel jspFNPB2;
	protected HtmlLink lnkDetFinanTCred;
	protected HtmlOutputText lblEtFinanTCredito;
	protected HtmlOutputText lblFinanTCredito;
	protected HtmlLink lnkDetFinanDepBanco;
	protected HtmlOutputText lblEtFinanDDbanco;
	protected HtmlOutputText lblFinanDbanco;
	protected HtmlLink lnkDetFinanTransBanc;
	protected HtmlOutputText lblEtFinanTransBanc;
	protected HtmlOutputText lblFinanTransBanc;
	protected HtmlOutputText lblTotalFinanPagBanco;
	protected HtmlPanelSection psPrimasPagBanco;
	protected HtmlJspPanel jspPrimasPB0;
	protected HtmlOutputText lblPsprpb;
	protected HtmlJspPanel jspPrimasPB1;
	protected HtmlOutputText lblPsprpb1;
	protected HtmlJspPanel jspPrimasPB2;
	protected HtmlLink lnkDetPrimaTCred;
	protected HtmlOutputText lblEtPrimasTCredito;
	protected HtmlOutputText lblPrimasTCredito;
	protected HtmlLink lnkDetPrimasDepBanco;
	protected HtmlOutputText lblEtPrimasDDbanco;
	protected HtmlOutputText lblPrimasDDbanco;
	protected HtmlLink lnkDetPrimasTransBanc;
	protected HtmlOutputText lblEtPrimasTransBanc;
	protected HtmlOutputText lblPrimasTransBanc;
	protected HtmlOutputText lblTotalPrimasPagBanco;
	protected HtmlPanelSection psEgIngEx;
	protected HtmlJspPanel jspegIngEx0;
	protected HtmlGraphicImageEx gieImgPanelSeciex0;
	protected HtmlOutputText lblPsIngEx0;
	protected HtmlJspPanel jspegIngEx1;
	protected HtmlGraphicImageEx gieImgPanelSeciex1;
	protected HtmlOutputText lblPsIngEx1;
	protected HtmlJspPanel jsppEgIngEx;
	protected HtmlLink lnkegIngExtraH;
	protected HtmlOutputText lbletegIexH;
	protected HtmlOutputText lblegIexH;
	protected HtmlLink lnkegIngExtraN;
	protected HtmlOutputText lbletegiexDepBanco;
	protected HtmlOutputText lblegIexN;
	protected HtmlLink lnkegIngExtra8;
	protected HtmlOutputText lbletegiexTransBanco;
	protected HtmlOutputText lblegIex8;
	protected HtmlOutputText lblegTotalIex;
	protected HtmlPanelSection psOtrosEg;
	protected HtmlJspPanel jspOtrosEg0;
	protected HtmlOutputText lblpsOtrosEg0;
	protected HtmlJspPanel jspOtrosEg1;
	protected HtmlOutputText lblpsOtrosEg1;
	protected HtmlJspPanel jspOtrosEg2;
	protected HtmlLink lnkDetOECambios;
	protected HtmlOutputText lblEtOEcambios;
	protected HtmlOutputText lblOEcambios;
	protected HtmlLink lnkDetEgrexRecPagMonEx;
	protected HtmlOutputText lblEtOEmonEx;
	protected HtmlOutputText lblOEmonEx;
	protected HtmlLink lnkDetSalidas;
	protected HtmlOutputText lblEtOEsalidas;
	protected HtmlOutputText lblOEsalidas;
	protected HtmlOutputText lblTotalOtrosEgresos;
	protected HtmlOutputText lblEtTotalEgresos;
	protected HtmlOutputText lblTotalEgresos;
	protected HtmlLink lknProcesarArqueo;
	protected HtmlLink lknImprimirRptPrelim;
	protected HtmlDialogWindow dwFacturasRegistradas;
	protected HtmlDialogWindowHeader hdFactura;
	protected HtmlDialogWindowClientEvents clFacturasRegistradas;
	protected HtmlDialogWindowRoundedCorners crFacturas;
	protected HtmlDialogWindowContentPane cnpFacturas;
	protected HtmlGridView gvFacturaRegistradas;
	protected HtmlLink lnkDetalleFactura;
	protected HtmlOutputText lblNoFactura1;
	protected HtmlOutputText lblEtCantFacC0;
	protected HtmlOutputText lblCantFacCO;
	protected HtmlColumn coTipofactura2;
	protected HtmlOutputText lblTipofactura1;
	protected HtmlOutputText lblTipofactura2;
	protected HtmlOutputText lblEtCantFacCr;
	protected HtmlOutputText lblCantFacCr;
	protected HtmlColumn coTipoPagofact;
	protected HtmlOutputText lblTipoPagofact1;
	protected HtmlOutputText lblTipopagofact2;
	protected HtmlOutputText lblEtTotalFacCo;
	protected HtmlOutputText lblTotalFacCo;
	protected HtmlColumn coUnineg2;
	protected HtmlOutputText lblUnineg1;
	protected HtmlOutputText lblUnineg2;
	protected HtmlOutputText lblEtTotalFacCr;
	protected HtmlOutputText lblTotalFacCr;
	protected HtmlColumn coTotal;
	protected HtmlOutputText lblPartida22;
	protected HtmlOutputText lblPartida23;
	protected HtmlColumn coFecha2;
	protected HtmlOutputText lblFecha22;
	protected HtmlOutputText lblFecha23;
	protected HtmlPanelGrid hpgFacturasReg1;
	protected HtmlLink lnkCerrarFacturasReg;
	protected HtmlDialogWindow dgDetalleFactura;
	protected HtmlDialogWindowHeader hdDetalleFactura;
	protected HtmlDialogWindowClientEvents clDetalleFacturaCon;
	protected HtmlDialogWindowRoundedCorners crDetalleFacturaCon;
	protected HtmlDialogWindowContentPane cnpDetalleFacturaCon;
	protected HtmlJspPanel jspPanel4;
	protected HtmlOutputText text18;
	protected HtmlOutputText txtFechaFactura;
	protected HtmlOutputText text20;
	protected HtmlOutputText txtNoFactura;
	protected HtmlOutputText lblCodigo23;
	protected HtmlOutputText txtCodigoCliente;
	protected HtmlOutputText txtMonedaContado1;
	protected HtmlDropDownList ddlDetalleFacCon;
	protected HtmlOutputText txtNomCliente;
	protected HtmlOutputText lblTasaDetalleCont;
	protected HtmlOutputText text3333;
	protected HtmlOutputText lblUninegDetalleCont;
	protected HtmlOutputText txtCodUnineg;
	protected HtmlOutputText text23;
	protected HtmlGridView gvDfacturasDiario;
	protected HtmlColumn coCoditem;
	protected HtmlOutputText lblCoditem1;
	protected HtmlOutputText lblCoditem2;
	protected HtmlColumn coDescitemCont;
	protected HtmlOutputText lblDescitem1;
	protected HtmlOutputText lblDescitem2;
	protected HtmlColumn coCant;
	protected HtmlOutputText lblCantDetalle1;
	protected HtmlOutputText lblCantDetalle2;
	protected HtmlColumn coPreciounit;
	protected HtmlOutputText lblPrecionunitDetalle1;
	protected HtmlOutputText lblPrecionunitDetalle2;
	protected HtmlColumn coImpuesto;
	protected HtmlOutputText lblImpuestoDetalle1;
	protected HtmlOutputText lblImpuestoDetalle2;
	protected HtmlOutputText lblSubtotalDetalleContado;
	protected HtmlOutputText txtSubtotal;
	protected HtmlOutputText text28;
	protected HtmlOutputText txtIva;
	protected HtmlOutputText lblTotalDetCont;
	protected HtmlOutputText txtTotal;
	protected HtmlLink lnkCerrarDetalleContado;
	protected HtmlDialogWindowAutoPostBackFlags apbDetalleFactura;
	protected HtmlDialogWindow dwRecibosxTipoIngreso;
	protected HtmlDialogWindowHeader hdRecxTipoIng;
	protected HtmlDialogWindowClientEvents ceRecxTipoIng;
	protected HtmlDialogWindowRoundedCorners crRecxTipoIng;
	protected HtmlDialogWindowContentPane cnpRecxTipoIng;
	protected HtmlPanelGrid hpgRecxTipoIng;
	protected HtmlGridView gvRecibosIngresos;
	protected HtmlLink lnkDetalleRecibo;
	protected HtmlOutputText lblVNC_numrec;
	protected HtmlOutputText lblVNC_cliente0;
	protected HtmlOutputText lblVNC_cliente1;
	protected HtmlColumn coHora;
	protected HtmlOutputText lblVNC_hora0;
	protected HtmlOutputText lblVNC_hora1;
	protected HtmlOutputText lblVNC_monto0;
	protected HtmlOutputText lblVNC_monto1;
	protected HtmlPanelGrid hpgDetRecibosxTipoIng;
	protected HtmlLink lnkCerrarDetRecxTipoIng;
	protected HtmlDialogWindow dwDetalleRecibo;
	protected HtmlDialogWindowHeader hdDetalleRecibo;
	protected HtmlDialogWindowClientEvents clDetalleContado;
	protected HtmlDialogWindowRoundedCorners crDetalle;
	protected HtmlDialogWindowContentPane cnpDetalle;
	protected HtmlJspPanel jspPanel5;
	protected HtmlOutputText lblChkDiaAnterior;
	protected HtmlPanelGrid pgrHoraArqueo;
	protected HtmlOutputText lbletFechaArqueo;
	protected HtmlCheckBox chkArqueoDiaAnterior;
	protected HtmlDateChooser dcFechaArqueoAnterior;
	protected HtmlOutputText lbletHoraInicio;
	protected HtmlDropDownList ddlArqueoHini;
	protected HtmlOutputText lbletDosPuntosHoraInicio;
	protected HtmlDropDownList ddlArqueoMini;
	protected HtmlOutputText lbletHoraFinal;
	protected HtmlDropDownList ddlArqueoHFin;
	protected HtmlOutputText lbletDosPuntosMinFinal;
	protected HtmlDropDownList ddlArqueoMFin;
	protected HtmlDropDownListClientEvents ddlCeCargarTrans;
	protected HtmlOutputText lblTitCalculoDepCaja;
	protected HtmlOutputText lblet_efectNetoCaja;
	protected HtmlOutputText lblCDC_efectnetoRec;
	protected HtmlOutputText lblet_efectCaja;
	protected HtmlInputText txtCDC_efectivoenCaja;
	protected HtmlLink lnkCalDepCaja;
	protected HtmlOutputText lblet_pagocheques;
	protected HtmlOutputText lblCDC_pagocheques;
	protected HtmlOutputText lblet_SobranteFaltante;
	protected HtmlOutputText lblCDC_SobranteFaltante;
	protected HtmlOutputText lblet_montominimo;
	protected HtmlOutputText lblCDC_montominimo;
	protected HtmlOutputText lblet_depositoFinal;
	protected HtmlOutputText lblCDC_depositoFinal;
	protected HtmlOutputText lblet_montoMinReint;
	protected HtmlOutputText lblet_montoMinajust;
	protected HtmlOutputText lblet_DepositoSug;
	protected HtmlOutputText lblCDC_montoMinReint;
	protected HtmlOutputText lblCDC_montoMinAjust;
	protected HtmlOutputText lblCDC_depositoSug;
	protected HtmlOutputText lblet_ReferenciaDeposito;
	protected HtmlInputText txtCDC_ReferDeposito;
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
	protected HtmlLink lnkACDetfp_TarCredSocketpos;
	protected HtmlOutputText lblACDetfp_etTarCreditoSocketpos;
	protected HtmlOutputText lblACDetfp_TCSocketpos;
	protected HtmlLink lnkACDetfp_DepDbanco;
	protected HtmlOutputText lblACDetfp_etDepDbanco;
	protected HtmlOutputText lblACDetfp_DepDbanco;
	protected HtmlLink lnkACDetfp_TransBanco;
	protected HtmlOutputText lblACDetfp_etTransBanco;
	protected HtmlOutputText lblACDetfp_TransBanco;
	protected HtmlOutputText lblACDetfp_etTotal;
	protected HtmlOutputText lblACDetfp_Total;
	protected HtmlOutputText txtHoraRecibo;
	protected HtmlOutputText txtNoRecibo;
	protected HtmlOutputText lblCodCli;
	protected HtmlOutputText txtDRMonedaRecibo;
	protected HtmlOutputText txtDRCodCli;
	protected HtmlOutputText txtNoBatch;
	protected HtmlOutputText txtDRNomCliente;
	protected HtmlGridView gvDetalleRecibo;
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
	protected HtmlColumn coDRDescitemCont;
	protected HtmlColumn coDRCant;
	protected HtmlColumn coDRPreciounit;
	protected HtmlColumn coDRImpuesto;
	protected HtmlColumn coRefer1;
	protected HtmlColumn coRefer2;
	protected HtmlColumn coRefer3;
	protected HtmlColumn coRefer4;
	protected HtmlGridView gvFacturasRecibo;
	protected HtmlColumn coNoFactura2234;
	protected HtmlOutputText lblDRNoFactura1;
	protected HtmlOutputText lblNoFactura234;
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
	protected HtmlColumn coDRTipofactura2;
	protected HtmlColumn coDRUnineg2;
	protected HtmlColumn coDRMoneda2;
	protected HtmlColumn coDRFecha2;
	protected HtmlColumn coPartida2;
	protected HtmlOutputText lblConcepto;
	protected HtmlInputTextarea txtConcepto;
	protected HtmlOutputText lblDRSubtotalDetalleContado;
	protected HtmlOutputText txtDRSubtotalDetalle;
	protected HtmlOutputText txtDRIvaDetalle;
	protected HtmlOutputText lblDRTotalDetCont;
	protected HtmlOutputText txtDRTotalDetalle;
	protected HtmlLink lnkCerrarDetalleRecibo;
	protected HtmlDialogWindowAutoPostBackFlags apbDetalle;
	protected HtmlDialogWindowHeader hdDetalleCam;
	protected HtmlGridView gvDetalleDCambios;
	protected HtmlLink lnkDetalleRecibosxDevoluciones;
	protected HtmlOutputText lblDetDev_numrec;
	protected HtmlOutputText lblDetDev_cliente;
	protected HtmlOutputText lblDetDev_montoapl;
	protected HtmlOutputText lblDetDev_montorec;
	protected HtmlOutputText lblDetDev_cambio;
	protected HtmlOutputText lblDetDev_hora;
	protected HtmlLink lnkCerrarDetalleDevoluciones;
	protected HtmlDialogWindow dwDetalleCambios;
	protected HtmlDialogWindowClientEvents clDetalleCam;
	protected HtmlDialogWindowRoundedCorners crDetalleCam;
	protected HtmlDialogWindowContentPane cnpDetalleCam;
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
	protected HtmlOutputText lblRcTmp_refer3;
	protected HtmlOutputText lblRcTmp_refer4;
	protected HtmlLink lnkCerrarDetRecxtipoyMetpago;
	protected HtmlDialogWindow dwDetTipoReciboxMetodoPago;
	protected HtmlDialogWindowClientEvents clDetTrecxMetPago;
	protected HtmlDialogWindowRoundedCorners crDetTrecxMetPago;
	protected HtmlDialogWindowContentPane cnpDetTrecxMetPago;
	protected HtmlDialogWindowHeader hdVarqueo;
	protected HtmlJspPanel jspVarqueo0;
	protected HtmlGraphicImageEx imgVarqueo;
	protected HtmlOutputText lblValidarArqueo;
	protected HtmlLink lnkCerrarVarqueo;
	protected HtmlDialogWindow dwValidarArqueo;
	protected HtmlDialogWindowClientEvents ceVarqueo;
	protected HtmlDialogWindowRoundedCorners rcVarqueo;
	protected HtmlDialogWindowContentPane cpVarqueo;
	protected HtmlDialogWindowAutoPostBackFlags apbReciboContado2;
	protected HtmlDialogWindowHeader hdProcArqueo;
	protected HtmlPanelGrid grdProcArqueo;
	protected HtmlGraphicImageEx imgProcArqueo;
	protected HtmlOutputText lblavisoFaltanteArqueo;
	protected HtmlLink lnkProcArqueoOk;
	protected HtmlDialogWindow dwConfirmarProcesarArqueo;
	protected HtmlDialogWindowClientEvents cleProcArqueo;
	protected HtmlDialogWindowRoundedCorners rcProcArqueo;
	protected HtmlDialogWindowContentPane cpProcArqueo;
	protected HtmlOutputText lblConfirmProcArqueo;
	protected HtmlPanelGrid grdAvisoDebCajero;
	protected HtmlPanelGrid grdAOpProcesarAr;
	protected HtmlLink lnkProcArqueoCan;
	protected HtmlDialogWindowAutoPostBackFlags apbProcArqueo;
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
	protected HtmlDialogWindowHeader hdImpRtpAr;
	protected HtmlPanelGrid grdImpRtpAr;
	protected HtmlGraphicImageEx imgImpRtpAr;
	protected HtmlLink lnkImpRtpArOk;
	protected HtmlDialogWindow dwConfImprRptpre;
	protected HtmlDialogWindowClientEvents cleImpRtpAr;
	protected HtmlDialogWindowRoundedCorners rcImpRtpAr;
	protected HtmlDialogWindowContentPane cpImpRtpAr;
	protected HtmlOutputText lblConfirmImpRtpAr;
	protected HtmlJspPanel jspImpRtpAr;
	protected HtmlLink lnkImpRtpArCan;
	protected HtmlDialogWindowAutoPostBackFlags apbImpRtpAr;
	protected HtmlDialogWindowHeader hdProcAr;
	protected HtmlJspPanel jspProcAr0;
	protected HtmlGraphicImageEx imgProcAr;
	protected HtmlOutputText lblMsgProcArqueo;
	protected HtmlLink lnkCerrarProcArqueo;
	protected HtmlDialogWindow dwMsgProcesarArq;
	protected HtmlDialogWindowClientEvents ceProcAr;
	protected HtmlDialogWindowRoundedCorners rcProcAr;
	protected HtmlDialogWindowContentPane cpProcAr;
	protected HtmlDialogWindowHeader hdDetSalida;
	protected HtmlGridView gvDetalleSalidas;
	protected HtmlOutputText lblds_numsal;
	protected HtmlOutputText lblds_solicitante;
	protected HtmlOutputText lblds_monto;
	protected HtmlOutputText lblds_toperacion;
	protected HtmlOutputText lblds_hora;
	protected HtmlOutputText lblds_Refer1;
	protected HtmlOutputText lblds_Refer2;
	protected HtmlOutputText lblRcTmp_refer334rr;
	protected HtmlOutputText lblds_refer4;
	protected HtmlLink lnkCerrarDetalleSalidas;
	protected HtmlDialogWindow dwDetalleSalidas;
	protected HtmlDialogWindowClientEvents clDetSalida;
	protected HtmlDialogWindowRoundedCorners crDetSalida;
	protected HtmlDialogWindowContentPane cnpDetSalida;
	protected HtmlDialogWindowClientEvents cledwCargando;
	protected HtmlJspPanel jspdwCargando;
	protected HtmlOutputText lblMensajeCargando;
	protected HtmlGraphicImageEx imagenCargando;
	protected HtmlDialogWindow dwCargando;
	protected HtmlDialogWindowRoundedCorners cledwCargando22;
	protected HtmlDialogWindowContentPane cpdwCargando;
	protected HtmlDialogWindowAutoPostBackFlags apbdwCargando;
	protected HtmlBehaviorKeyPress behaviorKeyPress1;
	protected HtmlOutputText getLblFiltroCompania() {
		if (lblFiltroCompania == null) {
			lblFiltroCompania = (HtmlOutputText) findComponentInRoot("lblFiltroCompania");
		}
		return lblFiltroCompania;
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

	protected HtmlOutputText getLblEthoraArqueo() {
		if (lblEthoraArqueo == null) {
			lblEthoraArqueo = (HtmlOutputText) findComponentInRoot("lblEthoraArqueo");
		}
		return lblEthoraArqueo;
	}

	protected HtmlOutputText getLblFechaHoraArqueo() {
		if (lblFechaHoraArqueo == null) {
			lblFechaHoraArqueo = (HtmlOutputText) findComponentInRoot("lblFechaHoraArqueo");
		}
		return lblFechaHoraArqueo;
	}

	protected HtmlOutputText getLblMsgNoRegistros() {
		if (lblMsgNoRegistros == null) {
			lblMsgNoRegistros = (HtmlOutputText) findComponentInRoot("lblMsgNoRegistros");
		}
		return lblMsgNoRegistros;
	}

	protected HtmlOutputText getLbletMsgArqueoPrevio() {
		if (lbletMsgArqueoPrevio == null) {
			lbletMsgArqueoPrevio = (HtmlOutputText) findComponentInRoot("lbletMsgArqueoPrevio");
		}
		return lbletMsgArqueoPrevio;
	}

	protected HtmlOutputText getLblMsgArqueoPrevio() {
		if (lblMsgArqueoPrevio == null) {
			lblMsgArqueoPrevio = (HtmlOutputText) findComponentInRoot("lblMsgArqueoPrevio");
		}
		return lblMsgArqueoPrevio;
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

	protected HtmlLink getLnkDetalleFinancimiento() {
		if (lnkDetalleFinancimiento == null) {
			lnkDetalleFinancimiento = (HtmlLink) findComponentInRoot("lnkDetalleFinancimiento");
		}
		return lnkDetalleFinancimiento;
	}

	protected HtmlOutputText getLblEtPagoFinan() {
		if (lblEtPagoFinan == null) {
			lblEtPagoFinan = (HtmlOutputText) findComponentInRoot("lblEtPagoFinan");
		}
		return lblEtPagoFinan;
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

	protected HtmlOutputText getLblEtIngresosEx() {
		if (lblEtIngresosEx == null) {
			lblEtIngresosEx = (HtmlOutputText) findComponentInRoot("lblEtIngresosEx");
		}
		return lblEtIngresosEx;
	}

	protected HtmlOutputText getLblTotalOtrosIngresos() {
		if (lblTotalOtrosIngresos == null) {
			lblTotalOtrosIngresos = (HtmlOutputText) findComponentInRoot("lblTotalOtrosIngresos");
		}
		return lblTotalOtrosIngresos;
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

	protected HtmlLink getLnkIngCambiosOtMon() {
		if (lnkIngCambiosOtMon == null) {
			lnkIngCambiosOtMon = (HtmlLink) findComponentInRoot("lnkIngCambiosOtMon");
		}
		return lnkIngCambiosOtMon;
	}

	protected HtmlOutputText getLblEtCambiosOtrMon() {
		if (lblEtCambiosOtrMon == null) {
			lblEtCambiosOtrMon = (HtmlOutputText) findComponentInRoot("lblEtCambiosOtrMon");
		}
		return lblEtCambiosOtrMon;
	}

	protected HtmlOutputText getLblCambiosOtraMon() {
		if (lblCambiosOtraMon == null) {
			lblCambiosOtraMon = (HtmlOutputText) findComponentInRoot("lblCambiosOtraMon");
		}
		return lblCambiosOtraMon;
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

	protected HtmlPanelSection getPsVtasPagBanco() {
		if (psVtasPagBanco == null) {
			psVtasPagBanco = (HtmlPanelSection) findComponentInRoot("psVtasPagBanco");
		}
		return psVtasPagBanco;
	}

	protected HtmlJspPanel getJspAbono_0() {
		if (jspAbono_0 == null) {
			jspAbono_0 = (HtmlJspPanel) findComponentInRoot("jspAbono_0");
		}
		return jspAbono_0;
	}

	protected HtmlOutputText getLblPsAbono1() {
		if (lblPsAbono1 == null) {
			lblPsAbono1 = (HtmlOutputText) findComponentInRoot("lblPsAbono1");
		}
		return lblPsAbono1;
	}

	protected HtmlJspPanel getJspAbono_1() {
		if (jspAbono_1 == null) {
			jspAbono_1 = (HtmlJspPanel) findComponentInRoot("jspAbono_1");
		}
		return jspAbono_1;
	}

	protected HtmlOutputText getLblPsAbono0() {
		if (lblPsAbono0 == null) {
			lblPsAbono0 = (HtmlOutputText) findComponentInRoot("lblPsAbono0");
		}
		return lblPsAbono0;
	}

	protected HtmlJspPanel getJspVtsPagBanco() {
		if (jspVtsPagBanco == null) {
			jspVtsPagBanco = (HtmlJspPanel) findComponentInRoot("jspVtsPagBanco");
		}
		return jspVtsPagBanco;
	}

	protected HtmlLink getLnkDetVtsTarjCred() {
		if (lnkDetVtsTarjCred == null) {
			lnkDetVtsTarjCred = (HtmlLink) findComponentInRoot("lnkDetVtsTarjCred");
		}
		return lnkDetVtsTarjCred;
	}

	protected HtmlOutputText getLblEtVtsTCredito() {
		if (lblEtVtsTCredito == null) {
			lblEtVtsTCredito = (HtmlOutputText) findComponentInRoot("lblEtVtsTCredito");
		}
		return lblEtVtsTCredito;
	}

	protected HtmlOutputText getLblVtsTCredito() {
		if (lblVtsTCredito == null) {
			lblVtsTCredito = (HtmlOutputText) findComponentInRoot("lblVtsTCredito");
		}
		return lblVtsTCredito;
	}

	protected HtmlLink getLnkDetVtsDepBanco() {
		if (lnkDetVtsDepBanco == null) {
			lnkDetVtsDepBanco = (HtmlLink) findComponentInRoot("lnkDetVtsDepBanco");
		}
		return lnkDetVtsDepBanco;
	}

	protected HtmlOutputText getLblEtVtsDDbanco() {
		if (lblEtVtsDDbanco == null) {
			lblEtVtsDDbanco = (HtmlOutputText) findComponentInRoot("lblEtVtsDDbanco");
		}
		return lblEtVtsDDbanco;
	}

	protected HtmlOutputText getLblVtsDDbanco() {
		if (lblVtsDDbanco == null) {
			lblVtsDDbanco = (HtmlOutputText) findComponentInRoot("lblVtsDDbanco");
		}
		return lblVtsDDbanco;
	}

	protected HtmlLink getLnkDetVtsTransBanc() {
		if (lnkDetVtsTransBanc == null) {
			lnkDetVtsTransBanc = (HtmlLink) findComponentInRoot("lnkDetVtsTransBanc");
		}
		return lnkDetVtsTransBanc;
	}

	protected HtmlOutputText getLblEtVtsTransBanc() {
		if (lblEtVtsTransBanc == null) {
			lblEtVtsTransBanc = (HtmlOutputText) findComponentInRoot("lblEtVtsTransBanc");
		}
		return lblEtVtsTransBanc;
	}

	protected HtmlOutputText getLblVtsTransBanc() {
		if (lblVtsTransBanc == null) {
			lblVtsTransBanc = (HtmlOutputText) findComponentInRoot("lblVtsTransBanc");
		}
		return lblVtsTransBanc;
	}

	protected HtmlOutputText getLblTotalVtsPagBanco() {
		if (lblTotalVtsPagBanco == null) {
			lblTotalVtsPagBanco = (HtmlOutputText) findComponentInRoot("lblTotalVtsPagBanco");
		}
		return lblTotalVtsPagBanco;
	}

	protected HtmlPanelSection getPsAbonoPagBanco() {
		if (psAbonoPagBanco == null) {
			psAbonoPagBanco = (HtmlPanelSection) findComponentInRoot("psAbonoPagBanco");
		}
		return psAbonoPagBanco;
	}

	protected HtmlJspPanel getJspAPB0() {
		if (jspAPB0 == null) {
			jspAPB0 = (HtmlJspPanel) findComponentInRoot("jspAPB0");
		}
		return jspAPB0;
	}

	protected HtmlOutputText getLblPsapb() {
		if (lblPsapb == null) {
			lblPsapb = (HtmlOutputText) findComponentInRoot("lblPsapb");
		}
		return lblPsapb;
	}

	protected HtmlJspPanel getJspAPB2() {
		if (jspAPB2 == null) {
			jspAPB2 = (HtmlJspPanel) findComponentInRoot("jspAPB2");
		}
		return jspAPB2;
	}

	protected HtmlOutputText getLblPsapb1() {
		if (lblPsapb1 == null) {
			lblPsapb1 = (HtmlOutputText) findComponentInRoot("lblPsapb1");
		}
		return lblPsapb1;
	}

	protected HtmlJspPanel getJspAPB3() {
		if (jspAPB3 == null) {
			jspAPB3 = (HtmlJspPanel) findComponentInRoot("jspAPB3");
		}
		return jspAPB3;
	}

	protected HtmlLink getLnkDetAbonoTCred() {
		if (lnkDetAbonoTCred == null) {
			lnkDetAbonoTCred = (HtmlLink) findComponentInRoot("lnkDetAbonoTCred");
		}
		return lnkDetAbonoTCred;
	}

	protected HtmlOutputText getLblEtAbonoTCredito() {
		if (lblEtAbonoTCredito == null) {
			lblEtAbonoTCredito = (HtmlOutputText) findComponentInRoot("lblEtAbonoTCredito");
		}
		return lblEtAbonoTCredito;
	}

	protected HtmlOutputText getLblAbonoTCredito() {
		if (lblAbonoTCredito == null) {
			lblAbonoTCredito = (HtmlOutputText) findComponentInRoot("lblAbonoTCredito");
		}
		return lblAbonoTCredito;
	}

	protected HtmlLink getLnkDetAbonoDepBanco() {
		if (lnkDetAbonoDepBanco == null) {
			lnkDetAbonoDepBanco = (HtmlLink) findComponentInRoot("lnkDetAbonoDepBanco");
		}
		return lnkDetAbonoDepBanco;
	}

	protected HtmlOutputText getLblEtAbonoDDbanco() {
		if (lblEtAbonoDDbanco == null) {
			lblEtAbonoDDbanco = (HtmlOutputText) findComponentInRoot("lblEtAbonoDDbanco");
		}
		return lblEtAbonoDDbanco;
	}

	protected HtmlOutputText getLblAbonoDDbanco() {
		if (lblAbonoDDbanco == null) {
			lblAbonoDDbanco = (HtmlOutputText) findComponentInRoot("lblAbonoDDbanco");
		}
		return lblAbonoDDbanco;
	}

	protected HtmlLink getLnkDetAbonoTransBanc() {
		if (lnkDetAbonoTransBanc == null) {
			lnkDetAbonoTransBanc = (HtmlLink) findComponentInRoot("lnkDetAbonoTransBanc");
		}
		return lnkDetAbonoTransBanc;
	}

	protected HtmlOutputText getLblEtAbonoTransBanc() {
		if (lblEtAbonoTransBanc == null) {
			lblEtAbonoTransBanc = (HtmlOutputText) findComponentInRoot("lblEtAbonoTransBanc");
		}
		return lblEtAbonoTransBanc;
	}

	protected HtmlOutputText getLblAbonoTransBanc() {
		if (lblAbonoTransBanc == null) {
			lblAbonoTransBanc = (HtmlOutputText) findComponentInRoot("lblAbonoTransBanc");
		}
		return lblAbonoTransBanc;
	}

	protected HtmlOutputText getLblTotalAbonoPagBanco() {
		if (lblTotalAbonoPagBanco == null) {
			lblTotalAbonoPagBanco = (HtmlOutputText) findComponentInRoot("lblTotalAbonoPagBanco");
		}
		return lblTotalAbonoPagBanco;
	}

	protected HtmlPanelSection getPsFinanPagBanco() {
		if (psFinanPagBanco == null) {
			psFinanPagBanco = (HtmlPanelSection) findComponentInRoot("psFinanPagBanco");
		}
		return psFinanPagBanco;
	}

	protected HtmlJspPanel getJspFNPB0() {
		if (jspFNPB0 == null) {
			jspFNPB0 = (HtmlJspPanel) findComponentInRoot("jspFNPB0");
		}
		return jspFNPB0;
	}

	protected HtmlOutputText getLblPsFNpb() {
		if (lblPsFNpb == null) {
			lblPsFNpb = (HtmlOutputText) findComponentInRoot("lblPsFNpb");
		}
		return lblPsFNpb;
	}

	protected HtmlJspPanel getJspFNPB1() {
		if (jspFNPB1 == null) {
			jspFNPB1 = (HtmlJspPanel) findComponentInRoot("jspFNPB1");
		}
		return jspFNPB1;
	}

	protected HtmlOutputText getLblPsFNpb2() {
		if (lblPsFNpb2 == null) {
			lblPsFNpb2 = (HtmlOutputText) findComponentInRoot("lblPsFNpb2");
		}
		return lblPsFNpb2;
	}

	protected HtmlJspPanel getJspFNPB2() {
		if (jspFNPB2 == null) {
			jspFNPB2 = (HtmlJspPanel) findComponentInRoot("jspFNPB2");
		}
		return jspFNPB2;
	}

	protected HtmlLink getLnkDetFinanTCred() {
		if (lnkDetFinanTCred == null) {
			lnkDetFinanTCred = (HtmlLink) findComponentInRoot("lnkDetFinanTCred");
		}
		return lnkDetFinanTCred;
	}

	protected HtmlOutputText getLblEtFinanTCredito() {
		if (lblEtFinanTCredito == null) {
			lblEtFinanTCredito = (HtmlOutputText) findComponentInRoot("lblEtFinanTCredito");
		}
		return lblEtFinanTCredito;
	}

	protected HtmlOutputText getLblFinanTCredito() {
		if (lblFinanTCredito == null) {
			lblFinanTCredito = (HtmlOutputText) findComponentInRoot("lblFinanTCredito");
		}
		return lblFinanTCredito;
	}

	protected HtmlLink getLnkDetFinanDepBanco() {
		if (lnkDetFinanDepBanco == null) {
			lnkDetFinanDepBanco = (HtmlLink) findComponentInRoot("lnkDetFinanDepBanco");
		}
		return lnkDetFinanDepBanco;
	}

	protected HtmlOutputText getLblEtFinanDDbanco() {
		if (lblEtFinanDDbanco == null) {
			lblEtFinanDDbanco = (HtmlOutputText) findComponentInRoot("lblEtFinanDDbanco");
		}
		return lblEtFinanDDbanco;
	}

	protected HtmlOutputText getLblFinanDbanco() {
		if (lblFinanDbanco == null) {
			lblFinanDbanco = (HtmlOutputText) findComponentInRoot("lblFinanDbanco");
		}
		return lblFinanDbanco;
	}

	protected HtmlLink getLnkDetFinanTransBanc() {
		if (lnkDetFinanTransBanc == null) {
			lnkDetFinanTransBanc = (HtmlLink) findComponentInRoot("lnkDetFinanTransBanc");
		}
		return lnkDetFinanTransBanc;
	}

	protected HtmlOutputText getLblEtFinanTransBanc() {
		if (lblEtFinanTransBanc == null) {
			lblEtFinanTransBanc = (HtmlOutputText) findComponentInRoot("lblEtFinanTransBanc");
		}
		return lblEtFinanTransBanc;
	}

	protected HtmlOutputText getLblFinanTransBanc() {
		if (lblFinanTransBanc == null) {
			lblFinanTransBanc = (HtmlOutputText) findComponentInRoot("lblFinanTransBanc");
		}
		return lblFinanTransBanc;
	}

	protected HtmlOutputText getLblTotalFinanPagBanco() {
		if (lblTotalFinanPagBanco == null) {
			lblTotalFinanPagBanco = (HtmlOutputText) findComponentInRoot("lblTotalFinanPagBanco");
		}
		return lblTotalFinanPagBanco;
	}

	protected HtmlPanelSection getPsPrimasPagBanco() {
		if (psPrimasPagBanco == null) {
			psPrimasPagBanco = (HtmlPanelSection) findComponentInRoot("psPrimasPagBanco");
		}
		return psPrimasPagBanco;
	}

	protected HtmlJspPanel getJspPrimasPB0() {
		if (jspPrimasPB0 == null) {
			jspPrimasPB0 = (HtmlJspPanel) findComponentInRoot("jspPrimasPB0");
		}
		return jspPrimasPB0;
	}

	protected HtmlOutputText getLblPsprpb() {
		if (lblPsprpb == null) {
			lblPsprpb = (HtmlOutputText) findComponentInRoot("lblPsprpb");
		}
		return lblPsprpb;
	}

	protected HtmlJspPanel getJspPrimasPB1() {
		if (jspPrimasPB1 == null) {
			jspPrimasPB1 = (HtmlJspPanel) findComponentInRoot("jspPrimasPB1");
		}
		return jspPrimasPB1;
	}

	protected HtmlOutputText getLblPsprpb1() {
		if (lblPsprpb1 == null) {
			lblPsprpb1 = (HtmlOutputText) findComponentInRoot("lblPsprpb1");
		}
		return lblPsprpb1;
	}

	protected HtmlJspPanel getJspPrimasPB2() {
		if (jspPrimasPB2 == null) {
			jspPrimasPB2 = (HtmlJspPanel) findComponentInRoot("jspPrimasPB2");
		}
		return jspPrimasPB2;
	}

	protected HtmlLink getLnkDetPrimaTCred() {
		if (lnkDetPrimaTCred == null) {
			lnkDetPrimaTCred = (HtmlLink) findComponentInRoot("lnkDetPrimaTCred");
		}
		return lnkDetPrimaTCred;
	}

	protected HtmlOutputText getLblEtPrimasTCredito() {
		if (lblEtPrimasTCredito == null) {
			lblEtPrimasTCredito = (HtmlOutputText) findComponentInRoot("lblEtPrimasTCredito");
		}
		return lblEtPrimasTCredito;
	}

	protected HtmlOutputText getLblPrimasTCredito() {
		if (lblPrimasTCredito == null) {
			lblPrimasTCredito = (HtmlOutputText) findComponentInRoot("lblPrimasTCredito");
		}
		return lblPrimasTCredito;
	}

	protected HtmlLink getLnkDetPrimasDepBanco() {
		if (lnkDetPrimasDepBanco == null) {
			lnkDetPrimasDepBanco = (HtmlLink) findComponentInRoot("lnkDetPrimasDepBanco");
		}
		return lnkDetPrimasDepBanco;
	}

	protected HtmlOutputText getLblEtPrimasDDbanco() {
		if (lblEtPrimasDDbanco == null) {
			lblEtPrimasDDbanco = (HtmlOutputText) findComponentInRoot("lblEtPrimasDDbanco");
		}
		return lblEtPrimasDDbanco;
	}

	protected HtmlOutputText getLblPrimasDDbanco() {
		if (lblPrimasDDbanco == null) {
			lblPrimasDDbanco = (HtmlOutputText) findComponentInRoot("lblPrimasDDbanco");
		}
		return lblPrimasDDbanco;
	}

	protected HtmlLink getLnkDetPrimasTransBanc() {
		if (lnkDetPrimasTransBanc == null) {
			lnkDetPrimasTransBanc = (HtmlLink) findComponentInRoot("lnkDetPrimasTransBanc");
		}
		return lnkDetPrimasTransBanc;
	}

	protected HtmlOutputText getLblEtPrimasTransBanc() {
		if (lblEtPrimasTransBanc == null) {
			lblEtPrimasTransBanc = (HtmlOutputText) findComponentInRoot("lblEtPrimasTransBanc");
		}
		return lblEtPrimasTransBanc;
	}

	protected HtmlOutputText getLblPrimasTransBanc() {
		if (lblPrimasTransBanc == null) {
			lblPrimasTransBanc = (HtmlOutputText) findComponentInRoot("lblPrimasTransBanc");
		}
		return lblPrimasTransBanc;
	}

	protected HtmlOutputText getLblTotalPrimasPagBanco() {
		if (lblTotalPrimasPagBanco == null) {
			lblTotalPrimasPagBanco = (HtmlOutputText) findComponentInRoot("lblTotalPrimasPagBanco");
		}
		return lblTotalPrimasPagBanco;
	}

	protected HtmlPanelSection getPsEgIngEx() {
		if (psEgIngEx == null) {
			psEgIngEx = (HtmlPanelSection) findComponentInRoot("psEgIngEx");
		}
		return psEgIngEx;
	}

	protected HtmlJspPanel getJspegIngEx0() {
		if (jspegIngEx0 == null) {
			jspegIngEx0 = (HtmlJspPanel) findComponentInRoot("jspegIngEx0");
		}
		return jspegIngEx0;
	}

	protected HtmlGraphicImageEx getGieImgPanelSeciex0() {
		if (gieImgPanelSeciex0 == null) {
			gieImgPanelSeciex0 = (HtmlGraphicImageEx) findComponentInRoot("gieImgPanelSeciex0");
		}
		return gieImgPanelSeciex0;
	}

	protected HtmlOutputText getLblPsIngEx0() {
		if (lblPsIngEx0 == null) {
			lblPsIngEx0 = (HtmlOutputText) findComponentInRoot("lblPsIngEx0");
		}
		return lblPsIngEx0;
	}

	protected HtmlJspPanel getJspegIngEx1() {
		if (jspegIngEx1 == null) {
			jspegIngEx1 = (HtmlJspPanel) findComponentInRoot("jspegIngEx1");
		}
		return jspegIngEx1;
	}

	protected HtmlGraphicImageEx getGieImgPanelSeciex1() {
		if (gieImgPanelSeciex1 == null) {
			gieImgPanelSeciex1 = (HtmlGraphicImageEx) findComponentInRoot("gieImgPanelSeciex1");
		}
		return gieImgPanelSeciex1;
	}

	protected HtmlOutputText getLblPsIngEx1() {
		if (lblPsIngEx1 == null) {
			lblPsIngEx1 = (HtmlOutputText) findComponentInRoot("lblPsIngEx1");
		}
		return lblPsIngEx1;
	}

	protected HtmlJspPanel getJsppEgIngEx() {
		if (jsppEgIngEx == null) {
			jsppEgIngEx = (HtmlJspPanel) findComponentInRoot("jsppEgIngEx");
		}
		return jsppEgIngEx;
	}

	protected HtmlLink getLnkegIngExtraH() {
		if (lnkegIngExtraH == null) {
			lnkegIngExtraH = (HtmlLink) findComponentInRoot("lnkegIngExtraH");
		}
		return lnkegIngExtraH;
	}

	protected HtmlOutputText getLbletegIexH() {
		if (lbletegIexH == null) {
			lbletegIexH = (HtmlOutputText) findComponentInRoot("lbletegIexH");
		}
		return lbletegIexH;
	}

	protected HtmlOutputText getLblegIexH() {
		if (lblegIexH == null) {
			lblegIexH = (HtmlOutputText) findComponentInRoot("lblegIexH");
		}
		return lblegIexH;
	}

	protected HtmlLink getLnkegIngExtraN() {
		if (lnkegIngExtraN == null) {
			lnkegIngExtraN = (HtmlLink) findComponentInRoot("lnkegIngExtraN");
		}
		return lnkegIngExtraN;
	}

	protected HtmlOutputText getLbletegiexDepBanco() {
		if (lbletegiexDepBanco == null) {
			lbletegiexDepBanco = (HtmlOutputText) findComponentInRoot("lbletegiexDepBanco");
		}
		return lbletegiexDepBanco;
	}

	protected HtmlOutputText getLblegIexN() {
		if (lblegIexN == null) {
			lblegIexN = (HtmlOutputText) findComponentInRoot("lblegIexN");
		}
		return lblegIexN;
	}

	protected HtmlLink getLnkegIngExtra8() {
		if (lnkegIngExtra8 == null) {
			lnkegIngExtra8 = (HtmlLink) findComponentInRoot("lnkegIngExtra8");
		}
		return lnkegIngExtra8;
	}

	protected HtmlOutputText getLbletegiexTransBanco() {
		if (lbletegiexTransBanco == null) {
			lbletegiexTransBanco = (HtmlOutputText) findComponentInRoot("lbletegiexTransBanco");
		}
		return lbletegiexTransBanco;
	}

	protected HtmlOutputText getLblegIex8() {
		if (lblegIex8 == null) {
			lblegIex8 = (HtmlOutputText) findComponentInRoot("lblegIex8");
		}
		return lblegIex8;
	}

	protected HtmlOutputText getLblegTotalIex() {
		if (lblegTotalIex == null) {
			lblegTotalIex = (HtmlOutputText) findComponentInRoot("lblegTotalIex");
		}
		return lblegTotalIex;
	}

	protected HtmlPanelSection getPsOtrosEg() {
		if (psOtrosEg == null) {
			psOtrosEg = (HtmlPanelSection) findComponentInRoot("psOtrosEg");
		}
		return psOtrosEg;
	}

	protected HtmlJspPanel getJspOtrosEg0() {
		if (jspOtrosEg0 == null) {
			jspOtrosEg0 = (HtmlJspPanel) findComponentInRoot("jspOtrosEg0");
		}
		return jspOtrosEg0;
	}

	protected HtmlOutputText getLblpsOtrosEg0() {
		if (lblpsOtrosEg0 == null) {
			lblpsOtrosEg0 = (HtmlOutputText) findComponentInRoot("lblpsOtrosEg0");
		}
		return lblpsOtrosEg0;
	}

	protected HtmlJspPanel getJspOtrosEg1() {
		if (jspOtrosEg1 == null) {
			jspOtrosEg1 = (HtmlJspPanel) findComponentInRoot("jspOtrosEg1");
		}
		return jspOtrosEg1;
	}

	protected HtmlOutputText getLblpsOtrosEg1() {
		if (lblpsOtrosEg1 == null) {
			lblpsOtrosEg1 = (HtmlOutputText) findComponentInRoot("lblpsOtrosEg1");
		}
		return lblpsOtrosEg1;
	}

	protected HtmlJspPanel getJspOtrosEg2() {
		if (jspOtrosEg2 == null) {
			jspOtrosEg2 = (HtmlJspPanel) findComponentInRoot("jspOtrosEg2");
		}
		return jspOtrosEg2;
	}

	protected HtmlLink getLnkDetOECambios() {
		if (lnkDetOECambios == null) {
			lnkDetOECambios = (HtmlLink) findComponentInRoot("lnkDetOECambios");
		}
		return lnkDetOECambios;
	}

	protected HtmlOutputText getLblEtOEcambios() {
		if (lblEtOEcambios == null) {
			lblEtOEcambios = (HtmlOutputText) findComponentInRoot("lblEtOEcambios");
		}
		return lblEtOEcambios;
	}

	protected HtmlOutputText getLblOEcambios() {
		if (lblOEcambios == null) {
			lblOEcambios = (HtmlOutputText) findComponentInRoot("lblOEcambios");
		}
		return lblOEcambios;
	}

	protected HtmlLink getLnkDetEgrexRecPagMonEx() {
		if (lnkDetEgrexRecPagMonEx == null) {
			lnkDetEgrexRecPagMonEx = (HtmlLink) findComponentInRoot("lnkDetEgrexRecPagMonEx");
		}
		return lnkDetEgrexRecPagMonEx;
	}

	protected HtmlOutputText getLblEtOEmonEx() {
		if (lblEtOEmonEx == null) {
			lblEtOEmonEx = (HtmlOutputText) findComponentInRoot("lblEtOEmonEx");
		}
		return lblEtOEmonEx;
	}

	protected HtmlOutputText getLblOEmonEx() {
		if (lblOEmonEx == null) {
			lblOEmonEx = (HtmlOutputText) findComponentInRoot("lblOEmonEx");
		}
		return lblOEmonEx;
	}

	protected HtmlLink getLnkDetSalidas() {
		if (lnkDetSalidas == null) {
			lnkDetSalidas = (HtmlLink) findComponentInRoot("lnkDetSalidas");
		}
		return lnkDetSalidas;
	}

	protected HtmlOutputText getLblEtOEsalidas() {
		if (lblEtOEsalidas == null) {
			lblEtOEsalidas = (HtmlOutputText) findComponentInRoot("lblEtOEsalidas");
		}
		return lblEtOEsalidas;
	}

	protected HtmlOutputText getLblOEsalidas() {
		if (lblOEsalidas == null) {
			lblOEsalidas = (HtmlOutputText) findComponentInRoot("lblOEsalidas");
		}
		return lblOEsalidas;
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

	protected HtmlLink getLknProcesarArqueo() {
		if (lknProcesarArqueo == null) {
			lknProcesarArqueo = (HtmlLink) findComponentInRoot("lknProcesarArqueo");
		}
		return lknProcesarArqueo;
	}

	protected HtmlLink getLknImprimirRptPrelim() {
		if (lknImprimirRptPrelim == null) {
			lknImprimirRptPrelim = (HtmlLink) findComponentInRoot("lknImprimirRptPrelim");
		}
		return lknImprimirRptPrelim;
	}

	protected HtmlDialogWindow getDwFacturasRegistradas() {
		if (dwFacturasRegistradas == null) {
			dwFacturasRegistradas = (HtmlDialogWindow) findComponentInRoot("dwFacturasRegistradas");
		}
		return dwFacturasRegistradas;
	}

	protected HtmlDialogWindowHeader getHdFactura() {
		if (hdFactura == null) {
			hdFactura = (HtmlDialogWindowHeader) findComponentInRoot("hdFactura");
		}
		return hdFactura;
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

	protected HtmlGridView getGvFacturaRegistradas() {
		if (gvFacturaRegistradas == null) {
			gvFacturaRegistradas = (HtmlGridView) findComponentInRoot("gvFacturaRegistradas");
		}
		return gvFacturaRegistradas;
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

	protected HtmlOutputText getLblEtCantFacC0() {
		if (lblEtCantFacC0 == null) {
			lblEtCantFacC0 = (HtmlOutputText) findComponentInRoot("lblEtCantFacC0");
		}
		return lblEtCantFacC0;
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

	protected HtmlColumn getCoFecha2() {
		if (coFecha2 == null) {
			coFecha2 = (HtmlColumn) findComponentInRoot("coFecha2");
		}
		return coFecha2;
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

	protected HtmlPanelGrid getHpgFacturasReg1() {
		if (hpgFacturasReg1 == null) {
			hpgFacturasReg1 = (HtmlPanelGrid) findComponentInRoot("hpgFacturasReg1");
		}
		return hpgFacturasReg1;
	}

	protected HtmlLink getLnkCerrarFacturasReg() {
		if (lnkCerrarFacturasReg == null) {
			lnkCerrarFacturasReg = (HtmlLink) findComponentInRoot("lnkCerrarFacturasReg");
		}
		return lnkCerrarFacturasReg;
	}

	protected HtmlDialogWindow getDgDetalleFactura() {
		if (dgDetalleFactura == null) {
			dgDetalleFactura = (HtmlDialogWindow) findComponentInRoot("dgDetalleFactura");
		}
		return dgDetalleFactura;
	}

	protected HtmlDialogWindowHeader getHdDetalleFactura() {
		if (hdDetalleFactura == null) {
			hdDetalleFactura = (HtmlDialogWindowHeader) findComponentInRoot("hdDetalleFactura");
		}
		return hdDetalleFactura;
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

	protected HtmlOutputText getTxtFechaFactura() {
		if (txtFechaFactura == null) {
			txtFechaFactura = (HtmlOutputText) findComponentInRoot("txtFechaFactura");
		}
		return txtFechaFactura;
	}

	protected HtmlOutputText getText20() {
		if (text20 == null) {
			text20 = (HtmlOutputText) findComponentInRoot("text20");
		}
		return text20;
	}

	protected HtmlOutputText getTxtNoFactura() {
		if (txtNoFactura == null) {
			txtNoFactura = (HtmlOutputText) findComponentInRoot("txtNoFactura");
		}
		return txtNoFactura;
	}

	protected HtmlOutputText getLblCodigo23() {
		if (lblCodigo23 == null) {
			lblCodigo23 = (HtmlOutputText) findComponentInRoot("lblCodigo23");
		}
		return lblCodigo23;
	}

	protected HtmlOutputText getTxtCodigoCliente() {
		if (txtCodigoCliente == null) {
			txtCodigoCliente = (HtmlOutputText) findComponentInRoot("txtCodigoCliente");
		}
		return txtCodigoCliente;
	}

	protected HtmlOutputText getTxtMonedaContado1() {
		if (txtMonedaContado1 == null) {
			txtMonedaContado1 = (HtmlOutputText) findComponentInRoot("txtMonedaContado1");
		}
		return txtMonedaContado1;
	}

	protected HtmlDropDownList getDdlDetalleFacCon() {
		if (ddlDetalleFacCon == null) {
			ddlDetalleFacCon = (HtmlDropDownList) findComponentInRoot("ddlDetalleFacCon");
		}
		return ddlDetalleFacCon;
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

	protected HtmlOutputText getText3333() {
		if (text3333 == null) {
			text3333 = (HtmlOutputText) findComponentInRoot("text3333");
		}
		return text3333;
	}

	protected HtmlOutputText getLblUninegDetalleCont() {
		if (lblUninegDetalleCont == null) {
			lblUninegDetalleCont = (HtmlOutputText) findComponentInRoot("lblUninegDetalleCont");
		}
		return lblUninegDetalleCont;
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

	protected HtmlColumn getCoDescitemCont() {
		if (coDescitemCont == null) {
			coDescitemCont = (HtmlColumn) findComponentInRoot("coDescitemCont");
		}
		return coDescitemCont;
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

	protected HtmlColumn getCoCant() {
		if (coCant == null) {
			coCant = (HtmlColumn) findComponentInRoot("coCant");
		}
		return coCant;
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

	protected HtmlColumn getCoPreciounit() {
		if (coPreciounit == null) {
			coPreciounit = (HtmlColumn) findComponentInRoot("coPreciounit");
		}
		return coPreciounit;
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

	protected HtmlColumn getCoImpuesto() {
		if (coImpuesto == null) {
			coImpuesto = (HtmlColumn) findComponentInRoot("coImpuesto");
		}
		return coImpuesto;
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

	protected HtmlDialogWindowAutoPostBackFlags getApbDetalleFactura() {
		if (apbDetalleFactura == null) {
			apbDetalleFactura = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbDetalleFactura");
		}
		return apbDetalleFactura;
	}

	protected HtmlDialogWindow getDwRecibosxTipoIngreso() {
		if (dwRecibosxTipoIngreso == null) {
			dwRecibosxTipoIngreso = (HtmlDialogWindow) findComponentInRoot("dwRecibosxTipoIngreso");
		}
		return dwRecibosxTipoIngreso;
	}

	protected HtmlDialogWindowHeader getHdRecxTipoIng() {
		if (hdRecxTipoIng == null) {
			hdRecxTipoIng = (HtmlDialogWindowHeader) findComponentInRoot("hdRecxTipoIng");
		}
		return hdRecxTipoIng;
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

	protected HtmlPanelGrid getHpgRecxTipoIng() {
		if (hpgRecxTipoIng == null) {
			hpgRecxTipoIng = (HtmlPanelGrid) findComponentInRoot("hpgRecxTipoIng");
		}
		return hpgRecxTipoIng;
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

	protected HtmlColumn getCoHora() {
		if (coHora == null) {
			coHora = (HtmlColumn) findComponentInRoot("coHora");
		}
		return coHora;
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

	protected HtmlPanelGrid getHpgDetRecibosxTipoIng() {
		if (hpgDetRecibosxTipoIng == null) {
			hpgDetRecibosxTipoIng = (HtmlPanelGrid) findComponentInRoot("hpgDetRecibosxTipoIng");
		}
		return hpgDetRecibosxTipoIng;
	}

	protected HtmlLink getLnkCerrarDetRecxTipoIng() {
		if (lnkCerrarDetRecxTipoIng == null) {
			lnkCerrarDetRecxTipoIng = (HtmlLink) findComponentInRoot("lnkCerrarDetRecxTipoIng");
		}
		return lnkCerrarDetRecxTipoIng;
	}

	protected HtmlDialogWindow getDwDetalleRecibo() {
		if (dwDetalleRecibo == null) {
			dwDetalleRecibo = (HtmlDialogWindow) findComponentInRoot("dwDetalleRecibo");
		}
		return dwDetalleRecibo;
	}

	protected HtmlDialogWindowHeader getHdDetalleRecibo() {
		if (hdDetalleRecibo == null) {
			hdDetalleRecibo = (HtmlDialogWindowHeader) findComponentInRoot("hdDetalleRecibo");
		}
		return hdDetalleRecibo;
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

	protected HtmlJspPanel getJspPanel5() {
		if (jspPanel5 == null) {
			jspPanel5 = (HtmlJspPanel) findComponentInRoot("jspPanel5");
		}
		return jspPanel5;
	}

	protected HtmlOutputText getLblChkDiaAnterior() {
		if (lblChkDiaAnterior == null) {
			lblChkDiaAnterior = (HtmlOutputText) findComponentInRoot("lblChkDiaAnterior");
		}
		return lblChkDiaAnterior;
	}

	protected HtmlPanelGrid getPgrHoraArqueo() {
		if (pgrHoraArqueo == null) {
			pgrHoraArqueo = (HtmlPanelGrid) findComponentInRoot("pgrHoraArqueo");
		}
		return pgrHoraArqueo;
	}

	protected HtmlOutputText getLbletFechaArqueo() {
		if (lbletFechaArqueo == null) {
			lbletFechaArqueo = (HtmlOutputText) findComponentInRoot("lbletFechaArqueo");
		}
		return lbletFechaArqueo;
	}

	protected HtmlCheckBox getChkArqueoDiaAnterior() {
		if (chkArqueoDiaAnterior == null) {
			chkArqueoDiaAnterior = (HtmlCheckBox) findComponentInRoot("chkArqueoDiaAnterior");
		}
		return chkArqueoDiaAnterior;
	}

	protected HtmlDateChooser getDcFechaArqueoAnterior() {
		if (dcFechaArqueoAnterior == null) {
			dcFechaArqueoAnterior = (HtmlDateChooser) findComponentInRoot("dcFechaArqueoAnterior");
		}
		return dcFechaArqueoAnterior;
	}

	protected HtmlOutputText getLbletHoraInicio() {
		if (lbletHoraInicio == null) {
			lbletHoraInicio = (HtmlOutputText) findComponentInRoot("lbletHoraInicio");
		}
		return lbletHoraInicio;
	}

	protected HtmlDropDownList getDdlArqueoHini() {
		if (ddlArqueoHini == null) {
			ddlArqueoHini = (HtmlDropDownList) findComponentInRoot("ddlArqueoHini");
		}
		return ddlArqueoHini;
	}

	protected HtmlOutputText getLbletDosPuntosHoraInicio() {
		if (lbletDosPuntosHoraInicio == null) {
			lbletDosPuntosHoraInicio = (HtmlOutputText) findComponentInRoot("lbletDosPuntosHoraInicio");
		}
		return lbletDosPuntosHoraInicio;
	}

	protected HtmlDropDownList getDdlArqueoMini() {
		if (ddlArqueoMini == null) {
			ddlArqueoMini = (HtmlDropDownList) findComponentInRoot("ddlArqueoMini");
		}
		return ddlArqueoMini;
	}

	protected HtmlOutputText getLbletHoraFinal() {
		if (lbletHoraFinal == null) {
			lbletHoraFinal = (HtmlOutputText) findComponentInRoot("lbletHoraFinal");
		}
		return lbletHoraFinal;
	}

	protected HtmlDropDownList getDdlArqueoHFin() {
		if (ddlArqueoHFin == null) {
			ddlArqueoHFin = (HtmlDropDownList) findComponentInRoot("ddlArqueoHFin");
		}
		return ddlArqueoHFin;
	}

	protected HtmlOutputText getLbletDosPuntosMinFinal() {
		if (lbletDosPuntosMinFinal == null) {
			lbletDosPuntosMinFinal = (HtmlOutputText) findComponentInRoot("lbletDosPuntosMinFinal");
		}
		return lbletDosPuntosMinFinal;
	}

	protected HtmlDropDownList getDdlArqueoMFin() {
		if (ddlArqueoMFin == null) {
			ddlArqueoMFin = (HtmlDropDownList) findComponentInRoot("ddlArqueoMFin");
		}
		return ddlArqueoMFin;
	}

	protected HtmlDropDownListClientEvents getDdlCeCargarTrans() {
		if (ddlCeCargarTrans == null) {
			ddlCeCargarTrans = (HtmlDropDownListClientEvents) findComponentInRoot("ddlCeCargarTrans");
		}
		return ddlCeCargarTrans;
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

	protected HtmlInputText getTxtCDC_efectivoenCaja() {
		if (txtCDC_efectivoenCaja == null) {
			txtCDC_efectivoenCaja = (HtmlInputText) findComponentInRoot("txtCDC_efectivoenCaja");
		}
		return txtCDC_efectivoenCaja;
	}

	protected HtmlLink getLnkCalDepCaja() {
		if (lnkCalDepCaja == null) {
			lnkCalDepCaja = (HtmlLink) findComponentInRoot("lnkCalDepCaja");
		}
		return lnkCalDepCaja;
	}

	protected HtmlOutputText getLblet_pagocheques() {
		if (lblet_pagocheques == null) {
			lblet_pagocheques = (HtmlOutputText) findComponentInRoot("lblet_pagocheques");
		}
		return lblet_pagocheques;
	}

	protected HtmlOutputText getLblCDC_pagocheques() {
		if (lblCDC_pagocheques == null) {
			lblCDC_pagocheques = (HtmlOutputText) findComponentInRoot("lblCDC_pagocheques");
		}
		return lblCDC_pagocheques;
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

	protected HtmlOutputText getLblet_DepositoSug() {
		if (lblet_DepositoSug == null) {
			lblet_DepositoSug = (HtmlOutputText) findComponentInRoot("lblet_DepositoSug");
		}
		return lblet_DepositoSug;
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

	protected HtmlLink getLnkACDetfp_TarCredSocketpos() {
		if (lnkACDetfp_TarCredSocketpos == null) {
			lnkACDetfp_TarCredSocketpos = (HtmlLink) findComponentInRoot("lnkACDetfp_TarCredSocketpos");
		}
		return lnkACDetfp_TarCredSocketpos;
	}

	protected HtmlOutputText getLblACDetfp_etTarCreditoSocketpos() {
		if (lblACDetfp_etTarCreditoSocketpos == null) {
			lblACDetfp_etTarCreditoSocketpos = (HtmlOutputText) findComponentInRoot("lblACDetfp_etTarCreditoSocketpos");
		}
		return lblACDetfp_etTarCreditoSocketpos;
	}

	protected HtmlOutputText getLblACDetfp_TCSocketpos() {
		if (lblACDetfp_TCSocketpos == null) {
			lblACDetfp_TCSocketpos = (HtmlOutputText) findComponentInRoot("lblACDetfp_TCSocketpos");
		}
		return lblACDetfp_TCSocketpos;
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

	protected HtmlOutputText getTxtDRNomCliente() {
		if (txtDRNomCliente == null) {
			txtDRNomCliente = (HtmlOutputText) findComponentInRoot("txtDRNomCliente");
		}
		return txtDRNomCliente;
	}

	protected HtmlGridView getGvDetalleRecibo() {
		if (gvDetalleRecibo == null) {
			gvDetalleRecibo = (HtmlGridView) findComponentInRoot("gvDetalleRecibo");
		}
		return gvDetalleRecibo;
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

	protected HtmlGridView getGvFacturasRecibo() {
		if (gvFacturasRecibo == null) {
			gvFacturasRecibo = (HtmlGridView) findComponentInRoot("gvFacturasRecibo");
		}
		return gvFacturasRecibo;
	}

	protected HtmlColumn getCoNoFactura2234() {
		if (coNoFactura2234 == null) {
			coNoFactura2234 = (HtmlColumn) findComponentInRoot("coNoFactura2234");
		}
		return coNoFactura2234;
	}

	protected HtmlOutputText getLblDRNoFactura1() {
		if (lblDRNoFactura1 == null) {
			lblDRNoFactura1 = (HtmlOutputText) findComponentInRoot("lblDRNoFactura1");
		}
		return lblDRNoFactura1;
	}

	protected HtmlOutputText getLblNoFactura234() {
		if (lblNoFactura234 == null) {
			lblNoFactura234 = (HtmlOutputText) findComponentInRoot("lblNoFactura234");
		}
		return lblNoFactura234;
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

	protected HtmlDialogWindowAutoPostBackFlags getApbDetalle() {
		if (apbDetalle == null) {
			apbDetalle = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbDetalle");
		}
		return apbDetalle;
	}

	protected HtmlDialogWindowHeader getHdDetalleCam() {
		if (hdDetalleCam == null) {
			hdDetalleCam = (HtmlDialogWindowHeader) findComponentInRoot("hdDetalleCam");
		}
		return hdDetalleCam;
	}

	protected HtmlGridView getGvDetalleDCambios() {
		if (gvDetalleDCambios == null) {
			gvDetalleDCambios = (HtmlGridView) findComponentInRoot("gvDetalleDCambios");
		}
		return gvDetalleDCambios;
	}

	protected HtmlLink getLnkDetalleRecibosxDevoluciones() {
		if (lnkDetalleRecibosxDevoluciones == null) {
			lnkDetalleRecibosxDevoluciones = (HtmlLink) findComponentInRoot("lnkDetalleRecibosxDevoluciones");
		}
		return lnkDetalleRecibosxDevoluciones;
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

	protected HtmlLink getLnkCerrarDetalleDevoluciones() {
		if (lnkCerrarDetalleDevoluciones == null) {
			lnkCerrarDetalleDevoluciones = (HtmlLink) findComponentInRoot("lnkCerrarDetalleDevoluciones");
		}
		return lnkCerrarDetalleDevoluciones;
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

	protected HtmlOutputText getLblRcTmp_refer3() {
		if (lblRcTmp_refer3 == null) {
			lblRcTmp_refer3 = (HtmlOutputText) findComponentInRoot("lblRcTmp_refer3");
		}
		return lblRcTmp_refer3;
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

	protected HtmlDialogWindow getDwDetTipoReciboxMetodoPago() {
		if (dwDetTipoReciboxMetodoPago == null) {
			dwDetTipoReciboxMetodoPago = (HtmlDialogWindow) findComponentInRoot("dwDetTipoReciboxMetodoPago");
		}
		return dwDetTipoReciboxMetodoPago;
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

	protected HtmlOutputText getLblValidarArqueo() {
		if (lblValidarArqueo == null) {
			lblValidarArqueo = (HtmlOutputText) findComponentInRoot("lblValidarArqueo");
		}
		return lblValidarArqueo;
	}

	protected HtmlLink getLnkCerrarVarqueo() {
		if (lnkCerrarVarqueo == null) {
			lnkCerrarVarqueo = (HtmlLink) findComponentInRoot("lnkCerrarVarqueo");
		}
		return lnkCerrarVarqueo;
	}

	protected HtmlDialogWindow getDwValidarArqueo() {
		if (dwValidarArqueo == null) {
			dwValidarArqueo = (HtmlDialogWindow) findComponentInRoot("dwValidarArqueo");
		}
		return dwValidarArqueo;
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

	protected HtmlDialogWindowHeader getHdProcArqueo() {
		if (hdProcArqueo == null) {
			hdProcArqueo = (HtmlDialogWindowHeader) findComponentInRoot("hdProcArqueo");
		}
		return hdProcArqueo;
	}

	protected HtmlPanelGrid getGrdProcArqueo() {
		if (grdProcArqueo == null) {
			grdProcArqueo = (HtmlPanelGrid) findComponentInRoot("grdProcArqueo");
		}
		return grdProcArqueo;
	}

	protected HtmlGraphicImageEx getImgProcArqueo() {
		if (imgProcArqueo == null) {
			imgProcArqueo = (HtmlGraphicImageEx) findComponentInRoot("imgProcArqueo");
		}
		return imgProcArqueo;
	}

	protected HtmlOutputText getLblavisoFaltanteArqueo() {
		if (lblavisoFaltanteArqueo == null) {
			lblavisoFaltanteArqueo = (HtmlOutputText) findComponentInRoot("lblavisoFaltanteArqueo");
		}
		return lblavisoFaltanteArqueo;
	}

	protected HtmlLink getLnkProcArqueoOk() {
		if (lnkProcArqueoOk == null) {
			lnkProcArqueoOk = (HtmlLink) findComponentInRoot("lnkProcArqueoOk");
		}
		return lnkProcArqueoOk;
	}

	protected HtmlDialogWindow getDwConfirmarProcesarArqueo() {
		if (dwConfirmarProcesarArqueo == null) {
			dwConfirmarProcesarArqueo = (HtmlDialogWindow) findComponentInRoot("dwConfirmarProcesarArqueo");
		}
		return dwConfirmarProcesarArqueo;
	}

	protected HtmlDialogWindowClientEvents getCleProcArqueo() {
		if (cleProcArqueo == null) {
			cleProcArqueo = (HtmlDialogWindowClientEvents) findComponentInRoot("cleProcArqueo");
		}
		return cleProcArqueo;
	}

	protected HtmlDialogWindowRoundedCorners getRcProcArqueo() {
		if (rcProcArqueo == null) {
			rcProcArqueo = (HtmlDialogWindowRoundedCorners) findComponentInRoot("rcProcArqueo");
		}
		return rcProcArqueo;
	}

	protected HtmlDialogWindowContentPane getCpProcArqueo() {
		if (cpProcArqueo == null) {
			cpProcArqueo = (HtmlDialogWindowContentPane) findComponentInRoot("cpProcArqueo");
		}
		return cpProcArqueo;
	}

	protected HtmlOutputText getLblConfirmProcArqueo() {
		if (lblConfirmProcArqueo == null) {
			lblConfirmProcArqueo = (HtmlOutputText) findComponentInRoot("lblConfirmProcArqueo");
		}
		return lblConfirmProcArqueo;
	}

	protected HtmlPanelGrid getGrdAvisoDebCajero() {
		if (grdAvisoDebCajero == null) {
			grdAvisoDebCajero = (HtmlPanelGrid) findComponentInRoot("grdAvisoDebCajero");
		}
		return grdAvisoDebCajero;
	}

	protected HtmlPanelGrid getGrdAOpProcesarAr() {
		if (grdAOpProcesarAr == null) {
			grdAOpProcesarAr = (HtmlPanelGrid) findComponentInRoot("grdAOpProcesarAr");
		}
		return grdAOpProcesarAr;
	}

	protected HtmlLink getLnkProcArqueoCan() {
		if (lnkProcArqueoCan == null) {
			lnkProcArqueoCan = (HtmlLink) findComponentInRoot("lnkProcArqueoCan");
		}
		return lnkProcArqueoCan;
	}

	protected HtmlDialogWindowAutoPostBackFlags getApbProcArqueo() {
		if (apbProcArqueo == null) {
			apbProcArqueo = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbProcArqueo");
		}
		return apbProcArqueo;
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

	protected HtmlDialogWindowHeader getHdImpRtpAr() {
		if (hdImpRtpAr == null) {
			hdImpRtpAr = (HtmlDialogWindowHeader) findComponentInRoot("hdImpRtpAr");
		}
		return hdImpRtpAr;
	}

	protected HtmlPanelGrid getGrdImpRtpAr() {
		if (grdImpRtpAr == null) {
			grdImpRtpAr = (HtmlPanelGrid) findComponentInRoot("grdImpRtpAr");
		}
		return grdImpRtpAr;
	}

	protected HtmlGraphicImageEx getImgImpRtpAr() {
		if (imgImpRtpAr == null) {
			imgImpRtpAr = (HtmlGraphicImageEx) findComponentInRoot("imgImpRtpAr");
		}
		return imgImpRtpAr;
	}

	protected HtmlLink getLnkImpRtpArOk() {
		if (lnkImpRtpArOk == null) {
			lnkImpRtpArOk = (HtmlLink) findComponentInRoot("lnkImpRtpArOk");
		}
		return lnkImpRtpArOk;
	}

	protected HtmlDialogWindow getDwConfImprRptpre() {
		if (dwConfImprRptpre == null) {
			dwConfImprRptpre = (HtmlDialogWindow) findComponentInRoot("dwConfImprRptpre");
		}
		return dwConfImprRptpre;
	}

	protected HtmlDialogWindowClientEvents getCleImpRtpAr() {
		if (cleImpRtpAr == null) {
			cleImpRtpAr = (HtmlDialogWindowClientEvents) findComponentInRoot("cleImpRtpAr");
		}
		return cleImpRtpAr;
	}

	protected HtmlDialogWindowRoundedCorners getRcImpRtpAr() {
		if (rcImpRtpAr == null) {
			rcImpRtpAr = (HtmlDialogWindowRoundedCorners) findComponentInRoot("rcImpRtpAr");
		}
		return rcImpRtpAr;
	}

	protected HtmlDialogWindowContentPane getCpImpRtpAr() {
		if (cpImpRtpAr == null) {
			cpImpRtpAr = (HtmlDialogWindowContentPane) findComponentInRoot("cpImpRtpAr");
		}
		return cpImpRtpAr;
	}

	protected HtmlOutputText getLblConfirmImpRtpAr() {
		if (lblConfirmImpRtpAr == null) {
			lblConfirmImpRtpAr = (HtmlOutputText) findComponentInRoot("lblConfirmImpRtpAr");
		}
		return lblConfirmImpRtpAr;
	}

	protected HtmlJspPanel getJspImpRtpAr() {
		if (jspImpRtpAr == null) {
			jspImpRtpAr = (HtmlJspPanel) findComponentInRoot("jspImpRtpAr");
		}
		return jspImpRtpAr;
	}

	protected HtmlLink getLnkImpRtpArCan() {
		if (lnkImpRtpArCan == null) {
			lnkImpRtpArCan = (HtmlLink) findComponentInRoot("lnkImpRtpArCan");
		}
		return lnkImpRtpArCan;
	}

	protected HtmlDialogWindowAutoPostBackFlags getApbImpRtpAr() {
		if (apbImpRtpAr == null) {
			apbImpRtpAr = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbImpRtpAr");
		}
		return apbImpRtpAr;
	}

	protected HtmlDialogWindowHeader getHdProcAr() {
		if (hdProcAr == null) {
			hdProcAr = (HtmlDialogWindowHeader) findComponentInRoot("hdProcAr");
		}
		return hdProcAr;
	}

	protected HtmlJspPanel getJspProcAr0() {
		if (jspProcAr0 == null) {
			jspProcAr0 = (HtmlJspPanel) findComponentInRoot("jspProcAr0");
		}
		return jspProcAr0;
	}

	protected HtmlGraphicImageEx getImgProcAr() {
		if (imgProcAr == null) {
			imgProcAr = (HtmlGraphicImageEx) findComponentInRoot("imgProcAr");
		}
		return imgProcAr;
	}

	protected HtmlOutputText getLblMsgProcArqueo() {
		if (lblMsgProcArqueo == null) {
			lblMsgProcArqueo = (HtmlOutputText) findComponentInRoot("lblMsgProcArqueo");
		}
		return lblMsgProcArqueo;
	}

	protected HtmlLink getLnkCerrarProcArqueo() {
		if (lnkCerrarProcArqueo == null) {
			lnkCerrarProcArqueo = (HtmlLink) findComponentInRoot("lnkCerrarProcArqueo");
		}
		return lnkCerrarProcArqueo;
	}

	protected HtmlDialogWindow getDwMsgProcesarArq() {
		if (dwMsgProcesarArq == null) {
			dwMsgProcesarArq = (HtmlDialogWindow) findComponentInRoot("dwMsgProcesarArq");
		}
		return dwMsgProcesarArq;
	}

	protected HtmlDialogWindowClientEvents getCeProcAr() {
		if (ceProcAr == null) {
			ceProcAr = (HtmlDialogWindowClientEvents) findComponentInRoot("ceProcAr");
		}
		return ceProcAr;
	}

	protected HtmlDialogWindowRoundedCorners getRcProcAr() {
		if (rcProcAr == null) {
			rcProcAr = (HtmlDialogWindowRoundedCorners) findComponentInRoot("rcProcAr");
		}
		return rcProcAr;
	}

	protected HtmlDialogWindowContentPane getCpProcAr() {
		if (cpProcAr == null) {
			cpProcAr = (HtmlDialogWindowContentPane) findComponentInRoot("cpProcAr");
		}
		return cpProcAr;
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

	protected HtmlOutputText getLblRcTmp_refer334rr() {
		if (lblRcTmp_refer334rr == null) {
			lblRcTmp_refer334rr = (HtmlOutputText) findComponentInRoot("lblRcTmp_refer334rr");
		}
		return lblRcTmp_refer334rr;
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

	protected HtmlBehaviorKeyPress getBehaviorKeyPress1() {
		if (behaviorKeyPress1 == null) {
			behaviorKeyPress1 = (HtmlBehaviorKeyPress) findComponentInRoot("behaviorKeyPress1");
		}
		return behaviorKeyPress1;
	}

}
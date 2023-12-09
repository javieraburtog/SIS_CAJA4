package com.casapellas.dao;
/**
 * CASA PELLAS S.A.
 * Creado por.........: Carlos Manuel Hernández Morrison
 * Fecha de Creación..: 29/07/2009
 * Última modificación: Juan Carlos Ñamendi Pineda
 * Modificado por.....:	08/06/2012
 * Descripción:.......: Correccion de arqueo* 
 */
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import javax.faces.application.NavigationHandler;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlInputTextarea;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
//import org.apache.commons.mail.EmailAttachment;
//import org.apache.commons.mail.MultiPartEmail;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.casapellas.conciliacion.GnrMinutaAutomatica;
import com.casapellas.controles.ArqueoCajaCtrl;
import com.casapellas.controles.ArqueoControl;
import com.casapellas.controles.ArqueofacCtrl;
import com.casapellas.controles.ArqueorecCtrl;
import com.casapellas.controles.ClsParametroCaja;
import com.casapellas.controles.ConsolidadoDepositosBcoCtrl;
import com.casapellas.controles.DonacionesCtrl;
import com.casapellas.controles.PlanMantenimientoTotalCtrl;
import com.casapellas.controles.tmp.BancoCtrl;
import com.casapellas.controles.tmp.CompaniaCtrl;
import com.casapellas.controles.tmp.CtrlCajas;
import com.casapellas.controles.tmp.EmpleadoCtrl;
import com.casapellas.controles.FacturaCrtl;
import com.casapellas.controles.MetodosPagoCtrl;
import com.casapellas.controles.tmp.MonedaCtrl;
import com.casapellas.controles.tmp.ReciboCtrl;
import com.casapellas.controles.SalidasCtrl;
import com.casapellas.controles.TasaCambioCtrl;

import com.casapellas.donacion.entidades.DncDonacion;

import com.casapellas.entidades.A02factco;
import com.casapellas.entidades.Aplicacion;
import com.casapellas.entidades.Arqueo;
import com.casapellas.entidades.B64strfile;
import com.casapellas.entidades.Cambiodet;
import com.casapellas.entidades.Dfactura;
import com.casapellas.entidades.F55ca01;
import com.casapellas.entidades.F55ca014;
import com.casapellas.entidades.F55ca017;
import com.casapellas.entidades.F55ca022;
import com.casapellas.entidades.Hfactura;
import com.casapellas.entidades.HistoricoReservasProformas;
import com.casapellas.entidades.Minutadp;
import com.casapellas.entidades.Numcaja;
import com.casapellas.entidades.Recibo;
import com.casapellas.entidades.Recibodet;
import com.casapellas.entidades.Recibojde;
import com.casapellas.entidades.Tcambio;
import com.casapellas.entidades.Transactsp;
import com.casapellas.entidades.Vreporterecibos;
import com.casapellas.entidades.ens.Vautoriz;
import com.casapellas.entidades.Vdetallecambiorec;
import com.casapellas.entidades.Vdetallecambiorecibo;
import com.casapellas.entidades.Vf0101;
import com.casapellas.entidades.Vf0901;
import com.casapellas.entidades.Vf55ca01;
import com.casapellas.entidades.Vhfactura;
import com.casapellas.entidades.Vmonedafactrec;
import com.casapellas.entidades.Vrecibosxtipoegreso;
import com.casapellas.entidades.Vrecibosxtipoingreso;
import com.casapellas.entidades.Vrecibosxtipompago;
import com.casapellas.entidades.VrecibosxtipompagoId;
import com.casapellas.entidades.Vreciboxdevolucion;
import com.casapellas.entidades.Vsalida;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.jde.creditos.CodigosJDE1;
import com.casapellas.jde.creditos.DatosComprobanteF0911;
import com.casapellas.navegacion.As400Connection;
import com.casapellas.reportes.ArqueoCajaR;
import com.casapellas.reportes.ArqueoSocketPos;
import com.casapellas.reportes.Rptmcaja014_RecibosCaja;
import com.casapellas.util.CalendarToJulian;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.CustomEmailAddress;
import com.casapellas.util.Divisas;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.MailHelper;
import com.casapellas.util.PropertiesSystem;
import com.casapellas.util.SqlUtil;
import com.ibm.faces.component.html.HtmlJspPanel;
import com.infragistics.faces.grid.component.Cell;
import com.infragistics.faces.grid.component.RowItem;
import com.infragistics.faces.grid.component.html.HtmlColumn;
import com.infragistics.faces.grid.component.html.HtmlGridView;
import com.infragistics.faces.input.component.html.HtmlCheckBox;
import com.infragistics.faces.input.component.html.HtmlDateChooser;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
import com.infragistics.faces.shared.component.DataRepeater;
import com.infragistics.faces.shared.component.html.HtmlLink;
import com.infragistics.faces.shared.smartrefresh.SmartRefreshManager;
import com.infragistics.faces.window.component.html.HtmlDialogWindow;
import com.infragistics.faces.window.component.html.HtmlDialogWindowHeader;
import com.casapellas.socketpos.PosCtrl;
import com.casapellas.socketpos.TransaccionTerminal;
import com.casapellas.socketpos.bac.pojos.Response;
import com.casapellas.socketpos.bac.transactions.ResponseCodes;
import com.crystaldecisions.report.web.viewer.ReportExportControl;
import com.crystaldecisions.reports.sdk.ReportClientDocument;
import com.crystaldecisions.sdk.occa.report.application.OpenReportOptions;
import com.crystaldecisions.sdk.occa.report.exportoptions.ExportOptions;
import com.crystaldecisions.sdk.occa.report.exportoptions.PDFExportFormatOptions;
import com.crystaldecisions.sdk.occa.report.exportoptions.ReportExportFormat;

@SuppressWarnings({"unchecked"})
public class ArqueoCajaDAO {
	Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	//Valores reimplentacion JDE
		
		String[] valoresJdeNumeracion = (String[]) m.get("valoresJDENumeracionIns");
		String[] valoresJDEInsCredito = (String[]) m.get("valoresJDEInsCredito");
		String[] valoresJdeInsContado = (String[]) m.get("valoresJDEInsContado");
	
	private boolean renderCierrePOS = false;
	
	private HtmlGridView gvRsmTransactTerminales;
	private List<TransaccionTerminal> rsmTerminales;
	private HtmlGridView gvTransaccionesPOS;
	private List<Transactsp> lstTransaccionesPOS;
	private String lblMsgValidaSocketPos;
	private String lblDtaTerminalCierre; 
	
	private HtmlDialogWindow dwRsmTransactSocketPos;
	private HtmlDialogWindow dwTransaccionesPOS;
	private HtmlDialogWindow dwDwnCierreRpt;
	private HtmlDialogWindow dwConfirmaCierreTerminal;
	private HtmlDialogWindow dwValidaSocketPos;
	private HtmlLink lnkCerrarTermSinTransact;
	
	/*************** CONSTANTES  *******************/
	
	final static String R_CONTADO = "CO";
	final static String R_ABONO   = "CR";
	final static String R_PRIMAS  = "PR";
	final static String R_INGEX   = "EX";
	final static int 	CodRecibo = 1;
	
	private HtmlDropDownList ddlFiltroCompania,ddlFiltroMoneda;
	private List lstFiltroComp,lstFiltroMoneda;
	private HtmlOutputText lblMsgNoRegistros,lblFechaHoraArqueo,lblEthoraArqueo;
	private HtmlOutputText lbletMsgArqueoPrevio,lblMsgArqueoPrevio;
	private HtmlDialogWindow dwCargando;
		
	/********** VARIABLES DE INGRESOS *************/
	private HtmlOutputText lblVentasTotales,lblTotalDevoluciones,lblTotalVentasNetas;
	private HtmlOutputText lblTotalAbonos,lblTotalPrimas,lblTotalOtrosIngresos,lblTotaIngresos;
	private HtmlOutputText lblOEmonEx,lblTotalIngRecxmonex,lblIngresosExord,lblCambiosOtraMon;
	private HtmlOutputText lbliniexDepBanco,lbliniexTransBanco,lblTotalFinanciamiento;
	
	private HtmlDialogWindow dwFacturasRegistradas,dwRecibosxTipoIngreso;
	private List lstFacturasRegistradas,lstRecibosxIngresos;
	private HtmlGridView gvFacturaRegistradas,gvRecibosIngresos;
	private String lblEtCantFacC0,lblCantFacCO,lblEtCantFacCr,lblCantFacCr;
	private String lblEtTotalFacCo,lblTotalFacCo,lblEtTotalFacCr,lblTotalFacCr;
	
	/********** Variables del Detalle de Facturas *************/
	private HtmlDialogWindow dgDetalleFactura;	
	private HtmlDropDownList ddlDetalleFacCon;
	private List lstMonedasDetalle,lstCierreCajaDetfactura;
	private HtmlGridView gvDfacturasDiario;
	private String txtFechaFactura, txtNofactura = "", txtCliente = "", txtCodigoCliente = "";
	private String txtCodUnineg = "", txtUnineg = "", txtTotal = "";
	private double txtSubtotal,txtIva;
	private HtmlOutputText lblTasaDetalle,txtTasaDetalle;
	private HtmlDialogWindowHeader hdFactura,hdRecxTipoIng,hdDetTrecxMetPago;
	
	/********* Variables de Detalle de Recibo *****************/
	private HtmlDialogWindow dwDetalleRecibo;
	private HtmlOutputText txtHoraRecibo,txtNoRecibo,txtDRCodCli,txtNoBatch,txtDRNomCliente;
	private HtmlOutputText txtMontoAplicar,txtMontoRecibido,txtDetalleCambio;	
	private HtmlInputTextarea txtConcepto;
	private HtmlGridView gvDetalleRecibo,gvFacturasRecibo;
	private List lstDetalleRecibo,lstFacturasRecibo;
	
	/******* variables de Egresos y/o Ajustes *****************/
	private HtmlOutputText lblTotalVtsCredito,lblVtsTCredito,lblVtsDDbanco,lblVtsTransBanc,lblTotalVtsPagBanco;
	private HtmlOutputText lblAbonoTCredito,lblAbonoDDbanco,lblAbonoTransBanc,lblTotalAbonoPagBanco; 
	private HtmlOutputText lblTotalPrimasPagBanco,lblPrimasTransBanc,lblPrimasDDbanco,lblPrimasTCredito;
	private HtmlOutputText lblOEcambios,lblOEsalidas,lblTotalOtrosEgresos,lblTotalEgresos;
	private HtmlOutputText lblegIexH,lblegIexN,lblegIex8,lblegTotalIex;
	private HtmlOutputText lblFinanTCredito, lblFinanDbanco, lblFinanTransBanc, lblTotalFinanPagBanco;
	
	private HtmlDialogWindow dwDetalleCambios;
	private HtmlGridView gvDetalleDCambios;
	private List lstDetalleCambios;	
	
	/******* variables para el calculo del deposito final************/
	private HtmlOutputText lblCDC_efectnetoRec,	lblCDC_montominimo,lblet_SobranteFaltante,lblCDC_pagocheques;
	private HtmlOutputText lblCDC_SobranteFaltante,lblCDC_depositoSug,lblCDC_depositoFinal, lblCDC_montoMinReint, lblCDC_montoMinAjust;
	private HtmlInputText txtCDC_efectivoenCaja,txtCDC_ReferDeposito;	
	private HtmlOutputText lblACDetfp_Efectivo,lblACDetfp_Cheques,lblACDetfp_TarCred;
	private HtmlOutputText lblACDetfp_TCmanual, lblACDetfp_TCSocketpos, lblACDetfp_DepDbanco,lblACDetfp_TransBanco,lblACDetfp_Total;
	
	/*******  Variables de Detalle de recibos por tipoRec y metodo de pago************/
	private HtmlDialogWindow dwDetTipoReciboxMetodoPago;
	private HtmlGridView gvRecibosxTipoyMetodopago;
	private List lstRecxTipoyMetpago;
	
	/******  Variables para mostrar los recibos pagados con monedas diferentes a la de la factura **/
	private HtmlDialogWindow dwDetallerecpagmonEx;
	private HtmlGridView gvDetRecpagMonEx;
	private List lstDetRecpagMonEx;
	private HtmlDialogWindowHeader hdDetrecpagmonEx;
	
	/******** Variables para el dw de Imprimir RptArqueo, ValidarArqueo y ProcesarArqueo ******************/
	private HtmlDialogWindow dwValidarArqueo,dwConfirmarProcesarArqueo,dwConfImprRptpre,dwMsgProcesarArq;
	private HtmlOutputText lblValidarArqueo,lblMsgProcArqueo,lblavisoFaltanteArqueo;
	
	//-------- Objetos de Encabezados de detalle de factura o detalle del bien del recibo.
	private HtmlOutputText lblNoFactura2,lblTipofactura2,lblUnineg2,lblMoneda2,lblFecha23,lblPartida23;	
	private HtmlColumn coNoFactura2;
	
	//-------- Ventana de detalle de Salida.
	private HtmlDialogWindow dwDetalleSalidas;
	private HtmlGridView gvDetalleSalidas;
	private List lstDetalleSalidas;
	
	//-------- Opción de Arqueo de día anterior.
	private HtmlCheckBox chkArqueoDiaAnterior;
	private HtmlOutputText lblChkDiaAnterior;
	private HtmlDateChooser dcFechaArqueoAnterior;
	private HtmlOutputText lbletFechaArqueo;
	private HtmlPanelGrid pgrHoraArqueo;
	private List lstArqueoHini,lstArqueoMini,lstArqueoHFin,lstArqueoMFin;
	private HtmlDropDownList ddlArqueoHini,ddlArqueoMini,ddlArqueoHFin,ddlArqueoMFin;
	
	private String rutaarqueo;
	private String rutaarqueoprelm;
	
	private HtmlGridView gvDonacionesFormaPago ;
	private List<DncDonacion> lstDonacionesFormaPago ;
	private HtmlDialogWindow dwDetalleDonacionesMpago;
	private HtmlGridView gvDetalleDonacionesMpago; 
	private List<DncDonacion> lstDetalleDonacionesMpago;
	
	private HtmlDialogWindow dwRecibosxMpago8N;
	private HtmlDialogWindowHeader hdReciboMPago8N;
	private HtmlGridView gvRecibosxMpago8N;
	private List<Vrecibosxtipompago> lstRecibosxMpago8N;
	
	//&& =========== anticipos por PMT 
	private HtmlOutputText lblTotalRecibosPMT ;
	private HtmlOutputText lblpmtTarjetaCredito        ;
	private HtmlOutputText lblpmtDepositoDirectoBanco  ;
	private HtmlOutputText lblpmtTransferenciaBancaria ;
	private HtmlOutputText lblTotalPmtPagoEnBanco      ;
	
	private HtmlJspPanel pnlDatosFacturas ;
	private HtmlJspPanel pnlDatosAnticiposPMT ;
	
	private List<HistoricoReservasProformas> detalleContratoPmt ;
	private HtmlGridView gvDetalleContratoPmt;
	
	private static ClsParametroCaja cajaparm = new ClsParametroCaja();
	
	public static boolean procesoContableCierreDonaciones(int noarqueo, int caid, String codcomp, String moneda, Date fecha,Vautoriz vaut,Session s ){
		
		boolean hecho = true;
		try {
			
				
			
			F55ca014 f14 = com.casapellas.controles.CompaniaCtrl.obtenerF55ca014( caid, codcomp );
			String monedabase = f14.getId().getC4bcrcd();
			
			
			DonacionesCtrl.caid = caid;
			DonacionesCtrl.codcomp = codcomp;
		
			 hecho = DonacionesCtrl.comprobantesCierreDonaciones( null, moneda, noarqueo, fecha, monedabase, vaut, s) ;
			
	
			
			
		} catch (Exception e) {
			hecho = false;
			e.printStackTrace(); 
		}finally{
			
		}
		return hecho;
	}
	
	public boolean procesarDonacionesArqueoCaja(int caid, String codcomp, int coduser, int noarqueo, Date fecha ){
		boolean valido = true;
		
		try {
			
			//&& ============ sacar la retencion del monto sin comision por tarjetas de credito
			final BigDecimal bdTasaRetencion ;
			F55ca014 f14 = com.casapellas.controles.CompaniaCtrl.obtenerF55ca014( caid, codcomp );
			 if(f14 != null){
				 bdTasaRetencion = f14.getId().getC4trir();
			 }else{
				 bdTasaRetencion = BigDecimal.ZERO;
			 }
			
			final BigDecimal BigDecimal_CIEN = new BigDecimal("100");
			List<String>queriesInsert = new ArrayList<String>();
			String current_timestamp_dncrsm ;
			
			String strSqlInsertCierreDnc =  
					"insert into "+PropertiesSystem.ESQUEMA+".dnc_cierre_donacion " +
					"( idbeneficiario, caid, codcomp, moneda, noarqueo, montorecibido, montoneto, transacciones," +
					" fechacierre, transactrc, montorecibos, estado, observaciones,usrcrea,usrmod, codigobeneficiario ) values" +
					"( @IDBENEFICIARIO, @CAID, '@CODCOMP','@MONEDA', @NOARQUEO, @MONTORECIBIDO, @MONTONETO, @TRANSACCIONES, " +
					"'@FECHACIERRE', @TRANSACTRC, @MONTORECIBOS, @ESTADO, '@OBSERVACIONES', @USRCREA, @USRMOD, @CODIGOBENEFICIARIO ) " ;
			
			String sqlSelectCierreDnc = "select IDCIERREDONACION from "+PropertiesSystem.ESQUEMA
						+".dnc_cierre_donacion where FECHACIERRE = '@FECHACIERRE'" ;
			
			String strSqlInsertCierreDetalle = "insert into "+PropertiesSystem.ESQUEMA+".dnc_cierre_detalle " +
				"( iddonacion, idcierrednc, monto, formapago, moneda, fechacrea,fechamod ) " +
				"(select iddonacion, (@SELECT_ID_CIERRE), montorecibido, formadepago,moneda, '@FECHAINSERT', '@FECHAINSERT' "+ 
				" from "+PropertiesSystem.ESQUEMA+".dnc_donacion where iddonacion in (@IDS_DONACION) )";
			
			String strSqlInsertCierreRsmFormaPago = 
					" insert into "+PropertiesSystem.ESQUEMA+".dnc_donacion_cns_mpago " +
					"( idcierredonacion, codigobeneficiario, formadepago, montobruto, montoneto, " +
					"transacciones,referencia,usrcrea,fechacrea)" +
					" values " +
					" ( (@IDCIERREDONACION), @CODIGOBENEFICIARIO, '@FORMADEPAGO', @MONTOBRUTO, " +
					"   @MONTONETO, @TRANSACCIONES, '@REFERENCIA', @USRCREA, '@FECHACREA')";
			
			String sqlUpdateEstadoDonacion = "update "+PropertiesSystem.ESQUEMA+".dnc_donacion set estado = 3, fechamod = current_timestamp  where iddonacion in (@IDS_DONACION) ";
			
			List<DncDonacion> donacionesCaja = (ArrayList<DncDonacion>)CodeUtil.getFromSessionMap("ac_lstDonacionesArqueoCaja") ;
			
			//&& ============ crear los nuevos montos aplicados.
			CollectionUtils.forAllDo(donacionesCaja, new Closure(){

				public void execute(Object o) {
					DncDonacion dnc = (DncDonacion)o ;
					
					if( dnc.getComisionpos().compareTo(BigDecimal.ZERO) != 0 ){
						
						//& ============ sacar comision
						BigDecimal bdMontoNeto =  dnc.getMontorecibido().subtract( 
								dnc.getMontorecibido().multiply(dnc.getComisionpos()
								.divide( BigDecimal_CIEN, 4, RoundingMode.HALF_UP ) )  ) ;
						
						//& ============ sacar retencion
						if(dnc.getFormadepago().compareTo(MetodosPagoCtrl.TARJETA) == 0) {
							bdMontoNeto =  bdMontoNeto.subtract( bdMontoNeto.multiply( 
										bdTasaRetencion.divide(BigDecimal_CIEN, 4, RoundingMode.HALF_UP)  ) ) ;
						}
						dnc.setMontoaplicado( bdMontoNeto );
					}
				}
			});
			
			//&& ============ Sacar los beneficiarios
			List<Integer>codigosBnfs = (ArrayList<Integer>)CodeUtil.selectPropertyListFromEntity(donacionesCaja, "codigo", true);	
			for (final Integer codigobnf : codigosBnfs) {
				
				List<DncDonacion> dncsxbnf = (ArrayList<DncDonacion>)
				CollectionUtils.select(donacionesCaja, new Predicate(){
					public boolean evaluate(Object o) {
						return ((DncDonacion)o).getCodigo() == codigobnf;
					}
				}); 
				
				List<String[]>totalesxformapago = new ArrayList<String[]>();
				
				List<String>formasPago = (ArrayList<String>)CodeUtil.selectPropertyListFromEntity(dncsxbnf, "formadepago", true);	
				for (final String mpago : formasPago) {
					
					List<DncDonacion> dncsxbnfxfpago = (ArrayList<DncDonacion>)
							CollectionUtils.select(dncsxbnf, new Predicate(){
								public boolean evaluate(Object o) {
									return ((DncDonacion)o).getFormadepago().compareTo(mpago) == 0 ;
							}
						});
					
					BigDecimal totalxmpagoBruto = CodeUtil.sumPropertyValueFromEntityList(dncsxbnfxfpago, "montorecibido", false);
					BigDecimal totalxmpagoNeto = BigDecimal.ZERO; 
					
					if(mpago.compareTo(MetodosPagoCtrl.TARJETA) == 0 ){
						totalxmpagoNeto = CodeUtil.sumPropertyValueFromEntityList(dncsxbnfxfpago, "montoaplicado", false);
					}else{
						totalxmpagoNeto = CodeUtil.sumPropertyValueFromEntityList(dncsxbnfxfpago, "montorecibido", false);
					}
					totalesxformapago.add(new String[]{
							mpago, totalxmpagoNeto.toString(), 
							Integer.toString( dncsxbnfxfpago.size() ),
							totalxmpagoBruto.toString() 
						});
				}
				
				BigDecimal totalrecibidoTmp = CodeUtil.sumPropertyValueFromEntityList(dncsxbnf, "montorecibido", false);
				
				BigDecimal totalAplicadoTmp = BigDecimal.ZERO;
				for (String[] totalMpago : totalesxformapago) {
					totalAplicadoTmp = totalAplicadoTmp.add(new BigDecimal (totalMpago[1]) ); 
				}
				
				//&& ============ donaciones aosciadas a recibos.
				List<DncDonacion> dncxBnfxRcs = (ArrayList<DncDonacion>)
				CollectionUtils.select(dncsxbnf, new Predicate(){
					public boolean evaluate(Object o) {
						return  ((DncDonacion)o).getNumrec() != 0 && !((DncDonacion)o).getTiporec().isEmpty();
					}
				}); 
				
				BigDecimal totalRecibos = CodeUtil.sumPropertyValueFromEntityList(dncxBnfxRcs, "montorecibido", false);
				int cantDncxRecibos = dncxBnfxRcs.size();
				
				current_timestamp_dncrsm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.")
				 	.format(fecha) + String.valueOf( (100000 + new Random().nextInt(900000)) );
				
				DncDonacion dncInsert = dncsxbnf.get(0);
				
				queriesInsert.add( 
					strSqlInsertCierreDnc
						.replace("@IDBENEFICIARIO", Integer.toString( dncInsert.getIdbenficiario() ) )
						.replace("@CAID", Integer.toString( dncInsert.getCaid() ) )
						.replace("@CODCOMP", dncInsert.getCodcomp() )
						.replace("@MONEDA", dncInsert.getMoneda() )
						.replace("@NOARQUEO",  Integer.toString( noarqueo ))
						.replace("@MONTORECIBIDO", totalrecibidoTmp.toString() )
						.replace("@MONTONETO", totalAplicadoTmp.toString())
						.replace("@TRANSACCIONES", Integer.toString( dncsxbnf.size() ))
						.replace("@FECHACIERRE", current_timestamp_dncrsm )
						.replace("@TRANSACTRC", Integer.toString(cantDncxRecibos))
						.replace("@MONTORECIBOS", totalRecibos.toString())
						.replace("@ESTADO", "1")
						.replace("@OBSERVACIONES", "")
						.replace("@USRCREA", Integer.toString( coduser ))
						.replace("@USRMOD", Integer.toString( coduser))
						.replace("@CODIGOBENEFICIARIO", Integer.toString( dncInsert.getCodigo() ))
				);
				
				//&& ==================== Detalle de transacciones por donacion incluidas en el cierre
				List<Integer> idsDnc = (ArrayList<Integer>)CodeUtil.selectPropertyListFromEntity(dncsxbnf, "iddonacion", true);
				String idsDncs = idsDnc.toString().replace("[", "").replace("]", "");
						
				queriesInsert.add( 
					strSqlInsertCierreDetalle.replace("@SELECT_ID_CIERRE", 
							sqlSelectCierreDnc.replace("@FECHACIERRE", current_timestamp_dncrsm) )
					.replace("@IDS_DONACION", idsDncs)
					.replace("@FECHAINSERT", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.ssssss").format(fecha))
				);
				
				//&& ==================== Resumen por forma de pago por cierre de donaciones
				for (String[] totalMpago : totalesxformapago) {
					
					queriesInsert.add( 
						strSqlInsertCierreRsmFormaPago
							.replace("@IDCIERREDONACION", sqlSelectCierreDnc.replace("@FECHACIERRE", current_timestamp_dncrsm) )
							.replace("@CODIGOBENEFICIARIO", Integer.toString( dncInsert.getIdbenficiario() ) )
							.replace("@FORMADEPAGO", totalMpago[0])
							.replace("@MONTOBRUTO", totalMpago[3])
							.replace("@MONTONETO", totalMpago[1])
							.replace("@TRANSACCIONES", totalMpago[2])
							.replace("@REFERENCIA", Integer.toString(noarqueo))
							.replace("@USRCREA",  Integer.toString(coduser))
							.replace("@FECHACREA", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.ssssss").format(fecha) ) 
					);
				} 
				//&& ==================== actualiar el estado de las donaciones. Estado : 3 Procesado!
				queriesInsert.add( 
					sqlUpdateEstadoDonacion.replace("@IDS_DONACION", idsDncs) 
				);
			}
			
			if(queriesInsert != null && !queriesInsert.isEmpty()){
				valido = ConsolidadoDepositosBcoCtrl.executeSqlQueries( queriesInsert ) ;
			}
			
		} catch (Exception e) {
			e.printStackTrace(); 
			valido = false;
		}
		return valido;
	}
	
	public void mostrarDetalleDonacionesMpago(ActionEvent ev){
			
		try {
			
			lstDetalleDonacionesMpago = new ArrayList<DncDonacion>(); 
			
			if(CodeUtil.getFromSessionMap("ac_lstDonacionesArqueoCaja") == null){
				return;
			}
			
			List<DncDonacion>lstDonacionesArqueo = (ArrayList<DncDonacion>)CodeUtil.getFromSessionMap("ac_lstDonacionesArqueoCaja");
			
			RowItem ri = (RowItem) ev.getComponent().getParent().getParent();
			final DncDonacion dncRsm = (DncDonacion) DataRepeater.getDataRow(ri);
			
			lstDetalleDonacionesMpago = (ArrayList<DncDonacion>) CollectionUtils
					.select(lstDonacionesArqueo, new Predicate() {
						public boolean evaluate(Object o) {
							return ((DncDonacion) o).getFormadepago()
									.compareTo(dncRsm.getFormadepago()) == 0;
						}
					});
			
			CodeUtil.putInSessionMap("ac_lstDetalleDonacionesMpago", lstDetalleDonacionesMpago);
			gvDetalleDonacionesMpago.dataBind();
			CodeUtil.refreshIgObjects(gvDetalleDonacionesMpago);
			
			
		} catch (Exception e) {
			e.printStackTrace(); 
		} 
		
		dwDetalleDonacionesMpago.setWindowState("normal");
	}
	public void cerrarDlgDetalleDonacion(ActionEvent ev){
		dwDetalleDonacionesMpago.setWindowState("hidden");
	}
	
	public void cerrarTerminalSinTransacciones(ActionEvent ev){
		String msg = "";
		
		try {
			if(!m.containsKey("ac_Terminal_Cerrar")){
				msg = "Datos inválidos para cierre, los datos de la terminal" +
						" deben ser nuevamente cargados ";
				return;
			}
			//&& ====== Aplicar el cierre a la terminal
			TransaccionTerminal terminal = (TransaccionTerminal) m
					.get("ac_Terminal_Cerrar");
			
			msg = PosCtrl.cerrarTerminalPOS(terminal, false);
			
			if (msg.isEmpty()) {
				msg = "Cierre Aplicado: Lote #"
						+ terminal.getBatchnumber()
						+ ", Transacciones Aplicadas: "
						+ terminal.getCant_Creditos()
						+ ", Monto: "
						+ new DecimalFormat("#,##0.00").format(terminal.getMto_Creditos());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			m.remove("ac_Terminal_Cerrar");
			lnkCerrarTermSinTransact.setRendered(false);
			lblMsgValidaSocketPos = msg;
			
			SmartRefreshManager.getCurrentInstance().addSmartRefreshId(
					lnkCerrarTermSinTransact.getClientId(FacesContext
							.getCurrentInstance()));
		}
	}
	
	//** ************************ Cierre de Ventanas 	
	public void cerrarDscRptSocket(ActionEvent ev) {
		try {
			TransaccionTerminal terminal = (TransaccionTerminal) m
					.get("ac_TerminalSeleccionada");

			if (terminal.getRutarealreporte() == null)
				return;

			if (new File(terminal.getRutarealreporte()).exists())
				new File(terminal.getRutarealreporte()).delete();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dwDwnCierreRpt.setWindowState("hidden");
		}
	}
	public void cerrarValidaSocketPos(ActionEvent ev) {
		dwValidaSocketPos.setWindowState("hidden");
	}
	public void cancelarCierreTerminal(ActionEvent ev) {
		dwConfirmaCierreTerminal.setWindowState("hidden");
	}
	public void cerrarResumenTerminal(ActionEvent ev){
		m.remove("ac_rsmTerminales");
		dwRsmTransactSocketPos.setWindowState("hidden");
	}
	//** ************************ Eventos de Socket Pos
	@SuppressWarnings("serial")
	public static void enviarCierreSocketCajero(TransaccionTerminal terminal){
		String htlm =  new String("");
		String cajeroCorreo = "";
		String cajeroNombre = "";
		String subject = "";
		String cssFuente = "font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;";
		
		try {
			
			int caid = ((ArrayList<Vf55ca01>) FacesContext.getCurrentInstance()
					.getExternalContext().getSessionMap().get("lstCajas"))
					.get(0).getId().getCaid();  
			
			Vf0101 vf = new EmpleadoCtrl().buscarEmpleadoxCodigo2(terminal.getUsrCodreg());
			cajeroCorreo = vf.getId().getWwrem1().trim();
			cajeroNombre = vf.getId().getAbalph();
			
			subject = "Reporte de cierre de Terminal "
					+ terminal.getTerminalid() + ", Caja " 
					+ caid + ",  " + new SimpleDateFormat("dd MMMM yyyy",
							new Locale("es", "ES")).format(new Date());
			
			htlm = "<br> <span style=\"" + cssFuente + "\" > <b>Señor (a): </b>"
					+ cajeroNombre.toUpperCase() + " </span>";
			
			htlm += "<br><br><span style=\"" + cssFuente
					+ "\" ><b> Adjunto se encuentra reporte de liquidación"
					+ " de transacciones de SocketPos </b> </span>";
			
			htlm += "<br><br><span style=\"" + cssFuente
					+ "\" ><b>	Terminal:</b> " + terminal.getTerminalid() + " /  "
					+ terminal.getNombreterminal().toLowerCase();
			
			htlm += "<br><span style=\"" + cssFuente + "\" > <b> Monto de Cierre:</b> "
					+ new DecimalFormat("#,##0.00").format(terminal
						.getMto_Creditos()) + " </span>";
			
			htlm += "<br><span style=\"" + cssFuente
					+ "\" ><b>Transacciones aprobadas:</b> "
					+ terminal.getCant_Creditos() + " </span>";
			htlm += "<br><br><span style=\"" + cssFuente + "\" > " +
					" Detalle generado a la fecha: " + new SimpleDateFormat(
						"dd-MMMM-yyyy hh:mm:ss a", new Locale("ES", "es"))
						.format(new Date());
			
			List<String[]>lstCtaCorreo = new ArrayList<String[]>();
			lstCtaCorreo.add(new String[]{ cajeroNombre, cajeroCorreo });
			
			List<CustomEmailAddress> toList = new ArrayList<CustomEmailAddress>();
			for (String[] dtaCta  : lstCtaCorreo) {
				toList.add(new CustomEmailAddress(dtaCta[0].trim(), dtaCta[1].trim()));
			}
			
			MailHelper.SendHtmlEmail(
					new CustomEmailAddress(cajaparm.getParametros("34", "0", "WEB_EMAIL_ADRESS").getValorAlfanumerico().toString(), "Módulo de Caja"),
					toList, null, new ArrayList<CustomEmailAddress>() { { add(new CustomEmailAddress(cajaparm.getParametros("34", "0", "MAIL_BOUNCEADDRESS").getValorAlfanumerico().toString())); } }, 
					subject, htlm.toString(), new String[] { terminal.getRutarealreporte() });
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			 htlm =  null;
			 cajeroCorreo = null;
			 cajeroNombre = null;
			 subject = null;
			 cssFuente = null;
		}
	}
	public void mostrarDescargaRptCierreSocket(ActionEvent ev){
		String msgError = ""; 
		TransaccionTerminal terminal = null;
		
		try {
			RowItem ri = (RowItem) ev.getComponent().getParent().getParent();
			terminal = (TransaccionTerminal) DataRepeater.getDataRow(ri);
			
			if (terminal.getRutarealreporte() == null) {
				msgError = "Archivo fisico del reporte no encontrado ";
				return;
			}
			if (!terminal.isTerm_cerrada()) {
				msgError = "La terminal no ha sido cerrada";
				return;
			}
			if (terminal.getDtaReporteCierre() == null
					|| terminal.getDtaReporteCierre().length == 0) {
				msgError = "Datos binarios del documento no encontrados";
				return;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			msgError = "No se puede generar el documento";
		}finally{
			if(msgError.compareTo("") == 0 ){
				dwDwnCierreRpt.setWindowState("normal");
				m.put("ac_TerminalSeleccionada", terminal); 
			}else{
				lnkCerrarTermSinTransact.setRendered(false);
				m.remove("ac_Terminal_Cerrar");
				lblMsgValidaSocketPos = msgError ;
				dwValidaSocketPos.setWindowState("normal");
				SmartRefreshManager.getCurrentInstance().addSmartRefreshId(
						dwValidaSocketPos.getClientId(FacesContext
								.getCurrentInstance()));
			}	
		}
	}
	public void cerrarTerminalPos(ActionEvent ev) {
		String msg = "";
		TransaccionTerminal terminal = null; 
		
		try {
			
			if(!m.containsKey("ac_Terminal_Cerrar")){
				msg = "Datos inválidos para cierre, los datos de la terminal" +
						" deben ser nuevamente cargados ";
				return;
			}
			
			//&& ====== Aplicar el cierre a la terminal
			terminal = (TransaccionTerminal)CodeUtil.getFromSessionMap( "ac_Terminal_Cerrar" ) ;
			
			int iSufijo = Integer
					.parseInt((int) Math.round(Math.random() * 100) + ""
							+ (int) Math.round(Math.random() * 10));
			
			String realpath = FacesContext
					.getCurrentInstance().getExternalContext()
					.getRealPath( File.separatorChar + "Confirmacion" + File.separatorChar);
		
			String filaname = "_CierreTerminal_"+terminal.getTerminalid()+".pdf";
			
			terminal.setRutarealreporte(realpath+""+iSufijo+""+filaname);
			terminal.setNombrereporte(+iSufijo+""+filaname);
			
			
			msg = PosCtrl.cerrarTerminalPOS(terminal, true);
			
			if(msg.compareTo("") != 0) {
				return;
			}
			
			//&& ========== generar Correo de notificacion de cierre de terminal.
			enviarCierreSocketCajero(terminal);
			
			//&& ========== Actualizar la lista de terminales
			rsmTerminales = (ArrayList<TransaccionTerminal>) CodeUtil.getFromSessionMap("ac_rsmTerminales");
			
			for (int i = 0; i < rsmTerminales.size(); i++) {
				TransaccionTerminal tmp = rsmTerminales.get(i);
				if(tmp.getTerminalid().compareTo(terminal.getTerminalid()) == 0 ){
					rsmTerminales.set(i, terminal);
					break;
				}
			}
			
			CodeUtil.putInSessionMap( "ac_rsmTerminales", rsmTerminales);
			
			int pagina = gvRsmTransactTerminales.getPageIndex() ;
			gvRsmTransactTerminales.dataBind();
			gvRsmTransactTerminales.setPageIndex(pagina);
			
		} catch (Exception e) {
			e.printStackTrace(); 
//			LogCrtl.sendLogDebgs(e);
			msg = "Cierre de terminal no completado";
		}finally{
			
			if (terminal != null && terminal.getRutarealreporte() != null
					&& new File(terminal.getRutarealreporte()).exists())
				new File(terminal.getRutarealreporte()).delete();
			
			//&& ======= Ventana de notificacion del proceso.
			if(msg.compareTo("") == 0){
				msg = "Cierre terminal aplicado correctamente";
			}else{
				CodeUtil.removeFromSessionMap( "ac_Terminal_Cerrar" );
			}
			
			dwConfirmaCierreTerminal.setWindowState("hidden");
			lblMsgValidaSocketPos = msg;
			lnkCerrarTermSinTransact.setRendered(false);
			dwValidaSocketPos.setWindowState("normal");
			
			CodeUtil.refreshIgObjects(dwValidaSocketPos) ;
			
		}
	}
	public void confirmaCierreTerminal(ActionEvent ev){
		boolean valido = true;
		TransaccionTerminal terminal = null;
		String msg = "";
		
		try {
			
			msg = PosCtrl.credomatic_SocketPos_TestConnection() ;
			if(!msg.isEmpty()) {
				return;
			}
			
			CodeUtil.removeFromSessionMap( "ac_Terminal_Cerrar");
			
			lnkCerrarTermSinTransact.setRendered(false);
			
			RowItem ri = (RowItem) ev.getComponent().getParent().getParent();
			terminal = (TransaccionTerminal) DataRepeater.getDataRow(ri);
			
			if (terminal == null) {
				msg = "Cierre de terminal no puede ser procesado, Error al " +
						"obtener datos de terminal";
				return;
			}
			if (terminal.isTerm_cerrada()) {
				msg = "No se puede aplicar cierre a la terminal seleccionada";
				return;
			}
			if (terminal.getTotalcierre().compareTo(BigDecimal.ZERO) <= 0) {
				msg = " No hay Transacciones Registradas para la Terminal ";
				CodeUtil.putInSessionMap("ac_Terminal_Cerrar", terminal);
				lnkCerrarTermSinTransact.setRendered(true);
				return;
			}
			
			
			String pruebaConexion = PosCtrl.credomatic_SocketPos_TestConnection();
		 
			if( !pruebaConexion.isEmpty() ) {
				msg = "No se ha podido establecer conexión con Socket Pos Credomatic ";
			}
			
			
			
		} catch (Exception e) {
			msg = "Cierre no puede ser procesado, datos de cierre no válidos";
			e.printStackTrace(); 
		}finally{
			
			valido = msg.isEmpty();
			
			if(valido){
				dwConfirmaCierreTerminal.setWindowState("normal");
				CodeUtil.putInSessionMap( "ac_Terminal_Cerrar", terminal);
				lblDtaTerminalCierre = " Se aplicará cierre de Terminal "
						+ terminal.getTerminalid() + " por "
						+ terminal.getTotalcierre();
			}else{
				
				lblMsgValidaSocketPos = msg ;
				dwValidaSocketPos.setWindowState("normal");
				
				CodeUtil.refreshIgObjects(dwValidaSocketPos) ;
				
			}
		}
	}
	public void mostrarTransaccionesTerminal(ActionEvent ev){
		try {
			lstTransaccionesPOS = null;
			m.remove("ac_lstTransaccionesPOS");
			
			RowItem ri = (RowItem) ev.getComponent().getParent().getParent();
			TransaccionTerminal terminal = (TransaccionTerminal) DataRepeater.getDataRow(ri);
			
			if(terminal == null)
				return;
			lstTransaccionesPOS = terminal.getTransacciones();
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}finally{
			if(lstTransaccionesPOS == null)
				lstTransaccionesPOS = new ArrayList<Transactsp>();
			CodeUtil.putInSessionMap("ac_lstTransaccionesPOS", lstTransaccionesPOS);
			
			gvTransaccionesPOS.dataBind();
			gvTransaccionesPOS.setPageIndex(0);
			dwTransaccionesPOS.setWindowState("normal");
		}
	}
	public void cerrarDetalleTransacciones(ActionEvent ev){
		try {
			m.remove("ac_lstTransaccionesPOS");
			dwTransaccionesPOS.setWindowState("hidden");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Autor: CarlosHernandez Fecha: 24 Marzo 2014 Descrp: Mostrar resumen de
	 * transacciones por terminales configuradas en caja
	 */
	public void mostrarResumenTerminales(ActionEvent ev){
	
		try {
			
			String coduser = ((Vautoriz[])m.get("sevAut"))[0].getId().getLogin();
			int codemp = ((Vautoriz[])m.get("sevAut"))[0].getId().getCodreg();
			int caid = ((ArrayList<Vf55ca01>) m.get("lstCajas")).get(0).getId().getCaid();  
			
			rsmTerminales = PosCtrl.crearResumenPorTerminal(caid,coduser,codemp); 
			dwRsmTransactSocketPos.setWindowState("normal");
			
		} catch (Exception e) {	
			e.printStackTrace();
			rsmTerminales = new ArrayList<TransaccionTerminal>();
		}finally{
			if(rsmTerminales == null)
				rsmTerminales = new ArrayList<TransaccionTerminal>();
			m.put("ac_rsmTerminales", rsmTerminales);
			
			gvRsmTransactTerminales.dataBind();
			gvRsmTransactTerminales.setPageIndex(0);
		}
	}
	/**************************************************
	 *  Método: CRPMCAJA / com.casapellas.dao /generarMinutaDeposito
	 *  Descrp: 
	 *	Fecha:  Oct 26, 2012 
	 *  Autor:  CarlosHernandez
	 ***/
	/**
	 * @param md
	 * @param strNombreCaja
	 * @param a
	 * @param iTipoMin
	 * @param sMotivoMn
	 * @param dnetorec
	 * @param vf01
	 * @param strNombreCompania
	 * @return
	 */
	@SuppressWarnings({  "deprecation" })
	public String generarMinutaDeposito(Minutadp md, 
				String strNombreCaja, Arqueo a, int iTipoMin, 
				String sMotivoMn, double dnetorec, 
				Vf0101 vf01, String strNombreCompania, 
				String strContadorCorreo, String strContadorNombre, 
				Date horaini, Date horafin) {
		
		String sRETURN = "";
		boolean bHecho = true;
		String sRsmBAC = "0@0@0.00";
		String sRsmOtr = "0@0@0.00";
		String sCarpeta = "Confirmacion";
		String enviadoa = "";
			
		try {
			HttpServletRequest sHttpRqst  = (HttpServletRequest)FacesContext
								.getCurrentInstance()
								.getExternalContext().getRequest();
		    String sRutaFisica = sHttpRqst.getRealPath(
		    						File.separatorChar + sCarpeta );
		    String sRutaCntx = sHttpRqst.getRealPath( File.separatorChar +"" );
			
		    
			List<String>lstCheques = null;
			BigDecimal bdCheque = new BigDecimal(lblCDC_pagocheques.getValue().toString());
		
			if(bdCheque.compareTo(BigDecimal.ZERO) == 1 ){
				
				String[] lstRsmxBco = new ArqueorecCtrl().obtenerResumenBanco(a, horaini, horafin);
			
				if(lstRsmxBco != null){
					if(lstRsmxBco[0] != null)
						sRsmBAC = lstRsmxBco[0];
					if(lstRsmxBco[1] != null)
						sRsmOtr = lstRsmxBco[1];
				}
				
				lstCheques = new ArqueorecCtrl().obtenerChequesArqueo(a, horaini, horafin);
				
			}
			
			//&& ====== LLenar y crear el documento pdf.
			String sNombreArchivo = new SimpleDateFormat("ddMMyyyy")
									.format(a.getId().getFecha())
								+ "_Mdp" + new DecimalFormat("00000000")
										.format( md.getMindpxcajaNosiguente() )
								+ "_"+new DecimalFormat("00")
										.format(a.getId().getCaid())
								+ a.getId().getCodcomp().trim()
								+ a.getMoneda().trim().toUpperCase()
								+ "_"+a.getCodcajero()
								+".pdf";
			
			String dtaCedula = "000-000000-00000";
			if(vf01.getId().getAbtx2() != null && !vf01.getId().getAbalph().trim().isEmpty() ){
				dtaCedula =  vf01.getId().getAbtx2().trim() ;
			}
			
			String nombreCajero = vf01.getId().getWwgnnm() + " " +vf01.getId().getWwmdnm() + " " + vf01.getId().getWwsrnm() ;
			if(nombreCajero == null || nombreCajero.isEmpty() ){
				nombreCajero =  vf01.getId().getAbalph().trim() ;
			}
			
			GnrMinutaAutomatica mdpPdf = new GnrMinutaAutomatica();
			bHecho = mdpPdf.generarPDF(a, md, 
					(sRutaFisica + File.separatorChar + sNombreArchivo), 
					a.getCoduser(), sRutaCntx, dnetorec, nombreCajero,
					sRsmBAC, sRsmOtr, (ArrayList<String>)lstCheques,
					strNombreCaja, strNombreCompania, dtaCedula );
			
			if(!bHecho){
				if(mdpPdf.getError() == null)
					return "No se puede registrar cierre, " +
							"error al generar minuta de deposito";
				else
					return mdpPdf.getError().toString().split("@")[1] ;
			}
			
			m.put("ac_RutaMinutaPDF", sCarpeta+"@"+sNombreArchivo);
			File minuta = new File(sRutaFisica + File.separatorChar + sNombreArchivo);
			
			CodeUtil.putInSessionMap("ac_FileMinutaDeposito", minuta);
			
			
			//&& ====== Llenar el objeto de bitacora.
			enviadoa = vf01.getId().getWwrem1().trim() 
						+ PropertiesSystem.SPLIT_CHAR 
						+ strContadorCorreo ;
			
			bHecho = ArqueoCajaCtrl.grabarEmisionMinutaDeps(a.getId().getCaid(),
						a.getId().getCodcomp(), a.getCodcajero(),
						Integer.parseInt(a.getReferdep()), a.getId().getFecha(), 
						a.getId().getFecha(), enviadoa, minuta,
						md.getCuenta(), sMotivoMn, a.getDfinal(), 
						a.getMoneda(), iTipoMin, a.getId().getNoarqueo(),
						sNombreArchivo);
			
			if(!bHecho)
				return "Error al grabar bitácora de minuta de depósito";
		
			
		} catch (Exception e) {
			sRETURN = "Error de aplicación al enviar minuta de depósito por correo";
			e.printStackTrace(); 
		}
		return sRETURN;
	}
	
	
	@SuppressWarnings("serial")
	public boolean envioCorreoMinutaDeposito(String strNombreCaja, Arqueo a, File minuta){
		boolean enviado = true;
		
		try {
			
			String sNota = "============================================ <br> " +
					"Adjunto se encuentra minuta de depósito para fecha "
					+ FechasUtil.formatDatetoString(a.getId().getFecha(), "dd/MM/yyyy") ;
 
			sNota += "<br>";
			sNota += "Caja: "+Divisas.ponerCadenaenMayuscula( strNombreCaja );
			sNota += "<br>";
			sNota += "Compañía "+a.getId().getCodcomp();
			sNota += "<br>";
			sNota += "Moneda "+ a.getMoneda();
			sNota += "<br>";
			sNota += "Depósito final "+ String.format("%1$,.2f", a.getDfinal());
			
			
			//&& ===================== Codigos y cuentas de correo para enviar minuta.
			String strSql  = "select * from "+PropertiesSystem.ESQUEMA+".f55ca01 where caid = " + a.getId().getCaid() ;
		
			List<F55ca01>dtaCajas =  (ArrayList<F55ca01>) ConsolidadoDepositosBcoCtrl.executeSqlQuery( strSql, true, F55ca01.class );
			F55ca01 f55 = dtaCajas.get(0);
			
			List<Integer>codigosaEnvioCorreo = new ArrayList<Integer>() ;
			codigosaEnvioCorreo.add(f55.getId().getCaan8() );
			codigosaEnvioCorreo.add(f55.getId().getCacont());
			codigosaEnvioCorreo.add( a.getCodcajero() );
			 
			String codigosIn = codigosaEnvioCorreo.toString().replace("[", "(").replace("]", ")") ;
			strSql = "select * from "+PropertiesSystem.ESQUEMA+".vf0101 where aban8 in " + codigosIn ;
						
			List<Vf0101>dtaEmps =  (ArrayList<Vf0101>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSql, true, Vf0101.class );
			
			List<CustomEmailAddress> toList = new ArrayList<CustomEmailAddress>();
			
			List<String[]>lstCtaCorreo = new ArrayList<String[]>();
			
			for (Vf0101 v : dtaEmps) {
				
				if( !v.getId().getWwrem1().trim().toUpperCase().matches( PropertiesSystem.REGEXP_EMAIL_ADDRESS ) )
					continue;
				
				lstCtaCorreo.add(new String[] { v.getId().getWwrem1().trim().toLowerCase(),	
						Divisas.ponerCadenaenMayuscula(v.getId().getAbalph().trim()) });
				
			}
			
			for (String strEmailAddress : PropertiesSystem.MAILCCS ) {
				lstCtaCorreo.add(strEmailAddress.split(PropertiesSystem.SPLIT_CHAR));
			}
			
			for (String[] sCtaCorreo : lstCtaCorreo) {
				toList.add(new CustomEmailAddress(sCtaCorreo[0].toLowerCase(), sCtaCorreo[1]));
			}
			
			String sSubject = "Minuta depósito "
					+ new SimpleDateFormat("dd/MM/yyyy").format(a.getId().getFecha())+"_"
					+ new DecimalFormat("00").format( a.getId().getCaid() )+ "_"
					+ a.getId().getCodcomp().trim()+ "_"
					+ a.getMoneda();
			
			MailHelper.SendHtmlEmail(
					new CustomEmailAddress(cajaparm.getParametros("34", "0", "WEB_EMAIL_ADRESS").getValorAlfanumerico().toString(), "Módulo de Caja: Notificación de Traslado de Factura"),
					toList, null, new ArrayList<CustomEmailAddress>() { { add(new CustomEmailAddress(cajaparm.getParametros("34", "0", "MAIL_BOUNCEADDRESS").getValorAlfanumerico().toString())); } }, 
					sSubject, "", new String[] { minuta.getAbsolutePath() });
			
			
		} catch (Exception e) {
			enviado = false;
			LogCajaService.CreateLog("envioCorreoMinutaDeposito", "ERR", e.getMessage());
		}
		return enviado;
	}
	
	/* ******************** fin de metodo generarMinutaDeposito ****************************/
	@SuppressWarnings("serial")
	public void enviarArqueoAlCajero(String sCorreoCajero, String sCodcomp, String sCorreoCont, String rutaReporte){
		File flRptFisico = null;
		
		try {

		    //&& ======= Reporte principal. (Datos del arqueo).
			List<ArqueoCajaR> lstAcr = (ArrayList<ArqueoCajaR>)m.get("ac_Lstrptmcaja002");
		    ReportClientDocument rpt = new ReportClientDocument();
		    rpt.open("reportes/rptmcaja002.rpt", OpenReportOptions._openAsReadOnly);
			rpt.getDatabaseController().setDataSource(lstAcr,  ArqueoCajaR.class, "ArqueoCajaR", "ArqueoCajaR");
			
			//&& ======= Reporte del Socket POS.
			List<ArqueoSocketPos> lstSocktPOS = (ArrayList<ArqueoSocketPos>) 
												 m.get("ac_lstCierreSocketPos");
			if(lstSocktPOS == null)lstSocktPOS = new ArrayList<ArqueoSocketPos>(1);
			rpt.getSubreportController()
			 			.getSubreport("cierreSocketPos")
			 			.getDatabaseController()
			 			.setDataSource(lstSocktPOS, ArqueoSocketPos.class,"ArqueoSocketPos","ArqueoSocketPos");
			
			//&& ======= Propiedades del reporte.
			ReportExportControl exportControl = new ReportExportControl();
		    ExportOptions exportOptions = new ExportOptions();
		    exportOptions.setExportFormatType(ReportExportFormat.PDF);

		    PDFExportFormatOptions PDFExpOpts = new PDFExportFormatOptions();
		    PDFExpOpts.setStartPageNumber(1); 
		    PDFExpOpts.setEndPageNumber(100);
		    
		    exportOptions.setFormatOptions(PDFExpOpts);
		    exportControl.setExportOptions(exportOptions);
		    exportControl.setExportAsAttachment(true);
		    exportControl.setReportSource(rpt.getReportSource());
		    
		  //&& ====== Crear el archivo fisico.
		    ArqueoCajaR ar = lstAcr.get(0);
		    Vautoriz vAut[] = (Vautoriz[])m.get("sevAut");
		    
		    HttpServletRequest sHttpRqst  = (HttpServletRequest)FacesContext
											.getCurrentInstance()
											.getExternalContext().getRequest();
			String rutaf = sHttpRqst.getRealPath(File.separatorChar+"Confirmacion"+File.separatorChar);
		    
			String sNomFile = "Arqueo" + ar.getNocaja() + "" + sCodcomp.trim()
					+ ar.getMoneda() + "_"+ new SimpleDateFormat
					("ddMMyyyyHHmmss").format(new Date())+ "_" 
					+ vAut[0].getId().getCodreg() + ".pdf";
			
		    rutaarqueo = sNomFile;
		    m.put("ac_rutaarqueo", rutaarqueo);
		    
		    flRptFisico = new File(rutaf + sNomFile);
		    
		    byte[] data = new byte[1024];
		    OutputStream os = new FileOutputStream(flRptFisico.getAbsolutePath());
		    InputStream is  = new BufferedInputStream(rpt.getPrintOutputController().export(exportOptions));
		    
		    while (is.read(data) > -1) {
                os.write(data);
            }
		    os.close();

		    //&& ========= Guardar el arqueo de caja en base64.
		    Session sesion = null;
			Transaction trans = null;
			boolean newCn = false;
		   
			try {
				
				Date dtfechaHora = (Date)m.get("ac_HoraArqueo");
		    	String parentrowid = ar.getNocaja() +"" +ar.getNoarqueo() 
		    			+ FechasUtil.formatDatetoString(dtfechaHora, "ddMMyyyyHHmmss");
		    	
		    	int idemision = Integer.parseInt( (ar.getNocaja() +""+ ar.getNoarqueo()) ) ;
		    	
				sesion = HibernateUtilPruebaCn.currentSession();
				trans = (newCn = !(sesion.getTransaction().isActive())) ? sesion
						.beginTransaction() : sesion.getTransaction();
				
				String[] strParts = ArqueoCajaCtrl.crearMinutaStringArray(flRptFisico);
				
				if(strParts == null)
					strParts = new String[]{"","",""};
				
				for (int i = 0; i < strParts.length; i++) {
					B64strfile b64StrPart = new B64strfile(strParts[i], i, (int) idemision, 71, parentrowid);
					try {
						sesion.save(b64StrPart);
					} catch (Exception e) {
						e.printStackTrace(); 
					}
				}
				
				//&& =================== Grabar reporte de Excel con los recibos del dia.	
				if( rutaReporte != null && !rutaReporte.trim().isEmpty() ){
				
					File rptRecibos = new File( rutaReporte );
					strParts = ArqueoCajaCtrl.crearMinutaStringArray(rptRecibos);
					
					if(strParts == null)
						strParts = new String[]{"","",""};
					
					for (int i = 0; i < strParts.length; i++) {
						B64strfile b64StrPart = new B64strfile(strParts[i], i, (int) idemision, 74, parentrowid);
						try {
							sesion.save(b64StrPart);
						} catch (Exception e) {
							e.printStackTrace(); 
						}
					}
				}
						
			} catch (Exception e) {
				e.printStackTrace(); 
			}finally{
				if(newCn){
					try {  trans.commit(); } 
					catch (Exception e2) { }
					try {  HibernateUtilPruebaCn.closeSession(sesion); }
					catch (Exception e2) { }
				}
				sesion = null;
				trans = null;
			}
			
			//&& ===============  Enviar el correo con el adjunto.
			String htlm = "<table>";
			htlm += "<tr>";
			htlm += "<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"100%\" >";
			htlm += "<b>SE HA GENERADO EL REPORTE DE ARQUEO #"+ar.getNoarqueo()+"</b></td>";
			htlm += "</tr>";	
			
			htlm += "<tr>";
			htlm += "<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"100%\" >";
			htlm += "<b>CAJA "+ar.getNocaja()+" "+ar.getNombrecaja().toUpperCase()+" </b></td>";
			htlm += "</tr>";
			
			htlm += "<tr>";
			htlm += "<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"100%\" >";
			htlm += "<b>"+ar.getCompania().toUpperCase()+"</b></td>";
			htlm += "</tr>";
			
			htlm += "<tr>";
			htlm += "<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"100%\" >";
			htlm += "<b>MONEDA "+ar.getMoneda().toUpperCase()+"</b></td>";
			htlm += "</tr>";
			
			htlm += "<tr>";
			htlm += "<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"100%\" >";
			htlm += "<b>FECHA "+ar.getFechahora().toUpperCase()+"</b></td>";
			htlm += "</tr>";
			
			htlm += "<tr>";
			htlm += "<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"100%\" >";
			htlm += "<b><br>SALUDOS</b></td>";
			htlm += "</tr>";
			htlm += "</table>";
			
			List<String> attachList = new ArrayList<String>();
			attachList.add(flRptFisico.getAbsolutePath());
			
			if( rutaReporte != null && !rutaReporte.trim().isEmpty() ){
				attachList.add(rutaReporte);
			}
			
			List<String[]>lstCtaCorreo = new ArrayList<String[]>();
			lstCtaCorreo.add(new String[]{ar.getNombrecajero(),sCorreoCajero});
			lstCtaCorreo.add(new String[]{"Contador Caja "+ ar.getNocaja()+", " + ar.getNombrecaja().trim(),sCorreoCont});
			
			List<CustomEmailAddress> toList = new ArrayList<CustomEmailAddress>();
			for (String[] sCtaCorreo : lstCtaCorreo) {
				toList.add(new CustomEmailAddress(sCtaCorreo[1], sCtaCorreo[0]));
			}
			
			String sSubject = "Reporte de arqueo de caja a la fecha:"
					+new SimpleDateFormat("dd MMMM yyyy",new Locale("es","ES"))
					.format(new Date());
			
			MailHelper.SendHtmlEmail(
					new CustomEmailAddress(cajaparm.getParametros("34", "0", "WEB_EMAIL_ADRESS").getValorAlfanumerico().toString(), "Módulo de Caja"),
					toList, null, new ArrayList<CustomEmailAddress>() { { add(new CustomEmailAddress(cajaparm.getParametros("34", "0", "MAIL_BOUNCEADDRESS").getValorAlfanumerico().toString())); } }, 
					sSubject, htlm, attachList.toArray(new String[0]));
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}
	
	public String realizarAjusteMinimo( Session session, Transaction transaction, 
				int caid,String sCodcomp,String sCodsuc, String sMoneda, 
				double dMontoAjustar, double dMontoMinimo, int noarqueo, Date fecha ){
		
		String msgProceso = "" ;
		Divisas dv = new Divisas();
		ReciboCtrl rcCtrl = new ReciboCtrl();
		boolean hecho = true;
		
		try {
			
			Vautoriz vaut = ( (Vautoriz[]) m.get("sevAut") )[0];
			
			Numcaja numcaja = Divisas.obtenerNumeracionCaja("REINTEGRO", caid, sCodcomp, sCodsuc, true, vaut.getId().getLogin(),session );
			if(numcaja == null){
				return msgProceso = "No existe la configuración de números de reintegros para la caja " ;
			}
			
			String sTipoCliente = EmpleadoCtrl.determinarTipoCliente( vaut.getId().getCodreg(), session);
			if(sTipoCliente == null){
				sTipoCliente = "E";
			}
			
			String concepto =  "ReintegroFondo C:" + caid + ", Comp:" + sCodcomp.trim();
			String msgLogs = concepto;
			

			int iNoBatch = Divisas.numeroSiguienteJdeE1(  );
			int iNoDocumento = Divisas.numeroSiguienteJdeE1Custom(valoresJdeNumeracion[8],valoresJdeNumeracion[9] ); 
			
			String[] sCuentaCaja = dv.obtenerCuentaCaja( caid, sCodcomp, MetodosPagoCtrl.EFECTIVO , sMoneda, session , null,null,null);
			if(sCuentaCaja == null){
				return msgProceso="No se ha podido leer la cuenta de caja para el método: Efectivo";				
			}
			String[] sCuentaMinimo = dv.obtenerCuentaFondoMinimo(caid, sCodcomp, sMoneda);
			if(sCuentaMinimo == null){
				return msgProceso = "No se ha podido leer la cuenta de fondo minimo para la moneda: " + sMoneda;				
			}
			
			String scodsuc2 = sCodsuc.trim();
			String tipodoc =  CodigosJDE1.BATCH_CONTADO.codigo() ;
			
			int iMonto = Divisas.pasarAentero(dMontoAjustar);
			hecho = rcCtrl.registrarBatchA92(session, fecha, CodigosJDE1.RECIBOCONTADO, iNoBatch, iMonto, vaut.getId().getLogin(), 1, "ARQUEO-C", CodigosJDE1.BATCH_ESTADO_PENDIENTE );
			
			if(!hecho){
				return msgProceso = "No se ha podido grabar control de batch para reintegro";
			}
			
			hecho = rcCtrl.registrarAsientoDiarioLogs(session, msgLogs, fecha, scodsuc2, 
					tipodoc, iNoDocumento, 1.0,
					iNoBatch, sCuentaCaja[0], sCuentaCaja[1], 
					sCuentaCaja[3], sCuentaCaja[4], sCuentaCaja[5],
					"AA", sMoneda, iMonto, 
					concepto, vaut.getId().getLogin(), vaut.getId().getCodapp(), 
					BigDecimal.ZERO, sTipoCliente,"Ajuste de fondos ",
					sCuentaCaja[2], "", "", sMoneda, sCuentaCaja[2], "D", 0);
			
			if(!hecho){
				return msgProceso = "No se ha podido grabar detalle de batch para reintegro";
			}
			
			hecho = rcCtrl.registrarAsientoDiarioLogs(session, msgLogs, fecha, scodsuc2, 
					tipodoc , iNoDocumento, 2.0,
					iNoBatch, sCuentaMinimo[0],	sCuentaMinimo[1], 
					sCuentaMinimo[3], sCuentaMinimo[4], 
					sCuentaMinimo[5], "AA", sMoneda, iMonto*-1,
					concepto, vaut.getId().getLogin(), vaut.getId().getCodapp(), 
					BigDecimal.ZERO, sTipoCliente, "Ajuste de fondos ",
					sCuentaMinimo[2], "", "", sMoneda, sCuentaMinimo[2], "D", 0);
			
			if(!hecho){
				return msgProceso = "No se ha podido grabar detalle de batch para reintegro";
			}
			
			hecho = rcCtrl.grabarReintegro(session, caid, sCodcomp,
					sCodsuc, numcaja.getNosiguiente(), dMontoAjustar, 
					iNoBatch, iNoDocumento, vaut.getId().getLogin(), noarqueo, sMoneda, fecha);
			
			if(!hecho){
				return msgProceso = "No se ha podido grabar registro de Reintegro para la caja ";
			}
			
			
		} catch (Exception e) {
			e.printStackTrace(); 
			msgProceso = "No se ha podido procesar el comprobante contable por ajuste de fondo mínimo";
		}finally{
			
		}
		return msgProceso;
	}
	
	
	
	
/******************************************************************************************/
	public String realizarAjusteMinimo(
					Connection cn, Session s, Transaction tx,int caid,String sCodcomp,String sCodsuc,
							String sMoneda,double dMontoAjustar,double dMontoMinimo,int noarqueo){
		boolean hecho = true;
		Numcaja numcaja = null;
		Divisas dv = new Divisas();
		String sMensajeError = "";
		int iNoBatch = 0, iNoDocumento = 0;
		String[] sCuentaCaja = null, sCuentaMinimo = null;
		String sConcepto = "", sTipoCliente;
		EmpleadoCtrl emCtrl = new EmpleadoCtrl();
		String sMensaje = "", scodsuc2 = "" ;
		
		try{			
			//--- Datos de la caja.
			Vf55ca01 f55ca01 = (Vf55ca01)((List)m.get("lstCajas")).get(0);
			Vautoriz[] vaut = (Vautoriz[]) m.get("sevAut");
			
			Date dtFecha =  new Date();
			ReciboCtrl rCtrl = new ReciboCtrl();
			//leer el numero de reintegro
			numcaja = dv.obtenerNumeracionCaja("REINTEGRO", caid, sCodcomp, sCodsuc, true,vaut[0].getId().getLogin(),s );
			
			//
			sTipoCliente = emCtrl.determinarTipoCliente(f55ca01.getId().getCacati(), s);
			if(numcaja != null){
				sConcepto = "ca: " + caid + " Ajuste Min: " + numcaja.getNosiguiente();
				//leer el numero de batch
				iNoBatch = dv.leerActualizarNoBatch();
				if(iNoBatch == -1){					
					sMensajeError = "No se ha podido obtener el Número de batch para registro de Reintegro de fondo minimo ";
					return sMensajeError;
				}
				//leer el numero documento
				iNoDocumento = dv.leerActualizarNoDocJDE();
				if(iNoDocumento == -1){
					sMensajeError = "No se ha podido obtener el Número de Documento para el registro de reintegro de fondo minimo ";
					return sMensajeError;
				}
				//leer la cuenta de caja de moneda de arqueo
				//--- Validar la cuenta de caja para el método de pago con sobrante.
				sCuentaCaja = dv.obtenerCuentaCaja(caid,sCodcomp, MetodosPagoCtrl.EFECTIVO , sMoneda,s,null,null,null);
				if(sCuentaCaja==null){
					sMensajeError="No se ha podido leer la cuenta de caja para el método: Efectivo";				
					return sMensajeError;
				}
				//leer la cuenta de fondo minimo por caja
				sCuentaMinimo = dv.obtenerCuentaFondoMinimo(caid, sCodcomp, sMoneda);
				if(sCuentaMinimo == null){
					sMensajeError="No se ha podido leer la cuenta de fondo minimo para la moneda: " + sMoneda;				
					return sMensajeError;
				}
				//
				//--- registrar el encabezado del asiento de batch.
				int iMonto = dv.pasarAentero(dMontoAjustar);
				hecho = rCtrl.registrarBatchA92(cn,"G", iNoBatch,iMonto, vaut[0].getId().getLogin(), 1, MetodosPagoCtrl.DEPOSITO);

				if(!hecho){
					sMensajeError="No se ha podido leer la cuenta de fondo minimo para la moenda: " + sMoneda;				
					return sMensajeError;
				}
				
				scodsuc2 = sCodsuc.trim();
				
				String TipoDoc = cajaparm.getParametros("34", "0", "AJUSTEMINIMO_TIPODOC").getValorAlfanumerico().toString();
				
				hecho = rCtrl.registrarAsientoDiario(dtFecha, cn, scodsuc2, TipoDoc,iNoBatch, 1.0,
						iNoBatch, sCuentaCaja[0], sCuentaCaja[1], 
						sCuentaCaja[3], sCuentaCaja[4], sCuentaCaja[5],
						"AA", sMoneda, iMonto, 
						sConcepto, vaut[0].getId().getLogin(), vaut[0].getId().getCodapp(), 
						BigDecimal.ZERO, sTipoCliente,"Ajuste de minimo CTA CA: "+5+" ",
						sCuentaCaja[2], "", "",sMoneda,sCuentaCaja[2],"D");
				if(hecho){
					hecho = rCtrl.registrarAsientoDiario(dtFecha, cn, scodsuc2, TipoDoc, iNoBatch, 2.0,
								iNoBatch, sCuentaMinimo[0],	sCuentaMinimo[1], 
								sCuentaMinimo[3], sCuentaMinimo[4], 
								sCuentaMinimo[5], "AA", sMoneda,  iMonto*-1,
								sConcepto, vaut[0].getId().getLogin(), vaut[0].getId().getCodapp(), 
								BigDecimal.ZERO, sTipoCliente,"Ajuste de minimo ",
								sCuentaMinimo[2], "", "",sMoneda,sCuentaMinimo[2],"D");
					if(!hecho){
						sMensajeError="No se pudo registrar la linea 2 del batch!!!";				
						return sMensajeError;
					}else{
						hecho = rCtrl.grabarReintegro(s, caid, sCodcomp,
								sCodsuc, numcaja.getNosiguiente(),
								dMontoAjustar, iNoBatch, iNoDocumento, vaut[0]
										.getId().getLogin(), noarqueo, sMoneda, dtFecha );
						if(!hecho){
							sMensajeError="No se pudo registrar el reintegro!!!";				
							return sMensajeError;
						}
					}
				}else{
					sMensajeError="No se pudo registrar la linea 1 del batch!!!";				
					return sMensajeError;
				}
			}else{
				hecho = false;
				sMensajeError = "Error: No se encuentra configurada numeración para reintegro de fondo minimo para caja:" +caid + ", Comp: "+sCodcomp +", Suc: "+sCodsuc;
			}	
			lblValidarArqueo.setValue(sMensajeError);
		}catch(Exception ex){			
			ex.printStackTrace();
		}
		return sMensajeError;
	}
/******************************************************************************************/
/** Método: Mostrar/ocultar los campos para generar un arqueo de día anterior.
 *	Fecha:  25/06/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public void cargarDatosArqueoAnterior(ValueChangeEvent ev){
		SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();		
		try {
			ddlArqueoHFin.dataBind();
			ddlArqueoHini.dataBind();
			ddlArqueoMFin.dataBind();
			ddlArqueoMini.dataBind();
			ddlFiltroMoneda.dataBind();
			ddlFiltroCompania.dataBind();
			dcFechaArqueoAnterior.setValue(new FechasUtil().quitarAgregarDiasFecha(-1, new Date()) );
			lblFechaHoraArqueo.setValue("");
			lblEthoraArqueo.setValue("");
			lbletMsgArqueoPrevio.setStyle("visibility: hidden ");
			lblMsgArqueoPrevio.setStyle("visibility: hidden");
			
			if(chkArqueoDiaAnterior.isChecked()){
				pgrHoraArqueo.setStyle("visibility:visible");
			}else{
				pgrHoraArqueo.setStyle("visibility:hidden");
			}
			srm.addSmartRefreshId(lblFechaHoraArqueo.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblEthoraArqueo.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lbletMsgArqueoPrevio.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblMsgArqueoPrevio.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(ddlFiltroCompania.getClientId(FacesContext.getCurrentInstance()));

			srm.addSmartRefreshId(pgrHoraArqueo.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(ddlFiltroCompania.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(ddlFiltroMoneda.getClientId(FacesContext.getCurrentInstance()));
			
			srm.addSmartRefreshId(ddlArqueoHFin.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(ddlArqueoHini.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(ddlArqueoMFin.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(ddlArqueoMini.getClientId(FacesContext.getCurrentInstance()));
			
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
/******************************************************************************************/
/** Método: Verificar si hubo cambio de tasa en pago de recibos, si hay, enviar correo.
 *	Fecha:  25/06/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public boolean enviarCorreoCambioTasa(int iCaid,Date dtFechaArqueo,List<Arqueo>lstArqueos, Vf55ca01 f5,Vautoriz vaut){
		boolean bValido = true;
		List<Recibo> lstRecibos = new ArrayList<Recibo>();
		StringBuilder sbTablaRecibo = new StringBuilder();
		String sEstiloTD="",sEstiloTdhdr="",sCliente="";
		ArqueoCajaCtrl acCtrl = new ArqueoCajaCtrl();
		String sFrom, sNombreCajero="", sTo; 
		String sCaja, sUbicacion, sCajero, sFecha;
		List<String> sCc = new ArrayList<String>();
		FechasUtil f = new FechasUtil();
		Divisas dv = new Divisas();
		Date dtFechaArqueoIni = null;
		
		try {
			sTo = "creditoycobro@casapellas.com.ni";			
			
			//--- Obtener Dirección URL de la aplicación.
			String sUrl = new Divisas().obtenerURL();
			if(sUrl==null || sUrl.trim().equals("")){
				Aplicacion ap = new Divisas().obtenerAplicacion(vaut.getId().getCodapp());
				sUrl = (ap==null)?"http://ap.casapellas.com.ni:7080/"+PropertiesSystem.CONTEXT_NAME:ap.getUrl().trim();
			}
			//---- Verificar arqueos anteriores.
			if(lstArqueos!=null && lstArqueos.size()>0){
				Arqueo a = (Arqueo)lstArqueos.get(0);
				dtFechaArqueoIni = a.getId().getHora();
			}
			lstRecibos = acCtrl.obtenerRecibosCambioTasa(iCaid, dtFechaArqueo, dtFechaArqueoIni);
			if(lstRecibos!=null && lstRecibos.size()>0){
				
				sEstiloTdhdr += "style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a; ";
				sEstiloTdhdr += " background-color: silver\" align=\"center\" ";
				sEstiloTD += " style=\" font-family: Arial,Helvetica, sans-serif;font-size: 12px;color: #1a1a1a; border-color: silver; ";
				sEstiloTD += " border-style: dashed; border-width: 1px\" ";
				
				sbTablaRecibo.append("<table width=\"460px\" style=\"border-color: silver; border-width: 1px; border-style: solid\">");
				sbTablaRecibo.append("<tr>");
				
				sbTablaRecibo.append("<td "+sEstiloTdhdr+ ">");
				sbTablaRecibo.append("<b>N°</b>");
				sbTablaRecibo.append("</td>");
				
				sbTablaRecibo.append("<td "+sEstiloTdhdr+ ">");
				sbTablaRecibo.append("<b>Cliente</b>");
				sbTablaRecibo.append("</td>");
				
				sbTablaRecibo.append("<td "+sEstiloTdhdr+ ">");
				sbTablaRecibo.append("<b>Tasa</b>");
				sbTablaRecibo.append("</td>");
				
				sbTablaRecibo.append("<td "+sEstiloTdhdr+ ">");
				sbTablaRecibo.append("<b>Batch</b>");
				sbTablaRecibo.append("</td>");
				sbTablaRecibo.append("</tr>");
				
				for (Recibo recibo : lstRecibos) {
					sbTablaRecibo.append("<tr>");
					sbTablaRecibo.append("<td align=\"right\" " +sEstiloTD+ " width=\"15%\" align >");
					sbTablaRecibo.append(recibo.getId().getNumrec());
					sbTablaRecibo.append("</td>");
					
					sCliente = recibo.getCodcli()+" "+ recibo.getCliente().trim();
					sCliente = sCliente.length()>35? sCliente.substring(0,35):sCliente;
					sbTablaRecibo.append("<td align=\"left\" " +sEstiloTD+ ">");
					sbTablaRecibo.append(sCliente.toLowerCase());
					sbTablaRecibo.append("</td>");
					
					sbTablaRecibo.append("<td align=\"right\" " +sEstiloTD+ " width=\"15%\">");
					sbTablaRecibo.append(recibo.getMotivo().trim());
					sbTablaRecibo.append("</td>");
					
					sbTablaRecibo.append("<td align=\"right\" " +sEstiloTD+ " width=\"15%\">");
					sbTablaRecibo.append(recibo.getNobatch().trim());
					sbTablaRecibo.append("</td>");
					sbTablaRecibo.append("</tr>");
				}
				sbTablaRecibo.append("</table>");
				
				//------- Construir el cuerpo del recibo y enviarlo.
				EmpleadoCtrl ec = new EmpleadoCtrl();
				Vf0101 vf01 = ec.buscarEmpleadoxCodigo2(vaut.getId().getCodreg());
				if(vf01!=null){
					sFrom   = vf01.getId().getWwrem1().trim().toUpperCase();
					sNombreCajero = vf01.getId().getAbalph().trim();
					sCajero = vf01.getId().getAban8() +" "+ sNombreCajero;
					
					if(!dv.validarCuentaCorreo(sFrom))
						sTo = "webmaster@casapellas.com.ni";
				}else{
					sFrom   = "webmaster@casapellas.com.ni";
					sNombreCajero =  f5.getId().getCacatinom().trim();
					sCajero = f5.getId().getCaan8() +" "+sNombreCajero;
				}
				
				sCaja = f5.getId().getCaid() +" "+ f5.getId().getCaname().trim(); 
				sUbicacion = f5.getId().getCaco().trim() +" "+ f5.getId().getCaconom().trim();
				sFecha =  f.formatDatetoString(dtFechaArqueo, "dd/MMM/yyyy hh:mm:ss a");
				
				bValido = acCtrl.enviarCorreoCambioTasa(sFrom,sNombreCajero, sTo, sCc, sbTablaRecibo.toString(),
														sUrl,sCaja, sUbicacion, sCajero, sFecha);
			}
		} catch (Exception error) { 
			LogCajaService.CreateLog("envioCorreoMinutaDeposito", "ERR", error.getMessage());
		}
		return bValido;
	}
/*** 23.Mostrar la Ventana para el detalle de las sblidas de caja -------------------------------*/
	public void cargarDetalleSalidas(ActionEvent ev){
		try {
			m.put("", 123);
			if(m.get("ac_egreRecibosSalidas")!=null){
				List lstSalidas = (ArrayList)m.get("ac_egreRecibosSalidas");
				m.put("ac_lstDetalleSalidas", lstSalidas);
				gvDetalleSalidas.dataBind();
				dwDetalleSalidas.setWindowState("normal");
			}
			
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
	
/*** 22. Cargar los datos para el reporte Resumen de arqueo después de haber procesado el arqueo */
	public void mostrarRptResumenArqueo(ActionEvent ev){
 
		try{			
 
			FacesContext.getCurrentInstance().getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/reportes/rptmcaja002.faces");
			
			
		}catch(Exception error){
			error.printStackTrace(); 
		}		
	}
	
/*** 21. Cargar los datos del arqueo para el reporte preliminar ***********************************/
	@SuppressWarnings("deprecation")
	public void cargarDatosRptPreArqueo(ActionEvent ev){
		ArqueoCajaR acr = new ArqueoCajaR();
		Vf55ca01 f55ca01 = (Vf55ca01)((List)m.get("lstCajas")).get(0);
		String nombrecaja = "",sCodcomp,sNombrecomp="";
		FechasUtil f = new FechasUtil();
		Vautoriz vAut[] = (Vautoriz[])m.get("sevAut");
		EmpleadoCtrl ec = new EmpleadoCtrl();
		String sNombreCajero = "";
		SelectItem[] seFiltroCo = null;
		
		try{
			
			m.remove("ac_LstRptArqueo");
			m.remove("ac_NombreArqueo");
			
			//--- Nombre del Cajero.
			Vf0101 vf01 = ec.buscarEmpleadoxCodigo2(vAut[0].getId().getCodreg());
			sNombreCajero = (vf01 == null)? 
								f55ca01.getId().getCacatinom():
								vf01.getId().getAbalph().trim();

			//--- Nombre de la compañía
			sCodcomp = ddlFiltroCompania.getValue().toString();
			seFiltroCo = ddlFiltroCompania.getSelectItems();					
			for (SelectItem seCo : seFiltroCo) {
				if(seCo.getValue().equals(sCodcomp)){
					sNombrecomp = seCo.getLabel().trim().toUpperCase();
					break;
				}
			}
			//--- Demás valores del reporte.
			nombrecaja = f55ca01.getId().getCaname();
			acr.setNocaja(f55ca01.getId().getCaid());
			acr.setNombrecajero(sNombreCajero);
			acr.setNombrecaja(nombrecaja.trim());
			acr.setNombrecaja(f55ca01.getId().getCaname());
			acr.setMoneda(ddlFiltroMoneda.getValue().toString());			
			acr.setCompania(sNombrecomp);
			acr.setSucursal(f55ca01.getId().getCaconom());						

			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
			acr.setFechahora(format.format((Date)m.get("ac_HoraArqueo")));				
			
			acr.setVentastotales(Double.parseDouble(lblVentasTotales.getValue().toString()));
			acr.setDevoluciones(Double.parseDouble(lblTotalDevoluciones.getValue().toString()));
			acr.setVentascredito(Double.parseDouble(lblTotalVtsCredito.getValue().toString()));
			acr.setVentasnetas(Double.parseDouble(lblTotalVentasNetas.getValue().toString()));
			acr.setAbonos(Double.parseDouble(lblTotalAbonos.getValue().toString()));
			acr.setPrimasreservas(Double.parseDouble(lblTotalPrimas.getValue().toString()));
			acr.setIngpagosdifmonedas(Double.parseDouble(lblTotalIngRecxmonex.getValue().toString()));
			acr.setOtrosingresos(Double.parseDouble(lblTotalOtrosIngresos.getValue().toString()));
			acr.setTotalingresos(Double.parseDouble(lblTotaIngresos.getValue().toString()));

			acr.setVentaspagbanco(Double.parseDouble(lblTotalVtsPagBanco.getValue().toString()));
			acr.setAbonospagbanco(Double.parseDouble(lblTotalAbonoPagBanco.getValue().toString()));
			acr.setPrimaspagbanco(Double.parseDouble(lblTotalPrimasPagBanco.getValue().toString()));
			acr.setOtrosegresos(Double.parseDouble(lblTotalOtrosEgresos.getValue().toString()));
			acr.setTotalegresos(Double.parseDouble(lblTotalEgresos.getValue().toString()));
				
			acr.setEfecnetorecibido(Double.parseDouble(lblCDC_efectnetoRec.getValue().toString()));
			acr.setMinimoencaja(Double.parseDouble(lblCDC_montominimo.getValue().toString()));
			acr.setDepositosugerido(Double.parseDouble(lblCDC_depositoSug.getValue().toString()));
			acr.setEfecencaja(Double.parseDouble(txtCDC_efectivoenCaja.getValue().toString()));
			acr.setFaltantesobrante(Double.parseDouble(lblCDC_SobranteFaltante.getValue().toString()));
			acr.setDepositofinal(Double.parseDouble(lblCDC_depositoFinal.getValue().toString()));

			acr.setTefectivo(Double.parseDouble(lblACDetfp_Efectivo.getValue().toString()));
			acr.setTcheque(Double.parseDouble(lblACDetfp_Cheques.getValue().toString()));
			acr.setTtcredito(Double.parseDouble(lblACDetfp_TarCred.getValue().toString()));
			acr.setTtcreditom(Double.parseDouble(lblACDetfp_TCmanual.getValue().toString()));
			acr.setTdepositobanco(Double.parseDouble(lblACDetfp_DepDbanco.getValue().toString()));
			acr.setTtransfbanco(Double.parseDouble(lblACDetfp_TransBanco.getValue().toString()));
			acr.setTformaspago(Double.parseDouble(lblACDetfp_Total.getValue().toString()));
			
			List<ArqueoCajaR> lstAcr = new ArrayList<ArqueoCajaR>();
			lstAcr.add(0,acr);
			m.put("ac_LstRptArqueo", lstAcr);	
			dwConfImprRptpre.setWindowState("hidden");
			
			String sNombre="";
			sNombre += String.valueOf(f55ca01.getId().getCaid());
			sNombre =(sNombre.trim().length()==1)? "0"+sNombre:sNombre;
			sNombre += sCodcomp.trim().toUpperCase();
			sNombre += ddlFiltroMoneda.getValue().toString().trim().toUpperCase();
			sNombre += f.formatDatetoString(new Date(), "ddMMyyyy");
			sNombre += "ResumenArqueoCaja";
			m.put("ac_NombreArqueo", sNombre);
			
			//&& ==== Generar el documento fisico.
			ReportClientDocument rpt = new ReportClientDocument();
		    rpt.open("reportes/rptmcaja001.rpt", OpenReportOptions._openAsReadOnly);
			rpt.getDatabaseController().setDataSource(lstAcr,  ArqueoCajaR.class, "ArqueoCajaR", "ArqueoCajaR");
			
			//&& ======= Propiedades del reporte.
			ReportExportControl exportControl = new ReportExportControl();
		    ExportOptions exportOptions = new ExportOptions();
		    exportOptions.setExportFormatType(ReportExportFormat.PDF);

		    PDFExportFormatOptions PDFExpOpts = new PDFExportFormatOptions();
		    PDFExpOpts.setStartPageNumber(1); 
		    PDFExpOpts.setEndPageNumber(2);
		    
		    exportOptions.setFormatOptions(PDFExpOpts);
		    exportControl.setExportOptions(exportOptions);
		    exportControl.setExportAsAttachment(true);
		    exportControl.setReportSource(rpt.getReportSource());
			
		    //&& ====== Crear el archivo fisico.
		    ArqueoCajaR ar = lstAcr.get(0);
		    
		    HttpServletRequest sHttpRqst  = (HttpServletRequest)FacesContext
											.getCurrentInstance()
											.getExternalContext().getRequest();

			String rutaf = sHttpRqst.getRealPath(File.separatorChar+"Confirmacion"+File.separatorChar);
		    
		    String sNomFile =  "Arqueo"+ar.getNocaja()+""+sCodcomp.trim()+ar.getMoneda()+"_"
		    					+new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date())
		    					+"_"+vAut[0].getId().getCodreg()+".pdf";
		    rutaarqueoprelm = sNomFile;
		    m.put("ac_rutaarqueoprelm", rutaarqueoprelm);
		    
		    File flRptFisico = new File(rutaf + sNomFile);
		    
		    byte[] data = new byte[1024];
		    OutputStream os = new FileOutputStream(flRptFisico.getAbsolutePath());
		    InputStream is  = new BufferedInputStream(
		    		rpt.getPrintOutputController().export(exportOptions));

		    int i = 1;
		    while (is.read(data) > -1) {
		    	i++;
                os.write(data);
            }
		    os.close();
			
			FacesContext fc = FacesContext.getCurrentInstance();
			NavigationHandler nh = fc.getApplication().getNavigationHandler();
			nh.handleNavigation(fc, null, "rptmcaja001");
			
		}catch(Exception error){
			error.printStackTrace();
		}
	}
	
/**** 20. Mostrar la ventana de confirmación de impresión de reporte preliminar de arqueo ***************/
	public void confirmarImpRtpArqueo(ActionEvent ev){
		String sEfCaja,sDepfinal;
		
		try{
			sEfCaja = txtCDC_efectivoenCaja.getValue().toString().trim();
			sDepfinal = lblCDC_depositoFinal.getValue().toString().trim();
						
			if(sEfCaja.matches("^[0-9]+(\\.[0-9]*)?$") && Double.parseDouble(sEfCaja)>0){
				if(Double.parseDouble(sDepfinal)> 0 && !sDepfinal.equals("")){
					dwConfImprRptpre.setWindowState("normal");
				}else{
					lblValidarArqueo.setValue(
					 "<img width=\"7\" src=\"theme/icons/redCircle.jpg\" border=\"0\" /> Primero realice el cálculo del depósito.<br>");
					dwValidarArqueo.setWindowState("normal");
				}				
			}else{
				lblValidarArqueo.setValue(
				 "<img width=\"7\" src=\"theme/icons/redCircle.jpg\" border=\"0\" /> El monto ingresado no es correcto.!<br>");
				dwValidarArqueo.setWindowState("normal");
			}
							
		}catch(Exception error){
			error.printStackTrace();
		}
	}
	
/**** 19. Mostrar los recibos por ingresos y egresos de pagos con monedas distintas del arqueo **********/
	public void cargarRecpagOtrasMonedas(ActionEvent ev){
		String sLinkId = "",sTitulo = "",sLstRecibos = "";
		
		try{
			sLinkId = ev.getComponent().getId();
			
			if(sLinkId.equals("lnkDetIngxRecPagMonEx")){
				sLstRecibos = "ac_ingRecibosxmonex";
				sTitulo = "Ingresos por recibos pagados con diferente moneda";
			}
			else if(sLinkId.equals("lnkDetEgrexRecPagMonEx")){
				sLstRecibos = "ac_egreRecibosxmonex";
				sTitulo = "Egresos por recibos pagados con diferente moneda";
			}
			
			if(m.get(sLstRecibos)!=null  && ((ArrayList)m.get(sLstRecibos)).size()>0){
				m.put("ac_lstDetRecpagMonEx", ((ArrayList)m.get(sLstRecibos)));
				gvDetRecpagMonEx.dataBind();
				gvDetRecpagMonEx.setPageIndex(0);	
				hdDetrecpagmonEx.setCaptionText(sTitulo);
				dwDetallerecpagmonEx.setWindowState("normal");
			}
			
		}catch(Exception error){
			error.printStackTrace();
		}
	}
	
	
/******** 18.1 Guardar la Nota de débito al cajero en concepto de faltante de efectivo en el arqueo ****/	
	public boolean generarNotadeDebito(Connection cn,Session sesCaja,Session sesENS, Transaction transCaja, Transaction transENS,
									   int iNoarqueo,int iCaid,String sCodsuc,String sCodcomp, Date dtFecha, double dMonto,String sMoneda,
									   Vautoriz[] vaut){
		boolean bHecho = true;
		double dTasaOf = 1.0;
		int iNobatchNodoc[]=null,iMonto=0,iMontoDom=0;
		BigDecimal dbValorTjde = BigDecimal.ZERO;
		String sMsjErrorjde="",sCodEmpleado,sTipoCliente,sAsientoSuc="";
		String sCuenta5[]=null, sCuentaFE = "",sCompCtaFE="", sConcepto="",sFecha="",sTasa="1.0";
		
		Tcambio[] tcambio;
		Vf0901 vCtaFE = null;
		Divisas dv = new Divisas();
		FechasUtil f = new FechasUtil();
		ReciboCtrl rcCtrl = new ReciboCtrl();
		EmpleadoCtrl emCtrl = new EmpleadoCtrl();
		TasaCambioCtrl tcCtrl = new TasaCambioCtrl();
		F55ca014 f14 = null;
		
		BigDecimal bdTasa = BigDecimal.ZERO;
		try {
			sConcepto = "Nota Débito x Faltante en Caja";
			sCodEmpleado = dv.rellenarCadena((vaut[0].getId().getCodreg()+"").trim(), "0", 8);
			sTipoCliente = emCtrl.determinarTipoCliente(vaut[0].getId().getCodreg());
			
			f14 = new CompaniaCtrl().obtenerF55ca014xCajaCompania(iCaid, sCodcomp);
			if(f14==null){
				m.put("ac_sMsjErrorJde", "Error al obtener la configuración de la compañía "+sCodcomp+" para la caja "+iCaid);
				return false;
			}
			
			//------------- Obtener la tasa oficial.
			sFecha = f.formatDatetoString(dtFecha,"yyyy-MM-dd");
			tcambio = tcCtrl.obtenerTasaCambioJDExFecha(sFecha);
			if(tcambio == null){
				sMsjErrorjde = "No se encuentra configurada la tasa de cambio jde";
				bHecho = false;
			}else{
				for(int l = 0; l < tcambio.length;l++){
					dbValorTjde = tcambio[l].getId().getCxcrrd();
					sTasa = sTasa + " " + tcambio[l].getId().getCxcrdc() + ": " + dbValorTjde;
				}
				dTasaOf = dbValorTjde.doubleValue();
			}
			
			/** Nota de Cambio:  Se pasa de cuenta Funcionarios y Empleados a Deudores Varios
			 *  Fecha de Cambio: 19/0/2010.
			 *  Cuenta Inicial: 10.15000.15 => cuenta Actual: 24.13600
			 */
			//------------- Verificar que exista la cuenta de Funcionarios y Empleados.
			String sUN =  cajaparm.getParametros("34", "0", "DEUDO_VAR_UNE01").getValorAlfanumerico().toString();
			String sCtaOb=sUN = cajaparm.getParametros("34", "0", "DEUDO_VAR_UNE01").getCodigoCuentaObjeto().toString();
			
			if(sCodcomp.trim().equals("11")){
				sUN = cajaparm.getParametros("34", "0", "DEUDO_VAR_UNE11").getValorAlfanumerico().toString();;
			}else
			if(sCodcomp.trim().equals("90")){
				sUN = cajaparm.getParametros("34", "0", "DEUDO_VAR_UNE08").getValorAlfanumerico().toString();
			}else
			if(sCodcomp.trim().equals("20")){
				sUN = cajaparm.getParametros("34", "0", "DEUDO_VAR_UNE03").getValorAlfanumerico().toString();
			}	
	
			
			vCtaFE  = dv.validarCuentaF0901(sUN,sCtaOb,"");
			if(vCtaFE!=null){
				sCuentaFE = sUN+"."+sCtaOb;
				sCompCtaFE = sUN;
			}else{
				bHecho = false;
				sMsjErrorjde = "No se ha podido obtener la cuenta de Deudores Varios: '"+sUN+"."+sCtaOb+"'";
			}
			
			//------------- Obtener cuenta de caja.
			if(bHecho){
				sCuenta5 = dv.obtenerCuentaCaja(iCaid, sCodcomp, MetodosPagoCtrl.EFECTIVO , sMoneda, sesCaja, transCaja, sesENS, transENS);
				if(sCuenta5!=null){
				
					//---- Asignar la sucursal de la cuenta Deudores varios al asiento diario. 
					sAsientoSuc = sCompCtaFE;
					
					//---- Leer el número de batch y Número de documento a utilizar
					iNobatchNodoc = dv.obtenerNobatchNodoco();
					if(iNobatchNodoc != null){
						iMonto = (int)(dv.roundDouble(dMonto * 100));
						
						String TipoDoc = cajaparm.getParametros("34", "0", "GENENOTADEB_TIPODOC").getValorAlfanumerico().toString();
						String TipoBatch = cajaparm.getParametros("34", "0", "GENENOTADEB_TIPBATCH").getValorAlfanumerico().toString();
						
						//---- Guardar el batch.
						bHecho = rcCtrl.registrarBatchA92(cn, TipoBatch, iNobatchNodoc[0], iMonto, vaut[0].getId().getLogin(), 1, "");
						if(bHecho){
							if(sMoneda.equals(f14.getId().getC4bcrcd())){
								bHecho = rcCtrl.registrarAsientoDiario(dtFecha, cn, sAsientoSuc, TipoDoc, iNobatchNodoc[1], 1.0, iNobatchNodoc[0], sCuentaFE,
											vCtaFE.getId().getGmaid(), vCtaFE.getId().getGmmcu().trim(), vCtaFE.getId().getGmobj().trim(), 
											vCtaFE.getId().getGmsub().trim(), "AA",	sMoneda, iMonto, sConcepto, vaut[0].getId().getLogin(),
											vaut[0].getId().getCodapp(), BigDecimal.ZERO, sTipoCliente,"Débito Cajero Efectivo "+sMoneda,
											sCompCtaFE, sCodEmpleado, "A",sMoneda,sCompCtaFE,"D");
								if(bHecho){
									bHecho = rcCtrl.registrarAsientoDiario(dtFecha, cn,sAsientoSuc, TipoDoc, iNobatchNodoc[1], 2.0, iNobatchNodoc[0], 
												sCuenta5[0], sCuenta5[1], sCuenta5[3], sCuenta5[4], sCuenta5[5], "AA", sMoneda,
												iMonto*-1, sConcepto, vaut[0].getId().getLogin(), vaut[0].getId().getCodapp(),
												BigDecimal.ZERO, sTipoCliente,"Crédito Caja, Efectivo "+sMoneda, sCuenta5[2], 
												"", "",sMoneda,sCompCtaFE,"D");
									if(!bHecho)
										sMsjErrorjde =  "No se pudo registrar el asiento diario, linea 2, para Salida, "+sMoneda+", Caja: "+iCaid+ ", Suc: " + sCodsuc+", Comp: "+sCodcomp;
								}else{
									sMsjErrorjde =  "No se pudo registrar el asiento diario, linea 1, para Salida, "+sMoneda+", Caja: "+iCaid+ ", Suc: " + sCodsuc+", Comp: "+sCodcomp;
								}
							}
							//------- Registro de asientos en Dólares.
							else{ //if(sMoneda.equals("USD")){
								iMontoDom = (int)dv.roundDouble(dMonto*dTasaOf*100);
								bdTasa = new BigDecimal(dTasaOf);
								bHecho = rcCtrl.registrarAsientoDiario(dtFecha, cn, sAsientoSuc,TipoDoc, iNobatchNodoc[1], 1.0, iNobatchNodoc[0], sCuentaFE,
															vCtaFE.getId().getGmaid(), vCtaFE.getId().getGmmcu().trim(), vCtaFE.getId().getGmobj().trim(), 
															vCtaFE.getId().getGmsub().trim(), "AA", sMoneda, iMontoDom, sConcepto, vaut[0].getId().getLogin(), 
															vaut[0].getId().getCodapp(), new BigDecimal(dTasaOf), sTipoCliente,
															"Débito Cajero Efectivo "+sMoneda,sCompCtaFE, sCodEmpleado, "A",f14.getId().getC4bcrcd(),sCompCtaFE,"F");
							if(bHecho){
									bHecho = rcCtrl.registrarAsientoDiario(dtFecha, cn, sAsientoSuc, TipoDoc,iNobatchNodoc[1], 1.0, iNobatchNodoc[0], sCuentaFE,
																vCtaFE.getId().getGmaid(), vCtaFE.getId().getGmmcu().trim(), vCtaFE.getId().getGmobj().trim(),
																vCtaFE.getId().getGmsub().trim(),"CA", sMoneda, iMonto, sConcepto, vaut[0].getId().getLogin(),
																vaut[0].getId().getCodapp(), BigDecimal.ZERO, sTipoCliente,"Débito Cajero Efectivo "+sMoneda,
																sCompCtaFE, sCodEmpleado, "A",sMoneda,sCompCtaFE,"F");
									if(bHecho){
										bHecho = rcCtrl.registrarAsientoDiario(dtFecha, cn,sAsientoSuc, TipoDoc, iNobatchNodoc[1], 2.0, iNobatchNodoc[0], sCuenta5[0],
																	sCuenta5[1], sCuenta5[3], sCuenta5[4], sCuenta5[5], "AA", sMoneda, iMontoDom*-1,
																	sConcepto, vaut[0].getId().getLogin(), vaut[0].getId().getCodapp(), new BigDecimal(dTasaOf),
																	sTipoCliente,"Crédito Caja,Efectivo "+sMoneda, sCuenta5[2],sCodEmpleado, "A",f14.getId().getC4bcrcd(), sCompCtaFE, "F");
										if(bHecho){
											bHecho = rcCtrl.registrarAsientoDiario(dtFecha, cn,sAsientoSuc, TipoDoc, iNobatchNodoc[1], 2.0, iNobatchNodoc[0], sCuenta5[0],
																		sCuenta5[1], sCuenta5[3], sCuenta5[4], sCuenta5[5], "CA", sMoneda, iMonto*-1, 
																		sConcepto, vaut[0].getId().getLogin(), vaut[0].getId().getCodapp(), BigDecimal.ZERO, 
																		sTipoCliente,"Crédito Salida,Efectivo "+sMoneda, sCuenta5[2], sCodEmpleado, "A",sMoneda, sCompCtaFE, "F");
											if(!bHecho){
												sMsjErrorjde =  "No se pudo registrar el asiento diario, linea 2 CA, para Nota de Débito, "+sMoneda+", Caja: "+iCaid+ ", Suc: " + sCodsuc+", Comp: "+sCodcomp;
											}
										}else{
											sMsjErrorjde =  "No se pudo registrar el asiento diario, linea 2 AA, para Nota de Débito, "+sMoneda+", Caja: "+iCaid+ ", Suc: " + sCodsuc+", Comp: "+sCodcomp;
										}
									}else{
										sMsjErrorjde =  "No se pudo registrar el asiento diario, linea 1 CA, para Nota de Débito, "+sMoneda+", Caja: "+iCaid+ ", Suc: " + sCodsuc+", Comp: "+sCodcomp;
									}
								}else{
									sMsjErrorjde =  "No se pudo registrar el asiento diario, linea 1 AA, para Nota de Débito, "+sMoneda+", Caja: "+iCaid+ ", Suc: " + sCodsuc+", Comp: "+sCodcomp;
								}
							}
						}else{
							sMsjErrorjde = "Error al guardar el batch"+ iNobatchNodoc[0]+" "+sMoneda+", Caja: "+iCaid+ ", Suc: " + sCodsuc+", Comp: "+sCodcomp;
						}
					}else{
						bHecho = false;
						sMsjErrorjde = "Error al obtener el número de batch y documento para generar Nota de débito al cajero";
					}
				}else{
					bHecho = false;
					sMsjErrorjde = "Error al obtener la cuenta de caja para Efectivo "+sMoneda+", Caja: "+iCaid+ ", Suc: " + sCodsuc+", Comp: "+sCodcomp;
				}
			}
			//----- Guardar enlace con ReciboJde. 
			if(bHecho){
				bHecho = rcCtrl.fillEnlaceMcajaJde(sesCaja, transCaja, iNoarqueo, sCodcomp, iNobatchNodoc[1], iNobatchNodoc[0], iCaid, sCodsuc, "D", "F");
				if(bHecho){
					//---- Guardar registro del depósito correspondiente.
					/**
					 * 06/05/2010
					 * Cambio: Leer el número de depósito desde NUMCAJA, el número de arqueo
					 * 		 : se asigna al número de referencia del depósito.
					 */
					Numcaja n = dv.obtenerNumeracionCaja("NDEPOSITO", iCaid, sCodcomp, sCodsuc, true, vaut[0].getId().getLogin(),sesCaja);
					if(n!=null){
					
						if(!bHecho){
							sMsjErrorjde = "Error al guardar enlace "+PropertiesSystem.CONTEXT_NAME+" - Depósito para registro de nota de Débito con batch"+iNobatchNodoc[0]+" NoDoc. "+iNobatchNodoc[1]+", Caja: "+iCaid+ ", Suc: " + sCodsuc+", Comp: "+sCodcomp;
						}
					}else{
						bHecho = false;
						sMsjErrorjde = "Error al obtener la númeración de Depósito para registro de nota de Débito con batch"+iNobatchNodoc[0]+" NoDoc. "+iNobatchNodoc[1]+", Caja: "+iCaid+ ", Suc: " + sCodsuc+", Comp: "+sCodcomp;
					}
				}else{
					sMsjErrorjde = "Error al guardar enlace "+PropertiesSystem.CONTEXT_NAME+" - JDE para registro de nota de Débito con batch"+iNobatchNodoc[0]+" NoDoc. "+iNobatchNodoc[1]+", Caja: "+iCaid+ ", Suc: " + sCodsuc+", Comp: "+sCodcomp;
				}
			}
			 
			//----- Poner al mapa el mensaje de error en caso de existir.
			if(!bHecho)
				m.put("ac_sMsjErrorJde", sMsjErrorjde);
			
		} catch (Exception error) {
			sMsjErrorjde = "Error al Generar Nota de débito: Error de sistema: <br> " +error;
			m.put("ac_sMsjErrorJde", sMsjErrorjde);
			error.printStackTrace();
		}
		return bHecho;
	}

/******** 18. Procesar y guardar los datos del arqueo de Caja ******************************************/
	@SuppressWarnings({ "static-access", "rawtypes" })
	public void ProcesarArqueoCaja(ActionEvent ev){
		
		int codcajero= 0,caid = 0;
		int noarqueo = 0;
		String moneda = new String();
		String coduser = new String();
		String codsuc = new String();
		String codcomp = new String();
		String sReferDep = new String();
		Date fecha = new Date();
		double dMontoAjustar = 0;
		BigDecimal dfinal = BigDecimal.ZERO;
		
		BigDecimal tingreso,tegresos,netorec,minimo,dsugerido,efectcaja,sf,tpagos;	
		
		double dingreso,degresos,dnetorec,dminimo,ddsugerido,defectcaja,dsf,ddfinal,dtpagos;
		List lstCajas;
		Vautoriz[] vAut = null;
		Date dtHoraArprevio = null;
		ArqueoCajaCtrl acCtrl = new ArqueoCajaCtrl();
		Divisas dv = new Divisas();
		Vf55ca01 vf55ca01 = null;
		List<Arqueo> lstArqueos = null;
		String sMensajeError="";

		Session sesion = null;
		Transaction trans = null;
		
		boolean bHecho = true;
		Vf0101 vf01 = null;
		boolean cnDonaciones = false;
		Minutadp mindp = null;
		Arqueo arqueo = null;
		
		
		try{
			
			LogCajaService.CreateLog("ProcesarArqueoCaja", "INFO", "ProcesarArqueoCaja - INICIO");
			HttpServletRequest r = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
			
			dwConfirmarProcesarArqueo.setWindowState("hidden");
			m.remove("ac_Lstrptmcaja002");
		 
			lstCajas = (List)m.get("lstCajas");
			vf55ca01 = ((Vf55ca01)lstCajas.get(0));
			caid = vf55ca01.getId().getCaid();
			codsuc  = CodeUtil.pad(ddlFiltroCompania.getValue().toString().trim(), 5 , "0"); 
			codcomp = ddlFiltroCompania.getValue().toString();
			sReferDep = txtCDC_ReferDeposito.getValue().toString().trim().toUpperCase();
											 
			vAut = (Vautoriz[])m.get("sevAut");
			coduser = vAut[0].getId().getLogin();
			codcajero = vAut[0].getId().getCodreg();
			
			moneda = ddlFiltroMoneda.getValue().toString();
			fecha = (Date)m.get("ac_HoraArqueo");
						
			//&& =========================== El numero de minuta ya fue asignado al cargar los datos del arqueo.
			sReferDep = txtCDC_ReferDeposito.getValue().toString().trim().toUpperCase(); 
			
			//----- Obtener fecha del arqueo Anterior.
			if(m.get("ac_ArqueoPrevio")!=null){
				Arqueo arprev = (Arqueo)m.get("ac_ArqueoPrevio");
				dtHoraArprevio = arprev.getId().getHora();
			}
			//&& === Capturar la hora de inicio para arqueo de dias anteriores.
			if(chkArqueoDiaAnterior.isChecked())
				dtHoraArprevio = (Date)m.get("ac_HoraInicioDant");
			
			//----- Datos del arqueo.
			dingreso = dv.roundDouble(Double.parseDouble(lblTotaIngresos.getValue().toString()));
			degresos = dv.roundDouble(Double.parseDouble(lblTotalEgresos.getValue().toString()));
			dnetorec = dv.roundDouble(Double.parseDouble(lblCDC_efectnetoRec.getValue().toString()));
			dminimo  = dv.roundDouble(Double.parseDouble(lblCDC_montominimo.getValue().toString()));
			ddsugerido = dv.roundDouble(Double.parseDouble(lblCDC_depositoSug.getValue().toString()));
			defectcaja = dv.roundDouble(Double.parseDouble(txtCDC_efectivoenCaja.getValue().toString()));
			dsf = dv.roundDouble(Double.parseDouble(lblCDC_SobranteFaltante.getValue().toString()));
			ddfinal = dv.roundDouble(Double.parseDouble(lblCDC_depositoFinal.getValue().toString()));
			dtpagos = dv.roundDouble(Double.parseDouble(lblACDetfp_Total.getValue().toString()));
							
			tingreso  = BigDecimal.valueOf(dingreso);				 
			tegresos  = BigDecimal.valueOf(degresos);
			netorec   = BigDecimal.valueOf(dnetorec);
			minimo 	  =	BigDecimal.valueOf(dminimo);
			dsugerido = BigDecimal.valueOf(ddsugerido);
			efectcaja = BigDecimal.valueOf(defectcaja);
			sf 		  =	BigDecimal.valueOf(dsf);
			dfinal    =	BigDecimal.valueOf(ddfinal);
			tpagos    =	BigDecimal.valueOf(dtpagos);
			
			//--------- Consultar la lista de arqueos del día.
			List<Hfactura> lstFact = (ArrayList)m.get("arqueoFacturasDia");
			lstArqueos  = acCtrl.obtenerArqueosCaja(caid, fecha);
			Numcaja numcaja = null; 
			ArqueorecCtrl arCtrl = new ArqueorecCtrl();
			ArqueofacCtrl afCtrl = new ArqueofacCtrl();
			
			
			sesion = HibernateUtilPruebaCn.currentSession();
			trans =  (!sesion.getTransaction().isActive()) ? sesion
					.beginTransaction() : sesion.getTransaction();
			 
			
			//&& ============= Obtener la lista de recibos a incluir en el cierre.
			List<Object[]>lstRecibosCierre = arCtrl.obtenerRecibosEnCierre(
												caid, codcomp, fecha, 
												dtHoraArprevio, moneda);
			
			
			//&& ============= Validar que no se haya grabado ya el cierre.
			bHecho = acCtrl.verificarExisteCierre(lstRecibosCierre, caid, 
									codcomp, moneda, codcajero, fecha, ddsugerido,
									dingreso, degresos, ddfinal, defectcaja);
			if(bHecho){
				sMensajeError = "Error: El cierre para caja " + caid +" Comp: "
								+codcomp +" ya ha sido procesado ";
				bHecho = false;
				return;
			}
			
			//&& ============= Obtener numeraciones y continuar con la grabacion
			numcaja = Divisas.obtenerNumeracionCaja("NARQUEO", caid, codcomp, codsuc, true, coduser,sesion);
			if(numcaja == null){
				bHecho = false;
				sMensajeError = "Error: No se encuentra configurada " +
							" numeración de arqueo para caja:" + caid 
							+ ", Comp: "+codcomp ;
				return;
			}
			
			noarqueo = numcaja.getNosiguiente();
			bHecho   = acCtrl.guardarArqueoCaja(noarqueo, codcajero, caid, 
						3, moneda, coduser,sReferDep, codcomp, codsuc,fecha, 
						fecha, fecha, fecha, tingreso, tegresos, netorec, 
						minimo, dsugerido, efectcaja, sf, dfinal,
						 tpagos,sesion,trans);
			if(!bHecho){
				bHecho = false;
				sMensajeError = "Error al guardar Maestro de arqueo de caja";
				return;
			}
			//&& ======= Facturas del cierre
 			if( lstFact != null && !lstFact.isEmpty() ){
				 
				bHecho = afCtrl.guardarFacturasArqueo(caid, codcomp, codsuc,
									noarqueo, fecha, lstFact,sesion,trans);
				if(!bHecho){
					bHecho = false;
					sMensajeError = "Error al guardar el registro de las "+
									"facturas incluidas en el arqueo";
					return;
				}
			}
			//&& ======= Recibos del cierre.
			bHecho = arCtrl.guardarRecibosArqueo(caid, codcomp, codsuc,
								noarqueo, fecha, moneda, dtHoraArprevio,
								sesion,trans,lstRecibosCierre);
			if(!bHecho){
				bHecho = false;
				sMensajeError = "Error al guardar el registro de los "+
								"recibos incluidos en el arqueo";
				return;
			}
			
			//&& ======= Salidas de caja.
			bHecho = arCtrl.guardarSalidasArc(caid, codcomp, codsuc, noarqueo,
							fecha, moneda, dtHoraArprevio,sesion,trans);
			if(!bHecho){
				bHecho = false;
				sMensajeError = "Error al guardar las salidas de caja "+
								" registradas para el cierre";
				return;
			}
																												
			//&& ======= Aplicaciones del fondo minimo.m.
			if(  m.get("AjustaMinimo") != null && 	m.get("AjustaMinimo").toString().equals("0")){	
				
				dMontoAjustar = Double.parseDouble(lblCDC_montoMinReint.getValue().toString());
				double dMontoMinimo = Double.parseDouble(lblCDC_montoMinAjust.getValue().toString());
				double dMontomin =  Double.parseDouble(lblCDC_montominimo.getValue().toString());
				
				sMensajeError = realizarAjusteMinimo(sesion, trans, caid,
									codcomp,codsuc,moneda,dMontoAjustar,
									dMontoMinimo, noarqueo , fecha);
				
				if(sMensajeError.trim().compareTo("") != 0){
					bHecho = false;
					return;
				}
				
				dMontomin = dMontomin - Math.abs( dMontoAjustar ) ;	
				bHecho = new CtrlCajas().actualizarMontoMinimo(caid, codcomp, moneda, dMontomin,sesion);
				
				if(!bHecho){
					sMensajeError = "No se ha podido completar la "+
									"operación: Error de sistema: "+
									"No se ha podido actualizar el "+
									"monto mínimo de la caja";
					return;
				}
			}
			//&& ================ (I) Cierre de Transacciones por donaciones.
			
			cnDonaciones = CodeUtil.getFromSessionMap("ac_lstDonacionesArqueoCaja") != null &&  
					!((ArrayList<DncDonacion>)CodeUtil.getFromSessionMap("ac_lstDonacionesArqueoCaja")).isEmpty() ;
				 
			if( bHecho && cnDonaciones ) {
				bHecho = procesarDonacionesArqueoCaja(caid, codcomp, codcajero, noarqueo, fecha) ;
				sMensajeError = DonacionesCtrl.getMsgProceso() ;
			}
			
			//&& ================ (F)
			
			//&& ================ Minuta de Depositos.
			vf01 = EmpleadoCtrl.buscarEmpleadoxCodigo2(codcajero);

			if( bHecho && CodeUtil.getFromSessionMap("ac_MinutaDeposito") != null ){
				
				F55ca014 f14 = (F55ca014)CodeUtil.getFromSessionMap( "ac_DatosCompaniaArqueo" );
				
				mindp = (Minutadp) CodeUtil.getFromSessionMap("ac_MinutaDeposito");
				mindp.setMindpxcajaNosiguente( mindp.getNumerominutamanual() );
				
				if(mindp.getNumerominutamanual() == 0 ){
					mindp = ArqueoCajaCtrl.obtenerUltimaMinuta( caid, codcomp, moneda, coduser, false );
					bHecho = (mindp != null);
				}
				
				if(!bHecho){
					sMensajeError = "Cierre no procesado: Configuración para minuta de depósitos no encontrada ";
					return;
				}
				 
				Date horainicio = (Date)CodeUtil.getFromSessionMap("acHora_Inicio");
				Date horafinal  = (Date)CodeUtil.getFromSessionMap("acHora_Finaliza"); 
				arqueo = (Arqueo)m.get("ac_ArqueoActual");
				
				sMensajeError = generarMinutaDeposito(mindp,  vf55ca01.getId().getCaname(), 
						arqueo , 1,  "",  dnetorec,  vf01,  f14.getId().getC4rp01d1().trim(), 
						vf55ca01.getId().getCacontmail().trim(), vf55ca01.getId().getCacontnom(), horainicio, horafinal );
				
				bHecho = true; 
				
				CodeUtil.removeFromSessionMap(new String[]{"ac_MinutaDeposito","ac_ArqueoActual","ac_DatosCompaniaArqueo"});
				
				if( !sMensajeError.isEmpty() && !sMensajeError.trim().contains("@")){
					bHecho = false;
					return;
				}
			}
		
		}catch(Exception error){
			LogCajaService.CreateLog("ProcesarArqueoCaja", "ERR", error.getMessage());
			bHecho = false; 
			sMensajeError = "Cierre de Caja no puede aplicarse"; 
		}finally{
			
			try{
				boolean bCommit = true;
				
				dwCargando.setWindowState("hidden");
				
				//&& =========== Finalizar las sessiones y transacciones.
				
				if(bHecho){
					try{
						trans.commit();
					}catch(Exception ex){
						bCommit = false;
						bHecho = false;
						LogCajaService.CreateLog("ProcesarArqueoCaja", "ERR", ex.getMessage());
					}
				}else{
					try{ trans.rollback(); }
					catch(Exception e){LogCajaService.CreateLog("ProcesarArqueoCaja", "ERR", e.getMessage());}
				}
				
				try {
					HibernateUtilPruebaCn.closeSession(sesion);
				} catch (Exception e) {
					LogCajaService.CreateLog("ProcesarArqueoCaja", "ERR", e.getMessage());
				}
				
				
				
				
				//&& =========== Continuar con el envio de correos y notificacion.
				if(bHecho){
					generarReporteArqueo(vf55ca01, vf01, moneda, codcomp, noarqueo);
					
					enviarCorreoCambioTasa(caid, fecha,lstArqueos,vf55ca01,vAut[0]);
				 	
					correoaContador(vf55ca01,codcomp, moneda, vf01, caid, noarqueo,
									dfinal, fecha, dMontoAjustar);
					
					
					Date horaInicia = new Date();
					Date horaFinaliza = new Date();
					try {
						  horaInicia = (Date)CodeUtil.getFromSessionMap("ac_HoraArqueoInicia");
						  horaFinaliza = (Date)CodeUtil.getFromSessionMap("ac_HoraArqueoTermina");
					} catch (Exception e) {
						 e.printStackTrace();
					}
					
					String rutaReporte = generarReporteRecibosArqueo(noarqueo, caid, codcomp, fecha, horaInicia, horaFinaliza );
					
				 	enviarArqueoAlCajero(vf01.getId().getWwrem1().trim(), codcomp,
									vf55ca01.getId().getCacontmail().trim(), rutaReporte);
					
					
					if(mindp != null) {
						File minuta = (File)CodeUtil.getFromSessionMap("ac_FileMinutaDeposito");
						envioCorreoMinutaDeposito(vf55ca01.getId().getCaname(), arqueo, minuta) ;
					}
					
					if(cnDonaciones){
						List<DncDonacion> donacionesCaja = (ArrayList<DncDonacion>)CodeUtil.getFromSessionMap("ac_lstDonacionesArqueoCaja") ;
						enviarCorreoNotificacionDonaciones(donacionesCaja, caid , codcajero );
					}
				
					m.remove("lstFiltroComp");
					m.remove("lstFiltroMoneda");
					ddlFiltroCompania.dataBind();
					ddlFiltroMoneda.dataBind();
					limpiarObj();
					
					//&& ========== cambio temporal =================//
					if(vAut[0].getId().getLogin().trim().toLowerCase().compareTo("") == 0){
						lblValidarArqueo.setValue("Cierre aplicado");
						dwValidarArqueo.setWindowState("normal");
					}else{
						dwMsgProcesarArq.setWindowState("normal");
					}
					
				}else{
					lblValidarArqueo.setValue(sMensajeError);
					dwValidarArqueo.setWindowState("normal");
				}
			 
			}catch(Exception e){
				lblValidarArqueo.setValue("Error de Sistema, cierre no aplicado.");
				dwValidarArqueo.setWindowState("normal");
				e.printStackTrace();
			
			}finally{
				
				CodeUtil.removeFromSessionMap(new String[]{"AjustaMinimo", "ac_HoraArqueoInicia", "ac_HoraArqueoTermina"});
				
			}
			
		}
	}
	
	
	public static String generarReporteRecibosArqueo(int noarqueo, int caid, String codcomp, Date fecha, Date horaIni, Date HoraFin){
		String rutaReporte = "";
		
		try {
			
			//&& ======= Validacion de fecha =============
			
			String strHoraIni = horaIni != null?  "00:00:00" : new SimpleDateFormat("HH:mm:ss").format(horaIni);
			String strHoraFin = new SimpleDateFormat("HH:mm:ss").format( (HoraFin  == null ? new Date() : HoraFin ) );
			
			String rutaf = cajaparm.getParametros("34", "0", "RUTA_DOC_EXPORTAR").getValorAlfanumerico().toString();//PropertiesSystem.RUTA_DOCUMENTOS_EXPORTAR ;
			String nombrereporte = "RecibosCaja_"+caid+"_Arqueo_" + noarqueo+"_"+new SimpleDateFormat("ddMMyyyyHHmmss").format(fecha)+".xlsx";
			
			rutaReporte = rutaf + nombrereporte;
			
			String strSqlQuery = "select * from @BDCAJA.Vreporterecibos " +
					"where caid = @CAID and trim(codcomp) = '@CODCOMP' and fecha = '@FECHA' " +
					" and hora between '@HINI' and '@HFIN' order by numrec  " ;
			
			strSqlQuery = strSqlQuery
					.replace("@BDCAJA", PropertiesSystem.ESQUEMA)
					.replace("@CAID", String.valueOf(caid))
					.replace("@CODCOMP",codcomp.trim() )
					.replace("@FECHA", new SimpleDateFormat("yyyy-MM-dd").format(fecha))
					.replace("@HINI",  strHoraIni )
					.replace("@HFIN",  strHoraFin);
			
			List<Vreporterecibos> recibosRpt = (ArrayList<Vreporterecibos>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSqlQuery, true, Vreporterecibos.class);
			
			Rptmcaja014_RecibosCaja rpt = new Rptmcaja014_RecibosCaja();
			rpt.agregarfirmaContador = true;
			rpt.recibosCaja = recibosRpt;
			rpt.rutafisica = rutaReporte ;
			
			rpt.generarReporteRecibos();
			
			if(rpt.getMensajeProceso() != null && !rpt.getMensajeProceso().trim().isEmpty()){
				return rutaReporte = "";
			}
			
			
		} catch (Exception e) {
			LogCajaService.CreateLog("generarReporteRecibosArqueo", "ERR", e.getMessage());
			rutaReporte = "" ;
		}
		return rutaReporte; 
	}
	
	//&& ========= Armar el reporte en pdf que se muestra al final.
	public void generarReporteArqueo(Vf55ca01 vf55ca01, Vf0101 vf01, 
								String moneda, String codcomp, int noarqueo ){
								
		m.remove("ac_lstCierreSocketPos");
		m.remove("ac_Lstrptmcaja002");
		Divisas dv = new Divisas();
		
		try{
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
			String sCajero = vf01.getId().getAbalph().trim(); 
			String sCaja = vf55ca01.getId().getCaname();
			String sUbicacion = "";
			String  querySucursal = "select CONCAT(CASUCUR,CONCAT(  ' ', IFNULL ( (SELECT DRDL01 FROM PRODCTL920.F0005 SUC WHERE DRSY = '00' AND DRRT = '11' AND TRIM(DRKY) = TRIM(a.CASUCUR) LIMIT 1), '' )))   from "+PropertiesSystem.ESQUEMA+".vf55ca01 a " + 
					"where caid="+vf55ca01.getId().getCaid();
			List<Object> sucursalObj= ConsolidadoDepositosBcoCtrl.executeSqlQuery(querySucursal, null, true);
			
			if(sucursalObj != null  && sucursalObj.size()>0) {
				sUbicacion= sucursalObj.get(0).toString();
			}
			
				
				
			sCaja   = Divisas.ponerCadenaenMayuscula(sCaja); 
			sCajero = Divisas.ponerCadenaenMayuscula(sCajero); 
			sUbicacion = Divisas.ponerCadenaenMayuscula(sUbicacion);
			String sNombrecomp = new CompaniaCtrl().obtenerNombreComp(codcomp, vf55ca01.getId().getCaid(), null, null);
			if(moneda.equals("COR"))
					moneda = "Córdobas";
			if(moneda.equals("USD"))
				moneda = "Dólares";
						
			ArqueoCajaR rptmcaja = new ArqueoCajaR();
			rptmcaja.setCompania(sNombrecomp);
			rptmcaja.setNoarqueo(noarqueo);
			rptmcaja.setFechahora(format.format((Date)m.get("ac_HoraArqueo")));
			rptmcaja.setNocaja( vf55ca01.getId().getCaid() );
			rptmcaja.setNombrecaja(sCaja);
			rptmcaja.setNombrecajero(sCajero);
			rptmcaja.setMoneda(moneda);
			rptmcaja.setSucursal(sUbicacion);
			
			rptmcaja.setVentastotales(Double.parseDouble(lblVentasTotales.getValue().toString()));
			rptmcaja.setDevoluciones(Double.parseDouble(lblTotalDevoluciones.getValue().toString()));
			rptmcaja.setVentascredito(Double.parseDouble(lblTotalVtsCredito.getValue().toString()));
			rptmcaja.setVentasnetas(Double.parseDouble(lblTotalVentasNetas.getValue().toString()));
			rptmcaja.setAbonos(Double.parseDouble(lblTotalAbonos.getValue().toString()));
			rptmcaja.setPrimasreservas(Double.parseDouble(lblTotalPrimas.getValue().toString()));
			rptmcaja.setIngpagosdifmonedas(Double.parseDouble(lblTotalIngRecxmonex.getValue().toString()));
			rptmcaja.setOtrosingresos(Double.parseDouble(lblTotalOtrosIngresos.getValue().toString()));
			rptmcaja.setTotalingresos(Double.parseDouble(lblTotaIngresos.getValue().toString()));

			rptmcaja.setVentaspagbanco(Double.parseDouble(lblTotalVtsPagBanco.getValue().toString()));
			rptmcaja.setAbonospagbanco(Double.parseDouble(lblTotalAbonoPagBanco.getValue().toString()));
			rptmcaja.setPrimaspagbanco(Double.parseDouble(lblTotalPrimasPagBanco.getValue().toString()));
			rptmcaja.setOtrosegresos(Double.parseDouble(lblTotalOtrosEgresos.getValue().toString()));
			rptmcaja.setTotalegresos(Double.parseDouble(lblTotalEgresos.getValue().toString()));
			rptmcaja.setEfecnetorecibido(Double.parseDouble(lblCDC_efectnetoRec.getValue().toString()));

			rptmcaja.setMinimoencaja(Double.parseDouble(lblCDC_montominimo.getValue().toString()));					
					
			String sReint = lblCDC_montoMinReint.getValue().toString();
			sReint = sReint.replace(",", "");
			BigDecimal bdReint = new BigDecimal(sReint);
			
			if(bdReint.compareTo(BigDecimal.ZERO) == 1){
				BigDecimal bdMinim = new BigDecimal(lblCDC_montominimo.getValue().toString().replace("", ""));
				bdMinim = bdMinim.subtract(bdReint) ;
				rptmcaja.setMinimoencaja( bdMinim.doubleValue() );
			}
			
			rptmcaja.setDepositosugerido(Double.parseDouble(lblCDC_depositoSug.getValue().toString()));
			rptmcaja.setEfecencaja(Double.parseDouble(txtCDC_efectivoenCaja.getValue().toString()));
			rptmcaja.setFaltantesobrante(Double.parseDouble(lblCDC_SobranteFaltante.getValue().toString()));
			rptmcaja.setDepositofinal(Double.parseDouble(lblCDC_depositoFinal.getValue().toString()));

			rptmcaja.setTefectivo(Double.parseDouble(lblACDetfp_Efectivo.getValue().toString()));
			rptmcaja.setTcheque(Double.parseDouble(lblACDetfp_Cheques.getValue().toString()));
			rptmcaja.setTtcredito(Double.parseDouble(lblACDetfp_TarCred.getValue().toString()));
			rptmcaja.setTtcreditom(Double.parseDouble(lblACDetfp_TCmanual.getValue().toString()));
			rptmcaja.setTtcreditosktpos(Double.parseDouble(lblACDetfp_TCSocketpos.getValue().toString()));
			
			rptmcaja.setTdepositobanco(Double.parseDouble(lblACDetfp_DepDbanco.getValue().toString()));
			rptmcaja.setTtransfbanco(Double.parseDouble(lblACDetfp_TransBanco.getValue().toString()));
			rptmcaja.setTformaspago(Double.parseDouble(lblACDetfp_Total.getValue().toString()));
			
			//---- Nuevos Campos del reporte.
			rptmcaja.setSPagoIngresosExt(lblTotalOtrosIngresos.getValue().toString());
			rptmcaja.setSPagoFinanciamiento(lblTotalFinanciamiento.getValue().toString());
			rptmcaja.setSCambioOtraMoneda(lblCambiosOtraMon.getValue().toString());
			rptmcaja.setSFinanciamientosBanco(lblTotalFinanPagBanco.getValue().toString());
			rptmcaja.setSIngresosExtBanco(lblegTotalIex.getValue().toString());
			rptmcaja.setSReferenciaDeposito(txtCDC_ReferDeposito.getValue().toString());
			rptmcaja.setSTipoImpresionRpt("");
			
			List<ArqueoCajaR> lstAcr = new ArrayList<ArqueoCajaR>();
			lstAcr.add(0,rptmcaja);
			m.put("ac_Lstrptmcaja002", lstAcr);						
	
			m.put("ac_lstCierreSocketPos",  new ArrayList<ArqueoSocketPos>());
			
		}catch(Exception error){
			error.printStackTrace();
		}
	}
	//&& ========= Notificacion por correo del cierre de caja.
	public void correoaContador(Vf55ca01 vf55ca01, String codcomp, 
					String moneda, Vf0101 vf01, int caid, int noarqueo,
					BigDecimal dfinal, Date fecha, double dMontoajustar){
		boolean bCorreo = true;
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		
		try{
			String sNombreFrom = "";
			String sContmail = vf55ca01.getId().getCacontmail().trim();
			String sCc = vf55ca01.getId().getCaan8mail().trim();
			String sCaja = vf55ca01.getId().getCaname();
			String sSubject = "Arqueo de Caja de "+sCaja;
			String sCajero = vf55ca01.getId().getCacatinom().trim();
			String sUbicacion = vf55ca01.getId().getCaco().trim() + " " +vf55ca01.getId().getCaconom().trim();
			String sNombrecomp = cCtrl.obtenerNombreComp(codcomp, caid, null, null);
			String sCajeromail ;
			String sUrl = new Divisas().obtenerURL();
			String estilo = "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" />" ;
			int codcajero = 0;
			
			if(moneda.equals("COR"))
				moneda = "Córdobas";
			if(moneda.equals("USD"))
				moneda = "Dólares";

			String sPatronCorreo = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$" ;
	
			codcajero   = vf01.getId().getAban8();
			sCajero     = vf01.getId().getAbalph().trim(); 
			sCajeromail = vf01.getId().getWwrem1().trim();
			sNombreFrom = vf01.getId().getAbalph().trim();
			
			if( !sCajeromail.toUpperCase().matches(sPatronCorreo) )
				sCajeromail="webmaster@casapellas.com.ni";
			if(  !sContmail.toUpperCase().matches(sPatronCorreo) && 
				 !sCc.toUpperCase().matches(sPatronCorreo) ) {
				 lblMsgProcArqueo.setValue( "Cierre Registrado correctamente" );	
				return;
			}
			
			boolean ajusteMinimo =  
					CodeUtil.getFromSessionMap("AjustaMinimo") != null && 
					CodeUtil.getFromSessionMap("AjustaMinimo").toString().compareTo("0") == 0;
			
			bCorreo =  new ArqueoCajaCtrl().enviarCorreo(sContmail,sCajeromail,
							sNombreFrom,sCc, sSubject, codcajero, sCajero, 
							caid, sCaja,codcomp+" /"+sNombrecomp,
							sUbicacion, noarqueo, dfinal, moneda, sUrl, 
			 				fecha,ajusteMinimo,dMontoajustar );		
			estilo += (bCorreo) ?
						" Se ha enviado un correo  de notificación de "+
						  " arqueo al contador.<br>" : 
						 "Ocurrió un problema en la notificación " +
						 " al contador.<br>";
			
			lblMsgProcArqueo.setValue( estilo );	
				
		}catch(Exception error){
			LogCajaService.CreateLog("generarReporteArqueo", "ERR", error.getMessage());
		}
	}
/******* 17. Mostrar la ventana de confirmación de procesar el arqueo de caja **************************/
	public void confirmarProcesarArqueo(ActionEvent ev){
		String sEfCaja = "", sDepfinal="", sTotalxmpago = "";
		String sDsugerido="",sReferDep="", sFaltante = "", sMensaje = "";
		String sCodcomp="", sMoneda = "";
		boolean bValido = true, bConfigCajas = true,bEfectivoCaja=false;
		CtrlCajas cc = new CtrlCajas();
		List lstFacs = new ArrayList();
		List lstLocalizaciones=null;
		F55ca017[] f55ca017=null;
		String sTipoDoc[] = null;
		int iFechaActual=0;
		FechasUtil f = new FechasUtil();
		Vf55ca01 f55 = null;

		try{
			
			m.remove("ac_MinutaDep");
			
		    List lstCajas = (List)m.get("lstCajas");
            f55 = ((Vf55ca01)lstCajas.get(0));
			
            //------ verificar que existan los datos de la caja.
			if (m.get("lstLocalizaciones") == null || m.get("f55ca017") == null
					|| m.get("sTiposDoc") == null
					|| m.get("iFechaActual") == null) {
                if(!consultarDatosCaja())
                    bConfigCajas=false;
            }
            //----- Validar que no hayan facturas pendientes de pago.
            if(bConfigCajas){
                lstLocalizaciones = (ArrayList)m.get("lstLocalizaciones");           
                f55ca017 = (F55ca017[])m.get("f55ca017");       
                sTipoDoc = (String[])m.get("sTiposDoc");           
                
                iFechaActual = (chkArqueoDiaAnterior.isChecked())? 
        				f.obtenerFechaJulianaDia((Date)m.get("ac_HoraArqueo")):
    					(Integer)m.get("iFechaActual");
                
                lstFacs = CtrlCajas.leerFacturaDelDia(iFechaActual, sTipoDoc, f55ca017 , f55.getId().getCaid());
                
    			if(lstFacs != null && lstFacs.size() > 0 ){
    				final String comp = ddlFiltroCompania.getValue().toString().trim();
    				
    				CollectionUtils.filter(lstFacs, new Predicate() {
    					public boolean evaluate(Object f) {
							return ((A02factco) f).getCodcomp().trim()
									.compareTo(comp) == 0;
    					}
    				});
    			}
                if(lstFacs != null && !lstFacs.isEmpty() ){
                    bValido = false;
                    sMensaje = "No se puede procesar el arqueo, existen facturas pendientes de pago";
                }
            }
            //&& =========== Validar transacciones pendientes con socket pos.
			if (!chkArqueoDiaAnterior.isChecked()) {

				List<Transactsp> transacciones = PosCtrl.getTransactionCaid(f55	.getId().getCaid());
				if (transacciones != null && !transacciones.isEmpty()) {
					bValido = false;
					sMensaje = "Existen Terminales de SocketPos con "
							+ "transacciones pendientes de cerrar";
				}
			}
            
		if(!bValido){
			return;
		}
			sEfCaja 	 = txtCDC_efectivoenCaja.getValue().toString().trim();
			sDepfinal 	 = lblCDC_depositoFinal.getValue().toString().trim();
			sTotalxmpago = lblACDetfp_Total.getValue().toString().trim();
			sDsugerido   = lblCDC_depositoSug.getValue().toString().trim();
			sReferDep 	 = txtCDC_ReferDeposito.getValue().toString().trim();
			sFaltante	 = lblCDC_SobranteFaltante.getValue().toString().trim();
			sCodcomp 	 = ddlFiltroCompania.getValue().toString();
			sMoneda		 = ddlFiltroMoneda.getValue().toString();
			
			//-------- Mensaje de Aviso de nota de débito al cajero.
			if(Double.parseDouble(sFaltante)<0){
				lblavisoFaltanteArqueo.setStyle("visibility: visible; display: inline");
				dwConfirmarProcesarArqueo.setStyle("width:360px;height:175px");
			}else{
				lblavisoFaltanteArqueo.setStyle("visibility: hidden; display: none");
				dwConfirmarProcesarArqueo.setStyle("width:340px;height:150px");
			}			
			//-------- Validaciones para los parámetros del arqueo.
			if( new Divisas().roundDouble(Double.parseDouble(sDsugerido)) > 0){
				
				if(sEfCaja.matches("^[0-9]+(\\.[0-9]*)?$") && Double.parseDouble(sEfCaja) >= 0){
					
					
					if( Double.parseDouble(sDepfinal) == 0 && Double.parseDouble(sFaltante) == 0 ){
						bValido = false;
						sMensaje = "Ingrese el monto correspondiente al depósito de caja";
						return;
					}
					
					
					//&& ====== Permitir que se grabe cierre de caja sin deposito final, solo con faltante al cajero
					if( Double.parseDouble(sDepfinal) == 0 && Double.parseDouble(sFaltante) < 0 ){
						bValido = true;
						bEfectivoCaja = false;
						txtCDC_ReferDeposito.setValue("99999999");
					}
					if(Double.parseDouble(sDepfinal)> 0 ){
						bValido = true;
						bEfectivoCaja = true;
					}
					
				}else{
					bValido = false;
					sMensaje = "El monto ingresado no es correcto";
				}
			}else{
				bEfectivoCaja = false;
				txtCDC_ReferDeposito.setValue("99999999");
				String minimo = "1";
				if(m.get("AjustaMinimo") != null) minimo = m.get("AjustaMinimo").toString();
				
				if(!minimo.equals("0")){
					if(Double.parseDouble(sTotalxmpago)!=0){ 
						bValido = true;
					}else{
						bValido = false;
						sMensaje = "El total por transacciones del arqueo debe ser distinta de cero";
					}
				}
			}
			//----Validaciones de la referencia.
			if(bValido && bEfectivoCaja){
				
				sReferDep = sReferDep.trim();
				
				Minutadp md = ArqueoCajaCtrl.leerConfiguracionMndp(f55.getId().getCaid(), sCodcomp, sMoneda);
				
				final String compania = sCodcomp;
				F55ca014 f14Select = (F55ca014) 
				CollectionUtils.find(Arrays.asList((F55ca014[])CodeUtil.getFromSessionMap("ac_lstCompaniasCaja") ), new Predicate(){
					public boolean evaluate(Object o) {
						return compania.trim().compareTo( ((F55ca014)o ).getId().getC4rp01().trim() ) == 0 ;
					}
				});
				CodeUtil.putInSessionMap("ac_DatosCompaniaArqueo", f14Select);
				
				if( sReferDep.isEmpty() && ( f14Select.getId().getMndepatm() == 0 || md == null ) ){
					sMensaje = "Número de minuta de depósito Requerido";
					bValido = false;
					return;
				}
				if( !sReferDep.isEmpty() && !sReferDep.matches(PropertiesSystem.REGEXP_8DIGTS) ){
					sMensaje = "Datos inválidos en campo Referencia";
					bValido = false;
					return;
				}
				
				if(md == null && f14Select.getId().getMndepatm() != 0 ){
					sMensaje = "No se encuentra configuración de minuta automática para Caja y Compañía seleccionada ";
					bValido = false;
					return;
				}
				
				if( md != null && !sReferDep.isEmpty() ){
					md.setNumerominutamanual( Integer.parseInt(sReferDep) ) ;
				}
				if(md != null && sReferDep.isEmpty()  && f14Select.getId().getMndepatm() != 0  ){
					txtCDC_ReferDeposito.setValue( md.getNosiguienteDisplay() );
				}
				//&& ========================== Conservar los datos de la minuta generada
				if(  md != null  ){
					CodeUtil.putInSessionMap("ac_MinutaDeposito", md);
				}
				
				int iCodBanco = f14Select.getId().getC4bnc();
				
				F55ca022 f22 = new BancoCtrl().obtenerBancoxId(iCodBanco);
				
				if( !( f22.getId().getConciliar() == 1 && f14Select.getId().isConfrmauto()  ) && !sReferDep.trim().isEmpty() ){
					
					sMensaje = cc.verificarReferenciaPago(sReferDep, Integer.parseInt(sReferDep), "ZX", iCodBanco, sCodcomp, sMoneda, "");
					if(sMensaje != null && !sMensaje.isEmpty() ){
						bValido = false;
						return;
					}
				}
			}
	 
			
		}catch(Exception error){ 
			error.printStackTrace(); 
		}finally{
			
			if(bValido)
				dwConfirmarProcesarArqueo.setWindowState("normal");
			else{
				lblValidarArqueo.setValue( sMensaje );
				dwValidarArqueo.setWindowState("normal");
			}
		}
	}
	
/******** 16. Realizar el cálculo del deposito final y obtener Sobrante/faltante ***********************/
	public void calcularDepositoCaja(ActionEvent ev){
 		double dEfectEnCaja = 0,dEfecNeto=0, dMontomin = 0,vs = 0,dDepFinal = 0;
		String sEfCaja,fs = "", sEstilo = "";
		Divisas dv = new Divisas();
		boolean ajustarMinimo = false;
		try{
			
			sEfCaja = txtCDC_efectivoenCaja.getValue().toString().trim();
			
			//validar que se hayan ingresado solamente números y punto.
			if(m.get("ac_CDCefectivoneto")!=null && m.get("ac_CDCMontoMinimo")!=null){
				if(!sEfCaja.equals("") && sEfCaja.matches("^[0-9]+(\\.[0-9]*)?$")){
					
					dEfecNeto = Double.parseDouble(m.get("ac_CDCefectivoneto").toString());
					dMontomin = Double.parseDouble(m.get("ac_CDCMontoMinimo").toString());
					dEfectEnCaja = Double.parseDouble(sEfCaja);
					
					dEfecNeto = dv.roundDouble(dEfecNeto);
					dMontomin = dv.roundDouble(dMontomin);
					dEfectEnCaja = dv.roundDouble(dEfectEnCaja);
					
					if(m.get("AjustaMinimo").toString().equals("0")){
						dMontomin = Double.parseDouble(lblCDC_montoMinAjust.getValue().toString());
						ajustarMinimo = true;
					}
					
					vs = dEfectEnCaja - dEfecNeto;
					vs -= (dMontomin>0)?
							dMontomin:
							0;	
					vs = dv.roundDouble(vs);
					
					if(vs>0){ //sobrante					
						fs= "Sobrante";
						sEstilo = "color: green";
						dDepFinal = dEfecNeto + vs;	
					}
					else if(vs<0){ //faltante
						fs= "Faltante";
						sEstilo = "color: red";
						
						if(ajustarMinimo)
							dDepFinal = dEfectEnCaja;
						else
							dDepFinal = dEfectEnCaja - dMontomin;
					}else{					
						fs= "Faltante/Sobrante";
						sEstilo = "color: black";
						dDepFinal = dEfecNeto;					
					}							
				}else{				
					lblValidarArqueo.setValue(
					 "<img width=\"7\" src=\"theme/icons/redCircle.jpg\" border=\"0\" /> El monto ingresado no es correcto.!<br>");			
					dwValidarArqueo.setWindowState("normal");
					fs= "Sobrante/Faltante";
					sEstilo = "color: black";
					dDepFinal = 0;
				}			
			}else{
				lblValidarArqueo.setValue(
				 "<img width=\"7\" src=\"theme/icons/redCircle.jpg\" border=\"0\" /> Seleccione primero moneda y compañía.!<br>");			
				dwValidarArqueo.setWindowState("normal");
				fs= "Sobrante/Faltante";
				sEstilo = "color: black";
				dDepFinal = 0;
		
			}
			if(dDepFinal <  0 ) dDepFinal = 0 ;
			//establecer los valores que se mostraran en pantalla.
			lblet_SobranteFaltante.setValue(fs);					
			lblet_SobranteFaltante.setStyle(sEstilo);
			lblCDC_SobranteFaltante.setStyle(sEstilo);
			lblCDC_depositoFinal.setValue(dDepFinal);
			lblCDC_SobranteFaltante.setValue(vs);	
			
		}catch(Exception error){
			error.printStackTrace();
		}
	}
/******** 15. Cargar todos los recibos filtrados solo por metodo de pago *******************************/
	
	//-----------------------------------------------------------------------------
	public double formatListaRecibos(List<Vrecibosxtipompago> lstRecibos,String sVariableMapa ){
		double total = 0;				
		try{
			if(lstRecibos == null || lstRecibos.isEmpty())
				return 0;
				
			for(int i=0; i<lstRecibos.size();i++){
				Vrecibosxtipompago v = lstRecibos.get(i);
				v.setMonto(v.getId().getMonto());
				lstRecibos.set(i,v);
				total += v.getId().getMonto().doubleValue();					
			}
			m.put(sVariableMapa, lstRecibos);
				
		}catch(Exception error){
			error.printStackTrace();
		}		
		return total;
	}
	
	/**------------------------	 recibos por métodos de pago  --------------------------------------**/	
	@SuppressWarnings({ "rawtypes", "static-access" })
	public void cargarRecxMetodoPago(String sCodcomp,String sCodsuc,int iCaid,String sMoneda,
										Date dtFechaArqueo,Date dtHoraInicio, Date dtHoraFin,Session sesion1){
		
ArqueoControl acCtrl1 = new ArqueoControl();
		
		Divisas dv = new Divisas();
		List lstRecibosmpago = new ArrayList(), lstRecibos = new ArrayList();
		List lstRe5=new ArrayList(),lstReq=new ArrayList(),lstRe8=new ArrayList(),lstRen=new ArrayList();
		List lstRehe=new ArrayList(),lstRehm=new ArrayList(),lstRehs=new ArrayList();
		
		double dTotalmp = 0,dTotaldev = 0,dTotalcambio = 0;
		double dRech=0,dRechm=0,dRechs = 0, dRecn=0,dRec8=0,dRec5=0,dRecq=0;
		String sMpago = "";
		
		try{			
			//-----------------------Recibos pagados con efectivo.

			lstRecibosmpago = acCtrl1.cargarRecibosxMetodoPago(iCaid, sCodsuc, sCodcomp, sMoneda,
														dtFechaArqueo,dtHoraInicio,dtHoraFin);
			if(lstRecibosmpago != null && lstRecibosmpago.size()>0){
				for(int i=0; i<lstRecibosmpago.size(); i++){
					Vrecibosxtipompago v = (Vrecibosxtipompago)lstRecibosmpago.get(i);
					if (v.getId().getTiporec().trim().toUpperCase().equals("DCO")) {
						v.setMonto(v.getId().getMonto().multiply(BigDecimal.valueOf(-1)));
					} else {
						v.setMonto(v.getId().getMonto());
					}
					
					lstRecibosmpago.remove(i);
					lstRecibosmpago.add(i,v);
					
					dTotalmp = dv.roundDouble( v.getId().getMonto().doubleValue());
					sMpago = v.getId().getMpago().trim();
					
					if(sMpago.equals(MetodosPagoCtrl.EFECTIVO )){
						dRec5 += v.getId().getTiporec().trim().toUpperCase().equals("DCO") ? 0 : dTotalmp;
						lstRe5.add(v);	
					}

					if(sMpago.equals(MetodosPagoCtrl.CHEQUE)){
						dRecq += v.getId().getTiporec().trim().toUpperCase().equals("DCO") ? 0 : dTotalmp;
						lstReq.add(v);
						
					}

					if(sMpago.equals(MetodosPagoCtrl.TARJETA)){
						if(v.getId().getVmanual().equals("0") && v.getId().getRefer6().trim().equals("") && v.getId().getRefer7().trim().equals("")){
							dRech += v.getId().getTiporec().trim().toUpperCase().equals("DCO") ? 0 : dTotalmp;
							lstRehe.add(v);
						}else if(v.getId().getVmanual().equals("2") && !v.getId().getRefer6().trim().equals("") && !v.getId().getRefer7().trim().equals("")){
							dRechs += v.getId().getTiporec().trim().toUpperCase().equals("DCO") ? 0 : dTotalmp;
							lstRehs.add(v);
						}else{
							dRechm += v.getId().getTiporec().trim().toUpperCase().equals("DCO") ? 0 : dTotalmp;
							lstRehm.add(v);
						}
					}

					if(sMpago.equals(MetodosPagoCtrl.TRANSFERENCIA)){
						dRec8 += v.getId().getTiporec().trim().toUpperCase().equals("DCO") ? 0 : dTotalmp;
						lstRe8.add(v);
					}

					if(sMpago.equals(MetodosPagoCtrl.DEPOSITO)){
						dRecn += v.getId().getTiporec().trim().toUpperCase().equals("DCO") ? 0 : dTotalmp;
						lstRen.add(v);
					}					
				}
			}
			List<Vreciboxdevolucion>lstDev = acCtrl1.obtenerRecibosxdevolucion(false, iCaid, sCodsuc,
					sCodcomp, "DCO", sMoneda, dtFechaArqueo, dtHoraInicio,		dtHoraFin);
			
			if (lstDev != null && !lstDev.isEmpty() ) {
				
				String strDevContains = "";
				String strDataDev = "";
				for (Vreciboxdevolucion v : lstDev) {
					
					dTotaldev = dv.roundDouble(v.getId().getMonto().doubleValue());
					
					strDataDev  = 
							v.getId().getNumrec()+"@"+v.getId().getNofact() + "@" +
							v.getId().getMonto().toString()+"@"+v.getId().getMoneda()+"@"
							+v.getId().getMpago()+"@" + v.getId().getTiporec() +"@"  +v.getId().getTipofact()+"@"
							+ new SimpleDateFormat("HH:mm:ss").format( v.getId().getHora() ) ;
					
					if(strDevContains.contains(strDataDev)){
						continue;
					}
					
					strDevContains += strDataDev;
					
					
					if( v.getId().getMpago().compareTo(MetodosPagoCtrl.EFECTIVO ) == 0 ){
						dRec5 -= v.getId().getMonto().doubleValue();
						continue;
					}
					if( v.getId().getMpago().compareTo(MetodosPagoCtrl.CHEQUE) == 0 ){
						dRecq -= v.getId().getMonto().doubleValue();
						continue;
					}
					if( v.getId().getMpago().compareTo(MetodosPagoCtrl.DEPOSITO) == 0 ){
						dRecn -= dTotaldev;
						continue;
					}
					if( v.getId().getMpago().compareTo(MetodosPagoCtrl.TRANSFERENCIA) == 0 ){
						dRec8 -= dTotaldev;
						continue;
					}
					if( v.getId().getMpago().compareTo(MetodosPagoCtrl.TARJETA) == 0 ){
						if (v.getId().getVmanual().equals("0")
								&& v.getId().getRefer6().trim().equals("")
								&& v.getId().getRefer7().trim().equals("")) {
							dRech -= dTotaldev;
						} else if (v.getId().getVmanual().equals("2")) {
							dRechs -= dTotaldev;
						} else {
							dRechm -= dTotaldev;
						}
					}
				}
			 
			}
			//-- -obtener los cambios realizados para método de pago en efectivo (5).
			lstRecibos = acCtrl1.obtenerRecibosxcambios(iCaid, sCodcomp, sCodsuc, sMoneda,
														dtFechaArqueo, dtHoraInicio,dtHoraFin);
			if(lstRecibos!= null && lstRecibos.size()>0){
				dTotalcambio =0;
				for(int i=0; i<lstRecibos.size();i++){
					Vdetallecambiorec v = (Vdetallecambiorec)lstRecibos.get(i);
					dTotalcambio += dv.roundDouble(v.getId().getCambio().doubleValue());
				}
				dRec5 -= dv.roundDouble(dTotalcambio);			
			}
			
			//--------------------------------------------------//
			//---   obtener el monto por Salidas de caja.
			SalidasCtrl sCtrl = new SalidasCtrl();
			double dMontoSal = 0;
			List<Vsalida> lstSal = null;
			lstSal = sCtrl.obtenerSolicitudSalidas(iCaid, sCodcomp, sCodsuc, 0, 0, sMoneda, "",
													0,dtFechaArqueo,dtHoraInicio,dtHoraFin);
			if(lstSal!=null && lstSal.size()>0){
				for(int i=0;i<lstSal.size();i++){
					Vsalida v = (Vsalida)lstSal.get(i);
					v.setMonto(v.getId().getMonto());
					dMontoSal += dv.roundDouble(v.getId().getMonto().doubleValue());
					if(v.getId().getOperacion().equals(MetodosPagoCtrl.CHEQUE)){
						dRecq += dv.roundDouble(v.getId().getMonto().doubleValue());

						//------ llenar el objeto Vrecibosxtipompago para la lista del detalle.
						Vrecibosxtipompago vs = new Vrecibosxtipompago();
						VrecibosxtipompagoId vsId = new VrecibosxtipompagoId();
						vsId.setNumrec(v.getId().getNumsal());
						vsId.setTiporec("S");
						vsId.setCliente(v.getId().getNombresol());
						vsId.setMonto(v.getMonto());
						vsId.setMontoapl(v.getMonto());
						vsId.setHora(v.getId().getFproceso());
						vsId.setRefer1(v.getId().getRefer1());
						vsId.setRefer2(v.getId().getRefer2());
						vsId.setRefer3(v.getId().getRefer3());
						vsId.setRefer4(v.getId().getRefer4());
						vs.setMonto(v.getMonto());
						vs.setId(vsId);
						lstReq.add(vs);
					}
				}
				dMontoSal = dv.roundDouble(dMontoSal);
				dRec5 -= dMontoSal;
			}
			
			
			//&& ======================= obtener los datos por donaciones y agregarlos a los totales.
			CodeUtil.removeFromSessionMap(new String[]
					{ "ac_totalDonacionEfectivo, ac_lstDonacionesArqueoCaja", "ac_lstDonacionesFormaPago" } );
			
			lstDonacionesFormaPago = new ArrayList<DncDonacion>();
			
			List<DncDonacion>lstDonaciones = DonacionesCtrl.obtenerDonaciones(iCaid, sCodcomp, sMoneda, dtFechaArqueo, dtHoraInicio, dtHoraFin) ;
			
			if( !lstDonaciones.isEmpty() ){
				
				List<String>formasdepago = (List<String>)CodeUtil.selectPropertyListFromEntity(lstDonaciones, "formadepago", true);
				for (final String mpago : formasdepago) {
					
					List<DncDonacion> dncFormaPago = (List<DncDonacion>)
					CollectionUtils.select(lstDonaciones, new Predicate(){
						public boolean evaluate(Object o) {
							return ((DncDonacion)o).getFormadepago().compareTo(mpago) == 0;
						}
					});
					
					BigDecimal totalFormaPago = CodeUtil.sumPropertyValueFromEntityList(dncFormaPago, "montorecibido", false) ;
					
					lstDonacionesFormaPago.add(new DncDonacion(mpago,
							MetodosPagoCtrl.descripcionMetodoPago(mpago),
							dncFormaPago.size(), sMoneda, totalFormaPago));
					
					if( mpago.compareTo(MetodosPagoCtrl.EFECTIVO ) == 0 ){
						dRec5 += totalFormaPago.doubleValue();
						CodeUtil.putInSessionMap("ac_totalDonacionEfectivo", totalFormaPago);
						continue;
					}
					if( mpago.compareTo(MetodosPagoCtrl.TARJETA) == 0 ){
						dRech += totalFormaPago.doubleValue();
						continue;
					}
					if( mpago.compareTo(MetodosPagoCtrl.CHEQUE) == 0 ){
						dRecq += totalFormaPago.doubleValue();
						continue;
					}
					if(mpago.compareTo(MetodosPagoCtrl.DEPOSITO) == 0 ){
						dRecn += totalFormaPago.doubleValue();
						continue;
					}
					if( mpago.compareTo(MetodosPagoCtrl.TRANSFERENCIA) == 0 ){
						dRec8 += totalFormaPago.doubleValue();
						continue;
					}
				}
				
				CodeUtil.putInSessionMap("ac_lstDonacionesArqueoCaja", lstDonaciones);
				CodeUtil.putInSessionMap("ac_lstDonacionesFormaPago", lstDonacionesFormaPago);
			}

			gvDonacionesFormaPago.dataBind();
			CodeUtil.refreshIgObjects(gvDonacionesFormaPago);
			
			//&& ====================================================================================================
			 
			if(dRec5 < 0){
				lblACDetfp_Efectivo.setValue(0);
			}else{
				lblACDetfp_Efectivo.setValue(dRec5);
			}
			lblACDetfp_Cheques.setValue(dRecq);			
			lblACDetfp_TarCred.setValue(dRech);
			lblACDetfp_TCmanual.setValue(dRechm);
			lblACDetfp_TCSocketpos.setValue(dRechs);
			lblACDetfp_TransBanco.setValue(dRec8);
			lblACDetfp_DepDbanco.setValue(dRecn);			
			lblACDetfp_Total.setValue( ((dRec5<0)?0:dRec5) + dRecq +dRech +dRechm + dRechs +dRec8 +dRecn);
			lblCDC_pagocheques.setValue(dRecq);			
			
			m.put("ac_TotalEfectivo", dRec5);
			m.put("ac_recibosPagEfectivo", lstRe5);
			m.put("ac_recibosPagCheque",lstReq);
			m.put("ac_recibosPagTarjCred",lstRehe);  //-- pago con voucher's electrónicos
			m.put("ac_recibosPagTarjCredm",lstRehm); //-- Pago con voucher's manuales
			m.put("ac_recibosPagTarjCreds",lstRehs); //-- Pago con socketPOS
			m.put("ac_recibosPagTransBanco", lstRe8);
			m.put("ac_recibosPagDepositoBanco", lstRen);			
			m.put("ac_totalcheques", dRecq);
			
		}catch(Exception error){
			LogCajaService.CreateLog("cargarRecxMetodoPago", "ERR", error.getMessage());
		} finally {
			HibernateUtilPruebaCn.closeSession();
		}
	}

	public void enviarCorreoNotificacionDonaciones(List<DncDonacion>donaciones,int codigocaja , int codigocajero ){
		try {
			
			Collections.sort(donaciones, new Comparator<DncDonacion>(){
				public int compare(DncDonacion d1, DncDonacion d2) {
					return d1.getFormadepago().trim().compareTo( d2.getFormadepago().trim() ) ;
				}
			}) ;
			
			List<String[]> dtaFormasdepago = new ArrayList<String[]>();
			
			List<String>formasPago = (ArrayList<String>)CodeUtil.selectPropertyListFromEntity(donaciones, "formadepago", true);	
			for (final String mpago : formasPago) {
				
				List<DncDonacion> dncsxfpago = (ArrayList<DncDonacion>)
						CollectionUtils.select(donaciones, new Predicate(){
							public boolean evaluate(Object o) {
								return ((DncDonacion)o).getFormadepago().compareTo(mpago) == 0 ;
						}
					});
				BigDecimal total = CodeUtil.sumPropertyValueFromEntityList(dncsxfpago, "montorecibido", false);
				dtaFormasdepago.add(new String[]{MetodosPagoCtrl.descripcionMetodoPago(mpago),  total.toString(), String.valueOf( dncsxfpago.size() ) } ) ;
			}
 			
			String styleHead = "style=\"padding: 5px; color: white; background-color: #5e89b5; text-align: center;  font: bold 12px Arial, sans-serif;\" "  ;
			
			StringBuilder sbTablaCorreo = new StringBuilder();
			
			String spanHtml = "<span style = \"padding-top: 3px; display:block;  font: 13px Arial, sans-serif; color: #1a1a1a;\"> @TEXTOSPAN </span>" ;
			
			String htlm1 = 
			"<div style = \"margin-bottom: 5px; \" >"+ 
				spanHtml.replace("@TEXTOSPAN", "<b>Detalle de Donaciones Recibidas en caja</b> "+codigocaja ) + 	
				spanHtml.replace("@TEXTOSPAN", "<b>Transacciones: </b>" +donaciones.size() ) + 
				spanHtml.replace("@TEXTOSPAN", "<b>Moneda: </b>" + donaciones.get(0).getMoneda() ) ;
			
			for (String[] dtaPago : dtaFormasdepago) {
				String strAdd = "<b>Pago: </b>" + dtaPago[0] + "<b>, Cantidad: </b>" + dtaPago[2] +  "<b>, Monto: </b>" + String.format("%1$,.2f", new BigDecimal(dtaPago[1])) ; 
				htlm1 += spanHtml.replace("@TEXTOSPAN",strAdd ) ;
			}
			
			htlm1 += "</div>";
			
			sbTablaCorreo.append(htlm1);
			
			sbTablaCorreo.append("<div style = \"width: 100%; padding-top: 10px; padding-left: 15%;\">") ;
			
			sbTablaCorreo.append("<table style=\"border: 1px solid silver;  \" > <thead>")
					.append("<tr>").append("<th "+styleHead+" >Código</th>")
					.append("<th "+styleHead+"  >Beneficiario</th>").append("<th "+styleHead+" >Monto</th>")
					.append("<th "+styleHead+" >Moneda</th>").append("<th "+styleHead+" >Forma de pago</th>")
					.append("<th "+styleHead+" >Cliente</th>").append("<th "+styleHead+" >Hora</th>")
					.append("</tr>").append("</thead>").append("<tbody>");
			
			String styleTd = "style = \"padding: 5px; border: 1px dashed gray; text-transform: capitalize; font: 12px Arial, sans-serif;  \"";
			String styleTdMonto = "style = \"padding: 5px; border: 1px dashed gray; text-transform: capitalize; text-align: right;  font: 12px Arial, sans-serif;\" " ;
			
			String strBody = 
				"<tr><td @styleTD >@CODBNF</td><td @styleTD >@NOMBNF</td><td @tdStyleMonto >@MONTODNC</td>" +
					"<td @styleTD >@MONEDA</td><td @styleTD >@FPAGO</td><td @styleTD >@CLTNOMBRE</td>" +
					"<td @styleTD >@HORADNC</td>" +
				"</tr>"; 
			
			strBody = strBody.replace("@styleTD", styleTd ).replace("@tdStyleMonto", styleTdMonto );
			
			SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a"); 
			
			for (DncDonacion dnc : donaciones) {
				
				sbTablaCorreo.append( 
					strBody.replace("@CODBNF", Integer.toString( dnc.getCodigo() ) )
						.replace("@NOMBNF", dnc.getBeneficiarionombre().trim() )
						.replace("@MONTODNC", String.format( "%1$,.2f", dnc.getMontorecibido() ) )
						.replace("@MONEDA", dnc.getMoneda() )
						.replace("@FPAGO", MetodosPagoCtrl.descripcionMetodoPago(dnc.getFormadepago()) )
						.replace("@CLTNOMBRE", dnc.getClientenombre().trim())
						.replace("@HORADNC", sdf.format(dnc.getFechacrea()))
				);
			}
			sbTablaCorreo.append("</tbody></table>");
			sbTablaCorreo.append("</div>");
					
			//&& ============ obtener destinatarios de correos.
			String strSql  = "select * from "+PropertiesSystem.ESQUEMA+".f55ca01 where caid = " + codigocaja ;
		
			List<F55ca01>dtaCajas =  (ArrayList<F55ca01>) ConsolidadoDepositosBcoCtrl.executeSqlQuery( strSql, true, F55ca01.class );
			F55ca01 f55 = dtaCajas.get(0);
			
			List<Integer>codigosaEnvioCorreo = new ArrayList<Integer>() ;
			codigosaEnvioCorreo.add(f55.getId().getCaan8() );
			codigosaEnvioCorreo.add(f55.getId().getCacont());
			codigosaEnvioCorreo.add(codigocajero);
			 
			String codigosIn = codigosaEnvioCorreo.toString().replace("[", "(").replace("]", ")") ;
			strSql = "select * from "+PropertiesSystem.ESQUEMA+".vf0101 where aban8 in " + codigosIn ;
						
			List<Vf0101>dtaEmps =  (ArrayList<Vf0101>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSql, true, Vf0101.class );
			
			List<String[]>lstCtaCorreo = new ArrayList<String[]>();
			
			for (Vf0101 dtaEmp : dtaEmps) {
				lstCtaCorreo.add(new String[]{dtaEmp.getId().getAbalph().toLowerCase(), 
								dtaEmp.getId().getWwrem1().toLowerCase().trim()}) ;
			}
			
			List<CustomEmailAddress> toList= new ArrayList<CustomEmailAddress>();
			for (String[] sCtaCorreo : lstCtaCorreo) {
				toList.add(new CustomEmailAddress(sCtaCorreo[1], sCtaCorreo[0]));
			}
			
			String sSubject = "Donaciones Procesadas en arqueo de caja a la fecha:"	+new SimpleDateFormat("dd MMMM yyyy",new Locale("es","ES")).format(new Date());
			
			MailHelper.SendHtmlEmail(
					new CustomEmailAddress(cajaparm.getParametros("34", "0", "WEB_EMAIL_ADRESS").getValorAlfanumerico().toString(), "Módulo de Caja"),
					toList, null, new CustomEmailAddress(cajaparm.getParametros("34", "0", "MAIL_BOUNCEADDRESS").getValorAlfanumerico().toString()), 
					sSubject, sbTablaCorreo.toString());
			
		} catch (Exception e) {
			LogCajaService.CreateLog("enviarCorreoNotificacionDonaciones", "ERR", e.getMessage());
		}
	}
	
	
/***********************************************************************************************/
/** Obtener los recibos por todos los métodos de pago distintos de efectivo y clasificarlos. 
 **********************/
	public void obtenerRecibosPagBanco(int iCaid,String sCodsuc,String sCodcomp,String sMoneda,Date dtFecha){
		double dCoh=0,dCon=0,dCo8=0,dCrh=0,dCrn=0,dCr8=0;
		List lstCoh =new ArrayList(),lstCon = new ArrayList(),lstCo8 = new ArrayList();
		List lstCrh =new ArrayList(),lstCrn =new ArrayList(),lstCr8 = new ArrayList();
		List lstRecibos = new ArrayList();
		String sMpago = "", sTiporec = "";
		double dMonto = 0;
		Divisas dv = new Divisas();
		
		try{			
//			lstRecibos = acCtrl.obtenerRecpagBanco(iCaid, sCodsuc, sCodcomp, sMoneda, dtFecha, null);
			
			if(lstRecibos!= null && lstRecibos.size()>0){
				for(int i=0; i<lstRecibos.size();i++){
					Vrecibosxtipoegreso v = (Vrecibosxtipoegreso)lstRecibos.get(i);
					v.setMonto(v.getId().getMonto());
					sMpago 	 = v.getId().getMpago().trim();
					sTiporec = v.getId().getTiporec().trim();
					dMonto	 = dv.roundDouble(v.getId().getMonto().doubleValue());
					
					//--------------------- pago de ventas.
					if(sTiporec.equals("CO")){
						if(sMpago.equals(MetodosPagoCtrl.TARJETA)){
							lstCoh.add(v);
							dCoh += dMonto;
						}else
						if(sMpago.equals(MetodosPagoCtrl.DEPOSITO)){
							lstCon.add(v);
							dCon += dMonto;							
						}else
						if(sMpago.equals(MetodosPagoCtrl.TRANSFERENCIA)){
							lstCo8.add(v);
							dCo8 +=dMonto;
						}						
					}else
					if(sTiporec.equals("CR")){
						if(sMpago.equals(MetodosPagoCtrl.TARJETA)){
							lstCrh.add(v);
							dCrh += dMonto;
						}else
						if(sMpago.equals(MetodosPagoCtrl.DEPOSITO)){
							lstCrn.add(v);
							dCrn += dMonto;							
						}else
						if(sMpago.equals(MetodosPagoCtrl.TRANSFERENCIA)){
							lstCr8.add(v);
							dCr8 +=dMonto;
						}
					}
				}
			}
			
		}catch(Exception error){
			error.printStackTrace();
		}
	}
	
	
	
/******** 14. cargar todos los recibos filtrados por tipo recibo y metodo de pago **********************/
	public void cargarRecxTipoyMetPago(ActionEvent ev){
		String sIdLink, sListaRecibos = "",sTextoEncabezado = "";
		
		try{
			sIdLink = ev.getComponent().getId().trim();
			
			//recibos filtrados por Tipo de recibo y por método de pago 'Sección de egresos'.
			if(sIdLink.equals("lnkDetVtsTarjCred")){
				sListaRecibos = "ac_recContTarjCred";
				sTextoEncabezado = "Recibos de pago por ventas con tarjeta de crédito vouchers electrónicos";
			}else if(sIdLink.equals("lnkACDetfp_TarCredManual")){
				sListaRecibos = "ac_recibosPagTarjCredm";
				sTextoEncabezado = "Recibos de pago por ventas con tarjeta de crédito vouchers manuales";
			}			
			else if(sIdLink.equals("lnkDetVtsTransBanc")){
				sListaRecibos = "ac_recContTransBanco";
				sTextoEncabezado = "Recibos de pago por ventas con transferencia en banco";
			}
			else if(sIdLink.equals("lnkDetVtsDepBanco")){
				sListaRecibos = "ac_recContDepositoBanco";
				sTextoEncabezado = "Recibos por ventas con depósito en banco";
			}
			else if(sIdLink.equals("lnkDetAbonoTCred")){
				sListaRecibos = "ac_recAbonosTarCred";
				sTextoEncabezado = "Recibos por abonos con tarjeta de crédito";
			}
			else if(sIdLink.equals("lnkDetAbonoTransBanc")){
				sListaRecibos = "ac_recAbonosTransBanco";
				sTextoEncabezado = "Recibos por abono con transferencia en banco";
			}
			else if(sIdLink.equals("lnkDetAbonoDepBanco")){
				sListaRecibos = "ac_recAbonosDepositoBanco";
				sTextoEncabezado = "Recibos por abonos pagados con depósito en banco";
			}
			
			//--- Primas y Reservas.
			else if(sIdLink.equals("lnkDetPrimaTCred")){
				sListaRecibos = "ac_recPrimasTarCred";
				sTextoEncabezado = "Recibos por primas pagadas con tarjeta de crédito";
			}
			else if(sIdLink.equals("lnkDetPrimasTransBanc")){
				sListaRecibos = "ac_recPrimasTransBanco";
				sTextoEncabezado = "Recibos por primas pagadas con transferencia en banco";
			}
			else if(sIdLink.equals("lnkDetPrimasDepBanco")){
				sListaRecibos = "ac_recPrimasDepositoBanco";
				sTextoEncabezado = "Recibos por primas pagadas con depósito en banco";
			}
			
			//----- Ingresos Extraordinarios.
			else if(sIdLink.equals("lnkIngExtOrdin") || sIdLink.equals("lnkegIngExtraH")){
				sListaRecibos = "ac_lstriexH";
				sTextoEncabezado = "Recibos por Ingresos Extra. pagados con tarjeta de crédito";
			}
			else if(sIdLink.equals("lnkiniexDepBanco") || sIdLink.equals("lnkegIngExtraN")){
				sListaRecibos = "ac_lstriexN";
				sTextoEncabezado = "Recibos por Ingresos ex pagados con depósito en banco";
			}
			else if(sIdLink.equals("lnkiniexTransBanco") || sIdLink.equals("lnkegIngExtra8")){
				sListaRecibos = "ac_lstriex8";
				sTextoEncabezado = "Recibos por Ingresos ex pagados con transferencia bancaria";
			}
			//--- Pagos a Financimientos.
			else if(sIdLink.equals("lnkDetFinanTCred")){
				sListaRecibos = "ac_recFinanTarCred";
				sTextoEncabezado = "Recibos de financimientos pagadas con tarjeta de crédito";
			}
			else if(sIdLink.equals("lnkDetFinanTransBanc")){
				sListaRecibos = "ac_recFinanTransBanco";
				sTextoEncabezado = "Recibos de financimientos pagadas con transferencia en banco";
			}
			else if(sIdLink.equals("lnkDetFinanDepBanco")){
				sListaRecibos = "ac_recFinanDepositoBanco";
				sTextoEncabezado = "Recibos de financimientos pagadas con depósito en banco";
			}			
			else 
			//---- recibos filtrados solamente por método de pago 'ayuda del cálculo del depósito'.								
			if(sIdLink.equals("lnkACDetfp_Efectivo")){
				sListaRecibos = "ac_recibosPagEfectivo";
				sTextoEncabezado = "Recibos pagados con efectivo";
				}			
			else if(sIdLink.equals("lnkACDetfp_TarCred")){
				sListaRecibos = "ac_recibosPagTarjCred";
				sTextoEncabezado = "Recibos pagados tarjeta de crédito";
				}	
			else if(sIdLink.equals("lnkACDetfp_Cheques")){
				sListaRecibos = "ac_recibosPagCheque";
				sTextoEncabezado = "Recibos pagados con cheque";
			}
			
			//&& ================ Recibos por anticipos PMT
			
			else if(sIdLink.equals("lnkDetTarjetaCreditoPMT")){
				sListaRecibos = "ac_recPMTTarCred";
				sTextoEncabezado = "Recibos de Anticipos PMT con tarjeta de crédito";
			}
			else if(sIdLink.equals("lnkDetDepositoEnBancoPMT")){
				sListaRecibos = "ac_recPMTDepositoBanco";
				sTextoEncabezado = "Recibosde Anticipos PMT con depósito en banco";
			}		
			else if(sIdLink.equals("lnkDetTransferenciaBancariaPMT")){
				sListaRecibos = "ac_recPMTTransBanco";
				sTextoEncabezado = "Recibosde Anticipos PMT con transferencias bancarias";
			}
			
			
			//================ actualizar el grid, el encabezado y mostrar la ventana.
			
			List<Vrecibosxtipoegreso> recibosPorTipo = (ArrayList<Vrecibosxtipoegreso>)CodeUtil.getFromSessionMap(sListaRecibos) ;
			
			if( recibosPorTipo != null && !recibosPorTipo.isEmpty() ){		
				CodeUtil.putInSessionMap("lstRecxTipoyMetpago", recibosPorTipo) ;
				gvRecibosxTipoyMetodopago.dataBind();
				gvRecibosxTipoyMetodopago.setPageIndex(0);
				hdDetTrecxMetPago.setCaptionText(sTextoEncabezado);
				dwDetTipoReciboxMetodoPago.setWindowState("normal");
				return;
			}	
			
			//&& ===================== Nuevo grid con datos del banco para transferencias
			if(sIdLink.compareTo("lnkACDetfp_TransBanco") == 0 ){
				sListaRecibos = "ac_recibosPagTransBanco";
				sTextoEncabezado = "Recibos pagados con Transferencia Bancaria";
			}
			if(sIdLink.compareTo("lnkACDetfp_DepDbanco") == 0 ){
				sListaRecibos = "ac_recibosPagDepositoBanco";
				sTextoEncabezado = "Recibos pagados con depositos directos en banco";
			}
			
			lstRecibosxMpago8N = (List<Vrecibosxtipompago>) CodeUtil.getFromSessionMap(sListaRecibos) ;
			
			if(lstRecibosxMpago8N == null || lstRecibosxMpago8N.isEmpty() ){
				return;
			}
			
			CollectionUtils.forAllDo(lstRecibosxMpago8N, new Closure(){
				public void execute(Object o) {
					Vrecibosxtipompago v = (Vrecibosxtipompago)o;
					if(v.getId().getMpago().compareTo(MetodosPagoCtrl.DEPOSITO) == 0 || v.isProcesado() ){
						return;
					}
					String refer2 = v.getId().getRefer2().trim();
					v.getId().setRefer2( v.getId().getRefer3().trim() ) ;
					v.getId().setRefer3( refer2 ) ;
					v.setProcesado(true);
				}
			});
			
			CodeUtil.putInSessionMap("ac_lstRecibosxMpago8N", lstRecibosxMpago8N);
			gvRecibosxMpago8N.dataBind();
			gvRecibosxMpago8N.setPageIndex(0);
			hdReciboMPago8N.setCaptionText( sTextoEncabezado);
			dwRecibosxMpago8N.setWindowState("normal");
			
			CodeUtil.refreshIgObjects(new Object[]{dwRecibosxMpago8N,hdReciboMPago8N,gvRecibosxMpago8N });
			
			return;
				
		}catch(Exception error){
			error.printStackTrace(); 
		}		
	}
	
	public void cerrarDwRecibosxMpago8N(ActionEvent ev){
		dwRecibosxMpago8N.setWindowState("hidden");
	}
	
/**************** 13. cargar los recibos por cambios realizados ***********************************/
	public void cargarRecibosxCambios(ActionEvent ev){
		String sListaRecibos = "",sLinkId;
		
		try{
			//cambios por pago de abonos y facturas de contado pagadas con moneda extranjera;
			sLinkId = ev.getComponent().getId();
			if(sLinkId.equals("lnkDetOECambios"))
				sListaRecibos = "ac_OtrosEgCambios";
			
			//cambios mixtos realizados.
			else if(sLinkId.equals("lnkIngCambiosOtMon"))
				sListaRecibos = "ac_lstRecCambiosOtrMoneda";
			
			if(m.get(sListaRecibos)!=null && !((ArrayList)m.get(sListaRecibos)).isEmpty()){
				m.put("lstDetalleCambios",(ArrayList)m.get(sListaRecibos));
				gvDetalleDCambios.setPageIndex(0);
				gvDetalleDCambios.dataBind();
				dwDetalleCambios.setWindowState("normal");				
			}			
		}catch(Exception error){
			error.printStackTrace();
		}		
	}
		
/******************** 12. cargar los recibos por tipos de egresos ************************************/	
	public void cargarRecibosxTipoEgreso(ActionEvent ev){
		String sIdLink = "",sListaRecibos = "",sHdDialog = "";
		
		try{
			//obtener el Id del link que se seleccionó.
			sIdLink = ev.getComponent().getId().trim();
			if(sIdLink.equals("lnkDetalleAbonos")){
				sListaRecibos = "lstIngresosAbono";
				sHdDialog = "Recibos por abonos a facturas de crédito";
			}
			else if(sIdLink.equals("lnkDetallePrimasReservas")){
				sListaRecibos = "lstIngresoPrimas";
				sHdDialog = "Recibos de pago a primas y reservas";
			}
			else if(sIdLink.equals("lnkDetalleOtrosIngresos")){
				sListaRecibos = "lstIngresoOtros";
				sHdDialog = "Recibos de pago por otros ingresos";
			}
			else if(sIdLink.equals("lnkDetalleIngresosEx")){
				sListaRecibos = "ac_RecibosxIngresosEx";
				sHdDialog = "Recibos de pago de Ingresos Extraordinarios";
			}	
			else if(sIdLink.equals("lnkDetalleFinancimiento")){
				sListaRecibos = "ac_RecibosxFinancimiento";
				sHdDialog = "Recibos de pago a Financiamientos";
			}
			else if(sIdLink.equals("lnkDetalleRecibosPMT")){
				sListaRecibos = "ac_RecibosxAnticipoPMT";
				sHdDialog = "Recibos de pago por anticipos PMT";
			}
			
		}catch(Exception error){
			error.printStackTrace(); 
		}finally{
			
			
			if(sListaRecibos.isEmpty()){
				lblValidarArqueo.setValue("No se ha podido cargar el detalle de los recibos");
				dwValidarArqueo.setWindowState("normal");
				CodeUtil.refreshIgObjects(dwValidarArqueo);
				return;
			}
			
			List<Vrecibosxtipoingreso> lstRecibos = (ArrayList<Vrecibosxtipoingreso>)CodeUtil.getFromSessionMap(sListaRecibos);
			
			if(lstRecibos == null || lstRecibos.isEmpty()){
				lblValidarArqueo.setValue("No se ha podido cargar el detalle de los recibos");
				dwValidarArqueo.setWindowState("normal");
				CodeUtil.refreshIgObjects(dwValidarArqueo);
				return;
			}
			
			CodeUtil.removeFromSessionMap("lstRecibosxIngresos");
			CodeUtil.putInSessionMap("lstRecibosxIngresos", lstRecibos);
			gvRecibosIngresos.dataBind();
			gvRecibosIngresos.setPageIndex(0);
			hdRecxTipoIng.setCaptionText(sHdDialog);
			dwRecibosxTipoIngreso.setWindowState("normal");
		}
	}
		
/******************** 11. cargar recibos para los egresos del día ************************************/
	@SuppressWarnings("rawtypes")
	public void cargarEgresos(int iCaid,String sCodcomp,String sCodsuc,String sMoneda,
								Date dtFechaArqueo,Date dtHoraInicio,Date dtHoraFin,
								Session sesion1, boolean ajustarMinimo){
		
		ArqueoCajaCtrl acCtrl = new ArqueoCajaCtrl();
		ArqueoControl acCtrl1 = new ArqueoControl();
		
		List lstRecibos = new ArrayList(),lstDev = new ArrayList();		
		double dTVentasPagBancos = 0,dTabonosPagBancos=0, dTPrimasPagBancos=0,dTOtrosEgresos =0,dTIngExPagBancos=0;
		double dTdevolucion = 0,dTotalxEgresos = 0, dTFinanPagBancos = 0;	
		double dCoh=0,dCon=0,dCo8=0,dCrh=0,dCrn=0,dCr8=0,dPrh=0,dPrn=0,dPr8=0, dFNh=0,dFNn=0,dFN8=0;
		List lstCoh =new ArrayList(),lstCon = new ArrayList(),lstCo8 = new ArrayList();
		List lstCrh =new ArrayList(),lstCrn =new ArrayList(),lstCr8 = new ArrayList();	
		List lstPrh =new ArrayList(),lstPrn =new ArrayList(),lstPr8 = new ArrayList();
		List lstFNh =new ArrayList(),lstFNn =new ArrayList(),lstFN8 = new ArrayList();
		
		List lstPMh = new ArrayList(), lstPMn =new ArrayList(), lstPM8 = new ArrayList();
		double dPMh = 0, dPMn = 0, dPM8 = 0;
		double dTPMTPagosBanco = 0 ;
		
		String sMpago = "", sTiporec = "";
		double dMonto = 0;
		Divisas dv = new Divisas();
		
		try{			
			//inicia = System.currentTimeMillis();
			lstRecibos = acCtrl1.obtenerRecpagBanco(iCaid, sCodsuc, sCodcomp, sMoneda, 
												dtFechaArqueo,dtHoraInicio,dtHoraFin);			

			
			if(lstRecibos!= null && lstRecibos.size()>0){
				for(int i=0; i<lstRecibos.size();i++){
					Vrecibosxtipoegreso v = (Vrecibosxtipoegreso)lstRecibos.get(i);
					v.setMonto(v.getId().getMonto());
					sMpago = v.getId().getMpago().trim();
					sTiporec = v.getId().getTiporec().trim();
					dMonto = dv.roundDouble(v.getId().getMonto().doubleValue());
					
					//--------------------- pago de ventas.
					if(sTiporec.equals("CO")){
//						if(v.getId().getFmoneda().equals(sMoneda)){
							if(sMpago.equals(MetodosPagoCtrl.TARJETA)){
								lstCoh.add(v);
								dCoh += dMonto;
							}else
							if(sMpago.equals(MetodosPagoCtrl.DEPOSITO)){
								lstCon.add(v);
								dCon += dMonto;							
							}else
							if(sMpago.equals(MetodosPagoCtrl.TRANSFERENCIA)){
								lstCo8.add(v);
								dCo8 +=dMonto;
							}
//						}
					}else //-------------- pago de abonos.
					if(sTiporec.equals("CR")){
						if(sMpago.equals(MetodosPagoCtrl.TARJETA)){
							lstCrh.add(v);
							dCrh += dMonto;
						}else
						if(sMpago.equals(MetodosPagoCtrl.DEPOSITO)){
							lstCrn.add(v);
							dCrn += dMonto;							
						}else
						if(sMpago.equals(MetodosPagoCtrl.TRANSFERENCIA)){
							lstCr8.add(v);
							dCr8 +=dMonto;
						}					
					}else //-------------- pago de primas y reservas.
					if(sTiporec.equals("PR")){
						if(sMpago.equals(MetodosPagoCtrl.TARJETA)){
							lstPrh.add(v);
							dPrh += dMonto;
						}else
						if(sMpago.equals(MetodosPagoCtrl.DEPOSITO)){
							lstPrn.add(v);
							dPrn += dMonto;							
						}else
						if(sMpago.equals(MetodosPagoCtrl.TRANSFERENCIA)){
							lstPr8.add(v);
							dPr8 +=dMonto;
						}					
					}else //-------------- Financimiento
					if(sTiporec.equals("FN")){
						if(sMpago.equals(MetodosPagoCtrl.TARJETA)){
							lstFNh.add(v);
							dFNh += dMonto;
						}else
						if(sMpago.equals(MetodosPagoCtrl.DEPOSITO)){
							lstFNn.add(v);
							dFNn += dMonto;							
						}else
						if(sMpago.equals(MetodosPagoCtrl.TRANSFERENCIA)){
							lstFN8.add(v);
							dFN8 +=dMonto;
						}					
					}
					else //-------------- Anticipos por PMT
					if(sTiporec.equals("PM")){
						if(sMpago.equals(MetodosPagoCtrl.TARJETA)){
							lstPMh.add(v);
							dPMh += dMonto;
						}else
						if(sMpago.equals(MetodosPagoCtrl.DEPOSITO)){
							lstPMn.add(v);
							dPMn += dMonto;							
						}else
						if(sMpago.equals(MetodosPagoCtrl.TRANSFERENCIA)){
							lstPM8.add(v);
							dPM8 +=dMonto;
						}					
					}
				}
				//-----------------------------------------------------------------------------------
				//-------- cargar los datos para los ingresos por Recibos x ingresos extraordinarios
				//-----------------------------------------------------------------------------------
				double totalIngEx = 0,dIexH=0,dIexN=0,dIex8=0;
				List<Vrecibosxtipoegreso> lstIexH =  new ArrayList<Vrecibosxtipoegreso> ();
				List<Vrecibosxtipoegreso> lstIexN =  new ArrayList<Vrecibosxtipoegreso> ();
				List<Vrecibosxtipoegreso> lstIex8 =  new ArrayList<Vrecibosxtipoegreso> ();

				lstRecibos = acCtrl1.obtenerRecibosxTipo(iCaid, sCodsuc, sCodcomp, sMoneda,"EX",
											dtFechaArqueo,dtHoraInicio,dtHoraFin);
				 
				if(lstRecibos!= null && lstRecibos.size()>0){
					for(int i=0; i<lstRecibos.size();i++){
						Vrecibosxtipoegreso v = (Vrecibosxtipoegreso)lstRecibos.get(i);
						v.setMonto(v.getId().getMonto());
						
						totalIngEx += v.getId().getMonto().doubleValue();
						if(v.getId().getRmoneda().equals(sMoneda)){
							if(v.getId().getMpago().equals(MetodosPagoCtrl.TARJETA)){
								lstIexH.add(v);
								dIexH += v.getId().getMonto().doubleValue();
							}else
							if(v.getId().getMpago().equals(MetodosPagoCtrl.DEPOSITO)){
								lstIexN.add(v);
								dIexN += v.getId().getMonto().doubleValue();							
							}else
							if(v.getId().getMpago().equals(MetodosPagoCtrl.TRANSFERENCIA)){
								lstIex8.add(v);
								dIex8 +=  v.getId().getMonto().doubleValue();
							}
						}
					}
					m.put("ac_lstriexH", lstIexH);
					m.put("ac_lstriexN", lstIexN);
					m.put("ac_lstriex8", lstIex8);
				}else{
					m.remove("ac_lstriexH");
					m.remove("ac_lstriexN");
					m.remove("ac_lstriex8");
				}				
				//------- OBJETOS DE EGRESOS----//
				lblegIexH.setValue(dIexH);
				lblegIexN.setValue(dIexN);
				lblegIex8.setValue(dIex8);
				dTIngExPagBancos  += (dIexH + dIexN + dIex8);
				lblegTotalIex.setValue(dv.roundDouble(dTIngExPagBancos));
				
				//------------------- devoluciones de ventas pagadas en banco.
				//inicia = System.currentTimeMillis();
				lstDev = acCtrl1.obtenerRecibosxdevolucion(true,iCaid, sCodsuc, sCodcomp,"DCO",sMoneda,
															dtFechaArqueo,dtHoraInicio,dtHoraFin);
				 
				if(lstDev != null && lstDev.size()>0){				
					for(int i=0; i<lstDev.size(); i++){					
						Vreciboxdevolucion v = (Vreciboxdevolucion)lstDev.get(i);
						dTdevolucion = dv.roundDouble(v.getId().getMonto().doubleValue());
						sMpago = v.getId().getMpago();
						
						if(v.getId().getFechadev().intValue() == v.getId().getFechafact().intValue() && 
						 	v.getId().getMontodev().compareTo(v.getId().getMontofact()) == 0)
							continue;
						
						if(sMpago.equals(MetodosPagoCtrl.TARJETA)){
							dCoh -= dTdevolucion;
						}else
						if(sMpago.equals(MetodosPagoCtrl.DEPOSITO)){
							dCon -= dTdevolucion;
						}else
						if(sMpago.equals(MetodosPagoCtrl.TRANSFERENCIA)){
							dCo8 -= dTdevolucion;
						}
					}				
				}
			}		
			dCoh = Divisas.roundDouble(dCoh);
			dCo8 = Divisas.roundDouble(dCo8);
			dCon = Divisas.roundDouble(dCon);
			dCrh = Divisas.roundDouble(dCrh);
			dCr8 = Divisas.roundDouble(dCr8);
			dCrn = Divisas.roundDouble(dCrn);
			dPrh = Divisas.roundDouble(dPrh);
			dPr8 = Divisas.roundDouble(dPr8);
			dPrn = Divisas.roundDouble(dPrn);
			dFNh = Divisas.roundDouble(dFNh);
			dFN8 = Divisas.roundDouble(dFN8);
			dFNn = Divisas.roundDouble(dFNn);
			dPMh = Divisas.roundDouble(dPMh);
			dPM8 = Divisas.roundDouble(dPM8);
			dPMn = Divisas.roundDouble(dPMn);
			
			dTVentasPagBancos += (dCoh+dCo8+dCon);
			dTabonosPagBancos += (dCrh+dCr8+dCrn);
			dTPrimasPagBancos += (dPrh+dPr8+dPrn);
			dTFinanPagBancos  += (dFNh+dFN8+dFNn);
			dTPMTPagosBanco   +=  (dPMh+dPM8+dPMn);
			
			dTotalxEgresos += Divisas.roundDouble(dTVentasPagBancos) + Divisas.roundDouble(dTabonosPagBancos);
			dTotalxEgresos += Divisas.roundDouble(dTPrimasPagBancos) + Divisas.roundDouble(dTFinanPagBancos);
			dTotalxEgresos += dTIngExPagBancos;
			dTotalxEgresos += dTPMTPagosBanco;
			
			lblTotalVtsPagBanco.setValue(dTVentasPagBancos);
			lblTotalAbonoPagBanco.setValue(dTabonosPagBancos);
			lblTotalPrimasPagBanco.setValue(dTPrimasPagBancos);
			lblTotalFinanPagBanco.setValue(dTFinanPagBancos);
			lblTotalPmtPagoEnBanco.setValue(dTPMTPagosBanco);
			
			lblVtsTCredito.setValue(dCoh);
			lblVtsTransBanc.setValue(dCo8);
			lblVtsDDbanco.setValue(dCon);

			lblAbonoTCredito.setValue(dCrh);
			lblAbonoTransBanc.setValue(dCr8);
			lblAbonoDDbanco.setValue(dCrn);
			
			lblPrimasTCredito.setValue(dPrh);
			lblPrimasTransBanc.setValue(dPr8);
			lblPrimasDDbanco.setValue(dPrn);
			
			lblFinanTCredito.setValue(dFNh);
			lblFinanTransBanc.setValue(dFN8);
			lblFinanDbanco.setValue(dFNn);
			
			lblpmtTarjetaCredito.setValue(dPMh);
			lblpmtDepositoDirectoBanco.setValue(dPMn);
			lblpmtTransferenciaBancaria.setValue(dPM8);
			
			CodeUtil.refreshIgObjects(new Object[]{lblpmtTarjetaCredito, lblpmtDepositoDirectoBanco, lblpmtTransferenciaBancaria, lblTotalPmtPagoEnBanco });
			
			m.put("ac_recContTarjCred",lstCoh);
			m.put("ac_recContTransBanco",lstCo8);
			m.put("ac_recContDepositoBanco",lstCon);
			m.put("ac_recAbonosTarCred",lstCrh);
			m.put("ac_recAbonosTransBanco",lstCr8);
			m.put("ac_recAbonosDepositoBanco",lstCrn);
			m.put("ac_recPrimasTarCred",lstPrh);
			m.put("ac_recPrimasTransBanco",lstPr8);
			m.put("ac_recPrimasDepositoBanco",lstPrn);
			m.put("ac_recFinanTarCred",lstFNh);
			m.put("ac_recFinanTransBanco",lstFN8);
			m.put("ac_recFinanDepositoBanco",lstFNn);
			
			m.put("ac_recPMTTarCred",lstPMh);
			m.put("ac_recPMTTransBanco",lstPM8);
			m.put("ac_recPMTDepositoBanco",lstPMn);
			
			//---------------------------------------------------------------------
			//inicia = System.currentTimeMillis();
			lstRecibos = acCtrl1.cargarDetCambio(iCaid, sCodcomp, sCodsuc, sMoneda,
												dtFechaArqueo,dtHoraInicio,dtHoraFin);
 
			List<Vdetallecambiorecibo> lstRecca = new ArrayList<Vdetallecambiorecibo>();
			double dtc = 0;
			
			String sInclude="";
			String sInclude1="";
			
			if(lstRecibos!=null && lstRecibos.size()>0){
				for(int i=0;i<lstRecibos.size();i++){
					Vdetallecambiorecibo v = (Vdetallecambiorecibo)lstRecibos.get(i);
					v.setCambio(v.getId().getCambio());
					
					
					if(v.getId().getTiporec().equals("CO")){
						if(v.getId().getMonedacamb().compareTo(sMoneda) == 0 && v.getId().getCambio().doubleValue() > 0){
							
							if(v.getId().getCantmon() == 1 && v.getId().getMonedarec().compareTo(sMoneda) != 0 ){
									dtc += dv.roundDouble(v.getId().getCambio().doubleValue());
									lstRecca.add(v);
									sInclude1 += v.getId().getNumrec()+",";
							}
							//&& caso en que el monto del cambio supere al monto del pago en efectivo (ambos en la misma moneda) 						
							else if(v.getId().getCantmon() == 2  && !sInclude1.contains(String.valueOf(v.getId().getNumrec()))){	
								
								if(	v.getId().getMonedarec().compareTo(sMoneda) == 0 &&
									v.getId().getMonedafac().compareTo(sMoneda) != 0 ){
									sInclude1 += v.getId().getNumrec()+",";
									continue;
								} 
								if( v.getId().getMonedarec().compareTo(sMoneda) == 0
									&& v.getId().getMonedarec().compareTo(v.getId().getMonedacamb())== 0
									&& v.getId().getCambio().compareTo(v.getId().getMonto()) == 1 ){
								
									v.getId().setCambio(v.getId().getCambio().subtract(v.getId().getMonto()));
									v.setCambio(v.getId().getCambio());
									
									dtc += dv.roundDouble(v.getId().getCambio().doubleValue());
									lstRecca.add(v);

									sInclude1 += v.getId().getNumrec()+",";
									
								}else{
									if(
										v.getId().getMonedarec().compareTo(sMoneda) != 0 &&
										v.getId().getMonedarec().compareTo(v.getId().getMonedafac()) == 0 && 
										v.getId().getCambio().doubleValue() > 0 ){
										 
										Session sesion = HibernateUtilPruebaCn.currentSession();
										
										Criteria cr = sesion.createCriteria(Recibodet.class);
										cr.add(Restrictions.eq("id.caid",iCaid ))
											.add(Restrictions.eq("id.mpago", MetodosPagoCtrl.EFECTIVO ))
											.add(Restrictions.eq("id.codcomp",sCodcomp ))
											.add(Restrictions.eq("id.codsuc",sCodsuc ))
											.add(Restrictions.eq("id.numrec",v.getId().getNumrec() ))
											.setProjection(Projections.countDistinct("id.moneda") );
										int iCant = Integer.parseInt(String.valueOf(cr.uniqueResult()));
										 
										try{HibernateUtilPruebaCn.closeSession(sesion);}catch(Exception e){e.printStackTrace();}
										 
										if(iCant == 1){
											dtc += dv.roundDouble(v.getId().getCambio().doubleValue());
											lstRecca.add(v);
											sInclude1 += v.getId().getNumrec()+",";
										}
									}
								}
							}
						}
					}else{
						if(v.getId().getCantmon() == 1){
							dtc += dv.roundDouble(v.getId().getCambio().doubleValue());
							lstRecca.add(v);
							sInclude += v.getId().getNumrec()+",";
						}else{
							if(v.getId().getCantmon() == 2 && !sInclude.contains(String.valueOf(v.getId().getNumrec()))){
								sInclude += v.getId().getNumrec()+",";
								dtc += dv.roundDouble(v.getId().getCambio().doubleValue());
								lstRecca.add(v);
							}
						}
					}
				}	
				if(ajustarMinimo){
					lblOEcambios.setValue(dtc);	
					m.put("ac_CambiosOE", dtc);
					dTOtrosEgresos += dtc;
				}else{
					lblOEcambios.setValue(dtc);	
					m.put("ac_CambiosOE", dtc);
					dTOtrosEgresos += dtc;
				}
				//m.put("ac_CambiosOE", dtc);
				m.put("ac_OtrosEgCambios", lstRecca);				
				
			}else{
				lblOEcambios.setValue("0.00");
				m.put("ac_CambiosOE", 0);
				m.remove("ac_OtrosEgCambios");
			}
			//---- cargar los recibos pagados con distinta moneda de la factura y del arqueo ------//
			ReciboCtrl rcCtrl = new ReciboCtrl();
			double dTotalErme = 0, dTotalDeverme =0;
			lblOEmonEx.setValue("0.00");
			m.remove("ac_egreRecibosxmonex");
			
			lstRecibos = new ArrayList();
			Recibo r = null;
			
			List<Vmonedafactrec> lstRecTmp = acCtrl1.obtenerEgresosRecMonEx(iCaid, sCodcomp, sCodsuc, sMoneda,
					dtFechaArqueo,dtHoraInicio,dtHoraFin);

			if(lstRecTmp!=null && lstRecTmp.size()>0){
				
				String sRecibosIn="";
				String sCompara = "";
				String sComparaDevolucion = "";
				
				for(int i=0; i<lstRecTmp.size();i++){
					Vmonedafactrec vm = lstRecTmp.get(i);

					if( vm.getId().getMpago().compareTo(MetodosPagoCtrl.EFECTIVO ) != 0 ){
						sCompara = vm.getId().getNumrec() + ":"
								+ vm.getId().getMpago() + ":"
								+ vm.getId().getMonto().toString() + ":"
								+ vm.getId().getRmoneda() + ":"
								+ vm.getId().getRefer1().trim() + ":"
								+ vm.getId().getRefer2().trim() + ":"
								+ vm.getId().getRefer3().trim() + ":"
								+ vm.getId().getRefer4().trim() + ",";
						if(sRecibosIn.trim().contains(sCompara))
							continue;
						else
							sRecibosIn += sCompara;
					}
					
					
					//&& buscar devolucion total para la linea del metodo de pago.
					r = rcCtrl.getReciboDevolucionxRecibo(iCaid,vm.getId().getNumrec(),sCodcomp,sCodsuc,"DCO", dtFechaArqueo);
					
					if(r != null && r.getMontoapl().compareTo(vm.getId().getMontoapl()) == 0){
						continue;
					}
					if(vm.getId().getMpago().compareTo(MetodosPagoCtrl.EFECTIVO ) != 0 &&
							vm.getId().getCambio().compareTo(BigDecimal.ZERO) == 1 
							&& vm.getId().getRmoneda().compareTo(vm.getId().getFmoneda()) == 0 ){
						continue;
					}
					
					//&& ======= Validar que el metodo de pago no acepte cambio y que no exceda el montoapl.
					if (vm.getId().getMpago().compareTo(MetodosPagoCtrl.EFECTIVO ) != 0
							&& vm.getId().getEquiv().compareTo(
									vm.getId().getMontoapl()) == 1) {

						vm.getId().setDomeaplicado(vm.getId().getMontoapl());
						vm.getId().setForarecibido(
								Divisas.roundBigDecimal(vm.getId().getMontoapl()
										.multiply(vm.getId().getTasa())));
					}
					
					if(vm.getId().getDomeaplicado().doubleValue()!= 0 && vm.getId().getForarecibido().doubleValue()!= 0){
						vm.setMonto(vm.getId().getForarecibido());
						vm.setEquiv(vm.getId().getDomeaplicado());
						vm.setIngresoegreso((vm.getId().getDomeaplicado()));
						dTotalErme += dv.roundDouble(vm.getEquiv().doubleValue());
 
					}else{
						vm.setMonto(vm.getId().getMonto());
						vm.setEquiv(vm.getId().getEquiv());
						vm.setIngresoegreso(vm.getId().getEquiv());
						dTotalErme += dv.roundDouble(vm.getId().getEquiv().doubleValue());
					}		
					lstRecibos.add(vm);
					
					//&& =============== validar si en el recibo se pago mas de una factura	
						//Ajuste hecho por lfonseca 2019-08-27
					    //se agrego el parametro moneda
						List<Recibo> recibos = ReciboCtrl.getDevolucionPorRecibo(
												iCaid, vm.getId().getNumrec(), 
												sCodcomp, dtFechaArqueo, sMoneda);
						
						if (recibos != null && !recibos.isEmpty()){
						
							for (Recibo r1 : recibos) {
								
								String comparatmp = "@" + r1.getId().getCaid()
										+ ":" + r1.getId().getCodcomp().trim()
										+ ":" + r1.getId().getTiporec().trim()
										+ ":" + r1.getId().getNumrec();
								if(  !sComparaDevolucion.contains(comparatmp) ){
									dTotalErme -= r1.getMontoapl().doubleValue();
									sComparaDevolucion += comparatmp ;
								}
							}
						}
				}
				
			}
			
			//&& ======== Cargar los pagos en moneda de arqueo por devoluciones en facturas en otra moneda
			List<Vreciboxdevolucion> lstDevs = acCtrl1.obtenerRecxDevmonex4(
									false, iCaid, sCodsuc, sCodcomp, sMoneda, "DCO",
									dtFechaArqueo, dtHoraInicio, dtHoraFin);
						
			if(lstDevs != null && lstDevs.size() > 0){
				dTotalDeverme = 0;
				for (Vreciboxdevolucion v : lstDevs) 
					dTotalErme +=  Divisas.roundBigDecimal(v.getId().getMonto()).doubleValue();
			}
			
			//&& ==== devoluciones en un solo pago y moneda a facturas con pago mixto.
			lstDevs = acCtrl1.obtenerDevParcialMonForanea(
												iCaid, sCodsuc, sCodcomp, sMoneda, "DCO",
												dtFechaArqueo, dtHoraInicio, dtHoraFin);
			if (lstDevs != null && lstDevs.size() > 0) {
				dTotalDeverme = 0;
				for (Vreciboxdevolucion v : lstDevs)
					dTotalErme += Divisas.roundBigDecimal(v.getId().getMonto()).doubleValue();
			}
			
			lblOEmonEx.setValue(dTotalErme);
			m.put("ac_egreRecibosxmonex", lstRecibos);
			dTOtrosEgresos += dv.roundDouble(dTotalErme);
			
			//&& ========= cargar las devoluciones de facturas pagadas en diferente moneda de dias anteriores//
			List<Vreciboxdevolucion> lstDev2 = acCtrl1.obtenerRecxDevmonex2
					(false, iCaid, sCodsuc, sCodcomp, sMoneda, "DCO", 
					 dtFechaArqueo,dtHoraInicio,dtHoraFin);
		
			BigDecimal bdTotalDevs = BigDecimal.ZERO;
			for (Vreciboxdevolucion v : lstDev2) 
				bdTotalDevs = bdTotalDevs.add(v.getId().getMonto());
			
			dTotalErme += bdTotalDevs.doubleValue();
			
			lblOEmonEx.setValue(dTotalErme);
			dTOtrosEgresos += dv.roundDouble(bdTotalDevs.doubleValue());
			
			//&& ========= Cargar los montos de pagos a facturas en moneda disitnta al arqueo que tienen devolucion
			//&& ========= y que la factura original fue pagada en la moneda del arqueo
			
			if(sMoneda.equals("COR")){
				List<BigDecimal>lstMontos = acCtrl.obtenerRecxDevmonFor(
								false,iCaid, sCodsuc, sCodcomp, sMoneda, "DCO", 
								dtFechaArqueo, dtHoraInicio, dtHoraFin);
				
				double dTotalMontos = 0.0;
				if (lstMontos != null && lstMontos.size() > 0) {
					for (BigDecimal v : lstMontos)
						dTotalMontos += v.doubleValue();
				}
				
				lblOEmonEx.setValue(dTotalErme + dTotalMontos);
				//m.put("ac_ingRecibosxmonex", lstRecibos);				
				dTOtrosEgresos += dTotalMontos;
			}
			//-------------------------------------------------------------------------------
			
			//------ Cargar el monto por salidas de caja, sea por salida efectivo o cambio de cheque.
			//------ Solo Salidas procesadas enlazadas por caja, compania, sucursal, moneda, fecha.
			SalidasCtrl sCtrl = new SalidasCtrl();
			double dMontoSal = 0,dMontosal5 = 0;
			List lstSal = null;
			
			//inicia = System.currentTimeMillis();
			lstSal = sCtrl.obtenerSolicitudSalidas(iCaid, sCodcomp, sCodsuc, 0, 0, sMoneda, "",0,
													dtFechaArqueo, dtHoraInicio,dtHoraFin);

			if(lstSal!=null && lstSal.size()>0){
				for(int i=0;i<lstSal.size();i++){
					Vsalida v = (Vsalida)lstSal.get(i);
					v.setMonto(v.getId().getMonto());
					
					dMontoSal += dv.roundDouble(v.getId().getMonto().doubleValue());
					if(v.getId().getOperacion().equals(MetodosPagoCtrl.EFECTIVO ))
						dMontosal5 += dv.roundDouble(v.getId().getMonto().doubleValue());
				}
				dMontoSal = dv.roundDouble(dMontoSal);
				dTOtrosEgresos += dMontosal5;
				lblOEsalidas.setValue(dMontoSal);
				m.put("ac_egreRecibosSalidas", lstSal);
			}else{
				lblOEsalidas.setValue("0.00");
				m.remove("ac_egreRecibosSalidas");
			}
			dTotalxEgresos += dv.roundDouble(dTOtrosEgresos);
			lblTotalOtrosEgresos.setValue(dTOtrosEgresos);
			m.put("ac_dTotalxEgresos",dTotalxEgresos);
			//-------------------------------------------------------------------------//
			lblTotalEgresos.setValue(dTotalxEgresos);
			m.put("ac_TotalxEgresos", dTotalxEgresos); // guardar el total de Egresos.
					
		}catch(Exception error){
			LogCajaService.CreateLog("cargarEgresos", "ERR", error.getMessage());
		} finally {
			HibernateUtilPruebaCn.closeSession();
		}
	}	
/******************** 10. cargar datos para el detalle del recibo ************************************/
	public void mostrarDetalleRecibo(int iNumrec,int iCaid,String sCodcomp,String sCodsuc, String tiporec ){
		ReciboCtrl recCtrl = new ReciboCtrl();		
		
		try{
			Recibo recibo = recCtrl.obtenerRegRecibo(iNumrec,iCaid,sCodcomp,sCodsuc, tiporec  );
			if(recibo!=null){
				
				//-------- llenar la cabecera del recibo ---------- //
				txtHoraRecibo.setValue(recibo.getHora()+"");
				txtNoRecibo.setValue(iNumrec);
				txtDRCodCli.setValue(recibo.getCodcli() + " (Código)");
				txtDRNomCliente.setValue(recibo.getCliente()+ " (Nombre)");
				txtMontoAplicar.setValue(recibo.getMontoapl());
				txtMontoRecibido.setValue(recibo.getMontorec());
				txtConcepto.setValue(recibo.getConcepto());
				
				//-------- leer numero de batch del recibo---------//
//				int iNoBatch = recCtrl.leerNoBatchRecibo(iCaid, sCodsuc, sCodcomp, iNumrec, "R",recibo.getId().getTiporec());
//				txtNoBatch.setValue(iNoBatch);
				
				
				//&& ============== Numeros de batch y documento grabados en JdEdward's
				Recibojde[] recibojde = com.casapellas.controles.ReciboCtrl.leerEnlaceReciboJDE(
						iCaid, sCodsuc,
						sCodcomp, iNumrec,
						recibo.getId().getTiporec());
				
				String sNumeros   = "";
				String sNumbatchs = "";
				for (Recibojde rj : recibojde) {
					sNumeros += " " + rj.getId().getRecjde();
					sNumbatchs += " " + rj.getId().getNobatch();
				}
				
				txtNoBatch.setValue( sNumbatchs );
				
				//---------  leer detalle de recibo -------------- //
				List lstDetalleRecibo = recCtrl.leerDetalleRecibo(iCaid, sCodsuc, sCodcomp, iNumrec,recibo.getId().getTiporec());				
				MetodosPagoCtrl metCtrl = new MetodosPagoCtrl();  //poner descripcion a metodos
				Recibodet recibodet = null;
				for(int m = 0; m < lstDetalleRecibo.size();m++){
					recibodet = (Recibodet)lstDetalleRecibo.get(m);
					recibodet.getId().setMpago(metCtrl.obtenerDescripcionMetodoPago(recibodet.getId().getMpago().trim()));
				}
				m.put("lstDetalleRecibo", lstDetalleRecibo);
				gvDetalleRecibo.dataBind();
				
				//---------  leer detalle de cambio del recibo -------------- //
				Cambiodet[] cambio = recCtrl.leerDetalleCambio(iCaid, sCodsuc, sCodcomp, iNumrec);				
				String sCambio = "";
				if(cambio!=null){
					for(int i = 0; i < cambio.length;i++){
						sCambio = sCambio + cambio[i].getId().getMoneda() + " " + cambio[i].getCambio() + "<br/>";
					}
				}
				txtDetalleCambio.setValue(sCambio);
				
				//---------  leer detalle de Facturas aplicadas al recibo -------------- //	
				gvFacturasRecibo.setRendered(true);
				lblNoFactura2.setValue("No. Factura");
				lblTipofactura2.setValue("Tipo Fac.");
				lblUnineg2.setValue("Unidad de Negocios");
				lblMoneda2.setValue("Moneda");
				lblFecha23.setValue("Fecha");
				lblPartida23.setValue("Partida");	
				coNoFactura2.setRendered(true);
				
				List lstFacturasRecibo = new ArrayList();
				String sTiporec = recibo.getId().getTiporec();
				
				if(sTiporec.equals("CR"))				
					lstFacturasRecibo = recCtrl.leerFacturasReciboCredito(
							iCaid, sCodcomp, iNumrec, sTiporec, recibo.getCodcli() );
				
				else if(sTiporec.equals("CO"))
					lstFacturasRecibo = recCtrl.leerFacturasRecibo(iCaid, sCodcomp, 
							iNumrec,sTiporec, recibo.getCodcli() );
				
				else if(sTiporec.equals("FN"))
					lstFacturasRecibo = recCtrl.leerFacturasReciboFN(iCaid, sCodcomp, 
							iNumrec, sTiporec, recibo.getCodcli() );
				
				else if(sTiporec.equals("PR")){
					lstFacturasRecibo = recCtrl.leerDatosBien(iNumrec,iCaid, sCodcomp,sCodsuc);
					lblNoFactura2.setValue("");
					lblTipofactura2.setValue("T.Producto");
					lblUnineg2.setValue("Marca");
					lblMoneda2.setValue("Modelo");
					lblFecha23.setValue("Referecia");
					lblPartida23.setValue("");	
					coNoFactura2.setRendered(false);
				}
				else if(sTiporec.equals("EX"))
					gvFacturasRecibo.setRendered(false);
				
				
				// && ================ Mostrar los paneles de facturas o de datos del PMT
				boolean panelFacturas = recibo.getId().getTiporec().compareTo("PM") != 0;
				
				pnlDatosFacturas.setRendered(panelFacturas);
				pnlDatosAnticiposPMT.setRendered(!panelFacturas);
			 
				
				//&& ================ Buscar el detalle del contrato pagado en el recibo
				if(!panelFacturas){
					
					detalleContratoPmt = PlanMantenimientoTotalCtrl.detalleContratoPMT(recibo);
					
					if( detalleContratoPmt == null )
						detalleContratoPmt = new ArrayList<HistoricoReservasProformas>();
					 
					if( !detalleContratoPmt.isEmpty() ){
						
						HistoricoReservasProformas hpp = detalleContratoPmt.get(0);
						
						Object[] dtaContrato = PlanMantenimientoTotalCtrl.queryNumberContrato( hpp.getNumeroproforma(), hpp.getCodcli(), hpp.getCodcomp(), "");
						
						if(dtaContrato != null){
							hpp.setPropuesta( String.valueOf( dtaContrato[1] ) ) ;
							hpp.setChasis( String.valueOf( dtaContrato[3] ) );
							hpp.setMotor( String.valueOf( dtaContrato[4] ) );
							hpp.setHechopor( String.valueOf( dtaContrato[7] ) );
							hpp.setFechacontrato( String.valueOf( dtaContrato[5] ) );
						}
					}
					
					CodeUtil.putInSessionMap("ac_detalleContratoPmt", detalleContratoPmt);
					gvDetalleContratoPmt.dataBind();
					
				}else{
					m.put("lstFacturasRecibo", lstFacturasRecibo);
					gvFacturasRecibo.dataBind();
					dwDetalleRecibo.setWindowState("normal");
				}

				dwDetalleRecibo.setWindowState("normal");
			}
			
		}catch(Exception error){ 
			error.printStackTrace();
			dwDetalleRecibo.setWindowState("hidden");
		}		
	}
	
/***************** 9. Cargar el detalla del recibo seleccionado *******************************/
	public void mostarDetalleRecibo(ActionEvent ev){
	
		try{
			
			RowItem ri = (RowItem) ev.getComponent().getParent().getParent();
			final Vrecibosxtipoingreso rec = (Vrecibosxtipoingreso) DataRepeater.getDataRow(ri);
			
			
			RowItem riRecibo = (RowItem)ev.getComponent().getParent().getParent();
			List lstFilaRec = riRecibo.getCells();
			Cell celdaCodrec = (Cell)lstFilaRec.get(CodRecibo);
			HtmlOutputText hoCodrec = (HtmlOutputText)celdaCodrec.getChildren().get(0);
			int iNumrec = Integer.parseInt(hoCodrec.getValue().toString());
			
			String sCodcomp = ddlFiltroCompania.getValue().toString(); //código de la compañía.
			List cajas = (ArrayList)m.get("lstCajas");
			int iCaid = ((Vf55ca01)cajas.get(0)).getId().getCaid();   //código de la caja.
			String sCodsuc = ((Vf55ca01)cajas.get(0)).getId().getCaco(); //código de la sucursal.
			
			mostrarDetalleRecibo(iNumrec, iCaid, sCodcomp, sCodsuc, rec.getId().getTiporec() );
			
		}catch(Exception error){
			error.printStackTrace(); 
		}		
	}
	
/*********** 7. Limpiar los objetos por cambios en filtros moneda y compañía  ***********************/
	public void limpiarObj(){
		try{
			lblFechaHoraArqueo.setValue("");
			lblEthoraArqueo.setValue("");
			lbletMsgArqueoPrevio.setStyle("visibility: hidden ");
			lblMsgArqueoPrevio.setStyle("visibility: hidden");
			lblMsgArqueoPrevio.setValue("");
			
			// -----  INGRESOS(+) -------//
			lblVentasTotales.setValue("0.00");
			lblTotalDevoluciones.setValue("0.00");
			lblTotalVentasNetas.setValue("0.00");
			lblTotalAbonos.setValue("0.00");
			lblTotalPrimas.setValue("0.00");
			lblTotalOtrosIngresos.setValue("0.00");
			lblMsgNoRegistros.setValue("");
			lblTotaIngresos.setValue("0.00");
			lblTotalIngRecxmonex.setValue("0.00");
			lblTotalVtsCredito.setValue("0.00");
			lblCambiosOtraMon.setValue("0.00");
			
			m.remove("lstFacturasxVentas"); 	
			m.remove("lstFacturasxDevolucion");					
			m.remove("lstTodasFacturas");
			m.remove("lstIngresosAbono");
			m.remove("lstIngresoOtros");
			m.remove("lstIngresoPrimas");
			m.remove("ac_ingRecibosxmonex");
			m.remove("arqueoFacturasCredito");
			m.remove("ac_lstRecxcambiosmixtos");
			m.remove("ac_lstriexH");
			m.remove("ac_lstriexN");
			m.remove("ac_lstriex8");
			
			
			// -----  EGRESOS(+) -------//	
			lblVtsTransBanc.setValue("0.00");
			lblVtsTCredito.setValue("0.00");
			lblVtsDDbanco.setValue("0.00");			
			lblTotalVtsPagBanco.setValue("0.00");			
			lblAbonoTCredito.setValue("0.00");			
			lblAbonoTransBanc.setValue("0.00");
			lblAbonoDDbanco.setValue("0.00");
			lblTotalAbonoPagBanco.setValue("0.00");			
			lblOEcambios.setValue("0.00");
			lblTotalOtrosEgresos.setValue("0.00");
			lblTotalEgresos.setValue("0.00");
			lblOEmonEx.setValue("0.00");
			lblegIexH.setValue("0.00");
			lblegIexN.setValue("0.00");
			lblegIex8.setValue("0.00");
			lblegTotalIex.setValue("0.00");
						
			m.remove("dtotalVtsCredito");			
			m.remove("lstFacturasRegistradas");	
			m.remove("ac_recContTarjCred"); 	
			m.remove("ac_recContTransBanco");
			m.remove("ac_recContDepositoBanco");	
			m.remove("ac_recAbonosTarCred");
			m.remove("ac_recAbonosTransBanco");
			m.remove("ac_recAbonosDepositoBanco");
			m.remove("ac_OtrosEgCambios");
			m.remove("ac_cantFactCo");	
			m.remove("ac_cantFactCr");
			m.remove("ac_egreRecibosxmonex");
			
			//calculo del deposito de caja.
			m.remove("ac_TotalxIngresos");			
			m.remove("ac_TotalxEgresos");			
			m.remove("ac_CDCefectivoneto");
			m.remove("ac_CDCMontoMinimo");
			m.remove("ac_totalcheques");
			
			lblCDC_efectnetoRec.setValue("0.00");
			lblCDC_pagocheques.setValue("0.00");
			lblCDC_montominimo.setValue("0.00");
			lblCDC_depositoSug.setValue("0.00");
			lblCDC_depositoFinal.setValue("0.00");
			lblCDC_SobranteFaltante.setValue("0.00");
			lblet_SobranteFaltante.setValue("Sobrante/Faltante");
			lblet_SobranteFaltante.setStyle("color:black");
			lblCDC_SobranteFaltante.setStyle("color:black");
			txtCDC_efectivoenCaja.setValue("0.00");
			txtCDC_ReferDeposito.setValue("");
			
			//Detalles de recibos por método de pago.
			m.remove("ac_recibosPagEfectivo");	  // efectivo
			m.remove("ac_recibosPagCheque");	  // cheque
			m.remove("ac_recibosPagTarjCred");	  // credito
			m.remove("ac_recibosPagTarjCredm");   // Tarjetas de crédito con voucher manual
			m.remove("ac_recibosPagTransBanco");  // transferencia banco
			m.remove("ac_recibosPagDepositoBanco");//Deposito en banco

			lblACDetfp_Efectivo.setValue("0.00");
			lblACDetfp_Cheques.setValue("0.00");
			lblACDetfp_TransBanco.setValue("0.00");
			lblACDetfp_TarCred.setValue("0.00");
			lblACDetfp_TCmanual.setValue("0.00");
			lblACDetfp_DepDbanco.setValue("0.00");
			lblACDetfp_Total.setValue("0.00");
			
			lblCDC_montoMinReint.setValue("0.00");
			lblCDC_montoMinAjust.setValue("0.00");
			actualizarObj();
		}catch(Exception error){
			error.printStackTrace();
		}
	}
	
/*********** 6. refrescar los objetos por cambios en filtros moneda y compañía  *********************/
	public void actualizarObj(){
		SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();		
		try{			
			srm.addSmartRefreshId(lblFechaHoraArqueo.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblEthoraArqueo.getClientId(FacesContext.getCurrentInstance()));		
			
			//INGRESOS(+)
			srm.addSmartRefreshId(lblVentasTotales.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblTotalDevoluciones.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblTotalVentasNetas.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblTotalAbonos.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblTotalPrimas.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblTotalOtrosIngresos.getClientId(FacesContext.getCurrentInstance()));		
			srm.addSmartRefreshId(lblTotaIngresos.getClientId(FacesContext.getCurrentInstance()));	
			srm.addSmartRefreshId(lblTotalIngRecxmonex.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblCambiosOtraMon.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblTotalFinanciamiento.getClientId(FacesContext.getCurrentInstance()));
			
			//Egresos y/o Ajustes(-)
			srm.addSmartRefreshId(lblTotalVtsCredito.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblVtsTransBanc.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblVtsTCredito.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblVtsDDbanco.getClientId(FacesContext.getCurrentInstance()));			
			srm.addSmartRefreshId(lblTotalVtsPagBanco.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblAbonoTCredito.getClientId(FacesContext.getCurrentInstance()));				
			srm.addSmartRefreshId(lblAbonoTransBanc.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblAbonoDDbanco.getClientId(FacesContext.getCurrentInstance()));			
			srm.addSmartRefreshId(lblTotalAbonoPagBanco.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblPrimasTCredito.getClientId(FacesContext.getCurrentInstance()));				
			srm.addSmartRefreshId(lblPrimasTransBanc.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblPrimasDDbanco.getClientId(FacesContext.getCurrentInstance()));			
			srm.addSmartRefreshId(lblTotalPrimasPagBanco.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblOEcambios.getClientId(FacesContext.getCurrentInstance())); 		
			srm.addSmartRefreshId(lblTotalOtrosEgresos.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblOEmonEx.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblOEsalidas.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblTotalEgresos.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblegIexH.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblegIexN.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblegIex8.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblegTotalIex.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblFinanTCredito.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblFinanDbanco.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblFinanTransBanc.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblTotalFinanPagBanco.getClientId(FacesContext.getCurrentInstance()));
			
			// Calculo del depósito sugerido
			srm.addSmartRefreshId(lblCDC_efectnetoRec.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblCDC_montominimo.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblCDC_montoMinReint.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblCDC_montoMinAjust.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblCDC_pagocheques.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblCDC_depositoSug.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblet_SobranteFaltante.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblCDC_SobranteFaltante.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblCDC_depositoFinal.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(txtCDC_efectivoenCaja.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(txtCDC_ReferDeposito.getClientId(FacesContext.getCurrentInstance()));
			
			// detalle de recibos por metodo de pago.
			srm.addSmartRefreshId(lblACDetfp_Efectivo.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblACDetfp_Cheques.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblACDetfp_TransBanco.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblACDetfp_TarCred.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblACDetfp_TCmanual.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblACDetfp_Total.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblACDetfp_DepDbanco.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lbletMsgArqueoPrevio.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblMsgArqueoPrevio.getClientId(FacesContext.getCurrentInstance()));
			
			srm.addSmartRefreshId(lblCDC_montoMinReint.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblCDC_montoMinAjust.getClientId(FacesContext.getCurrentInstance()));
			
		}catch(Exception error){
			error.printStackTrace();
		}
	}	
/*************** 5. Cargar los datos de los Ingresos en la caja  ************************/
	@SuppressWarnings("rawtypes")
	public void cargarIngresos(String sMoneda, String sCodcomp,int iCaid,boolean bDatosCaja,
								Date dtFechaArqueo,Date dtHoraInicio,Date dtHoraFin,Session sesion1){
		ArqueoCajaCtrl acCtrl1 = new ArqueoCajaCtrl();
		ArqueoControl acCtrl = new ArqueoControl();
		List lstFacturas = new ArrayList(), lstLocalizaciones = new ArrayList(),lstDevoluciones = new ArrayList();
		int sCajaId;
		String[] sTipoDoc = null;
		F55ca017[] f55ca017 = null;
		String sCodsuc;
		double dTotalIngresos=0;
		Divisas dv = new Divisas();
		
		long inicio = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
		
		try{
			List lstcaja = (ArrayList)m.get("lstCajas");
			Vf55ca01 caja = (Vf55ca01)lstcaja.get(0);			
			sCajaId  = caja.getId().getCaid();	
			sCodsuc  = caja.getId().getCaco();
			sCodcomp = ddlFiltroCompania.getValue().toString();
			
			//--- Verificar que hayan configuradas Unidades de Negocio y si no hay leer los traslados de facturas.
			if(bDatosCaja){
				lstLocalizaciones = (ArrayList)m.get("lstLocalizaciones");			
				f55ca017 = (F55ca017[])m.get("f55ca017");		
				sTipoDoc = (String[])m.get("sTiposDoc");
				
				long l = System.currentTimeMillis();
				
				lstFacturas = acCtrl.obtenerFacturas(sTipoDoc, f55ca017,
						lstLocalizaciones, caja.getId().getCaid(), sMoneda,
						sCodsuc.substring(3), sCodcomp, dtFechaArqueo,
						dtHoraInicio, dtHoraFin);
				
			}else{
				lstFacturas = null;
				f55ca017 =  CtrlCajas.obtenerUniNegCaja(iCaid, sCodcomp);
				if(f55ca017==null)
					lstFacturas =  acCtrl.obtenerFacturasDiaxTraslado(iCaid, sCodcomp, dtFechaArqueo, sMoneda,dtHoraInicio, dtHoraFin);
			}
			
			//si hay fact. separar vtas de devolucion y sumar totales.
			if(lstFacturas != null && !lstFacturas.isEmpty() ){
				FacturaCrtl faCtrl = new FacturaCrtl();
				List lstFacCO = new ArrayList(), lstFacCR = new ArrayList(),lstDevCO = new ArrayList();
				List lstFVentas = new ArrayList(),lstFDev = new ArrayList(),lstDevCR = new ArrayList();
				double dTotalVentas=0,dTotalDevoluciones=0,dtotalVtsCredito=0,dtotalVtsContado = 0;
				int iCantFactContado=0, iCantFactCredito=0 ;
								
				List lstFact = faCtrl.formatFactura(lstFacturas);				
				for(int i=0; i<lstFact.size();i++){
					Hfactura hfac = (Hfactura)lstFact.get(i);
					hfac.setFecha(hfac.getFecha().substring(10));
					lstFact.remove(i);
					lstFact.add(i,hfac);
				}
				// --------- FACTURAS POR VENTAS ---------
				for(int i=0;i<lstFact.size();i++){
					Hfactura hFac = (Hfactura)lstFact.get(i);					
					String stipoPago = hFac.getTipopago().trim();					
					// Contado.
					if(stipoPago.equals("01")||stipoPago.equals("00")||stipoPago.equals("001")){						
						if(hFac.getNodoco() == 0 && hFac.getTipodoco().trim().equals("")){
							lstFacCO.add(hFac);
							lstFVentas.add(hFac);								
							iCantFactContado++;
							dTotalVentas +=  dv.roundDouble(hFac.getTotal());
							dtotalVtsContado += dv.roundDouble(hFac.getTotal());
						}
					}else{ 
						// Crédito.
						if(hFac.getNodoco() == 0 && hFac.getTipodoco().trim().equals("")){ //1
							lstFacCR.add(hFac);
							lstFVentas.add(hFac);
							dTotalVentas += dv.roundDouble(hFac.getTotal());
							dtotalVtsCredito += dv.roundDouble(hFac.getTotal());
							iCantFactCredito++;								
						}
					}						
				}
				//--------- FACTURAS POR DEVOLUCIONES ---------//
				lstDevoluciones = null;
				if(bDatosCaja){
					lstDevoluciones = acCtrl.obtenerDevolucionDeldia(sTipoDoc,
							f55ca017, lstLocalizaciones, sMoneda, sCodsuc,
							sCodcomp, iCaid, dtFechaArqueo, dtHoraInicio,
							dtHoraFin);
				}else{
					if(f55ca017 == null){
						lstDevoluciones = acCtrl.obtenerDevolucionesxTraslado(
								iCaid, sCodcomp, dtFechaArqueo, sMoneda,
								dtHoraInicio, dtHoraFin);
					}
				}
				if(lstDevoluciones != null && lstDevoluciones.size()>0){
					List lstDev = acCtrl1.formatDevolucion(lstDevoluciones);
					
					for(int i=0;i<lstDev.size();i++){
						Hfactura hDev = (Hfactura)lstDev.get(i);						
						String stipoPago = hDev.getTipopago().trim();
						
						// Devolución de contado.
						if(stipoPago.equals("01")||stipoPago.equals("00")||stipoPago.equals("001")){
							/**
							 * Cambio 07/05/2010: Se incluyen las facturas por devoluciones de días anteriores.
							 * Validación anterior: hDev.getFechadev() == hDev.getFechafac()
							 */
							if(hDev.getTasa().doubleValue()> 1){
								dTotalDevoluciones +=  dv.roundDouble(hDev.getTotalf());
							}else{
								dTotalDevoluciones +=  dv.roundDouble(hDev.getTotal());
							}
							lstDevCO.add(hDev);
							lstFDev.add(hDev);								
						}else{ 
							// Devolución de crédito.						 						
								lstDevCR.add(hDev);
								lstFDev.add(hDev);
							}	
						}						
					}
				double dVnetas = dv.roundDouble(dTotalVentas) - dv.roundDouble(dTotalDevoluciones) - dv.roundDouble(dtotalVtsCredito);
				lblVentasTotales.setValue(dTotalVentas);
				lblTotalDevoluciones.setValue(dTotalDevoluciones);
				lblTotalVentasNetas.setValue(dVnetas);
				lblTotalVtsCredito.setValue(dtotalVtsCredito); //ventas de crédito
				
				dTotalIngresos+=(dv.roundDouble(dVnetas));

				//************************************************************************************************				
				
				List<Hfactura> facturasUnicas = new ArrayList<Hfactura>();
				
				for (int i = 0; i < lstFact.size(); i++) {
				
					final Hfactura hFacNueva = (Hfactura)lstFact.get(i);  
					
					Hfactura factExistente = (Hfactura)
					CollectionUtils.find(facturasUnicas, new Predicate(){

						public boolean evaluate(Object o) {
						 
							 Hfactura factExistente = (Hfactura)o;
							 
							 return
							  factExistente.getCodunineg().trim().compareTo(hFacNueva.getCodunineg().trim()) == 0 && 
							  factExistente.getTipofactura().trim().compareTo(hFacNueva.getTipofactura().trim()) == 0 && 
							  factExistente.getPartida().trim().compareTo(hFacNueva.getPartida().trim()) == 0 &&
							  factExistente.getNofactura() ==  hFacNueva.getNofactura()  && 
							  factExistente.getCodcli() == hFacNueva.getCodcli() && 
							  factExistente.getFechajulian()  == hFacNueva.getFechajulian() ;
						}
						
					}) ;
					
					if(factExistente != null ){
						continue;
					}
					
					facturasUnicas.add(hFacNueva);
				}				
				
				//***********************************************************************************************
				
				m.put("arqueoFacturasDia", lstFact);		// todas las facturas del día.
				m.put("arqueoFacturasCredito", lstFacCR);	// solamente facturas de credito.
				m.put("arqueoFacturasContado", lstFacCO);	// solamente facturas de contado.
				m.put("arqueoDevCredito", lstDevCR);		// solamente devoluciones de credito.
				m.put("arqueoDevContado", lstDevCO);		// solamente devoluciones de contado
				m.put("lstFacturasxVentas", lstFVentas); 	// ventas Totales sin devoluciones.
				m.put("lstFacturasxDevolucion", lstFDev);	// solo facturas por devoluciones.
				m.put("dtotalVtsCredito", dtotalVtsCredito);// Total por ventas de credito.
				
				m.put("ac_cantFactCo", iCantFactContado);	//cantidad de facturas de contado.
				m.put("ac_cantFactCr", iCantFactCredito);	//cantidad de facturas de crédito.
				m.put("ac_TotalFacCo", dtotalVtsContado);	//total por facturas de contado.
				m.put("ac_TotalFacCr", dtotalVtsCredito);	//total por facturas de crédito.
								
			}else{
				m.remove("arqueoFacturasDia");
				m.remove("lstFacturasxVentas");
				m.remove("lstFacturasxDevolucion");
				lblVentasTotales.setValue("0.00");
				lblTotalDevoluciones.setValue("0.00");
				lblTotalVentasNetas.setValue("0.00");
			}
			//------------------------ RECIBOS ------------------------------/
			List lstRecibos = new ArrayList();
			ReciboCtrl rc = new ReciboCtrl();
			double dTotal = 0;
			
			//---------- INICIO DE CAMBIO DE CODIGO ----------// FALL
//			String[] tipoRecibo = {"CR","FN","PR","EX"};
			String[] tipoRecibo = {R_ABONO, "FN", R_PRIMAS, R_INGEX, "PM"};
			BigDecimal bdTotal = BigDecimal.ZERO;
			BigDecimal bdTotalIngresos = BigDecimal.ZERO;
			
			List<Vrecibosxtipoingreso> lst = acCtrl.obtenerRecibosxTipoIngresoNew(sCajaId, sCodsuc, sCodcomp, sMoneda, tipoRecibo, dtFechaArqueo, dtHoraInicio, dtHoraFin);

			for (Iterator<Vrecibosxtipoingreso> it = lst.iterator(); it.hasNext();) {
	            Vrecibosxtipoingreso vrecibosxtipoingreso = it.next();
	            vrecibosxtipoingreso.setMonto(vrecibosxtipoingreso.getId().getMonto());
	            bdTotalIngresos = bdTotalIngresos.add(vrecibosxtipoingreso.getMonto());
	        }
	        dTotalIngresos += bdTotalIngresos.doubleValue();
	        
	        // (1) Cargar los datos para los ingresos por recibos de Abono (credito)
	        List<Vrecibosxtipoingreso> lstTmp1 = SqlUtil.getFilterListVrecibosxtipoingreso(lst,"CR",'=');
	        for (Iterator<Vrecibosxtipoingreso> it = lstTmp1.iterator(); it.hasNext();) {
	            Vrecibosxtipoingreso vrecibosxtipoingreso = it.next();
	            bdTotal = bdTotal.add(vrecibosxtipoingreso.getMonto());
	        }
	        m.remove("lstIngresosAbono");
	        lblTotalAbonos.setValue("0.00");
	        if(!lstTmp1.isEmpty()){
		        lblTotalAbonos.setValue(SqlUtil.roundDouble(bdTotal, 2));
		        m.put("lstIngresosAbono", lstTmp1);
	        }
	        
	        // (2) Cargar los datos para los ingresos por pagos a financimientos.
	        bdTotal = BigDecimal.ZERO;
	        lstTmp1 = SqlUtil.getFilterListVrecibosxtipoingreso(lst,"FN",'=');
	        for (Iterator<Vrecibosxtipoingreso> it = lstTmp1.iterator(); it.hasNext();) {
	            Vrecibosxtipoingreso vrecibosxtipoingreso = it.next();
	            bdTotal = bdTotal.add(vrecibosxtipoingreso.getMonto());
	        }
	        m.remove("ac_RecibosxFinancimiento");
	        lblTotalFinanciamiento.setValue("0.00");
	        if(!lstTmp1.isEmpty()){
				lblTotalFinanciamiento.setValue(SqlUtil.roundDouble(bdTotal, 2));
				m.put("ac_RecibosxFinancimiento", lstTmp1);
	        }
	        
	        // (3) Cargar los datos para los ingresos por recibos por primas y reservas
	        bdTotal = BigDecimal.ZERO;
	        lstTmp1 = SqlUtil.getFilterListVrecibosxtipoingreso(lst,"PR",'=');
	        for (Iterator<Vrecibosxtipoingreso> it = lstTmp1.iterator(); it.hasNext();) {
	            Vrecibosxtipoingreso vrecibosxtipoingreso = it.next();
	            bdTotal = bdTotal.add(vrecibosxtipoingreso.getMonto());
	        }
	        m.remove("lstIngresoPrimas");
	        lblTotalPrimas.setValue("0.00");
	        if(!lstTmp1.isEmpty()){
				lblTotalPrimas.setValue(SqlUtil.roundDouble(bdTotal, 2));
				m.put("lstIngresoPrimas", lstTmp1);	
	        }
	        
	        // (4) Cargar recibos por ingresos Extraordinarios.
	        bdTotal = BigDecimal.ZERO;
	        lstTmp1 = SqlUtil.getFilterListVrecibosxtipoingreso(lst,"EX",'=');
	        for (Iterator<Vrecibosxtipoingreso> it = lstTmp1.iterator(); it.hasNext();) {
	            Vrecibosxtipoingreso vrecibosxtipoingreso = it.next();
	            bdTotal = bdTotal.add(vrecibosxtipoingreso.getMonto());
	        }
	        m.remove("ac_RecibosxIngresosEx");
	        lblTotalOtrosIngresos.setValue("0.00");
	        if(!lstTmp1.isEmpty()){
				lblTotalOtrosIngresos.setValue(SqlUtil.roundDouble(bdTotal, 2));
				m.put("ac_RecibosxIngresosEx", lstTmp1);	
	        }
	       
	        // (5) Cargar recibos por anticipos PMT 
	        bdTotal = BigDecimal.ZERO;
	        lstTmp1 = SqlUtil.getFilterListVrecibosxtipoingreso(lst,"PM",'=');
	        for (Iterator<Vrecibosxtipoingreso> it = lstTmp1.iterator(); it.hasNext();) {
	            Vrecibosxtipoingreso vrecibosxtipoingreso = it.next();
	            bdTotal = bdTotal.add(vrecibosxtipoingreso.getMonto());
	        }
	        m.remove("ac_RecibosxAnticipoPMT");
	        lblTotalRecibosPMT.setValue("0.00");
	        if(!lstTmp1.isEmpty()){
	        	lblTotalRecibosPMT.setValue(SqlUtil.roundDouble(bdTotal, 2));
				m.put("ac_RecibosxAnticipoPMT", lstTmp1);	
	        }
			CodeUtil.refreshIgObjects(lblTotalRecibosPMT);
			//---------- FINAL DE CAMBIO DE CODIGO  ----------// FALL
			
			
			//------------ Recibos que aplican cambios que se pagan con otras monedas.
			lstRecibos = new ArrayList();
			 
			List<Vdetallecambiorecibo>lstTmp = acCtrl.obtenerRecibosxcambiomixto(iCaid, sCodcomp, sCodsuc, sMoneda,
															dtFechaArqueo,dtHoraInicio,dtHoraFin);
			 
			boolean mixto = false;
			BigDecimal dTmp = BigDecimal.ZERO,bdCambio = BigDecimal.ZERO;
			if(lstTmp != null && lstTmp.size()>0){				
				for(int i=0; i<lstTmp.size(); i++){ 
					Vdetallecambiorecibo v = lstTmp.get(i);
					
					//validar que el pago no haya sido mixto				
					mixto = acCtrl1.isPagoOriginalMixto(v.getId().getCaid(), v.getId().getCodcomp(),v.getId().getNumrec(),sMoneda);
					if(!mixto){
						bdCambio =  v.getId().getCambio().divide(v.getId().getTasacambio(), 2, BigDecimal.ROUND_HALF_EVEN);
						dTmp = dTmp.add(bdCambio);
						v.setCambio(bdCambio);	
						lstRecibos.add(v);
					}else{
						BigDecimal cambio = null;
						if(	v.getId().getMonedarec().compareTo(sMoneda) == 0
							&& v.getId().getMonto().compareTo(v.getId().getMontoapl()) == 1 
							&& v.getId().getMonedacamb().compareTo(sMoneda) != 0
							&& v.getId().getMpago().compareTo(MetodosPagoCtrl.EFECTIVO ) == 0 ) {
							
							cambio = v.getId().getMonto().subtract(v.getId().getMontoapl());
							v.setCambio(cambio);
							dTmp = dTmp.add(cambio);
							lstRecibos.add(v);
						}
					}
					
				}
				dTmp = Divisas.roundBigDecimal(dTmp);
				dTotalIngresos += dTmp.doubleValue();
				lblCambiosOtraMon.setValue(dTmp.doubleValue());
				m.put("ac_lstRecCambiosOtrMoneda", lstRecibos);
			}else{
				lblCambiosOtraMon.setValue("0.00");
				m.remove("ac_lstRecCambiosOtrMoneda");
			}
			
			//------- cargar los recibos por pagos de facturas de moneda distinta al arqueo ----------//
			double  dTotalrme = 0;
			m.remove("ac_ingRecibosxmonex");
			lblTotalIngRecxmonex.setValue("0.00");
		
			
			lstRecibos = new ArrayList();
			List<Vmonedafactrec> lstRecTmp = acCtrl.obtenerIngresoRecMonEx(iCaid, sCodcomp, sCodsuc, 
												sMoneda,dtFechaArqueo, dtHoraInicio, dtHoraFin);
			
			
			String sCompara = "";
			StringBuilder sb = new StringBuilder("");
			String sComparaDevolucion = "";
			String sComparaDevolucionTmp = "";
			String sComparaDevRestadas = "";
			
			if(lstRecTmp!=null && lstRecTmp.size()>0){
				for(int i=0; i<lstRecTmp.size();i++){
					Vmonedafactrec vm = lstRecTmp.get(i);

					if(vm.getId().getMpago().compareTo(MetodosPagoCtrl.EFECTIVO ) != 0 ){
						
						sCompara = vm.getId().getNumrec() + ":"
								+ vm.getId().getMpago() + ":"
								+ vm.getId().getMonto().toString() + ":"
								+ vm.getId().getRmoneda() + ":"
								+ vm.getId().getRefer1().trim() + ":"
								+ vm.getId().getRefer2().trim() + ":"
								+ vm.getId().getRefer3().trim() + ":"
								+ vm.getId().getRefer4().trim() + ",";
						
						if(sb.indexOf(sCompara) != -1)
							continue;
						else
							sb.append(sCompara);
					}

					//&& =============  buscar devolucion total para la linea del metodo de pago.
					boolean devoluciontotal = false;
					List<Recibo> recibos = ReciboCtrl.getDevolucionPorRecibo(
							iCaid, vm.getId().getNumrec(), 
							sCodcomp, dtFechaArqueo, null);
					if (recibos != null && !recibos.isEmpty()){
						for (Recibo r : recibos) {
							
							sComparaDevolucionTmp = "@" + r.getId().getCaid() + ":"
									+ r.getId().getCodcomp().trim() + ":"
									+ r.getId().getTiporec().trim() + ":"
									+ r.getId().getNumrec();
							
							if(r != null && r.getMontoapl().compareTo(vm.getId().getMontoapl()) == 0){
								continue;
							}
							//&&==============  buscar devolucion parcial para la linea del metodo de pago.
							if( r != null && r.getMontoapl().compareTo( vm.getId().getMontoapl()) == -1 
										&& !sComparaDevolucion.contains(sComparaDevolucionTmp) ){
								dTotalrme -= Divisas.roundDouble( r.getMontoapl()
											.multiply(vm.getId().getTasa()).doubleValue(), 2);
								sComparaDevolucion += sComparaDevolucionTmp;
								sComparaDevRestadas  += sComparaDevolucionTmp;
							}
						} 
					}
					if(devoluciontotal)
						continue;
					
					if(vm.getId().getMpago().compareTo(MetodosPagoCtrl.EFECTIVO )!=0 && 
							vm.getId().getCambio().compareTo(BigDecimal.ZERO) == 1 
							&& vm.getId().getRmoneda().compareTo(vm.getId().getFmoneda()) == 0){
						continue;
					}
					
					//&& ======= Validar que el metodo de pago no acepte cambio y que no exceda el montoapl.
					if (vm.getId().getMpago().compareTo(MetodosPagoCtrl.EFECTIVO ) != 0
							&& vm.getId().getEquiv().compareTo(
									vm.getId().getMontoapl()) == 1) {

						vm.getId().setDomeaplicado(vm.getId().getMontoapl());
						vm.getId().setForarecibido(vm.getId().getForarecibido());
						
					}
					
					if(vm.getId().getTiporec().equals("CO")){
						
						/* *****************************************
						 * Validación (24/Abril/2010)
						 * Si el cambio es en la misma moneda, restar el cambio y utilizar el monto aplicado al pago.
						 * Si el cambio es en moneda distinta, no restar el cambio y utilizar el monto recibido al pago.
						 */
						if(vm.getId().getCmoneda().equals(sMoneda)){
							vm.setMonto(vm.getId().getForarecibido());
							vm.setEquiv(vm.getId().getDomeaplicado());
							vm.setIngresoegreso((vm.getId().getForarecibido()));
							dTotalrme += dv.roundDouble(vm.getId().getForarecibido().doubleValue());
						}else{
							vm.setMonto(vm.getId().getMonto());
							vm.setEquiv(vm.getId().getEquiv());
							vm.setIngresoegreso((vm.getId().getMonto()));
							dTotalrme += dv.roundDouble(vm.getId().getMonto().doubleValue());
						}						
					}else{
						vm.setMonto(vm.getId().getMonto());
						vm.setEquiv(vm.getId().getEquiv());
						vm.setIngresoegreso(vm.getId().getMonto());
						dTotalrme += dv.roundDouble(vm.getId().getMonto().doubleValue());
					}
					lstRecibos.add(vm);	
				}
				//-----------------------------------------------------------------
				//recibos por devoluciones de facturas pagadas con diferente moneda
				List lstDev;
				 
				lstDev = acCtrl.obtenerRecxDevmonex(true, iCaid, sCodsuc, sCodcomp, sMoneda, "DCO", 
													dtFechaArqueo,dtHoraInicio,dtHoraFin); 
				
				if(lstDev != null && lstDev.size()>0){
					for(int i=0; i<lstDev.size();i++){
						Vreciboxdevolucion v = (Vreciboxdevolucion)lstDev.get(i);
						//dTotalrme -= dv.roundDouble(v.getId().getMonto().doubleValue());
						
						//&& 30/06/2012
						//&& Valida que la devolucion sea parcial, si es total en los egresos no se incluye 
						//&& el recibo original, entonces la devolucion resta dos veces.
						if(v.getId().getMontofact().compareTo(v.getId().getMontodev()) != 0 && !sComparaDevRestadas.contains(sComparaDevolucionTmp)){
							dTotalrme  -= dv.roundDouble(v.getId().getMonto().doubleValue());
							sComparaDevRestadas += sComparaDevolucionTmp;
						}
						
					}
				}
			}
			//&& ========= Cargar las devoluciones que se procesaron con moneda distinta
			//&& ========= a la moneda con que se pago la factura. 
			List<Vreciboxdevolucion>lstDevs	= acCtrl1.obtenerRecxDevmonex5(iCaid, sCodsuc, sCodcomp, sMoneda, "DCO", 
																		dtFechaArqueo, dtHoraInicio, dtHoraFin, null);
			 
			if (lstDevs != null && lstDevs.size() > 0) {
				for (Vreciboxdevolucion v : lstDevs)
					dTotalrme += Divisas.roundBigDecimal(v.getId().getMontodev()).doubleValue();
			}
			
			//&& ======== justificar pago en moneda de factura, que tuvo devolucion en otra moneda. 
			lstDevs = acCtrl.obtenerDevParcialMonForaneaIng(iCaid, sCodsuc,
										sCodcomp, sMoneda, "DCO", dtFechaArqueo,
										dtHoraInicio, dtHoraFin); 
			
			if (lstDevs != null && lstDevs.size() > 0) {
				for (Vreciboxdevolucion v : lstDevs)
					dTotalrme += Divisas.roundBigDecimal(v.getId().getMonto()).doubleValue();
			}
			
			lblTotalIngRecxmonex.setValue(dTotalrme);
			m.put("ac_ingRecibosxmonex", lstRecibos);				
			dTotalIngresos += dTotalrme;
			
			//-----------------------------------------------------------------
			//recibos por devoluciones de facturas pagadas con diferente moneda
			double dTotalrme3 = 0.0;
			boolean existe = false;
			
			List lstDev3 = acCtrl.obtenerRecxDevmonex3(true, iCaid, sCodsuc, sCodcomp, sMoneda, "DCO", 
												dtFechaArqueo,dtHoraInicio,dtHoraFin);
			
			if(lstDev3 != null && lstDev3.size()>0){				
				for(int i=0; i<lstDev3.size();i++){
					Vreciboxdevolucion v = (Vreciboxdevolucion)lstDev3.get(i);
					existe = false;
					if (lstDevs != null && lstDevs.size() > 0) {						
						for (Vreciboxdevolucion v3 : lstDevs){
							if(v3.getId().getNofact() == v.getId().getNofact()){
								existe = true;
								break;
							}
						}							
					}//fin validacion
					if(!existe)dTotalrme3 += dv.roundDouble(v.getId().getMontoapl().doubleValue());
				}//fin for
			}
			lblTotalIngRecxmonex.setValue(dv.roundDouble(dTotalrme3+dTotalrme));
			m.put("ac_ingRecibosxmonex", lstRecibos);				
			dTotalIngresos += dTotalrme3;		
			
			//&& ========= Cargar los montos de pagos a facturas en moneda distinta al arqueo que tienen devolucion
			//&& ========= y que la factura original fue pagada en la moneda del arqueo
			if(!sMoneda.equals("COR")){
				 
				List<BigDecimal>lstMontos = acCtrl1.obtenerRecxDevmonFor(true,iCaid, sCodsuc, sCodcomp, sMoneda, "DCO", 
																			dtFechaArqueo, dtHoraInicio, dtHoraFin);
				 
				double dTotalMontos = 0.0;
				if (lstMontos != null && lstMontos.size() > 0) {
					for (BigDecimal v : lstMontos)
						dTotalMontos += v.doubleValue();
				}
				lblTotalIngRecxmonex.setValue(dTotalMontos + dTotalrme + dTotalrme3);
	
				dTotalIngresos += dTotalMontos;
			}
			
			//----------------------------------------------------------------------------//
			//--- Fijar la fecha del arqueo
			Date dtFecha = new Date();
			String sFecha = "";
			SimpleDateFormat sdf1 = new SimpleDateFormat();
			
			sdf1.applyPattern("yyyy-MM-dd");
			sFecha = sdf1.format(dtFechaArqueo);
			sFecha = sFecha.concat(" ");
			sdf1.applyPattern("HH:mm:ss");
			sFecha = sFecha.concat(sdf1.format(dtHoraFin));
			sdf1.applyPattern("yyyy-MM-dd HH:mm:ss");
			dtFecha = sdf1.parse(sFecha);
			
			lblFechaHoraArqueo.setValue(dtFecha);
			lblEthoraArqueo.setValue("Hora de Arqueo:  ");
			m.put("ac_HoraArqueo", dtFecha);	//hora del arqueo.

			lblTotaIngresos.setValue(dTotalIngresos);
			m.put("ac_TotalxIngresos", dTotalIngresos); // guardar el total de ingresos.
			m.put("lstTodasFacturas", lstFacturas);		// todas las Facturas Registradas.
			actualizarObj();

		}catch(Exception error){
			LogCajaService.CreateLog("cargarIngresos", "ERR", error.getMessage());
		} finally {
			HibernateUtilPruebaCn.closeSession();
		}
	}
	
/********************** 4. Cargar los montos por transaccion  ***************************/
	public void cargarTransDia(ValueChangeEvent ev){
		try {
			cargarTransDia();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
  //------- operacionenew ArqueoCajaCtrl();s a realizar para cargar las transacciones --------------/
	public void cargarTransDia(){
		int iCajaId;
		String sCodcomp, sMoneda, sCodsuc;
		ArqueoCajaCtrl acCtrl = new ArqueoCajaCtrl();
		boolean bDatoscaja=true;
		List lstArqueos = null;
		Date dtFechaActual = null, dtFechaArqueo=null, dtHoraInicio=null,dtHoraFin=null;
		SimpleDateFormat sdf = null;
		Divisas d = new Divisas();
		try{
			
			limpiarObj();
			
			m.remove("ac_Lstrptmcaja002");
			m.remove("ac_ArqueoPrevio");
			m.remove("ac_ArqueoActual");
			m.remove("ac_F14MinDep");
			m.remove("ac_MinutaDep");
			m.remove("ac_RecibosCierre");
			
			CodeUtil.removeFromSessionMap(new String[] { "acHora_Finaliza",
					"acHora_Inicio", "ac_MinutaDeposito", "ac_ArqueoActual",
					"ac_DatosCompaniaArqueo", "ac_HoraArqueoInicia" , "ac_HoraArqueoTermina" });
			
			sdf = new SimpleDateFormat("HH:mm:ss");
			dtFechaArqueo  = new Date();
			dtHoraFin      = new Date();
			dtHoraInicio   = sdf.parse("00:00:00");
			
			List lstcaja = (ArrayList)m.get("lstCajas");
			Vf55ca01 caja = (Vf55ca01)lstcaja.get(0);			
			sCodsuc  = caja.getId().getCaco();
			iCajaId  = caja.getId().getCaid();
			sMoneda  = ddlFiltroMoneda.getValue().toString();
			sCodcomp = ddlFiltroCompania.getValue().toString();
			
			
			CodeUtil.removeFromSessionMap(new String[]{ "ac_lstDonacionesArqueoCaja","ac_lstDonacionesFormaPago", "ac_totalDonacionEfectivo" } );
			gvDonacionesFormaPago.dataBind();
			CodeUtil.refreshIgObjects(gvDonacionesFormaPago);
			
			
			if(sMoneda.trim().compareTo("MON") == 0)
				return;
			
			boolean bValido = true;
			String sMensaje = "";

			txtCDC_ReferDeposito.setValue("");
			
			if(chkArqueoDiaAnterior.isChecked()){
				if(dcFechaArqueoAnterior.getValue()==null){
					bValido = false;
					sMensaje = "El valor de la fecha es requerido";
				}else{
					sdf = new SimpleDateFormat("dd/MM/yyyy");
					dtFechaArqueo = (Date)dcFechaArqueoAnterior.getValue();
					dtFechaArqueo = sdf.parse(FechasUtil.formatDatetoString(dtFechaArqueo, "dd/MM/yyyy"));
					dtFechaActual = sdf.parse(FechasUtil.formatDatetoString(new Date(), "dd/MM/yyyy")); 
					
					switch(dtFechaArqueo.compareTo(dtFechaActual)){
					case -1:
						bValido = true;
						break;
					case 0:
						bValido = false;
						sMensaje = "La fecha para el arqueo debe ser distinta a la actual";
						break;
					case 1:	
						bValido = false;
						sMensaje = "La fecha para el arqueo debe menor de la fecha actual";
						break;
					}
				}
				if(bValido){
					String sInicio,sFin;
					sInicio = ddlArqueoHini.getValue().toString().concat(":");
					sInicio = sInicio.concat(ddlArqueoMini.getValue().toString()).concat(":00");
					sFin = ddlArqueoHFin.getValue().toString().concat(":");
					sFin = sFin.concat(ddlArqueoMFin.getValue().toString()).concat(":00");
					
					sdf = new SimpleDateFormat("HH:mm:ss");
					dtHoraInicio = sdf.parse(sInicio);
					dtHoraFin  = sdf.parse(sFin);
					
					switch (dtHoraInicio.compareTo(dtHoraFin)){
					case -1:bValido = true;
							break;
					case 0:	bValido = false;
							sMensaje = "La hora de inicio y de fin deben ser distintas";
							break;
					case 1: Date dtTemp = dtHoraInicio;
							dtHoraInicio = dtHoraFin;
							dtHoraFin = dtTemp;
							break;
					}
				}
				if(bValido){
					ArqueoCajaCtrl ac = new ArqueoCajaCtrl();
					List<Arqueo> lstArqueo = ac.obtenerArqueosPorFechaHora(iCajaId, sCodcomp, sCodsuc,
															sMoneda, dtFechaArqueo, dtHoraInicio, dtHoraFin);
					if(lstArqueo!=null && lstArqueo.size()>=1){
						bValido = false;
						sMensaje = "Ya existe arqueo para la caja a la fecha e intervalo de horas";
					}
				}
			}
			
			if(!bValido){
				ddlFiltroCompania.dataBind();
				ddlFiltroMoneda.dataBind();
				lblValidarArqueo.setValue(sMensaje);
				dwValidarArqueo.setWindowState("normal");
				return;
			}
			
			//verificar que existan los datos de la caja, si no... cargarlos.
			if (m.get("lstLocalizaciones") == null
					|| m.get("f55ca017") == null
					|| m.get("sTiposDoc") == null
					|| m.get("iFechaActual") == null) {
				if(!consultarDatosCaja())
					bDatoscaja = false;
			}
			//--------- Leer los arqueos del día y determinar si se debe arquear entre horas.
			if(!chkArqueoDiaAnterior.isChecked()){
				m.remove("ac_HoraInicioDant");
				
				lstArqueos = acCtrl.obtenerArqueos(iCajaId, sCodcomp, 
								sCodsuc, sMoneda, dtFechaArqueo,
								"('P','A')");
				
				if(lstArqueos != null && lstArqueos.size()>0){

					Arqueo a = (Arqueo)lstArqueos.get(0);
					dtHoraInicio = a.getId().getHora();
					m.put("ac_ArqueoPrevio", (Arqueo)lstArqueos.get(0));
					
					lbletMsgArqueoPrevio.setStyle("visibility: visible");
					lblMsgArqueoPrevio.setStyle("visibility: visible");
					lblMsgArqueoPrevio.setValue(a.getId().getNoarqueo() + ", Hora: " + a.getId().getHora());
				}
			}else{
				m.put("ac_HoraInicioDant", dtHoraInicio);
			}
 
			//&& ============== Conservar Variables para reporte de recibos
			CodeUtil.putInSessionMap("ac_HoraArqueoInicia", dtHoraInicio);
			CodeUtil.putInSessionMap("ac_HoraArqueoTermina", dtHoraFin);
			
			this.cargarIngresos(sMoneda, sCodcomp, iCajaId, bDatoscaja, dtFechaArqueo, 
					dtHoraInicio, dtHoraFin,null);
			
 
			this.cargarRecxMetodoPago(sCodcomp, sCodsuc, iCajaId, sMoneda, 
							dtFechaArqueo,dtHoraInicio, dtHoraFin, null);	
			
			//validar si aplicara el ajuste de minimo de caja
			boolean ajustarMinimo = false;
			List lstVenta = (List)m.get("lstFacturasxVentas");
			double dEfec = Divisas.roundDouble(Double.parseDouble(m.get("ac_TotalEfectivo").toString()),2);
			double dOtrosEg = 0;
			double montoReint = 0;
			double montoMiniAjust=0;
			
			if(lstVenta == null && dEfec < 0){
				 ajustarMinimo = true;
				 m.put("AjustaMinimo", 0);
			}
			else if(dEfec < 0){
				 ajustarMinimo = true;
				 m.put("AjustaMinimo", 0);
			}else{
				m.put("AjustaMinimo", 1);
			}
			
			CodeUtil.putInSessionMap("acHora_Inicio",   dtHoraInicio);
			CodeUtil.putInSessionMap("acHora_Finaliza", dtHoraFin);
			
			this.cargarEgresos(iCajaId, sCodcomp, sCodsuc, sMoneda, dtFechaArqueo,
					dtHoraInicio, dtHoraFin, null, ajustarMinimo);
			
			double dTotalIng = 0;
			double dTotalEg = 0; 
			
			//--------------------------------------------------------//
			//Cargar los datos para el calculo de deposito de caja.
			
			if(CodeUtil.getFromSessionMap("ac_totalDonacionEfectivo") != null ){
				BigDecimal totaldncefectivo = (BigDecimal) CodeUtil.getFromSessionMap("ac_totalDonacionEfectivo"); 
				BigDecimal ac_TotalxIngresos = BigDecimal.ZERO;
				
				if(  CodeUtil.getFromSessionMap("ac_TotalxIngresos") != null ){
					  ac_TotalxIngresos = new BigDecimal(String.valueOf( CodeUtil.getFromSessionMap("ac_TotalxIngresos") )) ;
				}
				ac_TotalxIngresos = ac_TotalxIngresos.add(totaldncefectivo);
				CodeUtil.putInSessionMap("ac_TotalxIngresos", ac_TotalxIngresos.doubleValue());
			} 
			
			
			double dEfetivoRecibido = 0 ;
			
			if( m.get("ac_TotalxIngresos") != null && m.get("ac_TotalxEgresos") != null ){
			
				dTotalIng = (Double.parseDouble(m.get("ac_TotalxIngresos").toString()));
				dTotalEg  = (Double.parseDouble(m.get("ac_TotalxEgresos").toString()));
				double dTotalchk = (Double.parseDouble(m.get("ac_totalcheques").toString()));
				
				double dEfectivoNeto = 0;
				double dDepSugerido = 0;
				
				dEfectivoNeto = dTotalIng - dTotalEg;
				dEfetivoRecibido = d.roundDouble(dEfectivoNeto) - d.roundDouble(dTotalchk) ;
				
				//&& Inicio =========== Ajuste de centavos para depositos sugerido.
				
				String sEfectivoResumenMp = lblACDetfp_Efectivo.getValue().toString().replace(",", "");
				
				BigDecimal bdEfectivoResm = new BigDecimal(sEfectivoResumenMp);
				BigDecimal bdEfectivoCalc = new BigDecimal(Double.toString(dEfetivoRecibido));

				if (bdEfectivoResm.subtract(bdEfectivoCalc).abs().compareTo(BigDecimal.valueOf(0.25)) == -1) {

					dEfectivoNeto = d.roundDouble(bdEfectivoResm.add(
							BigDecimal.valueOf(dTotalchk)).doubleValue());
					dEfetivoRecibido = d.roundDouble(bdEfectivoResm
							.doubleValue());
				}
				//&& Finaliza  =========== Ajuste de centavos para depositos sugerido.
				
				List<A02factco> facts = buscarFacturasPendientes(iCajaId, sCodcomp, dtFechaArqueo);
				
				
				if (facts != null && facts.size() > 0) {

					final String monedaArqueo = sMoneda;
					
					CollectionUtils.filter(facts, new Predicate() {
						public boolean evaluate(Object f) {
							return ((A02factco) f).getMoneda().trim()
									.compareTo(monedaArqueo.trim()) == 0;
						}
					});
				}
				//&& ============ iguala los montos de calculo del deposito y resumen de efectivo
				if(bdEfectivoResm.compareTo(bdEfectivoCalc) != 0 && 
						!ajustarMinimo && ( facts == null || facts.isEmpty()))
					dEfetivoRecibido = bdEfectivoResm.doubleValue();
				
				if(dEfectivoNeto < 0)
					dEfectivoNeto = 0;
				
				dDepSugerido = (dEfetivoRecibido < 0)? 
									dTotalchk: 
									dEfectivoNeto;
				
				dDepSugerido =  ((dEfetivoRecibido < 0)?0:dEfetivoRecibido) + dTotalchk ;
				
				
				lblCDC_efectnetoRec.setValue(dEfetivoRecibido);
				lblCDC_depositoSug.setValue(dDepSugerido);
				m.put("ac_CDCefectivoneto", dDepSugerido);
				
			}else{
				lblCDC_efectnetoRec.setValue("0.00");
				lblCDC_depositoSug.setValue("0.00");
				m.remove("ac_CDCefectivoneto");
			}
			
			double dMontomin = Double.parseDouble(acCtrl.obtenerMontoMinimodeCaja(iCajaId, sCodcomp, sMoneda));
			dMontomin = dMontomin / 100;
			double montominimo  = dMontomin ;
			
			//---- no hay ingreso efectivo y si hay egresos.
			double dTotalEfectivo = Double.parseDouble(m.get("ac_TotalEfectivo").toString());
			double dDepSugerido = (Double.parseDouble(m.get("ac_CDCefectivoneto").toString()));
			
			//ajustael monto minimo
			if(ajustarMinimo){
				m.put("AjustaMinimo", 0);
				dOtrosEg = Double.parseDouble(m.get("ac_dTotalxEgresos").toString());					
				montoReint = Double.parseDouble(m.get("ac_CambiosOE").toString());
				dTotalEg  = (Double.parseDouble(m.get("ac_TotalxEgresos").toString()));		
				
				if( (montoReint == 0 || montoReint == dTotalEg)  && dTotalEg > dTotalIng )
					montoReint = new Divisas().roundDouble(dTotalEg - dTotalIng);
				
				if( dTotalEg > dTotalIng  && dEfetivoRecibido < 0)
					montoReint = Math.abs(dEfetivoRecibido);
				
				montoMiniAjust = dMontomin - montoReint;
				
				lblTotalOtrosEgresos.setValue(dOtrosEg);
				lblTotalEgresos.setValue(dTotalEg);
				lblCDC_efectnetoRec.setValue("0.00");
				lblCDC_montominimo.setValue(dMontomin);
				lblCDC_montoMinReint.setValue(montoReint);
				lblCDC_montoMinAjust.setValue(montoMiniAjust);					
				lblCDC_depositoSug.setValue(dDepSugerido);				
			}else{
				dEfec = Double.parseDouble(lblCDC_efectnetoRec.getValue().toString());					

				if(dEfec < 0){
					dOtrosEg =  Double.parseDouble(lblTotalOtrosEgresos.getValue().toString()); 
					dTotalEg  = (Double.parseDouble(m.get("ac_TotalxEgresos").toString()));

					montoReint = dEfec*-1;
					montoMiniAjust = dMontomin - montoReint;
					m.put("AjustaMinimo", 0);
					lblTotalOtrosEgresos.setValue(dOtrosEg);
					lblCDC_efectnetoRec.setValue("0.00");
					lblTotalEgresos.setValue(dTotalEg);
					lblCDC_montominimo.setValue(dMontomin);
					lblCDC_montoMinReint.setValue(montoReint);
					lblCDC_montoMinAjust.setValue(montoMiniAjust);
					lblCDC_depositoSug.setValue(dDepSugerido);
				}else{
					m.put("AjustaMinimo", 1);
					lblCDC_montominimo.setValue(dMontomin);
				}					
			}
			
			if(dTotalEfectivo<0){
				dMontomin += dTotalEfectivo;
				m.put( "ac_updMontoMinimo", dMontomin );
			}

			m.put("ac_CDCMontoMinimo", dMontomin );	
			
			
			//&& =================== calculos para fondo minimo.
			if(ajustarMinimo){
				
				BigDecimal posicionNetaEfectivo = ArqueoCajaCtrl.calcularIngresosCaja(
						dtFechaArqueo, iCajaId, sCodcomp, sMoneda, dtHoraInicio, dtHoraFin) ;
				
				if(posicionNetaEfectivo.compareTo(BigDecimal.ZERO) == -1){
					
					BigDecimal mtominajustado = new BigDecimal(Double.toString(montominimo)).subtract(posicionNetaEfectivo.abs()) ;
					
					m.put("ac_updMontoMinimo", mtominajustado.doubleValue() );
					m.put("AjustaMinimo", 0);
					
					lblCDC_efectnetoRec.setValue("0.00");
					lblCDC_montominimo.setValue(montominimo);
					
					lblCDC_montoMinReint.setValue( posicionNetaEfectivo.abs().doubleValue() );
					lblCDC_montoMinAjust.setValue(  mtominajustado.doubleValue()   );			
				}
			}
			 
			actualizarObj();
			
			try {
				
				String sEfectivoCalculado = lblCDC_efectnetoRec.getValue().toString().replace(",", "");
				String sEfectivoResumenMp = lblACDetfp_Efectivo.getValue().toString().replace(",", "");

				BigDecimal bdEfectivoCalc = new BigDecimal(sEfectivoCalculado);
				BigDecimal bdEfectivoResm = new BigDecimal(sEfectivoResumenMp);
				
				if ( bdEfectivoResm.subtract(bdEfectivoCalc).abs()
								.compareTo(BigDecimal.valueOf(0.25)) == -1) {

					lblCDC_efectnetoRec.setValue( Divisas.roundDouble(bdEfectivoResm.doubleValue(),2 ) );
					lblACDetfp_Efectivo.setValue( Divisas.roundDouble(bdEfectivoResm.doubleValue(),2 ) );
					
				}

			} catch (Exception e) {
				LogCajaService.CreateLog("cargarTransDia", "ERR", e.getMessage());
			}
			
		}catch(Exception error){
			LogCajaService.CreateLog("cargarTransDia", "ERR", error.getMessage());
		} finally {
			HibernateUtilPruebaCn.closeSession();
		}
	}

	public  List<A02factco> buscarFacturasPendientes(int caid, final String codcomp, Date fecha){
		 List<A02factco> lstFacs = null;
		
		try {
			
			F55ca017[] f55ca017 = (F55ca017[]) m.get("f55ca017");
			String sTipoDoc[] = (String[]) m.get("sTiposDoc");
			int ifechaactual = FechasUtil.dateToJulian(fecha);

			if( m.get("f55ca017") == null || m.get("sTiposDoc") == null)
				return null;
			
			lstFacs = CtrlCajas.leerFacturaDelDia(ifechaactual, sTipoDoc, f55ca017, caid);

			if (lstFacs != null && lstFacs.size() > 0) {

				CollectionUtils.filter(lstFacs, new Predicate() {
					public boolean evaluate(Object f) {
						return ((A02factco) f).getCodcomp().trim()
								.compareTo(codcomp.trim()) == 0;
					}
				});
			}
		} catch (Exception e) {
			 e.printStackTrace();
		} 	
		return lstFacs;
	}
	
/********** 3. Actualizar los tipos de monedas a partir de la compañía seleccionada *******************/
	public void cargarMonedasxCompania(ValueChangeEvent ev){
		String sCodcomp = "";
		
		try{
			List lstcaja = (ArrayList)m.get("lstCajas");
			Vf55ca01 caja = (Vf55ca01)lstcaja.get(0);			
			int sCajaId = caja.getId().getCaid();
			
			// obtener el código de la compañía seleccionada en el combo y cargar sus tipos de monedas.
			sCodcomp = ddlFiltroCompania.getValue().toString();
			if(sCodcomp.equals("COMP")){
				if(m.get("lstFiltroMoneda")!=null )
					m.remove("lstFiltroMoneda");		
				
			}else{
				String[] monedas = new MonedaCtrl().obtenerMonedasxCaja(sCajaId, sCodcomp);
				List lstFiltro = new ArrayList();
				
				lstFiltro.add(new SelectItem("MON","-- Moneda --","Seleccione un tipo de moneda"));
				for(int i=0; i<monedas.length;i++){
					lstFiltro.add(new SelectItem(monedas[i].toString(),monedas[i].toString(),"Tipo moneda "+(i+1)));
				}
				m.put("lstFiltroMoneda", lstFiltro);				
			}			
			limpiarObj();
			actualizarObj();			
			ddlFiltroMoneda.dataBind();
			lblMsgNoRegistros.setValue("");			
			
			
			 /*int codemp = ((Vautoriz[])m.get("sevAut"))[0].getId().getCodreg();
			 String s = "select *  from crpmcaja.dnc_donacion where date(fechacrea) = '2015-06-22' " ;
			 List<DncDonacion>lstDonaciones = (List<DncDonacion>)ConsolidadoDepositosBcoCtrl.executeQuery(s, true, DncDonacion.class, true);
			 enviarCorreoNotificacionDonaciones(lstDonaciones, sCajaId, codemp);*/
			
			
		}catch(Exception error){
			error.printStackTrace();
		}
	}
/************* 2.1  Cambiar el tipo de moneda al detalle de la factura  *********************************/
	public void cambiarMonedaDetalle(ValueChangeEvent e){
		Divisas divisas = new Divisas();		
		Hfactura hFac = (Hfactura)m.get("Hfactura");
		List lstDetalle = hFac.getDfactura();
		Dfactura dFac = null;
		List lstNewDetalle = new ArrayList();
		String monedaActual = ddlDetalleFacCon.getValue().toString();
		try{
			if(monedaActual.equals("COR")){
				txtSubtotal = hFac.getSubtotal() * hFac.getTasa().doubleValue();
				txtIva		= hFac.getIva() * hFac.getTasa().doubleValue();
				txtTotal 	= divisas.formatDouble(hFac.getTotal() * hFac.getTasa().doubleValue()) + "";
				for (int i = 0; i < lstDetalle.size(); i++){
					dFac = (Dfactura)lstDetalle.get(i); 
					dFac.setPreciounit(dFac.getPreciounit() * dFac.getTasa().doubleValue());
					lstNewDetalle.add(dFac);
				}				
			}else {
				txtSubtotal = hFac.getSubtotal();
				txtIva 		= hFac.getIva();
				txtTotal 	= divisas.formatDouble(hFac.getTotal());
				for (int i = 0; i < lstDetalle.size(); i++){
					dFac = (Dfactura)lstDetalle.get(i); 
					dFac.setPreciounit(dFac.getPreciounit()/dFac.getTasa().doubleValue());
					lstNewDetalle.add(dFac);
				}
			}		
			m.put("lstCierreCajaDetfactura",lstNewDetalle);			
			gvDfacturasDiario.dataBind();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}	
/********************** 2. cargar el detalle de la factura ***************************/	
	public void mostrarDetalleFact(ActionEvent ev){
		try{			
			RowItem ri = (RowItem)ev.getComponent().getParent().getParent();
			List lstA = (List) ri.getCells();		
			Cell c = (Cell) lstA.get(1);//Columna a obtener: No. Factura
			HtmlOutputText noFactura = (HtmlOutputText) c.getChildren().get(0);
			int iNoFactura = Integer.parseInt(noFactura.getValue().toString());
			
			c = (Cell) lstA.get(2);//Columna a obtener: Tipo de documento
			HtmlOutputText TipoDoc = (HtmlOutputText) c.getChildren().get(0);
			String sTipoDoc = TipoDoc.getValue().toString();
			
			List lstFacsActuales = (List) m.get("lstFacturasRegistradas");
			Hfactura hFac = new Hfactura();		
			Divisas divisas = new Divisas();

			
			for (int i = 0; i < lstFacsActuales.size(); i++){
				hFac = (Hfactura)lstFacsActuales.get(i);
				if (hFac.getNofactura() == iNoFactura && hFac.getTipofactura().equals(sTipoDoc)){
					//poner valor a labels del detalle
					txtNofactura = iNoFactura+"";
					txtFechaFactura = ((Hfactura)lstFacsActuales.get(i)).getFecha();
					txtCliente = (((Hfactura)lstFacsActuales.get(i)).getNomcli() + " (Nombre)");
					txtCodigoCliente = (((Hfactura)lstFacsActuales.get(i)).getCodcli() + " (Código)");
					txtCodUnineg = (((Hfactura)lstFacsActuales.get(i)).getCodunineg());	
					txtUnineg = (((Hfactura)lstFacsActuales.get(i)).getUnineg());					
					txtTasaDetalle.setValue(((Hfactura)lstFacsActuales.get(i)).getTasa()+"");					
					txtSubtotal = ((Hfactura)lstFacsActuales.get(i)).getSubtotal();					
									
					//actualizar lista de detalle buscar detalle
					FacturaCrtl faCtrl = new FacturaCrtl();
					lstCierreCajaDetfactura = faCtrl.formatDetalle(hFac);	
					hFac.setDfactura(lstCierreCajaDetfactura);
					m.put("lstCierreCajaDetfactura",lstCierreCajaDetfactura);					
				
					txtIva = hFac.getTotal() - hFac.getSubtotal();
					txtTotal = divisas.formatDouble(((Hfactura)lstFacsActuales.get(i)).getTotal());
				
					lstMonedasDetalle = new ArrayList();
					String moneda = ((Hfactura)lstFacsActuales.get(i)).getMoneda();
					if(moneda.equals("COR")){
						lstMonedasDetalle.add(new SelectItem("COR","COR"));
						txtTasaDetalle.setStyle("visibility: hidden");
						lblTasaDetalle.setStyle("visibility: hidden");
						
					}else{
						lstMonedasDetalle.add(new SelectItem(moneda,moneda));
						lstMonedasDetalle.add(new SelectItem("COR","COR"));
						lblTasaDetalle.setValue("Tasa de Cambio: ");
						lblTasaDetalle.setStyle("visibility: visible");
						lblTasaDetalle.setStyleClass("frmLabel2");
						txtTasaDetalle.setStyle("visibility: visible");
						txtTasaDetalle.setStyleClass("frmLabel3");
					}
					//poner la factura al mapa para utilizar en caso de cambiar moneda.
					m.put("Hfactura", hFac);
					List oldDfac = hFac.getDfactura();
					m.put("oldDfac",oldDfac);
					m.put("lstMonedasDetalle",lstMonedasDetalle);
					ddlDetalleFacCon.dataBind();					
					gvDfacturasDiario.dataBind();
					break;					
				}
			}
			dgDetalleFactura.setWindowState("normal");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
/*********************** cargar los registros de las facturas  *****************************/
	public void cargarFacturas(ActionEvent ev){
		List lstFacturas = new ArrayList();
		String titulo = "",sLinkid;
		String sEtCantFco = "Contado", sEtCantFcr = "Crédito", sEtTotalFco = "T.Contado",sEtTotalFcr = "T.Crédito";
		String sCantFco = "0", sCantFcr = "0", sTotalFco = "0",sTotalFcr = "0";
		DecimalFormat df = new DecimalFormat("#,###,##0.00");
		double valor = 0;
				
		try{
			sLinkid = ev.getComponent().getId();
			
			if(sLinkid.equals("lnkDetalleVtasDia")){  //VENTAS -- CRÉDITO Y CONTADO
				if(m.get("lstFacturasxVentas")!=null && ((ArrayList)m.get("lstFacturasxVentas")).size()>0){
					
					lstFacturas = ((ArrayList)m.get("lstFacturasxVentas"));
					titulo = "Todas las Facturas registradas";
					
					if(m.get("ac_cantFactCo")!=null){						
						valor = Double.parseDouble(m.get("ac_TotalFacCo").toString());	
						valor = Math.rint(valor*100)/100;						
						sCantFco  = m.get("ac_cantFactCo").toString();
						sTotalFco = df.format(valor);
					}
					if(m.get("ac_cantFactCr")!=null){
						valor = Double.parseDouble(m.get("ac_TotalFacCr").toString());
						valor = Math.rint(valor*100)/100;
						sCantFcr  = m.get("ac_cantFactCr").toString();
						sTotalFcr = df.format(valor);
					}
				}
			}else
			if(sLinkid.equals("lnkDetalleFactDevoluciones")){ //DEVOLUCIONES --CRÉDITO Y CONTADO
				if(m.get("lstFacturasxDevolucion")!=null && ((ArrayList)m.get("lstFacturasxDevolucion")).size()>0){
					lstFacturas =  ((ArrayList)m.get("lstFacturasxDevolucion"));
					titulo = "Facturas por devoluciones";
					
					double totalFact = 0;
					List lstFact;
				
					if(m.get("arqueoDevCredito")!=null && ((ArrayList)m.get("arqueoDevCredito")).size()>0){						
						lstFact = (ArrayList)m.get("arqueoDevCredito");						
						for(int i=0; i<lstFact.size();i++){
							Hfactura hFac = (Hfactura)lstFact.get(i);
							totalFact +=  hFac.getTotal();
						}
						totalFact = Math.rint(totalFact*100)/100;						
						sCantFcr  = ""+lstFact.size();
						sTotalFcr = df.format(totalFact);		
					}
					if(m.get("arqueoDevContado")!=null && ((ArrayList)m.get("arqueoDevContado")).size()>0){					
						totalFact = 0;
						lstFact = (ArrayList)m.get("arqueoDevContado");						
						for(int i=0; i<lstFact.size();i++){
							Hfactura hFac = (Hfactura)lstFact.get(i);
							totalFact +=  hFac.getTotal();
						}
						totalFact = Math.rint(totalFact*100)/100;						
						sCantFco = ""+lstFact.size();
						sTotalFco = df.format(totalFact);
					}
				}
			}else
			if(sLinkid.equals("lnkDetalleVtasCredito")){ //VENTAS DE CRÉDITO.
				if(m.get("arqueoFacturasCredito")!=null && ((ArrayList)m.get("arqueoFacturasCredito")).size()>0){					
					lstFacturas = (ArrayList)m.get("arqueoFacturasCredito");
					titulo = "Facturas de crédito";					
					sEtCantFco 	= "";
					sEtCantFcr 	= "";
					sEtTotalFco = "";
					sEtTotalFcr = "";
					sCantFco	= "";
					sCantFcr 	= "";
					sTotalFco	= "";
					sTotalFcr 	= "";					
				}				
			}
			lblEtCantFacC0  = sEtCantFco;
			lblEtTotalFacCo = sEtTotalFco;
			lblEtCantFacCr  = sEtCantFcr;
			lblEtTotalFacCr = sEtTotalFcr;
													
			lblCantFacCO  = sCantFco;
			lblTotalFacCo = sTotalFco;
			lblCantFacCr  = sCantFcr;
			lblTotalFacCr = sTotalFcr;		
						
			m.put("lblEtCantFacC0",sEtCantFco);
			m.put("lblEtTotalFacCo",sEtTotalFco);
			m.put("lblEtCantFacCr",sEtCantFcr);
			m.put("lblEtTotalFacCr",sEtTotalFcr);
			m.put("lblCantFacCO",sCantFco);
			m.put("lblTotalFacCo",sTotalFco);
			m.put("lblCantFacCr",sCantFcr);
			m.put("lblTotalFacCr",sTotalFcr);
						
			if(lstFacturas != null && lstFacturas.size()>0){
				m.put("lstFacturasRegistradas", lstFacturas);
				gvFacturaRegistradas.dataBind();
				gvFacturaRegistradas.setPageIndex(0);
				hdFactura.setCaptionText(titulo);
				dwFacturasRegistradas.setWindowState("normal");
			}
			
		}catch(Exception error){
			error.printStackTrace();
		}	
	}
	
/************************* 1. cargar las facturas registradas  *****************************/
	public void cargarFacturasReg(ActionEvent ev){
		double uno;
		DecimalFormat df ;
		String result;	
		try{			
			if(m.get("lstFacturasxVentas")!=null && ((ArrayList)m.get("lstFacturasxVentas")).size()>0){
				m.put("lstFacturasRegistradas", ((ArrayList)m.get("lstFacturasxVentas")));

				//aplicar valores a footer del grid para todas las facturas
				//contado
				lblEtCantFacC0 = "F.Contado";
				lblEtTotalFacCo = "T.Contado";
				if(m.get("ac_cantFactCo")!=null){					
					
					uno = Double.parseDouble(m.get("ac_TotalFacCo").toString());	
					uno = Math.rint(uno*100)/100;
					
					df = new DecimalFormat("#,###,##0.00");
					result = df.format(uno);				
					lblTotalFacCo = result;									
					lblCantFacCO  = m.get("ac_cantFactCo").toString();
					
				}else{
					lblCantFacCO = "0";
					lblTotalFacCo = "0.00";
				}
				
				//crédito.
				lblEtCantFacCr = "F.Crédito";
				lblEtTotalFacCr ="T.Crédito";
				if(m.get("ac_cantFactCr")!=null){
					uno = Double.parseDouble(m.get("ac_TotalFacCr").toString());
					uno = Math.rint(uno*100)/100;
					df = new DecimalFormat("#,###,##0.00");
					result = df.format(uno);	
					
					lblCantFacCr = m.get("ac_cantFactCr").toString();
					lblTotalFacCr = result;
				}else{
					lblCantFacCr = "0";
					lblTotalFacCr  = "0.00";
				}
				
				gvFacturaRegistradas.dataBind();
				gvFacturaRegistradas.setPageIndex(0);
				hdFactura.setCaptionText("Todas las Facturas registradas");
				dwFacturasRegistradas.setWindowState("normal");
				
			}		
		}catch(Exception error){
			error.printStackTrace();
		}
	}
	public void cargarFacturasxDevolucion(ActionEvent ev){
		int cantFact;
		double totalFact;
		List lstFact ;
		DecimalFormat df;
		String result;
		
		try{			
			if(m.get("lstFacturasxDevolucion")!=null && ((ArrayList)m.get("lstFacturasxDevolucion")).size()>0){
				m.put("lstFacturasRegistradas", ((ArrayList)m.get("lstFacturasxDevolucion")));
				gvFacturaRegistradas.dataBind();
	
				if(m.get("arqueoDevCredito")!=null && ((ArrayList)m.get("arqueoDevCredito")).size()>0){
					totalFact = 0;
					lstFact = (ArrayList)m.get("arqueoDevCredito");
					cantFact = lstFact.size();
					for(int i=0; i<lstFact.size();i++){
						Hfactura hFac = (Hfactura)lstFact.get(i);
						totalFact +=  hFac.getTotal();
					}
					totalFact = Math.rint(totalFact*100)/100;
					df = new DecimalFormat("#,###,##0.00");
					result = df.format(totalFact);
					
					lblEtCantFacCr ="D.Crédito";
					lblEtTotalFacCr="T.Crédito";
					lblCantFacCr   = Integer.toString(cantFact);
					lblTotalFacCr  = result;
				}else{
					lblEtCantFacCr="";
					lblEtTotalFacCr="";
					lblCantFacCr="";
					lblTotalFacCr="";;
				}
				if(m.get("arqueoDevContado")!=null && ((ArrayList)m.get("arqueoDevContado")).size()>0){					
					totalFact = 0;
					lstFact = (ArrayList)m.get("arqueoDevContado");
					cantFact = lstFact.size();
					for(int i=0; i<lstFact.size();i++){
						Hfactura hFac = (Hfactura)lstFact.get(i);
						totalFact +=  hFac.getTotal();
					}
					totalFact = Math.rint(totalFact*100)/100;
					df = new DecimalFormat("#,###,##0.00");
					result = df.format(totalFact);
					
					lblEtCantFacC0 ="D.Contado";
					lblEtTotalFacCo="T.Contado";
					lblCantFacCO   = Integer.toString(cantFact);
					lblTotalFacCo  = result;
				}else{
					lblEtCantFacC0 ="";
					lblEtTotalFacCo="";
					lblCantFacCO="";
					lblTotalFacCo="";
				}
				
				gvFacturaRegistradas.setPageIndex(0);				
				hdFactura.setCaptionText("Facturas por devoluciones");
				dwFacturasRegistradas.setWindowState("normal");				
			}		
		}catch(Exception error){
			error.printStackTrace();
		}
	}
	public void cargarFacturasxdeCredito(ActionEvent ev){
		try{			
			if(m.get("arqueoFacturasCredito")!=null && ((ArrayList)m.get("arqueoFacturasCredito")).size()>0){
				m.put("lstFacturasRegistradas", ((ArrayList)m.get("arqueoFacturasCredito")));
							
				lblEtCantFacC0 = "";
				lblEtTotalFacCo = "";
				lblCantFacCO = "";
				lblTotalFacCo = "";
				
				lblEtCantFacCr  = "";
				lblEtTotalFacCr = "";
				lblCantFacCr    = "";
				lblTotalFacCr   = "";
				
				gvFacturaRegistradas.dataBind();
				hdFactura.setCaptionText("Facturas de crédito");
				dwFacturasRegistradas.setWindowState("normal");				
			}		
		}catch(Exception error){
			error.printStackTrace();
		}
	}

/***************************** CERRAR LAS VENTANAS **************************************/	
	public void cerrarDetFacturas(ActionEvent ev){
		try{
			m.remove("lblEtCantFacC0");
			m.remove("lblEtTotalFacCo");
			m.remove("lblEtCantFacCr");
			m.remove("lblEtTotalFacCr");
			m.remove("lblCantFacCO");
			m.remove("lblTotalFacCo");
			m.remove("lblCantFacCr");
			m.remove("lblTotalFacCr");		
			dwFacturasRegistradas.setWindowState("hidden");
		}catch(Exception error){
			error.printStackTrace();
		}
	}
	public void cerrarDetalleFacDiario(ActionEvent ev){
		dgDetalleFactura.setWindowState("hidden");
	}
	public void cerrarDetRecibos(ActionEvent ev){
		dwRecibosxTipoIngreso.setWindowState("hidden");
	}
	public void cerrarDetalleRecibo(ActionEvent ev){
		dwDetalleRecibo.setWindowState("hidden");
	}
	public void cerrarDetalleCambios(ActionEvent ev){
		dwDetalleCambios.setWindowState("hidden");
	}
	public void cerrarRecxTipoyMetPago(ActionEvent ev){
		dwDetTipoReciboxMetodoPago.setWindowState("hidden");
	}	
	public void cerrarValidarArqueo(ActionEvent ev){
		dwValidarArqueo.setWindowState("hidden");
	}	
	public void CerrarProcesarArqueoCaja(ActionEvent ev){
		dwConfirmarProcesarArqueo.setWindowState("hidden");
	}
	public void cerrarDetRecPagMonEx(ActionEvent ev){
		dwDetallerecpagmonEx.setWindowState("hidden");
	}
	public void CerrarImprimirRptArqueo(ActionEvent ev){
		dwConfImprRptpre.setWindowState("hidden");
	}
	public void cerrarDetalleSalida(ActionEvent ev){
		dwDetalleSalidas.setWindowState("hidden");
	}
/************** CONSULTAR DOCUMENTOS, LOCALIZACIONES, LINEAS ***************************/
	public boolean consultarDatosCaja(){
		Calendar calFechaActual = null;
		F55ca017[] f55ca017 =  null,f55ca017i = null;
		CtrlCajas cajasCtrl = new CtrlCajas();
		String[] sLineas = null, sTiposDoc;
		boolean passed = true, completo = true;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		try{
			List lstCajas = (List)m.get("lstCajas");
			Vf55ca01 f55ca01 = ((Vf55ca01)lstCajas.get(0));
			String sCodSuc = f55ca01.getId().getCaco().substring(3,5);
			
			// leer unidades de negocio activas e inactivas para la caja
			f55ca017 = cajasCtrl.obtenerUniNegCaja(f55ca01.getId().getCaid(), f55ca01.getId().getCaco());
			f55ca017i = cajasCtrl.obtenerUniNegCajaInactiva(f55ca01.getId().getCaid(), f55ca01.getId().getCaco());
			
			//obtener lineas correspondientes menos las inactivas
			if (f55ca017 != null && f55ca017i != null){
				sLineas = cajasCtrl.obtenerLineas(sCodSuc,f55ca017, f55ca017i);
			}else if(f55ca017 != null && f55ca017i == null){//no hay inactivas
				sLineas = cajasCtrl.obtenerLineas(sCodSuc,f55ca017);
			}else{// no hay unidades de negocio o sucursales configuradas en esta caja
				passed = false;
				return false;				
			}
			if (passed){
				//obtener tipos de documentos de todas las lineas encontradas
				sTiposDoc = cajasCtrl.obtenerTiposDeDocumento(sLineas);
				
				if (sTiposDoc != null){
				
					calFechaActual = Calendar.getInstance();
					String sFechaActual = calFechaActual.get(Calendar.DATE)
							+ "/" + (calFechaActual.get(Calendar.MONTH) + 1)
							+ "/" + calFechaActual.get(Calendar.YEAR);
					
					calFechaActual.setTime(new Date(sdf.parse(sFechaActual).getTime()));
					CalendarToJulian julian = new CalendarToJulian(calFechaActual);
					int iFechaActual = julian.getDate();
					
					//obtener localizaciones
					List lstLocalizaciones = new ArrayList();
					boolean bEmtpy = false;
					for (int a = 0 ; a < f55ca017.length; a++){
						if (!f55ca017[a].getId().getC7locn().trim().equals("")){
							lstLocalizaciones.add(f55ca017[a].getId().getC7locn().trim());	
						}else{
							if(!bEmtpy){
								lstLocalizaciones.add("");
								bEmtpy = true;
							}
						}
					}
					m.put("iFechaActual", iFechaActual);
					m.put("sTiposDoc", sTiposDoc);
					m.put("lstLocalizaciones",lstLocalizaciones);
					m.put("f55ca017", f55ca017);
				}else{
					return false;
				}
			}else{
				return false;
			}			
		}catch(Exception error){
			error.printStackTrace();
		}	
		return completo;
	}
	
/****************************** GETTERS Y SETTERS **************************************/	
	//------------------- variables de Ingresos ------------------//
	public HtmlOutputText getLblVentasTotales() {
		return lblVentasTotales;
	}
	public void setLblVentasTotales(HtmlOutputText lblVentasTotales) {
		this.lblVentasTotales = lblVentasTotales;
	}
	public HtmlOutputText getLblTotalAbonos() {
		return lblTotalAbonos;
	}
	public void setLblTotalAbonos(HtmlOutputText lblTotalAbonos) {
		this.lblTotalAbonos = lblTotalAbonos;
	}
	public HtmlOutputText getLblTotalDevoluciones() {
		return lblTotalDevoluciones;
	}
	public void setLblTotalDevoluciones(HtmlOutputText lblTotalDevoluciones) {
		this.lblTotalDevoluciones = lblTotalDevoluciones;
	}
	public HtmlOutputText getLblTotalOtrosIngresos() {
		return lblTotalOtrosIngresos;
	}
	public void setLblTotalOtrosIngresos(HtmlOutputText lblTotalOtrosIngresos) {
		this.lblTotalOtrosIngresos = lblTotalOtrosIngresos;
	}
	public HtmlOutputText getLblTotalPrimas() {
		return lblTotalPrimas;
	}
	public void setLblTotalPrimas(HtmlOutputText lblTotalPrimas) {
		this.lblTotalPrimas = lblTotalPrimas;
	}
	public HtmlOutputText getLblTotalVentasNetas() {
		return lblTotalVentasNetas;
	}
	public void setLblTotalVentasNetas(HtmlOutputText lblTotalVentasNetas) {
		this.lblTotalVentasNetas = lblTotalVentasNetas;
	}
	public HtmlOutputText getLblOEmonEx() {
		return lblOEmonEx;
	}
	public void setLblOEmonEx(HtmlOutputText lblOEmonEx) {
		this.lblOEmonEx = lblOEmonEx;
	}
	public HtmlOutputText getLblTotalIngRecxmonex() {
		return lblTotalIngRecxmonex;
	}
	public void setLblTotalIngRecxmonex(HtmlOutputText lblTotalIngRecxmonex) {
		this.lblTotalIngRecxmonex = lblTotalIngRecxmonex;
	}
	public HtmlOutputText getLblIngresosExord() {
		return lblIngresosExord;
	}
	public void setLblIngresosExord(HtmlOutputText lblIngresosExord) {
		this.lblIngresosExord = lblIngresosExord;
	}
	public HtmlOutputText getLblCambiosOtraMon() {
		return lblCambiosOtraMon;
	}
	public void setLblCambiosOtraMon(HtmlOutputText lblCambiosOtraMon) {
		this.lblCambiosOtraMon = lblCambiosOtraMon;
	}
	public HtmlDialogWindow getDwFacturasRegistradas() {
		return dwFacturasRegistradas;
	}
	public void setDwFacturasRegistradas(HtmlDialogWindow dwFacturasRegistradas) {
		this.dwFacturasRegistradas = dwFacturasRegistradas;
	}
	public HtmlGridView getGvFacturaRegistradas() {
		return gvFacturaRegistradas;
	}
	public void setGvFacturaRegistradas(HtmlGridView gvFacturaRegistradas) {
		this.gvFacturaRegistradas = gvFacturaRegistradas;
	}
	public List getLstFacturasRegistradas() {
		try{
			if(m.get("lstFacturasRegistradas")==null)
				lstFacturasRegistradas = new ArrayList();
			else
				lstFacturasRegistradas = (ArrayList)m.get("lstFacturasRegistradas");			
		}catch(Exception error){
			error.printStackTrace();
		}
		return lstFacturasRegistradas;
	}
	public void setLstFacturasRegistradas(List lstFacturasRegistradas) {
		this.lstFacturasRegistradas = lstFacturasRegistradas;
	}	
	public HtmlOutputText getLblTotaIngresos() {
		return lblTotaIngresos;
	}
	public void setLblTotaIngresos(HtmlOutputText lblTotaIngresos) {
		this.lblTotaIngresos = lblTotaIngresos;
	}
	//	-------------- variables de Detalle de Factura. ------------------//
	public HtmlDropDownList getDdlDetalleFacCon() {
		return ddlDetalleFacCon;
	}
	public void setDdlDetalleFacCon(HtmlDropDownList ddlDetalleFacCon) {
		this.ddlDetalleFacCon = ddlDetalleFacCon;
	}
	public HtmlDialogWindow getDgDetalleFactura() {
		return dgDetalleFactura;
	}
	public void setDgDetalleFactura(HtmlDialogWindow dgDetalleFactura) {
		this.dgDetalleFactura = dgDetalleFactura;
	}
	public HtmlGridView getGvDfacturasDiario() {
		return gvDfacturasDiario;
	}
	public void setGvDfacturasDiario(HtmlGridView gvDfacturasDiario) {
		this.gvDfacturasDiario = gvDfacturasDiario;
	}
	public HtmlOutputText getLblTasaDetalle() {
		return lblTasaDetalle;
	}
	public void setLblTasaDetalle(HtmlOutputText lblTasaDetalle) {
		this.lblTasaDetalle = lblTasaDetalle;
	}	
	public List getLstCierreCajaDetfactura() {
		try{
			if(m.get("lstCierreCajaDetfactura")==null)
				lstCierreCajaDetfactura = new ArrayList();
			else
				lstCierreCajaDetfactura = (ArrayList)m.get("lstCierreCajaDetfactura");
		}catch(Exception error){
			error.printStackTrace();
		}		
		return lstCierreCajaDetfactura;
	}
	public void setLstCierreCajaDetfactura(List lstCierreCajaDetfactura) {
		this.lstCierreCajaDetfactura = lstCierreCajaDetfactura;
	}
	public List getLstMonedasDetalle() {
		try{
			if(m.get("lstMonedasDetalle")==null)
				lstMonedasDetalle = new ArrayList();
			else
				lstMonedasDetalle = (ArrayList)m.get("lstMonedasDetalle");
		}catch(Exception error){
			error.printStackTrace();
		}
		return lstMonedasDetalle;
	}
	public void setLstMonedasDetalle(List lstMonedasDetalle) {
		this.lstMonedasDetalle = lstMonedasDetalle;
	}
	public String getTxtCliente() {
		return txtCliente;
	}
	public void setTxtCliente(String txtCliente) {
		this.txtCliente = txtCliente;
	}
	public String getTxtCodigoCliente() {
		return txtCodigoCliente;
	}
	public void setTxtCodigoCliente(String txtCodigoCliente) {
		this.txtCodigoCliente = txtCodigoCliente;
	}
	public String getTxtCodUnineg() {
		return txtCodUnineg;
	}
	public void setTxtCodUnineg(String txtCodUnineg) {
		this.txtCodUnineg = txtCodUnineg;
	}
	public String getTxtFechaFactura() {
		return txtFechaFactura;
	}
	public void setTxtFechaFactura(String txtFechaFactura) {
		this.txtFechaFactura = txtFechaFactura;
	}
	public double getTxtIva() {
		return txtIva;
	}
	public void setTxtIva(double txtIva) {
		this.txtIva = txtIva;
	}
	public String getTxtNofactura() {
		return txtNofactura;
	}
	public void setTxtNofactura(String txtNofactura) {
		this.txtNofactura = txtNofactura;
	}
	public double getTxtSubtotal() {
		return txtSubtotal;
	}
	public void setTxtSubtotal(double txtSubtotal) {
		this.txtSubtotal = txtSubtotal;
	}
	public HtmlOutputText getTxtTasaDetalle() {
		return txtTasaDetalle;
	}
	public void setTxtTasaDetalle(HtmlOutputText txtTasaDetalle) {
		this.txtTasaDetalle = txtTasaDetalle;
	}
	public String getTxtTotal() {
		return txtTotal;
	}
	public void setTxtTotal(String txtTotal) {
		this.txtTotal = txtTotal;
	}
	public String getTxtUnineg() {
		return txtUnineg;
	}
	public void setTxtUnineg(String txtUnineg) {
		this.txtUnineg = txtUnineg;
	}
//	 --------------------- compañias y tipo de monedas --------------------//
	public HtmlDropDownList getDdlFiltroCompania() {
		return ddlFiltroCompania;
	}
	public void setDdlFiltroCompania(HtmlDropDownList ddlFiltroCompania) {
		this.ddlFiltroCompania = ddlFiltroCompania;
	}
	public HtmlDropDownList getDdlFiltroMoneda() {
		return ddlFiltroMoneda;
	}
	public void setDdlFiltroMoneda(HtmlDropDownList ddlFiltroMoneda) {
		this.ddlFiltroMoneda = ddlFiltroMoneda;
	}
	public HtmlOutputText getLblMsgNoRegistros() {
		return lblMsgNoRegistros;
	}
	public void setLblMsgNoRegistros(HtmlOutputText lblMsgNoRegistros) {
		this.lblMsgNoRegistros = lblMsgNoRegistros;
	}
	public HtmlOutputText getLblFechaHoraArqueo() {
		return lblFechaHoraArqueo;
	}
	public void setLblFechaHoraArqueo(HtmlOutputText lblFechaHoraArqueo) {
		this.lblFechaHoraArqueo = lblFechaHoraArqueo;
	}
	public HtmlOutputText getLblEthoraArqueo() {
		return lblEthoraArqueo;
	}
	public void setLblEthoraArqueo(HtmlOutputText lblEthoraArqueo) {
		this.lblEthoraArqueo = lblEthoraArqueo;
	}
	/* *** cargar las monedas por compañía.  ****/
	public List getLstFiltroComp() {		
		try{
			if(m.get("lstFiltroComp")==null){				
				List lstcaja = (ArrayList)m.get("lstCajas");
				Vf55ca01 caja = (Vf55ca01)lstcaja.get(0);			
				int sCajaId = caja.getId().getCaid();
				
				CompaniaCtrl cc = new CompaniaCtrl();
				List lstFiltro = new ArrayList();
				F55ca014[] lstComxCaja = cc.obtenerCompaniasxCaja(sCajaId);			
				
				lstFiltro.add(new SelectItem("COMP"," -- Compañía --","Seleccione una Compañía"));
				for(int i=0; i<lstComxCaja.length;i++){				
					lstFiltro.add(new SelectItem(lstComxCaja[i].getId().getC4rp01(),lstComxCaja[i].getId().getC4rp01d1()));
				}
				m.put("lstFiltroComp", lstFiltro);
				m.put("ac_lstCompaniasCaja", lstComxCaja); 
				lstFiltroComp = lstFiltro;		
				ddlFiltroCompania.dataBind();
			}else
				lstFiltroComp = (List)m.get("lstFiltroComp");			
		
		}catch(Exception error){
			error.printStackTrace();
		}		
		return lstFiltroComp;
	}
	public void setLstFiltroComp(List lstFiltroComp) {
		this.lstFiltroComp = lstFiltroComp;
	}
	/* *** cargar las monedas por compañía.  ****/
	public List getLstFiltroMoneda() {
		try{
			if(m.get("lstFiltroMoneda")==null){
				List lstFiltro = new ArrayList();
				lstFiltro.add(new SelectItem("MON","-- Moneda --","Seleccione un tipo de moneda"));
				lstFiltroMoneda = lstFiltro;
			}else
				lstFiltroMoneda = (ArrayList)m.get("lstFiltroMoneda");			
			
		}catch(Exception error){
			error.printStackTrace();
		}		
		return lstFiltroMoneda;
	}
	public void setLstFiltroMoneda(List lstFiltroMoneda) {
		this.lstFiltroMoneda = lstFiltroMoneda;
	}
	// ------------------ recibos por tipo ingreso --------------//
	public HtmlDialogWindow getDwRecibosxTipoIngreso() {
		return dwRecibosxTipoIngreso;
	}
	public void setDwRecibosxTipoIngreso(HtmlDialogWindow dwRecibosxTipoIngreso) {
		this.dwRecibosxTipoIngreso = dwRecibosxTipoIngreso;
	}
	public HtmlGridView getGvRecibosIngresos() {
		return gvRecibosIngresos;
	}
	public void setGvRecibosIngresos(HtmlGridView gvRecibosIngresos) {
		this.gvRecibosIngresos = gvRecibosIngresos;
	}
	public List getLstRecibosxIngresos() {
		try{
			if(m.get("lstRecibosxIngresos")==null)
				lstRecibosxIngresos = new ArrayList();
			else
				lstRecibosxIngresos = (ArrayList)m.get("lstRecibosxIngresos"); 
		}catch(Exception error){
			error.printStackTrace();
		}
		
		return lstRecibosxIngresos;
	}
	public void setLstRecibosxIngresos(List lstRecibosxIngresos) {
		this.lstRecibosxIngresos = lstRecibosxIngresos;
	}	
	public String getLblCantFacCO() {
		if(m.get("lblCantFacCO")==null)
			lblCantFacCO = "";
		else
			lblCantFacCO = m.get("lblCantFacCO").toString();
		return lblCantFacCO;
	}
	public void setLblCantFacCO(String lblCantFacCO) {
		this.lblCantFacCO = lblCantFacCO;
	}
	public String getLblCantFacCr() {
		if(m.get("lblCantFacCr") == null)
			lblCantFacCr = "";
		else
			lblCantFacCr = m.get("lblCantFacCr").toString();
		return lblCantFacCr;
	}
	public void setLblCantFacCr(String lblCantFacCr) {
		this.lblCantFacCr = lblCantFacCr;
	}
	public String getLblEtCantFacC0() {
		if(m.get("lblEtCantFacC0") == null)
			lblEtCantFacC0 = "";
		else
			lblEtCantFacC0 = m.get("lblEtCantFacC0").toString();		
		return lblEtCantFacC0;
	}
	public void setLblEtCantFacC0(String lblEtCantFacC0) {
		this.lblEtCantFacC0 = lblEtCantFacC0;
	}
	public String getLblEtCantFacCr() {
		if(m.get("lblEtCantFacCr") == null)
			lblEtCantFacCr = "";
		else
			lblEtCantFacCr = m.get("lblEtCantFacCr").toString();		
		return lblEtCantFacCr;
	}
	public void setLblEtCantFacCr(String lblEtCantFacCr) {
		this.lblEtCantFacCr = lblEtCantFacCr;
	}
	public String getLblEtTotalFacCo() {
		if(m.get("lblEtTotalFacCo") == null)
			lblEtTotalFacCo = "";
		else
			lblEtTotalFacCo = m.get("lblEtTotalFacCo").toString();
		return lblEtTotalFacCo;
	}
	public void setLblEtTotalFacCo(String lblEtTotalFacCo) {
		this.lblEtTotalFacCo = lblEtTotalFacCo;
	}
	public String getLblEtTotalFacCr() {
		if(m.get("lblEtTotalFacCr") == null)
			lblEtTotalFacCr = "";
		else
			lblEtTotalFacCr = m.get("lblEtTotalFacCr").toString();
		return lblEtTotalFacCr;
	}
	public void setLblEtTotalFacCr(String lblEtTotalFacCr) {
		this.lblEtTotalFacCr = lblEtTotalFacCr;
	}
	public String getLblTotalFacCo() {
		if(m.get("lblTotalFacCo") == null)
			lblTotalFacCo = "";
		else
			lblTotalFacCo = m.get("lblTotalFacCo").toString();		
		return lblTotalFacCo;
	}
	public void setLblTotalFacCo(String lblTotalFacCo) {
		this.lblTotalFacCo = lblTotalFacCo;
	}
	public String getLblTotalFacCr() {
		if(m.get("lblTotalFacCr")==null)
			lblTotalFacCr = "";
		else
			lblTotalFacCr = m.get("lblTotalFacCr").toString();
		return lblTotalFacCr;
	}
	public void setLblTotalFacCr(String lblTotalFacCr) {
		this.lblTotalFacCr = lblTotalFacCr;
	}
	//	----------------- DETALLE DE LOS RECIBOS  --------------//
	public HtmlDialogWindow getDwDetalleRecibo() {
		return dwDetalleRecibo;
	}
	public void setDwDetalleRecibo(HtmlDialogWindow dwDetalleRecibo) {
		this.dwDetalleRecibo = dwDetalleRecibo;
	}
	public HtmlGridView getGvDetalleRecibo() {
		return gvDetalleRecibo;
	}
	public void setGvDetalleRecibo(HtmlGridView gvDetalleRecibo) {
		this.gvDetalleRecibo = gvDetalleRecibo;
	}
	public HtmlGridView getGvFacturasRecibo() {
		return gvFacturasRecibo;
	}
	public void setGvFacturasRecibo(HtmlGridView gvFacturasRecibo) {
		this.gvFacturasRecibo = gvFacturasRecibo;
	}
	public List getLstDetalleRecibo() {
		try{
			if(m.get("lstDetalleRecibo")==null)
				lstDetalleRecibo = new ArrayList();
			else
				lstDetalleRecibo = (ArrayList)m.get("lstDetalleRecibo");
		}catch(Exception error){
			error.printStackTrace();
		}
		return lstDetalleRecibo;
	}
	public void setLstDetalleRecibo(List lstDetalleRecibo) {
		this.lstDetalleRecibo = lstDetalleRecibo;
	}
	public List getLstFacturasRecibo() {
		try{
			if(m.get("lstFacturasRecibo")==null)
				lstFacturasRecibo = new ArrayList();
			else
				lstFacturasRecibo = (ArrayList)m.get("lstFacturasRecibo");
		}catch(Exception error){
			error.printStackTrace();
		}
		return lstFacturasRecibo;
	}
	public void setLstFacturasRecibo(List lstFacturasRecibo) {
		this.lstFacturasRecibo = lstFacturasRecibo;
	}
	public HtmlInputTextarea getTxtConcepto() {
		return txtConcepto;
	}
	public void setTxtConcepto(HtmlInputTextarea txtConcepto) {
		this.txtConcepto = txtConcepto;
	}
	public HtmlOutputText getTxtDetalleCambio() {
		return txtDetalleCambio;
	}
	public void setTxtDetalleCambio(HtmlOutputText txtDetalleCambio) {
		this.txtDetalleCambio = txtDetalleCambio;
	}
	public HtmlOutputText getTxtDRCodCli() {
		return txtDRCodCli;
	}
	public void setTxtDRCodCli(HtmlOutputText txtDRCodCli) {
		this.txtDRCodCli = txtDRCodCli;
	}
	public HtmlOutputText getTxtDRNomCliente() {
		return txtDRNomCliente;
	}
	public void setTxtDRNomCliente(HtmlOutputText txtDRNomCliente) {
		this.txtDRNomCliente = txtDRNomCliente;
	}
	public HtmlOutputText getTxtHoraRecibo() {
		return txtHoraRecibo;
	}
	public void setTxtHoraRecibo(HtmlOutputText txtHoraRecibo) {
		this.txtHoraRecibo = txtHoraRecibo;
	}
	public HtmlOutputText getTxtMontoAplicar() {
		return txtMontoAplicar;
	}
	public void setTxtMontoAplicar(HtmlOutputText txtMontoAplicar) {
		this.txtMontoAplicar = txtMontoAplicar;
	}
	public HtmlOutputText getTxtMontoRecibido() {
		return txtMontoRecibido;
	}
	public void setTxtMontoRecibido(HtmlOutputText txtMontoRecibido) {
		this.txtMontoRecibido = txtMontoRecibido;
	}
	public HtmlOutputText getTxtNoBatch() {
		return txtNoBatch;
	}
	public void setTxtNoBatch(HtmlOutputText txtNoBatch) {
		this.txtNoBatch = txtNoBatch;
	}
	public HtmlOutputText getTxtNoRecibo() {
		return txtNoRecibo;
	}
	public void setTxtNoRecibo(HtmlOutputText txtNoRecibo) {
		this.txtNoRecibo = txtNoRecibo;
		
	}
	// ------ variables de Egresos y/o Ajustes ----------/ 
	public HtmlOutputText getLblTotalVtsCredito() {
		return lblTotalVtsCredito;
	}
	public void setLblTotalVtsCredito(HtmlOutputText lblTotalVtsCredito) {
		this.lblTotalVtsCredito = lblTotalVtsCredito;
	}
	public HtmlOutputText getLblTotalVtsPagBanco() {
		return lblTotalVtsPagBanco;
	}
	public void setLblTotalVtsPagBanco(HtmlOutputText lblTotalVtsPagBanco) {
		this.lblTotalVtsPagBanco = lblTotalVtsPagBanco;
	}
	public HtmlOutputText getLblVtsDDbanco() {
		return lblVtsDDbanco;
	}
	public void setLblVtsDDbanco(HtmlOutputText lblVtsDDbanco) {
		this.lblVtsDDbanco = lblVtsDDbanco;
	}
	public HtmlOutputText getLblVtsTCredito() {
		return lblVtsTCredito;
	}
	public void setLblVtsTCredito(HtmlOutputText lblVtsTCredito) {
		this.lblVtsTCredito = lblVtsTCredito;
	}
	public HtmlOutputText getLblVtsTransBanc() {
		return lblVtsTransBanc;
	}
	public void setLblVtsTransBanc(HtmlOutputText lblVtsTransBanc) {
		this.lblVtsTransBanc = lblVtsTransBanc;
	}
	//--------------- abonos pagados en banco --------------------//
	public HtmlOutputText getLblAbonoDDbanco() {
		return lblAbonoDDbanco;
	}
	public void setLblAbonoDDbanco(HtmlOutputText lblAbonoDDbanco) {
		this.lblAbonoDDbanco = lblAbonoDDbanco;
	}
	public HtmlOutputText getLblAbonoTCredito() {
		return lblAbonoTCredito;
	}
	public void setLblAbonoTCredito(HtmlOutputText lblAbonoTCredito) {
		this.lblAbonoTCredito = lblAbonoTCredito;
	}
	public HtmlOutputText getLblAbonoTransBanc() {
		return lblAbonoTransBanc;
	}
	public void setLblAbonoTransBanc(HtmlOutputText lblAbonoTransBanc) {
		this.lblAbonoTransBanc = lblAbonoTransBanc;
	}
	public HtmlOutputText getLblTotalAbonoPagBanco() {
		return lblTotalAbonoPagBanco;
	}
	public void setLblTotalAbonoPagBanco(HtmlOutputText lblTotalAbonoPagBanco) {
		this.lblTotalAbonoPagBanco = lblTotalAbonoPagBanco;
	}
	public HtmlOutputText getLblPrimasDDbanco() {
		return lblPrimasDDbanco;
	}
	public void setLblPrimasDDbanco(HtmlOutputText lblPrimasDDbanco) {
		this.lblPrimasDDbanco = lblPrimasDDbanco;
	}
	public HtmlOutputText getLblPrimasTCredito() {
		return lblPrimasTCredito;
	}
	public void setLblPrimasTCredito(HtmlOutputText lblPrimasTCredito) {
		this.lblPrimasTCredito = lblPrimasTCredito;
	}
	public HtmlOutputText getLblPrimasTransBanc() {
		return lblPrimasTransBanc;
	}
	public void setLblPrimasTransBanc(HtmlOutputText lblPrimasTransBanc) {
		this.lblPrimasTransBanc = lblPrimasTransBanc;
	}
	public HtmlOutputText getLblTotalPrimasPagBanco() {
		return lblTotalPrimasPagBanco;
	}
	public void setLblTotalPrimasPagBanco(HtmlOutputText lblTotalPrimasPagBanco) {
		this.lblTotalPrimasPagBanco = lblTotalPrimasPagBanco;
	}
	public HtmlOutputText getLblOEcambios() {
		return lblOEcambios;
	}
	public void setLblOEcambios(HtmlOutputText lblOEcambios) {
		this.lblOEcambios = lblOEcambios;
	}	
	public HtmlOutputText getLblOEsalidas() {
		return lblOEsalidas;
	}
	public void setLblOEsalidas(HtmlOutputText lblOEsalidas) {
		this.lblOEsalidas = lblOEsalidas;
	}
	public HtmlOutputText getLblTotalOtrosEgresos() {
		return lblTotalOtrosEgresos;
	}
	public void setLblTotalOtrosEgresos(HtmlOutputText lblTotalOtrosEgresos) {
		this.lblTotalOtrosEgresos = lblTotalOtrosEgresos;
	}
	public HtmlOutputText getLblTotalEgresos() {
		return lblTotalEgresos;
	}
	public void setLblTotalEgresos(HtmlOutputText lblTotalEgresos) {
		this.lblTotalEgresos = lblTotalEgresos;
	}
	public HtmlDialogWindow getDwDetalleCambios() {
		return dwDetalleCambios;
	}
	public void setDwDetalleCambios(HtmlDialogWindow dwDetalleCambios) {
		this.dwDetalleCambios = dwDetalleCambios;
	}
	public HtmlGridView getGvDetalleDCambios() {
		return gvDetalleDCambios;
	}
	public void setGvDetalleDCambios(HtmlGridView gvDetalleDCambios) {
		this.gvDetalleDCambios = gvDetalleDCambios;
	}
	public List getLstDetalleCambios() {
		try{
			if(m.get("lstDetalleCambios")==null)
				lstDetalleCambios = new ArrayList();
			else
				lstDetalleCambios = (ArrayList)m.get("lstDetalleCambios");
		}catch(Exception error){
			error.printStackTrace();
		}
		return lstDetalleCambios;
	}
	public void setLstDetalleCambios(List lstDetalleCambios) {
		this.lstDetalleCambios = lstDetalleCambios;
	}
	public HtmlDialogWindowHeader getHdFactura() {
		return hdFactura;
	}
	public void setHdFactura(HtmlDialogWindowHeader hdFactura) {
		this.hdFactura = hdFactura;
	}
	// --------------  variables del calculo del deposito de caja. --------------------
	public HtmlOutputText getLblCDC_depositoFinal() {
		return lblCDC_depositoFinal;
	}
	public void setLblCDC_depositoFinal(HtmlOutputText lblCDC_depositoFinal) {
		this.lblCDC_depositoFinal = lblCDC_depositoFinal;
	}
	public HtmlOutputText getLblCDC_depositoSug() {
		return lblCDC_depositoSug;
	}
	public void setLblCDC_depositoSug(HtmlOutputText lblCDC_depositoSug) {
		this.lblCDC_depositoSug = lblCDC_depositoSug;
	}
	public HtmlOutputText getLblCDC_efectnetoRec() {
		return lblCDC_efectnetoRec;
	}
	public void setLblCDC_efectnetoRec(HtmlOutputText lblCDC_efectnetoRec) {
		this.lblCDC_efectnetoRec = lblCDC_efectnetoRec;
	}
	public HtmlOutputText getLblCDC_montominimo() {
		return lblCDC_montominimo;
	}
	public void setLblCDC_montominimo(HtmlOutputText lblCDC_montominimo) {
		this.lblCDC_montominimo = lblCDC_montominimo;
	}
	public HtmlOutputText getLblCDC_SobranteFaltante() {
		return lblCDC_SobranteFaltante;
	}
	public void setLblCDC_SobranteFaltante(HtmlOutputText lblCDC_SobranteFaltante) {
		this.lblCDC_SobranteFaltante = lblCDC_SobranteFaltante;
	}
	public HtmlOutputText getLblet_SobranteFaltante() {
		return lblet_SobranteFaltante;
	}
	public void setLblet_SobranteFaltante(HtmlOutputText lblet_SobranteFaltante) {
		this.lblet_SobranteFaltante = lblet_SobranteFaltante;
	}
	public HtmlInputText getTxtCDC_efectivoenCaja() {
		return txtCDC_efectivoenCaja;
	}
	public void setTxtCDC_efectivoenCaja(HtmlInputText txtCDC_efectivoenCaja) {
		this.txtCDC_efectivoenCaja = txtCDC_efectivoenCaja;
	}
	public HtmlInputText getTxtCDC_ReferDeposito() {
		return txtCDC_ReferDeposito;
	}
	public void setTxtCDC_ReferDeposito(HtmlInputText txtCDC_ReferDeposito) {
		this.txtCDC_ReferDeposito = txtCDC_ReferDeposito;
	}
	public HtmlOutputText getLblCDC_pagocheques() {
		return lblCDC_pagocheques;
	}
	public void setLblCDC_pagocheques(HtmlOutputText lblCDC_pagocheques) {
		this.lblCDC_pagocheques = lblCDC_pagocheques;
	}
	public HtmlDialogWindowHeader getHdRecxTipoIng() {
		return hdRecxTipoIng;
	}
	public void setHdRecxTipoIng(HtmlDialogWindowHeader hdRecxTipoIng) {
		this.hdRecxTipoIng = hdRecxTipoIng;
	}
	public HtmlOutputText getLblACDetfp_Cheques() {
		return lblACDetfp_Cheques;
	}
	public void setLblACDetfp_Cheques(HtmlOutputText lblACDetfp_Cheques) {
		this.lblACDetfp_Cheques = lblACDetfp_Cheques;
	}
	public HtmlOutputText getLblACDetfp_DepDbanco() {
		return lblACDetfp_DepDbanco;
	}
	public void setLblACDetfp_DepDbanco(HtmlOutputText lblACDetfp_DepDbanco) {
		this.lblACDetfp_DepDbanco = lblACDetfp_DepDbanco;
	}
	public HtmlOutputText getLblACDetfp_Efectivo() {
		return lblACDetfp_Efectivo;
	}
	public void setLblACDetfp_Efectivo(HtmlOutputText lblACDetfp_Efectivo) {
		this.lblACDetfp_Efectivo = lblACDetfp_Efectivo;
	}
	public HtmlOutputText getLblACDetfp_TarCred() {
		return lblACDetfp_TarCred;
	}
	public void setLblACDetfp_TarCred(HtmlOutputText lblACDetfp_TarCred) {
		this.lblACDetfp_TarCred = lblACDetfp_TarCred;
	}
	public HtmlOutputText getLblACDetfp_TransBanco() {
		return lblACDetfp_TransBanco;
	}
	public void setLblACDetfp_TransBanco(HtmlOutputText lblACDetfp_TransBanco) {
		this.lblACDetfp_TransBanco = lblACDetfp_TransBanco;
	}
	public HtmlOutputText getLblACDetfp_Total() {
		return lblACDetfp_Total;
	}
	public void setLblACDetfp_Total(HtmlOutputText lblACDetfp_Total) {
		this.lblACDetfp_Total = lblACDetfp_Total;
	}
	// ------------- variables para recibos por Tipo recibo y metodos de pago.
	public HtmlDialogWindow getDwDetTipoReciboxMetodoPago() {
		return dwDetTipoReciboxMetodoPago;
	}
	public void setDwDetTipoReciboxMetodoPago(
			HtmlDialogWindow dwDetTipoReciboxMetodoPago) {
		this.dwDetTipoReciboxMetodoPago = dwDetTipoReciboxMetodoPago;
	}
	public HtmlGridView getGvRecibosxTipoyMetodopago() {
		return gvRecibosxTipoyMetodopago;
	}
	public void setGvRecibosxTipoyMetodopago(HtmlGridView gvRecibosxTipoyMetodopago) {
		this.gvRecibosxTipoyMetodopago = gvRecibosxTipoyMetodopago;
	}
	public HtmlDialogWindowHeader getHdDetTrecxMetPago() {
		return hdDetTrecxMetPago;
	}
	public void setHdDetTrecxMetPago(HtmlDialogWindowHeader hdDetTrecxMetPago) {
		this.hdDetTrecxMetPago = hdDetTrecxMetPago;
	}
	public List getLstRecxTipoyMetpago() {
		try{
			if(m.get("lstRecxTipoyMetpago")==null)
				lstRecxTipoyMetpago = new ArrayList();
			else
				lstRecxTipoyMetpago = (ArrayList)m.get("lstRecxTipoyMetpago");
		}catch(Exception error){
			error.printStackTrace();
		}
		return lstRecxTipoyMetpago;
	}
	public void setLstRecxTipoyMetpago(List lstRecxTipoyMetpago) {
		this.lstRecxTipoyMetpago = lstRecxTipoyMetpago;
	}
    /* Variables para las ventanas de validar y procesar arqueo */
	public HtmlDialogWindow getDwValidarArqueo() {
		return dwValidarArqueo;
	}
	public void setDwValidarArqueo(HtmlDialogWindow dwValidarArqueo) {
		this.dwValidarArqueo = dwValidarArqueo;
	}
	public HtmlOutputText getLblValidarArqueo() {
		return lblValidarArqueo;
	}
	public void setLblValidarArqueo(HtmlOutputText lblValidarArqueo) {
		this.lblValidarArqueo = lblValidarArqueo;
	}
	public HtmlDialogWindow getDwConfirmarProcesarArqueo() {
		return dwConfirmarProcesarArqueo;
	}
	public void setDwConfirmarProcesarArqueo(
			HtmlDialogWindow dwConfirmarProcesarArqueo) {
		this.dwConfirmarProcesarArqueo = dwConfirmarProcesarArqueo;
	}
	public HtmlDialogWindow getDwDetallerecpagmonEx() {
		return dwDetallerecpagmonEx;
	}
	public void setDwDetallerecpagmonEx(HtmlDialogWindow dwDetallerecpagmonEx) {
		this.dwDetallerecpagmonEx = dwDetallerecpagmonEx;
	}
	public HtmlGridView getGvDetRecpagMonEx() {
		return gvDetRecpagMonEx;
	}
	public void setGvDetRecpagMonEx(HtmlGridView gvDetRecpagMonEx) {
		this.gvDetRecpagMonEx = gvDetRecpagMonEx;
	}
	public HtmlDialogWindowHeader getHdDetrecpagmonEx() {
		return hdDetrecpagmonEx;
	}
	public void setHdDetrecpagmonEx(HtmlDialogWindowHeader hdDetrecpagmonEx) {
		this.hdDetrecpagmonEx = hdDetrecpagmonEx;
	}
	public List getLstDetRecpagMonEx() {
		if(m.get("ac_lstDetRecpagMonEx")== null)
			lstDetRecpagMonEx = new ArrayList();
		else
			lstDetRecpagMonEx = (ArrayList)m.get("ac_lstDetRecpagMonEx");
		return lstDetRecpagMonEx;
	}
	public void setLstDetRecpagMonEx(List lstDetRecpagMonEx) {
		this.lstDetRecpagMonEx = lstDetRecpagMonEx;
	}
	public HtmlDialogWindow getDwConfImprRptpre() {
		return dwConfImprRptpre;
	}
	public void setDwConfImprRptpre(HtmlDialogWindow dwConfImprRptpre) {
		this.dwConfImprRptpre = dwConfImprRptpre;
	}
	public HtmlDialogWindow getDwMsgProcesarArq() {
		return dwMsgProcesarArq;
	}
	public void setDwMsgProcesarArq(HtmlDialogWindow dwMsgProcesarArq) {
		this.dwMsgProcesarArq = dwMsgProcesarArq;
	}
	public HtmlOutputText getLblMsgProcArqueo() {
		return lblMsgProcArqueo;
	}
	public void setLblMsgProcArqueo(HtmlOutputText lblMsgProcArqueo) {
		this.lblMsgProcArqueo = lblMsgProcArqueo;
	}
	public HtmlColumn getCoNoFactura2() {
		return coNoFactura2;
	}
	public void setCoNoFactura2(HtmlColumn coNoFactura2) {
		this.coNoFactura2 = coNoFactura2;
	}
	public HtmlOutputText getLblFecha23() {
		return lblFecha23;
	}
	public void setLblFecha23(HtmlOutputText lblFecha23) {
		this.lblFecha23 = lblFecha23;
	}
	public HtmlOutputText getLblMoneda2() {
		return lblMoneda2;
	}
	public void setLblMoneda2(HtmlOutputText lblMoneda2) {
		this.lblMoneda2 = lblMoneda2;
	}
	public HtmlOutputText getLblNoFactura2() {
		return lblNoFactura2;
	}
	public void setLblNoFactura2(HtmlOutputText lblNoFactura2) {
		this.lblNoFactura2 = lblNoFactura2;
	}
	public HtmlOutputText getLblPartida23() {
		return lblPartida23;
	}
	public void setLblPartida23(HtmlOutputText lblPartida23) {
		this.lblPartida23 = lblPartida23;
	}
	public HtmlOutputText getLblTipofactura2() {
		return lblTipofactura2;
	}
	public void setLblTipofactura2(HtmlOutputText lblTipofactura2) {
		this.lblTipofactura2 = lblTipofactura2;
	}
	public HtmlOutputText getLblUnineg2() {
		return lblUnineg2;
	}
	public void setLblUnineg2(HtmlOutputText lblUnineg2) {
		this.lblUnineg2 = lblUnineg2;
	}
	public HtmlDialogWindow getDwDetalleSalidas() {
		return dwDetalleSalidas;
	}
	public void setDwDetalleSalidas(HtmlDialogWindow dwDetalleSalidas) {
		this.dwDetalleSalidas = dwDetalleSalidas;
	}
	public HtmlGridView getGvDetalleSalidas() {
		return gvDetalleSalidas;
	}
	public void setGvDetalleSalidas(HtmlGridView gvDetalleSalidas) {
		this.gvDetalleSalidas = gvDetalleSalidas;
	}
	public List getLstDetalleSalidas() {
		try {
			lstDetalleSalidas = m.get("ac_lstDetalleSalidas")==null? new ArrayList(): (ArrayList)m.get("ac_lstDetalleSalidas");
		} catch (Exception error) {
			error.printStackTrace();
		}
		return lstDetalleSalidas;
	}
	public void setLstDetalleSalidas(List lstDetalleSalidas) {
		this.lstDetalleSalidas = lstDetalleSalidas;
	}
	public HtmlOutputText getLbliniexDepBanco() {
		return lbliniexDepBanco;
	}
	public void setLbliniexDepBanco(HtmlOutputText lbliniexDepBanco) {
		this.lbliniexDepBanco = lbliniexDepBanco;
	}
	public HtmlOutputText getLbliniexTransBanco() {
		return lbliniexTransBanco;
	}
	public void setLbliniexTransBanco(HtmlOutputText lbliniexTransBanco) {
		this.lbliniexTransBanco = lbliniexTransBanco;
	}
	public HtmlOutputText getLblegIex8() {
		return lblegIex8;
	}
	public void setLblegIex8(HtmlOutputText lblegIex8) {
		this.lblegIex8 = lblegIex8;
	}
	public HtmlOutputText getLblegIexH() {
		return lblegIexH;
	}
	public void setLblegIexH(HtmlOutputText lblegIexH) {
		this.lblegIexH = lblegIexH;
	}
	public HtmlOutputText getLblegIexN() {
		return lblegIexN;
	}
	public void setLblegIexN(HtmlOutputText lblegIexN) {
		this.lblegIexN = lblegIexN;
	}
	public HtmlOutputText getLblegTotalIex() {
		return lblegTotalIex;
	}
	public void setLblegTotalIex(HtmlOutputText lblegTotalIex) {
		this.lblegTotalIex = lblegTotalIex;
	}
	public HtmlOutputText getLbletMsgArqueoPrevio() {
		return lbletMsgArqueoPrevio;
	}
	public void setLbletMsgArqueoPrevio(HtmlOutputText lbletMsgArqueoPrevio) {
		this.lbletMsgArqueoPrevio = lbletMsgArqueoPrevio;
	}
	public HtmlOutputText getLblMsgArqueoPrevio() {
		return lblMsgArqueoPrevio;
	}
	public void setLblMsgArqueoPrevio(HtmlOutputText lblMsgArqueoPrevio) {
		this.lblMsgArqueoPrevio = lblMsgArqueoPrevio;
	}
	public HtmlOutputText getLblavisoFaltanteArqueo() {
		return lblavisoFaltanteArqueo;
	}
	public void setLblavisoFaltanteArqueo(HtmlOutputText lblavisoFaltanteArqueo) {
		this.lblavisoFaltanteArqueo = lblavisoFaltanteArqueo;
	}
	public HtmlOutputText getLblTotalFinanciamiento() {
		return lblTotalFinanciamiento;
	}
	public void setLblTotalFinanciamiento(HtmlOutputText lblTotalFinanciamiento) {
		this.lblTotalFinanciamiento = lblTotalFinanciamiento;
	}
	public HtmlOutputText getLblFinanDbanco() {
		return lblFinanDbanco;
	}
	public void setLblFinanDbanco(HtmlOutputText lblFinanDbanco) {
		this.lblFinanDbanco = lblFinanDbanco;
	}
	public HtmlOutputText getLblFinanTCredito() {
		return lblFinanTCredito;
	}
	public void setLblFinanTCredito(HtmlOutputText lblFinanTCredito) {
		this.lblFinanTCredito = lblFinanTCredito;
	}
	public HtmlOutputText getLblFinanTransBanc() {
		return lblFinanTransBanc;
	}
	public void setLblFinanTransBanc(HtmlOutputText lblFinanTransBanc) {
		this.lblFinanTransBanc = lblFinanTransBanc;
	}
	public HtmlOutputText getLblTotalFinanPagBanco() {
		return lblTotalFinanPagBanco;
	}
	public void setLblTotalFinanPagBanco(HtmlOutputText lblTotalFinanPagBanco) {
		this.lblTotalFinanPagBanco = lblTotalFinanPagBanco;
	}
	public HtmlOutputText getLblACDetfp_TCmanual() {
		return lblACDetfp_TCmanual;
	}
	public void setLblACDetfp_TCmanual(HtmlOutputText lblACDetfp_TCmanual) {
		this.lblACDetfp_TCmanual = lblACDetfp_TCmanual;
	}
	public HtmlCheckBox getChkArqueoDiaAnterior() {
		return chkArqueoDiaAnterior;
	}
	public void setChkArqueoDiaAnterior(HtmlCheckBox chkArqueoDiaAnterior) {
		Vautoriz[] vAut = null;
		try {
			
			if(chkArqueoDiaAnterior == null || !m.containsKey("sevAut")) return;
			
			chkArqueoDiaAnterior.setStyle("display:none");
			vAut = (Vautoriz[])m.get("sevAut");
			for (Vautoriz va : vAut) {
				if(	va.getId().getCodaut().equals("A000000095")) { 
					chkArqueoDiaAnterior.setStyle("display:inline");
					chkArqueoDiaAnterior.setStyleClass("frmLabel2");
					break;
				}
			}
			vAut = null;
		} catch (Exception error) {
			error.printStackTrace();
		}
		this.chkArqueoDiaAnterior = chkArqueoDiaAnterior;
	}
	public HtmlOutputText getLblChkDiaAnterior() {
		return lblChkDiaAnterior;
	}
	public void setLblChkDiaAnterior(HtmlOutputText lblChkDiaAnterior) {
		Vautoriz[] vAut = null;
		try {
			
			if(lblChkDiaAnterior == null || !m.containsKey("sevAut")) return;
				
			lblChkDiaAnterior.setStyle("display:none");
			vAut = (Vautoriz[])m.get("sevAut");
			for (Vautoriz va : vAut) {
				if(	va.getId().getCodaut().equals("A000000095")){
					lblChkDiaAnterior.setStyle("display:inline");
					lblChkDiaAnterior.setStyleClass("frmLabel2");
					break;
				}
			}
			vAut = null;
		} catch (Exception error) {
			error.printStackTrace();
		}
		this.lblChkDiaAnterior = lblChkDiaAnterior;
	}
	public HtmlDateChooser getDcFechaArqueoAnterior() {
		return dcFechaArqueoAnterior;
	}
	public void setDcFechaArqueoAnterior(HtmlDateChooser dcFechaArqueoAnterior) {
		this.dcFechaArqueoAnterior = dcFechaArqueoAnterior;
	}
	public HtmlOutputText getLbletFechaArqueo() {
		return lbletFechaArqueo;
	}
	public void setLbletFechaArqueo(HtmlOutputText lbletFechaArqueo) {
		this.lbletFechaArqueo = lbletFechaArqueo;
	}
	public HtmlPanelGrid getPgrHoraArqueo() {
		return pgrHoraArqueo;
	}
	public void setPgrHoraArqueo(HtmlPanelGrid pgrHoraArqueo) {
		this.pgrHoraArqueo = pgrHoraArqueo;
	}

	public HtmlDropDownList getDdlArqueoHFin() {
		return ddlArqueoHFin;
	}

	public void setDdlArqueoHFin(HtmlDropDownList ddlArqueoHFin) {
		this.ddlArqueoHFin = ddlArqueoHFin;
	}

	public HtmlDropDownList getDdlArqueoHini() {
		return ddlArqueoHini;
	}
	public void setDdlArqueoHini(HtmlDropDownList ddlArqueoHini) {
		this.ddlArqueoHini = ddlArqueoHini;
	}
	public HtmlDropDownList getDdlArqueoMini() {
		return ddlArqueoMini;
	}
	public void setDdlArqueoMini(HtmlDropDownList ddlArqueoMini) {
		this.ddlArqueoMini = ddlArqueoMini;
	}
	public List getLstArqueoHFin() {
		try {
			NumberFormat ft = new DecimalFormat("00");
			List<SelectItem>Horas = new ArrayList<SelectItem>(12);
			for (int i = 23; i > 0; i--)
				Horas.add(new SelectItem(ft.format(i),ft.format(i),"Hora Final: "+i));
			lstArqueoHFin = Horas;
		} catch (Exception error) {
			error.printStackTrace();
		}
		return lstArqueoHFin;
	}
	public void setLstArqueoHFin(List lstArqueoHFin) {
		this.lstArqueoHFin = lstArqueoHFin;
	}
	public List getLstArqueoHini() {
		try {
			NumberFormat ft = new DecimalFormat("00");
			List<SelectItem>Horas = new ArrayList<SelectItem>(12);
			for (int i = 0; i < 24; i++)
				Horas.add(new SelectItem(ft.format(i),ft.format(i),"Hora Inicio: "+i));
			lstArqueoHini = Horas;
		} catch (Exception error) {
			error.printStackTrace();
		}
		return lstArqueoHini;
	}
	public void setLstArqueoHini(List lstArqueoHini) {
		this.lstArqueoHini = lstArqueoHini;
	}
	public List getLstArqueoMFin() {
		try {
			NumberFormat ft = new DecimalFormat("00");
			List<SelectItem>Minutos = new ArrayList<SelectItem>(60);
			for (int i = 0; i <60; i++)
				Minutos.add(new SelectItem(ft.format(i),ft.format(i),"Minuto Final: "+i));
			lstArqueoMFin = Minutos;
		} catch (Exception error) {
			error.printStackTrace();
		}
		return lstArqueoMFin;
	}
	public void setLstArqueoMFin(List lstArqueoMFin) {
		this.lstArqueoMFin = lstArqueoMFin;
	}
	public List getLstArqueoMini() {
		try {
			NumberFormat ft = new DecimalFormat("00");
			List<SelectItem>Minutos = new ArrayList<SelectItem>(60);
			for (int i = 0; i <60; i++)
				Minutos.add(new SelectItem(ft.format(i),ft.format(i),"Minuto Inicial: "+i));
			lstArqueoMini = Minutos;
		} catch (Exception error) {
			error.printStackTrace();
		}
		return lstArqueoMini;
	}
	public void setLstArqueoMini(List lstArqueoMini) {
		this.lstArqueoMini = lstArqueoMini;
	}
	public HtmlDropDownList getDdlArqueoMFin() {
		return ddlArqueoMFin;
	}
	public void setDdlArqueoMFin(HtmlDropDownList ddlArqueoMFin) {
		this.ddlArqueoMFin = ddlArqueoMFin;
	}
	public HtmlDialogWindow getDwCargando() {
		return dwCargando;
	}
	public void setDwCargando(HtmlDialogWindow dwCargando) {
		this.dwCargando = dwCargando;
	}
	public HtmlOutputText getLblACDetfp_TCSocketpos() {
		return lblACDetfp_TCSocketpos;
	}
	public void setLblACDetfp_TCSocketpos(HtmlOutputText lblACDetfp_TCSocketpos) {
		this.lblACDetfp_TCSocketpos = lblACDetfp_TCSocketpos;
	}
	public HtmlOutputText getLblCDC_montoMinReint() {
		return lblCDC_montoMinReint;
	}
	public void setLblCDC_montoMinReint(HtmlOutputText lblCDC_montoMinReint) {
		this.lblCDC_montoMinReint = lblCDC_montoMinReint;
	}
	public HtmlOutputText getLblCDC_montoMinAjust() {
		return lblCDC_montoMinAjust;
	}
	public void setLblCDC_montoMinAjust(HtmlOutputText lblCDC_montoMinAjust) {
		this.lblCDC_montoMinAjust = lblCDC_montoMinAjust;
	}
	public String getRutaarqueo() {
		rutaarqueo = "";
		if(m.containsKey("ac_rutaarqueo"))
			rutaarqueo = String.valueOf(m.get("ac_rutaarqueo"));
		
		return rutaarqueo;
	}
	public void setRutaarqueo(String rutaarqueo) {
		this.rutaarqueo = rutaarqueo;
	}
	public String getRutaarqueoprelm() {
		rutaarqueoprelm = "";
		if(m.containsKey("ac_rutaarqueoprelm"))
			rutaarqueoprelm = String.valueOf(m.get("ac_rutaarqueoprelm"));
		return rutaarqueoprelm;
	}
	public void setRutaarqueoprelm(String rutaarqueoprelm) {
		this.rutaarqueoprelm = rutaarqueoprelm;
	}
	
	// * Resumen de Transacciones del Socket Pos //
	public boolean isRenderCierrePOS() {
		try {

			if (!m.containsKey("ac_TransSocketFloat")) {
				List<Transactsp> transacts = PosCtrl
						.getTransactionCaid(((List<Vf55ca01>) m.get("lstCajas"))
								.get(0).getId().getCaid());
				boolean existTrans = (transacts != null && !transacts.isEmpty());
				m.put("ac_TransSocketFloat", existTrans);
			}

			renderCierrePOS = (Boolean.parseBoolean(String.valueOf(m
					.get("ac_TransSocketFloat"))))
					|| (((List<Vf55ca01>) m.get("lstCajas")).get(0).getId()
							.getCasktpos() == 'Y');
		} catch (Exception e) {
			renderCierrePOS = false;
		}
		return renderCierrePOS;
	}

	public void setRenderCierrePOS(boolean renderCierrePOS) {
		this.renderCierrePOS = renderCierrePOS;
	}

	public HtmlDialogWindow getDwRsmTransactSocketPos() {
		return dwRsmTransactSocketPos;
	}

	public void setDwRsmTransactSocketPos(
			HtmlDialogWindow dwRsmTransactSocketPos) {
		this.dwRsmTransactSocketPos = dwRsmTransactSocketPos;
	}

	public HtmlGridView getGvRsmTransactTerminales() {
		return gvRsmTransactTerminales;
	}

	public void setGvRsmTransactTerminales(HtmlGridView gvRsmTransactTerminales) {
		this.gvRsmTransactTerminales = gvRsmTransactTerminales;
	}

	public List<TransaccionTerminal> getRsmTerminales() {
		rsmTerminales = new ArrayList<TransaccionTerminal>();
		if (m.containsKey("ac_rsmTerminales")) {
			rsmTerminales = (ArrayList<TransaccionTerminal>) m
					.get("ac_rsmTerminales");
		}
		return rsmTerminales;
	}

	public void setRsmTerminales(List<TransaccionTerminal> rsmTerminales) {
		this.rsmTerminales = rsmTerminales;
	}

	public HtmlDialogWindow getDwTransaccionesPOS() {
		return dwTransaccionesPOS;
	}

	public void setDwTransaccionesPOS(HtmlDialogWindow dwTransaccionesPOS) {
		this.dwTransaccionesPOS = dwTransaccionesPOS;
	}

	public HtmlGridView getGvTransaccionesPOS() {
		return gvTransaccionesPOS;
	}

	public void setGvTransaccionesPOS(HtmlGridView gvTransaccionesPOS) {
		this.gvTransaccionesPOS = gvTransaccionesPOS;
	}

	public List<Transactsp> getLstTransaccionesPOS() {

		lstTransaccionesPOS = new ArrayList<Transactsp>();
		if (m.containsKey("ac_lstTransaccionesPOS")) {
			lstTransaccionesPOS = (ArrayList<Transactsp>) m
					.get("ac_lstTransaccionesPOS");
		}
		return lstTransaccionesPOS;
	}

	public void setLstTransaccionesPOS(List<Transactsp> lstTransaccionesPOS) {
		this.lstTransaccionesPOS = lstTransaccionesPOS;
	}

	public HtmlDialogWindow getDwConfirmaCierreTerminal() {
		return dwConfirmaCierreTerminal;
	}

	public void setDwConfirmaCierreTerminal(
			HtmlDialogWindow dwConfirmaCierreTerminal) {
		this.dwConfirmaCierreTerminal = dwConfirmaCierreTerminal;
	}

	public String getLblDtaTerminalCierre() {
		return lblDtaTerminalCierre;
	}

	public void setLblDtaTerminalCierre(String lblDtaTerminalCierre) {
		this.lblDtaTerminalCierre = lblDtaTerminalCierre;
	}

	public HtmlDialogWindow getDwValidaSocketPos() {
		return dwValidaSocketPos;
	}

	public void setDwValidaSocketPos(HtmlDialogWindow dwValidaSocketPos) {
		this.dwValidaSocketPos = dwValidaSocketPos;
	}

	public String getLblMsgValidaSocketPos() {
		return lblMsgValidaSocketPos;
	}

	public void setLblMsgValidaSocketPos(String lblMsgValidaSocketPos) {
		this.lblMsgValidaSocketPos = lblMsgValidaSocketPos;
	}

	public HtmlDialogWindow getDwDwnCierreRpt() {
		return dwDwnCierreRpt;
	}

	public void setDwDwnCierreRpt(HtmlDialogWindow dwDwnCierreRpt) {
		this.dwDwnCierreRpt = dwDwnCierreRpt;
	}
	public HtmlLink getLnkCerrarTermSinTransact() {
		return lnkCerrarTermSinTransact;
	}
	public void setLnkCerrarTermSinTransact(HtmlLink lnkCerrarTermSinTransact) {
		this.lnkCerrarTermSinTransact = lnkCerrarTermSinTransact;
	}

	public HtmlGridView getGvDonacionesFormaPago() {
		return gvDonacionesFormaPago;
	}

	public void setGvDonacionesFormaPago(HtmlGridView gvDonacionesFormaPago) {
		this.gvDonacionesFormaPago = gvDonacionesFormaPago;
	}

	public List<DncDonacion> getLstDonacionesFormaPago() {
		
		if( CodeUtil.getFromSessionMap("ac_lstDonacionesFormaPago") != null )
			lstDonacionesFormaPago = (List<DncDonacion>)CodeUtil.getFromSessionMap("ac_lstDonacionesFormaPago");
		
		return  lstDonacionesFormaPago == null ? new ArrayList<DncDonacion>(): lstDonacionesFormaPago;
	}

	public void setLstDonacionesFormaPago(List<DncDonacion> lstDonacionesFormaPago) {
		this.lstDonacionesFormaPago = lstDonacionesFormaPago;
	}

	public HtmlDialogWindow getDwDetalleDonacionesMpago() {
		return dwDetalleDonacionesMpago;
	}

	public void setDwDetalleDonacionesMpago(
			HtmlDialogWindow dwDetalleDonacionesMpago) {
		this.dwDetalleDonacionesMpago = dwDetalleDonacionesMpago;
	}

	public HtmlGridView getGvDetalleDonacionesMpago() {
		return gvDetalleDonacionesMpago;
	}

	public void setGvDetalleDonacionesMpago(HtmlGridView gvDetalleDonacionesMpago) {
		this.gvDetalleDonacionesMpago = gvDetalleDonacionesMpago;
	}

	public List<DncDonacion> getLstDetalleDonacionesMpago() {
		
		if( CodeUtil.getFromSessionMap("ac_lstDetalleDonacionesMpago") != null )
			lstDetalleDonacionesMpago = (ArrayList<DncDonacion>)CodeUtil.getFromSessionMap("ac_lstDetalleDonacionesMpago");
		
		return  lstDetalleDonacionesMpago == null ? new ArrayList<DncDonacion>(): lstDetalleDonacionesMpago;
		
	}

	public void setLstDetalleDonacionesMpago(
			List<DncDonacion> lstDetalleDonacionesMpago) {
		this.lstDetalleDonacionesMpago = lstDetalleDonacionesMpago;
	}

	public HtmlDialogWindow getDwRecibosxMpago8N() {
		return dwRecibosxMpago8N;
	}

	public void setDwRecibosxMpago8N(HtmlDialogWindow dwRecibosxMpago8N) {
		this.dwRecibosxMpago8N = dwRecibosxMpago8N;
	}

	public HtmlDialogWindowHeader getHdReciboMPago8N() {
		return hdReciboMPago8N;
	}

	public void setHdReciboMPago8N(HtmlDialogWindowHeader hdReciboMPago8N) {
		this.hdReciboMPago8N = hdReciboMPago8N;
	}

	public HtmlGridView getGvRecibosxMpago8N() {
		return gvRecibosxMpago8N;
	}

	public void setGvRecibosxMpago8N(HtmlGridView gvRecibosxMpago8N) {
		this.gvRecibosxMpago8N = gvRecibosxMpago8N;
	}

	public List<Vrecibosxtipompago> getLstRecibosxMpago8N() {
		if(CodeUtil.getFromSessionMap( "ac_lstRecibosxMpago8N") == null)
			lstRecibosxMpago8N = new ArrayList<Vrecibosxtipompago>();
		else
			lstRecibosxMpago8N = (ArrayList<Vrecibosxtipompago>)CodeUtil.getFromSessionMap( "ac_lstRecibosxMpago8N");
		return lstRecibosxMpago8N;
	}

	public void setLstRecibosxMpago8N(List<Vrecibosxtipompago> lstRecibosxMpago8N) {
		this.lstRecibosxMpago8N = lstRecibosxMpago8N;
	}
	public HtmlOutputText getLblTotalRecibosPMT() {
		return lblTotalRecibosPMT;
	}

	public void setLblTotalRecibosPMT(HtmlOutputText lblTotalRecibosPMT) {
		this.lblTotalRecibosPMT = lblTotalRecibosPMT;
	}

	public HtmlOutputText getLblpmtTarjetaCredito() {
		return lblpmtTarjetaCredito;
	}

	public void setLblpmtTarjetaCredito(HtmlOutputText lblpmtTarjetaCredito) {
		this.lblpmtTarjetaCredito = lblpmtTarjetaCredito;
	}

	public HtmlOutputText getLblpmtDepositoDirectoBanco() {
		return lblpmtDepositoDirectoBanco;
	}

	public void setLblpmtDepositoDirectoBanco(
			HtmlOutputText lblpmtDepositoDirectoBanco) {
		this.lblpmtDepositoDirectoBanco = lblpmtDepositoDirectoBanco;
	}

	public HtmlOutputText getLblpmtTransferenciaBancaria() {
		return lblpmtTransferenciaBancaria;
	}

	public void setLblpmtTransferenciaBancaria(
			HtmlOutputText lblpmtTransferenciaBancaria) {
		this.lblpmtTransferenciaBancaria = lblpmtTransferenciaBancaria;
	}

	public HtmlOutputText getLblTotalPmtPagoEnBanco() {
		return lblTotalPmtPagoEnBanco;
	}

	public void setLblTotalPmtPagoEnBanco(HtmlOutputText lblTotalPmtPagoEnBanco) {
		this.lblTotalPmtPagoEnBanco = lblTotalPmtPagoEnBanco;
	}
	
	public HtmlJspPanel getPnlDatosFacturas() {
		return pnlDatosFacturas;
	}
	public void setPnlDatosFacturas(HtmlJspPanel pnlDatosFacturas) {
		this.pnlDatosFacturas = pnlDatosFacturas;
	}
	public HtmlJspPanel getPnlDatosAnticiposPMT() {
		return pnlDatosAnticiposPMT;
	}
	public void setPnlDatosAnticiposPMT(HtmlJspPanel pnlDatosAnticiposPMT) {
		this.pnlDatosAnticiposPMT = pnlDatosAnticiposPMT;
	}
	public List<HistoricoReservasProformas> getDetalleContratoPmt() {
		if(CodeUtil.getFromSessionMap("ac_detalleContratoPmt") != null)
			return detalleContratoPmt = (ArrayList<HistoricoReservasProformas>)CodeUtil.getFromSessionMap("ac_detalleContratoPmt");
		else 
			detalleContratoPmt = new ArrayList<HistoricoReservasProformas>();
		
		return detalleContratoPmt;
	}
	public void setDetalleContratoPmt(
			List<HistoricoReservasProformas> detalleContratoPmt) {
		this.detalleContratoPmt = detalleContratoPmt;
	}
	public HtmlGridView getGvDetalleContratoPmt() {
		return gvDetalleContratoPmt;
	}
	public void setGvDetalleContratoPmt(HtmlGridView gvDetalleContratoPmt) {
		this.gvDetalleContratoPmt = gvDetalleContratoPmt;
	}
	
}
package com.casapellas.dao;
/**
 * CASA PELLAS S.A.
 * Creado por.........: Carlos Manuel Hernández Morrison
 * Fecha de Creación..: 31/08/2009
 * Última modificación: Carlos Manuel Hernández Morrison
 * Modificado por.....:	29/11/2011
 * Descripcion:.......: Registro de IVA de la comsion.
 * 
 */

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.faces.application.NavigationHandler;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlInputTextarea;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
//import org.apache.commons.mail.MultiPartEmail;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.casapellas.controles.AfiliadoCtrl;
import com.casapellas.controles.AprobacionCierreCtrl;
import com.casapellas.controles.ArqueoCajaCtrl;
import com.casapellas.controles.ArqueoCtrl;
import com.casapellas.controles.BancoCtrl;
import com.casapellas.controles.ClsParametroCaja;
import com.casapellas.controles.CompaniaCtrl;
import com.casapellas.controles.ConfirmaDepositosCtrl;
import com.casapellas.controles.ConsolidadoDepositosBcoCtrl;
import com.casapellas.controles.CtrlCajas;
import com.casapellas.controles.DonacionesCtrl;
import com.casapellas.controles.EmpleadoCtrl;
import com.casapellas.controles.FacturaCrtl;
import com.casapellas.controles.MetodosPagoCtrl;
import com.casapellas.controles.PlanMantenimientoTotalCtrl;
import com.casapellas.controles.ReciboCtrl;
import com.casapellas.controles.RevisionArqueoCtrl;
import com.casapellas.controles.SalidasCtrl;
import com.casapellas.controles.TasaCambioCtrl;
import com.casapellas.donacion.entidades.DncBeneficiarioCuentas;
import com.casapellas.donacion.entidades.DncCierreDonacion;
import com.casapellas.donacion.entidades.DncDonacion;
import com.casapellas.entidades.Cafiliados;
import com.casapellas.entidades.Cambiodet;
import com.casapellas.entidades.Ctaxdeposito;
import com.casapellas.entidades.DetalleArqueoCaja;
import com.casapellas.entidades.Dfactura;
import com.casapellas.entidades.F55ca01;
import com.casapellas.entidades.F55ca014;
import com.casapellas.entidades.F55ca022;
import com.casapellas.entidades.F55ca03;
import com.casapellas.entidades.Hfactura;
import com.casapellas.entidades.HistoricoReservasProformas;
import com.casapellas.entidades.Recibo;
import com.casapellas.entidades.Recibodet;
import com.casapellas.entidades.RecibodetId;
import com.casapellas.entidades.Recibojde;
import com.casapellas.entidades.Reintegro;
import com.casapellas.entidades.ResumenAfiliado;
import com.casapellas.entidades.Salida;
import com.casapellas.entidades.Tcambio;
import com.casapellas.entidades.Valorcatalogo;
import com.casapellas.entidades.Varqueo;
import com.casapellas.entidades.Vcompania;
import com.casapellas.entidades.Vrecibosxtipoingreso;
import com.casapellas.entidades.Vwcuentascontablesporbanco;
import com.casapellas.entidades.ens.Vautoriz;
import com.casapellas.entidades.Vcierresktpos;
import com.casapellas.entidades.Vcompaniaxcontador;
import com.casapellas.entidades.Vdetallecambiorec;
import com.casapellas.entidades.Vdetallecambiorecibo;
import com.casapellas.entidades.Vf0101;
import com.casapellas.entidades.Vf0901;
import com.casapellas.entidades.Vf55ca01;
import com.casapellas.entidades.Vmonedafactrec;
import com.casapellas.entidades.Vmonedasxcontador;
import com.casapellas.entidades.Vrecibodevrecibo;
import com.casapellas.entidades.Vrecibosxtipoegreso;
import com.casapellas.entidades.Vrecibosxtipompago;
import com.casapellas.entidades.VrecibosxtipompagoId;
import com.casapellas.entidades.Vreciboxdevolucion;
import com.casapellas.entidades.Vsalida;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.jde.creditos.CodigosJDE1;
import com.casapellas.navegacion.As400Connection;
import com.casapellas.reportes.ArqueoCajaR;
import com.casapellas.reportes.ArqueoSocketPos;
import com.casapellas.socketpos.PosCtrl;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.CustomEmailAddress;
import com.casapellas.util.Divisas;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.LiquidacionCheque;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.MailHelper;
import com.casapellas.util.PropertiesSystem;
import com.ibm.faces.component.html.HtmlJspPanel;
import com.ibm.faces.component.html.HtmlProgressBar;
import com.ibm.icu.util.Calendar;
import com.infragistics.faces.grid.component.Cell;
import com.infragistics.faces.grid.component.RowItem;
import com.infragistics.faces.grid.component.html.HtmlColumn;
import com.infragistics.faces.grid.component.html.HtmlGridView;
import com.infragistics.faces.input.component.DropDownList;
import com.infragistics.faces.input.component.html.HtmlCheckBox;
import com.infragistics.faces.input.component.html.HtmlDateChooser;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
import com.infragistics.faces.shared.component.DataRepeater;
import com.infragistics.faces.shared.component.html.HtmlLink;
import com.infragistics.faces.shared.smartrefresh.SmartRefreshManager;
import com.infragistics.faces.window.component.html.HtmlDialogWindow;
import com.infragistics.faces.window.component.html.HtmlDialogWindowHeader;

public class RevisionArqueoDAO {
	
	ClsParametroCaja cajaparm = new ClsParametroCaja();
	
	private String sObjRetencion = "";
	private String sSubRetencion = "";
	private String sObjIVAcomision = "";
	private String sSubIVAcomision = "";
	/** Pantalla Principal **/
	private HtmlGridView gvArqueosPendRev;
	
	private List<SelectItem>lstFiltroCompania;
	
	private List lstArqueosPendRev,lstFiltroMoneda,lstFiltroEstado, lstFiltroCaja;
	private DropDownList ddlFiltroCompania,ddlFiltroMoneda,ddlFiltroEstado,ddlFiltroCaja;
	private HtmlOutputText lblMensaje;	
	private String msgArqueos;
	
	/** Ventana para el detalle del arqueo. ****/
	private HtmlDialogWindow dwDetalleArqueo,dwDetalleOtrosIngresos;
	private HtmlOutputText lblnocaja,lblsucursal,lblcajero,lblCompania,lblMoneda,lblnoarqueo;
	private HtmlDialogWindowHeader hdDetArqueo;
	private HtmlOutputText lblTotalEgrRecxmonex,lblTotalIngRecxmonex;
	
	private HtmlOutputText lblVentasTotales,lblTotalDevoluciones,lblTotalVentasNetas,lblTotalAbonos,lblTotalPrimas,lblTotalIngex;
	private HtmlOutputText lblTotalOtrosIngresos,lblTotaIngresos,lblTotalVtsCredito,lblVtsTCredito,lblVtsDDbanco;
	private HtmlOutputText lblVtsTransBanc,lblTotalVtsPagBanco,lblAbonoTCredito,lblAbonoDDbanco,lblAbonoTransBanc;
	private HtmlOutputText lblTotalAbonoPagBanco,lblPrimasTCredito,lblPrimasDDbanco,lblPrimasTransBanc,lblTotalPrimasPagBanco;
	private HtmlOutputText lblOEcambios,lblOEsalidas,lblTotalOtrosEgresos,lblTotalEgresos,lblTotalIexPagBanco;
	
	private HtmlOutputText lblIngresosExtraOrd,lblCambioOtraMoneda,lblTotalFinan,lblTotalFinanPagBanco;
	
	private HtmlOutputText lblCDC_efectnetoRec,lblbCDC_efectivoenCaja,lblCDC_montominimo,lblet_SobranteFaltante;
	private HtmlOutputText lblCDC_SobranteFaltante,lblCDC_depositoSug,lblCDC_depositoFinal,lblACDetfp_Efectivo, lblCDC_montoMinReint, lblCDC_montoMinAjust;
	private HtmlOutputText lblACDetfp_Cheques,lblACDetfp_TarCred,lblACDetfp_DepDbanco,lblACDetfp_TransBanco,lblACDetfp_Total;
	private HtmlOutputText lblaprobDepSugerido,lblCDC_pagochks;
	private HtmlInputText txtaprobDepFinal,txtCDC_ReferDeposito;	
		
	/** Ventana las mostrar la lista de facturas ****/	
	private HtmlDialogWindow  rv_dwFacturas;
	private HtmlDialogWindowHeader hdFactura;
	private HtmlGridView rv_gvFacturaRegistradas;
	private List rv_lstFacturasRegistradas;
	private String rv_lblEtCantFacC0, rv_lblCantFacCO, rv_lblEtCantFacCr, rv_lblCantFacCr;
	private String rv_lblEtTotalFacCo,rv_lblTotalFacCo,rv_blEtTotalFacCr, rv_lblTotalFacCr;
	
	/** Ventana para mostrar los recibos por tipo y metodo de pago *******/
	private HtmlDialogWindow rv_dwReciboxtipoMetPago;
	private HtmlDialogWindowHeader hdDetTrecxMetPago;
	private HtmlGridView rv_gvRecibosxTipoMetodopago;
	private List rv_lstRecxTipoMetpago;
	
	/** ventana para mostrar los recibos de pago a facturas **************/
	private HtmlDialogWindow  dwRecibosxTipoIngreso;
	private HtmlGridView gvRecibosIngresos;
	private List rv_lstRecibosxIngresos;
	private HtmlDialogWindowHeader hdRecxTipoIng;
	
	/********** Variables del Detalle de Facturas *************/
	private HtmlDialogWindow rv_dwDetalleFactura;	
	private HtmlDropDownList rv_ddlDetalleFacCon;
	private List rv_lstMonedasDetalle,rv_lstCierreCajaDetfactura;
	private HtmlGridView gvDfacturasDiario;
	private String txtFechaFactura, txtNofactura = "", txtCliente = "", txtCodigoCliente = "";
	private String txtCodUnineg = "", txtUnineg = "", txtTotal = "";
	private double txtSubtotal,txtIva;
	private HtmlOutputText lblTasaDetalle,txtTasaDetalle;
	
	/** variables del detalle de recibo **********************/
	private HtmlDialogWindow rv_dwDetalleRecibo;
	private HtmlGridView rv_gvDetalleRecibo,rv_gvFacturasRecibo;
	private List rv_lstDetalleRecibo,rv_lstFacturasRecibo;
	private HtmlOutputText txtHoraRecibo, txtNoRecibo, txtDRCodCli,	txtNoBatch,	txtDRNomCliente;	
	private HtmlOutputText txtMontoAplicar,txtMontoRecibido,txtDetalleCambio;
	private HtmlInputTextarea txtConcepto;
	
	/** Variables de ventanas de validación de aprobación *******************/
	private HtmlDialogWindow dwValidarAprobacionArqueo,rv_dwCancelarAprArqueo;
	private HtmlDialogWindow rv_dwConfirmarProcesarArq,rv_dwRechazarArqueoCaja;
	private HtmlOutputText lblValidarAprobacion,lblMsgMotivoRechazo;
	private HtmlInputTextarea txtMotivoRechazoArqueo;
	private HtmlProgressBar barraProgresoPrueba;
	
	/** Variables para mostrar los recibos pagados con moneda distinta del arqueo **/
	private HtmlDialogWindow dwDetallerecpagmonEx;
	private HtmlDialogWindowHeader hdDetrecpagmonEx;
	private HtmlGridView gvDetRecpagMonEx;
	private List rv_lstDetRecpagMonEx;
	
	/** variables para mostrar la ventana con total de egreso pagados en banco por método de pago **/
	private HtmlOutputText lblPagoTarjetaCredito,lblPagoDepBanco,lblPagoTransBanco;	
	private HtmlDialogWindow dwEgresosxMetPago;
	private HtmlDialogWindowHeader hdEgrxmetp;	
	private HtmlDialogWindow dwDetOtrosEgresos;
	
	/** variables para mostrar el detalle de los cambios realizados. **/
	private HtmlDialogWindow dwDetalleCambios;
	private HtmlGridView gvDetalleCambios;
	private List lstDetalleCambios;
	
	/** Objetos de Encabezados de detalle de factura o detalle del bien del recibo. */
	private HtmlOutputText lblNoFactura2,lblTipofactura2,lblUnineg2,lblMoneda2,lblFecha23,lblPartida23;	
	private HtmlColumn coNoFactura2;
	
	//-------- Ventana de detalle de Salida, ventana de agregar referencias a pos
	private HtmlDialogWindow dwDetalleSalidas,dwCargarReferenciasPOS,rv_dwConfirmarReimpresionRpt;
	private HtmlGridView gvDetalleSalidas,gvReferenciaPos;
	private List lstDetalleSalidas,lstReferenciapos;
	private HtmlOutputText lblMsgValidaReferencia;
	private HtmlDateChooser dcFechaArqueo;
	private Date dtFechaArqueo;
	
	private HtmlDateChooser dcFechaArqueoFin;
	private Date dtFechaArqueoFin;
	
	
	//------- Editar el valor para la referencia del pago.
	private HtmlColumn coDtmpagoRefer1,coDtmpagoRefer2,coDtmpagoRefer3;
	private HtmlOutputText lblMsgErrorCambioRefer;
	private HtmlGridView rv_gvEditarRecibosIdPos;
	private HtmlDialogWindow rv_dwEditarRecibosIdPos;
	private List rv_lstEditarRecibosIdPos;
	private HtmlOutputText lblACDetfp_TCmanual, lblACDetfp_TCsocketpos;
	private HtmlDialogWindow dwCargando;
	private HtmlDialogWindow rv_dwAsignarReferCheque;
	private HtmlGridView rv_gvAsignarReferenciaCheque;
	private List rv_lstAsignarReferenciaCheque;
	private HtmlOutputText lblMsgErrorAsignarReferChk;
	private HtmlLink lnkLiquidacionChk,lnkAnularArqueoCaja;
	
	private HtmlDialogWindow dwRecibosxMpago8N;
	private HtmlDialogWindowHeader hdReciboMPago8N;
	private HtmlGridView gvRecibosxMpago8N;
	private List<Vrecibosxtipompago> lstRecibosxMpago8N;
	private HtmlOutputText lblMsgCambioReferMPago;
	
	private HtmlDialogWindow dwUpdParamBloqueo;
	private HtmlInputTextarea txtDescripcionCambio;
	private HtmlInputText txtdiasblck; 
	private HtmlCheckBox chkbloqueocaja;
	private HtmlLink lnkParametrosBloqueo;
	private String strParametrosBloqueo;
	private HtmlOutputText lblMsgValidaUpdParm;
	private HtmlInputTextarea txtObservacionesArqueo;
	
	private HtmlDialogWindow dwReimprimirDocsxCierre;
	
	//&& ==================== Donaciones 
	private HtmlGridView gvDonacionesFormaPago ;
	private List<DncDonacion> lstDonacionesFormaPago ;
	private HtmlGridView gvDetalleDonacionesMpago; 
	private List<DncDonacion> lstDetalleDonacionesMpago;
	private HtmlDialogWindow dwDetalleDonacionesMpago;
	
	private HtmlDialogWindow dwSeleccionTipoTarjeta ;
	private HtmlGridView gvActualizaCodigoAfiliado;
	private List<Vrecibosxtipompago> lstGvActualizaCodigoAfiliado;
	private List<SelectItem> lstMarcasTarjetaDisponibles ;
	private HtmlDropDownList ddlTipoMarcasTarjetaDisponibles;
	private HtmlOutputText lblMsgValidaCambioAfiliado;
	
	//&& ====================  seleccion de bancos para cambio de cuenta 
	private HtmlDialogWindow  dwSeleccionarBancoParaCambio ;
	private HtmlGridView gvBancosDisponiblesCambio;
	private List< Vwcuentascontablesporbanco> lstBancosDisponiblesCambio ;
	
	private HtmlOutputText lblCambioBancoRecibo;
	private HtmlOutputText lblCambioBancoNoTransferencia;
	private HtmlOutputText lblCambioBancoNombreBanco;
	private HtmlOutputText lblCambioBancoCliente;
	private HtmlOutputText lblCambioBancoMonto;
	
	//&& =================== Adaptacion para PMT
	private HtmlOutputText lblTotalRecibosPMT ;
	private HtmlOutputText lblTotalAnticiposPMTPagBanco;
	
	private HtmlJspPanel pnlDatosFacturas ;
	private HtmlJspPanel pnlDatosAnticiposPMT ;
	
	private List<HistoricoReservasProformas> detalleContratoPmt ;
	private HtmlGridView gvDetalleContratoPmt;
	
	
	//Valores reimplentacion JDE
	Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();	
	String[] valoresJdeNumeracion = (String[]) m.get("valoresJDENumeracionIns");
	String[] valoresJDEInsCredito = (String[]) m.get("valoresJDEInsCredito");
	String[] valoresJdeInsContado = (String[]) m.get("valoresJDEInsContado");
	
	Exception errorApr = new Exception();
	public Exception getErrorApr() {
		return errorApr;
	}
	public void setErrorApr(Exception errorApr) {
		this.errorApr = errorApr;
	}
	public void cerrarDwRecibosxMpago8N(ActionEvent ev){
		dwRecibosxMpago8N.setWindowState("hidden");
	}
	
	public void cerrarDlgDetalleDonacion(ActionEvent ev){
		dwDetalleDonacionesMpago.setWindowState("hidden");
	}

	public void cerrarSeleccionBanco(ActionEvent ev){ 
		dwRecibosxMpago8N.setWindowState("normal");
		dwSeleccionarBancoParaCambio.setWindowState("hidden");
	}
	
	
	@SuppressWarnings("unchecked")
	public void seleccionarNuevoBancoParaRecibo(ActionEvent ev){
		String strMensaje="";
		
		try {
			
			RowItem ri = (RowItem) ev.getComponent().getParent().getParent();
			Vwcuentascontablesporbanco bancoSeleccionado = (Vwcuentascontablesporbanco) DataRepeater.getDataRow(ri);
			
			final Vrecibosxtipompago rc  = (Vrecibosxtipompago)CodeUtil.getFromSessionMap( "rva_reciboCambiarBanco");
			
			String refer2  = rc.getId().getRefer2().trim();
			String refer3  = rc.getId().getRefer3().trim();
			
			if(rc.getId().getMpago().compareTo(MetodosPagoCtrl.TRANSFERENCIA) == 0){
				refer2 = rc.getId().getRefer3().trim();
				refer3 = rc.getId().getRefer2().trim();
			}
			
			String sql = "update @BDCAJA.recibodet set refer1 = '@NUEVOBANCO', depctatran = @PRECONCILIA " +
					" WHERE caid = @CAID and trim(codcomp) = '@CODCOMP' and tiporec = '@TIPOREC' " +
					"and numrec = @NUMREC and mpago = '@MPAGO' and trim(refer1) = '@REFER1' " +
					"and trim(REFER2) = '@REFER2' and trim(REFER3) = '@REFER3'  and monto = @MONTOPAGO ";
			
			sql = sql.replace("@BDCAJA", PropertiesSystem.ESQUEMA)
					.replace("@NUEVOBANCO", String.valueOf( bancoSeleccionado.getCodb() ) )
					//.replace("@PRECONCILIA",  bancoSeleccionado.isConciliar()?  "1" : "0" )
					.replace("@PRECONCILIA",   String.valueOf( bancoSeleccionado.getConciliarxcompania() ) )
					.replace("@CAID", String.valueOf( rc.getId().getCaid() ) )
					.replace("@CODCOMP", rc.getId().getCodcomp().trim() )
					.replace("@TIPOREC", rc.getId().getTiporec() )
					.replace("@NUMREC", String.valueOf( rc.getId().getNumrec() ) )
					.replace("@MPAGO",  rc.getId().getMpago() )
					.replace("@REFER1", rc.getId().getRefer1().trim() )
					.replace("@REFER2", refer2 )
					.replace("@REFER3", refer3 )
					.replace("@MONTOPAGO", rc.getId().getMonto().toString() );
					
			boolean update = ConsolidadoDepositosBcoCtrl.executeSqlQueryTx(null, sql);
			
			if(!update){
				strMensaje = ConsolidadoDepositosBcoCtrl.getMsgStatus();
				return;
			}
			
			rc.getId().setRefer1( String.valueOf( bancoSeleccionado.getCodb() ) );
			rc.getId().setNombrebanco( bancoSeleccionado.getBanco().trim().toLowerCase() ) ;
			
			List<Vrecibosxtipompago> lstRecibosMago8N = (List<Vrecibosxtipompago>) CodeUtil.getFromSessionMap("rv_lstRecibosxMpago8N" ) ;
			
			lstRecibosMago8N = new RevisionArqueoCtrl().obtenerRecibosMpago8N(lstRecibosMago8N,
					rc.getId().getMpago(), rc.getId().getCaid(), rc.getId().getCodcomp(), rc.getId().getCodsuc(), 
					rc.getId().getMoneda());
			
			if(lstRecibosMago8N == null ){
				
				Vrecibosxtipompago vOriginal = (Vrecibosxtipompago)
					CollectionUtils.find(lstRecibosMago8N, new Predicate(){
						@Override
						public boolean evaluate(Object  o) {
							  
							Vrecibosxtipompago v = (Vrecibosxtipompago)o;
							return
							v.getId().getCaid()  == rc.getId().getCaid() && 
							v.getId().getCodcomp().trim().compareTo(rc.getId().getCodcomp().trim()) == 0 &&  
							v.getId().getNumrec() == rc.getId().getNumrec() &&
							v.getId().getTiporec().compareTo( rc.getId().getTiporec() ) == 0 &&   
							v.getId().getMpago().compareTo( rc.getId().getMpago() ) == 0 &&   
							v.getId().getRefer1().trim().compareTo( rc.getId().getRefer1().trim() ) == 0 &&   
							v.getId().getRefer2().trim().compareTo( rc.getId().getRefer2().trim() ) == 0 &&
							v.getId().getRefer3().trim().compareTo( rc.getId().getRefer3().trim() ) == 0 &&
							v.getId().getMonto().compareTo( rc.getId().getMonto() ) == 0;
						}
					});
				
				vOriginal.getId().setRefer1( String.valueOf( bancoSeleccionado.getCodb() ) );
				vOriginal.getId().setNombrebanco( bancoSeleccionado.getBanco().trim().toLowerCase() ) ;
				
			}
			
			CodeUtil.putInSessionMap("rv_lstRecibosxMpago8N", lstRecibosMago8N ) ;
			gvRecibosxMpago8N.dataBind();
			
			DetalleArqueoCaja dac = (DetalleArqueoCaja)CodeUtil.getFromSessionMap( "rv_DAC");
			
			if(rc.getId().getMpago().compareTo(MetodosPagoCtrl.TRANSFERENCIA) == 0){
				dac.setVrtransbanco(lstRecibosMago8N);
			}
			if(rc.getId().getMpago().compareTo(MetodosPagoCtrl.DEPOSITO) == 0){
				dac.setVrdepbanco(lstRecibosMago8N);
			}
			
			 
			CodeUtil.putInSessionMap( "rv_DAC", dac);
			 
			
		} catch (Exception e) {
			strMensaje = "No se ha podido actualizar el registro";
			e.printStackTrace(); 
		}finally{
			
			if(!strMensaje.isEmpty()){
				CodeUtil.refreshIgObjects(new Object[]{dwValidarAprobacionArqueo});
				dwValidarAprobacionArqueo.setWindowState("normal");
				lblValidarAprobacion.setValue(strMensaje);
				return;
			}
			
			dwRecibosxMpago8N.setWindowState("normal");
			dwSeleccionarBancoParaCambio.setWindowState("hidden");
			
			CodeUtil.refreshIgObjects(new Object[]{dwSeleccionarBancoParaCambio, dwRecibosxMpago8N, gvRecibosxMpago8N });
			
		}
	}
	
	@SuppressWarnings("unchecked")
	public void mostrarAsignarBancosDisponibles(ActionEvent ev){
		String strMsgValida = "";
		Vrecibosxtipompago reciboCambiarBanco = null;
		
		try {
			
			RowItem ri = (RowItem) ev.getComponent().getParent().getParent();
			reciboCambiarBanco = (Vrecibosxtipompago) DataRepeater.getDataRow(ri);
			
			Varqueo va = (Varqueo)CodeUtil.getFromSessionMap( "rva_VARQUEO");
			if(va.getId().getEstado().compareTo("P") != 0 ){
				strMsgValida = "El estado debe estar pendiente de aprobación";
				return;
			}
			
			String sql = "select * from @BDCAJA.Vwcuentascontablesporbanco v " +
					"where trim(v.d3rp01) = '@CODCOMP' and trim(v.d3crcd) = '@MONEDA'" +
					" and trim(v.codb) <> '@BANCOACTUAL'  ";
			
			sql = sql.replace("@BDCAJA", PropertiesSystem.ESQUEMA)
				 .replace("@CODCOMP", reciboCambiarBanco.getId().getCodcomp().trim() )
				 .replace("@MONEDA", reciboCambiarBanco.getId().getMoneda().trim() )
			 	 .replace("@BANCOACTUAL", reciboCambiarBanco.getId().getRefer1().trim() );
			
			lstBancosDisponiblesCambio = (ArrayList<Vwcuentascontablesporbanco>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, true, Vwcuentascontablesporbanco.class);
			
			if(lstBancosDisponiblesCambio == null || lstBancosDisponiblesCambio.isEmpty()){
				lstBancosDisponiblesCambio = new ArrayList<Vwcuentascontablesporbanco>();
				strMsgValida = "No se han encontrado bancos disponibles para Compania y moneda del arqueo";
				return;
			}
			
			lblCambioBancoRecibo.setValue(reciboCambiarBanco.getId().getNumrec());
			
			String refer = (reciboCambiarBanco.getId().getMpago().compareTo(MetodosPagoCtrl.TRANSFERENCIA) == 0 ) ? 
					reciboCambiarBanco.getId().getRefer3().trim() : 
					reciboCambiarBanco.getId().getRefer2().trim() ;
			lblCambioBancoNoTransferencia.setValue( refer );
			
			refer = reciboCambiarBanco.getId().getRefer1().trim() + " " + reciboCambiarBanco.getId().getNombrebanco() + " " + reciboCambiarBanco.getId().getCuentacontable() ;
			lblCambioBancoNombreBanco.setValue( refer );
		
			refer = reciboCambiarBanco.getId().getCodigocliente() + " " + CodeUtil.capitalize( reciboCambiarBanco.getId().getCliente().trim() ) ;
 			lblCambioBancoCliente.setValue(refer);
 			
 			refer = String.format("%1$,.2f", reciboCambiarBanco.getId().getMonto() ) + " " + reciboCambiarBanco.getId().getMoneda();
			lblCambioBancoMonto.setValue(refer);
			
			
		} catch (Exception e) {
			strMsgValida = "No se han podido obtener los bancos disponibles.";
			e.printStackTrace(); 
		}finally{
			
			CodeUtil.putInSessionMap("rva_lstBancosDisponiblesCambio", lstBancosDisponiblesCambio);
			
			if(strMsgValida.isEmpty()){
				
				dwRecibosxMpago8N.setWindowState("hidden");
				dwSeleccionarBancoParaCambio.setWindowState("normal");
				gvBancosDisponiblesCambio.dataBind();
				gvBancosDisponiblesCambio.setPageIndex(0);
				
				CodeUtil.putInSessionMap("rva_reciboCambiarBanco", reciboCambiarBanco);
				CodeUtil.refreshIgObjects(new Object[]{dwSeleccionarBancoParaCambio, dwRecibosxMpago8N, gvBancosDisponiblesCambio});
				
			}else{
				
				dwValidarAprobacionArqueo.setWindowState("normal");
				lblValidarAprobacion.setValue(strMsgValida);
				
				CodeUtil.refreshIgObjects(dwValidarAprobacionArqueo);
				
			}
		}
	}
	

	public void actualizarDatosPagoAfiliado(ActionEvent ev){
		String strMsgValida = "";
		String strNewStyleClass = ""; 
		
		try {
			
			Varqueo va = (Varqueo)CodeUtil.getFromSessionMap( "rva_VARQUEO");
			if(va.getId().getEstado().compareTo("P") != 0 ){
				strMsgValida = "El estado debe estar pendiente de aprobación";
				return;
			}
			
			//&& ============= Validar que el codigo del pos ingresado exista en la configuracion del sistema para la caja y compania.
			final Vrecibosxtipompago dncRsm = (Vrecibosxtipompago) CodeUtil.getFromSessionMap("rva_DetallePagoActualizar") ;
			
			String strQuery = " select * from @BDMCAJA.cafiliados where trim(cxcafi) = '@CXCAFI' and c6id = @CAID and trim(c6rp01) = '@C6RP01' and d7crcd  ='@MONEDA'";
			
			strQuery = strQuery
						.replace("@BDMCAJA", PropertiesSystem.ESQUEMA)
						.replace("@MONEDA", dncRsm.getId().getMoneda()  )
						.replace("@CXCAFI", dncRsm.getId().getRefer1().trim() )
						.replace("@CAID",String.valueOf(dncRsm.getId().getCaid()) )
						.replace("@C6RP01", dncRsm.getId().getCodcomp().trim() );
			
			@SuppressWarnings("unchecked")
			List<Cafiliados> afiliadoscf = (ArrayList<Cafiliados>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(strQuery, true, Cafiliados.class);
			if( afiliadoscf == null || afiliadoscf.isEmpty() ){
				strMsgValida = "El codigo de afiliado no es valido para caja/Compania";
				return;
			}
				
			String codigomarcatc = ddlTipoMarcasTarjetaDisponibles.getValue().toString();
			
			String strQueryPosClasificaMarcas = "( select cxclasificamarca from "
					+PropertiesSystem.ESQUEMA +".f55ca03 " +
					"where trim(cxcafi) = '@NUEVO_CODIGO_POS' fetch first rows only )" ;
			
			strQuery = "update @BDMCAJA.Recibodet set refer1 = '@NUEVO_CODIGO_POS', " +
						"codigomarcatarjeta = '@NUEVO_CODIGO_MARCATC', marcatarjeta = '@MARCA_TARJETA', " +
						"liquidarpormarca = @LIQUIDARPORMARCA " +
						"where caid = @CAID and trim(codcomp) = '@CODCOMP' and numrec = @NUMREC and tiporec = '@TIPOREC' " +
						"and trim(refer2) = '@REFER2' and trim(refer3) = '@REFER3' and moneda = '@MONEDA' and mpago = '@MPAGO' " +
						"and monto = @MONTOPAGO " ;
		
			strQuery =  strQuery
				.replace("@BDMCAJA", PropertiesSystem.ESQUEMA) 
				.replace("@NUEVO_CODIGO_MARCATC", codigomarcatc.split("@")[0] )
				.replace("@MARCA_TARJETA", codigomarcatc.split("@")[1].toLowerCase() )
				.replace("@LIQUIDARPORMARCA", strQueryPosClasificaMarcas )
				.replace("@CAID", String.valueOf( dncRsm.getId().getCaid() ) )
				.replace("@CODCOMP",dncRsm.getId().getCodcomp().trim() )
				.replace("@NUMREC", String.valueOf( dncRsm.getId().getNumrec() ) )
 				.replace("@TIPOREC",dncRsm.getId().getTiporec() ) 
				.replace("@REFER2", dncRsm.getId().getRefer2().trim())
				.replace("@REFER3", dncRsm.getId().getRefer3().trim())
				.replace("@MONEDA", dncRsm.getId().getMoneda() )
				.replace("@MPAGO",  dncRsm.getId().getMpago() )
				.replace("@MONTOPAGO", dncRsm.getId().getMonto().toString() );
			
			strQuery =  strQuery
					.replace("@NUEVO_CODIGO_POS", dncRsm.getId().getRefer1().trim() ) ;
			
			
			boolean aplicado = ConsolidadoDepositosBcoCtrl.executeQueryUpdate(strQuery, true);
			if( !aplicado ){
				strMsgValida = "Actualización de registro no completada";
				return;
			}
			
			
			//&& ===== actualizar los datos del registro modificado
			
			@SuppressWarnings("unchecked")
			List<Vrecibosxtipompago> lstRecsPagosTc = (ArrayList<Vrecibosxtipompago>)CodeUtil.getFromSessionMap("rva_rv_lstEditarRecibosIdPos");
			
			
			Vrecibosxtipompago vrcActualiar = (Vrecibosxtipompago)
				CollectionUtils.find(lstRecsPagosTc, new Predicate(){
	
					@Override
					public boolean evaluate(Object o) {
						 
						Vrecibosxtipompago vrcEvaluar = (Vrecibosxtipompago)o;
						
						return
						dncRsm.getId().getNumrec() == vrcEvaluar.getId().getNumrec() &&
						dncRsm.getId().getTiporec().compareTo(vrcEvaluar.getId().getTiporec()) == 0 &&
						dncRsm.getId().getRefer1().trim().compareTo(vrcEvaluar.getId().getRefer1().trim()) == 0 &&
						dncRsm.getId().getRefer2().trim().compareTo(vrcEvaluar.getId().getRefer2().trim()) == 0 &&
						dncRsm.getId().getRefer3().trim().compareTo(vrcEvaluar.getId().getRefer3().trim()) == 0 &&
						dncRsm.getId().getMonto().compareTo(vrcEvaluar.getId().getMonto()) == 0 &&
						dncRsm.getId().getEquiv().compareTo(vrcEvaluar.getId().getEquiv()) == 0  ;
						 
					}
				}); 
			
			vrcActualiar.getId().setCodigomarcatarjeta(codigomarcatc.split("@")[0]);
			vrcActualiar.getId().setMarcatarjeta(codigomarcatc.split("@")[1].toLowerCase());
			
			CodeUtil.putInSessionMap("rva_rv_lstEditarRecibosIdPos", lstRecsPagosTc) ;
			
			rv_gvEditarRecibosIdPos.dataBind();
			rv_gvEditarRecibosIdPos.setPageIndex(0);
			rv_dwEditarRecibosIdPos.setWindowState("normal");
			
		} catch (Exception e) {
			strMsgValida = "El registro no puede ser actualizado desde sistema ";
			e.printStackTrace(); 
		}finally{
			
			strNewStyleClass = strMsgValida.isEmpty() ? "frmLabel2Success" : "frmLabel2Error" ;
			strMsgValida = strMsgValida.isEmpty() ? "Registro actualizado correctamente " : strMsgValida;
			
			lblMsgErrorCambioRefer.setValue(strMsgValida);
			lblMsgErrorCambioRefer.setStyleClass(strNewStyleClass);
			
			rv_gvEditarRecibosIdPos.dataBind();
			dwSeleccionTipoTarjeta.setWindowState("hidden");
		}
	}
	public void cerrarSeleccionTipoTarjeta(ActionEvent ev){
		dwSeleccionTipoTarjeta.setWindowState("hidden");
	}
 
	public void mostrarCambiarCodigoAfiliado(ActionEvent ev) {
		try {
			
			
			//&& ============== Validar que sea un contador o administrador
			Vautoriz vaut = ((Vautoriz[])CodeUtil.getFromSessionMap( "sevAut"))[0];
			boolean autorizado = 
					vaut.getId().getCodper().compareTo("P000000004") == 0 ||
					vaut.getId().getCodper().compareTo("P000000015") == 0 ||
					vaut.getId().getCodper().compareTo("P000000025") == 0 ;

			if(!autorizado){
				lblMsgErrorCambioRefer.setStyleClass("frmLabel2Error");
				lblMsgErrorCambioRefer.setValue("No tiene autorización para realizar esta operación");
				CodeUtil.refreshIgObjects(lblMsgErrorCambioRefer) ;
				return;
			}
			
			
			RowItem ri = (RowItem) ev.getComponent().getParent().getParent();
			Vrecibosxtipompago dncRsm = (Vrecibosxtipompago) DataRepeater.getDataRow(ri);
			
			CodeUtil.putInSessionMap("rva_DetallePagoActualizar", dncRsm) ;
			
			dwSeleccionTipoTarjeta.setWindowState("normal");
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public void mostrarDetalleDonacionesMpago(ActionEvent ev){
		
		try {
			
			lstDetalleDonacionesMpago = new ArrayList<DncDonacion>(); 
			
			if(CodeUtil.getFromSessionMap("rva_lstDonacionesCierre") == null){
				return;
			}
			
			List<DncDonacion>lstDonacionesArqueo = (ArrayList<DncDonacion>)CodeUtil.getFromSessionMap("rva_lstDonacionesCierre");
			
			RowItem ri = (RowItem) ev.getComponent().getParent().getParent();
			final DncDonacion dncRsm = (DncDonacion) DataRepeater.getDataRow(ri);
			
			lstDetalleDonacionesMpago = (ArrayList<DncDonacion>) CollectionUtils
					.select(lstDonacionesArqueo, new Predicate() {
						public boolean evaluate(Object o) {
							return ((DncDonacion) o).getFormadepago()
									.compareTo(dncRsm.getFormadepago()) == 0;
						}
					});
			
			CodeUtil.putInSessionMap("rva_lstDetalleDonacionesMpago", lstDetalleDonacionesMpago);
			gvDetalleDonacionesMpago.dataBind();
			CodeUtil.refreshIgObjects(gvDetalleDonacionesMpago);
			
			
		} catch (Exception e) {
			e.printStackTrace(); 
		} 
		
		dwDetalleDonacionesMpago.setWindowState("normal");
	}
	
	
	public void cerrarDescargaDocxCierre(ActionEvent ev){
		try {
			
			Varqueo va = (Varqueo)CodeUtil.getFromSessionMap( "rva_ArqueoParaDocumentos");
			
			HttpServletRequest sHttpRqst  = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
			@SuppressWarnings("deprecation")
			String sRutaCarpeta = sHttpRqst.getRealPath(File.separatorChar+"Confirmacion");
			File directorio = new File(sRutaCarpeta);
		 
			String [] sListaArchivos = directorio.list();
			String prefixfiletodelete = va.getId().getCaid() +""+ va.getId().getNoarqueo() +"_";
			
			for (String sArchivo : sListaArchivos) {
				
				if( !sArchivo.startsWith( prefixfiletodelete ) ) {
					continue;
				}
				File archivo = new File(sRutaCarpeta + File.separatorChar + sArchivo);
				archivo.delete();
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			dwReimprimirDocsxCierre.setWindowState("hidden");
			CodeUtil.removeFromSessionMap( "rva_ArqueoParaDocumentos");
		}
	}
	
	@SuppressWarnings("unchecked")
	public void mostrarReimprimirDocsxCierre(ActionEvent ev){
		try {
			RowItem ri = (RowItem)ev.getComponent().getParent().getParent();
			Varqueo ar = (Varqueo)com.infragistics.faces.shared.component.DataRepeater.getDataRow(ri);
			CodeUtil.putInSessionMap("rva_ArqueoParaDocumentos", ar);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			dwReimprimirDocsxCierre.setWindowState("normal");
		}
	}
	/******************************************************************************************/
	@SuppressWarnings("unchecked")
	public void actualizarParmBlck(ActionEvent ev){
		try {
			txtDescripcionCambio.setStyle("width:100%;");
			
			String sDescripcion = txtDescripcionCambio.getValue().toString();
			String sDias = txtdiasblck.getValue().toString();
			boolean bDesbl = chkbloqueocaja.isChecked();
			
			if( sDescripcion.trim().compareTo("") == 0 ){
				txtDescripcionCambio.setStyle("width:100%; border:1px solid red");
				lblMsgValidaUpdParm.setStyle("color: red");
				lblMsgValidaUpdParm.setValue("Descripción obligatoria");
				return;
			}
			
			//&& ======= Actualizaci[on del registro de caja asociado.
			Vf55ca01 f5 = (Vf55ca01)((List<Vf55ca01>)CodeUtil.getFromSessionMap( "lstCajas")).get(0);
			Session sesion = HibernateUtilPruebaCn.currentSession();
			Transaction trans = sesion.beginTransaction();
			
			String sUpdate = " update "+PropertiesSystem.CONTEXT_NAME+".F55ca01 " +
							 "set cadiablk =:DIAS, castdblk = :ESTADO " +
							 "where caid =:CAJA ";
			
			Query q = sesion.createSQLQuery(sUpdate);
			q.setParameter("DIAS", Integer.parseInt(sDias));
			q.setParameter("ESTADO", (bDesbl)?1:0);
			q.setParameter("CAJA", f5.getId().getCaid());
			
			int iResult = q.executeUpdate();
			if(iResult > 0){
				lblMsgValidaUpdParm.setStyle("color: green");
				lblMsgValidaUpdParm.setValue("Registro actualizado correctamente");
			}
			trans.commit();
			sesion.close();
			
			//&& =============== enviar correo de notificacion con el motivo puesto en campo.
			Divisas dv = new Divisas();
			String sNota="";
			Vautoriz[] vAut = (Vautoriz[])CodeUtil.getFromSessionMap( "sevAut");
			Vf0101 vf01 =  EmpleadoCtrl.buscarEmpleadoxCodigo2(vAut[0].getId().getCodreg());
			
			//&& ======== Encabezado del correo.
			sNota = "<strong>Estimado(a):</strong><em> "+ 
					CodeUtil.capitalize( f5.getId().getCacontnom().trim())+"</em>:";
			sNota += "<br>";
			sNota += "<br>";
			
			sNota += "Los parámetros de bloqueo para a caja # "+f5.getId().getCaid();
			sNota += " ("+CodeUtil.capitalize( f5.getId().getCaname().trim())+") ";
			sNota += "han sido moficiados por <em>"
					+ CodeUtil.capitalize( vf01.getId().getAbalph().trim().toLowerCase())+"</em>";
			sNota += "<br><br>";
			sNota += "Los valores asignados corresponden:  ";
			sNota += "<br>";
			sNota += "<strong>Estado actual de la caja:</strong> "+
							((bDesbl)?"Bloqueo Activo":"Sin bloqueos");
			sNota += "<br><strong>Días permitidos para aprobación:</strong> "+sDias+" hábiles";
			
			sNota += "<br><br>";
			sNota += "Estos valores se establecieron bajo el concepto de: ";
			sNota += "<br><em>\""+sDescripcion.toLowerCase()+"\"</em>";
			
			//&& === Pie del correo
			sNota += "<br><br>";
			sNota += "<strong>Saludos.</strong>";
			sNota += "<br><em>Sistema de Pagos en Caja.</em>";
			sNota += "<br><em>Casa Pellas SA.</em>";
			
			String sSubject ="Actualización de parámetros para bloqueo de caja "+
					CodeUtil.capitalize( f5.getId().getCaname() );
			
			//&& =========================================== Construir el correo.
			String sComodin = "=>";
			String sCorreos = f5.getId().getCaautimail().trim() + sComodin +
							  f5.getId().getCaan8mail().trim()  + sComodin +
							  f5.getId().getCacontmail().trim() + sComodin +
							  vf01.getId().getWwrem1().trim() ;
			
			String sNombres = f5.getId().getCaautinom().trim() + sComodin +
							  f5.getId().getCaan8nom().trim()  + sComodin +
							  f5.getId().getCacontnom().trim() + sComodin +
							  vf01.getId().getAbalph().trim() ;
			
			String[] lstCorreos = sCorreos.split(sComodin);
			String[] lstNombres = sNombres.split(sComodin);
			 
			List<CustomEmailAddress> toList = new ArrayList<CustomEmailAddress>();
			for (int i = 0; i < lstCorreos.length; i++){
				
				if( !lstCorreos[i].trim().toUpperCase().matches(PropertiesSystem.REGEXP_EMAIL_ADDRESS)  )
					continue;

				toList.add(new CustomEmailAddress(lstCorreos[i].toLowerCase(), CodeUtil.capitalize( lstNombres[i] )));
			}
			
			MailHelper.SendHtmlEmail(
					new CustomEmailAddress(PropertiesSystem.WEBMASTER_EMAIL_ADRESS, "Módulo de Caja: Notificación de Traslado de Factura"),
					toList, null, new CustomEmailAddress(PropertiesSystem.MAIL_BOUNCEADDRESS), 
					sSubject, sNota.toString());
			 
			
		} catch (Exception e) {
			lblMsgValidaUpdParm.setStyle("color: red");
			lblMsgValidaUpdParm.setValue("No se pudo finalizar el proceso.");
			e.printStackTrace(); 
		}
	}
	/******************************************************************************************/	
	public void mostrarActualizarParamBlck(ActionEvent ev){
		try {
			
			Vf55ca01 f55ca01 = (Vf55ca01)((List)CodeUtil.getFromSessionMap( "lstCajas")).get(0);
			
			Session sesion = HibernateUtilPruebaCn.currentSession();
			
			
			String sql = "from F55ca01 f where f.id.caid =:CAJA ";
			
			F55ca01 f5 = (F55ca01)sesion.createQuery(sql)
							.setParameter("CAJA", f55ca01.getId().getCaid())
							.uniqueResult();
			
			sesion.close();
			
			lblMsgValidaUpdParm.setValue("");
			dwUpdParamBloqueo.setWindowState("normal");
			txtDescripcionCambio.setValue("");
			txtDescripcionCambio.setStyle("width:100%;");
			txtdiasblck.setValue(f5.getId().getCadiablk());
			chkbloqueocaja.setChecked(f5.getId().getCastdblk()==1?true:false);
			chkbloqueocaja.setDisabled(f5.getId().getCastdblk() == 0? true:false);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void cerrarActualizaPrmBlck(ActionEvent ev){
		dwUpdParamBloqueo.setWindowState("hidden");
	}
	
/******************************************************************************************/
	public String revertirAjusteMinimo( Session session, Transaction transaction, Reintegro r){
		boolean hecho = true;
		Divisas dv = new Divisas();
		String sMensajeError = "";
		int iNoBatch = 0, iNoDocumento = 0;
		String[] sCuentaCaja = null, sCuentaMinimo = null;
		String sConcepto = "", sTipoCliente;
		String scodsuc2 = "" ;
		
		try{			

			Vautoriz vaut =  ( (Vautoriz[]) CodeUtil.getFromSessionMap( "sevAut") ) [0];
			
			Date dtFecha =  new Date();
			ReciboCtrl rCtrl = new ReciboCtrl();
			 
			sConcepto = "Reversion ca: " + r.getId().getCaid() + " reintegro: " + r.getId().getNoreint();
			String msgLogs = sConcepto;
			String tipoDocumento = CodigosJDE1.BATCH_CONTADO.codigo();
		 
			sTipoCliente = EmpleadoCtrl.determinarTipoCliente( vaut.getId().getCodreg() );
			
			iNoBatch = Divisas.numeroSiguienteJdeE1(   );
			 
			if(iNoBatch == -1){					
				sMensajeError = "No se ha podido obtener el Número de batch para registro de Reintegro de fondo minimo ";
				return sMensajeError;
			}
			
			iNoDocumento =  Divisas.numeroSiguienteJdeE1Custom(valoresJdeNumeracion[8],valoresJdeNumeracion[9] );
			if(iNoDocumento == -1){
				sMensajeError = "No se ha podido obtener el Número de Documento para el registro de reintegro de fondo minimo ";
				return sMensajeError;
			}
			
			sCuentaCaja = dv.obtenerCuentaCaja(r.getId().getCaid(),r.getId().getCodcomp(), MetodosPagoCtrl.EFECTIVO , r.getMoneda(), session, null,null,null);
			if(sCuentaCaja==null){
				sMensajeError="No se ha podido leer la cuenta de caja para el método: Efectivo";				
				return sMensajeError;
			}
 
			sCuentaMinimo = dv.obtenerCuentaFondoMinimo(r.getId().getCaid(), r.getId().getCodcomp(), r.getMoneda());
			if(sCuentaMinimo == null){
				sMensajeError="No se ha podido leer la cuenta de fondo minimo para la moneda: " + r.getMoneda();				
				return sMensajeError;
			}
 
			int iMonto = Divisas.pasarAentero(r.getMonto().doubleValue());
			
			hecho = rCtrl.registrarBatchA92Session(session, dtFecha, valoresJdeInsContado[8],  iNoBatch, iMonto, vaut.getId().getLogin(), 1, "APRARQUEO" , valoresJdeInsContado[9]);
			
			if(!hecho){
				sMensajeError="No se ha podido leer la cuenta de fondo minimo para la moenda: " +  r.getMoneda();				
				return sMensajeError;
			}

			scodsuc2 = String.valueOf( Integer.parseInt(r.getId().getCodusc().trim() ) ) ;
			
			hecho = rCtrl.registrarAsientoDiarioLogsSession(session,  msgLogs, dtFecha,  scodsuc2, tipoDocumento, iNoBatch, 1.0,
						iNoBatch, sCuentaCaja[0], sCuentaCaja[1], 
						sCuentaCaja[3], sCuentaCaja[4], sCuentaCaja[5],
						"AA", r.getMoneda(), iMonto*-1, 
						sConcepto, vaut.getId().getLogin(), vaut.getId().getCodapp(), 
						BigDecimal.ZERO, sTipoCliente,"Ajuste de minimo CTA CA: "+5+" ",
						sCuentaCaja[2], "", "", r.getMoneda(),sCuentaCaja[2],"D", 0);
			
			if(hecho){
				hecho = rCtrl.registrarAsientoDiarioLogsSession(session,  msgLogs, dtFecha, scodsuc2, tipoDocumento, iNoBatch, 2.0,
								iNoBatch, sCuentaMinimo[0],	sCuentaMinimo[1], 
								sCuentaMinimo[3], sCuentaMinimo[4], 
								sCuentaMinimo[5], "AA",  r.getMoneda(), iMonto,
								sConcepto, vaut.getId().getLogin(), vaut.getId().getCodapp(), 
								BigDecimal.ZERO, sTipoCliente,"Ajuste de minimo ",
								sCuentaMinimo[2], "", "", r.getMoneda(),sCuentaMinimo[2],"D", 0);
				if(!hecho){
					sMensajeError="No se pudo registrar la linea 2 del batch!!!";				
					return sMensajeError;
				}
				
			}else{
				sMensajeError="No se pudo registrar la linea 1 del batch!!!";				
				return sMensajeError;
			}
			
			
		}catch(Exception ex){	
			LogCajaService.CreateLog("revertirAjusteMinimo", "ERR", ex.getMessage());
			sMensajeError = "Error de sistema al realizar reversion de batch por reintegro "+ex; 
			ex.printStackTrace(); 
		}
		return sMensajeError;
	}
/******************************************************************************/
/** Método: Actualizar el valor de la referencia de las minutas de depositos.
 *	Fecha:  30/11/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 **/
	@SuppressWarnings({"unchecked","static-access"})
	public void cambiarReferenciaMPagoN8(ActionEvent ev){
	
		String sReferNueva = "", sRfrAnterior="";
 
		try {
			
			//&& ============== Validar que sea un contador o administrador
			Vautoriz vaut = ((Vautoriz[])CodeUtil.getFromSessionMap( "sevAut"))[0];
			boolean autorizado = 
					vaut.getId().getCodper().compareTo("P000000004") == 0 ||
					vaut.getId().getCodper().compareTo("P000000015") == 0 ||
					vaut.getId().getCodper().compareTo("P000000025") == 0 ;

			if(!autorizado){
				lblMsgCambioReferMPago.setValue("No tiene autorización para realizar esta operación");
				return;
			}
			
			//&&========= Captar el objeto Vrecibosxtipompago del grid a actualizar.
			lblMsgCambioReferMPago.setStyle("color:red");
			lblMsgCambioReferMPago.setValue("");
			RowItem ri = (RowItem)ev.getComponent().getParent().getParent();
			Vrecibosxtipompago vrec = (Vrecibosxtipompago)gvRecibosxMpago8N.getDataRow(ri);//rv_gvEditarRecibosIdPos.getDataRow(ri);
			VrecibosxtipompagoId vId  = vrec.getId();
				
			// Valor anterior de la referencia.
			sRfrAnterior = String.valueOf( vrec.getId().getReferencenumber() ) ;
			
			// Valor nuevo ingresado.
			sReferNueva = vId.getRefer2();
			if(!sReferNueva.matches("^[0-9]{1,8}$")){
				lblMsgCambioReferMPago.setValue("El valor debe ser numérico de 1 a 8 dígitos");
				return;
			}
			
			Varqueo va = (Varqueo)CodeUtil.getFromSessionMap( "rva_VARQUEO");
			if(va.getId().getEstado().equals("A")){
				lblMsgCambioReferMPago.setValue("El estado del arqueo debe ser 'PENDIENTE' para realizar la operación");
				return;
			}
			
			if( vrec.getId().getDepctatran() == 0 ){
				
				F55ca014 f14 =   CompaniaCtrl.obtenerF55ca014(va.getId().getCaid(), va.getId().getCodcomp());
				if(f14 == null){
					lblMsgCambioReferMPago.setValue("F55CA014: Error al obtener información de compañía: "+va.getId().getCodcomp());
					return;
				}
				
				String sResul = new CtrlCajas().verificarReferenciaPago(sReferNueva, Integer.parseInt(sReferNueva),
											"ZX", f14.getId().getC4bnc(),f14.getId().getC4rp01(), vId.getMoneda(), "");
				if(sResul != null){
					lblMsgCambioReferMPago.setValue(sResul);
					return;
				}	
			}
			 
			//&& ========= Actualizar el valor de la referencia en el registro de RECIBODET.
			ReciboCtrl rcCtrl  = new ReciboCtrl();
			boolean bHecho = rcCtrl.actualizarReferencia8N(vId, sRfrAnterior);
			if(bHecho){
				int iPagina = gvRecibosxMpago8N.getPageIndex();
				DetalleArqueoCaja dac = (DetalleArqueoCaja)CodeUtil.getFromSessionMap( "rv_DAC");
				lstRecibosxMpago8N = new RevisionArqueoCtrl().
											obtenerRecibosMpago8N(lstRecibosxMpago8N, vId.getMpago(), 
																	vId.getCaid(),vId.getCodcomp(),
																	vId.getCodsuc(),vId.getMoneda());
				dac.setVrtransbanco(lstRecibosxMpago8N);
				CodeUtil.putInSessionMap("rv_DAC", dac);
				CodeUtil.putInSessionMap("rv_lstRecibosxMpago8N",lstRecibosxMpago8N);
				gvRecibosxMpago8N.dataBind();
				gvRecibosxMpago8N.setPageIndex(iPagina);
				lblMsgCambioReferMPago.setStyle("color: green");
				lblMsgCambioReferMPago.setValue("Se actualizo # referencia de "+sRfrAnterior +" a "+sReferNueva);
				
			}else{
				lblMsgCambioReferMPago.setValue(rcCtrl.getError().toString().split("@")[1]);
			}
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}
/******************************************************************************/
/** Método: Cargar los datos para mostrar el detalle de forma de pago para Transferencias y depositos.
 *	Fecha:  29/11/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 **/
	@SuppressWarnings("unchecked")
	 public void cargarRecibosMpago8N(ActionEvent ev){
		 String sTextoEncabezado = ""; 
		 String sLinkid = "";
		 List<Vrecibosxtipompago>lstRecibosMago8N = null;
		 
		 
		 try {
			 lblMsgCambioReferMPago.setValue("");
			 DetalleArqueoCaja dac = (DetalleArqueoCaja)CodeUtil.getFromSessionMap( "rv_DAC");
			 sLinkid = ev.getComponent().getId();
			 
			 //&& ====== Traer recibos por transferencias en banco.
			 if(sLinkid.compareTo("lnkACDetfp_TransBanco") == 0){
				 	sTextoEncabezado  = "Detalle Recibos pagados con transferencia en banco";
				 	
				 	lstRecibosMago8N = dac.getVrtransbanco();
				 	
					if(lstRecibosMago8N == null){	
						lstRecibosMago8N = dac.getRtransbanco();
						lstRecibosMago8N = new RevisionArqueoCtrl().
													obtenerRecibosMpago8N(lstRecibosMago8N, MetodosPagoCtrl.TRANSFERENCIA, 
															dac.getCaid(),dac.getCodcomp(),
															dac.getCodsuc(),dac.getMoneda());
						dac.setVrtransbanco(lstRecibosMago8N);
					}
				}	
			 else 
				 if(sLinkid.compareTo("lnkACDetfp_DepDbanco") == 0){
				 	sTextoEncabezado = "Detalle Recibos pagados Depósito en banco";
					lstRecibosMago8N = dac.getVrdepbanco();
					if(lstRecibosMago8N == null){	
						lstRecibosMago8N = dac.getRdepbanco();
						lstRecibosMago8N = new RevisionArqueoCtrl().
													obtenerRecibosMpago8N(lstRecibosMago8N, MetodosPagoCtrl.DEPOSITO, 
															dac.getCaid(),dac.getCodcomp(),
															dac.getCodsuc(),dac.getMoneda());
						dac.setVrdepbanco(lstRecibosMago8N);
					}
				 }
			 
				if(lstRecibosMago8N !=null && lstRecibosMago8N.size()>0){
					CodeUtil.putInSessionMap("rv_DAC", dac);
					CodeUtil.putInSessionMap("rv_lstRecibosxMpago8N",lstRecibosMago8N);
					gvRecibosxMpago8N.dataBind();
					gvRecibosxMpago8N.setPageIndex(0);			
					hdReciboMPago8N.setCaptionText(sTextoEncabezado);
					dwRecibosxMpago8N.setWindowState("normal");
				}	
			 
		} catch (Exception e) {
			rv_dwReciboxtipoMetPago.setWindowState("hidden");
			e.printStackTrace();
		}
	 }
/***************************************************************************************************************************************************/	
	@SuppressWarnings("unchecked")
	public boolean registrarIVAcomisionAfiliado(int iNoarqueo,List<ResumenAfiliado> lstResumenPos,int iCaid,String sCodcomp,
												String sCodsuc,	Date dtFecha, Date dtHoraArqueo, String sMoneda,
												 F55ca014 f14, Session session, String tipoCliente, BigDecimal tasaOficial){
		boolean bHecho = true;
		int iNoBatch = 0,iMontoH,iNoDocumento = 0,iCajaUso=0;
		String sCuentaC[], sSucursalDeposito ="", sConcepto,sFecha,sMensaje="",sCuentaRet = "";
		String sCuentaTemp[] = null;
		Vautoriz vaut;
		Divisas dv = new Divisas();
		ReciboCtrl recCtrl = new ReciboCtrl();
		Vf0901 vf0901 = null;
		 
		
		try {
			 
			String monedaBaseCompania = f14.getId().getC4bcrcd() ;
			vaut =  ((Vautoriz[]) CodeUtil.getFromSessionMap( "sevAut"))[0];
			sFecha = FechasUtil.formatDatetoString(dtFecha, "dd/MM/yyyy");
			
			sCuentaC = null;
			sCuentaTemp = null;
			for (int i = 0; i < lstResumenPos.size(); i++) {
				ResumenAfiliado ra = (ResumenAfiliado)lstResumenPos.get(i);
				
				if(ra.getIcaidpos()== iCaid){
					if(sCuentaTemp == null){
						sCuentaTemp = dv.obtenerCuentaCaja(iCaid, sCodcomp, MetodosPagoCtrl.TARJETA, sMoneda, session, null, null,null );
					}
					iCajaUso = iCaid;
					sCuentaC = sCuentaTemp;
				}else{
					iCajaUso = ra.getIcaidpos();
					sCuentaC = dv.obtenerCuentaCaja(ra.getIcaidpos(), sCodcomp, MetodosPagoCtrl.TARJETA, sMoneda,  session, null, null,null );
				}
			 
				
				if(sCuentaC == null){
					sMensaje = "No se ha podido leer la cuenta de Caja para el afiliado: "+ra.getCodigo();
					sMensaje += " de configuración de caja origen: "+ra.getIcaidpos();
					CodeUtil.putInSessionMap("sMensajeError", sMensaje);
					return false;
				}else{
					sConcepto = "Arqueo "+iNoarqueo+", Caja "+iCajaUso+ " "+ sFecha;
					if (sCodcomp.trim().equals("E08")) {
						sSubIVAcomision = "101301";
					}
					
					vf0901 = dv.validarCuentaF0901(sCuentaC[3], sObjIVAcomision, sSubIVAcomision);
					if(vf0901==null){
						bHecho = false;
						sMensaje = "No se ha podido leer la cuenta de Comision "+sObjIVAcomision+"."+sSubIVAcomision+" para la unidad de negocios: "+ sCuentaC[3] + " Revisar la configuracion de cuentas de JDE";
						CodeUtil.putInSessionMap("sMensajeError", sMensaje);
						return false;
					}else{	
						sCuentaRet = vf0901.getId().getGmmcu().trim() + "." + sObjIVAcomision;
						if(!sSubIVAcomision.trim().equals("")){
							sCuentaRet = sCuentaRet + "." + sSubIVAcomision;
						}
						vf0901.setCuenta(sCuentaRet);
						sSucursalDeposito = sCuentaC[2];
					}
				}
				
				iMontoH = Divisas.pasarAentero((Divisas.roundDouble(ra.getIvaComision().doubleValue())));
				
				iNoBatch = Divisas.numeroSiguienteJdeE1(   );
				if(iNoBatch == -1){
					bHecho = false;
					sMensaje = "No se ha podido obtener el Número de batch para registro de POS "+ra.getCodigo();
					break;
				}
				
				 
				if(!sMoneda.equals( monedaBaseCompania )){
					iMontoH = Divisas.pasarAentero( ra.getIvaComision().multiply(tasaOficial).setScale(2, RoundingMode.HALF_UP).doubleValue() ) ;							
				} 
				
				bHecho = recCtrl.registrarBatchA92Session( session, dtFecha, valoresJdeInsContado[8], iNoBatch, iMontoH, vaut.getId().getLogin(), 1, "APRARQUEO", valoresJdeInsContado[9]);
				
				if(bHecho){
					iNoDocumento = dv.leerActualizarNoDocJDE_AprobArqueo(session);
					if(iNoDocumento == -1){
						bHecho = false;
						sMensaje = "No se ha podido obtener el Número de Documento para el registro de POS "+ra.getCodigo();
						break;
					}
					
					String logs = sConcepto;
					
					bHecho  =  recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sSucursalDeposito,"P9",
										iNoDocumento,1.0,iNoBatch,vf0901.getCuenta(),
										vf0901.getId().getGmaid(),vf0901.getId().getGmmcu().trim(),
										vf0901.getId().getGmobj(), vf0901.getId().getGmsub(),"AA", 
										f14.getId().getC4bcrcd(), iMontoH,  sConcepto, vaut.getId().getLogin(),
										vaut.getId().getCodapp(), BigDecimal.ZERO, tipoCliente,
										"Deb. IVA de Comision",
										vf0901.getId().getGmco().trim(), "","", monedaBaseCompania,
										vf0901.getId().getGmco().trim(), "D", 0);
													
					if(bHecho){
						bHecho  = recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sSucursalDeposito,"P9", 
											iNoDocumento,2.0,iNoBatch,sCuentaC[0],
											sCuentaC[1],sCuentaC[3],sCuentaC[4],sCuentaC[5],"AA", f14.getId().getC4bcrcd(), 
											iMontoH*(-1),sConcepto, vaut.getId().getLogin(),vaut.getId().getCodapp(),
											BigDecimal.ZERO, tipoCliente,"Cred. Caja por POS "+ra.getCodigo(),sCuentaC[2],
											"","", monedaBaseCompania, vf0901.getId().getGmco().trim(), "D", 0);
						
						if(bHecho){
							
							List<Object[]> lstDeps = new ArrayList<Object[]>();
							if (CodeUtil.getFromSessionMap("rv_listaDepositos") != null)
								lstDeps = (ArrayList<Object[]>) CodeUtil.getFromSessionMap("rv_listaDepositos");
							
							
							Object oDep[] = new Object[20];
							oDep[0]= iCajaUso;
							oDep[1]= sCodsuc;
							oDep[2]= sCodcomp;
							oDep[3]= iNoBatch;
							oDep[4]= iNoDocumento;
							oDep[5]= iNoarqueo;
							oDep[6]= sMoneda;
							oDep[7]= new BigDecimal(ra.getMontoRetencion().doubleValue());
							oDep[8]= dtFecha;
							oDep[9]= dtHoraArqueo;
							oDep[10]= vaut.getId().getLogin();
							oDep[11]= ra.getReferencia();
							oDep[12]= "I";
							oDep[14]= MetodosPagoCtrl.TARJETA;
							oDep[15]= tasaOficial;
							
							List<Ctaxdeposito> lstCtasxDeps = new ArrayList<Ctaxdeposito>();
							Ctaxdeposito ctaxDeps  = new Ctaxdeposito();
							ctaxDeps.setMonto(ra.getIvaComision());
							ctaxDeps.setTipomov("C");
							ctaxDeps.setIdcuenta(vf0901.getId().getGmaid());
							ctaxDeps.setGmmcu(vf0901.getId().getGmmcu().trim());
							ctaxDeps.setGmobj(vf0901.getId().getGmobj());
							ctaxDeps.setGmsub(vf0901.getId().getGmsub().trim());
							lstCtasxDeps.add(ctaxDeps);
							
							//&& ========== Objeto para debito, caja.
							ctaxDeps = new Ctaxdeposito();
							ctaxDeps.setMonto(ra.getIvaComision());
							ctaxDeps.setTipomov("D");
							ctaxDeps.setIdcuenta(sCuentaC[1]);
							ctaxDeps.setGmmcu(sCuentaC[3]);
							ctaxDeps.setGmobj(sCuentaC[4]);
							ctaxDeps.setGmsub(sCuentaC[5]);
							lstCtasxDeps.add(ctaxDeps);
							oDep[16]= lstCtasxDeps;
							oDep[17] = ra.getCodigobancopos();
							oDep[18] = ra.getCodigobancopos();
							oDep[19] = 0;
							
							lstDeps.add(oDep);
							CodeUtil.putInSessionMap("rv_listaDepositos", lstDeps);
							
						}else{
							sMensaje =  " No se ha podido registrar línea 2 para batch: "+iNoBatch;
							sMensaje += " para registro de depósito general de POS: "+ra.getCodigo()+ ", en "+sMoneda;
						}
					}else{
						sMensaje =  " No se ha podido registrar línea 1 para batch: "+iNoBatch;
						sMensaje += " para registro de depósito general de POS: "+ra.getCodigo()+ ", en "+sMoneda;
					}
					//
				}else{
					sMensaje = "No se ha podido registrar el batch: "+iNoBatch + " para registro de depósito general de POS: "+ra.getCodigo();
				}
				//--- Detener la ejecución del ciclo en caso de errores.
				if(!bHecho)
					break;
			
			}//--fin de for de lista
			
			 
			
		} catch (Exception error) {
			
			bHecho = false;
			sMensaje = "Depósitos por retenciones de Afiliados no pueden ser generados en JDE" ;
			error.printStackTrace(); 
			 
		}finally{
			
			if(!bHecho){
				CodeUtil.putInSessionMap( "sMensajeError", sMensaje );
			}
			
		}
		return bHecho;
	}

/******************************************************************************/
/** Método: Registrar asientos de diario para Efectivo y Cheque Separados.
 *	Fecha:  13/06/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 **/@SuppressWarnings("unchecked")
	public boolean registrarTransaccionesChequeEfectivo(List<LiquidacionCheque>lstLiquidaCheque,int iNoarqueo,
								Date dtFecha, Date HoraArqueo,  Session session,
								String sCtaBcoTransitoria[], F55ca014 f14, 
								Varqueo ar, String sTipoCliente, BigDecimal tasaOficial){
		boolean bHecho = true;
		String sConcepto;
		String sCtaCja[];
		String sCtaBco[];
		 
		int iNoBatch = 0;
		int iNoDocumento = 0;
		ClsParametroCaja cajaparm = new ClsParametroCaja();
		long iMontoTotalBanco ;
		
		Divisas dv = new Divisas();
		ReciboCtrl recCtrl = new ReciboCtrl();
		String sTipoDoc = "";
		String sDescrip = "";
		
		List<Ctaxdeposito> lstCtasxDeps = null;
		Ctaxdeposito ctaxDeps = null;
		
		String strSubLibroCuenta = "";
		String tipoAuxiliarCtaTrans = "";
		
		
		
		try {
			sTipoDoc = cajaparm.getParametros("34", "0", "CIERRE_DOCTYPE_ZX").getValorAlfanumerico().toString();
			Vautoriz vaut = ((Vautoriz[]) CodeUtil.getFromSessionMap( "sevAut"))[0];
			 
			sConcepto = "Arqueo "+iNoarqueo+" Caja "+lstLiquidaCheque.get(0).getCaid()+ " "+   FechasUtil.formatDatetoString(dtFecha, "dd/MM/yyyy");
			String logs = sConcepto;
			
			//-----Recorrer la lista de liquidaciones y hacer asiento por cada registro.
			String monedaBaseCompania = f14.getId().getC4bcrcd() ;
					
					
			for (LiquidacionCheque lc : lstLiquidaCheque) {
				
				 
				sCtaCja = dv.obtenerCuentaCaja( lc.getCaid(), lc.getCodcomp(), lc.getMetodopago(), lc.getMoneda(), session, null, null, null);
				
				if(sCtaCja==null){
					CodeUtil.putInSessionMap("sMensajeError","No se ha podido obtener la cuenta de caja para el metodo pago: "+lc.getMetodopago());
					return false;
				}
				
				//&& ======= determinar si usar cuenta transitoria u obtener la cuenta de banco.
				
				boolean ctatransito = (lc.getConciliaauto().compareTo("1") == 0 && f14.getId().isConfrmauto());
				
				if(ctatransito){
					
					if(sCtaBcoTransitoria == null){
						if(errorApr == null)
							CodeUtil.putInSessionMap("sMensajeError","ACF55CA033: No se encontró la configuración de cuentas transitorias. ");
						else
							CodeUtil.putInSessionMap("sMensajeError",errorApr.toString().split("@")[1]);
						return false;
					}
					
					sDescrip = "REF:"+ar.getId().getReferencenumber()+" MP:5 C:"+ar.getId().getCaid();
					sTipoDoc = cajaparm.getParametros("34", "0", "CIERRE_DOCTYPE_P9").getValorAlfanumerico().toString();
					sCtaBco = sCtaBcoTransitoria;
					iNoDocumento =  Divisas.numeroSiguienteJdeE1Custom(valoresJdeNumeracion[8],valoresJdeNumeracion[9] );
					
					
					tipoAuxiliarCtaTrans = cajaparm.getParametros("34", "0", "CODIGO_TIPO_AUX_CT").getValorAlfanumerico().toString(); 
					strSubLibroCuenta = ConfirmaDepositosCtrl.constructSubLibroCtaTbanco(0, lc.getIcodigobanco(), lc.getCaid(), lc.getMoneda(), lc.getCodcomp());
					
				}else{
					sDescrip =  "REF:"+ar.getId().getReferencenumber()+" MP:5 C:"+ar.getId().getCaid() ;
					sTipoDoc = cajaparm.getParametros("34", "0", "TIPODOC_JDE_DEP_5").getValorAlfanumerico().toString() ;
					sCtaBco = dv.obtenerCuentaBanco(lc.getCodcomp(), lc.getMoneda(), lc.getIcodigobanco(), session, null, null, null);
					iNoDocumento = Integer.parseInt(lc.getReferenciaBanco());
					strSubLibroCuenta = "";
					tipoAuxiliarCtaTrans = "";
				}
				
				if(sCtaBco==null){
					CodeUtil.putInSessionMap("sMensajeError","No se ha podido obtener la cuenta de banco para el metodo pago: "+lc.getMetodopago());
					return false;
				}
				
				//&& =============== Validaciones del numero de referencia
				
				iNoDocumento = RevisionArqueoCtrl.generarReferenciaDeposito(iNoDocumento, MetodosPagoCtrl.EFECTIVO , sCtaBco[2], lc.getMoneda(), session);
				
				//********************************************************
				
				iMontoTotalBanco = Divisas.pasarAenteroLong(lc.getBtotalBanco().doubleValue());
				//iNoBatch = dv.leerActualizarNoBatch();
				iNoBatch = Divisas.numeroSiguienteJdeE1(   );
				
				
				//-- Asientos en moneda domestica.
				if( monedaBaseCompania.trim().equals(lc.getMoneda())){
					
					bHecho  =	recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sCtaBco[2], sTipoDoc, iNoDocumento, 1.0, iNoBatch, sCtaBco[0],
										sCtaBco[1], sCtaBco[3],  sCtaBco[4], sCtaBco[5], "AA", lc.getMoneda(), iMontoTotalBanco, sConcepto,
										vaut.getId().getLogin(), vaut.getId().getCodapp(), BigDecimal.ZERO, sTipoCliente,
										sDescrip, sCtaBco[2], strSubLibroCuenta ,tipoAuxiliarCtaTrans, lc.getMoneda(),sCtaBco[2],"D", 0);		
					if(bHecho){
						bHecho = recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sCtaBco[2], sTipoDoc, iNoDocumento, 2.0, iNoBatch, sCtaCja[0],
										sCtaCja[1], sCtaCja[3], sCtaCja[4], sCtaCja[5], "AA", lc.getMoneda(), iMontoTotalBanco*(-1), 
										sConcepto, vaut.getId().getLogin(),vaut.getId().getCodapp(), BigDecimal.ZERO, sTipoCliente,
										sDescrip, sCtaCja[2], "","", lc.getMoneda(),sCtaCja[2], "D", 0);
						if(!bHecho){
							CodeUtil.putInSessionMap("sMensajeError","Error al generar asiento de diario del pago "+lc.getMetodopago() + " "+f14.getId().getC4bcrcd()+" Referencia: " + iNoDocumento);
							break;
						}
					}else{
						CodeUtil.putInSessionMap("sMensajeError","Error al generar asiento de diario del pago "+lc.getMetodopago() + " "+f14.getId().getC4bcrcd()+" Referencia: " + iNoDocumento);
						break;
					}
				}
				//--------- asientos en moneda foranea.
				else{
					
					iMontoTotalBanco = Divisas.pasarAenteroLong( lc.getBtotalBanco().doubleValue());
					long iMontoTotalBancoDom = Divisas.pasarAenteroLong( lc.getBtotalBanco().multiply(tasaOficial).doubleValue());
					
					bHecho  =	recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sCtaBco[2],sTipoDoc, iNoDocumento, 1.0, iNoBatch, sCtaBco[0],
											sCtaBco[1], sCtaBco[3], sCtaBco[4], sCtaBco[5], "AA", lc.getMoneda(), iMontoTotalBancoDom, 
											sConcepto,vaut.getId().getLogin(), vaut.getId().getCodapp(), tasaOficial, sTipoCliente,
											sDescrip, sCtaBco[2], strSubLibroCuenta, tipoAuxiliarCtaTrans, monedaBaseCompania, sCtaBco[2],"F", iMontoTotalBanco);
					if(bHecho){
						bHecho  = recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha,  sCtaBco[2],sTipoDoc, iNoDocumento, 1.0, iNoBatch, sCtaBco[0],
												sCtaBco[1], sCtaBco[3], sCtaBco[4], sCtaBco[5], "CA", lc.getMoneda(), iMontoTotalBanco, 
												sConcepto,vaut.getId().getLogin(), vaut.getId().getCodapp(), tasaOficial, sTipoCliente,
												sDescrip, sCtaBco[2], strSubLibroCuenta ,tipoAuxiliarCtaTrans, monedaBaseCompania,sCtaBco[2],"F", 0);
					if(bHecho){
						bHecho = recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sCtaBco[2], sTipoDoc, iNoDocumento, 2.0, iNoBatch, sCtaCja[0],
												sCtaCja[1], sCtaCja[3], sCtaCja[4], sCtaCja[5], "AA", lc.getMoneda(), iMontoTotalBancoDom*(-1), 
												sConcepto,vaut.getId().getLogin(), vaut.getId().getCodapp(), tasaOficial, sTipoCliente,
												"Crédito Caja Mpago: "+lc.getMetodopago(),sCtaCja[2], "","", monedaBaseCompania, sCtaCja[2],"F", (iMontoTotalBancoDom*(-1)));
						if(bHecho){
							bHecho = recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sCtaBco[2],sTipoDoc, iNoDocumento, 2.0, iNoBatch,  sCtaCja[0],
												sCtaCja[1], sCtaCja[3], sCtaCja[4], sCtaCja[5], "CA", lc.getMoneda(), iMontoTotalBanco*(-1), 
												sConcepto,vaut.getId().getLogin(), vaut.getId().getCodapp(), tasaOficial, sTipoCliente,
												"Crédito Caja Mpago: "+lc.getMetodopago(),sCtaCja[2],"","",monedaBaseCompania,sCtaCja[2],"F", 0);
							
								if(!bHecho){
									CodeUtil.putInSessionMap("sMensajeError","Error al generar asiento de diario del pago "+lc.getMetodopago() + " "+f14.getId().getC4bcrcd()+" Referencia: " + iNoDocumento);
									break;
								}
							}else{
								CodeUtil.putInSessionMap("sMensajeError","Error al generar asiento de diario del pago "+lc.getMetodopago() + " "+f14.getId().getC4bcrcd()+" Referencia: " + iNoDocumento);
								break;
							}
						}else{
							CodeUtil.putInSessionMap("sMensajeError","Error al generar asiento de diario del pago "+lc.getMetodopago() + " "+f14.getId().getC4bcrcd()+" Referencia: " + iNoDocumento);
							break;
						}
					}else{
						CodeUtil.putInSessionMap("sMensajeError","Error al generar asiento de diario del pago "+lc.getMetodopago() + " "+f14.getId().getC4bcrcd()+" Referencia: " + iNoDocumento);
						break;
					}
				}
				//------ Registrar el encabezado del asiento de diario correspondiente.
				if(bHecho){
					

					bHecho = recCtrl.registrarBatchA92Session( session, dtFecha, valoresJdeInsContado[8], iNoBatch, iMontoTotalBanco, vaut.getId().getLogin(), 1 , "APRARQUEO", valoresJdeInsContado[9]);
					
					
					if(bHecho){
						
						List<Object[]> lstDeps = new ArrayList<Object[]>();
						if (CodeUtil.getFromSessionMap("rv_listaDepositos") != null)
							lstDeps = (ArrayList<Object[]>) CodeUtil.getFromSessionMap("rv_listaDepositos");
						
						Object oDep[] = new Object[20];
						oDep[0]= lc.getCaid();
						oDep[1]= lc.getCodsuc();
						oDep[2]= lc.getCodcomp();
						oDep[3]= iNoBatch;
						oDep[4]= iNoDocumento;
						oDep[5]= iNoarqueo;
						oDep[6]= lc.getMoneda();
						oDep[7]= lc.getBtotalBanco();
						oDep[8]= dtFecha;
						oDep[9]= HoraArqueo;
						oDep[10]= vaut.getId().getLogin();
						oDep[11]= lc.getReferenciaBanco();
						oDep[12]= "D";
						oDep[14]= lc.getMetodopago();
						oDep[15]= tasaOficial;

						//&& =========== Crear el objeto que contendra la informacion de los movimientos sobre las cuentas.
						lstCtasxDeps = new ArrayList<Ctaxdeposito>();
						ctaxDeps = new Ctaxdeposito();
						ctaxDeps.setMonto(lc.getBtotalBanco());
						ctaxDeps.setTipomov("C");
						ctaxDeps.setIdcuenta(sCtaBco[1]);
						ctaxDeps.setGmmcu(sCtaBco[3]);
						ctaxDeps.setGmobj(sCtaBco[4]);
						ctaxDeps.setGmsub(sCtaBco[5]);
						lstCtasxDeps.add(ctaxDeps);
						
						//&& ========== Objeto para debito, caja.
						ctaxDeps = new Ctaxdeposito();
						ctaxDeps.setMonto(lc.getBtotalBanco());
						ctaxDeps.setTipomov("D");
						ctaxDeps.setIdcuenta(sCtaCja[1]);
						ctaxDeps.setGmmcu(sCtaCja[3]);
						ctaxDeps.setGmobj(sCtaCja[4]);
						ctaxDeps.setGmsub(sCtaCja[5]);
						lstCtasxDeps.add(ctaxDeps);
						
						oDep[16] = lstCtasxDeps;
						oDep[17] = lc.getIcodigobanco();
						oDep[18] = lc.getReferenciaBanco();
						oDep[19] = (ctatransito)? 1 : 0;
						
						lstDeps.add(oDep);
						CodeUtil.putInSessionMap("rv_listaDepositos", lstDeps);
						
						
					}else{
						CodeUtil.putInSessionMap("sMensajeError","No se ha podido registrar el Encabezado de asiento de diario para banco: "+
										lc.getCodigoBanco()+" para metodo pago: "+lc.getMetodopago());
						break;
					}
				}
			}
		} catch (Exception e) {
			
			e.printStackTrace(); 
			bHecho = false;
			
			CodeUtil.removeFromSessionMap("rv_bvalidoCheques");
			CodeUtil.putInSessionMap("sMensajeError","Error de Sistema al guardar Asientos Cheque/Efectivo ");
			
		}
		return bHecho;
	}
/******************************************************************************/
/** Método: Asignar referencia a los totales por banco.
 *	Fecha:  13/06/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 **/
	@SuppressWarnings("unchecked")
	public void asignarReferenciaBancos(ActionEvent ev){
		String[] sReferencias = null;
		String sReferencia="",sMensaje="";
		boolean bValido = true;
		String sResul;
		List<LiquidacionCheque>lstLiquidaCheque = new ArrayList<LiquidacionCheque>();
		
		try {
			CodeUtil.putInSessionMap("rv_bvalidoCheques", "false");
			List filasGrid = rv_gvAsignarReferenciaCheque.getRows();
			sReferencias = new String[filasGrid.size()];

			//---Estilos iniciales antes de validaciones. 
			for(int i=0; i<filasGrid.size();i++){
				RowItem ri = (RowItem)filasGrid.get(i);
				List lstCeldas = ri.getCells();
				Cell celda = (Cell)lstCeldas.get(5);
				HtmlInputText hiRefer = (HtmlInputText)celda.getChildren().get(0);
				hiRefer.setStyle("height: 11px; width: 70px; text-align: right");
				hiRefer.setStyleClass("frmInput2ddl");
			}
			for(int i=0; i<filasGrid.size();i++){
				RowItem ri = (RowItem)filasGrid.get(i);
				LiquidacionCheque lchk = (LiquidacionCheque)rv_gvAsignarReferenciaCheque.getDataRow(ri);
				sReferencia = lchk.getReferenciaBanco();
				
				//---- Validaciones de campo y existencia en  f0911
				if(sReferencia.matches("^[0-9]{1,8}$")){
					
					F55ca022 f22 = new BancoCtrl().obtenerBancoxId(Integer.parseInt(lchk.getCodigoBanco()));
					if(f22 == null){
						sMensaje = "F55CA022: No se ha podido obtener la configuración de banco para "+ lchk.getCodigoBanco(); 
						bValido = false;
					}else{
						if(f22.getId().getConciliar() == 0){
							sResul =  new CtrlCajas()
											.verificarReferenciaPago(sReferencia, Integer.parseInt(sReferencia),"ZX",
																	Integer.parseInt(lchk.getCodigoBanco()), 
																	lchk.getCodcomp().trim(), lchk.getMoneda(), "");
							if(sResul!=null){
								sMensaje = sResul;
								bValido = false;
							}
						}
					}
				}else{
					bValido = false;
					sMensaje = "El valor de la referencia debe contener entre 1 y 8 dígitos unicamente";
				}

				//----- Verificar que la referencia no se repita entre las que se estan registrando.
				if(bValido){
					if(i==0){ 
						sReferencias[i]=sReferencia;
						lstLiquidaCheque.add(lchk);
					}
					else{
						for(int j=0;j<i;j++){
							if(sReferencias[j].equals(sReferencia)){
								bValido=false;
								sMensaje = "El valor de referencia: " +sReferencia + " ya ha sido incluído";
								break;
							}
						}
						if(bValido){
							sReferencias[i] = sReferencia;
							lstLiquidaCheque.add(lchk);
						}
					}
				}
				//--------- Actualizar el estado de caja de texto
				if(!bValido){
					List lstCeldas = ri.getCells();
					Cell celda = (Cell)lstCeldas.get(5);
					HtmlInputText hiRefer = (HtmlInputText)celda.getChildren().get(0);
					hiRefer.setStyle("height: 11px; width: 70px; text-align: right; border: 1px solid red;");
					break;
				}
			}
			//------ Validar si se debe continuar o mostrar mensaje de error.
			if(bValido){
				CodeUtil.putInSessionMap("rva_lstLiquidacionCheques", lstLiquidaCheque);	//--Datos para asientos de depósitos
				rv_dwAsignarReferCheque.setWindowState("hidden");
				CodeUtil.putInSessionMap("rv_bvalidoCheques", "true");
			}else{
				lblMsgErrorAsignarReferChk.setValue(sMensaje);
			}
		} catch (Exception e) {
			lblMsgErrorAsignarReferChk.setValue("Error en asignacion de Referencia de Banco");
			e.printStackTrace();
		}
	}
/******************************************************************************/
/** Método: Mostrar Totales de cheques agrupados por banco de la transaccion.
 *	Fecha:  13/06/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 **/
	@SuppressWarnings("unchecked")
	public void mostrarTotalChequeBanco(ActionEvent ev){
		RevisionArqueoCtrl rva = new RevisionArqueoCtrl();
		try {
			//--- Obtener los totales por banco de pagos con cheques desde la consulta a base de datos 
			CodeUtil.removeFromSessionMap( "rva_lstLiquidacionCheques");
			DetalleArqueoCaja   dac = (DetalleArqueoCaja)CodeUtil.getFromSessionMap( "rv_DAC");
			String sLstRecibos  = String.valueOf(CodeUtil.getFromSessionMap( "rv_ListaRecibosArqueo"));
			
			List<LiquidacionCheque>lcks = rva.obtenerTotalesChequexBanco(dac.getCaid(),dac.getMoneda(),
													dac.getFechaarqueo(),dac.getCodcomp(),dac.getCodsuc(),sLstRecibos);
			if(lcks==null){
				dwValidarAprobacionArqueo.setWindowState("normal");
				lblValidarAprobacion.setValue("No se han registrado transacciones de pago con cheque en el arqueo");
				CodeUtil.putInSessionMap("rv_bvalidoCheques", "true");
				CodeUtil.removeFromSessionMap( "rva_lstAsignarReferenciaCheque");
			}
			else{
				CodeUtil.putInSessionMap("rv_bvalidoCheques", "false");
				lblMsgErrorAsignarReferChk.setValue("");
				rv_lstAsignarReferenciaCheque = lcks;
				CodeUtil.putInSessionMap("rva_lstAsignarReferenciaCheque", rv_lstAsignarReferenciaCheque);
				rv_gvAsignarReferenciaCheque.dataBind();
				rv_dwAsignarReferCheque.setWindowState("normal");
			}
		} catch (Exception e) {
			CodeUtil.putInSessionMap("rv_bvalidoCheques", "false");
			rv_lstAsignarReferenciaCheque = null;
			rv_dwAsignarReferCheque.setWindowState("hidden");
			e.printStackTrace();
		}finally{
			rva = null;
		}
	}
	public void cerrarAgregarReferCheque(ActionEvent ev){
		CodeUtil.removeFromSessionMap( "rv_bvalidoCheques");
		rv_dwAsignarReferCheque.setWindowState("hidden");
	}
	/******************************************************************************/
	/** Método: Validar que se haya seleccionado un registro de voucher's manuales.
	 *	Fecha:  26/10/2010
	 *  Nombre: Carlos Manuel Hernández Morrison.
	 **/
	public void validarLiquidacionTrans(ValueChangeEvent ev){
		RowItem ri = null;
		ResumenAfiliado ra = null;
		List lstCeldas  = null;
		Cell celda = null;
		HtmlCheckBox chkVmanual = null;
			
		 try {
			 ri = (RowItem)ev.getComponent().getParent().getParent();
			 ra = (ResumenAfiliado)gvReferenciaPos.getDataRow(ri);
			 
			//--- Obtener objetos para actualización del grid
			lstCeldas = ri.getCells();
			celda = (Cell)lstCeldas.get(9);
			chkVmanual = (HtmlCheckBox)celda.getChildren().get(0);
			
			if(chkVmanual.isChecked()){
				if(ra.getScodvmanual().equals("0")){
					lblMsgValidaReferencia.setValue("Seleccione solo liquidaciones a voucher manuales");
					chkVmanual.setChecked(false);
				}else{
					lblMsgValidaReferencia.setValue("");
				}
			}
		} catch (Exception error) {
			lblMsgValidaReferencia.setValue("Error al validar la operacion!!!!");
			error.printStackTrace();
		} 
	}
/******************************************************************************************************/
/** Método: Validar y actualizar el valor del Id del afiliado seleccionado.
 *	Fecha:  24/09/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 **/
	public void actualizarIdPOS(ActionEvent ev){
		Vrecibosxtipompago vrec = null;
		VrecibosxtipompagoId vId = null;

		RowItem ri=null;
		String sIdPos="",sMensaje="",sRfrAnterior="";
		boolean bValido = true;
		List lstCeldas = null;
		Cell celda = null;
		HtmlInputText hiRfrNueva = null;
		HtmlOutputText hoRfrAnterior = null;
		int iPagina=0;
		
		try {
			
			//&& ============== Validar que sea un contador o administrador
			Vautoriz vaut = ((Vautoriz[])CodeUtil.getFromSessionMap( "sevAut"))[0];
			boolean autorizado = 
					vaut.getId().getCodper().compareTo("P000000004") == 0 ||
					vaut.getId().getCodper().compareTo("P000000015") == 0 ||
					vaut.getId().getCodper().compareTo("P000000025") == 0 ;

			if(!autorizado){
				bValido = false;
				lblMsgErrorCambioRefer.setStyleClass("frmLabel2Error");
				lblMsgErrorCambioRefer.setValue("No tiene autorización para realizar esta operación");
				return;
			}
			
			
			lblMsgErrorCambioRefer.setStyle("");
			lblMsgErrorCambioRefer.setStyleClass("frmLabel2Error");
			
			//--- Registros de Vreciboxtipompago.
			ri   = (RowItem)ev.getComponent().getParent().getParent();
			vrec = (Vrecibosxtipompago)rv_gvEditarRecibosIdPos.getDataRow(ri);
			vId  = vrec.getId();
			
			//--- Obtener objetos para actualización del grid
			lstCeldas = ri.getCells();
			celda = (Cell)lstCeldas.get(6);
			hiRfrNueva = (HtmlInputText)celda.getChildren().get(0);
			hiRfrNueva.setStyleClass("frmInput2ddl");
			hiRfrNueva.setStyle("height: 11px; width: 60px; text-align: right");
			
			
			celda = (Cell)lstCeldas.get(1);
			hoRfrAnterior = (HtmlOutputText)celda.getChildren().get(0);
			String cero  = hoRfrAnterior.getValue().toString().trim();
			
			
			//--- Valor anterior de la referencia.
			celda = (Cell)lstCeldas.get(10);
			hoRfrAnterior = (HtmlOutputText)celda.getChildren().get(0);
			sRfrAnterior  = hoRfrAnterior.getValue().toString().trim();			
				
			sIdPos = vrec.getId().getRefer1().trim();
			if(!sIdPos.matches("^[0-9]{8}$")){
				bValido = false;
				sMensaje = "El valor debe ser numérico de 8 dígitos";
			}else
			if(Integer.parseInt(sIdPos)== Integer.parseInt(sRfrAnterior)){
				bValido = false;
				sMensaje = "El código del afiliado no debe ser el mismo";
			}
			//--- Verificar que exista el valor para el id del pos.
			else{
				Cafiliados cafiliado = null;
				AfiliadoCtrl afCtrl = new AfiliadoCtrl();
				cafiliado = afCtrl.buscarPOSxCajaCompania(Integer.parseInt(sIdPos), vId.getCaid(),
															vId.getCodcomp(),vId.getMoneda());
				if(cafiliado==null){
					bValido = false;
					sMensaje = "El código no está asociado a un afiliado para la caja ";
				}
			}
			//---- Actualización del registro
			if(bValido){
				//--- Actualizar el registro en la base de datos.
				Recibodet rd = new Recibodet();
				RecibodetId rdId = new RecibodetId();
				
				rdId.setCaid(vId.getCaid());
				rdId.setCodcomp(vId.getCodcomp());
				rdId.setCodsuc(vId.getCodsuc());
				rdId.setMoneda(vId.getMoneda());
				rdId.setMpago(vId.getMpago());
				rdId.setNumrec(vId.getNumrec());
				rdId.setRefer1(sRfrAnterior);
				rdId.setRefer2(vId.getRefer2());
				rdId.setRefer3(vId.getRefer3());
				rdId.setRefer4(vId.getRefer4());
				rdId.setTiporec(vId.getTiporec());
				
				rd.setId(rdId);
				rd.setEquiv(vId.getEquiv());
				rd.setMonto(vId.getMonto());
				rd.setNumrecm(vId.getNumrecm());
				rd.setTasa(vId.getTasa());
//				rd.setRefer5(vId.getRefer5());
//				rd.setRefer6(vId.getRefer6());
//				rd.setRefer7(vId.getRefer7());
				
				ReciboCtrl rc = new ReciboCtrl();
				bValido = rc.actualizarRecibodet(rd,sIdPos);
				if(bValido){
					sMensaje = "Se actualizó correctamente el código de afiliado de "+sRfrAnterior+ " a "+sIdPos;
					sMensaje +=" en recibo " +rd.getId().getNumrec();
					DetalleArqueoCaja   dac = (DetalleArqueoCaja)CodeUtil.getFromSessionMap( "rv_DAC");
					
					RevisionArqueoCtrl rvc = new RevisionArqueoCtrl();
					List lstRecibos = dac.getRtcredito();
					lstRecibos = rvc.obtenerRecibosmpago(lstRecibos, MetodosPagoCtrl.TARJETA,dac.getCaid(),dac.getCodcomp(),
														  dac.getCodsuc(),dac.getMoneda());
					dac.setVrtcredito(lstRecibos);
					CodeUtil.putInSessionMap("rv_DAC",dac);
					rv_lstEditarRecibosIdPos = lstRecibos;
					CodeUtil.putInSessionMap("rva_rv_lstEditarRecibosIdPos",lstRecibos);
					
					iPagina = rv_gvEditarRecibosIdPos.getPageIndex();
					rv_gvEditarRecibosIdPos.dataBind();
					rv_gvEditarRecibosIdPos.setPageIndex(iPagina);
					
					String sEstilo = "font-family: Arial, Helvetica, sans-serif;font-size: 11px;";
					sEstilo += "font-weight: bold;color: green";
					lblMsgErrorCambioRefer.setStyle(sEstilo);
					
				}else{
					sMensaje = "El recibo no se pudo actualizar al valor " +sIdPos;
					hiRfrNueva.setValue(sRfrAnterior.trim());
					hiRfrNueva.setStyle("height: 11px; width: 60px; text-align: right; border: 1px solid red;");
				}
			}else{
				hiRfrNueva.setStyle("height: 11px; width: 60px; text-align: right; border: 1px solid red;");
			}
			lblMsgErrorCambioRefer.setValue(sMensaje);		
			
		} catch (Exception error) {
			error.printStackTrace(); 
		}
	}
/******************************************************************************************************/
/** Método: Mostrar la lista de recibos pagados con tarjetas de crédito.
 *	Fecha:  21/09/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 **/
	@SuppressWarnings("unchecked")
	public void mostrarCambiarIdPOS(ActionEvent ev){
		List lstRecibos = null;
		DetalleArqueoCaja dac = null;
		RevisionArqueoCtrl rvc = new RevisionArqueoCtrl();
		Varqueo v = null;
		try {
			v = (Varqueo)CodeUtil.getFromSessionMap( "rva_VARQUEO");
			if(!v.getId().getEstado().equals("P")){
				dwValidarAprobacionArqueo.setWindowState("normal");
				lblValidarAprobacion.setValue("El estado del arqueo debe ser 'PENDIENTE' para realizar la petición");
			}else{
				lblMsgErrorCambioRefer.setValue("");
				dac = (DetalleArqueoCaja)CodeUtil.getFromSessionMap( "rv_DAC");
				lstRecibos = new ArrayList();
				
				//--- Cambiar la fuente de datos si son manuales o automaticos.
				if(Double.parseDouble(lblACDetfp_TarCred.getValue().toString())>0
					&& dac.getRtcredito()!=null && dac.getRtcredito().size()>0){
					lstRecibos.addAll(dac.getRtcredito());
				}
				if(Double.parseDouble(lblACDetfp_TCmanual.getValue().toString())>0
						&& dac.getRtcreditom()!=null && dac.getRtcreditom().size()>0){
						lstRecibos.addAll(dac.getRtcreditom());
				}
				lstRecibos = rvc.obtenerRecibosmpago(lstRecibos, MetodosPagoCtrl.TARJETA,dac.getCaid(),dac.getCodcomp(),
														dac.getCodsuc(),dac.getMoneda());
				if(lstRecibos!=null && lstRecibos.size()>0){
					CodeUtil.putInSessionMap("rva_rv_lstEditarRecibosIdPos", lstRecibos);
					rv_gvEditarRecibosIdPos.dataBind();
					rv_gvEditarRecibosIdPos.setPageIndex(0);
					rv_dwEditarRecibosIdPos.setWindowState("normal");
				}else{
					dwValidarAprobacionArqueo.setWindowState("normal");
					lblValidarAprobacion.setValue("No se han registrado pagos con tarjeta de crédito");
				}
			}
		} catch (Exception error) {
			dwValidarAprobacionArqueo.setWindowState("normal");
			lblValidarAprobacion.setValue("No se ha podido completar la petición!\n Error de sistema: " +error.getMessage());
			error.printStackTrace();
		}finally{
			dac = null;
			v = null;
			HibernateUtilPruebaCn.closeSession();
		}
	}
/**-------------------------------------------------------------------------------------------- **/
/** Cerrar la ventana que muestra de actualización de POS en los recibos con tarjeta de crédito **/
	public void cerrarActualizarIdPOS(ActionEvent ev){
		try {
			DetalleArqueoCaja dac = (DetalleArqueoCaja)CodeUtil.getFromSessionMap( "rv_DAC");
			dac.setVrtcredito(null);
			CodeUtil.putInSessionMap("rv_DAC",dac);
			dac = null;
			rv_dwEditarRecibosIdPos.setWindowState("hidden");
			CodeUtil.removeFromSessionMap( "rva_rv_lstEditarRecibosIdPos");
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
/******************************************************************************************************/
/** Método: Actualizar el valor para la referencia del depósito de cheques y efectivo ( minuta de dep).
 *	Fecha:  16/09/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 **/
	public void actualizarReferenciaArqueo(ActionEvent ev){
		String sNuevaRfr="",sActualRfr="";
		CtrlCajas cc = new CtrlCajas();
		Varqueo varqueo = new Varqueo();
		DetalleArqueoCaja dac = new DetalleArqueoCaja();
		boolean bValido = true;
		String sMensaje="";
		
		Session session = null;
		Transaction transaction = null;
		
		try {
			
			LogCajaService.CreateLog("actualizarReferenciaArqueo", "INFO", "actualizarReferenciaArqueo-INICIO");
			
			//&& ============== Validar que sea un contador o administrador
			Vautoriz vaut = ((Vautoriz[])CodeUtil.getFromSessionMap( "sevAut"))[0];
			boolean autorizado = 
					vaut.getId().getCodper().compareTo("P000000004") == 0 ||
					vaut.getId().getCodper().compareTo("P000000015") == 0 ||
					vaut.getId().getCodper().compareTo("P000000025") == 0 ;

			if(!autorizado){
				dwValidarAprobacionArqueo.setWindowState("normal");
				lblValidarAprobacion.setValue("No tiene autorización para realizar esta operación");
				return;
			}
			
			
			dac = (DetalleArqueoCaja)CodeUtil.getFromSessionMap( "rv_DAC");
			if(CodeUtil.getFromSessionMap( "rva_VARQUEO")!=null){
				varqueo = (Varqueo)CodeUtil.getFromSessionMap( "rva_VARQUEO");
				
				if(varqueo.getId().getEstado().equals("A")){
					bValido = false;
					sMensaje = "El estado del arqueo debe ser 'PENDIENTE' para realizar la operación";
				}else{
					//--- Validaciones para el nuevo valor de la referencia.
					sActualRfr =  varqueo.getId().getReferdep().trim();
					sNuevaRfr = txtCDC_ReferDeposito.getValue().toString();
	
					if(sNuevaRfr.matches("^[0-9]{1,8}$")){
						
						if(sActualRfr.matches("^[0-9]{1,8}$") && 
							Integer.parseInt(sNuevaRfr) == Integer.parseInt(sActualRfr)){
							bValido = false;
							sMensaje = "El nuevo número de referencia debe ser distinto del valor inicial";
						}else{	
							F55ca014 f14 = CompaniaCtrl.obtenerF55ca014(varqueo.getId().getCaid(), varqueo.getId().getCodcomp());
							if(f14 == null){
								sMensaje = "Error al obtener la configuración de banco para caja en validación de número de minuta ";
								dwValidarAprobacionArqueo.setWindowState("normal");
								lblValidarAprobacion.setValue(sMensaje);
								return;
							}
							F55ca022 f22 = new BancoCtrl().obtenerBancoxId(f14.getId().getC4bnc());
							if(f22 == null){
								sMensaje = "F55CA022: No se ha podido obtener la configuración de banco para "+ f14.getId().getC4bnc(); 
								dwValidarAprobacionArqueo.setWindowState("normal");
								lblValidarAprobacion.setValue(sMensaje);
								return;
							}
							if( !(f22.getId().getConciliar() == 1 && f14.getId().isConfrmauto()) ){
								String sResul = cc.verificarReferenciaPago(sNuevaRfr, Integer.parseInt(sNuevaRfr),"ZX",
													f14.getId().getC4bnc(), dac.getCodcomp().trim(), dac.getMoneda(), "");
								if(sResul!=null){
									sMensaje = sResul;
									dwValidarAprobacionArqueo.setWindowState("normal");
									lblValidarAprobacion.setValue(sMensaje);
									return;
								}
							}
						}
					}else{
						bValido = false;
						sMensaje = "El valor de la referencia debe contener entre 1 y 8 dígitos unicamente";
					}
				}
			}else{
				bValido = false;
				sMensaje = "No se ha podido obtener el objeto original de Arqueo desde: 'sessionMap.get(\"rva_VARQUEO\")'";
			}
			//--- Actualizar el valor de la referencia.
			if(bValido){
				
				session = HibernateUtilPruebaCn.currentSession();
				transaction = session.beginTransaction();
						
				Vautoriz vAut[] = (Vautoriz[])CodeUtil.getFromSessionMap( "sevAut");
				RevisionArqueoCtrl rvCtrl = new RevisionArqueoCtrl();
				
				varqueo.getId().setReferdep(sNuevaRfr);
				varqueo.getId().setReferencenumber(Integer.parseInt(sNuevaRfr.trim()));
				
				dac.setDepositoRefer(sNuevaRfr);
				dac.setReferencenumber(Integer.parseInt(sNuevaRfr.trim()));
				
				dac.setDepositoRefer(sNuevaRfr);
				bValido = rvCtrl.actualizarEstadoArqueo( session, varqueo, 3, "", vAut[0].getId().getCodreg() );
				
				transaction.commit();
			
				if(bValido){
					sMensaje = "Se actualizó correctamente referencia: " +sActualRfr+ " hacia: " +sNuevaRfr;
					
					CodeUtil.putInSessionMap("rv_DAC", dac) ;
					CodeUtil.putInSessionMap("rva_VARQUEO", varqueo) ;
					
				}else{
					dac.setDepositoRefer(sActualRfr);
					varqueo.getId().setReferdep(sActualRfr);
					txtCDC_ReferDeposito.setValue(sActualRfr);
					sMensaje = "No se puede realizar operación, ocurrió un error al actualizar la referencia de deposito";
				}
				
			}else{				
				txtCDC_ReferDeposito.setValue(sActualRfr);
			}
			
			dwValidarAprobacionArqueo.setWindowState("normal");
			lblValidarAprobacion.setValue(sMensaje);
			
		} catch (Exception error) {
			LogCajaService.CreateLog("actualizarReferenciaArqueo", "ERR", error.getMessage());
			sMensaje = "No se ha podido realizar operación\n Error de Sistema=> ";
			sMensaje+= error.getMessage();
			dwValidarAprobacionArqueo.setWindowState("normal");
			lblValidarAprobacion.setValue(sMensaje);
			txtCDC_ReferDeposito.setValue(sActualRfr);
			
			try {
				if (transaction != null) {
					transaction.rollback();
				}
			} catch (Exception e) {
				LogCajaService.CreateLog("actualizarReferenciaArqueo", "ERR", e.getMessage());
			}
		} finally {
			LogCajaService.CreateLog("actualizarReferenciaArqueo", "INFO", "actualizarReferenciaArqueo-INICIO");
			HibernateUtilPruebaCn.closeSession(session);
		}		
	}
/*****************************************************************************************/
/** Método: Cargar datos para reimprimir el reporte de resumen de arqueo.
 *	Fecha:  29/07/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 **/
	@SuppressWarnings({ "unchecked", "deprecation" })
	public void reimprimirReporteArqueo(ActionEvent ev){
		ArqueoCajaR rptmcaja002 = new ArqueoCajaR();
		CodeUtil.removeFromSessionMap( "rva_rptmcaja002");
		String sCaja,sCajero,sUbicacion,sNombrecomp;

		FechasUtil f = new FechasUtil();
		String moneda="MONEDA";
		String sFechaHora = "";
		int noarqueo=0;
		
		try {
			Varqueo va = (Varqueo)CodeUtil.getFromSessionMap( "rva_VARQUEO");
			noarqueo 	= va.getId().getNoarqueo();
			sCaja 		= va.getId().getCaname().trim();
			sCajero 	= lblcajero.getValue().toString().trim();
			sUbicacion  = lblsucursal.getValue().toString().trim();
			sNombrecomp = lblCompania.getValue().toString().trim().toUpperCase();

			sCaja = Divisas.ponerCadenaenMayuscula(sCaja); 
			sCajero = Divisas.ponerCadenaenMayuscula(sCajero); 
			sUbicacion = Divisas.ponerCadenaenMayuscula(sUbicacion);
			
			moneda = va.getId().getMoneda().trim();
			if(moneda.equals("COR"))
				moneda = "Córdobas";
			else
			if(moneda.equals("USD"))
				moneda = "Dólares";
			
			sFechaHora += FechasUtil.formatDatetoString(va.getId().getFecha(), "dd/MMMM/yyyy");
			sFechaHora += " "+ FechasUtil.formatDatetoString(va.getId().getHora(), "hh:mm:ss a");
			
			rptmcaja002.setCompania(sNombrecomp);
			rptmcaja002.setNoarqueo(noarqueo);
			rptmcaja002.setFechahora(sFechaHora);
			rptmcaja002.setNocaja(va.getId().getCaid());
			rptmcaja002.setNombrecaja(sCaja);
			rptmcaja002.setNombrecajero(sCajero);
			rptmcaja002.setMoneda(moneda);
			rptmcaja002.setSucursal(sUbicacion);					
			
			rptmcaja002.setVentastotales(Double.parseDouble(lblVentasTotales.getValue().toString()));
			rptmcaja002.setDevoluciones(Double.parseDouble(lblTotalDevoluciones.getValue().toString()));
			rptmcaja002.setVentascredito(Double.parseDouble(lblTotalVtsCredito.getValue().toString()));
			rptmcaja002.setVentasnetas(Double.parseDouble(lblTotalVentasNetas.getValue().toString()));
			rptmcaja002.setAbonos(Double.parseDouble(lblTotalAbonos.getValue().toString()));
			rptmcaja002.setPrimasreservas(Double.parseDouble(lblTotalPrimas.getValue().toString()));
			rptmcaja002.setIngpagosdifmonedas(Double.parseDouble(lblTotalIngRecxmonex.getValue().toString()));
			rptmcaja002.setOtrosingresos(Double.parseDouble(lblTotalIngex.getValue().toString()));
			rptmcaja002.setTotalingresos(Double.parseDouble(lblTotaIngresos.getValue().toString()));

			rptmcaja002.setVentaspagbanco(Double.parseDouble(lblTotalVtsPagBanco.getValue().toString()));
			rptmcaja002.setAbonospagbanco(Double.parseDouble(lblTotalAbonoPagBanco.getValue().toString()));
			rptmcaja002.setPrimaspagbanco(Double.parseDouble(lblTotalPrimasPagBanco.getValue().toString()));
			rptmcaja002.setOtrosegresos(Double.parseDouble(lblTotalOtrosEgresos.getValue().toString()));
			rptmcaja002.setTotalegresos(Double.parseDouble(lblTotalEgresos.getValue().toString()));
				
			
			rptmcaja002.setEfecnetorecibido(Double.parseDouble(lblCDC_efectnetoRec.getValue().toString()));
			rptmcaja002.setMinimoencaja(Double.parseDouble(lblCDC_montominimo.getValue().toString()));
			
			String sReint = lblCDC_montoMinReint.getValue().toString();
			sReint = sReint.replace(",", "");
			BigDecimal bdReint = new BigDecimal(sReint);
			
			if(bdReint.compareTo(BigDecimal.ZERO) == 1){
				BigDecimal bdMinim = new BigDecimal(lblCDC_montominimo.getValue().toString().replace("", ""));
				bdMinim = bdMinim.subtract(bdReint) ;
				rptmcaja002.setMinimoencaja( bdMinim.doubleValue() );
			}
			
			rptmcaja002.setDepositosugerido(Double.parseDouble(lblCDC_depositoSug.getValue().toString()));
			rptmcaja002.setEfecencaja(Double.parseDouble(lblbCDC_efectivoenCaja.getValue().toString()));
			rptmcaja002.setFaltantesobrante(Double.parseDouble(lblCDC_SobranteFaltante.getValue().toString()));
			rptmcaja002.setDepositofinal(Double.parseDouble(lblCDC_depositoFinal.getValue().toString()));
	
			rptmcaja002.setTefectivo(Double.parseDouble(lblACDetfp_Efectivo.getValue().toString()));
			rptmcaja002.setTcheque(Double.parseDouble(lblACDetfp_Cheques.getValue().toString()));
			rptmcaja002.setTtcredito(Double.parseDouble(lblACDetfp_TarCred.getValue().toString()));
			rptmcaja002.setTtcreditom(Double.parseDouble(lblACDetfp_TCmanual.getValue().toString()));
			rptmcaja002.setTtcreditosktpos(Double.parseDouble(lblACDetfp_TCsocketpos.getValue().toString()));
			rptmcaja002.setTdepositobanco(Double.parseDouble(lblACDetfp_DepDbanco.getValue().toString()));
			rptmcaja002.setTtransfbanco(Double.parseDouble(lblACDetfp_TransBanco.getValue().toString()));
			rptmcaja002.setTformaspago(Double.parseDouble(lblACDetfp_Total.getValue().toString()));
			
			//---- Nuevos Campos del reporte.
			rptmcaja002.setSPagoIngresosExt(lblTotalIngex.getValue().toString());
			rptmcaja002.setSPagoFinanciamiento(lblTotalFinan.getValue().toString());
			rptmcaja002.setSCambioOtraMoneda(lblCambioOtraMoneda.getValue().toString());
			rptmcaja002.setSFinanciamientosBanco(lblTotalFinanPagBanco.getValue().toString());
			rptmcaja002.setSIngresosExtBanco(lblTotalIexPagBanco.getValue().toString());
			rptmcaja002.setSReferenciaDeposito(txtCDC_ReferDeposito.getValue().toString());
			rptmcaja002.setSTipoImpresionRpt("Reporte Reimpreso:  " + f.formatDatetoString(new Date(), "dd/MMMM/yyyy  hh:mm:ss a"));
			
			List<ArqueoCajaR> lstAcr = new ArrayList<ArqueoCajaR>();
			lstAcr.add(0,rptmcaja002);
			CodeUtil.removeFromSessionMap( "ac_Lstrptmcaja002");
			CodeUtil.putInSessionMap("ac_Lstrptmcaja002", lstAcr);	
			//meter el arqueo del socket pos 	
			PosCtrl pCtrl = new PosCtrl();
			List<Vcierresktpos> lstCierreSocketPos = null;
			List<ArqueoSocketPos> lstCierreSocketPosId = new ArrayList();
			lstCierreSocketPos = pCtrl.leerCierreSocketPos(va.getId().getCaid(),va.getId().getCodcomp(),va.getId().getMoneda().trim(),va.getId().getNoarqueo());
			if(lstCierreSocketPos != null && !lstCierreSocketPos.isEmpty()){
				for (Iterator iterator = lstCierreSocketPos.iterator(); iterator.hasNext();) {
					Vcierresktpos vcierresktpos = (Vcierresktpos) iterator.next();
					ArqueoSocketPos asp = new ArqueoSocketPos();
					asp.setNocaja( vcierresktpos.getId().getCaid());
					asp.setSucursal(vcierresktpos.getId().getCodsuc());
					asp.setMoneda(vcierresktpos.getId().getMoneda());
					asp.setNoarqueo(vcierresktpos.getId().getNoarqueo());
					asp.setFechaarqueo((java.sql.Date)vcierresktpos.getId().getFecha());
					
					asp.setAfiliado(vcierresktpos.getId().getAfiliado());
					asp.setTermId(vcierresktpos.getId().getTermId());
					asp.setAutorizacion(vcierresktpos.getId().getAutorizacion());
					asp.setPurshtrans(vcierresktpos.getId().getPurshtrans());
					asp.setPurshamount(vcierresktpos.getId().getPurshamount().doubleValue());
					asp.setRettrans(vcierresktpos.getId().getRettrans());
					asp.setRetamount(vcierresktpos.getId().getRetamount().doubleValue());
					
					lstCierreSocketPosId.add(asp);
				}			
			}
			CodeUtil.removeFromSessionMap( "ac_lstCierreSocketPos");
			CodeUtil.putInSessionMap("ac_lstCierreSocketPos", lstCierreSocketPosId);
 
			FacesContext.getCurrentInstance().getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/reportes/rptmcaja002.faces");
		
			CodeUtil.removeFromSessionMap( "rva_VARQUEO");
			CodeUtil.removeFromSessionMap( "rv_DAC");
			
		} catch (Exception error) {
			error.printStackTrace();
		}		
	}

/*****************************************************************************************/
/** Método: mostrar - Cerrar ventana de confirmación de reimprimir rpt de resumen de arqueo
 *	Fecha:  29/07/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 **/
	public void mostrarConfirmarReimpresionRtp(ActionEvent ev){
		try {
			CodeUtil.removeFromSessionMap( "ac_Lstrptmcaja002");
			rv_dwConfirmarReimpresionRpt.setWindowState("normal");
		} catch (Exception error) {
			error.printStackTrace();
		}		
	}
	public void cerrarReimpresionRptArqueo(ActionEvent ev){
		try {
			CodeUtil.removeFromSessionMap( "ac_Lstrptmcaja002");
			rv_dwConfirmarReimpresionRpt.setWindowState("hidden");
		} catch (Exception error) {
			error.printStackTrace();
		}		
	}
	
	@SuppressWarnings("unchecked")
	public boolean registrarComisionRetencionDonacion(List<ResumenAfiliado>lstResumenAfiliadoDnc, 
			List<DncCierreDonacion>cierresDnc, String monedabase, Vautoriz vaut, Date fechadoc, int noarqueo, 
			Session session, String tipocliente, BigDecimal tasaoficial){
		
		boolean hecho = true;
		String msgProceso="";
		String codbnf_aux = ""; 
		String obsDnc = "";
		String tipodocjde = CodigosJDE1.BATCH_CONTADO.codigo() ;
		
		Vf0901 vfBnfCuenta = null;
		DncBeneficiarioCuentas dncBnfCtaCfg ;
		ReciboCtrl rcCtrl = new ReciboCtrl();
		
		//Object[] dtaCnDnc = null;
		
		try {
			
			int caid = cierresDnc.get(0).getCaid();
			
			List<Integer>idsCierresDnc = (ArrayList<Integer>)CodeUtil.selectPropertyListFromEntity(cierresDnc, "idcierredonacion", false);
			String idsIn = idsCierresDnc.toString().replace("[", "(").replace("]", ")");
			
			String strSqlF0901 = " select * from "+ PropertiesSystem.ESQUEMA +
					".Vf0901 where trim(gmmcu) = '@GMMCU' and trim( gmobj ) = '@GMOBJ' " +
					"and trim(gmsub) = '@GMSUB' and trim(gmpec) <> '"+MetodosPagoCtrl.DEPOSITO+"' fetch first rows only ";
			
			String strSqlCtaBnf = "select * from " + PropertiesSystem.ESQUEMA +
				".dnc_beneficiario_cuentas  where idbeneficiario = @BNF_ID " +
				"and codigobnf = @BNF_CODIGO and moneda = '@MONEDA' " +
				"and trim(codcomp) = '@CODCOMP' fetch first rows only ";

			String strSqlCtaformaPago = 
			" select "+ 
			" c1mcu, c1obj, c1sub, gmaid, "+
			" trim(c1mcu)||'.'|| trim(c1obj)||'.'||trim(c1sub), "+
			" gmco "+
			" from @BDMCAJA.f55ca011 inner join @BDMCAJA.vf0901 on " +
			"	 trim(gmmcu) = trim(c1mcu) and trim(gmobj) = trim(c1obj)  and trim(gmsub) = trim(c1sub) "+
			" where c1id = @CAID and trim(c1rp01) = '@CODCOMP' and c1crcd = '@MONEDA' " +
			" and c1ryin ='@MPAGO' and c1stat = 'A' "; 
			
			
			String strQueryTotalxDonador = 
			 " select sum(montorecibido), codigo, idbenficiario " + 
			 " from "+PropertiesSystem.ESQUEMA+".dnc_donacion d where codigopos = @CODIGOPOS and iddonacion in ( "+
				"	select  iddonacion  "+
				"	from "+PropertiesSystem.ESQUEMA+".dnc_cierre_detalle "+
				"	where idcierrednc in " + idsIn +" and  formapago = '"+MetodosPagoCtrl.TARJETA+"' " +
				"	and d.liquidarpormarca = @LIQUIDAPORMARCA   @MARCA_TARJETA_CONDICION ) group by codigo, idbenficiario " ;
			
			String strQueryExecute = "" ;
			
			//=== obtener la cuenta de donacion que se debe afectar.
			for (ResumenAfiliado posDnc : lstResumenAfiliadoDnc) {
				
				strQueryExecute = strQueryTotalxDonador.replace("@CODIGOPOS", posDnc.getCodigo() )
							.replace("@LIQUIDAPORMARCA", String.valueOf( posDnc.getLiquidarpormarca() ) )
							.replace("@MARCA_TARJETA_CONDICION", 
								posDnc.getLiquidarpormarca() == 1 ?   
								" and d.codigomarcatarjeta = '" + String.valueOf( posDnc.getCodigomarcatarjeta() ) + "'" : ""  ); 
				
				
				List<Object[]>dtaTotalesDncxBnf = (ArrayList<Object[]>) session.createSQLQuery(strQueryExecute).list();
				//List<Object[]>dtaTotalesDncxBnf = (ArrayList<Object[]>) ConsolidadoDepositosBcoCtrl.executeSqlQuery( strQueryExecute, true, null );
				
				for (Object[] dtaBnf : dtaTotalesDncxBnf) {
					
					BigDecimal montototal = (BigDecimal)dtaBnf[0];
					BigDecimal comisiondnc  = montototal.multiply(posDnc.getPorcentajecomision().divide(new BigDecimal("100")));
					BigDecimal retenciondnc = montototal.subtract(comisiondnc).multiply( posDnc.getPorcentajeretencion().divide(new BigDecimal("100")));
					BigDecimal montoneto = montototal.subtract(comisiondnc).subtract(retenciondnc);
					
					BigDecimal mtoBrutoTmp = new BigDecimal(String.format("%1$.2f", montototal));
					BigDecimal mtoNetoTmp  = new BigDecimal(String.format("%1$.2f", montoneto));
					BigDecimal mtoRetencionTmp = new BigDecimal(String.format("%1$.2f", retenciondnc));
					BigDecimal mtoComisionTmp = new BigDecimal(String.format("%1$.2f", comisiondnc));
					
					BigDecimal ajuste = mtoBrutoTmp.subtract( mtoNetoTmp.add(mtoRetencionTmp).add(mtoComisionTmp) );
							
					if( ajuste.abs().compareTo(BigDecimal.ZERO) == 1 ){
						retenciondnc = retenciondnc.add(ajuste);
						ajuste = ajuste.multiply(new BigDecimal("-1"));
					}
					
					//&& ======================== cuenta del beneficiario.
					
					strQueryExecute = 
							strSqlCtaBnf.replace("@BNF_ID", String.valueOf( dtaBnf[2] ) )
							.replace("@BNF_CODIGO", String.valueOf( dtaBnf[1] ) )
							.replace("@MONEDA", posDnc.getMoneda())
							.replace("@CODCOMP",cierresDnc.get(0).getCodcomp()  );
					
					List<DncBeneficiarioCuentas> bnf_cta = (ArrayList<DncBeneficiarioCuentas>)session.createSQLQuery(strQueryExecute).addEntity(DncBeneficiarioCuentas.class).list();
					//List<DncBeneficiarioCuentas> bnf_cta = (ArrayList<DncBeneficiarioCuentas>)ConsolidadoDepositosBcoCtrl.executeSqlQuery( strQueryExecute, true, DncBeneficiarioCuentas.class );
					
					dncBnfCtaCfg = bnf_cta.get(0);
					
					strQueryExecute = strSqlF0901.replace("@GMMCU", dncBnfCtaCfg.getUnegocio().trim() )
								.replace("@GMOBJ",dncBnfCtaCfg.getObjeto().trim() )
								.replace("@GMSUB",dncBnfCtaCfg.getSubsidiaria().trim() ) ;
			
					List<Vf0901>bnf_CuentasF0901 = ( ArrayList<Vf0901>) session.createSQLQuery(strQueryExecute).addEntity(Vf0901.class).list();
					//List<Vf0901>bnf_CuentasF0901 = ( ArrayList<Vf0901>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(strQueryExecute, true, Vf0901.class );
					
					if(bnf_CuentasF0901 == null || bnf_CuentasF0901.isEmpty()){
						msgProceso = "No se encontro en JDE cuenta contable para la configuracion de cuenta para beneficiario codigo " + String.valueOf(dtaBnf[1]) ;
						return false;
					}
					
					codbnf_aux = String.format("%08d", Integer.parseInt( String.valueOf(dtaBnf[1]) ) ) ;
					
					vfBnfCuenta = bnf_CuentasF0901.get(0);
					int lineadoc = 0;
					
					String codsucdoc = vfBnfCuenta.getId().getGmco().trim();
					String ctaBnfMcu = vfBnfCuenta.getId().getGmmcu().trim();
					String ctaBnfObj = vfBnfCuenta.getId().getGmobj().trim();
					String ctaBnfSub = vfBnfCuenta.getId().getGmsub();
					String ctaBnfGmaid  = vfBnfCuenta.getId().getGmaid() ;
					String ctaBnfcuenta = ctaBnfMcu+"."+ctaBnfObj +  ( (ctaBnfSub == null || ctaBnfSub.isEmpty())?"":"."+ctaBnfSub.trim() );
					 
					//&& ============================= Cuenta del metodo de pago
					strQueryExecute = 
							strSqlCtaformaPago
							.replace("@BDMCAJA", PropertiesSystem.ESQUEMA )
							.replace("@MONEDA", posDnc.getMoneda())
							.replace("@CODCOMP", cierresDnc.get(0).getCodcomp().trim() )
							.replace("@MPAGO", MetodosPagoCtrl.TARJETA) 
							.replace("@CAID", Integer.toString( cierresDnc.get(0).getCaid() ) ) ;
					
					List<Object[]> lstDtaCtaMpago  = (ArrayList<Object[]> )session.createSQLQuery(strQueryExecute).list();
					//List<Object[]> lstDtaCtaMpago  = (ArrayList<Object[]> )ConsolidadoDepositosBcoCtrl.executeSqlQuery( strQueryExecute, true, null );
					
					if(lstDtaCtaMpago == null || lstDtaCtaMpago.isEmpty() ){
						msgProceso = "No se encontró la cuenta del método de pago para la donación ";
						return false;
					}
					
					Object[] ctaMpago = lstDtaCtaMpago.get(0);
					String mpagoMcu = String.valueOf( ctaMpago[0] ).trim() ;
					String mpagoObj = String.valueOf( ctaMpago[1] ).trim() ;
					String mpagoSub = String.valueOf( ctaMpago[2] ).trim() ;
					String mpagoGmaid  = String.valueOf( ctaMpago[3] ).trim() ;
					String mpagocuenta = String.valueOf(ctaMpago[4]).trim() ;
					String mpagoSucursal = String.valueOf(ctaMpago[5]).trim() ;
					String observacion = "Comision Donacion C:"+caid +" A:"+noarqueo;
					
					// && ================================================================================ &&//
					// && 					Asientos de diario por comisiones.
					// && ================================================================================ &&//
					
					// && ====================== Asientos de diario por comisiones.
					int numerobatch = Divisas.numeroSiguienteJdeE1(   );
					int numeroDocumento =  Divisas.numeroSiguienteJdeE1Custom(valoresJdeNumeracion[8],valoresJdeNumeracion[9] );//Divisas.numeroSiguienteJdeE1(CodigosJDE1.NUMERO_DOC_CONTAB_GENERAL );
					int[] iNobatchNodoc = new int[]{numerobatch, numeroDocumento};
					
					int montoentero = Integer.parseInt( String.format("%1$.2f", comisiondnc).replace(".", "")  ); 
					
					int montoAA = montoentero;
					int montoCA = montoentero;
					int montoCA1 = 0;
					String monedaAA = posDnc.getMoneda();
					String monedaCA = posDnc.getMoneda();
					String tipoDocxMon = "D" ;
					tipodocjde = cajaparm.getParametros("34", "0", "TIPODOC_COMIDONACION").getValorAlfanumerico().toString();
					
					BigDecimal tasacambio = BigDecimal.ONE;
					
					if(posDnc.getMoneda().compareTo(monedabase) != 0 ){
						tasacambio = tasaoficial;
						tipoDocxMon= "F";
						montoCA1 = montoCA ;
					}
					
					montoAA = Integer.parseInt( String.format("%1$.2f", comisiondnc.multiply(tasacambio) ).replace(".", "")  ); 
					
					if(posDnc.getMoneda().compareTo(monedabase) == 0 ){
						tasacambio =  BigDecimal.ZERO;
					}
					
					String logs = "" ;
					
					//&& =========================== si el monto de la comision es demasiado pequenio
					if( montoCA <= 0 ) {
						
						//&& =========================== Grabar el encabezado del batch
						hecho = rcCtrl.registrarBatchA92Session( session, fechadoc, valoresJdeInsContado[8], iNobatchNodoc[0], montoCA, vaut.getId().getLogin(), 1, "APRARQUEO",  valoresJdeInsContado[9]);
						
						if(!hecho) {
							msgProceso ="No se pudo grabar Comprobante por Monto de Comision a donaciones TC (encabezado de batch) " ;
							return false;
						}
						
						//&& =========================== linea 1 cargo a cuenta del beneficiario 
						
						//obsDnc = "Total Donacion: "+ String.format("%1$.2f", montototal);
						obsDnc = "Mto: "+ String.format("%1$.2f", montototal) + " " + posDnc.getCodigo() + " " + String.format("%1$.2f", posDnc.getPorcentajecomision() ) +"%";
						logs = observacion;
						
						hecho = rcCtrl.registrarAsientoDiarioLogsSession(session, logs,  fechadoc, codsucdoc, tipodocjde, iNobatchNodoc[1], 
								(++lineadoc), iNobatchNodoc[0], ctaBnfcuenta, ctaBnfGmaid, ctaBnfMcu, ctaBnfObj, ctaBnfSub, 
								"AA", monedaAA,   montoAA , observacion, vaut.getId().getLogin(),
								vaut.getId().getCodapp(), tasacambio, tipocliente, obsDnc, 
								codsucdoc, codbnf_aux, "A", monedabase, codsucdoc, tipoDocxMon, montoCA1);
						
						if(!hecho) {
							msgProceso =" No se pudo grabar Comprobante por Monto de Comision a donaciones TC (detalle de batch) " ;
							return false;
						}
						
						if(posDnc.getMoneda().compareTo(monedabase) != 0 ){
							hecho = rcCtrl.registrarAsientoDiarioLogsSession(session, logs, fechadoc, codsucdoc, tipodocjde,  iNobatchNodoc[1], 
									(lineadoc), iNobatchNodoc[0], ctaBnfcuenta, ctaBnfGmaid, ctaBnfMcu, ctaBnfObj, ctaBnfSub, 
									"CA", monedaCA,  montoCA ,  observacion , vaut.getId().getLogin(),
									vaut.getId().getCodapp(), tasacambio, tipocliente, obsDnc,								
									codsucdoc, codbnf_aux, "A", monedabase, codsucdoc, tipoDocxMon, 0);
						}
						
						if(!hecho) {
							msgProceso =" No se pudo grabar Comprobante por Monto de Comision a donaciones TC (detalle de batch) " ;
							return false;
						}
						//&& =========================== linea 2 contracargo a cuenta de caja del metodo de pago
						
						//obsDnc = "POS: "+posDnc.getCodigo() +" Coms:"+ posDnc.getPorcentajecomision() +"%" ;
						obsDnc = "Liq.Marca: " + posDnc.getMarcatarjeta() ;
						
						hecho = rcCtrl.registrarAsientoDiarioLogsSession(session, logs, fechadoc, codsucdoc, tipodocjde, iNobatchNodoc[1], 
								(++lineadoc), iNobatchNodoc[0], mpagocuenta, mpagoGmaid, mpagoMcu, mpagoObj, mpagoSub, 
								"AA", monedaAA, ( montoAA * -1 ), observacion, vaut.getId().getLogin(),
								vaut.getId().getCodapp(), tasacambio, tipocliente, obsDnc, 
								mpagoSucursal, "", "", monedabase, codsucdoc, tipoDocxMon, ( montoCA1 * -1 ) );
	
						
						if(!hecho) {
							msgProceso =" no se pudo grabar cargo a cuenta de  caja en jde " ;
							return false;
						}
						
						if( posDnc.getMoneda().compareTo(monedabase) != 0 ){
							hecho = rcCtrl.registrarAsientoDiarioLogsSession(session, logs, fechadoc, codsucdoc, tipodocjde, iNobatchNodoc[1], 
									(lineadoc), iNobatchNodoc[0], mpagocuenta, mpagoGmaid, mpagoMcu, mpagoObj, mpagoSub, 
									"CA", monedaCA, ( montoCA * -1 ), observacion, vaut.getId().getLogin(),
									vaut.getId().getCodapp(), tasacambio, tipocliente, obsDnc,
									mpagoSucursal, "", "", monedabase, codsucdoc, tipoDocxMon, 0);
						}
						if(!hecho) {
							msgProceso =" no se pudo grabar cargo a cuenta de  caja en jde " ;
							return false;
						}
					}
					
					// && ================================================================================ &&//
					// && 					Asientos de diario por Retenciones.
					// && ================================================================================ &&//
					
					lineadoc = 0;
					
					numerobatch = Divisas.numeroSiguienteJdeE1(   );
					numeroDocumento =  Divisas.numeroSiguienteJdeE1Custom(valoresJdeNumeracion[8],valoresJdeNumeracion[9] );
					iNobatchNodoc = new int[]{numerobatch, numeroDocumento};
					
					montoentero = Integer.parseInt( String.format("%1$.2f", retenciondnc).replace(".", "")  ); 
					
					montoCA1 = 0;
					montoAA = montoentero;
					montoCA = montoentero;
					tasacambio = BigDecimal.ONE;
					tipodocjde = cajaparm.getParametros("34", "0", "TIPODOC_RETEDONACION").getValorAlfanumerico().toString();
					observacion = "Retención Donación C:"+caid +" A:"+noarqueo;
					logs = observacion;
					
					if(posDnc.getMoneda().compareTo(monedabase) != 0 ){
						tasacambio = tasaoficial;
						tipoDocxMon= "F";
						montoCA1 = montoCA ;
					}
					
					montoAA = Integer.parseInt( String.format("%1$.2f", retenciondnc.multiply(tasacambio) ).replace(".", "")  );
					if(ajuste.compareTo(BigDecimal.ZERO) != 0){
						montoAA = Integer.parseInt( String.format("%1$.2f", retenciondnc.add(ajuste).multiply(tasacambio) ).replace(".", "")  );
					}
					
					if(posDnc.getMoneda().compareTo(monedabase) == 0 ){
						tasacambio =  BigDecimal.ZERO;
					}
					
					//&& =========================== si el monto de la comision es demasiado pequenio
					if( montoCA <= 0 ) {
						
						//&& =========================== Grabar el encabezado del batch
						hecho = rcCtrl.registrarBatchA92Session( session, fechadoc,  valoresJdeInsContado[8], iNobatchNodoc[0], montoCA, vaut.getId().getLogin(), 1, "APRARQUEO", valoresJdeInsContado[9]);
						if(!hecho) {
							msgProceso ="No se pudo grabar Comprobante por Monto de Retención a donaciones TC (encabezado de batch) " ;
							return false;
						}
						
						//&& =========================== linea 1 cargo a cuenta del beneficiario 
						
						obsDnc = "Mto: "+ String.format("%1$.2f", montototal) + " " + posDnc.getCodigo() + " " + String.format("%1$.2f", posDnc.getPorcentajeretencion() ) +"%";
						
						hecho = rcCtrl.registrarAsientoDiarioLogsSession(session, logs, fechadoc, codsucdoc, tipodocjde, iNobatchNodoc[1], 
								(++lineadoc), iNobatchNodoc[0], ctaBnfcuenta, ctaBnfGmaid, ctaBnfMcu, ctaBnfObj, ctaBnfSub, 
								"AA", monedaAA, montoAA   , observacion, vaut.getId().getLogin(),
								vaut.getId().getCodapp(), tasacambio, tipocliente, obsDnc,
								codsucdoc, codbnf_aux, "A", monedabase, codsucdoc, tipoDocxMon, montoCA1);
								
						
						if(!hecho) {
							msgProceso = "No se pudo grabar Comprobante por Monto de Retención a donaciones TC (detalle de batch) " ;
							return false;
						}
						
						if(posDnc.getMoneda().compareTo(monedabase) != 0 ){
							hecho = rcCtrl.registrarAsientoDiarioLogsSession(session, logs, fechadoc, codsucdoc, tipodocjde,  iNobatchNodoc[1], 
									(lineadoc),  iNobatchNodoc[0], ctaBnfcuenta, ctaBnfGmaid, ctaBnfMcu, ctaBnfObj, ctaBnfSub, 
									"CA", monedaCA,  montoCA,  observacion , vaut.getId().getLogin(),
									vaut.getId().getCodapp(), tasacambio, tipocliente, obsDnc,
									codsucdoc, codbnf_aux, "A", monedabase, codsucdoc, tipoDocxMon, 0);
	
						}
						
						if(!hecho) {
							msgProceso =" No se pudo grabar Comprobante por Monto de Retención a donaciones TC (detalle de batch) " ;
							return false;
						}
						//&& =========================== linea 2 contracargo a cuenta de caja del metodo de pago
					
						obsDnc = "Liq.Marca: " + posDnc.getMarcatarjeta() ;
						
						hecho = rcCtrl.registrarAsientoDiarioLogsSession(session, logs, fechadoc, codsucdoc, tipodocjde, iNobatchNodoc[1], 
								(++lineadoc), iNobatchNodoc[0], mpagocuenta, mpagoGmaid, mpagoMcu, mpagoObj, mpagoSub, 
								"AA", monedaAA, ( montoAA * -1 ), observacion, vaut.getId().getLogin(),
								vaut.getId().getCodapp(), tasacambio, tipocliente, obsDnc, 
								mpagoSucursal, "", "", monedabase, codsucdoc, tipoDocxMon, ( montoCA1 * -1 ));
	
						
						if(!hecho) {
							msgProceso =" no se pudo grabar cargo por Retención de donaciones a cuenta de caja en jde " ;
							return false;
						}
						
						if( posDnc.getMoneda().compareTo(monedabase) != 0 ){
							hecho = rcCtrl.registrarAsientoDiarioLogsSession(session, logs, fechadoc, codsucdoc, tipodocjde, iNobatchNodoc[1], 
									(lineadoc), iNobatchNodoc[0], mpagocuenta, mpagoGmaid, mpagoMcu, mpagoObj, mpagoSub, 
									"CA", monedaCA, ( montoCA * -1 ), observacion, vaut.getId().getLogin(),
									vaut.getId().getCodapp(), tasacambio, tipocliente, observacion,
									mpagoSucursal, "", "", monedabase, codsucdoc, tipoDocxMon, 0);
						}
						if(!hecho) {
							msgProceso =" no se pudo grabar cargo por Retención de donaciones a cuenta de  caja en jde " ;
							return false;
						}
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace(); 
			hecho = false;
			msgProceso = "Error al procesar comprobantes para donaciones recibidas" ;
		}finally{
			
			if(!hecho || !msgProceso.isEmpty()){
				CodeUtil.putInSessionMap("sMensajeError", msgProceso);
			}
			
		}
		return hecho;
	}
	
/***************************************************************************************************************************************************/
	@SuppressWarnings("unchecked")
	public boolean registrarRetencionAfiliados(int iNoarqueo,List lstResumenPos,int iCaid,String sCodcomp,
												String sCodsuc,	Date dtFecha, Date dtHoraArqueo, String sMoneda,
												F55ca014 f14, Session session, String tipoEmpleado, BigDecimal tasaCambioOficial ){
		boolean bHecho = true;
		int iNoBatch = 0,iMontoH,iNoDocumento = 0,iCajaUso=0;
		String sCuentaC[], sSucursalDeposito ="",  sConcepto,sFecha,sMensaje="",sCuentaRet = "";
		String sCuentaTemp[] = null;
		
		Vautoriz vaut;
		Vf55ca01 f55ca01;
		Divisas dv = new Divisas();
		ReciboCtrl recCtrl = new ReciboCtrl();
		Vf0901 vf0901 = null;
		List<Ctaxdeposito> lstCtasxDeps = null;
		Ctaxdeposito ctaxDeps = null;
		
		String strDscripL1  = "";
		String strDscripL2  = "";
		
		try {
			sObjRetencion = cajaparm.getParametros("34", "0", "CIERRE_RETE_OBJ").getCodigoCuentaObjeto().toString();
			sSubRetencion = cajaparm.getParametros("34", "0", "CIERRE_RETE_SUB").getCodigoSubCuenta().toString();
			sObjIVAcomision = cajaparm.getParametros("34", "0", "CIERRE_RETE_OBJ").getCodigoCuentaObjeto().toString();
			sSubIVAcomision = cajaparm.getParametros("34", "0", "CIERRE_RETE_SUBIVA").getCodigoSubCuenta().toString();
			
			//--- Datos de la caja.
			f55ca01 = (Vf55ca01)((List)CodeUtil.getFromSessionMap( "lstCajas")).get(0);
			vaut = ((Vautoriz[]) CodeUtil.getFromSessionMap( "sevAut"))[0];
			sFecha = FechasUtil.formatDatetoString(dtFecha, "dd/MM/yyyy");
			
			String monedaBaseCompania = f14.getId().getC4bcrcd() ;
			 
			sCuentaC = null;
			sCuentaTemp = null;
			for (int i = 0; i < lstResumenPos.size(); i++) {
			
				ResumenAfiliado ra = (ResumenAfiliado)lstResumenPos.get(i);
				
				if(ra.getMontoRetencion().compareTo(BigDecimal.ZERO) <= 0 )
					continue;
				
				if(ra.getIcaidpos()== iCaid){
					if(sCuentaTemp == null){
						sCuentaTemp = dv.obtenerCuentaCaja(iCaid, sCodcomp, MetodosPagoCtrl.TARJETA, sMoneda, session, null, null, null ) ; 
					}
					iCajaUso = iCaid;
					sCuentaC = sCuentaTemp;
				}else{
					iCajaUso = ra.getIcaidpos();
					sCuentaC = dv.obtenerCuentaCaja(ra.getIcaidpos(), sCodcomp, MetodosPagoCtrl.TARJETA, sMoneda, session, null, null, null ) ; 
				}
				 
				if(sCuentaC == null){
					sMensaje = "No se ha podido leer la cuenta de Caja para el afiliado: "
								+ra.getCodigo() + " de configuración de caja origen: "
								+ra.getIcaidpos() +" intente nuevamente";
					CodeUtil.putInSessionMap("sMensajeError", sMensaje);
					return false;
				}else{
					
					sConcepto = "RtncionTC ARQ: "+iNoarqueo+", C:" + iCajaUso ;
					
					/*
					 * La empresa E12 funciona con cuentas diferentes hay que cambiarlas antes de hacer la validacion
					 * */
					if (sCodcomp.trim().toUpperCase().equals("31")) {
						sObjRetencion = cajaparm.getParametros("34", "0", "CIERRE_RETE_OBJ2").getCodigoCuentaObjeto().toString() ;
						sSubRetencion = "";
					}
					
					vf0901 = dv.validarCuentaF0901(sCuentaC[3], sObjRetencion, sSubRetencion);
					if(vf0901==null){
						bHecho = false;
						sMensaje = "No se ha podido leer la cuenta de Retencion "
									+ sObjRetencion+"."+sSubRetencion +" para la unidad de negocios: "
									+ sCuentaC[3] + " Revisar la configuracion de cuentas de JDE";
						CodeUtil.putInSessionMap("sMensajeError", sMensaje);
						return false;
					}else{	
						
						sCuentaRet = CodeUtil.pad( vf0901.getId().getGmmcu().trim(), 12, " " ) + "." + sObjRetencion;
						if( !sSubRetencion.trim().isEmpty() ){
							sCuentaRet = sCuentaRet + "." + sSubRetencion;
						}
						
						vf0901.setCuenta(sCuentaRet);
						sSucursalDeposito = sCuentaC[2];
						
					}
				}
				
				String logs = sConcepto;
				
				iMontoH = Divisas.pasarAentero((Divisas.roundDouble(ra.getMontoRetencion().doubleValue())));
				iNoBatch = Divisas.numeroSiguienteJdeE1(   );
				
				if(iNoBatch == -1){
					bHecho = false;
					sMensaje = "Error al generar número de Batch para retención de afiliado "+ra.getCodigo()+" intente nuevamente";
					CodeUtil.putInSessionMap("sMensajeError", sMensaje);
					return false;
				}
				
				if(!sMoneda.equals( monedaBaseCompania  )){
					iMontoH = dv.pasarAentero((dv.roundDouble(ra.getMontoRetencion().doubleValue() * tasaCambioOficial.doubleValue() )));							
				} 
				
				bHecho = recCtrl.registrarBatchA92Session( session, dtFecha,  valoresJdeInsContado[8], iNoBatch, iMontoH, vaut.getId().getLogin(), 1, "APRARQUEO",  valoresJdeInsContado[9]);
				
				if(bHecho){
					iNoDocumento = Divisas.numeroSiguienteJdeE1Custom(valoresJdeNumeracion[8],valoresJdeNumeracion[9] );							
					if(iNoDocumento == -1){
						sMensaje = "Error al generar número de documento para retención de afiliado "
								+ra.getCodigo()+" intente nuevamente";
						CodeUtil.putInSessionMap("sMensajeError", sMensaje);
						return false;
						
					}
					
					strDscripL1  = "POS: "+ ra.getCodigo() +" Rtnción: " + String.format("%1$,.2f", f14.getId().getC4trir() )+ "%" ;
					strDscripL2  = "LIQ: " + ra.getReferencia() + " " + ra.getMarcatarjeta().trim();
					
				
					bHecho  = recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sSucursalDeposito,"P9", iNoDocumento,1.0, iNoBatch,vf0901.getCuenta(),
												vf0901.getId().getGmaid(),vf0901.getId().getGmmcu().trim(),
												vf0901.getId().getGmobj(), vf0901.getId().getGmsub(),"AA", 
												f14.getId().getC4bcrcd(), iMontoH,  sConcepto, vaut.getId().getLogin(),
												vaut.getId().getCodapp(), BigDecimal.ZERO, tipoEmpleado,
												strDscripL1,
												vf0901.getId().getGmco().trim(), "","", monedaBaseCompania,
												vf0901.getId().getGmco().trim(), "D", 0);
					if(bHecho){
						bHecho  = recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sSucursalDeposito,"P9",iNoDocumento,2.0,iNoBatch,sCuentaC[0],
																sCuentaC[1],sCuentaC[3],sCuentaC[4],sCuentaC[5],"AA", f14.getId().getC4bcrcd(), 
																iMontoH*(-1), sConcepto, vaut.getId().getLogin(),vaut.getId().getCodapp(),
																BigDecimal.ZERO, tipoEmpleado, strDscripL2, sCuentaC[2],
																"","", monedaBaseCompania,
																vf0901.getId().getGmco().trim(), "D", 0);
						if(bHecho){
							
							List<Object[]> lstDeps = new ArrayList<Object[]>();
							if (CodeUtil.getFromSessionMap("rv_listaDepositos") != null)
								lstDeps = (ArrayList<Object[]>)CodeUtil.getFromSessionMap("rv_listaDepositos");
							
							Object oDep[] = new Object[20];
							oDep[0]= iCajaUso;
							oDep[1]= sCodsuc;
							oDep[2]= sCodcomp;
							oDep[3]= iNoBatch;
							oDep[4]= iNoDocumento;
							oDep[5]= iNoarqueo;
							oDep[6]= sMoneda;
							oDep[7]= new BigDecimal(ra.getMontoRetencion().doubleValue());
							oDep[8]= dtFecha;
							oDep[9]= dtHoraArqueo;
							oDep[10]= vaut.getId().getLogin();
							oDep[11]= ra.getReferencia();
							oDep[12]= "R";
							oDep[14]= MetodosPagoCtrl.TARJETA;
							oDep[15]= tasaCambioOficial;
							
							lstCtasxDeps = new ArrayList<Ctaxdeposito>();
							ctaxDeps = new Ctaxdeposito();
							ctaxDeps.setMonto(ra.getMontoRetencion());
							ctaxDeps.setTipomov("C");
							ctaxDeps.setIdcuenta(vf0901.getId().getGmaid());
							ctaxDeps.setGmmcu(vf0901.getId().getGmmcu().trim());
							ctaxDeps.setGmobj(vf0901.getId().getGmobj());
							ctaxDeps.setGmsub(vf0901.getId().getGmsub());
							lstCtasxDeps.add(ctaxDeps);
							
							//&& ========== Objeto para debito, caja.
							ctaxDeps = new Ctaxdeposito();
							ctaxDeps.setMonto(ra.getMontoRetencion());
							ctaxDeps.setTipomov("D");
							ctaxDeps.setIdcuenta(sCuentaC[1]);
							ctaxDeps.setGmmcu(sCuentaC[3]);
							ctaxDeps.setGmobj(sCuentaC[4]);
							ctaxDeps.setGmsub(sCuentaC[5]);
							lstCtasxDeps.add(ctaxDeps);
							oDep[16] = lstCtasxDeps;
							oDep[17] = ra.getCodigo();
							oDep[18] = Integer.parseInt(ra.getReferencia());
							oDep[19] = 0;
							
							lstDeps.add(oDep);
							CodeUtil.putInSessionMap( "rv_listaDepositos", lstDeps );
							

						}else{
							sMensaje = "Batch no registrado por retención de afiliado: "
									+ra.getCodigo() + " "+recCtrl.getError()
										.toString().split("@")[1];
							CodeUtil.putInSessionMap("sMensajeError", sMensaje);
							return false;
						}
					}else{
						sMensaje = "Batch no registrado por retención de afiliado: "
								+ra.getCodigo() + " "+recCtrl.getError()
									.toString().split("@")[1];
						CodeUtil.putInSessionMap("sMensajeError", sMensaje);
						return false;
					}
					//
				}else{
					sMensaje = "Batch no registrado por retención de afiliado: "
							+ra.getCodigo() + " "+recCtrl.getError()
								.toString().split("@")[1];
					CodeUtil.putInSessionMap("sMensajeError", sMensaje);
					return false;
				}
				
				//--- Detener la ejecución del ciclo en caso de errores.
				if(!bHecho)
				break;
			
			}//--fin de for de lista
			
			
		} catch (Exception error) {
			
			bHecho = false;
			sMensaje = "Depósitos por retención de afiliados no pudo ser generado en JDE, favor intente nuevamente";
			
			error.printStackTrace(); 
		}
		
		
		//-----Poner bandera de mensaje de error
		if(!bHecho){
			CodeUtil.putInSessionMap( "sMensajeError", sMensaje );
		}
		
		return bHecho;
	}

/****************************************************************************************/
/** Método: Filtra los Arqueo para la opción de búsqueda por fecha.
 *	Fecha:  18/05/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 **/	
	public void filtrarArqueoxfecha(ActionEvent ev){
		String sMensaje = "";
		boolean bValido = true;
		try {			
			
			ValueChangeEvent vcEvent = null;
			filtrarArqueos(vcEvent);

		} catch (Exception error) {
			error.printStackTrace();
		}
	}
/****************************************************************************************/
/** Método: Registra los registro de depósitos en banco por Afiliados y su monto neto.
 *	Fecha:  18/05/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 **/@SuppressWarnings("unchecked")
 	public boolean registrarDepositosAfiliados(int iNoarqueo,List<ResumenAfiliado> lstResumenPos,int iCaid, 
								String sCodcomp,String sCodsuc, Date dtFecha,Date dtHora, String sMoneda, 
								String sCtaBcoTransitoria[],F55ca014 f14, Varqueo arqueo, Session session, 
								String tipoCliente, BigDecimal tasaCambioOficial ){
		
	 
	 	boolean bHecho = true;
		int iNoBatch = 0,iNorefer=0, iCajaUso=0;
		String sCuentaC[], sSucursalDeposito ="", sConcepto,sFecha, sMensaje="";
		String sCuentaT[], sCuentaBco[], sDescripcion,sCodigoAuxiliar,sTipoAuxiliar,sTipodoc;
		String sCuentaTemp[];
		String sCuentaB[];
		
		Vautoriz vaut;
		Divisas dv = new Divisas();
		ReciboCtrl recCtrl = new ReciboCtrl();
		List<Ctaxdeposito> lstCtasxDeps = null;
		Ctaxdeposito ctaxDeps = null;
		ClsParametroCaja cajaparm = new ClsParametroCaja();
		
		long iMontoH = 0 ;
		long iMontoCorH = 0 ;
		
		try {
			
			String monedaBaseCompania = f14.getId().getC4bcrcd() ;
			
			vaut =  ( (Vautoriz[]) CodeUtil.getFromSessionMap( "sevAut") ) [0];
			sFecha = FechasUtil.formatDatetoString(dtFecha, "dd/MM/yyyy");
			sConcepto = "Arqueo "+iNoarqueo+", Caja "+iCaid+ " "+ sFecha;
			String logs = sConcepto;
			
			
			//--- Obtener la cuenta de funcionarios y empleados, crear el objeto Cuenta[];
			String sUN="10";
			if( sCodcomp.trim().toUpperCase().compareTo("E02") == 0 )
				sUN = cajaparm.getParametros("34", "0", "DEUDO_VAR_UNE02").getValorAlfanumerico().toString();//PropertiesSystem.CTA_DEUDORES_VARIOS_UNE02 ;
			if( sCodcomp.trim().toUpperCase().compareTo("E03") == 0 )
				sUN = cajaparm.getParametros("34", "0", "DEUDO_VAR_UNE03").getValorAlfanumerico().toString();//PropertiesSystem.CTA_DEUDORES_VARIOS_UNE03 ;
			if( sCodcomp.trim().trim().toUpperCase().compareTo("E08") == 0 )
				sUN = cajaparm.getParametros("34", "0", "DEUDO_VAR_UNE08").getValorAlfanumerico().toString();//PropertiesSystem.CTA_DEUDORES_VARIOS_UNE08 ;
			if( sCodcomp.trim().trim().toUpperCase().compareTo("E10") == 0 )
				sUN = cajaparm.getParametros("34", "0", "DEUDO_VAR_UNE01").getValorAlfanumerico().toString();//PropertiesSystem.CTA_DEUDORES_VARIOS_UNE01 ;
			
			Vf0901 vCtaFE  = dv.validarCuentaF0901(sUN, cajaparm.getParametros("34", "0", "CTA_DEUDO_VAR_OB").getValorAlfanumerico().toString(),"");
			
			if(vCtaFE==null){
				sCuentaT = null;
			}else{
				sCuentaT = new String[6];
				sCuentaT[0] = sUN + "." + cajaparm.getParametros("34", "0", "CTA_DEUDO_VAR_OB").getValorAlfanumerico().toString();
				sCuentaT[1] = vCtaFE.getId().getGmaid();
				sCuentaT[2] = sUN;
				sCuentaT[3] = vCtaFE.getId().getGmmcu().trim();
				sCuentaT[4] = vCtaFE.getId().getGmobj().trim();
				sCuentaT[5] = vCtaFE.getId().getGmsub().trim();
			}
			
			//***************************************************************************//
			Set<Integer> codigosBanco = new HashSet<Integer>();
			
			//&& ===== lista unica de codigos de banco
			for (ResumenAfiliado ra : lstResumenPos) {
				codigosBanco.add(Integer.valueOf( ra.getCodigobancopos() ) )  ;
			}
			
			//&& ======  crear lista de cuentas transitorias por banco
			List<String[]> cuentasTransitoriasBanco = new ArrayList<String[]>();
			for (Integer codigo : codigosBanco) {
				
				String[] cuentaTransitoria  = dv.obtenerCuentaTransitoBanco(sMoneda, sCodcomp, codigo, session);
				if(cuentaTransitoria == null )
					continue;
				
				cuentasTransitoriasBanco.add(cuentaTransitoria);
			}
			//***************************************************************************//			
			
			sCuentaC = null;
			sCuentaTemp = null;
			int iCodigoBanco = 100001; 
			
			for (ResumenAfiliado ra : lstResumenPos) {
				
				//&& ======== Determinar el banco del afiliado y luego validar si este debe conciliarse o no.
				F55ca03 f03 =  AfiliadoCtrl.obtenerAfiliadoxId(ra.getCodigo(), sCodcomp.trim(), session );
				if(f03 == null){
					CodeUtil.putInSessionMap("sMensajeError", "F55CA03: No se encontró configuración para afiliado "+ra.getCodigo());
					return bHecho = false;
				}
				 
				F55ca022 f22 = BancoCtrl.obtenerBancoxId( f03.getId().getCodb(), session );
				if(f22 == null){
					CodeUtil.putInSessionMap("sMensajeError",  "F55CA022: No se ha podido obtener la configuración de banco para "+ f03.getId().getCodb());
					return false;
				}
				//&& ========= Obtener el numero de batch 
				iNoBatch = Divisas.numeroSiguienteJdeE1(   );
				
				if(iNoBatch == 0){
					CodeUtil.putInSessionMap("sMensajeError",  "No se ha podido obtener el Número de batch para registro de POS "+iNorefer );
					return bHecho = false;
				}
				
				//------ Determinar la cuenta de caja a utilizar, si local o de traslado
				if(ra.getIcaidpos()== iCaid){
					if(sCuentaTemp == null){
						sCuentaTemp = dv.obtenerCuentaCaja(iCaid, sCodcomp, MetodosPagoCtrl.TARJETA, sMoneda, session, null, null, null);
					}
					iCajaUso = iCaid;
					sCuentaC = sCuentaTemp;
				}else{
					iCajaUso = ra.getIcaidpos();
					sCuentaC = dv.obtenerCuentaCaja(ra.getIcaidpos(), sCodcomp, MetodosPagoCtrl.TARJETA, sMoneda, session, null, null, null);
				}
				//---- Cancelar el método en caso que no se encuentre la caja.
				if(sCuentaC == null){
					sMensaje = "No se encontró cuenta de Caja para el afiliado: "
								+ra.getCodigo() + " para caja origen: "
								+ra.getIcaidpos();
					CodeUtil.putInSessionMap("sMensajeError", sMensaje);
					return bHecho = false;
				}
				

				iMontoH = Long.parseLong( String.format("%1$.2f", ra.getDmontoneto() ).replace(".", "")  ); 
				
				//--- Identificar la cuenta de banco a utilizar;
				if(ra.getScodvmanual().equals("1")){ //-- Cuenta transitoria Voucher manual;
					if (sCuentaT == null){
						sMensaje =  "No se ha podido obtener la cuenta de Funcionarios y Empleados: '10.13600'";
						CodeUtil.putInSessionMap("sMensajeError", sMensaje);
						return false;
					}else{
						sCuentaB = sCuentaT;
						sDescripcion = "Déb.voucher pend.af:"+ra.getCodigo();
						sCodigoAuxiliar = "00062651";
						sTipoAuxiliar = "A";
						sTipodoc = "XG";
						iNorefer   = Integer.parseInt( ra.getReferencia() );
						iNorefer   =  Divisas.numeroSiguienteJdeE1Custom(valoresJdeNumeracion[8],valoresJdeNumeracion[9] );
						ra.setDepositoctatransitoria(0);
						
					}
				}else{
					//&& ======= Verificar si el banco debe conciliar, ajustar parametros para ambos casos (si, no)
					sCodigoAuxiliar = "";
					sTipoAuxiliar = "";
					iCodigoBanco = f03.getId().getCodb();
					
					if( ra.getDepositoctatransitoria() == 1 ) {
						
						 
						final String strCodigoBancoBuscar = String.valueOf( ra.getCodigobancopos() );
						final String moneda =  sMoneda.trim();
						final String codcomp =  sCodcomp.trim();
						
						sCtaBcoTransitoria = (String[])
						 CollectionUtils.find(cuentasTransitoriasBanco, new Predicate(){

							@Override
							public boolean evaluate(Object o) {
								String [] ctatrans = (String[])o;
								return
								ctatrans[6].trim().compareTo( moneda ) == 0 && 
								ctatrans[7].trim().compareTo( codcomp ) == 0 &&  
								ctatrans[8].trim().compareTo(strCodigoBancoBuscar) == 0 ;
							}
						 });
						
						sCuentaBco = sCtaBcoTransitoria;
						iNorefer   =  Divisas.numeroSiguienteJdeE1Custom(valoresJdeNumeracion[8],valoresJdeNumeracion[9] );
						sTipodoc = cajaparm.getParametros("34", "0", "CIERRE_DOCTYPE_P9").getValorAlfanumerico().toString();
						sDescripcion = "POS:"+ra.getCodigo()+" REF:"+ra.getReferencia();

						sTipoAuxiliar = cajaparm.getParametros("34", "0", "CIERRE_AUXTYPE").getValorAlfanumerico().toString() ; 
						sCodigoAuxiliar = ConfirmaDepositosCtrl.constructSubLibroCtaTbanco	(0, iCodigoBanco, iCaid, sMoneda, sCodcomp );
						
					}else{
						
						iNorefer   = Integer.parseInt(ra.getReferencia());
						sTipodoc = cajaparm.getParametros("34", "0", "TIPODOC_JDE_DEP_H").getValorAlfanumerico().toString() ;  
						sDescripcion = "POS:"+ra.getCodigo()+" REF:"+ra.getReferencia();
						sCuentaBco = dv.obtenerCuentaBanco(sCodcomp, sMoneda,iCodigoBanco, session, null, null, null);
						
						iNorefer = RevisionArqueoCtrl.generarReferenciaDeposito(iNorefer, MetodosPagoCtrl.TARJETA, sCuentaBco[2], sMoneda );
						
//						LogCrtl.sendLogDebgs("Referencia para deposito de tarjeta digitada  : " +ra.getReferencia()  +", validad " + iNorefer);
						
					}
					 
					sCuentaB = sCuentaBco;
					
					if(sCuentaBco == null){
						CodeUtil.putInSessionMap("sMensajeError", "No se ha podido obtener " +
								"la cuenta de banco "+iCodigoBanco
								+" para afiliado: "+ra.getCodigo());
						return bHecho = false;
					}
				}
				
				sSucursalDeposito = sCuentaB[2];
				
				bHecho = recCtrl.registrarBatchA92Session( session, dtFecha,  valoresJdeInsContado[8], iNoBatch, iMontoH, vaut.getId().getLogin(), 1, "APRARQUEO",  valoresJdeInsContado[9]);
				
				if(bHecho){
					//--- Asiento de pagos en Córdobas.
					if(sMoneda.equals( monedaBaseCompania.trim() )){
						
						bHecho  = recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sSucursalDeposito,sTipodoc, iNorefer,1.0,iNoBatch,
												sCuentaB[0],sCuentaB[1],sCuentaB[3], sCuentaB[4],sCuentaB[5],
												"AA", sMoneda, iMontoH,  sConcepto, vaut.getId().getLogin(),
												vaut.getId().getCodapp(), BigDecimal.ZERO, tipoCliente,
												sDescripcion,sCuentaB[2], sCodigoAuxiliar, sTipoAuxiliar, monedaBaseCompania,
												sCuentaB[2], "D", 0);
						if(!bHecho){
							sMensaje = "Batch no registrado por liquidacion de afiliado: "
									+ra.getCodigo() + " "+recCtrl.getError()
										.toString().split("@")[1];
							CodeUtil.putInSessionMap("sMensajeError", sMensaje);
							return bHecho;
						}
						bHecho  = recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sSucursalDeposito,sTipodoc, iNorefer, 2.0, iNoBatch,
											 sCuentaC[0],sCuentaC[1],sCuentaC[3],sCuentaC[4],sCuentaC[5],
											 "AA", sMoneda, iMontoH*(-1),sConcepto, vaut.getId().getLogin(),
											 vaut.getId().getCodapp(),BigDecimal.ZERO, tipoCliente,
											 "LIQ:" + ra.getReferencia()+" T:"+ ra.getMontototal(), sCuentaC[2],"","", monedaBaseCompania,
											 sCuentaC[2],"D", 0);
						if(!bHecho){
							sMensaje = "Batch no registrado por liquidacion de afiliado: "
									+ra.getCodigo() + " "+recCtrl.getError()
										.toString().split("@")[1];
							CodeUtil.putInSessionMap("sMensajeError", sMensaje);
							return false;
						}
					}//--Fin de depósitos en córdobas.
					else{
						
						iMontoCorH = (long)dv.roundDouble((ra.getDmontoneto() * tasaCambioOficial.doubleValue() )*100);
						
						bHecho  = recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sSucursalDeposito,sTipodoc,iNorefer,1.0,iNoBatch,
											sCuentaB[0],sCuentaB[1],sCuentaB[3], sCuentaB[4],sCuentaB[5],
											"AA", sMoneda, iMontoCorH, sConcepto, vaut.getId().getLogin(),
											vaut.getId().getCodapp(), tasaCambioOficial, tipoCliente,
											sDescripcion,sCuentaB[2], sCodigoAuxiliar,sTipoAuxiliar,
											monedaBaseCompania, sCuentaB[2], "F", iMontoH);
						if(!bHecho){
							sMensaje = "Batch no registrado por liquidacion de afiliado: "
									+ra.getCodigo() + " "+recCtrl.getError()
										.toString().split("@")[1];
							CodeUtil.putInSessionMap("sMensajeError", sMensaje);
							return false;
						}
						bHecho  = recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sSucursalDeposito,sTipodoc,iNorefer,1.0,iNoBatch,
													sCuentaB[0],sCuentaB[1],sCuentaB[3], sCuentaB[4],sCuentaB[5],
													"CA", sMoneda, iMontoH,  sConcepto, vaut.getId().getLogin(),
													vaut.getId().getCodapp(), tasaCambioOficial, tipoCliente,
													sDescripcion,sCuentaB[2], sCodigoAuxiliar,sTipoAuxiliar,
													monedaBaseCompania,sCuentaB[2],"F", 0);
						if(!bHecho){
							sMensaje = "Batch no registrado por liquidacion de afiliado: "
									+ra.getCodigo() + " "+recCtrl.getError()
										.toString().split("@")[1];
							CodeUtil.putInSessionMap("sMensajeError", sMensaje);
							return false;
						}
						bHecho  = recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sSucursalDeposito,sTipodoc,iNorefer,2.0,iNoBatch,
													 sCuentaC[0],sCuentaC[1],sCuentaC[3],sCuentaC[4],sCuentaC[5],
													 "AA", sMoneda, iMontoCorH*(-1),sConcepto, vaut.getId().getLogin(),
													 vaut.getId().getCodapp(), tasaCambioOficial, tipoCliente,
													 "LIQ:" + ra.getReferencia()+" T:"+ ra.getMontototal(), sCuentaC[2],"","",
													 monedaBaseCompania,sCuentaC[2],"F", (iMontoH*(-1)) );
						if(!bHecho){
							sMensaje = "Batch no registrado por liquidacion de afiliado: "
									+ra.getCodigo() + " "+recCtrl.getError()
										.toString().split("@")[1];
							CodeUtil.putInSessionMap("sMensajeError", sMensaje);
							return false;
						}
						bHecho  = recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sSucursalDeposito,sTipodoc,iNorefer,2.0,iNoBatch,
													sCuentaC[0],sCuentaC[1],sCuentaC[3],sCuentaC[4],sCuentaC[5],
													"CA", sMoneda, iMontoH*(-1),sConcepto, vaut.getId().getLogin(),
													vaut.getId().getCodapp(), tasaCambioOficial, tipoCliente,
													"LIQ:" + ra.getReferencia()+" T:"+ ra.getMontototal(), sCuentaC[2],"","",
													monedaBaseCompania ,sCuentaC[2],"F", 0);
						if(!bHecho){
							sMensaje = "Batch no registrado por liquidacion de afiliado: "
									+ra.getCodigo() + " "+recCtrl.getError()
										.toString().split("@")[1];
							CodeUtil.putInSessionMap("sMensajeError", sMensaje);
							return false;
						}
					}
					//&& ===== Guardar el registro de los datos para luego guardar el deposito
					List<Object[]> lstDeps = new ArrayList<Object[]>();
					if (CodeUtil.getFromSessionMap("rv_listaDepositos") != null)
						lstDeps = (ArrayList<Object[]>) CodeUtil.getFromSessionMap("rv_listaDepositos");
					
					BigDecimal bdMontoBatch;
					
					try {
						String strMontoBatch = Long.toString(iMontoH);
						strMontoBatch = strMontoBatch.substring(0, strMontoBatch.length() - 2)  + "." +
										strMontoBatch.substring(strMontoBatch.length() - 2, strMontoBatch.length());
						bdMontoBatch = new BigDecimal(strMontoBatch).setScale(2, RoundingMode.HALF_UP);
						
					} catch (Exception e) {
						e.printStackTrace();
						bdMontoBatch = new BigDecimal( Double.toString(ra.getDmontoneto()) ).setScale(2, RoundingMode.HALF_UP);
					}
					
					Object oDep[] = new Object[20];
					oDep[0]= iCajaUso;
					oDep[1]= sCodsuc;
					oDep[2]= sCodcomp;
					oDep[3]= iNoBatch;
					oDep[4]= iNorefer;
					oDep[5]= iNoarqueo;
					oDep[6]= sMoneda;
					oDep[7] = bdMontoBatch;
					oDep[8]= dtFecha;
					oDep[9]= dtHora;
					oDep[10]= vaut.getId().getLogin();
					oDep[11]= iNorefer ; //ra.getReferencia().trim();
					oDep[12]= "D";
					oDep[14]= MetodosPagoCtrl.TARJETA;
					oDep[15] = tasaCambioOficial.setScale(4, RoundingMode.HALF_UP);
					
					//&& =========== Crear el objeto que contendra la informacion de los movimientos sobre las cuentas.
					lstCtasxDeps = new ArrayList<Ctaxdeposito>();
					ctaxDeps = new Ctaxdeposito();
					ctaxDeps.setMonto(new BigDecimal(String.valueOf(ra.getDmontoneto())));
					ctaxDeps.setTipomov("C");
					ctaxDeps.setIdcuenta(sCuentaB[1]);
					ctaxDeps.setGmmcu(sCuentaB[3]);
					ctaxDeps.setGmobj(sCuentaB[4]);
					ctaxDeps.setGmsub(sCuentaB[5]);
					lstCtasxDeps.add(ctaxDeps);
					
					//&& ========== Objeto para debito, caja.
					ctaxDeps = new Ctaxdeposito();
					ctaxDeps.setMonto(new BigDecimal(String.valueOf(ra.getDmontoneto())));
					ctaxDeps.setTipomov("D");
					ctaxDeps.setIdcuenta(sCuentaC[1]);
					ctaxDeps.setGmmcu(sCuentaC[3]);
					ctaxDeps.setGmobj(sCuentaC[4]);
					ctaxDeps.setGmsub(sCuentaC[5]);
					lstCtasxDeps.add(ctaxDeps);
					oDep[16] = lstCtasxDeps;
					oDep[17] = iCodigoBanco ;
					oDep[18] = Integer.parseInt( ra.getReferencia().trim() );
					oDep[19] = ra.getDepositoctatransitoria(); //arqueo.getId().getDepctatran();
					
					lstDeps.add(oDep);
					CodeUtil.putInSessionMap("rv_listaDepositos", lstDeps);					
					
				}else{
					sMensaje = "No se ha podido registrar el batch: "+iNoBatch + " para registro de depósito de liquidación de POS: "+ra.getCodigo();
				}
			 
				if(!bHecho)
					break;
				
			} 

		 
			if(!bHecho){
				CodeUtil.putInSessionMap("sMensajeError", sMensaje);
			}
			
		} catch (Exception error) {
			bHecho = false;
			error.printStackTrace(); 
			CodeUtil.putInSessionMap( "sMensajeError",	"Depósitos de afiliados no registrados, favor intente nuevamente" );
		}
		return bHecho;
	}
/****************************************************************************************/
/** Método: Registra los registro de depósitos en banco por Afiliados y su monto neto.
 *	Fecha:  18/05/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 **/
	@SuppressWarnings("unchecked")
	public boolean registrarDepositosAfiliados1(int iNoarqueo,List lstResumenPos,int iCaid,String sCodcomp,String sCodsuc,
												Date dtFecha,Date dtHora, String sMoneda, Connection cn,Session sesionCaja,
												Session sesionENS,Transaction transCaja,Transaction transENS,
												F55ca014 f14){
		boolean bHecho = true;
		double dTasaJDE = 1.0;
		BigDecimal bdTasaJDE = BigDecimal.ONE, bdTasa = BigDecimal.ZERO;
		int iNoBatch = 0,iMontoH,iMontoCorH,iNorefer;
		String sCuentaC[],sCuentaB[], sSucursalDeposito ="", sTipoCliente, sConcepto,sFecha,sMensaje="";
		
		Vautoriz[] vaut;
		Vf55ca01 f55ca01;
		TasaCambioCtrl tcCtrl;
		Divisas dv = new Divisas();
		FechasUtil f = new FechasUtil();
		ReciboCtrl recCtrl = new ReciboCtrl();
		EmpleadoCtrl emCtrl = new EmpleadoCtrl();
		
		try {
			//--- Datos de la caja.
			f55ca01 = (Vf55ca01)((List)CodeUtil.getFromSessionMap( "lstCajas")).get(0);
			vaut = (Vautoriz[]) CodeUtil.getFromSessionMap( "sevAut");
			sFecha = f.formatDatetoString(dtFecha, "dd/MM/yyyy");
			sConcepto = "Arqueo "+iNoarqueo+", Caja "+iCaid+ " "+ sFecha;
			
			//--- Tasa de Cambio.
			if(!sMoneda.equals(f14.getId().getC4bcrcd().trim())){
				tcCtrl = new TasaCambioCtrl();
				dTasaJDE = tcCtrl.obtenerTasaJDExFecha(sMoneda, f14.getId().getC4bcrcd().trim(),
														dtFecha, sesionENS, transENS);											
				bdTasaJDE = new BigDecimal(dTasaJDE);
			}
			
			//--- obtener el tipo de usuario que aprueba del arqueo.				
			sTipoCliente = emCtrl.determinarTipoCliente(f55ca01.getId().getCaan8());
			
			//--- Leer la cuenta de caja y la cuenta del banco.
			sCuentaC = dv.obtenerCuentaCaja(iCaid, sCodcomp, MetodosPagoCtrl.TARJETA, sMoneda, sesionCaja, transCaja, sesionENS, transENS);
			if(sCuentaC!=null){
				sCuentaB = dv.obtenerCuentaBanco(sCodcomp, sMoneda, (f14.getId().getC4bnc()==0)?100001:f14.getId().getC4bnc(), 
												sesionCaja, transCaja, sesionENS, transENS);
				if(sCuentaB!=null){
					//sSucursalDeposito = sCuentaB[2];
					sSucursalDeposito = sCuentaC[2];
					
					//----Recorrer toda la lista de POS y hacer sus asientos de diarios.
					for (int i = 0; i < lstResumenPos.size(); i++) {
						ResumenAfiliado ra = (ResumenAfiliado)lstResumenPos.get(i);
						iMontoH = (int)(dv.roundDouble(ra.getDmontoneto() * 100));
						iNorefer= Integer.parseInt(ra.getReferencia());
						
						iNoBatch = dv.leerActualizarNoBatch();
						if(iNoBatch == -1){
							bHecho = false;
							sMensaje = "No se ha podido obtener el Número de batch para registro de POS "+iNorefer;
							break;
						}
						
						//--- registrar el encabezado del asiento de batch.
						//bHecho = recCtrl.registrarBatch(dtFecha,cn,"G", iNoBatch, iMontoH, vaut[0].getId().getLogin(), 1, MetodosPagoCtrl.DEPOSITO); VERSION A7.3
						bHecho = recCtrl.registrarBatchA92(dtFecha,cn,"G", iNoBatch, iMontoH, vaut[0].getId().getLogin(), 1, MetodosPagoCtrl.DEPOSITO);
						if(bHecho){
							//--- Asiento de pagos en Córdobas.
							if(sMoneda.equals(f14.getId().getC4bcrcd().trim())){
								bHecho  = recCtrl.registrarAsientoDiario(dtFecha, sesionCaja,sSucursalDeposito,"ZX",iNorefer,1.0,iNoBatch,sCuentaB[0],sCuentaB[1],sCuentaB[3], sCuentaB[4],
														sCuentaB[5],"AA", sMoneda, iMontoH,  sConcepto, vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),
														BigDecimal.ZERO,sTipoCliente,"Débito Bco por POS "+ra.getCodigo(),sCuentaB[2],
														"","",sMoneda,sCuentaB[2],"F");
								if(bHecho){
									bHecho  = recCtrl.registrarAsientoDiario(dtFecha, sesionCaja,sSucursalDeposito,"ZX",iNorefer,2.0,iNoBatch,sCuentaC[0],sCuentaC[1],sCuentaC[3],sCuentaC[4],
														sCuentaC[5],"AA", sMoneda, iMontoH*(-1),sConcepto, vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),
														BigDecimal.ZERO,sTipoCliente,"Crédito Caja por POS "+ra.getCodigo(),sCuentaC[2],
														"","",sMoneda,sCuentaC[2],"F");
									if(bHecho){
										
										List<Object[]> lstDeps = new ArrayList<Object[]>();
										if (CodeUtil.getFromSessionMap("rv_listaDepositos") != null)
											lstDeps = (ArrayList<Object[]>) CodeUtil.getFromSessionMap("rv_listaDepositos");
										
										Object oDep[] = new Object[17];
										oDep[0]= iCaid;
										oDep[1]= sCodsuc;
										oDep[2]= sCodcomp;
										oDep[3]= iNoBatch;
										oDep[4]= iNorefer;
										oDep[5]= iNoarqueo;
										oDep[6]= sMoneda;
										oDep[7]= new BigDecimal(ra.getDmontoneto());
										oDep[8]= dtFecha;
										oDep[9]= dtHora;
										oDep[10]= vaut[0].getId().getLogin();
										oDep[11]= String.valueOf(ra.getReferencia());
										oDep[12]= "D";
										oDep[14]= MetodosPagoCtrl.TARJETA;
										oDep[15] = bdTasa;
										lstDeps.add(oDep);
										CodeUtil.putInSessionMap("rv_listaDepositos", lstDeps);
									}else{
										sMensaje =  " No se ha podido registrar línea 2 para batch: "+iNoBatch +", Refer: "+ra.getReferencia();
										sMensaje += " para registro de depósito general de POS: "+ra.getCodigo()+ ", en "+sMoneda;
									}
								}else{
									sMensaje =  " No se ha podido registrar línea 1 para batch: "+iNoBatch +", Refer: "+ra.getReferencia();
									sMensaje += " para registro de depósito general de POS: "+ra.getCodigo()+ ", en "+sMoneda;
								}								
							}//--Fin de depósitos en córdobas.
							else{
								iMontoCorH = (int)dv.roundDouble((ra.getDmontoneto() * dTasaJDE)*100);
								bdTasa = new BigDecimal(dTasaJDE);
								bHecho  = recCtrl.registrarAsientoDiario(dtFecha,sesionCaja,sSucursalDeposito,"ZX",iNorefer,1.0,iNoBatch,sCuentaB[0],sCuentaB[1],sCuentaB[3], sCuentaB[4],
												sCuentaB[5],"AA", sMoneda, iMontoCorH,  sConcepto, vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),
												bdTasaJDE,sTipoCliente,"Débito Bco por POS "+ra.getReferencia(),sCuentaB[2],
												"","",f14.getId().getC4bcrcd(),sCuentaB[2],"F");
								if(bHecho){
									bHecho  = recCtrl.registrarAsientoDiario(dtFecha, sesionCaja,sSucursalDeposito,"ZX",iNorefer,1.0,iNoBatch,sCuentaB[0],sCuentaB[1],sCuentaB[3], sCuentaB[4],
													sCuentaB[5],"CA", sMoneda, iMontoH,  sConcepto, vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),
													BigDecimal.ZERO,sTipoCliente,"Débito Bco por POS "+ra.getReferencia(),sCuentaB[2],
													"","",sMoneda,sCuentaB[2],"F");
									if(bHecho){
										bHecho  = recCtrl.registrarAsientoDiario(dtFecha, sesionCaja,sSucursalDeposito,"ZX",iNorefer,2.0,iNoBatch,sCuentaC[0],sCuentaC[1],sCuentaC[3],sCuentaC[4],
														sCuentaC[5],"AA", sMoneda, iMontoCorH*(-1),sConcepto, vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),
														bdTasaJDE,sTipoCliente,"Crédito Caja por POS "+ra.getReferencia(),sCuentaC[2],
														"","",f14.getId().getC4bcrcd(),sCuentaC[2],"F");
										if(bHecho){
											bHecho  = recCtrl.registrarAsientoDiario(dtFecha, sesionCaja,sSucursalDeposito,"ZX",iNorefer,2.0,iNoBatch,sCuentaC[0],sCuentaC[1],sCuentaC[3],sCuentaC[4],
													sCuentaC[5],"CA", sMoneda, iMontoH*(-1),sConcepto, vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),
													bdTasaJDE,sTipoCliente,"Crédito Caja por POS "+ra.getReferencia(),sCuentaC[2],
													"","",sMoneda,sCuentaC[2],"F");
											if(bHecho){
												
												List<Object[]> lstDeps = new ArrayList<Object[]>();
												if (CodeUtil.getFromSessionMap("rv_listaDepositos") != null)
													lstDeps = (ArrayList<Object[]>) CodeUtil.getFromSessionMap("rv_listaDepositos");
												
												Object oDep[] = new Object[17];
												oDep[0]= iCaid;
												oDep[1]= sCodsuc;
												oDep[2]= sCodcomp;
												oDep[3]= iNoBatch;
												oDep[4]= iNorefer;
												oDep[5]= iNoarqueo;
												oDep[6]= sMoneda;
												oDep[7]= new BigDecimal(ra.getDmontoneto());
												oDep[8]= dtFecha;
												oDep[9]= dtHora;
												oDep[10]= vaut[0].getId().getLogin();
												oDep[11]= String.valueOf(ra.getReferencia());
												oDep[12]= "D";
												oDep[14]= MetodosPagoCtrl.TARJETA;
												oDep[15] = bdTasa;
												lstDeps.add(oDep);
												CodeUtil.putInSessionMap("rv_listaDepositos", lstDeps);
											}else{
												sMensaje =  " No se ha podido registrar línea 2 CA para batch: "+iNoBatch +", Refer: "+ra.getReferencia();
												sMensaje += " para registro de depósito general de POS: "+ra.getCodigo()+ ", en "+sMoneda;	
											}
										}else{
											sMensaje =  " No se ha podido registrar línea 2 AA para batch: "+iNoBatch +", Refer: "+ra.getReferencia();
											sMensaje += " para registro de depósito general de POS: "+ra.getCodigo()+ ", en "+sMoneda;	
										}
									}else{
										sMensaje =  " No se ha podido registrar línea 1 CA para batch: "+iNoBatch +", Refer: "+ra.getReferencia();
										sMensaje += " para registro de depósito general de POS: "+ra.getCodigo()+ ", en "+sMoneda;	
									}
								}else{
									sMensaje =  " No se ha podido registrar línea 1 AA para batch: "+iNoBatch +", Refer: "+ra.getReferencia();
									sMensaje += " para registro de depósito general de POS: "+ra.getCodigo()+ ", en "+sMoneda;
								}
							}
						}else{
							sMensaje = "No se ha podido registrar el batch: "+iNoBatch + " para registro de depósito general de POS: "+ra.getCodigo();
						}
						//--- Detener la ejecución del ciclo en caso de errores.
						if(!bHecho)
							break;
						
					}//--fin de for de lista
				}else{
					bHecho = false;
					sMensaje = "No se ha podido leer la cuenta de banco "+f14.getId().getC4bnc()+" para depósito de tarjeta de crédito: "+ sMoneda;
				}
			}else{
				bHecho = false;
				sMensaje = "No se ha podido leer la cuenta de caja para Tarjeta de Crédito: "+ sMoneda;
			}
			
			//-----Poner bandera de mensaje de error
			if(!bHecho){
				CodeUtil.putInSessionMap("sMensajeError", sMensaje);
			}
		} catch (Exception error) {
			bHecho = false;
			sMensaje = "Error de Sistema: No se ha podido realizar la transacción de registro de depósito general de afiliados " + error;
			CodeUtil.putInSessionMap("sMensajeError", sMensaje);
			error.printStackTrace();
		}
		return bHecho;
	}
	
/****************************************************************************************/
/** Método: Verifica que el valor digitado para la referencia del POS sea válido.
 *	Fecha:  17/05/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 **/
	public void agregarReferenciasPos(ActionEvent ev){
		String sReferencia="",sMensaje="";
		boolean bValido = true;
		CtrlCajas cc = new CtrlCajas();
		String sReferAusar[]= null;
		List<ResumenAfiliado> lstResumenAfiliado = new ArrayList<ResumenAfiliado>();
		
		try {

			lblMsgValidaReferencia.setValue("");
			List rows = gvReferenciaPos.getRows();
			sReferAusar = new String[rows.size()];
			
			//---- Establecer los estilos correctos al iniciar la validación.
			for(int i=0; i<rows.size();i++){
				RowItem ri = (RowItem)rows.get(i);
				List lstCeldas = ri.getCells();
				Cell celda = (Cell)lstCeldas.get(0);
				HtmlInputText hiRefer = (HtmlInputText)celda.getChildren().get(0);
				hiRefer.setStyle("height: 11px; width: 60px; text-align: right");
				hiRefer.setStyleClass("frmInput2ddl");
			}
			
			//------- Validaciones.
			for(int i=0; i<rows.size();i++){
				
				RowItem ri = (RowItem)rows.get(i);
				ResumenAfiliado ra = (ResumenAfiliado)DataRepeater.getDataRow(ri);
				ra.setReferencia(ra.getReferencia().trim());
				sReferencia = ra.getReferencia();
				
				
				if( !sReferencia.trim().matches(PropertiesSystem.REGEXP_8DIGTS) ){
					sMensaje = "El valor de la referencia debe contener entre 1 y 8 dígitos unicamente";
					bValido = false;
					return;
				}
				
				//----- Verificar que la referencia no se repita entre las que se estan registrando.
				if(bValido){
					if(i==0){ 
						sReferAusar[i]=sReferencia;
						lstResumenAfiliado.add(ra);
					}
					else{
						for(int j=0;j<i;j++){
							if(sReferAusar[j].equals(sReferencia)){
								bValido=false;
								sMensaje = "El valor de referencia: " +sReferencia + " se está usando en más de un afiliado";
								break;
							}
						}
						if(bValido){
								sReferAusar[i] = sReferencia;
								lstResumenAfiliado.add(ra);
						}
					}
				}

				//--------- Actualizar el estado de caja de texto
				List lstCeldas = ri.getCells();
				Cell celda = (Cell)lstCeldas.get(0);
				HtmlInputText hiRefer = (HtmlInputText)celda.getChildren().get(0);
				if(!bValido){
					hiRefer.setStyle("height: 11px; width: 60px; text-align: right; border: 1px solid red;");
					break;
				}
			}
		 
			
		} catch (Exception error) {
			
			error.printStackTrace(); 
			sMensaje = "No se puede asignar el valor de la referencia, intente nuevamente " ; 
			bValido = false;
			
		}finally{
			
			if(bValido){
				 
				CodeUtil.putInSessionMap("rva_lstResumenAfiliado", lstResumenAfiliado);		//--Datos para asientos de depósitos
				CodeUtil.putInSessionMap("rva_pasoAprobarArqueo", "1"); 					//--Bandera de paso.
				dwCargarReferenciasPOS.setWindowState("hidden");

			}else{
				lblMsgValidaReferencia.setValue(sMensaje);
			}
			
		}
	}
/****************************************************************************************/
/** Método: Consulta los pagos y muestra el resumen por POS para que se le pueda asignar
 * 			la referencia que entrega el banco, que será usada como GLDOC en los asientos. 
 *	Fecha:  14/05/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 **/
	@SuppressWarnings("unchecked")
	public void mostrarAsignarReferenciaPOS(ActionEvent ev){
		RevisionArqueoCtrl rvCtrl = new RevisionArqueoCtrl();
		List lstPOS = null;
		DetalleArqueoCaja dac = null;
		String sListaRecibos ="(0)";
		
		try {

			LogCajaService.CreateLog("mostrarAsignarReferenciaPOS", "INFO", "mostrarAsignarReferenciaPOS-INICIO");
			
			lblMsgValidaReferencia.setValue("");
			
			if(CodeUtil.getFromSessionMap( "rv_DAC") == null){
				return;
			}
				
			dac = (DetalleArqueoCaja)CodeUtil.getFromSessionMap( "rv_DAC");
			
			if(CodeUtil.getFromSessionMap("rv_ListaRecibosArqueo") != null )
				sListaRecibos = CodeUtil.getFromSessionMap("rv_ListaRecibosArqueo").toString();
			
			List<DncCierreDonacion> cierreDonacion = null;
			if( CodeUtil.getFromSessionMap( "rv_CierreDonaciones" ) != null ){
				cierreDonacion = (ArrayList<DncCierreDonacion>) CodeUtil.getFromSessionMap( "rv_CierreDonaciones" ) ;
			}
			
			lstPOS = rvCtrl.consultarMontosPOS(dac.getCaid(),dac.getCodcomp(),dac.getCodsuc(),
						   dac.getMoneda(),sListaRecibos,dac.getFechaarqueo(), cierreDonacion);
			
			if( lstPOS == null ){
				CodeUtil.removeFromSessionMap( "rva_pasoAprobarArqueo");
				dwValidarAprobacionArqueo.setWindowState("normal");
				lblValidarAprobacion.setValue( " No se han podido cargar los datos para validar referencia de liquidación de Afiliados" );
				return;
			}
			
			
			if(lstPOS.isEmpty() ){
				dwValidarAprobacionArqueo.setWindowState("normal");
				lblValidarAprobacion.setValue("No se han registrado transacciones con Afiliados en este arqueo");
				CodeUtil.putInSessionMap("rva_pasoAprobarArqueo", "1"); //--Bandera de paso.
			
			}else{
				CodeUtil.putInSessionMap("rva_lstReferenciapos", lstPOS);
				gvReferenciaPos.dataBind();
				dwCargarReferenciasPOS.setWindowState("normal");
			}
			 
			
		} catch (Exception error) { 
			LogCajaService.CreateLog("mostrarAsignarReferenciaPOS", "ERR", error.getMessage());
		} finally {
			LogCajaService.CreateLog("mostrarAsignarReferenciaPOS", "INFO", "mostrarAsignarReferenciaPOS-FIN");
		}
	}
/****************************************************************************************/	
/**  Mostrar la Ventana para el detalle de las cambios mixtos o en otras monedas  *******/	
	public void cargarRecibosxCambios(ActionEvent ev){
		try {
			if(CodeUtil.getFromSessionMap( "rv_DAC")!=null){
				DetalleArqueoCaja dac = (DetalleArqueoCaja)CodeUtil.getFromSessionMap( "rv_DAC");
				List lstCambiosMixtos = dac.getReccambiomixto();
				if(lstCambiosMixtos!=null){
					CodeUtil.putInSessionMap("rv_lstDetalleCambios",lstCambiosMixtos);
					gvDetalleCambios.setPageIndex(0);
					gvDetalleCambios.dataBind();
					dwDetalleCambios.setWindowState("normal");
				}
			}
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
/****************************************************************************************/	
/********* 	  Mostrar la Ventana para el detalle de las salidas de caja   ***************/
	public void cargarDetalleSalidas(ActionEvent ev){
		try {
			if(CodeUtil.getFromSessionMap( "rv_DAC")!=null){
				DetalleArqueoCaja dac = (DetalleArqueoCaja)CodeUtil.getFromSessionMap( "rv_DAC");
				List lstSalidas = dac.getLstSalidas();
				if(lstSalidas!=null){
					CodeUtil.putInSessionMap("rev_lstDetalleSalidas", lstSalidas);
					gvDetalleSalidas.dataBind();
					dwDetalleSalidas.setWindowState("normal");
				}
			}
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
/****** 1. refrescar la lista de arqueos del grid principal **************/
	public void refrescarlstArqueos(ActionEvent ev){
		refrescarArqueos();
	}
	@SuppressWarnings("unchecked")
	public void refrescarArqueos(){
		List<Varqueo> lstArqueosPendRev = new ArrayList<Varqueo>();
		SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
		
		try{
			Vf55ca01 caja = ((ArrayList<Vf55ca01>)CodeUtil.getFromSessionMap( "lstCajas")).get(0);	
			Date dtInicio = new FechasUtil().quitarAgregarDiasFecha(-30, new Date());
			Date dtFinal  = new FechasUtil().quitarAgregarDiasFecha(-1, new Date());
			String mensaje = "" ;
			
			lstArqueosPendRev = new ArqueoCtrl().getArqueosCaja(0, 
					caja.getId().getCaid(), "", "", "P",
					dtInicio,dtFinal, caja.getId().getCacont(), 100);
			
			if(lstArqueosPendRev == null || lstArqueosPendRev.isEmpty()){					
				CodeUtil.removeFromSessionMap( "lstArqueosPendRev");
				CodeUtil.removeFromSessionMap( "lstInicial_Arqueos");
				CodeUtil.removeFromSessionMap( "rva_msgArqueos") ;	
				mensaje = "No se encontraron arqueos pendientes"; 
			}else{
				CodeUtil.putInSessionMap("lstArqueosPendRev", lstArqueosPendRev);
				CodeUtil.putInSessionMap("lstInicial_Arqueos", lstArqueosPendRev);
				mensaje = lstArqueosPendRev.size() +" arqueo(s) encontrados"; 
			}						
			CodeUtil.removeFromSessionMap( "rva_lstFiltroMoneda");
			CodeUtil.removeFromSessionMap( "rva_lstFiltroCompania");
			CodeUtil.removeFromSessionMap( "rva_lstFiltroEstado");
			CodeUtil.removeFromSessionMap( "rva_lstFiltroCaja");
			CodeUtil.removeFromSessionMap( "rva_dtFechaArqueo");
			
			dcFechaArqueo.setValue(dtInicio);
			dcFechaArqueoFin.setValue(dtFinal);
			lblMensaje.setValue(mensaje);
			gvArqueosPendRev.dataBind();
			ddlFiltroCompania.dataBind();
			ddlFiltroMoneda.dataBind();
			ddlFiltroEstado.dataBind();
			ddlFiltroCaja.dataBind();
			
			srm.addSmartRefreshId(lblMensaje.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(gvArqueosPendRev.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(ddlFiltroCompania.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(ddlFiltroMoneda.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(ddlFiltroEstado.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(ddlFiltroCaja.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(dcFechaArqueo.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(dcFechaArqueo.getClientId(FacesContext.getCurrentInstance()));

		}catch(Exception error){
			error.printStackTrace();
		}
	}
/****** 2. Filtrar los arqueos por monedas, por compañía o por ambos **************/
	@SuppressWarnings("unchecked")
	public void filtrarArqueos(ValueChangeEvent ev){
		String sCaid, sMoneda, sCodcomp,sMsg, sEstado;
		Date dtFechaAr = new Date();
		Date dtFechaArFin = new Date();
		int iCaid = 0;
		List<Varqueo> lstArqueos;
		 
		try{
			
			LogCajaService.CreateLog("filtrarArqueos", "INFO", "filtrarArqueos-INICIO");
			
			Vf55ca01 caja = ((ArrayList<Vf55ca01>)CodeUtil.getFromSessionMap( "lstCajas")).get(0);
			
			sCaid    = ddlFiltroCaja.getValue().toString();
			sCodcomp = ddlFiltroCompania.getValue().toString();
			sMoneda   = ddlFiltroMoneda.getValue().toString();
			sEstado   = ddlFiltroEstado.getValue().toString();
			
			FechasUtil f = new FechasUtil();
			dtFechaAr = (dcFechaArqueo.getValue() == null)?
					f.quitarAgregarDiasFecha(-5, new Date())
					:(Date)dcFechaArqueo.getValue();	

			dtFechaArFin = (dcFechaArqueoFin.getValue() == null)?
					f.quitarAgregarDiasFecha(-1, new Date())
					:(Date)dcFechaArqueoFin.getValue();
			
			if(sCaid.compareTo("SCA") != 0)
				iCaid = Integer.parseInt(sCaid);
			if(sCodcomp.compareTo("COMP") == 0 )
				sCodcomp = "";
			if(sMoneda.compareTo("MON") == 0 )
				sMoneda = "";
			if(sEstado.compareTo("SE") == 0 )
				sEstado = "";
			
			lstArqueos = new ArqueoCtrl().getArqueosCaja(iCaid, caja.getId()
							.getCaid(),sCodcomp,sMoneda, sEstado,dtFechaAr,
							dtFechaArFin,caja.getId().getCacont(), 300 );
			
			sMsg = (lstArqueos.size() == 0)? 
					"No se encontraron arqueos pendientes":
					"Resultados encontrados: "+lstArqueos.size();
			
			CodeUtil.putInSessionMap("lstArqueosPendRev", lstArqueos);
			gvArqueosPendRev.dataBind();
			lblMensaje.setValue(sMsg);
			
			dwCargando.setWindowState("hidden");
			dcFechaArqueo.setValue(dtFechaAr);
			dcFechaArqueoFin.setValue(dtFechaArFin);
			
			SmartRefreshManager.getCurrentInstance().addSmartRefreshId(
				dcFechaArqueo.getClientId(FacesContext.getCurrentInstance()));
			SmartRefreshManager.getCurrentInstance().addSmartRefreshId(
				dcFechaArqueoFin.getClientId(FacesContext.getCurrentInstance()));
			SmartRefreshManager.getCurrentInstance().addSmartRefreshId(
					dwCargando.getClientId(FacesContext.getCurrentInstance()));

		}catch(Exception error){
			LogCajaService.CreateLog("filtrarArqueos", "ERR", error.getMessage());
		} finally {
			LogCajaService.CreateLog("filtrarArqueos", "INFO", "filtrarArqueos-INICIO");
			HibernateUtilPruebaCn.closeSession();
		}
	}
	
/************* 3. mostrar la ventana del detalle de arqueo ***************/
	@SuppressWarnings({ "unchecked", "static-access" })
	public void mostrarDetalleArqueo(ActionEvent ev){
		Reintegro r = null;
		boolean ajustarMinimo = false;
		try{
			LogCajaService.CreateLog("mostrarDetalleArqueo", "INFO", "mostrarDetalleArqueo-INICIO");
			
			RowItem ri = (RowItem)ev.getComponent().getParent().getParent();
			Varqueo ar = (Varqueo)com.infragistics.faces.shared.component.DataRepeater.getDataRow(ri);
			CodeUtil.putInSessionMap("rva_VARQUEO", ar);
			
			//&& ====== Validar que se tenga permiso para anular arqueos.
			Vautoriz vaut = ((Vautoriz[]) CodeUtil.getFromSessionMap( "sevAut"))[0];
			lnkAnularArqueoCaja.setStyle(    (vaut.getId().getCodper().equals("P000000004"))?"display:inline":"display:none"  );
			
			//Verificar si ajuste al fondo minimo
			
			 lblCDC_montoMinReint.setValue("0.00");
			 lblCDC_montoMinAjust.setValue("0.00");
			
			ReciboCtrl recCtrl = new ReciboCtrl();
			r = recCtrl.verificarSiTieneReintegro(ar.getId().getNoarqueo(), ar
					.getId().getCaid(), ar.getId().getCodcomp(), ar.getId()
					.getCodsuc(), ar.getId().getMoneda());	
			if(r!=null){
				 CodeUtil.putInSessionMap("AjustaMinimo", 0);
				 lblCDC_montoMinReint.setValue(r.getMonto());
				 lblCDC_montoMinAjust.setValue(ar.getId().getMinimo().doubleValue() - r.getMonto().doubleValue());
				 ajustarMinimo = true;
			}
			
			//Datos del encabezado de la ventana.
			lblnocaja.setValue(ar.getId().getCaid()+ ", " + ar.getId().getCaname().trim());
			lblsucursal.setValue(ar.getId().getCodsuc().trim() + ", " + ar.getId().getNombresuc().trim());
			lblcajero.setValue(ar.getId().getCodcajero() + ", " + ar.getId().getNombrecajero().trim());
			lblCompania.setValue(ar.getId().getCodcomp().trim() + ", " +ar.getId().getNombrecomp().trim());
			
			lblnoarqueo.setValue(ar.getId().getNoarqueo()+" "+ar.getId().getMoneda().trim());
			lblMoneda.setValue( DateFormat.getDateInstance(DateFormat.FULL, new Locale("ES","es"))
					.format(ar.getId().getFecha())+ " "+ ar.getId().getHora());
			
			//totales del arqueo.
			lblTotaIngresos.setValue(ar.getId().getTingreso());
			lblTotalEgresos.setValue(ar.getId().getTegresos());
			lblACDetfp_Total.setValue(ar.getId().getTpagos());
			
			//Datos de cálculo del depósito final.			
			lblCDC_efectnetoRec.setValue(ar.getId().getNetorec());
			lblCDC_montominimo.setValue(ar.getId().getMinimo());
			lblCDC_depositoSug.setValue(ar.getId().getDsugerido());
			lblbCDC_efectivoenCaja.setValue(ar.getId().getEfectcaja());			
			lblCDC_depositoFinal.setValue(ar.getId().getDfinal());
			txtCDC_ReferDeposito.setValue(ar.getId().getReferdep());
			
			String sEstilo = "",etiqueta = "";
			double sf = ar.getId().getSf().doubleValue();
			if(sf > 0){
				sEstilo = "color: green";
				etiqueta = "Sobrante";
			}
			else if(sf < 0){
				sEstilo = "color : red";
				etiqueta = "faltante";
			}
			else if(sf == 0){
				sEstilo = "color : black";
				etiqueta = "Sobrante/Faltante";
			}
			
			lblCDC_SobranteFaltante.setValue(sf);
			lblCDC_SobranteFaltante.setStyle(sEstilo);
			lblet_SobranteFaltante.setValue(etiqueta);
			lblet_SobranteFaltante.setStyle(sEstilo);
			lblaprobDepSugerido.setValue(ar.getId().getDfinal());
			
			CodeUtil.removeFromSessionMap( "rv_bvalidoCheques");
			CodeUtil.removeFromSessionMap( "rva_lstLiquidacionCheques");
			lnkLiquidacionChk.setStyleClass("igLink");
			lnkLiquidacionChk.setStyle((ar.getId().getCodcomp().trim().equals("E03"))? "display:inline":"display:none");
			
			DetalleArqueoCaja dac = cargarDetArqueo(ar.getId().getReferdep(),
					ar.getId().getNoarqueo(), ar.getId().getDfinal()
							.doubleValue(), ar.getId().getFecha(), ar.getId()
							.getHora(), ar.getId().getCaid(), ar.getId()
							.getCodcomp(), ar.getId().getCodsuc(), ar.getId()
							.getMoneda(), ajustarMinimo);
			if(dac == null){
				dwValidarAprobacionArqueo.setWindowState("normal");
				lblValidarAprobacion
						.setValue("Error al cargar los datos del arqueo: "
								+ ar.getId().getNoarqueo() + ", Caja: "
								+ ar.getId().getCaid());
				return;
			}
			CodeUtil.putInSessionMap("rv_DAC", dac);
			
			lblVentasTotales.setValue(dac.getTotalFact());
			lblTotalDevoluciones.setValue(dac.getTotalDev());
			lblTotalVtsCredito.setValue(dac.getTotalFactcr());
			lblTotalVentasNetas.setValue(dac.getTotalFact() - dac.getTotalDev() - dac.getTotalFactcr());			
			lblTotalAbonos.setValue(dac.getTotalrecr());
			lblTotalFinan.setValue(dac.getTotalrefn());
			lblTotalPrimas.setValue(dac.getTotalrepr());
			lblTotalIngex.setValue(dac.getTotalreex());
			
			lblTotalRecibosPMT.setValue( dac.getTotalrepm() );
			
			lblTotalIngRecxmonex.setValue(dac.getTotalinrcpom());
			lblCambioOtraMoneda.setValue(dac.getTotalcambiomixto());

			lblTotalVtsPagBanco.setValue(dac.getTotalventaspb());
			lblTotalAbonoPagBanco.setValue(dac.getTotalabonospb());
			lblTotalFinanPagBanco.setValue(dac.getTotalfinanpb());
			lblTotalPrimasPagBanco.setValue(dac.getTotalprimaspb());
			lblTotalIexPagBanco.setValue(dac.getTotalIngextpb());
			
			lblTotalAnticiposPMTPagBanco.setValue(dac.getTotalpmpb());
			
			lblOEsalidas.setValue(dac.getDTotalsalidas());
			lblTotalOtrosEgresos.setValue(dac.getTotalotrosegresos());
			
			
			//&& ================== buscar donaciones para el cierre;
			CodeUtil.removeFromSessionMap(new String[]{"rv_listaDepositos", "rva_TotalesDonacionAfiliado", 
					"rv_CierreDonaciones", "rva_lstDonacionesFormaPago"}  ) ;
			
			String sqlSelect = "select * from " + PropertiesSystem.ESQUEMA
					+ ".dnc_cierre_donacion " + "where caid = " +  ar.getId().getCaid()
					+ " and codcomp = '" + ar.getId().getCodcomp() + "' and moneda = '"
					+ ar.getId().getMoneda() + "' and date(fechacierre) = '"
					+ new SimpleDateFormat("yyyy-MM-dd").format(ar.getId().getFecha() ) + "' "
					+ " and noarqueo = " + ar.getId().getNoarqueo();

			// Object[] dtaCnDnc= ConsolidadoDepositosBcoCtrl.getSessionForQuery() ;

			List<DncCierreDonacion> cierreDonacion = (ArrayList<DncCierreDonacion>)
					ConsolidadoDepositosBcoCtrl.executeSqlQuery( sqlSelect, true, DncCierreDonacion.class );
			
			lstDonacionesFormaPago = new ArrayList<DncDonacion>();
			
			if( !cierreDonacion.isEmpty() ){
				CodeUtil.putInSessionMap("rv_CierreDonaciones", cierreDonacion) ;
				
				List<Integer>idsCierresDnc = (ArrayList<Integer>)CodeUtil.selectPropertyListFromEntity(cierreDonacion, "idcierredonacion", false);
				
				String idsIn = idsCierresDnc.toString().replace("[", "(").replace("]", ")");
				
				sqlSelect = 
					"select * from "+ PropertiesSystem.ESQUEMA + ".dnc_donacion where iddonacion in " +
					"( SELECT iddonacion FROM "+ PropertiesSystem.ESQUEMA +".DNC_CIERRE_DETALLE WHERE idcierrednc in "+ idsIn + " ) " ; 
				
				LogCajaService.CreateLog("mostrarDetalleArqueo", "QRY", sqlSelect);
				
				List<DncDonacion> lstDonacionesCierre = ( ArrayList<DncDonacion> )
						ConsolidadoDepositosBcoCtrl.executeSqlQuery( sqlSelect, true, DncDonacion.class );
				
				CodeUtil.putInSessionMap("rva_lstDonacionesCierre", lstDonacionesCierre);
				
				sqlSelect = " select sum(montobruto), formadepago,  sum(transacciones) " +
						"from " + PropertiesSystem.ESQUEMA +".dnc_donacion_cns_mpago " +
						"where idcierredonacion in " + idsIn +" group by formadepago ";

				LogCajaService.CreateLog("mostrarDetalleArqueo", "QRY", sqlSelect);
				
				List<Object[]> montosFormaDePago = (ArrayList<Object[]>)
						ConsolidadoDepositosBcoCtrl.executeSqlQuery(sqlSelect, true, null );
				
				for (Object[] dtaFormaPago : montosFormaDePago) {
					
					lstDonacionesFormaPago.add(new DncDonacion( String.valueOf(dtaFormaPago[1]),
							MetodosPagoCtrl.descripcionMetodoPago( String.valueOf(dtaFormaPago[1]) ),
							Integer.parseInt( String.valueOf(dtaFormaPago[2]) ), 
							ar.getId().getMoneda(), new BigDecimal(String.valueOf(dtaFormaPago[0]))));
					
					if(String.valueOf(dtaFormaPago[1]).compareTo(MetodosPagoCtrl.EFECTIVO ) == 0 ){
						dac.setTefectivo(
								new BigDecimal(String.valueOf(dtaFormaPago[0]))
								.add(new BigDecimal( Double.toString(dac.getTefectivo() ))).doubleValue()  
						);
						continue;
					}
					if(String.valueOf(dtaFormaPago[1]).compareTo(MetodosPagoCtrl.TARJETA) == 0 ){
						dac.setTtarcredito(
								new BigDecimal(String.valueOf(dtaFormaPago[0]))
								.add(new BigDecimal( Double.toString( dac.getTtarcredito() ))).doubleValue()  
						);
						continue;
					}
				}
				
				CodeUtil.putInSessionMap("rva_lstDonacionesFormaPago", lstDonacionesFormaPago);
				gvDonacionesFormaPago.dataBind();
				CodeUtil.refreshIgObjects(gvDonacionesFormaPago);
				
			}
			
			//ConsolidadoDepositosBcoCtrl.closeSessionForQuery(true, dtaCnDnc);
			
			lblACDetfp_Efectivo.setValue(dac.getTefectivo());
			lblACDetfp_Cheques.setValue(dac.getTcheque());
			lblCDC_pagochks.setValue(dac.getTcheque());
			lblACDetfp_TarCred.setValue(dac.getTtarcredito());
			lblACDetfp_TCmanual.setValue(dac.getTtarcreditom());
			lblACDetfp_TCsocketpos.setValue(dac.getTtarcreditos());
			lblACDetfp_DepDbanco.setValue(dac.getTdepbanco());
			lblACDetfp_TransBanco.setValue(dac.getTtransbanco());
			
			txtObservacionesArqueo.setValue(Divisas.ponerCadenaenMayuscula(ar.getId().getMotivo()));
			
			dwDetalleArqueo.setWindowState("normal");
			
		}catch(Exception error){
			LogCajaService.CreateLog("mostrarDetalleArqueo", "ERR", error.getMessage());
			error.printStackTrace(); 
		}
		finally {
			LogCajaService.CreateLog("mostrarDetalleArqueo", "INFO", "mostrarDetalleArqueo-INICIO");
			HibernateUtilPruebaCn.closeSession();
		}
	}
	
/****************** 4 cargar el detalle del arqueo procesado *************************/
	@SuppressWarnings("unchecked")
	public DetalleArqueoCaja cargarDetArqueo(String sReferdep, int noarqueo,
			double dDepFinal, Date fecha, Date hora, int caid, String codcomp,
			String codsuc, String moneda, boolean ajustarMinimo) {
		
		AprobacionCierreCtrl rvaCtrl1 = new AprobacionCierreCtrl();
		DetalleArqueoCaja dac = new DetalleArqueoCaja();
		
		try{
			
			//--- DATOS LLAVE DEL ARQUEO --- //
			dac.setCaid(caid);
			dac.setNoarqueo(noarqueo);
			dac.setCodcomp(codcomp);
			dac.setCodsuc(codsuc);	
			dac.setMoneda(moneda);
			dac.setFechaarqueo(fecha);
			dac.setDepositoFinal(dDepFinal);
			dac.setHoraarqueo(hora);
			dac.setDepositoRefer(sReferdep);
			
			
			//&& ============= Cargar la lista de facturas incluidas en el arqueo.
			List<Hfactura> lstVhfact = AprobacionCierreCtrl.cargarFacturasArqueo(noarqueo, caid, codcomp);
			
			if(lstVhfact == null){
				lstVhfact = new ArrayList<Hfactura>();
			}
			
			dac = clasificarFacturas(lstVhfact, dac);
			
			//&& =============  Cargar los recibos incluidos en el arqueo 
			String sLstRecibos = AprobacionCierreCtrl.cargarRecibosArqueo(noarqueo, caid, codcomp, codsuc);

			if(!sLstRecibos.equals("")){
				CodeUtil.putInSessionMap("rv_ListaRecibosArqueo", sLstRecibos);
				
				dac = clasificarRecibos(sLstRecibos, dac, ajustarMinimo);
			}
		}catch(Exception error){
			dac = null;
			dwDetalleArqueo.setWindowState("hidden");
			dwValidarAprobacionArqueo.setWindowState("normal");
			lblValidarAprobacion.setValue("Error al cargar el detalle de arqueo de caja: Error de Aplicacion ");
			error.printStackTrace();
		}finally{
			rvaCtrl1 = null;
		}
		return dac;
	}
	
/******* 5. Separar las facturas, crédito contado y devoluciones *********************/
	public DetalleArqueoCaja clasificarFacturas(List lstVhfact,DetalleArqueoCaja dac){
		List lstFac = new ArrayList(), lstFco = new ArrayList(),lstFcr = new ArrayList();
		List lstDco = new ArrayList(),lstDcr = new ArrayList(),lstDev = new ArrayList();
		double totalFact=0, totalFactco=0, totalFactcr=0;
		double totalDevco = 0, totalDevcr = 0;
		
		try{	
			
			for(int i=0;i<lstVhfact.size();i++){
				Hfactura hFac = (Hfactura)lstVhfact.get(i);				
				String stipoPago = hFac.getTipopago().trim();
				
				
				if(stipoPago.equals("01")||stipoPago.equals("00")||stipoPago.equals("001")){
					if(hFac.getNodoco() == 0 && hFac.getTipodoco().trim().equals("")){//1
						lstFco.add(hFac);
						lstFac.add(hFac);
						totalFactco += hFac.getTotal();	
						totalFact   += hFac.getTotal();
					}else{//2						
						lstDco.add(hFac);
						lstDev.add(hFac);
						totalDevco += hFac.getTotal();					
					}	
				}else{
					
					if(hFac.getNodoco() == 0 && hFac.getTipodoco().trim().equals("")){ //1
						lstFcr.add(hFac);
						lstFac.add(hFac);
						totalFactcr += hFac.getTotal();
						totalFact   += hFac.getTotal(); 							
					}
					
				}
 
			}	
			
			double dDevolucion = 0;
			RevisionArqueoCtrl rvCtrl = new RevisionArqueoCtrl();
			if(lstDev!=null && lstDev.size()>0)
				dDevolucion = rvCtrl.obtenerTotalxdevdia(lstDev, dac.getFechaarqueo());		
					
			dac.setTodasFacturas(lstVhfact);
			dac.setTodasVentas(lstFac);
			dac.setFacContado(lstFco);
			dac.setFacCredito(lstFcr);
			dac.setTotalFact(totalFact);
			dac.setTotalFactco(totalFactco);
			dac.setTotalFactcr(totalFactcr);
			
			dac.setTodasDev(lstDev);
			dac.setTotalDev(dDevolucion); // facturas por dev
			
			dac.setDevContado(lstDco);
			dac.setDevCredito(lstDcr);
			dac.setTotalDevco(totalDevco);
			dac.setTotalDevcr(totalDevcr);
			
			
		}catch(Exception error){
			error.printStackTrace(); 
		}
		return dac;
	}
/******* 6. Clasificar los recibos por tipo y por método de pago *********************/
	public DetalleArqueoCaja clasificarRecibos(String recibos,
			DetalleArqueoCaja dac, boolean ajustarMinimo) {
		List RecibosContado = new ArrayList(), RecibosCredito = new ArrayList();
		List RecibosPrimas  = new ArrayList(), RecibosCambio  = new ArrayList();	
		List RecibosIngEx	= new ArrayList(), RecibosFinan   = new ArrayList();
		double totalreco = 0,totalrecr = 0,totalrepr = 0,totalreex = 0,totalrefn = 0,dTotalcambios = 0; 
		
		double totalrepm = 0 ;
		List<Vrecibosxtipoegreso> recibosAnticiposPM = new ArrayList<Vrecibosxtipoegreso>();
		
		double dPmh=0,dPmn=0,dPm8=0;
		List<Vrecibosxtipoegreso> lstPmh = new ArrayList<Vrecibosxtipoegreso>() ;
		List<Vrecibosxtipoegreso> lstPm8 = new ArrayList<Vrecibosxtipoegreso>() ;
		List<Vrecibosxtipoegreso> lstPmn = new ArrayList<Vrecibosxtipoegreso>() ;
		
		
		int caid;
		Date fechaAr,horaAr;
		String sCodsuc,sCodcomp,sArqueoMon;
		RevisionArqueoCtrl rvCtrl = new RevisionArqueoCtrl();
		AprobacionCierreCtrl rvCtrl1 = new AprobacionCierreCtrl();
		Divisas dv = new Divisas();
		
		//nuevas variables, por cambios en arqueo.
		String sMpago = "", sTiporec = "";
		double dCoh=0,dCon=0,dCo8=0,dCrh=0,dCrn=0,dCr8=0,dPrh=0,dPrn=0,dPr8=0, dMonto = 0;
		double dExh=0,dExn=0,dEx8=0, dFnh=0,dFnn=0,dFn8=0;
		List lstCoh =new ArrayList(),lstCon = new ArrayList(),lstCo8 = new ArrayList();
		List lstCrh =new ArrayList(),lstCrn = new ArrayList(),lstCr8 = new ArrayList();	
		List lstPrh =new ArrayList(),lstPrn = new ArrayList(),lstPr8 = new ArrayList();
		List lstExh =new ArrayList(),lstExn = new ArrayList(),lstEx8 = new ArrayList();
		List lstFnh =new ArrayList(),lstFnn = new ArrayList(),lstFn8 = new ArrayList();
		List lstRecibos;
		
		try{
			// --------- Valores llave del arqueo. --------
			caid = dac.getCaid();
			sCodcomp = dac.getCodcomp();
			sCodsuc = dac.getCodsuc();
			fechaAr = dac.getFechaarqueo();		
			sArqueoMon = dac.getMoneda();
			horaAr = dac.getHoraarqueo();
			
			//-----------------------------------------------------------------------------------------//			
			//----------- clasificar los recibos pagados en bancos por método de pago -----------------//
			//-----------------------------------------------------------------------------------------//
			
			List<Vrecibosxtipoegreso> lstRecpagban = AprobacionCierreCtrl.obtenerRecibosCierre(caid,sCodcomp,sArqueoMon, fechaAr, recibos);
			
			if(lstRecpagban != null && lstRecpagban.size()>0){
				for(int i=0; i<lstRecpagban.size();i++){
					Vrecibosxtipoegreso v = (Vrecibosxtipoegreso)lstRecpagban.get(i);
					v.setMonto(v.getId().getMonto());
					sMpago = v.getId().getMpago().trim();
					sTiporec = v.getId().getTiporec().trim();
					dMonto = dv.roundDouble(v.getId().getMonto().doubleValue());
 
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
					}else //-------------- pago de abonos.
					if(sTiporec.equals("CR")){
						RecibosCredito.add(v);
						totalrecr += dMonto;
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
						RecibosPrimas.add(v);
						totalrepr += dMonto;
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
					}else //----------- Recibos por ingresos extraordinarios.
					if(sTiporec.equals("EX")){
						RecibosIngEx.add(v);
						totalreex += dMonto;
						if(sMpago.equals(MetodosPagoCtrl.TARJETA)){
							lstExh.add(v);
							dExh += dMonto;
						}else
						if(sMpago.equals(MetodosPagoCtrl.DEPOSITO)){
							lstExn.add(v);
							dExn += dMonto;							
						}else
						if(sMpago.equals(MetodosPagoCtrl.TRANSFERENCIA)){
							lstEx8.add(v);
							dEx8 +=dMonto;
						}
					}else //----------- Recibos por pagos a financimiento.
					if(sTiporec.equals("FN")){
						RecibosFinan.add(v);
						totalrefn += dMonto;
						if(sMpago.equals(MetodosPagoCtrl.TARJETA)){
							lstFnh.add(v);
							dFnh += dMonto;
						}else
						if(sMpago.equals(MetodosPagoCtrl.DEPOSITO)){
							lstFnn.add(v);
							dFnn += dMonto;							
						}else
						if(sMpago.equals(MetodosPagoCtrl.TRANSFERENCIA)){
							lstFn8.add(v);
							dFn8 +=dMonto;
						}
					}
					else
						//----------- Recibos por anticipos PMT
						if(sTiporec.equals("PM")){
							 
							recibosAnticiposPM.add(v);
							totalrepm += dMonto;
							
							if(sMpago.equals(MetodosPagoCtrl.TARJETA)){
								lstPmh.add(v);
								dPmh += dMonto;
							}else
							if(sMpago.equals(MetodosPagoCtrl.DEPOSITO)){
								lstPmn.add(v);
								dPmn += dMonto;							
							}else
							if(sMpago.equals(MetodosPagoCtrl.TRANSFERENCIA)){
								lstPm8.add(v);
								dPm8 +=dMonto;
							}
						}
					 
				}
			}			
			//------------------- restar las devoluciones de ventas pagadas en banco.
			List lstDev = rvCtrl1.obtenerRecibosxdevolucion(true,caid, sCodsuc, sCodcomp,"DCO",sArqueoMon, fechaAr, horaAr,recibos);
			if(lstDev != null && lstDev.size()>0){
				double dTdevolucion =0;
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
			
			//-----------------------obtener la lista y calcular el total por cambios realizados.
			lstRecibos = rvCtrl1.cargarDetCambio(caid, sCodcomp, sCodsuc, sArqueoMon, fechaAr,horaAr,recibos);//<<<<...>>>>//
			List lstRecca = new ArrayList();
			dTotalcambios = 0;
			
			if(lstRecibos!=null && lstRecibos.size()>0){	
				String sInclude="";
				String sInclude1="";
				
				for(int i=0;i<lstRecibos.size();i++){
					Vdetallecambiorecibo v = (Vdetallecambiorecibo)lstRecibos.get(i);
					v.setCambio(v.getId().getCambio());
					
					
					if(v.getId().getTiporec().equals("CO")){
						if(v.getId().getMonedacamb().compareTo(sArqueoMon) == 0 && v.getId().getCambio().doubleValue() > 0){
							
							if(v.getId().getCantmon() == 1 && v.getId().getMonedarec().compareTo(sArqueoMon) != 0 ){
									dTotalcambios += dv.roundDouble(v.getId().getCambio().doubleValue());
									lstRecca.add(v);
									sInclude1 += v.getId().getNumrec()+",";
							}
							//&& caso en que el monto del cambio supere al monto del pago en efectivo (ambos en la misma moneda) 
							else if(v.getId().getCantmon() == 2  && !sInclude1.contains(String.valueOf(v.getId().getNumrec()))){	
								
								if(	v.getId().getMonedarec().compareTo(sArqueoMon) == 0 &&
									v.getId().getMonedafac().compareTo(sArqueoMon) != 0 ){
									sInclude1 += v.getId().getNumrec()+",";
									continue;
								} 
								if( v.getId().getMonedarec().compareTo(sArqueoMon) == 0
									&& v.getId().getMonedarec().compareTo(v.getId().getMonedacamb())== 0
									&& v.getId().getCambio().compareTo(v.getId().getMonto()) == 1 ){
								
									v.getId().setCambio(v.getId().getCambio().subtract(v.getId().getMonto()));
									v.setCambio(v.getId().getCambio());
									
									dTotalcambios += dv.roundDouble(v.getId().getCambio().doubleValue());
									lstRecca.add(v);

									sInclude1 += v.getId().getNumrec()+",";
									
								}else{
									if(
										v.getId().getMonedarec().compareTo(sArqueoMon) != 0 &&
										v.getId().getMonedarec().compareTo(v.getId().getMonedafac()) == 0 && 
										v.getId().getCambio().doubleValue() > 0 ){
										
										Transaction trans = null;
										boolean bNuevaSesionCaja = false;
										Session sesion = HibernateUtilPruebaCn.currentSession();
										if( sesion.getTransaction().isActive() )
											trans = sesion.getTransaction();
										else{
											trans = sesion.beginTransaction();
											bNuevaSesionCaja = true;
										}
										
										Criteria cr = sesion.createCriteria(Recibodet.class);
										cr.add(Restrictions.eq("id.caid",caid ))
											.add(Restrictions.eq("id.mpago", MetodosPagoCtrl.EFECTIVO ))
											.add(Restrictions.eq("id.codcomp",sCodcomp ))
											.add(Restrictions.eq("id.codsuc",sCodsuc ))
											.add(Restrictions.eq("id.numrec",v.getId().getNumrec() ))
											.setProjection(Projections.countDistinct("id.moneda") );
										int iCant = Integer.parseInt(String.valueOf(cr.uniqueResult()));
										
										if(bNuevaSesionCaja){
											try{trans.commit(); }catch(Exception e){e.printStackTrace();}
											try{HibernateUtilPruebaCn.closeSession(sesion);}catch(Exception e){e.printStackTrace();}
										}
										if(iCant == 1){
											dTotalcambios += dv.roundDouble(v.getId().getCambio().doubleValue());
											lstRecca.add(v);
											sInclude1 += v.getId().getNumrec()+",";
										}
									}
								}
							}
						}
					}else{
						if(v.getId().getCantmon() == 1){
							dTotalcambios += dv.roundDouble(v.getId().getCambio().doubleValue());
							lstRecca.add(v);
							sInclude += v.getId().getNumrec()+",";
						}else{
							if(v.getId().getCantmon() == 2 && !sInclude.contains(String.valueOf(v.getId().getNumrec()))){
								sInclude += v.getId().getNumrec()+",";
								dTotalcambios += dv.roundDouble(v.getId().getCambio().doubleValue());
								lstRecca.add(v);
							}
						}
					}
				}	
 
				RecibosCambio = lstRecca;			
			}
			
			// ----------------------------------------------------------------------------- //
			// - Cargar los recibos por pagos con monedas distintas de la factura.           //
			// ----------------------------------------------------------------------------- //				
				List lstRecIng = new ArrayList(),lstRecEgre = new ArrayList();
				List<Vreciboxdevolucion> lstDevpme;
				double dTotalirme = 0, dTotalerme = 0;
				
				//Ingresos.		
				lstRecIng = rvCtrl1.obtenerIngEgrRecMonEx(true, caid, sCodcomp, sCodsuc, sArqueoMon, fechaAr, horaAr, recibos);		//<<<<...>>>>//		
				if(lstRecIng!=null && lstRecIng.size()>0){		
					
					String sCompara = "";
					StringBuilder sb = new StringBuilder("");
					
					for(int i=0; i<lstRecIng.size();i++){
						Vmonedafactrec vm = (Vmonedafactrec)lstRecIng.get(i);
					
						//&& ===== Validar que no se repitan los recibos que no son pagos en efectivo
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
						
						if(vm.getId().getTiporec().equals("CO")){	
							
							/* *****************************************
							 * Validación (24/Abril/2010)
							 * Si el cambio es en la misma moneda, restar el cambio y utilizar el monto aplicado al pago.
							 * Si el cambio es en moneda distinta, no restar el cambio y utilizar el monto recibido al pago.
							 */
							if(vm.getId().getCmoneda().equals(sArqueoMon)){
								vm.setMonto(vm.getId().getForarecibido());
								vm.setEquiv(vm.getId().getDomeaplicado());
								vm.setIngresoegreso((vm.getId().getForarecibido()));
								dTotalirme += dv.roundDouble(vm.getId().getForarecibido().doubleValue());
							}else{
								vm.setMonto(vm.getId().getMonto());
								vm.setEquiv(vm.getId().getEquiv());
								vm.setIngresoegreso((vm.getId().getMonto()));
								dTotalirme += dv.roundDouble(vm.getId().getMonto().doubleValue());
							}					
						}else{
							vm.setMonto(vm.getId().getMonto());
							vm.setEquiv(vm.getId().getEquiv());
							vm.setIngresoegreso(vm.getId().getMonto());
							dTotalirme += dv.roundDouble(vm.getId().getMonto().doubleValue());
						}
												
						lstRecIng.remove(i);
						lstRecIng.add(i,vm);					
					}
				}		
				
				//&& =========== cargar las devoluciones de facturas pagadas en diferente moneda //
				lstDevpme = rvCtrl1.obtenerRecxDevmonex(true, caid, sCodsuc, sCodcomp, sArqueoMon, "DCO", fechaAr, horaAr, recibos);	//<<<<...>>>>//				
				if(lstDevpme != null && lstDevpme.size()>0){
					double dTotalDeverme = 0;
					BigDecimal bdTotal = BigDecimal.ZERO;
					
					for (Vreciboxdevolucion v : lstDevpme) 
						bdTotal = bdTotal.add(v.getId().getMonto());
						
					dTotalirme -= dv.roundDouble(bdTotal.doubleValue());
				}
				//&& =========== Otras devoluciones......
				List<Vreciboxdevolucion>lstDev3 = rvCtrl1.obtenerRecxDevmonex3(
											 caid, sCodcomp, sArqueoMon, "DCO", 
											 fechaAr, horaAr, recibos);
				if(lstDev3 != null && !lstDev3.isEmpty()){
					for (Vreciboxdevolucion vr : lstDev3) 
						dTotalirme += vr.getId().getMontoapl().doubleValue();
				}
				dTotalirme = dv.roundDouble(dTotalirme);
				
				
				//&& =========== Egresos		
				lstRecEgre = rvCtrl1.obtenerIngEgrRecMonEx(false, caid, sCodcomp, sCodsuc, sArqueoMon, fechaAr, horaAr, recibos);//<<<<...>>>>//
				if(lstRecEgre!=null && lstRecEgre.size()>0){
					
					String sRecibosIn="";
					String sCompara = "";
					
					for(int i=0; i<lstRecEgre.size();i++){
						Vmonedafactrec vm = (Vmonedafactrec)lstRecEgre.get(i);
						
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
						
						
						
						if(vm.getId().getDomeaplicado().doubleValue()!= 0 && vm.getId().getForarecibido().doubleValue()!= 0){
							vm.setMonto(vm.getId().getForarecibido());
							vm.setEquiv(vm.getId().getDomeaplicado());
							vm.setIngresoegreso((vm.getId().getDomeaplicado()));
							dTotalerme += dv.roundDouble(vm.getId().getDomeaplicado().doubleValue());
						}else{
							vm.setMonto(vm.getId().getMonto());
							vm.setEquiv(vm.getId().getEquiv());
							vm.setIngresoegreso(vm.getId().getEquiv());
							dTotalerme += dv.roundDouble(vm.getId().getEquiv().doubleValue());
						}						
						lstRecEgre.remove(i);
						lstRecEgre.add(i,vm);
					}					
				}	
				//---------------cargar las devoluciones de facturas pagadas en diferente moneda //
				lstDevpme = rvCtrl1.obtenerRecxDevmonex(false, caid, sCodsuc, 
								sCodcomp, sArqueoMon, "DCO", 
								fechaAr, horaAr, recibos);	
				
				if(lstDevpme != null && lstDevpme.size()>0){
					BigDecimal bdTotal = BigDecimal.ZERO;
					
					for(int i=0; i<lstDevpme.size(); i++){
						Vreciboxdevolucion v = (Vreciboxdevolucion)lstDevpme.get(i);		
						
						//&& Valida que la devolucion sea parcial, si es total en los egresos no se incluye 
						//&& el recibo original, entonces la devolucion resta dos veces.
						if(v.getId().getMontofact().compareTo(v.getId().getMontodev()) != 0 )
							bdTotal = bdTotal.add( v.getId().getEquiv() );
					}
					dTotalerme -= dv.roundDouble(bdTotal.doubleValue());
				}
				
				//&& =========== Restar las devoluciones pagadas en monedas distintnas del arqueo de dias anteriores.
				List<Vreciboxdevolucion> lstDevdiasant = rvCtrl1.obtenerRecxDevmonex2
								(false, caid, sCodsuc, sCodcomp,
								sArqueoMon, "DCO", fechaAr, horaAr, recibos);	
				
				if( lstDevdiasant != null && !lstDevdiasant.isEmpty()){
					for(Vreciboxdevolucion v : lstDevdiasant)
						dTotalerme += dv.roundDouble(v.getId().getMonto().doubleValue());
				}
				
				//---------------------------------------------------------------------------------------//
				//-------- Sumar en ingresos los montos por cambios en otras moneda cambio mixto --------//
				//---------------------------------------------------------------------------------------//
				boolean mixto = false;
				double dCambiomixto = 0;
				List lstRecCamixto = new ArrayList();
				BigDecimal dTmp = BigDecimal.ZERO;
				BigDecimal bdCambio = BigDecimal.ZERO;
	
				List<Vdetallecambiorecibo>lstTmp = rvCtrl1.
							obtenerRecibosxcambiomixto(caid, sCodcomp, 
									sArqueoMon, fechaAr, horaAr, recibos);
				
				if(lstTmp != null && !lstTmp.isEmpty()){				
					
					for(int i=0; i<lstTmp.size(); i++){ 
						Vdetallecambiorecibo v = lstTmp.get(i);
						
						mixto = rvCtrl1.isPagoOriginalMixto(v.getId().getCaid(), 
								v.getId().getCodcomp(),v.getId().getNumrec(), sArqueoMon);
						if(!mixto){
							bdCambio =  v.getId().getCambio().divide(v.getId().getTasacambio(),
													2, BigDecimal.ROUND_HALF_EVEN);
							dTmp = dTmp.add(bdCambio);
							v.setCambio(bdCambio);	
							lstRecibos.add(v);
						}else{
							BigDecimal cambio = null;
							if(	v.getId().getMonedarec().compareTo(sArqueoMon) == 0
								&& v.getId().getMonto().compareTo(v.getId().getMontoapl()) == 1 
								&& v.getId().getMonedacamb().compareTo(sArqueoMon) != 0
								&& v.getId().getMpago().compareTo(MetodosPagoCtrl.EFECTIVO ) == 0 ) {
								
								cambio = v.getId().getMonto().subtract(v.getId().getMontoapl());
								v.setCambio(cambio);
								dTmp = dTmp.add(cambio);
								lstRecibos.add(v);
							}
						}
					}
					
					dCambiomixto = dTmp.doubleValue();
				}
				//---------------------------------------------------------------------------------------//
				//-------- calcular los totales por métodos de pago y obtener la lista de recibos -------//
				//---------------------------------------------------------------------------------------//
				List lstRecibosmpago = new ArrayList(),lstDevmpago = new ArrayList(),lstRec = new ArrayList();
				List lstRe5=new ArrayList(),lstReq=new ArrayList(),lstRehe=new ArrayList();
				List lstRehm = new ArrayList(),lstRehs = new ArrayList(), lstRe8=new ArrayList(),lstRen=new ArrayList();						
				double dTotalmp = 0,dTotaldev = 0,dTotalcambio = 0;
				double dRech=0, dRechm=0, dRechs = 0, dRecn=0, dRec8=0, dRec5=0, dRecq=0;
				
				lstRecibosmpago = rvCtrl1.cargarRecibosxMetodoPago(caid, sCodsuc, sCodcomp, sArqueoMon, fechaAr, horaAr, recibos);
				if(lstRecibosmpago != null && lstRecibosmpago.size()>0){
					for(int i=0; i<lstRecibosmpago.size(); i++){
						Vrecibosxtipompago v = (Vrecibosxtipompago)lstRecibosmpago.get(i);
						v.setMonto(v.getId().getMonto());
						lstRecibosmpago.remove(i);
						lstRecibosmpago.add(i,v);
						dTotalmp = dv.roundDouble(v.getId().getMonto().doubleValue());
						sMpago = v.getId().getMpago().trim();
						
						if(sMpago.equals(MetodosPagoCtrl.EFECTIVO )){
							dRec5 += dTotalmp;
							lstRe5.add(v);	
						}else
						if(sMpago.equals(MetodosPagoCtrl.CHEQUE)){
							dRecq += dTotalmp;
							lstReq.add(v);
						}else
							if(sMpago.equals(MetodosPagoCtrl.TARJETA)){
								if(v.getId().getVmanual().equals("0") && v.getId().getRefer6().trim().equals("") && v.getId().getRefer7().trim().equals("")){
									dRech += dTotalmp;
									lstRehe.add(v);
								}else if(v.getId().getVmanual().equals("2") && !v.getId().getRefer6().trim().equals("") && !v.getId().getRefer7().trim().equals("")){
									dRechs += dTotalmp;
									lstRehs.add(v);
								}
								else{
									dRechm += dTotalmp;
									lstRehm.add(v);
								}
							}else
						if(sMpago.equals(MetodosPagoCtrl.TRANSFERENCIA)){
							dRec8 += dTotalmp;
							lstRe8.add(v);
						}else
						if(sMpago.equals(MetodosPagoCtrl.DEPOSITO)){
							dRecn += dTotalmp;
							lstRen.add(v);
						}					
					}
					//obtener las devoluciones por método de pago
					lstDevmpago = rvCtrl1.obtenerRecibosxdevolucion(false,caid, sCodsuc, sCodcomp, "DCO", 
																sArqueoMon, fechaAr, horaAr, recibos);
					if(lstDevmpago!=null && lstDevmpago.size()>0){
						for(int i=0; i<lstDevmpago.size();i++){
							Vreciboxdevolucion v = (Vreciboxdevolucion)lstDevmpago.get(i);
							dTotaldev = dv.roundDouble(v.getId().getMonto().doubleValue());	
							sMpago = v.getId().getMpago();
							
							if(sMpago.equals(MetodosPagoCtrl.EFECTIVO )){
								dRec5 -= dTotaldev;
							}else
							if(sMpago.equals(MetodosPagoCtrl.CHEQUE)){
								dRecq -= dTotaldev;
							}else
								if(sMpago.equals(MetodosPagoCtrl.TARJETA)){
									if(v.getId().getVmanual().equals("0") && v.getId().getRefer6().trim().equals("") && v.getId().getRefer7().trim().equals("")){
										dRech -= dTotaldev;
									}else if(v.getId().getVmanual().equals("2") /*&& !v.getId().getRefer6().trim().equals("") && !v.getId().getRefer7().trim().equals("")*/){
										dRechs -= dTotaldev;
									}
									else{
										dRechm -= dTotaldev;
									}
								}else
							if(sMpago.equals(MetodosPagoCtrl.TRANSFERENCIA)){
								dRec8 -= dTotaldev;
							}else
							if(sMpago.equals(MetodosPagoCtrl.DEPOSITO)){
								dRecn -= dTotaldev;
							}				
						}
					}
					//obtener los cambios realizados para método de pago en efectivo (5).
					lstRec = rvCtrl1.obtenerRecibosxcambios(caid, sCodcomp, sCodsuc, sArqueoMon, fechaAr, horaAr, recibos);
					if(lstRec!= null && lstRec.size()>0){
						dTotalcambio =0;
						for(int i=0; i<lstRec.size();i++){
							Vdetallecambiorec v = (Vdetallecambiorec)lstRec.get(i);
							dTotalcambio += dv.roundDouble(v.getId().getCambio().doubleValue());
						}
						
						dRec5 -= dv.roundDouble(dTotalcambio);
						if(ajustarMinimo && dRec5 < 0){
							dRec5 = 0;
						}
					}
				}
				//---------------------------------------------------------------------------------------//
				//------------- calcular los totales  para las salidas de caja	-------------------------//
				//---------------------------------------------------------------------------------------//
//				String sqlSalida = "";
//				sqlSalida =  " from Vsalida v where v.id.caid = "+caid;
//				sqlSalida += " and trim(v.id.codcomp) = '"+sCodcomp.trim()+"' and trim(v.id.codsuc)= '"+sCodsuc.trim()+"'";
//				sqlSalida += " and v.id.moneda = '"+sArqueoMon+"' and  v.id.estado = 'P'";
//				sqlSalida += " and cast(v.id.faproba as date) = '"+fechaAr+"' and cast(v.id.faproba as time) <= '"+horaAr+"'";
// 				sqlSalida += " and v.id.numsal in " + recibos;				
 				
			String sqlSalidaCaja = "from Salida s where s.id.caid =" + caid
					+ " and trim(s.id.codcomp) = '" + sCodcomp.trim() + "' "
					+ " and moneda = '" + sArqueoMon + "' and estado = 'P' "
					+ " and cast(faproba as date) = '" + fechaAr + "'"
					+ " and s.id.numsal in " + recibos;

			List<Salida> salidasCaja = ConsolidadoDepositosBcoCtrl.executeSqlQuery(sqlSalidaCaja, Salida.class, false);
			 
			List lstSal = null;
			double dtotalSalidas = 0, dTotalSalQ = 0, dTotalSal5 = 0;

			if (salidasCaja != null && !salidasCaja.isEmpty()) {

				lstSal = SalidasCtrl.castSalidaToVSalida(salidasCaja);

				BigDecimal totalsalida = BigDecimal.ZERO;
				BigDecimal totalsalida5 = BigDecimal.ZERO;
				BigDecimal totalsalidaQ = BigDecimal.ZERO;

				for (int i = 0; i < lstSal.size(); i++) {
					Vsalida v = (Vsalida) lstSal.get(i);
					v.setMonto(v.getId().getMonto());
					totalsalida = totalsalida.add(v.getId().getMonto());

					if (v.getId().getOperacion().equals(MetodosPagoCtrl.EFECTIVO )) {
						totalsalida5 = totalsalida5.add(v.getId().getMonto());
						continue;
					}
					if (v.getId().getOperacion().equals(MetodosPagoCtrl.CHEQUE)) {
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
						totalsalidaQ = totalsalidaQ.add(v.getId().getMonto());
						continue;
					}
				}

				dtotalSalidas = dv.roundDouble(totalsalida.doubleValue());
				dTotalSalQ = dv.roundDouble(totalsalidaQ.doubleValue());
				dTotalSal5 = dv.roundDouble(totalsalida5.doubleValue());

				dRec5 -= dtotalSalidas;
				dRecq += dv.roundDouble(dTotalSalQ);
			}
				 
			// Lista y total por cambios mixtos.
			dac.setTotalcambiomixto(dCambiomixto);
			dac.setReccambiomixto(lstRecCamixto);
			dac.setTotalotrosingresos(0);
			dac.setLstotrosingresos(new ArrayList());
			
			// Lista y totales de otros egresos.
			dac.setTotalcambio(dTotalcambios);
			dac.setRecibosCambio(RecibosCambio);
			dac.setTotalotrosegresos( dv.roundDouble(dTotalcambios + dTotalerme + dTotalSal5) );		
			
			//Lista y totales por recibos pagados con monedas distintas al de la fct. 
			dac.setIngrepagotramon(lstRecIng);
			dac.setEgrgrepagotramon(lstRecEgre);
			dac.setTotalinrcpom(dTotalirme);
			dac.setTotalegrcpom(dTotalerme);			
		
			//Lista de recibos por tipo y metodo de pago.
			dac.setRecibosContado(RecibosContado);
			dac.setRecibosCredito(RecibosCredito);
			dac.setRecibosPrimas(RecibosPrimas);
			dac.setRecibosIngEx(RecibosIngEx);
			dac.setRecibosFinan(RecibosFinan);
			dac.setRecibosAnticiposPMT(recibosAnticiposPM);
			
			dac.setRecotc(lstCoh); //- contado
			dac.setRecotb(lstCo8);
			dac.setRecodb(lstCon);
			dac.setRecrtc(lstCrh); //- crédito
			dac.setRecrtb(lstCr8);
			dac.setRecrdb(lstCrn);			
			dac.setReprtc(lstPrh); //- primas.
			dac.setReprtb(lstPr8);
			dac.setReprdb(lstPrn);
			dac.setReextc(lstExh); //- Ing.Extraordinario.
			dac.setReextb(lstEx8);
			dac.setRefndb(lstExn);
			
			dac.setRefntc(lstFnh); //- Financimiento
			dac.setRefntb(lstFn8);
			dac.setRefndb(lstFnn);
			
			dac.setRepmtc(lstPmh); //- anticipos PMT
			dac.setRepmtb(lstPm8);
			dac.setRepmdb(lstPmn);
			
			dCoh = dv.roundDouble(dCoh);//-Contado
			dCo8 = dv.roundDouble(dCo8);
			dCon = dv.roundDouble(dCon);  			
			dCrh = dv.roundDouble(dCrh);//- Crédito
			dCr8 = dv.roundDouble(dCr8);
			dCrn = dv.roundDouble(dCrn);			
			dPrh = dv.roundDouble(dPrh); //-Primas y reservas
			dPr8 = dv.roundDouble(dPr8);
			dPrn = dv.roundDouble(dPrn);			
			dExh = dv.roundDouble(dExh);//-Ingresos Extraordinarios
			dEx8 = dv.roundDouble(dEx8);
			dExn = dv.roundDouble(dExn);
			dFnh = dv.roundDouble(dFnh);//-Pagos a financimientos
			dFn8 = dv.roundDouble(dFn8);
			dFnn = dv.roundDouble(dFnn);
			
			dPmh = dv.roundDouble(dPmh);//-Pagos a anticipos pmt
			dPm8 = dv.roundDouble(dPm8);
			dPmn = dv.roundDouble(dPmn);
			
			//Totales de recibos por tipo y metodo de pago
			dac.setTotalreco(totalreco);//- contado
			dac.setTotalcotc(dCoh);
			dac.setTotalcotb(dCo8);
			dac.setTotalcodb(dCon);
			dac.setTotalrecr(totalrecr);//- crédito			
			dac.setTotalcrtc(dCrh);
			dac.setTotalcrtb(dCr8);
			dac.setTotalcrdb(dCrn);	
			dac.setTotalrepr(totalrepr);//- primas
			dac.setTotalprtc(dPrh);
			dac.setTotalprtb(dPr8);
			dac.setTotalprdb(dPrn);
			dac.setTotalreex(totalreex);//- Ingresos Ex.
			dac.setTotalextc(dExh);
			dac.setTotalextb(dEx8);
			dac.setTotalexdb(dExn);
			dac.setTotalrefn(dv.roundDouble(totalrefn));//- Financimiento.
			dac.setTotalfntc(dFnh);
			dac.setTotalfntb(dFn8);
			dac.setTotalfndb(dFnn);
			
			dac.setTotalrepm(Divisas.roundDouble( totalrepm ) ); //&& ======== anticipos pmt
			dac.setTotalpmtc(dPmh);
			dac.setTotalpmtb(dPm8);
			dac.setTotalpmdb(dPmn);
			
			dac.setDTotalSalida5(dTotalSal5);//- Salidas.
			dac.setDTotalSalidaQ(dTotalSalQ);
			dac.setDTotalsalidas(dtotalSalidas);
			dac.setLstSalidas(lstSal);

			//Totales por método de pago
			dac.setTotalventaspb(dCoh + dCo8 + dCon);
			dac.setTotalabonospb(dCrh + dCr8 + dCrn);
			dac.setTotalprimaspb(dPrh + dPr8 + dPrn);
			dac.setTotalIngextpb(dExh + dEx8 + dExn);
			dac.setTotalfinanpb(dFnh + dFn8 + dFnn);
			dac.setTotalpmpb(dPmh + dPm8 + dPmn);
						
			dac.setTefectivo(dv.roundDouble(dRec5));
			dac.setTtarcredito(dv.roundDouble(dRech));
			dac.setTtarcreditom(dv.roundDouble(dRechm));
			dac.setTtarcreditos(dv.roundDouble(dRechs));
			dac.setTcheque(dv.roundDouble(dRecq));
			dac.setTdepbanco(dv.roundDouble(dRecn));
			dac.setTtransbanco(dv.roundDouble(dRec8));			
			
			//Lista de recibos por método de pago.
			dac.setRtcredito(lstRehe);
			dac.setRtcreditom(lstRehm);
			dac.setRtcreditos(lstRehs);
			dac.setRefectivo(lstRe5);
			dac.setRcheque(lstReq);
			dac.setRtransbanco(lstRe8);
			dac.setRdepbanco(lstRen);
								
		}catch(Exception error){
			dac = null;
			error.printStackTrace();
//			LogCrtl.imprimirError(error);
		}finally{
			rvCtrl = null;
			rvCtrl1 = null;
			dv = null;
		}
		return dac;
	}
/*********** 7.  mostrar la ventana con las facturas del arqueo *********************/
	public void mostrarFacturasArqueo(ActionEvent ev){
		List lstFactura = new ArrayList();
		String titulo = "",sLinkid;
		String sEtCantFco = "Contado", sEtCantFcr = "Crédito", sEtTotalFco = "T.Contado",sEtTotalFcr = "T.Crédito";
		String sCantFco = "", sCantFcr = "", sTotalFco = "",sTotalFcr = "";
		DecimalFormat df = new DecimalFormat("#,###,##0.00");
			
		try{
			if(CodeUtil.getFromSessionMap( "rv_DAC")!=null){
				DetalleArqueoCaja dac = (DetalleArqueoCaja)CodeUtil.getFromSessionMap( "rv_DAC");
				
				sLinkid = ev.getComponent().getId();
				
				if(sLinkid.equals("lnkDetalleVtasDia")){
					lstFactura = dac.getTodasFacturas();
					if(lstFactura == null  || lstFactura.size()== 0)
						return;
					
					titulo = "Facturas por ventas incluidas en arqueo";					
					sCantFco = "" + dac.getFacContado().size();
					sCantFcr = "" + dac.getFacCredito().size();
					sTotalFco =  df.format( dac.getTotalFactco());
					sTotalFcr =  df.format( dac.getTotalFactcr());					
				}
				if(sLinkid.equals("lnkDetalleFactDevoluciones")){							
					lstFactura = dac.getTodasDev();
					if(lstFactura == null  || lstFactura.size()== 0)
						return;
					
					titulo = "Facturas por devoluciones incluidas en arqueo";
					sCantFco = "" + dac.getDevContado().size();
					sCantFcr = "" + dac.getDevCredito().size();					
					sTotalFco =  df.format(dac.getTotalDevco());
					sTotalFcr =  df.format(dac.getTotalDevcr());
				
				}
				if(sLinkid.equals("lnkDetalleVtasCredito")){
					lstFactura = dac.getFacCredito();
					if(lstFactura ==null  || lstFactura.size()==0)
						return;
					
					titulo = "Facturas de crédito incluidas en arqueo";
					sEtCantFco = "";
					sEtCantFcr = ""; 
					sEtTotalFco = "";
					sEtTotalFcr = "";	
					sCantFco = "";
					sCantFcr = "" ;					
					sTotalFco = "" ;
					sTotalFcr = "" ;						
				}
			}
			rv_lblEtCantFacC0 = sEtCantFco;
			rv_lblEtCantFacCr = sEtCantFcr;
			rv_lblEtTotalFacCo = sEtTotalFco;
			rv_blEtTotalFacCr = sEtTotalFcr;
			
			rv_lblCantFacCO  = sCantFco;
			rv_lblCantFacCr  = sCantFcr;	
			rv_lblTotalFacCo = sTotalFco;			
			rv_lblTotalFacCr = sTotalFcr;	
			
			CodeUtil.putInSessionMap("rv_lblEtCantFacC0", sEtCantFco);
			CodeUtil.putInSessionMap("rv_lblEtCantFacCr", sEtCantFcr);
			CodeUtil.putInSessionMap("rv_lblEtTotalFacCo", sEtTotalFco);
			CodeUtil.putInSessionMap("rv_blEtTotalFacCr",sEtTotalFcr);
			CodeUtil.putInSessionMap("rv_lblCantFacCO",sCantFco);
			CodeUtil.putInSessionMap("rv_lblCantFacCr",sCantFcr);
			CodeUtil.putInSessionMap("rv_lblTotalFacCo",sTotalFco);
			CodeUtil.putInSessionMap("rv_lblTotalFacCr",sTotalFcr); 			
			
			CodeUtil.putInSessionMap("rv_lstFacturasRegistradas", lstFactura);
			rv_gvFacturaRegistradas.dataBind();
			rv_gvFacturaRegistradas.setPageIndex(0);			
			hdFactura.setCaptionText(titulo);
			rv_dwFacturas.setWindowState("normal");
						
		}catch(Exception error){
			error.printStackTrace();
		}
	}
/********* 8. Mostrar los recibos p. en banco por métodos de pago ***************************/
	public void cargarRecibos(ActionEvent ev){
		String sLinkid = "",sCodcomp,sCodsuc,sTitulo = "", sTipoRecibos = "";
		RevisionArqueoCtrl rvc = new RevisionArqueoCtrl();
		List lstRec  = new ArrayList();
		int iCaid;		
		
		try{		
			DetalleArqueoCaja dac = (DetalleArqueoCaja)CodeUtil.getFromSessionMap( "rv_DAC");
			iCaid 	 = dac.getCaid();
			sCodcomp = dac.getCodcomp();
			sCodsuc  = dac.getCodsuc();				
			sLinkid = ev.getComponent().getId();
			sTipoRecibos = CodeUtil.getFromSessionMap( "rv_EgresoSolicitado").toString();
			
			if(sTipoRecibos.equals("lnkvtspbanco")){ // recibos por ventas de contado
				sTitulo = "Recibos por ventas de contado";
				if(sLinkid.equals("lnkDetpagoTarjetaCr")){// tarjeta de crédito.  
					lstRec = dac.getVrecotc();
					if(lstRec == null){		
						lstRec = dac.getRecotc();
						lstRec = rvc.obtenerRecibostmp(lstRec, MetodosPagoCtrl.TARJETA,iCaid,sCodcomp,sCodsuc);
						dac.setVrecotc(lstRec);
					}				
				}else
				if(sLinkid.equals("lnkDetpagoDepBanco")){ //Depósito directo en banco.  
					lstRec = dac.getVrecodb();
					if(lstRec == null){		
						lstRec = dac.getRecodb();
						lstRec = rvc.obtenerRecibostmp(lstRec, MetodosPagoCtrl.DEPOSITO,iCaid,sCodcomp,sCodsuc);
						dac.setVrecodb(lstRec);
					}				
				}else
				if(sLinkid.equals("lnkDetpagoTransBanco")){ //Transferencia en banco.  
					lstRec = dac.getVrecotb();
					if(lstRec == null){		
						lstRec = dac.getRecotb();
						lstRec = rvc.obtenerRecibostmp(lstRec, MetodosPagoCtrl.TRANSFERENCIA,iCaid,sCodcomp,sCodsuc);
						dac.setVrecotb(lstRec);
					}				
				}
			}else
			if(sTipoRecibos.equals("lnkabonospbanco")){	  // recibos por abonos.
				sTitulo = "Recibos por pago de abonos";
				if(sLinkid.equals("lnkDetpagoTarjetaCr")){// tarjeta de crédito.  
					lstRec = dac.getVrecrtc();
					if(lstRec == null){		
						lstRec = dac.getRecrtc();
						lstRec = rvc.obtenerRecibostmp(lstRec, MetodosPagoCtrl.TARJETA,iCaid,sCodcomp,sCodsuc);
						dac.setVrecrtc(lstRec);
					}				
				}else
				if(sLinkid.equals("lnkDetpagoDepBanco")){ //Depósito directo en banco.  
					lstRec = dac.getVrecrdb();
					if(lstRec == null){		
						lstRec = dac.getRecrdb();
						lstRec = rvc.obtenerRecibostmp(lstRec, MetodosPagoCtrl.DEPOSITO,iCaid,sCodcomp,sCodsuc);
						dac.setVrecrdb(lstRec);
					}				
				}else
				if(sLinkid.equals("lnkDetpagoTransBanco")){ //Transferencia en banco.  
					lstRec = dac.getVrecrtb();
					if(lstRec == null){		
						lstRec = dac.getRecrtb();
						lstRec = rvc.obtenerRecibostmp(lstRec, MetodosPagoCtrl.TRANSFERENCIA,iCaid,sCodcomp,sCodsuc);
						dac.setVrecrtb(lstRec);
					}				
				}
			}else  
			if(sTipoRecibos.equals("lnkprimaspbanco")){	  // recibos por primas y reservas.
				 sTitulo = "Recibos por pago de primas o reservas"; 
				if(sLinkid.equals("lnkDetpagoTarjetaCr")){// tarjeta de crédito.  
					lstRec = dac.getVreprtc();
					if(lstRec == null){		
						lstRec = dac.getReprtc();
						lstRec = rvc.obtenerRecibostmp(lstRec, MetodosPagoCtrl.TARJETA,iCaid,sCodcomp,sCodsuc);
						dac.setVreprtc(lstRec);
					}				
				}else
				if(sLinkid.equals("lnkDetpagoDepBanco")){ //Depósito directo en banco.  
					lstRec = dac.getVreprdb();
					if(lstRec == null){		
						lstRec = dac.getReprdb();
						lstRec = rvc.obtenerRecibostmp(lstRec, MetodosPagoCtrl.DEPOSITO,iCaid,sCodcomp,sCodsuc);
						dac.setVreprdb(lstRec);
					}				
				}else
				if(sLinkid.equals("lnkDetpagoTransBanco")){ //Transferencia en banco.  
					lstRec = dac.getVreprtb();
					if(lstRec == null){		
						lstRec = dac.getReprtb();
						lstRec = rvc.obtenerRecibostmp(lstRec, MetodosPagoCtrl.TRANSFERENCIA,iCaid,sCodcomp,sCodsuc);
						dac.setVreprtb(lstRec);
					}				
				}
			}else  
			if(sTipoRecibos.equals("lnkingresosexpbanco") || sTipoRecibos.equals("lnkDetalleIngresosEx")){ //Ingresos Extraordinarios.
				 sTitulo = "Recibos por pago de Ingresos Extraordinarios"; 
				if(sLinkid.equals("lnkDetpagoTarjetaCr")){// tarjeta de crédito.  
					lstRec = dac.getVreietc();
					if(lstRec == null){		
						lstRec = dac.getReextc();
						lstRec = rvc.obtenerRecibostmp(lstRec, MetodosPagoCtrl.TARJETA,iCaid,sCodcomp,sCodsuc);
						dac.setVreietc(lstRec);
					}				
				}else
				if(sLinkid.equals("lnkDetpagoDepBanco")){ //Depósito directo en banco.  
					lstRec = dac.getVreiedb();
					if(lstRec == null){		
						lstRec = dac.getReexdb();
						lstRec = rvc.obtenerRecibostmp(lstRec, MetodosPagoCtrl.DEPOSITO,iCaid,sCodcomp,sCodsuc);
						dac.setVreprdb(lstRec);
					}				
				}else
				if(sLinkid.equals("lnkDetpagoTransBanco")){ //Transferencia en banco.  
					lstRec = dac.getVreietb();
					if(lstRec == null){		
						lstRec = dac.getReextb();
						lstRec = rvc.obtenerRecibostmp(lstRec, MetodosPagoCtrl.TRANSFERENCIA,iCaid,sCodcomp,sCodsuc);
						dac.setVreprtb(lstRec);
					}				
				}
			}else  
				if(sTipoRecibos.equals("lnkAnticiposPMTenBanco") ){ // anticipos por pmt
					 sTitulo = "Recibos por pago de Anticipos PMt"; 
					if(sLinkid.equals("lnkDetpagoTarjetaCr")){// tarjeta de crédito.  
						lstRec = dac.getVreietc();
						if(lstRec == null){		
							lstRec = dac.getRepmtc();
							lstRec = rvc.obtenerRecibostmp(lstRec, MetodosPagoCtrl.TARJETA,iCaid,sCodcomp,sCodsuc);
							dac.setVreietc(lstRec);
						}				
					}else
					if(sLinkid.equals("lnkDetpagoDepBanco")){ //Depósito directo en banco.  
						lstRec = dac.getVreiedb();
						if(lstRec == null){		
							lstRec = dac.getRepmtc();
							lstRec = rvc.obtenerRecibostmp(lstRec, MetodosPagoCtrl.DEPOSITO,iCaid,sCodcomp,sCodsuc);
							dac.setVreprdb(lstRec);
						}				
					}else
					if(sLinkid.equals("lnkDetpagoTransBanco")){ //Transferencia en banco.  
						lstRec = dac.getRepmdb();
						if(lstRec == null){		
							lstRec = dac.getReextb();
							lstRec = rvc.obtenerRecibostmp(lstRec, MetodosPagoCtrl.TRANSFERENCIA,iCaid,sCodcomp,sCodsuc);
							dac.setVreprtb(lstRec);
						}				
					}
					
				}
			
		if(lstRec !=null && lstRec.size()>0){
			CodeUtil.putInSessionMap("rv_DAC", dac);
			CodeUtil.putInSessionMap("rv_lstRecxTipoMetpago",lstRec);
			rv_gvRecibosxTipoMetodopago.dataBind();
			rv_gvRecibosxTipoMetodopago.setPageIndex(0);			
			hdDetTrecxMetPago.setCaptionText(sTitulo);
			rv_dwReciboxtipoMetPago.setWindowState("normal");
		}
			
		}catch(Exception error){
			error.printStackTrace();
		}
	}
/********* Mostrar la ventana de totales por recibos pagados en banco y por método de pago ***/
	public void mostrarTotalEgBancoxMetPago(ActionEvent ev){
		String sLinkid = "",sTitulo = "";
		
		try{
			DetalleArqueoCaja dac = (DetalleArqueoCaja)CodeUtil.getFromSessionMap( "rv_DAC");
			sLinkid = ev.getComponent().getId();
			
			if(sLinkid.equals("lnkvtspbanco")){ //Ventas pagadas en banco.
				lblPagoTarjetaCredito.setValue(dac.getTotalcotc());
				lblPagoDepBanco.setValue(dac.getTotalcodb());
				lblPagoTransBanco.setValue(dac.getTotalcotb());
				sTitulo = "Ventas pagadas en bancos";				
			}else
			if(sLinkid.equals("lnkabonospbanco")){ //Abonos pagados en banco.
				lblPagoTarjetaCredito.setValue(dac.getTotalcrtc());
				lblPagoDepBanco.setValue(dac.getTotalcrdb());
				lblPagoTransBanco.setValue(dac.getTotalcrtb());
				sTitulo = "Abonos pagados en bancos";				
			}else
			if(sLinkid.equals("lnkprimaspbanco")){ //primas o reservas pagadas en banco.
				lblPagoTarjetaCredito.setValue(dac.getTotalprtc());
				lblPagoDepBanco.setValue(dac.getTotalprdb());
				lblPagoTransBanco.setValue(dac.getTotalprtb());
				sTitulo = "Primas y Reservas pagadas en bancos";				
			}else
			if(sLinkid.equals("lnkingresosexpbanco") || sLinkid.equals("lnkDetalleIngresosEx")){ //Ingresos Extraordinarios pagados en banco.
				lblPagoTarjetaCredito.setValue(dac.getTotalextc());
				lblPagoDepBanco.setValue(dac.getTotalexdb());
				lblPagoTransBanco.setValue(dac.getTotalextb());
				sTitulo = "Ingresos Extraordinarios pagados en bancos";				
			}else
			if(sLinkid.equals("lnkFinanpbanco")){ //primas o reservas pagadas en banco.
				lblPagoTarjetaCredito.setValue(dac.getTotalfntc());
				lblPagoDepBanco.setValue(dac.getTotalfndb());
				lblPagoTransBanco.setValue(dac.getTotalfntb());
				sTitulo = "Financimientos pagadas en bancos";				
			}else
			if(sLinkid.equals("lnkAnticiposPMTenBanco")){ //anticipos por PMT 
				lblPagoTarjetaCredito.setValue(dac.getTotalpmtc() );
				lblPagoDepBanco.setValue(dac.getTotalpmdb());
				lblPagoTransBanco.setValue(dac.getTotalpmtb());
				sTitulo = "Anticipos PM pagadas en bancos";				
			}
			
			
			hdEgrxmetp.setCaptionText(sTitulo);
			CodeUtil.putInSessionMap("rv_EgresoSolicitado", sLinkid);
			dwEgresosxMetPago.setWindowState("normal");			
			
		}catch(Exception error){
			error.printStackTrace();
		}
	}

/************** mostrar la ventana de tipos de otros egresos de caja ***************/
	public void mostrarOtrosEgresos(ActionEvent ev){	
		try{
			DetalleArqueoCaja dac = (DetalleArqueoCaja)CodeUtil.getFromSessionMap( "rv_DAC");			
			lblTotalEgrRecxmonex.setValue(dac.getTotalegrcpom());			
			lblOEcambios.setValue(dac.getTotalcambio());		
			dwDetOtrosEgresos.setWindowState("normal");
		}catch(Exception error){
			error.printStackTrace();
		}
	}
/************** mostrar la ventana de detalle de cambios realizados ****************/
	public void mostrarDetalleCambios(ActionEvent ev){		
		try{
			DetalleArqueoCaja dac = (DetalleArqueoCaja)CodeUtil.getFromSessionMap( "rv_DAC");
			CodeUtil.putInSessionMap("rv_lstDetalleCambios",dac.getRecibosCambio());
			gvDetalleCambios.setPageIndex(0);
			gvDetalleCambios.dataBind();
			dwDetalleCambios.setWindowState("normal");
		}catch(Exception error){
			error.printStackTrace();
		}
	}
/**************** 9. mostrar los recibos método de pago *****************************/
	public void cargarRecibosmpago(ActionEvent ev){
		int iCaid;
		String sLinkid = "",sCodcomp,sCodsuc,sTextoEncabezado = "",sMoneda = "";
		RevisionArqueoCtrl rvc = new RevisionArqueoCtrl();
		List lstRec  = new ArrayList();		
		DetalleArqueoCaja dac = (DetalleArqueoCaja)CodeUtil.getFromSessionMap( "rv_DAC");
		
		try{
			iCaid    = dac.getCaid();
			sCodcomp = dac.getCodcomp();
			sCodsuc  = dac.getCodsuc();
			sMoneda  = dac.getMoneda();
						
			sLinkid = ev.getComponent().getId();
			 if(sLinkid.equals("lnkACDetfp_Efectivo")){
				 	sTextoEncabezado = "Recibos pagados con efectivo";
					lstRec = dac.getVrefectivo();
					if(lstRec == null){	
						lstRec = dac.getRefectivo();
						lstRec = rvc.obtenerRecibosmpago(lstRec, MetodosPagoCtrl.EFECTIVO ,iCaid,sCodcomp,sCodsuc,sMoneda);
						dac.setVrefectivo(lstRec);
					}
				}			
			 else if(sLinkid.equals("lnkACDetfp_Cheques")){
				 	sTextoEncabezado = "Recibos pagados con cheque";
					lstRec = dac.getVrcheque();
					if(lstRec == null){	
						lstRec = dac.getRcheque();
						lstRec = rvc.obtenerRecibosmpago(lstRec, MetodosPagoCtrl.CHEQUE,iCaid,sCodcomp,sCodsuc,sMoneda);
						dac.setVrcheque(lstRec);
					}
				}
			 else if(sLinkid.equals("lnkACDetfp_TarCred")){
				 	sTextoEncabezado = "Recibos de pago por ventas con tarjeta de crédito vouchers electrónicos";
					lstRec = dac.getVrtcredito();
					if(lstRec == null){	
						lstRec = dac.getRtcredito();
						lstRec = rvc.obtenerRecibosmpago(lstRec, MetodosPagoCtrl.TARJETA,iCaid,sCodcomp,sCodsuc,sMoneda);
						dac.setVrtcredito(lstRec);
					}
				}
			 else if(sLinkid.equals("lnkACDetfp_TarCredManual")){
				 	sTextoEncabezado = "Recibos de pago por ventas con tarjeta de crédito vouchers manuales";
					lstRec = dac.getVrtcreditom();
					if(lstRec == null){	
						lstRec = dac.getRtcreditom();
						lstRec = rvc.obtenerRecibosmpago(lstRec, MetodosPagoCtrl.TARJETA,iCaid,sCodcomp,sCodsuc,sMoneda);
						dac.setVrtcreditom(lstRec);
					}
			 	}	
			 else if(sLinkid.equals("lnkACDetfp_Tarsocketpos")){
				 	sTextoEncabezado = "Recibos de pago por ventas con tarjeta de crédito Socketpos";
					lstRec = dac.getVrtcreditos();
					if(lstRec == null){	
						lstRec = dac.getRtcreditos();
						lstRec = rvc.obtenerRecibosmpago(lstRec, MetodosPagoCtrl.TARJETA,iCaid,sCodcomp,sCodsuc,sMoneda);
						dac.setVrtcreditos(lstRec);
					}
			 	}	
			 else if(sLinkid.equals("lnkACDetfp_TransBanco")){
				 	sTextoEncabezado = "Recibos pagados con transferencia en banco";
					lstRec = dac.getVrtransbanco();
					if(lstRec == null){	
						lstRec = dac.getRtransbanco();
						lstRec = rvc.obtenerRecibosmpago(lstRec, MetodosPagoCtrl.TRANSFERENCIA,iCaid,sCodcomp,sCodsuc,sMoneda);
						dac.setVrtransbanco(lstRec);
					}
				}	
			 else if(sLinkid.equals("lnkACDetfp_DepDbanco")){
				 	sTextoEncabezado = "Recibos pagados Depósito en banco";
					lstRec = dac.getVrdepbanco();
					if(lstRec == null){	
						lstRec = dac.getRdepbanco();
						lstRec = rvc.obtenerRecibosmpago(lstRec, MetodosPagoCtrl.DEPOSITO,iCaid,sCodcomp,sCodsuc,sMoneda);
						dac.setVrdepbanco(lstRec);
					}
				}
			if(lstRec !=null && lstRec.size()>0){
				CodeUtil.putInSessionMap("rv_DAC", dac);
				CodeUtil.putInSessionMap("rv_lstRecxTipoMetpago",lstRec);
				rv_gvRecibosxTipoMetodopago.dataBind();
				rv_gvRecibosxTipoMetodopago.setPageIndex(0);			
				hdDetTrecxMetPago.setCaptionText(sTextoEncabezado);
				rv_dwReciboxtipoMetPago.setWindowState("normal");
			}			
		}catch(Exception error){
			error.printStackTrace();
		}
	}
/**************** 10. mostrar el detalle de la factura *****************************/
	public void mostrarDetalleFactura(ActionEvent ev){
		ArqueoCajaCtrl acCtrl = new ArqueoCajaCtrl();
		DetalleArqueoCaja dac = (DetalleArqueoCaja)CodeUtil.getFromSessionMap( "rv_DAC");
		
		try{			
			RowItem ri = (RowItem)ev.getComponent().getParent().getParent();
			List lstA = (List) ri.getCells();		
			Cell c = (Cell) lstA.get(1);//Columna a obtener: No. Factura
			HtmlOutputText noFactura = (HtmlOutputText) c.getChildren().get(0);
			int iNoFactura = Integer.parseInt(noFactura.getValue().toString());
			
			c = (Cell) lstA.get(2);//Columna a obtener: Tipo de documento
			HtmlOutputText TipoDoc = (HtmlOutputText) c.getChildren().get(0);
			String sTipoDoc = TipoDoc.getValue().toString();
			
//			List lstFacsActuales = (List) CodeUtil.getFromSessionMap( "lstFacturasRegistradas");
			List lstFacsActuales  = dac.getTodasFacturas();
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
					rv_lstCierreCajaDetfactura = faCtrl.formatDetalle(hFac);	
					hFac.setDfactura(rv_lstCierreCajaDetfactura);
					CodeUtil.putInSessionMap("rv_lstCierreCajaDetfactura",rv_lstCierreCajaDetfactura);					
				
					txtIva = hFac.getTotal() - hFac.getSubtotal();
					txtTotal = divisas.formatDouble(((Hfactura)lstFacsActuales.get(i)).getTotal());
				
					rv_lstMonedasDetalle = new ArrayList();
					String moneda = ((Hfactura)lstFacsActuales.get(i)).getMoneda();
					if(moneda.equals("COR")){
						rv_lstMonedasDetalle.add(new SelectItem("COR","COR"));
						txtTasaDetalle.setStyle("visibility: hidden");
						lblTasaDetalle.setStyle("visibility: hidden");
						
					}else{
						rv_lstMonedasDetalle.add(new SelectItem(moneda,moneda));
						rv_lstMonedasDetalle.add(new SelectItem("COR","COR"));
						lblTasaDetalle.setValue("Tasa de Cambio: ");
						lblTasaDetalle.setStyle("visibility: visible");
						lblTasaDetalle.setStyleClass("frmLabel2");
						txtTasaDetalle.setStyle("visibility: visible");
						txtTasaDetalle.setStyleClass("frmLabel3");
					}
					//poner la factura al mapa para utilizar en caso de cambiar moneda.
					CodeUtil.putInSessionMap("Hfactura", hFac);
					List oldDfac = hFac.getDfactura();
					CodeUtil.putInSessionMap("oldDfac",oldDfac);
					CodeUtil.putInSessionMap("rv_lstMonedasDetalle",rv_lstMonedasDetalle);
					rv_ddlDetalleFacCon.dataBind();					
					gvDfacturasDiario.dataBind();
					break;					
				}
			}
			rv_dwDetalleFactura.setWindowState("normal");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
/************* 11. Cambiar los valores por tasa de cambio de moneda *****************/
	public void cambiarMonedaDetalle(ValueChangeEvent ev){
		Divisas divisas = new Divisas();		
		Hfactura hFac = (Hfactura)CodeUtil.getFromSessionMap( "Hfactura");
		List lstDetalle = hFac.getDfactura();
		Dfactura dFac = null;
		List lstNewDetalle = new ArrayList();
		String monedaActual = rv_ddlDetalleFacCon.getValue().toString();
		double dTasa;
		
		
		try{			
			if(monedaActual.equals("COR")){
				dTasa = hFac.getTasa().doubleValue();
				txtSubtotal = hFac.getSubtotal() * dTasa;
				txtIva		= hFac.getIva() * dTasa;
				txtTotal 	= divisas.formatDouble(hFac.getTotal() *dTasa) + "";
				for (int i = 0; i < lstDetalle.size(); i++){
					dFac = (Dfactura)lstDetalle.get(i); 
					dFac.setPreciounit(dFac.getPreciounit() * dTasa);
					lstNewDetalle.add(dFac);
				}				
			}else {
				dTasa = dFac.getTasa().doubleValue();
				txtSubtotal = hFac.getSubtotal();
				txtIva 		= hFac.getIva();
				txtTotal 	= divisas.formatDouble(hFac.getTotal());
				for (int i = 0; i < lstDetalle.size(); i++){
					dFac = (Dfactura)lstDetalle.get(i); 
					dFac.setPreciounit(dFac.getPreciounit()/dTasa);
					lstNewDetalle.add(dFac);
				}
			}		
			CodeUtil.putInSessionMap("rv_lstCierreCajaDetfactura",lstNewDetalle);			
			gvDfacturasDiario.dataBind();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
/************* 12. Mostrar los recibos por tipo de ingreso **************************/
	public void mostrarRecibos(ActionEvent ev){
		int iCaid;
		String sLinkid = "",sCodcomp,sCodsuc,sTitulo = "";
		RevisionArqueoCtrl rvc = new RevisionArqueoCtrl();		
		List lstRecibos = new ArrayList();
		DetalleArqueoCaja dac; 
		
		try{
			dac = (DetalleArqueoCaja)CodeUtil.getFromSessionMap( "rv_DAC");
			iCaid = dac.getCaid();
			sCodcomp = dac.getCodcomp();
			sCodsuc = dac.getCodsuc();			
			sLinkid = ev.getComponent().getId();
			
			if(sLinkid.equals("lnkDetalleAbonos")){				
				sTitulo = "Recibos por pagos a ventas de crédito";
				lstRecibos = dac.getRecibosCredito();
				lstRecibos = rvc.obtenerRecibosxTipo(lstRecibos, "CR", iCaid, sCodcomp, sCodsuc);				
			}else
			if(sLinkid.equals("lnkDetallePrimasReservas")){				
				sTitulo = "Recibos por pagos de primas y reservas";
				lstRecibos = dac.getRecibosPrimas();
				lstRecibos = rvc.obtenerRecibosxTipo(lstRecibos, "PR", iCaid, sCodcomp, sCodsuc);				
			}else
			if(sLinkid.equals("lnkDetalleIngresosEx")){				
				sTitulo = "Recibos por pagos de Ingresos Extraordinarios";
				lstRecibos = dac.getRecibosIngEx();
				lstRecibos = rvc.obtenerRecibosxTipo(lstRecibos, "EX", iCaid, sCodcomp, sCodsuc);				
			}else
			if(sLinkid.equals("lnkDetalleFinanciamiento")){				
				sTitulo = "Recibos por pagos a financimientos";
				lstRecibos = dac.getRecibosIngEx();
				lstRecibos = rvc.obtenerRecibosxTipo(lstRecibos, "FN", iCaid, sCodcomp, sCodsuc);				
			}else
				
			if(sLinkid.equals("lnkDetalleRecibosPMT")){				
				sTitulo = "Recibos por Anticipos PMT";
				lstRecibos = dac.getRecibosAnticiposPMT();
				lstRecibos = rvc.obtenerRecibosxTipo(lstRecibos, "PM", iCaid, sCodcomp, sCodsuc);				
			}	
				
			if(lstRecibos !=null && !lstRecibos.isEmpty() ){
				CodeUtil.putInSessionMap("rv_lstRecibosxIngresos", lstRecibos);
				gvRecibosIngresos.dataBind();
				gvRecibosIngresos.setPageIndex(0);			
				hdRecxTipoIng.setCaptionText(sTitulo);
				dwRecibosxTipoIngreso.setWindowState("normal");
			}
				
		}catch(Exception error){
			error.printStackTrace();
//			LogCrtl.imprimirError(error);
		}
	}

/************* 13. cargar los datos para mostrar el detalle del recibo *********************/
	public void mostrarDetalleRecibo(ActionEvent ev){
		int iNumrec,iCaid;
		String sCodcomp, sCodsuc;
		DetalleArqueoCaja dac;
		
		try{		
			
			RowItem ri = (RowItem) ev.getComponent().getParent().getParent();
			final Vrecibosxtipoingreso rc = (Vrecibosxtipoingreso) DataRepeater.getDataRow(ri);
			
			
			mostrarDetalleRecibo(rc.getId().getCaid(), rc.getId().getCodcomp(), rc.getId().getCodsuc(), rc.getId().getNumrec(), rc.getId().getTiporec() );		
			
			
/*			RowItem riRecibo = (RowItem)ev.getComponent().getParent().getParent();
			List lstFilaRec = riRecibo.getCells();
			Cell celdaCodrec = (Cell)lstFilaRec.get(1);
			HtmlOutputText hoCodrec = (HtmlOutputText)celdaCodrec.getChildren().get(0);
			iNumrec = Integer.parseInt(hoCodrec.getValue().toString());
			
			dac = (DetalleArqueoCaja)CodeUtil.getFromSessionMap( "rv_DAC");
			iCaid = dac.getCaid();
			sCodcomp = dac.getCodcom */
			
//			mostrarDetalleRecibo(iCaid, sCodcomp, sCodsuc, iNumrec);		
			
		}catch(Exception error){
			error.printStackTrace();
		}		
	}
/********** 13.1 Mostrar el detalle del recibo seleccionado **************************/
	public void mostrarDetalleRecibo(int iCaid, String sCodcomp, String sCodsuc,int iNumrec, String tiporec ){
		ReciboCtrl recCtrl = new ReciboCtrl();	
		
		try{			
			//--------------------------------------------------------------------------
			Recibo recibo = recCtrl.obtenerRegRecibo(iNumrec,iCaid,sCodcomp,sCodsuc, tiporec);
			if(recibo!=null){
				
				//-------- llenar la cabecera del recibo ---------- //
				txtHoraRecibo.setValue(recibo.getHora()+"");
				txtNoRecibo.setValue(iNumrec);
				txtDRCodCli.setValue(recibo.getCodcli() + " (Código)");
				txtDRNomCliente.setValue(recibo.getCliente()+ " (Nombre)");
				txtMontoAplicar.setValue(recibo.getMontoapl());
				txtMontoRecibido.setValue(recibo.getMontorec());
				txtConcepto.setValue(recibo.getConcepto());
				
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
				
			/*	//-------- leer numero de batch del recibo---------//
				int iNoBatch = recCtrl.leerNoBatchRecibo(iCaid, sCodsuc, sCodcomp, iNumrec,"R",recibo.getId().getTiporec());
				txtNoBatch.setValue(iNoBatch);*/
				
				//---------  leer detalle de recibo -------------- //
				List lstDetalleRecibo = recCtrl.leerDetalleRecibo(iCaid, sCodsuc, sCodcomp, iNumrec,recibo.getId().getTiporec());				
				MetodosPagoCtrl metCtrl = new MetodosPagoCtrl();  //poner descripcion a metodos
				Recibodet recibodet = null;
				for(int m = 0; m < lstDetalleRecibo.size();m++){
					recibodet = (Recibodet)lstDetalleRecibo.get(m);
					recibodet.getId().setMpago(metCtrl.obtenerDescripcionMetodoPago(recibodet.getId().getMpago().trim()));
				}
				CodeUtil.putInSessionMap("rv_lstDetalleRecibo", lstDetalleRecibo);
				rv_gvDetalleRecibo.dataBind();
				
				//---------  leer detalle de cambio del recibo -------------- //
				Cambiodet[] cambio = recCtrl.leerDetalleCambio(iCaid, sCodsuc, sCodcomp, iNumrec, tiporec);				
				String sCambio = "";
				if(cambio!=null){
					for(int i = 0; i < cambio.length;i++){
						sCambio = sCambio + cambio[i].getId().getMoneda() + " " + cambio[i].getCambio() + "<br/>";
					}
				}
				txtDetalleCambio.setValue(sCambio);
				
				//---------  leer detalle de Facturas aplicadas al recibo -------------- //	
				lblNoFactura2.setValue("No. Factura");
				lblTipofactura2.setValue("Tipo Fac.");
				lblUnineg2.setValue("Unidad de Negocios");
				lblMoneda2.setValue("Moneda");
				lblFecha23.setValue("Fecha");
				lblPartida23.setValue("Partida");	
				coNoFactura2.setRendered(true);
				rv_gvFacturasRecibo.setRendered(true);
				
				List lstFacturasRecibo = new ArrayList();
				String sTiporec = recibo.getId().getTiporec();
				
				if(sTiporec.equals("CR"))				
					lstFacturasRecibo = recCtrl.leerFacturasReciboCredito(iCaid, 
								sCodcomp, iNumrec,sTiporec, recibo.getCodcli());
				else 
				if(sTiporec.equals("CO"))
					lstFacturasRecibo = recCtrl.leerFacturasRecibo(iCaid, sCodcomp, 
										iNumrec, sTiporec, recibo.getCodcli(), 
										new FechasUtil().DateToJulian(recibo.getFecha()));
				else
				if(sTiporec.equals("PR")){
						lstFacturasRecibo = recCtrl.leerDatosBien(iNumrec,iCaid, sCodcomp,sCodsuc);
						lblNoFactura2.setValue("");
						lblTipofactura2.setValue("T.Producto");
						lblUnineg2.setValue("Marca");
						lblMoneda2.setValue("Modelo");
						lblFecha23.setValue("Referecia");
						lblPartida23.setValue("");	
						coNoFactura2.setRendered(false);
				}
				else
				if(sTiporec.equals("EX")){
					rv_gvFacturasRecibo.setRendered(false);
				}
				
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
					
					CodeUtil.putInSessionMap("rva_detalleContratoPmt", detalleContratoPmt);
					gvDetalleContratoPmt.dataBind();
					
				}else{
					CodeUtil.putInSessionMap("rv_lstFacturasRecibo", lstFacturasRecibo);
					rv_gvFacturasRecibo.dataBind();
				}
				
				rv_dwDetalleRecibo.setWindowState("normal");
			}
			
			
		}catch(Exception error){
			error.printStackTrace();
//			LogCrtl.imprimirError(error);
		}
	}
	
/********* Mostrar el detalle de otros ingresos del día. ************************************/
	public void mostrarDetalleOtrosIngresos(ActionEvent ev){
		DetalleArqueoCaja dac;
		
		try{
			dac = (DetalleArqueoCaja)CodeUtil.getFromSessionMap( "rv_DAC");			
			lblIngresosExtraOrd.setValue(dac.getTotalotrosingresos());
			lblCambioOtraMoneda.setValue(dac.getTotalcambiomixto());			
			dwDetalleOtrosIngresos.setWindowState("normal");
			
		}catch(Exception error){
			error.printStackTrace();
		}
	}
	
/********** 13.1 mostrar la ventana de confirmación de proceso de arqueo *******************/
	public void cargarRecpagOtrasMonedas(ActionEvent ev){
		String sLinkId = "",sTitulo = "";
		List lstRecibos = new ArrayList();
		
		try{
			DetalleArqueoCaja dac = (DetalleArqueoCaja)CodeUtil.getFromSessionMap( "rv_DAC");			
			sLinkId = ev.getComponent().getId();
			
			if(sLinkId.equals("lnkDetIngxRecPagMonEx")){
				lstRecibos = dac.getIngrepagotramon();
				sTitulo = "Ingresos por recibos pagados con diferente moneda";
			}
			else if(sLinkId.equals("lnkDet")){
				lstRecibos = dac.getEgrgrepagotramon();
				sTitulo = "Egresos por recibos pagados con diferente moneda";
			}
			
			if(lstRecibos!=null  && lstRecibos.size()>0){
				CodeUtil.putInSessionMap("rv_lstDetRecpagMonEx", lstRecibos);
				gvDetRecpagMonEx.dataBind();
				gvDetRecpagMonEx.setPageIndex(0);	
				hdDetrecpagmonEx.setCaptionText(sTitulo);
				dwDetallerecpagmonEx.setWindowState("normal");
			}			
		}catch(Exception error){
			error.printStackTrace();
		}
	}
/********** 14. mostrar la ventana de confirmación de proceso de arqueo *******************/
	public void confAprobarArqueo(ActionEvent ev){
		String msg = "";
					
		try{
 
			//&& ============== Validar que sea un contador o administrador
			Vautoriz vaut = ( (Vautoriz[]) CodeUtil.getFromSessionMap( "sevAut") )[0];
			boolean autorizado = 
					vaut.getId().getCodper().compareTo("P000000004") == 0 ||
					vaut.getId().getCodper().compareTo("P000000015") == 0 ||
					vaut.getId().getCodper().compareTo("P000000025") == 0 ;

			if(!autorizado){
				msg = "No tiene autorización para realizar esta operación" ;
				return;
			}
			
			if( CodeUtil.getFromSessionMap("rva_VARQUEO")  == null ){
				msg = "Los valores del arqueo no han sido encontrados, cierre y recargue nuevamente los datos para aprobación" ;
				return;
			} 
			
			Varqueo v = (Varqueo) CodeUtil.getFromSessionMap("rva_VARQUEO");
			
			if( v.getId().getEstado().compareTo("P") != 0 ){
				msg = "El estado del arqueo debe ser 'PENDIENTE' para ser procesado" ;
				return;
			}
			if( CodeUtil.getFromSessionMap ("rva_pasoAprobarArqueo") == null || ! CodeUtil.getFromSessionMap ("rva_pasoAprobarArqueo").toString().equals("1")){
				msg = "Debe de verificar la opción de Asignar referencias de depósitos de afiliados" ;
				return;
			}
			if( v.getId().getCodcomp().trim().compareTo("E03") == 0 && (   
				 CodeUtil.getFromSessionMap ("rv_bvalidoCheques") == null ||  
				 CodeUtil.getFromSessionMap ("rv_bvalidoCheques").toString().compareTo("false") == 0 )) {
				 
				msg = "Debe de verificar que se hayan registrado las referencias de cheques" ;
				return;
			}
			
		}catch(Exception error){
			error.printStackTrace();
//			LogCrtl.imprimirError(error);
		}finally{
			
			if(msg.isEmpty()){
				rv_dwConfirmarProcesarArq.setWindowState("normal");		
			}else{
				dwValidarAprobacionArqueo.setWindowState("normal");
				lblValidarAprobacion.setValue(msg);
			}
			
			HibernateUtilPruebaCn.closeSession();
		}
	}	
/********** 15. Mostrar la ventana de confirmación para cancelar la aprobación del arqueo. *****/
	public void confirmarCancelarAprobArq(ActionEvent ev){
		try{
			rv_dwCancelarAprArqueo.setWindowState("normal");
		}catch(Exception error){
			error.printStackTrace();
		}
	}
	public String[] obtenerCuentasTransitorias(String sCodcomp, String sMoneda, int iIdBanco){
		String sCtaBcoTransitoria[] = new String[6];
		
		try {
			Divisas dv = new Divisas();
			sCtaBcoTransitoria  = dv.obtenerCuentaTransitoBanco(sMoneda, sCodcomp, iIdBanco, null); 
			if(sCtaBcoTransitoria == null)
				errorApr = dv.getError();
			
		} catch (Exception e) {
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
			e.printStackTrace();
		}
		return sCtaBcoTransitoria;
	}
	
/********** 18. Aprobación del arqueo de caja. ************************************************/
	public void aprobarArqueoCaja(ActionEvent ev){
		boolean bHecho = true, bCredito=true;
		double dTotal5,dTotalQ;
		String sLstRecibos,sCodcomp,sCodsuc,sMensaje="";
		Divisas dv = new Divisas();
		int iCaid;
		List lstRecibosN8,lstRecibosH;
		double bdMontoNetoPos[]= new double[1];
		String sNoReferPos[]= new String[1];
		String sAfiliados[] = new String[1];
		String sUninegPOS[]= new String[1];
		int iNoReferPos[] = new int[1];
		BigDecimal bdMtoComis[] = new BigDecimal[1];
		BigDecimal bdMtoPos[] = new BigDecimal[1];
		int iCaidPOS[]= new int[1];
		String[] marcaTarjeta = new String[1];
		
		List lstDepositos = new ArrayList();
		
		RevisionArqueoCtrl rvCtrl = new RevisionArqueoCtrl();
		F55ca014 f14 = null;
		
		
		
		String strDtaAprCierre ="";
		String strEventhashcode = "";
		SimpleDateFormat timelog = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");	
		
		boolean incluyeDonaciones = false;
		boolean incluyeDonacionesTc = false;
		List<DncCierreDonacion>cierreDonaciones = null;
		
		Session sessionAprArqueo = null;
		Transaction transactionAprArqueo = null;
		
		
		try{
			
			rv_dwConfirmarProcesarArq.setWindowState("hidden");
			dwCargando.setWindowState("hidden");
			Vautoriz[] vaut = (Vautoriz[]) CodeUtil.getFromSessionMap( "sevAut");
			
			CodeUtil.removeFromSessionMap("rv_listaDepositos") ;
			
			if(CodeUtil.getFromSessionMap( "aprar_processRecibo")==null){
				CodeUtil.putInSessionMap("aprar_processRecibo","1");
				
				//**** Datos para asientos de depósitos
				List lstResumenAfiliado = (List)CodeUtil.getFromSessionMap( "rva_lstResumenAfiliado");
				bCredito = lstResumenAfiliado != null && !lstResumenAfiliado.isEmpty() ; 
								
				DetalleArqueoCaja dac = (DetalleArqueoCaja)CodeUtil.getFromSessionMap( "rv_DAC");
				iCaid = dac.getCaid();
				sCodcomp = dac.getCodcomp();
				sCodsuc = dac.getCodsuc();
				
				sLstRecibos = "(0)" ;
				if(CodeUtil.getFromSessionMap("rv_ListaRecibosArqueo") != null )
					sLstRecibos = CodeUtil.getFromSessionMap("rv_ListaRecibosArqueo").toString();
				
				strEventhashcode = "{{"+ ev.hashCode() + " }}" ;
				strDtaAprCierre = "Caja:'"+iCaid+"'"+" || Compania: '"
						+sCodcomp+"'"+" || Arqueo: '"+dac.getNoarqueo()
						+"' Moneda: '"+dac.getMoneda()+"' || Usuario '"
						+ vaut[0].getId().getLogin()+"'" ;
				
				
				f14 = CompaniaCtrl.obtenerF55ca014(iCaid, sCodcomp);
				if(f14==null){
					rv_dwConfirmarProcesarArq.setWindowState("hidden");
					dwValidarAprobacionArqueo.setWindowState("normal");	
					sMensaje = "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" />";
					sMensaje = "Error al obtener la configuración de la compañía "+sCodcomp+" para la caja "+iCaid;
					lblValidarAprobacion.setValue(sMensaje+"<br>");
					return;
				}
				
				
				//&& ====================================== Datos de las donaciones
				
				incluyeDonaciones = CodeUtil.getFromSessionMap("rv_CierreDonaciones") != null ;
				if( incluyeDonaciones ){
					cierreDonaciones = (ArrayList<DncCierreDonacion>) CodeUtil.getFromSessionMap("rv_CierreDonaciones") ;
				}
				incluyeDonacionesTc =  CodeUtil.getFromSessionMap( "rva_TotalesDonacionAfiliado" ) != null;
				
				//================================================================
				
				
				//&& =======================  unica conexion para todo el procedimiento =============== &&/
				sessionAprArqueo = HibernateUtilPruebaCn.currentSession();
				transactionAprArqueo = sessionAprArqueo.beginTransaction();
				//================================================================
				
				
				//********************** Cuentas a utilizar, transitorias a banco.
				String sCtunineg="",sCtaObj="",sCtaSub="";
				String sCtaBcoTransitoria[] = new String[6];
				Vf0901 vCtaBcoTransitoria = null;
				

				sCtaBcoTransitoria = dv.obtenerCuentaTransitoBanco (dac.getMoneda(), dac.getCodcomp(), f14.getId().getC4bnc(), sessionAprArqueo) ; 
				if(sCtaBcoTransitoria == null){
					sMensaje =  "No se pudo obtener las cuentas transitorias de banco " +  f14.getId().getC4bnc() ;
				}
				
				//********** Montos para efectivo y cheques.
				BigDecimal bdMonto5 = dv.roundBigDecimal(new BigDecimal(lblCDC_depositoFinal.getValue().toString()));
				dTotalQ = Double.parseDouble(lblACDetfp_Cheques.getValue().toString().trim());
				dTotal5 = dv.roundDouble(bdMonto5.doubleValue());
				dTotalQ = dv.roundDouble(dTotalQ);
				
				//--------- Montos para transacciones de depósito y transferencia en banco.				
				lstRecibosN8 = rvCtrl.obtenerRecibosSinDev(iCaid, sCodcomp, sCodsuc, 
						dac.getMoneda(), "('"+MetodosPagoCtrl.DEPOSITO+"','"+MetodosPagoCtrl.TRANSFERENCIA+"')",
						sLstRecibos, dac.getFechaarqueo(), sessionAprArqueo);
				
				//--------- Montos para el depósito por afiliados.
				if(bCredito){
					
					lstRecibosH = rvCtrl.obtenerMontoPOSxUN(iCaid, sCodcomp, sCodsuc, dac.getMoneda(), sLstRecibos, dac.getFechaarqueo(), cierreDonaciones, sessionAprArqueo );
					
					bCredito = lstRecibosH != null && !lstRecibosH.isEmpty() ;
					
					if ( bCredito ) {
						
						BigDecimal bdComis  = BigDecimal.ONE;
						BigDecimal bMonto = BigDecimal.ONE;
						BigDecimal bdMontoComisionable = BigDecimal.ONE;
										
						bdMontoNetoPos  = new double[lstRecibosH.size()];
						iNoReferPos= new int[lstRecibosH.size()];
						sNoReferPos= new String[lstRecibosH.size()];
						sAfiliados = new String[lstRecibosH.size()];
						sUninegPOS = new String[lstRecibosH.size()];
						bdMtoComis = new BigDecimal[lstRecibosH.size()];
						bdMtoPos   = new BigDecimal[lstRecibosH.size()];
						iCaidPOS   = new  int[lstRecibosH.size()];
						marcaTarjeta = new String[lstRecibosH.size()] ;
						
						for(int i=0; i<lstRecibosH.size();i++){
							Object[] obPos= (Object[]) lstRecibosH.get(i);
							bdMtoPos[i]	  = (BigDecimal)obPos[0];
							bdComis		  = (BigDecimal)obPos[1];
							sAfiliados[i] = (String)obPos[2];					
							iNoReferPos[i]= Integer.parseInt( (String)obPos[3] );
							sNoReferPos[i]=   (String)obPos[3] ;
							sUninegPOS[i] = ((String)obPos[4]).trim();
							iCaidPOS[i]   = Integer.parseInt(obPos[5].toString());
							marcaTarjeta[i] = ((String)obPos[6]).trim();
							
							if(sCodcomp.trim().equals("E08")){	
								bdMontoComisionable = new BigDecimal(dv.roundDouble(bdMtoPos[i].doubleValue()/(1.13)));		
								bdMtoComis[i] = dv.roundBigDecimal(bdMontoComisionable.multiply(bdComis.divide(new BigDecimal("100"))));
								bMonto 		  = dv.roundBigDecimal(bdMtoPos[i].subtract(bdMtoComis[i]));
								bdMontoNetoPos[i]  = dv.roundDouble(bMonto.doubleValue());
							}else{
								bdMtoComis[i] = dv.roundBigDecimal(bdMtoPos[i].multiply(bdComis.divide(new BigDecimal("100"))));
								bMonto 		  = dv.roundBigDecimal(bdMtoPos[i].subtract(bdMtoComis[i]));
								bdMontoNetoPos[i]  = dv.roundDouble(bMonto.doubleValue());
							}
						}
					}
				}
				
				//&& ========== Guardar la nota de debito al cajero en caso de faltante.
				Varqueo ar = (Varqueo)CodeUtil.getFromSessionMap( "rva_VARQUEO");
				
				String tipoEmpleado= EmpleadoCtrl.determinarTipoCliente( ar.getId().getCodcajero() );
				BigDecimal tasaOficial = TasaCambioCtrl.tasaCambioOficial( ar.getId().getFecha(), "USD", sessionAprArqueo );
				
				
				if( ar.getId().getSf().compareTo(BigDecimal.ZERO) == -1 ){
				
					bHecho = rvCtrl.generarNotadeDebito(sessionAprArqueo, dac.getNoarqueo(), iCaid, sCodsuc, sCodcomp, dac.getFechaarqueo(),
									ar.getId().getSf().abs(), dac.getMoneda(), vaut[0], ar.getId().getCodcajero(), tipoEmpleado, tasaOficial);
					if(!bHecho){
						sMensaje = "No se ha podido registrar la nota de debito al cajero por faltante en caja.";
					}
				}
				
				if(bHecho){
				//===== Liquidacion para transacciones cheque efectivo de ALPESA ======= // 
					if(dac.getCodcomp().trim().equals("E03")){
						boolean bChequeEfectivo = false;
						LiquidacionCheque lqEfectivo = null;
						List<LiquidacionCheque>lstLiquidacionChks = new ArrayList<LiquidacionCheque>();
						
						//===== Cheques
						if(dTotalQ>0){
							bChequeEfectivo = true;
							if(CodeUtil.getFromSessionMap( "rva_lstLiquidacionCheques")==null){
								bHecho = false;
								sMensaje = "Error al obtener la lista de Consolidados de Montos en cheques por banco";
							}else{
								dTotal5 = dv.roundDouble(dTotal5-dTotalQ);
								lstLiquidacionChks.addAll((ArrayList<LiquidacionCheque>)CodeUtil.getFromSessionMap( "rva_lstLiquidacionCheques"));
							}
						}
						//===== Efectivo
						if(dTotal5>0){
							F55ca022 f22 = BancoCtrl.obtenerBancoxId(f14.getId().getC4bnc(), sessionAprArqueo );
							if(f22 == null){
								sMensaje = "F55CA022: No se ha podido obtener la configuración de banco para "+ f14.getId().getC4bnc(); 
								CodeUtil.putInSessionMap("sMensajeError", sMensaje);
								bHecho =  false;
							}else{
								bChequeEfectivo = true;
								lqEfectivo = new LiquidacionCheque();
								lqEfectivo.setMetodopago(MetodosPagoCtrl.EFECTIVO );
								lqEfectivo.setCaid(dac.getCaid());
								lqEfectivo.setCantidadCheque(1);
								lqEfectivo.setCodcomp(dac.getCodcomp().trim());
								lqEfectivo.setCodigoBanco(String.valueOf(f14.getId().getC4bnc()));
								lqEfectivo.setIcodigobanco(f14.getId().getC4bnc());
								lqEfectivo.setCodsuc(dac.getCodsuc());
								lqEfectivo.setMoneda(dac.getMoneda());
								lqEfectivo.setBtotalBanco(new BigDecimal(String.valueOf(dTotal5)));
								lqEfectivo.setMontototal(String.valueOf(dTotal5));
								lqEfectivo.setReferenciaBanco(dac.getDepositoRefer());
								lqEfectivo.setConciliaauto(String.valueOf(f22.getId().getConciliar()));
								lstLiquidacionChks.add(lqEfectivo);
							}
						}
						if(bHecho && bChequeEfectivo){
							bHecho = registrarTransaccionesChequeEfectivo(lstLiquidacionChks, dac.getNoarqueo(), 
											dac.getFechaarqueo(), dac.getHoraarqueo(),
											sessionAprArqueo, sCtaBcoTransitoria, f14, ar, tipoEmpleado, tasaOficial);
							
						}
						
					}else{			
						//-------------- depósitos por cheques y efectivo --------------------/
						if(dTotal5 >0){
							dTotalQ = 0;
							bHecho = realizarTrasladoBanco(dac.getNoarqueo(),iCaid, sCodcomp,sCodsuc, dac.getFechaarqueo(),dac.getHoraarqueo(),
											dac.getDepositoRefer(),dTotal5, dTotalQ, null, null, null,null,null,null, null, null,
											dac.getMoneda(), 1, sCtaBcoTransitoria, f14, null, ar, sessionAprArqueo, tipoEmpleado, tasaOficial);
							
						}
					}
				}
				//-------- Depósitos por transferencias y depósitos en banco --------/	
				if(bHecho){			
					if(lstRecibosN8 != null && lstRecibosN8.size()>0){
						
						bHecho = realizarTrasladoBanco(dac.getNoarqueo(), iCaid, sCodcomp,sCodsuc, dac.getFechaarqueo(),
										   dac.getHoraarqueo(),"", 0, 0, lstRecibosN8, null,null, null,null, 
										   null, null, null,dac.getMoneda(), 2,  sCtaBcoTransitoria,  f14, null, ar, 
										   sessionAprArqueo, tipoEmpleado, tasaOficial);
					
					}
				} 	
				
				//&& ====================== asientos de diario por comision y retencion de donaciones
				if(bHecho && incluyeDonacionesTc){
				 
					cierreDonaciones = (ArrayList<DncCierreDonacion>) CodeUtil.getFromSessionMap("rv_CierreDonaciones") ;
					
					List<ResumenAfiliado>lstRsmDnc = separaRetencionDonacion(lstResumenAfiliado);
					
					bHecho = registrarComisionRetencionDonacion(lstRsmDnc, cierreDonaciones, 
							f14.getId().getC4bcrcd(), vaut[0], dac.getFechaarqueo(), dac.getNoarqueo(), sessionAprArqueo, tipoEmpleado, tasaOficial);
											
				}
				
				if(bHecho){
					
					//&& ====================== realizar depositos de monto en banco
					if( bCredito || incluyeDonacionesTc ){
						
						
						bHecho = registrarDepositosAfiliados(dac.getNoarqueo(), lstResumenAfiliado, iCaid,sCodcomp, 
				 							  sCodsuc, dac.getFechaarqueo(),dac.getHoraarqueo(),dac.getMoneda(),
				 							  sCtaBcoTransitoria,f14, ar, sessionAprArqueo, tipoEmpleado, tasaOficial );
						
					}
					
					//&& ====================== asientos de diario solo por pagos con tarjeta de credito que no es donacion
					if( bHecho && bCredito  ){
						
						//&& ====================== realizar depositos de comision
						
						bHecho = realizarTrasladoBanco2(dac.getNoarqueo(), iCaid, sCodcomp,sCodsuc, dac.getFechaarqueo(),
									dac.getHoraarqueo(),"",0, 0, null,bdMtoPos, bdMontoNetoPos, 
									sAfiliados,sUninegPOS, bdMtoComis, sNoReferPos, iCaidPOS,
									dac.getMoneda(), 3, sCtaBcoTransitoria, f14, marcaTarjeta, ar, 
									sessionAprArqueo, tipoEmpleado, tasaOficial);	
						
						
						
						//&& ====================== realizar depositos de retencion
						if(bHecho ){
							bHecho = registrarRetencionAfiliados(dac.getNoarqueo(), lstResumenAfiliado, iCaid, sCodcomp, sCodsuc, 
		 			                   			dac.getFechaarqueo(), dac.getHoraarqueo(), dac.getMoneda(),
		 			                   			f14, sessionAprArqueo, tipoEmpleado, tasaOficial );
						}
						
						//&& ====================== impuesto sobre comision para Trader
						if(bHecho &&  sCodcomp.trim().compareTo("E08") == 0 ){
							bHecho = registrarIVAcomisionAfiliado(dac.getNoarqueo(), lstResumenAfiliado, iCaid, sCodcomp, sCodsuc, 
	 			                   			dac.getFechaarqueo(), dac.getHoraarqueo(), dac.getMoneda(), f14, sessionAprArqueo, tipoEmpleado, tasaOficial);
						}
					}
					
				}//--- Verificación de Paso correcto de Registro de depósitos y transferencias.
				//---- Guardar los registros para los depósitos		
				if(bHecho && CodeUtil.getFromSessionMap("rv_listaDepositos") != null){
					
					ArrayList<Ctaxdeposito> lstCtasxDeps = null;
					List lstDeps = (ArrayList) CodeUtil.getFromSessionMap("rv_listaDepositos");

					
					for (int i = 0; i < lstDeps.size(); i++) {
						
						Object oDep[] = (Object[])lstDeps.get(i);
						int iNobatch 	= Integer.parseInt(oDep[3].toString());
						int iNodoco		= Integer.parseInt(oDep[4].toString());
						int iNoarqueo	= Integer.parseInt(oDep[5].toString());
						String sMoneda		= oDep[6].toString();
						BigDecimal bdMonto	= ( (BigDecimal)oDep[7]).setScale(2, RoundingMode.HALF_UP) ;
						Date dtFecha		= (Date)oDep[8];
						Date dtHora			= (Date)oDep[9];
						String sCoduser		= oDep[10].toString();
						String sRefer		= oDep[11].toString();
						String sTipodep		= oDep[12].toString();
						String sTipopago    = oDep[14].toString();
						BigDecimal bdTasa   = new BigDecimal(oDep[15].toString());
						lstCtasxDeps 		=  (oDep.length < 17)? null :(ArrayList<Ctaxdeposito>)oDep[16];

						int codigobanco =  (oDep[17] == null )?
											f14.getId().getC4bnc():
											Integer.parseInt( oDep[17].toString() );
						
						int referdep = Integer.parseInt(String.valueOf( oDep[18] ) );
						int depctatran = Integer.parseInt(String.valueOf( oDep[19] ) );
											
						bHecho = rvCtrl.guardarRegistroDeposito(iCaid, sCodsuc, sCodcomp, iNobatch,	iNodoco, 
	     						iNoarqueo, sMoneda, bdMonto, dtFecha, dtHora, 
	     						sCoduser, sRefer, sTipodep,vaut[0].getId().getLogin(),sTipopago,
	     						bdTasa, vaut[0].getId().getCodreg().intValue(), codigobanco ,
	     						sessionAprArqueo, ar.getId().getCodcajero(), lstCtasxDeps, referdep, depctatran, f14.getId().getC4bcrcd() );
						
						
						if(!bHecho){	
							CodeUtil.putInSessionMap("sMensajeError", "No fue posible crear " +
									"registros por depósitos en Sistema " +
									"de Caja, favor intente nuevamente");
							break;
						}
					}
				}
				//------- Leer el objeto arqueo seleccionado y actualizar su estado a "APROBADO" 
				if(bHecho){
					if(CodeUtil.getFromSessionMap( "rva_VARQUEO")==null){
						bHecho = false;
						sMensaje = "No se ha podido realizar la operación: no se ha recuperar el objeto VARQUEO del SESION MAP";
					}else{
						Varqueo v = (Varqueo)CodeUtil.getFromSessionMap( "rva_VARQUEO");
						
						bHecho =  rvCtrl.actualizarEstadoArqueo(sessionAprArqueo, v, 1, "ARQUEO DE CAJA APROBADO Y PROCESADO", vaut[0].getId().getCodreg() );
						
						if(!bHecho)
							sMensaje = "No se ha podido realizar la operación: no se ha actualizado el estado del arqueo a 'APROBADO'";
					}
				}
				
				//&& ======================= hacer depositos por donaciones
				
				if( incluyeDonaciones  && bHecho){
					
					DonacionesCtrl.caid = ar.getId().getCaid();
					DonacionesCtrl.codcomp = sCodcomp;
					
					bHecho = DonacionesCtrl.comprobantesCierreDonaciones( cierreDonaciones, 
							ar.getId().getMoneda(), ar.getId().getNoarqueo(), 
							ar.getId().getFecha(), f14.getId().getC4bcrcd(), vaut[0], sessionAprArqueo) ;
					
					if(!bHecho){
						sMensaje = DonacionesCtrl.getMsgProceso();
					}
					
				}
				
				//-------------- FINALIZADAS TODAS LAS OPERACIONES CON ÉXITO ----------------/
				
				
				
				if(bHecho){
					dwDetalleArqueo.setWindowState("hidden");
					sMensaje = "Se han realizado correctamente los registros por depósitos en banco.";
				
					try {
						transactionAprArqueo.commit();
					} catch (Exception e) {
						e.printStackTrace() ;
						bHecho = false;
					}
					
					CodeUtil.removeFromSessionMap( "rv_DAC");
					CodeUtil.removeFromSessionMap( "rva_VARQUEO");
					CodeUtil.removeFromSessionMap( "rva_lstResumenAfiliado");	
					CodeUtil.removeFromSessionMap( "rva_pasoAprobarArqueo"); 
					refrescarArqueos();
					CodeUtil.removeFromSessionMap( "aprar_processRecibo");

				}else{		
					
					try {
						transactionAprArqueo.rollback();
					} catch (Exception e) {
						e.printStackTrace() ;
						bHecho = false;
					}

					
					
					if( CodeUtil.getFromSessionMap( "sMensajeError") != null )
						sMensaje = CodeUtil.getFromSessionMap( "sMensajeError").toString();
					
					if( sMensaje.trim().isEmpty() && CodeUtil.getFromSessionMap( "sMensajeError") == null)
						sMensaje = "Error al registrar los depósitos en banco: No se ha podido realizar la operación.";
					
				}

				rv_dwConfirmarProcesarArq.setWindowState("hidden");
				dwValidarAprobacionArqueo.setWindowState("normal");			
				lblValidarAprobacion.setValue("<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" />"+sMensaje+"<br>");
				CodeUtil.removeFromSessionMap( "aprar_processRecibo");
				CodeUtil.removeFromSessionMap( "sMensajeError");
				
			}
			
		}catch(Exception error){
			
			
			error.printStackTrace();
			
			try{
				

				CodeUtil.removeFromSessionMap( "aprar_processRecibo");
				CodeUtil.removeFromSessionMap( "sMensajeError");
				
				if(transactionAprArqueo.isActive() && !transactionAprArqueo.wasRolledBack() && !transactionAprArqueo.wasCommitted() )
					transactionAprArqueo.rollback();
				
				sMensaje = "No han podido aplicar los depósitos a cuenta de banco";
				
			}catch(Exception ex){
				ex.printStackTrace();
				sMensaje = "No han podido aplicar los depósitos a cuenta de banco";
			}
		}finally{
			
			
			try {
				HibernateUtilPruebaCn.closeSession(sessionAprArqueo);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			dwValidarAprobacionArqueo.setWindowState("normal");			
			lblValidarAprobacion.setValue("<img width=\"7\" src=\"/"
					+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" />"
					+sMensaje+"<br>");
			
//			LogCrtl.sendLogInfo(strEventhashcode + " ||||  Finaliza proceso de aprobacion de cierre: DtaCierre["+strDtaAprCierre+"]");
			
		}
	}
	
	public List<ResumenAfiliado> separaRetencionDonacion( List<ResumenAfiliado> resumenAfiliados ){
		List<ResumenAfiliado>dncResumenAfiliado = new ArrayList<ResumenAfiliado>();
		
		try {
			
			//&& =================== lista de Consolidado de afiliados agrupados por marca (consultaMontoPOSxMarca; RevisionArqueoCtrl)
			List<Object[]>lstPOSdncs = (ArrayList<Object[]>) CodeUtil.getFromSessionMap( "rva_TotalesDonacionAfiliado" );
			
			
			//&& =================== consolidar los montos por afiliados que no clasifican por marca.
			List<Object[]> lstNoClasificaMarca = (List<Object[]>) 
				CollectionUtils.select(lstPOSdncs, new Predicate(){
					public boolean evaluate(Object o) {
						return String.valueOf( ((Object[])o)[12] ).compareTo( "0" ) == 0 ;
					}
				}) ;
			
			lstPOSdncs = (ArrayList<Object[]>) CollectionUtils.subtract(lstPOSdncs, lstNoClasificaMarca);
			
			for (int i = 0; i < lstNoClasificaMarca.size(); i++) {
				final Object[] posNoClasficado = lstNoClasificaMarca.get(i);
				
				posNoClasficado[11] = "Genérico";
				
				List<Object[]>lstRepetidosNoClasifica = ( ArrayList<Object[]> ) 
					CollectionUtils.select(lstNoClasificaMarca, new Predicate() {
						public boolean evaluate(Object o) {
							return
							String.valueOf( ((Object[])o)[3] ) .compareTo( String.valueOf( posNoClasficado[3]) ) == 0  &&
							String.valueOf( ((Object[])o)[13] ).compareTo( String.valueOf( posNoClasficado[13]) ) != 0 ;
						}
					});
				
				if(lstRepetidosNoClasifica == null || lstRepetidosNoClasifica.isEmpty() ){
					lstPOSdncs.add(posNoClasficado);
					continue;
				}
				
				for (int j = 0; j < lstRepetidosNoClasifica.size(); j++) {
					posNoClasficado[0] = ((BigDecimal)posNoClasficado[0]) .add( (BigDecimal) lstRepetidosNoClasifica.get(j)[0]  ) ;
				}
				
				lstNoClasificaMarca = (ArrayList<Object[]>) CollectionUtils.subtract(lstNoClasificaMarca, lstRepetidosNoClasifica);
				lstPOSdncs.add(posNoClasficado);
				
			}
			
			//&& =================== restar del monto por afiliado la parte que corresponde a donacion
			for (final Object[] posdnc : lstPOSdncs) {
				
				ResumenAfiliado ra = (ResumenAfiliado)
				CollectionUtils.find(resumenAfiliados, new Predicate(){
					public boolean evaluate(Object o) {
						
						ResumenAfiliado raEval = (ResumenAfiliado)o ; 
						
						return 
						raEval.getCodigo().trim().compareTo( String.valueOf( posdnc[3]) ) == 0 &&
						raEval.getCodigomarcatarjeta().compareTo( String.valueOf( posdnc[13]) ) == 0 &&
						raEval.getLiquidarpormarca() == Integer.parseInt( String.valueOf(posdnc[12] ) );
						 
					}
				});
				
				if(ra == null )
					continue;
				
				BigDecimal comisionpos = ra.getPorcentajecomision();
				BigDecimal retencionpos = ra.getPorcentajeretencion();
				BigDecimal totaldonacion =  (BigDecimal)posdnc[0];
				
				BigDecimal comisiondnc  = totaldonacion.multiply(comisionpos.divide(new BigDecimal("100")));
				BigDecimal retenciondnc = totaldonacion.subtract(comisiondnc).multiply(retencionpos.divide(new BigDecimal("100")));
				
				ra.setMontoRetencion( Divisas.roundBigDecimal( ra.getMontoRetencion().subtract(retenciondnc) ) ) ;
				
				ResumenAfiliado raDnc = new ResumenAfiliado();
				raDnc.setCodigo(ra.getCodigo());
				raDnc.setReferencia(ra.getReferencia());
				raDnc.setMoneda(ra.getMoneda());
				raDnc.setCodigobancopos( ra.getCodigobancopos() );
				
				raDnc.setDmontototal(totaldonacion.doubleValue());
				raDnc.setDmontoxcomision(comisiondnc.doubleValue());
				raDnc.setMontoRetencion(retenciondnc);
				raDnc.setDmontoneto(totaldonacion.subtract(comisiondnc).subtract(retenciondnc).doubleValue());
				raDnc.setPorcentajecomision(ra.getPorcentajecomision());
				raDnc.setPorcentajeretencion(ra.getPorcentajeretencion());
				raDnc.setLiquidarpormarca(ra.getLiquidarpormarca());
				raDnc.setMarcatarjeta(ra.getMarcatarjeta());
				raDnc.setCodigomarcatarjeta(ra.getCodigomarcatarjeta());
				dncResumenAfiliado.add(raDnc);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
		}
		return dncResumenAfiliado;
	}
	
	
/***************************************************************************************************/
/** Método para realizar batch y asientos diarios para los depósitos a banco por métodos de pago
 **********************/
	public boolean realizarTrasladoBanco(
		  int iNoarqueo, int iCaid, final String sCodcomp, String sCodsuc, Date dtFecha, Date HoraArqueo, String sReferdep, double dMonto5, double dMontoQ,
		  List lstRecN8, BigDecimal bdMtoPos[], double bdMontoNetoPos[], String sAfiliados[], String sUninegPOS[], BigDecimal bdMtoComis[],
		  int iNoReferPos[], int iCaidPos[], final String sMoneda, int iMpago, 
		  String sCtaBcoTransitoria[],  F55ca014 f14 , String[] marcaTarjeta, Varqueo ar,
		  Session session, String sTipoCliente, BigDecimal tasaCambioOficial
			){
		
		boolean bHecho = true;
		int iNobatchNodoc[];
		String sMensaje = "";
		String sCompCuentaCaja="",sCompCuentaBanco="",sFecha,sCuenta[],sAsientoSucursal="";
		String sConcepto, sCtmcu = "", sCtobj = "",sCtsub = "",sCcobj="", sCcsub="",sCc1mcu="" ;
		String sIdCuentaCaja = "", sIdCtaTranBanco = "", sCuentaCaja = "", sCtaTranBanco = "";
		int iNoBatch = 0, iNoDocumento = 0,  iMontoQ ;
		SimpleDateFormat format ;
		Vautoriz[] vaut;
		Vf55ca01 f55ca01;
		 
		Divisas dv = new Divisas();
		ReciboCtrl recCtrl = new ReciboCtrl();
		ClsParametroCaja cajaparm = new ClsParametroCaja();
	 
		int iCodigoBanco=0;
		F55ca022 f22 = null;
		String sTipoDoc = "ZX";
		String sDetLineaBco = "";
		List<Ctaxdeposito> lstCtasxDeps = null;
		Ctaxdeposito ctaxDeps = null;
		
		long iMonto  = 0 ;
		long iMonto5 = 0 ;
		
		String strSubLibroCuenta = "" ;
		String strTipoAuxiliarCtB = "";
		int tipogenerareferencia = 0 ; // 0: original, 1: cambiada por sistema, 2: consecutiva de jde
		
		try {
			//------ Leer y actualizar el número de documento y número de batch ------/
			//------ Obtener el tipo de cliente y el login del usuario ---------------/
			f55ca01 = (Vf55ca01)((List)CodeUtil.getFromSessionMap( "lstCajas")).get(0);
			vaut = (Vautoriz[]) CodeUtil.getFromSessionMap( "sevAut");
			
			format = new SimpleDateFormat("dd/MM/yyyy");
			sFecha = format.format(dtFecha);
			sConcepto = "Arqueo "+iNoarqueo+" Caja "+iCaid+ " "+ sFecha;
			String logs = sConcepto;
			
			iCodigoBanco = f14.getId().getC4bnc();
			
			String monedaBaseCompania = f14.getId().getC4bcrcd() ;
			
			
			//------------ ASIENTO PARA EFECTIVO Y CHEQUES --------------- //.			
			if(iMpago == 1){
				
				iNoBatch = Divisas.numeroSiguienteJdeE1(   );
 
				sCuenta = dv.obtenerCuentaCaja(iCaid, sCodcomp, MetodosPagoCtrl.EFECTIVO , sMoneda, session, null, null, null);
				
				if(sCuenta != null){
					
					sCuentaCaja = sCuenta[0];
					sIdCuentaCaja = sCuenta[1];
					sCompCuentaCaja = sCuenta[2];
					sCc1mcu = sCuenta[3];
					sCcobj  = sCuenta[4];
					sCcsub  = sCuenta[5];
					sAsientoSucursal= sCuenta[2];
					
					//&& ====== Validar que cuenta de banco se debe utilizar si transitoria o directo a banco
					f22 =  BancoCtrl.obtenerBancoxId( iCodigoBanco, session );
					if(f22 == null){
						CodeUtil.putInSessionMap("sMensajeError","ACF55CA022: Error al recuperar datos de banco "+iCodigoBanco+", Favor intente de nuevo. ");
						return false;
					}
					
					if( ar.getId().getDepctatran() == 1){
						 
						tipogenerareferencia = 2; 
						
						if(sCtaBcoTransitoria == null){
							CodeUtil.putInSessionMap("sMensajeError","ACF55CA033: No se encuentra configurada la cuenta transitoria de banco "+f22.getId().getBanco());
							return false;
						}
						sCuenta = sCtaBcoTransitoria;
						
						sTipoDoc = cajaparm.getParametros("34", "0", "CIERRE_DOCTYPE_P9").getValorAlfanumerico().toString();
						
						sDetLineaBco = "REF:"+ar.getId().getReferencenumber()+" MP:Q5 C:"+ar.getId().getCaid();
						
						//iNoDocumento = dv.leerActualizarNoDocJDE();
						iNoDocumento =  Divisas.numeroSiguienteJdeE1Custom(valoresJdeNumeracion[8],valoresJdeNumeracion[9] );
						strSubLibroCuenta = ConfirmaDepositosCtrl.constructSubLibroCtaTbanco (0, iCodigoBanco, iCaid, sMoneda, sCodcomp );
						strTipoAuxiliarCtB =cajaparm.getParametros("34", "0", "CODIGO_TIPO_AUX_CT").getValorAlfanumerico().toString();  //PropertiesSystem.CODIGO_TIPO_AUXILIAR_CT ;
					}	
					else{
						
						iNoDocumento = ar.getId().getReferencenumber();
						if(iNoDocumento == 0){
							iNoDocumento = Integer.parseInt(ar.getId().getReferdep());
							ar.getId().setReferencenumber(iNoDocumento);
						}
							
						sTipoDoc = cajaparm.getParametros("34", "0", "TIPODOC_JDE_DEP_5").getValorAlfanumerico().toString(); //PropertiesSystem.TIPODOC_JDE_DEP_5;
						sDetLineaBco = "REF:"+ar.getId().getReferencenumber()+" MP:Q5 C:"+ar.getId().getCaid();
							
						sCuenta = dv.obtenerCuentaBanco(sCodcomp, sMoneda,iCodigoBanco, session, null, null, null);
						strSubLibroCuenta = "";
						strTipoAuxiliarCtB = "";
						
						//&& =============== Validaciones del numero de referencia
						
						iNoDocumento = RevisionArqueoCtrl.generarReferenciaDeposito(iNoDocumento, MetodosPagoCtrl.EFECTIVO , sAsientoSucursal, sMoneda);
						
						//********************************************************
					}
					
					 
					//&& =========== hacer asientos de diario
					if(sCuenta != null){
						sCtaTranBanco 	= sCuenta[0];
						sIdCtaTranBanco = sCuenta[1];
						sCompCuentaBanco= sCuenta[2];
						sCtmcu			= sCuenta[3];
						sCtobj			= sCuenta[4];
						sCtsub			= sCuenta[5];	
						//sAsientoSucursal= sCuenta[2];
					
						//---------- Guardar el asiento diario.----------
						if(sMoneda.equals( monedaBaseCompania .trim() ) ){
							if(dMonto5>0){
								dMonto5 = dv.roundDouble(dMonto5);
								iMonto5 = (long)(dv.roundDouble(dMonto5 * 100));		
								
								bHecho = recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sAsientoSucursal,sTipoDoc, iNoDocumento, 1.0, iNoBatch, sCtaTranBanco,
													sIdCtaTranBanco, sCtmcu, sCtobj, sCtsub, "AA", sMoneda, iMonto5, sConcepto,
													vaut[0].getId().getLogin(),	vaut[0].getId().getCodapp(), BigDecimal.ZERO, sTipoCliente,
													sDetLineaBco,sCompCuentaBanco, strSubLibroCuenta, strTipoAuxiliarCtB, sMoneda, sCompCuentaBanco,"D", 0);
								if(!bHecho){
									CodeUtil.putInSessionMap("sMensajeError", "Error en Depósito de Efectivo: "
															+recCtrl.getError().toString().split("@")[1]);
									return bHecho =  false;
								}
								bHecho = recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sAsientoSucursal, sTipoDoc, iNoDocumento, 2.0, iNoBatch, sCuentaCaja,
													sIdCuentaCaja, sCc1mcu, sCcobj, sCcsub, "AA", sMoneda, iMonto5*(-1), sConcepto, 
													vaut[0].getId().getLogin(),	vaut[0].getId().getCodapp(),BigDecimal.ZERO, sTipoCliente,
													"Crédito caja Efectivo-cheque",sCompCuentaCaja, "", "",sMoneda,sCompCuentaCaja,"D", 0 );
								if(!bHecho){
									CodeUtil.putInSessionMap("sMensajeError", "Error en Depósito de Efectivo: "
															+recCtrl.getError().toString().split("@")[1]);
									return bHecho = false;
								}
							}
						}//moneda córdobas.
						else{
							
							long iMonto5Dom;								
							
							if(dMonto5 >0){							
								iMonto5Dom = (long)(dv.roundDouble((dMonto5 * tasaCambioOficial.doubleValue() )*100));
								iMonto5    = (long)(dv.roundDouble((dMonto5*100)));
								
								bHecho = recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sAsientoSucursal, sTipoDoc, iNoDocumento, 1.0, 
													iNoBatch, sCtaTranBanco, sIdCtaTranBanco, sCtmcu, sCtobj, 
													sCtsub, "AA", sMoneda, iMonto5Dom, sConcepto , vaut[0].getId().getLogin(),
													vaut[0].getId().getCodapp(), tasaCambioOficial, sTipoCliente, sDetLineaBco,
													sCompCuentaBanco,  strSubLibroCuenta, strTipoAuxiliarCtB, monedaBaseCompania, sCompCuentaBanco,"F", iMonto5);
								if(!bHecho){
									CodeUtil.putInSessionMap("sMensajeError",  "Error en Depósito de Efectivo: "
															+recCtrl.getError().toString().split("@")[1]);
									return bHecho = false;
								}
								bHecho = recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sAsientoSucursal,sTipoDoc, iNoDocumento, 1.0, 
													iNoBatch, sCtaTranBanco, sIdCtaTranBanco, sCtmcu, sCtobj, 
													sCtsub, "CA", sMoneda, iMonto5, sConcepto, vaut[0].getId().getLogin(),
													vaut[0].getId().getCodapp(), tasaCambioOficial, sTipoCliente,
													sDetLineaBco,sCompCuentaBanco,  strSubLibroCuenta, strTipoAuxiliarCtB, monedaBaseCompania,sCompCuentaBanco,"F", 0);
								if(!bHecho){
									CodeUtil.putInSessionMap("sMensajeError", "Error en Depósito de Efectivo: "
															+recCtrl.getError().toString().split("@")[1]);
									return bHecho = false;
								}
								bHecho = recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sAsientoSucursal, sTipoDoc, iNoDocumento, 2.0, 
													iNoBatch, sCuentaCaja, sIdCuentaCaja, sCc1mcu, sCcobj, 
													sCcsub, "AA", sMoneda, iMonto5Dom*(-1), sConcepto, vaut[0].getId().getLogin(),
													vaut[0].getId().getCodapp(), tasaCambioOficial, sTipoCliente,"Cred. Caja Efectivo-Cheque",
													sCompCuentaCaja, "", "", monedaBaseCompania, sCompCuentaCaja, "F", (iMonto5 *(-1) ) ); 
								if(!bHecho){
									CodeUtil.putInSessionMap("sMensajeError", "Error en Depósito de Efectivo: "
															+recCtrl.getError().toString().split("@")[1]);
									return bHecho = false;
								}
								bHecho = recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sAsientoSucursal, sTipoDoc, iNoDocumento, 2.0, 
													iNoBatch, sCuentaCaja, sIdCuentaCaja, sCc1mcu, sCcobj, 
													sCcsub, "CA", sMoneda, iMonto5*(-1), sConcepto,	vaut[0].getId().getLogin(),	
													vaut[0].getId().getCodapp(), tasaCambioOficial, sTipoCliente, "Cred. Caja Efectivo-Cheque",
													sCompCuentaCaja, "", "", monedaBaseCompania, sCompCuentaCaja,"F", 0);
								if(!bHecho){
									CodeUtil.putInSessionMap("sMensajeError", "Error en Depósito de Efectivo: "
															+recCtrl.getError().toString().split("@")[1]);
									return bHecho = false;
								}
							}
						}
					}else{
						CodeUtil.putInSessionMap("sMensajeError","Error obtener la cuenta de banco para depósito de cheques y efectivo en moneda "+sMoneda+", Caja: "+iCaid+ ", Suc: " + sCodsuc+", Comp: "+sCodcomp);
						return bHecho = false;
					}
				}else{
					CodeUtil.putInSessionMap("sMensajeError","Error obtener la cuenta de caja para depósito de cheques y efectivo en moneda "+sMoneda+", Caja: "+iCaid+ ", Suc: " + sCodsuc+", Comp: "+sCodcomp);
					return bHecho = false;
				}						
		 					
				BigDecimal bdMonto = new BigDecimal(Double.toString(dMonto5))
									.add(new BigDecimal(Double.toString(dMontoQ)))
									.setScale(2, RoundingMode.HALF_UP);
				
				iMonto = Long.parseLong( String.format("%1$.2f", bdMonto).replace(".", "")  ); 
			
				if(bHecho){
					
					bHecho = recCtrl.registrarBatchA92Session( session, dtFecha,  valoresJdeInsContado[8], iNoBatch, iMonto, vaut[0].getId().getLogin(), 1, "APRARQUEO",  valoresJdeInsContado[9]);
					
					if(bHecho){
						
						List<Object[]> lstDeps = new ArrayList<Object[]>();
						if (CodeUtil.getFromSessionMap("rv_listaDepositos") != null)
							lstDeps = (ArrayList<Object[]>) CodeUtil.getFromSessionMap("rv_listaDepositos");
						
						Object oDep[] = new Object[20];
						oDep[0]= iCaid;
						oDep[1]= sCodsuc;
						oDep[2]= sCodcomp;
						oDep[3]= iNoBatch;
						oDep[4]= iNoDocumento;
						oDep[5]= iNoarqueo;
						oDep[6]= sMoneda;
						oDep[7]= bdMonto.setScale(2, RoundingMode.HALF_UP);
						oDep[8]= dtFecha;
						oDep[9]= HoraArqueo;
						oDep[10]= vaut[0].getId().getLogin();
						oDep[11]= iNoDocumento; //sReferdep;  
						oDep[12]= "D";
						oDep[14]= MetodosPagoCtrl.EFECTIVO ;
						oDep[15] = tasaCambioOficial.setScale(4, RoundingMode.HALF_UP);
						
						//&& =========== Crear el objeto que contendra la informacion de los movimientos sobre las cuentas.
						lstCtasxDeps = new ArrayList<Ctaxdeposito>();
						ctaxDeps = new Ctaxdeposito();
						ctaxDeps.setMonto(new BigDecimal(String.valueOf(dMonto5)));
						ctaxDeps.setTipomov("C");
						ctaxDeps.setIdcuenta(sIdCtaTranBanco);
						ctaxDeps.setGmmcu(sCtmcu);
						ctaxDeps.setGmobj(sCtobj);
						ctaxDeps.setGmsub(sCtsub);
						lstCtasxDeps.add(ctaxDeps);
						
						//&& ========== Objeto para debito, caja.
						ctaxDeps = new Ctaxdeposito();
						ctaxDeps.setMonto(new BigDecimal(String.valueOf(dMonto5)));
						ctaxDeps.setTipomov("D");
						ctaxDeps.setIdcuenta(sIdCuentaCaja);
						ctaxDeps.setGmmcu(sCc1mcu);
						ctaxDeps.setGmobj(sCcobj);
						ctaxDeps.setGmsub(sCcsub);
						lstCtasxDeps.add(ctaxDeps);
						
						oDep[16] = lstCtasxDeps;
						oDep[17] = iCodigoBanco;
						oDep[18] = ar.getId().getReferencenumber();
						oDep[19] = ar.getId().getDepctatran();
						
						lstDeps.add(oDep);	
						CodeUtil.putInSessionMap("rv_listaDepositos", lstDeps);
						
					}else{
						CodeUtil.putInSessionMap("sMensajeError","Error al guardar el batch de efectivo y cheques en "+sMoneda+", Caja: "+iCaid+ ", Suc: " + sCodsuc+", Comp: "+sCodcomp);
					}
				}//validación de asientos para el batch.
			}//método de pago cheques y efectivo.
				
				/* *************************************************************************** */
				/* ************* REGISTRAR BATCH'S PARA TRANSFERENCIAS Y DEPÓSITOS. ********** */
				if(iMpago == 2){
					
					String sCuentaCaja8[],sCuentaCajaN[],sRefer="",sSucursalCtaBco="";					
					boolean bCuentaN=false,bCuenta8=false;
					String sDescrip ="";
					
					//---------------
					sCuentaCaja8 = dv.obtenerCuentaCaja(iCaid, sCodcomp, MetodosPagoCtrl.TRANSFERENCIA, sMoneda, session, null, null, null);
					if(sCuentaCaja8 != null){
						bCuenta8 = true;
					}
					sCuentaCajaN = dv.obtenerCuentaCaja(iCaid, sCodcomp, MetodosPagoCtrl.DEPOSITO, sMoneda, session,null, null, null);
					if(sCuentaCajaN != null){
						bCuentaN = true;
					}
					
					//***************************************************************************//
					Set<Integer> codigosBanco = new HashSet<Integer>();
					
					//&& ===== lista unica de codigos de banco
					for (int i = 0; i < lstRecN8.size(); i++) {
						Vrecibodevrecibo v = (Vrecibodevrecibo) lstRecN8.get(i);
						codigosBanco.add(Integer.valueOf( v.getId().getRefer1() ) )  ;
					}
					
					//&& ======  crear lista de cuentas transitorias por banco
					List<String[]> cuentasTransitoriasBanco = new ArrayList<String[]>();
					for (Integer codigo : codigosBanco) {
						
						String[] cuentaTransitoria  = dv.obtenerCuentaTransitoBanco(sMoneda, sCodcomp, codigo, session);
						if(cuentaTransitoria == null )
							continue;
						
						cuentasTransitoriasBanco.add(cuentaTransitoria);
					}
					
					//***************************************************************************//
					
					
					//-------- recorrer la lista de recibos y clasificar por mpago. 
					for(int i=0; i<lstRecN8.size();i++){
						
						Vrecibodevrecibo v = (Vrecibodevrecibo)lstRecN8.get(i);
						if(v.getId().getMpago().equals(MetodosPagoCtrl.DEPOSITO)){
							if(bCuentaN){
								sDescrip = "Dep.Banco";
								sRefer   = v.getId().getRefer2();
								sCuentaCaja 	= sCuentaCajaN[0];
								sIdCuentaCaja 	= sCuentaCajaN[1];
								sCompCuentaCaja = sCuentaCajaN[2];
								sCc1mcu 		= sCuentaCajaN[3];
								sCcobj 			= sCuentaCajaN[4];
								sCcsub 			= sCuentaCajaN[5];
								
								if( v.getId().getReferencenumber() == 0){
									v.getId().setReferencenumber(Integer.parseInt(v.getId().getRefer2().trim()));
								}
								
							}else{
								CodeUtil.putInSessionMap("sMensajeError", "Error al obtener la cuenta " +sMoneda +" de caja "+iCaid+", compañía "+sCodcomp.trim()+" para método de pago Deposito en bancos ");
								return bHecho = false;
							}
						}else{
							if(bCuenta8){
								sDescrip = "Trans.Banco";
								sRefer   = v.getId().getRefer3();
								sCuentaCaja 	= sCuentaCaja8[0];
								sIdCuentaCaja 	= sCuentaCaja8[1];
								sCompCuentaCaja = sCuentaCaja8[2];
								sCc1mcu 		= sCuentaCaja8[3];
								sCcobj 			= sCuentaCaja8[4];
								sCcsub 			= sCuentaCaja8[5];
								
								if(v.getId().getReferencenumber() == 0){
									v.getId().setReferencenumber(Integer.parseInt(v.getId().getRefer3().trim()));
								}
								
							}else{
								CodeUtil.putInSessionMap("sMensajeError", "Error al obtener la cuenta " +sMoneda +" de caja "+iCaid+", compañía "+sCodcomp.trim()+", para método de pago Transferencia Bancarias ");
								return bHecho = false;
							}
						}
						
						iCodigoBanco = Integer.parseInt(v.getId().getRefer1().trim());
						
						//-------------- número de batch y de documento.
						//iNoBatch = dv.leerActualizarNoBatch();
						iNoBatch =  Divisas.numeroSiguienteJdeE1(   );
 
						if(bHecho){
							f22 =  BancoCtrl.obtenerBancoxId( iCodigoBanco, session );
							if(f22 == null){
								CodeUtil.putInSessionMap("sMensajeError", "F55CA022: No se puedo obtener la información de banco: " + iCodigoBanco);
								return bHecho = false;
							}
							
							//&& ========================= Cargar el deposito a cuenta transitoria
							if(  v.getId().getDepctatran() == 1){
								
								final String strCodigoBancoBuscar = v.getId().getRefer1().trim();
								
								sCtaBcoTransitoria = (String[])
								 CollectionUtils.find(cuentasTransitoriasBanco, new Predicate(){

									@Override
									public boolean evaluate(Object o) {
										String [] ctatrans = (String[])o;
										return
										ctatrans[6].trim().compareTo(sMoneda.trim() ) == 0 && 
										ctatrans[7].trim().compareTo(sCodcomp.trim() ) == 0 &&  
										ctatrans[8].trim().compareTo(strCodigoBancoBuscar) == 0 ;
									}
								 });
								
								
								if(sCtaBcoTransitoria == null ){
									CodeUtil.putInSessionMap("sMensajeError","ACF55CA033: No se encuentra configurada la cuenta transitoria de banco "+f22.getId().getBanco());
									return bHecho = false;
								}
								
								//iNoDocumento = dv.leerActualizarNoDocJDE();
								iNoDocumento =  Divisas.numeroSiguienteJdeE1Custom(valoresJdeNumeracion[8],valoresJdeNumeracion[9] );
								
								sCuenta = sCtaBcoTransitoria;
								
								sTipoDoc = cajaparm.getParametros("34", "0", "CIERRE_DOCTYPE_P9").getValorAlfanumerico().toString();
								 
								sDetLineaBco = "REF:"+ v.getId().getReferencenumber() + " MP:" + v.getId().getMpago().trim()+" RC:" + v.getId().getNumrec();
								
								strSubLibroCuenta = ConfirmaDepositosCtrl.constructSubLibroCtaTbanco(0, f22.getId().getCodb(), iCaid, sMoneda, sCodcomp);
								
								strTipoAuxiliarCtB = cajaparm.getParametros("34", "0", "CODIGO_TIPO_AUX_CT").getValorAlfanumerico().toString();//PropertiesSystem.CODIGO_TIPO_AUXILIAR_CT ;
								
							}else{
								
								iNoDocumento = v.getId().getReferencenumber();
								
								sCuenta = dv.obtenerCuentaBanco(sCodcomp, sMoneda, iCodigoBanco , session, null, null, null);
								
								sTipoDoc =  v.getId().getMpago().trim().compareTo(MetodosPagoCtrl.DEPOSITO) == 0 ?
										cajaparm.getParametros("34", "0", "TIPODOC_JDE_DEP_N").getValorAlfanumerico().toString()://PropertiesSystem.TIPODOC_JDE_DEP_N: 
								        cajaparm.getParametros("34", "0", "TIPODOC_JDE_DEP_8").getValorAlfanumerico().toString();//PropertiesSystem.TIPODOC_JDE_DEP_8 ;
								
								sDetLineaBco = "REF:"+v.getId().getReferencenumber()+" MP:"+v.getId().getMpago().trim()+" RC:" + v.getId().getNumrec();
								
								strSubLibroCuenta = "";
								strTipoAuxiliarCtB = "";
								
								//&& =============== Validaciones del numero de referencia
								
								iNoDocumento = RevisionArqueoCtrl.generarReferenciaDeposito(iNoDocumento, v.getId().getMpago().trim(), sCompCuentaCaja, sMoneda);
								
							}

							if(sCuenta != null){
								sCtaTranBanco 	= sCuenta[0];
								sIdCtaTranBanco = sCuenta[1];
								sCompCuentaBanco= sCuenta[2];
								sCtmcu			= sCuenta[3];
								sCtobj			= sCuenta[4];
								sCtsub			= sCuenta[5];
								sSucursalCtaBco = sCompCuentaCaja; 
								
								
								iMonto = (long)dv.roundDouble((v.getId().getMontoneto().doubleValue() * 100));
								
								//&& ====== Encabezado
								bHecho = recCtrl.registrarBatchA92Session( session, dtFecha,  valoresJdeInsContado[8], iNoBatch, iMonto, vaut[0].getId().getLogin(), 1, "APRARQUEO",  valoresJdeInsContado[9]);
								
								if(!bHecho){
									CodeUtil.putInSessionMap("sMensajeError", "No se registró Batch para "
											+ (( v.getId().getMpago().compareTo(MetodosPagoCtrl.TRANSFERENCIA) == 0? "Transferencia":"Depósito"))
											+" en Recibo #"+v.getId().getNumrec()+":  "
											+recCtrl.getError().toString().split("@")[1]);
									return bHecho = false;
								}
								
								//-----------------  guardar el asiento diario
								if(sMoneda.equals(  monedaBaseCompania.trim()  )){
									
									//& ======= Cuerpo moneda domestica.
									bHecho = recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sSucursalCtaBco,sTipoDoc, iNoDocumento, 1.0, iNoBatch, sCtaTranBanco,
												sIdCtaTranBanco, sCtmcu, sCtobj, sCtsub, "AA", sMoneda, iMonto, sConcepto,vaut[0].getId().getLogin(),
												vaut[0].getId().getCodapp(), BigDecimal.ZERO, sTipoCliente, sDetLineaBco ,sCompCuentaBanco,
												strSubLibroCuenta, strTipoAuxiliarCtB, monedaBaseCompania, sCompCuentaBanco,"D", 0);
									if(!bHecho){
										CodeUtil.putInSessionMap("sMensajeError", "No se registró Batch para "
												+ (( v.getId().getMpago().compareTo(MetodosPagoCtrl.TRANSFERENCIA) == 0? "Transferencia":"Depósito"))
												+" en Recibo #"+v.getId().getNumrec()+":  "
												+recCtrl.getError().toString().split("@")[1]);
										return bHecho = false;
									}
									bHecho = recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sSucursalCtaBco,sTipoDoc, iNoDocumento, 2.0, iNoBatch, sCuentaCaja,
														sIdCuentaCaja, sCc1mcu, sCcobj, sCcsub, "AA", sMoneda, iMonto*(-1), sConcepto,vaut[0].getId().getLogin(), 
														vaut[0].getId().getCodapp(),BigDecimal.ZERO, sTipoCliente,"Crédito Caja "+sDescrip, sCompCuentaCaja,
														"","", monedaBaseCompania,sCompCuentaCaja,"D", 0);
									if(!bHecho){
										CodeUtil.putInSessionMap("sMensajeError", "No se registró Batch para "
												+ (( v.getId().getMpago().compareTo(MetodosPagoCtrl.TRANSFERENCIA) == 0? "Transferencia":"Depósito"))
												+" en Recibo #"+v.getId().getNumrec()+":  "
												+recCtrl.getError().toString().split("@")[1]);
										return bHecho = false;
									}
								}// moneda córdobas
								else{
									
									iMonto 		= (long)(dv.roundDouble((v.getId().getMontoneto().doubleValue() * 100)));
									long iMontodom	= (long)(dv.roundDouble(((v.getId().getMontoneto().doubleValue() * tasaCambioOficial.doubleValue() )*100)));
									
									bHecho  =	recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sSucursalCtaBco,sTipoDoc, iNoDocumento, 1.0, iNoBatch, sCtaTranBanco,
														sIdCtaTranBanco, sCtmcu, sCtobj, sCtsub, "AA", sMoneda, iMontodom, sConcepto,vaut[0].getId().getLogin(),
														vaut[0].getId().getCodapp(), tasaCambioOficial, sTipoCliente,sDetLineaBco, sCompCuentaBanco,
														strSubLibroCuenta,strTipoAuxiliarCtB, monedaBaseCompania, sCompCuentaBanco,"F", iMonto );
									if(!bHecho){
										CodeUtil.putInSessionMap("sMensajeError", "No se registró Batch para "
												+ (( v.getId().getMpago().compareTo(MetodosPagoCtrl.TRANSFERENCIA) == 0? "Transferencia":"Depósito"))
												+" en Recibo #"+v.getId().getNumrec()+":  "
												+recCtrl.getError().toString().split("@")[1]);
										return bHecho = false;
									}
									bHecho  =	recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sSucursalCtaBco,sTipoDoc, iNoDocumento, 1.0, iNoBatch, sCtaTranBanco,
													 	sIdCtaTranBanco, sCtmcu, sCtobj, sCtsub, "CA", sMoneda, iMonto, sConcepto,vaut[0].getId().getLogin(),
													 	vaut[0].getId().getCodapp(), tasaCambioOficial, sTipoCliente, sDetLineaBco, sCompCuentaBanco,
													 	strSubLibroCuenta,strTipoAuxiliarCtB, monedaBaseCompania,sCompCuentaBanco, "F", 0);
									if(!bHecho){
										CodeUtil.putInSessionMap("sMensajeError", "No se registró Batch para "
												+ (( v.getId().getMpago().compareTo(MetodosPagoCtrl.TRANSFERENCIA) == 0? "Transferencia":"Depósito"))
												+" en Recibo #"+v.getId().getNumrec()+":  "
												+recCtrl.getError().toString().split("@")[1]);
										return bHecho = false;
									}
									bHecho = recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sSucursalCtaBco,sTipoDoc, iNoDocumento, 2.0, iNoBatch, sCuentaCaja,
														 sIdCuentaCaja, sCc1mcu, sCcobj, sCcsub, "AA", sMoneda, iMontodom*(-1), sConcepto,vaut[0].getId().getLogin(), 
														 vaut[0].getId().getCodapp(), tasaCambioOficial, sTipoCliente,"Crédito Caja "+ sDescrip,sCompCuentaCaja,
														 "","", monedaBaseCompania, sCompCuentaCaja,"F", ( iMonto*(-1) ) );
									if(!bHecho){
										CodeUtil.putInSessionMap("sMensajeError", "No se registró Batch para "
												+ (( v.getId().getMpago().compareTo(MetodosPagoCtrl.TRANSFERENCIA) == 0? "Transferencia":"Depósito"))
												+" en Recibo #"+v.getId().getNumrec()+":  "
												+recCtrl.getError().toString().split("@")[1]);
										return bHecho = false;
									}
									bHecho = recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sSucursalCtaBco, sTipoDoc, iNoDocumento, 2.0, iNoBatch, sCuentaCaja,
														 sIdCuentaCaja, sCc1mcu, sCcobj, sCcsub, "CA", sMoneda, iMonto*(-1), sConcepto,vaut[0].getId().getLogin(), 
														 vaut[0].getId().getCodapp(), tasaCambioOficial, sTipoCliente,"Crédito Caja "+ sDescrip,sCompCuentaCaja,
														 "","", monedaBaseCompania, sCompCuentaCaja,"F", 0);
									if(!bHecho){
										CodeUtil.putInSessionMap("sMensajeError", "No se registró Batch para "
												+ (( v.getId().getMpago().compareTo(MetodosPagoCtrl.TRANSFERENCIA) == 0? "Transferencia":"Depósito"))
												+" en Recibo #"+v.getId().getNumrec()+":  "
												+recCtrl.getError().toString().split("@")[1]);
										return bHecho = false;
									}
								}//asiento en moneda foránea.
								
								//&& =========== Conservar los datos para el deposito.					
								List<Object[]> lstDeps = new ArrayList<Object[]>();
								if (CodeUtil.getFromSessionMap("rv_listaDepositos") != null)
									lstDeps = (ArrayList<Object[]>) CodeUtil.getFromSessionMap("rv_listaDepositos");
								
								Object oDep[] = new Object[20];
								oDep[0]= iCaid;
								oDep[1]= sCodsuc;
								oDep[2]= sCodcomp;
								oDep[3]= iNoBatch;
								oDep[4]= iNoDocumento;
								oDep[5]= iNoarqueo;
								oDep[6]= sMoneda;
								oDep[7]= v.getId().getMontoneto();
								oDep[8]= dtFecha;
								oDep[9]= HoraArqueo;
								oDep[10]= vaut[0].getId().getLogin();
								oDep[11]= iNoDocumento; //sRefer;
								oDep[12]= "D";
								oDep[14]= v.getId().getMpago();
								oDep[15]= tasaCambioOficial.setScale(4, RoundingMode.HALF_UP);
								
								//&& =========== Crear el objeto que contendra la informacion de los movimientos sobre las cuentas.
								lstCtasxDeps = new ArrayList<Ctaxdeposito>();
								ctaxDeps = new Ctaxdeposito();
								ctaxDeps.setMonto(v.getId().getMontoneto());
								ctaxDeps.setTipomov("C");
								ctaxDeps.setIdcuenta(sIdCtaTranBanco);
								ctaxDeps.setGmmcu(sCtmcu);
								ctaxDeps.setGmobj(sCtobj);
								ctaxDeps.setGmsub(sCtsub);
								lstCtasxDeps.add(ctaxDeps);
								
								//&& ========== Objeto para debito, caja.
								ctaxDeps = new Ctaxdeposito();
								ctaxDeps.setMonto(v.getId().getMontoneto());
								ctaxDeps.setTipomov("D");
								ctaxDeps.setIdcuenta(sIdCuentaCaja);
								ctaxDeps.setGmmcu(sCc1mcu);
								ctaxDeps.setGmobj(sCcobj);
								ctaxDeps.setGmsub(sCcsub);
								lstCtasxDeps.add(ctaxDeps);
								oDep[16] = lstCtasxDeps;
								oDep[17] = iCodigoBanco;
								
								oDep[18] = v.getId().getReferencenumber()  ;
								try {
									oDep[18] = ( v.getId().getReferencenumber()  == 0 ) ? Integer.parseInt( sRefer ): v.getId().getReferencenumber();
								} catch (Exception e) {
									e.printStackTrace();
								}
								
								oDep[19] = v.getId().getDepctatran();
								
								lstDeps.add(oDep);
								CodeUtil.putInSessionMap("rv_listaDepositos", lstDeps);
								
							}//Numero de cuenta de banco
							else{
								CodeUtil.putInSessionMap("sMensajeError","La cuenta para banco "+f22.getId().getBanco()+" no se encuentra configurada para moneda "+sMoneda +" en " +sCodcomp);
								return bHecho = false;
							}
						}//número de batch y de documento.
						else{
							CodeUtil.putInSessionMap("sMensajeError","No se han realizado los depósitos: ocurrió un error al obtener el número de batch o número de documento");
							return bHecho = false;
						}
					}//ciclo de los métodos de pago.
				}//método de pago para Transferencia y depósito en banco.
				
				//--------------------- DEPÓSITOS PARA TARJETAS DE CRÉDITO -----------------------/
				if(iMpago == 3){
					bHecho=true;
					
					String conceptoComis = "";
					String descripLinea1 = "";
					String descripLinea2 = "";
					
					String sCuentaPos[],sD7sbl,sD7sblt;
					String sCtaPos,sIdCtaPos,sCompCtaPos,sCp1mcu,sCpob,sCpsub; 
					int iMontocom = 0,iMontoPos=0,iCajaUso=0;
					
					//&& ===== True para no borrar el codigo que ya existe y no tener que reacomodarlo.
					if(true){
						
						//------- lista de montos por pos
						String sCuentaTemp[] = null; 
						sCuenta = null;
						for(int i=0; i<bdMontoNetoPos.length; i++){
							
							//----  Determinar la cuenta de caja para el afiliado
							if(iCaid == iCaidPos[i]){
								if(sCuentaTemp == null){
									sCuentaTemp = dv.obtenerCuentaCaja(iCaid, sCodcomp, MetodosPagoCtrl.TARJETA, 
																		sMoneda, session, 
																		null, null, null);
								}
								iCajaUso = iCaid;
								sCuenta = sCuentaTemp;
							}else{
								iCajaUso = iCaidPos[i];
								sCuenta = dv.obtenerCuentaCaja(iCajaUso, sCodcomp, MetodosPagoCtrl.TARJETA, 
																sMoneda,session, 
																null, null, null);
							}
							
							if(sCuenta == null){
								CodeUtil.putInSessionMap("sMensajeError","No se puede obtener la cuenta de caja para depósitos de Afiliados en caja: "+iCaid);
								return bHecho = false;
							}
							//--- Asignación de valores para los asientos de diario 
							sCuentaCaja = sCuenta[0];
							sIdCuentaCaja = sCuenta[1];
							sCompCuentaCaja = sCuenta[2];
							sCc1mcu = sCuenta[3];
							sCcobj  = sCuenta[4];
							sCcsub  = sCuenta[5];
							
							//obtener la cuenta del afiliado.
							sCuentaPos  = dv.obtenerCuentaPOS(iNoReferPos[i]+"", sMoneda, sUninegPOS[i], session, null);
							if(sCuentaPos!=null){
								sCtaPos 	= sCuentaPos[0];
								sIdCtaPos 	= sCuentaPos[1];
								sCompCtaPos = sCuentaPos[2];
								sCp1mcu 	= sCuentaPos[3];
								sCpob 		= sCuentaPos[4];
								sCpsub 		= sCuentaPos[5];
								sD7sbl		= sCuentaPos[6];
								sD7sblt		= sCuentaPos[7];
								sAsientoSucursal = sCuenta[2];
								bHecho = false;
								
								int numerobatch = Divisas.numeroSiguienteJdeE1(   );
								int numeroDocumento =  Divisas.numeroSiguienteJdeE1Custom(valoresJdeNumeracion[8],valoresJdeNumeracion[9] );
								
								iNobatchNodoc = new int[]{numerobatch, numeroDocumento};

								if(iNobatchNodoc != null){
									iNoBatch = iNobatchNodoc[0];
									iNoDocumento = iNobatchNodoc[1];
									
									conceptoComis = "Arqueo "+ iNoarqueo +" Caja " + iCaid + " ComisTc ";
									
									if(sMoneda.equals( monedaBaseCompania.trim() ) ){ 
										
										descripLinea1 = "POS: " + iNoReferPos[i]+ " UN:"+ sCp1mcu  +" C: "+iCajaUso;
										descripLinea2 = "LIQ: " + marcaTarjeta[i] ;

										iMontocom = (int)dv.roundDouble(bdMtoComis[i].multiply(new BigDecimal("100")).doubleValue());
										
										bHecho  = recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sAsientoSucursal, "P9", iNoDocumento, 1.0, iNoBatch, sCtaPos,
												  sIdCtaPos, sCp1mcu, sCpob, sCpsub, "AA", sMoneda, iMontocom, conceptoComis, vaut[0].getId().getLogin(),
												  vaut[0].getId().getCodapp(),BigDecimal.ZERO, sTipoCliente, descripLinea1 ,
												  sCompCtaPos,sD7sbl,sD7sblt, monedaBaseCompania, sCompCtaPos,"D", 0);
										if(bHecho){
											
											bHecho  = recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sAsientoSucursal, "P9", iNoDocumento, 2.0, iNoBatch, sCuentaCaja,
													  sIdCuentaCaja, sCc1mcu, sCcobj, sCcsub, "AA", sMoneda, iMontocom*(-1), conceptoComis, vaut[0].getId().getLogin(),
													  vaut[0].getId().getCodapp(), BigDecimal.ZERO, sTipoCliente, descripLinea2,
													  sCompCuentaCaja,"","", monedaBaseCompania, sCompCuentaCaja,"D", 0);
												
											if(bHecho){
												
												bHecho = recCtrl.registrarBatchA92Session( session, dtFecha,  valoresJdeInsContado[8], iNoBatch, iMontocom, vaut[0].getId().getLogin(), 1 , "APRARQUEO",  valoresJdeInsContado[9]);
												
												if(bHecho){
														
													List<Object[]> lstDeps = new ArrayList<Object[]>();
													if (CodeUtil.getFromSessionMap("rv_listaDepositos") != null)
														lstDeps = (ArrayList<Object[]>) CodeUtil.getFromSessionMap("rv_listaDepositos");
													
													Object oDep[] = new Object[20];
													oDep[0]= iCajaUso;
													oDep[1]= sCodsuc;
													oDep[2]= sCodcomp;
													oDep[3]= iNoBatch;
													oDep[4]= iNoDocumento;
													oDep[5]= iNoarqueo;
													oDep[6]= sMoneda;
													oDep[7]= bdMtoComis[i]; //bdMtoPos[i];
													oDep[8]= dtFecha;
													oDep[9]= HoraArqueo;
													oDep[10]= vaut[0].getId().getLogin();
													oDep[11]= String.valueOf(iNoReferPos[i]);
													oDep[12]= "C";
													oDep[14]= MetodosPagoCtrl.TARJETA;
													oDep[15] = BigDecimal.ZERO;
													
													//&& =========== Crear el objeto que contendra la informacion de los movimientos sobre las cuentas.
													
													lstCtasxDeps = new ArrayList<Ctaxdeposito>();
													ctaxDeps = new Ctaxdeposito();
													ctaxDeps.setMonto(bdMtoComis[i]);
													ctaxDeps.setTipomov("C");
													ctaxDeps.setIdcuenta(sIdCtaPos);
													ctaxDeps.setGmmcu(sCp1mcu);
													ctaxDeps.setGmobj(sCpob);
													ctaxDeps.setGmsub(sCpsub);
													lstCtasxDeps.add(ctaxDeps);
													
													
													//&& ========== Objeto para debito, caja.
													ctaxDeps = new Ctaxdeposito();
													ctaxDeps.setMonto(bdMtoComis[i]);
													ctaxDeps.setTipomov("D");
													ctaxDeps.setIdcuenta(sIdCuentaCaja);
													ctaxDeps.setGmmcu(sCc1mcu);
													ctaxDeps.setGmobj(sCcobj);
													ctaxDeps.setGmsub(sCcsub);
													lstCtasxDeps.add(ctaxDeps);
													oDep[16] = lstCtasxDeps;
													oDep[17] = iNoReferPos[i] ;
													oDep[18] = iNoReferPos[i] ;
													oDep[19] = 0 ;
													
													lstDeps.add(oDep);
													CodeUtil.putInSessionMap("rv_listaDepositos", lstDeps);
														
													if(!bHecho){
														CodeUtil.putInSessionMap("sMensajeError", "Depósito para afiliado "+iNoReferPos[i] + " No registrado, favor intente de nuevo");
														return bHecho = false;															 
													}
												}else{
													CodeUtil.putInSessionMap("sMensajeError", "Depósito para afiliado "+iNoReferPos[i] + " No registrado, favor intente de nuevo");
													return bHecho = false;
												}
											}else{
												CodeUtil.putInSessionMap("sMensajeError", "Depósito para afiliado "+iNoReferPos[i] + " No registrado, favor intente de nuevo");
												return bHecho = false;
											}
										}else{
											CodeUtil.putInSessionMap("sMensajeError", "Depósito para afiliado "+iNoReferPos[i] + " No registrado, favor intente de nuevo");
											return bHecho = false;
										}
									}//Moneda Córdobas.
									else{
									
										iMonto  = (int)(dv.roundDouble((bdMontoNetoPos[i]*100)));
										int iMontodom = (int)(dv.roundDouble((bdMontoNetoPos[i] * tasaCambioOficial.doubleValue() )*100));
										
										iMontocom = (int)dv.roundDouble(bdMtoComis[i].multiply(new BigDecimal("100")).doubleValue());
										int iMtoCoDo =(int)dv.roundDouble(bdMtoComis[i].multiply(tasaCambioOficial).multiply(new BigDecimal("100")).doubleValue());
										
										descripLinea1 = "POS: " + iNoReferPos[i]+ " UN:"+ sCp1mcu  +" C: "+iCajaUso;
										descripLinea2 = "Liq: " + marcaTarjeta[i] ;
										
										
										bHecho  = recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sAsientoSucursal, "P9", iNoDocumento, 1.0, iNoBatch, sCtaPos,
												  sIdCtaPos, sCp1mcu, sCpob, sCpsub, "AA", sMoneda, iMtoCoDo, conceptoComis, vaut[0].getId().getLogin(),
												  vaut[0].getId().getCodapp(), tasaCambioOficial , sTipoCliente, descripLinea1,
												  sCompCtaPos,sD7sbl,sD7sblt, monedaBaseCompania, sCompCtaPos,"F", iMontocom);
										if(bHecho){
											bHecho  = recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sAsientoSucursal, "P9", iNoDocumento, 1.0, iNoBatch, sCtaPos,
													  sIdCtaPos, sCp1mcu, sCpob, sCpsub, "CA", sMoneda, iMontocom, conceptoComis, vaut[0].getId().getLogin(),
													  vaut[0].getId().getCodapp(), tasaCambioOficial, sTipoCliente, descripLinea1, 
													  sCompCtaPos,sD7sbl,sD7sblt, monedaBaseCompania, sCompCtaPos,"F", 0);
											if(bHecho){
												bHecho = recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sAsientoSucursal, "P9", iNoDocumento, 2.0, iNoBatch, sCuentaCaja,
														 sIdCuentaCaja, sCc1mcu, sCcobj, sCcsub, "AA", sMoneda, iMtoCoDo*(-1), conceptoComis, vaut[0].getId().getLogin(), 
														 vaut[0].getId().getCodapp(),tasaCambioOficial, sTipoCliente, descripLinea2 ,sCompCuentaCaja, 
														 "", "", monedaBaseCompania, sCompCuentaCaja,"F", (iMontocom*(-1)) );
												if(bHecho){
													bHecho = recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sAsientoSucursal, "P9", iNoDocumento, 2.0, iNoBatch, sCuentaCaja,
															 sIdCuentaCaja, sCc1mcu, sCcobj, sCcsub, "CA", sMoneda, iMontocom*(-1), conceptoComis, vaut[0].getId().getLogin(), 
															 vaut[0].getId().getCodapp(), tasaCambioOficial, sTipoCliente, descripLinea2,
															 sCompCuentaCaja, "", "", monedaBaseCompania, sCompCuentaCaja,"F", 0);
													if(bHecho){
														
														bHecho = recCtrl.registrarBatchA92Session( session, dtFecha,  valoresJdeInsContado[8], iNoBatch, iMontocom, vaut[0].getId().getLogin(), 1 , "APRARQUEO" ,  valoresJdeInsContado[9]);
														
														if(bHecho){
															
															List<Object[]> lstDeps = new ArrayList<Object[]>();
															if (CodeUtil.getFromSessionMap("rv_listaDepositos") != null)
																lstDeps = (ArrayList<Object[]>) CodeUtil.getFromSessionMap("rv_listaDepositos");
															
															Object oDep[] = new Object[20];
															oDep[0]= iCajaUso;
															oDep[1]= sCodsuc;
															oDep[2]= sCodcomp;
															oDep[3]= iNoBatch;
															oDep[4]= iNoDocumento;
															oDep[5]= iNoarqueo;
															oDep[6]= sMoneda;
															oDep[7]= bdMtoComis[i];
															oDep[8]= dtFecha;
															oDep[9]= HoraArqueo;
															oDep[10]= vaut[0].getId().getLogin();
															oDep[11]= String.valueOf(iNoReferPos[i]);
															oDep[12]= "C";
															oDep[14]= MetodosPagoCtrl.TARJETA;
															oDep[15] = tasaCambioOficial;
															
															//&& =========== Crear el objeto que contendra la informacion de los movimientos sobre las cuentas.
															lstCtasxDeps = new ArrayList<Ctaxdeposito>();
															ctaxDeps = new Ctaxdeposito();
															ctaxDeps.setMonto(bdMtoComis[i]);
															ctaxDeps.setTipomov("C");
															ctaxDeps.setIdcuenta(sIdCtaPos);
															ctaxDeps.setGmmcu(sCp1mcu);
															ctaxDeps.setGmobj(sCpob);
															ctaxDeps.setGmsub(sCpsub);
															lstCtasxDeps.add(ctaxDeps);
															
															//&& ========== Objeto para debito, caja.
															ctaxDeps = new Ctaxdeposito();
															ctaxDeps.setMonto(bdMtoComis[i]);
															ctaxDeps.setTipomov("D");
															ctaxDeps.setIdcuenta(sIdCuentaCaja);
															ctaxDeps.setGmmcu(sCc1mcu);
															ctaxDeps.setGmobj(sCcobj);
															ctaxDeps.setGmsub(sCcsub);
															lstCtasxDeps.add(ctaxDeps);
															oDep[16] = lstCtasxDeps;
															oDep[17] = iNoReferPos[i] ;
															oDep[18] = iNoReferPos[i] ;
															oDep[19] = 0 ;
															
															lstDeps.add(oDep);	
															CodeUtil.putInSessionMap("rv_listaDepositos", lstDeps);
															
															if(!bHecho){
																CodeUtil.putInSessionMap("sMensajeError", "Depósito para afiliado "+iNoReferPos[i] + " No registrado, favor intente de nuevo");
																return bHecho = false;
															}
														}
														else{
															CodeUtil.putInSessionMap("sMensajeError", "Depósito para afiliado "+iNoReferPos[i] + " No registrado, favor intente de nuevo");
															return bHecho = false;
														}
													}else{
														CodeUtil.putInSessionMap("sMensajeError", "Depósito para afiliado "+iNoReferPos[i] + " No registrado, favor intente de nuevo");
														return bHecho = false;
													}
												}else{
													CodeUtil.putInSessionMap("sMensajeError", "Depósito para afiliado "+iNoReferPos[i] + " No registrado, favor intente de nuevo");
													return bHecho = false;
												}
											}else{
												CodeUtil.putInSessionMap("sMensajeError", "Depósito para afiliado "+iNoReferPos[i] + " No registrado, favor intente de nuevo");
												return bHecho = false;
											}
										}else{
											CodeUtil.putInSessionMap("sMensajeError", "Depósito para afiliado "+iNoReferPos[i] + " No registrado, favor intente de nuevo");
											return bHecho = false;
										}
									}//moneda foránea.
								}else{
									CodeUtil.putInSessionMap("sMensajeError", "Depósito para afiliado "+iNoReferPos[i] + " No registrado, favor intente de nuevo");
									return bHecho = false;
								}
							}else{
								CodeUtil.putInSessionMap("sMensajeError","No se encuentra configurada la cuenta para el afiliado "+iNoReferPos[i]+" en "+sMoneda+ ", Unidad de Neg: "+sUninegPOS[i]);
								return bHecho = false;
							}
						}//cierre del for de montos pos
					}else{
						CodeUtil.putInSessionMap("sMensajeError","No se puede obtener la cuenta de banco para depósitos de Afiliados de caja: "+iCajaUso);
						return bHecho = false;
					}
				}//método de pago H, depósitos con tarjetos de crédito.
				
		} catch (Exception error) {
			CodeUtil.putInSessionMap("sMensajeError", "Traslado de depósitos no procesado,  Favor intente nuevamente ");
//			LogCrtl.imprimirError(error);
			return bHecho = false;
		}
		return bHecho;
	}	
	
	/***************************************************************************************************/
	/** Método para realizar batch y asientos diarios para los depósitos a banco por métodos de pago
	 **********************/
		public boolean realizarTrasladoBanco2(
			  int iNoarqueo, int iCaid, final String sCodcomp, String sCodsuc, Date dtFecha, Date HoraArqueo, String sReferdep, double dMonto5, double dMontoQ,
			  List lstRecN8, BigDecimal bdMtoPos[], double bdMontoNetoPos[], String sAfiliados[], String sUninegPOS[], BigDecimal bdMtoComis[],
			  String iNoReferPos[], int iCaidPos[], final String sMoneda, int iMpago, 
			  String sCtaBcoTransitoria[],  F55ca014 f14 , String[] marcaTarjeta, Varqueo ar,
			  Session session, String sTipoCliente, BigDecimal tasaCambioOficial
				){
			
			boolean bHecho = true;
			int iNobatchNodoc[];
			String sMensaje = "";
			String sCompCuentaCaja="",sCompCuentaBanco="",sFecha,sCuenta[],sAsientoSucursal="";
			String sConcepto, sCtmcu = "", sCtobj = "",sCtsub = "",sCcobj="", sCcsub="",sCc1mcu="" ;
			String sIdCuentaCaja = "", sIdCtaTranBanco = "", sCuentaCaja = "", sCtaTranBanco = "";
			int iNoBatch = 0, iNoDocumento = 0,  iMontoQ ;
			SimpleDateFormat format ;
			Vautoriz[] vaut;
			Vf55ca01 f55ca01;
			 
			Divisas dv = new Divisas();
			ReciboCtrl recCtrl = new ReciboCtrl();
		 
			ClsParametroCaja cajaparm = new ClsParametroCaja();
			
			int iCodigoBanco=0;
			F55ca022 f22 = null;
			String sTipoDoc = "ZX";
			String sDetLineaBco = "";
			List<Ctaxdeposito> lstCtasxDeps = null;
			Ctaxdeposito ctaxDeps = null;
			
			long iMonto  = 0 ;
			long iMonto5 = 0 ;
			
			String strSubLibroCuenta = "" ;
			String strTipoAuxiliarCtB = "";
			int tipogenerareferencia = 0 ; // 0: original, 1: cambiada por sistema, 2: consecutiva de jde
			
			try {
				//------ Leer y actualizar el número de documento y número de batch ------/
				//------ Obtener el tipo de cliente y el login del usuario ---------------/
				f55ca01 = (Vf55ca01)((List)CodeUtil.getFromSessionMap( "lstCajas")).get(0);
				vaut = (Vautoriz[]) CodeUtil.getFromSessionMap( "sevAut");
				
				format = new SimpleDateFormat("dd/MM/yyyy");
				sFecha = format.format(dtFecha);
				sConcepto = "Arqueo "+iNoarqueo+" Caja "+iCaid+ " "+ sFecha;
				String logs = sConcepto;
				
				iCodigoBanco = f14.getId().getC4bnc();
				
				String monedaBaseCompania = f14.getId().getC4bcrcd() ;
				
				
				//------------ ASIENTO PARA EFECTIVO Y CHEQUES --------------- //.			
				if(iMpago == 1){
					
					iNoBatch = Divisas.numeroSiguienteJdeE1(   );
	 
					sCuenta = dv.obtenerCuentaCaja(iCaid, sCodcomp, MetodosPagoCtrl.EFECTIVO , sMoneda, session, null, null, null);
					
					if(sCuenta != null){
						
						sCuentaCaja = sCuenta[0];
						sIdCuentaCaja = sCuenta[1];
						sCompCuentaCaja = sCuenta[2];
						sCc1mcu = sCuenta[3];
						sCcobj  = sCuenta[4];
						sCcsub  = sCuenta[5];
						sAsientoSucursal= sCuenta[2];
						
						//&& ====== Validar que cuenta de banco se debe utilizar si transitoria o directo a banco
						f22 =  BancoCtrl.obtenerBancoxId( iCodigoBanco, session );
						if(f22 == null){
							CodeUtil.putInSessionMap("sMensajeError","ACF55CA022: Error al recuperar datos de banco "+iCodigoBanco+", Favor intente de nuevo. ");
							return false;
						}
						
						if( ar.getId().getDepctatran() == 1){
							 
							tipogenerareferencia = 2; 
							
							if(sCtaBcoTransitoria == null){
								CodeUtil.putInSessionMap("sMensajeError","ACF55CA033: No se encuentra configurada la cuenta transitoria de banco "+f22.getId().getBanco());
								return false;
							}
							sCuenta = sCtaBcoTransitoria;
							
							sTipoDoc = cajaparm.getParametros("34", "0", "CIERRE_DOCTYPE_P9").getValorAlfanumerico().toString();
							
							sDetLineaBco = "REF:"+ar.getId().getReferencenumber()+" MP:Q5 C:"+ar.getId().getCaid();
							
							
							iNoDocumento = Divisas.numeroSiguienteJdeE1Custom(valoresJdeNumeracion[8],valoresJdeNumeracion[9] );
							
							strSubLibroCuenta = ConfirmaDepositosCtrl.constructSubLibroCtaTbanco (0, iCodigoBanco, iCaid, sMoneda, sCodcomp );
							strTipoAuxiliarCtB = cajaparm.getParametros("34", "0", "CODIGO_TIPO_AUX_CT").getValorAlfanumerico().toString(); //PropertiesSystem.CODIGO_TIPO_AUXILIAR_CT ;
						}	
						else{
							
							iNoDocumento = ar.getId().getReferencenumber();
							if(iNoDocumento == 0){
								iNoDocumento = Integer.parseInt(ar.getId().getReferdep());
								ar.getId().setReferencenumber(iNoDocumento);
							}
								
							sTipoDoc =cajaparm.getParametros("34", "0", "TIPODOC_JDE_DEP_5").getValorAlfanumerico().toString(); //PropertiesSystem.TIPODOC_JDE_DEP_5;
							sDetLineaBco = "REF:"+ar.getId().getReferencenumber()+" MP:Q5 C:"+ar.getId().getCaid();
								
							sCuenta = dv.obtenerCuentaBanco(sCodcomp, sMoneda,iCodigoBanco, session, null, null, null);
							strSubLibroCuenta = "";
							strTipoAuxiliarCtB = "";
							
							//&& =============== Validaciones del numero de referencia
							
							iNoDocumento = RevisionArqueoCtrl.generarReferenciaDeposito(iNoDocumento, MetodosPagoCtrl.EFECTIVO , sAsientoSucursal, sMoneda);
							
							//********************************************************
						}
						
						 
						//&& =========== hacer asientos de diario
						if(sCuenta != null){
							sCtaTranBanco 	= sCuenta[0];
							sIdCtaTranBanco = sCuenta[1];
							sCompCuentaBanco= sCuenta[2];
							sCtmcu			= sCuenta[3];
							sCtobj			= sCuenta[4];
							sCtsub			= sCuenta[5];	
							//sAsientoSucursal= sCuenta[2];
						
							//---------- Guardar el asiento diario.----------
							if(sMoneda.equals( monedaBaseCompania .trim() ) ){
								if(dMonto5>0){
									dMonto5 = dv.roundDouble(dMonto5);
									iMonto5 = (long)(dv.roundDouble(dMonto5 * 100));		
									
									bHecho = recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sAsientoSucursal,sTipoDoc, iNoDocumento, 1.0, iNoBatch, sCtaTranBanco,
														sIdCtaTranBanco, sCtmcu, sCtobj, sCtsub, "AA", sMoneda, iMonto5, sConcepto,
														vaut[0].getId().getLogin(),	vaut[0].getId().getCodapp(), BigDecimal.ZERO, sTipoCliente,
														sDetLineaBco,sCompCuentaBanco, strSubLibroCuenta, strTipoAuxiliarCtB, sMoneda, sCompCuentaBanco,"D", 0);
									if(!bHecho){
										CodeUtil.putInSessionMap("sMensajeError", "Error en Depósito de Efectivo: "
																+recCtrl.getError().toString().split("@")[1]);
										return bHecho =  false;
									}
									bHecho = recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sAsientoSucursal, sTipoDoc, iNoDocumento, 2.0, iNoBatch, sCuentaCaja,
														sIdCuentaCaja, sCc1mcu, sCcobj, sCcsub, "AA", sMoneda, iMonto5*(-1), sConcepto, 
														vaut[0].getId().getLogin(),	vaut[0].getId().getCodapp(),BigDecimal.ZERO, sTipoCliente,
														"Crédito caja Efectivo-cheque",sCompCuentaCaja, "", "",sMoneda,sCompCuentaCaja,"D", 0 );
									if(!bHecho){
										CodeUtil.putInSessionMap("sMensajeError", "Error en Depósito de Efectivo: "
																+recCtrl.getError().toString().split("@")[1]);
										return bHecho = false;
									}
								}
							}//moneda córdobas.
							else{
								
								long iMonto5Dom;								
								
								if(dMonto5 >0){							
									iMonto5Dom = (long)(dv.roundDouble((dMonto5 * tasaCambioOficial.doubleValue() )*100));
									iMonto5    = (long)(dv.roundDouble((dMonto5*100)));
									
									bHecho = recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sAsientoSucursal, sTipoDoc, iNoDocumento, 1.0, 
														iNoBatch, sCtaTranBanco, sIdCtaTranBanco, sCtmcu, sCtobj, 
														sCtsub, "AA", sMoneda, iMonto5Dom, sConcepto , vaut[0].getId().getLogin(),
														vaut[0].getId().getCodapp(), tasaCambioOficial, sTipoCliente, sDetLineaBco,
														sCompCuentaBanco,  strSubLibroCuenta, strTipoAuxiliarCtB, monedaBaseCompania, sCompCuentaBanco,"F", iMonto5);
									if(!bHecho){
										CodeUtil.putInSessionMap("sMensajeError",  "Error en Depósito de Efectivo: "
																+recCtrl.getError().toString().split("@")[1]);
										return bHecho = false;
									}
									bHecho = recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sAsientoSucursal,sTipoDoc, iNoDocumento, 1.0, 
														iNoBatch, sCtaTranBanco, sIdCtaTranBanco, sCtmcu, sCtobj, 
														sCtsub, "CA", sMoneda, iMonto5, sConcepto, vaut[0].getId().getLogin(),
														vaut[0].getId().getCodapp(), tasaCambioOficial, sTipoCliente,
														sDetLineaBco,sCompCuentaBanco,  strSubLibroCuenta, strTipoAuxiliarCtB, monedaBaseCompania,sCompCuentaBanco,"F", 0);
									if(!bHecho){
										CodeUtil.putInSessionMap("sMensajeError", "Error en Depósito de Efectivo: "
																+recCtrl.getError().toString().split("@")[1]);
										return bHecho = false;
									}
									bHecho = recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sAsientoSucursal, sTipoDoc, iNoDocumento, 2.0, 
														iNoBatch, sCuentaCaja, sIdCuentaCaja, sCc1mcu, sCcobj, 
														sCcsub, "AA", sMoneda, iMonto5Dom*(-1), sConcepto, vaut[0].getId().getLogin(),
														vaut[0].getId().getCodapp(), tasaCambioOficial, sTipoCliente,"Cred. Caja Efectivo-Cheque",
														sCompCuentaCaja, "", "", monedaBaseCompania, sCompCuentaCaja, "F", (iMonto5 *(-1) ) ); 
									if(!bHecho){
										CodeUtil.putInSessionMap("sMensajeError", "Error en Depósito de Efectivo: "
																+recCtrl.getError().toString().split("@")[1]);
										return bHecho = false;
									}
									bHecho = recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sAsientoSucursal, sTipoDoc, iNoDocumento, 2.0, 
														iNoBatch, sCuentaCaja, sIdCuentaCaja, sCc1mcu, sCcobj, 
														sCcsub, "CA", sMoneda, iMonto5*(-1), sConcepto,	vaut[0].getId().getLogin(),	
														vaut[0].getId().getCodapp(), tasaCambioOficial, sTipoCliente, "Cred. Caja Efectivo-Cheque",
														sCompCuentaCaja, "", "", monedaBaseCompania, sCompCuentaCaja,"F", 0);
									if(!bHecho){
										CodeUtil.putInSessionMap("sMensajeError", "Error en Depósito de Efectivo: "
																+recCtrl.getError().toString().split("@")[1]);
										return bHecho = false;
									}
								}
							}
						}else{
							CodeUtil.putInSessionMap("sMensajeError","Error obtener la cuenta de banco para depósito de cheques y efectivo en moneda "+sMoneda+", Caja: "+iCaid+ ", Suc: " + sCodsuc+", Comp: "+sCodcomp);
							return bHecho = false;
						}
					}else{
						CodeUtil.putInSessionMap("sMensajeError","Error obtener la cuenta de caja para depósito de cheques y efectivo en moneda "+sMoneda+", Caja: "+iCaid+ ", Suc: " + sCodsuc+", Comp: "+sCodcomp);
						return bHecho = false;
					}						
			 					
					BigDecimal bdMonto = new BigDecimal(Double.toString(dMonto5))
										.add(new BigDecimal(Double.toString(dMontoQ)))
										.setScale(2, RoundingMode.HALF_UP);
					
					iMonto = Long.parseLong( String.format("%1$.2f", bdMonto).replace(".", "")  ); 
				
					if(bHecho){
						
						bHecho = recCtrl.registrarBatchA92Session( session, dtFecha,  valoresJdeInsContado[8], iNoBatch, iMonto, vaut[0].getId().getLogin(), 1, "APRARQUEO",  valoresJdeInsContado[9]);
						
						if(bHecho){
							
							List<Object[]> lstDeps = new ArrayList<Object[]>();
							if (CodeUtil.getFromSessionMap("rv_listaDepositos") != null)
								lstDeps = (ArrayList<Object[]>) CodeUtil.getFromSessionMap("rv_listaDepositos");
							
							Object oDep[] = new Object[20];
							oDep[0]= iCaid;
							oDep[1]= sCodsuc;
							oDep[2]= sCodcomp;
							oDep[3]= iNoBatch;
							oDep[4]= iNoDocumento;
							oDep[5]= iNoarqueo;
							oDep[6]= sMoneda;
							oDep[7]= bdMonto.setScale(2, RoundingMode.HALF_UP);
							oDep[8]= dtFecha;
							oDep[9]= HoraArqueo;
							oDep[10]= vaut[0].getId().getLogin();
							oDep[11]= iNoDocumento; //sReferdep;  
							oDep[12]= "D";
							oDep[14]= MetodosPagoCtrl.EFECTIVO ;
							oDep[15] = tasaCambioOficial.setScale(4, RoundingMode.HALF_UP);
							
							//&& =========== Crear el objeto que contendra la informacion de los movimientos sobre las cuentas.
							lstCtasxDeps = new ArrayList<Ctaxdeposito>();
							ctaxDeps = new Ctaxdeposito();
							ctaxDeps.setMonto(new BigDecimal(String.valueOf(dMonto5)));
							ctaxDeps.setTipomov("C");
							ctaxDeps.setIdcuenta(sIdCtaTranBanco);
							ctaxDeps.setGmmcu(sCtmcu);
							ctaxDeps.setGmobj(sCtobj);
							ctaxDeps.setGmsub(sCtsub);
							lstCtasxDeps.add(ctaxDeps);
							
							//&& ========== Objeto para debito, caja.
							ctaxDeps = new Ctaxdeposito();
							ctaxDeps.setMonto(new BigDecimal(String.valueOf(dMonto5)));
							ctaxDeps.setTipomov("D");
							ctaxDeps.setIdcuenta(sIdCuentaCaja);
							ctaxDeps.setGmmcu(sCc1mcu);
							ctaxDeps.setGmobj(sCcobj);
							ctaxDeps.setGmsub(sCcsub);
							lstCtasxDeps.add(ctaxDeps);
							
							oDep[16] = lstCtasxDeps;
							oDep[17] = iCodigoBanco;
							oDep[18] = ar.getId().getReferencenumber();
							oDep[19] = ar.getId().getDepctatran();
							
							lstDeps.add(oDep);	
							CodeUtil.putInSessionMap("rv_listaDepositos", lstDeps);
							
						}else{
							CodeUtil.putInSessionMap("sMensajeError","Error al guardar el batch de efectivo y cheques en "+sMoneda+", Caja: "+iCaid+ ", Suc: " + sCodsuc+", Comp: "+sCodcomp);
						}
					}//validación de asientos para el batch.
				}//método de pago cheques y efectivo.
					
					/* *************************************************************************** */
					/* ************* REGISTRAR BATCH'S PARA TRANSFERENCIAS Y DEPÓSITOS. ********** */
					if(iMpago == 2){
						
						String sCuentaCaja8[],sCuentaCajaN[],sRefer="",sSucursalCtaBco="";					
						boolean bCuentaN=false,bCuenta8=false;
						String sDescrip ="";
						
						//---------------
						sCuentaCaja8 = dv.obtenerCuentaCaja(iCaid, sCodcomp, MetodosPagoCtrl.TRANSFERENCIA, sMoneda, session, null, null, null);
						if(sCuentaCaja8 != null){
							bCuenta8 = true;
						}
						sCuentaCajaN = dv.obtenerCuentaCaja(iCaid, sCodcomp, MetodosPagoCtrl.DEPOSITO, sMoneda, session,null, null, null);
						if(sCuentaCajaN != null){
							bCuentaN = true;
						}
						
						//***************************************************************************//
						Set<Integer> codigosBanco = new HashSet<Integer>();
						
						//&& ===== lista unica de codigos de banco
						for (int i = 0; i < lstRecN8.size(); i++) {
							Vrecibodevrecibo v = (Vrecibodevrecibo) lstRecN8.get(i);
							codigosBanco.add(Integer.valueOf( v.getId().getRefer1() ) )  ;
						}
						
						//&& ======  crear lista de cuentas transitorias por banco
						List<String[]> cuentasTransitoriasBanco = new ArrayList<String[]>();
						for (Integer codigo : codigosBanco) {
							
							String[] cuentaTransitoria  = dv.obtenerCuentaTransitoBanco(sMoneda, sCodcomp, codigo, session);
							if(cuentaTransitoria == null )
								continue;
							
							cuentasTransitoriasBanco.add(cuentaTransitoria);
						}
						
						//***************************************************************************//
						
						
						//-------- recorrer la lista de recibos y clasificar por mpago. 
						for(int i=0; i<lstRecN8.size();i++){
							
							Vrecibodevrecibo v = (Vrecibodevrecibo)lstRecN8.get(i);
							if(v.getId().getMpago().equals(MetodosPagoCtrl.DEPOSITO)){
								if(bCuentaN){
									sDescrip = "Dep.Banco";
									sRefer   = v.getId().getRefer2();
									sCuentaCaja 	= sCuentaCajaN[0];
									sIdCuentaCaja 	= sCuentaCajaN[1];
									sCompCuentaCaja = sCuentaCajaN[2];
									sCc1mcu 		= sCuentaCajaN[3];
									sCcobj 			= sCuentaCajaN[4];
									sCcsub 			= sCuentaCajaN[5];
									
									if( v.getId().getReferencenumber() == 0){
										v.getId().setReferencenumber(Integer.parseInt(v.getId().getRefer2().trim()));
									}
									
								}else{
									CodeUtil.putInSessionMap("sMensajeError", "Error al obtener la cuenta " +sMoneda +" de caja "+iCaid+", compañía "+sCodcomp.trim()+" para método de pago Deposito en bancos ");
									return bHecho = false;
								}
							}else{
								if(bCuenta8){
									sDescrip = "Trans.Banco";
									sRefer   = v.getId().getRefer3();
									sCuentaCaja 	= sCuentaCaja8[0];
									sIdCuentaCaja 	= sCuentaCaja8[1];
									sCompCuentaCaja = sCuentaCaja8[2];
									sCc1mcu 		= sCuentaCaja8[3];
									sCcobj 			= sCuentaCaja8[4];
									sCcsub 			= sCuentaCaja8[5];
									
									if(v.getId().getReferencenumber() == 0){
										v.getId().setReferencenumber(Integer.parseInt(v.getId().getRefer3().trim()));
									}
									
								}else{
									CodeUtil.putInSessionMap("sMensajeError", "Error al obtener la cuenta " +sMoneda +" de caja "+iCaid+", compañía "+sCodcomp.trim()+", para método de pago Transferencia Bancarias ");
									return bHecho = false;
								}
							}
							
							iCodigoBanco = Integer.parseInt(v.getId().getRefer1().trim());
							
							//-------------- número de batch y de documento.
							//iNoBatch = dv.leerActualizarNoBatch();
							iNoBatch =  Divisas.numeroSiguienteJdeE1(   );
	 
							if(bHecho){
								f22 =  BancoCtrl.obtenerBancoxId( iCodigoBanco, session );
								if(f22 == null){
									CodeUtil.putInSessionMap("sMensajeError", "F55CA022: No se puedo obtener la información de banco: " + iCodigoBanco);
									return bHecho = false;
								}
								
								//&& ========================= Cargar el deposito a cuenta transitoria
								if(  v.getId().getDepctatran() == 1){
									
									final String strCodigoBancoBuscar = v.getId().getRefer1().trim();
									
									sCtaBcoTransitoria = (String[])
									 CollectionUtils.find(cuentasTransitoriasBanco, new Predicate(){

										@Override
										public boolean evaluate(Object o) {
											String [] ctatrans = (String[])o;
											return
											ctatrans[6].trim().compareTo(sMoneda.trim() ) == 0 && 
											ctatrans[7].trim().compareTo(sCodcomp.trim() ) == 0 &&  
											ctatrans[8].trim().compareTo(strCodigoBancoBuscar) == 0 ;
										}
									 });
									
									
									if(sCtaBcoTransitoria == null ){
										CodeUtil.putInSessionMap("sMensajeError","ACF55CA033: No se encuentra configurada la cuenta transitoria de banco "+f22.getId().getBanco());
										return bHecho = false;
									}
									
							
									iNoDocumento =  Divisas.numeroSiguienteJdeE1Custom(valoresJdeNumeracion[8],valoresJdeNumeracion[9] );
									sCuenta = sCtaBcoTransitoria;
									
									sTipoDoc =cajaparm.getParametros("34", "0", "CIERRE_DOCTYPE_P9").getValorAlfanumerico().toString();
									 
									sDetLineaBco = "REF:"+ v.getId().getReferencenumber() + " MP:" + v.getId().getMpago().trim()+" RC:" + v.getId().getNumrec();
									
									strSubLibroCuenta = ConfirmaDepositosCtrl.constructSubLibroCtaTbanco(0, f22.getId().getCodb(), iCaid, sMoneda, sCodcomp);
									
									strTipoAuxiliarCtB = cajaparm.getParametros("34", "0", "CODIGO_TIPO_AUX_CT").getValorAlfanumerico().toString(); //PropertiesSystem.CODIGO_TIPO_AUXILIAR_CT ;
									
								}else{
									
									iNoDocumento = v.getId().getReferencenumber();
									
									sCuenta = dv.obtenerCuentaBanco(sCodcomp, sMoneda, iCodigoBanco , session, null, null, null);
									
									sTipoDoc =  v.getId().getMpago().trim().compareTo(MetodosPagoCtrl.DEPOSITO) == 0 ?
											cajaparm.getParametros("34", "0", "TIPODOC_JDE_DEP_N").getValorAlfanumerico().toString()://PropertiesSystem.TIPODOC_JDE_DEP_N: 
											cajaparm.getParametros("34", "0", "TIPODOC_JDE_DEP_8").getValorAlfanumerico().toString();//PropertiesSystem.TIPODOC_JDE_DEP_8 ;
									
									sDetLineaBco = "REF:"+v.getId().getReferencenumber()+" MP:"+v.getId().getMpago().trim()+" RC:" + v.getId().getNumrec();
									
									strSubLibroCuenta = "";
									strTipoAuxiliarCtB = "";
									
									//&& =============== Validaciones del numero de referencia
									
									iNoDocumento = RevisionArqueoCtrl.generarReferenciaDeposito(iNoDocumento, v.getId().getMpago().trim(), sCompCuentaCaja, sMoneda);
									
								}

								if(sCuenta != null){
									sCtaTranBanco 	= sCuenta[0];
									sIdCtaTranBanco = sCuenta[1];
									sCompCuentaBanco= sCuenta[2];
									sCtmcu			= sCuenta[3];
									sCtobj			= sCuenta[4];
									sCtsub			= sCuenta[5];
									sSucursalCtaBco = sCompCuentaCaja; 
									
									
									iMonto = (long)dv.roundDouble((v.getId().getMontoneto().doubleValue() * 100));
									
									//&& ====== Encabezado
									bHecho = recCtrl.registrarBatchA92Session( session, dtFecha,  valoresJdeInsContado[8], iNoBatch, iMonto, vaut[0].getId().getLogin(), 1, "APRARQUEO",  valoresJdeInsContado[9]);
									
									if(!bHecho){
										CodeUtil.putInSessionMap("sMensajeError", "No se registró Batch para "
												+ (( v.getId().getMpago().compareTo(MetodosPagoCtrl.TRANSFERENCIA) == 0? "Transferencia":"Depósito"))
												+" en Recibo #"+v.getId().getNumrec()+":  "
												+recCtrl.getError().toString().split("@")[1]);
										return bHecho = false;
									}
									
									//-----------------  guardar el asiento diario
									if(sMoneda.equals(  monedaBaseCompania.trim()  )){
										
										//& ======= Cuerpo moneda domestica.
										bHecho = recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sSucursalCtaBco,sTipoDoc, iNoDocumento, 1.0, iNoBatch, sCtaTranBanco,
													sIdCtaTranBanco, sCtmcu, sCtobj, sCtsub, "AA", sMoneda, iMonto, sConcepto,vaut[0].getId().getLogin(),
													vaut[0].getId().getCodapp(), BigDecimal.ZERO, sTipoCliente, sDetLineaBco ,sCompCuentaBanco,
													strSubLibroCuenta, strTipoAuxiliarCtB, monedaBaseCompania, sCompCuentaBanco,"D", 0);
										if(!bHecho){
											CodeUtil.putInSessionMap("sMensajeError", "No se registró Batch para "
													+ (( v.getId().getMpago().compareTo(MetodosPagoCtrl.TRANSFERENCIA) == 0? "Transferencia":"Depósito"))
													+" en Recibo #"+v.getId().getNumrec()+":  "
													+recCtrl.getError().toString().split("@")[1]);
											return bHecho = false;
										}
										bHecho = recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sSucursalCtaBco,sTipoDoc, iNoDocumento, 2.0, iNoBatch, sCuentaCaja,
															sIdCuentaCaja, sCc1mcu, sCcobj, sCcsub, "AA", sMoneda, iMonto*(-1), sConcepto,vaut[0].getId().getLogin(), 
															vaut[0].getId().getCodapp(),BigDecimal.ZERO, sTipoCliente,"Crédito Caja "+sDescrip, sCompCuentaCaja,
															"","", monedaBaseCompania,sCompCuentaCaja,"D", 0);
										if(!bHecho){
											CodeUtil.putInSessionMap("sMensajeError", "No se registró Batch para "
													+ (( v.getId().getMpago().compareTo(MetodosPagoCtrl.TRANSFERENCIA) == 0? "Transferencia":"Depósito"))
													+" en Recibo #"+v.getId().getNumrec()+":  "
													+recCtrl.getError().toString().split("@")[1]);
											return bHecho = false;
										}
									}// moneda córdobas
									else{
										
										iMonto 		= (long)(dv.roundDouble((v.getId().getMontoneto().doubleValue() * 100)));
										long iMontodom	= (long)(dv.roundDouble(((v.getId().getMontoneto().doubleValue() * tasaCambioOficial.doubleValue() )*100)));
										
										bHecho  =	recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sSucursalCtaBco,sTipoDoc, iNoDocumento, 1.0, iNoBatch, sCtaTranBanco,
															sIdCtaTranBanco, sCtmcu, sCtobj, sCtsub, "AA", sMoneda, iMontodom, sConcepto,vaut[0].getId().getLogin(),
															vaut[0].getId().getCodapp(), tasaCambioOficial, sTipoCliente,sDetLineaBco, sCompCuentaBanco,
															strSubLibroCuenta,strTipoAuxiliarCtB, monedaBaseCompania, sCompCuentaBanco,"F", iMonto );
										if(!bHecho){
											CodeUtil.putInSessionMap("sMensajeError", "No se registró Batch para "
													+ (( v.getId().getMpago().compareTo(MetodosPagoCtrl.TRANSFERENCIA) == 0? "Transferencia":"Depósito"))
													+" en Recibo #"+v.getId().getNumrec()+":  "
													+recCtrl.getError().toString().split("@")[1]);
											return bHecho = false;
										}
										bHecho  =	recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sSucursalCtaBco,sTipoDoc, iNoDocumento, 1.0, iNoBatch, sCtaTranBanco,
														 	sIdCtaTranBanco, sCtmcu, sCtobj, sCtsub, "CA", sMoneda, iMonto, sConcepto,vaut[0].getId().getLogin(),
														 	vaut[0].getId().getCodapp(), tasaCambioOficial, sTipoCliente, sDetLineaBco, sCompCuentaBanco,
														 	strSubLibroCuenta,strTipoAuxiliarCtB, monedaBaseCompania,sCompCuentaBanco, "F", 0);
										if(!bHecho){
											CodeUtil.putInSessionMap("sMensajeError", "No se registró Batch para "
													+ (( v.getId().getMpago().compareTo(MetodosPagoCtrl.TRANSFERENCIA) == 0? "Transferencia":"Depósito"))
													+" en Recibo #"+v.getId().getNumrec()+":  "
													+recCtrl.getError().toString().split("@")[1]);
											return bHecho = false;
										}
										bHecho = recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sSucursalCtaBco,sTipoDoc, iNoDocumento, 2.0, iNoBatch, sCuentaCaja,
															 sIdCuentaCaja, sCc1mcu, sCcobj, sCcsub, "AA", sMoneda, iMontodom*(-1), sConcepto,vaut[0].getId().getLogin(), 
															 vaut[0].getId().getCodapp(), tasaCambioOficial, sTipoCliente,"Crédito Caja "+ sDescrip,sCompCuentaCaja,
															 "","", monedaBaseCompania, sCompCuentaCaja,"F", ( iMonto*(-1) ) );
										if(!bHecho){
											CodeUtil.putInSessionMap("sMensajeError", "No se registró Batch para "
													+ (( v.getId().getMpago().compareTo(MetodosPagoCtrl.TRANSFERENCIA) == 0? "Transferencia":"Depósito"))
													+" en Recibo #"+v.getId().getNumrec()+":  "
													+recCtrl.getError().toString().split("@")[1]);
											return bHecho = false;
										}
										bHecho = recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sSucursalCtaBco, sTipoDoc, iNoDocumento, 2.0, iNoBatch, sCuentaCaja,
															 sIdCuentaCaja, sCc1mcu, sCcobj, sCcsub, "CA", sMoneda, iMonto*(-1), sConcepto,vaut[0].getId().getLogin(), 
															 vaut[0].getId().getCodapp(), tasaCambioOficial, sTipoCliente,"Crédito Caja "+ sDescrip,sCompCuentaCaja,
															 "","", monedaBaseCompania, sCompCuentaCaja,"F", 0);
										if(!bHecho){
											CodeUtil.putInSessionMap("sMensajeError", "No se registró Batch para "
													+ (( v.getId().getMpago().compareTo(MetodosPagoCtrl.TRANSFERENCIA) == 0? "Transferencia":"Depósito"))
													+" en Recibo #"+v.getId().getNumrec()+":  "
													+recCtrl.getError().toString().split("@")[1]);
											return bHecho = false;
										}
									}//asiento en moneda foránea.
									
									//&& =========== Conservar los datos para el deposito.					
									List<Object[]> lstDeps = new ArrayList<Object[]>();
									if (CodeUtil.getFromSessionMap("rv_listaDepositos") != null)
										lstDeps = (ArrayList<Object[]>) CodeUtil.getFromSessionMap("rv_listaDepositos");
									
									Object oDep[] = new Object[20];
									oDep[0]= iCaid;
									oDep[1]= sCodsuc;
									oDep[2]= sCodcomp;
									oDep[3]= iNoBatch;
									oDep[4]= iNoDocumento;
									oDep[5]= iNoarqueo;
									oDep[6]= sMoneda;
									oDep[7]= v.getId().getMontoneto();
									oDep[8]= dtFecha;
									oDep[9]= HoraArqueo;
									oDep[10]= vaut[0].getId().getLogin();
									oDep[11]= iNoDocumento; //sRefer;
									oDep[12]= "D";
									oDep[14]= v.getId().getMpago();
									oDep[15]= tasaCambioOficial.setScale(4, RoundingMode.HALF_UP);
									
									//&& =========== Crear el objeto que contendra la informacion de los movimientos sobre las cuentas.
									lstCtasxDeps = new ArrayList<Ctaxdeposito>();
									ctaxDeps = new Ctaxdeposito();
									ctaxDeps.setMonto(v.getId().getMontoneto());
									ctaxDeps.setTipomov("C");
									ctaxDeps.setIdcuenta(sIdCtaTranBanco);
									ctaxDeps.setGmmcu(sCtmcu);
									ctaxDeps.setGmobj(sCtobj);
									ctaxDeps.setGmsub(sCtsub);
									lstCtasxDeps.add(ctaxDeps);
									
									//&& ========== Objeto para debito, caja.
									ctaxDeps = new Ctaxdeposito();
									ctaxDeps.setMonto(v.getId().getMontoneto());
									ctaxDeps.setTipomov("D");
									ctaxDeps.setIdcuenta(sIdCuentaCaja);
									ctaxDeps.setGmmcu(sCc1mcu);
									ctaxDeps.setGmobj(sCcobj);
									ctaxDeps.setGmsub(sCcsub);
									lstCtasxDeps.add(ctaxDeps);
									oDep[16] = lstCtasxDeps;
									oDep[17] = iCodigoBanco;
									
									oDep[18] = v.getId().getReferencenumber()  ;
									try {
										oDep[18] = ( v.getId().getReferencenumber()  == 0 ) ? Integer.parseInt( sRefer ): v.getId().getReferencenumber();
									} catch (Exception e) {
										e.printStackTrace();
									}
									
									oDep[19] = v.getId().getDepctatran();
									
									lstDeps.add(oDep);
									CodeUtil.putInSessionMap("rv_listaDepositos", lstDeps);
									
								}//Numero de cuenta de banco
								else{
									CodeUtil.putInSessionMap("sMensajeError","La cuenta para banco "+f22.getId().getBanco()+" no se encuentra configurada para moneda "+sMoneda +" en " +sCodcomp);
									return bHecho = false;
								}
							}//número de batch y de documento.
							else{
								CodeUtil.putInSessionMap("sMensajeError","No se han realizado los depósitos: ocurrió un error al obtener el número de batch o número de documento");
								return bHecho = false;
							}
						}//ciclo de los métodos de pago.
					}//método de pago para Transferencia y depósito en banco.
					
					//--------------------- DEPÓSITOS PARA TARJETAS DE CRÉDITO -----------------------/
					if(iMpago == 3){
						bHecho=true;
						
						String conceptoComis = "";
						String descripLinea1 = "";
						String descripLinea2 = "";
						
						String sCuentaPos[],sD7sbl,sD7sblt;
						String sCtaPos,sIdCtaPos,sCompCtaPos,sCp1mcu,sCpob,sCpsub; 
						int iMontocom = 0,iMontoPos=0,iCajaUso=0;
						
						//&& ===== True para no borrar el codigo que ya existe y no tener que reacomodarlo.
						if(true){
							
							//------- lista de montos por pos
							String sCuentaTemp[] = null; 
							sCuenta = null;
							for(int i=0; i<bdMontoNetoPos.length; i++){
								
								//----  Determinar la cuenta de caja para el afiliado
								if(iCaid == iCaidPos[i]){
									if(sCuentaTemp == null){
										sCuentaTemp = dv.obtenerCuentaCaja(iCaid, sCodcomp, MetodosPagoCtrl.TARJETA, 
																			sMoneda, session, 
																			null, null, null);
									}
									iCajaUso = iCaid;
									sCuenta = sCuentaTemp;
								}else{
									iCajaUso = iCaidPos[i];
									sCuenta = dv.obtenerCuentaCaja(iCajaUso, sCodcomp, MetodosPagoCtrl.TARJETA, 
																	sMoneda,session, 
																	null, null, null);
								}
								
								if(sCuenta == null){
									CodeUtil.putInSessionMap("sMensajeError","No se puede obtener la cuenta de caja para depósitos de Afiliados en caja: "+iCaid);
									return bHecho = false;
								}
								//--- Asignación de valores para los asientos de diario 
								sCuentaCaja = sCuenta[0];
								sIdCuentaCaja = sCuenta[1];
								sCompCuentaCaja = sCuenta[2];
								sCc1mcu = sCuenta[3];
								sCcobj  = sCuenta[4];
								sCcsub  = sCuenta[5];
								
								//obtener la cuenta del afiliado.
								sCuentaPos  = dv.obtenerCuentaPOS(iNoReferPos[i]+"", sMoneda, sUninegPOS[i], session, null);
								if(sCuentaPos!=null){
									sCtaPos 	= sCuentaPos[0];
									sIdCtaPos 	= sCuentaPos[1];
									sCompCtaPos = sCuentaPos[2];
									sCp1mcu 	= sCuentaPos[3];
									sCpob 		= sCuentaPos[4];
									sCpsub 		= sCuentaPos[5];
									sD7sbl		= sCuentaPos[6];
									sD7sblt		= sCuentaPos[7];
									sAsientoSucursal = sCuenta[2];
									bHecho = false;
									
									int numerobatch = Divisas.numeroSiguienteJdeE1(   );
									int numeroDocumento = Divisas.numeroSiguienteJdeE1Custom(valoresJdeNumeracion[8],valoresJdeNumeracion[9] );
									
									iNobatchNodoc = new int[]{numerobatch, numeroDocumento};

									if(iNobatchNodoc != null){
										iNoBatch = iNobatchNodoc[0];
										iNoDocumento = iNobatchNodoc[1];
										
										conceptoComis = "Arqueo "+ iNoarqueo +" Caja " + iCaid + " ComisTc ";
										
										if(sMoneda.equals( monedaBaseCompania.trim() ) ){ 
											
											descripLinea1 = "POS: " + iNoReferPos[i]+ " UN:"+ sCp1mcu  +" C: "+iCajaUso;
											descripLinea2 = "LIQ: " + marcaTarjeta[i] ;

											iMontocom = (int)dv.roundDouble(bdMtoComis[i].multiply(new BigDecimal("100")).doubleValue());
											
											bHecho  = recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sAsientoSucursal, "P9", iNoDocumento, 1.0, iNoBatch, sCtaPos,
													  sIdCtaPos, sCp1mcu, sCpob, sCpsub, "AA", sMoneda, iMontocom, conceptoComis, vaut[0].getId().getLogin(),
													  vaut[0].getId().getCodapp(),BigDecimal.ZERO, sTipoCliente, descripLinea1 ,
													  sCompCtaPos,sD7sbl,sD7sblt, monedaBaseCompania, sCompCtaPos,"D", 0);
											if(bHecho){
												
												bHecho  = recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sAsientoSucursal, "P9", iNoDocumento, 2.0, iNoBatch, sCuentaCaja,
														  sIdCuentaCaja, sCc1mcu, sCcobj, sCcsub, "AA", sMoneda, iMontocom*(-1), conceptoComis, vaut[0].getId().getLogin(),
														  vaut[0].getId().getCodapp(), BigDecimal.ZERO, sTipoCliente, descripLinea2,
														  sCompCuentaCaja,"","", monedaBaseCompania, sCompCuentaCaja,"D", 0);
													
												if(bHecho){
													
													bHecho = recCtrl.registrarBatchA92Session( session, dtFecha,  valoresJdeInsContado[8], iNoBatch, iMontocom, vaut[0].getId().getLogin(), 1 , "APRARQUEO",  valoresJdeInsContado[9]);
													
													if(bHecho){
															
														List<Object[]> lstDeps = new ArrayList<Object[]>();
														if (CodeUtil.getFromSessionMap("rv_listaDepositos") != null)
															lstDeps = (ArrayList<Object[]>) CodeUtil.getFromSessionMap("rv_listaDepositos");
														
														Object oDep[] = new Object[20];
														oDep[0]= iCajaUso;
														oDep[1]= sCodsuc;
														oDep[2]= sCodcomp;
														oDep[3]= iNoBatch;
														oDep[4]= iNoDocumento;
														oDep[5]= iNoarqueo;
														oDep[6]= sMoneda;
														oDep[7]= bdMtoComis[i]; //bdMtoPos[i];
														oDep[8]= dtFecha;
														oDep[9]= HoraArqueo;
														oDep[10]= vaut[0].getId().getLogin();
														oDep[11]= String.valueOf(iNoReferPos[i]);
														oDep[12]= "C";
														oDep[14]= MetodosPagoCtrl.TARJETA;
														oDep[15] = BigDecimal.ZERO;
														
														//&& =========== Crear el objeto que contendra la informacion de los movimientos sobre las cuentas.
														
														lstCtasxDeps = new ArrayList<Ctaxdeposito>();
														ctaxDeps = new Ctaxdeposito();
														ctaxDeps.setMonto(bdMtoComis[i]);
														ctaxDeps.setTipomov("C");
														ctaxDeps.setIdcuenta(sIdCtaPos);
														ctaxDeps.setGmmcu(sCp1mcu);
														ctaxDeps.setGmobj(sCpob);
														ctaxDeps.setGmsub(sCpsub);
														lstCtasxDeps.add(ctaxDeps);
														
														
														//&& ========== Objeto para debito, caja.
														ctaxDeps = new Ctaxdeposito();
														ctaxDeps.setMonto(bdMtoComis[i]);
														ctaxDeps.setTipomov("D");
														ctaxDeps.setIdcuenta(sIdCuentaCaja);
														ctaxDeps.setGmmcu(sCc1mcu);
														ctaxDeps.setGmobj(sCcobj);
														ctaxDeps.setGmsub(sCcsub);
														lstCtasxDeps.add(ctaxDeps);
														oDep[16] = lstCtasxDeps;
														oDep[17] = iNoReferPos[i] ;
														oDep[18] = iNoReferPos[i] ;
														oDep[19] = 0 ;
														
														lstDeps.add(oDep);
														CodeUtil.putInSessionMap("rv_listaDepositos", lstDeps);
															
														if(!bHecho){
															CodeUtil.putInSessionMap("sMensajeError", "Depósito para afiliado "+iNoReferPos[i] + " No registrado, favor intente de nuevo");
															return bHecho = false;															 
														}
													}else{
														CodeUtil.putInSessionMap("sMensajeError", "Depósito para afiliado "+iNoReferPos[i] + " No registrado, favor intente de nuevo");
														return bHecho = false;
													}
												}else{
													CodeUtil.putInSessionMap("sMensajeError", "Depósito para afiliado "+iNoReferPos[i] + " No registrado, favor intente de nuevo");
													return bHecho = false;
												}
											}else{
												CodeUtil.putInSessionMap("sMensajeError", "Depósito para afiliado "+iNoReferPos[i] + " No registrado, favor intente de nuevo");
												return bHecho = false;
											}
										}//Moneda Córdobas.
										else{
										
											iMonto  = (int)(dv.roundDouble((bdMontoNetoPos[i]*100)));
											int iMontodom = (int)(dv.roundDouble((bdMontoNetoPos[i] * tasaCambioOficial.doubleValue() )*100));
											
											iMontocom = (int)dv.roundDouble(bdMtoComis[i].multiply(new BigDecimal("100")).doubleValue());
											int iMtoCoDo =(int)dv.roundDouble(bdMtoComis[i].multiply(tasaCambioOficial).multiply(new BigDecimal("100")).doubleValue());
											
											descripLinea1 = "POS: " + iNoReferPos[i]+ " UN:"+ sCp1mcu  +" C: "+iCajaUso;
											descripLinea2 = "Liq: " + marcaTarjeta[i] ;
											
											
											bHecho  = recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sAsientoSucursal, "P9", iNoDocumento, 1.0, iNoBatch, sCtaPos,
													  sIdCtaPos, sCp1mcu, sCpob, sCpsub, "AA", sMoneda, iMtoCoDo, conceptoComis, vaut[0].getId().getLogin(),
													  vaut[0].getId().getCodapp(), tasaCambioOficial , sTipoCliente, descripLinea1,
													  sCompCtaPos,sD7sbl,sD7sblt, monedaBaseCompania, sCompCtaPos,"F", iMontocom);
											if(bHecho){
												bHecho  = recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sAsientoSucursal, "P9", iNoDocumento, 1.0, iNoBatch, sCtaPos,
														  sIdCtaPos, sCp1mcu, sCpob, sCpsub, "CA", sMoneda, iMontocom, conceptoComis, vaut[0].getId().getLogin(),
														  vaut[0].getId().getCodapp(), tasaCambioOficial, sTipoCliente, descripLinea1, 
														  sCompCtaPos,sD7sbl,sD7sblt, monedaBaseCompania, sCompCtaPos,"F", 0);
												if(bHecho){
													bHecho = recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sAsientoSucursal, "P9", iNoDocumento, 2.0, iNoBatch, sCuentaCaja,
															 sIdCuentaCaja, sCc1mcu, sCcobj, sCcsub, "AA", sMoneda, iMtoCoDo*(-1), conceptoComis, vaut[0].getId().getLogin(), 
															 vaut[0].getId().getCodapp(),tasaCambioOficial, sTipoCliente, descripLinea2 ,sCompCuentaCaja, 
															 "", "", monedaBaseCompania, sCompCuentaCaja,"F", (iMontocom*(-1)) );
													if(bHecho){
														bHecho = recCtrl.registrarAsientoDiarioLogsSession(session, logs, dtFecha, sAsientoSucursal, "P9", iNoDocumento, 2.0, iNoBatch, sCuentaCaja,
																 sIdCuentaCaja, sCc1mcu, sCcobj, sCcsub, "CA", sMoneda, iMontocom*(-1), conceptoComis, vaut[0].getId().getLogin(), 
																 vaut[0].getId().getCodapp(), tasaCambioOficial, sTipoCliente, descripLinea2,
																 sCompCuentaCaja, "", "", monedaBaseCompania, sCompCuentaCaja,"F", 0);
														if(bHecho){
															
															bHecho = recCtrl.registrarBatchA92Session( session, dtFecha,  valoresJdeInsContado[8], iNoBatch, iMontocom, vaut[0].getId().getLogin(), 1 , "APRARQUEO" ,  valoresJdeInsContado[9]);
															
															if(bHecho){
																
																List<Object[]> lstDeps = new ArrayList<Object[]>();
																if (CodeUtil.getFromSessionMap("rv_listaDepositos") != null)
																	lstDeps = (ArrayList<Object[]>) CodeUtil.getFromSessionMap("rv_listaDepositos");
																
																Object oDep[] = new Object[20];
																oDep[0]= iCajaUso;
																oDep[1]= sCodsuc;
																oDep[2]= sCodcomp;
																oDep[3]= iNoBatch;
																oDep[4]= iNoDocumento;
																oDep[5]= iNoarqueo;
																oDep[6]= sMoneda;
																oDep[7]= bdMtoComis[i];
																oDep[8]= dtFecha;
																oDep[9]= HoraArqueo;
																oDep[10]= vaut[0].getId().getLogin();
																oDep[11]= String.valueOf(iNoReferPos[i]);
																oDep[12]= "C";
																oDep[14]= MetodosPagoCtrl.TARJETA;
																oDep[15] = tasaCambioOficial;
																
																//&& =========== Crear el objeto que contendra la informacion de los movimientos sobre las cuentas.
																lstCtasxDeps = new ArrayList<Ctaxdeposito>();
																ctaxDeps = new Ctaxdeposito();
																ctaxDeps.setMonto(bdMtoComis[i]);
																ctaxDeps.setTipomov("C");
																ctaxDeps.setIdcuenta(sIdCtaPos);
																ctaxDeps.setGmmcu(sCp1mcu);
																ctaxDeps.setGmobj(sCpob);
																ctaxDeps.setGmsub(sCpsub);
																lstCtasxDeps.add(ctaxDeps);
																
																//&& ========== Objeto para debito, caja.
																ctaxDeps = new Ctaxdeposito();
																ctaxDeps.setMonto(bdMtoComis[i]);
																ctaxDeps.setTipomov("D");
																ctaxDeps.setIdcuenta(sIdCuentaCaja);
																ctaxDeps.setGmmcu(sCc1mcu);
																ctaxDeps.setGmobj(sCcobj);
																ctaxDeps.setGmsub(sCcsub);
																lstCtasxDeps.add(ctaxDeps);
																oDep[16] = lstCtasxDeps;
																oDep[17] = iNoReferPos[i] ;
																oDep[18] = iNoReferPos[i] ;
																oDep[19] = 0 ;
																
																lstDeps.add(oDep);	
																CodeUtil.putInSessionMap("rv_listaDepositos", lstDeps);
																
																if(!bHecho){
																	CodeUtil.putInSessionMap("sMensajeError", "Depósito para afiliado "+iNoReferPos[i] + " No registrado, favor intente de nuevo");
																	return bHecho = false;
																}
															}
															else{
																CodeUtil.putInSessionMap("sMensajeError", "Depósito para afiliado "+iNoReferPos[i] + " No registrado, favor intente de nuevo");
																return bHecho = false;
															}
														}else{
															CodeUtil.putInSessionMap("sMensajeError", "Depósito para afiliado "+iNoReferPos[i] + " No registrado, favor intente de nuevo");
															return bHecho = false;
														}
													}else{
														CodeUtil.putInSessionMap("sMensajeError", "Depósito para afiliado "+iNoReferPos[i] + " No registrado, favor intente de nuevo");
														return bHecho = false;
													}
												}else{
													CodeUtil.putInSessionMap("sMensajeError", "Depósito para afiliado "+iNoReferPos[i] + " No registrado, favor intente de nuevo");
													return bHecho = false;
												}
											}else{
												CodeUtil.putInSessionMap("sMensajeError", "Depósito para afiliado "+iNoReferPos[i] + " No registrado, favor intente de nuevo");
												return bHecho = false;
											}
										}//moneda foránea.
									}else{
										CodeUtil.putInSessionMap("sMensajeError", "Depósito para afiliado "+iNoReferPos[i] + " No registrado, favor intente de nuevo");
										return bHecho = false;
									}
								}else{
									CodeUtil.putInSessionMap("sMensajeError","No se encuentra configurada la cuenta para el afiliado "+iNoReferPos[i]+" en "+sMoneda+ ", Unidad de Neg: "+sUninegPOS[i]);
									return bHecho = false;
								}
							}//cierre del for de montos pos
						}else{
							CodeUtil.putInSessionMap("sMensajeError","No se puede obtener la cuenta de banco para depósitos de Afiliados de caja: "+iCajaUso);
							return bHecho = false;
						}
					}//método de pago H, depósitos con tarjetos de crédito.
					
			} catch (Exception error) {
				CodeUtil.putInSessionMap("sMensajeError", "Traslado de depósitos no procesado,  Favor intente nuevamente ");
//				LogCrtl.imprimirError(error);
				return bHecho = false;
			}
			return bHecho;
		}
/**************** II. Cerrar las ventanas ********************************/
	public void cerrarAgregarReferPos(ActionEvent ev){
		dwCargarReferenciasPOS.setWindowState("hidden");
		CodeUtil.removeFromSessionMap( "rva_pasoAprobarArqueo");
	}
	public void cerrarDetalleArqueo(ActionEvent ev){
		CodeUtil.removeFromSessionMap( "rv_DAC");
		CodeUtil.removeFromSessionMap( "rva_lstResumenAfiliado");
		CodeUtil.removeFromSessionMap( "rva_pasoAprobarArqueo");
		rv_dwCancelarAprArqueo.setWindowState("hidden");
		dwDetalleArqueo.setWindowState("hidden");
	}
	public void cerrarDetFacturas(ActionEvent ev){
		try{
			CodeUtil.removeFromSessionMap( "rv_lblEtCantFacC0");
			CodeUtil.removeFromSessionMap( "rv_lblEtCantFacCr");
			CodeUtil.removeFromSessionMap( "rv_lblEtTotalFacCo");
			CodeUtil.removeFromSessionMap( "rv_blEtTotalFacCr");
			CodeUtil.removeFromSessionMap( "rv_lblCantFacCO");
			CodeUtil.removeFromSessionMap( "rv_lblCantFacCr");
			CodeUtil.removeFromSessionMap( "rv_lblTotalFacCo");
			CodeUtil.removeFromSessionMap( "rv_lblTotalFacCr");
			rv_dwFacturas.setWindowState("hidden");
		}catch(Exception error){
			error.printStackTrace();
		}
	}
	public void cerrarRecxTipoMetPago(ActionEvent ev){		
		rv_dwReciboxtipoMetPago.setWindowState("hidden");
	}
	public void cerrarDetRecibos(ActionEvent ev){
		dwRecibosxTipoIngreso.setWindowState("hidden");
	}
	public void cerrarDetalleFacDiario(ActionEvent ev){
		rv_dwDetalleFactura.setWindowState("hidden");
	}
	public void cerrarDetalleRecibo(ActionEvent ev){
		rv_dwDetalleRecibo.setWindowState("hidden");
	}
	public void cerrarValidarAprobacion(ActionEvent ev){
		dwValidarAprobacionArqueo.setWindowState("hidden");
	}
	public void cerrarConfirmarCancelarAprobAr(ActionEvent ev){
		rv_dwCancelarAprArqueo.setWindowState("hidden");
	}
	public void cerrarConfirmAprobacion(ActionEvent ev){
		rv_dwConfirmarProcesarArq.setWindowState("hidden");
	}
	public void cerrarRechazoArqueo(ActionEvent ev){
		rv_dwRechazarArqueoCaja.setWindowState("hidden");
	}
	public void cerrarDetRecPagMonEx(ActionEvent ev){
		dwDetallerecpagmonEx.setWindowState("hidden");
	}
	public void cerrarVentana(ActionEvent ev){
		CodeUtil.removeFromSessionMap( "rv_EgresoSolicitado");
		dwEgresosxMetPago.setWindowState("hidden");
	}
	public void cerrarDetOtrosEgresos(ActionEvent ev){		
		dwDetOtrosEgresos.setWindowState("hidden");
	}
	public void cerrarDetalleCambios(ActionEvent ev){		
		dwDetalleCambios.setWindowState("hidden");
	}
	public void cerrarDetOtrosIng(ActionEvent ev){		
		dwDetalleOtrosIngresos.setWindowState("hidden");
	}
	public void cerrarDetalleSalida(ActionEvent ev){
		dwDetalleSalidas.setWindowState("hidden");
	}
	
/***** 16. Mostrar la ventana para rechazar el arqueo de caja. ****************************/
	public void mostrarRechazarArqueo(ActionEvent ev){		
		try{
			Varqueo varqueo  = (Varqueo)CodeUtil.getFromSessionMap( "rva_VARQUEO");
			if(varqueo.getId().getEstado().compareTo("P") != 0  ){
				dwValidarAprobacionArqueo.setWindowState("normal");
				lblValidarAprobacion.setValue("Solo puede anularse arqueos en estado pendiente");
				return;
			}
			rv_dwRechazarArqueoCaja.setWindowState("normal");
			txtMotivoRechazoArqueo.setStyle("width: 250px; height: 60px");
			txtMotivoRechazoArqueo.setValue("");			
			lblMsgMotivoRechazo.setValue("");
			
		}catch(Exception error){
			dwValidarAprobacionArqueo.setWindowState("normal");
			lblValidarAprobacion.setValue("Error de sistema, No ha podido anularse el arqueo de caja :( !");
			error.printStackTrace();
		}
	}		
/****** 18. Rechazar el arqueo de caja. ************************************************/
	public void rechazarArqueoCaja(ActionEvent ev){
		String sMotivo = "";
		ArqueoCajaCtrl ac;
		boolean bHecho = true;
		ReciboCtrl recCtrl = new ReciboCtrl();
		String sEstado = "", sEstadoDet = "";
	
		Session session = null;
		Transaction transaction = null;
 
		Reintegro r = null;
		String sMensajeError = "Error de proceso";
		
		boolean conexionDonaciones = false;
		Object[] dtaCnDnc = null;
		
		try{
			LogCajaService.CreateLog("rechazarArqueoCaja", "INFO", "rechazarArqueoCaja-INICIO");
			
			//&& ====== Validaciones.
			txtMotivoRechazoArqueo.setStyle("border-color: black");
			sMotivo = txtMotivoRechazoArqueo.getValue().toString().trim();
			if(sMotivo.compareTo("") == 0  ){
				txtMotivoRechazoArqueo.setStyle("width: 250px; height: 60px; border-color: light-red");
				lblMsgMotivoRechazo.setValue("Debe ingresar el Motivo");
				return;
			}
			if(sMotivo.length() < 20){
				txtMotivoRechazoArqueo.setStyle("width: 250px; height: 60px; border-color: light-red");
				lblMsgMotivoRechazo.setValue("Ingrese al menos 20 caracteres");
				return;
			}
			rv_dwRechazarArqueoCaja.setWindowState("hidden");
			
			//&& ====== Conexiones.
			session = HibernateUtilPruebaCn.currentSession();
			transaction  = session.beginTransaction();
		 
			Varqueo va  = (Varqueo)CodeUtil.getFromSessionMap( "rva_VARQUEO");
			String tipoBatch = CodigosJDE1.BATCH_CONTADO.posicion() ;
			
			//&& ===== Anular el arqueo
			bHecho = new ArqueoCajaCtrl().rechazarArqueo(session, transaction, va.getId().getNoarqueo(), 
								va.getId().getCaid(), va.getId().getCodcomp(), 
								va.getId().getCodsuc(), sMotivo);
			
			if(!bHecho)	throw new Exception("@Error al actualizar estado del arqueo");
			
			//&& ====== Anulacion del reintegro (si existe)
			r = recCtrl.verificarSiTieneReintegro(va.getId().getNoarqueo(),  
										va.getId().getCaid(), va.getId().getCodcomp(), 
										va.getId().getCodsuc(), va.getId().getMoneda());	
			if(r != null){
				 
				sEstado = recCtrl.leerEstadoBatch( r.getNobatch(), tipoBatch );
				
				if(sEstado != null && sEstado.trim().compareToIgnoreCase("D") == 0	){

					sEstadoDet = recCtrl.leerEstadoDetalleAsiento(r.getNobatch());
					if(sEstadoDet.trim().equals("P") || sEstadoDet.trim().equals("D"))
						sEstado = "D";
				}

				//====== hacer asientos a la inversa para anular el original
				if(sEstado!= null && sEstado.compareTo("D") == 0){
					
					sMensajeError = revertirAjusteMinimo(session, transaction, r);
					
					if(!sMensajeError.equals(""))
						throw new Exception("@Error al revertir asiento de " +
										"diario por reintegro de fondo minimo");
				}else{
					//======= borrar el registro del batch hecho
					bHecho = recCtrl.borrarBatch( session, r.getNobatch(), tipoBatch );
					if(!bHecho)
						throw new Exception("@Error al borrar asiento de diario "+r.getNobatch()+" para reintegro ");
					
					bHecho = recCtrl.borrarAsientodeDiario( session, r.getNodoc(), r.getNobatch());
					if(!bHecho)
						throw new Exception("@Error al borrar asiento de diario "+r.getNobatch()+" para reintegro ");
				}
				
				//&& ===== Actualizar el estado del reintegro.
				r.setEstado(true);
				bHecho = recCtrl.editarReintegro(session, r);
				if(!bHecho)
					throw new Exception("@Error al actualizar el estado del reintegro asociado al arqueo");
				
				//&& ===== Restablecer el fondo minimo afectado por el reintegro.
				CtrlCajas cc = new CtrlCajas();		
				
				String sMminActual = new ArqueoCajaCtrl()
											.obtenerMontoMinimodeCaja(va.getId().getCaid(),
														  va.getId().getCodcomp(), 
														  va.getId().getMoneda());
				if(sMminActual == null)
					throw new Exception("@Error al obtener el monto minimo actual");
				if(sMminActual.trim().compareTo("") == 0 )
					sMminActual = "0";
				
				double dMminActual = Double.parseDouble(sMminActual);
				dMminActual = dMminActual / 100;
				dMminActual += r.getMonto().doubleValue();
				dMminActual = new Divisas().roundDouble(dMminActual);
				
				bHecho = cc.actualizarMontoMinimo(va.getId().getCaid(), 
												va.getId().getCodcomp(), 
												va.getId().getMoneda(),
												dMminActual );
				if(!bHecho)
					throw new Exception("@Error al actualizar el monto minimo de la caja");
				
			}
			
			//&& =================== Cambiar al estado de pendiente las transacciones por donaciones 
			
			if(bHecho && CodeUtil.getFromSessionMap( "rv_CierreDonaciones" ) != null ){
				
				Vautoriz vaut = ((Vautoriz[])CodeUtil.getFromSessionMap( "sevAut"))[0];
				
				List<DncCierreDonacion> cierreDonacion = (ArrayList<DncCierreDonacion>)CodeUtil.getFromSessionMap( "rv_CierreDonaciones" );
				List<Integer>idsCierresDnc = (ArrayList<Integer>)CodeUtil.selectPropertyListFromEntity(cierreDonacion, "idcierredonacion", false);
				String idsIn = idsCierresDnc.toString().replace("[", "(").replace("]", ")");
			
				List<String>lstStrlSqls = new ArrayList<String>();
				
				String strSqlUpdate = "update " + PropertiesSystem.ESQUEMA
						+ ".dnc_cierre_donacion set usrmod = "+vaut.getId().getCodreg() +", "
						+ " estado =  0, fechamod = current_timestamp, observaciones = '"+sMotivo+"' "
						+ " where idcierredonacion in " + idsIn;
						
				lstStrlSqls.add(strSqlUpdate);
				
				strSqlUpdate =  " update "+ PropertiesSystem.ESQUEMA + ".dnc_donacion " +
					" set estado = 1, fechamod = current_timestamp where iddonacion in " +
					"( SELECT iddonacion FROM "+ PropertiesSystem.ESQUEMA +
						".DNC_CIERRE_DETALLE WHERE idcierrednc in "+ idsIn + " ) " ; 
						
				lstStrlSqls.add(strSqlUpdate);
				
				bHecho = ConsolidadoDepositosBcoCtrl.executeSqlQueries( lstStrlSqls ) ;
				
			}
			
			try {
				
				if(bHecho){
					transaction.commit();
				}else{
					transaction.rollback();
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				bHecho = false;
			}
			
			if(bHecho){

				dwDetalleArqueo.setWindowState("hidden");
				refrescarArqueos();
				CodeUtil.removeFromSessionMap( "rv_DAC");
				CodeUtil.removeFromSessionMap( "rva_VARQUEO");
				sMotivo = "Se realizó correctamente el proceso de anulación";
				
			}else{				
				sMotivo = "No se ha podido anular el arqueo";
			}
			
			dwValidarAprobacionArqueo.setWindowState("normal");
			lblValidarAprobacion.setValue(sMotivo);
			
		}catch(Exception error){
			LogCajaService.CreateLog("rechazarArqueoCaja", "ERR", error.getMessage());
			error.printStackTrace();
			sMensajeError = "No ha podido anularse el arqueo de caja ";
			dwValidarAprobacionArqueo.setWindowState("normal");
			lblValidarAprobacion.setValue(sMensajeError);
			
			try {
				if (transaction != null) {
					transaction.rollback();
				}
			} catch (Exception e) {
				LogCajaService.CreateLog("rechazarArqueoCaja", "ERR", e.getMessage());
			}
			
		}finally{
			HibernateUtilPruebaCn.closeSession(session) ;
			LogCajaService.CreateLog("rechazarArqueoCaja", "INFO", "rechazarArqueoCaja-FIN");
			
		}
	}	
/********************** I. GETTERS Y SETTERS ****************************/	
	public HtmlGridView getGvArqueosPendRev() {
		return gvArqueosPendRev;
	}
	public void setGvArqueosPendRev(HtmlGridView gvArqueosPendRev) {
		this.gvArqueosPendRev = gvArqueosPendRev;
	}
	public List getLstArqueosPendRev() {		
		try{			
			if(CodeUtil.getFromSessionMap( "lstArqueosPendRev")==null)
				lstArqueosPendRev = new ArrayList();
			else
				lstArqueosPendRev = (ArrayList)CodeUtil.getFromSessionMap( "lstArqueosPendRev");
			
		}catch(Exception error){
			error.printStackTrace();
		}
		return lstArqueosPendRev;
	}
	public void setLstArqueosPendRev(List lstArqueosPendRev) {
		this.lstArqueosPendRev = lstArqueosPendRev;
	}
	public DropDownList getDdlFiltroCompania() {
		return ddlFiltroCompania;
	}
	public void setDdlFiltroCompania(DropDownList ddlFiltroCompania) {
		this.ddlFiltroCompania = ddlFiltroCompania;
	}
	public DropDownList getDdlFiltroMoneda() {
		return ddlFiltroMoneda;
	}
	public void setDdlFiltroMoneda(DropDownList ddlFiltroMoneda) {
		this.ddlFiltroMoneda = ddlFiltroMoneda;
	}
	//------------ lista de compañías a cargo del contador.
	public List getLstFiltroCompania() {
		
		try {
			
			if(CodeUtil.getFromSessionMap( "rva_lstFiltroCompania") != null){
				return lstFiltroCompania = (ArrayList<SelectItem>)CodeUtil.getFromSessionMap( "rva_lstFiltroCompania");
			}
			
			RevisionArqueoCtrl rvaCtrl = new RevisionArqueoCtrl();
			List lstcaja = (ArrayList)CodeUtil.getFromSessionMap( "lstCajas");
			Vf55ca01 caja = (Vf55ca01)lstcaja.get(0);
					
			lstFiltroCompania = new ArrayList<SelectItem>();
			lstFiltroCompania.add(new SelectItem("COMP","Todas","Selección de compañía"));
			
			Vautoriz[] vAut = (Vautoriz[])CodeUtil.getFromSessionMap( "sevAut");
			
			if (vAut[0].getId().getCodper().compareTo(cajaparm.getParametros("34", "0", "ENS_CONCILI_PRIN").getValorAlfanumerico().toString())  == 0 || 
				vAut[0].getId().getCodper().compareTo(cajaparm.getParametros("34", "0", "ENS_CONCIL_SUPER").getValorAlfanumerico().toString()) == 0 ){
				
				String sqlOrCtaConc = ConfirmaDepositosCtrl.constructSqlOrCtaxCon( Arrays.asList(new String[][]{ {"drky","2"} }) );
				
				String strSql = "select * from ENS.Vcompania " ;
				
				if( !sqlOrCtaConc.isEmpty() ){
					strSql += " where "+ sqlOrCtaConc;
				}		 
				
				List<Vcompania> lstCompanias = (ArrayList<Vcompania>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSql, true, Vcompania.class);
				for (Vcompania vc : lstCompanias) {
					lstFiltroCompania.add(new SelectItem(vc.getId().getDrky().trim(), 
								vc.getId().getDrky().trim() +" " + vc.getId().getDrdl01().trim(), 
								vc.getId().getDrky().trim() +" " + vc.getId().getDrdl01().trim() ) ) ;
				}
				
			}else{
				
				List lstComp = rvaCtrl.cargarCompaniaxContador(caja.getId().getCacont());
				if(lstComp!=null && lstComp.size()>0){
					for(int i=0; i<lstComp.size();i++){
						Vcompaniaxcontador vcc = (Vcompaniaxcontador)lstComp.get(i);
						lstFiltroCompania.add(new SelectItem(vcc.getId().getCodcomp(),vcc.getId().getCompania(),"Compañía: "+vcc.getId().getCompania()));
					}				
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
		}finally{
			
			if(lstFiltroCompania == null)
				lstFiltroCompania = new ArrayList<SelectItem>();
			
			CodeUtil.putInSessionMap("rva_lstFiltroCompania",lstFiltroCompania);
			ddlFiltroCompania.dataBind();
		}
		
		return lstFiltroCompania;
	}
	public void setLstFiltroCompania(List lstFiltroCompania) {
		this.lstFiltroCompania = lstFiltroCompania;
	}
	//------------- lista de monedas por contador.
	public List getLstFiltroMoneda() {		
		if(CodeUtil.getFromSessionMap( "rva_lstFiltroMoneda")==null){
			
			List lstcaja = (ArrayList)CodeUtil.getFromSessionMap( "lstCajas");
			Vf55ca01 caja = (Vf55ca01)lstcaja.get(0);			
							
			List lstMonedas = new ArrayList();			
			lstMonedas.add(new SelectItem("MON","Todas","Selección de monedas"));
			
			RevisionArqueoCtrl rvCtrl = new RevisionArqueoCtrl();
			List lstMon = rvCtrl.cargarMonedasxContador(caja.getId().getCacont());
			if(lstMon!=null && lstMon.size()>0){
				for(int i=0; i<lstMon.size();i++){
					Vmonedasxcontador vmc = (Vmonedasxcontador)lstMon.get(i);
					lstMonedas.add(new SelectItem(vmc.getId().getMoneda(),vmc.getId().getMoneda(),"Moneda: "+vmc.getId().getMoneda()));
				}
			}			
			lstFiltroMoneda = lstMonedas;
			CodeUtil.putInSessionMap("rva_lstFiltroMoneda", lstMonedas);
			this.ddlFiltroMoneda.dataBind();
		}		
		else
			lstFiltroMoneda = (ArrayList)CodeUtil.getFromSessionMap( "rva_lstFiltroMoneda");		
		return lstFiltroMoneda;
	}
	public void setLstFiltroMoneda(List lstFiltroMoneda) {
		this.lstFiltroMoneda = lstFiltroMoneda;
	}
	public HtmlOutputText getLblMensaje() {		
		return lblMensaje;
	}
	public void setLblMensaje(HtmlOutputText lblMensaje) {
		this.lblMensaje = lblMensaje;
	}
	public String getMsgArqueos() {
		if(CodeUtil.getFromSessionMap( "rva_msgArqueos")!=null)
			msgArqueos = CodeUtil.getFromSessionMap( "rva_msgArqueos").toString();
		else
			msgArqueos = "";
		return msgArqueos;
	}
	public void setMsgArqueos(String msgArqueos) {
		this.msgArqueos = msgArqueos;
	}
	// ------- ventana para el detalle del arqueo procesado ----------//
	public HtmlDialogWindow getDwDetalleArqueo() {
		return dwDetalleArqueo;
	}
	public void setDwDetalleArqueo(HtmlDialogWindow dwDetalleArqueo) {
		this.dwDetalleArqueo = dwDetalleArqueo;
	}
	public HtmlDialogWindowHeader getHdDetArqueo() {
		return hdDetArqueo;
	}
	public void setHdDetArqueo(HtmlDialogWindowHeader hdDetArqueo) {
		this.hdDetArqueo = hdDetArqueo;
	}
	public HtmlOutputText getLblcajero() {
		return lblcajero;
	}
	public void setLblcajero(HtmlOutputText lblcajero) {
		this.lblcajero = lblcajero;
	}
	public HtmlOutputText getLblCompania() {
		return lblCompania;
	}
	public void setLblCompania(HtmlOutputText lblCompania) {
		this.lblCompania = lblCompania;
	}
	public HtmlOutputText getLblMoneda() {
		return lblMoneda;
	}
	public void setLblMoneda(HtmlOutputText lblMoneda) {
		this.lblMoneda = lblMoneda;
	}
	public HtmlOutputText getLblnocaja() {
		return lblnocaja;
	}
	public void setLblnocaja(HtmlOutputText lblnocaja) {
		this.lblnocaja = lblnocaja;
	}
	public HtmlOutputText getLblsucursal() {
		return lblsucursal;
	}
	public HtmlOutputText getLblnoarqueo() {
		return lblnoarqueo;
	}
	public void setLblnoarqueo(HtmlOutputText lblnoarqueo) {
		this.lblnoarqueo = lblnoarqueo;
	}
	public void setLblsucursal(HtmlOutputText lblsucursal) {
		this.lblsucursal = lblsucursal;
	}	
	// ---------- objetos de detalles de ingresos y egresos. --------------//
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
	public HtmlOutputText getLblTotaIngresos() {
		return lblTotaIngresos;
	}
	public void setLblTotaIngresos(HtmlOutputText lblTotaIngresos) {
		this.lblTotaIngresos = lblTotaIngresos;
	}
	public HtmlOutputText getLblTotalAbonoPagBanco() {
		return lblTotalAbonoPagBanco;
	}
	public void setLblTotalAbonoPagBanco(HtmlOutputText lblTotalAbonoPagBanco) {
		this.lblTotalAbonoPagBanco = lblTotalAbonoPagBanco;
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
	public HtmlOutputText getLblTotalEgresos() {
		return lblTotalEgresos;
	}
	public void setLblTotalEgresos(HtmlOutputText lblTotalEgresos) {
		this.lblTotalEgresos = lblTotalEgresos;
	}
	public HtmlOutputText getLblTotalOtrosEgresos() {
		return lblTotalOtrosEgresos;
	}
	public void setLblTotalOtrosEgresos(HtmlOutputText lblTotalOtrosEgresos) {
		this.lblTotalOtrosEgresos = lblTotalOtrosEgresos;
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
	public HtmlOutputText getLblTotalPrimasPagBanco() {
		return lblTotalPrimasPagBanco;
	}
	public void setLblTotalPrimasPagBanco(HtmlOutputText lblTotalPrimasPagBanco) {
		this.lblTotalPrimasPagBanco = lblTotalPrimasPagBanco;
	}
	public HtmlOutputText getLblTotalVentasNetas() {
		return lblTotalVentasNetas;
	}
	public void setLblTotalVentasNetas(HtmlOutputText lblTotalVentasNetas) {
		this.lblTotalVentasNetas = lblTotalVentasNetas;
	}
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
	public HtmlOutputText getLblVentasTotales() {
		return lblVentasTotales;
	}
	public void setLblVentasTotales(HtmlOutputText lblVentasTotales) {
		this.lblVentasTotales = lblVentasTotales;
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
	//--- objetos del depóstio final y ayuda de método de pago. --- //
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
	public HtmlOutputText getLblACDetfp_Total() {
		return lblACDetfp_Total;
	}
	public void setLblACDetfp_Total(HtmlOutputText lblACDetfp_Total) {
		this.lblACDetfp_Total = lblACDetfp_Total;
	}
	public HtmlOutputText getLblACDetfp_TransBanco() {
		return lblACDetfp_TransBanco;
	}
	public void setLblACDetfp_TransBanco(HtmlOutputText lblACDetfp_TransBanco) {
		this.lblACDetfp_TransBanco = lblACDetfp_TransBanco;
	}
	public HtmlOutputText getLblbCDC_efectivoenCaja() {
		return lblbCDC_efectivoenCaja;
	}
	public void setLblbCDC_efectivoenCaja(HtmlOutputText lblbCDC_efectivoenCaja) {
		this.lblbCDC_efectivoenCaja = lblbCDC_efectivoenCaja;
	}
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
	
	public HtmlDialogWindowHeader getHdFactura() {
		return hdFactura;
	}
	public void setHdFactura(HtmlDialogWindowHeader hdFactura) {
		this.hdFactura = hdFactura;
	}
	public HtmlDialogWindow getRv_dwFacturas() {
		return rv_dwFacturas;
	}
	public void setRv_dwFacturas(HtmlDialogWindow rv_dwFacturas) {
		this.rv_dwFacturas = rv_dwFacturas;
	}
	public HtmlGridView getRv_gvFacturaRegistradas() {
		return rv_gvFacturaRegistradas;
	}
	public void setRv_gvFacturaRegistradas(HtmlGridView rv_gvFacturaRegistradas) {
		this.rv_gvFacturaRegistradas = rv_gvFacturaRegistradas;
	}
	public String getRv_lblCantFacCO() {
		if(CodeUtil.getFromSessionMap( "rv_lblCantFacCO")==null)
			rv_lblCantFacCO = "";
		else
			rv_lblCantFacCO = CodeUtil.getFromSessionMap( "rv_lblCantFacCO").toString();
		return rv_lblCantFacCO;
	}
	public void setRv_lblCantFacCO(String rv_lblCantFacCO) {	
		this.rv_lblCantFacCO = rv_lblCantFacCO;
	}
	public String getRv_lblCantFacCr() {
		if(CodeUtil.getFromSessionMap( "rv_lblCantFacCr")==null)
			rv_lblCantFacCr = "";
		else
			rv_lblCantFacCr = CodeUtil.getFromSessionMap( "rv_lblCantFacCr").toString();
		return rv_lblCantFacCr;
	}
	public void setRv_lblCantFacCr(String rv_lblCantFacCr) {
		this.rv_lblCantFacCr = rv_lblCantFacCr;
	}
	public String getRv_lblEtCantFacC0() {
		if(CodeUtil.getFromSessionMap( "rv_lblEtCantFacC0")== null)
			rv_lblEtCantFacC0 = "";
		else
			rv_lblEtCantFacC0 = CodeUtil.getFromSessionMap( "rv_lblEtCantFacC0").toString();
		return rv_lblEtCantFacC0;
	}
	public void setRv_lblEtCantFacC0(String rv_lblEtCantFacC0) {
		this.rv_lblEtCantFacC0 = rv_lblEtCantFacC0;
	}
	public String getRv_lblEtCantFacCr() {
		if(CodeUtil.getFromSessionMap( "rv_lblEtCantFacCr")==null)
			rv_lblEtCantFacCr = "";
		else
			rv_lblEtCantFacCr = CodeUtil.getFromSessionMap( "rv_lblEtCantFacCr").toString();
		return rv_lblEtCantFacCr;
	}
	public void setRv_lblEtCantFacCr(String rv_lblEtCantFacCr) {		
		this.rv_lblEtCantFacCr = rv_lblEtCantFacCr;
	}
	public String getRv_lblEtTotalFacCo() {
		if(CodeUtil.getFromSessionMap( "rv_lblEtTotalFacCo")==null)
			rv_lblEtTotalFacCo = "";
		else
			rv_lblEtTotalFacCo = CodeUtil.getFromSessionMap( "rv_lblEtTotalFacCo").toString();
		return rv_lblEtTotalFacCo;
	}
	public void setRv_lblEtTotalFacCo(String rv_lblEtTotalFacCo) {
		this.rv_lblEtTotalFacCo = rv_lblEtTotalFacCo;
	}
	public String getRv_lblTotalFacCo() {
		if(CodeUtil.getFromSessionMap( "rv_lblTotalFacCo")==null)
			rv_lblTotalFacCo = "";
		else
			rv_lblTotalFacCo = CodeUtil.getFromSessionMap( "rv_lblTotalFacCo").toString();
		return rv_lblTotalFacCo;
	}
	public void setRv_lblTotalFacCo(String rv_lblTotalFacCo) {
		this.rv_lblTotalFacCo = rv_lblTotalFacCo;
	}
	public String getRv_lblTotalFacCr() {
		if(CodeUtil.getFromSessionMap( "rv_lblTotalFacCr") == null)
			rv_lblTotalFacCr = "";
		else
			rv_lblTotalFacCr = CodeUtil.getFromSessionMap( "rv_lblTotalFacCr").toString();				
		return rv_lblTotalFacCr;
	}
	public void setRv_lblTotalFacCr(String rv_lblTotalFacCr) {
		this.rv_lblTotalFacCr = rv_lblTotalFacCr;
	}
	public String getRv_blEtTotalFacCr() {
		if(CodeUtil.getFromSessionMap( "rv_blEtTotalFacCr")==null)
			rv_blEtTotalFacCr = "";
		else
			rv_blEtTotalFacCr = CodeUtil.getFromSessionMap( "rv_blEtTotalFacCr").toString();
		return rv_blEtTotalFacCr;
	}	
	public void setRv_blEtTotalFacCr(String rv_blEtTotalFacCr) {
		this.rv_blEtTotalFacCr = rv_blEtTotalFacCr;
	}	
	public List getRv_lstFacturasRegistradas() {
		if(CodeUtil.getFromSessionMap( "rv_lstFacturasRegistradas")==null)
			rv_lstFacturasRegistradas = new ArrayList();
		else
			rv_lstFacturasRegistradas = (ArrayList)CodeUtil.getFromSessionMap( "rv_lstFacturasRegistradas");
		
		return rv_lstFacturasRegistradas;
	}
	public void setRv_lstFacturasRegistradas(List rv_lstFacturasRegistradas) {
		this.rv_lstFacturasRegistradas = rv_lstFacturasRegistradas;
	}
	public HtmlDialogWindowHeader getHdDetTrecxMetPago() {
		return hdDetTrecxMetPago;
	}
	public void setHdDetTrecxMetPago(HtmlDialogWindowHeader hdDetTrecxMetPago) {
		this.hdDetTrecxMetPago = hdDetTrecxMetPago;
	}
	public HtmlDialogWindow getRv_dwReciboxtipoMetPago() {
		return rv_dwReciboxtipoMetPago;
	}
	public void setRv_dwReciboxtipoMetPago(HtmlDialogWindow rv_dwReciboxtipoMetPago) {
		this.rv_dwReciboxtipoMetPago = rv_dwReciboxtipoMetPago;
	}
	public HtmlGridView getRv_gvRecibosxTipoMetodopago() {
		return rv_gvRecibosxTipoMetodopago;
	}
	public void setRv_gvRecibosxTipoMetodopago(
			HtmlGridView rv_gvRecibosxTipoMetodopago) {
		this.rv_gvRecibosxTipoMetodopago = rv_gvRecibosxTipoMetodopago;
	}
	public List getRv_lstRecxTipoMetpago() {
		if(CodeUtil.getFromSessionMap( "rv_lstRecxTipoMetpago")==null)
			rv_lstRecxTipoMetpago = new ArrayList();
		else
			rv_lstRecxTipoMetpago = (ArrayList)CodeUtil.getFromSessionMap( "rv_lstRecxTipoMetpago");
		return rv_lstRecxTipoMetpago;
	}
	public void setRv_lstRecxTipoMetpago(List rv_lstRecxTipoMetpago) {
		this.rv_lstRecxTipoMetpago = rv_lstRecxTipoMetpago;
	}
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
	public HtmlDialogWindowHeader getHdRecxTipoIng() {
		return hdRecxTipoIng;
	}
	public void setHdRecxTipoIng(HtmlDialogWindowHeader hdRecxTipoIng) {
		this.hdRecxTipoIng = hdRecxTipoIng;
	}
	public List getRv_lstRecibosxIngresos() {
		if(CodeUtil.getFromSessionMap( "rv_lstRecibosxIngresos")==null)
			rv_lstRecibosxIngresos = new ArrayList();
		else
			rv_lstRecibosxIngresos = (ArrayList)CodeUtil.getFromSessionMap( "rv_lstRecibosxIngresos");
		return rv_lstRecibosxIngresos;
	}
	public void setRv_lstRecibosxIngresos(List rv_lstRecibosxIngresos) {
		this.rv_lstRecibosxIngresos = rv_lstRecibosxIngresos;
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
	public HtmlDropDownList getRv_ddlDetalleFacCon() {
		return rv_ddlDetalleFacCon;
	}
	public void setRv_ddlDetalleFacCon(HtmlDropDownList rv_ddlDetalleFacCon) {
		this.rv_ddlDetalleFacCon = rv_ddlDetalleFacCon;
	}
	public HtmlDialogWindow getRv_dwDetalleFactura() {
		return rv_dwDetalleFactura;
	}
	public void setRv_dwDetalleFactura(HtmlDialogWindow rv_dwDetalleFactura) {
		this.rv_dwDetalleFactura = rv_dwDetalleFactura;
	}
	public List getRv_lstCierreCajaDetfactura() {
		if(CodeUtil.getFromSessionMap( "rv_lstCierreCajaDetfactura")==null)
			rv_lstCierreCajaDetfactura = new ArrayList();
		else
			rv_lstCierreCajaDetfactura = (ArrayList)CodeUtil.getFromSessionMap( "rv_lstCierreCajaDetfactura");
		
		return rv_lstCierreCajaDetfactura;
	}
	public void setRv_lstCierreCajaDetfactura(List rv_lstCierreCajaDetfactura) {
		this.rv_lstCierreCajaDetfactura = rv_lstCierreCajaDetfactura;
	}
	public List getRv_lstMonedasDetalle() {
		if(CodeUtil.getFromSessionMap( "rv_lstMonedasDetalle")== null)
			rv_lstMonedasDetalle = new ArrayList();
		else
			rv_lstMonedasDetalle = (ArrayList)CodeUtil.getFromSessionMap( "rv_lstMonedasDetalle");			
		return rv_lstMonedasDetalle;
	}
	public void setRv_lstMonedasDetalle(List rv_lstMonedasDetalle) {
		this.rv_lstMonedasDetalle = rv_lstMonedasDetalle;
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
	public HtmlDialogWindow getRv_dwDetalleRecibo() {
		return rv_dwDetalleRecibo;
	}
	public void setRv_dwDetalleRecibo(HtmlDialogWindow rv_dwDetalleRecibo) {
		this.rv_dwDetalleRecibo = rv_dwDetalleRecibo;
	}
	public HtmlGridView getRv_gvDetalleRecibo() {
		return rv_gvDetalleRecibo;
	}
	public void setRv_gvDetalleRecibo(HtmlGridView rv_gvDetalleRecibo) {
		this.rv_gvDetalleRecibo = rv_gvDetalleRecibo;
	}
	public HtmlGridView getRv_gvFacturasRecibo() {
		return rv_gvFacturasRecibo;
	}
	public void setRv_gvFacturasRecibo(HtmlGridView rv_gvFacturasRecibo) {
		this.rv_gvFacturasRecibo = rv_gvFacturasRecibo;
	}
	public List getRv_lstDetalleRecibo() {
		if(CodeUtil.getFromSessionMap( "rv_lstDetalleRecibo")==null)
			rv_lstDetalleRecibo = new ArrayList();
		else
			rv_lstDetalleRecibo = (ArrayList)CodeUtil.getFromSessionMap( "rv_lstDetalleRecibo");
		return rv_lstDetalleRecibo;
	}
	public void setRv_lstDetalleRecibo(List rv_lstDetalleRecibo) {
		this.rv_lstDetalleRecibo = rv_lstDetalleRecibo;
	}
	public List getRv_lstFacturasRecibo() {
		if(CodeUtil.getFromSessionMap( "rv_lstFacturasRecibo") == null)
			rv_lstFacturasRecibo = new ArrayList();
		else
			rv_lstFacturasRecibo = (ArrayList)CodeUtil.getFromSessionMap( "rv_lstFacturasRecibo");
		return rv_lstFacturasRecibo;
	}
	public void setRv_lstFacturasRecibo(List rv_lstFacturasRecibo) {
		this.rv_lstFacturasRecibo = rv_lstFacturasRecibo;
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
	public HtmlOutputText getLblaprobDepSugerido() {
		return lblaprobDepSugerido;
	}
	public void setLblaprobDepSugerido(HtmlOutputText lblaprobDepSugerido) {
		this.lblaprobDepSugerido = lblaprobDepSugerido;
	}
	public HtmlInputText getTxtaprobDepFinal() {
		return txtaprobDepFinal;
	}
	public void setTxtaprobDepFinal(HtmlInputText txtaprobDepFinal) {
		this.txtaprobDepFinal = txtaprobDepFinal;
	}
	public HtmlOutputText getLblCDC_pagochks() {
		return lblCDC_pagochks;
	}
	public void setLblCDC_pagochks(HtmlOutputText lblCDC_pagochks) {
		this.lblCDC_pagochks = lblCDC_pagochks;
	}
	public HtmlInputText getTxtCDC_ReferDeposito() {
		return txtCDC_ReferDeposito;
	}
	public void setTxtCDC_ReferDeposito(HtmlInputText txtCDC_ReferDeposito) {
		this.txtCDC_ReferDeposito = txtCDC_ReferDeposito;
	}
	public HtmlDialogWindow getDwValidarAprobacionArqueo() {
		return dwValidarAprobacionArqueo;
	}
	public void setDwValidarAprobacionArqueo(
			HtmlDialogWindow dwValidarAprobacionArqueo) {
		this.dwValidarAprobacionArqueo = dwValidarAprobacionArqueo;
	}
	public HtmlOutputText getLblValidarAprobacion() {
		return lblValidarAprobacion;
	}
	public void setLblValidarAprobacion(HtmlOutputText lblValidarAprobacion) {
		this.lblValidarAprobacion = lblValidarAprobacion;
	}
	public HtmlDialogWindow getRv_dwCancelarAprArqueo() {
		return rv_dwCancelarAprArqueo;
	}
	public void setRv_dwCancelarAprArqueo(HtmlDialogWindow rv_dwCancelarAprArqueo) {
		this.rv_dwCancelarAprArqueo = rv_dwCancelarAprArqueo;
	}
	public HtmlDialogWindow getRv_dwConfirmarProcesarArq() {
		return rv_dwConfirmarProcesarArq;
	}
	public void setRv_dwConfirmarProcesarArq(
			HtmlDialogWindow rv_dwConfirmarProcesarArq) {
		this.rv_dwConfirmarProcesarArq = rv_dwConfirmarProcesarArq;
	}
	public HtmlDialogWindow getRv_dwRechazarArqueoCaja() {
		return rv_dwRechazarArqueoCaja;
	}
	public void setRv_dwRechazarArqueoCaja(HtmlDialogWindow rv_dwRechazarArqueoCaja) {
		this.rv_dwRechazarArqueoCaja = rv_dwRechazarArqueoCaja;
	}
	public HtmlInputTextarea getTxtMotivoRechazoArqueo() {
		return txtMotivoRechazoArqueo;
	}
	public void setTxtMotivoRechazoArqueo(HtmlInputTextarea txtMotivoRechazoArqueo) {
		this.txtMotivoRechazoArqueo = txtMotivoRechazoArqueo;
	}
	public HtmlOutputText getLblMsgMotivoRechazo() {
		return lblMsgMotivoRechazo;
	}
	public void setLblMsgMotivoRechazo(HtmlOutputText lblMsgMotivoRechazo) {
		this.lblMsgMotivoRechazo = lblMsgMotivoRechazo;
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
	public HtmlOutputText getLblTotalEgrRecxmonex() {
		return lblTotalEgrRecxmonex;
	}
	public void setLblTotalEgrRecxmonex(HtmlOutputText lblTotalEgrRecxmonex) {
		this.lblTotalEgrRecxmonex = lblTotalEgrRecxmonex;
	}
	public HtmlOutputText getLblTotalIngRecxmonex() {
		return lblTotalIngRecxmonex;
	}
	public void setLblTotalIngRecxmonex(HtmlOutputText lblTotalIngRecxmonex) {
		this.lblTotalIngRecxmonex = lblTotalIngRecxmonex;
	}
	public List getRv_lstDetRecpagMonEx() {
		try{
			if(CodeUtil.getFromSessionMap( "rv_lstDetRecpagMonEx") == null)
				rv_lstDetRecpagMonEx = new ArrayList();
			else
				rv_lstDetRecpagMonEx = (ArrayList)CodeUtil.getFromSessionMap( "rv_lstDetRecpagMonEx");			
		}catch(Exception error){
			error.printStackTrace();
		}
		return rv_lstDetRecpagMonEx;
	}
	public void setRv_lstDetRecpagMonEx(List rv_lstDetRecpagMonEx) {
		this.rv_lstDetRecpagMonEx = rv_lstDetRecpagMonEx;
	}
	public HtmlDialogWindow getDwEgresosxMetPago() {
		return dwEgresosxMetPago;
	}
	public void setDwEgresosxMetPago(HtmlDialogWindow dwEgresosxMetPago) {
		this.dwEgresosxMetPago = dwEgresosxMetPago;
	}
	public HtmlOutputText getLblPagoDepBanco() {
		return lblPagoDepBanco;
	}
	public void setLblPagoDepBanco(HtmlOutputText lblPagoDepBanco) {
		this.lblPagoDepBanco = lblPagoDepBanco;
	}
	public HtmlOutputText getLblPagoTarjetaCredito() {
		return lblPagoTarjetaCredito;
	}
	public void setLblPagoTarjetaCredito(HtmlOutputText lblPagoTarjetaCredito) {
		this.lblPagoTarjetaCredito = lblPagoTarjetaCredito;
	}
	public HtmlOutputText getLblPagoTransBanco() {
		return lblPagoTransBanco;
	}
	public void setLblPagoTransBanco(HtmlOutputText lblPagoTransBanco) {
		this.lblPagoTransBanco = lblPagoTransBanco;
	}
	public HtmlDialogWindowHeader getHdEgrxmetp() {
		return hdEgrxmetp;
	}
	public void setHdEgrxmetp(HtmlDialogWindowHeader hdEgrxmetp) {
		this.hdEgrxmetp = hdEgrxmetp;
	}
	public HtmlDialogWindow getDwDetOtrosEgresos() {
		return dwDetOtrosEgresos;
	}
	public void setDwDetOtrosEgresos(HtmlDialogWindow dwDetOtrosEgresos) {
		this.dwDetOtrosEgresos = dwDetOtrosEgresos;
	}
	public HtmlDialogWindow getDwDetalleCambios() {
		return dwDetalleCambios;
	}
	public void setDwDetalleCambios(HtmlDialogWindow dwDetalleCambios) {
		this.dwDetalleCambios = dwDetalleCambios;
	}
	public HtmlGridView getGvDetalleCambios() {
		return gvDetalleCambios;
	}
	public void setGvDetalleCambios(HtmlGridView gvDetalleCambios) {
		this.gvDetalleCambios = gvDetalleCambios;
	}
	public List getLstDetalleCambios() {
		if(CodeUtil.getFromSessionMap( "rv_lstDetalleCambios") == null)
			lstDetalleCambios = new ArrayList();
		else
			lstDetalleCambios = (ArrayList)CodeUtil.getFromSessionMap( "rv_lstDetalleCambios");
		return lstDetalleCambios;
	}
	public void setLstDetalleCambios(List lstDetalleCambios) {
		this.lstDetalleCambios = lstDetalleCambios;
	}
	public HtmlDialogWindow getDwDetalleOtrosIngresos() {
		return dwDetalleOtrosIngresos;
	}
	public void setDwDetalleOtrosIngresos(HtmlDialogWindow dwDetalleOtrosIngresos) {
		this.dwDetalleOtrosIngresos = dwDetalleOtrosIngresos;
	}
	public HtmlOutputText getLblCambioOtraMoneda() {
		return lblCambioOtraMoneda;
	}
	public void setLblCambioOtraMoneda(HtmlOutputText lblCambioOtraMoneda) {
		this.lblCambioOtraMoneda = lblCambioOtraMoneda;
	}
	public HtmlOutputText getLblIngresosExtraOrd() {
		return lblIngresosExtraOrd;
	}
	public void setLblIngresosExtraOrd(HtmlOutputText lblIngresosExtraOrd) {
		this.lblIngresosExtraOrd = lblIngresosExtraOrd;
	}
	public HtmlProgressBar getBarraProgresoPrueba() {
		return barraProgresoPrueba;
	}
	public void setBarraProgresoPrueba(HtmlProgressBar barraProgresoPrueba) {
		this.barraProgresoPrueba = barraProgresoPrueba;
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
	public HtmlOutputText getLblTotalIngex() {
		return lblTotalIngex;
	}
	public void setLblTotalIngex(HtmlOutputText lblTotalIngex) {
		this.lblTotalIngex = lblTotalIngex;
	}
	public HtmlOutputText getLblTotalIexPagBanco() {
		return lblTotalIexPagBanco;
	}
	public void setLblTotalIexPagBanco(HtmlOutputText lblTotalIexPagBanco) {
		this.lblTotalIexPagBanco = lblTotalIexPagBanco;
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
			lstDetalleSalidas = CodeUtil.getFromSessionMap( "rev_lstDetalleSalidas")==null? new ArrayList(): (ArrayList)CodeUtil.getFromSessionMap( "rev_lstDetalleSalidas");
		} catch (Exception error) {
			error.printStackTrace();
		}
		return lstDetalleSalidas;
	}
	public void setLstDetalleSalidas(List lstDetalleSalidas) {
		this.lstDetalleSalidas = lstDetalleSalidas;
	}
	public HtmlOutputText getLblTotalFinan() {
		return lblTotalFinan;
	}
	public void setLblTotalFinan(HtmlOutputText lblTotalFinan) {
		this.lblTotalFinan = lblTotalFinan;
	}
	public HtmlOutputText getLblTotalFinanPagBanco() {
		return lblTotalFinanPagBanco;
	}
	public void setLblTotalFinanPagBanco(HtmlOutputText lblTotalFinanPagBanco) {
		this.lblTotalFinanPagBanco = lblTotalFinanPagBanco;
	}
	public DropDownList getDdlFiltroEstado() {
		return ddlFiltroEstado;
	}
	public void setDdlFiltroEstado(DropDownList ddlFiltroEstado) {
		this.ddlFiltroEstado = ddlFiltroEstado;
	}
	public List getLstFiltroEstado() {
		
		try {
			if(CodeUtil.getFromSessionMap( "rva_lstFiltroEstado")==null){
				
				SalidasCtrl sCtrl = new SalidasCtrl();
				List lstEstados = sCtrl.leerValorCatalogo(1);
				lstFiltroEstado = new ArrayList();
				lstFiltroEstado.add(new SelectItem("SE","Estados","Seleccione el estado de arqueos"));
				if(lstEstados !=null && lstEstados.size()>0){
					for(int i=0; i<lstEstados.size();i++){
						Valorcatalogo v = (Valorcatalogo)lstEstados.get(i);
						lstFiltroEstado.add(new SelectItem(v.getCodigointerno(),
										v.getDescripcion(),v.getDescripcion()));
					}
				}
				CodeUtil.putInSessionMap("rva_lstFiltroEstado", lstFiltroEstado);
			}else{
				lstFiltroEstado = (ArrayList)CodeUtil.getFromSessionMap( "rva_lstFiltroEstado");
			}
		} catch (Exception error) {
			error.printStackTrace();
		}		
		return lstFiltroEstado;
	}
	public void setLstFiltroEstado(List lstFiltroEstado) {
		this.lstFiltroEstado = lstFiltroEstado;
	}
	public DropDownList getDdlFiltroCaja() {
		return ddlFiltroCaja;
	}
	public void setDdlFiltroCaja(DropDownList ddlFiltroCaja) {
		this.ddlFiltroCaja = ddlFiltroCaja;
	}
	public List getLstFiltroCaja() {
		try {

			if(CodeUtil.getFromSessionMap( "rva_lstFiltroCaja")==null){
				List lstcaja = (ArrayList)CodeUtil.getFromSessionMap( "lstCajas");
				Vf55ca01 caja = (Vf55ca01)lstcaja.get(0);
				CtrlCajas cc = new CtrlCajas();
				
				lstFiltroCaja = new ArrayList();
				lstFiltroCaja.add(new SelectItem("SCA","Todas","Seleccione la Caja para filtrar"));
				
				
				Vautoriz[] vAut = (Vautoriz[])CodeUtil.getFromSessionMap( "sevAut");
				
				if (vAut[0].getId().getCodper().compareTo(cajaparm.getParametros("33", "0", "ENS_CONCILI_PRIN").getValorAlfanumerico().toString())  == 0 || 
					vAut[0].getId().getCodper().compareTo(cajaparm.getParametros("33", "0", "ENS_CONCIL_SUPER").getValorAlfanumerico().toString()) == 0 ){
					
					String sql = "select * from " + PropertiesSystem.ESQUEMA +".Vf55ca01 where castat = 'A' " ; 
					List<Vf55ca01> cajas = (ArrayList<Vf55ca01>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, true, Vf55ca01.class);
					
					for (Vf55ca01 v : cajas) {
						lstFiltroCaja.add(new SelectItem( 
								String.valueOf( v.getId().getCaid()),	
								"# " + v.getId().getCaid() + " " + CodeUtil.capitalize( v.getId().getCaname().trim() ),
								v.getId().getCaname().trim() ) );
					}
				}else{
					
					List lstCaContador = cc.obtenerCajasxContador(caja.getId().getCacont());
					if(lstCaContador!=null && lstCaContador.size()>0){
						for(int i=0;i<lstCaContador.size();i++){
							Vf55ca01 v = (Vf55ca01)lstCaContador.get(i);
							lstFiltroCaja.add(new SelectItem(v.getId().getCaid()+"",
										v.getId().getCaname().trim(),
										v.getId().getCaid()+ " " +v.getId().getCaname().trim() ));
						}
					}
				}
				
				CodeUtil.putInSessionMap("rva_lstFiltroCaja", lstFiltroCaja);
				
			}else{
				lstFiltroCaja = (ArrayList)CodeUtil.getFromSessionMap( "rva_lstFiltroCaja");
			}
		} catch (Exception error) {
			LogCajaService.CreateLog("getLstFiltroCaja", "ERR", error.getMessage());
		}
		return lstFiltroCaja;
	}
	public void setLstFiltroCaja(List lstFiltroCaja) {
		this.lstFiltroCaja = lstFiltroCaja;
	}
	public HtmlDialogWindow getDwCargarReferenciasPOS() {
		return dwCargarReferenciasPOS;
	}
	public void setDwCargarReferenciasPOS(HtmlDialogWindow dwCargarReferenciasPOS) {
		this.dwCargarReferenciasPOS = dwCargarReferenciasPOS;
	}
	public HtmlGridView getGvReferenciaPos() {
		return gvReferenciaPos;
	}
	public void setGvReferenciaPos(HtmlGridView gvReferenciaPos) {
		this.gvReferenciaPos = gvReferenciaPos;
	}
	public List getLstReferenciapos() {
		try {
			if(CodeUtil.getFromSessionMap( "rva_lstReferenciapos")==null)
				lstReferenciapos = new ArrayList();
			else
				lstReferenciapos = (ArrayList)CodeUtil.getFromSessionMap( "rva_lstReferenciapos");
		} catch (Exception error) {
			error.printStackTrace();
		}
		return lstReferenciapos;
	}
	public void setLstReferenciapos(List lstReferenciapos) {
		this.lstReferenciapos = lstReferenciapos;
	}
	public HtmlOutputText getLblMsgValidaReferencia() {
		return lblMsgValidaReferencia;
	}
	public void setLblMsgValidaReferencia(HtmlOutputText lblMsgValidaReferencia) {
		this.lblMsgValidaReferencia = lblMsgValidaReferencia;
	}
	public HtmlDateChooser getDcFechaArqueo() {
		return dcFechaArqueo;
	}
	public void setDcFechaArqueo(HtmlDateChooser dcFechaArqueo) {
		this.dcFechaArqueo = dcFechaArqueo;
	}
	public Date getDtFechaArqueo() {
		if(CodeUtil.getFromSessionMap( "rva_dtFechaArqueo")==null){
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -5);
			dtFechaArqueo = cal.getTime();
			CodeUtil.putInSessionMap("rva_dtFechaArqueo",dtFechaArqueo);
		}
		return dtFechaArqueo;
	}
	public void setDtFechaArqueo(Date dtFechaArqueo) {
		this.dtFechaArqueo = dtFechaArqueo;
	}
	public HtmlDialogWindow getRv_dwConfirmarReimpresionRpt() {
		return rv_dwConfirmarReimpresionRpt;
	}
	public void setRv_dwConfirmarReimpresionRpt(
			HtmlDialogWindow rv_dwConfirmarReimpresionRpt) {
		this.rv_dwConfirmarReimpresionRpt = rv_dwConfirmarReimpresionRpt;
	}
	public HtmlColumn getCoDtmpagoRefer1() {
		return coDtmpagoRefer1;
	}
	public void setCoDtmpagoRefer1(HtmlColumn coDtmpagoRefer1) {
		this.coDtmpagoRefer1 = coDtmpagoRefer1;
	}
	public HtmlColumn getCoDtmpagoRefer2() {
		return coDtmpagoRefer2;
	}
	public void setCoDtmpagoRefer2(HtmlColumn coDtmpagoRefer2) {
		this.coDtmpagoRefer2 = coDtmpagoRefer2;
	}
	public HtmlColumn getCoDtmpagoRefer3() {
		return coDtmpagoRefer3;
	}
	public void setCoDtmpagoRefer3(HtmlColumn coDtmpagoRefer3) {
		this.coDtmpagoRefer3 = coDtmpagoRefer3;
	}
	public HtmlOutputText getLblMsgErrorCambioRefer() {
		return lblMsgErrorCambioRefer;
	}
	public void setLblMsgErrorCambioRefer(HtmlOutputText lblMsgErrorCambioRefer) {
		this.lblMsgErrorCambioRefer = lblMsgErrorCambioRefer;
	}
	public HtmlDialogWindow getRv_dwEditarRecibosIdPos() {
		return rv_dwEditarRecibosIdPos;
	}
	public void setRv_dwEditarRecibosIdPos(HtmlDialogWindow rv_dwEditarRecibosIdPos) {
		this.rv_dwEditarRecibosIdPos = rv_dwEditarRecibosIdPos;
	}
	public HtmlGridView getRv_gvEditarRecibosIdPos() {
		return rv_gvEditarRecibosIdPos;
	}
	public void setRv_gvEditarRecibosIdPos(HtmlGridView rv_gvEditarRecibosIdPos) {
		this.rv_gvEditarRecibosIdPos = rv_gvEditarRecibosIdPos;
	}
	public List getRv_lstEditarRecibosIdPos() {
		try {
			rv_lstEditarRecibosIdPos = (CodeUtil.getFromSessionMap( "rva_rv_lstEditarRecibosIdPos")==null)? 
							new ArrayList():
							(ArrayList)CodeUtil.getFromSessionMap( "rva_rv_lstEditarRecibosIdPos");
		} catch (Exception error) {
			error.printStackTrace();
		}
		return rv_lstEditarRecibosIdPos;
	}
	public void setRv_lstEditarRecibosIdPos(List rv_lstEditarRecibosIdPos) {
		this.rv_lstEditarRecibosIdPos = rv_lstEditarRecibosIdPos;
	}
	public HtmlOutputText getLblACDetfp_TCmanual() {
		return lblACDetfp_TCmanual;
	}
	public void setLblACDetfp_TCmanual(HtmlOutputText lblACDetfp_TCmanual) {
		this.lblACDetfp_TCmanual = lblACDetfp_TCmanual;
	}
	public HtmlDialogWindow getDwCargando() {
		return dwCargando;
	}
	public void setDwCargando(HtmlDialogWindow dwCargando) {
		this.dwCargando = dwCargando;
	}
	public HtmlDialogWindow getRv_dwAsignarReferCheque() {
		return rv_dwAsignarReferCheque;
	}
	public void setRv_dwAsignarReferCheque(HtmlDialogWindow rv_dwAsignarReferCheque) {
		this.rv_dwAsignarReferCheque = rv_dwAsignarReferCheque;
	}
	public HtmlGridView getRv_gvAsignarReferenciaCheque() {
		return rv_gvAsignarReferenciaCheque;
	}
	public void setRv_gvAsignarReferenciaCheque(
			HtmlGridView rv_gvAsignarReferenciaCheque) {
		this.rv_gvAsignarReferenciaCheque = rv_gvAsignarReferenciaCheque;
	}
	public List getRv_lstAsignarReferenciaCheque() {
		try {
			if(CodeUtil.getFromSessionMap( "rva_lstAsignarReferenciaCheque")==null){
				rv_lstAsignarReferenciaCheque = new ArrayList();
				CodeUtil.removeFromSessionMap( "rva_lstLiquidacionCheques" );
			}
			else
				rv_lstAsignarReferenciaCheque = (ArrayList)CodeUtil.getFromSessionMap( "rva_lstAsignarReferenciaCheque");
		} catch (Exception error) {
			error.printStackTrace();
		}
		return rv_lstAsignarReferenciaCheque;
	}
	public void setRv_lstAsignarReferenciaCheque(List rv_lstAsignarReferenciaCheque) {
		this.rv_lstAsignarReferenciaCheque = rv_lstAsignarReferenciaCheque;
	}
	public HtmlOutputText getLblMsgErrorAsignarReferChk() {
		return lblMsgErrorAsignarReferChk;
	}
	public void setLblMsgErrorAsignarReferChk(
			HtmlOutputText lblMsgErrorAsignarReferChk) {
		this.lblMsgErrorAsignarReferChk = lblMsgErrorAsignarReferChk;
	}
	public HtmlLink getLnkLiquidacionChk() {
		return lnkLiquidacionChk;
	}
	public void setLnkLiquidacionChk(HtmlLink lnkLiquidacionChk) {
		this.lnkLiquidacionChk = lnkLiquidacionChk;
	}
	public HtmlOutputText getLblACDetfp_TCsocketpos() {
		return lblACDetfp_TCsocketpos;
	}
	public void setLblACDetfp_TCsocketpos(HtmlOutputText lblACDetfp_TCsocketpos) {
		this.lblACDetfp_TCsocketpos = lblACDetfp_TCsocketpos;
	}
	public HtmlLink getLnkAnularArqueoCaja() {
		return lnkAnularArqueoCaja;
	}
	public void setLnkAnularArqueoCaja(HtmlLink lnkAnularArqueoCaja) {
		this.lnkAnularArqueoCaja = lnkAnularArqueoCaja;
	}
	public HtmlDialogWindow getDwRecibosxMpago8N() {
		return dwRecibosxMpago8N;
	}
	public HtmlDialogWindowHeader getHdReciboMPago8N() {
		return hdReciboMPago8N;
	}
	public HtmlGridView getGvRecibosxMpago8N() {
		return gvRecibosxMpago8N;
	}
	public List<Vrecibosxtipompago> getLstRecibosxMpago8N() {
		if(CodeUtil.getFromSessionMap( "rv_lstRecibosxMpago8N") == null)
			lstRecibosxMpago8N = new ArrayList<Vrecibosxtipompago>();
		else
			lstRecibosxMpago8N = (ArrayList<Vrecibosxtipompago>)
											CodeUtil.getFromSessionMap( "rv_lstRecibosxMpago8N");
		return lstRecibosxMpago8N;
	}
	public HtmlOutputText getLblMsgCambioReferMPago() {
		return lblMsgCambioReferMPago;
	}
	public void setDwRecibosxMpago8N(HtmlDialogWindow dwRecibosxMpago8N) {
		this.dwRecibosxMpago8N = dwRecibosxMpago8N;
	}
	public void setHdReciboMPago8N(HtmlDialogWindowHeader hdReciboMPago8N) {
		this.hdReciboMPago8N = hdReciboMPago8N;
	}
	public void setGvRecibosxMpago8N(HtmlGridView gvRecibosxMpago8N) {
		this.gvRecibosxMpago8N = gvRecibosxMpago8N;
	}
	public void setLstRecibosxMpago8N(List<Vrecibosxtipompago> lstRecibosxMpago8N) {
		this.lstRecibosxMpago8N = lstRecibosxMpago8N;
	}
	public void setLblMsgCambioReferMPago(HtmlOutputText lblMsgCambioReferMPago) {
		this.lblMsgCambioReferMPago = lblMsgCambioReferMPago;
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
	public HtmlDialogWindow getDwUpdParamBloqueo() {
		return dwUpdParamBloqueo;
	}
	public void setDwUpdParamBloqueo(HtmlDialogWindow dwUpdParamBloqueo) {
		this.dwUpdParamBloqueo = dwUpdParamBloqueo;
	}
	public HtmlInputTextarea getTxtDescripcionCambio() {
		return txtDescripcionCambio;
	}
	public void setTxtDescripcionCambio(HtmlInputTextarea txtDescripcionCambio) {
		this.txtDescripcionCambio = txtDescripcionCambio;
	}
	public HtmlInputText getTxtdiasblck() {
		return txtdiasblck;
	}
	public void setTxtdiasblck(HtmlInputText txtdiasblck) {
		this.txtdiasblck = txtdiasblck;
	}
	public HtmlCheckBox getChkbloqueocaja() {
		return chkbloqueocaja;
	}
	public void setChkbloqueocaja(HtmlCheckBox chkbloqueocaja) {
		this.chkbloqueocaja = chkbloqueocaja;
	}
	public HtmlLink getLnkParametrosBloqueo() {
		return lnkParametrosBloqueo;
	}
	public void setLnkParametrosBloqueo(HtmlLink lnkParametrosBloqueo) {
		this.lnkParametrosBloqueo = lnkParametrosBloqueo;
	}
	public String getStrParametrosBloqueo() {
		try {
			Vautoriz[] vaut = (Vautoriz[]) CodeUtil.getFromSessionMap( "sevAut");
			strParametrosBloqueo = vaut[0].getId().getCodper()
										.compareTo("P000000004")== 0 ?
									"display:inline":"display:none";
		} catch (Exception e) {
			strParametrosBloqueo = "display:none";
			e.printStackTrace();
		}
		return strParametrosBloqueo;
	}
	public void setStrParametrosBloqueo(String strParametrosBloqueo) {
		this.strParametrosBloqueo = strParametrosBloqueo;
	}
	public HtmlOutputText getLblMsgValidaUpdParm() {
		return lblMsgValidaUpdParm;
	}
	public void setLblMsgValidaUpdParm(HtmlOutputText lblMsgValidaUpdParm) {
		this.lblMsgValidaUpdParm = lblMsgValidaUpdParm;
	}
	public HtmlInputTextarea getTxtObservacionesArqueo() {
		return txtObservacionesArqueo;
	}
	public void setTxtObservacionesArqueo(HtmlInputTextarea txtObservacionesArqueo) {
		this.txtObservacionesArqueo = txtObservacionesArqueo;
	}
	public HtmlDateChooser getDcFechaArqueoFin() {
		return dcFechaArqueoFin;
	}
	public void setDcFechaArqueoFin(HtmlDateChooser dcFechaArqueoFin) {
		this.dcFechaArqueoFin = dcFechaArqueoFin;
	}
	public Date getDtFechaArqueoFin() {
		
		if(CodeUtil.getFromSessionMap( "rva_dtFechaArqueoFin")==null){
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -1);
			dtFechaArqueoFin = cal.getTime();
			CodeUtil.putInSessionMap("rva_dtFechaArqueoFin",dtFechaArqueoFin);
		}
		return dtFechaArqueoFin;
	}
	public void setDtFechaArqueoFin(Date dtFechaArqueoFin) {
		this.dtFechaArqueoFin = dtFechaArqueoFin;
	}
	public HtmlDialogWindow getDwReimprimirDocsxCierre() {
		return dwReimprimirDocsxCierre;
	}
	public void setDwReimprimirDocsxCierre(HtmlDialogWindow dwReimprimirDocsxCierre) {
		this.dwReimprimirDocsxCierre = dwReimprimirDocsxCierre;
	}
	public HtmlGridView getGvDonacionesFormaPago() {
		return gvDonacionesFormaPago;
	}

	public void setGvDonacionesFormaPago(HtmlGridView gvDonacionesFormaPago) {
		this.gvDonacionesFormaPago = gvDonacionesFormaPago;
	}

	public List<DncDonacion> getLstDonacionesFormaPago() {
		
		if( CodeUtil.getFromSessionMap("rva_lstDonacionesFormaPago") != null )
			lstDonacionesFormaPago = (List<DncDonacion>)CodeUtil.getFromSessionMap("rva_lstDonacionesFormaPago");
		
		return  lstDonacionesFormaPago == null ? new ArrayList<DncDonacion>(): lstDonacionesFormaPago;
	}

	public void setLstDonacionesFormaPago(List<DncDonacion> lstDonacionesFormaPago) {
		this.lstDonacionesFormaPago = lstDonacionesFormaPago;
	}
	public HtmlGridView getGvDetalleDonacionesMpago() {
		return gvDetalleDonacionesMpago;
	}

	public void setGvDetalleDonacionesMpago(HtmlGridView gvDetalleDonacionesMpago) {
		this.gvDetalleDonacionesMpago = gvDetalleDonacionesMpago;
	}
	public List<DncDonacion> getLstDetalleDonacionesMpago() {
		
		if( CodeUtil.getFromSessionMap("rva_lstDetalleDonacionesMpago") != null )
			lstDetalleDonacionesMpago = (ArrayList<DncDonacion>)CodeUtil.getFromSessionMap("rva_lstDetalleDonacionesMpago");
		
		return  lstDetalleDonacionesMpago == null ? new ArrayList<DncDonacion>(): lstDetalleDonacionesMpago;
		
	}
	public void setLstDetalleDonacionesMpago(
			List<DncDonacion> lstDetalleDonacionesMpago) {
		this.lstDetalleDonacionesMpago = lstDetalleDonacionesMpago;
	}
	public HtmlDialogWindow getDwDetalleDonacionesMpago() {
		return dwDetalleDonacionesMpago;
	}
	public void setDwDetalleDonacionesMpago(
			HtmlDialogWindow dwDetalleDonacionesMpago) {
		this.dwDetalleDonacionesMpago = dwDetalleDonacionesMpago;
	}
	public HtmlDialogWindow getDwSeleccionTipoTarjeta() {
		return dwSeleccionTipoTarjeta;
	}
	public void setDwSeleccionTipoTarjeta(HtmlDialogWindow dwSeleccionTipoTarjeta) {
		this.dwSeleccionTipoTarjeta = dwSeleccionTipoTarjeta;
	}
	public HtmlGridView getGvActualizaCodigoAfiliado() {
		return gvActualizaCodigoAfiliado;
	}
	public void setGvActualizaCodigoAfiliado(HtmlGridView gvActualizaCodigoAfiliado) {
		this.gvActualizaCodigoAfiliado = gvActualizaCodigoAfiliado;
	}
	public List<Vrecibosxtipompago> getLstGvActualizaCodigoAfiliado() {
		
		try {
			
			if( CodeUtil.getFromSessionMap("rva_lstGvActualizaCodigoAfiliado") != null )
				return lstGvActualizaCodigoAfiliado = (ArrayList<Vrecibosxtipompago>) CodeUtil.getFromSessionMap("rva_lstGvActualizaCodigoAfiliado");
			
		} catch (Exception e) {
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
		}finally{
			
			if(lstGvActualizaCodigoAfiliado == null )
				lstGvActualizaCodigoAfiliado = new ArrayList<Vrecibosxtipompago>();

		 CodeUtil.putInSessionMap("rva_lstGvActualizaCodigoAfiliado", lstGvActualizaCodigoAfiliado);
			
		}
		return  lstGvActualizaCodigoAfiliado  ;
		
	}
	public void setLstGvActualizaCodigoAfiliado(
			List<Vrecibosxtipompago> lstGvActualizaCodigoAfiliado) {
		this.lstGvActualizaCodigoAfiliado = lstGvActualizaCodigoAfiliado;
	}
	public List<SelectItem> getLstMarcasTarjetaDisponibles() {
		
	try {
			
		lstMarcasTarjetaDisponibles = (ArrayList<SelectItem>)CodeUtil.getFromSessionMap("rva_lstMarcasDeTarjetas");
			if(lstMarcasTarjetaDisponibles != null &&  !lstMarcasTarjetaDisponibles.isEmpty() )
				return lstMarcasTarjetaDisponibles;
			
			lstMarcasTarjetaDisponibles = AfiliadoCtrl.obtenerTiposMarcaTarjetas();
			
		} catch (Exception e) {
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
		}finally{
			
			if(lstMarcasTarjetaDisponibles == null )
				lstMarcasTarjetaDisponibles = new ArrayList<SelectItem>();

			 CodeUtil.putInSessionMap("rva_lstMarcasTarjetaDisponibles", lstMarcasTarjetaDisponibles);
		}
		
		return lstMarcasTarjetaDisponibles;
	}
	public void setLstMarcasTarjetaDisponibles(
			List<SelectItem> lstMarcasTarjetaDisponibles) {
		this.lstMarcasTarjetaDisponibles = lstMarcasTarjetaDisponibles;
	}
	public HtmlDropDownList getDdlTipoMarcasTarjetaDisponibles() {
		return ddlTipoMarcasTarjetaDisponibles;
	}
	public void setDdlTipoMarcasTarjetaDisponibles(
			HtmlDropDownList ddlTipoMarcasTarjetaDisponibles) {
		this.ddlTipoMarcasTarjetaDisponibles = ddlTipoMarcasTarjetaDisponibles;
	}
	public HtmlOutputText getLblMsgValidaCambioAfiliado() {
		return lblMsgValidaCambioAfiliado;
	}
	public void setLblMsgValidaCambioAfiliado(
			HtmlOutputText lblMsgValidaCambioAfiliado) {
		this.lblMsgValidaCambioAfiliado = lblMsgValidaCambioAfiliado;
	}
	public HtmlDialogWindow getDwSeleccionarBancoParaCambio() {
		return dwSeleccionarBancoParaCambio;
	}
	public void setDwSeleccionarBancoParaCambio(
			HtmlDialogWindow dwSeleccionarBancoParaCambio) {
		this.dwSeleccionarBancoParaCambio = dwSeleccionarBancoParaCambio;
	}
	public HtmlGridView getGvBancosDisponiblesCambio() {
		return gvBancosDisponiblesCambio;
	}
	public void setGvBancosDisponiblesCambio(HtmlGridView gvBancosDisponiblesCambio) {
		this.gvBancosDisponiblesCambio = gvBancosDisponiblesCambio;
	}
	public List<Vwcuentascontablesporbanco> getLstBancosDisponiblesCambio() {
		
		if(CodeUtil.getFromSessionMap("rva_lstBancosDisponiblesCambio") != null){
			return lstBancosDisponiblesCambio = (ArrayList<Vwcuentascontablesporbanco>)CodeUtil.getFromSessionMap("rva_lstBancosDisponiblesCambio");
		}

		lstBancosDisponiblesCambio = new ArrayList<Vwcuentascontablesporbanco>();
		CodeUtil.putInSessionMap("rva_lstBancosDisponiblesCambio", lstBancosDisponiblesCambio);
		
		return lstBancosDisponiblesCambio;
	}
	public void setLstBancosDisponiblesCambio(
			List<Vwcuentascontablesporbanco> lstBancosDisponiblesCambio) {
		this.lstBancosDisponiblesCambio = lstBancosDisponiblesCambio;
	}
	public HtmlOutputText getLblCambioBancoRecibo() {
		return lblCambioBancoRecibo;
	}
	public void setLblCambioBancoRecibo(HtmlOutputText lblCambioBancoRecibo) {
		this.lblCambioBancoRecibo = lblCambioBancoRecibo;
	}
	public HtmlOutputText getLblCambioBancoNoTransferencia() {
		return lblCambioBancoNoTransferencia;
	}
	public void setLblCambioBancoNoTransferencia(
			HtmlOutputText lblCambioBancoNoTransferencia) {
		this.lblCambioBancoNoTransferencia = lblCambioBancoNoTransferencia;
	}
	public HtmlOutputText getLblCambioBancoNombreBanco() {
		return lblCambioBancoNombreBanco;
	}
	public void setLblCambioBancoNombreBanco(
			HtmlOutputText lblCambioBancoNombreBanco) {
		this.lblCambioBancoNombreBanco = lblCambioBancoNombreBanco;
	}
	public HtmlOutputText getLblCambioBancoCliente() {
		return lblCambioBancoCliente;
	}
	public void setLblCambioBancoCliente(HtmlOutputText lblCambioBancoCliente) {
		this.lblCambioBancoCliente = lblCambioBancoCliente;
	}
	public HtmlOutputText getLblCambioBancoMonto() {
		return lblCambioBancoMonto;
	}
	public void setLblCambioBancoMonto(HtmlOutputText lblCambioBancoMonto) {
		this.lblCambioBancoMonto = lblCambioBancoMonto;
	}
	public HtmlOutputText getLblTotalRecibosPMT() {
		return lblTotalRecibosPMT;
	}
	public void setLblTotalRecibosPMT(HtmlOutputText lblTotalRecibosPMT) {
		this.lblTotalRecibosPMT = lblTotalRecibosPMT;
	}
	public HtmlOutputText getLblTotalAnticiposPMTPagBanco() {
		return lblTotalAnticiposPMTPagBanco;
	}
	public void setLblTotalAnticiposPMTPagBanco(
			HtmlOutputText lblTotalAnticiposPMTPagBanco) {
		this.lblTotalAnticiposPMTPagBanco = lblTotalAnticiposPMTPagBanco;
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
		if(CodeUtil.getFromSessionMap("rva_detalleContratoPmt") != null)
			return detalleContratoPmt = (ArrayList<HistoricoReservasProformas>)CodeUtil.getFromSessionMap("rva_detalleContratoPmt");
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
 
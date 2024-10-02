package com.casapellas.dao;

/**
 * CASA PELLAS S.A.
 * Creado por.........: Juan Carlos Ñamendi Pineda
 * Fecha de Creación..: 20/01/2009
 * Última modificación: 03/08/2011
 * Modificado por.....:	Juan Carlos Ñamendi Pineda.
 * cambio: Agregar numero de factura original a RM para liga
 * 
 */
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.component.html.HtmlInputSecret;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlInputTextarea;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.casapellas.controles.tmp.AfiliadoCtrl;
import com.casapellas.controles.tmp.BancoCtrl;
import com.casapellas.controles.tmp.CompaniaCtrl;
import com.casapellas.controles.tmp.CtrlCajas;
import com.casapellas.controles.tmp.CtrlPoliticas;
import com.casapellas.controles.tmp.CtrlSolicitud;
import com.casapellas.controles.tmp.EmpleadoCtrl;
import com.casapellas.controles.tmp.FacturaContadoCtrl;
import com.casapellas.controles.tmp.FacturaCreditoCtrl;
import com.casapellas.controles.tmp.FacturaCrtl;
import com.casapellas.controles.tmp.MetodosPagoCtrl;
import com.casapellas.controles.tmp.MonedaCtrl;
import com.casapellas.controles.tmp.NumcajaCtrl;
import com.casapellas.controles.tmp.ReciboCtrl;
import com.casapellas.controles.tmp.ReciboPrimaCtrl;
import com.casapellas.controles.tmp.SolechequeCtrl;
import com.casapellas.controles.tmp.TasaCambioCtrl;
import com.businessobjects.visualization.dataexchange.data.graph.INode;
import com.casapellas.controles.ConsolidadoDepositosBcoCtrl;
import com.casapellas.controles.CuotaCtrl;
import com.casapellas.controles.DonacionesCtrl;
import com.casapellas.controles.TrasladoCtrl;
import com.casapellas.controles.VerificarFacturaProceso;
import com.casapellas.donacion.entidades.DncDonacion;
import com.casapellas.donacion.entidades.DncDonacionJde;
import com.casapellas.donacion.entidades.DncIngresoDonacion;
import com.casapellas.entidades.A02factco;
import com.casapellas.entidades.Aplicacion;
import com.casapellas.entidades.Cafiliados;
import com.casapellas.entidades.Cambiodet;
import com.casapellas.entidades.ConfiguracionMensaje;
import com.casapellas.entidades.Credhdr;
import com.casapellas.entidades.Df4211;
import com.casapellas.entidades.Dfactjdecon;
import com.casapellas.entidades.Dfactura;
import com.casapellas.entidades.F55ca014;
import com.casapellas.entidades.F55ca017;
import com.casapellas.entidades.F55ca022;
import com.casapellas.entidades.Hf4211;
import com.casapellas.entidades.Hfactjdecon;
import com.casapellas.entidades.Hfactura;
import com.casapellas.entidades.MetodosPago;
import com.casapellas.entidades.Metpago;
import com.casapellas.entidades.Numcaja;
import com.casapellas.entidades.Recibo;
import com.casapellas.entidades.Recibodet;
import com.casapellas.entidades.Recibofac;
import com.casapellas.entidades.Recibojde;
import com.casapellas.entidades.Solecheque;
import com.casapellas.entidades.SolechequeId;
import com.casapellas.entidades.Solicitud;
import com.casapellas.entidades.SolicitudId;
import com.casapellas.entidades.Tcambio;
import com.casapellas.entidades.TermAfl;
import com.casapellas.entidades.Tpararela;
import com.casapellas.entidades.Trasladofac;
import com.casapellas.entidades.Vf55ca012;
import com.casapellas.entidades.ens.Vautoriz;
import com.casapellas.entidades.Vf0101;
import com.casapellas.entidades.Vf0901;
import com.casapellas.entidades.Vf55ca01;
import com.casapellas.entidades.Vhfactura;
import com.casapellas.entidades.Vrecfac;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.jde.creditos.DatosComprobanteF0911;
import com.casapellas.jde.creditos.ProcesarNuevaFacturaF03B11;
import com.casapellas.navegacion.As400Connection;
import com.casapellas.rpg.P55CA090;
import com.casapellas.rpg.P55RECIBO;
import com.casapellas.socketpos.EcommerceTransaction;
import com.casapellas.socketpos.PosCtrl;
import com.casapellas.util.BitacoraSecuenciaReciboService;
import com.casapellas.util.CalendarToJulian;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.CustomEmailAddress;
import com.casapellas.util.Divisas;
import com.casapellas.util.DocumuentosTransaccionales;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.JulianToCalendar;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.MailHelper;
import com.casapellas.util.PropertiesSystem;
import com.ibm.faces.component.html.HtmlGraphicImageEx;
import com.ibm.faces.component.html.HtmlJspPanel;
import com.infragistics.faces.grid.component.Cell;
import com.infragistics.faces.grid.component.GridView;
import com.infragistics.faces.grid.component.RowItem;
import com.infragistics.faces.grid.component.html.HtmlGridView;
import com.infragistics.faces.grid.event.CellValueChangeEvent;
import com.infragistics.faces.grid.event.SelectedRowsChangeEvent;
import com.infragistics.faces.grid.event.SelectedRowsChangeEvent.SelectedRowChange;
import com.infragistics.faces.input.component.html.HtmlCheckBox;
import com.infragistics.faces.input.component.html.HtmlDateChooser;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
import com.infragistics.faces.shared.component.DataRepeater;
import com.infragistics.faces.shared.component.html.HtmlLink;
import com.infragistics.faces.shared.smartrefresh.SmartRefreshManager;
import com.infragistics.faces.window.component.DialogWindow;
import com.infragistics.faces.window.component.html.HtmlDialogWindow;
import com.infragistics.faces.window.event.StateChangeEvent;

import nl.bitwalker.useragentutils.UserAgent;

public class FacContadoDAO {    
	protected P55RECIBO p55recibo;
	protected P55CA090 p55ca090;

	private HtmlInputSecret track;	
	private UIOutput lblTrack;

	private HtmlCheckBox chkIngresoManual;
	private HtmlOutputText lblNoTarjeta;
	private HtmlInputText txtNoTarjeta;
	private HtmlOutputText lblFechaVenceT;
	private HtmlInputText txtFechaVenceT;

	String sCredyCobMail = "creditoycobro@casapellas.com.ni";

	Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	//cuadro para proceso de transacciones
	private HtmlDialogWindow dwCargando;
	
	private HtmlDialogWindow dgwDetalleContado;
	private HtmlGridView gvHfacturasContado;
	private List lstHfacturasContado = null;
	private GridView gvDfacturasContado;
	private List lstDfacturasContado = null;
	private UIInput cmbBusqueda;
	private List lstBusqueda = null;
	private UIInput txtParametro;
	private HtmlOutputText txtMensaje;
	private String txtFechaFactura = "";
	private String txtNofactura = "";
	private String txtCliente = "";
	private String txtCodigoCliente = "";
	private String txtUnineg = "";
	private double txtSubtotal = 0.0;
	private double txtIva;
	private String txtTotal = "";
	private String sMensaje = "";
	private String txtVendedorCont;
	private String lblVendedorCont;
	private String lblTasaDetalle;
	private String txtTasaDetalle;
	private HtmlOutputText lblMonedaDomesticaCon;
	private DialogWindow dgwDetalleEnReciboContado;
	private GridView gvDfacturasContadoRecibo;
	private HtmlDropDownList cmbMonedaDetalle;

	private List lstMonedasDetalle;
	private List lstMonedasDetalleReciboContado;
	private HtmlDropDownList cmbMonedaDetalleReciboContado;
	private DialogWindow dgwMensajeDetalleContado;
	private List selectedFacs = null;
	private GridView gvfacturasSelec;

	// filtros de facturas mostradas en el grid
	private List lstFiltroFacturas;
	private HtmlDropDownList cmbFiltroFacturas;
	private List lstFiltroMonedas;
	private HtmlDropDownList cmbFiltroMonedas;

	// validacion de factura
	private DialogWindow dwValidacionFactura;
	private HtmlOutputText lblValidaFactura;
	private HtmlGraphicImageEx imgWatermark;

	// recibo
	private DialogWindow dwRecibo;

	// datos del recibo
	private String fechaRecibo;
	private HtmlDateChooser txtFecham;
	private HtmlInputText txtNumRec;
	private HtmlOutputText lblNumRecm;
	private UIOutput lblNumrec2;
	private HtmlOutputText lblNumeroRecibo;
	private String lblNumrec = "Último Recibo: ";
	private HtmlOutputText lblCodigoCliente;
	private HtmlOutputText lblNombreCliente;
	private HtmlDropDownList cmbTiporecibo;
	private List lstTiporecibo;
	private HtmlOutputText lblTasaCambio;
	private HtmlOutputText lblMontoAplicar;
	private HtmlOutputText lblMontoRecibido;
	private HtmlOutputText txtMontoAplicar;
	private HtmlOutputText txtMontoRecibido;
	private HtmlOutputText lblTasaJDE;
	private String txtCodUnineg;

	// Metodos de pago
	private HtmlDropDownList cmbMetodosPago;
	private List lstMetodosPago;

	// Monedas
	private HtmlInputText txtMonto;
	private HtmlDropDownList cmbMoneda;
	private List lstMoneda;

	// Afiliado
	private List lstAfiliado;
	private HtmlDropDownList ddlAfiliado;
	private UIOutput lblAfiliado;

	// Bancos
	private List lstBanco;
	private HtmlDropDownList ddlBanco;
	private UIOutput lblBanco;

	// referencias
	private HtmlInputText txtReferencia1;
	private HtmlInputText txtReferencia2;
	private HtmlInputText txtReferencia3;
	private UIOutput lblReferencia1;
	private UIOutput lblReferencia2;
	private UIOutput lblReferencia3;

	// Dialogs Window
	private DialogWindow dwProcesa;
	private DialogWindow dwImprime;

	// validacion
	private UIOutput lblMensajeValidacion;
	private String strMensajeValidacion;
	// solicitud
	private DialogWindow dwSolicitud;
	private UIOutput lblMensajeAutorizacion;
	private List lstAutoriza;
	private HtmlInputText txtReferencia;
	private HtmlDateChooser txtFecha;
	private HtmlInputTextarea txtObs;
	private HtmlDropDownList cmbAutoriza;

	// Grid de metodos de pago
	private HtmlGridView metodosGrid = null;
	private ArrayList<MetodosPago> selectedMet = null;

	// cambio
	private HtmlOutputText lblCambio;
	private HtmlOutputText txtCambio;
	private HtmlInputText txtCambioForaneo;
	private HtmlOutputText lblCambioDomestico;
	private HtmlOutputText txtCambioDomestico;
	private HtmlOutputText lblPendienteDomestico;
	private HtmlOutputText txtPendienteDomestico;
	private HtmlLink lnkCambio;

	// cancelar recibo
	private DialogWindow dwCancelar;
	private HtmlInputTextarea txtConcepto;
	private HtmlCheckBox chkImprimir;
	private boolean isSelected;
	private HtmlOutputText lblResultadoRec;

	// recibo de devolucion de contado
	private HtmlDialogWindow dwDevolucion;

	// factura original
	private HtmlOutputText fechaReciboDev;
	private HtmlDateChooser txtFechaManualDev;
	private HtmlOutputText txtNumeroReciboDev;
	private HtmlOutputText lblNumeroReciboManualDev;
	private HtmlInputText txtNumeroReciboManulDev;
	private HtmlOutputText lblCodigoClienteDev;
	private HtmlOutputText lblNombreClienteDev;
	private HtmlDropDownList cmbTiporeciboDev;
	private HtmlGridView gvDetalleReciboOriginal;
	private List lstDetalleReciboOriginal;

	// factura de devolucion
	private HtmlGridView gvDetalleReciboFactDev;
	private List lstDetalleReciboFactDev;
	private HtmlDropDownList cmbMetodosPagoFacDev;
	private HtmlInputText txtMontoFacDev;
	private HtmlDropDownList cmbMonedaFacDev;

	// private List lstTiporeciboDev;
	private HtmlInputTextarea txtConceptoDev;
	private HtmlOutputText lblMontoAplicarDev;
	private HtmlOutputText txtMontoAplicarDev;
	private HtmlOutputText lblMontoProcesado;
	private HtmlOutputText txtMontoProcesado;
	private HtmlOutputText lblFaltante;
	private HtmlOutputText txtFaltante;

	// info de devolucion
	private HtmlOutputText lblNoFactDev;
	private HtmlOutputText lblTipoDocFactDev;
	private HtmlOutputText lblMonedaFactDev;
	private HtmlOutputText lblMontoFactDev;
	private HtmlOutputText lblTasaCambioDev;
	private HtmlOutputText lblTasaJDEDev;

	// info de factura original
	private HtmlOutputText lblNodoco;
	private HtmlOutputText lblTipodoco;
	private HtmlOutputText lblMonedaOdev;
	private HtmlOutputText lblMontoOdev;
	private HtmlOutputText lblFechadev;
	private HtmlOutputText lblNoReciboOdev;
	private HtmlOutputText lblHoraReciboDev;

	// Devoluciones.
	private HtmlDialogWindow dwCancelarDev,dwProcesaDevolucion,dwConfirmaEmisionCheque;
	private HtmlCheckBox chkImprimirDevolucion;
	private List lstMonedaDev;
	private List lstMetodosPagoDev;
	private HtmlOutputText lblCodsucDev;
	private HtmlOutputText lblCoduninegDev;
	private HtmlOutputText lblMsgValidaSolecheque;
	
	//Crear Nota de Creditog
	private HtmlDialogWindow dwCrearNotaCredito;
	private String msgCodigoCliente;
	private HtmlInputText txtCodCli;
	private String strInfoCliente;
	
	//--- Traslado de Facturas.
	private HtmlDialogWindow dwCajasParaTraslado,dwFacturasTraslado, dwMostrarTraslado;
	private HtmlDropDownList ddlFiltroCajaEnvio,ddlFiltroFacturaTras,ddlFiltroCajasTras;
	private List lstFiltroCajaEnvio,lstCajasDisponibleEnvio, lstFiltroFacturaTras;
	private List lstFiltroCajasTras, lstFacturasParaTraslado, lstFacturasTrasladadas;
	private HtmlInputText txtParamCajaEnvio,txtParamFiltrofac;
	private HtmlGridView gvCajasDisponibleEnvio,gvFacturasParaTraslado, gvFacturasTrasladadas;
	private HtmlOutputText lblValidaEnvioFac,lblValidaTraerFac;

	//------ Pago con tarjeta de crédito con voucher manuales.
	private HtmlCheckBox chkVoucherManual;
	private HtmlOutputText lbletVouchermanual;
	private HtmlOutputText lblEtTrasladoPOS;
	private HtmlCheckBox chkTrasladarPOS; 
	
	//asignar Factura a RM
	private HtmlGridView gvClienteFacsRM;
	private List selectedFacsRM;

	//&& ====== Manejo de sobrantes en pagos distintos de Efectivo
	private double dMaximoSobrante=600;
	
	//&& ====== Confirmacion de borrar pago.
	private HtmlDialogWindow dwBorrarPago;
	private HtmlOutputText lblMontoEquiv;
	private HtmlOutputText lblDevMontoEquiv;
	
	//&& ===== valores globales para pagar contado.
	private String msgValidaSockPos = new String("");
	private boolean bExisteFichaCambio = false;
	private List<MetodosPago> lstPagoFicha = null;
	private int NoFichaCV = 0;
	
	private HtmlDialogWindow dwIngresarDatosDonacion;
	private HtmlGridView gvDonacionesRecibidas;
	private List<DncIngresoDonacion>lstDonacionesRecibidas;
	private HtmlOutputText lblTotalMontoDonacion;
	private HtmlOutputText lblTotalMontoDisponible;
	private HtmlOutputText lblFormaDePagoCliente;
	private List<SelectItem> lstBeneficiarios ;
	private HtmlDropDownList ddlDnc_Beneficiario;
	private HtmlInputText txtdnc_montodonacion;
	private HtmlOutputText msgValidaIngresoDonacion;
	
	//-------- Objetos para mostrar información donaciones aplicadas al recibo.
	private HtmlJspPanel pnlSeccionDonaciones ;
	private HtmlGridView gvDetalleDonaciones;
	private List<DncDonacion>lstDetalleDonaciones;
	private HtmlOutputText lblNoBatchDonacion; 
	private HtmlOutputText lblNoDocsJdeDonacion;
	
	private HtmlOutputText lblMontosTotalDonaciones ;
	private HtmlOutputText lblEtTotalDonaciones ;
	
	private List<SelectItem>lstMarcasDeTarjetas ;
	private HtmlDropDownList ddlTipoMarcasTarjetas;
	private HtmlOutputText lblMarcaTarjeta;
	private Object ref2;
	
	//Valores de insercion de documentos en JDE
	String[] valoresJdeIns = (String[]) m.get("valoresJDEInsContado");
	//String[] valoresJdeInsDev = (String[]) m.get("valoresJDEInsDevolucionContado");
	String[] valoresJdeNumeracion = (String[]) m.get("valoresJDENumeracionIns");
	String[] valoresJDEInsCredito = (String[]) m.get("valoresJDEInsCredito");
	//String[] valoresJDEInsFinanciamiento = (String[]) m.get("valoresJDEInsFinanciamiento");
	
/*******************************************************************************************************/	
	
	
	public void procesarDonacionesIngresadas(ActionEvent ev){
		String msg = "";
		try {
			
			if( CodeUtil.getFromSessionMap("fdc_lstDonacionesRecibidas") == null ){
				msg = "Ingrese al menos una donación para el proceso" ;
				return;
			}
			BigDecimal montodonado = new BigDecimal(lblTotalMontoDonacion.getValue().toString().trim().replace(",", ""));
			if(montodonado.compareTo(BigDecimal.ZERO) == 0 ){
				msg = "El monto de donaciónd debe ser mayor a cero" ;
				return;
			}
			CodeUtil.putInSessionMap("fdc_MontoTotalEnDonacion", montodonado);
			
			dwIngresarDatosDonacion.setWindowState("hidden");
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}finally{
			msgValidaIngresoDonacion.setValue(msg);
			CodeUtil.refreshIgObjects(new Object[]{msgValidaIngresoDonacion}) ;
		}
	}
	
	
	
	public void cerrarVentanaDonacion(ActionEvent ev) {
		m.remove( "fdc_MontoTotalEnDonacion" ) ;
		m.remove("fdc_lstDonacionesRecibida");
		dwIngresarDatosDonacion.setWindowState("hidden");
	}
	
	@SuppressWarnings("unchecked")
	public void agregarMontoDonacion(ActionEvent ev){
		String msg = "" ;
		try {
			
			List<Hfactura> lstFacturasSelected = (List<Hfactura>) m.get("facturasSelected");
			
			final String beneficiario = ddlDnc_Beneficiario.getValue().toString();
			String strMontoDonacion = txtdnc_montodonacion.getValue().toString();
			String moneda = cmbMoneda.getValue().toString();
			String codformapago = cmbMetodosPago.getValue().toString(); 
			String metodopago = codformapago + ", " + lblFormaDePagoCliente.getValue().toString();
			String codcomp = lstFacturasSelected.get(0).getCodcomp().trim();
			
			if(!strMontoDonacion.trim().matches(PropertiesSystem.REGEXP_AMOUNT)){
				msg = "Monto no válido";
				return;
			}
			
			BigDecimal montoDonacion = new BigDecimal(strMontoDonacion);
			BigDecimal montodonado = new BigDecimal(lblTotalMontoDonacion.getValue().toString().trim().replace(",", ""));
			BigDecimal montodisponible = new BigDecimal(lblTotalMontoDisponible.getValue().toString().trim().replace(",", ""));
			
			montodonado = montodonado.add( montoDonacion ) ;
			montodisponible = montodisponible.subtract(montoDonacion);
			
			if(montodisponible.compareTo(BigDecimal.ZERO) == -1){
				msg = "El Total de donación excede el monto disponible";
				return;
			}
			
			lblTotalMontoDonacion.setValue(String.format("%1$,.2f", montodonado ) );
			lblTotalMontoDisponible.setValue(String.format("%1$,.2f", montodisponible ) );
			
			lstBeneficiarios = (ArrayList<SelectItem>)CodeUtil.getFromSessionMap("fdc_lstbeneficiarios");
			SelectItem si = (SelectItem) CollectionUtils.find(lstBeneficiarios,
				new Predicate() {
					public boolean evaluate(Object o) {
						return ((SelectItem) o).getValue().toString().compareTo(beneficiario) == 0;
					}
				});
			
			lstDonacionesRecibidas = ( CodeUtil.getFromSessionMap("fdc_lstDonacionesRecibidas") == null )?
					new ArrayList<DncIngresoDonacion>():
					(ArrayList<DncIngresoDonacion>)CodeUtil.getFromSessionMap("fdc_lstDonacionesRecibidas");
					
			DncIngresoDonacion	dncExist = (DncIngresoDonacion)	 
			 CollectionUtils.find(lstDonacionesRecibidas, new Predicate(){
				public boolean evaluate(Object o) {
					return ((DncIngresoDonacion)o).getIdbenficiario() == 
							Integer.parseInt(beneficiario.split("@")[0]) ;
				}
			 });
			
			String nombrecliente =  lstFacturasSelected.get(0).getNomcli().trim().toLowerCase() ;
			int codigocliente = lstFacturasSelected.get(0).getCodcli();
			int codigocajero = ((Vautoriz[])m.get("sevAut"))[0].getId().getCodreg();
			int caid = ( (List<Vf55ca01>) CodeUtil.getFromSessionMap( "lstCajas" ) ).get(0).getId().getCaid();
			
			String codigomarcatarjeta = "";
			String marcatarjeta = "";
			if(codformapago.compareTo(MetodosPagoCtrl.TARJETA) == 0){
				codigomarcatarjeta = ddlTipoMarcasTarjetas.getValue().toString().split("@")[0];
				marcatarjeta = ddlTipoMarcasTarjetas.getValue().toString().split("@")[1];
			}
			
			if(dncExist == null){
				DncIngresoDonacion dnc =  new DncIngresoDonacion(
					Integer.parseInt( beneficiario.split("@")[0]),
					Integer.parseInt( beneficiario.split("@")[1]),
					si.getDescription().split("<>")[0],
					si.getLabel().toUpperCase(),
					montoDonacion, moneda, metodopago, "", "", 0, 0,
					codigocliente, nombrecliente, codigocajero, codcomp , 
					caid, codformapago, codigomarcatarjeta, marcatarjeta  );
				
				lstDonacionesRecibidas.add(dnc) ;
			}else{
				dncExist.setMontorecibido( dncExist.getMontorecibido().add(montoDonacion) ) ;
			}
			
			CodeUtil.putInSessionMap("fdc_lstDonacionesRecibidas", lstDonacionesRecibidas) ;
			gvDonacionesRecibidas.dataBind();
			
			CodeUtil.refreshIgObjects(new Object[]{lblTotalMontoDonacion, lblTotalMontoDisponible}) ;
			
		} catch (Exception e) {
			msg = "Donación no pudo ser aplicada";
			e.printStackTrace(); 
		}finally{
		
			msgValidaIngresoDonacion.setValue(msg);
			CodeUtil.refreshIgObjects(new Object[]{msgValidaIngresoDonacion}) ;
			
		}
	}
	
	public void mostrarVentanaDonaciones(ActionEvent ev) {
		boolean valido = true;
		String msg = "";
		
		try {
			
			final String metodopago = cmbMetodosPago.getValue().toString();
			
			if(metodopago.compareTo("MP") == 0){
				msg = "Seleccione metodo de pago";
				return;
			} 
			String sMonto  = txtMonto.getValue().toString();
			if ( !sMonto.trim().matches(PropertiesSystem.REGEXP_AMOUNT) ){
				msg = "El monto ingresado no es válido";
				return;
			}
			final String moneda = cmbMoneda.getValue().toString();
			
			@SuppressWarnings("unchecked")
			List<Vf55ca012>metodospagoconfig = (ArrayList<Vf55ca012>)CodeUtil.getFromSessionMap("fdc_MetodosPagoConfigurados");
			if(metodospagoconfig == null || metodospagoconfig.isEmpty()){
				msg = "No se encontraron formas de pago configurados para donaciones";
				return;
			}
			
			Vf55ca012 mp = (Vf55ca012)CollectionUtils.find(metodospagoconfig, new Predicate(){
				public boolean evaluate(Object o) {
					Vf55ca012 vf = (Vf55ca012) o;
					return String.valueOf(vf.getId().getC2ryin()).compareTo(metodopago) == 0 
						   && vf.getId().getC2crcd().compareTo(moneda) == 0;
				}
			});
			
			if(mp == null || mp.getId().getC2acpdn() == 0){
				msg = "La forma de pago no acepta donaciones";
				return;
			}
			
			msgValidaIngresoDonacion.setValue("");
			
			lstDonacionesRecibidas = new ArrayList<DncIngresoDonacion>();
			CodeUtil.putInSessionMap("fdc_lstDonacionesRecibidas", lstDonacionesRecibidas);
			gvDonacionesRecibidas.dataBind();
			
			txtdnc_montodonacion.setValue("");
			lblTotalMontoDonacion.setValue("0.00");
			lblTotalMontoDisponible.setValue(String.format("%1$,.2f", new BigDecimal(sMonto)));
			lblFormaDePagoCliente.setValue(mp.getId().getMpago().toLowerCase());
			
		} catch (Exception e) {
			msg=" Error al cargar interfaz para donaciones " ;
			e.printStackTrace(); 
		}finally{
			
			valido = msg.isEmpty();
			
			if(valido)
				dwIngresarDatosDonacion.setWindowState("normal");
			else{
				dwProcesa.setWindowState("normal");
				lblMensajeValidacion.setValue(msg);
			}
		}
	}
	
/*******************************************************************************************************/
	public void mostrarBorrarPago(ActionEvent ev){
		try {
			RowItem ri = (RowItem) ev.getComponent().getParent().getParent();
			MetodosPago mPago = (MetodosPago) DataRepeater.getDataRow(ri);
			m.put("metodopagoborrar", mPago);
		} catch (Exception e) {
			e.printStackTrace();
		}
		dwBorrarPago.setWindowState("normal");
	}
	public void cerrarBorrarPago(ActionEvent ev){
		dwBorrarPago.setWindowState("hidden");
		m.remove("metodopagoborrar");
	}
	
/*********************************************************************************************************/
	@SuppressWarnings("unchecked")
	public void anularRecibo(int iNumrec,int iCaid,String sCodsuc, String sCodcomp,String sCodUsera, int iNoFCV, int iCodcli){
		ReciboCtrl recCtrl = new ReciboCtrl();
		List lstReciboFac = null;
		List<Recibojde> lstRecibojde = null;
		Recibofac rf = null;
		Recibojde rj = null;
		Connection cn;
		
		try{
		
			cn = As400Connection.getJNDIConnection("DSMCAJA2");
			cn.setAutoCommit(false);
			
			lstRecibojde = recCtrl.getEnlaceReciboJDE(iCaid, sCodsuc,sCodcomp,iNumrec,valoresJdeIns[0]);
			
			recCtrl.actualizarEstadoRecibo(iNumrec,iCaid,sCodsuc,sCodcomp, sCodUsera, "No se completo pago con SP",valoresJdeIns[0]);
			
			//borrar enlace entre recibo y factura
			lstReciboFac = recCtrl.leerFacturasxRecibo(iCaid, sCodcomp,iNumrec,valoresJdeIns[0], iCodcli);
			for(int j = 0;j < lstReciboFac.size(); j ++){
				rf =  (Recibofac)lstReciboFac.get(j);
				rf.setEstado("A");
				recCtrl.actualizarEnlaceReciboFac(null,null,rf);
			}
			
			//borrar asiento de diario
			for (int k = 0; k < lstRecibojde.size(); k++){
				rj = (Recibojde)lstRecibojde.get(k);
				recCtrl.borrarAsientodeDiario(null, rj.getId().getRecjde(), rj.getId().getNobatch());
			}
			//borrar batch
			recCtrl.borrarBatch(null, rj.getId().getNobatch(),"G");
				
			//---- Verificar que la factura sea traslado, si lo es, cambiar estado de traslado a 'E'
			TrasladoCtrl tcCtrl = new TrasladoCtrl();
			rf = null;
			Trasladofac tf = null;
			for (Iterator iter = lstReciboFac.iterator(); iter.hasNext();) {
				rf = (Recibofac) iter.next();
				tf = tcCtrl.buscarTrasladofac(rf.getId().getNumfac(),
							rf.getId().getTipofactura(),rf.getId().getCodsuc(),
							rf.getId().getCodcomp(),rf.getId().getCodunineg(),
							iCaid,"P", rf.getId().getCodcli(), rf.getId().getFecha() );
				if(tf!=null){
					tcCtrl.actualizarEstadoTraslado(null, tf, "E","P");			
				}
			}
			
			//&& ============ Eliminar la ficha FCV
			if(iNoFCV > 0 ){
				Recibo ficha = recCtrl.obtenerFichaCV(iNoFCV, iCaid, sCodcomp, sCodsuc);
				lstRecibojde = recCtrl.getEnlaceReciboJDE(iCaid, sCodsuc, 
									ficha.getId().getCodcomp(),
									ficha.getId().getNumrec(),
									ficha.getId().getTiporec());
				
				recCtrl.actualizarEstadoRecibo(null, null, 
									ficha.getId().getNumrec(),
									ficha.getId().getCaid(),
									ficha.getId().getCodsuc(),
									ficha.getId().getCodcomp(), 
									sCodUsera, 
									"Error de aplicacion Socket POS",
									ficha.getId().getTiporec());
			
				Recibojde recibojde = null;
				for(Recibojde rjd: lstRecibojde){
					recCtrl.borrarAsientodeDiario(cn, rjd.getId().getRecjde(), rjd.getId().getNobatch());
					recibojde = rjd;
				}
				if(recibojde != null)
					recCtrl.borrarBatch(cn, recibojde.getId().getNobatch(),valoresJdeIns[8]);
			} 
			//&& ======= Borrar los sobrantes.
			Recibojde recibojde = null;
			lstRecibojde = recCtrl.getEnlaceReciboJDE(iCaid, sCodsuc, sCodcomp, iNumrec, "SBR");
			
			for (Recibojde rjd : lstRecibojde) {
				recCtrl.borrarAsientodeDiario(cn, rjd.getId().getRecjde(), rjd.getId().getNobatch());
				recibojde = rjd;
			}
			if(recibojde != null)
				recCtrl.borrarBatch(cn, recibojde.getId().getNobatch(),valoresJdeIns[8]);
			
			cn.commit();
			
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
//			try {cn.close();} catch (Exception e) {}
		}
	}
/******************************************************************************************/
/** Método: Guardar asientos de diario para sobrantes de caja.
 *	Fecha:  09/12/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 * @throws Exception 
 */
	@SuppressWarnings("unchecked")
	public boolean registarAsientosxSobrantes(Session s,Transaction tx, MetodosPago mpago,
												  Vautoriz vaut, Vf55ca01 f55, int iNumrec, String sCodcomp,
											  String sCodunineg,double dSobrante,String sMonedaBase,Hfactura Factura) throws Exception{
		boolean bHecho = true;
		Divisas dv = new Divisas();
		String[] sCuentaCaja = null;
		String  sCoCuentaDI = "";
		String sCuentaDI="";
		int iNobatchNodoc[]=null, iMonto = 0,iMontoDom=0;
		String sConcepto = "", sMensajeError = "", sTipoCliente="";
		String sCtaOb="",sCtaSub="",sAsientoSuc="";
		Vf0901 vCtaDI = null;
		ReciboCtrl rcCtrl = new ReciboCtrl();
		EmpleadoCtrl emCtrl = new EmpleadoCtrl();
		Date dtFecha = new Date();
		double dTasaJDE = 0;
		String sTipo="DIF/CAMB";
		
		try {
			
			sCodunineg = sCodunineg.trim();
			sConcepto = "Dif/Camb en RcCo:" +iNumrec+ " Ca:"+f55.getId().getCaid();
			sTipoCliente = emCtrl.determinarTipoCliente(f55.getId().getCaan8());	
			
			String msgLogs = sConcepto;
			
			
			//--- Validar la cuenta de caja para el método de pago con sobrante.
			sCuentaCaja = dv.obtenerCuentaCaja(f55.getId().getCaid(),sCodcomp, mpago.getMetodo(), mpago.getMoneda(),s,tx,null,null);
			if(sCuentaCaja==null){
				sMensajeError="No se ha podido leer la cuenta de caja para el método: " +mpago.getMetododescrip().trim();
				m.put("sMsgErrorSobrante", sMensajeError);
				throw new Exception(sMensajeError);
			}
			
			//&& ==== Validar la cuenta a utilizar, en dependencia del tipo de transaccion.
			//&& ==== UN.66000.01: Diferencial Cambiario, UN.65100.10: Sobrante de Caja.
			String[] fcvCuentaPerdia = DocumuentosTransaccionales.obtenerCuentasFCVPerdida(f55.getId().getCaco()).split(",",-1);
			sCtaOb = fcvCuentaPerdia[0];
			sCtaSub= fcvCuentaPerdia[1];
			
		
				MetodosPago mpSobrante = (MetodosPago)m.get("sco_MpagoSobrante");
				if(Factura.getMoneda().equals(mpSobrante.getMoneda())){
					sTipo = "SBRTE";
					sConcepto = "Sobrante en RcCo:" +iNumrec+ " Ca:"+f55.getId().getCaid();
					String[] fcvCuentaGanacia = DocumuentosTransaccionales.obtenerCuentasFCVGanancia(f55.getId().getCaco()).split(",",-1);
					sCtaOb = fcvCuentaGanacia[0];
					sCtaSub= fcvCuentaGanacia[1];
					
				}
//				}
			vCtaDI  = dv.validarCuentaF0901(sCodunineg,sCtaOb,sCtaSub);
			if(vCtaDI==null){
				sMensajeError = "No se ha podido obtener la cuenta Diversos Ingresos: '"+sCodunineg+"."+sCtaOb+"'."+sCtaSub;
				m.put("sMsgErrorSobrante", sMensajeError);
				throw new Exception(sMensajeError);
			}else{
				sCoCuentaDI =  vCtaDI.getId().getGmco().trim(); //sCodunineg;
				sCuentaDI = vCtaDI.getCuenta().trim();//sCodunineg+"."+sCtaOb+ (sCtaSub==" "?"":".") +sCtaSub;				
			}
			sAsientoSuc  = sCuentaCaja[2];
			
			int iNoBatch = Divisas.numeroSiguienteJdeE1(  );
			int iNoDocumentoFor = Divisas.numeroSiguienteJdeE1Custom(valoresJdeNumeracion[8],valoresJdeNumeracion[9] );
			iNobatchNodoc = new int[]{iNoBatch, iNoDocumentoFor};
			
			iMonto = Divisas.pasarAentero(dSobrante);
		
			//---- Guardar el batch.			
			bHecho = rcCtrl.registrarBatchA92Custom(s, dtFecha, valoresJdeIns[8], iNoBatch, iMonto, vaut.getId().getLogin(), 1, "RCONTADO", valoresJdeIns[9] );
			
			
			if(bHecho){
				
				//---- Registro en córdobas.
				if(mpago.getMoneda().equals(sMonedaBase)){
				
					bHecho = rcCtrl.registrarAsientoDiarioLogs(s,  msgLogs, dtFecha,  sAsientoSuc, valoresJdeIns[1], iNobatchNodoc[1], 1.0,
								iNobatchNodoc[0], sCuentaCaja[0], sCuentaCaja[1], 
								sCuentaCaja[3], sCuentaCaja[4], sCuentaCaja[5],
								valoresJdeIns[5], mpago.getMoneda(), iMonto, 
								sConcepto, vaut.getId().getLogin(), vaut.getId().getCodapp(), 
								BigDecimal.ZERO, sTipoCliente,"DBTO X "+sTipo+" PAGO CTA CA: "+mpago.getMetodo()+" ",
								sCuentaCaja[2], "", "", mpago.getMoneda(),sCuentaCaja[2],valoresJdeIns[7], 0);
					
					if(bHecho){
						bHecho = rcCtrl.registrarAsientoDiarioLogs(s,  msgLogs, dtFecha, sAsientoSuc, valoresJdeIns[1], iNobatchNodoc[1], 2.0,
									iNobatchNodoc[0], sCuentaDI,	vCtaDI.getId().getGmaid(), 
									vCtaDI.getId().getGmmcu().trim(), vCtaDI.getId().getGmobj().trim(), 
									vCtaDI.getId().getGmsub().trim(), valoresJdeIns[5],	mpago.getMoneda(),  iMonto*-1,
									sConcepto, vaut.getId().getLogin(), vaut.getId().getCodapp(), 
									BigDecimal.ZERO, sTipoCliente,"CRDTO X "+sTipo+" CTA ",
									sCuentaCaja[2], "", "",mpago.getMoneda(),sCoCuentaDI,valoresJdeIns[7], 0);
					
						if(!bHecho){
							sMensajeError = "No se ha podido registrar  línea 2.0 de asientos para registro de Sobrante de Pagos ";
						}
					}else{
						sMensajeError = "No se ha podido registrar  línea 1.0 de asientos para registro de Sobrante de Pagos ";
					}
				}
				//--- Registro en dólares.
				else{
					TasaCambioCtrl tcCtrl = new  TasaCambioCtrl();
					dTasaJDE = tcCtrl.obtenerTasaJDExFecha(mpago.getMoneda(), "COR",dtFecha, null, null);
					
					if( dv.roundDouble4(dTasaJDE) == 0  ){
						sMensajeError = " No se encontro la configuración de tasa para registrar asientos de diario";
						m.put("sMsgErrorSobrante", sMensajeError);
						throw new Exception(sMensajeError);
					}
					
					dTasaJDE = dv.roundDouble4(dTasaJDE);
					iMontoDom = (int)(dv.roundDouble4((dSobrante * dTasaJDE)*100));
					
					bHecho = rcCtrl.registrarAsientoDiarioLogs(s,  msgLogs, dtFecha, sAsientoSuc, valoresJdeIns[1], iNobatchNodoc[1], 1.0,
									iNobatchNodoc[0], sCuentaCaja[0], sCuentaCaja[1], 
									sCuentaCaja[3], sCuentaCaja[4], sCuentaCaja[5],
									valoresJdeIns[5], mpago.getMoneda(), iMontoDom, 
									sConcepto, vaut.getId().getLogin(), vaut.getId().getCodapp(), 
									new BigDecimal(dTasaJDE), sTipoCliente,"DBTO CTA CA:"+mpago.getMetodo()+" "+sTipo+" PAGO",
									sCuentaCaja[2], "", "", mpago.getMoneda(),sCuentaCaja[2],valoresJdeIns[4],  iMonto );
					if(bHecho){
						bHecho = rcCtrl.registrarAsientoDiarioLogs(s,  msgLogs, dtFecha, sAsientoSuc, valoresJdeIns[1], iNobatchNodoc[1], 1.0,
										iNobatchNodoc[0], sCuentaCaja[0], sCuentaCaja[1], 
										sCuentaCaja[3], sCuentaCaja[4], sCuentaCaja[5],
										valoresJdeIns[2], mpago.getMoneda(), iMonto, 
										sConcepto, vaut.getId().getLogin(), vaut.getId().getCodapp(), 
										BigDecimal.ZERO, sTipoCliente,"DBTO CTA CA:"+mpago.getMetodo()+" "+sTipo+" PAGO",
										sCuentaCaja[2], "", "",mpago.getMoneda(),sCuentaCaja[2],valoresJdeIns[4], 0 );
						
						if(bHecho){
							bHecho = rcCtrl.registrarAsientoDiarioLogs(s,  msgLogs, dtFecha,  sAsientoSuc, valoresJdeIns[1], iNobatchNodoc[1], 2.0,
											iNobatchNodoc[0], sCuentaDI,	vCtaDI.getId().getGmaid(), 
											vCtaDI.getId().getGmmcu().trim(), vCtaDI.getId().getGmobj().trim(), 
											vCtaDI.getId().getGmsub().trim(), valoresJdeIns[5],	mpago.getMoneda(), (iMontoDom*-1), 
											sConcepto, vaut.getId().getLogin(), vaut.getId().getCodapp(), 
											new BigDecimal(dTasaJDE), sTipoCliente,"CRDTO CTA  X "+sTipo+" PAGO",
											sCuentaCaja[2], "", "",mpago.getMoneda(),sCoCuentaDI,valoresJdeIns[4],  (iMonto*-1));
							
							if(bHecho){
								bHecho = rcCtrl.registrarAsientoDiarioLogs(s,  msgLogs, dtFecha, sAsientoSuc, valoresJdeIns[1], iNobatchNodoc[1], 2.0,
													iNobatchNodoc[0], sCuentaDI,	vCtaDI.getId().getGmaid(), 
													vCtaDI.getId().getGmmcu().trim(), vCtaDI.getId().getGmobj().trim(), 
													vCtaDI.getId().getGmsub().trim(), valoresJdeIns[2],	mpago.getMoneda(), (iMonto*-1), 
													sConcepto, vaut.getId().getLogin(), vaut.getId().getCodapp(), 
													BigDecimal.ZERO, sTipoCliente,"CRDTO CTA  X "+sTipo+" PAGO",
													sCuentaCaja[2], "", "", mpago.getMoneda(),sCoCuentaDI,valoresJdeIns[4], 0);
								if(!bHecho)
									sMensajeError = "No se ha podido registrar  línea 2.0 cA de asientos para registro de Excedente de Pagos ";
							}else{
								sMensajeError = "No se ha podido registrar  línea 2.0 AA de asientos para registro de Excedente de Pagos ";
							}
						}else{
							sMensajeError = "No se ha podido registrar  línea 1.0 CA de asientos para registro de Excedente de Pagos ";
						}
					}else{
						sMensajeError = "No se ha podido registrar  línea 1.0 AA de asientos para registro de Excedente de Pagos ";
					}
				}
				//---- Guardar el registro del sobrante en recibojde.	
				
				List<String> numerosRecibosJde = new ArrayList<String>();
				numerosRecibosJde.add(String.valueOf( iNobatchNodoc[1]));

				bHecho =  com.casapellas.controles.ReciboCtrl.crearRegistroReciboCajaJde( f55.getId().getCaid(), iNumrec, sCodcomp, f55.getId().getCaco(), "SBR", "A", iNobatchNodoc[0], numerosRecibosJde);
				
				if(!bHecho){
					sMensajeError = "No se ha podido guardar registro de Excedente en RECIBOJDE ";
				}
				
			}else{
				sMensajeError = "No se ha podido registrar  batch  para registro de Excedente de Pagos ";
			}
			
			if(!bHecho)
				m.put("sMsgErrorSobrante", sMensajeError);
			
		} catch (Exception error) {
			LogCajaService.CreateLog("registrarAsientoDiarioLogs", "ERR", error.getMessage());
			bHecho = false;
			m.put("sMsgErrorSobrante", "Error de sistema al intentar registrar asiento por sobrantes de pago");
			error.printStackTrace();
			throw new Exception(error.getMessage());
		} 
		return bHecho; 
	}
/**************************************************************************************************/
	public void imprimirVoucherDev(Recibodet rd,String sTerminal,String TipoTrans, F55ca014[] f14){
		MetodosPago mPago = null;
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		String sDescripcion = "", sPrinter = "";
		try{
			//obtener descricpion de compania
			sDescripcion = cCtrl.obtenerCompaniaxCodigo(rd.getId().getCodcomp());	
			//sacar impresora por compania
			for(int a = 0; a < f14.length; a++){
				if(f14[a].getId().getC4rp01().trim().equals(rd.getId().getCodcomp().trim())){
					sPrinter =  f14[a].getId().getC4prt();
				}
			}		
						
			getP55ca090().setCIA("     " + sDescripcion);
			getP55ca090().setTERMINAL(sTerminal);
			getP55ca090().setDIGITO(rd.getId().getRefer3());
			getP55ca090().setREFERENC(rd.getId().getRefer4());
			getP55ca090().setAUTORIZ(rd.getRefer5());
			getP55ca090().setFECHA(rd.getRefer6());
			getP55ca090().setVARIOS("-"+ rd.getMonto() + ";" + TipoTrans + ";" + sPrinter.trim() + 
					";" + rd.getId().getRefer1() + ";" + rd.getId().getMoneda()+ ";" + rd.getNombre());
			getP55ca090().invoke();
					
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	/***********************************************************************************************************************/
	public boolean anularPagosSPDev(List lstDetalleRecibo,List lstDetalleDev){
		boolean bBorrado = true;
		String sTc = "";
		List lstResult = null;
		PosCtrl pCtrl = new PosCtrl();
		Recibodet rd = null;
		MetodosPago rDev = null;
		lstResult = null;
		String sMensaje1 = "";
		try{
			TermAfl tf = null;
			F55ca014[] f14 = (F55ca014[])m.get("cont_f55ca014");
			
			//leer la terminal del afiliado seleccionado
			for (int i = 0; i < lstDetalleRecibo.size(); i++){
				rd = (Recibodet)lstDetalleRecibo.get(i);
				
				if(i < lstDetalleDev.size() ){
					rDev = (MetodosPago)lstDetalleDev.get(i);
				}
				
				if(rd.getId().getMpago().equals(MetodosPagoCtrl.TARJETA) && rd.getVmanual().equals("2")){
					tf = pCtrl.getTerminalxAfiliado(rd.getId().getRefer1());
					if(tf != null){
						lstResult = pCtrl.anularTransaccionPOS(tf.getId().getTermId() + "",rd.getId().getRefer4(), rd.getRefer5(), rd.getRefer7());
						if(!lstResult.get(0).equals("00")){
							bBorrado = false;
							sTc = lstResult.get(0) + " " + lstResult.get(1);							
							break;
						}else{
							rDev.setReferencia5(rd.getRefer5());
							rDev.setReferencia6(rd.getRefer6());
							rDev.setReferencia7(rd.getRefer7());
							lstDetalleDev.set(i,rDev);							
							//imprimir voucher de anulacion
							imprimirVoucherDev(rd,tf.getId().getTermId(),"A", f14);
						}
					}else{
						bBorrado = false;
						strMensajeValidacion = sMensaje1 + "<br/> No se pudo anular la transaccion de tarjeta con el BANCO: " + sTc;
						lblMensajeValidacion.setValue(strMensajeValidacion);
					}
				}
			}
			 m.put("lstDetalleReciboFactDev",lstDetalleDev);
		}catch(Exception ex){
			bBorrado = false;
			ex.printStackTrace();
		}
		return bBorrado;
	}	
/***********************************************************************************************************************/
/***********************************************************************************************************************/
	public void imprimirVoucher(List lstMetodosPago,String sCompania,String TipoTrans, F55ca014[] f14){
		MetodosPago mPago = null;
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		String sDescripcion = "", sPrinter = "";
		Divisas d = new Divisas();
		try{
			
			//obtener descricpion de compania
			sDescripcion = cCtrl.obtenerCompaniaxCodigo(sCompania);	
			//sacar impresora por compania
			for(int a = 0; a < f14.length; a++){
				if(f14[a].getId().getC4rp01().trim().equals(sCompania.trim())){
					sPrinter =  f14[a].getId().getC4prt();
				}
			}
			//imprimir todos los voucher del cliente
			for(int i = 0; i < lstMetodosPago.size(); i ++){
				mPago = (MetodosPago)lstMetodosPago.get(i);
				if(mPago.getMetodo().equals(MetodosPagoCtrl.TARJETA) && mPago.getVmanual().equals("2")){
					getP55ca090().setCIA("     "+sDescripcion);
					getP55ca090().setTERMINAL(mPago.getTerminal());
					getP55ca090().setDIGITO(mPago.getReferencia3());
					getP55ca090().setREFERENC(mPago.getReferencia4());
					getP55ca090().setAUTORIZ(mPago.getReferencia5());
					getP55ca090().setFECHA(mPago.getReferencia6());
					getP55ca090().setVARIOS(d.ponerDosCifrasDec(mPago.getMontopos()) + ";" + TipoTrans + ";" + sPrinter.trim()+
							";" + mPago.getReferencia() + ";" + mPago.getMoneda()+ ";" + mPago.getNombre());
					getP55ca090().invoke();
				}
			}			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
/***********************************************************************************************************************/
	public boolean anularPagosSP(List lstMetodosPago){
		boolean bPago = true;
		MetodosPago rd = null;String sTc = "";
		List lstResult = null;
		PosCtrl p = new PosCtrl();
		String sTerminal = "";
		try{
			//leer la terminal del afiliado seleccionado
			for (int i = 0; i < lstMetodosPago.size(); i++){
				rd = (MetodosPago)lstMetodosPago.get(i);
				if(rd.getMetodo().equals(MetodosPagoCtrl.TARJETA)  && rd.getVmanual().equals("2")){
					lstResult = p.anularTransaccionPOS(rd.getTerminal(), rd.getReferencia4(), rd.getReferencia5(), rd.getReferencia7());
					if(!lstResult.get(0).equals("00")){
						bPago = false;
						sTc = lstResult.get(0) + " " + lstResult.get(1);
						//break;
					}else{
						//agregar la impresion del voucher de anulacion
					}
				}
			}
		}catch(Exception ex){
			bPago = false;
			ex.printStackTrace();
		}
		return bPago;
	}	
/***********************************************************************************************************************/	
	@SuppressWarnings({"unchecked"})
	public String validarPagoSocket(double dMontoAplicar,Hfactura hFac, 
			String ref1, String ref2, String ref3, int iCaid, String sMonedaPago ){
		
		strMensajeValidacion = new String("");
		String sMonto = new String("");
		double monto = 0;
		
		String iconMsg = "<br><img width=\"7\" src=\"/"
					+PropertiesSystem.CONTEXT_NAME
					+"/theme/icons/redCircle.jpg\" border=\"0\" /> ";
		
		try{	
			
			sMonto = txtMonto.getValue() == null ? ""
					: txtMonto.getValue().toString().trim();
			if(sMonto.compareTo("") == 0 || !sMonto.matches(PropertiesSystem.REGEXP_AMOUNT)){
				txtMonto.setStyleClass("frmInput2Error");
				return  strMensajeValidacion = "Monto Inválido";
			}
			
			monto = new Divisas().formatStringToDouble(sMonto); 
			if (monto > dMontoAplicar) {
					txtMonto.setStyleClass("frmInput2Error");
					strMensajeValidacion += iconMsg+ "El monto ingresado para" +
							" este método de pago debe ser menor o igual al monto a aplicar";
			}
			if (ddlAfiliado.getValue().toString().equals("01")) {
					ddlAfiliado.setStyleClass("frmInput2Error2");
					strMensajeValidacion += "Seleccione un afiliado<br>";
			}
			if (ref1.trim().compareTo("") == 0 ) {
					txtReferencia1.setStyleClass("frmInput2Error");
					strMensajeValidacion += iconMsg + " Identificación requerida";
			}
			if ( !ref1.trim().matches(PropertiesSystem.REGEXP_ALFANUMERIC) ) {
					strMensajeValidacion += iconMsg+ "El campo <b>Identificación<b/>" +
							" contiene caracteres inválidos";
					txtReferencia1.setStyleClass("frmInput2Error");
			}
			if (ref1.length() > 150) {
					strMensajeValidacion += iconMsg +" La cantidad de letras " +
							"del campo <b>Identificación<b/> es muy alta (lim. 150)<br>";
					txtReferencia1.setStyleClass("frmInput2Error");
			}
			
			boolean bMontoMaximo =  new CtrlPoliticas().validarMonto(
										iCaid, hFac.getCodcomp(),
					 					sMonedaPago, MetodosPagoCtrl.TARJETA, monto);
			if(!bMontoMaximo){
				lblMensajeAutorizacion.setValue("El monto ingresado no cumple " +
											"con las politicas de caja");
				dwSolicitud.setWindowState("normal");
				txtFecha.setValue(new Date());
				m.put("montoIsertando", monto);
				m.put("monto", monto);
				
				SmartRefreshManager.getCurrentInstance()
					.addSmartRefreshId(dwSolicitud.getClientId(
							FacesContext.getCurrentInstance()));
				
				return "El monto ingresado no cumple " +
						"con las politicas de caja";
			}
			
			if ( selectedMet != null && !selectedMet.isEmpty()) {
				
				BigDecimal bdTotalMpagos = BigDecimal.ZERO;
				for (MetodosPago mp : selectedMet) 
					bdTotalMpagos =  bdTotalMpagos.add(new BigDecimal(
									Double.toString(mp.getEquivalente())) ) ;
				
				String sAplicado  = txtMontoAplicar.getValue()
									.toString().replace(",", "");

				if(new BigDecimal(sAplicado).compareTo(bdTotalMpagos) != 1){
					txtMonto.setStyleClass("frmInput2Error");
					strMensajeValidacion += iconMsg + " El consolidado de " +
											"pagos excede el monto aplicar ";
				}
			}
				
			if(!chkIngresoManual.isChecked()){	
				
				String sTrack = track.getValue().toString();
				
				if(sTrack.equals("")){ 
						track.setStyleClass("frmInput2Error");
						strMensajeValidacion += iconMsg + "Datos de Tarjeta requeridos";
				}else{
					
					List<String> lstDatosTrack = Divisas.obtenerDatosTrack(sTrack);

					if (lstDatosTrack == null || lstDatosTrack.isEmpty()) {
						track.setValue("");
						track.setStyleClass("frmInput2Error");
						strMensajeValidacion += iconMsg
								+ "Error en lectura de tarjeta, favor intente de nuevo ";
					}
				}
			}else{
				ref2 = txtNoTarjeta.getValue().toString().trim();
				ref3 = txtFechaVenceT.getValue().toString().trim();
				
				if(ref2.trim().matches("^[0-9]{1,8}$")){
					txtNoTarjeta.setStyleClass("frmInput2Error");
					strMensajeValidacion += iconMsg + " Número de Tarjeta inválido";
				}
				if(ref3.trim().matches("^[0-9]{1,8}$")){
					txtFechaVenceT.setStyleClass("frmInput2Error");
					strMensajeValidacion += iconMsg + " Fecha de Vencimiento inválida";
				}
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
			strMensajeValidacion = "Error en validación para datos" +
					" de tarjeta, favor intente nuevamente ";
		}finally{
			if(strMensajeValidacion.compareTo("") != 0){
				lblMensajeValidacion.setValue(strMensajeValidacion);
				dwProcesa.setWindowState("normal");
			}
		}
		return strMensajeValidacion;
	}
	
	/*************************************************************************************************************************/	
	public boolean aplicarPagoSocketPos(List lstPagos, List lstDatosTrack,int iCaid,String sCodSuc,String sCodComp,int iNumrec){
		boolean bHecho = true,bPasa = false;
		MetodosPago mPago = null;
		Recibodet rd = null;
		Divisas d = new Divisas();
		PosCtrl p = new PosCtrl();ReciboCtrl rc = new ReciboCtrl();
		List<String> lstDatos  = new ArrayList<String>(), lstRespuesta = new ArrayList<String>();
		String sTerminal = "",sMonto = "", sNotarjeta = "", sFechavence = "";
		int j = 0;
		String nombre = "";
		try{
			//comprobar si hay pagos con tarjeta de credito
			for(int i = 0;i < lstPagos.size();i++){
				mPago = (MetodosPago)lstPagos.get(i);
				if(mPago.getMetodo().equals(MetodosPagoCtrl.TARJETA) && mPago.getVmanual().equals("2")){
					 rd = rc.leerPagoRecibo(iCaid,sCodSuc,sCodComp,iNumrec, valoresJdeIns[0],mPago.getMetodo(),
											mPago.getReferencia(),mPago.getReferencia2(),mPago.getReferencia3(),
											mPago.getReferencia4(),mPago.getMoneda());
					//leer terminal por afiliado caja
					//solicitar autorizacion a credomatic
					sMonto = d.roundDouble4(rd.getMonto().doubleValue()) + "";
					sTerminal = mPago.getTerminal();
					
					//automatica
					if(!mPago.getTrack().equals("")){
						lstDatos = (List)lstDatosTrack.get(j);
						lstRespuesta = p.realizarPago(sTerminal, sMonto, mPago.getTrack(), lstDatos);
						sNotarjeta = lstDatos.get(1);
						nombre = lstDatos.get(2);
						
						if(!lstDatos.get(3).trim().matches("^\\d+$"))
							nombre +=  " " + lstDatos.get(3).trim();
						
					}
					//manual	
					else{
						lstRespuesta = p.realizarPagoManual(sTerminal,sMonto,mPago.getReferencia4(),mPago.getReferencia5());
						sNotarjeta = mPago.getReferencia4();
						nombre = "-";
					}
					
					if(lstRespuesta!=null && !lstRespuesta.isEmpty()){
						if(lstRespuesta.get(0).equals("00") || lstRespuesta.get(0).equals("08")){//aprobada
							//poner datos de respuesta a metodo de pago para luego ser grabados
							sNotarjeta = (sNotarjeta).substring(sNotarjeta.length()-4, sNotarjeta.length());//4 ult. digitos de tarjeta
							mPago.setReferencia3(sNotarjeta);//ult 4 de tarjeta
							mPago.setReferencia4(lstRespuesta.get(4));//referencia
							mPago.setReferencia5(lstRespuesta.get(5));//autorizacion
							mPago.setReferencia6(p.formatFechaSocket(lstRespuesta.get(3), lstRespuesta.get(2)));//socket devuelve formato mmddyyyy y debe ser pasada a mmm dd,yy 09012008 = Sep 01, 08
							mPago.setReferencia7(lstRespuesta.get(6));//systraceno
							mPago.setNombre(nombre);
							lstDatos.add("true");
							lstDatosTrack.set(j,lstDatos);
							m.put("lstDatosTrack_Con",lstDatosTrack);
							lstPagos.set(i, mPago);
							j++;
						}else if (lstRespuesta.get(0).toString().equals("error")){
							bHecho = false;
							lblMensajeValidacion.setValue("Tarjeta No.: " + sNotarjeta + "; No pudo ser procesada!!!\n  Error: " + lstRespuesta.get(1) + "\n" +
															lstRespuesta.get(2)+"\n" + lstRespuesta.get(3));
							lstDatos.add("false");
							//lstDatosTrack.set(j,lstDatos);
							m.put("lstDatosTrack_Con",lstDatosTrack);
							j++;
							return false;
						}else{
							
							bHecho = false;
							String sMensaje = "No se puede aplicar cargo a tarjeta" + sNotarjeta ;
							
							if( lstRespuesta.get(1).trim().toLowerCase().contains("time out"))
								sMensaje += ": No se ha podido obtener conexión " +
										"con el servidor. Favor intente de nuevo";
							else
								sMensaje += "; No pudo ser procesada!!!\n  Error: " 
										 + lstRespuesta.get(1);
							
							lblMensajeValidacion.setValue(sMensaje);
							lstDatos.add("false");
							m.put("lstDatosTrack_Con",lstDatosTrack);
							j++;
							return false;
						}
						m.put("lstPagos",lstPagos);
					}else{//hubo algun error con la transferencia con credomatic
						lblMensajeValidacion.setValue("Error al aplicar el pago con tarjeta a credomatic!!!");
						return false;
					}
				}
			}
		}catch(Exception ex){
			 bHecho = false;
			 lblMensajeValidacion.setValue("Error en aplicarPagoSocketPos!!!" + ex);
			 ex.printStackTrace();
		}
		return bHecho;
	}
/*************************************************************************************************************************/
	public void setVoucherManual(ValueChangeEvent ev){
		try{
			if(chkVoucherManual.isChecked()){
				//quitar manuales
				lblNoTarjeta.setValue("");
				lblNoTarjeta.setStyle("display:none");
				txtNoTarjeta.setStyle("display:none");
				lblFechaVenceT.setValue("");
				lblFechaVenceT.setStyle("display:none");
				txtFechaVenceT.setStyle("display:none");
				//poner voucher
				lblReferencia2.setValue("Referencia:");
				txtReferencia2.setStyle("display:inline");
				
				lblTrack.setValue("");
				track.setStyle("display:none");
				
				chkIngresoManual.setStyle("display:none");
			}else{
				//quitar manuales
				lblNoTarjeta.setValue("");
				lblNoTarjeta.setStyle("display:none");
				txtNoTarjeta.setStyle("display:none");
				lblFechaVenceT.setValue("");
				lblFechaVenceT.setStyle("display:none");
				txtFechaVenceT.setStyle("display:none");
				//poner track
				lblReferencia2.setValue("");
				txtReferencia2.setStyle("display:none");
				
				lblTrack.setValue("Banda:");
				track.setStyle("display:inline");
				
				chkIngresoManual.setStyle("display:inline");
				chkIngresoManual.setChecked(false);
			}	
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

/*************************************************************************************************************************/	
	public void setIngresoManual(ValueChangeEvent ev){
		try{
			if(chkIngresoManual.isChecked()){
				//mostrar Manuales
				lblNoTarjeta.setValue("Tarjeta:");
				lblNoTarjeta.setStyle("display:inline");
				txtNoTarjeta.setStyle("display:inline");
				lblFechaVenceT.setValue("Vence:");
				lblFechaVenceT.setStyle("display:inline");
				txtFechaVenceT.setStyle("display:inline");
				//quitar track
				lblReferencia2.setValue("");
				txtReferencia2.setStyle("display:none");
				
				lblTrack.setValue("");
				track.setStyle("display:none");
			}else{
				//quitar manuales
				lblNoTarjeta.setValue("");
				lblNoTarjeta.setStyle("display:none");
				txtNoTarjeta.setStyle("display:none");
				lblFechaVenceT.setValue("");
				lblFechaVenceT.setStyle("display:none");
				txtFechaVenceT.setStyle("display:none");
				//poner track
				lblReferencia2.setValue("");
				txtReferencia2.setStyle("display:none");
				
				lblTrack.setValue("Banda:");
				track.setStyle("display:inline");
			}	
			
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
/***********************************************************************************************************/	
	public List getLstAfiliadosSocketPOS(int iCaid, String sCodcomp, String sLineaFactura,String sMoneda){
		List Afiliados = null;
		List<SelectItem>lstPOS = new ArrayList<SelectItem>();
		Cafiliados[] cafiliado = null;
		AfiliadoCtrl afCtrl = new AfiliadoCtrl();
		List<Cafiliados> lstCafiliados = null;
		
		try {
			lstPOS.add(new SelectItem("01", "--Afiliados--"));
			
			cafiliado = afCtrl.obtenerAfiliadoxCaja_Compania(iCaid, sCodcomp, sLineaFactura, sMoneda);
			if(cafiliado == null || cafiliado.length == 0)
				return lstPOS;
			
			lstCafiliados = AfiliadoCtrl.getTerminalSocketPos(cafiliado);
			if(lstCafiliados == null || lstCafiliados.isEmpty())
				return lstPOS;
			
			for (Cafiliados ca : lstCafiliados) {
				SelectItem si = new SelectItem();
				si.setValue(ca.getId().getCxcafi().trim() + "," + ca.getTermid());
				si.setLabel(ca.getId().getCxdcafi().trim());
				String sDescrip = ca.getId().getCxcafi().trim()+", "+ ca.getId().getCxdcafi().trim();
				sDescrip += ", LN:";
				sDescrip +=	(String.valueOf(ca.getId().getC6rp07()).trim().equalsIgnoreCase(""))?
							"S/L": ca.getId().getC6rp07().trim(); 
				sDescrip += "," + ca.getTermid();
				si.setDescription(sDescrip);
				lstPOS.add(si);
			}
			 Afiliados = new ArrayList<SelectItem>( lstPOS );
			
		} catch (Exception error) {
			Afiliados = new ArrayList<SelectItem>();
			error.printStackTrace();
		}
		return Afiliados;
	} 

/*****************************************************************************************************/	
	public int getNumeroRM(String sCodsuc,String sTipodoc){
		int iNodoc = 0;
		ReciboCtrl rCtrl = new ReciboCtrl();
		boolean existe = true;
		try{
			while(existe){
				//leer el no de doc a usar
				iNodoc = rCtrl.leerNumeroRpdocmRM(true);
				//validar si ya existe
				existe = rCtrl.validarSiExisteRM(iNodoc, sCodsuc, sTipodoc);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return iNodoc;
	}
/*****************************************************************************************************/
	public void setSelectedCred(SelectedRowsChangeEvent e){
		List lstSelectedRows = null;
		Credhdr hFac = null;
		RowItem row  = null;
		try{
			SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
			lstSelectedRows = gvClienteFacsRM.getSelectedRows();				
			if (lstSelectedRows.size() > 1){
				msgCodigoCliente = "Solo puede seleccionar una factura a la vez";
				srm.addSmartRefreshId(gvClienteFacsRM.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(txtMensaje.getClientId(FacesContext.getCurrentInstance()));			
			}else{
				row = (RowItem)lstSelectedRows.get(0);
				hFac = (Credhdr)gvClienteFacsRM.getDataRow(row);
				m.put("selectedRM",hFac);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
/**********************************************************************************************/
	/** Método: Carlos los afiliados configurados para la caja original de la factura (SOLO DOLLAR)
	 *	Fecha: 26/01/2010
	 *  Nombre: Carlos Manuel Hernández Morrison.
	 */
		public void trasladarAfiliados(ValueChangeEvent ev){
			boolean bValido = true;
			String sMensaje = "";
			Trasladofac tf = null;
			int iCaidPOS=0;
			String sLineaFactura ="";
			
			try {
				//---Traer los datos de la factura en traslado
				if(m.get("fdc_ObjTrasladoFacTrasladoPOS") != null){
					tf = (Trasladofac)m.get("fdc_ObjTrasladoFacTrasladoPOS");
				
					sLineaFactura = CompaniaCtrl.leerLineaNegocio( tf.getCodunineg().trim() );
			
				}else{
					lblMensajeValidacion.setValue("Error al leer los datos del traslado de factura");
					dwProcesa.setWindowState("normal");
					dwProcesa.setStyle("width:370px;height:160px");
					return;
				}
				
				//---Determinar la caja a utilizar al traer los afiliados.
				if(bValido){
					if(chkTrasladarPOS.isChecked()){
						if(chkVoucherManual.isChecked()){
							iCaidPOS = tf.getCaidorig();
						}else{
							sMensaje = "Para traslado de POS el pago debe ser por Vouchers manuales";
							chkTrasladarPOS.setChecked(false);
							bValido = false;
						}
					}else{
						iCaidPOS = tf.getCaiddest();
					}
				}
				//---- Obtener los afiliados para la caja.
				lstAfiliado = new ArrayList();
				lstAfiliado = this.getLstAfiliados(iCaidPOS, tf.getCodcomp(),
												   sLineaFactura, cmbMoneda.getValue().toString());
				m.put("lstAfiliado", lstAfiliado);
				ddlAfiliado.dataBind();
				
				if(!bValido){
					lblMensajeValidacion.setValue(sMensaje);
					dwProcesa.setWindowState("normal");
					dwProcesa.setStyle("width:370px;height:160px");
				}
				
			} catch (Exception error) {
				lblMensajeValidacion.setValue("No se pudo cargar los afiliados de caja origen "+error);
				dwProcesa.setWindowState("normal");
				dwProcesa.setStyle("width:370px;height:160px");
				error.printStackTrace();
			}
		}

		
		
		
		public boolean generarNotaCredito(Session session, Transaction transaction, Hfactura dev, 
					Vf0101 cliente, Vautoriz v, Vf55ca01 f55ca01, String sMonedaBase, 
					String iNoDoco, String sTipodoco, Date fecharecibo ){
			
			boolean done = true;
			
			try {
				
				
				BigDecimal tasaCambioFactura = (dev.getMoneda().compareTo(sMonedaBase) == 0 )? BigDecimal.ZERO : dev.getTasa();
				
				
				Trasladofac tf = new TrasladoCtrl().buscarTrasladofac(session, dev, f55ca01.getId().getCaid(), 0, "" );
				
				String codcomp = dev.getCodcomp() ;
				int fechajul = FechasUtil.DateToJulian( fecharecibo );
				int caid = (tf == null ) ? f55ca01.getId().getCaid() : tf.getCaidprop();
				int numrec =  com.casapellas.controles.tmp.ReciboCtrl.generarNumeroRecibo(caid, codcomp);
				
				BigDecimal montoSaldoFavor = new BigDecimal(Double.toString(( dev.getTotal() ) ) ) .abs();
				
				int numeroBatch = Divisas.numeroSiguienteJdeE1(  );
				int numeroDocumento = Divisas.numeroSiguienteJdeE1Custom(valoresJdeNumeracion[6],valoresJdeNumeracion[7] );
				
				String concepto = "Dev:" + dev.getNofactura() + " " + dev.getTipofactura() + " C:" + caid;
				
				List<String> codigosSucursal = Arrays.asList(new String[]{dev.getCodsuc()}); 
				List<String[]> cuentasContableFactura = CuotaCtrl.idCuentaFacturaTipoClienteSucursal(codigosSucursal, cliente.getId().getA5arc(), "RC");
				
				String[] sCuenta = new Divisas().obtenerCuentaVenta(caid, dev.getCodcomp(), dev.getCodunineg(), session, transaction);
				
				DatosComprobanteF0911 dtaCuentaInteres = new DatosComprobanteF0911(
						sCuenta[1],
						montoSaldoFavor, 
						concepto,
						//dev.getTasa(),
						tasaCambioFactura,
						"",
						"",
						sCuenta[2]);
				
				List<DatosComprobanteF0911> lineasComprobante = new ArrayList<DatosComprobanteF0911>();
				lineasComprobante.add(dtaCuentaInteres);
				
				new ProcesarNuevaFacturaF03B11();
				ProcesarNuevaFacturaF03B11.saldoFavor = true;
				ProcesarNuevaFacturaF03B11.nombrecliente  = cliente.getId().getAbalph().trim(); 
				ProcesarNuevaFacturaF03B11.tipoSolicitud  = sTipodoco;       
				ProcesarNuevaFacturaF03B11.numeroSolicitud = String.valueOf(iNoDoco);
				ProcesarNuevaFacturaF03B11.numeroCuota  = "001";
				ProcesarNuevaFacturaF03B11.tipoInteres  = dev.getTipofactura();
				ProcesarNuevaFacturaF03B11.moneda  = dev.getMoneda();
				ProcesarNuevaFacturaF03B11.sucursal  = CodeUtil.pad( dev.getCodsuc().trim(), 5, "0" );
				ProcesarNuevaFacturaF03B11.unidadNegocio1  = dev.getCodunineg().trim();
				ProcesarNuevaFacturaF03B11.unidadNegocio2  = dev.getCodunineg().trim();
				ProcesarNuevaFacturaF03B11.usuario  = v.getId().getLogin();
				ProcesarNuevaFacturaF03B11.tipoBatch  = DocumuentosTransaccionales.tipoBatchDevolucionCredito();
				ProcesarNuevaFacturaF03B11.tipoImpuesto  = "EXE";
				ProcesarNuevaFacturaF03B11.fechavencimiento = String.valueOf( fechajul ) ;
				ProcesarNuevaFacturaF03B11.monedaLocal = sMonedaBase;
				ProcesarNuevaFacturaF03B11.montoFactura  = montoSaldoFavor ;
				ProcesarNuevaFacturaF03B11.tasaCambio  = tasaCambioFactura;
				ProcesarNuevaFacturaF03B11.montoInteres  = montoSaldoFavor;
				ProcesarNuevaFacturaF03B11.montoImpuesto  = BigDecimal.ZERO;
				 
				ProcesarNuevaFacturaF03B11.codigoCliente  = cliente.getId().getAban8();
				ProcesarNuevaFacturaF03B11.codigoClientePadre = cliente.getId().getAban8();
				ProcesarNuevaFacturaF03B11.numerobatch  = numeroBatch;
				ProcesarNuevaFacturaF03B11.numeroFactura  = numeroDocumento;
				ProcesarNuevaFacturaF03B11.numeroDocumentoOriginal = String.valueOf( numeroDocumento ) ;
				ProcesarNuevaFacturaF03B11.concepto  = concepto ;
				ProcesarNuevaFacturaF03B11.lineasComprobante = lineasComprobante;
				ProcesarNuevaFacturaF03B11.claseContableCliente = cliente.getId().getA5arc();
				ProcesarNuevaFacturaF03B11.idCuentaContableFactura = cuentasContableFactura.get(0)[0];
				ProcesarNuevaFacturaF03B11.codigoCategoria08 = cliente.getId().getAbac08();
				
				ProcesarNuevaFacturaF03B11.fecha = fecharecibo;
				ProcesarNuevaFacturaF03B11.fechaFactura = fecharecibo;
								
				ProcesarNuevaFacturaF03B11.procesarNuevaFactura(session);
				
				String msgProcesoNotaCredito = ProcesarNuevaFacturaF03B11.strMensajeProceso;
				
				done = msgProcesoNotaCredito.isEmpty();
				
				if(!done)
					return done;
				
				ReciboCtrl rCtrl = new ReciboCtrl();
				
				done = rCtrl.fillEnlaceMcajaJde(session, transaction, numrec, codcomp,
						numeroDocumento, numeroBatch, f55ca01.getId().getCaid(), f55ca01.getId().getCaco(), "R", "DCO");
				
				if(!done){
					return done;
				}
				
				
				done = rCtrl.fillEnlaceReciboFac(session, transaction, numrec, codcomp, 
						dev.getNofactura(), 
						dev.getTotal(), 
						dev.getTipofactura(), 
						f55ca01.getId().getCaid(), 
					    CodeUtil.pad(dev.getCodsuc().trim(), 5, "0"), 
					    "",
					    dev.getCodunineg().trim(),"DCO", 
					    dev.getCodcli(), 
					    dev.getFechajulian());
	
				
			} catch (Exception e) {
				done = false;
				LogCajaService.CreateLog("generarNotaCredito", "ERR", e.getMessage());
			}
			
			return done;
		}
		
		
		
/*************************************************************************************************************************/	
		@SuppressWarnings("unchecked")
		public boolean generarNotaDeCredito(Connection cn,Session s,Transaction tx,Hfactura dev,Vf0101 c,Vautoriz v,Vf55ca01 f55ca01, String sMonedaBase,String iNoDoco,String sTipodoco){
			
			boolean bHecho = true;
			Divisas d = new Divisas();
			int iNobatch = 0,iNodoc = 0,iNumrec = 0;
			ReciboCtrl rCtrl = new ReciboCtrl();
			FechasUtil fec = new FechasUtil();
			String[] sCuenta = null;
			int iMonto = 0, iMonto2 = 0, iFecha = 0,iTotalTransaccion = 0;
 
			try{
				iFecha = fec.obtenerFechaActualJuliana();
				
				int iCaid = f55ca01.getId().getCaid();
				Trasladofac tf = new TrasladoCtrl().buscarTrasladofac(s, dev,
											f55ca01.getId().getCaid(), 0, "");
				if(tf != null)
					iCaid = tf.getCaidprop();
				tf = null;
 
				iNumrec = rCtrl.obtenerUltimoRecibo(f55ca01.getId().getCaid(), dev.getCodcomp()) + 1;
				if(iNumrec > 0){
					
					m.put("fdc_NumrecRefactura", iNumrec);
					
					rCtrl.actualizarNumeroRecibo(f55ca01.getId().getCaid(),dev.getCodcomp(), iNumrec);

					iNobatch = d.leerActualizarNoBatch();
					if(iNobatch>0){
	
						
						iNodoc = getNumeroRM( CodeUtil.pad( dev.getCodsuc().trim(), 5, "0") ,dev.getTipofactura());
						
						if(iNodoc > 0){
														
						bHecho = rCtrl.insertarRM(cn, dev, dev.getTipofactura(), iNodoc,
								iFecha, iNobatch, dev.getTasa(), v.getId()
										.getLogin(), v.getId().getNomcorto(),
								iCaid, c, sMonedaBase, iNoDoco + "", sTipodoco);
							
							
							
							if(bHecho){
								
								//buscar cuenta para asiento contable de RM
								// obtener cuenta de venta de contado
								sCuenta = d.obtenerCuentaVenta(iCaid, dev.getCodcomp(), dev.getCodunineg(), s, tx);
								
								if(sCuenta != null){								
									if(dev.getMoneda().equals(sMonedaBase)){
										iMonto =  d.pasarAentero(dev.getTotal());
										
										bHecho = rCtrl.insertarAsientoRM(cn, dev,
												dev.getTipofactura(), iNodoc, iFecha, iNobatch,
												BigDecimal.ZERO, v.getId()
												.getLogin(), v.getId()
												.getNomcorto(), sCuenta,
												"AA", iMonto, iCaid, c, 1,
												sMonedaBase, sCuenta[2], "D");
										
									}else{
										
										double monto1 = dev.getTasa().multiply(
												new BigDecimal( Double
												.toString(dev.getTotal())))
												.setScale(2, RoundingMode.HALF_UP)
												.doubleValue(); 
									
										iMonto  = d.pasarAentero( monto1 );
										iMonto2 = d.pasarAentero( dev.getTotal() );
										
										bHecho = rCtrl.insertarAsientoRM(cn, dev, dev.getTipofactura(), iNodoc, iFecha, iNobatch, 
												dev.getTasa(), v.getId().getLogin(), v.getId().getNomcorto(), 
												sCuenta, "CA", iMonto2, iCaid , c, 1,
												"USD",sCuenta[2],"F");	
										
										if(!bHecho){
											lblMensajeValidacion.setValue("No se pudo grabar el asiento para la nota de credito !!!");
											return false;
										}else{

											bHecho = rCtrl.insertarAsientoRM(cn, dev, dev.getTipofactura(), iNodoc, iFecha, iNobatch, dev.getTasa(), 
													 v.getId().getLogin(), v.getId().getNomcorto(), sCuenta, "AA", 
													 iMonto, iCaid, c, 2,sMonedaBase,sCuenta[2],"F");
										}
									}
									//grabar enlace recibo jde
									if(bHecho){
										bHecho = rCtrl.fillEnlaceMcajaJde(s, tx, iNumrec, dev.getCodcomp(),iNodoc, iNobatch, f55ca01.getId().getCaid(), f55ca01.getId().getCaco(), "R", "DCO");
										//grabar batch
										if(bHecho){
											iTotalTransaccion = d.pasarAentero(dev.getTotal());
											
											bHecho = rCtrl.registrarBatchA92(cn, "I",iNobatch, iTotalTransaccion, v.getId().getLogin(), 1,MetodosPagoCtrl.DEPOSITO);
											//grabar enlace recibofac
											if(bHecho){
												bHecho = rCtrl.fillEnlaceReciboFac(s, tx,iNumrec, dev.getCodcomp(), 
															dev.getNofactura(), dev.getTotal(),dev.getTipofactura(),
														     f55ca01.getId().getCaid(), 
														     
														     //"000"+dev.getCodsuc(), 
														     CodeUtil.pad(dev.getCodsuc().trim(), 5, "0"), 
														     
														     "",
														     dev.getCodunineg().trim(),"DCO", dev.getCodcli(), dev.getFechajulian());
												if(bHecho){
													//---- enviar correo de notificacion a credito y cobranzas
													FacturaContadoCtrl fcc = new FacturaContadoCtrl();
													EmpleadoCtrl ec = new EmpleadoCtrl();
													int iCodCajero = 0;
													Vf0101 vf = null;
													String sTo="",sFrom="",sSubject="",sCliente,sNombreFrom="";
													List<String> lstCc = new ArrayList<String>();
													String sDevolucion,sFactura, sMonto,sCaja,sCompania,sSucursal="";
													Pattern pCorreo = Pattern.compile( "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$" );
													
													//---- Verificar el código del empleado y su correo.
													iCodCajero = v.getId().getCodreg();
													vf = ec.buscarEmpleadoxCodigo2(iCodCajero);
													if(vf!=null){
														sNombreFrom = vf.getId().getAbalph().trim();
														sFrom = vf.getId().getWwrem1().trim().toUpperCase();
														if(!d.validarCuentaCorreo(sFrom))
															sTo = "webmaster@casapellas.com.ni";
													}else{
														sNombreFrom = f55ca01.getId().getCacatinom().trim();
														sFrom = "webmaster@casapellas.com.ni";
													}
													//----- Validar correo destino y copy carbon.
													if(d.validarCuentaCorreo(f55ca01.getId().getCacontmail().trim().toUpperCase()))
														lstCc.add(f55ca01.getId().getCacontmail().trim().toUpperCase());
													if(d.validarCuentaCorreo(f55ca01.getId().getCaan8mail().trim().toUpperCase()))
														lstCc.add(f55ca01.getId().getCaan8mail().trim().toUpperCase());
													
													sTo = sCredyCobMail.trim().toUpperCase();
													if(pCorreo.matcher(sTo).matches()){
														
														sSubject = "Notificación de Emisión de Nota de Crédito por Devolución de Contado";
														sCliente = c.getId().getAban8() +" "+ c.getId().getAbalph().trim();
														sDevolucion = dev.getTipofactura() +" "+ dev.getNofactura();
														sFactura    = dev.getTipodoco() +" "+ dev.getNodoco();
														sMonto		= d.formatDouble(dev.getTotal()) +" "+ dev.getMoneda();
														sCaja 	 	= f55ca01.getId().getCaid() +" "+ f55ca01.getId().getCaname().trim();
														sCompania   = dev.getCodcomp().trim() +" "+ dev.getNomcomp();
						
														sSucursal = CodeUtil.pad(dev.getCodsuc().trim(), 5, "0") + " "+ dev.getNomsuc().trim();	
														
														fcc.enviarCorreoCredyCob(sTo, sFrom, sNombreFrom,sSubject, lstCc, sDevolucion, 
																			sFactura, sCliente, iNobatch, iNodoc+"", 
																			sMonto, sCaja, sCompania, sSucursal);
													}
												}else {
													lblMensajeValidacion.setValue("No se pudo grabar el enlace entre recibo y factura!!!");
													return false;
												}
											}else{
												lblMensajeValidacion.setValue("No se pudo grabar el batch en el JDE!!!");
												return false;
											}
										}else{
											lblMensajeValidacion.setValue("No se pudo grabar el enlace entre recibo y JDE!!!");
											return false;
										}
									}
									else{
										lblMensajeValidacion.setValue("No se pudo grabar el asiento para la nota de credio !!!");
										return false;
									}
								
								}else{
									lblMensajeValidacion.setValue("La cuenta: "+ dev.getUnineg()+" no existe en el mestro de cuentas!!!");
									return false;
								}
							}else{
								lblMensajeValidacion.setValue("No se pudo grabar la nota de credito para cliente: " + c.getId().getAban8()+" "+c.getId().getAbalph());
								return false;
							}
						}else{
							lblMensajeValidacion.setValue("No se pudo leer el numero de documento");
							return false;
						}//validacion de numero de documento
					}else{
						lblMensajeValidacion.setValue("No se pudo leer el numero de batch");
						return false;
					}//validacion de numero de batch
				}else{
					lblMensajeValidacion.setValue("No se pudo leer el numero de recibo");
					return false;
				}
			}catch(Exception ex){
				bHecho = false;
				ex.printStackTrace();
			}
			return bHecho;
		}
		
		@SuppressWarnings("unchecked")
		public void crearNotaCredito(ActionEvent ev){
			boolean aplicado = true;
			Session sesion =  null;
			Transaction trans = null;
			//Connection cn = null;
			Hfactura dev = null;
			Hfactura fac = null;
			
			try {
				LogCajaService.CreateLog("crearNotaCredito", "QRY", "crearNotaCredito-INICIO");
				
				Vautoriz vaut = ((Vautoriz[]) m.get("sevAut"))[0];
				Vf55ca01 caja = ((List<Vf55ca01>) m.get("lstCajas")).get(0);
				
				fac = (Hfactura)m.get("fcd_hFactura");
				List<Hfactura> lstFacturasSelected = (List<Hfactura>) 
										m.get("facturasSelected");
				dev =  lstFacturasSelected.get(0);
				dev.setDfactura(formatDetalle(dev));
				
				String sCajero = (String) m.get("sNombreEmpleado");
				F55ca014 f14 = CompaniaCtrl.filtrarCompania((F55ca014[])m.get("cont_f55ca014"), dev.getCodcomp());
				String sMonedaBase = f14.getId().getC4bcrcd() ;
				
				
				String nodoco   = "0";
				String tipodoco = "";
				
				//&& =============== obtener la primera factura seleccionada para actualizar campos rppo y rpdcto y que aplique la liga automatica
				List<RowItem> facturasSeleccionadas = gvClienteFacsRM.getSelectedRows() ;
				if(facturasSeleccionadas != null && facturasSeleccionadas.size() > 0){
					
					Credhdr factura = (Credhdr) DataRepeater.getDataRow( facturasSeleccionadas.get(0) );
					
					nodoco = String.valueOf( factura.getId().getNofactura() ) ;
					tipodoco = factura.getId().getTipofactura();
					 
				}
				
				
				//&& ==== datos del cliente
				if(!m.containsKey("cNota"))
					aplicado = validarCliente();
				
				if(!aplicado)
					throw new Exception("No se logro validar el cliente");
				
				Vf0101 cliente = (Vf0101)m.get("cNota");	
				
				//&& ==== Conexiones.
				sesion = HibernateUtilPruebaCn.currentSession();
				trans = sesion.beginTransaction();

				aplicado = generarNotaCredito(sesion, trans, dev, cliente, vaut, caja, sMonedaBase, nodoco, tipodoco, new Date() );
				
				if(!aplicado) {
					throw new Exception("No se ha podido generar la nota de credito!");
				}
				
				//&& =====  verificar y actualizar en caso de que sea traslado.
				TrasladoCtrl tcCtrl = new TrasladoCtrl();
				Trasladofac tf = tcCtrl.buscarTrasladofac(sesion, dev, 	caja.getId().getCaid(), 0, "");
				
				if(tf!=null){
					aplicado =  TrasladoCtrl.actualizarEstadoTraslado(sesion, tf, "P");
					if( !aplicado )
						throw new Exception("No se ha podido actualizar el estado del traslado asociado!");
				}
				tf = null;
				tcCtrl = null;
				
				//&& =====  crear solicitud de cheque con los datos de la refactura.
				int numrec = (m.containsKey("fdc_NumrecRefactura"))?
								Integer.parseInt(String.valueOf(
									m.get("fdc_NumrecRefactura"))):0;
				
				String msg = grabarSolechequeRefactura(fac, dev, sesion, caja, 
								vaut.getId().getLogin(), vaut.getId().getCodreg(),
								sMonedaBase, numrec, cliente.getId().getAban8());
				
				aplicado = msg.compareTo("") == 0; 
				if( !aplicado )
					throw new Exception(msg);
				
			} catch (Exception e) {
				LogCajaService.CreateLog("crearNotaCredito", "ERR", e.getMessage());
				e.printStackTrace(); 
				aplicado = false;
			}finally{
				m.remove("cNota");
				dwCrearNotaCredito.setWindowState("hidden");
				m.remove("selectedRM");
				m.remove("selectedFacsRM");
				m.remove("facturasSelected");
				m.remove("fdc_NumrecRefactura");
				dwProcesa.setWindowState("normal");
				dwProcesa.setStyle("width:370px;height:160px");
				
				try {
					if (aplicado) trans.commit();
				} catch (Exception e) {
					LogCajaService.CreateLog("crearNotaCredito", "ERR", e.getMessage());
					e.printStackTrace();
					aplicado = false;
				}
								
				if(aplicado){
					List<Hfactura>lstFacturas = (List<Hfactura>)  m.get("lstHfacturasContado");
					lstFacturas.remove(dev);
					m.put("lstHfacturasContado",lstFacturas);
					gvHfacturasContado.dataBind();
					txtCodCli.setValue("");
					lblMensajeValidacion.setValue("Refacturación aplicada");					
				}else{
					try { 
						trans.rollback(); 
					} catch (Exception e) { 
						LogCajaService.CreateLog("crearNotaCredito", "ERR", e.getMessage());
						e.printStackTrace(); 
					}
					 
					lblMensajeValidacion.setValue("Refacturación no aplicada, intente nuevamente " + ( (msgCodigoCliente != null && !msgCodigoCliente.isEmpty() ) ? msgCodigoCliente : ""  ) );
				}
				

				HibernateUtilPruebaCn.closeSession(sesion);				
				LogCajaService.CreateLog("crearNotaCredito", "QRY", "crearNotaCredito-FIN");

			}
		}

	private String grabarSolechequeRefactura(Hfactura fac, Hfactura dev,
			Session sesion, Vf55ca01 caja, String login, int codreg,
			String monedabase, int numrec, int codcliRM) {

		String result = new String("");
		BigDecimal totalfac = BigDecimal.ZERO;

		try {

			totalfac = (dev.getMoneda().compareTo(monedabase) == 0) ? new BigDecimal(
					String.valueOf(dev.getTotalf())) : new BigDecimal(
					String.valueOf(dev.getTotal()));

			Numcaja numcaja = new Divisas().obtenerNumeracionCaja("SOLECHEQUE",
					caja.getId().getCaid(), dev.getCodcomp(), caja.getId()
							.getCaco(), true, login,sesion);
			if (numcaja == null)
				return "Numeración no encontrada para solicitud de cheque ";

			Solecheque s = new Solecheque();
			SolechequeId sid = new SolechequeId();
			sid.setCaid(caja.getId().getCaid());
			sid.setNosol(numcaja.getNosiguiente());
			sid.setCodcomp(dev.getCodcomp());
			sid.setCodsuc(dev.getCodsuc());
			sid.setCodsuc((dev.getCodsuc().trim().length() == 2) ? "000"
					+ dev.getCodsuc().trim() : dev.getCodsuc());
			sid.setCodunineg(dev.getCodunineg());
			s.setId(sid);

			s.setNumfac(dev.getNofactura());
			s.setTipofactura(dev.getTipofactura());
			s.setFecha(new Date());
			s.setHora(new Date());
			s.setEstado("G");
			s.setCodemp(codreg);
			s.setUsuariomod(codreg);
			s.setFechamod(new Date());
			s.setObservacion("Refacturacion");
			s.setMpago( MetodosPagoCtrl.EFECTIVO);
			s.setTipoemision("RFC");
			s.setMoneda(dev.getMoneda());
			s.setMonto(totalfac);
			s.setCodautoriz(String.valueOf(numrec));
			s.setNotarjeta(String.valueOf(codcliRM)); //&& ======= codigo del cliente con el que se graba el RM en el F0311
			s.setFechapago(fac.getFecha());
			s.setIdafiliado("");
			s.setTasacambio(dev.getTasa());
			s.setCodcli(dev.getCodcli());
			s.setFechafac(dev.getFechajulian());

			LogCajaService.CreateLog("grabarSolechequeRefactura", "HQRY", LogCajaService.toJson(s));
			sesion.save(s);

		} catch (Exception e) {
			LogCajaService.CreateLog("grabarSolechequeRefactura", "ERR", e.getMessage());
			result = " Solicitud por nota de Crédito no aplicada ";
		}
		return result;
	}
/****************************************************************************************/		
	public void crearNotaCredito(){
		boolean bValido = true;
		Vf0101 c = null;
		String sCajero = "";
		Vf55ca01 f55ca01 = null;
		List lstFacturasSelected = null,lstFacturas = null;
		Hfactura dev = null;
		Vautoriz[] vautoriz = null;
		Connection cn = null;
		Transaction tx = null;
		Session s = null;
		String sMonedaBase = "";
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		Credhdr fCred = null;
		try{
			if(m.get("con_processRecibo")==null){
				m.put("con_processRecibo","1");
				f55ca01 = (Vf55ca01) ((List) m.get("lstCajas")).get(0);
				// obtener datos de factura
				lstFacturasSelected = (List) m.get("facturasSelected");
				//obtener detalle de factura		
				dev = (Hfactura) lstFacturasSelected.get(0);
				dev.setDfactura(formatDetalle(dev));
				vautoriz = (Vautoriz[]) m.get("sevAut");
				sCajero = (String) m.get("sNombreEmpleado");
				
				//obtener companias x caja
				sMonedaBase = cCtrl.sacarMonedaBase((F55ca014[])m.get("cont_f55ca014"),dev.getCodcomp());
				
				s = HibernateUtilPruebaCn.currentSession();
				tx = s.beginTransaction();
				// obtener conexion del datasource
				As400Connection as400connection = new As400Connection();
				cn = as400connection.getJNDIConnection("DSMCAJA2");
				cn.setAutoCommit(false);
				
				if(m.get("cNota")==null){
					//validar que el cliente exista
					bValido = validarCliente();
					c = (Vf0101)m.get("cNota");			
				}else{
					c = (Vf0101)m.get("cNota");
				}
				//insertar nota de credito
				if(bValido){
					//validar si hay factura seleccionada
					if(m.get("selectedRM") != null){
						fCred =  (Credhdr)m.get("selectedRM");
						bValido = generarNotaDeCredito(cn,s,tx,dev,c,vautoriz[0],f55ca01,sMonedaBase,fCred.getId().getNofactura() + "",fCred.getId().getTipofactura() + "");
						
						//--- Verificar si es un traslado y de ser asi actualizar su estado.
						if(bValido){
							//----- verificar traslado de factura.
							TrasladoCtrl tcCtrl = new TrasladoCtrl();
							Trasladofac tf = null;
							tf = tcCtrl.buscarTrasladofac(s, dev, f55ca01.getId().getCaid(), 0, "");
							if(tf!=null){
								bValido = tcCtrl.actualizarEstadoTraslado(s, tf, "P");
//								bValido =  tcCtrl.actualizarEstadoTraslado(s, tf, "P","E");
								if(!bValido){
									lblMensajeValidacion.setValue(tcCtrl.getErrorTf().toString().split("@")[1]);
//									lblMensajeValidacion.setValue("No se ha podido actualizar el estado del registro TRASLADOFAC a procesado (P)");
								}
							}
						}
						
						if(bValido){
							cn.commit();
							tx.commit();
							lstFacturas = (List) m.get("lstHfacturasContado");
							lstFacturas.remove(dev);
							m.put("lstHfacturasContado",lstFacturas);
							m.remove("facturasSelected");
							dwCrearNotaCredito.setWindowState("hidden");
							gvHfacturasContado.dataBind();
							txtCodCli.setValue("");
							m.remove("selectedRM");
							m.remove("selectedFacsRM");
						}else{					
							cn.rollback();
							tx.rollback();
							dwProcesa.setWindowState("normal");
							dwProcesa.setStyle("width:370px;height:160px");
							dwCrearNotaCredito.setWindowState("hidden");
						}
					}else{
						//msgCodigoCliente = "Debe seleccionar una factura para Realizar esta operación!!!";
						
						
						bValido = generarNotaDeCredito(cn,s,tx,dev,c,vautoriz[0],f55ca01,sMonedaBase,"","");
						
						//--- Verificar si es un traslado y de ser asi actualizar su estado.
						if(bValido){
							//----- verificar traslado de factura.
							TrasladoCtrl tcCtrl = new TrasladoCtrl();
							Trasladofac tf = null;
							tf = tcCtrl.buscarTrasladofac(s,dev, f55ca01.getId().getCaid(), 0, "");
							if(tf!=null){
								bValido =  tcCtrl.actualizarEstadoTraslado(s, tf, "P");
//								bValido =  tcCtrl.actualizarEstadoTraslado(s, tf, "P","E");
								if(!bValido){
									lblMensajeValidacion.setValue(tcCtrl.getErrorTf().toString().split("@")[1]);
//									lblMensajeValidacion.setValue("No se ha podido actualizar el estado del registro TRASLADOFAC a procesado (P)");
								}
							}
						}
						
						if(bValido){
							cn.commit();
							tx.commit();
							lstFacturas = (List) m.get("lstHfacturasContado");
							lstFacturas.remove(dev);
							m.put("lstHfacturasContado",lstFacturas);
							m.remove("facturasSelected");
							dwCrearNotaCredito.setWindowState("hidden");
							gvHfacturasContado.dataBind();
							txtCodCli.setValue("");
							m.remove("selectedRM");
							m.remove("selectedFacsRM");
						}else{					
							cn.rollback();
							tx.rollback();
							dwProcesa.setWindowState("normal");
							dwProcesa.setStyle("width:370px;height:160px");
							dwCrearNotaCredito.setWindowState("hidden");
						}
					}
				}	
				
			}
			m.remove("con_processRecibo");
			m.remove("cNota");
			m.remove("selectedRM");
			m.remove("selectedFacsRM");
		}catch(Exception ex){
			m.remove("cNota");
			m.remove("selectedRM");
			m.remove("selectedFacsRM");
			m.remove("con_processRecibo");
			dwProcesa.setWindowState("normal");
			dwProcesa.setStyle("width:370px;height:160px");
			dwCrearNotaCredito.setWindowState("hidden");
			lblMensajeValidacion.setValue("No se pudo realizar la operacion "+ex);
			ex.printStackTrace();
		}finally{
			dwCargando.setWindowState("hidden");
			try {cn.close();} catch (Exception e) {}
		}
	}
	public void actualizarInfoCliente(ActionEvent ev){
		boolean bValido = false;
		List<Credhdr> result = null;
		List<Hfactura>lstFacturasSelected = null;
		
		FacturaCreditoCtrl facCreCtrl = new FacturaCreditoCtrl();
		Hfactura dev = null;
		Vf0101 c = null;
		
		try{
			
			Date fechafactura = new Date();
			
			lstFacturasSelected = (List<Hfactura>) m.get("facturasSelected");
			dev = (Hfactura) lstFacturasSelected.get(0);
			
			bValido = validarCliente(); 
			
			
			if(bValido){
				
				c = (Vf0101)m.get("cNota");
				F55ca014[] f14 = (F55ca014[])m.get("cont_f55ca014");
				
				
				result = facCreCtrl.buscarFacturaCreditoNoLigas(c.getId().getAban8() + "",dev.getMoneda(),
						dev.getCodcomp(),
						dev.getCodunineg(),
						CodeUtil.pad( dev.getCodsuc().trim(), 5, "0") ,
						f14, fechafactura);
				
				
				if(result != null  && !result.isEmpty()){
					m.put("selectedFacsRM",result);
					gvClienteFacsRM.dataBind();
					if(result.size() == 1){
						m.put("selectedRM",(Credhdr)result.get(0));
					}
				}else{
					msgCodigoCliente = "No Se encontro factura de credito para el Documento!!!";
					m.remove("selectedRM");
					m.remove("selectedFacsRM");
				}
			}
			
		}catch(Exception ex){
			ex.printStackTrace(); 
		}	
	}
/******************************************************************************************/	
	public void cancelarNotaCredito(ActionEvent ev){
		m.remove("facturasSelected");
		txtCodCli.setValue("");
		m.remove("selectedRM");
		m.remove("selectedFacsRM");
		dwCrearNotaCredito.setWindowState("hidden");
	}
/******************************************************************************************/
/** Método: Realizar validaciones y traslado de factura por "Traer factura".
 *	Fecha:  01/06/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public void traerFacturaPorTraslado(ActionEvent ev){
		Hfactura hFac = null;
		List lstRiCajaSelec;
		List<Hfactura> lstFactura = null;
		String sCodcomp,sMensaje="";
		MonedaCtrl moCtrl = new MonedaCtrl();
		CompaniaCtrl coCtrl = new CompaniaCtrl();
		RowItem riCaja=null;
		boolean bValido = true;
		Vf55ca01 f5Orig=null,f5Dest = null;
	
		try {
			//--- Estilos.
			txtParamFiltrofac.setStyleClass("frmInput2");
			lblValidaEnvioFac.setValue("");
			lblValidaTraerFac.setStyle("display:none");
			
			lstRiCajaSelec = gvFacturasParaTraslado.getSelectedRows();
			if(lstRiCajaSelec.size()==0){
				bValido = false;
				sMensaje = "Debe seleccionar al menos 1 factura para trasladar";
			}else{
				f5Dest = (Vf55ca01)((List) m.get("lstCajas")).get(0);
				f5Orig = (Vf55ca01)(m.get("fdc_TrasFacCajaOrigen"));
				lstFactura = new ArrayList<Hfactura>(lstRiCajaSelec.size());
				
				for(Iterator iter = lstRiCajaSelec.iterator(); iter.hasNext();) {
					riCaja = (RowItem) iter.next();
					hFac = (Hfactura)gvFacturasParaTraslado.getDataRow(riCaja);
					lstFactura.add(hFac);
					sCodcomp = hFac.getCodcomp();

					/* **  Validar que la caja pueda recibir y pagar la factura. ** */
					//--- Compañía.
					F55ca014[] f14 = null;
					f14 = coCtrl.obtenerCompaniasxCaja(f5Dest.getId().getCaid());
					if(f14==null || f14.length==0){
						bValido = false;
						sMensaje = "La Caja actual no tiene configuradas compañías para pago de facturas.";
						break;
					}else{
						bValido = false;
						for (byte i = 0; i < f14.length; i++) {
							if(sCodcomp.trim().equals(f14[i].getId().getC4rp01().trim())){
								bValido = true;
								break;
							}
						}
						if(!bValido){
							sMensaje =  "La Caja actual no puede pagar la Compañía: "+sCodcomp + " " +hFac.getNomcomp().trim();
							sMensaje += ", Fact: "+hFac.getNofactura();
							break;
						}else{
							//--- Moneda.
							String sMoxCaja[] = moCtrl.obtenerMonedasxCaja(f5Dest.getId().getCaid(), sCodcomp);
							if(sMoxCaja==null ||sMoxCaja.length==0){
								bValido = false;
								sMensaje = "La Caja no tiene monedas de pago configuradas para "+hFac.getNomcomp().trim();
								break;
							}
						}	
					}
				}
				//----- Guardar los traslados.
				if(!bValido){
					lblValidaTraerFac.setStyle("display: inline");
					lblValidaTraerFac.setValue(sMensaje);
				}else{
					dwFacturasTraslado.setWindowState("hidden");
					TrasladoCtrl tc = new TrasladoCtrl();
					Vautoriz[] vAut  = (Vautoriz[])m.get("sevAut");
					bValido = tc.guardarListaTraslado(vAut[0],f5Orig, f5Dest, lstFactura,false);
					if(bValido){
						m.remove("facturasSelected");
						m.remove("fcd_lstCajasDisponibleEnvio");
						lblMensajeValidacion.setValue("Se ha realizado correctamente el traslado de factura desde caja: " +f5Orig.getId().getCaname().trim());
						
						//---- Refrescar la lista de Facturas de la caja.
						listarFacturasContadoDelDia();
						gvHfacturasContado.dataBind();
						
					}else{
						lblMensajeValidacion.setValue("No se han podido realizar el traslado de Factura: Error al registrar en TRASLADOFAC");
					}
					dwProcesa.setWindowState("normal");
				}
			}
		} catch (Exception error) {
			dwProcesa.setWindowState("normal");
			lblMensajeValidacion.setValue("No se han podido realizar el traslado de Factura: Error de sistema: "+error);
			LogCajaService.CreateLog("traerFacturaPorTraslado", "ERR", error.getMessage());
		}
	}
	
	/******************************************************************************************/
	/** Método: Anular Traslado de Factura.
	 *	Fecha:  04/03/2021
	 *  Nombre: Luis Alberto Fonseca Méndez.
	 */
	public void anularTraslado(ActionEvent ev){
		Trasladofac tFac = null;
		List lstRiCajaSelec;
		List<Trasladofac> lstFactura = null;
		String sCodcomp,sMensaje="";
		MonedaCtrl moCtrl = new MonedaCtrl();
		CompaniaCtrl coCtrl = new CompaniaCtrl();
		RowItem riCaja=null;
		boolean bValido = true;
		int iNoFactura=0;
	
		try {
			//--- Estilos.
			txtParamFiltrofac.setStyleClass("frmInput2");
			lblValidaEnvioFac.setValue("");
			lblValidaTraerFac.setStyle("display:none");
			
			lstRiCajaSelec = gvFacturasTrasladadas.getSelectedRows();
			if(lstRiCajaSelec.size()==0){
				bValido = false;
				sMensaje = "Debe seleccionar al menos 1 factura para trasladar";
			}else{

				lstFactura = new ArrayList<Trasladofac>(lstRiCajaSelec.size());
				
				for(Iterator iter = lstRiCajaSelec.iterator(); iter.hasNext();) {
					riCaja = (RowItem) iter.next();
					tFac = (Trasladofac)gvFacturasTrasladadas.getDataRow(riCaja);
					lstFactura.add(tFac);
					sCodcomp = tFac.getCodcomp();
					iNoFactura = tFac.getNofactura();

					TrasladoCtrl tc = new TrasladoCtrl();
					bValido = tc.deleteTrasladofac(tFac);
					
				}
				//----- Guardar los traslados.
				if(!bValido){
					lblValidaTraerFac.setStyle("display: inline");
					lblValidaTraerFac.setValue("Error al intentar anular el traslado");
				}else{
					dwMostrarTraslado.setWindowState("hidden");
					lblMensajeValidacion.setValue("Se ha realizado correctamente la anulación de traslado de factura numero: " +iNoFactura);
						
					//---- Refrescar la lista de Facturas de la caja.
					listarFacturasContadoDelDia();
					gvHfacturasContado.dataBind();
			
					dwProcesa.setWindowState("normal");
				}
			}
		} catch (Exception error) {
			dwProcesa.setWindowState("normal");
			lblMensajeValidacion.setValue("No se han podido realizar la anulacion de traslado de Factura: Error de sistema: "+error);
			error.printStackTrace();
		}
	}
/******************************************************************************************/
/** Método: Filtrar la lista de Facturas disponibles para traslado de la caja seleccionada.
 *	Fecha:  01/06/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public void filtrarFacturaParaEnvio(ActionEvent ev){
		String sParametro = "",sTipoFiltro ="",sMensaje="";
		CtrlCajas cc = new CtrlCajas();
		Vf55ca01 vfCajaSel = null;
		boolean bValido = true;	
		String sTiposDoc[] = null;
		List lstLocalizaciones = null;
		F55ca017[] f55ca017 =  null;
		List lstFactsA02 = null, lstFacHfact=null;
		int iTipoBusq = 0, iCaid=0, iFechaActual=0;
		FechasUtil f = new FechasUtil();
		
		try {
			lblValidaTraerFac.setValue("");
			lblValidaTraerFac.setStyle("display:none");
			txtParamFiltrofac.setStyleClass("frmInput2");
			
			sParametro  = txtParamFiltrofac.getValue().toString().trim();
			sTipoFiltro = ddlFiltroFacturaTras.getValue().toString();
			iTipoBusq = Integer.parseInt(sTipoFiltro);
			
			//---Validar los parámetros ingresados.
			if(!sParametro.equals("")){
				switch(iTipoBusq){
				case 1:
					if(!sParametro.matches("^[A-Za-z0-9-,-.\\p{Blank}]+$")){
						bValido = false;
						sMensaje = "El valor ingresado contiene caracteres inválidos.";
					}
					break;
				default: 
					if(!sParametro.matches("^[0-9]+$")){
						bValido = false;
						sMensaje = "El valor ingresado solo debe contener entre 1 y 8 dígitos .";
					}
					break;
				}
			}
			//---- Traer el registro para la caja.
			if(bValido){
				iCaid =Integer.parseInt(ddlFiltroCajasTras.getValue().toString().trim());
				vfCajaSel = cc.obtenerDatosCaja(iCaid);
				if(vfCajaSel!=null){					
					m.put("fdc_TrasFacCajaOrigen", vfCajaSel);
					
					//--- Buscar los datos de configuración de la caja.
					List<Object> lstConfigCaja = cc.leerConfiguracionCaja(vfCajaSel);
					if(lstConfigCaja!=null){
						sTiposDoc = (String[])lstConfigCaja.get(0);
						lstLocalizaciones = (ArrayList)lstConfigCaja.get(1);
						f55ca017 = (F55ca017[])lstConfigCaja.get(2);
						iFechaActual = f.DateToJulian(new Date());
						
						//--- Leer las facturas para la caja seleccionada y su configuración.
						FacturaContadoCtrl fcc = new FacturaContadoCtrl();
						lstFactsA02 = fcc.buscarFactura(sParametro, iFechaActual, sTiposDoc, "01", "01", 
													iTipoBusq, f55ca017, lstLocalizaciones, iCaid);
						if(lstFactsA02!=null && lstFactsA02.size()>0){
						
							//---- Formatear Facturas de Hfactjdecon  a Hfactura.
							lstFacHfact = formatFacturaHfactjdeCon(lstFactsA02);
							
						}else{
							bValido = false;
							sMensaje = "No se han encontrado facturas para la caja";
						}
					}else{
						bValido = false;
						sMensaje = "No se ha podido leer los datos de configuración de la caja";
					}
				}else{
					bValido = false;
					sMensaje = "No se ha podido obtener los datos básicos de la caja seleccionada.";
				}
			}
			if(bValido){
				m.put("fcd_lstFacturasParaTraslado", lstFacHfact);
				gvFacturasParaTraslado.dataBind();
				gvFacturasParaTraslado.setPageIndex(0);
			}else{
				if(!sParametro.equals(""))
					txtParamFiltrofac.setStyleClass("frmInput2Error");
				lblValidaTraerFac.setValue(sMensaje);
				lblValidaTraerFac.setStyle("display:inline");
			}
		} catch (Exception error) {
			lblValidaTraerFac.setValue("No se ha podido realizar Filtro: Error de systema: "+error);
			lblValidaTraerFac.setStyle("display:inline");
			error.printStackTrace();
		}
	}
/****************************************************************************************/
/** Método: Mostrar la ventana para seleccionar la factura a importar.
 *	Fecha:  01/06/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public void mostrarTraerFactura(ActionEvent ev){
		List lstCajas = null;
		CtrlCajas cc = new CtrlCajas();
		String sql = "",sMensajeError="";
		Vf55ca01 vf5 = null;
		boolean bHecho = true;
		
		try {
			txtParamFiltrofac.setStyleClass("frmInput2");
			txtParamFiltrofac.setValue("");
			lblValidaTraerFac.setValue("");
			lblValidaTraerFac.setStyle("display:none");
			m.remove("fcd_lstFacturasParaTraslado");
			gvFacturasParaTraslado.setPageIndex(0);
			
			//--- Llenar la lista de cajas disponibles.
			vf5 = (Vf55ca01)((List) m.get("lstCajas")).get(0);
			sql =  " from Vf55ca01 f where f.id.castat = 'A' and f.id.caid <> "+vf5.getId().getCaid();
			lstCajas = cc.obtenerListaCajas(sql, 0);
			
			if(lstCajas!=null && lstCajas.size()>0){
				List<SelectItem> lstCajasTras = new ArrayList<SelectItem>(lstCajas.size());
				for (Iterator iter = lstCajas.iterator(); iter.hasNext();) {
					Vf55ca01 vf = (Vf55ca01) iter.next();
					if(vf!=null)
						lstCajasTras.add(new SelectItem(vf.getId().getCaid()+"", vf.getId().getCaname().trim(),										
											vf.getId().getCaid() + " " + vf.getId().getCaname().trim()));
				}
				m.put("fcd_lstFiltroCajasTras",lstCajasTras);m.get("fcd_lstFiltroCajasTras");
				ddlFiltroCajasTras.dataBind();
			}else{
				bHecho = false;
				sMensajeError = "Error: No se ha podido obtener la lista de cajas disponibles";
			}
			if(bHecho){
				dwFacturasTraslado.setWindowState("normal");
			}else{
				lblValidaFactura.setValue(sMensajeError);
				dwValidacionFactura.setWindowState("normal");
			}
		} catch (Exception error) {
			lblValidaFactura.setValue("No se puede Realizar operación - Error de Sistema: "+error);
			dwValidacionFactura.setWindowState("normal");
			error.printStackTrace();
		}
	}
	
	/****************************************************************************************/	
	
	/****************************************************************************************/
	/** Método: Mostrar la ventana donde se veran los traslados asociados a una factura.
	 *	Fecha:  24/02/2021
	 *  Nombre: Luis Alberto Fonseca Méndez.
	 */
		public void mostrarTrasladoFactura(ActionEvent ev){
			
			boolean bValido = false;
			List<Hfactura> lstFacturasSelected = null;
			Hfactura hFac = null;
			
			try {

				ConfiguracionMensaje[] lstConfiguracionMensaje = (ConfiguracionMensaje[]) m.get("lstConfiguracionMensaje");
				
				//Valida información de Facturas
				bValido = getFacturasSelected(lstConfiguracionMensaje);
				if(!bValido)
					return;
				
				//Valida información de la factura incluso que no se haya trasladado a otra caja
				//verificar si se va a quitar esto
				bValido = validarReciboAntesDeMostrar(m.get("facturasSelected"), lstConfiguracionMensaje);
				
				if (bValido) {
					lstFacturasSelected = (List<Hfactura>) m.get("facturasSelected");
					hFac = lstFacturasSelected.get(0);
					
					if(lstFacturasSelected.size()>1)
						throw new Exception("Tiene que Seleccionar solo una factura");
					
					if(lstFacturasSelected.size()==0)
						throw new Exception("Tiene que seleccionar una Factura");
				}
				
				Session sesion = null;
				Transaction tx = null;
				boolean aplicado = false;
				
				m.remove("fcd_lstFacturasTrasladadas");
				gvFacturasTrasladadas.dataBind();
				gvFacturasTrasladadas.setPageIndex(0);
				
				//&& ======== Actualizar los estados de las facturas por traslado.
				for (Hfactura hf : lstFacturasSelected) {
					List<Trasladofac> tf = new TrasladoCtrl().buscarTraslados(null, hf, 0, 0, "");
					if(tf.size() == 0) 
						break;
					
					m.put("fcd_lstFacturasTrasladadas",tf);m.get("fcd_lstFacturasTrasladadas");

				}
				
		
				dwMostrarTraslado.setWindowState("normal");
				

			} catch (Exception error) {
				lblValidaFactura.setValue("No se puede Realizar operación - Error de Sistema: "+error);
				dwValidacionFactura.setWindowState("normal");
				error.printStackTrace();
			}
		}
		
		/****************************************************************************************/	
	public boolean validarCliente(){
		Vf0101 c = null;
		FacturaContadoCtrl fCtrl = new FacturaContadoCtrl();
		int iCodcli = 0;
		String sCodcli = "";
		boolean bValido = true;
		strInfoCliente = "";
		msgCodigoCliente = "";
		try{
			//validar si existe el cliente
			if(txtCodCli.getValue() != null){
				sCodcli = txtCodCli.getValue().toString();
				if(sCodcli.matches("^[0-9]+$")){
					iCodcli = Integer.parseInt(sCodcli);
					c = fCtrl.existeCliente(iCodcli, "00000");
					if(c != null){
						m.put("cNota", c);
						strInfoCliente = "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> "+ c.getId().getAban8() +"  " + c.getId().getAbalph() + "<br>" + c.getId().getAbtx2();
					}else{
						bValido = false;
						msgCodigoCliente = "No existe cliente con este código";
					}
				}else{
					bValido = false;
					msgCodigoCliente = "Codigo de cliente no valido";
				}
			}else{
				bValido = false;
				msgCodigoCliente = "Codigo de cliente no valido";
			}
			//fin de validar si existe el cliente
			
		}catch(Exception ex){
			bValido = false;
			LogCajaService.CreateLog("validarCliente", "ERR", ex.getMessage());
		}
		return bValido;
	}
/****************************************************************************************/
	public void mostrarCrearNotaCredito(ActionEvent ev){
		boolean bValido = false;
		FacturaCreditoCtrl facCreCtrl = new FacturaCreditoCtrl();
		List result = new ArrayList();
		try{
			
			LogCajaService.CreateLog("mostrarCrearNotaCredito", "INFO", "mostrarCrearNotaCredito-INICIO");
			
			txtCodCli.setValue("");
			List lstFacturasSelected = (List) m.get("facturasSelected");
			//obtener detalle de factura		
			Hfactura dev = (Hfactura) lstFacturasSelected.get(0);
			txtCodCli.setValue(dev.getCodcli());
			//validar que el cliente exista
			bValido = validarCliente(); 
			//realizar busqueda de nota de credito
			if(bValido){
				F55ca014[] f14 = (F55ca014[])m.get("cont_f55ca014");
				//realizar busqueda de facturas de credito del cliente del dia
				result = facCreCtrl.buscarFacturasNoLigas(dev.getCodcli() + "",
						dev.getMoneda(),
						dev.getCodcomp(),
						dev.getCodunineg(),
						CodeUtil.pad( dev.getCodsuc().trim(), 5, "0") ,
						f14);
				
				if(result != null  && !result.isEmpty()){
					m.put("selectedFacsRM",result);
					gvClienteFacsRM.dataBind();
					if(result.size() == 1){
						m.put("selectedRM",(Credhdr)result.get(0));
					}
				}else{
					msgCodigoCliente = "No Se encontro factura de credito para el Documento!!!";
					m.remove("selectedRM");
				}
			}
			dwConfirmaEmisionCheque.setWindowState("hidden");
			dwCrearNotaCredito.setWindowState("normal");
			msgCodigoCliente = "";
			
		}catch(Exception ex){
			LogCajaService.CreateLog("mostrarCrearNotaCredito", "ERR", ex.getMessage());
		} finally {
			LogCajaService.CreateLog("mostrarCrearNotaCredito", "INFO", "mostrarCrearNotaCredito-FIN");
		}
	}
/****************************************************************************************/
/** Método: Validar que solo se seleccione una caja.
 *	Fecha:  27/05/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 */	
	public void trasladarFactura(ActionEvent ev){
		Hfactura hFac = null;
		List lstFactura = null,lstCajaSelec;
		String sCodcomp,sMoneda,sMensaje="";
		MonedaCtrl moCtrl = new MonedaCtrl();
		CompaniaCtrl coCtrl = new CompaniaCtrl();
		RowItem riCaja=null;
		boolean bValido = true;
		Vf55ca01 f5Orig=null,f5Dest = null;
		
		try {
			//--- Estilos.
			txtParamCajaEnvio.setStyleClass("frmInput2");
			lblValidaEnvioFac.setValue("");
			lblValidaEnvioFac.setStyle("display:none");
			dwCajasParaTraslado.setStyle("width:740px;height:460px");
			
			lstCajaSelec = gvCajasDisponibleEnvio.getSelectedRows();
			if(lstCajaSelec.size()==0){
				bValido = false;
				sMensaje = "Debe seleccionar la caja a utilizar ";
			}else{
				riCaja = (RowItem)lstCajaSelec.get(0);
				f5Dest = (Vf55ca01)gvCajasDisponibleEnvio.getDataRow(riCaja);
				f5Orig = (Vf55ca01)((List) m.get("lstCajas")).get(0);
				
				lstFactura = (ArrayList)m.get("facturasSelected");
				if(lstFactura!=null && lstFactura.size()>0){
					hFac = (Hfactura)lstFactura.get(0);
					sCodcomp = hFac.getCodcomp();
					sMoneda  = hFac.getMoneda();
					
					/* **  Validar que la caja pueda recibir y pagar la factura. ** */
					//--- Compañía.
					F55ca014[] f14 = null;
					f14 = coCtrl.obtenerCompaniasxCaja(f5Dest.getId().getCaid());
					if(f14==null || f14.length==0){
						bValido = false;
						sMensaje = "La Caja destino no puede pagar la Compañía: "+sCodcomp + " " +hFac.getNomcomp().trim();
					}else{
						bValido = false;
						for (byte i = 0; i < f14.length; i++) {
							if(sCodcomp.trim().equals(f14[i].getId().getC4rp01().trim())){
								bValido = true;
								break;
							}
						}
						if(!bValido){
							sMensaje = "La Caja destino no puede pagar la Compañía: "+sCodcomp + " " +hFac.getNomcomp().trim();
						}else{
							//--- Moneda.
							String sMoxCaja[] = moCtrl.obtenerMonedasxCaja(f5Dest.getId().getCaid(), sCodcomp);
							if(sMoxCaja==null ||sMoxCaja.length==0){
								bValido = false;
								sMensaje = "La Caja no tiene monedas de pago configuradas para "+hFac.getNomcomp().trim();
							}
						}
					}
				}else{
					bValido = false;
					sMensaje = "No se ha podido obtener los datos de la factura seleccionada";
				}
			}
			//-------- Si esta correcto guardar en TRASLADOFAC. si no mandar mensaje de aviso.
			if(!bValido){
				lblValidaEnvioFac.setStyle("display: inline");
				lblValidaEnvioFac.setValue(sMensaje);
			}else{
				dwCajasParaTraslado.setWindowState("hidden");
				TrasladoCtrl tc = new TrasladoCtrl();
				Vautoriz[] vAut  = (Vautoriz[])m.get("sevAut");
				bValido = tc.guardarListaTraslado(vAut[0], f5Orig, f5Dest, lstFactura,true);
				if(bValido){
					m.remove("facturasSelected");
					m.remove("fcd_lstCajasDisponibleEnvio");
					lblMensajeValidacion.setValue("Se ha realizado correctamente el traslado de factura a caja: " +f5Dest.getId().getCaname().trim());
					
					//---- Refrescar la lista de Facturas de la caja.
					listarFacturasContadoDelDia();
					gvHfacturasContado.dataBind();
					
				}else{
					lblMensajeValidacion.setValue("No se han podido realizar el traslado de Factura: Error al registrar en TRASLADOFAC");
				}
				dwProcesa.setWindowState("normal");
			}
		} catch (Exception error) {
			dwCajasParaTraslado.setWindowState("hidden");
			lblMensajeValidacion.setValue("No se puede realizar operación: Error de Sistema --> "+error);
			dwProcesa.setWindowState("normal");
			error.printStackTrace();
		}
	}
/****************************************************************************************/
/** Método: Validar que solo se seleccione una caja.
 *	Fecha:  27/05/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 */	
	public void verificarSelecCaja(SelectedRowsChangeEvent e) {
		List lstCajasSelec = null;
		SelectedRowChange se[]=null;
		RowItem riActual, riAnterior;
		
		try {
			lstCajasSelec = gvCajasDisponibleEnvio.getSelectedRows();
			if(lstCajasSelec.size()>1){
				se =  e.getSelectedRowsChange();
				riActual = se[0].getRow();
				
				if(riActual.isSelected()){
					for (int i = 0; i < lstCajasSelec.size(); i++) {
						riAnterior = (RowItem)lstCajasSelec.get(i);
						if(!riActual.equals(riAnterior))
							riAnterior.setSelected(false);
					}
				}
				SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
				srm.addSmartRefreshId(gvCajasDisponibleEnvio.getClientId(FacesContext.getCurrentInstance()));
			}
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
	
/****************************************************************************************/
/** Método: Filtrar la lista de cajas disponibles para envio, a partir de los parámetros
 *	Fecha:  25/05/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public void filtrarCajasParaEnvio(ActionEvent ev){
		String sql="",sFiltrarPor="",sParametroFiltro="",sMensaje="";
		boolean bValido = true;
		int iTipoBusqueda =0;
		List lstCajasDisponibles = null;
		
		try {
			//--- Estilos.
			txtParamCajaEnvio.setStyleClass("frmInput2");
			lblValidaEnvioFac.setValue("");
			lblValidaEnvioFac.setStyle("display:none");
			dwCajasParaTraslado.setStyle("width:740px;height:460px");
			
			sFiltrarPor = ddlFiltroCajaEnvio.getValue().toString();
			sParametroFiltro = txtParamCajaEnvio.getValue().toString().trim();
			
			Vf55ca01 vf5 = (Vf55ca01)((List) m.get("lstCajas")).get(0);
			sql =  " from Vf55ca01 f where f.id.castat = 'A' and f.id.caid <> "+vf5.getId().getCaid();
			
			if(!sParametroFiltro.equals("")){
				iTipoBusqueda = Integer.parseInt(sFiltrarPor);
				switch(iTipoBusqueda){
				case 1:
					if(sParametroFiltro.matches("^[A-Za-z0-9-\\p{Blank}]+$")){
						sql += " and trim(lower(f.id.caname)) like '%"+sParametroFiltro.trim().toLowerCase()+"%'"; 
					}else{
						bValido = false;
						sMensaje = "El valor ingresado contiene caracteres inválidos.";
					}
					break;
				case 2:
					if(sParametroFiltro.matches("^[0-9]+$")){
						sql += " and f.id.caid = "+sParametroFiltro; 
					}else{
						bValido = false;
						sMensaje = "El valor ingresado debe contener solo números.";
					}
					break;
				case 3:
					if(sParametroFiltro.matches("^[A-Za-z0-9-\\p{Blank}]+$")){
						sql += " and trim(lower(f.id.cacatinom)) like '%"+sParametroFiltro.trim().toLowerCase()+"%'"; 
					}else{
						bValido = false;
						sMensaje = "El valor ingresado contiene caracteres inválidos.";
					}
					break;
				}
			}
			//-----Ejecutar la Consulta.
			if(bValido){
				CtrlCajas cc  = new CtrlCajas();
				lstCajasDisponibles = cc.obtenerListaCajas(sql,25);
				if(lstCajasDisponibles==null ||lstCajasDisponibles.size()==0 ){
					lstCajasDisponibles = new ArrayList(1);
					bValido = false;
					sMensaje = "No se encontró resultados en la búsqueda";
					dwCajasParaTraslado.setStyle("width:740px;height:430px");
				}
			}
			if(!bValido){
				lblValidaEnvioFac.setValue(sMensaje);
				lblValidaEnvioFac.setStyle("display:inline");
				txtParamCajaEnvio.setStyleClass("frmInput2Error");
			}else{
				m.remove("fcd_lstCajasDisponibleEnvio");
				m.put("fcd_lstCajasDisponibleEnvio", lstCajasDisponibles);
				dwCajasParaTraslado.setStyle("width:740px;height:430px");
				gvCajasDisponibleEnvio.dataBind();
				gvCajasDisponibleEnvio.setPageIndex(0);
			}
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
/****************************************************************************************/
/** Método: Muestra una ventana con la lista de cajas disponibles para enviar un factura
 *	Fecha:  25/05/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 **/
	public void mostrarEnviarFactura(ActionEvent ev){
		List<Hfactura> lstFactura = new ArrayList<Hfactura>();
		List lstCajasDisponibles  = null;
		String sMensaje   = new String();
		String sEstiloMsg = new String();
		String sql		  = new String();
		
		boolean bValido = true;
		Hfactura hFac = null;
//		lstFactura = (ArrayList<Hfactura>)m.get("facturasSelected");
		
		try {			
			
			if( gvHfacturasContado.getSelectedRows().size() == 0){
				lblValidaFactura.setValue("Seleccione al menos una factura");
				dwValidacionFactura.setWindowState("normal");
				return;
			}
			
			List<RowItem> lstSelectedRows =  gvHfacturasContado.getSelectedRows();
			for (RowItem ri : lstSelectedRows) {
				Hfactura hfTmp = (Hfactura) gvHfacturasContado.getDataRow(ri);
				lstFactura.add(hfTmp);
			}
			
			sEstiloMsg += "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" />"; 
			
			txtParamCajaEnvio.setValue("");
			lblValidaEnvioFac.setValue("");
			lblValidaEnvioFac.setStyle("display:none");
			
			if(lstFactura!=null && lstFactura.size()>0){
				Vf55ca01 f5 = (Vf55ca01)((List) m.get("lstCajas")).get(0);
				for (int i = 0; i < lstFactura.size(); i++) {
					hFac = (Hfactura) lstFactura.get(i);
					if(hFac.getEstado().trim().equals("A")){
						bValido = false;
						sMensaje += sEstiloMsg;
						sMensaje += "La factura "+hFac.getNofactura()+" se encuentra anulada y no puede trasladarse";
						break;
					}
					Trasladofac tf = new TrasladoCtrl().existeFacturaenTrasladofac(hFac, f5.getId().getCaid(),"");
					if( tf!=null ){
						bValido = false;
						sMensaje += sEstiloMsg;
						sMensaje += "La factura "+hFac.getNofactura()+" se encuentra traslada hacia la caja "+tf.getCaiddest()+"";
						sMensaje += ", Hora: "+new SimpleDateFormat("hh:mm:ss a").format(tf.getFecha());
						break;
					}
				}
				if(bValido){
					//--- Leer la configuración y obtener lista de las cajas restantes.
					sql =  " from Vf55ca01 f where f.id.castat = 'A' and f.id.caid <> "+f5.getId().getCaid();
					sql += " order by f.id.caid"; 
					lstCajasDisponibles = new CtrlCajas().obtenerListaCajas(sql, 30);
					if(lstCajasDisponibles==null){
						lstCajasDisponibles = new ArrayList();
						bValido = false;
						sMensaje += sEstiloMsg;
						sMensaje = "No se ha podido obtener la lista de cajas disponibles para enviar la  Factura";
					}else{
						m.remove("fcd_lstCajasDisponibleEnvio");
						m.put("fcd_lstCajasDisponibleEnvio", lstCajasDisponibles);
						gvCajasDisponibleEnvio.dataBind();
						gvCajasDisponibleEnvio.setPageIndex(0);
						ddlFiltroCajaEnvio.dataBind();
					}
				}
			}else{
				bValido = false;
				sMensaje += sEstiloMsg;
				sMensaje += "Debe Seleccionar al menos una factura para realizar la operación.<br>";
			}
			if(!bValido){
				lblValidaFactura.setValue(sMensaje);
				dwValidacionFactura.setWindowState("normal");
			}else{
				m.put("facturasSelected", lstFactura);
				dwCajasParaTraslado.setStyle("width:740px;height:460px");
				dwCajasParaTraslado.setWindowState("normal");
			}
		} catch (Exception error) {
			lblValidaFactura.setValue("No se puede realizar operación: Error de Sistema: " + error);
			dwValidacionFactura.setWindowState("normal");
			error.printStackTrace();
		}
	}
/************ cerrar Ventanas usadas en traslado de factura ***************/
	public void cerrarEnvioFactura(ActionEvent ev){
		m.remove("fcd_lstCajasDisponibleEnvio");
		dwCajasParaTraslado.setWindowState("hidden");
	}
	public void cerrarTraerFactura(ActionEvent ev){
		dwFacturasTraslado.setWindowState("hidden");
	}
	
	public void cerrarMostrarTraslado(ActionEvent ev){
		dwMostrarTraslado.setWindowState("hidden");
	}
	/*******************************************************************/
	
	
	public void comprobarFacturas(List lstFacturas){
		Hfactura f = null;
		String sLinea = "";
		Hf4211 h = null;
		FacturaContadoCtrl fCtrl = new FacturaContadoCtrl();
		boolean bHecho = false;
		Connection cn;
		
		try{
			// obtener conexion del datasource
			As400Connection as400connection = new As400Connection();
			cn = as400connection.getJNDIConnection("DSMCAJA2");
			for(int i = 0;i < lstFacturas.size();i++){
				f = (Hfactura)lstFacturas.get(i);				
				sLinea = f.getCodunineg().trim().substring(2,4);
				if(sLinea.equals("04")|| sLinea.equals("06") || sLinea.equals("11") ||
					sLinea.equals("22")|| sLinea.equals("23") || sLinea.equals("24")		
				){
					h = fCtrl.comprobarSiExisteF4211(f.getNofactura(), f.getTipofactura(), 
							CodeUtil.pad(f.getCodsuc().trim() , 5, "0") ,
							f.getCodunineg());
					
					//========== anular factura
					if(h == null){
					
						bHecho = fCtrl.anularFacturaFDC(cn, f.getNofactura(), 
								f.getTipofactura(),
								CodeUtil.pad(f.getCodsuc().trim() , 5, "0") ,
								f.getCodunineg(), f.getCodcli(), 
								f.getFechajulian());
					
						lstFacturas.remove(i);
						cn.commit();
					}
				}
			}
			m.put("lstHfacturasContado", lstHfacturasContado);
			
		}catch(Exception ex){
			ex.printStackTrace();
		} 
	}
/***************************************************************************************************************/	
	public void sincronizarFacturas(){
		F55ca017[] f55ca017 = null;
		String sLinea = "";
		FechasUtil fecUtil = new FechasUtil();
		int iFechaActual = 0;
		FacturaContadoCtrl fCtrl = new FacturaContadoCtrl();
		List lstF4211Facs = new ArrayList(),lstDet = new ArrayList(), lstBorrarItems = new ArrayList();
		Hf4211 h = null;
		Df4211 d = null;
		Vhfactura vh = null;
		boolean bHecho = false, bAnular = true;
		Connection cn = null;
		try{
		
			f55ca017 = (F55ca017[]) m.get("f55ca017");
			
			if(f55ca017 != null){
				// obtener conexion del datasource
				As400Connection as400connection = new As400Connection();
				cn = as400connection.getJNDIConnection("DSMCAJA2");
				
				iFechaActual = fecUtil.obtenerFechaActualJuliana();
				
				for(int i = 0; i < f55ca017.length;i++){
					
					sLinea = CompaniaCtrl.leerLineaNegocio(
							f55ca017[i].getId().getC7mcul());
					if(sLinea == null || sLinea.compareTo("") == 0)
						continue;
					sLinea = sLinea.trim();
					
					//&& ======= Verificar si debe sincronizarse o no facturas.
					if(PropertiesSystem.LNS_JDEDWARDS.contains(sLinea.trim())){
					
						//buscar facturas en el f4211
						lstF4211Facs = fCtrl.getFacturasFDC(f55ca017[i].getId().getC7mcul(), f55ca017[i].getId().getC7locn(), iFechaActual);
						for(int j = 0; j < lstF4211Facs.size();j++){
							h = (Hf4211)lstF4211Facs.get(j);
							
							//comprobar si la factura ya existe en el modulo de caja
							vh = fCtrl.comprobarSiExiste(h.getId().getNofactura(), 
									h.getId().getTipofactura(), h.getId().getCodsuc(),
									h.getId().getCodunineg(), h.getId().getCodcli(),
									h.getId().getFecha(),
									h.getId().getHora(),
									h.getId().getEstado() );
							
							if(vh == null){// no existe se inserta la factura
								
								//insertarFactura header
								bHecho = fCtrl.insertarFacturaFDC(cn,h);

							}//si existe comprobar q no hayan cambios en la factura
							else{
								lstDet = fCtrl.buscarDetalleFactura(h);
								//1) comprobar si esta totalmente anulada
								for(int a = 0; a < lstDet.size(); a++){
									d = (Df4211)lstDet.get(a);
									if(d.getId().getSdnxtr().equals("999") && d.getId().getSdlttr().equals("980")){//linea anulada
										lstBorrarItems.add(d);
									}else{
										bAnular = false;
									}
								}
								if(bAnular){//Si esta totalmente anulada anular
									bHecho = fCtrl.anularFacturaFDC(cn, h.getId().getNofactura(), 
											h.getId().getTipofactura(), h.getId().getCodsuc(), 
											h.getId().getCodunineg(), h.getId().getCodcli(),
											h.getId().getFecha());
								}else{
									
									//&& === Si cambio el monto de la factura en jde, actualizar la factura en caja.
									if(h.getId().getSubtotal().intValue() != vh.getId().getSubtotal().intValue())
										bHecho = 
											fCtrl.actualizarFacturaFDC(cn, h.getId().getNofactura(), 
													h.getId().getTipofactura(), 
													h.getId().getCodsuc(), 
													h.getId().getCodunineg(), 
													h.getId().getCodcli(),
													h.getId().getSubtotal().intValue(),
													h.getId().getTotal().intValue(),
													h.getId().getSubtotalf().intValue(),
													h.getId().getTotalf().intValue(),
													h.getId().getTotalcosto(),
													h.getId().getFecha());	
						
									if(bHecho && lstBorrarItems != null && lstBorrarItems.size() > 0)	
										bHecho = fCtrl.borrarDetalleAnulado(cn,lstBorrarItems);
									
								}
							}
						}
					}
				}		
				cn.commit();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			try {cn.close();} catch (Exception e) {}
		}
	}
	
/******************************************************************************************************************/	
	public List sacarCambioMetodos(List lstMetodosPago,Hfactura f, String sMonedaBase){
		Divisas d = new Divisas();
		String sCambio1 = "", sCambio2 = "";
		String[] sLblCambios1,sLblCambios2;
		double dCambio1 = 0.0, dCambio2 = 0.0, dNewMonto1 = 0.0, dNewMonto2 = 0.0, dCambio = 0.0;
		MetodosPago[] metPago = null;
		boolean paso1 = false, paso2 = false;
		metPago = new MetodosPago[lstMetodosPago.size()];	
		String sLblCambio1 = "", sLblCambio2 = "";
		try{
			sLblCambio1 = lblCambio.getValue().toString();
			sLblCambio2 = lblCambioDomestico.getValue().toString();
			// obtener cambios
			if (txtCambio.getValue().toString().trim().equals("")){//hay cambio mixto todos los  pagos fueron en usd
				sCambio1 = txtCambioForaneo.getValue().toString();
				dCambio1 = d.formatStringToDouble(sCambio1);
				sCambio2 = txtCambioDomestico.getValue().toString();
				dCambio2 = d.formatStringToDouble(sCambio2);
				

				dCambio = d.formatStringToDouble(txtMontoRecibido.getValue().toString()) - d.formatStringToDouble(txtMontoAplicar.getValue().toString());
				
				sLblCambios1 = sLblCambio1.trim().split(" ");
				sLblCambios2 = sLblCambio2.trim().split(" ");
				//
				sLblCambio1 = sLblCambios1[sLblCambios1.length - 1].substring(0, 3);
				sLblCambio2 = sLblCambios2[sLblCambios1.length - 1].substring(0, 3);
				//
				for (int i = 0; i < lstMetodosPago.size(); i++) {
					metPago[i] = (MetodosPago) lstMetodosPago.get(i);
					if (metPago[i].getMetodo().equals( MetodosPagoCtrl.EFECTIVO)&& !metPago[i].getMoneda().equals(sMonedaBase)&& !paso1) {
						dNewMonto1 = metPago[i].getMonto() - dCambio;
						metPago[i].setMonto(dNewMonto1);
						metPago[i].setEquivalente(dNewMonto1);
						lstMetodosPago.remove(i);
						lstMetodosPago.add(i, metPago[i]);
						paso1 = true;
					} 
				}
			} else {// hay cambio en una moneda unicamente
				sCambio1 = txtCambio.getValue().toString();
				dCambio1 = d.formatStringToDouble(sCambio1);
				sLblCambios1 = sLblCambio1.trim().split(" ");
				//
				sLblCambio1 = sLblCambios1[sLblCambios1.length - 1].substring(0, 3);
				//
				for (int i = 0; i < lstMetodosPago.size(); i++) {
					metPago[i] = (MetodosPago) lstMetodosPago.get(i);
					if (metPago[i].getMetodo().equals( MetodosPagoCtrl.EFECTIVO) && metPago[i].getMoneda().equals(sLblCambio1) && !paso1) {
						if (sLblCambio1.equals(sMonedaBase)&& f.getMoneda().equals(sMonedaBase)) {// se saca el cambio en cor del pago en cor
							dNewMonto1 = metPago[i].getMonto() - dCambio1;
							metPago[i].setMonto(dNewMonto1);
							metPago[i].setEquivalente(dNewMonto1);
						} else if (sLblCambio1.equals(sMonedaBase) && !f.getMoneda().equals(sMonedaBase) && lstMetodosPago.size() > 1) {// se saca el cambio del pago en cor y se convierte a usd con tasa del metodo
							dNewMonto1 = metPago[i].getMonto() - dCambio1;
							metPago[i].setMonto(dNewMonto1);
							metPago[i].setEquivalente(d.roundDouble4(dNewMonto1 / metPago[i].getTasa().doubleValue()));
						} else if (sLblCambio1.equals(sMonedaBase) && !f.getMoneda().equals(sMonedaBase) && lstMetodosPago.size() == 1) {// si pago toda la fac de usd en cor
							dNewMonto1 = metPago[i].getMonto() - dCambio1;
							metPago[i].setMonto(dNewMonto1);
							metPago[i].setEquivalente(d.roundDouble4(dNewMonto1/ metPago[i].getTasa().doubleValue()));
						} else if (sLblCambio1.equals(sMonedaBase) && f.getMoneda().equals(sMonedaBase) && lstMetodosPago.size() == 1) {// el pago en usd cubre toda la venta
							dNewMonto1 = metPago[i].getEquivalente() - dCambio1;
							metPago[i].setMonto(d.roundDouble4(dNewMonto1 / metPago[i].getTasa().doubleValue()));
							metPago[i].setEquivalente(dNewMonto1);
						}
						lstMetodosPago.remove(i);
						lstMetodosPago.add(i, metPago[i]);
						paso1 = true;
					} else if (metPago[i].getMetodo().equals( MetodosPagoCtrl.EFECTIVO) && sLblCambio1.equals(sMonedaBase) && !metPago[i].getMoneda().equals(sMonedaBase) && !paso1) {
						if (f.getMoneda().equals(sMonedaBase) && lstMetodosPago.size() == 1) {// el pago en usd cubre toda la venta
							
						}
					}
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();				
		}
		return lstMetodosPago;
	}

/*******************************************************************/
/************************************************************************************************/
	public BigDecimal obtenerTasaParalela(String sMoneda){
		BigDecimal tasa = BigDecimal.ONE;
		Tpararela[] tcPar;
		tcPar = (Tpararela[]) m.get("tpcambio");
		// buscar tasa de cambio paralela para moneda
		for (int t = 0; t < tcPar.length; t++) {
			if (tcPar[t].getId().getCmono().equals(sMoneda) || tcPar[t].getId().getCmond().equals(sMoneda)) {
				tasa = tcPar[t].getId().getTcambiom();
				break;
			}
		}
		return tasa;
	}
	
	/*********************************************************************************************/
	public BigDecimal obtenerTasaOficial(){
		Tcambio[] tcJDE;
		BigDecimal tasa = BigDecimal.ZERO;
		//buscar tasa de cambio oficial para la moneda de pago.
		tcJDE = (Tcambio[])m.get("tcambio");
		for(int l = 0; l < tcJDE.length;l++){
			if(tcJDE[l].getId().getCxcrcd().equals("COR")){
				tasa = tcJDE[l].getId().getCxcrrd();
				break;
			}
		}
		return tasa;
	}
	/***********************************************************************************************/
/*****************************************************************************/
/***********************************************************************************
 *                  REALIZAR ASIENTOS PARA DEVOLUCION DE CONTADO
 * ********************************************************************************/
	public boolean generarAsientosDevolucion(Session s, Transaction tx,  int iCajaId,
						String sCodComp, double dTotalAplicar, List lstFacturasSelected,
						List lstMetodosPago, Hfactura hFac, int iNumrec, String sMonedaBase, Date dtFecha) {
		boolean bContabilizado = true, bForGenerado = false, bDomGenerado = false,bHayDiferencial = false;
		Divisas d = new Divisas();
		ReciboCtrl recCtrl = new ReciboCtrl();
		MetodosPago metPago = null;
		EmpleadoCtrl empCtrl = new EmpleadoCtrl();
		String[]  sCuentaDC = null, sCuentaCaja = null, sCuentaVenta = null;
		Vautoriz[] vaut = null;
		int iNoBatch = 0, iNoDocumentoFor = 0, iNoDocumentoDom = 0;
		String sConcepto = "";
		int iTotalTransaccion = 0;
		int iContadorDom = 0, iContadorFor = 0;
		double dMonto1 = 0.0,dMonto2 = 0.0,dMontoDif = 0.0;
		boolean bTraslado = false;
		Trasladofac tf = null;
		
		
		try {
			m.put("iContadorDom", iContadorDom);
			m.put("iContadorFor", iContadorFor);
			sConcepto = "Dev No:" + iNumrec + " Ca:" + iCajaId + " Com:" + hFac.getCodcomp();
			
			String msgLogs = sConcepto;
			
			Vf55ca01 f55ca01 = (Vf55ca01) ((List) m.get("lstCajas")).get(0);
			vaut = (Vautoriz[]) m.get("sevAut");
			
			LogCajaService.CreateLog("generarAsientosDevolucion", "INFO", "generarAsientosDevolucion - INICIO");			
			iNoBatch = Divisas.numeroSiguienteJdeE1(  );
			
			// determinar el tipo de cliente
			String sCompCuentaCaja = "", sCompCuentaVenta = "";
			// validar Batch
			if (iNoBatch > 0) {
				if (bContabilizado) {				
					//---- Verificar si factura es un traslado o es local y determinar cuenta Venta de Contado.
					int iCaid = 0;
					tf = new TrasladoCtrl().buscarTrasladofac(s, hFac, f55ca01.getId().getCaid(), 0, "");
					if(tf!=null){
						 bTraslado = true; 
						 iCaid = tf.getCaidprop();
					}else{
						 iCaid = f55ca01.getId().getCaid();
						 bTraslado = false;
					}
					// obtener cuenta de venta de contado
					sCuentaVenta = d.obtenerCuentaVenta(iCaid, hFac.getCodcomp(), hFac.getCodunineg(), s, tx);
					//
					if (sCuentaVenta != null) {
						for (int i = 0; i < lstMetodosPago.size(); i++) {
							metPago = (MetodosPago) lstMetodosPago.get(i);
							// Obtener cuenta de metodo de pago						
							sCuentaCaja = d.obtenerCuentaCaja(f55ca01.getId().getCaid(),hFac.getCodcomp(),metPago.getMetodo(),metPago.getMoneda(),s,tx,null,null);
							//
							if (sCuentaCaja != null) {
								if (!hFac.getMoneda().equals(sMonedaBase)) {// factura en dolares
									if (metPago.getMoneda().equals(sMonedaBase)) {
										iTotalTransaccion = iTotalTransaccion + d.pasarAentero(d.roundDouble4(metPago.getMonto()));
										if (!bForGenerado) {
											
											// leer el numero de documento foraneo a utilizar para el asiento de diario
											//iNoDocumentoFor = d.leerActualizarNoDocJDE();
											iNoDocumentoFor = Divisas.numeroSiguienteJdeE1Custom(valoresJdeNumeracion[8],valoresJdeNumeracion[9] );
											
											if (iNoDocumentoFor > 0) {
												bForGenerado = true;
											} else {
												// No se pudo encontrar el No.  de documento
												lblMensajeValidacion.setValue("No se encontró No. de Documento para Asiento Domestico!!!");
												bContabilizado = false;
												break;
											}
										}
										iContadorDom = Integer.parseInt(m.get("iContadorDom").toString());
										iContadorFor = Integer.parseInt(m.get("iContadorFor").toString());
										//validar si hay diferencial cambiario			
										//partir el monto en pago neto y dif cambiario
										if(metPago.getTasa().doubleValue()!=hFac.getTasa().doubleValue()){
											bHayDiferencial = true;
										
											dMonto1 = d.roundDouble(metPago.getEquivalente() * hFac.getTasa().doubleValue());
											dMonto2 = d.roundDouble(metPago.getEquivalente());
											
											dMontoDif = metPago.getMonto() -  dMonto1;
											// leer el numero de documento domestico a utilizar para el asiento de diario
											if (!bDomGenerado) {					
												
												//iNoDocumentoDom = d.leerActualizarNoDocJDE();
												iNoDocumentoDom = Divisas.numeroSiguienteJdeE1Custom(valoresJdeNumeracion[8],valoresJdeNumeracion[9] );
												
												if (iNoDocumentoDom > 0) {
													bDomGenerado = true;
												} else {
													// No se pudo encontrar el No. de documento
													lblMensajeValidacion.setValue("No se encontró No. de Documento para Asiento Domestico!!!");
													bContabilizado = false;
													break;
												}
											}
										}else{
											dMonto1 = d.roundDouble(metPago.getMonto());//dMonto1 = d.roundDouble4(metPago.getMonto());
											dMonto2 = d.roundDouble(metPago.getEquivalente());//dMonto2 = d.roundDouble4(metPago.getEquivalente());
										}
										
										iContadorFor++;
										bContabilizado = recCtrl.registrarAsientoDiarioLogs(s, msgLogs, dtFecha, hFac.getCodsuc(),valoresJdeIns[1], iNoDocumentoFor,(iContadorFor + 1) * 1.0,iNoBatch,sCuentaVenta[0],sCuentaVenta[1],sCuentaVenta[3],sCuentaVenta[4],
												sCuentaVenta[5],valoresJdeIns[2],hFac.getMoneda(), d.pasarAentero(dMonto2), sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),
												hFac.getTasa(),"","Met: " + metPago.getMetodo() + " D: " + hFac.getNofactura(),
												sCuentaVenta[2],"","",valoresJdeIns[3],sCuentaVenta[2],valoresJdeIns[4], 0);									
										
										if (bContabilizado) {
											
											bContabilizado = recCtrl.registrarAsientoDiarioLogs(s, msgLogs, dtFecha,  hFac.getCodsuc(),valoresJdeIns[1],iNoDocumentoFor,(iContadorFor + 1) * 1.0,iNoBatch,sCuentaVenta[0],sCuentaVenta[1],sCuentaVenta[3],sCuentaVenta[4],
													sCuentaVenta[5],valoresJdeIns[5],hFac.getMoneda(),d.pasarAentero(dMonto1),sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),hFac.getTasa(),"",
													"Met: " + metPago.getMetodo()+ " D: " + hFac.getNofactura(),sCuentaVenta[2],
													"","", sMonedaBase,sCuentaVenta[2],valoresJdeIns[4], d.pasarAentero(dMonto2) );
											
											if (bContabilizado) {
												
												// asiento de diario para cuenta de venta de contado
												bContabilizado = recCtrl.registrarAsientoDiarioLogs(s, msgLogs, dtFecha, hFac.getCodsuc(),valoresJdeIns[1], iNoDocumentoFor,(iContadorFor) * 1.0,iNoBatch,sCuentaCaja[0],sCuentaCaja[1],sCuentaCaja[3],
														sCuentaCaja[4],sCuentaCaja[5],valoresJdeIns[2],hFac.getMoneda(),-1*d.pasarAentero(dMonto2),sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),
														hFac.getTasa(),"","Met: " + metPago.getMetodo()+ " D: " + hFac.getNofactura(),
														sCuentaCaja[2],"","",valoresJdeIns[3],sCuentaCaja[2],valoresJdeIns[4], 0);
												
												if (bContabilizado) {
													bContabilizado = recCtrl.registrarAsientoDiarioLogs(s, msgLogs, dtFecha, hFac.getCodsuc(),valoresJdeIns[1], iNoDocumentoFor,(iContadorFor) * 1.0,iNoBatch,sCuentaCaja[0],sCuentaCaja[1],sCuentaCaja[3],
															sCuentaCaja[4],sCuentaCaja[5],valoresJdeIns[5],hFac.getMoneda(),-1* d.pasarAentero(dMonto1) ,sConcepto,vaut[0].getId().getLogin(),
															vaut[0].getId().getCodapp(),hFac.getTasa(),"","Met: " + metPago.getMetodo()+ " D: " + hFac.getNofactura(),
															sCuentaCaja[2],"","",sMonedaBase,sCuentaCaja[2],valoresJdeIns[4], (-1*d.pasarAentero(dMonto2)) );
													
													iContadorFor++;
													
													//validar si hay diferencial cambiario
													if(bHayDiferencial){
														//buscar cuenta para el dif cambiario
														if(sCuentaDC==null){
															sCuentaDC = d.obtenerCuentaDifCambiario(iCaid, hFac.getCodcomp(), hFac.getCodunineg(), s, tx, null, null);
														}
														
														if(sCuentaDC!=null){
															iContadorDom++;
															
															bContabilizado = recCtrl.registrarAsientoDiarioLogs(s, msgLogs, dtFecha, hFac.getCodsuc(),valoresJdeIns[1], iNoDocumentoDom,(iContadorDom + 1) * 1.0,iNoBatch,sCuentaDC[0],sCuentaDC[1],sCuentaDC[3],sCuentaDC[4],
																	sCuentaDC[5],valoresJdeIns[5],sMonedaBase,d.pasarAentero(dMontoDif),sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),BigDecimal.ZERO,"",
																	"Dif/Camb Met: " + metPago.getMetodo(),sCuentaDC[2],"","",sMonedaBase,sCuentaDC[2],valoresJdeIns[7], 0);
															
															bContabilizado = recCtrl.registrarAsientoDiarioLogs(s, msgLogs, dtFecha, hFac.getCodsuc(),valoresJdeIns[1], iNoDocumentoDom,(iContadorDom) * 1.0,iNoBatch,sCuentaCaja[0],sCuentaCaja[1],sCuentaCaja[3],
																	sCuentaCaja[4],sCuentaCaja[5],valoresJdeIns[5],sMonedaBase,(-1)* d.pasarAentero(dMontoDif),
																	sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),BigDecimal.ZERO,"",
																	"Dif/Camb Met: " + metPago.getMetodo(),sCuentaCaja[2],"","",sMonedaBase,sCuentaCaja[2],valoresJdeIns[7], 0);
															
															
															iContadorDom++;
														}else{
															bContabilizado = false;
															lblMensajeValidacion.setValue("La cuenta de diferencial cambiario para la unidad de negocios: "+ hFac.getCodunineg()+ "; no está configurada!!!");
															break;
														}
													}
													m.put("iContadorDom",iContadorDom);
													m.put("iContadorFor",iContadorFor);
												} else {
													lblMensajeValidacion.setValue("No se pudo registrar el aiento de diario: "+ iNoDocumentoFor+ "!!!");
													break;
												}
											} else {
												lblMensajeValidacion.setValue("No se pudo registrar el aiento de diario: "+ iNoDocumentoFor+ "!!!");
												break;
											}
										} else {
											lblMensajeValidacion.setValue("No se pudo registrar el aiento de diario: "+ iNoDocumentoFor+ "!!!");
											break;
										}
										if (!bContabilizado) {
											break;
										}
									} else {// pago en la moneda de la factura
										iTotalTransaccion = iTotalTransaccion + d.pasarAentero(d.roundDouble4(metPago.getMonto() * hFac.getTasa().doubleValue()));
										// comprobar si generar el no. de documento
										if (!bForGenerado) {
											
											// leer el numero de documento foraneo a utilizar para el asiento de diario						
											//iNoDocumentoFor = d.leerActualizarNoDocJDE();	
											iNoDocumentoFor = Divisas.numeroSiguienteJdeE1Custom(valoresJdeNumeracion[8],valoresJdeNumeracion[9] );
											
											if (iNoDocumentoFor > 0) {												
												bForGenerado = true;												
											} else {
												// No se pudo encontrar el No. de documento
												lblMensajeValidacion.setValue("No se encontró No. de Documento para Asiento Foraneo!!!");
												bContabilizado = false;
												break;
											}
										}
										//
										iContadorDom = Integer.parseInt(m.get("iContadorDom").toString());
										iContadorFor = Integer.parseInt(m.get("iContadorFor").toString());
										iContadorFor++;
										
										// asiento de diario para cuenta de venta de contado
										bContabilizado = recCtrl.registrarAsientoDiarioLogs(s, msgLogs, dtFecha, hFac.getCodsuc(),valoresJdeIns[1], iNoDocumentoFor,(iContadorFor + 1) * 1.0,iNoBatch,sCuentaVenta[0],sCuentaVenta[1],sCuentaVenta[3],sCuentaVenta[4],
												sCuentaVenta[5],valoresJdeIns[2],metPago.getMoneda(),d.pasarAentero(d.roundDouble4(metPago.getMonto())),sConcepto,vaut[0].getId().getLogin(),
												vaut[0].getId().getCodapp(),hFac.getTasa(),"","Met: " + metPago.getMetodo() + " D: " + hFac.getNofactura(),
												sCuentaVenta[2],"","",valoresJdeIns[3],sCuentaVenta[2],valoresJdeIns[4], 0);
										
										if (bContabilizado) {
											bContabilizado = recCtrl.registrarAsientoDiarioLogs(s, msgLogs, dtFecha, hFac.getCodsuc(),valoresJdeIns[1],iNoDocumentoFor,(iContadorFor + 1) * 1.0,iNoBatch,sCuentaVenta[0],sCuentaVenta[1],sCuentaVenta[3],sCuentaVenta[4],
													sCuentaVenta[5],valoresJdeIns[5],metPago.getMoneda(),d.pasarAentero(d.roundDouble4(metPago.getMonto()* hFac.getTasa().doubleValue())) ,
													sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),hFac.getTasa(),"","Met: " + metPago.getMetodo()+ " D: " + hFac.getNofactura(),
													sCuentaVenta[2],"","",sMonedaBase,sCuentaVenta[2],valoresJdeIns[4],
													(d.pasarAentero(d.roundDouble4(metPago.getMonto())))
													);
											
											if (bContabilizado) {	
												bContabilizado = recCtrl.registrarAsientoDiarioLogs(s, msgLogs, dtFecha, hFac.getCodsuc(),valoresJdeIns[1],iNoDocumentoFor,(iContadorFor) * 1.0,iNoBatch,sCuentaCaja[0],sCuentaCaja[1],sCuentaCaja[3],
														sCuentaCaja[4],sCuentaCaja[5],valoresJdeIns[2],metPago.getMoneda(), (-1)* (d.pasarAentero(d.roundDouble4(metPago.getMonto()))),
														sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),hFac.getTasa(),"","Met: " + metPago.getMetodo()+ " D: " + hFac.getNofactura(),
														sCuentaCaja[2],"","",valoresJdeIns[3],sCuentaCaja[2],valoresJdeIns[4], 0);
												
												if (bContabilizado) {
													bContabilizado = recCtrl.registrarAsientoDiarioLogs(s, msgLogs, dtFecha, hFac.getCodsuc(),valoresJdeIns[1],iNoDocumentoFor,(iContadorFor) * 1.0,iNoBatch,sCuentaCaja[0],sCuentaCaja[1],sCuentaCaja[3],
															sCuentaCaja[4],sCuentaCaja[5],valoresJdeIns[5],metPago.getMoneda(),(-1)* (d.pasarAentero(d.roundDouble4(metPago.getMonto()* hFac.getTasa().doubleValue()))),
															sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),hFac.getTasa(),"","Met: " + metPago.getMetodo()+ " D: " + hFac.getNofactura(),
															sCuentaCaja[2],"","",sMonedaBase,sCuentaCaja[2],valoresJdeIns[4],
															((-1)* (d.pasarAentero(d.roundDouble4(metPago.getMonto()))))
															);
													iContadorFor++;
													m.put("iContadorDom",iContadorDom);
													m.put("iContadorFor",iContadorFor);
												} else {
													lblMensajeValidacion.setValue("No se pudo registrar el aiento de diario: "+ iNoDocumentoFor+ "!!!");
													break;
												}
											} else {
												lblMensajeValidacion.setValue("No se pudo registrar el aiento de diario: "+ iNoDocumentoFor+ "!!!");
												break;
											}
										} else {
											lblMensajeValidacion.setValue("No se pudo registrar el aiento de diario: "+ iNoDocumentoFor+ "!!!");
											break;
										}
									}
								} else {// factura en cordobas
									if (!metPago.getMoneda().equals(sMonedaBase)) {
										iTotalTransaccion = iTotalTransaccion+ d.pasarAentero(d.roundDouble4(metPago.getEquivalente()));
										if (!bDomGenerado) {
											// leer el numero de documento domestico a utilizar para el asiento de diario											
											if (!bForGenerado) {
												
												// leer el numero de documento foraneo a utilizar para el asiento de diario												
												//iNoDocumentoFor = d.leerActualizarNoDocJDE();												
												iNoDocumentoFor = Divisas.numeroSiguienteJdeE1Custom(valoresJdeNumeracion[8],valoresJdeNumeracion[9] );
												
												if (iNoDocumentoFor > 0) {																									
													bForGenerado = true;													
												} else {
													// No se pudo encontrar el No. de documento
													lblMensajeValidacion.setValue("No se encontró No. de Documento para Asiento Foraneo!!!");
													bContabilizado = false;
													break;
												}
											}
										}
										
										if(bContabilizado){
											iContadorFor++;
											// asiento de diario para cuenta de venta de contado
											bContabilizado = recCtrl.registrarAsientoDiarioLogs(s, msgLogs, dtFecha, hFac.getCodsuc(),valoresJdeIns[1], iNoDocumentoFor,(iContadorFor + 1) * 1.0,iNoBatch,sCuentaVenta[0],sCuentaVenta[1],sCuentaVenta[3],sCuentaVenta[4],
													sCuentaVenta[5],valoresJdeIns[2],metPago.getMoneda(), d.pasarAentero(d.roundDouble4(metPago.getMonto())),
													sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(), metPago.getTasa(),"","Met: " + metPago.getMetodo()+ " D: " + hFac.getNofactura(),
													sCuentaVenta[2],"","",valoresJdeIns[3],sCuentaVenta[2],valoresJdeIns[4], 0 );
											if (bContabilizado) {
												bContabilizado = recCtrl.registrarAsientoDiarioLogs(s, msgLogs, dtFecha, hFac.getCodsuc(),valoresJdeIns[1], iNoDocumentoFor,(iContadorFor + 1) * 1.0,iNoBatch,sCuentaVenta[0],sCuentaVenta[1],sCuentaVenta[3],sCuentaVenta[4],
														sCuentaVenta[5],valoresJdeIns[5],metPago.getMoneda(), d.pasarAentero(d.roundDouble4(metPago.getMonto()* metPago.getTasa().doubleValue())),
														sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),metPago.getTasa(),"","Met: " + metPago.getMetodo()+ " D: " + hFac.getNofactura(),
														sCuentaVenta[2],"","",sMonedaBase,sCuentaVenta[2],valoresJdeIns[4], ( d.pasarAentero(d.roundDouble4(metPago.getMonto())) ) );
												if (bContabilizado) {
													//asiento cuenta caja
													bContabilizado = recCtrl.registrarAsientoDiarioLogs(s, msgLogs, dtFecha, hFac.getCodsuc(),valoresJdeIns[1], iNoDocumentoFor,(iContadorFor) * 1.0,iNoBatch,sCuentaCaja[0],sCuentaCaja[1],sCuentaCaja[3],
															sCuentaCaja[4],sCuentaCaja[5],valoresJdeIns[2],metPago.getMoneda(), (-1)* (d.pasarAentero(d.roundDouble4(metPago.getMonto()))),
															sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),metPago.getTasa(),"","Met: " + metPago.getMetodo()+ " D: " + hFac.getNofactura(),
															sCuentaCaja[2],"","",valoresJdeIns[3],sCuentaCaja[2],valoresJdeIns[4], 0);
													if (bContabilizado) {
														bContabilizado = recCtrl.registrarAsientoDiarioLogs(s, msgLogs, dtFecha, hFac.getCodsuc(),valoresJdeIns[1], iNoDocumentoFor,(iContadorFor) * 1.0,iNoBatch,sCuentaCaja[0],sCuentaCaja[1],sCuentaCaja[3],
																sCuentaCaja[4],sCuentaCaja[5],valoresJdeIns[5],metPago.getMoneda(), (-1)* (d.pasarAentero(d.roundDouble4(metPago.getMonto()* metPago.getTasa().doubleValue()))),
																sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),metPago.getTasa(),"","Met: " + metPago.getMetodo()+ " D: " + hFac.getNofactura(),
																sCuentaCaja[2],"","",sMonedaBase,sCuentaCaja[2],valoresJdeIns[4], 
																((-1)* (d.pasarAentero(d.roundDouble4(metPago.getMonto())))) );
														iContadorFor++;
														m.put("iContadorDom",iContadorDom);
														m.put("iContadorFor",iContadorFor);
													} else {
														lblMensajeValidacion.setValue("No se pudo registrar el aiento de diario: "+ iNoDocumentoFor+ "!!!");
														break;
													}
												} else {
													lblMensajeValidacion.setValue("No se pudo registrar el aiento de diario: "+ iNoDocumentoFor+ "!!!");
													break;
												}
											} else {
												lblMensajeValidacion.setValue("No se pudo registrar el aiento de diario: "+ iNoDocumentoFor+ "!!!");
												break;
											}
										}
										iContadorDom = Integer.parseInt(m.get("iContadorDom").toString());
										iContadorFor = Integer.parseInt(m.get("iContadorFor").toString());
									} else {// pago en la misma moneda COR a COR
										iTotalTransaccion = iTotalTransaccion + d.pasarAentero(d.roundDouble4(metPago.getMonto()));
										// comprobar si generar el no. de documento
										if (!bDomGenerado) {
											// leer el numero de documento foraneo a utilizar para el asiento de diario											
											
											//iNoDocumentoDom = d.leerActualizarNoDocJDE();											
											iNoDocumentoDom = Divisas.numeroSiguienteJdeE1Custom(valoresJdeNumeracion[8],valoresJdeNumeracion[9] );
											
											if (iNoDocumentoDom > 0) {												
												bDomGenerado = true;												
											} else {
												// No se pudo encontrar el No. de documento
												lblMensajeValidacion.setValue("No se encontró No. de Documento para Asiento Foraneo!!!");
												bContabilizado = false;
												break;
											}
										}
										iContadorDom = Integer.parseInt(m.get("iContadorDom").toString());
										iContadorFor = Integer.parseInt(m.get("iContadorFor").toString());
										//
										iContadorDom++;
										bContabilizado = recCtrl.registrarAsientoDiarioLogs(s, msgLogs, dtFecha, hFac.getCodsuc(),valoresJdeIns[1], iNoDocumentoDom,(iContadorDom + 1) * 1.0,iNoBatch,sCuentaVenta[0],sCuentaVenta[1],
												sCuentaVenta[3],sCuentaVenta[4],sCuentaVenta[5],valoresJdeIns[5],metPago.getMoneda(),d.pasarAentero(d.roundDouble4(metPago.getMonto())),
												sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),BigDecimal.ZERO,"","Met: " + metPago.getMetodo()+ " D: " + hFac.getNofactura(),
												sCuentaVenta[2],"","",sMonedaBase,sCuentaVenta[2],valoresJdeIns[7], 0);										
										if (bContabilizado) {
											// asiento de diario para cuenta de venta de contado
											bContabilizado = recCtrl.registrarAsientoDiarioLogs(s, msgLogs, dtFecha, hFac.getCodsuc(),valoresJdeIns[1], iNoDocumentoDom,(iContadorDom) * 1.0,iNoBatch,
													sCuentaCaja[0],sCuentaCaja[1],sCuentaCaja[3],sCuentaCaja[4],sCuentaCaja[5],valoresJdeIns[5],metPago.getMoneda(),
													(-1)* (d.pasarAentero(d.roundDouble4(metPago.getMonto()))),sConcepto,
													vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),BigDecimal.ZERO,"","Met: " + metPago.getMetodo()+ " D: " + hFac.getNofactura(),
													sCuentaCaja[2],"","",sMonedaBase,sCuentaCaja[2],valoresJdeIns[7], 0);	
											iContadorDom++;
											m.put("iContadorDom", iContadorDom);
											m.put("iContadorFor", iContadorDom);
										} else {
											lblMensajeValidacion.setValue("No se pudo registrar el aiento de diario: "+ iNoDocumentoDom+ "!!!");
											break;
										}
									}
								}
							} else {
								// no se encontro cuenta configurada para este
								// metodo de pago
								lblMensajeValidacion.setValue("No se encuentra registrada la cuenta para el método de pago: "+ metPago.getMetodo() + "!!!");
								bContabilizado = false;
								break;
							}
						}
						// grabar enlace recibo caja con recibo jde para
						// documento domestico
						if (bContabilizado && bDomGenerado) {
	
							bContabilizado = recCtrl.fillEnlaceMcajaJde(s, tx, iNumrec, sCodComp,iNoDocumentoDom, iNoBatch, iCajaId, f55ca01.getId().getCaco(), "A", "DCO");
							
							if (!bContabilizado) {
								lblMensajeValidacion.setValue("No se pudo Grabar el enlace entre Recibo y JDE para asiento Domestico!!!");
							}
						}else
						// grabar enlace recibo caja con recibo jde foraneo
						if (bContabilizado && bForGenerado) {
							
						
							bContabilizado = recCtrl.fillEnlaceMcajaJde(s, tx, iNumrec, sCodComp,iNoDocumentoDom, iNoBatch, iCajaId, f55ca01.getId().getCaco(), "A", "DCO");
							
							if (!bContabilizado) {
								lblMensajeValidacion.setValue("No se pudo Grabar el enlace entre Recibo y JDE para asiento Foraneo!!!");
							}
						}
						// Grabar Batch
						if (bContabilizado) {
							
							long montoBatch = Long.parseLong( String.format("%1$.2f", dTotalAplicar ).replace(".", "") ) ;
							
							if(iNoDocumentoFor>0 && iNoDocumentoDom>0){
								 
								bContabilizado = recCtrl.registrarBatchA92Custom(s, dtFecha, valoresJdeIns[8], iNoBatch, montoBatch/* iTotalTransaccion*/, vaut[0].getId().getLogin(), 2, "RCONTADO", valoresJdeIns[9] );
								
							}else{
								bContabilizado = recCtrl.registrarBatchA92Custom(s, dtFecha, valoresJdeIns[8], iNoBatch, montoBatch /*iTotalTransaccion*/, vaut[0].getId().getLogin(), 1, "RCONTADO", valoresJdeIns[9] );
							
							}
						}
					} else {
						// No se encontro cuenta de venta de contado para la unidad de negocio ####
						lblMensajeValidacion.setValue("No se encontro cuenta de caja transitoria para la unidad de negocio de la factura "+ hFac.getCodunineg()+ " "+ hFac.getUnineg());
						bContabilizado = false;
					}
				} else {
					// no se pudo actualizar el no de batch
					lblMensajeValidacion.setValue("No se pudo actualizar el No. de batch!!!");
				}
				m.remove("iContadorDom");
				m.remove("iContadorFor");
			} else {
				// no se encontro el no de batch
				lblMensajeValidacion.setValue("No se encontró No. de batch!!!");
				bContabilizado = false;
			}
			
			LogCajaService.CreateLog("generarAsientosDevolucion", "INFO", "generarAsientosDevolucion - FIN");
		
		} catch (Exception ex) {
			LogCajaService.CreateLog("generarAsientosDevolucion", "ERR", ex.getMessage());			
			bContabilizado = false;
			lblMensajeValidacion.setValue("Asientos de diario por devolucion no generados, por favor intente nuevamente");
		}
		return bContabilizado;
	}
/***************************************************************************************************
*                       REALIZAR ASIENTOS DE COMPRA/VENTA DE FICHA
 * @throws Exception 
* ************************************************************************************************/
	public boolean generarAsientosFichaCV(Session session, Transaction tx, List lstPagoFicha, Vf55ca01 f55ca01, Hfactura hFac,
			String sTipoCliente, Vautoriz vaut, String[] sCuentaMetodo, String[] sCtaMetodoDom, int iNumFicha,int iNumrec, Date dtFecha) throws Exception {
		int iContadorDom = 0, iContadorFor = 0, iNoBatch = 0, iNoDocDomestico = 0, iNoDocForaneo = 0;
		boolean bContabilizado = false;
		ReciboCtrl recCtrl = new ReciboCtrl();
		Divisas d = new Divisas();
		MetodosPago mPago = null;
		String sConcepto = "";
		String[] valoresJdeIns = (String[]) m.get("valoresJDEInsContado");
		
		try {
			
			sConcepto = "Ficha No:" + iNumFicha + " Ca:" + f55ca01.getId().getCaid() + " Com:" + hFac.getCodcomp();
		
			iNoBatch = Divisas.numeroSiguienteJdeE1(  );
			iNoDocForaneo = Divisas.numeroSiguienteJdeE1Custom(valoresJdeNumeracion[8],valoresJdeNumeracion[9] );

			mPago = (MetodosPago) lstPagoFicha.get(0);
			 
			String msgLogs = "Generacion Ficha de Cambio " +  sConcepto;
			String tipobatch = valoresJdeIns[1];
			
			
			iContadorFor++;
			bContabilizado = recCtrl.registrarAsientoDiarioLogs(session, msgLogs, dtFecha, hFac.getCodsuc(),tipobatch, iNoDocForaneo, (iContadorFor) * 1.0,iNoBatch, 
								sCuentaMetodo[0], sCuentaMetodo[1], sCuentaMetodo[3], sCuentaMetodo[4],sCuentaMetodo[5],valoresJdeIns[2], mPago.getMoneda(), 
								d.pasarAentero(d.roundDouble4(mPago.getMonto())), sConcepto, vaut.getId().getLogin(), vaut.getId().getCodapp(), mPago.getTasa(), "", "Deb caja dolares "
							+ d.roundDouble4(mPago.getMonto()), sCuentaMetodo[2],"","",valoresJdeIns[3], sCuentaMetodo[2],valoresJdeIns[4], 0);
			if (bContabilizado) {
				bContabilizado = recCtrl.registrarAsientoDiarioLogs(session, msgLogs, dtFecha, hFac.getCodsuc(),tipobatch,iNoDocForaneo, (iContadorFor) * 1.0, iNoBatch,
								sCuentaMetodo[0], sCuentaMetodo[1], sCuentaMetodo[3], sCuentaMetodo[4], sCuentaMetodo[5], valoresJdeIns[5], mPago.getMoneda(),
								d.pasarAentero(d.roundDouble4(mPago.getEquivalente())), sConcepto,vaut.getId().getLogin(), vaut.getId().getCodapp(), mPago.getTasa(),
								"", "Deb caja dolares " + d.roundDouble4(mPago.getMonto()), sCuentaMetodo[2],"","",valoresJdeIns[6], sCuentaMetodo[2],valoresJdeIns[4], 
								d.pasarAentero(d.roundDouble4(mPago.getMonto())) );
				if (bContabilizado) {
					bContabilizado = recCtrl.registrarAsientoDiarioLogs(session, msgLogs, dtFecha, hFac.getCodsuc(), tipobatch, iNoDocForaneo,(iContadorFor + 1) * 1.0, iNoBatch, 
									sCtaMetodoDom[0],sCtaMetodoDom[1],sCtaMetodoDom[3],sCtaMetodoDom[4],sCtaMetodoDom[5], valoresJdeIns[2], mPago.getMoneda(), (-1)* (d.pasarAentero(d.roundDouble4(mPago.getMonto()))), sConcepto,vaut.getId().getLogin(), vaut.getId().getCodapp(),
									mPago.getTasa(), "","Cred caja cordobas " + d.roundDouble4(mPago.getMonto()),
									sCtaMetodoDom[2],"","",valoresJdeIns[3],sCtaMetodoDom[2],valoresJdeIns[4], 0);
					if (bContabilizado) {
						bContabilizado = recCtrl.registrarAsientoDiarioLogs(session, msgLogs, dtFecha, hFac.getCodsuc(), tipobatch, iNoDocForaneo,(iContadorFor + 1) * 1.0, iNoBatch,sCtaMetodoDom[0], sCtaMetodoDom[1], sCtaMetodoDom[3], sCtaMetodoDom[4],sCtaMetodoDom[5],
								valoresJdeIns[5], mPago.getMoneda(), (-1)* (d.pasarAentero(d.roundDouble4(mPago.getEquivalente()))),sConcepto, vaut.getId().getLogin(), vaut
										.getId().getCodapp(), mPago.getTasa(),"", "Cred caja cordobas "+ d.roundDouble4(mPago.getMonto()), 
										sCtaMetodoDom[2],"","",valoresJdeIns[6],sCtaMetodoDom[2],valoresJdeIns[4],
										((-1)* (d.pasarAentero(d.roundDouble4(mPago.getMonto()))))
										);
						iContadorFor++;
						
						if(bContabilizado){
							
						
							bContabilizado = recCtrl.fillEnlaceMcajaJde(session,tx,iNumFicha, hFac.getCodcomp(), iNoDocForaneo, iNoBatch,f55ca01.getId().getCaid(),f55ca01.getId().getCaco(),"A","FCV");
							
							if(bContabilizado){

								long iTotalTransaccion = d.pasarAentero(d.roundDouble4(mPago.getMonto())) ;
								bContabilizado = recCtrl.registrarBatchA92Custom(session, dtFecha, valoresJdeIns[8], iNoBatch, iTotalTransaccion, vaut.getId().getLogin(), 1, "RCONTADO", valoresJdeIns[9] );
							
							}else {
								strMensajeValidacion = recCtrl.getError() + " <BR/> DETALLE: " + recCtrl.getErrorDetalle();		
								throw new Exception(strMensajeValidacion);								
							}
						}else{
							strMensajeValidacion = d.getError() + " <BR/> DETALLE: " + d.getErrorDetalle();		
							throw new Exception(strMensajeValidacion);		
						}		
						
					} else {
						strMensajeValidacion = d.getError() + " <BR/> DETALLE: " + d.getErrorDetalle();		
						throw new Exception(strMensajeValidacion);
					}
				} else {
					strMensajeValidacion = d.getError() + " <BR/> DETALLE: " + d.getErrorDetalle();		
					throw new Exception(strMensajeValidacion);
				}
			} else {
				strMensajeValidacion = d.getError() + " <BR/> DETALLE: " + d.getErrorDetalle();		
				throw new Exception(strMensajeValidacion);	
			}
		} catch (Exception ex) {
			
			bContabilizado = false;
			
			strMensajeValidacion = "No se pudo realizar la operacion!!!";		
			lblMensajeValidacion.setValue(strMensajeValidacion);							
		
			throw new Exception(strMensajeValidacion);
			
		}
		return bContabilizado;
	}

/** *****************************************************************************************
 *                         GENERAR ASIENTOS PARA CANCELAR FACTURA DE CONTADO
 * @throws Exception 
 * ******************************************************************************************/
	public boolean generarAsientos(Session s, Transaction tx,  int iCajaId, String sCodComp, double dTotalAplicar, 
			                       List lstFacturasSelected,List lstMetodosPago, int iNumrec, boolean bHayFicha,List lstPagoFicha, 
			                       String sMonedaBase, Date dtFecha) throws Exception {
		

		boolean bContabilizado = true, bForGenerado = false, bDomGenerado = false,bHayDiferencial = false;
		Divisas d = new Divisas();
		ReciboCtrl recCtrl = new ReciboCtrl();
		MetodosPago metPago = null;
		EmpleadoCtrl empCtrl = new EmpleadoCtrl();
		Vautoriz[] vaut = null;
		
		
		int iNoBatch = 0;		
		int iNoDocumentoFor = 0;

		
		String sConcepto = "";
		int iCantDocs = 0;
		long iTotalTransaccion = 0 ;
		
		//int iContadorDom = 0;
		int iContadorFor = 0;
		int iCinf1 = 0;
		int iCinf2 = 0;
		
		
		String[] sCuentaDC = null, sCuentaVenta = null, sCuentaCaja = null;
		double dMonto1 = 0.0,dMonto2 = 0.0,dMontoDif = 0.0;
		boolean bTraslado = false;
		Trasladofac tf = null;		
		Hfactura hFac = null;
		int i = 0, j = 0;
		
		strMensajeValidacion = "";
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss sss");
		
		int posicionProceso = 1;
		String nombreEvento = "";
		String termina = "" ;
		String dtaLog =  " |||| generarAsientos: Caja: "+iCajaId+" ||  Recibo:"+iNumrec+" || Compania: "+sCodComp+"|| Monto: "+dTotalAplicar+" ||";
		
		try {
			
			LogCajaService.CreateLog("generarAsientos", "QRY", "generarAsientos-INICIO");		
			m.put("iContadorFor", iContadorFor);
			
			Vf55ca01 f55ca01 = (Vf55ca01) ((List) m.get("lstCajas")).get(0);
			vaut = (Vautoriz[]) m.get("sevAut");
			 
			sConcepto = "R:" + iNumrec + " Ca:" + f55ca01.getId().getCaid() + " Com:" + ((Hfactura)lstFacturasSelected.get(0)).getCodcomp();
			iTotalTransaccion = iTotalTransaccion + d.pasarAenteroLong( dTotalAplicar );
		
			iNoBatch = Divisas.numeroSiguienteJdeE1();
		
			if (iNoBatch > 0) {
				if (bContabilizado) {					
										
					while (i < lstFacturasSelected.size()){
						//&& ======= seguro contra ciclo infinito
						iCinf1++;
						if(iCinf1>99){
							strMensajeValidacion = "Proceso no completado, aplicación de pagos inconsistente en recibo 1";								
							throw new Exception(strMensajeValidacion);
						}
						//&& ======= seguro contra ciclo infinito
						
						hFac = (Hfactura)lstFacturasSelected.get(i);
						//---- Verificar si factura es un traslado o es local y determinar cuenta Venta de Contado.
						int iCaid = 0;
						TrasladoCtrl tcCtrl = new TrasladoCtrl();
						
						
						nombreEvento = " TrasladoFactura:(I) ["+sdf.format(new Date())+"] << -- >>" ;
						tf = tcCtrl.buscarTrasladofac(s, hFac, f55ca01.getId().getCaid(), 0, "P");
						nombreEvento += "(F) ["+sdf.format(new Date())+"]";
//						LogCrtl.sendLogInfo("-------"+sdf.format(new Date())+ " Paso "+(++posicionProceso) + dtaLog + " "+nombreEvento);
						
						if(tf!=null){
							 bTraslado = true; 
							 iCaid = tf.getCaidprop();
						}else{
							 iCaid = f55ca01.getId().getCaid();
							 bTraslado = false;
						}
					
						
						// obtener cuenta de venta de contado
						nombreEvento = " obtenerCuentaVenta:(I) ["+sdf.format(new Date())+"] << -- >>" ;
						sCuentaVenta = d.obtenerCuentaVenta(iCaid, hFac.getCodcomp(), hFac.getCodunineg(), s, tx);
						nombreEvento += "(F) ["+sdf.format(new Date())+"]";
						if (sCuentaVenta != null) 
						{
							j=0;
							while (j < lstMetodosPago.size()) {
								iCinf2++;
								if(iCinf2>99){
									strMensajeValidacion = "Proceso no completado, aplicación de pagos inconsistente en recibo 2";										
									throw new Exception(strMensajeValidacion);
								}//seguro contra ciclo infinito
								
								metPago = (MetodosPago) lstMetodosPago.get(j);
								
								//============ Determinar si es un traslado de voucher's y de afiliados.
								if(metPago.getMetodo().equals(MetodosPagoCtrl.TARJETA) && metPago.getVmanual().equals("1") && 
											metPago.getICaidpos() != f55ca01.getId().getCaid()){
									if(m.get("fdc_ObjTrasladoFacTrasladoPOS")==null){
										strMensajeValidacion = "Error al leer datos de traslado de factura en registro de Traslado de POS";
										bContabilizado = false; 
										throw new Exception(strMensajeValidacion);
									}else{
										Trasladofac tfPos = (Trasladofac)m.get("fdc_ObjTrasladoFacTrasladoPOS");
										sCuentaCaja = d.obtenerCuentaCaja(tfPos.getCaidorig(),tfPos.getCodcomp(),
																		  metPago.getMetodo(),metPago.getMoneda(),
																		  s,tx,null,null);
									}
								}else{
									sCuentaCaja = d.obtenerCuentaCaja(f55ca01.getId().getCaid(),
													hFac.getCodcomp(),metPago.getMetodo(),
													metPago.getMoneda(),s,tx,null,null);	
								}

								double dTasaCalculada = hFac.getCpendiente()/hFac.getDpendiente();
								 
								if (sCuentaCaja != null) {
									if (!hFac.getMoneda().equals(sMonedaBase)) {// factura en moneda foranea
										if (metPago.getMoneda().equals(sMonedaBase)) {//F:USD P:COR pago en moneda domestica activar compra venta
																						
											if(d.roundDouble4( metPago.getEquivalente() ) > d.roundDouble4(hFac.getDpendiente())){//el pago es mayor al monto de la factura
												
												if (!bForGenerado) {
													// leer el numero de documento foraneo a utilizar para el asiento de diario	
													
													nombreEvento = " leerActualizarNoDocJDE - iNoDocumentoFor:(I) ["+sdf.format(new Date())+"] << -- >>" ;
													iNoDocumentoFor = Divisas.numeroSiguienteJdeE1Custom(valoresJdeNumeracion[8],valoresJdeNumeracion[9] );
													
													nombreEvento += "(F) ["+sdf.format(new Date())+"]";
													
													if (iNoDocumentoFor > 0) {												
														bForGenerado = true;												
													} else {
														// No se pudo encontrar el No.  de documento
														strMensajeValidacion = d.getError() + " <BR/> DETALLE: " + d.getErrorDetalle();		
														lblMensajeValidacion.setValue(strMensajeValidacion);	
														bContabilizado = false;
														throw new Exception(strMensajeValidacion);
													}
												}

												iContadorFor = Integer.parseInt(m.get("iContadorFor").toString());
												
												//validar si hay diferencial cambiario

												if(metPago.getTasa().doubleValue()!=dTasaCalculada){
													bHayDiferencial = true;
													//partir el monto

													dMonto1 = d.roundDouble4(hFac.getDpendiente() * dTasaCalculada);
													dMonto2 = hFac.getDpendiente();
													dMontoDif = d.roundDouble4((hFac.getDpendiente()*(metPago.getTasa().doubleValue())) -  dMonto1);

													if (!bDomGenerado) {
														
														
														nombreEvento = " leerActualizarNoDocJDE - iNoDocumentoFor:(I) ["+sdf.format(new Date())+"] << -- >>" ;
														

														iNoDocumentoFor = Divisas.numeroSiguienteJdeE1Custom(valoresJdeNumeracion[8],valoresJdeNumeracion[9] );
														
														nombreEvento += "(F) ["+sdf.format(new Date())+"]";

														
														
														if (iNoDocumentoFor > 0) {
															bDomGenerado = true;
														} else {
															// No se pudo encontrar el No. de documento
															lblMensajeValidacion.setValue("No se encontró No. de Documento para Asiento Domestico!!!");
															strMensajeValidacion ="Asiento Domestico!!! " + d.getError() + " <BR/> DETALLE: " + d.getErrorDetalle();		
															
															bContabilizado = false;
															throw new Exception(strMensajeValidacion);
														}
													}
												}else{
													dMonto1 = hFac.getCpendiente();
													dMonto2 = hFac.getDpendiente();
												}
												
												iContadorFor++;
												//partir el monto en pago neto y dif cambiario
												bContabilizado = recCtrl.registrarAsientoDiarioLogs( s,  (dtaLog+" Proceso:"+posicionProceso),dtFecha, hFac.getCodsuc(),valoresJdeIns[1], iNoDocumentoFor,(iContadorFor) * 1.0,iNoBatch,sCuentaCaja[0],sCuentaCaja[1],sCuentaCaja[3],
																sCuentaCaja[4],sCuentaCaja[5],valoresJdeIns[2], hFac.getMoneda(), d.pasarAenteroLong(dMonto2),sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),
																hFac.getTasa(),"","Met: " + metPago.getMetodo()+" f:"+hFac.getNofactura(),
																sCuentaCaja[2],"","",valoresJdeIns[3],sCuentaCaja[2],valoresJdeIns[4], 0 );
												if (bContabilizado) {
													bContabilizado = recCtrl.registrarAsientoDiarioLogs( s,  (dtaLog+" Proceso:"+posicionProceso),dtFecha, hFac.getCodsuc(),valoresJdeIns[1], iNoDocumentoFor,(iContadorFor) * 1.0,iNoBatch,sCuentaCaja[0],sCuentaCaja[1],sCuentaCaja[3],
																		sCuentaCaja[4],sCuentaCaja[5],valoresJdeIns[5],hFac.getMoneda(),d.pasarAenteroLong(dMonto1),
																		sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),hFac.getTasa(),"","Met: " + metPago.getMetodo()+" f:"+hFac.getNofactura(),
																		sCuentaCaja[2],"","",sMonedaBase,sCuentaCaja[2],valoresJdeIns[4], d.pasarAenteroLong(dMonto2) );
													if (bContabilizado) {
														// asiento de diario para cuenta de venta de contado
														bContabilizado = recCtrl.registrarAsientoDiarioLogs( s,  (dtaLog+" Proceso:"+posicionProceso),dtFecha,  hFac.getCodsuc(),valoresJdeIns[1], iNoDocumentoFor,(iContadorFor + 1) * 1.0,iNoBatch,sCuentaVenta[0],sCuentaVenta[1],sCuentaVenta[3],sCuentaVenta[4],
																		sCuentaVenta[5],valoresJdeIns[2],hFac.getMoneda(), -1*( d.pasarAenteroLong(dMonto2) ), sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),
																		hFac.getTasa(),"","Met: " + metPago.getMetodo()+" f:"+hFac.getNofactura(),
																		sCuentaVenta[2],"","",valoresJdeIns[3],sCuentaVenta[2],valoresJdeIns[4], 0);
														if (bContabilizado) {
															bContabilizado = recCtrl.registrarAsientoDiarioLogs( s,  (dtaLog+" Proceso:"+posicionProceso),dtFecha, hFac.getCodsuc(),valoresJdeIns[1], iNoDocumentoFor,(iContadorFor + 1) * 1.0,iNoBatch,sCuentaVenta[0],sCuentaVenta[1],sCuentaVenta[3],sCuentaVenta[4],
																				sCuentaVenta[5],valoresJdeIns[5],hFac.getMoneda(),(-1)* (d.pasarAenteroLong(dMonto1)),sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),hFac.getTasa(),"",
																				"Met: " + metPago.getMetodo()+" f:"+hFac.getNofactura(),
																				sCuentaVenta[2],"","",sMonedaBase,sCuentaVenta[2],valoresJdeIns[4] , (-1*( d.pasarAenteroLong(dMonto2) ) ) );
															iContadorFor++;
															
															//validar si hay diferencial cambiario
															if(bHayDiferencial){
																

																
																//buscar cuenta para el dif cambiario
																if(sCuentaDC==null){

																	sCuentaDC = d.obtenerCuentaDifCambiario(iCaid, hFac.getCodcomp(), hFac.getCodunineg(), s, tx, null, null);
																}
																if(sCuentaDC!=null){
																	
																	//iContadorDom++;
																	iContadorFor++;
																	
																	bContabilizado = recCtrl.registrarAsientoDiarioLogs( s,  (dtaLog+" Proceso:"+posicionProceso),dtFecha, hFac.getCodsuc(),valoresJdeIns[1], iNoDocumentoFor,(iContadorFor/*iContadorDom*/) * 1.0,iNoBatch,sCuentaCaja[0],sCuentaCaja[1],sCuentaCaja[3],
																			sCuentaCaja[4],sCuentaCaja[5],valoresJdeIns[5],sMonedaBase,d.pasarAenteroLong(dMontoDif),
																			sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),BigDecimal.ZERO,"","Dif/Camb Met: " + metPago.getMetodo()+" f:"+hFac.getNofactura(),
																			sCuentaCaja[2],"","",sMonedaBase,sCuentaCaja[2],valoresJdeIns[7],0 );
																	
																	bContabilizado = recCtrl.registrarAsientoDiarioLogs( s,  (dtaLog+" Proceso:"+posicionProceso),dtFecha,  hFac.getCodsuc(), valoresJdeIns[1], iNoDocumentoFor,(iContadorFor /*iContadorDom*/ + 1) * 1.0,iNoBatch,sCuentaDC[0],sCuentaDC[1],sCuentaDC[3],sCuentaDC[4],
																			sCuentaDC[5],valoresJdeIns[5],sMonedaBase,(-1)* (d.pasarAenteroLong(dMontoDif)),sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),BigDecimal.ZERO,"",
																			"Dif/Camb Met: " + metPago.getMetodo()+" f:"+hFac.getNofactura(),
																			sCuentaDC[2],"","",sMonedaBase,sCuentaDC[2],valoresJdeIns[7], 0);
																	
																	//iContadorDom++;
																	iContadorFor++;
																	
																}else{
																	bContabilizado = false;
																	strMensajeValidacion = d.getError() + " <BR/> DETALLE: " + d.getErrorDetalle();																			
																	throw new Exception(strMensajeValidacion);
																}
															}

															m.put("iContadorFor",iContadorFor);
															
														} else {
															strMensajeValidacion = recCtrl.getError() + " <BR/> DETALLE: " + recCtrl.getErrorDetalle();																																	
															throw new Exception(strMensajeValidacion);
														}
													} else {
														strMensajeValidacion = recCtrl.getError() + " <BR/> DETALLE: " + recCtrl.getErrorDetalle();		
														throw new Exception(strMensajeValidacion);
													}
												} else {
													strMensajeValidacion = recCtrl.getError() + " <BR/> DETALLE: " + recCtrl.getErrorDetalle();		
													throw new Exception(strMensajeValidacion);
												}
												if (!bContabilizado) {
													throw new Exception("No se pudo contabilizar!!");
												}
												//poner en cero el monto de factura y restar el monto a aplicar
												metPago.setMonto(d.roundDouble4(metPago.getMonto() - hFac.getCpendiente()));
												metPago.setEquivalente(d.roundDouble4(metPago.getEquivalente() - hFac.getDpendiente()));
												hFac.setCpendiente(0);hFac.setDpendiente(0);
												
											}else if(d.roundDouble4(metPago.getEquivalente()) == d.roundDouble4(hFac.getDpendiente())){// el pago es igual al monto de la factura
												if (!bForGenerado) {
													// leer el numero de documento foraneo a utilizar para el asiento de diario	
													
													
													nombreEvento= " leerActualizarNoDocJDE: documento foraneo a utilizar para el asiento de diario (I) ["+sdf.format(new Date())+"] << -- >>" ;
													
													//iNoDocumentoFor = d.leerActualizarNoDocJDE();
													iNoDocumentoFor = Divisas.numeroSiguienteJdeE1Custom(valoresJdeNumeracion[8],valoresJdeNumeracion[9] );
													
													nombreEvento+= "(F) ["+sdf.format(new Date())+"]";
//													LogCrtl.sendLogInfo("-------"+sdf.format(new Date())+ "Paso "+(++posicionProceso) + dtaLog + " "+nombreEvento);
													
													if (iNoDocumentoFor > 0) {												
														bForGenerado = true;												
													} else {
														// No se pudo encontrar el No.  de documento
														strMensajeValidacion = d.getError() + " <BR/> DETALLE: " + d.getErrorDetalle();	
														bContabilizado = false;
														throw new Exception(strMensajeValidacion);
													}
												}
												

												iContadorFor = Integer.parseInt(m.get("iContadorFor").toString());
												
												if(metPago.getTasa().doubleValue()!=dTasaCalculada){
													bHayDiferencial = true;
													//partir el monto

													dMonto1 = d.roundDouble4(metPago.getEquivalente() * dTasaCalculada);
													dMonto2 = metPago.getEquivalente();
													dMontoDif = metPago.getMonto() -  dMonto1;
													// leer el numero de documento domestico a utilizar para el asiento de diario
													
													if (!bDomGenerado) {			
														
														nombreEvento= " leerActualizarNoDocJDE:  documento domestico a utilizar diferencial cambiario(I) ["+sdf.format(new Date())+"] << -- >>" ;
														
														//iNoDocumentoFor = d.leerActualizarNoDocJDE();
														iNoDocumentoFor = Divisas.numeroSiguienteJdeE1Custom(valoresJdeNumeracion[8],valoresJdeNumeracion[9] );
														
														nombreEvento+= "(F) ["+sdf.format(new Date())+"]";
//														LogCrtl.sendLogInfo("-------"+sdf.format(new Date())+ "Paso "+(++posicionProceso) + dtaLog + " "+nombreEvento);
														
														if (iNoDocumentoFor > 0) {
															bDomGenerado = true;
														} else {
															// No se pudo encontrar el No. de documento
															strMensajeValidacion = d.getError() + " <BR/> DETALLE: " + d.getErrorDetalle();		
															bContabilizado = false;
															throw new Exception(strMensajeValidacion);
														}
													}
												}else{
													dMonto1 = metPago.getMonto();
													dMonto2 = metPago.getEquivalente();
												}
												
												iContadorFor++;
												//partir el monto en pago neto y dif cambiario
																								
												bContabilizado = recCtrl.registrarAsientoDiarioLogs( s,  (dtaLog+" Proceso:"+posicionProceso),dtFecha, hFac.getCodsuc(),valoresJdeIns[1], iNoDocumentoFor,(iContadorFor) * 1.0,iNoBatch,sCuentaCaja[0],sCuentaCaja[1],sCuentaCaja[3],
																sCuentaCaja[4],sCuentaCaja[5],valoresJdeIns[2],hFac.getMoneda(),d.pasarAenteroLong(dMonto2),sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),
																/*new BigDecimal(d.roundDouble4(dTasaCalculada))*/ hFac.getTasa(),"","Met: " + metPago.getMetodo()+" f:"+hFac.getNofactura(),
																sCuentaCaja[2],"","",valoresJdeIns[3],sCuentaCaja[2],valoresJdeIns[4], 0);
												if (bContabilizado) {
													bContabilizado = recCtrl.registrarAsientoDiarioLogs( s,  (dtaLog+" Proceso:"+posicionProceso),dtFecha, hFac.getCodsuc(),valoresJdeIns[1], iNoDocumentoFor,(iContadorFor) * 1.0,iNoBatch,sCuentaCaja[0],sCuentaCaja[1],sCuentaCaja[3],
																		sCuentaCaja[4],sCuentaCaja[5],valoresJdeIns[5],hFac.getMoneda(),d.pasarAenteroLong(dMonto1),
																		sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),hFac.getTasa(),"","Met: " + metPago.getMetodo()+" f:"+hFac.getNofactura(),
																		sCuentaCaja[2],"","",sMonedaBase,sCuentaCaja[2],valoresJdeIns[4], d.pasarAenteroLong(dMonto2) );
													if (bContabilizado) {
														// asiento de diario para cuenta de venta de contado
														bContabilizado = recCtrl.registrarAsientoDiarioLogs( s,  (dtaLog+" Proceso:"+posicionProceso),dtFecha,  hFac.getCodsuc(),valoresJdeIns[1], iNoDocumentoFor,(iContadorFor + 1) * 1.0,iNoBatch,sCuentaVenta[0],sCuentaVenta[1],sCuentaVenta[3],sCuentaVenta[4],
																		sCuentaVenta[5],valoresJdeIns[2],hFac.getMoneda(),-1*(d.pasarAenteroLong(dMonto2)), sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),
																		hFac.getTasa(),"","Met: " + metPago.getMetodo()+" f:"+hFac.getNofactura(),
																		sCuentaVenta[2],"","",valoresJdeIns[3],sCuentaVenta[2],valoresJdeIns[4], 0 );
														if (bContabilizado) {
															bContabilizado = recCtrl.registrarAsientoDiarioLogs( s,  (dtaLog+" Proceso:"+posicionProceso),dtFecha, hFac.getCodsuc(),valoresJdeIns[1], iNoDocumentoFor,(iContadorFor + 1) * 1.0,iNoBatch,sCuentaVenta[0],sCuentaVenta[1],sCuentaVenta[3],sCuentaVenta[4],
																				sCuentaVenta[5],valoresJdeIns[5],hFac.getMoneda(),(-1)* (d.pasarAenteroLong(dMonto1)),sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),hFac.getTasa(),"",
																				"Met: " + metPago.getMetodo()+" f:"+hFac.getNofactura(),
																				sCuentaVenta[2],"","",sMonedaBase,sCuentaVenta[2],valoresJdeIns[4] , (-1*(d.pasarAenteroLong(dMonto2))) );
															
															
															iContadorFor++;
//															LogCrtl.sendLogInfo("-------"+sdf.format(new Date())+ "Paso "+(++posicionProceso) + dtaLog + "(F) partir el monto en pago neto y dif cambiario" );
															
															//validar si hay diferencial cambiario
															if(bHayDiferencial){
																//buscar cuenta para el dif cambiario
																if(sCuentaDC==null){
																	sCuentaDC = d.obtenerCuentaDifCambiario(iCaid, hFac.getCodcomp(), hFac.getCodunineg(), s, tx, null, null);
																}
																if(sCuentaDC!=null){

																	
																	iContadorFor++;
																	
//																	LogCrtl.sendLogInfo("-------"+sdf.format(new Date())+ "Paso "+(++posicionProceso) + dtaLog + "(I) aplicar diferencial cambiario " );
																	
																	bContabilizado = recCtrl.registrarAsientoDiarioLogs( s,  (dtaLog+" Proceso:"+posicionProceso),dtFecha,  hFac.getCodsuc(),valoresJdeIns[1], iNoDocumentoFor,(iContadorFor/*iContadorDom*/) * 1.0,iNoBatch,sCuentaCaja[0],sCuentaCaja[1],sCuentaCaja[3],
																			sCuentaCaja[4],sCuentaCaja[5],valoresJdeIns[5],sMonedaBase,d.pasarAenteroLong(dMontoDif),
																			sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),BigDecimal.ZERO,"","Dif/Camb Met: " + metPago.getMetodo()+" f:"+hFac.getNofactura(),
																			sCuentaCaja[2],"","",sMonedaBase,sCuentaCaja[2],valoresJdeIns[7], 0);
																	
																	bContabilizado = recCtrl.registrarAsientoDiarioLogs( s,  (dtaLog+" Proceso:"+posicionProceso),dtFecha, hFac.getCodsuc(), valoresJdeIns[1], iNoDocumentoFor,(iContadorFor/*iContadorDom*/ + 1) * 1.0,iNoBatch,sCuentaDC[0],sCuentaDC[1],sCuentaDC[3],sCuentaDC[4],
																			sCuentaDC[5],valoresJdeIns[5],sMonedaBase,(-1)* (d.pasarAenteroLong(dMontoDif)),sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),BigDecimal.ZERO,"",
																			"Dif/Camb Met: " + metPago.getMetodo()+" f:"+hFac.getNofactura(),
																			sCuentaDC[2],"","",sMonedaBase,sCuentaDC[2],valoresJdeIns[7], 0 );
																	
																	//iContadorDom++;
																	iContadorFor++;
																	
//																	LogCrtl.sendLogInfo("-------"+sdf.format(new Date())+ "Paso "+(++posicionProceso) + dtaLog + "(I) aplicar diferencial cambiario" );
																	
																}else{
																	bContabilizado = false;																	
																	strMensajeValidacion = "cuenta de diferencial cambiario  " + d.getError() + " <BR/> DETALLE: " + d.getErrorDetalle();		
																	throw new Exception(strMensajeValidacion);
																}
															}
															//m.put("iContadorDom",iContadorDom);
															m.put("iContadorFor",iContadorFor);
														} else {
															strMensajeValidacion = recCtrl.getError() + " <BR/> DETALLE: " + recCtrl.getErrorDetalle();		
															throw new Exception(strMensajeValidacion);
														}
													} else {
														strMensajeValidacion = recCtrl.getError() + " <BR/> DETALLE: " + recCtrl.getErrorDetalle();		
														throw new Exception(strMensajeValidacion);
													}
												} else {
													strMensajeValidacion = recCtrl.getError() + " <BR/> DETALLE: " + recCtrl.getErrorDetalle();		
													throw new Exception(strMensajeValidacion);
												}
												if (!bContabilizado) {
													throw new Exception("No se pudo contabilizar el registro!!");
												}
												//poner en cero el monto de factura y el monto a aplicar
												metPago.setMonto(0);
												metPago.setEquivalente(0);
												hFac.setCpendiente(0);hFac.setDpendiente(0);
												
											}else{// el pago es menor al monto de la factura
												if (!bForGenerado) {
													
													// leer el numero de documento foraneo a utilizar para el asiento de diario								
													
													//iNoDocumentoFor = d.leerActualizarNoDocJDE();										
													iNoDocumentoFor = Divisas.numeroSiguienteJdeE1Custom(valoresJdeNumeracion[8],valoresJdeNumeracion[9] );
													
													if (iNoDocumentoFor > 0) {												
														bForGenerado = true;												
													} else {
														// No se pudo encontrar el No.  de documento
														strMensajeValidacion = d.getError() + " <BR/> DETALLE: " + d.getErrorDetalle();		
														bContabilizado = false;
														throw new Exception(strMensajeValidacion);
													}
												}
												iContadorFor = Integer.parseInt(m.get("iContadorFor").toString());

												//validar si hay diferencial cambiario										
//												if(metPago.getTasa().doubleValue()!=hFac.getTasa().doubleValue()){
												if(metPago.getTasa().doubleValue()!=dTasaCalculada){
													bHayDiferencial = true;
													//partir el monto
//													dMonto1 = d.roundDouble4(metPago.getEquivalente() * hFac.getTasa().doubleValue());
													dMonto1 = d.roundDouble4(metPago.getEquivalente() * dTasaCalculada);
													dMonto2 = metPago.getEquivalente();
													dMontoDif = metPago.getMonto() -  dMonto1;
													// leer el numero de documento domestico a utilizar para el asiento de diario
													if (!bDomGenerado) {	
														
														
														nombreEvento= " leerActualizarNoDocJDE:(I) ["+sdf.format(new Date())+"] << -- >>" ;
														
														//iNoDocumentoFor = d.leerActualizarNoDocJDE();
														iNoDocumentoFor = Divisas.numeroSiguienteJdeE1Custom(valoresJdeNumeracion[8],valoresJdeNumeracion[9] );
														
														nombreEvento+= "(F) ["+sdf.format(new Date())+"]";
//														LogCrtl.sendLogInfo("-------"+sdf.format(new Date())+ "Paso "+(++posicionProceso) + dtaLog + " "+nombreEvento);
														
														if (iNoDocumentoFor > 0) {
															bDomGenerado = true;
														} else {
															// No se pudo encontrar el No. de documento
															strMensajeValidacion = d.getError() + " <BR/> DETALLE: " + d.getErrorDetalle();		
															
															bContabilizado = false;
															throw new Exception(strMensajeValidacion);
														}
													}
												}else{
													dMonto1 = metPago.getMonto();
													dMonto2 = metPago.getEquivalente();
												}
												
												iContadorFor++;
												//partir el monto en pago neto y dif cambiario
												
//												LogCrtl.sendLogInfo("-------"+sdf.format(new Date())+ "Paso "+(++posicionProceso) + dtaLog + "(I) partir el monto en pago neto y dif cambiario" );
												
												bContabilizado = recCtrl.registrarAsientoDiarioLogs( s,  (dtaLog+" Proceso:"+posicionProceso),dtFecha, hFac.getCodsuc(),valoresJdeIns[1], iNoDocumentoFor,(iContadorFor) * 1.0,iNoBatch,sCuentaCaja[0],sCuentaCaja[1],sCuentaCaja[3],
																sCuentaCaja[4],sCuentaCaja[5],valoresJdeIns[2],hFac.getMoneda(),d.pasarAenteroLong(dMonto2),sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),
																hFac.getTasa(),"","Met: " + metPago.getMetodo()+" f:"+hFac.getNofactura(),
																sCuentaCaja[2],"","",valoresJdeIns[3],sCuentaCaja[2],valoresJdeIns[4], 0 );
												if (bContabilizado) {
													bContabilizado = recCtrl.registrarAsientoDiarioLogs( s,  (dtaLog+" Proceso:"+posicionProceso),dtFecha, hFac.getCodsuc(),valoresJdeIns[1], iNoDocumentoFor,(iContadorFor) * 1.0,iNoBatch,sCuentaCaja[0],sCuentaCaja[1],sCuentaCaja[3],
																		sCuentaCaja[4],sCuentaCaja[5],valoresJdeIns[5],hFac.getMoneda(),d.pasarAenteroLong(dMonto1),
																		sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),hFac.getTasa(),"","Met: " + metPago.getMetodo()+" f:"+hFac.getNofactura(),
																		sCuentaCaja[2],"","",sMonedaBase,sCuentaCaja[2],valoresJdeIns[4], d.pasarAenteroLong(dMonto2) );
													if (bContabilizado) {
														// asiento de diario para cuenta de venta de contado
														bContabilizado = recCtrl.registrarAsientoDiarioLogs( s,  (dtaLog+" Proceso:"+posicionProceso),dtFecha, hFac.getCodsuc(),valoresJdeIns[1], iNoDocumentoFor,(iContadorFor + 1) * 1.0,iNoBatch,sCuentaVenta[0],sCuentaVenta[1],sCuentaVenta[3],sCuentaVenta[4],
																		sCuentaVenta[5],valoresJdeIns[2],hFac.getMoneda(),-1*(d.pasarAenteroLong(dMonto2)), sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),
																		hFac.getTasa(),"","Met: " + metPago.getMetodo()+" f:"+hFac.getNofactura(),
																		sCuentaVenta[2],"","",valoresJdeIns[3],sCuentaVenta[2],valoresJdeIns[4], 0 );
														if (bContabilizado) {
															bContabilizado = recCtrl.registrarAsientoDiarioLogs( s,  (dtaLog+" Proceso:"+posicionProceso),dtFecha, hFac.getCodsuc(),valoresJdeIns[1], iNoDocumentoFor,(iContadorFor + 1) * 1.0,iNoBatch,sCuentaVenta[0],sCuentaVenta[1],sCuentaVenta[3],sCuentaVenta[4],
																				sCuentaVenta[5],valoresJdeIns[5],hFac.getMoneda(),(-1)* (d.pasarAenteroLong(dMonto1)),sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),hFac.getTasa(),"",
																				"Met: " + metPago.getMetodo()+" f:"+hFac.getNofactura(),
																				sCuentaVenta[2],"","",sMonedaBase,sCuentaVenta[2],valoresJdeIns[4] , ((-1)* (d.pasarAenteroLong(dMonto2))) );
															iContadorFor++;
															
//															LogCrtl.sendLogInfo("-------"+sdf.format(new Date())+ "Paso "+(++posicionProceso) + dtaLog + "(F) partir el monto en pago neto y dif cambiario" );

															
															
															//validar si hay diferencial cambiario
															if(bHayDiferencial){
																//buscar cuenta para el dif cambiario
																if(sCuentaDC==null){
																	sCuentaDC = d.obtenerCuentaDifCambiario(iCaid, hFac.getCodcomp(), hFac.getCodunineg(), s, tx, null, null);
																}
																if(sCuentaDC!=null){
																	
																	iContadorFor++;
																	//iContadorDom++;
																	
																	bContabilizado = recCtrl.registrarAsientoDiarioLogs( s,  (dtaLog+" Proceso:"+posicionProceso),dtFecha, hFac.getCodsuc(),valoresJdeIns[1], iNoDocumentoFor,(iContadorFor/*iContadorDom*/) * 1.0,iNoBatch,sCuentaCaja[0],sCuentaCaja[1],sCuentaCaja[3],
																			sCuentaCaja[4],sCuentaCaja[5],valoresJdeIns[5],sMonedaBase,d.pasarAenteroLong(dMontoDif),
																			sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),BigDecimal.ZERO,"","Dif/Camb Met: " + metPago.getMetodo()+" f:"+hFac.getNofactura(),
																			sCuentaCaja[2],"","",sMonedaBase,sCuentaCaja[2],valoresJdeIns[7], 0);
																	
																	bContabilizado = recCtrl.registrarAsientoDiarioLogs( s,  (dtaLog+" Proceso:"+posicionProceso),dtFecha, hFac.getCodsuc(), valoresJdeIns[1], iNoDocumentoFor,(iContadorFor/*iContadorDom*/ + 1) * 1.0,iNoBatch,sCuentaDC[0],sCuentaDC[1],sCuentaDC[3],sCuentaDC[4],
																			sCuentaDC[5],valoresJdeIns[5],sMonedaBase,(-1)* (d.pasarAenteroLong(dMontoDif)),sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),BigDecimal.ZERO,"",
																			"Dif/Camb Met: " + metPago.getMetodo()+" f:"+hFac.getNofactura(),
																			sCuentaDC[2],"","",sMonedaBase,sCuentaDC[2],valoresJdeIns[7], 0);
																	
																	//iContadorDom++;
																	iContadorFor++;
																	
																}else{
																	bContabilizado = false;
																	strMensajeValidacion = "Diferencial Cambiario " + d.getError() + " <BR/> DETALLE: " + d.getErrorDetalle();		
																																		
																	throw new Exception(strMensajeValidacion);
																}
															}
															//m.put("iContadorDom",iContadorDom);
															m.put("iContadorFor",iContadorFor);
														} else {
															strMensajeValidacion = recCtrl.getError() + " <BR/> DETALLE: " + recCtrl.getErrorDetalle();		
															throw new Exception(strMensajeValidacion);
														}
													} else {
														strMensajeValidacion = recCtrl.getError() + " <BR/> DETALLE: " + recCtrl.getErrorDetalle();		
														throw new Exception(strMensajeValidacion);
													}
												} else {
													strMensajeValidacion = recCtrl.getError() + " <BR/> DETALLE: " + recCtrl.getErrorDetalle();		
													throw new Exception(strMensajeValidacion);
												}
												if (!bContabilizado) {
													throw new Exception("No se pudo contabilizar!!");
												}
												//poner en cero el monto de pagos y poner el monto pendiente
												hFac.setCpendiente(d.roundDouble4(hFac.getCpendiente() - dMonto1));hFac.setDpendiente(d.roundDouble4(hFac.getDpendiente() - dMonto2));
												metPago.setMonto(0);
												metPago.setEquivalente(0);
												
											
											}
										
										}else{// F:USD P:USD el pago es en la misma moneda de la factura
											
											if(d.roundDouble4(metPago.getMonto()) > d.roundDouble4(hFac.getDpendiente())){
												
												// comprobar si generar el no. de documento
												if (!bForGenerado) {
													
													// leer el numero de documento foraneo a utilizar para el asiento de diario
													
													//iNoDocumentoFor = d.leerActualizarNoDocJDE();
													iNoDocumentoFor = Divisas.numeroSiguienteJdeE1Custom(valoresJdeNumeracion[8],valoresJdeNumeracion[9] );
													
													if (iNoDocumentoFor > 0) {												
														bForGenerado = true;											
													} else {
														// No se pudo encontrar el No. de documento
														strMensajeValidacion = d.getError() + " <BR/> DETALLE: " + d.getErrorDetalle();		
														
														bContabilizado = false;
														throw new Exception(strMensajeValidacion);
													}
												}
												//
												//iContadorDom = Integer.parseInt(m.get("iContadorDom").toString());
												iContadorFor = Integer.parseInt(m.get("iContadorFor").toString());
												iContadorFor++;
												dMonto1 = d.roundDouble4(hFac.getDpendiente());
												dMonto2 = d.roundDouble4(hFac.getCpendiente());
												
//												LogCrtl.sendLogInfo("-------"+sdf.format(new Date())+ "Paso "+(++posicionProceso) + dtaLog + "(I) asiento de diario para cuenta de venta de contado Factura Dolares pago dolares" );
												
												bContabilizado = recCtrl.registrarAsientoDiarioLogs( s,  (dtaLog+" Proceso:"+posicionProceso),dtFecha, hFac.getCodsuc(),valoresJdeIns[1],iNoDocumentoFor,(iContadorFor) * 1.0,iNoBatch,sCuentaCaja[0],sCuentaCaja[1],sCuentaCaja[3],
																	sCuentaCaja[4],sCuentaCaja[5],valoresJdeIns[2],metPago.getMoneda(),d.pasarAenteroLong(dMonto1),
																	sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),hFac.getTasa(),"","Met: " + metPago.getMetodo()+" f:"+hFac.getNofactura(),
																	sCuentaCaja[2],"","",valoresJdeIns[3],sCuentaCaja[2],valoresJdeIns[4], 0);
												if (bContabilizado) {
													bContabilizado = recCtrl.registrarAsientoDiarioLogs( s,  (dtaLog+" Proceso:"+posicionProceso),dtFecha, hFac.getCodsuc(),valoresJdeIns[1],iNoDocumentoFor,(iContadorFor) * 1.0,iNoBatch,sCuentaCaja[0],sCuentaCaja[1],sCuentaCaja[3],
																		sCuentaCaja[4],sCuentaCaja[5],valoresJdeIns[5],metPago.getMoneda(),d.pasarAenteroLong(dMonto2),
																		sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),hFac.getTasa(),"","Met: " + metPago.getMetodo()+" f:"+hFac.getNofactura(),
																		sCuentaCaja[2],"","",sMonedaBase,sCuentaCaja[2],valoresJdeIns[4], d.pasarAenteroLong(dMonto1) );
													if (bContabilizado) {
														// asiento de diario para cuenta de venta de contado
														bContabilizado = recCtrl.registrarAsientoDiarioLogs( s,  (dtaLog+" Proceso:"+posicionProceso),dtFecha, hFac.getCodsuc(),valoresJdeIns[1], iNoDocumentoFor,(iContadorFor + 1) * 1.0,iNoBatch,sCuentaVenta[0],sCuentaVenta[1],sCuentaVenta[3],sCuentaVenta[4],
																			sCuentaVenta[5],valoresJdeIns[2],metPago.getMoneda(),(-1)* (d.pasarAenteroLong(dMonto1)),
																			sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),hFac.getTasa(),"","Met: " + metPago.getMetodo()+" f:"+hFac.getNofactura(),
																			sCuentaVenta[2],"","",valoresJdeIns[3],sCuentaVenta[2],valoresJdeIns[4], 0 );
														if (bContabilizado) {
															bContabilizado = recCtrl.registrarAsientoDiarioLogs( s,  (dtaLog+" Proceso:"+posicionProceso),dtFecha, hFac.getCodsuc(),valoresJdeIns[1], iNoDocumentoFor,(iContadorFor + 1) * 1.0,iNoBatch,sCuentaVenta[0],sCuentaVenta[1],sCuentaVenta[3],sCuentaVenta[4],
																			sCuentaVenta[5],valoresJdeIns[5],metPago.getMoneda(),(-1)* (d.pasarAenteroLong(dMonto2)),
																			sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),hFac.getTasa(),"","Met: " + metPago.getMetodo()+" f:"+hFac.getNofactura(),
																			sCuentaVenta[2],"","",sMonedaBase,sCuentaVenta[2],valoresJdeIns[4], ((-1)* (d.pasarAenteroLong(dMonto1))) );
															iContadorFor++;
															
															//m.put("iContadorDom",iContadorDom);
															m.put("iContadorFor",iContadorFor);
														} else {														
															strMensajeValidacion = recCtrl.getError() + " <BR/> DETALLE: " + recCtrl.getErrorDetalle();		
															throw new Exception(strMensajeValidacion);
														}
													} else {														
														strMensajeValidacion = recCtrl.getError() + " <BR/> DETALLE: " + recCtrl.getErrorDetalle();		
														throw new Exception(strMensajeValidacion);
													}
												} else {
													strMensajeValidacion = recCtrl.getError() + " <BR/> DETALLE: " + recCtrl.getErrorDetalle();		
													throw new Exception(strMensajeValidacion);
												}
		
												//poner en cero el monto de factura y restar el monto a aplicar
												metPago.setMonto(d.roundDouble4(metPago.getMonto() - dMonto1));
												metPago.setEquivalente(d.roundDouble4(metPago.getEquivalente() - dMonto1));
												hFac.setCpendiente(0);hFac.setDpendiente(0);
												
											}else if(d.roundDouble4(metPago.getMonto()) == d.roundDouble4(hFac.getDpendiente())){// el pago es igual al monto de la factura
												// comprobar si generar el no. de documento
												if (!bForGenerado) {
													
													// leer el numero de documento foraneo a utilizar para el asiento de diario
													
													//iNoDocumentoFor = d.leerActualizarNoDocJDE();
													iNoDocumentoFor = Divisas.numeroSiguienteJdeE1Custom(valoresJdeNumeracion[8],valoresJdeNumeracion[9] );
													
													if (iNoDocumentoFor > 0) {												
														bForGenerado = true;											
													} else {
														// No se pudo encontrar el No. de documento
														strMensajeValidacion = d.getError() + " <BR/> DETALLE: " + d.getErrorDetalle();		
														
														bContabilizado = false;
														throw new Exception(strMensajeValidacion);
													}
												}
												//
												//iContadorDom = Integer.parseInt(m.get("iContadorDom").toString());
												iContadorFor = Integer.parseInt(m.get("iContadorFor").toString());
												iContadorFor++;
												
												//&& ====== 20112014: el monto del pago es igual al de la factura, utilizar los montos de la factura en vez de calcular de nuevo
												dMonto1 = hFac.getDpendiente() ; // dMonto1 = d.roundDouble4(metPago.getMonto());
												dMonto2 = hFac.getCpendiente() ; //  d.roundDouble4(metPago.getMonto()* hFac.getTasa().doubleValue());
												
//												LogCrtl.sendLogInfo("-------"+sdf.format(new Date())+ "Paso "+(++posicionProceso) + dtaLog + "(I) asiento de diario para cuenta de venta de contado el pago es igual al monto de la factura" );
												
												bContabilizado = recCtrl.registrarAsientoDiarioLogs( s,  (dtaLog+" Proceso:"+posicionProceso),dtFecha, hFac.getCodsuc(),valoresJdeIns[1],iNoDocumentoFor,(iContadorFor) * 1.0,iNoBatch,sCuentaCaja[0],sCuentaCaja[1],sCuentaCaja[3],
																	sCuentaCaja[4],sCuentaCaja[5],valoresJdeIns[2],metPago.getMoneda(),d.pasarAenteroLong(dMonto1),
																	sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),hFac.getTasa(),"","Met: " + metPago.getMetodo()+" f:"+hFac.getNofactura(),
																	sCuentaCaja[2],"","",valoresJdeIns[3],sCuentaCaja[2],valoresJdeIns[4], 0 );
												if (bContabilizado) {
													bContabilizado = recCtrl.registrarAsientoDiarioLogs( s,  (dtaLog+" Proceso:"+posicionProceso),dtFecha, hFac.getCodsuc(),valoresJdeIns[1],iNoDocumentoFor,(iContadorFor) * 1.0,iNoBatch,sCuentaCaja[0],sCuentaCaja[1],sCuentaCaja[3],
																		sCuentaCaja[4],sCuentaCaja[5],valoresJdeIns[5],metPago.getMoneda(),d.pasarAenteroLong(dMonto2),
																		sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),hFac.getTasa(),"","Met: " + metPago.getMetodo()+" f:"+hFac.getNofactura(),
																		sCuentaCaja[2],"","",sMonedaBase,sCuentaCaja[2],valoresJdeIns[4], d.pasarAenteroLong(dMonto1) );
													if (bContabilizado) {
														// asiento de diario para cuenta de venta de contado
														bContabilizado = recCtrl.registrarAsientoDiarioLogs( s,  (dtaLog+" Proceso:"+posicionProceso),dtFecha, hFac.getCodsuc(),valoresJdeIns[1], iNoDocumentoFor,(iContadorFor + 1) * 1.0,iNoBatch,sCuentaVenta[0],sCuentaVenta[1],sCuentaVenta[3],sCuentaVenta[4],
																			sCuentaVenta[5],valoresJdeIns[2],metPago.getMoneda(),(-1)* (d.pasarAenteroLong(dMonto1)),
																			sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),hFac.getTasa(),"","Met: " + metPago.getMetodo()+" f:"+hFac.getNofactura(),
																			sCuentaVenta[2],"","",valoresJdeIns[3],sCuentaVenta[2],valoresJdeIns[4], 0 );
														if (bContabilizado) {
															bContabilizado = recCtrl.registrarAsientoDiarioLogs( s,  (dtaLog+" Proceso:"+posicionProceso),dtFecha, hFac.getCodsuc(),valoresJdeIns[1], iNoDocumentoFor,(iContadorFor + 1) * 1.0,iNoBatch,sCuentaVenta[0],sCuentaVenta[1],sCuentaVenta[3],sCuentaVenta[4],
																			sCuentaVenta[5],valoresJdeIns[5],metPago.getMoneda(),(-1)* (d.pasarAenteroLong(dMonto2)),
																			sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),hFac.getTasa(),"","Met: " + metPago.getMetodo()+" f:"+hFac.getNofactura(),
																			sCuentaVenta[2],"","",sMonedaBase,sCuentaVenta[2],valoresJdeIns[4],  ( (-1)* (d.pasarAenteroLong(dMonto1))) );
															iContadorFor++;
															//m.put("iContadorDom",iContadorDom);
															m.put("iContadorFor",iContadorFor);
														} else {
															strMensajeValidacion = recCtrl.getError() + " <BR/> DETALLE: " + recCtrl.getErrorDetalle();		
															throw new Exception(strMensajeValidacion);
														}
													} else {
														strMensajeValidacion = recCtrl.getError() + " <BR/> DETALLE: " + recCtrl.getErrorDetalle();		
														throw new Exception(strMensajeValidacion);
													}
												} else {
													strMensajeValidacion = recCtrl.getError() + " <BR/> DETALLE: " + recCtrl.getErrorDetalle();		
													throw new Exception(strMensajeValidacion);
												}
												
												//poner en cero el monto de factura y restar el monto a aplicar
												metPago.setMonto(0);
												metPago.setEquivalente(0);
												hFac.setCpendiente(0);hFac.setDpendiente(0);
												
											}else{// el pago es menor al monto de la factura
												// comprobar si generar el no. de documento
												if (!bForGenerado) {
													
													// leer el numero de documento foraneo a utilizar para el asiento de diario
													
													//iNoDocumentoFor = d.leerActualizarNoDocJDE();
													iNoDocumentoFor = Divisas.numeroSiguienteJdeE1Custom(valoresJdeNumeracion[8],valoresJdeNumeracion[9] );
													
													if (iNoDocumentoFor > 0) {												
														bForGenerado = true;											
													} else {
														// No se pudo encontrar el No. de documento
														strMensajeValidacion = d.getError() + " <BR/> DETALLE: " + d.getErrorDetalle();		
														
														bContabilizado = false;
														throw new Exception(strMensajeValidacion);
													}
												}
												//
												//iContadorDom = Integer.parseInt(m.get("iContadorDom").toString());
												iContadorFor = Integer.parseInt(m.get("iContadorFor").toString());
												iContadorFor++;
												dMonto1 = d.roundDouble4(metPago.getMonto());
												dMonto2 = d.roundDouble4(metPago.getMonto()* hFac.getTasa().doubleValue());
												
										
												bContabilizado = recCtrl.registrarAsientoDiarioLogs( s,  (dtaLog+" Proceso:"+posicionProceso),dtFecha, hFac.getCodsuc(),valoresJdeIns[1],iNoDocumentoFor,(iContadorFor) * 1.0,iNoBatch,sCuentaCaja[0],sCuentaCaja[1],sCuentaCaja[3],
																	sCuentaCaja[4],sCuentaCaja[5],valoresJdeIns[2],metPago.getMoneda(),d.pasarAenteroLong(dMonto1),
																	sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),hFac.getTasa(),"","Met: " + metPago.getMetodo()+" f:"+hFac.getNofactura(),
																	sCuentaCaja[2],"","",valoresJdeIns[3],sCuentaCaja[2],valoresJdeIns[4], 0 );
												if (bContabilizado) {
													bContabilizado = recCtrl.registrarAsientoDiarioLogs( s,  (dtaLog+" Proceso:"+posicionProceso),dtFecha, hFac.getCodsuc(),valoresJdeIns[1],iNoDocumentoFor,(iContadorFor) * 1.0,iNoBatch,sCuentaCaja[0],sCuentaCaja[1],sCuentaCaja[3],
																		sCuentaCaja[4],sCuentaCaja[5],valoresJdeIns[5],metPago.getMoneda(),d.pasarAenteroLong(dMonto2),
																		sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),hFac.getTasa(),"","Met: " + metPago.getMetodo()+" f:"+hFac.getNofactura(),
																		sCuentaCaja[2],"","",sMonedaBase,sCuentaCaja[2],valoresJdeIns[4], (d.pasarAenteroLong(dMonto1)) );
													if (bContabilizado) {
														// asiento de diario para cuenta de venta de contado
														bContabilizado = recCtrl.registrarAsientoDiarioLogs( s,  (dtaLog+" Proceso:"+posicionProceso),dtFecha, hFac.getCodsuc(),valoresJdeIns[1], iNoDocumentoFor,(iContadorFor + 1) * 1.0,iNoBatch,sCuentaVenta[0],sCuentaVenta[1],sCuentaVenta[3],sCuentaVenta[4],
																			sCuentaVenta[5],valoresJdeIns[2],metPago.getMoneda(),(-1)* (d.pasarAenteroLong(dMonto1)),
																			sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),hFac.getTasa(),"","Met: " + metPago.getMetodo()+" f:"+hFac.getNofactura(),
																			sCuentaVenta[2],"","",valoresJdeIns[3],sCuentaVenta[2],valoresJdeIns[4], 0);
														if (bContabilizado) {
															bContabilizado = recCtrl.registrarAsientoDiarioLogs( s,  (dtaLog+" Proceso:"+posicionProceso),dtFecha, hFac.getCodsuc(),valoresJdeIns[1], iNoDocumentoFor,(iContadorFor + 1) * 1.0,iNoBatch,sCuentaVenta[0],sCuentaVenta[1],sCuentaVenta[3],sCuentaVenta[4],
																			sCuentaVenta[5],valoresJdeIns[5],metPago.getMoneda(),(-1)* (d.pasarAenteroLong(dMonto2)),
																			sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),hFac.getTasa(),"","Met: " + metPago.getMetodo()+" f:"+hFac.getNofactura(),
																			sCuentaVenta[2],"","",sMonedaBase,sCuentaVenta[2],valoresJdeIns[4], ((-1)* (d.pasarAenteroLong(dMonto1))) );
															iContadorFor++;
															//m.put("iContadorDom",iContadorDom);
															m.put("iContadorFor",iContadorFor);
														} else {
															strMensajeValidacion = recCtrl.getError() + " <BR/> DETALLE: " + recCtrl.getErrorDetalle();		
															throw new Exception(strMensajeValidacion);
														}
													} else {
														strMensajeValidacion = recCtrl.getError() + " <BR/> DETALLE: " + recCtrl.getErrorDetalle();		
														throw new Exception(strMensajeValidacion);
													}
												} else {
													strMensajeValidacion = recCtrl.getError() + " <BR/> DETALLE: " + recCtrl.getErrorDetalle();		
													throw new Exception(strMensajeValidacion);
												}
												
												//poner en cero el monto de pagos y poner el monto pendiente
												hFac.setCpendiente(d.roundDouble4(hFac.getCpendiente() - dMonto2));hFac.setDpendiente(d.roundDouble4(hFac.getDpendiente() - dMonto1));
												metPago.setMonto(0);
												metPago.setEquivalente(0);
												
													
												}
										}
									} else {// factura en cordobas
										if (!metPago.getMoneda().equals(sMonedaBase)) {//F:COR P:USD activar compra venta pago dolares
											
											if(d.roundDouble(metPago.getEquivalente()) > d.roundDouble(hFac.getCpendiente())){//el pago es mayor al monto de la factura
												if (!bForGenerado) {
													
													// leer el numero de documento foraneo a utilizar para el asiento de diario
													
													//iNoDocumentoFor = d.leerActualizarNoDocJDE();
													iNoDocumentoFor = Divisas.numeroSiguienteJdeE1Custom(valoresJdeNumeracion[8],valoresJdeNumeracion[9] );
													
													if (iNoDocumentoFor > 0) {													
														bForGenerado = true;													
													} else {
														// No se pudo encontrar el No. de documento
														strMensajeValidacion = d.getError() + " <BR/> DETALLE: " + d.getErrorDetalle();		
														lblMensajeValidacion.setValue(strMensajeValidacion);	;
														bContabilizado = false;
															return false;
														}
													}	
												dMonto1 = hFac.getCpendiente();
												dMonto2 = hFac.getDpendiente();
												if(bContabilizado){
													iContadorFor++;
													
//													LogCrtl.sendLogInfo("-------"+sdf.format(new Date())+ "Paso "+(++posicionProceso) + dtaLog + "(I) asiento de diario para cuenta de venta de contado el pago es mayor al monto de la factura" );
													
													
													bContabilizado = recCtrl.registrarAsientoDiarioLogs( s,  (dtaLog+" Proceso:"+posicionProceso),dtFecha, hFac.getCodsuc(),valoresJdeIns[1], iNoDocumentoFor,(iContadorFor) * 1.0,iNoBatch,sCuentaCaja[0],sCuentaCaja[1],sCuentaCaja[3],
																	sCuentaCaja[4],sCuentaCaja[5],valoresJdeIns[2],metPago.getMoneda(),d.pasarAenteroLong(dMonto2),
																	sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),metPago.getTasa(),"","Met: " + metPago.getMetodo() +" f:"+hFac.getNofactura(),
																	sCuentaCaja[2],"","",valoresJdeIns[3],sCuentaCaja[2],valoresJdeIns[4], 0);
													if (bContabilizado) {
														bContabilizado = recCtrl.registrarAsientoDiarioLogs( s,  (dtaLog+" Proceso:"+posicionProceso),dtFecha, hFac.getCodsuc(),valoresJdeIns[1], iNoDocumentoFor,(iContadorFor) * 1.0,iNoBatch,sCuentaCaja[0],sCuentaCaja[1],sCuentaCaja[3],
																		sCuentaCaja[4],sCuentaCaja[5],valoresJdeIns[5],metPago.getMoneda(),d.pasarAenteroLong(dMonto1),
																		sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),metPago.getTasa(),"","Met: " + metPago.getMetodo()+" f:"+hFac.getNofactura(),
																		sCuentaCaja[2],"","",sMonedaBase,sCuentaCaja[2],valoresJdeIns[4], d.pasarAenteroLong(dMonto2) );
														if (bContabilizado) {
															// asiento de diario para cuenta de venta de contado
															bContabilizado = recCtrl.registrarAsientoDiarioLogs( s,  (dtaLog+" Proceso:"+posicionProceso),dtFecha, hFac.getCodsuc(),valoresJdeIns[1], iNoDocumentoFor,(iContadorFor + 1) * 1.0,iNoBatch,sCuentaVenta[0],sCuentaVenta[1],sCuentaVenta[3],sCuentaVenta[4],
																			sCuentaVenta[5],valoresJdeIns[2],metPago.getMoneda(),(-1)* (d.pasarAenteroLong(dMonto2)),
																			sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),metPago.getTasa(),"","Met: " + metPago.getMetodo()+" f:"+hFac.getNofactura(),
																			sCuentaVenta[2],"","",valoresJdeIns[3],sCuentaVenta[2],valoresJdeIns[4], 0 );
															if (bContabilizado) {
																bContabilizado = recCtrl.registrarAsientoDiarioLogs( s,  (dtaLog+" Proceso:"+posicionProceso),dtFecha, hFac.getCodsuc(),valoresJdeIns[1], iNoDocumentoFor,(iContadorFor + 1) * 1.0,iNoBatch,sCuentaVenta[0],sCuentaVenta[1],sCuentaVenta[3],sCuentaVenta[4],
																			sCuentaVenta[5],valoresJdeIns[5],metPago.getMoneda(),(-1)* (d.pasarAenteroLong(dMonto1)),
																			sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),metPago.getTasa(),"","Met: " + metPago.getMetodo()+" f:"+hFac.getNofactura(),
																			sCuentaVenta[2],"","",sMonedaBase,sCuentaVenta[2],valoresJdeIns[4], ((-1)* (d.pasarAenteroLong(dMonto2))) );
																iContadorFor++;
																//m.put("iContadorDom",iContadorDom);
																m.put("iContadorFor",iContadorFor);
															} else {
																strMensajeValidacion = recCtrl.getError() + " <BR/> DETALLE: " + recCtrl.getErrorDetalle();		
																throw new Exception(strMensajeValidacion);
															}
														} else {
															strMensajeValidacion = recCtrl.getError() + " <BR/> DETALLE: " + recCtrl.getErrorDetalle();		
															throw new Exception(strMensajeValidacion);
														}
													} else {
														strMensajeValidacion = recCtrl.getError() + " <BR/> DETALLE: " + recCtrl.getErrorDetalle();		
														throw new Exception(strMensajeValidacion);
													}
												}
											
												
												//iContadorDom = Integer.parseInt(m.get("iContadorDom").toString());
												iContadorFor = Integer.parseInt(m.get("iContadorFor").toString());
												

												//poner en cero el monto de factura y restar el monto a aplicar
												metPago.setMonto(d.roundDouble4(metPago.getMonto() - hFac.getDpendiente()));
												metPago.setEquivalente(d.roundDouble4(metPago.getEquivalente() - hFac.getCpendiente()));
												hFac.setCpendiente(0);hFac.setDpendiente(0);
												
											}else if (d.roundDouble(metPago.getEquivalente()) == d.roundDouble(hFac.getCpendiente())){
												
												if (!bForGenerado) {
													
													// leer el numero de documento foraneo a utilizar para el asiento de diario
													//iNoDocumentoFor = d.leerActualizarNoDocJDE();
													iNoDocumentoFor = Divisas.numeroSiguienteJdeE1Custom(valoresJdeNumeracion[8],valoresJdeNumeracion[9] );
													
													if (iNoDocumentoFor > 0) {													
														bForGenerado = true;													
													} else {
														// No se pudo encontrar el No. de documento
														strMensajeValidacion = d.getError() + " <BR/> DETALLE: " + d.getErrorDetalle();		
														
														bContabilizado = false;
														throw new Exception(strMensajeValidacion);
														}
													}	
												dMonto1 = hFac.getCpendiente();
												dMonto2 = hFac.getDpendiente();
												if(bContabilizado){
													iContadorFor++;
													
													
													bContabilizado = recCtrl.registrarAsientoDiarioLogs( s,  (dtaLog+" Proceso:"+posicionProceso),dtFecha, hFac.getCodsuc(),valoresJdeIns[1], iNoDocumentoFor,(iContadorFor) * 1.0,iNoBatch,sCuentaCaja[0],sCuentaCaja[1],sCuentaCaja[3],
																	sCuentaCaja[4],sCuentaCaja[5],valoresJdeIns[2],metPago.getMoneda(),d.pasarAenteroLong(dMonto2),
																	sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),metPago.getTasa(),"","Met: " + metPago.getMetodo() +" f:"+hFac.getNofactura(),
																	sCuentaCaja[2],"","",valoresJdeIns[3],sCuentaCaja[2],valoresJdeIns[4], 0);
													if (bContabilizado) {
														bContabilizado = recCtrl.registrarAsientoDiarioLogs( s,  (dtaLog+" Proceso:"+posicionProceso),dtFecha, hFac.getCodsuc(),valoresJdeIns[1], iNoDocumentoFor,(iContadorFor) * 1.0,iNoBatch,sCuentaCaja[0],sCuentaCaja[1],sCuentaCaja[3],
																		sCuentaCaja[4],sCuentaCaja[5],valoresJdeIns[5],metPago.getMoneda(),d.pasarAenteroLong(dMonto1),
																		sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),metPago.getTasa(),"","Met: " + metPago.getMetodo()+" f:"+hFac.getNofactura(),
																		sCuentaCaja[2],"","",sMonedaBase,sCuentaCaja[2],valoresJdeIns[4], d.pasarAenteroLong(dMonto2)  );
														if (bContabilizado) {
															// asiento de diario para cuenta de venta de contado
															bContabilizado = recCtrl.registrarAsientoDiarioLogs( s,  (dtaLog+" Proceso:"+posicionProceso),dtFecha, hFac.getCodsuc(),valoresJdeIns[1], iNoDocumentoFor,(iContadorFor + 1) * 1.0,iNoBatch,sCuentaVenta[0],sCuentaVenta[1],sCuentaVenta[3],sCuentaVenta[4],
																			sCuentaVenta[5],valoresJdeIns[2],metPago.getMoneda(),(-1)* (d.pasarAenteroLong(dMonto2)),
																			sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),metPago.getTasa(),"","Met: " + metPago.getMetodo()+" f:"+hFac.getNofactura(),
																			sCuentaVenta[2],"","",valoresJdeIns[3],sCuentaVenta[2],valoresJdeIns[4], 0 );
															if (bContabilizado) {
																bContabilizado = recCtrl.registrarAsientoDiarioLogs( s,  (dtaLog+" Proceso:"+posicionProceso),dtFecha, hFac.getCodsuc(),valoresJdeIns[1], iNoDocumentoFor,(iContadorFor + 1) * 1.0,iNoBatch,sCuentaVenta[0],sCuentaVenta[1],sCuentaVenta[3],sCuentaVenta[4],
																			sCuentaVenta[5],valoresJdeIns[5],metPago.getMoneda(),(-1)* (d.pasarAenteroLong(dMonto1)),
																			sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),metPago.getTasa(),"","Met: " + metPago.getMetodo()+" f:"+hFac.getNofactura(),
																			sCuentaVenta[2],"","",sMonedaBase,sCuentaVenta[2],valoresJdeIns[4], ((-1)* (d.pasarAenteroLong(dMonto2)) )  );
																iContadorFor++;
																//m.put("iContadorDom",iContadorDom);
																m.put("iContadorFor",iContadorFor);
															} else {
																strMensajeValidacion = recCtrl.getError() + " <BR/> DETALLE: " + recCtrl.getErrorDetalle();		
																throw new Exception(strMensajeValidacion);
															}
														} else {
															strMensajeValidacion = recCtrl.getError() + " <BR/> DETALLE: " + recCtrl.getErrorDetalle();		
															throw new Exception(strMensajeValidacion);
														}
													} else {
														strMensajeValidacion = recCtrl.getError() + " <BR/> DETALLE: " + recCtrl.getErrorDetalle();		
														throw new Exception(strMensajeValidacion);
													}
												}

												iContadorFor = Integer.parseInt(m.get("iContadorFor").toString());
												

												//poner en cero el monto de factura y restar el monto a aplicar
												metPago.setMonto(0);
												metPago.setEquivalente(0);
												hFac.setCpendiente(0);hFac.setDpendiente(0);
												
											}else{//el pago es menor al monto de la factura
												if (!bForGenerado) {
													
													// leer el numero de documento foraneo a utilizar para el asiento de diario
													//iNoDocumentoFor = d.leerActualizarNoDocJDE();
													iNoDocumentoFor = Divisas.numeroSiguienteJdeE1Custom(valoresJdeNumeracion[8],valoresJdeNumeracion[9] );
													
													if (iNoDocumentoFor > 0) {													
														bForGenerado = true;													
													} else {
														// No se pudo encontrar el No. de documento
														strMensajeValidacion = d.getError() + " <BR/> DETALLE: " + d.getErrorDetalle();		
														lblMensajeValidacion.setValue(strMensajeValidacion);
														bContabilizado = false;
														throw new Exception(strMensajeValidacion);
														}
													}	
												dMonto1 = metPago.getEquivalente();
												dMonto2 = metPago.getMonto();
												if(bContabilizado){
													iContadorFor++;
													
//													LogCrtl.sendLogInfo("-------"+sdf.format(new Date())+ "Paso "+(++posicionProceso) + dtaLog + "(I) asiento de diario para cuenta de venta de contado el pago es menor al monto de la factura" );
													
													bContabilizado = recCtrl.registrarAsientoDiarioLogs( s,  (dtaLog+" Proceso:"+posicionProceso),dtFecha, hFac.getCodsuc(),valoresJdeIns[1], iNoDocumentoFor,(iContadorFor) * 1.0,iNoBatch,sCuentaCaja[0],sCuentaCaja[1],sCuentaCaja[3],
																	sCuentaCaja[4],sCuentaCaja[5],valoresJdeIns[2],metPago.getMoneda(),d.pasarAenteroLong(dMonto2),
																	sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),metPago.getTasa(),"","Met: " + metPago.getMetodo() +" f:"+hFac.getNofactura(),
																	sCuentaCaja[2],"","",valoresJdeIns[3],sCuentaCaja[2],valoresJdeIns[4], 0 );
													if (bContabilizado) {
														bContabilizado = recCtrl.registrarAsientoDiarioLogs( s,  (dtaLog+" Proceso:"+posicionProceso),dtFecha, hFac.getCodsuc(),valoresJdeIns[1], iNoDocumentoFor,(iContadorFor) * 1.0,iNoBatch,sCuentaCaja[0],sCuentaCaja[1],sCuentaCaja[3],
																		sCuentaCaja[4],sCuentaCaja[5],valoresJdeIns[5],metPago.getMoneda(),d.pasarAenteroLong(dMonto1),
																		sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),metPago.getTasa(),"","Met: " + metPago.getMetodo()+" f:"+hFac.getNofactura(),
																		sCuentaCaja[2],"","",sMonedaBase,sCuentaCaja[2],valoresJdeIns[4], d.pasarAenteroLong(dMonto2) );
														if (bContabilizado) {
															// asiento de diario para cuenta de venta de contado
															bContabilizado = recCtrl.registrarAsientoDiarioLogs( s,  (dtaLog+" Proceso:"+posicionProceso),dtFecha, hFac.getCodsuc(),valoresJdeIns[1], iNoDocumentoFor,(iContadorFor + 1) * 1.0,iNoBatch,sCuentaVenta[0],sCuentaVenta[1],sCuentaVenta[3],sCuentaVenta[4],
																			sCuentaVenta[5],valoresJdeIns[2],metPago.getMoneda(),(-1)* (d.pasarAenteroLong(dMonto2)),
																			sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),metPago.getTasa(),"","Met: " + metPago.getMetodo()+" f:"+hFac.getNofactura(),
																			sCuentaVenta[2],"","",valoresJdeIns[3],sCuentaVenta[2],valoresJdeIns[4], 0 );
															if (bContabilizado) {
																bContabilizado = recCtrl.registrarAsientoDiarioLogs( s,  (dtaLog+" Proceso:"+posicionProceso),dtFecha, hFac.getCodsuc(),valoresJdeIns[1], iNoDocumentoFor,(iContadorFor + 1) * 1.0,iNoBatch,sCuentaVenta[0],sCuentaVenta[1],sCuentaVenta[3],sCuentaVenta[4],
																			sCuentaVenta[5],valoresJdeIns[5],metPago.getMoneda(),(-1)* (d.pasarAenteroLong(dMonto1)),
																			sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),metPago.getTasa(),"","Met: " + metPago.getMetodo()+" f:"+hFac.getNofactura(),
																			sCuentaVenta[2],"","",sMonedaBase,sCuentaVenta[2],valoresJdeIns[4], ((-1)* (d.pasarAenteroLong(dMonto2))) );
																iContadorFor++;
																//m.put("iContadorDom",iContadorDom);
																m.put("iContadorFor",iContadorFor);
															} else {
																strMensajeValidacion = recCtrl.getError() + " <BR/> DETALLE: " + recCtrl.getErrorDetalle();		
																throw new Exception(strMensajeValidacion);
															}
														} else {
															strMensajeValidacion = recCtrl.getError() + " <BR/> DETALLE: " + recCtrl.getErrorDetalle();		
															throw new Exception(strMensajeValidacion);
														}
													} else {
														strMensajeValidacion = recCtrl.getError() + " <BR/> DETALLE: " + recCtrl.getErrorDetalle();		
														throw new Exception(strMensajeValidacion);
													}
												}
												

												iContadorFor = Integer.parseInt(m.get("iContadorFor").toString());
												

												//poner en cero el monto de factura y restar el monto a aplicar
												hFac.setCpendiente(d.roundDouble4(hFac.getCpendiente() - dMonto1));hFac.setDpendiente(d.roundDouble4(hFac.getDpendiente() - dMonto2));
												metPago.setMonto(0);
												metPago.setEquivalente(0);											
											
											}
										}else{//F:COR P:COR pago en la moneda de la factura
											if(d.roundDouble4(metPago.getMonto()) > d.roundDouble4(hFac.getCpendiente())){//el pago es mayor al monto de la factura
												// comprobar si generar el no. de documento
												if (!bDomGenerado) {
													
													// leer el numero de documento foraneo a utilizar para el asiento de diario
													//iNoDocumentoFor = d.leerActualizarNoDocJDE();
													iNoDocumentoFor = Divisas.numeroSiguienteJdeE1Custom(valoresJdeNumeracion[8],valoresJdeNumeracion[9] );
													
													if (iNoDocumentoFor > 0) {
														bDomGenerado = true;										
													} else {
														// No se pudo encontrar el No. de documento
														strMensajeValidacion = d.getError() + " <BR/> DETALLE: " + d.getErrorDetalle();		
														lblMensajeValidacion.setValue(strMensajeValidacion);
														bContabilizado = false;
														throw new Exception(strMensajeValidacion);
													}
												}
												dMonto1 = hFac.getCpendiente();												
												 
												//iContadorDom = Integer.parseInt(m.get("iContadorDom").toString());
												iContadorFor = Integer.parseInt(m.get("iContadorFor").toString());
												 
												//iContadorDom++;
												iContadorFor++;												

												bContabilizado = recCtrl.registrarAsientoDiarioLogs( s,  (dtaLog+" Proceso:"+posicionProceso),dtFecha, hFac.getCodsuc(),valoresJdeIns[1],iNoDocumentoFor,( iContadorFor /*iContadorDom*/) * 1.0,iNoBatch,
																	sCuentaCaja[0],sCuentaCaja[1],sCuentaCaja[3],sCuentaCaja[4],sCuentaCaja[5],valoresJdeIns[5],metPago.getMoneda(),
																	d.pasarAenteroLong(dMonto1),sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),
																	BigDecimal.ZERO,"","Met: " + metPago.getMetodo()+" f:"+hFac.getNofactura(),
																	sCuentaCaja[2],"","",sMonedaBase,sCuentaCaja[2],valoresJdeIns[7], 0);
												if (bContabilizado) {
													// asiento de diario para cuenta de venta de contado
													bContabilizado = recCtrl.registrarAsientoDiarioLogs( s,  (dtaLog+" Proceso:"+posicionProceso),dtFecha, hFac.getCodsuc(),valoresJdeIns[1], iNoDocumentoFor,( iContadorFor /*iContadorDom*/ + 1) * 1.0,iNoBatch,sCuentaVenta[0],sCuentaVenta[1],
																			sCuentaVenta[3],sCuentaVenta[4],sCuentaVenta[5],valoresJdeIns[5],metPago.getMoneda(),(-1)* (d.pasarAenteroLong(dMonto1)),
																			sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),BigDecimal.ZERO,"","Met: " + metPago.getMetodo()+" f:"+hFac.getNofactura(),
																			sCuentaVenta[2],"","",sMonedaBase,sCuentaVenta[2],valoresJdeIns[7], 0);
													
													iContadorFor++;
													
													m.put("iContadorFor", iContadorFor);
												} else {
													strMensajeValidacion = recCtrl.getError() + " <BR/> DETALLE: " + recCtrl.getErrorDetalle();		
													throw new Exception(strMensajeValidacion);
												}	
												
												//poner en cero el monto de factura y restar el monto a aplicar
												metPago.setMonto(d.roundDouble4(metPago.getMonto() - hFac.getCpendiente()));
												metPago.setEquivalente(d.roundDouble4(metPago.getEquivalente() - hFac.getDpendiente()));
												hFac.setCpendiente(0);hFac.setDpendiente(0);												
												
											}else if(d.roundDouble4(metPago.getMonto()) == d.roundDouble4(hFac.getCpendiente())){//El pago es igual al monto de la factura
												// comprobar si generar el no. de documento
												if (!bDomGenerado) {
													
													// leer el numero de documento foraneo a utilizar para el asiento de diario
													
													iNoDocumentoFor = Divisas.numeroSiguienteJdeE1Custom(valoresJdeNumeracion[8],valoresJdeNumeracion[9] );
													
													if (iNoDocumentoFor > 0) {
														bDomGenerado = true;										
													} else {
														// No se pudo encontrar el No. de documento
														strMensajeValidacion = d.getError() + " <BR/> DETALLE: " + d.getErrorDetalle();		
														lblMensajeValidacion.setValue(strMensajeValidacion);	
														bContabilizado = false;
														throw new Exception(strMensajeValidacion);
													}
												}
												dMonto1 = hFac.getCpendiente();					
												iContadorFor = Integer.parseInt(m.get("iContadorFor").toString());
											 
												//iContadorDom++;
												iContadorFor ++ ;
												bContabilizado = recCtrl.registrarAsientoDiarioLogs( s,  (dtaLog+" Proceso:"+posicionProceso),dtFecha, hFac.getCodsuc(),valoresJdeIns[1],iNoDocumentoFor,( iContadorFor/*iContadorDom*/) * 1.0,iNoBatch,
																	sCuentaCaja[0],sCuentaCaja[1],sCuentaCaja[3],sCuentaCaja[4],sCuentaCaja[5],valoresJdeIns[5],metPago.getMoneda(),
																	d.pasarAenteroLong(dMonto1),sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),
																	BigDecimal.ZERO,"","Met: " + metPago.getMetodo()+" f:"+hFac.getNofactura(),
																	sCuentaCaja[2],"","",sMonedaBase,sCuentaCaja[2],valoresJdeIns[7], 0);
												if (bContabilizado) {
													// asiento de diario para cuenta de venta de contado
													bContabilizado = recCtrl.registrarAsientoDiarioLogs( s,  (dtaLog+" Proceso:"+posicionProceso),dtFecha, hFac.getCodsuc(),valoresJdeIns[1], iNoDocumentoFor,( iContadorFor /*iContadorDom*/ + 1) * 1.0,iNoBatch,sCuentaVenta[0],sCuentaVenta[1],
																			sCuentaVenta[3],sCuentaVenta[4],sCuentaVenta[5],valoresJdeIns[5],metPago.getMoneda(),(-1)* (d.pasarAenteroLong(dMonto1)),
																			sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),BigDecimal.ZERO,"","Met: " + metPago.getMetodo()+" f:"+hFac.getNofactura(),
																			sCuentaVenta[2],"","",sMonedaBase,sCuentaVenta[2],valoresJdeIns[7], 0);
													iContadorFor++;
													m.put("iContadorFor", iContadorFor);
												} else {
													strMensajeValidacion = recCtrl.getError() + " <BR/> DETALLE: " + recCtrl.getErrorDetalle();		
													throw new Exception(strMensajeValidacion);
												}	
												//poner en cero el monto de factura y restar el monto a aplicar
												metPago.setMonto(0);
												metPago.setEquivalente(0);
												hFac.setCpendiente(0);hFac.setDpendiente(0);												
												
											}else{//el pago es menor al monto de la factura
												// comprobar si generar el no. de documento
												if (!bDomGenerado) {
													
													// leer el numero de documento foraneo a utilizar para el asiento de diario									
													iNoDocumentoFor = Divisas.numeroSiguienteJdeE1Custom(valoresJdeNumeracion[8],valoresJdeNumeracion[9] );
													
													if (iNoDocumentoFor > 0) {
														bDomGenerado = true;										
													} else {
														// No se pudo encontrar el No. de documento
														strMensajeValidacion = d.getError() + " <BR/> DETALLE: " + d.getErrorDetalle();		
														lblMensajeValidacion.setValue(strMensajeValidacion);	
														bContabilizado = false;
														throw new Exception(strMensajeValidacion);
													}
												}
												dMonto1 = metPago.getMonto();												
												 
												//iContadorDom = Integer.parseInt(m.get("iContadorDom").toString());
												iContadorFor = Integer.parseInt(m.get("iContadorFor").toString());
												 
												//iContadorDom++;
												iContadorFor ++;
												
	
												bContabilizado = recCtrl.registrarAsientoDiarioLogs( s,  (dtaLog+" Proceso:"+posicionProceso),dtFecha, hFac.getCodsuc(),valoresJdeIns[1],iNoDocumentoFor,( iContadorFor /*iContadorDom*/) * 1.0,iNoBatch,
																	sCuentaCaja[0],sCuentaCaja[1],sCuentaCaja[3],sCuentaCaja[4],sCuentaCaja[5],valoresJdeIns[5],metPago.getMoneda(),
																	d.pasarAenteroLong(dMonto1),sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),
																	BigDecimal.ZERO,"","Met: " + metPago.getMetodo()+" f:"+hFac.getNofactura(),
																	sCuentaCaja[2],"","",sMonedaBase,sCuentaCaja[2],valoresJdeIns[7], 0);
												if (bContabilizado) {
													
													// asiento de diario para cuenta de venta de contado
													bContabilizado = recCtrl.registrarAsientoDiarioLogs( s,  (dtaLog+" Proceso:"+posicionProceso), dtFecha, hFac.getCodsuc(),valoresJdeIns[1], iNoDocumentoFor,( iContadorFor /*iContadorDom*/ + 1) * 1.0,iNoBatch,sCuentaVenta[0],sCuentaVenta[1],
																			sCuentaVenta[3],sCuentaVenta[4],sCuentaVenta[5],valoresJdeIns[5],metPago.getMoneda(),(-1)* (d.pasarAenteroLong(dMonto1)),
																			sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),BigDecimal.ZERO,"","Met: " + metPago.getMetodo()+" f:"+hFac.getNofactura(),
																			sCuentaVenta[2],"","",sMonedaBase,sCuentaVenta[2],valoresJdeIns[7], 0);

													iContadorFor ++ ;

													m.put("iContadorFor", iContadorFor);
												} else {
													strMensajeValidacion = recCtrl.getError() + " <BR/> DETALLE: " + recCtrl.getErrorDetalle();		
													lblMensajeValidacion.setValue(strMensajeValidacion);
													throw new Exception(strMensajeValidacion);
												}	

												//poner en cero el monto de factura y restar el monto a aplicar
												hFac.setCpendiente(d.roundDouble4(hFac.getCpendiente() - metPago.getMonto()));
												metPago.setMonto(0);
												metPago.setEquivalente(0);											
											
											}//Fin del pago es menos al monto de la factura																				
										}//Fin de pago F:COR P:COR
									}// fin de factura en cordobas
								} else {
			 
									strMensajeValidacion = d.getError() + " <BR/> DETALLE: " + d.getErrorDetalle();
									
									if (lblMensajeValidacion != null) 
									    lblMensajeValidacion.setValue(strMensajeValidacion);	
									bContabilizado = false;
									throw new Exception(strMensajeValidacion);
								}
								
								j++;
							}
							
						} else {

							try{
								strMensajeValidacion = d.getError().toString().split("@LOGCAJA:")[1];
							}catch(Exception e){
								strMensajeValidacion = " No se encontró cuenta de caja Transitoria, Favor intente nuevamente ";
							}
							
							bContabilizado = false;
							throw new Exception(strMensajeValidacion);
							
						}
						//termina validacion de cuenta transitoria
						
						//Fin del blucle While agrego el i++ para evitar el bucle infinito en que estaba
				
						i++;
					}
				} else {
					// no se pudo actualizar el no de batch					
					strMensajeValidacion = "No se pudo actualizar el No. de batch!!! " + d.getError() + " <BR/> DETALLE: " + d.getErrorDetalle();		
					lblMensajeValidacion.setValue(strMensajeValidacion);
					throw new Exception(strMensajeValidacion);
				}
				
				
				if (bContabilizado && (bDomGenerado || bForGenerado ) ) {
					iCantDocs++;
					
					nombreEvento= " recCtrl.fillEnlaceMcajaJde:(I) ["+sdf.format(new Date())+"] << -- >>" ;
					

					bContabilizado = recCtrl.fillEnlaceMcajaJde(s, tx, iNumrec, sCodComp,iNoDocumentoFor, iNoBatch, iCajaId, f55ca01.getId().getCaco(), "A", valoresJdeIns[0]);
					
					nombreEvento+= "(F) ["+sdf.format(new Date())+"]";

					
					if (!bContabilizado) {
						strMensajeValidacion = recCtrl.getError() + " <BR/> DETALLE: " + recCtrl.getErrorDetalle();		
						lblMensajeValidacion.setValue(strMensajeValidacion);
						throw new Exception(strMensajeValidacion);
					}
				}
				
				
				
				
				
				// Grabar Batch
				if (bContabilizado) {
					
					
					nombreEvento= " registrarBatchA92:(I) ["+sdf.format(new Date())+"] << -- >>" ;
					
					
					bContabilizado = recCtrl.registrarBatchA92Custom(s, dtFecha, valoresJdeIns[8], iNoBatch, iTotalTransaccion, vaut[0].getId().getLogin(), iCantDocs, "RCONTADO", valoresJdeIns[9] );
					 
					
					
					nombreEvento+= "(F) ["+sdf.format(new Date())+"]";

					
					if(!bContabilizado){
						strMensajeValidacion = "Asiento de diario no " +
								"registrado para el recibo, favor" +
								" intente de nuevo ";
						throw new Exception(strMensajeValidacion);
					}
				}

				if (bHayFicha && bContabilizado) {
					String[] sCuentaCajaDom = d.obtenerCuentaCaja(f55ca01.getId().getCaid(),hFac.getCodcomp(), MetodosPagoCtrl.EFECTIVO,sMonedaBase,s,tx,null,null);
					sCuentaCaja = d.obtenerCuentaCaja(f55ca01.getId().getCaid(),hFac.getCodcomp(), MetodosPagoCtrl.EFECTIVO,"USD",s,tx,null,null);
					int iNoficha = Integer.parseInt(m.get("iNoFicha").toString());
					
					nombreEvento= " generarAsientosFichaCV:(I) ["+sdf.format(new Date())+"] << -- >>" ;

					bContabilizado = generarAsientosFichaCV(s,
											tx, lstPagoFicha,
											f55ca01, hFac, "", vaut[0],
											sCuentaCaja,
											sCuentaCajaDom, 
											iNoficha,
											iNumrec, new Date());
					
					nombreEvento+= "(F) ["+sdf.format(new Date())+"]";
					
					
					if(!bContabilizado){
						strMensajeValidacion = "Recibo no aplicado, no se " +
								"pudo ingresar el registro de compra venta" +
								" de divisas, intente nuevamente";
						throw new Exception(strMensajeValidacion);
					}
				}
				m.remove("iContadorDom");
				m.remove("iContadorFor");
				
			} else {
				strMensajeValidacion = d.getError() + " <BR/> DETALLE: " + d.getErrorDetalle();		
				lblMensajeValidacion.setValue(strMensajeValidacion);
				bContabilizado = false;
			}
			
			
			LogCajaService.CreateLog("generarAsientos", "QRY", "generarAsientos-FIN");

		} catch (Exception ex) {
			LogCajaService.CreateLog("generarAsientos", "ERR", ex.getMessage());
			bContabilizado = false;
			strMensajeValidacion = "Recibo no procesado, error al grabar datos en JdEdwars ";
			lblMensajeValidacion.setValue(strMensajeValidacion); 
			throw new Exception(strMensajeValidacion);
		}
		return bContabilizado;
	
		
		
	}

/*********************************************************************************************************/
/********************************************************************************
 * 						INSERTAR FICHA DE COMPRA/VENTA
 * *******************************************************************************/
	public boolean insertarFichaCV(Session session, Transaction tx,
			Connection cn, Vf55ca01 f55ca01, Hfactura hFac, Vautoriz vautoriz,
			double dMonto, int iNumrec, String sCajero, List lstPagoFicha) {
		boolean bInsertado = false;
		NumcajaCtrl numCtrl = new NumcajaCtrl();
		int iNoFicha = 0;
		ReciboCtrl conCtrl = new ReciboCtrl();
		try {
			
			// obtener y actualizar el numero de ficha a utilizar
			iNoFicha = numCtrl.obtenerNoSiguiente("FICHACV", 
					f55ca01.getId().getCaid(), 
					hFac.getCodcomp(), 
					f55ca01.getId().getCaco(),
					vautoriz.getId().getLogin());
			
			if (iNoFicha > 0) {

				// registrar encabezado de ficha
				bInsertado = conCtrl.registrarRecibo(session, tx, iNoFicha, 0,
						hFac.getCodcomp(), dMonto, dMonto, 0, "", new Date(),
						new Date(), hFac.getCodcli(), hFac.getNomcli(),
						sCajero, f55ca01.getId().getCaid(), f55ca01.getId()
								.getCaco(), vautoriz.getId().getLogin(), "FCV",
						0, "", iNumrec, new Date(), hFac.getCodunineg().trim(),
						"", hFac.getMoneda());
				
				if (bInsertado) {
					// registrar detalle de ficha
					bInsertado = conCtrl.registrarDetalleRecibo(session, tx,iNoFicha, 0, hFac.getCodcomp(), lstPagoFicha,
							f55ca01.getId().getCaid(), f55ca01.getId().getCaco(), "FCV");
					
					
					m.put("iNoFicha", iNoFicha);
					NoFichaCV = iNoFicha;
					
					if (!bInsertado) {						
						strMensajeValidacion = conCtrl.getError() + " <BR/> DETALLE: " + conCtrl.getErrorDetalle();		
						lblMensajeValidacion.setValue(strMensajeValidacion);
					}
				} else {					
					strMensajeValidacion = conCtrl.getError() + " <BR/> DETALLE: " + conCtrl.getErrorDetalle();		
					lblMensajeValidacion.setValue(strMensajeValidacion);
				}
			} else {
				strMensajeValidacion = "No se encontro No. de ficha configurado para esta caja!!!";
				lblMensajeValidacion.setValue(strMensajeValidacion);
				bInsertado = false;
			}
		} catch (Exception ex) {
			bInsertado = false;
			strMensajeValidacion = "Se capturo una excepcion en FacContadoDAO.insertarFichaCV!!!" + " <BR/> DETALLE: " +ex;
			lblMensajeValidacion.setValue(strMensajeValidacion);
			ex.printStackTrace();
		}
		return bInsertado;
	}

/** ******************************************************************************************************** 
 * @throws Exception */
	public boolean insertarRecibo(Session session, Transaction tx,Connection cn, 
			Vf55ca01 f55ca01, Hfactura hFac, Vautoriz[] vautoriz, 
			List<MetodosPago> lstMetodosPago) throws Exception {
		
		boolean reciboHeader = false, reciboDetalle = false, actualizado = false, enlace = false, cambio = false, bSolicitud = false;
		int iNumRec = 0, iCodCli = 0, iNumRecm = 0, iCajaId = 0;
		double dMontoRec = 0, dMontoAplicar = 0, dCambio = 0;
		String sCodComp = null, sConcepto = null, sNomCli = null, sCajero = null;
		Date dFecha = new Date(), dHora = new Date();
		
		String[] sPartida = null, sCodunineg = null, sCodsuc = null, sTipoDoc;
		String sMonedaBase = "COR";
		
		Divisas divisas = new Divisas();
		CtrlSolicitud solCtrl = new CtrlSolicitud();
		ReciboCtrl conCtrl = new ReciboCtrl();
		
		try {

			List<Hfactura> lstFacturasSelected = (ArrayList<Hfactura>) m.get("facturasSelected");

			iCajaId = f55ca01.getId().getCaid();
			sCodComp = hFac.getCodcomp();

			sMonedaBase = new CompaniaCtrl().sacarMonedaBase((F55ca014[])
									m.get("cont_f55ca014"), sCodComp);

			iNumRec = com.casapellas.controles.tmp.ReciboCtrl
						.generarNumeroRecibo(iCajaId, sCodComp);
			
			if (iNumRec == 0) {
				strMensajeValidacion = conCtrl.getError() + " <BR/> "
						+ conCtrl.getErrorDetalle();				
				throw new Exception(strMensajeValidacion);
			}
			
			m.put("iNumRec", iNumRec);
			dMontoAplicar = divisas.formatStringToDouble(txtMontoAplicar.getValue().toString());
			dMontoRec = divisas.formatStringToDouble(txtMontoRecibido.getValue().toString());
			if (!txtCambio.getValue().toString().trim().equals("")) {
				dCambio = divisas.formatStringToDouble(txtCambio.getValue().toString());
			}
			sConcepto = txtConcepto.getValue().toString();
			iCodCli = hFac.getCodcli();
			sNomCli = hFac.getNomcli();
			sCajero = (String) m.get("sNombreEmpleado");

			List lstMetodosPago2 = new ArrayList();
			List lstSolicitud = (List) m.get("lstSolicitud");
			
			int iNumSol = 0;
			int[] iNumFac   = new int[lstFacturasSelected.size()];
			double[] dMonto = new double[lstFacturasSelected.size()];
			sPartida 		= new String[lstFacturasSelected.size()];
			sCodunineg 		= new String[lstFacturasSelected.size()];
			sCodsuc 		= new String[lstFacturasSelected.size()];
			sTipoDoc		= new String[lstFacturasSelected.size()];
			Solicitud sol = null;
			
			BitacoraSecuenciaReciboService.insertarLogReciboNumerico(iCajaId,iNumRec,sCodComp,CodeUtil.pad(f55ca01.getId().getCaco(), 5, "0"),valoresJdeIns[0]);
			
			if (cmbTiporecibo.getValue().toString().equals("AUTOMATICO")) {// RECIBO AUTOMATICO
			
				//&& ============== recalcular el monto aplicado.
				dMontoAplicar = 0;
				for (Hfactura hfs : lstFacturasSelected) 
					dMontoAplicar += (hFac.getMoneda().equals(sMonedaBase))?
										hfs.getCpendiente():
										hfs.getDpendiente(); 
				dMontoAplicar = new Divisas().roundDouble4(dMontoAplicar);
				
				//&& ============== guardar el header del recibo
				reciboHeader = conCtrl.registrarRecibo(session, tx,
						iNumRec, iNumRecm, sCodComp, dMontoAplicar,
						dMontoRec, dCambio, sConcepto, dFecha, dHora,
						iCodCli, sNomCli, sCajero, iCajaId, f55ca01.getId()
								.getCaco(), vautoriz[0].getId()
								.getCoduser(), valoresJdeIns[0], 0, "", 0, new Date(),
						hFac.getCodunineg().trim(), "", hFac.getMoneda());
				
				if (reciboHeader) {
					
					//&& ==============guardar detalle de recibo
					lstMetodosPago2 = ponerCodigoBanco(lstMetodosPago);
					
					//&& =============== Determinar si el pago trabaja bajo preconciliacion y conservar su referencia original
					com.casapellas.controles.BancoCtrl.datosPreconciliacion(lstMetodosPago2, iCajaId, sCodComp) ;
					
					reciboDetalle = conCtrl.registrarDetalleRecibo(session,tx,
										iNumRec, iNumRecm, sCodComp,lstMetodosPago2, 
										iCajaId, f55ca01.getId().getCaco(), valoresJdeIns[0]);
					
					if (reciboDetalle) {
						
						//&& ============== llenar enlaces entre recibo y factura							
						for (int h = 0; h < lstFacturasSelected.size(); h++) {
							iNumFac[h] = ((Hfactura) lstFacturasSelected.get(h)).getNofactura();
							dMonto[h] = ((Hfactura) lstFacturasSelected.get(h)).getTotal();
							sPartida[h] = "";
							sCodunineg[h] = ((Hfactura) lstFacturasSelected.get(h)).getCodunineg();
							sCodsuc[h] = ((Hfactura) lstFacturasSelected.get(h)).getCodsuc();
							sTipoDoc[h] = ((Hfactura) lstFacturasSelected.get(h)).getTipofactura();
						}
						// ************************************************************************
						enlace = conCtrl.fillEnlaceReciboFac(session, tx,iNumRec,
								sCodComp, iNumFac, dMonto,sTipoDoc, iCajaId,
								sCodsuc, sPartida,sCodunineg,valoresJdeIns[0],iCodCli,
								new FechasUtil().DateToJulian(dFecha) );
						// ************************************************************************
//						enlace = true;
						if (enlace) {
							if (lstSolicitud != null && !lstSolicitud.isEmpty()) {
								for (int i = 0; i < lstSolicitud.size(); i++) {
									sol = (Solicitud) lstSolicitud.get(i);
									iNumSol = solCtrl.getNumeroSolicitud();
									if (iNumSol > 0) {
										bSolicitud = solCtrl.registrarSolicitud(session,tx,iNumSol,iNumRec,valoresJdeIns[0],iCajaId,
														sCodComp,f55ca01.getId().getCaco(),sol.getId().getReferencia(),
														sol.getAutoriza(),dFecha,sol.getObs(),sol.getMpago(),sol.getMonto(),sol.getMoneda());
									} else {
										strMensajeValidacion = solCtrl.getError() + " <BR/> DETALLE ERROR: " + solCtrl.getErrorDetalle();										
										throw new Exception(strMensajeValidacion);
									}
								}
							} else {
								bSolicitud = true;
							}
						} else {
							strMensajeValidacion = conCtrl.getError() + " <BR/> DETALLE ERROR: " + conCtrl.getErrorDetalle();									
							throw new Exception(strMensajeValidacion);
						}
					} else {
						strMensajeValidacion = conCtrl.getError() + " <BR/> DETALLE: " + conCtrl.getErrorDetalle();	
						throw new Exception(strMensajeValidacion);
					}
				} else {
					strMensajeValidacion = conCtrl.getError() + " <BR/> DETALLE: " + conCtrl.getErrorDetalle();						
					throw new Exception(strMensajeValidacion);
				}
			} else {// RECIBO MANUAL
				iNumRecm = Integer.parseInt(txtNumRec.getValue().toString());
				
				// guardar el header del recibo
				reciboHeader = conCtrl.registrarRecibo(session, tx,
						iNumRec, iNumRecm, sCodComp, dMontoAplicar,
						dMontoRec, dCambio, sConcepto, dFecha, dHora,
						iCodCli, sNomCli, sCajero, iCajaId, f55ca01.getId()
								.getCaco(), vautoriz[0].getId()
								.getCoduser(), valoresJdeIns[0], 0, "", 0, dFecha, hFac
								.getCodunineg().trim(), "", hFac
								.getMoneda());
				
				if (reciboHeader) {
					// guardar detalle de recibo
					lstMetodosPago2 = ponerCodigoBanco(lstMetodosPago);
					reciboDetalle = conCtrl.registrarDetalleRecibo(session,tx, iNumRec, iNumRecm, sCodComp,
							lstMetodosPago2, iCajaId, f55ca01.getId().getCaco(), valoresJdeIns[0]);
					
					if (reciboDetalle) {
						// llenar enlaces entre recibo y factura leer facturas seleccionadas
						for (int h = 0; h < lstFacturasSelected.size(); h++) {
							iNumFac[h] = ((Hfactura) lstFacturasSelected.get(h)).getNofactura();
							dMonto[h] = ((Hfactura) lstFacturasSelected.get(h)).getTotal();
							sPartida[h] = "";
							sCodunineg[h] = ((Hfactura) lstFacturasSelected.get(h)).getCodunineg();
							sCodsuc[h] = ((Hfactura) lstFacturasSelected.get(h)).getCodsuc();
							sTipoDoc[h] = ((Hfactura) lstFacturasSelected.get(h)).getTipofactura();
							
						}
						
						//--------------------------------------------------------
						enlace = conCtrl.fillEnlaceReciboFac(session, tx,iNumRec, 
								sCodComp, iNumFac, dMonto, sTipoDoc, iCajaId, 
								sCodsuc, sPartida,sCodunineg,valoresJdeIns[0], iCodCli,
								new FechasUtil().DateToJulian(dFecha) );
						//--------------------------------------------------------
						
//						enlace = true;
						if (enlace) {
							
							// guardar solicitudes
							if (lstSolicitud != null&& !lstSolicitud.isEmpty()) {
								for (int i = 0; i < lstSolicitud.size(); i++) {
									sol = (Solicitud) lstSolicitud.get(i);
									iNumSol = solCtrl.getNumeroSolicitud();
									if (iNumSol > 0) {
										bSolicitud = solCtrl.registrarSolicitud(session,tx,iNumSol,iNumRec,valoresJdeIns[0],iCajaId,sCodComp,
														f55ca01.getId().getCaco(),sol.getId().getReferencia(),sol.getAutoriza(),
														dFecha, sol.getObs(),sol.getMpago(),sol.getMonto(),sol.getMoneda());
									} else {
										strMensajeValidacion = solCtrl.getError() + " <BR/> DETALLE: " + solCtrl.getErrorDetalle();						
										throw new Exception(strMensajeValidacion);
									}
								}
							} else {
								bSolicitud = true;
							}
						} else {
							strMensajeValidacion = conCtrl.getError() + " <BR/> DETALLE: " + conCtrl.getErrorDetalle();						
							throw new Exception(strMensajeValidacion);
						}
					} else {
						strMensajeValidacion = conCtrl.getError() + " <BR/> DETALLE: " + conCtrl.getErrorDetalle();						
						throw new Exception(strMensajeValidacion);
					}
				} else {
					strMensajeValidacion = conCtrl.getError() + " <BR/> DETALLE: " + conCtrl.getErrorDetalle();						
					throw new Exception(strMensajeValidacion);
				}
			}
			//
			if (bSolicitud) {
				//&& =======  registrar Cambio
				String sLblCambio1 = "", sLblCambio2 = "", sCambio1 = "", sCambio2 = "";
				String[] sLblCambios1, sLblCambios2;
				sLblCambio1 = lblCambio.getValue().toString();
				sLblCambio2 = lblCambioDomestico.getValue().toString();
				BigDecimal bdTasa = BigDecimal.ZERO;
				
				if (txtCambio.getValue().toString().trim().equals("")) {
					sCambio1 = txtCambioForaneo.getValue().toString();
					sCambio2 = txtCambioDomestico.getValue().toString();
					sLblCambios1 = sLblCambio1.trim().split(" ");
					sLblCambios2 = sLblCambio2.trim().split(" ");
					//
					sLblCambio1 = sLblCambios1[sLblCambios1.length - 1].substring(0, 3);
					sLblCambio2 = sLblCambios2[sLblCambios1.length - 1].substring(0, 3);
					//
					if (m.get("bdTasa") != null) {
						bdTasa = (BigDecimal) m.get("bdTasa");
					}
					cambio = conCtrl.registrarCambio(session, tx, iNumRec,sCodComp, sLblCambio1, divisas.formatStringToDouble(sCambio1),iCajaId, f55ca01.getId().getCaco(), bdTasa,valoresJdeIns[0]);
					cambio = conCtrl.registrarCambio(session, tx, iNumRec,sCodComp, sLblCambio2, divisas.formatStringToDouble(sCambio2),iCajaId, f55ca01.getId().getCaco(), bdTasa,valoresJdeIns[0]);						

				} else {
					sCambio1 = txtCambio.getValue().toString();
					sLblCambios1 = sLblCambio1.trim().split(" ");
					sLblCambio1 = sLblCambios1[sLblCambios1.length - 1].substring(0, 3);

					if (m.get("bdTasa") != null) 
						bdTasa = (BigDecimal) m.get("bdTasa");
					
					//&& ===== Calcular la tasa de cambio para grabar en Cambiodet.
					BigDecimal bdCmbMonFac = new BigDecimal(
							Double.toString(dMontoRec)).subtract(
							new BigDecimal(Double.toString(dMontoAplicar)));
					
					if(bdCmbMonFac.compareTo(BigDecimal.ZERO) > 0){
						bdTasa = new BigDecimal(Double.toString(divisas.
								formatStringToDouble(sCambio1))).divide(	
								bdCmbMonFac, 4, RoundingMode.HALF_UP );		
					}
					cambio = conCtrl.registrarCambio(session, tx, iNumRec,
								sCodComp, sLblCambio1, 
								divisas.formatStringToDouble(sCambio1),
								iCajaId, f55ca01.getId().getCaco(), bdTasa,valoresJdeIns[0]);
				}
			} else {
				throw new Exception("");
			}
			
			
		} catch (Exception ex) {
			LogCajaService.CreateLog("insertarRecibo", "ERR", ex.getMessage());
			bSolicitud = false;
			strMensajeValidacion = "No se pudo realizar la operacion: " + "</br> " + ex;
			throw new Exception(strMensajeValidacion);
		}
		return bSolicitud;
	}

	/** ******************************************************************************************* */
	/** ****************************************************************************************************** */
	public void ponerTasaSegunFecha(ValueChangeEvent ev) {
		Date dFecha = null;
		FechasUtil fecUtil = new FechasUtil();
		int iFecha = 0;
		TasaCambioCtrl tcCtrl = new TasaCambioCtrl();
		Tcambio[] tcambio = null;
		String sTasa = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			dFecha = (Date) txtFecham.getValue();
			String sFecha = sdf.format(dFecha);
			tcambio = tcCtrl.obtenerTasaCambioJDExFecha(sFecha);

			// poner tasa de cambio de JDE
			// Tcambio[] tcJDE = (Tcambio[])m.get("tcambio");
			for (int l = 0; l < tcambio.length; l++) {
				sTasa = sTasa + " " + tcambio[l].getId().getCxcrdc() + ": "+ tcambio[l].getId().getCxcrrd() + "<br/>";

			}

			lblTasaJDE.setValue(sTasa);
			m.put("tcambio", tcambio);
			m.put("lblTasaJDE", sTasa);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

/******************************************************************************************************** */
	public List ponerCodigoBanco(List<MetodosPago> lstMetodosPago) {
		F55ca022[] f55ca022 = null;
		String sBanco = "";
		
		List<MetodosPago> mpBancos = new ArrayList<MetodosPago>();
		
		try {
			if( !m.containsKey("f55ca022") )
					return lstMetodosPago;
			
			if(lstMetodosPago == null)
				return new ArrayList<MetodosPago>();			
			
			f55ca022 = (F55ca022[]) m.get("f55ca022");

			for (MetodosPago mpTmp : lstMetodosPago) {
				
				MetodosPago mp = mpTmp.clone();
				mpBancos.add(mp);
				
				if(mp.getMetodo().compareTo( MetodosPagoCtrl.EFECTIVO) == 0 || 
						mp.getMetodo().compareTo(MetodosPagoCtrl.TARJETA) == 0){
					continue;
				}
				final String codbanco = mp.getReferencia();
				
				F55ca022 f22 = (F55ca022) CollectionUtils.find(
						Arrays.asList(f55ca022), new Predicate() {
							public boolean evaluate(Object F22) {
								return codbanco.trim().compareTo(
									((F55ca022) F22).getId()
									.getBanco()	.trim()) == 0;
							}
						});
				if(f22 == null) continue;
				
				mp.setReferencia(String.valueOf(f22.getId().getCodb()));
					
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return mpBancos;
	}

/********************* Cancelar la devolucion de contado ************************************************ */		
	public void aceptarProcesaDevolucion(ActionEvent ev){
		List<Recibodet>   lstDetOriginal = null;
		List<MetodosPago> lstMetodosPago = null;
		List<Hfactura> lstFacturasSelected  = null;
		boolean aplicado = true;
		boolean hayPagoSp = false;
		
		Session sesion = null;
		Transaction tx = null;
		//Connection  cn = null;
		
		F55ca014 dtComp  = null;
		String msgErrorSp = new String("");
		String msgErrorDv = new String("");
		String codcomp = new String("");
		String codsuc = "";
		int caid = 0;
		int iNumrec = 0;
		Hfactura hFac = null;
		
		List<String[]>dtaFacs = new ArrayList<String[]>();		
		Date dtTimeIni = new Date();
		
		Object[] dtaCnDnc = null ;
		boolean conexionProcesoDonacion = false;
		
		ConfiguracionMensaje[] lstConfiguracionMensaje = (ConfiguracionMensaje[]) m.get("lstConfiguracionMensaje");
		
		try {
			
			LogCajaService.CreateLog("aceptarProcesaDevolucion", "INFO", "aceptarProcesaDevolucion-INICIO");
					
			dwProcesaDevolucion.setWindowState("hidden");
			
			lstDetOriginal = (ArrayList<Recibodet>)  m.get("lstDetalleReciboOriginal");
			lstMetodosPago = (ArrayList<MetodosPago>) m.get("lstDetalleReciboFactDev");
			Vf55ca01 caja  = ( (List<Vf55ca01>) m.get("lstCajas")).get(0);
			lstFacturasSelected = (ArrayList<Hfactura>) m.get("facturasSelected");	
			
			if(lstFacturasSelected == null || lstFacturasSelected.size()==0)
			{
				aplicado = false;
				
				int i = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(lstConfiguracionMensaje, "CAJA", "REC_CONTADO", "ACEPTARPROCESARDEV", "ACEPTARPROCESARDEV_02");
				
				if(i<0)
					msgErrorDv = "No se ha seleccionado facturas de contado o de devolución, favor volver a la selección de facturas";
				else
					msgErrorDv = lstConfiguracionMensaje[i].getDescripcionMensaje();
				
				throw new Exception(msgErrorDv);
			}
			
			hFac = lstFacturasSelected.get(0);
			
			caid = caja.getId().getCaid();
			codcomp = hFac.getCodcomp();
			codsuc = caja.getId().getCaco();
			
			//&& ****************** Crear el objeto con la informacion de las facturas
			for (Hfactura hf : lstFacturasSelected){ 
				String[] dta = new String[] {
					String.valueOf(hf.getNofactura()),
					String.valueOf(hf.getTotal()), hf.getPartida(),
					hf.getCodunineg(), hf.getCodsuc(), hf.getTipofactura() };
				dtaFacs.add(dta);
			}

			
			//&& ============validar que no se hayan pagado las facturas
			String msg = com.casapellas.controles.tmp.ReciboCtrl.validatePaymentForInvoice(lstFacturasSelected, caid) ;
			if(!msg.isEmpty()){				
				msgErrorDv = msg;
				throw new Exception(msg);
			}
			
			
			
//			LogCrtl.sendLogInfo("se grabo en recibofac:  reciboTmp: "+iNumrecTmp+" : proceso {{  "+ ev.hashCode() + " }}");
			
			//**********************
			
			if(lstMetodosPago.size()==0)
			{
				aplicado = false;
				
				int i = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(lstConfiguracionMensaje, "CAJA", "REC_CONTADO", "ACEPTARPROCESARDEV", "ACEPTARPROCESARDEV_03");
				
				if(i<0)
					msgErrorDv = "No hay métodos de pagos seleccionados al intentar realizar este recibo";
				else
					msgErrorDv = lstConfiguracionMensaje[i].getDescripcionMensaje();
				
				throw new Exception(msgErrorDv);
			}
			
			
			//&& ========= Aplicar el redondeo a los equivalentes del pago.
			for (int i = 0; i < lstMetodosPago.size(); i++) {
				MetodosPago m = lstMetodosPago.get(i);
				m.setEquivalente(new Divisas().roundDouble(m.getEquivalente(), 2));
				lstMetodosPago.set(i, m);
			}
			
			List<Recibodet> pagosSocket = (ArrayList<Recibodet>)
					CollectionUtils.select(lstDetOriginal, new Predicate() {
						public boolean evaluate(Object o) {
							Recibodet rd = (Recibodet)o;
							return (
								rd.getId().getMpago().compareTo(MetodosPagoCtrl.TARJETA)  == 0 &&
								rd.getVmanual().compareTo("2") == 0
							);
						}
				});
			
			hayPagoSp = (pagosSocket != null && !pagosSocket.isEmpty());
			
			//&& ======== Datos de la compania.
			dtComp = com.casapellas.controles.tmp.CompaniaCtrl
					.filtrarCompania((F55ca014[])m.get
					("cont_f55ca014"), codcomp);
			
			//=========================================
			//************************************************/
			//****** anulacion de pagos con ecommerce ********/ 
			boolean pagoEcommerce = false;
			CodeUtil.removeFromSessionMap("pfc_DataPayEcommerce");
			
			String anularCobroEcommerce = PosCtrl.anularCargoPorEcommerce(
					lstDetOriginal, 
					dtComp.getId().getC4rp01d1().trim(), 
					dtComp.getId().getC4prt().trim() );
			
			pagoEcommerce = anularCobroEcommerce != null;
			
			if( pagoEcommerce && !anularCobroEcommerce.isEmpty() ) {
				msgErrorSp = anularCobroEcommerce;
				throw new Exception(msgErrorSp);
			}
			if( pagoEcommerce && anularCobroEcommerce.isEmpty() ) {
				
				EcommerceTransaction et = (EcommerceTransaction)CodeUtil.getFromSessionMap("pfc_DataPayEcommerce");
				Recibodet rd = lstDetOriginal.get(0);
				
				lstMetodosPago.get(0).setStatuspago("ANL");
				lstMetodosPago.get(0).setTerminal( et.getTerminalid() );				//codigo de la terminal.
				lstMetodosPago.get(0).setReferencia3( rd.getId().getRefer3() );		 	//ult 4 de tarjeta
				lstMetodosPago.get(0).setReferencia4( et.getReferencenumber() );		//referencia
				lstMetodosPago.get(0).setReferencia5( et.getAuthorizationNumber() );	//autorizacion
				lstMetodosPago.get(0).setReferencia6( rd.getRefer6() ) ;				//Fecha socket
				lstMetodosPago.get(0).setReferencia7( et.getTransactionid() );			//systraceno (transaccion original)
				lstMetodosPago.get(0).setNombre( rd.getNombre() );
				
			}
			//&& ======== buscar en el recibo pagos con socket pos.
			if(hayPagoSp && !pagoEcommerce ){
				msgErrorSp = new PosCtrl().aplicarDevolucionSp( lstDetOriginal, lstMetodosPago, dtComp);
			}
				
			sesion = HibernateUtilPruebaCn.currentSession();			
			tx = sesion.beginTransaction();
			
//			As400Connection as400connection = new As400Connection();
//			cn = as400connection.getJNDIConnection("DSMCAJA2");
//			cn.setAutoCommit(false);
			
			aplicado = insertarDevolucion(sesion, tx, null);
			
			if(!aplicado){ 
				
				int i = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(lstConfiguracionMensaje, "CAJA", "REC_CONTADO", "ACEPTARPROCESARDEV", "ACEPTARPROCESARDEV_04");
				
				if(i<0)
					msgErrorDv = "Recibo no aplicado, error al guardar datos de recibo, Favor intente de nuevo";
				else
					msgErrorDv = lstConfiguracionMensaje[i].getDescripcionMensaje();
				
				
				throw new Exception(msgErrorDv);
			}
			
			iNumrec = Integer.parseInt(m.get("iNumRec").toString());
			String sAplicado  = txtMontoAplicarDev.getValue().toString().replace(",", "");
			double dTotalAplicar = new Divisas().roundDouble(Double.parseDouble(sAplicado));
			
			if(iNumrec==0)
			{
				aplicado = false;
				
				int i = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(lstConfiguracionMensaje, "CAJA", "REC_CONTADO", "ACEPTARPROCESARDEV", "ACEPTARPROCESARDEV_05");
				
				if(i<0)
					msgErrorDv = "No se pudo obtener numero de recibo valido, favor intente nuevamente procesar el pago de esta factura";
				else
					msgErrorDv = lstConfiguracionMensaje[i].getDescripcionMensaje();
				
				throw new Exception(msgErrorDv);
			}
			
			aplicado = generarAsientosDevolucion(sesion, tx,  
						caid,codcomp.trim(), dTotalAplicar, 
						lstFacturasSelected, lstMetodosPago, hFac, 
						iNumrec, dtComp.getId().getC4bcrcd(), new Date());
			
			//&& ======== Aplicar asientos de diario en jde
			if(!aplicado){ 
			
				int i = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(lstConfiguracionMensaje, "CAJA", "REC_CONTADO", "ACEPTARPROCESARDEV", "ACEPTARPROCESARDEV_06");
				
				if(i<0)
					msgErrorDv = "Recibo no aplicado, error al guardar datos  en JdEdwards, favor intente nuevamente";
				else
					msgErrorDv = lstConfiguracionMensaje[i].getDescripcionMensaje();
				
				
				throw new Exception(msgErrorDv);
			}
			//&& ======== Actualizar los estados de las facturas por traslado.
			for (Hfactura hf : lstFacturasSelected) {
				Trasladofac tf = new TrasladoCtrl().buscarTrasladofac(sesion, hf, caid, 0, "");
				if(tf == null) 
					continue;
				
				aplicado = TrasladoCtrl.actualizarEstadoTraslado(sesion, tf, "P");
				
				if(!aplicado){
					
					int i = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(lstConfiguracionMensaje, "CAJA", "REC_CONTADO", "ACEPTARPROCESARDEV", "ACEPTARPROCESARDEV_07");
					
					if(i<0)
						msgErrorDv = "Recibo no aplicado: Error de actualización de traslado de factura " +hf.getNofact();
					else
						msgErrorDv = lstConfiguracionMensaje[i].getDescripcionMensaje().replace("@NumeroFactura", String.valueOf(hf.getNofact()));
					
					
					throw new Exception(msgErrorDv);
				}
			}
			
		
			
			if( aplicado && CodeUtil.getFromSessionMap("fdc_dncDonacionJDE_devolver") != null ){
				
				
				DonacionesCtrl.numrec = iNumrec;
				DonacionesCtrl.tiporecibo = "DCO";
				
				List<DncDonacionJde> nobatchs =	(ArrayList<DncDonacionJde>) CodeUtil.getFromSessionMap("fdc_dncDonacionJDE_devolver");
				msgErrorDv = DonacionesCtrl.revertirDonacion(nobatchs, ((Vautoriz[]) m.get("sevAut"))[0]);
				aplicado = msgErrorDv.isEmpty();
				
				
			}
			
			LogCajaService.CreateLog("aceptarProcesaDevolucion", "INFO", "aceptarProcesaDevolucion-FIN");
		} catch (Exception e) {
			LogCajaService.CreateLog("aceptarProcesaDevolucion", "ERR", e.getMessage());
			e.printStackTrace();
			aplicado = false;
			
			int i = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(lstConfiguracionMensaje, "CAJA", "REC_CONTADO", "ACEPTARPROCESARDEV", "ACEPTARPROCESARDEV_EX");
			
			if(i<0) {
				msgErrorDv = "Recibo no aplicado: Error interno de aplicación, " +
						"favor intente de nuevo";
				LogCajaService.CreateLog("aceptarProcesaDevolucion", "ERR", msgErrorDv);
			}
			else {
				msgErrorDv = lstConfiguracionMensaje[i].getDescripcionMensaje().replace("@Error", e.getMessage());
				LogCajaService.CreateLog("aceptarProcesaDevolucion", "ERR", msgErrorDv);
			}
		}finally{
			
			dwProcesa.setWindowState("normal");
			dwProcesa.setStyle("width:370px;height:160px");
			lblMensajeValidacion.setValue("Devolución aplicada correctamente\n " + msgErrorSp);
			
			boolean actualizaRf = false;
			
			try {
				
				if(aplicado) {
					tx.commit();
					BitacoraSecuenciaReciboService.actualizarSatisfactorioLogReciboNumerico(caid,iNumrec,codcomp,codsuc,"DCO");
				}
				else {
					tx.rollback();
					try {
						List<CustomEmailAddress> list = new ArrayList<CustomEmailAddress>() {
							{
								add(new CustomEmailAddress("jaburto@casapellas.com"));
								add(new CustomEmailAddress("mpomares@casapellas.com"));
								add(new CustomEmailAddress("giovanny.acevedo@ahinko.com"));
								add(new CustomEmailAddress("byron.canales@ahinko.com"));							
							}
						};
						
						String error="Caja:" + caid + "Compania:"+ codcomp + " Numero Recibo:"+ iNumrec + " Posible mensaje mostrado:"+ msgErrorDv;
					MailHelper.SendHtmlEmail(new CustomEmailAddress(PropertiesSystem.WEBMASTER_EMAIL_ADRESS), list, " Fallo en generacion de recibo",error);
					}catch(Exception e) {}
						
				}
				
			} catch (Exception e) {
				LogCajaService.CreateLog("aceptarProcesaDevolucion", "ERR", e.getMessage());
				msgErrorDv = "Error en confirmación de registro de valores: Caja";
				aplicado = false;
				
			}
 
			if(aplicado){

				CodeUtil.removeFromSessionMap("fdc_dncDonacionJDE_devolver");
				
				dwDevolucion.setWindowState("hidden");
				
				//&& ======== Imprimir el recibo
				if (chkImprimirDevolucion.isChecked()) {
					CtrlCajas.imprimirRecibo(caid, iNumrec, codcomp, 
							
							//"000" + hFac.getCodsuc().trim(),
							CodeUtil.pad( hFac.getCodsuc().trim() , 5, "0"), 
							
							"DCO" , false);
				}
				
				//&& ======== refrescar lista de facturas.
				lstHfacturasContado = (ArrayList<Hfactura>) m.get
										("lstHfacturasContado");
				for (Hfactura hf : lstFacturasSelected) {
					final int numero = hf.getNofactura();
					final int codcli = hf.getCodcli();
					final int fecha = hf.getFechajulian();
					
					CollectionUtils.filter(lstHfacturasContado, new Predicate() {
						public boolean evaluate(Object hfac) {
							Hfactura f = (Hfactura)hfac;
							return !(
								f.getNofactura() == numero && 
								f.getCodcli() == codcli && 
								f.getFechajulian() == fecha);
						}
					});
				}
				m.put("lstHfacturasContado", lstHfacturasContado);
				gvHfacturasContado.dataBind();
				
			}else{
				lblMensajeValidacion.setValue(msgErrorDv + "\n"+ msgErrorSp);
			}
			
			try {
				HibernateUtilPruebaCn.closeSession(sesion);
			} catch (Exception e) {
				LogCajaService.CreateLog("aceptarProcesaDevolucion", "ERR", e.getMessage());
				e.printStackTrace();
			}

		}
	}
/******************************************************************************************************** */	
/************************************************************************************************************************ */
	public void aceptarProcesaDevolucion1(ActionEvent e) {
		boolean bDevolucionInertada = false;
		Connection cn = null;
		Session s = null;
		Transaction tx = null;
		int iNumrec = 0;
		boolean bContabilizado = false;
		Divisas divisas = new Divisas();
		String sMonedaBase = "";
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		
		try {
			if(m.get("con_processRecibo") == null){
				Vf55ca01 f55ca01 = (Vf55ca01) ((List) m.get("lstCajas")).get(0);
				m.put("con_processRecibo", "1");
				boolean imprimir = chkImprimirDevolucion.isChecked();
				s = HibernateUtilPruebaCn.currentSession();
				tx = s.getTransaction();
				if (tx == null || !tx.isActive()) {
					tx = s.beginTransaction();
				}
				// obtener conexion del datasource
				As400Connection as400connection = new As400Connection();
				cn = as400connection.getJNDIConnection("DSMCAJA2");
				cn.setAutoCommit(false);
				
				List lstMetodosPago = (List) m.get("lstDetalleReciboFactDev");
				List lstDetalleReciboOriginal = (List) m.get("lstDetalleReciboOriginal");
				if((f55ca01.getId().getCasktpos()+"").equals("Y")){				
					bDevolucionInertada = anularPagosSPDev(lstDetalleReciboOriginal,lstMetodosPago);
				}else{
					bDevolucionInertada = true;
				}
				//lstMetodosPago = (List) m.get("lstDetalleReciboFactDev");
				
				if(bDevolucionInertada){
					lstMetodosPago = (List) m.get("lstDetalleReciboFactDev");
					bDevolucionInertada = insertarDevolucion(s, tx, cn);
		
					
					List lstFacturasSelected = (List) m.get("facturasSelected");				
					Hfactura hFac = (Hfactura) lstFacturasSelected.get(0);
					//obtener companias x caja
					sMonedaBase = cCtrl.sacarMonedaBase((F55ca014[])m.get("cont_f55ca014"), hFac.getCodcomp());
		
					if (bDevolucionInertada) {
						iNumrec = Integer.parseInt(m.get("iNumRec").toString());
						double dTotalAplicar = divisas.roundDouble4(divisas.formatStringToDouble(txtMontoAplicarDev.getValue().toString()));
						

						//------ Verificar Traslados de Factura.
						if(bDevolucionInertada){
							TrasladoCtrl tcCtrl = new TrasladoCtrl();
							Trasladofac tf = null;
							for (Iterator iter = lstFacturasSelected.iterator(); iter.hasNext();) {
								Hfactura hFactura = (Hfactura) iter.next();
								tf = tcCtrl.buscarTrasladofac(s, hFactura, f55ca01.getId().getCaid(), 0, "");
								if(tf!=null){
									String sEstadotr = (tf.getCaidorig() == tf.getCaidprop())?"E":"R";
									bDevolucionInertada = tcCtrl.actualizarEstadoTraslado(s, tf, sEstadotr);
									if(!bDevolucionInertada){
										lblMensajeValidacion.setValue(tcCtrl.getErrorTf().toString().split("@")[1]);
										break;
									}
								}
							}
						}
						if (bDevolucionInertada) {
							tx.commit();
							cn.commit();
							cn.close();
							m.remove("con_processRecibo");
							m.remove("facturasSelected");
							dwProcesaDevolucion.setWindowState("hidden");
							dwDevolucion.setWindowState("hidden");
							dwProcesa.setWindowState("hidden");
							if (imprimir) {
								// imprime el recibo de devolucion de contado
								BigDecimal bdNumrec = new BigDecimal(iNumrec);
								getP55recibo().setIDCAJA(new BigDecimal(f55ca01.getId().getCaid()));
								getP55recibo().setNORECIBO(bdNumrec);
								getP55recibo().setIDEMPRESA(hFac.getCodcomp().trim());
								
								//getP55recibo().setIDSUCURSAL("000" + hFac.getCodsuc().trim());
								getP55recibo().setIDSUCURSAL( CodeUtil.pad( hFac.getCodsuc().trim(), 5, "0")  );
								
								getP55recibo().setTIPORECIBO("DCO");
								getP55recibo().setRESULTADO("");
								getP55recibo().setCOMANDO("");
								getP55recibo().invoke();
								getP55recibo().getRESULTADO();
								if (getP55recibo().getRESULTADO().equals("1")) {// no se pudo imprimir
									lblResultadoRec.setValue("No se pudo imprimir");
								} else if (getP55recibo().getRESULTADO().equals("0")) {
									lblResultadoRec.setValue("Impresion Exitosa");
								}
							}
						} else {
							dwProcesaDevolucion.setWindowState("hidden");
							dwDevolucion.setWindowState("hidden");
							dwProcesa.setWindowState("normal");
							dwProcesa.setStyle("width:370px;height:160px");
		
							tx.rollback();
							cn.rollback();
							cn.close();
						}
					} else {
						dwProcesaDevolucion.setWindowState("hidden");
						dwDevolucion.setWindowState("hidden");
						dwProcesa.setWindowState("normal");
						dwProcesa.setStyle("width:370px;height:160px");
		
						tx.rollback();
						cn.rollback();
						cn.close();
					}
		
					dwProcesaDevolucion.setWindowState("hidden");
					dwDevolucion.setWindowState("hidden");
		
					gvHfacturasContado.dataBind();
					List lstSelectedFacs = new ArrayList();
					m.put("facturasSelected", lstSelectedFacs);
					listarFacturasContadoDelDia();
					String sMensaje2 = "";
					List lstHfacturasContado = null;
					if (m.get("lstHfacturasContado") != null) {
						lstHfacturasContado = (List) m.get("lstHfacturasContado");
					}
					if (lstHfacturasContado == null || lstHfacturasContado.isEmpty()) {
						sMensaje2 = "No se encontraron resultados";
						getSMensaje();
						m.put("mostrar", "m");
					} else {
						if (m.get("mostrar") != null) {
							m.remove("mostrar");
						}
					}
		
					sMensaje = sMensaje2;
					txtMensaje.setValue(sMensaje2);
					m.put("sMensaje", sMensaje2);
					cmbFiltroMonedas.dataBind();
					cmbFiltroFacturas.dataBind();
					gvHfacturasContado.dataBind();
					m.remove("con_processRecibo");
				}else{//no se pudo anular la transaccion con el banco
					dwProcesaDevolucion.setWindowState("hidden");
					dwDevolucion.setWindowState("hidden");
					dwProcesa.setWindowState("normal");
					dwProcesa.setStyle("width:370px;height:160px");
				}
			}
		} catch (Exception ex) {
			try {
				m.remove("con_processRecibo");
				dwProcesaDevolucion.setWindowState("hidden");
				dwDevolucion.setWindowState("hidden");
				lblMensajeValidacion.setValue("No se pudo realizar la operación!!! " + ex);
				dwProcesa.setWindowState("normal");
				dwProcesa.setStyle("width:370px;height:160px");
				ex.printStackTrace();
				tx.rollback();
				cn.rollback();
				cn.close();
			} catch (Exception ex2) {
				ex2.printStackTrace();
			}
		}finally{
			try {cn.close();} catch (Exception e2) {}
		}
	}

	/** *********************************************************************************************************************************** */
	public void procesarDevolucion(ActionEvent ev) {
		String sNoReciboManual, sTipoRecibo;
		boolean bValido = false;
		try {
			List lstDetalleReciboFactDev = (List) m.get("lstDetalleReciboFactDev");
			sTipoRecibo = cmbTiporeciboDev.getValue().toString();

			bValido = validarProcesarDevolucion(lstDetalleReciboFactDev,sTipoRecibo);
			if (bValido) {
				dwProcesaDevolucion.setWindowState("normal");
				chkImprimirDevolucion.setChecked(false);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/** ***************************************************************************************************************************** */
	public void cancelarProcesaDevolucion(ActionEvent e) {
		dwProcesaDevolucion.setWindowState("hidden");
	}

	/** ***************************************************************************************************************************** */
	public boolean insertarDevolucion(Session session, Transaction tx,Connection cn1) {
		ReciboCtrl conCtrl = new ReciboCtrl();
		boolean reciboHeader = false, reciboDetalle = false, actualizado = false, enlace = false, cambio = false, bSolicitud = false;
		int iNumRec = 0, iCodCli = 0, iNumRecm = 0, iCajaId = 0;
		String sCodComp = null, sConcepto = null, sNomCli = null, sCajero = null;
		Date dFecha = new Date(), dHora = new Date();
		double dMontoRec = 0, dMontoAplicar = 0, dCambio = 0;
		List lstMetodosPago = null;

		try {
			LogCajaService.CreateLog("insertarDevolucion", "INFO", "insertarDevolucion - INICIO");
			
			Vf55ca01 f55ca01 = (Vf55ca01) ((List) m.get("lstCajas")).get(0);
			// obtener datos de factura
			List lstFacturasSelected = (List) m.get("facturasSelected");
			Hfactura hFac = (Hfactura) lstFacturasSelected.get(0);
			// ASIGNAR VALOR A PROPIEDADES
			// datos del recibo
			iCajaId = Integer.parseInt(m.get("sCajaId").toString());
			sCodComp = hFac.getCodcomp();

			iNumRec = conCtrl.obtenerUltimoRecibo(iCajaId, sCodComp) + 1;
			actualizado = conCtrl.actualizarNumeroRecibo(iCajaId, sCodComp.trim(), iNumRec);

			Vautoriz[] vautoriz = (Vautoriz[]) m.get("sevAut");

			if (iNumRec > 0) {
				dMontoAplicar = Double.parseDouble(txtMontoAplicarDev.getValue().toString());
				dMontoRec = Double.parseDouble(txtMontoProcesado.getValue().toString());

				sConcepto = txtConceptoDev.getValue().toString();
				iCodCli = hFac.getCodcli();
				sNomCli = hFac.getNomcli();
				sCajero = (String) m.get("sNombreEmpleado");
				lstMetodosPago = (List) m.get("lstDetalleReciboFactDev");

				// poner codigo a metodo
				List lstMetodosPago2 = new ArrayList();
				

				int[] iNumFac = new int[lstFacturasSelected.size()];
				double[] dMonto = new double[lstFacturasSelected.size()];
				String[] sPartida = new String[lstFacturasSelected.size()];
				String[] sCodunineg = new String[lstFacturasSelected.size()];
				String[] sCodsuc = new String[lstFacturasSelected.size()];
				String[] sTipoDoc = new String[lstFacturasSelected.size()];
				
				// Agregar a la bitacora de consecutivos
				BitacoraSecuenciaReciboService.insertarLogReciboNumerico(iCajaId,iNumRec,sCodComp,f55ca01.getId().getCaco(),"DCO");
				
				if (cmbTiporeciboDev.getValue().toString().equals("AUTOMATICO")) {// RECIBO AUTOMATICO
					
					// guardar el header del recibo
					reciboHeader = conCtrl.registrarRecibo(session, tx,iNumRec,
							iNumRecm, sCodComp, dMontoAplicar, dMontoRec, dCambio, 
							sConcepto, dFecha, dHora,iCodCli, sNomCli, sCajero, 
							iCajaId, f55ca01.getId().getCaco(), 
							vautoriz[0].getId().getCoduser(), "DCO", 
							Integer.parseInt(lblNoReciboOdev.getValue().toString()),
							valoresJdeIns[0], 0, dFecha,((Hfactura) lstFacturasSelected.get(0))
							.getCodunineg().trim(),"",hFac.getMoneda());
					
					
					if (reciboHeader) {
						// guardar detalle de recibo
						lstMetodosPago2 = ponerCodigoBanco(lstMetodosPago);
						reciboDetalle = conCtrl.registrarDetalleRecibo(session,tx, iNumRec, iNumRecm, sCodComp,lstMetodosPago2, iCajaId, f55ca01.getId()
										.getCaco(), "DCO");
						if (reciboDetalle) {
							// llenar enlaces entre recibo y factura
							// leer facturas seleccionadas
							for (int h = 0; h < lstFacturasSelected.size(); h++) {
								iNumFac[h] = ((Hfactura) lstFacturasSelected.get(h)).getNofactura();
								dMonto[h] = ((Hfactura) lstFacturasSelected.get(h)).getTotal();
								sPartida[h] = "";
								sCodunineg[h] = ((Hfactura) lstFacturasSelected.get(h)).getCodunineg();
								sCodsuc[h] = ((Hfactura) lstFacturasSelected.get(h)).getCodsuc();
								sTipoDoc[h] = ((Hfactura) lstFacturasSelected.get(h)).getTipofactura();
							}
							enlace = true;
							enlace = conCtrl.fillEnlaceReciboFac(session, tx,iNumRec, 
									sCodComp, iNumFac, dMonto,sTipoDoc, iCajaId, 
									sCodsuc, sPartida, sCodunineg,"DCO", iCodCli,
									new FechasUtil().DateToJulian(dFecha)  );
							
							if (enlace) {
								// actualizar nuemro de recibo
								//actualizado = conCtrl.actualizarNumeroRecibo(cn, iCajaId, sCodComp.trim(), iNumRec);
							} else {
								return false;
							}
						} else {
							return false;
						}
					} else {
						return false;
					}
				} else {// RECIBO MANUAL
					iNumRecm = Integer.parseInt(txtNumeroReciboManulDev.getValue().toString());

					// guardar el header del recibo
					reciboHeader = conCtrl.registrarRecibo(session, tx,
							iNumRec, iNumRecm, sCodComp, dMontoAplicar,
							dMontoRec, dCambio, sConcepto, dFecha, dHora,
							iCodCli, sNomCli, sCajero, iCajaId,
							f55ca01.getId().getCaco(), 
							vautoriz[0].getId().getCoduser(), "DCO", 
							Integer.parseInt(lblNoReciboOdev.getValue().toString()), 
							valoresJdeIns[0], 0, dFecha, ((Hfactura) lstFacturasSelected.get(0))
								.getCodunineg().trim(), "", hFac.getMoneda());
					
					
					if (reciboHeader) {
						// guardar detalle de recibo
						reciboDetalle = conCtrl.registrarDetalleRecibo(session,tx, iNumRec, iNumRecm, sCodComp,
								lstMetodosPago2, iCajaId, f55ca01.getId().getCaco(), "DCO");
						if (reciboDetalle) {
							// llenar enlaces entre recibo y factura
							// leer facturas seleccionadas
							for (int h = 0; h < lstFacturasSelected.size(); h++) {
								iNumFac[h] = ((Hfactura) lstFacturasSelected.get(h)).getNofactura();
								dMonto[h] = ((Hfactura) lstFacturasSelected.get(h)).getTotal();
								sPartida[h] = "";
								sCodunineg[h] = ((Hfactura) lstFacturasSelected.get(h)).getCodunineg();
								sCodsuc[h] = ((Hfactura) lstFacturasSelected.get(h)).getCodsuc();
								sTipoDoc[h] = ((Hfactura) lstFacturasSelected.get(h)).getTipofactura();
							}
							enlace = conCtrl.fillEnlaceReciboFac(session, tx,
									iNumRec, sCodComp, iNumFac, dMonto,
									sTipoDoc, iCajaId, sCodsuc, sPartida,
									sCodunineg,"DCO", iCodCli, 
									new FechasUtil().DateToJulian(dFecha) );
							
							if (enlace) {
								// actualizar numero de recibo
								//actualizado = conCtrl.actualizarNumeroRecibo(cn, iCajaId, sCodComp.trim(), iNumRec);
							} else {
								return false;
							}
						} else {
							return false;
						}
					} else {
						return false;
					}
				}
			} else {
				return false;
			}
			
		
			LogCajaService.CreateLog("insertarDevolucion", "INFO", "insertarDevolucion - FIN");
			
			m.put("iNumRec", iNumRec);
			
		} catch (Exception ex) {
			LogCajaService.CreateLog("insertarDevolucion", "ERR", ex.getMessage());			 
		}
		return actualizado;
	}

/************************************************************************************************************************************* */
	public void mostrarDetalleDevolucion(ActionEvent e) {
		dgwDetalleContado.setWindowState("normal");
		String sMonedaBase = "";
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		try {
			int iNoFactura = Integer.parseInt(lblNoFactDev.getValue().toString());
			String sTipoFactura = lblTipoDocFactDev.getValue().toString();
			String sCodunineg = lblCoduninegDev.getValue().toString();
			String sCodsuc = lblCodsucDev.getValue().toString();

			List lstFacsActuales = (List) m.get("facturasSelected");
			Hfactura hFac =  (Hfactura) lstFacsActuales.get(0);
			Divisas divisas = new Divisas();
			
			//obtener companias x caja
			
			sMonedaBase = cCtrl.sacarMonedaBase((F55ca014[])m.get("cont_f55ca014"), hFac.getCodcomp());
			
			for (int i = 0; i < lstFacsActuales.size(); i++) {
				hFac = (Hfactura) lstFacsActuales.get(i);
				if (hFac.getNofactura() == iNoFactura && hFac.getTipofactura().trim().equals(sTipoFactura) && hFac.getCodsuc().equals(sCodsuc) && hFac.getCodunineg().equals(sCodunineg)) {
					// poner valor a labels del detalle
					txtNofactura = iNoFactura + "";
					txtFechaFactura = ((Hfactura) lstFacsActuales.get(i)).getFecha();
					txtCliente = ((Hfactura) lstFacsActuales.get(i)).getNomcli()+ " (Nombre)";
					txtCodigoCliente = ((Hfactura) lstFacsActuales.get(i)).getCodcli()+ " (Código)";
					txtUnineg = ((Hfactura) lstFacsActuales.get(i)).getCodunineg()+ " "+ ((Hfactura) lstFacsActuales.get(i)).getUnineg();

					txtTasaDetalle = ((Hfactura) lstFacsActuales.get(i)).getTasa()+ "";

					if (hFac.getCodVendedor() > 0) {
						EmpleadoCtrl emp = new EmpleadoCtrl();
						lblVendedorCont = "Vendedor:";
						txtVendedorCont = emp.buscarEmpleadoxCodigo(hFac.getCodVendedor());
					} else {
						lblVendedorCont = "Facturador:";
						txtVendedorCont = hFac.getHechopor();
					}

					txtSubtotal = ((Hfactura) lstFacsActuales.get(i)).getSubtotal();
					// actualizar lista de detalle buscar detalle
					lstDfacturasContado = formatDetalle(hFac);
					hFac.setDfactura(lstDfacturasContado);

					txtIva = ((Hfactura) lstFacsActuales.get(i)).getIva();
					txtTotal = divisas.formatDouble(((Hfactura) lstFacsActuales.get(i)).getTotal());
					m.put("lstDfacturasContado", lstDfacturasContado);
					// poner monedas

					lstMonedasDetalle = new ArrayList();
					String moneda = ((Hfactura) lstFacsActuales.get(i)).getMoneda();
					if (moneda.equals("COR")) {
						lstMonedasDetalle.add(new SelectItem(sMonedaBase, sMonedaBase));
						txtTasaDetalle = "";
						lblTasaDetalle = "";

					} else {
						lstMonedasDetalle.add(new SelectItem(moneda, moneda));
						lstMonedasDetalle.add(new SelectItem(sMonedaBase, sMonedaBase));
						lblTasaDetalle = "Tasa de Cambio: ";
					}
					m.put("Hfactura", hFac);
					List oldDfac = hFac.getDfactura();
					m.put("oldDfac", oldDfac);
					m.put("lstMonedasDetalle", lstMonedasDetalle);
					cmbMonedaDetalle.dataBind();
					gvDfacturasContado.dataBind();
					break;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/** *********************************************************************************************************************************** */
	public void mostrarDetalleFacturaOriginal(ActionEvent e) {
		dgwDetalleContado.setWindowState("normal");
		FacturaCrtl facCtrl = new FacturaCrtl();
		Hfactura hFacDev = null;
		String sMonedaBase = "";
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		
		try {
			
			int iNoFactura = Integer.parseInt(lblNodoco.getValue().toString());
			String sTipoFactura = lblTipodoco.getValue().toString();
			List lstFacsActuales = (List) m.get("facturasSelected");
			hFacDev = (Hfactura) lstFacsActuales.get(0);
			Hfactura hFac = new Hfactura();
			Divisas divisas = new Divisas();

			int fechadoco = Integer.parseInt( String.valueOf( CodeUtil.getFromSessionMap("fdc_FechaFacturaOriginal" ) ) ) ; 
			
			hFac = facCtrl.getFacturaOriginal(iNoFactura, sTipoFactura, hFacDev.getCodsuc(), hFacDev.getCodcomp().trim(), hFacDev.getCodunineg(), fechadoco );
		 
			sMonedaBase = cCtrl.sacarMonedaBase((F55ca014[])m.get("cont_f55ca014"), hFac.getCodcomp());
			
			// poner valor a labels del detalle
			txtNofactura = iNoFactura + "";
			txtFechaFactura = hFac.getFecha();
			txtCliente = hFac.getNomcli() + " (Nombre)";
			txtCodigoCliente = hFac.getCodcli() + " (Código)";
			txtUnineg = hFac.getCodunineg() + " " + hFac.getUnineg();

			txtTasaDetalle = hFac.getTasa() + "";

			if (hFac.getCodVendedor() > 0) {
				EmpleadoCtrl emp = new EmpleadoCtrl();
				lblVendedorCont = "Vendedor:";
				txtVendedorCont = emp.buscarEmpleadoxCodigo(hFac.getCodVendedor());
			} else {
				lblVendedorCont = "Facturador:";
				txtVendedorCont = hFac.getHechopor();
			}

			txtSubtotal = hFac.getSubtotal();
			// actualizar lista de detalle buscar detalle
			lstDfacturasContado = formatDetalle(hFac);
			hFac.setDfactura(lstDfacturasContado);

			txtIva = hFac.getIva();
			txtTotal = divisas.formatDouble(hFac.getTotal());
			m.put("lstDfacturasContado", lstDfacturasContado);
			// poner monedas

			lstMonedasDetalle = new ArrayList();
			String moneda = hFac.getMoneda();
			if (moneda.equals("COR")) {
				lstMonedasDetalle.add(new SelectItem(sMonedaBase, sMonedaBase));
				txtTasaDetalle = "";
				lblTasaDetalle = "";

			} else {
				lstMonedasDetalle.add(new SelectItem(moneda, moneda));
				lstMonedasDetalle.add(new SelectItem(sMonedaBase, sMonedaBase));
				lblTasaDetalle = "Tasa de Cambio: ";
			}
			m.put("Hfactura", hFac);
			List oldDfac = hFac.getDfactura();
			m.put("oldDfac", oldDfac);
			m.put("lstMonedasDetalle", lstMonedasDetalle);
			cmbMonedaDetalle.dataBind();
			gvDfacturasContado.dataBind();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/** *********************************************************************************************************************************** */
	
	public boolean validarProcesarDevolucion( List<MetodosPago> lstFormasDePago, String sTipoRecibo) {
		String msgError = "";
				
		
		try {
			
			if (lstFormasDePago == null || lstFormasDePago.isEmpty()) {
				msgError = " No hay registrado formas de pago para la devolución ";
				return false;
			}
			
			BigDecimal montoAplicado = new BigDecimal ( txtMontoAplicarDev.getValue().toString().replace(",", "") );
			BigDecimal montoRecibido = new BigDecimal ( txtMontoProcesado.getValue().toString().replace(",", "") );
			
			montoAplicado = montoAplicado.setScale(2, RoundingMode.HALF_UP);
			montoRecibido = montoRecibido.setScale(2, RoundingMode.HALF_UP);
			
			if( montoAplicado.compareTo(montoRecibido ) != 0  ) {
				msgError = " El consolidado de montos no es igual al monto aplicado ";
				return false;
			}
		 
			BigDecimal totalPagosIngresados = CodeUtil.sumPropertyValueFromEntityList(lstFormasDePago, "equivalente", false);
			totalPagosIngresados = totalPagosIngresados.setScale(2, RoundingMode.HALF_UP);
			
			if( totalPagosIngresados.compareTo(montoAplicado) != 0  ) {
				msgError = " El consolidado de pagos ingresados no es igual al monto aplicado a devolver, Aplicado: " + montoAplicado.toString() + ", ingresado: " + totalPagosIngresados.toString();
				return false;
			}
			
			String concepto = txtConceptoDev.getValue().toString().trim();
			if( concepto.isEmpty() || !concepto.matches(PropertiesSystem.REGEXP_DESCRIPTION)) {
				msgError = "Concepto requerido o con valores no permitidos ";
				return false;
			}
			
			if(sTipoRecibo.compareTo("MANUAL") == 0) {
				if( !txtNumeroReciboManulDev.getValue().toString().trim().matches(PropertiesSystem.REGEXP_NUMBER)   ) {
					msgError = "Número de recibo manual inválido";
					return false;
				} 
				if( txtFechaManualDev.getValue() == null  ) {
					msgError = " La fecha para el recibo manual es inválida ";
					return false;
				}
			}	
			
			
		}catch(Exception e) {
			e.printStackTrace(); 
			msgError = "Error al validar datos para aplicar devolucion ";
		}finally {
			
			  if( !msgError.isEmpty() ) {
				
				  lblMensajeValidacion.setValue(msgError);
				  dwProcesa.setStyle("width:390px;height: 150px;" );
				  dwProcesa.setWindowState("normal");
				  
			  }
			
		}
		
		return msgError.isEmpty();
	}
	
	public boolean validarProcesarDevolucion1(List lstDetalleReciboFactDev,String sTipoRecibo) {
		boolean bValido = true;
		int y = 160;
		String sMensajeError = "";
        Recibodet rd = null;
		Vf55ca01 f55ca01 = null;
		String montoDev = "", montoFac = "";
		try {
			f55ca01 = (Vf55ca01)((List)m.get("lstCajas")).get(0);
			
			restablecerEstiloProcesarDev();
			// expresion regular de valores alfanumericos
			Matcher matAlfa = null;
			String sConcepto = txtConceptoDev.getValue().toString();
			Pattern pAlfa = Pattern.compile("^[A-Za-z0-9-.;,\\p{Blank}]+$");
			matAlfa = pAlfa.matcher(sConcepto);

			if((f55ca01.getId().getCasktpos()+"").equals("Y")){
				List lstDetalleReciboOriginal = (List)m.get("lstDetalleReciboOriginal");
				for(int i = 0; i < lstDetalleReciboOriginal.size();i++){
					rd = (Recibodet)lstDetalleReciboOriginal.get(i);
					if(rd.getId().getMpago().equals(MetodosPagoCtrl.TARJETA) && rd.getVmanual().equals("2")){
						montoDev = lblMontoFactDev.getValue().toString(); 
						montoFac = lblMontoOdev.getValue().toString();
						if(!montoDev.equals(montoFac)){
							sMensajeError = sMensajeError
							+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El monto de la devolución debe ser igual al monto de la factura!<br>";
							bValido = false;
							y = y + 5;
							break;
						}
					}
				}
			}
			if (sTipoRecibo.equals("AUTOMATICO")) {
				
				double montoapl = new Divisas()
									.formatStringToDouble(
										txtMontoAplicarDev.getValue()
										.toString().trim());
				double montorec = new Divisas()
									.formatStringToDouble(
										txtMontoProcesado.getValue()
										.toString().trim());
				
				String monto    = Double.toString(new Divisas().roundDouble(montorec));
				String aplicado = Double.toString(new Divisas().roundDouble(montoapl));
				
				BigDecimal bdRecibido = new BigDecimal(monto);
				BigDecimal bdAplicar = new BigDecimal(aplicado);
				
				if (lstDetalleReciboFactDev == null || lstDetalleReciboFactDev.isEmpty()) {
					gvDetalleReciboFactDev.setStyleClass("igGridError");
					sMensajeError = sMensajeError
							+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Debe agregar montos a devolver!<br>";
					bValido = false;
					y = y + 5;
				} else
					if ( bdRecibido.compareTo(bdAplicar) != 0  ) {
					
//					if (!(txtMontoProcesado.getValue().toString().trim()).equals(txtMontoAplicarDev.getValue().toString().trim())) {
					sMensajeError = sMensajeError
							+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Debe cumplir con el total a devolver!<br>";
					bValido = false;
					y = y + 5;
				} else if (sConcepto.trim().equals("")) {
					txtConceptoDev.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError
							+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El concepto es requerido!<br>";
					bValido = false;
					y = y + 5;
				} else if (!matAlfa.matches()) {
					txtConceptoDev.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError
							+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El concepto contiene texto no valido!<br>";
					bValido = false;
					y = y + 5;
				}
			} else {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				Matcher matFecha = null;
				Date dFecha = null;
				String sFecha = null;

				if (txtFechaManualDev.getValue() != null) {
					dFecha = (Date) txtFechaManualDev.getValue();
					sFecha = sdf.format(dFecha);
					Pattern pFecha = Pattern.compile("^[0-3]?[0-9](/|-)[0-2]?[0-9](/|-)[1-2][0-9][0-9][0-9]$");
					matFecha = pFecha.matcher(sFecha);
				}

				String sNoManual = txtNumeroReciboManulDev.getValue().toString();
				Matcher matNumero = null;
				Pattern pNumero = Pattern.compile("^[0-9]+\\.?[0-9]*$");
				matNumero = pNumero.matcher(sNoManual);
				// valida la fecha del recibo
				if (txtFechaManualDev.getValue() == null|| (txtFechaManualDev.getValue().toString().trim()).equals("")) {
					sMensajeError = sMensajeError
							+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Fecha de recibo requerida<br>";
					txtFechaManualDev.setStyleClass("frmInput2Error2");
					bValido = false;
					y = y + 5;
				}
				if (matFecha == null || !matFecha.matches()) {
					sMensajeError = sMensajeError
							+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Fecha de recibo no es valida<br>";
					txtFechaManualDev.setStyleClass("frmInput2Error");
					bValido = false;
					y = y + 5;
				} else if (sNoManual.trim().equals("")) {
					txtNumeroReciboManulDev.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError
							+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> No. recibo manual es requerido!<br>";
					bValido = false;
					y = y + 5;
				} else if (!matNumero.matches()) {
					txtNumeroReciboManulDev.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError
							+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> No. recibo manual contiene texto no valido!<br>";
					bValido = false;
					y = y + 5;
				} else if (lstDetalleReciboFactDev == null
						|| lstDetalleReciboFactDev.isEmpty()) {
					gvDetalleReciboFactDev.setStyleClass("igGridError");
					sMensajeError = sMensajeError
							+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Debe agregar montos a devolver!<br>";
					bValido = false;
					y = y + 5;
				} else if (!(Double.parseDouble(txtMontoProcesado.getValue()
						.toString()) == Double.parseDouble(txtMontoAplicarDev
						.getValue().toString()))) {
					sMensajeError = sMensajeError
							+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Debe cumplir con el total a devolver!<br>";
					bValido = false;
					y = y + 5;
				} else if (sConcepto.trim().equals("")) {
					txtConceptoDev.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError
							+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El concepto es requerido!<br>";
					bValido = false;
					y = y + 5;
				} else if (!matAlfa.matches()) {
					txtConceptoDev.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError
							+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El concepto contiene texto no valido!<br>";
					bValido = false;
					y = y + 5;
				}
			}
			if (!bValido) {
				lblMensajeValidacion.setValue(sMensajeError);
				dwProcesa.setStyle("width:390px;height:" + y + "px");
				dwProcesa.setWindowState("normal");
			}
		} catch (Exception ex) {
			bValido = false;
			ex.printStackTrace();
		}
		return bValido;
	}

/************************************************************************************************************************************* */
	public void restablecerEstiloProcesarDev() {
		gvDetalleReciboFactDev.setStyleClass("igGrid");
		txtConceptoDev.setStyleClass("frmInput2");
		txtNumeroReciboManulDev.setStyleClass("frmInput2");
	}

/************************************************************************************************************************************* */
	public void quitarPagoDevolucion(ActionEvent e) {
		MetodosPago metpagos = null,selectedPago = null;
		Hfactura hFac = null;
		
		String sMonedaBase = "";
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		try {			
			RowItem ri = (RowItem) e.getComponent().getParent().getParent();
			selectedPago = (MetodosPago) gvDetalleReciboFactDev.getDataRow(ri);
			
			List lstFacturasSelected = (List) m.get("facturasSelected");

			hFac = (Hfactura) lstFacturasSelected.get(0);
			List lstDetalleReciboFactDev = (List) m.get("lstDetalleReciboFactDev");
			double dProcesado = Double.parseDouble(txtMontoProcesado.getValue().toString());
			double dFaltante = 0;
			double dAplicar = Double.parseDouble(txtMontoAplicarDev.getValue().toString());
			for (int i = 0; i < lstDetalleReciboFactDev.size(); i++) {
				metpagos = (MetodosPago) lstDetalleReciboFactDev.get(i);
				if (selectedPago.getMetodo().equals(metpagos.getMetodo())
						&& selectedPago.getMoneda().equals(metpagos.getMoneda())
						&& selectedPago.getMonto() == metpagos.getMonto()
						&& selectedPago.getTasa() == metpagos.getTasa()
						&& selectedPago.getEquivalente() == metpagos.getEquivalente()
						&& selectedPago.getReferencia().equals(metpagos.getReferencia())
						&& selectedPago.getReferencia2().equals(metpagos.getReferencia2())
						&&  selectedPago.getReferencia3().equals(metpagos.getReferencia3())
						&&  selectedPago.getReferencia4().equals(metpagos.getReferencia4())) {
					if (hFac.getMoneda().equals(sMonedaBase)) {
						if (metpagos.getMoneda().equals(sMonedaBase)) {
							dProcesado = dProcesado - metpagos.getMonto();
						} else {
							dProcesado = dProcesado - metpagos.getEquivalente();
						}
					} else {
						if (metpagos.getMoneda().equals(sMonedaBase)) {
							dProcesado = dProcesado - metpagos.getEquivalente();
						} else {
							dProcesado = dProcesado - metpagos.getMonto();
						}
					}
					lstDetalleReciboFactDev.remove(i);
				}
			}
			// resumen de pago
			if(lstDetalleReciboFactDev.size()==0){
				txtMontoProcesado.setValue(0);
				txtFaltante.setValue(dAplicar);
			}else{
				dFaltante = dAplicar - dProcesado;
				txtMontoProcesado.setValue(dProcesado);
				txtFaltante.setValue(dFaltante);
			}

			m.put("lstDetalleReciboFactDev", lstDetalleReciboFactDev);
			gvDetalleReciboFactDev.dataBind();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/** *********************************************************************************************************************************** */
	@SuppressWarnings("unchecked")
	public void agregarMontoDevolucion(ActionEvent ev) {
		boolean bValido = false;
		Tpararela[] tcPar = null;
		Tcambio[] tcJDE = null;
		Divisas divisas = new Divisas();
		BigDecimal tasa = BigDecimal.ONE;
		Double monto = 0.0, equiv = 0.0;
		MetodosPago metodoPago = null;
		Recibodet recibodet = null;
		String ref1 = "", ref2 = "", ref3 = "", ref4 = "", metDesc = "",sVmanual="",ref5 = "",ref6 = "",ref7="", nombre = "";
		Hfactura hFac = null;
		int iCaidpos = 0;
		
		String codigomarcatarjeta = "";
		String marcatarjeta = "";
		int liquidarpormarca = 0 ; 
				
		
		String sMonedaBase = "";
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		try {
			
			
			String sMontoIngresado = txtMontoFacDev.getValue().toString().trim();
			// recibo devolucion
			List<MetodosPago> lstDetalleReciboFactDev = (List<MetodosPago>) m.get("lstDetalleReciboFactDev");
			// recibo Original
			List lstDetalleReciboOriginal = (List) m.get("lstDetalleReciboOriginal");
			List<Hfactura> lstFacturasSelected = (List<Hfactura>) m.get("facturasSelected");
			hFac =  lstFacturasSelected.get(0);
			//obtener companias x caja
			sMonedaBase = cCtrl.sacarMonedaBase((F55ca014[])m.get("cont_f55ca014"), hFac.getCodcomp());
			
			String metodo = cmbMetodosPagoFacDev.getValue().toString();
			String moneda = cmbMonedaFacDev.getValue().toString();

			List lstMetPago = (List) m.get("metpago");
			boolean flgpagos = true;
			Metpago[] metpago = null;

			// poner descripcion a metodo
			for (int m = 0; m < lstMetPago.size(); m++) {
				metpago = (Metpago[]) lstMetPago.get(m);
				if (metodo.trim().equals(metpago[0].getId().getCodigo().trim())) {
					metDesc = metpago[0].getId().getMpago().trim();
					break;
				}
			}
			
		
			//&& ====== generar la tasa de cambio a partir de los valores de la factura.
			//&& ====== Solo facturacion repuestos dolares.
			for (int i = 0; i <lstFacturasSelected.size(); i++) {
				Hfactura 	hfac1 = lstFacturasSelected.get(i);
			
				if(PropertiesSystem.LNS_REPUESTOS.contains(hFac.getCodunineg().trim().substring(2, 4)) 
							&& hfac1.getMoneda().compareTo(sMonedaBase) != 0 ){
					
					hfac1.setTasa( hfac1.getTotaldomtmp().divide( 
						new BigDecimal(String.valueOf(hfac1.getTotal())), 4, RoundingMode.HALF_UP ));
				
					lstFacturasSelected.set(i, hfac1);
				}
			}
			hFac = (Hfactura) lstFacturasSelected.get(0);
			
			restablecerEstiloProcesarDev();
			bValido = validarMontoDevolucion(sMontoIngresado,lstDetalleReciboFactDev, metodo, moneda,lstDetalleReciboOriginal, hFac, sMonedaBase);

			if (bValido) {
				monto = Double.parseDouble(txtMontoFacDev.getValue().toString().trim());
				
				// poner las referencias al metodo corrrespondiente
				for (int a = 0; a < lstDetalleReciboOriginal.size(); a++) {
					recibodet = (Recibodet) lstDetalleReciboOriginal.get(a);
					
					if (metodo.equals( recibodet.getId().getMpago().trim() ) && moneda.equals(recibodet.getId().getMoneda().trim())) {
					
						ref1 = recibodet.getId().getRefer1().trim();
						ref2 = recibodet.getId().getRefer2().trim();
						ref3 = recibodet.getId().getRefer3().trim();
						ref4 = recibodet.getId().getRefer4().trim();
						ref5 = recibodet.getRefer5().trim();
						ref6 = recibodet.getRefer6().trim();
						ref7 = recibodet.getRefer7().trim(); 
						
						nombre = recibodet.getNombre().trim();
						sVmanual = recibodet.getVmanual();
						iCaidpos = recibodet.getCaidpos();
						
						codigomarcatarjeta = recibodet.getCodigomarcatarjeta();
					 	marcatarjeta =  recibodet.getMarcatarjeta();
					 	liquidarpormarca = recibodet.getLiquidarpormarca() ; 
						
						break;
					}
				}

				// calcular equivalente en dependencia de moneda de factura y
				// moneda de pago
				if (hFac.getMoneda().equals(sMonedaBase)) {//Factura domestica
					if (moneda.equals(sMonedaBase)) {//Pago COR monedabase COR
						tasa = BigDecimal.ONE;
						equiv = monto;
					} else {// pago USD monedabase COR
						tcJDE = (Tcambio[]) m.get("tcambio");
						// buscar tasa de cambio JDE para moneda
						for (int l = 0; l < tcJDE.length; l++) {
							if (tcJDE[l].getId().getCxcrdc().equals(moneda)) {
								tasa = tcJDE[l].getId().getCxcrrd();
								break;
							}
						}
						equiv = divisas.roundDouble4(monto * tasa.doubleValue());
					}
				} else {//factura en moneda foranea
					if (moneda.equals(sMonedaBase)) {
						tasa = hFac.getTasa();
						equiv = divisas.roundDouble4(monto / tasa.doubleValue());
					} else {
						tasa = BigDecimal.ONE;
						equiv = monto;
					}
				}

				if (lstDetalleReciboFactDev != null) {
					MetodosPago[] mpagos = new MetodosPago[lstDetalleReciboFactDev.size()];
					int cant = lstDetalleReciboFactDev.size();

					for (int i = 0; i < cant; i++) {
						mpagos[i] = ((MetodosPago) lstDetalleReciboFactDev.get(i));
					}
					// valida que se agreguen los metodos iguales a un solo registro
					for (int i = 0; i < mpagos.length; i++) {
						if (mpagos[i].getMetodo().trim().equals(metodo)&& mpagos[i].getMoneda().trim().equals(moneda)
								&& mpagos[i].getReferencia().trim().equals(ref1)
								&& mpagos[i].getReferencia2().trim().equals(ref2)
								&& mpagos[i].getReferencia3().trim().equals(ref3)
								&& mpagos[i].getReferencia4().trim().equals(ref4)) {

							monto = monto + mpagos[i].getMonto();
							equiv = equiv + mpagos[i].getEquivalente();

							mpagos[i].setMonto(monto);

							mpagos[i].setEquivalente(equiv);

							mpagos[i].setReferencia5(ref5);
							mpagos[i].setReferencia6(ref6);
							mpagos[i].setReferencia7(ref7);
							mpagos[i].setNombre(nombre);
							
							mpagos[i].setCodigomarcatarjeta(codigomarcatarjeta);
							mpagos[i].setMarcatarjeta(marcatarjeta);

							lstDetalleReciboFactDev.set(i,mpagos[i]);
							flgpagos = false;
						}
					}
				} else {
					MetodosPago metpagos = new MetodosPago(metDesc,metodo, moneda,monto, tasa, equiv, ref1, ref2, ref3, ref4,sVmanual,iCaidpos);
					lstDetalleReciboFactDev = new ArrayList<MetodosPago>();
					metpagos.setReferencia5(ref5);
					metpagos.setReferencia6(ref6);
					metpagos.setReferencia7(ref7);
					metpagos.setNombre(nombre);
					
					metpagos.setCodigomarcatarjeta(codigomarcatarjeta);
					metpagos.setMarcatarjeta(marcatarjeta);
					
					lstDetalleReciboFactDev.add(metpagos);
					flgpagos = false;
				}

				if (flgpagos) {
					MetodosPago metpagos = new MetodosPago(metDesc,metodo, moneda,monto, tasa, equiv, ref1, ref2, ref3, ref4,sVmanual,iCaidpos);
					
					metpagos.setReferencia5(ref5);
					metpagos.setReferencia6(ref6);
					metpagos.setReferencia7(ref7);
					metpagos.setNombre(nombre);
					metpagos.setCodigomarcatarjeta(codigomarcatarjeta);
					metpagos.setMarcatarjeta(marcatarjeta);
					
					lstDetalleReciboFactDev.add(metpagos);
				}

			}
			m.put("lstDetalleReciboFactDev", lstDetalleReciboFactDev);
			gvDetalleReciboFactDev.dataBind();
			double dProcesado = 0;
			double dFaltante = 0;
			double dAplicar = Double.parseDouble(txtMontoAplicarDev.getValue().toString());

			// calcular el procesado
			for (int z = 0; z < lstDetalleReciboFactDev.size(); z++) {
				metodoPago = (MetodosPago) lstDetalleReciboFactDev.get(z);
				if (hFac.getMoneda().equals(sMonedaBase)) {
					if (metodoPago.getMoneda().equals(sMonedaBase)) {
						dProcesado = dProcesado + metodoPago.getMonto();
					} else {
						dProcesado = dProcesado + metodoPago.getEquivalente();
					}
				} else {
					if (metodoPago.getMoneda().equals(sMonedaBase)) {
						dProcesado = dProcesado + metodoPago.getEquivalente();
					} else {
						dProcesado = dProcesado + metodoPago.getMonto();
					}
				}

			}
			dFaltante = new Divisas().roundDouble(dAplicar) - new Divisas().roundDouble(dProcesado);

			txtMontoProcesado.setValue(dProcesado);
			txtFaltante.setValue(dFaltante);
			txtMontoFacDev.setValue("");
			
		} catch (Exception ex) {
			ex.printStackTrace(); 
		}
	}

	/** *********************************************************************************************************************************** */
	public void restablecerEstiloDevolucion() {
		txtMontoFacDev.setStyleClass("frmInput2");
	}

	/** *********************************************************************************************************************************** */
	public boolean validarMontoDevolucion(String sMontoIngresado,
			List lstDetalleReciboFactDev, String sMetodo, String sMoneda,
			List lstDetalleReciboOriginal, Hfactura hfac,String sMonedaBase) {
		boolean bValido = true;
		String sMensajeError = "";
		double dMontoIngresado = 0.0, dMontoAplicar = 0.0, dMonto = 0.0, dMontoxMetodo = 0.0, dMontoAplicarxMetodo = 0.0;
		MetodosPago metPago = null;
		boolean bExiste = false;
		Recibodet recibodet = null;
		try {
			restablecerEstiloDevolucion();
			// expresion regular solo numeros
			Matcher matNumero = null;
			if (!sMontoIngresado.equals("")) {
				Pattern pNumero = Pattern.compile("^[0-9]+\\.?[0-9]*$");
				matNumero = pNumero.matcher(sMontoIngresado);
				dMontoIngresado = Double.parseDouble(sMontoIngresado);
				dMonto = Double.parseDouble(sMontoIngresado);
			}
			// verificar que el monto agregado y el que existe no exceda el
			// monto a devolver
			if (lstDetalleReciboFactDev != null && !lstDetalleReciboFactDev.isEmpty()) {
				for (int i = 0; i < lstDetalleReciboFactDev.size(); i++) {
					metPago = (MetodosPago) lstDetalleReciboFactDev.get(i);
					if (hfac.getMoneda().equals(sMonedaBase)) {// domestica
						if (sMetodo.equals(sMonedaBase)) {
							dMontoIngresado = dMontoIngresado + metPago.getMonto();
						} else {
							dMontoIngresado = dMontoIngresado + metPago.getEquivalente();
						}
					} else {// foranea
						if (sMetodo.equals(sMonedaBase)) {
							dMontoIngresado = dMontoIngresado + metPago.getEquivalente();
						} else {
							dMontoIngresado = dMontoIngresado + metPago.getMonto();
						}
					}
					// sumar monto para metodo actual
					if (metPago.getMetodo().trim().equals(sMetodo)
							&& metPago.getMoneda().trim().equals(sMoneda)) {
						dMontoxMetodo = dMontoxMetodo + metPago.getMonto();
					}
				}
			}
			//
			// sumar monto original por metodo
			for (int h = 0; h < lstDetalleReciboOriginal.size(); h++) {
				recibodet = (Recibodet) lstDetalleReciboOriginal.get(h);
				if (recibodet.getId().getMpago().trim().equals(sMetodo) && recibodet.getId().getMoneda().trim().equals(sMoneda)) {
					dMontoAplicarxMetodo = dMontoAplicarxMetodo + recibodet.getMonto().doubleValue();
				}
			}
			if (hfac.getMoneda().equals(sMonedaBase)) {// domestica
				dMontoAplicar = Double.parseDouble(txtMontoAplicarDev.getValue().toString());
			}else {
				dMontoAplicar = hfac.getTotaldomtmp().doubleValue();

			}

			if (sMontoIngresado.equals("")) {
				txtMontoFacDev.setStyleClass("frmInput2Error");
				sMensajeError = sMensajeError
						+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El monto es requerido!<br>";
				dwProcesa.setWindowState("normal");
				bValido = false;
			} else if (!matNumero.matches()) {
				txtMontoFacDev.setStyleClass("frmInput2Error");
				sMensajeError = sMensajeError
						+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El monto ingresado no es correcto!<br>";
				dwProcesa.setWindowState("normal");
				bValido = false;
			} else if (dMontoIngresado > dMontoAplicar) {
				sMensajeError = sMensajeError
						+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El monto ingresado debe ser igual al monto a aplicar!<br>";
				dwProcesa.setWindowState("normal");
				bValido = false;
			}
			else {
				// validar que no pague en un metodo de pago diferente a los de
				// la devolucion
				if (hfac.getMoneda().equals(sMonedaBase)) {
					for (int j = 0; j < lstDetalleReciboOriginal.size(); j++) {
						recibodet = (Recibodet) lstDetalleReciboOriginal.get(j);
						if (recibodet.getId().getMpago().trim().equals(sMetodo) /*&& recibodet.getId().getMoneda().trim().equals(sMoneda)*/) {
							bExiste = true;
							break;
						}
					}
				} else {
					for (int j = 0; j < lstDetalleReciboOriginal.size(); j++) {
						recibodet = (Recibodet) lstDetalleReciboOriginal.get(j);
						if (recibodet.getId().getMpago().equals(sMetodo)
								&& sMetodo.equals(MetodosPagoCtrl.TARJETA)) {
							if (recibodet.getId().getMoneda().equals(sMoneda)) {
								bExiste = true;
								break;
							}
						} else if (recibodet.getId().getMpago().trim().equals(
								sMetodo)) {
							bExiste = true;
							break;
						}
					}
				}

				
				if (!bExiste) {
					sMensajeError = sMensajeError
							+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> No puede usar esta forma de pago!<br>";
					dwProcesa.setWindowState("normal");
					bValido = false;
				}
			}
			lblMensajeValidacion.setValue(sMensajeError);
			dwProcesa.setStyle("width:350px;height:150px");
		} catch (Exception ex) {
			bValido = false;
			ex.printStackTrace();
		}
		return bValido;
	}

	/** ******************************************************************************************************************************** */
	public void limpiarDevolucion() {
		fechaReciboDev.setValue(new Date());
		txtNumeroReciboDev.setValue("");
		lblCodigoClienteDev.setValue("");
		lblNombreClienteDev.setValue("");
		lblNoFactDev.setValue("");
		lblTipoDocFactDev.setValue("");
		lblMonedaFactDev.setValue("");
		lblMontoFactDev.setValue("");
		lblNodoco.setValue("");
		lblTipodoco.setValue("");
		lblMonedaOdev.setValue("");
		lblMontoOdev.setValue("");
		lblFechadev.setValue("");
		lblNoReciboOdev.setValue("");
		lblDevMontoEquiv.setValue("");
		lblMontoEquiv.setValue("");
		
//		lblHoraReciboDev.setValue("");
		m.remove("lstDetalleReciboOriginal");
		m.remove("lstDetalleReciboFactDev");
	}

	/**
	 * **********ESTABLECER MONEDAS Y BUSCAR METODOS DE PAGO PARA LA
	 * MONEDA*********************************************************
	 */
	public void setMonedaDevolucion(ValueChangeEvent e) {
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		String sCodigo = null, sCajaId = null;
		String[] sMetodosPago = null;
		MetodosPagoCtrl metPagoCtrl = new MetodosPagoCtrl();
		Metpago[] metpago = null;
		Tpararela[] tpcambio = null;
		try {
			//
			List lstFacturasSelected = (List) m.get("facturasSelected");
			Hfactura hFac = (Hfactura) lstFacturasSelected.get(0);
			// verificar si tas de cambio paralela existe
			tpcambio = (Tpararela[]) m.get("tpcambio");

			List lstMetodosPago = new ArrayList();
			sCodigo = cmbMonedaFacDev.getValue().toString();
			sCajaId = (String) m.get("sCajaId");
			// obtener metodos de pago por moneda
			sMetodosPago = metPagoCtrl.obtenerMetodoPagoxCaja_Moneda(Integer
					.parseInt(sCajaId), hFac.getCodcomp(), sCodigo);
			metpago = new Metpago[sMetodosPago.length];
			for (int i = 0; i < sMetodosPago.length; i++) {
				metpago = metPagoCtrl.obtenerDescripcionMetodosPago(sMetodosPago[i]);
				lstMetodosPago.add(new SelectItem(metpago[0].getId().getCodigo(), metpago[0].getId().getMpago()));
			}
			m.put("lstMetodosPagoDev", lstMetodosPago);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/** ***************************************************************************************************** */
	/**
	 * ***********ESTABLECE LAS REFERENCIA REQUERIDAS PARA EL METODO DE PAGO
	 * SELECCIONADO**********************************
	 */
	public void EstablecerMetodosPagoDevolucion(ValueChangeEvent e) {
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		MonedaCtrl monCtrl = new MonedaCtrl();
		String[] sMoneda = null;
		try {
			String codcaja = m.get("sCajaId").toString();
			String codmetodo = cmbMetodosPagoFacDev.getValue().toString();
			List lstMoneda = new ArrayList();
			//
			List lstFacturasSelected = (List) m.get("facturasSelected");
			Hfactura hFac = (Hfactura) lstFacturasSelected.get(0);
			// obtener monedas para el metodo de pago selecccionado
			sMoneda = monCtrl.obtenerMonedasxMetodosPago_Caja(Integer
					.parseInt(codcaja), hFac.getCodcomp(), codmetodo);
			for (int i = 0; i < sMoneda.length; i++) {
				lstMoneda.add(new SelectItem(sMoneda[i], sMoneda[i]));
			}
			m.put("lstMonedaDev", lstMoneda);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
/*********************************************************************************************/
/**		Método: Procedimiento para generar una solicitud de emisión de cheque o carta a BAC.
 * 		Nombre: Carlos Manuel Hernández Morrison 	         
 * 		Fecha:  12/05/2010 
 */	public void solicitarEmisionChk(ActionEvent ev){
	 	SolechequeCtrl solCtrl = new SolechequeCtrl();
	 	ReciboCtrl recCtrl = new ReciboCtrl();
		Divisas dv = new Divisas();
		FechasUtil f = new FechasUtil();
		Numcaja numcaja = null;
		Hfactura hFac=null, hDev=null;
		List<Solecheque>lstEmision = new ArrayList<Solecheque>();
		List<Recibodet>lstRecibos = new ArrayList<Recibodet>();
		String sMensaje = "",sMonedaSolicitud="";
		Cambiodet[] cambio=null;
		Recibodet rd = new Recibodet();
		int  iCantCambio=0,iCantEfectivo=0;
		boolean bHecho = false;
		double dMontodev=0,dTasaCambioMpago=1,dMontoTotalEfectivo=0;
		
		String sMonedaBase = "";
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		
		Session sesion = null;
		Transaction trans = null;
		
		try {
			LogCajaService.CreateLog("solicitarEmisionChk", "INFO", "solicitarEmisionChk-INICIO");
			//---- Obtener conexiones y transacciones.
			sesion = HibernateUtilPruebaCn.currentSession();
			trans = sesion.beginTransaction();
			
			dwConfirmaEmisionCheque.setWindowState("hidden");
			//--- Datos de la caja.
			List lstCajas = (List) m.get("lstCajas");
			Vf55ca01 f5 = ((Vf55ca01) lstCajas.get(0));
			Vautoriz[] vaut = (Vautoriz[]) m.get("sevAut");
			
			//--- Recuperar factura y devolución y detalle de recibo a utilizar.
			hFac = (Hfactura)m.get("fcd_hFactura");
			hDev = (Hfactura)m.get("fcd_hDevolución");
			lstRecibos = (ArrayList<Recibodet>)m.get("fcd_lstDetalleReciboOriginal");
			rd = lstRecibos.get(0);
			
			//obtener companias x caja
			sMonedaBase = cCtrl.sacarMonedaBase((F55ca014[])m.get("cont_f55ca014"), hFac.getCodcomp());
			//--- Devolución parcial si el monto de la devolución != al de la factura.
			if(hDev.getTotal() != hFac.getTotal()){
				List<Recibodet>lstEfectivo = new ArrayList<Recibodet>();
				List<Recibodet>lstTCredito = new ArrayList<Recibodet>();
				List<Recibodet>lstOtros    = new ArrayList<Recibodet>();
				boolean b5 = false,bH=false,bOtros=false;
				double dMontoDevCOR=0;
				double dMontoSolicitud = 0;
				Solecheque sc = new Solecheque();
				String sMonedaDevolucion="COR";
				
				dMontodev = hDev.getTotal();
				sMonedaDevolucion = hDev.getMoneda();
				
				double dMontoPagoCor=0,dMontoPagoUsd=0;
				//--- Separar los métodos de pago y contar la cantidad pagos y monto total de efectivo.
				for (Recibodet recibodet : lstRecibos){
					if(recibodet.getId().getMpago().equals( MetodosPagoCtrl.EFECTIVO)){
						lstEfectivo.add(recibodet);
						b5 = true;
						iCantEfectivo++;
						
						if(recibodet.getId().getMoneda().equals(sMonedaBase))
							dMontoPagoCor = recibodet.getMonto().doubleValue();
						else
							dMontoPagoUsd = recibodet.getMonto().doubleValue();

						//--- Tasa de cambio para convertir la factura a córdobas.
						if(recibodet.getTasa().doubleValue()>1)
							dTasaCambioMpago=recibodet.getTasa().doubleValue();
					}else
					if(recibodet.getId().getMpago().equals(MetodosPagoCtrl.TARJETA)){
						lstTCredito.add(recibodet);
						bH = true;
					}else{
						lstOtros.add(recibodet);
						bOtros = true;
					}
				}
				dMontoTotalEfectivo = dMontoPagoCor + (dMontoPagoUsd*dTasaCambioMpago);
				dMontoTotalEfectivo = dv.roundDouble4(dMontoTotalEfectivo);
				
				//--- Generar documentos (solicitudes) en orden: Efectivo,Tarjeta,Otros.
				if(b5){
					boolean bPasoEfectivo = false;
					for (Recibodet recibo5 : lstEfectivo) {
						boolean bEmisionValida=false;
						sc = new Solecheque(); 
						
						if(dMontodev>0){
							//--- Dos monedas para efectivo, devolver en córdobas.
							if(iCantEfectivo == 2){
								sMonedaSolicitud = "COR";
								if(!bPasoEfectivo){
									bPasoEfectivo = true;
									bEmisionValida = true;
									sMonedaSolicitud = "COR";
									
									//--- Monto de la devolución en córdobas.
									dMontoDevCOR = (hDev.getMoneda().equals(sMonedaBase))?
														hDev.getTotal():
														hDev.getTotal()*dTasaCambioMpago;
									if(dMontoTotalEfectivo >= dMontoDevCOR){
										dMontoSolicitud = dMontoDevCOR;
										dMontodev = 0;
									}else{
										dMontoSolicitud = dMontoTotalEfectivo;
										dMontodev -= (sMonedaDevolucion.equals("USD"))? 
														dMontoTotalEfectivo/dTasaCambioMpago:
														dMontoTotalEfectivo;
									}
								}
							}
							//--- Una sola móneda para efectivo, devolver en la misma moneda.
							else{
								bEmisionValida = true;
								sMonedaSolicitud = recibo5.getId().getMoneda();
								dTasaCambioMpago = recibo5.getTasa().doubleValue();
								
								if(sMonedaSolicitud.equals(sMonedaDevolucion)){
									if(recibo5.getMonto().doubleValue()>dMontodev){
										dMontoSolicitud = dMontodev;
										dMontodev = 0;
									}else{
										dMontoSolicitud = recibo5.getMonto().doubleValue();
										dMontodev -= recibo5.getMonto().doubleValue();
									}
								}else{
									if(recibo5.getEquiv().doubleValue()>=dMontodev){
										dMontoSolicitud = (recibo5.getEquiv().doubleValue() >= recibo5.getMonto().doubleValue())?
															 dMontodev/dTasaCambioMpago:
															 dMontodev*dTasaCambioMpago;
										 dMontodev = 0;
									}else{
										dMontoSolicitud = recibo5.getMonto().doubleValue();
										dMontodev -= recibo5.getEquiv().doubleValue();
									}
								}
							}
							if(bEmisionValida){
								dMontoSolicitud = dv.roundDouble4(dMontoSolicitud);
								sc.setMonto(new BigDecimal(Double.toString(dMontoSolicitud)));
								sc.setMoneda(sMonedaSolicitud);
								sc.setMpago(recibo5.getId().getMpago());
								sc.setTipoemision("CHK");
								sc.setIdafiliado("");
								sc.setTasacambio(new BigDecimal(Double.toString(dTasaCambioMpago)));
								sc.setFechapago(hFac.getFecha());
								sc.setCodcli(hDev.getCodcli());
								sc.setFechafac(hDev.getFechajulian());
								lstEmision.add(sc);
							}
						}
					}
				}
				
				//---- Solicitudes para tarjetas de crédito (Cartas)
				if(bH){
					for (Recibodet rdH : lstTCredito) {
						Solecheque solchk = new Solecheque();
						
						if(dMontodev>0){
							dTasaCambioMpago = rdH.getTasa().doubleValue();
							sMonedaSolicitud = rdH.getId().getMoneda();
							
							if(rdH.getId().getMoneda().equals(sMonedaDevolucion)){
								if(rdH.getMonto().doubleValue()>dMontodev){
									dMontoSolicitud = dMontodev;
									dMontodev = 0;
								}else{
									dMontoSolicitud = rdH.getMonto().doubleValue();
									dMontodev -=  rdH.getMonto().doubleValue();
								}
							}else{
								if(rdH.getEquiv().doubleValue()>dMontodev){
									dMontoSolicitud = (rdH.getEquiv().doubleValue()>rdH.getMonto().doubleValue())?
														dMontodev/rdH.getTasa().doubleValue():
														dMontodev*rdH.getTasa().doubleValue();
									
									dMontodev=0;
								}else{
									dMontoSolicitud = rdH.getMonto().doubleValue();
									dMontodev -= rdH.getEquiv().doubleValue();
								}
							}
							dMontoSolicitud = dv.roundDouble4(dMontoSolicitud);
							solchk.setMonto(new BigDecimal(Double.toString(dMontoSolicitud)));
							solchk.setMoneda(sMonedaSolicitud);
							solchk.setMpago(rdH.getId().getMpago());
							solchk.setTipoemision("CRT");
							solchk.setIdafiliado(rdH.getId().getRefer1().trim());
							solchk.setTasacambio(new BigDecimal(Double.toString(dTasaCambioMpago)));
							solchk.setFechapago(hFac.getFecha());
							solchk.setCodcli(hDev.getCodcli());
							solchk.setFechafac(hDev.getFechajulian()); 
							
							lstEmision.add(solchk);
						}
					}
				}
				//---- Solicitudes para pagos con cheques, depósitos en bancos y transferencias (Cheques)
				if(bOtros){
					for (Recibodet rdOtros : lstOtros) {
						Solecheque solchk = new Solecheque();
						
						if(dMontodev>0){
							dTasaCambioMpago = rdOtros.getTasa().doubleValue();
							sMonedaSolicitud = rdOtros.getId().getMoneda();
							
							if(rdOtros.getId().getMoneda().equals(sMonedaDevolucion)){
								if(rdOtros.getMonto().doubleValue()>dMontodev){
									dMontoSolicitud = dMontodev;
									dMontodev = 0;
								}else{
									dMontoSolicitud = rdOtros.getMonto().doubleValue();
									dMontodev -=  rdOtros.getMonto().doubleValue();
								}
							}else{
								
								if(rdOtros.getEquiv().doubleValue()>dMontodev){
									dMontoSolicitud = (rdOtros.getEquiv().compareTo(rdOtros.getMonto())== 1)?
															dMontodev/rdOtros.getTasa().doubleValue():
															dMontodev*rdOtros.getTasa().doubleValue();
									dMontodev=0;
								}else{
									dMontoSolicitud = rdOtros.getMonto().doubleValue();
									dMontodev -= rdOtros.getEquiv().doubleValue();
								}
							}
							dMontoSolicitud = dv.roundDouble4(dMontoSolicitud);
							solchk.setMonto(new BigDecimal(Double.toString(dMontoSolicitud)));
							solchk.setMoneda(sMonedaSolicitud);
							solchk.setMpago(rdOtros.getId().getMpago());
							solchk.setTipoemision("CHK");
							solchk.setIdafiliado(rdOtros.getId().getRefer1().trim());
							solchk.setTasacambio(new BigDecimal(Double.toString(dTasaCambioMpago)));
							solchk.setFechapago(hFac.getFecha());
							solchk.setCodcli(hDev.getCodcli());
							solchk.setFechafac(hDev.getFechajulian());
							lstEmision.add(solchk);
						}
					}
				}
			}
			//---- Devolución total de la factura.
			else{
				//--- Leer el cambio del recibo.
				cambio = recCtrl.leerDetalleCambio(rd.getId().getCaid(), rd.getId().getCodsuc(),  
												   rd.getId().getCodcomp(), rd.getId().getNumrec());
				//--- Contar cantidad de registros de cambio y de pagos en Efectivo.
				iCantCambio = cambio.length;
				for ( Recibodet detalle : lstRecibos) {
					if(detalle.getId().getMpago().equals( MetodosPagoCtrl.EFECTIVO))
						iCantEfectivo++;
				}
				//--- Determinar el monto de aplicado y generar las solicitudes.
				for (Recibodet recibodet : lstRecibos) {
					Solecheque sc = new Solecheque();
					double dMontoSolicitud = 0;
					String sTipo = "";
					
					if(recibodet.getId().getMpago().equals( MetodosPagoCtrl.EFECTIVO)){
						sTipo = "CHK";
						dMontoSolicitud = recibodet.getMonto().doubleValue();
						
						//--- Determinar el monto aplicado a la emisión.
						if(iCantEfectivo==2){
							for (Cambiodet cmbDet : cambio) {
								if(recibodet.getId().getMoneda().equals(cmbDet.getId().getMoneda())){
									dMontoSolicitud -=  cmbDet.getCambio().doubleValue();
								}
							}
						}else{
							if(iCantCambio==2){
								for (Cambiodet cmbDet : cambio) {
									dMontoSolicitud -= (recibodet.getId().getMoneda().equals(cmbDet.getId().getMoneda()))?
														cmbDet.getCambio().doubleValue():
														dv.roundDouble4(cmbDet.getCambio().doubleValue() / cmbDet.getTasa().doubleValue());
								}
							}else{
								dMontoSolicitud -= (recibodet.getId().getMoneda().equals(cambio[0].getId().getMoneda()))?
													cambio[0].getCambio().doubleValue():
													cambio[0].getCambio().divide(cambio[0].getTasa()).doubleValue();
							}
						}
						sc.setMonto(new BigDecimal(String.valueOf(dMontoSolicitud)));
						sc.setMoneda(recibodet.getId().getMoneda());
						sc.setMpago(recibodet.getId().getMpago());
						sc.setTipoemision(sTipo);
						sc.setIdafiliado("");
						sc.setTasacambio(new BigDecimal("1"));
						sc.setFechapago(hFac.getFecha());
						sc.setCodcli(hDev.getCodcli());
						sc.setFechafac(hDev.getFechajulian());
						lstEmision.add(sc);
						
					}else{
						sTipo = (recibodet.getId().getMpago().equals(MetodosPagoCtrl.TARJETA))?"CRT":"CHK";
						sc.setMonto(recibodet.getMonto());
						sc.setMoneda(recibodet.getId().getMoneda());
						sc.setMpago(recibodet.getId().getMpago());
						sc.setTipoemision(sTipo);
						sc.setIdafiliado(recibodet.getId().getRefer1());
						sc.setTasacambio(recibodet.getTasa());
						sc.setFechapago(hFac.getFecha());
						sc.setCodcli(hDev.getCodcli());
						sc.setFechafac(hDev.getFechajulian());
						lstEmision.add(sc);
					}
				}
			}
			
			//--- Completar los datos de la solicitudes.
			for (Solecheque sc : lstEmision) {
				SolechequeId scId = new SolechequeId();
				
				numcaja = dv.obtenerNumeracionCaja("SOLECHEQUE",rd.getId().getCaid(),rd.getId().getCodcomp(), rd.getId().getCodsuc(),true, vaut[0].getId().getLogin(),sesion);
				if(numcaja!=null){
					scId.setNosol(numcaja.getNosiguiente());
					sc.setId(scId);
					
					bHecho = solCtrl.grabarSolecheque(sesion, rd.getId().getCaid(),  rd.getId().getCodcomp(),
							 hDev.getCodsuc(), hDev.getCodunineg(), numcaja.getNosiguiente(), vaut[0].getId().getCodreg(), "P", new Date(),
							 hDev.getNofactura(), hDev.getTipofactura(), "", vaut[0].getId().getCodreg(), 
							 sc.getMpago(), sc.getTipoemision(), sc.getMoneda(), sc.getMonto(),  vaut[0].getId().getLogin(),
							 sc.getIdafiliado(),sc.getFechapago(),sc.getTasacambio(), sc.getCodcli(), sc.getFechafac());
					
					if(!bHecho){
						sMensaje = "No se ha podido registrar la solicitud para emisión de cheque error al insertar el registro en la Base de datos";
						break;
					}
				}else{
					sMensaje = "No se ha podido obtener el registro de numeración de caja para solicitud de emisión de cheques.";
					bHecho = false;
					break;
				}
			}
			//--- Generar los registros de enlace de Recibofac y recibo.
			if(bHecho){
				int iNumrec = 0;
				iNumrec = recCtrl.obtenerUltimoRecibo(null, null, f5.getId().getCaid(), hDev.getCodcomp());
				if(iNumrec>0){
					iNumrec+=1;
					recCtrl.actualizarNoReciboV2(sesion,iNumrec, f5.getId().getCaid(), hDev.getCodcomp());
					bHecho = recCtrl.fillEnlaceReciboFac(sesion, trans, iNumrec, hDev.getCodcomp(), 
														 hDev.getNofactura(), hDev.getTotal(),
														 hDev.getTipofactura(), f5.getId().getCaid(), 
														 hDev.getCodsuc(), "", hDev.getCodunineg(),"DCO",
														 hDev.getCodcli(), hDev.getFechajulian() );
					if(!bHecho){
						sMensaje = "No se ha podido realizar la operación de procesar la devolución: RECIBOFAC";
					}
				}else{
					bHecho = false;
					sMensaje = "No se ha podido obtener la numeración de recibos para la caja";
				}
			}
			
			//---- Verificar si es un traslado y de ser asi actualizar su estado.
			if(bHecho){
				//----- verificar traslado de factura.
				TrasladoCtrl tcCtrl = new TrasladoCtrl();
				Trasladofac tf = null;
				tf = tcCtrl.buscarTrasladofac(sesion, hDev, f5.getId().getCaid(), 0, "");
				if(tf!=null){
					bHecho =  tcCtrl.actualizarEstadoTraslado(sesion, tf, "P");
					if(!bHecho){
						lblMensajeValidacion.setValue(tcCtrl.getErrorTf().toString().split("@")[1]);
					}
				}
			}
			
			//--- Enviar los correos de notificación por cada documento generado.
			String sNoSols = "";
			if(bHecho){
				//-------------------------------------------  enviar correo (Agregado: 11/05/2010)
				EmpleadoCtrl ec = new EmpleadoCtrl();
				String sFechaCorreo, sHoraCorreo,sEncabezado,sPieCorreo,sSubject,sTo,sFrom,sCc,sNombreFrom="";
				sFechaCorreo = f.formatDatetoString(new Date(), "dd/MM/yyyy");
				sHoraCorreo  = f.formatDatetoString(new Date(),  "hh:mm:ss a");
				Pattern pCorreo = Pattern.compile( "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$" );
				
				Vf0101 vf01 = ec.buscarEmpleadoxCodigo2(vaut[0].getId().getCodreg());
				if(vf01!=null){
					sFrom = vf01.getId().getWwrem1().trim();
					sNombreFrom = vf01.getId().getAbalph().trim();
					if(!pCorreo.matcher(sFrom).matches())
						sFrom = PropertiesSystem.WEBMASTER_EMAIL_ADRESS;
				}else{
					sFrom =   PropertiesSystem.WEBMASTER_EMAIL_ADRESS;
					sNombreFrom = f5.getId().getCacatinom().trim();
				}
				sTo =   f5.getId().getCacontmail().trim();
				sCc   = f5.getId().getCacontmail().trim();
				
				for (Solecheque sc : lstEmision) {
					sNoSols += sc.getId().getNosol() + ". ";
					
					if(sc.getTipoemision().equals("CHK")){//Emisión de cheques
						sEncabezado = "Solicitud de Emisión de Cheque";
						sPieCorreo  = "Esta Emisión de cheque ha sido solicitada";
						sSubject 	= "Notificación de Solicitud de Emisión de cheque por devolución de contado";
					}else{
						sEncabezado = "Solicitud de Emisión de Carta a Credomatic";
						sPieCorreo  = "Esta Emisión de Carta ha sido solicitada";
						sSubject 	= "Notificación de Solicitud de  Emisión de Carta a Credomatic por devolución de contado";
					}
					if(pCorreo.matcher(sTo).matches()){
						String sUrl = new Divisas().obtenerURL();
						if(sUrl==null || sUrl.trim().equals("")){
							Aplicacion ap = new Divisas().obtenerAplicacion(vaut[0].getId().getCodapp());
							sUrl = (ap==null)?"http://ap.casapellas.com.ni:7080/"+PropertiesSystem.CONTEXT_NAME+"":ap.getUrl().trim();
						}
						solCtrl.enviarCorreo(sEncabezado, sPieCorreo, sTo, sFrom,sNombreFrom, sCc, sSubject,sc.getId().getNosol(), f5.getId().getCaid() + " " + f5.getId().getCaname(), 
							 		f5.getId().getCaco()+" " +  f5.getId().getCaconom(), hDev.getCodcomp().trim()+" "+ hDev.getNomcomp().trim(), hDev.getCodunineg()+" "+hDev.getUnineg(),
							 		hDev.getCodcli() + " " +hDev.getNomcli(), "Devolución: "+hDev.getNofactura()+" / Factura: "+ hDev.getNodoco(), 
									sc.getMonto().doubleValue() +" " +sc.getMoneda(), sFechaCorreo+" "+sHoraCorreo,sUrl);
					}
				}
			}
			//-------------------------------------------
			//---- guardar los registros definitivamente.
			if(bHecho){
				trans.commit();
				sMensaje += "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" />";
				sMensaje += "Se realizó correctamente la solicitud de emisión de cheque para:<br/>";
				sMensaje += "Factura: "+ hDev.getNodoco() + " Tipo: " + hDev.getTipodoco();
				sMensaje += "<br>Solicitudes: "+sNoSols;
				
				//------ Actualizar la lista de facturas.
				listarFacturasContadoDelDia();
				String sMensaje2 = "";
				List lstHfacturasContado = null;
				if (m.get("lstHfacturasContado") != null) {
					lstHfacturasContado = (List)m.get("lstHfacturasContado");
				}
				if (lstHfacturasContado == null	|| lstHfacturasContado.isEmpty()) {
					sMensaje2 = "No se encontraron resultados";
					getSMensaje();
					m.put("mostrar", "m");
				} else {
					if (m.get("mostrar") != null) {
						m.remove("mostrar");
					}
				}
				txtMensaje.setValue(sMensaje2);
				m.put("sMensaje", sMensaje2);
				cmbFiltroMonedas.dataBind();
				cmbFiltroFacturas.dataBind();
				gvHfacturasContado.dataBind();
			
				m.remove("lstSolicitud");
				m.remove("facturasSelected");
				m.remove("lstPagos");			
				
				m.remove("fcd_hFactura");
				m.remove("fcd_hDevolución");
				
				SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
				srm.addSmartRefreshId(gvHfacturasContado.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(dwValidacionFactura.getClientId(FacesContext.getCurrentInstance()));
			}else{
				trans.rollback();
				sMensaje += "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" />";
				sMensaje += "No se logro realizar la solicitud de emisión de cheque para:<br/>";
				sMensaje += "Factura: "+ hDev.getNodoco() + " Tipo: " + hDev.getTipodoco();
			}
			dwValidacionFactura.setWindowState("normal");
			dwValidacionFactura.setStyle("height: 150px; visibility: visible; width: 370px");
			lblValidaFactura.setValue(sMensaje);
			
		} catch (Exception error) {
			LogCajaService.CreateLog("solicitarEmisionChk", "ERR", error.getMessage());
			dwValidacionFactura.setWindowState("normal");
			dwValidacionFactura.setStyle("height: 150px; visibility: visible; width: 370px");
			lblValidaFactura.setValue("No se puede realizar operación \nError de Sistema==>  "+error.getMessage());
			error.printStackTrace();
		} finally {
			LogCajaService.CreateLog("solicitarEmisionChk", "INFO", "solicitarEmisionChk-FIN");
			HibernateUtilPruebaCn.closeSession(sesion);
		}
 	}
/*********************************************************************************************/
/**		Método: Procedimiento relizado para generar una solicitud de emisión de cheques.
 * 		Nombre: Carlos Manuel Hernández Morrison 	         
 * 		Fecha: 11/05/2010 
 * 		Observación: Método no en uso.
 */	public void solicitarEmisionChk1(ActionEvent ev){
		SolechequeCtrl solCtrl = new SolechequeCtrl();
		Divisas dv = new Divisas();
		FechasUtil f = new FechasUtil();
		Numcaja numcaja = null;
		int iNoSolicitud=0;
		Hfactura hFac=null, hDev=null;
		boolean bHecho = false;
		String sMensaje = "";
		Vautoriz[] vAut;
		
		//---- Obtener conexiones y transacciones.
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = session.beginTransaction();
		
		try {
			dwConfirmaEmisionCheque.setWindowState("hidden");
			
			//--- Datos de la caja.
			List lstCajas = (List) m.get("lstCajas");
			Vf55ca01 f5 = ((Vf55ca01) lstCajas.get(0));
			Vautoriz[] vaut = (Vautoriz[]) m.get("sevAut");
			
			//--- Recuperar factura y devolución a utilizar.
			hFac = (Hfactura)m.get("fcd_hFactura");
			hDev = (Hfactura)m.get("fcd_hDevolución");
			
			numcaja = dv.obtenerNumeracionCaja("SOLECHEQUE", f5.getId().getCaid(),hFac.getCodcomp(), 
												f5.getId().getCaco(), true, vaut[0].getId().getLogin(),session);
			if(numcaja!=null){
				//--- guardar solicitud de emision de cheques
				iNoSolicitud = numcaja.getNosiguiente();
				
				bHecho = solCtrl.grabarSolecheque(session, f5.getId().getCaid(), hDev.getCodcomp(), 
						
						//"000"+hDev.getCodsuc(), 
						CodeUtil.pad( hDev.getCodsuc().trim() , 5, "0") ,
						
						hDev.getCodunineg(), iNoSolicitud, vaut[0].getId().getCodreg(),
						"P", new Date(),  hDev.getNofactura(), hDev.getTipofactura(), " ", 
						vaut[0].getId().getCodreg(), hDev.getCodcli(), hDev.getFechajulian());
				
				if(bHecho){
					//-------------------------------------------  enviar correo (Agregado: 11/05/2010)
					SolechequeCtrl sc = new SolechequeCtrl();
					EmpleadoCtrl ec = new EmpleadoCtrl();
					vAut = (Vautoriz[])m.get("sevAut");
					String sFechaCorreo, sHoraCorreo,sEncabezado,sPieCorreo,sSubject,sTo,sFrom,sCc,sNombreFrom="";
					sFechaCorreo = f.formatDatetoString(new Date(), "dd/MM/yyyy");
					sHoraCorreo  = f.formatDatetoString(new Date(),  "hh:mm:ss a");
					sEncabezado = "Solicitud de Emisión de Cheque";
					sPieCorreo  = "Esta Emisión de cheque ha sido solicitada";
					sSubject 	= "Notificación de Solicitud de Emisión de cheque por devolución de contado";
					Pattern pCorreo = Pattern.compile( "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$" );
					
					Vf0101 vf01 = ec.buscarEmpleadoxCodigo2(vAut[0].getId().getCodreg());
					if(vf01!=null){
						sFrom = vf01.getId().getWwrem1().trim();
						sNombreFrom = vf01.getId().getAbalph().trim();
						if(!pCorreo.matcher(sFrom).matches())
							sFrom = "webmaster@casapellas.com.ni";
					}else{
						sFrom =  "webmaster@casapellas.com.ni";
						sNombreFrom = f5.getId().getCacatinom().trim();
					}
					sTo =   f5.getId().getCacontmail().trim();
					sCc   = f5.getId().getCacontmail().trim();
					
					if(pCorreo.matcher(sTo).matches()){
						String sUrl = new Divisas().obtenerURL();
						if(sUrl==null || sUrl.trim().equals("")){
							Aplicacion ap = new Divisas().obtenerAplicacion(vaut[0].getId().getCodapp());
							sUrl = (ap==null)?"http://ap.casapellas.com.ni:7080/"+PropertiesSystem.CONTEXT_NAME+"":ap.getUrl().trim();
						}
						sc.enviarCorreo(sEncabezado, sPieCorreo, sTo, sFrom,sNombreFrom, sCc, sSubject, iNoSolicitud, f5.getId().getCaid() + " " + f5.getId().getCaname(), 
								 		f5.getId().getCaco()+" " +  f5.getId().getCaconom(), hDev.getCodcomp().trim()+" "+ hDev.getNomcomp().trim(), hDev.getCodunineg()+" "+hDev.getUnineg(),
								 		hDev.getCodcli() + " " +hDev.getNomcli(), "Devolución: "+hDev.getNofactura()+" / Factura: "+ hDev.getNodoco(), 
										dv.formatDouble(hDev.getTotal()) +" " +hDev.getMoneda(), sFechaCorreo+" "+sHoraCorreo,sUrl);
					}
					//-------------------------------------------
					
					//--- Generar los registros de enlace de Recibofac y recibo.
					ReciboCtrl recCtrl = new ReciboCtrl();
					int iNumrec = 0;
					
					iNumrec = recCtrl.obtenerUltimoRecibo(null, null, f5.getId().getCaid(), hDev.getCodcomp());
					if(iNumrec>0){
						iNumrec+=1;
						recCtrl.actualizarNoReciboV2(session,iNumrec, f5.getId().getCaid(), hDev.getCodcomp());
						bHecho = recCtrl.fillEnlaceReciboFac(session, tx,
								iNumrec, hDev.getCodcomp(),
								hDev.getNofactura(), hDev.getTotal(), 
								hDev.getTipofactura(),f5.getId().getCaid(),
								f5.getId().getCaco(), "", hDev.getCodunineg(), 
								valoresJdeIns[0], hDev.getCodcli(), hDev.getFechadev());
						
						if(!bHecho){
							sMensaje = "No se ha podido realizar la operación de procesar la devolución: RECIBOFAC";
						}
					}else{
						bHecho = false;
						sMensaje = "No se ha podido obtener la numeración de recibos para la caja";
					}
				}else{
					sMensaje = "No se ha podido registrar correctamente la solicitud de emisión de cheque.";
				}
			}else{
				bHecho = false;
				sMensaje = "No se encuentra configurada la numeración de caja para solicitudes de emisión de cheque.";
			}
			
			//----- Verificar que se hayan completado las operaciones.
			if(bHecho){
				tx.commit();
				sMensaje += "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" />";
				sMensaje += "Se realizó correctamente la solicitud de emisión de cheque para:<br/>";
				sMensaje += "Factura: "+ hDev.getNodoco() + " Tipo: " + hDev.getTipodoco();
				sMensaje += " <br/> " + "No. de solicitud: " +iNoSolicitud;

				//------ Actualizar la lista de facturas.
				listarFacturasContadoDelDia();
				String sMensaje2 = "";
				List lstHfacturasContado = null;
				if (m.get("lstHfacturasContado") != null) {
					lstHfacturasContado = (List)m.get("lstHfacturasContado");
				}
				if (lstHfacturasContado == null	|| lstHfacturasContado.isEmpty()) {
					sMensaje2 = "No se encontraron resultados";
					getSMensaje();
					m.put("mostrar", "m");
				} else {
					if (m.get("mostrar") != null) {
						m.remove("mostrar");
					}
				}
				txtMensaje.setValue(sMensaje2);
				m.put("sMensaje", sMensaje2);
				cmbFiltroMonedas.dataBind();
				cmbFiltroFacturas.dataBind();
				gvHfacturasContado.dataBind();
			
				m.remove("lstSolicitud");
				m.remove("facturasSelected");
				m.remove("lstPagos");
				SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
				srm.addSmartRefreshId(gvHfacturasContado.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(dwValidacionFactura.getClientId(FacesContext.getCurrentInstance()));
			}else{
				tx.rollback();
			}
			dwValidacionFactura.setWindowState("normal");
			dwValidacionFactura.setStyle("height: 150px; visibility: visible; width: 370px");
			lblValidaFactura.setValue(sMensaje);
			session.close();
			m.remove("fcd_hFactura");
			m.remove("fcd_hDevolución");
		 
		} catch (Exception error) {
			tx.rollback();
			session.close();
			error.printStackTrace();
		}	 
	}
 /*********************************************************************************************/
 /**	Método: Validaciones para permitir procesar devolución o generar emisión de cheque
  * 	Nombre: Carlos Manuel Hernández Morrison 	         
  * 	Fecha:  02/07/2010 
 * @throws Exception 
  */public boolean validarEmisionCheque(Hfactura hFac,Hfactura hDev, List<Recibodet> lstDetalleRecibo,Vf55ca01 f5, 
		  ConfiguracionMensaje[] lstConfiguracionMensaje) throws Exception{
		boolean bValido = true, bMismodia = true;
	 
		Date fechaDev, fechafac;
		int iDiasTranscurridos=0, iDiasPermitidos = 0;
		List lstDatosDev = null;
		String sMensaje = "";		
		boolean bEfect=false, bTarjeta=false, bOtros=false;
		double dMontoPermitido=0,dMonto = 0.0;
		BigDecimal dTotalPagosDev  =BigDecimal.ZERO;
		BigDecimal dTotalPagoNoDev =BigDecimal.ZERO;
		
		try {
			m.remove("pfc_sMsgValidaDevolucion");
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			fechaDev = df.parse(hDev.getFecha());
			fechafac = df.parse(hFac.getFecha());
			
			//---- Verificar los tipo de pago utilizados.
			for (Recibodet rd : lstDetalleRecibo) {
				if(rd.getId().getMpago().equals( MetodosPagoCtrl.EFECTIVO)){
					bEfect = true;
					dTotalPagosDev =  dTotalPagosDev.add(rd.getMonto());
				}
				else
				if(rd.getId().getMpago().equals(MetodosPagoCtrl.TARJETA)){
					bTarjeta = true;
					dTotalPagosDev =  dTotalPagosDev.add(rd.getMonto());
				}
				else{
					bOtros = true;
					if(hDev.getMoneda().equals("COR")){
						if(rd.getId().getMoneda().equals((hDev.getMoneda()))){
							dMonto = rd.getMonto().doubleValue();
							dTotalPagoNoDev =  dTotalPagoNoDev.add(rd.getMonto());
						}else{
							dMonto = rd.getEquiv().doubleValue();
							dTotalPagoNoDev =  dTotalPagoNoDev.add(rd.getMonto());
						}
					}else{
						if(rd.getId().getMoneda().equals((hDev.getMoneda()))){
							dMonto = rd.getMonto().doubleValue();
							dTotalPagoNoDev =  dTotalPagoNoDev.add(rd.getMonto());
						}else{
							dMonto = rd.getEquiv().doubleValue();
							dTotalPagoNoDev =  dTotalPagoNoDev.add(rd.getMonto());
						}
					}
				}
			}
			
			//---- la factura y devolución son del mismo día?
			if(fechaDev.compareTo(fechafac) != 0){
				bMismodia = false;
				
				//---- Intervalo de días y configuración de caja para devoluciones.
				iDiasTranscurridos = FechasUtil.obtieneCantDiasHabiles(fechafac, fechaDev);
				lstDatosDev = CtrlCajas.obtenerF55ca028(f5.getId().getCaid(),hFac.getCodcomp(),hFac.getMoneda());				
				
				//----- Caso Solo Efectivo: Validaciones para determinar si procesar devolución.
				if(bEfect && !bTarjeta && !bOtros){
					if(lstDatosDev!=null && lstDatosDev.size()>0 ){
						Object ob[] = (Object[])lstDatosDev.get(0);
						iDiasPermitidos = Integer.parseInt(ob[0].toString());
						dMontoPermitido = new BigDecimal(ob[1].toString()).doubleValue();
						
						if(iDiasPermitidos < iDiasTranscurridos){
							bValido = false;							
							//Agregado por lfonseca
							//Dinamisar mensaje de validación, el cual sera manejado desde una tabla a nivel de base de datos
							//Fecha: 2021-01-08
							int i = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(lstConfiguracionMensaje, "CAJA", "REC_CONTADO", "MOSTRARRECIBO", 
									"CARGARDEVOL_V_EMISIONCHEQUE_02");
							
							if(i<0)
								sMensaje +="El tiempo transcurrido es mayor al rango de días permitidos: "+iDiasPermitidos + " días.";
							else
								sMensaje = lstConfiguracionMensaje[i].getDescripcionMensaje().replace("@Dias", String.valueOf(iDiasPermitidos));
						}
						
						if(hDev.getTotal() > dMontoPermitido){
							bValido = false;							
							//Agregado por lfonseca
							//Dinamisar mensaje de validación, el cual sera manejado desde una tabla a nivel de base de datos
							//Fecha: 2021-01-08
							int i = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(lstConfiguracionMensaje, "CAJA", "REC_CONTADO", "MOSTRARRECIBO", 
									"CARGARDEVOL_V_EMISIONCHEQUE_03");
							
							if(i<0)
								sMensaje += "El monto de la devolución es mayor al permitido: "+String.format( "%1$,.2f" , dMontoPermitido )+ " " +hFac.getMoneda();
							else
								sMensaje =  lstConfiguracionMensaje[i].getDescripcionMensaje()
														.replace("@MontoPermitido", String.valueOf(dMontoPermitido))
														.replace("@Moneda", hFac.getMoneda());
						}
					}else{					
						bValido = false;						
						//Agregado por lfonseca
						//Dinamisar mensaje de validación, el cual sera manejado desde una tabla a nivel de base de datos
						//Fecha: 2021-01-08
						int i = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(lstConfiguracionMensaje, "CAJA", "REC_CONTADO", "MOSTRARRECIBO", 
								"CARGARDEVOL_V_EMISIONCHEQUE_04");
						
						if(i<0)
							sMensaje = "No se encuentra la configuración de caja para manejo de devoluciones de días anteriores";
						else
							sMensaje =  lstConfiguracionMensaje[i].getDescripcionMensaje();						
					}
				}else{
					bValido = false;					
					//Agregado por lfonseca
					//Dinamisar mensaje de validación, el cual sera manejado desde una tabla a nivel de base de datos
					//Fecha: 2021-01-08
					int i = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(lstConfiguracionMensaje, "CAJA", "REC_CONTADO", "MOSTRARRECIBO", 
							"CARGARDEVOL_V_EMISIONCHEQUE_01");
					
					if(i<0)
						sMensaje = "La devolución en días posteriores puede ser procesada solo para efectivo";
					else
						sMensaje =  lstConfiguracionMensaje[i].getDescripcionMensaje();
				}				
			}else{
				
				boolean devparcial = hDev.getTotal() < hFac.getTotal() ;
				
				if( bTarjeta  &&  devparcial  ){
					//Agregado por lfonseca
					//Dinamisar mensaje de validación, el cual sera manejado desde una tabla a nivel de base de datos
					//Fecha: 2021-01-08
					int i = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(lstConfiguracionMensaje, "CAJA", "REC_CONTADO", "MOSTRARRECIBO", 
							"CARGARDEVOL_V_EMISIONCHEQUE_05");
					
					if(i<0)
						sMensaje = "La devolucion parcial del dia solo se puede procesar para Efectivo ";
					else
						sMensaje = lstConfiguracionMensaje[i].getDescripcionMensaje();
					
					//Aqui no necesita continuar sino que retorna automaticamente
					return bValido = false;
				}
				if(bOtros && devparcial  && hDev.getTotal() > dTotalPagosDev.doubleValue() ){					
					//Agregado por lfonseca
					//Dinamisar mensaje de validación, el cual sera manejado desde una tabla a nivel de base de datos
					//Fecha: 2021-01-08
					int i = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(lstConfiguracionMensaje, "CAJA", "REC_CONTADO", "MOSTRARRECIBO", 
							"CARGARDEVOL_V_EMISIONCHEQUE_06");
					
					if(i<0)
						sMensaje = "La devolución parcial no puede ser procesada, no hay suficiente monto permitido ";
					else
						sMensaje = lstConfiguracionMensaje[i].getDescripcionMensaje();
					
					 //Aqui no necesita continuar sino retorna automaticamente y no poner nada en session
					return bValido = false;
				}
				if( !bEfect && !bTarjeta && bOtros && ( devparcial || (hDev.getTotal() < dMonto) )  ){ 
					//Agregado por lfonseca
					//Dinamisar mensaje de validación, el cual sera manejado desde una tabla a nivel de base de datos
					//Fecha: 2021-01-08
					int i = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(lstConfiguracionMensaje, "CAJA", "REC_CONTADO", "MOSTRARRECIBO", 
							"CARGARDEVOL_V_EMISIONCHEQUE_07");
					
					if(i<0)
						sMensaje = "La devolucion parcial del dia solo se puede procesar para Efectivo y tarjeta";
					else
						sMensaje = lstConfiguracionMensaje[i].getDescripcionMensaje();

					//Aqui no necesita continuar sino retorna automaticamente y no poner nada en session
					return bValido = false;
				}
			}
		} catch (Exception error) {			
			bValido = false;
			LogCajaService.CreateLog("validarEmisionCheque", "ERR", error.getMessage());
			
			int i = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(lstConfiguracionMensaje, "CAJA", "REC_CONTADO", "MOSTRARRECIBO", 
					"CARGARDEVOL_V_EMISIONCHEQUE_EX");
			
			if(i<0)
				sMensaje = " Error al validar que devolución pueda ser procesada";
			else
				sMensaje = lstConfiguracionMensaje[i].getDescripcionMensaje().replace("@Error", error.getMessage());
		}finally{
			if( !sMensaje.trim().isEmpty() || !bValido ){
				m.put("pfc_sMsgValidaDevolucion", sMensaje);
			}
			
		}

		return bValido;
  }
/*********************************************************************************************/
/**		Método:Validaciones para permitir procesar devolución o generar emisión de cheque
 * 		Nombre: Carlos Manuel Hernández Morrison 	         
 * 		Fecha: 10/05/2010 
 */	public boolean validarEmisionChequexDev(Hfactura hFac,Hfactura hDev, List lstDetalleRecibo,Vf55ca01 f5){
		boolean bValido = true, bEfectivo = true, bMismodia = true;
		FechasUtil f = new FechasUtil();
		CtrlCajas cc = new CtrlCajas();
		Divisas dv = new Divisas();
		Date fechaDev, fechafac;
		int iDiasTranscurridos=0, iDiasPermitidos = 0;
		double dMontoPermitido=0;
		List lstDatosDev = null;
		String sMensaje = "",sEstilo = "<br><img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" />";
		
		try {
			m.remove("pfc_sMsgValidaDevolucion");
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			fechaDev = df.parse(hDev.getFecha());
			fechafac = df.parse(hFac.getFecha());
			
			//---- la factura y devolución son del mismo día?
			if(fechaDev.compareTo(fechafac)!=0){
				bMismodia = false;
				
				//---- Intervalo de días y configuración de caja para devoluciones.
				iDiasTranscurridos = f.obtieneCantDiasHabiles(fechafac, fechaDev);
				lstDatosDev = cc.obtenerF55ca028(f5.getId().getCaid(),hFac.getCodcomp(),hFac.getMoneda());
				
				//---- Verificar que sea efectivo.
				for(int i=0; i<lstDetalleRecibo.size(); i++){
					Recibodet rd = (Recibodet)lstDetalleRecibo.get(i);
					if(!rd.getId().getMpago().equals( MetodosPagoCtrl.EFECTIVO) ){
						bEfectivo = false;
					}
				}
				//--- Validaciones para procesar o emisión de cheque.
				if(bEfectivo){
					if(lstDatosDev!=null && lstDatosDev.size()>0 ){
						Object ob[] = (Object[])lstDatosDev.get(0);
						iDiasPermitidos = Integer.parseInt(ob[0].toString());
						dMontoPermitido = new BigDecimal(ob[1].toString()).doubleValue();
						
						if(iDiasPermitidos < iDiasTranscurridos){
							bValido = false;
							sMensaje += sEstilo+  "El tiempo transcurrido es mayor al rango de días permitidos: "+iDiasPermitidos + " días.";
						}
						if(hDev.getTotal() > dMontoPermitido){
							bValido = false;
							sMensaje += sEstilo + "El monto de la devolución es mayor al permitido: "+dv.formatDouble(dMontoPermitido)+ " " +hFac.getMoneda();
						}
					}else{					
						bValido = false;
						sMensaje = sEstilo + "No se encuentra la configuración de caja para manejo de devoluciones de días anteriores";
					}
				}else{
					bValido = false;
					sMensaje = sEstilo + "Devoluciones a facturas de días anteriores solo se pueden procesar si fueron pagadas en efectivo";
				}
				if(!bValido)
					m.put("pfc_sMsgValidaDevolucion", sMensaje);
			}
		} catch (Exception error) {
			bValido = false;
			error.printStackTrace();
		}		
		return bValido;
	}
 	public void cancelarSolicitaEmisionChk(ActionEvent ev){
 		m.remove("facturasSelected");
 		dwConfirmaEmisionCheque.setWindowState("hidden");
 	}	
    /** ************************************************************************************* */
 	/** *********************************************************************************************** 
 	 * @throws Exception */
	public void cargarDevolucion(Hfactura hFac, List<Hfactura> lstFacturasSelected,
			String sUltimoNumero, int iCajaId, String sTasaJDE,
			String sTasaParalela, Tpararela[] tpcambio, String sMonedaBase, ConfiguracionMensaje[] lstConfiguracionMensaje) throws Exception {
		
		Hfactura hFacOriginal = null;
		Vrecfac recfac = null;
		FacturaCrtl facCtrl = new FacturaCrtl();
		ReciboCtrl recCtrl = new ReciboCtrl();
		List<Recibodet> lstDetalleReciboOriginal = null;
		List lstMetodosSugeridos = new ArrayList();
		MetodosPago metpagos = null;
		double dProcesado = 0.0;
		double dFaltante = 0.0;
		double dEfectivoDev = 0.0;
		double dEquiv = 0.0;
		double dTasaJDE = 0.0;
		double dTasaParalela = 0.0,dTotal=0.0;
		boolean bTc = false, bCheque = false;
		MetodosPagoCtrl metCtrl = new MetodosPagoCtrl();
		Recibodet recibodet = null;
		double dEfectRestante = 0.0;
		Divisas d = new Divisas();
		SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
	
		try {
			
			LogCajaService.CreateLog("cargarDevolucion", "INFO", "cargarDevolucion-INICIO");
			
			List lstCajas = (List) m.get("lstCajas");
			Vf55ca01 f55ca01 = ((Vf55ca01) lstCajas.get(0));
			
			limpiarDevolucion();
			restablecerEstiloProcesarDev();
			restablecerEstiloDevolucion();
			
			// info general del recibo
			fechaReciboDev.setValue(new Date());
			txtNumeroReciboDev.setValue(sUltimoNumero);
			lblCodigoClienteDev.setValue(hFac.getCodcli() + "");
			lblNombreClienteDev.setValue(hFac.getNomcli());
			 
			
			lblNoFactDev.setValue(hFac.getNofactura());
			lblTipoDocFactDev.setValue(hFac.getTipofactura());
			lblMonedaFactDev.setValue(hFac.getMoneda());
			lblMontoFactDev.setValue(hFac.getTotal());
			lblCodsucDev.setValue(hFac.getCodsuc());
			lblCoduninegDev.setValue(hFac.getCodunineg());

			// info general de factura original
			lblNodoco.setValue(hFac.getNodoco());
			lblTipodoco.setValue(hFac.getTipodoco());
			
			//&& ================= Conservar la fecha de la factura original.
			CodeUtil.putInSessionMap("fdc_FechaFacturaOriginal", hFac.getFechadoco()) ;
			
			

			for (int i = 0; i < lstFacturasSelected.size(); i++) {
				Hfactura hfac1 = lstFacturasSelected.get(i);

				String lineaDeFactura = CompaniaCtrl.leerLineaNegocio( hfac1.getCodunineg().trim() );
				
				//Se validara que sLineaFactura no venga vacia o null
				if(lineaDeFactura == null || lineaDeFactura.equals(""))
				{
					String msgProceso = "";
					
					int j = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(lstConfiguracionMensaje, "CAJA", "REC_CONTADO", "MOSTRARRECIBO", 
							"CARGARDEVOL_01");
					
					if(j<0)
						msgProceso = "No se logro encontrar linea de negocio asociado a la unidad de negocio: " + hfac1.getCodunineg().trim();
					else
						msgProceso = lstConfiguracionMensaje[j].getDescripcionMensaje()
								.replace("@UnidadNegocio",hfac1.getCodunineg().trim());					
					
					throw new Exception(msgProceso);
				}
				
				if (PropertiesSystem.LNS_REPUESTOS.contains( lineaDeFactura.trim() ) && hfac1.getMoneda().compareTo(sMonedaBase) != 0) {

					hfac1.setTasa(hfac1.getTotaldomtmp().divide(
							new BigDecimal(String.valueOf(hfac1.getTotal())),
							4, RoundingMode.HALF_UP));
					lstFacturasSelected.set(i, hfac1);
				}
			}
			
			// obtener factura original
			hFacOriginal = facCtrl.getFacturaOriginal(hFac.getNodoco(), hFac
					.getTipodoco(), hFac.getCodsuc(), hFac.getCodcomp(), hFac
					.getCodunineg(), hFac.getFechadoco() );
			
			if (hFacOriginal != null) {
				String sFecha = hFacOriginal.getFecha();
				FechasUtil fecUtil = new FechasUtil();

				lblMonedaOdev.setValue(hFacOriginal.getMoneda());
				lblMontoOdev.setValue(hFacOriginal.getTotal());
				lblFechadev.setValue(hFacOriginal.getFecha());
				
				//&& ==========  leer recibo de factura original
				recfac = recCtrl.getReciboFac(iCajaId, f55ca01.getId().getCaco().trim(), 
								hFacOriginal.getCodcomp(), 
								hFac.getNodoco(),
								hFac.getTipodoco(),
								hFacOriginal.getCodunineg(),
								hFacOriginal.getCodcli(),
								hFacOriginal.getFechajulian()); 
				
				//&& ========= Equivalentes de montos, para factura y devolucion.
				lblDevMontoEquiv.setValue(String.format("%1$,.2f", hFac.getTotaldomtmp()) );
				lblMontoEquiv.setValue(String.format("%1$,.2f", hFac.getTotaldomtmp()));
				
				
				if (recfac != null) {// se encontro el recibo
					// leer detalle de recibo original
					lstDetalleReciboOriginal = recCtrl.leerDetalleRecibo(recfac.getId().getCaid(), f55ca01.getId().getCaco().trim(), hFac.getCodcomp(), recfac.getId().getNumrec(),recfac.getId().getTiporec());
				
					//Agregado por lfonseca
					//2021-01-08
					if(lstDetalleReciboOriginal.size()==0)
					{
						String msgProceso = "";
						
						int i = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(lstConfiguracionMensaje, "CAJA", "REC_CONTADO", "MOSTRARRECIBO", 
								"CARGARDEVOL_04");
						
						if(i<0)
							msgProceso = "No se encontro registro de detalle de recibo para el recibo numero " + recfac.getId().getNumrec() + " asociado a la factura original ";
						else
							msgProceso = lstConfiguracionMensaje[i].getDescripcionMensaje()
									.replace("@Recibo", String.valueOf(recfac.getId().getNumrec()));						
						
						throw new Exception(msgProceso);
					}
					
					for (int n = 0; n < lstDetalleReciboOriginal.size(); n++) {
						recibodet = (Recibodet) lstDetalleReciboOriginal.get(n);
						if (recibodet.getId().getMpago().equals(MetodosPagoCtrl.DEPOSITO) || recibodet.getId().getMpago().equals(MetodosPagoCtrl.TRANSFERENCIA)) {
							bCheque = true;
						}
					}				
					
					//------------------------------------------- Validación para días permitidos de devolución.(Add: 23/04/2010)
					boolean bProcesar = validarEmisionCheque(hFacOriginal, hFac, lstDetalleReciboOriginal, f55ca01, lstConfiguracionMensaje);
					if(!bProcesar){
						if(m.get("pfc_sMsgValidaDevolucion")!=null){
							String sMensaje = m.get("pfc_sMsgValidaDevolucion").toString();
							dwConfirmaEmisionCheque.setWindowState("normal");
							lblMsgValidaSolecheque.setValue(sMensaje);
							m.put("fcd_lstDetalleReciboOriginal", lstDetalleReciboOriginal);
							m.put("fcd_hFactura",hFacOriginal);
							m.put("fcd_hDevolución",hFac);
						}else{
							dwProcesa.setWindowState("normal");
							
							int i = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(lstConfiguracionMensaje, "CAJA", "REC_CONTADO", "MOSTRARRECIBO", 
									"CARGARDEVOL_05");
							
							if(i<0)
								lblMensajeValidacion.setValue("No se pudo realizar la operacion: -->Error de Sistema en FacContadoDAO.ValidarEmisiondeCheque ");
							else
								lblMensajeValidacion.setValue(lstConfiguracionMensaje[i].getDescripcionMensaje());
						}
					}else{
		
						{
							lblNoReciboOdev.setValue(recfac.getId().getNumrec());
							lblTasaCambioDev.setValue(sTasaParalela);
							lblTasaJDEDev.setValue(sTasaJDE);
	
							boolean bPaso = false;
							String metDesc = "";
							// establecer forma de pago sugerida
							
							//&& === Devolucion total y pagada con un solo metodo.
							if ( hFacOriginal.getTotal() == hFac.getTotal() &&  	lstDetalleReciboOriginal.size() == 1) {
								
								Recibodet rd = (Recibodet) lstDetalleReciboOriginal.get(0);
								
								if(hFac.getTasa().compareTo(BigDecimal.ONE) == 1){ 
									if(rd.getId().getMoneda().compareTo(hFac.getMoneda()) == 0){
										dEfectivoDev = hFac.getTotalf();
										dEquiv = hFac.getTotal();
									}else{
										String lineaDeFactura2 = CompaniaCtrl.leerLineaNegocio( hFac.getCodunineg().trim() );
										
										//Se validara que sLineaFactura no venga vacia o null
										if(lineaDeFactura2 == null || lineaDeFactura2.equals(""))
										{
											String msgProceso = "";
											
											int j = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(lstConfiguracionMensaje, "CAJA", "REC_CONTADO", "MOSTRARRECIBO", 
													"CARGARDEVOL_06");
											
											if(j<0)
												msgProceso = "No se logro encontrar linea de negocio asociado a la unidad de negocio: " + hFac.getCodunineg().trim();
											else
												msgProceso = lstConfiguracionMensaje[j].getDescripcionMensaje()
														.replace("@UnidadNegocio",hFac.getCodunineg().trim());
											
											throw new Exception(msgProceso);
										}
										
										if(PropertiesSystem.LNS_TALLER.contains( lineaDeFactura2.trim() ))
										{
											dEquiv = hFac.getDpendiente();
											dEfectivoDev = Divisas.roundDouble(
													hFac.getDpendiente()*rd.getTasa().doubleValue(),2);
										}
										else
										{
											dEfectivoDev = hFac.getCpendiente();
											dEquiv = hFac.getDpendiente();
										}
									}
								}else{
									if(rd.getId().getMoneda().compareTo(hFac.getMoneda()) == 0){
										dEfectivoDev = hFac.getTotal();
										dEquiv = hFac.getTotal();
									}else{
										dEquiv = hFac.getTotal() ;
										dEfectivoDev = Divisas.roundDouble( 
												hFac.getTotal() / 
												rd.getTasa().doubleValue(),2);
									}
								}
								
								metpagos = new MetodosPago(
										com.casapellas.controles.MetodosPagoCtrl.descripcionMetodoPago(rd.getId().getMpago()),
										rd.getId().getMpago(), 
										rd.getId().getMoneda(),
										dEfectivoDev, rd.getTasa(), dEquiv,
										rd.getId().getRefer1(),
										rd.getId().getRefer2(),
										rd.getId().getRefer3(),
										rd.getId().getRefer4(),
										rd.getVmanual(),
										rd.getCaidpos());
								
								metpagos.setCodigomarcatarjeta(rd.getCodigomarcatarjeta());
								metpagos.setMarcatarjeta(rd.getMarcatarjeta());
								
								try {
										metpagos.setReferencia5(recibodet.getRefer5());
										metpagos.setReferencia6(recibodet.getRefer6());
										metpagos.setReferencia7(recibodet.getRefer7());
										metpagos.setNombre(recibodet.getNombre());
										metpagos.setMontopos(recibodet.getMonto().doubleValue());
								}catch(Exception e) {
									LogCajaService.CreateLog("cargarDevolucion", "ERR", e.getMessage());
								}
								
								lstMetodosSugeridos.add(metpagos);
								dProcesado = dEfectivoDev;	
								bPaso = true;
							
							}
							else
							
							if (hFacOriginal.getTotal() == hFac.getTotal()) {// sugerir devolver lo mismo
	
								for (int m = 0; m < lstDetalleReciboOriginal.size(); m++) {
	
									recibodet = (Recibodet) lstDetalleReciboOriginal.get(m);
	
									// agregar a sugeridos
									bPaso = false;
									if (recibodet.getId().getMpago().equals( MetodosPagoCtrl.EFECTIVO)) {// si es efectivo
										// leer detalle de cambio
										Cambiodet[] cambio = recCtrl.leerDetalleCambio(recibodet.getId().getCaid(), f55ca01.getId().getCaco().trim(),hFac.getCodcomp(), recfac.getId().getNumrec());
										for (int l = 0; l < cambio.length; l++) {
											if (recibodet.getId().getMoneda().equals(cambio[l].getId().getMoneda())) {
												dEfectivoDev = recibodet.getMonto().doubleValue()- cambio[l].getCambio().doubleValue();
												// calcular equivalente
												if(hFac.getTasa().doubleValue() > 1){//es foranea
													if (recibodet.getId().getMoneda().equals(hFac.getMoneda())) {//F: USD P:USD
														dEquiv = dEfectivoDev;//d.roundDouble4(dEfectivoDev * recibodet.getTasa().doubleValue());
													} else {//F: USD P:COR
														dEquiv = dEfectivoDev / recibodet.getTasa().doubleValue();  //Cambio hecho por luis fonseca
													}
												}else{// es domestica
													if (recibodet.getId().getMoneda().equals(hFac.getMoneda())) {//F:COR P:COR
														dEquiv = dEfectivoDev;
													} else {//F:COR P:USD
														dEquiv = d.roundDouble4(dEfectivoDev * recibodet.getTasa().doubleValue());
													}
												}
												bPaso = true;
											}
										}
										// poner descripcion a metodos
										Metpago formapago = com.casapellas.controles.MetodosPagoCtrl.metodoPagoCodigo( recibodet.getId().getMpago().trim() );
										
										//Agregado por lfonseca
										//2021-01-08
										if(formapago==null)
										{
											String msgProceso = "";
												
											int j = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(lstConfiguracionMensaje, "CAJA", "REC_CONTADO", "MOSTRARRECIBO", 
													"CARGARDEVOL_07");
											
											if(j<0)
												msgProceso = "No se logro encontrar metodo de pago con código: " + recibodet.getId().getMpago().trim();
											else
												msgProceso = lstConfiguracionMensaje[j].getDescripcionMensaje()
														.replace("@MetodoPago",recibodet.getId().getMpago().trim());
											
											throw new Exception(msgProceso);
										}
										
										metDesc = formapago.getId().getMpago();

										if (bPaso) {
											metpagos = new MetodosPago(metDesc,recibodet.getId().getMpago(), recibodet.getId().getMoneda(),
													dEfectivoDev, recibodet.getTasa(), dEquiv,
													recibodet.getId().getRefer1(),
													recibodet.getId().getRefer2(),
													recibodet.getId().getRefer3(),
													recibodet.getId().getRefer4(),
													recibodet.getVmanual(),
													recibodet.getCaidpos());
											
											metpagos.setCodigomarcatarjeta(recibodet.getCodigomarcatarjeta());
											metpagos.setMarcatarjeta(recibodet.getMarcatarjeta());
											
											try {
												metpagos.setReferencia5(recibodet.getRefer5());
												metpagos.setReferencia6(recibodet.getRefer6());
												metpagos.setReferencia7(recibodet.getRefer7());
												metpagos.setNombre(recibodet.getNombre());
												metpagos.setMontopos(recibodet.getMonto().doubleValue());
											}catch(Exception e) {
												LogCajaService.CreateLog("cargarDevolucion", "ERR", e.getMessage());
											}
										} else {
											metpagos = new MetodosPago(metDesc,recibodet.getId().getMpago(), recibodet.getId().getMoneda(), recibodet.getMonto().doubleValue(),
													recibodet.getTasa(), recibodet.getEquiv().doubleValue(),
													recibodet.getId().getRefer1(),
													recibodet.getId().getRefer2(),
													recibodet.getId().getRefer3(),
													recibodet.getId().getRefer4(),
													recibodet.getVmanual(),
													recibodet.getCaidpos());
											
											metpagos.setCodigomarcatarjeta(recibodet.getCodigomarcatarjeta());
											metpagos.setMarcatarjeta(recibodet.getMarcatarjeta());
											
											try {
												metpagos.setReferencia5(recibodet.getRefer5());
												metpagos.setReferencia6(recibodet.getRefer6());
												metpagos.setReferencia7(recibodet.getRefer7());
												metpagos.setNombre(recibodet.getNombre());
												metpagos.setMontopos(recibodet.getMonto().doubleValue());
											}catch(Exception e) {
												LogCajaService.CreateLog("cargarDevolucion", "ERR", e.getMessage());
											}
											
											bPaso = true;
										}
										
										lstMetodosSugeridos.add(metpagos);
										dProcesado = dProcesado+ metpagos.getMonto();
										// }
	
									} else {
										// poner descripcion a metodos

										Metpago formapago = com.casapellas.controles.MetodosPagoCtrl.metodoPagoCodigo( recibodet.getId().getMpago().trim() );
										metDesc = formapago.getId().getMpago();
										
										
										metpagos = new MetodosPago(metDesc,recibodet.getId().getMpago(), recibodet.getId().getMoneda(),
															recibodet.getMonto().doubleValue(), recibodet.getTasa(),
															recibodet.getEquiv().doubleValue(), recibodet.getId().getRefer1(),
															recibodet.getId().getRefer2(), recibodet.getId().getRefer3(),
															recibodet.getId().getRefer4(), recibodet.getVmanual(),
															recibodet.getCaidpos(), recibodet.getRefer5(),
															recibodet.getRefer6(), recibodet.getRefer7(), 
															recibodet.getMonto().doubleValue()
															);
										
										metpagos.setCodigomarcatarjeta(recibodet.getCodigomarcatarjeta());
										metpagos.setMarcatarjeta(recibodet.getMarcatarjeta());
										
										try {
											metpagos.setReferencia5(recibodet.getRefer5());
											metpagos.setReferencia6(recibodet.getRefer6());
											metpagos.setReferencia7(recibodet.getRefer7());
											metpagos.setNombre(recibodet.getNombre());
											metpagos.setMontopos(recibodet.getMonto().doubleValue());
										}catch(Exception e) {
											LogCajaService.CreateLog("cargarDevolucion", "ERR", e.getMessage());
										}
										
										lstMetodosSugeridos.add(metpagos);
										dProcesado = dProcesado+ metpagos.getMonto();
										bPaso = true;
									}
								}
							} else {// sugerir devolver lo que cubra el efectivo comprobar si se pago con tarjeta de credito
								for (int m = 0; m < lstDetalleReciboOriginal.size(); m++) {
									recibodet = (Recibodet) lstDetalleReciboOriginal.get(m);
									if (recibodet.getId().getMpago().equals(MetodosPagoCtrl.TARJETA)) {
										bTc = true;
										break;
									}
								}
								//
								if (bTc) {
									for (int m = 0; m < lstDetalleReciboOriginal.size(); m++) {
										recibodet = (Recibodet) lstDetalleReciboOriginal.get(m);
										if (recibodet.getId().getMpago().equals(MetodosPagoCtrl.TARJETA)) {
											if (recibodet.getMonto().doubleValue() < hFac.getTotal()) {
												dEfectRestante = hFac.getTotal() - recibodet.getMonto().doubleValue();
												if (recibodet.getId().getMoneda().equals(sMonedaBase)) {
													dEquiv = recibodet.getMonto().doubleValue();
												} else {
													dEquiv = recibodet.getMonto().doubleValue() * recibodet.getTasa().doubleValue();
												}
												// poner descripcion a metodos												
												Metpago formapago = com.casapellas.controles.MetodosPagoCtrl.metodoPagoCodigo( recibodet.getId().getMpago().trim() );
												
												//Agregado por lfonseca
												//2021-01-08
												if(formapago==null)
												{
													String msgProceso = "";
														
													int j = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(lstConfiguracionMensaje, "CAJA", "REC_CONTADO", "MOSTRARRECIBO", 
															"CARGARDEVOL_07");
													
													if(j<0)
														msgProceso = "No se logro encontrar metodo de pago con código: " + recibodet.getId().getMpago().trim();
													else
														msgProceso = lstConfiguracionMensaje[j].getDescripcionMensaje()
																.replace("@MetodoPago",recibodet.getId().getMpago().trim());
													
													
													throw new Exception(msgProceso);
												}
												
												metDesc = formapago.getId().getMpago();
												
												recibodet.getId().setMpago(metDesc);
												
												metpagos = new MetodosPago(
														recibodet.getId().getMpago(),
														recibodet.getId().getMoneda(),
														recibodet.getMonto().doubleValue(),
														recibodet.getTasa(),
														dEquiv, recibodet.getId().getRefer1(),
														recibodet.getId().getRefer2(),
														recibodet.getId().getRefer3(),
														recibodet.getId().getRefer4(),
														recibodet.getVmanual(),
														recibodet.getCaidpos());
														metpagos.setReferencia5(recibodet.getRefer5());
														metpagos.setReferencia6(recibodet.getRefer6());
														metpagos.setReferencia7(recibodet.getRefer7());
														metpagos.setNombre(recibodet.getNombre());
														metpagos.setMontopos(recibodet.getMonto().doubleValue());
														
													metpagos.setCodigomarcatarjeta(recibodet.getCodigomarcatarjeta());
													metpagos.setMarcatarjeta(recibodet.getMarcatarjeta());
														
												lstMetodosSugeridos.add(metpagos);
												dProcesado = dProcesado+ metpagos.getMonto();
												
											} else if (recibodet.getMonto().doubleValue() == hFac.getTotal()) {
												Metpago formapago = com.casapellas.controles.MetodosPagoCtrl.metodoPagoCodigo( recibodet.getId().getMpago().trim() );
												//Agregado por lfonseca
												//2021-01-08
												if(formapago==null)
												{
													String msgProceso = "";
														
													int j = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(lstConfiguracionMensaje, "CAJA", "REC_CONTADO", "MOSTRARRECIBO", 
															"CARGARDEVOL_07");
													
													if(j<0)
														msgProceso = "No se logro encontrar metodo de pago con código: " + recibodet.getId().getMpago().trim();
													else
														msgProceso = lstConfiguracionMensaje[j].getDescripcionMensaje()
																.replace("@MetodoPago",recibodet.getId().getMpago().trim());
													throw new Exception(msgProceso);
												}
												
												
												metDesc = formapago.getId().getMpago();
												
												metpagos = new MetodosPago(metDesc,
														recibodet.getId().getMpago(),
														recibodet.getId().getMoneda(),
														recibodet.getMonto().doubleValue(),
														recibodet.getTasa(),
														recibodet.getEquiv().doubleValue(),
														recibodet.getId().getRefer1(),
														recibodet.getId().getRefer2(),
														recibodet.getId().getRefer3(),
														recibodet.getId().getRefer4(),
														recibodet.getVmanual(),
														recibodet.getCaidpos());
														metpagos.setReferencia5(recibodet.getRefer5());
														metpagos.setReferencia6(recibodet.getRefer6());
														metpagos.setReferencia7(recibodet.getRefer7());
														metpagos.setNombre(recibodet.getNombre());
														metpagos.setMontopos(recibodet.getMonto().doubleValue());
														
												metpagos.setCodigomarcatarjeta(recibodet.getCodigomarcatarjeta());
												metpagos.setMarcatarjeta(recibodet.getMarcatarjeta());		
														
												lstMetodosSugeridos.add(metpagos);
												dProcesado = dProcesado + metpagos.getMonto();
											} else {
												if (recibodet.getId().getMoneda().equals(sMonedaBase)) {
													dEquiv = hFac.getTotal();
												} else {
													dEquiv = hFac.getTotal() * recibodet.getTasa().doubleValue();
												}
												
												// poner descripcion a metodos
												Metpago formapago = com.casapellas.controles.MetodosPagoCtrl.metodoPagoCodigo( recibodet.getId().getMpago().trim() );
												
												//Agregado por lfonseca
												//2021-01-08
												if(formapago==null)
												{
													String msgProceso = "";
														
													int j = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(lstConfiguracionMensaje, "CAJA", "REC_CONTADO", "MOSTRARRECIBO", 
															"CARGARDEVOL_07");
													
													if(j<0)
														msgProceso = "No se logro encontrar metodo de pago con código: " + recibodet.getId().getMpago().trim();
													else
														msgProceso = lstConfiguracionMensaje[j].getDescripcionMensaje()
																.replace("@MetodoPago",recibodet.getId().getMpago().trim());
													throw new Exception(msgProceso);
												}
												
												metDesc = formapago.getId().getMpago();
												
												metpagos = new MetodosPago(metDesc,
														recibodet.getId().getMpago(),
														recibodet.getId().getMoneda(), hFac.getTotal(),
														recibodet.getTasa(),
														dEquiv, recibodet.getId().getRefer1(),
														recibodet.getId().getRefer2(),
														recibodet.getId().getRefer3(),
														recibodet.getId().getRefer4(),
														recibodet.getVmanual(),
														recibodet.getCaidpos());
														metpagos.setReferencia5(recibodet.getRefer5());
														metpagos.setReferencia6(recibodet.getRefer6());
														metpagos.setReferencia7(recibodet.getRefer7());
														metpagos.setNombre(recibodet.getNombre());
														metpagos.setMontopos(recibodet.getMonto().doubleValue());
												
												metpagos.setCodigomarcatarjeta(recibodet.getCodigomarcatarjeta());
												metpagos.setMarcatarjeta(recibodet.getMarcatarjeta());			
														
												lstMetodosSugeridos.add(metpagos);
												dProcesado = dProcesado + metpagos.getMonto();
											}
										}
										break;
									}
								} else {
									dTotal =  hFac.getTotal();double dMontoF = 0.0;
									if(hFac.getMoneda().equals(sMonedaBase)){//facturas en cordobas
										for (int m = 0; m < lstDetalleReciboOriginal.size(); m++) {
											if(dTotal == 0){break;}
											recibodet = (Recibodet) lstDetalleReciboOriginal.get(m);
											if (recibodet.getId().getMoneda().equals(sMonedaBase)) {
												if (recibodet.getMonto().doubleValue() > dTotal) {// se pago mas con el metodo de lo q se devuelve
													dEquiv = dTotal;
													dTotal = 0;
												}else{
													dEquiv = recibodet.getMonto().doubleValue();
													dTotal = d.roundDouble4(dTotal - dEquiv);
												}											
												
												Metpago formapago = com.casapellas.controles.MetodosPagoCtrl.metodoPagoCodigo( recibodet.getId().getMpago().trim() );
												//Agregado por lfonseca
												//2021-01-08
												if(formapago==null)
												{
													String msgProceso = "";
														
													int j = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(lstConfiguracionMensaje, "CAJA", "REC_CONTADO", "MOSTRARRECIBO", 
															"CARGARDEVOL_07");
													
													if(j<0)
														msgProceso = "No se logro encontrar metodo de pago con código: " + recibodet.getId().getMpago().trim();
													else
														msgProceso = lstConfiguracionMensaje[j].getDescripcionMensaje()
																.replace("@MetodoPago",recibodet.getId().getMpago().trim());
													
													throw new Exception(msgProceso);
												}
												
												metDesc = formapago.getId().getMpago();
												
												metpagos = new MetodosPago(metDesc,recibodet.getId().getMpago(), recibodet.getId().getMoneda(), 
														 dEquiv, recibodet.getTasa(), dEquiv, recibodet
														.getId().getRefer1(), recibodet
														.getId().getRefer2(), recibodet
														.getId().getRefer3(), recibodet
														.getId().getRefer4(),
														recibodet.getVmanual(),
														recibodet.getCaidpos());
												metpagos.setMontopos(dEquiv);
												
												metpagos.setCodigomarcatarjeta(recibodet.getCodigomarcatarjeta());
												metpagos.setMarcatarjeta(recibodet.getMarcatarjeta());	
												
												try {
													metpagos.setReferencia5(recibodet.getRefer5());
													metpagos.setReferencia6(recibodet.getRefer6());
													metpagos.setReferencia7(recibodet.getRefer7());
													metpagos.setNombre(recibodet.getNombre());
													metpagos.setMontopos(recibodet.getMonto().doubleValue());
												}catch(Exception e) {
													LogCajaService.CreateLog("cargarDevolucion", "ERR", e.getMessage());
												}
												
												bPaso = true;
											}//fin de if de cordobas
											else{//el pago original es en dolares
												if (recibodet.getEquiv().doubleValue() > dTotal) {// se pago mas con el metodo de lo q se devuelve
													
													dEquiv = dTotal;
													dMontoF = d.roundDouble4(dEquiv/recibodet.getTasa().doubleValue());
													dTotal = 0;
												}else{
													dEquiv = recibodet.getEquiv().doubleValue();
													dMontoF = d.roundDouble4(dEquiv/recibodet.getTasa().doubleValue());
													dTotal = d.roundDouble4(dTotal - dEquiv);
												}											
												metDesc = metCtrl.obtenerDescripcionMetodoPago(recibodet.getId().getMpago().trim());
												metpagos = new MetodosPago(metDesc,recibodet.getId().getMpago(), recibodet.getId().getMoneda(),
														 dMontoF, recibodet.getTasa(), dEquiv, 
														 recibodet.getId().getRefer1(), recibodet
														.getId().getRefer2(), recibodet
														.getId().getRefer3(), recibodet
														.getId().getRefer4(), 
														recibodet.getVmanual(),
														recibodet.getCaidpos());
												
												metpagos.setCodigomarcatarjeta(recibodet.getCodigomarcatarjeta());
												metpagos.setMarcatarjeta(recibodet.getMarcatarjeta());	
												
												bPaso = true;
											}
											metpagos.setReferencia5("");
											metpagos.setReferencia6("");
											metpagos.setReferencia7("");
											metpagos.setMontopos(dMontoF);
											
											metpagos.setCodigomarcatarjeta(recibodet.getCodigomarcatarjeta());
											metpagos.setMarcatarjeta(recibodet.getMarcatarjeta());	
											
											try {
												metpagos.setReferencia5(recibodet.getRefer5());
												metpagos.setReferencia6(recibodet.getRefer6());
												metpagos.setReferencia7(recibodet.getRefer7());
												metpagos.setNombre(recibodet.getNombre());
												metpagos.setMontopos(recibodet.getMonto().doubleValue());
											}catch(Exception e) {
												LogCajaService.CreateLog("cargarDevolucion", "ERR", e.getMessage());
											}
											
											lstMetodosSugeridos.add(metpagos);
										}//fin de for de metodos de pago
									}else{//facturas en dolares
										for (int m = 0; m < lstDetalleReciboOriginal.size(); m++) {
											if(dTotal == 0){break;}
											recibodet = (Recibodet) lstDetalleReciboOriginal.get(m);
											if (!recibodet.getId().getMoneda().equals(sMonedaBase)) {
												if (recibodet.getMonto().doubleValue() > dTotal) {// se pago mas con el metodo de lo q se devuelve
													dEquiv = dTotal;
													dTotal = 0;
												}else{
													dEquiv = recibodet.getMonto().doubleValue();
													dTotal = d.roundDouble4(dTotal - dEquiv);
												}											
												
												Metpago formapago = com.casapellas.controles.MetodosPagoCtrl.metodoPagoCodigo( recibodet.getId().getMpago().trim() );
												//Agregado por lfonseca
												//2021-01-08
												if(formapago==null)
												{
													String msgProceso = "";
														
													int j = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(lstConfiguracionMensaje, "CAJA", "REC_CONTADO", "MOSTRARRECIBO", 
															"CARGARDEVOL_07");
													
													if(j<0)
														msgProceso = "No se logro encontrar metodo de pago con código: " + recibodet.getId().getMpago().trim();
													else
														msgProceso = lstConfiguracionMensaje[j].getDescripcionMensaje()
																.replace("@MetodoPago",recibodet.getId().getMpago().trim());
													throw new Exception(msgProceso);
												}
												
												metDesc = formapago.getId().getMpago();
												
												
												metpagos = new MetodosPago(metDesc,recibodet.getId().getMpago(), recibodet.getId().getMoneda(), 
														 dEquiv, recibodet.getTasa(), dEquiv, recibodet
														.getId().getRefer1(), recibodet
														.getId().getRefer2(), recibodet
														.getId().getRefer3(), recibodet
														.getId().getRefer4(), 
														recibodet.getVmanual(),
														recibodet.getCaidpos());
												
												metpagos.setCodigomarcatarjeta(recibodet.getCodigomarcatarjeta());
												metpagos.setMarcatarjeta(recibodet.getMarcatarjeta());	
												
												try {
													metpagos.setReferencia5(recibodet.getRefer5());
													metpagos.setReferencia6(recibodet.getRefer6());
													metpagos.setReferencia7(recibodet.getRefer7());
													metpagos.setNombre(recibodet.getNombre());
													metpagos.setMontopos(recibodet.getMonto().doubleValue());
												}catch(Exception e) {
													e.printStackTrace();
												}
												
												bPaso = true;
											}//fin de if de cordobas
											else{//el pago original es en Cordobas
												if (recibodet.getEquiv().doubleValue() > dTotal) {// se pago mas con el metodo de lo q se devuelve
													dEquiv = dTotal;
													dMontoF = d.roundDouble4(dEquiv*recibodet.getTasa().doubleValue());
													dTotal = 0;
												}else{
													dEquiv = recibodet.getEquiv().doubleValue();
													dMontoF = d.roundDouble4(dEquiv*recibodet.getTasa().doubleValue());
													dTotal = d.roundDouble4(dTotal - dEquiv);
												}											
												
												Metpago formapago = com.casapellas.controles.MetodosPagoCtrl.metodoPagoCodigo( recibodet.getId().getMpago().trim() );
												metDesc = formapago.getId().getMpago();
												
												metpagos = new MetodosPago(metDesc,recibodet.getId().getMpago(), recibodet.getId().getMoneda(),
														 dMontoF, recibodet.getTasa(), dEquiv, 
														 recibodet.getId().getRefer1(), recibodet
														.getId().getRefer2(), recibodet
														.getId().getRefer3(), recibodet
														.getId().getRefer4(),
														recibodet.getVmanual(),
														recibodet.getCaidpos());
												
												metpagos.setCodigomarcatarjeta(recibodet.getCodigomarcatarjeta());
												metpagos.setMarcatarjeta(recibodet.getMarcatarjeta());
												
												try {
													metpagos.setReferencia5(recibodet.getRefer5());
													metpagos.setReferencia6(recibodet.getRefer6());
													metpagos.setReferencia7(recibodet.getRefer7());
													metpagos.setNombre(recibodet.getNombre());
													metpagos.setMontopos(recibodet.getMonto().doubleValue());
												}catch(Exception e) {
													LogCajaService.CreateLog("cargarDevolucion", "ERR", e.getMessage());
												}
												
												bPaso = true;
											}
											lstMetodosSugeridos.add(metpagos);
										}//fin de for de metodos de pago
									}									
								}
							}
							
							for (Recibodet rd : lstDetalleReciboOriginal) 
								rd.setDescripcionformapago( com.casapellas.controles.MetodosPagoCtrl.descripcionMetodoPago(rd.getId().getMpago() ) );
							
							m.put("lstDetalleReciboOriginal",lstDetalleReciboOriginal);
							gvDetalleReciboOriginal.dataBind();
							lstDetalleReciboFactDev = lstMetodosSugeridos;
							m.put("lstDetalleReciboFactDev",lstDetalleReciboFactDev);
							gvDetalleReciboFactDev.dataBind();
							if (bPaso) {
								dProcesado = hFac.getTotal();
								dFaltante = hFac.getTotal() - dProcesado;
							} else {
								dProcesado = 0;
								dFaltante = hFac.getTotal();
							}
	
							// resumen de pago
							lblMontoAplicarDev.setValue("Monto a Aplicar "+ hFac.getMoneda() + ":");
							txtMontoAplicarDev.setValue(hFac.getTotal());
							lblMontoProcesado.setValue("Monto Procesado " + hFac.getMoneda() + ":");
							txtMontoProcesado.setValue(dProcesado);
							lblFaltante.setValue("Monto Faltante "+ hFac.getMoneda() + ":");
							txtFaltante.setValue(dFaltante);
							dwDevolucion.setWindowState("normal");
							
							//&& ================ validaciones para donaciones 
							
							boolean valido = false;
							valido = ( hFac.getFechajulian() == hFacOriginal.getFechajulian() ) ;
							
							valido = valido &&  
									 ( new BigDecimal(Double.toString( hFac.getTotal() )).subtract(
											new BigDecimal(Double.toString( hFacOriginal.getTotal() )) )
											.abs().compareTo(BigDecimal.ONE) == -1 );
							
							//&& ============== donaciones asociadas a la caja.
							String strSqlDnc = "select *  from "+PropertiesSystem.ESQUEMA+".dnc_donacion " +
								"where clientecodigo = " + hFacOriginal.getCodcli()+
								" and caid = " + recfac.getId().getCaid() +
								" and trim(codcomp) = '"+recfac.getId().getCodcomp().trim()+"' " +
								" and numrec =  " + recfac.getId().getNumrec() +
								" and estado = 1 ";
							
							lstDetalleDonaciones = (ArrayList<DncDonacion>) ConsolidadoDepositosBcoCtrl
									.executeSqlQuery(strSqlDnc, true, DncDonacion.class );
							
							boolean haydonacion = lstDetalleDonaciones != null && !lstDetalleDonaciones.isEmpty() && valido ;
							
							//&& === Si la devolucion es con socket pos, mandar anular la donacion o con algo que no sea efectivo
							for (DncDonacion dnc : lstDetalleDonaciones) {
								if( dnc.getSocketpos() == 0 || dnc.getFormadepago().compareTo( MetodosPagoCtrl.EFECTIVO) != 0 ){
									haydonacion = true;
									break;
								}
							}
							
							if(haydonacion) {
								
								for (DncDonacion dnc : lstDetalleDonaciones) {
									dnc.setFormadepago( com.casapellas.controles.MetodosPagoCtrl.descripcionMetodoPago( dnc.getFormadepago().trim() ) );
								}
								
								CodeUtil.putInSessionMap("fdc_lstDetalleDonaciones", lstDetalleDonaciones);
								
								pnlSeccionDonaciones.setRendered(haydonacion) ;
								gvDetalleDonaciones.dataBind() ;
								
								strSqlDnc = "select * from "+PropertiesSystem.ESQUEMA
										+".DNC_DONACION_JDE where iddonacion = " 
										+ lstDetalleDonaciones.get(0).getIddncrsm() +" and estado = 1  AND trim(tiporec) = '"
										+ recfac.getId().getTiporec().trim()+"' and clientecodigo = " + hFacOriginal.getCodcli() ;
								
								List<DncDonacionJde> nobatchs =	(ArrayList<DncDonacionJde>) ConsolidadoDepositosBcoCtrl
										.executeSqlQuery(strSqlDnc, true, DncDonacionJde.class );
								
								CodeUtil.putInSessionMap("fdc_dncDonacionJDE_devolver", nobatchs);

							}	
						}
					}
				} else {
					// La factura correspondiente a esta devolucion no se ha
					// procesado
					
					int i = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(lstConfiguracionMensaje, "CAJA", "REC_CONTADO", "MOSTRARRECIBO", 
							"CARGARDEVOL_03");
					
					if(i<0)
						sMensaje = sMensaje
							+ " La factura correspondiente a esta devolucion no ha sido pagada  "
							+ hFac.getTipodoco() + " " + hFac.getNodoco();
					else
						sMensaje = sMensaje
						+ " " + lstConfiguracionMensaje[i].getDescripcionMensaje()
												.replace("@TipoDocumento", hFac.getTipodoco())
												.replace("@NumeroDocumento", String.valueOf(hFac.getNodoco())) + " ";
					
					
					lblValidaFactura.setValue(sMensaje);
					throw new Exception(sMensaje);
				}
			} else {
				// La factura correspondiente a esta devolucion No se encontró
				
				int i = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(lstConfiguracionMensaje, "CAJA", "REC_CONTADO", "MOSTRARRECIBO", 
						"CARGARDEVOL_02");
				
				if(i<0)
					sMensaje = sMensaje
					+ " No se encontró la factura original para esta devolución:  "
					+ hFac.getTipodoco() + " " + hFac.getNodoco();
				else
					sMensaje = sMensaje
					+ " " + lstConfiguracionMensaje[i].getDescripcionMensaje()
											.replace("@TipoDocumento", hFac.getTipodoco())
											.replace("@NumeroDocumento", String.valueOf(hFac.getNodoco())) + "  ";
				
				lblValidaFactura.setValue(sMensaje);
				throw new Exception(sMensaje);
			}
		} catch (Exception ex) {
			LogCajaService.CreateLog("cargarDevolucion", "ERR", ex.getMessage());
			ex.printStackTrace();
			
			String msgProceso = "";
			
			int i = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(lstConfiguracionMensaje, "CAJA", "REC_CONTADO", 
					"MOSTRARRECIBO", "CARGARDEVOL_EX");
			
			if(i<0)
			{
				msgProceso = ex.getMessage();
			}
			else
			{
				msgProceso = lstConfiguracionMensaje[i].getDescripcionMensaje()
						.replace("@Error", ex.getMessage());
			}
			
			lblValidaFactura.setValue(msgProceso);
			throw new Exception(msgProceso);
		} finally {
			HibernateUtilPruebaCn.closeSession();
			LogCajaService.CreateLog("cargarDevolucion", "INFO", "cargarDevolucion-FIN");
		}
	}
/*********************************************************************************************************** */
	public void cerrarDevolucion(ActionEvent ev) {
		dwCancelarDev.setWindowState("normal");
	}

	public void aceptarCerrarDevolucion(ActionEvent ev) {
		SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
		try {
			dwCancelarDev.setWindowState("hidden");
			dwDevolucion.setWindowState("hidden");
			gvHfacturasContado.dataBind();
			List lstSelectedFacs = new ArrayList();
			m.put("facturasSelected", lstSelectedFacs);
			m.remove("facturasSelected");
			srm.addSmartRefreshId(gvHfacturasContado.getClientId(FacesContext.getCurrentInstance()));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void cancelarCerrarDevolucion(ActionEvent ev) {
		dwCancelarDev.setWindowState("hidden");
	}

	/** ********************************************************************************************************** */
	/** ******************************************************************************************************************************* */
	public void setTipoReciboDevolucion(ValueChangeEvent ev) {
		try {
			String sTipo = cmbTiporeciboDev.getValue().toString();
			txtNumRec.setValue("");
			txtNumeroReciboManulDev.setValue("");
			if (sTipo.equals("MANUAL")) {
				fechaReciboDev.setStyle("display:none");
				txtFechaManualDev.setStyle("display:inline");
				txtFechaManualDev.setValue(new Date());
				lblNumeroReciboManualDev.setStyle("display:inline");
				txtNumeroReciboManulDev.setStyle("display:inline");
			} else {
				fechaReciboDev.setStyle("display:inline");
				fechaReciboDev.setValue(new Date());
				txtFechaManualDev.setStyle("display:none");
				lblNumeroReciboManualDev.setStyle("display:none");
				txtNumeroReciboManulDev.setStyle("display:none");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/** ******************************************************************************************************************************* */
	public List formatDetalle(Hfactura hFac) {
		FacturaContadoCtrl facContCtrl = new FacturaContadoCtrl();
		List lstDetalle = null;
		Dfactjdecon[] dFacJde = null;
		Dfactura dFac = null;
		Divisas divisas = new Divisas();
		BigDecimal tasa = BigDecimal.ZERO;
		double total = 0;
		double ivaTotal = 0;
		double ivaActual = 0;
		double totalfac = 0;
		double equiv = 0;
		String sIvaTotal = "", sTotal = "";

		try {
			lstDetalle = facContCtrl.buscarDetalleFactura(hFac.getNofactura(),hFac.getTipofactura(), hFac.getCodsuc(), hFac.getCodunineg());
			dFacJde = new Dfactjdecon[lstDetalle.size()];
			for (int j = 0; j < lstDetalle.size(); j++) {
				dFacJde[j] = (Dfactjdecon) lstDetalle.get(j);

				double precioUnit = (dFacJde[j].getId().getPreciounit()) / 100.0;
				// String sPrecionUnit = divisas.formatDouble(precioUnit);
				tasa = hFac.getTasa();
				double factor = dFacJde[j].getId().getFactor() / 1000.0;
				dFac = new Dfactura(dFacJde[j].getId().getNofactura(),dFacJde[j].getId().getTipofactura(), dFacJde[j].getId().getCoditem(),
						dFacJde[j].getId().getDescitem(), precioUnit,
						dFacJde[j].getId().getCant(), dFacJde[j].getId().getImpuesto(), factor, hFac.getMoneda(), tasa);
				lstDetalle.remove(j);
				lstDetalle.add(j, dFac);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return lstDetalle;
	}

	/***************************************************************************
	 * DETALLE DE FACTURA DE CONTADO***************
	 * 
	 **************************************************************************/
	public void mostrarDetalleContado(ActionEvent e) {
		Hfactura hFac = new Hfactura(), hFacClicked = null;
		String sMonedaBase = "";
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		try {
			dgwDetalleContado.setWindowState("normal");
			RowItem ri = (RowItem) e.getComponent().getParent().getParent();
			hFacClicked = (Hfactura) gvHfacturasContado.getDataRow(ri);
			
			//obtener companias x caja
			sMonedaBase = cCtrl.sacarMonedaBase((F55ca014[])m.get("cont_f55ca014"), hFacClicked.getCodcomp());

			List lstFacsActuales = (List) m.get("lstHfacturasContado");

			Divisas divisas = new Divisas();

			for (int i = 0; i < lstFacsActuales.size(); i++) {
				hFac = (Hfactura) lstFacsActuales.get(i);
				if (hFac.getNofactura() == hFacClicked.getNofactura()&& hFac.getTipofactura().equals(hFacClicked.getTipofactura())&& hFac.getCodsuc().equals(hFacClicked.getCodsuc())
						&& hFac.getCodunineg().equals(hFacClicked.getCodunineg())) {
					// poner valor a labels del detalle
					txtNofactura = hFacClicked.getNofactura() + "";
					txtFechaFactura = hFac.getFecha();
					txtCliente = hFac.getNomcli() + " (Nombre)";
					txtCodigoCliente = hFac.getCodcli() + " (Código)";
					txtCodUnineg = hFac.getCodunineg();
					txtUnineg = hFac.getUnineg();
					txtTasaDetalle = hFac.getTasa() + "";

					
					if (hFac.getCodVendedor() > 0) {
						EmpleadoCtrl emp = new EmpleadoCtrl();
						lblVendedorCont = "Vendedor:";
						txtVendedorCont = emp.buscarEmpleadoxCodigo(hFac.getCodVendedor());
					} else {
						lblVendedorCont = "Facturador:";
						txtVendedorCont = hFac.getHechopor();
					}

					// actualizar lista de detalle buscar detalle

					lstDfacturasContado = formatDetalle(hFac);
					hFac.setDfactura(lstDfacturasContado);
					
					if(sMonedaBase.equals(hFac.getMoneda())){
						txtSubtotal = hFac.getSubtotal();
						txtIva = hFac.getTotal() - hFac.getSubtotal();
						txtTotal = divisas.formatDouble(hFac.getTotal());
					}else{
						txtSubtotal = hFac.getSubtotalf();
						txtIva = hFac.getTotalf() - hFac.getSubtotalf();
						txtTotal = divisas.formatDouble(hFac.getTotalf());
					}
					
					m.put("lstDfacturasContado", lstDfacturasContado);
					// poner monedas

					lstMonedasDetalle = new ArrayList();
					String moneda = hFac.getMoneda();
					if (moneda.equals(sMonedaBase)) {
						lstMonedasDetalle.add(new SelectItem(sMonedaBase, sMonedaBase));
						txtTasaDetalle = "";
						lblTasaDetalle = "";

					} else {
						lstMonedasDetalle.add(new SelectItem(moneda, moneda));
						lstMonedasDetalle.add(new SelectItem(sMonedaBase, sMonedaBase));
						lblTasaDetalle = "Tasa de Cambio: ";
					}
					m.put("Hfactura", hFac);
					List oldDfac = hFac.getDfactura();
					m.put("oldDfac", oldDfac);
					m.put("lstMonedasDetalle", lstMonedasDetalle);
					cmbMonedaDetalle.dataBind();
					gvDfacturasContado.dataBind();
					break;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/** *************************************************************************************** */
	public void cerrarDetalleContado(ActionEvent e) {
		Divisas divisas = new Divisas();
		Dfactura dFac = null;
		String sMonedaBase = "";
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		try {
			String monedaActual = cmbMonedaDetalle.getValue().toString();
			Hfactura hFac = (Hfactura) m.get("Hfactura");
			//obtener companias x caja
			sMonedaBase = cCtrl.sacarMonedaBase((F55ca014[])m.get("cont_f55ca014"), hFac.getCodcomp());
			List oldDet = hFac.getDfactura(), newDet = new ArrayList();
			int cant = oldDet.size();
			if (monedaActual.equals(sMonedaBase) && !hFac.getMoneda().equals(sMonedaBase)) {
				for (int i = 0; i < cant; i++) {
					dFac = (Dfactura) oldDet.get(i);
					dFac.setPreciounit(dFac.getPreciounit() / dFac.getTasa().doubleValue());
					newDet.add(dFac);
				}
				m.put("lstDfacturasContado", newDet);
				hFac.setDfactura(newDet);
			}
			m.put("Hfactura", hFac);
			gvDfacturasContado.dataBind();
			dgwDetalleContado.setWindowState("hidden");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

/****************DETALLE DE FACTURA DE CONTADO en el popup del recibo*****************/
	public void mostrarDetalleEnReciboContado(ActionEvent e) {
		Hfactura hFacClicked = null;
		String sMonedaBase = "";
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		try {
			dgwDetalleContado.setWindowState("normal");
			RowItem ri = (RowItem) e.getComponent().getParent().getParent();
			hFacClicked = (Hfactura) gvHfacturasContado.getDataRow(ri);

			List lstFacsActuales = (List) m.get("facturasSelected");
			Hfactura hFac = new Hfactura();
			Divisas divisas = new Divisas();			
			//obtener companias x caja
			sMonedaBase = cCtrl.sacarMonedaBase((F55ca014[])m.get("cont_f55ca014"), hFacClicked.getCodcomp());
			
			for (int i = 0; i < lstFacsActuales.size(); i++) {
				hFac = (Hfactura) lstFacsActuales.get(i);
				if (hFac.getNofactura() == hFacClicked.getNofactura()&& hFac.getTipofactura().equals(
					hFacClicked.getTipofactura())&& hFac.getCodsuc().equals(hFacClicked.getCodsuc())&& hFac.getCodunineg().equals(hFacClicked.getCodunineg())) {
					// poner valor a labels del detalle
					txtNofactura = hFac.getNofactura() + "";
					txtFechaFactura = hFac.getFecha();
					txtCliente = hFac.getNomcli() + " (Nombre)";
					txtCodigoCliente = hFac.getCodcli() + " (Código)";
					txtUnineg = hFac.getCodunineg() + " " + hFac.getUnineg();

					txtTasaDetalle = hFac.getTasa() + "";

					if (hFac.getCodVendedor() > 0) {
						EmpleadoCtrl emp = new EmpleadoCtrl();
						lblVendedorCont = "Vendedor:";
						txtVendedorCont = emp.buscarEmpleadoxCodigo(hFac.getCodVendedor());
					} else {
						lblVendedorCont = "Facturador:";
						txtVendedorCont = hFac.getHechopor();
					}
					
					// actualizar lista de detalle buscar detalle
					lstDfacturasContado = formatDetalle(hFac);
					hFac.setDfactura(lstDfacturasContado);


					m.put("lstDfacturasContado", lstDfacturasContado);
					// poner monedas

					lstMonedasDetalle = new ArrayList();
					String moneda = hFac.getMoneda();
					if (moneda.equals(sMonedaBase)) {
						lstMonedasDetalle.add(new SelectItem(sMonedaBase, sMonedaBase));
						txtTasaDetalle = "";
						lblTasaDetalle = "";
						
						txtSubtotal = hFac.getSubtotal();
						txtTotal = divisas.formatDouble(hFac.getTotal());
						txtIva = hFac.getTotal() - hFac.getSubtotal();
					} else {
						lstMonedasDetalle.add(new SelectItem(moneda, moneda));
						lstMonedasDetalle.add(new SelectItem(sMonedaBase, sMonedaBase));
						lblTasaDetalle = "Tasa de Cambio: ";
						
						txtSubtotal = hFac.getSubtotalf();
						txtTotal = divisas.formatDouble(hFac.getTotalf());
						txtIva = hFac.getTotalf() - hFac.getSubtotalf();
					}
				
					m.put("Hfactura", hFac);
					List oldDfac = hFac.getDfactura();
					m.put("oldDfac", oldDfac);
					m.put("lstMonedasDetalle", lstMonedasDetalle);
					cmbMonedaDetalle.dataBind();
					gvDfacturasContado.dataBind();
					break;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
/******************************************************************************************************** */
	public void cerrarDetalleEnReciboContado(ActionEvent e) {
		Divisas divisas = new Divisas();
		Dfactura dFac = null;
		String sMonedaBase = "";
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		try {
			String monedaActual = cmbMonedaDetalleReciboContado.getValue().toString();
			Hfactura hFac = (Hfactura) m.get("Hfactura");
			//obtener companias x caja
			sMonedaBase = cCtrl.sacarMonedaBase((F55ca014[])m.get("cont_f55ca014"), hFac.getCodcomp());
			List oldDet = hFac.getDfactura(), newDet = new ArrayList();
			int cant = oldDet.size();
			if (monedaActual.equals(sMonedaBase) && !hFac.getMoneda().equals(sMonedaBase)) {
				for (int i = 0; i < cant; i++) {
					dFac = (Dfactura) oldDet.get(i);
					dFac.setPreciounit(dFac.getPreciounit() / dFac.getTasa().doubleValue());
					newDet.add(dFac);
				}
				m.put("lstDfacturasContado", newDet);
				hFac.setDfactura(newDet);
			}
			m.put("Hfactura", hFac);
			gvDfacturasContadoRecibo.dataBind();
			dgwDetalleEnReciboContado.setWindowState("hidden");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/** ********************************************************************************** */
/******************QUITAR FACTURA DE DETALLE DE RECIBO DE CONTADO**************************/
	public void quitarFacturaContado(ActionEvent e) {
		Divisas divisas = new Divisas();
		List lstDcontadoFacs = (List) m.get("facturasSelected");
		List lstNewDFacs = new ArrayList();
		
		String sMonedaBase = "";
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		try {
			double montoAplicar = 0;
			if (lstDcontadoFacs.size() > 1) {
				RowItem ri = (RowItem) e.getComponent().getParent().getParent();
				List lstA = (List) ri.getCells();
				// Columna a obtener: No. Factura
				Cell c = (Cell) lstA.get(2);
				HtmlOutputText noFactura = (HtmlOutputText) c.getChildren().get(0);
				int iNoFactura = Integer.parseInt(noFactura.getValue().toString());

				Cell c3 = (Cell) lstA.get(3);// Columna a obtener: Tipo Factura
				HtmlOutputText tipofactura = (HtmlOutputText) c3.getChildren().get(0);
				String sTipoFactura = tipofactura.getValue().toString();

				// Columna a obtener:Monto
				Cell c2 = (Cell) lstA.get(5);
				HtmlOutputText monto = (HtmlOutputText) c2.getChildren().get(0);
				String sMonto = monto.getValue().toString();
				Hfactura hFac = null;
				for (int i = 0; i < lstDcontadoFacs.size(); i++) {
					hFac = (Hfactura) lstDcontadoFacs.get(i);
					if (hFac.getNofactura() == iNoFactura && hFac.getTipofactura().trim().equals(sTipoFactura.trim()) && (String.valueOf(hFac.getTotal())).equals(sMonto.trim())) {
						// no las incluye en la lista
					} else {
						lstNewDFacs.add(hFac);
						montoAplicar = montoAplicar + hFac.getTotal();
					}
				}
				// lblMontoAplicar.setValue(divisas.formatDouble(montoAplicar));
				txtMontoAplicar.setValue(divisas.formatDouble(montoAplicar));
				m.put("mTotalFactura", divisas.formatDouble(montoAplicar));
				m.put("facturasSelected", lstNewDFacs);
				gvfacturasSelec.dataBind();
				
				//obtener companias x caja
				sMonedaBase = cCtrl.sacarMonedaBase((F55ca014[])m.get("cont_f55ca014"), hFac.getCodcomp());
				List selectedMet = (List) m.get("lstPagos");
				// --------------------------------------------------
				// calcular el monto recibido en dependencia de moneda de
				// factura y moneda de pago
				double montoRecibido = calcularElMontoRecibido(selectedMet,hFac,sMonedaBase);

				// determinar como dar el cambio
				determinarCambio(selectedMet, hFac, montoRecibido, sMonedaBase);
			} else {
				dgwMensajeDetalleContado.setWindowState("normal");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void cerrarMensajeDetallefactura(ActionEvent e) {
		dgwMensajeDetalleContado.setWindowState("hidden");
	}

	/** ********************************************************************************* */
	/** **************LISTAR FACTURAS DE CONTADO DEL DIA**************** */
	
	public void listarFacturasContadoDelDia() {
		
		SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Calendar calFechaActual = null;
		List lstFacs = new ArrayList();
		F55ca017[] f55ca017 = null, f55ca017i = null;
		CtrlCajas cajasCtrl = new CtrlCajas();
		int iCaId = 0;
		String sCodComp = "";
		String[] sLineas = null, sTiposDoc;
		boolean passed = true;
		// txtMensaje.setValue("");
		try {
			List lstCajas = (List)m.get("lstCajas");
			Vf55ca01 f55ca01 = ((Vf55ca01)lstCajas.get(0));
			sTiposDoc = (String[]) m.get("sTiposDoc");
			f55ca017 = (F55ca017[]) m.get("f55ca017");
			// obtener fecha actual y convertirla a formato juliano
			calFechaActual = Calendar.getInstance();
			String sFechaActual = calFechaActual.get(Calendar.DATE) + "/"
					+ (calFechaActual.get(Calendar.MONTH) + 1) + "/"
					+ calFechaActual.get(Calendar.YEAR);
			calFechaActual.setTime(new Date(sdf.parse(sFechaActual).getTime()));
			CalendarToJulian julian = new CalendarToJulian(calFechaActual);
			int iFechaActual = julian.getDate();
			JulianToCalendar cal = new JulianToCalendar(iFechaActual);
			Calendar dtPrueba = cal.getDate();

			//---- Llamada a método que incluye manejo de traslados en la consulta.
			
			lstFacs = cajasCtrl.leerFacturaDelDia(iFechaActual, sTiposDoc,f55ca017,f55ca01.getId().getCaid());
//			cajasCtrl.leerFacturaDelDia1(iFechaActual, sTiposDoc,f55ca017, lstLocalizaciones,f55ca01.getId().getCaid());
			
			if (lstFacs != null && !lstFacs.isEmpty()) {
				lstHfacturasContado = formatFactura(lstFacs);
				m.put("lstHfacturasContado", lstHfacturasContado);
				if (m.get("mostrar") != null) {
					m.remove("mostrar");
				}
			} else {
				// cambiar mensaje y quitar visivilidad de grid
				sMensaje = "No se han realizado facturas para el dia";
				m.put("sMensaje", sMensaje);
				getSMensaje();
				m.put("mostrar", "m");
				m.put("lstHfacturasContado", lstFacs);
			}
		} catch (Exception ex) {
			LogCajaService.CreateLog("listarFacturasContadoDelDia", "ERR", ex.getMessage());
		}
	}

/****************BUSCAR FACTURAS DE CONTADO DEL DIA POR PARAMETROS*****************/
	public void BuscarFacturasContado(ActionEvent e) {
 
		try { 		
					
			LogCajaService.CreateLog("BuscarFacturasContado", "INFO", "INICIO METODO => BuscarFacturasContado");
			
		String strParametro = null;
		int iFechaActual = 0;
		FacturaContadoCtrl facContCtrl = new FacturaContadoCtrl();
		String sMoneda = "";
		String sRangoFacs = "";
		SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
		FechasUtil fecUtil = new FechasUtil();
		
			List lstCajas = (List) m.get("lstCajas");
			Vf55ca01 f55ca01 = ((Vf55ca01) lstCajas.get(0));
			String sCodSuc = f55ca01.getId().getCaco().substring(3, 5);
			String sMensaje2 = "";
			String[] sTiposDoc = (String[]) m.get("sTiposDoc");
			F55ca017[] f55ca017 = (F55ca017[]) m.get("f55ca017");
			// obtener fecha actual del mapa
			iFechaActual = fecUtil.obtenerFechaActualJuliana();
			if (txtParametro.getValue() != null) {
				strParametro = txtParametro.getValue().toString().trim();
				sMoneda = cmbFiltroMonedas.getValue().toString();
				sRangoFacs = cmbFiltroFacturas.getValue().toString();
				int busqueda = 1;
				if (m.get("strBusqueda") != null) {
					busqueda = Integer.parseInt((String) m.get("strBusqueda"));
				}
				List result = new ArrayList();
				List lstLocalizaciones = (List) m.get("lstLocalizaciones");
				switch (busqueda) {
				case (1):
					// Busqueda por nombre de cliente
					result = facContCtrl.buscarFactura(strParametro,
							iFechaActual, sTiposDoc, sMoneda, sRangoFacs,
							busqueda, f55ca017, lstLocalizaciones,f55ca01.getId().getCaid());
					if (result == null || result.isEmpty()) {
						sMensaje2 = "No se encontraron resultados";
						getSMensaje();
						m.put("mostrar", "m");
						m.put("lstHfacturasContado", result);
					} else {
						if (m.get("mostrar") != null) {
							m.remove("mostrar");
						}
						lstHfacturasContado = formatFactura(result);
						m.put("lstHfacturasContado", lstHfacturasContado);
					}
					break;
				case (2):
					// Busqueda por codigo de cliente
					result = facContCtrl.buscarFactura(strParametro,
							iFechaActual, sTiposDoc, sMoneda, sRangoFacs,
							busqueda, f55ca017, lstLocalizaciones,f55ca01.getId().getCaid());
					if (result == null || result.isEmpty()) {
						sMensaje2 = "No se encontraron resultados";
						getSMensaje();
						m.put("mostrar", "m");
						m.put("lstHfacturasContado", result);
					} else {
						if (m.get("mostrar") != null) {
							m.remove("mostrar");
						}
						lstHfacturasContado = formatFactura(result);
						m.put("lstHfacturasContado", lstHfacturasContado);
					}
					break;
				case (3):
					// Busqueda por numero de factura
					result = facContCtrl.buscarFactura(strParametro,
							iFechaActual, sTiposDoc, sMoneda, sRangoFacs,
							busqueda, f55ca017, lstLocalizaciones,f55ca01.getId().getCaid());
					if (result == null || result.isEmpty()) {
						sMensaje2 = "No se encontraron resultados";
						getSMensaje();
						m.put("mostrar", "m");
						m.put("lstHfacturasContado", result);
					} else {
						if (m.get("mostrar") != null) {
							m.remove("mostrar");
						}
						lstHfacturasContado = formatFacturaHfactjdeCon(result);
						m.put("lstHfacturasContado", lstHfacturasContado);
					}
					break;
				}
				gvHfacturasContado.setPageIndex(0);
				gvHfacturasContado.dataBind();
				txtMensaje.setValue(sMensaje2);
				m.put("sMensaje", sMensaje2);

			}
			lblResultadoRec.setValue("");
			m.remove("facturasSelected");
			srm.addSmartRefreshId(lblResultadoRec.getClientId(FacesContext.getCurrentInstance()));
			
			LogCajaService.CreateLog("BuscarFacturasContado", "INFO", "FIN METODO => BuscarFacturasContado");
		} catch (Exception ex) {
			ex.printStackTrace();
		}finally{
			dwCargando.setWindowState("hidden");
		}
	}

/***********************VALUECHANGELISTENER DE CAMBIO DE MONEDA EN EL FILTRO DE MONEDA**************************/
	public void onFiltrosChange(ValueChangeEvent ev) {
		String strParametro = null;
		int iFechaActual = 0;
		FacturaContadoCtrl facContCtrl = new FacturaContadoCtrl();
		FacturaCrtl facCtrl = new FacturaCrtl();
		String sMoneda = "";
		String sRangoFacs = "";
		try {
			String sMensaje2 = "";
			String[] sTiposDoc = (String[]) m.get("sTiposDoc");
			F55ca017[] f55ca017 = (F55ca017[]) m.get("f55ca017");
			// obtener fecha actual del mapa
			iFechaActual = facCtrl.obtenerFechaActualJuliana();
			if (txtParametro.getValue() != null) {
				strParametro = txtParametro.getValue().toString().trim();
				sMoneda = cmbFiltroMonedas.getValue().toString();
				sRangoFacs = cmbFiltroFacturas.getValue().toString();
				int busqueda = 1;
				if (m.get("strBusqueda") != null) {
					busqueda = Integer.parseInt((String) m.get("strBusqueda"));
				}
				List result = new ArrayList();
				lstHfacturasContado = new ArrayList();
				List lstCajas = (List) m.get("lstCajas");
				Vf55ca01 f55ca01 = ((Vf55ca01) lstCajas.get(0));
				String sCodSuc = f55ca01.getId().getCaco().substring(3, 5);
				List lstLocalizaciones = (List) m.get("lstLocalizaciones");
				switch (busqueda) {
				case (1):
					// Busqueda por nombre de cliente
					result = facContCtrl.buscarFactura(strParametro,iFechaActual, sTiposDoc, sMoneda, sRangoFacs,busqueda, f55ca017, lstLocalizaciones,f55ca01.getId().getCaid());
					if (result == null || result.isEmpty()) {
						sMensaje2 = "No se encontraron resultados";
						getSMensaje();
						m.put("mostrar", "m");
						m.put("lstHfacturasContado", result);
					} else {
						if (m.get("mostrar") != null) {
							m.remove("mostrar");
						}
						lstHfacturasContado = formatFactura(result);
						m.put("lstHfacturasContado", lstHfacturasContado);
					}
					break;
				case (2):
					// Busqueda por codigo de cliente
					result = facContCtrl.buscarFactura(strParametro,iFechaActual, sTiposDoc, sMoneda, sRangoFacs,busqueda, f55ca017, lstLocalizaciones,f55ca01.getId().getCaid());
					if (result == null || result.isEmpty()) {
						sMensaje2 = "No se encontraron resultados";
						getSMensaje();
						m.put("mostrar", "m");
						m.put("lstHfacturasContado", result);
					} else {
						if (m.get("mostrar") != null) {
							m.remove("mostrar");
						}
						lstHfacturasContado = formatFactura(result);
						m.put("lstHfacturasContado", lstHfacturasContado);
					}
					break;
				case (3):
					// Busqueda por numero de factura
					result = facContCtrl.buscarFactura(strParametro,
							iFechaActual, sTiposDoc, sMoneda, sRangoFacs,
							busqueda, f55ca017, lstLocalizaciones,f55ca01.getId().getCaid());
					if (result == null || result.isEmpty()) {
						sMensaje2 = "No se encontraron resultados";
						getSMensaje();
						m.put("mostrar", "m");
						m.put("lstHfacturasContado", result);
					} else {
						if (m.get("mostrar") != null) {
							m.remove("mostrar");
						}
						lstHfacturasContado = formatFactura(result);
						m.put("lstHfacturasContado", lstHfacturasContado);
					}
					break;
				}
			}
			gvHfacturasContado.setPageIndex(0);
			gvHfacturasContado.dataBind();
			txtMensaje.setValue(sMensaje2);
			m.put("sMensaje", sMensaje2);
			m.remove("facturasSelected");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

/***************************SET BUSQUEDA**************************************************************************************************/
	public void setBusqueda(ValueChangeEvent e) {
		String strBusqueda = cmbBusqueda.getValue().toString();
		m.put("strBusqueda", strBusqueda);
	}

/***************************FORMATEAR CAMPOS PARA VISUALIZACION:: recibe una lista de Hfactjdecon y retorna una lista de Hfactura************************/
	public static ArrayList formatFactura(List lstFacturas) {
		Divisas d = new Divisas();
		ArrayList lstResultado = new ArrayList();
		A02factco[] hFacJde = new A02factco[lstFacturas.size()];
		Hfactjdecon[] hFacJdeTmp = new Hfactjdecon[lstFacturas.size()];
		Hfactura hFac = null;
		List result = new ArrayList();
		double total = 0, ivaTotal = 0, ivaActual = 0, totalfac = 0, equiv = 0, equiv2 = 0, totalf = 0;
		String sHora = "";
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		String sMonedaBase = "";

		try {
			hFacJde[0] = (A02factco) lstFacturas.get(0);
			
			sMonedaBase = cCtrl.sacarMonedaBase((F55ca014[]) FacesContext
					.getCurrentInstance().getExternalContext().getSessionMap()
					.get("cont_f55ca014"), hFacJde[0].getCodcomp());
			
			//=============== TasaParale  ================
			//===============  lfonseca   ================
			
			TasaCambioCtrl tcCtrl = new TasaCambioCtrl();
			Tpararela[] tpcambio = tcCtrl.obtenerTasaCambioParalela(new Date());
			BigDecimal bdTasaParalela = tpcambio[0].getId().getTcambiom();
			
			//===============  Termina TP ================
			
			
			//&& ============ Tasa de cambio oficial.
			BigDecimal tasafac;
			BigDecimal tasa = BigDecimal.ONE;
			Tcambio[] tcJDE = new TasaCambioCtrl().obtenerTasaCambioJDEdelDia();
			
			if(tcJDE != null){
				for (Tcambio t : tcJDE) {
					if (t.getId().getCxcrcd().compareTo(sMonedaBase) == 0) {
						tasa = t.getId().getCxcrrd();
						break;
					}
				}
			}
			for (int i = 0; i < lstFacturas.size(); i++) {
				hFacJde[i] = (A02factco) lstFacturas.get(i);
				
				JulianToCalendar fecha = new JulianToCalendar(hFacJde[i].getId().getFecha());
				JulianToCalendar fechagrab = new JulianToCalendar(hFacJde[i].getFechagrab());
				double subtotal = (hFacJde[i].getSubtotal().doubleValue()) / 100.0;
				
				
				String sSubtotal = d.formatDouble(subtotal);
				String sTotal = null;
				String sIvaTotal = null;
				
				// ****************Anexar Detalle a facturas y formatear*************************/
				
				total = d.roundDouble4((hFacJde[i] .getTotal()) / 100.0);
				totalf = d.roundDouble4((hFacJde[i] .getTotalf()) / 100.0);
				ivaTotal = d.roundDouble4((hFacJde[i] .getTotal() - hFacJde[i] .getSubtotal()) / 100.0);
				
				double dTotal = total;
				sIvaTotal = d.formatDouble(ivaTotal);
				
				String moneda = hFacJde[i].getMoneda();
				
				if (moneda.equals(sMonedaBase)) {
					
					equiv = total;
					dTotal = totalf;
					totalf = hFacJde[i].getTotalf();
					tasafac = hFacJde[i].getTasa();
					
					if(tasafac.compareTo(BigDecimal.ZERO) == 0|| 
						tasafac.compareTo(BigDecimal.ONE) == 0)
						tasafac = tasa;
					
					if(dTotal == 0){
						String lineaDeFactura = CompaniaCtrl.leerLineaNegocio( hFacJde[i].getId().getCodunineg().trim() );
						if(PropertiesSystem.LNS_TALLER.contains( lineaDeFactura.trim() ) || PropertiesSystem.LNS_REPUESTOS.contains( lineaDeFactura.trim() ))
						{
							dTotal = d.roundDouble(total / bdTasaParalela.doubleValue());
							totalf = dTotal;
						}
						else
						{
							dTotal = d.roundDouble(total / tasafac.doubleValue());
							totalf = dTotal;
						}
					}
					
//					equiv = 0;
				} else {
					equiv = total;
					equiv2 =  (new BigDecimal(totalf).multiply(bdTasaParalela)).doubleValue(); //ajuste hecho por lfonseca
				}
				
				totalfac += total;
				FacesContext.getCurrentInstance().getExternalContext()
						.getSessionMap().put("mTotalFactura", totalfac);
				
				sHora = new Divisas().rellenarCadena(hFacJde[i].getHora().toString(), "0", 6);
				
				int iLargoHora = sHora.length();
				if (iLargoHora == 6) {
					sHora = sHora.substring(0, 2) + ":" + sHora.substring(2, 4)	+ ":" + sHora.substring(4, 6);
				} else {
					sHora = "0" + sHora.substring(0, 1) + ":" + sHora.substring(1, 3) + ":" + sHora.substring(3, 5);
				}
				
				String sPago = "";
				if (hFacJde[i].getTipopago().trim().equals("00") || hFacJde[i].getTipopago().trim().equals("01") || hFacJde[i].getTipopago().trim().equals("001")) {
					sPago = "Contado";
				} else {
					sPago = "Crédito";
				}
				
				
				
				String codsuc = "";
				try {
					codsuc = String.valueOf( Integer.parseInt(hFacJde[i].getId().getCodsuc().trim() )  ); 
				}catch(Exception e) {
					codsuc = hFacJde[i].getId().getCodsuc().trim() ;
				}
				
				hFac = new Hfactura(hFacJde[i].getId().getNofactura(),
						hFacJde[i].getId().getTipofactura(), 
						hFacJde[i].getId().getCodcli(), 
						hFacJde[i].getNomclie(),
						hFacJde[i].getId().getCodunineg(), 
						hFacJde[i].getUnineg(),
						codsuc, 
						hFacJde[i].getNomsuc(), 
						hFacJde[i].getCodcomp(), 
						hFacJde[i].getNomcomp(),
						fecha.toString() + " " + sHora, 
						subtotal, 
						hFacJde[i].getMoneda(), 
						hFacJde[i].getTasa(), 
						hFacJde[i].getTipopago(),						
						equiv,//cor
						dTotal,//usd
						fechagrab.toString(),
						hFacJde[i].getHechopor(),
						hFacJde[i].getPantalla(), 
						ivaTotal,						
						total, 						
						(moneda.equals(sMonedaBase) ? d.formatDouble(equiv) : d.formatDouble(equiv2)),
						"", 
						null,
						hFacJde[i].getNodoco(),
						hFacJde[i].getTipodoco(), 
						hFacJde[i].getEstado(),
						sPago, 
						hFacJde[i].getCodvendor(),
						totalf,
						new BigDecimal(String.valueOf(total )), // && Conservar el total en cordobas de la factura.
						hFacJde[i].getId().getFecha(),
						hFacJde[i].getHora()
						);
					
				//&& ============== Fecha de factura  original a devolver.
				hFac.setFechadoco( hFacJde[i].getFechadoco() );
				
				if(hFacJde[i].getMoneda().equals(sMonedaBase)){//domestica
					hFac.setTotal(total);
					hFac.setCpendiente(total);
					hFac.setSubtotal(hFacJde[i].getSubtotal()/100.00);
				}else{
					hFac.setTotal(totalf);
					hFac.setCpendiente(total);
					hFac.setDpendiente(totalf);
					hFac.setSubtotal(hFacJde[i].getSubtotal()/100.00);
					hFac.setSubtotalf(hFacJde[i].getSubtotalf()/100.00);
				}
				
				lstResultado.add(hFac);
			}
		} catch (Exception ex) {
			ex.printStackTrace(); 
		}finally{
			d = null;
			hFacJde = null;
			hFacJdeTmp = null;
			hFac = null;
			result = null;
			sHora = null;
			cCtrl = null;
			sMonedaBase = null;
		}
		return lstResultado;
	}

	public ArrayList formatFacturaHfactjdeCon(List lstFacturas) {
		Divisas d = new Divisas();
		ArrayList lstResultado = new ArrayList();
		
		Hfactjdecon[] hFacJde = new Hfactjdecon[lstFacturas.size()];
 
		
		Hfactura hFac = null;
		List result = new ArrayList();
		double total = 0, ivaTotal = 0, ivaActual = 0, totalfac = 0, equiv = 0, equiv2 = 0, totalf = 0;
		String sHora = "";
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		String sMonedaBase = "";

		try {
			hFacJde[0] = (Hfactjdecon) lstFacturas.get(0);
			
			//obtener companias x caja
			sMonedaBase = cCtrl.sacarMonedaBase((F55ca014[])m.get("cont_f55ca014"),hFacJde[0].getId().getCodcomp());
			
			//&& ============ Tasa de cambio oficial.
			BigDecimal tasafac;
			BigDecimal tasa = BigDecimal.ONE;
			Tcambio[] tcJDE = new TasaCambioCtrl().obtenerTasaCambioJDEdelDia();
			
			if(tcJDE != null){
				for (Tcambio t : tcJDE) {
					if (t.getId().getCxcrcd().compareTo(sMonedaBase) == 0) {
						tasa = t.getId().getCxcrrd();
						break;
					}
				}
			}
			for (int i = 0; i < lstFacturas.size(); i++) {
				hFacJde[i] = (Hfactjdecon) lstFacturas.get(i);
				
				JulianToCalendar fecha = new JulianToCalendar(hFacJde[i].getId().getFecha());
				
				JulianToCalendar fechagrab = new JulianToCalendar(hFacJde[i].getId().getFechagrab());
				double subtotal = (hFacJde[i].getId().getSubtotal().doubleValue()) / 100.0;
				
				
				String sSubtotal = d.formatDouble(subtotal);
				String sTotal = null;
				String sIvaTotal = null;
				// ****************Anexar Detalle a facturas y formatear*************************/

				Dfactjdecon[] dFacJde = new Dfactjdecon[result.size()];
				Dfactura dFac = null;
				List lstDfactura = new ArrayList();
				total = d.roundDouble4((hFacJde[i].getId().getTotal()) / 100.0);
				totalf = d.roundDouble4((hFacJde[i].getId().getTotalf()) / 100.0);
				ivaTotal = d.roundDouble4((hFacJde[i].getId().getTotal() - hFacJde[i].getId().getSubtotal()) / 100.0);
				
				double dTotal = total;
				
				//
				sIvaTotal = d.formatDouble(ivaTotal);
				String moneda = hFacJde[i].getId().getMoneda();
				
				if (moneda.equals(sMonedaBase)) {
					
					equiv = total;
					//equiv2 = total;
					dTotal = totalf;
					totalf = hFacJde[i].getId().getTotalf();
					
					tasafac = hFacJde[i].getId().getTasa();
					
					if(tasafac.compareTo(BigDecimal.ZERO) == 0|| 
						tasafac.compareTo(BigDecimal.ONE) == 0)
						tasafac = tasa;
					
					if(dTotal == 0){
						dTotal = d.roundDouble(total / tasafac.doubleValue());
						totalf = dTotal;
					}
					
					
				} else {
					//=============== TasaParale  ================
					//===============  lfonseca   ================
					
					TasaCambioCtrl tcCtrl = new TasaCambioCtrl();
					Tpararela[] tpcambio = tcCtrl.obtenerTasaCambioParalela(new Date());
					BigDecimal bdTasaParalela = tpcambio[0].getId().getTcambiom();
					
					//===============  Termina TP ================
					
					equiv = total;
					
				}
				
				totalfac += total;
				m.put("mTotalFactura", totalfac);
				
				//------ formatear Hora
				sHora = new Divisas().rellenarCadena(hFacJde[i].getId().getHora().toString(), "0", 6);
				
				int iLargoHora = sHora.length();
				if (iLargoHora == 6) {
					sHora = sHora.substring(0, 2) + ":" + sHora.substring(2, 4)	+ ":" + sHora.substring(4, 6);
				} else {
					sHora = "0" + sHora.substring(0, 1) + ":" + sHora.substring(1, 3) + ":" + sHora.substring(3, 5);
				}
				
				// establecer tipo de factura
				String sPago = "";
				if (hFacJde[i].getId().getTipopago().trim().equals("00") || hFacJde[i].getId().getTipopago().trim().equals("01") || hFacJde[i].getId().getTipopago().trim().equals("001")) {
					sPago = "Contado";
				} else {
					sPago = "Crédito";
				}

				hFac = new Hfactura(hFacJde[i].getId().getNofactura(),
						hFacJde[i].getId().getTipofactura(), 
						hFacJde[i].getId().getCodcli(), 
						hFacJde[i].getId().getNomcli(),
						hFacJde[i].getId().getCodunineg(), 
						hFacJde[i].getId().getUnineg(), 
						hFacJde[i].getId().getCodsuc(),
						hFacJde[i].getId().getNomsuc(), 
						hFacJde[i].getId().getCodcomp(), 
						hFacJde[i].getId().getNomcomp(),
						fecha.toString() + " " + sHora, 
						subtotal, 
						hFacJde[i].getId().getMoneda(), 
						hFacJde[i].getId().getTasa(), 
						hFacJde[i].getId().getTipopago(),						
						equiv,//cor
						dTotal,//usd
						fechagrab.toString(),
						hFacJde[i].getId().getHechopor(),
						hFacJde[i].getId().getPantalla(), 
						ivaTotal,						
						total, 
						d.formatDouble(equiv),						
						"", 
						lstDfactura,
						hFacJde[i].getId().getNodoco(),
						hFacJde[i].getId().getTipodoco(), 
						hFacJde[i].getId().getEstado(),
						sPago, 
						hFacJde[i].getId().getCodvendor(),
						totalf,
						new BigDecimal(String.valueOf(total )), // && Conservar el total en cordobas de la factura.
						hFacJde[i].getId().getFecha(),
						hFacJde[i].getId().getHora()
						);
				
				if(hFacJde[i].getId().getMoneda().equals(sMonedaBase)){//domestica
					hFac.setTotal(total);
					hFac.setCpendiente(total);
					hFac.setSubtotal(hFacJde[i].getId().getSubtotal()/100.00);
				}else{
					hFac.setTotal(totalf);
					hFac.setCpendiente(total);
					hFac.setDpendiente(totalf);
					hFac.setSubtotal(hFacJde[i].getId().getSubtotal()/100.00);
					hFac.setSubtotalf(hFacJde[i].getId().getSubtotalf()/100.00);
				}
				
				lstResultado.add(hFac);
			}
		} catch (Exception ex) { 
			ex.printStackTrace(); 
			
		}
		return lstResultado;
	}
/***********************REFRESCAR FACTURAS DE CONTADO DEL DIA********************************/
	public void refrescarFacturasContado(ActionEvent e) {
		try {
			LogCajaService.CreateLog("refrescarFacturasContado", "INFO", "INICIO METODO => refrescarFacturasContado");
			
			m.remove("lstHfacturasContado");
			lstHfacturasContado = new ArrayList();
			F55ca017[] f55ca017 = (F55ca017[]) m.get("f55ca017");
			boolean bSincroniza = false;
			
			if(f55ca017 == null || f55ca017.length == 0){
				int iFechaHoy = new FechasUtil().DateToJulian(new Date());
				int caid = ((ArrayList<Vf55ca01>)m.get("lstCajas"))
								.get(0).getId().getCaid();
				
				List<A02factco> lstFacs = CtrlCajas.leerTraslados(caid, iFechaHoy);

				if (lstFacs != null && !lstFacs.isEmpty())
					lstHfacturasContado = formatFactura(lstFacs);
				
				return;
			}

			//&& ======= Verificar si debe sincronizarse o no facturas.
			for (F55ca017 f017 : f55ca017) {
				String sLinea = CompaniaCtrl.leerLineaNegocio(
									f017.getId().getC7mcul());
				if(sLinea == null || sLinea.trim().compareTo("") == 0)
					continue;
				bSincroniza = PropertiesSystem.LNS_JDEDWARDS
									.contains(sLinea.trim()); 
				if(bSincroniza) 
					break;
			}
			if (bSincroniza)
				sincronizarFacturas();
			
//			listarFacturasContadoDelDia();
			new com.casapellas.controles.tmp.FacturaCrtl().listarFacturasContadoDelDiaTmp();
			
			//&& ==== Verificar que las facturas cargadas en caja se hayan anulado en jde (FDC Microtec).
			if(m.containsKey("lstHfacturasContado") )
				lstHfacturasContado = (ArrayList)m.get("lstHfacturasContado") ;
			
			if( !lstHfacturasContado.isEmpty() && bSincroniza){
				new FacturaContadoCtrl()
						.comprobarFacturaActiva(lstHfacturasContado);
			}
		
			String sMensaje2 = "";
			if(m.get("lstHfacturasContado") == null || ((ArrayList)m.get("lstHfacturasContado")).size() == 0){
				lstHfacturasContado = new ArrayList();
				sMensaje2 = "No se encontraron resultados";

			}
			
			sMensaje = sMensaje2;
			txtMensaje.setValue(sMensaje2);
			m.put("sMensaje", sMensaje2);
			
			LogCajaService.CreateLog("refrescarFacturasContado", "INFO", "FIN METODO => refrescarFacturasContado");
		
		} catch (Exception ex) {
			ex.printStackTrace();
		}finally{
			try{
				m.put("lstHfacturasContado",lstHfacturasContado);
				cmbFiltroMonedas.dataBind();
				cmbFiltroFacturas.dataBind();
				gvHfacturasContado.dataBind();
				lblResultadoRec.setValue("");
				dwCargando.setWindowState("hidden");
			}catch(Exception e2){
				e2.printStackTrace();
			}
		}
	}

/************************CAMBIAR MONEDA DEL DETALLE***************************************/
	public void cambiarMonedaDetalle(ValueChangeEvent e) {
		Divisas d = new Divisas();
		Map m = null;
		Hfactura hFac = null;
		List lstDetalle = null;
		Dfactura dFac = null, newDfac = null;
		List lstNewDetalle = new ArrayList();
		String monedaActual = "";
		String sMonedaBase = "";
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		try {
			m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
			monedaActual = cmbMonedaDetalle.getValue().toString();
			hFac = (Hfactura) m.get("Hfactura");
			lstDetalle = hFac.getDfactura();
			//obtener companias x caja
			sMonedaBase = cCtrl.sacarMonedaBase((F55ca014[])m.get("cont_f55ca014"), hFac.getCodcomp());
			if (monedaActual.equals(sMonedaBase)) {
				txtSubtotal = hFac.getSubtotal();
				txtTotal = d.formatDouble(hFac.getCpendiente()) + "";
				txtIva = d.roundDouble4(hFac.getCpendiente() - txtSubtotal);
				for (int i = 0; i < lstDetalle.size(); i++) {
					dFac = (Dfactura) lstDetalle.get(i);
					dFac.setPreciounit(dFac.getPreciounit() * dFac.getTasa().doubleValue());
					lstNewDetalle.add(dFac);
				}

			} else {
				txtSubtotal = hFac.getSubtotalf();
				txtTotal = d.formatDouble(hFac.getDpendiente());
				txtIva = d.roundDouble4(hFac.getDpendiente() - txtSubtotal);
				for (int i = 0; i < lstDetalle.size(); i++) {
					dFac = (Dfactura) lstDetalle.get(i);
					dFac.setPreciounit(dFac.getPreciounit() / dFac.getTasa().doubleValue());
					lstNewDetalle.add(dFac);
				}
			}
			int h = lstDetalle.size();
			m.put("lstDfacturasContado", lstNewDetalle);

			gvDfacturasContado.dataBind();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
/************************CAMBIAR MONEDA DEL DETALLE en recibo de contado***************************************/
	public void cambiarMonedaDetalleReciboContado(ValueChangeEvent e) {
		Divisas divisas = new Divisas();
		Hfactura hFac = (Hfactura) m.get("Hfactura");
		List lstDetalle = hFac.getDfactura();
		Dfactura dFac = null, newDfac = null;
		List lstNewDetalle = new ArrayList();
		String monedaActual = cmbMonedaDetalleReciboContado.getValue().toString();
		String sMonedaBase = "";
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		try {
			//obtener companias x caja
			sMonedaBase = cCtrl.sacarMonedaBase((F55ca014[])m.get("cont_f55ca014"), hFac.getCodcomp());
			if (monedaActual.equals(sMonedaBase)) {
				txtSubtotal = hFac.getSubtotal() * hFac.getTasa().doubleValue();
				txtIva = hFac.getIva() * hFac.getTasa().doubleValue();
				txtTotal = divisas.formatDouble(hFac.getTotal() * hFac.getTasa().doubleValue()) + "";

				for (int i = 0; i < lstDetalle.size(); i++) {
					dFac = (Dfactura) lstDetalle.get(i);
					dFac.setPreciounit(dFac.getPreciounit() * dFac.getTasa().doubleValue());
					lstNewDetalle.add(dFac);
				}

			} else {
				txtSubtotal = hFac.getSubtotal();
				txtIva = hFac.getIva();
				txtTotal = divisas.formatDouble(hFac.getTotal());

				for (int i = 0; i < lstDetalle.size(); i++) {
					dFac = (Dfactura) lstDetalle.get(i);
					dFac.setPreciounit(dFac.getPreciounit() / dFac.getTasa().doubleValue());
					lstNewDetalle.add(dFac);
				}
			}
			m.put("lstDfacturasContado", lstNewDetalle);
			gvDfacturasContadoRecibo.dataBind();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

/**************************SELECCION DE FACTURAS DE CONTADO DESDE EL GRID**************************************************/

		public boolean getFacturasSelected(ConfiguracionMensaje[] configuracionMensaje) {
		
		
		boolean valido = true;
		List lstSelectedRows = new ArrayList();
		List lstSelectedFacs = new ArrayList();
		FacturaContadoCtrl facContCtrl = new FacturaContadoCtrl();
		
		Boolean bValidaCliente = false;
		Boolean bValidaCompania = false;
		Boolean bValidaMoneda = false;
		Boolean bValidaCombinacionFac_Dev = false;

		SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
		
		try {
			m.remove("facturasSelected");
			
			lstSelectedRows = gvHfacturasContado.getSelectedRows();
			Hfactura hFacSelected = null;
			Hfactura hFacFirst = null;

			// sacar rows seleccionados
			RowItem row = null;

			// determinar si esta anulada

			if (lstSelectedRows.size() > 1) {// validar facturas
				row = (RowItem) lstSelectedRows.get(0);
				hFacFirst = (Hfactura) gvHfacturasContado.getDataRow(row);
				lstSelectedFacs.add(hFacFirst);
				String sl1 = hFacFirst.getCodunineg().trim().substring(2, 4);
				for (int i = 1; i < lstSelectedRows.size(); i++) {
					row = (RowItem) lstSelectedRows.get(i);
					hFacSelected = (Hfactura) gvHfacturasContado.getDataRow(row);
					String sl21 = hFacSelected.getCodunineg().trim().substring(2, 4);
					// validar cliente
					if (hFacFirst.getCodcli() != hFacSelected.getCodcli() || !hFacFirst.getNomcli().trim().equals(hFacSelected.getNomcli().trim())) {
						valido = false;
						bValidaCliente=true;
					}
					// validar compañia
					else if (!hFacFirst.getCodcomp().trim().equals(hFacSelected.getCodcomp().trim())) {
						valido = false;
						bValidaCompania=true;
					}
					// validar moneda
					else if (!hFacFirst.getMoneda().equals(hFacSelected.getMoneda())) {
						valido = false;
						bValidaMoneda=true;
					}
					// validar si hay devolucion con facturas de contado
					else if ((hFacFirst.getTipodoco().trim().equals("") && hFacFirst.getNodoco() == 0)&& (!hFacSelected.getTipodoco().trim().equals("") && hFacSelected.getNodoco() > 0)) {
						valido = false;
						bValidaCombinacionFac_Dev=true;
					}
					if (valido) {
						lstSelectedFacs.add(hFacSelected);
					}
				}
			} else if (lstSelectedRows.size() == 1) {
				row = (RowItem) lstSelectedRows.get(0);
				hFacFirst = (Hfactura) gvHfacturasContado.getDataRow(row);
				lstSelectedFacs.add(hFacFirst);
			}
			else //Cambio hecho por lfonseca - 2021-01-05, condición no existia
			{
				valido = false;
			}
			
			if (valido) {
				m.put("facturasSelected", lstSelectedFacs);
				gvfacturasSelec.dataBind();
			} 
			else {

				int indexMensaje;
				
				if(lstSelectedRows.size()>=1)
				{
					if(bValidaCliente)
					{
						indexMensaje = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(configuracionMensaje, "CAJA", "REC_CONTADO", "MOSTRARRECIBO", "GETFACTSELECT_01");
												
						if(indexMensaje < 0)
							lblValidaFactura.setValue("<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El cliente de la factura debe ser el mismo<br>");
						else
							lblValidaFactura.setValue("<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> " + configuracionMensaje[indexMensaje].getDescripcionMensaje() + "<br>");
					}
					else if(bValidaCompania)
					{
						indexMensaje = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(configuracionMensaje, "CAJA", "REC_CONTADO", "MOSTRARRECIBO", "GETFACTSELECT_02");
												
						if(indexMensaje < 0)
							lblValidaFactura.setValue("<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> La compania de la factura debe ser la misma<br>");
						else
							lblValidaFactura.setValue("<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> " + configuracionMensaje[indexMensaje].getDescripcionMensaje() + "<br>");
					}
					else if(bValidaMoneda)
					{
						
						indexMensaje = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(configuracionMensaje, "CAJA", "REC_CONTADO", "MOSTRARRECIBO", "GETFACTSELECT_03");
												
						if(indexMensaje < 0)
							lblValidaFactura.setValue("<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> La moneda de la factura debe ser la misma<br>");
						else
							lblValidaFactura.setValue("<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> " + configuracionMensaje[indexMensaje].getDescripcionMensaje() + "<br>");
					}
					else if(bValidaCombinacionFac_Dev)
					{
						indexMensaje = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(configuracionMensaje, "CAJA", "REC_CONTADO", "MOSTRARRECIBO", "GETFACTSELECT_04");
												
						if(indexMensaje < 0)
							lblValidaFactura.setValue("<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> No debe procesar devoluciones y facturas<br>");
						else
							lblValidaFactura.setValue("<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> " + configuracionMensaje[indexMensaje].getDescripcionMensaje() + "<br>");
					}
					else
					{
						lblValidaFactura.setValue("<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El cliente de la factura debe ser el mismo<br>"
										+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> La moneda de la factura debe ser la misma<br>"
										+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> La Compañia de la factura debe ser la misma<br>"
										+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> No debe procesar devoluciones y facturas<br>");
					}
					dwValidacionFactura.setStyle("height: 170px; visibility: visible; width: 365px");
				}
				else
				{
					indexMensaje = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(configuracionMensaje, "CAJA", "REC_CONTADO", "MOSTRARRECIBO", "GETFACTSELECT_05");
					
					if(indexMensaje < 0)
						lblValidaFactura.setValue("<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> No se encontro factura o dev seleccionado<br>");
					else
						lblValidaFactura.setValue("<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> " + configuracionMensaje[indexMensaje].getDescripcionMensaje() + "<br>");
					
					dwValidacionFactura.setStyle("height: 170px; visibility: visible; width: 365px");
				}
				dwValidacionFactura.setWindowState("normal");

				gvHfacturasContado.dataBind();
				srm.addSmartRefreshId(gvHfacturasContado.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(lblValidaFactura.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(dwValidacionFactura.getClientId(FacesContext.getCurrentInstance()));
			}
		} catch (Exception ex) {
			valido = false;
			ex.printStackTrace(); 
			
			int i = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(configuracionMensaje, "CAJA", "REC_CONTADO", "MOSTRARRECIBO", "GETFACTSELECT_EX");
			if(i < 0)
				lblValidaFactura.setValue("<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> " + ex.getMessage() + "<br>");
			else
				lblValidaFactura.setValue("<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> " + configuracionMensaje[i].getDescripcionMensaje().replace("@Error", ex.getMessage()) + "<br>");
			
			dwValidacionFactura.setStyle("height: 170px; visibility: visible; width: 365px");
			dwValidacionFactura.setWindowState("normal");

			gvHfacturasContado.dataBind();
			srm.addSmartRefreshId(gvHfacturasContado.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblValidaFactura.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(dwValidacionFactura.getClientId(FacesContext.getCurrentInstance()));
		}
		return valido;
	}

	public void cerrarValidaFactura(ActionEvent ev) {
		dwValidacionFactura.setWindowState("hidden");
	}

	
	public List getLstAfiliados(int iCaid, String sCodcomp, String sLineaFactura,String sMoneda){
		List<SelectItem> Afiliados = new ArrayList<SelectItem>();

		try {
			List<SelectItem>lstPOS = new ArrayList<SelectItem>();
			Cafiliados[] cafiliado = null;
			AfiliadoCtrl afCtrl = new AfiliadoCtrl();
			String sDescrip="";
			
			Afiliados.add(new SelectItem("01", "--Afiliados--"));
			lstPOS.add(new SelectItem("01", "--Afiliados--"));

			cafiliado = afCtrl.obtenerAfiliadoxCaja_Compania(iCaid, sCodcomp, sLineaFactura, sMoneda);
			if(cafiliado != null && cafiliado.length > 0){ 
				 for (Cafiliados ca : cafiliado) {
						SelectItem si = new SelectItem();
						si.setValue(ca.getId().getCxcafi().trim());
						si.setLabel(ca.getId().getCxdcafi().trim());
						sDescrip = ca.getId().getCxcafi().trim()+", "+ ca.getId().getCxdcafi().trim();
						sDescrip += ", LN:";
						sDescrip +=	(String.valueOf(ca.getId().getC6rp07()).trim().equalsIgnoreCase(""))?
									"S/L": ca.getId().getC6rp07().trim(); 
						si.setDescription(sDescrip);
						lstPOS.add(si);
				 }
			}
			 Afiliados = lstPOS;
			 
		} catch (Exception error) {
			Afiliados = new ArrayList();
			error.printStackTrace();
		}
		return Afiliados;
	} 

	/** ******************************************************************************************** 
	 * @throws Exception */
	// //////////////////////METODOS DE RECIBO/////////////////////////////////////////////////////////
	public void cargarRecibo(Hfactura hFac, List<Hfactura> lstFacturasSelected,String sUltimoNumero, int iCajaId, String sTasaJDE,
							String sTasaParalela, Tpararela[] tpcambio,String sMonedaBase, Vf55ca01 f01, 
							ConfiguracionMensaje[] lstConfiguracionMensaje) throws Exception {
		
		// formateador de numeros
		Divisas divisas = new Divisas();
		Hfactura[] hFacSelected = null;
		List lstPagosClean = new ArrayList();
		m.put("lstPagos", lstPagosClean);
		m.put("mpagos", lstPagosClean);
		double dMontoAplicar = 0,dMontoAplicarf = 0;
		double TasaPar = 0.0;

		// afiliados
		AfiliadoCtrl afCtrl = new AfiliadoCtrl();
		Cafiliados[] cafiliado = null;
		boolean bAnulada = false;
		FacturaContadoCtrl facContCtrl = new FacturaContadoCtrl();
		String sLineaFactura = "";
		Vf55ca01 vf01 = null;
		try {
			//obtener datos de Caja
			vf01 = (Vf55ca01) ((List) m.get("lstCajas")).get(0);
			
			restablecerEstilosPago();
			restablecerEstilos();

			lblCodigoCliente.setValue(hFac.getCodcli() + "");
			lblNombreCliente.setValue(hFac.getNomcli());
			lblNumeroRecibo.setValue(sUltimoNumero);

			cmbMetodosPago.dataBind();
		
			String sCodMet = cmbMetodosPago.getValue().toString();
			cambiarVistaMetodos(sCodMet,vf01);
			cmbMoneda.dataBind();
		
			sLineaFactura = CompaniaCtrl.leerLineaNegocio( hFac.getCodunineg().trim() );
			
			//Se validara que sLineaFactura no venga vacia o null
			if(sLineaFactura == null || sLineaFactura.equals(""))
			{
				String msgProceso = "";
				
				int i = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(lstConfiguracionMensaje, "CAJA", "REC_CONTADO", "MOSTRARRECIBO", "CARGARRECIBO_01");
				
				if(i<0)
					msgProceso = "No se logro encontrar linea de negocio asociado a la unidad de negocio: " + hFac.getCodunineg().trim();
				else
					msgProceso = lstConfiguracionMensaje[i].getDescripcionMensaje()
							.replace("@UnidadNegocio",hFac.getCodunineg().trim());				
				
				throw new Exception(msgProceso);
			}
			
			//cargar afiliados
			if( f01.getId().getCasktpos() == 'N' ){			
		
				lstAfiliado = this.getLstAfiliados(iCajaId,hFac.getCodcomp(),sLineaFactura,cmbMoneda.getValue().toString());
				
			}else{
				lstAfiliado = getLstAfiliadosSocketPOS(iCajaId, hFac.getCodcomp(), sLineaFactura, cmbMoneda.getValue().toString());
			}
			
			
			if(lstAfiliado != null){
				m.put("lstAfiliado", lstAfiliado);
				ddlAfiliado.dataBind();
	
				//---- Calcular el monto aplicar.
				for (Hfactura hfs : lstFacturasSelected) {
					if((hFac.getMoneda().equals(sMonedaBase))){
						dMontoAplicar += hfs.getCpendiente();											
					}else{
						dMontoAplicar += hfs.getDpendiente();		
						dMontoAplicarf += hfs.getCpendiente();
					}
				}
				// poner resumen de pago
				lblMontoAplicar.setValue("A Aplicar " + hFac.getMoneda() + ":");
				txtMontoAplicar.setValue(divisas.formatDouble(dMontoAplicar));
				lblMontoRecibido.setValue("Recibido " + hFac.getMoneda() + ":");
				txtMontoRecibido.setValue("0.00");
				lblCambio.setValue("Cambio " + hFac.getMoneda() + ":");
				txtCambio.setValue("-" + divisas.formatDouble(dMontoAplicar));
				txtCambio.setStyle("font-size: 10pt; color: red");
				txtCambioForaneo.setStyle("visibility: hidden; width: 0px");
				lblCambioDomestico.setValue("");
				txtCambioDomestico.setValue("");
				lnkCambio.setStyle("visibility: hidden; width: 0px");
				if (!hFac.getMoneda().equals(sMonedaBase)) {
					// buscar tasa de cambio paralela para moneda
					for (int t = 0; t < tpcambio.length; t++) {
						if (tpcambio[t].getId().getCmono().equals(sMonedaBase)) {
							TasaPar = tpcambio[t].getId().getTcambiom().doubleValue();
							break;
						}
					}
					lblPendienteDomestico.setValue("Cambio " + sMonedaBase + ":");
					lblPendienteDomestico.setStyle("visibility: visible");
//					txtPendienteDomestico.setValue("-" + divisas.formatDouble(dMontoAplicarf));
					txtPendienteDomestico.setValue("-" + divisas.formatDouble(dMontoAplicar*TasaPar));
					txtPendienteDomestico.setStyle("visibility: visible; color: red");
				}
				lnkCambio.setIconUrl("");
				lnkCambio.setHoverIconUrl("");
				// limpiar campos del recibo
				txtConcepto.setValue("");
				txtMonto.setValue("");
				txtReferencia1.setValue("");
				txtReferencia2.setValue("");
				txtReferencia3.setValue("");
				txtNumRec.setValue("");
				lblNumRecm.setValue("");
				lblNumrec = "Último Recibo: ";
				txtNumRec.setStyle("display:none");
				txtFecham.setStyle("display:none");
				cmbTiporecibo.dataBind();
				
				//--- Determinar si debe mostrarse el check de traslado de POS.
				lblEtTrasladoPOS.setStyle("display:none");
				chkTrasladarPOS.setStyle("width: 20px; display: none");
				
				if(	hFac.getTipofactura().trim().toUpperCase().equals("Y2") &&	sLineaFactura.trim().equals("07")){
					
					//---Determinar si es traslado de factura.
					Trasladofac tf = new TrasladoCtrl().buscarTrasladofac(null, hFac, iCajaId, 0, "");
					if(tf!=null){
						m.put("fdc_ObjTrasladoFacTrasladoPOS", tf);
					}else{
						m.remove("fdc_ObjTrasladoFacTrasladoPOS");
					}
					
				}
				dwRecibo.setWindowState("normal");
				
				
				//&& ======== Validaciones de factura de linea repuestos.
				BigDecimal bdTasaMulpFac = new BigDecimal(
						Double.toString(dMontoAplicarf)).divide(new BigDecimal(
						Double.toString(dMontoAplicar)), 4,
						RoundingMode.HALF_UP);
				boolean bMultipleFacs = lstFacturasSelected.size() > 1;
				
				for (int i = 0; i < lstFacturasSelected.size(); i++) {
					Hfactura hfac1 = lstFacturasSelected.get(i);

					String lineaDeFactura = CompaniaCtrl.leerLineaNegocio( hFac.getCodunineg().trim() );
					if (PropertiesSystem.LNS_REPUESTOS.contains( lineaDeFactura.trim() ) && hfac1.getMoneda().compareTo(sMonedaBase) != 0) {

						if(bMultipleFacs)
							hfac1.setTasa(bdTasaMulpFac);
						else
							hfac1.setTasa(hfac1.getTotaldomtmp()
								.divide(new BigDecimal(String.valueOf(hfac1
										.getTotal())), 4, RoundingMode.HALF_UP));

						lstFacturasSelected.set(i, hfac1);
					}
				}
				m.put("facturasSelected", lstFacturasSelected);
				
				hFac = lstFacturasSelected.get(0);
				if(!hFac.getMoneda().equals(sMonedaBase)){
					sTasaJDE = hFac.getMoneda() +  ": " + hFac.getTasa();
				}
				
				lblTasaCambio.setValue(sTasaParalela);
				m.put("lblTasaCambio", sTasaParalela);
	
				lblTasaJDE.setValue(sTasaJDE);
				m.put("lblTasaJDE", sTasaJDE);
				
			}else{//mostrar mensaje de error
				
				int i = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(lstConfiguracionMensaje, "CAJA", "REC_CONTADO", "MOSTRARRECIBO", "CARGARRECIBO_02");
				
				if(i<0)
					sMensaje = sMensaje
				    + " No se encontraron afiliados configurados para esta caja!!!";
				else
					sMensaje = sMensaje
						    +  lstConfiguracionMensaje[i].getDescripcionMensaje();
				
				throw new Exception(sMensaje);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			
			String msgProceso = "";
				
			int i = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(lstConfiguracionMensaje, "CAJA", "REC_CONTADO", 
					"MOSTRARRECIBO", "CARGARRECIBO_EX");
			
			if(i<0)
			{
				msgProceso = ex.getMessage();
			}
			else
			{
				msgProceso = lstConfiguracionMensaje[i].getDescripcionMensaje()
						.replace("@Error", ex.getMessage());
			}
			
		throw new Exception(msgProceso);
		
		}
	}

/******************************************************************************************************/
	public boolean validarReciboAntesDeMostrar(Object oFac, ConfiguracionMensaje[] configuracionMensaje) {
		boolean bValido = true;
		boolean bAnulada = false;
		boolean bTraslado = false;
		SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
		Hfactura hFac = null;
		FacturaContadoCtrl facContCtrl = new FacturaContadoCtrl();
		String sMensaje = "";
		try {
			dwProcesa.setWindowState("hidden");
			dwValidacionFactura.setWindowState("hidden");

			// Valida Factura
			List<Hfactura> lstFacturasSelected = new ArrayList<Hfactura>();
			if (oFac != null && !((List) oFac).isEmpty()) {
				lstFacturasSelected = (ArrayList<Hfactura>) oFac;
				
				List<Hfactura> devoluciones = (ArrayList<Hfactura>)
				CollectionUtils.select(lstFacturasSelected, new Predicate(){
					@Override
					public boolean evaluate(Object o) {
						return ((Hfactura)o).getNodoco() != 0 ;
					}
				});
				List<Hfactura> facturas = (ArrayList<Hfactura>)
				CollectionUtils.select(lstFacturasSelected, new Predicate(){
					@Override
					public boolean evaluate(Object o) {
						return ((Hfactura)o).getNodoco() == 0 ;
					}
				});
				
				if(facturas != null && devoluciones != null && !facturas.isEmpty() && !devoluciones.isEmpty()){
					
					int i = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(configuracionMensaje, "CAJA", "REC_CONTADO", "MOSTRARRECIBO", "VALIDARRECIBO_03");
					
					if(i<0)
						sMensaje = "No pueden incluirse facturas y devoluciones en un mismo recibo";
					else
						sMensaje = configuracionMensaje[i].getDescripcionMensaje();
					
					lblValidaFactura.setValue("<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" />"+sMensaje);
					dwValidacionFactura.setWindowState("normal");
					dwValidacionFactura.setStyle("height: 150px; visibility: visible; width: 375px");
					
					return bValido = false;
				}
				
				
				Vf55ca01 f5 = (Vf55ca01)((List) m.get("lstCajas")).get(0);
				TrasladoCtrl tc = new TrasladoCtrl();
				bValido = true;
				for (Hfactura hFc : lstFacturasSelected) {
					
					//--- Validar que la factura no este anulada.
					Vhfactura vhf = facContCtrl.comprobarSiExiste(hFc.getNofactura(), 
							hFc.getTipofactura(), hFc.getCodsuc(), 
							hFc.getCodunineg(), hFc.getCodcli(),
							hFc.getFechajulian(),
							hFc.getHoraEntera(),
							hFc.getEstado());
					
					if(vhf!=null){
						if(!vhf.getId().getEstado().trim().equals("")){
							bValido = false;
							
							int i = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(configuracionMensaje, "CAJA", "REC_CONTADO", "MOSTRARRECIBO", "VALIDARRECIBO_04");
							
							if(i<0)
								sMensaje = "La factura "+hFc.getNofactura()+" se encuentra anulada.";
							else
								sMensaje = configuracionMensaje[i].getDescripcionMensaje().replace("@NumeroFactura", String.valueOf(hFc.getNofactura()));
						}
					}else{
						bValido = false;
						
						int i = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(configuracionMensaje, "CAJA", "REC_CONTADO", "MOSTRARRECIBO", "VALIDARRECIBO_05");
						
						if(i<0)
							sMensaje = "Error al obtener información de factura "+hFc.getNofactura();
						else
							sMensaje = configuracionMensaje[i].getDescripcionMensaje().replace("@NumeroFactura", String.valueOf(hFc.getNofactura()));
						
					}
					
					//&& ========= Verificar que la factura no se encuentre trasladada.
					Trasladofac tf = tc.existeFacturaenTrasladofac(hFc, f5.getId().getCaid(), "");
					if(tf!=null){
						bValido = false;
						int i = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(configuracionMensaje, "CAJA", "REC_CONTADO", "MOSTRARRECIBO", "VALIDARRECIBO_06");
						
						if(i<0)
						{
							sMensaje = "La factura "+hFc.getNofactura()+" la factura se encuentra trasladada hacia caja "+tf.getCaiddest();
							sMensaje += ", Hora: "+new SimpleDateFormat("hh:mm:ss a").format(tf.getFecha());
						}
						else
						{
							sMensaje = configuracionMensaje[i].getDescripcionMensaje()
									.replace("@NumeroFactura", String.valueOf(hFc.getNofactura()))
									.replace("@NumeroCaja", String.valueOf(tf.getCaiddest()))
									.replace("@Hora", new SimpleDateFormat("hh:mm:ss a").format(tf.getFecha()));
						}
					}
				}
				
				//&& ========= Verificar que la factura no haya sido cancelada.
				List<Recibofac> lstPagos = tc.buscarPagosFactura(lstFacturasSelected); //lstPagos = new ArrayList() 
				if( !lstPagos.isEmpty() ){
					
					int i = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(configuracionMensaje, "CAJA", "REC_CONTADO", "MOSTRARRECIBO", "VALIDARRECIBO_07");
					
					if(i<0)
					{
						sMensaje = "Las facturas # ";
						for (Recibofac rf : lstPagos) 
							sMensaje += String.valueOf( rf.getId().getNumfac() ) ;
						sMensaje += " ya han sido canceladas.";
					}
					else
					{
						String strFacturas = "";
						
						for (Recibofac rf : lstPagos) 
							strFacturas += String.valueOf( rf.getId().getNumfac() ) + ", " ;
						
						sMensaje = configuracionMensaje[i].getDescripcionMensaje()
								.replace("@NumerosFacturas", strFacturas);
					}
					
					bValido = false;
				}
					
				if(!bValido){
					lblValidaFactura.setValue("<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" />"+sMensaje);
					dwValidacionFactura.setWindowState("normal");
					dwValidacionFactura.setStyle("height: 150px; visibility: visible; width: 375px");

					listarFacturasContadoDelDia();
					gvHfacturasContado.dataBind();
					srm.addSmartRefreshId(gvHfacturasContado.getClientId(FacesContext.getCurrentInstance()));
					srm.addSmartRefreshId(dwValidacionFactura.getClientId(FacesContext.getCurrentInstance()));
					return false;
				}
				
				// verificar si las facturas estan anuladas
				if (cmbFiltroFacturas.getValue().toString().equals("04")) {// anuladas
					bAnulada = true;
					bValido = false;
				} else if (cmbFiltroFacturas.getValue().toString().equals("03")) {// todas
					for (int i = 0; i < lstFacturasSelected.size(); i++) {
						hFac = (Hfactura) lstFacturasSelected.get(i);
						bAnulada = facContCtrl.isFacturaAnulada(hFac.getNofactura(), hFac.getTipofactura());
						if (bAnulada) {
							bValido = false;
							break;
						}
					}
				}
				if (bAnulada) {// facturas anuladas
					
					int i = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(configuracionMensaje, "CAJA", "REC_CONTADO", "MOSTRARRECIBO", "VALIDARRECIBO_08");
					
					if(i<0)
						sMensaje = sMensaje + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Factura Anulada!<br/>";
					else
						sMensaje = sMensaje + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> " + configuracionMensaje[i].getDescripcionMensaje() + "<br/>";
					
					
					dwValidacionFactura.setStyle("height: 140px; visibility: visible; width: 365px");
					dwValidacionFactura.setWindowState("normal");
					srm.addSmartRefreshId(dwValidacionFactura.getClientId(FacesContext.getCurrentInstance()));
					bValido = false;
				}
			} else {// no hay facturas seleccionadas
				
				int i = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(configuracionMensaje, "CAJA", "REC_CONTADO", "MOSTRARRECIBO", "VALIDARRECIBO_01");
				
				if(i<0)
					sMensaje = sMensaje
						+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Seleccione al menos una factura para procesar el recibo<br/>";
				else
					sMensaje = sMensaje 
						+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> " + configuracionMensaje[i].getDescripcionMensaje() + "<br/>";
				
				
				
				dwValidacionFactura.setWindowState("normal");
				dwValidacionFactura.setStyle("height: 130px; visibility: visible; width: 330px");
				srm.addSmartRefreshId(dwValidacionFactura.getClientId(FacesContext.getCurrentInstance()));
				bValido = false;
			}
			// verificar si tasa de cambio paralela existe
			if (m.get("tpcambio") == null) {
				
				int i = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(configuracionMensaje, "CAJA", "REC_CONTADO", "MOSTRARRECIBO", "VALIDARRECIBO_02");
				
				if(i<0)
					sMensaje = sMensaje
						+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Tasa de cambio paralela no esta configurada<br/>";
				else
					sMensaje = "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> " + configuracionMensaje[i].getDescripcionMensaje() + "<br/>";
				
				dwValidacionFactura.setWindowState("normal");
				dwValidacionFactura.setStyle("height: 150px; visibility: visible; width: 370px");
				bValido = false;
			}
			lblValidaFactura.setValue(sMensaje);
		} catch (Exception ex) {
			bValido = false;
			ex.printStackTrace(); 
			
			int i = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(configuracionMensaje, "CAJA", "REC_CONTADO", "MOSTRARRECIBO", "VALIDARRECIBO_EX");
			if(i<0)
				sMensaje = "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> " + ex.getMessage() + "<br/>";
			else
				sMensaje = "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> " + configuracionMensaje[i].getDescripcionMensaje().replace("@Error", ex.getMessage()) + "<br/>";
			
		}
		return bValido;
	}

/*****************************ABRIR VENTANA PARA REALIZAR RECIBO A FACTURA DE CONTADO*********************************************/
	public void mostrarRecibo(ActionEvent e) {
		boolean bValido = false;
		List<Hfactura> lstFacturasSelected = null;
		Hfactura hFac = null;
		String sUltimoNumero = "";
		ReciboCtrl rpCtrl = new ReciboCtrl();
		int iCajaId = 0;
		
		
		MetodosPagoCtrl metCtrl = new MetodosPagoCtrl();
		// metodos de pago
		String[] f55ca012 = null;
		List<Metpago[]> lstMetPago = null;
		// monedas
		String[] sCodMod = null;
		MonedaCtrl monCtrl = new MonedaCtrl();
		
		CompaniaCtrl cCtrl = new CompaniaCtrl();	
		String sMonedaBase = "";
		
		Vf55ca01 f55ca01 = null;
		
		Session session = null;
		
		
		
		try {
		
			//Cerrando la session para limpiar
			HibernateUtilPruebaCn.closeSession();
			
			ConfiguracionMensaje[] lstConfiguracionMensaje = (ConfiguracionMensaje[]) m.get("lstConfiguracionMensaje"); //((ArrayList<ConfiguracionMensaje>) JsfUtil.getObjectFromSessionMap("lstConfiguracionMensaje"));
			
			CodeUtil.removeFromSessionMap(new String[]{
					"sco_SbrDfr"
					,"sco_SobrantePago"
					,"sco_MpagoSobrante"
					,"lstSolicitud"
					,"facturasSelected"
					,"lstPagos"
					,"lstDatosTrack_Con"
					,"lstPagos_TC"
					,"con_processRecibo"
					,"fdc_ObjTrasladoFacTrasladoPOS",
					"fdc_lstDetalleDonaciones", "fdc_dncDonacionJDE_devolver",
					"fdc_FechaFacturaOriginal", "fdc_ObjTrasladoFacTrasladoPOS", 
					"sco_SbrDfr", "sco_SobrantePago", "sco_MpagoSobrante",
					"metodopagoborrar", "lstPagosCoTmp", "lstCoFactsTMP",
					"bdTasa" 
					
			});
		 
			//&& ============================================		
			
			bValido = getFacturasSelected(lstConfiguracionMensaje);
			if(!bValido)
				return;
			
			LogCajaService.CreateLog("mostrarRecibo", "INFO", "INICIO METODO => mostrarRecibo");
			
			session = HibernateUtilPruebaCn.currentSession();
			
			lstMetodosPago = new ArrayList<SelectItem>();
			f55ca01 = (Vf55ca01) ((List) m.get("lstCajas")).get(0);
			
			bValido = validarReciboAntesDeMostrar(m.get("facturasSelected"), lstConfiguracionMensaje);
			
			if (bValido) {
				
				iCajaId = Integer.parseInt(m.get("sCajaId").toString());
				lstFacturasSelected = (List<Hfactura>) m.get("facturasSelected");
				hFac = lstFacturasSelected.get(0);
				
				//obtener moneda base en base a companias x caja
				sMonedaBase = cCtrl.sacarMonedaBase((F55ca014[])m.get("cont_f55ca014"), hFac.getCodcomp());
				
				//Valida que moneda base este configurada
				if(sMonedaBase == null)
				{
					String msgProceso = "";
					
					int i = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(lstConfiguracionMensaje, "CAJA", "REC_CONTADO", "MOSTRARRECIBO", "MOSTRARRECIBO_02");
					
					if(i<0)
					{
						msgProceso = "Moneda base no esa configurada en esta caja: " + iCajaId + " para esta compañia: " + hFac.getCodcomp();
					}
					else
					{
						msgProceso = lstConfiguracionMensaje[i].getDescripcionMensaje()
								.replace("@Caja", String.valueOf(iCajaId))
								.replace("@Compania", hFac.getCodcomp());
					}
					
					throw new Exception(msgProceso);
				}
				
				sUltimoNumero = rpCtrl.obtenerUltimoRecibo(iCajaId, hFac.getCodcomp() )+"";
				
				//Valida numero de recibo que sera registrado en tablas de recibos de caja
				if(sUltimoNumero.trim().equals("-1") || sUltimoNumero.trim().equals("0"))
				{
					String msgProceso = "";
					
					int i = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(lstConfiguracionMensaje, "CAJA", "REC_CONTADO", "MOSTRARRECIBO", "MOSTRARRECIBO_03");
					
					if(i<0)
					{
						msgProceso = "No se pudo obtener el numero de recibo asociado a la caja: " + iCajaId + " para la compañia: " + hFac.getCodcomp();
					}
					else
					{
						msgProceso = lstConfiguracionMensaje[i].getDescripcionMensaje()
								.replace("@Caja", String.valueOf(iCajaId))
								.replace("@Compania", hFac.getCodcomp());
					}
					
					throw new Exception(msgProceso);
				}
				
				String sTasaJDE = "";
				if(!hFac.getMoneda().equals(sMonedaBase)){//si la factura es en dolares
					sTasaJDE = hFac.getMoneda() +  ": " + hFac.getTasa();
				}else{
					// poner tasa de cambio de JDE
					Tcambio[] tcJDE = (Tcambio[]) m.get("tcambio");
					for (int l = 0; l < tcJDE.length; l++) {
						sTasaJDE = sTasaJDE + " " + tcJDE[l].getId().getCxcrdc()+ ": " + tcJDE[l].getId().getCxcrrd() + "<br/>";
					}
				}
				// poner etiqueta paralela
				Tpararela[] tpcambio = null;
				tpcambio = (Tpararela[]) m.get("tpcambio");
				String sTasaParalela = "";
				for (int j = 0; j < tpcambio.length; j++) {
					sTasaParalela = sTasaParalela + " "
							+ tpcambio[j].getId().getCmond() + ": "
							+ tpcambio[j].getId().getTcambiom();
				}
				
				//--------------------------------
				//Modificado por lfonseca
				//2018-10-15
				//--------------------------------
				VerificarFacturaProceso vfp = new VerificarFacturaProceso();
				Vautoriz vautoriz = ((Vautoriz[]) m.get("sevAut"))[0];
				HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

				String ipAddress = request.getHeader("X-FORWARDED-FOR");
				if (ipAddress == null) {
				    ipAddress = request.getRemoteAddr();
				}

				String userAgent = UserAgent.parseUserAgentString(request.getHeader("user-agent")).getBrowser() + " " 
						+ UserAgent.parseUserAgentString(request.getHeader("user-agent")).getBrowserVersion();

				//&& ========== Objetos de tipos Hfactura 
				for(Object o : lstFacturasSelected){
					Hfactura hfactura = (Hfactura) o;
					
					String[] strVerificarF = vfp.verificarFactura(String.valueOf(iCajaId), 
																  String.valueOf(hfactura.getCodcli()), 
																  String.valueOf(hfactura.getNofactura()), 
																  hFac.getTipofactura(), 
																  "", 
																  String.valueOf(f55ca01.getId().getCacati()),
																  vautoriz.getId().getLogin().toLowerCase(),
																  ipAddress.toUpperCase(),
																  userAgent.toUpperCase(),
																  request.getLocalAddr().toUpperCase());
					
					//Modificado por lfonseca - 2021-01-07
					if(!strVerificarF[0].equals("S"))
					{
						String msgProceso = "";
						
						int i = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(lstConfiguracionMensaje, "CAJA", "REC_CONTADO", "MOSTRARRECIBO", "MOSTRARRECIBO_01");
						
						if(i<0)
						{
							msgProceso = "La factura  numero " + strVerificarF[2] +  
									 		", se esta procesando en la caja # " + strVerificarF[1] + ", " +
									 		"por el usuario " + strVerificarF[5] + " usando un navegador " + strVerificarF[6] +
									 		" y accediendo al servidor de caja " + strVerificarF[7];
						}
						else
						{
							msgProceso = lstConfiguracionMensaje[i].getDescripcionMensaje()
									.replace("@Factura", strVerificarF[2])
									.replace("@Caja", strVerificarF[1])
									.replace("@Usuario",strVerificarF[5])
									.replace("@Navegador", strVerificarF[6])
									.replace("@ServidorCaja", strVerificarF[7]);
						}
						
						throw new Exception(msgProceso);
					}
					
				}
				
				//--------------------------------
				//Termina Modificacion lfonseca
				//--------------------------------
				
				//&& =========== conservar todos los metodos de pago configurados.
				List<Vf55ca012> MetodosPagoConfigurados =  DonacionesCtrl.obtenerMpagosConfiguradosCaja(hFac.getCodcomp(), iCajaId);
				
				//Valida numero de recibo que sera registrado en tablas de recibos de caja
				if(MetodosPagoConfigurados == null || MetodosPagoConfigurados.size()==0)
				{
					String msgProceso = "";
					
					int i = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(lstConfiguracionMensaje, "CAJA", "REC_CONTADO", "MOSTRARRECIBO", "MOSTRARRECIBO_04");
					
					if(i<0)
					{
						msgProceso = "No se pudo obtener los métodos de pagos asociado a la caja: " + iCajaId + " para la compañia: " + hFac.getCodcomp();
					}
					else
					{
						msgProceso = lstConfiguracionMensaje[i].getDescripcionMensaje()
								.replace("@Caja", String.valueOf(iCajaId))
								.replace("@Compania", hFac.getCodcomp());
					}
					
					throw new Exception(msgProceso);
				}
				
				CodeUtil.putInSessionMap("fdc_MetodosPagoConfigurados", MetodosPagoConfigurados );
				 
				// cargar metodos de pago de compania de factura
				f55ca012 = metCtrl.obtenerMetodosPagoxCaja_Compania(iCajaId,hFac.getCodcomp());
				if (f55ca012 != null && f55ca012.length > 0) { 

					lstMetPago = new ArrayList<Metpago[]>();

					for (String f55 : f55ca012) {
			    		Metpago formapago = com.casapellas.controles.MetodosPagoCtrl.metodoPagoCodigo( f55 );
			    		lstMetodosPago.add(new SelectItem( formapago.getId().getCodigo(), formapago.getId().getMpago() ) );
			    		lstMetPago.add( new Metpago[]{formapago} ) ;
					}
					m.put("metpago", lstMetPago);
					m.put("lstMetodosPago", lstMetodosPago);
					m.put("lstMetodosPagoDev", lstMetodosPago);

					
					// cargar monedas de metodo de pago y compania de factura
					List lstMoneda = new ArrayList();
					sCodMod = monCtrl.obtenerMonedasxMetodosPago_Caja(iCajaId,hFac.getCodcomp(), cmbMetodosPago.getValue().toString());
					// validar monedas
					if (sCodMod != null && sCodMod.length > 0) {
						
						for (int i = 0; i < sCodMod.length; i++) {
							lstMoneda.add(new SelectItem(sCodMod[i], sCodMod[i]));
						}
						m.put("lstMoneda", lstMoneda);
						m.put("lstMonedaDev", lstMoneda);
						
						// verficar si es recibo de factura o recibo de
						// devolucion de factura
						
						if (hFac.getTipodoco().trim().equals("")) {// factura
							cargarRecibo(hFac, lstFacturasSelected,sUltimoNumero, iCajaId, sTasaJDE,sTasaParalela, tpcambio, 
									sMonedaBase, f55ca01, lstConfiguracionMensaje);
							
							//&& ======== si el metodo es tarjeta credito y es traslado, mostrar trasladar POS
							if(f55ca012[0].equals(MetodosPagoCtrl.TARJETA) && m.get("fdc_ObjTrasladoFacTrasladoPOS")!=null){
								lblEtTrasladoPOS.setStyle("display:inline");
								chkTrasladarPOS.setChecked(false);
								chkTrasladarPOS.setStyle("width: 20px; display: inline");
							}
						} else {
							cargarDevolucion(hFac, lstFacturasSelected,sUltimoNumero, iCajaId, sTasaJDE,sTasaParalela, tpcambio, 
									sMonedaBase, lstConfiguracionMensaje);
						}
						
					} else {// no hay monedas configuradas
						
						int i = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(lstConfiguracionMensaje, "CAJA", "REC_CONTADO", "MOSTRARRECIBO", "MOSTRARRECIBO_04");
						
						if(i<0)
						{
							sMensaje = sMensaje
									+  "No hay monedas configuradas!!!";
						}
						else
						{
							sMensaje = sMensaje
									+" " + lstConfiguracionMensaje[i].getDescripcionMensaje() + "<br/>";
						}
						
						throw new Exception(sMensaje);
					}
				} else {// no hay metodos de pago configurados para la caja
					
					int i = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(lstConfiguracionMensaje, "CAJA", "REC_CONTADO", "MOSTRARRECIBO", "MOSTRARRECIBO_05");
					
					if(i<0)
					{
						sMensaje = sMensaje
								+ " No hay métodos de pago configurados!!!<br/>";
					}
					else
					{
						sMensaje = sMensaje
								+ " " + lstConfiguracionMensaje[i].getDescripcionMensaje() + "<br/>";
					}
					throw new Exception(sMensaje);
				}
			}		
			
		} catch (Exception ex) {
			LogCajaService.CreateLog("mostrarRecibo", "ERR", ex.getMessage());

			int i = -1;
			ConfiguracionMensaje[] lstConfiguracionMensaje=null;
			try {
				lstConfiguracionMensaje = (ConfiguracionMensaje[]) m.get("lstConfiguracionMensaje");
				i = ConfiguracionMensaje.getConfiguracionMensajeXCodigo(lstConfiguracionMensaje, "CAJA", "REC_CONTADO", "MOSTRARRECIBO", "MOSTRARRECIBO_EX");
			}
			catch (Exception exx) {
				LogCajaService.CreateLog("mostrarRecibo", "ERR", exx.getMessage());
			}
			
			if(i<0)
				sMensaje = "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> " + ex.getMessage() + "<br/>";
			else
				sMensaje = "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> " + lstConfiguracionMensaje[i].getDescripcionMensaje().replace("@Error", ex.getMessage()) + "<br/>";
			
			dwValidacionFactura.setWindowState("normal");
			dwValidacionFactura.setStyle("height: 150px; visibility: visible; width: 370px");
			lblValidaFactura.setValue(sMensaje);
			
		}finally{
			HibernateUtilPruebaCn.closeSession(session);
			LogCajaService.CreateLog("mostrarRecibo", "INFO", "FIN METODO => mostrarRecibo");
		}
	}

/******************************************************************************************************************** */
/****************************CANCELAR RECIBO A FACTURA DE CONTADO******************************************************/
	public void cancelarRecibo(ActionEvent ev) {
		m.remove("fdc_ObjTrasladoFacTrasladoPOS");
		dwCancelar.setWindowState("normal");
	}

/** *************************************************************************************************************** */
	public void cancelarCancelarRecibo(ActionEvent ev) {
		dwCancelar.setWindowState("hidden");
	}

	public void cancelaRecibo(ActionEvent ev) {
		
		
		//++++++++++++++++++++++++++++++++++++++++
		//Agregado por LFonseca
		//2018-10-15
		//----------------------------------------
		Vf55ca01 caja = (Vf55ca01) ((List) m.get("lstCajas")).get(0);
		int caid = caja.getId().getCaid();	
		int iCajero = caja.getId().getCacati();
		
		List<Hfactura>  lstFacturasSelected = (List<Hfactura>) m.get("facturasSelected");
		
		VerificarFacturaProceso verificarFac = new VerificarFacturaProceso();
		for(Object o : lstFacturasSelected){
			Hfactura hfactura = (Hfactura)o;
			 
			String[] strResultado =  verificarFac.actualizarVerificarFactura(String.valueOf(caid), 
													String.valueOf(hfactura.getCodcli()), 
													String.valueOf(hfactura.getNofactura()), 
													hfactura.getTipofactura(), 
													"", 
													String.valueOf(iCajero));
			 
			
		}
		
		//---------------------------------------------
		//+++++++++++++++++++++++++++++++++++++++++++++
		
		
		SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
		dwCancelar.setWindowState("hidden");
		dwRecibo.setWindowState("hidden");
		gvHfacturasContado.dataBind();
		List lstSelectedFacs = new ArrayList();
		m.put("facturasSelected", lstSelectedFacs);
		m.remove("facturasSelected");
		m.remove("lstDatosTrack_Con");
		m.remove("sco_SbrDfr");
		m.remove("sco_SobrantePago");
		m.remove("sco_MpagoSobrante");
		
		srm.addSmartRefreshId(gvHfacturasContado.getClientId(FacesContext.getCurrentInstance()));
	}

/******************ESTABLECE EL TIPO DE RECIBO**********************************************/
	public void setTipoRecibo(ValueChangeEvent e) {
		SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
		String tipo = cmbTiporecibo.getValue().toString();
		m.put("mTipoRecibo", tipo);
		try {
			// Tipo Manual
			if (tipo.equals("MANUAL")) {
				txtNumRec.setStyle("");
				fechaRecibo = "";
				lblNumRecm.setValue("No. Manual: ");
				lblNumRecm.setStyle("display:inline");
				txtNumRec.setStyle("display:inline");
				txtFecham.setStyle("display:inline");
				Date dFechaActual = new Date();
				txtFecham.setValue(dFechaActual);

			} else {
				lblNumRecm.setValue("");
				lblNumrec = "Último Recibo: ";
				txtNumRec.setStyle("display:none");
				txtFecham.setStyle("display:none");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/** ************************************************************************************************** */
/*************ESTABLECE LAS REFERENCIA REQUERIDAS PARA EL METODO DE PAGO SELECCIONADO***********************************/
	public void setMetodosPago(ValueChangeEvent e) {
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		String codmetodo = cmbMetodosPago.getValue().toString();
		m.put("cMpagos", codmetodo);

		MonedaCtrl monCtrl = new MonedaCtrl();
		String[] sMoneda = null;

		Vf55ca01 vf01 = null;
		try {
			//obtener datos de factura
			vf01 = (Vf55ca01) ((List) m.get("lstCajas")).get(0);
			
			String codcaja = m.get("sCajaId").toString();
			restablecerEstilosPago();
			List lstMoneda = new ArrayList();
			//
			List lstFacturasSelected = (List) m.get("facturasSelected");
			Hfactura hFac = (Hfactura) lstFacturasSelected.get(0);
			// obtener monedas para el metodo de pago selecccionado
			sMoneda = monCtrl.obtenerMonedasxMetodosPago_Caja(Integer.parseInt(codcaja), hFac.getCodcomp(), codmetodo);
			for (int i = 0; i < sMoneda.length; i++) {
				lstMoneda.add(new SelectItem(sMoneda[i], sMoneda[i]));
			}
			m.put("lstMoneda", lstMoneda);
			txtReferencia1.setValue("");
			txtReferencia2.setValue("");
			txtReferencia3.setValue("");
			cambiarVistaMetodos(codmetodo,vf01);
			
			//--- Validaciones para mostrar check de traslado de POS
			if(codmetodo.equals(MetodosPagoCtrl.TARJETA)){
				if(m.get("fdc_ObjTrasladoFacTrasladoPOS")!=null){
//					Trasladofac tf = (Trasladofac)m.get("fdc_ObjTrasladoFacTrasladoPOS");
					lblEtTrasladoPOS.setStyle("display:inline");
					chkTrasladarPOS.setChecked(false);
					chkTrasladarPOS.setStyle("width: 20px; display: inline");
				}
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

 
	/*******************CAMBIAR VISTAS DE METODOS DE PAGO********************************************/
	
	public void cambiarVistaMetodos(String sCodMetodo, Vf55ca01 vf01) {
		try {
			lbletVouchermanual.setValue("Voucher Manual");
			lbletVouchermanual.setStyle("visibility:hidden; display:none");
			chkVoucherManual.setChecked(false);
			chkVoucherManual.setStyle("width:20px; visibility:hidden; display:none");
			lblEtTrasladoPOS.setStyle("display:none");
			chkTrasladarPOS.setStyle("width: 20px; display: none");
			chkTrasladarPOS.setChecked(false);
			ddlTipoMarcasTarjetas.setStyle("display:none");
			lblMarcaTarjeta.setStyle("display:none;");
			
			
			// metodo = TC
			if (sCodMetodo.equals(MetodosPagoCtrl.TARJETA)) {
				// Set to blank
				lblAfiliado.setValue("Afiliado:");
				lblReferencia1.setValue("Identificación:");
				//lblReferencia2.setValue("4 ult. Digitos:");
				lblReferencia2.setValue("Referencia:");
				lblReferencia3.setValue("");
				lblBanco.setValue("");
				chkVoucherManual.setChecked(false);

				// Set to visible
				ddlAfiliado.setStyle("display:inline; width: 153px");
				ddlBanco.setStyle("display:none;width: 153px");
				txtReferencia1.setStyle("display:inline");
				txtReferencia2.setStyle("display:inline");
				txtReferencia3.setStyle("visibility:hidden; display:none");
				lbletVouchermanual.setValue("V.Manual");
				lbletVouchermanual.setStyle("visibility:visible; display:inline");
				chkVoucherManual.setStyle("width:20px; visibility:visible; display:inline");

				ddlTipoMarcasTarjetas.dataBind();
				ddlTipoMarcasTarjetas.setStyle("width: 153px; display:inline");
				lblMarcaTarjeta.setStyle("display:inline;");
				
				if((vf01.getId().getCasktpos()+"").equals("Y")){
					lblTrack.setValue("Banda:");
					track.setStyle("display:inline");
					chkIngresoManual.setStyle("display:inline");
					
					if(chkIngresoManual.isChecked()){
						//mostrar Manuales
						lblNoTarjeta.setValue("Tarjeta:");
						lblNoTarjeta.setStyle("display:inline");
						txtNoTarjeta.setStyle("display:inline");
						lblFechaVenceT.setValue("Vence:");
						lblFechaVenceT.setStyle("display:inline");
						txtFechaVenceT.setStyle("display:inline");
						//quitar track
						lblReferencia2.setValue("");
						txtReferencia2.setStyle("display:none");
						
						lblTrack.setValue("");
						track.setStyle("display:none");
					}else{
						//quitar manuales
						lblNoTarjeta.setValue("");
						lblNoTarjeta.setStyle("display:none");
						txtNoTarjeta.setStyle("display:none");
						lblFechaVenceT.setValue("");
						lblFechaVenceT.setStyle("display:none");
						txtFechaVenceT.setStyle("display:none");
						lblReferencia2.setValue("");
						txtReferencia2.setStyle("display:none");
						//poner track
						//lblReferencia2.setValue("4 ult. Digitos:");
						//txtReferencia2.setStyle("display:inline");
						
						lblTrack.setValue("Banda:");
						track.setStyle("display:inline");
					}
				}					
				// metodo = CHK
			} else if (sCodMetodo.equals( MetodosPagoCtrl.CHEQUE)) {

				// Set to blank
				lblAfiliado.setValue("");
				lblReferencia1.setValue("No. Cheque:");
				lblReferencia2.setValue("Emisor:");
				lblReferencia3.setValue("Portador:");
				lblBanco.setValue("Banco:");

				// Set to visible
				ddlAfiliado.setStyle("display:none");
				ddlBanco.setStyle("display:inline;width: 153px");
				txtReferencia1.setStyle("display:inline");
				txtReferencia2.setStyle("display:inline");
				txtReferencia3.setStyle("display:inline");
				
				chkIngresoManual.setStyle("display:none");
				//quitar manuales
				lblNoTarjeta.setValue("");
				lblNoTarjeta.setStyle("display:none");
				txtNoTarjeta.setStyle("display:none");
				lblFechaVenceT.setValue("");
				lblFechaVenceT.setStyle("display:none");
				txtFechaVenceT.setStyle("display:none");
				lblTrack.setValue("");
				track.setStyle("display:none");

				// metodo = EFEC
			} else if (sCodMetodo.equals( MetodosPagoCtrl.EFECTIVO)) {

				// Set to blank
				lblAfiliado.setValue("");
				lblReferencia1.setValue("");
				lblReferencia2.setValue("");
				lblReferencia3.setValue("");
				lblBanco.setValue("");

				// Set to not visivble
				ddlAfiliado.setStyle("display:none");
				ddlBanco.setStyle("display:none");
				txtReferencia1.setStyle("visibility:hidden");
				txtReferencia2.setStyle("visibility:hidden");
				txtReferencia3.setStyle("visibility:hidden");
				
				chkIngresoManual.setStyle("display:none");
				//quitar manuales
				lblNoTarjeta.setValue("");
				lblNoTarjeta.setStyle("display:none");
				txtNoTarjeta.setStyle("display:none");
				lblFechaVenceT.setValue("");
				lblFechaVenceT.setStyle("display:none");
				txtFechaVenceT.setStyle("display:none");
				lblTrack.setValue("");
				track.setStyle("display:none");
			}
			// Transferencia bancaria
			else if (sCodMetodo.equals(MetodosPagoCtrl.TRANSFERENCIA)) {
				// Set to blank
				lblAfiliado.setValue("");
				lblReferencia1.setValue("Identificación:");
				lblReferencia2.setValue("Referencia:");
				lblReferencia3.setValue("");
				lblBanco.setValue("Banco:");

				// Set to visible
				ddlAfiliado.setStyle("display:none");
				ddlBanco.setStyle("display:inline;width: 153px");
				txtReferencia1.setStyle("display:inline");
				txtReferencia2.setStyle("display:inline");
				txtReferencia3.setStyle("visibility:hidden");
				
				chkIngresoManual.setStyle("display:none");
				//quitar manuales
				lblNoTarjeta.setValue("");
				lblNoTarjeta.setStyle("display:none");
				txtNoTarjeta.setStyle("display:none");
				lblFechaVenceT.setValue("");
				lblFechaVenceT.setStyle("display:none");
				txtFechaVenceT.setStyle("display:none");
				lblTrack.setValue("");
				track.setStyle("display:none");
			}
			// Deposito de banco
			else if (sCodMetodo.equals(MetodosPagoCtrl.DEPOSITO)) {
				// Set to blank
				lblAfiliado.setValue("");
				lblReferencia1.setValue("");
				lblReferencia2.setValue("Referencia:");
				lblReferencia3.setValue("");
				lblBanco.setValue("Banco:");

				chkIngresoManual.setStyle("display:none");
				// Set to visible
				ddlAfiliado.setStyle("display:none");
				ddlBanco.setStyle("display:inline;width: 153px");
				txtReferencia1.setStyle("display:none");
				txtReferencia2.setStyle("display:inline");
				txtReferencia3.setStyle("visibility:hidden");
				
				//quitar manuales
				lblNoTarjeta.setValue("");
				lblNoTarjeta.setStyle("display:none");
				txtNoTarjeta.setStyle("display:none");
				lblFechaVenceT.setValue("");
				lblFechaVenceT.setStyle("display:none");
				txtFechaVenceT.setStyle("display:none");
				lblTrack.setValue("");
				track.setStyle("display:none");
			}
			
			Object[] igObjects = new Object[]{ ddlTipoMarcasTarjetas, lblMarcaTarjeta };
			CodeUtil.refreshIgObjects(igObjects);
			
		} catch (Exception error) {
			error.printStackTrace(); 
		}
	}	 

	/****************************************************************************************/

/************ESTABLECER MONEDAS Y BUSCAR METODOS DE PAGO PARA LA MONEDA**********************************************************/
		public void setMoneda(ValueChangeEvent e) {
			Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
			String sMonedaPago = null, sCajaId = null;
			String[] sMetodosPago = null;
			MetodosPagoCtrl metPagoCtrl = new MetodosPagoCtrl();
			Metpago[] metpago = null;
			Tpararela[] tpcambio = null;
			Cafiliados[] cafiliado = null;
			AfiliadoCtrl afCtrl = new AfiliadoCtrl();
			int iCaid = 0;
			
			try { 
				
				Vf55ca01 caja = (Vf55ca01) ((List) m.get("lstCajas")).get(0);
				List lstFacturasSelected = (List) m.get("facturasSelected");
				Hfactura hFac = (Hfactura) lstFacturasSelected.get(0);

				tpcambio = (Tpararela[]) m.get("tpcambio");
				if (tpcambio == null) {
					lblMensajeValidacion.setValue("Tasa de cambio paralela no esta configurada");
					dwProcesa.setWindowState("normal");
					dwProcesa.setStyle("width:370px;height:160px");
					cmbMoneda.dataBind();
				} else {

					List lstMetodosPago = new ArrayList();
					sMonedaPago = cmbMoneda.getValue().toString();
					sCajaId = (String) m.get("sCajaId");
					iCaid = Integer.parseInt(sCajaId);
					
					
					// obtener metodos de pago por moneda
					sMetodosPago = metPagoCtrl.obtenerMetodoPagoxCaja_Moneda(iCaid, hFac.getCodcomp(), sMonedaPago);
					metpago = new Metpago[sMetodosPago.length];
					for (int i = 0; i < sMetodosPago.length; i++) {
						metpago = metPagoCtrl.obtenerDescripcionMetodosPago(sMetodosPago[i]);
						lstMetodosPago.add(new SelectItem(metpago[0].getId().getCodigo(), metpago[0].getId().getMpago()));
					}
					m.put("lstMetodosPago", lstMetodosPago);
					
					//----- Cargar los afiliados por caja, compañía y moneda.
					int iCajaDePos =0;
					String sLineaFactura = "";
					String sCodcomp = "";
					
					if(chkTrasladarPOS.isChecked()){
						if(m.get("fdc_ObjTrasladoFacTrasladoPOS") == null){
							lblMensajeValidacion.setValue("Error al obtener los datos de la factura por traslado");
							dwProcesa.setWindowState("normal");
							dwProcesa.setStyle("width:370px;height:160px");
							return;
						}else{
							Trasladofac tf = (Trasladofac)m.get("fdc_ObjTrasladoFacTrasladoPOS");
							sLineaFactura = tf.getCodunineg().trim();
							iCajaDePos = tf.getCaidorig();
							sCodcomp = tf.getCodcomp();
						}
					}else{
						sLineaFactura = hFac.getCodunineg().trim();
						iCajaDePos = iCaid;
						sCodcomp = hFac.getCodcomp();
					}
					
					sLineaFactura = CompaniaCtrl.leerLineaNegocio( sLineaFactura );
					
					//&& ===== obtener si la caja del pos usa socket o no.
					lstAfiliado = new ArrayList();
					if ( caja.getId().getCasktpos() == 'N' ) {
						lstAfiliado = this.getLstAfiliados(iCajaDePos, hFac
								.getCodcomp(), sLineaFactura, cmbMoneda.getValue()
								.toString());
					} else {
						lstAfiliado = getLstAfiliadosSocketPOS(iCajaDePos,
								hFac.getCodcomp(), sLineaFactura,
								cmbMoneda.getValue().toString());
					}
					m.put("lstAfiliado", lstAfiliado);
					ddlAfiliado.dataBind();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}


/******************************************************************************************************* */
/****************CERRRAR MENSAJE DE VALIDACION DE RECIBO**************************************************************************/
	public void cerrarProcesa(ActionEvent ev) {
		dwProcesa.setWindowState("hidden");
	}

/** ******************************************************************************************************* */
/************************PROCESAR SOLICITUD**********************************************************************/
	public void procesarSolicitud(ActionEvent e) {
		boolean validado = false, valido = false, bTarjeta = false;
		Divisas divisas = new Divisas();
		BigDecimal tasa = BigDecimal.ONE;
		Double total, monto, equiv,montototal=0.0;
		String sTotal, sIdMpago = "";
		int cont = 1;
		double montoRecibido = 0;
		Tpararela[] tpcambio = null;
		CtrlSolicitud solCtrl = new CtrlSolicitud();
		Tpararela[] tcPar = null;
		Tcambio[] tcJDE = null;
		String sVoucherManual;
		
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		String sMonedaBase = "";
		
		List<String> lstDatosTrack = null;List lstDatosTrack2 = null;
		
		try {
			
			dwSolicitud.setWindowState("hidden");
			
			Vf55ca01 vf01 = (Vf55ca01)((List) m.get("lstCajas")).get(0);
			boolean bUsaSocketPos  = vf01.getId().getCasktpos() == 'Y' ;
			boolean bIngresoManual = chkIngresoManual.isChecked();
			boolean bVoucherManual = chkVoucherManual.isChecked();
			
			int iCaidPos = vf01.getId().getCaid();
					
			validado = validarSolicitud();
			if (validado) {
				String metodo = getCmbMetodosPago().getValue().toString();
				String metDesc = "";
				String moneda = cmbMoneda.getValue().toString();
				String ref1 = getTxtReferencia1().getValue().toString().trim();
				String ref2 = getTxtReferencia2().getValue().toString().trim();
				String ref3 = getTxtReferencia3().getValue().toString().trim();
				String ref4 = "";
				String ref5 = "";
				String sTrack = "";
				String sTerminal = "";
				
				if ( bUsaSocketPos && bIngresoManual)
					ref2 = txtNoTarjeta.getValue().toString().trim();
				
				selectedMet = (ArrayList<MetodosPago>) m.get("lstPagos");
				
				sIdMpago = metodo;
				//determinar si es voucher Manual
				sVoucherManual = (chkVoucherManual.isChecked())?"1":"0";
				
				metodosGrid.setStyleClass("igGrid");// retablecer el estilo
				montototal = Double.parseDouble(m.get("montoIsertando").toString());
				monto = Double.parseDouble ( txtMonto.getValue().toString().trim() );				
				equiv = monto;
				List lstMetPago = (List) m.get("metpago");
				// cambiar ubicacion de referencias
				if (metodo.equals( MetodosPagoCtrl.CHEQUE)) {// cheque
					ref4 = ref3;
					ref3 = ref2;
					ref2 = ref1;
					ref1 = ddlBanco.getValue().toString().split("@")[1];
					
				} 
				if(metodo.equals(MetodosPagoCtrl.TARJETA)) {
					bTarjeta = true;
					ref3 = ref2;
					ref2 = ref1;
					ref1 = ddlAfiliado.getValue().toString();
					
					// si la caja usa socketPOS
					if ( bUsaSocketPos  && !bVoucherManual) {
						
						ref1 = ((ddlAfiliado.getValue().toString()).split(","))[0];
						sTerminal = ((ddlAfiliado.getValue().toString()).split(","))[1];
						if ( bIngresoManual ) {
							String snoT = txtNoTarjeta.getValue().toString().trim();
							ref3 = (snoT).substring(snoT.length() - 4, snoT.length());// 4 ult. digitos de tarjeta
							ref4 = txtNoTarjeta.getValue().toString();
							ref5 = txtFechaVenceT.getValue().toString();
						} else {
							sTrack = track.getValue().toString();
							sTrack = divisas.rehacerCadenaTrack(sTrack);
							lstDatosTrack = divisas.obtenerDatosTrack(sTrack);
							ref3 = lstDatosTrack.get(1).substring(lstDatosTrack.get(1).length()-4, lstDatosTrack.get(1).length());//4 ult. digitos de tarjeta
							if(m.get("lstDatosTrack_Con") == null){
								lstDatosTrack2 = new ArrayList();
								lstDatosTrack2.add(lstDatosTrack);
								m.put("lstDatosTrack_Con", lstDatosTrack2);
							}else{
								lstDatosTrack2 = (List)m.get("lstDatosTrack_Con");
								lstDatosTrack2.add(lstDatosTrack);
								m.put("lstDatosTrack_Con", lstDatosTrack2);
							}
						}
					}
					else if( bUsaSocketPos &&  bIngresoManual ){
						ref1 = ((ddlAfiliado.getValue().toString()).split(","))[0];
						sTerminal = ((ddlAfiliado.getValue().toString()).split(","))[1];	
					}
					
					
				} else if (metodo.equals(MetodosPagoCtrl.TRANSFERENCIA)) {// Transf. Elect.
					ref3 = ref2;
					ref2 = ref1;
					ref1 = ddlBanco.getValue().toString().split("@")[1];
				} else if (metodo.equals(MetodosPagoCtrl.DEPOSITO)) {// Deposito en banco
					// ref2 = ref1;
					ref1 = ddlBanco.getValue().toString().split("@")[1];
				}
				//
				int cant = 1;
				boolean flgpagos = true;
				Metpago[] metpago = null;

				// poner descripcion a metodo
				for (int m = 0; m < lstMetPago.size(); m++) {
					metpago = (Metpago[]) lstMetPago.get(m);
					if (metodo.trim().equals(metpago[0].getId().getCodigo().trim())) {
						metDesc = metpago[0].getId().getMpago().trim();
						break;
					}
				}

				List<Hfactura> lstFacturasSelected = (List) m.get("facturasSelected");
				Hfactura hFac = (Hfactura) lstFacturasSelected.get(0);

				sMonedaBase = cCtrl.sacarMonedaBase((F55ca014[])m.get("cont_f55ca014"), hFac.getCodcomp());

				if (hFac.getMoneda().equals(sMonedaBase)) {// domestica
					if (moneda.equals(sMonedaBase)) {
						tasa = BigDecimal.ONE;
						equiv = monto;
					} else {// foranea
						tcJDE = (Tcambio[]) m.get("tcambio");
						// buscar tasa de cambio JDE para moneda
						for (int l = 0; l < tcJDE.length; l++) {
							if (tcJDE[l].getId().getCxcrdc().equals(moneda)) {
								tasa = tcJDE[l].getId().getCxcrrd();
								break;
							}
						}
						equiv = divisas.roundDouble4(monto * tasa.doubleValue());
					}
				} else {// foranea
					if (moneda.equals(sMonedaBase)) {
						tcPar = (Tpararela[]) m.get("tpcambio");
						tasa = hFac.getTasa();
						equiv = divisas.roundDouble4(monto / tasa.doubleValue());
					} else {
						tasa = BigDecimal.ONE;
						equiv = monto;
					}
				}

				//---- Validar Voucher manual.
				sVoucherManual = (chkVoucherManual.isChecked())?"1":"0";
				if( chkTrasladarPOS.isChecked() &&
					m.containsKey("fdc_ObjTrasladoFacTrasladoPOS") ){
						iCaidPos = ((Trasladofac)m.get(
							"fdc_ObjTrasladoFacTrasladoPOS")).getCaidorig();
				}
				
				//&& ============ Datos de tipo de marca tarjeta
				String codigomarcatarjeta = "";
				String marcatarjeta = "";
				
				if( bTarjeta ){
					codigomarcatarjeta = ddlTipoMarcasTarjetas.getValue().toString().split("@")[0];
					marcatarjeta = ddlTipoMarcasTarjetas.getValue().toString().split("@")[1];
				}
				
				
				if (selectedMet != null) {
					MetodosPago[] mpagos = selectedMet.toArray(
								new MetodosPago[selectedMet.size()]);

					for (int i = 0; i < mpagos.length; i++) {
						if (mpagos[i].getMetodo().equals(metodo)
								&& mpagos[i].getMoneda().equals(moneda)
								&& mpagos[i].getReferencia().equals(ref1)
								&& mpagos[i].getReferencia2().equals(ref2)
								&& mpagos[i].getReferencia3().equals(ref3)
								&& mpagos[i].getReferencia4().equals(ref4)
								&& mpagos[i].getVmanual().equals(sVoucherManual)
								&& mpagos[i].getICaidpos()==iCaidPos) {

							monto = monto + mpagos[i].getMonto();
							equiv = equiv + mpagos[i].getEquivalente();

							mpagos[i].setMonto(monto);
							mpagos[i].setEquivalente(equiv);
							
							mpagos[i].setReferencia5("");
							if (bTarjeta && bUsaSocketPos  && !bVoucherManual) {
								mpagos[i].setTrack(sTrack);
								mpagos[i].setTerminal(sTerminal);
								mpagos[i].setReferencia5(ref5);
								mpagos[i].setMontopos(monto);
								mpagos[i].setVmanual("2");
							}

							mpagos[i].setReferencia6("");
							mpagos[i].setReferencia7("");
							mpagos[i].setNombre("");
							
							mpagos[i].setCodigomarcatarjeta(codigomarcatarjeta);
							mpagos[i].setMarcatarjeta(marcatarjeta);
							
							selectedMet.remove(i);
							selectedMet.add(i, mpagos[i]);
							flgpagos = false;
						}
					}
				} else {
					MetodosPago metpagos = new MetodosPago(metDesc, metodo,
							moneda, monto, tasa, equiv, ref1, ref2, ref3, ref4,
							sVoucherManual, iCaidPos);
					
					metpagos.setReferencia5("");
					if (bTarjeta &&  bUsaSocketPos  && !bVoucherManual) {
						metpagos.setTrack(sTrack);
						metpagos.setTerminal(sTerminal);
						metpagos.setReferencia5(ref5);
						metpagos.setMontopos(monto);
						metpagos.setVmanual("2");
					} 
					metpagos.setReferencia6("");
					metpagos.setReferencia7("");
					metpagos.setNombre("");
					
					metpagos.setCodigomarcatarjeta(codigomarcatarjeta);
					metpagos.setMarcatarjeta(marcatarjeta);
					
					selectedMet = new ArrayList();
					selectedMet.add(metpagos);
					flgpagos = false;
				}

				if (flgpagos) {
					MetodosPago metpagos = new MetodosPago(metDesc, metodo,
							moneda, monto, tasa, equiv, ref1, ref2, ref3, ref4,
							sVoucherManual, iCaidPos);
					
					metpagos.setReferencia5("");
					if (bTarjeta && bUsaSocketPos  && !bVoucherManual) {
						metpagos.setTrack(sTrack);
						metpagos.setTerminal(sTerminal);
						metpagos.setReferencia5(ref5);
						metpagos.setMontopos(monto);
						metpagos.setVmanual("2");
					} 
					
					metpagos.setCodigomarcatarjeta(codigomarcatarjeta);
					metpagos.setMarcatarjeta(marcatarjeta);
					
					metpagos.setReferencia6("");
					metpagos.setReferencia7("");
					metpagos.setNombre("");
					selectedMet.add(metpagos);
				}

				montoRecibido = calcularElMontoRecibido(selectedMet, hFac, sMonedaBase);

				// determinar como dar el cambio
				determinarCambio(selectedMet, hFac, montoRecibido, sMonedaBase);

				m.put("montoRecibibo", montoRecibido);
				m.put("lstPagos", selectedMet);
				m.put("mpagos", selectedMet);
				
				metodosGrid.dataBind();
				SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
				srm.addSmartRefreshId(metodosGrid.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(txtNoTarjeta.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(txtFechaVenceT.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(track.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(ddlAfiliado.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(lblPendienteDomestico.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(txtPendienteDomestico.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(lnkCambio.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(txtCambioForaneo.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(lblCambioDomestico.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(txtCambioDomestico.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(dwProcesa.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(dwSolicitud.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(txtMonto.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(txtReferencia1.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(txtReferencia2.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(txtReferencia3.getClientId(FacesContext.getCurrentInstance()));
				
				List lstSolicitud =  new ArrayList() ;
				if (m.get("lstSolicitud") != null) 
					lstSolicitud = (List) m.get("lstSolicitud");
				
				// info del autorizador 0 = correo; 1 en adelante = nombre
				String[] sAutorizador = cmbAutoriza.getValue().toString().split(" ");
				String sNomAut = "";
				for (int a = 1; a < sAutorizador.length; a++) {
					sNomAut = sNomAut + sAutorizador[a] + " ";
				}
				Solicitud solicitud = new Solicitud();
				SolicitudId solicitId = new SolicitudId();

				solicitId.setReferencia(txtReferencia.getValue().toString());
				solicitud.setAutoriza(Integer.parseInt(sAutorizador[1].trim()));
				solicitud.setObs(txtObs.getValue().toString());
				solicitud.setFecha((Date) txtFecha.getValue());
				solicitud.setMoneda(moneda);
				solicitud.setMetododesc(metodo);
				solicitud.setMpago(sIdMpago);
				solicitud.setMonto(new BigDecimal(String.valueOf(montototal)));

				solicitud.setId(solicitId);
				lstSolicitud.add(solicitud);
				m.put("lstSolicitud", lstSolicitud);

				getTxtMonto().setValue("");
				getTxtReferencia1().setValue("");
				getTxtReferencia2().setValue("");
				getTxtReferencia3().setValue("");
				track.setValue("");
				txtCambioForaneo.setStyleClass("frmInput2");
				txtNoTarjeta.setValue("");
				txtFechaVenceT.setValue("");

				dwSolicitud.setWindowState("hidden");

				// enviar correo a cajero o suplente, responsable de caja y
				// autorizador
				String sFrom="";
				EmpleadoCtrl empCtrl = new EmpleadoCtrl();
				List lstCajas = (List) m.get("lstCajas");
				Vf55ca01 f55ca01 = (Vf55ca01) lstCajas.get(0);
				List lstAutorizadores = (List) m.get("lstAutorizadores");
				
				//------- Agregar Copias al Cajero y autorizadores.
				Vautoriz[] vAut =(Vautoriz[])m.get("sevAut");
				int iCodCajero = vAut[0].getId().getCodreg();
				List<String> lstCc = new ArrayList<String>();
				
				Vf0101 vf0101 = empCtrl.buscarEmpleadoxCodigo2(iCodCajero);
				if(vf0101!=null){
					if(divisas.validarCuentaCorreo(vf0101.getId().getWwrem1().trim())){
						sFrom = vf0101.getId().getWwrem1().trim();
						lstCc.add(sFrom);
					}else sFrom = "webmaster@casapellas.com.ni";
				}else sFrom = "webmaster@casapellas.com.ni";
					
				for (Iterator iter = lstAutorizadores.iterator(); iter.hasNext();) {
					Vf0101 f01 = (Vf0101) iter.next();
					if(divisas.validarCuentaCorreo(f01.getId().getWwrem1().trim()))
						lstCc.add(f01.getId().getWwrem1().trim());
				}
				String sSucursal = m.get("sNombreSucursal").toString();
				String sCajero = m.get("sNombreEmpleado").toString();
				String sSubject = f55ca01.getId().getCaname().trim()+ ": Referencia de Autorización";
				String sMontoAutorizado = divisas.formatDouble(Double.parseDouble(m.get("monto").toString()));
				String sMoneda = cmbMoneda.getValue().toString();
				String sMetodoPago = cmbMetodosPago.getValue().toString();

				List lstMetPago2 = (List) m.get("metpago");
				Metpago[] metpago2 = null;

				// poner descripcion a metodo
				for (int m = 0; m < lstMetPago2.size(); m++) {
					metpago2 = (Metpago[])lstMetPago2.get(m);
					if (sMetodoPago.trim().equals(metpago2[0].getId().getCodigo().trim())) {
						sMetodoPago = metpago2[0].getId().getMpago().trim();
						break;
					}
				}
				
				//---Determinar los datos de caja y empleado para el correo
				Vautoriz[] vaut  = (Vautoriz[])m.get("sevAut");
				int iCodcajero = vaut[0].getId().getCodreg();
				String sNombreFrom = f55ca01.getId().getCaname();
				
				Vf0101 f01 = new EmpleadoCtrl().buscarEmpleadoxCodigo2(iCodcajero);
				if(f01!=null){
					sNombreFrom = f01.getId().getAbalph().trim();
					sFrom = f01.getId().getWwrem1().trim().toUpperCase();
					if(!new Divisas().validarCuentaCorreo(sFrom))
						sFrom = "webmaster@casapellas.com.ni";
				}else{
					sFrom = "webmaster@casapellas.com.ni";
					sNombreFrom = f55ca01.getId().getCaname();
				}
				
				solCtrl.enviarCorreo(sAutorizador[0],sFrom,sNombreFrom, lstCc,
									txtReferencia.getValue().toString(),
									txtObs.getValue().toString(),sSubject,
									sCajero,sNomAut,sSucursal,f55ca01.getId().getCaname().trim(),
									sMontoAutorizado +" "+ sMoneda,sMetodoPago,
									hFac.getCodcli()+""+hFac.getNomcli());
				
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

/***********VALIDAR DATOS DEL DIALOGO DE LA SOLICITUD DE AUTORIZACION*******************************************/
	public boolean validarSolicitud() {
		boolean validado = true;
		// validar referencia
		try {
			restableceEstiloAutorizacion();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Matcher matFecha = null, matCorreo = null;
			Date dFecha = null;
			String sFecha = null;
			String[] sCorreo = null;
			// expresion regular de fecha
			if (txtFecha.getValue() != null) {
				dFecha = (Date) txtFecha.getValue();
				sFecha = sdf.format(dFecha);
				Pattern pFecha = Pattern.compile("^[0-3]?[0-9](/|-)[0-2]?[0-9](/|-)[1-2][0-9][0-9][0-9]$");
				matFecha = pFecha.matcher(sFecha);
			}
			// expresion regular de correo electronico
			sCorreo = cmbAutoriza.getValue().toString().split(" ");
			Pattern pCorreo = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$");
			matCorreo = pCorreo.matcher(sCorreo[0].toUpperCase());
			// expresion regular valores alfanumericos
			Matcher matRef = null;
			Matcher matObs = null;
			if (txtObs.getValue() != null) {
				String sObs = txtObs.getValue().toString();
				Pattern pAlfa = Pattern.compile("^[A-Za-z0-9-.;,\\p{Blank}]+$");
				matObs = pAlfa.matcher(sObs);
			}
			if (txtReferencia.getValue() != null) {
				String sRef = txtReferencia.getValue().toString();
				Pattern pAlfa = Pattern.compile("^[A-Za-z0-9-_\\p{Blank}]+$");
				matRef = pAlfa.matcher(sRef);
			}
			// validar Referencia
			if (txtReferencia.getValue() == null || txtReferencia.getValue().toString().trim().equals("")) {
				lblMensajeAutorizacion.setValue("La referencia es requerida");
				txtReferencia.setStyleClass("frmInput2Error");
				validado = false;
			} else if (!matRef.matches()) {
				lblMensajeAutorizacion.setValue("El campo Referencia contiene caracteres invalidos");
				txtReferencia.setStyleClass("frmInput2Error");
				validado = false;
			} else if (txtReferencia.getValue().toString().length() > 50) {
				validado = false;
				lblMensajeAutorizacion.setValue("La cantidad de caracteres en el campo referencia es muy alta (lim. 50)");
				txtReferencia.setStyleClass("frmInput2Error");
			}
			// validar fecha
			else if (txtFecha.getValue() == null
					|| txtFecha.getValue().toString().trim().equals("")) {
				lblMensajeAutorizacion.setValue("La fecha es requerida");
				txtFecha.setStyleClass("frmInput2Error2");
				validado = false;
			}
			// validar observacion
			else if (txtObs.getValue() == null || txtObs.getValue().toString().trim().equals("")) {
				lblMensajeAutorizacion.setValue("La Observación es requerida");
				txtObs.setStyleClass("frmInput2Error");
				validado = false;
			} else if (!matObs.matches()) {
				lblMensajeAutorizacion.setValue("El campo Observación contiene caracteres invalidos");
				txtObs.setStyleClass("frmInput2Error");
				validado = false;
			} else if (txtObs.getValue().toString().length() > 250) {
				validado = false;
				lblMensajeAutorizacion.setValue("La cantidad de caracteres en el campo Observación es muy alta (lim. 250)");
				txtObs.setStyleClass("frmInput2Error");
			} else if (matFecha == null || !matFecha.matches()) {
				lblMensajeAutorizacion.setValue("La fecha no es valida");
				txtFecha.setStyleClass("frmInput2Error2");
				validado = false;
			}
			// validar correo
			else if (matCorreo == null || !matCorreo.matches()) {
				lblMensajeAutorizacion.setValue("El autorizador seleccionado no tiene correo electrónico configurado");
				cmbAutoriza.setStyleClass("frmInput2Error2");
				validado = false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return validado;
	}

/********************************************************************************************************* */
	public void onCerrarAutorizacion(StateChangeEvent e) {
		restableceEstiloAutorizacion();
		txtFecha.setValue("");
		txtReferencia.setValue("");
		txtObs.setValue("");
		dwSolicitud.setWindowState("hidden");
	}

/*** *******************RESTABLECE EL ESTILO DE LOS CAMPOS DE LA AUTORIZACION******************************/
	public void restableceEstiloAutorizacion() {
		txtObs.setStyleClass("frmInput2");
		txtReferencia.setStyleClass("frmInput2");
		txtFecha.setStyleClass("");
		cmbAutoriza.setStyleClass("frmInput2");

	}

/*******************CIERRA EL CUADRO DE DIALOGO Y CANCELA LA SOLICITUD***********************************************************************/
	public void cancelarSolicitud(ActionEvent e) {
		restableceEstiloAutorizacion();
		txtFecha.setValue("");
		txtReferencia.setValue("");
		txtObs.setValue("");
		dwSolicitud.setWindowState("hidden");
	}
/**************************AGREGAR METODOS DE PAGO A LA LISTA DESDE LINK* AGREGAR*********************************************/
	public void registrarPago(ActionEvent e) {
		Divisas d = new Divisas();
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		
		BigDecimal tasa = BigDecimal.ONE;
		
		Tpararela[] tcPar = null;
		Tcambio[] tcJDE = null;
		
		double total = 0; 
		double monto = 0;
		double equiv = 0;
		double montoRecibido = 0;
		
		int cont = 1;
		int iCaidPos = 0;
		
		String sTotal = new String("");
		String sMonedaBase = new String("");
		String sVoucherManual = new String("");
		
		boolean valido = false;
		boolean bTarjeta = false;
		List lstMoneda = null;
		
		Vf55ca01 vf01 = null;
		
		List<String> lstDatosTrack  = null;
		List lstDatosTrack2 = null;
		MetodosPago metpagos = null;
		ArrayList<Hfactura> lstFacturasSelected = null;
		
		try {	
			
			String metodo = getCmbMetodosPago().getValue().toString();

			metodo = cmbMetodosPago.getValue().toString();
			if(metodo.compareTo("MP") == 0) 
				return;
			
			
			SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
			
			vf01 = (Vf55ca01) ((List) m.get("lstCajas")).get(0);
			iCaidPos = vf01.getId().getCaid();
			
			lstFacturasSelected = (ArrayList<Hfactura>) m.get("facturasSelected");
			Hfactura hFac = lstFacturasSelected.get(0);
			lstMoneda = (List)m.get("lstMoneda");

			boolean bUsaSocketPos  =  vf01.getId().getCasktpos() == 'Y';
			boolean bIngresoManual =  chkIngresoManual.isChecked();
			boolean bVoucherManual =  chkVoucherManual.isChecked();
			sVoucherManual		   = (chkVoucherManual.isChecked())? "1":"0";
			
			//&& ==== Validar que no se haya alcanzado el metodo de pago.
			String sAplicado  = txtMontoAplicar.getValue().toString().replace(",", "");
			String  sRecibido = txtMontoRecibido.getValue().toString().replace(",", "");
			
			boolean aplicadonacion = ( CodeUtil.getFromSessionMap("fdc_MontoTotalEnDonacion") != null );
			
			if(new BigDecimal(sAplicado).compareTo(new BigDecimal(sRecibido)) != 1 &&  !aplicadonacion) {
				lblMensajeValidacion.setValue("El monto total de los metodos de pago ingresados supera el monto a aplicar.");
				dwProcesa.setWindowState("normal");
				dwProcesa.setStyle("width:370px;height:160px");
				SmartRefreshManager.getCurrentInstance()
						.addSmartRefreshId(dwProcesa.getClientId(
								FacesContext.getCurrentInstance()));
				return;
			}
			
			sMonedaBase = cCtrl.sacarMonedaBase((F55ca014[])m.get("cont_f55ca014"), hFac.getCodcomp());
			
			String metDesc = "";
			String moneda = cmbMoneda.getValue().toString();
			String ref1 = getTxtReferencia1().getValue().toString().trim();			
			String ref2 = getTxtReferencia2().getValue().toString().trim();
			String ref3 = getTxtReferencia3().getValue().toString().trim();
			String ref4 = "";
			String ref5 = "";
			String sTrack = "";
			
			String sTerminal = "";
			if( bUsaSocketPos  && bIngresoManual)
				ref2 = txtNoTarjeta.getValue().toString().trim();
			
			valido = validarPago(moneda, sMonedaBase, lstMoneda, hFac,
						bUsaSocketPos, bIngresoManual,bVoucherManual,
						lstFacturasSelected);
			
			if(!valido){
				//blMensajeValidacion.setValue("Prueba de mensaje de vida");
				dwProcesa.setWindowState("normal");
				dwProcesa.setStyle("width:370px;height:160px");
				
				srm.addSmartRefreshId(dwProcesa.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(dwSolicitud.getClientId(FacesContext.getCurrentInstance()));
				return;
			}
			
			selectedMet = (ArrayList<MetodosPago>) m.get("lstPagos");
			
			if (m.get("tpcambio") == null) {
				lblMensajeValidacion.setValue("Tasa de cambio paralela no esta configurada");
				dwProcesa.setWindowState("normal");
				dwProcesa.setStyle("width:370px;height:160px");
				SmartRefreshManager.getCurrentInstance()
						.addSmartRefreshId(dwProcesa.getClientId(
								FacesContext.getCurrentInstance()));
				return;
			}
			
			
			
			
			//&& ======= Conservar el cambio pendiente en domestica.
			double dCambioDom = 0;
			boolean bCambioDom = false;
			
			if( txtPendienteDomestico.getValue() != null && 
				txtPendienteDomestico.getValue().toString().compareTo("") != 0){
				dCambioDom = d.formatStringToDouble(
				txtPendienteDomestico.getValue().toString());
				bCambioDom = true;
			}
			
			metodosGrid.setStyleClass("igGrid");
			monto = Double.parseDouble(txtMonto.getValue().toString());
			

			//&& ============= restar el monto de la donacion antes de la validacion del monto del pago.
			BigDecimal montooriginal = new BigDecimal(Double.toString(monto));
			BigDecimal montoNeto = new BigDecimal(Double.toString(monto));
			BigDecimal montoendonacion = BigDecimal.ZERO;
			
			if(aplicadonacion){
				montoendonacion = new BigDecimal( String.valueOf( CodeUtil.getFromSessionMap("fdc_MontoTotalEnDonacion") ) );
				montoNeto = montooriginal.subtract(montoendonacion) ;
				monto = montoNeto.doubleValue() ;
			}
			//=========================================================================================
			
			equiv = monto;
			List lstMetPago = (List) m.get("metpago");
		
			if (metodo.equals( MetodosPagoCtrl.CHEQUE)) {// cheque
				ref4 = ref3;
				ref3 = ref2;
				ref2 = ref1;
				ref1 = ddlBanco.getValue().toString().split("@")[1];
			} 
			else if (metodo.equals(MetodosPagoCtrl.TARJETA)) {
				bTarjeta = true;
				ref3 = ref2;
				ref2 = ref1;
				ref1 = ddlAfiliado.getValue().toString();
				
				if( bUsaSocketPos && !bVoucherManual){
					ref1 = ((ddlAfiliado.getValue().toString()).split(","))[0];
					sTerminal = ((ddlAfiliado.getValue().toString()).split(","))[1];						
					
					if( bIngresoManual ){
						String snoT = txtNoTarjeta.getValue().toString().trim();
						ref3 = (snoT).substring(snoT.length()-4, snoT.length());//4 ult. digitos de tarjeta
						ref4 = txtNoTarjeta.getValue().toString();	
						ref5 = txtFechaVenceT.getValue().toString();	
					}else{
						sTrack = track.getValue().toString(); 
						sTrack = d.rehacerCadenaTrack(sTrack);
						lstDatosTrack = d.obtenerDatosTrack(sTrack);
						
						ref3 = lstDatosTrack.get(1).substring(lstDatosTrack.get(1).length()-4, lstDatosTrack.get(1).length());//4 ult. digitos de tarjeta
						if(m.get("lstDatosTrack_Con") == null){
							lstDatosTrack2 = new ArrayList();
							lstDatosTrack2.add(lstDatosTrack);
							m.put("lstDatosTrack_Con", lstDatosTrack2);
						}else{
							lstDatosTrack2 = (List)m.get("lstDatosTrack_Con");
							lstDatosTrack2.add(lstDatosTrack);
							m.put("lstDatosTrack_Con", lstDatosTrack2);
						}
					}
				}					
				else if( bUsaSocketPos && bVoucherManual){
					ref1 = ((ddlAfiliado.getValue().toString()).split(","))[0];
					sTerminal = ((ddlAfiliado.getValue().toString()).split(","))[1];	
				}
				
			} else if (metodo.equals(MetodosPagoCtrl.TRANSFERENCIA)) {// Transf. Elect.
				ref3 = ref2;
				ref2 = ref1;
				ref1 = ddlBanco.getValue().toString().split("@")[1];
			} else if (metodo.equals(MetodosPagoCtrl.DEPOSITO)) {// Deposito en banco
				// ref2 = ref1;
				ref1 = ddlBanco.getValue().toString().split("@")[1];
			}
			//
			int cant = 1;
			boolean flgpagos = true;
			Metpago[] metpago = null;

			// poner descripcion a metodo
			for (int m = 0; m < lstMetPago.size(); m++) {
				metpago = (Metpago[]) lstMetPago.get(m);
				if (metodo.trim().equals(metpago[0].getId().getCodigo().trim())) {
					metDesc = metpago[0].getId().getMpago().trim();
					break;
				}
			}
			
			// calcular equivalente en dependencia de moneda de factura y
			// moneda de pago
			if (hFac.getMoneda().equals(sMonedaBase)) {// domestica
				if (moneda.equals(sMonedaBase)) {
					tasa = BigDecimal.ONE;
					equiv = monto;
				} else {// foranea
					tcJDE = (Tcambio[]) m.get("tcambio");
					tcPar = (Tpararela[]) m.get("tpcambio");

					tasa = tcPar[0].getId().getTcambiom();
					equiv = d.roundDouble4(monto * tcPar[0].getId().getTcambiom().doubleValue());
				}
			} else {// foranea
				if (moneda.equals(sMonedaBase)) {
					tcPar = (Tpararela[]) m.get("tpcambio");
					tasa = tcPar[0].getId().getTcambiom(); //hFac.getTasa();
					equiv =  d.roundDouble4(monto / tasa.doubleValue());
				} else {
					tasa = BigDecimal.ONE;
					equiv = monto;
				}
			}

			//---- Validar Voucher manual.
			if(chkTrasladarPOS.isChecked() && 
				m.containsKey("fdc_ObjTrasladoFacTrasladoPOS"))
				iCaidPos = ((Trasladofac)m.get
					("fdc_ObjTrasladoFacTrasladoPOS")).getCaidorig();
				
			
			String codigomarcatarjeta = "";
			String marcatarjeta = "";
			
			if( bTarjeta ){
				codigomarcatarjeta = ddlTipoMarcasTarjetas.getValue().toString().split("@")[0];
				marcatarjeta = ddlTipoMarcasTarjetas.getValue().toString().split("@")[1];
			}
			
			
			metpagos = null;
			if (selectedMet != null) {
				MetodosPago[] mpagos = selectedMet
						.toArray(new MetodosPago[selectedMet.size()]);
				
				for (int i = 0; i < mpagos.length; i++) {
					if (mpagos[i].getMetodo().equals(metodo)
							&& mpagos[i].getMoneda().equals(moneda)
							&& mpagos[i].getReferencia().equals(ref1)
							&& mpagos[i].getReferencia2().equals(ref2)
							&& mpagos[i].getReferencia3().equals(ref3)
							&& mpagos[i].getReferencia4().equals(ref4)
							&& mpagos[i].getVmanual().equals(sVoucherManual)
							&& mpagos[i].getICaidpos() == iCaidPos) {

						monto = monto + mpagos[i].getMonto();
						equiv = equiv + mpagos[i].getEquivalente();

						mpagos[i].setMonto(monto);
						mpagos[i].setMontorecibido(new BigDecimal(monto));

						mpagos[i].setEquivalente(equiv);
						if(bTarjeta && bUsaSocketPos && !bVoucherManual){
							mpagos[i].setTrack(sTrack);
							mpagos[i].setTerminal(sTerminal);
							mpagos[i].setReferencia5(ref5);
							mpagos[i].setMontopos(monto);
							mpagos[i].setVmanual("2");
						}else{
							mpagos[i].setReferencia5("");
						}
						
						mpagos[i].setReferencia6("");
						mpagos[i].setReferencia7("");
						mpagos[i].setNombre("");
						selectedMet.remove(i);
						selectedMet.add(i, mpagos[i]);
						flgpagos = false;
					}
				}
			} else {
				metpagos = new MetodosPago(metDesc,metodo,moneda,monto,tasa,equiv,
									ref1,ref2,ref3,ref4,sVoucherManual,iCaidPos);
				
				
				metpagos.setMontorecibido(montooriginal );
				metpagos.setMontoendonacion(montoendonacion);
				metpagos.setIncluyedonacion(aplicadonacion);
				
				if(aplicadonacion){
					metpagos.setDonaciones(
						new ArrayList<DncIngresoDonacion>((ArrayList<DncIngresoDonacion>)
								CodeUtil.getFromSessionMap("fdc_lstDonacionesRecibidas")) ) ;
				}
				
				if (bTarjeta && bUsaSocketPos && !bVoucherManual) {
					metpagos.setTrack(sTrack);
					metpagos.setTerminal(sTerminal);
					metpagos.setReferencia5(ref5);
					metpagos.setMontopos(monto);
					metpagos.setVmanual("2");
				} else {
					metpagos.setReferencia5("");
				}					
				metpagos.setReferencia6("");
				metpagos.setReferencia7("");
				metpagos.setNombre("");
				
				metpagos.setCodigomarcatarjeta(codigomarcatarjeta);
				metpagos.setMarcatarjeta(marcatarjeta);
				
				selectedMet = new ArrayList();
				selectedMet.add(metpagos);
				flgpagos = false;
			}

			if (flgpagos) {
				metpagos = new MetodosPago(metDesc,metodo,moneda,monto,tasa,equiv,
														ref1,ref2,ref3,ref4,sVoucherManual,iCaidPos);
				
				metpagos.setMontorecibido(montooriginal );
				metpagos.setMontoendonacion(montoendonacion);
				metpagos.setIncluyedonacion(aplicadonacion);
			
				if(aplicadonacion){
					metpagos.setDonaciones(
						new ArrayList<DncIngresoDonacion>((ArrayList<DncIngresoDonacion>)
								CodeUtil.getFromSessionMap("fdc_lstDonacionesRecibidas")) ) ;
				}
				
				
				if (bTarjeta && bUsaSocketPos && !bVoucherManual) {
					metpagos.setTrack(sTrack);
					metpagos.setTerminal(sTerminal);
					metpagos.setReferencia5(ref5);
					metpagos.setMontopos(monto);
					metpagos.setVmanual("2");
				} else {
					metpagos.setReferencia5("");
				}
				
				metpagos.setReferencia6("");
				metpagos.setReferencia7("");
				metpagos.setNombre("");
				
				metpagos.setCodigomarcatarjeta(codigomarcatarjeta);
				metpagos.setMarcatarjeta(marcatarjeta);
				
				selectedMet.add(metpagos);
			}
			
			if(aplicadonacion){
				CodeUtil.removeFromSessionMap(new String[] { "fdc_MontoTotalEnDonacion","fdc_lstDonacionesRecibidas" });

			}
			
			
			//&& ========= Guardar el objeto del método de pago que genera el sobrante ======== &&//
			if(m.get("sco_SobrantePago") != null)
				m.put("sco_MpagoSobrante", metpagos);
			else
				m.remove("sco_MpagoSobrante");

			//&& ==== Determinar el monto recibido y verificar si hay cambio.
			montoRecibido = calcularElMontoRecibido(selectedMet, hFac, sMonedaBase);
			boolean bEfectivo = false;
			List<MetodosPago>lstMetodos = (ArrayList<MetodosPago>)selectedMet;
			for (MetodosPago pago : lstMetodos) {
				if(pago.getMetodo().trim().toLowerCase().equals( MetodosPagoCtrl.EFECTIVO)){
					bEfectivo = true;
					break;
				}
			}
			
			//&& ======== determinar como dar el cambio
			double dMontoAplicar = d.formatStringToDouble(txtMontoAplicar.getValue().toString());
			if(bEfectivo || d.roundDouble(montoRecibido) < dMontoAplicar ){
				m.remove("sco_SbrDfr");
				m.remove("sco_SobrantePago");
				m.remove("sco_MpagoSobrante");
				determinarCambio(selectedMet, hFac, montoRecibido,sMonedaBase);
			}else{
				if( d.roundDouble(montoRecibido) == dMontoAplicar ){
					txtCambio.setValue("0.00");
					lblPendienteDomestico.setValue("");
					txtPendienteDomestico.setValue("");
				}
				if(m.get("sco_SobrantePago") != null){
					lblCambio.setValue("Cambio: "+hFac.getMoneda());
					txtCambio.setValue(d.formatDouble(0));
					lblPendienteDomestico.setValue("");
					txtPendienteDomestico.setValue("");
					lblCambioDomestico.setValue("");
					txtCambioDomestico.setValue("");
					txtMontoRecibido.setValue(txtMontoAplicar.getValue().toString());
					
					srm.addSmartRefreshId(lblCambio.getClientId(FacesContext.getCurrentInstance()));
					srm.addSmartRefreshId(txtCambio.getClientId(FacesContext.getCurrentInstance()));
				}
			}
			m.put("lstPagos", selectedMet);
			m.put("mpagos", selectedMet);

			txtMonto.setValue("");
			txtReferencia1.setValue("");
			txtReferencia2.setValue("");
			txtReferencia3.setValue("");
			track.setValue("");
			txtCambioForaneo.setStyleClass("frmInput2");
			txtNoTarjeta.setValue("");	
			txtFechaVenceT.setValue("");	
			
			if(selectedMet != null && !selectedMet.isEmpty())
				metodosGrid.dataBind();
			
			srm.addSmartRefreshId(metodosGrid.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(txtNoTarjeta.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(txtFechaVenceT.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(track.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(ddlAfiliado.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblPendienteDomestico.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(txtPendienteDomestico.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lnkCambio.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(txtCambioForaneo.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblCambioDomestico.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(txtCambioDomestico.getClientId(FacesContext.getCurrentInstance()));
		
			srm.addSmartRefreshId(txtMonto.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(txtReferencia1.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(txtReferencia2.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(txtReferencia3.getClientId(FacesContext.getCurrentInstance()));


			//&& ========== validacion de factura de repuestos. (problemas con vueltos por centavos)
			BigDecimal totaldomFacs = BigDecimal.ZERO;
			BigDecimal montocalcdomfac 	= BigDecimal.ZERO;
			BigDecimal difMtoFac  	= BigDecimal.ZERO;
			BigDecimal difPagFact	= BigDecimal.ZERO;
			
			for (Hfactura hfac1 : lstFacturasSelected) {
				totaldomFacs = totaldomFacs.add( new BigDecimal(hfac1.getEquiv()) );  //Cambio hecho por lfonseca
				
				if(totaldomFacs.compareTo(BigDecimal.ZERO) == 0)
				{
					totaldomFacs = totaldomFacs.add( hfac1.getTotaldomtmp() );
				}
				
				montocalcdomfac  = montocalcdomfac.add(hfac1.getTasa().multiply(
										new BigDecimal(String.valueOf(
												hfac1.getTotal()))));
			}
			totaldomFacs =  totaldomFacs.setScale(2,BigDecimal.ROUND_HALF_UP);
			montocalcdomfac =  montocalcdomfac.setScale(2,BigDecimal.ROUND_HALF_UP);
			
			difMtoFac = totaldomFacs.subtract(montocalcdomfac);
			
			difPagFact = new BigDecimal(String.valueOf( selectedMet.get(0)
								.getMonto())).subtract(totaldomFacs);
			boolean bRedondeo = 
					selectedMet.size() == 1 && selectedMet.get(0)
					.getMoneda().compareTo( sMonedaBase ) == 0 && 
					hFac.getMoneda().compareTo( sMonedaBase) != 0 ;
			
			if( !m.containsKey("sco_MpagoSobrante") ){
				double dMontoAplicarTmp = d.formatStringToDouble(
								txtMontoAplicar.getValue().toString());

				if(montoRecibido >= dMontoAplicarTmp && valido ){
					BigDecimal cambioDom = BigDecimal.ZERO;
					
					if( txtCambio.getValue() != null && txtCambio.getValue().toString().compareTo("") != 0 ){
						
						double cambiotmp = d.formatStringToDouble(txtCambio.getValue().toString());
						if(cambiotmp > 0)
							cambioDom = new BigDecimal(String.valueOf(cambiotmp));
					}
					
					if( txtCambioForaneo.getValue() == null || 
						txtCambioForaneo.getValue().toString().compareTo("") == 0 ){
						lblPendienteDomestico.setValue("");
						txtPendienteDomestico.setValue("");
						txtCambioForaneo.setValue("");
						lblCambio.setValue("Cambio "+sMonedaBase+":");
						
						if(selectedMet.size() == 1 && totaldomFacs.compareTo( 
							new BigDecimal(String.valueOf(
									selectedMet.get(0).getMonto()))) < 0 ){
							
							cambioDom =  new BigDecimal( String.valueOf(
											selectedMet.get(0).getMonto()))
										.subtract(totaldomFacs);
						}
						
						if(selectedMet.size() > 1 ){
							
							if( metpagos.getMoneda().compareTo(sMonedaBase) == 0 && 
								metpagos.getMetodo().compareTo( MetodosPagoCtrl.EFECTIVO) == 0  &&
								metpagos.getMonto()	> Math.abs(dCambioDom) &&
								dCambioDom < 0  && bCambioDom) {
								
								cambioDom = new BigDecimal(String.valueOf(metpagos.getMonto()))
												.subtract(new BigDecimal(
													String.valueOf(Math.abs(dCambioDom))));
							}
						}
						txtCambio.setValue(new Divisas().formatDouble(cambioDom.doubleValue())); 
					}
				}
			}
		} catch (Exception ex) {
		}
	}
/***************DETERMINAR COMO DAR EL CAMBIO***********************************************/
	public void determinarCambio(List lstPagos, Hfactura hFac,double montoRecibido, String sMonedaBase) {
		boolean bCumple = true, bCambioCOR = false;
		MetodosPago mpago = null;
		Divisas dv = new Divisas();
		SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
		double cambio = 0.0, montoCOR = 0, montoUSD = 0;
		BigDecimal tasa = BigDecimal.ZERO;
		Tcambio[] tcJDE = null; 
		try {
			Tpararela[] tcPar = (Tpararela[]) m.get("tpcambio");
			BigDecimal tasap = tcPar[0].getId().getTcambiom();
			
			// verificar si se cumplen condiciones para cambio mixto
			for (int i = 0; i < lstPagos.size(); i++) {
				mpago = (MetodosPago) lstPagos.get(i);
				if (mpago.getMoneda().equals(sMonedaBase) && mpago.getMetodo().equals( MetodosPagoCtrl.EFECTIVO)) {
					bCumple = false;
				}
				if (!hFac.getMoneda().equals(sMonedaBase) && mpago.getMoneda().equals(sMonedaBase) && mpago.getMetodo().equals( MetodosPagoCtrl.EFECTIVO)) {
					montoCOR = montoCOR + mpago.getMonto();
					bCambioCOR = true;
				}
				if (!hFac.getMoneda().equals(sMonedaBase) && !mpago.getMoneda().equals(sMonedaBase) && mpago.getMetodo().equals( MetodosPagoCtrl.EFECTIVO)) {
					montoUSD = montoUSD + mpago.getMonto();
				}
			}
			//
			double dCambio = dv.formatStringToDouble(txtCambio.getValue().toString());
			if (bCumple && dCambio > 0) {// realiza el calculo del cambio
				tasa = hFac.getTasa();//obtenerTasaOficial();
//				tasa = obtenerTasaOficial();//hFac.getTasa();
				if(hFac.getMoneda().equals(sMonedaBase)){
					String lineaDeFactura = CompaniaCtrl.leerLineaNegocio( hFac.getCodunineg().trim() );
					
					if(PropertiesSystem.LNS_TALLER.contains( lineaDeFactura.trim() ) || PropertiesSystem.LNS_REPUESTOS.contains( lineaDeFactura.trim() ))
					{
						txtCambioForaneo.setValue(dv.roundDouble4(dCambio/tasap.doubleValue()));
					}
					else
					{
						tasa = obtenerTasaOficial();
						txtCambioForaneo.setValue(dv.roundDouble4(dCambio/tasa.doubleValue()));
					}
					
					
				}else{
					txtCambioForaneo.setValue(txtCambio.getValue().toString());
				}
				 
				txtCambioForaneo.setStyle("visibility: visible; width: 40px;display:inline");
				lblCambio.setValue("Cambio USD:");
				txtCambio.setValue("");
				lblCambioDomestico.setValue("Cambio " + sMonedaBase + ":");
				txtCambioDomestico.setValue("0.00");
				lnkCambio.setStyle("visibility: visible; width: 16px");
				lnkCambio.setIconUrl("/theme/icons/RefreshWhite.gif");
				lnkCambio.setHoverIconUrl("/theme/icons/RefreshWhiteOver.gif");
				lblPendienteDomestico.setValue("");
				txtPendienteDomestico.setValue("");
				m.put("bdTasa", BigDecimal.ONE);
			} else if (dCambio < 0) {
				cambio = montoRecibido - dv.formatStringToDouble(txtMontoAplicar.getValue().toString());
				lblCambio.setValue("Cambio " + hFac.getMoneda() + ":");
				txtCambio.setValue(dv.formatDouble(cambio));
				txtCambioForaneo.setStyle("visibility: hidden; width: 0px");
				lblCambioDomestico.setValue("");
				txtCambioDomestico.setValue("");
				lnkCambio.setStyle("visibility: hidden;width: 0px");
				lnkCambio.setIconUrl("");
				lnkCambio.setHoverIconUrl("");
			
				
				tasa = hFac.getTasa();
				
				if (!hFac.getMoneda().equals(sMonedaBase)) {
					lblPendienteDomestico.setValue("Cambio " + sMonedaBase + ":");
					lblPendienteDomestico.setStyle("visibility: visible");
//					txtPendienteDomestico.setValue(dv.formatDouble(dv.roundDouble4(cambio * tasa.doubleValue())));
					txtPendienteDomestico.setValue(dv.formatDouble(dv.roundDouble4(cambio * tasap.doubleValue())));
					m.put("bdTasa", tasa);
					txtPendienteDomestico.setStyle("visibility: visible; color: red");
				}
				if (cambio < 0) {
					txtCambio.setStyle("color: red;font-size: 10pt");
				} else if (cambio > 0) {
					txtCambio.setStyle("color: green;font-size: 10pt");
				} else {
					txtCambio.setStyle("font-size: 10pt");
				}
			} else if (!hFac.getMoneda().equals(sMonedaBase) && bCambioCOR) {// toma el total a aplicar y lo convierte a la tasa paralela a eso se le resta lo recibido a la misma tasa y ese es el cambio
				// obtener tasa del cambio
				tasa = hFac.getTasa();//obtenerTasaParalela(sMonedaBase);
				String lineaDeFactura = CompaniaCtrl.leerLineaNegocio( hFac.getCodunineg().trim() );
				if(PropertiesSystem.LNS_TALLER.contains( lineaDeFactura.trim() ) || PropertiesSystem.LNS_REPUESTOS.contains( lineaDeFactura.trim() ))
				{
					cambio = dv.roundDouble4( ( dv.formatStringToDouble(txtMontoRecibido.getValue().toString()) - dv.formatStringToDouble(txtMontoAplicar.getValue().toString())) *tasap.doubleValue()) ;

				}
				else
				{
					cambio = dv.roundDouble4( ( dv.formatStringToDouble(txtMontoRecibido.getValue().toString()) - dv.formatStringToDouble(txtMontoAplicar.getValue().toString())) *tasa.doubleValue()) ;
				}
				
				m.put("bdTasa", tasa);
				if (cambio < 0) {
					txtCambio.setStyle("color: red;font-size: 10pt");
				} else if (cambio > 0) {
					txtCambio.setStyle("color: green;font-size: 10pt");
				} else {
					txtCambio.setStyle("font-size: 10pt");
				}
				lblCambio.setValue("Cambio "+sMonedaBase+":");
				txtCambio.setValue(dv.formatDouble(cambio));
				txtCambioForaneo.setStyle("visibility: hidden; width: 0px");
				lnkCambio.setStyle("visibility: hidden; width: 0px");
				lnkCambio.setIconUrl("");
				lnkCambio.setHoverIconUrl("");
				lblCambioDomestico.setValue("");
				txtCambioDomestico.setValue("");
				lblPendienteDomestico.setValue("");
				txtPendienteDomestico.setValue("");
			} else if (!hFac.getMoneda().equals(sMonedaBase)) {// restablece el cambio al tipo de factura
				// obtener tasa del cambio
				tcJDE = (Tcambio[]) m.get("tcambio");
				tasa = hFac.getTasa();
				m.put("bdTasa", tasa);
				
				String recibido = Double.toString(dv.roundDouble(montoRecibido));
				String aplicado = Double.toString(dv.formatStringToDouble(
										txtMontoAplicar.getValue().toString()));
				
				cambio = dv.roundDouble4(new BigDecimal(recibido)
						.subtract(new BigDecimal(aplicado)).multiply(tasap)
						.doubleValue());
				
				if (cambio < 0) {
					txtCambio.setStyle("color: red;font-size: 10pt");
				} else if (cambio > 0) {
					txtCambio.setStyle("color: green;font-size: 10pt");
				} else {
					txtCambio.setStyle("font-size: 10pt");
				}
				lblCambio.setValue("Cambio  " + sMonedaBase + ":");
				txtCambio.setValue(dv.formatDouble(cambio));
				txtCambioForaneo.setStyle("visibility: hidden; width: 0px");
				lnkCambio.setStyle("visibility: hidden; width: 0px");
				lnkCambio.setIconUrl("");
				lnkCambio.setHoverIconUrl("");
				lblCambioDomestico.setValue("");
				txtCambioDomestico.setValue("");
				lblPendienteDomestico.setValue("");
				txtPendienteDomestico.setValue("");
			} else { 
				m.put("bdTasa", BigDecimal.ONE);
				cambio = (montoRecibido - dv.formatStringToDouble(txtMontoAplicar.getValue().toString()));

				if (cambio < 0) {
					txtCambio.setStyle("color: red;font-size: 10pt");
				} else if (cambio > 0) {
					txtCambio.setStyle("color: green;font-size: 10pt");
				} else {
					txtCambio.setStyle("font-size: 10pt");
				}
				lblCambio.setValue("Cambio  " + sMonedaBase + ":");
				txtCambio.setValue(dv.formatDouble(cambio));
				txtCambioForaneo.setStyle("visibility: hidden; width: 0px");
				lnkCambio.setStyle("visibility: hidden; width: 0px");
				lnkCambio.setIconUrl("");
				lnkCambio.setHoverIconUrl("");
				lblCambioDomestico.setValue("");
				txtCambioDomestico.setValue("");
				lblPendienteDomestico.setValue("");
				txtPendienteDomestico.setValue("");
			}
			srm.addSmartRefreshId(lblCambio.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(txtCambioDomestico.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lnkCambio.getClientId(FacesContext.getCurrentInstance()));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
/*********************************************************************************************************************/
/*****************CALCULAR EL MONTO RECIBIDO EN DEPENDENCIA DE LOS METODOS DE PAGO***********************************/
	public double calcularElMontoRecibido(List<MetodosPago> selectedMet, Hfactura hFac, String sMonedaBase) {
		double montoRecibido = 0;
		Divisas divisas = new Divisas();
		double cambio = 0;
		boolean bEfectivo = false;
		List<MetodosPago> pagosCambio = new ArrayList<MetodosPago>();
		
		try {
			
			double dMontoAplicar = divisas.formatStringToDouble(txtMontoAplicar.getValue().toString());
			
			
			//&& ====== Determinar si hay efectivo en los pagos.			
			if(selectedMet.size() == 0)
				bEfectivo = true;
			else{
				for (MetodosPago metodoPago : selectedMet) {
					if(metodoPago.getMetodo().trim().equals( MetodosPagoCtrl.EFECTIVO))
						bEfectivo = true;

					if(hFac.getMoneda().equals(sMonedaBase)){//domestica
						montoRecibido+= (metodoPago.getMoneda().equals(sMonedaBase))?
											metodoPago.getMonto():
											metodoPago.getEquivalente();
					}else{//foranea
						montoRecibido+= (metodoPago.getMoneda().equals(sMonedaBase))?
											metodoPago.getEquivalente():
											metodoPago.getMonto();
					}
				}
				//&& ======= Si ya existe sobrante, calcular de nuevo las diferencias entre monto factura y monto recibido.
				if(m.get("sco_SobrantePago") != null){
					double dDiferencia = divisas.roundDouble4(montoRecibido - dMontoAplicar);
					MetodosPago mpSobrante = (MetodosPago)m.get("sco_MpagoSobrante");
					
					//&& ====== Determinar el monto original del sobrante.
					if(hFac.getMoneda().compareTo(mpSobrante.getMoneda()) != 0 && dDiferencia > 0){
						dDiferencia = (mpSobrante.getEquivalente() > mpSobrante.getMonto())?
										dDiferencia / mpSobrante.getTasa().doubleValue():
										dDiferencia * mpSobrante.getTasa().doubleValue();
						dDiferencia = 	divisas.roundDouble4(dDiferencia);		
					}

					if(dDiferencia > 0 && dDiferencia < dMaximoSobrante)
						m.put("sco_SobrantePago", dDiferencia);
					else
						m.remove("sco_SobrantePago");
				}
			}
			//&& ====== Actualizacion de objetos en pantalla de cambios.========= &&//
			txtCambio.setStyle("color: black; font-size: 10pt");
			if(m.get("sco_SobrantePago") == null && bEfectivo || montoRecibido < dMontoAplicar ){
				cambio = montoRecibido - dMontoAplicar;
				txtCambio.setStyle( (cambio > 0)? "color: red; font-size: 10pt":  "color: green; font-size: 10pt");
			}else{
				cambio = 0;
			}
			
			m.put("montoRecibibo", montoRecibido);
			txtCambio.setValue(divisas.formatDouble(cambio));
			txtMontoRecibido.setValue(divisas.formatDouble(montoRecibido));
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return montoRecibido;
	}

/***********************************************************************************************************/
	/***********************************************************************************************************/
	/******************VALIDAR LOS DATOS DEL PAGO INGRESADO************************************/
		public boolean validarPago(String sMonedaPago, String sMonedaBase, 
				List lstMoneda, Hfactura hFac, 
				boolean usaSckPos, boolean usaManual, boolean vouchermanual, 
				ArrayList<Hfactura> lstFacturasSelected) {
			
			Divisas d = new Divisas();
			CtrlCajas cc = new CtrlCajas();
			CtrlPoliticas polCtrl = new CtrlPoliticas();
			
			String metodo = getCmbMetodosPago().getValue().toString();
			String ref1 = getTxtReferencia1().getValue().toString().trim();
			String ref2 = getTxtReferencia2().getValue().toString().trim();
			String ref3 = getTxtReferencia3().getValue().toString().trim();
			String ref4 = new String("");
			
			int y = 158;
			boolean validado = true;
			double monto = 0;
			double dMontoAplicar = 0;
			double dTotald = 0;
			double dTotalf = 0;
			double dEquivalente = 0;
			
			String monedaFactura="";
			String sCodcomp = new String("");
			String sMonto =  new String("");
			String sCajaId = new String("");
			String sMensajeError = new String("");
			
			BigDecimal tasa =BigDecimal.ZERO;
			BigDecimal tasaPar = BigDecimal.ZERO; 
			BigDecimal tasaJDE =BigDecimal.ZERO;
			
			selectedMet = null;
			Pattern pNumero = null;
			Tcambio[] tcJDE = null;
			
			
			try {
				
				selectedMet = (ArrayList<MetodosPago>) m.get("lstPagos");
				sCajaId = (String) m.get("sCajaId");
				restablecerEstilosPago();
				sCodcomp = hFac.getCodcomp();

				Matcher matNumero = null;
				Matcher matVoucher = null;
				
				if (txtMonto.getValue() != null) {
					sMonto = txtMonto.getValue().toString().trim();
					pNumero = Pattern.compile("^[0-9]+\\.?[0-9]*$");
					matNumero = pNumero.matcher(sMonto);
				}
				
				if (!sMonto.trim().equals("") && matNumero.matches()) {
					monto = d.formatStringToDouble( sMonto );
				}
				
				// expresion regular valores alfanumericos
				Matcher matAlfa1 = null;
				Matcher matAlfa2 = null;
				Matcher matAlfa3 = null;
				Matcher matAlfa4 = null;
				
			 
				sMonto = txtMonto.getValue().toString().trim();
				Pattern pAlfa = Pattern.compile("^[A-Za-z0-9-\\p{Blank}]+$");
				Pattern pAlfaRef1 = Pattern.compile("^[A-Za-z0-9-]+$");
				matAlfa1 = pAlfaRef1.matcher(ref1);
				matAlfa2 = pAlfa.matcher(ref2);
				matAlfa3 = pAlfa.matcher(ref3);
				matAlfa4 = pAlfa.matcher(ref4);
		 
				
				//========================================================================================
				
				//&& ============= restar el monto de la donacion antes de la validacion del monto del pago.
				BigDecimal montoNeto = BigDecimal.ZERO;
				BigDecimal montooriginal = BigDecimal.ZERO;
				BigDecimal montoendonacion = BigDecimal.ZERO;
				
				if ( sMonto.trim().matches(PropertiesSystem.REGEXP_AMOUNT) ) {
					montooriginal = new BigDecimal(sMonto);
					montoNeto = new BigDecimal(sMonto);
				}
				
				boolean aplicadonacion = ( CodeUtil.getFromSessionMap("fdc_MontoTotalEnDonacion") != null );
				
				if(aplicadonacion){
					montoendonacion = new BigDecimal( String.valueOf( CodeUtil.getFromSessionMap("fdc_MontoTotalEnDonacion") ) );
					montoNeto = montooriginal.subtract(montoendonacion) ;
					sMonto = montoNeto.toString();
					monto = montoNeto.doubleValue();
				}
				//=========================================================================================
								
				if(lstMoneda.size()>1){
					// obtener tasa del cambio
					Tpararela[] tcPar = (Tpararela[]) m.get("tpcambio");
					// buscar tasa de cambio paralela para moneda
					for (int t = 0; t < tcPar.length; t++) {
						if (tcPar[t].getId().getCmono().equals(sMonedaBase) 
								|| tcPar[t].getId().getCmond().equals(sMonedaBase)) {
							tasa = tcPar[t].getId().getTcambiom();
							tasaPar = tcPar[t].getId().getTcambiom();
							break;
						}
					}
					//&& ======== Si la tasa viene en cero, obtener la tasa paralela de JdEdwards
					tasaJDE = hFac.getTasa();
					if(tasaJDE.compareTo(BigDecimal.ZERO) == 0 || tasaJDE.compareTo(BigDecimal.ONE) == 0){
						String tasajde = Double.toString(new TasaCambioCtrl()
										.obtenerTasaJDExFecha(sMonedaPago, 
											"COR", new Date(), null, null));
						tasaJDE = new BigDecimal(tasajde);
					}
				
					dTotald = 0;
					dTotalf = 0;
					monedaFactura = "COR";
					for (Hfactura hfac1 : lstFacturasSelected) {
						dTotald += hfac1.getCpendiente();
						dTotalf += hfac1.getDpendiente();
						monedaFactura = hfac1.getMoneda();
					}
					dTotald = new Divisas().roundDouble(dTotald);
					dTotalf = new Divisas().roundDouble(dTotalf);
					
					dEquivalente = monto;
					dMontoAplicar = sMonedaPago.equals(sMonedaBase)? dTotald : dTotalf;
					
					if((!hFac.getMoneda().equals(sMonedaBase)) && sMonedaPago.equals(sMonedaBase)){

						dMontoAplicar = d.roundDouble(dTotalf * tasaPar.doubleValue(), 2);
						dEquivalente = d.roundDouble4(monto / tasaPar.doubleValue());
					}
					if((hFac.getMoneda().equals(sMonedaBase)) && !sMonedaPago.equals(sMonedaBase)){
						dMontoAplicar = dTotald;
						dMontoAplicar = d.roundDouble4(dMontoAplicar/tasaJDE.doubleValue());
						dEquivalente  = d.roundDouble4(monto * tasaJDE.doubleValue());
					}
				}else{
					dMontoAplicar = d.formatStringToDouble(txtMontoAplicar.getValue().toString());
					dEquivalente  = dMontoAplicar;
				}
				
				boolean donaciontotal = aplicadonacion && montoNeto.compareTo(BigDecimal.ZERO) == 0 ;
				if(donaciontotal){
					montoNeto = montooriginal;
					monto = montoNeto.doubleValue();
					dEquivalente  = monto;
					dMontoAplicar += monto ;
					
				}
				
				monto = montoNeto.doubleValue();
				
				//&& ====== Validar que si hay sobrante en pago, no se registre otro pago
				if(sMonto.matches("^[0-9]+\\.?[0-9]*$") && m.get("sco_SobrantePago")!=null){
					sMensajeError = "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" />";
					sMensajeError +="Los pagos ingresados ya han cubierto el monto aplicado ";
					dwProcesa.setWindowState("normal");
					lblMensajeValidacion.setValue(sMensajeError);
					dwProcesa.setStyle("width:350px; height:" + y + "px;");
					return false;
				}
				
				// Valida efectivo
				if (metodo.equals( MetodosPagoCtrl.EFECTIVO)) {
					// validar monto
					if (sMonto.equals("")) {
						validado = false;
						txtMonto.setStyleClass("frmInput2Error");
						sMensajeError = sMensajeError+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El monto es requerido<br>";
						dwProcesa.setWindowState("normal");
						y = y + 5;
					} else if (matNumero == null || !matNumero.matches()
							|| monto == 0) {
						txtMonto.setValue("");
						validado = false;
						txtMonto.setStyleClass("frmInput2Error");
						sMensajeError = sMensajeError+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El monto ingresado no es correcto<br>";
						dwProcesa.setWindowState("normal");
						y = y + 5;
					} else if (!polCtrl.validarMonto(Integer.parseInt(sCajaId),hFac.getCodcomp(), cmbMoneda.getValue().toString(),cmbMetodosPago.getValue().toString(), monto)) {
						validado = false;
						lblMensajeAutorizacion.setValue("El monto ingresado no cumple con las politicas de caja");
						dwSolicitud.setWindowState("normal");
						Date dFechaActual = new Date();
						txtFecha.setValue(dFechaActual);
						if (!txtMonto.getValue().toString().trim().equals("")) {
							sMonto = txtMonto.getValue().toString().trim();
						}
						monto = Double.parseDouble(sMonto);
						m.put("montoIsertando", monto);
						m.put("monto", monto);
					} 
					else if (selectedMet.size() > 0) {
						
						List<MetodosPago> lstPagos = (ArrayList<MetodosPago>)selectedMet;
						for (MetodosPago pago : lstPagos) {
							if( pago.getMetodo().equals(metodo) && pago.getMoneda().equals(sMonedaPago)){
								monto += pago.getMonto();
								dEquivalente += (hFac.getMoneda().equals(pago.getMoneda()))?
												 pago.getMonto():
											     pago.getEquivalente();
							}
						}
						dEquivalente = d.roundDouble4(dEquivalente);
						
						if (!polCtrl.validarMonto(Integer.parseInt(sCajaId),sCodcomp, sMonedaPago, metodo, monto)){
							validado = false;
							lblMensajeAutorizacion.setValue("El consolidado de los montos del método de pago no cumple con las politicas de caja");
							dwSolicitud.setWindowState("normal");
							Date dFechaActual = new Date();
							txtFecha.setValue(dFechaActual);
							m.put("monto", monto);
							if (!txtMonto.getValue().toString().trim().equals("")) {
								sMonto = txtMonto.getValue().toString().trim();
							}
							monto = Double.parseDouble(sMonto);
							m.put("montoIsertando", monto);
						}
					}
					//&& ==== Si pasa el efectivo, entonces otorgar cambio y borrar rastro del sobrante.
					if(validado){
					    m.remove("sco_SobrantePago");
						m.remove("sco_MpagoSobrante");
						m.remove("sco_SbrDfr");
					}
				//&& ====== Validaciones para cheque.
				} else if (metodo.equals( MetodosPagoCtrl.CHEQUE)) {
					// validar montos
					if (sMonto.equals("")) {
						validado = false;
						txtMonto.setStyleClass("frmInput2Error");
						sMensajeError = sMensajeError
								+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El monto es requerido<br>";
						dwProcesa.setWindowState("normal");
						y = y + 7;
					} else if (matNumero == null || !matNumero.matches() || monto == 0) {
						txtMonto.setValue("");
						validado = false;
						txtMonto.setStyleClass("frmInput2Error");
						sMensajeError = sMensajeError
								+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El monto ingresado no es correcto<br>";
						dwProcesa.setWindowState("normal");
						y = y + 7;
					}
					//========= validar los sobrantes en caja para el metodo.================//
					else if (d.roundDouble4(monto) > d.roundDouble4(dMontoAplicar)){
						double dSobrante = d.roundDouble4(monto - dMontoAplicar);
						
						//--- Validar conversión de monedas.
						if( dSobrante > dMaximoSobrante){
							validado = false;
							txtMonto.setStyleClass("frmInput2Error");
							sMensajeError += "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" />";
							sMensajeError += " El monto ingresado para este método de pago debe ser menor o igual al monto a aplicar<br>"; 
							sMensajeError += "monto: " + d.roundDouble4(monto) + " Monto a aplicar: " + d.roundDouble4(dMontoAplicar);
							dwProcesa.setWindowState("normal");	
							y = y + 7;
						}else{
							if(hFac.getMoneda().equals(sMonedaPago) && hFac.getMoneda().equals(sMonedaBase))
								m.put("scr_SbrDfr", "1");
							else
								m.remove("scr_SbrDfr");
							
							m.remove("sco_SobrantePago");
							m.put("sco_SobrantePago", d.roundDouble4(dSobrante));
						}
					}
					// validar referencias
					if(!ref1.matches("^[0-9]{1,8}$")){
						validado = false;
						y = y + 7;
						sMensajeError +=  "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> de 1 a 8 dígitos requeridos para referencia<br>";
						txtReferencia1.setStyleClass("frmInput2Error");
						dwProcesa.setWindowState("normal");
					}
					if (ref2.equals("")) {
						validado = false;
						sMensajeError = sMensajeError
								+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Emisor requerido<br>";
						txtReferencia2.setStyleClass("frmInput2Error");
						dwProcesa.setWindowState("normal");
						y = y + 7;
					} else if (!matAlfa2.matches()) {
						validado = false;
						sMensajeError = sMensajeError
								+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El campo <b>Emisor<b/> contiene caracteres invalidos<br>";
						txtReferencia2.setStyleClass("frmInput2Error");
						dwProcesa.setWindowState("normal");
						y = y + 7;
					} else if (ref2.length() > 150) {
						validado = false;
						sMensajeError = sMensajeError
								+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> La cantidad de caracteres del campo <b>Emisor<b/> es muy alta (lim. 150)<br>";
						txtReferencia2.setStyleClass("frmInput2Error");
						dwProcesa.setWindowState("normal");
						y = y + 15;
					}
					if (ref3.equals("")) {
						validado = false;
						sMensajeError = sMensajeError
								+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Portador requerido<br>";
						txtReferencia3.setStyleClass("frmInput2Error");
						dwProcesa.setWindowState("normal");
						y = y + 7;
					} else if (!matAlfa3.matches()) {
						validado = false;
						sMensajeError = sMensajeError
								+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El campo <b>Portador<b/> contiene caracteres invalidos<br>";
						txtReferencia3.setStyleClass("frmInput2Error");
						dwProcesa.setWindowState("normal");
						y = y + 7;
					} else if (ref3.length() > 150) {
						validado = false;
						sMensajeError = sMensajeError
								+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> La cantidad de caracteres del campo <b>Portador<b/> es muy alta (lim. 150)<br>";
						txtReferencia3.setStyleClass("frmInput2Error");
						dwProcesa.setWindowState("normal");
						y = y + 15;
					} else if (!sMonto.equals("")) {
						if (!polCtrl.validarMonto(Integer.parseInt(sCajaId), hFac
								.getCodcomp(), cmbMoneda.getValue().toString(),
								cmbMetodosPago.getValue().toString(), monto)) {
							validado = false;
							lblMensajeAutorizacion
									.setValue("El monto ingresado no cumple con las politicas de caja");
							dwSolicitud.setWindowState("normal");
							Date dFechaActual = new Date();
							txtFecha.setValue(dFechaActual);
							if (!txtMonto.getValue().toString().trim().equals("")) {
								sMonto = txtMonto.getValue().toString().trim();
							}
							monto = Double.parseDouble(sMonto);
							m.put("montoIsertando", monto);
							m.put("monto", monto);
						} 
						else if (selectedMet.size() > 0 && !donaciontotal) {

							//&& ===== Validaciones para sobrantes de pago por consolidado de metodos.
							List<MetodosPago> lstPagos = (ArrayList<MetodosPago>)selectedMet;
							for (MetodosPago pago : lstPagos) {
								if(pago.getMoneda().equals(sMonedaPago))
									monto += pago.getMonto();
								dEquivalente += (hFac.getMoneda().equals(pago.getMoneda()))?
												 pago.getMonto():
											     pago.getEquivalente();
							}
							dEquivalente = d.roundDouble4(dEquivalente);
							
							//&& ======= Validar politicas de Caja
							if (!polCtrl.validarMonto(Integer.parseInt(sCajaId),sCodcomp, sMonedaPago, metodo,monto)){
								validado = false;
								lblMensajeAutorizacion.setValue("El consolidado de los montos del método no cumple con las politicas de caja");
								dwSolicitud.setWindowState("normal");
								Date dFechaActual = new Date();
								txtFecha.setValue(dFechaActual);
								m.put("monto", monto);
								if (!txtMonto.getValue().toString().trim().equals("")) {
									sMonto = txtMonto.getValue().toString().trim();
								}
								monto = Double.parseDouble(sMonto);
								m.put("montoIsertando", monto);
							}
							//&& ======== validar consolidado de montos
							 else if (dEquivalente > dMontoAplicar) {
								 double dSobrante = monto - dMontoAplicar;
								 dSobrante = d.roundDouble4(dSobrante);
								//--- Validar conversión de monedas.
								if( dSobrante > dMaximoSobrante){
									validado = false;
									txtMonto.setStyleClass("frmInput2Error");
									sMensajeError += "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" />";
									sMensajeError += "El Consolidado de montos ingresados para este método de pago debe ser menor o igual al monto a aplicar<br>";
									dwProcesa.setWindowState("normal");
									y = y + 7;
								}else{
									if(hFac.getMoneda().equals(sMonedaPago) && hFac.getMoneda().equals(sMonedaBase)){
										m.put("sco_SbrDfr", "1");
									}else{
										m.remove("sco_SbrDfr");
									}
									m.remove("sco_SobrantePago");
									m.put("sco_SobrantePago", d.roundDouble4(dSobrante));
								}
							}
						}
					}
					// Valida TC
				}
				else if (metodo.equals(MetodosPagoCtrl.TARJETA)) {
					matVoucher = pNumero.matcher(ref2);

					if( usaSckPos && !vouchermanual){
						
						sMensajeError =  validarPagoSocket(dMontoAplicar, 
											hFac, ref1, ref2, ref3,
											Integer.parseInt(sCajaId), sMonedaPago);
						
						if(!sMensajeError.equals(""))
							validado = false;
						
					}
					else if (sMonto.equals("")) {
						validado = false;
						txtMonto.setStyleClass("frmInput2Error");
						sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El monto es requerido<br>";
						dwProcesa.setWindowState("normal");
						y = y + 5;
					} else if (matNumero == null || !matNumero.matches() || monto == 0) {
						txtMonto.setValue("");
						validado = false;
						txtMonto.setStyleClass("frmInput2Error");
						sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El monto ingresado no es correcto<br>";
						dwProcesa.setWindowState("normal");
						y = y + 5;
					} else if (monto > dMontoAplicar) {
						validado = false;
						txtMonto.setStyleClass("frmInput2Error");
						sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El monto ingresado para este método de pago debe ser menor o igual al monto a aplicar<br>";
						dwProcesa.setWindowState("normal");
						y = y + 7;
					}
					// Valida Referencias
					else if (ddlAfiliado.getValue().toString().equals("01")) {
						validado = false;
						ddlAfiliado.setStyleClass("frmInput2Error2");
						sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Seleccione un afiliado<br>";
						dwProcesa.setWindowState("normal");
						y = y + 5;
					} else if (ref1.equals("")) {//valida identificacion del cliente
						validado = false;
						txtReferencia1.setStyleClass("frmInput2Error");
						sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Identificación requerida<br>";
						dwProcesa.setWindowState("normal");
						y = y + 5;
					} else if (!matAlfa1.matches()) {
						validado = false;
						sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El campo <b>Identificación<b/> contiene caracteres invalidos<br>";
						txtReferencia1.setStyleClass("frmInput2Error");
						dwProcesa.setWindowState("normal");
						y = y + 7;
					} else if (ref1.length() > 150) {
						validado = false;
						sMensajeError = sMensajeError+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> La cantidad de caracteres del campo <b>Identificación<b/> es muy alta (lim. 150)<br>";
						txtReferencia1.setStyleClass("frmInput2Error");
						dwProcesa.setWindowState("normal");
						y = y + 15;
					}else if (!matVoucher.matches()) {
						validado = false;
						sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El campo <b>Referencia<b/> contiene caracteres invalidos<br>";
						txtReferencia2.setStyleClass("frmInput2Error");
						dwProcesa.setWindowState("normal");
						y = y + 7;
					} else if (ref2.length() > 8) {
						validado = false;
						sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> " +
								"El valor de la referencia debe contener entre 1 y 8 dígitos unicamente ";
				
						txtReferencia2.setStyleClass("frmInput2Error");
						dwProcesa.setWindowState("normal");
						y = y + 15;
					} 
					
					else if (!polCtrl.validarMonto(Integer.parseInt(sCajaId),hFac.getCodcomp(), cmbMoneda.getValue().toString(),cmbMetodosPago.getValue().toString(), monto)) {
						validado = false;
						lblMensajeAutorizacion.setValue("El monto ingresado no cumple con las politicas de caja");
						dwSolicitud.setWindowState("normal");
						Date dFechaActual = new Date();
						txtFecha.setValue(dFechaActual);
						if (!txtMonto.getValue().toString().trim().equals("")) {
							sMonto = txtMonto.getValue().toString().trim();
						}
						monto = Double.parseDouble(sMonto);
						m.put("montoIsertando", monto);
						m.put("monto", monto);
					} 
					else if (selectedMet.size() > 0 && !donaciontotal) {
						
						if(selectedMet.size() > 0 && !donaciontotal){
							List<MetodosPago> lstPagos = (ArrayList<MetodosPago>)selectedMet;
							for (MetodosPago pago : lstPagos) {
								if(pago.getMoneda().equals(sMonedaPago))
									monto += pago.getMonto();
								
								dEquivalente += (hFac.getMoneda().equals(pago.getMoneda()))?
												 pago.getMonto():
											     pago.getEquivalente();
							}
							monto = d.roundDouble4(monto);
							dEquivalente = d.roundDouble4(dEquivalente);
						}

						if( hFac.getMoneda().compareTo(sMonedaBase) == 0 && monto > dMontoAplicar ||
							hFac.getMoneda().compareTo(sMonedaBase) != 0 
							&& d.roundDouble(dEquivalente) > dTotalf /*dEquivalente > dTotalf*/ ){
							
							txtMonto.setStyleClass("frmInput2Error");
							sMensajeError  += "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" ";
							sMensajeError  += " border=\"0\" /> El Consolidado de pagos ingresados ";
							sMensajeError  += " debe ser menor o igual al monto a aplicar<br>";
							y = y + 7;
							
							lblMensajeValidacion.setValue(sMensajeError);
							dwProcesa.setStyle("width:350px;height:" + y + "px");
							dwProcesa.setWindowState("normal");
							
							return false;
						
						}

						if (!polCtrl.validarMonto(Integer.parseInt(sCajaId), hFac.getCodcomp(), cmbMoneda.getValue().toString(),cmbMetodosPago.getValue().toString(), monto)) {
							validado = false;
							lblMensajeAutorizacion.setValue("El consolidado de los montos no cumple con las politicas de caja");
							dwSolicitud.setWindowState("normal");
							Date dFechaActual = new Date();
							txtFecha.setValue(dFechaActual);
							m.put("monto", monto);
							if (!txtMonto.getValue().toString().trim().equals("")) {
								sMonto = txtMonto.getValue().toString();
							}
							monto = Double.parseDouble(sMonto);
							m.put("montoIsertando", monto);
						}
					}
				}
				// validar Transferencia Electronica
				else if (metodo.equals(MetodosPagoCtrl.TRANSFERENCIA)) {
					
					
					//validamos si la referencia agregada ya ha sido utilizada para el mismo banco 
					if(polCtrl.validarReferenciaBancaria(Integer.parseInt(sCajaId), ddlBanco.getValue().toString().split("@")[1], ref2,"T"))
					{
						y = y + 7;
						dwProcesa.setWindowState("normal");
						txtReferencia2.setStyleClass("frmInput2Error");
						sMensajeError = "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> " +
										"La referencia ya ha sido utilizada, favor verificar e ingresar una referencia válida ";
						lblMensajeValidacion.setValue(sMensajeError);	
						return validado = false;
					}
					
					// vallidar montos
					if (sMonto.equals("")) {
						validado = false;
						txtMonto.setStyleClass("frmInput2Error");
						sMensajeError = sMensajeError
								+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El monto es requerido<br>";
						dwProcesa.setWindowState("normal");
						y = y + 5;
					} else if (matNumero == null || !matNumero.matches()
							|| monto == 0) {
						txtMonto.setValue("");
						validado = false;
						txtMonto.setStyleClass("frmInput2Error");
						sMensajeError = sMensajeError
								+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El monto ingresado no es correcto<br>";
						dwProcesa.setWindowState("normal");
						y = y + 5;
					}
					else
						if (monto > d.roundDouble4(dMontoAplicar)){
							double dSobrante = d.roundDouble4( monto - dMontoAplicar );
							
							//--- Validar conversión de monedas.
							if( dSobrante > dMaximoSobrante){
								validado = false;
								txtMonto.setStyleClass("frmInput2Error");
								sMensajeError = "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" />";
								sMensajeError += "El monto ingresado para este método de pago debe ser menor o igual al monto a aplicar<br>";
								dwProcesa.setWindowState("normal");	
								y = y + 7;
							}else{
								if(hFac.getMoneda().equals(sMonedaPago) && hFac.getMoneda().equals(sMonedaBase))
									m.put("sco_SbrDfr", "1");
								else
									m.remove("sco_SbrDfr");
								m.remove("sco_SobrantePago");
								m.put("sco_SobrantePago", dSobrante);
							}
						}
					// referencias
					else if (ref1.equals("")) {
						validado = false;
						txtReferencia1.setStyleClass("frmInput2Error");
						sMensajeError = sMensajeError
								+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Identificación requerida<br>";
						dwProcesa.setWindowState("normal");
						y = y + 5;
					} else if (!matAlfa1.matches()) {
						validado = false;
						sMensajeError = sMensajeError
								+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El campo <b>Identificación<b/> contiene caracteres invalidos<br>";
						txtReferencia1.setStyleClass("frmInput2Error");
						dwProcesa.setWindowState("normal");
						y = y + 7;
					} else if (ref1.length() > 150) {
						validado = false;
						sMensajeError = sMensajeError
								+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> La cantidad de caracteres del campo <b>Identificación<b/> es muy alta (lim. 150)<br>";
						txtReferencia1.setStyleClass("frmInput2Error");
						dwProcesa.setWindowState("normal");
						y = y + 15;
					} else{ 

						
						if( !ref2.matches(PropertiesSystem.REGEXP_8DIGTS) ){
							y = y + 7;
							dwProcesa.setWindowState("normal");
							txtReferencia2.setStyleClass("frmInput2Error");
							sMensajeError = "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> " +
											"El valor de la referencia debe contener entre 1 y 8 dígitos unicamente ";
							lblMensajeValidacion.setValue(sMensajeError);
							return validado = false;
						}
						
						lblMensajeValidacion.setValue("sMensajeError");
					}

					if (validado && !polCtrl.validarMonto(Integer.parseInt(sCajaId), sCodcomp, sMonedaPago, metodo, monto)){
						validado = false;
						lblMensajeAutorizacion.setValue("El monto ingresado no cumple con las politicas de caja");
						dwSolicitud.setWindowState("normal");
						Date dFechaActual = new Date();
						txtFecha.setValue(dFechaActual);
						if (!txtMonto.getValue().toString().trim().equals("")) {
							sMonto = txtMonto.getValue().toString().trim();
						}
						monto = Double.parseDouble(sMonto);
						m.put("montoIsertando", monto);
						m.put("monto", monto);
					}
					else 
						if (selectedMet.size() > 0 && !donaciontotal) {
														
							List<MetodosPago> lstPagos = (ArrayList<MetodosPago>)selectedMet;
							for (MetodosPago pago : lstPagos) {
								if(pago.getMoneda().equals(sMonedaPago))
									monto += pago.getMonto();
								dEquivalente += (hFac.getMoneda().equals(pago.getMoneda()))?
												 pago.getMonto():
											     pago.getEquivalente();
							}
							dEquivalente = d.roundDouble4(dEquivalente);

							if (!polCtrl.validarMonto(Integer.parseInt(sCajaId),sCodcomp, sMonedaPago, metodo, monto)){
								validado = false;
								lblMensajeAutorizacion.setValue("El consolidado de los montos no cumple con las politicas de caja");
								dwSolicitud.setWindowState("normal");
								Date dFechaActual = new Date();
								txtFecha.setValue(dFechaActual);
								m.put("monto", monto);
								if (!txtMonto.getValue().toString().trim().equals("")) 
									sMonto = txtMonto.getValue().toString();
								
								monto = Double.parseDouble(sMonto);
								m.put("montoIsertando", monto);
							}							//validar consolidado de montos
							 else{ 
								 if (dEquivalente > dMontoAplicar) {
									double dSobrante = monto - dMontoAplicar;
									dSobrante = d.roundDouble4(dSobrante);
									
									//--- Validar conversión de monedas.
									if( dSobrante > dMaximoSobrante){
										validado = false;
										txtMonto.setStyleClass("frmInput2Error");
										sMensajeError += "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" />";
										sMensajeError += "El Consolidado de montos ingresados para este método de pago debe ser menor o igual al monto a aplicar<br>";
										dwProcesa.setWindowState("normal");
										y = y + 7;
									}else{
										if(hFac.getMoneda().equals(sMonedaPago) && hFac.getMoneda().equals(sMonedaBase)){
											m.put("sco_SbrDfr", "1");
										}else{
											m.remove("sco_SbrDfr");
										}
										m.remove("scr_SobrantePago");
										m.put("scr_SobrantePago", d.roundDouble4(dSobrante));
									}
								 }
							 }
						}
				}
				// validar Deposito en banco
				else if (metodo.equals(MetodosPagoCtrl.DEPOSITO)) {
					
					//validamos si la referencia agregada ya ha sido utilizada para el mismo banco 
					if(polCtrl.validarReferenciaBancaria(Integer.parseInt(sCajaId), ddlBanco.getValue().toString().split("@")[1], ref2,"N"))
					{
						y = y + 7;
						dwProcesa.setWindowState("normal");
						txtReferencia2.setStyleClass("frmInput2Error");
						sMensajeError = "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> " +
										"La referencia ya ha sido utilizada, favor verificar e ingresar una referencia válida ";
						lblMensajeValidacion.setValue(sMensajeError);	
						return validado = false;
					}
					
					//Validamos no importa que ni como ni donde si la referenia tiene mas de 8 digitos
					if( !ref2.matches(PropertiesSystem.REGEXP_8DIGTS) ){
						y = y + 7;
						dwProcesa.setWindowState("normal");
						txtReferencia2.setStyleClass("frmInput2Error");
						sMensajeError = "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> " +
										"El valor de la referencia debe contener entre 1 y 8 dígitos unicamente ";
						
						lblMensajeValidacion.setValue(sMensajeError);
						return validado = false;
						
						
					}
					
					
					// vallidar montos
					if (sMonto.equals("")) {
						validado = false;
						txtMonto.setStyleClass("frmInput2Error");
						sMensajeError = sMensajeError+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El monto es requerido<br>";
						dwProcesa.setWindowState("normal");
						y = y + 5;
					} else if (matNumero == null || !matNumero.matches()
							|| monto == 0) {
						txtMonto.setValue("");
						validado = false;
						txtMonto.setStyleClass("frmInput2Error");
						sMensajeError = sMensajeError
								+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El monto ingresado no es correcto<br>";
						dwProcesa.setWindowState("normal");
						y = y + 5;
					}
					else if (monto > d.roundDouble4(dMontoAplicar)){
						double dSobrante = monto - dMontoAplicar;
						dSobrante = d.roundDouble4(dSobrante);
						//--- Validar conversión de monedas.
						if( dSobrante > dMaximoSobrante){
							validado = false;
							txtMonto.setStyleClass("frmInput2Error");
							txtMonto.setStyleClass("frmInput2Error");
							sMensajeError += "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" />";
							sMensajeError += "El monto ingresado para este método de pago debe ser menor o igual al monto a aplicar";
							sMensajeError += "<br>monto: "  + d.roundDouble4(monto) + " Monto a aplicar: " + d.roundDouble4(dMontoAplicar);
							dwProcesa.setWindowState("normal");	
							y = y + 7;
						}else{
							m.remove("sco_SobrantePago");
							m.put("sco_SobrantePago", d.roundDouble4(dSobrante));
						}
					}
					// referencias
					else{
 
						if( !ref2.matches(PropertiesSystem.REGEXP_8DIGTS) ){
							y = y + 7;
							dwProcesa.setWindowState("normal");
							txtReferencia2.setStyleClass("frmInput2Error");
							sMensajeError = "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> " +
											"El valor de la referencia debe contener entre 1 y 8 dígitos unicamente ";
							
							lblMensajeValidacion.setValue(sMensajeError);
							return validado = false;
							
							
						}
						

					}											
					if (validado && !polCtrl.validarMonto(Integer.parseInt(sCajaId), sCodcomp, sMonedaPago,metodo, monto)){
						validado = false;
						lblMensajeAutorizacion.setValue("El monto ingresado no cumple con las politicas de caja");
						dwSolicitud.setWindowState("normal");
						Date dFechaActual = new Date();
						txtFecha.setValue(dFechaActual);
						if (!txtMonto.getValue().toString().trim().equals("")) {
							sMonto = txtMonto.getValue().toString().trim();
						}
						monto = Double.parseDouble(sMonto);
						m.put("montoIsertando", monto);
						m.put("monto", monto);
					}
					else if(selectedMet.size() > 0 && !donaciontotal ){
						List<MetodosPago> lstPagos = (ArrayList<MetodosPago>)selectedMet;
						for (MetodosPago pago : lstPagos) {
							if(pago.getMoneda().equals(sMonedaPago))
								monto += pago.getMonto();
							
							dEquivalente += (hFac.getMoneda().equals(pago.getMoneda()))?
											 pago.getMonto():
										     pago.getEquivalente();
						}
						dEquivalente = d.roundDouble4(dEquivalente);
						
						if (!polCtrl.validarMonto(Integer.parseInt(sCajaId), hFac.getCodcomp(), cmbMoneda.getValue().toString(),cmbMetodosPago.getValue().toString(), monto)) {
							validado = false;
							lblMensajeAutorizacion.setValue("El consolidado de los montos no cumple con las politicas de caja");
							dwSolicitud.setWindowState("normal");
							Date dFechaActual = new Date();
							txtFecha.setValue(dFechaActual);
							m.put("monto", monto);
							if (!txtMonto.getValue().toString().trim().equals("")) {
								sMonto = txtMonto.getValue().toString().trim();
							}
							monto = Double.parseDouble(sMonto);
							m.put("montoIsertando", monto);
						}
						//validar consolidado de montos
						 else {
							 if (dEquivalente > dMontoAplicar) {
								double dSobrante = monto - dMontoAplicar;
								dSobrante = d.roundDouble4(dSobrante);
								//--- Validar conversión de monedas.
								if( dSobrante > dMaximoSobrante){
									validado = false;
									txtMonto.setStyleClass("frmInput2Error");
									sMensajeError += "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" />";
									sMensajeError += "El Consolidado de montos ingresados para este método de pago debe ser menor o igual al monto a aplicar<br>";
									dwProcesa.setWindowState("normal");
									y = y + 7;
								}else{
									if(hFac.getMoneda().equals(sMonedaPago) && hFac.getMoneda().equals(sMonedaBase)){
										m.put("sco_SbrDfr", "1");
									}else{
										m.remove("sco_SbrDfr");
									}
									m.remove("sco_SobrantePago");
									m.put("sco_SobrantePago", d.roundDouble4(dSobrante));
								}
							 }
						 }
					}
				}
				
				lblMensajeValidacion.setValue(sMensajeError);
				dwProcesa.setStyle("width:350px;height:" + y + "px");
				
			} catch (Exception ex) {
				
				ex.printStackTrace(); 
				validado = false;
				
				lblMensajeValidacion.setValue("");
				dwProcesa.setStyle(" width: 350px; height: 150px");
				
			}
			return validado;
		}

		/** ******************************************************************************************************* */

/****************REESTABLECER ESTILOS A DATOS DEL PAGO****************************************************************/
	public void restablecerEstilosPago() {
		txtMonto.setStyleClass("frmInput2");
		txtReferencia1.setStyleClass("frmInput2");
		txtReferencia2.setStyleClass("frmInput2");
		txtReferencia3.setStyleClass("frmInput2");
		ddlAfiliado.setStyleClass("frmInput2");
		track.setStyleClass("frmInput2");
		txtFechaVenceT.setStyleClass("frmInput2");
		txtNoTarjeta.setStyleClass("frmInput2");
	}

/********************************************************************************************************* */
/****************ELIMINA METODOS DE PAGO DEL GRID****************************************************************/
	public void borrarPago(ActionEvent e) {
		Divisas divisas = new Divisas();
		List<MetodosPago> lstPagos = null;
		MetodosPago mPago = null, mPagoComp;
		boolean bValido = true, bSocket = true;
		String sMonedaBase = new String("");
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		List<Hfactura> lstFacturasSelected = null;
		
		try {
			
			selectedMet =  (ArrayList<MetodosPago>)	m.get("lstPagos");
			lstFacturasSelected = (List<Hfactura>) m.get("facturasSelected");
			Hfactura hFac =  lstFacturasSelected.get(0);
			mPago = (MetodosPago) m.get("metodopagoborrar");
			
			
			// && ========== Borrar datos de la donacion.
			if (mPago.isIncluyedonacion()) {
				CodeUtil.removeFromSessionMap(new String[] 
					{ "fdc_MontoTotalEnDonacion","fdc_lstDonacionesRecibidas" });
			}
			
			sMonedaBase = cCtrl.sacarMonedaBase((F55ca014[])
					m.get("cont_f55ca014"), hFac.getCodcomp());
			
			//&& ========== remover solicitudes, en caso de existir.
			ArrayList<Solicitud> lstSolicitud = m.containsKey("lstSolicitud")? 
								(ArrayList<Solicitud>)m.get("lstSolicitud"):
								 new ArrayList<Solicitud>();
			CtrlSolicitud.removerSolicitud(lstSolicitud, mPago);	
			
			//&& ========== remover el pago de la lista de los registrados.		
			MetodosPagoCtrl.removerPago(selectedMet, mPago);
					
			//&& ======= Validar que los pagos restantes no generen sobrantes.
			double dSobrante=0;
			double dTotalMpagos=0;
			lstPagos = new ArrayList<MetodosPago>(selectedMet);
			double dMontoAplicar = divisas.formatStringToDouble(txtMontoAplicar.getValue().toString());
			
			for (MetodosPago mPagoValidar : lstPagos) {
				dTotalMpagos += mPagoValidar.getEquivalente();
				
				if(mPago.getEquivalente() > dMontoAplicar || dTotalMpagos > dMontoAplicar){
					dSobrante =  new Divisas().roundDouble4(dTotalMpagos - dMontoAplicar);
					if(dSobrante> dMaximoSobrante){
						lblMensajeValidacion.setValue("No puede borrar el pago, el monto restante excede el monto aplicar");
						dwProcesa.setStyle("width:320px;height: 160px");
						dwProcesa.setWindowState("normal");
						return;
					}else{
						//&& ===== verificar el tipo de excedente, si sobrante o si diferencial cambiario.
						if(hFac.getMoneda().equals(mPago.getMoneda()) && hFac.getMoneda().equals(sMonedaBase)){
							m.put("sco_SbrDfr", "1");
						}else{
							m.remove("sco_SbrDfr");
						}
						m.put("sco_MpagoSobrante",mPagoValidar);
						m.put("sco_SobrantePago",dSobrante);
					}
				}
			}
			m.put("mpagos", selectedMet);
			m.put("lstPagos", selectedMet);

			//&& ========== calcular el monto recibido en dependencia de moneda de factura y moneda de pago
			double montoRecibido = calcularElMontoRecibido(selectedMet,hFac,sMonedaBase);
			boolean bEfectivo = false;
			List<MetodosPago>lstMetodos = (ArrayList<MetodosPago>)selectedMet;
			if(lstMetodos.size() == 0)
				bEfectivo = true;
			else{
				for (MetodosPago pago : lstMetodos) {
					if(pago.getMetodo().trim().toLowerCase().equals( MetodosPagoCtrl.EFECTIVO)){
						bEfectivo = true;
						break;
					}
				}
			}
			SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
			if(bEfectivo || montoRecibido <= dMontoAplicar ){
			    m.remove("sco_SobrantePago");
				m.remove("sco_MpagoSobrante");
				m.remove("sco_SbrDfr");
				determinarCambio(selectedMet, hFac, montoRecibido, sMonedaBase);
			}else{
				if(m.get("scr_SobrantePago") != null){
					lblCambio.setValue("Cambio: "+hFac.getMoneda());
					txtCambio.setValue(divisas.formatDouble(0));
					lblPendienteDomestico.setValue("");
					txtPendienteDomestico.setValue("");
					lblCambioDomestico.setValue("");
					txtCambioDomestico.setValue("");
					
					txtCambioForaneo.setStyle("visibility: hidden; width: 0px");
					lnkCambio.setStyle("visibility: hidden;width: 0px");
					lnkCambio.setIconUrl("");
					lnkCambio.setHoverIconUrl("");				

					srm.addSmartRefreshId(lblCambio.getClientId(FacesContext.getCurrentInstance()));
					srm.addSmartRefreshId(txtCambio.getClientId(FacesContext.getCurrentInstance()));
					srm.addSmartRefreshId(txtCambioForaneo.getClientId(FacesContext.getCurrentInstance()));
					srm.addSmartRefreshId(lnkCambio.getClientId(FacesContext.getCurrentInstance()));
					
				}
			}
			m.put("montoRecibibo", montoRecibido);
			
			// determinar como dar el cambio
			determinarCambio(selectedMet, hFac, montoRecibido, sMonedaBase);
			m.put("montoRecibibo", montoRecibido);
			metodosGrid.dataBind();
			
			if( (selectedMet == null || selectedMet.size() == 0) 
					&& hFac.getMoneda().compareTo(sMonedaBase) != 0){
				double dMontoAplicarf = 0;
				
				//---- Calcular el monto aplicar.
				for (Hfactura hfs : lstFacturasSelected) {
					if((hFac.getMoneda().equals(sMonedaBase))){
						dMontoAplicar += hfs.getCpendiente();											
					}else{
						dMontoAplicar += hfs.getDpendiente();		
						dMontoAplicarf += hfs.getCpendiente();
					}
				}
				txtPendienteDomestico.setValue("-" + divisas.formatDouble(dMontoAplicarf));
				srm.addSmartRefreshId(txtPendienteDomestico.getClientId(FacesContext.getCurrentInstance()));
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}finally{
			dwBorrarPago.setWindowState("hidden");
			m.remove("metodopagoborrar");
		}
	}

/***********************MODIFICAR METODOS DE PAGO EN EL GRID******************************************************/
	public void modificarPago(CellValueChangeEvent e) {
		SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
		Divisas divisas = new Divisas();
		double tasa = 1.0, equiv = 0;
		boolean cumplePoliticas = true;
		double dNewMonto = 0.0, montoRecibido = 0.0;
		Tpararela[] tcPar = null;
		Tcambio[] tcJDE = null;
		boolean montoValido = true;
		String sMoneda = "";
		String sMonedaBase = "";
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		try {
			// obtener info de la factura
			List lstFacturasSelected = (List) m.get("facturasSelected");
			Hfactura hFac = (Hfactura) lstFacturasSelected.get(0);
			//obtener companias x caja
			sMonedaBase = cCtrl.sacarMonedaBase((F55ca014[])m.get("cont_f55ca014"), hFac.getCodcomp());
			// Determine the row and column being updated
			List rows = metodosGrid.getRows();
			RowItem row = (RowItem) rows.get(e.getPosition().getRow());
			int colpos = e.getPosition().getCol();
			// obtener los datos viejos de la fila (anteriores a modificacion).
			MetodosPago mPago = (MetodosPago) metodosGrid.getDataRow(row);
			m.put("mPagoOld", mPago);
			m.put("sMontoOld", mPago.getMonto());
			// obtener metodos de pago anteriores a modificacion
			selectedMet = (ArrayList<MetodosPago>) m.get("lstPagos");
			MetodosPago[] mPagoOld = new MetodosPago[selectedMet.size()];
			for (int i = 0; i < selectedMet.size(); i++) {
				mPagoOld[i] = (MetodosPago) selectedMet.get(i);
			}
			// obtener el nuevo monto y asignar a Mpago
			String sNewMonto = "";
			// actualizar monto
			if (colpos == 3) {
				sNewMonto = e.getNewValue().toString();
				montoValido = validarModificacionMonto(sNewMonto);
				if (montoValido) {
					dNewMonto = divisas.formatStringToDouble(sNewMonto);
					mPago.setMonto(dNewMonto);
					if (hFac.getMoneda().equals("COR")) {// domestica
						if (mPago.getMoneda().equals("COR")) {
							tasa = 1.00;
							equiv = dNewMonto;
						} else {// foranea
							tcJDE = (Tcambio[]) m.get("tcambio");
							// buscar tasa de cambio JDE para moneda
							for (int l = 0; l < tcJDE.length; l++) {
								if (tcJDE[l].getId().getCxcrcd().equals(
										mPago.getMoneda())) {
									tasa = tcJDE[l].getId().getCxcrrd()
											.doubleValue();
									break;
								}
							}
							equiv = dNewMonto * tasa;
						}
					} else {// foranea
						if (mPago.getMoneda().equals("COR")) {
							tcPar = (Tpararela[]) m.get("tpcambio");
							// buscar tasa de cambio paralela para moneda
							for (int t = 0; t < tcPar.length; t++) {
								if (tcPar[t].getId().getCmono().equals(
										mPago.getMoneda())
										|| tcPar[t].getId().getCmond().equals(
												mPago.getMoneda())) {
									tasa = tcPar[t].getId().getTcambiom()
											.doubleValue();
									break;
								}
							}
							equiv = dNewMonto / tasa;
						} else {
							tasa = 1.00;
							equiv = dNewMonto;
						}
					}
					mPago.setEquivalente(equiv);

					// buscar el registro en el datasource y sustituirlo
					for (int j = 0; j < mPagoOld.length; j++) {
						if (mPago.getMetodo().equals(mPagoOld[j].getMetodo())
								&& mPago.getEquivalente() == mPago
										.getEquivalente()) {
							selectedMet.remove(j);
							selectedMet.add(j, mPago);
						} else {// formatear string anterior
							mPagoOld[j].setMonto(mPagoOld[j].getMonto());
							selectedMet.remove(j);
							selectedMet.add(j, mPagoOld[j]);
						}
					}
					// meter lista en el mapa
					m.remove("mpagos");
					m.remove("lstPagos");
					m.put("mpagos", selectedMet);
					m.put("lstPagos", selectedMet);
					// --------------------------------------------------
					// calcular el monto recibido en dependencia de moneda de
					// factura y moneda de pago
					calcularElMontoRecibido(selectedMet, hFac, sMonedaBase);
					double dMontoAplicar = divisas.formatStringToDouble(txtMontoAplicar.getValue().toString());
					montoRecibido = divisas.formatStringToDouble(txtMontoRecibido.getValue().toString());
					double cambio = montoRecibido - dMontoAplicar;

					txtCambio.setValue(divisas.formatDouble(cambio));
					// determinar el cambio

					// determinar como dar el cambio
					determinarCambio(selectedMet, hFac, montoRecibido, sMonedaBase);
					m.put("montoRecibibo", montoRecibido);
					metodosGrid.dataBind();
				} else {// monto invalido
					// buscar el registro en el datasource y sustituirlo
					for (int j = 0; j < mPagoOld.length; j++) {
						if (mPago.getMetodo().equals(mPagoOld[j].getMetodo())&& mPago.getEquivalente() == mPago.getEquivalente()) {
							selectedMet.remove(j);
							mPago.setMonto(mPagoOld[j].getMonto());
							selectedMet.add(j, mPago);
						}
					}
					m.remove("mpagos");
					m.remove("lstPagos");
					m.put("mpagos", selectedMet);
					m.put("lstPagos", selectedMet);
				}
			}
			srm.addSmartRefreshId(txtCambio.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(txtMontoRecibido.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(dwProcesa.getClientId(FacesContext.getCurrentInstance()));
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	/** ***************************************************************************************** */
	public boolean validarModificacionMonto(String sNewMonto) {
		boolean validado = true;
		CtrlPoliticas pol = new CtrlPoliticas();
		Divisas divisas = new Divisas();
		try {
			Pattern p = Pattern.compile("^[0-9]+\\.?[0-9]*$");
			Matcher mat = p.matcher(sNewMonto);
			//
			// obtener datos de factura
			List lstFacturasSelected = (List) m.get("facturasSelected");
			Hfactura hFac = (Hfactura) lstFacturasSelected.get(0);
			if (sNewMonto.trim().equals("")) {
				validado = false;
				lblMensajeValidacion.setValue("El valor ingresado no es correcto");
				dwProcesa.setWindowState("normal");
			} else if (!mat.matches()) {
				validado = false;
				lblMensajeValidacion.setValue("El valor ingresado no es correcto");
				dwProcesa.setWindowState("normal");
			} else if (!pol.validarMonto(Integer.parseInt(m.get("sCajaId")
					.toString()), hFac.getCodcomp(), cmbMoneda.getValue()
					.toString(), cmbMetodosPago.getValue().toString(), divisas
					.formatStringToDouble(sNewMonto))) {
				validado = false;
				lblMensajeValidacion.setValue("El monto ingresado no cumple con las politicas de caja");
				dwProcesa.setWindowState("normal");
			}
		} catch (Exception ex) {
			validado = false;
			System.err.print("Se capturo una excecion en FacContadoDAO.validarModificacionMonto:  "+ ex);
		}
		return validado;
	}

	/** ******VALUE CHANGE LISTENER DE CAMBIO *********************************** */
	public void cambioChanged(ValueChangeEvent ev) {
		try{
			List lstFacturasSelected = (List) m.get("facturasSelected");
			Hfactura hFac = (Hfactura) lstFacturasSelected.get(0);
			procesarCambio(hFac);
		}catch(Exception ex){
			
		}
	}

/***********ACTION LISTENER DE CAMBIO*****************************************************************/
	public void aplicarCambio(ActionEvent ev) {
		try{
			List lstFacturasSelected = (List) m.get("facturasSelected");
			Hfactura hFac = (Hfactura) lstFacturasSelected.get(0);
			procesarCambio(hFac);
		}catch(Exception ex){
			
		}
	}

/******************************PROCESAR CAMBIO****************************************************/
	public void procesarCambio(Hfactura hFac) {
		SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
		boolean valido = false;
		double dCambioDom = 0.0;
		BigDecimal tasa = BigDecimal.ZERO;
		Divisas divisas = new Divisas();
		Tcambio[] tcJDE = null;	
		String sMonedaBase = "";
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		try {
			//obtener companias x caja
			sMonedaBase = cCtrl.sacarMonedaBase((F55ca014[])m.get("cont_f55ca014"), hFac.getCodcomp());
			valido = validarCambio();
			if (valido) {
				txtCambioForaneo.setStyleClass("frmInput2");
				double dMontoAplicar = divisas.formatStringToDouble(txtMontoAplicar.getValue().toString());
				double montoRecibido = divisas.formatStringToDouble(txtMontoRecibido.getValue().toString());
				double cambio = montoRecibido - dMontoAplicar;
				double dCambioForaneo = divisas.formatStringToDouble(txtCambioForaneo.getValue().toString());
				if (dCambioForaneo > cambio) {
					txtCambioForaneo.setValue(divisas.formatDouble(divisas.roundDouble4(cambio)));
					txtCambioDomestico.setValue("0.00");
				} else {
					
					tasa =  obtenerTasaOficial();
					m.put("bdTasa", tasa);
					
					if(hFac.getMoneda().equals(sMonedaBase)){
						
						dCambioDom = new BigDecimal(Double.toString(cambio)).subtract(
										new BigDecimal(Double.toString(dCambioForaneo))
											.multiply(tasa) ).doubleValue();
					}else{
						dCambioDom = new BigDecimal(Double.toString(cambio)).subtract(
								new BigDecimal(Double.toString(dCambioForaneo)))
								.multiply(tasa).doubleValue();
					}					
					txtCambioDomestico.setValue(divisas.formatDouble(dCambioDom));
				}
			}
			srm.addSmartRefreshId(txtCambioForaneo.getClientId(FacesContext.getCurrentInstance()));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

/****************VALIDACIONES DE CAMBIO**************************************************/
	public boolean validarCambio() {
		boolean validado = true;
		String sCambio = "";
		Divisas divisas = new Divisas();
		try {
			// expresion regular solo numeros
			Matcher matNumero = null;
			if (txtMonto.getValue() != null) {
				sCambio = txtCambioForaneo.getValue().toString().trim();
				Pattern pNumero = Pattern.compile("^[0-9]+\\.?[0-9]*$");
				matNumero = pNumero.matcher(sCambio);
			}
			if (sCambio.equals("")) {
				txtCambioForaneo.setStyleClass("frmInput2Error");
				validado = false;
			} else if (!matNumero.matches()) {
				txtCambioForaneo.setStyleClass("frmInput2Error");
				validado = false;
			} else if (divisas.formatStringToDouble(sCambio) < 0) {
				txtCambioForaneo.setStyleClass("frmInput2Error");
				validado = false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return validado;
	}

/**************************************************************************************************************/
/*******************************PROCESAR RECIBO*******************************************************************************/
	public void procesarRecibo(ActionEvent ev) {
		boolean valido = false;
		boolean cambiado = true;
		Hfactura hFac = null;
		try{
			
			//&& ===== Validar si la caja no esta bloqueada.
			String sMensaje = CtrlCajas.generarMensajeBlk();
			if(sMensaje.compareTo("") != 0){
				lblMensajeValidacion.setValue(sMensaje);
				dwProcesa.setStyle("width:400px; min-height:200px;");
				dwProcesa.setWindowState("normal");
				return;
			}
			
			//&& ======= Continuar con las validaciones normales.
			hFac = (Hfactura) ((List) m.get("facturasSelected")).get(0);
			if (txtCambio.getValue().toString().trim().equals("")) {
				procesarCambio(hFac);
			}
			if (!txtCambioForaneo.getValue().toString().trim().equals("")) {
				cambiado = validarCambio();
			}
			valido = validarDatosRecibo();
			if (valido && cambiado) {
				restablecerEstilos();
				dwProcesa.setWindowState("hidden");
				dwImprime.setWindowState("normal");
				chkImprimir.setChecked(false);
			} else {
				dwProcesa.setWindowState("normal");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

/****************VALIDAR DATOS DEL RECIBO ANTES DE PROCESAR****************************************/
	public boolean validarDatosRecibo() {
		Divisas divisas = new Divisas();
		boolean validado = true;
		ReciboCtrl rpCtrl = new ReciboCtrl();
		int iNumRecm = 0;
		String sMensajeError = "";
		int y = 145;
		double dMontoRecibido = 0.0, dMontoAplicar = 0.0;
		Vf55ca01 vf55ca01 = null;
		Hfactura hfac = null;
		try {
			vf55ca01 = (Vf55ca01) ((List) m.get("lstCajas")).get(0);
			hfac = (Hfactura) ((List) m.get("facturasSelected")).get(0);
			restablecerEstilos();
			selectedMet = (ArrayList<MetodosPago>) m.get("lstPagos");
			// expresion regular de valores alfanumericos
			Matcher matAlfa = null;
			if (txtConcepto.getValue() != null) {
				String sObs = txtConcepto.getValue().toString();
				Pattern pAlfa = Pattern.compile("^[A-Za-z0-9-.;,\\p{Blank}]+$");
				matAlfa = pAlfa.matcher(sObs);
			}

			/** ****************RECIBO AUTOMATICO*********************** */
			if (cmbTiporecibo.getValue().toString().equals("AUTOMATICO")) {
				txtNumRec.setStyle("display:none");
				// validar metodos de pago
				if (selectedMet == null || selectedMet.isEmpty()) {
					sMensajeError = sMensajeError
							+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> No se han agregado pagos al recibo<br>";
					metodosGrid.setStyleClass("igGridError");
					validado = false;
					y = y + 14;
				}
				if (!txtConcepto.getValue().toString().trim().equals("")) {
					if (!matAlfa.matches()) {
						sMensajeError = sMensajeError
								+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El campo concepto contiene caracteres invalidos <br>";
						txtConcepto.setStyleClass("frmInput2Error");
						validado = false;
						y = y + 14;
					}
				}
				if (txtConcepto.getValue().toString().length() > 250) {
					sMensajeError = sMensajeError
							+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> La longitud del campo es muy alta (lim. 250) <br>";
					txtConcepto.setStyleClass("frmInput2Error");
					validado = false;
					y = y + 14;
				}
				dMontoRecibido = divisas.formatStringToDouble(txtMontoRecibido.getValue().toString());
				dMontoAplicar = divisas.formatStringToDouble(txtMontoAplicar.getValue().toString());
				if (dMontoAplicar > dMontoRecibido) {
					sMensajeError = sMensajeError
							+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El monto recibido debe ser mayor al monto a aplicar <br>";
					validado = false;
					y = y + 14;
				}
				double cambio = dMontoRecibido - dMontoAplicar;
				if (!txtCambioForaneo.getValue().toString().trim().equals("")) {
					double dCambioForaneo = divisas.formatStringToDouble(txtCambioForaneo.getValue().toString());
					if (dCambioForaneo > cambio) {
						txtCambioForaneo.setValue(divisas.formatDouble(cambio));
						txtCambioDomestico.setValue("0.00");
						sMensajeError = sMensajeError
								+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> La cantidad de cambio ingresada es mayor al correcto <br>";
						validado = false;
						y = y + 20;
					}
				}
				/** ****************RECIBO MANUAL*********************** */
			} else {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				Matcher matFecha = null;
				Date dFecha = null;
				String sFecha = null;

				if (txtFecham.getValue() != null) {
					dFecha = (Date) txtFecham.getValue();
					sFecha = sdf.format(dFecha);
					Pattern pFecha = Pattern.compile("^[0-3]?[0-9](/|-)[0-2]?[0-9](/|-)[1-2][0-9][0-9][0-9]$");
					matFecha = pFecha.matcher(sFecha);
				}
				// expresion regular solo numeros
				Matcher matNumero = null;
				if (txtNumRec.getValue() != null) {
					Pattern pNumero = Pattern.compile("^[0-9]*$");
					matNumero = pNumero.matcher(txtNumRec.getValue().toString().trim());
					if (matNumero.matches()&& !txtNumRec.getValue().toString().trim().equals("")) {
						iNumRecm = Integer.parseInt(txtNumRec.getValue().toString());
					}
				}
				// validar metodos de pago
				if (selectedMet == null || selectedMet.isEmpty()) {
					sMensajeError = sMensajeError
							+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> No se han agregado pagos al recibo<br>";
					metodosGrid.setStyleClass("igGridError");
					validado = false;
					y = y + 14;
				}
				// valida la fecha del recibo
				if (txtFecham.getValue() == null || (txtFecham.getValue().toString().trim()).equals("")) {
					sMensajeError = sMensajeError
							+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Fecha de recibo requerida<br>";
					txtFecham.setStyleClass("frmInput2Error2");
					validado = false;
					y = y + 14;
				}
				if (matFecha == null || !matFecha.matches()) {
					sMensajeError = sMensajeError
							+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Fecha de recibo no es valida<br>";
					txtFecham.setStyleClass("frmInput2Error");
					validado = false;
					y = y + 14;
				}
				// validar el numero de recibo
				if (txtNumRec.getValue() == null || (txtNumRec.getValue().toString().trim()).equals("")) {
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Número de recibo manual es requerido<br>";
					txtNumRec.setStyleClass("frmInput2Error");
					validado = false;
					y = y + 14;
				}
				if (matNumero == null || !matNumero.matches()) {
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Número de recibo no es valido<br>";
					txtNumRec.setStyleClass("frmInput2Error");
					validado = false;
					y = y + 14;
				}
				if (txtConcepto.getValue().toString().length() > 250) {
					sMensajeError = sMensajeError+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> La longitud del campo es muy alta (lim. 250) <br>";
					txtConcepto.setStyleClass("frmInput2Error");
					validado = false;
					y = y + 14;
				}
				// validar existencia del numero de recibo manual
				if (iNumRecm > 0) {
					if (rpCtrl.verificarNumeroRecibo(vf55ca01.getId().getCaid(), hfac.getCodcomp(),iNumRecm, vf55ca01.getId().getCaco().trim())) {
						sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El número de recibo manual ya existe!<br>";
						txtNumRec.setStyleClass("frmInput2Error");
						validado = false;
						y = y + 14;
					}
				}
				dMontoRecibido = divisas.formatStringToDouble(txtMontoRecibido.getValue().toString());
				dMontoAplicar = divisas.formatStringToDouble(txtMontoAplicar.getValue().toString());
				if (dMontoAplicar > dMontoRecibido) {
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El monto recibido debe ser mayor al monto a aplicar <br>";
					// txtConcepto.setStyleClass("frmInput2Error");
					validado = false;
					y = y + 20;
				}
				double cambio = divisas.roundDouble4(dMontoRecibido - dMontoAplicar);
				if (!txtCambioForaneo.getValue().toString().trim().equals("")) {
					double dCambioForaneo = divisas.formatStringToDouble(txtCambioForaneo.getValue().toString());
					if (dCambioForaneo > cambio) {
						txtCambioForaneo.setValue(divisas.formatDouble(cambio));
						txtCambioDomestico.setValue("0.00");
						sMensajeError = sMensajeError
								+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> La cantidad de cambio ingresada es mayor al correcto <br>";
						validado = false;
						y = y + 20;
					}
				}
			}
			lblMensajeValidacion.setValue(sMensajeError);
			dwProcesa.setStyle("width:390px;height:" + y + "px");
		} catch (Exception ex) {
			validado = false;
			ex.printStackTrace();
		}
		return validado;
	}

/*****************REESTABLECER EL ESTILO DE LOS COMPONENTES********************************************************/
	public void restablecerEstilos() {
		txtNumRec.setStyleClass("frmInput2");
		metodosGrid.setStyleClass("igGrid");
		txtFecham.setStyleClass("");
		txtConcepto.setStyleClass("frmInput2");
	}

/******************************************************************************************************************* */
	public void cancelarProcesa(ActionEvent e) {
		dwImprime.setWindowState("hidden");
	}

/***************************** RESTAR EL CAMBIO A LOS PAGOS *******************************/
	public boolean aplicarCambiosEnPago(Hfactura hFac, List<MetodosPago>lstMetodosPago,
					int iCaid, String sCodcomp, int iNumrec, String sCajero,
					Session sesion, Transaction trans, String sMonedaBase, 
					Vf55ca01 f55Caja, Vautoriz vautoriz, double dTotalAplicar ){
		boolean aplicado = true;
		
		try {
			Divisas dv = new Divisas();
			String sTipoDoc = hFac.getTipofactura();
			String sCambio1 = new String("");
			String sCambio2 = new String("");
			String[] sLblCambios1;
			String[] sLblCambios2;
			
			double dCambio1 = 0;
			double dCambio2 = 0;
			double dNewMonto1 = 0;
			double dNewMonto2 = 0;
			double dMontoFicha = 0;
			boolean paso1 = false;
			boolean paso2 = false;
			boolean imprimir = chkImprimir.isChecked();
			
			BigDecimal bdTasa = BigDecimal.ONE;
			
			String sLblCambio1 = lblCambio.getValue().toString();
			String sLblCambio2 = lblCambioDomestico.getValue().toString();
			
			if (txtCambio.getValue().toString().trim().equals("")) {//hay cambio mixto todos los  pagos fueron en usd
				sCambio1 = txtCambioForaneo.getValue().toString();
				dCambio1 = dv.formatStringToDouble(sCambio1);
				sCambio2 = txtCambioDomestico.getValue().toString();
				dCambio2 = dv.formatStringToDouble(sCambio2);
	
				sLblCambios1 = sLblCambio1.trim().split(" ");
				sLblCambios2 = sLblCambio2.trim().split(" ");
				
				sLblCambio1 = sLblCambios1[sLblCambios1.length - 1].substring(0, 3);
				sLblCambio2 = sLblCambios2[sLblCambios1.length - 1].substring(0, 3);
				
				bdTasa = obtenerTasaOficial(); 
				
				for (int i = 0; i < lstMetodosPago.size(); i++) {
					
					MetodosPago mp =  lstMetodosPago.get(i);
					
					if (mp.getMetodo().equals( MetodosPagoCtrl.EFECTIVO)&& !mp.getMoneda().equals(sMonedaBase)&& !paso1) {				
						
						if (hFac.getMoneda().equals(sMonedaBase)) {
							
							if(dCambio2 > 0){//hay cambio domestico
								
								dNewMonto1 = dv.roundDouble4(mp.getEquivalente() - dCambio2);
								dMontoFicha = dv.roundDouble4(dCambio2/bdTasa.doubleValue());
								
								mp.setMonto(dv.roundDouble4(dNewMonto1/ mp.getTasa().doubleValue()));
								mp.setEquivalente(dNewMonto1);
								
								if(dMontoFicha > 0){
									
									MetodosPago metPagoFicha = new MetodosPago( MetodosPagoCtrl.EFECTIVO,mp.getMoneda(), dMontoFicha, mp.getTasa(), 
																				dCambio2, "", "","", "","0", iCaid);
									lstPagoFicha = new ArrayList();
									lstPagoFicha.add(metPagoFicha);
									aplicado = insertarFichaCV(sesion, trans, 
												null, f55Caja,
												hFac, vautoriz, dMontoFicha, 
												iNumrec, sCajero,lstPagoFicha);
									bExisteFichaCambio = true;
								}
							}
							if(dCambio1 > 0){
								
								dNewMonto1 = mp.getMonto() - dCambio1;								
								mp.setMonto(dNewMonto1);
								dNewMonto1 = dCambio1*mp.getTasa().doubleValue();
								dNewMonto2 = mp.getEquivalente();
								dNewMonto1 = dv.roundDouble4(dNewMonto2 - dNewMonto1);
								mp.setEquivalente(dNewMonto1);
								lstMetodosPago.set(i, mp);
								
								paso1 = true;
							}
							paso1 = true;
						}else{
							if(dCambio2 > 0){ // cambio2 =  cambio cordobas.
								
								dNewMonto1 = mp.getMonto() - dv.roundDouble4((mp.getMonto() - dTotalAplicar));
								
								dMontoFicha = new BigDecimal(String.valueOf(dCambio2))
											.divide(bdTasa, 2, RoundingMode.HALF_UP )
											.doubleValue();
								
								mp.setMonto(dNewMonto1);
								mp.setEquivalente(dNewMonto1);
								
								if(dMontoFicha > 0){
									MetodosPago metPagoFicha = new MetodosPago
											( MetodosPagoCtrl.EFECTIVO,mp.getMoneda(), dMontoFicha, 
												hFac.getTasa(), dCambio2, "", 
												"","", "","0", iCaid);
									
									lstPagoFicha = new ArrayList();
									lstPagoFicha.add(metPagoFicha);
									aplicado = insertarFichaCV(sesion, trans,
											null, f55Caja, hFac, vautoriz,
											dMontoFicha, iNumrec, sCajero,
											lstPagoFicha);
									bExisteFichaCambio = true;
								}
							}
							if(dCambio1 > 0 && dCambio2 == 0){ // cambio1 =  cambio dolares.
								dNewMonto1 = mp.getMonto() - dCambio1;								
								mp.setMonto(dNewMonto1);
								mp.setEquivalente(dNewMonto1);
								paso1 = true;
							}
							lstMetodosPago.set(i, mp);
						}
						
					} else if (mp.getMetodo().equals( MetodosPagoCtrl.EFECTIVO)&& mp.getMoneda().equals(sLblCambio2)&& !paso2) {
						dNewMonto2 = mp.getMonto() - dCambio2;
						mp.setMonto(dNewMonto2);
						mp.setEquivalente(dNewMonto2 * mp.getTasa().doubleValue());
						lstMetodosPago.set(i, mp);
						paso2 = true;
					}
				}
			} else {// hay cambio en una moneda unicamente
				sCambio1     = txtCambio.getValue().toString();
				dCambio1     = dv.formatStringToDouble(sCambio1);
				sLblCambios1 = sLblCambio1.trim().split(" ");
				sLblCambio1  = sLblCambios1[sLblCambios1.length - 1].substring(0, 3);
				
				for (int i = 0; i < lstMetodosPago.size(); i++) {
					MetodosPago mp = lstMetodosPago.get(i);
					
					if (mp.getMetodo().equals( MetodosPagoCtrl.EFECTIVO) && mp.getMoneda().equals(sLblCambio1) && !paso1) {
						if (sLblCambio1.equals(sMonedaBase)&& hFac.getMoneda().equals(sMonedaBase)) {// se saca el cambio en cor del pago en cor
							dNewMonto1 = mp.getMonto() - dCambio1;
							mp.setMonto(dNewMonto1);
							mp.setEquivalente(dNewMonto1);
						} else if (sLblCambio1.equals(sMonedaBase) && 
								!hFac.getMoneda().equals(sMonedaBase) &&
								lstMetodosPago.size() > 1) {// se saca el cambio del pago en cor y se convierte a usd con tasa del metodo
							dNewMonto1 = mp.getMonto() - dCambio1;
							mp.setMonto(dNewMonto1); //BigDecimal.valueOf(dNewMonto1).divide(mp.getTasa(),2, RoundingMode.HALF_UP)
							mp.setEquivalente(dv.roundDouble( (dNewMonto1 / mp.getTasa().doubleValue()), 4 ) );
						} else 
							if (sLblCambio1.equals(sMonedaBase) && 
									!hFac.getMoneda().equals(sMonedaBase) && 
									lstMetodosPago.size() == 1) {// si pago toda la fac de usd en cor
					
//							dNewMonto1 = mp.getMonto() - 
//											(mp.getMonto() 
//												- dTotalAplicar * hFac.getTasa().doubleValue());
								
							dNewMonto1 = mp.getMonto() - dCambio1;
							double dtasaTemp =  dv.roundDouble4(mp.getMonto()/mp.getEquivalente());
								
							dNewMonto1 = dv.roundDouble4(dNewMonto1);
							mp.setMonto(dNewMonto1);
//							mp.setEquivalente(dv.roundDouble4(dNewMonto1/ mp.getTasa().doubleValue()));
							mp.setEquivalente(dv.roundDouble4(dNewMonto1/ dtasaTemp));
						
						} else if (sLblCambio1.equals(sMonedaBase) &&
									hFac.getMoneda().equals(sMonedaBase) &&
									lstMetodosPago.size() == 1) {// el pago en usd cubre toda la venta
							dNewMonto1 = mp.getEquivalente() - dCambio1;
							mp.setMonto(dv.roundDouble4(dNewMonto1 / mp.getTasa().doubleValue()));
							mp.setEquivalente(dNewMonto1);
						}
						lstMetodosPago.set(i, mp);
						paso1 = true;
						
					} else if (mp.getMetodo().equals( MetodosPagoCtrl.EFECTIVO) && 
								sLblCambio1.equals(sMonedaBase) && 
								!mp.getMoneda().equals(sMonedaBase) 
								&& !paso1) {
					
						if (hFac.getMoneda().equals(sMonedaBase) && lstMetodosPago.size() == 1) {// el pago en usd cubre toda la venta
							dNewMonto1 = mp.getEquivalente() - dCambio1;
							dMontoFicha = mp.getMonto() - dv.roundDouble4(dNewMonto1/ mp.getTasa().doubleValue());

							mp.setMonto(dv.roundDouble4(dNewMonto1/ mp.getTasa().doubleValue()));
							mp.setEquivalente(dNewMonto1);
							
							if(dMontoFicha > 0){
								MetodosPago metPagoFicha = new MetodosPago( MetodosPagoCtrl.EFECTIVO,mp.getMoneda(),
										dMontoFicha,	mp.getTasa(), 
										dCambio1, "", "","", "","0",iCaid);
								lstPagoFicha = new ArrayList<MetodosPago>();
								lstPagoFicha.add(metPagoFicha);
								
								aplicado = insertarFichaCV(sesion, trans, 
										null,f55Caja, hFac, vautoriz, 
										dMontoFicha, iNumrec,
										sCajero,lstPagoFicha);
								bExisteFichaCambio = true;
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			
			msgValidaSockPos = "Error al guardar ficha de " +
					"compra/venta para el recibo";
			aplicado = false;

		}finally{
			msgValidaSockPos = new String("");
			if(!aplicado && bExisteFichaCambio)
				msgValidaSockPos = "Error al guardar ficha de " +
						"compra/venta para el recibo";
		}
		return aplicado;
	}

	
/*************************** PROCESAR EL RECIBO DE CONTADO *******************************/
	
	public void procesaReciboContado(ActionEvent ev){
		Divisas divisas = new Divisas();
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		
		MetodosPago[] metPago = null;
		
		boolean aplicado = true;
		boolean bHayFicha = false;
		boolean bHayPagoSocket = false;
		boolean bAplicadoSockP = false;
		
		int caid = 0;
		int iNumrec = 0;
		double dMontoFicha = 0;
		String codsuc = new String("");
		String codcomp = new String("");
		String tiporec = valoresJdeIns[0];
		String msgError = new String("");
		F55ca014 dtComp = null;
		
		//Connection cn1 = null;
		Session sesion = null;
		Transaction tx = null;
		
		List<MetodosPago> lstMetodosPago = new ArrayList<MetodosPago>();
		List<Hfactura> lstFacturasSelected = new ArrayList<Hfactura>();
		
		
		int ihascode =  0; 		
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss ss");
		Date dtIniciaPago = new Date();
		Date dtTerminaPago = new Date();
		boolean oneclick = false;
		
		List<String[]>dtaFacs = new ArrayList<String[]>();
		int iNumrecTmp = 0 ;
		Hfactura hFac = null;
		Date dtTimeIni = new Date();
		long idproceso = 0 ;
		Vautoriz[] vautoriz = null;
		
		Object[] dtaCnDnc = null;
		boolean cnDonacion = false;
		boolean aplicadonacion = false;
		List<MetodosPago>pagosConDonacion;
		
		try {
			
			LogCajaService.CreateLog("procesaReciboContado", "INFO", "INCIO METODO => procesaReciboContado");
			
			oneclick = m.containsKey("coProcesoCancelaFactura") ;
			if(oneclick){
				aplicado = false;
				return;
			}
			m.put("coProcesoCancelaFactura", "true");
			
			msgValidaSockPos = new String("");
			bExisteFichaCambio = false;
			lstPagoFicha = null;
			NoFichaCV = 0;
			dwImprime.setWindowState("hidden");
			dwRecibo.setWindowState("hidden");
			dwRecibo.setWindowState("hidden");
			//&& ===== Datos globales a utilizar.
			List<MetodosPago> pagos = (ArrayList<MetodosPago>) m.get("lstPagos");
			List<Hfactura> facturas = (ArrayList<Hfactura>) m.get("facturasSelected");
			
			
			for (Hfactura hf : facturas){ 
				lstFacturasSelected.add(hf.clone());
				
				String[] dta = new String[] {
					String.valueOf(hf.getNofactura()),
					String.valueOf(hf.getTotal()), hf.getPartida(),
					hf.getCodunineg(), hf.getCodsuc(), hf.getTipofactura() };
				dtaFacs.add(dta);
			}
			
			if(HibernateUtilPruebaCn.currentSession().getTransaction().isActive()) {
				return;
			}
			for (MetodosPago mp : pagos) 
				lstMetodosPago.add(mp.clone());
			
			Vf55ca01 caja = (Vf55ca01) ((List) m.get("lstCajas")).get(0);
			hFac = (Hfactura) lstFacturasSelected.get(0);
			vautoriz = (Vautoriz[]) m.get("sevAut");
			String sCajero  = (String) m.get("sNombreEmpleado");
			
			double dTotalAplicar = divisas.formatStringToDouble(txtMontoAplicar.getValue().toString());
			 
			caid  = caja.getId().getCaid();
			codsuc = caja.getId().getCaco();
			codcomp = hFac.getCodcomp();
			
			
			//&& ============validar que no se hayan pagado las facturas
			String msg = com.casapellas.controles.tmp.ReciboCtrl.validatePaymentForInvoice(facturas, caid) ;
			if(!msg.isEmpty()){
				aplicado = false;
				msgError = msg;
				throw new Exception(msg);
			}

			dtComp = CompaniaCtrl.filtrarCompania((F55ca014[])
							m.get("cont_f55ca014"), codcomp);
			
			String sMonedaBase = dtComp.getId().getC4bcrcd();
			
			//&& =========== validacion para impedir que se  cancele dos veces.
			iNumrecTmp = new com.casapellas.controles.ReciboCtrl()
							.procesoPagoFactura2(dtaFacs, caid, codcomp, 
									hFac.getFechajulian(),  hFac.getCodcli(), tiporec);
			
			if(iNumrecTmp == 0){
				aplicado = false;
				msgError = "Pago a factura en proceso o ya aplicado";
				throw new Exception(msgError);
			}

			//&& ========= Aplicar el redondeo a los equivalentes del pago.
			for (int i = 0; i < lstMetodosPago.size(); i++) {
				MetodosPago m = lstMetodosPago.get(i);
				m.setEquivalente(divisas.roundDouble(m.getEquivalente(), 2));
				lstMetodosPago.set(i, m);
				
				if(m.getMetodo().compareTo(MetodosPagoCtrl.TARJETA) == 0 && 
						m.getVmanual().compareTo("2") == 0 )
					bHayPagoSocket = true;
			}
			//&& ========= aplicar el pago de Socket Pos 
			if( bHayPagoSocket ){
				String msgSocket =  PosCtrl.aplicarPagoSocketPos( 
									lstMetodosPago, hFac.getNomcli(),
									caid,codcomp, tiporec);
				
				aplicado = msgSocket.compareTo("") == 0;
				bAplicadoSockP = aplicado;
				msgError = msgSocket;
				
				if(!aplicado) throw new Exception(msgError);;
			}
			
			//&& ========= Grabar el recibo de caja
			sesion = HibernateUtilPruebaCn.currentSession();
			tx =  sesion.beginTransaction();
		
			//&& ========== obtener pagos con donaciones
			pagosConDonacion =  new ArrayList<MetodosPago>( (ArrayList<MetodosPago>)
				CollectionUtils.select(lstMetodosPago, new Predicate(){
					public boolean evaluate(Object o) {
						return ((MetodosPago)o).isIncluyedonacion();
					}
				})
			);
			//&& ========== remover pagos que son donaciones unicamente.
			CollectionUtils.filter(lstMetodosPago, new Predicate(){
				public boolean evaluate(Object o) {
					return !(	((MetodosPago)o).isIncluyedonacion() 
								&& ((MetodosPago)o).getEquivalente() == 0
								&& ((MetodosPago)o).getMonto() == 0 )  ;
				}
			});
			aplicadonacion = !pagosConDonacion.isEmpty();
			//&& ========== ====================================
			
			aplicado = insertarRecibo(sesion, tx, null, caja, hFac, vautoriz, lstMetodosPago );
			
			if(!aplicado){
				msgError = (strMensajeValidacion == null || strMensajeValidacion.compareTo("") == 0) ? 
						"Recibo no aplicado, Error al grabar datos " +
						"del recibo en caja"
						: strMensajeValidacion;		
				throw new Exception(msgError);
			}
			
			iNumrec = Integer.valueOf(m.get("iNumRec").toString());
			
			
			//*********************************************************
			try{ihascode = ev.hashCode() ;}catch(Exception e ){ }
			
			
			//&& ======== remover sobrante de los pagos.
			if( m.containsKey("sco_SobrantePago") ){
				double dMontorec  = 0;
				double dMontoNeto = 0;
				double dDiferencia= 0;
				double dMonto = 0;
				double dEquiv = 0;
				MetodosPago mpSobrante = (MetodosPago)m.get("sco_MpagoSobrante");
				
				dMontoNeto = divisas.formatStringToDouble(txtMontoAplicar.getValue().toString());
				dMontorec =  divisas.formatStringToDouble(txtMontoRecibido.getValue().toString());
				dDiferencia = dMontorec - dMontoNeto;
				dDiferencia = divisas.roundDouble4(dDiferencia);
				
				for(int i = lstMetodosPago.size()-1; i >= 0; i--){
					MetodosPago mpago = lstMetodosPago.get(i);
					if(mpago.equals(mpSobrante)){
						dEquiv = mpago.getEquivalente();
						dMonto = mpago.getMonto();
						
						dEquiv -= dDiferencia;
						dMonto = (mpago.getEquivalente()>mpago.getMonto())?
									dEquiv / mpago.getTasa().doubleValue():
									dEquiv * mpago.getTasa().doubleValue();
						
						dEquiv = divisas.roundDouble4(dEquiv);
						dMonto = divisas.roundDouble4(dMonto);
						mpago.setMonto(dMonto);
						mpago.setEquivalente(dEquiv);
						lstMetodosPago.set(i,mpago);
						break;
					}
				}
				m.put( "lstPagos", lstMetodosPago);
			}
			//&& ========= Restar cambios a pagos y grabar ficha de compra venta.
			aplicado = aplicarCambiosEnPago(hFac, lstMetodosPago, caid, 
								codcomp, iNumrec, sCajero, sesion, tx,
								sMonedaBase, caja, vautoriz[0], dTotalAplicar);
			
			for (MetodosPago m : lstMetodosPago) {
				m.setMonto(Divisas.roundDouble(m.getMonto(), 2)) ;
				m.setEquivalente(Divisas.roundDouble(m.getEquivalente(), 2));
			}

			if(!aplicado){
				msgError = (strMensajeValidacion == null || 
							strMensajeValidacion.compareTo("") == 0) ? 
						"Recibo no aplicado, Error al aplicar operaciones" +
						" sobre cambios otorgados"
						: strMensajeValidacion;		
				throw new Exception(msgError);
			}
				
			//&& ========  Actualizar los estados de traslado de factura
			for (Hfactura hf : lstFacturasSelected) {
				Trasladofac tf = new TrasladoCtrl()
							.buscarTrasladofac(sesion, hf, caid, 0, "");
				if(tf == null) 
					continue;
				
				aplicado = TrasladoCtrl.actualizarEstadoTraslado(sesion, tf, "P");
				
				if(!aplicado){
					msgError = "Recibo no aplicado: Error de actualización" +
							" de traslado de factura " +hf.getNofact();
					throw new Exception(msgError);
				}
			}
			
			//&& ======== Aplicar los asientos de diario por el recibo.
			strMensajeValidacion = new String("");
			aplicado = generarAsientos(sesion, tx, caid, codcomp, 
						dTotalAplicar, lstFacturasSelected, lstMetodosPago,
						iNumrec, bExisteFichaCambio, lstPagoFicha, 
						sMonedaBase, new Date()); 


			if(!aplicado){
				
				msgError = (strMensajeValidacion == null || 
						strMensajeValidacion.trim().compareTo("") == 0) ? 
							"Recibo no aplicado, Error al generar asientos" +
							" de diario para factura"
							: strMensajeValidacion;	
				throw new Exception(msgError);
			}
			
			//&& =========== asientos de diario por sobrantes de pago.
			if(m.containsKey("sco_SobrantePago")){
				MetodosPago mpago = (MetodosPago)m.get("sco_MpagoSobrante");
				double dSobrante = divisas.formatStringToDouble(
								String.valueOf(m.get("sco_SobrantePago")));
				
				dSobrante = Divisas.roundDouble(dSobrante) ;
				
				if( dSobrante > 0 ){

					aplicado = registarAsientosxSobrantes(sesion, tx, mpago,
									vautoriz[0], caja, iNumrec, codcomp, 
									hFac.getCodunineg(), dSobrante, 
									sMonedaBase, hFac);
					if(!aplicado){
						msgError = ( m.containsKey("sMsgErrorSobrante") )? 
									"No se ha podido registrar el sobrante de pago en JDE":
									String.valueOf(m.get("sMsgErrorSobrante"));
						throw new Exception(msgError);
					}
				}
			}
			
			
			//&& =========== donaciones recibidas en el recibo
			if(aplicado && aplicadonacion ){
				
				DonacionesCtrl.msgProceso = "";
				DonacionesCtrl.caid = caid;
				DonacionesCtrl.codcomp = codcomp;
				DonacionesCtrl.numrec = iNumrec;
				DonacionesCtrl.tiporecibo = tiporec;
				
				aplicado = DonacionesCtrl.grabarDonacion(pagosConDonacion, vautoriz[0],  hFac.getCodcli() );
				
				if( !aplicado ){
					msgError = DonacionesCtrl.msgProceso;
					if(msgError == null || msgError.isEmpty()) {
						msgError = "Error al grabar la donacion ";
					}else{ 
						msgError = "Procesar Donacion: " + msgError; 
					}
					throw new Exception(msgError);
				}
			}
			
		} catch (Exception ex) {
			LogCajaService.CreateLog("procesaReciboContado", "ERR", ex.getMessage());
			aplicado = false;			
			msgError = "Recibo no aplicado, favor intentar nuevamente";
		}finally{
			
			try
			{
				//++++++++++++++++++++++++++++++++++++++++
				//Agregado por LFonseca
				//----------------------------------------
				Vf55ca01 caja = (Vf55ca01) ((List) m.get("lstCajas")).get(0);
				int caid2 = caja.getId().getCaid();	
				int iCajero = caja.getId().getCacati();
				 
				List<Hfactura>  lstFacturasSelected4 = (List<Hfactura>) m.get("facturasSelected");
				
				VerificarFacturaProceso verificarFac = new VerificarFacturaProceso();
				for(Object o : lstFacturasSelected4){
					Hfactura hfactura = (Hfactura)o;
					
					String[] strResultado =  verificarFac.actualizarVerificarFactura(String.valueOf(caid2), 
															String.valueOf(hfactura.getCodcli()), 
															String.valueOf(hfactura.getNofactura()), 
															hfactura.getTipofactura(), 
															"", 
															String.valueOf(iCajero));
					 
					
				}
			}
			catch (Exception ex4) {
				LogCajaService.CreateLog("procesaReciboContado", "ERR", ex4.getMessage());
			}
			
			boolean actualizaRf = false;

			if(oneclick){
				lblMensajeValidacion.setValue("Pago de factura en otro proceso");
				dwProcesa.setWindowState("normal");
				dwProcesa.setStyle("width:370px;height:160px");
				dwImprime.setWindowState("hidden");
				dwRecibo.setWindowState("hidden");
				return;
			}
			
			lblMensajeValidacion.setValue(msgError);
			dwProcesa.setWindowState("normal");
			dwProcesa.setStyle("width:370px;height:160px");
			dwImprime.setWindowState("hidden");
			dwRecibo.setWindowState("hidden");

			try {
				if(aplicado) {				
					tx.commit();
					// Marcar el recibo como satisfactorio
					BitacoraSecuenciaReciboService.actualizarSatisfactorioLogReciboNumerico(caid, iNumrec, codcomp,codsuc, valoresJdeIns[0]);
					
					LogCajaService.CreateLog("procesaReciboContado", "INFO", "FIN METODO => procesaReciboContado");
				}
			} catch (Exception e) {
				LogCajaService.CreateLog("procesaReciboContado", "ERR", e.getMessage());
				msgError = "Error en confirmación de registro de valores: Caja";
				aplicado = false;
				e.printStackTrace();
				
				// *********************************
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
			}

			if(aplicado){
				
				if (bExisteFichaCambio) 
					ReciboCtrl.setNoFichaRecibo(iNumrec, NoFichaCV, caid,
												codcomp, codsuc, tiporec);
				
				dwRecibo.setWindowState("hidden");
				lblMensajeValidacion.setValue("Recibo aplicado Correctamente");
				
				//&& ====== Impresion de recibos
				if ( chkImprimir.isChecked() ) {
					CtrlCajas.imprimirRecibo(caid, iNumrec, 
								codcomp, codsuc, tiporec, false);
				}
				if(bHayPagoSocket){
					PosCtrl.imprimirVoucher(lstMetodosPago, "V", 
							dtComp.getId().getC4rp01d1(), 
							dtComp.getId().getC4prt());
				}

				//&& ======== refrescar lista de facturas.
				lstHfacturasContado = (ArrayList<Hfactura>) m.get
										("lstHfacturasContado");
				for (Hfactura hf : lstFacturasSelected) {
					final int numero = hf.getNofactura();
					final int codcli = hf.getCodcli();
					final int fecha = hf.getFechajulian();
					
					CollectionUtils.filter(lstHfacturasContado, new Predicate() {
						public boolean evaluate(Object hfac) {
							Hfactura f = (Hfactura)hfac;
							return !(
								f.getNofactura() == numero && 
								f.getCodcli() == codcli && 
								f.getFechajulian() == fecha);
						}
					});
				}
				m.put("lstHfacturasContado", lstHfacturasContado);
				gvHfacturasContado.dataBind();
				
			}else{
							
				//&& ========= revertir las transacciones registradas.
				try {
					if (tx != null) {						
						tx.rollback();
						
						try {
							List<CustomEmailAddress> list = new ArrayList<CustomEmailAddress>() {
								{
									add(new CustomEmailAddress("jaburto@casapellas.com"));
									add(new CustomEmailAddress("mpomares@casapellas.com"));
									add(new CustomEmailAddress("giovanny.acevedo@ahinko.com"));
									add(new CustomEmailAddress("byron.canales@ahinko.com"));							
								}
							};
							
							String error="Caja:" + caid + "Compania:"+ codcomp + " Numero Recibo:"+ iNumrec + " Posible mensaje mostrado:"+ msgError;
						MailHelper.SendHtmlEmail(new CustomEmailAddress(PropertiesSystem.WEBMASTER_EMAIL_ADRESS), list, "Fallo en generacion de recibo",error);
						}catch(Exception e) {}
					
					}
				} catch (Exception e) {
					LogCajaService.CreateLog("procesaReciboContado", "ERR", e.getMessage());					
				}

				//&& ======== anulacion del Socket
				if(bAplicadoSockP){
					
					String msgValidaSockPos = PosCtrl.revertirPagosAplicados
							(lstMetodosPago, caid, dtComp.getId().
								getC4rp01(), tiporec);
					
					lblMensajeValidacion.setValue(msgError + "\n" + msgValidaSockPos );
				}
			}
			
			HibernateUtilPruebaCn.closeSession(sesion);
			
			NoFichaCV = 0;
			
			m.remove("coProcesoCancelaFactura");
						
		}

	}
	
/***********PROCESA E IMPRIME EL RECIBO DE PAGO A FACTURAS DE CONTADO*******************************/
	public void ProcesaReciboContado(ActionEvent e) {
		Divisas divisas = new Divisas();
		MetodosPago[] metPago = null;
		Connection cn = null;
		Session s = null;
		Transaction tx = null;
		boolean insertado = false, bHayFicha = false;
		double dMontoFicha = 0.0;
		int iNumrec = 0;
		String sCajero = "";
		List lstPagoFicha = null;
		ReciboCtrl recCtrl = new ReciboCtrl();
		BigDecimal bdTasa = BigDecimal.ZERO;
		String sMonedaBase = "";
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		F55ca014[] f14 = null;
		
		try {

			s = HibernateUtilPruebaCn.currentSession();
			tx = s.getTransaction();
			if (tx == null || !tx.isActive()) {
				tx = s.beginTransaction();
			}

			// obtener conexion del datasource
			As400Connection as400connection = new As400Connection();
			cn = as400connection.getJNDIConnection("DSMCAJA2");
			cn.setAutoCommit(false);

			Vf55ca01 f55ca01 = (Vf55ca01) ((List) m.get("lstCajas")).get(0);
			// obtener datos de factura
			List<Hfactura> lstFacturasSelected = (ArrayList<Hfactura>) m.get("facturasSelected");
			Hfactura hFac = (Hfactura) lstFacturasSelected.get(0);
			Vautoriz[] vautoriz = (Vautoriz[]) m.get("sevAut");
			sCajero = (String) m.get("sNombreEmpleado");
			
			List<MetodosPago> lstMetodosPago = null;
			
			//obtener companias x caja
			f14 = (F55ca014[])m.get("cont_f55ca014");
			sMonedaBase = cCtrl.sacarMonedaBase(f14, hFac.getCodcomp());
			
			//leer nuevamente los datos del socketPOS
			lstMetodosPago = (List) m.get("lstPagos");
			insertado = true;//quitar esta linea para activar pos virtual
			if(insertado){		
				
				for (int i = 0; i < lstMetodosPago.size(); i++) {
					MetodosPago m = lstMetodosPago.get(i);
					m.setEquivalente(divisas.roundDouble(m.getEquivalente(), 2));
					lstMetodosPago.set(i, m);
				}
				
				//insertar encabezado de recibo
//				insertado = insertarRecibo(s, tx, cn, f55ca01, hFac, vautoriz );
				
				if (insertado) {
					
					//--- determinar si hay sobrante y restarlo del pago.
					if(m.get("sco_SobrantePago")!=null){
						double dMontorec  = 0;
						double dMontoNeto = 0;
						double dDiferencia= 0;
						double dMonto = 0,dEquiv=0;
						lstMetodosPago = null;
						MetodosPago mpSobrante = (MetodosPago)m.get("sco_MpagoSobrante");
						
						dMontoNeto = divisas.formatStringToDouble(txtMontoAplicar.getValue().toString());
						dMontorec =  divisas.formatStringToDouble(txtMontoRecibido.getValue().toString());
						dDiferencia = dMontorec - dMontoNeto;
						dDiferencia = divisas.roundDouble4(dDiferencia);					
						lstMetodosPago = (List<MetodosPago>) m.get("lstPagos");
						
						for(int i=lstMetodosPago.size()-1; i>=0; i--){
							MetodosPago mpago = lstMetodosPago.get(i);
							if(mpago.equals(mpSobrante)){
								dEquiv = mpago.getEquivalente();
								dMonto = mpago.getMonto();
								
								dEquiv -= dDiferencia;
								dMonto = (mpago.getEquivalente()>mpago.getMonto())?
											dEquiv / mpago.getTasa().doubleValue():
											dEquiv * mpago.getTasa().doubleValue();
								
								dEquiv = divisas.roundDouble4(dEquiv);
								dMonto = divisas.roundDouble4(dMonto);
								mpago.setMonto(dMonto);
								mpago.setEquivalente(dEquiv);
								lstMetodosPago.set(i,mpago);
								break;
							}
						}
						m.put( "lstPagos", lstMetodosPago);
					}
					
					iNumrec = Integer.valueOf(m.get("iNumRec").toString());
					int iCajaId = Integer.parseInt(m.get("sCajaId").toString());
					
					String sTipoDoc = hFac.getTipofactura();
					String sCodComp = hFac.getCodcomp();
					String sCambio1 = "", sCambio2 = "";
					String sLblCambio1 = "", sLblCambio2 = "";
					String[] sLblCambios1, sLblCambios2;
					
					double dCambio1 = 0.0, dCambio2 = 0.0, dNewMonto1 = 0.0, dNewMonto2 = 0.0;
					double dTotalAplicar = divisas.formatStringToDouble(txtMontoAplicar.getValue().toString());
					
					boolean paso1 = false, paso2 = false;
					boolean imprimir = chkImprimir.isChecked();
					
					metPago = new MetodosPago[lstMetodosPago.size()];
					lstMetodosPago = (List) m.get("lstPagos");			

					sLblCambio1 = lblCambio.getValue().toString();
					sLblCambio2 = lblCambioDomestico.getValue().toString();
					
					
					if (txtCambio.getValue().toString().trim().equals("")) {//hay cambio mixto todos los  pagos fueron en usd
						sCambio1 = txtCambioForaneo.getValue().toString();
						dCambio1 = divisas.formatStringToDouble(sCambio1);
						sCambio2 = txtCambioDomestico.getValue().toString();
						dCambio2 = divisas.formatStringToDouble(sCambio2);
	
						sLblCambios1 = sLblCambio1.trim().split(" ");
						sLblCambios2 = sLblCambio2.trim().split(" ");
						//
						sLblCambio1 = sLblCambios1[sLblCambios1.length - 1].substring(0, 3);
						sLblCambio2 = sLblCambios2[sLblCambios1.length - 1].substring(0, 3);
						//
						bdTasa = obtenerTasaOficial(); //bdTasa = lstFacturasSelected.get(0).getTasa();
						for (int i = 0; i < lstMetodosPago.size(); i++) {
							metPago[i] = (MetodosPago) lstMetodosPago.get(i);
							if (metPago[i].getMetodo().equals( MetodosPagoCtrl.EFECTIVO)&& !metPago[i].getMoneda().equals(sMonedaBase)&& !paso1) {				
								
								if (hFac.getMoneda().equals(sMonedaBase)) {
									
									if(dCambio2 > 0){//hay cambio domestico
										dNewMonto1 = divisas.roundDouble4(metPago[i].getEquivalente() - dCambio2);
										dMontoFicha = divisas.roundDouble4(dCambio2/bdTasa.doubleValue());
										
										
										metPago[i].setMonto(divisas.roundDouble4(dNewMonto1/ metPago[i].getTasa().doubleValue()));
										metPago[i].setEquivalente(dNewMonto1);
										
										if(dMontoFicha > 0){
											// Grabar ficha de CV
											MetodosPago metPagoFicha = new MetodosPago( MetodosPagoCtrl.EFECTIVO,metPago[i].getMoneda(), dMontoFicha,metPago[i].getTasa(), 
																						dCambio2, "", "","", "","0",f55ca01.getId().getCaid());
											lstPagoFicha = new ArrayList();
											lstPagoFicha.add(metPagoFicha);
											insertado = insertarFichaCV(s, tx, cn,f55ca01, hFac, vautoriz[0],dMontoFicha, iNumrec, sCajero,lstPagoFicha);
											bHayFicha = true;
										}
									}
									if(dCambio1 > 0){
										dNewMonto1 = metPago[i].getMonto() - dCambio1;								
										metPago[i].setMonto(dNewMonto1);
										dNewMonto1 = dCambio1*metPago[i].getTasa().doubleValue();
										dNewMonto2 = metPago[i].getEquivalente();
										dNewMonto1 = divisas.roundDouble4(dNewMonto2 - dNewMonto1);
										metPago[i].setEquivalente(dNewMonto1);
										
										lstMetodosPago.remove(i);
										lstMetodosPago.add(i, metPago[i]);
										paso1 = true;
									}
									paso1 = true;
								}else{
									if(dCambio2 > 0){ // cambio2 =  cambio cordobas.
										
										dNewMonto1 = metPago[i].getMonto() - divisas.roundDouble4((metPago[i].getMonto() - dTotalAplicar));
//										dMontoFicha = divisas.roundDouble4((dCambio2 / hFac.getTasa().doubleValue()));
										
										dMontoFicha = new BigDecimal(String.valueOf(dCambio2))
													.divide(bdTasa, 2, RoundingMode.HALF_UP )
													.doubleValue();
										
										metPago[i].setMonto(dNewMonto1);
										metPago[i].setEquivalente(dNewMonto1);
										
										if(dMontoFicha > 0){
											MetodosPago metPagoFicha = new MetodosPago( MetodosPagoCtrl.EFECTIVO,metPago[i].getMoneda(), dMontoFicha,hFac.getTasa(), dCambio2, "", "","", "","0",f55ca01.getId().getCaid());
											lstPagoFicha = new ArrayList();
											lstPagoFicha.add(metPagoFicha);
											insertado = insertarFichaCV(s, tx, cn,f55ca01, hFac, vautoriz[0],dMontoFicha, iNumrec, sCajero,lstPagoFicha);
											bHayFicha = true;
										}
									}
									if(dCambio1 > 0 && dCambio2 == 0){ // cambio1 =  cambio dolares.
										dNewMonto1 = metPago[i].getMonto() - dCambio1;								
										metPago[i].setMonto(dNewMonto1);
										metPago[i].setEquivalente(dNewMonto1);
										paso1 = true;
									}
									lstMetodosPago.set(i, metPago[i]);
								}
								
							} else if (metPago[i].getMetodo().equals( MetodosPagoCtrl.EFECTIVO)&& metPago[i].getMoneda().equals(sLblCambio2)&& !paso2) {
								dNewMonto2 = metPago[i].getMonto() - dCambio2;
								metPago[i].setMonto(dNewMonto2);
								metPago[i].setEquivalente(dNewMonto2 * metPago[i].getTasa().doubleValue());
								lstMetodosPago.remove(i);
								lstMetodosPago.add(i, metPago[i]);
								paso2 = true;
							}
						}
					} else {// hay cambio en una moneda unicamente
						sCambio1 = txtCambio.getValue().toString();
						dCambio1 = divisas.formatStringToDouble(sCambio1);
						sLblCambios1 = sLblCambio1.trim().split(" ");
						//
						sLblCambio1 = sLblCambios1[sLblCambios1.length - 1].substring(0, 3);
						//
						for (int i = 0; i < lstMetodosPago.size(); i++) {
							metPago[i] = (MetodosPago) lstMetodosPago.get(i);
							if (metPago[i].getMetodo().equals( MetodosPagoCtrl.EFECTIVO) && metPago[i].getMoneda().equals(sLblCambio1) && !paso1) {
								if (sLblCambio1.equals(sMonedaBase)&& hFac.getMoneda().equals(sMonedaBase)) {// se saca el cambio en cor del pago en cor
									dNewMonto1 = metPago[i].getMonto() - dCambio1;
									metPago[i].setMonto(dNewMonto1);
									metPago[i].setEquivalente(dNewMonto1);
								} else if (sLblCambio1.equals(sMonedaBase) && !hFac.getMoneda().equals(sMonedaBase) && lstMetodosPago.size() > 1) {// se saca el cambio del pago en cor y se convierte a usd con tasa del metodo
									dNewMonto1 = metPago[i].getMonto() - dCambio1;
									metPago[i].setMonto(dNewMonto1);
									metPago[i].setEquivalente(divisas.roundDouble( (dNewMonto1 / metPago[i].getTasa().doubleValue()), 2 ) );
								} else if (sLblCambio1.equals(sMonedaBase) && !hFac.getMoneda().equals(sMonedaBase) && lstMetodosPago.size() == 1) {// si pago toda la fac de usd en cor
									
					
									dNewMonto1 = metPago[i].getMonto() - 
													(metPago[i].getMonto() 
														- dTotalAplicar * hFac.getTasa().doubleValue());
									dNewMonto1 = divisas.roundDouble4(dNewMonto1);
									metPago[i].setMonto(dNewMonto1);
									metPago[i].setEquivalente(divisas.roundDouble4(dNewMonto1/ metPago[i].getTasa().doubleValue()));
								
								} else if (sLblCambio1.equals(sMonedaBase) && hFac.getMoneda().equals(sMonedaBase) && lstMetodosPago.size() == 1) {// el pago en usd cubre toda la venta
									dNewMonto1 = metPago[i].getEquivalente() - dCambio1;
									metPago[i].setMonto(divisas.roundDouble4(dNewMonto1 / metPago[i].getTasa().doubleValue()));
									metPago[i].setEquivalente(dNewMonto1);
								}
								lstMetodosPago.remove(i);
								lstMetodosPago.add(i, metPago[i]);
								paso1 = true;
							} else if (metPago[i].getMetodo().equals( MetodosPagoCtrl.EFECTIVO) && sLblCambio1.equals(sMonedaBase) && !metPago[i].getMoneda().equals(sMonedaBase) && !paso1) {
								if (hFac.getMoneda().equals(sMonedaBase) && lstMetodosPago.size() == 1) {// el pago en usd cubre toda la venta
									dNewMonto1 = metPago[i].getEquivalente() - dCambio1;
									dMontoFicha = metPago[i].getMonto() - divisas.roundDouble4(dNewMonto1/ metPago[i].getTasa().doubleValue());
	
									metPago[i].setMonto(divisas.roundDouble4(dNewMonto1/ metPago[i].getTasa().doubleValue()));
									metPago[i].setEquivalente(dNewMonto1);
									if(dMontoFicha > 0){
										// Grabar ficha de CV
										MetodosPago metPagoFicha = new MetodosPago( MetodosPagoCtrl.EFECTIVO,metPago[i].getMoneda(), dMontoFicha,metPago[i].getTasa(), dCambio1, "", "","", "","0",f55ca01.getId().getCaid());
										lstPagoFicha = new ArrayList();
										lstPagoFicha.add(metPagoFicha);
										insertado = insertarFichaCV(s, tx, cn,f55ca01, hFac, vautoriz[0],dMontoFicha, iNumrec, sCajero,lstPagoFicha);
										bHayFicha = true;
									}
								}
							}
						}
					}

					if (insertado) {


						if(insertado){
							//----- verificar traslado de factura.
							TrasladoCtrl tcCtrl = new TrasladoCtrl();
							Trasladofac tf = null;
							for (Iterator iter = lstFacturasSelected.iterator(); iter.hasNext();) {
								Hfactura hFactura = (Hfactura) iter.next();
								tf = tcCtrl.buscarTrasladofac(s, hFactura, f55ca01.getId().getCaid(), 0, "");
								if(tf!=null){
									insertado = tcCtrl.actualizarEstadoTraslado(s, tf, "P");
									if(!insertado){
										strMensajeValidacion = tcCtrl.getErrorTf().toString().split("@")[1];
										lblMensajeValidacion.setValue(strMensajeValidacion);
										break;
									}
								}
							}
						}
						//&& =============== Guardar los asientos de diario por sobrantes ========== &&//
						if(insertado && m.get("sco_SobrantePago") != null){
							
							MetodosPago mpago = null;
							if(m.get("sco_MpagoSobrante")==null){
								strMensajeValidacion = "Error de sistema al obtener los datos del sobrante de pago para el recibo ";
								lblMensajeValidacion.setValue(strMensajeValidacion);
								insertado = false;
							}else{
								
								String sCodunineg = ((Hfactura)lstFacturasSelected.get(0)).getCodunineg();
								String sCodcomp  = ((Hfactura)lstFacturasSelected.get(0)).getCodcomp();
								double dSobrante = divisas.formatStringToDouble(m.get("sco_SobrantePago").toString());
								mpago = (MetodosPago)m.get("sco_MpagoSobrante");
								
								/*insertado = registarAsientosxSobrantes(s, tx, cn, mpago,  vautoriz[0], f55ca01,
																		iNumrec, sCodcomp,sCodunineg,
																		dSobrante,sMonedaBase,lstFacturasSelected.get(0));*/
								if(!insertado){
									strMensajeValidacion = (m.get("sMsgErrorSobrante") == null)? 
														"No se ha podido registrar el sobrante de pago en JDE":
														String.valueOf(m.get("sMsgErrorSobrante"));
									lblMensajeValidacion.setValue(strMensajeValidacion);
								}
							}
						}
						if (insertado) {
							tx.commit();
							cn.commit();
							if (bHayFicha) {
								// actualizar el recibo con el numero de ficha
	 							recCtrl.actualizarNoFicha(cn, iNumrec, Integer.parseInt(m.get("iNoFicha").toString()),
								f55ca01.getId().getCaid(), hFac.getCodcomp(), f55ca01.getId().getCaco(), valoresJdeIns[0]);
								cn.commit();
							}
							
							//realizar transaccion de tarjeta			
							if(  f55ca01.getId().getCasktpos() == 'Y' ){
								
								int iNoficha = (m.get("iNoFicha") == null)?
										0 : Integer.parseInt(
											String.valueOf(m.get("iNoFicha")));
								
								lstMetodosPago = (List) m.get("lstPagos");										
								List lstDatos = (List)m.get("lstDatosTrack_Con");
								insertado = aplicarPagoSocketPos(lstMetodosPago,lstDatos,f55ca01.getId().getCaid(),f55ca01.getId().getCaco(),hFac.getCodcomp(),iNumrec);
								
								if(insertado){
									insertado = recCtrl.actualizarReferenciasRecibo(
											iNumrec, iCajaId, sCodComp,
											f55ca01.getId().getCaco(),
											valoresJdeIns[0], lstMetodosPago);
									if(insertado){
										imprimirVoucher(lstMetodosPago,sCodComp,"V", f14);
									}else{
										anularPagosSP(lstMetodosPago);
										anularRecibo(iNumrec,iCajaId,f55ca01.getId().getCaco(),
												sCodComp, vautoriz[0].getId().getCoduser(),
												iNoficha, hFac.getCodcli());
										
										if(recCtrl.getError() != null)
											sMensaje = recCtrl.getError().toString().split("@")[1];
										else
											sMensaje = "Error de actualización de referencias de pago en recibo";
										
										lblMensajeValidacion.setValue(sMensaje);
										dwImprime.setWindowState("hidden");
										dwRecibo.setWindowState("hidden");
										dwProcesa.setWindowState("normal");
										dwProcesa.setStyle("width:370px;height:160px");
										m.remove("lstDatosTrack_Con");
									}
								}
								else {
									dwImprime.setWindowState("hidden");
									dwRecibo.setWindowState("hidden");
									dwProcesa.setWindowState("normal");
									dwProcesa.setStyle("width:370px;height:160px");
									m.remove("lstDatosTrack_Con");
									
									anularPagosSP(lstMetodosPago);
									anularRecibo(iNumrec,iCajaId,f55ca01.getId().getCaco(),
												sCodComp, vautoriz[0].getId().getCoduser(),
												iNoficha, hFac.getCodcli());
								}
							}
							
							if (imprimir && insertado) {
								
								//&& ======  imprime el recibo de contado
								BigDecimal bdNumrec = new BigDecimal(iNumrec);
								getP55recibo().setIDCAJA(new BigDecimal(iCajaId));
								getP55recibo().setNORECIBO(bdNumrec);
								getP55recibo().setIDEMPRESA(sCodComp.trim());
								getP55recibo().setIDSUCURSAL(f55ca01.getId().getCaco());
								getP55recibo().setTIPORECIBO(valoresJdeIns[0]);
								getP55recibo().setRESULTADO("");
								getP55recibo().setCOMANDO("");
								getP55recibo().invoke();
								getP55recibo().getRESULTADO();
							}
						} else {
							dwImprime.setWindowState("hidden");
							dwRecibo.setWindowState("hidden");
							dwProcesa.setWindowState("normal");
							dwProcesa.setStyle("width:370px;height:160px");
							tx.rollback();
							cn.rollback();
						}
					} else {
						dwImprime.setWindowState("hidden");
						dwRecibo.setWindowState("hidden");					
						dwProcesa.setWindowState("normal");
						dwProcesa.setStyle("width:370px;height:160px");
						tx.rollback();
						cn.rollback();
					}
				} else {
					dwImprime.setWindowState("hidden");
					dwRecibo.setWindowState("hidden");
					dwProcesa.setWindowState("normal");
					dwProcesa.setStyle("width:370px;height:160px");
					tx.rollback();
					cn.rollback();
				}
				dwImprime.setWindowState("hidden");
				dwRecibo.setWindowState("hidden");
				listarFacturasContadoDelDia(); // -----------------
				String sMensaje2 = "";
				List lstHfacturasContado = null;
				if (m.get("lstHfacturasContado") != null) {
					lstHfacturasContado = (List) m.get("lstHfacturasContado");
				}
				if (lstHfacturasContado == null || lstHfacturasContado.isEmpty()) {
					sMensaje2 = "No se encontraron resultados";
					getSMensaje();
					m.put("mostrar", "m");
				} else {
					if (m.get("mostrar") != null) {
						m.remove("mostrar");
					}
				}
				sMensaje = sMensaje2;
				txtMensaje.setValue(sMensaje2);
				m.put("sMensaje", sMensaje2);
				cmbFiltroMonedas.dataBind();
				cmbFiltroFacturas.dataBind();
				gvHfacturasContado.dataBind();

			}else {
				dwImprime.setWindowState("hidden");
				dwRecibo.setWindowState("hidden");
				dwProcesa.setWindowState("normal");
				dwProcesa.setStyle("width:370px;height:160px");
				tx.rollback();
				cn.rollback();
			}

		} catch (Exception ex) {
			try {
				dwImprime.setWindowState("hidden");
				dwRecibo.setWindowState("hidden");
				strMensajeValidacion = "Error: No se pudo realizar la operación: "+ ex;
				lblMensajeValidacion.setValue(strMensajeValidacion);
				dwProcesa.setWindowState("normal");
				dwProcesa.setStyle("width:370px;height:160px");
				tx.rollback();
				cn.rollback();
				anularPagosSP(lstMetodosPago);

			} catch (Exception ex2) {
				ex2.printStackTrace();
			}
			ex.printStackTrace();
		}finally{
			
			
			try{
				m.remove("sco_SbrDfr");
				m.remove("sco_SobrantePago");
				m.remove("sco_MpagoSobrante");
				
				m.remove("lstSolicitud");
				m.remove("facturasSelected");
				m.remove("lstPagos");
				m.remove("lstDatosTrack_Con");
				m.remove("lstPagos_TC");
				m.remove("con_processRecibo");
				m.remove("fdc_ObjTrasladoFacTrasladoPOS");
				
				dwCargando.setWindowState("hidden");
				
				cn.close();
				
			}catch(Exception ex4){
				ex4.printStackTrace();
			}finally{
				m.remove("metodopagoborrar");
			}
		}
		
	}

	/** ************************************************************************************************************************** */
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/** **************GETTERS Y SETTERS**************** */
	public UIInput getCmbBusqueda() {
		return cmbBusqueda;
	}

	public void setCmbBusqueda(UIInput cmbBusqueda) {
		this.cmbBusqueda = cmbBusqueda;
	}

	public List getLstBusqueda() {
		if (lstBusqueda == null) {
			lstBusqueda = new ArrayList();
			lstBusqueda.add(new SelectItem("1", "Nombre Cliente","Búsqueda por nombre de cliente"));
			lstBusqueda.add(new SelectItem("2", "Código Cliente","Búsqueda por código de cliente"));
			lstBusqueda.add(new SelectItem("3", "No. Factura","Búsqueda por número de factura"));
		}
		return lstBusqueda;
	}

	public void setLstBusqueda(List lstBusqueda) {
		this.lstBusqueda = lstBusqueda;
	}

	public HtmlGridView getGvHfacturasContado() {
		return gvHfacturasContado;
	}

	public void setGvHfacturasContado(HtmlGridView gvHfacturasContado) {
		this.gvHfacturasContado = gvHfacturasContado;
	}

	public List getLstHfacturasContado() {
		try {
			lstHfacturasContado = new ArrayList();
			lstHfacturasContado = (List) m.get("lstHfacturasContado");
 

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return lstHfacturasContado;
	}

	public void setLstHfacturasContado(List lstHfacturasContado) {
		this.lstHfacturasContado = lstHfacturasContado;
	}

	public UIInput getTxtParametro() {
		return txtParametro;
	}

	public void setTxtParametro(UIInput txtParametro) {
		this.txtParametro = txtParametro;
	}

	public HtmlDialogWindow getDgwDetalleContado() {
		return dgwDetalleContado;
	}

	public void setDgwDetalleContado(HtmlDialogWindow dgwDetalleContado) {
		this.dgwDetalleContado = dgwDetalleContado;
	}

	public List getLstDfacturasContado() {
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		Object o = m.get("bapp2");
		if (o == null) {
			lstDfacturasContado = new ArrayList();
			m.put("lstDfacturasContado", lstDfacturasContado);
			m.put("bapp2", "y");
		} else {
			lstDfacturasContado = (List) m.get("lstDfacturasContado");
		}
		return lstDfacturasContado;
	}

	public void setLstDfacturasContado(List lstDfacturasContado) {
		this.lstDfacturasContado = lstDfacturasContado;
	}

	public GridView getGvDfacturasContado() {
		return gvDfacturasContado;
	}

	public void setGvDfacturasContado(GridView gvDfacturasContado) {
		this.gvDfacturasContado = gvDfacturasContado;
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

	public String getTxtFechaFactura() {
		return txtFechaFactura;
	}

	public void setTxtFechaFactura(String txtFechaFactura) {
		this.txtFechaFactura = txtFechaFactura;
	}

	public String getTxtNofactura() {
		return txtNofactura;
	}

	public void setTxtNofactura(String txtNofactura) {
		this.txtNofactura = txtNofactura;
	}

	public String getTxtUnineg() {
		return txtUnineg;
	}

	public void setTxtUnineg(String txtUnineg) {
		this.txtUnineg = txtUnineg;
	}

	public double getTxtIva() {
		return txtIva;
	}

	public void setTxtIva(double txtIva) {
		this.txtIva = txtIva;
	}

	public double getTxtSubtotal() {
		return txtSubtotal;
	}

	public void setTxtSubtotal(double txtSubtotal) {
		this.txtSubtotal = txtSubtotal;
	}

	public String getTxtTotal() {
		return txtTotal;
	}

	public void setTxtTotal(String txtTotal) {
		this.txtTotal = txtTotal;
	}

	public String getLblTasaDetalle() {
		return lblTasaDetalle;
	}

	public void setLblTasaDetalle(String lblTasaDetalle) {
		this.lblTasaDetalle = lblTasaDetalle;
	}

	public HtmlDropDownList getCmbMonedaDetalle() {
		return cmbMonedaDetalle;
	}

	public void setCmbMonedaDetalle(HtmlDropDownList cmbMonedaDetalle) {
		this.cmbMonedaDetalle = cmbMonedaDetalle;
	}

	public HtmlDropDownList getCmbMonedaDetalleReciboContado() {
		return cmbMonedaDetalleReciboContado;
	}

	public void setCmbMonedaDetalleReciboContado(HtmlDropDownList cmbMonedaDetalleReciboContado) {
		this.cmbMonedaDetalleReciboContado = cmbMonedaDetalleReciboContado;
	}

	public List getLstMonedasDetalle() {
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		Object o = m.get("bLstMonDetalle");
		if (o == null) {
			lstMonedasDetalle = new ArrayList();
			m.put("lstMonedasDetalle", lstMonedasDetalle);
			m.put("bLstMonDetalle", "y");
		} else {
			lstMonedasDetalle = (List) m.get("lstMonedasDetalle");
		}
		return lstMonedasDetalle;
	}

	public void setLstMonedasDetalle(List lstMonedasDetalle) {
		this.lstMonedasDetalle = lstMonedasDetalle;
	}

	public List getLstMonedasDetalleReciboContado() {
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		Object o = m.get("bLstMonDetalleReciboCon");
		if (o == null) {
			lstMonedasDetalleReciboContado = new ArrayList();
			m.put("lstMonedasDetalleReciboContado",lstMonedasDetalleReciboContado);
			m.put("bLstMonDetalleReciboCon", "y");
		} else {
			lstMonedasDetalleReciboContado = (List) m.get("lstMonedasDetalleReciboContado");
		}
		return lstMonedasDetalleReciboContado;
	}

	public void setLstMonedasDetalleReciboContado(
			List lstMonedasDetalleReciboContado) {
		this.lstMonedasDetalleReciboContado = lstMonedasDetalleReciboContado;
	}

	public DialogWindow getDgwDetalleEnReciboContado() {
		return dgwDetalleEnReciboContado;
	}

	public void setDgwDetalleEnReciboContado(
			DialogWindow dgwDetalleEnReciboContado) {
		this.dgwDetalleEnReciboContado = dgwDetalleEnReciboContado;
	}

	public GridView getGvDfacturasContadoRecibo() {
		return gvDfacturasContadoRecibo;
	}

	public void setGvDfacturasContadoRecibo(GridView gvDfacturasContadoRecibo) {
		this.gvDfacturasContadoRecibo = gvDfacturasContadoRecibo;
	}

	public DialogWindow getDgwMensajeDetalleContado() {
		return dgwMensajeDetalleContado;
	}

	public void setDgwMensajeDetalleContado(
			DialogWindow dgwMensajeDetalleContado) {
		this.dgwMensajeDetalleContado = dgwMensajeDetalleContado;
	}

	public HtmlOutputText getTxtMensaje() {
		return txtMensaje;
	}

	public void setTxtMensaje(HtmlOutputText txtMensaje) {
		this.txtMensaje = txtMensaje;
	}

	public String getSMensaje() {
		if (m.get("sMensaje") != null) {
			sMensaje = m.get("sMensaje").toString();
		}
		return sMensaje;
	}

	public void setSMensaje(String sMensaje) {
		sMensaje = sMensaje;
	}

	public HtmlDropDownList getCmbFiltroFacturas() {
		return cmbFiltroFacturas;
	}

	public void setCmbFiltroFacturas(HtmlDropDownList cmbFiltroFacturas) {
		this.cmbFiltroFacturas = cmbFiltroFacturas;
	}

	public List getLstFiltroFacturas() {
		if (lstFiltroFacturas == null) {
			lstFiltroFacturas = new ArrayList();
			lstFiltroFacturas.add(new SelectItem("01", "Facturas del dia"));
			lstFiltroFacturas.add(new SelectItem("04","Facturas del dia Anuladas"));
//			lstFiltroFacturas.add(new SelectItem("02","Facturas de dias anteriores"));
			lstFiltroFacturas.add(new SelectItem("03", "Todas"));
		}
		return lstFiltroFacturas;
	}

	public void setLstFiltroFacturas(List lstFiltroFacturas) {
		this.lstFiltroFacturas = lstFiltroFacturas;
	}

	public HtmlOutputText getLblMonedaDomesticaCon() {
		return lblMonedaDomesticaCon;
	}

	public void setLblMonedaDomesticaCon(HtmlOutputText lblMonedaDomesticaCon) {
		this.lblMonedaDomesticaCon = lblMonedaDomesticaCon;
	}

	public HtmlOutputText getLblMontoAplicar() {
		return lblMontoAplicar;
	}

	public void setLblMontoAplicar(HtmlOutputText lblMontoAplicar) {
		this.lblMontoAplicar = lblMontoAplicar;
	}

	public String getTxtTasaDetalle() {
		return txtTasaDetalle;
	}

	public void setTxtTasaDetalle(String txtTasaDetalle) {
		this.txtTasaDetalle = txtTasaDetalle;
	}

	public GridView getGvfacturasSelec() {
		return gvfacturasSelec;
	}

	public void setGvfacturasSelec(GridView gvfacturasSelec) {
		this.gvfacturasSelec = gvfacturasSelec;
	}

	public List getSelectedFacs() {
		selectedFacs = (List) m.get("facturasSelected");
		return selectedFacs;
	}

	public void setSelectedFacs(List selectedFacs) {
		this.selectedFacs = selectedFacs;
	}

	public HtmlDropDownList getCmbFiltroMonedas() {
		return cmbFiltroMonedas;
	}

	public void setCmbFiltroMonedas(HtmlDropDownList cmbFiltroMonedas) {
		this.cmbFiltroMonedas = cmbFiltroMonedas;
	}

	/** **************************************** */
	public List getLstFiltroMonedas() {
		try {
			if (m.get("fmonCon") == null) {
				// leer companias configuradas en caja
				List lstCajas = (List) m.get("lstCajas");
				CompaniaCtrl compCtrl = new CompaniaCtrl();
				F55ca014[] f55ca014 = null;
				f55ca014 = compCtrl.obtenerCompaniasxCaja(((Vf55ca01) lstCajas.get(0)).getId().getCaid());
				// leer monedas de companias configuradas
				MonedaCtrl monCtrl = new MonedaCtrl();
				String[] sMonedas = monCtrl.obtenerMonedasxCaja_Companias(((Vf55ca01) lstCajas.get(0)).getId().getCaid(),f55ca014);
				lstFiltroMonedas = new ArrayList();
				lstFiltroMonedas.add(new SelectItem("01", "Todas"));
				for (int i = 0; i < sMonedas.length; i++) {
					lstFiltroMonedas.add(new SelectItem(sMonedas[i],
							sMonedas[i]));
				}
				m.put("fmonCon", "fm");
				m.put("lstFiltroMonedas", lstFiltroMonedas);
			} else {
				lstFiltroMonedas = (List) m.get("lstFiltroMonedas");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return lstFiltroMonedas;
	}

	/** **************************************** */
	public void setLstFiltroMonedas(List lstFiltroMonedas) {
		this.lstFiltroMonedas = lstFiltroMonedas;
	}

	public DialogWindow getDwValidacionFactura() {
		return dwValidacionFactura;
	}

	public void setDwValidacionFactura(DialogWindow dwValidacionFactura) {
		this.dwValidacionFactura = dwValidacionFactura;
	}

	public HtmlOutputText getLblValidaFactura() {
		return lblValidaFactura;
	}

	public void setLblValidaFactura(HtmlOutputText lblValidaFactura) {
		this.lblValidaFactura = lblValidaFactura;
	}

	public DialogWindow getDwRecibo() {
		return dwRecibo;
	}

	public void setDwRecibo(DialogWindow dwRecibo) {
		this.dwRecibo = dwRecibo;
	}

	public String getFechaRecibo() {

		if (fechaRecibo == null) {
			Date fecharecibo = new Date();
			Format formatter = new SimpleDateFormat("dd/MM/yyyy");
			fechaRecibo = formatter.format(fecharecibo);
		}

		return fechaRecibo;
	}

	public void setFechaRecibo(String fechaRecibo) {
		this.fechaRecibo = fechaRecibo;
	}

	public HtmlDateChooser getTxtFecham() {
		return txtFecham;
	}

	public void setTxtFecham(HtmlDateChooser txtFecham) {
		this.txtFecham = txtFecham;
	}

	public HtmlOutputText getLblNumeroRecibo() {
		return lblNumeroRecibo;
	}

	public void setLblNumeroRecibo(HtmlOutputText lblNumeroRecibo) {
		this.lblNumeroRecibo = lblNumeroRecibo;
	}

	public String getLblNumrec() {
		return lblNumrec;
	}

	public void setLblNumrec(String lblNumrec) {
		this.lblNumrec = lblNumrec;
	}

	public UIOutput getLblNumrec2() {
		return lblNumrec2;
	}

	public void setLblNumrec2(UIOutput lblNumrec2) {
		this.lblNumrec2 = lblNumrec2;
	}

	public HtmlOutputText getLblNumRecm() {
		return lblNumRecm;
	}

	public void setLblNumRecm(HtmlOutputText lblNumRecm) {
		this.lblNumRecm = lblNumRecm;
	}

	public HtmlInputText getTxtNumRec() {
		return txtNumRec;
	}

	public void setTxtNumRec(HtmlInputText txtNumRec) {
		this.txtNumRec = txtNumRec;
	}

	public HtmlOutputText getLblCodigoCliente() {
		return lblCodigoCliente;
	}

	public void setLblCodigoCliente(HtmlOutputText lblCodigoCliente) {
		this.lblCodigoCliente = lblCodigoCliente;
	}

	public HtmlOutputText getLblNombreCliente() {
		return lblNombreCliente;
	}

	public void setLblNombreCliente(HtmlOutputText lblNombreCliente) {
		this.lblNombreCliente = lblNombreCliente;
	}

	public HtmlDropDownList getCmbTiporecibo() {
		return cmbTiporecibo;
	}

	public void setCmbTiporecibo(HtmlDropDownList cmbTiporecibo) {
		this.cmbTiporecibo = cmbTiporecibo;
	}

	public List getLstTiporecibo() {

		if (lstTiporecibo == null) {
			lstTiporecibo = new ArrayList();
			lstTiporecibo.add(new SelectItem("AUTOMATICO", "AUTOMATICO"));
			lstTiporecibo.add(new SelectItem("MANUAL", "MANUAL"));
		}
		m.put("mTipoRecibo", "AUTOMATICO");
		return lstTiporecibo;
	}

	public void setLstTiporecibo(List lstTiporecibo) {
		this.lstTiporecibo = lstTiporecibo;
	}

/***********OBTENER TASA DE CAMBIO PARALELA***************************************************************/
	public HtmlOutputText getLblTasaCambio() {
		TasaCambioCtrl tcCtrl = new TasaCambioCtrl();
		Tpararela[] tpcambio = null;
		try {
			if (m.get("tasaCon") == null) {
				String Tasa = "1.0";
				// obtener tasas de cambio paralela
				tpcambio = tcCtrl.obtenerTasaCambioParalela(new Date());
				m.put("tpcambio", tpcambio);
				m.put("lblTasaCambio", Tasa);
				m.put("tasaCon", "t");
			} else {
				
				lblTasaCambio = (lblTasaCambio == null)?
									new HtmlOutputText():lblTasaCambio;
				
				if(lblTasaCambio != null){
					lblTasaCambio.setValue((String) m.get("lblTasaCambio"));
					tpcambio = (Tpararela[]) m.get("tpcambio");
					if (tpcambio == null) {
						lblMensajeValidacion.setValue("Tasa de cambio paralela no esta configurada");
						dwProcesa.setWindowState("normal");
						dwProcesa.setStyle("width:370px;height:160px");
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return lblTasaCambio;
	}

	/** ****************************************************************************************** */
	public void setLblTasaCambio(HtmlOutputText lblTasaCambio) {
		this.lblTasaCambio = lblTasaCambio;
	}

	public HtmlOutputText getLblMontoRecibido() {
		return lblMontoRecibido;
	}

	public void setLblMontoRecibido(HtmlOutputText lblMontoRecibido) {
		this.lblMontoRecibido = lblMontoRecibido;
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

/****************************Lista Metodos de Pago por Caja***********************************************************/
	public List getLstMetodosPago() {
		try {
			
			if (m.get("lstMetodosPago") == null) {
				lstMetodosPago = new ArrayList();
			
			} else {
				lstMetodosPago = (List) m.get("lstMetodosPago");
			}
		} catch (Exception ex) {
			ex.printStackTrace();

		}
		return lstMetodosPago;
	}

	/** ******************************************************* */
	public HtmlDropDownList getCmbMetodosPago() {
		return cmbMetodosPago;
	}

	public void setCmbMetodosPago(HtmlDropDownList cmbMetodosPago) {
		this.cmbMetodosPago = cmbMetodosPago;
	}

	public void setLstMetodosPago(List lstMetodosPago) {
		this.lstMetodosPago = lstMetodosPago;
	}

/***************GET LISTA DE MONEDAS*****************************************************/
	public List getLstMoneda() {
		try {
			if (m.get("lstMoneda") == null) {
				lstMoneda = new ArrayList();
			} else {
				lstMoneda = (List) m.get("lstMoneda");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return lstMoneda;
	}

	/** ************************************************************************************ */
	public HtmlDropDownList getCmbMoneda() {
		return cmbMoneda;
	}

	public void setCmbMoneda(HtmlDropDownList cmbMoneda) {
		this.cmbMoneda = cmbMoneda;
	}

	public void setLstMoneda(List lstMoneda) {
		this.lstMoneda = lstMoneda;
	}

	public HtmlInputText getTxtMonto() {
		return txtMonto;
	}

	public void setTxtMonto(HtmlInputText txtMonto) {
		this.txtMonto = txtMonto;
	}

/*******************OBTENER AFILIADOS PARA LA CAJA**********************************************************/
	public List getLstAfiliado() {
		try {
			if (m.get("lstAfiliado") == null) {
				lstAfiliado = new ArrayList();
			} else {
				lstAfiliado = (List) m.get("lstAfiliado");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return lstAfiliado;
	}

	/** ************************************************************************************ */
	public HtmlDropDownList getDdlAfiliado() {
		return ddlAfiliado;
	}

	public void setDdlAfiliado(HtmlDropDownList ddlAfiliado) {
		this.ddlAfiliado = ddlAfiliado;
	}

	public UIOutput getLblAfiliado() {
		return lblAfiliado;
	}

	public void setLblAfiliado(UIOutput lblAfiliado) {
		this.lblAfiliado = lblAfiliado;
	}

	public void setLstAfiliado(List lstAfiliado) {
		this.lstAfiliado = lstAfiliado;
	}

	public UIOutput getLblReferencia1() {
		return lblReferencia1;
	}

	public void setLblReferencia1(UIOutput lblReferencia1) {
		this.lblReferencia1 = lblReferencia1;
	}

	public UIOutput getLblReferencia2() {
		return lblReferencia2;
	}

	public void setLblReferencia2(UIOutput lblReferencia2) {
		this.lblReferencia2 = lblReferencia2;
	}

	public UIOutput getLblReferencia3() {
		return lblReferencia3;
	}

	public void setLblReferencia3(UIOutput lblReferencia3) {
		this.lblReferencia3 = lblReferencia3;
	}

	public HtmlInputText getTxtReferencia1() {
		return txtReferencia1;
	}

	public void setTxtReferencia1(HtmlInputText txtReferencia1) {
		this.txtReferencia1 = txtReferencia1;
	}

	public HtmlInputText getTxtReferencia2() {
		return txtReferencia2;
	}

	public void setTxtReferencia2(HtmlInputText txtReferencia2) {
		this.txtReferencia2 = txtReferencia2;
	}

	public HtmlInputText getTxtReferencia3() {
		return txtReferencia3;
	}

	public void setTxtReferencia3(HtmlInputText txtReferencia3) {
		this.txtReferencia3 = txtReferencia3;
	}

/*******************OBTENER LAS LISTAS DE BANCOS********************************************************************/
	public List getLstBanco() {
		BancoCtrl bancoCtrl = new BancoCtrl();
		F55ca022[] banco = null;
		try {
			if (m.get("lstBanco") == null) {
				lstBanco = new ArrayList();
				banco = bancoCtrl.obtenerBancos();
	        	for (F55ca022 f22 : banco) {
					SelectItem si = new SelectItem();
					si.setValue(f22.getId().getConciliar()+"@"+f22.getId().getBanco());
					si.setLabel(f22.getId().getBanco());
					si.setDescription(String.valueOf(f22.getId().getCodb()));
					lstBanco.add(si);
				}
				m.put("lstBanco", lstBanco);
				m.put("f55ca022", banco);
			} else {
				lstBanco = (List) m.get("lstBanco");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return lstBanco;
	}

	/** ************************************************************************************ */
	public HtmlDropDownList getDdlBanco() {
		return ddlBanco;
	}

	public void setDdlBanco(HtmlDropDownList ddlBanco) {
		this.ddlBanco = ddlBanco;
	}

	public UIOutput getLblBanco() {
		return lblBanco;
	}

	public void setLblBanco(UIOutput lblBanco) {
		this.lblBanco = lblBanco;
	}

	public void setLstBanco(List lstBanco) {
		this.lstBanco = lstBanco;
	}

	public DialogWindow getDwImprime() {
		return dwImprime;
	}

	public void setDwImprime(DialogWindow dwImprime) {
		this.dwImprime = dwImprime;
	}

	public DialogWindow getDwProcesa() {
		return dwProcesa;
	}

	public void setDwProcesa(DialogWindow dwProcesa) {
		this.dwProcesa = dwProcesa;
	}

	public UIOutput getLblMensajeValidacion() {
		return lblMensajeValidacion;
	}

	public void setLblMensajeValidacion(UIOutput lblMensajeValidacion) {
		this.lblMensajeValidacion = lblMensajeValidacion;
	}

	public HtmlDropDownList getCmbAutoriza() {
		return cmbAutoriza;
	}

	public void setCmbAutoriza(HtmlDropDownList cmbAutoriza) {
		this.cmbAutoriza = cmbAutoriza;
	}

	public DialogWindow getDwSolicitud() {
		return dwSolicitud;
	}

	public void setDwSolicitud(DialogWindow dwSolicitud) {
		this.dwSolicitud = dwSolicitud;
	}

	public UIOutput getLblMensajeAutorizacion() {
		return lblMensajeAutorizacion;
	}

	public void setLblMensajeAutorizacion(UIOutput lblMensajeAutorizacion) {
		this.lblMensajeAutorizacion = lblMensajeAutorizacion;
	}

	public HtmlDateChooser getTxtFecha() {
		return txtFecha;
	}

	public void setTxtFecha(HtmlDateChooser txtFecha) {
		this.txtFecha = txtFecha;
	}

	public HtmlInputTextarea getTxtObs() {
		return txtObs;
	}

	public void setTxtObs(HtmlInputTextarea txtObs) {
		this.txtObs = txtObs;
	}

	public HtmlInputText getTxtReferencia() {
		return txtReferencia;
	}

	public void setTxtReferencia(HtmlInputText txtReferencia) {
		this.txtReferencia = txtReferencia;
	}

	
	public List getLstAutoriza() {
		try{
			
			if(CodeUtil.getFromSessionMap("lstAutoriza") != null){
				 return lstAutoriza = (List)m.get("lstAutoriza");
			}
		 
			lstAutoriza = new ArrayList<SelectItem>();
			Vf55ca01 f = (Vf55ca01)( (List)CodeUtil.getFromSessionMap("lstCajas")).get(0);
			
			lstAutoriza.add( new SelectItem( f.getId().getCaan8mail(), f.getId().getCaan8nom() , "Supervisor" ) ) ;
			lstAutoriza.add( new SelectItem( f.getId().getCaautimail(), f.getId().getCaautinom() , "Autorizador titular" ) ) ;
			lstAutoriza.add( new SelectItem( f.getId().getCaausumail(), f.getId().getCaausunom() , "Autorizador suplente" ) ) ;
			lstAutoriza.add( new SelectItem( f.getId().getCacontmail(), f.getId().getCacontnom() , "Contador" ) ) ;
			
			CodeUtil.putInSessionMap( "lstAutoriza", lstAutoriza);
			CodeUtil.putInSessionMap("lstAutorizadores", "");
			 
			}catch(Exception ex){
				ex.printStackTrace(); 
			}
			return lstAutoriza;
		}	
	

	public void setLstAutoriza(List lstAutoriza) {
		this.lstAutoriza = lstAutoriza;
	}

	/** ************************************ */
	public List getSelectedMet() {
		Object o = m.get("metCon");
		if (o == null) {
			selectedMet = new ArrayList();
			m.put("metCon", "y");
		} else {
			selectedMet = (ArrayList<MetodosPago>) m.get("lstPagos");
		}
		return selectedMet;
	}

	/** ****************************************** */
	public HtmlGridView getMetodosGrid() {
		return metodosGrid;
	}

	public void setMetodosGrid(HtmlGridView metodosGrid) {
		this.metodosGrid = metodosGrid;
	}

	public void setSelectedMet(ArrayList<MetodosPago> selectedMet) {
		this.selectedMet = selectedMet;
	}

	public HtmlOutputText getLblTasaJDE() {
		TasaCambioCtrl tcCtrl = new TasaCambioCtrl();
		Tcambio[] tcambio = null;
		try {
			if (m.get("tasaJDECon") == null) {
				String Tasa = "1.0";
				// obtener tasas de cambio paralela
				tcambio = tcCtrl.obtenerTasaCambioJDEdelDia();
				m.put("tcambio", tcambio);
				m.put("lblTasaJDE", Tasa);
				m.put("tasaJDECon", "t");
			} else {
				lblTasaJDE = lblTasaJDE==null? new HtmlOutputText():lblTasaJDE;
				lblTasaJDE.setValue((String) m.get("lblTasaJDE"));
				tcambio = (Tcambio[]) m.get("tcambio");
				if (tcambio == null) {
					lblMensajeValidacion.setValue("Tasa de cambio JDE no esta configurada");
					dwProcesa.setWindowState("normal");
					dwProcesa.setStyle("width:370px;height:160px");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return lblTasaJDE;
	}

	public void setLblTasaJDE(HtmlOutputText lblTasaJDE) {
		this.lblTasaJDE = lblTasaJDE;
	}

	public HtmlOutputText getLblCambio() {
		return lblCambio;
	}

	public void setLblCambio(HtmlOutputText lblCambio) {
		this.lblCambio = lblCambio;
	}

	public HtmlOutputText getTxtCambio() {
		return txtCambio;
	}

	public void setTxtCambio(HtmlOutputText txtCambio) {
		this.txtCambio = txtCambio;
	}

	public DialogWindow getDwCancelar() {
		return dwCancelar;
	}

	public void setDwCancelar(DialogWindow dwCancelar) {
		this.dwCancelar = dwCancelar;
	}

	public HtmlOutputText getLblCambioDomestico() {
		return lblCambioDomestico;
	}

	public void setLblCambioDomestico(HtmlOutputText lblCambioDomestico) {
		this.lblCambioDomestico = lblCambioDomestico;
	}

	public HtmlOutputText getTxtCambioDomestico() {
		return txtCambioDomestico;
	}

	public void setTxtCambioDomestico(HtmlOutputText txtCambioDomestico) {
		this.txtCambioDomestico = txtCambioDomestico;
	}

	public HtmlInputText getTxtCambioForaneo() {
		return txtCambioForaneo;
	}

	public void setTxtCambioForaneo(HtmlInputText txtCambioForaneo) {
		this.txtCambioForaneo = txtCambioForaneo;
	}

	public HtmlLink getLnkCambio() {
		return lnkCambio;
	}

	public void setLnkCambio(HtmlLink lnkCambio) {
		this.lnkCambio = lnkCambio;
	}

	public HtmlInputTextarea getTxtConcepto() {
		return txtConcepto;
	}

	public void setTxtConcepto(HtmlInputTextarea txtConcepto) {
		this.txtConcepto = txtConcepto;
	}

	public HtmlCheckBox getChkImprimir() {
		return chkImprimir;
	}

	public void setChkImprimir(HtmlCheckBox chkImprimir) {
		this.chkImprimir = chkImprimir;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public HtmlOutputText getLblPendienteDomestico() {
		return lblPendienteDomestico;
	}

	public void setLblPendienteDomestico(HtmlOutputText lblPendienteDomestico) {
		this.lblPendienteDomestico = lblPendienteDomestico;
	}

	public HtmlOutputText getTxtPendienteDomestico() {
		return txtPendienteDomestico;
	}

	public void setTxtPendienteDomestico(HtmlOutputText txtPendienteDomestico) {
		this.txtPendienteDomestico = txtPendienteDomestico;
	}

	protected P55RECIBO getP55recibo() {
		try {
			p55recibo = (P55RECIBO)FacesContext.getCurrentInstance().
					getELContext().getELResolver().getValue(FacesContext
							.getCurrentInstance().getELContext(),
							null,"p55recibo" ); 
			
//			HttpServletRequest request = (HttpServletRequest) FacesContext
//					.getCurrentInstance().getExternalContext().getRequest();
//			HttpSession s = request.getSession();
//			p55recibo = (P55RECIBO) s.getAttribute("p55recibo");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p55recibo;
	}
	protected void setP55recibo(P55RECIBO p55recibo) {
		this.p55recibo = p55recibo;
	}
	public HtmlOutputText getLblResultadoRec() {
		return lblResultadoRec;
	}
	public void setLblResultadoRec(HtmlOutputText lblResultadoRec) {
		this.lblResultadoRec = lblResultadoRec;
	}
	public String getTxtCodUnineg() {
		return txtCodUnineg;
	}
	public void setTxtCodUnineg(String txtCodUnineg) {
		this.txtCodUnineg = txtCodUnineg;
	}
	public HtmlGraphicImageEx getImgWatermark() {
		return imgWatermark;
	}
	public void setImgWatermark(HtmlGraphicImageEx imgWatermark) {
		this.imgWatermark = imgWatermark;
	}

	public HtmlDialogWindow getDwDevolucion() {
		return dwDevolucion;
	}

	public void setDwDevolucion(HtmlDialogWindow dwDevolucion) {
		this.dwDevolucion = dwDevolucion;
	}

	public HtmlDropDownList getCmbTiporeciboDev() {
		return cmbTiporeciboDev;
	}

	public void setCmbTiporeciboDev(HtmlDropDownList cmbTiporeciboDev) {
		this.cmbTiporeciboDev = cmbTiporeciboDev;
	}

	public HtmlOutputText getFechaReciboDev() {
		return fechaReciboDev;
	}

	public void setFechaReciboDev(HtmlOutputText fechaReciboDev) {
		this.fechaReciboDev = fechaReciboDev;
	}

	public HtmlOutputText getLblCodigoClienteDev() {
		return lblCodigoClienteDev;
	}

	public void setLblCodigoClienteDev(HtmlOutputText lblCodigoClienteDev) {
		this.lblCodigoClienteDev = lblCodigoClienteDev;
	}

	public HtmlOutputText getLblNombreClienteDev() {
		return lblNombreClienteDev;
	}

	public void setLblNombreClienteDev(HtmlOutputText lblNombreClienteDev) {
		this.lblNombreClienteDev = lblNombreClienteDev;
	}

	public HtmlOutputText getLblNumeroReciboManualDev() {
		return lblNumeroReciboManualDev;
	}

	public void setLblNumeroReciboManualDev(
			HtmlOutputText lblNumeroReciboManualDev) {
		this.lblNumeroReciboManualDev = lblNumeroReciboManualDev;
	}

	public HtmlDateChooser getTxtFechaManualDev() {
		return txtFechaManualDev;
	}

	public void setTxtFechaManualDev(HtmlDateChooser txtFechaManualDev) {
		this.txtFechaManualDev = txtFechaManualDev;
	}

	public HtmlOutputText getTxtNumeroReciboDev() {
		return txtNumeroReciboDev;
	}

	public void setTxtNumeroReciboDev(HtmlOutputText txtNumeroReciboDev) {
		this.txtNumeroReciboDev = txtNumeroReciboDev;
	}

	public HtmlInputText getTxtNumeroReciboManulDev() {
		return txtNumeroReciboManulDev;
	}

	public void setTxtNumeroReciboManulDev(HtmlInputText txtNumeroReciboManulDev) {
		this.txtNumeroReciboManulDev = txtNumeroReciboManulDev;
	}

	public HtmlDropDownList getCmbMetodosPagoFacDev() {
		return cmbMetodosPagoFacDev;
	}

	public void setCmbMetodosPagoFacDev(HtmlDropDownList cmbMetodosPagoFacDev) {
		this.cmbMetodosPagoFacDev = cmbMetodosPagoFacDev;
	}

	public HtmlDropDownList getCmbMonedaFacDev() {
		return cmbMonedaFacDev;
	}

	public void setCmbMonedaFacDev(HtmlDropDownList cmbMonedaFacDev) {
		this.cmbMonedaFacDev = cmbMonedaFacDev;
	}

	public HtmlInputText getTxtMontoFacDev() {
		return txtMontoFacDev;
	}

	public void setTxtMontoFacDev(HtmlInputText txtMontoFacDev) {
		this.txtMontoFacDev = txtMontoFacDev;
	}

	public HtmlOutputText getLblFaltante() {
		return lblFaltante;
	}

	public void setLblFaltante(HtmlOutputText lblFaltante) {
		this.lblFaltante = lblFaltante;
	}

	public HtmlOutputText getLblMontoAplicarDev() {
		return lblMontoAplicarDev;
	}

	public void setLblMontoAplicarDev(HtmlOutputText lblMontoAplicarDev) {
		this.lblMontoAplicarDev = lblMontoAplicarDev;
	}

	public HtmlOutputText getLblMontoProcesado() {
		return lblMontoProcesado;
	}

	public void setLblMontoProcesado(HtmlOutputText lblMontoProcesado) {
		this.lblMontoProcesado = lblMontoProcesado;
	}

	public HtmlInputTextarea getTxtConceptoDev() {
		return txtConceptoDev;
	}

	public void setTxtConceptoDev(HtmlInputTextarea txtConceptoDev) {
		this.txtConceptoDev = txtConceptoDev;
	}

	public HtmlOutputText getTxtFaltante() {
		return txtFaltante;
	}

	public void setTxtFaltante(HtmlOutputText txtFaltante) {
		this.txtFaltante = txtFaltante;
	}

	public HtmlOutputText getTxtMontoAplicarDev() {
		return txtMontoAplicarDev;
	}

	public void setTxtMontoAplicarDev(HtmlOutputText txtMontoAplicarDev) {
		this.txtMontoAplicarDev = txtMontoAplicarDev;
	}

	public HtmlOutputText getTxtMontoProcesado() {
		return txtMontoProcesado;
	}

	public void setTxtMontoProcesado(HtmlOutputText txtMontoProcesado) {
		this.txtMontoProcesado = txtMontoProcesado;
	}

	public HtmlOutputText getLblMonedaFactDev() {
		return lblMonedaFactDev;
	}

	public void setLblMonedaFactDev(HtmlOutputText lblMonedaFactDev) {
		this.lblMonedaFactDev = lblMonedaFactDev;
	}

	public HtmlOutputText getLblMontoFactDev() {
		return lblMontoFactDev;
	}

	public void setLblMontoFactDev(HtmlOutputText lblMontoFactDev) {
		this.lblMontoFactDev = lblMontoFactDev;
	}

	public HtmlOutputText getLblNoFactDev() {
		return lblNoFactDev;
	}

	public void setLblNoFactDev(HtmlOutputText lblNoFactDev) {
		this.lblNoFactDev = lblNoFactDev;
	}

	public HtmlOutputText getLblTipoDocFactDev() {
		return lblTipoDocFactDev;
	}

	public void setLblTipoDocFactDev(HtmlOutputText lblTipoDocFactDev) {
		this.lblTipoDocFactDev = lblTipoDocFactDev;
	}

	public HtmlOutputText getLblNodoco() {
		return lblNodoco;
	}

	public void setLblNodoco(HtmlOutputText lblNodoco) {
		this.lblNodoco = lblNodoco;
	}

	public HtmlOutputText getLblTipodoco() {
		return lblTipodoco;
	}

	public void setLblTipodoco(HtmlOutputText lblTipodoco) {
		this.lblTipodoco = lblTipodoco;
	}

	public HtmlOutputText getLblFechadev() {
		return lblFechadev;
	}

	public void setLblFechadev(HtmlOutputText lblFechadev) {
		this.lblFechadev = lblFechadev;
	}

	public HtmlOutputText getLblHoraReciboDev() {
		return lblHoraReciboDev;
	}

	public void setLblHoraReciboDev(HtmlOutputText lblHoraReciboDev) {
		this.lblHoraReciboDev = lblHoraReciboDev;
	}

	public HtmlOutputText getLblMonedaOdev() {
		return lblMonedaOdev;
	}

	public void setLblMonedaOdev(HtmlOutputText lblMonedaOdev) {
		this.lblMonedaOdev = lblMonedaOdev;
	}

	public HtmlOutputText getLblMontoOdev() {
		return lblMontoOdev;
	}

	public void setLblMontoOdev(HtmlOutputText lblMontoOdev) {
		this.lblMontoOdev = lblMontoOdev;
	}

	public HtmlOutputText getLblNoReciboOdev() {
		return lblNoReciboOdev;
	}

	public void setLblNoReciboOdev(HtmlOutputText lblNoReciboOdev) {
		this.lblNoReciboOdev = lblNoReciboOdev;
	}

	public HtmlGridView getGvDetalleReciboFactDev() {
		return gvDetalleReciboFactDev;
	}

	public void setGvDetalleReciboFactDev(HtmlGridView gvDetalleReciboFactDev) {
		this.gvDetalleReciboFactDev = gvDetalleReciboFactDev;
	}

	public HtmlGridView getGvDetalleReciboOriginal() {
		return gvDetalleReciboOriginal;
	}

	public void setGvDetalleReciboOriginal(HtmlGridView gvDetalleReciboOriginal) {
		this.gvDetalleReciboOriginal = gvDetalleReciboOriginal;
	}

	public List getLstDetalleReciboFactDev() {
		if (m.get("lstDetalleReciboFactDev") == null) {
			lstDetalleReciboFactDev = new ArrayList();
		} else {
			lstDetalleReciboFactDev = (List) m.get("lstDetalleReciboFactDev");
		}
		return lstDetalleReciboFactDev;
	}

	public void setLstDetalleReciboFactDev(List lstDetalleReciboFactDev) {
		this.lstDetalleReciboFactDev = lstDetalleReciboFactDev;
	}

	public List getLstDetalleReciboOriginal() {
		if (m.get("lstDetalleReciboOriginal") == null) {
			lstDetalleReciboOriginal = new ArrayList();
		} else {
			lstDetalleReciboOriginal = (List) m.get("lstDetalleReciboOriginal");
		}
		return lstDetalleReciboOriginal;
	}

	public void setLstDetalleReciboOriginal(List lstDetalleReciboOriginal) {
		this.lstDetalleReciboOriginal = lstDetalleReciboOriginal;
	}

	public HtmlDialogWindow getDwCancelarDev() {
		return dwCancelarDev;
	}

	public void setDwCancelarDev(HtmlDialogWindow dwCancelarDev) {
		this.dwCancelarDev = dwCancelarDev;
	}

	public HtmlOutputText getLblTasaCambioDev() {
		return lblTasaCambioDev;
	}

	public void setLblTasaCambioDev(HtmlOutputText lblTasaCambioDev) {
		this.lblTasaCambioDev = lblTasaCambioDev;
	}

	public HtmlOutputText getLblTasaJDEDev() {
		return lblTasaJDEDev;
	}

	public void setLblTasaJDEDev(HtmlOutputText lblTasaJDEDev) {
		this.lblTasaJDEDev = lblTasaJDEDev;
	}

	public HtmlCheckBox getChkImprimirDevolucion() {
		return chkImprimirDevolucion;
	}

	public void setChkImprimirDevolucion(HtmlCheckBox chkImprimirDevolucion) {
		this.chkImprimirDevolucion = chkImprimirDevolucion;
	}

	public HtmlDialogWindow getDwProcesaDevolucion() {
		return dwProcesaDevolucion;
	}

	public void setDwProcesaDevolucion(HtmlDialogWindow dwProcesaDevolucion) {
		this.dwProcesaDevolucion = dwProcesaDevolucion;
	}

	public List getLstMonedaDev() {
		try {
			if (m.get("lstMonedaDev") == null) {
				lstMonedaDev = new ArrayList();
			} else {
				lstMonedaDev = (List) m.get("lstMonedaDev");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return lstMonedaDev;
	}

	public void setLstMonedaDev(List lstMonedaDev) {
		this.lstMonedaDev = lstMonedaDev;
	}

	public List getLstMetodosPagoDev() {
		try {
			if (m.get("lstMetodosPagoDev") == null) {
				lstMetodosPagoDev = new ArrayList();
			} else {
				lstMetodosPagoDev = (List) m.get("lstMetodosPagoDev");
			}
		} catch (Exception ex) {
			ex.printStackTrace();

		}
		return lstMetodosPagoDev;
	}

	public void setLstMetodosPagoDev(List lstMetodosPagoDev) {
		this.lstMetodosPagoDev = lstMetodosPagoDev;
	}

	public HtmlOutputText getLblCodsucDev() {
		return lblCodsucDev;
	}

	public void setLblCodsucDev(HtmlOutputText lblCodsucDev) {
		this.lblCodsucDev = lblCodsucDev;
	}

	public HtmlOutputText getLblCoduninegDev() {
		return lblCoduninegDev;
	}

	public void setLblCoduninegDev(HtmlOutputText lblCoduninegDev) {
		this.lblCoduninegDev = lblCoduninegDev;
	}

	public String getLblVendedorCont() {
		return lblVendedorCont;
	}

	public void setLblVendedorCont(String lblVendedorCont) {
		this.lblVendedorCont = lblVendedorCont;
	}

	public String getTxtVendedorCont() {
		return txtVendedorCont;
	}
	public void setTxtVendedorCont(String txtVendedorCont) {
		this.txtVendedorCont = txtVendedorCont;
	}
	public HtmlDialogWindow getDwConfirmaEmisionCheque() {
		return dwConfirmaEmisionCheque;
	}
	public void setDwConfirmaEmisionCheque(HtmlDialogWindow dwConfirmaEmisionCheque) {
		this.dwConfirmaEmisionCheque = dwConfirmaEmisionCheque;
	}
	public HtmlOutputText getLblMsgValidaSolecheque() {
		return lblMsgValidaSolecheque;
	}
	public void setLblMsgValidaSolecheque(HtmlOutputText lblMsgValidaSolecheque) {
		this.lblMsgValidaSolecheque = lblMsgValidaSolecheque;
	}
	public HtmlDropDownList getDdlFiltroCajaEnvio() {
		return ddlFiltroCajaEnvio;
	}
	public void setDdlFiltroCajaEnvio(HtmlDropDownList ddlFiltroCajaEnvio) {
		this.ddlFiltroCajaEnvio = ddlFiltroCajaEnvio;
	}
	public HtmlDialogWindow getDwCajasParaTraslado() {
		return dwCajasParaTraslado;
	}
	public void setDwCajasParaTraslado(HtmlDialogWindow dwCajasParaTraslado) {
		this.dwCajasParaTraslado = dwCajasParaTraslado;
	}
	public HtmlGridView getGvCajasDisponibleEnvio() {
		return gvCajasDisponibleEnvio;
	}
	public void setGvCajasDisponibleEnvio(HtmlGridView gvCajasDisponibleEnvio) {
		this.gvCajasDisponibleEnvio = gvCajasDisponibleEnvio;
	}
	public HtmlInputText getTxtParamCajaEnvio() {
		return txtParamCajaEnvio;
	}
	public void setTxtParamCajaEnvio(HtmlInputText txtParamCajaEnvio) {
		this.txtParamCajaEnvio = txtParamCajaEnvio;
	}
	public List getLstCajasDisponibleEnvio() {
		lstCajasDisponibleEnvio = (m.get("fcd_lstCajasDisponibleEnvio")==null)? 
										new ArrayList():
										(ArrayList)m.get("fcd_lstCajasDisponibleEnvio");
		return lstCajasDisponibleEnvio;
	}
	public void setLstCajasDisponibleEnvio(List lstCajasDisponibleEnvio) {
		this.lstCajasDisponibleEnvio = lstCajasDisponibleEnvio;
	}
	public List getLstFiltroCajaEnvio() {
		if(lstFiltroCajaEnvio==null){
			lstFiltroCajaEnvio = new ArrayList(3);
			lstFiltroCajaEnvio.add(new SelectItem("1","Nombre","Buscar Caja por su nombre"));
			lstFiltroCajaEnvio.add(new SelectItem("2","Código","Buscar Caja por código"));
			lstFiltroCajaEnvio.add(new SelectItem("3","Cajero","Buscar Caja por nombre del cajero"));
		}
		return lstFiltroCajaEnvio;
	}
	public void setLstFiltroCajaEnvio(List lstFiltroCajaEnvio) {
		this.lstFiltroCajaEnvio = lstFiltroCajaEnvio;
	}
	public HtmlOutputText getLblValidaEnvioFac() {
		return lblValidaEnvioFac;
	}
	public void setLblValidaEnvioFac(HtmlOutputText lblValidaEnvioFac) {
		this.lblValidaEnvioFac = lblValidaEnvioFac;
	}
	public HtmlDropDownList getDdlFiltroCajasTras() {
		return ddlFiltroCajasTras;
	}
	public void setDdlFiltroCajasTras(HtmlDropDownList ddlFiltroCajasTras) {
		this.ddlFiltroCajasTras = ddlFiltroCajasTras;
	}
	public HtmlDropDownList getDdlFiltroFacturaTras() {
		return ddlFiltroFacturaTras;
	}
	public void setDdlFiltroFacturaTras(HtmlDropDownList ddlFiltroFacturaTras) {
		this.ddlFiltroFacturaTras = ddlFiltroFacturaTras;
	}
	public HtmlDialogWindow getDwFacturasTraslado() {
		return dwFacturasTraslado;
	}
	public void setDwFacturasTraslado(HtmlDialogWindow dwFacturasTraslado) {
		this.dwFacturasTraslado = dwFacturasTraslado;
	}
	
	public HtmlDialogWindow getDwMostrarTraslado() {
		return dwMostrarTraslado;
	}

	public void setDwMostrarTraslado(HtmlDialogWindow dwMostrarTraslado) {
		this.dwMostrarTraslado = dwMostrarTraslado;
	}

	public HtmlGridView getGvFacturasParaTraslado() {
		return gvFacturasParaTraslado;
	}
	public void setGvFacturasParaTraslado(HtmlGridView gvFacturasParaTraslado) {
		this.gvFacturasParaTraslado = gvFacturasParaTraslado;
	}
	public HtmlOutputText getLblValidaTraerFac() {
		return lblValidaTraerFac;
	}
	public void setLblValidaTraerFac(HtmlOutputText lblValidaTraerFac) {
		this.lblValidaTraerFac = lblValidaTraerFac;
	}
	public List getLstFacturasParaTraslado() {
		lstFacturasParaTraslado = (m.get("fcd_lstFacturasParaTraslado")==null)? new ArrayList(1):
			(ArrayList)m.get("fcd_lstFacturasParaTraslado");
		return lstFacturasParaTraslado;
	}
	public void setLstFacturasParaTraslado(List lstFacturasParaTraslado) {
		this.lstFacturasParaTraslado = lstFacturasParaTraslado;
	}
	public List getLstFiltroCajasTras() {
		lstFiltroCajasTras = (m.get("fcd_lstFiltroCajasTras")==null)? new ArrayList(1):
									(ArrayList)m.get("fcd_lstFiltroCajasTras");
		return lstFiltroCajasTras;
	}
	public void setLstFiltroCajasTras(List lstFiltroCajasTras) {
		this.lstFiltroCajasTras = lstFiltroCajasTras;
	}
	public List getLstFiltroFacturaTras() {
		if(lstFiltroFacturaTras==null){
			lstFiltroFacturaTras = new ArrayList(3);
			lstFiltroFacturaTras.add(new SelectItem("1","Nombre Cliente","Buscar Factura por nombre del cliente"));
			lstFiltroFacturaTras.add(new SelectItem("2","Código Cliente","Buscar Factura por código del cliente"));
			lstFiltroFacturaTras.add(new SelectItem("3","N° Factura","Buscar Factura por número"));
		}
		return lstFiltroFacturaTras;
	}
	public void setLstFiltroFacturaTras(List lstFiltroFacturaTras) {
		this.lstFiltroFacturaTras = lstFiltroFacturaTras;
	}
	public HtmlInputText getTxtParamFiltrofac() {
		return txtParamFiltrofac;
	}
	public void setTxtParamFiltrofac(HtmlInputText txtParamFiltrofac) {
		this.txtParamFiltrofac = txtParamFiltrofac;
	}
	public HtmlDialogWindow getDwCrearNotaCredito() {
		return dwCrearNotaCredito;
	}
	public void setDwCrearNotaCredito(HtmlDialogWindow dwCrearNotaCredito) {
		this.dwCrearNotaCredito = dwCrearNotaCredito;
	}

	public String getMsgCodigoCliente() {
		return msgCodigoCliente;
	}

	public void setMsgCodigoCliente(String msgCodigoCliente) {
		this.msgCodigoCliente = msgCodigoCliente;
	}

	public HtmlInputText getTxtCodCli() {
		return txtCodCli;
	}

	public void setTxtCodCli(HtmlInputText txtCodCli) {
		this.txtCodCli = txtCodCli;
	}
	public String getStrInfoCliente() {
		return strInfoCliente;
	}
	public void setStrInfoCliente(String strInfoCliente) {
		this.strInfoCliente = strInfoCliente;
	}
	public HtmlInputSecret getTrack() {
		return track;
	}
	public void setTrack(HtmlInputSecret track) {
		this.track = track;
	}
	public HtmlDialogWindow getDwCargando() {
		return dwCargando;
	}
	public void setDwCargando(HtmlDialogWindow dwCargando) {
		this.dwCargando = dwCargando;
	}
	public HtmlCheckBox getChkVoucherManual() {
		return chkVoucherManual;
	}
	public void setChkVoucherManual(HtmlCheckBox chkVoucherManual) {
		this.chkVoucherManual = chkVoucherManual;
	}
	public HtmlOutputText getLbletVouchermanual() {
		return lbletVouchermanual;
	}
	public void setLbletVouchermanual(HtmlOutputText lbletVouchermanual) {
		this.lbletVouchermanual = lbletVouchermanual;
	}
	public HtmlCheckBox getChkTrasladarPOS() {
		return chkTrasladarPOS;
	}
	public void setChkTrasladarPOS(HtmlCheckBox chkTrasladarPOS) {
		this.chkTrasladarPOS = chkTrasladarPOS;
	}
	public HtmlOutputText getLblEtTrasladoPOS() {
		return lblEtTrasladoPOS;
	}
	public void setLblEtTrasladoPOS(HtmlOutputText lblEtTrasladoPOS) {
		this.lblEtTrasladoPOS = lblEtTrasladoPOS;
	}


	public HtmlGridView getGvClienteFacsRM() {
		return gvClienteFacsRM;
	}


	public void setGvClienteFacsRM(HtmlGridView gvClienteFacsRM) {
		this.gvClienteFacsRM = gvClienteFacsRM;
	}


	public List getSelectedFacsRM() {
		try{
			if(m.get("selectedFacsRM") != null){
				selectedFacsRM = (List)m.get("selectedFacsRM");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return selectedFacsRM;
	}

	public void setSelectedFacsRM(List selectedFacsRM) {
		this.selectedFacsRM = selectedFacsRM;
	}

	public UIOutput getLblTrack() {
		return lblTrack;
	}

	public void setLblTrack(UIOutput lblTrack) {
		this.lblTrack = lblTrack;
	}

	public HtmlCheckBox getChkIngresoManual() {
		return chkIngresoManual;
	}

	public void setChkIngresoManual(HtmlCheckBox chkIngresoManual) {
		this.chkIngresoManual = chkIngresoManual;
	}

	public HtmlOutputText getLblNoTarjeta() {
		return lblNoTarjeta;
	}

	public void setLblNoTarjeta(HtmlOutputText lblNoTarjeta) {
		this.lblNoTarjeta = lblNoTarjeta;
	}

	public HtmlInputText getTxtNoTarjeta() {
		return txtNoTarjeta;
	}

	public void setTxtNoTarjeta(HtmlInputText txtNoTarjeta) {
		this.txtNoTarjeta = txtNoTarjeta;
	}

	public HtmlOutputText getLblFechaVenceT() {
		return lblFechaVenceT;
	}

	public void setLblFechaVenceT(HtmlOutputText lblFechaVenceT) {
		this.lblFechaVenceT = lblFechaVenceT;
	}

	public HtmlInputText getTxtFechaVenceT() {
		return txtFechaVenceT;
	}

	public void setTxtFechaVenceT(HtmlInputText txtFechaVenceT) {
		this.txtFechaVenceT = txtFechaVenceT;
	}
	
	/**
	 * @managed-bean true
	 */
	protected P55CA090 getP55ca090() {
		if (p55ca090 == null) {
			p55ca090 = (P55CA090) FacesContext.getCurrentInstance()
					.getApplication().createValueBinding("#{p55ca090}")
					.getValue(FacesContext.getCurrentInstance());
		}
		return p55ca090;
	}

	public double getDMaximoSobrante() {
		return dMaximoSobrante;
	}
	public void setDMaximoSobrante(double maximoSobrante) {
		dMaximoSobrante = maximoSobrante;
	}
	/**
	 * @managed-bean true
	 */
	protected void setP55ca090(P55CA090 p55ca090) {
		this.p55ca090 = p55ca090;
	}
	public String getStrMensajeValidacion() {
		return strMensajeValidacion;
	}
	public void setStrMensajeValidacion(String strMensajeValidacion) {
		this.strMensajeValidacion = strMensajeValidacion;
	}
	public HtmlDialogWindow getDwBorrarPago() {
		return dwBorrarPago;
	}
	public void setDwBorrarPago(HtmlDialogWindow dwBorrarPago) {
		this.dwBorrarPago = dwBorrarPago;
	}
	public HtmlOutputText getLblMontoEquiv() {
		return lblMontoEquiv;
	}
	public void setLblMontoEquiv(HtmlOutputText lblMontoEquiv) {
		this.lblMontoEquiv = lblMontoEquiv;
	}
	public HtmlOutputText getLblDevMontoEquiv() {
		return lblDevMontoEquiv;
	}
	public void setLblDevMontoEquiv(HtmlOutputText lblDevMontoEquiv) {
		this.lblDevMontoEquiv = lblDevMontoEquiv;
	}
	public HtmlDialogWindow getDwIngresarDatosDonacion() {
		return dwIngresarDatosDonacion;
	}

	public void setDwIngresarDatosDonacion(HtmlDialogWindow dwIngresarDatosDonacion) {
		this.dwIngresarDatosDonacion = dwIngresarDatosDonacion;
	}

	public HtmlGridView getGvDonacionesRecibidas() {
		return gvDonacionesRecibidas;
	}

	public void setGvDonacionesRecibidas(HtmlGridView gvDonacionesRecibidas) {
		this.gvDonacionesRecibidas = gvDonacionesRecibidas;
	}

	public List<DncIngresoDonacion> getLstDonacionesRecibidas() {
		
		if(CodeUtil.getFromSessionMap("fcd_lstDonacionesRecibidas") != null )
			return lstDonacionesRecibidas = (ArrayList<DncIngresoDonacion>)
					CodeUtil.getFromSessionMap("fcd_lstDonacionesRecibidas");
		if(lstDonacionesRecibidas == null) 
			lstDonacionesRecibidas = new ArrayList<DncIngresoDonacion>();
		
		return lstDonacionesRecibidas;
	}

	public void setLstDonacionesRecibidas(
			List<DncIngresoDonacion> lstDonacionesRecibidas) {
		this.lstDonacionesRecibidas = lstDonacionesRecibidas;
	}

	public HtmlOutputText getLblTotalMontoDonacion() {
		return lblTotalMontoDonacion;
	}

	public void setLblTotalMontoDonacion(HtmlOutputText lblTotalMontoDonacion) {
		this.lblTotalMontoDonacion = lblTotalMontoDonacion;
	}

	public HtmlOutputText getLblTotalMontoDisponible() {
		return lblTotalMontoDisponible;
	}

	public void setLblTotalMontoDisponible(HtmlOutputText lblTotalMontoDisponible) {
		this.lblTotalMontoDisponible = lblTotalMontoDisponible;
	}

	public HtmlOutputText getLblFormaDePagoCliente() {
		return lblFormaDePagoCliente;
	}

	public void setLblFormaDePagoCliente(HtmlOutputText lblFormaDePagoCliente) {
		this.lblFormaDePagoCliente = lblFormaDePagoCliente;
	}

	public List<SelectItem> getLstBeneficiarios() {
		try {
			
			lstBeneficiarios = (ArrayList<SelectItem>)CodeUtil.getFromSessionMap("fdc_lstbeneficiarios");
			if(lstBeneficiarios != null &&  !lstBeneficiarios.isEmpty() )
				return lstBeneficiarios;
			
			lstBeneficiarios = (ArrayList<SelectItem>)DonacionesCtrl.obtenerBeneficiariosConfigurados(true, false);
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}finally{
			
			if(lstBeneficiarios == null )
				lstBeneficiarios = new ArrayList<SelectItem>();

			 CodeUtil.putInSessionMap("fdc_lstbeneficiarios", lstBeneficiarios);
			
		}
		
		return lstBeneficiarios;
	}

	public void setLstBeneficiarios(List<SelectItem> lstBeneficiarios) {
		this.lstBeneficiarios = lstBeneficiarios;
	}

	public HtmlDropDownList getDdlDnc_Beneficiario() {
		return ddlDnc_Beneficiario;
	}

	public void setDdlDnc_Beneficiario(HtmlDropDownList ddlDnc_Beneficiario) {
		this.ddlDnc_Beneficiario = ddlDnc_Beneficiario;
	}

	public HtmlInputText getTxtdnc_montodonacion() {
		return txtdnc_montodonacion;
	}

	public void setTxtdnc_montodonacion(HtmlInputText txtdnc_montodonacion) {
		this.txtdnc_montodonacion = txtdnc_montodonacion;
	}

	public HtmlOutputText getMsgValidaIngresoDonacion() {
		return msgValidaIngresoDonacion;
	}

	public void setMsgValidaIngresoDonacion(HtmlOutputText msgValidaIngresoDonacion) {
		this.msgValidaIngresoDonacion = msgValidaIngresoDonacion;
	}
	public HtmlJspPanel getPnlSeccionDonaciones() {
		return pnlSeccionDonaciones;
	}
	public void setPnlSeccionDonaciones(HtmlJspPanel pnlSeccionDonaciones) {
		this.pnlSeccionDonaciones = pnlSeccionDonaciones;
	}
	public HtmlGridView getGvDetalleDonaciones() {
		return gvDetalleDonaciones;
	}
	public void setGvDetalleDonaciones(HtmlGridView gvDetalleDonaciones) {
		this.gvDetalleDonaciones = gvDetalleDonaciones;
	}
	public List<DncDonacion> getLstDetalleDonaciones() {
		
		if(lstDetalleDonaciones == null)
			lstDetalleDonaciones = new ArrayList<DncDonacion>();
		
		return lstDetalleDonaciones;
	}
	public void setLstDetalleDonaciones(List<DncDonacion> lstDetalleDonaciones) {
		this.lstDetalleDonaciones = lstDetalleDonaciones;
	}
	public HtmlOutputText getLblNoBatchDonacion() {
		return lblNoBatchDonacion;
	}
	public void setLblNoBatchDonacion(HtmlOutputText lblNoBatchDonacion) {
		this.lblNoBatchDonacion = lblNoBatchDonacion;
	}
	public HtmlOutputText getLblNoDocsJdeDonacion() {
		return lblNoDocsJdeDonacion;
	}
	public void setLblNoDocsJdeDonacion(HtmlOutputText lblNoDocsJdeDonacion) {
		this.lblNoDocsJdeDonacion = lblNoDocsJdeDonacion;
	}

	public HtmlOutputText getLblMontosTotalDonaciones() {
		return lblMontosTotalDonaciones;
	}

	public void setLblMontosTotalDonaciones(HtmlOutputText lblMontosTotalDonaciones) {
		this.lblMontosTotalDonaciones = lblMontosTotalDonaciones;
	}

	public HtmlOutputText getLblEtTotalDonaciones() {
		return lblEtTotalDonaciones;
	}

	public void setLblEtTotalDonaciones(HtmlOutputText lblEtTotalDonaciones) {
		this.lblEtTotalDonaciones = lblEtTotalDonaciones;
	}
	public HtmlDropDownList getDdlTipoMarcasTarjetas() {
		return ddlTipoMarcasTarjetas;
	}

	public void setDdlTipoMarcasTarjetas(HtmlDropDownList ddlTipoMarcasTarjetas) {
		this.ddlTipoMarcasTarjetas = ddlTipoMarcasTarjetas;
	}

	public List<SelectItem> getLstMarcasDeTarjetas() {
		
		try {
			
			lstMarcasDeTarjetas = (ArrayList<SelectItem>)CodeUtil.getFromSessionMap("fdc_lstMarcasDeTarjetas");
			if(lstMarcasDeTarjetas != null &&  !lstMarcasDeTarjetas.isEmpty() )
				return lstMarcasDeTarjetas;
			
			lstMarcasDeTarjetas = AfiliadoCtrl.obtenerTiposMarcaTarjetas();
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}finally{
			
			if(lstMarcasDeTarjetas == null )
				lstMarcasDeTarjetas = new ArrayList<SelectItem>();

			 CodeUtil.putInSessionMap("fdc_lstMarcasDeTarjetas", lstMarcasDeTarjetas);
		}
		
		return lstMarcasDeTarjetas;
	}
	public void setLstMarcasDeTarjetas(List<SelectItem> lstMarcasDeTarjetas) {
		this.lstMarcasDeTarjetas = lstMarcasDeTarjetas;
	}
	public HtmlOutputText getLblMarcaTarjeta() {
		return lblMarcaTarjeta;
	}
	public void setLblMarcaTarjeta(HtmlOutputText lblMarcaTarjeta) {
		this.lblMarcaTarjeta = lblMarcaTarjeta;
	}



	public List getLstFacturasTrasladadas() {
		lstFacturasTrasladadas = (m.get("fcd_lstFacturasTrasladadas")==null)? new ArrayList(1):
			(ArrayList)m.get("fcd_lstFacturasTrasladadas");
		
		return lstFacturasTrasladadas;
	}



	public void setLstFacturasTrasladadas(List lstFacturasTrasladadas) {
		this.lstFacturasTrasladadas = lstFacturasTrasladadas;
	}



	public HtmlGridView getGvFacturasTrasladadas() {
		return gvFacturasTrasladadas;
	}



	public void setGvFacturasTrasladadas(HtmlGridView gvFacturasTrasladadas) {
		this.gvFacturasTrasladadas = gvFacturasTrasladadas;
	}



	
}

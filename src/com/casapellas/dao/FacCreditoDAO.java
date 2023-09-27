package com.casapellas.dao;
/**
 * CASA PELLAS S.A.
 * Creado por.........: Juan Carlos Ñamendi Pineda
 * Fecha de Creación..: 28/02/2009
 * Última modificación: 07/10/2011
 * Modificado por.....:	Juan Carlos Ñamendi Pineda
 * Manejo de moneda base para incluir TRADER
 * 
 */

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

import nl.bitwalker.useragentutils.UserAgent;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
//import org.apache.commons.mail.MultiPartEmail;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.casapellas.controles.DonacionesCtrl;
import com.casapellas.controles.SendMailsCtrl;
import com.casapellas.controles.VerificarFacturaProceso;
import com.casapellas.controles.tmp.AfiliadoCtrl;
import com.casapellas.controles.tmp.BancoCtrl;
import com.casapellas.controles.tmp.CompaniaCtrl;
import com.casapellas.controles.tmp.CtrlCajas;
import com.casapellas.controles.tmp.CtrlPoliticas;
import com.casapellas.controles.tmp.CtrlSolicitud;
import com.casapellas.controles.tmp.EmpleadoCtrl;
import com.casapellas.controles.tmp.FacturaCreditoCtrl;
import com.casapellas.controles.tmp.InteresesRevolventesCtrl;
import com.casapellas.controles.tmp.MetodosPagoCtrl;
import com.casapellas.controles.tmp.MonedaCtrl;
import com.casapellas.controles.tmp.NumcajaCtrl;
import com.casapellas.controles.tmp.ReciboCtrl;
import com.casapellas.controles.tmp.ReciboPrimaCtrl;
import com.casapellas.controles.tmp.SucursalCtrl;
import com.casapellas.controles.tmp.TasaCambioCtrl;
import com.casapellas.donacion.entidades.DncIngresoDonacion;
import com.casapellas.entidades.Cafiliados;
import com.casapellas.entidades.Credhdr;
import com.casapellas.entidades.F55ca014;
import com.casapellas.entidades.F55ca022;
import com.casapellas.entidades.FacturaCredito;
import com.casapellas.entidades.Hfacturajde;
import com.casapellas.entidades.MetodosPago;
import com.casapellas.entidades.Metpago;
import com.casapellas.entidades.Pago;
import com.casapellas.entidades.Recibo;
import com.casapellas.entidades.Recibojde;
import com.casapellas.entidades.Solicitud;
import com.casapellas.entidades.SolicitudId;
import com.casapellas.entidades.Tcambio;
import com.casapellas.entidades.TcambioId;
import com.casapellas.entidades.Tpararela;
import com.casapellas.entidades.TpararelaId;
import com.casapellas.entidades.Unegocio;
import com.casapellas.entidades.Vf55ca012;
import com.casapellas.entidades.ens.Vautoriz;
import com.casapellas.entidades.Vf0101;
import com.casapellas.entidades.Vf0901;
import com.casapellas.entidades.Vf55ca01;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.jde.creditos.DatosComprobanteF0911;
import com.casapellas.jde.creditos.ProcesarEntradaDeDiarioCustom;
import com.casapellas.jde.creditos.ProcesarPagoFacturaJdeCustom;
import com.casapellas.navegacion.As400Connection;
import com.casapellas.rpg.P55CA090;
import com.casapellas.rpg.P55RECIBO;
import com.casapellas.socketpos.PosCtrl;
import com.casapellas.util.BitacoraSecuenciaReciboService;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.CustomEmailAddress;
import com.casapellas.util.Divisas;
import com.casapellas.util.DocumuentosTransaccionales;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.MailHelper;
import com.casapellas.util.PropertiesSystem;
import com.ibm.ObjectQuery.crud.util.Array;
import com.infragistics.faces.grid.component.Cell;
import com.infragistics.faces.grid.component.GridView;
import com.infragistics.faces.grid.component.RowItem;
import com.infragistics.faces.grid.component.html.HtmlGridView;
import com.infragistics.faces.grid.event.CellValueChangeEvent;
import com.infragistics.faces.grid.event.SelectedRowsChangeEvent;
import com.infragistics.faces.input.component.html.HtmlCheckBox;
import com.infragistics.faces.input.component.html.HtmlDateChooser;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
import com.infragistics.faces.shared.component.DataRepeater;
import com.infragistics.faces.shared.component.html.HtmlLink;
import com.infragistics.faces.shared.smartrefresh.SmartRefreshManager;
import com.infragistics.faces.window.component.DialogWindow;
import com.infragistics.faces.window.component.html.HtmlDialogWindow;
import com.infragistics.faces.window.event.StateChangeEvent;

public class FacCreditoDAO {
	protected P55RECIBO p55recibo;
	protected P55CA090 p55ca090;
	//Componentes para el pago con tarjeta
	private HtmlInputSecret track;	
	private UIOutput lblTrack;
	
	private HtmlCheckBox chkIngresoManual;
	private HtmlOutputText lblNoTarjeta;
	private HtmlInputText txtNoTarjeta;
	private HtmlOutputText lblFechaVenceT;
	private HtmlInputText txtFechaVenceT;
	//
	Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();	
	
	private List lstCompaniaCre,lstUninegCred,lstSucursalCred;
	private HtmlDropDownList ddlCompaniaCre,ddlUninegCred,ddlSucursalCred;
	
	private HtmlOutputText txtMensaje;
	private String sMensaje = "Realice la búsqueda de facturas del cliente";
	
	private HtmlOutputText lblTasaDetalle;
	private HtmlOutputText txtTasaDetalle;
	
	private HtmlDropDownList cmbMonedaDetalle;
	private List lstMonedasDetalle;
		
	private DialogWindow dgwDetalleCredito;
	private HtmlGridView gvHfacturasCredito;
	private List lstHfacturasCredito = null;      
	private HtmlDialogWindow dwCargando;   
	
	private HtmlOutputText lblMontoAplicarCredito;
	
	//Facturas selecciondas - definicion
	private List selectedFacsCredito = null;
	private GridView gvfacturasSelecCredito;
	//detalle de factura
	private DialogWindow dgwMensajeDetalleCredito;
	private HtmlOutputText lblMensajeDetalleCredito;
	
	private HtmlDropDownList cmbMonedaDetalleCredito;
	private HtmlOutputText txtFechaFactura;
	private HtmlOutputText txtNofactura;
	private HtmlOutputText txtCliente;
	private HtmlOutputText txtCodigoCliente;
	private HtmlOutputText txtUnineg;
	private HtmlOutputText txtSubtotal;
	private HtmlOutputText txtPendiente;
	private HtmlOutputText txtIva;
	private HtmlOutputText txtTotal;
	private HtmlOutputText txtTipoFactura;
	private HtmlOutputText txtCompania;
	private HtmlOutputText txtFechalm;
	private HtmlOutputText txtFechaVenc;
	private HtmlOutputText txtFechaImp;
	private HtmlOutputText txtDescuento;
	private HtmlOutputText txtDescuentoAplicado;
	private HtmlOutputText txtFechaVenDecto;
	private HtmlOutputText txtCondicion;
	private HtmlOutputText txtCompens;
	private HtmlOutputText txtNoOrden;
	private HtmlOutputText txtNoBatch;
	private HtmlOutputText txtTipoOrden;
	private HtmlOutputText txtFechaBatch;
	private HtmlOutputText txtObservaciones;
	private HtmlOutputText txtReferenciaFactura;
	
	private List lstDfacturasCredito;
	private HtmlGridView gvDfacturasCredito;
	//validacion de factura
	private HtmlOutputText lblValidaFactura;
	private DialogWindow dwValidacionFactura;
	//busqueda de facturas
	private HtmlDropDownList cmbFiltroMonedas;
	private List lstFiltroMonedas;
	private UIInput cmbBusquedaCredito;
	private List lstBusquedaCredito = null;
	private UIInput txtParametroCredito;
	private HtmlDateChooser dcFechaDesde;
	private HtmlDateChooser dcFechaHasta;
//////////RECIBO/////////////////////////////
	private DialogWindow dwRecibo;
	private String fechaRecibo;
	private HtmlOutputText lblCodigoCliente;
	private HtmlOutputText lblNombreCliente;
	private HtmlDateChooser txtFecham;
	private UIOutput lblNumrec2;
	private HtmlOutputText lblNumeroRecibo;
	private String lblNumrec = "Último Recibo: ";
	private HtmlInputText txtNumRec;
	private UIOutput lblNumRecm;
	private HtmlDropDownList cmbTiporecibo;
	private List lstTiporecibo;
	private HtmlOutputText lblTasaCambio;
	private HtmlOutputText lblMontoAplicar;
	private HtmlOutputText lblMontoRecibido;
	private HtmlOutputText txtMontoAplicar;
	private HtmlOutputText txtMontoRecibido;
	private HtmlOutputText lblTasaJDE;
	
//	Metodos de pago
	private HtmlDropDownList cmbMetodosPago;
	private List lstMetodosPago = null;
	
//	Monedas
	private HtmlInputText txtMonto;
	private HtmlDropDownList cmbMoneda;
	private List lstMoneda = null;
	
//	Afiliado
	private List lstAfiliado;	
	private HtmlDropDownList ddlAfiliado;
	private UIOutput lblAfiliado;
	
//	Bancos
	private List lstBanco;	
	private HtmlDropDownList ddlBanco;
	private UIOutput lblBanco;

//  referencias
	private HtmlInputText txtReferencia1;
	private HtmlInputText txtReferencia2;
	private HtmlInputText txtReferencia3;	
	private UIOutput lblReferencia1;
	private UIOutput lblReferencia2;
	private UIOutput lblReferencia3;
	
//	cancelar recibo
	private DialogWindow dwCancelar;
	
//	Dialogs Window
	private DialogWindow dwProcesa;
	private DialogWindow dwImprime;
	//validacion
	private UIOutput lblMensajeValidacion;
	private String strMensajeValidacion;
	
//	Grid de metodos de pago
	private HtmlGridView metodosGrid = null;
	private ArrayList<MetodosPago> selectedMet = null;

//	solicitud
	private DialogWindow dwSolicitud;
	private UIOutput lblMensajeAutorizacion;
	private List lstAutoriza;
	private HtmlInputText txtReferencia;	
	private HtmlDateChooser txtFecha;
	private HtmlInputTextarea txtObs;
	private HtmlDropDownList cmbAutoriza;
	
//	cambio
	private HtmlOutputText lblCambio;
	private HtmlOutputText txtCambio;
	private HtmlInputText txtCambioForaneo;
	private HtmlOutputText lblCambioDomestico;
	private HtmlOutputText txtCambioDomestico;
	private HtmlOutputText lblPendienteDomestico;
	private HtmlOutputText txtPendienteDomestico;
	private HtmlLink lnkCambio;
	
	private HtmlInputTextarea txtConcepto;
	
	private HtmlOutputText montoTotalAplicarDomestico;
	private HtmlOutputText montoTotalFaltanteDomestico;
	private HtmlOutputText montoTotalAplicarForaneo;
	private HtmlOutputText montoTotalFaltanteForaneo;
	
	//ventana para agregar facturas al recibo
	private DialogWindow dwAgregarFactura;
	private HtmlGridView gvAgregarFactura;
	private List lstAgregarFactura;
	private HtmlInputText txtNofacturaDesde;
	private HtmlInputText txtNofacturaHasta;
	
	//facturas originales para deshacer todo 
	//private List lstRefreshFacturas;
	private HtmlOutputText intSelected;
	private HtmlOutputText intSelectedDet;
	
	//---- Fijar Tasa de Cambio.
	private DialogWindow dwFijarTasaCambio;	
	private HtmlInputTextarea txtMotivoCambioTasa;
	private HtmlInputText txtNuevaTasaCambio;
	private HtmlOutputText lblMsgErrorCambioTasa;
	
	//------ Pago con tarjeta de crédito con voucher manuales.
	private HtmlCheckBox chkVoucherManual;
	private HtmlOutputText lbletVouchermanual;
	private double dMaximoSobrante = 600;
	private HtmlOutputText lblMarcaSobrDifer;
	private HtmlCheckBox chkSobranteDifrl;

	//&& ===== Mostrar Todos los resultados de la busqueda.
	private HtmlCheckBox chkMostrarTodoSrch;
	private HtmlDialogWindow dwBorrarPago;
	private int iNumeroFichaCambio = 0;
	
	//&& ===== donaciones en caja
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
	
	private List<SelectItem>lstMarcasDeTarjetas ;
	private HtmlDropDownList ddlTipoMarcasTarjetas;
	private HtmlOutputText lblMarcaTarjeta;
	
	//Nuevos valores para JDE
	String[] valoresJdeNumeracion = (String[]) m.get("valoresJDENumeracionIns");
	String[] valoresJDEInsCredito = (String[]) m.get("valoresJDEInsCredito");
	String[] valoresJdeInsContado = (String[]) m.get("valoresJDEInsContado");
	/***********************************************************************************************/
	
	public void procesarDonacionesIngresadas(ActionEvent ev){
		String msg = "";
		try {
			
			if( CodeUtil.getFromSessionMap("cr_lstDonacionesRecibidas") == null ){
				msg = "Ingrese al menos una donación para el proceso" ;
				return;
			}
			BigDecimal montodonado = new BigDecimal(lblTotalMontoDonacion.getValue().toString().trim().replace(",", ""));
			if(montodonado.compareTo(BigDecimal.ZERO) == 0 ){
				msg = "El monto de donaciónd debe ser mayor a cero" ;
				return;
			}
			CodeUtil.putInSessionMap("cr_MontoTotalEnDonacion", montodonado);
			
			dwIngresarDatosDonacion.setWindowState("hidden");
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}finally{
			msgValidaIngresoDonacion.setValue(msg);
			CodeUtil.refreshIgObjects(new Object[]{msgValidaIngresoDonacion}) ;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void agregarMontoDonacion(ActionEvent ev){
		String msg = "" ;
		try {
			
			List<Credhdr> lstFacturasSelected = (List<Credhdr>) m.get("selectedFacsCredito");
			lstFacturasSelected.get(0);
			
			final String beneficiario = ddlDnc_Beneficiario.getValue().toString();
			String strMontoDonacion = txtdnc_montodonacion.getValue().toString();
			String moneda = cmbMoneda.getValue().toString();
			String codformapago = cmbMetodosPago.getValue().toString(); 
			String metodopago = codformapago + ", " + lblFormaDePagoCliente.getValue().toString();
			String codcomp = lstFacturasSelected.get(0).getId().getCodcomp().trim();
			
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
			
			lstBeneficiarios = (ArrayList<SelectItem>)CodeUtil.getFromSessionMap("cr_lstbeneficiarios");
			SelectItem si = (SelectItem) CollectionUtils.find(lstBeneficiarios,
				new Predicate() {
					public boolean evaluate(Object o) {
						return ((SelectItem) o).getValue().toString().compareTo(beneficiario) == 0;
					}
				});
			
			lstDonacionesRecibidas = ( CodeUtil.getFromSessionMap("cr_lstDonacionesRecibidas") == null )?
					new ArrayList<DncIngresoDonacion>():
					(ArrayList<DncIngresoDonacion>)CodeUtil.getFromSessionMap("cr_lstDonacionesRecibidas");
					
			DncIngresoDonacion	dncExist = (DncIngresoDonacion)	 
			 CollectionUtils.find(lstDonacionesRecibidas, new Predicate(){
				public boolean evaluate(Object o) {
					return ((DncIngresoDonacion)o).getIdbenficiario() == 
							Integer.parseInt(beneficiario.split("@")[0]) ;
				}
			 });
			
			String nombrecliente =  lstFacturasSelected.get(0).getNomcli().trim().toLowerCase() ;
			int codigocliente = lstFacturasSelected.get(0).getId().getCodcli();
			int codigocajero = ((Vautoriz[])m.get("sevAut"))[0].getId().getCodreg();
			int caid = ( (List<Vf55ca01>) CodeUtil.getFromSessionMap( "lstCajas" ) ).get(0).getId().getCaid();
			
			String codigomarcatarjeta = "";
			String marcatarjeta = "";
			if(codformapago.compareTo(MetodosPagoCtrl.TARJETA ) == 0){
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
			
			CodeUtil.putInSessionMap("cr_lstDonacionesRecibidas", lstDonacionesRecibidas) ;
			gvDonacionesRecibidas.dataBind();
			
			CodeUtil.refreshIgObjects(new Object[]{lblTotalMontoDonacion, lblTotalMontoDisponible}) ;
			
		} catch (Exception e) {
			msg = "Donación no pudo ser aplicada";
			e.printStackTrace(); 
		}finally{
		
			msgValidaIngresoDonacion.setValue(msg);
			CodeUtil.refreshIgObjects( msgValidaIngresoDonacion ) ;
			
		}
	}
	public void cerrarVentanaDonacion(ActionEvent ev) {
		CodeUtil.removeFromSessionMap("cr_MontoTotalEnDonacion" ) ;
		CodeUtil.removeFromSessionMap("cr_lstDonacionesRecibida");
		dwIngresarDatosDonacion.setWindowState("hidden");
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
			List<Vf55ca012>metodospagoconfig = (ArrayList<Vf55ca012>)CodeUtil.getFromSessionMap("cr_MetodosPagoConfigurados");
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
			CodeUtil.putInSessionMap("cr_lstDonacionesRecibidas", lstDonacionesRecibidas);
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

	
	
	/** **************** mostrar ventana de confirmacion de borrar pago */
	public void mostrarBorrarPago(ActionEvent ev){
		try {
			RowItem ri = (RowItem) ev.getComponent().getParent().getParent();
			MetodosPago mPago = (MetodosPago) DataRepeater.getDataRow(ri);
			m.put("metodopagoborrar", mPago);
		} catch (Exception e) { 
		}
		dwBorrarPago.setWindowState("normal");
	}
	public void cerrarBorrarPago(ActionEvent ev){
		dwBorrarPago.setWindowState("hidden");
		m.remove("metodopagoborrar");
	}
	
	/** Método: Anular recibo de caja por mala aplicacion de socket pos
	 *	Fecha:  16/0622011
	 *  Nombre: Carlos Manuel Hernández Morrison.
	 */
	@SuppressWarnings("unchecked")
	public boolean  anularRecibo(int iNumrec,int iCaid, String sCodsuc, String sCodcomp,
								String sCodUsera, String sMonedaBase, 
								Vautoriz vAut[], int iNoFCV, int iCodcli){
		List<Recibojde> lstRecibojde = null;
		List<Hfacturajde> lstReciboFac = null;
		
		int iNobatch = 0;
		int iNodoc 	 = 0;
		int iBatchRC = 0;
		int iBatchAsiento = 0;
		
		boolean bHecho = true;
		boolean bBorrado = true;
		
		double[] dMontos = null;
		
		Hfacturajde hFac = null;
		Recibojde recibojde = null;
		ReciboCtrl recCtrl = new ReciboCtrl();
		Divisas divisas = new Divisas();
		Connection cn;
		
		try{
			cn = As400Connection.getJNDIConnection("DSMCAJA2");
			cn.setAutoCommit(false);
			
			recCtrl.actualizarEstadoRecibo(iNumrec,iCaid,sCodsuc,sCodcomp, sCodUsera, "No se completo pago con SP","CR");
			
			lstRecibojde = recCtrl.getEnlaceReciboJDE(iCaid, sCodsuc,sCodcomp, iNumrec, "CR");
			lstReciboFac = recCtrl.leerFacturasReciboCredito2(iCaid, sCodcomp, iNumrec, "CR", iCodcli);
			
			for(int j = 0;j < lstReciboFac.size(); j ++){
				hFac =  (Hfacturajde)lstReciboFac.get(j);

				if(hFac.getId().getMoneda().equals(sMonedaBase)){

					for (int k = 0; k < lstRecibojde.size(); k++){
						recibojde = (Recibojde)lstRecibojde.get(k);
						if(recibojde.getId().getNumrec() == iNumrec && recibojde.getId().getTipodoc().equals("R")){
							iBatchRC = recibojde.getId().getNobatch();
							break;
						}
					}
					
					dMontos = recCtrl.getMontoAplicadoF0311(cn, 
												hFac.getId().getNofactura(), 
												hFac.getId().getTipofactura(), 
												recibojde.getId().getRecjde(), 
												hFac.getId().getPartida(),
												hFac.getId().getCodcli());
					
					bBorrado = recCtrl.aplicarMontoF0311(cn, "A",
											(hFac.getId().getCpendiente() / 100.0) + divisas.roundDouble(dMontos[0]),
											0, vAut[0].getId().getLogin(), 
											"000"+hFac.getId().getCodsuc(), 
											hFac.getId().getTipofactura(), 
											hFac.getId().getNofactura(), 
											hFac.getId().getPartida(),
											hFac.getId().getCodcli());
					
					if(!bBorrado){
						break;
					}
					
					//&& =============== borrar RC 
					bBorrado = recCtrl.borrarRC(cn, hFac.getId().getNofactura(), 
												hFac.getId().getTipofactura(), 
												recibojde.getId().getRecjde(), 
												hFac.getId().getPartida(),
												hFac.getId().getCodcli());
					if(!bBorrado){
						break;
					}
					
					//&& =============== DETERMINAR SI HAY ASIENTOS EN EL RECIBO Y BORRAR
					for (int k = 0; k < lstRecibojde.size(); k++){
						recibojde = (Recibojde)lstRecibojde.get(k);
						if(recibojde.getId().getNumrec() == iNumrec && recibojde.getId().getTipodoc().equals("A")){
							iBatchAsiento = recibojde.getId().getRecjde();
							bBorrado = recCtrl.borrarAsientodeDiario(cn, recibojde.getId().getRecjde(),
																recibojde.getId().getNobatch());
							if(!bBorrado)
								break;
							
						}		
					}
				}else{
					//buscar enlace
					for (int k = 0; k < lstRecibojde.size(); k++){
						recibojde = (Recibojde)lstRecibojde.get(k);
						if(recibojde.getId().getNumrec() == iNumrec && recibojde.getId().getTipodoc().equals("R")){
							iBatchRC = recibojde.getId().getNobatch();
							break;
						}
					}
					//leer montos aplicados en el recibo
					dMontos = recCtrl.getMontoAplicadoF0311(cn, 
										hFac.getId().getNofactura(), 
										hFac.getId().getTipofactura(), 
										recibojde.getId().getRecjde(), 
										hFac.getId().getPartida(),
										hFac.getId().getCodcli());
					//actualizar factura
					// si el problema de centavos persiste aplicar pasarEnteroADouble() sobre el monto
					bBorrado = recCtrl.aplicarMontoF0311(cn, "A",
										divisas.roundDouble((hFac.getId().getCpendiente()/100.0) + divisas.roundDouble(dMontos[0])),
										(hFac.getId().getDpendiente()/100.0) + divisas.roundDouble(dMontos[1]), 
										vAut[0].getId().getLogin(), 
										"000"+hFac.getId().getCodsuc(), 
										hFac.getId().getTipofactura(), 
										hFac.getId().getNofactura(), 
										hFac.getId().getPartida(),
										hFac.getId().getCodcli());
					if(!bBorrado)
						break;
					
					//&& ========== borrar RC y RG
					bBorrado = recCtrl.borrarRC(cn, hFac.getId().getNofactura(), 
										hFac.getId().getTipofactura(), 
										recibojde.getId().getRecjde(), 
										hFac.getId().getPartida(),
										hFac.getId().getCodcli());
					if(!bBorrado)
						break;
					
					//&& ========== DETERMINAR SI HAY ASIENTOS EN EL RECIBO Y BORRAR
					for (int k = 0; k < lstRecibojde.size(); k++){
						recibojde = (Recibojde)lstRecibojde.get(k);
						if(recibojde.getId().getNumrec() == iNumrec && recibojde.getId().getTipodoc().equals("A")){
							bBorrado = recCtrl.borrarAsientodeDiario(cn, 
												recibojde.getId().getRecjde(), 
												recibojde.getId().getNobatch());
							if(!bBorrado)
								break;
							
						}		
					}
				}
				//borrar enlace entre recibo y factura
				bBorrado = recCtrl.actualizarEnlaceReciboFac(cn,hFac,iNumrec,iCaid);
				if(!bBorrado)
					break;
				
			}
			
			//7777777777777777777777777777777777777777777777777777777777777777
			
			//&& =========== Borrar batch
			if(bBorrado){
				bBorrado = recCtrl.borrarBatch(cn, recibojde.getId().getNobatch(),"R");
				if(iBatchAsiento > 0)
					bBorrado = recCtrl.borrarBatch(cn, iBatchAsiento,"G");
			}
			
			//&& ========== Anulacion de FCV
			if(iNoFCV > 0 ){
				Recibo ficha = recCtrl.obtenerFichaCV(iNoFCV, iCaid, sCodcomp, sCodsuc);
				lstRecibojde = recCtrl.getEnlaceReciboJDE(iCaid, sCodsuc, 
									ficha.getId().getCodcomp(),
									ficha.getId().getNumrec(),
									ficha.getId().getTiporec());
				
				bHecho = recCtrl.actualizarEstadoRecibo(null, null, 
										ficha.getId().getNumrec(),
										ficha.getId().getCaid(),
										ficha.getId().getCodsuc(),
										ficha.getId().getCodcomp(), 
										vAut[0].getId().getCoduser(), 
										"Error de aplicacion Socket POS",
										ficha.getId().getTiporec());
				
				recibojde = null;
				for(Recibojde rj: lstRecibojde){
					recCtrl.borrarAsientodeDiario(cn, rj.getId().getRecjde(), rj.getId().getNobatch());
					recibojde = rj;
				}
				if(recibojde != null)
					recCtrl.borrarBatch(cn, recibojde.getId().getNobatch(),"G");
			} 
			//&& ======= Borrar los sobrantes.
			recibojde = null;
			lstRecibojde = recCtrl.getEnlaceReciboJDE(iCaid, sCodsuc, sCodcomp, iNumrec, "SBR");
			
			for (Recibojde rj : lstRecibojde) {
				recCtrl.borrarAsientodeDiario(cn, rj.getId().getRecjde(), rj.getId().getNobatch());
				recibojde = rj;
			}
			if(recibojde != null)
				recCtrl.borrarBatch(cn, recibojde.getId().getNobatch(),"G");
			
			if(bHecho)
				cn.commit();
			else
				cn.rollback();
			
		}catch(Exception ex){
			bHecho = false;
			ex.printStackTrace();
		}finally{
//			try {cn.close(); } catch (Exception e) {}
		}
		return bHecho;
	}
	
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
				if(mPago.getMetodo().equals(MetodosPagoCtrl.TARJETA ) && mPago.getVmanual().equals("2")){	
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
		MetodosPago rd = null;
		List lstResult = null;
		PosCtrl p = new PosCtrl();
		
		try{
			
			for (int i = 0; i < lstMetodosPago.size(); i++){
				rd = (MetodosPago)lstMetodosPago.get(i);

				if(rd.getMetodo().compareTo(MetodosPagoCtrl.TARJETA ) != 0 || rd.getVmanual().compareTo("2") != 0)
					continue;

				lstResult = p.anularTransaccionPOS(rd.getTerminal(), rd.getReferencia4(), 
												rd.getReferencia5(), rd.getReferencia7());
				
				if(!lstResult.get(0).equals("00"))
					bPago = false;
				
			}
		}catch(Exception ex){
			bPago = false;
			ex.printStackTrace();
		}
		return bPago;
	}		
/*************************************************************************************************************************/	
	public boolean aplicarPagoSocketPos(List lstPagos, List lstDatosTrack){
		boolean bHecho = true,bPasa = false;
		MetodosPago mPago = null;
		Divisas d = new Divisas();
		PosCtrl p = new PosCtrl();
		List<String> lstDatos  = new ArrayList<String>(), lstRespuesta = new ArrayList<String>();
		String sTerminal = "",sMonto = "", sNotarjeta = "", sFechavence = "";
		int j = 0;
		String nombre = "";
		List<MetodosPago> lstPagosAplicados = new ArrayList<MetodosPago>();
		
		try{
			//comprobar si hay pagos con tarjeta de credito
			for(int i = 0;i < lstPagos.size();i++){
				mPago = (MetodosPago)lstPagos.get(i);
				if(mPago.getMetodo().equals(MetodosPagoCtrl.TARJETA ) && mPago.getVmanual().equals("2")){
					//leer terminal por afiliado caja
					//solicitar autorizacion a credomatic
					sMonto = d.roundDouble(mPago.getMonto()) + "";
					sTerminal = mPago.getTerminal();
					
					//automatica
					if(!mPago.getTrack().equals("")){
						lstDatos = (List)lstDatosTrack.get(j);
						lstRespuesta = p.realizarPago(sTerminal, sMonto, mPago.getTrack(), lstDatos);
						sNotarjeta = lstDatos.get(1);
						
//						if(lstDatos.size()==7)nombre = lstDatos.get(2);else nombre = lstDatos.get(2) + " " + lstDatos.get(3);
						
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
						
						
						sNotarjeta = (sNotarjeta).substring(sNotarjeta.length()-4, sNotarjeta.length());//4 ult. digitos de tarjeta
						
						//&& ===== Anular todos los pagos que si se aplicaron en el recibo.
						if(lstRespuesta.get(0).compareTo("00") != 0 && lstRespuesta.get(0).compareTo("08") != 0)
							anularPagosSP(lstPagosAplicados);
						
						if(lstRespuesta.get(0).equals("00") || lstRespuesta.get(0).equals("08")){//aprobada
							//poner datos de respuesta a metodo de pago para luego ser grabados
							//sNotarjeta = (sNotarjeta).substring(sNotarjeta.length()-4, sNotarjeta.length());//4 ult. digitos de tarjeta
							
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
							
							lstPagosAplicados.add(mPago);
							
							j++;
							
						}
						else if (lstRespuesta.get(0).toString().equals("error")){

							lblMensajeValidacion.setValue("Tarjeta No.: " + sNotarjeta 
									+ " no aprobada, \n  Respuesta de banco: " 
									+ lstRespuesta.get(1) + "\n"
									+ lstRespuesta.get(2)+"\n" 
									+ lstRespuesta.get(3));
							lstDatos.add("false");
							m.put("lstDatosTrack_Con", lstDatosTrack);
							j++;
							return false;
							
						}else{
							lblMensajeValidacion.setValue("Tarjeta No.: " + sNotarjeta 
									+ " no aprobada, \nRespuesta de banco: " 
									+ lstRespuesta.get(1) );
							lstDatos.add("false");
							m.put("lstDatosTrack_Con", lstDatosTrack);
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
/***********************************************************************************************************************/	
	public String validarPagoSocket(String sMonedaPago,double dMontoAplicar,Credhdr hFac,boolean usaManual){
		boolean validado = false;
		String sMensajeError = "";
		double monto = 0;
		String sMonto = "";
		Pattern pNumero = null;
		Divisas d = new Divisas();
		int y = 158;
		CtrlPoliticas polCtrl = new CtrlPoliticas();
		double dEquivalente=0;
		Divisas divisas = new Divisas();
		try{	
			String sCajaId = (String) m.get("sCajaId");
			Pattern pAlfa = Pattern.compile("^[A-Za-z0-9-\\p{Blank}]+$");
			pNumero = Pattern.compile("^[0-9]+\\.?[0-9]*$");
			Pattern pAlfaRef1 = Pattern.compile("^[A-Za-z0-9-]+$");
			String ref1 = getTxtReferencia1().getValue().toString().trim();
			String ref2 = getTxtReferencia2().getValue().toString().trim();
			String ref3 = getTxtReferencia3().getValue().toString().trim();
			String ref4 = "";
			// expresion regular valores alfanumericos
			Matcher matAlfa1 = null;
			Matcher matAlfa2 = null;
			Matcher matAlfa3 = null;
			Matcher matAlfa4 = null;
			if (txtMonto.getValue() != null) {
				sMonto = txtMonto.getValue().toString();
							
				matAlfa1 = pAlfaRef1.matcher(ref1);
				matAlfa2 = pAlfa.matcher(ref2);
				matAlfa3 = pAlfa.matcher(ref3);
				matAlfa4 = pAlfa.matcher(ref4);
			}
			// expresion regular solo numeros
			Matcher matNumero = null;
			Matcher matVoucher = null;
			if (txtMonto.getValue() != null) {
				sMonto = txtMonto.getValue().toString();
				
				matNumero = pNumero.matcher(sMonto);
			}
			if (!sMonto.equals("") && matNumero.matches()) {
				monto = d.formatStringToDouble(getTxtMonto().getValue().toString());
			}
			
			if (sMonto.equals("")) {
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
			}else if (ref1.equals("")) {//valida identificacion del cliente
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
			}
			else if (!polCtrl.validarMonto(Integer.parseInt(sCajaId),hFac.getId().getCodcomp(), cmbMoneda.getValue().toString(),cmbMetodosPago.getValue().toString(), monto)) {
					validado = false;
					lblMensajeAutorizacion.setValue("El monto ingresado no cumple con las politicas de caja");
					dwSolicitud.setWindowState("normal");
					Date dFechaActual = new Date();
					txtFecha.setValue(dFechaActual);
					if (!txtMonto.getValue().toString().trim().equals("")) {
						sMonto = txtMonto.getValue().toString();
					}
					monto = Double.parseDouble(sMonto);
					m.put("montoIsertando", monto);
					m.put("monto", monto);
			} else if(selectedMet.size() > 0){
				List<MetodosPago> lstPagos = (ArrayList<MetodosPago>)selectedMet;
				for (MetodosPago pago : lstPagos) {
					if(pago.getMoneda().equals(sMonedaPago))
						monto += pago.getMonto();
					
					dEquivalente += (hFac.getMoneda().equals(pago.getMoneda()))?
									 pago.getMonto():
								     pago.getEquivalente();
				}
				dEquivalente = divisas.roundDouble(dEquivalente);
			
			if (!polCtrl.validarMonto(Integer.parseInt(sCajaId), hFac.getId().getCodcomp(), sMonedaPago, MetodosPagoCtrl.TARJETA ,monto)){
				validado = false;
				lblMensajeAutorizacion.setValue("El consolidado de los montos no cumple con las politicas de caja");
				dwSolicitud.setWindowState("normal");
				Date dFechaActual = new Date();
				txtFecha.setValue(dFechaActual);
				m.put("monto", monto);
				if(!txtMonto.getValue().toString().trim().equals("")){
					sMonto = txtMonto.getValue().toString();
				}
				monto = Double.parseDouble(sMonto);
				m.put("montoIsertando", monto);
			}
			//validar consolidado de montos
			 else if (dEquivalente > dMontoAplicar) {
					validado = false;
					txtMonto.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El Consolidado de montos ingresados para este método de pago debe ser menor o igual al monto a aplicar<br>";
					dwProcesa.setWindowState("normal");
					y = y + 7;
			}
		}
				
			if(!chkIngresoManual.isChecked()){				
				String sTrack = track.getValue().toString();
				List lstDatosTrack = null,lstDatosTrack2 = null;
				if(sTrack.equals("")){//validar q pase la tarjeta en el lector
						validado = false;
						track.setStyleClass("frmInput2Error");
						sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> No se detectó la información de la tarjeta; Debe deslizar primero la tarjeta por el lector<br>";
						dwProcesa.setWindowState("normal");
				}else{//validar que la lectura fue correcta
					lstDatosTrack = d.obtenerDatosTrack(sTrack);
					if(lstDatosTrack.size()<6){
						validado = false;
						track.setStyleClass("frmInput2Error");
						sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> No se leyó correctamente la información de la tarjeta!!!<br>";
						dwProcesa.setWindowState("normal");
					}else if (lstDatosTrack.get(1) == null){
						validado = false;
						track.setStyleClass("frmInput2Error");
						sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> No se leyó correctamente el numero de tarjeta!!!<br>";
						dwProcesa.setWindowState("normal");
					}else if(lstDatosTrack.get(4) == null){
						validado = false;
						track.setStyleClass("frmInput2Error");
						sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> No se leyó correctamente la fecha de vencimiento de la tarjeta!!!<br>";
						dwProcesa.setWindowState("normal");
					}
				}
			}else{///
				ref2 = txtNoTarjeta.getValue().toString().trim();
				matAlfa2 = pNumero.matcher(ref2);
				ref3 = txtFechaVenceT.getValue().toString().trim();
				//matAlfa3 = pAlfa.matcher(ref3);
				matNumero = pNumero.matcher(ref3);
				if (ref2.equals("")) {//numero de tarjeta
					validado = false;
					txtNoTarjeta.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Número de Tarjeta requerido<br>";
					dwProcesa.setWindowState("normal");
					y = y + 5;
				}else if(ref2.length() > 16){
					validado = false;
					txtNoTarjeta.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Longitud muy larga (max. 16)<br>";
					dwProcesa.setWindowState("normal");
					y = y + 5;
				}else if(!matAlfa2.matches()){
					validado = false;
					txtNoTarjeta.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" />El Número de tarjeta contiene caracteres invalidos<br>";
					dwProcesa.setWindowState("normal");
					y = y + 5;
				}else if(ref3.equals("")){
					validado = false;
					txtFechaVenceT.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Fecha de vencimiento requerida<br>";
					dwProcesa.setWindowState("normal");
					y = y + 5;
				}else if(ref3.length() > 4){
					validado = false;
					txtFechaVenceT.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Longitud muy larga (max. 4)<br>";
					dwProcesa.setWindowState("normal");
					y = y + 5;
				}else if(!matNumero.matches()){
					validado = false;
					txtFechaVenceT.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> La fecha debe tener el formato MMYY (mes y año)<br>";
					dwProcesa.setWindowState("normal");
					y = y + 5;
				}
			}///f
			lblMensajeValidacion.setValue(sMensajeError);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return sMensajeError;
	}
/********************************************************************************************************************/	
	public List getLstAfiliados(int iCaid, String sCodcomp, String sLineaFactura,String sMoneda){
		List Afiliados = null;
		List<SelectItem>lstPOS = new ArrayList<SelectItem>();
		Cafiliados[] cafiliado = null;
		AfiliadoCtrl afCtrl = new AfiliadoCtrl();
		String sDescrip="";
		
		try {
			
			cafiliado = afCtrl.obtenerAfiliadoxCaja_Compania(iCaid, sCodcomp,sLineaFactura, sMoneda);
			lstPOS.add(new SelectItem("01", "--Afiliados--"));
			
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
			 Afiliados = lstPOS;
		} catch (Exception error) {
			Afiliados = new ArrayList();
			error.printStackTrace(); 
		}
		return Afiliados;
	} 
/***********************************************************************************************************/	
	public List getLstAfiliadosSocketPOS(int iCaid, String sCodcomp, String sLineaFactura,String sMoneda){
		List Afiliados = null;
		List<SelectItem>lstPOS = new ArrayList<SelectItem>();
		Cafiliados[] cafiliado = null;
		AfiliadoCtrl afCtrl = new AfiliadoCtrl();
		String sDescrip="";
		List<Cafiliados> lstCafiliados = null;
		try {
			
			cafiliado = afCtrl.obtenerAfiliadoxCaja_Compania(iCaid, sCodcomp,sLineaFactura, sMoneda);
			if(cafiliado != null){
				lstCafiliados = afCtrl.getTerminalSocketPos(cafiliado);
				if(!lstCafiliados.isEmpty()){
					//buscar afiliados en el banco
					lstPOS.add(new SelectItem("01", "--Afiliados--"));
					
					for (Iterator iterator = lstCafiliados.iterator(); iterator.hasNext();) {
						    Cafiliados ca = (Cafiliados) iterator.next();
							SelectItem si = new SelectItem();
							si.setValue(ca.getId().getCxcafi().trim() + "," + ca.getTermid());
							si.setLabel(ca.getId().getCxdcafi().trim());
							sDescrip = ca.getId().getCxcafi().trim()+", "+ ca.getId().getCxdcafi().trim();
							sDescrip += ", LN:";
							sDescrip +=	(String.valueOf(ca.getId().getC6rp07()).trim().equalsIgnoreCase(""))?
										"S/L": ca.getId().getC6rp07().trim(); 
							sDescrip += "," + ca.getTermid();
							si.setDescription(sDescrip);
							lstPOS.add(si);
					 }
					 Afiliados = lstPOS;
				}
			}
		} catch (Exception error) {
			Afiliados = new ArrayList();
			error.printStackTrace();
		}
		return Afiliados;
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
	
	public  boolean batchExcedentesEnPago(Session session, String monedaFactura,String companiaFactura, String codunineg, int numrec, int caid, 
			String codcomp, String codsuc, MetodosPago mpago, BigDecimal monto, BigDecimal tasaOficial,
			String usuario, int codigousuario, Date fecharecibo, boolean diferencialCambiario, String monedaLocal){
		
		boolean aplicado = true;
		String companiaComprobante="00000";
		
		try {
			
			int numeroBatchJde = Divisas.numeroSiguienteJdeE1(  );
			int numeroReciboJde = Divisas.numeroSiguienteJdeE1Custom(valoresJdeNumeracion[8],valoresJdeNumeracion[9]);
			
			String concepto = "Sobrante ";
			String[] fcvCuentaGanacia = DocumuentosTransaccionales.obtenerCuentasFCVGanancia(codcomp).split(",");
			String strCuentaObj = fcvCuentaGanacia[0];
			String strCuentaSub = fcvCuentaGanacia[1] ;
			
			if( monedaFactura.compareTo(mpago.getMoneda()) != 0 || diferencialCambiario){
				String[] fcvCuentaPerdida = DocumuentosTransaccionales.obtenerCuentasFCVPerdida(codcomp).split(",");
				strCuentaObj = fcvCuentaPerdida[0]; strCuentaSub = fcvCuentaPerdida[1] ;
			}
			 
			concepto = "Recibo "+numrec +" caja " + caid;
			
			Vf0901 cuentaSobrante = new Divisas().validarCuentaF0901(codunineg, strCuentaObj, strCuentaSub);
			List<String[]> cuentaCaja = Divisas.cuentasFormasPago( Arrays.asList(new MetodosPago[]{mpago} ), caid, codcomp); 
			
			companiaComprobante = cuentaSobrante.getId().getGmco();
			

			List<DatosComprobanteF0911> lineasComprobante = new ArrayList<DatosComprobanteF0911>();
			 DatosComprobanteF0911 dtCtaSobrante = new DatosComprobanteF0911(
					 cuentaSobrante.getId().getGmaid(),
					 monto.negate(), 
					 concepto,
					 tasaOficial,
					 "",
					 "",
					 companiaComprobante );
			 lineasComprobante.add(dtCtaSobrante);
			 
			 DatosComprobanteF0911 dtaFormaPago = new DatosComprobanteF0911(
					 cuentaCaja.get(0)[1],
					 monto, 
					 concepto,
					 tasaOficial,
					 "",
					 "",
					 companiaComprobante );
			 lineasComprobante.add(dtaFormaPago);
			
			//++++++++++++++++++++++++++++++++++++++++++++++++++
			
			
			 new ProcesarEntradaDeDiarioCustom();
			 ProcesarEntradaDeDiarioCustom.procesarSql = false;
			 ProcesarEntradaDeDiarioCustom.monedaComprobante = mpago.getMoneda();
			 ProcesarEntradaDeDiarioCustom.monedaLocal = monedaLocal;
			 ProcesarEntradaDeDiarioCustom.fecharecibo = fecharecibo;
			 ProcesarEntradaDeDiarioCustom.tasaCambioParalela = tasaOficial; 
			 ProcesarEntradaDeDiarioCustom.tasaCambioOficial = tasaOficial; 
			 ProcesarEntradaDeDiarioCustom.montoComprobante = monto;
			 ProcesarEntradaDeDiarioCustom.tipoDocumento = valoresJdeInsContado[1];
			 ProcesarEntradaDeDiarioCustom.conceptoComprobante = " Sobrante de pago en caja ";
			 ProcesarEntradaDeDiarioCustom.numeroBatchJde = String.valueOf( numeroBatchJde );
			 ProcesarEntradaDeDiarioCustom.numeroReciboJde = String.valueOf( numeroReciboJde ) ;
			 ProcesarEntradaDeDiarioCustom.usuario = usuario;
			 ProcesarEntradaDeDiarioCustom.codigousuario = codigousuario;
			 ProcesarEntradaDeDiarioCustom.programaActualiza = PropertiesSystem.CONTEXT_NAME;
			 ProcesarEntradaDeDiarioCustom.lineasComprobante = lineasComprobante;
			 ProcesarEntradaDeDiarioCustom.valoresJdeInsContado = valoresJdeInsContado; 
			 ProcesarEntradaDeDiarioCustom.procesarEntradaDeDiarioCustom(session);
			 
			 aplicado = ProcesarEntradaDeDiarioCustom.msgProceso.isEmpty();
			 
			 if(!aplicado){
				 return aplicado;
			 }
			 
			 List<String[]> querysToExecute = ProcesarEntradaDeDiarioCustom.lstSqlsInserts ;
			 if( querysToExecute == null || querysToExecute.isEmpty() ){
				 return aplicado = false;
			 }
			 
			 for (String[] querys : querysToExecute ) {
					 
					try {
						LogCajaService.CreateLog("batchExcedentesEnPago", "QRY", querys[0]);
						
						int rows = session.createSQLQuery(querys[0]).executeUpdate() ;
		
					} catch (Exception e) {
						LogCajaService.CreateLog("batchExcedentesEnPago", "ERR", e.getMessage());
						e.printStackTrace(); 
						return aplicado = false;
					}
				}
			 
			 aplicado = com.casapellas.controles.ReciboCtrl.crearRegistroReciboCajaJde(
					 	session, caid, numrec, codcomp, codsuc,  "SBR", "A", 
					 	numeroBatchJde, Arrays.asList(new String[]{String.valueOf(numeroReciboJde)}));
			 
			 if(!aplicado){
				return aplicado = false;
			 }
			 
		} catch (Exception e) {
			aplicado = false;
			e.printStackTrace();
		}
		return aplicado;
	}
	
	
	
/***********************************************************************************************************/	
/******************************************************************************************/
/** Método: Guardar asientos de diario para sobrantes de caja.
 *	Fecha:  8/07/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	@SuppressWarnings("unchecked")
	public boolean registarAsientosxSobrantes(Session s,Transaction tx,Connection cn,MetodosPago mpago,
											  Vautoriz vaut, Vf55ca01 f55, int iNumrec, String sCodcomp,
											  String sCodunineg, double dSobrante, String sMonedaBase,Credhdr Factura){
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
			sConcepto = "Dif/Camb en Rc:" +iNumrec+ " Ca:"+f55.getId().getCaid();
			sTipoCliente = emCtrl.determinarTipoCliente(f55.getId().getCaan8());	
			
			//--- Validar la cuenta de caja para el método de pago con sobrante.
			sCuentaCaja = dv.obtenerCuentaCaja(f55.getId().getCaid(),sCodcomp, mpago.getMetodo(), mpago.getMoneda(),s,tx,null,null);
			if(sCuentaCaja==null){
				sMensajeError="No se ha podido leer la cuenta de caja para el método: " +mpago.getMetododescrip().trim();
				m.put("sMsgErrorSobrante", sMensajeError);
				return false;
			}
			
			//&& ==== Validar la cuenta a utilizar, en dependencia del tipo de transaccion.
			//&& ==== UN.66000.01: Diferencial Cambiario, UN.65100.10: Sobrante de Caja.
			
			String[] fcvCuentaPerdia = DocumuentosTransaccionales.obtenerCuentasFCVPerdida(f55.getId().getCaco()).split(",");
			sCtaOb = fcvCuentaPerdia[0];
			sCtaSub= fcvCuentaPerdia[1];
			
			//&& === Condicion de seleccion tipo sobrante/diferencia para casos TRADER.
			if(Factura.getId().getCodcomp().trim().equals("E03") ){
				if( m.get("scr_SbrDfr")!=null && chkSobranteDifrl.isChecked()){
					sTipo = "SBRTE";
					sConcepto = "Sobrante en Rc:" +iNumrec+ " Ca:"+f55.getId().getCaid();
					String[] fcvCuentaGanacia = DocumuentosTransaccionales.obtenerCuentasFCVGanancia(f55.getId().getCaco()).split(",");
					sCtaOb = fcvCuentaGanacia[0];
					sCtaSub= fcvCuentaGanacia[1];
				}
			}else{
				MetodosPago mpSobrante = (MetodosPago)m.get("scr_MpagoSobrante");
				if(Factura.getMoneda().equals(mpSobrante.getMoneda())){
					sTipo = "SBRTE";
					sConcepto = "Sobrante en Rc:" +iNumrec+ " Ca:"+f55.getId().getCaid();
					String[] fcvCuentaGanacia = DocumuentosTransaccionales.obtenerCuentasFCVGanancia(f55.getId().getCaco()).split(",");
					sCtaOb = fcvCuentaGanacia[0];
					sCtaSub= fcvCuentaGanacia[1];
				}
			}
			vCtaDI  = dv.validarCuentaF0901(sCodunineg,sCtaOb,sCtaSub);
			if(vCtaDI==null){
				sMensajeError = "No se ha podido obtener la cuenta Diversos Ingresos: '"+sCodunineg+"."+sCtaOb+"'."+sCtaSub;
				m.put("sMsgErrorSobrante", sMensajeError);
				return false;
			}else{
				
				sCoCuentaDI = vCtaDI.getId().getGmco().trim();
												
				sCuentaDI = sCodunineg+"."+sCtaOb+"."+sCtaSub;				
			}
			sAsientoSuc  = sCuentaCaja[2];
			
			//--- número de batch y de documento.
			iNobatchNodoc = dv.obtenerNobatchNodoco();
			if(iNobatchNodoc==null){
				sMensajeError = "No se ha podido obtener número de batch y documento para registro de Sobrante de Pagos ";
				m.put("sMsgErrorSobrante", sMensajeError);
				return false;
			}
			iMonto = dv.pasarAentero(dSobrante);
		
			//---- Guardar el batch.
			bHecho = rcCtrl.registrarBatchA92(cn,valoresJdeInsContado[8], iNobatchNodoc[0],iMonto, vaut.getId().getLogin(), 1, MetodosPagoCtrl.DEPOSITO);
			if(bHecho){
				
				//---- Registro en córdobas.
				if(mpago.getMoneda().equals(sMonedaBase)){
				
					bHecho = rcCtrl.registrarAsientoDiario(dtFecha, cn, sAsientoSuc, valoresJdeInsContado[1], iNobatchNodoc[1], 1.0,
								iNobatchNodoc[0], sCuentaCaja[0], sCuentaCaja[1], 
								sCuentaCaja[3], sCuentaCaja[4], sCuentaCaja[5],
								valoresJdeInsContado[5], mpago.getMoneda(), iMonto, 
								sConcepto, vaut.getId().getLogin(), vaut.getId().getCodapp(), 
								BigDecimal.ZERO, sTipoCliente,"DBTO X "+sTipo+" PAGO CTA CA: "+mpago.getMetodo()+" ",
								sCuentaCaja[2], "", "", mpago.getMoneda(),sCuentaCaja[2],valoresJdeInsContado[7]);
					if(bHecho){
						bHecho = rcCtrl.registrarAsientoDiario(dtFecha, cn, sAsientoSuc, valoresJdeInsContado[1], iNobatchNodoc[1], 2.0,
									iNobatchNodoc[0], sCuentaDI,	vCtaDI.getId().getGmaid(), 
									vCtaDI.getId().getGmmcu().trim(), vCtaDI.getId().getGmobj().trim(), 
									vCtaDI.getId().getGmsub().trim(), valoresJdeInsContado[5],	mpago.getMoneda(),  iMonto*-1,
									sConcepto, vaut.getId().getLogin(), vaut.getId().getCodapp(), 
									BigDecimal.ZERO, sTipoCliente,"CRDTO X "+sTipo+" CTA ",
									sCoCuentaDI, "", "",mpago.getMoneda(),sCoCuentaDI,valoresJdeInsContado[7]);
					
						if(!bHecho){
							sMensajeError = "No se ha podido registrar  línea 2.0 de asientos para registro de Sobrante de Pagos ";
						}
					}else{
						sMensajeError = "No se ha podido registrar  línea 1.0 de asientos para registro de Sobrante de Pagos ";
					}
				}
				//--- Registro en dólares.
				else{
					
					/*TasaCambioCtrl tcCtrl = new  TasaCambioCtrl();
					dTasaJDE = tcCtrl.obtenerTasaJDExFecha(mpago.getMoneda(), "COR",dtFecha, null, null);
					dTasaJDE = dv.roundDouble(dTasaJDE);
					iMontoDom = (int)( dv.roundDouble(( dSobrante * dTasaJDE ) * 100) ); */
					
					BigDecimal bdTasaOficial = TasaCambioCtrl.obtenerTasaOficialPorFecha(dtFecha, mpago.getMoneda(), sMonedaBase) ;
					
					iMontoDom =	Integer.parseInt( String.format("%1$.2f", bdTasaOficial.multiply(
									new BigDecimal( Double.toString( dSobrante ) ) ) ).replace(".", "") ); 
					
					bHecho = rcCtrl.registrarAsientoDiario(dtFecha, cn, sAsientoSuc, valoresJdeInsContado[1], iNobatchNodoc[1], 1.0,
									iNobatchNodoc[0], sCuentaCaja[0], sCuentaCaja[1], 
									sCuentaCaja[3], sCuentaCaja[4], sCuentaCaja[5],
									valoresJdeInsContado[5], mpago.getMoneda(), iMontoDom, 
									sConcepto, vaut.getId().getLogin(), vaut.getId().getCodapp(), 
									bdTasaOficial, sTipoCliente,"DBTO CTA CA:"+mpago.getMetodo()+" "+sTipo+" PAGO",
									sCuentaCaja[2], "", "", mpago.getMoneda(),sCuentaCaja[2],valoresJdeInsContado[4]);
					if(bHecho){
						bHecho = rcCtrl.registrarAsientoDiario(dtFecha, cn, sAsientoSuc, valoresJdeInsContado[1], iNobatchNodoc[1], 1.0,
										iNobatchNodoc[0], sCuentaCaja[0], sCuentaCaja[1], 
										sCuentaCaja[3], sCuentaCaja[4], sCuentaCaja[5],
										valoresJdeInsContado[2], mpago.getMoneda(), iMonto, 
										sConcepto, vaut.getId().getLogin(), vaut.getId().getCodapp(), 
										BigDecimal.ZERO, sTipoCliente,"DBTO CTA CA:"+mpago.getMetodo()+" "+sTipo+" PAGO",
										sCuentaCaja[2], "", "",mpago.getMoneda(),sCuentaCaja[2],valoresJdeInsContado[4]);
						
						if(bHecho){
							bHecho = rcCtrl.registrarAsientoDiario(dtFecha, cn, sAsientoSuc, valoresJdeInsContado[1], iNobatchNodoc[1], 2.0,
											iNobatchNodoc[0], sCuentaDI,	vCtaDI.getId().getGmaid(), 
											vCtaDI.getId().getGmmcu().trim(), vCtaDI.getId().getGmobj().trim(), 
											vCtaDI.getId().getGmsub().trim(), valoresJdeInsContado[5],	mpago.getMoneda(), (iMontoDom*-1), 
											sConcepto, vaut.getId().getLogin(), vaut.getId().getCodapp(), 
											bdTasaOficial, sTipoCliente,"CRDTO CTA  X "+sTipo+" PAGO",
											sCoCuentaDI, "", "",mpago.getMoneda(),sCoCuentaDI,valoresJdeInsContado[4]);
							
							if(bHecho){
								bHecho = rcCtrl.registrarAsientoDiario(dtFecha, cn, sAsientoSuc, valoresJdeInsContado[1], iNobatchNodoc[1], 2.0,
													iNobatchNodoc[0], sCuentaDI,	vCtaDI.getId().getGmaid(), 
													vCtaDI.getId().getGmmcu().trim(), vCtaDI.getId().getGmobj().trim(), 
													vCtaDI.getId().getGmsub().trim(), valoresJdeInsContado[2],	mpago.getMoneda(), (iMonto*-1), 
													sConcepto, vaut.getId().getLogin(), vaut.getId().getCodapp(), 
													BigDecimal.ZERO, sTipoCliente,"CRDTO CTA  X "+sTipo+" PAGO",
													sCoCuentaDI, "", "", mpago.getMoneda(),sCoCuentaDI,valoresJdeInsContado[4]);
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
				bHecho = rcCtrl.fillEnlaceMcajaJde(s, null,iNumrec, sCodcomp, iNobatchNodoc[1], 
														iNobatchNodoc[0], f55.getId().getCaid(), 
														f55.getId().getCaco(), "A","SBR");
				if(!bHecho){
					sMensajeError = "No se ha podido guardar registro de Excedente en RECIBOJDE ";
				}
				
			}else{
				sMensajeError = "No se ha podido registrar  batch  para registro de Excedente de Pagos ";
			}
			
			if(!bHecho)
				m.put("sMsgErrorSobrante", sMensajeError);
			
		} catch (Exception error) {
			bHecho = false;
			m.put("sMsgErrorSobrante", "Error de sistema al intentar registrar asiento por sobrantes de pago");
			error.printStackTrace();
		} 
		return bHecho; 
	}
/*************************************************************************************************/
	public void agregarFacturasRecibo(ActionEvent e){
		List lstAgregarFactura,lstSelection;
		ArrayList<Credhdr>lstFacturaSelected;
		Credhdr Hsel = null;
		RowItem ri = null;
		String sMonedaBase = "";
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		FacturaCreditoCtrl fCtrl = new FacturaCreditoCtrl();
		
		try{
			lstAgregarFactura = (List)m.get("lstAgregarFactura");
			lstFacturaSelected = (ArrayList<Credhdr>)m.get("selectedFacsCredito");
			
			lstSelection = gvAgregarFactura.getSelectedRows();		
			
			for(int a = 0; a < lstSelection.size();a++){
				ri = (RowItem)lstSelection.get(a);
				Hsel = (Credhdr)gvAgregarFactura.getDataRow(ri);
				lstFacturaSelected.add(Hsel);
			}
	
			int numFacturaDesde = (  CodeUtil.getFromSessionMap("cr_AgregarFacturasNumeroDesde") == null  ) ? 0 : 
						Integer.parseInt(String.valueOf( CodeUtil.getFromSessionMap("cr_AgregarFacturasNumeroDesde") ));
			int numFacturaHasta = (  CodeUtil.getFromSessionMap("cr_AgregarFacturasNumeroHasta") == null  ) ? 0 : 
					Integer.parseInt(String.valueOf( CodeUtil.getFromSessionMap("cr_AgregarFacturasNumeroHasta") ));
			
		
			intSelectedDet.setValue(lstFacturaSelected.size());
			F55ca014[] f14 = (F55ca014[])m.get("cont_f55ca014");
			
			lstAgregarFactura.clear();
			lstAgregarFactura = fCtrl.buscarFacturasCredito(Hsel.getId().getCodcli(),Hsel.getId().getMoneda(),Hsel.getId().getCodcomp(), 
					numFacturaDesde, numFacturaHasta, (ArrayList)lstFacturaSelected, f14);
			
			m.put("lstAgregarFactura",lstAgregarFactura);
			gvAgregarFactura.dataBind();
			gvfacturasSelecCredito.dataBind();
			List lstSelectedMet = (List)m.get("lstPagos");
			
			//obtener companias x caja
			sMonedaBase = cCtrl.sacarMonedaBase(f14, Hsel.getId().getCodcomp());
			//--------------------------------------------------
			//calcular el monto recibido en dependencia de moneda de factura y moneda de pago
			double montoRecibido = calcularElMontoRecibido(lstSelectedMet, Hsel, sMonedaBase);

			//determinar como dar el cambio
			determinarCambio(lstSelectedMet, Hsel, montoRecibido,sMonedaBase);
			
			//distribuir el monto recibido a los montos a aplicar en factura
			if(m.get("dMontoAplicarCre") == null){
				distribuirMontoAplicar(sMonedaBase, lstFacturaSelected);
			}
			m.remove("dMontoAplicarCre");
			
			calcularTotales(lstFacturaSelected,sMonedaBase);
			
		}catch(Exception ex){
			LogCajaService.CreateLog("agregarFacturasRecibo", "ERR", ex.getMessage()); 
		}
	}
/*************************************************************************************************/
	
/******************************************************************************************/
	public void establecerPagoExonerado(ActionEvent ev){
		List<Credhdr> lstFacturasSelected = null;
		Credhdr fh = null;
		try{
			lstFacturasSelected = (List<Credhdr>)m.get("selectedFacsCredito");
			for(int i = 0; i < lstFacturasSelected.size(); i ++){
				fh = lstFacturasSelected.get(i);
			}
		}catch(Exception ex){
			ex.printStackTrace(); 
		}
	}
/******************************************************************************************/
/** Método: Establecer los valores para tasa paralela y tasa oficial al dia actual.
 *	Fecha:  24/06/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public void cargarTasasCambioAldia(){
		TasaCambioCtrl tcCtrl = new TasaCambioCtrl();
		Tpararela[] tpcambio = null;
		Tcambio[] tcambio = null;
		String sTasaCamP="", sTasaCamOf="";
		
		try {
			//---- Remover el valor para el motivo de camibio de tasa.
			m.remove("fcr_MotivoCambioTasa");
			
			tpcambio = tcCtrl.obtenerTasaCambioParalela(new Date());
			tcambio = tcCtrl.obtenerTasaCambioJDEdelDia();
			
			//-------- Tasa Paralela.
			if(tpcambio!=null)
				sTasaCamP = " " + tpcambio[0].getId().getCmond()+ ": " + tpcambio[0].getId().getTcambiom();				
			else
				sTasaCamP = "USD: 1.0000";
			lblTasaCambio.setValue(sTasaCamP);
			m.put("tpcambio", tpcambio);
			m.put("lblTasaCambio",sTasaCamP);
			m.put("tasaCon","t");
			
			//-------- Tasa Oficial JDE.
			if(tcambio!=null)
				sTasaCamOf = " " + tcambio[0].getId().getCxcrdc() + ": " + tcambio[0].getId().getCxcrrd();
			else
				sTasaCamOf = "USD: 1.0000";
			lblTasaJDE.setValue(sTasaCamOf);
			m.put("tcambio", tcambio);
			m.put("lblTasaJDE",sTasaCamOf);
			m.put("tasaJDECon","t");
			
			SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
			srm.addSmartRefreshId(lblTasaCambio.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblTasaJDE.getClientId(FacesContext.getCurrentInstance()));
			
		} catch (Exception error) {
			error.printStackTrace(); 
		}
	}
/** Cerrar la ventana de fijatar Tasa Cambio **********************************************/
	public void cancelarFijarTasaCambio(ActionEvent ev){
		dwFijarTasaCambio.setWindowState("hidden");
	}
/******************************************************************************************/
/** Método: Establecer el nuevo valor para la tasa de cambio a utilizar en el pago del recibo
 *	Fecha:  24/06/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	@SuppressWarnings("unchecked")
	public void fijarTasaCambio(ActionEvent ev){
		boolean bValido=true;
		String sMensaje="";
		String sNuevaTasa="",sMotivoCambio="";
		double dNuevaTasa=0;
		Pattern pNumero = Pattern.compile("^[0-9]+\\.?[0-9]*$");
		Pattern pAlfa = Pattern.compile("^[A-Za-z0-9-.;,\\p{Blank}]{1,200}$");
		
		try {
			//---- Valores y estilos iniciales.
			lblMsgErrorCambioTasa.setValue("");
			txtNuevaTasaCambio.setStyleClass("frmInput2");
			txtMotivoCambioTasa.setStyleClass("frmInput2");
			
			//----- Validar el valor de la tasa y el valor del motivo de cambio.
			sNuevaTasa = txtNuevaTasaCambio.getValue().toString().trim();
			sMotivoCambio = txtMotivoCambioTasa.getValue().toString().trim();
			
			if(!pNumero.matcher(sNuevaTasa).matches()){
				sMensaje = "El valor ingresado para la tasa no es válido ";
				bValido = false;
				txtNuevaTasaCambio.setStyleClass("frmInput2Error");
			}else
			if(Double.parseDouble(sNuevaTasa)<=0){
				sMensaje = "La Tasa de cambio debe ser mayor que cero";
				bValido = false;
				txtNuevaTasaCambio.setStyleClass("frmInput2Error");
			}else
			if(!pAlfa.matcher(sMotivoCambio).matches()){
				sMensaje = "Debe contener de 1 a 200 Caracteres válidos";
				bValido = false;
				txtMotivoCambioTasa.setStyleClass("frmInput2Error");
			}
			//----- Continuar la operación.
			if(!bValido){
				lblMsgErrorCambioTasa.setValue(sMensaje);
			}else{
				dwFijarTasaCambio.setWindowState("hidden");			
				dNuevaTasa = Double.parseDouble(sNuevaTasa);
				String sTasaP="", sTasaJde="";
//				Divisas dv = new Divisas();
				
				//------ Guardar en el mapa el motivo del cambio de tasa.
				m.put("fcr_MotivoCambioTasa", sMotivoCambio);
				
				//---- Crear los objetos y poner las tasas al mapa.
				//---- Tasa Paralela.
				Tpararela[] tpCambio = new Tpararela[1];
				Tpararela tParal = new Tpararela(); 
				TpararelaId tParalId = new TpararelaId(); 
								
				tParalId.setCmond("USD");
				tParalId.setCmono("COR");
				tParalId.setDirec('C');
				tParalId.setFechaf(1);
				tParalId.setFechai(1);
				tParalId.setTcambiod(new BigDecimal(1/dNuevaTasa));
				tParalId.setTcambiom(new BigDecimal(sNuevaTasa));
				tParal.setId(tParalId);
				tpCambio[0] = tParal;
				
				sTasaP += tParal.getId().getCmond()+ ": " + String.format("%1$,.4f",tParal.getId().getTcambiom());
				lblTasaCambio.setValue(sTasaP);
				m.put("tpcambio", tpCambio);
				m.put("lblTasaCambio",sTasaP);
				m.put("tasaCon","t");
				
				//---- Tasa Oficial.
				Tcambio[] tcambio = new Tcambio[1];
				Tcambio tOficial = new Tcambio();
				TcambioId tOficialID = new TcambioId();
				tOficialID.setCxcrcd("COR");
				tOficialID.setCxcrdc("USD");
				tOficialID.setCxcrr(new BigDecimal(1/dNuevaTasa));
				tOficialID.setCxcrrd(new BigDecimal(sNuevaTasa));
				tOficialID.setCxeft(new Date());
				tOficial.setId(tOficialID);
				tcambio[0] = tOficial;							
				
				sTasaJde += tOficial.getId().getCxcrdc() + ": " + String.format("%1$,.4f",tOficial.getId().getCxcrrd());
					
				lblTasaJDE.setValue(sTasaJde);
				m.put("tcambio", tcambio);
				m.put("lblTasaJDE",sTasaJde);
				m.put("tasaJDECon","t");
				
				//&& ===== Ponerle tasa de cambio a las facturas.
				List<Credhdr> lstFacturasSelected = (ArrayList<Credhdr>)
						m.get("selectedFacsCredito");
				for (Credhdr hFac : lstFacturasSelected)
					hFac.getId().setTasa(new BigDecimal(sNuevaTasa));
				m.put("selectedFacsCredito", lstFacturasSelected);
				
				SmartRefreshManager srm = SmartRefreshManager
						.getCurrentInstance();
				srm.addSmartRefreshId(lblTasaCambio.getClientId(FacesContext
						.getCurrentInstance()));
				srm.addSmartRefreshId(lblTasaJDE.getClientId(FacesContext
						.getCurrentInstance()));
			}
			
		} catch (Exception error) {
			dwFijarTasaCambio.setWindowState("hidden");
			error.printStackTrace();
		}
	}
/******************************************************************************************/
/** Método: Mostrar ventana para permitir al cajero fijar la tasa de cambio.
 *	Fecha:  23/06/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public void mostrarFijarTasaCambio(ActionEvent ev){
		List<Credhdr> lstFacturasSelected = new ArrayList<Credhdr>();
		Credhdr hFac = null;
		String sMonedaFac="", sMensaje="";
		boolean bValido = true;
		String sMonedaBase = "";
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		
		try {
			txtNuevaTasaCambio.setStyleClass("frmInput2");
			txtMotivoCambioTasa.setStyleClass("frmInput2");
			
			lstFacturasSelected = (List<Credhdr>)m.get("selectedFacsCredito");
			hFac = (Credhdr)lstFacturasSelected.get(0);
			sMonedaFac = hFac.getMoneda();
			
			//obtener companias x caja
			sMonedaBase = cCtrl.sacarMonedaBase((F55ca014[])m.get("cont_f55ca014"), hFac.getId().getCodcomp());
			if(!sMonedaFac.equals(sMonedaBase)){
				txtNuevaTasaCambio.setValue(String.format("%1$,.4f",hFac.getId().getTasa() ));
				txtMotivoCambioTasa.setValue("");
				lblMsgErrorCambioTasa.setValue("");
			}else{
				bValido = false;
				sMensaje = "La opción de Fijar tasa esta disponible para facturas únicamente en Moneda Foranea";
			}
			//---- Verificar que se hayan registrado métodos de pago.
			if(bValido){
				selectedMet = (ArrayList<MetodosPago>) m.get("lstPagos");
				if(selectedMet!=null && selectedMet.size()>0){
					bValido = false;
					sMensaje = "La tasa debe fijarse antes de registrar pagos.";
				}
			}
			
			//----- Mostrar la Ventana.
			if(bValido){
				dwFijarTasaCambio.setWindowState("normal");
			}else{
				dwProcesa.setWindowState("normal");
				lblMensajeValidacion.setValue(sMensaje);
			}
			
		} catch (Exception error) {
			error.printStackTrace();
		}
	}	
/*****************************************************************************************************/
	public void buscarAgregarFacturas(ActionEvent ev){
		List lstResult = new ArrayList();
		ArrayList<Credhdr> lstFacturasSelected;
		FacturaCreditoCtrl facCreCtrl = new FacturaCreditoCtrl();
		Credhdr f = null;
		int iDesde = 0,iHasta = 0;
		Matcher matNumero = null;
		Pattern pNumero = null;
		String sDesde,sHasta;
		
		try{
		
			CodeUtil.removeFromSessionMap(new String[]{"cr_AgregarFacturasNumeroDesde","cr_AgregarFacturasNumeroHasta"});
			
			pNumero = Pattern.compile("^[0-9]+\\.?[0-9]*$");
			
			lstFacturasSelected = (ArrayList)m.get("selectedFacsCredito");	
			f = (Credhdr)lstFacturasSelected.get(0);
			
			sDesde = txtNofacturaDesde.getValue().toString();
			sHasta = txtNofacturaHasta.getValue().toString();
		
			//validar desde
			if(!sDesde.trim().equals("")){
				matNumero = pNumero.matcher(sDesde);
				if(matNumero .matches()){
					iDesde = Integer.parseInt(sDesde);
				}
			}
			//validar Hasta
			if(!sHasta.trim().equals("")){
				matNumero = pNumero.matcher(sHasta);
				if(matNumero .matches()){
					iHasta = Integer.parseInt(sHasta);
				}
			}
			
			CodeUtil.putInSessionMap("cr_AgregarFacturasNumeroDesde", iDesde);
			CodeUtil.putInSessionMap("cr_AgregarFacturasNumeroHasta", iHasta );
			
			F55ca014[] f14 = (F55ca014[])m.get("cont_f55ca014");
			lstResult = facCreCtrl.buscarFacturasCredito(f.getId().getCodcli(),f.getId().getMoneda(),f.getId().getCodcomp(),iDesde,iHasta,lstFacturasSelected,f14);
			
			gvAgregarFactura.setPageIndex(0);
			m.put("lstAgregarFactura", lstResult);
			gvAgregarFactura.dataBind();
			
		}catch(Exception ex){
			ex.printStackTrace(); 
		}
	}
/*****************************************************************************************************/
	@SuppressWarnings("unchecked")
	public boolean grabarCredito(Session s,Transaction tx,Connection cn,List lstMetodosPago,Vf55ca01 f55ca01,List<Credhdr> lstFacturasSelected, int iNumRecCredito, 
			boolean bHayFicha, List lstPagoFicha,BigDecimal bdTasaJde,BigDecimal bdTasaPar,Vautoriz vaut, String sMonedaBase, Date dFechaDelRecibo){
		boolean bHecho = false;
		String sTipoCliente = "", sConcepto = "";
	
		int iNobatch = 0, iNoReciboJDE = 0, i = 0, j = 0, iTemp = 0, iCinf = 0;
		long  iTotalTransaccion = 0 ;
		
		EmpleadoCtrl empCtrl = new EmpleadoCtrl();
		Credhdr hFac = null, hFacSel = null;
		Divisas d = new Divisas();
		ReciboCtrl rcCtrl = new ReciboCtrl();
		MetodosPago mPago = null;
		String[] sCuentaCaja = null;
		FechasUtil fecUtil = new FechasUtil();
		double dMontoD = 0, dMontoF = 0, dMontoDif = 0, dNewPendienteForaneo = 0, dNewPendienteDom = 0;
		
		double  dMontoAplicarxFactura = 0;

		
		double d1 = 0, d2 = 0;
		int imaxiter = 300;
		try {
			
			for (Credhdr factura : lstFacturasSelected) {
				boolean pagoparcial = 
				 (  (factura.getMoneda().compareTo(sMonedaBase) == 0 && 
					  factura.getMontoAplicar().compareTo(factura.getId().getCpendiente()) != 0) || 
					(factura.getMoneda().compareTo(sMonedaBase) != 0 && 
					 factura.getMontoAplicar().compareTo(factura.getId().getDpendiente()) != 0)
				) ;
				factura.setPagoparcial(pagoparcial);
			}
			
			hFac = (Credhdr) lstFacturasSelected.get(0);
			sConcepto = "R:" + iNumRecCredito + " Ca:"
					+ f55ca01.getId().getCaid() + " Com:"
					+ hFac.getId().getCodcomp();

			sTipoCliente = empCtrl.determinarTipoCliente(hFac.getId().getCodcli());
			 
			iNobatch = d.leerActualizarNoBatch();

			m.put("fcred_BatchRecibo", iNobatch);
			
			if (iNobatch > 0) {
				iNoReciboJDE = rcCtrl.leerNumeroRpdocm(true, hFac.getId().getCodcomp() );
				if (iNoReciboJDE > 0) {
					if (!hFac.getMoneda().equals(sMonedaBase)) {// facturas usd
																// F:USD
	
	while (i < lstMetodosPago.size()){	
		mPago = (MetodosPago)lstMetodosPago.get(i);
		iCinf++;
		if (iCinf > imaxiter) {
			strMensajeValidacion = "No se pudo Completar la " +
					"operación de aplicar pago a factura";
			return false;
		}// seguro contra ciclo infinito
		
		//obtener cuentas a usar por metodos de pago y moneda
		sCuentaCaja = d.obtenerCuentaCaja(f55ca01.getId().getCaid(),hFac.getId().getCodcomp(),mPago.getMetodo(),mPago.getMoneda(),null,null,null,null);
		
		if(sCuentaCaja != null){
			
			if( mPago.getMoneda().equals(sMonedaBase) ){//pago en moneda distinta a la de la factura f:USD p:COR
			 
				iTotalTransaccion = iTotalTransaccion + d.pasarAenteroLong(mPago.getEquivalente());
			 
				
				while(j < lstFacturasSelected.size()){
					
					iCinf++;
					if (iCinf > imaxiter) {
						strMensajeValidacion = "No se pudo Completar la " +
								"operación de aplicar pago a factura";
						return false;
					}// seguro contra ciclo infinito
					
					hFacSel = (Credhdr)lstFacturasSelected.get(j);
					
					//si el monto a aplicar es mayor a 0
					if( hFacSel.getMontoAplicar().compareTo(BigDecimal.ZERO) == 1 ){
						
						
						//&& =================== El pago cubre el monto de la factura
						if( new BigDecimal(Double.toString(mPago.getEquivalente())).compareTo(hFacSel.getMontoAplicar()) == 1 && 
								new BigDecimal(Double.toString(mPago.getEquivalente())).subtract(hFacSel.getMontoAplicar()) 
								.compareTo(new BigDecimal("0.25")) == -1 ){
							mPago.setEquivalente(Divisas.roundDouble(hFacSel.getMontoAplicar().doubleValue(), 2)) ;
						}
						
						if(d.roundDouble(mPago.getEquivalente()) > d.roundDouble(hFacSel.getMontoAplicar().doubleValue())){	
						
							dMontoF = d.roundDouble(mPago.getEquivalente() - hFacSel.getMontoAplicar().doubleValue());
							dMontoAplicarxFactura = d.roundDouble(dMontoAplicarxFactura + hFacSel.getMontoAplicar().doubleValue());
							dNewPendienteForaneo = d.roundDouble(hFacSel.getId().getDpendiente().doubleValue() - dMontoAplicarxFactura);
							
							if(dNewPendienteForaneo > 0){
								d2 = d.roundDouble(hFacSel.getMontoAplicar().doubleValue()*hFacSel.getId().getTasa().doubleValue());
								dNewPendienteDom = d.roundDouble(hFacSel.getId().getCpendiente().doubleValue() - d2 - dMontoD);		
								d1 = 0;	
							}else{

								d1 = d.roundDouble(hFacSel.getMontoAplicar().doubleValue()*hFacSel.getId().getTasa().doubleValue());
								d1 = d1 + dMontoD;
								d2 = hFacSel.getId().getCpendiente().doubleValue();		
								dNewPendienteDom = 0;
							}																								

							//Para el caso de fdc dFechaDelRecibo viene con la fecha del Recibo, para el resto de casos viene con new Date().
							bHecho = rcCtrl.grabarRC(cn, hFacSel.getId().getCodsuc(), hFacSel.getId().getCodcli(), hFacSel.getId().getTipofactura(), hFacSel.getId().getNofactura(), hFacSel.getId().getPartida(), hFacSel.getId().getRpdivj(), 
												 "RC", iNoReciboJDE, fecUtil.obtenerFechaJulianaDia(dFechaDelRecibo), "R", iNobatch, d2, "F",  hFacSel.getMoneda(), hFacSel.getId().getTasa().doubleValue(), d.roundDouble(hFacSel.getMontoAplicar().doubleValue()), 
												 hFacSel.getId().getCompenslm(), sCuentaCaja[1], hFacSel.getId().getCodunineg().trim(), hFacSel.getId().getTipopago(), "I", sConcepto, "", hFacSel.getId().getNomcli(), vaut.getId().getLogin(), vaut.getId().getNomapp(),hFacSel.getId().getRpddj(),hFacSel.getId().getRppo(),hFacSel.getId().getRpdcto());
							
							if(bHecho){
								
								if( mPago.getTasa().compareTo( hFacSel.getId().getTasa() ) != 0 ){
									
									dMontoDif = Divisas.roundDouble(
											mPago.getTasa().subtract(hFacSel.getId().getTasa())
												.multiply(hFacSel.getMontoAplicar()).abs()
												.doubleValue(),2);

									if( dMontoDif > 0){
										
										if( mPago.getTasa().compareTo( hFacSel.getId().getTasa() ) == -1 )
											dMontoDif = Double.parseDouble("-" + dMontoDif ) ;
										
										bHecho = rcCtrl.grabarRC(cn, hFacSel.getId().getCodsuc(), hFacSel.getId().getCodcli(), hFacSel.getId().getTipofactura(), hFacSel.getId().getNofactura(), hFacSel.getId().getPartida(), hFacSel.getId().getRpdivj(), 
											 "RG", iNoReciboJDE, fecUtil.obtenerFechaJulianaDia(dFechaDelRecibo), "R", iNobatch, dMontoDif, "F",  hFacSel.getId().getMoneda(), mPago.getTasa().doubleValue(), 0, 
											 hFacSel.getId().getCompenslm(), sCuentaCaja[1], hFacSel.getId().getCodunineg().trim(), hFacSel.getId().getTipopago(), "F", sConcepto, "P", hFacSel.getId().getNomcli(), vaut.getId().getLogin(), vaut.getId().getNomapp(),hFacSel.getId().getRpddj(),hFacSel.getId().getRppo(),hFacSel.getId().getRpdcto());
									}
									
									if(!bHecho){
										strMensajeValidacion = "Dif. cambiario ==" + rcCtrl.getError() + " <BR/> DETALLE: " + rcCtrl.getErrorDetalle();		
										return false;
									}
								}
								
								if (dNewPendienteDom > 0){
									bHecho = rcCtrl.aplicarMontoF0311(cn, "A",dNewPendienteDom, dNewPendienteForaneo, vaut.getId().getLogin(), hFacSel.getId().getCodsuc(), hFacSel.getId().getTipofactura(), hFacSel.getId().getNofactura(), hFacSel.getId().getPartida(),hFacSel.getId().getCodcli());
									if(!bHecho){	
										strMensajeValidacion = rcCtrl.getError() + " <BR/> DETALLE: " + rcCtrl.getErrorDetalle();		
										return false;
									}
								}else{
									bHecho = rcCtrl.aplicarMontoF0311(cn, "P",dNewPendienteDom, dNewPendienteForaneo, vaut.getId().getLogin(), hFacSel.getId().getCodsuc(), hFacSel.getId().getTipofactura(), hFacSel.getId().getNofactura(), hFacSel.getId().getPartida(),hFacSel.getId().getCodcli());
									if(!bHecho){
										strMensajeValidacion = rcCtrl.getError() + " <BR/> DETALLE: " + rcCtrl.getErrorDetalle();		
										return false;
									}
								}
								dMontoD = 0;
								dMontoAplicarxFactura = 0;
								d1 = 0; 
								d2 = 0;
								mPago.setMonto(Divisas.roundDouble( (mPago.getMonto()- hFacSel.getId().getCpendiente().doubleValue()),2));
								mPago.setEquivalente(dMontoF);
								hFacSel.getId().setCpendiente(new BigDecimal(dNewPendienteDom));
								hFacSel.getId().setDpendiente(new BigDecimal(dNewPendienteForaneo));	
								hFacSel.setMontoAplicar(BigDecimal.ZERO);
								j++;
							}else{													
								strMensajeValidacion = "No se pudo grabar el recibo en el JDE!!! ==" + rcCtrl.getError() + " <BR/> DETALLE: " + rcCtrl.getErrorDetalle();		
								return false;
							}
						}//el pago cubre toda la factura //========================================= CH
						else if(d.roundDouble(mPago.getEquivalente()) == d.roundDouble(hFacSel.getMontoAplicar().doubleValue())){//el pago cubre el monto de la factura

							dMontoF = d.roundDouble(mPago.getEquivalente() - hFacSel.getMontoAplicar().doubleValue());							 
							dMontoAplicarxFactura = d.roundDouble(dMontoAplicarxFactura + hFacSel.getMontoAplicar().doubleValue());							
							dNewPendienteForaneo = d.roundDouble(hFacSel.getId().getDpendiente().doubleValue() - dMontoAplicarxFactura);
							if(dNewPendienteForaneo > 0){
							
								d2 = d.roundDouble(hFacSel.getMontoAplicar().doubleValue()*hFacSel.getId().getTasa().doubleValue());	
							
								dNewPendienteDom = Divisas.roundDouble(hFacSel.getId().getCpendiente().subtract(new BigDecimal(Double.toString(d2))).doubleValue(),2) ;//   dNewPendienteDom = d.roundDouble(hFacSel.getId().getCpendiente().doubleValue() - d2 - dMontoD);				
								
								d1 = 0;	
							}else{
							
								d1 = d.roundDouble(hFacSel.getMontoAplicar().doubleValue() * hFacSel.getId().getTasa().doubleValue());
								d1 = d1 + dMontoD;
								dNewPendienteDom = 0;
								
								d2 = hFacSel.getId().getCpendiente().doubleValue();	
								if( hFacSel.isPagoparcial() && 	hFacSel.getId().getCpendiente()
										.subtract( new BigDecimal(Double.toString(mPago.getMonto()) ) )
										.abs().compareTo(new BigDecimal( MetodosPagoCtrl.EFECTIVO ) ) <= 0 ) {
									d2 = mPago.getMonto();
								}
							}																								

							
							bHecho = rcCtrl.grabarRC(cn, hFacSel.getId().getCodsuc(), hFacSel.getId().getCodcli(), hFacSel.getId().getTipofactura(), hFacSel.getId().getNofactura(), hFacSel.getId().getPartida(), hFacSel.getId().getRpdivj(), 
									            "RC", iNoReciboJDE, fecUtil.obtenerFechaJulianaDia(dFechaDelRecibo), "R", iNobatch, d2, "F",  hFacSel.getMoneda(), hFacSel.getId().getTasa().doubleValue(), d.roundDouble(hFacSel.getMontoAplicar().doubleValue()), 
									            hFacSel.getId().getCompenslm(), sCuentaCaja[1], hFacSel.getId().getCodunineg().trim(), hFacSel.getId().getTipopago(), "I", sConcepto, "", hFacSel.getId().getNomcli(), vaut.getId().getLogin(), vaut.getId().getNomapp(),hFacSel.getId().getRpddj(),hFacSel.getId().getRppo(),hFacSel.getId().getRpdcto());
							
							if(bHecho ){ 
								
								if( mPago.getTasa().compareTo(hFacSel.getId().getTasa()) != 0 ){
									
									dMontoDif = Divisas.roundDouble(
											mPago.getTasa().subtract(hFacSel.getId().getTasa())
													.multiply(hFacSel.getMontoAplicar()).abs()
													.doubleValue(),2);
									
									if( dMontoDif > 0){
										
										if( mPago.getTasa().compareTo( hFacSel.getId().getTasa() ) == -1 )
											dMontoDif = Double.parseDouble("-" + dMontoDif ) ;
										
										bHecho = rcCtrl.grabarRC(cn, hFacSel.getId().getCodsuc(), hFacSel.getId().getCodcli(), hFacSel.getId().getTipofactura(), hFacSel.getId().getNofactura(), hFacSel.getId().getPartida(), hFacSel.getId().getRpdivj(), 
											             "RG", iNoReciboJDE, fecUtil.obtenerFechaJulianaDia(dFechaDelRecibo), "R", iNobatch, dMontoDif, "F",  hFacSel.getId().getMoneda(), mPago.getTasa().doubleValue(), 0, 
											             hFacSel.getId().getCompenslm(), sCuentaCaja[1], hFacSel.getId().getCodunineg().trim(), hFacSel.getId().getTipopago(), "F", sConcepto, "P", hFacSel.getId().getNomcli(), vaut.getId().getLogin(), vaut.getId().getNomapp(),hFacSel.getId().getRpddj(),hFacSel.getId().getRppo(),hFacSel.getId().getRpdcto());
									}
									
									if(!bHecho){
										strMensajeValidacion = "Dif. cambiario ==" + rcCtrl.getError() + " <BR/> DETALLE: " + rcCtrl.getErrorDetalle();		
										return false;
									}
								}
								
								
								if (dNewPendienteDom > 0){
									bHecho = rcCtrl.aplicarMontoF0311(cn, "A",dNewPendienteDom, dNewPendienteForaneo, vaut.getId().getLogin(), hFacSel.getId().getCodsuc(), hFacSel.getId().getTipofactura(), hFacSel.getId().getNofactura(), hFacSel.getId().getPartida(),hFacSel.getId().getCodcli());
									if(!bHecho){
										strMensajeValidacion = rcCtrl.getError() + " <BR/> DETALLE: " + rcCtrl.getErrorDetalle();		
										return false;
									}
								}else{
									if(dNewPendienteDom < 0)
										dNewPendienteDom = 0;
									if(dNewPendienteForaneo < 0)
										dNewPendienteForaneo = 0 ; 
									
									bHecho = rcCtrl.aplicarMontoF0311(cn, "P",dNewPendienteDom, dNewPendienteForaneo, vaut.getId().getLogin(), hFacSel.getId().getCodsuc(), hFacSel.getId().getTipofactura(), hFacSel.getId().getNofactura(), hFacSel.getId().getPartida(),hFacSel.getId().getCodcli());
									
									if(!bHecho){
										strMensajeValidacion = rcCtrl.getError() + " <BR/> DETALLE: " + rcCtrl.getErrorDetalle();		
										return false;
									}
								}
								dMontoD = 0;
								dMontoAplicarxFactura = 0;
								d1 = 0; 
								d2 = 0;
								mPago.setMonto(0);
								mPago.setEquivalente(0);
								hFacSel.setMontoAplicar(BigDecimal.ZERO);
								hFacSel.getId().setCpendiente(new BigDecimal(dNewPendienteDom));
								hFacSel.getId().setDpendiente(new BigDecimal(dNewPendienteForaneo));	
								j++;
								i++;
								break;
							}else{
								strMensajeValidacion = "No se pudo grabar el recibo en el JDE!!! ==" + rcCtrl.getError() + " <BR/> DETALLE: " + rcCtrl.getErrorDetalle();		
								return false;
							}
						}//el pago cubre toda la factura
						
						else{
							
							//el pago no cubre la factura 
							dMontoF = d.roundDouble(hFacSel.getMontoAplicar().doubleValue() - mPago.getEquivalente());
							
							i++;

							//Para el caso de fdc dFechaDelRecibo viene con la fecha del Recibo, para el resto de casos viene con new Date().
							bHecho = rcCtrl.grabarRC(cn, hFacSel.getId().getCodsuc(), hFacSel.getId().getCodcli(), hFacSel.getId().getTipofactura(), 
										hFacSel.getId().getNofactura(), hFacSel.getId().getPartida(), hFacSel.getId().getRpdivj(), 
										"RC", iNoReciboJDE, fecUtil.obtenerFechaJulianaDia(dFechaDelRecibo), "R", iNobatch, 
										
										//d.roundDouble( mPago.getEquivalente()*hFacSel.getId().getTasa().doubleValue()),
										Divisas.roundDouble( mPago.getMonto(), 2 ),
										
										"F",  hFacSel.getMoneda(), hFacSel.getId().getTasa().doubleValue(), 
										
										//mPago.getEquivalente(), 
										Divisas.roundDouble( mPago.getEquivalente(), 2 ),
										
										hFacSel.getId().getCompenslm(), sCuentaCaja[1], hFacSel.getId().getCodunineg().trim(), 
										hFacSel.getId().getTipopago(), "I", sConcepto, "", hFacSel.getId().getNomcli(), 
										vaut.getId().getLogin(), vaut.getId().getNomapp(),hFacSel.getId().getRpddj(), 
										hFacSel.getId().getRppo(),hFacSel.getId().getRpdcto());
							
							if(!bHecho){
								strMensajeValidacion = "No se pudo grabar el recibo en el JDE!!! ==" + rcCtrl.getError() + " <BR/> DETALLE: " + rcCtrl.getErrorDetalle();		
								return false;
							}
							//se decremente el monto a apllicar a la factura 
							hFacSel.setMontoAplicar(Divisas.roundBigDecimal(new BigDecimal(dMontoF) ));
							
	
							
							dMontoAplicarxFactura = d.roundDouble(dMontoAplicarxFactura + mPago.getEquivalente());
							
								
								dMontoD = Divisas.roundDouble( hFacSel.getId().getCpendiente()
											.subtract(new BigDecimal(Double.toString(
												mPago.getMonto()))).doubleValue(), 2 ) ;
								dMontoF = Divisas.roundDouble( hFacSel.getId().getDpendiente().subtract(new BigDecimal(Double.toString(mPago.getEquivalente()))).doubleValue(), 2  ) ;

								 
								bHecho = rcCtrl.aplicarMontoF0311(cn, "A",
									dMontoD, dMontoF, 
									vaut.getId().getLogin(), hFacSel.getId().getCodsuc(), 
									hFacSel.getId().getTipofactura(), 
									hFacSel.getId().getNofactura(), 
									hFacSel.getId().getPartida(),
									hFacSel.getId().getCodcli());
								
								mPago.setMonto(0);
								mPago.setEquivalente(0);
								hFacSel.setMontoAplicar(
										  hFacSel.getMontoAplicar()
											.subtract(new BigDecimal(Double
											.toString(mPago.getEquivalente())))
										) ;
								
								hFacSel.getId().setCpendiente(new BigDecimal(Double.toString( dMontoD ) ));
								hFacSel.getId().setDpendiente(new BigDecimal(Double.toString( dMontoF ) ));	
								dMontoAplicarxFactura = 0;
								
//							}
							break; 
						}//el pago no cubre la factura
						
					}else{//no incluye la factura
						j++;
					}//no incluye la factura
				}//while de facturas

			}else{//pago en la misma moneda de la factura f:USD p:USD
				
				iTotalTransaccion = iTotalTransaccion + d.pasarAenteroLong( mPago.getMonto());
				 
				while(j < lstFacturasSelected.size()){
					
					// seguro contra ciclo infinito
					iCinf++;
					if (iCinf > imaxiter) {
						strMensajeValidacion = "Error al aplicar monto de pago a factura";
						return false;
					}
					
					hFacSel = (Credhdr)lstFacturasSelected.get(j);
					if(hFacSel.getMontoAplicar().doubleValue()>0){//si el monto a aplicar es mayor a 0
						
						if(d.roundDouble(mPago.getMonto()) > d.roundDouble(hFacSel.getMontoAplicar().doubleValue())){//el pago cubre el monto de la factura
						
							dMontoF = d.roundDouble(mPago.getMonto() - hFacSel.getMontoAplicar().doubleValue());
						
							dMontoAplicarxFactura = d.roundDouble(dMontoAplicarxFactura + hFacSel.getMontoAplicar().doubleValue());
																		
							dNewPendienteForaneo = d.roundDouble(hFacSel.getId().getDpendiente().doubleValue() - dMontoAplicarxFactura);
						
							if(dNewPendienteForaneo > 0){
						
								d2 = d.roundDouble(hFacSel.getMontoAplicar().doubleValue()*hFacSel.getId().getTasa().doubleValue());	
						
								dNewPendienteDom = d.roundDouble(hFacSel.getId().getCpendiente().doubleValue() - d2 - dMontoD);
																														
								d1 = 0;	
							}else{													
								d1 = d.roundDouble(hFacSel.getMontoAplicar().doubleValue()*hFacSel.getId().getTasa().doubleValue());
						
								d1 = d1 + dMontoD;
						
								d2 = hFacSel.getId().getCpendiente().doubleValue();	
						
								dNewPendienteDom = 0;

							}																																				

							bHecho = rcCtrl.grabarRC(cn, hFacSel.getId().getCodsuc(), hFacSel.getId().getCodcli(), hFacSel.getId().getTipofactura(), hFacSel.getId().getNofactura(), hFacSel.getId().getPartida(), hFacSel.getId().getRpdivj(), 
									            "RC", iNoReciboJDE, fecUtil.obtenerFechaJulianaDia(dFechaDelRecibo), "R", iNobatch, d2, "F",  hFacSel.getMoneda(), hFacSel.getId().getTasa().doubleValue(), d.roundDouble(hFacSel.getMontoAplicar().doubleValue()), 
									            hFacSel.getId().getCompenslm(), sCuentaCaja[1], hFacSel.getId().getCodunineg().trim(), hFacSel.getId().getTipopago(), "I", sConcepto, "", hFacSel.getId().getNomcli(), vaut.getId().getLogin(), vaut.getId().getNomapp(),hFacSel.getId().getRpddj(),hFacSel.getId().getRppo(),hFacSel.getId().getRpdcto());
							
							if(bHecho){
								
								//&& ========================== calculo del diferencial cambiario
								BigDecimal bdTasaDiferencial = (mPago.getTasa().compareTo(BigDecimal.ONE) == 0) ?
									    bdTasaJde:
										mPago.getTasa();
										
								if( bdTasaDiferencial.compareTo( hFacSel.getId().getTasa() ) != 0 ){
									
									dMontoDif =	Divisas.roundDouble( hFacSel.getId().getTasa().subtract( bdTasaDiferencial ).multiply( hFacSel.getMontoAplicar() ).abs().doubleValue(),  2);
									
									if( dMontoDif > 0){
										
										if( bdTasaDiferencial.compareTo( hFacSel.getId().getTasa() ) == -1 )
											dMontoDif = Double.parseDouble("-".concat( Double.toString( dMontoDif ) ) ) ;
										
										bHecho = rcCtrl.grabarRC(cn, hFacSel.getId().getCodsuc(), hFacSel.getId().getCodcli(), hFacSel.getId().getTipofactura(), hFacSel.getId().getNofactura(), hFacSel.getId().getPartida(), hFacSel.getId().getRpdivj(), 
												"RG", iNoReciboJDE, fecUtil.obtenerFechaJulianaDia(dFechaDelRecibo), "R", iNobatch, dMontoDif, "F",  hFacSel.getId().getMoneda(),  bdTasaDiferencial.doubleValue(), 0, 
												hFacSel.getId().getCompenslm(), sCuentaCaja[1], hFacSel.getId().getCodunineg().trim(), hFacSel.getId().getTipopago(), "F", sConcepto, "P", hFacSel.getId().getNomcli(), vaut.getId().getLogin(), vaut.getId().getNomapp(),hFacSel.getId().getRpddj(),hFacSel.getId().getRppo(),hFacSel.getId().getRpdcto());
									}
									
									if(!bHecho){
										strMensajeValidacion = "Dif. cambiario ==" + rcCtrl.getError() + " <BR/> DETALLE: " + rcCtrl.getErrorDetalle();		
										return false;
									}
								}
								
								
								if (dNewPendienteDom > 0){
									bHecho = rcCtrl.aplicarMontoF0311(cn, "A",dNewPendienteDom, dNewPendienteForaneo, vaut.getId().getLogin(), hFacSel.getId().getCodsuc(), hFacSel.getId().getTipofactura(), hFacSel.getId().getNofactura(), hFacSel.getId().getPartida(),hFacSel.getId().getCodcli());
									if(!bHecho){
										strMensajeValidacion = rcCtrl.getError() + " <BR/> DETALLE: " + rcCtrl.getErrorDetalle();		
										return false;
									}
								}else{
									bHecho = rcCtrl.aplicarMontoF0311(cn, "P",dNewPendienteDom, dNewPendienteForaneo, vaut.getId().getLogin(), hFacSel.getId().getCodsuc(), hFacSel.getId().getTipofactura(), hFacSel.getId().getNofactura(), hFacSel.getId().getPartida(),hFacSel.getId().getCodcli());
									if(!bHecho){
										strMensajeValidacion = rcCtrl.getError() + " <BR/> DETALLE: " + rcCtrl.getErrorDetalle();		
										return false;
									}
								}
								dMontoD = 0;
								dMontoAplicarxFactura = 0;
								d1=0; d2=0;
								if(!bHecho){														
									strMensajeValidacion = "No se pudo grabar el diferencial cambiario en el JDE!!! ==" + rcCtrl.getError() + " <BR/> DETALLE: " + rcCtrl.getErrorDetalle();		
									return false;
								}
								mPago.setMonto(dMontoF);	
								mPago.setEquivalente(dMontoF);
								hFacSel.setMontoAplicar(BigDecimal.ZERO);
								hFacSel.getId().setDpendiente(new BigDecimal(dNewPendienteDom));
								hFacSel.getId().setCpendiente(new BigDecimal(dNewPendienteForaneo));
								j++;
							}else{													
								strMensajeValidacion = "No se pudo grabar el recibo en el JDE!!! ==" + rcCtrl.getError() + " <BR/> DETALLE: " + rcCtrl.getErrorDetalle();		
								return false;
							}
						}//el pago cubre toda la factura
						else if(d.roundDouble(mPago.getMonto()) == d.roundDouble(hFacSel.getMontoAplicar().doubleValue())){//el pago es igual a la factura

							dMontoF = d.roundDouble(mPago.getMonto() - hFacSel.getMontoAplicar().doubleValue());

							dMontoAplicarxFactura = d.roundDouble(dMontoAplicarxFactura + hFacSel.getMontoAplicar().doubleValue());

							dNewPendienteForaneo = d.roundDouble(hFacSel.getId().getDpendiente().doubleValue() - dMontoAplicarxFactura);

							if(dNewPendienteForaneo > 0){

								d2 = d.roundDouble(hFacSel.getMontoAplicar().doubleValue() * hFacSel.getId().getTasa().doubleValue());
								
								dNewPendienteDom = Divisas.roundDouble( hFacSel.getId().getCpendiente().subtract(new BigDecimal(Double.toString(d2))).doubleValue(),2); //dNewPendienteDom = d.roundDouble(hFacSel.getId().getCpendiente().doubleValue()  );
																																						
								d1 = 0;	
								
							}else{													
								d1 = d.roundDouble(hFacSel.getMontoAplicar().doubleValue()*hFacSel.getId().getTasa().doubleValue());
								
								d1 = d1 + dMontoD;
								//LogCrtl.sendLogDebgs2("<===========> d1: "+d1+" dMontoD: "+dMontoD);
								d2 = hFacSel.getId().getCpendiente().doubleValue();			
								//LogCrtl.sendLogDebgs2("<===========> d2: "+d2+"  hFacSel.getId().getCpendiente().doubleValue(): "+ hFacSel.getId().getCpendiente().doubleValue());
								dNewPendienteDom = 0;
								

							}																																				

							
							//Para el caso de fdc dFechaDelRecibo viene con la fecha del Recibo, para el resto de casos viene con new Date().
							bHecho = rcCtrl.grabarRC(cn, hFacSel.getId().getCodsuc(), hFacSel.getId().getCodcli(), hFacSel.getId().getTipofactura(), hFacSel.getId().getNofactura(), hFacSel.getId().getPartida(), hFacSel.getId().getRpdivj(), 
									             "RC", iNoReciboJDE, fecUtil.obtenerFechaJulianaDia(dFechaDelRecibo), "R", iNobatch, d2, "F",  hFacSel.getMoneda(), hFacSel.getId().getTasa().doubleValue(), d.roundDouble(hFacSel.getMontoAplicar().doubleValue()), 
									             hFacSel.getId().getCompenslm(), sCuentaCaja[1], hFacSel.getId().getCodunineg().trim(), hFacSel.getId().getTipopago(), "I", sConcepto, "", hFacSel.getId().getNomcli(), vaut.getId().getLogin(), vaut.getId().getNomapp(),hFacSel.getId().getRpddj(),hFacSel.getId().getRppo(),hFacSel.getId().getRpdcto());
							
							if(bHecho){
							
								BigDecimal bdTasaDiferencial = (mPago.getTasa().compareTo(BigDecimal.ONE) == 0) ?
										bdTasaJde:
										mPago.getTasa()	;
								
								
								//si hay diferencial
								if( bdTasaDiferencial.compareTo(hFacSel.getId().getTasa()) != 0 ){
									
									dMontoDif =	Divisas.roundDouble( hFacSel.getId().getTasa().subtract( bdTasaDiferencial ).multiply( hFacSel.getMontoAplicar() ).abs().doubleValue(), 2);
									
									if( dMontoDif > 0){
										
										if( bdTasaDiferencial.compareTo( hFacSel.getId().getTasa() ) == -1 )
											dMontoDif = Double.parseDouble("-".concat( Double.toString( dMontoDif ) ) ) ;
										
										bHecho = rcCtrl.grabarRC(cn, hFacSel.getId().getCodsuc(), hFacSel.getId().getCodcli(), hFacSel.getId().getTipofactura(), hFacSel.getId().getNofactura(), hFacSel.getId().getPartida(), hFacSel.getId().getRpdivj(), 
												"RG", iNoReciboJDE, fecUtil.obtenerFechaJulianaDia(dFechaDelRecibo), "R", iNobatch, dMontoDif, "F",  hFacSel.getId().getMoneda(), bdTasaDiferencial.doubleValue(), 0, 
												hFacSel.getId().getCompenslm(), sCuentaCaja[1], hFacSel.getId().getCodunineg().trim(), hFacSel.getId().getTipopago(), "F", sConcepto, "P", hFacSel.getId().getNomcli(), vaut.getId().getLogin(), vaut.getId().getNomapp(),hFacSel.getId().getRpddj(),hFacSel.getId().getRppo(),hFacSel.getId().getRpdcto());
									}
									
									if(!bHecho){
										strMensajeValidacion = "Dif. cambiario ==" + rcCtrl.getError() + " <BR/> DETALLE: " + rcCtrl.getErrorDetalle();		
										return false;
									}
								}
								
								if (dNewPendienteDom > 0){
									bHecho = rcCtrl.aplicarMontoF0311(cn, "A",dNewPendienteDom, dNewPendienteForaneo, vaut.getId().getLogin(), hFacSel.getId().getCodsuc(), hFacSel.getId().getTipofactura(), hFacSel.getId().getNofactura(), hFacSel.getId().getPartida(),hFacSel.getId().getCodcli());
									if(!bHecho){
										strMensajeValidacion = rcCtrl.getError() + " <BR/> DETALLE: " + rcCtrl.getErrorDetalle();		
										return false;
									}
								}else{
									bHecho = rcCtrl.aplicarMontoF0311(cn, "P",dNewPendienteDom, dNewPendienteForaneo, vaut.getId().getLogin(), hFacSel.getId().getCodsuc(), hFacSel.getId().getTipofactura(), hFacSel.getId().getNofactura(), hFacSel.getId().getPartida(),hFacSel.getId().getCodcli());
									if(!bHecho){
										strMensajeValidacion = rcCtrl.getError() + " <BR/> DETALLE: " + rcCtrl.getErrorDetalle();		
										return false;
									}
								}
								dMontoD = 0;
								dMontoAplicarxFactura = 0;
								d1 = 0; 
								d2 = 0;
								mPago.setMonto(0);
								hFacSel.setMontoAplicar(BigDecimal.ZERO);
								hFacSel.getId().setDpendiente(new BigDecimal(dNewPendienteDom));
								hFacSel.getId().setCpendiente(new BigDecimal(dNewPendienteForaneo));
								j++;
								i++;
								break;
							}else{
								strMensajeValidacion = "No se pudo grabar el recibo en el JDE!!! ==" + rcCtrl.getError() + " <BR/> DETALLE: " + rcCtrl.getErrorDetalle();		
								return false;
							}												
						}//el pago es igual al de la factura
						else{//el pago no cubre la factura
							 
							i++;
							dMontoF = d.roundDouble(hFacSel.getMontoAplicar().doubleValue() - mPago.getMonto());
							
							BigDecimal tasatmp = hFacSel.getId().getCpendiente().divide(hFacSel.getId().getDpendiente(),4,RoundingMode.HALF_UP);
							
							bHecho = rcCtrl.grabarRC(cn, hFacSel.getId().getCodsuc(), hFacSel.getId().getCodcli(), hFacSel.getId().getTipofactura(), hFacSel.getId().getNofactura(), hFacSel.getId().getPartida(), hFacSel.getId().getRpdivj(), 
									 "RC", iNoReciboJDE, fecUtil.obtenerFechaJulianaDia(dFechaDelRecibo), "R", iNobatch, d.roundDouble(mPago.getMonto() * tasatmp.doubleValue() ), "F",  hFacSel.getMoneda(), tasatmp.doubleValue(), d.roundDouble(mPago.getMonto()), 
									 hFacSel.getId().getCompenslm(), sCuentaCaja[1], hFacSel.getId().getCodunineg().trim(), hFacSel.getId().getTipopago(), "I", sConcepto, "", hFacSel.getId().getNomcli(), vaut.getId().getLogin(), vaut.getId().getNomapp(),hFacSel.getId().getRpddj(),hFacSel.getId().getRppo(),hFacSel.getId().getRpdcto());
							
							
							if(!bHecho){													
								strMensajeValidacion = "No se pudo grabar el recibo en el JDE!!! ==" + rcCtrl.getError() + " <BR/> DETALLE: " + rcCtrl.getErrorDetalle();		
								return false;
							}
							
							//&& ==================  condicionar a pago completo o abono de la factura.
							if(hFacSel.isPagoparcial() && hFacSel.getMontoAplicar().compareTo(hFacSel.getId().getDpendiente()) != 0 ){
								BigDecimal cpendienteNew = hFacSel.getMontoAplicar()
											.multiply(tasatmp).setScale(2, RoundingMode.HALF_UP);
								hFacSel.getId().setCpendiente(cpendienteNew);
							}
							
							hFacSel.setMontoAplicar(new BigDecimal(dMontoF).setScale(2, RoundingMode.HALF_UP));
							hFacSel.getId().setDpendiente(new BigDecimal(dMontoF).setScale(2, RoundingMode.HALF_UP));	  	
							hFacSel.getId().setCpendiente( Divisas.roundBigDecimal( hFacSel.getId().getCpendiente().subtract(  new BigDecimal(Double.toString( mPago.getMonto()) ).multiply(tasatmp))));
							
							mPago.setMonto(0);	
							mPago.setEquivalente(0);
							
							dMontoAplicarxFactura = d.roundDouble(dMontoAplicarxFactura + mPago.getMonto());
							//LogCrtl.sendLogDebgs2("<===========> dMontoAplicarxFactura: "+dMontoAplicarxFactura+"  mPago.getMonto(): "+ mPago.getMonto());
							dMontoD = d.roundDouble(dMontoAplicarxFactura * hFacSel.getId().getTasa().doubleValue());
							//LogCrtl.sendLogDebgs2("<===========> dMontoAplicarxFactura: "+dMontoAplicarxFactura+"  hFacSel.getId().getTasa().doubleValue(): "+ hFacSel.getId().getTasa().doubleValue());
							break;
						}//el pago no cubre la factura
					}else{//no incluye la factura
						j++;
					}//no incluye la factura
				}//while de facturas
				
			}//pago en la misma moneda de la factura f:USD p:USD
		}else{
			strMensajeValidacion = d.getError() + " <BR/> DETALLE: " + d.getErrorDetalle();		
			return false;
		}
	}//while de metodos de pago
	
}//fin facturas usd
else{//facturas en cor F: COR 
	//LogCrtl.sendLogDebgs2("<===========> facturas en cor F: COR  l2141 ");
	while (i < lstMetodosPago.size()){
		iCinf++;
		if (iCinf > imaxiter) {
			strMensajeValidacion = "No se pudo Completar la operación!!! ";
			return false;
		}// seguro contra ciclo infinito
		
		mPago = (MetodosPago)lstMetodosPago.get(i);
		//obtener cuentas a usar por metodos de pago y moneda
		sCuentaCaja = d.obtenerCuentaCaja(f55ca01.getId().getCaid(),hFac.getId().getCodcomp(),mPago.getMetodo(),mPago.getMoneda(),null,null,null,null);
		if(sCuentaCaja != null){
			if(!mPago.getMoneda().equals(sMonedaBase)){//pago en moneda distinta a la de la factura f:COR p:USD
				//LogCrtl.sendLogDebgs2("<===========> pago en moneda distinta a la de la factura f:COR p:USD  l2145 ");
				iTotalTransaccion = iTotalTransaccion + d.pasarAenteroLong(mPago.getEquivalente());
				while(j < lstFacturasSelected.size()){
					hFacSel = (Credhdr)lstFacturasSelected.get(j);
					if(hFacSel.getMontoAplicar().doubleValue()>0){//si el monto a aplicar es mayor a 0
						if(d.roundDouble(mPago.getEquivalente()) > d.roundDouble(hFacSel.getMontoAplicar().doubleValue())){//el pago cubre el monto de la factura
							dMontoF = d.roundDouble(mPago.getEquivalente() - hFacSel.getMontoAplicar().doubleValue());

							dMontoAplicarxFactura = d.roundDouble(dMontoAplicarxFactura + hFacSel.getMontoAplicar().doubleValue());

							dNewPendienteDom = d.roundDouble(hFacSel.getId().getCpendiente().doubleValue() - hFacSel.getMontoAplicar().doubleValue());
							
							//Para el caso de fdc dFechaDelRecibo viene con la fecha del Recibo, para el resto de casos viene con new Date().
							bHecho = rcCtrl.grabarRC(cn, hFacSel.getId().getCodsuc(), hFacSel.getId().getCodcli(), hFacSel.getId().getTipofactura(), hFacSel.getId().getNofactura(), hFacSel.getId().getPartida(), hFacSel.getId().getRpdivj(), 
									             "RC", iNoReciboJDE, fecUtil.obtenerFechaJulianaDia(dFechaDelRecibo), "R", iNobatch, d.roundDouble(hFacSel.getMontoAplicar().doubleValue()), "D",  hFacSel.getMoneda(), 0, 0, 
									             hFacSel.getId().getCompenslm(), sCuentaCaja[1], hFacSel.getId().getCodunineg().trim(), hFacSel.getId().getTipopago(), "I", sConcepto, "", hFacSel.getId().getNomcli(), vaut.getId().getLogin(), vaut.getId().getNomapp(),hFacSel.getId().getRpddj(),hFacSel.getId().getRppo(),hFacSel.getId().getRpdcto());
																			
							if(bHecho){
																																		
								if (dNewPendienteDom > 0){
									bHecho = rcCtrl.aplicarMontoF0311(cn, "A",dNewPendienteDom, 0, vaut.getId().getLogin(), hFacSel.getId().getCodsuc(), hFacSel.getId().getTipofactura(), hFacSel.getId().getNofactura(), hFacSel.getId().getPartida(),hFacSel.getId().getCodcli());
									if(!bHecho){
										strMensajeValidacion = rcCtrl.getError() + " <BR/> DETALLE: " + rcCtrl.getErrorDetalle();		
										return false;
									}
								}else{
									bHecho = rcCtrl.aplicarMontoF0311(cn, "P",dNewPendienteDom, 0, vaut.getId().getLogin(), hFacSel.getId().getCodsuc(), hFacSel.getId().getTipofactura(), hFacSel.getId().getNofactura(), hFacSel.getId().getPartida(),hFacSel.getId().getCodcli());
									if(!bHecho){
										strMensajeValidacion = rcCtrl.getError() + " <BR/> DETALLE: " + rcCtrl.getErrorDetalle();		
										return false;
									}
								}

								dMontoAplicarxFactura = 0;
								mPago.setEquivalente(dMontoF);
								hFacSel.setMontoAplicar(BigDecimal.ZERO);
								hFacSel.getId().setCpendiente(BigDecimal.ZERO);
								j++;
								
							}else{
								strMensajeValidacion = "No se pudo grabar el recibo en el JDE!!! ==" + rcCtrl.getError() + " <BR/> DETALLE: " + rcCtrl.getErrorDetalle();		
								return false;
							}
						}//el pago cubre toda la factura
						else if(d.roundDouble(mPago.getEquivalente()) == d.roundDouble(hFacSel.getMontoAplicar().doubleValue())){//el pago cubre el monto de la factura

							dMontoF = d.roundDouble(mPago.getEquivalente() - hFacSel.getMontoAplicar().doubleValue());

							dMontoAplicarxFactura = d.roundDouble(dMontoAplicarxFactura + hFacSel.getMontoAplicar().doubleValue());

							dNewPendienteDom = d.roundDouble(hFacSel.getId().getCpendiente().doubleValue() - hFacSel.getMontoAplicar().doubleValue());

							bHecho = rcCtrl.grabarRC(cn, hFacSel.getId().getCodsuc(), hFacSel.getId().getCodcli(), hFacSel.getId().getTipofactura(), hFacSel.getId().getNofactura(), hFacSel.getId().getPartida(), hFacSel.getId().getRpdivj(), 
									             "RC", iNoReciboJDE, fecUtil.obtenerFechaJulianaDia(dFechaDelRecibo), "R", iNobatch, d.roundDouble(hFacSel.getMontoAplicar().doubleValue()), "D",  hFacSel.getMoneda(), 0, 0, 
									             hFacSel.getId().getCompenslm(), sCuentaCaja[1], hFacSel.getId().getCodunineg().trim(), hFacSel.getId().getTipopago(), "I", sConcepto, "", hFacSel.getId().getNomcli(), vaut.getId().getLogin(), vaut.getId().getNomapp(),hFacSel.getId().getRpddj(),hFacSel.getId().getRppo(),hFacSel.getId().getRpdcto());
																			
							if(bHecho){
																																		
								if (dNewPendienteDom > 0){
									bHecho = rcCtrl.aplicarMontoF0311(cn, "A",dNewPendienteDom, 0, vaut.getId().getLogin(), hFacSel.getId().getCodsuc(), hFacSel.getId().getTipofactura(), hFacSel.getId().getNofactura(), hFacSel.getId().getPartida(),hFacSel.getId().getCodcli());
									if(!bHecho){
										strMensajeValidacion = rcCtrl.getError() + " <BR/> DETALLE: " + rcCtrl.getErrorDetalle();		
										return false;
									}
								}else{
									bHecho = rcCtrl.aplicarMontoF0311(cn, "P",dNewPendienteDom, 0, vaut.getId().getLogin(), hFacSel.getId().getCodsuc(), hFacSel.getId().getTipofactura(), hFacSel.getId().getNofactura(), hFacSel.getId().getPartida(),hFacSel.getId().getCodcli());
									if(!bHecho){
										strMensajeValidacion = rcCtrl.getError() + " <BR/> DETALLE: " + rcCtrl.getErrorDetalle();		
										return false;
									}
								}

								dMontoAplicarxFactura = 0;
								mPago.setEquivalente(0);
								hFacSel.setMontoAplicar(BigDecimal.ZERO);
								hFacSel.getId().setCpendiente(new BigDecimal(dNewPendienteDom));
								j++;												
								i++;
								break;
							}else{
								strMensajeValidacion = "No se pudo grabar el recibo en el JDE!!! ==" + rcCtrl.getError() + " <BR/> DETALLE: " + rcCtrl.getErrorDetalle();		
								return false;
							}
						}//el pago cubre toda la factura
						else{//el pago no cubre la factura

							dMontoF = d.roundDouble(hFacSel.getMontoAplicar().doubleValue() - mPago.getEquivalente());
							i++;
							bHecho = rcCtrl.grabarRC(cn, hFacSel.getId().getCodsuc(), hFacSel.getId().getCodcli(), hFacSel.getId().getTipofactura(), hFacSel.getId().getNofactura(), hFacSel.getId().getPartida(), hFacSel.getId().getRpdivj(), 
									 "RC", iNoReciboJDE, fecUtil.obtenerFechaJulianaDia(dFechaDelRecibo), "R", iNobatch, d.roundDouble(mPago.getEquivalente()), "D",  hFacSel.getMoneda(), 0,0, 
									 hFacSel.getId().getCompenslm(), sCuentaCaja[1], hFacSel.getId().getCodunineg().trim(), hFacSel.getId().getTipopago(), "I", sConcepto, "", hFacSel.getId().getNomcli(), vaut.getId().getLogin(), vaut.getId().getNomapp(),hFacSel.getId().getRpddj(),hFacSel.getId().getRppo(),hFacSel.getId().getRpdcto());
							
							if(!bHecho){
								strMensajeValidacion = "No se pudo grabar el recibo en el JDE!!! ==" + rcCtrl.getError() + " <BR/> DETALLE: " + rcCtrl.getErrorDetalle();		
								return false;
							}
							//se decremente el monto a apllicar a la factura 
							hFacSel.setMontoAplicar(new BigDecimal(dMontoF));

							hFacSel.getId().setCpendiente(new BigDecimal(d.roundDouble(hFacSel.getId().getCpendiente().doubleValue() - mPago.getEquivalente())));
							dMontoAplicarxFactura = d.roundDouble(dMontoAplicarxFactura + mPago.getEquivalente());

							break;
						}//el pago no cubre la factura
					}else{//no incluye la factura
						j++;
					}//no incluye la factura
				}//while de facturas
				//que sea el ultimo pago
				//que sea la ultima factura
				//que ya no haya monto pendiente de la ultima factura.
				//Esta es una solución temporal. La solución correcta proviene revisando la justificación
				//del pago en la pocket y adecuar los redondeos como los hace el sistema de caja central.
				//El problema está en que paso montos redondeados de facturas al a02factco
			    //(el valor * 100 y trunco el resto), luego tengo los métodos de pago sin truncar nada
			    //y en la pocket no trunco ni las facturas ni los métodos de pago...por lo tanto es muy
			    //dificil que coincidan por los redondeos del sistema central de caja.
				if (j >= lstFacturasSelected.size()){ //ya no hay más facturas
					if (i == lstMetodosPago.size() -1) { //estoy en el último método de pago
						if (mPago.getEquivalente() <= 0.20) {
							++i;
						}
					}
				}
			}else{//pago en la misma moneda de la factura f:COR p:COR
				iTotalTransaccion = iTotalTransaccion + d.pasarAenteroLong(mPago.getMonto());
				while(j < lstFacturasSelected.size()){
					iCinf++;
					if (iCinf > 999) {
						strMensajeValidacion = "No se pudo Completar la operación";
						return false;
						
					}// seguro contra ciclo infinito
					hFacSel = (Credhdr)lstFacturasSelected.get(j);
					if(hFacSel.getMontoAplicar().doubleValue()>0){//si el monto a aplicar es mayor a 0
						if(d.roundDouble(mPago.getMonto()) > d.roundDouble(hFacSel.getMontoAplicar().doubleValue())){//el pago cubre el monto de la factura												
							dMontoF = d.roundDouble(mPago.getMonto() - hFacSel.getMontoAplicar().doubleValue());
							dMontoAplicarxFactura = d.roundDouble(dMontoAplicarxFactura + hFacSel.getMontoAplicar().doubleValue());
							dNewPendienteDom = d.roundDouble(hFacSel.getId().getCpendiente().doubleValue() - d.roundDouble(hFacSel.getMontoAplicar().doubleValue()));
							bHecho = rcCtrl.grabarRC(cn, hFacSel.getId().getCodsuc(), hFacSel.getId().getCodcli(), hFacSel.getId().getTipofactura(), hFacSel.getId().getNofactura(), hFacSel.getId().getPartida(), hFacSel.getId().getRpdivj(), 
									             "RC", iNoReciboJDE, fecUtil.obtenerFechaJulianaDia(dFechaDelRecibo), "R", iNobatch, d.roundDouble(hFacSel.getMontoAplicar().doubleValue()), "D",  hFacSel.getMoneda(), 0, 0, 
									             hFacSel.getId().getCompenslm(), sCuentaCaja[1], hFacSel.getId().getCodunineg().trim(), hFacSel.getId().getTipopago(), "I", sConcepto, "", hFacSel.getId().getNomcli(), vaut.getId().getLogin(), vaut.getId().getNomapp(),hFacSel.getId().getRpddj(),hFacSel.getId().getRppo(),hFacSel.getId().getRpdcto());
							
							if(bHecho){																										
								
								if (dNewPendienteDom > 0){
									bHecho = rcCtrl.aplicarMontoF0311(cn, "A",dNewPendienteDom, 0, vaut.getId().getLogin(), hFacSel.getId().getCodsuc(), hFacSel.getId().getTipofactura(), hFacSel.getId().getNofactura(), hFacSel.getId().getPartida(),hFacSel.getId().getCodcli());
									if(!bHecho){
										strMensajeValidacion = rcCtrl.getError() + " <BR/> DETALLE: " + rcCtrl.getErrorDetalle();		
										return false;
									}
								}else{
									bHecho = rcCtrl.aplicarMontoF0311(cn, "P",dNewPendienteDom, 0, vaut.getId().getLogin(), hFacSel.getId().getCodsuc(), hFacSel.getId().getTipofactura(), hFacSel.getId().getNofactura(), hFacSel.getId().getPartida(),hFacSel.getId().getCodcli());
									if(!bHecho){
										strMensajeValidacion = rcCtrl.getError() + " <BR/> DETALLE: " + rcCtrl.getErrorDetalle();		
										return false;
									}
								}

								dMontoAplicarxFactura = 0;
								mPago.setMonto(dMontoF);
								hFacSel.setMontoAplicar(BigDecimal.ZERO);
								hFacSel.getId().setCpendiente(new BigDecimal(dNewPendienteDom));
								j++;
							}else{
								strMensajeValidacion = "No se pudo grabar el recibo en el JDE!!! ==" + rcCtrl.getError() + " <BR/> DETALLE: " + rcCtrl.getErrorDetalle();		
								return false;
							}
						}//el pago cubre toda la factura
						else if(d.roundDouble(mPago.getMonto()) == d.roundDouble(hFacSel.getMontoAplicar().doubleValue())){//el pago cubre el monto de la factura
							dMontoF = d.roundDouble(mPago.getMonto() - hFacSel.getMontoAplicar().doubleValue());
							dMontoAplicarxFactura = d.roundDouble(dMontoAplicarxFactura + hFacSel.getMontoAplicar().doubleValue());
							dNewPendienteDom = d.roundDouble(hFacSel.getId().getCpendiente().doubleValue() - d.roundDouble(hFacSel.getMontoAplicar().doubleValue()));
							bHecho = rcCtrl.grabarRC(cn, hFacSel.getId().getCodsuc(), hFacSel.getId().getCodcli(), hFacSel.getId().getTipofactura(), hFacSel.getId().getNofactura(), hFacSel.getId().getPartida(), hFacSel.getId().getRpdivj(), 
									             "RC", iNoReciboJDE, fecUtil.obtenerFechaJulianaDia(dFechaDelRecibo), "R", iNobatch, d.roundDouble(hFacSel.getMontoAplicar().doubleValue()), "D",  hFacSel.getMoneda(), 0, 0, 
									             hFacSel.getId().getCompenslm(), sCuentaCaja[1], hFacSel.getId().getCodunineg().trim(), hFacSel.getId().getTipopago(), "I", sConcepto, "", hFacSel.getId().getNomcli(), vaut.getId().getLogin(), vaut.getId().getNomapp(),hFacSel.getId().getRpddj(),hFacSel.getId().getRppo(),hFacSel.getId().getRpdcto());

							if(bHecho){																										
								
								if (dNewPendienteDom > 0){
									bHecho = rcCtrl.aplicarMontoF0311(cn, "A",dNewPendienteDom, 0, vaut.getId().getLogin(), hFacSel.getId().getCodsuc(), hFacSel.getId().getTipofactura(), hFacSel.getId().getNofactura(), hFacSel.getId().getPartida(),hFacSel.getId().getCodcli());
									if(!bHecho){
										strMensajeValidacion = rcCtrl.getError() + " <BR/> DETALLE: " + rcCtrl.getErrorDetalle();		
										return false;
									}
								}else{
									bHecho = rcCtrl.aplicarMontoF0311(cn, "P",dNewPendienteDom, 0, vaut.getId().getLogin(), hFacSel.getId().getCodsuc(), hFacSel.getId().getTipofactura(), hFacSel.getId().getNofactura(), hFacSel.getId().getPartida(),hFacSel.getId().getCodcli());
									if(!bHecho){
										strMensajeValidacion = rcCtrl.getError() + " <BR/> DETALLE: " + rcCtrl.getErrorDetalle();		
										return false;
									}
								}

								dMontoAplicarxFactura = 0;

								if(!bHecho){
									strMensajeValidacion = "Dif. cambiario ==" + rcCtrl.getError() + " <BR/> DETALLE: " + rcCtrl.getErrorDetalle();		
									return false;
								}
								mPago.setMonto(0);
								hFacSel.setMontoAplicar(BigDecimal.ZERO);
								hFacSel.getId().setCpendiente(new BigDecimal(dNewPendienteDom));
								j++;
								i++;
								break;
							}else{
								strMensajeValidacion = "No se pudo grabar el recibo en el JDE!!! ==" + rcCtrl.getError() + " <BR/> DETALLE: " + rcCtrl.getErrorDetalle();		
								return false;
							}
						}//el pago cubre toda la factura
						else{//el pago no cubre la factura
							dMontoF = d.roundDouble(hFacSel.getMontoAplicar().doubleValue() - mPago.getMonto());
							i++;
							bHecho = rcCtrl.grabarRC(cn, hFacSel.getId().getCodsuc(), hFacSel.getId().getCodcli(), hFacSel.getId().getTipofactura(), hFacSel.getId().getNofactura(), hFacSel.getId().getPartida(), hFacSel.getId().getRpdivj(), 
									 "RC", iNoReciboJDE, fecUtil.obtenerFechaJulianaDia(dFechaDelRecibo), "R", iNobatch, d.roundDouble(mPago.getMonto()), "D",  hFacSel.getMoneda(), 0, 0, 
									 hFacSel.getId().getCompenslm(), sCuentaCaja[1], hFacSel.getId().getCodunineg().trim(), hFacSel.getId().getTipopago(), "I", sConcepto, "", hFacSel.getId().getNomcli(), vaut.getId().getLogin(), vaut.getId().getNomapp(),hFacSel.getId().getRpddj(),hFacSel.getId().getRppo(),hFacSel.getId().getRpdcto());
																				
							if(!bHecho){
								strMensajeValidacion = "No se pudo grabar el recibo en el JDE!!! ==" + rcCtrl.getError() + " <BR/> DETALLE: " + rcCtrl.getErrorDetalle();		
								return false;
							}
							//se decremente el monto a apllicar a la factura 
							hFacSel.setMontoAplicar(new BigDecimal(dMontoF));
							hFacSel.getId().setCpendiente(new BigDecimal(d.roundDouble(hFacSel.getId().getCpendiente().doubleValue() -  mPago.getMonto())));
							
							dMontoAplicarxFactura = d.roundDouble(dMontoAplicarxFactura + mPago.getMonto());
							//LogCrtl.sendLogDebgs2("<===========> dMontoAplicarxFactura: "+dMontoAplicarxFactura+" mPago.getMonto(): "+mPago.getMonto() );
							break;
						}//el pago no cubre la factura
					}else{//no incluye la factura
						j++;
					}//no incluye la factura
				}//while de facturas
				
				if (j >= lstFacturasSelected.size()){ //ya no hay más facturas
					if (i == lstMetodosPago.size() -1) { //estoy en el último método de pago
						if (mPago.getMonto() <= 0.20) {
							++i;
						}
					}
				}
				
			}//pago en la misma moneda de la factura f:USD p:USD
		}else{
			strMensajeValidacion = d.getError() + " <BR/> DETALLE: " + d.getErrorDetalle();		
			return false;
		}
	}//while de metodos de pago
	
}//fin facturas en cor

				if(bHecho){
					//grabar batch de recibo en el F0011
					 
					bHecho = rcCtrl.registrarBatchA92(cn,"R", iNobatch,iTotalTransaccion, vaut.getId().getLogin(), 1, MetodosPagoCtrl.DEPOSITO);
					if(bHecho){
						bHecho = rcCtrl.fillEnlaceMcajaJde(s,tx,iNumRecCredito, hFac.getId().getCodcomp(), iNoReciboJDE, iNobatch,f55ca01.getId().getCaid(),f55ca01.getId().getCaco(),"R","CR");							
						
						if(bHayFicha && bHecho){
							String[] sCuentaCajaDom = d.obtenerCuentaCaja(f55ca01.getId().getCaid(),hFac.getId().getCodcomp(), MetodosPagoCtrl.EFECTIVO ,sMonedaBase,null,null,null,null);
							sCuentaCaja = d.obtenerCuentaCaja(f55ca01.getId().getCaid(),hFac.getId().getCodcomp(), MetodosPagoCtrl.EFECTIVO ,"USD",null,null,null,null);
							
							bHecho = generarAsientosFichaCV(s,
									tx, cn, lstPagoFicha,
									f55ca01, hFac, sTipoCliente, vaut,
									sCuentaCaja,
									sCuentaCajaDom, 
									iNumeroFichaCambio,
									iNumRecCredito);
							if(!bHecho){
								strMensajeValidacion = "No se pudo Grabar la ficha de compra venta!!!";
								return false;
							}							
						}							
				
						if(!bHecho){
							strMensajeValidacion = rcCtrl.getError() + " <BR/> DETALLE: " + rcCtrl.getErrorDetalle();		
							return false;
						}else{
						
							//ACTUALIZAR INFO DEL ULTIMO PAGO DEL CLIENTE
							Pago p = null;
							p = empCtrl.getultimoPago(cn, hFac.getId().getCodcli());
							if(p != null){
								if(p.getFecha() == null){
									bHecho = empCtrl.updUltimoPago(cn, hFac.getId().getCodcli(), iTotalTransaccion);
								}else if (p.getFecha().equals(new Date())){
									iTotalTransaccion = iTotalTransaccion + d.pasarAentero(p.getMonto().doubleValue());
									bHecho = empCtrl.updUltimoPago(cn, hFac.getId().getCodcli(), iTotalTransaccion);
								}else{
									bHecho = empCtrl.updUltimoPago(cn, hFac.getId().getCodcli(), iTotalTransaccion);
								}
								if(!bHecho){
									strMensajeValidacion = empCtrl.getError() + " <BR/> DETALLE: " + empCtrl.getErrorDetalle();		
									return false;
								}
							}
						}
						
					}else{							
						strMensajeValidacion = rcCtrl.getError() + " <BR/> DETALLE: " + rcCtrl.getErrorDetalle();		
						return false;
					}
				}
			} else {
				strMensajeValidacion = rcCtrl.getError()
						+ " <BR/> DETALLE: " + rcCtrl.getErrorDetalle();
				return false;
			}
		} else {// validacion de numero de batch
			return false;
		}// validacion de numero de batch
		// estaba el metodo validate
	} catch (Exception ex) {
		strMensajeValidacion = "Error de sistema al aplicar pago a facturas de credito"; bHecho = false;
		ex.printStackTrace();
	}
	return bHecho;
}

	// METODO PARA VALIDAR DESCUANDRE DE F0311
			
	private void validateAccount(Credhdr f, int noFactura, int ncaja,
			String sMontoRestante) {
		try {

			List<Object[]> result = com.casapellas.controles.tmp.ReciboCtrl
					.ValidateCustomerAccount(f.getId().getCodcli(), noFactura,
							ncaja);

			String strBody = "Cliente descuadrado  -> Codigo: "
					+ "@codCliente --> No.Factura: "
					+ "@noFactura <br/> Caja: " + "@noCaja <br/>";
  ;
			if (!result.isEmpty()
					&& String.valueOf(((Object[]) result.get(0))[2])
							.compareToIgnoreCase("0.00") != 0) {

				strBody += "Monto Calculado: "
						+ "@montoCalculado <br/> Monto Restante : "
						+ "@montoRestante";
				strBody = strBody
						.replace("@codCliente", f.getId().getCodcli() + "")
						.replace("@noFactura", noFactura + "")
						.replace("@noCaja", ncaja + "")
						.replace("@montoCalculado", result.get(0)[0] + "")
						.replace("@montoRestante", result.get(0)[1] + "");
				sendMail(strBody,
						"Cliente descuadrado Pago Credito R2 - Saldo vs Detalle - Ambiente: "
								+ PropertiesSystem.JDEDTA + " server: "
								+ new PropertiesSystem().URLSIS, result);
			} else if (!result.isEmpty()
					&& String.valueOf(result.get(0)[1]).compareToIgnoreCase(
							sMontoRestante) == 0) {
				strBody = strBody
						.replace("@codCliente", f.getId().getCodcli() + "")
						.replace("@noFactura", noFactura + "")
						.replace("@noCaja", ncaja + "");
				sendMail(strBody,
						"Cliente descuadrado Pago Credito R2 - F0311 NO MODIFICADO - Ambiente: "
								+ PropertiesSystem.JDEDTA + " server: "
								+ new PropertiesSystem().URLSIS, result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void sendMail(String strBody, String strTitle, List<Object[]> result) {
		try {

			if (result != null && result.size() > 0) {
				for (int i = 1; i < result.size(); i++) {
					strBody += "<br/> Metodo de Pago " + i + " : "
							+ result.get(i)[1] + " Moneda: " + result.get(i)[0]
							+ " Monto: " + result.get(i)[2];
				}
			}
			
			String strInternalEmail = PropertiesSystem.MAIL_INTERNAL_ADDRESS;
			String[] listEmail = strInternalEmail.split(",");
			
			List<CustomEmailAddress> toList = new ArrayList<CustomEmailAddress>();
			for (String strEmail: listEmail) {
				toList.add(new CustomEmailAddress(strEmail));
			}
			
			if (toList.size() > 0) {
				MailHelper.SendHtmlEmail(
						new CustomEmailAddress(PropertiesSystem.WEBMASTER_EMAIL_ADRESS, "Módulo de Caja"),
						toList, null, new CustomEmailAddress(PropertiesSystem.MAIL_BOUNCEADDRESS), 
						strTitle, strBody);
			}
 		} catch (Exception e) { 
			e.printStackTrace();
		}
	}	
	
	
	
	public boolean grabarReciboCredito_FDC(Session s, Transaction tx,  
			List<MetodosPago> lstMetodosPago,Vf55ca01 f55ca01, 
			List<Credhdr> lstFacturasSelected, int numeroRecibo, 
			List lstPagoFicha, BigDecimal bdTasaOficial, BigDecimal bdTasaParalela, 
			Vautoriz vaut, String sMonedaBase, Date dFechaDelRecibo){
		
		boolean aplicado = true;
		String msgProceso = "" ;
		
		try {
			
			
			int caid = f55ca01.getId().getCaid() ;
			String codsuc = f55ca01.getId().getCaco();
			String codcomp = lstFacturasSelected.get(0).getId().getCodcomp();
			String tiporec = valoresJDEInsCredito[0];
			
			List<String> numerosRecibosJde = new ArrayList<String>();
			
			for (int i = 0; i < lstMetodosPago.size(); i++) {
				int numeroReciboJde = Divisas.numeroSiguienteJdeE1Custom(valoresJdeNumeracion[8],valoresJdeNumeracion[9] );
				numerosRecibosJde.add(String.valueOf(numeroReciboJde));
			}
			
			int numeroBatchJde = Divisas.numeroSiguienteJdeE1(  );
			int numeroReciboJde = Integer.parseInt( numerosRecibosJde.get(0) ) ;
			
			ProcesarPagoFacturaJdeCustom ppf = new  ProcesarPagoFacturaJdeCustom() ;
			
			ppf.caid = caid ;
			ppf.codcomp = codcomp ;
			ppf.tiporec = tiporec ;
			ppf.numrec = numeroRecibo;
			ppf.fecharecibo = dFechaDelRecibo ; 
			
			ppf.facturas = lstFacturasSelected ;
			ppf.formasdepago = lstMetodosPago ;
			ppf.tasaCambioRecibo = bdTasaOficial ;  
			ppf.tasaCambioOficial = bdTasaOficial;  
		
			ppf.numeroBatchJde = String.valueOf(numeroBatchJde);
			ppf.numeroReciboJde = String.valueOf(numeroReciboJde);
			ppf.numerosReciboJde = numerosRecibosJde;
		
			ppf.codigousuario = vaut.getId().getCodreg();
			ppf.usuario = vaut.getId().getLogin();
			ppf.programaActualiza = PropertiesSystem.CONTEXT_NAME;
			
			ppf.valoresJDEInsCredito=valoresJDEInsCredito;
			
			ppf.msgProceso = "";
			
			ppf.procesarPagosFacturas(s);
			
			aplicado = ppf.msgProceso.isEmpty();
			
			if(!aplicado){
				
				msgProceso = ppf.msgProceso ;
				
				if(msgProceso == null || msgProceso.isEmpty() ){
					 msgProceso = "Error al crear recibo en JdEdward's por factura de Crédito ";
				 }
				 
				 return aplicado = false ;
			}
			
			for (String numrecjde : numerosRecibosJde) {

				numeroReciboJde = Integer.valueOf(numrecjde);
				
				aplicado =  ReciboCtrl.grabarAsociacionCajaJDE(s, numeroRecibo, codcomp, numeroReciboJde, numeroBatchJde, caid, codsuc,"R", tiporec);
				 
				if(!aplicado){
					 msgProceso = "Error al generar registro de asociación de Caja a JdEdward's @recibojde" ;
					 return aplicado = false  ;
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			 
		}finally{
			
			if( !aplicado ){
				
				strMensajeValidacion = msgProceso;		
				lblMensajeValidacion.setValue(msgProceso);	
				
			}
			
			
		}
		
		return aplicado;
	}
	
	
	
	public boolean grabarCredito_fdc(Session s,Transaction tx,Connection cn,List lstMetodosPago,Vf55ca01 f55ca01,List lstFacturasSelected, int iNumRecCredito, 
			List lstPagoFicha,BigDecimal bdTasaJde,BigDecimal bdTasaPar,Vautoriz vaut, String sMonedaBase, Date dFechaDelRecibo) {
		boolean bHecho = false, lHacerBreak;
		String sConcepto = "";
		int iNobatch = 0, iNoReciboJDE = 0, i = 0, j = 0, iTotalTransaccion = 0, iCinf = 0;
		EmpleadoCtrl empCtrl = new EmpleadoCtrl();
		Credhdr hFac = null, hFacSel = null;
		Divisas d = new Divisas();
		ReciboCtrl rcCtrl = new ReciboCtrl();
		MetodosPago mPago = null;
		String[] sCuentaCaja = null;
		FechasUtil fecUtil = new FechasUtil();
		double dMontoD = 0, dMontoF = 0, dMontoDif = 0, dNewPendienteForaneo = 0, dNewPendienteDom = 0;
		double d1 = 0, d2 = 0;

		try {
			hFac = (Credhdr) lstFacturasSelected.get(0);
			sConcepto = "R:" + iNumRecCredito + " Ca:" + f55ca01.getId().getCaid() + " Com:" + hFac.getId().getCodcomp();

			//Obtener # de batch para la transacción.
			iNobatch = d.leerActualizarNoBatch();

			if (iNobatch > 0) {
				iNoReciboJDE = rcCtrl.leerNumeroRpdocm(true, hFac.getId().getCodcomp());
				if (iNoReciboJDE > 0) {
					if (hFac.getMoneda().equals(sMonedaBase)) { //factura:COR == MonedaBase:COR
						while (i < lstMetodosPago.size()) {
							if (++iCinf > 999) {
								strMensajeValidacion = "No se pudo Completar la operación!!! Ciclo infinito en recibos de credito!!!";
								lblMensajeValidacion.setValue(strMensajeValidacion);
								return false;
							}// seguro contra ciclo infinito
							mPago = (MetodosPago) lstMetodosPago.get(i);
							// obtener cuentas a usar por metodos de pago y moneda
							sCuentaCaja = d.obtenerCuentaCaja(f55ca01.getId().getCaid(), hFac.getId().getCodcomp(), mPago.getMetodo(), mPago.getMoneda(), null, null, null, null);
							if (sCuentaCaja != null) {
								if (!mPago.getMoneda().equals(sMonedaBase)) { //pago:USD MonedaBase:COR
									iTotalTransaccion += d.pasarAentero(mPago.getEquivalente());
									while (j < lstFacturasSelected.size()) {
										hFacSel = (Credhdr) lstFacturasSelected.get(j);

										//Pago >= Monto de Factura. Trabajo con el equivalente(COR) porque el pago viene en usd.
										if (d.roundDouble(mPago.getEquivalente()) >= d.roundDouble(hFacSel.getMontoAplicar().doubleValue())) {
											//dMontoF es lo que me queda del pago que todavía pudiera aplicar a otra factura
											dMontoF = d.roundDouble(mPago.getEquivalente() - hFacSel.getMontoAplicar().doubleValue());
											
											bHecho = rcCtrl.grabarRC(cn, hFacSel.getId().getCodsuc(), hFacSel.getId().getCodcli(), hFacSel.getId().getTipofactura(), hFacSel.getId().getNofactura(), hFacSel.getId().getPartida(), hFacSel.getId().getRpdivj(), 
										             "RC", iNoReciboJDE, fecUtil.obtenerFechaJulianaDia(dFechaDelRecibo), "R", iNobatch, d.roundDouble(hFacSel.getMontoAplicar().doubleValue()), "D",  hFacSel.getMoneda(), 0, 0, 
										             hFacSel.getId().getCompenslm(), sCuentaCaja[1], hFacSel.getId().getCodunineg().trim(), hFacSel.getId().getTipopago(), "I", sConcepto, "", hFacSel.getId().getNomcli(), vaut.getId().getLogin(), vaut.getId().getNomapp(),hFacSel.getId().getRpddj(),hFacSel.getId().getRppo(),hFacSel.getId().getRpdcto());

											if(bHecho) {
												bHecho = rcCtrl.aplicarMontoF0311_fdc(cn, d.roundDouble(hFacSel.getMontoAplicar().doubleValue()), hFacSel.getMoneda(), sMonedaBase, vaut.getId().getLogin(), hFacSel.getId().getCodsuc(), hFacSel.getId().getTipofactura(), hFacSel.getId().getNofactura(), hFacSel.getId().getPartida(), hFacSel.getId().getCodcli());
												if(!bHecho){
													strMensajeValidacion = rcCtrl.getError() + " <BR/> NO SE PUDO APLICAR VALOR AL F0311. DETALLE: " + rcCtrl.getErrorDetalle();		
													lblMensajeValidacion.setValue(strMensajeValidacion);	
													return false;
												}
												lHacerBreak = false;
												if(d.roundDouble(mPago.getEquivalente()) == d.roundDouble(hFacSel.getMontoAplicar().doubleValue())) {
													i++; //Incremento el contador del Metodo de Pago 
												    lHacerBreak = true;
												}
												mPago.setEquivalente(dMontoF); //lo que me queda del pago que todavía puedo aplicar a otra factura.
												hFacSel.setMontoAplicar(BigDecimal.ZERO); //porque la facura es cubierta totalmente por el pago.
												j++; //siempre incremento el contador de factura porque es cubierta totalmente por el pago.
												if (lHacerBreak) {
													break; //salgo del ciclo de las facturas
												}
											} else {
												strMensajeValidacion = "No se pudo grabar el recibo en el JDE!!! ==" + rcCtrl.getError() + " <BR/> DETALLE: " + rcCtrl.getErrorDetalle();		
												lblMensajeValidacion.setValue(strMensajeValidacion);
												return false;
											}
										}     //fin    -> el pago >= que la factura											
										else{ //inicio -> el pago <  que la factura
											dMontoF = d.roundDouble(hFacSel.getMontoAplicar().doubleValue() - mPago.getEquivalente());
											
											i++;
											
											//d.roundDouble(mPago.getEquivalente()) --> dMontoAplicar        --> RPAG,  RPAAP
											//                                   0  --> dMontoAplicarForaneo --> RPACR, RPFAP
											bHecho = rcCtrl.grabarRC(cn, hFacSel.getId().getCodsuc(), hFacSel.getId().getCodcli(), hFacSel.getId().getTipofactura(), hFacSel.getId().getNofactura(), hFacSel.getId().getPartida(), hFacSel.getId().getRpdivj(), 
													 "RC", iNoReciboJDE, fecUtil.obtenerFechaJulianaDia(dFechaDelRecibo), "R", iNobatch, d.roundDouble(mPago.getEquivalente()), "D",  hFacSel.getMoneda(), 0,0, 
													 hFacSel.getId().getCompenslm(), sCuentaCaja[1], hFacSel.getId().getCodunineg().trim(), hFacSel.getId().getTipopago(), "I", sConcepto, "", hFacSel.getId().getNomcli(), vaut.getId().getLogin(), vaut.getId().getNomapp(),hFacSel.getId().getRpddj(),hFacSel.getId().getRppo(),hFacSel.getId().getRpdcto());
											
											if (bHecho) {
												bHecho = rcCtrl.aplicarMontoF0311_fdc(cn, d.roundDouble(mPago.getEquivalente()), hFacSel.getMoneda(), sMonedaBase, vaut.getId().getLogin(), hFacSel.getId().getCodsuc(), hFacSel.getId().getTipofactura(), hFacSel.getId().getNofactura(), hFacSel.getId().getPartida(), hFacSel.getId().getCodcli());
												if(!bHecho) {
													strMensajeValidacion = rcCtrl.getError() + " <BR/> NO SE PUDO APLICAR VALOR AL F0311. DETALLE: " + rcCtrl.getErrorDetalle();		
													lblMensajeValidacion.setValue(strMensajeValidacion);	
													return false;
												}
											} else {
												strMensajeValidacion = "No se pudo grabar el recibo en el JDE!!! ==" + rcCtrl.getError() + " <BR/> DETALLE: " + rcCtrl.getErrorDetalle();		
												lblMensajeValidacion.setValue(strMensajeValidacion);
												return false;
											}
											hFacSel.setMontoAplicar(new BigDecimal(dMontoF)); //se decremente el monto a apllicar a la factura 
											break;
										}   //fin el pago < que la factura										
									}  //fin -> while de facturas
									if (j >= lstFacturasSelected.size()){ //ya no hay más facturas
										if (i == lstMetodosPago.size() -1) { //estoy en el último método de pago
											if (mPago.getEquivalente() <= 0.20) {
												++i;
											}
										}
									}
								} else {//pago en la misma moneda de la factura pago:COR MonedaBase:COR //es el else de if (!mPago.getMoneda().equals(sMonedaBase)) { //pago:USD MonedaBase:COR
									iTotalTransaccion += d.pasarAentero(mPago.getMonto());
									while (j < lstFacturasSelected.size()) {
										if(++iCinf > 999) {
											strMensajeValidacion = "No se pudo Completar la operación!!! Ciclo infinito en recibos de credito!!!";	
										    lblMensajeValidacion.setValue(strMensajeValidacion);
										    return false;
										}
										hFacSel = (Credhdr)lstFacturasSelected.get(j);
										//Pago >= Monto Factura
										if (d.roundDouble(mPago.getMonto()) >= d.roundDouble(hFacSel.getMontoAplicar().doubleValue())) {												
											dMontoF = d.roundDouble(mPago.getMonto() - hFacSel.getMontoAplicar().doubleValue());																						
											bHecho = rcCtrl.grabarRC(cn, hFacSel.getId().getCodsuc(), hFacSel.getId().getCodcli(), hFacSel.getId().getTipofactura(), hFacSel.getId().getNofactura(), hFacSel.getId().getPartida(), hFacSel.getId().getRpdivj(), 
													             "RC", iNoReciboJDE, fecUtil.obtenerFechaJulianaDia(dFechaDelRecibo), "R", iNobatch, d.roundDouble(hFacSel.getMontoAplicar().doubleValue()), "D",  hFacSel.getMoneda(), 0, 0, 
													             hFacSel.getId().getCompenslm(), sCuentaCaja[1], hFacSel.getId().getCodunineg().trim(), hFacSel.getId().getTipopago(), "I", sConcepto, "", hFacSel.getId().getNomcli(), vaut.getId().getLogin(), vaut.getId().getNomapp(),hFacSel.getId().getRpddj(),hFacSel.getId().getRppo(),hFacSel.getId().getRpdcto());											
											if (bHecho) {
												bHecho = rcCtrl.aplicarMontoF0311_fdc(cn, d.roundDouble(hFacSel.getMontoAplicar().doubleValue()), hFacSel.getMoneda(), sMonedaBase, vaut.getId().getLogin(), hFacSel.getId().getCodsuc(), hFacSel.getId().getTipofactura(), hFacSel.getId().getNofactura(), hFacSel.getId().getPartida(),hFacSel.getId().getCodcli());
												if(!bHecho) {
													strMensajeValidacion = rcCtrl.getError() + " <BR/> DETALLE: " + rcCtrl.getErrorDetalle();		
													lblMensajeValidacion.setValue(strMensajeValidacion);																
													return false;
												}
												lHacerBreak = false;
												if(d.roundDouble(mPago.getMonto()) == d.roundDouble(hFacSel.getMontoAplicar().doubleValue())) {
												    i++; //Incremento el contador del Metodo de Pago 
												    lHacerBreak = true; 
												}
												mPago.setMonto(dMontoF);
												hFacSel.setMontoAplicar(BigDecimal.ZERO);
												j++; //siempre incremento el contador de factura porque es cubierta totalmente por el pago.
												if (lHacerBreak) {
													break; //salgo del ciclo de las facturas
												}
											}else{
												strMensajeValidacion = "No se pudo grabar el recibo en el JDE!!! ==" + rcCtrl.getError() + " <BR/> DETALLE: " + rcCtrl.getErrorDetalle();		
												lblMensajeValidacion.setValue(strMensajeValidacion);
												return false;
											}
										}      //fin    -> el pago >= que la factura											
										else { //inicio -> el pago <  que la factura
											dMontoF = d.roundDouble(hFacSel.getMontoAplicar().doubleValue() - mPago.getMonto());
											bHecho = rcCtrl.grabarRC(cn, hFacSel.getId().getCodsuc(), hFacSel.getId().getCodcli(), hFacSel.getId().getTipofactura(), hFacSel.getId().getNofactura(), hFacSel.getId().getPartida(), hFacSel.getId().getRpdivj(), 
													 "RC", iNoReciboJDE, fecUtil.obtenerFechaJulianaDia(dFechaDelRecibo), "R", iNobatch, d.roundDouble(mPago.getMonto()), "D",  hFacSel.getMoneda(), 0, 0, 
													 hFacSel.getId().getCompenslm(), sCuentaCaja[1], hFacSel.getId().getCodunineg().trim(), hFacSel.getId().getTipopago(), "I", sConcepto, "", hFacSel.getId().getNomcli(), vaut.getId().getLogin(), vaut.getId().getNomapp(),hFacSel.getId().getRpddj(),hFacSel.getId().getRppo(),hFacSel.getId().getRpdcto());

											if (bHecho) {
												bHecho = rcCtrl.aplicarMontoF0311_fdc(cn, d.roundDouble(mPago.getMonto()), hFacSel.getMoneda(), sMonedaBase, vaut.getId().getLogin(), hFacSel.getId().getCodsuc(), hFacSel.getId().getTipofactura(), hFacSel.getId().getNofactura(), hFacSel.getId().getPartida(), hFacSel.getId().getCodcli());
												if(!bHecho) {
													strMensajeValidacion = rcCtrl.getError() + " <BR/> NO SE PUDO APLICAR VALOR AL F0311. DETALLE: " + rcCtrl.getErrorDetalle();		
													lblMensajeValidacion.setValue(strMensajeValidacion);	
													return false;
												}
											} else {
												strMensajeValidacion = "No se pudo grabar el recibo en el JDE!!! ==" + rcCtrl.getError() + " <BR/> DETALLE: " + rcCtrl.getErrorDetalle();		
												lblMensajeValidacion.setValue(strMensajeValidacion);
												return false;
											}
											hFacSel.setMontoAplicar(new BigDecimal(dMontoF)); //se decremente el monto a aplicar a la factura
											i++;
											break; //incremento el contador del pago y hago el break (
											       //el break quiere decir que voy a procesar un nuevo método de pago y voy a quedar en la misma factura.
										} //el pago no cubre la factura
									
									}//while de facturas
									
									if (j >= lstFacturasSelected.size()){ //ya no hay más facturas
										if (i == lstMetodosPago.size() -1) { //estoy en el último método de pago
											if (mPago.getMonto() <= 0.20) {
												++i;
											}
										}
									}
									
								} //fin -> pago en la misma moneda de la factura f:COR p: COR
							} else {
								strMensajeValidacion = d.getError() + " <BR/> DETALLE: " + d.getErrorDetalle();		
								lblMensajeValidacion.setValue(strMensajeValidacion);	
								return false;
							}
						}//while de metodos de pago
						
					}//fin facturas en cor

					if(bHecho){
						//grabar batch de recibo en el F0011
						bHecho = rcCtrl.registrarBatchA92(cn, "R", iNobatch, iTotalTransaccion, vaut.getId().getLogin(), 1, MetodosPagoCtrl.DEPOSITO);
						if(bHecho){
							bHecho = rcCtrl.fillEnlaceMcajaJde(s, tx, iNumRecCredito, hFac.getId().getCodcomp(), iNoReciboJDE, iNobatch, f55ca01.getId().getCaid(), f55ca01.getId().getCaco(), "R", "CR");							
							
							if(!bHecho){
								strMensajeValidacion = rcCtrl.getError() + " <BR/> DETALLE: " + rcCtrl.getErrorDetalle();		
								lblMensajeValidacion.setValue(strMensajeValidacion);	
								return false;
							}else{
							
								//ACTUALIZAR INFO DEL ULTIMO PAGO DEL CLIENTE
								Pago p = null;
								p = empCtrl.getultimoPago(cn, hFac.getId().getCodcli());
								if (p != null) {
									if (p.getFecha() == null) {
										bHecho = empCtrl.updUltimoPago(cn, hFac.getId().getCodcli(), iTotalTransaccion);
									} else if (p.getFecha().equals(new Date())) {
										iTotalTransaccion = iTotalTransaccion + d.pasarAentero(p.getMonto().doubleValue());
										bHecho = empCtrl.updUltimoPago(cn, hFac.getId().getCodcli(), iTotalTransaccion);
									} else {
										bHecho = empCtrl.updUltimoPago(cn, hFac.getId().getCodcli(), iTotalTransaccion);
									}
									if (!bHecho) {
										strMensajeValidacion = empCtrl.getError() + " <BR/> DETALLE: " + empCtrl.getErrorDetalle();		
										lblMensajeValidacion.setValue(strMensajeValidacion);
										return false;
									}
								}
							}
						} else {							
							strMensajeValidacion = rcCtrl.getError() + " <BR/> DETALLE: " + rcCtrl.getErrorDetalle();		
							lblMensajeValidacion.setValue(strMensajeValidacion);
							return false;
						}
					}
				} else {					
					strMensajeValidacion = rcCtrl.getError() + " <BR/> DETALLE: " + rcCtrl.getErrorDetalle();		
					lblMensajeValidacion.setValue(strMensajeValidacion);
					return false;
				}
			} else {//validacion de numero de  batch				
				strMensajeValidacion = d.getError() + " <BR/> DETALLE: " + d.getErrorDetalle();		
				lblMensajeValidacion.setValue(strMensajeValidacion);	
				return false;
			}//validacion de numero de batch
		} catch (Exception ex) {
			lblMensajeValidacion.setValue("Se capturo una excepcion en FacCreditoDAO.grabarCredito:" + ex);
			ex.printStackTrace();
		}
		return bHecho;
	}
	
/****************************************************************************************************/
	public BigDecimal obtenerTasaParalela(String sMoneda){
		BigDecimal tasa = BigDecimal.ONE;
		Tpararela[] tcPar;
		try {
			tcPar = (Tpararela[]) m.get("tpcambio");
			// buscar tasa de cambio paralela para moneda
			for (int t = 0; t < tcPar.length; t++) {
				if (tcPar[t].getId().getCmono().equals(sMoneda) || tcPar[t].getId().getCmond().equals(sMoneda)) {
					tasa = tcPar[t].getId().getTcambiom();
					break;
				}
			}
		} catch (Exception e) {

			strMensajeValidacion = "Problemas Obteniendo Tasa Paralela!!!";	
			lblMensajeValidacion.setValue(strMensajeValidacion);
			// TODO: handle exception
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
	/*************INSERTAR FICHA DE COMPRA/VENTA******************************************************************/	
	public boolean insertarFichaCV(Session session, Transaction tx,
				Vf55ca01 f55ca01,Credhdr hFac, Vautoriz vautoriz,double dMonto, 
				int iNumrec, String sCajero,List lstPagoFicha){
		boolean bInsertado = false;
		NumcajaCtrl numCtrl = new NumcajaCtrl();
		int iNoFicha = 0;
		ReciboCtrl conCtrl = new ReciboCtrl();
		String sMotivoTc = "";
		try{
			
			//---- Si hubo cambio de tasa, leer el motivo.
			sMotivoTc = (m.get("fcr_MotivoCambioTasa")!=null)? m.get("fcr_MotivoCambioTasa").toString(): " ";
			
			//obtener y actualizar el numero de ficha a utilizar
			iNoFicha = numCtrl.obtenerNoSiguiente("FICHACV", f55ca01.getId().getCaid(), hFac.getId().getCodcomp(), f55ca01.getId().getCaco(), vautoriz.getId().getLogin());
			if(iNoFicha > 0){
				
				iNumeroFichaCambio = iNoFicha;
				
				//registrar encabezado de ficha
				bInsertado = conCtrl.registrarRecibo(session, tx, iNoFicha, 0,
						hFac.getId().getCodcomp(), dMonto, dMonto, 0, "",
						new Date(), new Date(), hFac.getId().getCodcli(),
						hFac.getNomcli(), sCajero, f55ca01.getId().getCaid(),
						f55ca01.getId().getCaco(), vautoriz.getId().getLogin(),
						"FCV", 0, "", iNumrec, new Date(), hFac.getId()
								.getCodunineg().trim(), sMotivoTc, hFac.getId().getMoneda());
				
				if (bInsertado){
					//registrar detalle de ficha
					bInsertado = conCtrl.registrarDetalleRecibo(session, tx, iNoFicha, 0, hFac.getId().getCodcomp(), lstPagoFicha,f55ca01.getId().getCaid(),f55ca01.getId().getCaco(),"FCV");
					
					if(!bInsertado){
						strMensajeValidacion = conCtrl.getError() + " <BR/> DETALLE: " + conCtrl.getErrorDetalle();					
					}
				}else{
					strMensajeValidacion = conCtrl.getError() + " <BR/> DETALLE: " + conCtrl.getErrorDetalle();		

				}
			}else{
				strMensajeValidacion = "Numeración de Compra/Venta no configurada";	
				bInsertado = false;
			}
		}catch(Exception ex){
			bInsertado = false;
			strMensajeValidacion = "Error de sistema generado al grabar compra venta de Monedas";
			ex.printStackTrace();
		}
		return bInsertado;
	}
	
/***********************************************************************************************************/
	/*************REALIZAR ASIENTOS DE COMPRA/VENTA DE FICHA********************************************************/	
	public boolean generarAsientosFichaCV(Session session, Transaction tx, Connection cn,List lstPagoFicha, Vf55ca01 f55ca01, Credhdr hFac,
			String sTipoCliente, Vautoriz vaut, String[] sCuentaMetodo, String[] sCtaMetodoDom, int iNumFicha,int iNumrec) {
		int iContadorDom = 0, iContadorFor = 0, iNoBatch = 0, iNoDocDomestico = 0, iNoDocForaneo = 0;
		boolean bContabilizado = false;
		ReciboCtrl recCtrl = new ReciboCtrl();
		Divisas d = new Divisas();
		MetodosPago mPago = null;
		String sConcepto = "",sCodsuc = "",sSucursaldeAsiento="";
		Date dtFecha = new Date();
		
		try {
			sSucursaldeAsiento = sCuentaMetodo[2];
			sConcepto = "Ficha No:" + iNumFicha + " Ca:" + f55ca01.getId().getCaid() + " Com:" + hFac.getId().getCodcomp();
			// leer y actualizar numero de batch a utilizar para los documentos
			// leer y actualizar el numero de batch
			iNoBatch = d.leerActualizarNoBatch();

			// leer y actualizar el numero de documento para los asientos
			// domesticos
			iNoDocDomestico = d.leerActualizarNoDocJDE();

			// leer y actualizar los asientos para los documentos foraneos
			iNoDocForaneo = d.leerActualizarNoDocJDE();

			mPago = (MetodosPago) lstPagoFicha.get(0);
			sCodsuc =  hFac.getId().getCodsuc().substring(3, 5);
			// vender dolares (1)
			iContadorFor++;
			bContabilizado = recCtrl.registrarAsientoDiario(dtFecha, cn, sSucursaldeAsiento,valoresJdeInsContado[1], iNoDocForaneo, (iContadorFor) * 1.0,iNoBatch, 
								sCuentaMetodo[0], sCuentaMetodo[1], sCuentaMetodo[3], sCuentaMetodo[4],sCuentaMetodo[5],valoresJdeInsContado[2], mPago.getMoneda(), 
								d.pasarAentero(d.roundDouble(mPago.getMonto())), sConcepto, vaut.getId().getLogin(), vaut.getId().getCodapp(), mPago.getTasa(), sTipoCliente, "Deb caja dolares "
							+ d.roundDouble(mPago.getMonto()), sCuentaMetodo[2],"","",valoresJdeInsContado[3],sCuentaMetodo[2],valoresJdeInsContado[4]);
			if (bContabilizado) {
				bContabilizado = recCtrl.registrarAsientoDiario(dtFecha, cn, sSucursaldeAsiento,valoresJdeInsContado[1],iNoDocForaneo, (iContadorFor) * 1.0, iNoBatch,
								sCuentaMetodo[0], sCuentaMetodo[1], sCuentaMetodo[3], sCuentaMetodo[4], sCuentaMetodo[5], valoresJdeInsContado[5], mPago.getMoneda(),
								d.pasarAentero(d.roundDouble(mPago.getEquivalente())), sConcepto,vaut.getId().getLogin(), vaut.getId().getCodapp(), mPago.getTasa(),
								sTipoCliente, "Deb caja dolares " + d.roundDouble(mPago.getMonto()), 
								sCuentaMetodo[2],"","",valoresJdeInsContado[6],sCuentaMetodo[2],valoresJdeInsContado[4]);
				if (bContabilizado) {
					bContabilizado = recCtrl.registrarAsientoDiario(dtFecha, cn, sSucursaldeAsiento, valoresJdeInsContado[1], iNoDocForaneo,(iContadorFor + 1) * 1.0, iNoBatch, 
									sCtaMetodoDom[0],sCtaMetodoDom[1],sCtaMetodoDom[3],sCtaMetodoDom[4],sCtaMetodoDom[5], valoresJdeInsContado[2], mPago.getMoneda(), (-1)* d.pasarAentero(d.roundDouble(mPago.getMonto())), sConcepto,vaut.getId().getLogin(), vaut.getId().getCodapp(),
									mPago.getTasa(), sTipoCliente,"Cred caja cordobas " + d.roundDouble(mPago.getMonto()),
									sCtaMetodoDom[2],"","",valoresJdeInsContado[3],sCtaMetodoDom[2],valoresJdeInsContado[4]);
					if (bContabilizado) {
						bContabilizado = recCtrl.registrarAsientoDiario(dtFecha, cn,sSucursaldeAsiento,valoresJdeInsContado[1], iNoDocForaneo,(iContadorFor + 1) * 1.0, iNoBatch,sCtaMetodoDom[0], sCtaMetodoDom[1], sCtaMetodoDom[3], sCtaMetodoDom[4],sCtaMetodoDom[5],
								valoresJdeInsContado[5], mPago.getMoneda(), (-1)* d.pasarAentero(d.roundDouble(mPago.getEquivalente())),sConcepto, vaut.getId().getLogin(), vaut
										.getId().getCodapp(), mPago.getTasa(),sTipoCliente, "Cred caja cordobas "+ d.roundDouble(mPago.getMonto()), 
										sCtaMetodoDom[2],"","",valoresJdeInsContado[6],sCtaMetodoDom[2],valoresJdeInsContado[4]);
						iContadorFor++;
						
						if(bContabilizado){
							bContabilizado = recCtrl.fillEnlaceMcajaJde(session,tx,iNumFicha, hFac.getId().getCodcomp(), iNoDocForaneo, iNoBatch,f55ca01.getId().getCaid(),f55ca01.getId().getCaco(),"A","FCV");
							if(bContabilizado){
								//bContabilizado = recCtrl.registrarBatch(cn,"G", iNoBatch, d.pasarAentero(d.roundDouble(mPago.getMonto())), vaut.getId().getLogin(), 1,MetodosPagoCtrl.DEPOSITO); VERSION A7.3
								bContabilizado = recCtrl.registrarBatchA92(cn,valoresJdeInsContado[8], iNoBatch, d.pasarAentero(d.roundDouble(mPago.getMonto())), vaut.getId().getLogin(), 1,MetodosPagoCtrl.DEPOSITO);
							}else {
								lblMensajeValidacion.setValue("No se pudo insertar el enlace entre la ficha y el documento de transacciones Foraneas!!!");
							}
						}else{
							lblMensajeValidacion.setValue("No se pudo realizar el asiento de diario de la ficha de CV");
						}		
						
					} else {
						lblMensajeValidacion.setValue("No se pudo realizar el asiento de diario de la ficha de CV");
					}
				} else {
					lblMensajeValidacion.setValue("No se pudo realizar el asiento de diario de la ficha de CV");
				}
			} else {
				lblMensajeValidacion.setValue("No se pudo realizar el asiento de diario de la ficha de CV");
			}
		} catch (Exception ex) {
			bContabilizado = false;
			ex.printStackTrace();
		}
		return bContabilizado;
	}
/****************************************************************************************************************************/
	/*********************************************************************************************************/
	public void ponerTasaSegunFecha(ValueChangeEvent ev){
		Date dFecha = null;
		FechasUtil fecUtil = new FechasUtil();
		int iFecha = 0;
		TasaCambioCtrl tcCtrl = new TasaCambioCtrl();
		Tcambio[] tcambio = null;
		String sTasa = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try{
			dFecha = (Date)txtFecham.getValue();
			String sFecha = sdf.format(dFecha);
			tcambio = tcCtrl.obtenerTasaCambioJDExFecha(sFecha);
			
			//poner tasa de cambio de JDE
			//Tcambio[] tcJDE = (Tcambio[])m.get("tcambio");
			for(int l = 0; l < tcambio.length;l++){
				sTasa = sTasa + " " + tcambio[l].getId().getCxcrdc() + ": " + tcambio[l].getId().getCxcrrd() + "<br/>";
																				
			}
			lblTasaJDE.setValue(sTasa);
			m.put("tcambio", tcambio);
			m.put("lblTasaJDE",sTasa);
			
			//----- Actualizar tasa paralela a la fecha actual.
			String sTasaCamP="";
			Tpararela[] tpcambio = null;
			tpcambio = tcCtrl.obtenerTasaCambioParalela(new Date());
			if(tpcambio!=null)
				sTasaCamP = " " + tpcambio[0].getId().getCmond()+ ": " + tpcambio[0].getId().getTcambiom();				
			else
				sTasaCamP = "USD: 1.0000";
			lblTasaCambio.setValue(sTasaCamP);
			m.put("tpcambio", tpcambio);
			m.put("lblTasaCambio",sTasaCamP);
			m.put("tasaCon","t");
			
			SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
			srm.addSmartRefreshId(lblTasaCambio.getClientId(FacesContext.getCurrentInstance()));
			
		}catch(Exception ex){
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
				
				String metodo = MetodosPagoCtrl.equivalenciaTipoPago( mp.getMetodo() );
				
				if( metodo.compareTo( MetodosPagoCtrl.EFECTIVO ) == 0 || metodo.compareTo(MetodosPagoCtrl.TARJETA ) == 0){
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
/*******************************************************************************************************/	
	public void realizarBusquedaFacturas(){
		String strParametro = null;
		int iFechaActual = 0;
		FacturaCreditoCtrl facCreCtrl = new FacturaCreditoCtrl();
		String sMoneda = "",sCodComp = "",sCodunineg = "", sCodSuc = "";
		Date dFechaDesde = null;
		Date dFechaHasta = null;
		try {
			String sMensaje2 = "";
			if(dcFechaDesde.getValue() != null){
				if (!dcFechaDesde.getValue().toString().equals("")){
					dFechaDesde = (Date)dcFechaDesde.getValue();
					
				}
			}
			if(dcFechaHasta.getValue()!= null){
				if (!dcFechaHasta.getValue().toString().equals("")){
					dFechaHasta = (Date)dcFechaHasta.getValue();
				}
			}
			
			if(txtParametroCredito.getValue() != null) {
				strParametro = txtParametroCredito.getValue().toString().trim();
				sMoneda = cmbFiltroMonedas.getValue().toString();
				sCodComp = ddlCompaniaCre.getValue().toString();
				sCodunineg = ddlUninegCred.getValue().toString();
				sCodSuc = ddlSucursalCred.getValue().toString();
				int busqueda = 1;
				if (m.get("strBusquedaCredito") != null){
					busqueda = Integer.parseInt((String)m.get("strBusquedaCredito"));
				}
				List result = new ArrayList();
				List lstHfacturasCredito = new ArrayList();	
				
				boolean bMostrarTodo = chkMostrarTodoSrch.isChecked();
				
				F55ca014[] f14 = (F55ca014[])m.get("cont_f55ca014");
				result = facCreCtrl.buscarFacturasCredito(busqueda,strParametro,sMoneda,
											dFechaDesde,dFechaHasta,
											sCodComp,sCodunineg,
											sCodSuc,f14,bMostrarTodo);
				
				if (result == null || result.isEmpty()){
							sMensaje2 = "No se encontraron resultados";
							getSMensaje();
							m.remove("mostrarCredito");
							m.put("lstHfacturasCredito",result);
				}else{		
					
					m.put("lstHfacturasCredito",result);
					List lstRefreshFacs = result;
					m.put("lstRefreshFacturas", result);

				}
			}
			
			gvHfacturasCredito.setPageIndex(0);
			gvHfacturasCredito.dataBind();
			txtMensaje.setValue(sMensaje2);
			m.put("sMensajeCre", sMensaje2);	
			intSelected.setValue("0");
			dwCargando.setWindowState("hidden");
			
		}catch(Exception ex){
			dwCargando.setWindowState("hidden");
			ex.printStackTrace();
		}
	}
/***********************VALUECHANGELISTENER DE CAMBIO DE MONEDA EN EL FILTRO DE MONEDA**************************/
	public void onFiltrosChange(ValueChangeEvent ev){
		try{
			realizarBusquedaFacturas();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
/***************************************************************************************************/
	public boolean validarSeleccion(List lstSelectedFacs, FacturaCredito hFacFirst){
		boolean valido = true;
		SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
		//lstSelectedRows = gvHfacturasCredito.getSelectedRows();
		Credhdr hFacSelected = null;
		//FacturaCredito hFacFirst = null;   
		//sacar rows seleccionados		
		RowItem row  = null;
		try{
			if (lstSelectedFacs.size() > 1){//validar facturas
								
				String sl1 = hFacFirst.getCodunineg().trim().substring(2,4);
				for(int i = 0;i < lstSelectedFacs.size(); i++){
					hFacSelected = (Credhdr)lstSelectedFacs.get(i);
					String sl2 = hFacSelected.getId().getCodunineg().trim().substring(2,4);
					//validar cliente
					if (hFacFirst.getCodcli() !=  hFacSelected.getId().getCodcli()){
						valido = false;
						lblValidaFactura.setValue(
								"<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El cliente de las facturas seleccionadas debe ser el mismo<br>" 
								);
						break;
					}
					
					else if(!hFacFirst.getMoneda().equals(hFacSelected.getMoneda())){
						valido = false;
						lblValidaFactura.setValue(
								"<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> La moneda de las facturas seleccionadas debe ser la misma<br>" 
								);
						break;
					}
					else if(hFacFirst.getCtotal() < 0 || hFacSelected.getId().getCpendiente().doubleValue() < 0){
						valido = false;
						lblValidaFactura.setValue(
								"<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> No es posible pagar facturas con montos negativos<br>" 
								);
						break;
					}
				}
			}
			if(!valido){
				
				dwValidacionFactura.setWindowState("normal");
				dwValidacionFactura.setStyle("height: 170px; visibility: visible; width: 365px");				
				srm.addSmartRefreshId(lblValidaFactura.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(dwValidacionFactura.getClientId(FacesContext.getCurrentInstance()));
			}						
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return valido;
	}
/***************************************************************************************************/	
/**************************************************************************************************/	
	public void onSelectRow(ActionEvent e){
		boolean valido = true;
		List lstFacturas = new ArrayList(),lstSelectedFacs = new ArrayList();
		FacturaCredito hFacSelected = null, hFacFirst = null, hFac = null,newHfac = null;   
		try{		
			lstFacturas = (List)m.get("lstHfacturasCredito");
			if(m.get("selectedFacsCredito") != null){
				lstSelectedFacs = (List)m.get("selectedFacsCredito");	
				
			}
			
			//obtener el elemento del grid
			RowItem ri = (RowItem)e.getComponent().getParent().getParent();
			hFac = (FacturaCredito)gvHfacturasCredito.getDataRow(ri);
			//validar seleccionados
			valido = validarSeleccion(lstSelectedFacs,hFac);
			if(valido){	
				if(hFac.isSelected()){
					hFac.setSelected(false);
					//Sacar el elemento de la Lista de seleccionados
					if(lstSelectedFacs != null && !lstSelectedFacs.isEmpty()){
						for(int a = 0; a < lstSelectedFacs.size();a++){
							hFacSelected = (FacturaCredito)lstSelectedFacs.get(a);
							if(hFac.getNofactura() == hFacSelected.getNofactura() && hFac.getTipofactura().equals(hFacSelected.getTipofactura()) && hFac.getPartida().equals(hFacSelected.getPartida())){
								lstSelectedFacs.remove(a);
							}
						}
					}		
				}else{
					hFac.setSelected(true);
					//Meter el elemento a la lista de seleccionados
					lstSelectedFacs.add(hFac);
				}		
				
				//actualizar la lista del grid
				for(int i = 0; i < lstFacturas.size();i++){
					newHfac = (FacturaCredito)lstFacturas.get(i);
					if(hFac.getNofactura() == newHfac.getNofactura() && hFac.getTipofactura().equals(newHfac.getTipofactura()) && hFac.getPartida().equals(newHfac.getPartida())){
						lstFacturas.remove(i);
						//lstFacturas.add(i,hFac);
						break;
					}
				}
				m.put("lstHfacturasCredito", lstFacturas);
				m.put("selectedFacsCredito", lstSelectedFacs);
				gvHfacturasCredito.dataBind();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
/****************SELECT ROW CHANGE LISTENER DE FACTURAS DE CREDITO****************************************************/
	public void getFacturasCredito(SelectedRowsChangeEvent e) {

		boolean valido = true;
		List lstSelectedRows = new ArrayList();	
		List lstSelectedFacs = new ArrayList();
		
		try{
			SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
			lstSelectedRows = gvHfacturasCredito.getSelectedRows();
			Credhdr hFacSelected = null;
			Credhdr hFacFirst = null;   
			
			intSelected.setValue(lstSelectedRows.size());
		
			//sacar rows seleccionados
			RowItem row  = null;
				
			if (lstSelectedRows.size() > 1){//validar facturas
				row = (RowItem)lstSelectedRows.get(0);
				hFacFirst = (Credhdr)gvHfacturasCredito.getDataRow(row);
				lstSelectedFacs.add(hFacFirst);
				String sl1 = hFacFirst.getId().getCodunineg().trim().substring(2,4);
				for(int i = 1;i < lstSelectedRows.size(); i++){
					row = (RowItem)lstSelectedRows.get(i);
					hFacSelected = (Credhdr)gvHfacturasCredito.getDataRow(row);
					String sl2 = hFacSelected.getId().getCodunineg().trim().substring(2,4);
					//validar cliente
					if (hFacFirst.getId().getCodcli() !=  hFacSelected.getId().getCodcli()){
						valido = false;
						lblValidaFactura.setValue(
								"<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El cliente de las facturas seleccionadas debe ser el mismo<br>" 
								);
						break;
					}
					//validar compañia
					else if(!hFacFirst.getId().getCodcomp().trim().equals(hFacSelected.getId().getCodcomp().trim())){
						lblValidaFactura.setValue(
								"<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Las facturas deben pertenecer a la misma compañia<br>" 
								);
						valido = false;
					}
					//validar moneda
					else if(!hFacFirst.getId().getMoneda().equals(hFacSelected.getId().getMoneda())){
						valido = false;
						lblValidaFactura.setValue(
								"<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> La moneda de las facturas seleccionadas debe ser la misma<br>" 
								);
						break;
					}
					else if(hFacFirst.getId().getCpendiente().doubleValue() < 0 || hFacSelected.getId().getCpendiente().doubleValue() < 0){
						valido = false;
						lblValidaFactura.setValue(
								"<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> No es posible pagar facturas con montos negativos<br>" 
								);
						break;
					}
					if(valido){
						lstSelectedFacs.add(hFacSelected);
					}
				}
			}else if(lstSelectedRows.size() == 1){
				row = (RowItem)lstSelectedRows.get(0);
				hFacFirst = (Credhdr)gvHfacturasCredito.getDataRow(row);
				lstSelectedFacs.add(hFacFirst);
			}
			if(valido){
				m.put("selectedFacsCredito", lstSelectedFacs);	
				//gvfacturasSelecCredito.dataBind();
			}else{
				
				dwValidacionFactura.setWindowState("normal");
				dwValidacionFactura.setStyle("height: 170px; visibility: visible; width: 365px");
				gvHfacturasCredito.dataBind();
				srm.addSmartRefreshId(gvHfacturasCredito.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(lblValidaFactura.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(dwValidacionFactura.getClientId(FacesContext.getCurrentInstance()));
			}	
			srm.addSmartRefreshId(intSelected.getClientId(FacesContext.getCurrentInstance()));			
		}catch(Exception ex){   	
			ex.printStackTrace();     
		}
	}
	public void cerrarValidaFactura(ActionEvent ev){
		dwValidacionFactura.setWindowState("hidden");
	}
/**************************************************************************************************************************/	
/******************************************************************************************************************/
	public void onCompaniaChange(ValueChangeEvent ev){
		String sCodComp = "";
		Unegocio[] unegocio = null;
		try{
			sCodComp = ddlCompaniaCre.getValue().toString();
			if(!sCodComp.equals("10")){
				//
				SucursalCtrl sucCtrl = new SucursalCtrl();
				//
				unegocio = sucCtrl.obtenerSucursalesxCompania(sCodComp);
				
				List lstFiltro = new ArrayList();				
				lstFiltro.add(new SelectItem("00010","Todas","Seleccione una Compañía primero"));
				for(int i = 0; i < unegocio.length; i ++){
					lstFiltro.add(new SelectItem(unegocio[i].getId().getCodunineg().trim(),unegocio[i].getId().getCodunineg().trim() + ": " + unegocio[i].getId().getDesc().trim(),unegocio[i].getId().getCodunineg().trim()));
				}
				m.put("lstSucursalCred", lstFiltro);	
				ddlSucursalCred.dataBind();
				//
				realizarBusquedaFacturas();
			}else{
				List lstFiltro = new ArrayList();				
				lstFiltro.add(new SelectItem("00010","Todas","Seleccione una Compañía primero"));				
				m.put("lstSucursalCred", lstFiltro);
				ddlSucursalCred.dataBind();
				lstFiltro = new ArrayList();				
				lstFiltro.add(new SelectItem("10","Todas","Seleccione una Sucursal primero"));				
				m.put("lstUninegCred", lstFiltro);	
				ddlUninegCred.dataBind();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
/*************************************************************************************************/
/*************************************************************************************************/
public void onSucursalChange(ValueChangeEvent ev){
	String sCodSuc = "";
	Unegocio[] unegocio = null;
	try{
		sCodSuc = ddlSucursalCred.getValue().toString();
		sCodSuc = sCodSuc.substring(3,5);
		if(!sCodSuc.equals("10")){
			SucursalCtrl sucCtrl = new SucursalCtrl();
			unegocio = sucCtrl.obtenerUninegxSucursal(sCodSuc);
			List lstFiltro = new ArrayList();
			lstFiltro.add(new SelectItem("10","Todas","Seleccione una Sucursal primero"));
			for(int i = 0; i < unegocio.length; i ++){
				lstFiltro.add(new SelectItem(unegocio[i].getId().getCodunineg().trim(),unegocio[i].getId().getCodunineg().trim() + ": " + unegocio[i].getId().getDesc().trim(),unegocio[i].getId().getCodunineg().trim()));
			}
			m.put("lstUninegCred", lstFiltro);	
			ddlUninegCred.dataBind();
			//
			//realizarBusquedaFacturas();
		}else {
			List lstFiltro = new ArrayList();				
			lstFiltro.add(new SelectItem("10","Todas","Seleccione una Sucursal primero"));				
			m.put("lstUninegCred", lstFiltro);
			ddlUninegCred.dataBind();
			realizarBusquedaFacturas();
		}
	}catch(Exception ex){ 
		ex.printStackTrace();
	}
}
	
/*************************************************************************************************/
/*************************************************************************************************/
			
	public void agregarFacturaRecibo(ActionEvent e){
		List lstAgregarFactura;
		ArrayList<Credhdr>lstFacturaSelected;
		Credhdr hFac = null;
		String sMonedaBase = "";
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		try{
			lstAgregarFactura = (List)m.get("lstAgregarFactura");
			lstFacturaSelected = (ArrayList<Credhdr>)m.get("selectedFacsCredito");
			
			RowItem ri = (RowItem)e.getComponent().getParent().getParent();
			List lstA = (List) ri.getCells();
			//Columna a obtener: No. Factura
			Cell c = (Cell) lstA.get(1);
			HtmlOutputText noFactura = (HtmlOutputText) c.getChildren().get(0);
			int iNoFactura = Integer.parseInt(noFactura.getValue().toString());
			//Columna a obtener:Partida
			Cell c2 = (Cell) lstA.get(3);
			HtmlOutputText partida = (HtmlOutputText) c2.getChildren().get(0);
			String sPartida = partida.getValue().toString();
			
			for(int i = 0; i < lstAgregarFactura.size();i++){
				hFac = (Credhdr)lstAgregarFactura.get(i);
				if(hFac.getNofactura() == iNoFactura && hFac.getPartida().trim().equals(sPartida.trim())){
					lstFacturaSelected.add(hFac);
					lstAgregarFactura.remove(i);
				}
			}
			m.put("selectedFacsCredito",lstFacturaSelected);
			intSelectedDet.setValue(lstFacturaSelected.size());
			m.put("lstAgregarFactura",lstAgregarFactura);
			gvAgregarFactura.dataBind();
			gvfacturasSelecCredito.dataBind();
			List lstSelectedMet = (List)m.get("lstPagos");
			
			//obtener companias x caja
			sMonedaBase = cCtrl.sacarMonedaBase((F55ca014[])m.get("cont_f55ca014"), hFac.getId().getCodcomp());
			//--------------------------------------------------
			//calcular el monto recibido en dependencia de moneda de factura y moneda de pago
			double montoRecibido = calcularElMontoRecibido(lstSelectedMet, hFac, sMonedaBase);

			//determinar como dar el cambio
			determinarCambio(lstSelectedMet, hFac, montoRecibido, sMonedaBase);
			
			//distribuir el monto recibido a los montos a aplicar en factura
			if(m.get("dMontoAplicarCre") == null){
				distribuirMontoAplicar(sMonedaBase, lstFacturaSelected);
			}
			m.remove("dMontoAplicarCre");
			
			calcularTotales(lstFacturaSelected, sMonedaBase);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
/*************************************************************************************************/
	public void mostrarAgregarFacturas(ActionEvent ev){
		
		List lstFacturasSelected, lstFacturas, lstAgregarFacts = new ArrayList();
		Credhdr hFacSel, hFac;
		
		try{
			
			CodeUtil.removeFromSessionMap(new String[] {
					"cr_AgregarFacturasNumeroDesde",
					"cr_AgregarFacturasNumeroHasta" });
			
			lstFacturasSelected = (List)m.get("selectedFacsCredito");	
			lstFacturas = (List)m.get("lstHfacturasCredito");	
			
			
			//sacar las q ya estan seleccionadas
			for (int i = 0; i < lstFacturasSelected.size(); i++){
				hFacSel = (Credhdr)lstFacturasSelected.get(i);
				for (int j = 0; j < lstFacturas.size(); j++){
					hFac = (Credhdr)lstFacturas.get(j);
					if(hFacSel.getId().getNofactura() == hFac.getId().getNofactura() && hFacSel.getId().getTipofactura().equals(hFac.getId().getTipofactura()) && hFacSel.getId().getPartida().equals(hFac.getId().getPartida())){
						lstFacturas.remove(j);
						break;
					}
				}
			}
			hFacSel = (Credhdr)lstFacturasSelected.get(0);
			//meter a a la lista solo las q corresponden
			for (int m = 0; m < lstFacturas.size(); m++){
				hFac = (Credhdr)lstFacturas.get(m);
				if(hFacSel.getId().getCodcli() == hFac.getId().getCodcli() && hFacSel.getId().getMoneda().equals(hFac.getId().getMoneda()) && hFacSel.getId().getCodcomp().equals(hFac.getId().getCodcomp()) && hFac.getId().getCpendiente().doubleValue() > 0){
					lstAgregarFacts.add(hFac);
				}
			}
			
			m.put("lstAgregarFactura", lstAgregarFacts);
			
			gvAgregarFactura.dataBind();
			dwAgregarFactura.setWindowState("normal");
			gvAgregarFactura.setPageIndex(0);
			
		}catch(Exception ex){
			ex.printStackTrace(); 
		}	
	}
	
	public void cerrarAgregarFacturas(ActionEvent ev){
		dwAgregarFactura.setWindowState("hidden");
	}
	
/*****************ACTION LISTENER PARA BUSCAR FACTURAS DE CREDITO POR PARAMETROS*****************/
	public void BuscarFacturasCredito(ActionEvent e) {
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		String strParametro = "";
		FacturaCreditoCtrl facCtrl = new FacturaCreditoCtrl();
		List lstResult = new ArrayList();
		Date dFechaDesde = null;
		Date dFechaHasta = null;
		String sMoneda = "",sCodComp = "",sCodunineg = "",sCodSuc = "";
		try {
			
			LogCajaService.CreateLog("FacCreditoDAO.BuscarFacturasCredito - INICIO", "JR8", "Inicio ejecucion Metodo");
			
			sMoneda = cmbFiltroMonedas.getValue().toString();
			sCodComp = ddlCompaniaCre.getValue().toString();
			sCodunineg = ddlUninegCred.getValue().toString();
			sCodSuc = ddlSucursalCred.getValue().toString();
			
			if (dcFechaDesde.getValue() != null){
				dFechaDesde = (Date)dcFechaDesde.getValue();	
			}
			if (dcFechaHasta.getValue() != null){
				dFechaHasta = (Date)dcFechaHasta.getValue();
			}
			
			if (txtParametroCredito.getValue() != null && !txtParametroCredito.getValue().toString().trim().equals("")) {
				strParametro = txtParametroCredito.getValue().toString().trim();
			}
			int busqueda = 1;
			if (m.get("strBusquedaCredito") != null) {
				busqueda = Integer.parseInt((String) m.get("strBusquedaCredito"));
			}
			F55ca014[] f14 = (F55ca014[])m.get("cont_f55ca014");
			
			boolean bTodos = chkMostrarTodoSrch.isChecked();
			
			lstResult = facCtrl.buscarFacturasCredito(busqueda,strParametro,
									sMoneda,dFechaDesde,dFechaHasta,
									sCodComp,sCodunineg,sCodSuc,f14,
									bTodos); 

			if (lstResult != null && !lstResult.isEmpty()){
				m.put("lstHfacturasCredito", lstResult);
				lstHfacturasCredito = lstResult;
				m.put("lstRefreshFacturas", lstResult);
				sMensaje = "";
												
			}else{
				sMensaje = "No se encontraron resultados";
				lstHfacturasCredito = new ArrayList();
				m.put("lstHfacturasCredito", lstResult);
			}
			gvHfacturasCredito.setPageIndex(0);
			gvHfacturasCredito.dataBind();
			txtMensaje.setValue(sMensaje);
			m.put("sMensajeCre", sMensaje);
			
			LogCajaService.CreateLog("FacCreditoDAO.BuscarFacturasCredito - FIN", "JR8", "Fin ejecucion Metodo");
		} catch (Exception ex) {
			ex.printStackTrace();
		}finally{
			dwCargando.setWindowState("hidden");
		}
	}
/**************************************************************************************************************************/
/************************SET BUSQUEDA CREDITO*********************************************************/
	public void setBusquedaCredito(ValueChangeEvent e) {
		Map m = FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap();
		String strBusqueda = cmbBusquedaCredito.getValue().toString();
		m.put("strBusquedaCredito", strBusqueda); 
	}
/**************************************************************************************************************************/

/****************MOSTRAR EL DETALLE DE FACTURA DE CREDITO*****************************************************************/	
	public void mostrarDetalleCredito(ActionEvent e) {
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		FacturaCreditoCtrl facCred = new FacturaCreditoCtrl();
		Credhdr hFac = new Credhdr(),hFacClicked = null;
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		String sMonedaBase = "";
		try {
			RowItem ri = (RowItem) e.getComponent().getParent().getParent();
			hFacClicked =  (Credhdr)gvHfacturasCredito.getDataRow(ri);
			
			List lstFacsActuales = (List) m.get("lstHfacturasCredito");
			List lstMonedasDetalle = new ArrayList();
			
			//obtener companias x caja
			sMonedaBase = cCtrl.sacarMonedaBase((F55ca014[])m.get("cont_f55ca014"),hFacClicked.getId().getCodcomp());
			
			for (int i = 0; i < lstFacsActuales.size(); i++) {
				hFac = (Credhdr) lstFacsActuales.get(i);
				if (hFac.getNofactura() == hFacClicked.getNofactura() && hFac.getTipofactura().equals(hFacClicked.getTipofactura()) 
						&& hFac.getPartida().equals(hFacClicked.getPartida()) && hFac.getId().getCodsuc().equals(hFacClicked.getId().getCodsuc()) && 
						hFac.getId().getCodunineg().equals(hFacClicked.getId().getCodunineg())) {
					m.put("hFac", hFac);
					txtFechaFactura.setValue(hFac.getId().getFecha());
					txtNofactura.setValue(hFac.getId().getNofactura() + "");
					txtCliente.setValue(hFac.getId().getNomcli() + " (nombre)");
					txtCodigoCliente.setValue(hFac.getId().getCodcli() + " (código)");
					txtUnineg.setValue(hFac.getId().getCodunineg() + " " + hFac.getId().getUnineg());
					txtTipoFactura.setValue(hFac.getId().getTipofactura());
					txtCompania.setValue(hFac.getId().getNomcomp());
					
					txtFechaVenc.setValue(hFac.getId().getFechavenc());
					txtFechaVenDecto.setValue(hFac.getId().getFechavenc());
					txtTasaDetalle.setValue(hFac.getId().getTasa());
					
					
					//
					if(hFac.getMoneda().equals(sMonedaBase)){
						txtPendiente.setValue(hFac.getId().getCpendiente());						
						lstMonedasDetalle.add(new SelectItem(sMonedaBase,sMonedaBase));
					}else{
						txtPendiente.setValue(hFac.getId().getDpendiente());						
						lstMonedasDetalle.add(new SelectItem(hFac.getMoneda(),hFac.getMoneda()));
						lstMonedasDetalle.add(new SelectItem(sMonedaBase,sMonedaBase));
					}
					m.put("lstMonedasDetalle", lstMonedasDetalle);
					cmbMonedaDetalleCredito.dataBind();
					//buscar detalle 
					List lstDfacturasCredito = facCred.getDetalleFacturaCredito(hFac.getId().getNofactura(), hFac.getId().getTipofactura(),hFac.getId().getCodsuc(),hFac.getId().getCodunineg());
					m.put("lstDfacturasCredito", lstDfacturasCredito);
					gvDfacturasCredito.dataBind();
					
					break;
				}
			}
			dgwDetalleCredito.setWindowState("normal");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
/*****************************************************************************************/
	public void cerrarDetalleCredito(ActionEvent e) {
		Divisas divisas = new Divisas();
		try {
			dgwDetalleCredito.setWindowState("hidden");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
/************************CAMBIAR MONEDA DEL DETALLE EN CREDITO***************************************/
	public void cambiarMonedaDetalleCredito(ValueChangeEvent e){	
		Divisas divisas = new Divisas();
		String sMonedaBase = "";
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		String monedaActual = cmbMonedaDetalleCredito.getValue().toString();
		try{
			Credhdr hFac = (Credhdr)m.get("hFac");
			//obtener companias x caja
			sMonedaBase = cCtrl.sacarMonedaBase((F55ca014[])m.get("cont_f55ca014"), hFac.getId().getCodcomp());
			if(monedaActual.equals(sMonedaBase)){
				txtSubtotal.setValue(hFac.getId().getCsubtotal());
				txtIva.setValue(hFac.getId().getCimpuesto());	
				txtPendiente.setValue(hFac.getId().getCpendiente());						
				txtTotal.setValue(hFac.getId().getCtotal());
			}else {
				txtSubtotal.setValue(hFac.getId().getDsubtotal());
				txtIva.setValue(hFac.getId().getDimpuesto());	
				txtPendiente.setValue(hFac.getId().getDpendiente());				
				txtTotal.setValue(hFac.getId().getDtotal());
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
/****************************************************************************************************************************/	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/******************************METODOS DEL RECIBO******************************************************************/
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void calcularTotales(List lstFacturasSelected, String sMonedaBase){
		Credhdr[] hFacSelected = null;
//		calcular total y faltante
		double dMontoTotalAplicarDomestico = 0.0;
		double dMontoTotalFaltanteDomestico = 0.0;
		double dMontoTotalAplicarForaneo = 0.0;
		double dMontoTotalFaltanteForaneo = 0.0;
		Divisas divisas  = new Divisas();
		String sMoneda = "";
		Tpararela[] tcPar = null;
		Tcambio[] tcJDE = null;
		double tasa = 1.0;

		try{
			
			//
			//calcular monto a aplicar
			hFacSelected = new Credhdr[lstFacturasSelected.size()];
			for(int i = 0; i < lstFacturasSelected.size(); i++){
				hFacSelected[i] = (Credhdr)lstFacturasSelected.get(i);
				if (hFacSelected[i].getMoneda().equals(sMonedaBase)){
					dMontoTotalAplicarDomestico = dMontoTotalAplicarDomestico + hFacSelected[i].getId().getCpendiente().doubleValue();
					dMontoTotalFaltanteDomestico = dMontoTotalFaltanteDomestico + hFacSelected[i].getMontoAplicar().doubleValue();
				}else{
					dMontoTotalAplicarForaneo = dMontoTotalAplicarForaneo + hFacSelected[i].getId().getDpendiente().doubleValue();
					dMontoTotalFaltanteForaneo = dMontoTotalFaltanteForaneo + hFacSelected[i].getMontoAplicar().doubleValue();
				}
			}
			if(hFacSelected[0].getMoneda().equals(sMonedaBase)){
				montoTotalAplicarForaneo.setStyle("display:none");
				montoTotalFaltanteForaneo.setStyle("display:none");
				montoTotalAplicarDomestico.setValue("<B>"+sMonedaBase+"</B> " + divisas.formatDouble(dMontoTotalAplicarDomestico));
				montoTotalFaltanteDomestico.setValue("<B>"+sMonedaBase+"</B> " + divisas.formatDouble(dMontoTotalAplicarDomestico - dMontoTotalFaltanteDomestico));
			}else {
				tcPar = (Tpararela[]) m.get("tpcambio");
				// buscar tasa de cambio paralela para moneda
				for (int t = 0; t < tcPar.length; t++) {
					if (tcPar[t].getId().getCmono().equals(hFacSelected[0].getMoneda().trim()) || tcPar[t].getId().getCmond().equals(hFacSelected[0].getMoneda().trim())) {
						tasa = tcPar[t].getId().getTcambiom().doubleValue();
						break;
					}
				}
				montoTotalAplicarForaneo.setStyle("display:inline");
				montoTotalFaltanteForaneo.setStyle("display:inline");
				montoTotalAplicarDomestico.setValue("<B>"+sMonedaBase+"</B> " + divisas.formatDouble(dMontoTotalAplicarForaneo*tasa));
				montoTotalFaltanteDomestico.setValue("<B>"+sMonedaBase+"</B> " + divisas.formatDouble((dMontoTotalAplicarForaneo - dMontoTotalFaltanteForaneo)*tasa));
				montoTotalAplicarForaneo.setValue("<B>"+hFacSelected[0].getMoneda()+"</B>" +" " + divisas.formatDouble(dMontoTotalAplicarForaneo));
				montoTotalFaltanteForaneo.setValue("<B>"+hFacSelected[0].getMoneda()+"</B>" +" " + divisas.formatDouble((dMontoTotalAplicarForaneo - dMontoTotalFaltanteForaneo)));
			}
			
		}catch(Exception ex){
			ex.printStackTrace(); 
		}
	}
/**************************MOSTRAR RECIBO********************************************************************/
 public void mostrarRecibo(ActionEvent ev){
	 SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
	 List lstFacturasSelected = new ArrayList();
	 List<Credhdr> lstInt = null, lstNew = new ArrayList<Credhdr>();
	 Credhdr hFac = null;
	 ReciboPrimaCtrl rpCtrl = new ReciboPrimaCtrl();
	 //	formateador de numeros
 	 Divisas divisas = new Divisas();
 	 Credhdr[] hFacSelected = null;
	 List lstPagosClean = new ArrayList();
	 CodeUtil.putInSessionMap("lstPagos", lstPagosClean);
	 CodeUtil.putInSessionMap("mpagos", lstPagosClean);		
	 double dMontoAplicar = 0;
	 double TasaPar = 0.0;
	 //metodos de pago
	 MetodosPagoCtrl metCtrl = new MetodosPagoCtrl();
	 String[] f55ca012 = null;
	 Metpago[] metpago = null;
	 List lstMetPago = null;
	 //monedas
	 String[] sCodMod = null;
	 MonedaCtrl monCtrl = new MonedaCtrl();
	 //afiliados
	 AfiliadoCtrl afCtrl = new AfiliadoCtrl();
	 Cafiliados[] cafiliado = null;
 	
	 CompaniaCtrl cCtrl = new CompaniaCtrl();
	 String sMonedaBase = "";
	 String sMoneda ;
	 Vf55ca01 vf01 = null;
	 String sLineaFactura = "";
	 
	 boolean cobra= false;
	 InteresesRevolventesCtrl intCtrl = new InteresesRevolventesCtrl();
	 
	Session session = null;
	
	 try{
		 
		 LogCajaService.CreateLog("FacCreditoDAO.mostrarRecibo - INICIO", "JR8", "Inicio ejecucion metodo");
		 
		 session = HibernateUtilPruebaCn.currentSession();
		 
		vf01 = (Vf55ca01) ((List) CodeUtil.getFromSessionMap("lstCajas")).get(0);
		
		F55ca014[] f14 = (F55ca014[])CodeUtil.getFromSessionMap("cont_f55ca014");
		
		//===== Borrar bandera para sobrantes en pagos.
		 CodeUtil.removeFromSessionMap("scr_SobrantePago");
		 CodeUtil.removeFromSessionMap("scr_MpagoSobrante");
		 CodeUtil.removeFromSessionMap("scr_SbrDfr");
		 CodeUtil.removeFromSessionMap("lstSolicitud");
		 
		 //Remover facturas previas seleccionadas
		// CodeUtil.removeFromSessionMap("selectedFacsCredito");
		 
		 restablecerEstilos();
		 restablecerEstilosPago();
		 
		 if(CodeUtil.getFromSessionMap("selectedFacsCredito") != null && !((List)CodeUtil.getFromSessionMap("selectedFacsCredito")).isEmpty()){
			
			 lstFacturasSelected = (List) CodeUtil.getFromSessionMap("selectedFacsCredito");
			
			 hFac = (Credhdr)lstFacturasSelected.get(0);
			 //----- Restablecer los valores de las tasas de cambio a la fecha actual.
			 cargarTasasCambioAldia();		
			
			 //verificar si al cliente se le cobraran intereses
			cobra = intCtrl.verificarSiCobraInteres(hFac.getId().getCodcli());
			
			if(cobra){
				
				lstInt = intCtrl.leerInteresesRevxCliente(hFac.getId().getCodsuc(),hFac.getId().getCodcli(),"L9", hFac.getMoneda(),f14);
				
				if(lstInt != null && !lstInt.isEmpty() ){
					lstNew.addAll(lstInt);
				}
			 
				lstNew.addAll(lstFacturasSelected);
				
				lstFacturasSelected = lstNew;
				CodeUtil.putInSessionMap("selectedFacsCredito",lstFacturasSelected);
			}
			 
			intSelectedDet.setValue(lstFacturasSelected.size());
			int iCajaId = Integer.parseInt(CodeUtil.getFromSessionMap("sCajaId").toString());
			
			if(hFac.getId().getCpendiente().doubleValue() > 0){

				sMonedaBase = cCtrl.sacarMonedaBase( f14, hFac.getId().getCodcomp());
				
				lblCodigoCliente.setValue(hFac.getId().getCodcli());
				lblNombreCliente.setValue(hFac.getId().getNomcli());

				String sUltimoNumero = rpCtrl.obtenerUltimoRecibo(iCajaId, hFac.getId().getCodcomp())+"";
				lblNumeroRecibo.setValue(sUltimoNumero);

				//&& =========== conservar todos los metodos de pago configurados.
				List<Vf55ca012> MetodosPagoConfigurados =  DonacionesCtrl.obtenerMpagosConfiguradosCaja(hFac.getId().getCodcomp(), iCajaId);
				CodeUtil.putInSessionMap("cr_MetodosPagoConfigurados", MetodosPagoConfigurados );
				
				f55ca012 = metCtrl.obtenerMetodosPagoxCaja_Compania(iCajaId, hFac.getId().getCodcomp());

		    	metpago = new Metpago[f55ca012.length];
		    	lstMetPago = new ArrayList();
		    	lstMetodosPago = new ArrayList();
		    	
		    	for(int i = 0; i <  f55ca012.length; i ++){		    		
   		
		    		
		    		Metpago formapago = com.casapellas.controles.MetodosPagoCtrl.metodoPagoCodigo(f55ca012[i]);
		    		lstMetodosPago.add(new SelectItem( formapago.getId().getCodigo(), formapago.getId().getMpago() ) );
		    		lstMetPago.add( new Metpago[]{formapago} ) ;
		    		
		    		
		    	}
		    	CodeUtil.putInSessionMap("metpago", lstMetPago);
		    	CodeUtil.putInSessionMap("lstMetodosPagoCre", lstMetodosPago);
		    	cmbMetodosPago.dataBind();

		    	String sCodMet = cmbMetodosPago.getValue().toString();
		    	cambiarVistaMetodos(sCodMet,vf01);

		    	//cargar monedas de metodo de pago y compania de factura
		    	lstMoneda = new ArrayList();
		    	sCodMod = monCtrl.obtenerMonedasxMetodosPago_Caja(iCajaId, hFac.getId().getCodcomp(),cmbMetodosPago.getValue().toString());
		    	for(int i = 0; i < sCodMod.length; i++){
		    		lstMoneda.add(new SelectItem(sCodMod[i],sCodMod[i]));
		    	}
		    	CodeUtil.putInSessionMap("lstMonedaCre", lstMoneda);
		    	cmbMoneda.dataBind();

		    	
				//&& ========== cargar afiliados
		    	sMoneda = cmbMoneda.getValue().toString() ;
				sLineaFactura = (hFac.getId().getCodunineg().trim()).substring(2, 4);
		    	
				lstAfiliado = vf01.getId().getCasktpos() == 'N' ?
								getLstAfiliados(iCajaId, hFac.getId()
										.getCodcomp(), sLineaFactura, sMoneda):
								PosCtrl.getAfiliadosSp(iCajaId, 
										hFac.getId().getCodcomp(), 
										sLineaFactura, sMoneda) ;		
			    
				if(lstAfiliado == null)
					lstAfiliado = new ArrayList<SelectItem>();
								
				CodeUtil.putInSessionMap("lstAfiliadoCre",lstAfiliado);
				ddlAfiliado.dataBind();
			
				//&& ========== poner etiqueta paralela
				Tpararela[] tpcambio  = (Tpararela[])CodeUtil.getFromSessionMap("tpcambio");
				if(tpcambio == null){
					lblMensajeValidacion.setValue("Tasa de cambio paralela no esta configurada");
					dwProcesa.setWindowState("normal");
					dwProcesa.setStyle("width:370px;height:160px");
					return;
				}

				String sTasaCambio ="";
				for (int j = 0; j < tpcambio.length; j++) {
					sTasaCambio = sTasaCambio + " "
							+ tpcambio[j].getId().getCmond() + ": "
							+ tpcambio[j].getId().getTcambiom();
					TasaPar = tpcambio[j].getId().getTcambiom()
							.doubleValue();
					break;
				}
				lblTasaCambio.setValue(sTasaCambio);
				CodeUtil.putInSessionMap("lblTasaCambio", sTasaCambio);

				//poner tasa de cambio de JDE
				Tcambio[] tcJDE = (Tcambio[])CodeUtil.getFromSessionMap("tcambio");
				String sTasaJDE = "";
				for (int l = 0; l < tcJDE.length; l++) {
					sTasaJDE = sTasaJDE + " "
							+ tcJDE[l].getId().getCxcrdc() + ": "
							+ tcJDE[l].getId().getCxcrrd();

				}
				lblTasaJDE.setValue(sTasaJDE);
				CodeUtil.putInSessionMap("lblTasaJDE", sTasaJDE);
				//calcular total y faltante
				double dMontoTotalAplicarDomestico = 0.0;
				double dMontoTotalFaltanteDomestico = 0.0;
				double dMontoTotalAplicarForaneo = 0.0;
				double dMontoTotalFaltanteForaneo = 0.0;
				
				
				
				//Modificado por lfonseca
				//2018-09-18
				
				Vf55ca01 caja = (Vf55ca01) ((List) m.get("lstCajas")).get(0);
				int caid = caja.getId().getCaid();	
				int iCajero = caja.getId().getCacati();
				
				VerificarFacturaProceso vfp = new VerificarFacturaProceso();
				Vautoriz vautoriz = ((Vautoriz[]) m.get("sevAut"))[0];
				HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

				String ipAddress = request.getHeader("X-FORWARDED-FOR");
				if (ipAddress == null) {
				    ipAddress = request.getRemoteAddr();
				}

				String userAgent = UserAgent.parseUserAgentString(request.getHeader("user-agent")).getBrowser() + " " 
						+ UserAgent.parseUserAgentString(request.getHeader("user-agent")).getBrowserVersion();
				
				//
				//calcular monto a aplicar
				hFacSelected = new Credhdr[lstFacturasSelected.size()];
				for(int i = 0; i < lstFacturasSelected.size(); i++){
					hFacSelected[i] = (Credhdr)lstFacturasSelected.get(i);
					if (hFac.getMoneda().equals(sMonedaBase)){
						dMontoAplicar = dMontoAplicar + hFacSelected[i].getId().getCpendiente().doubleValue();
						dMontoTotalAplicarDomestico = dMontoTotalAplicarDomestico + hFacSelected[i].getId().getCpendiente().doubleValue();
					}else{
						dMontoAplicar = dMontoAplicar + hFacSelected[i].getId().getDpendiente().doubleValue();
						dMontoTotalAplicarForaneo = dMontoTotalAplicarForaneo + hFacSelected[i].getId().getDpendiente().doubleValue();
						dMontoTotalAplicarDomestico = dMontoTotalAplicarDomestico + (hFacSelected[i].getId().getDpendiente().doubleValue()*TasaPar);
					}
					
					String[] strVerificarF = vfp.verificarFactura(String.valueOf(caid), 
																  String.valueOf(hFacSelected[i].getId().getCodcli()), 
																  String.valueOf(hFacSelected[i].getNofactura()), 
																  hFacSelected[i].getTipofactura(), 
																  hFacSelected[i].getPartida(), 
																  String.valueOf(iCajero),
																  vautoriz.getId().getLogin().toLowerCase(),
																  ipAddress.toUpperCase(),
																  userAgent.toUpperCase(),
																  request.getLocalAddr().toUpperCase());
					
					if(!strVerificarF[0].equals("S"))
					{
						String strError = "La factura  numero " + strVerificarF[2] +  
						 		", se esta procesando en la caja # " + strVerificarF[1] + ", " +
						 		"por el usuario " + strVerificarF[5] + " usando un navegador " + strVerificarF[6] +
						 		" y accediendo al servidor de caja " + strVerificarF[7];
						
						lblValidaFactura.setValue("<img width=\"7\" src=\"/"
												+ PropertiesSystem.CONTEXT_NAME
												+ "/theme/icons/redCircle.jpg\" border=\"0\" />"+strError+"<br>");
								dwValidacionFactura.setWindowState("normal");
								dwValidacionFactura
										.setStyle("height: 150px; visibility: visible; width: 330px");
								srm.addSmartRefreshId(dwValidacionFactura
										.getClientId(FacesContext.getCurrentInstance()));

						return;
					}
				}
				
				//----------------------------------------
				
				
				if(hFac.getMoneda().equals(sMonedaBase)){
					montoTotalAplicarForaneo.setStyle("display:none");
					montoTotalFaltanteForaneo.setStyle("display:none");
					montoTotalAplicarDomestico.setValue("<B>"+sMonedaBase+"</B> " + divisas.formatDouble(dMontoTotalAplicarDomestico));
					montoTotalFaltanteDomestico.setValue("<B>"+sMonedaBase+"</B> " + divisas.formatDouble(dMontoTotalAplicarDomestico));		
				}else{
					montoTotalAplicarForaneo.setStyle("display:inline");
					montoTotalFaltanteForaneo.setStyle("display:inline");
					montoTotalAplicarForaneo.setValue("<B>" + hFac.getMoneda() + "</B>" +" "+ divisas.formatDouble(dMontoTotalAplicarForaneo));
					montoTotalFaltanteForaneo.setValue("<B>" + hFac.getMoneda() + "</B>" +" "+ divisas.formatDouble(dMontoTotalAplicarForaneo));
					montoTotalAplicarDomestico.setValue("<B>"+sMonedaBase+"</B> " + divisas.formatDouble(dMontoTotalAplicarDomestico));
					montoTotalFaltanteDomestico.setValue("<B>"+sMonedaBase+"</B> " + divisas.formatDouble(dMontoTotalAplicarDomestico));
				}
				//
				
				//poner resumen de pago
				lblMontoAplicar.setValue("A Aplicar " + hFac.getMoneda() +":");
				txtMontoAplicar.setValue("0.00");
				lblMontoRecibido.setValue("Recibido "+ hFac.getMoneda() +":");
				txtMontoRecibido.setValue("0.00");
				lblCambio.setValue("Cambio " + hFac.getMoneda() +":");		
				txtCambio.setValue("0.00");
				txtCambio.setStyle("font-size: 10pt; color: red");
				txtCambioForaneo.setStyle("visibility: hidden; width: 0px");
				lblCambioDomestico.setValue("");
				txtCambioDomestico.setValue("");
				lnkCambio.setStyle("visibility: hidden; width: 0px");
				

				lnkCambio.setIconUrl("");
				lnkCambio.setHoverIconUrl("");
				txtConcepto.setValue("");
				txtMonto.setValue("");
				txtReferencia1.setValue("");
				txtReferencia2.setValue("");
				txtReferencia3.setValue("");
				txtNumRec.setValue("");
				lblNumRecm.setValue("");
				lblNumrec = "Último Recibo: "; 
				txtNumRec.setStyle("visibility:hidden");
				txtFecham.setStyle("visibility:hidden");
				dwRecibo.setWindowState("normal");
				gvfacturasSelecCredito.dataBind();
				cmbTiporecibo.dataBind();
				
		
			}else{
				lblValidaFactura
						.setValue("<img width=\"7\" src=\"/"
								+ PropertiesSystem.CONTEXT_NAME
								+ "/theme/icons/redCircle.jpg\" border=\"0\" /> No es posible pagar facturas con montos negativos<br>");
				dwValidacionFactura.setWindowState("normal");
				dwValidacionFactura
						.setStyle("height: 150px; visibility: visible; width: 330px");
				srm.addSmartRefreshId(dwValidacionFactura
						.getClientId(FacesContext.getCurrentInstance()));
			}
		 }else {//no hay facturas seleccionadas
				lblValidaFactura
						.setValue("<img width=\"7\" src=\"/"
								+ PropertiesSystem.CONTEXT_NAME
								+ "/theme/icons/redCircle.jpg\" border=\"0\" /> Seleccione al menos una factura para procesar el recibo<br>");
				dwValidacionFactura.setWindowState("normal");
				dwValidacionFactura
						.setStyle("height: 150px; visibility: visible; width: 330px");
				srm.addSmartRefreshId(dwValidacionFactura
						.getClientId(FacesContext.getCurrentInstance()));
		}
		 
		//&& === Ocultar los objetos del check de sobrante o diferencial.
		lblMarcaSobrDifer.setStyle("display: none");
		chkSobranteDifrl.setStyle("display: none");
		srm.addSmartRefreshId(lblMarcaSobrDifer.getClientId(FacesContext.getCurrentInstance()));
		srm.addSmartRefreshId(chkSobranteDifrl.getClientId(FacesContext.getCurrentInstance()));		
		
		LogCajaService.CreateLog("FacCreditoDAO.mostrarRecibo - FIN", "JR8", "Fin ejecucion metodo");
	 }catch(Exception ex){
		 LogCajaService.CreateLog("FacCreditoDAO.mostrarRecibo", "ERR", ex.getMessage());
	 }finally{
		 HibernateUtilPruebaCn.closeSession(session);
	 }
 }
/**************************************************************************************************************************/
 /*********************************ESTABLECE EL TIPO DE RECIBO**********************************************/
	public void setTipoRecibo(ValueChangeEvent e) {
		SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();		
		String tipo = cmbTiporecibo.getValue().toString();	
		m.put("mTipoRecibo", tipo);
		try{
			//Tipo Manual
			if(tipo.equals("MANUAL")) {
				txtNumRec.setStyle("");
				fechaRecibo = "";
				lblNumRecm.setValue("No. Recibo Manual: ");
				txtNumRec.setStyle("display:inline");
				txtFecham.setStyle("display:inline");
				txtFecham.setStyleClass("dateChooserSyleClass");
				Date dFechaActual = new Date();
				txtFecham.setValue(dFechaActual);				
			} else {
				lblNumRecm.setValue("");
				lblNumrec = "Último Recibo: "; 
				txtNumRec.setStyle("visibility:hidden");
				txtFecham.setStyle("visibility:hidden");
			}
			//----- Actualizar las tasas de cambio al día.
			cargarTasasCambioAldia();
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	/*************ESTABLECE LAS REFERENCIA REQUERIDAS PARA EL METODO DE PAGO SELECCIONADO***********************************/
	public void setMetodosPago(ValueChangeEvent e) {
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
				
		Vf55ca01 vf01 = null;
		MonedaCtrl monCtrl = new MonedaCtrl();
		String[] sMoneda = null;
		try {
			
			CodeUtil.removeFromSessionMap(new String[] 
					{ "cr_MontoTotalEnDonacion","cr_lstDonacionesRecibidas" });
			
			//obtener datos de Caja
			vf01 = (Vf55ca01) ((List) m.get("lstCajas")).get(0);
			
			String codcaja = m.get("sCajaId").toString();
			String codmetodo = cmbMetodosPago.getValue().toString();
			m.put("cMpagos", codmetodo);
			restablecerEstilosPago();
			lstMoneda = new ArrayList();
			//
	    	List lstFacturasSelected = (List) m.get("selectedFacsCredito");
	    	Credhdr hFac = (Credhdr)lstFacturasSelected.get(0);
			//obtener monedas para el metodo de pago selecccionado
			sMoneda = monCtrl.obtenerMonedasxMetodosPago_Caja(Integer.parseInt(codcaja),hFac.getId().getCodcomp(),cmbMetodosPago.getValue().toString());
			
			if (sMoneda != null && sMoneda.length > 0) {
				for (int i = 0; i < sMoneda.length; i++){
					lstMoneda.add(new SelectItem(sMoneda[i],sMoneda[i]));
				}
			}
			
			m.put("lstMonedaCre", lstMoneda);	
			txtReferencia1.setValue("");
			txtReferencia2.setValue("");
			txtReferencia3.setValue("");
			cambiarVistaMetodos(codmetodo,vf01);
		}catch(Exception ex){
			LogCajaService.CreateLog("setMetodosPago", "ERR", ex.getMessage());
		}
		
	}
/****************************************************************************************/
	/****************REESTABLECER ESTILOS A DATOS DEL PAGO****************************************************************/
	public void restablecerEstilosPago(){
		txtMonto.setStyleClass("frmInput2");
		txtReferencia1.setStyleClass("frmInput2");
		txtReferencia2.setStyleClass("frmInput2");
		txtReferencia3.setStyleClass("frmInput2");
		track.setStyleClass("frmInput2");
		txtFechaVenceT.setStyleClass("frmInput2");
		txtNoTarjeta.setStyleClass("frmInput2");
	}

	/**********************************************************************************************************/
	/*******************CAMBIAR VISTAS DE METODOS DE PAGO********************************************/
	public void cambiarVistaMetodos(String sCodMetodo, Vf55ca01 vf01){
		
		try {
		
			lbletVouchermanual.setStyle("visibility:hidden; display:none");
			chkVoucherManual.setChecked(false);
			chkVoucherManual.setStyle("width:20px; visibility:hidden; display:none");
			ddlTipoMarcasTarjetas.setStyle("display:none");
			lblMarcaTarjeta.setStyle("display:none;");
			
			
			//---metodo = TC
			if(sCodMetodo.equals(MetodosPagoCtrl.TARJETA )) {		
				
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
						lblTrack.setValue("Banda:");
						track.setStyle("display:inline");
					}
				}		
			//metodo = CHK	
			} else if(sCodMetodo.equals(MetodosPagoCtrl.CHEQUE)) {
				
				//Set to blank
				lblAfiliado.setValue("");
				lblReferencia1.setValue("No. Cheque:");
				lblReferencia2.setValue("Emisor:");
				lblReferencia3.setValue("Portador:");			
				lblBanco.setValue("Banco:");								
				
				//Set to visible
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
				
			//metodo = EFEC	
			} else if(sCodMetodo.equals(MetodosPagoCtrl.EFECTIVO)) {
				
				//Set to blank
				lblAfiliado.setValue("");
				lblReferencia1.setValue("");
				lblReferencia2.setValue("");
				lblReferencia3.setValue("");
				lblBanco.setValue("");
				
				//Set to not visivble
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
			//Transferencia bancaria
			else if (sCodMetodo.equals(MetodosPagoCtrl.TRANSFERENCIA)){
				//Set to blank
				lblAfiliado.setValue("");
				lblReferencia1.setValue("Identificación:");
				lblReferencia2.setValue("Referencia:");
				lblReferencia3.setValue("");
				lblBanco.setValue("Banco:");
				
				//Set to visible
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
			//Deposito de banco
			else if (sCodMetodo.equals( MetodosPagoCtrl.DEPOSITO )){
				//Set to blank
				lblAfiliado.setValue("");
				lblReferencia1.setValue("");
				lblReferencia2.setValue("Referencia:");
				lblReferencia3.setValue("");
				lblBanco.setValue("Banco:");
				
				//Set to visible
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
			
			Object[] igObjects = new Object[]{ 
					ddlTipoMarcasTarjetas, lblMarcaTarjeta
			};
			
			CodeUtil.refreshIgObjects(igObjects);
			
		} catch (Exception error) {
			 error.printStackTrace(); 
		}		
	}
/****************************************************************************************/
	public void setMoneda(ValueChangeEvent e) {
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		String sMoneda; 
		String[] sMetodosPago = null;
		MetodosPagoCtrl metPagoCtrl = new MetodosPagoCtrl();
		Metpago[] metpago = null;
		Tpararela[] tpcambio = null;
		Cafiliados[] cafiliado = null;
		AfiliadoCtrl afCtrl = new AfiliadoCtrl();
		
		try{
			 Vf55ca01 caja = ( (List<Vf55ca01>) m.get("lstCajas")).get(0);
			
	    	List lstFacturasSelected = (List) m.get("selectedFacsCredito");
	    	
	    	if(lstFacturasSelected == null || lstFacturasSelected.isEmpty())
	    		return;
	    	
	    	Credhdr hFac = (Credhdr)lstFacturasSelected.get(0);

			tpcambio = (Tpararela[])m.get("tpcambio");
			if(tpcambio == null){
				lblMensajeValidacion.setValue("Tasa de cambio paralela no esta configurada");
				dwProcesa.setWindowState("normal");
				dwProcesa.setStyle("width:370px;height:160px");
				return;
			}
			
			lstMetodosPago = new ArrayList();
			sMoneda = cmbMoneda.getValue().toString();

			sMetodosPago = metPagoCtrl.obtenerMetodoPagoxCaja_Moneda(
									caja.getId().getCaid(), 
									hFac.getId().getCodcomp(), sMoneda);
		    	
	    	for(int i = 0; i <  sMetodosPago.length; i ++){
	    		Metpago formapago = com.casapellas.controles.MetodosPagoCtrl.metodoPagoCodigo( sMetodosPago[i] );
	    		lstMetodosPago.add(new SelectItem( formapago.getId().getCodigo(), formapago.getId().getMpago() ) );
	    	}
	    	
	    	m.put("lstMetodosPagoCre", lstMetodosPago);
	    	
	    	//&& ========== cargar afiliados
			String sLineaFactura = (hFac.getId().getCodunineg().trim()).substring(2, 4);
	    	lstAfiliado = caja.getId().getCasktpos() == 'N' ?
					getLstAfiliados(caja.getId().getCaid(), hFac.getId()
							.getCodcomp(), sLineaFactura, sMoneda):
					PosCtrl.getAfiliadosSp(caja.getId().getCaid(), 
							hFac.getId().getCodcomp(), 
							sLineaFactura, sMoneda) ;	
					
			m.put("lstAfiliadoCre", lstAfiliado);
			ddlAfiliado.dataBind();
			
		}catch(Exception ex){
			ex.printStackTrace();
		}		
	}
/**************************AGREGAR METODOS DE PAGO A LA LISTA DESDE LINK AGREGAR*********************************************/
	public void registrarPago(ActionEvent e) {
		Divisas d = new Divisas();
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		
		BigDecimal tasa = BigDecimal.ONE;
		
		int cont = 1;
		double montoRecibido = 0;
		double total, monto = 0.0, equiv;
		
		boolean valido = false, bTarjeta = false;
		Tpararela[] tcPar = null;
		Tcambio[] tcJDE = null;
		Credhdr hFac = null;
		
		final String sCodcomp;
		String sVoucherManual="0";
		String sTotal, sMonto = "", sEquiv;
		String sCodigoMetPago;
		
		Vf55ca01 caja = null;
		List<String> lstDatosTrack  = null;
		List lstDatosTrack2 = null;
		
		F55ca014 dtComp;
		
		try {
			
			caja = (Vf55ca01) ((List) m.get("lstCajas")).get(0);
			sVoucherManual = (chkVoucherManual.isChecked())?"1":"0";
			
			boolean usaSocketPos  = caja.getId().getCasktpos() == 'Y';
			boolean ingresoManual = chkIngresoManual.isChecked();
			boolean voucherManual = chkVoucherManual.isChecked();
			
			sCodigoMetPago = getCmbMetodosPago().getValue().toString();
			String metodo  = getCmbMetodosPago().getValue().toString();
			String moneda = cmbMoneda.getValue().toString();
			String ref1 =  txtReferencia1.getValue().toString().trim();
			String ref2 =  txtReferencia2.getValue().toString().trim();
			String ref3 =  txtReferencia3.getValue().toString().trim();
			String ref4 = new String("");
			String ref5 = new String("");
			String sTrack = new String("");
			String sTerminal = new String("");
			
			// obtener datos de factura
			ArrayList<Credhdr> lstFacturasSelected = (ArrayList<Credhdr>) m.get("selectedFacsCredito");
			
			hFac = lstFacturasSelected.get(0);
			sCodcomp = hFac.getId().getCodcomp();
			
			//&& ======== Datos de la compania.
			dtComp = com.casapellas.controles.tmp.CompaniaCtrl
						.filtrarCompania((F55ca014[])m.get
						("cr_lstComxCaja"), sCodcomp);
	    	
			boolean bConfirmAuto = dtComp.getId().isConfrmauto();
			String sMonedaBase = dtComp.getId().getC4bcrcd();
			
			valido = validarPago(moneda, sMonedaBase, lstMoneda, hFac, 
								usaSocketPos, voucherManual, metodo, 
								ref1, ref2, ref3,bConfirmAuto);
			if(!valido){
				SmartRefreshManager.getCurrentInstance().addSmartRefreshId(
						dwProcesa.getClientId(FacesContext.getCurrentInstance()));
				SmartRefreshManager.getCurrentInstance().addSmartRefreshId(
						dwSolicitud.getClientId(FacesContext.getCurrentInstance()));
				return;
			}
			
			selectedMet = (ArrayList<MetodosPago>) m.get("lstPagos");
			List<Metpago[]> lstMetPago = (ArrayList<Metpago[]>) m.get("metpago");
				
			monto = Double.parseDouble(txtMonto.getValue().toString());
			
			//&& ============= restar el monto de la donacion antes de la validacion del monto del pago.
			
			boolean aplicadonacion = ( CodeUtil.getFromSessionMap("cr_MontoTotalEnDonacion") != null );
			BigDecimal montooriginal = new BigDecimal(Double.toString(monto));
			BigDecimal montoNeto = new BigDecimal(Double.toString(monto));
			BigDecimal montoendonacion = BigDecimal.ZERO;
			
			if(aplicadonacion){
				montoendonacion = new BigDecimal( String.valueOf( CodeUtil.getFromSessionMap("cr_MontoTotalEnDonacion") ) );
				montoNeto = montooriginal.subtract(montoendonacion) ;
				monto = montoNeto.doubleValue() ;
			}
			
			//=========================================================================================
			
			sMonto = d.formatDouble(monto);
			equiv = monto;
			
			if ( metodo.compareTo(MetodosPagoCtrl.CHEQUE) == 0 ){//cheque
				ref4 = ref3;
				ref3 = ref2;
				ref2 = ref1;
				ref1 = ddlBanco.getValue().toString().split("@")[1];
				
			}else if( metodo.compareTo(MetodosPagoCtrl.TARJETA) == 0 ){//TC
				ref3 = ref2;
				ref2 = ref1;
				ref1 = ddlAfiliado.getValue().toString();	
				bTarjeta = true;
				
				if(usaSocketPos   && !voucherManual ){
					
					ref1 = ((ddlAfiliado.getValue().toString()).split(","))[0];
					sTerminal = ((ddlAfiliado.getValue().toString()).split(","))[1];	
					
					if( ingresoManual ){//manual
						String snoT = txtNoTarjeta.getValue().toString().trim();
						ref3 = (snoT).substring(snoT.length()-4, snoT.length());//4 ult. digitos de tarjeta
						ref4 = txtNoTarjeta.getValue().toString();	
						ref5 = txtFechaVenceT.getValue().toString();	
					}else{
						sTrack = track.getValue().toString();
						sTrack = d.rehacerCadenaTrack(sTrack);
						lstDatosTrack = d.obtenerDatosTrack(sTrack);
						
						//4 ult. digitos de tarjeta
						ref3 = lstDatosTrack.get(1)
								.substring(lstDatosTrack.get(1).length()-4,
										lstDatosTrack.get(1).length());
						
						lstDatosTrack2 = m.containsKey("lstDatosTrack_Con")?
											(List)m.get("lstDatosTrack_Con"):
											new ArrayList() ;
						lstDatosTrack2.add(lstDatosTrack);
						m.put("lstDatosTrack_Con", lstDatosTrack2);
					}
				}
				else if( usaSocketPos   && voucherManual ){
					ref1 = ((ddlAfiliado.getValue().toString()).split(","))[0];
					sTerminal = ((ddlAfiliado.getValue().toString()).split(","))[1];	
				}
				
			}else if(metodo.compareTo( MetodosPagoCtrl.TRANSFERENCIA ) == 0 ){//Transf. Elect.
				ref3 = ref2;
				ref2 = ref1;
				ref1 = ddlBanco.getValue().toString().split("@")[1];		
			}else if(metodo.compareTo(MetodosPagoCtrl.DEPOSITO) == 0 ){//Deposito en banco
				ref1 = ddlBanco.getValue().toString().split("@")[1];
			}

			int cant = 1;
			boolean flgpagos = true;
			Metpago[] metpago = null;

			// poner descripcion a metodo
			for (int m = 0; m < lstMetPago.size(); m++) {
				metpago = (Metpago[]) lstMetPago.get(m);
				if (metodo.trim().equals(
						metpago[0].getId().getCodigo().trim())) {
					metodo = metpago[0].getId().getMpago().trim();
					break;
				}
			}

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
					equiv = d.roundDouble(monto * tasa.doubleValue());
				}
			} else {// foranea
				if (moneda.equals(sMonedaBase)) {
					tcPar = (Tpararela[]) m.get("tpcambio");
					// buscar tasa de cambio paralela para moneda
					for (int t = 0; t < tcPar.length; t++) {
						if (tcPar[t].getId().getCmono().equals(moneda) || tcPar[t].getId().getCmond().equals(moneda)) {
							tasa = tcPar[t].getId().getTcambiom();
							break;
						}
					}
					
					equiv = d.roundDouble(monto / tasa.doubleValue());
				} else {
					tasa = BigDecimal.ONE;
					equiv = monto;
				}
			}
			sEquiv = d.formatDouble(equiv);
			sMonto = d.formatDouble(monto);
			
			
			String codigomarcatarjeta = "";
			String marcatarjeta = "";
			
			if( bTarjeta ){
				codigomarcatarjeta = ddlTipoMarcasTarjetas.getValue().toString().split("@")[0];
				marcatarjeta = ddlTipoMarcasTarjetas.getValue().toString().split("@")[1];
			}

			MetodosPago metpagos = null;
			if (selectedMet != null) {

				//========= valida que se agreguen los metodos iguales a un solo registro =======//
				for (MetodosPago mp : selectedMet) {
					if (mp.getMetodo().equals(sCodigoMetPago)
							&& mp.getMoneda().equals(moneda)
							&& mp.getReferencia().equals(ref1)
							&& mp.getReferencia2().equals(ref2)
							&& mp.getReferencia3().equals(ref3)
							&& mp.getReferencia4().equals(ref4)) {
						
						monto = monto + (mp.getMonto());
						equiv = equiv + (mp.getEquivalente());

						sMonto = d.formatDouble(monto);
						mp.setMonto(monto);
						mp.setMontorecibido(new BigDecimal(monto));

						sEquiv = d.formatDouble(equiv);
						mp.setEquivalente(equiv);
						mp.setReferencia5("");
						
						if(bTarjeta && usaSocketPos   && !voucherManual ){
							mp.setTrack(sTrack);
							mp.setTerminal(sTerminal);
							mp.setReferencia5(ref5);
							mp.setMontopos(monto);
							mp.setVmanual("2");
						} 
						
						mp.setReferencia6("");
						mp.setReferencia7("");
						mp.setNombre("");
						
						metpagos = mp;
						
						flgpagos = false;
					}
				}
				
			} else {
				metpagos = new MetodosPago(metodo, sCodigoMetPago, moneda,
						monto, tasa, equiv, ref1, ref2, ref3, ref4,
						sVoucherManual, 0);
				
				metpagos.setMontorecibido(montooriginal );
				metpagos.setMontoendonacion(montoendonacion);
				metpagos.setIncluyedonacion(aplicadonacion);
				
				if(aplicadonacion){
					metpagos.setDonaciones(
						new ArrayList<DncIngresoDonacion>((ArrayList<DncIngresoDonacion>)
								CodeUtil.getFromSessionMap("cr_lstDonacionesRecibidas")) ) ;
				}
				
				
				metpagos.setReferencia5("");
				if (bTarjeta && usaSocketPos && !voucherManual) {
					metpagos.setTrack(sTrack);
					metpagos.setTerminal(sTerminal);
					metpagos.setReferencia5(ref5);
					metpagos.setMontopos(monto);
					metpagos.setVmanual("2");
				} 					
				metpagos.setReferencia6("");
				metpagos.setReferencia7("");
				metpagos.setNombre("");
				
				selectedMet = new ArrayList();
				selectedMet.add(metpagos);
				
				flgpagos = false;
			}

			if (flgpagos) {
				metpagos = new MetodosPago(metodo, sCodigoMetPago, moneda,
						monto, tasa, equiv, ref1, ref2, ref3, ref4,
						sVoucherManual, 0);
				
				metpagos.setMontorecibido(montooriginal );
				metpagos.setMontoendonacion(montoendonacion);
				metpagos.setIncluyedonacion(aplicadonacion);
				
				if(aplicadonacion){
					metpagos.setDonaciones(
						new ArrayList<DncIngresoDonacion>((ArrayList<DncIngresoDonacion>)
								CodeUtil.getFromSessionMap("cr_lstDonacionesRecibidas")) ) ;
				}
				
				metpagos.setReferencia5("");
				
				if (bTarjeta && usaSocketPos && !voucherManual) {
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
				
				selectedMet.add(metpagos);
			}
			
			if(aplicadonacion){
				CodeUtil.removeFromSessionMap(new String[] { "cr_MontoTotalEnDonacion","cr_lstDonacionesRecibidas" });
			}
			
			
			//&& =========== Redondeo de faltantes al total recibido contra monto aplicado.
			BigDecimal totalFacts = BigDecimal.ZERO;
			BigDecimal totalEquiv = BigDecimal.ZERO;
			for (MetodosPago mp : selectedMet) 
				totalEquiv = totalEquiv.add(new BigDecimal(
						Double.toString( mp.getEquivalente())));
			 
			List<Credhdr>selectedFacs = (ArrayList<Credhdr>)m.get("selectedFacsCredito");
			for (Credhdr credhdr : selectedFacs) {
				totalFacts = totalFacts.add( (
						(hFac.getMoneda().equals(sMonedaBase))?
							credhdr.getId().getCpendiente() :
							credhdr.getId().getDpendiente() ) );
			}
			if( m.containsKey("dMontoAplicarCre") ){
				totalFacts = new BigDecimal(m.get("dMontoAplicarCre").toString());
			}
			
			if(totalFacts.compareTo(totalEquiv) == 1 && totalFacts.subtract(totalEquiv)
					.compareTo(new BigDecimal("0.30")) == -1  && metpagos.getMonto() != metpagos.getEquivalente() ){
				
				selectedMet.get(selectedMet.size()-1).setEquivalente(
					Divisas.roundDouble( totalFacts.subtract(totalEquiv)
						.add(new BigDecimal( Double.toString(selectedMet
							.get(selectedMet.size()-1).getEquivalente())))
							.doubleValue(), 2)  
						) ;
			}
			
			//&& ========= Guardar el objeto del método de pago que genera el sobrante ======== &&//
			if(m.get("scr_SobrantePago") != null){
				metpagos.setExcedente(new BigDecimal(String.valueOf(m.get("scr_SobrantePago") ))) ;
				m.put("scr_MpagoSobrante", metpagos);
			}	
			else{
				m.remove("scr_MpagoSobrante");
			}
			//&& ======== calcular el monto recibido en dependencia de moneda de factura y moneda de pago
			montoRecibido = calcularElMontoRecibido(selectedMet, hFac, sMonedaBase);
			boolean bEfectivo = false;
			List<MetodosPago>lstMetodos = (ArrayList<MetodosPago>)selectedMet;
			for (MetodosPago pago : lstMetodos) {
				if(pago.getMetodo().trim().toLowerCase().equals(MetodosPagoCtrl.EFECTIVO)){
					bEfectivo = true;
					break;
				}
			}
			//&& ======== determinar como dar el cambio
			lblMarcaSobrDifer.setStyle("display: none");
			chkSobranteDifrl.setStyle("display: none");
			SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
			if(bEfectivo){
				m.remove("scr_SbrDfr");
				m.remove("scr_SobrantePago");
				m.remove("scr_MpagoSobrante");
				determinarCambio(selectedMet, hFac, montoRecibido,sMonedaBase);
			}else{
				if(m.get("scr_SobrantePago") != null){
					if(hFac.getId().getCodcomp().trim().equals("E03") && m.get("scr_SbrDfr")!=null){
						lblMarcaSobrDifer.setStyle("display: inline");
						chkSobranteDifrl.setStyle("display: inline");
					}
					lblCambio.setValue("Cambio: "+hFac.getMoneda());
					txtCambio.setValue(d.formatDouble(0));
					lblPendienteDomestico.setValue("");
					txtPendienteDomestico.setValue("");
					lblCambioDomestico.setValue("");
					txtCambioDomestico.setValue("");
					
					srm.addSmartRefreshId(lblCambio.getClientId(FacesContext.getCurrentInstance()));
					srm.addSmartRefreshId(txtCambio.getClientId(FacesContext.getCurrentInstance()));
				}else{
					txtPendienteDomestico.setValue("0.00");
				}
			}
			srm.addSmartRefreshId(lblMarcaSobrDifer.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(chkSobranteDifrl.getClientId(FacesContext.getCurrentInstance()));
			
			//&& ======== determinar el monto aplicado a las facturas, realizar las operaciones para totales y cambios
			distribuirMontoAplicar(sMonedaBase, lstFacturasSelected );
			calcularTotales(lstFacturasSelected,sMonedaBase);
			m.put("lstPagos", selectedMet);
			m.put("mpagos", selectedMet);

			txtMonto.setValue("");
			txtReferencia1.setValue("");
			txtReferencia2.setValue("");
			txtReferencia3.setValue("");
			txtCambioForaneo.setStyleClass("frmInput2");
			track.setValue("");
			txtCambioForaneo.setStyleClass("frmInput2");
			txtNoTarjeta.setValue("");	
			txtFechaVenceT.setValue("");	
			
			metodosGrid.dataBind();

			SmartRefreshManager.getCurrentInstance().addSmartRefreshId(
					metodosGrid.getClientId(FacesContext
							.getCurrentInstance()));
			SmartRefreshManager.getCurrentInstance().addSmartRefreshId(
					track.getClientId(FacesContext
							.getCurrentInstance()));
			
		} catch (Exception ex) { 
			ex.printStackTrace();
		}
	}
/***********************************VALIDAR LOS DATOS DEL PAGO INGRESADO************************************/
	public boolean validarPago(String sMonedaPago, String sMonedaBase, 
							List lstMoneda, Credhdr hFac, boolean usaSckPos,
							boolean usaManual, String metodo, String ref1,
							String ref2,String ref3, boolean bConfirmAuto ){
		
		String sCodcomp;
		String ref4 = new String("");
		String sMonto = new String("");
		String sCajaId = new String("");
		String sMensajeError = new String("");
		F55ca014 dtComp  = null;
		
		int y = 158;
		boolean validado = true;
		double dEquivalente = 0, monto = 0;
		double dMontoAplicar = 0;
		double montoaplequiv = 0;
		
		CtrlCajas cc = new CtrlCajas();
		Divisas divisas = new Divisas();
		CtrlPoliticas polCtrl = new CtrlPoliticas();
		
		Pattern pNumero = null;
		Matcher matVoucher = null;
		BigDecimal tasa = BigDecimal.ZERO ;
		BigDecimal tasaPar = BigDecimal.ONE;
		BigDecimal tasaJDE = BigDecimal.ONE;
		Credhdr hFacSel = null;
		int iCaid = 0 ;
		
		try{

			
//			metodo = MetodosPagoCtrl.equivalenciaTipoPago(metodo); 
			
			
			//&& ====== Validacion de tasa de cambio paralela.
			if (lstMoneda.size() > 1 && !m.containsKey("tpcambio")) {
				lblMensajeValidacion.setValue("Tasa de cambio paralela" +
						" no esta configurada");
				dwProcesa.setWindowState("normal");
				return false;
			}
			
			//&& ====== Determinar si la caja confirma para la compania seleccionada.
			if(m.get("cr_lstComxCaja") ==  null){
				sMensajeError = "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" />";
				sMensajeError += "No se puede validar configuración de caja para confirmación de depósitos";
				lblMensajeValidacion.setValue(sMensajeError);
				dwProcesa.setStyle("width:320px;height: 180px");
				dwProcesa.setWindowState("normal");
				return false;
			}
			
	    	List<Credhdr> lstFacturasSelected = (List<Credhdr>)
	    								m.get("selectedFacsCredito");
	    	sCodcomp = hFac.getId().getCodcomp();
	    	
			selectedMet = (ArrayList<MetodosPago>) m.get("lstPagos");
			sCajaId = (String)m.get("sCajaId");
			iCaid   = Integer.parseInt(sCajaId);
			restablecerEstilosPago();
	    	
			//expresion regular solo numeros
			Matcher matNumero = null;
			if(txtMonto.getValue() != null){
				sMonto = txtMonto.getValue().toString().trim();
				pNumero = Pattern.compile("^[0-9]+\\.?[0-9]*$");
				matNumero = pNumero.matcher(sMonto);
			}
			if (!sMonto.equals("") && matNumero.matches()){
				monto = divisas.formatStringToDouble( sMonto );
			}
			
			//expresion regular valores alfanumericos
			Matcher matAlfa1 = null;
			Matcher matAlfa2 = null;
			Matcher matAlfa3 = null;
			Matcher matAlfa4 = null;
			
			if(txtMonto.getValue() != null){
				sMonto = txtMonto.getValue().toString().trim();
				Pattern pAlfa = Pattern.compile("^[A-Za-z0-9-\\p{Blank}]+$");
				Pattern pAlfaRef1 = Pattern.compile("^[A-Za-z0-9-]+$");
				matAlfa1 = pAlfaRef1.matcher(ref1);
				matAlfa2 = pAlfa.matcher(ref2);
				matAlfa3 = pAlfa.matcher(ref3);
				matAlfa4 = pAlfa.matcher(ref4);
			}
			
			//========================================================================================
			
			//&& ============= restar el monto de la donacion antes de la validacion del monto del pago.
			BigDecimal montoNeto = BigDecimal.ZERO;
			BigDecimal montooriginal = BigDecimal.ZERO;
			BigDecimal montoendonacion = BigDecimal.ZERO;
			sMonto = sMonto.trim().trim();
			
			if ( sMonto.matches(PropertiesSystem.REGEXP_AMOUNT) ) {
				montooriginal = new BigDecimal(sMonto);
				montoNeto = new BigDecimal(sMonto);
			}
			
			boolean aplicadonacion = ( CodeUtil.getFromSessionMap("cr_MontoTotalEnDonacion") != null );
			
			if(aplicadonacion){
				montoendonacion = new BigDecimal( String.valueOf( CodeUtil.getFromSessionMap("cr_MontoTotalEnDonacion") ) );
				montoNeto = montooriginal.subtract(montoendonacion) ;
				sMonto = montoNeto.toString().trim();
			}
			
			//=========================================================================================
			
			if(lstMoneda.size()>1){
				//obtener tasa del cambio
				Tpararela[] tcPar = (Tpararela[])m.get("tpcambio");
				//buscar tasa de cambio paralela para moneda
				for(int t = 0; t < tcPar.length;t++){
					if(tcPar[t].getId().getCmono().equals(sMonedaBase) || tcPar[t].getId().getCmond().equals(sMonedaBase)){
						tasa = tcPar[t].getId().getTcambiom();
						tasaPar = tcPar[t].getId().getTcambiom();
						break;
					}
				}
				Tcambio[] tcJDE = (Tcambio[])m.get("tcambio");
				//buscar tasa de cambio JDE para moneda
				for(int l = 0; l < tcJDE.length;l++){
					if(tcJDE[l].getId().getCxcrdc().equals(sMonedaPago)){
						tasaJDE = tcJDE[l].getId().getCxcrrd();
						tasa = tcJDE[l].getId().getCxcrrd();
						break;
					}
				}
			}
			//sumar el monto a aplicar de las facturas
			for (int z = 0; z < lstFacturasSelected.size();z++){
				hFacSel = (Credhdr)lstFacturasSelected.get(z);
				dMontoAplicar = dMontoAplicar + hFacSel.getMontoPendiente().doubleValue();
			}
			
			montoaplequiv =  dMontoAplicar;
			dEquivalente = monto;
			
			if((!hFac.getMoneda().equals(sMonedaBase)) && sMonedaPago.equals(sMonedaBase)){
				dMontoAplicar = divisas.roundDouble(dMontoAplicar*tasaPar.doubleValue());
				dEquivalente = divisas.roundDouble(monto / tasa.doubleValue());
			}
			if((hFac.getMoneda().equals(sMonedaBase)) && !sMonedaPago.equals(sMonedaBase)){
				dMontoAplicar = divisas.roundDouble(dMontoAplicar/tasaJDE.doubleValue());//se usa la tasa jde
				dEquivalente = divisas.roundDouble(monto * tasa.doubleValue());
			}
			
			boolean donaciontotal = aplicadonacion && montoNeto.compareTo(BigDecimal.ZERO) == 0 ;
			if(donaciontotal){
				montoNeto = montooriginal;
				dMontoAplicar = montooriginal.add(BigDecimal.TEN).doubleValue();
				dEquivalente  = dMontoAplicar;
			}
			
			monto = montoNeto.doubleValue();
			
			
			//&& ===== Validar que si hay sobrante no se agreguen mas metodos de pago.
			 if (matNumero == null || !matNumero.matches() || monto == 0){
				validado = false;
				sMensajeError += "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" />";
				sMensajeError += "El monto ingresado no es correcto<br>";
			}else if(m.get("scr_MpagoSobrante")!=null){
				validado = false;
				sMensajeError += "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" />";
				sMensajeError += "El Monto de la factura ya ha sido alcanzado por otro metodo de pago";
			}
			 if(!validado){
				 txtMonto.setValue(""); 
				 txtMonto.setStyleClass("frmInput2Error");
				 lblMensajeValidacion.setValue(sMensajeError);
				 dwProcesa.setStyle("width:350px;height:165px");
				 dwProcesa.setWindowState("normal");
				 return validado;
			 }
			 
			//&& ====== Valida efectivo
			  
			if (  metodo.compareTo( MetodosPagoCtrl.EFECTIVO ) == 0 ){
				 
				if(sMonto.equals("")){
					validado = false;
					txtMonto.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El monto es requerido<br>";
					dwProcesa.setWindowState("normal");	
					y = y + 5;
				}
				else if (matNumero == null || !matNumero.matches() || monto == 0){
					txtMonto.setValue(""); 
					validado = false;
					txtMonto.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El monto ingresado no es correcto<br>";
					dwProcesa.setWindowState("normal");	
					y = y + 5;
				}
				else if (!polCtrl.validarMonto(Integer.parseInt(sCajaId),sCodcomp, cmbMoneda.getValue().toString(), cmbMetodosPago.getValue().toString(), monto)){
					validado = false;
					lblMensajeAutorizacion.setValue("El monto ingresado no cumple con las politicas de caja");
					dwSolicitud.setWindowState("normal");
					Date dFechaActual = new Date();
					txtFecha.setValue(dFechaActual);
					if(!txtMonto.getValue().toString().trim().equals("")){
						sMonto = txtMonto.getValue().toString().trim();
					}
					monto = Double.parseDouble(sMonto);
					m.put("montoIsertando", monto);
					m.put("monto", monto);
				}
				else if(selectedMet.size() > 0 && !donaciontotal ){
					List<MetodosPago> lstPagos = (ArrayList<MetodosPago>)selectedMet;
					for (MetodosPago pago : lstPagos) {
						if( pago.getMetodo().equals(metodo) && pago.getMoneda().equals(sMonedaPago)){
							monto += pago.getMonto();
							dEquivalente += (hFac.getMoneda().equals(pago.getMoneda()))?
											 pago.getMonto():
										     pago.getEquivalente();
						}
					}
					dEquivalente = divisas.roundDouble(dEquivalente);
					
					if (!polCtrl.validarMonto(Integer.parseInt(sCajaId),sCodcomp, sMonedaPago, metodo, monto)){
						validado = false;
						lblMensajeAutorizacion.setValue("El consolidado de los montos del método de pago no cumple con las politicas de caja");
						dwSolicitud.setWindowState("normal");
						Date dFechaActual = new Date();
						txtFecha.setValue(dFechaActual);
						m.put("monto", monto);
						if(!txtMonto.getValue().toString().trim().equals("")){
							sMonto = txtMonto.getValue().toString().trim();
						}
						monto = Double.parseDouble(sMonto);
						m.put("montoIsertando", monto);
					}
				}
				//---- Remover la variable de control de sobrante de pago en caso de que haya efectivo.
				if(validado){
				    m.remove("scr_SobrantePago");
					m.remove("scr_MpagoSobrante");
					m.remove("scr_SbrDfr");
				}
			//Valida CHK
			}else if (  metodo.compareTo( MetodosPagoCtrl.CHEQUE ) == 0 ){
				//validar montos
				if(sMonto.equals("")){
					validado = false;
					txtMonto.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El monto es requerido<br>";
					dwProcesa.setWindowState("normal");
					y = y + 7;
				}
				else if (matNumero == null || !matNumero.matches() || monto == 0){
					txtMonto.setValue(""); 
					validado = false;
					txtMonto.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El monto ingresado no es correcto<br>";
					dwProcesa.setWindowState("normal");	
					y = y + 7;
				}
				//========= validar los sobrantes en caja para el metodo.================//
				else if (divisas.roundDouble(monto) > divisas.roundDouble(dMontoAplicar)){
					double dSobrante = monto - dMontoAplicar;
					dSobrante = divisas.roundDouble(dSobrante);
					//--- Validar conversión de monedas.
					if( dSobrante > dMaximoSobrante){
						validado = false;
						txtMonto.setStyleClass("frmInput2Error");
						sMensajeError += "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" />";
						sMensajeError += " El monto ingresado para este método de pago debe ser menor o igual al monto a aplicar<br>"; 
						sMensajeError += "monto: " + divisas.roundDouble(monto) + " Monto a aplicar: " + divisas.roundDouble(dMontoAplicar);
						dwProcesa.setWindowState("normal");	
						y = y + 7;
					}else{
						if(hFac.getMoneda().equals(sMonedaPago) && hFac.getMoneda().equals(sMonedaBase)){
							m.put("scr_SbrDfr", "1");
						}else{
							m.remove("scr_SbrDfr");
						}
						m.remove("scr_SobrantePago");
						m.put("scr_SobrantePago", divisas.roundDouble(dSobrante));
					}
				}
				//validar referencias
				if(!ref1.matches("^[0-9]{1,8}$")){
					validado = false;
					y += 7;
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" />1 a 8 Dígitos para número de cheque es requerido<br>";
					txtReferencia1.setStyleClass("frmInput2Error");
					dwProcesa.setWindowState("normal");
				}
				else if(ref2.equals("")){
					validado = false;
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Emisor requerido<br>";
					txtReferencia2.setStyleClass("frmInput2Error");
					dwProcesa.setWindowState("normal");
					y = y + 7;
				}else if(!matAlfa2.matches()){
					validado = false;
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El campo <b>Emisor<b/> contiene caracteres invalidos<br>";
					txtReferencia2.setStyleClass("frmInput2Error");
					dwProcesa.setWindowState("normal");				
					y = y + 7;
				}else if(ref2.length() > 150){
					validado = false;
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> La cantidad de caracteres del campo <b>Emisor<b/> es muy alta (lim. 150)<br>";
					txtReferencia2.setStyleClass("frmInput2Error");
					dwProcesa.setWindowState("normal");				
					y = y + 15;
				}
				if(ref3.equals("")){
					validado = false;
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Portador requerido<br>";
					txtReferencia3.setStyleClass("frmInput2Error");
					dwProcesa.setWindowState("normal");	
					y = y + 7;
				}else if(!matAlfa3.matches()){
					validado = false;
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El campo <b>Portador<b/> contiene caracteres invalidos<br>";
					txtReferencia3.setStyleClass("frmInput2Error");
					dwProcesa.setWindowState("normal");				
					y = y + 7;
				}
				else if(ref3.length() > 150){
					validado = false;
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> La cantidad de caracteres del campo <b>Portador<b/> es muy alta (lim. 150)<br>";
					txtReferencia3.setStyleClass("frmInput2Error");
					dwProcesa.setWindowState("normal");				
					y = y + 15;
				}
				else if(!sMonto.equals("")){
					if (!polCtrl.validarMonto(Integer.parseInt(sCajaId), sCodcomp, sMonedaPago, metodo, monto)){
						validado = false;
						lblMensajeAutorizacion.setValue("El monto ingresado no cumple con las politicas de caja");
						dwSolicitud.setWindowState("normal");
						Date dFechaActual = new Date();
						txtFecha.setValue(dFechaActual);
						if(!txtMonto.getValue().toString().trim().equals("")){
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
							dEquivalente    += (hFac.getMoneda().equals(pago.getMoneda()))?
											 pago.getMonto():
										     pago.getEquivalente();
						}
						dEquivalente = divisas.roundDouble(dEquivalente  );
						
						if (!polCtrl.validarMonto(Integer.parseInt(sCajaId),sCodcomp, sMonedaPago, metodo,monto)){
							validado = false;
							lblMensajeAutorizacion.setValue("El consolidado de los montos del método no cumple con las politicas de caja");
							dwSolicitud.setWindowState("normal");
							Date dFechaActual = new Date();
							txtFecha.setValue(dFechaActual);
							m.put("monto", monto);
							if(!txtMonto.getValue().toString().trim().equals("")){
								sMonto = txtMonto.getValue().toString().trim();
							}
							monto = Double.parseDouble(sMonto);
							m.put("montoIsertando", monto);
						}
						//validar consolidado de montos
						 else if ( dEquivalente > montoaplequiv ) { 

							 BigDecimal sbr = new BigDecimal(Double.toString(dEquivalente))
				 				.subtract( new BigDecimal(Double.toString(montoaplequiv))) ;
				 	 
						 	 if( montoaplequiv > dMontoAplicar)
						 		sbr = sbr.divide(new BigDecimal(Double.toString(
						 				dEquivalente)), 2, RoundingMode.HALF_UP);
						 	
						 	 if( montoaplequiv < dMontoAplicar)
						 		sbr = sbr.multiply(tasa);
						 	
						 	 double dSobrante = divisas.roundDouble(sbr.doubleValue());
							 
							 
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
									m.put("scr_SbrDfr", "1");
								}else{
									m.remove("scr_SbrDfr");
								}
								m.remove("scr_SobrantePago");
								m.put("scr_SobrantePago", divisas.roundDouble(dSobrante));
							}					
						}
					}
				}
			//Valida TC	
			} else if (  metodo.compareTo( MetodosPagoCtrl.TARJETA ) == 0 ){
				matVoucher = pNumero.matcher(ref2);
				
				if(usaSckPos && !usaManual){
					
					String codpos = ddlAfiliado.getValue().toString();
					String sTrack = track.getValue().toString();
					String notarjeta  = txtNoTarjeta.getValue().toString().trim();
					String fechavence = txtFechaVenceT.getValue().toString().trim();
					
					sMensajeError = PosCtrl.validaPagoSocket(sMonedaPago,
							dEquivalente, dMontoAplicar, usaManual, ref1, ref2,
							ref3, iCaid, sCodcomp, codpos, selectedMet, 
							sTrack, notarjeta, fechavence, monto) ;
					
					if(sMensajeError.compareTo("") != 0){  //sMensajeError = "-1"
						
						if (sMensajeError.compareTo("-1") == 0) {
							lblMensajeAutorizacion.setValue("El monto " +
									"ingresado no cumple con las politicas de caja");
							dwSolicitud.setWindowState("normal");
							txtFecha.setValue(new Date());
							m.put("montoIsertando", monto);
							m.put("monto", monto);

						}else{
							dwProcesa.setWindowState("normal");
							lblMensajeValidacion.setValue(sMensajeError);
						}
						return false;
					}
				}
				else if(sMonto.equals("")){
					validado = false;
					txtMonto.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El monto es requerido<br>";
					dwProcesa.setWindowState("normal");		
					y = y + 5;
				}
				else if (matNumero == null || !matNumero.matches() || monto == 0){
					txtMonto.setValue(""); 
					validado = false;
					txtMonto.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El monto ingresado no es correcto<br>";
					dwProcesa.setWindowState("normal");			
					y = y + 5;
				}
				else if (monto > divisas.roundDouble(dMontoAplicar)){
					validado = false;
					txtMonto.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El monto ingresado para este método de pago debe ser menor o igual al monto a aplicar<br>";
					dwProcesa.setWindowState("normal");	
					y = y + 7;
				}
				//Valida Referencias
				else if(ddlAfiliado.getValue().toString().equals("01")){
					validado = false;
					ddlAfiliado.setStyleClass("frmInput2Error2");
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Seleccione un afiliado<br>";
					dwProcesa.setWindowState("normal");	
					y = y + 5;
				}
				else if(ref1.equals("")) {						
					validado = false;
					txtReferencia1.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Identificación requerida<br>";
					dwProcesa.setWindowState("normal");	
					y = y + 5;
				}else if(!matAlfa1.matches()){
					validado = false;
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El campo <b>Identificación<b/> contiene caracteres invalidos<br>";
					txtReferencia1.setStyleClass("frmInput2Error");
					dwProcesa.setWindowState("normal");				
					y = y + 7;
				}else if(ref1.length() > 150){
					validado = false;
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> La cantidad de caracteres del campo <b>Identificación<b/> es muy alta (lim. 150)<br>";
					txtReferencia1.setStyleClass("frmInput2Error");
					dwProcesa.setWindowState("normal");				
					y = y + 15;
				}
				else if(ref2.equals("")) {						
					validado = false;
					txtReferencia2.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Voucher requerido<br>";
					dwProcesa.setWindowState("normal");	
					y = y + 5;
				}else if(!matVoucher.matches()){
					validado = false;
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El campo <b>Voucher<b/> contiene caracteres invalidos<br>";
					txtReferencia2.setStyleClass("frmInput2Error");
					dwProcesa.setWindowState("normal");				
					y = y + 7;
				}
				else if(ref2.length() > 150){
					validado = false;
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> La cantidad de caracteres del campo <b>Voucher<b/> es muy alta (lim. 150)<br>";
					txtReferencia2.setStyleClass("frmInput2Error");
					dwProcesa.setWindowState("normal");				
					y = y + 15;
				}
				else if (!polCtrl.validarMonto(Integer.parseInt(sCajaId), hFac.getId().getCodcomp(), cmbMoneda.getValue().toString(), cmbMetodosPago.getValue().toString(), monto)){
					validado = false;
					lblMensajeAutorizacion.setValue("El monto ingresado no cumple con las politicas de caja");
					dwSolicitud.setWindowState("normal");	
					Date dFechaActual = new Date();
					txtFecha.setValue(dFechaActual);
					if(!txtMonto.getValue().toString().trim().equals("")){
						sMonto = txtMonto.getValue().toString().trim();
					}
					monto = Double.parseDouble(sMonto);
					m.put("montoIsertando", monto);
					m.put("monto", monto);
				}else 
					if(selectedMet.size() > 0 && !donaciontotal ){
						List<MetodosPago> lstPagos = (ArrayList<MetodosPago>)selectedMet;
						for (MetodosPago pago : lstPagos) {
							if(pago.getMoneda().equals(sMonedaPago))
								monto += pago.getMonto();
							
							dEquivalente += (hFac.getMoneda().equals(pago.getMoneda()))?
											 pago.getMonto():
										     pago.getEquivalente();
						}
						dEquivalente = divisas.roundDouble(dEquivalente);
					
					if (!polCtrl.validarMonto(Integer.parseInt(sCajaId), sCodcomp, sMonedaPago, metodo,monto)){
						validado = false;
						lblMensajeAutorizacion.setValue("El consolidado de los montos no cumple con las politicas de caja");
						dwSolicitud.setWindowState("normal");
						Date dFechaActual = new Date();
						txtFecha.setValue(dFechaActual);
						m.put("monto", monto);
						if(!txtMonto.getValue().toString().trim().equals("")){
							sMonto = txtMonto.getValue().toString().trim();
						}
						monto = Double.parseDouble(sMonto);
						m.put("montoIsertando", monto);
					}
					//validar consolidado de montos
					 else if (dEquivalente > dMontoAplicar) {
							validado = false;
							txtMonto.setStyleClass("frmInput2Error");
							sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El Consolidado de montos ingresados para este método de pago debe ser menor o igual al monto a aplicar<br>";
							dwProcesa.setWindowState("normal");
							y = y + 7;
					}
				} 
			}
			//validar Transferencia Electronica
			else if (  metodo.compareTo( MetodosPagoCtrl.TRANSFERENCIA ) == 0 ){
				
				//validamos si la referencia agregada ya ha sido utilizada para el mismo banco 
				if(polCtrl.validarReferenciaBancaria(Integer.parseInt(sCajaId), ddlBanco.getValue().toString().split("@")[1], ref2,"T"))
				{
					y = y + 7;
					dwProcesa.setWindowState("normal");
					txtReferencia2.setStyleClass("frmInput2Error");
					sMensajeError = "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> " +
									"La referencia ya ha sido utilizada, favor verificar e ingresar una referencia válida ";
					return validado = false;
				}
				
				//vallidar montos
				if(sMonto.equals("")){
					validado = false;
					txtMonto.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El monto es requerido<br>";
					dwProcesa.setWindowState("normal");		
					y = y + 5;
				}
				else if (matNumero == null || !matNumero.matches() || monto == 0){
					txtMonto.setValue(""); 
					validado = false;
					txtMonto.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El monto ingresado no es correcto<br>";
					dwProcesa.setWindowState("normal");	
					y = y + 5;
				}
				else 
					if (monto > divisas.roundDouble(dMontoAplicar)){
						double dSobrante = monto - dMontoAplicar;
						dSobrante = divisas.roundDouble(dSobrante);
						//--- Validar conversión de monedas.
						if( dSobrante > dMaximoSobrante){
							validado = false;
							txtMonto.setStyleClass("frmInput2Error");
							sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El monto ingresado para este método de pago debe ser menor o igual al monto a aplicar<br>";
							dwProcesa.setWindowState("normal");	
							y = y + 7;
						}else{
							if(hFac.getMoneda().equals(sMonedaPago) && hFac.getMoneda().equals(sMonedaBase)){
								m.put("scr_SbrDfr", "1");
							}else{
								m.remove("scr_SbrDfr");
							}
							m.remove("scr_SobrantePago");
							m.put("scr_SobrantePago", dSobrante);
						}
					}
				//referencias
				else if(ref1.equals("")) {						
					validado = false;
					txtReferencia1.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Identificación requerida<br>";
					dwProcesa.setWindowState("normal");	
					y = y + 5;
				}else if(!matAlfa1.matches()){
					validado = false;
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El campo <b>Identificación<b/> contiene caracteres invalidos<br>";
					txtReferencia1.setStyleClass("frmInput2Error");
					dwProcesa.setWindowState("normal");				
					y = y + 7;
				}else if(ref1.length() > 150){
					validado = false;
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> La cantidad de caracteres del campo <b>Identificación<b/> es muy alta (lim. 150)<br>";
					txtReferencia1.setStyleClass("frmInput2Error");
					dwProcesa.setWindowState("normal");				
					y = y + 15;
				}
				else{ 
					
					if( !ref2.matches(PropertiesSystem.REGEXP_8DIGTS)  || Integer.parseInt( ref2.trim() ) == 0 ){
						y = y + 7;
						dwProcesa.setWindowState("normal");
						txtReferencia2.setStyleClass("frmInput2Error");
						sMensajeError = "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> " +
										"El valor de la referencia debe contener entre 1 y 8 dígitos unicamente ";
						return validado = false;
					}
					
				}				
				if (validado && !polCtrl.validarMonto(Integer.parseInt(sCajaId), sCodcomp, sMonedaPago, metodo, monto)){
					validado = false;
					lblMensajeAutorizacion.setValue("El monto ingresado no cumple con las politicas de caja");
					dwSolicitud.setWindowState("normal");	
					Date dFechaActual = new Date();
					txtFecha.setValue(dFechaActual);
					if(!txtMonto.getValue().toString().trim().equals("")){
						sMonto = txtMonto.getValue().toString().trim();
					}
					monto = Double.parseDouble(sMonto);
					m.put("montoIsertando", monto);
					m.put("monto", monto);
				}
				else 
					if(selectedMet.size() > 0  && !donaciontotal ){
						List<MetodosPago> lstPagos = (ArrayList<MetodosPago>)selectedMet;
						for (MetodosPago pago : lstPagos) {
							if(pago.getMoneda().equals(sMonedaPago))
								monto += pago.getMonto();
							dEquivalente += (hFac.getMoneda().equals(pago.getMoneda()))?
											 pago.getMonto():
										     pago.getEquivalente();
						}
						dEquivalente = divisas.roundDouble(dEquivalente);
						
						if (!polCtrl.validarMonto(Integer.parseInt(sCajaId),sCodcomp, sMonedaPago, metodo, monto)){
							validado = false;
							lblMensajeAutorizacion.setValue("El consolidado de los montos no cumple con las politicas de caja");
							dwSolicitud.setWindowState("normal");
							Date dFechaActual = new Date();
							txtFecha.setValue(dFechaActual);
							m.put("monto", monto);
							if(!txtMonto.getValue().toString().trim().equals("")){
								sMonto = txtMonto.getValue().toString();
							}
							monto = Double.parseDouble(sMonto);
							m.put("montoIsertando", monto);
						}
						//validar consolidado de montos
						 else{ if ( dEquivalente > montoaplequiv ) { 
							 
							 
							 	 BigDecimal sbr = new BigDecimal(Double.toString(dEquivalente))
							 				.subtract( new BigDecimal(Double.toString(montoaplequiv))) ;
							 	 
							 	 if( montoaplequiv > dMontoAplicar)
							 		sbr = sbr.divide(new BigDecimal(Double.toString(
							 				dEquivalente)), 2, RoundingMode.HALF_UP);
							 	
							 	 if( montoaplequiv < dMontoAplicar)
							 		sbr = sbr.multiply(tasa);
							 	
							 	 double dSobrante = divisas.roundDouble(sbr.doubleValue());
						
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
										m.put("scr_SbrDfr", "1");
									}else{
										m.remove("scr_SbrDfr");
									}
									m.remove("scr_SobrantePago");
									m.put("scr_SobrantePago", divisas.roundDouble(dSobrante));
								}
							 }
						 }
					} 
			}
			//validar Deposito en banco
			else if (  metodo.compareTo( MetodosPagoCtrl.DEPOSITO ) == 0 ){
				
				//validamos si la referencia agregada ya ha sido utilizada para el mismo banco 
				if(polCtrl.validarReferenciaBancaria(Integer.parseInt(sCajaId), ddlBanco.getValue().toString().split("@")[1], ref2,"N"))
				{
					y = y + 7;
					dwProcesa.setWindowState("normal");
					txtReferencia2.setStyleClass("frmInput2Error");
					sMensajeError = "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> " +
									"La referencia ya ha sido utilizada, favor verificar e ingresar una referencia válida ";
					return validado = false;
				}
				
				//Validamos la referencia no importa que no importa como 
				if( !ref2.matches(PropertiesSystem.REGEXP_8DIGTS) ){
					y = y + 7;
					dwProcesa.setWindowState("normal");
					txtReferencia2.setStyleClass("frmInput2Error");
					sMensajeError = "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> " +
									"El valor de la referencia debe contener entre 1 y 8 dígitos unicamente ";
					return validado = false;
				}
				
				//vallidar montos
				if(sMonto.equals("")){
					validado = false;
					txtMonto.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El monto es requerido<br>";
					dwProcesa.setWindowState("normal");		
					y = y + 5;
				}
				else if (matNumero == null || !matNumero.matches() || monto == 0){
					txtMonto.setValue(""); 
					validado = false;
					txtMonto.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El monto ingresado no es correcto<br>";
					dwProcesa.setWindowState("normal");			
					y = y + 5;
				}
				else if (monto > divisas.roundDouble(dMontoAplicar)){
					double dSobrante = monto - dMontoAplicar;
					dSobrante = divisas.roundDouble(dSobrante);
					//--- Validar conversión de monedas.
					if( dSobrante > dMaximoSobrante){
						validado = false;
						txtMonto.setStyleClass("frmInput2Error");
						txtMonto.setStyleClass("frmInput2Error");
						sMensajeError += "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" />";
						sMensajeError += "El monto ingresado para este método de pago debe ser menor o igual al monto a aplicar";
						sMensajeError += "<br>monto: "  + divisas.roundDouble(monto) + " Monto a aplicar: " + divisas.roundDouble(dMontoAplicar);
						dwProcesa.setWindowState("normal");	
						y = y + 7;
					}else{
						m.remove("scr_SobrantePago");
						m.put("scr_SobrantePago", divisas.roundDouble(dSobrante));
					}
				}
				else{ 
					
					if( !ref2.matches(PropertiesSystem.REGEXP_8DIGTS) ){
						y = y + 7;
						dwProcesa.setWindowState("normal");
						txtReferencia2.setStyleClass("frmInput2Error");
						sMensajeError = "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> " +
										"El valor de la referencia debe contener entre 1 y 8 dígitos unicamente ";
						return validado = false;
					}
					
				}
				if (!polCtrl.validarMonto(Integer.parseInt(sCajaId), sCodcomp, sMonedaPago,metodo, monto)){
					validado = false;
					lblMensajeAutorizacion.setValue("El monto ingresado no cumple con las politicas de caja");
					dwSolicitud.setWindowState("normal");	
					txtFecha.setValue( new Date());
					if(!txtMonto.getValue().toString().trim().equals("")){
						sMonto = txtMonto.getValue().toString().trim();
					}
					monto = Double.parseDouble(sMonto);
					m.put("montoIsertando", monto);
					m.put("monto", monto);
				}else if(selectedMet.size() > 0  && !donaciontotal ){
					List<MetodosPago> lstPagos = (ArrayList<MetodosPago>)selectedMet;
					for (MetodosPago pago : lstPagos) {
						if(pago.getMoneda().equals(sMonedaPago))
							monto += pago.getMonto();
						
						dEquivalente += (hFac.getMoneda().equals(pago.getMoneda()))?
										 pago.getMonto():
									     pago.getEquivalente();
					}
					dEquivalente = divisas.roundDouble(dEquivalente);
					
					if (!polCtrl.validarMonto(Integer.parseInt(sCajaId),sCodcomp, sMonedaPago, metodo,monto)){
						validado = false;
						lblMensajeAutorizacion.setValue("El consolidado de los montos no cumple con las politicas de caja");
						dwSolicitud.setWindowState("normal");
						txtFecha.setValue(new Date());
						m.put("monto", monto);
						if(!txtMonto.getValue().toString().trim().equals("")){
							sMonto = txtMonto.getValue().toString().trim();
						}
						monto = Double.parseDouble(sMonto);
						m.put("montoIsertando", monto);
					}
					//validar consolidado de montos
					 else {
						 if ( dEquivalente > montoaplequiv ) { //else if ( monto > dMontoAplicar) {
						 
							 BigDecimal sbr = new BigDecimal(Double.toString(dEquivalente))
				 				.subtract( new BigDecimal(Double.toString(montoaplequiv))) ;
				 	 
						 	 if( montoaplequiv > dMontoAplicar)
						 		sbr = sbr.divide(new BigDecimal(Double.toString(
						 				dEquivalente)), 2, RoundingMode.HALF_UP);
						 	
						 	 if( montoaplequiv < dMontoAplicar)
						 		sbr = sbr.multiply(tasa);
						 	
						 	 double dSobrante = divisas.roundDouble(sbr.doubleValue());
						 
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
									m.put("scr_SbrDfr", "1");
								}else{
									m.remove("scr_SbrDfr");
								}
								m.remove("scr_SobrantePago");
								m.put("scr_SobrantePago", divisas.roundDouble(dSobrante));
							}
						 }
					 }
				}
			}
			
		}catch(Exception ex){
			lblMensajeValidacion.setValue("datos ingresados no válidos");
			dwProcesa.setStyle("width:350px; height: 160px;");
			validado = false;
			ex.printStackTrace(); 
		}finally{
			lblMensajeValidacion.setValue(sMensajeError);
			dwProcesa.setStyle("width:350px;height:"+y+"px");
		}
		return validado;
	}
/***************************************************************************************************************/
/*****************CALCULAR EL MONTO RECIBIDO EN DEPENDENCIA DE LOS METODOS DE PAGO******************************/
	public double calcularElMontoRecibido(List<MetodosPago> selectedMet,Credhdr hFac, String sMonedaBase){
		//MetodosPago metMontoRec = null;
		double montoRecibido = 0;
		Divisas divisas = new Divisas();
		double cambio = 0;
		List<Credhdr> selectedFacs = new ArrayList<Credhdr>();
		double dMontoAplFacturas = 0.0;
		boolean bEfectivo = false;		
		
		try{
			//&& ========== Obtener facturas seleccionadas y calcular la suma total de sus pendientes
			selectedFacs = (List)m.get("selectedFacsCredito");
			for (Credhdr credhdr : selectedFacs) {
				dMontoAplFacturas += (hFac.getMoneda().equals(sMonedaBase))?
										credhdr.getId().getCpendiente().doubleValue():
										credhdr.getId().getDpendiente().doubleValue();
			}
			if(selectedMet.size()==0)
				bEfectivo = true;
			else{
				for (MetodosPago metMontoRec : selectedMet) {
					if(metMontoRec.getMetodo().trim().equals( MetodosPagoCtrl.EFECTIVO ))
						bEfectivo = true;
					
					if(hFac.getMoneda().equals(sMonedaBase)){//domestica
						montoRecibido+= (metMontoRec.getMoneda().equals(sMonedaBase))?
												metMontoRec.getMonto():
												metMontoRec.getEquivalente();
					}else{//foranea
						montoRecibido+= (metMontoRec.getMoneda().equals(sMonedaBase))?
												metMontoRec.getEquivalente():
												metMontoRec.getMonto();
					}
				}
			}
			//&& ======= Si ya existe sobrante, calcular de nuevo las diferencias entre monto factura y monto recibido.
			if(m.get("scr_SobrantePago") != null){
				double dDiferencia =  divisas.roundDouble( montoRecibido - dMontoAplFacturas );
				MetodosPago mpSobrante = (MetodosPago)m.get("scr_MpagoSobrante");
				
				//&& ====== Determinar el monto original del sobrante.
				if(hFac.getMoneda().compareTo(mpSobrante.getMoneda()) != 0 && dDiferencia > 0){
					dDiferencia = (mpSobrante.getEquivalente() > mpSobrante.getMonto())?
									dDiferencia / mpSobrante.getTasa().doubleValue():
									dDiferencia * mpSobrante.getTasa().doubleValue();
					dDiferencia = 	divisas.roundDouble(dDiferencia);		
				}
				if(dDiferencia > 0 && dDiferencia < dMaximoSobrante){
					m.put("scr_SobrantePago", divisas.roundDouble(dDiferencia));
				}else{
					m.remove("scr_SobrantePago");
				}
			}
			//&& ======= comparar contra monto aplicar de facturas seleccionadas
			if(m.get("dMontoAplicarCre") == null){
				if (montoRecibido <= dMontoAplFacturas){
					txtMontoAplicar.setValue(divisas.formatDouble(montoRecibido));
				}else{
					txtMontoAplicar.setValue(divisas.formatDouble(dMontoAplFacturas));
				}
			}else{
				double dMontoAplicar = Double.parseDouble(m.get("dMontoAplicarCre").toString());
				txtMontoAplicar.setValue(divisas.formatDouble(dMontoAplicar));
			}	

			//&& ====== Actualizacion de objetos en pantalla de cambios.========= &&//
			txtCambio.setStyle("font-size: 10pt");
			if(m.get("scr_SobrantePago")==null && bEfectivo){
				cambio = montoRecibido - divisas.formatStringToDouble(txtMontoAplicar.getValue().toString());
				if(cambio < 0){
					txtCambio.setStyle("color: red;font-size: 10pt");
				}else if(cambio > 0){
					txtCambio.setStyle("color: green;font-size: 10pt");
				}else{
					txtCambio.setStyle("font-size: 10pt");
				}
			}else{
				cambio = 0;
			}
			txtCambio.setValue(divisas.formatDouble(cambio));
			m.put("montoRecibibo", montoRecibido);					
			txtMontoRecibido.setValue(divisas.formatDouble(montoRecibido));
		
		}catch(Exception ex){
			ex.printStackTrace(); 
		}
		return montoRecibido;
	}
	
	
	//** **************  distribucion de monto aplicar. *********************/
	public void distribuirMontoAplicar(String sMonedaBase, 
							ArrayList<Credhdr> selectedFacs ){

		Divisas divisas = new Divisas();
		double dAcumMontoApl = 0;
		
		double dMontoPendAnt = 0;
		double dMonAplRestante = 0; 
		
		try {
			
			BigDecimal montoaplicado = new BigDecimal(txtMontoAplicar
						.getValue().toString().trim().replace(",", ""));
			BigDecimal montorecibido = new BigDecimal(txtMontoRecibido
						.getValue().toString().trim().replace(",", ""));
				
			//&& ======== Poner el monto aplicar en cero.
			for (int i = 0; i < selectedFacs.size(); i++) 
				selectedFacs.get(i).setMontoAplicar(BigDecimal.ZERO);
			
			for (int i = 0; i < selectedFacs.size(); i++) {
				Credhdr hFac = selectedFacs.get(i);
				
				//&& ======== ya se distribuyo el monto recibido
				if(montorecibido.compareTo(BigDecimal.ZERO) != 1){
					hFac.setMontoAplicar(BigDecimal.ZERO);
					continue;
				}
				//&& ======== Todavia hay pago para mas facturas.
				BigDecimal pendientefac = hFac.getMontoAplicar();
				if( pendientefac.compareTo(BigDecimal.ZERO) == 0 ){
					pendientefac = (hFac.getMoneda().compareTo(sMonedaBase) == 0)?
									hFac.getId().getCpendiente():
									hFac.getId().getDpendiente();
				}
				if(montorecibido.compareTo(pendientefac) >= 0 ){
					hFac.setMontoAplicar( pendientefac );
					montorecibido = montorecibido.subtract(pendientefac);
				}else{
					hFac.setMontoAplicar( montorecibido );
					montorecibido = BigDecimal.ZERO ;
				}
			}
			m.put("selectedFacsCredito", selectedFacs);
			gvfacturasSelecCredito.dataBind();
			
		} catch (Exception e) { 
			e.printStackTrace();
		}
	}
	
	
/***********************************************************************************************************/
/********************DISTRIBUIR EL MONTO A APLICAR EN EL GRID DE DETALLE DE FACTURA***********************/
	public void distribuirMontoAplicar1(String sMonedaBase){
		Divisas divisas = new Divisas();
		double dMontoAplicar = 0.0, dMontoRecibido = 0.0, dAcumMontoApl = 0.00;
		Credhdr hFac = null;
		double dMontoPendActual = 0.00, dMontoPendAnt = 0.00, dMonAplRestante = 0.00; 
		try{
			List selectedFacs = (List)m.get("selectedFacsCredito");
			dMontoAplicar = divisas.formatStringToDouble(txtMontoAplicar.getValue().toString());
			dMontoRecibido = divisas.formatStringToDouble(txtMontoRecibido.getValue().toString());
				for(int i = 0 ; i < selectedFacs.size(); i++){
					hFac = (Credhdr)selectedFacs.get(i);
					//obtener el monto pendiente de factura dependiendo de la moneda
					if(hFac.getMoneda().equals(sMonedaBase)){
						dMontoPendActual = hFac.getId().getCpendiente().doubleValue();
					}else{
						dMontoPendActual = hFac.getId().getDpendiente().doubleValue();
					}
					//establecer el monto aplicar de la factura 
					if(i == 0){//primera factura
						if(dMontoRecibido <= dMontoPendActual){
							if(dMontoRecibido <= dMontoAplicar){
								hFac.setMontoAplicar(new BigDecimal(dMontoRecibido));
								dAcumMontoApl = dAcumMontoApl + dMontoRecibido;
							}else{
								hFac.setMontoAplicar(new BigDecimal(dMontoAplicar));
								dAcumMontoApl = dAcumMontoApl + dMontoAplicar;
							}
						}else{
							hFac.setMontoAplicar(new BigDecimal(dMontoPendActual));
							dAcumMontoApl = dAcumMontoApl + dMontoPendActual;
						}
						//actualizar la lista
						selectedFacs.set(i, hFac);
					}
					else /*if( i != selectedFacs.size() - 1 && i != 0)*/{//no es la ultima
						dMonAplRestante = dMontoRecibido - dAcumMontoApl;
						if (dMonAplRestante <= 0){//ya no hay monto restante
							hFac.setMontoAplicar(BigDecimal.ZERO);
						}else{
							if(dMonAplRestante <= dMontoPendActual){
								hFac.setMontoAplicar(new BigDecimal(dMonAplRestante));
								dAcumMontoApl = dAcumMontoApl + dMonAplRestante;
							}else{
								hFac.setMontoAplicar(new BigDecimal(dMontoPendActual));
								dAcumMontoApl = dAcumMontoApl + dMontoPendActual;
							}
						}
						//actualizar la lista
						selectedFacs.set(i, hFac);
					}
				}
			m.put("selectedFacsCredito", selectedFacs);
			gvfacturasSelecCredito.dataBind();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
/***********************************************************************************************************/
/**************DETERMINAR COMO DAR EL CAMBIO****************************************************************/
	public void determinarCambio(List lstPagos, Credhdr hFac, double montoRecibido, String sMonedaBase){
		boolean bCumple = true, bCambioCOR = false;
		MetodosPago mpago = null;
		Divisas divisas = new Divisas();
		SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
		double cambio = 0.0, montoCOR = 0,montoUSD = 0;
		BigDecimal tasa = BigDecimal.ZERO;
		Tcambio[] tcJDE = null;
		try{
			tasa = obtenerTasaOficial();
			//verificar si se cumplen condiciones para cambio mixto
			for (int i = 0; i < lstPagos.size(); i++){
				mpago = (MetodosPago)lstPagos.get(i);
				if (mpago.getMoneda().equals(sMonedaBase) && mpago.getMetodo().equals( MetodosPagoCtrl.EFECTIVO ) ) {
					bCumple = false;
				}
				if (!hFac.getMoneda().equals(sMonedaBase) && mpago.getMoneda().equals(sMonedaBase) && mpago.getMetodo().equals( MetodosPagoCtrl.EFECTIVO ) ){
					montoCOR = montoCOR + mpago.getMonto();
					bCambioCOR = true;
				}
				if (!hFac.getMoneda().equals(sMonedaBase) && !mpago.getMoneda().equals(sMonedaBase) && mpago.getMetodo().equals( MetodosPagoCtrl.EFECTIVO )){
					montoUSD = montoUSD + mpago.getMonto();
				}
			}
			double dCambio = divisas.formatStringToDouble(txtCambio.getValue().toString());
			if (bCumple && dCambio > 0){//realiza el calculo del cambio
				if(hFac.getMoneda().equals(sMonedaBase)){
					txtCambioForaneo.setValue(divisas.roundDouble(dCambio/tasa.doubleValue()));
				}else{
					txtCambioForaneo.setValue(txtCambio.getValue().toString());
				}
				txtCambioForaneo.setStyle("visibility: visible; width: 40px;display:inline");
				//lblCambio.setValue("Cambio " +mpago.getMoneda()  + ":");
				lblCambio.setValue("Cambio USD:");
				txtCambio.setValue("");
				
				lnkCambio.setStyle("visibility: visible; width: 16px");
				lnkCambio.setIconUrl("/theme/icons/RefreshWhite.gif");
				lnkCambio.setHoverIconUrl("/theme/icons/RefreshWhiteOver.gif");
				lblPendienteDomestico.setValue("");
				txtPendienteDomestico.setValue("");
				
				lblCambioDomestico.setValue("Cambio " + sMonedaBase + ":");
				txtCambioDomestico.setValue("0.00");
				
				m.put("bdTasa", BigDecimal.ZERO);
			}else if(dCambio < 0){
				cambio = montoRecibido - divisas.formatStringToDouble(txtMontoAplicar.getValue().toString());
				lblCambio.setValue("Cambio " + hFac.getMoneda() + ":");
				txtCambio.setValue(divisas.formatDouble(cambio));
				txtCambioForaneo.setStyle("visibility: hidden; width: 0px");
				lblCambioDomestico.setValue("");
				txtCambioDomestico.setValue("");	
				lnkCambio.setStyle("visibility: hidden;width: 0px");
				lnkCambio.setIconUrl("");
				lnkCambio.setHoverIconUrl("");				
				// obtener tasa del cambio
				tasa = obtenerTasaParalela(sMonedaBase /*"COR"*/);
				if(!hFac.getMoneda().equals(sMonedaBase)){
					lblPendienteDomestico.setValue("Cambio " + sMonedaBase + ":");
					lblPendienteDomestico.setStyle("visibility: visible");
					txtPendienteDomestico.setValue(divisas.formatDouble(divisas.roundDouble(cambio*tasa.doubleValue())));
					m.put("bdTasa", tasa);
					txtPendienteDomestico.setStyle("visibility: visible; color: red");
				}
				if(cambio < 0){
					txtCambio.setStyle("color: red;font-size: 10pt");
				}else if(cambio > 0){
					txtCambio.setStyle("color: green;font-size: 10pt");
				}else{
					txtCambio.setStyle("font-size: 10pt");
				}
			}
			else if (!hFac.getMoneda().equals(sMonedaBase) && bCambioCOR){
				

				//obtener tasa del cambio
				Tpararela[] tcPar = (Tpararela[])m.get("tpcambio");
				//buscar tasa de cambio paralela para moneda
				for(int t = 0; t < tcPar.length;t++){
					if(tcPar[t].getId().getCmono().equals(sMonedaBase) || tcPar[t].getId().getCmond().equals(sMonedaBase)){
						tasa = tcPar[t].getId().getTcambiom();
						break;
					}
				}
				m.put("bdTasa", tasa); 
							
				BigDecimal bdCambio = new BigDecimal(txtMontoRecibido
						.getValue().toString().replace(",", ""))
						.subtract(new BigDecimal(txtMontoAplicar.getValue()
								.toString().replace(",", "")));
				bdCambio = bdCambio.multiply(tasa);
				bdCambio = bdCambio.setScale(4, BigDecimal.ROUND_HALF_UP);
				switch (bdCambio.compareTo(BigDecimal.ZERO)) {
				case -1:
					txtCambio.setStyle("color: red;font-size: 10pt");
					break;
				case 1:
					txtCambio.setStyle("color: green;font-size: 10pt");
					break;
				case 0:
					txtCambio.setStyle("font-size: 10pt");
					break;
				}
				
			
				
				lblCambio.setValue("Cambio " + sMonedaBase + ":");
				txtCambio.setValue(String.format("%1$,.2f", bdCambio));
				
				txtCambioForaneo.setStyle("visibility: hidden; width: 0px");
				lnkCambio.setStyle("visibility: hidden; width: 0px");
				lnkCambio.setIconUrl("");
				lnkCambio.setHoverIconUrl("");
				lblCambioDomestico.setValue("");
				txtCambioDomestico.setValue("");	
				lblPendienteDomestico.setValue("");
				txtPendienteDomestico.setValue("");
			}
			else if (!hFac.getMoneda().equals(sMonedaBase)){//restablece el cambio al tipo de factura
				//obtener tasa del cambio
				tcJDE = (Tcambio[])m.get("tcambio");
				//buscar tasa de cambio JDE para moneda
				for(int l = 0; l < tcJDE.length;l++){
					if(tcJDE[l].getId().getCxcrcd().equals(sMonedaBase/*"COR"*/)){
						tasa = tcJDE[l].getId().getCxcrrd();
						break;
					}
				}
				m.put("bdTasa", tasa);
				cambio = divisas.roundDouble((montoRecibido - divisas.formatStringToDouble(txtMontoAplicar.getValue().toString()))*tasa.doubleValue());
				if(cambio < 0){
					txtCambio.setStyle("color: red;font-size: 10pt");
				}else if(cambio > 0){
					txtCambio.setStyle("color: green;font-size: 10pt");
				}else{
					txtCambio.setStyle("font-size: 10pt");
				}
				lblCambio.setValue("Cambio " + sMonedaBase + ":");
				txtCambio.setValue(divisas.formatDouble(cambio));
				txtCambioForaneo.setStyle("visibility: hidden; width: 0px");
				lnkCambio.setStyle("visibility: hidden; width: 0px");
				lnkCambio.setIconUrl("");
				lnkCambio.setHoverIconUrl("");
				lblCambioDomestico.setValue("");
				txtCambioDomestico.setValue("");	
				lblPendienteDomestico.setValue("");
				txtPendienteDomestico.setValue("");
			}
			else{
				m.put("bdTasa", BigDecimal.ZERO);
				cambio = (montoRecibido - divisas.formatStringToDouble(txtMontoAplicar.getValue().toString()));
				if(cambio < 0){
					txtCambio.setStyle("color: red;font-size: 10pt");
				}else if(cambio > 0){
					txtCambio.setStyle("color: green;font-size: 10pt");
				}else{
					txtCambio.setStyle("font-size: 10pt");
				}
				lblCambio.setValue("Cambio " + sMonedaBase + ":");
				txtCambio.setValue(divisas.formatDouble(cambio));
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
			srm.addSmartRefreshId(lnkCambio.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(txtCambio.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(txtCambioForaneo.getClientId(FacesContext.getCurrentInstance()));
			
			srm.addSmartRefreshId(lblPendienteDomestico.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(txtPendienteDomestico.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(txtCambioDomestico.getClientId(FacesContext.getCurrentInstance()));
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
/*********************************************************************************************************************/
/********VALUE CHANGE LISTENER DE CAMBIO ****************************************************************************/
	public void cambioChanged(ValueChangeEvent ev){
		String sMonedaBase = "";
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		try{
			List lstFacturasSelected = (List) m.get("selectedFacsCredito");
			Credhdr hFac = (Credhdr)lstFacturasSelected.get(0);
			//obtener companias x caja
			sMonedaBase = cCtrl.sacarMonedaBase((F55ca014[])m.get("cont_f55ca014"), hFac.getId().getCodcomp());
			procesarCambio(hFac,sMonedaBase);
		}catch(Exception ex){
			
		}
	}
/**********ACTION LISTENER DE CAMBIO**********************************************************************************/
	public void aplicarCambio(ActionEvent ev){
		String sMonedaBase = "";
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		try{
			List lstFacturasSelected = (List) m.get("selectedFacsCredito");
			Credhdr hFac = (Credhdr)lstFacturasSelected.get(0);
			//obtener companias x caja
			sMonedaBase = cCtrl.sacarMonedaBase((F55ca014[])m.get("cont_f55ca014"), hFac.getId().getCodcomp());
			procesarCambio(hFac,sMonedaBase);
		}catch(Exception ex){
			
		}
	}
/*****************************PROCESAR CAMBIO**********************************************************************/
	public void procesarCambio(Credhdr hFac,String sMonedaBase){
		SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
		boolean valido = false;
		double dCambioDom = 0.0;
		BigDecimal tasa = BigDecimal.ZERO;
		Divisas divisas = new Divisas();
		Tcambio[] tcJDE = null;
		try{
			valido = validarCambio();
			if(valido){
			
				txtCambioForaneo.setStyleClass("frmInput2");
			
				double dMontoAplicar = divisas.formatStringToDouble(txtMontoAplicar.getValue().toString());	
				double montoRecibido = divisas.formatStringToDouble(txtMontoRecibido.getValue().toString());
				double cambio = montoRecibido - dMontoAplicar;
				double dCambioForaneo = divisas.formatStringToDouble(txtCambioForaneo.getValue().toString());
				
				
				if(dCambioForaneo > cambio){
					txtCambioForaneo.setValue(divisas.formatDouble(cambio));
					txtCambioDomestico.setValue("0.00");
				}else{
					
					tcJDE = (Tcambio[]) m.get("tcambio");
					for (int l = 0; l < tcJDE.length; l++) {
						if (tcJDE[l].getId().getCxcrcd().equals(sMonedaBase)) {
							tasa = tcJDE[l].getId().getCxcrrd();
							break;
						}
					}
					m.put("bdTasa", tasa);
					if(hFac.getMoneda().equals(sMonedaBase)){
						dCambioDom = (cambio - (dCambioForaneo * tasa.doubleValue()));
					}else{
						dCambioDom = ((cambio - dCambioForaneo) * tasa.doubleValue());
					}		
					txtCambioDomestico.setValue(divisas.formatDouble(divisas.roundDouble(dCambioDom)));
				}
			}
		srm.addSmartRefreshId(txtCambioForaneo.getClientId(FacesContext.getCurrentInstance()));
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
/****************************************************************************************************/
	/****************ELIMINA METODOS DE PAGO DEL GRID****************************************************************/
	public void borrarPago(ActionEvent e){
		Credhdr hFac = null;
		ArrayList<Credhdr> lstFacturasSelected = null;
		
		try{
			MetodosPago mPago = (MetodosPago) m.get("metodopagoborrar");
			lstFacturasSelected = (ArrayList<Credhdr>) m.get("selectedFacsCredito");
			hFac = lstFacturasSelected.get(0);

			// && ========== Borrar datos de la donacion.
			if (mPago.isIncluyedonacion()) {
				CodeUtil.removeFromSessionMap(new String[] 
					{ "cr_MontoTotalEnDonacion","cr_lstDonacionesRecibidas" });
			}
			
			String sMonedaBase = new CompaniaCtrl().sacarMonedaBase(
						(F55ca014[])m.get("cont_f55ca014"), 
						hFac.getId().getCodcomp());
			
			//&& ========== remover solicitudes, en caso de existir.
			ArrayList<Solicitud> lstSolicitud = m.containsKey("lstSolicitud")? 
								(ArrayList<Solicitud>)m.get("lstSolicitud"):
								 new ArrayList<Solicitud>();
			CtrlSolicitud.removerSolicitud(lstSolicitud, mPago);				
								
			//&& ========== remover el pago de la lista de los registrados.
			selectedMet = (ArrayList<MetodosPago>)m.get("lstPagos");	
			MetodosPagoCtrl.removerPago(selectedMet, mPago);
			
			//&& ========== Sumar el total de las facturas.
			double dMontoAplicar = 0;
			double dTotalMpagos=0;
			double dSobrante=0;
			
			for (Credhdr hFacSel : lstFacturasSelected)
				dMontoAplicar += hFacSel.getMontoPendiente().doubleValue();
			
			for (MetodosPago mPagoValidar : selectedMet) {
				dTotalMpagos += mPagoValidar.getEquivalente();
				if(mPago.getEquivalente() > dMontoAplicar || dTotalMpagos > dMontoAplicar){
					dSobrante = dSobrante = new Divisas().roundDouble(dTotalMpagos-dMontoAplicar);
					if(dSobrante> dMaximoSobrante){
						lblMensajeValidacion.setValue("No puede borrar el pago, el monto restante excede el monto aplicar");
						dwProcesa.setStyle("width:320px;height: 160px");
						dwProcesa.setWindowState("normal");
						return;
					}else{
						//&& ===== verificar el tipo de excedente, si sobrante o si diferencial cambiario.
						if(hFac.getMoneda().equals(mPago.getMoneda()) && hFac.getMoneda().equals(sMonedaBase)){
							m.put("scr_SbrDfr", "1");
						}else{
							m.remove("scr_SbrDfr");
						}
						m.put("scr_MpagoSobrante",mPagoValidar);
						m.put("scr_SobrantePago",dSobrante);
					}
				}
			}
			
			//&& ========== calcular el monto recibido en dependencia de moneda de factura y moneda de pago
			double montoRecibido = calcularElMontoRecibido(selectedMet,hFac,sMonedaBase);
			boolean bEfectivo = false;
			List<MetodosPago>lstMetodos = (ArrayList<MetodosPago>)selectedMet;
			if(lstMetodos.size() == 0)
				bEfectivo = true;
			else{
				for (MetodosPago pago : lstMetodos) {
					if(pago.getMetodo().trim().toLowerCase().equals( MetodosPagoCtrl.EFECTIVO )){
						bEfectivo = true;
						break;
					}
				}
			}
			SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
			lblMarcaSobrDifer.setStyle("display: none");
			chkSobranteDifrl.setStyle("display: none");
			
			if(bEfectivo){
			    m.remove("scr_SobrantePago");
				m.remove("scr_MpagoSobrante");
				m.remove("scr_SbrDfr");
				determinarCambio(selectedMet, hFac, montoRecibido, sMonedaBase);
			}else{
				if(m.get("scr_SobrantePago") != null){
					if(hFac.getId().getCodcomp().trim().equals("E03") && m.get("scr_SbrDfr")!=null){
						lblMarcaSobrDifer.setStyle("display: inline");
						chkSobranteDifrl.setStyle("display: inline");
						chkSobranteDifrl.setChecked(false);
					}
					lblCambio.setValue("Cambio: "+hFac.getMoneda());
					txtCambio.setValue(String.format("%1$,.2f", BigDecimal.ZERO));
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
			srm.addSmartRefreshId(lblMarcaSobrDifer.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(chkSobranteDifrl.getClientId(FacesContext.getCurrentInstance()));
			
			m.put("montoRecibibo", montoRecibido);
			
			//&& ========== distribuir el monto recibido a los montos a aplicar en factura  && calcular Totales
			distribuirMontoAplicar(sMonedaBase, lstFacturasSelected);
			calcularTotales(lstFacturasSelected, sMonedaBase);
			
			metodosGrid.dataBind();
			
		}catch(Exception ex){ 
			ex.printStackTrace();
		}finally{
			dwBorrarPago.setWindowState("hidden");
			m.remove("metodopagoborrar");
		}
	}
	
	
	public String validarCambio(String monedabase, String monedafactura){
		String msg = "" ;
		String sCambio = "";
		Divisas divisas = new Divisas();
		BigDecimal bdDiferenciaPermitida = new BigDecimal("0.25") ;
 
		
		try {
			
			BigDecimal bdMontoAplicado = new BigDecimal( txtMontoAplicar.getValue().toString().trim().replace(",", "") ) ;
			BigDecimal bdMontoRecibido = new BigDecimal( txtMontoRecibido.getValue().toString().trim().replace(",", "") ) ;
			BigDecimal bdExcedentePago = bdMontoRecibido.subtract(bdMontoAplicado);
			
			boolean validarMultiCambio = txtCambio.getValue().toString().trim().isEmpty() ;
			
			if(validarMultiCambio){
				
				String montoCambioExtranjero = txtCambioForaneo.getValue().toString().trim().replace(",", "") ;
				String montoCambioMndLocal  =  txtCambioDomestico.getValue().toString().trim().replace(",", "") ;
				
				if( !montoCambioExtranjero.matches(PropertiesSystem.REGEXP_AMOUNT) ){
					txtCambioForaneo.setStyleClass("frmInput2Error");
					return "El valor ingresado para cambio extranjero no es válido";
				}
				
				BigDecimal bdMtoCmbioExtranjero = new BigDecimal(montoCambioExtranjero); 
				BigDecimal bdMtoCmbioMndLocal   = new BigDecimal(montoCambioMndLocal); 
				BigDecimal bdTasaOficial = obtenerTasaOficial();
				
				//&& ============ Validar que si hay cambio en dos monedas, los montos correspondan entre si.
				if(bdMtoCmbioExtranjero.compareTo(BigDecimal.ZERO) == 1  && bdMtoCmbioMndLocal.compareTo(BigDecimal.ZERO) == 1 ){
					
					BigDecimal bdMtoExtranjeroEnLocal = bdMtoCmbioExtranjero.multiply(bdTasaOficial);
					
					if( bdMtoExtranjeroEnLocal.subtract(bdMtoCmbioMndLocal).abs().compareTo( bdDiferenciaPermitida ) != 1 ){
						txtCambioForaneo.setStyleClass("frmInput2Error");
						return "Los montos de cambio ingresados no corresponden entre si ";
					}
					
					//&& ================== Convertir el monto extranjero
					
					BigDecimal bdTotalAmbosCambios = bdMtoExtranjeroEnLocal.add(bdMtoCmbioMndLocal) ;
					
					if( monedafactura.compareTo(monedabase) == 0 ){
						
						if( bdTotalAmbosCambios.subtract( bdExcedentePago ).abs().compareTo( bdDiferenciaPermitida ) == 1 ) {
							return "Los montos de cambio ingresados no corresponden entre si ";
						} 
						
					}else{
						
						if( bdTotalAmbosCambios.divide( bdTasaOficial, 2, RoundingMode.HALF_UP ).subtract( bdExcedentePago ).abs().compareTo( bdDiferenciaPermitida ) == 1  ) {
							return "Los montos de cambio ingresados no corresponden entre si ";
						} 
						
					}
					
				}
				
				//&& ============ validar que si solo hay monto en moneda extranjera corresponda al total del cambio
				if( bdMtoCmbioMndLocal.compareTo(BigDecimal.ZERO) == 0 ){
					
					if(monedafactura.compareTo(monedabase) == 0 ){
						
						if( bdMtoCmbioExtranjero.multiply(bdTasaOficial).subtract(bdExcedentePago).abs().compareTo( bdDiferenciaPermitida ) == 1 ){
							txtCambioForaneo.setStyleClass("frmInput2Error");
							return "El monto de cambio extranjero no corresponde con la diferencia de aplicado y recibido";
						}
						
					}else{
						
						if( bdMtoCmbioExtranjero.subtract( bdExcedentePago ).abs().compareTo( bdDiferenciaPermitida ) == 1 ){
							txtCambioForaneo.setStyleClass("frmInput2Error");
							return "El monto de cambio extranjero no corresponde con la diferencia de aplicado y recibido";
						}
					}
				}
				
				//&& ============ validar que si solo hay monto en local sea el total de la diferencia del recibido contra aplicado
				if( bdMtoCmbioExtranjero.compareTo(BigDecimal.ZERO) == 0 ){
					
					
					if( monedafactura.compareTo(monedabase) == 0 ){
						 
						if( bdMtoCmbioMndLocal.subtract( bdExcedentePago ).abs().compareTo( bdDiferenciaPermitida ) == 1 ){
							txtCambioForaneo.setStyleClass("frmInput2Error");
							return "El monto de cambio local no corresponde con la diferencia de aplicado y recibido";
						}
						
					}else{
						
						if( bdExcedentePago.multiply( bdTasaOficial ).subtract(bdMtoCmbioMndLocal).abs().compareTo( bdDiferenciaPermitida ) == 1 ){
							txtCambioForaneo.setStyleClass("frmInput2Error");
							return "El monto de cambio local no corresponde con la diferencia de aplicado y recibido";
						}
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace(); 
			msg = "Error al validar monto de cambios ";
		} 
		
		return msg;
		
	}
	
/**************************************************************************************************/
/****************VALIDACIONES DE CAMBIO*************************************************************/
	public boolean validarCambio(){
		boolean validado = true;
		String sCambio = "";
		Divisas divisas = new Divisas();
		
		try{
	
			//expresion regular solo numeros
			Matcher matNumero = null;
			if(txtMonto.getValue() != null){
				sCambio = txtCambioForaneo.getValue().toString().trim();
				Pattern pNumero = Pattern.compile("^[0-9]+\\.?[0-9]*$");
				matNumero = pNumero.matcher(sCambio);
			}
			if(sCambio.equals("")){
				txtCambioForaneo.setStyleClass("frmInput2Error");
				validado = false;
			}
			else if(!matNumero.matches()){
				txtCambioForaneo.setStyleClass("frmInput2Error");
				validado = false;
			}
			else if(divisas.formatStringToDouble(sCambio)<0){
				txtCambioForaneo.setStyleClass("frmInput2Error");
				validado = false;
			}
			
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return validado;
	}
/**************************************************************************************************************/	
/*************************************QUITAR FACTURA DE CREDITO DEL DETALLE**********************************************/	
	public void	quitarFacturaCredito(ActionEvent e){
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		Divisas divisas = new Divisas();
		ArrayList<Credhdr> lstDcontadoFacs = new ArrayList();
		List lstNewDFacs = new ArrayList();
		Credhdr hFac = null,fSel = null;
		String sMonedaBase = "";
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		try {
			lstDcontadoFacs = (ArrayList<Credhdr>)m.get("selectedFacsCredito");
			double montoAplicar = 0;
			
			if (lstDcontadoFacs.size()>1){

				RowItem ri = (RowItem) e.getComponent().getParent().getParent();
				fSel = (Credhdr)gvfacturasSelecCredito.getDataRow(ri);
				if(!fSel.getTipofactura().equals("IF") && !fSel.getTipofactura().equals("MF")){
					for(int i = 0;i < lstDcontadoFacs.size();i++){
						hFac = (Credhdr)lstDcontadoFacs.get(i);
						if (hFac.getNofactura() == fSel.getNofactura() && (hFac.getPartida().trim()).equals(fSel.getPartida().trim())){
							//no las incluye en la lista
							//lstDcontadoFacs.remove(i);
						}else{
							lstNewDFacs.add(hFac);
							montoAplicar = montoAplicar + hFac.getMontoAplicar().doubleValue();
						}
					}
					txtMontoAplicar.setValue(divisas.formatDouble(montoAplicar));
					m.put("dMontoAplicarCre",montoAplicar);
					m.put("selectedFacsCredito", lstNewDFacs);
					List lstSelectedMet = (List)m.get("lstPagos");
					intSelectedDet.setValue(lstNewDFacs.size());
					//obtener companias x caja
					sMonedaBase = cCtrl.sacarMonedaBase((F55ca014[])m.get("cont_f55ca014"), hFac.getId().getCodcomp());
					//--------------------------------------------------
					//calcular el monto recibido en dependencia de moneda de factura y moneda de pago
					double montoRecibido = calcularElMontoRecibido(lstSelectedMet, hFac, sMonedaBase);
	
					//determinar como dar el cambio
					determinarCambio(lstSelectedMet, hFac, montoRecibido, sMonedaBase);
					
					//distribuir el monto recibido a los montos a aplicar en factura
					if(m.get("dMontoAplicarCre") == null){
						distribuirMontoAplicar(sMonedaBase, lstDcontadoFacs);
					}
					m.remove("dMontoAplicarCre");
					gvfacturasSelecCredito.dataBind();
					SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
					srm.addSmartRefreshId(gvfacturasSelecCredito.getClientId(FacesContext.getCurrentInstance()));
					//calcular Totales
					calcularTotales(lstNewDFacs, sMonedaBase);
				}else{
					lblMensajeDetalleCredito.setValue("Los intereses no pueden quitarse del recibo, Deben ser cancelados o condonados!!!");
					dgwMensajeDetalleCredito.setWindowState("normal");
				}
			}else{
				lblMensajeDetalleCredito.setValue("El recibo debe aplicarse al menos a una factura");
				dgwMensajeDetalleCredito.setWindowState("normal");
			}		
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public void cerrarMensajeDetallefacturaCredito(ActionEvent e){
		Credhdr newFac = null,hFacCurrent = null;
		try{
			//restablecer el valor de la factura
 
			dgwMensajeDetalleCredito.setWindowState("hidden");
		}catch(Exception ex){
			
		}
	}
/****************************************************************************************************************/
/************************PROCESAR SOLICITUD**********************************************************************/
	public void procesarSolicitud(ActionEvent e) {
		boolean validado = false;
		Divisas d = new Divisas();
		BigDecimal tasa = BigDecimal.ONE;
		Double  total, monto, equiv, montototal=0.0;
		String sTotal,sMonto, sEquiv,sIdMpago = "";
		int cont = 1;		
		double montoRecibido = 0;
		boolean valido = false, bTarjeta = false;	
		Tpararela[] tpcambio = null;
		CtrlSolicitud solCtrl = new CtrlSolicitud();
		Tpararela[] tcPar = null;
		Tcambio[] tcJDE = null;
		String sMonedaBase = "";
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		List<String> lstDatosTrack  = null;
		
		ArrayList<Credhdr> lstFacturasSelected = (ArrayList<Credhdr>) m.get("selectedFacsCredito");
		Credhdr hFac = lstFacturasSelected.get(0);
		
		try{

			validado = validarSolicitud();		
			
			if(validado){
				String sCodigoMetPago = getCmbMetodosPago().getValue().toString();
				String metodo = getCmbMetodosPago().getValue().toString();		
				String moneda = cmbMoneda.getValue().toString();		
				String ref1 = txtReferencia1.getValue().toString().trim();
				String ref2 = txtReferencia2.getValue().toString().trim();
				String ref3 = txtReferencia3.getValue().toString().trim();
				String ref4 = "";
				String ref5 = "";
				String sTrack = "";
				String sTerminal = "";	
				
				Vf55ca01 caja  = ((List<Vf55ca01>) m.get("lstCajas")).get(0);
				
				boolean usaSocketPos  = caja.getId().getCasktpos() == 'Y';
				boolean ingresoManual = chkIngresoManual.isChecked();
				boolean voucherManual = chkVoucherManual.isChecked();
				String sVoucherManual = (chkVoucherManual.isChecked())?"1":"0";
				
				sIdMpago =   metodo;
				montototal = Double.parseDouble(m.get("montoIsertando").toString().trim());
				monto = Double.parseDouble( txtMonto.getValue().toString().trim() );
				sMonto = d.formatDouble(monto);
				equiv = monto;
				
				List lstMetPago = (List)m.get("metpago");		
				//cambiar ubicacion de referencias
				if (metodo.compareTo(MetodosPagoCtrl.CHEQUE) == 0){//cheque
					ref4 = ref3;
					ref3 = ref2;
					ref2 = ddlBanco.getValue().toString().split("@")[1];
				}else if(metodo.compareTo(MetodosPagoCtrl.TARJETA ) == 0){//TC
					ref3 = ref2;
					ref2 = ref1;
					ref1 = ddlAfiliado.getValue().toString();	

					if( usaSocketPos && !voucherManual){
						ref1 = ((ddlAfiliado.getValue().toString()).split(","))[0];
						sTerminal = ((ddlAfiliado.getValue().toString()).split(","))[1];						
						if( ingresoManual ){//manual
							String snoT = txtNoTarjeta.getValue().toString().trim();
							ref3 = (snoT).substring(snoT.length()-4, snoT.length());//4 ult. digitos de tarjeta
							ref4 = txtNoTarjeta.getValue().toString();	
							ref5 = txtFechaVenceT.getValue().toString();	
						}else{
							sTrack = track.getValue().toString();
							sTrack = d.rehacerCadenaTrack(sTrack);
							lstDatosTrack = d.obtenerDatosTrack(sTrack);
							ref3 = lstDatosTrack.get(1).substring(lstDatosTrack.get(1).length()-4, lstDatosTrack.get(1).length());//4 ult. digitos de tarjeta
							
						}
					}
					else if( usaSocketPos && voucherManual){
						ref1 = ((ddlAfiliado.getValue().toString()).split(","))[0];
						sTerminal = ((ddlAfiliado.getValue().toString()).split(","))[1];	
					}
					bTarjeta = true;	
				}else if(metodo.compareTo(MetodosPagoCtrl.TRANSFERENCIA) == 0){//Transf. Elect.
					ref3 = ref2;
					ref2 = ddlBanco.getValue().toString().split("@")[1];
					ref1 = ref1;		
				}else if(metodo.compareTo(MetodosPagoCtrl.DEPOSITO) == 0){//Deposito en banco
					ref1 = ddlBanco.getValue().toString().split("@")[1];
				}
				//
				int cant = 1;
				boolean flgpagos = true;					
				Metpago[] metpago = null;
				
				//poner descripcion a metodo
				for (int m = 0 ; m < lstMetPago.size();m++){
					metpago = (Metpago[])lstMetPago.get(m);
					if(metodo.trim().equals(metpago[0].getId().getCodigo().trim())){
						metodo = metpago[0].getId().getMpago().trim();
						break;
					}
				}
				
				//obtener companias x caja
				sMonedaBase = cCtrl.sacarMonedaBase((F55ca014[])m.get("cont_f55ca014"), hFac.getId().getCodcomp());
				//calcular equivalente en dependencia de moneda de factura y moneda de pago
				if(hFac.getMoneda().equals(sMonedaBase)){//domestica
					if(moneda.equals(sMonedaBase)){
						tasa = BigDecimal.ONE;
						equiv = monto;
					}else{//foranea
						tcJDE = (Tcambio[])m.get("tcambio");
						//buscar tasa de cambio JDE para moneda
						for (int l = 0; l < tcJDE.length; l++) {
							if (tcJDE[l].getId().getCxcrdc().equals(moneda)) {
								tasa = tcJDE[l].getId().getCxcrrd();
								break;
							}
						}
						equiv = d.roundDouble(monto*tasa.doubleValue());
					}
				}else{//foranea
					if(moneda.equals(sMonedaBase)){
						tcPar = (Tpararela[])m.get("tpcambio");
						//buscar tasa de cambio paralela para moneda
						for(int t = 0; t < tcPar.length;t++){
							if(tcPar[t].getId().getCmono().equals(moneda) || tcPar[t].getId().getCmond().equals(moneda)){
								tasa = tcPar[t].getId().getTcambiom();
								break;
							}
						}
						equiv = d.roundDouble(monto/tasa.doubleValue());
					}else{
						tasa = BigDecimal.ONE;
						equiv = monto;
					}
				}
				sEquiv = d.formatDouble(equiv);	
				sMonto = d.formatDouble(monto);
				
				//&& ============ Datos de tipo de marca tarjeta
				String codigomarcatarjeta = "";
				String marcatarjeta = "";
				
				if( bTarjeta ){
					codigomarcatarjeta = ddlTipoMarcasTarjetas.getValue().toString().split("@")[0];
					marcatarjeta = ddlTipoMarcasTarjetas.getValue().toString().split("@")[1];
				}
				
				if(selectedMet != null) {
					
					MetodosPago[] mpagos = selectedMet
							.toArray(new MetodosPago[selectedMet.size()]);
					
					//valida que se agreguen los metodos iguales a un solo registro
					for(int i=0; i < mpagos.length; i++) {							
						if(mpagos[i].getMetodo().equals(sCodigoMetPago) && mpagos[i].getMoneda().equals(moneda) && 
							mpagos[i].getReferencia().equals(ref1) && mpagos[i].getReferencia2().equals(ref2) && 
						    mpagos[i].getReferencia3().equals(ref3) && mpagos[i].getReferencia4().equals(ref4)) {
									
							monto = monto + (mpagos[i].getMonto());																			
							equiv = equiv + (mpagos[i].getEquivalente());									
									
							sMonto = d.formatDouble(monto);
							mpagos[i].setMonto(monto);
									
							sEquiv = d.formatDouble(equiv);
							mpagos[i].setEquivalente(equiv);
							mpagos[i].setReferencia5("");
							
							if (bTarjeta && usaSocketPos && !voucherManual) {
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
							selectedMet.add(i,mpagos[i]);	
							flgpagos = false;
						}								
					}				
				} else {
					MetodosPago metpagos = new MetodosPago(metodo,
							sIdMpago, moneda, monto, tasa, equiv, ref1,
							ref2, ref3, ref4, sVoucherManual, 0);
					
					metpagos.setReferencia5("");
					if (bTarjeta && usaSocketPos && !voucherManual) {
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
				
				if (flgpagos){
					MetodosPago metpagos = new MetodosPago(metodo,
							sIdMpago, moneda, monto, tasa, equiv, ref1,
							ref2, ref3, ref4, sVoucherManual, 0);
					
					metpagos.setReferencia5("");
					if (bTarjeta && usaSocketPos && !voucherManual) {
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
					
					selectedMet.add(metpagos);
				}
				
				// calcular el monto recibido en dependencia de moneda de factura y moneda de pago
				calcularElMontoRecibido(selectedMet,hFac,sMonedaBase);
				double dMontoAplicar = d.formatStringToDouble(txtMontoAplicar.getValue().toString());	
				montoRecibido = d.formatStringToDouble(txtMontoRecibido.getValue().toString());
				double cambio = montoRecibido - dMontoAplicar;
				
				txtCambio.setValue(d.formatDouble(cambio));
				//determinar el cambio 
				
				//	determinar como dar el cambio
				determinarCambio(selectedMet,hFac,montoRecibido,sMonedaBase);
				m.put("montoRecibibo", montoRecibido);
				
				//distribuir el monto recibido a los montos a aplicar en factura
				distribuirMontoAplicar(sMonedaBase, lstFacturasSelected);
				
				//calcular Totales
				calcularTotales(lstFacturasSelected,sMonedaBase);
				
				m.put("lstPagos", selectedMet);
				m.put("mpagos", selectedMet);
				
				List lstSolicitud = m.containsKey("lstSolicitud")?
								(ArrayList<Solicitud>)m.get("lstSolicitud"):
								new ArrayList<Solicitud>();

				//info del autorizador 0 = correo; 1 en adelante = nombre
				String[] sAutorizador = cmbAutoriza.getValue().toString().split(" ");
				String sNomAut = "";
				
				for (String aut : sAutorizador) 
					sNomAut += aut+" ";
				
				Solicitud solicitud = new Solicitud();
	        	SolicitudId solicitId = new SolicitudId(); 
	        	
	        	solicitId.setReferencia(txtReferencia.getValue().toString());
	        	solicitud.setAutoriza(Integer.parseInt(sAutorizador[1].trim()));
	        	solicitud.setObs(txtObs.getValue().toString());
	        	solicitud.setFecha((Date)txtFecha.getValue());
	        	solicitud.setMetododesc(metodo);
	        	solicitud.setMoneda(moneda);
	        	solicitud.setMpago(sIdMpago);
	        	solicitud.setMonto(new BigDecimal(String.valueOf(montototal)));
	        	
	        	solicitud.setId(solicitId);
				lstSolicitud.add(solicitud);
				m.put("lstSolicitud", lstSolicitud);
				
				//limpiar
				txtMonto.setValue("");
				txtReferencia1.setValue("");
				txtReferencia2.setValue("");
				txtReferencia3.setValue("");
				track.setValue("");
				txtCambioForaneo.setStyleClass("frmInput2");
				txtNoTarjeta.setValue("");	
				txtFechaVenceT.setValue("");	
				
				//enviar correo a cajero o suplente, responsable de caja y autorizador
				EmpleadoCtrl empCtrl = new EmpleadoCtrl();
				Vf0101 vf0101 = null;
				
				int iCodCajero = Integer.parseInt(m.get("iCodCajero").toString());
				//List lstAutorizadores = (List)m.get("lstAutorizadores");
				List<String> lstCc = new ArrayList<String>();
				
				//---- Agregas copias
				vf0101 = empCtrl.buscarEmpleadoxCodigo2(iCodCajero); 
				
				if(d.validarCuentaCorreo(vf0101.getId().getWwrem1().trim()))
					lstCc.add(vf0101.getId().getWwrem1().trim());
				
				List<SelectItem> correosAutorizadores = (ArrayList<SelectItem>)CodeUtil.getFromSessionMap("lstAutoriza") ;
				for (SelectItem si : correosAutorizadores) {
					
					if(d.validarCuentaCorreo( si.getValue().toString().trim() ) )
						lstCc.add(  si.getValue().toString().trim() );
					
				}
				 
				String sSucursal = m.get("sNombreSucursal").toString();
				String sCajero  = m.get("sNombreEmpleado").toString();
				String sSubject = caja.getId().getCaname().trim() + ": Referencia de Autorización";
				String sMontoAutorizado =  d.formatDouble(Double.parseDouble(m.get("monto").toString()));
				
				List lstMetPago2 = (List)m.get("metpago");		
				Metpago[] metpago2 = null;
				
				//poner descripcion a metodo
				for (int m = 0 ; m < lstMetPago2.size();m++){
					metpago2 = (Metpago[])lstMetPago2.get(m);
					if(sCodigoMetPago.trim().equals(metpago2[0].getId().getCodigo().trim())){
						sCodigoMetPago = metpago2[0].getId().getMpago().trim();
						break;
					}
				}
				
				solCtrl.enviarCorreo(sAutorizador[0], vf0101.getId().getWwrem1().trim(), 
										caja.getId().getCaname(),
										lstCc, txtReferencia.getValue().toString(), 
										txtObs.getValue().toString(),sSubject,sCajero,
										sNomAut,sSucursal,caja.getId().getCaname().trim(),
										sMontoAutorizado + " " + moneda, sCodigoMetPago,
										hFac.getId().getCodcli() + " "+hFac.getId().getNomcli()
						);

			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			dwSolicitud.setWindowState("hidden");
			metodosGrid.dataBind();
			
			SmartRefreshManager.getCurrentInstance().addSmartRefreshId(
					metodosGrid.getClientId(FacesContext
							.getCurrentInstance()));
			SmartRefreshManager.getCurrentInstance().addSmartRefreshId(
					track.getClientId(FacesContext
							.getCurrentInstance()));
		}
	}
	/***********VALIDAR DATOS DEL DIALOGO DE LA SOLICITUD DE AUTORIZACION*******************************************/
	public boolean validarSolicitud() {
		boolean validado = true;			
		//validar referencia
		try{
			restableceEstiloAutorizacion();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Matcher matFecha = null, matCorreo = null;
			Date dFecha = null;
			String sFecha =null; 
			String[] sCorreo = null;
			//expresion regular de fecha
			if(txtFecha.getValue() != null){
				dFecha = (Date)txtFecha.getValue();
				sFecha = sdf.format(dFecha);
				Pattern pFecha = Pattern.compile( "^[0-3]?[0-9](/|-)[0-2]?[0-9](/|-)[1-2][0-9][0-9][0-9]$" );	
				matFecha = pFecha.matcher(sFecha);
			}
			//expresion regular de correo electronico
				sCorreo = cmbAutoriza.getValue().toString().split(" ");
				Pattern pCorreo = Pattern.compile( "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$" );	
				matCorreo = pCorreo.matcher(sCorreo[0].toUpperCase());
			//expresion regular valores alfanumericos
				Matcher matRef = null;
				Matcher matObs = null;
				if(txtObs.getValue() != null){
					String sObs = txtObs.getValue().toString();
					Pattern pAlfa = Pattern.compile("^[A-Za-z0-9-'.;,\\p{Blank}]+$");
					matObs = pAlfa.matcher(sObs);
					
				}
				if(txtReferencia.getValue() != null){
					String sRef = txtReferencia.getValue().toString();
					Pattern pAlfa = Pattern.compile("^[A-Za-z0-9-_\\p{Blank}]+$");
					matRef = pAlfa.matcher(sRef);
				}
			//validar Referencia
			if(txtReferencia.getValue() == null || txtReferencia.getValue().toString().trim().equals("")){
				lblMensajeAutorizacion.setValue("La referencia es requerida");
				txtReferencia.setStyleClass("frmInput2Error");
				validado = false;
			}else if(!matRef.matches()){
				lblMensajeAutorizacion.setValue("El campo Referencia contiene caracteres invalidos");
				txtReferencia.setStyleClass("frmInput2Error");
				validado = false;
			}else if(txtReferencia.getValue().toString().length() > 50){
				validado = false;
				lblMensajeAutorizacion.setValue("La cantidad de caracteres en el campo referencia es muy alta (lim. 50)");
				txtReferencia.setStyleClass("frmInput2Error");
			}
			//validar fecha
			else if (txtFecha.getValue() == null || txtFecha.getValue().toString().trim().equals("")){
				lblMensajeAutorizacion.setValue("La fecha es requerida");
				txtFecha.setStyleClass("frmInput2Error2");
				validado = false;
			}
			//validar observacion
			else if (txtObs.getValue() == null || txtObs.getValue().toString().trim().equals("")) {		
				lblMensajeAutorizacion.setValue("La Observación es requerida");
				txtObs.setStyleClass("frmInput2Error");
				validado = false;
			}else if(!matObs.matches()){
				lblMensajeAutorizacion.setValue("El campo Observación contiene caracteres invalidos");
				txtObs.setStyleClass("frmInput2Error");
				validado = false;
			}
			else if(txtObs.getValue().toString().length() > 250){
				validado = false;
				lblMensajeAutorizacion.setValue("La cantidad de caracteres en el campo Observación es muy alta (lim. 250)");
				txtObs.setStyleClass("frmInput2Error");
			}
			else if(matFecha == null || !matFecha.matches()){
				lblMensajeAutorizacion.setValue("La fecha no es valida");
				txtFecha.setStyleClass("frmInput2Error2");
				validado = false;
			}
			//validar correo
			else if(matCorreo == null || !matCorreo.matches()){
				lblMensajeAutorizacion.setValue("El autorizador seleccionado no tiene correo electrónico configurado");
				cmbAutoriza.setStyleClass("frmInput2Error2");
				validado = false;
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return validado;
	}
/**********************************************************************************************************/
	public void onCerrarAutorizacion(StateChangeEvent e){
		restableceEstiloAutorizacion();
		txtFecha.setValue("");
		txtReferencia.setValue("");
		txtObs.setValue("");
		dwSolicitud.setWindowState("hidden");		
	}
/*********************RESTABLECE EL ESTILO DE LOS CAMPOS DE LA AUTORIZACION******************************/
	public void restableceEstiloAutorizacion(){
		txtObs.setStyleClass("frmInput2");
		txtReferencia.setStyleClass("frmInput2");
		txtFecha.setStyleClass("");
		cmbAutoriza.setStyleClass("frmInput2");
		
	}
/*******************CIERRA EL CUADRO DE DIALOGO Y CANCELA LA SOLICITUD***********************************************************************/
public void cancelarSolicitud(ActionEvent e) {	

	try
	{
		//++++++++++++++++++++++++++++++++++++++++
		//Agregado por LFonseca
		//----------------------------------------
		Vf55ca01 caja = (Vf55ca01) ((List) m.get("lstCajas")).get(0);
		int caid2 = caja.getId().getCaid();	
		int iCajero = caja.getId().getCacati();
		 
//		lstFacturasSelected = (List) CodeUtil.getFromSessionMap("selectedFacsCredito");
		List lstFacturasSelected2 = (List) m.get("selectedFacsCredito");
		VerificarFacturaProceso verificarFac = new VerificarFacturaProceso();
		for(Object o : lstFacturasSelected2){
			Credhdr fd2 = (Credhdr)o;
			 
			String[] strResultado =  verificarFac.actualizarVerificarFactura(String.valueOf(caid2), 
													String.valueOf(fd2.getId().getCodcli()), 
													String.valueOf(fd2.getNofactura()), 
													fd2.getTipofactura(), 
													fd2.getPartida(), 
													String.valueOf(iCajero));
			 
			
		}
		
		//---------------------------------------------
		//+++++++++++++++++++++++++++++++++++++++++++++
		
	}
	catch (Exception ex) {
		ex.printStackTrace();
	}
	
	restableceEstiloAutorizacion();
	txtFecha.setValue("");
	txtReferencia.setValue("");
	txtObs.setValue("");
	dwSolicitud.setWindowState("hidden");		
}
/**********************************************************************************************************/
/*****************************SUMAR MONTO A APLICAR*************************************************************/	
	public void sumarMontoAplicar(CellValueChangeEvent e){
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
		boolean valido = false;
		Credhdr newFac = null,hFacCurrent = null;
		String sMonedaBase = "";
		CompaniaCtrl cCtrl = new CompaniaCtrl();
        hFacCurrent = null;
        double dFormatedMA = 0.00;
        double montoAplicar = 0.00;
        double dNewMontoApl = 0.00;
		try{
			//Determine the row and column being updated
	        List rows = gvfacturasSelecCredito.getRows();
	        RowItem row  = (RowItem)rows.get(e.getPosition().getRow());
	        int colpos = e.getPosition().getCol();
	        Divisas divisas = new Divisas();
	        String sEquiv;
	        
	        List lstSelectedFacs = (List)m.get("selectedFacsCredito");
	        
	        //Get the current data for that row.
	        newFac = (Credhdr)gvfacturasSelecCredito.getDataRow(row);
	        String sNewMontoApl = e.getNewValue().toString();
	        String sOldMontoApl = e.getOldValue().toString(); 
	        
	        if(!newFac.getTipofactura().equals("IF") && !newFac.getTipofactura().equals("MF")){    
		        
		        //obtener companias x caja
		        sMonedaBase = cCtrl.sacarMonedaBase((F55ca014[])m.get("cont_f55ca014"), newFac.getId().getCodcomp());
		        
		        //validar el monto introducido
		        valido = validarMontoAplicar(sNewMontoApl,newFac,sMonedaBase);
		        
		        if (valido){
		        	montoAplicar = divisas.formatStringToDouble(sNewMontoApl);
		        	//txtMontoAplicar.setValue(divisas.formatDouble(montoAplicar));
		        	newFac.setMontoAplicar(new BigDecimal(sNewMontoApl));
		        	//sustituir el elemento editado en arreglo 
		        	for (int i = 0; i < lstSelectedFacs.size(); i++){
		        		hFacCurrent = (Credhdr)lstSelectedFacs.get(i);
		        		if(newFac.getNofactura() == hFacCurrent.getNofactura() && newFac.getPartida().equals(hFacCurrent.getPartida())){
		        			lstSelectedFacs.remove(i);
		        			lstSelectedFacs.add(i, newFac);
		        			dNewMontoApl =  dNewMontoApl + newFac.getMontoAplicar().doubleValue();
		        		}else{
		        			dNewMontoApl =  dNewMontoApl + hFacCurrent.getMontoAplicar().doubleValue();
		        		}
		        	}
		        	m.put("selectedFacsCredito", lstSelectedFacs);
		        	txtMontoAplicar.setValue(divisas.formatDouble(dNewMontoApl));
		        	
		        	m.put("dMontoAplicarCre",dNewMontoApl);
		        	
		        	List lstSelectedMet = (List)m.get("lstPagos");
		        	//calcular el monto recibido en dependencia de moneda de factura y moneda de pago
					double montoRecibido = calcularElMontoRecibido(lstSelectedMet, newFac, sMonedaBase);
	
					//determinar como dar el cambio
					determinarCambio(lstSelectedMet, newFac, montoRecibido, sMonedaBase);
					
					//calcular Totales
					calcularTotales(lstSelectedFacs, sMonedaBase);
					//m.remove("dMontoAplicarCre");
		        }else{	
		        	dgwMensajeDetalleCredito.setWindowState("normal");
		        }
		        //   
		        gvfacturasSelecCredito.dataBind();
		        srm.addSmartRefreshId(txtMontoAplicar.getClientId(FacesContext.getCurrentInstance()));
		        srm.addSmartRefreshId(dgwMensajeDetalleCredito.getClientId(FacesContext.getCurrentInstance()));
		        srm.addSmartRefreshId(gvfacturasSelecCredito.getClientId(FacesContext.getCurrentInstance()));
		        srm.addSmartRefreshId(lblCambio.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(txtCambioDomestico.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(lnkCambio.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(txtCambio.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(txtCambioForaneo.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(lblCambioDomestico.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(lblPendienteDomestico.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(txtPendienteDomestico.getClientId(FacesContext.getCurrentInstance()));
				
				srm.addSmartRefreshId(montoTotalAplicarDomestico.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(montoTotalAplicarForaneo.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(montoTotalFaltanteDomestico.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(montoTotalFaltanteForaneo.getClientId(FacesContext.getCurrentInstance()));
			}else{
				lblMensajeDetalleCredito.setValue("No es posible establecer el monto a aplicar para los Intereses!!!");
				dgwMensajeDetalleCredito.setWindowState("normal");								
	        	montoAplicar = divisas.formatStringToDouble(sOldMontoApl);
	        	//txtMontoAplicar.setValue(divisas.formatDouble(montoAplicar));
	        	newFac.setMontoAplicar(new BigDecimal(montoAplicar));
	        	m.put("editedFac", newFac); 
	        	
				gvfacturasSelecCredito.dataBind();
				srm.addSmartRefreshId(dgwMensajeDetalleCredito.getClientId(FacesContext.getCurrentInstance()));
			    srm.addSmartRefreshId(gvfacturasSelecCredito.getClientId(FacesContext.getCurrentInstance()));
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
/*************************************************************************************************/
	public boolean validarMontoAplicar(String sMonto, Credhdr hFac ,String sMonedaBase){
		boolean valido = true;
		Divisas divisas = new Divisas();
		double dMontoPen = 0.0, dMontoApl = 0.0;
		try{
			//poner el monto pendiente en dependencia de la moneda
			if(hFac.getMoneda().equals(sMonedaBase)){
				dMontoPen = hFac.getId().getCpendiente().doubleValue();
			}else{
				dMontoPen = hFac.getId().getDpendiente().doubleValue();
			}
			//expresion regular solo numeros
			Matcher matNumero = null;
			if(sMonto != null){
				Pattern pNumero = Pattern.compile("^[0-9]+\\.?[0-9]*$");
				matNumero = pNumero.matcher(sMonto);
			}
			if(sMonto.trim().equals("")){
				lblMensajeDetalleCredito.setValue("El monto ingresado no es valido");
				valido = false;
			}
			else if(!matNumero.matches()){
				lblMensajeDetalleCredito.setValue("El monto ingresado no es valido");
				valido = false;
			}
			else if(divisas.formatStringToDouble(sMonto) > dMontoPen){
				lblMensajeDetalleCredito.setValue("El monto a aplicar debe ser menor o igual al monto Pendiente de la factura");
				valido = false;
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return valido;
	}
/***************************CANCELAR RECIBO A FACTURA DE CREDITO******************************************************/
	public void cancelarRecibo(ActionEvent ev){
		try{
 
			try
			{
				//++++++++++++++++++++++++++++++++++++++++
				//Agregado por LFonseca
				//----------------------------------------
				Vf55ca01 caja = (Vf55ca01) ((List) m.get("lstCajas")).get(0);
				int caid2 = caja.getId().getCaid();	
				int iCajero = caja.getId().getCacati();
				 
				//lstFacturasSelected = (List) CodeUtil.getFromSessionMap("selectedFacsCredito");
				List lstFacturasSelected2 = (List) m.get("selectedFacsCredito");
				VerificarFacturaProceso verificarFac = new VerificarFacturaProceso();
				for(Object o : lstFacturasSelected2){
					Credhdr fd2 = (Credhdr)o;
					 
					String[] strResultado =  verificarFac.actualizarVerificarFactura(String.valueOf(caid2), 
															String.valueOf(fd2.getId().getCodcli()), 
															String.valueOf(fd2.getNofactura()), 
															fd2.getTipofactura(), 
															fd2.getPartida(), 
															String.valueOf(iCajero));
					 
				}
				
				//---------------------------------------------
				//+++++++++++++++++++++++++++++++++++++++++++++
				
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
			dwCancelar.setWindowState("normal");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
/******************************************************************************************************************/
	public void cancelarCancelarRecibo(ActionEvent ev){
		dwCancelar.setWindowState("hidden");
	}
	public void cancelaRecibo(ActionEvent ev){
		List lstFacturas = new ArrayList(),lstNewFacturas = new ArrayList();
		FacturaCredito hFac = null;
		try{
			//SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
			cargarTasasCambioAldia();
			dwCancelar.setWindowState("hidden");
			dwRecibo.setWindowState("hidden");
			lstFacturas = (List)m.get("lstRefreshFacturas");

			m.remove("dMontoAplicarCre");
			m.remove("selectedFacsCredito");
			m.remove("lstDatosTrack_Con");
			
			//&& ========== Excedentes en pagos
			m.remove("scr_SobrantePago");
			m.remove("scr_MpagoSobrante");
			m.remove("scr_SbrDfr");
			
			this.BuscarFacturasCredito(ev);

		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
/****************CERRRAR MENSAJE DE VALIDACION DE RECIBO**************************************************************************/
	public void cerrarProcesa(ActionEvent ev){
		dwProcesa.setWindowState("hidden");
	}
/**********************************************************************************************************/
/*******************************PROCESAR RECIBO*********************************************************/
	public void procesarRecibo(ActionEvent ev){
		boolean valido = false;
	 
		String sMonedaBase = "";
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		
		try{
			 
			m.get("lstPagos");
			
			 //&& ===== Validar si la caja no esta bloqueada.
			String sMensaje = CtrlCajas.generarMensajeBlk();
			if(sMensaje.compareTo("") != 0){
				lblMensajeValidacion.setValue(sMensaje);
				dwProcesa.setStyle("width:400px; min-height:200px;");
				dwProcesa.setWindowState("normal");
				return;
			}
			
			 
			List lstFacturasSelected = (List) m.get("selectedFacsCredito");
			Credhdr hFac = (Credhdr)lstFacturasSelected.get(0);	
		 
			sMonedaBase = cCtrl.sacarMonedaBase((F55ca014[])m.get("cont_f55ca014"), hFac.getId().getCodcomp());
			 

			//&& ================= validar que la suma de equivalentes de forma de pago no es distinta del monto recibido.
			List<MetodosPago> pagos = (ArrayList<MetodosPago>) m.get("lstPagos");
			
			BigDecimal totalEquivs = CodeUtil.sumPropertyValueFromEntityList(pagos, "equivalente", false);
			
			BigDecimal bdMontoAplicado = new BigDecimal( txtMontoAplicar.getValue().toString().trim().replace(",", "") ) ;
			BigDecimal bdMontoRecibido = new BigDecimal( txtMontoRecibido.getValue().toString().trim().replace(",", "") ) ;
			
			if( bdMontoRecibido.compareTo(totalEquivs) != 0 &&  
				bdMontoRecibido.subtract(totalEquivs).abs().compareTo(BigDecimal.ONE) == 1 ) {
				
				String msgValida = " La sumatoria de pagos no coincide con el total del monto recibido, favor verificar nuevamente." ;
				lblMensajeValidacion.setValue(msgValida);
				dwProcesa.setWindowState("normal");
				return ;
			}
			
			
			boolean vaildarDatosRec = this.validarDatosRecibo();
			if(!vaildarDatosRec) {
				dwProcesa.setWindowState("normal");
				return;				
			}
		
			String msgValida = validarCambio(sMonedaBase, hFac.getMoneda() ) ; 
			
			valido = msgValida.isEmpty();
			
			if ( valido ){	
				restablecerEstilos();
				
				dwImprime.setWindowState("normal");
			}else{
				lblMensajeValidacion.setValue(msgValida);
				dwProcesa.setWindowState("normal");
			}
			
			
		}catch(Exception ex){
			ex.printStackTrace(); 
		}
	}
/***********************************************************************************************************/
/****************VALIDAR DATOS DEL RECIBO ANTES DE PROCESAR****************************************/
	public boolean validarDatosRecibo(){
		Divisas divisas = new Divisas();
		boolean validado = true;	
		ReciboCtrl rpCtrl = new ReciboCtrl();
		
		int iNumRecm = 0;
		String sMensajeError = "";
		int y = 145;
		double dMontoRecibido = 0.0,dMontoAplicar = 0.0;
		BigDecimal bdMontoSel = BigDecimal.ZERO;
		Vf55ca01 vf55ca01 = null;
		Credhdr hfac = null, hSel = null;
		List lstSelectedFacs = null;
		try{
			vf55ca01 = (Vf55ca01)((List)m.get("lstCajas")).get(0);
			hfac = (Credhdr)((List)m.get("selectedFacsCredito")).get(0);
			restablecerEstilos();
			selectedMet = (ArrayList<MetodosPago>) m.get("lstPagos");
			//expresion regular de valores alfanumericos
			Matcher matAlfa = null;
			if(txtConcepto.getValue() != null){
				String sObs = txtConcepto.getValue().toString();
				Pattern pAlfa = Pattern.compile("^[A-Za-z0-9-.;,\\p{Blank}]+$");
				matAlfa = pAlfa.matcher(sObs);	
			}
			//validar si la suma de monto a aplicar es mayor que el monto recibido
			lstSelectedFacs = (List)m.get("selectedFacsCredito");
			for(int i = 0;i <lstSelectedFacs.size();i++){
				hSel = (Credhdr)((List)m.get("selectedFacsCredito")).get(i);
				bdMontoSel = bdMontoSel.add(hSel.getMontoAplicar());
			}
			bdMontoSel = divisas.roundBigDecimal(bdMontoSel);
			/******************RECIBO AUTOMATICO************************/
			if (cmbTiporecibo.getValue().toString().equals("AUTOMATICO")){
				txtNumRec.setStyle("visibility:hidden");
				//validar metodos de pago
				if (selectedMet == null || selectedMet.isEmpty()){
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> No se han agregado pagos al recibo<br>";
					metodosGrid.setStyleClass("igGridError");
					validado = false;
					y=y+14;
				}
				if (!txtConcepto.getValue().toString().trim().equals("")){
					if(!matAlfa.matches()){
						sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El campo concepto contiene caracteres invalidos <br>";
						txtConcepto.setStyleClass("frmInput2Error");
						validado = false;
						y=y+14;
					}
				}
				if(txtConcepto.getValue().toString().length() > 250){
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> La longitud del campo es muy alta (lim. 250) <br>";
					txtConcepto.setStyleClass("frmInput2Error");
					validado = false;
					y=y+14;
				}
				dMontoRecibido = divisas.formatStringToDouble(txtMontoRecibido.getValue().toString());
				dMontoAplicar = divisas.formatStringToDouble(txtMontoAplicar.getValue().toString());
				if(dMontoAplicar > dMontoRecibido){
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El monto recibido debe ser mayor al monto a aplicar <br>";
					validado = false;
					y=y+14;
				}	
				double cambio = divisas.roundDouble(dMontoRecibido - dMontoAplicar);
				if (!txtCambioForaneo.getValue().toString().trim().equals("")){
					double dCambioForaneo = divisas.formatStringToDouble(txtCambioForaneo.getValue().toString());
					if(dCambioForaneo > cambio){
						txtCambioForaneo.setValue(divisas.formatDouble(cambio));
						txtCambioDomestico.setValue("0.00");
						sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> La cantidad de cambio ingresada es mayor al correcto <br>";
						validado = false;
						y=y+20;
					}
				}		
			/******************RECIBO MANUAL************************/
			}else{
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				Matcher matFecha = null;
				Date dFecha = null;
				String sFecha =null; 
				
				if(txtFecham.getValue() != null){
					dFecha = (Date)txtFecham.getValue();
					sFecha = sdf.format(dFecha);
					Pattern pFecha = Pattern.compile( "^[0-3]?[0-9](/|-)[0-2]?[0-9](/|-)[1-2][0-9][0-9][0-9]$" );	
					matFecha = pFecha.matcher(sFecha);
				}
				//expresion regular solo numeros
				Matcher matNumero = null;
				if(txtNumRec.getValue() != null && !txtNumRec.getValue().toString().trim().equals("")){
					Pattern pNumero = Pattern.compile("^[0-9]*$");
					matNumero = pNumero.matcher(txtNumRec.getValue().toString().trim());
					if(matNumero.matches() && !txtNumRec.getValue().toString().trim().equals("")){
						iNumRecm = Integer.parseInt(txtNumRec.getValue().toString().trim());
					}
				}
				//validar metodos de pago
				if (selectedMet == null || selectedMet.isEmpty()){
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> No se han agregado pagos al recibo<br>";
					metodosGrid.setStyleClass("igGridError");
					validado = false;
					y=y+14;
				}
				//valida la fecha del recibo
				if(txtFecham.getValue() == null || (txtFecham.getValue().toString().trim()).equals("")){
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Fecha de recibo requerida<br>";
					txtFecham.setStyleClass("frmInput2Error2");
					validado = false;
					y=y+14;
				} 
				if(matFecha == null || !matFecha.matches()){
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Fecha de recibo no es valida<br>";
					txtFecham.setStyleClass("frmInput2Error");
					validado = false;
					y=y+14;
				}
				//validar el numero de recibo
				 if(txtNumRec.getValue() == null || (txtNumRec.getValue().toString().trim()).equals("")){
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Número de recibo manual es requerido<br>";
					txtNumRec.setStyleClass("frmInput2Error");
					validado = false;
					y=y+14;
				}
				if(matNumero == null || !matNumero.matches()){
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Número de recibo no es valido<br>";
					txtNumRec.setStyleClass("frmInput2Error");
					validado = false;
					y=y+14;
				}
			
				if(txtConcepto.getValue().toString().length() > 250){
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> La longitud del campo es muy alta (lim. 250) <br>";
					txtConcepto.setStyleClass("frmInput2Error");
					validado = false;
					y=y+14;
				}
				//validar existencia del numero de recibo manual
				if (iNumRecm > 0){
					if(rpCtrl.verificarNumeroRecibo(vf55ca01.getId().getCaid(), hfac.getId().getCodcomp(), iNumRecm,vf55ca01.getId().getCaco().trim())){
						sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El número de recibo manual ya existe!<br>";
						txtNumRec.setStyleClass("frmInput2Error");
						validado = false;
						y=y+14;
					}
				}
				dMontoRecibido = divisas.formatStringToDouble(txtMontoRecibido.getValue().toString());
				dMontoAplicar = divisas.formatStringToDouble(txtMontoAplicar.getValue().toString());
				if(dMontoAplicar > dMontoRecibido){
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El monto recibido debe ser mayor al monto a aplicar <br>";
					//txtConcepto.setStyleClass("frmInput2Error");
					validado = false;
					y=y+20;
				}
				double cambio = dMontoRecibido - dMontoAplicar;
				if (!txtCambioForaneo.getValue().toString().trim().equals("")){
					double dCambioForaneo = divisas.formatStringToDouble(txtCambioForaneo.getValue().toString());
					if(dCambioForaneo > cambio){
						txtCambioForaneo.setValue(divisas.formatDouble(cambio));
						txtCambioDomestico.setValue("0.00");
						sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> La cantidad de cambio ingresada es mayor al correcto <br>";
						validado = false;
						y=y+20;
					}
				}		
			}
			if (bdMontoSel.doubleValue() > dMontoRecibido){
				sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El monto a aplicar de facturas es mayor al monto recibido <br>";
				validado = false;
				y=y+20;
			}
			lblMensajeValidacion.setValue(sMensajeError);
			dwProcesa.setStyle("width:390px;height:"+y+"px");
		}catch(Exception ex){
			validado = false;
			ex.printStackTrace();
		}
		return validado;
	}
/*****************REESTABLECER EL ESTILO DE LOS COMPONENTES*********************************************************/
	public void restablecerEstilos(){
		txtNumRec.setStyleClass("frmInput2");
		metodosGrid.setStyleClass("igGrid");
		txtFecham.setStyleClass("");
		txtConcepto.setStyleClass("frmInput2");
	}
/********************************************************************************************************************/
	/********************************************************************************************************************/
	public void cancelarProcesa(ActionEvent e) {		
		dwImprime.setWindowState("hidden");
	}
	
	
	/******************** Grabar el recibo de credito ************************/
	public void procesarReciboCr(ActionEvent ev){
		Divisas dv = new Divisas();
		
		Session sesionCreditos = null;
		Transaction transCreditos = null;
		F55ca014 dtComp = null;
		
		boolean aplicado = true;
		boolean bCambiodom = false;
		boolean bHayPagoSocket = false;
		boolean bAplicadoSockP = false ;
		boolean bHayFicha = false;
		
		String msgProceso = new String("");
		String codsuc     = new String("");
		String codcomp    = new String("");
		String codunineg  = new String("");
		String tiporec    = new String(valoresJDEInsCredito[0]); 
		String sTipoDoc   = new String("");
		String sCodunineg = new String("");
		
		int caid = 0;
		int numrec = 0;
		Date fechaRecibo = new Date();
		int fechajuliana = FechasUtil .DateToJulian(fechaRecibo);
	
		BigDecimal bdTasa = BigDecimal.ONE;
		
		MetodosPago mpSobrante = null;
		List<MetodosPago> lstPagoFicha = new ArrayList<MetodosPago>();
		List<Credhdr> lstFacturasSelected = new ArrayList<Credhdr>();
		List<MetodosPago> lstMetodosPago  = new ArrayList<MetodosPago>();
		
		
		List<String[]>dtaFacs = new ArrayList<String[]>();
		List<String[]>dtaFacsTemp = new ArrayList<String[]>();
		Credhdr hFac = null;
		
		Object[] dtaCnDnc = null;
		boolean cnDonacion = false;
		boolean aplicadonacion = false;
		List<MetodosPago>pagosConDonacion;
		
		try {
			Boolean oneclick = m.containsKey("procesandoReciboValidacion") ;
			if(oneclick){	
				return;
			}
			m.put("procesandoReciboValidacion", "true");	
		}catch(Exception e) {
			LogCajaService.CreateLog("procesarReciboCr", "ERR", "Error al validad multiples procesamiento");
		}
		
		
		try {
			LogCajaService.CreateLog("FacCreditoDAO.procesarReciboCr - INICIO", "JR8", "Inicio ejecucion metodo");
					
			iNumeroFichaCambio = 0;
			
			//&& ===== Datos globales a utilizar.
			List<Credhdr> facturas = (ArrayList<Credhdr>) m.get("selectedFacsCredito");
			List<MetodosPago> pagos = (ArrayList<MetodosPago>) m.get("lstPagos");
			for (MetodosPago mp : pagos) 
				lstMetodosPago.add(mp.clone());
			
			//&& =====================================================
			BigDecimal totalEquivs = CodeUtil.sumPropertyValueFromEntityList(pagos, "equivalente", false);
			
			BigDecimal bdMontoAplicado = new BigDecimal( txtMontoAplicar.getValue().toString().trim().replace(",", "") ) ;
			BigDecimal bdMontoRecibido = new BigDecimal( txtMontoRecibido.getValue().toString().trim().replace(",", "") ) ;
			
			if( bdMontoRecibido.compareTo(totalEquivs) != 0 &&  
				bdMontoRecibido.subtract(totalEquivs).abs().compareTo(BigDecimal.ONE) == 1 ) {
				aplicado = false;
				msgProceso = " La sumatoria de pagos no coincide con el total del monto recibido, favor verificar nuevamente." ;
				return ;
			}
			
			//========================================================			
			for (Credhdr hf : facturas) {
				lstFacturasSelected.add(hf.clone());

				if (hf.getMontoAplicar().compareTo(BigDecimal.ZERO) == 0)
					continue;
				
				String[] dta = new String[] {
						String.valueOf(hf.getNofactura()),
						hf.getMontoAplicar().toString(), 
						hf.getPartida(),
						hf.getId().getCodunineg(),
						hf.getId().getCodsuc(), 
						hf.getTipofactura() 
					};
				dtaFacs.add(dta);
				
				String[] dtaTemp = new String[] {
						String.valueOf(hf.getNofactura()),
						String.valueOf(hf.getId().getCpendiente()),
						String.valueOf(hf.getId().getPartida()),
						String.valueOf(hf.getId().getCodsuc()),
						String.valueOf(hf.getId().getTipofactura()) };
				dtaFacsTemp.add(dtaTemp);
			}
			
			// Validar si la lista de factura a aplicar es igual a cero
			if (dtaFacs == null || dtaFacs.size() <= 0) {
				aplicado = false;
				msgProceso = "No se encontraron facturas a aplicar." ;
				return ;
				//throw new Exception("No se encontraron facturas a aplicar.");
			}
					
			hFac = lstFacturasSelected.get(0);
			
			Vautoriz vaut  = ((Vautoriz[])m.get("sevAut"))[0];
			Vf55ca01 caja  = ((List<Vf55ca01>)m.get("lstCajas")).get(0);
			F55ca014[] f14 = (F55ca014[])m.get("cont_f55ca014");
			dtComp = CompaniaCtrl.filtrarCompania(f14, hFac.getId().getCodcomp());
			
			caid    = caja.getId().getCaid();
			codsuc  = caja.getId().getCaco();
			codcomp  = hFac.getId().getCodcomp();
			sTipoDoc = hFac.getTipofactura();
			sCodunineg = hFac.getId().getCodunineg();
			String sCajero = (String)m.get("sNombreEmpleado");	
			String monedabase = dtComp.getId().getC4bcrcd();
			
			//&& ========= Validar pago con socket.
			for (MetodosPago m : lstMetodosPago) {
				bHayPagoSocket = (m.getMetodo().compareTo(MetodosPagoCtrl.TARJETA) == 0 &&  m.getVmanual().compareTo("2") == 0 );
				if(bHayPagoSocket)
					break;
			}
			//&& ========= aplicar el pago de Socket Pos msgSocket = ""; 
			if( bHayPagoSocket ){
				String msgSocket =  PosCtrl.aplicarPagoSocketPos( 
									lstMetodosPago, hFac.getNomcli(), 
									caid, codcomp, tiporec);
				aplicado = msgSocket.compareTo("") == 0;
				bAplicadoSockP = aplicado;
				msgProceso = msgSocket;
				if(!aplicado) return;
			}
			
			BigDecimal bdTasaJde = obtenerTasaOficial();
			BigDecimal bdTasaPar = obtenerTasaParalela("COR");

			sesionCreditos = HibernateUtilPruebaCn.currentSession();
			transCreditos = sesionCreditos.beginTransaction() ; 
 
			
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
			
			//&& ========= Grabar el recibo en caja.
			aplicado = insertarRecibo(sesionCreditos, transCreditos, lstFacturasSelected, caja, hFac,vaut, lstMetodosPago);
			
			if(!aplicado){
				msgProceso = (strMensajeValidacion == null || 
							strMensajeValidacion.trim().compareTo("") == 0) ? 
								"Recibo no aplicado, Error al grabar datos " +
								"del recibo en caja"
								: strMensajeValidacion;
				throw new Exception(msgProceso);
			}

			numrec = Integer.parseInt(String.valueOf(m.get("iNumRecCredito")));
			
			aplicado = new com.casapellas.controles.ReciboCtrl()
					.procesoPagoFacturaWithSession(dtaFacs, caid, codcomp, fechajuliana, hFac.getId().getCodcli(), tiporec,sesionCreditos,numrec);
			
			//&& ========= ajustar el metodo de pago que contiene el sobrante.
			BigDecimal montoSobrante = BigDecimal.ZERO;
			BigDecimal equivSobrante = BigDecimal.ZERO;
			
			if( m.containsKey("scr_SobrantePago") ){
				double dMontorec  = 0;
				double dMontoNeto = 0;
				double dDiferencia= 0;
				double dMonto = 0,dEquiv = 0;
				mpSobrante = (MetodosPago)m.get("scr_MpagoSobrante");
				BigDecimal excedente = BigDecimal.ZERO;
				
				dMontoNeto = dv.formatStringToDouble(txtMontoAplicar.getValue().toString());
				dMontorec =  dv.formatStringToDouble(txtMontoRecibido.getValue().toString());
				dDiferencia = dMontorec - dMontoNeto;
				dDiferencia = dv.roundDouble(dDiferencia);
				
				for(int i = lstMetodosPago.size()-1; i >= 0; i--){
					MetodosPago mpago = lstMetodosPago.get(i);
					if(!mpago.equals(mpSobrante))
						continue;
					
					montoSobrante = new BigDecimal( String.valueOf( mpago.getMonto() ) ) ;
					equivSobrante = new BigDecimal( String.valueOf( mpago.getEquivalente() ) ) ; 
					
					
					dEquiv = mpago.getEquivalente();
					dMonto = mpago.getMonto();
					excedente =  mpSobrante.getExcedente();
					
					dEquiv -= dDiferencia;
					dMonto = BigDecimal.valueOf(dMonto).subtract(excedente).doubleValue();
							
					dEquiv = dv.roundDouble(dEquiv,4);
					dMonto = dv.roundDouble(dMonto,4);
					
					mpago.setMonto(dMonto);
					mpago.setEquivalente(dEquiv);
					
					break;
				}
			}
			
			//&& ====== operar los montos y equivalencias con precision de 4 decimales.
			BigDecimal bdMontoTmp = BigDecimal.ZERO ;
			BigDecimal bdEquivTmp = BigDecimal.ZERO ;
			BigDecimal bdTasaTmp  = BigDecimal.ZERO ;
			BigDecimal bdTotalEquiv = BigDecimal.ZERO ;
			BigDecimal bdMtoAplicar = BigDecimal.ZERO ;
			
			for (MetodosPago mp : lstMetodosPago) {
				
				bdMontoTmp =  new BigDecimal(Double.toString(mp.getEquivalente()));
				bdEquivTmp = new BigDecimal(Double.toString(mp.getEquivalente()));
				bdTasaTmp  = mp.getTasa();
				
				if (bdEquivTmp.compareTo(bdMontoTmp) == 1)
					bdEquivTmp = bdMontoTmp.multiply(bdTasaTmp);
				else if (bdEquivTmp.compareTo(bdMontoTmp) == -1)
					bdEquivTmp = bdMontoTmp.divide(bdTasaTmp, 4, RoundingMode.HALF_UP);
				
				bdEquivTmp = bdEquivTmp.setScale(4, RoundingMode.HALF_UP);
				bdTotalEquiv = bdTotalEquiv.add(bdEquivTmp);
				
				mp.setEquivalente(bdEquivTmp.doubleValue());
			}
			//===================
			
			bdMtoAplicar = new BigDecimal(txtMontoAplicar.getValue().toString().trim().replace(",", ""));
			double dTotalAplicar = bdMtoAplicar.doubleValue();
			
			//&& ========= restar el cambio del pago de efectivo.
			boolean paso1 = false, paso2 = false;
			double dCambio1 = 0, dCambio2 = 0, dNewMonto1 = 0, dNewMonto2 = 0;
			double dMontoFicha = 0;
			
			String sCambio1 = "", sCambio2 = "";
			String sLblCambio1 = "", sLblCambio2 = "";
			String[] sLblCambios1, sLblCambios2;
			
			sLblCambio1 = lblCambio.getValue().toString();
			sLblCambio2 = lblCambioDomestico.getValue().toString();
			
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
					MetodosPago mp = lstMetodosPago.get(i);

					if (!paso1 && mp.getMetodo().compareTo( MetodosPagoCtrl.EFECTIVO ) == 0 && 
						 mp.getMoneda().compareTo(monedabase) != 0 ) {
						
						if (hFac.getMoneda().compareTo(monedabase) == 0) {
							
							//&& ============= hay cambio domestico
							if(dCambio2 > 0){
								
								dNewMonto1 = dv.roundDouble(mp.getEquivalente() - dCambio2);
								dMontoFicha = dv.roundDouble(dCambio2/mp.getTasa().doubleValue());
																	
								mp.setMonto(dv.roundDouble(dNewMonto1/ mp.getTasa().doubleValue()));
								mp.setEquivalente(dNewMonto1);
								
								bHayFicha = dMontoFicha > 0 ;

								MetodosPago metPagoFicha = new MetodosPago(MetodosPagoCtrl.EFECTIVO,
										mp.getMoneda(), dMontoFicha, 
										mp.getTasa(), dCambio2,
										"", "","", "","0",0);
								
								lstPagoFicha.add(metPagoFicha);
								
							}
							if(dCambio1 > 0){
								dNewMonto1 = mp.getMonto() - dCambio1;								
								mp.setMonto(dNewMonto1);
								dNewMonto1 = dCambio1 * mp.getTasa().doubleValue();
								dNewMonto2 = mp.getEquivalente();
								dNewMonto1 = dv.roundDouble(dNewMonto2 - dNewMonto1);
								mp.setEquivalente(dNewMonto1);
								paso1 = true;
							}
							paso1 = true;
							
						}else{
							
							if(dCambio2 > 0){
								
								dNewMonto1 = mp.getMonto() - dv.roundDouble((dCambio2/bdTasa.doubleValue()));
								dMontoFicha = dv.roundDouble((dCambio2/bdTasa.doubleValue()));
								
								mp.setMonto(dNewMonto1);
								mp.setEquivalente(dNewMonto1);
								
								bHayFicha =  dMontoFicha > 0;
								
								MetodosPago metPagoFicha = new MetodosPago(MetodosPagoCtrl.EFECTIVO,
										mp.getMoneda(), dMontoFicha, bdTasa,
										dCambio2, "", "","", "","0",0);
								lstPagoFicha.add(metPagoFicha);
							}
							
							if(dCambio1 > 0){
								dNewMonto1 = mp.getMonto() - dCambio1;								
								mp.setMonto(dNewMonto1);
								mp.setEquivalente(dNewMonto1);
								
								lstMetodosPago.remove(i);
								lstMetodosPago.add(i, mp);
								paso1 = true;
							}
						}
					} else 
						if (mp.getMetodo().compareTo(MetodosPagoCtrl.EFECTIVO) == 0 && 
							mp.getMoneda().compareTo(sLblCambio2) == 0 && !paso2) {
							dNewMonto2 = mp.getMonto() - dCambio2;
							mp.setMonto(dNewMonto2);
							mp.setEquivalente(dNewMonto2
									* mp.getTasa().doubleValue());

							paso2 = true;
						}
				}
			} else {
				
				//&& =============== hay cambio en una moneda unicamente
				
				sCambio1 = txtCambio.getValue().toString();
				dCambio1 = dv.formatStringToDouble(sCambio1);
				sLblCambios1 = sLblCambio1.trim().split(" ");
				
				sLblCambio1 = sLblCambios1[sLblCambios1.length - 1].substring(0, 3);
				
				
				if(dCambio1 > 0 ) {
				
				
					for (int i = 0; i < lstMetodosPago.size(); i++) {
						MetodosPago mp = lstMetodosPago.get(i);
						
						if (mp.getMetodo().equals(MetodosPagoCtrl.EFECTIVO) && mp.getMoneda().equals(sLblCambio1) && !paso1) {
							
							//&& ======= se saca el cambio en cor del pago en cor
							if (sLblCambio1.equals( monedabase )&& hFac.getMoneda().equals( monedabase )) {
								dNewMonto1 = mp.getMonto() - dCambio1;
								mp.setMonto(dNewMonto1);
								mp.setEquivalente(dNewMonto1);
							} 
							else	
							//&& ======= se saca el cambio del pago en cor y se convierte a usd con tasa del metodo
							
								if (sLblCambio1.equals(monedabase) && !hFac.getMoneda().equals(monedabase) &&  lstMetodosPago.size() >= 1) {
									
									BigDecimal bdCambio  = bdTotalEquiv.subtract(bdMtoAplicar);
									bdCambio = bdCambio.multiply(mp.getTasa());
									bdCambio = bdCambio.setScale(4, RoundingMode.HALF_UP);
									
									BigDecimal bdMtoNeto = BigDecimal.valueOf(mp.getMonto()).subtract(bdCambio);
									bdMtoNeto = bdMtoNeto.setScale(4, RoundingMode.HALF_UP);
									
									mp.setMonto( bdMtoNeto.doubleValue() ) ;
									mp.setEquivalente( bdMtoNeto.divide(mp.getTasa(),4, RoundingMode.HALF_UP).doubleValue() );
								
							}
							else
							//&& ======= si pago toda la fac de usd en cor
							if (sLblCambio1.compareTo(monedabase) == 0 && 
								hFac.getMoneda().compareTo(monedabase) == 0 && 
								lstMetodosPago.size() == 1) {
									dNewMonto1 = mp.getMonto() - dCambio1;
									mp.setMonto(dNewMonto1);
									mp.setEquivalente(dv.roundDouble(dNewMonto1/ 
											mp.getTasa().doubleValue()));
							} else
							//&& ======= el pago en usd cubre toda la venta
							if (sLblCambio1.compareTo(monedabase) == 0 && 
									hFac.getMoneda().equals(monedabase) && 
									lstMetodosPago.size() == 1) {
								dNewMonto1 = mp.getEquivalente() - dCambio1;
								mp.setMonto(dv.roundDouble(dNewMonto1 / mp.getTasa().doubleValue()));
								mp.setEquivalente(dNewMonto1);
							}
							paso1 = true;
						} 
						else
						//&& ============  el pago en usd cubre toda la venta
						if (mp.getMetodo().compareTo(MetodosPagoCtrl.EFECTIVO) == 0 && sLblCambio1
								.compareTo(monedabase)  == 0 && mp.getMoneda()
								.compareTo(monedabase) != 0 && !paso1  && 
								hFac.getMoneda().compareTo(monedabase) == 0 &&
								lstMetodosPago.size() == 1) {
							
							dNewMonto1 = mp.getEquivalente() - dCambio1;
							dMontoFicha = mp.getMonto() - dv.roundDouble(
									dNewMonto1/ mp.getTasa().doubleValue());
	
							mp.setMonto(dv.roundDouble(dNewMonto1/ 
									mp.getTasa().doubleValue()));
							
							mp.setEquivalente(dNewMonto1);
							
							bHayFicha = dMontoFicha > 0;
							
							MetodosPago metPagoFicha = new MetodosPago(MetodosPagoCtrl.EFECTIVO,
										mp.getMoneda(), dMontoFicha, mp.getTasa(),
										dCambio1, "", "","", "","0",0);
							lstPagoFicha.add(metPagoFicha);
						}
					}
				}
			}
			//&& =========== Guardar la ficha de cambio y actualizar el recibo en caja.
			if(bHayFicha){
				aplicado = insertarFichaCV(sesionCreditos, transCreditos, caja, hFac, vaut, dMontoFicha, numrec, sCajero,lstPagoFicha);
				if(!aplicado){
					msgProceso = (strMensajeValidacion == null || 
								strMensajeValidacion.trim().compareTo("") == 0) ? 
								"Recibo no aplicado, error al ficha de " +
								"compra/Venta, intente nuevamente"
								: strMensajeValidacion;
					throw new Exception(msgProceso);
				}
			}
			
			// inicio ========================================================================
			//&& =========== Redondeo de faltantes al total recibido contra monto aplicado.
			BigDecimal totalFacts = BigDecimal.ZERO;
			BigDecimal totalEquiv = BigDecimal.ZERO;
			for (MetodosPago mp : lstMetodosPago) 
				totalEquiv = totalEquiv.add(new BigDecimal(
						Double.toString( mp.getEquivalente())));
			 
			List<Credhdr>selectedFacs = (ArrayList<Credhdr>)m.get("selectedFacsCredito");
			for (Credhdr credhdr : selectedFacs) {
				totalFacts = totalFacts.add( (
						(hFac.getMoneda().equals(monedabase))?
							credhdr.getId().getCpendiente() :
							credhdr.getId().getDpendiente() ) );
			}
			
			if( m.containsKey("dMontoAplicarCre") ){
				totalFacts = new BigDecimal(m.get("dMontoAplicarCre").toString());
			}
			
			if(totalFacts.compareTo(totalEquiv) == 1 && totalFacts.subtract(totalEquiv).compareTo(new BigDecimal("0.30")) == -1){
			
				lstMetodosPago.get(lstMetodosPago.size()-1).setEquivalente(
					Divisas.roundDouble( totalFacts.subtract(totalEquiv)
						.add(new BigDecimal( Double.toString(lstMetodosPago
							.get(lstMetodosPago.size()-1).getEquivalente())))
							.doubleValue(), 2)  
						) ;
			}
			// finaliza ========================================================================
			
			
			//&& ************************************************************************** && //
			//&& **************************** Grabar en E1  ******************************* && //
			
			//&& ====== Establecer el monto aplicado a cada factura
			for (Credhdr factura : facturas) {
				boolean pagoparcial = 
				 (  (factura.getMoneda().compareTo(monedabase) == 0 && 
					  factura.getMontoAplicar().compareTo(factura.getId().getCpendiente()) != 0) || 
					(factura.getMoneda().compareTo(monedabase) != 0 && 
					 factura.getMontoAplicar().compareTo(factura.getId().getDpendiente()) != 0)
				) ;
				factura.setPagoparcial(pagoparcial);
			}
			
			List<String> numerosRecibosJde = new ArrayList<String>();
				
			for (int i = 0; i < lstMetodosPago.size(); i++) {
				int numeroReciboJde = Divisas.numeroSiguienteJdeE1Custom(valoresJdeNumeracion[8],valoresJdeNumeracion[9] );
				numerosRecibosJde.add(String.valueOf(numeroReciboJde));
			}
			
			if(numerosRecibosJde.isEmpty()) {
				aplicado = false;
				msgProceso = "Error al generar consecutivo número de documento para el recibo";
				throw new Exception(msgProceso);
			}
			
			int numeroBatchJde = Divisas.numeroSiguienteJdeE1(  );
			int numeroReciboJde = Integer.parseInt( numerosRecibosJde.get(0) ) ;
			
			if (numeroBatchJde == 0) {
				aplicado = false;
				msgProceso = "Error al generar consecutivo número de batchs para el recibo";
				throw new Exception(msgProceso);
			}
			if (numeroReciboJde == 0) {
				aplicado = false;
				msgProceso = "Error al generar consecutivo número de documento para el recibo";
				throw new Exception(msgProceso);
			}
			
			ProcesarPagoFacturaJdeCustom ppf = new  ProcesarPagoFacturaJdeCustom() ;
			
			ppf.executeQueries = false; 
			
			ppf.caid = caid ;
			ppf.codcomp = codcomp ;
			ppf.tiporec = tiporec ;
			ppf.numrec = numrec;
			ppf.fecharecibo = fechaRecibo ; 
			
			ppf.facturas = facturas ;
			ppf.formasdepago = lstMetodosPago ;
			ppf.tasaCambioRecibo = bdTasaJde ;  
			ppf.tasaCambioOficial = bdTasaJde;  
		
			ppf.numeroBatchJde = String.valueOf(numeroBatchJde);
			ppf.numeroReciboJde = String.valueOf(numeroReciboJde);
			ppf.numerosReciboJde = numerosRecibosJde;
		
			ppf.codigousuario = vaut.getId().getCodreg();
			ppf.usuario = vaut.getId().getLogin();
			ppf.programaActualiza = PropertiesSystem.CONTEXT_NAME;
			ppf.moduloSistema = "RCREDITO";
			ppf.valoresJDEInsCredito =valoresJDEInsCredito;
			
			ppf.ajustarMontoAplicado = false;
			
			ppf.msgProceso = "";
			
			ppf.procesarPagosFacturas(sesionCreditos);
			
			aplicado = ppf.msgProceso.isEmpty();
			
			if(!aplicado){
				 msgProceso = ppf.msgProceso ;
				 if(msgProceso == null || msgProceso.isEmpty() ){
					 msgProceso = "Error al crear recibo en JdEdward's por factura de Crédito ";
				 }
				 throw new Exception(msgProceso);
			}
			
			List<String[]> queriesInsert = ppf.lstSqlsInserts;
			if( queriesInsert == null || queriesInsert.isEmpty() ){
				aplicado = false;
				msgProceso = "Error al crear recibo en JdEdward's por factura de Crédito ";
				throw new Exception(msgProceso);
			}
			
			//&& ===============================================================================//
			//&& ========================= grabar el recibo edwards ============================//
			for (String[] querys : queriesInsert) {
				 
				try {
					
					LogCajaService.CreateLog("procesarReciboCr-" + querys[1], "QRY", querys[0]);
					
					int rows = sesionCreditos.createSQLQuery( querys[0] ).executeUpdate() ;
					
					if( rows == 0 ){
						LogCajaService.CreateLog("procesarReciboCr-" + querys[1], "ERR", "Error al procesar");
						aplicado = false;
						msgProceso = "error al procesar: " + querys[1];
						throw new Exception(msgProceso);
					}
					
				} catch (Exception e) {
					LogCajaService.CreateLog("procesarReciboCr-" + querys[1], "ERR", e.getMessage());
					aplicado = false;					
					msgProceso = "fallo en interfaz Edwards "+ querys[1] ;					
					throw new Exception(msgProceso);
				}
			}
			
			//&& ===============================================================================//
			//&& ========= grabar en recibojde enlace de recibo caja recibo edwards=============// 
			aplicado =  com.casapellas.controles.ReciboCtrl.crearRegistroReciboCajaJde(sesionCreditos, caid, numrec, codcomp, codsuc, tiporec, "R", numeroBatchJde, numerosRecibosJde);
			if(!aplicado){
				 msgProceso = "Error al generar registro de asociación de Caja a JdEdward's @recibojde" ;
				 throw new Exception(msgProceso);
			}
			
			//&& ===============================================================================//
			//&& ================ compra venta de dolares por cambio mixto ===== ===============// 
			
			if(bHayFicha){
				
				BigDecimal montoCambio = new BigDecimal(Double.toString(dMontoFicha));
				
				msgProceso = com.casapellas.controles.ReciboCtrl.batchCompraVentaCambios(
						sesionCreditos, iNumeroFichaCambio, numrec, caid, codcomp,  
						codsuc, montoCambio, bdTasaJde, fechaRecibo,  vaut.getId().getLogin(), 
						vaut.getId().getCodreg(), monedabase );
				
				
				aplicado = msgProceso.isEmpty();
				
				if(!aplicado){
					throw new Exception(msgProceso);
				}
			}
			
			//&& ================ Asientos por sobrantes.
			if ( CodeUtil.getFromSessionMap( "scr_SobrantePago") != null) {
				
				BigDecimal monto = new BigDecimal(String.valueOf(CodeUtil.getFromSessionMap("scr_SobrantePago")));

				boolean diferencialCambiario = 
						hFac.getId().getCodcomp().compareTo("E03") == 0 &&
						CodeUtil.getFromSessionMap("scr_SbrDfr") != null && 
						chkSobranteDifrl.isChecked() ;
						
				aplicado = batchExcedentesEnPago(  sesionCreditos, 
						hFac.getId().getMoneda(), hFac.getId().getCodcomp(), hFac.getId().getCodunineg(),
						numrec,	caid, codcomp, codsuc, mpSobrante, monto, bdTasaJde, vaut.getId().getLogin(),  
						vaut.getId().getCodreg(),  fechaRecibo, diferencialCambiario, monedabase );
				
				if(!aplicado){
					 msgProceso = "Error al crear recibos en JdEdward's por Ficha de Cambio";
					 throw new Exception(msgProceso);
				}
				
			}
			
			//&& =========== donaciones recibidas en el recibo
			if(aplicado && aplicadonacion ){
				DonacionesCtrl.msgProceso = "";
				DonacionesCtrl.caid = caid;
				DonacionesCtrl.codcomp = codcomp;
				DonacionesCtrl.numrec = numrec;
				DonacionesCtrl.tiporecibo = tiporec;
				
				aplicado = DonacionesCtrl.grabarDonacion(sesionCreditos, pagosConDonacion, vaut, hFac.getId().getCodcli() );
				//aplicado = DonacionesCtrl.grabarDonacion(pagosConDonacion, vaut, hFac.getId().getCodcli() );
				
				if( !aplicado ){
					msgProceso = DonacionesCtrl.msgProceso;
					if(msgProceso == null || msgProceso.isEmpty()) {
						msgProceso = "Error al grabar la donacion ";
					}else{  msgProceso = "Procesar Donacion: " + msgProceso; }
				}
				
			}
		} catch (Exception e) {
			LogCajaService.CreateLog("procesarReciboCr", "ERR", e.getMessage());
			e.printStackTrace();
			aplicado = false;
			msgProceso = "Recibo no aplicado, favor intente nuevamente";
		}finally{
			
			try
			{
				//++++++++++++++++++++++++++++++++++++++++
				//Agregado por LFonseca
				//----------------------------------------
				Vf55ca01 caja = (Vf55ca01) ((List) m.get("lstCajas")).get(0);
				int caid2 = caja.getId().getCaid();	
				int iCajero = caja.getId().getCacati();

				List lstFacturasSelected2 = (List) m.get("selectedFacsCredito");
				VerificarFacturaProceso verificarFac = new VerificarFacturaProceso();
				for(Object o : lstFacturasSelected2){
					Credhdr fd2 = (Credhdr)o;
					 
					String[] strResultado =  verificarFac.actualizarVerificarFactura(String.valueOf(caid2), 
															String.valueOf(fd2.getId().getCodcli()), 
															String.valueOf(fd2.getNofactura()), 
															fd2.getTipofactura(), 
															fd2.getPartida(), 
															String.valueOf(iCajero));
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				LogCajaService.CreateLog("procesarReciboCr", "ERR", e.getMessage());
			}

			dwProcesa.setWindowState("normal");
			dwImprime.setWindowState("hidden");
			//&& ======== commit / rollback las transacciones
			try {
				if(aplicado) {
					transCreditos.commit();
					BitacoraSecuenciaReciboService.actualizarSatisfactorioLogReciboNumerico(caid, numrec, codcomp,codsuc, "CR");
					
					dwRecibo.setWindowState("hidden");
				}
			} catch (Exception e) {
				LogCajaService.CreateLog("procesarReciboCr", "ERR", e.getMessage());
				msgProceso = "Error en confirmación de registro de valores:  Caja, intente nuevamente";
				aplicado = false;
				e.printStackTrace();
			}
			
			// Commit
			try {
				if(!aplicado) {
					if (transCreditos != null) {
						transCreditos.rollback();
					}
				}
			} catch (Exception e) {
				LogCajaService.CreateLog("procesarReciboCr", "ERR", e.getMessage());
				msgProceso = "Error en confirmación de registro de valores:  Caja, intente nuevamente";
				aplicado = false;
				e.printStackTrace();
			}
 
			if(aplicado){
				dwRecibo.setWindowState("hidden");
				msgProceso = "Recibo de caja aplicado correctamente " ;
				
				//&& ======= En caso de que exista ficha, actualizar recjde en Recibo
				if(bHayFicha){
					
					ReciboCtrl.setNoFichaRecibo(numrec, iNumeroFichaCambio,	caid, codcomp, codsuc, tiporec);
					
				}
				//&& ======= Impresion de recibos
				if(bHayPagoSocket){
					PosCtrl.imprimirVoucher(lstMetodosPago, "V", 
							dtComp.getId().getC4rp01d1(), 
							dtComp.getId().getC4prt());
				}
			
				//&& ============ Datos del recibo
				m.remove("dMontoAplicarCre");
				m.remove("lstSolicitud");
				m.remove("selectedFacsCredito");
				m.remove("lstPagos");
				m.remove("iNoFicha");
				m.remove("iNumRecCredito");
				m.remove("lstDatosTrack_Con");
				m.remove("lstCrFactsTMP");
				m.remove("selectedFacsCredito");
				
				//&& ============ Excedentes de pago
				m.remove("scr_SobrantePago");
				m.remove("scr_MpagoSobrante");
				m.remove("scr_SbrDfr");
				
				m.put("lstHfacturasCredito", new ArrayList<Credhdr>());
				gvHfacturasCredito.dataBind();
				gvHfacturasCredito.setPageIndex(0);
				
				//&& ============ Restaurar las tasas de cambio a la fecha actual.
				cargarTasasCambioAldia();
				
			}else{
				//&& ======== anulacion del Socket
				if(bAplicadoSockP){
					String msgValidaSockPos = PosCtrl.revertirPagosAplicados
							(lstMetodosPago, caid, codcomp, tiporec);
					msgProceso += msgValidaSockPos ;
				}
			}
			
			lblMensajeValidacion.setValue(msgProceso);
			
			if (aplicado) {
				CtrlCajas.imprimirRecibo(caid, numrec, codcomp, codsuc, tiporec, false ) ;
			}

			HibernateUtilPruebaCn.closeSession(sesionCreditos);
			m.remove("procesandoReciboValidacion");
			LogCajaService.CreateLog("FacCreditoDAO.procesarReciboCr - FIN", "JR8", "Fin ejecucion metodo");
		}
	}
	
/***************************************************************************/
	public void ProcesaRecibo(ActionEvent e){
		Divisas divisas = new Divisas();
		Connection cn = null;
		Session session = null;
		Transaction tx = null;
		boolean bInsertado = false, bHayFicha= false;
		MetodosPago[] metPago = null;
		int iNumrec = 0;
		List<Credhdr> lstFacturasSelected = null;
		Credhdr hFac = null;
		List lstPagoFicha = null;
		double dMontoFicha = 0.0;
		ReciboCtrl recCtrl = new ReciboCtrl();
		BigDecimal bdTasa = BigDecimal.ZERO,bdTasaJde, bdTasaPar;
		
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		String sMonedaBase = "";
		F55ca014[] f14 = null;
		try{
			if(m.get("cre_processRecibo")==null){
				m.put("cre_processRecibo","1");
				session = HibernateUtilPruebaCn.currentSession();
				bdTasaJde = obtenerTasaOficial();
				bdTasaPar = obtenerTasaParalela("COR");
				Vf55ca01 f55ca01 = (Vf55ca01)((List)m.get("lstCajas")).get(0);
	
				//&& ======== 	obtener datos de factura
		    	lstFacturasSelected = (List) m.get("selectedFacsCredito");
				hFac = (Credhdr)lstFacturasSelected.get(0);
				Vautoriz[] vautoriz = (Vautoriz[])m.get("sevAut");
				String sCajero = (String)m.get("sNombreEmpleado");	
				
				//&& ======== 	obtener companias x caja
				f14 = (F55ca014[])m.get("cont_f55ca014");
				sMonedaBase = cCtrl.sacarMonedaBase(f14, hFac.getId().getCodcomp());
				
				//&& ======== 	obtener conexion del datasource
				As400Connection as400connection = new As400Connection();
				cn = as400connection.getJNDIConnection("DSMCAJA2");
				cn.setAutoCommit(false);
				tx = session.beginTransaction();
				
				List<MetodosPago> lstMetodosPago = (List)m.get("lstPagos");
				
				bInsertado = true;
				if(bInsertado){
				
					bInsertado = insertarRecibo(session, tx,
									lstFacturasSelected,f55ca01,hFac,
									vautoriz[0], lstMetodosPago);
					
					if(bInsertado){
						//--- determinar si hay sobrante y restarlo del pago.
						if(m.get("scr_SobrantePago")!=null){
							double dMontorec  = 0;
							double dMontoNeto = 0;
							double dDiferencia= 0;
							double dMonto = 0,dEquiv=0;
							lstMetodosPago = null;
							MetodosPago mpSobrante = (MetodosPago)m.get("scr_MpagoSobrante");
							
							dMontoNeto = divisas.formatStringToDouble(txtMontoAplicar.getValue().toString());
							dMontorec =  divisas.formatStringToDouble(txtMontoRecibido.getValue().toString());
							dDiferencia = dMontorec - dMontoNeto;
							dDiferencia = divisas.roundDouble(dDiferencia);					
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
									
									dEquiv = divisas.roundDouble(dEquiv);
									dMonto = divisas.roundDouble(dMonto);
									mpago.setMonto(dMonto);
									mpago.setEquivalente(dEquiv);
									lstMetodosPago.set(i,mpago);
									break;
								}
							}
							m.put( "lstPagos", lstMetodosPago);
						}		
						lstMetodosPago = (List) m.get("lstPagos");
						String sTipoDoc = hFac.getTipofactura();
						int iCajaId = Integer.parseInt(m.get("sCajaId").toString());
						String sCodComp = hFac.getId().getCodcomp();
						double dTotalAplicar = divisas.formatStringToDouble(txtMontoAplicar.getValue().toString());
						// restar el cambio de los pagos en efectivo si existe
						// obtener cambios
						metPago = new MetodosPago[lstMetodosPago.size()];
						String sCambio1 = "", sCambio2 = "";
						double dCambio1 = 0.0, dCambio2 = 0.0, dNewMonto1 = 0.0, dNewMonto2 = 0.0;
						String sLblCambio1 = "", sLblCambio2 = "";
						boolean paso1 = false, paso2 = false;
						String[] sLblCambios1, sLblCambios2;
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
							bdTasa = obtenerTasaOficial();
							for (int i = 0; i < lstMetodosPago.size(); i++) {
								metPago[i] = (MetodosPago) lstMetodosPago.get(i);
								if (metPago[i].getMetodo().equals( MetodosPagoCtrl.EFECTIVO )&& !metPago[i].getMoneda().equals(sMonedaBase)&& !paso1) {				
									if (hFac.getMoneda().equals(sMonedaBase)) {
										if(dCambio2 > 0){//hay cambio domestico
											dNewMonto1 = divisas.roundDouble(metPago[i].getEquivalente() - dCambio2);
											dMontoFicha = divisas.roundDouble(dCambio2/metPago[i].getTasa().doubleValue());//metPago[i].getMonto() - divisas.roundDouble(dNewMonto1/ metPago[i].getTasa().doubleValue());
																				
											metPago[i].setMonto(divisas.roundDouble(dNewMonto1/ metPago[i].getTasa().doubleValue()));
											metPago[i].setEquivalente(dNewMonto1);
											
											if(dMontoFicha > 0){
												// Grabar ficha de CV
												MetodosPago metPagoFicha = new MetodosPago( MetodosPagoCtrl.EFECTIVO ,metPago[i].getMoneda(), dMontoFicha,metPago[i].getTasa(), dCambio2, "", "","", "","0",0);
												lstPagoFicha = new ArrayList();
												lstPagoFicha.add(metPagoFicha);
												bInsertado = insertarFichaCV(session, tx, f55ca01, hFac, vautoriz[0],dMontoFicha, iNumrec, sCajero,lstPagoFicha);
												if(!bInsertado){		
													lblMensajeValidacion.setValue("No se pudo registra la ficha de compra para este recibo ");
												}
												bHayFicha = true;
											}
										}
										if(dCambio1 > 0){
											dNewMonto1 = metPago[i].getMonto() - dCambio1;								
											metPago[i].setMonto(dNewMonto1);
											dNewMonto1 = dCambio1*metPago[i].getTasa().doubleValue();
											dNewMonto2 = metPago[i].getEquivalente();
											dNewMonto1 = divisas.roundDouble(dNewMonto2 - dNewMonto1);
											metPago[i].setEquivalente(dNewMonto1);
											
											lstMetodosPago.remove(i);
											lstMetodosPago.add(i, metPago[i]);
											paso1 = true;
										}
										paso1 = true;
									}else{
										if(dCambio2 > 0){
											
											dNewMonto1 = metPago[i].getMonto() - divisas.roundDouble((dCambio2/bdTasa.doubleValue()));
											dMontoFicha = divisas.roundDouble((dCambio2/bdTasa.doubleValue()));
											
											metPago[i].setMonto(dNewMonto1);
											metPago[i].setEquivalente(dNewMonto1);
											if(dMontoFicha > 0){
												// Grabar ficha de CV
												MetodosPago metPagoFicha = new MetodosPago( MetodosPagoCtrl.EFECTIVO ,metPago[i].getMoneda(), dMontoFicha,bdTasa, dCambio2, "", "","", "","0",0);
												lstPagoFicha = new ArrayList();
												lstPagoFicha.add(metPagoFicha);
												bInsertado = insertarFichaCV(session, tx, f55ca01, hFac, vautoriz[0],dMontoFicha, iNumrec, sCajero,lstPagoFicha);
												if(!bInsertado){
													lblMensajeValidacion.setValue("No se pudo registra la ficha de compra para este recibo ");
												}
												bHayFicha = true;
											}
										}
										if(dCambio1 > 0){
											dNewMonto1 = metPago[i].getMonto() - dCambio1;								
											metPago[i].setMonto(dNewMonto1);
											metPago[i].setEquivalente(dNewMonto1);
											
											lstMetodosPago.remove(i);
											lstMetodosPago.add(i, metPago[i]);
											paso1 = true;
										}
									}
									
								} else if (metPago[i].getMetodo().equals( MetodosPagoCtrl.EFECTIVO )&& metPago[i].getMoneda().equals(sLblCambio2)&& !paso2) {
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
								if (metPago[i].getMetodo().equals( MetodosPagoCtrl.EFECTIVO ) && metPago[i].getMoneda().equals(sLblCambio1) && !paso1) {
									if (sLblCambio1.equals(sMonedaBase)&& hFac.getMoneda().equals(sMonedaBase)) {// se saca el cambio en cor del pago en cor
										dNewMonto1 = metPago[i].getMonto() - dCambio1;
										metPago[i].setMonto(dNewMonto1);
										metPago[i].setEquivalente(dNewMonto1);
									} else if (sLblCambio1.equals(sMonedaBase) && !hFac.getMoneda().equals(sMonedaBase) && lstMetodosPago.size() > 1) {// se saca el cambio del pago en cor y se convierte a usd con tasa del metodo
										dNewMonto1 = metPago[i].getMonto() - dCambio1;
										metPago[i].setMonto(dNewMonto1);
										metPago[i].setEquivalente(divisas.roundDouble(dNewMonto1 / metPago[i].getTasa().doubleValue()));
									} else if (sLblCambio1.equals(sMonedaBase) && !hFac.getMoneda().equals(sMonedaBase) && lstMetodosPago.size() == 1) {// si pago toda la fac de usd en cor
										dNewMonto1 = metPago[i].getMonto() - dCambio1;
										metPago[i].setMonto(dNewMonto1);
										metPago[i].setEquivalente(divisas.roundDouble(dNewMonto1/ metPago[i].getTasa().doubleValue()));
									} else if (sLblCambio1.equals(sMonedaBase) && hFac.getMoneda().equals(sMonedaBase) && lstMetodosPago.size() == 1) {// el pago en usd cubre toda la venta
										dNewMonto1 = metPago[i].getEquivalente() - dCambio1;
										metPago[i].setMonto(divisas.roundDouble(dNewMonto1 / metPago[i].getTasa().doubleValue()));
										metPago[i].setEquivalente(dNewMonto1);
									}
									lstMetodosPago.remove(i);
									lstMetodosPago.add(i, metPago[i]);
									paso1 = true;
								} else if (metPago[i].getMetodo().equals( MetodosPagoCtrl.EFECTIVO ) && sLblCambio1.equals(sMonedaBase) && !metPago[i].getMoneda().equals(sMonedaBase) && !paso1) {
									if (hFac.getMoneda().equals(sMonedaBase) && lstMetodosPago.size() == 1) {// el pago en usd cubre toda la venta
										dNewMonto1 = metPago[i].getEquivalente() - dCambio1;
										dMontoFicha = metPago[i].getMonto() - divisas.roundDouble(dNewMonto1/ metPago[i].getTasa().doubleValue());
			
										metPago[i].setMonto(divisas.roundDouble(dNewMonto1/ metPago[i].getTasa().doubleValue()));
										metPago[i].setEquivalente(dNewMonto1);
										if(dMontoFicha > 0){
											// Grabar ficha de CV
											MetodosPago metPagoFicha = new MetodosPago( MetodosPagoCtrl.EFECTIVO ,metPago[i].getMoneda(), dMontoFicha,metPago[i].getTasa(), dCambio1, "", "","", "","0",0);
											lstPagoFicha = new ArrayList();
											lstPagoFicha.add(metPagoFicha);
											bInsertado = insertarFichaCV(session, tx, f55ca01, hFac, vautoriz[0],dMontoFicha, iNumrec, sCajero,lstPagoFicha);
											if(!bInsertado){
												lblMensajeValidacion.setValue("No se pudo registra la ficha de compra para este recibo ");
											}
											bHayFicha = true;
										}
									}
								}
							}
						}
					}	
					//registrar recibo en F0311
					if (bInsertado){
						
						int iNumRecCredito = Integer.parseInt(m.get("iNumRecCredito").toString());
						bInsertado = grabarCredito(session, tx, cn, lstMetodosPago, f55ca01, lstFacturasSelected, iNumRecCredito, bHayFicha, lstPagoFicha, bdTasaJde, bdTasaPar, vautoriz[0], sMonedaBase, new Date());
	
						//&& =============== Guardar los asientos de diario por sobrantes ========== &&//
						if(bInsertado && m.get("scr_SobrantePago")!=null){
							MetodosPago mpago = null;
							if(m.get("scr_MpagoSobrante")==null){
								lblMensajeValidacion.setValue("Error de sistema al obtener los datos del sobrante de pago para el recibo ");
								bInsertado = false;
							}else{
								
								String sCodunineg = ((Credhdr)lstFacturasSelected.get(0)).getId().getCodunineg();
								String sCodcomp  = ((Credhdr)lstFacturasSelected.get(0)).getId().getCodcomp();
								double dSobrante = divisas.formatStringToDouble(m.get("scr_SobrantePago").toString());
								mpago = (MetodosPago)m.get("scr_MpagoSobrante");
								bInsertado = registarAsientosxSobrantes(session, tx, cn, mpago,  vautoriz[0], f55ca01,
																		iNumRecCredito, sCodcomp,sCodunineg,
																		dSobrante,sMonedaBase,lstFacturasSelected.get(0));
								if(!bInsertado){
									lblMensajeValidacion.setValue("No se ha podido registrar el sobrante de pago en JDE");
								}
							}
						}
						//bInsertado = false;//quitar para prueba
						if (bInsertado){	
							tx.commit();
							cn.commit();
										    	
							if(bHayFicha){
								//actualizar el recibo con el numero de ficha
								recCtrl.actualizarNoFicha(cn, iNumRecCredito, Integer.parseInt(m.get("iNoFicha").toString()), f55ca01.getId().getCaid(),hFac.getId().getCodcomp() , f55ca01.getId().getCaco(), "CR");
								cn.commit();
							}
							
							//&& ================  Aplicar el pago de socket pos.
							if((f55ca01.getId().getCasktpos()+"").equals("Y")){
								List lstDatos = (List)m.get("lstDatosTrack_Con");
								bInsertado = aplicarPagoSocketPos(lstMetodosPago,lstDatos);
							}
							if(bInsertado){
								imprimirVoucher(lstMetodosPago,hFac.getId().getCodcomp(), "V", f14);
								recCtrl.actualizarReferenciasRecibo(iNumRecCredito, f55ca01.getId().getCaid(),
												hFac.getId().getCodcomp(),f55ca01.getId().getCaco(),
												"CR",lstMetodosPago);
							}else{
								int iNoficha = (m.get("iNoFicha") == null )? 0:
												Integer.parseInt(
														String.valueOf(m.get("iNoFicha")));
								
								bInsertado = anularRecibo(iNumRecCredito, 
													f55ca01.getId().getCaid(), 
													f55ca01.getId().getCaco(),
													hFac.getId().getCodcomp(), 
													vautoriz[0].getId().getCoduser(),
													sMonedaBase,
													vautoriz,
													iNoficha, hFac.getId().getCodcli() );
								
								sMensaje = lblMensajeValidacion.getValue().toString();
								
								m.remove("cre_processRecibo");
								dwCargando.setWindowState("hidden");
								dwImprime.setWindowState("hidden");
								dwProcesa.setWindowState("normal");
								return;
							}
							//================== fin del pago con socket pos..
						}
						else{
							dwProcesa.setWindowState("normal");
							tx.rollback();
							cn.rollback();
						}	
							
						//&& ===== Imprimir el recibo.
						if(bInsertado){	

							sMensaje = "La operacion fue exitosa!; Realice la búsqueda de facturas del cliente";
							m.put("lstHfacturasCredito",new ArrayList());
							m.remove("cre_processRecibo");
							
							BigDecimal bdNumrec = new BigDecimal(iNumRecCredito);
							getP55recibo().setIDCAJA(new BigDecimal(f55ca01.getId().getCaid()));
							getP55recibo().setNORECIBO(bdNumrec);
							getP55recibo().setIDEMPRESA(hFac.getId().getCodcomp().trim());
							getP55recibo().setIDSUCURSAL(f55ca01.getId().getCaco().trim());
							getP55recibo().setTIPORECIBO("CR");
							getP55recibo().setRESULTADO("");
							getP55recibo().setCOMANDO("");
							getP55recibo().invoke();
							getP55recibo().getRESULTADO();
							 
						}	
					}else{
						dwProcesa.setWindowState("normal");				
						tx.rollback();
						cn.rollback();
						//cancelar transacciones con socket POS si hubieron 
						//anularPagosSP(lstMetodosPago);
						m.remove("lstDatosTrack_Con");
					}
				
				
				}else{
					dwProcesa.setWindowState("normal");
					tx.rollback();
					cn.rollback();
					//cancelar transacciones con socket POS si hubieron 
					//anularPagosSP(lstMetodosPago);
					m.remove("lstDatosTrack_Con");					
				}
				
				dwImprime.setWindowState("hidden");
				dwRecibo.setWindowState("hidden");
				gvHfacturasCredito.dataBind();
				List lstSelectedFacs = new ArrayList();
				m.put("selectedFacsCredito", lstSelectedFacs);
				m.remove("dMontoAplicarCre");
				m.remove("lstSolicitud");
				m.remove("selectedFacsCredito");
				m.remove("lstPagos");
				m.remove("cre_processRecibo");
				
				//&& ==== Excedentes de paogo
				m.remove("scr_SobrantePago");
				m.remove("scr_MpagoSobrante");
				m.remove("scr_SbrDfr");
				
				//------ Restaurar las tasas de cambio a la fecha actual.
				cargarTasasCambioAldia();
			}
		}catch(Exception ex){
			ex.printStackTrace();
			try{
				m.remove("cre_processRecibo");
				tx.rollback();
				cn.rollback();
				dwProcesa.setWindowState("normal");
				lblMensajeValidacion.setValue("No se pudo realizar la operación " + ex);
				dwImprime.setWindowState("hidden");
				dwRecibo.setWindowState("hidden");
				gvHfacturasCredito.dataBind();
				List lstSelectedFacs = new ArrayList();
				m.put("selectedFacsCredito", lstSelectedFacs);
				m.remove("dMontoAplicarCre");
				m.remove("lstSolicitud");
				m.remove("selectedFacsCredito");
				m.remove("lstPagos");	
				//cancelar transacciones con socket POS si hubieron 
				//anularPagosSP(lstMetodosPago);
				m.remove("lstDatosTrack_Con");
			}catch(Exception ex2){
				ex.printStackTrace();
				lblMensajeValidacion.setValue("Error: no se pudo realizar la operacion!!!");
				ex2.printStackTrace();
			}
			ex.printStackTrace();
		}finally{
			try{
				dwCargando.setWindowState("hidden");
				cn.close();
				session.close();			
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		
	}
/**************************************************************************************************************/

/***********************************************************************************************************/
	public boolean insertarRecibo(Session session, Transaction transaction, 
						List lstFacturasSelected, 
						Vf55ca01 f55ca01,Credhdr hFac,Vautoriz vAut,
						List<MetodosPago> lstMetodosPago2){
		
		ReciboCtrl creCtrl = new ReciboCtrl();
		boolean bHecho = false ;
		int iNumRec = 0, iCodCli = 0, iNumRecm = 0, iCajaId = 0;
		String sCodComp = null,sConcepto = null, sNomCli = null, sCajero = null;
		Date dFecha = new Date(), dHora = new Date();
		double dMontoRec = 0, dMontoAplicar = 0, dCambio = 0;
		Divisas divisas = new Divisas();
		CtrlSolicitud solCtrl = new CtrlSolicitud();
		
		try{
			
			Vautoriz[] vautoriz = (Vautoriz[])m.get("sevAut");

			iCajaId = f55ca01.getId().getCaid();
			sCodComp = hFac.getId().getCodcomp();
			
			iNumRec = com.casapellas.controles.tmp.ReciboCtrl.generarNumeroRecibo(iCajaId, sCodComp);
			
			if (iNumRec == 0) {
				strMensajeValidacion = "No se encontro número de recibo " +
						"configurado para la compañia: " + sCodComp ; 
				return false;
			}
			BitacoraSecuenciaReciboService.insertarLogReciboNumerico(iCajaId,iNumRec,sCodComp,CodeUtil.pad(f55ca01.getId().getCaco(), 5, "0"),"CR");
			
			dMontoAplicar = divisas.formatStringToDouble(txtMontoAplicar.getValue().toString());
			dMontoRec = divisas.formatStringToDouble(txtMontoRecibido.getValue().toString());
			if (!txtCambio.getValue().toString().trim().equals("")){
				dCambio = divisas.formatStringToDouble(txtCambio.getValue().toString());
			}
			sConcepto = txtConcepto.getValue().toString();
			iCodCli = hFac.getId().getCodcli();
			sNomCli = hFac.getNomcli();
			sCajero = (String)m.get("sNombreEmpleado");		

			int iNumSol = 0;
			List lstSolicitud = (List)m.get("lstSolicitud");
			Solicitud sol = null;
			int[] iNumFac = new int[lstFacturasSelected.size()];
			double[] dMonto = new double[lstFacturasSelected.size()];
			String[] sPartida = new String[lstFacturasSelected.size()];
			String[] sCodunineg = new String[lstFacturasSelected.size()];
			String[] sCodsuc = new String[lstFacturasSelected.size()];
			String[] sTipoDoc = new String[lstFacturasSelected.size()];
			
			if(!cmbTiporecibo.getValue().toString().equals("AUTOMATICO")){
				iNumRecm = Integer.parseInt(txtNumRec.getValue().toString().trim());
			}
			
			//---- Si hubo cambio de tasa, leer el motivo.
			String sMotivoTc = (m.get("fcr_MotivoCambioTasa") != null) ? m.get("fcr_MotivoCambioTasa").toString() : " ";
			
			
			//&& ================ validacion para que la sumatoria de pagos no sea distinta a la del monto recibido
			BigDecimal sumMontoEquiv = CodeUtil.sumPropertyValueFromEntityList(lstMetodosPago2, "equivalente", false);
			BigDecimal montoRecibido = new BigDecimal( Double.toString(dMontoRec) ) ;
		
			if( 	montoRecibido.compareTo(sumMontoEquiv) != 0 &&  
					montoRecibido.subtract(sumMontoEquiv).abs().compareTo( new BigDecimal("0.01") ) == 1 ) {
				
				strMensajeValidacion = "La sumatoria de pagos no coincide con el total del monto recibido, favor intentar nuevamente." ;
				lblMensajeValidacion.setValue(strMensajeValidacion);
				
				String body = 
					"<br>Pago de Recibo de Credito, Suma de equivalentes distinta a monto recibido" +
					"<br>Monto Aplicado: " + dMontoAplicar +
					"<br>Monto recibido: " + montoRecibido +
					"<br>Monto Pagos: " + sumMontoEquiv +
					"<br>caja:"+iCajaId+", compania"+sCodComp+", recibo: "+ iNumRec +
					"<br>cliente: " + iCodCli+ " " +sNomCli ;
					
					String subject = "Pago de Recibo de Credito, Suma de equivalentes distinta a monto recibido" ;
					SendMailsCtrl.sendSimpleMail(body, null, subject);
				
				//return bHecho = false;
				
			}
					
			//guardar el header del recibo
			bHecho = creCtrl.registrarRecibo(session, transaction, iNumRec,
					iNumRecm, sCodComp, dMontoAplicar, dMontoRec, dCambio,
					sConcepto, dFecha, dHora, iCodCli, sNomCli, sCajero,
					iCajaId, f55ca01.getId().getCaco(), 
					vautoriz[0].getId().getCoduser(), valoresJDEInsCredito[0], 0, "", 
					0, dFecha, hFac.getId().getCodunineg().trim(), 
					sMotivoTc, hFac.getId().getMoneda() );
				
			//guardar detalle de recibo
			if(bHecho){
				
				lstMetodosPago2 = ponerCodigoBanco(lstMetodosPago2);

				//&& =============== Determinar si el pago trabaja bajo preconciliacion y conservar su referencia original
				com.casapellas.controles.BancoCtrl.datosPreconciliacion(lstMetodosPago2, iCajaId, sCodComp) ;
				
				bHecho = creCtrl.registrarDetalleRecibo(session, transaction, 
						iNumRec, iNumRecm, sCodComp, lstMetodosPago2,
						iCajaId,f55ca01.getId().getCaco(),"CR");		
				
				if(bHecho){
					//leer facturas seleccionadas
					for (int h = 0;h  < lstFacturasSelected.size();h++){
						if(((Credhdr)lstFacturasSelected.get(h)).getMontoAplicar().doubleValue() > 0){
							iNumFac[h] = ((Credhdr)lstFacturasSelected.get(h)).getNofactura();
							dMonto[h] = ((Credhdr)lstFacturasSelected.get(h)).getMontoAplicar().doubleValue();
							sPartida[h] = ((Credhdr)lstFacturasSelected.get(h)).getPartida();
							sCodunineg[h] = ((Credhdr)lstFacturasSelected.get(h)).getId().getCodunineg();
							sCodsuc[h] = ((Credhdr)lstFacturasSelected.get(h)).getId().getCodsuc().substring(3, 5);
							sTipoDoc[h] =  ((Credhdr)lstFacturasSelected.get(h)).getTipofactura();
						}else{
							iNumFac[h] = 0;
						}
					}
					 
					bHecho = true;
						
					if(bHecho){
						
						//guardar solicitudes
						if(bHecho){
							if (lstSolicitud != null && !lstSolicitud.isEmpty()){
								for(int i = 0; i < lstSolicitud.size();i++){
									sol = (Solicitud)lstSolicitud.get(i);
									iNumSol = solCtrl.getNumeroSolicitud();
									
									bHecho = solCtrl.registrarSolicitud(null,null, iNumSol, iNumRec, valoresJDEInsCredito[0], iCajaId, 
											sCodComp,f55ca01.getId().getCaco(), sol.getId().getReferencia(),sol.getAutoriza(),
											dFecha,sol.getObs(),sol.getMpago(),sol.getMonto(),sol.getMoneda());
									if(!bHecho){
										strMensajeValidacion = "No se pudo registrar la solicitud: " + iNumSol;
										lblMensajeValidacion.setValue(strMensajeValidacion);
										return false;
									}
									
								}
							}
						}else{//validacion de actualizacion de numero de recibo de caja
							strMensajeValidacion = "No se pudo actualizar el numero: "+iNumRec+" de recibo de caja!!!" ;
							lblMensajeValidacion.setValue(strMensajeValidacion);
							return false;
						}
					}else {
							strMensajeValidacion = "No se pudo registrar el enlace con las facturas" ;
							lblMensajeValidacion.setValue(strMensajeValidacion);
							return false;
						}
					}else{//validacion de detalle de recibo
						strMensajeValidacion = "No se pudo registrar el detalle del recibo!!!" ;
						lblMensajeValidacion.setValue(strMensajeValidacion);
						return false;
					}
				}else{//validacion de encabezado de recibo
					strMensajeValidacion = "No se pudo registrar el encabezado del recibo";
					lblMensajeValidacion.setValue(strMensajeValidacion);
					return false;
				}//validacion de enca bezado de recibo
				
			//
			//registrar Cambio 
			if(bHecho){
					String sLblCambio1 = "", sLblCambio2 = "",sCambio1 = "", sCambio2 = "";
					String[] sLblCambios1, sLblCambios2;
					sLblCambio1 = lblCambio.getValue().toString();
					sLblCambio2 = lblCambioDomestico.getValue().toString();
					BigDecimal bdTasa = BigDecimal.ZERO;
					if(txtCambio.getValue().toString().trim().equals("")){
						sCambio1 = txtCambioForaneo.getValue().toString();
						sCambio2 = txtCambioDomestico.getValue().toString();
						sLblCambios1 = sLblCambio1.trim().split(" ");
						sLblCambios2 = sLblCambio2.trim().split(" ");
						//
						sLblCambio1 = sLblCambios1[sLblCambios1.length-1].substring(0,3);
						sLblCambio2 = sLblCambios2[sLblCambios1.length-1].substring(0,3);
						//
						if (m.get("bdTasa") != null){
							bdTasa = (BigDecimal)m.get("bdTasa");
						}
						bHecho = creCtrl.registrarCambio(session, transaction, iNumRec, sCodComp, sLblCambio1, divisas.formatStringToDouble(sCambio1),iCajaId,f55ca01.getId().getCaco(),bdTasa,valoresJDEInsCredito[0]);
						if(bHecho){
							bHecho = creCtrl.registrarCambio(session, transaction, iNumRec, sCodComp, sLblCambio2, divisas.formatStringToDouble(sCambio2),iCajaId,f55ca01.getId().getCaco(),bdTasa,valoresJDEInsCredito[0]);
							if(!bHecho){
								strMensajeValidacion = "No se pudo registrar el cambio " + sLblCambio2 ;
								lblMensajeValidacion.setValue(strMensajeValidacion);
								return false;
							}
						}else{
							strMensajeValidacion = "No se pudo registrar el cambio " + sLblCambio1 ;
							lblMensajeValidacion.setValue(strMensajeValidacion);
							return false;
						}
					}else{
						sCambio1 = txtCambio.getValue().toString();
						sLblCambios1 = sLblCambio1.trim().split(" ");
						//
						sLblCambio1 = sLblCambios1[sLblCambios1.length-1].substring(0,3);
						//
						if (m.get("bdTasa") != null){
							bdTasa = (BigDecimal)m.get("bdTasa");
						}
						bHecho = creCtrl.registrarCambio(session, transaction, iNumRec, sCodComp, sLblCambio1, divisas.formatStringToDouble(sCambio1),iCajaId,f55ca01.getId().getCaco(),bdTasa,valoresJDEInsCredito[0]);
						if(!bHecho){
							strMensajeValidacion = "No se pudo registrar el cambio " + sLblCambio1;
							lblMensajeValidacion.setValue(strMensajeValidacion );
							return false;
						}
					}
					
					if(bHecho){
						m.put("iNumRecCredito", iNumRec);
						return bHecho;
					}else return false;
			}else return false;
		
		}catch(Exception ex){
			strMensajeValidacion = "Recibo no aplicado, error en sistema"  ;
			ex.printStackTrace();
			return false;
		}
	}
/**********************************************************************************************/
/************MOSTRAR DETALLE DE FACTURA DE CREDITO EN VENTANA DE RECIBO DE CREDITO****************/
	public void mostrarDetalleEnReciboCredito(ActionEvent e){
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		FacturaCreditoCtrl facCred = new FacturaCreditoCtrl();
		String sMonedaBase = "";
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		try {
			RowItem ri = (RowItem) e.getComponent().getParent().getParent();
			Credhdr hFacActual=  (Credhdr)gvHfacturasCredito.getDataRow(ri);
			
			List lstFacsActuales = (List) m.get("selectedFacsCredito");
			List lstMonedasDetalle = new ArrayList();
			Credhdr hFac = new Credhdr();
			
			//obtener companias x caja
			sMonedaBase = cCtrl.sacarMonedaBase((F55ca014[])m.get("cont_f55ca014"), hFacActual.getId().getCodcomp());
			
			for (int i = 0; i < lstFacsActuales.size(); i++) {
				hFac = (Credhdr) lstFacsActuales.get(i);
				if (hFac.getNofactura() == hFacActual.getNofactura() && hFac.getTipofactura().equals(hFacActual.getTipofactura()) 
					&& hFac.getPartida().equals(hFacActual.getPartida()) && hFac.getId().getCodsuc().equals(hFacActual.getId().getCodsuc()) && hFac.getId().getCodunineg().equals(hFacActual.getId().getCodunineg())) {
					m.put("hFac", hFac);
					txtFechaFactura.setValue(hFac.getId().getFecha());
					txtNofactura.setValue(hFac.getId().getNofactura() + "");
					txtCliente.setValue(hFac.getId().getNomcli() + " (nombre)");
					txtCodigoCliente.setValue(hFac.getId().getCodcli() + " (código)");
					txtTasaDetalle.setValue(hFac.getId().getTasa());
					txtUnineg.setValue(hFac.getId().getCodunineg() + " " + hFac.getId().getUnineg());
					txtTipoFactura.setValue(hFac.getId().getTipofactura());
					txtCompania.setValue(hFac.getId().getNomcomp());
					
					txtFechaVenc.setValue(hFac.getId().getFechavenc());					
					txtFechaVenDecto.setValue(hFac.getId().getFechavenc());					
					
					if(hFac.getMoneda().equals(sMonedaBase)){
						
						lstMonedasDetalle.add(new SelectItem(sMonedaBase,sMonedaBase));
					}else{
						
						lstMonedasDetalle.add(new SelectItem(hFac.getMoneda(),hFac.getMoneda()));
						lstMonedasDetalle.add(new SelectItem(sMonedaBase,sMonedaBase));
					}
					m.put("lstMonedasDetalle", lstMonedasDetalle);
					cmbMonedaDetalleCredito.dataBind();
					//buscar detalle 
					List lstDfacturasCredito = facCred.getDetalleFacturaCredito(hFac.getId().getNofactura(), hFac.getId().getTipofactura(),hFac.getId().getCodsuc(),hFac.getId().getCodunineg());
					m.put("lstDfacturasCredito", lstDfacturasCredito);
					gvDfacturasCredito.dataBind();
					break;
				}
			}
			dgwDetalleCredito.setWindowState("normal");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public UIInput getCmbBusquedaCredito() {
		return cmbBusquedaCredito;
	}

	public void setCmbBusquedaCredito(UIInput cmbBusquedaCredito) {
		this.cmbBusquedaCredito = cmbBusquedaCredito;
	}

	public List getLstBusquedaCredito() {
		if(lstBusquedaCredito == null){
			lstBusquedaCredito = new ArrayList();	
			lstBusquedaCredito.add(new SelectItem("1","Nombre Cliente","Búsqueda por nombre de cliente"));
			lstBusquedaCredito.add(new SelectItem("2","Código Cliente","Búsqueda por código de cliente"));
			lstBusquedaCredito.add(new SelectItem("3","No. Factura","Búsqueda por número de factura"));
		}
		return lstBusquedaCredito;
	}

	public void setLstBusquedaCredito(List lstBusquedaCredito) {
		this.lstBusquedaCredito = lstBusquedaCredito;
	}
	
	public HtmlGridView getGvHfacturasCredito() {
		return gvHfacturasCredito;
	}

	public void setGvHfacturasCredito(HtmlGridView gvHfacturasCredito) {
		this.gvHfacturasCredito = gvHfacturasCredito;
	}

	public List getLstHfacturasCredito() {
		try{
			lstHfacturasCredito = new ArrayList();
			lstHfacturasCredito = (List)m.get("lstHfacturasCredito");	
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return lstHfacturasCredito;
	}

	public void setLstHfacturasCredito(List lstHfacturasCredito) {
		this.lstHfacturasCredito = lstHfacturasCredito;
	}
	public UIInput getTxtParametroCredito() {
		return txtParametroCredito;
	}
	public void setTxtParametroCredito(UIInput txtParametroCredito) {
		this.txtParametroCredito = txtParametroCredito;
	}
	public DialogWindow getDgwDetalleCredito() {
		return dgwDetalleCredito;
	}
	public void setDgwDetalleCredito(DialogWindow dgwDetalleCredito) {
		this.dgwDetalleCredito = dgwDetalleCredito;
	}
	
	
	public HtmlOutputText getTxtPendiente() {
		return txtPendiente;
	}
	public void setTxtPendiente(HtmlOutputText txtPendiente) {
		this.txtPendiente = txtPendiente;
	}

	public HtmlDropDownList getCmbMonedaDetalleCredito() {
		return cmbMonedaDetalleCredito;
	}
	public void setCmbMonedaDetalleCredito(HtmlDropDownList cmbMonedaDetalleCredito) {
		this.cmbMonedaDetalleCredito = cmbMonedaDetalleCredito;
	}
	
	public GridView getGvfacturasSelecCredito() {
		return gvfacturasSelecCredito;
	}
	public void setGvfacturasSelecCredito(GridView gvfacturasSelecCredito) {
		this.gvfacturasSelecCredito = gvfacturasSelecCredito;
	}
	public List getSelectedFacsCredito() {
		//Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		selectedFacsCredito = (List) m.get("selectedFacsCredito");						
		return selectedFacsCredito;
	}
	public void setSelectedFacsCredito(List selectedFacsCredito) {
		this.selectedFacsCredito = selectedFacsCredito;
	}
	public DialogWindow getDgwMensajeDetalleCredito() {
		return dgwMensajeDetalleCredito;
	}
	public void setDgwMensajeDetalleCredito(DialogWindow dgwMensajeDetalleCredito) {
		this.dgwMensajeDetalleCredito = dgwMensajeDetalleCredito;
	}
	public HtmlOutputText getLblMontoAplicarCredito() {
		return lblMontoAplicarCredito;
	}
	public void setLblMontoAplicarCredito(HtmlOutputText lblMontoAplicarCredito) {
		this.lblMontoAplicarCredito = lblMontoAplicarCredito;
	}
	
	public HtmlDropDownList getCmbMonedaDetalle() {
		return cmbMonedaDetalle;
	}
	public void setCmbMonedaDetalle(HtmlDropDownList cmbMonedaDetalle) {
		this.cmbMonedaDetalle = cmbMonedaDetalle;
	}
	public HtmlOutputText getLblMontoAplicar() {
		return lblMontoAplicar;
	}
	public void setLblMontoAplicar(HtmlOutputText lblMontoAplicar) {
		this.lblMontoAplicar = lblMontoAplicar;
	}
	public HtmlOutputText getLblTasaDetalle() {
		return lblTasaDetalle;
	}
	public void setLblTasaDetalle(HtmlOutputText lblTasaDetalle) {
		this.lblTasaDetalle = lblTasaDetalle;
	}
	public List getLstMonedasDetalle() {
		try{
			lstMonedasDetalle = (List)m.get("lstMonedasDetalle");
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return lstMonedasDetalle;
	}
	public void setLstMonedasDetalle(List lstMonedasDetalle) {
		this.lstMonedasDetalle = lstMonedasDetalle;
	}
	public String getSMensaje() {
		return sMensaje;
	}
	public void setSMensaje(String mensaje) {
		sMensaje = mensaje;
	}
	
	public HtmlOutputText getTxtMensaje() {
		return txtMensaje;
	}
	public void setTxtMensaje(HtmlOutputText txtMensaje) {
		this.txtMensaje = txtMensaje;
	}
	public HtmlOutputText getTxtTasaDetalle() {
		return txtTasaDetalle;
	}
	public void setTxtTasaDetalle(HtmlOutputText txtTasaDetalle) {
		this.txtTasaDetalle = txtTasaDetalle;
	}
	public HtmlOutputText getLblValidaFactura() {
		return lblValidaFactura;
	}
	public void setLblValidaFactura(HtmlOutputText lblValidaFactura) {
		this.lblValidaFactura = lblValidaFactura;
	}
	public DialogWindow getDwValidacionFactura() {
		return dwValidacionFactura;
	}
	public void setDwValidacionFactura(DialogWindow dwValidacionFactura) {
		this.dwValidacionFactura = dwValidacionFactura;
	}
	public HtmlOutputText getTxtTipoFactura() {
		return txtTipoFactura;
	}
	public void setTxtTipoFactura(HtmlOutputText txtTipoFactura) {
		this.txtTipoFactura = txtTipoFactura;
	}
	public HtmlOutputText getTxtCompania() {
		return txtCompania;
	}
	public void setTxtCompania(HtmlOutputText txtCompania) {
		this.txtCompania = txtCompania;
	}
	public HtmlOutputText getTxtFechalm() {
		return txtFechalm;
	}
	public void setTxtFechalm(HtmlOutputText txtFechalm) {
		this.txtFechalm = txtFechalm;
	}
	public HtmlOutputText getTxtFechaVenc() {
		return txtFechaVenc;
	}
	public void setTxtFechaVenc(HtmlOutputText txtFechaVenc) {
		this.txtFechaVenc = txtFechaVenc;
	}
	public HtmlOutputText getTxtFechaImp() {
		return txtFechaImp;
	}
	public void setTxtFechaImp(HtmlOutputText txtFechaImp) {
		this.txtFechaImp = txtFechaImp;
	}
	public HtmlOutputText getTxtCliente() {
		return txtCliente;
	}
	public void setTxtCliente(HtmlOutputText txtCliente) {
		this.txtCliente = txtCliente;
	}
	public HtmlOutputText getTxtCodigoCliente() {
		return txtCodigoCliente;
	}
	public void setTxtCodigoCliente(HtmlOutputText txtCodigoCliente) {
		this.txtCodigoCliente = txtCodigoCliente;
	}
	public HtmlOutputText getTxtFechaFactura() {
		return txtFechaFactura;
	}
	public void setTxtFechaFactura(HtmlOutputText txtFechaFactura) {
		this.txtFechaFactura = txtFechaFactura;
	}
	public HtmlOutputText getTxtIva() {
		return txtIva;
	}
	public void setTxtIva(HtmlOutputText txtIva) {
		this.txtIva = txtIva;
	}
	public HtmlOutputText getTxtNofactura() {
		return txtNofactura;
	}
	public void setTxtNofactura(HtmlOutputText txtNofactura) {
		this.txtNofactura = txtNofactura;
	}
	public HtmlOutputText getTxtSubtotal() {
		return txtSubtotal;
	}
	public void setTxtSubtotal(HtmlOutputText txtSubtotal) {
		this.txtSubtotal = txtSubtotal;
	}
	public HtmlOutputText getTxtTotal() {
		return txtTotal;
	}
	public void setTxtTotal(HtmlOutputText txtTotal) {
		this.txtTotal = txtTotal;
	}
	public HtmlOutputText getTxtUnineg() {
		return txtUnineg;
	}
	public void setTxtUnineg(HtmlOutputText txtUnineg) {
		this.txtUnineg = txtUnineg;
	}
	public HtmlOutputText getTxtDescuento() {
		return txtDescuento;
	}
	public void setTxtDescuento(HtmlOutputText txtDescuento) {
		this.txtDescuento = txtDescuento;
	}
	public HtmlOutputText getTxtDescuentoAplicado() {
		return txtDescuentoAplicado;
	}
	public void setTxtDescuentoAplicado(HtmlOutputText txtDescuentoAplicado) {
		this.txtDescuentoAplicado = txtDescuentoAplicado;
	}
	public HtmlOutputText getTxtCompens() {
		return txtCompens;
	}
	public void setTxtCompens(HtmlOutputText txtCompens) {
		this.txtCompens = txtCompens;
	}
	public HtmlOutputText getTxtCondicion() {
		return txtCondicion;
	}
	public void setTxtCondicion(HtmlOutputText txtCondicion) {
		this.txtCondicion = txtCondicion;
	}
	public HtmlOutputText getTxtFechaBatch() {
		return txtFechaBatch;
	}
	public void setTxtFechaBatch(HtmlOutputText txtFechaBatch) {
		this.txtFechaBatch = txtFechaBatch;
	}
	public HtmlOutputText getTxtFechaVenDecto() {
		return txtFechaVenDecto;
	}
	public void setTxtFechaVenDecto(HtmlOutputText txtFechaVenDecto) {
		this.txtFechaVenDecto = txtFechaVenDecto;
	}
	public HtmlOutputText getTxtNoBatch() {
		return txtNoBatch;
	}
	public void setTxtNoBatch(HtmlOutputText txtNoBatch) {
		this.txtNoBatch = txtNoBatch;
	}
	public HtmlOutputText getTxtNoOrden() {
		return txtNoOrden;
	}
	public void setTxtNoOrden(HtmlOutputText txtNoOrden) {
		this.txtNoOrden = txtNoOrden;
	}
	public HtmlOutputText getTxtObservaciones() {
		return txtObservaciones;
	}
	public void setTxtObservaciones(HtmlOutputText txtObservaciones) {
		this.txtObservaciones = txtObservaciones;
	}
	public HtmlOutputText getTxtReferenciaFactura() {
		return txtReferenciaFactura;
	}
	public void setTxtReferenciaFactura(HtmlOutputText txtReferenciaFactura) {
		this.txtReferenciaFactura = txtReferenciaFactura;
	}
	public HtmlOutputText getTxtTipoOrden() {
		return txtTipoOrden;
	}
	public void setTxtTipoOrden(HtmlOutputText txtTipoOrden) {
		this.txtTipoOrden = txtTipoOrden;
	}
	/*******************************************/
	public List getLstFiltroMonedas() {
		try{
			if (m.get("fmonCre") == null){
				//leer companias configuradas en caja
				List lstCajas = (List)m.get("lstCajas");
				CompaniaCtrl compCtrl = new CompaniaCtrl();
				F55ca014[] f55ca014 = null;
				f55ca014 = compCtrl.obtenerCompaniasxCaja(((Vf55ca01)lstCajas.get(0)).getId().getCaid());
				//leer monedas de companias configuradas
				MonedaCtrl monCtrl =  new MonedaCtrl();
				String[] sMonedas = monCtrl.obtenerMonedasxCaja_Companias(((Vf55ca01)lstCajas.get(0)).getId().getCaid(), f55ca014);
				lstFiltroMonedas = new ArrayList();
				lstFiltroMonedas.add(new SelectItem("01","Todas"));
				for(int i = 0; i < sMonedas.length;i++){
					lstFiltroMonedas.add(new SelectItem(sMonedas[i],sMonedas[i]));
				}
				m.put("fmonCre", "fm");
				m.put("lstFiltroMonedas", lstFiltroMonedas);
			}else{
				lstFiltroMonedas = (List) m.get("lstFiltroMonedas");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return lstFiltroMonedas;
	}
	/*******************************************/
	public void setLstFiltroMonedas(List lstFiltroMonedas) {
		this.lstFiltroMonedas = lstFiltroMonedas;
	}
	public HtmlDropDownList getCmbFiltroMonedas() {
		return cmbFiltroMonedas;
	}
	public void setCmbFiltroMonedas(HtmlDropDownList cmbFiltroMonedas) {
		this.cmbFiltroMonedas = cmbFiltroMonedas;
	}
	public HtmlDateChooser getDcFechaDesde() {
		return dcFechaDesde;
	}
	public void setDcFechaDesde(HtmlDateChooser dcFechaDesde) {
		this.dcFechaDesde = dcFechaDesde;
	}
	public HtmlDateChooser getDcFechaHasta() {
		return dcFechaHasta;
	}
	public void setDcFechaHasta(HtmlDateChooser dcFechaHasta) {
		this.dcFechaHasta = dcFechaHasta;
	}
//////////////RECIBO///////////////////////////////////////////////////
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
	public HtmlOutputText getLblNumeroRecibo() {
		return lblNumeroRecibo;
	}
	public void setLblNumeroRecibo(HtmlOutputText lblNumeroRecibo) {
		this.lblNumeroRecibo = lblNumeroRecibo;
	}
	public HtmlDateChooser getTxtFecham() {
		return txtFecham;
	}
	public void setTxtFecham(HtmlDateChooser txtFecham) {
		this.txtFecham = txtFecham;
	}
	public HtmlDropDownList getCmbTiporecibo() {
		return cmbTiporecibo;
	}
	public void setCmbTiporecibo(HtmlDropDownList cmbTiporecibo) {
		this.cmbTiporecibo = cmbTiporecibo;
	}
	public HtmlOutputText getLblMontoRecibido() {
		return lblMontoRecibido;
	}
	public void setLblMontoRecibido(HtmlOutputText lblMontoRecibido) {
		this.lblMontoRecibido = lblMontoRecibido;
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
	public UIOutput getLblNumRecm() {
		return lblNumRecm;
	}
	public void setLblNumRecm(UIOutput lblNumRecm) {
		this.lblNumRecm = lblNumRecm;
	}
	public HtmlOutputText getLblTasaCambio() {
		TasaCambioCtrl tcCtrl = new TasaCambioCtrl();
		Tpararela[] tpcambio = null;
		try{
			if(m.get("tasaCon") == null) {			
				String Tasa = "1.0";
				//obtener tasas de cambio paralela
				tpcambio = tcCtrl.obtenerTasaCambioParalela(new Date());		
				m.put("tpcambio", tpcambio);
				m.put("lblTasaCambio",Tasa);
				m.put("tasaCon","t");
			} else {
				//lblTasaCambio.setValue((String)m.get("lblTasaCambio"));
				tpcambio = (Tpararela[])m.get("tpcambio");
				if(tpcambio == null){
					lblMensajeValidacion.setValue("Tasa de cambio paralela no esta configurada");
					dwProcesa.setWindowState("normal");
					dwProcesa.setStyle("width:370px;height:160px");
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return lblTasaCambio;
	}
	public void setLblTasaCambio(HtmlOutputText lblTasaCambio) {
		this.lblTasaCambio = lblTasaCambio;
	}
	public HtmlOutputText getLblTasaJDE() {
		TasaCambioCtrl tcCtrl = new TasaCambioCtrl();
		Tcambio[] tcambio = null;
		try{
			if(m.get("tasaJDECon") == null) {			
				String Tasa = "1.0";
				//obtener tasas de cambio paralela
				tcambio = tcCtrl.obtenerTasaCambioJDEdelDia();		
				m.put("tcambio", tcambio);
				m.put("lblTasaJDE",Tasa);
				m.put("tasaJDECon","t");
			} else {
				if(lblTasaJDE != null ){
					lblTasaJDE.setValue((String)m.get("lblTasaJDE"));
					tcambio = (Tcambio[])m.get("tcambio");
					if(tcambio == null){
						lblMensajeValidacion.setValue("Tasa de cambio JDE no esta configurada");
						dwProcesa.setWindowState("normal");
						dwProcesa.setStyle("width:370px;height:160px");
					}
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return lblTasaJDE;
	}
	public void setLblTasaJDE(HtmlOutputText lblTasaJDE) {
		this.lblTasaJDE = lblTasaJDE;
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
	public HtmlInputText getTxtNumRec() {
		return txtNumRec;
	}
	public void setTxtNumRec(HtmlInputText txtNumRec) {
		this.txtNumRec = txtNumRec;
	}
	public List getLstTiporecibo() {
		
		if(lstTiporecibo == null){
			lstTiporecibo = new ArrayList();	
			lstTiporecibo.add(new SelectItem("AUTOMATICO","AUTOMATICO"));
			lstTiporecibo.add(new SelectItem("MANUAL","MANUAL"));
		}
		m.put("mTipoRecibo", "AUTOMATICO");
		return lstTiporecibo;
	}
	public void setLstTiporecibo(List lstTiporecibo) {
		this.lstTiporecibo = lstTiporecibo;
	}
	/****************************Lista Metodos de Pago por Caja***********************************************************/
	public List getLstMetodosPago() {
	    try {
	    	if(m.get("lstMetodosPagoCre") == null){
	    		lstMetodosPago = new ArrayList();
		    }else{
		    	lstMetodosPago = (List)m.get("lstMetodosPagoCre");
		    }
	    }catch(Exception ex){
	    	ex.printStackTrace();
			
		}  
		return lstMetodosPago;
	}
	/**********************************************************/
	public void setLstMetodosPago(List lstMetodosPago) {
		this.lstMetodosPago = lstMetodosPago;
	}
	/***************GET LISTA DE MONEDAS*****************************************************/
	public List getLstMoneda() {	
		try{
			if (m.get("lstMonedaCre")==null){
				lstMoneda = new ArrayList();
		    }else{
		    	lstMoneda = (List)m.get("lstMonedaCre");
		    }
	    }catch(Exception ex){
	    	ex.printStackTrace();
		}      
		return lstMoneda;
	}
	/***************************************************************************************/
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
			if(m.get("lstAfiliadoCre") == null){
				lstAfiliado = new ArrayList();
			}else{
				lstAfiliado = (List)m.get("lstAfiliadoCre");
			}        			
		} catch (Exception ex) {
			ex.printStackTrace();						
		}  	
		return lstAfiliado;
	}	
	/***************************************************************************************/
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
	    	if(m.get("lstBancoCre") == null){
	        	lstBanco = new ArrayList();
	        	banco = bancoCtrl.obtenerBancos(); 	
	        	for (F55ca022 f22 : banco) {
					SelectItem si = new SelectItem();
					si.setValue(f22.getId().getConciliar()+"@"+f22.getId().getBanco());
					si.setLabel(f22.getId().getBanco());
					si.setDescription(String.valueOf(f22.getId().getCodb()));
					lstBanco.add(si);
				}
	        	m.put("lstBancoCre", lstBanco);
	        	m.put("f55ca022", banco);
	    	}else{
	    		lstBanco = (List)m.get("lstBancoCre");
	    	}
	    }catch(Exception ex){
	    	ex.printStackTrace();	
		}
		return lstBanco;
	}
	/***************************************************************************************/
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
	public HtmlDropDownList getCmbMetodosPago() {
		return cmbMetodosPago;
	}
	public void setCmbMetodosPago(HtmlDropDownList cmbMetodosPago) {
		this.cmbMetodosPago = cmbMetodosPago;
	}
	public DialogWindow getDwRecibo() {
		return dwRecibo;
	}
	public void setDwRecibo(DialogWindow dwRecibo) {
		this.dwRecibo = dwRecibo;
	}
	public DialogWindow getDwCancelar() {
		return dwCancelar;
	}
	
	public void setDwCancelar(DialogWindow dwCancelar) {
		this.dwCancelar = dwCancelar;
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
	/***************************************/
	public List getSelectedMet() {	
		Object o = m.get("metCre");
		
		if(o == null){	
			selectedMet = new ArrayList();
			m.put("metCre", "y");
		}else {
			selectedMet = (ArrayList<MetodosPago>)m.get("lstPagos");
		}
		
		return selectedMet;
	}
/*********************************************/
	public HtmlGridView getMetodosGrid() {
		return metodosGrid;
	}
	public void setMetodosGrid(HtmlGridView metodosGrid) {
		this.metodosGrid = metodosGrid;
	}
	public void setSelectedMet(ArrayList<MetodosPago> selectedMet) {
		this.selectedMet = selectedMet;
	}
	public HtmlOutputText getLblCambio() {
		return lblCambio;
	}
	public void setLblCambio(HtmlOutputText lblCambio) {
		this.lblCambio = lblCambio;
	}
	public HtmlOutputText getLblCambioDomestico() {
		return lblCambioDomestico;
	}
	public void setLblCambioDomestico(HtmlOutputText lblCambioDomestico) {
		this.lblCambioDomestico = lblCambioDomestico;
	}
	public HtmlOutputText getLblPendienteDomestico() {
		return lblPendienteDomestico;
	}
	public void setLblPendienteDomestico(HtmlOutputText lblPendienteDomestico) {
		this.lblPendienteDomestico = lblPendienteDomestico;
	}
	public HtmlLink getLnkCambio() {
		return lnkCambio;
	}
	public void setLnkCambio(HtmlLink lnkCambio) {
		this.lnkCambio = lnkCambio;
	}
	public HtmlOutputText getTxtCambio() {
		return txtCambio;
	}
	public void setTxtCambio(HtmlOutputText txtCambio) {
		this.txtCambio = txtCambio;
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
	public HtmlOutputText getTxtPendienteDomestico() {
		return txtPendienteDomestico;
	}
	public void setTxtPendienteDomestico(HtmlOutputText txtPendienteDomestico) {
		this.txtPendienteDomestico = txtPendienteDomestico;
	}
	public HtmlInputTextarea getTxtConcepto() {
		return txtConcepto;
	}
	public void setTxtConcepto(HtmlInputTextarea txtConcepto) {
		this.txtConcepto = txtConcepto;
	}
/*************************************************************************/
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
			
			
			/*
			List lstAutorizadores = new ArrayList();
			EmpleadoCtrl empCtrl = new EmpleadoCtrl();
			Vf0101 vf0101 = null;
		 
 
			//obtener info de supervisor  index 0
			vf0101 = empCtrl.buscarEmpleadoxCodigo2(f55ca01.getId().getCaan8());	
//				lstAutoriza.add(new SelectItem(vf0101.getId().getWwrem1().trim() + " " + vf0101.getId().getAbalph().trim(), vf0101.getId().getAbalph().trim(),"Supervisor"));
			lstAutoriza.add(new SelectItem(vf0101.getId().getWwrem1().trim() + " " + vf0101.getId().getAban8(), vf0101.getId().getAbalph().trim(),"Supervisor"));
			lstAutorizadores.add(vf0101);
			
			//obtener info del autorizador titular index 1
			vf0101 = empCtrl.buscarEmpleadoxCodigo2(f55ca01.getId().getCaauti());	
//				lstAutoriza.add(new SelectItem(vf0101.getId().getWwrem1().trim() + " " + vf0101.getId().getAbalph().trim(),vf0101.getId().getAbalph().trim(),"Autorizador titular"));
			lstAutoriza.add(new SelectItem(vf0101.getId().getWwrem1().trim() + " " + vf0101.getId().getAbalph().trim(),vf0101.getId().getAbalph().trim(),"Autorizador titular"));
			lstAutorizadores.add(vf0101);
			
			//obtener info del autorizador suplente index 2, opcional
			if(f55ca01.getId().getCaausu() > 0){
				vf0101 = empCtrl.buscarEmpleadoxCodigo2(f55ca01.getId().getCaausu());	
//					lstAutoriza.add(new SelectItem(vf0101.getId().getWwrem1().trim() + " " + vf0101.getId().getAbalph().trim(),vf0101.getId().getAbalph().trim(),"Autorizador suplente"));
				lstAutoriza.add(new SelectItem(vf0101.getId().getWwrem1().trim() + " " + vf0101.getId().getAban8(),vf0101.getId().getAbalph().trim(),"Autorizador suplente"));
				lstAutorizadores.add(vf0101);
			}
			
			//obtener info del autorizador suplente index 3, opcional
			if(f55ca01.getId().getCacont() > 0){
				vf0101 = empCtrl.buscarEmpleadoxCodigo2(f55ca01.getId().getCacont());	
//					lstAutoriza.add(new SelectItem(vf0101.getId().getWwrem1().trim() + " " + vf0101.getId().getAbalph().trim(),vf0101.getId().getAbalph().trim(),"Contador"));
				lstAutoriza.add(new SelectItem(vf0101.getId().getWwrem1().trim() + " " + vf0101.getId().getAban8(),vf0101.getId().getAbalph().trim(),"Contador"));
				lstAutorizadores.add(vf0101);
			}
			
			m.put("lstAutoriza", lstAutoriza);
			m.put("lstAutorizadores", lstAutorizadores);
			
			*/
		 
		}catch(Exception ex){
			ex.printStackTrace(); 
		}
		return lstAutoriza;
	}
	
	public void setLstAutoriza(List lstAutoriza) {
		this.lstAutoriza = lstAutoriza;
	}
/***********************************************************************************************/
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
	public HtmlOutputText getLblMensajeDetalleCredito() {
		return lblMensajeDetalleCredito;
	}
	public void setLblMensajeDetalleCredito(HtmlOutputText lblMensajeDetalleCredito) {
		this.lblMensajeDetalleCredito = lblMensajeDetalleCredito;
	}
	
	public List getLstDfacturasCredito() {
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		if(m.get("lstDfacturasCredito") == null){	
			lstDfacturasCredito = new ArrayList();
			m.put("lstDfacturasCredito",lstDfacturasCredito);
		}else {
			lstDfacturasCredito = (List)m.get("lstDfacturasCredito");
		}	
		return lstDfacturasCredito;
	}
	public void setLstDfacturasCredito(List lstDfacturasCredito) {
		this.lstDfacturasCredito = lstDfacturasCredito;
	}
	public HtmlGridView getGvDfacturasCredito() {
		return gvDfacturasCredito;
	}
	public void setGvDfacturasCredito(HtmlGridView gvDfacturasCredito) {
		this.gvDfacturasCredito = gvDfacturasCredito;
	}
	
	public HtmlOutputText getMontoTotalAplicarDomestico() {
		return montoTotalAplicarDomestico;
	}
	public void setMontoTotalAplicarDomestico(
			HtmlOutputText montoTotalAplicarDomestico) {
		this.montoTotalAplicarDomestico = montoTotalAplicarDomestico;
	}
	public HtmlOutputText getMontoTotalAplicarForaneo() {
		return montoTotalAplicarForaneo;
	}
	public void setMontoTotalAplicarForaneo(HtmlOutputText montoTotalAplicarForaneo) {
		this.montoTotalAplicarForaneo = montoTotalAplicarForaneo;
	}
	public HtmlOutputText getMontoTotalFaltanteDomestico() {
		return montoTotalFaltanteDomestico;
	}
	public void setMontoTotalFaltanteDomestico(
			HtmlOutputText montoTotalFaltanteDomestico) {
		this.montoTotalFaltanteDomestico = montoTotalFaltanteDomestico;
	}
	public HtmlOutputText getMontoTotalFaltanteForaneo() {
		return montoTotalFaltanteForaneo;
	}
	public void setMontoTotalFaltanteForaneo(
			HtmlOutputText montoTotalFaltanteForaneo) {
		this.montoTotalFaltanteForaneo = montoTotalFaltanteForaneo;
	}
	public DialogWindow getDwAgregarFactura() {
		return dwAgregarFactura;
	}
	public void setDwAgregarFactura(DialogWindow dwAgregarFactura) {
		this.dwAgregarFactura = dwAgregarFactura;
	}
	public HtmlGridView getGvAgregarFactura() {
		return gvAgregarFactura;
	}
	public void setGvAgregarFactura(HtmlGridView gvAgregarFactura) {
		this.gvAgregarFactura = gvAgregarFactura;
	}
/****************************************************************************/
	public List getLstAgregarFactura() {
		if(m.get("lstAgregarFactura") == null){
			lstAgregarFactura = new ArrayList();
		}
		else{
			lstAgregarFactura = (List)m.get("lstAgregarFactura");
		}
		return lstAgregarFactura;
	}
	public void setLstAgregarFactura(List lstAgregarFactura) {
		this.lstAgregarFactura = lstAgregarFactura;
	}
/****************************************************************************/
	public List getLstCompaniaCre() {
		try{
			if(m.get("lstCompaniaCre")==null){
				CompaniaCtrl cc = new CompaniaCtrl();
				List lstcaja = (ArrayList)m.get("lstCajas");
				Vf55ca01 caja = (Vf55ca01)lstcaja.get(0);			
				int sCajaId = caja.getId().getCaid();
				
				List lstFiltro = new ArrayList();
				F55ca014[] lstComxCaja = cc.obtenerCompaniasxCaja(sCajaId);			
				
				lstFiltro.add(new SelectItem("10","Todas","Seleccione una Compañía primero"));
				for(int i=0; i<lstComxCaja.length;i++){				
					lstFiltro.add(new SelectItem(lstComxCaja[i].getId().getC4rp01(),lstComxCaja[i].getId().getC4rp01() + " " + lstComxCaja[i].getId().getC4rp01d1().trim(),lstComxCaja[i].getId().getC4rp01()));
				}
				m.put("cr_lstComxCaja", lstComxCaja);
				m.put("lstCompaniaCre", lstFiltro);	
				lstCompaniaCre = lstFiltro;
			}else{
				lstCompaniaCre = (List)m.get("lstCompaniaCre");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return lstCompaniaCre;
	}
	/**********************************/
	public void setLstCompaniaCre(List lstCompaniaCre) {
		this.lstCompaniaCre = lstCompaniaCre;
	}
	/***************************************************/
	public List getLstUninegCred() {
		try{
			if(m.get("lstUninegCred")==null){
				List lstFiltro = new ArrayList();				
				lstFiltro.add(new SelectItem("10","Todas","Seleccione una Sucursal primero"));				
				m.put("lstUninegCred", lstFiltro);	
				lstUninegCred = lstFiltro;
			}else{
				lstUninegCred = (List)m.get("lstUninegCred");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return lstUninegCred;
	}
	/*************************************************/
	public void setLstUninegCred(List lstUninegCred) {
		this.lstUninegCred = lstUninegCred;
	}
	public HtmlDropDownList getDdlCompaniaCre() {
		return ddlCompaniaCre;
	}
	public void setDdlCompaniaCre(HtmlDropDownList ddlCompaniaCre) {
		this.ddlCompaniaCre = ddlCompaniaCre;
	}
	public HtmlDropDownList getDdlUninegCred() {
		return ddlUninegCred;
	}
	public void setDdlUninegCred(HtmlDropDownList ddlUninegCred) {
		this.ddlUninegCred = ddlUninegCred;
	}
	public HtmlDropDownList getDdlSucursalCred() {
		return ddlSucursalCred;
	}
	public void setDdlSucursalCred(HtmlDropDownList ddlSucursalCred) {
		this.ddlSucursalCred = ddlSucursalCred;
	}
	/***********************************************************************************/
	public List getLstSucursalCred() {
		try{
			if(m.get("lstSucursalCred")==null){
				List lstFiltro = new ArrayList();				
				lstFiltro.add(new SelectItem("00010","Todas","Seleccione una Compañía primero"));				
				m.put("lstSucursalCred", lstFiltro);
				lstSucursalCred = lstFiltro;
			}else{
				lstSucursalCred = (List)m.get("lstSucursalCred");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return lstSucursalCred;
	}
	
	
	public HtmlInputText getTxtNofacturaDesde() {
		return txtNofacturaDesde;
	}
	public void setTxtNofacturaDesde(HtmlInputText txtNofacturaDesde) {
		this.txtNofacturaDesde = txtNofacturaDesde;
	}
	public HtmlInputText getTxtNofacturaHasta() {
		return txtNofacturaHasta;
	}
	public void setTxtNofacturaHasta(HtmlInputText txtNofacturaHasta) {
		this.txtNofacturaHasta = txtNofacturaHasta;
	}
	/************************************************************************************/
	public void setLstSucursalCred(List lstSucursalCred) {
		this.lstSucursalCred = lstSucursalCred;
	}
	public HtmlOutputText getIntSelected() {
		return intSelected;
	}
	public void setIntSelected(HtmlOutputText intSelected) {
		this.intSelected = intSelected;
	}
	public HtmlOutputText getIntSelectedDet() {
		return intSelectedDet;
	}
	public void setIntSelectedDet(HtmlOutputText intSelectedDet) {
		this.intSelectedDet = intSelectedDet;
	}
	public DialogWindow getDwFijarTasaCambio() {
		return dwFijarTasaCambio;
	}
	public void setDwFijarTasaCambio(DialogWindow dwFijarTasaCambio) {
		this.dwFijarTasaCambio = dwFijarTasaCambio;
	}
	public HtmlOutputText getLblMsgErrorCambioTasa() {
		return lblMsgErrorCambioTasa;
	}
	public void setLblMsgErrorCambioTasa(HtmlOutputText lblMsgErrorCambioTasa) {
		this.lblMsgErrorCambioTasa = lblMsgErrorCambioTasa;
	}
	public HtmlInputTextarea getTxtMotivoCambioTasa() {
		return txtMotivoCambioTasa;
	}
	public void setTxtMotivoCambioTasa(HtmlInputTextarea txtMotivoCambioTasa) {
		this.txtMotivoCambioTasa = txtMotivoCambioTasa;
	}
	public HtmlInputText getTxtNuevaTasaCambio() {
		return txtNuevaTasaCambio;
	}
	public void setTxtNuevaTasaCambio(HtmlInputText txtNuevaTasaCambio) {
		this.txtNuevaTasaCambio = txtNuevaTasaCambio;
	}
	/** 
	 * @managed-bean true
	 */
	protected P55RECIBO getP55recibo() {
		p55recibo = (P55RECIBO) FacesContext
				.getCurrentInstance().getELContext().getELResolver()
				.getValue(FacesContext.getCurrentInstance()
					.getELContext(), null, "p55recibo");
		return p55recibo;
	}

	/** 
	 * @managed-bean true
	 */
	protected void setP55recibo(P55RECIBO p55recibo) {
		this.p55recibo = p55recibo;
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
	public HtmlOutputText getLblMarcaSobrDifer() {
		return lblMarcaSobrDifer;
	}
	public void setLblMarcaSobrDifer(HtmlOutputText lblMarcaSobrDifer) {
		this.lblMarcaSobrDifer = lblMarcaSobrDifer;
	}
	public HtmlCheckBox getChkSobranteDifrl() {
		return chkSobranteDifrl;
	}
	public void setChkSobranteDifrl(HtmlCheckBox chkSobranteDifrl) {
		this.chkSobranteDifrl = chkSobranteDifrl;
	}
	public HtmlInputSecret getTrack() {
		return track;
	}
	public void setTrack(HtmlInputSecret track) {
		this.track = track;
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
	public HtmlCheckBox getChkMostrarTodoSrch() {
		return chkMostrarTodoSrch;
	}
	public void setChkMostrarTodoSrch(HtmlCheckBox chkMostrarTodoSrch) {
		this.chkMostrarTodoSrch = chkMostrarTodoSrch;
	}
	public HtmlDialogWindow getDwBorrarPago() {
		return dwBorrarPago;
	}
	public void setDwBorrarPago(HtmlDialogWindow dwBorrarPago) {
		this.dwBorrarPago = dwBorrarPago;
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
		
		if(CodeUtil.getFromSessionMap("cr_lstDonacionesRecibidas") != null )
			return lstDonacionesRecibidas = (ArrayList<DncIngresoDonacion>)
					CodeUtil.getFromSessionMap("cr_lstDonacionesRecibidas");
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
			
			lstBeneficiarios = (ArrayList<SelectItem>)CodeUtil.getFromSessionMap("cr_lstbeneficiarios");
			if(lstBeneficiarios != null &&  !lstBeneficiarios.isEmpty() )
				return lstBeneficiarios;
			
			lstBeneficiarios = (ArrayList<SelectItem>)DonacionesCtrl.obtenerBeneficiariosConfigurados(true, false);
			
			
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}finally{
			
			if(lstBeneficiarios == null )
				lstBeneficiarios = new ArrayList<SelectItem>();

			 CodeUtil.putInSessionMap("cr_lstbeneficiarios", lstBeneficiarios);
			
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
	
	public HtmlDropDownList getDdlTipoMarcasTarjetas() {
		return ddlTipoMarcasTarjetas;
	}

	public void setDdlTipoMarcasTarjetas(HtmlDropDownList ddlTipoMarcasTarjetas) {
		this.ddlTipoMarcasTarjetas = ddlTipoMarcasTarjetas;
	}

	public List<SelectItem> getLstMarcasDeTarjetas() {
		
		try {
			
			lstMarcasDeTarjetas = (ArrayList<SelectItem>)CodeUtil.getFromSessionMap("cr_lstMarcasDeTarjetas");
			if(lstMarcasDeTarjetas != null &&  !lstMarcasDeTarjetas.isEmpty() )
				return lstMarcasDeTarjetas;
			
			lstMarcasDeTarjetas = AfiliadoCtrl.obtenerTiposMarcaTarjetas();
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}finally{
			
			if(lstMarcasDeTarjetas == null )
				lstMarcasDeTarjetas = new ArrayList<SelectItem>();

			 CodeUtil.putInSessionMap("cr_lstMarcasDeTarjetas", lstMarcasDeTarjetas);
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
	
}

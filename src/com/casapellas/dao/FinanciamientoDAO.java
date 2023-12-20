package com.casapellas.dao;
/**
 * CASA PELLAS S.A.
 * Creado por.........: Juan Carlos Ñamendi Pineda
 * Fecha de Creación..: 14/12/2009
 * Última modificación: 29/08/2011
 * Modificado por.....:	Juan Carlos Ñamendi Pineda
 * 
 */
import java.math.*;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
//import org.apache.commons.mail.MultiPartEmail;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.casapellas.controles.AfiliadoCtrl;
import com.casapellas.controles.BancoCtrl;
import com.casapellas.controles.ClsConfiguracionMensaje;
import com.casapellas.controles.ClsF5503B11;
import com.casapellas.controles.ClsParametroCaja;
import com.casapellas.controles.ClsTipoDocumentoFinanciamiento;
import com.casapellas.controles.CompaniaCtrl;
import com.casapellas.controles.ConsolidadoDepositosBcoCtrl;
import com.casapellas.controles.CtrlCajas;
import com.casapellas.controles.CtrlPoliticas;
import com.casapellas.controles.CtrlSolicitud;
import com.casapellas.controles.CuotaCtrl;
import com.casapellas.controles.DonacionesCtrl;
import com.casapellas.controles.EmpleadoCtrl;
import com.casapellas.controles.MetodosPagoCtrl;
import com.casapellas.controles.MonedaCtrl;
import com.casapellas.controles.NumcajaCtrl;
import com.casapellas.controles.ReciboCtrl;
import com.casapellas.controles.SendMailsCtrl;
import com.casapellas.controles.TasaCambioCtrl;
import com.casapellas.controles.VerificarFacturaProceso;
import com.casapellas.donacion.entidades.DncIngresoDonacion;
import com.casapellas.entidades.Aplicacion;
import com.casapellas.entidades.Cafiliados;
import com.casapellas.entidades.CajaParametro;
import com.casapellas.entidades.Credhdr;
import com.casapellas.entidades.Elementofin;
import com.casapellas.entidades.F55ca014;
import com.casapellas.entidades.F55ca022;
import com.casapellas.entidades.Finandet;
import com.casapellas.entidades.Finanhdr;
import com.casapellas.entidades.Hfinan;
import com.casapellas.entidades.MetodosPago;
import com.casapellas.entidades.Metpago;
import com.casapellas.entidades.Pago;
import com.casapellas.entidades.Producto;
import com.casapellas.entidades.Recibo;
import com.casapellas.entidades.Recibofac;
import com.casapellas.entidades.Recibojde;
import com.casapellas.entidades.Solicitud;
import com.casapellas.entidades.SolicitudId;
import com.casapellas.entidades.Tcambio;
import com.casapellas.entidades.TipoDocumentoFinanciamiento;
import com.casapellas.entidades.Tpararela;
import com.casapellas.entidades.ens.Vautoriz;
import com.google.gson.Gson;
import com.casapellas.entidades.Vf0101;
import com.casapellas.entidades.Vf0311fn;
import com.casapellas.entidades.Vf55ca01;
import com.casapellas.entidades.Vf55ca012;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.jde.creditos.ProcesarPagoFacturaJde;
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
import com.ibm.ejs.container.container;
import com.ibm.faces.component.html.HtmlJspPanel;
import com.infragistics.faces.grid.component.RowItem;
import com.infragistics.faces.grid.component.html.HtmlGridView;
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

import ni.com.casapellas.client.config.Parameter;
import ni.com.casapellas.tool.restful.connection.RestResponse;

public class FinanciamientoDAO {
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
	
	String sCredyCobMail = "creditoycobro@casapellas.com.ni";
	
	private HtmlDialogWindow dwCargando;
	/*Opciones de busqueda*/
	private List lstTiposBusqueda;
	private HtmlDropDownList hddTiposBusqueda;
	private HtmlInputText txtParametro;
	private String strMensajeBusqueda;
	private HtmlDropDownList hddFiltroCompania;
	private HtmlDropDownList hddFiltroMoneda;
	
	/*Grid principal de cuotas pendientes*/
	private HtmlGridView gvCuotas;
	private List lstCuotas;
	/*busqueda por monedas*/
	private List lstFiltroMonedas;
	
	/*busqueda por companias*/
	private List lstFiltroCompanias;
	
	/*Ventana de Detalle de Solicitud*/
	private HtmlDialogWindow dwDetalleSolicitud;
	private Date fechaDet;
	private String codcliDet;
	private String nomcliDet;
	private String monedaDet;
	private BigDecimal tasaDet;
	private String noSolDet;
	private String tipoSolDet;
	private String lineaDet;
	
	private BigDecimal txtPrincipalDet;
	private BigDecimal txtInteresDet;
	private BigDecimal txtImpuestoDet;
	private BigDecimal txtTotalDet;
	private BigDecimal txtTotalMoraDet;
	private BigDecimal txtPendienteDet;
	
	private HtmlDropDownList hddMonedaDetalle;
	private List lstMonedaDetalle;
	
	private List lstProductos;
	private HtmlGridView gvProductos;
	
	private List lstDetallesol;
	private HtmlGridView gvDetallesol;
	
	private HtmlGridView gvIntereses;
	private List lstIntereses; 
	
	/*****ventana de recibo**********/
	private HtmlDialogWindow dwRecibo;
	private Date fechaRecibo;
	private HtmlOutputText txtNumrec;
	private HtmlOutputText lblNumrecm;
	private HtmlInputText txtNumrecm;
	private HtmlOutputText txtCodcli;
	private HtmlOutputText txtNomcli;	
	private HtmlDropDownList ddlTipoRecibo;
	private List lstTipoRecibo;
	private HtmlDateChooser dcFecham;
	
	private HtmlInputText txtMonto;
	//metodos de pago
	private HtmlDropDownList ddlMetodosPago;
	private List lstMetodosPago;
	//monedas x metodo
	private HtmlDropDownList ddlMoneda;
	private List lstMoneda;
	// Afiliado
	private List<SelectItem> lstAfiliado;
	private HtmlDropDownList ddlAfiliado;
	private HtmlOutputText lblAfiliado;

	// Bancos
	private List lstBanco;
	private HtmlDropDownList ddlBanco;
	private HtmlOutputText lblBanco;

	// referencias
	private HtmlInputText txtReferencia1;
	private HtmlInputText txtReferencia2;
	private HtmlInputText txtReferencia3;
	private HtmlOutputText lblReferencia1;
	private HtmlOutputText lblReferencia2;
	private HtmlOutputText lblReferencia3;
	
	//tasas de cambio
	private HtmlOutputText tcJDE;
	private HtmlOutputText tcParalela;
	
	//grid de metodos de pago
	private ArrayList<MetodosPago> lstPagos;
	private HtmlGridView gvMetodosPago;
	//grid de cuotas seleccionadas
	private List lstSelectedCuotas;
	private HtmlGridView gvSelectedCuotas;
	
	//resumen de pago
	private HtmlInputTextarea txtConcepto;
	private HtmlOutputText lblMontoAplicar;
	private HtmlOutputText lblMontoRecibido;
	private HtmlInputText txtMontoAplicar;
	private HtmlOutputText txtMontoRecibido;
	
	//faltante 
	private HtmlOutputText montoTotalAplicarDomestico;
	private HtmlOutputText montoTotalFaltanteDomestico;
	private HtmlOutputText montoTotalAplicarForaneo;
	private HtmlOutputText montoTotalFaltanteForaneo;
	
	private HtmlOutputText intSelectedDet;
	
	// cambio
	private HtmlOutputText lblCambio;
	private HtmlOutputText txtCambio;
	private HtmlInputText txtCambioForaneo;
	private HtmlOutputText lblCambioDomestico;
	private HtmlOutputText txtCambioDomestico;
	private HtmlOutputText lblPendienteDomestico;
	private HtmlOutputText txtPendienteDomestico;
	private HtmlLink lnkCambio;
	
	
	//solicitud de autorizacion 
	//solicitud
	private DialogWindow dwSolicitud;
	private UIOutput lblMensajeAutorizacion;
	private List lstAutoriza;
	public HtmlInputText txtReferencia;	
	public HtmlDateChooser txtFecha;
	public HtmlInputTextarea txtObs;
	public HtmlDropDownList cmbAutoriza;
	
	/******mensaje de error********/
	private HtmlDialogWindow dwMensajeError;
	private HtmlOutputText lblMensajeError;
	
	/******Detalle de cuota******************/
	private HtmlDialogWindow dwDetalleCuota;
	private Date fechaCuotaDet;
	private String noCuotaDet;
	private BigDecimal tasaCuotaDet;
	private String monedaCuotaDet;
	private BigDecimal principalCuotaDet;
	private BigDecimal interesCuotaDet;
	private BigDecimal impuestoCuotaDet;
	private BigDecimal moraCuotaDet;
	private BigDecimal totalCuotaDet;
	private BigDecimal pendienteCuotaDet;
	private String diasVenCuotaDet;
	private String diasAcumCuotaDet;
	private String totalDiasCuotaDet;
	
	private String companiaCuotaDet;
	private String sucursalCuotaDet;
	
	//ventana para agregar cuotas a financiamiento
	private HtmlDialogWindow dwAgregarCuota;
	private HtmlGridView gvAgregarCuota;
	private List lstAgregarCuota;
	
	//ventana para procesar el recibo 
	private HtmlDialogWindow dwProcesaRecibo;
	//ventana de cancelar recibo
	private HtmlDialogWindow dwCancelar;
	
	//realizar abono a principal
	private HtmlCheckBox chkPrincipal;
	
	//------ Pago con tarjeta de crédito con voucher manuales.
	private HtmlCheckBox chkVoucherManual;
	private HtmlOutputText lbletVouchermanual;
	private List<Credhdr> lstComponentes;
	
	private HtmlDialogWindow dwBorrarPago;
	
	//&& =============== Donaciones
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
	
	//&& =============== Marcas de tarjetas 
	private List<SelectItem>lstMarcasDeTarjetas ;
	private HtmlDropDownList ddlTipoMarcasTarjetas;
	private HtmlOutputText lblMarcaTarjeta;
	
	//&& =============== grid para visualizar detalle de otros financimientos asociados
	private HtmlGridView gvDetalleExtraFinanciamientos;
	private List<Credhdr> lstDetalleExtraFinanciamientos;
	private HtmlJspPanel pnlDatosOtrosFinancimientos ;
	private HtmlOutputText lblFacturaOtrosFinan;
	private String styleDialogDetalleFinancimiento;

	//Nuevos valores para JDE
	String[] valoresJdeNumeracion = (String[]) m.get("valoresJDENumeracionIns");
	String[] valoresJDEInsFinanciamiento = (String[]) m.get("valoresJDEInsFinanciamiento");
	String[] valoresJDEInsFCV = (String[]) m.get("valoresJDEInsFCV");
	/*******************************************************************************************************/	
	public void procesarDonacionesIngresadas(ActionEvent ev){
		String msg = "";
		try {
			
			if( CodeUtil.getFromSessionMap("fin_lstDonacionesRecibidas") == null ){
				msg = "Ingrese al menos una donación para el proceso" ;
				return;
			}
			BigDecimal montodonado = new BigDecimal(lblTotalMontoDonacion.getValue().toString().trim().replace(",", ""));
			if(montodonado.compareTo(BigDecimal.ZERO) == 0 ){
				msg = "El monto de donación debe ser mayor a cero" ;
				return;
			}
			CodeUtil.putInSessionMap("fin_MontoTotalEnDonacion", montodonado);
			
			dwIngresarDatosDonacion.setWindowState("hidden");
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}finally{
			msgValidaIngresoDonacion.setValue(msg);
			CodeUtil.refreshIgObjects(new Object[]{msgValidaIngresoDonacion}) ;
		}
	}
	
	public void cerrarVentanaDonacion(ActionEvent ev) {
		CodeUtil.removeFromSessionMap(new String[]{"fin_MontoTotalEnDonacion","fin_lstDonacionesRecibida"} ) ;
		dwIngresarDatosDonacion.setWindowState("hidden");
	}
	
	@SuppressWarnings("unchecked")
	public void agregarMontoDonacion(ActionEvent ev){
		String msg = "" ;
		
		try {
			
			List<Finanhdr>lstFacturasSelected = (List<Finanhdr>) m.get("lstCreditosFinan");
			
			final String beneficiario = ddlDnc_Beneficiario.getValue().toString();
			String strMontoDonacion = txtdnc_montodonacion.getValue().toString();
			String moneda = ddlMoneda.getValue().toString();
			String codformapago = ddlMetodosPago.getValue().toString(); 
			String metodopago = codformapago + ", " + lblFormaDePagoCliente.getValue().toString();
			String codcomp = lstFacturasSelected.get(0).getId().getCodcomp().trim() ;
			
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
			
			lstBeneficiarios = (ArrayList<SelectItem>)CodeUtil.getFromSessionMap("fin_lstbeneficiarios");
			SelectItem si = (SelectItem) CollectionUtils.find(lstBeneficiarios,
				new Predicate() {
					public boolean evaluate(Object o) {
						return ((SelectItem) o).getValue().toString().compareTo(beneficiario) == 0;
					}
				});
			
			lstDonacionesRecibidas = ( CodeUtil.getFromSessionMap("fin_lstDonacionesRecibidas") == null )?
					new ArrayList<DncIngresoDonacion>():
					(ArrayList<DncIngresoDonacion>)CodeUtil.getFromSessionMap("fin_lstDonacionesRecibidas");
					
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
			
			CodeUtil.putInSessionMap("fin_lstDonacionesRecibidas", lstDonacionesRecibidas) ;
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
			
			final String metodopago = ddlMetodosPago.getValue().toString();
			
			if(metodopago.compareTo("MP") == 0){
				msg = "Seleccione metodo de pago";
				return;
			} 
			String sMonto  = txtMonto.getValue().toString();
			if ( !sMonto.trim().matches(PropertiesSystem.REGEXP_AMOUNT) ){
				msg = "El monto ingresado no es válido";
				return;
			}
			final String moneda = ddlMoneda.getValue().toString();
			
			@SuppressWarnings("unchecked")
			List<Vf55ca012>metodospagoconfig = (ArrayList<Vf55ca012>)CodeUtil.getFromSessionMap("fin_MetodosPagoConfigurados");
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
			CodeUtil.putInSessionMap("fin_lstDonacionesRecibidas", lstDonacionesRecibidas);
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
				dwMensajeError.setWindowState("normal");
				lblMensajeError.setValue(msg);
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
	public boolean anularRecibo(int iNumrec,int iCaid, String sCodsuc, String sCodcomp,
								String sMonedaBase, Vautoriz vAut[], int iNoFCV, int iCodcli){
		boolean bBorrado = true;
		
		double[] dMontos = null;
		double[] dPendientes = null;
		
		List<Vf0311fn> lstReciboFac = null;
		List<Recibojde> lstRecibojde = null;
		
		Divisas divisas = new Divisas();
		Recibojde recibojde = null;
		CuotaCtrl cuotaCtrl = new CuotaCtrl();
		ReciboCtrl recCtrl = new ReciboCtrl();
		Connection cn;
		
		try {
			cn = As400Connection.getJNDIConnection("DSMCAJA2");
			cn.setAutoCommit(false);
			
			recCtrl.actualizarEstadoRecibo(iNumrec,iCaid,sCodsuc,sCodcomp, vAut[0].getId().getCoduser(), "No se completo pago con SP",valoresJDEInsFinanciamiento[0]);
			lstRecibojde = recCtrl.getEnlaceReciboJDE(iCaid, sCodsuc,sCodcomp,iNumrec,valoresJDEInsFinanciamiento[0]);
			lstReciboFac = cuotaCtrl.leerFacturasReciboFinan2(iCaid, sCodcomp, iNumrec, valoresJDEInsFinanciamiento[0], iCodcli);

			for(Vf0311fn hFac2: lstReciboFac ){
				
				for(Recibojde rj: lstRecibojde){
					if(rj.getId().getNumrec() == iNumrec && rj.getId().getTipodoc().equals("R")){
						recibojde = rj;
						break;
					}
				}
				//&& ================ Moneda domestica.
				if(hFac2.getId().getMoneda().equals(sMonedaBase)){

					//&& ========== leer montos aplicados en el recibo
					dMontos = recCtrl.getMontoAplicadoF0311(cn, hFac2.getId().getNofactura(), 
											hFac2.getId().getTipofactura(), 
											recibojde.getId().getRecjde(), 
											hFac2.getId().getPartida(),
											hFac2.getId().getCodcli());
					
					bBorrado = recCtrl.aplicarMontoF0311(cn, "A",	
										(hFac2.getId().getCpendiente()/100.0) + divisas.roundDouble(dMontos[0]),
										0, vAut[0].getId().getLogin(), 
										"000"+hFac2.getId().getCodsuc(), 
										hFac2.getId().getTipofactura(),
										hFac2.getId().getNofactura(),
										hFac2.getId().getPartida(),
										hFac2.getId().getCodcli());
					if(!bBorrado) break;
					
					//&& ===== Restablecer valores originales en financimiento.
					dPendientes = recCtrl.getMontoPendienteF05503am(cn,
											hFac2.getId().getCodsuc(),
											hFac2.getId().getNosol(),
											hFac2.getId().getTiposol(),
											hFac2.getId().getNocuota(),
											hFac2.getId().getCodcli());
					bBorrado = cuotaCtrl.aplicarMontoF5503AM(cn,
										(dPendientes[0]) +  divisas.roundDouble(dMontos[0]),
										0,hFac2);
					if(!bBorrado) break;
				
					//&& ===== Borrar el recibo RC en jde
					bBorrado = recCtrl.borrarRC(cn, hFac2.getId().getNofactura(),
												hFac2.getId().getTipofactura(), 
												recibojde.getId().getRecjde(), 
												hFac2.getId().getPartida(),
												hFac2.getId().getCodcli());
					if(!bBorrado) break;
					
					//&& ===== borrar asiento de diario creados por el recibo.
					for(Recibojde rjd : lstRecibojde){
						if(rjd.getId().getNumrec() == iNumrec && rjd.getId().getTipodoc().equals("A")){
							/*bBorrado = recCtrl.borrarAsientodeDiario(cn, 
													rjd.getId().getRecjde(), 
													rjd.getId().getNobatch());*/
							if(!bBorrado) break;
						}
					}
				}
				//&& ============ Moneda Domestica.
				else{

					//&& =========== leer montos aplicados en el recibo
					dMontos = recCtrl.getMontoAplicadoF0311(cn, 
										hFac2.getId().getNofactura(), 
										hFac2.getId().getTipofactura(), 
										recibojde.getId().getRecjde(), 
										hFac2.getId().getPartida(),
										hFac2.getId().getCodcli());
					
					//&& =========== actualizar factura
					bBorrado = recCtrl.aplicarMontoF0311(cn, "A",
								(hFac2.getId().getCpendiente()/100.0) + divisas.roundDouble(dMontos[0]), 
								(hFac2.getId().getDpendiente()/100.0) + divisas.roundDouble(dMontos[1]), 
								vAut[0].getId().getLogin(), 
								"000"+hFac2.getId().getCodsuc(), 
								hFac2.getId().getTipofactura(), 
								hFac2.getId().getNofactura(), 
								hFac2.getId().getPartida(),
								hFac2.getId().getCodcli());
					if(!bBorrado) break;
					
					//&& =========== actualizacion de tablas de financimiento.
					dPendientes = recCtrl.getMontoPendienteF05503am(cn,hFac2.getId().getCodsuc(),
													hFac2.getId().getNosol(),
													hFac2.getId().getTiposol(),
													hFac2.getId().getNocuota(),
													hFac2.getId().getCodcli());
					if(!hFac2.getId().getTipofactura().trim().equals("MF"))
						bBorrado = cuotaCtrl.aplicarMontoF5503AM(cn,
											(dPendientes[0]) + divisas.roundDouble(dMontos[0]),
											(dPendientes[1]) + divisas.roundDouble(dMontos[1]),
											hFac2);
					if(!bBorrado) break;
				
					//&& ============== borrar RC y RG
					bBorrado = recCtrl.borrarRC(cn, hFac2.getId().getNofactura(), 
											hFac2.getId().getTipofactura(),
											recibojde.getId().getRecjde(), 
											hFac2.getId().getPartida(),
											hFac2.getId().getCodcli());
					if(!bBorrado) break;
					

					//&& ===== borrar asiento de diario creados por el recibo.
					for(Recibojde rjd : lstRecibojde){
						if(rjd.getId().getNumrec() == iNumrec && rjd.getId().getTipodoc().equals("A")){
							/*bBorrado = recCtrl.borrarAsientodeDiario(cn, 
													rjd.getId().getRecjde(), 
													rjd.getId().getNobatch());*/
							if(!bBorrado) break;
						}
					}
				}
				//borrar enlace entre recibo y factura
				bBorrado = recCtrl.actualizarEnlaceReciboFac(cn,hFac2,iNumrec,iCaid,sCodcomp, iCodcli);
				if(!bBorrado) break;
			}
			
			//&& ========== Anulacion de FCV 
			if(iNoFCV > 0 ){
				Recibo ficha = recCtrl.obtenerFichaCV(iNoFCV, iCaid, sCodcomp, sCodsuc,"");
				lstRecibojde = recCtrl.getEnlaceReciboJDE(iCaid, sCodsuc, 
									ficha.getId().getCodcomp(),
									ficha.getId().getNumrec(),
									ficha.getId().getTiporec());
				
				recCtrl.actualizarEstadoRecibo(null, null, 
										ficha.getId().getNumrec(),
										ficha.getId().getCaid(),
										ficha.getId().getCodsuc(),
										ficha.getId().getCodcomp(), 
										vAut[0].getId().getCoduser(), 
										"Error de aplicacion Socket POS",
										ficha.getId().getTiporec());
				
				recibojde = null;
				for(Recibojde rj: lstRecibojde){
					//recCtrl.borrarAsientodeDiario(cn, rj.getId().getRecjde(), rj.getId().getNobatch());
					recibojde = rj;
				}
				/*if(recibojde != null)
					recCtrl.borrarBatch(cn, recibojde.getId().getNobatch(),"G");*/
			} 
			//&& ======= Borrar los sobrantes.
			recibojde = null;
			lstRecibojde = recCtrl.getEnlaceReciboJDE(iCaid, sCodsuc, sCodcomp, iNumrec, "SBR");
			
			/*for (Recibojde rj : lstRecibojde) {
				recCtrl.borrarAsientodeDiario(cn, rj.getId().getRecjde(), rj.getId().getNobatch());
				recibojde = rj;
			}*/
			/*if(recibojde != null)
				recCtrl.borrarBatch(cn, recibojde.getId().getNobatch(),"G");*/
			
			cn.commit();
			
		} catch (Exception e) {
			bBorrado = false;
			e.printStackTrace();
		}finally{
//			try {cn.close();} catch (Exception e2) {}
		}
		return bBorrado;
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
		@SuppressWarnings("unchecked")
		public boolean anularPagosSP(List lstMetodosPago){
			boolean bPago = true;
			MetodosPago rd = null;
			List lstResult = null;
			PosCtrl p = new PosCtrl();
			
			try{
				
				for (int i = 0; i < lstMetodosPago.size(); i++){
					rd = (MetodosPago)lstMetodosPago.get(i);

					if(rd.getMetodo().compareTo(MetodosPagoCtrl.TARJETA) != 0 || rd.getVmanual().compareTo("2") != 0)
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
					if(mPago.getMetodo().equals(MetodosPagoCtrl.TARJETA) && mPago.getVmanual().equals("2")){
						//leer terminal por afiliado caja
						//solicitar autorizacion a credomatic
						sMonto = d.roundDouble(mPago.getMonto()) + "";
						sTerminal = mPago.getTerminal();
						
						//automatica
						if(!mPago.getTrack().equals("")){
							lstDatos = (List)lstDatosTrack.get(j);
							lstRespuesta = p.realizarPago(sTerminal, sMonto, mPago.getTrack(), lstDatos);
							sNotarjeta = lstDatos.get(1);
							
//							if(lstDatos.size()==7)nombre = lstDatos.get(2);else nombre = lstDatos.get(2) + " " + lstDatos.get(3);
							
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
							
							sNotarjeta = (sNotarjeta).substring(sNotarjeta.length()-4, 
									sNotarjeta.length());//4 ult. digitos de tarjeta
							
							//&& ===== Anular todos los pagos que si se aplicaron en el recibo.
							if(lstRespuesta.get(0).compareTo("00") != 0 && lstRespuesta.get(0).compareTo("08") != 0)
								anularPagosSP(lstPagosAplicados);
							
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
								
								lstPagosAplicados.add(mPago);
								
								j++;
							}else if (lstRespuesta.get(0).toString().equals("error")){
								bHecho = false;
								lblMensajeError.setValue("Tarjeta No.: " + sNotarjeta + "; No puedo ser procesada!!!\n  Error: " + lstRespuesta.get(1) + "\n" +
																lstRespuesta.get(2)+"\n" + lstRespuesta.get(3));
								lstDatos.add("false");
								//lstDatosTrack.set(j,lstDatos);
								m.put("lstDatosTrack_Con",lstDatosTrack);
								j++;
								return false;
							}else{
								bHecho = false;
								lblMensajeError.setValue("Tarjeta No.: " + sNotarjeta + "; No puedo ser procesada!!!  Error: " + lstRespuesta.get(1));
								lstDatos.add("false");
								//lstDatosTrack.set(j,lstDatos);
								m.put("lstDatosTrack_Con",lstDatosTrack);
								j++;
								return false;
							}
							m.put("lstPagos",lstPagos);
						}else{//hubo algun error con la transferencia con credomatic
							lblMensajeError.setValue("Error al aplicar el pago con tarjeta a credomatic!!!");
							return false;
						}

					}
				}
			}catch(Exception ex){
				bHecho = false;
				lblMensajeError.setValue("Error en aplicarPagoSocketPos!!!" + ex);
				ex.printStackTrace();
			}
			return bHecho;
		}
/***********************************************************************************************************************/	
	public String validarPagoSocket(double dMontoAplicar,Credhdr hFac,boolean usaManual){
		boolean validado = false;
		String sMensajeError = "";
		double monto = 0;
		String sMonto = "";
		Pattern pNumero = null;
		Divisas d = new Divisas();
		int y = 158;
		CtrlPoliticas polCtrl = new CtrlPoliticas();
		try{	
			List selectedMet = (List) m.get("lstPagosFinan");
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
				dwMensajeError.setWindowState("normal");
				y = y + 5;
			} else if (matNumero == null || !matNumero.matches() || monto == 0) {
					txtMonto.setValue("");
					validado = false;
					txtMonto.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El monto ingresado no es correcto<br>";
					dwMensajeError.setWindowState("normal");
					y = y + 5;
			} else if (monto > dMontoAplicar) {
					validado = false;
					txtMonto.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El monto ingresado para este método de pago debe ser menor o igual al monto a aplicar<br>";
					dwMensajeError.setWindowState("normal");
					y = y + 7;
			}
				// Valida Referencias
			else if (ddlAfiliado.getValue().toString().equals("01")) {
					validado = false;
					ddlAfiliado.setStyleClass("frmInput2Error2");
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Seleccione un afiliado<br>";
					dwMensajeError.setWindowState("normal");
					y = y + 5;
			}else if (ref1.equals("")) {//valida identificacion del cliente
					validado = false;
					txtReferencia1.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Identificación requerida<br>";
					dwMensajeError.setWindowState("normal");
					y = y + 5;
			} else if (!matAlfa1.matches()) {
					validado = false;
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El campo <b>Identificación<b/> contiene caracteres invalidos<br>";
					txtReferencia1.setStyleClass("frmInput2Error");
					dwMensajeError.setWindowState("normal");
					y = y + 7;
			} else if (ref1.length() > 150) {
					validado = false;
					sMensajeError = sMensajeError+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> La cantidad de caracteres del campo <b>Identificación<b/> es muy alta (lim. 150)<br>";
					txtReferencia1.setStyleClass("frmInput2Error");
					dwMensajeError.setWindowState("normal");
					y = y + 15;
			}
			else if (!polCtrl.validarMonto(Integer.parseInt(sCajaId),hFac.getId().getCodcomp(), ddlMoneda.getValue().toString(),ddlMetodosPago.getValue().toString(), monto)) {
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
			} else if (selectedMet.size() > 0) {
				MetodosPago[] mpagos = new MetodosPago[selectedMet.size()];
				for (int i = 0; i < selectedMet.size(); i++) {
					mpagos[i] = ((MetodosPago) selectedMet.get(i));
					if (mpagos[i].getMetodo().trim().equals(MetodosPagoCtrl.TARJETA)&& mpagos[i].getMoneda().trim().equals(ddlMoneda.getValue().toString())) {
						pNumero = Pattern.compile("^[0-9]+\\.?[0-9]*$");
						matNumero = pNumero.matcher(String.valueOf(mpagos[i].getMonto()));
						if (matNumero.matches()) {
							monto = monto + mpagos[i].getMonto();
						}
					}
				}
			if (monto > dMontoAplicar) {
				validado = false;
				txtMonto.setStyleClass("frmInput2Error");
				sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El monto ingresado para este método de pago debe ser menor o igual al monto a aplicar<br>";
				dwMensajeError.setWindowState("normal");
				y = y + 7;
			}
			else if (!polCtrl.validarMonto(Integer.parseInt(sCajaId), hFac.getId().getCodcomp(), ddlMoneda.getValue().toString(),ddlMetodosPago.getValue().toString(), monto)) {
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
				
			if(!chkIngresoManual.isChecked()){				
				String sTrack = track.getValue().toString();
				List lstDatosTrack = null,lstDatosTrack2 = null;
				if(sTrack.equals("")){//validar q pase la tarjeta en el lector
						validado = false;
						track.setStyleClass("frmInput2Error");
						sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> No se detectó la información de la tarjeta; Debe deslizar primero la tarjeta por el lector<br>";
						dwMensajeError.setWindowState("normal");
				}else{//validar que la lectura fue correcta
					lstDatosTrack = d.obtenerDatosTrack(sTrack);
					if(lstDatosTrack.size()<6){
						validado = false;
						track.setStyleClass("frmInput2Error");
						sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> No se leyó correctamente la información de la tarjeta!!!<br>";
						dwMensajeError.setWindowState("normal");
					}else if (lstDatosTrack.get(1) == null){
						validado = false;
						track.setStyleClass("frmInput2Error");
						sMensajeError = sMensajeError + "<img width=\"7\" src=\"/CRPMCAJA/theme/icons/redCircle.jpg\" border=\"0\" /> No se leyó correctamente el numero de tarjeta!!!<br>";
						dwMensajeError.setWindowState("normal");
					}else if(lstDatosTrack.get(4) == null){
						validado = false;
						track.setStyleClass("frmInput2Error");
						sMensajeError = sMensajeError + "<img width=\"7\" src=\"/CRPMCAJA/theme/icons/redCircle.jpg\" border=\"0\" /> No se leyó correctamente la fecha de vencimiento de la tarjeta!!!<br>";
						dwMensajeError.setWindowState("normal");
					}/*else if(lstDatosTrack.get(4) != null){
						if(lstDatosTrack.get(4).toString().length() < 4){
							validado = false;
							track.setStyleClass("frmInput2Error");
							sMensajeError = sMensajeError + "<img width=\"7\" src=\"/CRPMCAJA/theme/icons/redCircle.jpg\" border=\"0\" /> No se leyó correctamente la fecha de vencimiento de la tarjeta, long. menor a 4!!!<br>";
							dwMensajeError.setWindowState("normal");
						}
					}*/
					/* else if (ref2.equals("")) {//valida 4 ultimos digitos de la tarjeta
						validado = false;
						txtReferencia2.setStyleClass("frmInput2Error");
						sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> 4 Ultimos digitos de tarjeta requeridos<br>";
						dwProcesa.setWindowState("normal");
						y = y + 5;
					}*/ 
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
					dwMensajeError.setWindowState("normal");
					y = y + 5;
				}else if(ref2.length() > 16){
					validado = false;
					txtNoTarjeta.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Longitud muy larga (max. 16)<br>";
					dwMensajeError.setWindowState("normal");
					y = y + 5;
				}else if(!matAlfa2.matches()){
					validado = false;
					txtNoTarjeta.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" />El Número de tarjeta contiene caracteres invalidos<br>";
					dwMensajeError.setWindowState("normal");
					y = y + 5;
				}else if(ref3.equals("")){
					validado = false;
					txtFechaVenceT.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Fecha de vencimiento requerida<br>";
					dwMensajeError.setWindowState("normal");
					y = y + 5;
				}else if(ref3.length() > 4){
					validado = false;
					txtFechaVenceT.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Longitud muy larga (max. 4)<br>";
					dwMensajeError.setWindowState("normal");
					y = y + 5;
				}else if(!matNumero.matches()){
					validado = false;
					txtFechaVenceT.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> La fecha debe tener el formato MMYY (mes y año)<br>";
					dwMensajeError.setWindowState("normal");
					y = y + 5;
				}
			}///f
			lblMensajeError.setValue(sMensajeError);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return sMensajeError;
	}
/********************************************************************************************************************/	
/***********************************************************************************************************/	
	public List getLstAfiliadosSocketPOS(int iCaid, String sCodcomp, String sLineaFactura,String sMoneda){
		List Afiliados = null;
		List<SelectItem>lstPOS = new ArrayList<SelectItem>();
		Cafiliados[] cafiliado = null;
		AfiliadoCtrl afCtrl = new AfiliadoCtrl();
		String sDescrip="";
		List<Cafiliados> lstCafiliados = null;
		try {
			
			cafiliado = afCtrl.obtenerAfiliadoxCaja_Compania(iCaid, sCodcomp, sLineaFactura, sMoneda);
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
/***********************************************************************************************************/	
/*************************************************************************************************************/
	public void fijarMontoAplicado(ActionEvent ev){
		double dMontoAplicar = 0, monto = 0, dMontopend = 0;
		Divisas d = new Divisas();
		List lstPagos = new ArrayList(), lstComponentes = new ArrayList();
		String sMonedaBase = "", sMonto = "";
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		Pattern pNumero;
		Matcher matNumero = null;
		boolean validado = true;
		Credhdr hFac = null;
		try{
			lstComponentes = (List)m.get("lstComponentes");	
			//------- Validar los datos del monto.
			txtMontoAplicar.setStyleClass("frmInput2");
			pNumero = Pattern.compile("^[0-9]+\\.?[0-9]*$");
			sMonto = txtMontoAplicar.getValue().toString();
			matNumero = pNumero.matcher(sMonto);
			
			if(matNumero.matches()){
				monto = d.roundDouble(Double.parseDouble(sMonto));
				//sacar el monto pendiente de las facturas
				for(int s = 0 ; s < lstComponentes.size(); s++){
					hFac = (Credhdr)lstComponentes.get(s);
					dMontopend = dMontopend + hFac.getMontoPendiente().doubleValue();
				}
			}
			if (matNumero == null || !matNumero.matches() || monto == 0){
				validado = false;
				txtMontoAplicar.setValue("");
				txtMontoAplicar.setStyleClass("frmInput2Error");
				
				lblMensajeError.setValue("El valor del monto ingresado no es correcto!");
				dwMensajeError.setWindowState("normal");
				dwMensajeError.setStyle("width:390px;height:145px");
			}
			//el monto a aplicar no exeda el monto pendiente
			else if(monto > d.roundDouble(dMontopend)){
				txtMontoAplicar.setValue("");
				txtMontoAplicar.setStyleClass("frmInput2Error");
				
				lblMensajeError.setValue("El valor del monto ingresado debe ser menor al monto pendiente de las facturas seleccionadas!!!");
				dwMensajeError.setWindowState("normal");
				dwMensajeError.setStyle("width:390px;height:145px");
			}
			else{
				txtMontoAplicar.setStyleClass("frmInput2");
			}
			//
			if(validado){
				lstPagos = (List)m.get("lstPagosFinan");
				Credhdr c= (Credhdr)lstComponentes.get(0);
				//obtener companias x caja
				sMonedaBase = cCtrl.sacarMonedaBase((F55ca014[])m.get("cont_f55ca014"), c.getId().getCodcomp());
				
				dMontoAplicar = d.formatStringToDouble(txtMontoAplicar.getValue().toString());		
				m.put("finan_MontoAplicar", dMontoAplicar);
				
				
				//calcular el monto recibido en dependencia de moneda de factura y moneda de pago
				double montoRecibido = calcularElMontoRecibido(lstPagos,c,lstComponentes, sMonedaBase);						
				//distribuir el monto recibido a los montos a aplicar en factura
				distribuirMontoAplicar(sMonedaBase);		
				//determinar cambio
				determinarCambio(lstPagos,c,montoRecibido,sMonedaBase);
				
				gvSelectedCuotas.dataBind();
			}
			//
		}catch(Exception ex){
			LogCajaService.CreateLog("fijarMontoAplicado", "ERR", ex.getMessage());
		}
	}
	
/*************Cambiar el modo de operar a abono a principal**************************************************************************/		
	@SuppressWarnings("unchecked")
	public void setPrincipal(ValueChangeEvent ev){
		RowItem row = null;
		
		Finanhdr hFac = null;
		Finandet fd = null;
		
		List lstFacturasSelected = new ArrayList();
		List<Finanhdr> lstSelected = new ArrayList<Finanhdr>();
		List<MetodosPago> lstPagos = new ArrayList<MetodosPago>();
		List<Finandet> lstDetalle = new ArrayList<Finandet>();
		List<Credhdr> lstComponentes = new ArrayList<Credhdr>();
		List<Credhdr> lstCompEach = null;
		
		boolean bTieneCuotasPendientes = false;
		
		CuotaCtrl cuotaCtrl = new CuotaCtrl();
		BigDecimal bdInteres = BigDecimal.ZERO, dTasaPar =  BigDecimal.ZERO;
		double dMontoAplicar = 0,dMontoTotalAplicarDomestico = 0.0, dMontoTotalAplicarForaneo = 0.0;
		Divisas d = new Divisas();
		String sMonedaBase = "";
		
		try{
			
			F55ca014[] f14 = (F55ca014[])CodeUtil.getFromSessionMap("cont_f55ca014");
			
			lstPagos = (List<MetodosPago>)CodeUtil.getFromSessionMap("lstPagosFinan");
			 
			dTasaPar = obtenerTasaParalela("COR");
			lstFacturasSelected = gvCuotas.getSelectedRows();
		
			//&&====== Encabezado del financimiento, FinanHdr 
			for(int a = 0; a < lstFacturasSelected.size();a++){
				row = (RowItem) lstFacturasSelected.get(a);
				hFac = (Finanhdr) DataRepeater.getDataRow(row);
				lstSelected.add(hFac);
			}

			sMonedaBase = CompaniaCtrl.sacarMonedaBase(f14, hFac.getId().getCodcomp());

			if( chkPrincipal.isChecked() ){	
				
				lstFacturasSelected = cuotaCtrl.getCuotasVencidas(lstSelected);
				bTieneCuotasPendientes = (lstFacturasSelected != null && !lstFacturasSelected.isEmpty()) ;
				
				if(bTieneCuotasPendientes){
					chkPrincipal.setChecked(false);
					lblMensajeError.setValue("Hay cuotas vencidas no puede aplicar a principal!!!");
					dwMensajeError.setWindowState("normal");
					dwMensajeError.setStyle("width:390px;height:145px");
					return;
				}
				
				//&& =================== buscar la ultima cuota del financiamiento
				int numeroUltimaCuotaActual = 0; 
				List<Credhdr> componentesCuota = (List<Credhdr>) CodeUtil.getFromSessionMap ("lstComponentes");
				
				List<String> partidas = (ArrayList<String>)CodeUtil.selectPropertyListFromEntity(componentesCuota, "partida", true);
				
				Collections.sort(partidas) ;
				numeroUltimaCuotaActual = Integer.parseInt( partidas.get( partidas.size() -1 )  );
				 
				lstFacturasSelected = cuotaCtrl.getUltimaCuota(lstSelected);
				fd = (Finandet)lstFacturasSelected.get(0);
				
				if( numeroUltimaCuotaActual == (int) fd.getId().getNocuota() ){
					chkPrincipal.setChecked(false);
					lblMensajeError.setValue("No existen cuotas disponibles para abono a principal");
					dwMensajeError.setWindowState("normal");
					dwMensajeError.setStyle("width:390px;height:145px");
					CodeUtil.refreshIgObjects(dwRecibo);
					return;
				}
				
				CodeUtil.putInSessionMap("fn_NumeroUltimaCuotaCorriente", numeroUltimaCuotaActual);
				
				
				for(int i = 0; i < lstSelected.size(); i++){
					
					hFac = (Finanhdr)lstSelected.get(i);
					
					BigDecimal montoPendienteSinInteres = fd.getId().getMontopend().subtract( fd.getId().getInteres() ); 
					fd.setMontopend( montoPendienteSinInteres  );
					
					fd.setMontoAplicar(BigDecimal.ZERO);
					fd.setInteresPend(BigDecimal.ZERO);
					fd.setMora(BigDecimal.ZERO);
					fd.getId().setDiasmora(0);
					fd.setAbonoPrincipal(true); 
					
					lstDetalle.add(fd);
					if (fd.getId().getMoneda().compareTo(sMonedaBase) == 0){
						dMontoAplicar = dMontoAplicar + fd.getMontopend().doubleValue();
						dMontoTotalAplicarDomestico = dMontoTotalAplicarDomestico + fd.getMontopend().doubleValue();
					}else{
						dMontoAplicar = dMontoAplicar + fd.getMontopend().doubleValue();
						dMontoTotalAplicarForaneo = dMontoTotalAplicarForaneo + fd.getMontopend().doubleValue();
						dMontoTotalAplicarDomestico = dMontoTotalAplicarDomestico + (fd.getMontopend().doubleValue() * dTasaPar.doubleValue());
					}
				}
				
				
				lstComponentes = cuotaCtrl.documentosPorCuota(lstFacturasSelected, hFac, f14);
			}
			//si no esta seleccionado
			else{
				
				//buscar cuotas vencidas 
				lstFacturasSelected = cuotaCtrl.getCuotasVencidas(lstSelected);
				
				if(lstFacturasSelected != null && !lstFacturasSelected.isEmpty()){		
					
					cuotaCtrl.buscarMoraDeCuotas(lstFacturasSelected); 					
				}else{
					
					for(int i = 0;i < lstSelected.size();i++){					
					
						hFac = (Finanhdr)lstSelected.get(i);
						
						fd = cuotaCtrl.buscarSiguienteCuota(hFac.getId().getCodcomp(), hFac.getId().getCodsuc(), hFac.getId().getNosol(), 
															hFac.getId().getTiposol(), hFac.getId().getCodcli(),lstFacturasSelected);
						
						fd.setMora(BigDecimal.ZERO);
						fd.getId().setDiasmora(0);
						fd.setMontopend(fd.getId().getMontopend());
						fd.setMontoAplicar(BigDecimal.ZERO);
						fd.setAbonoPrincipal(false);
						
						//buscar interes corriente pendiente
						if(!(fd.getMontopend().doubleValue() < fd.getId().getMonto().doubleValue())){
							bdInteres = cuotaCtrl.buscarInteresCorrientePend(fd);
							if(bdInteres.doubleValue() == 0 && fd.getId().getAticu() == 0){
								bdInteres = fd.getId().getImpuesto().add(fd.getId().getInteres());
							}
						}
						
						fd.setInteresPend(bdInteres);
						
						lstDetalle.add(fd);
												
						if (fd.getId().getMoneda().equals(sMonedaBase)){
							dMontoAplicar = dMontoAplicar + fd.getMontopend().doubleValue();
							dMontoTotalAplicarDomestico = dMontoTotalAplicarDomestico + fd.getMontopend().doubleValue();
						}else{
							dMontoAplicar = dMontoAplicar + fd.getMontopend().doubleValue();
							dMontoTotalAplicarForaneo = dMontoTotalAplicarForaneo + fd.getMontopend().doubleValue();
							dMontoTotalAplicarDomestico = dMontoTotalAplicarDomestico + (fd.getMontopend().doubleValue()*dTasaPar.doubleValue());
						}
					} 
				
					
					Vautoriz vaut =  ((Vautoriz[])m.get("sevAut"))[0];
					BigDecimal tasaoficial = obtenerTasaOficial();
					
					String msgCreaInteres = crearFacturasPorIntereses2( lstSelected,  lstDetalle, vaut.getId().getLogin() );
					
					if( !msgCreaInteres.isEmpty() ){
						return ;
					}

					//&& ========= Buscar el detalle de cuotas en el f0311 (moratorios, corriente,saldo )
					for(int i = 0;i < lstSelected.size();i++){					
						
						hFac = (Finanhdr)lstSelected.get(i);
						lstCompEach = cuotaCtrl.documentosPorCuota(lstDetalle, hFac, f14);
						
						if(lstCompEach != null ){
							lstComponentes.addAll(lstCompEach);
						}
					 
					}					  
					CodeUtil.putInSessionMap("lstComponentes",lstComponentes);
				}
			} 
			
			CodeUtil.putInSessionMap("lstSelectedCuotas", lstDetalle);		
			CodeUtil.putInSessionMap("lstComponentes", lstComponentes);	
			
			Credhdr c= (Credhdr)lstComponentes.get(0);
			
			double montoRecibido = calcularElMontoRecibido(lstPagos,c,lstComponentes, sMonedaBase);						
			 
			distribuirMontoAplicar(sMonedaBase);		
			 
			determinarCambio(lstPagos,c,montoRecibido,sMonedaBase);
			
			gvMetodosPago.dataBind();
			gvSelectedCuotas.dataBind();
			
			if(fd.getId().getMoneda().compareTo(sMonedaBase) == 0 ){ 
				montoTotalAplicarForaneo.setStyle("display:none");
				montoTotalFaltanteForaneo.setStyle("display:none");
				montoTotalAplicarDomestico.setValue("<B>"+sMonedaBase+"</B> " + d.formatDouble(dMontoTotalAplicarDomestico));
				montoTotalFaltanteDomestico.setValue("<B>"+sMonedaBase+"</B> " + d.formatDouble(dMontoTotalAplicarDomestico));
			}else{
				montoTotalAplicarForaneo.setStyle("display:inline");
				montoTotalFaltanteForaneo.setStyle("display:inline");
				montoTotalAplicarForaneo.setValue("<B>" + fd.getId().getMoneda() + "</B>" +" "+ d.formatDouble(dMontoTotalAplicarForaneo));
				montoTotalFaltanteForaneo.setValue("<B>" + fd.getId().getMoneda() + "</B>" +" "+ d.formatDouble(dMontoTotalAplicarForaneo));
				montoTotalAplicarDomestico.setValue("<B>"+sMonedaBase+"</B> " + d.formatDouble(dMontoTotalAplicarDomestico));
				montoTotalFaltanteDomestico.setValue("<B>"+sMonedaBase+"</B> " + d.formatDouble(dMontoTotalAplicarDomestico));
			}
			
			intSelectedDet.setValue(lstComponentes.size());

			
			CodeUtil.refreshIgObjects(new Object[]{
				gvMetodosPago, gvSelectedCuotas, 
				montoTotalAplicarForaneo,
				intSelectedDet,lblMontoAplicar,txtMontoAplicar,lblMontoRecibido,
				txtMontoRecibido,lblCambio,
				lblCambioDomestico,txtCambioDomestico
			}) ;
			
			
			
		}catch(Exception ex){
			ex.printStackTrace(); 
		}finally{
			CodeUtil.refreshIgObjects(dwMensajeError);
		}
	}
	
	
	//&& ==================== no en uso ==================================//
	public void setPrincipal_NOENUSO(ValueChangeEvent ev){
		RowItem row = null;
		Finanhdr hFac = null;
		Finandet fd = null;
		List lstFacturasSelected = new ArrayList(),lstSelected = new ArrayList(),lstPagos = new ArrayList(),lstDetalle = new ArrayList();
		List<Credhdr> lstComponentes = new ArrayList<Credhdr>(), lstCompEach = null;
		boolean bMora = false, bPaso = false;
		CuotaCtrl cuotaCtrl = new CuotaCtrl();
		BigDecimal bdInteres = BigDecimal.ZERO,dTasaPar =  BigDecimal.ZERO;
		double dMontoAplicar = 0,dMontoTotalAplicarDomestico = 0.0, dMontoTotalAplicarForaneo = 0.0;
		Divisas d = new Divisas();
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		String sMonedaBase = "";

		try{
			lstPagos = (List)m.get("lstPagosFinan");
			//informacion de caja
			Vf55ca01 f55ca01 = (Vf55ca01) ((List) m.get("lstCajas")).get(0);
			//
			dTasaPar = obtenerTasaParalela("COR");
			lstFacturasSelected = gvCuotas.getSelectedRows();
			//sacar lista de finanhdr
			for(int a = 0; a < lstFacturasSelected.size();a++){
				row = (RowItem) lstFacturasSelected.get(a);
				hFac = (Finanhdr) gvCuotas.getDataRow(row);
				lstSelected.add(hFac);
			}

			//obtener companias x caja
			sMonedaBase = cCtrl.sacarMonedaBase((F55ca014[])m.get("cont_f55ca014"), hFac.getId().getCodcomp());

			if(chkPrincipal.isChecked()){//si esta seleccionado	
				//verificar si hay mora
				lstFacturasSelected = cuotaCtrl.getCuotasVencidas(lstSelected);
				if(lstFacturasSelected != null && !lstFacturasSelected.isEmpty()){//tiene mora
					bMora = true;
				}
				//fin de validacion de mora		
				if(!bMora){
					//limpiar pagos
					//m.put("lstPagosFinan", lstPagos);					
					//buscar la ultima cuota del financiamiento
					lstFacturasSelected = cuotaCtrl.getUltimaCuota(lstSelected);
					fd = (Finandet)lstFacturasSelected.get(0);
					
					for(int i = 0;i < lstSelected.size();i++){					
						hFac = (Finanhdr)lstSelected.get(i);
						fd.setMora(BigDecimal.ZERO);
						fd.getId().setDiasmora(0);
						fd.setMontopend(fd.getId().getMontopend());
						fd.setMontoAplicar(BigDecimal.ZERO);
						//buscar interes corriente pendiente
						if(!(fd.getMontopend().doubleValue() < fd.getId().getMonto().doubleValue())){
							bdInteres = cuotaCtrl.buscarInteresCorrientePend(fd);
							if(bdInteres.doubleValue() == 0 && fd.getId().getAticu() == 0){
								bdInteres = fd.getId().getImpuesto().add(fd.getId().getInteres());
							}
						}
						
						fd.setInteresPend(bdInteres);
						
						lstDetalle.add(fd);
						if (fd.getId().getMoneda().equals(sMonedaBase)){
							dMontoAplicar = dMontoAplicar + fd.getMontopend().doubleValue();
							dMontoTotalAplicarDomestico = dMontoTotalAplicarDomestico + fd.getMontopend().doubleValue();
						}else{
							dMontoAplicar = dMontoAplicar + fd.getMontopend().doubleValue();
							dMontoTotalAplicarForaneo = dMontoTotalAplicarForaneo + fd.getMontopend().doubleValue();
							dMontoTotalAplicarDomestico = dMontoTotalAplicarDomestico + (fd.getMontopend().doubleValue()*dTasaPar.doubleValue());
						}
					}
					//buscar componentes de ultima cuota
					//generar if para cuota
					generarIFCuota(fd, hFac, sMonedaBase,f55ca01);
					//Buscar el detalle de cuotas en el f0311 (moratorios, corriente,saldo )
					F55ca014[] f14 = (F55ca014[])m.get("cont_f55ca014");
					lstComponentes = cuotaCtrl.leerDocumentosxCuota(fd,hFac,f14);
					
				}else{
					bPaso = true;
					chkPrincipal.setChecked(false);
					lblMensajeError.setValue("Hay cuotas vencidas no puede aplicar a principal!!!");
					dwMensajeError.setWindowState("normal");
					dwMensajeError.setStyle("width:390px;height:145px");
				}
			}
			//si no esta seleccionado
			else{
				//buscar cuotas vencidas 
				lstFacturasSelected = cuotaCtrl.getCuotasVencidas(lstSelected);
				if(lstFacturasSelected != null && !lstFacturasSelected.isEmpty()){//buscar intereses moratorios de la cuota					
					lstFacturasSelected = cuotaCtrl.buscarMoraExistente(lstFacturasSelected);				
				}else{
					
					for(int i = 0;i < lstSelected.size();i++){					
						hFac = (Finanhdr)lstSelected.get(i);
						fd = cuotaCtrl.buscarSiguienteCuota(hFac.getId().getCodcomp(), hFac.getId().getCodsuc(), hFac.getId().getNosol(), hFac.getId().getTiposol(), hFac.getId().getCodcli(),lstFacturasSelected);
						fd.setMora(BigDecimal.ZERO);
						fd.getId().setDiasmora(0);
						fd.setMontopend(fd.getId().getMontopend());
						fd.setMontoAplicar(BigDecimal.ZERO);
						//buscar interes corriente pendiente
						if(!(fd.getMontopend().doubleValue() < fd.getId().getMonto().doubleValue())){
							bdInteres = cuotaCtrl.buscarInteresCorrientePend(fd);
							if(bdInteres.doubleValue() == 0 && fd.getId().getAticu() == 0){
								bdInteres = fd.getId().getImpuesto().add(fd.getId().getInteres());
							}
						}
						
						fd.setInteresPend(bdInteres);
						
						lstDetalle.add(fd);
												
						if (fd.getId().getMoneda().equals(sMonedaBase)){
							dMontoAplicar = dMontoAplicar + fd.getMontopend().doubleValue();
							dMontoTotalAplicarDomestico = dMontoTotalAplicarDomestico + fd.getMontopend().doubleValue();
						}else{
							dMontoAplicar = dMontoAplicar + fd.getMontopend().doubleValue();
							dMontoTotalAplicarForaneo = dMontoTotalAplicarForaneo + fd.getMontopend().doubleValue();
							dMontoTotalAplicarDomestico = dMontoTotalAplicarDomestico + (fd.getMontopend().doubleValue()*dTasaPar.doubleValue());
						}
					}//fin de for
					//Generar MF e IF
					generarMF_IF(lstDetalle,lstSelected,sMonedaBase, f55ca01);
					//Buscar el detalle de cuotas en el f0311 (moratorios, corriente,saldo )
					F55ca014[] f14 = (F55ca014[])m.get("cont_f55ca014");
					for(int i = 0;i < lstSelected.size();i++){					
						hFac = (Finanhdr)lstSelected.get(i);
						lstCompEach = cuotaCtrl.leerDocumentosxCuotas(lstFacturasSelected,hFac,f14);
						for (Iterator iterator = lstCompEach.iterator(); iterator.hasNext();) {
							Credhdr credhdr = (Credhdr) iterator.next();
							lstComponentes.add(credhdr);
						}
					}					  
					m.put("lstComponentes",lstComponentes);
				}
			}//fin de no esta seleccionado
			//solo se ejecutan estos metodos si se logro hacer la operacion
			if(!bPaso){
				m.put("lstSelectedCuotas", lstDetalle);		
				m.put("lstComponentes", lstComponentes);	
				Credhdr c= (Credhdr)lstComponentes.get(0);
				//calcular el monto recibido en dependencia de moneda de factura y moneda de pago
				double montoRecibido = calcularElMontoRecibido(lstPagos,c,lstComponentes, sMonedaBase);						
				//distribuir el monto recibido a los montos a aplicar en factura
				distribuirMontoAplicar(sMonedaBase);		
				//determinar cambio
				determinarCambio(lstPagos,c,montoRecibido,sMonedaBase);
				
				gvMetodosPago.dataBind();
				gvSelectedCuotas.dataBind();
				
				if(!fd.getId().getMoneda().equals(sMonedaBase)){//resumen de moneda foranea
					
					montoTotalAplicarForaneo.setStyle("display:inline");
					montoTotalFaltanteForaneo.setStyle("display:inline");
					montoTotalAplicarForaneo.setValue("<B>" + fd.getId().getMoneda() + "</B>" +" "+ d.formatDouble(dMontoTotalAplicarForaneo));
					montoTotalFaltanteForaneo.setValue("<B>" + fd.getId().getMoneda() + "</B>" +" "+ d.formatDouble(dMontoTotalAplicarForaneo));
					montoTotalAplicarDomestico.setValue("<B>"+sMonedaBase+"</B> " + d.formatDouble(dMontoTotalAplicarDomestico));
					montoTotalFaltanteDomestico.setValue("<B>"+sMonedaBase+"</B> " + d.formatDouble(dMontoTotalAplicarDomestico));
				}else{//resumen de moneda domestica
					montoTotalAplicarForaneo.setStyle("display:none");
					montoTotalFaltanteForaneo.setStyle("display:none");
					montoTotalAplicarDomestico.setValue("<B>"+sMonedaBase+"</B> " + d.formatDouble(dMontoTotalAplicarDomestico));
					montoTotalFaltanteDomestico.setValue("<B>"+sMonedaBase+"</B> " + d.formatDouble(dMontoTotalAplicarDomestico));
				}
				intSelectedDet.setValue(lstComponentes.size());
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
/*****************************************************************************************************************/		
	public boolean generarIFCuota(Finandet fd,Finanhdr fh, String sMonedaBase,Vf55ca01 f55ca01){
		boolean bCorrecto = true;
		Connection cn = null;
		BigDecimal bdTasaJde;
		Vautoriz[] vautoriz = null;
		FechasUtil fUtil = new FechasUtil();
		int iFecha = 0;
		try{
			//obtener tasa oficial del dia		
			bdTasaJde = obtenerTasaOficial();
			//obtener fecha actual juliana		
			iFecha = fUtil.obtenerFechaActualJuliana();
			vautoriz = (Vautoriz[]) m.get("sevAut");
		
			if(fh.getId().getNosol() == fd.getId().getNosol() && fd.getId().getAticu() == 0){
				bCorrecto = generarIF(cn,fh, fd, bdTasaJde, vautoriz[0].getId().getLogin(), vautoriz[0].getId().getNomapp(), iFecha,f55ca01,vautoriz[0],sMonedaBase);
				
				try {
					
					String origen = PropertiesSystem.CONTEXT_NAME+": "
							+ new Exception().getStackTrace()[1].getClassName() +":"	
							+ new Exception().getStackTrace()[1].getMethodName() ;
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
			}
						
		//termina de generar los IF
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return bCorrecto;
	}
/*****************************************************************************************************************/	
	public boolean actualizarPagoModuloFinan(Session session, List lstSelectedFacs, List lstComponents, 
			String usuario, TipoDocumentoFinanciamiento[] lstTipoDocFinanciamiento){
		boolean bHecho = true;
		double dMontoFinanF = 0, dMontoFinanD = 0;
		int iMontoAplicarD = 0, iMontoAplicarF = 0, nocuota;
		CuotaCtrl cCtrl = new CuotaCtrl();
		Finandet fd = null;
		Credhdr cr = null;
		
		try{
			 
			for(int i = 0; i < lstSelectedFacs.size(); i ++){
				
				fd = (Finandet)lstSelectedFacs.get(i);
				
				dMontoFinanD = fd.getId().getAtaap().doubleValue();
				dMontoFinanF = fd.getId().getAtfap().doubleValue();
				
				
				//Obtiene el tipo de documentos para intereses corrientes e intereses moratorios
				Boolean hayDocumentoFinanciadoConfMF = false;
				Boolean hayDocumentoFinanciadoConfIF = false;
				String strTipoDocFinanciamientoMF = "";
				String strTipoDocFinanciamientoIF = "";
				
				for(Object o : lstTipoDocFinanciamiento)
				{
					TipoDocumentoFinanciamiento tipoDocFinan = (TipoDocumentoFinanciamiento)o;
					
					//Obtener tipo de documentos intereses moratorios
					if(tipoDocFinan.getTipo_agrupacion().trim().equals("IMO") && 
					   tipoDocFinan.getCod_compania().trim().equals(fd.getId().getCodcomp().trim()) && 
					   hayDocumentoFinanciadoConfMF==false)
					{
						strTipoDocFinanciamientoMF = tipoDocFinan.getCod_documento().trim();
						hayDocumentoFinanciadoConfMF=true;
						continue;
					}
					
					//Obtener tipo de documentos intereses corrientes
					if(tipoDocFinan.getTipo_agrupacion().trim().equals("ICR") && 
					   tipoDocFinan.getCod_compania().trim().equals(fd.getId().getCodcomp().trim()) && 
					   hayDocumentoFinanciadoConfIF==false)
					{
						strTipoDocFinanciamientoIF = tipoDocFinan.getCod_documento().trim();
						hayDocumentoFinanciadoConfIF=true;
						continue;
					}
					
					if(hayDocumentoFinanciadoConfIF && hayDocumentoFinanciadoConfMF)
						break;
				}
				
				if(hayDocumentoFinanciadoConfIF==false || hayDocumentoFinanciadoConfMF==false)
					return false;
				
			 
				for(int j = 0;j < lstComponents.size(); j++){
					cr = (Credhdr)lstComponents.get(j);
					
					if( cr.isFinancimientoAsociado() )
					{					
						
						continue;
					}
					
					nocuota = Integer.parseInt(cr.getId().getPartida());
					
					if(nocuota == fd.getId().getNocuota() && !cr.getId().getTipofactura().trim().equals(strTipoDocFinanciamientoMF)){
						
						dMontoFinanF = Divisas.roundDouble(dMontoFinanF - cr.getMontoAplicar().doubleValue());
						 
						if(dMontoFinanF == 0){
							dMontoFinanD = 0 ;
							 
						}else{
							
							dMontoFinanD = 
								new BigDecimal( String.valueOf( dMontoFinanD) ).subtract(
											cr.getMontoAplicar().multiply(fd.getId().getTasa()) 
										).setScale(2, RoundingMode.HALF_UP).doubleValue();
						}
					}
				}
				
				iMontoAplicarD = Divisas.pasarAentero(dMontoFinanD);
				iMontoAplicarF = Divisas.pasarAentero(dMontoFinanF);
				 
				bHecho = cCtrl.aplicarPagoCuotaModFinan(session, fd, iMontoAplicarD, iMontoAplicarF, usuario);
				 
			}
			
		}catch(Exception ex){
			bHecho = false;
			ex.printStackTrace(); 
		}
		return bHecho;
	}
	
	//------------------------------------------------------------------
	//Creado por LFonseca
	//------------------------------------------------------------------
	
	/** 
	 * Metodo: crear documentos por intereses para el financimiento
	 * Fecha: 27 Marzo 2017
	 * Creado: Carlos Hernandez
	 */
	
	
	public static String crearFacturasPorIntereses2(List<Finanhdr> financiamientos, 
			List<Finandet>cuotasPorFinanciamientos, 
			String usuario){
		
		String msgProceso = "" ;

		try 
		{
			Parameter paramCodigoCliente = new Parameter();
		    Parameter paramCodigoCompania = new Parameter();
		    Parameter paramCodigoSucursal = new Parameter();
		    Parameter paramNumeroDocumento = new Parameter();
		    Parameter paramTipoDocumento = new Parameter();
		    Parameter paramCuota = new Parameter();      
		    Parameter paramUsuario = new Parameter();      
		    Parameter paramPantalla = new Parameter();
		    Parameter paramPrograma = new Parameter();
		    
		    for (final Finandet finandet : cuotasPorFinanciamientos) {
		    	paramCodigoCliente.addValue(String.valueOf(financiamientos.get(0).getId().getCodcli()));
				paramCodigoCompania.addValue(finandet.getId().getCodcomp());
				paramCodigoSucursal.addValue(finandet.getId().getCodsuc());
				paramNumeroDocumento.addValue(String.valueOf(finandet.getId().getNosol()));
				paramTipoDocumento.addValue(finandet.getId().getTiposol());
				paramCuota.addValue(String.valueOf(finandet.getId().getNocuota()));
				paramUsuario.addValue(usuario);
				paramPantalla.addValue("GCPMCAJA");
				paramPrograma.addValue("WSCANALES");
				
		    }

		    ClsF5503B11 clsF5503b11 = new ClsF5503B11();
			RestResponse resultado = clsF5503b11.procesarGenerarFacturaInteres(paramCodigoCliente, 
					  															 paramCodigoCompania, 
					  															 paramCodigoSucursal, 
					  															 paramNumeroDocumento, 
					  															 paramTipoDocumento, 
					  															 paramCuota, 
					  															 paramUsuario, 
					  															 paramPantalla, 
					  															 paramPrograma);
			 
			if(!resultado.getDataAsString().equals("S"))
				msgProceso=resultado.getDataAsString();
			
		} catch (Exception e) {
			e.printStackTrace();
			LogCajaService.CreateLog("crearFacturasPorIntereses2", "ERR", e.getMessage());
			msgProceso = "No se pudieron generar los intereses Corrientes o Moratorios, WS no esta respondiendo";
			return msgProceso;
		} 
		return msgProceso;
	}
	
	//-----------------------------------------------------------------
	//Termina ajuste de LFonseca
	//------------------------------------------------------------------
	
	/** 
	 * Metodo: crear documentos por intereses para el financimiento
	 * Fecha: 27 Marzo 2017
	 * Creado: Carlos Hernandez
	 */
	
	
	public static String crearFacturasPorIntereses(List<Finanhdr> financiamientos, 
			List<Finandet>cuotasPorFinanciamientos, 
			String monedaBase, BigDecimal tasaoficial, 
			String usuario, String monedaNacional){
		
		String msgProceso = "" ;
		
		String tipoInteresCorriente = "FI00";
		String tipoInteresMoratorio = "FI05";
		
		try {
			
			CollectionUtils.forAllDo(cuotasPorFinanciamientos, new Closure(){
				public void execute(Object o) {
				 Finandet finandet = (Finandet)o;
					finandet.setNumeroBatchIF( String.valueOf( finandet.getId().getAticu() ) );
					finandet.setNumeroBatchMF( String.valueOf( finandet.getId().getAticum() )  );
					finandet.setDiasVencidos( String.valueOf(finandet.getId().getDiasven() ));
				}
			} );
			
			Finanhdr finan = financiamientos.get(0) ;
			
			int codigoClientePadre = com.casapellas.controles.EmpleadoCtrl.addressNumberParent( finan.getId().getCodcli() );
			Vf0101 dtaCliente = EmpleadoCtrl.buscarEmpleadoxCodigo2( finan.getId().getCodcli() );
			
			String codigoCategoria08 = dtaCliente.getId().getAbac08();
			
			List<String[]> idCuentasPorCuotas = CuotaCtrl.idCuentaContablePagoFactura(cuotasPorFinanciamientos);
			
			List<String[]> cuentaInteresesCorriente = CuotaCtrl.cuentaIntereses( finan, tipoInteresCorriente);
			if( cuentaInteresesCorriente == null )
				return msgProceso = "Cuenta de interes corriente No encontrada para " 
						+ Integer.parseInt( finan.getId().getCodsuc() ) + finan.getId().getLinea()  ; 
			
			List<String[]> cuentaInteresesMoratorio = CuotaCtrl.cuentaIntereses( finan, tipoInteresMoratorio);
			if( cuentaInteresesMoratorio == null )
				return msgProceso = "Cuenta de interes moratorios No encontrada para " 
						+ Integer.parseInt( finan.getId().getCodsuc() ) + finan.getId().getLinea()  ; 
			
			List<Finandet> cuotasInteresGenerado = new ArrayList<Finandet>();
			
			
			for (final Finanhdr finanhdr : financiamientos) {
				
				
				@SuppressWarnings("unchecked")
				List<Finandet>  cuotasGenerarCorrientes = (ArrayList<Finandet>)
				CollectionUtils.select(cuotasPorFinanciamientos, new Predicate() {
					public boolean evaluate(Object o) {
						Finandet cuota = (Finandet)o;
						return finanhdr.getId().getNosol() == cuota.getId().getNosol() && cuota.getId().getAticu() == 0 ;
					}
				});
				
				if( cuotasGenerarCorrientes != null  && !cuotasGenerarCorrientes.isEmpty() ){
					
					for (final Finandet cuota : cuotasGenerarCorrientes) {
						
						String[] dtaCuentaFactura = (String[] )
						CollectionUtils.find( idCuentasPorCuotas , new Predicate(){
							 
							public boolean evaluate(Object o) {
								 
								String prefijo = "RC" +  cuota.getId().getClasecontable().trim() ;
								String[] dta = (String[])o ;
								
								return 
								dta[1].toLowerCase().compareTo( prefijo.toLowerCase() ) == 0  && 
								dta[2].compareTo( cuota.getId().getCodsuc() ) == 0 ;
							}
							
							
						}) ;
						
						if(dtaCuentaFactura == null){
							return msgProceso = "No se ha encontrado cuenta para factura ";
						}
						
						msgProceso = CuotaCtrl.crearInteresCuota(finanhdr, cuota,  "IF", cuota.getId().getNocuota(), cuota.getId().getNosol(), 
													cuota.getId().getInteres(), tasaoficial, usuario, monedaNacional, 
													dtaCuentaFactura[0], cuentaInteresesCorriente, codigoClientePadre, codigoCategoria08 );
						
						if(!msgProceso.isEmpty()){
							return msgProceso;
						}
						
						cuotasInteresGenerado.add(cuota);
						
					}	
					
				}
				
				//&& ============= buscar y generar los intereses moratorios 
				@SuppressWarnings("unchecked")
				List<Finandet>  cuotasGenerarMoratorios = (ArrayList<Finandet>)
				CollectionUtils.select(cuotasPorFinanciamientos, new Predicate() {
					public boolean evaluate(Object o) {
						Finandet cuota = (Finandet)o;
						return finanhdr.getId().getNosol() == cuota.getId().getNosol() &&  cuota.getId().getMora().compareTo(BigDecimal.ZERO) == 1  ;
					}
				});
			 
				if( cuotasGenerarMoratorios != null  && !cuotasGenerarMoratorios.isEmpty() ){
					
					for (final Finandet cuota : cuotasGenerarMoratorios) {
						
						String[] dtaCuentaFactura = (String[] )
						CollectionUtils.find( idCuentasPorCuotas , new Predicate(){
							 
							public boolean evaluate(Object o) {
								 
								String prefijo = "RC" +  cuota.getId().getClasecontable().trim() ;
								String[] dta = (String[])o ;
								
								return 
								dta[1].toLowerCase().compareTo( prefijo.toLowerCase() ) == 0  && 
								dta[2].compareTo( cuota.getId().getCodsuc() ) == 0 ;
							}
						}) ;
						
						
						if(dtaCuentaFactura == null){
							return msgProceso = "No se ha encontrado cuenta para factura ";
						}
						
						msgProceso = CuotaCtrl.crearInteresCuota(finanhdr, cuota, "MF", cuota.getId().getNocuota(), cuota.getId().getNosol(), 
													cuota.getId().getMora(), tasaoficial, usuario,  monedaNacional, 
													dtaCuentaFactura[0], cuentaInteresesMoratorio, codigoClientePadre, codigoCategoria08 );
						
						if(!msgProceso.isEmpty()){
							return msgProceso;
						}
						
						cuotasInteresGenerado.add(cuota);
						
					}	
					
				}
			}
			
			if( !cuotasInteresGenerado.isEmpty() ){
			
 
				Set<Finandet> cuotasFinanUpdate = new HashSet<Finandet>(cuotasInteresGenerado) ;
				for (Finandet finandet : cuotasFinanUpdate) {
					 	
					if( finandet.getNumeroBatchIF().compareTo("0") == 0 && finandet.getNumeroBatchMF().compareTo("0") == 0 )
						continue;
					
					ClsF5503B11 clsf5503am = new ClsF5503B11();
					String str[] = clsf5503am.ActualizarPlanFinanciamiento(financiamientos.get(0).getId().getCodcli(), 
																		   finandet.getId().getCodsuc(), 
																		   finandet.getId().getNosol(), 
																		   finandet.getId().getTiposol(), 
																		   Integer.valueOf(finandet.getId().getNocuota()), 
																		   Integer.valueOf(finandet.getDiasVencidos()), 
																		   Integer.valueOf(finandet.getNumeroBatchIF()), 
																		   Integer.valueOf(finandet.getNumeroBatchMF()), 
																		   usuario);
					
 
					if(str[0].trim().equals("N"))
					{
						return msgProceso = "No se pudo actualizar los batch correspondiente a IF y MF";
					}
 
					
				}
 
			}
			 
			
		} catch (Exception e) {
			e.printStackTrace(); 
			return msgProceso = "fallo en interfaz Edwards ";
		} 
		return msgProceso;
	}
	
	
/*****************************************************************************************************************/	
	public void generarMF_IF(List lstFacturasSelected, List lstCreditosFinan, String sMonedaBase, Vf55ca01 f55ca01){
		Finanhdr fh = null;
		Finandet fd = null;
		boolean bCorrecto = true;
		Connection cn = null;
		BigDecimal bdTasaJde;
		Vautoriz[] vautoriz = null;
		FechasUtil fUtil = new FechasUtil();
		int iFecha = 0;
		try{
			//obtener tasa oficial del dia		
			bdTasaJde = obtenerTasaOficial();
			//obtener fecha actual juliana		
			iFecha = fUtil.obtenerFechaActualJuliana();
			vautoriz = (Vautoriz[]) m.get("sevAut");
			
		//generar MF de credito si no esxisten
		for(int j = 0; j < lstCreditosFinan.size(); j++){
			fh = (Finanhdr)lstCreditosFinan.get(j);
			for(int i = 0; i < lstFacturasSelected.size();i++){
				fd = (Finandet)lstFacturasSelected.get(i);
				if(fh.getId().getNosol() == fd.getId().getNosol() && fd.getId().getMora().doubleValue()>0 ){
					bCorrecto = generarMF(cn,fh, fd, bdTasaJde, vautoriz[0].getId().getLogin(), vautoriz[0].getId().getNomapp(), iFecha,f55ca01,vautoriz[0],sMonedaBase);
					if(!bCorrecto){
						break;
					}
				}
			}
			if(!bCorrecto){
				break;
			}
		}	
		//termina generar MF
		
		//generar IF para los adelantos de cuota si no existen
		if(bCorrecto){
			for(int a = 0; a < lstCreditosFinan.size(); a++){
				fh = (Finanhdr)lstCreditosFinan.get(a);
				for(int b = 0; b < lstFacturasSelected.size();b++){
					fd = (Finandet)lstFacturasSelected.get(b);
					if(fh.getId().getNosol() == fd.getId().getNosol() && fd.getId().getAticu() == 0){
						bCorrecto = generarIF(cn,fh, fd, bdTasaJde, vautoriz[0].getId().getLogin(), vautoriz[0].getId().getNomapp(), iFecha,f55ca01,vautoriz[0],sMonedaBase);
						if(!bCorrecto){
							break;
						}
					}
				}
				if(!bCorrecto){
					break;
				}
			}			
		}
		//termina de generar los IF
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@SuppressWarnings({"unchecked", "static-access", "rawtypes"})
	public void agregarCuotaRecibo(ActionEvent e){
	
		Finandet f = null;
		List<Credhdr> lstComponentes;
		List<Finandet>lstFacturasSelected;
		List<Finandet>lstNewCuotas;
		List<MetodosPago>lstPagos;
		
		CuotaCtrl cuotaCtrl= new CuotaCtrl();
		BigDecimal bdInteres = BigDecimal.ZERO;
		BigDecimal dTasaPar =  BigDecimal.ZERO;
		double dMontoAplicar = 0,dMontoTotalAplicarDomestico = 0.0, dMontoTotalAplicarForaneo = 0.0,dMontoRecibido = 0.0, dMonto = 0.0;
		Divisas d = new Divisas();

		CompaniaCtrl cCtrl = new CompaniaCtrl();
		String sMonedaBase = "";
		
		List<Finandet> cuotaSeleccionada = new ArrayList<Finandet>();

		try{
			
			boolean adelantoPrincipal = chkPrincipal.isChecked() ;
			
			RowItem ri = (RowItem) e.getComponent().getParent().getParent();
			f = (Finandet) gvAgregarCuota.getDataRow(ri);
			
			List<Finanhdr> financimientos = (ArrayList<Finanhdr>)CodeUtil.getFromSessionMap("lstCreditosFinan");
			Finanhdr fh = financimientos.get(0);

			lstPagos     = (ArrayList<MetodosPago>)CodeUtil.getFromSessionMap("lstPagosFinan");
			lstNewCuotas = (ArrayList<Finandet>)CodeUtil.getFromSessionMap("lstAgregarCuota");
			
			
			F55ca014[] f14 = (F55ca014[])CodeUtil.getFromSessionMap("cont_f55ca014");
			
			Vf55ca01 f55ca01 =  ((List<Vf55ca01>)CodeUtil.getFromSessionMap("lstCajas")).get(0);
			sMonedaBase = cCtrl.sacarMonedaBase( f14 , f.getId().getCodcomp() );
			
			dTasaPar = this.obtenerTasaParalela("COR");
			
			double montoRecibido = d.formatStringToDouble(txtMontoRecibido.getValue().toString());
			
			if(lstNewCuotas.size() == 1 ){
				CodeUtil.putInSessionMap("lstAgregarCuota", new ArrayList<Finandet>());
				dwAgregarCuota.setWindowState("hidden");
			}
			if(lstNewCuotas.size() > 1){
				for(int i = lstNewCuotas.size()-1; i >= 0; i--){
					Finandet f2 = lstNewCuotas.get(i);
					if( f.getId().getNosol() == f2.getId().getNosol() &&
						f.getId().getNocuota() == f2.getId().getNocuota()){
						lstNewCuotas.remove(i);
						break;
					}
				}
				CodeUtil.putInSessionMap("lstAgregarCuota",lstNewCuotas);
				gvAgregarCuota.dataBind();
			}
			
			//agregar cuota a cuotas seleccionadas
			lstFacturasSelected = (List<Finandet>) CodeUtil.getFromSessionMap("lstSelectedCuotas");
			lstComponentes = (ArrayList<Credhdr>) CodeUtil.getFromSessionMap ("lstComponentes");
			
			
			f.setMora(BigDecimal.ZERO);
			f.getId().setMora(BigDecimal.ZERO);
			f.getId().setDiasmora(0);
			f.getId().setDiasven((short)0);
			f.setMontoAplicar(BigDecimal.ZERO);
			f.setAbonoPrincipal(adelantoPrincipal);
			
			cuotaSeleccionada.add(f);
			
			//&& =============== Si se esta haciendo adelanto al saldo principal, no generar Intereses (IF) y restarlos del monto pendiente en f5503am
			if(adelantoPrincipal){
				
				BigDecimal montoPendienteSinInteres = f.getId().getMontopend().subtract( f.getId().getInteres() );
				 
				f.setMontopend( montoPendienteSinInteres );
				f.setInteresPend(BigDecimal.ZERO);
				f.setMora(BigDecimal.ZERO);
				
			}else{
				
				f.setMontopend(f.getId().getMontopend());
				
				if( f.getMontopend().compareTo( f.getId().getMonto() )  == -1 ){
					bdInteres = cuotaCtrl.buscarInteresCorrientePend(f);
					
					if(bdInteres.compareTo(BigDecimal.ZERO) == 0 && f.getId().getAticu() == 0){
						bdInteres = f.getId().getImpuesto().add(f.getId().getInteres());
					}
				}
				
				f.setInteresPend(bdInteres);
				
				Vautoriz vaut =  ((Vautoriz[])m.get("sevAut"))[0];
				BigDecimal tasaoficial = obtenerTasaOficial();
				
				String msgCreaInteres = crearFacturasPorIntereses2(financimientos, cuotaSeleccionada, vaut.getId().getLogin() );
				
				if( !msgCreaInteres.isEmpty() ){
					return ;
				}
			}
			
			
			List<Credhdr> lstNewComp = cuotaCtrl.documentosPorCuota( cuotaSeleccionada, fh, f14);
			lstComponentes.addAll(lstNewComp);
			
			
			//&& ============ buscar que haya documentos por otras financimientos asociados al financimiento principal.
			List<Credhdr> docsPorFinancimientoAsociado = (List<Credhdr>)
			CollectionUtils.select(lstComponentes, new Predicate() {
				@Override
				public boolean evaluate(Object o) {
					return ((Credhdr)o).isFinancimientoAsociado() ;
				}
			}) ;
			
			if(docsPorFinancimientoAsociado != null && !docsPorFinancimientoAsociado.isEmpty() ){
			
				List<String> partidas = (List<String>)CodeUtil.selectPropertyListFromEntity(docsPorFinancimientoAsociado, "partida", true) ; 
				Collections.sort(partidas); 
				
				int  ultimaPartida = Integer.parseInt( partidas.get( partidas.size() - 1  ) );
				Date fechaPagoCuota = lstFacturasSelected.get(0).getId().getFechapago();
				
				List<Credhdr> otrosFinancimentos = CuotaCtrl.buscarFinancimientosAsociados(true, ultimaPartida, f.getId().getCodcli(), 
						String.valueOf( f.getId().getNosol() ), f.getId().getCodcomp(), f.getId().getCodsuc(), f14, fechaPagoCuota) ;
			
				if(otrosFinancimentos != null && !otrosFinancimentos.isEmpty()){
					lstComponentes.addAll(otrosFinancimentos);
				}
			}
			
			//&& =============== ordenar la lista de documentos, por cuota y luego por fecha de vencimiento.
			
			if(!adelantoPrincipal){
			
				Collections.sort(lstComponentes, new Comparator<Credhdr>() {
					public int compare(Credhdr c1, Credhdr c2) {
						 
						 int compare =  c1.getId().getFechavenc().compareTo( c2.getId().getFechavenc() );
						 
						 if(compare == 0){
							 
							 int partida1 =  Integer.parseInt( c1.getId().getPartida() ) ;
							 int partida2 =  Integer.parseInt( c2.getId().getPartida() ) ;
							 
							 compare = (partida1 > partida2) ? 1 : (partida1 < partida2) ? -1 : 0 ;
						 }
						
						return compare;
					}
	
				});
			}
			
			
			if (CodeUtil.getFromSessionMap("finan_MontoAplicar")== null){
				dMontoAplicar = d.formatStringToDouble(txtMontoAplicar.getValue().toString());
			}else {
				dMontoAplicar = d.formatStringToDouble(CodeUtil.getFromSessionMap("finan_MontoAplicar").toString());
			}	
			dMontoRecibido = d.formatStringToDouble(txtMontoRecibido.getValue().toString());
			dMonto =  d.roundDouble(dMontoRecibido - dMontoAplicar);
			f = distribuirMontoAplicaraCuota(f, new BigDecimal(dMonto));
			
			lstFacturasSelected.add(f);
			
			CodeUtil.putInSessionMap("lstSelectedCuotas", lstFacturasSelected);
			gvSelectedCuotas.dataBind();
			
			//distribuir el monto recibido a los montos a aplicar en factura
			distribuirMontoAplicar(sMonedaBase);
			
			lstComponentes = (List)CodeUtil.getFromSessionMap("lstComponentes");
			Credhdr hFacSelected =null;
			for(int i = 0; i < lstComponentes.size(); i++){
				hFacSelected  = (Credhdr)lstComponentes.get(i);
				if (hFacSelected.getId().getMoneda().equals(sMonedaBase)){
					dMontoAplicar = dMontoAplicar + hFacSelected.getMontoPendiente().doubleValue();
					dMontoTotalAplicarDomestico = dMontoTotalAplicarDomestico + hFacSelected.getMontoPendiente().doubleValue();
				}else{
					dMontoAplicar = dMontoAplicar + hFacSelected.getMontoPendiente().doubleValue();
					dMontoTotalAplicarForaneo = dMontoTotalAplicarForaneo + hFacSelected.getMontoPendiente().doubleValue();
					dMontoTotalAplicarDomestico = dMontoTotalAplicarDomestico + (hFacSelected.getMontoPendiente().doubleValue()*dTasaPar.doubleValue());
				}
			}
			

			if(!hFacSelected.getId().getMoneda().equals(sMonedaBase)){//resumen de moneda foranea
				
				double dFaltanteFor = 0;
				BigDecimal dFaltanteDom = BigDecimal.ONE;
				
				if(dMontoRecibido <= dMontoTotalAplicarForaneo ){
					dFaltanteFor = dMontoTotalAplicarForaneo - dMontoRecibido;
					BigDecimal bd = obtenerTasaParalela(hFacSelected.getId().getMoneda());
					dFaltanteDom = bd.multiply(new BigDecimal(String.valueOf( dFaltanteFor )));
				}
				
				montoTotalAplicarForaneo.setStyle("display:inline");
				montoTotalAplicarForaneo.setValue("<B>" + hFacSelected.getId().getMoneda() + "</B>" +" "+ d.formatDouble(dMontoTotalAplicarForaneo));
				montoTotalFaltanteForaneo.setValue("<B>" + hFacSelected.getId().getMoneda() + "</B>" +" "+ d.formatDouble(dFaltanteFor));
				
				montoTotalFaltanteForaneo.setStyle("display:inline");
				montoTotalAplicarDomestico.setValue("<B>"+sMonedaBase+"</B> " + d.formatDouble(dMontoTotalAplicarDomestico));
				montoTotalFaltanteDomestico.setValue("<B>"+sMonedaBase+"</B> " 
								+ d.formatDouble(dFaltanteDom.doubleValue()));
				
			}else{//resumen de moneda domestica
				montoTotalAplicarForaneo.setStyle("display:none");
				montoTotalFaltanteForaneo.setStyle("display:none");
				montoTotalAplicarDomestico.setValue("<B>"+sMonedaBase+"</B> " + d.formatDouble(dMontoTotalAplicarDomestico));
				montoTotalFaltanteDomestico.setValue("<B>"+sMonedaBase+"</B> " + d.formatDouble(dMontoTotalAplicarDomestico));
			}
			intSelectedDet.setValue(lstComponentes.size());
			
			dMonto = calcularElMontoRecibido(lstPagos,hFacSelected,lstComponentes, sMonedaBase);
			determinarCambio(lstPagos,hFacSelected,montoRecibido,sMonedaBase);
			
			
			CodeUtil.refreshIgObjects(new Object[]{intSelectedDet, montoTotalAplicarForaneo, txtCambio});
 
		}catch(Exception ex){
			ex.printStackTrace(); 
			
		}

	}
	
/*******************************************************************************/
	@SuppressWarnings({"unchecked", "static-access", "rawtypes"})
	public void agregarCuotaRecibo_NoEnUso_(ActionEvent e){
		Finandet f = null;
		List lstComponentes, lstNewComp;
		List<Finandet>lstFacturasSelected;
		List<Finandet>lstNewCuotas;
		List<MetodosPago>lstPagos;
		
		CuotaCtrl cuotaCtrl= new CuotaCtrl();
		BigDecimal bdInteres = BigDecimal.ZERO;
		BigDecimal dTasaPar =  BigDecimal.ZERO;
		double dMontoAplicar = 0,dMontoTotalAplicarDomestico = 0.0, dMontoTotalAplicarForaneo = 0.0,dMontoRecibido = 0.0, dMonto = 0.0;
		Divisas d = new Divisas();
		Credhdr cNew = null;
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		String sMonedaBase = "";
		try{

			RowItem ri = (RowItem) e.getComponent().getParent().getParent();
			f = (Finandet) gvAgregarCuota.getDataRow(ri);
			
			Vf55ca01 f55ca01 = (Vf55ca01) ((List) m.get("lstCajas")).get(0);
			sMonedaBase = cCtrl.sacarMonedaBase((F55ca014[])m.get("cont_f55ca014"), f.getId().getCodcomp());
			dTasaPar = this.obtenerTasaParalela("COR");
			
			double montoRecibido = d.formatStringToDouble(txtMontoRecibido.getValue().toString());
			Finanhdr fh = (Finanhdr)((List)m.get("lstCreditosFinan")).get(0);

			lstPagos = (ArrayList<MetodosPago>)m.get("lstPagosFinan");
			lstNewCuotas = (ArrayList<Finandet>)m.get("lstAgregarCuota");
			
			if(lstNewCuotas.size() == 1 ){
				m.put("lstAgregarCuota", new ArrayList<Finandet>());
				dwAgregarCuota.setWindowState("hidden");
			}
			if(lstNewCuotas.size() > 1){
				for(int i = lstNewCuotas.size()-1; i >= 0; i--){
					Finandet f2 = lstNewCuotas.get(i);
					if( f.getId().getNosol() == f2.getId().getNosol() &&
						f.getId().getNocuota() == f2.getId().getNocuota()){
						lstNewCuotas.remove(i);
						break;
					}
				}
				m.put("lstAgregarCuota",lstNewCuotas);
				gvAgregarCuota.dataBind();
			}
			
			//agregar cuota a cuotas seleccionadas
			lstFacturasSelected = (List<Finandet>) m.get("lstSelectedCuotas");
			lstComponentes = (List) m.get("lstComponentes");
			
			//agregar monto pendiente a cuota
			f.setMontopend(f.getId().getMontopend());
			f.setMora(BigDecimal.ZERO);
			f.getId().setDiasmora(0);
			f.getId().setDiasven((short)0);
			f.setMontoAplicar(BigDecimal.ZERO);
			//buscar interes corriente pendiente
			if(!(f.getMontopend().doubleValue() < f.getId().getMonto().doubleValue())){
				bdInteres = cuotaCtrl.buscarInteresCorrientePend(f);
				if(bdInteres.doubleValue() == 0 && f.getId().getAticu() == 0){
					bdInteres = f.getId().getImpuesto().add(f.getId().getInteres());
				}
			}	
			//
			f.setInteresPend(bdInteres);
			//generar if para cuota
			generarIFCuota(f, fh, sMonedaBase,f55ca01);
			//Buscar el detalle de cuotas en el f0311 (moratorios, corriente,saldo )
			F55ca014[] f14 = (F55ca014[])m.get("cont_f55ca014");
			lstNewComp = cuotaCtrl.leerDocumentosxCuota(f,fh,f14);
			lstComponentes.addAll(lstNewComp);
			
			if (m.get("finan_MontoAplicar")== null){
				dMontoAplicar = d.formatStringToDouble(txtMontoAplicar.getValue().toString());
			}else {
				dMontoAplicar = d.formatStringToDouble(m.get("finan_MontoAplicar").toString());
			}	
			dMontoRecibido = d.formatStringToDouble(txtMontoRecibido.getValue().toString());
			dMonto =  d.roundDouble(dMontoRecibido - dMontoAplicar);
			f = distribuirMontoAplicaraCuota(f,new BigDecimal(dMonto));
			
			lstFacturasSelected.add(f);
			
			m.put("lstSelectedCuotas", lstFacturasSelected);
			gvSelectedCuotas.dataBind();
			
			//distribuir el monto recibido a los montos a aplicar en factura
			distribuirMontoAplicar(sMonedaBase);
			
			lstComponentes = (List)m.get("lstComponentes");
			Credhdr hFacSelected =null;
			for(int i = 0; i < lstComponentes.size(); i++){
				hFacSelected  = (Credhdr)lstComponentes.get(i);
				if (hFacSelected.getId().getMoneda().equals(sMonedaBase)){
					dMontoAplicar = dMontoAplicar + hFacSelected.getMontoPendiente().doubleValue();
					dMontoTotalAplicarDomestico = dMontoTotalAplicarDomestico + hFacSelected.getMontoPendiente().doubleValue();
				}else{
					dMontoAplicar = dMontoAplicar + hFacSelected.getMontoPendiente().doubleValue();
					dMontoTotalAplicarForaneo = dMontoTotalAplicarForaneo + hFacSelected.getMontoPendiente().doubleValue();
					dMontoTotalAplicarDomestico = dMontoTotalAplicarDomestico + (hFacSelected.getMontoPendiente().doubleValue()*dTasaPar.doubleValue());
				}
			}
			

			if(!hFacSelected.getId().getMoneda().equals(sMonedaBase)){//resumen de moneda foranea
				
				double dFaltanteFor = 0;
				BigDecimal dFaltanteDom = BigDecimal.ONE;
				
				if(dMontoRecibido <= dMontoTotalAplicarForaneo ){
					dFaltanteFor = dMontoTotalAplicarForaneo - dMontoRecibido;
					BigDecimal bd = obtenerTasaParalela(hFacSelected.getId().getMoneda());
					dFaltanteDom = bd.multiply(new BigDecimal(String.valueOf( dFaltanteFor )));
				}
				
				montoTotalAplicarForaneo.setStyle("display:inline");
				montoTotalAplicarForaneo.setValue("<B>" + hFacSelected.getId().getMoneda() + "</B>" +" "+ d.formatDouble(dMontoTotalAplicarForaneo));
				montoTotalFaltanteForaneo.setValue("<B>" + hFacSelected.getId().getMoneda() + "</B>" +" "+ d.formatDouble(dFaltanteFor));
				
				montoTotalFaltanteForaneo.setStyle("display:inline");
				montoTotalAplicarDomestico.setValue("<B>"+sMonedaBase+"</B> " + d.formatDouble(dMontoTotalAplicarDomestico));
				montoTotalFaltanteDomestico.setValue("<B>"+sMonedaBase+"</B> " 
								+ d.formatDouble(dFaltanteDom.doubleValue()));
				
			}else{//resumen de moneda domestica
				montoTotalAplicarForaneo.setStyle("display:none");
				montoTotalFaltanteForaneo.setStyle("display:none");
				montoTotalAplicarDomestico.setValue("<B>"+sMonedaBase+"</B> " + d.formatDouble(dMontoTotalAplicarDomestico));
				montoTotalFaltanteDomestico.setValue("<B>"+sMonedaBase+"</B> " + d.formatDouble(dMontoTotalAplicarDomestico));
			}
			intSelectedDet.setValue(lstComponentes.size());
			
			dMonto = calcularElMontoRecibido(lstPagos,hFacSelected,lstComponentes, sMonedaBase);
			determinarCambio(lstPagos,hFacSelected,montoRecibido,sMonedaBase);
			
			SmartRefreshManager.getCurrentInstance().addSmartRefreshId(
					intSelectedDet.getClientId(FacesContext.getCurrentInstance()));
			SmartRefreshManager.getCurrentInstance().addSmartRefreshId(
					montoTotalAplicarForaneo.getClientId(FacesContext.getCurrentInstance()));
			SmartRefreshManager.getCurrentInstance().addSmartRefreshId(
					txtCambio.getClientId(FacesContext.getCurrentInstance()));
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
/*******************************************************************************/
	/********************DISTRIBUIR EL MONTO A APLICAR EN EL GRID DE DETALLE DE FACTURA***********************/
	public Finandet distribuirMontoAplicaraCuota(Finandet fd, BigDecimal dMontoRecibido){
		Divisas divisas = new Divisas();
		BigDecimal dMoraPend,bdIntPend, bdPrincipal, bdTotalAplicar = BigDecimal.ZERO;
		try{
			//setear el monto a aplicar a cero
		    fd.setMontoAplicar(BigDecimal.ZERO);
			
			//distribuir el monto recibido entre la mora de la cuota
			//sumar total
			bdTotalAplicar = bdTotalAplicar.add(fd.getMontopend());
			dMoraPend = fd.getMora();
			if(dMoraPend.doubleValue() > 0){//si hay mora en la cuota aplicar monto
				if(dMontoRecibido.doubleValue() >= dMoraPend.doubleValue()){
					fd.setMontoAplicar(dMoraPend);
					dMontoRecibido = dMontoRecibido.subtract(dMoraPend);
				}else{
					fd.setMontoAplicar(dMontoRecibido);
					dMontoRecibido = dMontoRecibido.subtract(dMontoRecibido);
				}
			}
			//distribuir el resto del monto en el interes corriente + impuestos
			if(dMontoRecibido.doubleValue() > 0){				
				bdIntPend = fd.getInteresPend();
				if(bdIntPend.doubleValue() > 0){
					if(dMontoRecibido.doubleValue() >= bdIntPend.doubleValue()){
						fd.setMontoAplicar(fd.getMontoAplicar().add(bdIntPend));
						dMontoRecibido = dMontoRecibido.subtract(bdIntPend);
					}else{
						fd.setMontoAplicar(fd.getMontoAplicar().add(dMontoRecibido));
						dMontoRecibido = dMontoRecibido.subtract(dMontoRecibido);
					}						
				}
			}
			//distribuir el resto del monto en el principal de la cuota
			if(dMontoRecibido.doubleValue() > 0){		
				if(fd.getId().getMontopend().doubleValue()>fd.getId().getPrincipal().doubleValue()){
					bdPrincipal = fd.getId().getMontopend().subtract(fd.getInteresPend());//hFac.getId().getPrincipal();
				}
				else{
					bdPrincipal = fd.getId().getMontopend();
				}
				//
				if(dMontoRecibido.doubleValue() >= bdPrincipal.doubleValue()){
					fd.setMontoAplicar(fd.getMontoAplicar().add(bdPrincipal));
					dMontoRecibido = dMontoRecibido.subtract(bdPrincipal);
				}else{
					fd.setMontoAplicar(fd.getMontoAplicar().add(dMontoRecibido));
					dMontoRecibido = dMontoRecibido.subtract(dMontoRecibido);
				}
				
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return fd;
	}
/************************************************************************************************************************************/		
	public boolean validarAplicado(){
		boolean bValido = false;
		List lstFacturasSelected = null;
		RowItem ri = null;
		Finandet fd = null;
		BigDecimal dAplicarSel = BigDecimal.ZERO;
		Divisas d = new Divisas();
		double dTotal = 0, dDif = 0;
		try{
			lstFacturasSelected = ((List) m.get("lstSelectedCuotas"));
			//sacar lista de finanhdr
			for(int a = 0; a < lstFacturasSelected.size();a++){
				fd = (Finandet) lstFacturasSelected.get(a);
				dAplicarSel = dAplicarSel.add(fd.getMontoAplicar());
			}
			if (m.get("finan_MontoAplicar")== null){
				dTotal = d.formatStringToDouble(txtMontoAplicar.getValue().toString());
			}else {
				dTotal = d.formatStringToDouble(m.get("finan_MontoAplicar").toString());
			}	
			dDif = d.roundDouble(dTotal - dAplicarSel.doubleValue());
			if(dAplicarSel.doubleValue() < dTotal){
				if(dDif > 1){
					bValido = false;
					lblMensajeError.setValue("El monto a aplicar de las cuotas es menor al monto a aplicar del recibo" +
												" Revisar saldo de cuota en modulo de financiamiento y JDE en crédito y cobranzas");
				}else{
					bValido = true;
				}
			}else{
				bValido = true;
			}
			
		}catch(Exception ex){
			bValido = false;
			ex.printStackTrace();
		}
		return bValido;
	}
/*************REALIZAR ASIENTOS DE COMPRA/VENTA DE FICHA*****************************************************************************/	
	public boolean generarAsientosFichaCV(Session s, Transaction tx,List lstPagoFicha, Vf55ca01 f55ca01, Credhdr hFac,
			String sTipoCliente, Vautoriz vaut, String[] sCuentaMetodo, String[] sCtaMetodoDom, int iNumFicha,int iNumrec) {
		int iContadorDom = 0, iContadorFor = 0, iNoBatch = 0, iNoDocDomestico = 0, iNoDocForaneo = 0;
		boolean bContabilizado = false;
		ReciboCtrl recCtrl = new ReciboCtrl();
		Divisas d = new Divisas();
		MetodosPago mPago = null;
		String sConcepto = "",sCodsuc = "",sSucursaldeAsiento="";
		Date dtFecha = new Date();
		
		try { 
			
			sConcepto = "Ficha No:" + iNumFicha + " Ca:" + f55ca01.getId().getCaid() + " Com:" + hFac.getId().getCodcomp();
			sSucursaldeAsiento = sCuentaMetodo[2];
			
			//leer no de batch
			iNoBatch = d.leerActualizarNoBatch();
			if(iNoBatch == 0){
				lblMensajeError.setValue("No se pudo leer No. de batch para la ficha");
				return false;
			}

			// leer y actualizar los asientos para los documentos foraneos
			iNoDocForaneo = recCtrl.leerActualizarNoAsiento();
			if(iNoDocForaneo == 0){
				lblMensajeError.setValue("No se pudo leer No. de Documento para el asiento de diario de la ficha");
				return false;
			}
			
			mPago = (MetodosPago) lstPagoFicha.get(0);
			sCodsuc =  hFac.getId().getCodsuc().substring(3, 5);
			// Registrar Asientos de la ficha de compra venta
			iContadorFor++;
			bContabilizado = recCtrl.registrarAsientoDiarioWithSession(dtFecha, s, sSucursaldeAsiento,valoresJDEInsFCV[1], iNoDocForaneo, (iContadorFor) * 1.0,iNoBatch, 
								sCuentaMetodo[0], sCuentaMetodo[1], sCuentaMetodo[3], sCuentaMetodo[4],sCuentaMetodo[5],"CA", mPago.getMoneda(), 
								d.pasarAentero(d.roundDouble(mPago.getMonto())), sConcepto, vaut.getId().getLogin(), vaut.getId().getCodapp(), mPago.getTasa(), sTipoCliente, "Deb caja dolares "
								+ d.roundDouble(mPago.getMonto()), sCuentaMetodo[2],"","","USD",sCuentaMetodo[2],"F");
			if (bContabilizado) {
				bContabilizado = recCtrl.registrarAsientoDiarioWithSession(dtFecha,s, sSucursaldeAsiento,valoresJDEInsFCV[1],iNoDocForaneo, (iContadorFor) * 1.0, iNoBatch,
								sCuentaMetodo[0], sCuentaMetodo[1], sCuentaMetodo[3], sCuentaMetodo[4], sCuentaMetodo[5], "AA", mPago.getMoneda(),
								d.pasarAentero(d.roundDouble(mPago.getEquivalente())), sConcepto,vaut.getId().getLogin(), vaut.getId().getCodapp(), mPago.getTasa(),
								sTipoCliente, "Deb caja dolares " + d.roundDouble(mPago.getMonto()), sCuentaMetodo[2],"","","COR",sCuentaMetodo[2],"F");
				if (bContabilizado) {
					bContabilizado = recCtrl.registrarAsientoDiarioWithSession(dtFecha,s, sSucursaldeAsiento,valoresJDEInsFCV[1], iNoDocForaneo,(iContadorFor + 1) * 1.0, iNoBatch, 
									sCtaMetodoDom[0],sCtaMetodoDom[1],sCtaMetodoDom[3],sCtaMetodoDom[4],sCtaMetodoDom[5], "CA", mPago.getMoneda(), (-1)* d.pasarAentero(d.roundDouble(mPago.getMonto())), sConcepto,vaut.getId().getLogin(), vaut.getId().getCodapp(),
									mPago.getTasa(), sTipoCliente,"Cred caja cordobas " + d.roundDouble(mPago.getMonto()),sCtaMetodoDom[2],"","","USD",sCtaMetodoDom[2],"F");
					if (bContabilizado) {
						bContabilizado = recCtrl.registrarAsientoDiarioWithSession(dtFecha, s,sSucursaldeAsiento,valoresJDEInsFCV[1], iNoDocForaneo,(iContadorFor + 1) * 1.0, iNoBatch,sCtaMetodoDom[0], sCtaMetodoDom[1], sCtaMetodoDom[3], sCtaMetodoDom[4],sCtaMetodoDom[5],
										"AA", mPago.getMoneda(), (-1)* d.pasarAentero(d.roundDouble(mPago.getEquivalente())),sConcepto, vaut.getId().getLogin(), vaut
										.getId().getCodapp(), mPago.getTasa(),sTipoCliente, "Cred caja cordobas "+ d.roundDouble(mPago.getMonto()), 
										sCtaMetodoDom[2],"","","COR",sCtaMetodoDom[2],"F");
						iContadorFor++;
						
						if(bContabilizado){
							bContabilizado = recCtrl.fillEnlaceMcajaJde(s,tx,iNumFicha, hFac.getId().getCodcomp(), iNoDocForaneo, iNoBatch,f55ca01.getId().getCaid(),f55ca01.getId().getCaco(),"A","FCV");
							if(bContabilizado){
								//bContabilizado = recCtrl.registrarBatch(cn,"G", iNoBatch, d.pasarAentero(d.roundDouble(mPago.getMonto())), vaut.getId().getLogin(), 1,MetodosPagoCtrl.DEPOSITO); VERSION A7.3
								bContabilizado = recCtrl.registrarBatchA92WithSession(s,"G", iNoBatch, d.pasarAentero(d.roundDouble(mPago.getMonto())), vaut.getId().getLogin(), 1,MetodosPagoCtrl.DEPOSITO);
								if(!bContabilizado){
									bContabilizado = false;
									lblMensajeError.setValue("No se pudo grabar el batch de le ficha ");
								}
							}else {
								lblMensajeError.setValue("No se pudo insertar el enlace entre la ficha y el documento de transacciones Foraneas!!!");
							}
						}else{
							lblMensajeError.setValue("No se pudo realizar el asiento de diario de la ficha de CV");
						}		
						
					} else {
						lblMensajeError.setValue("No se pudo realizar el asiento de diario de la ficha de CV");
					}
				} else {
					lblMensajeError.setValue("No se pudo realizar el asiento de diario de la ficha de CV");
				}
			} else {
				lblMensajeError.setValue("No se pudo realizar el asiento de diario de la ficha de CV");
			}
		} catch (Exception ex) {
			bContabilizado = false;
			lblMensajeError.setValue("Error no se grabaron los asientos de diario de la ficha: " + ex);
			ex.printStackTrace();
		}
		return bContabilizado;
	}
/****************************************************************************************************************************/
	
/*********************************************************************************************************/
	/*************INSERTAR FICHA DE COMPRA/VENTA******************************************************************/	
	public boolean insertarFichaCV(Session session, Transaction tx, Vf55ca01 f55ca01, Finanhdr hFac, Vautoriz vautoriz,double dMonto,int iNumrec, String sCajero,List lstPagoFicha){
		boolean bInsertado = false;
		NumcajaCtrl numCtrl = new NumcajaCtrl();
		int iNoFicha = 0;
		ReciboCtrl conCtrl = new ReciboCtrl();
		String sCodunineg = "";
		
		try{
			
			sCodunineg = hFac.getId().getCodsuc().substring(3, 5) + hFac.getId().getLinea();
		 
			
			iNoFicha = numCtrl.obtenerNoSiguiente("FICHACV", f55ca01.getId().getCaid(), hFac.getId().getCodcomp(), f55ca01.getId().getCaco(), vautoriz.getId().getLogin(),session);
			if(iNoFicha > 0){

				//registrar encabezado de ficha
				bInsertado = conCtrl.registrarRecibo(session, tx, iNoFicha, 0,
						hFac.getId().getCodcomp(), dMonto, dMonto, 0, "",
						new Date(), new Date(), hFac.getId().getCodcli(),
						hFac.getNomcli(), sCajero, f55ca01.getId().getCaid(),
						f55ca01.getId().getCaco(), vautoriz.getId().getLogin(),
						"FCV", 0, "", iNumrec, new Date(), sCodunineg, "", hFac.getId().getMoneda());
				
				if (bInsertado){
					 
					bInsertado = conCtrl.registrarDetalleRecibo(session, tx, iNoFicha, 0, hFac.getId().getCodcomp(), lstPagoFicha,f55ca01.getId().getCaid(),f55ca01.getId().getCaco(),"FCV");
					
					m.put("iNoFichaFinan", iNoFicha);
					if(!bInsertado){
						lblMensajeError.setValue("No se pudo registrar el detalle de la ficha de compra/venta!!!");					
					}
					
				}else{
					lblMensajeError.setValue("No se pudo registrar la ficha de compra/venta!!!");
				}
			}else{
				lblMensajeError.setValue("No se encontro No. de ficha configurado para esta caja!!!");
				bInsertado = false;
			}
		}catch(Exception ex){
			bInsertado = false;
			ex.printStackTrace(); 
			
		}
		return bInsertado;
	}
/*********************************************************************************************/
	public boolean pagarCreditoFinan(Session s,Transaction tx,
					Vf55ca01 f55ca01,Finanhdr fh,List lstFacturasSelected, 
					List<MetodosPago> lstPagos,int iFecha,BigDecimal bdTasaJde, 
					BigDecimal bdTasaPar,Vautoriz vAut,boolean bHayFicha, 
					List lstPagoFicha,String sMonedaBase,List<Credhdr> lstComponentes){
		boolean bHecho = false;
		Divisas d = new Divisas();
		int iNobatch = 0,iNodoc = 0;
		Finandet fd = null;
		Credhdr fd2 = null;		
		int b = 0, c = 0, iTotalTransaccion = 0;
		MetodosPago mPago = null;
		double dMontoDif,dMontoF = 0,dMontoAplicarxFactura = 0,dNewPendienteForaneo = 0,dNewPendienteDom = 0, dTotal = 0;
		double d1 = 0, d2 = 0,dMontoD = 0;
		String[] sCuentaCaja = null;
		ReciboCtrl recCtrl = new ReciboCtrl();
		String sConcepto = "",sTipoCliente="";
		EmpleadoCtrl empCtrl = new EmpleadoCtrl();		
		ReciboCtrl rCtrl = new ReciboCtrl();
		FechasUtil fecUtil = new FechasUtil();
		
		try{	
			
			
			if (m.get("finan_MontoAplicar")== null){
				dTotal = d.formatStringToDouble(txtMontoAplicar.getValue().toString());
			}else {
				dTotal = d.formatStringToDouble(m.get("finan_MontoAplicar").toString());
			}
			
			BigDecimal totalAplicadoFacturas = BigDecimal.ZERO ; //new BigDecimal(dTotal, MathContext.DECIMAL64);
			
			for(Credhdr fact : lstComponentes) {
				totalAplicadoFacturas = totalAplicadoFacturas.add( fact.getMontoAplicar() );
			}
			
			BigDecimal totalEquivFormasPago = CodeUtil.sumPropertyValueFromEntityList( lstPagos, "equivalente", false);
			 
			if(		totalAplicadoFacturas.compareTo(totalEquivFormasPago) != 0 && 
					totalAplicadoFacturas.subtract(totalEquivFormasPago).abs().compareTo(new BigDecimal("0.5") ) ==1 ) {
				lblMensajeError.setValue(" monto aplicado en facturas distinto a monto equivalente en pagos, intente de nuevo " );
				return false;
			}
			
			
			
			iTotalTransaccion = d.pasarAentero(dTotal);
			sConcepto = "R:" + m.get("iNumRecFinan").toString() + " Ca:" + f55ca01.getId().getCaid() + " Com:" + fh.getId().getCodcomp();
			sTipoCliente = empCtrl.determinarTipoCliente(fh.getId().getCodcli());
			
			
			//&& ===================== leer no de batch
			iNobatch = d.leerActualizarNoBatch();
			CodeUtil.putInSessionMap("fn_NumeroBatchReciboJde", iNobatch);
			
			if(iNobatch > 0){
				iNodoc = recCtrl.leerNumeroRpdocm(true,fh.getId().getCodcomp());
				if(iNodoc > 0){
					if (!fh.getId().getMoneda().equals(sMonedaBase)){//F:USD  la factura esta en moneda foranea
						
						while (b < lstPagos.size()){// while de metodos de pago
							mPago = (MetodosPago)lstPagos.get(b);
							
							//&& =========== ya fueron aplicados los montos a todas las facturas y quedo sobrante del pago menor de 25ctvs
							if( c >= lstComponentes.size()  && mPago.getEquivalente() <= 0.25) {
								b++;
								mPago.setEquivalente(0);
								mPago.setMonto(0);
								continue;
							}
							
							//&& ============== leer cuenta de metodo de pago
							sCuentaCaja = d.obtenerCuentaCaja(f55ca01.getId().getCaid(),fh.getId().getCodcomp(),mPago.getMetodo(),mPago.getMoneda(),null,null,null,null);						
							
							if(sCuentaCaja != null){ 

								if(mPago.getMoneda().equals(sMonedaBase)){//pago en moneda distinta a la de la factura f:USD p:COR
								
									while (c < lstComponentes.size()){//pagar cada MF,IF y principal de cuota en el F0311									
										
										fd2 = (Credhdr)lstComponentes.get(c);	
										
										//&& ==== Ajustar el metodo de pago al equivalente de la factura. ( en caso que sea mayor por centavos)
										if( new BigDecimal(Double.toString(mPago.getEquivalente())).compareTo(fd2.getMontoAplicar()) == 1 && 
												new BigDecimal(Double.toString(mPago.getEquivalente())).subtract(fd2.getMontoAplicar()) 
												.compareTo(new BigDecimal("0.01")) == -1 ){
											mPago.setEquivalente(Divisas.roundDouble(fd2.getMontoAplicar().doubleValue(), 2)) ;
										}
										
										if( fd2.getMontoAplicar().compareTo(BigDecimal.ZERO) == 1  ){//si el monto a aplicar es mayor a 0
											
											if(d.roundDouble(mPago.getEquivalente()) > d.roundDouble(fd2.getMontoAplicar().doubleValue())){//el pago es mayor al monto d ela factura
												dMontoF = d.roundDouble(mPago.getEquivalente() - fd2.getMontoAplicar().doubleValue());
												dMontoAplicarxFactura = d.roundDouble(dMontoAplicarxFactura + fd2.getMontoAplicar().doubleValue());
												
												dNewPendienteForaneo = d.roundDouble(fd2.getId().getDpendiente().doubleValue() - dMontoAplicarxFactura);

												//ajustar valores para el dif. camb.
												if(dNewPendienteForaneo > 0){													
													d2 = d.roundDouble(fd2.getMontoAplicar().doubleValue()*fd2.getId().getTasa().doubleValue());		
													dNewPendienteDom = d.roundDouble(d.roundDouble(fd2.getId().getCpendiente().doubleValue()) - d2 - dMontoD);
													d1 = 0;	
												}else{
													d1 = d.roundDouble(fd2.getMontoAplicar().doubleValue()*fd2.getId().getTasa().doubleValue());
													d1 = d1 + dMontoD;
													d2 = fd2.getId().getCpendiente().doubleValue();	
													dNewPendienteDom = 0;
													if (d1 >= d2 || d1 <= d2){
														d1 = d2 - d1;
														d2 = d.roundDouble(d2 - dMontoD);
													}else{
														d1=0;
														d2 = d.roundDouble(d2 - dMontoD);
													}
												}
												
												//grabar RC
												bHecho = rCtrl.grabarRCWithSession(s, fd2.getId().getCodsuc(), fd2.getId().getCodcli(), fd2.getId().getTipofactura(), fd2.getId().getNofactura(), fd2.getId().getPartida(), fd2.getId().getRpdivj(), 
																	 "RC", iNodoc, fecUtil.obtenerFechaActualJuliana(), "R", iNobatch, d2, "F",  fd2.getMoneda(), fd2.getId().getTasa().doubleValue(), d.roundDouble(fd2.getMontoAplicar().doubleValue()), 
																	 fd2.getId().getCompenslm(), sCuentaCaja[1], fd2.getId().getCodunineg().trim(), fd2.getId().getTipopago(), "I", sConcepto, "", fd2.getId().getNomcli(), vAut.getId().getLogin(), vAut.getId().getNomapp(),fd2.getId().getRpddj(),fd2.getId().getRppo(),fd2.getId().getRpdcto());	
												if(bHecho){
													//si hay diferencial
													if(bdTasaPar.doubleValue() != fd2.getId().getTasa().doubleValue()){
														
														dMontoDif = d.roundDouble(((dMontoAplicarxFactura * bdTasaPar.doubleValue()) - (dMontoAplicarxFactura*fd2.getId().getTasa().doubleValue())) + d1);
														
														if( Divisas.roundDouble(dMontoDif , 2) > 0 ){
															bHecho = rCtrl.grabarRCWithSession(s, fd2.getId().getCodsuc(), fd2.getId().getCodcli(), fd2.getId().getTipofactura(), fd2.getId().getNofactura(), fd2.getId().getPartida(), fd2.getId().getRpdivj(), 
																			 "RG", iNodoc, fecUtil.obtenerFechaActualJuliana(), "R", iNobatch, dMontoDif, "F",  fd2.getId().getMoneda(), bdTasaPar.doubleValue(), 0, 
																			 fd2.getId().getCompenslm(), sCuentaCaja[1], fd2.getId().getCodunineg().trim(), fd2.getId().getTipopago(), "F", sConcepto, "P", fd2.getId().getNomcli(), vAut.getId().getLogin(), vAut.getId().getNomapp(),fd2.getId().getRpddj(),fd2.getId().getRppo(),fd2.getId().getRpdcto());
														}
														if(!bHecho){
															lblMensajeError.setValue("No se pudo grabar el diferencial cambiario en el JDE!!!");
															return false;
														}
													}
													
													if (dNewPendienteDom > 0){
														bHecho = rCtrl.aplicarMontoF0311WithSession(s, "A",dNewPendienteDom, dNewPendienteForaneo, vAut.getId().getLogin(), fd2.getId().getCodsuc(), fd2.getId().getTipofactura(), fd2.getId().getNofactura(), fd2.getId().getPartida(),fd2.getId().getCodcli());
														if(!bHecho){
															lblMensajeError.setValue("No se pudo aplicar el monto a factura No. "+fd2.getId().getNofactura()+" en el JDE!!! " );
															return false;
														}
													}else{
														bHecho = rCtrl.aplicarMontoF0311WithSession(s, "P",dNewPendienteDom, dNewPendienteForaneo, vAut.getId().getLogin(), fd2.getId().getCodsuc(), fd2.getId().getTipofactura(), fd2.getId().getNofactura(), fd2.getId().getPartida(),fd2.getId().getCodcli());
														if(!bHecho){
															lblMensajeError.setValue("No se pudo aplicar el monto a factura No. "+fd2.getId().getNofactura()+" en el JDE!!! " );
															return false;
														}
													}
													
												}else{
													lblMensajeError.setValue("No se pudo grabar el recibo en el JDE!!!");
													return false;
												}
												
												//pasar a siguiente factura
												c++;
												dMontoD = 0;
												dMontoAplicarxFactura = 0;
												d1=0; 
												d2=0;
												
												mPago.setEquivalente(dMontoF);
												if(dMontoF <= 0.01  ){
													mPago.setEquivalente(0);
													mPago.setMonto(0);
													b++;
												}
												fd2.getId().setCpendiente(new BigDecimal(dNewPendienteDom));
												fd2.getId().setDpendiente(new BigDecimal(dNewPendienteForaneo));
												fd2.setMontoAplicar(BigDecimal.ZERO);
												
											}//el pago es mayor al monto de la factura											
											else if(d.roundDouble(mPago.getEquivalente()) == d.roundDouble(fd2.getMontoAplicar().doubleValue())){// el pago es igual al monto de la factura
												////LogCrtl.sendLogDebgsFinancing("<============> el pago es igual al monto de la factura  linea: 1908");
												dMontoF = d.roundDouble(mPago.getEquivalente() - fd2.getMontoAplicar().doubleValue());
												dMontoAplicarxFactura = d.roundDouble(dMontoAplicarxFactura + fd2.getMontoAplicar().doubleValue());
												
												dNewPendienteForaneo = d.roundDouble(fd2.getId().getDpendiente().doubleValue() - dMontoAplicarxFactura);
												//ajustar valores para el dif. camb.
												if(dNewPendienteForaneo > 0){											
													d2 = d.roundDouble(fd2.getMontoAplicar().doubleValue()*fd2.getId().getTasa().doubleValue());		
													dNewPendienteDom = d.roundDouble(d.roundDouble(fd2.getId().getCpendiente().doubleValue()) - d2 - dMontoD);
													d1 = 0;
												}else{
													d1 = d.roundDouble(fd2.getMontoAplicar().doubleValue()*fd2.getId().getTasa().doubleValue());
													d1 = d1 + dMontoD;
													d2 = fd2.getId().getCpendiente().doubleValue();	
													dNewPendienteDom = 0;
													if (d1 >= d2 || d1 <= d2){
														d1 = d2 - d1;
														d2 = d.roundDouble(d2 - dMontoD);
													}else{
														d1=0;
														d2 = d.roundDouble(d2 - dMontoD);
													}
												}
												
												//grabar RC
												bHecho = rCtrl.grabarRCWithSession(s, fd2.getId().getCodsuc(), fd2.getId().getCodcli(), fd2.getId().getTipofactura(), fd2.getId().getNofactura(), fd2.getId().getPartida(), fd2.getId().getRpdivj(), 
																	 "RC", iNodoc, fecUtil.obtenerFechaActualJuliana(), "R", iNobatch, d2, "F",  fd2.getMoneda(), fd2.getId().getTasa().doubleValue(),d.roundDouble(fd2.getMontoAplicar().doubleValue()), 
																	 fd2.getId().getCompenslm(), sCuentaCaja[1], fd2.getId().getCodunineg().trim(), fd2.getId().getTipopago(), "I", sConcepto, "", fd2.getId().getNomcli(), vAut.getId().getLogin(), vAut.getId().getNomapp(),fd2.getId().getRpddj(),fd2.getId().getRppo(),fd2.getId().getRpdcto());			
												
												if(bHecho){
													//si hay diferencial
													if(bdTasaPar.doubleValue() != fd2.getId().getTasa().doubleValue()){
														dMontoDif = d.roundDouble(((dMontoAplicarxFactura * bdTasaPar.doubleValue()) - (dMontoAplicarxFactura*fd2.getId().getTasa().doubleValue())) + d1);
														
														if( Divisas.roundDouble(dMontoDif , 2) > 0 ){
														
															bHecho = rCtrl.grabarRCWithSession(s, fd2.getId().getCodsuc(), fd2.getId().getCodcli(), fd2.getId().getTipofactura(), fd2.getId().getNofactura(), fd2.getId().getPartida(), fd2.getId().getRpdivj(), 
																			 "RG", iNodoc, fecUtil.obtenerFechaActualJuliana(), "R", iNobatch, dMontoDif, "F",  fd2.getId().getMoneda(), bdTasaPar.doubleValue(), 0, 
																			 fd2.getId().getCompenslm(), sCuentaCaja[1], fd2.getId().getCodunineg().trim(), fd2.getId().getTipopago(), "F", sConcepto, "P", fd2.getId().getNomcli(), vAut.getId().getLogin(), vAut.getId().getNomapp(),fd2.getId().getRpddj(),fd2.getId().getRppo(),fd2.getId().getRpdcto());
														}
														if(!bHecho){
															lblMensajeError.setValue("No se pudo grabar el diferencial cambiario en el JDE!!!");
															return false;
														}
													}
													
													if (dNewPendienteDom > 0){
														bHecho = rCtrl.aplicarMontoF0311WithSession(s, "A",dNewPendienteDom, dNewPendienteForaneo, vAut.getId().getLogin(), fd2.getId().getCodsuc(), fd2.getId().getTipofactura(), fd2.getId().getNofactura(), fd2.getId().getPartida(),fd2.getId().getCodcli());
														if(!bHecho){
															lblMensajeError.setValue("No se pudo aplicar el monto a factura No. "+fd2.getId().getNofactura()+" en el JDE!!! " );
															return false;
														}
													}else{
														bHecho = rCtrl.aplicarMontoF0311WithSession(s, "P",dNewPendienteDom, dNewPendienteForaneo, vAut.getId().getLogin(), fd2.getId().getCodsuc(), fd2.getId().getTipofactura(), fd2.getId().getNofactura(), fd2.getId().getPartida(),fd2.getId().getCodcli());
														if(!bHecho){
															lblMensajeError.setValue("No se pudo aplicar el monto a factura No. "+fd2.getId().getNofactura()+" en el JDE!!! " );
															return false;
														}
													}
													
												}else{
													lblMensajeError.setValue("No se pudo grabar el recibo en el JDE!!!");
													return false;
												}
												
												//pasar a siguiente factura
												c++;
												//pasar a sguiente forma de pago
												b++;
												dMontoD = 0;
												dMontoAplicarxFactura = 0;
												d1=0; d2=0;
												mPago.setEquivalente(0);
												fd2.setMontoAplicar(BigDecimal.ZERO);
												fd2.getId().setCpendiente(new BigDecimal(dNewPendienteDom));
												fd2.getId().setDpendiente(new BigDecimal(dNewPendienteForaneo));
												break;
											}// fin de el pago es igual al monto de la factura
											else{// el monto del pago es menor al pago de la factura
												/*dMontoF = d.roundDouble(fd2.getMontoAplicar().doubleValue() - mPago.getEquivalente());
												
												dMontoD = d.roundDouble(mPago.getEquivalente()*fd2.getId().getTasa().doubleValue());
												//grabarRC
												bHecho = rCtrl.grabarRC(cn, fd2.getId().getCodsuc(), fd2.getId().getCodcli(), fd2.getId().getTipofactura(), fd2.getId().getNofactura(), fd2.getId().getPartida(), fd2.getId().getRpdivj(), 
														 "RC", iNodoc, fecUtil.obtenerFechaActualJuliana(), "R", iNobatch, dMontoD, "F",  fd2.getMoneda(), fd2.getId().getTasa().doubleValue(), d.roundDouble(mPago.getEquivalente()), 
														 fd2.getId().getCompenslm(), sCuentaCaja[1], fd2.getId().getCodunineg().trim(), fd2.getId().getTipopago(), "I", sConcepto, "", fd2.getId().getNomcli(), vAut.getId().getLogin(), vAut.getId().getNomapp(),fd2.getId().getRpddj());
												if(!bHecho){
													lblMensajeError.setValue("No se pudo grabar el recibo en el JDE!!!");
													return false;
												}
												//se decremente el monto a apllicar a la factura 
												fd2.setMontoAplicar(new BigDecimal(dMontoF));
												dMontoAplicarxFactura = d.roundDouble(dMontoAplicarxFactura + mPago.getEquivalente());
												//monto en cordobas q se va aplicando
												dMontoD = d.roundDouble(dMontoAplicarxFactura * fd2.getId().getTasa().doubleValue());
												//pasar a sguiente forma de pago
												b++;*/
											
												//otro												
												dMontoF = d.roundDouble(fd2.getMontoAplicar().doubleValue() - mPago.getEquivalente());
												
												b++;
												//grabarRC
												bHecho = rCtrl.grabarRCWithSession(s, fd2.getId().getCodsuc(), fd2.getId().getCodcli(), fd2.getId().getTipofactura(), fd2.getId().getNofactura(), fd2.getId().getPartida(), fd2.getId().getRpdivj(), 
														 "RC", iNodoc, fecUtil.obtenerFechaActualJuliana(), "R", iNobatch, d.roundDouble(mPago.getEquivalente()*fd2.getId().getTasa().doubleValue()), "F",  fd2.getMoneda(), fd2.getId().getTasa().doubleValue(), mPago.getEquivalente(), 
														 fd2.getId().getCompenslm(), sCuentaCaja[1], fd2.getId().getCodunineg().trim(), fd2.getId().getTipopago(), "I", sConcepto, "", fd2.getId().getNomcli(), vAut.getId().getLogin(), vAut.getId().getNomapp(),fd2.getId().getRpddj(),fd2.getId().getRppo(),fd2.getId().getRpdcto());
												
												if(!bHecho){
													lblMensajeError.setValue("No se pudo grabar el recibo en el JDE!!!");
													return false;
												}
												//se decremente el monto a apllicar a la factura 
												fd2.setMontoAplicar(new BigDecimal(dMontoF));
												//hFacSel.getId().setDpendiente(new BigDecimal(d.roundDouble(hFacSel.getId().getDpendiente().doubleValue() - mPago.getEquivalente())));	
												//hFacSel.getId().setCpendiente(new BigDecimal(d.roundDouble(hFacSel.getId().getDpendiente().doubleValue()*hFacSel.getId().getTasa().doubleValue())));	
												dMontoAplicarxFactura = d.roundDouble(dMontoAplicarxFactura + mPago.getEquivalente());
												////LogCrtl.sendLogDebgsFinancing("<============> gdMontoAplicarxFactura :"+dMontoAplicarxFactura+"  linea: 2033 ");
												//monto en cordobas q se va aplicando
												dMontoD = d.roundDouble(dMontoAplicarxFactura * fd2.getId().getTasa().doubleValue());
												 
												break;
											}// fin del monto del pago es menor al pago de la factura
										}else{//no incluye la factura
											//pasar a siguiente factura
											c++;
										}//no incluye la factura																					
									}// fin de pagar cada MF,IF y principal de cuota en el F0311
								}// fin de pago en moneda foranea
								else{//pago en moneda domestica F:USD P:USD
								 
									while (c < lstComponentes.size()){//pagar cada MF,IF y principal de cuota en el F0311									
										fd2 = (Credhdr)lstComponentes.get(c);	
										if(fd2.getMontoAplicar().doubleValue()>0){//si el monto a aplicar es mayor a 0
											if(d.roundDouble(mPago.getMonto()) > d.roundDouble(fd2.getMontoAplicar().doubleValue())){//el pago es mayor al monto d ela factura
											 
												
												dMontoF = d.roundDouble(mPago.getMonto() - fd2.getMontoAplicar().doubleValue());
												 
												dMontoAplicarxFactura = d.roundDouble(dMontoAplicarxFactura + fd2.getMontoAplicar().doubleValue());
												 
												
												dNewPendienteForaneo = d.roundDouble(fd2.getId().getDpendiente().doubleValue() - dMontoAplicarxFactura);
											 
												//ajustar valores para el dif. camb.
												if(dNewPendienteForaneo > 0){
													d2 = d.roundDouble(fd2.getMontoAplicar().doubleValue()*fd2.getId().getTasa().doubleValue());	
												 
													dNewPendienteDom = d.roundDouble(fd2.getId().getCpendiente().doubleValue() - d2 - dMontoD);		
												 
													d1 = 0;	
												}else{
													d1 = d.roundDouble(fd2.getMontoAplicar().doubleValue()*fd2.getId().getTasa().doubleValue());
													d1 = d1 + dMontoD;
													
													d2 = fd2.getId().getCpendiente().doubleValue();		
													dNewPendienteDom = 0;
													if (d1 >= d2 || d1 <= d2){
														d1 = d2 - d1;
														d2 = d.roundDouble(d2 - dMontoD);
													}else{
														d1=0;
														d2 = d.roundDouble(d2 - dMontoD);
													}
												}
												
												//grabar RC
												bHecho = rCtrl.grabarRCWithSession(s, fd2.getId().getCodsuc(), fd2.getId().getCodcli(), fd2.getId().getTipofactura(), fd2.getId().getNofactura(), fd2.getId().getPartida(), fd2.getId().getRpdivj(), 
																	 "RC", iNodoc, fecUtil.obtenerFechaActualJuliana(), "R", iNobatch, d2, "F",  fd2.getMoneda(), fd2.getId().getTasa().doubleValue(), d.roundDouble(fd2.getMontoAplicar().doubleValue()), 
																	 fd2.getId().getCompenslm(), sCuentaCaja[1], fd2.getId().getCodunineg().trim(), fd2.getId().getTipopago(), "I", sConcepto, "", fd2.getId().getNomcli(), vAut.getId().getLogin(), vAut.getId().getNomapp(),fd2.getId().getRpddj(),fd2.getId().getRppo(),fd2.getId().getRpdcto());
												
												if(bHecho){
													//si hay diferencial
													if(bdTasaPar.doubleValue() != fd2.getId().getTasa().doubleValue()){
														dMontoDif = d.roundDouble(((dMontoAplicarxFactura * bdTasaPar.doubleValue()) - (dMontoAplicarxFactura*fd2.getId().getTasa().doubleValue())) + d1);
														
														if( Divisas.roundDouble(dMontoDif , 2) > 0 ){
														
															bHecho = rCtrl.grabarRCWithSession(s, fd2.getId().getCodsuc(), fd2.getId().getCodcli(), fd2.getId().getTipofactura(), fd2.getId().getNofactura(), fd2.getId().getPartida(), fd2.getId().getRpdivj(), 
																			 "RG", iNodoc, fecUtil.obtenerFechaActualJuliana(), "R", iNobatch, dMontoDif, "F",  fd2.getId().getMoneda(), bdTasaPar.doubleValue(), 0, 
																			 fd2.getId().getCompenslm(), sCuentaCaja[1], fd2.getId().getCodunineg().trim(), fd2.getId().getTipopago(), "F", sConcepto, "P", fd2.getId().getNomcli(), vAut.getId().getLogin(), vAut.getId().getNomapp(),fd2.getId().getRpddj(),fd2.getId().getRppo(),fd2.getId().getRpdcto());
														}
														
														if(!bHecho){
															lblMensajeError.setValue("No se pudo grabar el diferencial cambiario en el JDE!!!");
															return false;
														}
													}
													
													if (dNewPendienteDom > 0){
														bHecho = rCtrl.aplicarMontoF0311WithSession(s, "A",dNewPendienteDom, dNewPendienteForaneo, vAut.getId().getLogin(), fd2.getId().getCodsuc(), fd2.getId().getTipofactura(), fd2.getId().getNofactura(), fd2.getId().getPartida(),fd2.getId().getCodcli());
														
														if(!bHecho){
															lblMensajeError.setValue("No se pudo aplicar el monto a factura No. "+fd2.getId().getNofactura()+" en el JDE!!! " );
															return false;
														}
													}else{
														bHecho = rCtrl.aplicarMontoF0311WithSession(s, "P",dNewPendienteDom, dNewPendienteForaneo, vAut.getId().getLogin(), fd2.getId().getCodsuc(), fd2.getId().getTipofactura(), fd2.getId().getNofactura(), fd2.getId().getPartida(),fd2.getId().getCodcli());
														
														if(!bHecho){
															lblMensajeError.setValue("No se pudo aplicar el monto a factura No. "+fd2.getId().getNofactura()+" en el JDE!!! " );
															return false;
														}
													}
													
												}else{
													lblMensajeError.setValue("No se pudo grabar el recibo en el JDE!!!");
													return false;
												}
												
												//pasar a siguiente factura
												c++;
												dMontoD = 0;
												dMontoAplicarxFactura = 0;
												d1=0; d2=0;
												mPago.setMonto(dMontoF);
												fd2.setMontoAplicar(BigDecimal.ZERO);
												fd2.getId().setDpendiente(new BigDecimal(dNewPendienteDom));
												fd2.getId().setCpendiente(new BigDecimal(dNewPendienteForaneo));
											}//el pago es mayor al monto de la factura
											else if(d.roundDouble(mPago.getMonto()) == d.roundDouble(fd2.getMontoAplicar().doubleValue())){// el pago es igual al monto de la factura
												dMontoF = d.roundDouble(mPago.getMonto() - fd2.getMontoAplicar().doubleValue());
												dMontoAplicarxFactura = d.roundDouble(dMontoAplicarxFactura + fd2.getMontoAplicar().doubleValue());
												dNewPendienteForaneo = d.roundDouble(fd2.getId().getDpendiente().doubleValue() - dMontoAplicarxFactura);
												if(dNewPendienteForaneo > 0){
													d2 = d.roundDouble(fd2.getMontoAplicar().doubleValue()*fd2.getId().getTasa().doubleValue());	
													dNewPendienteDom = d.roundDouble(fd2.getId().getCpendiente().doubleValue() - d2 - dMontoD);		
													d1 = 0;	
												}else{
													d1 = d.roundDouble(fd2.getMontoAplicar().doubleValue()*fd2.getId().getTasa().doubleValue());
													d1 = d1 + dMontoD;
													d2 = fd2.getId().getCpendiente().doubleValue();	
													dNewPendienteDom = 0;
													if (d1 >= d2 || d1 <= d2){
														d1 = d2 - d1;
														d2 = d.roundDouble(d2 - dMontoD);
													}else{
														d1=0;
														d2 = d.roundDouble(d2 - dMontoD);
													}
												}
												
												//grabar RC
												bHecho = rCtrl.grabarRCWithSession(s, fd2.getId().getCodsuc(), fd2.getId().getCodcli(), fd2.getId().getTipofactura(), fd2.getId().getNofactura(), fd2.getId().getPartida(), fd2.getId().getRpdivj(), 
																	 "RC", iNodoc, fecUtil.obtenerFechaActualJuliana(), "R", iNobatch, d2, "F",  fd2.getMoneda(), fd2.getId().getTasa().doubleValue(), d.roundDouble(fd2.getMontoAplicar().doubleValue()), 
																	 fd2.getId().getCompenslm(), sCuentaCaja[1], fd2.getId().getCodunineg().trim(), fd2.getId().getTipopago(), "I", sConcepto, "", fd2.getId().getNomcli(), vAut.getId().getLogin(), vAut.getId().getNomapp(),fd2.getId().getRpddj(),fd2.getId().getRppo(),fd2.getId().getRpdcto());		

												if(bHecho){
													//si hay diferencial
													if(bdTasaPar.doubleValue() != fd2.getId().getTasa().doubleValue()){
														
														dMontoDif = d.roundDouble(((dMontoAplicarxFactura * bdTasaPar.doubleValue()) - (dMontoAplicarxFactura*fd2.getId().getTasa().doubleValue())) + d1);
														
														if( Divisas.roundDouble(dMontoDif , 2) > 0 ){
															bHecho = rCtrl.grabarRCWithSession(s, fd2.getId().getCodsuc(), fd2.getId().getCodcli(), fd2.getId().getTipofactura(), fd2.getId().getNofactura(), fd2.getId().getPartida(), fd2.getId().getRpdivj(), 
																			 "RG", iNodoc, fecUtil.obtenerFechaActualJuliana(), "R", iNobatch, dMontoDif, "F",  fd2.getId().getMoneda(), bdTasaPar.doubleValue(), 0, 
																			 fd2.getId().getCompenslm(), sCuentaCaja[1], fd2.getId().getCodunineg().trim(), fd2.getId().getTipopago(), "F", sConcepto, "P", fd2.getId().getNomcli(), vAut.getId().getLogin(), vAut.getId().getNomapp(),fd2.getId().getRpddj(),fd2.getId().getRppo(),fd2.getId().getRpdcto());
														}
														
														if(!bHecho){
															lblMensajeError.setValue("No se pudo grabar el diferencial cambiario en el JDE!!!");
															return false;
														}
													}
													
													if (dNewPendienteDom > 0){
														bHecho = rCtrl.aplicarMontoF0311WithSession(s, "A",dNewPendienteDom, dNewPendienteForaneo, vAut.getId().getLogin(), fd2.getId().getCodsuc(), fd2.getId().getTipofactura(), fd2.getId().getNofactura(), fd2.getId().getPartida(),fd2.getId().getCodcli());
														if(!bHecho){
															lblMensajeError.setValue("No se pudo aplicar el monto a factura No. "+fd2.getId().getNofactura()+" en el JDE!!! " );
															return false;
														}
													}else{
														bHecho = rCtrl.aplicarMontoF0311WithSession(s, "P",dNewPendienteDom, dNewPendienteForaneo, vAut.getId().getLogin(), fd2.getId().getCodsuc(), fd2.getId().getTipofactura(), fd2.getId().getNofactura(), fd2.getId().getPartida(),fd2.getId().getCodcli());
														
														if(!bHecho){
															lblMensajeError.setValue("No se pudo aplicar el monto a factura No. "+fd2.getId().getNofactura()+" en el JDE!!! " );
															return false;
														}
													}
													
												}else{
													lblMensajeError.setValue("No se pudo grabar el recibo en el JDE!!!");
													return false;
												}
												
												//pasar a siguiente factura
												c++;
												//pasar a sguiente forma de pago
												b++;
												dMontoD = 0;
												dMontoAplicarxFactura = 0;
												d1=0; d2=0;
												mPago.setMonto(0);
												fd2.setMontoAplicar(BigDecimal.ZERO);
												fd2.getId().setDpendiente(new BigDecimal(dNewPendienteDom));
												fd2.getId().setCpendiente(new BigDecimal(dNewPendienteForaneo));
												break;
											}// fin de el pago es igual al monto de la factura
											else{ 
												
												dMontoF = d.roundDouble(fd2.getMontoAplicar().doubleValue() - mPago.getMonto());
												 
												
												b++;
												//grabarRC
												bHecho = rCtrl.grabarRCWithSession(s, fd2.getId().getCodsuc(), fd2.getId().getCodcli(), fd2.getId().getTipofactura(), fd2.getId().getNofactura(), fd2.getId().getPartida(), fd2.getId().getRpdivj(), 
														 "RC", iNodoc, fecUtil.obtenerFechaActualJuliana(), "R", iNobatch, d.roundDouble(mPago.getMonto()*fd2.getId().getTasa().doubleValue()), "F",  fd2.getMoneda(), fd2.getId().getTasa().doubleValue(), d.roundDouble(mPago.getMonto()), 
														 fd2.getId().getCompenslm(), sCuentaCaja[1], fd2.getId().getCodunineg().trim(), fd2.getId().getTipopago(), "I", sConcepto, "", fd2.getId().getNomcli(), vAut.getId().getLogin(), vAut.getId().getNomapp(),fd2.getId().getRpddj(),fd2.getId().getRppo(),fd2.getId().getRpdcto());
												
												 
												if(!bHecho){
													lblMensajeError.setValue("No se pudo grabar el recibo en el JDE!!!");
													return false;
												}
												//se decremente el monto a apllicar a la factura 
												fd2.setMontoAplicar(new BigDecimal(dMontoF));
												
												 
												dMontoAplicarxFactura = d.roundDouble(dMontoAplicarxFactura + mPago.getMonto());
											 
												dMontoD = d.roundDouble(dMontoAplicarxFactura * fd2.getId().getTasa().doubleValue());
												 
												break;
											}// fin del monto del pago es menor al pago de la factura
										}else{//no incluye la factura
											//pasar a siguiente factura
											c++;
										}//no incluye la factura																					
									}// fin de pagar cada MF,IF y principal de cuota en el F0311
								}// fin pago en moneda domestica
							}else{//no se encontro la cuenta del pago
								bHecho = false;
								lblMensajeError.setValue("No se encontro la cuenta configurada para el metodo de pago: " + mPago.getMetododescrip() + " " + mPago.getMoneda()+" comp: " + fh.getId().getCodcomp());
								return false;
							}// fin de no se encontro la cuenta del pago
						}// fin de while e metodos de pago
					}//fin de pagos a facturas en moneda foranea	
					else{//La factura esta en moneda Domestica
						 
						while (b < lstPagos.size()){// while de metodos de pago
							mPago = (MetodosPago)lstPagos.get(b);
							//leer cuenta de metodo de pago
							sCuentaCaja = d.obtenerCuentaCaja(f55ca01.getId().getCaid(),fh.getId().getCodcomp(),mPago.getMetodo(),mPago.getMoneda(),null,null,null,null);						
							if(sCuentaCaja != null){//se encontro la cuenta del pago	
								if(!mPago.getMoneda().equals(sMonedaBase)){//pago en moneda distinta a la de la factura F:COR P:USD
									while (c < lstComponentes.size()){//pagar cada MF,IF y principal de cuota en el F0311			
										fd2 = (Credhdr)lstComponentes.get(c);										
										if(fd2.getMontoAplicar().doubleValue()>0){//si el monto a aplicar es mayor a 0
											 
											if(d.roundDouble(mPago.getEquivalente()) > d.roundDouble(fd2.getMontoAplicar().doubleValue())){//el pago es mayor al monto d ela factura
												 
												dMontoF = d.roundDouble(mPago.getEquivalente() - fd2.getMontoAplicar().doubleValue());
												 
												dMontoAplicarxFactura = d.roundDouble(dMontoAplicarxFactura + fd2.getMontoAplicar().doubleValue());
												 
												dNewPendienteDom = d.roundDouble(fd2.getId().getCpendiente().doubleValue() - fd2.getMontoAplicar().doubleValue());
												 
												 
											 
												
												//grabar RC
												bHecho = rCtrl.grabarRCWithSession(s, fd2.getId().getCodsuc(), fd2.getId().getCodcli(), fd2.getId().getTipofactura(), fd2.getId().getNofactura(), fd2.getId().getPartida(), fd2.getId().getRpdivj(), 
																	 "RC", iNodoc, fecUtil.obtenerFechaActualJuliana(), "R", iNobatch,  d.roundDouble(fd2.getMontoAplicar().doubleValue()), "D",  fd2.getMoneda(),0,0, 
																	 fd2.getId().getCompenslm(), sCuentaCaja[1], fd2.getId().getCodunineg().trim(), fd2.getId().getTipopago(), "I", sConcepto, "", fd2.getId().getNomcli(), vAut.getId().getLogin(), vAut.getId().getNomapp(),fd2.getId().getRpddj(),fd2.getId().getRppo(),fd2.getId().getRpdcto());
												
												 
												if(!bHecho){
													lblMensajeError.setValue("No se pudo grabar el recibo en el JDE!!!");
													return false;
												}											
												
												if(bHecho){
													
													if (dNewPendienteDom > 0){
														bHecho = rCtrl.aplicarMontoF0311WithSession(s, "A",dNewPendienteDom, 0, vAut.getId().getLogin(), fd2.getId().getCodsuc(), fd2.getId().getTipofactura(), fd2.getId().getNofactura(), fd2.getId().getPartida(),fd2.getId().getCodcli());
													 
														if(!bHecho){
															lblMensajeError.setValue("No se pudo aplicar el monto a factura No. "+fd2.getId().getNofactura()+" en el JDE!!! " );
															return false;
														}
													}else{
														bHecho = rCtrl.aplicarMontoF0311WithSession(s, "P",dNewPendienteDom, 0, vAut.getId().getLogin(), fd2.getId().getCodsuc(), fd2.getId().getTipofactura(), fd2.getId().getNofactura(), fd2.getId().getPartida(),fd2.getId().getCodcli());
														 
														if(!bHecho){
															lblMensajeError.setValue("No se pudo aplicar el monto a factura No. "+fd2.getId().getNofactura()+" en el JDE!!! " );
															return false;
														}
													}

													dMontoAplicarxFactura = 0;

													if(!bHecho){
														lblMensajeError.setValue("No se pudo grabar el diferencial cambiario en el JDE!!!");
														return false;
													}
													
													//pasar a siguiente factura
													c++;
													dMontoD = 0;
													dMontoAplicarxFactura = 0;
													d1=0; d2=0;
													mPago.setEquivalente(dMontoF);
													fd2.setMontoAplicar(BigDecimal.ZERO);
													fd2.getId().setCpendiente(BigDecimal.ZERO);
												}else{
													lblMensajeError.setValue("No se pudo grabar el recibo en el JDE!!!");
													return false;
												}
											}else if(d.roundDouble(mPago.getEquivalente()) == d.roundDouble(fd2.getMontoAplicar().doubleValue())){// el pago es igual al monto de la factura
												
												dMontoF = d.roundDouble(mPago.getEquivalente() - fd2.getMontoAplicar().doubleValue());
												 
												dMontoAplicarxFactura = d.roundDouble(dMontoAplicarxFactura + fd2.getMontoAplicar().doubleValue());
												 
												dNewPendienteDom = d.roundDouble(fd2.getId().getCpendiente().doubleValue() - fd2.getMontoAplicar().doubleValue());
												 
												//grabar RC
												bHecho = rCtrl.grabarRCWithSession(s, fd2.getId().getCodsuc(), fd2.getId().getCodcli(), fd2.getId().getTipofactura(), fd2.getId().getNofactura(), fd2.getId().getPartida(), fd2.getId().getRpdivj(), 
																	 "RC", iNodoc, fecUtil.obtenerFechaActualJuliana(), "R", iNobatch, d.roundDouble(fd2.getMontoAplicar().doubleValue()), "D",  fd2.getMoneda(), 0,0 , 
																	 fd2.getId().getCompenslm(), sCuentaCaja[1], fd2.getId().getCodunineg().trim(), fd2.getId().getTipopago(), "I", sConcepto, "", fd2.getId().getNomcli(), vAut.getId().getLogin(), vAut.getId().getNomapp(),fd2.getId().getRpddj(),fd2.getId().getRppo(),fd2.getId().getRpdcto());																																					
												 
												if(bHecho){
													
													if (dNewPendienteDom > 0){
														bHecho = rCtrl.aplicarMontoF0311WithSession(s, "A",dNewPendienteDom, 0, vAut.getId().getLogin(), fd2.getId().getCodsuc(), fd2.getId().getTipofactura(), fd2.getId().getNofactura(), fd2.getId().getPartida(),fd2.getId().getCodcli());
													 
														if(!bHecho){
															lblMensajeError.setValue("No se pudo aplicar el monto a factura No. "+fd2.getId().getNofactura()+" en el JDE!!! " );
															return false;
														}
													}else{
														bHecho = rCtrl.aplicarMontoF0311WithSession(s, "P",dNewPendienteDom, 0, vAut.getId().getLogin(), fd2.getId().getCodsuc(), fd2.getId().getTipofactura(), fd2.getId().getNofactura(), fd2.getId().getPartida(),fd2.getId().getCodcli());
														
														if(!bHecho){
															lblMensajeError.setValue("No se pudo aplicar el monto a factura No. "+fd2.getId().getNofactura()+" en el JDE!!! " );
															return false;
														}
													}

													dMontoAplicarxFactura = 0;

													if(!bHecho){
														lblMensajeError.setValue("No se pudo grabar el diferencial cambiario en el JDE!!!");
														return false;
													}
													//pasar a siguiente factura
													c++;
													//pasar a sguiente forma de pago
													b++;
													dMontoD = 0;
													dMontoAplicarxFactura = 0;
													d1=0; d2=0;
													mPago.setEquivalente(0);
													fd2.setMontoAplicar(BigDecimal.ZERO);
													break;													
												}else{
													lblMensajeError.setValue("No se pudo grabar el recibo en el JDE!!!");
													return false;
												}

											}else{// el monto del pago es menor al pago de la factura												
												///
											 
												dMontoF = d.roundDouble(fd2.getMontoAplicar().doubleValue() - mPago.getEquivalente());
												 
												b++;
												//grabarRC																																					
												bHecho = rCtrl.grabarRCWithSession(s, fd2.getId().getCodsuc(), fd2.getId().getCodcli(), fd2.getId().getTipofactura(), fd2.getId().getNofactura(), fd2.getId().getPartida(), fd2.getId().getRpdivj(), 
														 "RC", iNodoc, fecUtil.obtenerFechaActualJuliana(), "R", iNobatch, d.roundDouble(mPago.getEquivalente()), "D",  fd2.getMoneda(), 0,0, 
														 fd2.getId().getCompenslm(), sCuentaCaja[1], fd2.getId().getCodunineg().trim(), fd2.getId().getTipopago(), "I", sConcepto, "", fd2.getId().getNomcli(), vAut.getId().getLogin(), vAut.getId().getNomapp(),fd2.getId().getRpddj(),fd2.getId().getRppo(),fd2.getId().getRpdcto());	
												 
												if(!bHecho){
													lblMensajeError.setValue("No se pudo grabar el recibo en el JDE!!!");
													return false;
												}
												//se decremente el monto a apllicar a la factura 
												fd2.setMontoAplicar(new BigDecimal(dMontoF));
												 
												fd2.getId().setCpendiente(new BigDecimal(d.roundDouble(fd2.getId().getCpendiente().doubleValue() - mPago.getEquivalente())));
												dMontoAplicarxFactura = d.roundDouble(dMontoAplicarxFactura + mPago.getEquivalente());
												 
												break;
											}// fin del monto del pago es menor al pago de la factura											
										}//Fin de cuando el monto a aplicar es mayor a 0
										else{//no incluye la factura
											//pasar a siguiente factura
											c++;
										}//no incluye la factura	
									}//Fin de while de cuotas
								}// fin de pago en moneda foranea
								else{//pago en moneda domestica F:COR P:COR 
									 
									while (c < lstComponentes.size()){//pagar cada MF,IF y principal de cuota en el F0311	
										fd2 = (Credhdr)lstComponentes.get(c);	
										if(fd2.getMontoAplicar().doubleValue()>0){//si el monto a aplicar es mayor a 0
											 
											if(d.roundDouble(mPago.getMonto()) > d.roundDouble(fd2.getMontoAplicar().doubleValue())){//el pago es mayor al monto de la factura
												
												dMontoF = d.roundDouble(mPago.getMonto() - fd2.getMontoAplicar().doubleValue());
												
												dMontoAplicarxFactura = d.roundDouble(dMontoAplicarxFactura + fd2.getMontoAplicar().doubleValue());
												
												dNewPendienteDom = d.roundDouble(fd2.getId().getCpendiente().doubleValue() - d.roundDouble(fd2.getMontoAplicar().doubleValue()));
												
												//grabar RC
												bHecho = rCtrl.grabarRCWithSession(s, fd2.getId().getCodsuc(), fd2.getId().getCodcli(), fd2.getId().getTipofactura(), fd2.getId().getNofactura(), fd2.getId().getPartida(), fd2.getId().getRpdivj(), 
																	 "RC", iNodoc, fecUtil.obtenerFechaActualJuliana(), "R", iNobatch, d.roundDouble(fd2.getMontoAplicar().doubleValue()), "D",  fd2.getMoneda(), 0 , 0, 
																	 fd2.getId().getCompenslm(), sCuentaCaja[1], fd2.getId().getCodunineg().trim(), fd2.getId().getTipopago(), "I", sConcepto, "", fd2.getId().getNomcli(), vAut.getId().getLogin(), vAut.getId().getNomapp(),fd2.getId().getRpddj(),fd2.getId().getRppo(),fd2.getId().getRpdcto());
												
												
												if(bHecho){																										
													
													if (dNewPendienteDom > 0){
														bHecho = rCtrl.aplicarMontoF0311WithSession(s, "A",dNewPendienteDom, 0, vAut.getId().getLogin(), fd2.getId().getCodsuc(), fd2.getId().getTipofactura(), fd2.getId().getNofactura(), fd2.getId().getPartida(),fd2.getId().getCodcli());
														
														if(!bHecho){
															lblMensajeError.setValue("No se pudo aplicar el monto a factura No. "+fd2.getId().getNofactura()+" en el JDE!!! " );
															return false;
														}
													}else{
														bHecho = rCtrl.aplicarMontoF0311WithSession(s, "P",dNewPendienteDom, 0, vAut.getId().getLogin(), fd2.getId().getCodsuc(), fd2.getId().getTipofactura(), fd2.getId().getNofactura(), fd2.getId().getPartida(),fd2.getId().getCodcli());
														
														if(!bHecho){
															lblMensajeError.setValue("No se pudo aplicar el monto a factura No. "+fd2.getId().getNofactura()+" en el JDE!!! " );
															return false;
														}
													}

													dMontoAplicarxFactura = 0;

													if(!bHecho){
														lblMensajeError.setValue("No se pudo grabar el diferencial cambiario en el JDE!!!");
														return false;
													}

													//pasar a siguiente factura
													c++;
													dMontoD = 0;
													dMontoAplicarxFactura = 0;
													d1=0; d2=0;
													mPago.setMonto(dMontoF);
													fd2.setMontoAplicar(BigDecimal.ZERO);
													fd2.getId().setCpendiente(new BigDecimal(dNewPendienteDom));
												}else{
													lblMensajeError.setValue("No se pudo grabar el recibo en el JDE!!!");
													return false;
												}
											}//el pago es mayor al monto de la factura
											else if(d.roundDouble(mPago.getMonto()) == d.roundDouble(fd2.getMontoAplicar().doubleValue())){// el pago es igual al monto de la factura
												 
												dMontoF = d.roundDouble(mPago.getMonto() - fd2.getMontoAplicar().doubleValue());
											 
												dMontoAplicarxFactura = d.roundDouble(dMontoAplicarxFactura + fd2.getMontoAplicar().doubleValue());
												 
												dNewPendienteForaneo = d.roundDouble(fd2.getId().getDpendiente().doubleValue() - dMontoAplicarxFactura);
												 
												//grabar RC
												bHecho = rCtrl.grabarRCWithSession(s, fd2.getId().getCodsuc(), fd2.getId().getCodcli(), fd2.getId().getTipofactura(), fd2.getId().getNofactura(), fd2.getId().getPartida(), fd2.getId().getRpdivj(), 
																	 "RC", iNodoc, fecUtil.obtenerFechaActualJuliana(), "R", iNobatch, d.roundDouble(fd2.getMontoAplicar().doubleValue()), "D",  fd2.getMoneda(), 0, 0, 
																	 fd2.getId().getCompenslm(), sCuentaCaja[1], fd2.getId().getCodunineg().trim(), fd2.getId().getTipopago(), "I", sConcepto, "", fd2.getId().getNomcli(), vAut.getId().getLogin(), vAut.getId().getNomapp(),fd2.getId().getRpddj(),fd2.getId().getRppo(),fd2.getId().getRpdcto());		
												
												if(!bHecho){
													lblMensajeError.setValue("No se pudo grabar el recibo en el JDE!!!");
													return false;
												}
												//pasar a siguiente factura
												c++;
												//pasar a sguiente forma de pago
												b++;
												dMontoD = 0;
												dMontoAplicarxFactura = 0;
												d1=0; d2=0;
												mPago.setMonto(0);
												fd2.setMontoAplicar(BigDecimal.ZERO);
												break;
											}// fin de el pago es igual al monto de la factura
											else{// el monto del pago es menor al pago de la factura	
												
												dMontoF = d.roundDouble(fd2.getMontoAplicar().doubleValue() - mPago.getMonto());
											
												//grabarRC
												bHecho = rCtrl.grabarRCWithSession(s, fd2.getId().getCodsuc(), fd2.getId().getCodcli(), fd2.getId().getTipofactura(), fd2.getId().getNofactura(), fd2.getId().getPartida(), fd2.getId().getRpdivj(), 
														 "RC", iNodoc, fecUtil.obtenerFechaActualJuliana(), "R", iNobatch,d.roundDouble(mPago.getMonto()), "D",  fd2.getMoneda(), 0, 0, 
														 fd2.getId().getCompenslm(), sCuentaCaja[1], fd2.getId().getCodunineg().trim(), fd2.getId().getTipopago(), "I", sConcepto, "", fd2.getId().getNomcli(), vAut.getId().getLogin(), vAut.getId().getNomapp(),fd2.getId().getRpddj(),fd2.getId().getRppo(),fd2.getId().getRpdcto());
												
												if(!bHecho){
													lblMensajeError.setValue("No se pudo grabar el recibo en el JDE!!!");
													return false;
												}
												//se decremente el monto a apllicar a la factura 
												fd2.setMontoAplicar(new BigDecimal(dMontoF));
												dMontoAplicarxFactura = d.roundDouble(dMontoAplicarxFactura + mPago.getMonto());
												
												//pasar a sguiente forma de pago
												b++;
												break;
											}// fin del monto del pago es menor al pago de la factura
										}//fin de si monto a aplicar > 0
										else{//no incluye la factura
											//pasar a siguiente factura
											c++;
										}//no incluye la factura		
									}//Fin de pagar cada MF,IF y principal de cuota en el F0311
								}//fin de pago en moneda Foranea
							}else{//no se encontro la cuenta del pago
								bHecho = false;
								lblMensajeError.setValue("No se encontro la cuenta configurada para el metodo de pago: " + mPago.getMetododescrip() + " " + mPago.getMoneda()+" comp: " + fd2.getId().getCodcomp());
								return false;
							}// fin de no se encontro la cuenta del pago
						}//fin de while de metodos pago
					}// Fin de factura en moneda Domestica
				}else{
					bHecho = false;
					lblMensajeError.setValue("No se encontro el numero de recibo para el RC ");
				}
			}else{
				bHecho = false;
				lblMensajeError.setValue("No se encontro el numero de batch para el RC ");
			}
			
			if(bHecho){
				int iNumrec = Integer.parseInt(m.get("iNumRecFinan").toString());
				//grabar Batch
				 
				bHecho = recCtrl.registrarBatchA92WithSession(s,"R", iNobatch, iTotalTransaccion, vAut.getId().getLogin(), 1, MetodosPagoCtrl.DEPOSITO);
				 
				
				if(bHecho){
					bHecho = recCtrl.fillEnlaceMcajaJde(s,tx,iNumrec, fh.getId().getCodcomp(), iNodoc, iNobatch,f55ca01.getId().getCaid(),f55ca01.getId().getCaco(),"R",valoresJDEInsFinanciamiento[0]);
					 
					if(bHayFicha && bHecho){
						String[] sCuentaCajaDom = d.obtenerCuentaCaja(f55ca01.getId().getCaid(),fh.getId().getCodcomp(),MetodosPagoCtrl.EFECTIVO,sMonedaBase,null,null,null,null);
						sCuentaCaja = d.obtenerCuentaCaja(f55ca01.getId().getCaid(),fh.getId().getCodcomp(),MetodosPagoCtrl.EFECTIVO,"USD",null,null,null,null);
						int iNoficha = Integer.parseInt(m.get("iNoFichaFinan").toString());
						bHecho = generarAsientosFichaCV(s,
								tx, lstPagoFicha,
								f55ca01, fd2, sTipoCliente, vAut,
								sCuentaCaja,
								sCuentaCajaDom, 
								iNoficha,
								iNumrec);
						if(!bHecho){
							lblMensajeError.setValue("No se pudo Grabar la ficha de compra venta!!!");
							return false;
						}							
					}
					
					if (!bHecho){
						lblMensajeError.setValue("No se pudo grabar el enlace entre el recibo y el JDE");
					}else{
						
						//ACTUALIZAR INFO DEL ULTIMO PAGO DEL CLIENTE
						Pago p = null;
						p = empCtrl.getultimoPagoWithSession(s, fd2.getId().getCodcli());
						if(p != null){
							if(p.getFecha() == null){
								bHecho = empCtrl.updUltimoPagoWithSession(s, fd2.getId().getCodcli(), iTotalTransaccion);
							}else if (p.getFecha().equals(new Date())){
								iTotalTransaccion = iTotalTransaccion + d.pasarAentero(p.getMonto().doubleValue());
								bHecho = empCtrl.updUltimoPagoWithSession(s, fd2.getId().getCodcli(), iTotalTransaccion);
							}else{
								bHecho = empCtrl.updUltimoPagoWithSession(s, fd2.getId().getCodcli(), iTotalTransaccion);
							}
							if(!bHecho){
								lblMensajeError.setValue("No se pudo Actualizar el ultimo pago del cliente!!!");
								return false;
							}
						}
					}
										
				}else{
					lblMensajeError.setValue("No se pudo grabar el Batch del recibo");
				}//validacion de insercion del batch
			}
		}catch(Exception ex){
			ex.printStackTrace(); 
			bHecho = false;
			lblMensajeError.setValue("No se pudo realizar la operacion!!! " + ex);
		}
		return bHecho;
	}

	
	/** Grabar el recibo. **/
	@SuppressWarnings("unchecked")
	public void grabarRecibo(ActionEvent ev){
		//Connection cn = null;

		Session sessionInsJdeCaja = null;
		Transaction transInsJdeCaja = null;
		
		F55ca014 dtComp = null;
		
		boolean aplicado = true;
		boolean bHayPagoSocket = false;
		boolean bAplicadoSockP = false ;
		boolean bHayFicha = false;
		
		int caid = 0;
		int numrec = 0;
		int fechajul = 0;
		
		String msgProceso = new String("");
		String codsuc     = new String("");
		String codcomp    = new String("");
		String tiporec    = new String(valoresJDEInsFinanciamiento[0]); 
		String monedabase = new String("");
				
		BigDecimal tasaofi  = BigDecimal.ONE;
		BigDecimal tasapara = BigDecimal.ONE;
		
		List<MetodosPago> lstMetodosPago  = null;
		List<Finanhdr> lstCreditosFinan = null;
		List<Finandet> lstFacturasSelected  = null;
		List<Credhdr> lstComponentes  = null;
		
		Finanhdr f = null;
		
		List<String[]>dtaFacs = new ArrayList<String[]>();
		List<String[]>dtaFacsTemp = new ArrayList<String[]>();
		int iNumrecTmp = 0 ;
		Date fechaRecibo = new Date();
		int fechajuliana = FechasUtil .DateToJulian(fechaRecibo);
		
		Object[] dtaCnDnc = null;
		boolean cnDonacion = false;
		boolean aplicadonacion = false;
		List<MetodosPago>pagosConDonacion;
		ReciboCtrl rctrl  = new ReciboCtrl();
		
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
			
			LogCajaService.CreateLog("FinanciamientoDAO.grabarRecibo - INICIO", "JR8", "Inicio ejecucion Metodo");
			
			CodeUtil.removeFromSessionMap("Fn_MontoAplicadoRecibo");
					
			//&& ========== solo para insert del recibo de financimiento
			sessionInsJdeCaja = HibernateUtilPruebaCn.currentSession();
			transInsJdeCaja = sessionInsJdeCaja.beginTransaction();
			
			
			String sCajero = (String)m.get("sNombreEmpleado");	
			Vf55ca01 caja = ((List<Vf55ca01>) m.get("lstCajas")).get(0);
			Vautoriz vaut = ((Vautoriz[]) m.get("sevAut"))[0];
			
			lstFacturasSelected = (List<Finandet>) m.get("lstSelectedCuotas");
			lstComponentes   = (List<Credhdr>) m.get("lstComponentes");
			lstCreditosFinan = (List<Finanhdr>)m.get("lstCreditosFinan");
			lstMetodosPago   = (ArrayList<MetodosPago>) m.get("lstPagosFinan");
			
			f = lstCreditosFinan.get(0);
			caid = caja.getId().getCaid();
			codcomp = f.getId().getCodcomp();
			codsuc =caja.getId().getCaco();
			
			dtComp = com.casapellas.controles.tmp.CompaniaCtrl
							.filtrarCompania((F55ca014[])m.get
							("cont_f55ca014"), codcomp);
			monedabase = dtComp.getId().getC4bcrcd();
			
			//***********************************************************************
			//&& ======= Crear la estructura de cadenas para validacion de Recibofac
			for (Credhdr cr : lstComponentes) {
				
				if(cr.getMontoAplicar().compareTo(BigDecimal.ZERO) == 0)
					continue;
				
				cr.setMontoAplicar( cr.getMontoAplicar().setScale(2,RoundingMode.HALF_UP) );
				
				String[] dta = new String[] {
						String.valueOf(cr.getNofactura()),
						cr.getMontoAplicar().toString(), 
						cr.getPartida(),
						cr.getId().getCodunineg(),
						cr.getId().getCodsuc(),
						cr.getTipofactura() };
				dtaFacs.add(dta);
				
				String[] dtaTemp = new String[] {
						String.valueOf(cr.getNofactura()),
						String.valueOf(cr.getId().getCpendiente()),
						String.valueOf(cr.getId().getPartida()),
						String.valueOf(cr.getId().getRppo()),
						String.valueOf(cr.getId().getCodsuc()),
						String.valueOf(cr.getId().getTipofactura()) };
				dtaFacsTemp.add(dtaTemp);
			}
			
			// Validar si la lista de factura a aplicar es igual a cero
			if (dtaFacs == null || dtaFacs.size() <= 0) {
				throw new Exception("No se encontraron facturas a aplicar.");
			}
			
			// Validar que si es una cuota adelantada que solo pueda estar en aplicar a principal
			Boolean esCuoataAdelantada = CodeUtil.getFromSessionMap("esCuotaAdelantada") != null 
										? (Boolean)CodeUtil.getFromSessionMap("esCuotaAdelantada") : false;
										
			if (esCuoataAdelantada && !chkPrincipal.isChecked()) {
				throw new Exception("Esta aplicando a una cuota adelantada y no se tiene la opcion Abono a Principal Seleccionada.");
			}

			
			//***********************************************************************
			
			//&& ========= Validar pago con socket.
			for (MetodosPago m : lstMetodosPago) {
				bHayPagoSocket = (m.getMetodo().compareTo(MetodosPagoCtrl.TARJETA) == 0 && 
								  m.getVmanual().compareTo("2") == 0 ) ;
				if( bHayPagoSocket ) break;
			}
			//&& ========= aplicar el pago de Socket Pos msgSocket = ""; 
			if( bHayPagoSocket ){
				msgProceso =  PosCtrl.aplicarPagoSocketPos( 
									lstMetodosPago, f.getNomcli(), 
									caid, codcomp, tiporec);
				aplicado = msgProceso.compareTo("") == 0;
				bAplicadoSockP = aplicado;
				if(!aplicado) return;
			}
			//&& ======== tasas de cambio.
			tasaofi  = tasaOficial("COR");
			tasapara = tasaParalela("COR");
			
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
			
			aplicado = insertarRecibo(sessionInsJdeCaja, transInsJdeCaja, caja, f, lstMetodosPago, vaut, lstComponentes);
			
			if(!aplicado){
				msgProceso = "Recibo no aplicado, error al grabar los datos de recibo caja ";
				throw new Exception(msgProceso);
			}
			numrec = Integer.parseInt(String.valueOf( m.get("iNumRecFinan")) );
			
			aplicado = rctrl.procesoPagoFacturaWithSession(dtaFacs, caid, codcomp, fechajuliana, f
					.getId().getCodcli(), tiporec,sessionInsJdeCaja,numrec);

			if(!aplicado){
				msgProceso = "Recibo no aplicado, error al grabar los datos de recibo cajafac ";
				throw new Exception(msgProceso);
			}
			sacarCambioMetodos(sessionInsJdeCaja, transInsJdeCaja, caja,vaut,lstMetodosPago, f, sCajero, numrec, monedabase);
			
			
			//&& ================== crear historico de los saldos iniciales antes del pago 
			
			boolean abonoPrincipal = chkPrincipal.isChecked();
			
			String codunineg = CodeUtil.getFromSessionMap("Fn_CodUninegRecibo").toString();
			aplicado = crearHistoricoCuotaFinancimiento(sessionInsJdeCaja, lstFacturasSelected, caid, numrec, 
					new Date(), new Date(), vaut.getId().getCodreg(), vaut.getId().getLogin(), codunineg ,
					abonoPrincipal );
			
			if(!aplicado){
				msgProceso = " No se ha podido actualizar montos de abono a principal en tabla de pagos de amortización ";
				throw new Exception(msgProceso);
			}
			
			//Invocar Tipo de documentos Financiados
			ClsTipoDocumentoFinanciamiento clsTipoDocumentoFinanciado = new ClsTipoDocumentoFinanciamiento();
			String strResultadoTipoDocumentoFinanciado = clsTipoDocumentoFinanciado.getConfiguracionTipoDocumento(null, 
																													null, 
																													null).getDataAsString(); 
			
			Gson gson = new Gson();
			
			TipoDocumentoFinanciamiento[] lstTipoDocFinanciamiento =  gson.fromJson(strResultadoTipoDocumentoFinanciado, 
																					TipoDocumentoFinanciamiento[].class);
			
			if(lstTipoDocFinanciamiento.length==0)
			{
				aplicado = false;
				msgProceso = " No se encuentre tipos de documentos asociados al financiamiento "
						   + "en la tabla de configuración de tipo de documentos financiado ";
				
				throw new Exception(msgProceso);
			}
			
			aplicado = actualizarPagoModuloFinan( sessionInsJdeCaja, lstFacturasSelected, 	lstComponentes,  
					vaut.getId().getLogin(), lstTipoDocFinanciamiento) ;
		 
			if(!aplicado){
				msgProceso = "Recibo no aplicado, error al aplicar el pago a cuotas del financimiento.";
				throw new Exception(msgProceso);
			}
			
			List<MetodosPago> lstPagoFicha = (List<MetodosPago>) (m.get("lstPagoFichaFinan"));
			
			bHayFicha = (lstPagoFicha != null && !lstPagoFicha.isEmpty());
			
			
			//&& ===============================================================================//
			//&& ================ pagar facturas de credito en cuentas po cobrar ===============// 

			List<String> numerosRecibosJde = new ArrayList<String>();
			
			for (int i = 0; i < lstMetodosPago.size(); i++) {
				int numeroReciboJde = Divisas.numeroSiguienteJdeE1Custom( valoresJdeNumeracion[0],valoresJdeNumeracion[1] );
				numerosRecibosJde.add(String.valueOf(numeroReciboJde));
			}
			
			if(numerosRecibosJde.isEmpty()) {
				aplicado = false;
				msgProceso = "Error al generar consecutivo número de documento para el recibo";
				throw new Exception(msgProceso);
			}
			
			int numeroBatchJde = Divisas.numeroSiguienteJdeE1(  );
			int numeroReciboJde = Integer.parseInt( numerosRecibosJde.get(0) ) ;
			
			if(numeroBatchJde == 0) {
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
			
			ppf.facturas = lstComponentes ;
			ppf.formasdepago = lstMetodosPago ;
			ppf.tasaCambioRecibo = tasaofi ;  
			ppf.tasaCambioOficial = tasaofi;  
		
			ppf.numeroBatchJde = String.valueOf(numeroBatchJde);
			ppf.numeroReciboJde = String.valueOf(numeroReciboJde);
			ppf.numerosReciboJde = numerosRecibosJde;
		
			ppf.codigousuario = vaut.getId().getCodreg();
			ppf.usuario = vaut.getId().getLogin();
			ppf.programaActualiza = PropertiesSystem.CONTEXT_NAME;
			ppf.moduloSistema = "RFINANCIA";
			ppf.ajustarMontoAplicado = false;
			ppf.valoresJDEIns = valoresJDEInsFinanciamiento ;
			
			ppf.msgProceso = "";
			ppf.procesarPagosFacturas(sessionInsJdeCaja);
			
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
					LogCajaService.CreateLog(querys[1], "QRY", querys[0]);
					
					int rows = sessionInsJdeCaja.createSQLQuery( querys[0] ).executeUpdate() ;
					
					if( rows == 0 ){
						LogCajaService.CreateLog(querys[1], "ERR", "");
						aplicado = false;
						msgProceso = "error al procesar: " + querys[1];
						throw new Exception(msgProceso);
					}
					
					
				} catch (Exception e) {
					LogCajaService.CreateLog(querys[1], "ERR", e.getMessage());
					aplicado = false;					
					msgProceso = "fallo en interfaz Edwards "+ querys[1] ;					
					throw new Exception(msgProceso);
				}
			}
			
			
			//&& ===============================================================================//
			//&& ========= grabar en recibojde enlace de recibo caja recibo edwards=============// 
			
			aplicado =  ReciboCtrl.crearRegistroReciboCajaJde(sessionInsJdeCaja, caid, numrec, codcomp, codsuc, tiporec, "R", numeroBatchJde, numerosRecibosJde);
			if(!aplicado){
				 msgProceso = "Error al generar registro de asociación de Caja a JdEdward's @recibojde" ;
				 throw new Exception(msgProceso);
			}
			
			//&& ===============================================================================//
			//&& ================ compra venta de dolares por cambio mixto ===== ===============// 
			
			if(aplicado && bHayFicha  ){
				
				int numeroReciboFicha = Integer.parseInt( String.valueOf( CodeUtil.getFromSessionMap( "iNoFichaFinan" ) ) );
				BigDecimal montoCambio = new BigDecimal(String.valueOf( CodeUtil.getFromSessionMap("cr_MontoFichaCambio") ) );
			
				msgProceso = ReciboCtrl.batchCompraVentaCambios(sessionInsJdeCaja, numeroReciboFicha, numrec, caid, codcomp,  
						codsuc, montoCambio, tasaofi, fechaRecibo,  vaut.getId().getLogin(), 
						vaut.getId().getCodreg(), monedabase );
				
				aplicado = msgProceso.isEmpty();
				
				if(!aplicado){
					throw new Exception(msgProceso);
				}
			}
			
			//&& ===============================================================================//
			//&& ========================= donaciones registradas ==============================// 
			if(aplicado && aplicadonacion ){

				cnDonacion = true;
				
				
				DonacionesCtrl.caid = caid;
				DonacionesCtrl.codcomp = codcomp;
				DonacionesCtrl.numrec = numrec;
				DonacionesCtrl.tiporecibo = tiporec;
				
				aplicado = DonacionesCtrl.grabarDonacion(sessionInsJdeCaja, pagosConDonacion, vaut, f.getId().getCodcli() );
				
				if( !aplicado ){
					msgProceso = DonacionesCtrl.msgProceso;
					if(msgProceso == null || msgProceso.isEmpty()) {
						msgProceso = "Error al grabar la donacion ";
					}else{  msgProceso = "Procesar Donacion: " + msgProceso; }
					
					throw new Exception(msgProceso);
				}
				 
				
			}
			
			//---------------------------------------------
			//+++++++++++++++++++++++++++++++++++++++++++++
			if(aplicado && bHayFicha ) {
				
				int iNoFicha = Integer.parseInt(String.valueOf(m.get("iNoFichaFinan")));
				
				com.casapellas.controles.tmp.ReciboCtrl.setNoFichaRecibo( numrec, iNoFicha, caid, codcomp, tiporec  );
				
			}
			
			//&& ====================== Actualizacion de F5503am para pago de principal, actualizacion de monto de saldo pendiente.
			if( aplicado && chkPrincipal.isChecked() ){	
				aplicado = ajustarCuotaPagoPrincipal(lstFacturasSelected, lstComponentes);
			}
			
			//&& ====================== Actualizacion de F5503am para pago de principal cuando los intereses no han sido generados totalmente pero el pricipal ya fue pagado.
			if( aplicado && !chkPrincipal.isChecked() ){	
				aplicado = ajustarCuotaPagoPrincipalAdelantado(lstFacturasSelected, lstComponentes);
			}
			
			if(!aplicado){
				msgProceso = "Recibo no aplicado, error al ajustar las cuotas del financimiento por adelanto a principal.";
				throw new Exception(msgProceso);
			}
			//	
			
		} catch (Exception e) {
			LogCajaService.CreateLog("grabarRecibo", "ERR", e.getMessage());
			e.printStackTrace();
			msgProceso = "Recibo no aplicado, intente nuevamente";
			aplicado = false;
		}finally{
			
			try
			{
				//++++++++++++++++++++++++++++++++++++++++
				//Agregado por LFonseca
				//----------------------------------------
				Vf55ca01 caja = (Vf55ca01) ((List) m.get("lstCajas")).get(0);
				int caid2 = caja.getId().getCaid();	
				int iCajero = caja.getId().getCacati();
				 
				List<Finandet> lstFacturasSelected2 = (List<Finandet>) m.get("lstSelectedCuotas");
				VerificarFacturaProceso verificarFac = new VerificarFacturaProceso();
				for(Object o : lstFacturasSelected2){
					Finandet fd2 = (Finandet)o;
					 
					String[] strResultado =  verificarFac.actualizarVerificarFactura(String.valueOf(caid2), 
															String.valueOf(fd2.getId().getCodcli()), 
															String.valueOf(fd2.getId().getNosol()), 
															fd2.getId().getTiposol(), 
															"", 
															String.valueOf(iCajero));
					 
					
				}
				
				
			}
			catch (Exception e) {
				LogCajaService.CreateLog("grabarRecibo", "ERR", e.getMessage());
				e.printStackTrace();
			}
			
			dwCargando.setWindowState("hidden");
			dwRecibo.setWindowState("hidden");
			dwProcesaRecibo.setWindowState("hidden");
			dwMensajeError.setWindowState("normal");
			
	
			
			//&& ======== confirmar las transacciones
			
			
			try {
				if(aplicado) { 
					transInsJdeCaja.commit();
					BitacoraSecuenciaReciboService.actualizarSatisfactorioLogReciboNumerico(caid, numrec, codcomp,codsuc, "FN");
					CodeUtil.removeFromSessionMap("esCuotaAdelantada");
				}
			} catch (Exception e) {
				LogCajaService.CreateLog("grabarRecibo", "ERR", e.getMessage());
				msgProceso = "Error en confirmación de registro de valores: Caja, intente nuevamente";
				aplicado = false;
				e.printStackTrace();
			}
			 
			if(aplicado){
				msgProceso = "Recibo de caja aplicado correctamente " ;
				
				//&& ======= Impresion de recibos
				if(bHayPagoSocket){
					PosCtrl.imprimirVoucher(lstMetodosPago, "V", 
							dtComp.getId().getC4rp01d1(), 
							dtComp.getId().getC4prt());
				}
				 
				com.casapellas.controles.tmp.CtrlCajas.imprimirRecibo (caid, numrec, codcomp, codsuc, tiporec, false);
			}else{
				
				//&& ========== Revertir las transacciones.
				try {
					if (transInsJdeCaja != null){
						transInsJdeCaja.rollback();
					}
				} catch (Exception e) {
					LogCajaService.CreateLog("grabarRecibo", "ERR", e.getMessage());
					e.printStackTrace(); 
				}
				
				 
				
				
				//&& ========== anulacion del Socket
				if(bAplicadoSockP){
					String msgValidaSockPos = PosCtrl.revertirPagosAplicados(lstMetodosPago, caid, codcomp, tiporec);
					msgProceso += msgValidaSockPos ;
				}
				
				//
				
			}
			lblMensajeError.setValue( msgProceso );
			
			try {
				HibernateUtilPruebaCn.closeSession(sessionInsJdeCaja);
			} catch (Exception e) {
				LogCajaService.CreateLog("grabarRecibo", "ERR", e.getMessage());
				e.printStackTrace();
			}			
			
			m.remove("procesandoReciboValidacion");
			
			CodeUtil.removeFromSessionMap(new String[] { "fn_NumeroBatchReciboJde", "lstDatosTrack_Con",
					"iNoFichaFinan", "lstPagoFichaFinan", "iNumRecFinan",
					"lstSelectedCuotas", "lstSelectedCuotas",
					"lstCreditosFinan", "lstPagosFinan" });
			
			m.put("lstCuotas", new ArrayList());
			gvCuotas.dataBind();
			
			LogCajaService.CreateLog("FinanciamientoDAO.grabarRecibo - FIN", "JR8", "Fin ejecucion Metodo");
			
		}
	}
	
	//No se encuentra referencia de uso de este metodo, se propone borrarlo	
	public void verificacionSaldoDocumento(int caid, String codcomp, int fecharecibo_jul, 
			int codcli, int numrec, int nobatch ){
		
		boolean done = true;
		Connection cn = null;
		
		try {
		
			String strSqlSelect = " select * from @BDMCAJA.recibofac where caid = @CAID and trim(codcomp) = '@CODCOMP' " +
					" and numrec = @NUMREC and tiporec = 'FN' and codcli = @CODCLI and fecha = @FECHAJUL " ;
			
			strSqlSelect = 
				strSqlSelect.replace("@BDMCAJA", PropertiesSystem.ESQUEMA)
					.replace("@CAID", Integer.toString(caid))
					.replace("@CODCOMP", codcomp.trim() )
					.replace("@NUMREC", Integer.toString(numrec))
					.replace("@CODCLI", Integer.toString(codcli))
					.replace("@FECHAJUL", Integer.toString(fecharecibo_jul));
			
			List<Recibofac> lstFacturas_recibo = (ArrayList<Recibofac>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSqlSelect, true, Recibofac.class) ;
			
			String strOrCondition = "" ;
			String strOrParamter = "(rpdoc = @RPDOC and rpdct ='@RPDCT' and rpsfx = '@RPSFX' ) or ";
		
			for (Recibofac rf : lstFacturas_recibo) {
				strOrCondition +=  strOrParamter
						.replace("@RPDOC", Integer.toString( rf.getId().getNumfac() ) )
						.replace("@RPDCT", rf.getId().getTipofactura())
						.replace("@RPSFX", rf.getId().getPartida().trim() ) ;
			}
			strOrCondition = strOrCondition.substring(0, strOrCondition.lastIndexOf("or") );
			
			String strQueryF0311 = 
				"select  " +
				/*0*/" CAST(TRIM(RPDCT) AS VARCHAR(2) CCSID 37) rpdct, " +
				/*1*/"rpdoc, " +
				/*2*/"CAST (RPSFX AS VARCHAR(3) CCSID 37) rpsfx, " +
				/*3*/"rpaap, " +
				/*4*/"CAST(TRIM(RPCRCD) AS VARCHAR(3) CCSID 37) rpcrcd, " +
				/*5*/"rpcrr, " +
				/*6*/"CAST(TRIM(rpuser) AS VARCHAR(12) CCSID 37) rpuser," +
				/*7*/"RPDOCM rpdocm," +
				/*8*/"CAST(RPGLC AS VARCHAR(4) CCSID 37) AS COMPENSLM," +
				/*9*/"CAST(RPKCO AS VARCHAR(6) CCSID 37) AS RPKCO, " +
				/*10*/"CAST(RPALPH AS VARCHAR(50) CCSID 37) AS rpalph, " +
				/*11*/"CAST(RPPO AS VARCHAR(8) CCSID 37) AS RPPO, " +
				/*12*/"CAST(RPDCTO AS VARCHAR(2) CCSID 37) AS RPDCTO, " +	
				/*13*/"CAST(RPPTC AS VARCHAR(3) CCSID 37) AS RPPTC, " +	
				/*14*/"CAST(RPMCU AS VARCHAR(12) CCSID 37) AS RPMCU, " +	
				/*15*/"CAST(RPICU AS NUMERIC(8) ) AS RPICU, " +
				/*16*/"(select distinct(f2.rpdocm) from "+PropertiesSystem.JDEDTA+".f0311 f2 where rpicu = "+nobatch+"  fetch first rows only ) " +	
				
				"from (" +
				"	select * from "+PropertiesSystem.JDEDTA+".f0311 where " + strOrCondition +
				" ) f " +
				" where f.rpan8 = " + codcli +
				" and f.rpicut = 'I' " +
				" and f.rpfap = 0 " +
				" and f.rpaap > 0 ";
			
			String str_rpdct;
			String str_rpsfx;
			String str_rpcrcd;
			String str_rpuser;
			String str_compenslm;
			String str_rpkco;
			String str_rpalph;
			String str_rpdcto;
			String str_rpptc;
			String str_rpmcu;
			String str_rppo;
			String str_rpglba;
			String str_rpicu;
			
			String concepto = "Ajuste Diferencial Cambiario";
			int rpdoc;
			int rpdocm;
			BigDecimal tasa_cambio;
			double montodif;
			
			List<String>strSqlUpdates = new ArrayList<String>();
			
			String strSqlUpdate = "update " + PropertiesSystem.JDEDTA +".F0311 " +
					"SET RPAAP = 0 " +
					"WHERE RPDOC = @RPDOC AND RPAN8 = @RPAN8 AND RPSFX = '@RPSFX' AND RPICUT = 'I' " +
					"AND RPDCT = '@RPDCT' AND RPFAP = 0 AND RPAAP > 0 AND RPICU = @RPICU " ;
			
			List<Object[]> dtaF0311s = (ArrayList<Object[]>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(strQueryF0311, true, null);
			
			if(dtaF0311s == null || dtaF0311s.isEmpty() ){
				done = false;
				return;
			}
			
			ReciboCtrl rCtrl = new ReciboCtrl();
			cn = As400Connection.getJNDIConnection("");
			cn.setAutoCommit(false);
			
			for (Object[] dtaF311 : dtaF0311s) {
			
				rpdocm = Integer.valueOf(String.valueOf(dtaF311[16]));
				
				str_rpdct = String.valueOf(dtaF311[0]);
				str_rpkco = String.valueOf(dtaF311[9]);
				str_rpsfx = String.valueOf(dtaF311[2]);
				str_rpcrcd = String.valueOf(dtaF311[4]);
				str_rpalph = String.valueOf(dtaF311[10]);
				str_rpuser = String.valueOf(dtaF311[6]);
				str_rpdcto = String.valueOf(dtaF311[12]);
				str_compenslm = String.valueOf(dtaF311[8]);
				str_rpptc = String.valueOf(dtaF311[13]);
				str_rpmcu = String.valueOf(dtaF311[14]);
				str_rppo = String.valueOf(dtaF311[11]);
				str_rpicu = String.valueOf(dtaF311[15]);
				
				//str_rpglba = rCtrl.obtenerIdCuenta(null, null, str_rpmcu.trim(), "65200", "");  
				String[] fcvCuentaPerdia = DocumuentosTransaccionales.obtenerCuentasFCVPerdida(str_rpkco).split(",",-1);
				String sCtaOb = fcvCuentaPerdia[0];
				String sCtaSub= fcvCuentaPerdia[1];
				str_rpglba = rCtrl.obtenerIdCuenta(null, null, str_rpmcu.trim(), sCtaOb, sCtaSub);  
				
				rpdoc = Integer.valueOf(String.valueOf(dtaF311[1]));
				montodif =  ((BigDecimal)dtaF311[3]).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP).doubleValue() ; // Double.valueOf(String.valueOf(dtaF311[3])) / 100;
				tasa_cambio = (BigDecimal)dtaF311[5];
				  
				done = rCtrl.grabarRC(cn, str_rpkco, codcli, str_rpdct, rpdoc, str_rpsfx, fecharecibo_jul, 
						 "RG", rpdocm, fecharecibo_jul, "R", nobatch, montodif, "F", str_rpcrcd, tasa_cambio.doubleValue(), 0, 
						 str_compenslm, str_rpglba, str_rpmcu.trim(), str_rpptc, "F", 
						 concepto, "P", str_rpalph, str_rpuser, PropertiesSystem.CONTEXT_NAME,
						 fecharecibo_jul, str_rppo, str_rpdcto );
				
				if(!done)
					return;
				
				strSqlUpdates.add( 
					strSqlUpdate
					.replace("@RPDOC", Integer.toString(rpdoc))
					.replace("@RPAN8", Integer.toString(codcli))
					.replace("@RPSFX", str_rpsfx)
					.replace("@RPDCT", str_rpdct)
					.replace("@RPICU", str_rpicu) 
				);
 
			}
			 
			if(!done)
				return;
			
			done = ConsolidadoDepositosBcoCtrl.executeSqlQueries(strSqlUpdates) ;
			
 
		} catch (Exception e) {
			done = false;
			e.printStackTrace(); 
		}finally{
			
			try {

				if (cn != null) {
					if (done) {
						cn.commit();
					} else {
						cn.rollback();
					}
					cn.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}

			cn = null;
			
		}
		
	}
	
	
/*****************************************************************************************************/	
	public void grabarReciboFn(ActionEvent ev){
		boolean bCorrecto = false;
		Finanhdr f = null, fh = null;
		Credhdr fd = null;
		FechasUtil fUtil = new FechasUtil();
		Transaction tx = null;
		Vautoriz[] vautoriz = null;
		Vf55ca01 f55ca01 = null;
		List lstMetodosPago = null,lstFacturasSelected,lstCreditosFinan, lstComponentes;
		int iFecha = 0;
		//Connection cn = null;
		BigDecimal bdTasaJde = BigDecimal.ZERO, bdTasaPar = BigDecimal.ZERO;
		ReciboCtrl recCtrl = new ReciboCtrl();
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		String sMonedaBase = "";
		Session s = null;
		F55ca014[] f14 = null;
		////LogCrtl.sendLogDebgsFinancing("<============> Credito::["+ "Inicio de grabarReciboFn" + "] <============> 2837");
		
		try{
			if(m.get("fn_processRecibo")==null){
				m.put("fn_processRecibo","1");
				f14 = (F55ca014[])m.get("cont_f55ca014");
				String sCajero = (String)m.get("sNombreEmpleado");	
				////LogCrtl.sendLogDebgsFinancing("<===========> sNombreEmpleado : " + sCajero);
				// obtener conexion del datasource
			//	As400Connection as400connection = new As400Connection();		
				//cn = as400connection.getJNDIConnection("DSMCAJA2");
				//cn.setAutoCommit(false);
				//abrir sesion del hibernate
				s = HibernateUtilPruebaCn.currentSession();
				vautoriz = (Vautoriz[]) m.get("sevAut");
				tx = s.beginTransaction();
				
				//obtener fecha actual juliana		
				iFecha = fUtil.obtenerFechaActualJuliana();			
				//obtener tasa oficial del dia		
				bdTasaJde = obtenerTasaOficial();
				bdTasaPar = obtenerTasaParalela("COR");
				////LogCrtl.sendLogDebgsFinancing("<===========> Tasa Oficial / Tasa Paralela : " + bdTasaJde+" : "+bdTasaPar);
				//informacion de caja
				f55ca01 = (Vf55ca01) ((List) m.get("lstCajas")).get(0);
				//metodos de pago del recibo
				lstMetodosPago = (List) m.get("lstPagosFinan");
				//credito seleccionado
				lstCreditosFinan = (List)m.get("lstCreditosFinan");
				f = (Finanhdr)((List)m.get("lstCreditosFinan")).get(0);
				//obtener companias x caja
				sMonedaBase = cCtrl.sacarMonedaBase(f14, f.getId().getCodcomp());
				////LogCrtl.sendLogDebgsFinancing("<===========> MonedaBase : " + sMonedaBase);
				//cuotas seleccionadas
				lstFacturasSelected = (List) m.get("lstSelectedCuotas");
				lstComponentes = (List) m.get("lstComponentes");
				
//				//realizar transaccion de tarjeta
//				if((f55ca01.getId().getCasktpos()+"").equals("Y")){
//					List lstDatos = (List)m.get("lstDatosTrack_Con");
//					bCorrecto = aplicarPagoSocketPos(lstMetodosPago,lstDatos);
//				}else{
//					bCorrecto = true;
//				}
//				
//				//leer nuevamente los datos del socketPOS
//				lstMetodosPago = (List) m.get("lstPagosFinan");
				
				bCorrecto = true;
				if(bCorrecto){
					bCorrecto = insertarRecibo(s,tx,f55ca01,f,lstMetodosPago,vautoriz[0],lstComponentes);
					int iNumRecCredito = Integer.parseInt(m.get("iNumRecFinan").toString());
					////LogCrtl.sendLogDebgsFinancing("<===========> Numero Recibo : " + iNumRecCredito);
					/*seccion para Actualizar el pago en el modulo de Financiamiento*/
					if(bCorrecto){
						//sacar cambio de los pagos en efectivo para enviar solo lo neto a JDE
						
//						ClsTipoDocumentoFinanciamiento clsTipoDocumentoFinanciado = new ClsTipoDocumentoFinanciamiento();
//						String strResultadoTipoDocumentoFinanciado = clsTipoDocumentoFinanciado.getConfiguracionTipoDocumento(null, 
//																																null, 
//																																null).getDataAsString(); 
//						
//						Gson gson = new Gson();
//						
//						lstMetodosPago = sacarCambioMetodos(s,tx,f55ca01,vautoriz[0],lstMetodosPago,f,sCajero,iNumRecCredito,sMonedaBase);							
//						
//						TipoDocumentoFinanciamiento[] lstTipoDocFinanciamiento =  gson.fromJson(strResultadoTipoDocumentoFinanciado, 
//														TipoDocumentoFinanciamiento[].class);
//						
//						if(lstTipoDocFinanciamiento.length==0)
//						{
//							aplicado = false;
//							msgProceso = " No se encuentre tipos de documentos asociados al financiamiento "
//							+ "en la tabla de configuración de tipo de documentos financiado ";
//							
//							return;
//						}
						
						// no en uso
						bCorrecto = actualizarPagoModuloFinan( null /*cn*/, lstFacturasSelected, lstComponentes, 
								vautoriz[0].getId().getLogin(), null );
						
						
						
					}
					
					/*Seccion para pagar el credito*/
					boolean bHayFicha = Boolean.parseBoolean(m.get("bHayFicha").toString());
					List lstPagoFicha = (List)(m.get("lstPagoFichaFinan"));
					if(bCorrecto && lstMetodosPago != null){
						
						bCorrecto = pagarCreditoFinan(s,tx,f55ca01,f,lstFacturasSelected,lstMetodosPago,iFecha,bdTasaJde,bdTasaPar,vautoriz[0],bHayFicha,lstPagoFicha,sMonedaBase,lstComponentes);
						////LogCrtl.sendLogDebgsFinancing("<===========> Seccion para pagar el credito : " + bCorrecto);
					}

					if(bCorrecto && lstMetodosPago != null){
						
						tx.commit();
						
						//&& =========== guardar las transacciones de POS.
						if((f55ca01.getId().getCasktpos()+"").equals("Y")){
							List lstDatos = (List)m.get("lstDatosTrack_Con");
							bCorrecto = aplicarPagoSocketPos(lstMetodosPago,lstDatos);
							////LogCrtl.sendLogDebgsFinancing("<===========> pago Socket Pos : " + bCorrecto);
							if(bCorrecto){
								imprimirVoucher(lstMetodosPago,f.getId().getCodcomp().trim(),"V", f14);
								
								recCtrl.actualizarReferenciasRecibo(iNumRecCredito,f55ca01.getId().getCaid(),
														f.getId().getCodcomp(),f55ca01.getId().getCaco(),
														valoresJDEInsFinanciamiento[0],lstMetodosPago);
							}else{
								
								int iNoficha = (m.get("iNoFichaFinan") == null)? 0:
									Integer.parseInt(String.valueOf(m.get("iNoFichaFinan")));
								
								bCorrecto = anularRecibo(iNumRecCredito,f55ca01.getId().getCaid(), 
												f55ca01.getId().getCaco(),f.getId().getCodcomp(), 
												sMonedaBase,vautoriz, iNoficha, f.getId().getCodcli());
								
								lblMensajeError.getValue().toString();
								bCorrecto = false;
							}
						}
					}
					else{						
						tx.rollback();
						dwRecibo.setWindowState("hidden");
						dwProcesaRecibo.setWindowState("hidden");
						dwMensajeError.setWindowState("normal");
						dwMensajeError.setStyle("width:390px;height:145px");
						m.remove("lstDatosTrack_Con");
					}
					
					//&& ===== Imprimir el recibo.
					if(bCorrecto){
						
						////LogCrtl.sendLogDebgsFinancing("<===========> Imprimir el recibo. : " + bCorrecto);
						dwRecibo.setWindowState("hidden");
						dwProcesaRecibo.setWindowState("hidden");lstCreditosFinan.clear();
						m.put("lstCreditosFinan",lstCreditosFinan);
						m.put("lstCuotas",new ArrayList());
						
						lblMensajeError.setValue("Operación realizada con éxito!!!");
						dwMensajeError.setWindowState("normal");
						dwMensajeError.setStyle("width:390px;height:145px");
						gvCuotas.dataBind();
						BigDecimal bdNumrec = new BigDecimal(iNumRecCredito);
						getP55recibo().setIDCAJA(new BigDecimal(f55ca01.getId().getCaid()));
						getP55recibo().setNORECIBO(bdNumrec);
						getP55recibo().setIDEMPRESA(f.getId().getCodcomp().trim());
						getP55recibo().setIDSUCURSAL(f55ca01.getId().getCaco().trim());
						getP55recibo().setTIPORECIBO(valoresJDEInsFinanciamiento[0]);
						getP55recibo().setRESULTADO("");
						getP55recibo().setCOMANDO("");
						getP55recibo().invoke();
						getP55recibo().getRESULTADO();
						if (bHayFicha) {
							recCtrl.actualizarNoFichaWithSession(s, Integer.parseInt(m.get("iNumRecFinan").toString()), Integer.parseInt(m.get("iNoFichaFinan").toString()),
							f55ca01.getId().getCaid(), f.getId().getCodcomp(), f55ca01.getId().getCaco(), valoresJDEInsFinanciamiento[0]);
							
						}
					}


					m.remove("fn_processRecibo");
					dwRecibo.setWindowState("hidden");
					dwProcesaRecibo.setWindowState("hidden");
					dwMensajeError.setWindowState("normal");
					dwMensajeError.setStyle("width:390px;height:145px");

				}else{
					dwMensajeError.setWindowState("normal");
					dwMensajeError.setStyle("width:390px;height:145px");
					tx.rollback();
					
					m.remove("lstDatosTrack_Con");
				}
			}
		}catch(Exception ex){		
			try{
				tx.rollback();
				
				//cancelar transacciones con socket POS si hubieron 
				//anularPagosSP(lstMetodosPago);
				m.remove("lstDatosTrack_Con");
				////LogCrtl.sendLogDebgsFinancing("<===========> Error Reversion de Transacciones." );
			}catch(Exception ex2){
				ex2.printStackTrace();
			}
			m.remove("fn_processRecibo");
			dwRecibo.setWindowState("hidden");
			dwMensajeError.setWindowState("normal");
			dwProcesaRecibo.setWindowState("hidden");
			lblMensajeError.setValue("Error no se pudo relizar la operación!!! " + ex);
			dwMensajeError.setStyle("width:390px;height:145px");
			ex.printStackTrace();
		}finally{			
			try{
				dwCargando.setWindowState("hidden");				
				s.close();			
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		
		
	}
	
	
	// METODO PARA VALIDAR DESCUANDRE DE F0311
	  private void validateAccount(Finanhdr f,int noFactura, int ncaja, String sMontoRestante) {
	   try {
	    com.casapellas.controles.tmp.ReciboCtrl rcCtrl = new com.casapellas.controles.tmp.ReciboCtrl();
	    List<Object[]> result = rcCtrl.ValidateCustomerAccount(f.getId().getCodcli(), noFactura, ncaja);
	        
	    String strBody = "Cliente descuadrado  -> Codigo: "
	      +  "@codCliente --> No.Factura: "
	      +  "@noFactura <br/> Caja: "
	      +  "@noCaja <br/>"; 
	    if (!result.isEmpty() && String.valueOf(((Object[])result.get(0))[2]).compareToIgnoreCase("0.00")!=0) { 
	     strBody += "Monto Calculado: "
	       + "@montoCalculado <br/> Monto Restante : "
	       + "@montoRestante";
	     strBody = strBody.replace("@codCliente", f.getId().getCodcli()+"").replace("@noFactura",noFactura+"").replace("@noCaja",ncaja+"")
	                 .replace("@montoCalculado", result.get(0)[0]+"").replace("@montoRestante", result.get(0)[1]+"");
	     sendMail(strBody , "Cliente descuadrado Pago Financiamiento R2 - Saldo vs Detalle - Ambiente: "+PropertiesSystem.JDEDTA+" server: "+new PropertiesSystem().URLSIS,result);
	    } else if(!result.isEmpty() && String.valueOf(result.get(0)[1]).compareToIgnoreCase(sMontoRestante)==0){
	      strBody = strBody.replace("@codCliente", f.getId().getCodcli()+"")
	            .replace("@noFactura",noFactura+"").replace("@noCaja",ncaja+"");
	      sendMail(strBody,"Cliente descuadrado Pago Financiamiento R2 - F0311 NO MODIFICADO - Ambiente: "+PropertiesSystem.JDEDTA+" server: "+new PropertiesSystem().URLSIS,result); 
	    }
	   } catch (Exception e) {
	    e.printStackTrace();
	   }
	  }
	
		
		private void sendMail(String strBody,String strTitle,List<Object[]> result) {
			try {

				for (int i = 1; i < result.size(); i++) {
					strBody += "<br/> Metodo de Pago " + i + " : "
							+ result.get(i)[1] + " Moneda: " + result.get(i)[0]
							+ " Monto: " + result.get(i)[2];
				}
				
				MailHelper.SendHtmlEmail(
						new CustomEmailAddress(PropertiesSystem.WEBMASTER_EMAIL_ADRESS, "Módulo de Caja"),
						new CustomEmailAddress(PropertiesSystem.MAIL_BOUNCEADDRESS), null, new CustomEmailAddress(PropertiesSystem.MAIL_BOUNCEADDRESS), 
						strTitle, strBody);
			} catch (Exception e) { 
				e.printStackTrace();
			}
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
public boolean generarIF(Connection cn,Finanhdr fh,Finandet fd, BigDecimal bdTasa,String sUsuario,String sAplicacion, int iFecha,Vf55ca01 f55ca01,Vautoriz v,String sMonedaBase){
	Divisas d = new Divisas();
	CuotaCtrl c = new CuotaCtrl();
	ReciboCtrl rcCtrl = new ReciboCtrl();
	int iNobatch = 0, iNodoc = 0, iMonto = 0;
	boolean bHecho = false,bAsiento = false;
	BigDecimal bdMonto = BigDecimal.ZERO;
	String[] sCuenta = null;
	boolean bUnico = false;
	
	try{
		// obtener conexion del datasource
		As400Connection as400connection = new As400Connection();
		if(cn == null || cn.isClosed()){
			bUnico = true;
			cn = as400connection.getJNDIConnection("DSMCAJA2");
			cn.setAutoCommit(false);
		}
		//leer no de batch
		iNobatch = d.leerActualizarNoBatch();	
		
		if(iNobatch > 0){
			//leer no. de documento y actualizar no. de doc
			iNodoc = c.leerActualizarNoCorriente(fd.getId().getCodsuc());
			if(iNodoc > 0){					
				//insertar IF en F0311					
				bHecho = c.insertarIF(cn, fh, fd, "IF", iNodoc, iFecha, iNobatch, bdTasa, sUsuario,sAplicacion, sMonedaBase);
				
				if(bHecho){
					
					//buscar cuenta para asiento contable de IF
					sCuenta = c.buscarCuentaIntereses(fh,"FI00");
					if(sCuenta != null){
						if(fd.getId().getMoneda().equals(sMonedaBase)){								
							iMonto= d.pasarAentero(fd.getId().getInteres().doubleValue());
							//insertar asiento contable de MF
							bAsiento = c.insertarAsientoInteres(cn, fh, fd, "IF", iNodoc, iFecha, iNobatch, bdTasa, sUsuario,
													sAplicacion,sCuenta,"AA",-1*iMonto,"FI00",sMonedaBase,sCuenta[2],"D");
							if(!bAsiento){
								bHecho = false;
								cn.rollback();
								lblMensajeError.setValue("No se pudo grabar el asiento de tipo AA para la cuota: "+fd.getId().getNocuota()+", solicitud: "+ fd.getId().getNosol()+"!!!");
							}							
						}else{
							iMonto = d.pasarAentero((fd.getId().getInteres().doubleValue()));
							//insertar asiento contable de MF
							bAsiento = c.insertarAsientoInteres(cn, fh, fd, "IF", iNodoc, iFecha, iNobatch, bdTasa, 
																sUsuario,sAplicacion,sCuenta,"CA",-1*iMonto,"FI00",
																fd.getId().getMoneda(),sCuenta[2],"F");
							if(bAsiento){
								iMonto= d.pasarAentero(d.roundDouble((fd.getId().getInteres().multiply(bdTasa).doubleValue())));
								bAsiento = c.insertarAsientoInteres(cn, fh, fd, "IF", iNodoc, iFecha, iNobatch, bdTasa, 
																	sUsuario,sAplicacion,sCuenta,"AA",-1*iMonto,"FI00",
																	sMonedaBase,sCuenta[2],"F");
								if(!bAsiento){
									bHecho = false;
									cn.rollback();
									lblMensajeError.setValue("No se pudo grabar el asiento de tipo AA para la cuota: "+fd.getId().getNocuota()+", solicitud: "+ fd.getId().getNosol()+"!!!");
								}
							}else{
								bHecho = false;
								cn.rollback();
								lblMensajeError.setValue("No se pudo grabar el asiento de tipo CA para la cuota: "+fd.getId().getNocuota()+", solicitud: "+ fd.getId().getNosol()+"!!!");
							}
						}
					}else{
						cn.rollback();
						lblMensajeError.setValue("No se Encontro la cuenta para el credito de los interes moratorios: F4095LB!!!");
					}																																							
				}else{
					cn.rollback();
					lblMensajeError.setValue("No se pudo crear el IF en el modulo de cuentas x cobrar de JDE cuota: "+fd.getId().getNocuota()+", solicitud: "+ fd.getId().getNosol()+"!!!");						
				}
				//					
				//ultima parte	
				if(bAsiento){
					bdMonto = fd.getId().getInteres().add(fd.getId().getImpuesto());
					//bHecho = rcCtrl.registrarBatch(cn, "I", iNobatch, (d.pasarAentero(bdMonto.doubleValue())),  sUsuario, 1, ""); VERSION A7.3
					bHecho = rcCtrl.registrarBatchA92(cn, "I", iNobatch, (d.pasarAentero(bdMonto.doubleValue())),  sUsuario, 1, "");
					if(bHecho){
						//actualizar cuota en el F5503AM
						bHecho = c.actualizarInteresFinanciamiento(cn,fd,iNobatch,sUsuario,sAplicacion,iFecha);		
						//bHecho = false;//para prueba
						if(bHecho){							
							cn.commit();
							//enviar correo a credito y cobranzas
							EmpleadoCtrl ec = new EmpleadoCtrl();
							int iCodCajero = 0;
							Vf0101 vf = null;
							String sTo="",sFrom="",sSubject="",sCliente;
							List<String> lstCc = new ArrayList<String>();
							String sDevolucion,sFactura, sMonto,sCaja,sCompania,sSucursal="",sCc = "";
							Pattern pCorreo = Pattern.compile( "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$" );
							//---- Verificar el código del empleado y su correo.
							iCodCajero = v.getId().getCodreg();
							vf = ec.buscarEmpleadoxCodigo2(iCodCajero);
							if(vf!=null){
								sFrom = vf.getId().getWwrem1().trim().toUpperCase();
								if(!d.validarCuentaCorreo(sFrom))
									sTo = "webmaster@casapellas.com.ni";
							}else{
								sFrom = "webmaster@casapellas.com.ni";
							}
							if(d.validarCuentaCorreo(f55ca01.getId().getCacontmail().trim().toUpperCase()))
								sCc = f55ca01.getId().getCacontmail().trim().toUpperCase();
							
							String sUrl = new Divisas().obtenerURL();
							if(sUrl==null || sUrl.trim().equals("")){
								Aplicacion ap = new Divisas().obtenerAplicacion(v.getId().getCodapp());
								sUrl = (ap==null)?"http://ap.casapellas.com.ni:7080/"+PropertiesSystem.CONTEXT_NAME+"":ap.getUrl().trim();
							}
							c.enviarCorreo("Notificación de Interés Corriente Generado", "Interes Generado", fd.getId().getNosol() + " " + fd.getId().getTiposol(), 
									        sCredyCobMail, sFrom, sCc, "Notificación de Interés Corriente Generado", fd.getId().getNocuota(),fd.getId().getCodcli() + " " + fh.getNomcli(), 
											iNobatch, iNodoc + " IF", (fd.getId().getInteres().doubleValue()+ fd.getId().getImpuesto().doubleValue()) + " " +  fd.getId().getMoneda(), 
											f55ca01.getId().getCaid() + " " + f55ca01.getId().getCaname(), f55ca01.getId().getCaco() + " " + f55ca01.getId().getCaconom() , 
											fh.getId().getCodcomp() +" "+fd.getId().getNomcomp(),sUrl, new Date());
							//
						}else{
							cn.rollback();
							lblMensajeError.setValue("No se pudo actualizar la información de la cuota: "+fd.getId().getNocuota()+", solicitud: "+ fd.getId().getNosol()+"!!!");
						}
					}else{
						cn.rollback();
						lblMensajeError.setValue("No se pudo grabar el batch en el JDE!!!");								
					}
				}					
			}else{
				lblMensajeError.setValue("No se encontro numero de documento para IF generado!!!");
			}
		}else{
			lblMensajeError.setValue("No se encontro numero de batch!!!");
		}
	}catch(Exception ex){
		bHecho = false;
		lblMensajeError.setValue("No se pudo realizar la operacion!!! " + ex);
		ex.printStackTrace();
	}finally{
		if(bUnico){
			try {cn.close();} catch (Exception e) {}
		}
	}
	return bHecho;
}
/***********************************************************************************************/
	public boolean generarMF(Connection cn,Finanhdr fh,Finandet fd, BigDecimal bdTasa,String sUsuario,String sAplicacion, int iFecha,Vf55ca01 f55ca01,Vautoriz v, String sMonedaBase){
		Divisas d = new Divisas();
		CuotaCtrl c = new CuotaCtrl();
		ReciboCtrl rcCtrl = new ReciboCtrl();
		int iNobatch = 0, iNodoc = 0;
		boolean bHecho = false, bAsiento = false;
		BigDecimal bdMonto = BigDecimal.ZERO;
		String[] sCuenta = null;
		int iMonto = 0,iTotalBatch = 0;
		boolean bUnico = false;
		try{
			// obtener conexion del datasource
			As400Connection as400connection = new As400Connection();
			if(cn == null || cn.isClosed()){
				bUnico = true;
				cn = as400connection.getJNDIConnection("DSMCAJA2");
				cn.setAutoCommit(false);
			}
			//leer no de batch
			iNobatch = d.leerActualizarNoBatch();			
			
			if(iNobatch > 0){
				//leer no. de documento y actualizar no. de doc
				iNodoc = c.leerActualizarNoMoratorio(fd.getId().getCodsuc());
				if(iNodoc > 0){			
					
					//insertar MF en F0311					
					bHecho = c.insertarMF(cn, fh, fd, "MF", iNodoc, iFecha, iNobatch, bdTasa, sUsuario,sAplicacion,sMonedaBase);
					//
					if(bHecho){
						//buscar cuenta para asiento contable de MF
						sCuenta = c.buscarCuentaIntereses(fh,"FI05");
						if(sCuenta != null){
							if(fd.getId().getMoneda().equals(sMonedaBase)){
								iMonto= d.pasarAentero(fd.getId().getMora().doubleValue());
								iTotalBatch = iMonto;
							
								//insertar asiento contable de MF
								bAsiento = c.insertarAsientoInteres(cn, fh, fd, "MF", iNodoc, iFecha, iNobatch, bdTasa, 
																	sUsuario,sAplicacion,sCuenta,"AA",-1*iMonto,"FI05",
																	fd.getId().getMoneda(),sCuenta[2],"D");
								if(!bAsiento){
									bHecho = false;
									cn.rollback();
									lblMensajeError.setValue("No se pudo grabar el asiento de tipo AA para la cuota: "+fd.getId().getNocuota()+", solicitud: "+ fd.getId().getNosol()+"!!!");
								}	
								
							}else{
								
								iMonto = d.pasarAentero((fd.getId().getMora().doubleValue()));
								iTotalBatch = iMonto;
								
								//insertar asiento contable de MF
								bAsiento = c.insertarAsientoInteres(cn, fh, fd, "MF", iNodoc, iFecha, iNobatch, bdTasa, 
																	sUsuario,sAplicacion,sCuenta,"CA",-1*iMonto,"FI05",
																	"USD",sCuenta[2],"F");
								if(bAsiento){
//									iMonto = d.pasarAentero(d.roundDouble((fd.getId().getMora().multiply(bdTasa).doubleValue())));
									
									iMonto = d.pasarAentero(d.roundBigDecimal(
												fd.getId().getMora().multiply(
													bdTasa)).doubleValue());
									
									bAsiento = c.insertarAsientoInteres(cn, fh, fd, "MF", iNodoc, iFecha, iNobatch, bdTasa, 
																		sUsuario,sAplicacion,sCuenta,"AA",-1*iMonto,"FI05",
																		sMonedaBase,sCuenta[2],"F");
									if(!bAsiento){
										bHecho = false;
										cn.rollback();
										lblMensajeError.setValue("No se pudo grabar el asiento de tipo AA para la cuota: "+fd.getId().getNocuota()+", solicitud: "+ fd.getId().getNosol()+"!!!");
									}
								}else{
									bHecho = false;
									cn.rollback();
									lblMensajeError.setValue("No se pudo grabar el asiento de tipo CA para la cuota: "+fd.getId().getNocuota()+", solicitud: "+ fd.getId().getNosol()+"!!!");
								}
							}
						}else{
							cn.rollback();
							lblMensajeError.setValue("No se Encontro la cuenta para el credito de los interes moratorios: F4095LB!!!");
						}	
					}else{
						cn.rollback();
						lblMensajeError.setValue("No se pudo crear el MF en el modulo de cuentas x cobrar de JDE cuota: "+fd.getId().getNocuota()+", solicitud: "+ fd.getId().getNosol()+"!!!");
					}				
					//
					//ultima parte	
					if(bAsiento){
						//bdMonto = fd.getId().getInteres().add(fd.getId().getImpuesto());
						//bHecho = rcCtrl.registrarBatch(cn, "I", iNobatch, iTotalBatch,  sUsuario, 1, ""); VERSION A7.3
						bHecho = rcCtrl.registrarBatchA92(cn, "I", iNobatch, iTotalBatch,  sUsuario, 1, "");
						if(bHecho){
							//actualizar cuota en el F5503AM
							bHecho = c.actualizarMoraFinanciamiento(cn,fd,iNobatch,sUsuario,sAplicacion,iFecha);	
							//bHecho = false;//para prueba
							if(bHecho){							
								cn.commit();
								//enviar correo a credito y cobranzas
								EmpleadoCtrl ec = new EmpleadoCtrl();
								int iCodCajero = 0;
								Vf0101 vf = null;
								String sTo="",sFrom="",sSubject="",sCliente;
								List<String> lstCc = new ArrayList<String>();
								String sDevolucion,sFactura, sMonto,sCaja,sCompania,sSucursal="",sCc = "";
								Pattern pCorreo = Pattern.compile( "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$" );
								//---- Verificar el código del empleado y su correo.
								iCodCajero = v.getId().getCodreg();
								vf = ec.buscarEmpleadoxCodigo2(iCodCajero);
								if(vf!=null){
									sFrom = vf.getId().getWwrem1().trim().toUpperCase();
									if(!d.validarCuentaCorreo(sFrom))
										sTo = "webmaster@casapellas.com.ni";
								}else{
									sFrom = "webmaster@casapellas.com.ni";
								}
								if(d.validarCuentaCorreo(f55ca01.getId().getCacontmail().trim().toUpperCase()))
									sCc = f55ca01.getId().getCacontmail().trim().toUpperCase();
								
								String sUrl = new Divisas().obtenerURL();
								if(sUrl==null || sUrl.trim().equals("")){
									Aplicacion ap = new Divisas().obtenerAplicacion(v.getId().getCodapp());
									sUrl = (ap==null)?"http://ap.casapellas.com.ni:7080/"+PropertiesSystem.CONTEXT_NAME+"":ap.getUrl().trim();
								}
								c.enviarCorreo("Notificación de Interés por Mora Generado", "Interes Generado", fd.getId().getNosol() + " " + fd.getId().getTiposol(), 
										        sCredyCobMail, sFrom, sCc, "Notificación de Interés por mora Generado", fd.getId().getNocuota(),fd.getId().getCodcli() + " " + fh.getNomcli(), 
												iNobatch, iNodoc + " MF", fd.getId().getMora().doubleValue() + " " +  fd.getId().getMoneda(), 
												f55ca01.getId().getCaid() + " " + f55ca01.getId().getCaname(), f55ca01.getId().getCaco() + " " + f55ca01.getId().getCaconom() , 
												fh.getId().getCodcomp() +" "+fd.getId().getNomcomp(), sUrl, new Date());
								//
							}else{
								cn.rollback();
								lblMensajeError.setValue("No se pudo actualizar la información de la cuota: "+fd.getId().getNocuota()+", solicitud: "+ fd.getId().getNosol()+"!!!");
							}
						}else{
							cn.rollback();
							lblMensajeError.setValue("No se pudo grabar el batch en el JDE!!!");								
						}
					}																				
				}else{
					lblMensajeError.setValue("No se encontro numero de documento para MF generado!!!");
				}
			}else{
				lblMensajeError.setValue("No se encontro numero de batch!!!");
			}
			
		}catch(Exception ex){
			bHecho = false;
			lblMensajeError.setValue("No se pudo realizar la operacion!!! " + ex);
			ex.printStackTrace();
		}finally{
			if(bUnico){
				try {cn.close();} catch (Exception e) {}
			}
		}
		return bHecho;
	}
/******************************************************************************************************** */
	public List ponerCodigoBanco(List lstMetodosPago) {
		F55ca022[] f55ca022 = null;
		MetodosPago mPago = new MetodosPago();
		String sBanco = "";
		try {
			f55ca022 = (F55ca022[]) m.get("f55ca022");// lista de bancos

			for (int i = 0; i < lstMetodosPago.size(); i++) {
				mPago = (MetodosPago) lstMetodosPago.get(i);
				if (mPago.getMetodo().trim().equals(MetodosPagoCtrl.TRANSFERENCIA) || mPago.getMetodo().trim().equals(MetodosPagoCtrl.DEPOSITO) || mPago.getMetodo().trim().equals(MetodosPagoCtrl.CHEQUE)) {
					sBanco = mPago.getReferencia();
					for (int j = 0; j < f55ca022.length; j++) {
						if (sBanco.trim().equals(f55ca022[j].getId().getBanco().trim())) {mPago.setReferencia(f55ca022[j].getId().getCodb()+ "");
							lstMetodosPago.remove(i);
							lstMetodosPago.add(i, mPago);
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return lstMetodosPago;
	}

/******************************************************************************************************** */
/****************************************************************************************/	
	public boolean insertarRecibo(Session s, Transaction tx, Vf55ca01 f55ca01,Finanhdr hFac,List lstMetodosPago,Vautoriz vAut,  List lstFacturasSelected){
		boolean insertado = true;
		int iNumrec = 0,iNumsol = 0,iNumrecm =0;
		
		com.casapellas.controles.tmp.ReciboCtrl recCtrl = new com.casapellas.controles.tmp.ReciboCtrl();
		
		double dMontoAplicar,dMontoRec,dCambio = 0.0;
		Divisas d = new Divisas();
		String sConcepto,sCajero,sCodunineg = "";
		Solicitud sol = null;
		List lstSolicitud;
		CtrlSolicitud solCtrl = new CtrlSolicitud();
		Date dFecha = new Date();
		try{

			
			if(!ddlTipoRecibo.getValue().toString().equals("01")){
				iNumrecm = Integer.parseInt(txtNumrec.getValue().toString().trim());
			}	
			
			int[] iNumFac = new int[lstFacturasSelected.size()];
			double[] dMonto = new double[lstFacturasSelected.size()];
			String[] sPartida = new String[lstFacturasSelected.size()];
			String[] sCodunineg1 = new String[lstFacturasSelected.size()];
			String[] sCodsuc = new String[lstFacturasSelected.size()];
			String[] sTipoDoc = new String[lstFacturasSelected.size()];
			
			iNumrec = com.casapellas.controles.tmp.ReciboCtrl.generarNumeroRecibo( f55ca01.getId().getCaid(), hFac.getId().getCodcomp() );
		
			if (iNumrec == 0) {
				lblMensajeError.setValue("No se encontro número de recibo " +
						"configurado para la compañia: " 
						+ hFac.getId().getCodcomp() ) ; 
				return false;
			}
			
			BitacoraSecuenciaReciboService.insertarLogReciboNumerico(f55ca01.getId().getCaid(),iNumrec,hFac.getId().getCodcomp(),CodeUtil.pad(f55ca01.getId().getCaco(), 5, "0"),valoresJDEInsFinanciamiento[0]);
			
			if (iNumrec > 0) {
			
				if (m.get("finan_MontoAplicar")== null){
					dMontoAplicar = d.formatStringToDouble(txtMontoAplicar.getValue().toString());
				}else {
					dMontoAplicar = d.formatStringToDouble(m.get("finan_MontoAplicar").toString());
				}	
				
				dMontoRec = d.formatStringToDouble(txtMontoRecibido.getValue().toString());
				 
				if (!txtCambio.getValue().toString().trim().equals("")) {
					dCambio = d.formatStringToDouble(txtCambio.getValue().toString());
				 
				}
				sConcepto = txtConcepto.getValue().toString();
				sCajero = (String) m.get("sNombreEmpleado");
				
				lstSolicitud = (List) m.get("fin_lstSolicitud");				
				sCodunineg = CompaniaCtrl.leerUnidadNegocioPorLineaSucursal(hFac.getId().getCodsuc(), hFac.getId().getLinea()); 
				
				if( sCodunineg.trim().isEmpty() )
					sCodunineg = hFac.getId().getCodsuc().substring(3, 5) + hFac.getId().getLinea().trim();
		
				CodeUtil.putInSessionMap("Fn_CodUninegRecibo", sCodunineg.trim());
				CodeUtil.putInSessionMap("Fn_MontoAplicadoRecibo", Double.toString( dMontoAplicar ) );

				
				//&& ================ validacion para que la sumatoria de pagos no sea distinta a la del monto recibido
				BigDecimal sumMontoEquiv = CodeUtil.sumPropertyValueFromEntityList(lstMetodosPago, "equivalente", false);
				BigDecimal montoRecibido = new BigDecimal( Double.toString(dMontoRec) ) ;
			
				if( 	montoRecibido.compareTo(sumMontoEquiv) != 0 &&  
						montoRecibido.subtract(sumMontoEquiv).abs().compareTo( new BigDecimal("0.01") ) == 1 ) {
					
					lblMensajeError.setValue("La sumatoria de pagos no coincide con el total del monto recibido, favor intentar nuevamente.");
					insertado = false;
					
					String body = 
						"<br>Pago de Recibo de Financiamiento, Suma de equivalentes distinta a monto recibido" +
						"<br>Monto Aplicado: " + dMontoAplicar +
						"<br>Monto recibido: " + montoRecibido +
						"<br>Monto Pagos: " + sumMontoEquiv +
						"<br>caja:"+f55ca01.getId().getCaid()+", compania"+hFac.getId().getCodcomp()+", recibo: "+ iNumrec +
						"<br>cliente: " + hFac.getId().getCodcli()+ " " +hFac.getNomcli() ;
						
						String subject = "Pago de Recibo de Financiamiento, Suma de equivalentes distinta a monto recibido" ;
						SendMailsCtrl.sendSimpleMail(body, null, subject);
					
					return insertado = false;
					
				}
				
				
				// guardar el header del recibo
				insertado = recCtrl.registrarRecibo(s, tx, iNumrec, iNumrecm,
						hFac.getId().getCodcomp(), dMontoAplicar, dMontoRec,
						dCambio, sConcepto, dFecha, dFecha, hFac.getId()
								.getCodcli(), hFac.getNomcli(), sCajero,
						f55ca01.getId().getCaid(), f55ca01.getId().getCaco(),
						vAut.getId().getCoduser(), valoresJDEInsFinanciamiento[0], 0, "", 0, new Date(),
						sCodunineg, "", hFac.getId().getMoneda());
				
				if (insertado) {
					
					//&& =============== guardar detalle de recibo
					lstMetodosPago = ponerCodigoBanco(lstMetodosPago);
					
					//&& =============== Determinar si el pago trabaja bajo preconciliacion y conservar su referencia original
					com.casapellas.controles.BancoCtrl.datosPreconciliacion(lstMetodosPago, f55ca01.getId().getCaid(), hFac.getId().getCodcomp()) ;
					
					insertado = recCtrl.registrarDetalleRecibo(s, tx, iNumrec, iNumrecm,  hFac.getId().getCodcomp(), lstMetodosPago, 
								f55ca01.getId().getCaid(), f55ca01.getId().getCaco(), valoresJDEInsFinanciamiento[0]);
 
					if (insertado) {
						//leer facturas seleccionadas
						for (int h = 0;h  < lstFacturasSelected.size();h++){
							if(((Credhdr)lstFacturasSelected.get(h)).getMontoAplicar().doubleValue() > 0){
								iNumFac[h] = ((Credhdr)lstFacturasSelected.get(h)).getNofactura();
								dMonto[h] = ((Credhdr)lstFacturasSelected.get(h)).getMontoAplicar().doubleValue();
								sPartida[h] = ((Credhdr)lstFacturasSelected.get(h)).getPartida();
								sCodunineg1[h] = ((Credhdr)lstFacturasSelected.get(h)).getId().getCodunineg();
								sCodsuc[h] = ((Credhdr)lstFacturasSelected.get(h)).getId().getCodsuc().substring(3, 5);
								sTipoDoc[h] =  ((Credhdr)lstFacturasSelected.get(h)).getTipofactura();
							}else{
								iNumFac[h] = 0;
							}
						}
						
						insertado = true;
						
						if (insertado) {
 
							if (insertado) {
 
								if (lstSolicitud != null && !lstSolicitud.isEmpty()) {
									for (int i = 0; i < lstSolicitud.size(); i++) {
										sol = (Solicitud) lstSolicitud.get(i);
										iNumsol = solCtrl.getNumeroSolicitud();
										if (iNumsol > 0) {
											insertado = solCtrl.registrarSolicitud(s, tx, iNumsol,iNumrec,valoresJDEInsFinanciamiento[0],f55ca01.getId().getCaid(),
															hFac.getId().getCodcomp(),f55ca01.getId().getCaco(),sol.getId().getReferencia(),
															sol.getAutoriza(),dFecha,sol.getObs(),sol.getMpago(),sol.getMonto(),sol.getMoneda());
											
															////LogCrtl.sendLogDebgsFinancing("<===========> registrarSolicitud:  "+insertado);
										} else {
											lblMensajeError.setValue("No se pudo obtener el No. de Solicitud!!!");
											insertado = false;
										}
									}
								}
							} else {
									lblMensajeError.setValue("No se pudo Actualizar el número de Recibo!!!");
									insertado = false;
							}
						} else {
							lblMensajeError.setValue("No se pudo Registrar el enlace entre recibo y factura!!!");
							insertado = false;
						}
					} else {
						lblMensajeError.setValue("No se pudo Registrar el detalle de Recibo!!!");
						insertado = false;
					}
				} else {
					lblMensajeError.setValue("No se pudo Registrar el encabezado de Recibo!!!");
					insertado = false;
				}
			}	
				//
			if (insertado) {
				// registrar Cambio
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
					if (m.get("bdTasa") != null) {
						bdTasa = (BigDecimal) m.get("bdTasa");
					}
					insertado = recCtrl.registrarCambio(s, tx, iNumrec,hFac.getId().getCodcomp(), sLblCambio1, d.formatStringToDouble(sCambio1),f55ca01.getId().getCaid(), f55ca01.getId().getCaco(), bdTasa,valoresJDEInsFinanciamiento[0]);
					////LogCrtl.sendLogDebgsFinancing("<===========> registrarCambio:  "+insertado);
					insertado = recCtrl.registrarCambio(s, tx, iNumrec,hFac.getId().getCodcomp(), sLblCambio2, d.formatStringToDouble(sCambio2),f55ca01.getId().getCaid(), f55ca01.getId().getCaco(), bdTasa,valoresJDEInsFinanciamiento[0]);
					////LogCrtl.sendLogDebgsFinancing("<===========> registrarCambio:  "+insertado);
				} else {
					sCambio1 = txtCambio.getValue().toString();
					////LogCrtl.sendLogDebgsFinancing("<===========> sCambio1:  "+sCambio1);
					sLblCambios1 = sLblCambio1.trim().split(" ");
					//
					sLblCambio1 = sLblCambios1[sLblCambios1.length - 1].substring(0, 3);
					//
					if (m.get("bdTasa") != null) {
						bdTasa = (BigDecimal) m.get("bdTasa");
					}
					insertado = recCtrl.registrarCambio(s, tx, iNumrec,hFac.getId().getCodcomp(), sLblCambio1, d.formatStringToDouble(sCambio1),f55ca01.getId().getCaid(),f55ca01.getId().getCaco(), bdTasa,valoresJDEInsFinanciamiento[0]);
					////LogCrtl.sendLogDebgsFinancing("<===========> registrarCambio:  "+insertado);
				}
			} 
			
			m.put("iNumRecFinan", iNumrec);
			
		}catch(Exception ex){
			insertado = false;
			ex.printStackTrace();
		}
		return insertado;
	}
/***************************************************************/	
	@SuppressWarnings("unchecked")
	public List sacarCambioMetodos(Session s, Transaction tx, Vf55ca01 f55ca01,Vautoriz vaut,List lstMetodosPago,Finanhdr f,String sCajero,int iNumrec, String sMonedaBase){
		Divisas d = new Divisas();
		String sCambio1 = "", sCambio2 = "";
		String[] sLblCambios1,sLblCambios2;
		double dCambio1 = 0.0, dCambio2 = 0.0, dNewMonto1 = 0.0, dCambio = 0.0, dMontoFicha = 0.0, dTotal = 0.0;
		MetodosPago[] metPago = null;
		boolean paso1 = false, bInsertado = false,bHayFicha = false;
		metPago = new MetodosPago[lstMetodosPago.size()];	
		String sLblCambio1 = "", sLblCambio2 = "";
		List lstPagoFicha = new ArrayList();
		BigDecimal bdTasa = BigDecimal.ZERO;
		try{
			bdTasa = obtenerTasaOficial();
			sLblCambio1 = lblCambio.getValue().toString();
			sLblCambio2 = lblCambioDomestico.getValue().toString();
			// obtener cambios
			if (txtCambio.getValue().toString().trim().equals("")){//hay cambio mixto todos los  pagos fueron en usd
				sCambio1 = txtCambioForaneo.getValue().toString();
				dCambio1 = d.formatStringToDouble(sCambio1);
				sCambio2 = txtCambioDomestico.getValue().toString();
				dCambio2 = d.formatStringToDouble(sCambio2);
				
				if (m.get("finan_MontoAplicar")== null){
					dTotal = d.formatStringToDouble(txtMontoAplicar.getValue().toString());
				}else {
					dTotal = d.formatStringToDouble(m.get("finan_MontoAplicar").toString());
				}
				
				dCambio = d.formatStringToDouble(txtMontoRecibido.getValue().toString()) - dTotal;
				
				sLblCambios1 = sLblCambio1.trim().split(" ");
				sLblCambios2 = sLblCambio2.trim().split(" ");
				//
				sLblCambio1 = sLblCambios1[sLblCambios1.length - 1].substring(0, 3);
				sLblCambio2 = sLblCambios2[sLblCambios1.length - 1].substring(0, 3);
				//
				
				for (int i = 0; i < lstMetodosPago.size(); i++) {
					metPago[i] = (MetodosPago) lstMetodosPago.get(i);
					
					if (metPago[i].getMetodo().equals(MetodosPagoCtrl.EFECTIVO)&& !metPago[i].getMoneda().equals(sMonedaBase)&& !paso1) {
						dNewMonto1 = metPago[i].getMonto() - dCambio;
						metPago[i].setMonto(dNewMonto1);
						metPago[i].setEquivalente(dNewMonto1);
						lstMetodosPago.remove(i);
						lstMetodosPago.add(i, metPago[i]);
						paso1 = true;
						dMontoFicha = d.roundDouble(dCambio2/bdTasa.doubleValue());
						
						if(dMontoFicha > 0){
							
							// Grabar ficha de CV
							MetodosPago metPagoFicha = new MetodosPago(MetodosPagoCtrl.EFECTIVO, metPago[i].getMoneda(), dMontoFicha, bdTasa, dCambio2, "", "","", "","0",0);
							lstPagoFicha.add(metPagoFicha);
							bInsertado = insertarFichaCV(s, tx,f55ca01, f, vaut,dMontoFicha, iNumrec, sCajero,lstPagoFicha);
							
							if(!bInsertado){
								lblMensajeError.setValue("No se pudo registrar la ficha de compra para este recibo ");
								return null;
							}
							
							
							bHayFicha = true;
							CodeUtil.putInSessionMap("bHayFicha", bHayFicha);
							CodeUtil.putInSessionMap("cr_MontoFichaCambio", dMontoFicha);
						}	
					}
					
				}
			} else {// hay cambio en una moneda unicamente
				sCambio1 = txtCambio.getValue().toString();
				dCambio1 = d.formatStringToDouble(sCambio1);
				sLblCambios1 = sLblCambio1.trim().split(" ");
				 
				sLblCambio1 = sLblCambios1[sLblCambios1.length - 1].substring(0, 3);
				 
				for (int i = 0; i < lstMetodosPago.size(); i++) {
					metPago[i] = (MetodosPago) lstMetodosPago.get(i);
					if (metPago[i].getMetodo().equals(MetodosPagoCtrl.EFECTIVO) && metPago[i].getMoneda().equals(sLblCambio1) && !paso1) {
						if (sLblCambio1.equals(sMonedaBase)&& f.getId().getMoneda().equals(sMonedaBase)) {// se saca el cambio en cor del pago en cor
							dNewMonto1 = metPago[i].getMonto() - dCambio1;
							metPago[i].setMonto(dNewMonto1);
							metPago[i].setEquivalente(dNewMonto1);
							paso1 = true;
						} else if (sLblCambio1.equals(sMonedaBase) && !f.getMoneda().equals(sMonedaBase) && lstMetodosPago.size() > 1) {// se saca el cambio del pago en cor y se convierte a usd con tasa del metodo
							dNewMonto1 = metPago[i].getMonto() - dCambio1;
							metPago[i].setMonto(dNewMonto1);
							metPago[i].setEquivalente(d.roundDouble(dNewMonto1 / metPago[i].getTasa().doubleValue()));
							paso1 = true;
						} else if (sLblCambio1.equals(sMonedaBase) && !f.getId().getMoneda().equals(sMonedaBase) && lstMetodosPago.size() == 1) {// si pago toda la fac de usd en cor
							dNewMonto1 = metPago[i].getMonto() - dCambio1;
							metPago[i].setMonto(dNewMonto1);
							
							double aplicado = d.formatStringToDouble(txtMontoAplicar.getValue().toString());
							
							//&& =============== el equivalente debe ser el monto aplicado.
							metPago[i].setEquivalente(aplicado);	//	metPago[i].setEquivalente(d.roundDouble(dNewMonto1/ metPago[i].getTasa().doubleValue()));
							
							paso1 = true;
							
						} else if (sLblCambio1.equals(sMonedaBase) && f.getId().getMoneda().equals(sMonedaBase) && lstMetodosPago.size() == 1) {// el pago en usd cubre toda la venta
							dNewMonto1 = metPago[i].getEquivalente() - dCambio1;
							metPago[i].setMonto(d.roundDouble(dNewMonto1 / metPago[i].getTasa().doubleValue()));
							metPago[i].setEquivalente(dNewMonto1);
							paso1 = true;
						}
						lstMetodosPago.remove(i);
						lstMetodosPago.add(i, metPago[i]);
						
					} else if (metPago[i].getMetodo().equals(MetodosPagoCtrl.EFECTIVO) && sLblCambio1.equals(sMonedaBase) && !metPago[i].getMoneda().equals(sMonedaBase) && !paso1) {
						if (f.getId().getMoneda().equals(sMonedaBase) && lstMetodosPago.size() == 1) {// el pago en usd cubre toda la venta
							
						}
					}
				}
				bHayFicha = false;
				m.put("bHayFicha", bHayFicha);
			}
			m.put("lstPagoFichaFinan",lstPagoFicha);
		}catch(Exception ex){
			ex.printStackTrace(); 
		}
		return lstMetodosPago;
	}

/*******************************************************************/
	public void cancelarProcesa(ActionEvent ev){
		dwProcesaRecibo.setWindowState("hidden");
	}
	public void cancelarRecibo(ActionEvent ev) {
		dwCancelar.setWindowState("normal");
	}
	public void cancelarCancelarRecibo(ActionEvent ev) {
		dwCancelar.setWindowState("hidden");
	}

	public void cancelaRecibo(ActionEvent ev) {
			dwCancelar.setWindowState("hidden");
		dwRecibo.setWindowState("hidden");
 
		//++++++++++++++++++++++++++++++++++++++++
		//Agregado por LFonseca
		//----------------------------------------
		Vf55ca01 caja = (Vf55ca01) ((List) m.get("lstCajas")).get(0);
		int caid = caja.getId().getCaid();	
		int iCajero = caja.getId().getCacati();
		
		List lstFacturasSelected = (List) m.get("lstSelectedCuotas");
		
		VerificarFacturaProceso verificarFac = new VerificarFacturaProceso();
		for(Object o : lstFacturasSelected){
			Finandet fd2 = (Finandet)o;
			 
			String[] strResultado =  verificarFac.actualizarVerificarFactura(String.valueOf(caid), 
													String.valueOf(fd2.getId().getCodcli()), 
													String.valueOf(fd2.getId().getNosol()), 
													fd2.getId().getTiposol(), 
													"", 
													String.valueOf(iCajero));
			 
		}
		
		//---------------------------------------------
		//+++++++++++++++++++++++++++++++++++++++++++++
	}
/*****************************************************************/	
	public void restablecerEstilos() {
		txtNumrecm.setStyleClass("frmInput2");
		gvMetodosPago.setStyleClass("igGrid");
		dcFecham.setStyleClass("");
		txtConcepto.setStyleClass("frmInput2");
	}
/*****************************************************************/
	public boolean validarDatosRecibo() {
		Divisas divisas = new Divisas();
		boolean validado = true;
		ReciboCtrl rpCtrl = new ReciboCtrl();
		int iNumRecm = 0;
		String sMensajeError = "";
		int y = 145;
		double dMontoRecibido = 0.0, dMontoAplicar = 0.0;
		Vf55ca01 vf55ca01 = null;
		Finandet hfac = null;
		try {
			vf55ca01 = (Vf55ca01) ((List) m.get("lstCajas")).get(0);
			hfac = (Finandet) ((List) m.get("lstSelectedCuotas")).get(0);
			restablecerEstilos();
			List selectedMet = (List) m.get("lstPagosFinan");
			// expresion regular de valores alfanumericos
			Matcher matAlfa = null;
			if (txtConcepto.getValue() != null) {
				String sObs = txtConcepto.getValue().toString();
				Pattern pAlfa = Pattern.compile("^[A-Za-z0-9-.;,\\p{Blank}]+$");
				matAlfa = pAlfa.matcher(sObs);
			}

			/** ****************RECIBO AUTOMATICO*********************** */
			if (ddlTipoRecibo.getValue().toString().equals("01")) {
				//txtNumrec.setStyle("display:none");
				// validar metodos de pago
				if (selectedMet == null || selectedMet.isEmpty()) {
					sMensajeError = sMensajeError
							+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> No se han agregado pagos al recibo<br>";
					gvMetodosPago.setStyleClass("igGridError");
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
				//dMontoAplicar = divisas.formatStringToDouble(txtMontoAplicar.getValue().toString());
				if (m.get("finan_MontoAplicar")== null){
					dMontoAplicar = divisas.formatStringToDouble(txtMontoAplicar.getValue().toString());
				}else {
					dMontoAplicar = divisas.formatStringToDouble(m.get("finan_MontoAplicar").toString());
				}	
				if (dMontoAplicar > dMontoRecibido) {
					sMensajeError = sMensajeError
							+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El monto recibido debe ser mayor al monto a aplicar <br>";
					validado = false;
					y = y + 14;
				}
				double cambio = divisas.roundDouble(dMontoRecibido - dMontoAplicar);
				if (!txtCambioForaneo.getValue().toString().trim().equals("")) {
					double dCambioForaneo = divisas
							.formatStringToDouble(txtCambioForaneo.getValue()
									.toString());
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

				if (dcFecham.getValue() != null) {
					dFecha = (Date) dcFecham.getValue();
					sFecha = sdf.format(dFecha);
					Pattern pFecha = Pattern
							.compile("^[0-3]?[0-9](/|-)[0-2]?[0-9](/|-)[1-2][0-9][0-9][0-9]$");
					matFecha = pFecha.matcher(sFecha);
				}
				// expresion regular solo numeros
				Matcher matNumero = null;
				if (txtNumrec.getValue() != null) {
					Pattern pNumero = Pattern.compile("^[0-9]*$");
					matNumero = pNumero.matcher(txtNumrec.getValue().toString().trim());
					if (matNumero.matches() && !txtNumrec.getValue().toString().trim().equals(
									"")) {
						iNumRecm = Integer.parseInt(txtNumrec.getValue().toString().trim() );
					}
				}
				// validar metodos de pago
				if (selectedMet == null || selectedMet.isEmpty()) {
					sMensajeError = sMensajeError
							+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> No se han agregado pagos al recibo<br>";
					gvMetodosPago.setStyleClass("igGridError");
					validado = false;
					y = y + 14;
				}
				// valida la fecha del recibo
				if (dcFecham.getValue() == null || (dcFecham.getValue().toString().trim()).equals("")) {
					sMensajeError = sMensajeError
							+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Fecha de recibo requerida<br>";
					dcFecham.setStyleClass("frmInput2Error2");
					validado = false;
					y = y + 14;
				}
				if (matFecha == null || !matFecha.matches()) {
					sMensajeError = sMensajeError
							+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Fecha de recibo no es valida<br>";
					dcFecham.setStyleClass("frmInput2Error");
					validado = false;
					y = y + 14;
				}
				// validar el numero de recibo
				if (txtNumrec.getValue() == null
						|| (txtNumrec.getValue().toString().trim()).equals("")) {
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Número de recibo manual es requerido<br>";
					txtNumrec.setStyleClass("frmInput2Error");
					validado = false;
					y = y + 14;
				}
				if (matNumero == null || !matNumero.matches()) {
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Número de recibo no es valido<br>";
					txtNumrec.setStyleClass("frmInput2Error");
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
					if (rpCtrl.verificarNumeroRecibo(
							vf55ca01.getId().getCaid(), hfac.getId().getCodcomp(),
							iNumRecm, vf55ca01.getId().getCaco().trim())) {
						sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El número de recibo manual ya existe!<br>";
						txtNumrec.setStyleClass("frmInput2Error");
						validado = false;
						y = y + 14;
					}
				}
				dMontoRecibido = divisas.formatStringToDouble(txtMontoRecibido.getValue().toString());
				//dMontoAplicar = divisas.formatStringToDouble(txtMontoAplicar.getValue().toString());
				if (m.get("finan_MontoAplicar")== null){
					dMontoAplicar = divisas.formatStringToDouble(txtMontoAplicar.getValue().toString());
				}else {
					dMontoAplicar = divisas.formatStringToDouble(m.get("finan_MontoAplicar").toString());
				}	
				if (dMontoAplicar > dMontoRecibido) {
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El monto recibido debe ser mayor al monto a aplicar <br>";
					// txtConcepto.setStyleClass("frmInput2Error");
					validado = false;
					y = y + 20;
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
			}
			
			// Validar que si es una cuota adelantada que solo pueda estar en aplicar a principal
			Boolean esCuoataAdelantada = CodeUtil.getFromSessionMap("esCuotaAdelantada") != null 
										? (Boolean)CodeUtil.getFromSessionMap("esCuotaAdelantada") : false;
										
			if (esCuoataAdelantada && !chkPrincipal.isChecked()) {
				sMensajeError = "Esta aplicando a una cuota adelantada y no se tiene la opcion Abono a Principal Seleccionada.";
				validado = false;
			}
						
			lblMensajeError.setValue(sMensajeError);
			dwMensajeError.setStyle("width:390px;height:" + y + "px");
			
		} catch (Exception ex) {
			validado = false; 
			lblMensajeError.setValue("Error al validar datos del recibo ");
			ex.printStackTrace();
		}
		return validado;
	}
/************************************************************************/
	public void procesarRecibo(ActionEvent ev) {
		boolean valido = false;
		boolean cambiado = true;

		//&& ===== Validar si la caja no esta bloqueada.
		String sMensaje = CtrlCajas.generarMensajeBlk();
		if(sMensaje.compareTo("") != 0){
			lblMensajeError.setValue(sMensaje);
			dwMensajeError.setStyle("width:400px; min-height:200px;");
			dwMensajeError.setWindowState("normal");
			return;
		}
		
		List<Finandet> lstFacturasSelected = (List<Finandet>) m.get("lstSelectedCuotas");
		List<Credhdr>	lstComponentes   = (List<Credhdr>) m.get("lstComponentes");
		
		//&& =============== calcular si se esta pagando toda la cuota o solo abono (pago a principal)
		if( chkPrincipal.isChecked() ) {
			
			BigDecimal pendienteActual;
			boolean pagoparcial ;
			
			for (final Credhdr cuotaPagar : lstComponentes) {
				pendienteActual = cuotaPagar.getMontoPendiente().subtract( cuotaPagar.getMontoAplicar() ).setScale(2, RoundingMode.HALF_UP) ;
				pagoparcial = (pendienteActual.abs().compareTo(BigDecimal.ZERO) >= 0 && pendienteActual.abs().compareTo(BigDecimal.ONE) <= 0);
				
				cuotaPagar.setNuevoMontoPendiente(pendienteActual);
				cuotaPagar.setPagoparcial(pagoparcial);
				
			}
		}
		
		//&& =========== Dejar que siga normalmente sin bloqueo.
		if (!txtCambioForaneo.getValue().toString().trim().equals("")) {
			cambiado = validarCambio();
		}
		valido = validarDatosRecibo();		
	
		if (valido && cambiado) {
			
			if(valido){
				restablecerEstilos();
				dwProcesaRecibo.setWindowState("normal");
			}else{
				dwMensajeError.setWindowState("normal");
			}
		} else{
			dwMensajeError.setWindowState("normal");
		}
	}
	
	public boolean crearHistoricoCuotaFinancimiento( Session session, List<Finandet> lstFacturasSelected, int caid, 
			int numrec, Date fecha, Date hora, int coduser, String loginuser, String codunineg, boolean pagoprincipal  ){
		
		String strSqlInsertHistorico = "" ;
		
		boolean done = true;
		
		
		try {
			
			String pagoPrincipal = (pagoprincipal)? "1":"0" ;
			
			
			Finandet fd1 = lstFacturasSelected.get(0);
			
			List<Integer> numerosCuotaPagar = new ArrayList<Integer>();
			for (Finandet fd : lstFacturasSelected) {
				numerosCuotaPagar.add( (int) fd.getId().getNocuota() );
			}
			
			String strSqlSelectDtaCuota = 
				" SELECT  atdoco, atan8, atcgrp, atkcoo, '@CODUNINEG', " + 
				"@CAID, @NUMREC, atdfr, ATAG, ATAAP, ATACR, ATFAP, ATPRID, " +
				"ATPRIF, ATINTD, ATINTF, '@FECHARECIBO', '@HORARECIBO', current_timestamp, " +
				"@CODUSER, '@LOGINUSER', @PAGOPRINCIPAL, 1 " +
				"FROM  @BDFINAN.F5503AM f " +
				"WHERE atan8 = @CODCLI and atdoco = @NOSOL and atcgrp = '@CODCOMP' and atdfr in (@NUMEROSCUOTAS)";
			
			strSqlSelectDtaCuota = strSqlSelectDtaCuota
				.replace("@BDFINAN", PropertiesSystem.GCPCXC )	
				.replace("@CODUNINEG", codunineg.trim() )
				.replace("@CAID", Integer.toString(caid) ) 
				.replace("@NUMREC", Integer.toString(numrec) ) 
				.replace("@FECHARECIBO", new SimpleDateFormat("yyyy-MM-dd").format(fecha) ) 
				.replace("@HORARECIBO", new SimpleDateFormat("HH:mm:ss").format(hora) ) 
				.replace("@CODUSER", Integer.toString(coduser) ) 
				.replace("@LOGINUSER", loginuser ) 
				.replace("@GCPPRDDTA",  PropertiesSystem.JDEDTA) 
				.replace("@CODCLI", Integer.toString(fd1.getId().getCodcli()) ) 
				.replace("@NOSOL",  Integer.toString(fd1.getId().getNosol())) 
				.replace("@CODCOMP", fd1.getId().getCodcomp().trim() ) 
				.replace("@PAGOPRINCIPAL", pagoPrincipal ) 
				.replace("@NUMEROSCUOTAS", numerosCuotaPagar.toString().replace("[", "").replace("]", "") )  ;
			
			strSqlInsertHistorico = 
			" insert into " + PropertiesSystem.ESQUEMA + ".HISTORICO_CUOTAS_FINAN ("  +
				" NUMEROSOLICITUD, CODIGOCLIENTE, CODCOMP, CODSUC, CODUNINEG, "+  
				" CAID, NUMREC, NUMEROCUOTA, ATAG, ATAAP, ATACR, ATFAP, ATPRID,"+  
				" ATPRIF, ATINTD, ATINTF, FECHARECIBO, HORARECIBO, FECHAGRABACION,"+ 
				" CODIGOUSUARIO, LOGINUSUARIO, PAGOPRINCIPAL, ESTADO )  (" + strSqlSelectDtaCuota + ")" ;
			 
			LogCajaService.CreateLog("crearHistoricoCuotaFinancimiento", "QRY", strSqlInsertHistorico);
			
			int rows = session.createSQLQuery(strSqlInsertHistorico).executeUpdate() ;
			
			return (rows > 0) ;
			
		} catch (Exception e) {
			LogCajaService.CreateLog("crearHistoricoCuotaFinancimiento", "ERR", e.getMessage());
			done = false;
			e.printStackTrace(); 
		}
		
		return done;
	}
	
	public boolean ajustarCuotaPagoPrincipal(List<Finandet> lstFacturasSelected, List<Credhdr> lstComponentes){
		boolean aplicado = true;
		
		try {
			
			boolean cancelaTotalCuota = false;
			List<String> strSqlUpdateF5503am = new ArrayList<String>();
			Finandet fd1 = lstFacturasSelected.get(0);
			
			//Cambio hecho por lfonseca
			//Fecha: 2020-12-10
			//Se desea que por ningun motivo se modifique 
			//El ATAG, ATACR
			String strSqlUpdate = " update "+PropertiesSystem.GCPCXC+".F5503AM F set  " +
//					"ATAG  = @MONTODOM, ATAAP = @PNDDOM, " +
//					"ATACR = @MONTOFOR, ATFAP = @PNDFOR, " +
					"ATAAP = @PNDDOM, " +
					"ATFAP = @PNDFOR, " +
					"ATINTD = @INTDOM, ATINTF = @INTFOR " ;
			
			String strSqlQueryWhere = 
				"where TRIM(f.atcgrp) = '@CODCOMP' and trim(f.atkcoo) = '@CODSUC' " +
				"and f.atan8 = @CODCLI  and trim(f.atdoco) = @NOSOL and trim(f.atdcto) = '@TIPOSOL' "  ;
			
			strSqlQueryWhere = strSqlQueryWhere
					.replace("@CODCOMP",  fd1.getId().getCodcomp().trim() ) 
					.replace("@CODSUC",  fd1.getId().getCodsuc().trim() ) 
					.replace("@CODCLI",  String.valueOf(fd1.getId().getCodcli()) ) 
					.replace("@NOSOL",   String.valueOf( fd1.getId().getNosol() ) ) 
					.replace("@TIPOSOL", fd1.getId().getTiposol().trim() )  ;
			
			//&& ============== Validar cada cuota seleccionada, si el pago cubre todo su monto y actualizar tabla de pagos (F5503am)
			BigDecimal pendienteActual ; 
			
			for (final Credhdr cuotaPagar : lstComponentes) {
				
				pendienteActual = cuotaPagar.getNuevoMontoPendiente();
				cancelaTotalCuota = cuotaPagar.isPagoparcial() ;
				
				if(cancelaTotalCuota){
					 
				
					strSqlUpdateF5503am.add( 
						strSqlUpdate
							.replace("@MONTODOM", String.format("%1$.2f", cuotaPagar.getMontoPendiente().multiply(cuotaPagar.getId().getTasa()) ).replace(".", "") )
							.replace("@PNDDOM", "0")
							.replace("@MONTOFOR", String.format("%1$.2f", cuotaPagar.getMontoPendiente() ).replace(".", "") )
							.replace("@PNDFOR", "0")
							.replace("@INTDOM", "ATINTD" )
							.replace("@INTFOR", "ATINTF" ) +
	 
						strSqlQueryWhere +  " and f.atdfr = " + Integer.parseInt( cuotaPagar.getId().getPartida() )
					);
				}else{
					
					Finandet f = (Finandet)
					CollectionUtils.find(lstFacturasSelected, new Predicate(){

						public boolean evaluate(Object o) {
							Finandet f = (Finandet)o;
							return Integer.parseInt( cuotaPagar.getId().getPartida() ) == f.getId().getNocuota()  ;
						}
					});
					
					BigDecimal tasaInteres = f.getId().getInteres().multiply( new BigDecimal("100")).divide(cuotaPagar.getMontoPendiente(),2,RoundingMode.HALF_UP ); 
					BigDecimal bdNuevoInteres = pendienteActual.multiply(tasaInteres.divide(new BigDecimal("100"))).setScale(2, RoundingMode.HALF_UP) ;
					
					strSqlUpdateF5503am.add( 
							
						strSqlUpdate
							.replace("@MONTODOM", String.format("%1$.2f", pendienteActual.add(bdNuevoInteres).multiply(cuotaPagar.getId().getTasa()) ).replace(".", "") )
							.replace("@PNDDOM", String.format("%1$.2f",   pendienteActual.add(bdNuevoInteres).multiply(cuotaPagar.getId().getTasa()) ).replace(".", "") )
						
							.replace("@MONTOFOR", String.format("%1$.2f", pendienteActual.add(bdNuevoInteres) ).replace(".", "") )
							.replace("@PNDFOR", String.format("%1$.2f",   pendienteActual.add(bdNuevoInteres) ).replace(".", "") )
							
							.replace("@INTDOM", String.format("%1$.2f", bdNuevoInteres.multiply(cuotaPagar.getId().getTasa()) ).replace(".", "") )
							.replace("@INTFOR", String.format("%1$.2f", bdNuevoInteres ).replace(".", "") ) +
		 
						strSqlQueryWhere +  " and f.atdfr = " + Integer.parseInt( cuotaPagar.getId().getPartida() )
					);
				} 
			}
			 
			aplicado = ConsolidadoDepositosBcoCtrl.executeSqlQueries(strSqlUpdateF5503am);
			
		} catch (Exception e) {
			aplicado = false;
			LogCajaService.CreateLog("ajustarCuotaPagoPrincipal", "ERR", e.getMessage());
		}
		return aplicado ;
	}
	
	public boolean ajustarCuotaPagoPrincipalAdelantado(List<Finandet> lstFacturasSelected, List<Credhdr> lstComponentes){
		boolean aplicado = true;
		
		try {
			Finandet fd1 = lstFacturasSelected.get(0);
			
			String strSqlUpdate = " update "+PropertiesSystem.GCPCXC+".F5503AM F set  " +
					"ATAAP = 0, " +
					"ATFAP = 0 ";
			
			String strSqlQueryWhere = 
				"WHERE TRIM(f.atcgrp) = '@CODCOMP' AND TRIM(f.atkcoo) = '@CODSUC' " +
				"AND f.atan8 = @CODCLI AND TRIM(f.atdoco) = @NOSOL AND TRIM(f.atdcto) = '@TIPOSOL' AND f.ATDFR  = @NOCUOTA "  ;
			
			strSqlQueryWhere = strSqlQueryWhere
					.replace("@CODCOMP",  fd1.getId().getCodcomp().trim() ) 
					.replace("@CODSUC",  fd1.getId().getCodsuc().trim() ) 
					.replace("@CODCLI",  String.valueOf(fd1.getId().getCodcli()) ) 
					.replace("@NOSOL",   String.valueOf( fd1.getId().getNosol() ) ) 
					.replace("@TIPOSOL", fd1.getId().getTiposol().trim() );
					//.replace("@NOCUOTA", String.valueOf(fd1.getId().getNocuota()));
			
			//Filtrar los documentos de tipo Principal y verificar si tiene saldo en el JDE
			ClsParametroCaja servicioCajaParm = new ClsParametroCaja();
			CajaParametro objCajaParm = servicioCajaParm.getParametros("04", "0", "FN_TIPODOC_PRINCIPAL");
			
			List<String> lstPrincipal = Arrays
					.stream(objCajaParm.getValorAlfanumerico().trim().split(","))
					.map(String::trim)
					.collect(Collectors.toList()); 
			
			Session sesion = HibernateUtilPruebaCn.currentSession();
			List<Credhdr> listPrincipal = lstComponentes.stream()
					.filter(x -> lstPrincipal.contains(x.getTipofactura().trim().toUpperCase()))
					.collect(Collectors.toList());
			
			if (listPrincipal != null && listPrincipal.size() > 0) {
				//&& ============== Validar cada cuota seleccionada, si el pago cubre todo su monto y actualizar tabla de pagos (F5503am)			
				for (Credhdr cuotaPagar : listPrincipal) {
					// Por cada factura de tipo H1 verificar si tiene saldo en el jdedwards
					String strSQL = "SELECT " + 
							"    SUM(B11.RPFAP) AS SALDODOL " + 
							"FROM " + 
							"    " + PropertiesSystem.JDEDTA + ".F03B11 B11 " + 
							"WHERE " + 
							"    CAST(B11.RPKCO AS VARCHAR(5)) = '" + fd1.getId().getCodsuc() + "' " + 
							"    AND B11.RPAN8 = " + String.valueOf(fd1.getId().getCodcli()) + " " + 
							"    AND CAST(B11.RPSFX AS INT) = " + Integer.parseInt(cuotaPagar.getPartida()) +" " + 
							"    AND CAST(B11.RPPO AS INT) = " + String.valueOf( fd1.getId().getNosol() ) + " " + 
							"    AND B11.RPDOC = " + String.valueOf(cuotaPagar.getNofactura()) + " " + 
							"    AND B11.RPDCT = '" + String.valueOf(cuotaPagar.getTipofactura().trim().toUpperCase()) + "' " + 
							"    AND TRIM(B11.RPPO) <> '' ";
					
					LogCajaService.CreateLog("ajustarCuotaPagoPrincipalAdelantado", "QRY", strSQL);
					
					BigDecimal resultado = (BigDecimal)sesion.createSQLQuery(strSQL).uniqueResult();
					if (resultado.intValue() <= 0) {
						String strF5503AMUpdateQry = (strSqlUpdate + strSqlQueryWhere)
								.replace("@NOCUOTA", String.valueOf(Integer.parseInt(cuotaPagar.getPartida())));
						
						LogCajaService.CreateLog("ajustarCuotaPagoPrincipalAdelantado", "QRY", strF5503AMUpdateQry);
						sesion.createSQLQuery(strF5503AMUpdateQry).executeUpdate();		
					}
				}
			}
		} catch (Exception e) {
			aplicado = false;
			LogCajaService.CreateLog("ajustarCuotaPagoPrincipalAdelantado", "ERR", e.getMessage());
		}
		
		return aplicado ;
	}
	
	
/*********************************************************/	
	public void aplicarCambio(ActionEvent ev) {
		procesarCambio();
	}
/***********************************************************/	
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
				txtCambioForaneo.setStyleClass("frmInputVisibleError");
				validado = false;
			} else if (!matNumero.matches()) {
				txtCambioForaneo.setStyleClass("frmInputVisibleError");
				validado = false;
			} else if (divisas.formatStringToDouble(sCambio) < 0) {
				txtCambioForaneo.setStyleClass("frmInputVisibleError");
				validado = false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return validado;
	}
/***************************************************************************/	
	public void procesarCambio() {
		SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
		boolean valido = false;
		double dCambioDom = 0.0,dMontoAplicar = 0.0;
		BigDecimal tasa = BigDecimal.ZERO;
		Divisas divisas = new Divisas();
		Tcambio[] tcJDE = null;
		try {
			valido = validarCambio();
			if (valido) {
//				txtCambioForaneo.setStyleClass("frmInput2");
				//double dMontoAplicar = divisas.formatStringToDouble(txtMontoAplicar.getValue().toString());
				if (m.get("finan_MontoAplicar")== null){
					dMontoAplicar = divisas.formatStringToDouble(txtMontoAplicar.getValue().toString());
				}else {
					dMontoAplicar = divisas.formatStringToDouble(m.get("finan_MontoAplicar").toString());
				}	
				double montoRecibido = divisas.formatStringToDouble(txtMontoRecibido.getValue().toString());
				double cambio = montoRecibido - dMontoAplicar;
				double dCambioForaneo = divisas.formatStringToDouble(txtCambioForaneo.getValue().toString());
				if (dCambioForaneo > cambio) {
					txtCambioForaneo.setValue(divisas.formatDouble(divisas.roundDouble(cambio)));
					txtCambioDomestico.setValue("0.00");
				} else {
					tcJDE = (Tcambio[]) m.get("tcambio");
					// buscar tasa de cambio JDE para moneda
					for (int l = 0; l < tcJDE.length; l++) {
						if (tcJDE[l].getId().getCxcrcd().equals("COR")) {
							tasa = tcJDE[l].getId().getCxcrrd();
							break;
						}
					}
					m.put("bdTasa", tasa);
					dCambioDom = (cambio - dCambioForaneo) * tasa.doubleValue();
					txtCambioDomestico.setValue(divisas.formatDouble(dCambioDom));
				}
			}
//			srm.addSmartRefreshId(txtCambioForaneo.getClientId(FacesContext.getCurrentInstance()));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
/*****************************************************************************************************/
	public void determinarCambio(List lstPagos, Credhdr hFac,double montoRecibido, String sMonedaBase) {
		boolean bCumple = true, bCambioCOR = false;
		MetodosPago mpago = null;
		Divisas divisas = new Divisas();
		SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
		double cambio = 0.0, montoCOR = 0, montoUSD = 0, dmontoAplicar = 0.0;
		BigDecimal tasa = BigDecimal.ZERO;
		Tcambio[] tcJDE = null;
		try {
			if (m.get("finan_MontoAplicar")== null){
				dmontoAplicar = divisas.formatStringToDouble(txtMontoAplicar.getValue().toString());
			}else {
				dmontoAplicar = divisas.formatStringToDouble(m.get("finan_MontoAplicar").toString());
			}	
			// verificar si se cumplen condiciones para cambio mixto
			for (int i = 0; i < lstPagos.size(); i++) {
				mpago = (MetodosPago) lstPagos.get(i);
				if (mpago.getMoneda().equals(sMonedaBase)  && mpago.getMetodo().equals(MetodosPagoCtrl.EFECTIVO)) {
					bCumple = false;
				}
				if (!hFac.getId().getMoneda().equals(sMonedaBase) && mpago.getMoneda().equals(sMonedaBase) && mpago.getMetodo().equals(MetodosPagoCtrl.EFECTIVO)) {
					montoCOR = montoCOR + mpago.getMonto();
					bCambioCOR = true;
				}
				if (!hFac.getId().getMoneda().equals(sMonedaBase) && !mpago.getMoneda().equals(sMonedaBase) && mpago.getMetodo().equals(MetodosPagoCtrl.EFECTIVO)) {
					montoUSD = montoUSD + mpago.getMonto();
				}
			}
			//
			double dCambio = divisas.formatStringToDouble(txtCambio.getValue().toString());
			if (bCumple && dCambio > 0) {// realiza el calculo del cambio
				
				txtCambioForaneo.setValue(txtCambio.getValue().toString());
				txtCambioForaneo.setStyleClass("frmInputVisible");

				lblCambio.setValue("Cambio USD:");
				txtCambio.setValue("");
				lblCambioDomestico.setValue("Cambio " + sMonedaBase + ":");
				txtCambioDomestico.setValue("0.00");
				lnkCambio.setStyle("visibility: visible; width: 16px;");
				lnkCambio.setIconUrl("/theme/icons/RefreshWhite.gif");
				lnkCambio.setHoverIconUrl("/theme/icons/RefreshWhiteOver.gif");
				lblPendienteDomestico.setValue("");
				txtPendienteDomestico.setValue("");
				m.put("bdTasa", BigDecimal.ZERO);
			} else if (dCambio < 0) {
				cambio = montoRecibido - dmontoAplicar;
				lblCambio.setValue("Cambio " + hFac.getId().getMoneda() + ":");
				txtCambio.setValue(divisas.formatDouble(cambio));
	
				txtCambioForaneo.setStyleClass("frmInputHidden");
				txtCambioForaneo.setValue("0");
				
				lblCambioDomestico.setValue("");
				txtCambioDomestico.setValue("");
				lnkCambio.setStyle("visibility: hidden;width: 0px");
				lnkCambio.setIconUrl("");
				lnkCambio.setHoverIconUrl("");
				// obtener tasa del cambio
			
				tasa = obtenerTasaParalela(sMonedaBase /*"COR"*/);
					
				if (!hFac.getId().getMoneda().equals(sMonedaBase)) {
					lblPendienteDomestico.setValue("Cambio " + sMonedaBase + ":");
					lblPendienteDomestico.setStyle("visibility: visible");
					txtPendienteDomestico.setValue(divisas.formatDouble(divisas.roundDouble(cambio * tasa.doubleValue())));
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
			} else if (!hFac.getId().getMoneda().equals(sMonedaBase) && bCambioCOR) {// toma el total a aplicar y lo convierte a la tasa paralela a eso se le resta lo recibido a la misma tasa y ese es el cambio
				// obtener tasa del cambio
				tasa = obtenerTasaParalela(sMonedaBase /*"COR"*/);
				
				m.put("bdTasa", tasa);
				// monto en cordobas recibido		
				cambio = divisas.roundDouble( ( divisas.formatStringToDouble(txtMontoRecibido.getValue().toString()) - dmontoAplicar) *tasa.doubleValue());
				
				if (cambio < 0) {
					txtCambio.setStyle("color: red;font-size: 10pt");
				} else if (cambio > 0) {
					txtCambio.setStyle("color: green;font-size: 10pt");
				} else {
					txtCambio.setStyle("font-size: 10pt");
				}
				lblCambio.setValue("Cambio " + sMonedaBase + ":");
				txtCambio.setValue(divisas.formatDouble(cambio));
				
				txtCambioForaneo.setStyleClass("frmInputHidden");
				txtCambioForaneo.setValue("0");

				lnkCambio.setStyle("visibility: hidden; width: 0px");
				lnkCambio.setIconUrl("");
				lnkCambio.setHoverIconUrl("");
				lblCambioDomestico.setValue("");
				txtCambioDomestico.setValue("");
				lblPendienteDomestico.setValue("");
				txtPendienteDomestico.setValue("");
				
			} else if (!hFac.getId().getMoneda().equals(sMonedaBase)) {
				
//				tcJDE = (Tcambio[]) m.get("tcambio");
//				for (int l = 0; l < tcJDE.length; l++) {
//					if (tcJDE[l].getId().getCxcrcd().equals(sMonedaBase /*"COR"*/)) {
//						tasa = tcJDE[l].getId().getCxcrrd();
//						break;
//					}
//				}
//				m.put("bdTasa", tasa);
				
				tasa =  tasaParalela(sMonedaBase);
				
				cambio = divisas.roundDouble((montoRecibido - dmontoAplicar) * tasa.doubleValue());
				if (cambio < 0) {
					txtCambio.setStyle("color: red;font-size: 10pt");
				} else if (cambio > 0) {
					txtCambio.setStyle("color: green;font-size: 10pt");
				} else {
					txtCambio.setStyle("font-size: 10pt");
				}
				lblCambio.setValue("Cambio " + sMonedaBase + ":");
				txtCambio.setValue(divisas.formatDouble(cambio));
				
				txtCambioForaneo.setStyleClass("frmInputHidden");
				txtCambioForaneo.setValue("0");
				
				lnkCambio.setStyle("visibility: hidden; width: 0px");
				lnkCambio.setIconUrl("");
				lnkCambio.setHoverIconUrl("");
				lblCambioDomestico.setValue("");
				txtCambioDomestico.setValue("");
				lblPendienteDomestico.setValue("");
				txtPendienteDomestico.setValue("");
			} else {
				m.put("bdTasa", BigDecimal.ZERO);
				cambio = (montoRecibido - dmontoAplicar);
				if (cambio < 0) {
					txtCambio.setStyle("color: red;font-size: 10pt");
				} else if (cambio > 0) {
					txtCambio.setStyle("color: green;font-size: 10pt");
				} else {
					txtCambio.setStyle("font-size: 10pt");
				}
				lblCambio.setValue("Cambio " + sMonedaBase + ":");
				txtCambio.setValue(divisas.formatDouble(cambio));
			
				txtCambioForaneo.setStyleClass("frmInputHidden");
				
//				txtCambioForaneo.setStyle("visibility: hidden;");
//				txtcmbforaneoStyle = "visibility: hidden;";
				
				lnkCambio.setStyle("visibility: hidden; width: 0px");
				lnkCambio.setIconUrl("");
				lnkCambio.setHoverIconUrl("");
				lblCambioDomestico.setValue("");
				txtCambioDomestico.setValue("");
				lblPendienteDomestico.setValue("");
				txtPendienteDomestico.setValue("");
			}
			
			if( dmontoAplicar == montoRecibido ){
				txtCambioDomestico.setValue("");
				txtCambioForaneo.setValue("");
				txtCambio.setValue("0.00");
			}
			
			srm.addSmartRefreshId(lblCambio.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lnkCambio.getClientId(FacesContext.getCurrentInstance()));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

public void mostrarAgregarCuotas(ActionEvent ev){
		
		Finandet fSelected = null, f = null;
		BigDecimal bdMontoPendiente = BigDecimal.ZERO, bdMontoAplicado = BigDecimal.ZERO;
		List lstNewCuotas = new ArrayList(), lstFacturasSelected, lstCuotasEnGrid;
		List<Finanhdr> lstCreditosFinan;
		List<Credhdr> lstComponentes;
		CuotaCtrl cuotaCtrl = new CuotaCtrl();
		Finanhdr fHdr = null;
		Credhdr c = null;
		Divisas d = new Divisas();
		Vautoriz vaut =  ((Vautoriz[])m.get("sevAut"))[0];
		
		try{
			
			lstCreditosFinan = (List<Finanhdr>)CodeUtil.getFromSessionMap("lstCreditosFinan");
			lstFacturasSelected = (List) CodeUtil.getFromSessionMap("lstSelectedCuotas");
			lstComponentes = (List<Credhdr>) CodeUtil.getFromSessionMap("lstComponentes");
			
			for (int i = 0; i < lstComponentes.size(); i++){
				c =  lstComponentes.get(i);
				bdMontoPendiente = bdMontoPendiente.add(c.getMontoPendiente());
				bdMontoAplicado = bdMontoAplicado.add(c.getMontoAplicar());
			}
			bdMontoPendiente = d.roundBigDecimal(bdMontoPendiente);
			bdMontoAplicado = d.roundBigDecimal(bdMontoAplicado);
			
			if(bdMontoPendiente.subtract(bdMontoAplicado).doubleValue() != 0){
				lblMensajeError.setValue("No puede agregar más cuotas al recibo; Primero debe cancelar el monto de las cuotas actuales");
				dwMensajeError.setStyle("width:320px;height:160px");
				dwMensajeError.setWindowState("normal");
				return;
			}
			 
			boolean esAbonoPrincipal = chkPrincipal.isChecked();
			
			for(int i = 0; i < lstCreditosFinan.size();i++){
				
				fHdr = lstCreditosFinan.get(i);
				
				if(esAbonoPrincipal){
					
					f = cuotaCtrl.buscarAnteriorCuota(fHdr.getId().getCodcomp(), fHdr.getId().getCodsuc(), 
						     fHdr.getId().getNosol(), fHdr.getId().getTiposol(), fHdr.getId().getCodcli(),lstFacturasSelected);
					
					
					if(f.getId().getAticu() != 0){
						lblMensajeError.setValue("La cuota siguiente # "+ f.getId().getNocuota() +" ya tiene intereses generados en batch #" +f.getId().getAticu() );
						dwMensajeError.setStyle("width:320px;height:160px");
						dwMensajeError.setWindowState("normal");
						return;
					}
					
					int numeroUltimaCuotaActual = Integer.parseInt( String.valueOf( CodeUtil.getFromSessionMap("fn_NumeroUltimaCuotaCorriente") ) );
					if(numeroUltimaCuotaActual ==  f.getId().getNocuota() ){
						lblMensajeError.setValue("No hay mas cuotas disponibles para abono a Saldo Principal");
						dwMensajeError.setStyle("width:320px;height:160px");
						dwMensajeError.setWindowState("normal");
						return;
					}
					
					BigDecimal montoPendienteSinInteres = f.getId().getMontopend().subtract( f.getId().getInteres() );
					 
					f.getId().setMontopend( montoPendienteSinInteres );
					
				} else{
					f = cuotaCtrl.buscarSiguienteCuota(fHdr.getId().getCodcomp(), fHdr.getId().getCodsuc(), 
							fHdr.getId().getNosol(), fHdr.getId().getTiposol(), fHdr.getId().getCodcli(), lstFacturasSelected);
					
					if (f!= null) {
						// Esta cuota tenemos que ver los intereces
						List<Finandet> tmpFinanDet = new ArrayList<Finandet>();
						tmpFinanDet.add(f);
						String msgCreaInteres = crearFacturasPorIntereses2( lstCreditosFinan,  tmpFinanDet, vaut.getId().getLogin() );
						
						if (!msgCreaInteres.isEmpty()) {
							lblMensajeError.setValue(msgCreaInteres);
							dwMensajeError.setStyle("width:320px;height:160px");
							dwMensajeError.setWindowState("normal");
							return;
						}
						
						// Verificar que la siguiente cuota tenga intereses
						BigDecimal bdInteres = cuotaCtrl.buscarInteresCorrientePend(f);
						if(bdInteres.compareTo(BigDecimal.ZERO ) == -1 || bdInteres.compareTo(BigDecimal.ZERO ) == 0 ){
							lblMensajeError.setValue("No puede agregar la siguiente cuota por que no esta vigente o no tiene intereses corrientes generados");
							dwMensajeError.setStyle("width:320px;height:160px");
							dwMensajeError.setWindowState("normal");
							return;
						}
					}
					
				}

				if( f == null ){
					lblMensajeError.setValue("Ya no hay más cuotas disponibles " );
					dwMensajeError.setStyle("width:320px;height:160px");
					dwMensajeError.setWindowState("normal");
					return;
				}
				
				f.setAbonoPrincipal(esAbonoPrincipal);
				
				lstNewCuotas.add(f);
			}
			
			CodeUtil.putInSessionMap( "lstAgregarCuota", lstNewCuotas );
			
			gvAgregarCuota.dataBind();
			dwAgregarCuota.setWindowState("normal");
		 
		
		}catch(Exception ex){
			dwAgregarCuota.setWindowState("hidden");
			LogCajaService.CreateLog("mostrarAgregarCuotas", "ERR", ex.getMessage());
		}		
	}
	
	public void mostrarAgregarCuotas_NO_EN_USO(ActionEvent ev){
		Finandet fSelected = null, f = null;
		BigDecimal bdMontoPendiente = BigDecimal.ZERO, bdMontoAplicado = BigDecimal.ZERO;
		List lstNewCuotas = new ArrayList(), lstCreditosFinan,lstFacturasSelected,lstCuotasEnGrid, lstComponentes;
		CuotaCtrl cuotaCtrl = new CuotaCtrl();
		Finanhdr fHdr = null;
		Credhdr c = null;
		Divisas d = new Divisas();
		try{
			lstCreditosFinan = (List)m.get("lstCreditosFinan");
			//validar que se puedan agregar cuotas
			lstFacturasSelected = (List) m.get("lstSelectedCuotas");
			lstComponentes = (List) m.get("lstComponentes");
			
			for (int i = 0; i < lstComponentes.size(); i++){
				c = (Credhdr)lstComponentes.get(i);
				bdMontoPendiente = bdMontoPendiente.add(c.getMontoPendiente());
				bdMontoAplicado = bdMontoAplicado.add(c.getMontoAplicar());
			}
			bdMontoPendiente = d.roundBigDecimal(bdMontoPendiente);
			bdMontoAplicado = d.roundBigDecimal(bdMontoAplicado);
			if(bdMontoPendiente.subtract(bdMontoAplicado).doubleValue()==0){
				dwAgregarCuota.setWindowState("normal");
				//poner unicamente cuotas q se pueden agregar
				for(int i = 0;i < lstCreditosFinan.size();i++){					
					fHdr = (Finanhdr)lstCreditosFinan.get(i);
					//lstCuotasEnGrid = getCuotasEnGrid(fHdr,lstFacturasSelected);
					if (!chkPrincipal.isChecked()){
						f = cuotaCtrl.buscarSiguienteCuota(fHdr.getId().getCodcomp(), fHdr.getId().getCodsuc(), fHdr.getId().getNosol(), fHdr.getId().getTiposol(), fHdr.getId().getCodcli(),lstFacturasSelected);
					}else{
						f = cuotaCtrl.buscarAnteriorCuota(fHdr.getId().getCodcomp(), fHdr.getId().getCodsuc(), fHdr.getId().getNosol(), fHdr.getId().getTiposol(), fHdr.getId().getCodcli(),lstFacturasSelected);
					}
					lstNewCuotas.add(f);
				}
				m.put("lstAgregarCuota", lstNewCuotas);
				gvAgregarCuota.dataBind();
			}else{
				lblMensajeError.setValue("No puede agregar más cuotas al recibo;Primero debe cancelar el monto de las cuotas actuales");
				dwMensajeError.setStyle("width:320px;height:160px");
				dwMensajeError.setWindowState("normal");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}		
	}
/*******************************************************************************/	
	public void cerrarAgregarCuota(ActionEvent e){
		dwAgregarCuota.setWindowState("hidden");
	}
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
	
	
	
	public void mostrarDetalleCuota(ActionEvent e){
	 
		
		try{
			
			RowItem ri = (RowItem) e.getComponent().getParent().getParent();
			
			Credhdr detalleCuota = (Credhdr)gvSelectedCuotas.getDataRow(ri);
		 
			fechaCuotaDet = detalleCuota.getId().getFecha() ; 
			noCuotaDet = detalleCuota.getId().getPartida() ;
			tasaCuotaDet = detalleCuota.getId().getTasa();
			monedaCuotaDet = detalleCuota.getId().getMoneda();
			principalCuotaDet = detalleCuota.getId().getDtotal();
			interesCuotaDet = detalleCuota.getId().getDtotal(); 
			impuestoCuotaDet = detalleCuota.getId().getImpuesto();
			
			totalCuotaDet = detalleCuota.getId().getMonto();
			pendienteCuotaDet = detalleCuota.getId().getDpendiente() ;
			
 
			dwDetalleCuota.setWindowState("normal");
			
			companiaCuotaDet = detalleCuota.getId().getCodcomp() +" "+ detalleCuota.getId().getNomcomp();
			sucursalCuotaDet = detalleCuota.getId().getCodsuc() +" "+ detalleCuota.getId().getNomsuc();
			
			
		}catch(Exception ex){
			ex.printStackTrace(); 
			
		}
	}
	
	/*****************************************************************************/
	public void mostrarDetalleCuota_NoSeUsa(ActionEvent e){
		Finandet f = null;
		try{
			RowItem ri = (RowItem) e.getComponent().getParent().getParent();
			
			Object b[] = (Object[])gvSelectedCuotas.getDataRow(ri);
			 
			f = (Finandet) gvSelectedCuotas.getDataRow(ri);
			fechaCuotaDet = f.getId().getFechapago();
			noCuotaDet = f.getId().getNocuota() + "";
			tasaCuotaDet = f.getId().getTasa();
			monedaCuotaDet = f.getId().getMoneda();
			principalCuotaDet = f.getId().getPrincipal();
			interesCuotaDet = f.getId().getInteres();
			impuestoCuotaDet = f.getId().getImpuesto();
			
			totalCuotaDet = f.getId().getMonto();
			pendienteCuotaDet = f.getMontopend();
			
			if(f.getId().getDiasmora() > 0){
				moraCuotaDet = f.getMora();
				diasVenCuotaDet = f.getId().getDiasmora() + "";
				diasAcumCuotaDet = f.getId().getDiasven() + ""; 
				totalDiasCuotaDet = (f.getId().getDiasmora() + f.getId().getDiasven())+"";
			}else{
				moraCuotaDet = BigDecimal.ZERO;
				diasVenCuotaDet = 0 + "";
				diasAcumCuotaDet = 0 + ""; 
				totalDiasCuotaDet = 0+"";
			}
			dwDetalleCuota.setWindowState("normal");
			
			companiaCuotaDet = f.getId().getCodcomp() +" "+ f.getId().getNomcomp();
			sucursalCuotaDet = f.getId().getCodsuc() +" "+ f.getId().getNomsuc();
			
			
			
		}catch(Exception ex){ 
			ex.printStackTrace();
		}
	}
	/*****************************************************************************/
	public void cerrarDetalleCuota(ActionEvent ev){
		dwDetalleCuota.setWindowState("hidden");
	}
	/*****************************************************************************/
	/************************PROCESAR SOLICITUD**********************************************************************/
	public void procesarSolicitud(ActionEvent e) {
		
		boolean validado = false;
		boolean valido = false;
		boolean flgpagos = true;
		boolean bTarjeta = false;
		
		double montoRecibido = 0;
		double total = 0;
		double montototal = 0;
		double montoInser = 0;
		double equiv = 0;
		
		int cont = 1;
		int cant = 1;
		
		BigDecimal tasa = BigDecimal.ONE;
		
		String sMonUnineg = new String("");
		String sTotal = new String("");
		String sMonto = new String("");
		String sEquiv = new String("");
		String metododesc = new String("");
		String metodoId = new String("");
		String moneda = new String("");
		String ref1 = new String("");
		String ref2 = new String("");
		String ref3 = new String("");
		String ref4 = new String("");
		String sVoucherManual = "0";
		
		
		Metpago[] metpago = null;
		Tpararela[] tpcambio = null;
		Divisas d = new Divisas();
		CtrlSolicitud solCtrl = new CtrlSolicitud();
		Vf55ca012[] lstMetPago = null;
		String sMonedaBase = "";
		
		Vf55ca01 caja = null;
		List<String> lstDatosTrack  = null;
		List lstDatosTrack2 = null;
		
		BigDecimal tasaofi  = BigDecimal.ONE;
		BigDecimal tasapara = BigDecimal.ONE;
		BigDecimal tasapago = BigDecimal.ONE;
		
		try{
			validado = validarSolicitud();		
			if(!validado)
				return;

			caja = ((List<Vf55ca01>) m.get("lstCajas")).get(0);
			sVoucherManual = (chkVoucherManual.isChecked())?"1":"0";
			
			boolean usaSocketPos  = caja.getId().getCasktpos() == 'Y';
			boolean ingresoManual = chkIngresoManual.isChecked();
			boolean voucherManual = chkVoucherManual.isChecked();
			
			metodoId = ddlMetodosPago.getValue().toString();		
			moneda = ddlMoneda.getValue().toString();		
			ref1 = txtReferencia1.getValue().toString().trim();
			ref2 = txtReferencia2.getValue().toString().trim();
			ref3 = txtReferencia3.getValue().toString().trim();
			ref4 = "";
			String ref5 = "";
			String sTrack = "";
			String sTerminal = "";
			
			tasaofi  = tasaOficial(moneda);
			tasapara = tasaParalela(moneda);
			
			lstPagos = (ArrayList<MetodosPago>)m.get("lstPagosFinan"); 
			lstMetPago = (Vf55ca012[]) m.get("metpago");	
			
			montoInser = Double.parseDouble(m.get("fin_monto").toString());
			montototal = Double.parseDouble(m.get("fin_montoIsertando").toString());
			sMonto = d.formatDouble(montototal);
			equiv = montototal;
			
			//--------------- Cambiar ubicacion de referencias				
			if (metodoId.equals(MetodosPagoCtrl.CHEQUE)){//cheque
				ref4 = ref3;
				ref3 = ref2;
				ref2 = ref1;
				ref1 = ddlBanco.getValue().toString().split("@")[1];
			}
			
			if(metodoId.equals(MetodosPagoCtrl.TARJETA)){//TC
				ref3 = ref2;
				ref2 = ref1;
				ref1 = ddlAfiliado.getValue().toString();	
				
				if( usaSocketPos && !voucherManual){
					ref1 = ((ddlAfiliado.getValue().toString()).split(","))[0];
					sTerminal = ((ddlAfiliado.getValue().toString()).split(","))[1];						
					if(ingresoManual){
						String snoT = txtNoTarjeta.getValue().toString().trim();
						ref3 = (snoT).substring(snoT.length()-4, snoT.length());//4 ult. digitos de tarjeta
						ref4 = txtNoTarjeta.getValue().toString();	
						ref5 = txtFechaVenceT.getValue().toString();	
					}else{
						sTrack = track.getValue().toString();
						sTrack = d.rehacerCadenaTrack(sTrack);
						lstDatosTrack = d.obtenerDatosTrack(sTrack);
						
						//4 ult. digitos de tarjeta
						ref3 = lstDatosTrack.get(1).substring
								(lstDatosTrack.get(1).length()-4, 
								lstDatosTrack.get(1).length());
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
				else if(usaSocketPos && voucherManual){
					ref1 = ((ddlAfiliado.getValue().toString()).split(","))[0];
					sTerminal = ((ddlAfiliado.getValue().toString()).split(","))[1];	
				}
				bTarjeta = true;		
			}else if(metodoId.equals(MetodosPagoCtrl.TRANSFERENCIA)){//Transf. Elect.
				ref3 = ref2;
				ref2 = ref1;
				ref1 = ddlBanco.getValue().toString().split("@")[1];		
			}else if(metodoId.equals(MetodosPagoCtrl.DEPOSITO)){//Deposito en banco
				ref1 = ddlBanco.getValue().toString().split("@")[1];
			}
			
			// poner descripcion a metodo
			for (int m = 0; m < lstMetPago.length; m++) {
				if (metodoId.trim().equals(lstMetPago[m].getId().getCodigo().trim())) {
					metododesc = lstMetPago[m].getId().getMpago();
					break;
				}
			}
			//-------------- Equivalente del monto a la moneda de la Unidad de Negocio.
			
			List<Credhdr> lstFacturasSelected = (List<Credhdr>) m.get("lstComponentes");
			Credhdr hFac =  lstFacturasSelected.get(0);
			sMonUnineg = hFac.getId().getMoneda();
			
			//obtener companias x caja
			sMonedaBase = new CompaniaCtrl().sacarMonedaBase
								((F55ca014[])m.get("cont_f55ca014"),
								hFac.getId().getCodcomp());
			
			equiv = montototal;
			if (hFac.getId().getMoneda().equals(sMonedaBase)) {// Domestic
				if (moneda.compareTo(sMonedaBase) != 0) {
					tasapago = new BigDecimal( tasaofi.toString() );
					equiv = d.roundDouble(montototal * tasaofi.doubleValue());
				}
			} else {
				if (moneda.compareTo(sMonedaBase) == 0) {
					tasapago = new BigDecimal( tasapara.toString() );
					equiv = d.roundDouble(montototal / tasapara.doubleValue());
				} 
			}
			
			
			//&& ============ Datos de tipo de marca tarjeta
			String codigomarcatarjeta = "";
			String marcatarjeta = "";
			
			if( bTarjeta ){
				codigomarcatarjeta = ddlTipoMarcasTarjetas.getValue().toString().split("@")[0];
				marcatarjeta = ddlTipoMarcasTarjetas.getValue().toString().split("@")[1];
			}
			
			
			//-------------- obtener los métodos de pago y verificar que no se repita el registro
			if(lstPagos != null) {
				for(int i=0;i < lstPagos.size();i++){
					
					MetodosPago mp = ((MetodosPago)lstPagos.get(i));					
					
					if( mp.getMetodo().equals(metodoId)  && mp.getMoneda().equals(moneda)    && 
						mp.getReferencia().equals(ref1)  && mp.getReferencia2().equals(ref2) && 
						mp.getReferencia3().equals(ref3) && mp.getReferencia4().equals(ref4)){
						montototal = montototal + mp.getMonto();																			
						equiv = equiv + mp.getEquivalente();
						sMonto = d.formatDouble(montototal);
						sEquiv = d.formatDouble(equiv);
						mp.setMonto(montototal);
						mp.setEquivalente(equiv);
						
						mp.setReferencia5("");
						if (bTarjeta && usaSocketPos && !voucherManual) {
							mp.setTrack(sTrack);
							mp.setTerminal(sTerminal);
							mp.setReferencia5(ref5);
							mp.setMontopos(montototal);
							mp.setVmanual("2");
						} 
						mp.setReferencia6("");
						mp.setReferencia7("");
						mp.setNombre("");
						
						mp.setCodigomarcatarjeta(codigomarcatarjeta);
						mp.setMarcatarjeta(marcatarjeta);
						
						flgpagos = false;
					}
				}				
			} else {
				MetodosPago metpagos = new MetodosPago(metododesc,
						metodoId, moneda, montototal, tasapago, equiv, ref1,
						ref2, ref3, ref4, sVoucherManual, 0);
				
				metpagos.setReferencia5("");
				if (bTarjeta && usaSocketPos && !voucherManual) {
					metpagos.setTrack(sTrack);
					metpagos.setTerminal(sTerminal);
					metpagos.setReferencia5(ref5);
					metpagos.setMontopos(montototal);
					metpagos.setVmanual("2");
				} 					
				metpagos.setReferencia6("");
				metpagos.setReferencia7("");
				metpagos.setNombre("");
				
				metpagos.setCodigomarcatarjeta(codigomarcatarjeta);
				metpagos.setMarcatarjeta(marcatarjeta);
				
				lstPagos = new ArrayList();				
				lstPagos.add(metpagos);
				
				flgpagos = false;
			}	
			
			if (flgpagos){
				MetodosPago metpagos = new MetodosPago(metododesc,
						metodoId, moneda, montototal, tasapago, equiv, ref1,
						ref2, ref3, ref4, sVoucherManual, 0);	

				metpagos.setReferencia5("");
				if (bTarjeta && usaSocketPos && !voucherManual) {
					metpagos.setTrack(sTrack);
					metpagos.setTerminal(sTerminal);
					metpagos.setReferencia5(ref5);
					metpagos.setMontopos(montototal);
					metpagos.setVmanual("2");
				}
				metpagos.setReferencia6("");
				metpagos.setReferencia7("");
				metpagos.setNombre("");
				
				metpagos.setCodigomarcatarjeta(codigomarcatarjeta);
				metpagos.setMarcatarjeta(marcatarjeta);
				
				lstPagos.add(metpagos);
			}
			
			m.put("lstPagosFinan", lstPagos);
			gvMetodosPago.dataBind();
			
			//&& ========= calcular el monto recibido
			montoRecibido = 0;
			for(int i = 0; i < lstPagos.size();i++)
				montoRecibido += lstPagos.get(i).getEquivalente();
			
			m.put("fin_montoRecibibo", montoRecibido);					
			
			montoRecibido = calcularElMontoRecibido(lstPagos, hFac, 
					lstFacturasSelected, sMonedaBase);
			distribuirMontoAplicar(sMonedaBase);
			determinarCambio(lstPagos,hFac,montoRecibido,sMonedaBase);
			
			//------------------- OPERACIONES DE LA SOLICITUD -------------------------------//
			//-------------- guardar solicitud en mapa
			List lstSolicitud = null;
			lstSolicitud = m.get("fin_lstSolicitud") == null? 
								new ArrayList():
								(List)m.get("fin_lstSolicitud");
		
			//-------------- Info del autorizador 0 = correo; 1 en adelante = nombre
			String[] sAutorizador = cmbAutoriza.getValue().toString().split(" ");
			String sNomAut = "";
			for (int i = 1; i < sAutorizador.length;i++)
				sNomAut = sNomAut + sAutorizador[i] + " ";
			
			Solicitud solicitud = new Solicitud();
        	SolicitudId solicitId = new SolicitudId(); 
        	
        	solicitId.setReferencia(txtReferencia.getValue().toString());
        	solicitud.setAutoriza(Integer.parseInt(sAutorizador[1].trim()));
        	solicitud.setObs(txtObs.getValue().toString());
        	solicitud.setFecha((Date)txtFecha.getValue());	
        	solicitud.setMoneda(moneda);
        	solicitud.setMpago(metodoId);
        	solicitud.setMonto(new BigDecimal(String.valueOf(montoInser)));
        	solicitud.setMetododesc(metododesc);
        	
        	solicitud.setId(solicitId);
			lstSolicitud.add(solicitud);
			
			m.put("fin_lstSolicitud", lstSolicitud);
			
			//-------------- Enviar correo a cajero o suplente, responsable de caja y autorizador
			EmpleadoCtrl empCtrl = new EmpleadoCtrl();
			Vautoriz[] vAut;
			Vf0101 vf0101 = null;
			int iCodCajero = 0;
			List lstAutorizadores = (List)m.get("fin_lstAutorizadores");
			String sFromCajero = "", sToAutoriz = "";
			Matcher matCorreo = null;
			Pattern pCorreo = Pattern.compile( "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$" );	
				 
			//-------------- obtener el correo del remitente para la solicitud
			vAut = (Vautoriz[])m.get("sevAut");
			iCodCajero = vAut[0].getId().getCodreg();
			vf0101 = empCtrl.buscarEmpleadoxCodigo2(iCodCajero);
			sFromCajero = vf0101 == null? caja.getId().getCacatimail(): vf0101.getId().getWwrem1();
			
			if(sFromCajero == null || !pCorreo.matcher(sFromCajero.trim().toUpperCase()).matches())
				sFromCajero = "webmaster@casapellas.com.ni";

			List<String> lstCc = new ArrayList<String>();
			if(!sFromCajero.equals("webmaster@casapellas.com.ni"))
				lstCc.add(sFromCajero);
			
			String sSucursal = caja.getId().getCaconom().trim();
			String sCajero	 = m.get("sNombreEmpleado").toString();
			String sSubject	 = caja.getId().getCaname().trim() + ": Solicitud de Autorización de ingresos a caja";
			String sMontoAutorizado =  d.formatDouble(Double.parseDouble(m.get("fin_monto").toString()));
			sToAutoriz = sAutorizador[0];
			
			//-------------- validar el destino y mandar el correo.
			if(sToAutoriz != null && pCorreo.matcher(sFromCajero.trim().toUpperCase()).matches()){
				solCtrl.enviarCorreo(sToAutoriz, sFromCajero, caja.getId().getCaname().trim(),
									lstCc, txtReferencia.getValue().toString(),
									txtObs.getValue().toString(), sSubject,sCajero,
									sNomAut, sSucursal.trim(), caja.getId().getCaname().trim(),
									sMontoAutorizado + " " + moneda, metododesc);
			}
			//------------ Limpiar objetos del pago.
			txtMonto.setValue("");
			txtReferencia1.setValue("");
			txtReferencia2.setValue("");
			txtReferencia3.setValue("");
			track.setValue("");
			txtNoTarjeta.setValue("");	
			txtFechaVenceT.setValue("");	
			
			//------------ Limpiar objetos de ventana autorización.
			txtFecha.setValue("");
			txtReferencia.setValue("");
			txtObs.setValue("");
			
	  	}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			dwSolicitud.setWindowState("hidden");
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
					Pattern pAlfa = Pattern.compile("^[A-Za-z0-9-.;,\\p{Blank}]+$");
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
/*****************************************************************/	
	public void restableceEstiloAutorizacion(){
		txtObs.setStyleClass("frmInput2");
		txtReferencia.setStyleClass("frmInput2");
		txtFecha.setStyleClass("");
		cmbAutoriza.setStyleClass("frmInput2");
		
	}
/*****************************************************************/
	public void cancelarSolicitud(ActionEvent e) {	
		restableceEstiloAutorizacion();
		txtFecha.setValue("");
		txtReferencia.setValue("");
		txtObs.setValue("");
		dwSolicitud.setWindowState("hidden");		
	}
/*******************************************************************/
	public void onCerrarAutorizacion(StateChangeEvent e){
		dwSolicitud.setWindowState("hidden");
	}
/********************************************************************/	
	public void quitarPago(ActionEvent e){

		String sMonedaBase = new String("");

		try{
		
			MetodosPago mPago = (MetodosPago) m.get("metodopagoborrar");	
			List lstFacturasSelected = (List) m.get("lstComponentes");
			Credhdr f = (Credhdr) lstFacturasSelected.get(0);
			
			
			// && ========== Borrar datos de la donacion.
			if (mPago.isIncluyedonacion()) {
				CodeUtil.removeFromSessionMap(new String[] 
					{ "fin_MontoTotalEnDonacion","fin_lstDonacionesRecibidas" });
			}
			
			F55ca014 dtComp = com.casapellas.controles.tmp.CompaniaCtrl
								.filtrarCompania((F55ca014[])m.get
								("cont_f55ca014"), f.getId().getCodcomp());
			sMonedaBase = dtComp.getId().getC4bcrcd();
			
			//&& ========== remover solicitudes, en caso de existir.
			ArrayList<Solicitud> lstSolicitud = m.containsKey("fin_lstSolicitud")? 
					(ArrayList<Solicitud>)m.get("fin_lstSolicitud"):
					 new ArrayList<Solicitud>();
			com.casapellas.controles.tmp.CtrlSolicitud
								.removerSolicitud(lstSolicitud, mPago);
			
			//&& ========== remover el pago de la lista de los registrados.	
			lstPagos = (ArrayList<MetodosPago>)m.get("lstPagosFinan");				
			com.casapellas.controles.tmp.MetodosPagoCtrl
									.removerPago(lstPagos, mPago);
			gvMetodosPago.dataBind();
			
			//====== calcular el monto recibido 
			double montoRecibido = calcularElMontoRecibido(lstPagos, f,
					lstFacturasSelected, sMonedaBase);

			// distribuir el monto recibido a los montos a aplicar en factura
			distribuirMontoAplicar(sMonedaBase);

			// determinar cambio
			determinarCambio(lstPagos, f, montoRecibido, sMonedaBase);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			dwBorrarPago.setWindowState("hidden");
			m.remove("metodopagoborrar");
		}
	}
	
	public BigDecimal tasaOficial(String moneda){
		BigDecimal tasa = BigDecimal.ONE;
		try {
			if( !m.containsKey("tcambio") )
				return tasa;
			List<Tcambio> tasasjde = Arrays.asList((Tcambio[])
											m.get("tcambio"));
			for (Tcambio tc : tasasjde) {
				if(tc.getId().getCxcrcd().compareTo(moneda) == 0){
					tasa = tc.getId().getCxcrrd();
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tasa;
	}
	public BigDecimal tasaParalela(String moneda){
		BigDecimal tasa = BigDecimal.ONE;
		try {
			
			if( !m.containsKey("tpcambio") )
				return tasa;
			List<Tpararela> tasaspar = Arrays.asList((Tpararela[])
										m.get("tpcambio"));
			for (Tpararela tp : tasaspar) {
				if (tp.getId().getCmono().equals(moneda) ||
						tp.getId().getCmond().equals(moneda)) {
					tasa = tp.getId().getTcambiom();
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tasa;
	}
	
/********************************************************************************************/
	@SuppressWarnings("unchecked")
	public void agregarMetodo(ActionEvent ev){
		Divisas d = new Divisas();
		
		BigDecimal tasaofi  = BigDecimal.ONE;
		BigDecimal tasapara = BigDecimal.ONE;
		BigDecimal tasapago = BigDecimal.ONE;
		
		Double monto = 0.0, equiv;
		int  cant = 1;
		double montoRecibido = 0;
		boolean valido = false,flgpagos = true, bTarjeta = false;
		Tpararela[] tcPar = null;
		Tcambio[] tcJDE = null;

		Vf55ca012[] lstMetPago = null;
		String sVoucherManual="0";
		String sMonedaBase = "";
		
		Vf55ca01 caja = null;
		List<String> lstDatosTrack  = null;
		List lstDatosTrack2 = null;
		List<Credhdr> lstFacturasSelected = null;
		try{
			//&& ===== obtener datos de Caja
			caja =  ((List<Vf55ca01>) m.get("lstCajas")).get(0);
			
			boolean usaSocketPos  = caja.getId().getCasktpos() == 'Y';
			boolean ingresoManual = chkIngresoManual.isChecked();
			boolean voucherManual = chkVoucherManual.isChecked();
			boolean cnfirmAuto = false;
			
			//&& ========== determinar si es voucher Manual
			sVoucherManual = (chkVoucherManual.isChecked())?"1":"0";
			
			String metodo = ddlMetodosPago.getValue().toString();
			String descMetodo = "";
			String moneda = ddlMoneda.getValue().toString();
			String ref1 = txtReferencia1.getValue().toString().trim();
			String ref2 = txtReferencia2.getValue().toString().trim();
			String ref3 = txtReferencia3.getValue().toString().trim();
			String ref4 = new String("");
			String ref5 = new String("");
			String sTrack = new String("");
			String sTerminal = new String("");
			String codcomp = new String("");
			
			lstMetPago = (Vf55ca012[]) m.get("metpago");	
			lstPagos = (ArrayList<MetodosPago>) m.get("lstPagosFinan");
			
			//&& ======== tasas de cambio.
			tasaofi  = tasaOficial(moneda);
			tasapara = tasaParalela(moneda);
			
			//&& ======== obtener datos de factura
			lstFacturasSelected = (List<Credhdr>)
										m.get("lstComponentes");
			Credhdr hFac = (Credhdr) lstFacturasSelected.get(0);
			codcomp = hFac.getId().getCodcomp() ;
			
			//&& ======== obtener companias x caja
			F55ca014 dtComp = com.casapellas.controles.tmp.CompaniaCtrl
							.filtrarCompania(((F55ca014[])m.get
								("cont_f55ca014")), codcomp) ;
			
			sMonedaBase = dtComp.getId().getC4bcrcd();
			cnfirmAuto = dtComp.getId().isConfrmauto();
				
			//&& ========== Determinar el monto aplicado en el pago.
			BigDecimal bdMtoAplicar = BigDecimal.ZERO;
			if(m.containsKey("finan_MontoAplicar")){
				bdMtoAplicar = new BigDecimal(String.valueOf(m.get("finan_MontoAplicar")));
			}else{
				for (Credhdr chFacts : lstFacturasSelected) 
					bdMtoAplicar = bdMtoAplicar.add(chFacts.getMontoPendiente());
				bdMtoAplicar = d.roundBigDecimal(bdMtoAplicar);
			}
			
			
			String strMonto = txtMonto.getValue().toString().trim() ;
			if ( !strMonto.trim().matches(PropertiesSystem.REGEXP_AMOUNT) ) {
				lblMensajeError.setValue(" Monto inválido ");
				dwMensajeError.setWindowState("normal") ;
				SmartRefreshManager.getCurrentInstance().addSmartRefreshId(
						dwMensajeError.getClientId(FacesContext.getCurrentInstance()));
				return;
			}
			
			//========================================================================================
			
			//&& ============= restar el monto de la donacion antes de la validacion del monto del pago.
			BigDecimal montoendonacion = BigDecimal.ZERO;
			BigDecimal montooriginal = new BigDecimal(strMonto);
			BigDecimal montoNeto = new BigDecimal(strMonto);
			BigDecimal montoaplicadoorgn = new BigDecimal(bdMtoAplicar.toString()) ;
			
			
			boolean aplicadonacion = ( CodeUtil.getFromSessionMap("fin_MontoTotalEnDonacion") != null );
			
			
			if(aplicadonacion){
				montoendonacion = new BigDecimal( String.valueOf( CodeUtil.getFromSessionMap("fin_MontoTotalEnDonacion") ) );
				montoNeto = montooriginal.subtract(montoendonacion) ;
				strMonto = montoNeto.toString().trim();
			}
			
			boolean donaciontotal = aplicadonacion && montoNeto.compareTo(BigDecimal.ZERO) == 0 ;
			if(donaciontotal) {
				strMonto = montooriginal.toString().trim() ;
				bdMtoAplicar = bdMtoAplicar.add(BigDecimal.TEN);
			}
			
			//=========================================================================================
			
			
			//&& ======== validar datos de los métodos de pago.
			valido = validarMpagos(metodo, strMonto ,
						codcomp, moneda, ref1, ref2, ref3, ref4, 
						lstFacturasSelected, usaSocketPos, 
						voucherManual, cnfirmAuto, bdMtoAplicar.doubleValue(),
						sMonedaBase, tasaofi, tasapara);
			
			if(!valido){
				SmartRefreshManager.getCurrentInstance().addSmartRefreshId(
						dwMensajeError.getClientId(FacesContext.getCurrentInstance()));
				SmartRefreshManager.getCurrentInstance().addSmartRefreshId(
						dwSolicitud.getClientId(FacesContext.getCurrentInstance()));
				return;
			}
			
			if(donaciontotal) {
				strMonto = montoNeto.toString();
				bdMtoAplicar = new BigDecimal(montoaplicadoorgn.toString()) ; 
			}
			
			
			
			monto = Double.parseDouble( strMonto );
			equiv = monto;
			
			// poner descripcion a metodo
			for (int m = 0; m < lstMetPago.length; m++) {
				if (metodo.trim().equals(lstMetPago[m].getId().getCodigo().trim())) {
					descMetodo = lstMetPago[m].getId().getMpago();
					break;
				}
			}
			// cambiar ubicacion de referencias
			if (metodo.equals(MetodosPagoCtrl.CHEQUE)) {// cheque
				ref4 = ref3;
				ref3 = ref2;
				ref2 = ref1;
				ref1 = ddlBanco.getValue().toString().split("@")[1];
			} else if (metodo.equals(MetodosPagoCtrl.TARJETA)) {// TC
				ref3 = ref2;
				ref2 = ref1;
				ref1 = ddlAfiliado.getValue().toString();
				
				//&& ============ si la caja usa socketPOS
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
				else if( usaSocketPos  && voucherManual){
					ref1 = ((ddlAfiliado.getValue().toString()).split(","))[0];
					sTerminal = ((ddlAfiliado.getValue().toString()).split(","))[1];	
				}
				bTarjeta = true;
			} else if (metodo.equals(MetodosPagoCtrl.TRANSFERENCIA)) {// Transf. Elect.
				ref3 = ref2;
				ref2 = ref1;
				ref1 = ddlBanco.getValue().toString().split("@")[1];
			} else if (metodo.equals(MetodosPagoCtrl.DEPOSITO)) {// Deposito en banco
				ref1 = ddlBanco.getValue().toString().split("@")[1];
			}
			
			// calcular equivalente en dependencia de moneda de factura y moneda de pago
			equiv = monto;
			if (hFac.getId().getMoneda().equals(sMonedaBase)) {// Domestic
				if (moneda.compareTo(sMonedaBase) != 0) {
					tasapago = new BigDecimal( tasaofi.toString() );
					equiv = d.roundDouble(monto * tasaofi.doubleValue());
				}
			} else {
				if (moneda.compareTo(sMonedaBase) == 0) {
					tasapago = new BigDecimal( tasapara.toString() );
					equiv = d.roundDouble(monto / tasapara.doubleValue());
				} 
			}
			
			if(donaciontotal) {
				equiv = monto;
				tasapago = BigDecimal.ONE;
			}
			
			
			//--- Validaciones de excedentes en pago.
			boolean bValido=true;
			String sMensaje="";
			double dMontoMetodos = equiv;
			
			if(!metodo.equals(MetodosPagoCtrl.EFECTIVO) && !donaciontotal ){
				if( equiv > bdMtoAplicar.doubleValue()){
					sMensaje = "El Monto del método de pago por: "+equiv+" excede el monto aplicado: "+bdMtoAplicar.toString();
					bValido=false;
				}
			}
			if(lstPagos!=null && lstPagos.size()>0){

				boolean bEfectivo = false;
				if(metodo.equals(MetodosPagoCtrl.EFECTIVO))
					bEfectivo = true;
				
				//--- Total de metodos de pago
				for (MetodosPago mp : lstPagos) {
					if(mp.getMetodo().equals(MetodosPagoCtrl.EFECTIVO)){
						bEfectivo = true;
						break;
					}
					else{
						dMontoMetodos += mp.getEquivalente();
					}
				}
				dMontoMetodos = d.roundDouble(dMontoMetodos);
				if(dMontoMetodos > bdMtoAplicar.doubleValue()&& !bEfectivo){
					sMensaje = "El consolidado de montos de: "+dMontoMetodos+" excede el monto aplicado: "+bdMtoAplicar.toString();
					bValido=false;
				}
			}
			if(!bValido){
				lblMensajeError.setValue(sMensaje);
				dwMensajeError.setStyle("width:320px;height: 160px");
				dwMensajeError.setWindowState("normal");
				SmartRefreshManager.getCurrentInstance().addSmartRefreshId(
						dwMensajeError.getClientId(FacesContext.getCurrentInstance()));
				return;
			}
			//--- Determinar si el pago es con voucher manual y agregar el método
			sVoucherManual = (voucherManual) ? "1" : "0";
			
			String codigomarcatarjeta = "";
			String marcatarjeta = "";
			
			if( bTarjeta ){
				codigomarcatarjeta = ddlTipoMarcasTarjetas.getValue().toString().split("@")[0];
				marcatarjeta = ddlTipoMarcasTarjetas.getValue().toString().split("@")[1];
			}
			
			if (lstPagos != null) {
				MetodosPago[] mpagos = new MetodosPago[lstPagos.size()];
				cant = lstPagos.size();

				for (int i = 0; i < cant; i++) {
					mpagos[i] = ((MetodosPago) lstPagos.get(i));
				}
				// valida que se agreguen los metodos iguales a un solo registro
				for (int i = 0; i < mpagos.length; i++) {
					if (mpagos[i].getMetodo().equals(metodo)
							&& mpagos[i].getMoneda().equals(moneda)
							&& mpagos[i].getReferencia().equals(ref1)
							&& mpagos[i].getReferencia2().equals(ref2)
							&& mpagos[i].getReferencia3().equals(ref3)
							&& mpagos[i].getReferencia4().equals(ref4)) {

						monto = monto + mpagos[i].getMonto();
						equiv = equiv + mpagos[i].getEquivalente();

						mpagos[i].setMonto(monto);

						mpagos[i].setReferencia5("");
						mpagos[i].setEquivalente(equiv);
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

						flgpagos = false;
					}
				}
			} else {
				MetodosPago metpagos = new MetodosPago(descMetodo, metodo,
						moneda, monto, tasapago, equiv, ref1, ref2, ref3, ref4,
						sVoucherManual, 0);
				
				metpagos.setMontorecibido(montooriginal );
				metpagos.setMontoendonacion(montoendonacion);
				metpagos.setIncluyedonacion(aplicadonacion);
				
				if(aplicadonacion){
					metpagos.setDonaciones(
						new ArrayList<DncIngresoDonacion>((ArrayList<DncIngresoDonacion>)
								CodeUtil.getFromSessionMap("fin_lstDonacionesRecibidas")) ) ;
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
				
				lstPagos = new ArrayList();
				lstPagos.add(metpagos);
				flgpagos = false;
			}

			if (flgpagos) {
				MetodosPago metpagos = new MetodosPago(descMetodo, metodo,
						moneda, monto, tasapago, equiv, ref1, ref2, ref3, ref4,
						sVoucherManual, 0);
				
				
				metpagos.setMontorecibido(montooriginal );
				metpagos.setMontoendonacion(montoendonacion);
				metpagos.setIncluyedonacion(aplicadonacion);
				
				if(aplicadonacion){
					metpagos.setDonaciones(
						new ArrayList<DncIngresoDonacion>((ArrayList<DncIngresoDonacion>)
								CodeUtil.getFromSessionMap("fin_lstDonacionesRecibidas")) ) ;
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
				
				lstPagos.add(metpagos);
			}
			
			m.put("lstPagosFinan", lstPagos);
			gvMetodosPago.dataBind();
			
			if(aplicadonacion){
				CodeUtil.removeFromSessionMap(new String[] { "fin_MontoTotalEnDonacion","fin_lstDonacionesRecibidas" });
			}
			
			//calcular el monto recibido en dependencia de moneda de factura y moneda de pago
			montoRecibido = calcularElMontoRecibido(lstPagos, hFac, lstFacturasSelected, sMonedaBase);
			
			//distribuir el monto a aplicar
			distribuirMontoAplicar(sMonedaBase);
			
			//determinar cambio
			determinarCambio(lstPagos, hFac,montoRecibido,sMonedaBase);
			
			txtMonto.setValue("");
			txtReferencia1.setValue("");
			txtReferencia2.setValue("");
			txtReferencia3.setValue("");
			track.setValue("");
			txtNoTarjeta.setValue("");	
			txtFechaVenceT.setValue("");	
		
			SmartRefreshManager.getCurrentInstance().addSmartRefreshId(
					track.getClientId(FacesContext.getCurrentInstance()));
			SmartRefreshManager.getCurrentInstance().addSmartRefreshId(
					gvMetodosPago.getClientId(FacesContext.getCurrentInstance()));
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	/******************************************************************************************/
/*****************CALCULAR EL MONTO RECIBIDO EN DEPENDENCIA DE LOS METODOS DE PAGO******************************/
	public double calcularElMontoRecibido(List selectedMet,Credhdr hFac, List selectedFacs, String sMonedaBase){
		MetodosPago metMontoRec = null;
		Divisas divisas = new Divisas();
		double montoRecibido = 0.0,cambio = 0.0, dMontoAplicar = 0.0;
		BigDecimal dMontoAplFacturas = BigDecimal.ZERO;
		try{
			for (int i = 0;i < selectedFacs.size(); i++){				
				dMontoAplFacturas = dMontoAplFacturas.add(((Credhdr)selectedFacs.get(i)).getMontoPendiente());				
			}
			for(int z = 0; z < selectedMet.size();z++){
				metMontoRec = (MetodosPago)selectedMet.get(z);
				if(hFac.getId().getMoneda().equals(sMonedaBase)){//domestica
					if(metMontoRec.getMoneda().equals(sMonedaBase)){
						montoRecibido = montoRecibido + (metMontoRec.getMonto());
					}else{//foranea
						montoRecibido = montoRecibido + (metMontoRec.getEquivalente());
					}
				}else{//foranea
					if(metMontoRec.getMoneda().equals(sMonedaBase)){
						montoRecibido = montoRecibido + (metMontoRec.getEquivalente());
					}else{
						montoRecibido = montoRecibido + (metMontoRec.getMonto());
					}
				}
			}
			
			//comparar contra monto aplicar de facturas seleccionadas
			/*if(m.get("dMontoAplicarCre") == null){
				if (montoRecibido <= dMontoAplFacturas.doubleValue()){
					dMontoAplFacturas = new BigDecimal(montoRecibido);
				}else{
					dMontoAplFacturas = dMontoAplFacturas;					
				}
			}else{
				dMontoAplFacturas = new BigDecimal(Double.parseDouble(m.get("dMontoAplicarCre").toString()));
			}*/	
			if (m.get("finan_MontoAplicar")== null){
				if(dMontoAplFacturas.doubleValue() < montoRecibido){				
					txtMontoAplicar.setValue(divisas.formatDouble(dMontoAplFacturas.doubleValue()));
					cambio = montoRecibido - dMontoAplFacturas.doubleValue();
				}else{
					txtMontoAplicar.setValue(divisas.formatDouble(montoRecibido));
					cambio = 0.0;
				}
			}else {
				dMontoAplicar = divisas.formatStringToDouble(m.get("finan_MontoAplicar").toString());
				cambio = (dMontoAplicar < montoRecibido) ?
						montoRecibido - dMontoAplicar: 0;
			}	
			
			if(cambio < 0){
				txtCambio.setStyle("color: red;font-size: 10pt");
			}else if(cambio > 0){
				txtCambio.setStyle("color: green;font-size: 10pt");
			}else{
				txtCambio.setStyle("font-size: 10pt");
			}
			txtCambio.setValue(divisas.formatDouble(cambio));
			m.put("montoRecibibo", montoRecibido);					
			txtMontoRecibido.setValue(divisas.formatDouble(montoRecibido));
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return montoRecibido;
	}
/***********************************************************************************************************/
/********************DISTRIBUIR EL MONTO A APLICAR EN EL GRID DE DETALLE DE FACTURA***********************/
	public void distribuirMontoAplicar(String sMonedaBase){
		Divisas divisas = new Divisas();
		double dMontoAplicar = 0.0, dAcumMontoApl = 0.00;
		Credhdr hFac = null;
		double dMontoPendActual = 0.00, dMontoPendAnt = 0.00, dMonAplRestante = 0.00,dMontoRecibido = 0.00; 
		boolean[] bIntAplicado = null;
		try{
			// obtener datos de factura
			List lstFacturasSelected = (List) m.get("lstComponentes");
			bIntAplicado = new boolean[lstFacturasSelected.size()];
			dMontoRecibido = divisas.formatStringToDouble(txtMontoRecibido.getValue().toString());
			//setear el monto a aplicar a cero
			for(int s = 0 ; s < lstFacturasSelected.size(); s++){
				hFac = (Credhdr)lstFacturasSelected.get(s);
				hFac.setMontoAplicar(BigDecimal.ZERO);
				lstFacturasSelected.set(s, hFac);
			}
			//
			if (m.get("finan_MontoAplicar")== null){
				dMontoAplicar = divisas.formatStringToDouble(txtMontoAplicar.getValue().toString());
				for(int i = 0 ; i < lstFacturasSelected.size(); i++){
					hFac = (Credhdr)lstFacturasSelected.get(i);
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
						lstFacturasSelected.set(i, hFac);
					}
					else /*if( i != selectedFacs.size() - 1 && i != 0)*/{//no es la ultima
						dMonAplRestante = dMontoRecibido - dAcumMontoApl;
						if (dMonAplRestante <= 0){//ya no hay monto restante
							hFac.setMontoAplicar(new BigDecimal(0.00));
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
						lstFacturasSelected.set(i, hFac);
					}
				}
			}else {
				
				dMontoAplicar = divisas.formatStringToDouble(m.get("finan_MontoAplicar").toString());
				dMontoRecibido = divisas.formatStringToDouble(txtMontoRecibido.getValue().toString());
				
				if(dMontoRecibido > dMontoAplicar ){
					dMontoRecibido = dMontoAplicar ;
				}
				
				if(dMontoRecibido > 0){
					
					for(int i = 0 ; i < lstFacturasSelected.size(); i++){
						hFac = (Credhdr)lstFacturasSelected.get(i);
						
						//obtener el monto pendiente de factura dependiendo de la moneda
						if(hFac.getMoneda().equals(sMonedaBase)){
							dMontoPendActual = hFac.getId().getCpendiente().doubleValue();
						}else{
							dMontoPendActual = hFac.getId().getDpendiente().doubleValue();
						}
						
						//primera factura
						if(i == 0){
							
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
							
							lstFacturasSelected.set(i, hFac);
						}
						else {
							dMonAplRestante = dMontoAplicar - dAcumMontoApl;
							if (dMonAplRestante <= 0){//ya no hay monto restante
								hFac.setMontoAplicar(new BigDecimal(0.00));
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
							lstFacturasSelected.set(i, hFac);
						}
					}//fin de for
				}
			}	
			
			//
			
			m.put("lstComponentes", lstFacturasSelected);
			gvSelectedCuotas.dataBind();
			//calcular resumen de totales
			dMontoRecibido = divisas.formatStringToDouble(txtMontoRecibido.getValue().toString());
			if(!hFac.getId().getMoneda().equals(sMonedaBase)){
				if(dMontoRecibido <= dMontoAplicar){
					montoTotalFaltanteForaneo.setValue("<B>" + hFac.getId().getMoneda() + "</B>" +" "+ divisas.formatDouble(dMontoAplicar - dMontoRecibido));
					montoTotalFaltanteDomestico.setValue("<B>"+sMonedaBase+"</B> " + divisas.formatDouble((dMontoAplicar - dMontoRecibido)* (obtenerTasaParalela(hFac.getId().getMoneda())).doubleValue()));
				}else{
					montoTotalFaltanteForaneo.setValue("<B>" + hFac.getId().getMoneda() + "</B>" +" 0.00");
					montoTotalFaltanteDomestico.setValue("<B>"+sMonedaBase+"</B> 0.00");
				}
			}else{
				if(dMontoRecibido <= dMontoAplicar){
					montoTotalFaltanteDomestico.setValue("<B>"+sMonedaBase+"</B> " + divisas.formatDouble(dMontoAplicar - dMontoRecibido));
				}else{
					montoTotalFaltanteDomestico.setValue("<B>"+sMonedaBase+"</B> 0.00");
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			
		}
	}
/***********************************************************************************************************/
	/********** 	validación de los valores para los métodos de pago ingresados	***********/
	public boolean validarMpagos(String metodo, String sMonto, String sCodcomp,
			String sMoneda, String ref1, String ref2, String ref3, String ref4,
			List<Credhdr> lstFacturasSelected, boolean usaSckPos,
			boolean usaManual, boolean bConfirmAuto, double montoaplicar,
			String monedabase, BigDecimal tasaofi, BigDecimal tasaspara) {
		
			boolean validado = true, bErrorPoliticas = false, bEfectivo = false;
			int y = 158,iCaid=0;
			double monto = 0;
			String sEstiloMserror="",sMensajeError = "",sCajaId="";
			CtrlCajas cc = new CtrlCajas();
			Divisas divisas = new Divisas();
			CtrlPoliticas polCtrl = new CtrlPoliticas();
			Matcher matNumero=null,matAlfa1=null,matAlfa2=null,matAlfa3=null,matAlfa4=null;
			Pattern pAlfa;
			
			try {

				//&& ============== Lista de datasource del grid de pagos, Id de la caja.
				List selectedMet = (List) m.get("lstPagosFinan");
				sCajaId = (String)m.get("sCajaId");
				iCaid = Integer.parseInt(sCajaId);
				restablecerEstilosPago();
				sEstiloMserror = " <img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> ";
			
				//Patrones para monto y referencias
				Pattern pNumero = Pattern.compile("^[0-9]+\\.?[0-9]*$");
				pAlfa = Pattern.compile("^[A-Za-z0-9-\\p{Blank}]+$");
				Pattern pAlfaRef1 = Pattern.compile("^[A-Za-z0-9-]+$");
				matAlfa1 = pAlfaRef1.matcher(ref1);
				matAlfa2 = pAlfa.matcher(ref2);
				matAlfa3 = pAlfa.matcher(ref3);
				matAlfa4 = pAlfa.matcher(ref4);
				matNumero = pNumero.matcher(sMonto);
				
				if(matNumero.matches())
					monto = divisas.roundDouble(Double.parseDouble(sMonto));

				if(sMonto.equals("")){
					validado = false;
					txtMonto.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError + sEstiloMserror +" El monto es requerido<br>";
					dwMensajeError.setWindowState("normal");	
					y = y + 5;
				}
				else if (matNumero == null || !matNumero.matches() ||monto == 0){
					txtMonto.setValue("");
					validado = false;
					txtMonto.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError +sEstiloMserror+ "El monto ingresado no es correcto<br>";
					dwMensajeError.setWindowState("normal");	
					y = y + 5;
				}
				
				if(!validado){
					lblMensajeError.setValue(sMensajeError);
					return false;
				}
				
				Credhdr hfac = lstFacturasSelected.get(0);
				double  equiv = monto;
				boolean pagofact = hfac.getId().getMoneda().compareTo(sMoneda) == 0;
				boolean factbase = hfac.getId().getMoneda().compareTo(monedabase) == 0 ;
				
				if(!pagofact)
					equiv = (factbase)? (monto * tasaofi.doubleValue()) : 
										(monto / tasaspara.doubleValue());
				equiv = divisas.roundDouble(equiv);
					
				//-------------------- método: cheques Q.
				if(metodo.equals(MetodosPagoCtrl.CHEQUE)){
					if(!ref1.matches("^[0-9]{1,8}$")){
						validado = false;
						y = y + 7;
						sMensajeError = sMensajeError + sEstiloMserror+ "1 a 8 Dígitos para número de cheque es requerido<br>";
						txtReferencia1.setStyleClass("frmInput2Error");
					}
					if(ref2.equals("")){
						validado = false;
						y = y + 7;
						sMensajeError = sMensajeError + sEstiloMserror+ "Emisor requerido<br>";
						txtReferencia2.setStyleClass("frmInput2Error");
					}else if(!matAlfa2.matches()){
						validado = false;
						y = y + 7;
						sMensajeError = sMensajeError + sEstiloMserror+ "El campo <b>Emisor<b/> contiene caracteres inválidos<br>";
					}else if(ref2.length() > 150){
						validado = false;
						y = y + 15;
						sMensajeError = sMensajeError + sEstiloMserror+ "La cantidad de caracteres del campo <b>Emisor<b/> es muy alta (lim. 150)<br>";
						txtReferencia2.setStyleClass("frmInput2Error");
					}
					if(ref3.equals("")){
						validado = false;
						y = y + 7;
						sMensajeError = sMensajeError + sEstiloMserror+ "Portador requerido<br>";
						txtReferencia3.setStyleClass("frmInput2Error");
					}else if(!matAlfa3.matches()){
						validado = false;
						y = y + 7;
						sMensajeError = sMensajeError + sEstiloMserror+ "El campo <b>Portador<b/> contiene caracteres inválidos<br>";
						txtReferencia3.setStyleClass("frmInput2Error");
					}
					else if(ref3.length() > 150){
						validado = false;
						sMensajeError = sMensajeError + sEstiloMserror+ "La cantidad de caracteres del campo <b>Portador<b/> es muy alta (lim. 150)<br>";
						txtReferencia3.setStyleClass("frmInput2Error");
						y = y + 15;
					}
				}
				//------------------------ método de pago tarjeta de crédito H				
				else if(metodo.equals(MetodosPagoCtrl.TARJETA)){
					
					if(ddlAfiliado.getValue().toString().equals("01")){
						validado = false;
						ddlAfiliado.setStyleClass("frmInput2Error3");
						sMensajeError = sMensajeError + sEstiloMserror+ "Seleccione un afiliado<br>";
						dwMensajeError.setWindowState("normal");	
						y = y + 5;
					}
					
					if(usaSckPos && !usaManual){

						String codpos = ddlAfiliado.getValue().toString();
						String sTrack = track.getValue().toString();
						String notarjeta  = txtNoTarjeta.getValue().toString().trim();
						String fechavence = txtFechaVenceT.getValue().toString().trim();
						
						sMensajeError = PosCtrl.validaPagoSocket(sMoneda,
								equiv, montoaplicar, usaManual, ref1, ref2,
								ref3, iCaid, sCodcomp, codpos, selectedMet, 
								sTrack, notarjeta, fechavence, monto) ;
						
						if(sMensajeError.compareTo("") != 0){  //sMensajeError = "-1"
							
							if (sMensajeError.compareTo("-1") == 0) {
								lblMensajeAutorizacion.setValue("El monto " +
										"ingresado no cumple con las politicas de caja");
								dwSolicitud.setWindowState("normal");
								txtFecha.setValue(new Date());
								m.put("fin_monto", monto);
								m.put("fin_montoIsertando", monto);
							}else{
								dwMensajeError.setWindowState("normal");
								lblMensajeError.setValue(sMensajeError);
							}
							return false;
						}
					}
					else if(ref1.equals("")) {						
						validado = false;
						txtReferencia1.setStyleClass("frmInput2Error");
						sMensajeError = sMensajeError + sEstiloMserror+ "Número de identificación requerido<br>";
						y = y + 5;
					}else if(!matAlfa1.matches()){
						validado = false;
						y = y + 7;
						txtReferencia1.setStyleClass("frmInput2Error");
						sMensajeError = sMensajeError + sEstiloMserror+ "El campo <b>Número de identificación<b/> contiene caracteres inválidos<br>";
					}else if(ref1.length() > 150){
						validado = false;
						y = y + 7;
						sMensajeError = sMensajeError + sEstiloMserror+  "<La cantidad de caracteres del campo <b>Identificación<b/> es muy alta (lim. 150)<br>";
						txtReferencia1.setStyleClass("frmInput2Error");
					}
					else if(ref2.equals("")){
						validado = false;
						txtReferencia2.setStyleClass("frmInput2Error");
						sMensajeError = sMensajeError + sEstiloMserror+ "Número de voucher requerido<br>";
						y = y + 5;
					}
					else if(!pNumero.matcher(ref2).matches()){
						validado = false;
						y = y + 7;
						txtReferencia2.setStyleClass("frmInput2Error");
						sMensajeError = sMensajeError + sEstiloMserror+ "El campo <b>Número de voucher<b/> contiene caracteres inválidos<br>";
					}else if(ref1.length() > 150){
						validado = false;
						y = y + 7;
						sMensajeError = sMensajeError + sEstiloMserror+  "<La cantidad de caracteres del campo <b>Número de voucher<b/> es muy alta (lim. 150)<br>";
						txtReferencia2.setStyleClass("frmInput2Error");
					}
				}
				//Método de pago: Transferencias.
			else if (metodo.equals(MetodosPagoCtrl.TRANSFERENCIA)) {
				//validamos si la referencia agregada ya ha sido utilizada para el mismo banco 
				if(polCtrl.validarReferenciaBancaria(Integer.parseInt(sCajaId), ddlBanco.getValue().toString().split("@")[1], ref2,"T"))
				{
					y = y + 7;
					
					dwMensajeError.setWindowState("normal");
					sMensajeError = "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> " +
							"La referencia ya ha sido utilizada, favor verificar e ingresar una referencia válida ";
					lblMensajeError.setValue("sMensajeError");
					txtReferencia2.setStyleClass("frmInput2Error");
					return validado = false;
				}
				
				if (ref1.equals("")) {
					validado = false;
					txtReferencia1.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError + sEstiloMserror
							+ "Número de identificación requerido<br>";
					y = y + 5;
				} else if (!matAlfa1.matches()) {
					validado = false;
					y = y + 7;
					txtReferencia1.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError
							+ sEstiloMserror
							+ "El campo <b>Número de identificación<b/> contiene caracteres inválidos<br>";
				} else if (ref1.length() > 150) {
					validado = false;
					y = y + 7;
					sMensajeError = sMensajeError
							+ sEstiloMserror
							+ "<La cantidad de caracteres del campo <b>Número de identificación<b/> es muy alta (lim. 150)<br>";
					txtReferencia1.setStyleClass("frmInput2Error");
				}

				if (!ref2.matches(PropertiesSystem.REGEXP_8DIGTS)) {
					y = y + 7;
					txtReferencia2.setStyleClass("frmInput2Error");
					sMensajeError = "El valor de la referencia debe contener entre 1 y 8 dígitos unicamente ";
					return validado = false;
				}

			}
			if (metodo.equals(MetodosPagoCtrl.DEPOSITO)) {

				//validamos si la referencia agregada ya ha sido utilizada para el mismo banco 
				if(polCtrl.validarReferenciaBancaria(Integer.parseInt(sCajaId), ddlBanco.getValue().toString().split("@")[1], ref2,"N"))
				{
					y = y + 7;
					
					dwMensajeError.setWindowState("normal");
					sMensajeError = "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> " +
							"La referencia ya ha sido utilizada, favor verificar e ingresar una referencia válida ";
					lblMensajeError.setValue("sMensajeError");
					txtReferencia2.setStyleClass("frmInput2Error");
					return validado = false;
				}
				
				if (!ref2.matches(PropertiesSystem.REGEXP_8DIGTS)) {
					/*y = y + 7;
					txtReferencia2.setStyleClass("frmInput2Error");
					sMensajeError = "El valor de la referencia debe contener entre 1 y 8 dígitos unicamente ";
					return validado = false;*/
					
					y = y + 7;
					
					dwMensajeError.setWindowState("normal");
					//dwSolicitud.setWindowState("normal");
					sMensajeError = "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> " +
									"El valor de la referencia debe contener entre 1 y 8 dígitos unicamente ";
					//lblMensajeAutorizacion.setValue("sMensajeError");
					lblMensajeError.setValue("sMensajeError");
					txtReferencia2.setStyleClass("frmInput2Error");
					return validado = false;
				}

				
				
				
			}
				 
			if(validado){
				if(!polCtrl.validarMonto(iCaid, sCodcomp, sMoneda, metodo, monto)){
					validado = false;
					bErrorPoliticas = true;
					lblMensajeAutorizacion.setValue("El monto ingresado no cumple con las politicas de caja");
					dwSolicitud.setWindowState("normal");
					Date dFechaActual = new Date();
					txtFecha.setValue(dFechaActual);
					m.put("fin_montoIsertando", monto);
					m.put("fin_monto", monto);
				}else{
					boolean bDuplicado = false;
					if(selectedMet!=null && selectedMet.size() > 0 && validado){
						double dMontoIngresar = monto;
						for(int i=0; i<selectedMet.size(); i++){
							MetodosPago mp = (MetodosPago)selectedMet.get(i);
							if(mp.getMetodo().equals(MetodosPagoCtrl.EFECTIVO))
								bEfectivo = true;
							
							if(mp.getMetodo().equals(metodo)  && mp.getMoneda().equals(sMoneda)){				
								bDuplicado = true;
								monto += mp.getMonto();
								break;
							}								
						}
						
						if(bDuplicado && !polCtrl.validarMonto(iCaid, sCodcomp, sMoneda, metodo, monto)){
							validado = false;
							bErrorPoliticas=true;
							lblMensajeAutorizacion.setValue("El consolidado de los montos del método de pago no cumple con las politicas de caja");
							dwSolicitud.setWindowState("normal");
							Date dFechaActual = new Date();
							txtFecha.setValue(dFechaActual);
							txtFecha.setValue(dFechaActual);
							m.put("fin_monto", monto);
							m.put("fin_montoIsertando", dMontoIngresar);
						}
					}
				}
			}

		} catch (Exception error) {
			
			error.printStackTrace(); 
			validado = false;
			sMensajeError = "Datos de pago no válidos, intente nuevamente" ; 

		} finally {

			if (!validado && !bErrorPoliticas) {
				lblMensajeError.setValue(sMensajeError);
				dwMensajeError.setStyle("width:320px;height:" + y + "px");
			}

		}
		return validado;
	}
/*******************************************************************************/
/**  				REESTABLECER ESTILOS A DATOS DEL PAGO  				      **/
		public void restablecerEstilosPago(){
			try{
				txtMonto.setStyleClass("frmInput2");
				ddlBanco.setStyleClass("frmInput2");
				ddlAfiliado.setStyleClass("frmInput2ddl");	
				txtReferencia1.setStyleClass("frmInput2");
				txtReferencia2.setStyleClass("frmInput2");
				txtReferencia3.setStyleClass("frmInput2");
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
/*******************************************************************************/
/********************************************************************************************/	
	public void setSelectedCreditos(SelectedRowsChangeEvent e) {
		Finanhdr f1 = null, f2 = null;
		List lstSelected = new ArrayList();
		boolean bValido = true;
		RowItem row = null;
		try{
			SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
			lstSelected = gvCuotas.getSelectedRows();						
			
			if(lstSelected.size()>1){//hay validaciones
				row = (RowItem) lstSelected.get(0);
				f1 = (Finanhdr) gvCuotas.getDataRow(row);
				for(int i = 1; i < lstSelected.size();i++){
					row = (RowItem) lstSelected.get(i);
					f2 = (Finanhdr) gvCuotas.getDataRow(row);
					if(f1.getId().getCodcli() != f2.getId().getCodcli()){
						lblMensajeError.setValue("<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El cliente debe ser el mismo<br>");
						bValido = false;
					}
					else if(!f1.getId().getCodcomp().equals(f2.getId().getCodcomp())){
						lblMensajeError.setValue("<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> La compañia debe ser la misma<br>");
						bValido = false;
					}
					else if(!f1.getId().getMoneda().equals(f2.getId().getMoneda())){
						lblMensajeError.setValue("<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> La moneda debe ser la misma<br>");
						bValido = false;
					}
				}
				if(!bValido){
					dwMensajeError.setStyle("height: 170px; visibility: visible; width: 365px");
					dwMensajeError.setWindowState("normal");
					gvCuotas.dataBind();
					srm.addSmartRefreshId(dwMensajeError.getClientId(FacesContext.getCurrentInstance()));
					srm.addSmartRefreshId(gvCuotas.getClientId(FacesContext.getCurrentInstance()));
				}				
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	/***************************************************************************/
	/** Método: Cargar los afiliados para la caja, por moneda y compañía.
	 *  Hecho:  Carlos Manuel Hernández Morrison.
	 *  Fecha:  05/10/2010
	 ***************************************************************************/
	public List getLstAfiliados(int iCaid,String sCodcomp, String sLinea, String sMoneda){
		List Afiliados = null;
		List<SelectItem>lstPOS = new ArrayList<SelectItem>();
		Cafiliados[] cafiliado = null;
		AfiliadoCtrl afCtrl = new AfiliadoCtrl();
		String sDescrip="";
		
		try {
			cafiliado = afCtrl.obtenerAfiliadoxCaja_Compania(iCaid,sCodcomp, sLinea, sMoneda);
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
	
/*********************VENTANA DE RECIBO*************************************************/
	public void mostrarRecibo(ActionEvent ev){
		Finanhdr hFac = null;
		Finandet fd = null;
		List<Finanhdr> lstSelected = new ArrayList<Finanhdr>();
		List<Finandet> lstFacturasSelected = new ArrayList<Finandet>();
		
		Vf55ca01 caja = null;
		ReciboCtrl rpCtrl = new ReciboCtrl();
		String sUltimoRecibo = "";
		String[] sCodMod = null;
		Vf55ca012[] f55ca012 = null;
		MetodosPagoCtrl metCtrl = new MetodosPagoCtrl();
		MonedaCtrl monCtrl = new MonedaCtrl();
		AfiliadoCtrl afCtrl = new AfiliadoCtrl();
		Cafiliados[] cafiliado = null;
		CuotaCtrl cuotaCtrl = new CuotaCtrl();
		double dTasaPar = 0.0;
		Divisas divisas = new Divisas();
		BigDecimal bdInteres = BigDecimal.ZERO;
		String sMonedaBase = "";
		
		int caid = 0;
		String codcomp = ""; 
		
		String msgProceso = "";
		
		try{
			
			LogCajaService.CreateLog("FinanciamientoDAO.mostrarRecibo - INICIO", "JR8", "Inicio ejecucion Metodo");
			
			F55ca014[] f14 = (F55ca014[])m.get("cont_f55ca014");

			CodeUtil.removeFromSessionMap(new String[] { "finan_MontoAplicar",
					"Fn_CodUninegRecibo", "fn_NumeroUltimaCuotaCorriente", "esCuotaAdelantada" });
			
			chkPrincipal.setChecked(false);
			caja = (Vf55ca01) ((List) m.get("lstCajas")).get(0);
			caid = caja.getId().getCaid();	
			int iCajero = caja.getId().getCacati();
			
			//Modificado por lfonseca
			//2018-09-18
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
			List finanSeleccionados = gvCuotas.getSelectedRows() ;
			for(int i = 0; i < finanSeleccionados.size(); i++){
				RowItem row = (RowItem) finanSeleccionados.get(i);
				hFac = (Finanhdr) gvCuotas.getDataRow(row);
				lstSelected.add(hFac);

				String[] strVerificarF = vfp.verificarFactura(String.valueOf(caid), 
															  String.valueOf(hFac.getId().getCodcli()), 
															  String.valueOf(hFac.getNosol()), 
															  hFac.getTipofactura(), 
															  "", 
															  String.valueOf(iCajero),
															  vautoriz.getId().getLogin().toLowerCase(),
															  ipAddress.toUpperCase(),
															  userAgent.toUpperCase(),
															  request.getLocalAddr().toUpperCase());
				
				if(!strVerificarF[0].equals("S"))
				{
					msgProceso = "La factura  numero " + strVerificarF[2] +  
					 		", se esta procesando en la caja # " + strVerificarF[1] + ", " +
					 		"por el usuario " + strVerificarF[5] + " usando un navegador " + strVerificarF[6] +
					 		" y accediendo al servidor de caja " + strVerificarF[7];
					return;
				}
				
			}
			
			if (lstSelected == null || lstSelected.size() <= 0) {
				msgProceso = "Debe seleccionar un financiamiento para procesar.";
				return;
			}
			
			//-----------------------
			CodeUtil.putInSessionMap("lstCreditosFinan",lstSelected);
			hFac = lstSelected.get(0);
			codcomp = hFac.getId().getCodcomp() ;
			
			//&& ========== Configuracion de la compania a utilizar  
			F55ca014 dtComp = com.casapellas.controles.tmp.CompaniaCtrl.filtrarCompania( f14, codcomp);
			if (dtComp == null) {
				msgProceso = "La caja no tiene permitido procesar recibos para la compania " + codcomp;
				return;
			}
			sMonedaBase = dtComp.getId().getC4bcrcd();
			
			//buscar ultimo numero de recibo
			sUltimoRecibo = ReciboCtrl.ultimoRecioCajaCompania(caid, codcomp) + " " ;
			
			//poner datos generales en ventana
			fechaRecibo = new Date();
			txtNumrec.setValue(sUltimoRecibo);
			txtCodcli.setValue(hFac.getId().getCodcli());
			txtNomcli.setValue(hFac.getId().getNomcli());
			
			//&& =========== conservar todos los metodos de pago configurados.
			List<Vf55ca012> MetodosPagoConfigurados =  DonacionesCtrl.obtenerMpagosConfiguradosCaja(codcomp, caid);
			CodeUtil.putInSessionMap("fin_MetodosPagoConfigurados", MetodosPagoConfigurados );
			
			// cargar metodos de pago de compania de factura
			f55ca012 = metCtrl.obtenerMetodosPagoxCaja_Compania2(caid, codcomp);
			
			CodeUtil.putInSessionMap("metpago", f55ca012);
			List<SelectItem> lstMetodosPago = new ArrayList<SelectItem>();
			for (int i = 0; i < f55ca012.length; i++) {
				lstMetodosPago.add(new SelectItem(f55ca012[i].getId().getCodigo(), f55ca012[i].getId().getMpago()));
			}
			CodeUtil.putInSessionMap("lstMetodosPagoFinan",lstMetodosPago);
			ddlMetodosPago.dataBind();
			cambiarVistaMetodos(f55ca012[0].getId().getCodigo(),caja);
			
			//&& ========= cargar monedas de metodo de pago y compania de factura
			List<SelectItem> lstMoneda = new ArrayList<SelectItem>();
			sCodMod = monCtrl.obtenerMonedasxMetodosPago_Caja(caid, codcomp, ddlMetodosPago.getValue().toString());
			
			if (sCodMod != null) {
				for (String moneda : sCodMod) {
					lstMoneda.add(new SelectItem(moneda, moneda));
				}
			}
			
			CodeUtil.putInSessionMap("lstMonedaFinan", lstMoneda);
			ddlMoneda.dataBind();
						
			//&& =========  cargar afiliados
			lstAfiliado = caja.getId().getCasktpos()  == 'N'?
					getLstAfiliados(caid, codcomp, hFac.getId().getLinea()
						.trim(), ddlMoneda.getValue().toString()):
					PosCtrl.getAfiliadosSp(caid, codcomp, hFac.getId()
						.getLinea().trim(), ddlMoneda.getValue().toString());
				
			if(lstAfiliado == null)lstAfiliado = new ArrayList<SelectItem>();		
			CodeUtil.putInSessionMap("lstAfiliado", lstAfiliado);
			ddlAfiliado.dataBind();
		
			
			//&&========= tasa de cambio para clientes configurados como excepcion a tasa paralela
			
			Tpararela[] tparalelaTmp =  TasaCambioCtrl.obtenerTasaCambioParalela();
			CodeUtil.putInSessionMap("tpcambio", tparalelaTmp);
			
			String[] dtatasacte = EmpleadoCtrl.validarClienteTasaEspecial(hFac.getId().getCodcli(), valoresJDEInsFinanciamiento[0]);
			boolean clienteTasaEspecial = (dtatasacte != null && dtatasacte.length > 0 ) ;
			if(clienteTasaEspecial){
				Tpararela paralela = TasaCambioCtrl.crearTParalelaDeTOficial(((Tcambio[]) m.get("tcambio"))[0]);
				CodeUtil.putInSessionMap("tpcambio", new Tpararela[]{paralela});
			}
			
			//&&========= tasa de cambio oficial.
			Tcambio[] tcJDEs = (Tcambio[]) m.get("tcambio");
			String sTasaJDE = "";
			for (int l = 0; l < tcJDEs.length; l++) {
				sTasaJDE = sTasaJDE + " " + tcJDEs[l].getId().getCxcrdc()
						+ ": " + tcJDEs[l].getId().getCxcrrd();
			}
			tcJDE.setValue(sTasaJDE);

			//&&=========  Tasa de cambio paralela
			Tpararela[] tpcambio = null;
			tpcambio = (Tpararela[]) m.get("tpcambio");
			String sTasaParalela = "";
			for (int j = 0; j < tpcambio.length; j++) {
				sTasaParalela = sTasaParalela + " "
						+ tpcambio[j].getId().getCmond() + ": "
						+ tpcambio[j].getId().getTcambiom();
				dTasaPar = tpcambio[j].getId().getTcambiom().doubleValue();
			}
			tcParalela.setValue(sTasaParalela);

			m.remove("lstPagosFinan");
			gvMetodosPago.dataBind();
				
			//buscar cuotas vencidas 
			lstFacturasSelected = cuotaCtrl.getCuotasVencidas(lstSelected);
			
			//Variable que se utiliza para verificar si la cuota adelantada que se esta pagando
			//tiene que generado intereses corrientes. Sino no los tiene entonces se tiene
			//que pagar usando la opcion adelanto a principal.
			Boolean esCuotaAdelantada = false;
			CodeUtil.putInSessionMap("esCuotaAdelantada",false);
			
			//&& ============= buscar mora e intereses corrientes para las cuotas pendientes
			if( lstFacturasSelected != null && !lstFacturasSelected.isEmpty() ){
				
				cuotaCtrl.buscarMoraDeCuotas(lstFacturasSelected); 
				
			}else{
				
				lstFacturasSelected = new ArrayList<Finandet>();
					
				for(int i = 0;i < lstSelected.size(); i++){					
					
					hFac = (Finanhdr)lstSelected.get(i);
					
					fd = cuotaCtrl.buscarSiguienteCuota(hFac.getId().getCodcomp(), 
								hFac.getId().getCodsuc(), hFac.getId().getNosol(), 
								hFac.getId().getTiposol(), hFac.getId().getCodcli(),
								lstFacturasSelected);
					if(fd == null) {
						msgProceso = "Error al obtener la couta";
						return;
					}
					fd.setMora(BigDecimal.ZERO);
					fd.getId().setDiasmora(0);
					fd.setMontopend(fd.getId().getMontopend());
					fd.setMontoAplicar(BigDecimal.ZERO);
					
					bdInteres = cuotaCtrl.buscarInteresCorrientePend(fd);
					
					if(bdInteres.compareTo(BigDecimal.ZERO ) == -1 ){
						msgProceso = "Error al obtener los datos de intereses corriente, por favor intente nuevamente";
						return;
					}
					
					// Si no hay intereses generados con saldo en la F03B11
					// mostrar un mensaje que deberia usar la opcion pago a abono a principal
					if (bdInteres.compareTo(BigDecimal.ZERO) == 0) {
						esCuotaAdelantada = true;
						CodeUtil.putInSessionMap("esCuotaAdelantada",true);
					}
					
					if( bdInteres.compareTo(BigDecimal.ZERO) == 0 && fd.getId().getAticu() == 0 ){
						bdInteres = fd.getId().getImpuesto().add( fd.getId().getInteres() );
					}

					fd.setInteresPend(bdInteres);
					lstFacturasSelected.add(fd);
					
				}
			}
			
			
			//&& ==== Asignar monto aplicado igual al monto pendiente a las  cuotas.
			for (int i = 0; i < lstFacturasSelected.size(); i++) {
				Finandet fdTmp = (Finandet) lstFacturasSelected.get(i);
				fdTmp.setMontoAplicar(fdTmp.getMontopend());
			}
			
			
			Vautoriz vaut =  ((Vautoriz[])m.get("sevAut"))[0];
			BigDecimal tasaoficial = obtenerTasaOficial();
			String msgCreaInteres="";
			 msgCreaInteres = crearFacturasPorIntereses2( lstSelected,  lstFacturasSelected, vaut.getId().getLogin() );
			
			if( !msgCreaInteres.isEmpty() ){
				msgProceso = msgCreaInteres ;
				return ;
			}
			
			// Si no hay intereses generados con saldo en la F03B11
			// mostrar un mensaje que deberia usar la opcion pago a abono a principal
			if (esCuotaAdelantada) {
				if(fd == null) {
					msgProceso = "Error al obtener la couta";
					return;
				}
				
				// Volver a buscar los interes despues de llamar al servicio que los genera
				bdInteres = cuotaCtrl.buscarInteresCorrientePend(fd);
				
				if (bdInteres.compareTo(BigDecimal.ZERO ) == -1 || bdInteres.compareTo(BigDecimal.ZERO) == 0) {
					msgProceso = "Esta intentando hacer un pago adelantado de cuota, por favor utilize la opcion Abono a Principal";
					CodeUtil.putInSessionMap("esCuotaAdelantada",true);
					//m.remove("esCuotaAdelantada");
				}
			}

			//Buscar el detalle de cuotas en el f0311 (moratorios, corriente,saldo )
			List<Credhdr> lstComponentes = new ArrayList<Credhdr>();
			List<Credhdr> lstCompEach =  new ArrayList<Credhdr>();
			
			for(int i = 0; i < lstSelected.size();i++){					
				
				hFac = (Finanhdr)lstSelected.get(i);
				lstCompEach = cuotaCtrl.documentosPorCuota(lstFacturasSelected, hFac, f14);

				if( lstCompEach == null || lstCompEach.isEmpty() )
					continue;
				
				lstComponentes.addAll(lstCompEach);
 
			}
			
			//&& =============== buscar fecha de vencimiento de la primera cuota cuya fecha de vencimiento mayor que el dia actual
			
			List<Date> fechasVencimiento = new ArrayList<Date>();
			for (int i = 0; i < lstFacturasSelected.size(); i++) {
				Finandet fdTmp = (Finandet) lstFacturasSelected.get(i);
				
				if(fdTmp.getId().getFechapago().after(fdTmp.getId().getFechaactual() ) )
					fechasVencimiento.add(fdTmp.getId().getFechapago());
			}
			
			
			if(fechasVencimiento == null || fechasVencimiento.isEmpty() ) {
				fechasVencimiento.add( ((Finandet) lstFacturasSelected.get(0)).getId().getFechapago() ) ;
			}
			
			Collections.sort( fechasVencimiento  );
			Date fechaInicial = fechasVencimiento.get(0);
			
			
			//&& =============== buscar si el cliente tiene financimiento de seguros o de primas del bien.
			List<Credhdr> otrosFinancimentos = CuotaCtrl.buscarFinancimientosAsociados(false, 0, hFac.getId().getCodcli(), 
					String.valueOf( hFac.getNosol() ), hFac.getId().getCodcomp(), hFac.getId().getCodsuc(), f14, fechaInicial) ;
			
			if(otrosFinancimentos != null && !otrosFinancimentos.isEmpty() ){
				lstComponentes.addAll(otrosFinancimentos);
			}
			
			
			//&& =============== ordenar la lista de documentos, por cuota y luego por fecha de vencimiento.
			Collections.sort(lstComponentes, new Comparator<Credhdr>() {
				public int compare(Credhdr c1, Credhdr c2) {
					 
					 int compare =  c1.getId().getFechavenc().compareTo( c2.getId().getFechavenc() );
					 
					 if(compare == 0){
						 
						 int partida1 =  Integer.parseInt( c1.getId().getPartida() ) ;
						 int partida2 =  Integer.parseInt( c2.getId().getPartida() ) ;
						 
						 compare = (partida1 > partida2) ? 1 : (partida1 < partida2) ? -1 : 0 ;
					 }
					
					return compare;
				}

			});
			 
			CodeUtil.putInSessionMap("lstComponentes",lstComponentes);

			CodeUtil.putInSessionMap("lstSelectedCuotas",lstFacturasSelected);
			gvSelectedCuotas.dataBind();
		
			//&& =============== Si no se obtuvieron los datos de la cuota actual, no mostrar la ventana del recibo.
			if( lstComponentes == null || lstComponentes.isEmpty() ||  lstFacturasSelected == null || lstFacturasSelected.isEmpty() ){
				//msgProceso = " No se pudieron obtener documentos por cobrar para la cuota del cliente ";
				msgProceso = " No se pudieron obtener factura de CxC de la cuota " + lstFacturasSelected.get(0).getId().getNocuota() + " asociada al plan de financiamiento de este cliente, "
						   + "favor verificar en E1. "; 
				return;
			}
			
			
			//calcular total y faltante
			double dMontoTotalAplicarDomestico = 0.0;
			double dMontoTotalAplicarForaneo = 0.0;
			double dMontoAplicar = 0.0;
			//
			//calcular monto a aplicar
			Credhdr[] hFacSelected = new Credhdr[lstComponentes.size()];
			for(int i = 0; i < lstComponentes.size(); i++){
				hFacSelected[i] = (Credhdr)lstComponentes.get(i);
				if (hFac.getMoneda().equals(sMonedaBase)){
					dMontoAplicar = dMontoAplicar + hFacSelected[i].getId().getCpendiente().doubleValue();
					dMontoTotalAplicarDomestico = dMontoTotalAplicarDomestico + hFacSelected[i].getId().getCpendiente().doubleValue();
				}else{
					dMontoAplicar = dMontoAplicar + hFacSelected[i].getId().getDpendiente().doubleValue();
					dMontoTotalAplicarForaneo = dMontoTotalAplicarForaneo + hFacSelected[i].getId().getDpendiente().doubleValue();
					dMontoTotalAplicarDomestico = dMontoTotalAplicarDomestico + (hFacSelected[i].getId().getDpendiente().doubleValue()*dTasaPar);
				}
			}
			
			lblMontoAplicar.setValue("A Aplicar " + hFac.getMoneda() +":");
			txtMontoAplicar.setValue("0.00");
			lblMontoRecibido.setValue("Recibido "+ hFac.getMoneda() +":");
			txtMontoRecibido.setValue("0.00");
			lblCambio.setValue("Cambio " + hFac.getMoneda() +":");		
			txtCambio.setValue("0.00");
			txtCambio.setStyle("font-size: 10pt; color: red");
			txtCambioForaneo.setStyleClass("frmInputHidden");
			lblCambioDomestico.setValue("");
			txtCambioDomestico.setValue("");
			lnkCambio.setStyle("display: none");
				
			if(!hFac.getId().getMoneda().equals(sMonedaBase)){//resumen de moneda foranea
				montoTotalAplicarForaneo.setStyle("display:inline");
				montoTotalFaltanteForaneo.setStyle("display:inline");
				montoTotalAplicarForaneo.setValue("<B>" + hFac.getMoneda() + "</B>" +" "+ divisas.formatDouble(dMontoTotalAplicarForaneo));
				montoTotalFaltanteForaneo.setValue("<B>" + hFac.getMoneda() + "</B>" +" "+ divisas.formatDouble(dMontoTotalAplicarForaneo));
				montoTotalAplicarDomestico.setValue("<B>"+sMonedaBase+"</B> " + divisas.formatDouble(dMontoTotalAplicarDomestico));
				montoTotalFaltanteDomestico.setValue("<B>"+sMonedaBase+"</B> " + divisas.formatDouble(dMontoTotalAplicarDomestico));
			}else{//resumen de moneda domestica
				montoTotalAplicarForaneo.setStyle("display:none");
				montoTotalFaltanteForaneo.setStyle("display:none");
				montoTotalAplicarDomestico.setValue("<B>"+sMonedaBase+"</B> " + divisas.formatDouble(dMontoTotalAplicarDomestico));
				montoTotalFaltanteDomestico.setValue("<B>"+sMonedaBase+"</B> " + divisas.formatDouble(dMontoTotalAplicarDomestico));
			}
			intSelectedDet.setValue(lstComponentes.size());
				
			//limpiar campos del recibo
			txtConcepto.setValue("");
			txtMonto.setValue("");
			txtReferencia1.setValue("");
			txtReferencia2.setValue("");
			txtReferencia3.setValue("");			
			lblNumrecm.setValue("");
			txtNumrecm.setStyle("display:none");
			dcFecham.setStyle("display:none");
			
			txtNumrec.setStyle("display:inline");
			ddlTipoRecibo.dataBind();
			
			dwRecibo.setWindowState("normal");
			dwCargando.setWindowState("hidden");

			LogCajaService.CreateLog("FinanciamientoDAO.mostrarRecibo - FIN", "JR8", "Fin ejecucion Metodo");
		}catch(Exception ex){
			LogCajaService.CreateLog("FinanciamientoDAO.mostrarRecibo", "ERR", ex.getMessage());
		}finally{
							
			if( !msgProceso.isEmpty()){
				lblMensajeError.setValue(msgProceso);
				dwMensajeError.setWindowState("normal");
				dwMensajeError.setStyle("width:390px;height:145px");
				CodeUtil.refreshIgObjects(dwMensajeError);
			}
			
			HibernateUtilPruebaCn.closeSession();
		}
	}
	public void cerrarRecibo(ActionEvent ev){
		dwRecibo.setWindowState("hidden");
	}
/***********************************************************************************************************************************/	
	public void setSelectedCuotas(SelectedRowsChangeEvent e) {
		List lstSelectedCuotas = new ArrayList(),lstCuotas = new ArrayList(), lstCuotasActuales = new ArrayList();
		RowItem row = null;
		Hfinan cuotaSelected = null,cuotaFirst = null, cuotaActual = null;
		boolean valido = true;
		try{
			//obtener cuotas buscadas
			lstCuotasActuales = (List)m.get("lstCuotas");
			SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
			lstSelectedCuotas = gvCuotas.getSelectedRows();
			
			//validar los dias vencidos de la q se esta seleccionando contra todas las demas 
			//marcar las cuotas seleccionadas
			for (int j = 0; j < lstSelectedCuotas.size(); j ++){
				row = (RowItem) lstSelectedCuotas.get(j);
				cuotaSelected = (Hfinan) gvCuotas.getDataRow(row);
				for(int k = 0; k < lstCuotasActuales.size(); k++){
					cuotaActual = (Hfinan)lstCuotasActuales.get(k);
					if(cuotaSelected.getId().getNosol() == cuotaActual.getId().getNosol() && cuotaSelected.getId().getCodcli() == cuotaActual.getId().getCodcli() && cuotaSelected.getId().getCuota().equals(cuotaActual.getId().getCuota()) && cuotaActual.getId().getCodcomp().equals(cuotaSelected.getId().getCodcomp())){
						cuotaActual.setSelected(true);
						lstCuotasActuales.set(k, cuotaActual);
						break;
					}
				}
			}
			
			//comparar las selecccionadas contra las actuales
			for(int h  = 0 ; h < lstSelectedCuotas.size(); h++){
				RowItem ri = (RowItem)lstSelectedCuotas.get(h); 
				cuotaFirst = (Hfinan) gvCuotas.getDataRow(row);		
				for(int n = 0; n < lstCuotasActuales.size(); n++){
					cuotaActual = (Hfinan)lstCuotasActuales.get(n);
					if(!cuotaActual.isSelected() && cuotaActual.getId().getNosol() == cuotaFirst.getId().getNosol() && cuotaFirst.getId().getCodcli() == cuotaActual.getId().getCodcli() && cuotaActual.getId().getAtdven() > cuotaFirst.getId().getAtdven()){
						valido = false;
						break;
					}
					/*else if(!cuotaActual.isSelected() && cuotaActual.getId().getNosol() == cuotaFirst.getId().getNosol() && cuotaFirst.getId().getCodcli() == cuotaActual.getId().getCodcli() && cuotaActual.getId().getRpddj() > cuotaFirst.getId().getRpddj()){
						valido = false;
						break;
					}*/
				}
				if(!valido)break;
			}
			
			if(valido){
				//validar seleccionadas
				if (lstSelectedCuotas.size() > 1) {// validar facturas
					row = (RowItem) lstSelectedCuotas.get(0);
					cuotaFirst = (Hfinan) gvCuotas.getDataRow(row);
					lstCuotas.add(cuotaFirst);
					for (int i = 1; i < lstSelectedCuotas.size(); i++) {
						row = (RowItem) lstSelectedCuotas.get(i);
						cuotaSelected = (Hfinan) gvCuotas.getDataRow(row);
						// validar cliente
						if (cuotaFirst.getId().getCodcli() != cuotaSelected.getId().getCodcli() || !cuotaFirst.getNomcli().trim().equals(cuotaSelected.getNomcli().trim())) {
							valido = false;
						}	
						// validar moneda
						else if (!cuotaFirst.getMoneda().equals(cuotaSelected.getMoneda())) {
							valido = false;
						}
						//validar compania
						else if(!cuotaFirst.getId().getCodcomp().equals(cuotaSelected.getId().getCodcomp())){
							valido = false;
						}
						//validar si hay cuotas vencidas para el cliente en el mismo plan
				
					}
					if(valido){
						lstCuotas.add(cuotaSelected);
					}
				} else if (lstSelectedCuotas.size() == 1) {//si solo hay una seleccionada
					row = (RowItem) lstSelectedCuotas.get(0);
					cuotaFirst = (Hfinan) gvCuotas.getDataRow(row);
					if(valido){
						lstCuotas.add(cuotaFirst);
					}
				}//fin si solo hay una seleccionada
			}
			
			
			if (valido) {
				m.put("lstSelectedCuotas", lstCuotas);
			} else {
				
				lblMensajeError.setValue("<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El cliente de la factura debe ser el mismo<br>"
									+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> La moneda de la factura debe ser la misma<br>"
									+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> La Compañia de la factura debe ser la misma<br>"
									+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Debe procesar cuotas vencidas primero<br>");
				dwMensajeError.setStyle("height: 170px; visibility: visible; width: 365px");
			
				lstCuotas = new ArrayList();
				m.put("lstSelectedCuotas", lstCuotas);
				dwMensajeError.setWindowState("normal");
				
				//quitar seleccion de todas
				for(int k = 0; k < lstCuotasActuales.size(); k++){
					cuotaActual = (Hfinan)lstCuotasActuales.get(k);
					cuotaActual.setSelected(false);
					lstCuotasActuales.set(k, cuotaActual);
				}
				gvCuotas.dataBind();
				srm.addSmartRefreshId(dwMensajeError.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(gvCuotas.getClientId(FacesContext.getCurrentInstance()));
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public void cerrarMensajeError(ActionEvent e){
		dwMensajeError.setWindowState("hidden");
	}
	/**************************************************************************************/
/**************DETALLE DE SOLICITUD***************************************************************************/	
	public void onMonedaDetChange(ValueChangeEvent ev){
		Elementofin el = null;
		Producto p = null;
		Finandet f = null;
		BigDecimal dMora = BigDecimal.ZERO;
		try{		
			Finanhdr cuota = (Finanhdr)m.get("cuota");
			String sMoneda = hddMonedaDetalle.getValue().toString();
			lstProductos = (List)m.get("lstProductos");
			lstDetallesol = (List)m.get("lstDetallesol");
			if (sMoneda.equals("COR")){
				for(int i = 0; i < lstProductos.size();i++){
					p = (Producto)lstProductos.get(i);
					p.getId().setPrecioun(p.getId().getR2uprc());
					lstProductos.set(i, p);
				}
				//cambiar detalle de solicitud
				for(int j = 0; j < lstDetallesol.size();j++){
					f = (Finandet)lstDetallesol.get(j);					
					f.getId().setPrincipal(f.getId().getAtprid());
					f.getId().setInteres(f.getId().getAtintd());
					f.getId().setImpuesto(f.getId().getAtstam());
					f.getId().setMonto(f.getId().getAtag());
					f.getId().setMontopend(f.getId().getAtaap());
					f.setMora(f.getMora().multiply(f.getId().getTasa()));
					dMora = dMora.add(f.getMora());
					f.setMontopend(f.getMontopend().multiply(f.getId().getTasa()));
					lstDetallesol.set(j, f);
				}
				//cambiar resumen de solicitud		
				txtPrincipalDet = cuota.getId().getAtprid();
				txtInteresDet = cuota.getId().getAtintd();
				txtImpuestoDet = cuota.getId().getAtstam();
				txtTotalDet = cuota.getId().getMonto().multiply(f.getId().getTasa());
				txtTotalMoraDet = dMora;
				txtPendienteDet = cuota.getMontopend().multiply(f.getId().getTasa());
			}else{
				for(int i = 0; i < lstProductos.size();i++){
					p = (Producto)lstProductos.get(i);
					p.getId().setPrecioun(p.getId().getR2fup());
					lstProductos.set(i, p);										
				}
				//cambiar detalle de solicitud
				for(int j = 0; j < lstDetallesol.size();j++){
					f = (Finandet)lstDetallesol.get(j);					
					f.getId().setPrincipal(f.getId().getAtprif());
					f.getId().setInteres(f.getId().getAtintf());
					f.getId().setImpuesto(f.getId().getAtctam());
					f.getId().setMonto(f.getId().getAtacr());
					f.getId().setMontopend(f.getId().getAtaap());
					f.setMora(new BigDecimal(f.getMora().doubleValue()/(f.getId().getTasa().doubleValue())));
					dMora = dMora.add(f.getMora());
					f.setMontopend(new BigDecimal(f.getMontopend().doubleValue()/(f.getId().getTasa().doubleValue())));
					lstDetallesol.set(j, f);
				}
				//cambiar resumen de solicitud	
				txtPrincipalDet = cuota.getId().getAtprif();
				txtInteresDet = cuota.getId().getAtintf();
				txtImpuestoDet = cuota.getId().getAtctam();
				txtTotalDet = new BigDecimal(cuota.getId().getMonto().doubleValue()/(f.getId().getTasa().doubleValue()));
				txtTotalMoraDet = dMora;
				txtPendienteDet = new BigDecimal( cuota.getMontopend().doubleValue()/(f.getId().getTasa().doubleValue()) );
			}		
			m.put("lstDetallesol", lstDetallesol);
			m.put("lstProductos",lstProductos);
			gvProductos.dataBind();
			gvDetallesol.dataBind();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
/************************************************************************************/
	public void cargarDetalleCuota(Finanhdr cuota, boolean bEnDetalle){
		CuotaCtrl cuotaCtrl = new CuotaCtrl();	
		Finandet f = null;
		BigDecimal dMora = BigDecimal.ZERO;
		List lstNewdet = new ArrayList(),lstDetallesol;
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		String sMonedaBase = "";
	
		boolean nuevaConexion = true;
		
		try{
			
			ConsolidadoDepositosBcoCtrl.getCurrentSession();
			nuevaConexion = ConsolidadoDepositosBcoCtrl.isNewSession;
			 
			sMonedaBase = cCtrl.sacarMonedaBase((F55ca014[])m.get("cont_f55ca014"), cuota.getId().getCodcomp());
			 
			fechaDet = cuota.getFecha();
			
			codcliDet = cuota.getId().getCodcli() + " (Código)";
			nomcliDet = cuota.getId().getNomcli() + " (Nombre)";
			
			noSolDet = cuota.getId().getNosol() + "";
			tipoSolDet = cuota.getId().getTiposol();
			
			tasaDet = cuota.getId().getTasa();
	
			lstMonedaDetalle = new ArrayList();
			if(cuota.getId().getMoneda().equals(sMonedaBase)){
				
				monedaDet = cuota.getId().getMoneda();
				hddMonedaDetalle.setStyle("display:none");
			}else{
				
				lstMonedaDetalle.add(new SelectItem(cuota.getId().getMoneda(),cuota.getId().getMoneda()));
				lstMonedaDetalle.add(new SelectItem(sMonedaBase,sMonedaBase));
				hddMonedaDetalle.dataBind();
				monedaDet = "";
			}		
			m.put("lstMonedaDetalle",lstMonedaDetalle);
			
			//leer el detalle de productos de la solicitud
			lstProductos = cuotaCtrl.leerProductosxSolicitud(cuota.getId().getCodsuc(), cuota.getId().getNosol(), cuota.getId().getTiposol(), cuota.getId().getCodcli());
			m.put("lstProductos",lstProductos);
			gvProductos.dataBind();
			
			lstDetallesol = cuotaCtrl.getDetalleSolicitud(cuota.getId().getCodcomp(), cuota.getId().getCodsuc(), cuota.getId().getNosol(), cuota.getId().getTiposol(), cuota.getId().getCodcli());
			for(int i = 0; i < lstDetallesol.size();i++){
				f = (Finandet)lstDetallesol.get(i);
				f = cuotaCtrl.buscarMoraExistente(f);	
				dMora = dMora.add(f.getMora());
				lstNewdet.add(f);
			}
			m.put("lstDetallesol",lstNewdet);
			gvDetallesol.dataBind();		
			
			//poner resumen de pago
			txtPrincipalDet = cuota.getId().getPrincipal();
			txtInteresDet = cuota.getId().getInteres();
			txtImpuestoDet = cuota.getId().getImpuesto();
			txtTotalDet = cuota.getId().getMonto();
			txtTotalMoraDet = dMora;
			txtPendienteDet = cuota.getMontopend();
			
			
			//&& =============== buscar si el financimiento tiene otros financimientos asociados.
			F55ca014[] f14 = (F55ca014[])m.get("cont_f55ca014");
			lstDetalleExtraFinanciamientos = CuotaCtrl.buscarFinancimientosAsociados(cuota.getId().getCodcli(), cuota.getId().getCodcomp(), String.valueOf(cuota.getId().getNosol()), cuota.getId().getCodsuc(), f14);
			
			if(lstDetalleExtraFinanciamientos == null ){
				lstDetalleExtraFinanciamientos = new ArrayList<Credhdr>();
			}
			
			boolean renderpanel = !lstDetalleExtraFinanciamientos.isEmpty();
			
			String dimensiones = "height: 550px; width: 650px";
			String factura ="";
			if(renderpanel){
				dimensiones = "height: 630px; width: 670px";
				factura = lstDetalleExtraFinanciamientos.get(0).getId().getNofactura() + " > "  + lstDetalleExtraFinanciamientos.get(0).getId().getTipofactura();
			}
			
			CodeUtil.putInSessionMap("fin_lstDetalleExtraFinanciamientos", lstDetalleExtraFinanciamientos);
			gvDetalleExtraFinanciamientos.dataBind();
			pnlDatosOtrosFinancimientos.setRendered(renderpanel);
			lblFacturaOtrosFinan.setValue(factura);
			
			
			dwDetalleSolicitud.setStyle(dimensiones);
			dwDetalleSolicitud.setWindowState("normal");
			
		}catch(Exception ex){
			// ex.printStackTrace();
		}finally{
			
			ConsolidadoDepositosBcoCtrl.isNewSession = nuevaConexion;
			ConsolidadoDepositosBcoCtrl.closeCurrentSession(true);
		}
	}
	/*******************************************************************************/
	public void mostrarDetalleSolicitud(ActionEvent e){
		Finanhdr cuota = null;
		try{
			RowItem ri = (RowItem) e.getComponent().getParent().getParent();
			cuota = (Finanhdr) gvCuotas.getDataRow(ri);
			CodeUtil.putInSessionMap("cuota", cuota);
			cargarDetalleCuota(cuota,false);
			
		}catch(Exception ex){
			ex.printStackTrace(); 
		}
	}
	/*********************************************************************************/
	public void cerrarDetalleSolicitud(ActionEvent e){
		m.remove("cuota");
		dwDetalleSolicitud.setWindowState("hidden");
	}
	/**********************************************/
	public void mostrarDetalleEnRecibo(ActionEvent e){
		Finandet cuota = null;
		try{
			RowItem ri = (RowItem) e.getComponent().getParent().getParent();
			cuota = (Finandet) gvSelectedCuotas.getDataRow(ri);
			m.put("cuota", cuota);
			//cargarDetalleCuota(cuota,true);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
/********************************************************************************************************************/
 /* ****************FIN DE DETALLE DE SOLICITUD *******************************************************/
/******************************************************************************************************************/
/********************************************************************************************************/
	public void ponerTasaSegunFecha(ValueChangeEvent ev) {
		Date dFecha = null;
		FechasUtil fecUtil = new FechasUtil();
		int iFecha = 0;
		TasaCambioCtrl tcCtrl = new TasaCambioCtrl();
		Tcambio[] tcambio = null;
		String sTasa = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			dFecha = (Date) dcFecham.getValue();
			String sFecha = sdf.format(dFecha);
			tcambio = tcCtrl.obtenerTasaCambioJDExFecha(sFecha);

			// poner tasa de cambio de JDE
			for (int l = 0; l < tcambio.length; l++) {
				sTasa = sTasa + " " + tcambio[l].getId().getCxcrdc() + ": "+ tcambio[l].getId().getCxcrrd();
			}
			tcJDE.setValue(sTasa);
			m.put("tcambio", tcambio);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

/********************************************************************************************************/
/*******************CAMBIAR VISTAS DE METODOS DE PAGO***************************************************/
	public void cambiarVistaMetodos(String sCodMetodo, Vf55ca01 vf01) {
		
		try {

			lbletVouchermanual.setStyle("visibility:hidden; display:none");
			chkVoucherManual.setChecked(false);
			chkVoucherManual.setStyle("width:20px; visibility:hidden; display:none");
			ddlTipoMarcasTarjetas.setStyle("display:none");
			lblMarcaTarjeta.setStyle("display:none;");
			
			
			// metodo = TC
			if (sCodMetodo.equals(MetodosPagoCtrl.TARJETA)) {
				// Set to blank
				lblAfiliado.setValue("Afiliado:");
				lblReferencia1.setValue("Identificación:");
				lblReferencia2.setValue("Voucher:");
				lblReferencia3.setValue("");
				lblBanco.setValue("");
				chkVoucherManual.setChecked(false);

				// Set to visible
				ddlAfiliado.setStyle("display:inline;width: 154px");
				ddlBanco.setStyle("display:none");
				txtReferencia1.setStyle("display:inline");
				txtReferencia2.setStyle("display:inline");
				txtReferencia3.setStyle("visibility:hidden; display:none");
				lbletVouchermanual.setValue("V. Manual");
				lbletVouchermanual.setStyle("visibility:visible; display:inline");
				chkVoucherManual.setStyle("width:20px; visibility:visible; display:inline");
				
				ddlTipoMarcasTarjetas.dataBind();
				ddlTipoMarcasTarjetas.setStyle("width: 154px; display:inline");
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
			} else if (sCodMetodo.equals(MetodosPagoCtrl.CHEQUE)) {

				// Set to blank
				lblAfiliado.setValue("");
				lblReferencia1.setValue("No. Cheque:");
				lblReferencia2.setValue("Emisor:");
				lblReferencia3.setValue("Portador:");
				lblBanco.setValue("Banco:");

				// Set to visible
				ddlAfiliado.setStyle("visibility:hidden; display:none");
				ddlBanco.setStyle("display:inline;width: 154px");
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
			} else if (sCodMetodo.equals(MetodosPagoCtrl.EFECTIVO)) {

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
				ddlBanco.setStyle("display:inline; width: 154px");
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

				// Set to visible
				ddlAfiliado.setStyle("display:none");
				ddlBanco.setStyle("display:inline; width: 154px");
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

	/** **************************************************************************************/
	/****************************************************************************/
	public void onMonedaChange(ValueChangeEvent ev){
		Finandet hFac = null;
		List lstFacturasSelected = null,lstFacturasSelectedH = null;
		Vf55ca01 caja = null;
		Vf55ca012[] f55ca012 = null;
		MetodosPagoCtrl metCtrl = new MetodosPagoCtrl();
		Cafiliados[] cafiliado = null;
		AfiliadoCtrl afCtrl = new AfiliadoCtrl();
		try{
			caja = (Vf55ca01) ((List) m.get("lstCajas")).get(0);
			lstFacturasSelected = (List) m.get("lstSelectedCuotas");
			lstFacturasSelectedH = (List)m.get("lstCreditosFinan");
			
			
			if(lstFacturasSelected == null || lstFacturasSelected.isEmpty() )
				return;
			
			hFac = (Finandet) lstFacturasSelected.get(0);

			
			f55ca012 = metCtrl.obtenerMetodoPagoxCaja_Moneda2(caja.getId()
					.getCaid(), hFac.getId().getCodcomp(), ddlMoneda.getValue()
					.toString());
			List lstMetodosPago = new ArrayList();
			for (int i = 0; i < f55ca012.length; i++) {
				lstMetodosPago.add(new SelectItem(f55ca012[i].getId()
						.getCodigo(), f55ca012[i].getId().getMpago()));
			}
			m.put("lstMetodosPagoFinan", lstMetodosPago);

			String sLineaFactura =  (((Finanhdr)lstFacturasSelectedH.get(0)).getId().getLinea().trim());
			
			//&& =========  cargar afiliados
			lstAfiliado = caja.getId().getCasktpos() == 'N' ? 
					getLstAfiliados( caja.getId().getCaid(), hFac.getId()
							.getCodcomp(), sLineaFactura, ddlMoneda.getValue()
							.toString()) :
					PosCtrl.getAfiliadosSp(caja.getId().getCaid(), hFac.getId()
							.getCodcomp(), sLineaFactura, ddlMoneda.getValue()
							.toString());
				
			if(lstAfiliado == null)lstAfiliado = new ArrayList<SelectItem>();		
			m.put("lstAfiliado", lstAfiliado);
			ddlAfiliado.dataBind();
			
		}catch(Exception ex){ 
		}
	}
	/****************************************************************************/
	public void onMetodosPagoChange(ValueChangeEvent ev){
		Finandet hFac = null;
		List lstFacturasSelected = null;
		MonedaCtrl monCtrl = new MonedaCtrl();
		String[] sCodMod = null;
		Vf55ca01 f55ca01 = null;
		try{
			List lstMoneda = new ArrayList();
			f55ca01 = (Vf55ca01) ((List) m.get("lstCajas")).get(0);
			lstFacturasSelected = (List) m.get("lstSelectedCuotas");
			
			if (f55ca01 != null && lstFacturasSelected != null) {
				hFac = (Finandet) lstFacturasSelected.get(0);
				// cargar monedas de metodo de pago y compania de factura
				sCodMod = monCtrl.obtenerMonedasxMetodosPago_Caja(f55ca01.getId().getCaid(),hFac.getId().getCodcomp(), ddlMetodosPago.getValue().toString());
				
				if (sCodMod != null) {
					for (int i = 0; i < sCodMod.length; i++) {
						lstMoneda.add(new SelectItem(sCodMod[i], sCodMod[i]));
					}
				}
			}
			
			m.put("lstMonedaFinan", lstMoneda);
			ddlMoneda.dataBind();
			cambiarVistaMetodos(ddlMetodosPago.getValue().toString(),f55ca01);
		}catch(Exception ex){
			LogCajaService.CreateLog("onMetodosPagoChange", "ERR", ex.getMessage());
		}
	}
/******************************************************************************************/
/*****************************************************************************************/	
	public void onTipoReciboChange(ValueChangeEvent ev){
		String sTipoRecibo = "";
		TasaCambioCtrl tcCtrl = new TasaCambioCtrl();
		Tcambio[] tcambio = null;
		try{
			sTipoRecibo = ddlTipoRecibo.getValue().toString();
			if(sTipoRecibo.equals("01")){//AUTOMATICO
				fechaRecibo=new Date();
				dcFecham.setStyle("display:none");
				lblNumrecm.setValue("");
				txtNumrecm.setStyle("display:none");
				tcambio = tcCtrl.obtenerTasaCambioJDEdelDia();
				m.put("tcambio", tcambio);
				
				String sTasaJDE = "";
				for (int l = 0; l < tcambio.length; l++) {
					sTasaJDE = sTasaJDE + " " + tcambio[l].getId().getCxcrdc() + ": " + tcambio[l].getId().getCxcrrd();
				}
				tcJDE.setValue(sTasaJDE);
			}else{//MANUAL
				dcFecham.setStyleClass("dateChooserSyleClass1");
				dcFecham.setStyle("display:inline; margin-left: 2px; width: 100px;");
				dcFecham.setValue(new Date());
				lblNumrecm.setValue("No. Manual:");
				txtNumrecm.setStyle("display:inline");
				
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	
/*******************BUSQUEDA DE SOLICITUDES*****************************************/
	public void refrescarVista(ActionEvent ev){
		try{
			CuotaCtrl cuotaCtrl = new CuotaCtrl();
			lstCuotas = new ArrayList();
			lstCuotas = cuotaCtrl.getAllCuotas();
			m.put("lstCuotas", lstCuotas);
			if(lstCuotas.isEmpty() || lstCuotas == null){
				strMensajeBusqueda = "No se encontraron resultados!!!";
			}		
			gvCuotas.dataBind();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public void buscar(){
		CuotaCtrl cuotaCtrl = new CuotaCtrl();
		int iTipo = 1;
		String sParametro = "",sCodcomp,sMoneda;
		strMensajeBusqueda = "";
		
		try{
			iTipo = Integer.parseInt(hddTiposBusqueda.getValue().toString().trim());
			sParametro = txtParametro.getValue().toString().trim();
			sCodcomp = hddFiltroCompania.getValue().toString().trim();
			sMoneda = hddFiltroMoneda.getValue().toString().trim();
			
			LogCajaService.CreateLog("FinanciamientoDAO.buscar - INICIO", "JR8", "Inicio ejecucion Metodo");
			
			lstCuotas = cuotaCtrl.buscarFinancimiento(iTipo,sParametro,sCodcomp,sMoneda);
			
			LogCajaService.CreateLog("FinanciamientoDAO.buscar - FIN", "JR8", "Fin ejecucion Metodo");
			
			if( lstCuotas == null ||lstCuotas.isEmpty()){
				strMensajeBusqueda = "No se encontraron resultados!!!";
			}			
			m.put("lstCuotas", lstCuotas);
			gvCuotas.dataBind();
			dwCargando.setWindowState("hidden")
			;
		}catch(Exception ex){
			LogCajaService.CreateLog("buscar", "ERR", ex.getMessage());
		} finally {
			HibernateUtilPruebaCn.closeSession();
		}
	}
	public void realizarBusqueda(ActionEvent ev){
		buscar();
	}
	public void onFiltroChangeComp(ValueChangeEvent ev){
		//buscar();
	}
	public void onFiltroChangeMoneda(ValueChangeEvent ev){
		//buscar();
	}
	public void setBusqueda(ValueChangeEvent ev){
		String strBusqueda = hddTiposBusqueda.getValue().toString();
		m.put("strBusquedaFinan", strBusqueda);
	}
/*******************************************************************/
/*********GETTERS Y SETTERS DE COMPONENTES Y PROPIEDADES**********/	
	
	public HtmlGridView getGvCuotas() {
		return gvCuotas;
	}

	public List getLstProductos() {
		if(m.get("lstProductos")== null){
			lstProductos = new ArrayList();
		}else{
			lstProductos = (List)m.get("lstProductos");
		}
		return lstProductos;
}
public void setLstProductos(List lstProductos) {
	this.lstProductos = lstProductos;
}
public HtmlGridView getGvProductos() {
	return gvProductos;
}
public void setGvProductos(HtmlGridView gvProductos) {
	this.gvProductos = gvProductos;
}
	public HtmlDialogWindow getDwDetalleSolicitud() {
	return dwDetalleSolicitud;
}

public void setDwDetalleSolicitud(HtmlDialogWindow dwDetalleSolicitud) {
	this.dwDetalleSolicitud = dwDetalleSolicitud;
}

	public void setGvCuotas(HtmlGridView gvCuotas) {
		this.gvCuotas = gvCuotas;
	}

	public List getLstCuotas() {
		if(m.get("lstCuotas") == null){
			CuotaCtrl cuotaCtrl = new CuotaCtrl();
			lstCuotas = new ArrayList();
			//lstCuotas = cuotaCtrl.getAllCuotas();
			m.put("lstCuotas", lstCuotas);
		}else {
			lstCuotas = (List)m.get("lstCuotas");
		}
		return lstCuotas;
	}

	public void setLstCuotas(List lstCuotas) {
		this.lstCuotas = lstCuotas;
	}

	public List getLstTiposBusqueda() {
		if(lstTiposBusqueda == null){
			lstTiposBusqueda = new ArrayList();
			lstTiposBusqueda.add(new SelectItem("1","Nombre Cliente","Búsqueda por nombre de cliente"));
			lstTiposBusqueda.add(new SelectItem("2","Código Cliente","Búsqueda por código de cliente"));
			lstTiposBusqueda.add(new SelectItem("3","No. Solicitud","Búsqueda por número de solicitud"));
			//lstTiposBusqueda.add(new SelectItem("4","No. Factura","Búsqueda por número de factura"));
		}
		return lstTiposBusqueda;
	}

	public void setLstTiposBusqueda(List lstTiposBusqueda) {
		this.lstTiposBusqueda = lstTiposBusqueda;
	}

	public List getLstFiltroMonedas() {
		if(lstFiltroMonedas == null){
			lstFiltroMonedas = new ArrayList();
			lstFiltroMonedas.add(new SelectItem("01","Todas","Todas las monedas"));
			lstFiltroMonedas.add(new SelectItem("COR","COR",""));
			lstFiltroMonedas.add(new SelectItem("USD","USD",""));
		}
		return lstFiltroMonedas;
	}

	public void setLstFiltroMonedas(List lstFiltroMonedas) {
		this.lstFiltroMonedas = lstFiltroMonedas;
	}

	public List getLstFiltroCompanias() {
		
		if(m.get("finan_lstFiltroCompanias") == null) {
			lstFiltroCompanias = new ArrayList();
			List lstCajas = (List)m.get("lstCajas");
			List<SelectItem>lstComp = new ArrayList<SelectItem>();
			
			lstFiltroCompanias.add(new SelectItem("01","Todas","Todas las Compañias"));
			
			F55ca014[] f55ca014  = new CompaniaCtrl()
										.obtenerCompaniasxCaja(((Vf55ca01)lstCajas.get(0)).getId().getCaid());
			if(f55ca014!=null && f55ca014.length>0){
				for (F55ca014 f14 : f55ca014) {
					SelectItem si = new SelectItem();
					si.setValue(f14.getId().getC4rp01().trim());
					si.setLabel(f14.getId().getC4rp01d1().trim());
					si.setDescription("Compañía: "+f14.getId().getC4rp01().trim()+" "+f14.getId().getC4rp01d1().trim());
					lstComp.add(si);
				}
			}
			lstFiltroCompanias.addAll(lstComp);
			m.put("finan_F55ca014", f55ca014);
			m.put("finan_lstFiltroCompanias",lstFiltroCompanias);
		
		} else {
			lstFiltroCompanias = (List)m.get("finan_lstFiltroCompanias");
		}
		return lstFiltroCompanias;
	}
	public void setLstFiltroCompanias(List lstFiltroCompanias) {
		this.lstFiltroCompanias = lstFiltroCompanias;
	}

	public HtmlDropDownList getHddTiposBusqueda() {
		return hddTiposBusqueda;
	}

	public void setHddTiposBusqueda(HtmlDropDownList hddTiposBusqueda) {
		this.hddTiposBusqueda = hddTiposBusqueda;
	}

	public HtmlInputText getTxtParametro() {
		return txtParametro;
	}

	public void setTxtParametro(HtmlInputText txtParametro) {
		this.txtParametro = txtParametro;
	}
	public String getStrMensajeBusqueda() {
		return strMensajeBusqueda;
	}
	public void setStrMensajeBusqueda(String strMensajeBusqueda) {
		this.strMensajeBusqueda = strMensajeBusqueda;
	}
	public HtmlDropDownList getHddFiltroCompania() {
		return hddFiltroCompania;
	}
	public void setHddFiltroCompania(HtmlDropDownList hddFiltroCompania) {
		this.hddFiltroCompania = hddFiltroCompania;
	}
	public HtmlDropDownList getHddFiltroMoneda() {
		return hddFiltroMoneda;
	}
	public void setHddFiltroMoneda(HtmlDropDownList hddFiltroMoneda) {
		this.hddFiltroMoneda = hddFiltroMoneda;
	}
	public Date getFechaDet() {
		return fechaDet;
	}
	public void setFechaDet(Date fechaDet) {
		this.fechaDet = fechaDet;
	}
	public String getCodcliDet() {
		return codcliDet;
	}
	public void setCodcliDet(String codcliDet) {
		this.codcliDet = codcliDet;
	}
	public String getNomcliDet() {
		return nomcliDet;
	}
	public void setNomcliDet(String nomcliDet) {
		this.nomcliDet = nomcliDet;
	}
	public String getMonedaDet() {
		return monedaDet;
	}
	public void setMonedaDet(String monedaDet) {
		this.monedaDet = monedaDet;
	}
	public BigDecimal getTasaDet() {
		return tasaDet;
	}
	public void setTasaDet(BigDecimal tasaDet) {
		this.tasaDet = tasaDet;
	}
	public String getNoSolDet() {
		return noSolDet;
	}
	public void setNoSolDet(String noSolDet) {
		this.noSolDet = noSolDet;
	}
	public String getTipoSolDet() {
		return tipoSolDet;
	}
	public void setTipoSolDet(String tipoSolDet) {
		this.tipoSolDet = tipoSolDet;
	}
	public String getLineaDet() {
		return lineaDet;
	}
	public void setLineaDet(String lineaDet) {
		this.lineaDet = lineaDet;
	}
	
	public HtmlDropDownList getHddMonedaDetalle() {
		return hddMonedaDetalle;
	}
	public void setHddMonedaDetalle(HtmlDropDownList hddMonedaDetalle) {
		this.hddMonedaDetalle = hddMonedaDetalle;
	}
	public List getLstMonedaDetalle() {	
		if(m.get("lstMonedaDetalle")==null){
			lstMonedaDetalle = new ArrayList();
			m.put("lstMonedaDetalle",lstMonedaDetalle);
		}else{
			lstMonedaDetalle = (List)m.get("lstMonedaDetalle");
		}
		return lstMonedaDetalle;
	}
	public void setLstMonedaDetalle(List lstMonedaDetalle) {
		this.lstMonedaDetalle = lstMonedaDetalle;
	}
	public HtmlDialogWindow getDwRecibo() {
		return dwRecibo;
	}
	public void setDwRecibo(HtmlDialogWindow dwRecibo) {
		this.dwRecibo = dwRecibo;
	}
	
	

	public HtmlInputText getTxtNumrecm() {
		return txtNumrecm;
	}

	public void setTxtNumrecm(HtmlInputText txtNumrecm) {
		this.txtNumrecm = txtNumrecm;
	}
	public HtmlDropDownList getDdlTipoRecibo() {
		return ddlTipoRecibo;
	}
	public void setDdlTipoRecibo(HtmlDropDownList ddlTipoRecibo) {
		this.ddlTipoRecibo = ddlTipoRecibo;
	}
	public List getLstTipoRecibo() {
		if (lstTipoRecibo == null) {
			lstTipoRecibo = new ArrayList();
			lstTipoRecibo.add(new SelectItem("01", "AUTOMATICO"));
			lstTipoRecibo.add(new SelectItem("02", "MANUAL"));
		}
		return lstTipoRecibo;
	}
	public void setLstTipoRecibo(List lstTipoRecibo) {
		this.lstTipoRecibo = lstTipoRecibo;
	}
	public HtmlOutputText getTxtNumrec() {
		return txtNumrec;
	}
	public void setTxtNumrec(HtmlOutputText txtNumrec) {
		this.txtNumrec = txtNumrec;
	}
	public HtmlOutputText getLblNumrecm() {
		return lblNumrecm;
	}
	public void setLblNumrecm(HtmlOutputText lblNumrecm) {
		this.lblNumrecm = lblNumrecm;
	}
	public HtmlOutputText getTxtCodcli() {
		return txtCodcli;
	}
	public void setTxtCodcli(HtmlOutputText txtCodcli) {
		this.txtCodcli = txtCodcli;
	}
	public HtmlOutputText getTxtNomcli() {
		return txtNomcli;
	}
	public void setTxtNomcli(HtmlOutputText txtNomcli) {
		this.txtNomcli = txtNomcli;
	}
	public HtmlDateChooser getDcFecham() {
		return dcFecham;
	}
	public void setDcFecham(HtmlDateChooser dcFecham) {
		this.dcFecham = dcFecham;
	}

	public HtmlDropDownList getDdlMetodosPago() {
		return ddlMetodosPago;
	}

	public void setDdlMetodosPago(HtmlDropDownList ddlMetodosPago) {
		this.ddlMetodosPago = ddlMetodosPago;
	}

	public List getLstMetodosPago() {
		if (m.get("lstMetodosPagoFinan") == null) {
			lstMetodosPago = new ArrayList();
		} else {
			lstMetodosPago = (List) m.get("lstMetodosPagoFinan");
		}
		return lstMetodosPago;
	}

	public void setLstMetodosPago(List lstMetodosPago) {
		this.lstMetodosPago = lstMetodosPago;
	}

	public HtmlDropDownList getDdlMoneda() {
		return ddlMoneda;
	}

	public void setDdlMoneda(HtmlDropDownList ddlMoneda) {
		this.ddlMoneda = ddlMoneda;
	}

	public List getLstMoneda() {
		if (m.get("lstMonedaFinan") == null) {
			lstMoneda = new ArrayList();
		} else {
			lstMoneda = (List) m.get("lstMonedaFinan");
		}
		return lstMoneda;
	}

	public void setLstMoneda(List lstMoneda) {
		this.lstMoneda = lstMoneda;
	}

	public List getLstAfiliado() {
		try {
			lstAfiliado = (m.containsKey("lstAfiliado"))?
					(List<SelectItem>) m.get("lstAfiliado"):
					 new ArrayList<SelectItem>();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lstAfiliado;
	}

	public void setLstAfiliado(List lstAfiliado) {
		this.lstAfiliado = lstAfiliado;
	}

	public HtmlDropDownList getDdlAfiliado() {
		return ddlAfiliado;
	}

	public void setDdlAfiliado(HtmlDropDownList ddlAfiliado) {
		this.ddlAfiliado = ddlAfiliado;
	}

	public HtmlOutputText getLblAfiliado() {
		return lblAfiliado;
	}

	public void setLblAfiliado(HtmlOutputText lblAfiliado) {
		this.lblAfiliado = lblAfiliado;
	}

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

	public void setLstBanco(List lstBanco) {
		this.lstBanco = lstBanco;
	}

	public HtmlDropDownList getDdlBanco() {
		return ddlBanco;
	}

	public void setDdlBanco(HtmlDropDownList ddlBanco) {
		this.ddlBanco = ddlBanco;
	}

	public HtmlOutputText getLblBanco() {
		return lblBanco;
	}

	public void setLblBanco(HtmlOutputText lblBanco) {
		this.lblBanco = lblBanco;
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

	public HtmlOutputText getLblReferencia1() {
		return lblReferencia1;
	}

	public void setLblReferencia1(HtmlOutputText lblReferencia1) {
		this.lblReferencia1 = lblReferencia1;
	}

	public HtmlOutputText getLblReferencia2() {
		return lblReferencia2;
	}

	public void setLblReferencia2(HtmlOutputText lblReferencia2) {
		this.lblReferencia2 = lblReferencia2;
	}

	public HtmlOutputText getLblReferencia3() {
		return lblReferencia3;
	}

	public void setLblReferencia3(HtmlOutputText lblReferencia3) {
		this.lblReferencia3 = lblReferencia3;
	}

	public HtmlOutputText getTcJDE() {
		TasaCambioCtrl tcCtrl = new TasaCambioCtrl();
		Tcambio[] tcambio = null;
		try{
			if(m.get("tcambio")==null){
				tcambio = tcCtrl.obtenerTasaCambioJDEdelDia();
				m.put("tcambio", tcambio);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return tcJDE;
	}

	public void setTcJDE(HtmlOutputText tcJDE) {
		this.tcJDE = tcJDE;
	}

	public HtmlOutputText getTcParalela() {
		TasaCambioCtrl tcCtrl = new TasaCambioCtrl();
		Tpararela[] tpcambio = null;
		try{
			if(m.get("tpcambio") == null){
				tpcambio = tcCtrl.obtenerTasaCambioParalela();
				m.put("tpcambio", tpcambio);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return tcParalela;
	}

	public void setTcParalela(HtmlOutputText tcParalela) {
		this.tcParalela = tcParalela;
	}

	public Date getFechaRecibo() {
		return fechaRecibo;
	}

	public void setFechaRecibo(Date fechaRecibo) {
		this.fechaRecibo = fechaRecibo;
	}
	public ArrayList getLstPagos() {
		if(m.get("lstPagosFinan")==null){
			lstPagos = new ArrayList();
			m.put("lstPagosFinan",lstPagos);
		}else{
			lstPagos = (ArrayList)m.get("lstPagosFinan");
		}
		return lstPagos;
	}

	public void setLstPagos(ArrayList lstPagos) {
		this.lstPagos = lstPagos;
	}

	public HtmlGridView getGvMetodosPago() {
		return gvMetodosPago;
	}

	public void setGvMetodosPago(HtmlGridView gvMetodosPago) {
		this.gvMetodosPago = gvMetodosPago;
	}

	public List getLstSelectedCuotas() {
		if(m.get("lstComponentes")==null){
			lstSelectedCuotas = new ArrayList();
			m.put("lstComponentes",lstSelectedCuotas);
		}else{
			lstSelectedCuotas = (List)m.get("lstComponentes");
		}
		return lstSelectedCuotas;
	}

	public void setLstSelectedCuotas(List lstSelectedCuotas) {
		this.lstSelectedCuotas = lstSelectedCuotas;
	}

	public HtmlGridView getGvSelectedCuotas() {
		return gvSelectedCuotas;
	}

	public void setGvSelectedCuotas(HtmlGridView gvSelectedCuotas) {
		this.gvSelectedCuotas = gvSelectedCuotas;
	}
	public HtmlGridView getGvIntereses() {
		return gvIntereses;
	}
	public void setGvIntereses(HtmlGridView gvIntereses) {
		this.gvIntereses = gvIntereses;
	}
	public List getLstIntereses() {
		if(m.get("lstIntereses")==null){
			lstIntereses = new ArrayList();
			m.put("lstIntereses",lstIntereses);
		}else{
			lstIntereses = (List)m.get("lstIntereses");
		}
		return lstIntereses;
	}
	public void setLstIntereses(List lstIntereses) {
		this.lstIntereses = lstIntereses;
	}
	public HtmlInputTextarea getTxtConcepto() {
		return txtConcepto;
	}
	public void setTxtConcepto(HtmlInputTextarea txtConcepto) {
		this.txtConcepto = txtConcepto;
	}
	public HtmlOutputText getLblMontoAplicar() {
		return lblMontoAplicar;
	}
	public void setLblMontoAplicar(HtmlOutputText lblMontoAplicar) {
		this.lblMontoAplicar = lblMontoAplicar;
	}
	public HtmlOutputText getLblMontoRecibido() {
		return lblMontoRecibido;
	}
	public void setLblMontoRecibido(HtmlOutputText lblMontoRecibido) {
		this.lblMontoRecibido = lblMontoRecibido;
	}
	public HtmlInputText getTxtMontoAplicar() {
		return txtMontoAplicar;
	}
	public void setTxtMontoAplicar(HtmlInputText txtMontoAplicar) {
		this.txtMontoAplicar = txtMontoAplicar;
	}
	public HtmlOutputText getTxtMontoRecibido() {
		return txtMontoRecibido;
	}
	public void setTxtMontoRecibido(HtmlOutputText txtMontoRecibido) {
		this.txtMontoRecibido = txtMontoRecibido;
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
	public HtmlInputText getTxtCambioForaneo() {
		return txtCambioForaneo;
	}
	public void setTxtCambioForaneo(HtmlInputText txtCambioForaneo) {
		this.txtCambioForaneo = txtCambioForaneo;
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
	public HtmlLink getLnkCambio() {
		return lnkCambio;
	}
	public void setLnkCambio(HtmlLink lnkCambio) {
		this.lnkCambio = lnkCambio;
	}
	public HtmlDialogWindow getDwMensajeError() {
		return dwMensajeError;
	}
	public void setDwMensajeError(HtmlDialogWindow dwMensajeError) {
		this.dwMensajeError = dwMensajeError;
	}
	public HtmlOutputText getLblMensajeError() {
		return lblMensajeError;
	}
	public void setLblMensajeError(HtmlOutputText lblMensajeError) {
		this.lblMensajeError = lblMensajeError;
	}

	public HtmlOutputText getMontoTotalAplicarDomestico() {
		return montoTotalAplicarDomestico;
	}

	public void setMontoTotalAplicarDomestico(
			HtmlOutputText montoTotalAplicarDomestico) {
		this.montoTotalAplicarDomestico = montoTotalAplicarDomestico;
	}

	public HtmlOutputText getMontoTotalFaltanteDomestico() {
		return montoTotalFaltanteDomestico;
	}

	public void setMontoTotalFaltanteDomestico(
			HtmlOutputText montoTotalFaltanteDomestico) {
		this.montoTotalFaltanteDomestico = montoTotalFaltanteDomestico;
	}

	public HtmlOutputText getMontoTotalAplicarForaneo() {
		return montoTotalAplicarForaneo;
	}

	public void setMontoTotalAplicarForaneo(HtmlOutputText montoTotalAplicarForaneo) {
		this.montoTotalAplicarForaneo = montoTotalAplicarForaneo;
	}

	public HtmlOutputText getMontoTotalFaltanteForaneo() {
		return montoTotalFaltanteForaneo;
	}

	public void setMontoTotalFaltanteForaneo(
			HtmlOutputText montoTotalFaltanteForaneo) {
		this.montoTotalFaltanteForaneo = montoTotalFaltanteForaneo;
	}

	public HtmlInputText getTxtMonto() {
		return txtMonto;
	}

	public void setTxtMonto(HtmlInputText txtMonto) {
		this.txtMonto = txtMonto;
	}

	public List getLstDetallesol() {
		if(m.get("lstDetallesol")==null){
			lstDetallesol = new ArrayList();
			m.put("lstDetallesol",lstDetallesol);
		}else{
			lstDetallesol = (List)m.get("lstDetallesol");
		}
		return lstDetallesol;
	}

	public void setLstDetallesol(List lstDetallesol) {
		this.lstDetallesol = lstDetallesol;
	}

	public HtmlGridView getGvDetallesol() {
		return gvDetallesol;
	}

	public void setGvDetallesol(HtmlGridView gvDetallesol) {
		this.gvDetallesol = gvDetallesol;
	}

	public BigDecimal getTxtPrincipalDet() {
		return txtPrincipalDet;
	}

	public void setTxtPrincipalDet(BigDecimal txtPrincipalDet) {
		this.txtPrincipalDet = txtPrincipalDet;
	}

	public BigDecimal getTxtInteresDet() {
		return txtInteresDet;
	}

	public void setTxtInteresDet(BigDecimal txtInteresDet) {
		this.txtInteresDet = txtInteresDet;
	}

	public BigDecimal getTxtImpuestoDet() {
		return txtImpuestoDet;
	}

	public void setTxtImpuestoDet(BigDecimal txtImpuestoDet) {
		this.txtImpuestoDet = txtImpuestoDet;
	}

	public BigDecimal getTxtTotalDet() {
		return txtTotalDet;
	}

	public void setTxtTotalDet(BigDecimal txtTotalDet) {
		this.txtTotalDet = txtTotalDet;
	}

	public BigDecimal getTxtPendienteDet() {
		return txtPendienteDet;
	}

	public void setTxtPendienteDet(BigDecimal txtPendienteDet) {
		this.txtPendienteDet = txtPendienteDet;
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
	public List getLstAutoriza() {
		try{
			
			if(CodeUtil.getFromSessionMap("fin_lstAutoriza") != null){
				 return lstAutoriza = (List)m.get("fin_lstAutoriza");
			}
		 
			lstAutoriza = new ArrayList<SelectItem>();
			Vf55ca01 f = (Vf55ca01)( (List)CodeUtil.getFromSessionMap("lstCajas")).get(0);
			
			lstAutoriza.add( new SelectItem( f.getId().getCaan8mail(), f.getId().getCaan8nom() , "Supervisor" ) ) ;
			lstAutoriza.add( new SelectItem( f.getId().getCaautimail(), f.getId().getCaautinom() , "Autorizador titular" ) ) ;
			lstAutoriza.add( new SelectItem( f.getId().getCaausumail(), f.getId().getCaausunom() , "Autorizador suplente" ) ) ;
			lstAutoriza.add( new SelectItem( f.getId().getCacontmail(), f.getId().getCacontnom() , "Contador" ) ) ;
			
			CodeUtil.putInSessionMap( "fin_lstAutoriza", lstAutoriza);
			CodeUtil.putInSessionMap("fin_lstAutorizadores", "");
			
			/*
			if(m.get("fin_lstAutoriza") == null){
				lstAutoriza = new ArrayList();
				List lstAutorizadores = new ArrayList();
				EmpleadoCtrl empCtrl = new EmpleadoCtrl();
				Vf0101 vf0101 = null;
				//obtener caja
				List lstCajas = (List)m.get("lstCajas");
				Vf55ca01 f55ca01 = null;
				f55ca01 = (Vf55ca01)lstCajas.get(0);
				
				//obtener info de supervisor  index 0
				vf0101 = empCtrl.buscarEmpleadoxCodigo2(f55ca01.getId().getCaan8());	
				lstAutoriza.add(new SelectItem(vf0101.getId().getWwrem1().trim() + " " + vf0101.getId().getAban8(),vf0101.getId().getAbalph().trim(),"Supervisor"));
				lstAutorizadores.add(vf0101);
				
				//obtener info del autorizador titular index 1
				vf0101 = empCtrl.buscarEmpleadoxCodigo2(f55ca01.getId().getCaauti());	
				lstAutoriza.add(new SelectItem(vf0101.getId().getWwrem1().trim() + " " + vf0101.getId().getAban8(),vf0101.getId().getAbalph().trim(),"Autorizador titular"));
				lstAutorizadores.add(vf0101);
				
				//obtener info del autorizador suplente index 2, opcional
				if(f55ca01.getId().getCaausu() > 0){
					vf0101 = empCtrl.buscarEmpleadoxCodigo2(f55ca01.getId().getCaausu());	
					lstAutoriza.add(new SelectItem(vf0101.getId().getWwrem1().trim() + " " + vf0101.getId().getAban8(),vf0101.getId().getAbalph().trim(),"Autorizador suplente"));
					lstAutorizadores.add(vf0101);
				}
				
				//obtener info del autorizador suplente index 3, opcional
				if(f55ca01.getId().getCacont() > 0){
					vf0101 = empCtrl.buscarEmpleadoxCodigo2(f55ca01.getId().getCacont());	
					lstAutoriza.add(new SelectItem(vf0101.getId().getWwrem1().trim() + " " + vf0101.getId().getAban8(),vf0101.getId().getAbalph().trim(),"Contador"));
					lstAutorizadores.add(vf0101);
				}
				//leer los usuarios qure tengan el perfil de aprobador y la AUT025
				
				
				m.put("fin_lstAutoriza", lstAutoriza);
				m.put("fin_lstAutorizadores", lstAutorizadores);
			}else{
				lstAutoriza = (List)m.get("fin_lstAutoriza");
			}
			*/
			
		}catch(Exception ex){
			ex.printStackTrace(); 
		}
		return lstAutoriza;
	}
	public void setLstAutoriza(List lstAutoriza) {
		this.lstAutoriza = lstAutoriza;
	}
	public HtmlInputText getTxtReferencia() {
		return txtReferencia;
	}
	public void setTxtReferencia(HtmlInputText txtReferencia) {
		this.txtReferencia = txtReferencia;
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
	public HtmlDropDownList getCmbAutoriza() {
		return cmbAutoriza;
	}
	public void setCmbAutoriza(HtmlDropDownList cmbAutoriza) {
		this.cmbAutoriza = cmbAutoriza;
	}
	public BigDecimal getTxtTotalMoraDet() {
		return txtTotalMoraDet;
	}
	public void setTxtTotalMoraDet(BigDecimal txtTotalMoraDet) {
		this.txtTotalMoraDet = txtTotalMoraDet;
	}
	public HtmlDialogWindow getDwDetalleCuota() {
		return dwDetalleCuota;
	}
	public void setDwDetalleCuota(HtmlDialogWindow dwDetalleCuota) {
		this.dwDetalleCuota = dwDetalleCuota;
	}
	public Date getFechaCuotaDet() {
		return fechaCuotaDet;
	}
	public void setFechaCuotaDet(Date fechaCuotaDet) {
		this.fechaCuotaDet = fechaCuotaDet;
	}
	public String getNoCuotaDet() {
		return noCuotaDet;
	}
	public void setNoCuotaDet(String noCuotaDet) {
		this.noCuotaDet = noCuotaDet;
	}
	public BigDecimal getTasaCuotaDet() {
		return tasaCuotaDet;
	}
	public void setTasaCuotaDet(BigDecimal tasaCuotaDet) {
		this.tasaCuotaDet = tasaCuotaDet;
	}
	public BigDecimal getPrincipalCuotaDet() {
		return principalCuotaDet;
	}
	public void setPrincipalCuotaDet(BigDecimal principalCuotaDet) {
		this.principalCuotaDet = principalCuotaDet;
	}
	public BigDecimal getInteresCuotaDet() {
		return interesCuotaDet;
	}
	public void setInteresCuotaDet(BigDecimal interesCuotaDet) {
		this.interesCuotaDet = interesCuotaDet;
	}
	public BigDecimal getImpuestoCuotaDet() {
		return impuestoCuotaDet;
	}
	public void setImpuestoCuotaDet(BigDecimal impuestoCuotaDet) {
		this.impuestoCuotaDet = impuestoCuotaDet;
	}
	public BigDecimal getMoraCuotaDet() {
		return moraCuotaDet;
	}
	public void setMoraCuotaDet(BigDecimal moraCuotaDet) {
		this.moraCuotaDet = moraCuotaDet;
	}
	public BigDecimal getTotalCuotaDet() {
		return totalCuotaDet;
	}
	public void setTotalCuotaDet(BigDecimal totalCuotaDet) {
		this.totalCuotaDet = totalCuotaDet;
	}
	public BigDecimal getPendienteCuotaDet() {
		return pendienteCuotaDet;
	}
	public void setPendienteCuotaDet(BigDecimal pendienteCuotaDet) {
		this.pendienteCuotaDet = pendienteCuotaDet;
	}
	public String getDiasVenCuotaDet() {
		return diasVenCuotaDet;
	}
	public void setDiasVenCuotaDet(String diasVenCuotaDet) {
		this.diasVenCuotaDet = diasVenCuotaDet;
	}
	public String getDiasAcumCuotaDet() {
		return diasAcumCuotaDet;
	}
	public void setDiasAcumCuotaDet(String diasAcumCuotaDet) {
		this.diasAcumCuotaDet = diasAcumCuotaDet;
	}
	public String getTotalDiasCuotaDet() {
		return totalDiasCuotaDet;
	}
	public void setTotalDiasCuotaDet(String totalDiasCuotaDet) {
		this.totalDiasCuotaDet = totalDiasCuotaDet;
	}
	public String getMonedaCuotaDet() {
		return monedaCuotaDet;
	}
	public void setMonedaCuotaDet(String monedaCuotaDet) {
		this.monedaCuotaDet = monedaCuotaDet;
	}
	public String getCompaniaCuotaDet() {
		return companiaCuotaDet;
	}
	public void setCompaniaCuotaDet(String companiaCuotaDet) {
		this.companiaCuotaDet = companiaCuotaDet;
	}
	public String getSucursalCuotaDet() {
		return sucursalCuotaDet;
	}
	public void setSucursalCuotaDet(String sucursalCuotaDet) {
		this.sucursalCuotaDet = sucursalCuotaDet;
	}
	public HtmlOutputText getIntSelectedDet() {
		return intSelectedDet;
	}
	public void setIntSelectedDet(HtmlOutputText intSelectedDet) {
		this.intSelectedDet = intSelectedDet;
	}

	public HtmlDialogWindow getDwAgregarCuota() {
		return dwAgregarCuota;
	}

	public void setDwAgregarCuota(HtmlDialogWindow dwAgregarCuota) {
		this.dwAgregarCuota = dwAgregarCuota;
	}

	public HtmlGridView getGvAgregarCuota() {
		return gvAgregarCuota;
	}

	public void setGvAgregarCuota(HtmlGridView gvAgregarCuota) {
		this.gvAgregarCuota = gvAgregarCuota;
	}

	public List getLstAgregarCuota() {
		if(m.get("lstAgregarCuota")==null){
			lstAgregarCuota = new ArrayList();
			m.put("lstAgregarCuota", lstAgregarCuota);
		}else{
			lstAgregarCuota = (List)m.get("lstAgregarCuota");
		}
		return lstAgregarCuota;
	}

	public void setLstAgregarCuota(List lstAgregarCuota) {
		this.lstAgregarCuota = lstAgregarCuota;
	}
	public HtmlDialogWindow getDwProcesaRecibo() {
		return dwProcesaRecibo;
	}
	public void setDwProcesaRecibo(HtmlDialogWindow dwProcesaRecibo) {
		this.dwProcesaRecibo = dwProcesaRecibo;
	}
	public HtmlDialogWindow getDwCancelar() {
		return dwCancelar;
	}
	public void setDwCancelar(HtmlDialogWindow dwCancelar) {
		this.dwCancelar = dwCancelar;
	}
	
	/** 
	 * @managed-bean true
	 */
	protected P55RECIBO getP55recibo() {
		if (p55recibo == null) {
			p55recibo = (P55RECIBO)  FacesContext.getCurrentInstance().getApplication()
					.createValueBinding("#{p55recibo}").getValue(
							 FacesContext.getCurrentInstance());
		}
		return p55recibo;
	}

	/** 
	 * @managed-bean true
	 */
	protected void setP55recibo(P55RECIBO p55recibo) {
		this.p55recibo = p55recibo;
	}
	public HtmlCheckBox getChkPrincipal() {
		return chkPrincipal;
	}
	public void setChkPrincipal(HtmlCheckBox chkPrincipal) {
		this.chkPrincipal = chkPrincipal;
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

	public HtmlDialogWindow getDwCargando() {
		return dwCargando;
	}

	public void setDwCargando(HtmlDialogWindow dwCargando) {
		this.dwCargando = dwCargando;
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
	public HtmlDialogWindow getDwBorrarPago() {
		return dwBorrarPago;
	}
	public void setDwBorrarPago(HtmlDialogWindow dwBorrarPago) {
		this.dwBorrarPago = dwBorrarPago;
	}
	protected P55CA090 getP55ca090() {
		if (p55ca090 == null) {
			p55ca090 = (P55CA090) FacesContext.getCurrentInstance()
					.getApplication().createValueBinding("#{p55ca090}")
					.getValue(FacesContext.getCurrentInstance());
		}
		return p55ca090;
	}
	protected void setP55ca090(P55CA090 p55ca090) {
		this.p55ca090 = p55ca090;
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
		
		if(CodeUtil.getFromSessionMap("fin_lstDonacionesRecibidas") != null )
			return lstDonacionesRecibidas = (ArrayList<DncIngresoDonacion>)
					CodeUtil.getFromSessionMap("fin_lstDonacionesRecibidas");
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
			
			lstBeneficiarios = (ArrayList<SelectItem>)CodeUtil.getFromSessionMap("fin_lstbeneficiarios");
			if(lstBeneficiarios != null &&  !lstBeneficiarios.isEmpty() )
				return lstBeneficiarios;
			
			lstBeneficiarios = (ArrayList<SelectItem>)DonacionesCtrl.obtenerBeneficiariosConfigurados(true, false);
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}finally{
			
			if(lstBeneficiarios == null )
				lstBeneficiarios = new ArrayList<SelectItem>();

			 CodeUtil.putInSessionMap("fin_lstbeneficiarios", lstBeneficiarios);
			
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
			
			lstMarcasDeTarjetas = (ArrayList<SelectItem>)CodeUtil.getFromSessionMap("fin_lstMarcasDeTarjetas");
			if(lstMarcasDeTarjetas != null &&  !lstMarcasDeTarjetas.isEmpty() )
				return lstMarcasDeTarjetas;
			
			lstMarcasDeTarjetas = AfiliadoCtrl.obtenerTiposMarcaTarjetas();
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}finally{
			
			if(lstMarcasDeTarjetas == null )
				lstMarcasDeTarjetas = new ArrayList<SelectItem>();

			 CodeUtil.putInSessionMap("fin_lstMarcasDeTarjetas", lstMarcasDeTarjetas);
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

	public HtmlGridView getGvDetalleExtraFinanciamientos() {
		return gvDetalleExtraFinanciamientos;
	}

	public void setGvDetalleExtraFinanciamientos(
			HtmlGridView gvDetalleExtraFinanciamientos) {
		this.gvDetalleExtraFinanciamientos = gvDetalleExtraFinanciamientos;
	}

	public List<Credhdr> getLstDetalleExtraFinanciamientos() {
		
		if(CodeUtil.getFromSessionMap("fin_lstDetalleExtraFinanciamientos") == null)
			return lstDetalleExtraFinanciamientos = new ArrayList<Credhdr>();
		 
		return lstDetalleExtraFinanciamientos = (ArrayList<Credhdr>)CodeUtil.getFromSessionMap("fin_lstDetalleExtraFinanciamientos");
		 
	}

	public void setLstDetalleExtraFinanciamientos(
			List<Credhdr> lstDetalleExtraFinanciamientos) {
		this.lstDetalleExtraFinanciamientos = lstDetalleExtraFinanciamientos;
	}

	public HtmlJspPanel getPnlDatosOtrosFinancimientos() {
		return pnlDatosOtrosFinancimientos;
	}

	public void setPnlDatosOtrosFinancimientos(
			HtmlJspPanel pnlDatosOtrosFinancimientos) {
		this.pnlDatosOtrosFinancimientos = pnlDatosOtrosFinancimientos;
	}

	public HtmlOutputText getLblFacturaOtrosFinan() {
		return lblFacturaOtrosFinan;
	}

	public void setLblFacturaOtrosFinan(HtmlOutputText lblFacturaOtrosFinan) {
		this.lblFacturaOtrosFinan = lblFacturaOtrosFinan;
	}
	
}
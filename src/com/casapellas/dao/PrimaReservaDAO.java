package com.casapellas.dao;
/**
 * CASA PELLAS S.A.
 * Creado por.........: Juan Carlos Ñamendi Pineda
 * Fecha de Creación..: 28/02/2009
 * Última modificación: 12/06/2012
 * Modificado por.....:	Juan Carlos Ñamendi Pineda.
 * Modificacion.......: Seleccion de tipo de doc x linea
 * 
 */
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Types;
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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.casapellas.controles.ConsolidadoDepositosBcoCtrl;
import com.casapellas.controles.CuotaCtrl;
import com.casapellas.controles.DonacionesCtrl;
import com.casapellas.controles.tmp.AfiliadoCtrl;
import com.casapellas.controles.tmp.BancoCtrl;
import com.casapellas.controles.tmp.BienCtrl;
import com.casapellas.controles.tmp.CompaniaCtrl;
import com.casapellas.controles.tmp.CtrlCajas;
import com.casapellas.controles.tmp.CtrlPoliticas;
import com.casapellas.controles.tmp.CtrlSolicitud;
import com.casapellas.controles.tmp.F55ca036Ctrl;
import com.casapellas.controles.tmp.EmpleadoCtrl;
import com.casapellas.controles.tmp.MetodosPagoCtrl;
import com.casapellas.controles.tmp.MonedaCtrl;
import com.casapellas.controles.tmp.NumcajaCtrl;
import com.casapellas.controles.tmp.ProductoCrtl;
import com.casapellas.controles.tmp.ReciboCtrl;
import com.casapellas.controles.tmp.SucursalCtrl;
import com.casapellas.controles.tmp.TasaCambioCtrl;
import com.casapellas.donacion.entidades.DncIngresoDonacion;
import com.casapellas.entidades.Cafiliados;
import com.casapellas.entidades.F55ca014;
import com.casapellas.entidades.F55ca022;

import com.casapellas.entidades.MetodosPago;
import com.casapellas.entidades.Metpago;
import com.casapellas.entidades.Recibo;
import com.casapellas.entidades.Recibojde;
import com.casapellas.entidades.Solicitud;
import com.casapellas.entidades.SolicitudId;
import com.casapellas.entidades.TallerUn;
import com.casapellas.entidades.Tcambio;
import com.casapellas.entidades.TcambioId;
import com.casapellas.entidades.Tpararela;
import com.casapellas.entidades.TpararelaId;
import com.casapellas.entidades.Unegocio;
import com.casapellas.entidades.Vf55ca012;
import com.casapellas.entidades.ens.Vautoriz;
import com.casapellas.entidades.Vf0101;
import com.casapellas.entidades.Vf55ca01;
import com.casapellas.entidades.Vf55ca036;
import com.casapellas.entidades.Vmarca;
import com.casapellas.entidades.Vmodelo;
import com.casapellas.entidades.VtipoProd;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;

import com.casapellas.jde.creditos.ProcesarReciboRUCustom;
import com.casapellas.navegacion.As400Connection;
import com.casapellas.reportes.Prueba;
import com.casapellas.rpg.P55CA090;
import com.casapellas.rpg.P55RECIBO;
import com.casapellas.socketpos.PosCtrl;
import com.casapellas.util.BitacoraSecuenciaReciboService;
import com.casapellas.util.CalendarToJulian;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.Divisas;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.PropertiesSystem;
import com.ibm.faces.component.html.HtmlPanelSection;
import com.infragistics.faces.grid.component.RowItem;
import com.infragistics.faces.grid.component.html.HtmlGridView;
import com.infragistics.faces.grid.event.CellValueChangeEvent;
import com.infragistics.faces.input.component.html.HtmlCheckBox;
import com.infragistics.faces.input.component.html.HtmlDateChooser;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
import com.infragistics.faces.shared.component.DataRepeater;
import com.infragistics.faces.shared.component.html.HtmlLink;
import com.infragistics.faces.shared.smartrefresh.SmartRefreshManager;
import com.infragistics.faces.window.component.DialogWindow;
import com.infragistics.faces.window.component.html.HtmlDialogWindow;

@SuppressWarnings("unchecked")
public class PrimaReservaDAO {
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
	
	Map<String, Object> m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();	
	private String lblTasaCambio = "";
	private HtmlInputText txtCodigoSearch;
	private HtmlInputText txtNombreSearch;
	private UIInput cmbBusquedaPrima;
	private List lstBusquedaPrima;
	private UIInput txtParametro;
	private HtmlOutputText lblCodigoSearch,lblTcambioJde2;
	private HtmlOutputText lblNombreSearch;
	private UIOutput lblCod;
	private UIOutput lblNom;
	private HtmlPanelSection jspSec1;
	
	//---- Datos del Pago
	private HtmlInputText txtMonto;
	private HtmlInputText txtReferencia1;
	private HtmlInputText txtReferencia2;
	private HtmlInputText txtReferencia3;	
	private UIOutput lblReferencia1;
	private UIOutput lblReferencia2;
	private UIOutput lblReferencia3;	
	
	//Monedas
	private HtmlDropDownList cmbMoneda;
	private List lstMoneda = null;
	
	//Metodos de pago
	private HtmlDropDownList cmbMetodosPago;
	private List<SelectItem> lstMetodosPago;	
	private List<SelectItem> lstAfiliado;	
	
	//Grid de metodos de pago
	private HtmlGridView metodosGrid = null;
	private ArrayList<MetodosPago> selectedMet = new ArrayList<MetodosPago>();
	private String lblTotal = "0.00";
	
	private HtmlDropDownList ddlAfiliado;
	private UIOutput lblAfiliado;
	private List lstBanco;	
	private HtmlDropDownList ddlBanco;
	private UIOutput lblBanco;

	private DialogWindow dwAutoriza;	
	
	//Tasa cambio
	private UIInput txtTasaCambio;
	

//	Datos del Recibo
	private String fechaRecibo;	
	private String lblNumeroRecibo;
	private UIOutput txtCliente;
	private HtmlInputTextarea txtConcepto;
	private String lblcliente;
	
//	Dialogs Window
	private DialogWindow dwProcesa;
	private DialogWindow dwImprime;
	//validacion
	private UIOutput lblMensajeValidacion;
	//solicitud
	private DialogWindow dwSolicitud;
	private UIOutput lblMensajeAutorizacion;
	private List lstAutoriza;
	public HtmlInputText txtReferencia;	
	public HtmlDateChooser txtFecha;
	public HtmlInputTextarea txtObs;
	public HtmlDropDownList cmbAutoriza;
//	Tipo de recibos
	private HtmlDropDownList cmbTiporecibo;
	private List lstTiporecibo = null;

	//Variables Tipo Recibo
	private UIOutput lblNumrec2;
	private UIOutput lblNumeroRecibo2;
	private String lblNumrec = "Último Recibo: ";
	private HtmlDateChooser txtFecham;
	private HtmlInputText txtNumRec;
	private UIOutput lblNumRecm;
	
	//DATOS DEL BIEN 
	//tipos de productos
	private HtmlDropDownList cmbTipoProducto;
	private List lstTipoProducto = null;
	//marcas
	private HtmlDropDownList cmbMarcas;
	private List lstMarcas;
	//modelos
	private HtmlDropDownList cmbModelos;
	private List lstModelos;
	private HtmlInputText txtNoItem;
	
	//DATOS DE COMPANIA
	private HtmlDropDownList cmbCompanias;
	private List lstCompanias;
	
	//----- Variables para Tasa de cambio, Sucursales, Unidades de negocio.
	private String lblTasaCambioJde2 = "1.0";
	private HtmlDropDownList ddlFiltrosucursal,ddlFiltrounineg;
	private List lstFiltrosucursal,lstFiltrounineg;
	private HtmlOutputText lblMonedaunineg2,lblMonto;
	
	private HtmlDropDownList ddlTipodoc;
	private List lstTipodoc;
	//------ Monto aplicado y cambios en primas.
	private List lstMonedaAplicada;
	private HtmlDropDownList ddlMonedaAplicada;
	private HtmlLink lnkAjustarTasaJdeAfecha, lnkCambio;
	private String lblMontoAplicar2,lbletMontoRecibido,lbletCambioapl1,lbletCambioDom1;
	private HtmlOutputText lblMontoAplicar, lbletCambioapl,lblCambioapl,lbletCambioDom,lblCambioDom;
	private HtmlInputText txtMontoAplicar,txtCambioForaneo;
	private HtmlOutputText lblMontoRecibido,lblMontoRecibido2;
	
	//------ Pago con tarjeta de crédito con voucher manuales.
	private HtmlCheckBox chkVoucherManual;
	private HtmlOutputText lbletVouchermanual;
	private HtmlDialogWindow dwCargando;
	
	//contrato prepagado
	private HtmlCheckBox chkContrato;
	private HtmlLink lnkSearchContrato;
	
	private HtmlDialogWindow dwBorrarPago;
	
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
	
	//&& ============== proforma de repuestos linea 03
	private HtmlCheckBox chkValidarProformaRepuestos;
	private HtmlLink lnkConsultarProformaRepuestos;
	
	//Valores de insercion de documentos en JDE	
	String[] valoresJdeNumeracion = (String[]) m.get("valoresJDENumeracionIns");
	String[] valoresJDEInsFCV = (String[]) m.get("valoresJDEInsFCV");
	String[] valoresJDEInsPrimaReservas = (String[]) m.get("valoresJDEInsPrimaReservas");
	
	public void consultarProformaRepuestos(ActionEvent ev){
		
		boolean proforma_valida = true;
		String strMsg = "";
		
		try {
			
			int codigo_cliente = 0;
			int numero_proforma = 0;
			
			String codcomp = cmbCompanias.getValue().toString();
			String codsuc = cmbCompanias.getValue().toString().trim();
			String strProformaNumero = txtNoItem.getValue().toString().trim();
			String strCodigoCliente = lblCodigoSearch.getValue().toString().trim();
			String monedaAplicada = ddlMonedaAplicada.getValue().toString();
			String montorecibido = lblMontoRecibido2.getValue().toString();
			BigDecimal bdMontoRecibido = new BigDecimal(montorecibido.trim().replace(",", "")) ;		
			
			
			proforma_valida = strCodigoCliente.matches(PropertiesSystem.REGEXP_8DIGTS) ;
			if( !proforma_valida ){
				strMsg = "Seleccione el codigo de cliente";
				return ;
			}
			proforma_valida = strProformaNumero.matches(PropertiesSystem.REGEXP_8DIGTS) ;
			
			if(!proforma_valida){
				strMsg = "Ingrese un numero de proforma válido";
				return;
			}
			proforma_valida =  bdMontoRecibido.compareTo(BigDecimal.ZERO) > 0;
			if(!proforma_valida){
				strMsg = "Debe ingresar pagos para validar el monto requerido aplicado por anticipo de Proforma";
				return;
			}
			
			codigo_cliente = Integer.parseInt(strCodigoCliente);
			numero_proforma = Integer.parseInt(strProformaNumero);
		
			codsuc = "000"+codsuc;
			

			
			if(!proforma_valida){
				strMsg = "Validación no Aprobada para Proforma "+numero_proforma+" cliente " + codigo_cliente + " sucursal '" + codsuc;
					 
			}
			
		} catch (Exception e) {
			proforma_valida = false;
			e.printStackTrace(); 
		}finally{
			
			if(proforma_valida){
				strMsg =  "Numero de proforma válido";
				txtNoItem.setStyleClass("frmInput2ddl");
			}else{
				txtNoItem.setStyleClass("frmInput2Error");
			}
			
			lblMensajeValidacion.setValue(strMsg);
			dwProcesa.setWindowState("normal");
			
			CodeUtil.refreshIgObjects(new Object[]{dwProcesa, txtNoItem});
			
		}
	}
	
	
	public void procesarDonacionesIngresadas(ActionEvent ev){
		String msg = "";
		try {
			
			if( CodeUtil.getFromSessionMap("pr_lstDonacionesRecibidas") == null ){
				msg = "Ingrese al menos una donación para el proceso" ;
				return;
			}
			BigDecimal montodonado = new BigDecimal(lblTotalMontoDonacion.getValue().toString().trim().replace(",", ""));
			if(montodonado.compareTo(BigDecimal.ZERO) == 0 ){
				msg = "El monto de donaciónd debe ser mayor a cero" ;
				return;
			}
			CodeUtil.putInSessionMap("pr_MontoTotalEnDonacion", montodonado);
			
			dwIngresarDatosDonacion.setWindowState("hidden");
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}finally{
			msgValidaIngresoDonacion.setValue(msg);
			CodeUtil.refreshIgObjects(new Object[]{msgValidaIngresoDonacion}) ;
		}
	}
	
	
	public void agregarMontoDonacion(ActionEvent ev){
		String msg = "" ;
		try {
			
			final String beneficiario = ddlDnc_Beneficiario.getValue().toString();
			String strMontoDonacion = txtdnc_montodonacion.getValue().toString();
			String moneda = cmbMoneda.getValue().toString();
			String codformapago = cmbMetodosPago.getValue().toString(); 
			String metodopago = codformapago + ", " + lblFormaDePagoCliente.getValue().toString();
			String codcomp = cmbCompanias.getValue().toString();
			
			if( lblCodigoSearch.getValue() == null || (lblCodigoSearch.getValue().toString()).trim().isEmpty() ){
				msg = "Ingrese los datos del cliente";
				return;
			}
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
			
			lstBeneficiarios = (ArrayList<SelectItem>)CodeUtil.getFromSessionMap("pr_lstbeneficiarios");
			SelectItem si = (SelectItem) CollectionUtils.find(lstBeneficiarios,
				new Predicate() {
					public boolean evaluate(Object o) {
						return ((SelectItem) o).getValue().toString().compareTo(beneficiario) == 0;
					}
				});
			
			lstDonacionesRecibidas = ( CodeUtil.getFromSessionMap("pr_lstDonacionesRecibidas") == null )?
					new ArrayList<DncIngresoDonacion>():
					(ArrayList<DncIngresoDonacion>)CodeUtil.getFromSessionMap("pr_lstDonacionesRecibidas");
					
			DncIngresoDonacion	dncExist = (DncIngresoDonacion)	 
			 CollectionUtils.find(lstDonacionesRecibidas, new Predicate(){
				public boolean evaluate(Object o) {
					return ((DncIngresoDonacion)o).getIdbenficiario() == 
							Integer.parseInt(beneficiario.split("@")[0]) ;
				}
			 });
			
			String nombrecliente = lblNombreSearch.getValue().toString().trim();
			int codigocliente = Integer.parseInt(lblCodigoSearch.getValue().toString().trim());
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
			
			CodeUtil.putInSessionMap("pr_lstDonacionesRecibidas", lstDonacionesRecibidas) ;
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
			
			List<Vf55ca012>metodospagoconfig = (ArrayList<Vf55ca012>)CodeUtil.getFromSessionMap("pr_MetodosPagoConfigurados");
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
			CodeUtil.putInSessionMap("pr_lstDonacionesRecibidas", lstDonacionesRecibidas);
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

	public void cerrarVentanaDonacion(ActionEvent ev) {
		CodeUtil.removeFromSessionMap(new String[]{"pr_MontoTotalEnDonacion","pr_lstDonacionesRecibida"} ) ;
		dwIngresarDatosDonacion.setWindowState("hidden");
	}

	/** **************** mostrar ventana de confirmacion de borrar pago */
	public void mostrarBorrarPago(ActionEvent ev) {
		try {
			RowItem ri = (RowItem) ev.getComponent().getParent().getParent();
			MetodosPago mPago = (MetodosPago) DataRepeater.getDataRow(ri);
			m.put("metodopagoborrar", mPago);
		} catch (Exception e) {
			e.printStackTrace();
		}
		dwBorrarPago.setWindowState("normal");
	}

	public void cerrarBorrarPago(ActionEvent ev) {
		dwBorrarPago.setWindowState("hidden");
		m.remove("metodopagoborrar");
	}
		
	/** Método: Anular recibo de caja por mala aplicacion de socket pos
	 *	Fecha:  16/0622011
	 *  Nombre: Carlos Manuel Hernández Morrison.
	 */
	
	public boolean  anularRecibo(int iNumrec,int iCaid,String sCodsuc, String sCodcomp,String sCodUsera, int iNoFCV){
		ReciboCtrl recCtrl = new ReciboCtrl();
		List<Recibojde> lstRecibojde = null;
		int iNobatch = 0;
		int iNodoc 	 = 0;
		boolean bHecho = true;
		Connection cn;
		
		try{
			cn = As400Connection.getJNDIConnection("DSMCAJA2");
			cn.setAutoCommit(false);
			
			recCtrl.actualizarEstadoRecibo(iNumrec,iCaid,sCodsuc,sCodcomp, sCodUsera, "No se completo pago con SP","PR");
			lstRecibojde = recCtrl.getEnlaceReciboJDE(iCaid, sCodsuc,sCodcomp,iNumrec,valoresJDEInsPrimaReservas[0]);
			
			if(lstRecibojde != null && lstRecibojde.size() > 0){
				for(Recibojde rj: lstRecibojde){
					iNobatch = rj.getId().getNobatch();
					iNodoc 	 = rj.getId().getRecjde();
					
					if (rj.getId().getTipodoc().equals("R")) {
						bHecho = recCtrl.borrarBatch(cn, iNobatch, "R");
						if (bHecho)
							bHecho = recCtrl.eliminarRegistros311xTipo(
									iNobatch, "RU", cn);
					}
					if (rj.getId().getTipodoc().equals("A")) {
						bHecho = recCtrl.borrarBatch(cn, iNobatch, "G");
						if (bHecho) {
							bHecho = recCtrl.borrarAsientodeDiario(cn,
									iNodoc, iNobatch);
							if (!bHecho)
								break;
						}
					}
				}
			}
			
			//&& ========== Anulacion de FCV
			if(iNoFCV > 0 ){
				Recibo ficha = recCtrl.obtenerFichaCV(iNoFCV, iCaid, sCodcomp, sCodsuc);
				lstRecibojde = recCtrl.getEnlaceReciboJDE(iCaid, sCodsuc, 
									ficha.getId().getCodcomp(),
									ficha.getId().getNumrec(),
									ficha.getId().getTiporec());
				
				recCtrl.leerEstadoBatch(cn, lstRecibojde.get(0).getId().getNobatch(),"G");

				bHecho = recCtrl.actualizarEstadoRecibo(null, null, 
										ficha.getId().getNumrec(),
										ficha.getId().getCaid(),
										ficha.getId().getCodsuc(),
										ficha.getId().getCodcomp(), 
										sCodUsera,
										"Error de aplicacion Socket POS",
										ficha.getId().getTiporec());
				
				Recibojde recibojde = null;
				for(Recibojde rj: lstRecibojde){
					recCtrl.borrarAsientodeDiario(cn, rj.getId().getRecjde(), rj.getId().getNobatch());
					recibojde = rj;
				}
				if(recibojde != null)
					recCtrl.borrarBatch(cn, recibojde.getId().getNobatch(),"G");
			} 
			//&& ======= Borrar los sobrantes.
			Recibojde recibojde = null;
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
		}
		finally{
//			try {cn.close();} catch (Exception e) {}		
		}
		return bHecho;
	}
	
/*************************************************************************************************************************/
	@SuppressWarnings({ "rawtypes" })
	public void cambiarUnidadNegocio(ValueChangeEvent ev){
		F55ca036Ctrl f36 = new F55ca036Ctrl();
		List<Vf55ca036> lstResult = null;
		String sCodunineg = "",sLinea = "";
		try{

			sCodunineg = ddlFiltrounineg.getValue().toString();
			if(!sCodunineg.equals("UN")){
				sLinea = (sCodunineg.trim().substring(2,4));									
			}
	
			lstResult = f36.buscarDocsxLinea(cmbCompanias.getValue().toString(), sLinea);
			lstTipodoc = new ArrayList();
			
			if (lstResult != null && lstResult.size() > 0) {
				//obtener Tipos de Productos				
				for (Iterator iterator = lstResult.iterator(); iterator.hasNext();) {
					Vf55ca036 f = (Vf55ca036) iterator.next();
					lstTipodoc.add(new SelectItem(f.getId().getB6dct(),f.getId().getB6dct() + " : " + f.getId().getDrdl01().trim()));
				} 	
			}

			lstTipodoc.add(new SelectItem("RU","RU"));
			m.put("pr_lstTipodoc",lstTipodoc);
			ddlTipodoc.dataBind();
			
			
			//&& ============= condiciones para mostrar validaciones de proforma de repuestos.
			chkValidarProformaRepuestos.setChecked(false);
			chkValidarProformaRepuestos.setStyle("display:none;") ;
			lnkConsultarProformaRepuestos.setStyle("display:none;") ;
			
			sLinea = CompaniaCtrl.leerLineaNegocio(sCodunineg);
			if(sLinea.trim().compareTo("03") == 0) {
				
				chkValidarProformaRepuestos.setChecked(true);
				chkValidarProformaRepuestos.setStyle("display:inline;") ;
				lnkConsultarProformaRepuestos.setStyle("display:inline;") ;
				
				CodeUtil.putInSessionMap("pr_ValidarProformaRespuestos", "1");
				
			}

			//&& ============ deshabilitar opcion de MPP para talleres en Primas y Reservas
			CodeUtil.removeFromSessionMap("tallerUn");
			chkContrato.setStyle("width: 20px; display: none");
			lnkSearchContrato.setStyle("display: none");

			//&& =====  buscar la moneda asignada a la linea, si la encuentra 
			//&& =====  bloquear la moneda aplicada y usar la configurada.
			if(!sCodunineg.equals("UN")){
				
				sCodunineg = (sCodunineg.split(":"))[0];
				
				sLinea = CompaniaCtrl.leerLineaNegocio(sCodunineg.trim());
				if(sLinea == null || sLinea.compareTo("") == 0){
					if(sCodunineg.length()==2)
						sLinea  = sCodunineg;
					else if(sCodunineg.length() >2)
						sLinea = sCodunineg.substring(sCodunineg.length()-2);
				}
			
				String sMndLinea = CompaniaCtrl.obtenerMonedaLineaN(sLinea);
				if(sMndLinea.compareTo("") != 0){
					lstMonedaAplicada = new ArrayList<SelectItem>();
					lstMonedaAplicada.add(new SelectItem(sMndLinea,sMndLinea,sMndLinea));
					m.put("pr_lstMonedaAplicada", lstMonedaAplicada);
				}else{
					m.remove("pr_lstMonedaAplicada");
					lstMonedaAplicada = getLstMonedaAplicada();
				}
				m.put("pr_lstMonedaAplicada", lstMonedaAplicada);
				ddlMonedaAplicada.dataBind();
				resetResumenPago(SmartRefreshManager.getCurrentInstance());
				
				//------------ Establecer los afiliados
				m.remove("pr_lstAfiliado");
				lstAfiliado = new ArrayList<SelectItem>();
				lstAfiliado = getLstAfiliado();
				
				if(lstAfiliado != null && !lstAfiliado.isEmpty()){
					m.put("pr_lstAfiliado",lstAfiliado);
					ddlAfiliado.dataBind();
				}
				
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
			LogCajaService.CreateLog("cambiarUnidadNegocio", "ERR", ex.getMessage());
		}
		finally{
			CodeUtil.refreshIgObjects(new Object[]{chkValidarProformaRepuestos, lnkConsultarProformaRepuestos});
		}
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
	public boolean anularPagosSP(List lstMetodosPago){
		boolean bPago = true;
		MetodosPago rd = null;
		List lstResult = null;
		PosCtrl p = new PosCtrl();
		
		try{
			//leer la terminal del afiliado seleccionado
			for (int i = 0; i < lstMetodosPago.size(); i++){
				rd = (MetodosPago)lstMetodosPago.get(i);
				if(rd.getMetodo().equals(MetodosPagoCtrl.TARJETA)  && rd.getVmanual().equals("2")){
					lstResult = p.anularTransaccionPOS(rd.getTerminal(), rd.getReferencia4(), rd.getReferencia5(), rd.getReferencia7());
					if(!lstResult.get(0).equals("00")){
						bPago = false;

					}
				}
			}
		}catch(Exception ex){
			bPago = false;
			ex.printStackTrace();
		}
		return bPago;
	}	
/*************************************************************************************************************************/
	
	public boolean aplicarPagoSocketPos(List lstPagos, List lstDatosTrack){
		boolean bHecho = true ;
		MetodosPago mPago = null;
		Divisas d = new Divisas();
		PosCtrl p = new PosCtrl();
		List<String> lstDatos  = new ArrayList<String>(), lstRespuesta = new ArrayList<String>();
		String sTerminal = "",sMonto = "", sNotarjeta = "" ;
		int j = 0;
		String nombre = "";
		List<MetodosPago> lstPagosAplicados = new ArrayList<MetodosPago>();
		
		try{
			//comprobar si hay pagos con tarjeta de credito
			for(int i = 0; i < lstPagos.size(); i++){
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
						
//						if(lstDatos.size()==7)
//							nombre = lstDatos.get(2);
//						else 
//							nombre = lstDatos.get(2) + " " + lstDatos.get(3);
						
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
							
							
						}else if (lstRespuesta.get(0).toString().equals("error")){
							lblMensajeValidacion.setValue("Tarjeta No.: " + sNotarjeta 
														+ " no aprobada, \n  Respuesta de banco: " 
														+ lstRespuesta.get(1) + "\n"
														+ lstRespuesta.get(2)+"\n" 
														+ lstRespuesta.get(3));
							lstDatos.add("false");
							m.put("lstDatosTrack_Con",lstDatosTrack);
							j++;
							return false;
						}
						else{
							lblMensajeValidacion.setValue("Tarjeta No.: " + sNotarjeta 
														+ " no aprobada, \nRespuesta de banco: " 
														+ lstRespuesta.get(1) );
							lstDatos.add("false");
							m.put("lstDatosTrack_Con",lstDatosTrack);
							j++;
							return false;
						}
						m.put("lstPagos",lstPagos);
					}
					else{//hubo algun error con la transferencia con credomatic
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
	
/********************************************************************************************************************/	
	@SuppressWarnings("rawtypes")
	public List<SelectItem> getLstAfiliados(int iCaid, String sCodcomp, String sLineaFactura,String sMoneda){
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
			LogCajaService.CreateLog("getLstAfiliados", "ERR", error.getMessage());
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
/*****************************************************************************************************/
	/** Método: Generar objeto para guardar configuración de caja.
	 *	Fecha:  16/0622011
	 *  Nombre: Carlos Manuel Hernández Morrison.
	 */
	public void generarObjConfigComp(String sCodcomp){
		String sMonedaBase = "";
		Vf55ca01 f5 = null;
		F55ca014[] f14_ConfigComp = null;
		Object[] objConfigComp = new Object[4];
		String sUsarTasa = "1";
		MonedaCtrl mC = new MonedaCtrl();
		String[] lstMonedaCC = null;
		
		try {
			m.remove("pr_OBJCONFIGCOMP");
			f5 = (Vf55ca01)((List)m.get("lstCajas")).get(0);
			f14_ConfigComp = (F55ca014[])m.get("pr_F55CA014");
						
			//---- Leer la moneda base de la compañía.			
			for (F55ca014 f14 : f14_ConfigComp) {
				if(f14.getId().getC4rp01().trim().equals(sCodcomp)){
					sMonedaBase = f14.getId().getC4bcrcd().trim();
					break;
				}
			}
			//---- Obtener monedas para compañía agrupadas por método de pago
			lstMonedaCC = mC.obtenerMonedasxCaja_Companias(f5.getId().getCaid(), f14_ConfigComp);
			if( lstMonedaCC != null && lstMonedaCC.length > 0 ){
				if( lstMonedaCC.length == 1 && lstMonedaCC[0].trim().equals(sMonedaBase)){
					sUsarTasa = "0";
				}
			}
			objConfigComp[0] =  sCodcomp;
			objConfigComp[1] =  sMonedaBase;
			objConfigComp[2] =  lstMonedaCC.length+"";
			objConfigComp[3] =  sUsarTasa;
			m.put("pr_OBJCONFIGCOMP", objConfigComp);
			
		} catch (Exception error) {
			objConfigComp = null;
			error.printStackTrace();
		}
	}
/******************************************************************************************/
/** Método: Guardar los asientos de diario para la ficha de compra venta
 *	Fecha:  26/06/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public boolean guardarAsientosFCV(Connection cn, Transaction transCaja, Transaction transENS,
									   Session sesionCaja, Session sesionENS, int iNumFCV, 
									   int iNumRec,int iCaid, String sCodcomp,String sCodsuc,
									   double dMontoFOR,double dTasaJDE,String sMonedaForanea,
									   String sMonedaBase, Vautoriz vaut,Vf55ca01 f55ca01){
		boolean bHecho = true;
		int iNobatchNodoc[],iMontoCOR=0,iMontoUSD=0;
		String sCCOR[],sCUSD[],sConcepto = "";
		ReciboCtrl rcCtrl = new ReciboCtrl();
		Divisas dv = new Divisas();
		String sTipoCliente,sAsientoSucursal,sMensaje="";
		EmpleadoCtrl emCtrl = new EmpleadoCtrl();
		Date dtFecha = new Date();
		
		try {
			sConcepto = "Ficha No:" + iNumFCV + " Ca:" + iCaid + " Com:" + sCodcomp;
			sTipoCliente = emCtrl.determinarTipoCliente(f55ca01.getId().getCaan8());
			
			//----- Obtener y actualizar el número de batch y de documento a utilizar.
			iNobatchNodoc = dv.obtenerNobatchNodoco();
			if(iNobatchNodoc != null){ 
				
				//---- Leer la cuenta de caja para efectivo córdobas y efectivo dólares.
				sCCOR = dv.obtenerCuentaCaja(iCaid, sCodcomp, MetodosPagoCtrl.EFECTIVO, sMonedaBase, sesionCaja, transCaja, sesionENS, transENS);
				if(sCCOR != null){
					sCUSD = dv.obtenerCuentaCaja(iCaid, sCodcomp, MetodosPagoCtrl.EFECTIVO, sMonedaForanea, sesionCaja, transCaja, sesionENS, transENS);
					if(sCUSD != null){
						
						//------------ Guardar los asientos.
						sAsientoSucursal = sCUSD[2];
						iMontoUSD = (int)(dv.roundDouble(dMontoFOR * 100));
						iMontoCOR = (int)(dv.roundDouble((dMontoFOR * dTasaJDE)*100));
						
						bHecho = rcCtrl.registrarAsientoDiario(dtFecha, cn, sAsientoSucursal, valoresJDEInsFCV[1], iNobatchNodoc[1], 1.0, 
																iNobatchNodoc[0], sCUSD[0],	sCUSD[1], sCUSD[3], sCUSD[4],sCUSD[5], 
																valoresJDEInsFCV[5], sMonedaForanea, iMontoCOR, sConcepto,vaut.getId().getLogin(),
																vaut.getId().getCodapp(), new BigDecimal(dTasaJDE), sTipoCliente,
																"Débito caja Efectivo "+sMonedaForanea,sCUSD[2],"","",sMonedaBase,sCUSD[2],valoresJDEInsFCV[4]);
						if(bHecho){
							bHecho = rcCtrl.registrarAsientoDiario(dtFecha,cn, sAsientoSucursal, valoresJDEInsFCV[1], iNobatchNodoc[1], 1.0, 
																	iNobatchNodoc[0], sCUSD[0], sCUSD[1], sCUSD[3], sCUSD[4], sCUSD[5],
																	valoresJDEInsFCV[2], valoresJDEInsFCV[3], iMontoUSD, sConcepto, vaut.getId().getLogin(),
																	vaut.getId().getCodapp(), BigDecimal.ZERO, sTipoCliente,
																	"Débito caja Efectivo "+sMonedaForanea,sCUSD[2],"","",sMonedaForanea,sCUSD[2],valoresJDEInsFCV[4]);
							if(bHecho){
								bHecho = rcCtrl.registrarAsientoDiario(dtFecha, cn, sAsientoSucursal, valoresJDEInsFCV[1], iNobatchNodoc[1], 2.0, 
																		iNobatchNodoc[0], sCCOR[0],	sCCOR[1], sCCOR[3], sCCOR[4], sCCOR[5], 
																		valoresJDEInsFCV[5], "USD", iMontoCOR*(-1), sConcepto, vaut.getId().getLogin(),
																		vaut.getId().getCodapp(), new BigDecimal(dTasaJDE),sTipoCliente,
																		"Crédito caja Efectivo "+sMonedaBase,sCCOR[2],"","",sMonedaBase,sCCOR[2],valoresJDEInsFCV[7]);
								if(bHecho){
									bHecho = rcCtrl.registrarAsientoDiario(dtFecha, cn, sAsientoSucursal,valoresJDEInsFCV[1],iNobatchNodoc[1], 2.0, 
																			iNobatchNodoc[0], sCCOR[0], sCCOR[1], sCCOR[3], sCCOR[4], sCCOR[5],
																			valoresJDEInsFCV[2], valoresJDEInsFCV[3], iMontoUSD*(-1), sConcepto, vaut.getId().getLogin(),
																			vaut.getId().getCodapp(),  BigDecimal.ZERO, sTipoCliente,
																			"Crédito caja Efectivo COR",sCCOR[2],"","",sMonedaBase,sCCOR[2],valoresJDEInsFCV[7]);
									if(bHecho){
										//--------Guardar el batch.
										//bHecho = rcCtrl.registrarBatch(cn,"G", iNobatchNodoc[0], iMontoUSD, vaut[0].getId().getLogin(), 1, MetodosPagoCtrl.DEPOSITO); VERSION A7.3
										bHecho = rcCtrl.registrarBatchA92(cn,"G", iNobatchNodoc[0], iMontoUSD, vaut.getId().getLogin(), 1, MetodosPagoCtrl.DEPOSITO);
										if(!bHecho){
											sMensaje = "Error al registrar el Batch para FCV de Primas y Reservas";
										}
									}else{
										sMensaje = "Error al insertar línea 2 CA de asientos para FCV";
									}
								}else{
									sMensaje = "Error al insertar línea 2 AA de asientos para FCV";
								}
							}else{
								sMensaje =  "Error al insertar línea 1 CA de asientos para FCV";
							}
						}else{
							sMensaje = "Error al insertar línea 1 AA de asientos para FCV";
						}
					}else{
						sMensaje =  "Error al obtener la cuenta de caja para efectivo dólares para FCV";
						bHecho = false;
					}
				}else{
					sMensaje =  "Error al obtener la cuenta de caja para efectivo córdobas para FCV";
					bHecho = false;
				}
			}else{
				sMensaje = "Error al obtener el número de batch y documento para el asiento diario de FCV";
				bHecho = false;
			}
			//--------- Guardar el enlace con MCAJA.
			if(bHecho){
				bHecho = rcCtrl.fillEnlaceMcajaJde(sesionCaja, transCaja, iNumFCV, sCodcomp, 
												   iNobatchNodoc[1], iNobatchNodoc[0], iCaid, 
												   sCodsuc, "A", "FCV");
				if(!bHecho){
					sMensaje = " Error al guardar enlace "+PropertiesSystem.CONTEXT_NAME+" - JDE para registro de FCV Batch: ";
					sMensaje += iNobatchNodoc[0]+" NoDoc. "+iNobatchNodoc[1]+", Caja: "+iCaid+ ", Suc: "; 
					sMensaje += sCodsuc+", Comp: "+ sCodcomp;
				}
			}
		} catch (Exception error) {
			bHecho = false;
			sMensaje =  " No se puede realizar operación Generar Asientos de FCV => Error de sistema: <br>";
			sMensaje += error +"<br>";
			sMensaje += error.getCause();
			m.put("MsgErrorJDE",sMensaje);
			error.printStackTrace();
		}
		return bHecho;
	}
/******************************************************************************************/
/** Método: Leer la tasa de cambio oficial a una fecha dada, y actualizar campos necesarios.
 *	Fecha:  15/06/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public boolean leerTasaJDExFecha(Date dtFecha){
		boolean bHecho = true;
		try {
			String sTasa = "";
			Tcambio[] tcambio = null;
			Object ob[] = null;
			ob = obtenerTasaCambioJDE(dtFecha);	
			if(ob == null){
				bHecho = false;
			}else{
				sTasa   = ob[0].toString();
				tcambio = (Tcambio[])ob[2];
				m.put("pr_lblTasaJde", sTasa);									//etiqueta de la tasa
				m.put("pr_valortasajde", new BigDecimal(ob[1].toString()));		//Monto de la tasa.
				m.put("pr_TasaJdeTcambio",tcambio );    						//objeto de Tcambios
			}
			lblTasaCambioJde2  = sTasa;
			lblTcambioJde2.setValue(sTasa); 
			SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
			srm.addSmartRefreshId(lblTcambioJde2.getClientId(FacesContext.getCurrentInstance()));
		} catch (Exception error) {
			bHecho = false;
			error.printStackTrace();
		}
		return bHecho;
	}
/*************************************************************************************/
/** Método: Determinar la cantidad de cambio mixto aplicado al pago del recibo
 *	Fecha:  15/06/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public void aplicarCambio(ActionEvent ev){
		String sCambio = "";
		Divisas dv = new Divisas();
		boolean valido = true;
		Pattern pNumero = null;
		Matcher matNumero = null;
		double dCambioSug = 0,dCambiodom = 0,dCambioApl=0,dTasaJDE = 0,dCambioValidar=0;
		String sMensaje = "";
		
		try {
			Object[] objConfigComp = (Object[])m.get("pr_OBJCONFIGCOMP");
			String sMonedaBase = String.valueOf(objConfigComp[1]);
			
			dTasaJDE = m.get("pr_valortasajde")!= null? Double.parseDouble(m.get("pr_valortasajde").toString()): 1.0000;
			txtCambioForaneo.setStyleClass("frmInput2");
			
			//-------- validar el monto ingresado.
			pNumero = Pattern.compile("^[0-9]+\\.?[0-9]*$");
			if(txtCambioForaneo.getValue()!= null){				
				dCambioSug =  dv.roundDouble( dv.formatStringToDouble(lblMontoRecibido2.getValue().toString()) -
							  				  dv.formatStringToDouble(txtMontoAplicar.getValue().toString()));
				
				if(ddlMonedaAplicada.getValue().toString().trim().equals(sMonedaBase)){
					dCambioValidar = dv.roundDouble(dCambioSug / dTasaJDE);
				}else
					dCambioValidar = dCambioSug;
				
				sCambio    = txtCambioForaneo.getValue().toString().trim();
				matNumero  = pNumero.matcher(sCambio);
			
				if(!sCambio.equals("") && matNumero.matches()){
					dCambioApl =  dv.formatStringToDouble(sCambio);
					if(dCambioApl < 0 || dCambioApl > dCambioValidar ){
						sMensaje = "El monto debe ser mayor que cero y menor al cambio sugerido!";
						valido = false;
					}
				}else{
					sMensaje = "El valor ingresado para el monto no es correcto!";
					valido = false;
				}
			}else {
				sMensaje = "Verifique el monto ingresado!";
				valido = false;
			}
			//------ si es válido, procesar el cambio.
			if(!valido){
				if(ddlMonedaAplicada.getValue().toString().equals(sMonedaBase))
					dCambioSug = dv.roundDouble(dCambioSug /dTasaJDE);
				
				txtCambioForaneo.setValue(dv.formatDouble(dCambioSug));
				txtCambioForaneo.setStyleClass("frmInput2Error");
				lblCambioDom.setValue("0.00");
				lblMensajeValidacion.setValue(sMensaje);
				dwProcesa.setStyle("width:320px;height: 150px");
				dwProcesa.setWindowState("normal");
			}
			else{	//--------- procesar el cambio 
				txtCambioForaneo.setValue(dv.formatDouble(dCambioApl));
				lblCambioDom.setValue("0.00");

				if(ddlMonedaAplicada.getValue().toString().equals(sMonedaBase)){
					dCambiodom = dv.roundDouble(dCambioSug - (dCambioApl*dTasaJDE));
				}else{
					dCambiodom = dv.roundDouble(dCambioSug - dCambioApl);
					dCambiodom = dv.roundDouble(dCambiodom * dTasaJDE);
				}
				lblCambioDom.setValue(dv.formatDouble(dCambiodom));
			}
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
/*************************************************************************************/
/** Método: Determinar el cambio obtenido en dependencia del pago y del monto aplicado
 *	Fecha:  15/06/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public void determinarCambio(List<MetodosPago> lstselectedMet, double dMontorecibido,double dMontoapl, String sMonedaApl){
		double dCambio = 0,dTasaJDE, dTasaP,dCambiodom = 0;
		Divisas dv = new Divisas();
		boolean bMonedaBase = false;
		
		try {
			//----- tasas
			dTasaP   = m.get("pr_valortasap")  != null? Double.parseDouble(m.get("pr_valortasap").toString()): 1.0000;
			dTasaJDE = m.get("pr_valortasajde")!= null? Double.parseDouble(m.get("pr_valortasajde").toString()): 1.0000;
			dCambio = dMontorecibido - dMontoapl;
			dCambio = dv.roundDouble(dCambio);
			
			lblCambioapl.setStyle("visibility: visible");
			txtCambioForaneo.setValue("");
			txtCambioForaneo.setStyle("visibility: hidden; width: 0px; display:none; text-align: right");
			lnkCambio.setStyle("visibility: hidden; width: 0px; display : none");
			lnkCambio.setIconUrl("");
			lnkCambio.setHoverIconUrl("");
			lbletCambioDom.setValue("");
			lblCambioDom.setValue("");
			lbletCambioDom.setStyle("visibility: hidden");
			lblCambioDom.setStyle("visibility: hidden");
			
			Object[] objConfigComp = (Object[])m.get("pr_OBJCONFIGCOMP");
			String sMonedaBase = String.valueOf(objConfigComp[1]);
			String sMonedaCambioFor = sMonedaBase;
			
			boolean hayEfectivo = false;
			boolean efectivoLocal = false;
			boolean efectivoExtr = false;
			boolean aplicadolocal = sMonedaApl.compareTo(sMonedaBase) == 0;
			
			for (MetodosPago mp : lstselectedMet) {
				if(mp.getMetodo().compareTo(MetodosPagoCtrl.EFECTIVO) == 0){
					hayEfectivo = true;
					efectivoLocal = mp.getMoneda().compareTo(sMonedaBase) == 0 ;
				}
			}
			if(hayEfectivo){
				bMonedaBase = (aplicadolocal && efectivoLocal)? true: false;
			}else{
				bMonedaBase = (aplicadolocal)? true : false;
			}
			
			if(lstselectedMet.size() == 0 && !Boolean.valueOf(String.valueOf(objConfigComp[3])))
				bMonedaBase = true;
			
			//-------------- Cambio mixto COR-USD
			for(int i=0; i<lstselectedMet.size(); i++){
				MetodosPago mp = (MetodosPago)lstselectedMet.get(i);
				if(mp.getMoneda().equals(sMonedaBase) && mp.getMetodo().equals(MetodosPagoCtrl.EFECTIVO)){
					bMonedaBase = true;
					break;
				}else{
					sMonedaCambioFor = mp.getMoneda().trim();
				}
			}
			
			if(bMonedaBase){
				dCambio = (sMonedaApl.equals(sMonedaBase))?
								dv.roundDouble(dCambio):
								dv.roundDouble(dCambio * dTasaP);
				
				String sColor = dCambio >= 0? "color: green":"color: red";
				lbletCambioapl.setValue("Cambio "+sMonedaBase+":");
				lblCambioapl.setStyle(sColor+ ";"  + " visibility: visible");
				lblCambioapl.setValue(dv.formatDouble(dCambio));
			}else{
				
				lbletCambioapl.setValue("Cambio "+sMonedaCambioFor+":");//cambio pago
				lbletCambioDom.setValue("Cambio "+sMonedaBase+ ":");//cambio base
				lbletCambioDom.setStyle("visibility: visible");
				
				if(dCambio<=0){
					if(sMonedaApl.equals(sMonedaBase/*"COR"*/)){ //compara moneda base
						dCambiodom = dCambio;
						dCambio = dv.roundDouble(dCambio /dTasaJDE)* (-1);
					}else{ //if(sMonedaApl.equals("USD")){
						dCambiodom = dv.roundDouble(dCambio * dTasaP  * (-1));
					}	
					lblCambioapl.setValue(dv.formatDouble(dCambio));
					lblCambioDom.setValue(dv.formatDouble(dCambiodom));
					lblCambioapl.setStyle("color: red; visibility: visible");
					lblCambioDom.setStyle("color: red; visibility: visible");
				}
				else{
					if(sMonedaApl.equals(sMonedaBase/*"COR"*/)){
						dCambio = dv.roundDouble(dCambio/dTasaJDE);
					}
					txtCambioForaneo.setValue(dv.formatDouble(dCambio));
					lblCambioapl.setValue("");
					lblCambioapl.setStyle("visibility: hidden");
					txtCambioForaneo.setStyle("visibility: visible; width: 65px;display:inline;text-align: right");
					lnkCambio.setStyle("visibility: visible; width: 16px");
					lnkCambio.setIconUrl("/theme/icons/RefreshWhite.gif");
					lnkCambio.setHoverIconUrl("/theme/icons/RefreshWhiteOver.gif");
					lbletCambioDom.setValue("Cambio "+sMonedaBase+":");
					lblCambioDom.setValue("0.00");
					lbletCambioDom.setStyle("visibility: visible");
					lblCambioDom.setStyle("visibility: visible");
					lblCambioDom.setStyle("color: green; visibility: visible");
					lblCambioDom.setValue("0.00");
				}
			}
			if(dCambio == 0)
				lblCambioapl.setStyle("color: black");

			SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
			srm.addSmartRefreshId(lblCambioapl.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lbletCambioapl.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lbletCambioDom.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblCambioDom.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(txtCambioForaneo.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lnkCambio.getClientId(FacesContext.getCurrentInstance()));
			
		} catch (Exception error) {
			error.printStackTrace();
		}
	}	
	
/************************************************************************/
/** Método: Fijar el monto aplicado para el recibo y determinar su cambio
 *	Fecha:  14/06/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public void fijarMontoAplicado(ActionEvent ev){
		Pattern pNumero;
		Matcher matNumero = null;
		String sMonto = "";
		double monto = 0,dTparalela=0;
		Divisas dv = new Divisas();
		boolean validado = true;
		Vf55ca01 vf01 = null;		
		try {
			//obtener datos de Caja
			vf01 = (Vf55ca01) ((List) m.get("lstCajas")).get(0);
			//------- Validar los datos del monto.
			txtMontoAplicar.setStyleClass("frmInput2");
			pNumero = Pattern.compile("^[0-9]+\\.?[0-9]*$");
			sMonto = txtMontoAplicar.getValue().toString();
			matNumero = pNumero.matcher(sMonto);
			
			if(sMonto.equals("")){ //---- Liberar monto aplicado.
				m.remove("pr_MontoAplicado");
				txtMontoAplicar.setValue(lblMontoRecibido2.getValue().toString());
				lblCambioapl.setValue("0.00");
				lblCambioapl.setStyle("color: black");
				lbletCambioDom.setStyle("visibility: hidden");
				lblCambioDom.setStyle("visibility: hidden");
			}
			else{ //-------- Validar y fijar el monto aplicado.
				if(matNumero.matches())
					monto = dv.roundDouble(Double.parseDouble(sMonto));
				if (matNumero == null || !matNumero.matches() || monto == 0){
					validado = false;
					txtMontoAplicar.setValue("");
					txtMontoAplicar.setStyleClass("frmInput2Error");
					lblMensajeValidacion.setValue("El valor del monto ingresado no es correcto!");
					dwProcesa.setStyle("width:320px;height: 150px");
					dwProcesa.setWindowState("normal");
				}
				if(validado){
					//----- Limpiar los pagos y fijar el monto.
					selectedMet = new ArrayList();
					m.remove("pr_selectedMet");
		    		m.put("pr_selectedMet", selectedMet);
				
					m.remove("pr_lstSolicitud");
					cmbMetodosPago.dataBind();
					metodosGrid.dataBind();
					resetResumenPago(null);
					cambiarVistaMetodos("MP",vf01);
					txtMontoAplicar.setValue(dv.formatDouble(monto));
					m.put("pr_MontoAplicado",monto);
					
					lblCambioapl.setValue(dv.formatDouble(monto * (-1)));
					lblCambioapl.setStyle("color: red");
					
					//---- Determinar la moneda base.
					Object[]objConfigComp = (Object[])m.get("pr_OBJCONFIGCOMP");
					String sMonedaBase = String.valueOf(objConfigComp[1]);
					
					if(!ddlMonedaAplicada.getValue().toString().trim().equals(sMonedaBase)){
						
						//---- Determinar la tasa a utilizar.
						if(cmbTiporecibo.getValue().toString().equals("MANUAL")){
							Date dtFecha =  (Date)txtFecham.getValue();
							Object[]  ob = obtenerTasaCambioJDE(dtFecha);
							if(ob == null){
								lblMensajeValidacion.setValue("No se encuentra configurada la tasa de cambio paralela a la fecha seleccionada!");
								dwProcesa.setStyle("width:320px;height: 150px");
								dwProcesa.setWindowState("normal");
								validado = false;
							}else{
								String sTasa = "1.0";
								dTparalela = (new BigDecimal(ob[1].toString())).doubleValue();
								m.put("pr_lblTasaJde", sTasa);				//etiqueta de la tasa
								m.put("pr_valortasajde", dTparalela);		//Monto de la tasa.
								m.put("pr_TasaJdeTcambio", ((Tcambio[])ob[2]) );		//objeto de Tcambios
								lblTasaCambioJde2  = sTasa;
								lblTcambioJde2.setValue(sTasa);
							}
						}else{
							if(m.get("pr_valortasap")  == null){
								lblMensajeValidacion.setValue("No se encuentra configurada la tasa de cambio paralela!");
								dwProcesa.setStyle("width:320px;height: 150px");
								dwProcesa.setWindowState("normal");
								validado = false;
							}else{
								dTparalela  = Double.parseDouble(m.get("pr_valortasap").toString());
							}
						}
						if(validado){
							lbletCambioDom.setStyle("visibility: visible");
							lblCambioDom.setStyle("visibility: visible; color: red");
							monto = dv.roundDouble(monto * dTparalela  * (-1));
							lblCambioDom.setValue(dv.formatDouble(monto));
							lbletCambioDom.setValue("Cambio "+sMonedaBase+":");
						}
					}
				}
			}
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
/***********************************************************/
/** Método: Restablecer los campos para el resumen de pago
 *	Fecha:  01/06/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public void resetResumenPago(SmartRefreshManager srm ){
		try {
			
			m.remove("pr_MontoAplicado");
			
			String sMon = ddlMonedaAplicada.getValue().toString();
			lblMontoAplicar.setValue("Aplicado "  + sMon+ ":");
			lblMontoRecibido.setValue("Recibido " + sMon+ ":");
			lbletCambioapl.setValue("Cambio " + sMon + ":");
			lbletCambioDom.setValue("Cambio COR:");
			
			txtMontoAplicar.setValue("0.00");
			lblMontoRecibido2.setValue("0.00");
			lblCambioapl.setValue("0.00");
			lblCambioDom.setValue("0.00");
			txtCambioForaneo.setValue("");
			
			lblMontoAplicar.setStyleClass("frmLabel2");
			lblMontoRecibido.setStyleClass("frmLabel2");
			lbletCambioapl.setStyleClass("frmLabel2");
			lbletCambioDom.setStyleClass("frmLabel2");
			lbletCambioDom.setStyle("visibility: hidden");
			
			txtMontoAplicar.setStyleClass("frmInput2");
			txtCambioForaneo.setStyleClass("frmInput2");
			lblMontoRecibido2.setStyleClass("frmLabel3");
			lblCambioapl.setStyleClass("frmLabel3");
			lblCambioapl.setStyle("color: black; visibility: visible");
			lblCambioDom.setStyleClass("frmLabel3");
			lblCambioDom.setStyle("color: black; visibility: hidden");
			txtCambioForaneo.setStyle("visibility: hidden; width: 0px;  display:none; text-align: right");
			lnkCambio.setStyle("visibility: hidden; width: 0px; display: none");
			lnkCambio.setIconUrl("");
			lnkCambio.setHoverIconUrl("");
			lbletCambioDom.setValue("");
			lblCambioDom.setValue("");
			
			
			CodeUtil.refreshIgObjects(new Object[] { lblMontoAplicar,
					lblMontoRecibido, lblMontoRecibido, lbletCambioapl,
					lbletCambioDom, lbletCambioDom, txtMontoAplicar,
					lblMontoRecibido2, lblCambioapl, lblCambioDom,
					txtCambioForaneo, lnkCambio

			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void selecionarMonedaAplicar(ValueChangeEvent ev){
		Vf55ca01 vf01 = null;
		try {
			//obtener datos de Caja
			vf01 = (Vf55ca01) ((List) m.get("lstCajas")).get(0);
			SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();	
			 restablecerEstilosPago();
			 cambiarVistaMetodos("MP",vf01);
			 resetResumenPago(srm);
			 
			 m.remove("pr_lstMetodosPago");
			 m.remove("pr_selectedMet");
			 m.remove("pr_met");
			 m.remove("pr_lstSolicitud");
			 m.remove("pr_MontoAplicado");
			
			 selectedMet = new ArrayList();
			 m.put("pr_selectedMet",selectedMet);
			 metodosGrid.dataBind();

		} catch (Exception error) {
			error.printStackTrace();
		}
	}
	
	
	/*******************************************************************************/
	/** Establecer los datos del cliente filtrado para usar en el recibo **/
	public void actualizarInfoCliente(ActionEvent ev) {

		try {

			m.remove("pr_idNumericPago");

			lblCodigoSearch.setValue(" ");
			lblNombreSearch.setValue(" ");

			String sParametro = txtParametro.getValue().toString();

			if (sParametro.trim().compareTo("") == 0
					|| sParametro.length() <= 3 || !sParametro.contains("=>")) {
				lblCodigoSearch.setValue(" ");
				lblNombreSearch.setValue(" ");
				return;
			}

			String sCliente[] = sParametro.split("=>");
			lblCodigoSearch.setValue(sCliente[0].trim() + " ");
			lblNombreSearch.setValue(sCliente[1].trim() + " ");
			lblCodigoSearch.setStyleClass("frmLabel2");
			lblNombreSearch.setStyleClass("frmLabel2");

			// && ========== Generar numero aleatorio para registro del recibo.
			long idNumericPago = (long) new Random().nextInt(100000000);
			m.put("pr_idNumericPago", idNumericPago);
			 
			
		} catch (Exception error) { 
			error.printStackTrace();
		}
	}

/*******************************************************************************/
/** Cambiar la tasa oficial (JDE) para la fecha seleccionada en recibo manual **/
	public void cambiarTasajdexFecha(ActionEvent ev){
		Date dtFecha;
		Object ob[] = null;
		String sTasa = "1.0",sTipo = "";
		Tcambio[] tcambio = null;
		Vf55ca01 vf01 = null;
		try {
			//obtener datos de Caja
			vf01 = (Vf55ca01) ((List) m.get("lstCajas")).get(0);
			sTipo = cmbTiporecibo.getValue().toString();
			if(sTipo.equals("MANUAL")) {
				dtFecha =  (Date)txtFecham.getValue();
				ob = obtenerTasaCambioJDE(dtFecha);
				if(ob == null){
					lblMensajeValidacion.setValue("No se encuentra configurada la tasa de cambio oficial a la fecha!");
					dwProcesa.setStyle("width:320px;height: 150px");
					dwProcesa.setWindowState("normal");
				}else{
					sTasa =  ob[0].toString();
					tcambio = (Tcambio[])ob[2];
					m.put("pr_lblTasaJde", sTasa);									//etiqueta de la tasa
					m.put("pr_valortasajde", new BigDecimal(ob[1].toString()));		//Monto de la tasa.
					m.put("pr_TasaJdeTcambio", tcambio);    						//objeto de Tcambios
					m.put("pr_tjdeRmanual",new BigDecimal(ob[1].toString()));		//Bandera de paso sobre el método.
					
					m.get("pr_lstMetodosPago");					
					cambiarVistaMetodos("MP",vf01);
					resetResumenPago(null);
				}
				lblTasaCambioJde2  = sTasa;
				lblTcambioJde2.setValue(sTasa);
			}
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
/*******************************************************************************/
/**   Leer la tasa de cambio oficial (JDE) para una fecha específica	      **/	
	public Object[] obtenerTasaCambioJDE(Date dtFecha) {
		TasaCambioCtrl tcCtrl ;
		SimpleDateFormat sdf;
		String sFecha,sTasa=" ";
		Tcambio[] tcambio;
		Object obj[] = null;
		
		try {	
			
			tcCtrl = new TasaCambioCtrl();
			sdf = new SimpleDateFormat("yyyy-MM-dd");
			BigDecimal dbValorTjde = BigDecimal.ONE;
			dtFecha = dtFecha == null? new Date(): dtFecha;
			sFecha = sdf.format(dtFecha);
			tcambio = tcCtrl.obtenerTasaCambioJDExFecha(sFecha);
			
			if(tcambio == null){
				obj = null;
			}else{
				obj = new Object[3];
				for(int l = 0; l < tcambio.length;l++){
					dbValorTjde = tcambio[l].getId().getCxcrrd();
					sTasa = sTasa + " " + tcambio[l].getId().getCxcrdc() + ": " + dbValorTjde;
				}
				obj[0] = sTasa;			//etiqueta de la tasa
				obj[1] = dbValorTjde;	//Monto de la tasa.
				obj[2] = tcambio;  		//objeto de Tcambios
			}
		} catch (Exception error) {
			LogCajaService.CreateLog("obtenerTasaCambioJDE", "ERR", error.getMessage());
		}
		return obj;
	}
	
/************************************************************************************************************/
/**				Guardar Diferencial cambiario en 1 solo batch con todos los métodos de pago 			   **/
	public boolean guardarDC(int iNumrec, List lstDifCamb,int iCaid, String sCodsuc,String sCodcomp,String sCodunineg,
			Vautoriz[] vaut,Vf55ca01 f55ca01, Session sesENS,Session sesCaja,Transaction transENS,
			Transaction transCaja,Connection cn){
		boolean bHecho = true;
		String sCtaDC[],sCtaMpago[],sTipoCliente = "",sConcepto = "";
		int iNobatchNodoc[], iNoBatch = 0, iNoDocumento = 0,iMonto = 0;
		double dMontoTotal = 0,dMonto = 0,dLinea1 = -1.0, dLinea2 = 0.0 ;
		Date dtFecha = new Date();
		Divisas dv = new Divisas();
		ReciboCtrl rcCtrl = new ReciboCtrl();
		EmpleadoCtrl emCtrl = new EmpleadoCtrl();
		
		try {
			sConcepto = "Dif/Camb. Rec:"+iNumrec+ " Ca:"+iCaid +" Co:"+sCodcomp;			
			//-------- Obtener el tipo de cliente.
			sTipoCliente = emCtrl.determinarTipoCliente(f55ca01.getId().getCaan8());
			//------- obtener la cuenta de caja para el diferencial cambiario.
			sCtaDC = dv.obtenerCuentaDifCambiario(iCaid, sCodcomp, sCodunineg, sesCaja, transCaja, sesENS, transENS);
			
			if(sCtaDC !=null){
				//------- obtener el número de batch y número de documento.
				iNobatchNodoc = dv.obtenerNobatchNodoco();
				if(iNobatchNodoc == null){
					m.put("MsgErrorJDE", "Error al intentar obtener el número de batch y documento para registro de diferencial cambiario");
					bHecho = false;
				}
				else{
					iNoBatch =iNobatchNodoc[0];
					iNoDocumento = iNobatchNodoc[1];
				}
				if(bHecho){
					//-------- recorrer la lista de métodos que generaron diferencial cambiario.
					for(int i=0; i<lstDifCamb.size(); i++){
						String sMet="";
						Object oDifcam[] = (Object[])lstDifCamb.get(i);
						dMonto 	  =  Double.parseDouble(oDifcam[0].toString());
						dMontoTotal += dMonto;
						sCtaMpago =  (String[])oDifcam[1];
						sMet 	  = oDifcam[2].toString();
						dLinea1 += 2;
						dLinea2 += 2;
						
						//---------- Registrar los asientos de diario.
						iMonto = (int)dv.roundDouble(dMonto * 100);
						bHecho  =  rcCtrl.registrarAsientoDiario(dtFecha, cn, sCodsuc.substring(3), valoresJDEInsFCV[1], iNoDocumento, dLinea1, iNoBatch, sCtaMpago[0],
									sCtaMpago[1], sCtaMpago[3], sCtaMpago[4], sCtaMpago[5], valoresJDEInsFCV[5], valoresJDEInsFCV[6], iMonto, sConcepto,vaut[0].getId().getLogin(),
									vaut[0].getId().getCodapp(),BigDecimal.ZERO, sTipoCliente,"DebCa: Mpago: "+ sMet,sCtaMpago[2],
									"","",valoresJDEInsFCV[6],sCtaMpago[2],valoresJDEInsFCV[7]);
						if(bHecho){
							bHecho  =  rcCtrl.registrarAsientoDiario(dtFecha, cn, sCodsuc.substring(3),valoresJDEInsFCV[1], iNoDocumento, dLinea2, iNoBatch, sCtaDC[0],
										sCtaDC[1], sCtaDC[3], sCtaDC[4], sCtaDC[5], valoresJDEInsFCV[5], valoresJDEInsFCV[6], iMonto*-1, sConcepto,vaut[0].getId().getLogin(),
										vaut[0].getId().getCodapp(),BigDecimal.ZERO, sTipoCliente,"Cred. D/C Caja: Mpago: "+ sMet,
										sCtaDC[2],"","",valoresJDEInsFCV[6],sCtaMpago[2],valoresJDEInsFCV[7]);
							if(!bHecho){
								m.put("MsgErrorJDE", "No se pudo registrar el asiento diario, linea 2 para Diferencial Cambiario en Caja:" +iCaid + ", Comp: "+sCodcomp + ", Unineg: " +sCodunineg);
								break;
							}
						}else{
							m.put("MsgErrorJDE", "No se pudo registrar el asiento diario, linea 1 para Diferencial Cambiario en Caja:" +iCaid + ", Comp: "+sCodcomp + ", Unineg: " +sCodunineg);
							break;
						}
					}
					if(bHecho){
						//-------- guardar el batch para el asiento diario.
						iMonto = (int)dv.roundDouble(dMontoTotal * 100);
						//bHecho = rcCtrl.registrarBatch(cn,"G", iNoBatch, iMonto, vaut[0].getId().getLogin(), 1, MetodosPagoCtrl.DEPOSITO); VERSION A7.3
						bHecho = rcCtrl.registrarBatchA92(cn,"G", iNoBatch, iMonto, vaut[0].getId().getLogin(), 1, MetodosPagoCtrl.DEPOSITO);
						if(bHecho){
							bHecho = rcCtrl.fillEnlaceMcajaJde(sesCaja, transCaja, iNumrec, sCodcomp, iNoDocumento, iNoBatch, iCaid, sCodsuc, "A", "PR");
							if(!bHecho){
								m.put("MsgErrorJDE","Error al guardar enlace "+PropertiesSystem.CONTEXT_NAME+" - JDE para registro de Diferencial cambiario con batch"+ iNoBatch+" NoDoc. "+iNoDocumento+", Caja: "+iCaid+ ", Suc: " + sCodsuc+", Comp: "+sCodcomp);
							}
						}else{
							m.put("MsgErrorJDE","Error al guadar el registro del depósito para el batch"+ iNoBatch +" Caja: "+iCaid+ ", U/N: " + sCodunineg+", Comp: "+sCodcomp);
						}
					}
				}
			}else{
				m.put("MsgErrorJDE", "Error al intentar obtener la cuenta de diferencial cambiario para caja " +iCaid + ", Comp: "+sCodcomp + ", Unineg: " +sCodunineg);
				bHecho = false;
			}
		} catch (Exception error) {
			error.printStackTrace();
		}
		return bHecho;
	}
/*******************************************************************************/
/**    Registrar los asientos de diario para los diferenciales cambiarios	  **/
	public boolean guardarDifCambiario(int iNumrec, List lstDifCamb,int iCaid, String sCodsuc,String sCodcomp,String sCodunineg,
										Vautoriz[] vaut,Vf55ca01 f55ca01, Session sesENS,Session sesCaja,Transaction transENS,
										Transaction transCaja,Connection cn){
		boolean bHecho = true;
		String sCtaDC[],sCtaMpago[],sTipoCliente = "",sConcepto = "";
		int iNobatchNodoc[], iNoBatch = 0, iNoDocumento = 0,iMonto = 0;
		double dMonto = 0;
		Date dtFecha = new Date();
		
		Divisas dv = new Divisas();
		ReciboCtrl rcCtrl = new ReciboCtrl();
		EmpleadoCtrl emCtrl = new EmpleadoCtrl();
		
		try {
			sConcepto = "Diferencial Cambiario Caja;"+iCaid; 
			
			//-------- Obtener el tipo de cliente.
			sTipoCliente = emCtrl.determinarTipoCliente(f55ca01.getId().getCaan8());
			
			//------- obtener la cuenta de caja para el diferencial cambiario.
			sCtaDC = dv.obtenerCuentaDifCambiario(iCaid, sCodcomp, sCodunineg, sesCaja, transCaja, sesENS, transENS);
			if(sCtaDC !=null){
				
				//-------- recorrer la lista de métodos que generaron diferencial cambiario.
				for(int i=0; i<lstDifCamb.size(); i++){
					
					//------- obtener el número de batch y número de documento.
					iNobatchNodoc = dv.obtenerNobatchNodoco();
					if(iNobatchNodoc == null){
						m.put("MsgErrorJDE", "Error al intentar obtener el número de batch y documento para registro de diferencial cambiario");
						bHecho = false;
					}
					else{
						iNoBatch =iNobatchNodoc[0];
						iNoDocumento = iNobatchNodoc[1];
					}
					if(bHecho){
						String sMet="";
						Object oDifcam[] = (Object[])lstDifCamb.get(i);
						dMonto 	  =  Double.parseDouble(oDifcam[0].toString());
						sCtaMpago =  (String[])oDifcam[1];
						sMet 	  = oDifcam[2].toString();
						
						//---------- Registrar los asientos de diario.
						iMonto = (int)dv.roundDouble(dMonto * 100);
						bHecho  =  rcCtrl.registrarAsientoDiario(dtFecha, cn, sCodsuc.substring(3),valoresJDEInsFCV[1], iNoDocumento, 1.0, iNoBatch, sCtaMpago[0],
									sCtaMpago[1], sCtaMpago[3], sCtaMpago[4], sCtaMpago[5], valoresJDEInsFCV[5], valoresJDEInsFCV[6], iMonto, sConcepto,vaut[0].getId().getLogin(),
									vaut[0].getId().getCodapp(),BigDecimal.ZERO, sTipoCliente,"Deb.Caja: Mpago: "+ sMet,
									sCtaMpago[2],"","",valoresJDEInsFCV[6],sCtaMpago[2],valoresJDEInsFCV[7]);
						if(bHecho){
							bHecho  =  rcCtrl.registrarAsientoDiario(dtFecha, cn, sCodsuc.substring(3), valoresJDEInsFCV[1], iNoDocumento, 2.0, iNoBatch, sCtaDC[0],
										sCtaDC[1], sCtaDC[3], sCtaDC[4], sCtaDC[5], valoresJDEInsFCV[5], valoresJDEInsFCV[6], iMonto*-1, sConcepto,vaut[0].getId().getLogin(),
										vaut[0].getId().getCodapp(),BigDecimal.ZERO, sTipoCliente,"Cred.Cta D/C Caja: Mpago: "+ sMet,
										sCtaDC[2],"","",valoresJDEInsFCV[6],sCtaMpago[2],"D");
							if(bHecho){
								//-------- guardar el batch para el asiento diario.
								//bHecho = rcCtrl.registrarBatch(cn,"G", iNoBatch, iMonto, vaut[0].getId().getLogin(), 1, MetodosPagoCtrl.DEPOSITO); VERSION A 7.3
								bHecho = rcCtrl.registrarBatchA92(cn,"G", iNoBatch, iMonto, vaut[0].getId().getLogin(), 1, MetodosPagoCtrl.DEPOSITO);
								if(bHecho){
									bHecho = rcCtrl.fillEnlaceMcajaJde(sesCaja, transCaja, iNumrec, sCodcomp, iNoDocumento, iNoBatch, iCaid, sCodsuc, "A", "PR");
									if(!bHecho){
										m.put("MsgErrorJDE","Error al guardar enlace "+PropertiesSystem.CONTEXT_NAME+" - JDE para registro de Diferencial cambiario con batch"+ iNoBatch+" NoDoc. "+iNoDocumento+", Caja: "+iCaid+ ", Suc: " + sCodsuc+", Comp: "+sCodcomp);
									}
								}else{
									m.put("MsgErrorJDE","Error al guadar el registro del depósito para el batch"+ iNoBatch +" Caja: "+iCaid+ ", U/N: " + sCodunineg+", Comp: "+sCodcomp);
									break;
								}
							}
							else{
								m.put("MsgErrorJDE", "No se pudo registrar el asiento diario, linea 2 para Diferencial Cambiario en Caja:" +iCaid + ", Comp: "+sCodcomp + ", Unineg: " +sCodunineg);
								break;
							}
						}else{
							m.put("MsgErrorJDE", "No se pudo registrar el asiento diario, linea 1 para Diferencial Cambiario en Caja:" +iCaid + ", Comp: "+sCodcomp + ", Unineg: " +sCodunineg);
							break;
						}
					}
				}
			}else{
				m.put("MsgErrorJDE", "Error al intentar obtener la cuenta de diferencial cambiario para caja " +iCaid + ", Comp: "+sCodcomp + ", Unineg: " +sCodunineg);
				bHecho = false;
			}
		} catch (Exception error) {
			bHecho = false;
			m.put("MsgErrorJDE", "Error al registrar batchs para diferencial cambiario: Error de sistema: "+error) ;
			error.printStackTrace();
		}
		return bHecho;
	}
	
	
	
/*******************************************************************************/
/** Proceso de registrar transacciones contables en JDE. para el RU de primas  */
	public boolean realizarTransJDE(int iNumrec, Date dtFecha,String sMonRU,int iCaid,String sCodsuc,String sCodcomp, List lstMpagos,int iCodcli,
									String sCodunineg,
									Connection cn,Session sesCaja,Session sesENS,Transaction transCaja,Transaction transENS){
		boolean bHecho = true,bDifCam=false;
		int iNoBatch = 0, iNoRpdoc=0, iNoRpdocm=0,iFechaActual=0;
	
		long iMtoTotaltrans = 0 ;
		long iMtodomes = 0 ;
		long iMtoForaneo = 0;
		 
		String sCtaMpago[],sIdctampago,sFecha,sTasa ="",sMonUnineg="";
		String sMonMpago="",sIdmpago="",sConcepto="",sTipocont = "",iNocontrato = "";
		double dMtoTotaltrans, dTasaOf = 0.0,dMtoMpago=0.0,dMtoequiv=0.0;
		SimpleDateFormat format ;
		BigDecimal dbValorTjde = BigDecimal.ONE;
		List lstDifCamb = new ArrayList();
		Object oDifcam[]  = null;
		double dMontodif = 0;
		
		Vautoriz[] vaut;
		Tcambio[] tcambio;
		TasaCambioCtrl tcCtrl;
		CalendarToJulian fecha;
		Divisas dv = new Divisas();
		ReciboCtrl rcCtrl = new ReciboCtrl();
		String sTipodoc = "";
		try {
			//---numero de doc
			sTipodoc = ddlTipodoc.getValue().toString();
			if(sTipodoc.equals(""))sTipodoc = "RU";
			
			
			//&& ======== numero de contrato
			if(m.get("noContrato") != null){
				iNocontrato = m.get("noContrato").toString();
				sTipocont = "LC";
			}
			if(CodeUtil.getFromSessionMap("pr_NumeroProformaRepuesto") != null){
				iNocontrato = String.valueOf( CodeUtil.getFromSessionMap( "pr_NumeroProformaRepuesto" ) );
				sTipocont = "JF";
			}
			
			//--------------- Datos de la caja.
			vaut = (Vautoriz[]) m.get("sevAut");
			sMonUnineg = ddlMonedaAplicada.getValue().toString();
			fecha  = new CalendarToJulian(Calendar.getInstance());
			iFechaActual = fecha.getDate();
			sConcepto = "Rec:"+iNumrec + " Ca:"+iCaid+" Co:"+sCodcomp + " Met:";
			Object[] objConfigComp = (Object[])m.get("pr_OBJCONFIGCOMP");
			String sMonedaBase = String.valueOf(objConfigComp[1]);
			
			//--------------- Obtener la tasa oficial.
			format = new SimpleDateFormat("yyyy-MM-dd");
			tcCtrl = new TasaCambioCtrl();
			sFecha = format.format(dtFecha);
			tcambio = tcCtrl.obtenerTasaCambioJDExFecha(sFecha);
			if(tcambio == null){
				m.put("pr_MensajetasaJde", "No se encuentra configurada la tasa de cambio jde");
				sTasa = "1.0";
				bHecho = false;
			}else{
				for(int l = 0; l < tcambio.length;l++){
					dbValorTjde = tcambio[l].getId().getCxcrrd();
					sTasa = sTasa + " " + tcambio[l].getId().getCxcrdc() + ": " + dbValorTjde;
				}
				dTasaOf = dbValorTjde.doubleValue();
			}
			
			//--------------- Guardar el batch para el ru.
			dMtoTotaltrans = dv.formatStringToDouble(txtMontoAplicar.getValue().toString());
			dMtoTotaltrans = dv.roundDouble(dMtoTotaltrans);
			iMtoTotaltrans = (long)(dv.roundDouble(dMtoTotaltrans *100));
			
			iNoBatch = dv.leerActualizarNoBatch();
			if(iNoBatch == -1)
				bHecho = false;
			
			
			CodeUtil.putInSessionMap("pr_NumeroBatchEnRecibo", iNoBatch);
			
				
			if(bHecho){

				bHecho = rcCtrl.registrarBatchA92(cn, "R", iNoBatch, iMtoTotaltrans,  vaut[0].getId().getLogin(), 1, "");
				
				if(bHecho){
					
					//------------ Obtener Datos generales para los RU de cada método de pago. 
					//------------ Leer el número de RPDOCM a utilizar en el RU.
					iNoRpdocm = rcCtrl.leerNumeroRpdocm(true,sCodcomp);
					if(iNoRpdocm >= 0){
						String sReciboSuc="";
						
						//------- Con la moneda de UN, determinar la generación de registros para el RU.
						if(sMonUnineg.equals(sMonedaBase)){
							
							//----------- recorrer la lista de métodos de pago agregados.
							for(int i=0; i<lstMpagos.size();i++){
								MetodosPago mp = ((MetodosPago)lstMpagos.get(i));
								sMonMpago = mp.getMoneda();
								sIdmpago  = mp.getMetodo();
								dMtoMpago = mp.getMonto();
								dMtoequiv = mp.getEquivalente();
								
								sCtaMpago = dv.obtenerCuentaCaja(iCaid, sCodcomp, sIdmpago, sMonMpago, sesCaja, transCaja, sesENS, transENS);
								if(sCtaMpago != null){
									
									//-------Seleccionar la sucursal de la unidad de negocio del primer metodo de pago.
									if(i==0)
										//sReciboSuc = "000" + sCtaMpago[2];
										sReciboSuc = CodeUtil.pad(sCtaMpago[2], 5, "0");
									
									//---------- leer el número de RPDOC a utilizar en el registro.
									iNoRpdoc = rcCtrl.leerNumeroRpdoc(true);
									if(iNoRpdoc>=0){
										sIdctampago = sCtaMpago[1];
									
										long montoReciboF = (long)( dMtoequiv * 100) * -1 ;
										long montoReciboD = (long)( dMtoequiv * 100) * -1 ;
										
										//---------- Primera línea del RU.
										bHecho = rcCtrl.guardarRU(cn, sReciboSuc, iCodcli, sTipodoc, iNoRpdoc,"RC", iNoRpdocm, iFechaActual,iNoBatch, "","P",
													montoReciboF, 0, "D", sMonUnineg, 0, 0,	0, sIdctampago, sCodunineg,
													(sConcepto + sIdmpago),vaut[0].getId().getLogin(),vaut[0].getId().getNomapp(), iNocontrato, sTipocont);
										if(bHecho){
											//---------- Segunda línea del RU.
											bHecho = rcCtrl.guardarRU(cn, sReciboSuc, iCodcli, sTipodoc, iNoRpdoc,"",	0, 0, iNoBatch, "D","A",
													montoReciboF, montoReciboD, "D", sMonUnineg, 0, 0, 0, "", sCodunineg,
													(sConcepto + sIdmpago),vaut[0].getId().getLogin(),vaut[0].getId().getNomapp(),iNocontrato,sTipocont);
											if(!bHecho){
												m.put("MsgErrorJDE","Error al registrar la línea 2 del registro RU de recibo de primas para el método: "+sIdmpago + " "+sMonMpago);
												break;
											}
										}else{
											m.put("MsgErrorJDE","Error al registrar la línea 1 del RU de recibo de primas para el método: "+sIdmpago + " "+sMonMpago);
											break;
										}
									}else{
										bHecho = false;
										m.put("MsgErrorJDE","Error, no se ha podido leer el número de documento RPDOC para el registro RU del recibo de primas");
										break;
									}
								}else{
									bHecho = false;
									m.put("MsgErrorJDE","Error al obtener la cuenta de caja para el método "+ sIdmpago+" "+sMonRU+", Caja: "+iCaid+ ", Suc: " + sCodsuc+", Comp: "+sCodcomp);
									break;
								}
							}
						}else{ // fin moneda COR if(sMonUnineg.equals("USD")){
 							
 							//----------- recorrer la lista de métodos de pago agregados.
							for(int i=0; i<lstMpagos.size();i++){
								MetodosPago mp = ((MetodosPago)lstMpagos.get(i));
								sMonMpago = mp.getMoneda();
								sIdmpago  = mp.getMetodo();
								dMtoMpago = mp.getMonto();
								dMtoequiv = mp.getEquivalente();
								bDifCam = false;
								
								//-------- obtener la cuenta de caja del método.
								sCtaMpago = dv.obtenerCuentaCaja(iCaid, sCodcomp, sIdmpago, sMonMpago, sesCaja, transCaja, sesENS, transENS);
								if(sCtaMpago != null){
									
									//-------Seleccionar la sucursal de la unidad de negocio del primer metodo de pago.
									if(i==0)
										//sReciboSuc = "000" + sCtaMpago[2];
										sReciboSuc = CodeUtil.pad(sCtaMpago[2], 5, "0");
									
									//---------- leer el número de RPDOC a utilizar en el registro.
									iNoRpdoc = rcCtrl.leerNumeroRpdoc(true);
									if(iNoRpdoc>=0){
										sIdctampago = sCtaMpago[1];
										
										//--------- obtener los montos enteros para el RU.
										if(sMonMpago.equals(sMonedaBase)){
											iMtodomes =   (long)dv.roundDouble(dMtoequiv*dTasaOf*100*-1);
											iMtoForaneo = (long)dv.roundDouble(dMtoequiv*100*-1);
											
											//------ calcular el diferencial cambiario.
											bDifCam = true;
											oDifcam  = new Object[3];
											dMontodif = 0; 
											
											dMontodif = dv.roundDouble(dMtoMpago - (dMtoequiv*dTasaOf)); 
											oDifcam[0] = dMontodif;
											oDifcam[1] = sCtaMpago;
											oDifcam[2] = mp.getMetodo();
											lstDifCamb.add(oDifcam);
											
										}else{//if(sMonMpago.equals("USD")){
											iMtodomes =   (long)dv.roundDouble(dMtoMpago*dTasaOf*100*-1);
											iMtoForaneo = (long)dv.roundDouble(dMtoMpago*100*-1);
										}
										
										//---------- Primera línea del RU.
										bHecho = rcCtrl.guardarRU(cn, sReciboSuc, iCodcli, sTipodoc, iNoRpdoc,"RC", iNoRpdocm, iFechaActual,iNoBatch, 
													"", "P", iMtodomes, 0, "F", sMonUnineg, dTasaOf, iMtoForaneo, 0, sIdctampago, sCodunineg,
													(sConcepto + sIdmpago),vaut[0].getId().getLogin(),vaut[0].getId().getNomapp(),iNocontrato,sTipocont);
										if(bHecho){
											//---------- Segunda línea del RU.
											bHecho = rcCtrl.guardarRU(cn, sReciboSuc, iCodcli, sTipodoc, iNoRpdoc,"",	0, 0, iNoBatch, "D","A",
													iMtodomes,iMtodomes, "F", sMonUnineg, dTasaOf, iMtoForaneo, iMtoForaneo, "", sCodunineg,
													(sConcepto + sIdmpago),vaut[0].getId().getLogin(),vaut[0].getId().getNomapp(),iNocontrato,sTipocont);
											if(bHecho){
												
												if(bDifCam){
													long iMontoDF = (long)dv.roundDouble(dMontodif * 100 * -1); 
													bHecho = rcCtrl.guardarRU(cn, sReciboSuc, iCodcli, sTipodoc, iNoRpdoc,"RG", iNoRpdocm, iFechaActual,iNoBatch, 
																"", "P",  iMontoDF, 0, "F", sMonMpago, mp.getTasa().doubleValue(), 0, 0, sIdctampago, sCodunineg,
																(sConcepto + sIdmpago),vaut[0].getId().getLogin(),vaut[0].getId().getNomapp(),iNocontrato,sTipocont);
													if(!bHecho){
														m.put("MsgErrorJDE","Error al insertar registro RG de diferencial cambiario para el método: "+sIdmpago + " "+sMonMpago);
														break;
													}
												}
												
											}else{
												m.put("MsgErrorJDE","Error al registrar la línea 2 del registro RU de recibo de primas para el método: "+sIdmpago + " "+sMonMpago);
												break;
											}
										}else{
											m.put("MsgErrorJDE","Error al registrar la línea 1 del RU de recibo de primas para el método: "+sIdmpago + " "+sMonMpago);
											break;
										}
									}else{
										bHecho = false;
										m.put("MsgErrorJDE","Error, no se ha podido leer el número de documento RPDOC para el registro RU del recibo de primas");
										break;
									}
								}else{
									bHecho = false;
									m.put("MsgErrorJDE","Error al obtener la cuenta de caja para el método "+ sIdmpago+" "+sMonRU+", Caja: "+iCaid+ ", Suc: " + sCodsuc+", Comp: "+sCodcomp);
									break;
								}
							}
						}//fin moneda USD
					}else{
						bHecho = false;
						m.put("MsgErrorJDE","Error, no se ha podido leer el número de documento  RPDOCM  para el registro RU del recibo de primas");
					}
					//------------ FIN DE PROCEDIMIENTOS PARA REGISTROS DEL RU --------------//.
					if(bHecho){
						//------------ Guardar el registro de ReciboJDE.						
						bHecho = rcCtrl.fillEnlaceMcajaJde(sesCaja, transCaja, iNumrec, sCodcomp, iNoRpdocm, iNoBatch, iCaid, sCodsuc, "R", "PR");
						if(bHecho){
							//------------ Guardar asientos de dario para diferencial cambiario.
							if(bDifCam && lstDifCamb.size()>0){
//								bHecho = guardarDC(iNumrec,lstDifCamb, iCaid, sCodsuc, sCodcomp, sCodunineg, vaut, f55ca01, sesENS, sesCaja, transENS, transCaja, cn);
							}
						}else{
							m.put("MsgErrorJDE","Error al guardar enlace "+PropertiesSystem.CONTEXT_NAME+" - JDE para Recibo de primas con batch"+ iNoBatch+" NoDoc. "+iNoRpdocm+", Caja: "+iCaid+ ", Suc: " + sCodsuc+", Comp: "+sCodcomp);
						}
					}
				}else{
					m.put("MsgErrorJDE","Error al guardar el batch"+ iNoBatch+" "+sMonRU+", Caja: "+iCaid+ ", Suc: " + sCodsuc+", Comp: "+sCodcomp);
				}
			}else{
				m.put("MsgErrorJDE", "Error al leer el número de batch y documento para el registro del RU de primas");
			}
			
		} catch (Exception error) {
			bHecho = false;
			error.printStackTrace();
			m.put("MsgErrorJDE", "No se ha podido registrar el Recibo de primas");
		}
		return bHecho;
	}
/*******************************************************************************/
/** Limpiar la pantalla al cambiar de compañía, sucursal o unidad de negocio   */
	public void limpiarPantalla( ){
		Vf55ca01 vf01 = null;
		try {	
			
			//obtener datos de Caja
			vf01 = (Vf55ca01) ((List<Vf55ca01>) m.get("lstCajas")).get(0);
			
			cambiarVistaMetodos("MP",vf01);
			
			resetResumenPago(null);
		
			m.remove("pr_lstMetodosPago");
			m.remove("pr_metodospago");	//métodos de pagos.
			m.remove("pr_met"); 	  	//grid de pagos.
			m.remove("pr_tProd"); 		//datos del bien: tipo de producto.
			m.remove("pr_selectedMet");
			m.remove("pr_lstSolicitud");
			
			 
			selectedMet = new ArrayList <MetodosPago> ();
		   
		    m.put("pr_selectedMet", selectedMet);
			metodosGrid.dataBind();
			
			cmbMetodosPago.dataBind();
			cmbMoneda.dataBind();
			txtConcepto.setValue("");	
			txtFecham.setValue(new Date());
			
			cmbTipoProducto.dataBind();
			cmbMarcas.dataBind();
			cmbModelos.dataBind();
			txtNoItem.setValue("");
			txtNumRec.setValue("");
			txtNoItem.setValue("");
			
			CodeUtil.refreshIgObjects(new Object[]{
				lblMontoRecibido2, txtConcepto, txtNoItem, cmbTipoProducto, cmbMarcas, cmbModelos
			});
			
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
/*********************************************************************************/
/** obtener la moneda de pago de la unidad de negocio seleccionada				**/
	public void cambiarUnineg(ValueChangeEvent ev){
		String sCodunineg,sMonedaId="COR",sLinea="";
		MonedaCtrl mCtrl = new MonedaCtrl(); 
		 
		
		//-----------
		String sCodComp = null; 
		MetodosPagoCtrl metCtrl = new MetodosPagoCtrl();
		String[] sMetPago = null;
		lstMetodosPago = new ArrayList<SelectItem>();
		lstMoneda = new ArrayList<SelectItem>();
		lstAfiliado = new ArrayList<SelectItem>();
		int iCaid=0;
		List<Metpago[]> lstMetPago = null;
		
		Vf55ca01 caja = null;
		
		boolean nuevaConexion = true;
		
		try {
			
			ConsolidadoDepositosBcoCtrl.getCurrentSession();
			nuevaConexion = ConsolidadoDepositosBcoCtrl.isNewSession;
			
			 
			caja = ((List<Vf55ca01>) m.get("lstCajas")).get(0);
			
			selectedMet = new ArrayList<MetodosPago>();
			limpiarPantalla( );
			restablecerEstilos( );
			
			sCodunineg = ddlFiltrounineg.getValue().toString().trim();
			if(!sCodunineg.equals("UN")){
				sCodunineg = (sCodunineg.split(":"))[0];
				if(sCodunineg.length()==2){
					sLinea  = sCodunineg;
					sMonedaId =  mCtrl.obtenerMdaUnineg(sCodunineg);
				}else
				if(sCodunineg.length() >2){
					sLinea = sCodunineg.substring(sCodunineg.length()-2);
					sMonedaId =  mCtrl.obtenerMdaUnineg(sLinea);
				}
				if(sMonedaId.equals("")){
					sMonedaId="N/A";
				}
				m.put("pr_Monedaunineg", sMonedaId);
				lblMonedaunineg2.setValue(sMonedaId);
				
				//-------------------------------------------------------------------------
				//llenar los métodos de pago, monedas y cargar el último recibo de la caja.
				//-------------------------------------------------------------------------
				iCaid = (Integer.parseInt(m.get("sCajaId").toString()));
				sCodComp = cmbCompanias.getValue().toString();
				
				//------- Establecer metodos de pago
				lstMetodosPago = new ArrayList<SelectItem>();
				
				sMetPago = metCtrl.obtenerMetodosPagoxCaja_Compania(iCaid, sCodComp);
				
				lstMetPago = new ArrayList<Metpago[]>();
				for(int i = 0; i <  sMetPago.length; i ++){
		    		Metpago formapago = com.casapellas.controles.MetodosPagoCtrl.metodoPagoCodigo( sMetPago[i] );
		    		lstMetodosPago.add(new SelectItem( formapago.getId().getCodigo(), formapago.getId().getMpago() ) );
		    		lstMetPago.add( new Metpago[]{formapago} );
		    	}
				
		    	m.put("pr_metpago", lstMetPago);
		    	m.put("pr_lstMetodosPago", lstMetodosPago);
		    	m.put("pr_metodospago", "mt");
		    	
				//&& ======= Afiliados	    	
				lstAfiliado = caja.getId().getCasktpos()  == 'N'?
						getLstAfiliados(iCaid, sCodComp, sLinea, sMonedaId):
						PosCtrl.getAfiliadosSp(iCaid,sCodComp,
								sLinea, sMonedaId) ;
				if(lstAfiliado != null)
					lstAfiliado = new ArrayList<SelectItem>();
				m.put("pr_lstAfiliado",lstAfiliado);	
				ddlAfiliado.dataBind();

			 
				m.put("pr_selectedMet", selectedMet);
			}		
			
			metodosGrid.dataBind();
			cmbMetodosPago.dataBind();
			cmbMoneda.dataBind();

			
		} catch (Exception error) { 
			error.printStackTrace();
		} finally{
			
			ConsolidadoDepositosBcoCtrl.isNewSession = nuevaConexion;
			ConsolidadoDepositosBcoCtrl.closeCurrentSession( true );
			
		}
	}
	
/*********************************************************************************/
/** Llenar la lista de unidades de negocio a partir de la sucursal seleccionada **/
	public void cambiarSucursal(ValueChangeEvent ev){
		String sCodsuc,sCodComp;
		
		try {
			ddlTipodoc.dataBind();
			sCodsuc = ddlFiltrosucursal.getValue().toString().trim();
			sCodComp = cmbCompanias.getValue().toString();
			limpiarPantalla( );
			restablecerEstilos( );

			List<String[]>  unegocio2 = null;
			com.casapellas.controles.SucursalCtrl sucCtrl = new com.casapellas.controles.SucursalCtrl();
			
			//esconder datos de solicitar contrato
			chkContrato.setStyle("width: 20px; display: none");chkContrato.setChecked(true);
			lnkSearchContrato.setStyle("display: none");
			
			if(sCodsuc.equals("SUC")){
				m.remove("pr_lstFiltrounineg");
				ddlFiltrounineg.dataBind();
			}else{
				
				List<SelectItem> lstUnineg = new ArrayList<SelectItem>();
				

				unegocio2 = sucCtrl.obtenerUninegxSucursal(sCodsuc,sCodComp);
				
				if(unegocio2!=null && unegocio2.size()>0){
					
					lstUnineg.add(new SelectItem("UN","Unidad de Negocio","Seleccione la Unidad de negocio a utilizar en el pago"));
			
					for (Object[] unidad : unegocio2) {
						lstUnineg.add(new SelectItem(String.valueOf(unidad[0]),
							String.valueOf(unidad[0]) +": "+ String.valueOf(unidad[2]).trim(),
							"U/N: " +String.valueOf(unidad[2]).trim()));	
				}
					m.put("pr_lstFiltrounineg", lstUnineg);
					ddlFiltrounineg.dataBind();
				}
				
			}
		} catch (Exception error) {
			error.printStackTrace(); 
		}

	}
/**********************************************************************************/
/*************		ACTUALIZAR EL NOMBRE DEL CLIENTE	***************************/
	public void actualizarInfoCliente(ValueChangeEvent ev){
		
		try {

			m.remove("pr_idNumericPago");

			lblCodigoSearch.setValue(" ");
			lblNombreSearch.setValue(" ");

			String sParametro = txtParametro.getValue().toString();

			if (sParametro.trim().compareTo("") == 0
					|| sParametro.length() <= 3 || !sParametro.contains("=>")) {
				lblCodigoSearch.setValue(" ");
				lblNombreSearch.setValue(" ");
				return;
			}

			String sCliente[] = sParametro.split("=>");
			lblCodigoSearch.setValue(sCliente[0].trim() + " ");
			lblNombreSearch.setValue(sCliente[1].trim() + " ");
			lblCodigoSearch.setStyleClass("frmLabel2");
			lblNombreSearch.setStyleClass("frmLabel2");

			// && ========== Generar numero aleatorio para registro del recibo.
			long idNumericPago = (long) new Random().nextInt(100000000);
			m.put("pr_idNumericPago", idNumericPago);
			 
		} catch (Exception error) { 
			error.printStackTrace();
		}
		
	}
/************************************************************************************/
/************************* 		SET BUSQUEDA PRIMA		*****************************/	
	public void setBusquedaPrima(ValueChangeEvent e){
		try {
			Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();			
			String strBusqueda = cmbBusquedaPrima.getValue().toString();
			m.put("pr_strBusquedaPrima", strBusqueda);
			
			lblCodigoSearch.setValue("");
			lblNombreSearch.setValue("");
			
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
/**********************************************************************************/
/********************* PROCESA EL RECIBO DE LA PRIMA ******************************/	
	public void procesarReciboPrima(ActionEvent ev) {
		boolean validado = false;
		String sMensaje = "";

		Session session = null;
		Transaction tx = null;
		
		try { 
			
			restablecerEstilos( );
			validado = validarDatosRecibo();

			if (!validado) {
				sMensaje = lblMensajeValidacion.getValue().toString();
				throw new Exception(sMensaje);
			}
			
			sMensaje = CtrlCajas.generarMensajeBlk();
			if ( !(validado =  sMensaje.compareTo("") == 0 ) ) 
				throw new Exception("Caja Bloqueada");
	
			long idproceso = (long) new Random().nextInt(100000000);
			if( m.containsKey("pr_idNumericPago") && m.get("pr_idNumericPago") != null  )
				idproceso = Integer.parseInt(String.valueOf(m.get("pr_idNumericPago")));
			 m.put("pr_idNumericPago", idproceso);
			
			int caid = ((List<Vf55ca01>) m.get("lstCajas")).get(0).getId().getCaid();
			int codcli = Integer.parseInt(lblCodigoSearch.getValue().toString().trim());
			int codemp = ((Vautoriz[])m.get("sevAut"))[0].getId().getCodreg();
			String codcomp = cmbCompanias.getValue().toString();
			String usuario = ((Vautoriz[])m.get("sevAut"))[0].getId().getLogin().trim();
			String parametros = new PropertiesSystem().URLSIS + " ||  ipAddress:"
					+ new PropertiesSystem().IPADRESS_WBCLIENT + " || "
					+ new PropertiesSystem().WEBBROWSER;

			BigDecimal monto = new BigDecimal(txtMontoAplicar.getValue().toString()
					.trim().replace(",", "") );
			
			// Necesita una transaccion
			session = HibernateUtilPruebaCn.currentSession();
			tx = session.beginTransaction();
			
			com.casapellas.controles.ReciboCtrl.grabarReciboLog(caid, codcomp,
					codcli, "PR", idproceso, usuario, new Date(), codemp,
					parametros, monto);
			
			tx.commit();

		} catch (Exception ex) {
			sMensaje =  "Error en verificacion de datos. " + ex.getMessage() ;
			validado = false;
			LogCajaService.CreateLog("procesarReciboPrima", "ERR", ex.getMessage());
			
			try {
				if (tx != null) {
					tx.rollback();
				}				
			} catch (Exception e) {
				LogCajaService.CreateLog("procesarReciboPrima", "ERR", e.getMessage());
			}
		}finally{
			
			if(validado){				
				dwImprime.setWindowState("normal");
			}else{
				if(sMensaje.trim().compareTo("") != 0)
					lblMensajeValidacion.setValue(sMensaje);
				dwProcesa.setStyle("width:400px; min-height:200px;");
				dwProcesa.setWindowState("normal");
			}
			
			HibernateUtilPruebaCn.closeSession(session);
		}
	}
	
//Cerrar el dialogo de procesa	
public void cerrarProcesa(ActionEvent ev){
	dwProcesa.setWindowState("hidden");
}
//cerrar el dialogo de insercion 
public void cancelarIncersion(ActionEvent ev){
	dwImprime.setWindowState("hidden");
}

/*********** Guardar los datos del recibo, encabezado, detalle y datos del bien *****************/
	@SuppressWarnings("static-access")
	public void guardarReciboPrima(ActionEvent ev){
		Divisas dv = new Divisas();

		Session sessionInsJdeCaja = null;
		Transaction transInsJdeCaja = null;
		
		int caid = 0;
		int iNumRec = 0;
		int iCodCli = 0;
		
		double dtasaCam =  1;
		double dCambio = 0;
		double dCambiodom = 0;
		
		boolean bCambiodom = false;
		boolean bHayPagoSocket = false;
		boolean bAplicadoSockP = false;
		boolean aplicado = true;
		boolean rcNoduplicado = true;
		
		
		Vautoriz vAut = null;
		F55ca014 dtComp = null;
		String tiporec = new String("PR");
		String codsuc  = new String("");
		String compRc  = new String("");
		String msgError = new String("");
		List<MetodosPago> lstMetodosPago = new ArrayList<MetodosPago>();
		
		long idProceso = 0 ;
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


			CodeUtil.removeFromSessionMap(new String[]{"pr_NumeroBatchEnRecibo", "pr_NumeroProformaRepuesto"});
			
			int delay  = Integer.parseInt( new Random().nextInt(3) + ""+ new Random().nextInt(1000) ); 
			Thread.currentThread();
			Thread.sleep(delay);
			
			try {
				idProceso = Long.parseLong(String.valueOf(m.get("pr_idNumericPago"))) ;
			} catch (Exception e) {
				e.printStackTrace();
				msgError = "La transacción no ha sido completada, intente nuevamente";
				rcNoduplicado = false;
				aplicado = false;
				return;
			}
			
			List<MetodosPago> pagos = (List<MetodosPago>)m.get("pr_selectedMet");
			for (MetodosPago mp : pagos) 
				lstMetodosPago.add(mp.clone());
			
			
			lstMetodosPago = ponerCodigoBanco(lstMetodosPago);
			
			Vf55ca01 caja = ((List<Vf55ca01>) m.get("lstCajas")).get(0);
			vAut = ((Vautoriz[])m.get("sevAut"))[0];
			String codcomp = cmbCompanias.getValue().toString();

			dtComp = com.casapellas.controles.tmp.CompaniaCtrl
					.filtrarCompania((F55ca014[])m.get
					("pr_F55CA014"), codcomp);
			
			String sMonedaBase = dtComp.getId().getC4bcrcd();
			String sMontoRec = lblMontoRecibido2.getValue().toString() .trim().replace(",", ""); 
			String sMontoApl = txtMontoAplicar.getValue().toString() .trim().replace(",", ""); 
			String sConcepto  = txtConcepto.getValue().toString().trim();
			String sNomCli 	= lblNombreSearch.getValue().toString().trim();
			String sCajero	= String.valueOf(m.get("sNombreEmpleado"));		
			String sCoduser = vAut.getId().getCoduser();
			
			String sCodunineg =  ddlFiltrounineg.getValue().toString()
									.trim().compareTo("UN") == 0 ? "":
									ddlFiltrounineg.getValue()
									.toString().split(":")[0]	;		
			
			double dMtoForFCV = 0;
			double dMontoApl = Double.parseDouble(sMontoApl);
			double dMontoRec = Double.parseDouble(sMontoRec);
			int nofcv = 0;
			iCodCli = Integer.parseInt(lblCodigoSearch.getValue() .toString().trim());
			
			caid   = caja.getId().getCaid();
			codsuc = caja.getId().getCaco();
			compRc = cmbCompanias.getValue().toString();
			
			if(dMontoApl == 0 || dMontoRec == 0){
				aplicado = false;
				msgError = "Monto aplicado/Recibido inválido";
				return;
			}
			
			LogCajaService.CreateLog("guardarReciboPrima", "INFO", "guardarReciboPrima-INICIO");	
			
			sessionInsJdeCaja = HibernateUtilPruebaCn.currentSession();
			transInsJdeCaja = sessionInsJdeCaja.beginTransaction();
			
			//&& ===== Validar que en la tabla este aplicado el identificador en pendiente.
			rcNoduplicado = com.casapellas.controles.ReciboCtrl.updEstadoRecibolog(
					caid, codcomp, iCodCli, tiporec, idProceso, vAut.getId()
						.getLogin(), new Date(), vAut.getId().getCodreg(), 0, 1);
			if(!rcNoduplicado){
				aplicado = rcNoduplicado;
				msgError = "La transacción no ha sido completada";
				return;
			}
			
			//&& ========= Validar pago con socket.
			for (MetodosPago m : lstMetodosPago) {
				bHayPagoSocket = (m.getMetodo().compareTo(MetodosPagoCtrl.TARJETA) == 0 && 
								m.getVmanual().compareTo("2") == 0 ) ;
				if( bHayPagoSocket ) break;
			}
			//&& ========= aplicar el pago de Socket Pos 
			if( bHayPagoSocket ){
				String msgSocket =  PosCtrl.aplicarPagoSocketPos( 
									lstMetodosPago, sNomCli, 
									caid, compRc, tiporec);
				
				aplicado = msgSocket.compareTo("") == 0;
				bAplicadoSockP = aplicado;
				msgError = msgSocket;

				if(!aplicado) return;
			}
			
			//&& ======== Tasas de cambio a utilizar.
			double dTasaJDE = m.containsKey("pr_valortasajde") ? Double
					.parseDouble(String.valueOf(m.get("pr_valortasajde"))) : 1;
			double dTasaP = m.containsKey("pr_valortasap") ? Double
					.parseDouble(String.valueOf(m.get("pr_valortasap"))) : 1;
					
			//&& ======== Determinar como dar el cambio.	
			String sMonedaApl = ddlMonedaAplicada.getValue().toString();
			String sMoncamb  = lbletCambioapl.getValue().toString().trim().split(" ")[1].substring(0,3);
			
			//&& ========= Hay monto aplicado para el pago del recibo.
			if(m.containsKey("pr_MontoAplicado") && dMontoApl < dMontoRec ){
				
				if(sMoncamb.compareTo(sMonedaBase) == 0){
					
					dCambio =  Double.parseDouble(lblCambioapl.getValue()
									.toString().toString().replace(",", ""));
					if( sMonedaApl.compareTo(sMonedaBase) != 0 )
						dtasaCam = dTasaP;
					
				}else{
					bCambiodom = true;
					dtasaCam = dTasaJDE;
					
					if(txtCambioForaneo.getValue().toString().trim().compareTo("") != 0)
						dCambio	= Double.parseDouble(txtCambioForaneo.getValue()
										.toString().toString().replace(",", ""));
					
					dCambiodom = Double.parseDouble(lblCambioDom.getValue()
										.toString().toString().replace(",", ""));
					
					//&& ==== Generar el numero de ficha.
					nofcv = new NumcajaCtrl().obtenerNoSiguiente("FICHACV",
											caid, compRc, codsuc,sCoduser);
				}
			}
			
			//&& ========= Grabar el recibo.
			int iNumRecm = 0;
			Date dtFecharec = new Date();
			Date dtFecham = dtFecharec ;
			Date dHora    = dtFecharec ;
			
			if(cmbTiporecibo.getValue().toString().compareTo("MANUAL") == 0){			
				iNumRecm = Integer.parseInt(txtNumRec.getValue().toString().trim());
				dtFecham = (Date)txtFecham.getValue();
			}
			
			iNumRec = com.casapellas.controles.tmp.ReciboCtrl.generarNumeroRecibo(caid, compRc);
			if(iNumRec == 0){
				msgError = "Recibo no aplicado: Error al generar numero de recibo";
				throw new Exception(msgError);
			}
			
			BitacoraSecuenciaReciboService.insertarLogReciboNumerico(caid,iNumRec,codcomp,codsuc,"PR");
			
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
			
			//&& =============== Determinar si el pago trabaja bajo preconciliacion y conservar su referencia original
			com.casapellas.controles.BancoCtrl.datosPreconciliacion(lstMetodosPago, caid, codcomp) ;
			
			ReciboCtrl rc = new ReciboCtrl();
			
			//Aqui hace en base a la transacciones de hibernate
			//control de compromiso de hibernate
			//Tabla Recibo
			aplicado = rc.registrarRecibo(sessionInsJdeCaja, transInsJdeCaja, iNumRec,
								iNumRecm, compRc,dMontoApl, dMontoRec, 0.0,
								sConcepto, dtFecharec, dHora, iCodCli, sNomCli,
								sCajero, caid, codsuc, sCoduser, "PR", 0, "",
								nofcv, dtFecham, sCodunineg, "", sMonedaApl);
			if(!aplicado){
				msgError = "Recibo no aplicado: Error al grabar Recibo";
				throw new Exception(msgError);
			}
			
			//Aqui hace en base a la transacciones de hibernate
			//control de compromiso de hibernate
			//Tabla ReciboDet
			aplicado = rc.registrarDetalleRecibo(sessionInsJdeCaja, transInsJdeCaja, iNumRec,  
								iNumRecm, compRc, lstMetodosPago,	
								caid, codsuc, "PR");
			if(!aplicado){
				msgError = "Recibo no aplicado: Error al grabar Detalle de recibo";
				throw new Exception(msgError);
			}
			
			//Aqui hace en base a la transacciones de hibernate
			//control de compromiso de hibernate
			//Tabla CambioDet
			aplicado = rc.registrarCambio(sessionInsJdeCaja, transInsJdeCaja, iNumRec, compRc,
								sMoncamb, dCambio,caid, codsuc, 
								new BigDecimal(dtasaCam),"PR");
			if(!aplicado){
				msgError = "Recibo no aplicado: Error al grabar detalle de cambios";
				throw new Exception(msgError);
			}
			
			if(bCambiodom)  /*"COR"*/ 
				//Aqui hace en base a la transacciones de hibernate
				//control de compromiso de hibernate
				//Tabla CambioDet
				aplicado = rc.registrarCambio(sessionInsJdeCaja, transInsJdeCaja, iNumRec,compRc, 
								sMonedaBase, dCambiodom, caid, codsuc, 
								new BigDecimal(dtasaCam),"PR");
			
			//&& ========= Datos del bien del recibo al que aplica la prima
			String sCodproducto = (cmbTipoProducto.getValue().toString()
									.compareTo("S/T") != 0)?
									cmbTipoProducto.getValue()
									.toString().split("@")[1]:"";
		
			String sMarca  = cmbMarcas.getValue().toString().trim();
			String sModelo = cmbModelos.getValue().toString().trim();
			String sNoItem = txtNoItem.getValue().toString().trim();
			String codsucProf = CodeUtil.pad(cmbCompanias.getValue().toString().trim(), 5, "0"); 
			
			if( chkValidarProformaRepuestos.isChecked() ){
				try {
					sNoItem = String.valueOf( Integer.parseInt(sNoItem) );
				} catch (Exception e) {
					sNoItem = txtNoItem.getValue().toString().trim();
				}
				
				CodeUtil.putInSessionMap("pr_NumeroProformaRepuesto", sNoItem);
				
			}
			
			//Aqui hace en base a la transacciones de hibernate
			//control de compromiso de hibernate
			//Tabla Bien
			aplicado = new BienCtrl().ingresarBien2(sessionInsJdeCaja, transInsJdeCaja, iNumRec, 
									caid, codsuc, compRc, sCodproducto, sMarca,
									sModelo, sNoItem, codsucProf );
			if(!aplicado){
				msgError = "Recibo no aplicado: Error al grabar detalle del bien aplicado";
				throw new Exception(msgError);
			}
			//&& ========= Datos para las solicitudes de ingreso a caja.
			List<Solicitud>solicitudes = m.containsKey("pr_lstSolicitud")?
							(ArrayList<Solicitud>)m.get("pr_lstSolicitud"):
							new ArrayList<Solicitud>();
			
			CtrlSolicitud cs = new CtrlSolicitud();
			for (Solicitud s : solicitudes) {
				
				int iNumSol = cs.getNumeroSolicitud();
				if(iNumSol == 0){
					msgError = "No se ha encontrado numeración " +
							"de solicitudes de ingresos de  caja";
					throw new Exception(msgError);
				}
				
				
				aplicado = cs.registrarSolicitud2( sessionInsJdeCaja, transInsJdeCaja, iNumSol,iNumRec,
					"PR", caid, compRc,codsuc, s.getId().getReferencia(),
					s.getAutoriza(), dtFecharec, s.getObs(), s.getMpago(),
					s.getMonto(), s.getMoneda());
				
				

				
				if(!aplicado){
					msgError = "Recibo no aplicado, Error al grabar datos " +
							"de Solicitud de ingresos a caja";
					throw new Exception(msgError);
				}
			}
			//&& ======== Guardar los datos de la ficha CV
			if(bCambiodom){

				dMtoForFCV = dv.roundDouble(dCambiodom/dTasaJDE);
				
				if( dMtoForFCV > 0 ){
				
					MetodosPago mpFCV = new MetodosPago(MetodosPagoCtrl.EFECTIVO, sMoncamb, dMtoForFCV, 
										new BigDecimal(Double.toString(dTasaJDE)),
										dCambiodom, "", "","", "","0",0);
					
					List<MetodosPago>lstFCVs = new ArrayList<MetodosPago>();
					lstFCVs.add(mpFCV);
					
				
					aplicado = rc.registrarRecibo(sessionInsJdeCaja, transInsJdeCaja, nofcv, 0, compRc,
							dMtoForFCV, dMtoForFCV, 0, "", dtFecharec, dtFecharec,
							iCodCli, sNomCli, sCajero, caid,codsuc, sCoduser, 
							"FCV",0, "", iNumRec, dtFecham, sCodunineg,"",
							sMonedaApl );
					
					if(!aplicado){
						msgError = "Recibo no aplicado, Error al grabar Recibo" +
								" por Compra de Divisas";
						throw new Exception(msgError);
					}
					
				
					aplicado = rc.registrarDetalleRecibo(sessionInsJdeCaja, transInsJdeCaja,nofcv,
										0,compRc, lstFCVs, caid, codsuc, "FCV");
					if(!aplicado){
						msgError = "Recibo no aplicado, Error al grabar detalle " +
								" de Compra de Divisas";
						throw new Exception(msgError);
					}
				}
			}
			
			//&& ======== Actualizar monto de métodos cuando hay cambio.
			double dMontoneto = 0,dCambiotmp=0;
			boolean bCor5 = false, bUSD5 = false;
			
			if(bCambiodom){ //---- Cambio mixto.
				for(int i=0; i<lstMetodosPago.size();i++){
					MetodosPago m = (MetodosPago) lstMetodosPago.get(i);
					if(m.getMetodo().equals(MetodosPagoCtrl.EFECTIVO)){
						if (m.getMoneda().equals(sMoncamb)&& !bCor5) {
							dCambiotmp = dv.roundDouble(dMontoRec - dMontoApl);
							if(sMonedaApl.equals(sMonedaBase)){
								dCambiotmp = dv.roundDouble(dCambiotmp/m.getTasa().doubleValue());
								dMontoneto = dv.roundDouble(m.getMonto() - dCambiotmp );
								m.setMonto(dMontoneto);
								m.setEquivalente(dv.roundDouble(dMontoneto * m.getTasa().doubleValue()));
							}else{
								dMontoneto = dv.roundDouble(m.getMonto()- dCambiotmp);
								m.setMonto(dMontoneto);
								m.setEquivalente(dMontoneto);
							}
							bCor5 = true;
						} else if (m.getMoneda().equals(sMonedaBase)&& !bUSD5) {
							dMontoneto = dv.roundDouble(m.getMonto() - dCambiodom);
							m.setMonto(dMontoneto);
							m.setEquivalente(dv.roundDouble(dMontoneto	* m.getTasa().doubleValue()));
							bUSD5 = true;
						}
					}
				}
			}//fin de cambio mixto
			else{ //------- Hay cambio solamente en una moneda.
				for(int i=0; i<lstMetodosPago.size();i++){
					MetodosPago m = (MetodosPago) lstMetodosPago.get(i);
					
					if (m.getMetodo().equals(MetodosPagoCtrl.EFECTIVO) && m.getMoneda().equals(sMoncamb) && !bCor5) {
						if(sMoncamb.equals(sMonedaBase)){
							if (sMonedaApl.equals(sMonedaBase)) {// se saca el cambio en cor del pago en cor
								dMontoneto = dv.roundDouble(m.getMonto() - dCambio);
								m.setMonto(dMontoneto);
								m.setEquivalente(dMontoneto);
							}else if (!sMonedaApl.equals(sMonedaBase) && lstMetodosPago.size() > 1) {// se saca el cambio del pago en cor y se convierte a usd con tasa del metodo
								dMontoneto = dv.roundDouble(m.getMonto() - dCambio);
								m.setMonto(dMontoneto);
								m.setEquivalente(dv.roundDouble(dMontoneto / m.getTasa().doubleValue()));
							}else if (!sMonedaApl.equals(sMonedaBase) && lstMetodosPago.size() == 1) {// si pago toda la fac de usd en cor
								dMontoneto = dv.roundDouble(m.getMonto() - dCambio);
								m.setMonto(dMontoneto);
								m.setEquivalente(dv.roundDouble(dMontoneto/ m.getTasa().doubleValue()));
							}else if (sMonedaApl.equals(sMonedaBase) && lstMetodosPago.size() == 1) {// el pago en usd cubre todo el monto apl
								dMontoneto = dv.roundDouble(m.getEquivalente() - dCambio);
								m.setMonto(dv.roundDouble(dMontoneto / m.getTasa().doubleValue()));
								m.setEquivalente(dMontoneto);
							}  						
						}
						bCor5 = true;							
					}else 
					if (m.getMetodo().equals(MetodosPagoCtrl.EFECTIVO) && sMoncamb.equals(sMonedaBase) && !m.getMoneda().equals(sMonedaBase) && !bCor5) {
						if (sMonedaApl.equals(sMonedaBase) && lstMetodosPago.size() == 1) {// el pago en usd cubre toda la venta
							dMontoneto = dv.roundDouble(m.getEquivalente() - dCambio);
							m.setMonto(dv.roundDouble(dMontoneto/ m.getTasa().doubleValue()));
							m.setEquivalente(dMontoneto);
						}
					}
				}
			}
			
			
			//&& ===============================================================================//
			//&& ================ pagar facturas de credito en cuentas po cobrar ===============// 
			
			Vf0101 dtaCliente = EmpleadoCtrl.buscarEmpleadoxCodigo2(iCodCli);
			List<String[]> cuentasFormasPago = Divisas.cuentasFormasPago(lstMetodosPago, caid, codcomp);
			List<String[]> cuentasContableFactura = CuotaCtrl.idCuentaLibroMayorUC( codsucProf );
			
			int addressParentNumber = com.casapellas.controles.EmpleadoCtrl.addressNumberParent(iCodCli);
			
			BigDecimal tasaOficialReciboJde = sMonedaApl.compareTo(sMonedaBase) == 0 ? BigDecimal.ZERO: new BigDecimal( Double.toString( dTasaJDE ) ) ; 
			
			if(tasaOficialReciboJde.compareTo(BigDecimal.ZERO) == 1 ) {
				 
				List<BigDecimal> tasasRecibo = (List<BigDecimal>) CodeUtil.selectPropertyListFromEntity(lstMetodosPago, "tasa", true);
				for (BigDecimal tasa : tasasRecibo) {
					
					if( tasa.compareTo(BigDecimal.ONE) == 1  ) {
						tasaOficialReciboJde = tasa;
					} 
				}
			}
			
			//&& ====== generar un numero de factura y un numero de recibo asociado
			List<String> numerosRecibosJde = new ArrayList<String>();
			List<String[]> numerosFacturaRecibo = new ArrayList<String[]>();
			for (int i = 0; i < lstMetodosPago.size(); i++) {
				int numeroFactura = Divisas.numeroSiguienteJdeE1Custom( valoresJdeNumeracion[2],valoresJdeNumeracion[3] );
				int numeroRecibo = Divisas.numeroSiguienteJdeE1Custom( valoresJdeNumeracion[0],valoresJdeNumeracion[1] );
				// numeroFactura =549498 ; numeroRecibo =169241 ;
				numerosFacturaRecibo.add(new String[]{String.valueOf(numeroFactura), String.valueOf(numeroRecibo)});
				numerosRecibosJde.add( String.valueOf(numeroRecibo) ) ;
			}
			
			int numeroBatchJde  = Divisas.numeroSiguienteJdeE1(   );
			//numeroBatchJde = 1008252 ;
			if( numerosFacturaRecibo.isEmpty() ) {
				aplicado = false;
				msgError = "No se pudo obtener número de factura y documento de JdEdward's para recibo de caja " ;
				throw new Exception(msgError);
			}
			if( numeroBatchJde == 0 ) {
				aplicado = false;
				msgError = "No se pudo obtener número de batch JdEdward's para recibo de caja " ;
				throw new Exception(msgError);
			}
			
			new  ProcesarReciboRUCustom();
			ProcesarReciboRUCustom.formasDePago = lstMetodosPago;
			ProcesarReciboRUCustom.cuentasFormasPago = cuentasFormasPago;
			
			ProcesarReciboRUCustom.numeroBatchJde = String.valueOf( numeroBatchJde );
			ProcesarReciboRUCustom.numeroFacturaRecibo = numerosFacturaRecibo ;
			
			
			ProcesarReciboRUCustom.fecharecibo = dtFecharec ;
			ProcesarReciboRUCustom.montoRecibo = new BigDecimal( Double.toString( dMontoApl ) ) ;
			ProcesarReciboRUCustom.tasaCambioOficial = tasaOficialReciboJde ;
			ProcesarReciboRUCustom.monedaRecibo = sMonedaApl;
			ProcesarReciboRUCustom.monedaLocal = sMonedaBase;
			ProcesarReciboRUCustom.codigoCliente = iCodCli;
			ProcesarReciboRUCustom.codigoClientePadre = addressParentNumber;
			ProcesarReciboRUCustom.tipoRecibo = ddlTipodoc.getValue().toString();

			ProcesarReciboRUCustom.sucursal = codsucProf;
			ProcesarReciboRUCustom.unidadNegocio1 = sCodunineg;
			ProcesarReciboRUCustom.unidadNegocio2 = sCodunineg;
			ProcesarReciboRUCustom.concepto = " Rec:"+iNumRec + " Ca:"+caid+" Co:"+codcomp  ;
//			ProcesarReciboRU.nombrecliente = sNomCli;
			ProcesarReciboRUCustom.nombrecliente = sNomCli.replace("'", "");
			ProcesarReciboRUCustom.idCuentaContableFactura = cuentasContableFactura.get(0)[0]; //"20015020" ;
			ProcesarReciboRUCustom.claseContableCliente = "UC"; 
			ProcesarReciboRUCustom.numeroCuota = "001";
			ProcesarReciboRUCustom.usuario = vAut.getId().getLogin();
			ProcesarReciboRUCustom.codigousuario = vAut.getId().getCodreg();
			ProcesarReciboRUCustom.programa = PropertiesSystem.CONTEXT_NAME;
			ProcesarReciboRUCustom.categoria08 = dtaCliente.getId().getAbac08();
			ProcesarReciboRUCustom.numeroReciboCaja = iNumRec;
			ProcesarReciboRUCustom.valoresJDEIns = valoresJDEInsPrimaReservas;
			
			//**********************************************************
			//----------------------------------------------------------
			//++++++++++++++++++      INICIO     +++++++++++++++++++++++
			//----------------------------------------------------------
			//**********************************************************
			//Creado por Luis Alberto Fonseca Mendez 2019-04-24

			ProcesarReciboRUCustom.procesarRecibo2();
			
			aplicado = ProcesarReciboRUCustom.msgProceso.isEmpty();
			
			if(!aplicado){
				msgError = ProcesarReciboRUCustom.msgProceso ;
				throw new Exception(msgError);
			}
			
			List<String[]> queriesInsert = ProcesarReciboRUCustom.lstSqlsInserts;
			if( queriesInsert == null || queriesInsert.isEmpty() ){
				aplicado = false;
				msgError = "Error al crear recibo en JdEdward's por factura de Crédito ";
				throw new Exception(msgError);
			}
			
			for (String[] querys : queriesInsert) {
				
				try {
					
					LogCajaService.CreateLog("guardarReciboPrima-" + querys[1], "QRY", querys[0]);
					
					int rows = sessionInsJdeCaja.createSQLQuery( querys[0] ).executeUpdate() ;
					
					if( rows == 0 ){
						LogCajaService.CreateLog("guardarReciboPrima-" + querys[1], "ERR", "No hay filas afectadas");
						aplicado = false;
						msgError = "error al procesar: " + querys[1];
						throw new Exception(msgError);
					}
					
					
				} catch (Exception e) {
					LogCajaService.CreateLog("guardarReciboPrima-" + querys[1], "ERR", e.getMessage());
					aplicado = false;					
					msgError = "fallo en interfaz Edwards "+ querys[1] ;					
					throw new Exception(msgError);
				}
			}

			//Cambiado por lfonseca
			aplicado =  com.casapellas.controles.ReciboCtrl.crearRegistroReciboCajaJde(sessionInsJdeCaja, caid, iNumRec, codcomp, codsuc, tiporec, "R", numeroBatchJde, numerosRecibosJde);
			if(!aplicado){
				msgError = "Error al generar registro de asociación de Caja a JdEdward's @recibojde" ;
				throw new Exception(msgError);
			}
			
			
			//&& =============== Asientos de Diario por compra/venta de dolares.
			boolean bHayFicha = bCambiodom && Divisas.roundDouble( dMtoForFCV ) > 0 ;
			if(bHayFicha){
				
				BigDecimal tasaCompraVenta = new BigDecimal( Double.toString( dTasaJDE ) ) ;
				BigDecimal montoCambio = new BigDecimal(Double.toString( dMtoForFCV ));
			
				//Cambio hecho por lfonseca
				msgError = com.casapellas.controles.ReciboCtrl.batchCompraVentaCambios
						(sessionInsJdeCaja,nofcv, iNumRec, caid, codcomp,  codsuc, montoCambio, 
						tasaCompraVenta, dtFecharec,  vAut.getId().getLogin(),  
						vAut.getId().getCodreg(), sMonedaBase);
				
				aplicado = msgError.isEmpty();
				
				if(!aplicado){
					throw new Exception(msgError);
				}
			}


			if(aplicado && aplicadonacion ){				
				DonacionesCtrl.caid = caid;
				DonacionesCtrl.codcomp = codcomp;
				DonacionesCtrl.numrec = iNumRec;
				DonacionesCtrl.tiporecibo = tiporec;
				
				aplicado = DonacionesCtrl.grabarDonacion(sessionInsJdeCaja, pagosConDonacion, vAut, iCodCli );
				
				if( !aplicado ){
					msgError = DonacionesCtrl.msgProceso;
					if(msgError == null || msgError.isEmpty()) {
						msgError = "Error al grabar la donacion ";
					}
					else{ 
						msgError = "Procesar Donacion: " + msgError; 
					}
					throw new Exception(msgError);
				}
				
			}
			
			
			String tipodoc =  ddlTipodoc.getValue().toString();
			if(aplicado && tipodoc.compareTo("X4") == 0) {
				
				String strSql = " insert into @GCPMCAJA.HISTORICO_RESERVAS_PROFORMAS " +
					"(CAID, CODCOMP, CODSUC, CODUNINEG, CODCLI, NUMREC, NUMEROPROFORMA, NUMEROBATCH, TIPODOC, CODIGOCAJERO, MONEDAAPL, MONTORECIBO, TIPORECIBO) "+
					" values "+
					"(@CAID, '@CODCOMP', '@CODSUC', '@CODUNINEG', @CODCLI, @NUMREC, @NUMEROPROFORMA, @NUMEROBATCH, '@TIPODOC', @CODIGOCAJERO, '@MONEDAAPL', @MONTORECIBO, '@TIPORECIBO') ";
				
				strSql = strSql
				.replace("@GCPMCAJA",PropertiesSystem.ESQUEMA )
				.replace("@CAID", Integer.toString(caid) )
				.replace("@CODCOMP", compRc.trim() )	
				.replace("@CODSUC",	 "000" + cmbCompanias.getValue().toString().trim() )
				.replace("@CODUNINEG",  ddlFiltrounineg.getValue().toString().split(":")[0])	
				.replace("@CODCLI",	Integer.toString(iCodCli) )
				.replace("@NUMREC", Integer.toString(iNumRec) )
				.replace("@NUMEROPROFORMA", sNoItem.trim()  )	
				.replace("@NUMEROBATCH", String.valueOf( numeroBatchJde ))
				.replace("@TIPODOC", tipodoc)
				.replace("@CODIGOCAJERO", Integer.toString(vAut.getId().getCodreg() ) )
				.replace("@MONEDAAPL",	sMonedaApl)
				.replace("@MONTORECIBO", Double.toString(dMontoApl) ) 
				.replace("@TIPORECIBO", tiporec) ;
				
				LogCajaService.CreateLog("guardarReciboPrima-HISTORICO_RESERVAS_PROFORMAS", "QRY", strSql);
				
				sessionInsJdeCaja.createSQLQuery(strSql).executeUpdate() ;
				
			}
			
			//&& ======== confirmar las transacciones
			int estadoSiguiente = 2; //&& recibo aplicado correctamente
			
			if(!aplicado && rcNoduplicado ) //&& el recibo no se completo en caja o jde
				estadoSiguiente = -1;
			if(!aplicado && !rcNoduplicado )//&& el recibo no se pago por que ya existe un pago en el mismo evento
				estadoSiguiente = 0;
			
			if (estadoSiguiente != 0) {
				com.casapellas.controles.ReciboCtrl.updEstadoRecibolog(caid,
						compRc, iCodCli, tiporec, idProceso, vAut
								.getId().getLogin(), new Date(), vAut.getId()
								.getCodreg(), 1, estadoSiguiente);
			}
			
		} catch (Exception e) {
			LogCajaService.CreateLog("guardarReciboPrima", "ERR", e.getMessage());
			aplicado = false;
			msgError = "Recibo no aplicado, por favor intente nuevamente";			
		}finally{
			m.remove("procesandoReciboValidacion");
			dwImprime.setWindowState("hidden");
			dwCargando.setWindowState("hidden");
			dwProcesa.setWindowState("normal");

			try {
				if(aplicado) {
				
					transInsJdeCaja.commit();
					BitacoraSecuenciaReciboService.actualizarSatisfactorioLogReciboNumerico(caid, iNumRec, compRc.trim(),codsuc, tiporec);
					LogCajaService.CreateLog("guardarReciboPrima", "INFO", "guardarReciboPrima-FIN");
					
					msgError = "Recibo de caja aplicado correctamente " ;
					
					if(bHayPagoSocket){
						 PosCtrl.imprimirVoucher(lstMetodosPago, "V", 
								dtComp.getId().getC4rp01d1(), 
								dtComp.getId().getC4prt());
					}
					
					// Impresion de recibos.
					CtrlCajas.imprimirRecibo(caid, iNumRec, compRc, codsuc, tiporec, false);
					
					m.remove("noContrato");
					m.remove("pr_selectedMet");
					m.remove("lstDatosTrack_Con");
					m.remove("sMsgErrorRecPrimas");
					
					//** ********* Temporal **********//
					restablecerEstilos( );
					cleanWindow();
					
					dwProcesa.setStyle("width:250px;height: 150px");
					
				}
			} catch (Exception e) {
				aplicado = false;
				LogCajaService.CreateLog("guardarReciboPrima", "ERR", e.getMessage());
				msgError = "Error en confirmación de registro de valores: Caja, intente nuevamente";
				e.printStackTrace();
			}
			
			//Rollback
			try {
				if (!aplicado) {
					if (transInsJdeCaja != null){
						transInsJdeCaja.rollback();
					}
					
					//&& ======== anulacion del Socket
					if(bAplicadoSockP){
						String msgValidaSockPos = PosCtrl.revertirPagosAplicados
										(lstMetodosPago, caid, compRc, tiporec);
						msgError += msgValidaSockPos ;
					}
				}
			} catch (Exception e) {
				LogCajaService.CreateLog("guardarReciboPrima", "ERR", e.getMessage());
				msgError = "Error en confirmación de registro de valores: Caja, intente nuevamente";
				e.printStackTrace();
			}
			
			// La Excepcion esta manejada internamente.
			HibernateUtilPruebaCn.closeSession(sessionInsJdeCaja);

			lblMensajeValidacion.setValue(msgError);
		}
		
	} 	

/************************************************************************************************/
/*********** Guardar los datos del recibo, encabezado, detalle y datos del bien *****************/
	public void guardarReciboPrima1(ActionEvent ev){
		Divisas divisas = new Divisas();
		ReciboCtrl rcCtrl = new ReciboCtrl();
		BienCtrl bienCtrl = new BienCtrl();
		CtrlSolicitud solCtrl = new CtrlSolicitud();
		 
		Vautoriz[] vAut;
		Vf55ca01 f55ca01;
		List<MetodosPago> lstMetodosPago = null;
		List lstCajas, lstSolicitud = new ArrayList();
		Date dtFecharec = new Date(), dHora = new Date(),dtFecham = new Date();
		
		boolean bRecibo=true;
		double dMontoRec = 0;
		int iNumRec = 0,  iNumRecm = 0, iCodCli = 0, iCajaId = 0;
		String sCajero = null, sMonUnineg="",sCodunineg="";
		String sTipoprod[],sCodproducto="", sMarca = "", sModelo = "", sNoItem = "";
		String sCodComp = null,sConcepto = null, sNomCli = null,sCodsuc="",sCoduser="";
		
		Session sesion = HibernateUtilPruebaCn.currentSession();
		Transaction trans = null;
		
		//----- Nuevas variables por funcionalidad de cambio.
		boolean bCambiodom = false, bFCV = false;
		double dMontoapl = 0,dCambio = 0, dCambiodom = 0;
		String sMonedaForanea="";
		double dTasaJDE=1.0,dTasaP=1.0,dtasaCam = 1.0,dMtoForFCV=0, dMtoDomFCV=0;
		int iNoFCV = 0;
		List<MetodosPago>lstFCVs = null;
		
		String sCambio[],sMoncamb="";
		String sMenError = "", sMonedaApl = "";
		Divisas dv = new Divisas();
		Connection cn = null;
		boolean bNuevaSesionENS = false;

		F55ca014[] f14 = null;	
		try {
			//obtener companias x caja
			f14 = (F55ca014[])m.get("cont_f55ca014");
			
			dwImprime.setWindowState("hidden");
			dwCargando.setWindowState("hidden");
			
			//-------------- Datos de la caja
			lstCajas = (List)m.get("lstCajas");
			f55ca01 = ((Vf55ca01)lstCajas.get(0));
			vAut = (Vautoriz[])m.get("sevAut");
			sCodunineg = ddlFiltrounineg.getValue().toString().trim();
			if(!sCodunineg.equals("UN")){
				sCodunineg = (sCodunineg.split(":"))[0];	
			}
			Object[] objConfigComp = (Object[])m.get("pr_OBJCONFIGCOMP");
			String sMonedaBase = String.valueOf(objConfigComp[1]);
			
			//-------------- datos del recibo
			iCajaId		= f55ca01.getId().getCaid();
			sCodComp 	= cmbCompanias.getValue().toString();
			sCodsuc 	= f55ca01.getId().getCaco();
			dMontoRec 	= divisas.formatStringToDouble(lblMontoRecibido2.getValue().toString().trim());
			dMontoRec	= divisas.roundDouble(dMontoRec);
			dMontoapl 	= dv.formatStringToDouble(txtMontoAplicar.getValue().toString().trim());
			dMontoapl 	= dv.roundDouble(dMontoapl);
			sConcepto   = txtConcepto.getValue().toString().trim();
			iCodCli 	= Integer.parseInt(lblCodigoSearch.getValue().toString().trim());
			sNomCli 	= lblNombreSearch.getValue().toString().trim();
			sCajero	 	= (String)m.get("sNombreEmpleado");		
			sCoduser 	= vAut[0].getId().getCoduser();
			lstMetodosPago = (List<MetodosPago>)m.get("pr_selectedMet");
			lstMetodosPago = ponerCodigoBanco(lstMetodosPago);
			

			lstMetodosPago = (List<MetodosPago>)m.get("pr_selectedMet");
			//----------------------------------------------------------------------
			if(bRecibo){
				//----- tasas de cambio
				dTasaJDE = m.get("pr_valortasajde")!= null? Double.parseDouble(m.get("pr_valortasajde").toString()): 1.0000;
				dTasaP   = m.get("pr_valortasap")  != null? Double.parseDouble(m.get("pr_valortasap").toString()): 1.0000;
				
				//------------- determinar el cambio a registrar.
				sMonedaApl  = ddlMonedaAplicada.getValue().toString();
				sCambio 	= lbletCambioapl.getValue().toString().trim().split(" ");
				sMoncamb	= sCambio[sCambio.length -1].substring(0,3);
				
				if(m.get("pr_MontoAplicado")==null){
					dtasaCam =  1.0000;
					dCambio = 0;
					dCambiodom = 0;
					bCambiodom = false;
				}else{
					//------- Cambio solo córdobas.
					if(sMoncamb.equals(sMonedaBase)){				
						if(sMonedaApl.equals(sMonedaBase)){
							dtasaCam = 1.0000;
							dCambio =  dv.formatStringToDouble(lblCambioapl.getValue().toString().trim());
						}else{// if(sMonedaApl.equals("USD")){
							dtasaCam = dTasaP;
							dCambio  = dv.formatStringToDouble(lblCambioapl.getValue().toString().trim());
						}
					}else{//if(sMoncamb.equals("USD")){
						bCambiodom = true;
						
						if(txtCambioForaneo.getValue().toString().trim().equals(""))
							dCambio = 0;
						else
							dCambio	= dv.formatStringToDouble(txtCambioForaneo.getValue().toString().trim());
						
						dCambiodom = dv.formatStringToDouble(lblCambioDom.getValue().toString().trim());
						dtasaCam   = dTasaJDE;
						
						//--------- Condiciones para FCV, el registro se guarda despues del recibo PR
						//if(dCambio >0 && dCambiodom > 0){
						if(dCambiodom > 0){
							NumcajaCtrl numCtrl = new NumcajaCtrl();
							bFCV = true;
							sMonedaForanea = sMoncamb;
							dMtoDomFCV = dCambiodom;
							dMtoForFCV = dv.roundDouble(dCambiodom/dTasaJDE);
							iNoFCV = numCtrl.obtenerNoSiguiente("FICHACV", iCajaId, sCodComp, sCodsuc,sCoduser);
							MetodosPago mpFCV = new MetodosPago(MetodosPagoCtrl.EFECTIVO, sMoncamb, dMtoForFCV, new BigDecimal(dTasaJDE), dMtoDomFCV, "", "","", "","0",0);
							lstFCVs = new ArrayList<MetodosPago>();
							lstFCVs.add(mpFCV);
						}
					}
				}
				
				if(m.get("pr_lstSolicitud")!= null)
					lstSolicitud = (List)m.get("pr_lstSolicitud");
		
				//------------- Datos para el registro del bien.
				sCodproducto = cmbTipoProducto.getValue().toString();
				if(!sCodproducto.equals("S/T")){
					sTipoprod 	 = sCodproducto.split(" ");
					sCodproducto = sTipoprod[sTipoprod.length - 1].trim();
				}			
				sMarca  = cmbMarcas.getValue().toString().trim();
				sModelo = cmbModelos.getValue().toString().trim();
				sNoItem = txtNoItem.getValue().toString().trim();
				String codsucProf = "000" + cmbCompanias.getValue().toString();
								
				//------------- obtener el número del último recibo, sumarle 1 y actualizarlo.
				iNumRec = rcCtrl.obtenerUltimoRecibo(null, null, iCajaId, sCodComp);
				iNumRec++;
				//actualizar el numero de caja
				rcCtrl.actualizarNumeroRecibo(iCajaId, sCodComp.trim(), iNumRec);
				if(iNumRec == 0){
					bRecibo = false;
					sMenError = "No se ha podido obtener el número de recibo para registro del pago";
				}
				else{
					
					if(cmbTiporecibo.getValue().toString().equals("MANUAL")){			
						iNumRecm = Integer.parseInt(txtNumRec.getValue().toString().trim());
						dtFecham = (Date)txtFecham.getValue();
					}
					//------------- Guardar el registro del recibo.
					trans = sesion.beginTransaction();
					
					bRecibo = rcCtrl.registrarRecibo(sesion, trans, iNumRec,
							iNumRecm, sCodComp, dMontoapl, dMontoRec, 0.0,
							sConcepto, dtFecharec, dHora, iCodCli, sNomCli,
							sCajero, iCajaId, sCodsuc, sCoduser, "PR", 0, "",
							iNoFCV, dtFecham, sCodunineg, "", sMonedaApl);
					
					if(bRecibo){
						bRecibo = rcCtrl.registrarDetalleRecibo(sesion, trans, iNumRec, iNumRecm, sCodComp, 
									lstMetodosPago,	iCajaId, sCodsuc, "PR");
						if(bRecibo){
							//------- Guardar el cambio del recibo.
							bRecibo = rcCtrl.registrarCambio(sesion, trans, iNumRec,sCodComp, sMoncamb, 
									dCambio,iCajaId, f55ca01.getId().getCaco(), new BigDecimal(dtasaCam),"PR");
							if(bRecibo){
								if(bCambiodom)
									bRecibo = rcCtrl.registrarCambio(sesion, trans, iNumRec,sCodComp, sMonedaBase, /*"COR"*/ 
												dCambiodom, iCajaId, f55ca01.getId().getCaco(), new BigDecimal(dtasaCam),"PR");
									if(!bRecibo)
									 sMenError = "Error al registrar el detalle del cambio doméstico del recibo de caja";
							}else{
								sMenError = "Error al registrar el detalle del cambio del recibo de caja";
							}					
							if(bRecibo){
								bRecibo = bienCtrl.ingresarBien(sesion, trans, iNumRec, iCajaId, sCodsuc, sCodComp, 
											sCodproducto, sMarca, sModelo, sNoItem, codsucProf);
								if(bRecibo){
									if (lstSolicitud != null && lstSolicitud.size()>0){
										for(int i = 0; i < lstSolicitud.size();i++){
											Solicitud sol = (Solicitud)lstSolicitud.get(i);
											int iNumSol = solCtrl.getNumeroSolicitud();
											if(iNumSol > 0){
												bRecibo = solCtrl.registrarSolicitud(null,null, iNumSol, iNumRec, "PR", iCajaId, 
														sCodComp,f55ca01.getId().getCaco(), sol.getId().getReferencia(),sol.getAutoriza(),
														sol.getFecha(), sol.getObs(), sol.getMpago(), sol.getMonto(), sol.getMoneda());
												if(!bRecibo){
													m.put("sMsgErrorRecPrimas","No se ha podido registrar la solicitud autorización de pagos");
													break;
												}
											}else{
												m.put("sMsgErrorRecPrimas","No se pudo obtener el No. de Solicitud!!!");
												bRecibo = false;
												break;
											}
										}
									}
								}else{
									m.put("sMsgErrorRecPrimas", "Error al insertar el detalle del bien aplicado al recibo de primas");
								}
							}
						}else{
							m.put("sMsgErrorRecPrimas", "Error al insertar el detalle del recibo de primas");
						}
					}else{
						m.put("sMsgErrorRecPrimas", "Error al insertar la cabecera del recibo de primas");
					}
				 }
				//------------------- Guardar el registro de Fichas en MCAJA (en caso de haber).
				if(bRecibo){
					if(bFCV){
						if(iNoFCV >0){

							bRecibo = rcCtrl.registrarRecibo(sesion, trans, iNoFCV, 0,sCodComp, dMtoForFCV, dMtoForFCV, 
									 0, "", new Date(),	new Date(), iCodCli, sNomCli, sCajero, 
									 iCajaId,sCodsuc, sCoduser, "FCV",0, "", iNumRec, dtFecham,
									 sCodunineg,"", sMonedaApl);
							
							if(bRecibo){
								bRecibo = rcCtrl.registrarDetalleRecibo(sesion, trans,iNoFCV, 0,sCodComp, lstFCVs, iCajaId, sCodsuc, "FCV");
								if(!bRecibo)
									sMenError = "No se ha podido registrar el detalle de Ficha de Compra Venta";
							}else{
								sMenError = "No se ha podido registrar la Ficha de Compra Venta";
							}
						}else{
							bRecibo = false;
							sMenError = "No se encontro No. de ficha configurado para esta caja";
						}
					}
				}
				//---------------- Si hay cambio actualizar el monto de efectivo en el método de pago.
				if(bRecibo){
					//------- Actualizar monto de métodos cuando hay cambio.
					double dMontoneto = 0,dCambiotmp=0;
					boolean bCor5 = false, bUSD5 = false;
					
					if(bCambiodom){ //---- Cambio mixto.
						for(int i=0; i<lstMetodosPago.size();i++){
							MetodosPago m = (MetodosPago) lstMetodosPago.get(i);
							if(m.getMetodo().equals(MetodosPagoCtrl.EFECTIVO)){
								if (m.getMoneda().equals(sMoncamb)&& !bCor5) {
									dCambiotmp = dv.roundDouble(dMontoRec - dMontoapl);
									if(sMonedaApl.equals(sMonedaBase)){
										dCambiotmp = dv.roundDouble(dCambiotmp/m.getTasa().doubleValue());
										dMontoneto = dv.roundDouble(m.getMonto() - dCambiotmp );
										m.setMonto(dMontoneto);
										m.setEquivalente(dv.roundDouble(dMontoneto * m.getTasa().doubleValue()));
									}else{
										dMontoneto = dv.roundDouble(m.getMonto()- dCambiotmp);
										m.setMonto(dMontoneto);
										m.setEquivalente(dMontoneto);
									}
									bCor5 = true;
								} else if (m.getMoneda().equals(sMonedaBase)&& !bUSD5) {
									dMontoneto = dv.roundDouble(m.getMonto() - dCambiodom);
									m.setMonto(dMontoneto);
									m.setEquivalente(dv.roundDouble(dMontoneto	* m.getTasa().doubleValue()));
									bUSD5 = true;
								}
								lstMetodosPago.remove(i);
								lstMetodosPago.add(i, m);
							}
						}
					}//fin de cambio mixto
					else{ //------- Hay cambio solamente en una moneda.
						for(int i=0; i<lstMetodosPago.size();i++){
							MetodosPago m = (MetodosPago) lstMetodosPago.get(i);
							
							if (m.getMetodo().equals(MetodosPagoCtrl.EFECTIVO) && m.getMoneda().equals(sMoncamb) && !bCor5) {
								if(sMoncamb.equals(sMonedaBase)){
									if (sMonedaApl.equals(sMonedaBase)) {// se saca el cambio en cor del pago en cor
										dMontoneto = dv.roundDouble(m.getMonto() - dCambio);
										m.setMonto(dMontoneto);
										m.setEquivalente(dMontoneto);
									}else if (!sMonedaApl.equals(sMonedaBase) && lstMetodosPago.size() > 1) {// se saca el cambio del pago en cor y se convierte a usd con tasa del metodo
										dMontoneto = dv.roundDouble(m.getMonto() - dCambio);
										m.setMonto(dMontoneto);
										m.setEquivalente(dv.roundDouble(dMontoneto / m.getTasa().doubleValue()));
									}else if (!sMonedaApl.equals(sMonedaBase) && lstMetodosPago.size() == 1) {// si pago toda la fac de usd en cor
										dMontoneto = dv.roundDouble(m.getMonto() - dCambio);
										m.setMonto(dMontoneto);
										m.setEquivalente(dv.roundDouble(dMontoneto/ m.getTasa().doubleValue()));
									}else if (sMonedaApl.equals(sMonedaBase) && lstMetodosPago.size() == 1) {// el pago en usd cubre todo el monto apl
										dMontoneto = dv.roundDouble(m.getEquivalente() - dCambio);
										m.setMonto(dv.roundDouble(dMontoneto / m.getTasa().doubleValue()));
										m.setEquivalente(dMontoneto);
									}  						
								}
								lstMetodosPago.remove(i);
								lstMetodosPago.add(i, m);
								bCor5 = true;							
							}else 
							if (m.getMetodo().equals(MetodosPagoCtrl.EFECTIVO) && sMoncamb.equals(sMonedaBase) && !m.getMoneda().equals(sMonedaBase) && !bCor5) {
								if (sMonedaApl.equals(sMonedaBase) && lstMetodosPago.size() == 1) {// el pago en usd cubre toda la venta
									dMontoneto = dv.roundDouble(m.getEquivalente() - dCambio);
									m.setMonto(dv.roundDouble(dMontoneto/ m.getTasa().doubleValue()));
									m.setEquivalente(dMontoneto);
								}
							}
						}
					}
				}
				
				
				//------------ Verificar inserción: si hubo errores, borrar todo, si no, confirmar registro.
				if(bRecibo){
					
					
					//------- obtener conexión del datasource
					cn = As400Connection.getJNDIConnection("DSMCAJA2");
					cn.setAutoCommit(false);
					
					
					
					
					
					//----------------------------------------------------
					
					sMonUnineg = ddlMonedaAplicada.getValue().toString();
					bRecibo = realizarTransJDE(iNumRec, dtFecham, sMonUnineg, iCajaId, sCodsuc, sCodComp, lstMetodosPago, 
												iCodCli, sCodunineg, cn, sesion, sesion, trans, trans);
					
					//--- Guardar los asientos contables de FCV
					if(bRecibo){
						if(bFCV)
							bRecibo = guardarAsientosFCV(cn, trans, trans, sesion,sesion, 
															iNoFCV, iNumRec, iCajaId, sCodComp, sCodsuc, 
															dMtoForFCV, dTasaJDE, sMonedaForanea,sMonedaBase,
															vAut[0] , f55ca01);	
					}
					
					String sMensaje="";
					if(bRecibo){
						trans.commit();
						
						cn.commit();
						
						
						
						//&& =========== guardar las transacciones de POS.
						if((f55ca01.getId().getCasktpos()+"").equals("Y")){
							List lstDatos = (List)m.get("lstDatosTrack_Con");
							bRecibo = aplicarPagoSocketPos(lstMetodosPago,lstDatos);
						
							if(bRecibo){
								imprimirVoucher(lstMetodosPago,sCodComp,"V", f14);
								rcCtrl.actualizarReferenciasRecibo(iNumRec,iCajaId,
														sCodComp,f55ca01.getId().getCaco(),
														"PR",lstMetodosPago);
							}else{
								bRecibo = anularRecibo(iNumRec,iCajaId,f55ca01.getId().getCaco(),
														sCodComp, vAut[0].getId().getCoduser(),iNoFCV);
								sMensaje = lblMensajeValidacion.getValue().toString();
								bRecibo = false;
							}
						}
					}else{
						sMensaje += m.get("MsgErrorJDE").toString();
						dwProcesa.setStyle("width:320px;height: 200px");
						trans.rollback();
						cn.rollback();						
						m.remove("lstDatosTrack_Con");
						m.remove("noContrato");
					}
					
					if(bRecibo){	
						sMensaje =  "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons2/detalle.png\">";
						sMensaje += "Se registrado correctamente las transacciones de pago de recibo";
						dwProcesa.setStyle("width:320px;height: 150px");
						//------------- limpiar la pantalla y restablecer los valores.
						SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
						restablecerEstilos( );
						cleanWindow();
						
						//imprimir recibo de primas y reservas
						BigDecimal bdNumrec = new BigDecimal(iNumRec);
						getP55recibo().setIDCAJA(new BigDecimal(f55ca01.getId().getCaid()));
						getP55recibo().setNORECIBO(bdNumrec);
						getP55recibo().setIDEMPRESA(sCodComp);
						getP55recibo().setIDSUCURSAL(sCodsuc);
						getP55recibo().setTIPORECIBO("PR");
						getP55recibo().setRESULTADO("");
						getP55recibo().setCOMANDO("");
						getP55recibo().invoke();
						getP55recibo().getRESULTADO();

						m.remove("noContrato");
						m.remove("pr_selectedMet");
						m.remove("lstDatosTrack_Con");
					}
					//------------ Cerrar conexión y mostrar mensaje de alertas.
					m.remove("noContrato");
					cn.close();
					
					
					lblMensajeValidacion.setValue(sMensaje);
					dwProcesa.setWindowState("normal");
					
				}
				else{
					trans.rollback();
					
					sMenError = (sMenError!=null && sMenError.trim().equals(""))?
									"No se ha podido registrar el pago aplicado":sMenError;
					sMenError = (m.get("sMsgErrorRecPrimas")==null)?
									sMenError:
									m.get("sMsgErrorRecPrimas").toString();
					
					lblMensajeValidacion.setValue(sMenError);
					dwProcesa.setStyle("width:320px;height: 200px");
					dwProcesa.setWindowState("normal");
				
					
					m.remove("lstDatosTrack_Con");
					m.remove("noContrato");
				}
				sesion.close();
				
			}else{				
				dwProcesa.setWindowState("normal");
				anularPagosSP(lstMetodosPago);
				m.remove("lstDatosTrack_Con");
				m.remove("noContrato");
			}
		} catch (Exception error) {
			try{
				String sMensaje = "";
				sMensaje =  " No se puede realizar operación => Error de sistema: <br>";
				sMensaje += error +"<br>";
				sMensaje += error.getCause();
	
				lblMensajeValidacion.setValue(sMensaje);
				dwProcesa.setStyle("width:430px;height: 200px");
				dwProcesa.setWindowState("normal");
				if(trans.isActive())
					trans.rollback();
				if(sesion.isOpen())
					sesion.close();
				
				
				
				m.remove("lstDatosTrack_Con");
				m.remove("noContrato");
				error.printStackTrace();
				
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}finally{
			dwCargando.setWindowState("hidden");
			try {cn.close();}  catch (Exception e) { e.printStackTrace(); }
		}
	}
/***************************************************************************************************/
/** 		 Poner el código al banco en la referencia 1 en vez de su descripción 				  
 ************/
	public List ponerCodigoBanco(List lstMetodosPago){
		F55ca022[] f55ca022;
		MetodosPago mPago = new MetodosPago();
		String sBanco = "";
		
		try{
			f55ca022 = (F55ca022[])m.get("pr_banco");//lista de bancos
			for (int i=0;i<lstMetodosPago.size();i++){
				mPago = (MetodosPago)lstMetodosPago.get(i);
				if(mPago.getMetodo().trim().equals(MetodosPagoCtrl.TRANSFERENCIA) || mPago.getMetodo().trim().equals(MetodosPagoCtrl.DEPOSITO) || mPago.getMetodo().trim().equals(MetodosPagoCtrl.CHEQUE)){
					sBanco = mPago.getReferencia();
					for(int j = 0 ; j < f55ca022.length;j++){
						if(sBanco.trim().equals(f55ca022[j].getId().getBanco().trim())){
							mPago.setReferencia(f55ca022[j].getId().getCodb()+"");
							lstMetodosPago.remove(i);
							lstMetodosPago.add(i, mPago);
						}
					}
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return lstMetodosPago;
	}
	
/*********************************************************************************************/
/****************VALIDAR DATOS DEL RECIBO ANTES DE PROCESAR****************************************/
public boolean validarDatosRecibo(){
	boolean validado = true;	
	ReciboCtrl rpCtrl = new ReciboCtrl();
	String sCodComp = "", sMensajeError = "";
	int iNumRecm = 0, y = 145;
	Vf55ca01 vf55ca01 = null;
	Matcher matAlfa = null;
	String sConcepto = "",sHtmlError="";
	
	try{
		vf55ca01 = (Vf55ca01)((List)m.get("lstCajas")).get(0);
		Pattern pAlfa = Pattern.compile("^[A-Za-z0-9-.;,\\p{Blank}]*$");
		sHtmlError =  "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> ";
		
		if(lblCodigoSearch != null && lblCodigoSearch.getValue() != null){
			String sCodigo = lblCodigoSearch.getValue().toString();
			String sNombre = lblNombreSearch.getValue().toString();
			if (sCodigo.endsWith("_________")){
				lblCodigoSearch.setValue("");
				lblNombreSearch.setValue("");
			}
		}

		selectedMet = (ArrayList<MetodosPago>) m.get("pr_selectedMet");
		
		//---------------- Validar la sucursal y la Unidad de negocio.
		if(ddlFiltrosucursal.getValue().toString().equals("SUC")){
			sMensajeError += sHtmlError + "Debe seleccionar la sucursal a utilizar<br>";
			validado = false;
			y=y+14;
		}
		if(ddlFiltrounineg.getValue().toString().equals("UN")){
			sMensajeError += sHtmlError + "Debe seleccionar unidad de negocios a utilizar<br>";
			validado = false;
			y=y+14;
		}
		//---------------- Validar los datos del cliente 
		if (lblCodigoSearch.getValue() == null || (lblCodigoSearch.getValue().toString()).trim().equals("")){//validar codigo
			sMensajeError = sMensajeError + sHtmlError + "Debe especificar la información del cliente<br>";
			lblCodigoSearch.setStyleClass("frmLabel2Error");
			lblCodigoSearch.setValue("______________");
			lblNombreSearch.setStyleClass("frmLabel2Error");
			lblNombreSearch.setValue("____________________________");
			validado = false;
			y=y+14;
		}
		//---------------- Validaciones para el número de referencia.
		if(txtNoItem.getValue() == null || (txtNoItem.getValue().toString()).trim().equals("")){
			sMensajeError = sMensajeError + sHtmlError + "La referencia es requerida<br>";
			txtNoItem.setStyleClass("frmInput2Error");
			validado = false;
			y=y+14;
		}else
		if(!pAlfa.matcher(txtNoItem.getValue().toString().trim()).matches()){
			sMensajeError = sMensajeError + sHtmlError + "La Referencia contiene caracteres inválidos";
			txtNoItem.setStyleClass("frmInput2Error");
			validado = false;
			y=y+14;
		}else
		if(txtNoItem.getValue().toString().trim().length()>25){
			sMensajeError = sMensajeError + sHtmlError + "La Referencia contiene demasiados caracteres (Máx:25)<br>" ;
			txtNoItem.setStyleClass("frmInput2Error");
			validado = false;
			y=y+14;
		}else
			
		//=================== Validar si el numero de contrato existe
		if(ddlTipodoc.getValue().toString().compareTo("X3") == 0 && !chkContrato.isChecked() && m.containsKey( "tallerUn") ){
			sMensajeError = sMensajeError + sHtmlError + " Tipo de Documento X3 debe asociarse a numero de contrato ";
			txtNoItem.setStyleClass("frmInput2Error");
			validado = false;
			y=y+14;
		}else
	 
		if(chkContrato.isChecked()){//validar si existe numero de presupuesto
			if(m.get("tallerUn") != null){
				SucursalCtrl sCtrl = new SucursalCtrl();
				if(lblCodigoSearch.getValue() != null && !lblCodigoSearch.getValue().toString().equals("")){
					TallerUn tu = (TallerUn)m.get("tallerUn");
					
					int iNocontrato = sCtrl.existeContrato(tu.getId().getPcia(),tu.getId().getPtall(),Integer.parseInt(lblCodigoSearch.getValue().toString().trim()),txtNoItem.getValue().toString().trim());
					if(iNocontrato > 0){
						m.put("noContrato", iNocontrato);
					}else{
						sMensajeError = sMensajeError + sHtmlError + "El número de contrato especificado no existe en el sistema de talleres";
						txtNoItem.setStyleClass("frmInput2Error");
						validado = false;
						y=y+14;
					}
				}else{//
					sMensajeError = sMensajeError + sHtmlError + "Debe especificar el cliente al que va a aplicar el recibo";
					txtNoItem.setStyleClass("frmInput2Error");
					validado = false;
					y=y+14;
				}
			}
		}
		
	 
		//********************************** validar el numero de proforma para reservacion de repuestos **************************************************//
		
		String sLinea = CompaniaCtrl.leerLineaNegocio(ddlFiltrounineg.getValue().toString().split(":")[0] );
		if( validado && sLinea.trim().compareTo("03") == 0 && !chkValidarProformaRepuestos.isChecked() && ddlTipodoc.getValue().toString().compareTo("X4") == 0 ) {
			sMensajeError = "El tipo de documento X4 es debe ser utilizado para reservas de repuestos con proformas válidas. ";
			lblMensajeValidacion.setValue(sMensajeError);
			dwProcesa.setStyle("width:390px; height: 60px");
			return validado = false;
		}
		
		if( chkValidarProformaRepuestos.isChecked()  && validado){
					
			
			txtNoItem.setStyleClass("frmInput2ddl");
			
			ddlTipodoc.setValue("X4");
			ddlTipodoc.dataBind();
			CodeUtil.refreshIgObjects(ddlTipodoc);
			
			int codigo_cliente = Integer.parseInt(lblCodigoSearch.getValue().toString().trim());
			int numero_proforma = Integer.parseInt(txtNoItem.getValue().toString().trim());
			String codcompProf = cmbCompanias.getValue().toString();
			String codsucProf = cmbCompanias.getValue().toString();
			String monedaAplicada = ddlMonedaAplicada.getValue().toString();
			codsucProf = "000"+codsucProf;
			
			String montorecibido = lblMontoRecibido2.getValue().toString();
			BigDecimal bdMontoRecibido = new BigDecimal(montorecibido.trim().replace(",", "")) ;	
			
			
			validado =  bdMontoRecibido.compareTo(BigDecimal.ZERO) > 0;
			
			if(!validado){
				sMensajeError = "Debe ingresar pagos para validar el monto requerido aplicado por anticipo de Proforma";
				lblMensajeValidacion.setValue(sMensajeError);
				dwProcesa.setStyle("width:390px; height: 60px");
				return validado = false;
			}
			
		
			
			if(!validado){
				
				sMensajeError = "Validación no Aprobada para Proforma " + numero_proforma + " cliente " + codigo_cliente + " sucursal '" + codsucProf;
				
				txtNoItem.setStyleClass("frmInput2Error");
				
				lblMensajeValidacion.setValue(sMensajeError);
				dwProcesa.setStyle("width:390px; height: 60px");
				
				return validado;
			}
			 
		}
		
		
		//---------------- Validar que se hayan registrado métodos de pago.
		if (selectedMet == null || selectedMet.isEmpty() || selectedMet.size()==0){
			sMensajeError = sMensajeError + sHtmlError +  "No se han agregado pagos al recibo<br>";
			metodosGrid.setStyleClass("igGridError");
			validado = false;
			y=y+14;
		}
		//------------ validar el concepto del recibo ---------------//.
		sConcepto = txtConcepto.getValue().toString().trim();
		matAlfa = pAlfa.matcher(sConcepto.toUpperCase());
		
		if(txtConcepto.getValue().toString().trim().equals("")){
			sMensajeError = sMensajeError + sHtmlError + "El Concepto es requerido <br>";
			txtConcepto.setStyleClass("frmInput2Error");
			validado = false;
			y=y+14;
		}else
		if(!matAlfa.matches()){
			sMensajeError = sMensajeError + sHtmlError + "El campo concepto contiene caracteres invalidos <br>";
			txtConcepto.setStyleClass("frmInput2Error");
			validado = false;
			y=y+14;
		}else
		if(sConcepto.length() > 250){
			sMensajeError = sMensajeError + sHtmlError + "La longitud del campo es muy alta (lim. 250) <br>";
			txtConcepto.setStyleClass("frmInput2Error");
			validado = false;
			y=y+14;
		}
		//txtNumRec.setStyle("visibility:hidden");
		if (!cmbTiporecibo.getValue().toString().equals("AUTOMATICO")){
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Matcher matFecha = null;
			Date dFecha = null;
			String sFecha =null; 
			
			sCodComp = cmbCompanias.getValue().toString();
			//expresion regular de fecha
			if(txtFecham.getValue() != null){
				dFecha = (Date)txtFecham.getValue();
				sFecha = sdf.format(dFecha);
				Pattern pFecha = Pattern.compile( "^[0-3]?[0-9](/|-)[0-2]?[0-9](/|-)[1-2][0-9][0-9][0-9]$" );	
				matFecha = pFecha.matcher(sFecha);
			}
			//expresion regular solo numeros
			Matcher matNumero = null;
			if(txtNumRec.getValue() != null){
				Pattern pNumero = Pattern.compile("^[0-9]*$");
				matNumero = pNumero.matcher(txtNumRec.getValue().toString().trim());
				if(matNumero.matches() && !txtNumRec.getValue().toString().trim().equals("")){
					iNumRecm = Integer.parseInt(txtNumRec.getValue().toString().trim());
				}
			}
			//valida la fecha del recibo
			if(txtFecham.getValue() == null || (txtFecham.getValue().toString().trim()).equals("")){
				sMensajeError = sMensajeError + sHtmlError + "Fecha de recibo requerida<br>";
				txtFecham.setStyleClass("frmInput2Error3");
				validado = false;
				y=y+14;
			}else 
			if(matFecha == null || !matFecha.matches()){
				sMensajeError = sMensajeError + sHtmlError + " Fecha de recibo no es valida<br>";
				txtFecham.setStyleClass("frmInput2Error3");
				validado = false;
				y=y+14;
			}
			//validar el numero de recibo
			 if(txtNumRec.getValue() == null || (txtNumRec.getValue().toString().trim()).equals("")){
				sMensajeError = sMensajeError + sHtmlError + "Número de recibo manual es requerido<br>";
				txtNumRec.setStyleClass("frmInput2Error");
				validado = false;
				y=y+14;
			}else
			if(matNumero == null || !matNumero.matches()){
				sMensajeError = sMensajeError + sHtmlError + " Número de recibo no es valido<br>";
				txtNumRec.setStyleClass("frmInput2Error");
				validado = false;
				y=y+14;
			}else
			//validar existencia del numero de recibo manual
			if (iNumRecm > 0){
				if(rpCtrl.verificarNumeroRecibo(vf55ca01.getId().getCaid(), sCodComp, iNumRecm,vf55ca01.getId().getCaco())){
					sMensajeError = sMensajeError + sHtmlError + "El número de recibo manual ya existe!<br>";
					txtNumRec.setStyleClass("frmInput2Error");
					validado = false;
					y=y+14;
				}
			}
		}
		
		//--- Validaciones a partir de moneda base.
		String codcomp = cmbCompanias.getValue().toString();
		F55ca014 dtComp =  com.casapellas.controles.tmp.CompaniaCtrl
							.filtrarCompania((F55ca014[])m.get
								("pr_F55CA014"), codcomp);
		String sMonedaBase =  dtComp.getId().getC4bcrcd();
		
		Divisas dv = new Divisas();
		double dMontorec = 0;
		double dMontoapl = 0;
		
		String sMontorec = lblMontoRecibido2.getValue().toString();
		String sMontoapl = txtMontoAplicar.getValue().toString();
		dMontorec = dv.formatStringToDouble(sMontorec);
		dMontoapl = dv.formatStringToDouble(sMontoapl);
		
		if(dMontorec <= 0 || dMontoapl <= 0 ){
			sMensajeError += sHtmlError + "Monto Aplicado/Recibido inválido";
			lblMensajeValidacion.setValue(sMensajeError);
			dwProcesa.setStyle("width:390px;height:"+y+"px");
			return false;
		}
		if(dMontorec < dMontoapl){
			sMensajeError += sHtmlError + "Monto recibido debe ser mayor/igual que monto aplicado";
			lblMensajeValidacion.setValue(sMensajeError);
			dwProcesa.setStyle("width:390px;height:"+y+"px");
			return false;
		}
		
		//----------- Validaciones para el monto aplicado y el cambio. 
		if(m.get("pr_MontoAplicado")!=null){
			double dcambioUsd =0,dCambioneto = 0; 
			sMontorec = lblMontoRecibido2.getValue().toString();
			dMontorec = dv.formatStringToDouble(sMontorec);
			dMontoapl = Double.parseDouble(m.get("pr_MontoAplicado").toString());
			
			if(dMontorec < dMontoapl){
				sMensajeError += sHtmlError + "El monto recibido debe ser mayor o igual al monto aplicado <br>";
				validado = false;
				y=y+14;
			}
			else{
				double dTasaJDE =1.0;
				dTasaJDE = m.get("pr_valortasajde")!= null? Double.parseDouble(m.get("pr_valortasajde").toString()): 1.0000;
//				dTasaP   = m.get("pr_valortasap")  != null? Double.parseDouble(m.get("pr_valortasap").toString()): 1.0000;
					
				String sCambio[] = lbletCambioapl.getValue().toString().trim().split(" ");
				String sMoneda = sCambio[sCambio.length-1].substring(0,3);
				dCambioneto = dv.roundDouble(dMontorec - dMontoapl);
				
				if(ddlMonedaAplicada.getValue().toString().trim().equals(sMonedaBase))
					dCambioneto = dv.roundDouble(dCambioneto / dTasaJDE);
				
				if(!sMoneda.equals(sMonedaBase)){
					if(dCambioneto >0){
						if(!txtCambioForaneo.getValue().toString().trim().equals("")){
							dcambioUsd = dv.formatStringToDouble(txtCambioForaneo.getValue().toString());
							
							if(dcambioUsd < dCambioneto ){
								if(lblCambioDom.getValue().toString().trim().equals("") ||
									(dv.formatStringToDouble(lblCambioDom.getValue().toString().trim()) <=0)){
										sMensajeError = sMensajeError + sHtmlError + "El cambio debe ser procesado  <br>";
										txtCambioForaneo.setStyleClass("frmInput2Error");
										validado = false;
										y=y+14;
								}
							}else
							if(dcambioUsd > dCambioneto){
								sMensajeError = sMensajeError + sHtmlError + "El cambio digitado debe ser menor al cambio sugerido <br>";
								txtCambioForaneo.setStyleClass("frmInput2Error");
								validado = false;
								y=y+20;
							}
						}else{
							sMensajeError = sMensajeError + sHtmlError + "Debe especificar el monto del cambio <br>";
							txtCambioForaneo.setStyleClass("frmInput2Error");
							validado = false;
							y=y+14;
						}
					}
				}
			}
		}
		lblMensajeValidacion.setValue(sMensajeError);
		dwProcesa.setStyle("width:390px;height:"+y+"px");
		
	}catch(Exception ex){
		validado = false;
		ex.printStackTrace();
	}
	return validado;
}
/*****************REESTABLECER EL ESTILO DE LOS COMPONENTES*************************************/
	public void restablecerEstilos( ){
		
		try {
			
			txtFecham.setStyleClass("dateChooserSyleClass");
			txtNumRec.setStyleClass("frmInput2");
			metodosGrid.setStyleClass("igGrid");
			cmbTipoProducto.setStyleClass("frmInput2ddl");
			cmbMarcas.setStyleClass("frmInput2ddl");
			cmbModelos.setStyleClass("frmInput2ddl");
			txtNoItem.setStyleClass("frmInput2ddl");
			lblCodigoSearch.setStyleClass("frmLabel2");
			lblNombreSearch.setStyleClass("frmLabel2");
			txtConcepto.setStyleClass("frmInput2");
			
			CodeUtil.refreshIgObjects(new Object[] { txtFecham, txtNumRec,
					metodosGrid, cmbTipoProducto, cmbMarcas, cmbModelos,
					txtNoItem, lblCodigoSearch, lblNombreSearch, txtConcepto

			});
		
		} catch (Exception error) {
			error.printStackTrace(); 
		}
		
	}
/*****************************************************************************************************************************/
/*****************ESTABLECER LAS POLITICAS DE CAJA AL CAMBIAR LA SELECCION DE LA COMPANIA*************************************/
	public void cambiarCompania(ValueChangeEvent ev){
		String sCodComp = null;
		String[] sMetPago = null;
		Metpago[] metPago = null;
		Cafiliados[] afiliado = null;
		List<Metpago[]> lstMetPago = null;
		List<SelectItem> lstSelectItem = null;
		String sCajaId = null;
		lstMoneda = new ArrayList();
		lstAfiliado = new ArrayList();
		lstMetodosPago = new ArrayList();
		int iCaid = 0;
		AfiliadoCtrl afiCtrl = new AfiliadoCtrl();
		MetodosPagoCtrl metCtrl = new MetodosPagoCtrl();
		SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
		Vf55ca01 vf01 = null;
		
		try{

			//obtener datos de Caja
			vf01 = (Vf55ca01) ((List) m.get("lstCajas")).get(0);
			
			limpiarPantalla( );
			restablecerEstilos( );
			 
			chkContrato.setStyle("width: 20px; display: none");
			lnkSearchContrato.setStyle("display: none");
			
			
			m.remove("pr_lstFiltrosucursal");
			m.remove("pr_lstFiltrounineg");			
			ddlFiltrosucursal.dataBind();
			ddlFiltrounineg.dataBind();
			ddlTipodoc.dataBind();
			srm.addSmartRefreshId(ddlFiltrosucursal.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(ddlFiltrounineg.getClientId(FacesContext.getCurrentInstance()));
			
			sCodComp = cmbCompanias.getValue().toString();
			sCajaId = (String)m.get("sCajaId");
			iCaid = Integer.parseInt(sCajaId);
			generarObjConfigComp(sCodComp);
			
		 
	    	sMetPago = metCtrl.obtenerMetodosPagoxCaja_Compania(iCaid, cmbCompanias.getValue().toString());
	    	
			lstMetPago = new ArrayList<Metpago[]>();
			
			lstMetodosPago = new ArrayList<SelectItem>();
			lstMetodosPago.add(new SelectItem("MP","Método de Pago","Seleccione el método de pago a utilizar"));
			
			for(int i = 0; i <  sMetPago.length; i ++){
	    		Metpago formapago = com.casapellas.controles.MetodosPagoCtrl.metodoPagoCodigo( sMetPago[i] );
	    		lstMetodosPago.add(new SelectItem( formapago.getId().getCodigo(), formapago.getId().getMpago() ) );
	    		lstMetPago.add(new Metpago[]{ formapago } );
	    	}
	    	
	    	
	    	lstMetodosPago = lstSelectItem;
	    	m.put("pr_metpago", lstMetPago);
	    	m.put("pr_lstMetodosPago", lstMetodosPago);
			cmbMetodosPago.dataBind();
			
			//------------ Establecer los afiliados
			lstAfiliado = getLstAfiliado();
			if(lstAfiliado != null)
				lstAfiliado = new ArrayList<SelectItem>();
			m.put("pr_lstAfiliado",lstAfiliado);
			ddlAfiliado.dataBind();
			
			//----- Actualizar referencias de los métodos de pago.
			ReciboCtrl recCtrl = new ReciboCtrl();
			lblNumeroRecibo = recCtrl.obtenerUltimoRecibo(null, null, Integer.parseInt(sCajaId), sCodComp) + "";
			lblNumeroRecibo2.setValue(lblNumeroRecibo);
			m.put("pr_ReciboActual", lblNumeroRecibo);
			srm.addSmartRefreshId(lblNumeroRecibo2.getClientId(FacesContext.getCurrentInstance()));

			
		}catch(Exception error){
			selectedMet = new ArrayList();
			m.put("pr_selectedMet", selectedMet);
			metodosGrid.dataBind();
			cmbMetodosPago.dataBind();
			cmbMoneda.dataBind();
			cambiarVistaMetodos("MP",vf01);
			lblMensajeValidacion.setValue("Error en la operación: Error de Sistema: "+error);
			dwProcesa.setWindowState("normal");
			 
			error.printStackTrace(); 
			
		}

	}
/***********************************************************************************************************************/
/*********************ACTUALIZAR REFERENCIAS DEPENDIENDO DEL METODO DE PAGO SELECCIONADO*******************************/
public void actualizarReferencias(String sMetPago){
	//metodo = TC
	if(sMetPago.equals(MetodosPagoCtrl.TARJETA)) {										
		//Set to blank
		lblAfiliado.setValue("Afiliado");
		lblReferencia1.setValue("Cédula:");
		lblReferencia2.setValue("");
		lblReferencia3.setValue("");
		lblBanco.setValue("");
		txtReferencia1.setValue("");
		txtReferencia2.setValue("");
		txtReferencia3.setValue("");
		
		//Set to visible
		ddlAfiliado.setStyle("display:inline");
		ddlBanco.setStyle("visibility:hidden");
		txtReferencia1.setStyle("display:inline");
		txtReferencia2.setStyle("visibility:hidden");
		txtReferencia3.setStyle("visibility:hidden");
	//metodo = CHK	
	} else if(sMetPago.equals(MetodosPagoCtrl.CHEQUE)) {
		
		//Set to blank
		lblAfiliado.setValue("");
		txtReferencia1.setValue("");
		txtReferencia2.setValue("");
		txtReferencia3.setValue("");
		lblReferencia1.setValue("Número Cheque:");
		lblReferencia2.setValue("Emisor:");
		lblReferencia3.setValue("Portador:");			
		lblBanco.setValue("Banco:");								
		
		//Set to visible
		ddlAfiliado.setStyle("visibility:hidden");
		ddlBanco.setStyle("display:inline");
		txtReferencia1.setStyle("display:inline");
		txtReferencia2.setStyle("display:inline");
		txtReferencia3.setStyle("display:inline");
		
	//metodo = EFEC	
	} else if(sMetPago.equals(MetodosPagoCtrl.EFECTIVO))	{	
		//Set to blank
		lblAfiliado.setValue("");
		lblReferencia1.setValue("");
		lblReferencia2.setValue("");
		lblReferencia3.setValue("");
		lblBanco.setValue("");
		
		txtReferencia1.setValue("");
		txtReferencia2.setValue("");
		txtReferencia3.setValue("");
		//Set to not visivble
		ddlAfiliado.setStyle("visibility:hidden");
		ddlBanco.setStyle("visibility:hidden");
		txtReferencia1.setStyle("visibility:hidden");
		txtReferencia2.setStyle("visibility:hidden");
		txtReferencia3.setStyle("visibility:hidden");
		
	}
}
/**********************************************************************************************************/
/**************************AGREGAR METODOS DE PAGO A LA LISTA DESDE LINK AGREGAR **************************/

public void registrarPago(ActionEvent e) {
	Divisas d = new Divisas();
	BigDecimal tasa = BigDecimal.ONE;
	double  monto = 0.0, equiv,montoRecibido = 0.0;
	String sMonto = "",sCodcomp, sEquiv,metodo,metodoDesc="", moneda,ref1,ref2,ref3,ref4,sMonUnineg;
	boolean valido = false,bEfectivo = false, bTarjeta = false;	
	Tpararela[] tpcambio = null;
	String sVoucherManual="0";
	
	Vf55ca01 vf01 = null;
	List<String> lstDatosTrack  = null;
	List<List<String>> lstDatosTrack2 = null;
	
	BigDecimal montoendonacion = BigDecimal.ZERO;
	BigDecimal montooriginal = BigDecimal.ZERO;
	boolean aplicadonacion = false;
	
	try{
		
		metodo = cmbMetodosPago.getValue().toString();
		if(metodo.compareTo("MP") == 0) 
			return;
		
		vf01 = ((ArrayList<Vf55ca01>) m.get("lstCajas")).get(0);
		
		boolean usaSocketPos  =  vf01.getId().getCasktpos() == 'Y';
		boolean ingresoManual =  chkIngresoManual.isChecked();
		boolean vouchermanual = chkVoucherManual.isChecked();
		sVoucherManual		   = (chkVoucherManual.isChecked())? "1":"0";
		
		sMonto  = txtMonto.getValue().toString().trim();
		moneda  = cmbMoneda.getValue().toString();
		sCodcomp= cmbCompanias.getValue().toString();
		
		ref1 = txtReferencia1.getValue().toString().trim();
		ref2 = txtReferencia2.getValue().toString().trim();
		ref3 = txtReferencia3.getValue().toString().trim();
		
		ref4 = "";
		String ref5 = "";
		String sTrack = "";
		String sTerminal = "";
		
		Object[] objConfigComp = (Object[])m.get("pr_OBJCONFIGCOMP");
		boolean bUsarTasa = Boolean.valueOf(String.valueOf(objConfigComp[3]));
		String sMonedaBase = String.valueOf(objConfigComp[1]);
		
		//------ Validar si el recibo es manual y si se ha aplicado tasa de cambio a la fecha.
		String sTiporec = cmbTiporecibo.getValue().toString();
		if(sTiporec.trim().equals("MANUAL")&& bUsarTasa ){
			if(m.get("pr_tjdeRmanual") == null){
				lblMensajeValidacion.setValue("Debe fijar la tasa de cambio" +
						" a la fecha, ejecute la opción junto al " +
						" calendario de fecha manual");
				
				dwProcesa.setStyle("width:320px;height: 150px");
				dwProcesa.setWindowState("normal");
				valido = false;
				return;
			}
		}
		
		
		//&& ============= restar el monto de la donacion antes de la validacion del monto del pago.
		BigDecimal montoNeto = BigDecimal.ZERO;
		
		if ( sMonto.trim().matches(PropertiesSystem.REGEXP_AMOUNT) ) {
			montooriginal = new BigDecimal(sMonto);
			montoNeto = new BigDecimal(sMonto);
		}
		
		aplicadonacion = ( CodeUtil.getFromSessionMap("pr_MontoTotalEnDonacion") != null);
		
		if(aplicadonacion){
			montoendonacion = new BigDecimal( String.valueOf( CodeUtil.getFromSessionMap("pr_MontoTotalEnDonacion") ) );
			montoNeto = montooriginal.subtract(montoendonacion) ;
			sMonto = montoNeto.toString();
		}
		
		//&& ============ Caso en que todo el metodo de pago sea donacion
		BigDecimal montoaplicarRecibo  = BigDecimal.ZERO; 
		
		if( aplicadonacion && montoNeto.compareTo(BigDecimal.ZERO) == 0){
			sMonto = montooriginal.toString();
			
			if(m.get("pr_MontoAplicado") != null){
				montoaplicarRecibo = new BigDecimal(txtMontoAplicar.getValue().toString().replace(",", ""));									
			}
			m.remove("pr_MontoAplicado") ;
			
		}
		
		//&& ============= validar datos de los métodos de pago.
		valido = validarMpagos(metodo,sMonto, sCodcomp, moneda, ref1, ref2,
								ref3, ref4,usaSocketPos, ingresoManual);
				
		if(!valido){
			SmartRefreshManager.getCurrentInstance().addSmartRefreshId(
					dwProcesa.getClientId(FacesContext.getCurrentInstance()));
			SmartRefreshManager.getCurrentInstance().addSmartRefreshId(
					dwSolicitud.getClientId(FacesContext.getCurrentInstance()));
			return;
		}
		
		if( aplicadonacion && montoNeto.compareTo(BigDecimal.ZERO) == 0){
			sMonto = montoNeto.toString();
			
			if( montoaplicarRecibo.compareTo(BigDecimal.ZERO) == 1 )
				m.put("pr_MontoAplicado", montoaplicarRecibo.doubleValue() ) ;
		}
		
		
		selectedMet = (ArrayList<MetodosPago>) m.get("pr_selectedMet");
		
		monto = d.roundDouble( Double.parseDouble(sMonto) ) ;
		equiv = monto;
		
		List lstMetPago = (List)m.get("pr_metpago");
		
		//cambiar ubicacion de referencias				
		if (metodo.equals(MetodosPagoCtrl.CHEQUE)){//cheque
			ref4 = ref3;
			ref3 = ref2;
			ref2 = ref1;
			ref1 = ddlBanco.getValue().toString().split("@")[1];
		}else if(metodo.equals(MetodosPagoCtrl.TARJETA)){//TC
			ref3 = ref2;
			ref2 = ref1;
			ref1 = ddlAfiliado.getValue().toString();	
			
		
			if( usaSocketPos  && !vouchermanual){
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
						
					ref3 = lstDatosTrack.get(1).substring(
								lstDatosTrack.get(1).length() - 4,
								lstDatosTrack.get(1).length());// 4 ult. digitos de tarjeta
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
			else if( usaSocketPos && vouchermanual ){
				ref1 = ((ddlAfiliado.getValue().toString()).split(","))[0];
				sTerminal = ((ddlAfiliado.getValue().toString()).split(","))[1];	
			}
			bTarjeta = true;	
		}else if(metodo.equals(MetodosPagoCtrl.TRANSFERENCIA)){//Transf. Elect.
			ref3 = ref2;
			ref2 = ref1;
			ref1 = ddlBanco.getValue().toString().split("@")[1];		
		}else if(metodo.equals(MetodosPagoCtrl.DEPOSITO)){//Deposito en banco
			ref1 = ddlBanco.getValue().toString().split("@")[1];
		}
		
		int cant = 1;
		boolean flgpagos = true;					
		Metpago[] metpago = null;
		
		//poner descripcion a metodo
		for (int m = 0 ; m < lstMetPago.size();m++){
			metpago = (Metpago[])lstMetPago.get(m);
			if(metodo.trim().equals(metpago[0].getId().getCodigo().trim())){
				metodoDesc = metpago[0].getId().getMpago().trim();
				break;
			}
		}
		//------------------------------------------------------------------------------------
		//calcular el equivalente en dependencia de la moneda de pago de la unidad de negocio
		if(!moneda.equals(sMonedaBase /*"COR"*/)){
			tpcambio = (Tpararela[])m.get("pr_tpcambio");
			//buscar tasa de cambio para moneda
			for(int t = 0; t < tpcambio.length;t++){
				if(tpcambio[t].getId().getCmono().equals(moneda)){
					tasa = tpcambio[t].getId().getTcambiom();
					break;
				}
			}
			equiv = monto * tasa.doubleValue();
		}
		sEquiv = d.formatDouble(equiv);
		
		//------------------------------------
		sMonUnineg = ddlMonedaAplicada.getValue().toString();
		Tcambio[] tcJDE;
		Tpararela[] tcPar;
		
		if(sMonUnineg.equals(sMonedaBase /*"COR"*/)){	//moneda doméstica.
			if(moneda.equals(sMonedaBase /*"COR"*/)){
				equiv = monto;
				tasa = BigDecimal.ONE;
			}else{
				//buscar tasa de cambio oficial para la moneda de pago.
				if(m.get("pr_TasaJdeTcambio")==null){
					lblMensajeValidacion.setValue("No se encuentra configurada la tasa de cambio oficial jde");
					dwProcesa.setStyle("width:320px;height: 150px");
					dwProcesa.setWindowState("normal");
					return;
				}
				tcJDE = (Tcambio[])m.get("pr_TasaJdeTcambio");
				for(int l = 0; l < tcJDE.length;l++){
					if(tcJDE[l].getId().getCxcrdc().equals(moneda)){
						tasa = tcJDE[l].getId().getCxcrrd();
						break;
					}
				}
				equiv = d.roundDouble(monto * tasa.doubleValue());
			}
		}else{
			if(moneda.equals(sMonedaBase /*"COR"*/)){
				if(m.get("pr_tpcambio")== null){
					lblMensajeValidacion.setValue("No se encuentra configurada la tasa de cambio paralela");
					dwProcesa.setStyle("width:320px;height: 150px");
					dwProcesa.setWindowState("normal");
					return;
				}
				tcPar = (Tpararela[])m.get("pr_tpcambio");
				for(int t = 0; t < tcPar.length;t++){
					if((tcPar[t].getId().getCmono().equals(moneda) && tcPar[t].getId().getCmond().equals(sMonUnineg)) ||
					   (tcPar[t].getId().getCmono().equals(sMonUnineg) && tcPar[t].getId().getCmond().equals(moneda))){
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
		
		//------------- Validar que el método no sobrepase el monto aplicado(de existir montoapl)
		if(m.get("pr_MontoAplicado")!=null){ //hay monto aplicado.
			double dMontoapl = Double.parseDouble(m.get("pr_MontoAplicado").toString());
			double dMontorec = d.formatStringToDouble(lblMontoRecibido2.getValue().toString());
			dMontoapl = d.roundDouble(dMontoapl);
			dMontorec = d.roundDouble(dMontorec);
			
			if(!metodo.equals(MetodosPagoCtrl.EFECTIVO)){
				if(equiv > dMontoapl){
					valido = false;
					txtMonto.setStyleClass("frmInput2Error");
					lblMensajeValidacion.setValue("El monto del ingresado debe ser menor o igual al monto aplicado");
				}
				if(selectedMet != null && selectedMet.size()> 0 && valido){
					for(int i=0; i<selectedMet.size(); i++){
						MetodosPago mp = (MetodosPago)selectedMet.get(i);
						if(mp.getMetodo().equals(MetodosPagoCtrl.EFECTIVO))
							bEfectivo = true;
					}
					if(!bEfectivo && (dMontorec + equiv) > dMontoapl){
						valido = false; 
						txtMonto.setStyleClass("frmInput2Error");
						lblMensajeValidacion.setValue("El consolidado de montos debe ser menor o igual al monto aplicado");
					}
				}
			}
			if(!valido){					
				dwProcesa.setStyle("width:320px;height: 150px");
				dwProcesa.setWindowState("normal");
			}
		}
		//--- Voucher Manual
		sVoucherManual = (chkVoucherManual.isChecked())?"1":"0";	
		
		//---------------- Agregar a la lista los métodos de pago
		if(valido){
			
			
			String codigomarcatarjeta = "";
			String marcatarjeta = "";
			
			if( bTarjeta ){
				codigomarcatarjeta = ddlTipoMarcasTarjetas.getValue().toString().split("@")[0];
				marcatarjeta = ddlTipoMarcasTarjetas.getValue().toString().split("@")[1];
			}
			
			
			if(selectedMet != null && !selectedMet.isEmpty() ) {
				
				for (MetodosPago mp : selectedMet) {
					
					if(mp.getMetodo().equals(metodo) && mp.getMoneda().equals(moneda) && 
						mp.getReferencia().equals(ref1) && mp.getReferencia2().equals(ref2) && 
					    mp.getReferencia3().equals(ref3) && mp.getReferencia4().equals(ref4)) {
								
						
						mp.setMontorecibido( mp.getMontorecibido().add(montooriginal) );
						mp.setMontoendonacion(  mp.getMontoendonacion().add(montoendonacion) );
						mp.setIncluyedonacion(aplicadonacion);
						
						if(aplicadonacion){
							List<DncIngresoDonacion> dncs = (ArrayList<DncIngresoDonacion>) CodeUtil.getFromSessionMap("pr_lstDonacionesRecibidas") ;
							mp.getDonaciones().addAll(dncs);
						}
						
						monto = monto + mp.getMonto();																			
						equiv = equiv + mp.getEquivalente();
								
						sMonto = d.formatDouble(monto);
						mp.setMonto(d.roundDouble(monto));
						mp.setEquivalente(d.roundDouble(equiv));
						mp.setVmanual(sVoucherManual);
						mp.setReferencia5("");
						
						if (bTarjeta && usaSocketPos && !vouchermanual) {
							mp.setTrack(sTrack);
							mp.setTerminal(sTerminal);
							mp.setReferencia5(ref5);
							mp.setMontopos(monto);
							mp.setVmanual("2");
						}
						
						mp.setReferencia6("");
						mp.setReferencia7("");
						mp.setNombre("");
						flgpagos = false;
					}								
				}				
			} else {
				
				MetodosPago metpagos = new MetodosPago(metodoDesc, metodo,
						moneda, monto, tasa, equiv, ref1, ref2, ref3, ref4,
						sVoucherManual, 0);
				
				metpagos.setReferencia5("");
				
				metpagos.setMontorecibido(montooriginal );
				metpagos.setMontoendonacion(montoendonacion);
				metpagos.setIncluyedonacion(aplicadonacion);
				
				if(aplicadonacion){
					metpagos.setDonaciones(
						new ArrayList<DncIngresoDonacion>((ArrayList<DncIngresoDonacion>)
								CodeUtil.getFromSessionMap("pr_lstDonacionesRecibidas")) ) ;
				}
				
				if (bTarjeta && usaSocketPos && !vouchermanual) {
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
				
				selectedMet = new ArrayList<MetodosPago>();				
				selectedMet.add(metpagos);
				flgpagos = false;
			}
			
			if (flgpagos){
				
				MetodosPago metpagos = new MetodosPago(metodoDesc, metodo,
						moneda, monto, tasa, equiv, ref1, ref2, ref3, ref4,
						sVoucherManual, 0);
				
				metpagos.setMontorecibido(montooriginal );
				metpagos.setMontoendonacion(montoendonacion);
				metpagos.setIncluyedonacion(aplicadonacion);
			
				if(aplicadonacion){
					metpagos.setDonaciones(
						new ArrayList<DncIngresoDonacion>((ArrayList<DncIngresoDonacion>)
								CodeUtil.getFromSessionMap("pr_lstDonacionesRecibidas")) ) ;
				}
				
				metpagos.setReferencia5("");
				
				if (bTarjeta && usaSocketPos && !vouchermanual) {
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
				CodeUtil.removeFromSessionMap(new String[] { "pr_MontoTotalEnDonacion","pr_lstDonacionesRecibidas" });
			}
			
			
			//&& ============= calcular el monto recibido
			montoRecibido = 0;
			for (MetodosPago mpTmp : selectedMet) 
				montoRecibido += mpTmp.getEquivalente();
			
			lblMontoRecibido2.setValue(d.formatDouble(montoRecibido));
			m.put("pr_montoRecibibo", montoRecibido);	
			
			//--------- Verificar que exista monto aplicado, si hay, calcular cambio o pendiente.
			if(m.get("pr_MontoAplicado")!=null){
				double dMontoapl;
				dMontoapl = Double.parseDouble(m.get("pr_MontoAplicado").toString());
				dMontoapl = d.roundDouble(dMontoapl);						
				determinarCambio(selectedMet, montoRecibido, dMontoapl, ddlMonedaAplicada.getValue().toString());
			}else{
				txtMontoAplicar.setValue(d.formatDouble(montoRecibido));
			}
			m.put("pr_selectedMet", selectedMet);
			
			//Setting Blank
			txtMonto.setValue("");
			getTxtReferencia1().setValue("");
			getTxtReferencia2().setValue("");
			getTxtReferencia3().setValue("");
			txtCambioForaneo.setStyleClass("frmInput2");
			track.setValue("");
			txtCambioForaneo.setStyleClass("frmInput2");
			txtNoTarjeta.setValue("");	
			txtFechaVenceT.setValue("");	

			metodosGrid.dataBind();
			
			CodeUtil.refreshIgObjects(new Object[]{ txtMontoAplicar, metodosGrid, track	}) ;
			
		}
		
	}catch(Exception ex){
		ex.printStackTrace();
	}
}

/******************************************************************************************/
/********** 	validación de los valores para los métodos de pago ingresados	***********/
	public boolean validarMpagos(String metodo, String sMonto,String sCodcomp,
					String sMoneda, String ref1,String ref2,String ref3,
					String ref4, boolean usaSckPos, boolean usaManual){
		
		boolean validado = true, bErrorPoliticas = false;
		int y = 158,iCaid=0;
		String sEstiloMserror="",sMensajeError = "",sCajaId="";
		Divisas divisas = new Divisas();
		CtrlCajas cc = new CtrlCajas();
		CtrlPoliticas polCtrl = new CtrlPoliticas();
		Matcher matNumero=null,matAlfa1=null,matAlfa2=null,matAlfa3=null,matAlfa4=null;
		Pattern pAlfa;
		boolean bConfirmAuto = false;
		
		double dMontoAplicar = 0;
		double dEquivPago = 0;
		double monto = 0;
		
		try {
			
			sMonto = sMonto.trim();
			
			
			//&& ====== Determinar si la caja confirma para la compania seleccionada.
			if(m.get("pr_F55CA014") ==  null){
				validado = false;
				lblMensajeValidacion.setValue("No se encontró compañías configuradas");
				dwProcesa.setStyle("width:320px;height: 180px");
				dwProcesa.setWindowState("normal");
			}
			F55ca014[] f14s = (F55ca014[])m.get("pr_F55CA014");
			for (F55ca014 f14 : f14s) {
				if(f14.getId().getC4rp01().trim().equals(sCodcomp.trim()) && f14.getId().isConfrmauto()){
					bConfirmAuto = true;
					break;
				}
			}
			
			selectedMet = (ArrayList<MetodosPago>) m.get("pr_selectedMet");
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
				dwProcesa.setWindowState("normal");	
				y = y + 5;
			}
			else if (matNumero == null || !matNumero.matches() ||monto == 0){
				txtMonto.setValue("");
				validado = false;
				txtMonto.setStyleClass("frmInput2Error");
				sMensajeError = sMensajeError +sEstiloMserror+ "El monto ingresado no es correcto<br>";
				dwProcesa.setWindowState("normal");	
				y = y + 5;
			}
			
			Divisas dv = new Divisas();
			BigDecimal tasaPrl = BigDecimal.ZERO;
			BigDecimal tasaJde = BigDecimal.ZERO;
			
			Tpararela[] tpcambio = (Tpararela[])m.get("pr_tpcambio");
			for(int t = 0; t < tpcambio.length;t++){
				if(tpcambio[t].getId().getCmono().equals(sMoneda)){
					tasaPrl = tpcambio[t].getId().getTcambiom();
					break;
				}
			}	
			Tcambio[] tcJDE = (Tcambio[]) m.get("pr_TasaJdeTcambio");
			for (int l = 0; l < tcJDE.length; l++) {
				if (tcJDE[l].getId().getCxcrdc().equals(sMoneda)) {
					tasaJde = tcJDE[l].getId().getCxcrrd();
					tasaJde = tcJDE[l].getId().getCxcrrd();
					break;
				}
			}

			
			Object[] objConfigComp = (Object[])m.get("pr_OBJCONFIGCOMP");
			String sMonedaBase = String.valueOf(objConfigComp[1]);
			String sMonedaApli = ddlMonedaAplicada.getValue().toString();
			
			dEquivPago = monto;
			if(sMoneda.compareTo(sMonedaApli) != 0){
				if(sMoneda.compareTo(sMonedaBase) == 0 &&
					sMonedaApli.compareTo(sMonedaBase) != 0){
					dEquivPago = monto/tasaPrl.doubleValue();
				}
				if(sMoneda.compareTo(sMonedaBase) != 0 &&
					sMonedaApli.compareTo(sMonedaBase) == 0){
					dEquivPago = monto * tasaJde.doubleValue();
				}
				dEquivPago = dv.roundDouble(dEquivPago);
			}
			
			dMontoAplicar = dEquivPago; 
			if(m.get("pr_MontoAplicado") != null){
				dMontoAplicar = dv.formatStringToDouble(
									txtMontoAplicar.getValue().toString());
			}
			
			if(validado){
				//-------------  método: 5 - efectivo
				if (metodo.equals(MetodosPagoCtrl.EFECTIVO)){

				}else
				//-------------------- método: cheques Q.
				if(metodo.equals(MetodosPagoCtrl.CHEQUE)){
					if(!ref1.matches("^[0-9]{1,8}$")){
						validado = false;
						y = y + 7;
						sMensajeError = sMensajeError + sEstiloMserror+ "8 Dígitos para número de cheque es requerido<br>";
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
						txtReferencia2.setStyleClass("frmInput2Error");
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
						sMensajeError = sMensajeError + sEstiloMserror
									+ "La cantidad de caracteres del campo" +
									" <b>Portador<b/> es muy alta (lim. 150)<br>";
						txtReferencia3.setStyleClass("frmInput2Error");
						y = y + 15;
					}
				}
				//------------------------ método de pago tarjeta de crédito H				
				else if(metodo.equals(MetodosPagoCtrl.TARJETA)){
					
					if(usaSckPos){
						
						String codpos = ddlAfiliado.getValue().toString();
						String sTrack = track.getValue().toString();
						String notarjeta  = txtNoTarjeta.getValue().toString().trim();
						String fechavence = txtFechaVenceT.getValue().toString().trim();
						
						sMensajeError = PosCtrl.validaPagoSocket(sMoneda,
								dEquivPago, dMontoAplicar, usaManual, ref1, ref2,
								ref3, iCaid, sCodcomp, codpos, selectedMet, 
								sTrack, notarjeta, fechavence, monto) ;
						
						if(sMensajeError.compareTo("") != 0){  //sMensajeError = "-1"
							
							if (sMensajeError.compareTo("-1") == 0) {
								lblMensajeAutorizacion.setValue("El monto " +
										"ingresado no cumple con las politicas de caja");
								dwSolicitud.setWindowState("normal");
								txtFecha.setValue(new Date());
								m.put("pr_montoIsertando", monto);
								m.put("pr_monto", monto);

							}else{
								dwProcesa.setWindowState("normal");
								lblMensajeValidacion.setValue(sMensajeError);
							}
							return false;
						}
						
					}else{
						if(ddlAfiliado.getValue().toString().equals("01")){
							validado = false;
							ddlAfiliado.setStyleClass("frmInput2Error3");
							sMensajeError = sMensajeError + sEstiloMserror+ "Seleccione un afiliado<br>";
							dwProcesa.setWindowState("normal");	
							y = y + 5;
						}
						if(ref1.equals("")) {						
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
						if(ref2.equals("")){
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
				}
				
				//&& ================ Método de pago: Transferencias.
				if(metodo.compareTo(MetodosPagoCtrl.TRANSFERENCIA) == 0){
					
					//validamos si la referencia agregada ya ha sido utilizada para el mismo banco 
					if(polCtrl.validarReferenciaBancaria(Integer.parseInt(sCajaId), ddlBanco.getValue().toString().split("@")[1], ref2,"T"))
					{
						txtReferencia2.setStyleClass("frmInput2Error");
						sMensajeError += sEstiloMserror + "La referencia ya ha sido utilizada, favor verificar e ingresar una referencia válida";
						return validado = false;					
					}
					 
					//&& ================== validacion para el numero de identificacion. 03-02-2015
					if(!ref1.matches("^[A-Za-z0-9-]{1,100}$")){
						sMensajeError = sMensajeError + sEstiloMserror+  "Datos inválidos para el campo Identificaión (1-100 letras)";
						txtReferencia1.setStyleClass("frmInput2Error");
						return validado = false;
					}
					//&& ================== validacion para el numero de referencia. 03-02-2015
					if(!ref2.matches("^[0-9]{1,8}$")){
						txtReferencia2.setStyleClass("frmInput2Error");
						sMensajeError += sEstiloMserror + "Datos inválidos para número de referencia (1 a 8 dígitos)";
						return validado = false;
					}
				}
				//&& ================ Deposito directo en banco
				if(metodo.compareTo(MetodosPagoCtrl.DEPOSITO) == 0){
					if(!ref2.matches("^[0-9]{1,8}$")){
						txtReferencia2.setStyleClass("frmInput2Error");
						sMensajeError += sEstiloMserror + "Datos inválidos para número de referencia (1 a 8 dígitos)";
						return validado = false;
					}
				}
				
				//validamos si la referencia agregada ya ha sido utilizada para el mismo banco 
				if(polCtrl.validarReferenciaBancaria(Integer.parseInt(sCajaId), ddlBanco.getValue().toString().split("@")[1], ref2,"N"))
				{
					txtReferencia2.setStyleClass("frmInput2Error");
					sMensajeError += sEstiloMserror + "La referencia ya ha sido utilizada, favor verificar e ingresar una referencia válida";
					return validado = false;					
				}
				
				//-------------------------------------------------------------------
				if(validado){
					if(!polCtrl.validarMonto(iCaid, sCodcomp, sMoneda, metodo, monto)){
						validado = false;
						bErrorPoliticas = true;
						lblMensajeAutorizacion.setValue("El monto ingresado no cumple con las politicas de caja");
						dwSolicitud.setWindowState("normal");
						Date dFechaActual = new Date();
						txtFecha.setValue(dFechaActual);
						m.put("pr_montoIsertando", monto);
						m.put("pr_monto", monto);
					}else{
						boolean bDuplicado = false;
						if(selectedMet!=null && selectedMet.size() > 0 && validado){
							double dMontoIngresar = monto;
							for(int i=0; i<selectedMet.size(); i++){
								MetodosPago mp = (MetodosPago)selectedMet.get(i);
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
								m.put("pr_monto", monto);
								m.put("pr_montoIsertando", dMontoIngresar);
							}
						}
					}
				}
				//-------------------------------------------------------------------
			}	
			
		} catch (Exception error) {
			
			validado = false;
			lblMensajeValidacion.setValue("No se puede registrar el pago: Error de sistema: "+error);
			dwProcesa.setStyle("width:320px;height: 180px");
			dwProcesa.setWindowState("normal");
			error.printStackTrace(); 
			
		}finally{
			
			if(!validado && !bErrorPoliticas){
				lblMensajeValidacion.setValue(sMensajeError);
				dwProcesa.setStyle("width:320px;height:"+y+"px");
				dwProcesa.setWindowState("normal");
			}
			
		}
		return validado;
	}
/******************VALIDAR LOS DATOS DEL PAGO INGRESADO************************************/
public boolean validarPago(String metodo, String sMonto, String ref1,String ref2,String ref3,String ref4){
	boolean validado = true;
	double monto = 0;
	String sMensajeError = "",sCajaId="";
	int y = 158;
	CtrlPoliticas polCtrl = new CtrlPoliticas();
	selectedMet = (ArrayList<MetodosPago>) m.get("pr_lstMetodosPago");
	Divisas divisas = new Divisas();
	
	try{	
		sCajaId = (String)m.get("sCajaId");
		restablecerEstilosPago();
		
		//expresion regular solo numeros y alfanuméricos.
		Matcher matNumero = null;
		Matcher matAlfa1 = null;
		Matcher matAlfa2 = null;
		Matcher matAlfa3 = null;
		Matcher matAlfa4 = null;
		
		if(!sMonto.equals("")){
			Pattern pNumero = Pattern.compile("^[0-9]+\\.?[0-9]*$");
			matNumero = pNumero.matcher(sMonto);
		
			if (matNumero.matches())
				monto = divisas.formatStringToDouble(getTxtMonto().getValue().toString());
		}
		
		if(txtMonto.getValue() != null){
			sMonto = txtMonto.getValue().toString();
			Pattern pAlfa = Pattern.compile("^\\w*$");
			matAlfa1 = pAlfa.matcher(ref1);
			matAlfa2 = pAlfa.matcher(ref2);
			matAlfa3 = pAlfa.matcher(ref3);
			matAlfa4 = pAlfa.matcher(ref4);
		}	
		//--------------------------------------------------------
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
		else if (!polCtrl.validarMonto(Integer.parseInt(sCajaId), cmbCompanias.getValue().toString(), cmbMoneda.getValue().toString(), cmbMetodosPago.getValue().toString(), Double.parseDouble(sMonto))){
			validado = false;
			lblMensajeAutorizacion.setValue("El monto ingresado no cumple con las politicas de caja");
			dwSolicitud.setWindowState("normal");
			Date dFechaActual = new Date();
			txtFecha.setValue(dFechaActual);
			if(!txtMonto.getValue().toString().trim().equals("")){
				sMonto = txtMonto.getValue().toString();
			}
			monto = Double.parseDouble(sMonto);
			m.put("pr_montoIsertando", monto);
			m.put("pr_monto", monto);
		}
		//--------------------------------------------------------
		//--------------------------------------------------------
		
		//Valida efectivo
		if (metodo.equals(MetodosPagoCtrl.EFECTIVO)){
			if(validado && selectedMet!=null && selectedMet.size() > 0){
				MetodosPago[] mpagos = new MetodosPago[selectedMet.size()];
				for(int i = 0; i < selectedMet.size(); i++){
					mpagos[i] = ((MetodosPago)selectedMet.get(i));
					if (mpagos[i].getMetodo().trim().equals("Efectivo")){
						Pattern pNumero = Pattern.compile("^[0-9]+\\.?[0-9]*$");
						matNumero = pNumero.matcher(String.valueOf(mpagos[i].getMonto()));
						if(matNumero.matches()){
							monto = monto + mpagos[i].getMonto();
						}
					}	
				}
				if (!polCtrl.validarMonto(Integer.parseInt(sCajaId), cmbCompanias.getValue().toString(), cmbMoneda.getValue().toString(), cmbMetodosPago.getValue().toString(),monto)){
					validado = false;
					lblMensajeAutorizacion.setValue("El consolidado de los montos del método de pago no cumple con las politicas de caja");
					dwSolicitud.setWindowState("normal");
					Date dFechaActual = new Date();
					txtFecha.setValue(dFechaActual);
					m.put("pr_monto", monto);
					txtFecha.setValue(dFechaActual);
					if(!txtMonto.getValue().toString().trim().equals("")){
						sMonto = txtMonto.getValue().toString();
					}
					monto = Double.parseDouble(sMonto);
					m.put("pr_montoIsertando", monto);
				}
			}
		//Valida CHK
		}else if(metodo.equals(MetodosPagoCtrl.CHEQUE)) {
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
			//validar referencias
			if(ref1.equals("")) {						
				validado = false;
				sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Número de cheque requerido<br>";
				txtReferencia1.setStyleClass("frmInput2Error");
				dwProcesa.setWindowState("normal");				
				y = y + 7;
			}else if(!matAlfa1.matches()){
				validado = false;
				sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El campo <b>Número de cheque<b/> contiene caracteres invalidos<br>";
				txtReferencia1.setStyleClass("frmInput2Error");
				dwProcesa.setWindowState("normal");				
				y = y + 7;
			}else if(ref1.length() > 150){
				validado = false;
				sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> La cantidad de caracteres del campo <b>Número de cheque<b/> es muy alta (lim. 150)<br>";
				txtReferencia1.setStyleClass("frmInput2Error");
				dwProcesa.setWindowState("normal");				
				y = y + 20;
			}
			if(ref2.equals("")){
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
			else if (!polCtrl.validarMonto(Integer.parseInt(sCajaId), cmbCompanias.getValue().toString(), cmbMoneda.getValue().toString(), cmbMetodosPago.getValue().toString(), Double.parseDouble(sMonto))){
				validado = false;
				lblMensajeAutorizacion.setValue("El monto ingresado no cumple con las politicas de caja");
				dwSolicitud.setWindowState("normal");
				Date dFechaActual = new Date();
				txtFecha.setValue(dFechaActual);
				if(!txtMonto.getValue().toString().trim().equals("")){
					sMonto = txtMonto.getValue().toString();
				}
				monto = Double.parseDouble(sMonto);
				m.put("pr_montoIsertando", monto);
				m.put("pr_monto", monto);
			}
			else if(selectedMet.size() > 0){
				MetodosPago[] mpagos = new MetodosPago[selectedMet.size()];
				for(int i = 0; i < selectedMet.size(); i++){
					mpagos[i] = ((MetodosPago)selectedMet.get(i));
					if (mpagos[i].getMetodo().trim().equals("Cheque/Efectivo")){
						Pattern pNumero = Pattern.compile("^[0-9]+\\.?[0-9]*$");
						matNumero = pNumero.matcher(String.valueOf(mpagos[i].getMonto()));
						if(matNumero.matches()){
							monto = monto + mpagos[i].getMonto();
						}
					}	
				}
				if (!polCtrl.validarMonto(Integer.parseInt(sCajaId), cmbCompanias.getValue().toString(), cmbMoneda.getValue().toString(), cmbMetodosPago.getValue().toString(),monto)){
					validado = false;
					lblMensajeAutorizacion.setValue("El consolidado de los montos del método no cumple con las politicas de caja");
					dwSolicitud.setWindowState("normal");
					Date dFechaActual = new Date();
					txtFecha.setValue(dFechaActual);
					m.put("pr_monto", monto);
					if(!txtMonto.getValue().toString().trim().equals("")){
						sMonto = txtMonto.getValue().toString();
					}
					monto = Double.parseDouble(sMonto);
					m.put("pr_montoIsertando", monto);
				}
			}
		//Valida TC	
		} else if(metodo.equals(MetodosPagoCtrl.TARJETA)) {
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
			//Valida Referencias
			if(ref1.equals("")) {						
				validado = false;
				txtReferencia1.setStyleClass("frmInput2Error");
				sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Número de cédula requerido<br>";
				dwProcesa.setWindowState("normal");	
				y = y + 5;
			}else if(!matAlfa1.matches()){
				validado = false;
				sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El campo <b>Número de cédula<b/> contiene caracteres invalidos<br>";
				txtReferencia1.setStyleClass("frmInput2Error");
				dwProcesa.setWindowState("normal");				
				y = y + 7;
			}else if(ref1.length() > 150){
				validado = false;
				sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> La cantidad de caracteres del campo <b>Número de cédula<b/> es muy alta (lim. 150)<br>";
				txtReferencia1.setStyleClass("frmInput2Error");
				dwProcesa.setWindowState("normal");				
				y = y + 7;
			}
			else if (!polCtrl.validarMonto(Integer.parseInt(sCajaId), cmbCompanias.getValue().toString(), cmbMoneda.getValue().toString(), cmbMetodosPago.getValue().toString(), Double.parseDouble(sMonto))){
				validado = false;
				lblMensajeAutorizacion.setValue("El monto ingresado no cumple con las politicas de caja");
				dwSolicitud.setWindowState("normal");	
				Date dFechaActual = new Date();
				txtFecha.setValue(dFechaActual);
				if(!txtMonto.getValue().toString().trim().equals("")){
					sMonto = txtMonto.getValue().toString();
				}
				monto = Double.parseDouble(sMonto);
				m.put("pr_montoIsertando", monto);
				m.put("pr_monto", monto);
			}else if(selectedMet.size() > 0){
				MetodosPago[] mpagos = new MetodosPago[selectedMet.size()];
				for(int i = 0; i < selectedMet.size(); i++){
					mpagos[i] = ((MetodosPago)selectedMet.get(i));
					if (mpagos[i].getMetodo().trim().equals("Tarjeta de Credito")){
						Pattern pNumero = Pattern.compile("^[0-9]+\\.?[0-9]*$");
						matNumero = pNumero.matcher(String.valueOf(mpagos[i].getMonto()));
						if(matNumero.matches()){
							monto = monto + mpagos[i].getMonto();
						}
					}	
				}
				if (!polCtrl.validarMonto(Integer.parseInt(sCajaId), cmbCompanias.getValue().toString(), cmbMoneda.getValue().toString(), cmbMetodosPago.getValue().toString(),monto)){
					validado = false;
					lblMensajeAutorizacion.setValue("El consolidado de los montos no cumple con las politicas de caja");
					dwSolicitud.setWindowState("normal");
					Date dFechaActual = new Date();
					txtFecha.setValue(dFechaActual);
					m.put("pr_monto", monto);
					if(!txtMonto.getValue().toString().trim().equals("")){
						sMonto = txtMonto.getValue().toString();
					}
					monto = Double.parseDouble(sMonto);
					m.put("pr_montoIsertando", monto);
				}
			} 
		}
		else if(metodo.equals(MetodosPagoCtrl.DEPOSITO)){
		//validamos si la referencia agregada ya ha sido utilizada para el mismo banco 
			if(polCtrl.validarReferenciaBancaria(Integer.parseInt(sCajaId), ddlBanco.getValue().toString().split("@")[1], ref2,"N"))
			{
				validado = false;
				lblMensajeAutorizacion.setValue("La referencia ya ha sido utilizada, favor verificar e ingresar una referencia válida");
				dwSolicitud.setWindowState("normal");
				
			}
		}
		//validar Transferencias.
		else if(metodo.equals(MetodosPagoCtrl.TRANSFERENCIA)){
			
			//validamos si la referencia agregada ya ha sido utilizada para el mismo banco 
			if(polCtrl.validarReferenciaBancaria(Integer.parseInt(sCajaId), ddlBanco.getValue().toString().split("@")[1], ref2,"T"))
			{
				validado = false;
				lblMensajeAutorizacion.setValue("La referencia ya ha sido utilizada, favor verificar e ingresar una referencia válida");
				dwSolicitud.setWindowState("normal");
				
			}
			//validar montos
//			if(sMonto.equals("")){
//				validado = false;
//				txtMonto.setStyleClass("frmInput2Error");
//				sMensajeError = sMensajeError + "<img width=\"7\" src=\"/MCAJA/theme/icons/redCircle.jpg\" border=\"0\" /> El monto es requerido<br>";
//				dwProcesa.setWindowState("normal");
//				y = y + 7;
//			}
//			else if (matNumero == null || !matNumero.matches() || monto == 0){
//				txtMonto.setValue("");
//				validado = false;
//				txtMonto.setStyleClass("frmInput2Error");
//				sMensajeError = sMensajeError + "<img width=\"7\" src=\"/MCAJA/theme/icons/redCircle.jpg\" border=\"0\" /> El monto ingresado no es correcto<br>";
//				dwProcesa.setWindowState("normal");	
//				y = y + 7;
//			}		
		}		
		lblMensajeValidacion.setValue(sMensajeError);
		dwProcesa.setStyle("width:320px;height:"+y+"px");
	}catch(Exception ex){
		ex.printStackTrace();
	}
	//metodosGrid.dataBind();
	return validado;
}
/**********************************************************************************************************/
/****************REESTABLECER ESTILOS A DATOS DEL PAGO****************************************************************/
public void restablecerEstilosPago(){
	try{
		
		txtMonto.setStyleClass("frmInput2");
		cmbMetodosPago.setStyleClass("frmInput2ddl");
		ddlBanco.setStyleClass("frmInput2ddl");
		ddlAfiliado.setStyleClass("frmInput2ddl");	
		txtReferencia1.setStyleClass("frmInput2");
		txtReferencia2.setStyleClass("frmInput2");
		txtReferencia3.setStyleClass("frmInput2");
	}catch(Exception ex){
		ex.printStackTrace();
	}
}

/**********************************************************************************************************/
/****************ELIMINA METODOS DE PAGO DEL GRID****************************************************************/

	public void borrarPago(ActionEvent e) {
		Divisas dv = new Divisas();
		double montoRecibido = 0;

		try {

			MetodosPago mpSel = (MetodosPago) m.get("metodopagoborrar");
			selectedMet = (ArrayList<MetodosPago>) m.get("pr_selectedMet");

			// && ========== Borrar datos de la donacion.
			if (mpSel.isIncluyedonacion()) {
				CodeUtil.removeFromSessionMap(new String[] 
					{ "pr_MontoTotalEnDonacion","pr_lstDonacionesRecibidas" });
			}

			// && ========== remover solicitudes, en caso de existir.
			ArrayList<Solicitud> lstSolicitud = m
					.containsKey("pr_lstSolicitud") ? (ArrayList<Solicitud>) m
					.get("pr_lstSolicitud") : new ArrayList<Solicitud>();
			CtrlSolicitud.removerSolicitud(lstSolicitud, mpSel);

			// && ========== remover el pago de la lista de los registrados.
			MetodosPagoCtrl.removerPago(selectedMet, mpSel);

			for (MetodosPago mpTmp : selectedMet)
				montoRecibido += mpTmp.getEquivalente();

			m.put("pr_selectedMet", selectedMet);
			m.put("pr_montoRecibibo", montoRecibido);

			lblMontoRecibido2.setValue(dv.formatDouble(montoRecibido));
			metodosGrid.dataBind();

			// ---------- Verificar la actualización del monto aplicado.
			if (m.get("pr_MontoAplicado") != null) {
				double dMontoapl;
				dMontoapl = Double.parseDouble(m.get("pr_MontoAplicado").toString());
				dMontoapl = dv.roundDouble(dMontoapl);
				determinarCambio(selectedMet, montoRecibido, dMontoapl,
						ddlMonedaAplicada.getValue().toString());
			} else {
				txtMontoAplicar.setValue(dv.formatDouble(montoRecibido));
			}
			SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
			srm.addSmartRefreshId(txtMontoAplicar.getClientId(FacesContext.getCurrentInstance()));

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			dwBorrarPago.setWindowState("hidden");
			m.remove("metodopagoborrar");
		}
	}
/***********************MODIFICAR METODOS DE PAGO EN EL GRID******************************************************/
public void modificarPago(CellValueChangeEvent e){
	SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
	Divisas divisas = new Divisas();
    BigDecimal tasa = BigDecimal.ONE;
    double equiv = 0;
    String sEquiv;
    boolean cumplePoliticas = true;
    double dNewMonto = 0.0, montoRecibido = 0.0;
    
    boolean montoValido = true;
    try{
    	//Determine the row and column being updated
        List rows = metodosGrid.getRows();
        RowItem row  = (RowItem)rows.get(e.getPosition().getRow());
        int colpos = e.getPosition().getCol();
        // obtener los datos viejos de la fila (anteriores a modificacion).
        MetodosPago mPago = (MetodosPago)metodosGrid.getDataRow(row);
        m.put("pr_mPagoOld", mPago);
        m.put("pr_sMontoOld", mPago.getMonto());
        // obtener metodos de pago anteriores a modificacion
        selectedMet = (ArrayList<MetodosPago>) m.get("pr_lstMetodosPago");
        MetodosPago[] mPagoOld = new MetodosPago[selectedMet.size()];
        for(int i = 0; i < selectedMet.size(); i++){
        	mPagoOld[i] = (MetodosPago)selectedMet.get(i);
        }
        //obtener el nuevo monto y asignar a Mpago
        String sNewMonto = "";
        //actualizar monto
        if(colpos == 3){
        	sNewMonto = e.getNewValue().toString();
        	montoValido = validarModificacionMonto(sNewMonto);
        	if(montoValido){
	        	dNewMonto = divisas.formatStringToDouble(sNewMonto);
	        	mPago.setMonto(dNewMonto);
	        	
	        	if (cmbMoneda.getValue().toString().equals("COR")){// moneda cordoba
	        		mPago.setTasa(tasa);
	        		mPago.setEquivalente(dNewMonto);
			        //buscar el registro en el datasource y sustituirlo
			        for (int j = 0; j < mPagoOld.length; j ++){
			    		if (mPago.getMetodo().equals(mPagoOld[j].getMetodo()) && mPago.getEquivalente() == mPago.getEquivalente()){
			    			selectedMet.remove(j);
			    			selectedMet.add(j, mPago);
			    			montoRecibido = montoRecibido + mPago.getMonto();
			    		}else{//formatear string anterior
			    			mPagoOld[j].setMonto(mPagoOld[j].getMonto());
			    			selectedMet.remove(j);
			    			selectedMet.add(j, mPagoOld[j]);
			    			montoRecibido = montoRecibido + mPagoOld[j].getMonto();
			    		}
			    	}
	        	}else{//moneda foranea
	        		//leer tasa
	        		Tpararela[] tpcambio = null;
	        		tpcambio = (Tpararela[])m.get("pr_tpcambio");
					//buscar tasa de cambio para moneda
					for(int t = 0; t < tpcambio.length;t++){
						if(tpcambio[t].getId().getCmono().equals(mPago.getMoneda())){
							tasa = tpcambio[t].getId().getTcambiom();
							break;
						}
					}
					mPago.setTasa(tasa);
					mPago.setEquivalente(dNewMonto*tasa.doubleValue());
					//buscar el registro en el datasource y sustituirlo
			        for (int j = 0; j < mPagoOld.length; j ++){
			    		if (mPago.getMetodo().equals(mPagoOld[j].getMetodo()) && mPago.getEquivalente()==(mPago.getEquivalente())){
			    			selectedMet.remove(j);
			    			selectedMet.add(j, mPago);
			    			montoRecibido = montoRecibido + mPago.getMonto();
			    		}else{//formatear string anterior
			    			mPagoOld[j].setMonto(mPagoOld[j].getMonto());
			    			selectedMet.remove(j);
			    			selectedMet.add(j, mPagoOld[j]);
			    			montoRecibido = montoRecibido + mPagoOld[j].getMonto();
			    		}
			    	}
					//calcular equivalente
	        	}//moneda 
	        	lblMontoRecibido2.setValue(divisas.formatDouble(montoRecibido));
	        	srm.addSmartRefreshId(lblMontoRecibido2.getClientId(FacesContext.getCurrentInstance()));
	        	//meter lista en el mapa   
	        	//metodosGrid.dataBind();	
	        	
	        	m.remove("pr_selectedMet");
	    		m.put("pr_selectedMet", selectedMet);
	    		
        	}else{//monto invalido
        		// buscar el registro en el datasource y sustituirlo
		        for (int j = 0; j < mPagoOld.length; j ++){
		    		if (mPago.getMetodo().equals(mPagoOld[j].getMetodo()) && mPago.getEquivalente()==(mPago.getEquivalente())){
		    			selectedMet.remove(j);
		    			mPago.setMonto(mPagoOld[j].getMonto());
		    			selectedMet.add(j, mPago);
		    		}
		    	}
	        	m.remove("pr_selectedMet");
	    		m.put("pr_selectedMet", selectedMet);
        	}
        }  
		
		srm.addSmartRefreshId(metodosGrid.getClientId(FacesContext.getCurrentInstance()));
		srm.addSmartRefreshId(dwProcesa.getClientId(FacesContext.getCurrentInstance()));
    }catch(Exception ex){
    	ex.printStackTrace();
	}
	
}
/********************************************************************************************/
public boolean validarModificacionMonto(String sNewMonto){
	boolean validado = true;
	CtrlPoliticas pol = new CtrlPoliticas();
	 Divisas divisas = new Divisas();
	try{
		Pattern p = Pattern.compile("^[0-9]+\\.?[0-9]*$");
		Matcher mat = p.matcher(sNewMonto);
		if (sNewMonto.trim().equals("")){
			validado = false;
			lblMensajeValidacion.setValue("El valor ingresado no es correcto");
			dwProcesa.setWindowState("normal");
		}else if(!mat.matches()){
			validado = false;
			lblMensajeValidacion.setValue("El valor ingresado no es correcto");
			dwProcesa.setWindowState("normal");
		}else if(!pol.validarMonto(Integer.parseInt(m.get("sCajaId").toString()), cmbCompanias.getValue().toString(), cmbMoneda.getValue().toString(), cmbMetodosPago.getValue().toString(), divisas.formatStringToDouble(sNewMonto))){ 
			validado = false;
			lblMensajeValidacion.setValue("El monto ingresado no cumple con las politicas de caja");
			dwProcesa.setWindowState("normal");
		}
	}catch(Exception ex){
		validado = false;
		ex.printStackTrace();
	}
	return validado;
}
/****************************Lista Metodos de Pago por Caja***********************************************************/
public List getLstMetodosPago() {
	MetodosPagoCtrl metCtrl = new MetodosPagoCtrl();
	Metpago[] metPago = null;
	String[] sMetPago = null;
	String sCodComp="";
	int iCaid=0;
	List<Metpago[]> lstMetPago = null;
	List<SelectItem> lstMetodos = new ArrayList<SelectItem>();
	
    try {
    	
    	if( CodeUtil.getFromSessionMap("pr_lstMetodosPago") == null ){
			
    		iCaid = (Integer.parseInt(CodeUtil.getFromSessionMap("sCajaId").toString()));
			sCodComp = cmbCompanias.getValue().toString();
    		
			
			//&& ================ Obtener la lista de metodos de pago configurados para la caja y compania.
			
			List<Vf55ca012> MetodosPagoConfigurados =  DonacionesCtrl.obtenerMpagosConfiguradosCaja(sCodComp.trim(), iCaid);
			CodeUtil.putInSessionMap("pr_MetodosPagoConfigurados", MetodosPagoConfigurados );
			
			lstMetPago = new ArrayList<Metpago[]>();
			lstMetodosPago = new ArrayList<SelectItem>();  		
			lstMetodosPago.add(new SelectItem("MP","Método de Pago","Seleccione el método de pago a utilizar"));
			
			sMetPago = metCtrl.obtenerMetodosPagoxCaja_Compania(iCaid, sCodComp);
			
			for(int i = 0; i <  sMetPago.length; i ++){
	    		Metpago formapago = com.casapellas.controles.MetodosPagoCtrl.metodoPagoCodigo( sMetPago[i] );
	    		lstMetodosPago.add(new SelectItem( formapago.getId().getCodigo(), formapago.getId().getMpago() ) );
	    		lstMetPago.add( new Metpago[]{formapago} );
	    	}
			
			CodeUtil.putInSessionMap("pr_metpago", lstMetPago);
			CodeUtil.putInSessionMap("pr_lstMetodosPago", lstMetodosPago);
    		
    	}else{
    		lstMetodosPago = (List<SelectItem>)CodeUtil.getFromSessionMap( "pr_lstMetodosPago" );
    	}
    	
    }catch(Exception ex){
		LogCajaService.CreateLog("getLstMetodosPago", "ERR", ex.getMessage());
	}  
	return lstMetodosPago;
}

/******************ESTABLECE EL TIPO DE RECIBO**********************************************/
public void setTipoRecibo(ValueChangeEvent e) {	
	String tipo; 
	Vf55ca01 vf01 = null;
	try {
		//obtener datos de Caja
		vf01 = (Vf55ca01) ((List) m.get("lstCajas")).get(0);
		
		tipo = cmbTiporecibo.getValue().toString();	
		m.put("pr_mTipoRecibo", tipo);
		m.remove("pr_tjdeRmanual");		//---- Bandera de paso de opción de fijar Tasa según fecha seleccionada
		
		//Tipo Manual
		if(tipo.equals("MANUAL")) {
			Object[] objConfigComp = (Object[])m.get("pr_OBJCONFIGCOMP");
			boolean bUsarTasa = (String.valueOf(objConfigComp[3]).equals("1"))?true:false;
			
			lnkAjustarTasaJdeAfecha.setStyle((bUsarTasa)?"display:inline":"display:none");
			txtNumRec.setValue("");
			txtNumRec.setStyleClass("frmInput2");
			txtNumRec.setStyle("display:inline; visibility: visible");
			txtFecham.setValue(new Date());
			txtFecham.setStyleClass("dateChooserSyleClass");
			txtFecham.setStyle("display:inline; visibility: visible");
			fechaRecibo = "";
			lblNumeroRecibo = "";
			lblNumRecm.setValue("No. Recibo Manual: ");
		} else {
			lblNumRecm.setValue("");
			lblNumrec = "Último Recibo: ";
			lblNumeroRecibo = m.get("pr_ReciboActual").toString();
			lblNumeroRecibo2.setValue(lblNumeroRecibo);
			txtNumRec.setStyle("visibility:hidden;display:none");
			txtFecham.setStyle("display:none; visibility:hidden");
			lnkAjustarTasaJdeAfecha.setStyle("display:none");
			leerTasaJDExFecha(new Date());
		}
		
		selectedMet = new ArrayList();
		m.remove("pr_lstSolicitud");
	    m.put("pr_selectedMet", selectedMet);
		metodosGrid.dataBind();
		
		restablecerEstilos( );
		resetResumenPago(null);
		cambiarVistaMetodos("MP",vf01);
		
	} catch (Exception error) {
		error.printStackTrace();
	}
}
/*****************************************************************************************************/
/*************ESTABLECE LAS REFERENCIA REQUERIDAS PARA EL METODO DE PAGO SELECCIONADO***********************************/
public void setMetodosPago(ValueChangeEvent e) {
	Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	MonedaCtrl monCtrl = new MonedaCtrl();
	String[] sMoneda = null;
	String codmetodo,codcaja;
	 Vf55ca01 vf01 = null;
	try {
		//obtener datos de Caja
		vf01 = (Vf55ca01) ((List) m.get("lstCajas")).get(0);
		
		codmetodo = cmbMetodosPago.getValue().toString();
		m.put("pr_cMpagos", codmetodo);
		codcaja = m.get("sCajaId").toString();		
		restablecerEstilosPago();
		lstMoneda = new ArrayList();
		
		if(!codmetodo.equals("MP")){
			//obtener monedas para el metodo de pago selecccionado
			sMoneda = monCtrl.obtenerMonedasxMetodosPago_Caja(Integer.parseInt(codcaja),cmbCompanias.getValue().toString(),cmbMetodosPago.getValue().toString());
			for (int i = 0; i < sMoneda.length; i++){
				lstMoneda.add(new SelectItem(sMoneda[i],sMoneda[i]));
			}
			m.put("pr_lstMoneda", lstMoneda);
		}
		cambiarVistaMetodos(codmetodo,vf01);
	}catch(Exception ex){
		ex.printStackTrace();
	}
	
}
/*************************************************************************************************/
/*******************CAMBIAR VISTAS DE MEOTODOS DE PAGO********************************************/
public void cambiarVistaMetodos(String sCodMetodo, Vf55ca01 vf01){
	try {
		txtReferencia1.setValue("");
		txtReferencia2.setValue("");
		txtReferencia3.setValue("");
		ddlBanco.dataBind();
		lbletVouchermanual.setValue("Manual");
		lbletVouchermanual.setStyle("visibility:hidden; display:none");
		chkVoucherManual.setChecked(false);
		chkVoucherManual.setStyle("width:20px; visibility:hidden; display:none");
		ddlTipoMarcasTarjetas.setStyle("display:none");
		lblMarcaTarjeta.setStyle("display:none;");
		
		
		//mostrar los campos para digitar el monto y seleccionar moneda.
		if(sCodMetodo.equals("MP")){
			lblMonto.setValue("");
			txtMonto.setValue("");
			txtConcepto.setValue("");
			lblAfiliado.setValue("");
			lblReferencia1.setValue("");
			lblReferencia2.setValue("");
			lblReferencia3.setValue("");
			lblBanco.setValue("");
			lblMontoRecibido2.setValue("0.00");
			cmbMetodosPago.dataBind();
			
			chkIngresoManual.setStyle("display:none");
			//Set to visible
			ddlAfiliado.setStyle("visibility:hidden; display:none; width: 120px");
			ddlBanco.setStyle("visibility:hidden; display:none");
			txtReferencia1.setStyle("visibility:hidden");
			txtReferencia2.setStyle("visibility:hidden");
			txtReferencia3.setStyle("visibility:hidden");
			cmbMoneda.setStyle("visibility:hidden;width: 70px");
			txtMonto.setStyle("visibility:hidden; width: 76px");
			lblMonto.setStyle("visibility:hidden");
			
			//quitar manuales
			lblNoTarjeta.setValue("");
			lblNoTarjeta.setStyle("display:none");
			txtNoTarjeta.setStyle("display:none");
			lblFechaVenceT.setValue("");
			lblFechaVenceT.setStyle("display:none");
			txtFechaVenceT.setStyle("display:none");
			lblTrack.setValue("");
			track.setStyle("display:none");
			
		}else{
			txtMonto.setValue("");		
			lblMonto.setValue("Monto:");
			cmbMoneda.setStyle("display:inline; width: 70px"); 
			txtMonto.setStyle("display:inline; width: 76px");
			lblMonto.setStyle("display:inline");
		}
		
		//	metodo = TC
		if(sCodMetodo.equals(MetodosPagoCtrl.TARJETA)) {	
			//Set to blank
			lblAfiliado.setValue("Afiliado:");
			lblReferencia1.setValue("Identificación:");
			lblReferencia2.setValue("Voucher:");
			lblReferencia3.setValue("");
			lblBanco.setValue("");
			chkVoucherManual.setChecked(false);
			
			//Set to visible
			ddlAfiliado.setStyle("display:inline;width: 120px");
			ddlBanco.setStyle("display:none");
			txtReferencia1.setStyle("display:inline");
			txtReferencia2.setStyle("display:inline");
			txtReferencia3.setStyle("display:none");
			lbletVouchermanual.setValue("Manual");
			lbletVouchermanual.setStyle("visibility:visible; display:inline");
			chkVoucherManual.setStyle("width:20px; visibility:visible; display:inline");
		 
			ddlTipoMarcasTarjetas.dataBind();
			ddlTipoMarcasTarjetas.setStyle("width: 120px; display:inline");
			lblMarcaTarjeta.setStyle("display:inline;");
			
			
			if( vf01.getId().getCasktpos() == 'Y' ){
				lblTrack.setValue("Banda:");
				track.setStyle("display:inline");
				chkIngresoManual.setStyle("display:inline");
				
				if(chkIngresoManual.isChecked()){
					lblNoTarjeta.setValue("Tarjeta:");
					lblNoTarjeta.setStyle("display:inline");
					txtNoTarjeta.setStyle("display:inline");
					lblFechaVenceT.setValue("Vence:");
					lblFechaVenceT.setStyle("display:inline");
					txtFechaVenceT.setStyle("display:inline");
					lblReferencia2.setValue("");
					txtReferencia2.setStyle("display:none");
					
					lblTrack.setValue("");
					track.setStyle("display:none");
				}else{
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
			ddlBanco.setStyle("display:inline; width: 120px;");
			txtReferencia1.setStyle("display:inline;  width: 150px;");
			txtReferencia2.setStyle("display:inline;  width: 150px;");
			txtReferencia3.setStyle("display:inline;  width: 150px;");
			
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
			ddlBanco.setStyle("display:none;width: 120px");
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
		else 
		//Método: transferencia en banco.
		if(sCodMetodo.equals(MetodosPagoCtrl.TRANSFERENCIA)) {	
			//Set to blank
			lblAfiliado.setValue("");
			lblReferencia1.setValue("Identificación:");
			lblReferencia2.setValue("Referencia");
			lblReferencia3.setValue("");
			lblBanco.setValue("Banco:");
			
			//Set to not visivble
			ddlAfiliado.setStyle("display:none");
			ddlBanco.setStyle("display:inline;width: 120px");
			
			txtReferencia1.setStyle("display:inline;  width: 150px;");
			txtReferencia2.setStyle("display:inline;  width: 150px;");
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
		//Método: Depósito en banco	
		else if (sCodMetodo.equals(MetodosPagoCtrl.DEPOSITO)){
			//Set to blank
			lblAfiliado.setValue("");
			lblReferencia1.setValue("");
			lblReferencia2.setValue("Referencia:");
			lblReferencia3.setValue("");
			lblBanco.setValue("Banco:");
			
			//Set to visible
			ddlAfiliado.setStyle("display:none");
			ddlBanco.setStyle("display:inline;width: 120px");
			
			txtReferencia1.setStyle("display:none");
			txtReferencia2.setStyle("display:inline;  width: 150px;");
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
				lblMonto, cmbMetodosPago, txtMonto, lblAfiliado, lblBanco, lblReferencia1, lblReferencia2, lblReferencia3, 
				ddlAfiliado, ddlBanco, txtReferencia1, txtReferencia2, txtReferencia3, chkVoucherManual, lbletVouchermanual,
				ddlTipoMarcasTarjetas, lblMarcaTarjeta
		};
		
		CodeUtil.refreshIgObjects(igObjects);
		
	} catch (Exception error) {
		error.printStackTrace(); 
	}	
	
}
/****************************************************************************************/
/************************LIMPIAR PANTALLA Y REESTABLECER LOS VALORES DE ENTRADA*******************************************/
public void limpiarPantalla(ActionEvent ev){
	try {
		restablecerEstilos( );
		cleanWindow();
	} catch (Exception error) {
		error.printStackTrace();
	}
}
/***********************************************************************************************************************/
/*************************LIMPIAR PANTALLA Y REESTABLECER LOS VALORES DE ENTRADA*************************************/
public void cleanWindow(){
	SmartRefreshManager srm;
	 Vf55ca01 vf01 = null;
	try {
		//obtener datos de Caja
		vf01 = (Vf55ca01) ((List) m.get("lstCajas")).get(0);
		
		srm = SmartRefreshManager.getCurrentInstance();		
		m.remove("pr_lstSolicitud");
		
		//limpiar la lista de sucursal unidad de negocio y metodos de pago.
		m.remove("pr_lstFiltrosucursal");
		m.remove("pr_lstFiltrounineg");
		m.remove("pr_metodospago");
		
		cmbCompanias.dataBind();
		ddlFiltrosucursal.dataBind();
		ddlFiltrounineg.dataBind();
		cmbMetodosPago.dataBind();		
		cmbMoneda.dataBind();
		txtConcepto.setValue("");	
		txtFecham.setValue(new Date());
		lblNombreSearch.setValue("");
		lblCodigoSearch.setValue("");
	
		
		//----- Obtener la tasa del día.
		leerTasaJDExFecha(new Date());
		if(cmbTiporecibo.getValue().toString().equals("MANUAL")){
			fechaRecibo = "";	
		}
		
		//----- reestablecer datos del bien
		cmbTipoProducto.dataBind();
		cmbMarcas.dataBind();
		cmbModelos.dataBind();
		txtNoItem.setValue("");
		txtNumRec.setValue("");
				
		//establecer el número de recibo al actual.
		
 		List lstCajas = (List)m.get("lstCajas");
		ReciboCtrl recCtrl = new ReciboCtrl();
		String sCodcomp = cmbCompanias.getValue()!=null? cmbCompanias.getValue().toString(): "10";
		int iCaid = ((Vf55ca01)lstCajas.get(0)).getId().getCaid();
		
		lblNumeroRecibo = recCtrl.obtenerUltimoRecibo(null, null, iCaid, sCodcomp) + "";		
		lblNumeroRecibo2.setValue(lblNumeroRecibo);
		m.put("pr_ReciboActual", lblNumeroRecibo);
		
		//-------- Obtener los métodos de pago por compañía.
		String[] sMetPago = null;
		Metpago[] metPago = null;
		List<Metpago[]> lstMetPago = null;
		List<SelectItem> lstSelectItem = null;
		MetodosPagoCtrl metCtrl = new MetodosPagoCtrl();
		
		
		lstMetodosPago = new ArrayList<SelectItem>();
		sMetPago = metCtrl.obtenerMetodosPagoxCaja_Compania(iCaid, sCodcomp);
		
		lstMetPago = new ArrayList<Metpago[]>();
		for(int i = 0; i <  sMetPago.length; i ++){
    		Metpago formapago = com.casapellas.controles.MetodosPagoCtrl.metodoPagoCodigo( sMetPago[i] );
    		lstMetodosPago.add(new SelectItem( formapago.getId().getCodigo(), formapago.getId().getMpago() ) );
    		lstMetPago.add( new Metpago[]{formapago} );
    	}
    	m.put("pr_metpago", lstMetPago);
    	m.put("pr_lstMetodosPago", lstMetodosPago);
		cmbMetodosPago.dataBind();
		
		/*
		//&& ======================================= 
		lstMetodosPago = new ArrayList(0);
		sMetPago = metCtrl.obtenerMetodosPagoxCaja_Compania(iCaid, cmbCompanias.getValue().toString());
		metPago = new Metpago[sMetPago.length];
		lstMetPago = new ArrayList<Metpago[]>();
		lstSelectItem = new ArrayList<SelectItem>();
		lstSelectItem.add(new SelectItem("MP","Método de Pago","Seleccione el método de pago a utilizar"));
    	for(int i = 0; i <  sMetPago.length; i ++){
    		metPago = metCtrl.obtenerDescripcionMetodosPago(sMetPago[i]);
    		if(metPago!=null){
    			lstSelectItem.add(new SelectItem(metPago[0].getId().getCodigo(),metPago[0].getId().getMpago()));
    			lstMetPago.add(metPago);
    		}
    	}
    	lstMetodosPago = lstSelectItem;
    	m.put("pr_metpago", lstMetPago);
    	m.put("pr_lstMetodosPago", lstMetodosPago);
		cmbMetodosPago.dataBind();
		*/
		
		srm.addSmartRefreshId(cmbMetodosPago.getClientId(FacesContext.getCurrentInstance()));
		srm.addSmartRefreshId(cmbMoneda.getClientId(FacesContext.getCurrentInstance()));
		
		cambiarVistaMetodos("MP",vf01);
		resetResumenPago(null);
		srm.addSmartRefreshId(cmbCompanias.getClientId(FacesContext.getCurrentInstance()));
		srm.addSmartRefreshId(ddlFiltrosucursal.getClientId(FacesContext.getCurrentInstance()));
		srm.addSmartRefreshId(ddlFiltrounineg.getClientId(FacesContext.getCurrentInstance()));
		srm.addSmartRefreshId(cmbMetodosPago.getClientId(FacesContext.getCurrentInstance()));
		srm.addSmartRefreshId(lblMontoRecibido2.getClientId(FacesContext.getCurrentInstance()));
		srm.addSmartRefreshId(lblNumeroRecibo2.getClientId(FacesContext.getCurrentInstance()));

		//reestablecer el grid
		selectedMet = new ArrayList(1);
	    m.put("pr_selectedMet", selectedMet);
		metodosGrid.dataBind();
		
	} catch (Exception error) {
		error.printStackTrace(); 
	}
}
/***************GET LISTA DE MONEDAS*****************************************************/
public List getLstMoneda() {	
	String[] sCodMod = null;
	MonedaCtrl monCtrl = new MonedaCtrl();
 
	try {
    	
		if(m.get("pr_moneda") == null){
    		
			if( cmbMetodosPago.getValue().toString().compareTo("MP") ==0 )
				return lstMoneda = new ArrayList<SelectItem>();
			
			
			lstMoneda = new ArrayList();
    		 
    		List lstCajas = (List)m.get("lstCajas");
    		Vf55ca01 f55ca01 = ((Vf55ca01)lstCajas.get(0));
    		 
	    	sCodMod = monCtrl.obtenerMonedasxMetodosPago_Caja( f55ca01.getId().getCaid(), cmbCompanias.getValue().toString(), cmbMetodosPago.getValue().toString());
	    	for(int i = 0; i < sCodMod.length; i++){
	    		lstMoneda.add(new SelectItem(sCodMod[i],sCodMod[i]));
	    	}
	    	
	    	m.put("pr_moneda", "m");
	    	m.put("pr_lstMoneda", lstMoneda);
	    	
    	}else{
    		lstMoneda = (List)m.get("pr_lstMoneda");
    	}
    }catch(Exception ex){
		LogCajaService.CreateLog("getLstMoneda", "ERR", ex.getMessage());
	}      
	return lstMoneda;
}
/***************************************************************************************/
/*******************ESTABLECER MONEDAS**********************************************************/

public void setMoneda(ValueChangeEvent e) {
	Map<String, Object> m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	String sCodigo = null, sCajaId = null; 
	String[] sMetodosPago = null;
	MetodosPagoCtrl metPagoCtrl = new MetodosPagoCtrl();
	Metpago[] metpago = null;
	Tpararela[] tpcambio = null;
	
	try{
		//verificar si tasa de cambio paralela existe
		tpcambio = (Tpararela[])m.get("pr_tpcambio");
		if(tpcambio == null){
			lblMensajeValidacion.setValue("Tasa de cambio paralela no esta configurada");
			dwProcesa.setWindowState("normal");
			dwProcesa.setStyle("width:370px;height:160px");
			cmbMoneda.dataBind();
		}else{
			
			for (int j = 0; j < tpcambio.length; j++){
				lblTasaCambio =" " + tpcambio[j].getId().getCmono()+ ": " + tpcambio[j].getId().getTcambiom();
			}
			
			m.put("pr_lblTasaCambio", lblTasaCambio);
			lstMetodosPago = new ArrayList();
			sCodigo = cmbMoneda.getValue().toString();
			sCajaId = (String)m.get("sCajaId");
		    
			
			sMetodosPago = metPagoCtrl.obtenerMetodoPagoxCaja_Moneda(Integer.parseInt(sCajaId), cmbCompanias.getValue().toString(), sCodigo);
			for (String codigoMpago : sMetodosPago) {
				Metpago formapago = com.casapellas.controles.MetodosPagoCtrl.metodoPagoCodigo( codigoMpago );
				lstMetodosPago.add(new SelectItem( formapago.getId().getCodigo(), formapago.getId().getMpago() ) );
			}
			m.put("pr_lstMetodosPago", lstMetodosPago);
			
			/*
			//obtener metodos de pago por moneda
			sMetodosPago = metPagoCtrl.obtenerMetodoPagoxCaja_Moneda(Integer.parseInt(sCajaId), cmbCompanias.getValue().toString(), sCodigo);
			metpago = new Metpago[sMetodosPago.length];
			
			lstMetodosPago.add(new SelectItem("MP","Método de Pago","Seleccione el método de pago a utilizar"));
	    	for(int i = 0; i <  sMetodosPago.length; i ++){
	    		metpago = metPagoCtrl.obtenerDescripcionMetodosPago(sMetodosPago[i]);
	    		lstMetodosPago.add(new SelectItem(metpago[0].getId().getCodigo(),metpago[0].getId().getMpago()));
	    	}
	    	m.put("pr_lstMetodosPago", lstMetodosPago);
	    	*/
			
	    	//----- cargar afiliados
	    	m.remove("pr_lstAfiliado");
	    	lstAfiliado = getLstAfiliado();
			
		}			
	}catch(Exception ex){
		ex.printStackTrace(); 
	}		
}
/********************************************************************************************************/
/*******************OBTENER AFILIADOS PARA LA CAJA**********************************************************/
public List getLstAfiliado() {
	AfiliadoCtrl afCtrl = new AfiliadoCtrl();
	Cafiliados[] cafiliado = null;	   
	lstAfiliado = new ArrayList<SelectItem>();
	
	try {

		if (m.containsKey("pr_lstAfiliado") )
			return (List<SelectItem>)m.get("pr_lstAfiliado");
		if(cmbMoneda.getValue() == null)
			return lstAfiliado;
				
		Vf55ca01 caja = ((List<Vf55ca01>) m.get("lstCajas")).get(0);
		String codcomp = cmbCompanias.getValue().toString();
		String sMoneda = cmbMoneda.getValue().toString();

		lstAfiliado = caja.getId().getCasktpos()  == 'N'?
						getLstAfiliados(caja.getId().getCaid(),
								codcomp,"",sMoneda):
						PosCtrl.getAfiliadosSp(caja.getId().getCaid(), 
								codcomp, "", sMoneda) ;
			
		if(lstAfiliado == null)				
			lstAfiliado = new ArrayList<SelectItem>();
						
		m.put("pr_lstAfiliado",lstAfiliado);
		ddlAfiliado.dataBind();
    	
	} catch (Exception ex) {
		lstAfiliado  = new ArrayList<SelectItem>();
		LogCajaService.CreateLog("getLstAfiliado", "ERR", ex.getMessage());
	}  	
	return lstAfiliado;
}	
/*******************OBTENER LAS LISTAS DE BANCOS********************************************************************/
public List getLstBanco() {
	BancoCtrl bancoCtrl = new BancoCtrl();
	F55ca022[] banco = null;
    try {  
    	if(m.get("pr_lstBanco") == null){
        	lstBanco = new ArrayList();
        	banco = bancoCtrl.obtenerBancos();
        	
        	if (banco != null && banco.length > 0 ) {
        		for (F55ca022 f22 : banco) {
    				SelectItem si = new SelectItem();
    				si.setValue(f22.getId().getConciliar()+"@"+f22.getId().getBanco());
    				si.setLabel(f22.getId().getBanco());
    				si.setDescription(String.valueOf(f22.getId().getCodb()));
    				lstBanco.add(si);
    			}
			}
        	m.put("pr_lstBanco", lstBanco);
        	m.put("pr_banco", banco);
    	}else{
    		lstBanco = (List)m.get("pr_lstBanco");
    	}
    }catch(Exception ex){
    	LogCajaService.CreateLog("getLstBanco", "ERR", ex.getMessage());	
	}
	return lstBanco;
}

/*******************************************************************************************************/	
/*************  CAMBIAR VALOR DEL MARCAS DEPENDIENDO DEL TIPO DE PRODUCTO SELECCIONADO *****************/
public void establecerMarcas(ValueChangeEvent ev){
	ProductoCrtl prodCrtl = new ProductoCrtl();
	List listMarcas= new ArrayList();
	String sTproducto="";
	
	try{
		lstMarcas = new ArrayList();
		lstMarcas.add(new SelectItem("S/M","S/M"));
		sTproducto = cmbTipoProducto.getValue().toString();
		
		if(!sTproducto.equals("S/T")){
			String[] sCodigos =  sTproducto.split(" ");
			Vmarca[] vmarca = prodCrtl.obtenerMarcasxTipoProducto(sCodigos);			
			
			if(vmarca!= null){
				for (int i = 0; i < vmarca.length; i++)
					lstMarcas.add(new SelectItem(vmarca[i].getId().getDrky(),vmarca[i].getId().getDrdl01()));
			}
			m.put("pr_vmarca", vmarca);
		}		
		m.put("pr_lstMarcas",lstMarcas);
		cmbTipoProducto.setStyleClass("frmInput2ddl");
		
		//actualiar el combo de modelos
		lstModelos = new ArrayList();
		lstModelos.add(new SelectItem("S/M","S/M"));
		m.put("pr_lstModelos",lstModelos);
		cmbModelos.dataBind();
		
	}catch(Exception ex){
		LogCajaService.CreateLog("establecerMarcas", "ERR", ex.getMessage());
	}
}
/*****************************************************************************************************************/	
/**************CAMBIAR VALOR DEL MARCAS DEPENDIENDO DEL TIPO DE PRODUCTO SELECCIONADO******************************/
public void establecerModelos(ValueChangeEvent ev){
	try{					
		lstModelos = new ArrayList();
		lstModelos.add(new SelectItem("S/M","S/M"));
		String sCodigo =  cmbMarcas.getValue().toString().trim();
		
		if(!sCodigo.equals("S/M")){
			ProductoCrtl prodCrtl = new ProductoCrtl();
			Vmodelo[] vmodelo = prodCrtl.obtenerModelosxMarca(sCodigo);
			m.put("pr_vmodelo", vmodelo);
			if (vmodelo != null){
				for (int i = 0; i < vmodelo.length;i++)
					lstModelos.add(new SelectItem(vmodelo[i].getId().getDrky(),vmodelo[i].getId().getDrdl01()));
			}
		}
		cmbMarcas.setStyleClass("frmInput2ddl");
		m.put("pr_lstModelos",lstModelos);		
		cmbModelos.dataBind();
	}catch(Exception ex){
		ex.printStackTrace();
	}
}
/*********************************************************************************************************/
/************************PROCESAR SOLICITUD**********************************************************************/
public void procesarSolicitud(ActionEvent e) {
	boolean validado = false,flgpagos = true, bTarjeta = false;	
	double montoRecibido = 0;
	double montototal,montoInser, equiv;
	BigDecimal tasa = BigDecimal.ONE;
	String sMonUnineg,sMonto, sEquiv, metododesc="",metodoId, moneda, ref1,ref2,ref3,ref4;
	Metpago[] metpago = null;
	Divisas d = new Divisas();
	CtrlSolicitud solCtrl = new CtrlSolicitud();
	String sVoucherManual="0";
	
	Vf55ca01 vf01 = null;
	List<String> lstDatosTrack  = null;List lstDatosTrack2 = null;
	
	boolean bUsaSocketPos  = false;
	boolean bVaucherManual = false;
	boolean bPagoSPManual  = false;
	
	
	try{
		//obtener datos de caja
		vf01 = (Vf55ca01) ((List) m.get("lstCajas")).get(0);
	
		sVoucherManual = (chkVoucherManual.isChecked())?"1":"0";
		bVaucherManual = chkVoucherManual.isChecked();
		bUsaSocketPos  = vf01.getId().getCasktpos() == 'Y';
		bPagoSPManual  = chkIngresoManual.isChecked();
		 
		validado = validarSolicitud();		
		if(validado){
			
			metodoId = cmbMetodosPago.getValue().toString();		
			moneda = cmbMoneda.getValue().toString();		
			ref1 = txtReferencia1.getValue().toString().trim();
			ref2 = txtReferencia2.getValue().toString().trim();
			ref3 = txtReferencia3.getValue().toString().trim();
			ref4 = "";
			String ref5 = "";
			String sTrack = "";
			String sTerminal = "";	
			
			selectedMet 	= (ArrayList<MetodosPago>)m.get("pr_selectedMet"); 
			List lstMetPago = (List)m.get("pr_metpago"); 
			
			montoInser = Double.parseDouble(m.get("pr_monto").toString());
			montototal = Double.parseDouble(m.get("pr_montoIsertando").toString());
			sMonto = d.formatDouble(montototal);
			equiv = montototal;
			
			//--------------- Cambiar ubicacion de referencias				
			if (metodoId.equals(MetodosPagoCtrl.CHEQUE)){//cheque
				ref4 = ref3;
				ref3 = ref2;
				ref2 = ref1;
				ref1 = ddlBanco.getValue().toString().split("@")[1];
			}else if(metodoId.equals(MetodosPagoCtrl.TARJETA)){//TC
				ref3 = ref2;
				ref2 = ref1;
				ref1 = ddlAfiliado.getValue().toString();	
				//si la caja usa socketPOS
				
				if( bUsaSocketPos && !bVaucherManual){
				
					ref1 = ((ddlAfiliado.getValue().toString()).split(","))[0];
					sTerminal = ((ddlAfiliado.getValue().toString()).split(","))[1];						
					
					if(bPagoSPManual){//manual
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
				else if( bUsaSocketPos && bVaucherManual){
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
			
			//-------------- Poner Descripción al método.
			for (int m = 0 ; m < lstMetPago.size();m++){
				metpago = (Metpago[])lstMetPago.get(m);
				if(metodoId.trim().equals(metpago[0].getId().getCodigo().trim())){
					metododesc = metpago[0].getId().getMpago().trim();
					break;
				}
			}
			//-------------- Equivalente del monto a la moneda de la Unidad de Negocio.
			sMonUnineg = ddlMonedaAplicada.getValue().toString();
			Tcambio[] tcJDE;
			Tpararela[] tcPar;
			
			if(sMonUnineg.equals("COR")){	//moneda doméstica.
				if(moneda.equals("COR")){
					equiv = montototal;
					tasa = BigDecimal.ONE;
				}else{
					//buscar tasa de cambio oficial para la moneda de pago.
					tcJDE = (Tcambio[])m.get("pr_TasaJdeTcambio");
					for(int l = 0; l < tcJDE.length;l++){
						if(tcJDE[l].getId().getCxcrdc().equals(moneda)){
							tasa = tcJDE[l].getId().getCxcrrd();
							break;
						}
					}
					equiv = d.roundDouble(montototal * tasa.doubleValue());
				}
			}else{
				if(moneda.equals("COR")){
					tcPar = (Tpararela[])m.get("pr_tpcambio");
					for(int t = 0; t < tcPar.length;t++){
						if((tcPar[t].getId().getCmono().equals(moneda) && tcPar[t].getId().getCmond().equals(sMonUnineg)) ||
						   (tcPar[t].getId().getCmono().equals(sMonUnineg) && tcPar[t].getId().getCmond().equals(moneda))){
							tasa = tcPar[t].getId().getTcambiom();
							break;
						}
					}
					equiv = d.roundDouble(montototal/tasa.doubleValue());
				}else{
					tasa = BigDecimal.ONE;
					equiv = montototal;
				}
			}
			
			//-------------- obtener los métodos de pago y verificar que no se repita el registro
			
			String codigomarcatarjeta = "";
			String marcatarjeta = "";
			
			if( bTarjeta ){
				codigomarcatarjeta = ddlTipoMarcasTarjetas.getValue().toString().split("@")[0];
				marcatarjeta = ddlTipoMarcasTarjetas.getValue().toString().split("@")[1];
			}
			
			if(selectedMet != null) {
				for(int i=0;i<selectedMet.size();i++){
					MetodosPago mp = ((MetodosPago)selectedMet.get(i));					
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
						
						
						mp.setMontorecibido( mp.getMontorecibido().add(new BigDecimal(Double.toString( montototal ) )) );
						mp.setMontoendonacion(  BigDecimal.ZERO );
						mp.setIncluyedonacion(false);
						
						if (bTarjeta && bUsaSocketPos && !bVaucherManual) {
							mp.setTrack(sTrack);
							mp.setTerminal(sTerminal);
							mp.setReferencia5(ref5);
							mp.setMontopos(montototal);
							mp.setVmanual("2");
						}
						
						mp.setReferencia6("");
						mp.setReferencia7("");
						mp.setNombre("");
						selectedMet.remove(i);
						selectedMet.add(i,mp);	
						flgpagos = false;
					}
				}				
			} else {
					MetodosPago metpagos = new MetodosPago(metododesc,
							metodoId, moneda, montototal, tasa, equiv, ref1,
							ref2, ref3, ref4, sVoucherManual, 0);
				
					metpagos.setReferencia5("");	
					
					metpagos.setMontorecibido( new BigDecimal(Double.toString( montototal ) ) ) ;
					metpagos.setMontoendonacion( BigDecimal.ZERO );
					metpagos.setIncluyedonacion(false);
					
					if (bTarjeta && bUsaSocketPos && !bVaucherManual) {
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
					
					selectedMet = new ArrayList();				
					selectedMet.add(metpagos);
					flgpagos = false;
			}	
			
			if (flgpagos){
				MetodosPago metpagos = new MetodosPago(metododesc,
						metodoId, moneda, montototal, tasa, equiv, ref1,
						ref2, ref3, ref4, sVoucherManual, 0);
				metpagos.setReferencia5("");
				
				metpagos.setMontorecibido( new BigDecimal(Double.toString( montototal ) ) ) ;
				metpagos.setMontoendonacion( BigDecimal.ZERO );
				metpagos.setIncluyedonacion(false);

				if (bTarjeta && bUsaSocketPos && !bVaucherManual) {
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

				selectedMet.add(metpagos);
			}
			//-------------- calcular el monto recibido
			montoRecibido = 0;
			for(int i = 0; i < selectedMet.size();i++)
				montoRecibido = montoRecibido +((MetodosPago) selectedMet.get(i)).getEquivalente();
			
			m.put("pr_montoRecibibo", montoRecibido);
			lblMontoRecibido2.setValue(d.formatDouble(montoRecibido));
			
			m.put("pr_selectedMet", selectedMet);
			
			//--------- Verificar que exista monto aplicado, si hay, calcular cambio o pendiente.
			if(m.get("pr_MontoAplicado")!=null){
				double dMontoapl;
				dMontoapl = Double.parseDouble(m.get("pr_MontoAplicado").toString());
				dMontoapl = d.roundDouble(dMontoapl);
				determinarCambio(selectedMet, montoRecibido, dMontoapl, ddlMonedaAplicada.getValue().toString());
			}else{
				txtMontoAplicar.setValue(d.formatDouble(montoRecibido));
			}
			
			metodosGrid.dataBind();
			
			SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
			srm.addSmartRefreshId(txtMontoAplicar.getClientId(FacesContext.getCurrentInstance()));
			
			//------------------- OPERACIONES DE LA SOLICITUD -------------------------------//
			//-------------- guardar solicitud en mapa
			List lstSolicitud = null;
			lstSolicitud = m.get("pr_lstSolicitud") == null? new ArrayList():(List)m.get("pr_lstSolicitud");
		
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
        	
        	solicitud.setId(solicitId);
			lstSolicitud.add(solicitud);
			
			m.put("pr_lstSolicitud", lstSolicitud);
			
			//-------------- Enviar correo a cajero o suplente, responsable de caja y autorizador
			//int iCodCajero = Integer.parseInt(m.get("iCodCajero").toString());
			EmpleadoCtrl empCtrl = new EmpleadoCtrl();
			Vautoriz[] vAut;
			Vf0101 vf0101 = null;			
			Vf55ca01 f55ca01 = null;
			List lstCajas = (List)m.get("lstCajas");
			f55ca01 = (Vf55ca01)lstCajas.get(0);
			int iCodCajero = 0;
			List lstAutorizadores = (List)m.get("pr_lstAutorizadores");
			String sFromCajero="",sToAutoriz="";
			Matcher matCorreo = null;
			Pattern pCorreo = Pattern.compile( "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$" );	
				 
			//-------------- obtener el correo del remitente para la solicitud
			vAut = (Vautoriz[])m.get("sevAut");
			iCodCajero = vAut[0].getId().getCodreg();
			vf0101 = empCtrl.buscarEmpleadoxCodigo2(iCodCajero);
			sFromCajero = vf0101==null? f55ca01.getId().getCacatimail(): vf0101.getId().getWwrem1();
			if(sFromCajero == null || !pCorreo.matcher(sFromCajero.trim().toUpperCase()).matches())
				sFromCajero = "webmaster@casapellas.com.ni";
			
			vf0101 = empCtrl.buscarEmpleadoxCodigo2(iCodCajero); //--- Datos del cajero
			List<String> lstCc = new ArrayList<String>();
			for (Iterator iter = lstCc.iterator(); iter.hasNext();)
				lstCc.add((String) iter.next());
			if(!sFromCajero.equals("webmaster@casapellas.com.ni"))
				lstCc.add(sFromCajero);
			
			String sSucursal = m.get("sNombreSucursal").toString();
			String sCajero	 = m.get("sNombreEmpleado").toString();
			String sSubject	 = f55ca01.getId().getCaname().trim() + ": Solicitud de Autorización de ingresos a caja";
			String sMontoAutorizado =  d.formatDouble(Double.parseDouble(m.get("pr_monto").toString()));
			String sMoneda 	   = cmbMoneda.getValue().toString();
			String sMetodoPago = cmbMetodosPago.getValue().toString();
			sToAutoriz = sAutorizador[0];
			
			String cliente = "No Seleccionado por cajero";
			if(  lblCodigoSearch.getValue() != null &&  lblNombreSearch.getValue() != null)
				cliente =  lblCodigoSearch.getValue().toString().trim()+" " +
						   lblNombreSearch.getValue().toString().trim();
			
			//-------------- validar el destino y mandar el correo.
			if(sToAutoriz != null && pCorreo.matcher(sFromCajero.trim().toUpperCase()).matches()){
					solCtrl.enviarCorreo(sToAutoriz, sFromCajero, f55ca01
							.getId().getCaname().trim(), lstCc, txtReferencia
							.getValue().toString(), txtObs.getValue()
							.toString(), sSubject, sCajero, sNomAut, sSucursal
							.trim(), f55ca01.getId().getCaname().trim(),
							sMontoAutorizado + " " + sMoneda, sMetodoPago,
							cliente);
			}
			//------------ Limpiar objetos del pago.
			txtMonto.setValue("");
			txtReferencia1.setValue("");
			txtReferencia2.setValue("");
			txtReferencia3.setValue("");
			txtCambioForaneo.setStyleClass("frmInput2");
			track.setValue("");
			txtCambioForaneo.setStyleClass("frmInput2");
			txtNoTarjeta.setValue("");	
			txtFechaVenceT.setValue("");	
			
			//------------ Limpiar objetos de ventana autorización.
			txtFecha.setValue("");
			txtReferencia.setValue("");
			txtObs.setValue("");
		}
		
  	}catch(Exception ex){
  		LogCajaService.CreateLog("procesarSolicitud", "ERR", ex.getMessage());
	}finally{
		if(validado)
		{
			dwSolicitud.setWindowState("hidden");
		}
		
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
		LogCajaService.CreateLog("validarSolicitud", "ERR", ex.getMessage());
		validado = false;
		
		lblMensajeAutorizacion.setValue("Datos incorrectos para procesar solicitud ");
		
	}
	return validado;
}


/*********************RESTABLECE EL ESTILO DE LOS CAMPOS DE LA AUTORIZACION******************************/
	public void restableceEstiloAutorizacion(){
		txtObs.setStyleClass("frmInput2");
		txtReferencia.setStyleClass("frmInput2");
		txtFecha.setStyleClass("");
		cmbAutoriza.setStyleClass("frmInput2");
		
	}
/**********************************************************************************************************/
//Inserta portador de TC
public void insertarPortador(ValueChangeEvent e) {		
	String emisor = getTxtReferencia2().getValue().toString();
	getTxtReferencia3().setValue(emisor);
}
	
/*******************CIERRA EL CUADRO DE DIALOGO Y CANCELA LA SOLICITUD***********************************************************************/
public void cancelarSolicitud(ActionEvent e) {	
	restableceEstiloAutorizacion();
	txtFecha.setValue("");
	txtReferencia.setValue("");
	txtObs.setValue("");
	dwSolicitud.setWindowState("hidden");		
}
/**********************************************************************************************************/
/***********************************GETTERS Y SETTERS DEL BACKING BEAN****************************/	
public HtmlInputText getTxtCodigoSearch() {
	return txtCodigoSearch;
}
public void setTxtCodigoSearch(HtmlInputText txtCodigoSearch) {
	this.txtCodigoSearch = txtCodigoSearch;
}
public HtmlInputText getTxtNombreSearch() {
	return txtNombreSearch;
}
public void setTxtNombreSearch(HtmlInputText txtNombreSearch) {
	this.txtNombreSearch = txtNombreSearch;
}
public UIInput getCmbBusquedaPrima() {
	return cmbBusquedaPrima;
}
public void setCmbBusquedaPrima(UIInput cmbBusquedaPrima) {
	this.cmbBusquedaPrima = cmbBusquedaPrima;
}	
	
public HtmlDropDownList getCmbMoneda() {
	return cmbMoneda;
}
public void setCmbMoneda(HtmlDropDownList cmbMoneda) {
	this.cmbMoneda = cmbMoneda;
}
public void setLstMoneda(List lstMoneda) {
	this.lstMoneda = lstMoneda;
}	
public void setLstMetodosPago(List lstMetodosPago) {
	this.lstMetodosPago = lstMetodosPago;
}
public HtmlDropDownList getCmbMetodosPago() {
	return cmbMetodosPago;
}
public void setCmbMetodosPago(HtmlDropDownList cmbMetodosPago) {
	this.cmbMetodosPago = cmbMetodosPago;
}
public HtmlGridView getMetodosGrid() {
	return metodosGrid;
}
public void setMetodosGrid(HtmlGridView metodosGrid) {
	this.metodosGrid = metodosGrid;
}
public void setSelectedMet(ArrayList<MetodosPago> selectedMet) {
	this.selectedMet = selectedMet;
}
public HtmlInputText getTxtReferencia2() {
	return txtReferencia2;
}
public void setTxtReferencia2(HtmlInputText txtReferencia2) {
	this.txtReferencia2 = txtReferencia2;
}
public HtmlInputText getTxtMonto() {
	return txtMonto;
}
public void setTxtMonto(HtmlInputText txtMonto) {
	this.txtMonto = txtMonto;
}	

public DialogWindow getDwAutoriza() {
	return dwAutoriza;
}
public void setDwAutoriza(DialogWindow dwAutoriza) {
	this.dwAutoriza = dwAutoriza;
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
public HtmlOutputText getLblMontoRecibido() {
	return lblMontoRecibido;
}
public void setLblMontoRecibido(HtmlOutputText lblMontoRecibido) {
	this.lblMontoRecibido = lblMontoRecibido;
}
public HtmlOutputText getLblMontoRecibido2() {
	return lblMontoRecibido2;
}
public void setLblMontoRecibido2(HtmlOutputText lblMontoRecibido2) {
	this.lblMontoRecibido2 = lblMontoRecibido2;
}
public void setLstAfiliado(List lstAfiliado) {
	this.lstAfiliado = lstAfiliado;
}
public HtmlInputText getTxtReferencia3() {
	return txtReferencia3;
}
public void setTxtReferencia3(HtmlInputText txtReferencia3) {
	this.txtReferencia3 = txtReferencia3;
}
public UIOutput getLblReferencia3() {
	return lblReferencia3;
}
public void setLblReferencia3(UIOutput lblReferencia3) {
	this.lblReferencia3 = lblReferencia3;
}
public void setLblTotal(String lblTotal) {
	this.lblTotal = lblTotal;
}
public String getLblTotal() {
	return lblTotal;
}
public HtmlInputText getTxtReferencia1() {
	return txtReferencia1;
}
public void setTxtReferencia1(HtmlInputText txtReferencia1) {
	this.txtReferencia1 = txtReferencia1;
}
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
public UIInput getTxtTasaCambio() {
	return txtTasaCambio;
}
public void setTxtTasaCambio(UIInput txtTasaCambio) {
	this.txtTasaCambio = txtTasaCambio;
}
public HtmlDropDownList getDdlBanco() {
	return ddlBanco;
}
public void setDdlBanco(HtmlDropDownList ddlBanco) {
	this.ddlBanco = ddlBanco;
}
public void setLstBanco(List lstBanco) {
	this.lstBanco = lstBanco;
}	
public UIOutput getLblBanco() {
	return lblBanco;
}
public void setLblBanco(UIOutput lblBanco) {
	this.lblBanco = lblBanco;
}
public HtmlOutputText getLblCodigoSearch() {
	return lblCodigoSearch;
}
public void setLblCodigoSearch(HtmlOutputText lblCodigoSearch) {
	this.lblCodigoSearch = lblCodigoSearch;
}
public HtmlOutputText getLblNombreSearch() {
	return lblNombreSearch;
}
public void setLblNombreSearch(HtmlOutputText lblNombreSearch) {
	this.lblNombreSearch = lblNombreSearch;
}
public List getLstBusquedaPrima() {
	if(lstBusquedaPrima == null){
		lstBusquedaPrima = new ArrayList();	
		lstBusquedaPrima.add(new SelectItem("1","Nombre Cliente","Búsqueda por nombre de cliente"));
		lstBusquedaPrima.add(new SelectItem("2","Código Cliente","Búsqueda por código de cliente"));
	}
	return lstBusquedaPrima;
}
public void setLstBusquedaPrima(List lstBusquedaPrima) {
	this.lstBusquedaPrima = lstBusquedaPrima;
}
public UIInput getTxtParametro() {
	return txtParametro;
}
public void setTxtParametro(UIInput txtParametro) {
	this.txtParametro = txtParametro;
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
public DialogWindow getDwSolicitud() {
	return dwSolicitud;
}
public void setDwSolicitud(DialogWindow dwSolicitud) {
	this.dwSolicitud = dwSolicitud;
}

public HtmlInputTextarea getTxtConcepto() {
	return txtConcepto;
}
public void setTxtConcepto(HtmlInputTextarea txtConcepto) {
	this.txtConcepto = txtConcepto;
}
public String getLblTasaCambio() {
	boolean bUsarTasa = true;
	try{
		if(m.get("pr_tasaparelela") == null) {	
			TasaCambioCtrl tcCtrl = new TasaCambioCtrl();
			Tpararela[] tpcambio = null;
			String sTasa="1.0";
			BigDecimal dValortp = BigDecimal.ONE;
			
			Object[]objConfigComp = (Object[])m.get("pr_OBJCONFIGCOMP");
			bUsarTasa = String.valueOf(objConfigComp[3]).equals("1")?true:false;
			
			if(bUsarTasa){
				tpcambio = tcCtrl.obtenerTasaCambioParalela(new Date());
				if(tpcambio == null){
					m.put("pr_Mensajetasap", "No se encuentra configurada la tasa de cambio paralela");
				}else{
					for(int i=0; i<tpcambio.length;i++){
						dValortp = tpcambio[i].getId().getTcambiom();
						sTasa = tpcambio[i].getId().getCmond()+" "+dValortp;
					}
				}
			}else{
				tpcambio = new Tpararela[1];
				sTasa = String.valueOf(objConfigComp[1]) +" "+dValortp;
				dValortp = BigDecimal.ONE;
				
				FechasUtil f = new FechasUtil();
				Tpararela tp = new Tpararela();
				TpararelaId tpId  = new TpararelaId();
				
				tpId.setCmond(String.valueOf(objConfigComp[1]));
				tpId.setCmono(String.valueOf(objConfigComp[1]));
				tpId.setDirec('C');
				tpId.setFechaf(f.obtenerFechaJulianaDia( new Date()));
				tpId.setFechai(f.obtenerFechaJulianaDia( new Date()));
				tpId.setTcambiod(BigDecimal.ONE);
				tpId.setTcambiom(BigDecimal.ONE);
				tp.setId(tpId);
				tpcambio[0] = tp;
			}
			m.put("pr_tpcambio", tpcambio);
			m.put("pr_tasaparelela", sTasa);
			m.put("pr_valortasap", dValortp);
			
			//objetos existentes de jc.
			m.put("pr_lblTasaCambio",lblTasaCambio);
			m.put("pr_tasa","t");
			lblTasaCambio = sTasa;
		}else{
			lblTasaCambio = m.get("pr_tasaparelela").toString();
		}
	}catch(Exception ex){
		LogCajaService.CreateLog("getLblTasaCambio", "ERR", ex.getMessage());
	}
	return lblTasaCambio;
}
public void setLblTasaCambio(String lblTasaCambio) {
	this.lblTasaCambio = lblTasaCambio;
}
public HtmlDropDownList getCmbTiporecibo() {
	return cmbTiporecibo;
}
public void setCmbTiporecibo(HtmlDropDownList cmbTiporecibo) {
	this.cmbTiporecibo = cmbTiporecibo;
}

public List getLstTiporecibo() {
	
	if(lstTiporecibo == null){
		lstTiporecibo = new ArrayList();	
		lstTiporecibo.add(new SelectItem("AUTOMATICO","AUTOMATICO"));
		lstTiporecibo.add(new SelectItem("MANUAL","MANUAL"));
		m.put("pr_rCambio", "no");
	}
	m.put("pr_mTipoRecibo", "AUTOMATICO");
	return lstTiporecibo;
}

public void setLstTiporecibo(List lstTiporecibo) {
	this.lstTiporecibo = lstTiporecibo;
}	

public HtmlDateChooser getTxtFecham() {
	return txtFecham;
}
public void setTxtFecham(HtmlDateChooser txtFecham) {
	this.txtFecham = txtFecham;
}
public HtmlInputText getTxtNumRec() {
	return txtNumRec;
}
public void setTxtNumRec(HtmlInputText txtNumRec) {
	this.txtNumRec = txtNumRec;
}
public UIOutput getLblNumrec2() {
	return lblNumrec2;
}
public void setLblNumrec2(UIOutput lblNumrec2) {
	this.lblNumrec2 = lblNumrec2;
}
public String getLblNumrec() {
	return lblNumrec;
}
public void setLblNumrec(String lblNumrec) {
	this.lblNumrec = lblNumrec;
}
public UIOutput getLblNumeroRecibo2() {
	return lblNumeroRecibo2;
}
public void setLblNumeroRecibo2(UIOutput lblNumeroRecibo2) {
	this.lblNumeroRecibo2 = lblNumeroRecibo2;
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

public String getLblNumeroRecibo() {
	try{
		if(m.get("pr_ReciboActual") == null) {
			
			List lstCajas = (List)m.get("lstCajas");
			ReciboCtrl recCtrl = new ReciboCtrl();
			lblNumeroRecibo = recCtrl.obtenerUltimoRecibo(null, null, 
					((Vf55ca01) lstCajas.get(0)).getId().getCaid(), cmbCompanias.getValue().toString()) + "";
			m.put("pr_ReciboActual", lblNumeroRecibo);
				
		} else {
			lblNumeroRecibo = m.get("pr_ReciboActual").toString();
		}
	}catch(Exception ex){
		ex.printStackTrace();
	}
	return lblNumeroRecibo;
}
public void setLblNumeroRecibo(String lblNumeroRecibo) {
	this.lblNumeroRecibo = lblNumeroRecibo;
}
public String getLblcliente() {
	return lblcliente;
}
public void setLblcliente(String lblcliente) {
	this.lblcliente = lblcliente;
}
public UIOutput getTxtCliente() {
	return txtCliente;
}
public void setTxtCliente(UIOutput txtCliente) {
	this.txtCliente = txtCliente;
}
public UIOutput getLblCod() {
	return lblCod;
}
public void setLblCod(UIOutput lblCod) {
	this.lblCod = lblCod;
}
public UIOutput getLblNom() {
	return lblNom;
}
public void setLblNom(UIOutput lblNom) {
	this.lblNom = lblNom;
}
public HtmlDropDownList getCmbTipoProducto() {
	return cmbTipoProducto;
}
public void setCmbTipoProducto(HtmlDropDownList cmbTipoProducto) {
	this.cmbTipoProducto = cmbTipoProducto;
}
public List getLstTipoProducto() {
	try {
		if(m.get("pr_lstTipoProducto") == null) {			
			//obtener Tipos de Productos
			lstTipoProducto = new ArrayList();
			ProductoCrtl prodCrtl = new ProductoCrtl();
			VtipoProd[] vtipoProd = null;
			vtipoProd = prodCrtl.obtenerTipoProducto();
			m.put("pr_vtipoProd", vtipoProd);
			lstTipoProducto.add(new SelectItem("S/T","S/T"));
			for(int i = 0; i < vtipoProd.length; i ++){
				lstTipoProducto.add(new SelectItem(vtipoProd[i].getId().getDrdl02().trim() + "@" + vtipoProd[i].getId().getDrky().trim(),vtipoProd[i].getId().getDrdl01()));
			}
			m.put("pr_lstTipoProducto",lstTipoProducto);
			//m.put("pr_tProd","p");
		} else {
			lstTipoProducto = (List)m.get("pr_lstTipoProducto");
		}
	}catch(Exception ex){
		ex.printStackTrace();
	}
	return lstTipoProducto;
}
public void setLstTipoProducto(List lstTipoProducto) {
	this.lstTipoProducto = lstTipoProducto;
}
public HtmlDropDownList getCmbMarcas() {
	return cmbMarcas;
}
public void setCmbMarcas(HtmlDropDownList cmbMarcas) {
	this.cmbMarcas = cmbMarcas;
}
public List getLstMarcas() {
	try {
		if(m.get("pr_lstMarcas") == null) {			
			//obtener Tipos de Productos
			lstMarcas = new ArrayList();
			lstMarcas.add(new SelectItem("S/M","S/M"));
			m.put("pr_lstMarcas",lstMarcas);
			m.put("pr_marca","m");
		} else {
			lstMarcas = (List)m.get("pr_lstMarcas");
		}
	}catch(Exception ex){
		ex.printStackTrace();
	}
	return lstMarcas;
}
public void setLstMarcas(List lstMarcas) {
	this.lstMarcas = lstMarcas;
}
public HtmlDropDownList getCmbModelos() {
	return cmbModelos;
}
public void setCmbModelos(HtmlDropDownList cmbModelos) {
	this.cmbModelos = cmbModelos;
}
public List getLstModelos() {
	try {
		if(m.get("pr_lstModelos") == null) {			
			//obtener Tipos de Productos
			lstModelos = new ArrayList();
			lstModelos.add(new SelectItem("S/M","S/M"));
			m.put("pr_lstModelos",lstModelos);
			m.put("pr_modelo","m");
		} else {
			lstModelos = (List)m.get("pr_lstModelos");
		}
	}catch(Exception ex){
		ex.printStackTrace();
	}
	return lstModelos;
}
public void setLstModelos(List lstModelos) {
	this.lstModelos = lstModelos;
}
public UIOutput getLblNumRecm() {
	return lblNumRecm;
}
public void setLblNumRecm(UIOutput lblNumRecm) {
	this.lblNumRecm = lblNumRecm;
}
public HtmlPanelSection getJspSec1() {
	return jspSec1;
}
public void setJspSec1(HtmlPanelSection jspSec1) {
	this.jspSec1 = jspSec1;
}
public UIOutput getLblMensajeValidacion() {
	return lblMensajeValidacion;
}
public void setLblMensajeValidacion(UIOutput lblMensajeValidacion) {
	this.lblMensajeValidacion = lblMensajeValidacion;
}
public HtmlInputText getTxtNoItem() {
	return txtNoItem;
}
public void setTxtNoItem(HtmlInputText txtNoItem) {
	this.txtNoItem = txtNoItem;
}
public HtmlDropDownList getCmbCompanias() {
	return cmbCompanias;
}
public void setCmbCompanias(HtmlDropDownList cmbCompanias) {
	this.cmbCompanias = cmbCompanias;
}
public List getLstCompanias() {
	try {
		if(m.get("pr_lstCompanias") == null) {
			lstCompanias = new ArrayList();
			List lstCajas = (List)m.get("lstCajas");
			CompaniaCtrl compCtrl = new CompaniaCtrl();
			F55ca014[] f55ca014 = null;
			List<SelectItem>lstComp = new ArrayList<SelectItem>();
			
			f55ca014 = compCtrl.obtenerCompaniasxCaja(((Vf55ca01)lstCajas.get(0)).getId().getCaid());
			if(f55ca014!=null && f55ca014.length>0){
				for (F55ca014 f14 : f55ca014) {
					SelectItem si = new SelectItem();
					si.setValue(f14.getId().getC4rp01().trim());
					si.setLabel(f14.getId().getC4rp01d1().trim());
					si.setDescription("Compañía: "+f14.getId().getC4rp01().trim()+" "+f14.getId().getC4rp01d1().trim());
					lstComp.add(si);
				}
				//--- Lista de compañías para la configuración de la caja.
				m.put("pr_F55CA014", f55ca014);	
				generarObjConfigComp(f55ca014[0].getId().getC4rp01().trim());
				
			}else{
				lstComp.add(new SelectItem("NC","Sin Compañía","No hay configuración de Compañía"));
			}
			lstCompanias = lstComp;
			m.put("pr_lstCompanias",lstCompanias);
		
		} else {
			lstCompanias = (List)m.get("pr_lstCompanias");
		}
 
	}catch(Exception ex){
		LogCajaService.CreateLog("getLstCompanias", "ERR", ex.getMessage());
	}
	return lstCompanias;
}
public void setLstCompanias(List lstCompanias) {
	this.lstCompanias = lstCompanias;
}
public List getSelectedMet() {
	if(m.get("pr_selectedMet")==null){	
		selectedMet = new ArrayList();
		m.put("pr_selectedMet", selectedMet);
	}else {
		selectedMet = (ArrayList<MetodosPago>)m.get("pr_selectedMet");
	}	
	return selectedMet;
}
public UIOutput getLblMensajeAutorizacion() {
	return lblMensajeAutorizacion;
}
public void setLblMensajeAutorizacion(UIOutput lblMensajeAutorizacion) {
	this.lblMensajeAutorizacion = lblMensajeAutorizacion;
}	

	public List getLstAutoriza() {

		try {

			if (CodeUtil.getFromSessionMap("pr_lstAutoriza") != null) {
				return lstAutoriza = (List)CodeUtil.getFromSessionMap( "pr_lstAutoriza" );
			}

			lstAutoriza = new ArrayList<SelectItem>();
			Vf55ca01 f = (Vf55ca01) ((List) CodeUtil
					.getFromSessionMap("lstCajas")).get(0);

			lstAutoriza.add(new SelectItem(f.getId().getCaan8mail(), f.getId()
					.getCaan8nom(), "Supervisor"));
			lstAutoriza.add(new SelectItem(f.getId().getCaautimail(), f.getId()
					.getCaautinom(), "Autorizador titular"));
			lstAutoriza.add(new SelectItem(f.getId().getCaausumail(), f.getId()
					.getCaausunom(), "Autorizador suplente"));
			lstAutoriza.add(new SelectItem(f.getId().getCacontmail(), f.getId()
					.getCacontnom(), "Contador"));

			CodeUtil.putInSessionMap("pr_lstAutoriza", lstAutoriza);
			CodeUtil.putInSessionMap("pr_lstAutorizadores", "");

		} catch (Exception ex) {
			ex.printStackTrace(); 
		}
		return lstAutoriza;
	}
public void setLstAutoriza(List lstAutoriza) {
	this.lstAutoriza = lstAutoriza;
}
public HtmlDropDownList getCmbAutoriza() {
	return cmbAutoriza;
}
public void setCmbAutoriza(HtmlDropDownList cmbAutoriza) {
	this.cmbAutoriza = cmbAutoriza;
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
}//----------------------------------------------- variables de modificación por Carlos Hernández.
public String getLblTasaCambioJde2() {
	Object ob[] = null;
	String sTasa = "1.0";
	Tcambio[] tcambio = null;
	boolean bUsarTasa = true;
	BigDecimal dTasaJde = BigDecimal.ONE;
	
	try {
		if(m.get("pr_lblTasaJde")==null){
			
			//----- Verificar la configuración de compañía.
			Object[]objConfigComp = (Object[])m.get("pr_OBJCONFIGCOMP");
			bUsarTasa = String.valueOf(objConfigComp[3]).equals("1")?true:false;
			
			if(bUsarTasa){
				ob = obtenerTasaCambioJDE(new Date());
				if(ob == null){
					m.put("pr_MensajetasaJde", "No se encuentra configurada la tasa de cambio jde");
				}else{
					sTasa   = ob[0].toString();
					tcambio = (Tcambio[])ob[2];
					dTasaJde = (BigDecimal)ob[1];
				}
			}else{
				sTasa = String.valueOf(objConfigComp[1]) + " 1.0";
				tcambio = new Tcambio[1];
				
				Tcambio tc = new Tcambio();
				TcambioId tcId = new TcambioId();
				tcId.setCxcrcd(String.valueOf(objConfigComp[1]));
				tcId.setCxcrdc(String.valueOf(objConfigComp[1]));
				tcId.setCxcrr(BigDecimal.ONE);
				tcId.setCxcrrd(BigDecimal.ONE);
				tcId.setCxeft(new Date());
				tc.setId(tcId);
				tcambio[0] = tc;
			}
			m.put("pr_lblTasaJde", sTasa);			//etiqueta de la tasa
			m.put("pr_valortasajde", dTasaJde);		//Monto de la tasa.
			m.put("pr_TasaJdeTcambio",tcambio );	//objeto de Tcambios
			m.put("pr_tjdeactobj", ob);
			lblTasaCambioJde2  = sTasa;
		}else
			lblTasaCambioJde2 = m.get("pr_lblTasaJde").toString();
	} catch (Exception error) {
		LogCajaService.CreateLog("getLblTasaCambioJde2", "ERR", error.getMessage());
	}
	return lblTasaCambioJde2;
}
public void setLblTasaCambioJde2(String lblTasaCambioJde2) {
	this.lblTasaCambioJde2 = lblTasaCambioJde2;
}
public HtmlDropDownList getDdlFiltrosucursal() {
	return ddlFiltrosucursal;
}
public void setDdlFiltrosucursal(HtmlDropDownList ddlFiltrosucursal) {
	this.ddlFiltrosucursal = ddlFiltrosucursal;
}
public List getLstFiltrosucursal() {
	try {
		if(m.get("pr_lstFiltrosucursal")==null){
			List lstSuc = new ArrayList();
			lstSuc.add(new SelectItem("SUC","Sucursal","Seleccione la Sucursal a utilizar en el pago"));
			
			String sCodcomp = cmbCompanias.getValue().toString().trim();
			List<String[]>  unegocio2 = null;
			com.casapellas.controles.SucursalCtrl sucCtrl = new com.casapellas.controles.SucursalCtrl();
			unegocio2 = sucCtrl.obtenerSucursalesxCompania( cmbCompanias.getValue().toString() );
			
			for (Object[] sucursal : unegocio2) {
			
				String unineg = String.valueOf(sucursal[0]).trim();
				
				if(unineg.length() < 5 ) {
					unineg = CodeUtil.pad(unineg, 5, "0");
				}			
				
				lstSuc.add(new SelectItem(String.valueOf(sucursal[0]),
						unineg +": "+ String.valueOf(sucursal[2]).trim(),
								String.valueOf(sucursal[2]).trim()));
				
			}
			
			
			lstFiltrosucursal = lstSuc;
			m.put("pr_lstFiltrosucursal", lstSuc);
		}else
			lstFiltrosucursal = (ArrayList)m.get("pr_lstFiltrosucursal");
		
	} catch (Exception error) {
		LogCajaService.CreateLog("getLstFiltrosucursal", "ERR", error.getMessage());
	}
	return lstFiltrosucursal;
}
public void setLstFiltrosucursal(List lstFiltrosucursal) {
	this.lstFiltrosucursal = lstFiltrosucursal;
}
public HtmlDropDownList getDdlFiltrounineg() {
	return ddlFiltrounineg;
}
public void setDdlFiltrounineg(HtmlDropDownList ddlFiltrounineg) {
	this.ddlFiltrounineg = ddlFiltrounineg;
}
public List getLstFiltrounineg() {
	try {
		if(m.get("pr_lstFiltrounineg")==null){
			List lstUnineg = new ArrayList();
			lstUnineg.add(new SelectItem("UN","Unidad de Negocio","Seleccione la Unidad de negocio a utilizar en el pago"));
			lstFiltrounineg = lstUnineg;
			m.put("pr_lstFiltrounineg", lstUnineg);
		}else
			lstFiltrounineg = (ArrayList)m.get("pr_lstFiltrounineg");
		
	} catch (Exception error) {
		error.printStackTrace();
	}
	return lstFiltrounineg;
}
public void setLstFiltrounineg(List lstFiltrounineg) {
	this.lstFiltrounineg = lstFiltrounineg;
}
public HtmlOutputText getLblMonedaunineg2() {
	return lblMonedaunineg2;
}
public void setLblMonedaunineg2(HtmlOutputText lblMonedaunineg2) {
	this.lblMonedaunineg2 = lblMonedaunineg2;
}
public HtmlOutputText getLblMonto() {
	return lblMonto;
}
public void setLblMonto(HtmlOutputText lblMonto) {
	this.lblMonto = lblMonto;
}
public HtmlOutputText getLblTcambioJde2() {
	return lblTcambioJde2;
}
public void setLblTcambioJde2(HtmlOutputText lblTcambioJde2) {
	this.lblTcambioJde2 = lblTcambioJde2;
}
/******************************************************************************/
/** variables usadas en monto y moneda aplicada, y cambios generados en el pago
 * Fecha: 12/06/2010
 * Hecho: Carlos Hernández Morrison.
 */
public List getLstMonedaAplicada() {
	try{	
		if(m.get("pr_lstMonedaAplicada") == null) {
			MonedaCtrl mC = new MonedaCtrl();
			Vf55ca01 f5 = (Vf55ca01)((List)m.get("lstCajas")).get(0);
			F55ca014[] f14_ConfigComp = (F55ca014[])m.get("pr_F55CA014");

			List<SelectItem> lstFMonedaApl = new ArrayList<SelectItem>();
			String sMonedaBase = "";
			String[] lstMonedaCC = null;
			String sCodcomp = cmbCompanias.getValue().toString().trim();
			
			//---- Leer la moneda base de la compañía.
			for (F55ca014 f14 : f14_ConfigComp) {
				if(f14.getId().getC4rp01().trim().equals(sCodcomp)){
					sMonedaBase = f14.getId().getC4bcrcd().trim();
					break;
				}
			}
			if(sMonedaBase.trim().equals("")){
				return new ArrayList(1);
			}
			//--- Construir la lista para monedas aplicadas.
			lstFMonedaApl.add(new SelectItem(sMonedaBase,sMonedaBase,sMonedaBase));
			lstMonedaCC = mC.obtenerMonedasxCaja_Companias(f5.getId().getCaid(), f14_ConfigComp);
			if(lstMonedaCC!=null && lstMonedaCC.length>0){
				for (String sMoneda : lstMonedaCC)
					if(!sMoneda.equals(sMonedaBase)){					
						lstFMonedaApl.add(new SelectItem(sMoneda,sMoneda,sMoneda));
					}
			}
			else
				lstFMonedaApl.add(new SelectItem("SM","Moneda","Sin Moneda Configurada para la compañía"));
			
			lstMonedaAplicada = lstFMonedaApl;
			m.put("pr_lstMonedaAplicada", lstMonedaAplicada);
		}else
			lstMonedaAplicada = (ArrayList)m.get("pr_lstMonedaAplicada");
	
	}catch(Exception error){
		lstMonedaAplicada = new ArrayList(1);
		lstMonedaAplicada.set(1,new SelectItem("SM","Moneda","Sin Moneda Configurada para la compañía"));
		error.printStackTrace();
	}
	return lstMonedaAplicada;
}
public void setLstMonedaAplicada(List lstMonedaAplicada) {
	this.lstMonedaAplicada = lstMonedaAplicada;
}
public HtmlDropDownList getDdlMonedaAplicada() {
	return ddlMonedaAplicada;
}
public void setDdlMonedaAplicada(HtmlDropDownList ddlMonedaAplicada) {
	this.ddlMonedaAplicada = ddlMonedaAplicada;
}
public HtmlLink getLnkAjustarTasaJdeAfecha() {
	return lnkAjustarTasaJdeAfecha;
}
public void setLnkAjustarTasaJdeAfecha(HtmlLink lnkAjustarTasaJdeAfecha) {
	this.lnkAjustarTasaJdeAfecha = lnkAjustarTasaJdeAfecha;
}
public HtmlOutputText getLblCambioapl() {
	return lblCambioapl;
}
public void setLblCambioapl(HtmlOutputText lblCambioapl) {
	this.lblCambioapl = lblCambioapl;
}
public HtmlOutputText getLblCambioDom() {
	return lblCambioDom;
}
public void setLblCambioDom(HtmlOutputText lblCambioDom) {
	this.lblCambioDom = lblCambioDom;
}
public HtmlOutputText getLbletCambioapl() {
	return lbletCambioapl;
}
public void setLbletCambioapl(HtmlOutputText lbletCambioapl) {
	this.lbletCambioapl = lbletCambioapl;
}
public String getLbletCambioapl1() {
	try {
		if(lbletCambioapl1 == null)
			lbletCambioapl1 = "Cambio " + ddlMonedaAplicada.getValue().toString();
	} catch (Exception error) {
		error.printStackTrace();
	}
	return lbletCambioapl1;
}
public void setLbletCambioapl1(String lbletCambioapl1) {
	this.lbletCambioapl1 = lbletCambioapl1;
}
public HtmlOutputText getLbletCambioDom() {
	return lbletCambioDom;
}
public void setLbletCambioDom(HtmlOutputText lbletCambioDom) {
	this.lbletCambioDom = lbletCambioDom;
}
public String getLbletCambioDom1() {
	return lbletCambioDom1;
}
public void setLbletCambioDom1(String lbletCambioDom1) {
	this.lbletCambioDom1 = lbletCambioDom1;
}
public String getLbletMontoRecibido() {
	try {
		if(lbletMontoRecibido == null)
			lbletMontoRecibido = "Recibido " + ddlMonedaAplicada.getValue().toString();
	} catch (Exception error) {
		error.printStackTrace();
	}
	return lbletMontoRecibido;
}
public void setLbletMontoRecibido(String lbletMontoRecibido) {
	this.lbletMontoRecibido = lbletMontoRecibido;
}
public HtmlOutputText getLblMontoAplicar() {
	return lblMontoAplicar;
}
public void setLblMontoAplicar(HtmlOutputText lblMontoAplicar) {
	this.lblMontoAplicar = lblMontoAplicar;
}
public String getLblMontoAplicar2() {
	try {
		lblMontoAplicar2 = (lblMontoAplicar2==null)?
							"Aplicado " + ddlMonedaAplicada.getValue().toString()+":":"Aplicado:";
	} catch (Exception error) {
		error.printStackTrace();
	}
	return lblMontoAplicar2;
}
public void setLblMontoAplicar2(String lblMontoAplicar2) {
	this.lblMontoAplicar2 = lblMontoAplicar2;
}
public HtmlLink getLnkCambio() {
	return lnkCambio;
}
public void setLnkCambio(HtmlLink lnkCambio) {
	this.lnkCambio = lnkCambio;
}
public HtmlInputText getTxtMontoAplicar() {
	return txtMontoAplicar;
}
public void setTxtMontoAplicar(HtmlInputText txtMontoAplicar) {
	this.txtMontoAplicar = txtMontoAplicar;
}
public HtmlInputText getTxtCambioForaneo() {
	return txtCambioForaneo;
}
public void setTxtCambioForaneo(HtmlInputText txtCambioForaneo) {
	this.txtCambioForaneo = txtCambioForaneo;
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

public HtmlCheckBox getChkContrato() {
	return chkContrato;
}
public void setChkContrato(HtmlCheckBox chkContrato) {
	this.chkContrato = chkContrato;
}

public HtmlLink getLnkSearchContrato() {
	return lnkSearchContrato;
}
public void setLnkSearchContrato(HtmlLink lnkSearchContrato) {
	this.lnkSearchContrato = lnkSearchContrato;
}

	private P55RECIBO getP55recibo() {
	
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
public HtmlDropDownList getDdlTipodoc() {
	return ddlTipodoc;
}
public void setDdlTipodoc(HtmlDropDownList ddlTipodoc) {
	this.ddlTipodoc = ddlTipodoc;
}
public List getLstTipodoc() {
	try {
		if(m.get("pr_lstTipodoc") == null) {			
			//obtener Tipos de Productos
			lstTipodoc = new ArrayList();
			lstTipodoc.add(new SelectItem("S/TD","S/TD"));
			m.put("pr_lstTipodoc",lstTipodoc);
		} else {
			lstTipodoc = (List)m.get("pr_lstTipodoc");
		}
	}catch(Exception ex){
		ex.printStackTrace();
	}
	return lstTipodoc;
}
public void setLstTipodoc(List lstTipodoc) {
	this.lstTipodoc = lstTipodoc;
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
		
		if(CodeUtil.getFromSessionMap("pr_lstDonacionesRecibidas") != null )
			return lstDonacionesRecibidas = (ArrayList<DncIngresoDonacion>)
					CodeUtil.getFromSessionMap("pr_lstDonacionesRecibidas");
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
			
			lstBeneficiarios = (ArrayList<SelectItem>)CodeUtil.getFromSessionMap("pr_lstbeneficiarios");
			if(lstBeneficiarios != null &&  !lstBeneficiarios.isEmpty() )
				return lstBeneficiarios;
			
			lstBeneficiarios = (ArrayList<SelectItem>)DonacionesCtrl.obtenerBeneficiariosConfigurados(true, false);
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}finally{
			
			if(lstBeneficiarios == null )
				lstBeneficiarios = new ArrayList<SelectItem>();

			 CodeUtil.putInSessionMap("pr_lstbeneficiarios", lstBeneficiarios);
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
			
			lstMarcasDeTarjetas = (ArrayList<SelectItem>)CodeUtil.getFromSessionMap("pr_lstMarcasDeTarjetas");
			if(lstMarcasDeTarjetas != null &&  !lstMarcasDeTarjetas.isEmpty() )
				return lstMarcasDeTarjetas;
			
			lstMarcasDeTarjetas = AfiliadoCtrl.obtenerTiposMarcaTarjetas();
			
		} catch (Exception e) {
			LogCajaService.CreateLog("getLstMarcasDeTarjetas", "ERR", e.getMessage());
		}finally{
			
			if(lstMarcasDeTarjetas == null )
				lstMarcasDeTarjetas = new ArrayList<SelectItem>();

			 CodeUtil.putInSessionMap("pr_lstMarcasDeTarjetas", lstMarcasDeTarjetas);
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

	public HtmlCheckBox getChkValidarProformaRepuestos() {
		return chkValidarProformaRepuestos;
	}
	public void setChkValidarProformaRepuestos(
			HtmlCheckBox chkValidarProformaRepuestos) {
		this.chkValidarProformaRepuestos = chkValidarProformaRepuestos;
	}
	public HtmlLink getLnkConsultarProformaRepuestos() {
		return lnkConsultarProformaRepuestos;
	}
	public void setLnkConsultarProformaRepuestos(
			HtmlLink lnkConsultarProformaRepuestos) {
		this.lnkConsultarProformaRepuestos = lnkConsultarProformaRepuestos;
	}
	
}

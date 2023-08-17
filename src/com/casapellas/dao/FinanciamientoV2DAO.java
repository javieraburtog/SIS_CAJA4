package com.casapellas.dao;
import java.math.*;
import java.sql.Connection;
import java.text.DecimalFormat;
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
import org.apache.commons.mail.MultiPartEmail;
import org.apache.xalan.transformer.CountersTable;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.casapellas.controles.AfiliadoCtrl;
import com.casapellas.controles.BancoCtrl;
import com.casapellas.controles.ClsConfiguracionMensaje;
import com.casapellas.controles.ClsF5503B11;
import com.casapellas.controles.ClsTipoDocumentoFinanciamiento;
import com.casapellas.controles.CompaniaCtrl;
import com.casapellas.controles.ConsolidadoDepositosBcoCtrl;
import com.casapellas.controles.CtrlCajas;
import com.casapellas.controles.CtrlPoliticas;
import com.casapellas.controles.CtrlSolicitud;
import com.casapellas.controles.CuotaCtrl;
import com.casapellas.controles.DonacionesCtrl;
import com.casapellas.controles.EmpleadoCtrl;
import com.casapellas.controles.FinanciamientoCtrl;
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
import com.casapellas.entidades.ConfiguracionMensaje;
import com.casapellas.entidades.Credhdr;
import com.casapellas.entidades.Elementofin;
import com.casapellas.entidades.F55ca014;
import com.casapellas.entidades.F55ca022;
import com.casapellas.entidades.Finandet;
import com.casapellas.entidades.Finanhdr;
import com.casapellas.entidades.MetodosPago;
import com.casapellas.entidades.Metpago;
import com.casapellas.entidades.Pago;
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
import com.casapellas.jde.creditos.BatchControlF0011;
import com.casapellas.jde.creditos.CodigosJDE1;
import com.casapellas.jde.creditos.ProcesarPagoFacturaJdeV2;
import com.casapellas.navegacion.As400Connection;
import com.casapellas.rpg.P55CA090;
import com.casapellas.rpg.P55RECIBO;
import com.casapellas.socketpos.PosCtrl;
import com.casapellas.util.BitacoraSecuenciaReciboService;
import com.casapellas.util.CatalogoGenerico;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.Divisas;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.PropertiesSystem;
import com.casapellas.util.bt.detalleCobroFactura;
import com.casapellas.util.bt.serviceRequest;
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
import ni.com.casapellas.dto.bytes.request.PagoFinanciamiento;
import ni.com.casapellas.dto.bytes.response.ConsultaCuenta;
import ni.com.casapellas.dto.bytes.response.ConsultaPrestamo;
import ni.com.casapellas.dto.bytes.response.CuentaInfo;
import ni.com.casapellas.dto.bytes.response.MovimientoCuenta;
import ni.com.casapellas.dto.bytes.response.Prestamo;
import ni.com.casapellas.dto.bytes.response.PrestamoCuota;
import ni.com.casapellas.tool.restful.connection.RestResponse;

public class FinanciamientoV2DAO {
	

	protected P55RECIBO p55recibo;
	protected P55CA090 p55ca090;
	private HtmlInputSecret track;	
	private UIOutput lblTrack;
	
	Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	
	private HtmlCheckBox chkIngresoManual;
	private HtmlOutputText lblNoTarjeta;
	private HtmlInputText txtNoTarjeta;
	private HtmlOutputText lblFechaVenceT;
	private HtmlInputText txtFechaVenceT;
	private String terminal ;
	//
	
	
	
	String sCredyCobMail = "creditoycobro@casapellas.com.ni";
	
	private HtmlDialogWindow dwCargando;
	/*Opciones de busqueda*/
	private List lstTiposBusqueda;
	private HtmlDropDownList hddTiposBusqueda;
	private HtmlInputText txtParametro;
	private String strMensajeBusqueda;
	/*Grid principal de cuotas pendientes*/
	private HtmlGridView gvCuotas;
	private List lstCuotasV2;
	private List consultaInfoPrestamo;
	private List prestamoCabecera;
	private List prestamoCabeceraDetalle;

	
	/*COnsumo de pago Byte*/
	private PagoFinanciamiento pagoFin;
	private String noPrestamo;
	
	/*Ventana de Detalle de Solicitud*/
	private HtmlDialogWindow dwDetalleSolicitud;
	private String fechaDet;
	private String fechaFin;
	private String codigoMoneda;
	private String codcliDet;
	private String nomcliDet;
	private String monedaDet;
	private BigDecimal tasaDet;
	private String noSolDet;
	private String tipoSolDet;
	private String lineaDet;
	private static final DecimalFormat df = new DecimalFormat("0.00");
	
	public Double getTxtOtrosPagosDet() {
		return txtOtrosPagosDet;
	}

	public void setTxtOtrosPagosDet(Double txtOtrosPagosDet) {
		this.txtOtrosPagosDet = txtOtrosPagosDet;
	}

	private Double txtPrincipalDet;
	private Double txtInteresDet;
	private Double txtImpuestoDet;
	private Double txtTotalDet;
	private Double txtTotalMoraDet;
	private Double txtPendienteDet;
	private Double txtOtrosPagosDet;
	
	private HtmlDropDownList hddMonedaDetalle;
	private List lstMonedaDetalle;
	
	private List lstProductos;
	private HtmlGridView gvProductos;
	
	private List lstDetallesol;
	private HtmlGridView gvDetallesol;
	
	private HtmlGridView gvIntereses;
	private List lstIntereses; 
	
	/*****ventana de recibo**********/
	
	//ventana de recibo
	private HtmlDialogWindow dwReciboV2;
	private List lstTipoReciboPago;
	
	public List getLstTipoReciboPago() {
		FinanciamientoCtrl financiamiento= new FinanciamientoCtrl(); 
		lstTipoReciboPago = new ArrayList();
		try {
			Object[] lista = financiamiento.obtenerListaTipoPago();
			
			for(int i=0; i<lista.length;i++) {
				Object[] x =   (Object[]) lista[i];			

				String va =x[0].toString();
				String concep= x[1].toString();
				String aa="Búsqueda por "+x[1].toString();
				lstTipoReciboPago.add(new SelectItem(va,concep,aa));

			}
			
			
		
		}catch(Exception e) {
			e.printStackTrace();
		}

		
		return lstTipoReciboPago;
	}

	public void setLstTipoReciboPago(List lstTipoReciboPago) {
		this.lstTipoReciboPago = lstTipoReciboPago;
	}

	private Date fechaRecibo;
	public HtmlDialogWindow getDwReciboV2() {
		return dwReciboV2;
	}

	public void setDwReciboV2(HtmlDialogWindow dwReciboV2) {
		this.dwReciboV2 = dwReciboV2;
	}

	private HtmlOutputText txtNumrec;
	private HtmlOutputText lblNumrecm;
	private HtmlInputText txtNumrecm;
	private HtmlOutputText txtCodcli;
	private HtmlOutputText txtNomcli;	
	private HtmlDropDownList ddlTipoRecibo;
	private HtmlDropDownList ddlTipoReciboPago;
	public HtmlDropDownList getDdlTipoReciboPago() {
		return ddlTipoReciboPago;
	}

	public void setDdlTipoReciboPago(HtmlDropDownList ddlTipoReciboPago) {
		this.ddlTipoReciboPago = ddlTipoReciboPago;
	}

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
	private List lstSelectedCuotasV2;
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
			
			recCtrl.actualizarEstadoRecibo(iNumrec,iCaid,sCodsuc,sCodcomp, vAut[0].getId().getCoduser(), "No se completo pago con SP","FN");
			lstRecibojde = recCtrl.getEnlaceReciboJDE(iCaid, sCodsuc,sCodcomp,iNumrec,"FN");
			lstReciboFac = cuotaCtrl.leerFacturasReciboFinan2(iCaid, sCodcomp, iNumrec, "FN", iCodcli);

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
					recibojde = rj;
				}
			} 
			//&& ======= Borrar los sobrantes.
			recibojde = null;
			lstRecibojde = recCtrl.getEnlaceReciboJDE(iCaid, sCodsuc, sCodcomp, iNumrec, "SBR");
	
			cn.commit();
			
		} catch (Exception e) {
			bBorrado = false;
			e.printStackTrace();
		}finally{
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

			if(validado){
				lstPagos = (List)m.get("lstPagosFinan");
				Credhdr c= (Credhdr)lstComponentes.get(0);
				//obtener companias x caja
				sMonedaBase = cCtrl.sacarMonedaBase((F55ca014[])m.get("cont_f55ca014"), c.getId().getCodcomp());
				
				dMontoAplicar = d.formatStringToDouble(txtMontoAplicar.getValue().toString());		
				m.put("finan_MontoAplicar", dMontoAplicar);
				
				
				//calcular el monto recibido en dependencia de moneda de factura y moneda de pago
				double montoRecibido = calcularElMontoRecibido(lstPagos, sMonedaBase);						
				//distribuir el monto recibido a los montos a aplicar en factura
				distribuirMontoAplicar(sMonedaBase);		
				//determinar cambio
				determinarCambio(lstPagos,montoRecibido,sMonedaBase);
				
				gvSelectedCuotas.dataBind();
			}
			//
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	
/*****************************************************************************************************************/	
	
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
	
		boolean committ = true;
		boolean nuevaConexion = true;
		
		try{
			
			ConsolidadoDepositosBcoCtrl.getCurrentSession();
			nuevaConexion = ConsolidadoDepositosBcoCtrl.isNewSession;
			
			
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
				
				
				
				//generarIFCuota(f, fh, sMonedaBase,f55ca01);
				
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
			
			dMonto = calcularElMontoRecibido(lstPagos, sMonedaBase);
			determinarCambio(lstPagos,montoRecibido,sMonedaBase);
			
			
			CodeUtil.refreshIgObjects(new Object[]{intSelectedDet, montoTotalAplicarForaneo, txtCambio});
 
		}catch(Exception ex){
			ex.printStackTrace(); 
			
		}
		finally{
			ConsolidadoDepositosBcoCtrl.isNewSession = nuevaConexion;
			ConsolidadoDepositosBcoCtrl.closeCurrentSession( committ );
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
	public boolean generarAsientosFichaCV(Session s, Transaction tx,Connection cn,List lstPagoFicha, Vf55ca01 f55ca01, Credhdr hFac,
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
			bContabilizado = recCtrl.registrarAsientoDiario(dtFecha, cn, sSucursaldeAsiento,"P9", iNoDocForaneo, (iContadorFor) * 1.0,iNoBatch, 
								sCuentaMetodo[0], sCuentaMetodo[1], sCuentaMetodo[3], sCuentaMetodo[4],sCuentaMetodo[5],"CA", mPago.getMoneda(), 
								d.pasarAentero(d.roundDouble(mPago.getMonto())), sConcepto, vaut.getId().getLogin(), vaut.getId().getCodapp(), mPago.getTasa(), sTipoCliente, "Deb caja dolares "
								+ d.roundDouble(mPago.getMonto()), sCuentaMetodo[2],"","","USD",sCuentaMetodo[2],"F");
			if (bContabilizado) {
				bContabilizado = recCtrl.registrarAsientoDiario(dtFecha,cn, sSucursaldeAsiento,"P9",iNoDocForaneo, (iContadorFor) * 1.0, iNoBatch,
								sCuentaMetodo[0], sCuentaMetodo[1], sCuentaMetodo[3], sCuentaMetodo[4], sCuentaMetodo[5], "AA", mPago.getMoneda(),
								d.pasarAentero(d.roundDouble(mPago.getEquivalente())), sConcepto,vaut.getId().getLogin(), vaut.getId().getCodapp(), mPago.getTasa(),
								sTipoCliente, "Deb caja dolares " + d.roundDouble(mPago.getMonto()), sCuentaMetodo[2],"","","COR",sCuentaMetodo[2],"F");
				if (bContabilizado) {
					bContabilizado = recCtrl.registrarAsientoDiario(dtFecha,cn, sSucursaldeAsiento,"P9", iNoDocForaneo,(iContadorFor + 1) * 1.0, iNoBatch, 
									sCtaMetodoDom[0],sCtaMetodoDom[1],sCtaMetodoDom[3],sCtaMetodoDom[4],sCtaMetodoDom[5], "CA", mPago.getMoneda(), (-1)* d.pasarAentero(d.roundDouble(mPago.getMonto())), sConcepto,vaut.getId().getLogin(), vaut.getId().getCodapp(),
									mPago.getTasa(), sTipoCliente,"Cred caja cordobas " + d.roundDouble(mPago.getMonto()),sCtaMetodoDom[2],"","","USD",sCtaMetodoDom[2],"F");
					if (bContabilizado) {
						bContabilizado = recCtrl.registrarAsientoDiario(dtFecha, cn,sSucursaldeAsiento,"P9", iNoDocForaneo,(iContadorFor + 1) * 1.0, iNoBatch,sCtaMetodoDom[0], sCtaMetodoDom[1], sCtaMetodoDom[3], sCtaMetodoDom[4],sCtaMetodoDom[5],
										"AA", mPago.getMoneda(), (-1)* d.pasarAentero(d.roundDouble(mPago.getEquivalente())),sConcepto, vaut.getId().getLogin(), vaut
										.getId().getCodapp(), mPago.getTasa(),sTipoCliente, "Cred caja cordobas "+ d.roundDouble(mPago.getMonto()), 
										sCtaMetodoDom[2],"","","COR",sCtaMetodoDom[2],"F");
						iContadorFor++;
						
						if(bContabilizado){
							bContabilizado = recCtrl.fillEnlaceMcajaJde(s,tx,iNumFicha, hFac.getId().getCodcomp(), iNoDocForaneo, iNoBatch,f55ca01.getId().getCaid(),f55ca01.getId().getCaco(),"A","FCV");
							if(bContabilizado){								
								bContabilizado = recCtrl.registrarBatchA92(cn,"G", iNoBatch, d.pasarAentero(d.roundDouble(mPago.getMonto())), vaut.getId().getLogin(), 1,MetodosPagoCtrl.DEPOSITO);
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
	/*************INSERTAR FICHA DE COMPRA/VENTA
	 * @throws Exception ******************************************************************/	
	public boolean insertarFichaCV(Session session, Transaction tx, Vf55ca01 f55ca01,  Vautoriz vautoriz,double dMonto,int iNumrec, String sCajero,List lstPagoFicha) throws Exception{
		boolean bInsertado = false;
		NumcajaCtrl numCtrl = new NumcajaCtrl();
		int iNoFicha = 0;
		ReciboCtrl conCtrl = new ReciboCtrl();
		String sCodunineg = "",codSuc="",codLinea="",codCompania="",codMoneda="";
		FinanciamientoCtrl ctrFianciamiento = new FinanciamientoCtrl();
		try{

			codCompania = ctrFianciamiento.obtenerValoresSpe(PropertiesSystem.BYTE_PARAMETRO_CONFIGURACION, PropertiesSystem.BYTE_PARAMETRO_COMPANIA).getDESCRIPCION();
			codSuc= ctrFianciamiento.obtenerValoresSpe(PropertiesSystem.BYTE_PARAMETRO_CONFIGURACION, PropertiesSystem.BYTE_PARAMETRO_SUCURSAL).getDESCRIPCION();
			codLinea = ctrFianciamiento.obtenerValoresSpe(PropertiesSystem.BYTE_PARAMETRO_CONFIGURACION, PropertiesSystem.BYTE_PARAMETRO_LINEA).getDESCRIPCION();
			codMoneda = ctrFianciamiento.obtenerValoresSpe(PropertiesSystem.BYTE_PARAMETRO_CONFIGURACION, PropertiesSystem.BYTE_PARAMETRO_MONEDA).getDESCRIPCION();
			
			sCodunineg = CompaniaCtrl.leerUnidadNegocioPorLineaSucursal(codSuc, codLinea ); 
			//sCodunineg = hFac.getId().getCodsuc().substring(3, 5) + hFac.getId().getLinea();
		 
			//Revisar
			iNoFicha = numCtrl.obtenerNoSiguiente("FICHACV", f55ca01.getId().getCaid(), codCompania, f55ca01.getId().getCaco(), vautoriz.getId().getLogin());
			int codigoCliente = Integer.parseInt(txtCodcli.getValue().toString());
			if(iNoFicha > 0){

				//registrar encabezado de ficha
				bInsertado = conCtrl.registrarReciboNoSession( iNoFicha, 0,
						codCompania, dMonto, dMonto, 0, "",
						new Date(), new Date(), codigoCliente,
						txtNomcli.getValue().toString(), sCajero, f55ca01.getId().getCaid(),
						f55ca01.getId().getCaco(), vautoriz.getId().getLogin(),
						"FCV", 0, "", iNumrec, new Date(), sCodunineg, "", codMoneda,session);
				
				if (bInsertado){
					 
					bInsertado = conCtrl.registrarDetalleReciboNoSession( iNoFicha, 0, codCompania, lstPagoFicha,f55ca01.getId().getCaid(),f55ca01.getId().getCaco(),"FCV",session);
					
					m.put("iNoFichaFinan", iNoFicha);
					if(!bInsertado){
						lblMensajeError.setValue("No se pudo registrar el detalle de la ficha de compra/venta!!!");	
						String msg ="No se pudo registrar el detalle de la ficha de compra/venta!!!";
						throw new Exception(msg);
					}
					
				}else{
					lblMensajeError.setValue("No se pudo registrar la ficha de compra/venta!!!");
					String msg ="No se pudo registrar la ficha de compra/venta!!!";
					throw new Exception(msg);
				}
			}else{
				lblMensajeError.setValue("No se encontro No. de ficha configurado para esta caja!!!");
				bInsertado = false;
				String msg ="No se encontro No. de ficha configurado para esta caja!!!";
				throw new Exception(msg);
			}
		}catch(Exception ex){
			bInsertado = false;
			throw new Exception(ex.getMessage());
			
		}
		return bInsertado;
	}
/*********************************************************************************************/

	/** Grabar el recibo. **/
	@SuppressWarnings("unchecked")
	public void grabarRecibo(ActionEvent ev){
		Connection cn = null;

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
		
		Divisas d= new Divisas();
		
		String msgProceso = new String("");
		String codsuc     = new String("");
		String codcomp    = new String("");
		String tiporec    = new String("FN"); 
		//String monedabase = new String("");
		String monedatrx;
		String codLinea = "";
				
		BigDecimal tasaofi  = BigDecimal.ONE;
		BigDecimal tasapara = BigDecimal.ONE;
		
		List<MetodosPago> lstMetodosPago  = null;
		List<Finanhdr> lstCreditosFinan = null;
		List<Finandet> lstFacturasSelected  = null;
		List<Credhdr> lstComponentes  = null;
		
		//Finanhdr f = null;
		
		List<String[]>dtaFacs = new ArrayList<String[]>();
		List<String[]>dtaFacsTemp = new ArrayList<String[]>();
		int iNumrecTmp = 0 ;
		Date fechaRecibo = new Date();
		int fechajuliana = FechasUtil .DateToJulian(fechaRecibo);
		
		Object[] dtaCnDnc = null;
		boolean cnDonacion = false;
		boolean aplicadonacion = false;
		List<MetodosPago>pagosConDonacion;
		FinanciamientoCtrl ctrFianciamiento = new FinanciamientoCtrl();
		int codigoCliente = Integer.parseInt(txtCodcli.getValue().toString());
		
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
			
			LogCajaService.CreateLog("FinanciamientoV2DAO.grabarRecibo - INICIO", "JR8", "Inicio ejecucion Metodo");
			
			CodeUtil.removeFromSessionMap("Fn_MontoAplicadoRecibo");
			
			
			
			String sCajero = (String)m.get("sNombreEmpleado");	
			Vf55ca01 caja = ((List<Vf55ca01>) m.get("lstCajas")).get(0);
			Vautoriz vaut = ((Vautoriz[]) m.get("sevAut"))[0];
			
			lstFacturasSelected = (List<Finandet>) m.get("lstSelectedCuotas");
			lstComponentes   = (List<Credhdr>) m.get("lstComponentes");
			lstCreditosFinan = (List<Finanhdr>)m.get("lstCreditosFinan");
			lstMetodosPago   = (ArrayList<MetodosPago>) m.get("lstPagosFinan");
			 
			
			//f = lstCreditosFinan.get(0);
			caid = caja.getId().getCaid();
			codcomp =ctrFianciamiento.obtenerValoresSpe(PropertiesSystem.BYTE_PARAMETRO_CONFIGURACION, PropertiesSystem.BYTE_PARAMETRO_COMPANIA).getDESCRIPCION();
			monedatrx =ctrFianciamiento.obtenerValoresSpe(PropertiesSystem.BYTE_PARAMETRO_CONFIGURACION, PropertiesSystem.BYTE_PARAMETRO_MONEDA).getDESCRIPCION();
			codsuc = ctrFianciamiento.obtenerValoresSpe(PropertiesSystem.BYTE_PARAMETRO_CONFIGURACION, PropertiesSystem.BYTE_PARAMETRO_SUCURSAL).getDESCRIPCION();
			
			dtComp = com.casapellas.controles.tmp.CompaniaCtrl
							.filtrarCompania((F55ca014[])m.get
							("cont_f55ca014"), codcomp);
			//monedabase = dtComp.getId().getC4bcrcd();
			
			
	
			
			//&& ========= Validar pago con socket.
			for (MetodosPago m : lstMetodosPago) {
				bHayPagoSocket = (m.getMetodo().compareTo(MetodosPagoCtrl.TARJETA) == 0 && 
								  m.getVmanual().compareTo("2") == 0 ) ;
				if( bHayPagoSocket ) break;
			}
			//&& ========= aplicar el pago de Socket Pos msgSocket = ""; 
			if( bHayPagoSocket ){
				msgProceso =  PosCtrl.aplicarPagoSocketPos( 
									lstMetodosPago, txtNomcli.getValue().toString(), 
									caid, codcomp, tiporec);
				aplicado = msgProceso.compareTo("") == 0;
				bAplicadoSockP = aplicado;
				if(!aplicado) return;
			}
			//&& ======== tasas de cambio.
			tasaofi  = tasaOficial("COR");
			tasapara = tasaParalela("COR");
			
			//&& ========= Conexiones.
			cn = As400Connection.getJNDIConnection("DSMCAJA2");
			cn.setAutoCommit(false);

			
			//&& ========== solo para insert del recibo de financimiento
			sessionInsJdeCaja = HibernateUtilPruebaCn.currentSession();
			transInsJdeCaja = sessionInsJdeCaja.beginTransaction();
			
			
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
			
			aplicado = insertarRecibo(sessionInsJdeCaja, transInsJdeCaja, caja,  lstMetodosPago, vaut, cn, lstComponentes);
			
			if(!aplicado){
				msgProceso = "Recibo no aplicado, error al grabar los datos de recibo caja ";
			throw new Exception(msgProceso);
			}
			numrec = Integer.parseInt(String.valueOf( m.get("iNumRecFinan")) );
			 
			sacarCambioMetodos(sessionInsJdeCaja, transInsJdeCaja, caja,vaut,lstMetodosPago, sCajero, numrec, monedatrx);
			
			List<MetodosPago> lstPagoFicha = (List<MetodosPago>) (m.get("lstPagoFichaFinan"));
			
			bHayFicha = (lstPagoFicha != null && !lstPagoFicha.isEmpty());
			
			
			//&& ===============================================================================//
			//&& ================ pagar facturas de credito en cuentas po cobrar ===============// 

			List<String> numerosRecibosJde = new ArrayList<String>();
			
			for (int i = 0; i < lstMetodosPago.size(); i++) {
				int numeroReciboJde = Divisas.numeroSiguienteJdeE1( CodigosJDE1.NUMEROPAGORECIBO );
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
			
			ProcesarPagoFacturaJdeV2 ppf = new  ProcesarPagoFacturaJdeV2() ;
			
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
			ppf.estadobatch = CodigosJDE1.BATCH_ESTADO_PENDIENTE ;
			
			ppf.msgProceso = "";
			MetodosPago[] metPago = null;			
			metPago = new MetodosPago[lstMetodosPago.size()];	
			serviceRequest service = new serviceRequest();
			df.setRoundingMode(RoundingMode.UP);	
			if(pagoFin == null)
				pagoFin = new PagoFinanciamiento();
			pagoFin.setMontoEfectivo(BigDecimal.ZERO);
			pagoFin.setMontoTarjeta(BigDecimal.ZERO);
			pagoFin.setMontoCheque(BigDecimal.ZERO);
			pagoFin.setMontoDepositoBanco(BigDecimal.ZERO);
			pagoFin.setMontoTransferencia(BigDecimal.ZERO);
			double efectivo = 0,tarjeta=0,ck=0,deposito=0,tranferencia=0;
			
			for (int i = 0; i < lstMetodosPago.size(); i++) {
				metPago[i] = (MetodosPago) lstMetodosPago.get(i);
				
				if (metPago[i].getMetodo().equals(MetodosPagoCtrl.EFECTIVO)){					
					BigDecimal monto = new BigDecimal(metPago[i].getEquivalente());
					monto = monto.setScale(2,RoundingMode.HALF_UP);
					efectivo += monto.doubleValue();
				pagoFin.setMontoEfectivo(BigDecimal.valueOf(Double.parseDouble(df.format(efectivo))));
				}else if (metPago[i].getMetodo().equals(MetodosPagoCtrl.TARJETA)){
					BigDecimal monto = new BigDecimal(metPago[i].getEquivalente());
					monto = monto.setScale(2,RoundingMode.HALF_UP);
					tarjeta += monto.doubleValue();
					pagoFin.setMontoTarjeta(BigDecimal.valueOf(Double.parseDouble(df.format(tarjeta))));
				}else if (metPago[i].getMetodo().equals(MetodosPagoCtrl.CHEQUE)){
					BigDecimal monto = new BigDecimal(metPago[i].getEquivalente());
					monto = monto.setScale(2,RoundingMode.HALF_UP);
					ck += monto.doubleValue();
					pagoFin.setMontoCheque(BigDecimal.valueOf(Double.parseDouble(df.format(ck))));
				}else if (metPago[i].getMetodo().equals(MetodosPagoCtrl.DEPOSITO)){
					BigDecimal monto = new BigDecimal(metPago[i].getEquivalente());
					monto = monto.setScale(2,RoundingMode.HALF_UP);
					deposito += monto.doubleValue();
					pagoFin.setMontoDepositoBanco(BigDecimal.valueOf(Double.parseDouble(df.format(deposito))));
				}else if (metPago[i].getMetodo().equals(MetodosPagoCtrl.TRANSFERENCIA)){
					BigDecimal monto = new BigDecimal(metPago[i].getEquivalente());
					monto = monto.setScale(2,RoundingMode.HALF_UP);
					tranferencia += monto.doubleValue();
					pagoFin.setMontoTransferencia(BigDecimal.valueOf(Double.parseDouble(df.format(tranferencia))));
				}				
				}
			//Se setea el pago total a aplicar a Byte
			String sTotalT = txtMontoAplicar.getValue().toString();					
			double montoPagoNet = d.formatStringToDouble(sTotalT);			
			pagoFin.setMontoTotal(BigDecimal.valueOf(montoPagoNet));
			
			//Esto verifica si al vuelto y si hay vuelto de debe restar del monto en efectivo
			try {
				String vueltoCS = txtCambio.getValue().toString();					
				double montoVueltoCS =0,montoRecibido=0,montovueltoForaneo;
				
				if(!vueltoCS.equals("")) {
				montoVueltoCS=d.formatStringToDouble(vueltoCS);
				}
				double montoVueltoUSD=Double.parseDouble(df.format(montoVueltoCS/tasapara.doubleValue()));
				double montoEfectivoAPlicado = d.formatStringToDouble(txtMontoAplicar.getValue().toString());
				montoRecibido = d.formatStringToDouble(txtMontoRecibido.getValue().toString());
				
				if(!txtCambioForaneo.getValue().toString().equals("")) {
					montovueltoForaneo=d.formatStringToDouble(txtCambioForaneo.getValue().toString());
					if(montovueltoForaneo>0)
					montoVueltoUSD=montovueltoForaneo;
				}
				
				if(pagoFin.getMontoEfectivo().doubleValue()>0)
				if(montoVueltoUSD>0) {
					
					
					if(montoRecibido != (montoVueltoUSD+montoEfectivoAPlicado)) {
						 msgProceso = "Los montos no concuerdan - Monto Recibido:" + montoRecibido +" Monto Aplicado:"+  montoEfectivoAPlicado 
								 	+ " Vuelto:"+montoVueltoUSD + " Diferencia:"+(montoRecibido - (montoVueltoUSD+montoEfectivoAPlicado));
						throw new Exception(msgProceso);
					}
				
					
					
					if(lstMetodosPago.size()>1) {
						if(pagoFin.getMontoEfectivo().doubleValue()>0) {
							double pa =pagoFin.getMontoEfectivo().doubleValue()-montoVueltoUSD;
							
							pagoFin.setMontoEfectivo(BigDecimal.valueOf(pa));
						}
	
					}
					else {
						if(pagoFin.getMontoEfectivo().doubleValue()>0) {		
						 montoVueltoUSD=d.formatStringToDouble(txtMontoAplicar.getValue().toString());
						 montoEfectivoAPlicado = montoVueltoUSD;				
						
						pagoFin.setMontoEfectivo(BigDecimal.valueOf(montoEfectivoAPlicado));
						}
					}
			}
			}catch(Exception e) {
				 msgProceso = e.getMessage();				 
				throw new Exception(msgProceso);
			}
			pagoFin.setNumeroRecibo(numrec);
			if(noPrestamo ==null)
				noPrestamo = m.get("noPrestamoByte").toString();
			pagoFin.setNoPrestamo(noPrestamo);
			pagoFin.setTipoPago(ddlTipoReciboPago.getValue().toString());
			 
		
			MovimientoCuenta mov =service.pagarFinanciamiento(pagoFin, terminal);
			
			
			if( mov == null ){
				msgProceso = "Error al procesar request!!!";
				throw new Exception(msgProceso);
			}else if(mov.getMensaje()=="BERROR") {
				msgProceso = mov.getMensaje();
				throw new Exception(msgProceso);
			}else if( mov.getNumeroReciboCore() ==0) {
				msgProceso = mov.getMensaje();
				throw new Exception(msgProceso);
			}else {
				ReciboCtrl re = new ReciboCtrl();
				String json= service.getResquestPago(pagoFin);
			boolean insertRec=	re.registrarReciboByte(numrec, caid, codcomp, codsuc, mov.getNumeroReciboCore(), noPrestamo,json,sessionInsJdeCaja);
			
			List<detalleCobroFactura> cuotas = (List<detalleCobroFactura>) m.get("lstSelectedCuotas");	
			String reci =  String.valueOf(mov.getNumeroReciboCore()).substring(0, 7);
			String cuentaTransitoria =ctrFianciamiento.obtenerValoresSpe(PropertiesSystem.BYTE_PARAMETRO_CONFIGURACION, PropertiesSystem.BYTE_PARAMETRO_CUENTA_TRANSITORIA).getDESCRIPCION();
			 String cuentaPorCobrar =ctrFianciamiento.obtenerValoresSpe(PropertiesSystem.BYTE_PARAMETRO_CONFIGURACION, PropertiesSystem.BYTE_PARAMETRO_CUENTA_POR_COBRAR).getDESCRIPCION();
			 codLinea = ctrFianciamiento.obtenerValoresSpe(PropertiesSystem.BYTE_PARAMETRO_CONFIGURACION, PropertiesSystem.BYTE_PARAMETRO_LINEA).getDESCRIPCION();
			 String codSuc2= ctrFianciamiento.obtenerValoresSpe(PropertiesSystem.BYTE_PARAMETRO_CONFIGURACION, PropertiesSystem.BYTE_PARAMETRO_SUCURSAL).getDESCRIPCION();
			 String sCodunineg = CompaniaCtrl.leerUnidadNegocioPorLineaSucursal(codSuc2, codLinea ); 
			
			if(sCodunineg.length()==0) {
				throw new Exception("No se pudo leer la unidad de negocio por linea sucursal");
			}
			 
			 for(detalleCobroFactura item:cuotas) {
				 String[] dta = new String[] {
							String.valueOf(reci),
							"" +item.getAplicado(), 
							""+item.getNoFactura(),
							sCodunineg,
							codsuc,
							item.getTipoFactura() };
				 if(item.getAplicado()>0)
					dtaFacs.add(dta);
					
			 }
			iNumrecTmp = re
					.procesoPagoFacturaByte(dtaFacs, caid, codcomp, fechajuliana, codigoCliente, tiporec,numrec,sessionInsJdeCaja);
			
			 /*com.casapellas.controles.ReciboCtrl
				.actualizarRecibofac(dtaFacs, caid, codcomp, FechasUtil
						.DateToJulian(fechaRecibo), codigoCliente, tiporec, iNumrecTmp, numrec);
			*/
			if(insertRec) {
				//Grabar Batch
				double mAP =d.formatStringToDouble(txtMontoAplicar.getValue().toString());
				double tAP = d.formatStringToDouble(txtMontoRecibido.getValue().toString());
				BatchControlF0011 f0011 = new BatchControlF0011(
						 "G",
						 ""+numeroBatchJde,
						 "0",
						 vaut.getId().getLogin(),
						 mAP+"",
						 String.valueOf(  lstMetodosPago.size() ),
						 mAP+"",
						fechaRecibo,
						"RFINANCIA",
						 ""
					);
				
				re.registrarReciboF0011(f0011.insertStatement(),sessionInsJdeCaja);
				
			
						
				
				 double montoaplicadotrx=mAP;
				 
				 int i=1;
				 String[] sCuentaCaja = null,sCuentaDC =null;
				 String idCuentaPuente="";
				 Divisas dv = new Divisas();
			
				String	cuentat1=cuentaTransitoria.split("\\.")[0];
				String	cuentat2=cuentaTransitoria.split("\\.")[1];
				String	cuentat3=cuentaTransitoria.split("\\.")[2];
				 sCuentaCaja = dv.obtenerCuentaCaja(caid,codcomp, "N", monedatrx,sessionInsJdeCaja,transInsJdeCaja,null,null);
				 idCuentaPuente= re.obtenerIdCuenta(sessionInsJdeCaja, transInsJdeCaja, cuentat1, cuentat2, cuentat3);				
				 df.setRoundingMode(RoundingMode.UP);	 
				 boolean vericarIns=true;
					for (int a = 0; a < lstMetodosPago.size(); a++) {
						metPago[a] = (MetodosPago) lstMetodosPago.get(a);
						
						 sCuentaCaja = dv.obtenerCuentaCaja(caid,codcomp,metPago[a].getMetodo(), metPago[a].getMoneda(),sessionInsJdeCaja,transInsJdeCaja,null,null);
						 if(!metPago[a].getMoneda().equals(monedatrx)) {
							 double valorMontoTOficial=0,valorMontoDCambiario=0;
							 valorMontoTOficial=Double.parseDouble( df.format(metPago[a].getEquivalente()*tasaofi.doubleValue()));
							 sCuentaDC=dv.obtenerCuentaDifCambiario(caid, codcomp, sCodunineg, sessionInsJdeCaja, transInsJdeCaja, null, null);
							 valorMontoDCambiario=Double.parseDouble( df.format(metPago[a].getMonto()-valorMontoTOficial));
							 if(metPago[a].getMonto()!= (valorMontoTOficial+valorMontoDCambiario)) {
								 double valorsinFormato=metPago[a].getMonto()-valorMontoTOficial,sumaConFormato,sumaSinformato;
								 if(metPago[a].getMonto()== (valorMontoTOficial+valorsinFormato) )
								 {
									 valorMontoDCambiario=valorsinFormato;
								 }
								 
							 }
							 
							 vericarIns =	 re.registrarAsientoDiarioLogs(sessionInsJdeCaja,  "Asiento Byte", fechaRecibo,  codsuc, "P9", numeroReciboJde,i,
										numeroBatchJde,sCuentaCaja[0], sCuentaCaja[1], 
										sCuentaCaja[3], sCuentaCaja[4], sCuentaCaja[5],
										"AA", metPago[a].getMoneda(), (long) (metPago[a].getMonto() *100), 
										"Asiento Byte", vaut.getId().getLogin(), vaut.getId().getCodapp(), 
										BigDecimal.ZERO, "","R:"+numrec+" Ca:"+caid+" Com:"+codcomp,
										sCuentaCaja[2], "", "", "COR",sCuentaCaja[2],"D", 0);
							 if(!vericarIns) {
								 throw new Exception("No se pudo registrar el Asiento contable");
							 }
							 i = i+1;
							 vericarIns = re.registrarAsientoDiarioLogs(sessionInsJdeCaja,  "Asiento Byte", fechaRecibo,  codsuc, "P9", numeroReciboJde,i,
										numeroBatchJde, cuentaTransitoria, idCuentaPuente, 
										cuentat1, cuentat2, cuentat3,
										"AA", metPago[a].getMoneda(), (long) (valorMontoTOficial *-100), 
										"Asiento Byte", vaut.getId().getLogin(), vaut.getId().getCodapp(), 
										BigDecimal.ZERO, "","R:"+numrec+" Ca:"+caid+" Com:"+codcomp,
										cuentat1, "", "", "COR",cuentat1,"D", 0);
							 if(!vericarIns) {
								 throw new Exception("No se pudo registrar el Asiento contable");
							 }
							 i = i+1;	
							 vericarIns= re.registrarAsientoDiarioLogs(sessionInsJdeCaja, "Asiento Byte Diferencial", fechaRecibo, codsuc,"P9", numeroReciboJde,i,numeroBatchJde,sCuentaDC[0],sCuentaDC[1],sCuentaDC[3],sCuentaDC[4],
										sCuentaDC[5],"AA",metPago[a].getMoneda(),(long)(valorMontoDCambiario*-100),"Asiento Byte Diferencial",vaut.getId().getLogin(),vaut.getId().getCodapp(),BigDecimal.ZERO,"",
										"Dif/Camb Met: " + metPago[a].getMoneda(),sCuentaDC[2],"","","COR",sCuentaDC[2],"D", 0);
							 if(!vericarIns) {
								 throw new Exception("No se pudo registrar el Asiento contable del direncial");
							 }
								 i = i+1;
								
						 }else {

							 
							 vericarIns =re.registrarAsientoDiarioLogs(sessionInsJdeCaja,  "Asiento Byte", fechaRecibo,  codsuc, "P9", numeroReciboJde,i,
										numeroBatchJde,sCuentaCaja[0], sCuentaCaja[1], 
										sCuentaCaja[3], sCuentaCaja[4], sCuentaCaja[5],
										"AA", monedatrx, (long) (Double.parseDouble( df.format(metPago[a].getEquivalente() *tasaofi.doubleValue()))*100), 
										"Asiento Byte", vaut.getId().getLogin(), vaut.getId().getCodapp(), 
										tasaofi, "","R:"+numrec+" Ca:"+caid+" Com:"+codcomp,
										sCuentaCaja[2], "", "", "COR",sCuentaCaja[2],"F", (long) (metPago[a].getEquivalente()*100));
							 if(!vericarIns) {
								 throw new Exception("No se pudo registrar el Asiento contable");
							 }
							 vericarIns=	re.registrarAsientoDiarioLogs(sessionInsJdeCaja,  "Asiento Byte", fechaRecibo,  codsuc, "P9", numeroReciboJde,i,
										numeroBatchJde,sCuentaCaja[0], sCuentaCaja[1], 
										sCuentaCaja[3], sCuentaCaja[4], sCuentaCaja[5],
										"CA", monedatrx, (long) (metPago[a].getEquivalente()*100), 
										"Asiento Byte", vaut.getId().getLogin(), vaut.getId().getCodapp(), 
										tasaofi, "","R:"+numrec+" Ca:"+caid+" Com:"+codcomp,
										sCuentaCaja[2], "", "", "COR",sCuentaCaja[2],"F", 0);
							 if(!vericarIns) {
								 throw new Exception("No se pudo registrar el Asiento contable");
							 }
								i = i+1;
								vericarIns=	re.registrarAsientoDiarioLogs(sessionInsJdeCaja,  "Asiento Byte", fechaRecibo,  codsuc, "P9", numeroReciboJde,i,
										numeroBatchJde, cuentaTransitoria, idCuentaPuente, 
										cuentat1, cuentat2, cuentat3,
										"AA", monedatrx, (long) (Double.parseDouble( df.format(metPago[a].getEquivalente() *tasaofi.doubleValue()))*-100), 
										"Asiento Byte", vaut.getId().getLogin(), vaut.getId().getCodapp(), 
										tasaofi, "","R:"+numrec+" Ca:"+caid+" Com:"+codcomp,
										cuentat1, "", "", "COR",cuentat1,"F", (long) (metPago[a].getEquivalente()*-100));
								if(!vericarIns) {
									 throw new Exception("No se pudo registrar el Asiento contable");
								 }
								vericarIns=	re.registrarAsientoDiarioLogs(sessionInsJdeCaja,  "Asiento Byte", fechaRecibo,  codsuc, "P9", numeroReciboJde,i,
										numeroBatchJde, cuentaTransitoria, idCuentaPuente, 
										cuentat1, cuentat2, cuentat3,
										"CA", monedatrx, (long) (metPago[a].getEquivalente()*-100), 
										"Asiento Byte", vaut.getId().getLogin(), vaut.getId().getCodapp(), 
										tasaofi, "","R:"+numrec+" Ca:"+caid+" Com:"+codcomp,
										cuentat1, "", "", "COR",cuentat1,"F", 0);
								if(!vericarIns) {
									 throw new Exception("No se pudo registrar el Asiento contable");
								 }
								i = i+1;
						 }
					}
				 
			
				 boolean registarRecEnlace=ReciboCtrl.crearRegistroReciboCajaJde(sessionInsJdeCaja, caid, numrec, codcomp, codsuc, tiporec, "G", numeroBatchJde, numerosRecibosJde);
				
				 if(!registarRecEnlace) {
					 throw new Exception("No se pudo registrar en la tabla enlace");
				 }
				 
				 this.generarFinalizacion();
				 
				
			}
			
			}
			 
		} catch (Exception e) {
			LogCajaService.CreateLog("grabarRecibo", "ERR", e.getMessage());		
			msgProceso = e.getMessage();
			aplicado = false;
		}finally{
			if (aplicado) {
				//Session sessionInsJdeCaja = null;
				try {
					transInsJdeCaja.commit();
					ReciboCtrl re = new ReciboCtrl();
					 re.actualizarEstadoRecibo(numrec,caid,codcomp);
				}catch(Exception e) {
					LogCajaService.CreateLog("grabarRecibo-Commit", "ERR", e.getMessage());	
				}
				
				try {
					com.casapellas.controles.tmp.CtrlCajas.imprimirRecibo (caid, numrec, codcomp, codsuc, tiporec, false);
				}catch(Exception e) {
					LogCajaService.CreateLog("grabarRecibo-Imprimir", "ERR", e.getMessage());	
				}
			
			}else {
				try {
					transInsJdeCaja.rollback();
				}catch(Exception e) {
					LogCajaService.CreateLog("grabarRecibo", "ERR", e.getMessage());	
				}
			}			
			HibernateUtilPruebaCn.closeSession(sessionInsJdeCaja);
			dwCargando.setWindowState("hidden");
			dwProcesaRecibo.setWindowState("hidden");

			if(msgProceso.compareTo("") != 0){
				
				dwProcesaRecibo.setWindowState("hidden");
				dwCargando.setWindowState("hidden");
				lblMensajeError.setValue(msgProceso);
				dwMensajeError.setStyle("width:390px; min-height:145px;");
				dwMensajeError.setWindowState("normal");
				
			}else {
				dwReciboV2.setWindowState("hidden");
				dwProcesaRecibo.setWindowState("hidden");
				dwCargando.setWindowState("hidden");
				lblMensajeError.setValue("Recibo Generado!!!!!");
				dwMensajeError.setStyle("width:390px; min-height:145px;");
				dwMensajeError.setWindowState("normal");
			}

			m.remove("procesandoReciboValidacion");
			
			LogCajaService.CreateLog("FinanciamientoV2DAO.grabarRecibo - FIN", "JR8", "Fin ejecucion Metodo");
			
		}
		BitacoraSecuenciaReciboService.actualizarSatisfactorioLogReciboNumerico(caid, numrec, codcomp,codsuc, "FN");
	}
	
	/*********
	 * 
	 * Este Metodo es el encargo de generar y completar los recibos en caja
	 */	
	public void generarFinalizacion() {
		

		CodeUtil.removeFromSessionMap(new String[] { "fn_NumeroBatchReciboJde", "lstDatosTrack_Con",
				"iNoFichaFinan", "lstPagoFichaFinan", "iNumRecFinan",
				"lstSelectedCuotas", "lstSelectedCuotas",
				"lstCreditosFinan", "lstPagosFinan","lstPagos","consultaInfoPrestamo" });
		
		m.put("lstCuotasV2", new ArrayList());
		m.put("lstPagos", new ArrayList());
		gvCuotas.dataBind();
		
		dwReciboV2.setWindowState("hidden");
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
		Connection cn = null;
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
				As400Connection as400connection = new As400Connection();		
				cn = as400connection.getJNDIConnection("DSMCAJA2");
				cn.setAutoCommit(false);
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
					bCorrecto = insertarRecibo(s,tx,f55ca01,lstMetodosPago,vautoriz[0],cn,lstComponentes);
					int iNumRecCredito = Integer.parseInt(m.get("iNumRecFinan").toString());
					
					
					/*Seccion para pagar el credito*/
					boolean bHayFicha = Boolean.parseBoolean(m.get("bHayFicha").toString());
					List lstPagoFicha = (List)(m.get("lstPagoFichaFinan"));
					if(bCorrecto && lstMetodosPago != null){
						
						//bCorrecto = pagarCreditoFinan(s,tx,cn,f55ca01,f,lstFacturasSelected,lstMetodosPago,iFecha,bdTasaJde,bdTasaPar,vautoriz[0],bHayFicha,lstPagoFicha,sMonedaBase,lstComponentes);
						////LogCrtl.sendLogDebgsFinancing("<===========> Seccion para pagar el credito : " + bCorrecto);
					}

					if(bCorrecto && lstMetodosPago != null){
						cn.commit();
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
														"FN",lstMetodosPago);
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
						cn.rollback();
						tx.rollback();
						dwReciboV2.setWindowState("hidden");
						dwProcesaRecibo.setWindowState("hidden");
						dwMensajeError.setWindowState("normal");
						dwMensajeError.setStyle("width:390px;height:145px");
						m.remove("lstDatosTrack_Con");
					}
					
					//&& ===== Imprimir el recibo.
					if(bCorrecto){
						
						////LogCrtl.sendLogDebgsFinancing("<===========> Imprimir el recibo. : " + bCorrecto);
						dwReciboV2.setWindowState("hidden");
						dwProcesaRecibo.setWindowState("hidden");lstCreditosFinan.clear();
						m.put("lstCreditosFinan",lstCreditosFinan);
						m.put("lstCuotasV2",new ArrayList());
						
						lblMensajeError.setValue("Operación realizada con éxito!!!");
						dwMensajeError.setWindowState("normal");
						dwMensajeError.setStyle("width:390px;height:145px");
						gvCuotas.dataBind();
						BigDecimal bdNumrec = new BigDecimal(iNumRecCredito);
						getP55recibo().setIDCAJA(new BigDecimal(f55ca01.getId().getCaid()));
						getP55recibo().setNORECIBO(bdNumrec);
						getP55recibo().setIDEMPRESA(f.getId().getCodcomp().trim());
						getP55recibo().setIDSUCURSAL(f55ca01.getId().getCaco().trim());
						getP55recibo().setTIPORECIBO("FN");
						getP55recibo().setRESULTADO("");
						getP55recibo().setCOMANDO("");
						getP55recibo().invoke();
						getP55recibo().getRESULTADO();
						if (bHayFicha) {
							recCtrl.actualizarNoFicha(cn, Integer.parseInt(m.get("iNumRecFinan").toString()), Integer.parseInt(m.get("iNoFichaFinan").toString()),
							f55ca01.getId().getCaid(), f.getId().getCodcomp(), f55ca01.getId().getCaco(), "FN");
							cn.commit();
						}
					}
//					else{
//						cn.rollback();
//						tx.rollback();
//						dwRecibo.setWindowState("hidden");
//						dwProcesaRecibo.setWindowState("hidden");
//						dwMensajeError.setWindowState("normal");
//						dwMensajeError.setStyle("width:390px;height:145px");
//						//cancelar transacciones con socket POS si hubieron 
//						anularPagosSP(lstMetodosPago);
//						m.remove("lstDatosTrack_Con");
//					}

					m.remove("fn_processRecibo");
					dwReciboV2.setWindowState("hidden");
					dwProcesaRecibo.setWindowState("hidden");
					dwMensajeError.setWindowState("normal");
					dwMensajeError.setStyle("width:390px;height:145px");

				}else{
					dwMensajeError.setWindowState("normal");
					dwMensajeError.setStyle("width:390px;height:145px");
					tx.rollback();
					cn.rollback();
					//cancelar transacciones con socket POS si hubieron 
					//anularPagosSP(lstMetodosPago);
					m.remove("lstDatosTrack_Con");
				}
			}
		}catch(Exception ex){		
			try{
				tx.rollback();
				cn.rollback();
				//cancelar transacciones con socket POS si hubieron 
				//anularPagosSP(lstMetodosPago);
				m.remove("lstDatosTrack_Con");
				////LogCrtl.sendLogDebgsFinancing("<===========> Error Reversion de Transacciones." );
			}catch(Exception ex2){
				ex2.printStackTrace();
			}
			m.remove("fn_processRecibo");
			dwReciboV2.setWindowState("hidden");
			dwMensajeError.setWindowState("normal");
			dwProcesaRecibo.setWindowState("hidden");
			lblMensajeError.setValue("Error no se pudo relizar la operación!!! " + ex);
			dwMensajeError.setStyle("width:390px;height:145px");
			ex.printStackTrace();
		}finally{			
			try{
				dwCargando.setWindowState("hidden");
				cn.close();
				s.close();			
			}catch(Exception ex){
				ex.printStackTrace();
			}
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


/**
 * @throws Exception **************************************************************************************/	
	public boolean insertarRecibo(Session s, Transaction tx, Vf55ca01 f55ca01,List lstMetodosPago,Vautoriz vAut, Connection cn,  List lstFacturasSelected) throws Exception{
		boolean insertado = true;
		int iNumrec = 0,iNumsol = 0,iNumrecm =0;
		
		ReciboCtrl recCtrl = new ReciboCtrl();
		FinanciamientoCtrl ctrFianciamiento = new FinanciamientoCtrl();
		double dMontoAplicar,dMontoRec,dCambio = 0.0;
		Divisas d = new Divisas();
		String sConcepto,sCajero,sCodunineg = "",codSuc="",codLinea="",codCompania="",codMoneda="";
		Solicitud sol = null;
		List lstSolicitud;
		CtrlSolicitud solCtrl = new CtrlSolicitud();
		Date dFecha = new Date();
		try{
			codCompania = ctrFianciamiento.obtenerValoresSpe(PropertiesSystem.BYTE_PARAMETRO_CONFIGURACION, PropertiesSystem.BYTE_PARAMETRO_COMPANIA).getDESCRIPCION();
			codSuc= ctrFianciamiento.obtenerValoresSpe(PropertiesSystem.BYTE_PARAMETRO_CONFIGURACION, PropertiesSystem.BYTE_PARAMETRO_SUCURSAL).getDESCRIPCION();
			codLinea = ctrFianciamiento.obtenerValoresSpe(PropertiesSystem.BYTE_PARAMETRO_CONFIGURACION, PropertiesSystem.BYTE_PARAMETRO_LINEA).getDESCRIPCION();
			codMoneda = ctrFianciamiento.obtenerValoresSpe(PropertiesSystem.BYTE_PARAMETRO_CONFIGURACION, PropertiesSystem.BYTE_PARAMETRO_MONEDA).getDESCRIPCION();
			
			
			if(!ddlTipoRecibo.getValue().toString().equals("01")){
				iNumrecm = Integer.parseInt(txtNumrec.getValue().toString().trim());
			}	
	
			iNumrec = com.casapellas.controles.tmp.ReciboCtrl.generarNumeroRecibo( f55ca01.getId().getCaid(), codCompania);
		
			
			
			if (iNumrec == 0) {
				String msg="No se encontro número de recibo " +
						"configurado para la compañia: " 
						+ codCompania;
				lblMensajeError.setValue(msg ) ; 
			throw new Exception(msg);
			}
			BitacoraSecuenciaReciboService.insertarLogReciboNumerico(f55ca01.getId().getCaid(),iNumrec, codCompania,CodeUtil.pad(f55ca01.getId().getCaco(), 5, "0"),"FN");
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
				
				
				
				sCodunineg = CompaniaCtrl.leerUnidadNegocioPorLineaSucursal(codSuc, codLinea ); 
				
				if( sCodunineg.trim().isEmpty() )
					sCodunineg = codSuc + codLinea;
		
				CodeUtil.putInSessionMap("Fn_CodUninegRecibo", sCodunineg.trim());
				CodeUtil.putInSessionMap("Fn_MontoAplicadoRecibo", Double.toString( dMontoAplicar ) );

				
				//&& ================ validacion para que la sumatoria de pagos no sea distinta a la del monto recibido
				BigDecimal sumMontoEquiv = CodeUtil.sumPropertyValueFromEntityList(lstMetodosPago, "equivalente", false);
				BigDecimal montoRecibido = new BigDecimal( Double.toString(dMontoRec) ) ;
			
	
				int codigoCliente = Integer.parseInt(txtCodcli.getValue().toString());
				
				// guardar el header del recibo
				insertado = recCtrl.registrarReciboNoSession( iNumrec, iNumrecm,
						codCompania, dMontoAplicar, dMontoRec,
						dCambio, sConcepto, dFecha, dFecha, codigoCliente, txtNomcli.getValue().toString(), sCajero,
						f55ca01.getId().getCaid(), codSuc,
						vAut.getId().getCoduser(), "FN", 0, "", 0, new Date(),
						sCodunineg, "", codMoneda,s);
				
				if (insertado) {
					
					//&& =============== guardar detalle de recibo
					lstMetodosPago = ponerCodigoBanco(lstMetodosPago);
					
					//&& =============== Determinar si el pago trabaja bajo preconciliacion y conservar su referencia original
					//com.casapellas.controles.BancoCtrl.datosPreconciliacion(lstMetodosPago, f55ca01.getId().getCaid(), codCompania) ;
					
					insertado = recCtrl.registrarDetalleReciboNoSession( iNumrec, iNumrecm,  codCompania, lstMetodosPago, 
								f55ca01.getId().getCaid(), f55ca01.getId().getCaco(), "FN",s);
 
					if (!insertado)  {
						String msg ="No se pudo Registrar el detalle de Recibo!!!";
						lblMensajeError.setValue(msg);
						throw new Exception(msg);
					}
				} else {
					String msg ="No se pudo Registrar el encabezado de Recibo!!!";
					lblMensajeError.setValue(msg);
					throw new Exception(msg);				
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
					//
					if (m.get("bdTasa") != null) {
						bdTasa = (BigDecimal) m.get("bdTasa");
					}
					insertado = recCtrl.registrarCambioNoSession(s, tx, iNumrec,codCompania, sLblCambio1, d.formatStringToDouble(sCambio1),f55ca01.getId().getCaid(), f55ca01.getId().getCaco(), bdTasa,"FN");
					if (!insertado)  {
						String msg ="No se pudo Registrar el cambio de Recibo!!!";
						lblMensajeError.setValue(msg);
						throw new Exception(msg);
					}
					insertado = recCtrl.registrarCambioNoSession(s, tx, iNumrec,codCompania, sLblCambio2, d.formatStringToDouble(sCambio2),f55ca01.getId().getCaid(), f55ca01.getId().getCaco(), bdTasa,"FN");
					if (!insertado)  {
						String msg ="No se pudo Registrar el cambio de Recibo!!!";
						lblMensajeError.setValue(msg);
						throw new Exception(msg);
					}
				} else {
					sCambio1 = txtCambio.getValue().toString();
					sLblCambios1 = sLblCambio1.trim().split(" ");
					//
					sLblCambio1 = sLblCambios1[sLblCambios1.length - 1].substring(0, 3);
					//
					if (m.get("bdTasa") != null) {
						bdTasa = (BigDecimal) m.get("bdTasa");
					}
					insertado = recCtrl.registrarCambioNoSession(s, tx, iNumrec,codCompania, sLblCambio1, d.formatStringToDouble(sCambio1),f55ca01.getId().getCaid(), f55ca01.getId().getCaco(), bdTasa,"FN");
					if (!insertado)  {
						String msg ="No se pudo Registrar el cambio de Recibo!!!";
						lblMensajeError.setValue(msg);
						throw new Exception(msg);
					}
				}
			} 
			
			m.put("iNumRecFinan", iNumrec);
			
		}catch(Exception ex){
			insertado = false;
		throw new Exception(ex.getMessage());
		}
		return insertado;
	}
/**
 * @throws Exception *************************************************************/	
	@SuppressWarnings("unchecked")
	public List sacarCambioMetodos(Session s, Transaction tx, Vf55ca01 f55ca01,Vautoriz vaut,List lstMetodosPago,String sCajero,int iNumrec, String sMonedaBase) throws Exception{
		Divisas d = new Divisas();
		String sCambio1 = "", sCambio2 = "";
		String[] sLblCambios1,sLblCambios2;
		double dCambio1 = 0.0, dCambio2 = 0.0, dNewMonto1 = 0.0, dCambio = 0.0, dMontoFicha = 0.0, dTotal = 0.0;
		MetodosPago[] metPago = null;
		boolean paso1 = false, bInsertado = false,bHayFicha = false;
		metPago = new MetodosPago[lstMetodosPago.size()];	
		String sLblCambio1 = "", sLblCambio2 = "",monedaPrestamo="";
		List lstPagoFicha = new ArrayList();
		BigDecimal bdTasa = BigDecimal.ZERO;
		FinanciamientoCtrl ctrFianciamiento = new FinanciamientoCtrl();
		try{
			monedaPrestamo = 	ctrFianciamiento.obtenerValoresSpe(PropertiesSystem.BYTE_PARAMETRO_CONFIGURACION, PropertiesSystem.BYTE_PARAMETRO_MONEDA).getDESCRIPCION();
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
							bInsertado = insertarFichaCV(s, tx,f55ca01, vaut,dMontoFicha, iNumrec, sCajero,lstPagoFicha);
							
							if(!bInsertado){
								String msg ="No se pudo registrar la ficha de compra para este recibo ";
								lblMensajeError.setValue(msg);
								throw new Exception(msg);
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
						if (sLblCambio1.equals(sMonedaBase)&& monedaPrestamo.equals(sMonedaBase)) {// se saca el cambio en cor del pago en cor
							dNewMonto1 = metPago[i].getMonto() - dCambio1;
							metPago[i].setMonto(dNewMonto1);
							metPago[i].setEquivalente(dNewMonto1);
							paso1 = true;
						} else if (sLblCambio1.equals(sMonedaBase) && !monedaPrestamo.equals(sMonedaBase) && lstMetodosPago.size() > 1) {// se saca el cambio del pago en cor y se convierte a usd con tasa del metodo
							dNewMonto1 = metPago[i].getMonto() - dCambio1;
							metPago[i].setMonto(dNewMonto1);
							metPago[i].setEquivalente(d.roundDouble(dNewMonto1 / metPago[i].getTasa().doubleValue()));
							paso1 = true;
						} else if (sLblCambio1.equals(sMonedaBase) && !monedaPrestamo.equals(sMonedaBase) && lstMetodosPago.size() == 1) {// si pago toda la fac de usd en cor
							dNewMonto1 = metPago[i].getMonto() - dCambio1;
							metPago[i].setMonto(dNewMonto1);
							
							double aplicado = d.formatStringToDouble(txtMontoAplicar.getValue().toString());
							
							//&& =============== el equivalente debe ser el monto aplicado.
							metPago[i].setEquivalente(aplicado);	//	metPago[i].setEquivalente(d.roundDouble(dNewMonto1/ metPago[i].getTasa().doubleValue()));
							
							paso1 = true;
							
						} else if (sLblCambio1.equals(sMonedaBase) && monedaPrestamo.equals(sMonedaBase) && lstMetodosPago.size() == 1) {// el pago en usd cubre toda la venta
							dNewMonto1 = metPago[i].getEquivalente() - dCambio1;
							metPago[i].setMonto(d.roundDouble(dNewMonto1 / metPago[i].getTasa().doubleValue()));
							metPago[i].setEquivalente(dNewMonto1);
							paso1 = true;
						}
						lstMetodosPago.remove(i);
						lstMetodosPago.add(i, metPago[i]);
						
					} else if (metPago[i].getMetodo().equals(MetodosPagoCtrl.EFECTIVO) && sLblCambio1.equals(sMonedaBase) && !metPago[i].getMoneda().equals(sMonedaBase) && !paso1) {
						if (monedaPrestamo.equals(sMonedaBase) && lstMetodosPago.size() == 1) {// el pago en usd cubre toda la venta
							
						}
					}
				}
				bHayFicha = false;
				m.put("bHayFicha", bHayFicha);
			}
			m.put("lstPagoFichaFinan",lstPagoFicha);
		}catch(Exception ex){
		throw new Exception(ex.getMessage());
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
			dwReciboV2.setWindowState("hidden");
 	
	}
/*****************************************************************/	
	public void restablecerEstilos() {
		//txtNumrecm.setStyleClass("frmInput2");
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
		//	hfac = (Finandet) ((List) m.get("lstSelectedCuotas")).get(0);
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
	public void limpiarVariablesSession() {
		CodeUtil.removeFromSessionMap(new String[] { "fn_NumeroBatchReciboJde", "lstDatosTrack_Con",
				"iNoFichaFinan", "lstPagoFichaFinan", "iNumRecFinan",
				"lstSelectedCuotas", "lstSelectedCuotas",
				"lstCreditosFinan", "lstPagosFinan","lstPagos" });
		txtConcepto.resetValue();
		ddlTipoReciboPago.resetValue();
		m.put("lstCuotasV2", new ArrayList());
		m.put("lstPagos", new ArrayList());
		
	}
	public void procesarRecibo(ActionEvent e) {
	
		CuentaInfo	cuota = null;
		Vf55ca012[] f55ca012 = null;
		Vf55ca012[] f55ca012v2 = null;
		MetodosPagoCtrl metCtrl = new MetodosPagoCtrl();
		MonedaCtrl monCtrl = new MonedaCtrl();
		String[] sCodMod = null;
		Vf55ca01 caja = null;
		double dTasaPar = 0.0;
		serviceRequest service = new serviceRequest();
		try {
			RowItem ri = (RowItem) e.getComponent().getParent().getParent();
			fechaRecibo=new Date();			
			cuota = (CuentaInfo) gvCuotas.getDataRow(ri);
			txtCodcli.setValue( cuota.getCodigoCliente().trim());
			txtNomcli.setValue(cuota.getNombreCuenta().trim());
			noPrestamo = cuota.getCodigoProducto().toString();
			strMensajeBusqueda = "";
			
			ConsultaPrestamo consulta =service.buscarFinanciamientosEspecifico(cuota.getCodigoCuenta().trim(), cuota.getClaseProducto().trim(), cuota.getCodigoMoneda().trim(), terminal);
			//&& ===== Validar si la caja no esta bloqueada.
			String sMensaje="";
			if(consulta.getPrestamo().getCodigoEstado() != null && consulta.getPrestamo().getCodigoEstado().equals("20"))
			sMensaje= "El estado del prestamo no permite procesar pago";
		
			if(!sMensaje.isEmpty()){
				throw new Exception(sMensaje); 				
			}
			this.limpiarVariablesSession();
			
			
			m.put("noPrestamoByte",cuota.getCodigoCuenta().trim());
			//Cargando las listas

			caja = (Vf55ca01) ((List) m.get("lstCajas")).get(0);
			List lstMoneda = new ArrayList();
			sCodMod = monCtrl.obtenerMonedasxMetodosPago_CajaV2(caja.getId().getCaid(),PropertiesSystem.COMPANIA_BYTE);
			for (int i = 0; i < sCodMod.length; i++) {
				lstMoneda.add(new SelectItem(sCodMod[i], sCodMod[i]));
			}
			m.put("lstMonedaFinan", lstMoneda);
			this.lstMoneda = lstMoneda;
			ddlMoneda.dataBind();
			
			f55ca012 = metCtrl.obtenerMetodoPagoxCaja_Moneda2(caja.getId()
					.getCaid(), "E12", ddlMoneda.getValue()
					.toString());
			List lstMetodosPago = new ArrayList();
			for (int i = 0; i < f55ca012.length; i++) {
				lstMetodosPago.add(new SelectItem(f55ca012[i].getId()
						.getCodigo(), f55ca012[i].getId().getMpago()));
			}
			m.put("lstMetodosPagoFinan", lstMetodosPago);
			this.lstMetodosPago = lstMetodosPago;
			ddlMetodosPago.dataBind();
			
			//&& =========== conservar todos los metodos de pago configurados.
			List<Vf55ca012> MetodosPagoConfigurados =  DonacionesCtrl.obtenerMpagosConfiguradosCaja(PropertiesSystem.COMPANIA_BYTE, caja.getId().getCaid());
			CodeUtil.putInSessionMap("fin_MetodosPagoConfigurados", MetodosPagoConfigurados );
			
			// cargar metodos de pago de compania de factura
			f55ca012v2 = metCtrl.obtenerMetodosPagoxCaja_Compania2(caja.getId().getCaid(), PropertiesSystem.COMPANIA_BYTE);
			
			CodeUtil.putInSessionMap("metpago", f55ca012v2);
			
	
	
			//service.obetenerListaCoutasFaltantes(consulta);
			CodeUtil.putInSessionMap("lstPrestaInformation",consulta);	
 
			lstSelectedCuotas = service.obetenerListaCoutasFaltantes(consulta);
			lstSelectedCuotasV2 = lstSelectedCuotas;
			m.put("lstSelectedCuotasV2",lstSelectedCuotas);
			CodeUtil.putInSessionMap("lstSelectedCuotasV2",lstSelectedCuotas);		
			CodeUtil.putInSessionMap("lstSelectedCuotas",lstSelectedCuotas);	
			gvSelectedCuotas.dataBind();
			Divisas divisas = new 	Divisas();	
			
			List<detalleCobroFactura> listaSuma = (List<detalleCobroFactura>) m.get("lstSelectedCuotasV2");
			if(listaSuma.size()>0) {
				double valorFaltante2= listaSuma.stream().map(z -> z.getSaldo()).reduce((double) 0.00,Double::sum);
				df.setRoundingMode(RoundingMode.UP);
				double valorFaltante=  listaSuma.stream().map(z -> z.getSaldo()).reduce((double) 0,Double::sum);
				montoTotalAplicarForaneo.setValue(divisas.formatDouble(valorFaltante));
				montoTotalFaltanteForaneo.setValue(divisas.formatDouble(valorFaltante));
				montoTotalFaltanteDomestico.setValue(divisas.formatDouble((valorFaltante)*this.obtenerTasaParalela("USD").doubleValue()));		
				txtMontoAplicar.setValue("0.00");
				txtMontoRecibido.setValue("0.00");
				//txtCambioForaneo.setValue("0.00");
				CodeUtil.putInSessionMap("montoPrendienteFV2",valorFaltante);
				CodeUtil.putInSessionMap("montoPendienteCalculadoV2",valorFaltante);
			}
			
			
			Tpararela[] tparalelaTmp =  TasaCambioCtrl.obtenerTasaCambioParalela();
			CodeUtil.putInSessionMap("tpcambio", tparalelaTmp);		
			
			
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
			
			
			lblMontoAplicar.setValue("A Aplicar USD:");
			txtMontoAplicar.setValue("0.00");
			lblMontoRecibido.setValue("Recibido USD:");
			txtMontoRecibido.setValue("0.00");
			lblCambio.setValue("Cambio :");		
			txtCambio.setValue("0.00");
			txtCambio.setStyle("font-size: 10pt; color: red");
			txtCambioForaneo.setStyleClass("frmInputHidden");
			lblCambioDomestico.setValue("");
			txtCambioDomestico.setValue("");
			lnkCambio.setStyle("display: none");			
			
			
			dwReciboV2.setWindowState("normal");
			
		}catch(Exception ex) {
			dwCargando.setWindowState("hidden");
			strMensajeBusqueda = ex.getMessage();
			lblMensajeError.setValue(ex.getMessage());			
			dwMensajeError.setWindowState("normal");
			
		}
	}
	
	
	public void procesarPagoRecibo(ActionEvent e) {
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

				dwProcesaRecibo.setWindowState("normal");
			
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
	public void determinarCambio(List lstPagos,double montoRecibido, String sMonedaBase) {
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
				if (!PropertiesSystem.MONEDA_BYTE.equals(sMonedaBase) && mpago.getMoneda().equals(sMonedaBase) && mpago.getMetodo().equals(MetodosPagoCtrl.EFECTIVO)) {
					montoCOR = montoCOR + mpago.getMonto();
					bCambioCOR = true;
				}
				if (!PropertiesSystem.MONEDA_BYTE.equals(sMonedaBase) && !mpago.getMoneda().equals(sMonedaBase) && mpago.getMetodo().equals(MetodosPagoCtrl.EFECTIVO)) {
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
				lblCambio.setValue("Cambio " + PropertiesSystem.MONEDA_BYTE + ":");
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
					
				if (!PropertiesSystem.MONEDA_BYTE.equals(sMonedaBase)) {
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
			} else if (!PropertiesSystem.MONEDA_BYTE.equals(sMonedaBase) && bCambioCOR) {// toma el total a aplicar y lo convierte a la tasa paralela a eso se le resta lo recibido a la misma tasa y ese es el cambio
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
				
			} else if (!PropertiesSystem.MONEDA_BYTE.equals(sMonedaBase)) {
				
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
			 
			boolean esAbonoPrincipal = chkPrincipal.isChecked() ;
			
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
					
				}else{
					f = cuotaCtrl.buscarSiguienteCuota(fHdr.getId().getCodcomp(), fHdr.getId().getCodsuc(), 
							fHdr.getId().getNosol(), fHdr.getId().getTiposol(), fHdr.getId().getCodcli(), lstFacturasSelected);
				}
 
				
				if( f == null ){
					lblMensajeError.setValue(" Ya no hay más cuotas disponibles " );
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
			ex.printStackTrace(); 
		}		
	}
	
		
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
			
			montoRecibido = calcularElMontoRecibido(lstPagos, 
					 sMonedaBase);
			distribuirMontoAplicar(sMonedaBase);
			determinarCambio(lstPagos,montoRecibido,sMonedaBase);
			
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

		String sMonedaBase = new String("USD");

		try{
		
			MetodosPago mPago = (MetodosPago) m.get("metodopagoborrar");	
			List lstFacturasSelected = (List) m.get("lstComponentes");

			
			
			// && ========== Borrar datos de la donacion.
	
			
		
			
	
			
			//&& ========== remover el pago de la lista de los registrados.	
			lstPagos = (ArrayList<MetodosPago>)m.get("lstPagosFinan");				
			com.casapellas.controles.tmp.MetodosPagoCtrl
									.removerPago(lstPagos, mPago);
			gvMetodosPago.dataBind();
			
			//====== calcular el monto recibido 
			double montoRecibido = calcularElMontoRecibido(lstPagos, 
					 sMonedaBase);

			// distribuir el monto recibido a los montos a aplicar en factura
			distribuirMontoAplicar(sMonedaBase);

			// determinar cambio
			determinarCambio(lstPagos, montoRecibido, sMonedaBase);
			
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
		//List<Credhdr> lstFacturasSelected = null;
		try{
			if(pagoFin == null)
				pagoFin = new PagoFinanciamiento();
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
			//lstFacturasSelected = (List<Credhdr>)	m.get("lstComponentes");
			//Credhdr hFac = (Credhdr) lstFacturasSelected.get(0);
			codcomp = PropertiesSystem.COMPANIA_BYTE ;
			
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
				//for (Credhdr chFacts : lstFacturasSelected) 
				
				Divisas divisas = new Divisas();
					bdMtoAplicar = BigDecimal.valueOf((double)m.get("montoPrendienteFV2"));					
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
		/*	valido = validarMpagos(metodo, strMonto ,
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
			}*/
			
			
			
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
			if (PropertiesSystem.MONEDA_BYTE.equals(sMonedaBase)) {// Domestic
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
			montoRecibido = calcularElMontoRecibido(lstPagos,   sMonedaBase);
			
			//distribuir el monto a aplicar
			distribuirMontoAplicar(sMonedaBase);
			
			//determinar cambio
			determinarCambio(lstPagos, montoRecibido,sMonedaBase);
			
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
	public double calcularElMontoRecibido(List selectedMet, String sMonedaBase){
		MetodosPago metMontoRec = null;
		Divisas divisas = new Divisas();
		double montoRecibido = 0.0,cambio = 0.0, dMontoAplicar = 0.0;
		BigDecimal dMontoAplFacturas = BigDecimal.ZERO;
		try{
						
				dMontoAplFacturas = dMontoAplFacturas.add(BigDecimal.valueOf((double)m.get("montoPrendienteFV2")));				
			
				for(int z = 0; z < selectedMet.size();z++){
				metMontoRec = (MetodosPago)selectedMet.get(z);
				if(PropertiesSystem.MONEDA_BYTE.equals(sMonedaBase)){//domestica
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
			//List lstFacturasSelected = (List) m.get("lstComponentes");
			List<detalleCobroFactura> lstFacturasSelected=	(List<detalleCobroFactura>) m.get("lstSelectedCuotasV2");
			bIntAplicado = new boolean[lstFacturasSelected.size()];
			dMontoRecibido = divisas.formatStringToDouble(txtMontoAplicar.getValue().toString());
			
			
		for(detalleCobroFactura item:lstFacturasSelected) {
			
			
			double saldo = item.getSaldo();			
			if(dMontoRecibido>0 && saldo>0) {
				if(dMontoRecibido > saldo) {
					item.setAplicado(saldo);
					dMontoRecibido = dMontoRecibido-saldo;
				}else {
					item.setAplicado(dMontoRecibido);
					dMontoRecibido = 0;
				}
			}else if(dMontoRecibido == 0) {
				item.setAplicado(0);
			}
		}							

			m.put("lstSelectedCuotasV2", lstFacturasSelected);
			gvSelectedCuotas.dataBind();
			//calcular resumen de totales
			double montoPendiente=(double) m.get("montoPrendienteFV2");
			dMontoAplicar =lstFacturasSelected.stream().map(z -> z.getAplicado()).reduce((double) 0,Double::sum);
			
			CodeUtil.putInSessionMap("montoPendienteCalculadoV2", montoPendiente - dMontoAplicar);
			montoTotalFaltanteForaneo.setValue(divisas.formatDouble(montoPendiente - dMontoAplicar));
			montoTotalFaltanteDomestico.setValue(divisas.formatDouble((montoPendiente - dMontoAplicar)*this.obtenerTasaParalela("USD").doubleValue()));		
			
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
				
				if(polCtrl.validarReferenciaBancaria(Integer.parseInt(sCajaId), ddlBanco.getValue().toString().split("@")[1], ref2,"T"))
				{
					y = y + 7;
					txtReferencia2.setStyleClass("frmInput2Error");
					sMensajeError = "La referencia ya ha sido utilizada, favor verificar e ingresar una referencia válida";
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

				if(polCtrl.validarReferenciaBancaria(Integer.parseInt(sCajaId), ddlBanco.getValue().toString().split("@")[1], ref2,"N"))
				{
					y = y + 7;
					txtReferencia2.setStyleClass("frmInput2Error");
					sMensajeError = "La referencia ya ha sido utilizada, favor verificar e ingresar una referencia válida";
					return validado = false;			
				}
				
				if (!ref2.matches(PropertiesSystem.REGEXP_8DIGTS)) {
					y = y + 7;
					txtReferencia2.setStyleClass("frmInput2Error");
					sMensajeError = "El valor de la referencia debe contener entre 1 y 8 dígitos unicamente ";
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
		CuentaInfo f1 = null, f2 = null;
		List lstSelected = new ArrayList();
		boolean bValido = true;
		RowItem row = null;
		try{
			SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
			lstSelected = gvCuotas.getSelectedRows();						
			
			if(lstSelected.size()>1){//hay validaciones
				row = (RowItem) lstSelected.get(0);
				f1 = (CuentaInfo) gvCuotas.getDataRow(row);
				for(int i = 1; i < lstSelected.size();i++){
					row = (RowItem) lstSelected.get(i);
					f2 = (CuentaInfo) gvCuotas.getDataRow(row);
					/*if(f1.getId().getCodcli() != f2.getId().getCodcli()){
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
					}*/
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
				
		boolean committ = true;
		boolean nuevaConexion = true;
		
		String msgProceso = "";
		
		try{
			
			LogCajaService.CreateLog("FinanciamientoV2DAO.mostrarRecibo - INICIO", "JR8", "Inicio ejecucion Metodo");
			
			F55ca014[] f14 = (F55ca014[])m.get("cont_f55ca014");
			
			ConsolidadoDepositosBcoCtrl.getCurrentSession();
			nuevaConexion = ConsolidadoDepositosBcoCtrl.isNewSession;
			
			CodeUtil.removeFromSessionMap(new String[] { "finan_MontoAplicar",
					"Fn_CodUninegRecibo", "fn_NumeroUltimaCuotaCorriente" });
			
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
			
			//-----------------------
			CodeUtil.putInSessionMap("lstCreditosFinan",lstSelected);
			hFac = lstSelected.get(0);
			codcomp = hFac.getId().getCodcomp() ;
			
			//&& ========== Configuracion de la compania a utilizar  
			F55ca014 dtComp = com.casapellas.controles.tmp.CompaniaCtrl.filtrarCompania( f14, codcomp);
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
			for (String moneda : sCodMod) {
				lstMoneda.add(new SelectItem(moneda, moneda));
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
			
			String[] dtatasacte = EmpleadoCtrl.validarClienteTasaEspecial(hFac.getId().getCodcli(), "FN");
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
					
					fd.setMora(BigDecimal.ZERO);
					fd.getId().setDiasmora(0);
					fd.setMontopend(fd.getId().getMontopend());
					fd.setMontoAplicar(BigDecimal.ZERO);
					
					bdInteres = cuotaCtrl.buscarInteresCorrientePend(fd);
					
					if(bdInteres.compareTo(BigDecimal.ZERO ) == -1 ){
						msgProceso = "Error al obtener los datos de intereses corriente, por favor intente nuevamente";
						return;
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
//			String msgCreaInteres = crearFacturasPorIntereses( lstSelected,  lstFacturasSelected, sMonedaBase, tasaoficial, vaut.getId().getLogin(), sMonedaBase );
			
			
			
			//Realizamos el commit de la trasacciones realizadas
			
			ConsolidadoDepositosBcoCtrl.isNewSession = nuevaConexion;
			ConsolidadoDepositosBcoCtrl.closeCurrentSession( committ );
			
			
			//Abrimos nuevamente las conexiones para consultar los datos a mostrar
			
			ConsolidadoDepositosBcoCtrl.getCurrentSession();
			nuevaConexion = ConsolidadoDepositosBcoCtrl.isNewSession;
			
			
		
			
	
			
		
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
			
			dwReciboV2.setWindowState("normal");
			dwCargando.setWindowState("hidden");
			
			//Realizamos el commit y cierre de transaccion para  las consultas
			
			ConsolidadoDepositosBcoCtrl.isNewSession = nuevaConexion;
			ConsolidadoDepositosBcoCtrl.closeCurrentSession( committ );

			LogCajaService.CreateLog("FinanciamientoV2DAO.mostrarRecibo - FIN", "JR8", "Fin ejecucion Metodo");
		}catch(Exception ex){
			ex.printStackTrace(); 
		}finally{
							
			if( !msgProceso.isEmpty()){
				lblMensajeError.setValue(msgProceso);
				dwMensajeError.setWindowState("normal");
				dwMensajeError.setStyle("width:390px;height:145px");
				CodeUtil.refreshIgObjects(dwMensajeError);
			}
			
			
		}
	}
	public void cerrarRecibo(ActionEvent ev){
		dwReciboV2.setWindowState("hidden");
	}
/***********************************************************************************************************************************/	
	public void setSelectedCuotas(SelectedRowsChangeEvent e) {
	
	}
	public void cerrarMensajeError(ActionEvent e){
		dwMensajeError.setWindowState("hidden");
	}
	/**************************************************************************************/
/**************DETALLE DE SOLICITUD***************************************************************************/	

/************************************************************************************/
	public void cargarDetalleCuota(CuentaInfo cuota, boolean bEnDetalle){			
		
		BigDecimal dMora = BigDecimal.ZERO;
		
		
		String sMonedaBase = "";
	

		
		try{
			
			serviceRequest service = new serviceRequest();
			
			ConsultaPrestamo consulta =service.buscarFinanciamientosEspecifico(cuota.getCodigoCuenta().trim(), cuota.getClaseProducto().trim(), cuota.getCodigoMoneda().trim(), terminal);
			 if(consulta != null && consulta.getPrestamo() != null) {
				 
				 Prestamo prestamo = consulta.getPrestamo();
				 prestamoCabecera = new ArrayList<Prestamo>(); 
						 
				 prestamoCabecera.add(consulta.getPrestamo());
				 
					fechaDet = prestamo.getFechaApertura();
					codigoMoneda= prestamo.getCodigoMoneda();
					codcliDet = prestamo.getCodigoCliente() + " (Código)";
					nomcliDet = prestamo.getNombreCliente() + " (Nombre)";
					
					noSolDet = prestamo.getNumeroPrestamo() + "";
					tipoSolDet = prestamo.getTipoProducto();
					
					m.put("prestamoCabecera",prestamoCabecera);
					
					//poner resumen de pago
					txtPrincipalDet = prestamo.getMontoCapital();
					txtInteresDet = prestamo.getMontoInteres();
					txtImpuestoDet = (double) 0;
					txtTotalDet = prestamo.getMonto();
					txtOtrosPagosDet = prestamo.getMontoCobros();
					txtTotalMoraDet = prestamo.getMontoMora();
					txtPendienteDet = prestamo.getMontoPendiente();
					
					
					
					
					gvProductos.dataBind();
				 if(consulta.getPrestamo().getCuotas()!=null &&consulta.getPrestamo().getCuotas().size()>0) {
					  prestamoCabeceraDetalle =prestamo.getCuotas();
					  m.put("prestamoCabeceraDetalle",prestamoCabeceraDetalle);
						gvDetallesol.dataBind();	
				 }
			 }
			
	
				String dimensiones = "height: 550px; width: 650px";
				dwDetalleSolicitud.setStyle(dimensiones);

			dwDetalleSolicitud.setWindowState("normal");
			
		}catch(Exception ex){
			 ex.printStackTrace();
		}finally{
	
		}
	}
	/*******************************************************************************/
	public void mostrarDetalleSolicitud(ActionEvent e){
		CuentaInfo	cuota = null;
		try{
			RowItem ri = (RowItem) e.getComponent().getParent().getParent();
	
				cuota = (CuentaInfo) gvCuotas.getDataRow(ri);
			CodeUtil.putInSessionMap("cuotaV2", cuota);
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
		List lstFacturasSelected = null;
		MonedaCtrl monCtrl = new MonedaCtrl();
		String[] sCodMod = null;
		Vf55ca01 f55ca01 = null;
		try{
			f55ca01 = (Vf55ca01) ((List) m.get("lstCajas")).get(0);
			lstFacturasSelected = (List) m.get("lstSelectedCuotas");			
			// cargar monedas de metodo de pago y compania de factura
			List lstMoneda = new ArrayList();
			sCodMod = monCtrl.obtenerMonedasxMetodosPago_Caja(f55ca01.getId().getCaid(),PropertiesSystem.COMPANIA_BYTE, ddlMetodosPago.getValue().toString());
			for (int i = 0; i < sCodMod.length; i++) {
				lstMoneda.add(new SelectItem(sCodMod[i], sCodMod[i]));
			}
			m.put("lstMonedaFinan", lstMoneda);
			ddlMoneda.dataBind();
			cambiarVistaMetodos(ddlMetodosPago.getValue().toString(),f55ca01);
		}catch(Exception ex){
			ex.printStackTrace();
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
	public void onTipoReciboPagoChange(ValueChangeEvent ev){
		String sTipoRecibo = "";
		TasaCambioCtrl tcCtrl = new TasaCambioCtrl();
		Tcambio[] tcambio = null;
		serviceRequest service = new serviceRequest();
		try{
			sTipoRecibo = ddlTipoReciboPago.getValue().toString();
			ConsultaPrestamo consulta= (ConsultaPrestamo) m.get("lstPrestaInformation");
			
			m.put("lstPagosFinan", new ArrayList());
			switch(sTipoRecibo) {
			case "C":				
				lstSelectedCuotas = service.obetenerListaCoutasFaltantes(consulta);
				lstSelectedCuotasV2 = lstSelectedCuotas;
				break;
			case "A":				
				lstSelectedCuotas = service.obetenerListaAbonoCapital(consulta);
				lstSelectedCuotasV2 = lstSelectedCuotas;
				break;
			case "T":				
				lstSelectedCuotas = service.obetenerListaAbonoTotal(consulta);
				lstSelectedCuotasV2 = lstSelectedCuotas;
				break;
			}
						
				
			CodeUtil.putInSessionMap("lstSelectedCuotas",lstSelectedCuotas);	
			CodeUtil.putInSessionMap("lstSelectedCuotasV2",lstSelectedCuotas);	
			gvSelectedCuotas.dataBind();
			gvMetodosPago.dataBind();
			Divisas divisas = new 	Divisas();	
			
			List<detalleCobroFactura> listaSuma = (List<detalleCobroFactura>) m.get("lstSelectedCuotasV2");
			if(listaSuma.size()>0) {
				 df.setRoundingMode(RoundingMode.UP);
				double valorFaltante= listaSuma.stream().map(z -> z.getSaldo()).reduce((double) 0,Double::sum);
				
				montoTotalAplicarForaneo.setValue(divisas.formatDouble(valorFaltante));
				montoTotalFaltanteForaneo.setValue(divisas.formatDouble(valorFaltante));
				montoTotalFaltanteDomestico.setValue(divisas.formatDouble((valorFaltante)*this.obtenerTasaParalela("USD").doubleValue()));		
				txtMontoAplicar.setValue("0.00");
				txtMontoRecibido.setValue("0.00");
				//txtCambioForaneo.setValue("0.00");
				CodeUtil.putInSessionMap("montoPrendienteFV2",valorFaltante);
				CodeUtil.putInSessionMap("montoPendienteCalculadoV2",valorFaltante);
			}
			//gvSelectedCuotas
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	
/*******************BUSQUEDA DE SOLICITUDES*****************************************/
		public void buscar(){
		
		String iTipo ="";
		String sParametro = "",sCodcomp,sMoneda;
		strMensajeBusqueda = "";
		
		try{
			List<Finanhdr> lista;
			iTipo = hddTiposBusqueda.getValue().toString().trim();
			sParametro = txtParametro.getValue().toString().trim();
		
			serviceRequest servicio = new serviceRequest();
			
			ConsultaCuenta consulta =servicio.buscarFinanciamientos(iTipo, sParametro, "");
			
			if( consulta == null ){
				strMensajeBusqueda = "Error al procesar request!!!";
			}else if(consulta.getMensaje()=="IERROR") {
				strMensajeBusqueda = consulta.getMensaje();
			}else if(consulta.getCuenta() == null || consulta.getCuenta().size() ==0) {
				strMensajeBusqueda = consulta.getMensaje();
			}else {
				lstCuotasV2 = servicio.formatearFinanciamiento(consulta);
				consultaInfoPrestamo= consulta.getCuenta();
			}
		
			
			m.put("consultaInfoPrestamo", consultaInfoPrestamo);
			gvCuotas.dataBind();
			dwCargando.setWindowState("hidden");
			
		}catch(Exception ex){
			ex.printStackTrace();
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

	public List getLstCuotasV2() {
		if(m.get("lstCuotasV2") == null){
			//CuotaCtrl cuotaCtrl = new CuotaCtrl();
			//lstCuotasV2 = new ArrayList();
			//lstCuotas = cuotaCtrl.getAllCuotas();
			//m.put("lstCuotasV2", lstCuotasV2);
		}else {
			lstCuotasV2 = (List)m.get("lstCuotasV2");
		}
		return lstCuotasV2;
	}

	public List getConsultaInfoPrestamo() {
			if(txtParametro.getValue().toString().length()>0)
			this.consultaInfoPrestamo = (List)m.get("consultaInfoPrestamo");
		
		return this.consultaInfoPrestamo;
	}

	public void setConsultaInfoPrestamo(List consultaInfoPrestamo) {
		this.consultaInfoPrestamo = consultaInfoPrestamo;
	}

	public List getPrestamoCabecera() {
		prestamoCabecera = (List)m.get("prestamoCabecera");
		return prestamoCabecera;
	}

	public void setPrestamoCabecera(List prestamoCabecera) {
		this.prestamoCabecera = prestamoCabecera;
	}

	public List getPrestamoCabeceraDetalle() {
		prestamoCabeceraDetalle = (List)m.get("prestamoCabeceraDetalle");
		return prestamoCabeceraDetalle;
	}

	public void setPrestamoCabeceraDetalle(List prestamoCabeceraDetalle) {
		this.prestamoCabeceraDetalle = prestamoCabeceraDetalle;
	}

	public void setLstCuotasV2(List lstCuotas) {
		this.lstCuotasV2 = lstCuotas;
	}

	public List getLstTiposBusqueda() {
		FinanciamientoCtrl financiamiento= new FinanciamientoCtrl(); 
		lstTiposBusqueda = new ArrayList();
		try {
			Object[] lista = financiamiento.obtenerListaBusqueda();
			
			for(int i=0; i<lista.length;i++) {
				Object[] x =   (Object[]) lista[i];
				
				//String[] o = (String[]) x;
				String va =x[0].toString();
				String concep= x[1].toString();
				String aa="Búsqueda por "+x[1].toString();
				lstTiposBusqueda.add(new SelectItem(va,concep,aa));
		
			}
			
			
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		//List<CatalogoGenerico> tipoBusqueda = financiamiento.obtenerListaBusqueda();

	
		return lstTiposBusqueda;
	}

	public void setLstTiposBusqueda(List lstTiposBusqueda) {
		this.lstTiposBusqueda = lstTiposBusqueda;
	}

	/*public List getLstFiltroMonedas() {
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
	}*/

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
	/*public HtmlDropDownList getHddFiltroCompania() {
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
	}*/
	public String getFechaDet() {
		return fechaDet;
	}
	public void setFechaDet(String fechaDet) {
		this.fechaDet = fechaDet;
	}
	
	public String getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}
	
	public String getCodigoMoneda() {
		return codigoMoneda;
	}
	public void setCodigoMoneda(String codigoMoneda) {
		this.codigoMoneda = codigoMoneda;
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
		if(m.get("lstSelectedCuotas")==null){
			lstSelectedCuotas = new ArrayList();
			m.put("lstSelectedCuotas",lstSelectedCuotas);
		}else{
			lstSelectedCuotas = (List)m.get("lstSelectedCuotas");
		}
		return lstSelectedCuotas;
	}

	public void setLstSelectedCuotas(List lstSelectedCuotas) {
		this.lstSelectedCuotas = lstSelectedCuotas;
	}
	
	public List getLstSelectedCuotasV2() {
		if(m.get("lstSelectedCuotasV2")==null){
			lstSelectedCuotasV2 = new ArrayList();
			m.put("lstSelectedCuotasV2",lstSelectedCuotasV2);
		}else{
			lstSelectedCuotasV2 = (List)m.get("lstSelectedCuotasV2");
		}
		return lstSelectedCuotasV2;
	}

	public void setLstSelectedCuotasV2(List lstSelectedCuotas) {
		this.lstSelectedCuotasV2 = lstSelectedCuotasV2;
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

	public Double getTxtPrincipalDet() {
		return txtPrincipalDet;
	}

	public void setTxtPrincipalDet(Double txtPrincipalDet) {
		this.txtPrincipalDet = txtPrincipalDet;
	}

	public Double getTxtInteresDet() {
		return txtInteresDet;
	}

	public void setTxtInteresDet(Double txtInteresDet) {
		this.txtInteresDet = txtInteresDet;
	}

	public Double getTxtImpuestoDet() {
		return txtImpuestoDet;
	}

	public void setTxtImpuestoDet(Double txtImpuestoDet) {
		this.txtImpuestoDet = txtImpuestoDet;
	}

	public Double getTxtTotalDet() {
		return txtTotalDet;
	}

	public void setTxtTotalDet(Double txtTotalDet) {
		this.txtTotalDet = txtTotalDet;
	}

	public Double getTxtPendienteDet() {
		return txtPendienteDet;
	}

	public void setTxtPendienteDet(Double txtPendienteDet) {
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
	public Double getTxtTotalMoraDet() {
		return txtTotalMoraDet;
	}
	public void setTxtTotalMoraDet(Double txtTotalMoraDet) {
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
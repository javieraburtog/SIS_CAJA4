package com.casapellas.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import javax.faces.application.NavigationHandler;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlInputTextarea;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.casapellas.controles.CompaniaCtrl;
import com.casapellas.controles.ConsolidadoDepositosBcoCtrl;
import com.casapellas.controles.CtrlCajas;
import com.casapellas.controles.EmpleadoCtrl;
import com.casapellas.controles.FacturaCrtl;
import com.casapellas.controles.MetodosPagoCtrl;
import com.casapellas.controles.ReciboCtrl;
import com.casapellas.controles.SalidasCtrl;
import com.casapellas.controles.SolechequeCtrl;
import com.casapellas.entidades.Aplicacion;
import com.casapellas.entidades.F55ca01;
import com.casapellas.entidades.F55ca014;
import com.casapellas.entidades.Hfactura;
import com.casapellas.entidades.Recibodet;
import com.casapellas.entidades.RecibodetId;
import com.casapellas.entidades.Recibofac;
import com.casapellas.entidades.Recibojde;
import com.casapellas.entidades.Valorcatalogo;
import com.casapellas.entidades.ens.Vautoriz;
import com.casapellas.entidades.Vf0101;
import com.casapellas.entidades.Vf55ca01;
import com.casapellas.entidades.Vsolecheque;
import com.casapellas.entidades.VsolechequeId;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.jde.creditos.CodigosJDE1;
import com.casapellas.jde.creditos.DatosComprobanteF0911;
import com.casapellas.jde.creditos.ProcesarEntradaDeDiario;
import com.casapellas.navegacion.As400Connection;
import com.casapellas.reportes.Rptmcaja008;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.Divisas;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.NumeroEnLetras;
import com.casapellas.util.PropertiesSystem;
import com.infragistics.faces.grid.component.RowItem;
import com.infragistics.faces.grid.component.html.HtmlGridView;
import com.infragistics.faces.input.component.html.HtmlDateChooser;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
import com.infragistics.faces.shared.component.DataRepeater;
import com.infragistics.faces.shared.smartrefresh.SmartRefreshManager;
import com.infragistics.faces.window.component.html.HtmlDialogWindow;

/**
 * CASA PELLAS S.A.
 * Creado por.........: Carlos Manuel Hernández Morrison
 * Fecha de Creación..: 15/04/2010
 * Última modificación: 24/02/2011
 * Modificado por.....:	Juan Carlos Ñamendi Pinbeda.
 * Manejo de moneda base
 */
public class RevisionSolechequeDAO {
	Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	private HtmlInputText txtParametro;
	private List lstFiltroCompania,lstFiltroMoneda,lstFiltroEstado,lstSolicitudesCheques;
	private List<SelectItem> lstTipoBusquedaCliente;
	private List<SelectItem> lstTipoSolicitudes;
	private HtmlDropDownList ddlTipoSolicitudes;
	private HtmlDropDownList ddlTipoBusqueda, ddlFiltroCompanias, ddlFiltroMonedas, ddlFiltroEstado;
	private HtmlGridView gvSolicitudesCheques;
	private HtmlDateChooser dcFechaFinal,dcFechaInicio;
	private Date fechaactual1,fechaactual2;
	
	//-------- Objetos para el detalle de la solicitud.
	private HtmlDialogWindow dwDetalleSolicitud;
	private HtmlGridView gvDetalleDevolucion,gvDetalleFactura;
	private List lstDetalleDevolucion,lstDetalleFactura;
	private HtmlOutputText txtFechaFactura,txtNofactura,txtCodigoCliente,txtCliente,lblMonedaContado1;
	private HtmlOutputText txtCodUnineg, txtUnineg, lblVendedorCont, txtVendedorCont, txtSubtotal,txtIva,txtTotal;
	private HtmlOutputText lblfacFecha, lblfacNofac, lblFacCodcli, lblFacNomcli, lblFacCodUnineg,lblFacUnineg;
	private HtmlOutputText lblFacVendedor0, lblFacVendedor1, txtFacSubtotalDetalle,lblFacTotalDet,lblFacIva;
	private HtmlOutputText lblFacMoneda,lblNoReciboFactura;
	
	//------ Ventanas Emergentes.
	private HtmlDialogWindow dwConfirmaAprobarSchk,	dwValidacion,dwConfirmarRechazar;
	private HtmlOutputText lblMsgValidaSolecheque;
	private HtmlInputTextarea txtMotivoRechazo;
	
	private HtmlDialogWindow dwDatosCartaxDevolucion,dwConfirmaAprobarSCarta,dwMensajeAprSolicitud;
	private HtmlInputText txtDestinoCarta,txtOrigenCarta,txtCartaDigitosTarjeta,txtCartaCodAutoriz;
	private HtmlOutputText lblMsgAprSolicitud;
	
	
	public void cerrarCartaReversion(ActionEvent ev) {
		try {
			dwMensajeAprSolicitud.setWindowState("hidden");
			m.remove("rsc_solicitudSeleccionada");
			m.remove("rsc_DestinoCarta");
			m.remove("rsc_OrigenCarta");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
/******************************************************************************************/
/** Método: Generar el reporte y navegar hacia la página que lo contiene.
 *	Fecha:  09/07/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	//** no usado 30/01/2014
	public void generarReporteCartaAnulacion(ActionEvent ev){
		boolean bValido = true;
		
		Vsolecheque v = new Vsolecheque();
		VsolechequeId vId = new VsolechequeId();
		String  sNombreCliente="", sFecha="";
		
		String sNombreDestino="",sNombreOrigen="";
 
		Divisas dv = new Divisas();
		
		try {
			dwMensajeAprSolicitud.setWindowState("hidden");
			if(m.get("rsc_solicitudSeleccionada")!=null){
				
				v = (Vsolecheque)m.get("rsc_solicitudSeleccionada");
				vId =v.getId();
				sNombreDestino = m.get("rsc_DestinoCarta").toString();
				sNombreOrigen  = m.get("rsc_OrigenCarta").toString();	
				sNombreDestino = Divisas.ponerCadenaenMayuscula(sNombreDestino);
				sNombreOrigen  = Divisas.ponerCadenaenMayuscula(sNombreOrigen);
				sNombreCliente = Divisas.ponerCadenaenMayuscula(vId.getCliente().trim());
				sFecha = "Managua, "+FechasUtil.formatDatetoString(new Date(), "dd 'de' MMMM 'del' yyyy");
				
				//------ Llenar los datos a usar en el reporte.
				Rptmcaja008 r08 = new Rptmcaja008();
				r08.setSFechaSolicitud(sFecha);
				r08.setSContador(sNombreDestino);
				r08.setSSupervisor(sNombreOrigen);
				r08.setSConcepto(vId.getCodautoriz().trim());
				r08.setSFechadev(FechasUtil.formatDatetoString(vId.getFecha(), "dd/MM/yyy"));
				r08.setSnombrecliente(sNombreCliente);
				r08.setSTipofac("******"+vId.getNotarjeta().trim());
				r08.setCodsuc(vId.getIdafiliado().trim());
				r08.setINofactura(vId.getNofactoriginal());
				r08.setSFechafac(FechasUtil.formatDatetoString(vId.getFechafac(), "dd/MM/yyy"));
				
				//--------- Convertir un Número double a letras..
				int iParteEntera=0,iParteDecimal = 0;
				String sNumero[];
				sNumero = vId.getMonto().toString().split("\\.");
				if(sNumero!=null && sNumero.length>0){
					iParteEntera  = Integer.parseInt(sNumero[0]);
					if(sNumero.length>1)
						iParteDecimal = Integer.parseInt(sNumero[1]);
				}
				NumeroEnLetras nl = new NumeroEnLetras();
				String sNumeroLetras = "",sMonto="";
				sNumeroLetras = nl.convertirLetras(iParteEntera);
				sNumeroLetras = Divisas.ponerCadenaenMayuscula(sNumeroLetras);
				
				String sMoneda = "Córdobas";
				String sPrefix = "NIO";
				if(vId.getMoneda().trim().equals("USD")){
					sMoneda = "";
					sPrefix = "USD";
				}

				sMonto = sPrefix +" "+ dv.formatDouble(vId.getMonto().doubleValue());
				sMonto += " " +sNumeroLetras +" "+ sMoneda+ " con "+iParteDecimal+"/100";
				r08.setCodcomp(Divisas.ponerCadenaenMayuscula(sMonto));
				
				//----- Cargar los datos para el reporte.
				List<Rptmcaja008> lstR09 = new ArrayList<Rptmcaja008>(1);
				lstR09.add(r08);
				m.put("rptmcaja009_bd", lstR09);
				
				if(bValido){
					//------- Navegar hacia la página que contiene el reporte.
					FacesContext fc = FacesContext.getCurrentInstance();
					NavigationHandler nh = fc.getApplication().getNavigationHandler();
					nh.handleNavigation(fc, null, "rptmcaja009");
				}
			} 
		} catch (Exception error) {
			error.printStackTrace(); 
		}
	}
	
/******************************************************************************************/
/** Método: Generar batch y asientos de diario para la anulación de tarjeta de crédito.
 *	Fecha:  10/07/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 */	
	public boolean generarAsientosAnulacionTarjeta(Vsolecheque v, Vautoriz vaut, Session session, Transaction transaction,  String sMonedaBase){	
		Date dtFecha = new Date();
		boolean bHecho = true;
		String sMensaje="";
		String sCtaMpago[],sCtaVenta[], sMoneda, sConcepto;
		VsolechequeId vId = new VsolechequeId();
		Divisas dv = new Divisas();
		
		try {
			
			vId = v.getId();
			sConcepto = "Rever.Pago TC: C:"+vId.getCaid()+" Co:"+vId.getCodcomp().trim();
			sMoneda = vId.getMoneda().trim();
			dtFecha = vId.getFecha();
			
			sCtaMpago = dv.obtenerCuentaCaja( vId.getCaid(), vId.getCodcomp(), vId.getMpago(), sMoneda, session, transaction, null, null);
			sCtaVenta = dv.obtenerCuentaVenta(vId.getCaid(), vId.getCodcomp(), vId.getCodunineg(), session, transaction);
			
			BigDecimal montoComprobante =  vId.getMonto() ;
			BigDecimal tasaCambio = (  sMoneda.compareTo(sMonedaBase) == 0 )? BigDecimal.ZERO: vId.getTasacambio(); 
			
			List<DatosComprobanteF0911> lineasComprobante = new ArrayList<DatosComprobanteF0911>();
			DatosComprobanteF0911 dtaCtaBeneficiario = new DatosComprobanteF0911(
					sCtaVenta[1],
					montoComprobante, 
					sConcepto,
					tasaCambio,
					"",
					"",
					sCtaVenta[2] );
			 lineasComprobante.add(dtaCtaBeneficiario);
			
			 DatosComprobanteF0911 dtaCtaFormaPago = new DatosComprobanteF0911(
					 sCtaMpago[1],
					 montoComprobante.negate(), 
					 sConcepto,
					 tasaCambio,
					 "",
					 "",
					sCtaVenta[2] );
			 lineasComprobante.add(dtaCtaFormaPago);
	
			int iNumeroBatch = Divisas.numeroSiguienteJdeE1(    );
			int iNumeroDocumento = Divisas.numeroSiguienteJdeE1(CodigosJDE1.NUMERO_DOC_CONTAB_GENERAL );
			 
			 new ProcesarEntradaDeDiario();
			 ProcesarEntradaDeDiario.monedaComprobante = sMoneda;
			 ProcesarEntradaDeDiario.monedaLocal = sMonedaBase;
			 ProcesarEntradaDeDiario.fecharecibo = dtFecha;
			 ProcesarEntradaDeDiario.tasaCambioParalela = tasaCambio; 
			 ProcesarEntradaDeDiario.tasaCambioOficial = tasaCambio; 
			 ProcesarEntradaDeDiario.montoComprobante = montoComprobante;
			 ProcesarEntradaDeDiario.tipoDocumento = CodigosJDE1.BATCH_CONTADO.codigo();
			 ProcesarEntradaDeDiario.conceptoComprobante = sConcepto ;
			 ProcesarEntradaDeDiario.numeroBatchJde = String.valueOf( iNumeroBatch );
			 ProcesarEntradaDeDiario.numeroReciboJde = String.valueOf( iNumeroDocumento ) ;
			 ProcesarEntradaDeDiario.usuario = vaut.getId().getLogin();
			 ProcesarEntradaDeDiario.codigousuario = vaut.getId().getCodreg();
			 ProcesarEntradaDeDiario.programaActualiza = PropertiesSystem.CONTEXT_NAME;
			 ProcesarEntradaDeDiario.lineasComprobante = lineasComprobante;
			 ProcesarEntradaDeDiario.tipodebatch = CodigosJDE1.RECIBOCONTADO; 
			 ProcesarEntradaDeDiario.estadobatch = CodigosJDE1.BATCH_ESTADO_PENDIENTE ;
			 ProcesarEntradaDeDiario.procesarEntradaDeDiario(session);
			 
			 sMensaje = ProcesarEntradaDeDiario.msgProceso;	
			 bHecho = sMensaje.isEmpty();	
			
			 if(!bHecho){
				 CodeUtil.putInSessionMap("rsc_MsgErrorAsientoCarta", sMensaje) ;
				 return false;
			 }
			 
		} catch (Exception error) {
			LogCajaService.CreateLog("generarAsientosAnulacionTarjeta", "ERR", error.getMessage());
			bHecho = false;
			error.printStackTrace(); 
			CodeUtil.putInSessionMap("rsc_MsgErrorAsientoCarta", "No se ha podido procesar Batch por carta de reversion a Credomatic ") ;
		} 	
		
		return bHecho;
	}
/******************************************************************************************/
/** Método: Confirmar aprobar la emisión de carta de anulación por devolución de contado
 *	Fecha:  09/07/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	@SuppressWarnings("unchecked")
	public void realizarEmisionCartaxDevolucion(ActionEvent ev){
		
		String sNombreDestino = "",sNombreOrigen = "",sAutoriz="",sNotarjeta="";
		Divisas dv = new  Divisas();
		boolean bValido=true;
		String sMensaje = "";
		SolechequeCtrl sc = new SolechequeCtrl();
		Vautoriz vaut = null;
		
		
		String sMonedaBase = "";
		
		Session session = null;
		Transaction transaction = null;
		
		
		try {
			LogCajaService.CreateLog("realizarEmisionCartaxDevolucion", "INFO", "realizarEmisionCartaxDevolucion-INICIO");
			
			CodeUtil.removeFromSessionMap( "rsc_CuentasCorreo" );
			
			txtDestinoCarta.setStyleClass("frmInput2");
			txtOrigenCarta.setStyleClass("frmInput2");
			txtCartaDigitosTarjeta.setStyleClass("frmInput2");
			txtCartaCodAutoriz.setStyleClass("frmInput2");

			//---- Validar los datos ingresados para la carta.
			sNombreDestino = txtDestinoCarta.getValue().toString().trim();
			sNombreOrigen  = txtOrigenCarta.getValue().toString().trim();
			sNotarjeta     = txtCartaDigitosTarjeta.getValue().toString().trim();
			sAutoriz	   = txtCartaCodAutoriz.getValue().toString().trim();
			
			if(!dv.validarCadenaAlfaNumérica(sNombreDestino) && sNombreDestino.length()>150 ){
				txtDestinoCarta.setStyleClass("frmInput2Error");
				bValido = false;
			}
			if(!dv.validarCadenaAlfaNumérica(sNombreOrigen) && sNombreOrigen.length()>150){
				txtOrigenCarta.setStyleClass("frmInput2Error");
				bValido = false;
			}
			if(!sNotarjeta.matches("^[0-9]{1,4}$") ){
				txtCartaDigitosTarjeta.setStyleClass("frmInput2Error");
				bValido = false;
			}
			if(!sAutoriz.matches("^[0-9]{1,10}$") ){
				txtCartaCodAutoriz.setStyleClass("frmInput2Error");
				bValido = false;
			}	
			
			if(!bValido){
				return;
			}
			
			dwDatosCartaxDevolucion.setWindowState("hidden");
			 
			if(m.get("rsc_solicitudSeleccionada") == null){
				throw new Exception("No se ha podido aprobar la solicitud");
			}
						
			session  = HibernateUtilPruebaCn.currentSession();
			transaction = session.beginTransaction();
 
			Vsolecheque v = (Vsolecheque)m.get("rsc_solicitudSeleccionada");
			vaut  = ((Vautoriz[]) m.get("sevAut"))[0];
			v.getId().setCodautoriz(sAutoriz);
			v.getId().setNotarjeta(sNotarjeta);

			 
			bValido = sc.actualizarSolecheque(v, vaut.getId().getCodreg(),"",24, session);
			
			if(!bValido){
				throw new Exception("No se ha podido actualizar el estado de la solicitud");
			}

			sMonedaBase = CompaniaCtrl.sacarMonedaBase((F55ca014[])m.get("cont_f55ca014"), v.getId().getCodcomp());

			bValido  = generarAsientosAnulacionTarjeta(v, vaut, session, transaction, sMonedaBase);
			
			if (!bValido) {
				sMensaje =  m.get("rsc_MsgErrorAsientoCarta") == null ? "No se han podido generar los asientos de anulacion." :  m.get("rsc_MsgErrorAsientoCarta").toString();
				throw new Exception(sMensaje);
			}
				
			if(bValido){
				transaction.commit();
				
				//----- Guardar el objeto de Vsolecheque en sesion para el reporte.
				v = (Vsolecheque)m.get("rsc_solicitudSeleccionada");
				m.put("rsc_solicitudSeleccionada", v);
				m.put("rsc_DestinoCarta", sNombreDestino);
				m.put("rsc_OrigenCarta", sNombreOrigen);
				
				//------- Enviar correo para notificación de aprobación del contador hacia el supervisor de la caja.
				String sFecha, sHora,sEncabezado,sPieCorreo,sSubject;
				String sTo, sFrom,sNombreFrom,sCc;
				 
				List lstCajas = (List)m.get("lstCajas");
	    		Vf55ca01 f5 = ((Vf55ca01)lstCajas.get(0));
	    		VsolechequeId vId = v.getId();    		 
									
				sFecha = FechasUtil.formatDatetoString(vId.getFecha(), "dd/MM/yyyy");
				sHora  = FechasUtil.formatDatetoString(vId.getHora(),  "hh:mm:ss a");
				sEncabezado = "Aprobación de Solicitud de Emisión de Carta de Anulación";
				sPieCorreo  = "Esta solicitud de carta ha sido aprobada";
				sSubject 	= "Notificación de aprobación de Emisión de carta de anulación";
				
				sTo =   f5.getId().getCaan8mail().trim();
				 
				Vf0101 vf01 = EmpleadoCtrl.buscarEmpleadoxCodigo2(vaut.getId().getCodreg());
				
				if(vf01!=null){
					sFrom   = vf01.getId().getWwrem1().trim().toUpperCase();
					sNombreFrom = vf01.getId().getAbalph().trim();			
					
					if(!Divisas.validarCuentaCorreo(sFrom))
						sFrom = "webmaster@casapellas.com.ni";
				}else{
					sFrom = f5.getId().getCacontmail().trim();
					sNombreFrom = f5.getId().getCacontnom().trim();
				}
				sCc   = "";
				
				String sUrl = PropertiesSystem.UrlSisCaja();
				
				
				List<String[]> cuentas = new ArrayList<String[]>();
				cuentas.add(new String[]{f5.getId().getCaan8mail().trim(), f5.getId().getCaan8nom().trim()});
				cuentas.add(new String[]{sFrom, sNombreFrom});
				CodeUtil.putInSessionMap("rsc_CuentasCorreo", cuentas);
				
				//---- Validar el correo.
				Pattern pCorreo = Pattern.compile( "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$" );
				if(pCorreo.matcher(sTo).matches() && pCorreo.matcher(sFrom).matches()){
					sc.enviarCorreo(sEncabezado, sPieCorreo, sTo, sFrom,sNombreFrom, sCc, sSubject,vId.getNosol(), vId.getCaid() + " " +vId.getCaname(), 
							vId.getCodsuc()+" " + vId.getNomsuc(), vId.getCodcomp().trim() +" "+vId.getNomcomp(), vId.getCodunineg()+" "+vId.getUnineg(),
							vId.getCodcli() + " " +vId.getCliente(), "Devolución: "+vId.getNumfac()+" / Factura: "+ vId.getNofactoriginal(), 
							dv.formatDouble(vId.getMonto().doubleValue()) +" " +vId.getMoneda(), sFecha+" "+sHora,sUrl);
				} 
			}else{
				transaction.rollback();
			}			
			
			dwMensajeAprSolicitud.setWindowState("normal");
		 
		} catch (Exception error) {
			LogCajaService.CreateLog("realizarEmisionCartaxDevolucion", "ERR", error.getMessage());
			lblMsgValidaSolecheque.setValue("No se ha podido aprobar la solicitud ");
			dwValidacion.setWindowState("normal");
			error.printStackTrace();
			
			try {
				if (!bValido && transaction != null) {
					transaction.rollback();
				}
			} catch (Exception e) {
				LogCajaService.CreateLog("realizarEmisionCartaxDevolucion", "ERR", e.getMessage());
			}
		}finally{
			LogCajaService.CreateLog("realizarEmisionCartaxDevolucion", "INFO", "realizarEmisionCartaxDevolucion-FIN");
			HibernateUtilPruebaCn.closeSession(session);
		}
	}
/******************************************************************************************/
/** Método: Confirmar aprobar la emisión de carta de anulación por devolución de contado
 *	Fecha:  09/07/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public void solicitarDatosCartaAnulacion(ActionEvent ev){
		try {
			txtDestinoCarta.setStyleClass("frmInput2");
			txtOrigenCarta.setStyleClass("frmInput2");
			txtCartaDigitosTarjeta.setStyleClass("frmInput2");
			txtCartaCodAutoriz.setStyleClass("frmInput2");
			
			txtDestinoCarta.setValue("");
			txtOrigenCarta.setValue("");
			txtCartaCodAutoriz.setValue("");
			txtCartaDigitosTarjeta.setValue("");

			dwDatosCartaxDevolucion.setWindowState("normal");
//			dwConfirmaAprobarSCarta.setWindowState("hidden");
			
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
	
	public String revertirRefacturacion(Vsolecheque v, int codregUpd){
		boolean aplicado = true;
		String mensaje = new String("");
		Session sesion = null;
		Transaction trans = null;
		
		ReciboCtrl rCtrl = new ReciboCtrl();
		String statHdr = new String("");
		String statDtl = new String("");
//		Connection cn = null;
		
		
		try {
			//&& ======= Verificar que la anulacion sea en el mismo dia.
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy") ;
			Date dtRec = sdf.parse(sdf.format(v.getId().getFecha())); 
			Date dtHoy = sdf.parse(sdf.format(new Date())); 
			
			if( dtRec.compareTo(dtHoy) != 0 )
				return mensaje = "La reversión debe ser aplicada el mismo dia de devolución" ;
			
			sesion = HibernateUtilPruebaCn.currentSession();
			trans = sesion.beginTransaction();
			
			//&& ==== Buscar el registro de recibofac.
			Recibofac rf = rCtrl.getRecibofac(sesion,v.getId().getCaid(),			
							v.getId().getCodcomp(), v.getId().getNumfac(), 
							v.getId().getTipofactura(), v.getId().getCodunineg(),
							v.getId().getCodcli(), v.getId().getFechajul() );
			if(rf == null)
				return mensaje = "No se encontró registro de recibo asociado a devolución" ;
			
			//&& ======= Buscar el numero de batch y numero de documento.
			Recibojde rj = (Recibojde)sesion.createCriteria(Recibojde.class)
			.add(Restrictions.eq("id.tiporec", rf.getId().getTiporec()))
			.add(Restrictions.eq("id.numrec",  rf.getId().getNumrec()))
			.add(Restrictions.eq("id.caid", v.getId().getCaid()))
			.add(Restrictions.eq("id.codcomp",v.getId().getCodcomp() ))
			.add(Restrictions.eq("id.tipodoc", "R" ))
			.setMaxResults(1).uniqueResult();
			if(rj == null)
				return mensaje = "No se encontró el número de batch asociado a devolución";
			
			//&& ======= verificar que el RM en F0311 no se encuentre contabilizado.
			
			String tipoBatch = CodigosJDE1.BATCH_FINANCIMIENTO.codigo();
			
			statHdr = rCtrl.leerEstadoBatch( rj.getId().getNobatch(), tipoBatch  ) ;
			statDtl = rCtrl.leerEstadoDetalleRec( rj.getId().getNobatch(), tipoBatch, v.getId().getCodcli().intValue() );
			
			if(statHdr == null || statDtl == null || statHdr.compareTo("P")
				   == 0 || statHdr.compareTo("D") == 0 || statDtl.compareTo("P")
				   == 0 || statDtl.compareTo("D") == 0 ){
				return mensaje = "Nota de crédito ya ha sido contabilizada";
			}
				
			//&& ======= verificar que el RM no se encuentre contabilizado.
			statDtl = rCtrl.leerEstadoDetalleAD(rj.getId().getNobatch(),  v.getId().getCodcli().intValue(),  tipoBatch );
		
			if(statDtl == null || statDtl.compareTo("P") == 0 || statDtl.compareTo("D") == 0){
				return mensaje = "El asiento de diario ya esta contabilizado ";
			}
			
//			cn = As400Connection.getJNDIConnection("DSMCAJA2");
//			cn.setAutoCommit(false);
			
			List<String[]> queries = new ArrayList<String[]>();	
			
			//&& ================= Borrar asiento de diario F0911 .

			String sql  = " delete from "+PropertiesSystem.JDEDTA+".F0911 where gldoc = " + rj.getId().getRecjde() + " and glicu = " + rj.getId().getNobatch() ;
			queries.add(new String[]{sql, "borrar registro de contabilidad General en F0911" } );
			
//			aplicado = rCtrl.borrarAsientodeDiario(cn, rj.getId().getRecjde(), rj.getId().getNobatch() );
//			if(!aplicado){
//				return mensaje = "No se ha podido eliminar asiento de diario " + rj.getId().getNobatch() ;
//			}
			
			//&& ======= Borrar registro del F0311
			int codcli = (v.getId().getNotarjeta().trim().compareTo("") == 0) ?
					v.getId().getCodcli() : 
					Integer.valueOf(v.getId().getNotarjeta().trim());
		
			sql = " delete from "+PropertiesSystem.JDEDTA+".F03B11" +
				" where rpicu = "+rj.getId().getNobatch()+ " and rpdoc = " +
				rj.getId().getRecjde() + " and rpan8 = " + codcli + 
				" and rpdct = 'RM' and rpicut = '"+tipoBatch+"'" ;
			
			queries.add(new String[]{sql, "borrar registro de factura en F03B11" } );
			
//			aplicado  =  ( cn.prepareStatement(sql).executeUpdate() == 1 ) ;
//			if(!aplicado)
//				return mensaje = "No se ha podido deshacer nota de crédito: "+rj.getId().getNobatch() ;
			
			//&& ================= Borrar cabecera de batch en F0011
			sql = "delete from "+PropertiesSystem.JDEDTA+".F0011 where icicu = " +rj.getId().getNobatch() + " and icicut = '" +tipoBatch+"'" ; 
			queries.add(new String[]{sql, "borrar encabezado de comprobante en F0011" } );
			
//			aplicado = rCtrl.borrarBatch(cn, rj.getId().getNobatch(),"I");
//			if(!aplicado)
//				return mensaje = "Asiento de diario "+rj.getId().getNobatch()+ " no revertido " ;
			
			//&& ======= Actualizar Recibofac 
			sql = " update " +PropertiesSystem.ESQUEMA +".recibofac" +
				" set estado = 'A' where caid = " +rf.getId().getCaid()+
				" and trim(codcomp) = '"+rf.getId().getCodcomp().trim()+"'"+
				" and trim(codunineg)='"+rf.getId().getCodunineg().trim()+"'"+
				" and numrec = "+rf.getId().getNumrec()+" and numfac = "
				+rf.getId().getNumfac() +" and estado = '' ";
			
			queries.add(new String[]{sql, "No se ha podido liberar factura por devolución" } );
			
//			aplicado = sesion.createSQLQuery(sql).executeUpdate() == 1 ;
//			if(!aplicado)
//				return mensaje = "No se ha podido liberar factura por devolución ";

			//&& ======= Actualizar la solicitud de cheques.
			sql = " update " +PropertiesSystem.ESQUEMA +".solecheque "+
				" set estado = 'X', usuariomod = " + codregUpd +", fechamod = '"+
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss").format(new Date()) +"', "+
				" observacion = '"+v.getId().getObservacion().trim()+"' " +
				" where caid = " +v.getId().getCaid()+" and  trim(codcomp) ='"+
				v.getId().getCodcomp().trim()+"' and trim(codunineg) = '"+
				v.getId().getCodunineg().trim()+"' and nosol = " + 
				v.getId().getNosol() +" and estado = 'G'";
				
			queries.add(new String[]{sql, "No se ha podido actualizar el estado de la solicitud de refacturacion" } );
			 
			for (String[] querys : queries) {
				 
				try {
					boolean execute = ConsolidadoDepositosBcoCtrl .executeSqlQueryTx(sesion, querys[0]);
		
				} catch (Exception e) {
					return mensaje = " fallo en interfaz Edwards "+ querys[1] ;
				}
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			aplicado = false;
			mensaje = "Reversión no aplicada"; 
		}finally{
		
			aplicado = mensaje.trim().compareTo("") == 0;
			
			
			try {
				if (aplicado) trans.commit();
			} catch (Exception e) {
				aplicado = false;
				e.printStackTrace();
			}
			
			/*
			try {
				if (aplicado) cn.commit();
			} catch (Exception e) {
				aplicado = false;
			}
			*/
			
			try{
				if( !aplicado && sesion != null && trans.isActive() )
					 trans.rollback();
				
				/*
				if( !aplicado && cn != null && !cn.isClosed() )
					 cn.rollback();
				*/
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				HibernateUtilPruebaCn.closeSession(sesion);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			/*
			try{
				if(cn != null && !cn.isClosed()) cn.close();
			 } catch (Exception e) { }	
			*/
			
		}
		return mensaje;
	}
/************************************************************************************/
/**********			Rechazar la Solicitud de Emisión de Cheques					*****/	
	@SuppressWarnings("unchecked")
	public void rechazarSolicitudEmisionCheque(ActionEvent ev){
		String sMotivo = "";
		SolechequeCtrl sc = new SolechequeCtrl();
		boolean bHecho = true;
		ReciboCtrl rCtrl = new ReciboCtrl();
		
		try {
			dwConfirmarRechazar.setWindowState("hidden");
			
			sMotivo = txtMotivoRechazo.getValue().toString();
			if(sMotivo.trim().compareTo("") == 0) {
				txtMotivoRechazo.setStyleClass("frmInput2Error");
				return;
			}
			if(m.get("rsc_solicitudSeleccionada") == null){
				dwValidacion.setWindowState("normal");
				lblMsgValidaSolecheque.setValue("Error al obtener datos de solicitud, favor intente de nuevo.");
				return;
			}
			
			final Vsolecheque v = (Vsolecheque)m.get("rsc_solicitudSeleccionada");
			Vautoriz[] vaut  = (Vautoriz[]) m.get("sevAut");
			
			//&& ============= proceso para revertir refacturacion.
			if( v.getId().getTipoemision().compareTo("RFC")== 0 ){
				
				v.getId().setObservacion(sMotivo);
				
				String msg = revertirRefacturacion(v, vaut[0].getId().getCodreg());
				if(msg.compareTo("") == 0 )
					msg = "Reversión realizada correctamente";					

				//&& =====  Borrar el registro de la lista y refrescar el grid.
				List<Vsolecheque> solicitudes = (ArrayList<Vsolecheque>)m.get("rsc_lstSolicitudesCheques") ;
				CollectionUtils.filter(solicitudes, new Predicate() {
					public boolean evaluate(Object o) {
						Vsolecheque v1 = (Vsolecheque)o;
						return !(v.getId().getNosol() == v1.getId().getNosol()
								&& v.getId().getCaid() == v1.getId().getCaid()
								&& v.getId().getCodcomp().trim().compareTo(
									v1.getId().getCodcomp().trim()) == 0 && 
								v.getId().getCodunineg().trim().compareTo(
									v1.getId().getCodunineg().trim()) == 0);
					}
				}) ;
				m.put("rsc_lstSolicitudesCheques",solicitudes);
				gvSolicitudesCheques.dataBind();
				
				SmartRefreshManager.getCurrentInstance().addSmartRefreshId(
						gvSolicitudesCheques.getClientId(
								FacesContext.getCurrentInstance()));
				
				dwValidacion.setWindowState("normal");
				lblMsgValidaSolecheque.setValue(msg);
				return;
			}

			//&& ============= proceso para solicitudes de cheques y carta credomatic
			Session sesion = HibernateUtilPruebaCn.currentSession();
			Transaction trans = sesion.beginTransaction();
			
			int iNuevoEstado = (v.getId().getTipoemision().compareTo("RFC")==0)?65:25;
			
			bHecho = sc.actualizarSolecheque(v, vaut[0].getId().getCodreg(),
									sMotivo, iNuevoEstado, sesion);
			if(!bHecho){
				dwValidacion.setWindowState("normal");
				lblMsgValidaSolecheque.setValue("No se ha podido cambiar estado a la solicitud de cheque");
				trans.rollback();
				sesion.close();
				return;
			}
			
			//&& ===== Verificacion para liberacion de devolucion. (rechazo en el mismo dia)
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy") ;
			Date dtRec = sdf.parse(sdf.format(v.getId().getFecha())); 
			Date dtHoy = sdf.parse(sdf.format(new Date())); 
			sdf = null;
			
			if(dtRec.compareTo(dtHoy) == 0 ){
				Recibofac rf = rCtrl.getRecibofac(sesion,v.getId().getCaid(),			
												v.getId().getCodcomp(), 
												v.getId().getNumfac(),
												v.getId().getTipofactura(),
												v.getId().getCodunineg(),
												v.getId().getCodcli(),
												v.getId().getFechajul()
												
												/*FechasUtil.DateToJulian(v.getId().getFechafac())*/);
				
				if(rf == null){
					dwValidacion.setWindowState("normal");
					lblMsgValidaSolecheque.setValue("No se puede actualizar datos de la devolución para la emisión de cheque");
					trans.rollback();
					sesion.close();
					return;
				}				
				bHecho = rCtrl.actualizarRecibofac(
								sesion, rf.getId().getCaid(), rf.getId().getCodcomp(),
								rf.getId().getNumfac(), rf.getId().getTipofactura(),
								rf.getId().getTiporec(), rf.getId().getNumrec(), "A", 
								v.getId().getCodunineg(),v.getId().getCodcli(),
								v.getId().getFechajul()
								/*FechasUtil.DateToJulian(v.getId().getFechafac())*/);
			}
			String sMensaje = "";
			if(bHecho){
				trans.commit();
				sMensaje = "Se ha realizado correctamente la operación";
			}else{
				trans.rollback();
				sMensaje = "No se ha podido establecer disponible a pago la factura por devolución ";
			}
			sesion.close();
			
			m.remove("rsc_solicitudSeleccionada");
			dwValidacion.setWindowState("normal");
			lblMsgValidaSolecheque.setValue(sMensaje);

			restablecerValores(ev);
		
		} catch (Exception error) {
			dwValidacion.setWindowState("normal");
			lblMsgValidaSolecheque.setValue("Error al realizar proceso de rechazo de emision de cheque.");
			error.printStackTrace();
		}
	}
/*******************************************************************************************/
/** Mostrar la ventana de confirmación de rechazo de la solicitud de emisión de cheques	   */	
	@SuppressWarnings("unchecked")
	public void rechazarEmisionCheque(ActionEvent ev){
		String msg = new String("");
		Vsolecheque v = null;
		
		try {
			RowItem ri = (RowItem)ev.getComponent().getParent().getParent();
			v = (Vsolecheque)DataRepeater.getDataRow(ri);
			
			if (v.getId().getEstado().compareTo("G") == 0) {

				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				if (sdf.parse(sdf.format(v.getId().getFecha())).compareTo(
						sdf.parse(sdf.format(new Date()))) != 0) {
					msg = "La reversión de refactura " +
							"debe aplicarse el mismo día de la devolución";
					return;
				}
			}
			if (v.getId().getEstado().compareTo("P") != 0
					&& v.getId().getEstado().compareTo("G") != 0) {
				msg = "El estado de la solicitud no permite su reversión";
				return;
			}
			
		} catch (Exception error) {
			error.printStackTrace();
		}finally{
			
			if(msg.compareTo("") == 0){
				txtMotivoRechazo.setValue("");
				txtMotivoRechazo.setStyleClass("frmInput2");
				m.put("rsc_solicitudSeleccionada", v);
				dwConfirmarRechazar.setWindowState("normal");
			}else{
				dwValidacion.setWindowState("normal");
				lblMsgValidaSolecheque.setValue(msg);
			}
		}
	}
/************************************************************************************/
/**	Aprobar la emisión de cheque, enviar correos y generar reporte de emisión		*/		
	@SuppressWarnings({ "unchecked", "static-access" })
	public void aprobarSolicitudEmisionChk(ActionEvent ev){
		Vsolecheque v = null;
		VsolechequeId id = null;
		SolechequeCtrl sc = new SolechequeCtrl();
		boolean bHecho = true,bCorreo = true;
		String sEncabezado = "", sPieCorreo="", sTo, sFrom, sCc, sSubject="";
		String sFecha = "", sHora = "",sMensajeError="",sNombreFrom="";
		FechasUtil f = new FechasUtil();
		Divisas dv = new Divisas();
		
		Transaction trans = null;
		Session sesion = null;
		
		try {
			dwConfirmaAprobarSchk.setWindowState("hidden");
	
			LogCajaService.CreateLog("aprobarSolicitudEmisionChk", "INFO", "aprobarSolicitudEmisionChk-INICIO");
			//----- Obtener el registro de la solicitud seleccionada.
			if(m.get("rsc_solicitudSeleccionada")!=null){
				v = (Vsolecheque)m.get("rsc_solicitudSeleccionada");
				id = v.getId();
				
				List lstCajas = (List)m.get("lstCajas");
	    		Vf55ca01 f5 = ((Vf55ca01)lstCajas.get(0));
				Vautoriz[] vaut  = (Vautoriz[]) m.get("sevAut");
				
				sesion = HibernateUtilPruebaCn.currentSession();
				trans = sesion.beginTransaction();
				
				//------- Actualizar el registro de la solicitud.
				bHecho = sc.actualizarSolecheque(sesion, v, vaut[0].getId().getCodreg(),"",24);
				
				if (bHecho) {
					trans.commit();
				} else {
					trans.rollback();
				}
				
				if(bHecho){
					//------- Enviar correo para notificación de aprobación del contador hacia el supervisor de la caja.
					sFecha = FechasUtil.formatDatetoString(id.getFecha(), "dd/MM/yyyy");
					sHora  = FechasUtil.formatDatetoString(id.getHora(),  "hh:mm:ss a");
					sEncabezado = "Aprobación de Solicitud de Emisión de Cheques";
					sPieCorreo  = "Esta solicitud de cheque ha sido aprobada";
					sSubject 	= "Notificación de aprobación de Emisión de cheque";
					
					sTo =   f5.getId().getCaan8mail().trim();
					EmpleadoCtrl ec = new EmpleadoCtrl();
					Vf0101 vf01 = ec.buscarEmpleadoxCodigo2(vaut[0].getId().getCodreg());
					if(vf01!=null){
						sFrom   = vf01.getId().getWwrem1().trim().toUpperCase();
						sNombreFrom = vf01.getId().getAbalph().trim();			
						if(!dv.validarCuentaCorreo(sFrom))
							sFrom = "webmaster@casapellas.com.ni";
					}else{
						sFrom = f5.getId().getCacontmail().trim();
						sNombreFrom = f5.getId().getCacontnom().trim();
					}
					sCc   = "";
					
					//--- Direccion del sistema en el pie del correo.
					String sUrl = new Divisas().obtenerURL();
					if(sUrl==null || sUrl.trim().equals("")){
						Aplicacion ap = new Divisas().obtenerAplicacion(vaut[0].getId().getCodapp());
						sUrl = (ap==null)?"http://ap.casapellas.com.ni:7080/"+PropertiesSystem.CONTEXT_NAME+"":ap.getUrl().trim();
					}
					
					//---- Validar el correo.
					Pattern pCorreo = Pattern.compile( "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$" );
					if(pCorreo.matcher(sTo).matches() && pCorreo.matcher(sFrom).matches()){
						bCorreo = sc.enviarCorreo(sEncabezado, sPieCorreo, sTo, sFrom,sNombreFrom, sCc, sSubject, id.getNosol(), id.getCaid() + " " +id.getCaname(), 
										id.getCodsuc()+" " + id.getNomsuc(), id.getCodcomp().trim() +" "+id.getNomcomp(), id.getCodunineg()+" "+id.getUnineg(),
										id.getCodcli() + " " +id.getCliente(), "Devolución: "+id.getNumfac()+" / Factura: "+ id.getNofactoriginal(), 
										dv.formatDouble(id.getMonto().doubleValue()) +" " +id.getMoneda(), sFecha+" "+sHora,sUrl);
						if(!bCorreo){
							sMensajeError = "No se ha podido enviar el correo de notificación de emisión de Cheque";
							bCorreo = false;
						}
					}else{
						bCorreo = false;
						sMensajeError = "Los datos para cuentas de correo de Supervisor y/o Contador no se encuentran configurados correctamente";
					}
				}else{
					sMensajeError = "NO se ha podido completar la operación: se ha producido un error al cambiar estado de solicitud";
				}
				
				if(bHecho){
					//------- Generar Reporte para la emisión de cheques.
					Rptmcaja008 r08 = new Rptmcaja008();
					r08.setSCompaniaNombre(v.getId().getNomcomp());
					r08.setSFechaSolicitud(f.formatDatetoString(id.getFecha(), "dd/MM/yyyy") + "   " +f.formatDatetoString(id.getHora(), "hh:MM:ss a"));
					r08.setSContador(f5.getId().getCacontnom());
					r08.setSSupervisor(f5.getId().getCaan8nom());
					r08.setINosolicitud(id.getNosol());
				
					String sFactura = "";
					sFactura = "No: " +id.getNofactoriginal() + ", Tipo: " +id.getTipofactoriginal();
					sFactura += ", Fecha: " + f.formatDatetoString(id.getFechafac(), "dd/MM/yyy") ;
					sFactura += ", Monto: " + dv.formatDouble(id.getTotalfact().doubleValue());
					r08.setSDatosFactura(sFactura);
					
					String sDevolucion = "";
					sDevolucion = "No: " +id.getNumfac() + ", Tipo: " + id.getTipofactura() ;
					sDevolucion += ", Fecha: " + f.formatDatetoString(id.getFechadev(), "dd/MM/yyy") ;
					sDevolucion += ", Monto: " + dv.formatDouble(id.getTotaldev().doubleValue());
					r08.setSDatosDevolucion(sDevolucion);
					
					r08.setSnombrecliente(id.getCodcli() + "  " +id.getCliente().trim());
					r08.setSMonedadev(id.getMoneda());
					r08.setCaid(id.getCaid());
					r08.setNombrecaja(id.getCaid() + " " +id.getCaname().trim());
					r08.setNombrecajero(id.getCodemp()+"   " + id.getNomcajero());
					r08.setDMontodev(id.getTotaldev().doubleValue());
					r08.setCodsuc(id.getNumrec()+"");

					//--------- Convertir un Número double a letras..
					int iParteEntera=0,iParteDecimal = 0;
					String sNumero[];
					sNumero = id.getMonto().toString().split("\\.");
					if(sNumero!=null && sNumero.length>0){
						iParteEntera  = Integer.parseInt(sNumero[0]);
						if(sNumero.length>1)
							iParteDecimal = Integer.parseInt(sNumero[1]);
					}
					NumeroEnLetras nl = new NumeroEnLetras();
					String sNumeroLetras = "",sMonto="";
					sNumeroLetras = nl.convertirLetras(iParteEntera);
					
					String sMoneda = "CÓRDOBAS";
					String sPrefix = "C$";
					if(id.getMoneda().trim().equals("USD")){
						sMoneda = "DÓLARES";
						sPrefix = "US$";
					}
					sMonto = sPrefix +" "+ dv.formatDouble(id.getMonto().doubleValue());
					sMonto += " " +sNumeroLetras +" "+ sMoneda+ " con "+iParteDecimal+"/100";
					r08.setCodcomp(dv.ponerCadenaenMayuscula(sMonto));

					//----- Cargar los datos para el reporte.
					@SuppressWarnings("rawtypes")
					List lstR08 = new ArrayList(1);
					lstR08.add(r08);
					m.put("rptmcaja008_bd", lstR08);
					
					//---- LLenar el detalle del recibo, objeto recibodetId para el subreporte.
					ReciboCtrl recCtrl = new ReciboCtrl();
					@SuppressWarnings("rawtypes")
					List lstDetalleRecibo = recCtrl.leerDetalleRecibo(id.getCajarec(), id.getCajasuc(), id.getCajacomp(), id.getNumrec(),id.getTiporec());
					if(lstDetalleRecibo!=null && lstDetalleRecibo.size()>0){
						MetodosPagoCtrl metCtrl = new MetodosPagoCtrl();
						Recibodet recibodet = null;
						for(int i = 0; i < lstDetalleRecibo.size();i++){
							recibodet = (Recibodet)lstDetalleRecibo.get(i);
							RecibodetId rdi = recibodet.getId();
							rdi.setMonto(recibodet.getMonto().doubleValue());
							rdi.setMpago(metCtrl.obtenerDescripcionMetodoPago(recibodet.getId().getMpago().trim()));
							lstDetalleRecibo.remove(i);
							lstDetalleRecibo.add(i,rdi);
						}
					}else lstDetalleRecibo = new ArrayList(1);
					m.put("rptmcaja008_detrec", lstDetalleRecibo);
					
					String sNombre = "Solicitud Emision.de.Cheque.No" +id.getNosol() + ".Caja:"+id.getCaid()+".Comp:"+id.getCodcomp();
					m.put("rptmcaja008_nombre", sNombre);
					
					//------- Navegar hacia la página que contiene el reporte.
					FacesContext fc = FacesContext.getCurrentInstance();
					NavigationHandler nh = fc.getApplication().getNavigationHandler();
					nh.handleNavigation(fc, null, "rptmcaja008");
				}
				
				m.remove("rsc_solicitudSeleccionada");
			}
		} catch (Exception error) {
			bHecho = false;
			LogCajaService.CreateLog("aprobarSolicitudEmisionChk", "ERR", error.getMessage());
			error.printStackTrace();
			
			try {
				if (!bHecho && trans != null) {
					trans.rollback();
				}
			} catch (Exception e) {
				LogCajaService.CreateLog("aprobarSolicitudEmisionChk", "ERR", e.getMessage());
			}
		} finally {
			LogCajaService.CreateLog("aprobarSolicitudEmisionChk", "INFO", "aprobarSolicitudEmisionChk-FIN");
		}
	}
/************************************************************************************/
/**	Procedimiento para lo que conlleva la aprobación de la emisión de cheques		*/	
	@SuppressWarnings("unchecked")
	public void aprobarEmisionCheque(ActionEvent ev){
		RowItem ri = null;
		Vsolecheque v = null;
		
		try {
			 ri = (RowItem)ev.getComponent().getParent().getParent();
			 v = (Vsolecheque)DataRepeater.getDataRow(ri);
			 
			 if(v.getId().getTipoemision().compareTo("RFC") == 0){
				 dwValidacion.setWindowState("normal");
				 lblMsgValidaSolecheque.setValue("La Solicitud por Refacturación no requiere aprobación");
				 return;
			 }
			 if(!v.getId().getEstado().equals("P")){
				 dwValidacion.setWindowState("normal");
				 lblMsgValidaSolecheque.setValue("La solicitud debe estar en estado 'PENDIENTE' para poder ser aprobada");
				 return;
			 }
			 
			 m.put("rsc_solicitudSeleccionada", v);
			 if(v.getId().getTipoemision().equals("CHK")){
				 dwConfirmaAprobarSchk.setWindowState("normal");
			 }else{
				 
				//dwConfirmaAprobarSCarta.setWindowState("normal");
				 
				txtDestinoCarta.setStyleClass("frmInput2");
				txtOrigenCarta.setStyleClass("frmInput2");
				txtCartaDigitosTarjeta.setStyleClass("frmInput2");
				txtCartaCodAutoriz.setStyleClass("frmInput2");
				
				txtDestinoCarta.setValue("");
				txtOrigenCarta.setValue("");
				txtCartaCodAutoriz.setValue("");
				txtCartaDigitosTarjeta.setValue("");
				dwDatosCartaxDevolucion.setWindowState("normal");
				 
			 }
			 
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
/************************************************************************************/
/**	Mostrar el detalle de la solicitud de emisión de cheques, factura y devolución **/		
	@SuppressWarnings("unchecked")
	public void mostrarDetalleSolechk(ActionEvent ev){
		FacturaCrtl facCtrl = new FacturaCrtl();
		Divisas divisas = new Divisas();
		Hfactura hFacDev = null,hFacFactura=null;
		Vsolecheque v  = null;
		RowItem ri = null;
		String sMsgError = "",sCodsuc;
		boolean bError = false;
		
		try {
			ri = (RowItem)ev.getComponent().getParent().getParent();
			v  = (Vsolecheque)DataRepeater.getDataRow(ri);
			
			
			v.getId().getFechadev();
			v.getId().getFechafac();
			FechasUtil f = new FechasUtil();

			sCodsuc = v.getId().getCodsuc().trim();
			if(sCodsuc.length()>2)
				sCodsuc = sCodsuc.substring(sCodsuc.length()-2,sCodsuc.length());
			
			//------Leer la devolución.
			hFacDev = FacturaCrtl.getFacturaOriginal(v.getId().getNumfac(), v.getId().getTipofactura(),
							sCodsuc, v.getId().getCodcomp(), v.getId().getCodunineg());
			if(hFacDev != null){
				//----------------------- Cargar los datos para la devolución.
				txtNofactura.setValue(hFacDev.getNofactura());
				txtFechaFactura.setValue(hFacDev.getFecha());
				txtCliente.setValue(hFacDev.getNomcli());
				txtCodigoCliente.setValue(hFacDev.getCodcli());
				txtCodUnineg.setValue(hFacDev.getCodunineg());
				txtUnineg.setValue(hFacDev.getUnineg());

				f.obtenerDiasEntreFechas(v.getId().getFechafac(), new Date());
				f.obtenerDiferenciaDias(v.getId().getFechafac(), new Date());
				
				if (hFacDev.getCodVendedor() > 0) {
					EmpleadoCtrl emp = new EmpleadoCtrl();
					lblVendedorCont.setValue("Vendedor:");
					txtVendedorCont.setValue(emp.buscarEmpleadoxCodigo(hFacDev.getCodVendedor()));
				} else {
					lblVendedorCont.setValue("Facturador:");
					txtVendedorCont.setValue(hFacDev.getHechopor());
				}
								
				double dIVA = 0;
				double total = 0;
				double subtotal = 0;
				
				String sMonedaBase = CompaniaCtrl.sacarMonedaBase
							((F55ca014[])m.get("cont_f55ca014"), 
									v.getId().getCodcomp());
				
				if(hFacDev.getMoneda().compareTo(sMonedaBase) == 0){
					total = hFacDev.getTotal();
					subtotal = hFacDev.getSubtotal();
				}else{
					total = hFacDev.getTotalf();
					subtotal = hFacDev.getSubtotalf();
				}
				
				dIVA = new BigDecimal(Double.toString(total))
						.subtract(new BigDecimal(Double.toString(
								subtotal))).doubleValue();
				
				lblMonedaContado1.setValue(hFacDev.getMoneda());
				txtIva.setValue(divisas.formatDouble(dIVA));
				txtTotal.setValue(divisas.formatDouble(total));
				txtSubtotal.setValue(divisas.formatDouble(subtotal));

				//-------- Detalle de la devolución de la factura.
				lstDetalleDevolucion = FacturaCrtl.formatDetalle(hFacDev); 
				hFacDev.setDfactura(lstDetalleDevolucion);
				m.put("rsc_lstDetalleDevolucion", lstDetalleDevolucion);
				gvDetalleDevolucion.dataBind();
				
				//------Leer la Factura Original correspondiente a la devolución.
				hFacFactura = FacturaCrtl.getFacturaOriginal(v.getId().getNofactoriginal(), v.getId().getTipofactoriginal(),
														sCodsuc, v.getId().getCodcomp(), v.getId().getCodunineg());

				//------LLenar los datos de la factura a la ventana
				if(hFacFactura!=null){
					lblfacNofac.setValue(hFacFactura.getNofactura());
					lblfacFecha.setValue(hFacFactura.getFecha());
					lblFacNomcli.setValue(hFacFactura.getNomcli());
					lblFacCodcli.setValue(hFacFactura.getCodcli());
					lblFacCodUnineg.setValue(hFacFactura.getCodunineg());
					lblFacUnineg.setValue(hFacFactura.getUnineg());
					lblFacMoneda.setValue(hFacFactura.getMoneda());
					
					if (hFacFactura.getCodVendedor() > 0) {
						EmpleadoCtrl emp = new EmpleadoCtrl();
						lblFacVendedor0.setValue("Vendedor:");
						lblFacVendedor1.setValue(emp.buscarEmpleadoxCodigo(hFacFactura.getCodVendedor()));
					} else {
						lblFacVendedor0.setValue("Facturador:");
						lblFacVendedor1.setValue(hFacFactura.getHechopor());
					}
					
					if(hFacFactura.getMoneda().compareTo(sMonedaBase) == 0){
						total = hFacFactura.getTotal();
						subtotal = hFacFactura.getSubtotal();
					}else{
						total = hFacFactura.getTotalf();
						subtotal = hFacFactura.getSubtotalf();
					}
					
					dIVA = new BigDecimal(Double.toString(total))
							.subtract(new BigDecimal(Double.toString(
									subtotal))).doubleValue();
					
					lblFacIva.setValue(divisas.formatDouble(dIVA));
					lblFacTotalDet.setValue(divisas.formatDouble(total));
					txtFacSubtotalDetalle.setValue(divisas.formatDouble(subtotal));
					
					//-------- Detalle de la devolución de la factura.
					lstDetalleFactura = FacturaCrtl.formatDetalle(hFacFactura); 
					hFacFactura.setDfactura(lstDetalleFactura);
					m.put("rsc_lstDetalleFactura", lstDetalleFactura);
					gvDetalleFactura.dataBind();
					
				}else{
					bError = true;
					sMsgError = "No se ha podido encontrar la factura para devolución No: " + v.getId().getNofactoriginal();
					sMsgError += " Tipo: "+ v.getId().getTipofactoriginal() + " Suc:" + v.getId().getCodsuc()+" Comp: " + v.getId().getCodcomp();
					sMsgError += " UniNeg: "+  v.getId().getCodunineg().trim();
				}
				lblNoReciboFactura.setValue(v.getId().getNumrec());
				dwDetalleSolicitud.setWindowState("normal");
				
			}else{
				bError = true;
				sMsgError = "No se ha podido encontrar la factura para devolución No: " + v.getId().getNumfac();
				sMsgError += " Tipo: "+ v.getId().getTipofactura() + " Suc:" + v.getId().getCodsuc()+" Comp: " + v.getId().getCodcomp();
				sMsgError += " UniNeg: "+  v.getId().getCodunineg().trim();
				dwValidacion.setWindowState("normal");
				lblMsgValidaSolecheque.setValue(sMsgError);
			}

		} catch (Exception error) {
			error.printStackTrace();
		}
	}
/************************************************************************************/
/**				Restablecer los valores iniciales en pantalla 					   **/
	public void restablecerValores(ActionEvent ev){
		
		try {
			m.remove("rsc_lstSolicitudesCheques");
			m.remove("rsc_lstFiltroMoneda");
			m.remove("rsc_lstFiltroEstado");
			m.remove("rsc_lstFiltroCompania");
			dcFechaFinal.setValue(new Date());
			dcFechaInicio.setValue(new Date());
			gvSolicitudesCheques.dataBind();
			ddlTipoBusqueda.dataBind();
			ddlFiltroCompanias.dataBind();
			ddlFiltroMonedas.dataBind();
			ddlFiltroEstado.dataBind();
			
			SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
			srm.addSmartRefreshId(dcFechaFinal.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(dcFechaInicio.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(gvSolicitudesCheques.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(ddlTipoBusqueda.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(ddlFiltroCompanias.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(ddlFiltroMonedas.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(ddlFiltroEstado.getClientId(FacesContext.getCurrentInstance()));
			
		} catch (Exception error) {
			error.printStackTrace();
		}		
	}
/************************************************************************************/
/**	Ejecutar los filtros de búsquedas sobre los registros de emisión de cheques	*****/	
	public void buscarSolicitudesDeCheques(ActionEvent ev){
		realizarBusqueda();
	}
	public void buscarSolicitudesDeCheques(ValueChangeEvent ev){
		realizarBusqueda();
	}
	@SuppressWarnings("unchecked")
	public void realizarBusqueda(){
		Session sesion = null;
		Transaction trans = null;
		List<Vsolecheque> solicitudes = null;
		
		try {

			int cacont = ((List<Vf55ca01>)m.get("lstCajas")).get(0).getId().getCacont();
			String sCodcomp = ddlFiltroCompanias.getValue().toString();
			String sMoneda  = ddlFiltroMonedas.getValue().toString();
			String sEstado  = ddlFiltroEstado.getValue().toString();
			String sFiltro  = ddlTipoBusqueda.getValue().toString();
			String sTipo    = ddlTipoSolicitudes.getValue().toString();
			String sParam = new String("");
			Date dtInicio = null;
			Date dtFin = null; 
			
			if(txtParametro.getValue() != null)
				sParam = txtParametro.getValue().toString().trim();
			
			if(dcFechaInicio.getValue()!=null)
				dtInicio = (Date)dcFechaInicio.getValue();
			if(dcFechaFinal.getValue()!=null)
				dtFin = (Date)dcFechaFinal.getValue();
			
			if( dtInicio != null && dtFin != null && dtInicio.compareTo(dtFin) > 0 ){
				dtInicio = (Date)dcFechaFinal.getValue();
				dtFin    = (Date)dcFechaInicio.getValue();
			}
			
			sesion = HibernateUtilPruebaCn.currentSession();
			trans = sesion.beginTransaction();
			
			//&& ======== Cajas por contador.
			List<Integer> caidxcont = (ArrayList<Integer>)sesion
					.createCriteria(F55ca01.class)
					.add(Restrictions.eq("id.cacont", cacont))
					.add(Restrictions.eq("id.castat", 'A'))
					.setProjection(Projections.property("id.caid")).list();
			
			//&& ======== filtros de busqueda.
			Criteria cr = sesion.createCriteria(Vsolecheque.class)
					.add(Restrictions.in("id.caid", caidxcont));
			if(dtInicio != null)
				cr.add(Restrictions.ge("id.fecha", dtInicio));
			if(dtFin != null)
				cr.add(Restrictions.le("id.fecha", dtFin));
			if (sCodcomp.trim().compareTo("SCO") != 0)
				cr.add(Restrictions.eq("id.codcomp", sCodcomp));
			if (sMoneda.trim().compareTo("SM") != 0)
				cr.add(Restrictions.eq("id.moneda", sMoneda));
			if (sEstado.trim().compareTo("SE") != 0)
				cr.add(Restrictions.eq("id.estado", sEstado));
			if (sTipo.trim().compareTo("STS") != 0)
				cr.add(Restrictions.eq("id.tipoemision", sTipo));
			
			if(sParam.trim().compareTo("") != 0){
				
				String campo = "cliente";
				switch ( Integer.valueOf(sFiltro) ) {
				case 2:	campo = "codcli"; break;
				case 4:	campo = "numfac"; break;
				case 5:	campo = "nofactoriginal"; break;
				}
				
				if(sFiltro.compareTo("3") == 0 && sFiltro.matches(
							PropertiesSystem.REGEXP_NUMBER))
					cr.add(Restrictions.eq("id.nosol", Integer.parseInt(sParam)));
				else
					cr.add(Restrictions.sqlRestriction(" trim(lower("
									+campo+")) like '%"+sParam.toLowerCase()+"%'"));
			}
			solicitudes = (ArrayList<Vsolecheque>)cr.list();
			
		} catch (Exception e) {
			solicitudes = null;
			e.printStackTrace();
		}finally{
			
			try{ if(trans != null && trans.isActive() ) 
					trans.commit();	} catch (Exception e) {}
			try{ if(sesion != null && sesion.isOpen()) 
					HibernateUtilPruebaCn.closeSession(sesion); 
			} catch (Exception e) {}
			
			if(solicitudes == null)
				solicitudes = new ArrayList<Vsolecheque>();
			
			m.put("rsc_lstSolicitudesCheques", solicitudes);
			gvSolicitudesCheques.dataBind();
		}
	}
	public void buscarSolicitudes(){
		String sql,sCodcomp,sMoneda,sEstado;
		String sFiltro,sFechaIni="", sFechaFin="", sParam = "";
		List lstSolicitudes = null;
		FechasUtil f = new FechasUtil();
		Date dtInicio = null, dtFin = null;
		SolechequeCtrl sc = new SolechequeCtrl();
		
		try {
			//-------- Datos de la caja.
			List lstCajas = (List)m.get("lstCajas");
    		Vf55ca01 f5 = ((Vf55ca01)lstCajas.get(0));
			
			//-------- Filtros y manejos de fechas.
			sCodcomp = ddlFiltroCompanias.getValue().toString();
			sMoneda  = ddlFiltroMonedas.getValue().toString();
			sEstado  = ddlFiltroEstado.getValue().toString();
			sFiltro  = ddlTipoBusqueda.getValue().toString();
			if(txtParametro.getValue()!=null)
				sParam = txtParametro.getValue().toString().trim();
			
			if(dcFechaInicio.getValue()!=null)
				dtInicio = (Date)dcFechaInicio.getValue();
			if(dcFechaFinal.getValue()!=null)
				dtFin = (Date)dcFechaFinal.getValue();
			
			if( dtInicio != null && dtFin != null){
				
				dtInicio = new SimpleDateFormat("dd/MM/yyyy").parse(new SimpleDateFormat("dd/MM/yyyy").format(dtInicio));
				dtFin    = new SimpleDateFormat("dd/MM/yyyy").parse(new SimpleDateFormat("dd/MM/yyyy").format(dtFin));

				if(dtInicio.compareTo(dtFin) > 0){			
					dtInicio = (Date)dcFechaFinal.getValue();
					dtFin    = (Date)dcFechaInicio.getValue();
				}
			}
			if(dtInicio != null){
				dcFechaInicio.setValue(dtInicio);
				sFechaIni = f.formatDatetoString(dtInicio, "yyyy-MM-dd");
			}
			if(dtFin != null){
				dcFechaFinal.setValue(dtFin);
				sFechaFin = f.formatDatetoString(dtFin,	   "yyyy-MM-dd");
			}
			//----- Generar la consulta.
			sql =  " from Vsolecheque as v where v.id.caid = "+f5.getId().getCaid();
			
			if(sFechaIni.compareTo("") != 0)
				sql += " and v.id.fecha >=  '"+sFechaIni+"'" ; 
			if(sFechaFin.compareTo("") != 0)
				sql += " and v.id.fecha <=  '"+sFechaFin+"'" ;
			
			if(!sCodcomp.equals("SCO"))
				sql += " and v.id.codcomp = '"+sCodcomp+"'";
			if(!sMoneda.equals("SM"))
				sql += " and v.id.moneda = '"+sMoneda+"'";
			if(!sEstado.equals("SE"))
				sql += " and v.id.estado = '"+sEstado+"'";
			//------ Filtros por tipo de búsqueda de cliente.
			if(!sParam.equals("")){
				if(sFiltro.equals("3")) 		//--- busqueda x # de solicitud
					sql += " and v.id.nosol = " +Integer.parseInt(sParam);
				else{
					String sCliente[] = new String[2];
					if(sParam.contains("=>"))
						sCliente = sParam.split("=>");
					else{
						sCliente[0] = sParam;
						sCliente[1] = sParam;
					}
					if(sFiltro.equals("1")) //búsqueda por nombre de cliente.
						sql += " and trim(lower(v.id.cliente)) like '%"+sCliente[1].trim().toLowerCase()+"%'";
					if(sFiltro.equals("2"))	//búsqueda por código de cliente.
						sql += " and cast(v.id.codcli as string) like '"+sCliente[0].trim().toLowerCase()+"%'";
				}
			}
			sql += " order by v.id.fecha asc";
			
			lstSolicitudes = sc.obtenerSolicitudes(sql, 50);
			if(lstSolicitudes==null)
				lstSolicitudes = new ArrayList();
			
			m.put("rsc_lstSolicitudesCheques", lstSolicitudes);
			gvSolicitudesCheques.dataBind();
			
		} catch (Exception error) {
			error.printStackTrace();
		}		
	}
	
/************************************************************************************/
/************* 		ESTABLECER EL FILTRO PARA BUSCAR AL SOLICITANTE		*************/	
	public void settipoBusquedaCliente(ValueChangeEvent e){
		try {			
			String strBusqueda = ddlTipoBusqueda.getValue().toString();
			m.put("rsc_strBusquedaCheques", strBusqueda);
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
/************************************************************************************/
/************* 				CERRAR LAS VENTANAS EMERGENTES				*************/	
	public void cerrarDetalleSolicitud(ActionEvent ev){
		dwDetalleSolicitud.setWindowState("hidden");
		m.remove("rsc_lstDetalleDevolucion");
	}
	public void cancelarAprobación(ActionEvent ev){
		dwConfirmaAprobarSchk.setWindowState("hidden");
		m.remove("rsc_solicitudSeleccionada");
	}
	public void cerrarMensajeValidacion(ActionEvent ev){
		dwValidacion.setWindowState("hidden");
	}
	public void cerrarRechazarSolicitud(ActionEvent ev){
		dwConfirmarRechazar.setWindowState("hidden");
	}
	public void cancelarEmisionCarta(ActionEvent ev){
		dwDatosCartaxDevolucion.setWindowState("hidden");
	}
	public void cancelarAprobacionCarta(ActionEvent ev){
		dwConfirmaAprobarSCarta.setWindowState("hidden");
	}
	//------------------- GETTERS Y SETTERS --------------------------/
	public HtmlDropDownList getDdlTipoBusqueda() {
		return ddlTipoBusqueda;
	}
	public void setDdlTipoBusqueda(HtmlDropDownList ddlTipoBusqueda) {
		this.ddlTipoBusqueda = ddlTipoBusqueda;
	}
	public List getLstTipoBusquedaCliente() {	
		try{
			if(lstTipoBusquedaCliente == null){
				lstTipoBusquedaCliente = new ArrayList();	
				lstTipoBusquedaCliente.add(new SelectItem("1","Nombre Cliente","Búsqueda por nombre de cliente"));
				lstTipoBusquedaCliente.add(new SelectItem("2","Código Cliente","Búsqueda por código de cliente"));
				lstTipoBusquedaCliente.add(new SelectItem("3","No.Solicitud","Búsqueda el número de salida"));
			}
		}catch(Exception error){
			error.printStackTrace();
	}
		return lstTipoBusquedaCliente;
	}
	public void setLstTipoBusquedaCliente(List lstTipoBusquedaCliente) {
		this.lstTipoBusquedaCliente = lstTipoBusquedaCliente;
	}
	public HtmlInputText getTxtParametro() {
		return txtParametro;
	}
	public void setTxtParametro(HtmlInputText txtParametro) {
		this.txtParametro = txtParametro;
	}
	public HtmlDropDownList getDdlFiltroCompanias() {
		return ddlFiltroCompanias;
	}
	public void setDdlFiltroCompanias(HtmlDropDownList ddlFiltroCompanias) {
		this.ddlFiltroCompanias = ddlFiltroCompanias;
	}
	public HtmlDropDownList getDdlFiltroEstado() {
		return ddlFiltroEstado;
	}
	public void setDdlFiltroEstado(HtmlDropDownList ddlFiltroEstado) {
		this.ddlFiltroEstado = ddlFiltroEstado;
	}
	public HtmlDropDownList getDdlFiltroMonedas() {
		return ddlFiltroMonedas;
	}
	public void setDdlFiltroMonedas(HtmlDropDownList ddlFiltroMonedas) {
		this.ddlFiltroMonedas = ddlFiltroMonedas;
	}
	public HtmlGridView getGvSolicitudesCheques() {
		return gvSolicitudesCheques;
	}
	public void setGvSolicitudesCheques(HtmlGridView gvSolicitudesCheques) {
		this.gvSolicitudesCheques = gvSolicitudesCheques;
	}
	public List getLstFiltroCompania() {
		try {
			if(m.get("rsc_lstFiltroCompania") == null) {			
				lstFiltroCompania = new ArrayList();
				List lstCajas = (List)m.get("lstCajas");
				CompaniaCtrl compCtrl = new CompaniaCtrl();
				F55ca014[] f55ca014 = null;
				
				lstFiltroCompania.add(new SelectItem("SCO","Compañía","Seleccione la compañía a utilizar"));
				f55ca014 = compCtrl.obtenerCompaniasxCaja(((Vf55ca01)lstCajas.get(0)).getId().getCaid());
				for (int i = 0; i < f55ca014.length; i ++){	
					lstFiltroCompania.add(new SelectItem(f55ca014[i].getId().getC4rp01(),f55ca014[i].getId().getC4rp01d1()));
				}
				m.put("rsc_lstFiltroCompania",lstFiltroCompania);			
			} else {
				lstFiltroCompania = (ArrayList)m.get("rsc_lstFiltroCompania");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return lstFiltroCompania;
	}
	public void setLstFiltroCompania(List lstFiltroCompania) {
		this.lstFiltroCompania = lstFiltroCompania;
	}
	public List getLstFiltroEstado() {
		try {
			if(m.get("rsc_lstFiltroEstado")== null){
				lstFiltroEstado  = new ArrayList();
				SalidasCtrl sCtrl = new SalidasCtrl();
				List lstEstados = sCtrl.leerValorCatalogo(9);
				
				lstFiltroEstado.add(new SelectItem("SE","Estados","Seleccione el estado de las solicitudes"));
				if(lstEstados !=null && lstEstados.size()>0){
					for(int i=0; i<lstEstados.size();i++){
						Valorcatalogo v = (Valorcatalogo)lstEstados.get(i);
						lstFiltroEstado.add(new SelectItem(v.getCodigointerno(),
										v.getDescripcion(),v.getDescripcion()));
					}
				}
				m.put("rsc_lstFiltroEstado", lstFiltroEstado);
			}else
				lstFiltroEstado = (ArrayList)m.get("rsc_lstFiltroEstado");
		} catch (Exception error) {
			error.printStackTrace();
		}		
		return lstFiltroEstado;
	}
	public void setLstFiltroEstado(List lstFiltroEstado) {
		this.lstFiltroEstado = lstFiltroEstado;
	}
	public List getLstFiltroMoneda() {
		try{	
			if(m.get("rsc_lstFiltroMoneda") == null) {
				Divisas dv = new Divisas();
				List lstMon = new ArrayList();
				lstFiltroMoneda = new ArrayList();
				
				lstFiltroMoneda.add(new SelectItem("SM","Moneda","Seleccione la moneda para filtrar solicitudes de cheques"));
				
				lstMon = dv.obtenerMonedasJDE();
				if(lstMon!=null && lstMon.size()>0){
					for(int i=0; i<lstMon.size();i++){
						Object ob[] = (Object[])lstMon.get(i);
						lstFiltroMoneda.add(new SelectItem(ob[0].toString().trim(),ob[0].toString().trim(),ob[1].toString().trim()));
					}
					m.put("rsc_lstFiltroMoneda", lstFiltroMoneda);
				}
			}else
				lstFiltroMoneda = (ArrayList)m.get("rsc_lstFiltroMoneda");
		}catch(Exception error){
			error.printStackTrace();
		}
		return lstFiltroMoneda;
	}
	public void setLstFiltroMoneda(List lstFiltroMoneda) {
		this.lstFiltroMoneda = lstFiltroMoneda;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List getLstSolicitudesCheques() {
		
		try {
			
			if(m.containsKey("rsc_lstSolicitudesCheques")){
				return lstSolicitudesCheques = (ArrayList)m.get
									("rsc_lstSolicitudesCheques");
			}
			
			String fecha = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			
			String sql  = new String("");
			String sCajas = new String("");
			
			int iCaid  =  ((List<Vf55ca01>)m.get("lstCajas")).get(0).getId().getCaid();
			int cacont =  ((List<Vf55ca01>)m.get("lstCajas")).get(0).getId().getCacont();
			
			List<F55ca01> lstCajasContador = (ArrayList<F55ca01>)
					new CtrlCajas().obtenerCajasxContadorF55(cacont);
			
			sCajas = ""+iCaid;
			for (F55ca01 f5 : lstCajasContador){
				if(f5.getId().getCaid() != iCaid)
					sCajas +=","+ f5.getId().getCaid();
			}
			sql = " from Vsolecheque where caid in (" + sCajas + ")"
					+ " and ( trim(estado) = 'P' or (trim(estado)" +
					" = 'G' and fecha = '" + fecha + "') )";
			
			//lstSolicitudesCheques = new SolechequeCtrl().obtenerSolicitudes(sql, 0);
			
			lstSolicitudesCheques = ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, Vsolecheque.class, false);
			
			
		} catch (Exception error) {
			error.printStackTrace(); 
		}finally{
			
			if(lstSolicitudesCheques == null)
    			lstSolicitudesCheques = new ArrayList();
    		m.put("rsc_lstSolicitudesCheques", lstSolicitudesCheques);
		}
		return lstSolicitudesCheques;
	}
	public void setLstSolicitudesCheques(List lstSolicitudesCheques) {
		this.lstSolicitudesCheques = lstSolicitudesCheques;
	}
	public HtmlDateChooser getDcFechaFinal() {
		return dcFechaFinal;
	}
	public void setDcFechaFinal(HtmlDateChooser dcFechaFinal) {
		this.dcFechaFinal = dcFechaFinal;
	}
	public HtmlDateChooser getDcFechaInicio() {
		return dcFechaInicio;
	}
	public void setDcFechaInicio(HtmlDateChooser dcFechaInicio) {
		this.dcFechaInicio = dcFechaInicio;
	}
	public Date getFechaactual1() {
		if(m.get("rsc_fechaactual1") == null){
			fechaactual1 = new Date();
			m.put("rsc_fechaactual1", fechaactual1);
		}
		return fechaactual1;
	}
	public void setFechaactual1(Date fechaactual1) {
		this.fechaactual1 = fechaactual1;
	}
	public Date getFechaactual2() {
		if(m.get("rsc_fechaactual2")==null){
			fechaactual2 = new Date();
			m.put("rsc_fechaactual2", fechaactual2);
		}
		return fechaactual2;
	}
	public void setFechaactual2(Date fechaactual2) {
		this.fechaactual2 = fechaactual2;
	}//-------------- detalle de Solicitud.
	public HtmlDialogWindow getDwDetalleSolicitud() {
		return dwDetalleSolicitud;
	}
	public void setDwDetalleSolicitud(HtmlDialogWindow dwDetalleSolicitud) {
		this.dwDetalleSolicitud = dwDetalleSolicitud;
	}
	public HtmlGridView getGvDetalleDevolucion() {
		return gvDetalleDevolucion;
	}
	public void setGvDetalleDevolucion(HtmlGridView gvDetalleDevolucion) {
		this.gvDetalleDevolucion = gvDetalleDevolucion;
	}
	public HtmlOutputText getLblMonedaContado1() {
		return lblMonedaContado1;
	}
	public void setLblMonedaContado1(HtmlOutputText lblMonedaContado1) {
		this.lblMonedaContado1 = lblMonedaContado1;
	}
	public HtmlOutputText getLblVendedorCont() {
		return lblVendedorCont;
	}
	public void setLblVendedorCont(HtmlOutputText lblVendedorCont) {
		this.lblVendedorCont = lblVendedorCont;
	}
	public List getLstDetalleDevolucion() {
		if(m.get("rsc_lstDetalleDevolucion")==null)
			lstDetalleDevolucion = new ArrayList();
		else
			lstDetalleDevolucion = (ArrayList)m.get("rsc_lstDetalleDevolucion");
		return lstDetalleDevolucion;
	}
	public void setLstDetalleDevolucion(List lstDetalleDevolucion) {
		this.lstDetalleDevolucion = lstDetalleDevolucion;
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
	public HtmlOutputText getTxtCodUnineg() {
		return txtCodUnineg;
	}
	public void setTxtCodUnineg(HtmlOutputText txtCodUnineg) {
		this.txtCodUnineg = txtCodUnineg;
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
	public HtmlOutputText getTxtVendedorCont() {
		return txtVendedorCont;
	}
	public void setTxtVendedorCont(HtmlOutputText txtVendedorCont) {
		this.txtVendedorCont = txtVendedorCont;
	}
	//---------- Detalle de Solicitud: FACTURA.
	public HtmlGridView getGvDetalleFactura() {
		return gvDetalleFactura;
	}
	public void setGvDetalleFactura(HtmlGridView gvDetalleFactura) {
		this.gvDetalleFactura = gvDetalleFactura;
	}
	public HtmlOutputText getLblFacCodcli() {
		return lblFacCodcli;
	}
	public void setLblFacCodcli(HtmlOutputText lblFacCodcli) {
		this.lblFacCodcli = lblFacCodcli;
	}
	public HtmlOutputText getLblFacCodUnineg() {
		return lblFacCodUnineg;
	}
	public void setLblFacCodUnineg(HtmlOutputText lblFacCodUnineg) {
		this.lblFacCodUnineg = lblFacCodUnineg;
	}
	public HtmlOutputText getLblfacFecha() {
		return lblfacFecha;
	}
	public void setLblfacFecha(HtmlOutputText lblfacFecha) {
		this.lblfacFecha = lblfacFecha;
	}
	public HtmlOutputText getLblfacNofac() {
		return lblfacNofac;
	}
	public void setLblfacNofac(HtmlOutputText lblfacNofac) {
		this.lblfacNofac = lblfacNofac;
	}
	public HtmlOutputText getLblFacNomcli() {
		return lblFacNomcli;
	}
	public void setLblFacNomcli(HtmlOutputText lblFacNomcli) {
		this.lblFacNomcli = lblFacNomcli;
	}
	public HtmlOutputText getLblFacTotalDet() {
		return lblFacTotalDet;
	}
	public void setLblFacTotalDet(HtmlOutputText lblFacTotalDet) {
		this.lblFacTotalDet = lblFacTotalDet;
	}
	public HtmlOutputText getLblFacUnineg() {
		return lblFacUnineg;
	}
	public void setLblFacUnineg(HtmlOutputText lblFacUnineg) {
		this.lblFacUnineg = lblFacUnineg;
	}
	public HtmlOutputText getLblFacVendedor0() {
		return lblFacVendedor0;
	}
	public void setLblFacVendedor0(HtmlOutputText lblFacVendedor0) {
		this.lblFacVendedor0 = lblFacVendedor0;
	}
	public HtmlOutputText getLblFacVendedor1() {
		return lblFacVendedor1;
	}
	public void setLblFacVendedor1(HtmlOutputText lblFacVendedor1) {
		this.lblFacVendedor1 = lblFacVendedor1;
	}
	public List getLstDetalleFactura() {
		if(m.get("rsc_lstDetalleFactura")==null)
			lstDetalleFactura = new ArrayList();
		else
			lstDetalleFactura = (ArrayList)m.get("rsc_lstDetalleFactura");
		return lstDetalleFactura;
	}
	public void setLstDetalleFactura(List lstDetalleFactura) {
		this.lstDetalleFactura = lstDetalleFactura;
	}
	public HtmlOutputText getTxtFacSubtotalDetalle() {
		return txtFacSubtotalDetalle;
	}
	public void setTxtFacSubtotalDetalle(HtmlOutputText txtFacSubtotalDetalle) {
		this.txtFacSubtotalDetalle = txtFacSubtotalDetalle;
	}
	public HtmlOutputText getLblFacIva() {
		return lblFacIva;
	}
	public void setLblFacIva(HtmlOutputText lblFacIva) {
		this.lblFacIva = lblFacIva;
	}
	public HtmlOutputText getLblFacMoneda() {
		return lblFacMoneda;
	}
	public void setLblFacMoneda(HtmlOutputText lblFacMoneda) {
		this.lblFacMoneda = lblFacMoneda;
	}
	public HtmlOutputText getLblNoReciboFactura() {
		return lblNoReciboFactura;
	}
	public void setLblNoReciboFactura(HtmlOutputText lblNoReciboFactura) {
		this.lblNoReciboFactura = lblNoReciboFactura;
	}
	public HtmlDialogWindow getDwConfirmaAprobarSchk() {
		return dwConfirmaAprobarSchk;
	}
	public void setDwConfirmaAprobarSchk(HtmlDialogWindow dwConfirmaAprobarSchk) {
		this.dwConfirmaAprobarSchk = dwConfirmaAprobarSchk;
	}
	public HtmlDialogWindow getDwValidacion() {
		return dwValidacion;
	}
	public void setDwValidacion(HtmlDialogWindow dwValidacion) {
		this.dwValidacion = dwValidacion;
	}
	public HtmlOutputText getLblMsgValidaSolecheque() {
		return lblMsgValidaSolecheque;
	}
	public void setLblMsgValidaSolecheque(HtmlOutputText lblMsgValidaSolecheque) {
		this.lblMsgValidaSolecheque = lblMsgValidaSolecheque;
	}
	public HtmlDialogWindow getDwConfirmarRechazar() {
		return dwConfirmarRechazar;
	}
	public void setDwConfirmarRechazar(HtmlDialogWindow dwConfirmarRechazar) {
		this.dwConfirmarRechazar = dwConfirmarRechazar;
	}
	public HtmlInputTextarea getTxtMotivoRechazo() {
		return txtMotivoRechazo;
	}
	public void setTxtMotivoRechazo(HtmlInputTextarea txtMotivoRechazo) {
		this.txtMotivoRechazo = txtMotivoRechazo;
	}
	public HtmlDialogWindow getDwDatosCartaxDevolucion() {
		return dwDatosCartaxDevolucion;
	}
	public void setDwDatosCartaxDevolucion(HtmlDialogWindow dwDatosCartaxDevolucion) {
		this.dwDatosCartaxDevolucion = dwDatosCartaxDevolucion;
	}
	public HtmlInputText getTxtDestinoCarta() {
		return txtDestinoCarta;
	}
	public void setTxtDestinoCarta(HtmlInputText txtDestinoCarta) {
		this.txtDestinoCarta = txtDestinoCarta;
	}
	public HtmlInputText getTxtOrigenCarta() {
		return txtOrigenCarta;
	}
	public void setTxtOrigenCarta(HtmlInputText txtOrigenCarta) {
		this.txtOrigenCarta = txtOrigenCarta;
	}
	public HtmlDialogWindow getDwConfirmaAprobarSCarta() {
		return dwConfirmaAprobarSCarta;
	}
	public void setDwConfirmaAprobarSCarta(HtmlDialogWindow dwConfirmaAprobarSCarta) {
		this.dwConfirmaAprobarSCarta = dwConfirmaAprobarSCarta;
	}
	public HtmlDialogWindow getDwMensajeAprSolicitud() {
		return dwMensajeAprSolicitud;
	}
	public void setDwMensajeAprSolicitud(HtmlDialogWindow dwMensajeAprSolicitud) {
		this.dwMensajeAprSolicitud = dwMensajeAprSolicitud;
	}
	public HtmlOutputText getLblMsgAprSolicitud() {
		return lblMsgAprSolicitud;
	}
	public void setLblMsgAprSolicitud(HtmlOutputText lblMsgAprSolicitud) {
		this.lblMsgAprSolicitud = lblMsgAprSolicitud;
	}
	public HtmlInputText getTxtCartaCodAutoriz() {
		return txtCartaCodAutoriz;
	}
	public void setTxtCartaCodAutoriz(HtmlInputText txtCartaCodAutoriz) {
		this.txtCartaCodAutoriz = txtCartaCodAutoriz;
	}
	public HtmlInputText getTxtCartaDigitosTarjeta() {
		return txtCartaDigitosTarjeta;
	}
	public void setTxtCartaDigitosTarjeta(HtmlInputText txtCartaDigitosTarjeta) {
		this.txtCartaDigitosTarjeta = txtCartaDigitosTarjeta;
	}
	@SuppressWarnings("unchecked")
	public List<SelectItem> getLstTipoSolicitudes() {
		
		try{
			if(m.containsKey("rsc_TiposSol"))
				return lstTipoSolicitudes = (ArrayList<SelectItem>)
										m.get("rsc_TiposSol");
			
			lstTipoSolicitudes = new ArrayList<SelectItem>();
			lstTipoSolicitudes.add(new SelectItem("STS","Todos",
							"Seleccione el tipo de solicitudes"));
			List<Valorcatalogo> tipos = new SalidasCtrl().leerValorCatalogo(11);
			
			if( tipos == null || tipos.isEmpty() ) return lstTipoSolicitudes;
			
			for (Valorcatalogo v : tipos) {
				lstTipoSolicitudes.add(new SelectItem(v.getCodigointerno(),
									v.getDescripcion(),v.getDescripcion()));
			}
			
		}catch(Exception e){
			lstTipoSolicitudes = new ArrayList<SelectItem>();
			e.printStackTrace();
		}finally{
			m.put("rsc_TiposSol", lstTipoSolicitudes);
		}	
		return lstTipoSolicitudes;
	}
	public void setLstTipoSolicitudes(List<SelectItem> lstTipoSolicitudes) {
		this.lstTipoSolicitudes = lstTipoSolicitudes;
	}
	public HtmlDropDownList getDdlTipoSolicitudes() {
		return ddlTipoSolicitudes;
	}
	public void setDdlTipoSolicitudes(HtmlDropDownList ddlTipoSolicitudes) {
		this.ddlTipoSolicitudes = ddlTipoSolicitudes;
	}
}
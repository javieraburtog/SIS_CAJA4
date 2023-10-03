package com.casapellas.dao;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.faces.component.html.HtmlOutputText;
import javax.faces.event.ActionEvent;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.casapellas.controles.ArqueoCajaCtrl;
import com.casapellas.controles.ArqueorecCtrl;
import com.casapellas.controles.ClsParametroCaja;
import com.casapellas.controles.ConsolidadoDepositosBcoCtrl;
import com.casapellas.controles.DebitosAutomaticosPmtCtrl;
import com.casapellas.controles.MetodosPagoCtrl;
import com.casapellas.controles.PlanMantenimientoTotalCtrl;
import com.casapellas.controles.TasaCambioCtrl;
import com.casapellas.controles.tmp.CompaniaCtrl;
import com.casapellas.controles.tmp.MonedaCtrl;
import com.casapellas.controles.tmp.ReciboCtrl;
import com.casapellas.entidades.B64strfile;
import com.casapellas.entidades.CajaParametro;
import com.casapellas.entidades.CierreSpos;
import com.casapellas.entidades.F55ca014;
import com.casapellas.entidades.MetodosPago;
import com.casapellas.entidades.Numcaja;
import com.casapellas.entidades.ResumenCobroAutomatico;
import com.casapellas.entidades.Transactsp;
import com.casapellas.entidades.Vf55ca01;
import com.casapellas.entidades.pmt.Vwbitacoracobrospmt;
import com.casapellas.entidades.pmt.PmtDtCobroAutomatico;
import com.casapellas.entidades.pmt.PmtMtCobroAutomatico;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.reportes.Rptmcaja015_CuotasPMT;
import com.casapellas.socketpos.HistoricoCobrosSocketpos;
import com.casapellas.socketpos.PosCtrl;
import com.casapellas.socketpos.TransaccionTerminal;
import com.casapellas.socketpos.bac.pojos.ExecuteTransactionResult;
import com.casapellas.socketpos.bac.pojos.Response;
import com.casapellas.socketpos.bac.transactions.ResponseCodes;
import com.casapellas.socketpos.bac.transactions.TransactionType;
import com.casapellas.socketpos.bac.transactions.TransactionsSocketPosBac;
import com.casapellas.util.*;
import com.casapellas.entidades.ens.Vautoriz;
import com.infragistics.faces.grid.component.RowItem;
import com.infragistics.faces.grid.component.html.HtmlGridView;
import com.infragistics.faces.shared.component.DataRepeater;
import com.infragistics.faces.window.component.html.HtmlDialogWindow;
import com.infragistics.faces.window.component.html.HtmlDialogWindowHeader;

import ni.com.casapellas.db2.ClsECommerceTransaccion;
import ni.com.casapellas.db2.pojo.AccesoDB2;
import ni.com.casapellas.db2.pojo.ResultadoECommerce;
import ni.com.casapellas.db2.pojo.TransactBanco;

public class DebitosAutomaticosPMT {
	private static String TIPO_RECIBO_PMT = "PM" ;

	private HtmlGridView gvCuotasPendientesDebitos;
	private HtmlGridView gvResumenCobrosAutomaticosProcesar;
	
	private List<Vwbitacoracobrospmt> lstCuotasPendientesDebitos;
	private List<ResumenCobroAutomatico> lstResumenCobrosAutomaticosProcesar;

	public HtmlOutputText lblMensajeValidacion;
	public HtmlOutputText lblMensajeConfirmacionCobros ;
	public HtmlOutputText lblMensajeConfirmacionCobrosPorNivel;
	public HtmlOutputText lblMensajeConfirmacionCobroCuotaIndividual;
	
	private HtmlDialogWindow dwResumenProcedimientoCobroAutomatico;
	public HtmlDialogWindow dwMensajesValidacion;
	public HtmlDialogWindow dwConfirmacionProcesaRecibo;
	public HtmlDialogWindow dwConfirmacionProcesoDebitos;
	public HtmlDialogWindow dwConfirmacionProcesoDebitosPorNivel;
	public HtmlDialogWindow dwConfirmarProcesoCuotaIndividual;
	
	public HtmlDialogWindowHeader dwTituloMensajeValidacion;
	
	
	public static List<List<String[]>> codigosNotificar(Vf55ca01 datos_caja, Integer codcajero) {
	 
		List<List<String[]>> cuentasNotificaciones = new ArrayList<List<String[]>>();
		
		try {
			
			ArrayList<Integer> codigosCuentasNotificacionTaller = new ArrayList<Integer>();
			List<Integer> codigosConfiguradosTaller = DebitosAutomaticosPmtCtrl.cuentasDeNotificacionTaller() ;
			
			if(codigosConfiguradosTaller != null ) {
				 codigosCuentasNotificacionTaller.addAll(codigosConfiguradosTaller);
			}
			
			codigosCuentasNotificacionTaller.addAll(    
					 Arrays.asList( 
						 new Integer[]{
							 codcajero,
							 datos_caja.getId().getCaan8()
							 }	
						 )
					 ) ;
			 
			 List<String> ctanot = new ArrayList<String>();
			 
			 for(int i=0; i < codigosCuentasNotificacionTaller.size(); i++) 
				 ctanot.add( String.valueOf( codigosCuentasNotificacionTaller.get(i) )) ;
			 
			 Integer codigosNotificaciones[] = new Integer[ ctanot.size() ] ;  
			 for(int i=0; i < ctanot.size(); i++) 
				 codigosNotificaciones[i] = Integer.parseInt(ctanot.get(i));
			 
			 List<String[]>notificacionProceso = DebitosAutomaticosPmtCtrl.cuentasEnvioCorreos(codigosNotificaciones);
			 cuentasNotificaciones.add(0, notificacionProceso);
			 
			 Integer[] codigosConfgTaller = new Integer[codigosConfiguradosTaller.size()];
			 codigosConfiguradosTaller.toArray(codigosConfgTaller);
			 
			 List<String[]>notificacionCobros = DebitosAutomaticosPmtCtrl.cuentasEnvioCorreos( codigosConfgTaller );
			 cuentasNotificaciones.add(1, notificacionCobros);
			
			
		}catch(Exception ex) {
			ex.printStackTrace();
			cuentasNotificaciones = null;
		}
		return cuentasNotificaciones;
	}
	
	
	@SuppressWarnings("unchecked")
	public void procesarCuotaIndipendiente(ActionEvent ev){
		String strMensajeProceso = "" ;
		
		Vautoriz vautoriz = null;
		Vf55ca01 datos_caja = null;
		
		Date dtHoraInicia = new Date();
		List<Vwbitacoracobrospmt> lstCuotasPendientesDebitos = null ;
		Vwbitacoracobrospmt v =  null;
		List<String[]> cuentasNotificaciones = null ;
		List<String[]> cuentasNotificacionesCobros = null ;
		
		int numeroArqueo = 0;
		Date fechaArqueo = new Date();
		
		int caid = 0 ;
		int establoqueadodebito = 0;
		try {
			
			//&& ============ debitos automaticos bloqueados.
			ClsParametroCaja clsPC = new ClsParametroCaja();
			
			CajaParametro lstCP = clsPC.getParametros("06", "0", "DEBAUT");
			if(lstCP.getValorNumerico()==1)
			{
				establoqueadodebito = 1;
				strMensajeProceso  = "Debito automático se esta procesando";
				return;
			}

			if(!clsPC.setParametros("06", "0", "DEBAUT", null, 1, null))
			{
				establoqueadodebito = 1;
				strMensajeProceso  = "No se pudo bloquear proceso de pago";
				return;
			}
			
			CodeUtil.removeFromSessionMap("debatm_DatosArqueo");
			
			v = (Vwbitacoracobrospmt) CodeUtil.getFromSessionMap("dbatm_CuotaSeleccionadaProcesar");			
			
			//&& ============ validar que no hayan cierres pendientes para la terminal configurada
			
			List<Transactsp> pendientes = PosCtrl.cobrosSocketPosPendientes(v.getCodigoterminal().trim(), 0) ;
			if(  pendientes != null && !pendientes.isEmpty()){
				strMensajeProceso = "No se puede proceder  Existen transacciones pendientes de cerrar para la terminal " + v.getCodigoterminal() ; 
			}
			
			vautoriz = ((Vautoriz[])CodeUtil.getFromSessionMap("sevAut") )[0];
			datos_caja = ((List<Vf55ca01>) CodeUtil.getFromSessionMap("lstCajas")).get(0);
			caid = datos_caja.getId().getCaid();
			
			//&& ============ leer los codigos de las personas que se les enviara copia de los correos generados por el sistema.
			List<List<String[]>> cuentasEnviosCorreo = codigosNotificar(datos_caja, vautoriz.getId().getCodreg() );
			cuentasNotificaciones =  cuentasEnviosCorreo.get(0);
			cuentasNotificacionesCobros = cuentasEnviosCorreo.get(1);			
			 
			//&& ================= lista con todas las cuotas pendientes
			lstCuotasPendientesDebitos = (ArrayList<Vwbitacoracobrospmt>)	CodeUtil.getFromSessionMap("debatm_lstCuotasPendientesDebitos");
			
			generarObjConfigComp( v.getCodcomp().trim(), caid ) ; 
			
			PmtDtCobroAutomatico p = new  PmtDtCobroAutomatico( 
					 v.getMpbcli(), v.getMpbnctto(), v.getMpbnpag(), v.getMontocobrarentarjeta(), 
					 v.getMpbmon(), v.getMpbfcgnr(), v.getMpbnumrec(), 
					 v.getNumerotarjeta4d(), v.getCodigoterminal(), 
					 Long.parseLong( v.getReferenceNumber().trim()  ) , false,
					 v.getRowcount()
					 ) ;
			
			//&& ================= aplicar el cobro en credomatic
			strMensajeProceso = procesarPagoSocketPos(v, datos_caja.getId().getCaid(), vautoriz.getId().getCodreg() );
			
			if( !strMensajeProceso.isEmpty() ){
				
				v.setEstadoProceso("Error");
				v.setObservaciones(strMensajeProceso);
				v.setErrorpagotarjeta(true);
				v.setRegistroprocesado(false);
				
				p.setObservaciones(strMensajeProceso);
				p.setCodigorespuestasp( v.getResponseCode() );
				p.setDescripcionrespuestasp(v.getResponseCodeDescription());
				 
				DebitosAutomaticosPmtCtrl.crearIncidenciaPagosCuotas(null, v, "01");
				 
				DebitosAutomaticosPmtCtrl.notificacionCobroNoAplicado( Arrays.asList(new Vwbitacoracobrospmt[] {v} ) );
				
				return;
			 }
			 
		 	 p.setNumerovoucher( Long.parseLong( v.getReferenceNumber().trim() ) );
			
			 //&& ================= grabar el recibo en caja y el  pasivo en Edward's
			 List<Transactsp> transaccionesSocket = new ArrayList<Transactsp>();
			 
			 //Cerrar la session para liberar los recursos antes de iniciar el proceso del recibo
			 HibernateUtilPruebaCn.closeSession();
			 
			 strMensajeProceso =  procesarReciboCaja(v, vautoriz, datos_caja, transaccionesSocket) ;
			
			 if( !strMensajeProceso.isEmpty() ){
				 anularPagoSocketPos(v);
				 v.setEstadoProceso("Error");
				 v.setObservaciones(strMensajeProceso);
				 v.setRegistroprocesado(false);
				 
				 p.setObservaciones(strMensajeProceso);
				 
				 return;
			 }
			 
			p.setNumerorecibo( v.getMpbnumrec() );
			p.setEstado(true);
			p.setObservaciones(  "Cobro Aprobado con autorizado # " + v.getAuthorizationNumber() );
			 
			v.setObservaciones("Cobro Aprobado con autorizado # " + v.getAuthorizationNumber() );
			v.setRegistroprocesado(true);
			v.setEstadoProceso("Aplicado");			 
			
			//&& ============ notificacion al cliente del cobro de la cuota 
			//DebitosAutomaticosPmtCtrl.notificacionCorreoCuotaAplicada( Arrays.asList( new Vwbitacoracobrospmt[]{v} ), cuentasNotificacionesCobros  );
			
			p.setNotificacioncliente( v.isNotificado() );
			
			 
			//&& ================= crear el historico de la ejecucion del proceso
			PmtMtCobroAutomatico mt = 
				new PmtMtCobroAutomatico( vautoriz.getId().getCodreg(), 
					datos_caja.getId().getCaid(), 
					1, ( (v.isRegistroprocesado()? 1:0 ) ), 
					dtHoraInicia, dtHoraInicia, new Date(), 
					PropertiesSystem.getDataFromPcClient() ) ;
			
			PmtDtCobroAutomatico[] cuotasDtaProceso = new PmtDtCobroAutomatico[]{p};
			registrarBitacoraCobroAutomatico(mt, cuotasDtaProceso) ;
			 
			List<Vwbitacoracobrospmt> cuotasAplicadas = new ArrayList<Vwbitacoracobrospmt>(); 
			cuotasAplicadas.add(v);
			 
			//&& ========== remover la cuota de la lista de pendientes
			if(lstCuotasPendientesDebitos != null && !lstCuotasPendientesDebitos.isEmpty()){
				lstCuotasPendientesDebitos = (List<Vwbitacoracobrospmt>) CollectionUtils.subtract(lstCuotasPendientesDebitos, cuotasAplicadas);
			}
			
			//&& =================  aplicar el cierre de caja.  
			Date dtHoraFinaliza = new Date();
				
			strMensajeProceso = aplicarCierreCaja(cuotasAplicadas, cuentasNotificaciones, dtHoraInicia, dtHoraFinaliza, vautoriz,datos_caja);
			 
			if(!strMensajeProceso.isEmpty()){
				return;
			} 
			 
			Object[] dtaArqueo = (Object[]) CodeUtil.getFromSessionMap("debatm_DatosArqueo");
			numeroArqueo = Integer.parseInt( String.valueOf( dtaArqueo[0] ) ) ;
			fechaArqueo = (Date) dtaArqueo[1];
			
			//&& ================ Crear el objeto con los datos para el cierre de la terminal
			ResumenCobroAutomatico rc = new ResumenCobroAutomatico(v.getCodigoterminal(), 
					v.getCodigoafiliado(), v.getNombreterminal(), 
					v.getCodcomp(), v.getMpbmon(), datos_caja.getId().getCaid() ) ;
			
			//&& =================  aplicar el cierre a la terminal del socketpos
			boolean cerrar = true;
			
			if(cerrar){
				strMensajeProceso =  aplicarCierreTerminal(rc, transaccionesSocket, vautoriz, cuentasNotificaciones) ;
				
				if(!strMensajeProceso.isEmpty()){
					return;
				} 
			}
			

			
		} catch (Exception e) {
			strMensajeProceso = "No se ha podido aplicar el cargo a la cuota " ;
			e.printStackTrace(); 
		}finally{
			
			//&& Verificar si es un mensaje de debito automatico bloqueo
			if(establoqueadodebito==1)
			{
				dwConfirmarProcesoCuotaIndividual.setWindowState("hidden");
				dwMensajesValidacion.setWindowState("normal");
				lblMensajeValidacion.setValue(strMensajeProceso);
				
				if(lstCuotasPendientesDebitos == null){
					lstCuotasPendientesDebitos = new ArrayList<Vwbitacoracobrospmt>();
				}
				
				CodeUtil.refreshIgObjects(new Object[]{ dwMensajesValidacion, lblMensajeValidacion, dwConfirmarProcesoCuotaIndividual, gvCuotasPendientesDebitos} );
				
				return;
			}
			//------------------------------------------------------
			
			Vwbitacoracobrospmt[] cobro = {v};
			
			int idemision = Integer.parseInt( ( datos_caja.getId().getCaid()  +""+ numeroArqueo ) ) ;
			String parentrowid = datos_caja.getId().getCaid() +"" + numeroArqueo
					+ FechasUtil.formatDatetoString( fechaArqueo, "ddMMyyyy")
	    			+ FechasUtil.formatDatetoString( fechaArqueo, "HHmmss");
			
			crearReporteResumenCuotas1("Débitos Automáticos", "Resultado de Cobro Individual",  
					Arrays.asList(cobro), cuentasNotificaciones, idemision, parentrowid );
			
			if (strMensajeProceso.isEmpty()) {
				strMensajeProceso = "Se ha aplicado correctamente el cobro de cuota al cliente ";
			} 
			
			ClsParametroCaja clsPC = new ClsParametroCaja();
			if(!clsPC.setParametros("06", "0", "DEBAUT", null, 0, null))
			{
				strMensajeProceso = "Error al intentar desbloquear proceso de pago";
			}
			
			dwConfirmarProcesoCuotaIndividual.setWindowState("hidden");
			dwMensajesValidacion.setWindowState("normal");
			lblMensajeValidacion.setValue(strMensajeProceso);
			
			if(lstCuotasPendientesDebitos == null){
				lstCuotasPendientesDebitos = new ArrayList<Vwbitacoracobrospmt>();
			}
			
			
			CodeUtil.putInSessionMap("debatm_lstCuotasPendientesDebitos" , lstCuotasPendientesDebitos);
			gvCuotasPendientesDebitos.dataBind();
			gvCuotasPendientesDebitos.setPageIndex(0);
			
			CodeUtil.refreshIgObjects(new Object[]{ dwMensajesValidacion, lblMensajeValidacion, dwConfirmarProcesoCuotaIndividual, gvCuotasPendientesDebitos} );
		}
	}
	
	public void cerrarCobroCuotaIndividual(ActionEvent ev){
		CodeUtil.removeFromSessionMap("dbatm_CuotaSeleccionadaProcesar");
		dwConfirmarProcesoCuotaIndividual.setWindowState("hidden");
	}
	
	
	public void mostraConfirmacionCobroCuotaIndividual(ActionEvent ev){
		String strMessage = "" ;
		String codigocliente = "0" ; 
		
		try {
		
			strMessage = PosCtrl.credomatic_SocketPos_TestConnection() ;
			if( !strMessage.isEmpty() ) {
				return ;
			}
			
			RowItem ri = (RowItem) ev.getComponent().getParent().getParent();
			final Vwbitacoracobrospmt v  = (Vwbitacoracobrospmt) DataRepeater.getDataRow(ri);
			
			CodeUtil.removeFromSessionMap("dbatm_CuotaSeleccionadaProcesar");
			
			codigocliente = String.valueOf( v.getMpbcli() ) ;
			
			int caid = Integer.parseInt( CodeUtil.getFromSessionMap(("sCajaId") ).toString().trim() );
			lstResumenCobrosAutomaticosProcesar = DebitosAutomaticosPmtCtrl.generarResumenTransaccionesProcesar(caid);
			
			if(lstResumenCobrosAutomaticosProcesar == null ){
				strMessage = "No se encontró datos de configuración de afiliados para aplicar cobro";
				return;
			}
			
			ResumenCobroAutomatico rac = (ResumenCobroAutomatico)
			CollectionUtils.find(lstResumenCobrosAutomaticosProcesar, new Predicate(){
				public boolean evaluate(Object o) {
					ResumenCobroAutomatico rac = (ResumenCobroAutomatico)o;
					return v.getCodcomp().trim().compareTo(rac.getCompaniaCodigo().trim()) == 0 && v.getMpbmon().compareTo(rac.getMoneda())  == 0 ;
				}
			} );
			
			
			if(rac.getCodigoterminal().trim().compareTo("0") == 0 ){
				strMessage = "No se encuentra configurada terminal de POS "+rac.getCodigoafiliado()+" para la Compañía " 
						+ rac.getCompaniaNombre().trim() +" en moneda " + rac.getMoneda() ;
				return;
			}
			
			
			v.setCodigoafiliado(rac.getCodigoafiliado().trim() );
			v.setCodigoterminal(rac.getCodigoterminal().trim() );
			v.setNombreterminal(rac.getNombreterminal());
			
			
			//&& ============ validar que no hayan cierres pendientes para la terminal configurada
			List<Transactsp> pendientes = PosCtrl.cobrosSocketPosPendientes(v.getCodigoterminal().trim(), 0) ;
			if(  pendientes != null && !pendientes.isEmpty()){
				strMessage = "No se puede proceder  Existen transacciones pendientes de cerrar para la terminal " + v.getCodigoterminal() ; 
				return;
			}

			CodeUtil.putInSessionMap( "dbatm_CuotaSeleccionadaProcesar", v);			
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}finally{
			
			if(strMessage.isEmpty()){
				dwConfirmarProcesoCuotaIndividual.setWindowState("normal");
				lblMensajeConfirmacionCobroCuotaIndividual.setValue("¿ Confirma aplicar cobro de cuota al cliente "+ codigocliente + "? ");
				
			}else{
				dwMensajesValidacion.setWindowState("normal");
				lblMensajeValidacion.setValue(strMessage);
			}
			
			CodeUtil.refreshIgObjects(new Object[]{dwMensajesValidacion, dwConfirmarProcesoCuotaIndividual});
			
		}
	}
	
	public void actualizarListadoCuotasPendientes(ActionEvent ev){
		try {

			CodeUtil.removeFromSessionMap("debatm_lstCuotasPendientesDebitos")  ;
			
			lstCuotasPendientesDebitos = getLstCuotasPendientesDebitos() ;
		 
			gvCuotasPendientesDebitos.dataBind();
			gvCuotasPendientesDebitos.setPageIndex(0);
			
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}
	
	public void confirmarProcesarNivelResumen(ActionEvent ev){
		try {
			
			RowItem ri = (RowItem) ev.getComponent().getParent().getParent();
			ResumenCobroAutomatico r = (ResumenCobroAutomatico) DataRepeater.getDataRow(ri);
			
			CodeUtil.putInSessionMap("debatm_ResumenCobroAplicar", r);
			
			
			dwConfirmacionProcesoDebitosPorNivel.setWindowState("normal");
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}
	public void cerrarConfirmacionProcesarCobrosAutoPorNivel(ActionEvent ev){
		try {
			
			CodeUtil.removeFromSessionMap("debatm_ResumenCobroAplicar" ) ;
			
			dwConfirmacionProcesoDebitosPorNivel.setWindowState("hidden");
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public void procesarCobrosAutomaticosPorNivel(ActionEvent ev){
		String strMensajeProceso = ""; 
		
		Vautoriz vautoriz = null;
		Vf55ca01 datos_caja = null;
		
		int contadorCobrosProcesados = 0 ;
		boolean procesados = true;
		
		Date dtHoraInicia = new Date();
		Date dtHoraFinaliza = new Date();
		
		List<Vwbitacoracobrospmt> cuotasProcesar = null;
		
		int cantidadCuotasProcesar = 0 ;
		int cantidadCuotasProcesadas = 0 ;
		List<String[]> cuentasNotificaciones = null;
		List<String[]> cuentasNotificacionesCobros = null;
		
		int numeroArqueo = 0;
		Date fechaArqueo = new Date();
		
		int establoqueadodebito=0;
		try {
			
			ClsParametroCaja clsPC = new ClsParametroCaja();
			
			CajaParametro lstCP = clsPC.getParametros("06", "0", "DEBAUT");
			if(lstCP.getValorNumerico()==1)
			{ 
				strMensajeProceso = "Debito automático se esta procesando";
				establoqueadodebito=1;
						
				return;
			}
			
			//¿ Procesar la lista completa de cobros ?
			if(!clsPC.setParametros("06", "0", "DEBAUT", null, 1, null))
			{
				strMensajeProceso = "No se pudo bloquear proceso de pago";
				establoqueadodebito=1;
				return;
			}
			
			final ResumenCobroAutomatico r = (ResumenCobroAutomatico) CodeUtil.getFromSessionMap("debatm_ResumenCobroAplicar");
			
			lstCuotasPendientesDebitos =  (ArrayList<Vwbitacoracobrospmt>) CodeUtil.getFromSessionMap("debatm_lstCuotasPendientesDebitos");
			
			if(lstCuotasPendientesDebitos == null || lstCuotasPendientesDebitos.isEmpty()){
				strMensajeProceso = "No hay datos a procesar";
				return;
			}

			cuotasProcesar = (ArrayList<Vwbitacoracobrospmt>)
			CollectionUtils.select(lstCuotasPendientesDebitos, new Predicate(){
				@Override
				public boolean evaluate(Object o) {
					Vwbitacoracobrospmt v = (Vwbitacoracobrospmt)o;
					return
					r.getMoneda().compareTo(v.getMpbmon() ) == 0 && 
					r.getCompaniaCodigo().trim().compareTo(v.getCodcomp().trim() ) == 0 ;
				}
			});
		
			int indexCuota = -1; 
			PmtDtCobroAutomatico[] cuotasDtaProceso = new PmtDtCobroAutomatico[cuotasProcesar.size()];
			
			vautoriz = ((Vautoriz[])CodeUtil.getFromSessionMap("sevAut") )[0];
			datos_caja = ((List<Vf55ca01>) CodeUtil.getFromSessionMap("lstCajas")).get(0);
			
			//&& ============ notificaciones de proceso por correo
			List<List<String[]>> cuentasEnviosCorreo = codigosNotificar(datos_caja, vautoriz.getId().getCodreg() );
			cuentasNotificaciones =  cuentasEnviosCorreo.get(0);
			cuentasNotificacionesCobros = cuentasEnviosCorreo.get(1);
			
			generarObjConfigComp(r.getCompaniaCodigo().trim(), datos_caja.getId().getCaid() );
			
			List<Vwbitacoracobrospmt> cuotasAplicadas = new ArrayList<Vwbitacoracobrospmt>();
			List<Transactsp> transaccionesSocket = new ArrayList<Transactsp>();
			
			//Liberar la session por el proceso
			HibernateUtilPruebaCn.closeSession();
			 for (int i = 0; i < cuotasProcesar.size(); i++) {

				 strMensajeProceso = "" ;
				 
				 Vwbitacoracobrospmt v = cuotasProcesar.get(i);
				 
				 PmtDtCobroAutomatico p = new  PmtDtCobroAutomatico( 
						 v.getMpbcli(), v.getMpbnctto(), v.getMpbnpag(), v.getMontocobrarentarjeta(), 
						 v.getMpbmon(), v.getMpbfcgnr(), v.getMpbnumrec(), 
						 v.getNumerotarjeta4d(), v.getCodigoterminal(), 
						 Long.parseLong( v.getReferenceNumber().trim()  ) , false, 
						 v.getRowcount() ) ;
				 
				 p.setMonedaCuota("USD");
				 p.setMontoCuota(v.getMpbvpag() );
				 
				 cuotasDtaProceso[ (++indexCuota) ] = p;
				 
				 
				 if(v.isRegistroprocesado()){
					 continue;
				 }
				 	
				 //&& ================= aplicar el cobro en credomatic
				 strMensajeProceso = procesarPagoSocketPos(v, datos_caja.getId().getCaid(), vautoriz.getId().getCodreg() );
				 
				 if( !strMensajeProceso.isEmpty() ){
					
					 v.setEstadoProceso("Error");
					 v.setObservaciones(strMensajeProceso);
					 v.setErrorpagotarjeta(true);
					 v.setRegistroprocesado(false);
					 
					 DebitosAutomaticosPmtCtrl.crearIncidenciaPagosCuotas(null, v, "01");
					 
					 cuotasDtaProceso[indexCuota].setObservaciones(strMensajeProceso);
					 
					 cuotasDtaProceso[indexCuota].setCodigorespuestasp( v.getResponseCode() );
					 cuotasDtaProceso[indexCuota].setDescripcionrespuestasp(v.getResponseCodeDescription());

					 
					 continue;
				 }
				 
				 cuotasDtaProceso[indexCuota].setNumerovoucher( Long.parseLong( v.getReferenceNumber().trim() ) );
				 
				 //&& ================= grabar el recibo en caja y el  pasivo en Edward's
				 strMensajeProceso =  procesarReciboCaja(v, vautoriz, datos_caja, transaccionesSocket) ;

				 if( !strMensajeProceso.isEmpty() ){
					 anularPagoSocketPos(v);
					 v.setEstadoProceso("Error");
					 v.setObservaciones(strMensajeProceso);
					 v.setRegistroprocesado(false);
					 
					 cuotasDtaProceso[indexCuota].setObservaciones(strMensajeProceso);
					 cuotasDtaProceso[indexCuota].setCodigorespuestasp( v.getResponseCode() );
					 cuotasDtaProceso[indexCuota].setDescripcionrespuestasp(v.getResponseCodeDescription());
					 
					 continue;
				 }
				 
				 cuotasDtaProceso[indexCuota].setNumerorecibo( v.getMpbnumrec() );
				 cuotasDtaProceso[indexCuota].setEstado(true);
				 cuotasDtaProceso[indexCuota].setObservaciones( "Cobro Aprobado con autorizado # " + v.getAuthorizationNumber()  );
				 
				 cuotasDtaProceso[indexCuota].setCodigorespuestasp( v.getResponseCode() );
				 cuotasDtaProceso[indexCuota].setDescripcionrespuestasp(v.getResponseCodeDescription());
				 
				 v.setRegistroprocesado(true);
				 v.setEstadoProceso("Aplicado");
				 v.setObservaciones( "Cobro Aprobado con autorizado # " + v.getAuthorizationNumber() );
				 
				 contadorCobrosProcesados++;
				 
				 cuotasAplicadas.add(v);
				 
			}
			 
			//&& ============ notificaciones de proceso por correo
			DebitosAutomaticosPmtCtrl.notificacionCorreoCuotaAplicada( cuotasAplicadas, cuentasNotificacionesCobros  );
			 
			//&& ================= verificar la notificacion por correo.
			for (PmtDtCobroAutomatico dtaPrc : cuotasDtaProceso) {
				final long rowid = dtaPrc.getRowcountBitacora();
				
				Vwbitacoracobrospmt v = (Vwbitacoracobrospmt)
				CollectionUtils.find(cuotasAplicadas, new Predicate(){
					public boolean evaluate(Object o) {
						Vwbitacoracobrospmt v = (Vwbitacoracobrospmt)o;
						return v.getRowcount() == rowid ;
					}
				});
				if(v == null){
					dtaPrc.setNotificacioncliente(false); 
					continue;
				}
				dtaPrc.setNotificacioncliente( v.isNotificado() );
			}
			 
			//&& ================= crear el historico de la ejecucion del proceso
			 
			cantidadCuotasProcesar = cuotasProcesar.size() ;
			cantidadCuotasProcesadas = contadorCobrosProcesados ;
			 
			PmtMtCobroAutomatico mt = 
				new PmtMtCobroAutomatico(vautoriz.getId().getCodreg(), 
					datos_caja.getId().getCaid(), 
					cantidadCuotasProcesar, 
					cantidadCuotasProcesadas, 
					dtHoraInicia, dtHoraInicia, new Date(), 
					PropertiesSystem.getDataFromPcClient() ) ;
			
			registrarBitacoraCobroAutomatico(mt, cuotasDtaProceso) ;
			
			 
			r.setCantidadprocesadas(contadorCobrosProcesados);
			r.setCantidadexcluidas( Math.abs( cuotasProcesar.size() - contadorCobrosProcesados) ) ;
			r.setProcesado(false);
			
			if(contadorCobrosProcesados == 0 ){
				strMensajeProceso = " No ha podido aplicar el cobro de la cuota ";
				r.setObservaciones(strMensajeProceso);
				r.setProcesado(false);
				return ;
			} 
			
			//&& =================  aplicar el cierre de caja.  
			dtHoraFinaliza = new Date();
			
			strMensajeProceso = aplicarCierreCaja(cuotasAplicadas, cuentasNotificaciones, dtHoraInicia, dtHoraFinaliza, vautoriz, datos_caja );
			
			Object[] dtaArqueo = (Object[]) CodeUtil.getFromSessionMap("debatm_DatosArqueo");
			numeroArqueo = Integer.parseInt( String.valueOf( dtaArqueo[0] ) ) ;
			fechaArqueo = (Date) dtaArqueo[1];
			
			
			//&& =================  aplicar el cierre a la terminal del socketpos
			strMensajeProceso =  aplicarCierreTerminal(r, transaccionesSocket, vautoriz, cuentasNotificaciones) ;
			
			r.setObservaciones(strMensajeProceso);
			r.setProcesado(true);
			
			//&& ================= sacar de la lista de pendientes las cuotas que si se cobraron
			lstCuotasPendientesDebitos = (List<Vwbitacoracobrospmt>) CollectionUtils.subtract(lstCuotasPendientesDebitos, cuotasAplicadas);
			
			//&& ============ notificaciones a los clientes que no se les pudo aplicar el cargo
			List<Vwbitacoracobrospmt> notificacionesNoAplicado =  (List<Vwbitacoracobrospmt>) CollectionUtils.subtract(cuotasProcesar, cuotasAplicadas);   
			if(notificacionesNoAplicado != null && !notificacionesNoAplicado.isEmpty() ) {
				DebitosAutomaticosPmtCtrl.notificacionCobroNoAplicado(notificacionesNoAplicado);
			}
			
			
		} catch (Exception e) {
			 e.printStackTrace(); 
			 procesados  = false;
			 strMensajeProceso = "Error en el procedimiento de cobros por cliente";
		}finally{
			
			//&& Verificar si es un mensaje de debito automatico bloqueo
			if(establoqueadodebito==1)
			{
				dwConfirmarProcesoCuotaIndividual.setWindowState("hidden");
				dwMensajesValidacion.setWindowState("normal");
				lblMensajeValidacion.setValue(strMensajeProceso);
				
				if(lstCuotasPendientesDebitos == null){
					lstCuotasPendientesDebitos = new ArrayList<Vwbitacoracobrospmt>();
				}
				
				CodeUtil.refreshIgObjects(new Object[]{ dwMensajesValidacion, lblMensajeValidacion, dwConfirmarProcesoCuotaIndividual, gvCuotasPendientesDebitos} );
				
				return;
			}
			//------------------------------------------------------
			
			@SuppressWarnings("unused")
			String strDebsAuto = "debsAuto " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS").format(new Date() ) ;			
			int idemision = Integer.parseInt( ( datos_caja.getId().getCaid()  +""+ numeroArqueo ) ) ;
			String parentrowid = datos_caja.getId().getCaid() +"" + numeroArqueo
					+ FechasUtil.formatDatetoString( fechaArqueo, "ddMMyyyy")
	    			+ FechasUtil.formatDatetoString( fechaArqueo, "HHmmss");
			
			if( procesados ){
				
				strMensajeProceso = "Se ha finalizado el proceso correctamente: " 
								+ contadorCobrosProcesados +" Procesados," 
								+ (cuotasProcesar.size() - contadorCobrosProcesados) + " no procesados ";
				
			}else{
				
				strMensajeProceso = "No se ha podido completar el proceso de aplicar cobros automaticos " ;
				
			}			
			
			// ==========================================================
			try {
				dwConfirmacionProcesoDebitosPorNivel.setWindowState("hidden");
				dwMensajesValidacion.setWindowState("normal");
				lblMensajeValidacion.setValue(strMensajeProceso);
			}catch(Exception e) {
				 e.printStackTrace(); 
			}
			
			try {
				dwConfirmacionProcesoDebitos.setWindowState("hidden");
				dwResumenProcedimientoCobroAutomatico.setWindowState("hidden");
			}catch(Exception e) {
				 e.printStackTrace(); 
			}
			
			ClsParametroCaja clsPC = new ClsParametroCaja();
			if(!clsPC.setParametros("06", "0", "DEBAUT", null, 0, null))
			{
				strMensajeProceso = "Error al intentar desbloquear proceso de pago";
			}
			
			try {
				CodeUtil.removeFromSessionMap( "debatm_lstCuotasPendientesDebitos");
				lstCuotasPendientesDebitos =  getLstCuotasPendientesDebitos();
				gvCuotasPendientesDebitos.dataBind();
				gvCuotasPendientesDebitos.setPageIndex(0);
			}catch(Exception e) {
				 e.printStackTrace(); 
			}
			try {
				CodeUtil.refreshIgObjects(new Object[] { dwMensajesValidacion,
				                     					dwConfirmacionProcesoDebitosPorNivel, lblMensajeValidacion,
				                     					gvCuotasPendientesDebitos,
				                     					dwResumenProcedimientoCobroAutomatico });
			}catch(Exception e) {
				 e.printStackTrace(); 
			}
			
			try {
				crearReporteResumenCuotas1("Débitos Automáticos", "Resultado de Cobros por Compañía y Moneda", 
						cuotasProcesar, cuentasNotificaciones,idemision, parentrowid );
			}catch(Exception e) {
				 e.printStackTrace(); 
			}
			
			// ==========================================================			
			CodeUtil.removeFromSessionMap("debatm_ResumenCobroAplicar" ) ;

		}
	}
	
	@SuppressWarnings("unchecked")
	public void procesarCobrosAutomaticos(ActionEvent ev){
		
		String strMensajeProceso = "" ;
		int contadorCobrosProcesados = 0 ;
		
		Vautoriz vautoriz = null;
		Vf55ca01 datos_caja = null;
		
		boolean procesados = true;
		
		Date dtHoraInicia = new Date();
		Date dtHoraFinaliza = new Date();
		
		int cantidadCuotasProcesar = 0 ;
		int cantidadCuotasProcesadas = 0 ;
		List<Vwbitacoracobrospmt> todasCuotasAplicadas= new ArrayList<Vwbitacoracobrospmt>();
		List<Vwbitacoracobrospmt> cuotasReporte = new ArrayList<Vwbitacoracobrospmt>();
		List<Vwbitacoracobrospmt> cuotasNoAplicadas = new ArrayList<Vwbitacoracobrospmt>();
		
		List<String[]> cuentasNotificaciones = null;
		@SuppressWarnings("unused")
		List<String[]> cuentasNotificacionesCobros = null;
		
		int establoqueadodebito=0;
		try {
			
			ClsParametroCaja clsPC = new ClsParametroCaja();
			
			CajaParametro lstCP = clsPC.getParametros("06", "0", "DEBAUT");
			if(lstCP.getValorNumerico()==1)
			{
				strMensajeProceso = "Debito automático se esta procesando";
				establoqueadodebito=1;
				return;
			}

			if(!clsPC.setParametros("06", "0", "DEBAUT", null, 1, null))
			{
				strMensajeProceso = "No se pudo bloquear proceso de pago";
				establoqueadodebito=1;
				return;
				
			}
			
			vautoriz = ((Vautoriz[])CodeUtil.getFromSessionMap("sevAut") )[0];
			datos_caja = ((List<Vf55ca01>) CodeUtil.getFromSessionMap("lstCajas")).get(0);
			
			lstResumenCobrosAutomaticosProcesar = (ArrayList<ResumenCobroAutomatico>) CodeUtil.getFromSessionMap("debatm_lstResumenCobrosAutomaticosProcesar");
			
			if(lstResumenCobrosAutomaticosProcesar == null || lstResumenCobrosAutomaticosProcesar.isEmpty() ){
				strMensajeProceso = "No hay datos a procesar";
				
				return;
			}			

			//&& ============ notificaciones de proceso por correo
			List<List<String[]>> cuentasEnviosCorreo = codigosNotificar(datos_caja, vautoriz.getId().getCodreg() );
			cuentasNotificaciones =  cuentasEnviosCorreo.get(0);
			cuentasNotificacionesCobros = cuentasEnviosCorreo.get(1);

			
			lstCuotasPendientesDebitos =  (ArrayList<Vwbitacoracobrospmt>) CodeUtil.getFromSessionMap("debatm_lstCuotasPendientesDebitos");
			
			if(lstCuotasPendientesDebitos == null || lstCuotasPendientesDebitos.isEmpty()){
				strMensajeProceso = "No hay datos a procesar";
				return;
			}
			
			int indexCuota = -1; 
			PmtDtCobroAutomatico[] cuotasDtaProceso = new PmtDtCobroAutomatico[lstCuotasPendientesDebitos.size()];
			
			
			//&& =================  Por cada row del resumen generar los cobros y su cierre de caja y terminal.
			for (final ResumenCobroAutomatico rc : lstResumenCobrosAutomaticosProcesar) {
				
				if(vautoriz == null || datos_caja == null) {
					strMensajeProceso = "Datos de Perfil de Usuario y Datos de caja no existen ";
					break;
				}
				
				
				generarObjConfigComp(rc.getCompaniaCodigo().trim(), datos_caja.getId().getCaid());
				
				List<Vwbitacoracobrospmt> cuotasProcesar = (ArrayList<Vwbitacoracobrospmt>)
					CollectionUtils.select(lstCuotasPendientesDebitos, new Predicate(){
						@Override
						public boolean evaluate(Object o) {
							Vwbitacoracobrospmt v = (Vwbitacoracobrospmt)o;
							return
							rc.getMoneda().compareTo(v.getMpbmon() ) == 0 && 
							rc.getCompaniaCodigo().trim().compareTo(v.getCodcomp().trim() ) == 0 ;
						}
					});
				
				List<Vwbitacoracobrospmt> cuotasAplicadas = new ArrayList<Vwbitacoracobrospmt>();
				List<Transactsp> transaccionesSocket = new ArrayList<Transactsp>();
				
				contadorCobrosProcesados = 0 ;
				
				//Liberar la session por el proceso
				HibernateUtilPruebaCn.closeSession();
				
				//&& ============== aplicar el cargo a cada uno de los registros.
				 for (int i = 0; i < cuotasProcesar.size(); i++) {
					 
					 strMensajeProceso = "" ;
					 
					 Vwbitacoracobrospmt v = cuotasProcesar.get(i);
					 
					 cuotasReporte.add(v);
					 
					 PmtDtCobroAutomatico p = new  PmtDtCobroAutomatico( 
							 v.getMpbcli(), v.getMpbnctto(), v.getMpbnpag(), v.getMontocobrarentarjeta(), 
							 v.getMpbmon(), v.getMpbfcgnr(), v.getMpbnumrec(), 
							 v.getNumerotarjeta4d(), v.getCodigoterminal(), 
							 Long.parseLong( v.getReferenceNumber().trim() ) , false,
							 v.getRowcount() ) ;

					 p.setMonedaCuota("USD");
					 p.setMontoCuota(v.getMpbvpag() );
					 
					 cuotasDtaProceso[ (++indexCuota) ] = p;
					 
					 if(v.isRegistroprocesado()){
						 continue;
					 }
					 	
					 //&& ================= aplicar el cobro en credomatic
					 strMensajeProceso = procesarPagoSocketPos(v, datos_caja.getId().getCaid(), vautoriz.getId().getCodreg() );
					 
					 if( !strMensajeProceso.isEmpty() ){
						
						 v.setEstadoProceso("Error");
						 v.setObservaciones(strMensajeProceso);
						 v.setErrorpagotarjeta(true);
						 v.setRegistroprocesado(false);
						 
						 cuotasDtaProceso[indexCuota].setObservaciones(strMensajeProceso);
						 cuotasDtaProceso[indexCuota].setCodigorespuestasp( v.getResponseCode() );
						 cuotasDtaProceso[indexCuota].setDescripcionrespuestasp(v.getResponseCodeDescription());
						 
						 DebitosAutomaticosPmtCtrl.crearIncidenciaPagosCuotas(null, v, "01");
						 
						 cuotasNoAplicadas.add(v);
						 
						 continue;
					 }
					 
					 cuotasDtaProceso[indexCuota].setNumerovoucher( Long.parseLong( v.getReferenceNumber().trim() ) );
					 
					 //&& ================= grabar el recibo en caja y el  pasivo en Edward's
					 strMensajeProceso =  procesarReciboCaja(v, vautoriz, datos_caja, transaccionesSocket) ;
					 
					 if( !strMensajeProceso.isEmpty() ){
						//lfonseca
						 anularPagoSocketPos(v);
						 v.setEstadoProceso("Error");
						 v.setObservaciones(strMensajeProceso);
						 v.setRegistroprocesado(false);
						 
						 cuotasDtaProceso[indexCuota].setObservaciones(strMensajeProceso);
						 cuotasDtaProceso[indexCuota].setCodigorespuestasp( v.getResponseCode() );
						 cuotasDtaProceso[indexCuota].setDescripcionrespuestasp(v.getResponseCodeDescription());
						 
						 continue;
					 }
					 
					 cuotasDtaProceso[indexCuota].setNumerorecibo( v.getMpbnumrec() );
					 cuotasDtaProceso[indexCuota].setEstado(true);
					 cuotasDtaProceso[indexCuota].setObservaciones( "Cobro Aprobado con autorizado # " + v.getAuthorizationNumber()  );

					 cuotasDtaProceso[indexCuota].setCodigorespuestasp( v.getResponseCode() );
					 cuotasDtaProceso[indexCuota].setDescripcionrespuestasp(v.getResponseCodeDescription());
					 
					 v.setRegistroprocesado(true);
					 v.setEstadoProceso("Aplicado");
					 v.setObservaciones( "Cobro Aprobado con autorizado # " + v.getAuthorizationNumber() );
					 
					 contadorCobrosProcesados++;
					 
					 cuotasAplicadas.add(v);
					 
				}
				 
				cantidadCuotasProcesar += cuotasProcesar.size() ;
				cantidadCuotasProcesadas += contadorCobrosProcesados ;
				 
				rc.setCantidadprocesadas(contadorCobrosProcesados);
				rc.setCantidadexcluidas( Math.abs( cuotasProcesar.size() - contadorCobrosProcesados) ) ;
				rc.setProcesado(false);
				
				if(contadorCobrosProcesados == 0 ){
					continue;
				} 
				
				//&& =================  aplicar el cierre de caja.  
				dtHoraFinaliza = new Date();
				
				strMensajeProceso = aplicarCierreCaja(cuotasAplicadas, cuentasNotificaciones, dtHoraInicia, dtHoraFinaliza, vautoriz, datos_caja);

				//&& =================  aplicar el cierre a la terminal del socketpos
				strMensajeProceso =  aplicarCierreTerminal(rc, transaccionesSocket, vautoriz, cuentasNotificaciones);
				
				rc.setObservaciones(strMensajeProceso);
				rc.setProcesado(true);
				
				
				//&& ================= enviar el correo de notificacion de las cuotas que se aplicaron
				DebitosAutomaticosPmtCtrl.notificacionCorreoCuotaAplicada( cuotasAplicadas, cuentasNotificacionesCobros );
				
				//&& ================= sacar de la lista de pendientes las cuotas que si se cobraron
				lstCuotasPendientesDebitos = (List<Vwbitacoracobrospmt>) CollectionUtils.subtract(lstCuotasPendientesDebitos, cuotasAplicadas);
				
				todasCuotasAplicadas.addAll(cuotasAplicadas);
				
			}
			
			//&& ================= enviar el correo de notificacion de las cuotas que No se pudieron cobrar
			DebitosAutomaticosPmtCtrl.notificacionCobroNoAplicado(lstCuotasPendientesDebitos);
			
			//&& ================= verificar la notificacion por correo.
			for (PmtDtCobroAutomatico dtaPrc : cuotasDtaProceso) {
				final long rowid = dtaPrc.getRowcountBitacora();
				
				Vwbitacoracobrospmt v = (Vwbitacoracobrospmt)
				CollectionUtils.find(todasCuotasAplicadas, new Predicate(){
					public boolean evaluate(Object o) {
						Vwbitacoracobrospmt v = (Vwbitacoracobrospmt)o;
						return v.getRowcount() == rowid ;
					}
				});
				if(v == null){
					dtaPrc.setNotificacioncliente(false); 
					continue;
				}
				dtaPrc.setNotificacioncliente( v.isNotificado() );
			}
			
			
			//&& ================= crear el historico de la ejecucion del proceso
			PmtMtCobroAutomatico mt = 
				new PmtMtCobroAutomatico(vautoriz.getId().getCodreg(), 
					datos_caja.getId().getCaid(), 
					cantidadCuotasProcesar, 
					cantidadCuotasProcesadas, 
					dtHoraInicia, dtHoraInicia, new Date(), 
					PropertiesSystem.getDataFromPcClient() ) ;
			
			registrarBitacoraCobroAutomatico(mt, cuotasDtaProceso) ;
			
			
		} catch (Exception e) {
			 e.printStackTrace(); 
			 procesados  = false;
			 strMensajeProceso = "Error en el procedimiento de cobros por cliente";
		}finally{
			
			//&& Verificar si es un mensaje de debito automatico bloqueo
			if(establoqueadodebito==1)
			{
				dwConfirmarProcesoCuotaIndividual.setWindowState("hidden");
				dwMensajesValidacion.setWindowState("normal");
				lblMensajeValidacion.setValue(strMensajeProceso);
				
				if(lstCuotasPendientesDebitos == null){
					lstCuotasPendientesDebitos = new ArrayList<Vwbitacoracobrospmt>();
				}
				
				CodeUtil.refreshIgObjects(new Object[]{ dwMensajesValidacion, lblMensajeValidacion, dwConfirmarProcesoCuotaIndividual, gvCuotasPendientesDebitos} );
				
				return;
			}

			if( procesados ){
				
				strMensajeProceso = "Se ha finalizado el proceso correctamente: " 
								+ cantidadCuotasProcesadas +" Procesados," 
								+ ( cantidadCuotasProcesar - cantidadCuotasProcesadas) + " no procesados ";
			}else{
				
				strMensajeProceso = "No se ha podido completar el proceso de aplicar cobros automaticos " ;
				
			}

			ClsParametroCaja clsPC = new ClsParametroCaja();
			if(!clsPC.setParametros("06", "0", "DEBAUT", null, 0, null))
			{
				strMensajeProceso = "Error al intentar desbloquear proceso de pago";
			}
			
			try {
				dwMensajesValidacion.setWindowState("normal");
				lblMensajeValidacion.setValue(strMensajeProceso);
			}catch(Exception e) {
				 e.printStackTrace(); 
			}
			
			try {
				dwConfirmacionProcesoDebitos.setWindowState("hidden");
				dwResumenProcedimientoCobroAutomatico.setWindowState("hidden");
			}catch(Exception e) {
				 e.printStackTrace(); 
			}

			try {
				CodeUtil.removeFromSessionMap("debatm_lstCuotasPendientesDebitos");
				lstCuotasPendientesDebitos =  getLstCuotasPendientesDebitos();
				gvCuotasPendientesDebitos.dataBind();
				gvCuotasPendientesDebitos.setPageIndex(0);
			}catch(Exception e) {
				 e.printStackTrace(); 
			}
			try {
				CodeUtil.refreshIgObjects(new Object[]{ dwMensajesValidacion, dwConfirmacionProcesoDebitos, lblMensajeValidacion, gvCuotasPendientesDebitos, dwResumenProcedimientoCobroAutomatico} );
			}catch(Exception e) {
				 e.printStackTrace(); 
			}
			
			try {
				crearReporteResumenCuotas1("Débitos Automáticos", "Resumen Resultado Proceso Cobros", cuotasReporte,  cuentasNotificaciones, 0, "");
			}catch(Exception e) { 
				e.printStackTrace();
			}

			//&& ================= enviar el correo de notificacion de las cuotas que No se pudieron cobrar
			if(!cuotasNoAplicadas.isEmpty() ) {
				DebitosAutomaticosPmtCtrl.notificacionCobroNoAplicado(cuotasNoAplicadas);
			}
			
		}
	}
	
	public static void registrarBitacoraCobroAutomatico(PmtMtCobroAutomatico p, PmtDtCobroAutomatico[] cuotasDtaProceso){
		try {
			
			String sql = p.insertStatement();
			boolean aplicado = ConsolidadoDepositosBcoCtrl.executeSqlQueryTx(null, sql) ;
			
			if(!aplicado)
				return;
			
			sql = p.selectStatement();
			@SuppressWarnings("unchecked")
			final List<PmtMtCobroAutomatico> mt = (ArrayList<PmtMtCobroAutomatico>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, true, PmtMtCobroAutomatico.class);
			
			List<PmtDtCobroAutomatico> detalleBitacora = Arrays.asList(cuotasDtaProceso);
			
			CollectionUtils.forAllDo(detalleBitacora, new Closure(){
				public void execute(Object o) {
					PmtDtCobroAutomatico dt = (PmtDtCobroAutomatico)o;
					dt.setIdmtcobroauto( mt.get(0).getIdcobroauto() );
				}
			});
			
			CodeUtil.putInSessionMap("debatm_IdBitacoraProceso",  mt.get(0).getIdcobroauto());
			
			List<String> insertQueries = new ArrayList<String>();
			for (PmtDtCobroAutomatico dt : detalleBitacora) {
				insertQueries.add(dt.insertStatement());
			}
			
			ConsolidadoDepositosBcoCtrl.executeSqlQueries(insertQueries);
			
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}
	
	public String aplicarCierreTerminal( ResumenCobroAutomatico rc,  List<Transactsp> transacciones, Vautoriz vaut, List<String[]> cuentasNotificaciones ){
		String strMensaje =  "";
		
		Session sesion = null;
		Transaction trans = null;
		
		try {
			
			
			//&& ============ validar que no haya transacciones pendientes de revertir o con estado de aplicadas
			List<HistoricoCobrosSocketpos> pendientes = PosCtrl.listarCobrosRespuestaPendiente( rc.getCodigoterminal(), new Date() );

			if(pendientes != null && !pendientes.isEmpty()){
				return strMensaje = "No se ha podido aplicar el cierre a la terminal existen cobros pendientes para terminal "+ rc.getCodigoterminal() +" de confirmar y deben revertirse " ;
			}
			
			//&& ============ 1. Cerrar la terminal
			Response r =  cerrarTerminalSocketPos(rc.getCodigoterminal());
			if(r == null)
				return strMensaje = "No se ha podido aplicar el cierre a la terminal " + rc.getCodigoterminal() ;
			
			if(r.getResponseCode().compareTo("21") == 0 ){
				return strMensaje = "No se encontraron transacciones para la terminal " + rc.getCodigoterminal() ;
			}
			if( r.getResponseCode().compareTo( ResponseCodes.APROBADO.CODE ) != 0 ){
				return strMensaje = "No se ha podido aplicar el cierre a la terminal " + rc.getCodigoterminal() +" codigo de respuesta:  " + r.getResponseCode() ;
			}
			
			//&& ============ 2. Grabar cierre de Terminal
			sesion = HibernateUtilPruebaCn.currentSession();
			trans =  sesion.beginTransaction() ;
			
			int batchnumber = Integer.parseInt( "000000000" /*r.getSystemTraceNumber()*/ );
			int cantidadAprobadas = Integer.parseInt( r.getSalesTransactions().trim() ) ; 
			int cantidadAnuladas  = Integer.parseInt( r.getRefundsTransactions().trim() ) ;
			BigDecimal totalCierre = new BigDecimal(r.getSalesAmount()).divide(new BigDecimal("100") );
			BigDecimal totaldevolucion = new BigDecimal( r.getRefundsAmount() ).divide(new BigDecimal("100") );
			Date fecha_transaccion = new Date();
			
			TransaccionTerminal trm = new TransaccionTerminal(true, rc.getCodigoterminal(), rc.getCodigoafiliado().trim(), transacciones.size(),
					cantidadAprobadas, cantidadAnuladas, totalCierre,  rc.getNombreterminal(), fecha_transaccion, fecha_transaccion, 
					rc.getCompaniaCodigo().trim(), rc.getMoneda(), cantidadAprobadas, cantidadAnuladas, totalCierre, totaldevolucion, 
					transacciones, rc.getCodigocaja(), vaut.getId().getCoduser(), vaut.getId().getCodreg()) ;
			
			CierreSpos cs = new CierreSpos(trm.getTerminalid(),
					trm.getAfiliado(), batchnumber, trm.getTrans_fechaHasta(),
					trm.getTrans_fechaHasta(), cantidadAprobadas, totalCierre,
					transacciones.size(), "000000000"/* r.getReferenceNumber()*/, r.getAuthorizationNumber(),
					r.getResponseCode(), "00000000", /*r.getSystemTraceNumber(),*/ trm.getCaid(), trm.getCodcomp(),
					trm.getMoneda(), trm.getCoduser(), trm.getUsrCodreg(),
					trm.getTotalcierre(), totalCierre, cantidadAnuladas, totaldevolucion,
					fecha_transaccion );

			try {
				sesion.save(cs);
			} catch (Exception e) {
				e.printStackTrace();
				return strMensaje = "Error al guardar cierre de SocketPos en Caja ";
			}
			
			
			//&& ============ 3. Actualizar detalle de cierre de terminal
			
			String sql = " update " + PropertiesSystem.ESQUEMA
					+ ".Transactsp set stat_cred = 1, status = 'APL'"
					+ " where termid = '" + trm.getTerminalid()
					+ "' and status = 'PND' and stat_cred = 0";
			
			try {
				 sesion.createSQLQuery(sql).executeUpdate();
			} catch (Exception e) {
				e.printStackTrace(); 
				return strMensaje = "Error al actualizar detalle de transacciones de SocketPos ";
			}
			
			CollectionUtils.forAllDo(transacciones, new Closure(){
				public void execute(Object o) {
					Transactsp ts = (Transactsp)o;
					ts.setStatcred(1);
					ts.setStatus("APL");					
				}
			});
			
			
			//&& ============ 4. Generar el pdf con el detalle del cierre del Socket
			
			int iSufijo = Integer.parseInt((int) Math.round(Math.random() * 100) + "" + (int) Math.round(Math.random() * 10));
			
			String realpath = PropertiesSystem.RUTA_DOCUMENTOS_EXPORTAR ;
			
			String filename = "_CierreTerminal_"+trm.getTerminalid()+".pdf";
			
			trm.setRutarealreporte(realpath + "" +iSufijo +""+ filename);
			trm.setNombrereporte(+iSufijo+""+filename);
		 
			
			String[] strParts = PosCtrl.crearReporteCierre(trm);
			boolean documentoGenerado  =  (strParts != null);
			
			if(documentoGenerado){
				
				for (int i = 0; i < strParts.length; i++) {
					B64strfile b64StrPart = new B64strfile(strParts[i], i,
											cs.getIdcierrespos(), 68, 
											String.valueOf( cs.getIdcierrespos() ));
					try {
						sesion.save(b64StrPart);
					} catch (Exception e) {
						e.printStackTrace();  
						return strMensaje = "Error al guardar documento pdf  de transacciones de SocketPos ";
					}
				}

				DebitosAutomaticosPmtCtrl.enviarCierreSocketCajero(trm,  cuentasNotificaciones);

			}
		
			
		} catch (Exception e) {
			 
			e.printStackTrace();
			return strMensaje = "Error al guardar documento pdf  de transacciones de SocketPos ";
			
		}finally{
			
			if (strMensaje.isEmpty() && trans != null && trans.isActive() ){
				try {
					trans.commit();
				} catch (Exception e2) {
					strMensaje = "Error al confirmar transacciones de socket pos en gcpmcaja " ;
					e2.printStackTrace();  
				}
			}
			if ( !strMensaje.isEmpty() && trans != null	&& trans.isActive() ){
				try {
					trans.rollback();
				} catch (Exception e2) {
					e2.printStackTrace();  
				}
			}
			
			try {
				if( sesion != null && sesion.isOpen() )
					HibernateUtilPruebaCn.closeSession(sesion);
			} catch (Exception e2) {
				e2.printStackTrace();  
			}
		}
		
		return strMensaje;
	}
	
	@SuppressWarnings("unchecked")
	public String  aplicarCierreCaja(List<Vwbitacoracobrospmt> cuotasAplicadas, List<String[]> cuentasNotificaciones, 
							Date dtHoraInicio, Date dtHoraFinaliza, Vautoriz vautoriz, Vf55ca01 datos_caja  ){
		
		String strMensaje = "";
		boolean hecho = true ;
		ArqueorecCtrl arCtrl = new ArqueorecCtrl();
		ArqueoCajaCtrl acCtrl = new ArqueoCajaCtrl();
		
		Session sesion = null;
		Transaction trans = null;
		
		
		String codcomp ="" ;
		String moneda = "";
		
		int caid = 0; 
		int noarqueo = 0 ;
		BigDecimal TotalCobros = BigDecimal.ZERO ;
		
		Date fecha = new Date();
		
		try {
			
			CodeUtil.removeFromSessionMap("debatm_DatosArqueo");
			
			vautoriz = ((Vautoriz[])CodeUtil.getFromSessionMap("sevAut") )[0];
			datos_caja = ((List<Vf55ca01>) CodeUtil.getFromSessionMap("lstCajas")).get(0);
			
			caid = cuotasAplicadas.get(0).getMpbcaid();
			String codsuc = datos_caja.getId().getCaco() ;
			String coduser = cuotasAplicadas.get(0).getMpbhecam();
			
			codcomp = cuotasAplicadas.get(0).getMpbcodcomp().trim();
			moneda  = cuotasAplicadas.get(0).getMpbmon();
			
			Date dtHoraArprevio = null;		
			
			List<Object[]>lstRecibosCierre = new ArrayList<Object[]>();
			
			
			List<Integer> numrecibos = (List<Integer>) CodeUtil.selectPropertyListFromEntity(cuotasAplicadas, "mpbnumrec", true);
			for (Integer numrec : numrecibos) {
				lstRecibosCierre.add(new Object[]{numrec, TIPO_RECIBO_PMT});
			}
			
			
			//&& ================= Nueva conexion para cierre de caja 
			sesion = HibernateUtilPruebaCn.currentSession();
			trans = sesion.beginTransaction();
			
			//&& ================= leer la numeracion del arqueo para la caja
			@SuppressWarnings("static-access")
			Numcaja numcaja = new Divisas().obtenerNumeracionCaja("NARQUEO", caid, codcomp, codsuc, true, coduser,sesion);
			if(numcaja == null){
				return strMensaje = "Error: No se encuentra configurada numeración de arqueo para caja:" + caid  + ", Comp: "+codcomp ;
			}
			
			String sReferDep = "99999999";
			int codcajero = vautoriz.getId().getCodreg();
			
			BigDecimal ZERO = BigDecimal.ZERO;
			TotalCobros = CodeUtil.sumPropertyValueFromEntityList(cuotasAplicadas, "montocobrarentarjeta", false);
			 
			noarqueo = numcaja.getNosiguiente();
			hecho   = acCtrl.guardarArqueoCaja(noarqueo, codcajero, caid, 
						3, moneda, coduser,sReferDep, codcomp, codsuc,fecha, 
						fecha, fecha, fecha, ZERO, TotalCobros, ZERO, 
						ZERO, ZERO, ZERO, ZERO, ZERO, TotalCobros, sesion,trans);
			if(!hecho){
				return strMensaje = "Error al guardar Maestro de arqueo de caja";
			}
			
			//&& ================= Recibos del cierre.
			hecho = arCtrl.guardarRecibosArqueo(caid, codcomp, codsuc, noarqueo, fecha, moneda, dtHoraArprevio,	sesion, trans, lstRecibosCierre);
			if(!hecho){
				return strMensaje = "Error al guardar el registro de los  recibos incluidos en el arqueo";
			}
			
			CodeUtil.putInSessionMap("debatm_DatosArqueo", new Object[] {noarqueo, fecha} );
			
			
		} catch (Exception e) {
			e.printStackTrace();
			LogCajaService.CreateLog("aplicarCierreCaja", "ERR", e.getMessage());
		}finally{
			
			
			if(hecho){
				try{
					trans.commit();
				}catch(Exception ex){
					hecho = false;
					ex.printStackTrace(); 
					strMensaje = "No se ha podido registrar el cierre de caja para el proceso";
				}
			}
			if(!hecho){
				try {
					trans.rollback();
				} catch (Exception e) {
					e.printStackTrace(); 
				}
			}
			
			try {
				HibernateUtilPruebaCn.closeSession(sesion);
			} catch (Exception e) {
				e.printStackTrace(); 
			}
			
			if(hecho){
				
				
				String rutaReporte = ArqueoCajaDAO.generarReporteRecibosArqueo(noarqueo, caid, codcomp, fecha, dtHoraInicio, dtHoraFinaliza );
				
				DebitosAutomaticosPmtCtrl.generarReporteArqueoCaja(datos_caja, vautoriz, moneda, 
						codcomp, noarqueo, TotalCobros, fecha, cuentasNotificaciones, rutaReporte) ;
			}
			
		}
		
		return strMensaje;
		
	}
	
	/**
	 * Aplicar el cierre de la terminal y crear el objeto de maestro/detalle de cierre de socket pos en caja  
	 */
	public Response cerrarTerminalSocketPos(String terminalId){
		Response response = null;
		
		try {
			
			TransactionsSocketPosBac.transaction_type = TransactionType.SETTLEMENT.TYPE;
			TransactionsSocketPosBac.terminalid = terminalId ;
			TransactionsSocketPosBac.executeTransactionRequested();
			
			ExecuteTransactionResult tr = TransactionsSocketPosBac.transaction_result;
			
			if (tr == null) { 
				return null;
			}
			
			response = TransactionsSocketPosBac.response;
			
			/*
			response = new Response();
			response.setResponseCode("00");
			response.setSystemTraceNumber("12345678");
			response.setSalesTransactions("1");
			response.setRefundsTransactions("0"); 
			response.setSalesAmount("123456");
			response.setRefundsAmount("0"); 
			response.setAuthorizationNumber("12345678");
			response.setReferenceNumber("12345678");
			*/
			
		} catch (Exception e) {
			e.printStackTrace(); 
			response = null;
		} finally{
			
			TransactionsSocketPosBac.transaction_type = null;
			TransactionsSocketPosBac.terminalid = null;
		}
		
		return response;
	}
	
	public String anularPagoSocketPos(Vwbitacoracobrospmt v){
		String strMensajeProceso = "";
		
		try {

			TransactionsSocketPosBac.transaction_type = TransactionType.VOID.TYPE;
			TransactionsSocketPosBac.terminalid = v.getCodigoterminal().trim() ;
			TransactionsSocketPosBac.transaction_authorization_number = v.getAuthorizationNumber() ; //transaction_authorization_number ;
			TransactionsSocketPosBac.transaction_reference_number = v.getReferenceNumber(); //transaction_reference_number;
			TransactionsSocketPosBac.transaction_system_trace_number = v.getSystemTraceNumber() ; // transaction_system_trace_number ;
			
			boolean exec = TransactionsSocketPosBac.executeTransactionRequested();
			
			if(!exec){
				v.setResponseCode("XX");
				v.setResponseCodeDescription("ERROR AL ANULAR");
				LogCajaService.CreateLog("anularPagoSocketPos", "ERR", strMensajeProceso);
				return strMensajeProceso = "La transaccion por cobro en CREDOMATIC no se ha podido anular";
			}else
			{
				PosCtrl.setLogIntoTransactSPBanco(v.getReferenceNumber());
			}
			
			
			ExecuteTransactionResult tr = TransactionsSocketPosBac.transaction_result;
			 
			@SuppressWarnings("unused")
			Response r = TransactionsSocketPosBac.response;
			 
			if(tr.getResponseCode().compareTo( ResponseCodes.APROBADO.CODE ) != 0 ){
				v.setResponseCode(tr.getResponseCode());
				v.setResponseCodeDescription(tr.getResponseCodeDescription());
				return strMensajeProceso = "La transaccion por cobro en CREDOMATIC no se ha podido anular, codigo de respuesta: " + tr.getResponseCode();
			}
			
			v.setResponseCode(tr.getResponseCode());
			v.setResponseCodeDescription(tr.getResponseCodeDescription());
			
			//&& ================ marcar como anular las transacciones del SocketPos (Transactsp )
			 
			MetodosPago mp = v.metodosPagoDesdeCuota();
			mp.setStatuspago("ANL");
			PosCtrl.actualizarTransacciones(Arrays.asList(new MetodosPago[]{mp}), v.getMpbcaid(), v.getMpbcodcomp().trim(), 
					TIPO_RECIBO_PMT);

		} catch (Exception e) {
			strMensajeProceso = "Error al intentar aplicar la anulacion del pago" ;
			e.printStackTrace(); 
		}finally{
			
		}
		return strMensajeProceso;
	}
	
	public String procesarPagoSocketPos(Vwbitacoracobrospmt v, int caid, int coduser) {
		String strMensajeProceso = "";
		
		try {
			LogCajaService.CreateLog("generarCobroPos", "INFO", "generarPagoPOS - Inicio");
			Calendar cal= Calendar.getInstance();
			int anio= cal.get(Calendar.YEAR);
			int mes = cal.get(Calendar.MONTH);
			String strYYMM = String.valueOf(mes);
			if(mes<10)
				strYYMM = "0" + String.valueOf(mes);
			
			strYYMM = String.valueOf(anio-2000) + strYYMM;

			//&& ================== buscar alguna transaccion pendiente de revertir y si existe hacer la reversion
			PosCtrl.aplicarReversionPagosNoValidados( v.getCodigoterminal().trim(), new Date() ) ;
			
			//&& ================== crear el registro de control de las transacciones enviadas a credomatic
			v.setInvoicenumber( TransactionsSocketPosBac.createInvoiceNumber() ); 
			
			HistoricoCobrosSocketpos hc = new HistoricoCobrosSocketpos
				(caid, v.getMpbcodcomp().trim(),  v.getCodigoterminal().trim(), 
				v.getCodigoafiliado(), v.getInvoicenumber(), TransactionType.ENTRYMODE_MANUAL.TYPE, 
				v.getMontocobrarentarjeta(), String.valueOf( v.getNumerotarjeta4d() ) ,
				String.valueOf( v.getMpbvtrj() ), 0, "", PropertiesSystem.ESQUEMA, "PMTDA", coduser ) ;
			
			ConsolidadoDepositosBcoCtrl.executeSqlQueryTx(null,  hc.insertStatement() );
			
			if(Integer.parseInt(strYYMM)>v.getMpbvtrj())
			{
				Date date = new Date();
				String modifiedDate= new SimpleDateFormat("yyyy-MM-dd").format(date);
				
				v.setAuthorizationNumber("");
				v.setReferenceNumber("00000000");
				v.setResponseCode("51"); //00
				v.setResponseCodeDescription("TARJETA VENCIDA"); //APROBADO
				v.setSalesAmount( v.getMontocobrarentarjeta().toString() );
				v.setTransactionId(v.getInvoicenumber());
				v.setSystemTraceNumber(v.getInvoicenumber());
				v.setFechatransaccion(modifiedDate);
				
				hc.setStatus(1);
				hc.setResponsecode( "51" ) ;
				
				ConsolidadoDepositosBcoCtrl.executeSqlQueryTx( null, hc.updateStatment() );
				
				return strMensajeProceso = "No se ha podido aplicar el cobro a la tarjeta " + v.getNumerotarjetadsp()
				+ ",  Codigo de Razon: " + v.getResponseCode() + " > " + v.getResponseCodeDescription().trim();
			}
			else
			{
				
				ClsECommerceTransaccion clsECommerceT = new ClsECommerceTransaccion();
			    List<TransactBanco> lstTransactBanco = clsECommerceT.getTransaccionValidarTarjeta(PropertiesSystem.IPSERVERDB2, 
			    																				  PropertiesSystem.ESQUEMA, 
			    																				  PropertiesSystem.CN_USRNAME, 
			    																				  PropertiesSystem.CN_USRPWD, 
			    																				  String.valueOf(v.getMpbntrj()));
			    
			    
			    if(lstTransactBanco.size()>0)
			    {
			    	Date dUltimaTransaccion;
			    	for(Object o : lstTransactBanco)
			    	{
			    		TransactBanco tb = (TransactBanco)o;
			    		
			    		if(tb.getCodigoRespuesta()!=1)
			    		{
				    		//comparar fecha
				    		dUltimaTransaccion = FechasUtil.ConvertToDate2(tb.getFechaCreo());
				    		if((new Date()).compareTo(FechasUtil.addDaysToDate(dUltimaTransaccion, 4)) < 0)
				    		{
				    			Date date = new Date();
								String modifiedDate= new SimpleDateFormat("yyyy-MM-dd").format(date);
								
								v.setAuthorizationNumber("");
								v.setReferenceNumber("00000000");
								v.setResponseCode("51"); //00
								v.setResponseCodeDescription("INTENTO FALLIDO HACE MENOS DE 4 DIAS"); //APROBADO
								v.setSalesAmount( v.getMontocobrarentarjeta().toString() );
								v.setTransactionId(v.getInvoicenumber());
								v.setSystemTraceNumber(v.getInvoicenumber());
								v.setFechatransaccion(modifiedDate);
								
								hc.setStatus(1);
								hc.setResponsecode( "51" ) ;
								
								ConsolidadoDepositosBcoCtrl.executeSqlQueryTx( null,  hc.updateStatment() );
								
				    			return strMensajeProceso = "No se intento hacer cargo a la tarjeta en el banco, por intentos anteriores con error hace menos de 4 dias";
				    		}
			    		}
			    	}
			    }
				
				/*Solo para pruebas
				v.setAuthorizationNumber("038005");
				v.setReferenceNumber("038005");
				v.setResponseCode("00"); //00
				v.setResponseCodeDescription("APROBADO"); //APROBADO
				v.setSalesAmount( v.getMontocobrarentarjeta().toString() );
				v.setTransactionId("038005");
				v.setSystemTraceNumber("038005");
				v.setFechatransaccion("2023-01-24");
				
				hc.setStatus(1);
				hc.setResponsecode( "00" ) ;
				ConsolidadoDepositosBcoCtrl.executeSqlQueryTx(null, hc.updateStatment() );
				*/
				
				//&& ================== parametros de invocacion del webservice
				TransactionsSocketPosBac.terminalid =  v.getCodigoterminal().trim() ; //"EMVNIC01"; //
				TransactionsSocketPosBac.transaction_entry_mode = TransactionType.ENTRYMODE_MANUAL.TYPE;
				TransactionsSocketPosBac.transaction_type = TransactionType.SALE.TYPE;
				TransactionsSocketPosBac.transaction_invoice_number = v.getInvoicenumber() ;
				
				TransactionsSocketPosBac.transaction_amount = v.getMontocobrarentarjeta().doubleValue(); // 1.0d; //
				TransactionsSocketPosBac.transaction_card_number = String.valueOf( v.getMpbntrj() ); //"377701702258912"; //
				TransactionsSocketPosBac.transaction_card_expiration_date = String.valueOf( v.getMpbvtrj() ); // "2211"; //
	
				TransactionsSocketPosBac.executeTransactionRequested();
				ExecuteTransactionResult tr = TransactionsSocketPosBac.transaction_result;
				
				if(tr == null){
					
					v.setResponseCode("XX");
					v.setResponseCodeDescription("No se logra el intento de aplicar cobro a tarjeta ");
					
					return strMensajeProceso = "No se ha podido aplicar el cobro a la tarjeta " + v.getNumerotarjetadsp(); 
					
				}

				//&& ================== actualizar el estado la transaccion enviada a credomatic
				hc.setStatus(1);
				hc.setResponsecode( tr.getResponseCode() ) ;				
				
				ConsolidadoDepositosBcoCtrl.executeSqlQueryTx(null, hc.updateStatment() );
				
				//&& ================== continuar con el procedimiento de cobro
				
				Response rs =  TransactionsSocketPosBac.response;
				
				if( rs.getResponseCode().compareTo( ResponseCodes.APROBADO.CODE ) == 0 ){
					
					v.setAuthorizationNumber(tr.getAuthorizationNumber());
					v.setReferenceNumber(tr.getReferenceNumber());
					v.setResponseCode( tr.getResponseCode() );
					v.setResponseCodeDescription(tr.getResponseCodeDescription());
					v.setSalesAmount( v.getMontocobrarentarjeta().toString() );
					v.setTransactionId( tr.getTransactionId() );
					v.setSystemTraceNumber( tr.getSystemTraceNumber());
					v.setFechatransaccion( rs.getTransactionDate() );
					
				} else { 
					
					v.setAuthorizationNumber("");
					v.setReferenceNumber("00000000");
					v.setSystemTraceNumber(tr.getSystemTraceNumber() );
					
					v.setResponseCode( tr.getResponseCode() );
					v.setResponseCodeDescription( tr.getResponseCodeDescription() );
					
					ResultadoECommerce re = new ResultadoECommerce();
					re.setCodigoAutorizado(v.getAuthorizationNumber());
					re.setCodigoRespuesta(v.getResponseCode().compareTo(ResponseCodes.APROBADO.CODE)==0?1:2);
					re.setCodigoRespuesta2(v.getResponseCode());
					re.setDescripcionRespuesta(v.getResponseCodeDescription());
					re.setHash("");
					re.setHashRespuesta("");
					re.setMontoTransaccion(v.getMontocobrarentarjeta().doubleValue());
					re.setNumeroTarjeta(String.valueOf(v.getMpbntrj()));
					re.setOrderId(v.getInvoicenumber());
					re.setTransactionId(v.getReferenceNumber());
					re.setCantidadPuntosDisp(0.0d);
					re.setType("PMT");
					
					AccesoDB2 a = new AccesoDB2(PropertiesSystem.IPSERVERDB2, 
												PropertiesSystem.ESQUEMA, 
												PropertiesSystem.CN_USRNAME, 
												PropertiesSystem.CN_USRPWD, 
												String.valueOf(coduser), "PMT", 
												PropertiesSystem.CONTEXT_NAME, 
												PropertiesSystem.CONTEXT_NAME);
					
					try
					{
						clsECommerceT.setTransaccionInsert(a, String.valueOf(caid), "0", "", "", "", "0", "0", "0", re);
					}
					catch (Exception e) {
						// TODO: handle exception
					}
					
				
					return strMensajeProceso = "No se ha podido aplicar el cobro a la tarjeta " + v.getNumerotarjetadsp()
						+ ",  Codigo de Razon: " + tr.getResponseCode() + " > " + tr.getResponseCodeDescription().trim();
				}
				
				ResultadoECommerce re = new ResultadoECommerce();
				re.setCodigoAutorizado(v.getAuthorizationNumber());
				re.setCodigoRespuesta(v.getResponseCode().compareTo(ResponseCodes.APROBADO.CODE)==0?1:2);
				re.setCodigoRespuesta2(v.getResponseCode());
				re.setDescripcionRespuesta(v.getResponseCodeDescription());
				re.setHash("");
				re.setHashRespuesta("");
				re.setMontoTransaccion(Double.parseDouble(v.getSalesAmount()));
				re.setNumeroTarjeta(String.valueOf(v.getMpbntrj()));
				re.setOrderId(v.getInvoicenumber());
				re.setTransactionId(v.getTransactionId());
				re.setCantidadPuntosDisp(0.0d);
				re.setType("PMT");
				
				AccesoDB2 a = new AccesoDB2(PropertiesSystem.IPSERVERDB2, 
											PropertiesSystem.ESQUEMA, 
											PropertiesSystem.CN_USRNAME, 
											PropertiesSystem.CN_USRPWD, 
											String.valueOf(coduser), "PMT", 
											PropertiesSystem.CONTEXT_NAME, 
											PropertiesSystem.CONTEXT_NAME);
				
				try
				{
					clsECommerceT.setTransaccionInsert(a, String.valueOf(caid), "0", "", "", "", "0", "0", "0", re);
				}
				catch (Exception e) {
					// TODO: handle exception
				}
			} 
		} catch (Exception e) {
			
			v.setResponseCode("XX");
			v.setResponseCodeDescription(" error al procesar el cobro a la tarjeta  " + v.getNumerotarjetadsp());
			
			e.printStackTrace(); 
			strMensajeProceso = "No se ha podido aplicar el cobro " ; 
			LogCajaService.CreateLog("generarCobroPos", "ERR", e.getMessage());
		}
		
		LogCajaService.CreateLog("generarCobroPos", "INFO", "generarPagoPOS - Fin");
		
		return strMensajeProceso;
	}
	
	
	public String procesarReciboCaja(Vwbitacoracobrospmt v, Vautoriz vautoriz, Vf55ca01 datos_caja, List<Transactsp> transacciones ) {
		
		String strMensajeProceso = "" ;
		boolean aplicado = true;
		ReciboCtrl rc = new ReciboCtrl();
		
		Session session = null;
		Transaction transaction = null;
		
		String codcomp = "";
		String codsuc = "";
		String tiporec = TIPO_RECIBO_PMT;
		int caid = 0;
		int numrec = 0;
		List<MetodosPago> lstFormasDePagoRecibidas = null;
		
		try {

			LogCajaService.CreateLog("procesarReciboCaja", "INFO", "procesarReciboCaja-INICIO");
			
			caid = datos_caja.getId().getCaid();
			int clienteCodigo = v.getMpbcli() ; 
			int numrec_fcv = 0;
			
			BigDecimal bdMontoCambioExt = BigDecimal.ZERO;
			BigDecimal bdMontoCambioLocal = BigDecimal.ZERO;
			BigDecimal tasaOficial = tasaOficial();
			BigDecimal tasaParalela  = tasaParalela("USD");
			
			Date dtFechaGrabacion = new Date() ;	
			
			String strConceptoPago = "Debito Automatico PMT Contrato "+v.getMpbnctto()+" Cuota " + v.getMpbnpag();
			String strClienteNombre = v.getClientenombre().trim();
			String strCajeroNombre = vautoriz.getId().getEmpnombre().trim().toLowerCase();
			String strCajeroUsuario =  vautoriz.getId().getLogin() ;
			
			String codunineg = v.getCodunineg().trim();
			String strMonedaAplicada = v.getMpbmon();
			String monedaBaseCompania = monedaBaseActual();
			String monedaContrato = "COR";

			codsuc = datos_caja.getId().getCaco();			
			codcomp = v.getCodcomp().trim();
			
			//----   Agregado por lfonseca - 2019/01/21  ----			
			Object[] queryNumberContrato = PlanMantenimientoTotalCtrl.queryNumberContrato(v.getMpbnctto(), clienteCodigo, codcomp, "");
			
			if(queryNumberContrato == null){
				return strMensajeProceso = "-> No se ha encontrado datos asociados al número de Contrato " + v.getMpbnctto();
			}
			
			monedaContrato = String.valueOf(queryNumberContrato[12]).compareTo("")==0?"COR" : String.valueOf(queryNumberContrato[12]);
			strMonedaAplicada = monedaContrato;
			
			BigDecimal bdMontoaplicado = v.getMpbmon().compareTo(monedaBaseCompania)==0 ? 
					(strMonedaAplicada.compareTo(monedaBaseCompania)==0  ? 
						v.getMontocobrarentarjeta() : v.getMpbvpag()) : 
							(strMonedaAplicada.compareTo(monedaBaseCompania)==0 ? 
								v.getMontocobrarentarjeta().multiply(tasaOficial) : 
									v.getMontocobrarentarjeta());
		
			//-----------------------------------------------
			
			//&& ================= Crear el metodo de pago.
			MetodosPago mp = v.metodosPagoDesdeCuota(monedaContrato, tasaOficial);
			
			lstFormasDePagoRecibidas = new ArrayList<MetodosPago>() ;
			lstFormasDePagoRecibidas.add(mp);
			
			// && ============== establecer el numero de referencia
			CollectionUtils.forAllDo(lstFormasDePagoRecibidas, new Closure() {
				@Override
				public void execute(Object o) {
					MetodosPago mp = (MetodosPago) o;
					if (mp.getMetodo().compareTo(MetodosPagoCtrl.TRANSFERENCIA) == 0
							|| mp.getMetodo().compareTo(MetodosPagoCtrl.DEPOSITO) == 0
							|| mp.getMetodo().compareTo(MetodosPagoCtrl.CHEQUE) == 0) {
						mp.setReferencia(mp.getCodigobanco());
					}
				}
			});
			
			//&& =================== Conexiones ============================= &&//
			session = HibernateUtilPruebaCn.currentSession();
			transaction = session.beginTransaction();

			com.casapellas.controles.BancoCtrl.datosPreconciliacion(lstFormasDePagoRecibidas, caid, codcomp) ;

			numrec = com.casapellas.controles.tmp.ReciboCtrl.generarNumeroRecibo(caid, codcomp);
			if(numrec == 0){
				aplicado = false;
				strMensajeProceso = "Recibo no aplicado: Error al generar numero de recibo";
				throw new Exception(strMensajeProceso);
			}
			
			BitacoraSecuenciaReciboService.insertarLogReciboNumerico(caid,numrec,codcomp,codsuc,tiporec);
			
			//Cambio hecho por Luis, aun que considero malo,
			//dado a que la moneda aplicada deberia ser la del contrato.
			aplicado = rc.registrarRecibo(session, transaction, numrec,
								numrec, codcomp, v.getMontocobrarentarjeta().doubleValue(), v.getMontocobrarentarjeta().doubleValue(), 
								bdMontoCambioExt.doubleValue(), strConceptoPago, 
								dtFechaGrabacion, dtFechaGrabacion, clienteCodigo, strClienteNombre,
								strCajeroNombre, caid, codsuc, strCajeroUsuario, tiporec, 0, "",
								numrec_fcv, dtFechaGrabacion, codunineg, "", v.getMpbmon());
			
			if(!aplicado){
				strMensajeProceso = "Recibo no aplicado: Error al grabar Recibo";
				throw new Exception(strMensajeProceso);
			}
		
			aplicado = rc.registrarDetalleRecibo(session, transaction, numrec,  
					numrec, codcomp, lstFormasDePagoRecibidas,	
					caid, codsuc, tiporec);
			
			if(!aplicado){
				strMensajeProceso = "Recibo no aplicado: Error al grabar Detalle de recibo";
				throw new Exception(strMensajeProceso);
			}

			aplicado = rc.registrarCambio(session, transaction, numrec, codcomp,
						monedaBaseCompania, bdMontoCambioLocal.doubleValue(), caid, codsuc, 
						tasaOficial,tiporec);

			aplicado = rc.registrarCambio(session, transaction, numrec, codcomp,
					"USD", bdMontoCambioExt.doubleValue(), caid, codsuc, 
					tasaOficial,tiporec);

			if(!aplicado){
				strMensajeProceso = "Recibo no aplicado: Error al grabar cambios otorgados en el pago";
				throw new Exception(strMensajeProceso);
			}
			
			
			//&& =============== parametros de actualizacion del registro en sotmpba
			v.setMpbcodcomp(codcomp.trim());
			v.setMpbcodsuc(codsuc.trim());
			v.setMpbunineg(codunineg.trim());
			v.setMpbnumrec(numrec);
			v.setMpbtiporec(tiporec.trim());
			v.setMpbfecha(dtFechaGrabacion);
			v.setMpbcaid(caid);
			v.setMpbfecam(dtFechaGrabacion);
			v.setMpbhecam(strCajeroUsuario);
			v.setMpbhrcam(dtFechaGrabacion);
			v.setMpbpacam(PropertiesSystem.ESQUEMA);
			v.setMpbprcam(PropertiesSystem.ESQUEMA) ;			
			
			//&& =================== crear el registro de transaccion con socketpos
			Transactsp tsp = new Transactsp( v.getCodigoterminal(), v.getCodigoafiliado(), v.getSystemTraceNumber(),
					v.getAuthorizationNumber(), v.getNumerotarjetadsp().replace("*", "").trim(), v.getMontocobrarentarjeta(), v.getMpbvtrj().toString(),
					v.getReferenceNumber(), dtFechaGrabacion, dtFechaGrabacion, v.getMpbmon() ,
					v.getMpbcaid(), v.getMpbcodcomp(), v.getClientenombre(), v.getResponseCode(), "PND",
					v.getMpbtiporec(), 0);
			
			session.save(tsp);
			
			transacciones.add(tsp);
			
			//&& ============== crear las transacciones en JDE 
			PlanMantenimientoTotalCtrl.moneda_base = monedaBaseCompania;
			PlanMantenimientoTotalCtrl.moneda_aplicada = strMonedaAplicada;
			PlanMantenimientoTotalCtrl.codigo_compania = codcomp;
			PlanMantenimientoTotalCtrl.codigo_sucursal_usuario = v.getCodsuc();
			PlanMantenimientoTotalCtrl.codigo_unidad_negocio_usuario = v.getCodunineg().trim();
			PlanMantenimientoTotalCtrl.monto_aplicado = bdMontoaplicado;
			PlanMantenimientoTotalCtrl.diferencia = BigDecimal.ZERO;
			PlanMantenimientoTotalCtrl.tasaoficial = tasaOficial ;
			PlanMantenimientoTotalCtrl.tasaparalela = tasaParalela ;
			PlanMantenimientoTotalCtrl.codigo_caja = caid;
			PlanMantenimientoTotalCtrl.numero_recibo = numrec;
			PlanMantenimientoTotalCtrl.numero_contrato = v.getMpbnctto();
			PlanMantenimientoTotalCtrl.formas_de_pago = lstFormasDePagoRecibidas;
			PlanMantenimientoTotalCtrl.compra_venta_Cambio = null;
			PlanMantenimientoTotalCtrl.numero_recibo_fcv = numrec_fcv;
			
			PlanMantenimientoTotalCtrl.moneda_contrato = monedaContrato;
			PlanMantenimientoTotalCtrl.cuota_contrato_pmt_cod = v.getMpbvpag().multiply(tasaOficial);
			PlanMantenimientoTotalCtrl.cuota_contrato_pmt_usd = v.getMpbvpag();
			
			strMensajeProceso = PlanMantenimientoTotalCtrl.generarComprobantesContablesPMT(dtFechaGrabacion, clienteCodigo, vautoriz, v.getMpbnpag(), session );
			aplicado = strMensajeProceso.trim().isEmpty() ;
			
			if(!aplicado){
				throw new Exception(strMensajeProceso);
			}
			
			//&& ============== Grabar enlaces con jde
			ReciboCtrl recCtrl = new ReciboCtrl();
			List<String[]> numerosBatchDocs = PlanMantenimientoTotalCtrl.numeros_batch_documento;
			
			for (String[] recjde : numerosBatchDocs) {
				aplicado = recCtrl.fillEnlaceMcajaJde( session, transaction, Integer.parseInt(recjde[2].trim() ), codcomp, 
							Integer.parseInt(recjde[1].trim()), Integer.parseInt(recjde[0].trim()), caid, codsuc, recjde[3], recjde[4]);
				
				if(!aplicado){
					strMensajeProceso = "Error al guardar enlace entre caja y JdEdward's";
					throw new Exception(strMensajeProceso);
				}
			}
			
			//&& ============== conservar la asociacion de recibo por cliente y numero de contrato.
			String[] dtaBatchPv =  (String[])
			CollectionUtils.find(numerosBatchDocs, new Predicate(){
				@Override
				public boolean evaluate(Object o) {
					return ((String[])o)[5].compareTo("PV") == 0;
				}
			});
			
			String strSql = " insert into @GCPMCAJA.HISTORICO_RESERVAS_PROFORMAS " +
					"(CAID, CODCOMP, CODSUC, CODUNINEG, CODCLI, NUMREC, NUMEROPROFORMA, NUMEROBATCH, TIPODOC, " +
					" CODIGOCAJERO, MONEDAAPL, MONTORECIBO, TIPORECIBO, NUMEROCUOTA) "+
					" values "+
					"(@CAID, '@CODCOMP', '@CODSUC', '@CODUNINEG', @CODCLI, @NUMREC, @NUMEROPROFORMA, @NUMEROBATCH, '@TIPODOC', " +
					" @CODIGOCAJERO, '@MONEDAAPL', @MONTORECIBO, '@TIPORECIBO', @NUMEROCUOTA) ";
				
			strSql = strSql
				.replace("@GCPMCAJA",PropertiesSystem.ESQUEMA )
				.replace("@CAID", Integer.toString(caid) )
				.replace("@CODCOMP", codcomp)	
				.replace("@CODSUC",	 v.getCodsuc())
				.replace("@CODUNINEG",  codunineg)	
				.replace("@CODCLI",	Integer.toString(clienteCodigo) )
				.replace("@NUMREC", Integer.toString(numrec) )
				.replace("@NUMEROPROFORMA", Integer.toString( v.getMpbnctto() ) )	
				.replace("@NUMEROBATCH",  dtaBatchPv[0])	
				.replace("@TIPODOC", "PV")
				.replace("@CODIGOCAJERO", Integer.toString(vautoriz.getId().getCodreg() ) )
				.replace("@MONEDAAPL",	strMonedaAplicada)
				.replace("@MONTORECIBO", bdMontoaplicado.toString() )
				.replace("@TIPORECIBO", tiporec)
				.replace("@NUMEROCUOTA",  Integer.toString( v.getMpbnpag() ) );
			
			LogCajaService.CreateLog("procesarReciboCaja", "QRY", strSql);
			
			session.createSQLQuery(strSql).executeUpdate() ;
			
			//&& ========== Conservar el numero de batch
			v.setNumerobatchpago(Integer.parseInt( dtaBatchPv[0].trim() ) );
			
			
			//&& =================== actualizar el registro de la cuota en Sotmpba
			strMensajeProceso = DebitosAutomaticosPmtCtrl.actualizarRegistroCuota(session, v);
			aplicado = strMensajeProceso.isEmpty();
			
			if( !aplicado ){
				throw new Exception(strMensajeProceso);
			}
			
		} catch (Exception e) {
			LogCajaService.CreateLog("procesarReciboCaja", "ERR", e.getMessage());
			aplicado = false;
			strMensajeProceso = strMensajeProceso.trim().length() == 0 ?  "Error al procesar recibo de caja para pago de cuota" : strMensajeProceso;
			e.printStackTrace(); 
		}finally{
			
			try {
				
				if(aplicado) {
					transaction.commit();
					BitacoraSecuenciaReciboService.actualizarSatisfactorioLogReciboNumerico(caid,numrec,codcomp,codsuc,tiporec);
				}
			} catch (Exception e) {
				strMensajeProceso = "Error en confirmación de registro de valores: Caja, intente nuevamente";
				aplicado = false;
				e.printStackTrace();
			}
			
			try {
				if (!aplicado) {
					if (transaction != null) {
						transaction.rollback();
					}
				}
			} catch (Exception e) {
				strMensajeProceso = "Error en confirmación de registro de valores: Caja, intente nuevamente";
				aplicado = false;
				e.printStackTrace();
			}

			HibernateUtilPruebaCn.closeSession(session);
			
			LogCajaService.CreateLog("procesarReciboCaja", "INFO", "procesarReciboCaja-FIN");
		}
		
		return strMensajeProceso;
	}
	
	
	@SuppressWarnings("unchecked")
	public void  dwConfirmacionProcesoDebitos(ActionEvent ev){
		
		try {
			
			lstResumenCobrosAutomaticosProcesar = (ArrayList<ResumenCobroAutomatico>) CodeUtil.getFromSessionMap("debatm_lstResumenCobrosAutomaticosProcesar");
			if(lstResumenCobrosAutomaticosProcesar == null || lstResumenCobrosAutomaticosProcesar.isEmpty() ){
				return;
			}
			
			
			lblMensajeConfirmacionCobros.setValue(" ¿ Procesar la lista completa de cobros ? ");		
			dwConfirmacionProcesoDebitos.setWindowState("normal");
			
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}
	public void cerrarConfirmacionProcesarCobrosAuto(ActionEvent ev){
		dwConfirmacionProcesoDebitos.setWindowState("hidden");
		dwConfirmacionProcesoDebitos = null;
	}
	
	@SuppressWarnings("unchecked")
	public void excluirDebitoAutomatico(ActionEvent ev){
		try {
			RowItem ri = (RowItem) ev.getComponent().getParent().getParent();
			final Vwbitacoracobrospmt v = (Vwbitacoracobrospmt) DataRepeater.getDataRow(ri);
			 
			if( !v.getMpbsts1().trim().isEmpty() ){
				dwMensajesValidacion.setWindowState("normal");
				lblMensajeValidacion.setValue("El registro solo puede excluirse si se encuentra pendiente");
				CodeUtil.refreshIgObjects(dwMensajesValidacion);
				return;
			} 
			
			lstCuotasPendientesDebitos = (ArrayList<Vwbitacoracobrospmt>) CodeUtil.getFromSessionMap("debatm_lstCuotasPendientesDebitos");
			
			Vwbitacoracobrospmt onList = (Vwbitacoracobrospmt)
			CollectionUtils.find( lstCuotasPendientesDebitos, new Predicate(){
				@Override
				public boolean evaluate(Object o) {
					 return ((Vwbitacoracobrospmt)o).getRowcount() == v.getRowcount() ;
				}
			});
			
			onList.setExcluyeDebito( !onList.isExcluyeDebito() );
			onList.setExcluirDebito( (onList.isExcluyeDebito() ) ? "Si":"No") ;
			onList.setEstadoProceso( (onList.isExcluyeDebito() ) ? "Excluído": "Pendiente" ) ;
			
			int iPaginaActual = gvCuotasPendientesDebitos.getPageIndex();
			
			CodeUtil.putInSessionMap("debatm_lstCuotasPendientesDebitos", lstCuotasPendientesDebitos);
			gvCuotasPendientesDebitos.dataBind();
			gvCuotasPendientesDebitos.setPageIndex(iPaginaActual);
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}
	
	public void establecerConexionSocketPos(ActionEvent ev) {
		String strMensaje = "";

		try {

			strMensaje = PosCtrl.credomatic_SocketPos_TestConnection();

			if (strMensaje.trim().isEmpty()) {
				strMensaje = "Conexión establecida correctamente";
			}

		} catch (Exception e) {
			strMensaje = ">>> No se ha podido crear el enlace de conexión";
			e.printStackTrace(); 
		} finally {
			dwMensajesValidacion.setWindowState("normal");
			lblMensajeValidacion.setValue(strMensaje);
		}
	}
	
	
	public void cerrarVentanaMensajeValidacion(ActionEvent ev){
		dwMensajesValidacion.setWindowState("hidden");
	}
	
	public void cerrarResumenTransaccionesProcesar(ActionEvent ev){
		dwResumenProcedimientoCobroAutomatico.setWindowState("hidden");
	}
	
	@SuppressWarnings("unchecked")
	public void invocarProcesoCobrosAutomaticos(ActionEvent ev){
		String strMensajeValidacion = "" ;
		
		try {
			strMensajeValidacion = PosCtrl.credomatic_SocketPos_TestConnection() ;
			if( !strMensajeValidacion.isEmpty() ) {
				return ;
			}
			
			HibernateUtilPruebaCn.closeSession();
			
			int caid = Integer.parseInt( CodeUtil.getFromSessionMap(("sCajaId") ).toString() );
			lstResumenCobrosAutomaticosProcesar = DebitosAutomaticosPmtCtrl.generarResumenTransaccionesProcesar(caid);
			
			lstCuotasPendientesDebitos = (ArrayList<Vwbitacoracobrospmt>) CodeUtil.getFromSessionMap("debatm_lstCuotasPendientesDebitos");
			
			for (final ResumenCobroAutomatico  rac : lstResumenCobrosAutomaticosProcesar) {
				
				if(rac.getCodigoterminal().trim().compareTo("0") == 0 ){
					strMensajeValidacion = "No se encuentra configurada terminal de POS "+rac.getCodigoafiliado()+" para la Compañía " 
							+ rac.getCompaniaNombre().trim() +" en moneda " + rac.getMoneda() ;
					return;
				}
				
				//&& ============ validar que no hayan cierres pendientes para la terminal configurada
				List<Transactsp> pendientes = PosCtrl.cobrosSocketPosPendientes_cierrePOS( rac.getCodigoterminal().trim(), 0 ) ;
				if(  pendientes != null && !pendientes.isEmpty()){
					strMensajeValidacion = "No se puede proceder  Existen transacciones pendientes de cerrar para la terminal " + rac.getCodigoterminal() ; 
					return;
				}				
				
				List<Vwbitacoracobrospmt> tmpList = (ArrayList<Vwbitacoracobrospmt>)
				CollectionUtils.select(lstCuotasPendientesDebitos, new Predicate() {
					@Override
					public boolean evaluate(Object o) {
						Vwbitacoracobrospmt v = (Vwbitacoracobrospmt)o;
						return v.getCodcomp().trim().compareTo(rac.getCompaniaCodigo().trim()) == 0 && v.getMpbmon().compareTo(rac.getMoneda())  == 0 ;
					}
				} );
				CollectionUtils.forAllDo(tmpList, new Closure() {
					@Override
					public void execute(Object o ) {
						Vwbitacoracobrospmt v = (Vwbitacoracobrospmt)o;
						v.setCodigoafiliado(rac.getCodigoafiliado().trim() );
						v.setCodigoterminal(rac.getCodigoterminal().trim() );
						v.setNombreterminal(rac.getNombreterminal());
						v.setMpbfecam( new Date() ) ;
						v.setMpbhrcam( new Date() ) ;
					}
				}) ;
			}
			
			CodeUtil.putInSessionMap("debatm_lstCuotasPendientesDebitos", lstCuotasPendientesDebitos );
			
			
			CodeUtil.putInSessionMap("debatm_lstResumenCobrosAutomaticosProcesar", lstResumenCobrosAutomaticosProcesar);
			gvResumenCobrosAutomaticosProcesar.dataBind();

		} catch (Exception e) {
			strMensajeValidacion = "No se podido completar el proceso de resumen de transacciones ";
			e.printStackTrace(); 
		}finally{
			
			if(strMensajeValidacion.isEmpty()){
				dwResumenProcedimientoCobroAutomatico.setWindowState("normal");
			}else{
				dwMensajesValidacion.setWindowState("normal");
				lblMensajeValidacion.setValue(strMensajeValidacion);
				CodeUtil.refreshIgObjects(dwMensajesValidacion);
				return;
			}
		}
	}
	
	public void crearReporteResumenCuotas1(String titulo1, String titulo2, List<Vwbitacoracobrospmt> cuotas, 
			List<String[]> codigosNotificaciones, int idemision, String parentrowid ){
		try {
			
			String nombreDoc = "DebitosAutomaticos_"+new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date())+".xlsx";
			String sRutaFisica = PropertiesSystem.RUTA_DOCUMENTOS_EXPORTAR + nombreDoc;
			
			Rptmcaja015_CuotasPMT r = new  Rptmcaja015_CuotasPMT();
			r.cuotasPMT = cuotas;
			r.rutafisica = sRutaFisica;
			r.tituloReporte = titulo1 ;
			r.titulo2Reporte = titulo2 ;
			r.crearExcelTransaccionesCuotas();
			
			File reporte = new File (sRutaFisica);
			Long size = reporte.length();
			
			int retry = 0;
			while( size == 0 || retry > 2  ){
				reporte = null;
				 
				int delay  = Integer.parseInt( new Random().nextInt(2) + ""+ new Random().nextInt(1000) ); 
				Thread.currentThread();
				Thread.sleep(delay);
				
				retry ++ ;
				
				nombreDoc = "DebitosAutomaticos_"+new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date())+".xlsx";
				sRutaFisica = PropertiesSystem.RUTA_DOCUMENTOS_EXPORTAR + nombreDoc;
				
				r = new  Rptmcaja015_CuotasPMT();
				r.cuotasPMT = cuotas;
				r.rutafisica = sRutaFisica;
				r.tituloReporte = titulo1 ;
				r.titulo2Reporte = titulo2 ;
				r.crearExcelTransaccionesCuotas();
				
				reporte = new File (sRutaFisica);
				size = reporte.length();
			}
			
			if( size == 0 && retry > 2  ){ 
				return;
			}
			
			
			if(CodeUtil.getFromSessionMap( "debatm_IdBitacoraProceso" ) != null){
				String parentrowid1  = cuotas.get(0).getMpbcaid() + "" + CodeUtil.getFromSessionMap("debatm_IdBitacoraProceso").toString() ;
				int idemision1 = Integer.parseInt( ( cuotas.get(0).getMpbcaid()  +""+ CodeUtil.getFromSessionMap("debatm_IdBitacoraProceso").toString().trim() ) ) ;
				DebitosAutomaticosPmtCtrl.grabarReporteResumenCobrosAutomaticos(sRutaFisica, idemision1, parentrowid1) ;
			}
			
			if ( codigosNotificaciones  != null ){
				
				List<String[]>MontosMonedas = new ArrayList<String[]>();
				
				
				/*
				@SuppressWarnings("unchecked")
				List<Vwbitacoracobrospmt> cuotasNoAplicadas = (ArrayList<Vwbitacoracobrospmt>)
					CollectionUtils.select(cuotas, new Predicate(){
						@Override
						public boolean evaluate(Object o) {
							Vwbitacoracobrospmt vtmp = (Vwbitacoracobrospmt)o;
							return vtmp.getResponseCode().compareTo( ResponseCodes.APROBADO.CODE  ) != 0 ; 
						}
					}); 
				
				@SuppressWarnings("unchecked")
				final List<String> monedas = (ArrayList<String>)CodeUtil.selectPropertyListFromEntity(cuotasNoAplicadas, "mpbmon", true);
				 
				
				for (final String moneda : monedas) {
					
					@SuppressWarnings("unchecked")
					List<Vwbitacoracobrospmt> cuotaxmoneda = (ArrayList<Vwbitacoracobrospmt>)
						CollectionUtils.select(cuotas, new Predicate(){
							@Override
							public boolean evaluate(Object o) {
								Vwbitacoracobrospmt vtmp = (Vwbitacoracobrospmt)o;
								return vtmp.getMpbmon().compareTo(moneda) == 0  ; 
							}
						}); 
					*/
					
					List<Vwbitacoracobrospmt> cuotasNoAplicadas = cuotas.stream()
							.filter( cuota -> cuota.getResponseCode().compareTo( ResponseCodes.APROBADO.CODE  ) != 0 )
							.collect(Collectors.toList() ) ;
				
				
					@SuppressWarnings("unchecked")
					final List<String> monedas = (ArrayList<String>)CodeUtil.selectPropertyListFromEntity(cuotasNoAplicadas, "mpbmon", true);
					
					for (String moneda : monedas) {
						
						List<Vwbitacoracobrospmt> cuotaxmoneda =  cuotasNoAplicadas.stream()
								.filter( cuota -> cuota.getMpbmon().compareTo(moneda) == 0 )
								.collect(Collectors.toList() ) ;
					
					BigDecimal montoMoneda = CodeUtil.sumPropertyValueFromEntityList(cuotaxmoneda, "montocobrarentarjeta", false);	
							
					MontosMonedas.add(new String[] {moneda, String.format("%1$,.2f",montoMoneda ) }) ;
							
				}
				
				DebitosAutomaticosPmtCtrl.notificacionResumenProcesoCobros(cuotas.get(0).getNombrecaja(), cuotas.size(), codigosNotificaciones, sRutaFisica, MontosMonedas) ;
			}
			
		} catch (Exception e) { 
			e.printStackTrace();
		}
	}
	
	// ************************** utilidades *********************** //
	public String monedaBaseActual(){
		return	String.valueOf( ( ( (Object[])CodeUtil.getFromSessionMap("dbat_OBJCONFIGCOMP") )[1] ) ) ;
	}
	public BigDecimal tasaOficial(){
		return TasaCambioCtrl.tasaOficial();
	}
	public BigDecimal tasaParalela(String moneda) {
		return TasaCambioCtrl.tasaParalela(moneda);
	}
	public void generarObjConfigComp(String sCodcomp, int caid){

		String sMonedaBase = "";
		Object[] objConfigComp = new Object[4];
		String sUsarTasa = "1";
		String[] lstMonedaCC = null;
		
		try {
			
			CodeUtil.removeFromSessionMap("dbat_OBJCONFIGCOMP");
			
			if(CodeUtil.getFromSessionMap("dbat_F55CA014") == null ){
				F55ca014[] lstComxCaja = CompaniaCtrl.obtenerCompaniasxCaja(caid);
				CodeUtil.putInSessionMap("dbat_F55CA014", lstComxCaja);
			}
			
			F55ca014[] f14_ConfigComp = (F55ca014[])CodeUtil.getFromSessionMap("dbat_F55CA014");
						
			//&& ============= Leer la moneda base de la compañía.
			for (F55ca014 f14 : f14_ConfigComp) {
				if(f14.getId().getC4rp01().trim().equals(sCodcomp)){
					sMonedaBase = f14.getId().getC4bcrcd().trim();
					break;
				}
			}
			//&& ============= Obtener monedas para compañía agrupadas por método de pago
			lstMonedaCC = MonedaCtrl.obtenerMonedasxCaja_Companias(caid, f14_ConfigComp);
			
			if( lstMonedaCC != null && lstMonedaCC.length > 0 ){
				if( lstMonedaCC.length == 1 && lstMonedaCC[0].trim().equals(sMonedaBase)){
					sUsarTasa = "0";
				}
			}
			
			objConfigComp[0] =  sCodcomp;
			objConfigComp[1] =  sMonedaBase;
			objConfigComp[2] =  lstMonedaCC.length + "";
			objConfigComp[3] =  sUsarTasa;
			CodeUtil.putInSessionMap( "dbat_OBJCONFIGCOMP", objConfigComp);
			
		} catch (Exception e) {
			objConfigComp = null;
			e.printStackTrace(); 
		}
	
	}
	// ************************** utilidades *********************** //
	
	int iContador = 0;
	public HtmlGridView getGvCuotasPendientesDebitos() {

		
		return gvCuotasPendientesDebitos;
	}

	public void setGvCuotasPendientesDebitos(HtmlGridView gvCuotasPendientesDebitos) {
		this.gvCuotasPendientesDebitos = gvCuotasPendientesDebitos;
	}

	@SuppressWarnings("unchecked")
	public List<Vwbitacoracobrospmt> getLstCuotasPendientesDebitos() {
		
		try {
			CodeUtil.removeFromSessionMap("debatm_IdBitacoraProceso");
			
			if (CodeUtil.getFromSessionMap("debatm_lstCuotasPendientesDebitos") != null){
				return lstCuotasPendientesDebitos = (ArrayList<Vwbitacoracobrospmt>)
						CodeUtil.getFromSessionMap("debatm_lstCuotasPendientesDebitos");
			}
			
			final Vf55ca01 datos_caja = ((List<Vf55ca01>) CodeUtil.getFromSessionMap("lstCajas")).get(0);
			final Vautoriz vautoriz = ((Vautoriz[])CodeUtil.getFromSessionMap("sevAut") )[0];
			
			lstCuotasPendientesDebitos = DebitosAutomaticosPmtCtrl.cuotasPendientesCobroCaja( datos_caja.getId().getCaid() ) ;
			
			if(lstCuotasPendientesDebitos != null && !lstCuotasPendientesDebitos.isEmpty()){
				
				CollectionUtils.forAllDo(lstCuotasPendientesDebitos, new Closure(){
					public void execute(Object o) {
						Vwbitacoracobrospmt v = (Vwbitacoracobrospmt)o;
						v.setMpbcaid(datos_caja.getId().getCaid());
						v.setNombrecaja( datos_caja.getId().getCaname().trim().toLowerCase() );
						v.setMpbnumrec( 0 );
						v.setNombrecajero( vautoriz.getId().getEmpnombre().trim().toLowerCase() );
						v.setMpbfecam(new Date() ) ;
						v.setMpbhrcam(new Date() ) ;
					}
				}) ;
			}
			
		} catch (Exception e) {
			LogCajaService.CreateLog("getLstCuotasPendientesDebitos", "ERR", e.getMessage());
			e.printStackTrace(); 
		}finally{
			
			if(lstCuotasPendientesDebitos == null )
				lstCuotasPendientesDebitos = new ArrayList<Vwbitacoracobrospmt>();
			
			CodeUtil.putInSessionMap("debatm_lstCuotasPendientesDebitos", lstCuotasPendientesDebitos); 
		}
		
		return lstCuotasPendientesDebitos;
	}

	public void setLstCuotasPendientesDebitos(
			List<Vwbitacoracobrospmt> lstCuotasPendientesDebitos) {
		this.lstCuotasPendientesDebitos = lstCuotasPendientesDebitos;
	}

	public HtmlGridView getGvResumenCobrosAutomaticosProcesar() {
		return gvResumenCobrosAutomaticosProcesar;
	}

	public void setGvResumenCobrosAutomaticosProcesar(
			HtmlGridView gvResumenCobrosAutomaticosProcesar) {
		this.gvResumenCobrosAutomaticosProcesar = gvResumenCobrosAutomaticosProcesar;
	}

	@SuppressWarnings("unchecked")
	public List<ResumenCobroAutomatico> getLstResumenCobrosAutomaticosProcesar() {
		
		if (CodeUtil.getFromSessionMap("debatm_lstResumenCobrosAutomaticosProcesar") != null){
			return lstResumenCobrosAutomaticosProcesar = (ArrayList<ResumenCobroAutomatico>)
					CodeUtil.getFromSessionMap("debatm_lstResumenCobrosAutomaticosProcesar");
		}
		if(lstResumenCobrosAutomaticosProcesar == null)
			lstResumenCobrosAutomaticosProcesar = new ArrayList<ResumenCobroAutomatico>();
		
		
		return lstResumenCobrosAutomaticosProcesar;
	}

	public void setLstResumenCobrosAutomaticosProcesar(
			List<ResumenCobroAutomatico> lstResumenCobrosAutomaticosProcesar) {
		this.lstResumenCobrosAutomaticosProcesar = lstResumenCobrosAutomaticosProcesar;
	}

	public HtmlDialogWindow getDwResumenProcedimientoCobroAutomatico() {
		return dwResumenProcedimientoCobroAutomatico;
	}

	public void setDwResumenProcedimientoCobroAutomatico(
			HtmlDialogWindow dwResumenProcedimientoCobroAutomatico) {
		this.dwResumenProcedimientoCobroAutomatico = dwResumenProcedimientoCobroAutomatico;
	}

	public HtmlDialogWindow getDwMensajesValidacion() {
		return dwMensajesValidacion;
	}
	public void setDwMensajesValidacion(HtmlDialogWindow dwMensajesValidacion) {
		this.dwMensajesValidacion = dwMensajesValidacion;
	}
	public HtmlDialogWindow getDwConfirmacionProcesaRecibo() {
		return dwConfirmacionProcesaRecibo;
	}
	public void setDwConfirmacionProcesaRecibo(
			HtmlDialogWindow dwConfirmacionProcesaRecibo) {
		this.dwConfirmacionProcesaRecibo = dwConfirmacionProcesaRecibo;
	}

	public HtmlDialogWindowHeader getDwTituloMensajeValidacion() {
		return dwTituloMensajeValidacion;
	}

	public void setDwTituloMensajeValidacion(
			HtmlDialogWindowHeader dwTituloMensajeValidacion) {
		this.dwTituloMensajeValidacion = dwTituloMensajeValidacion;
	}

	public HtmlOutputText getLblMensajeValidacion() {
		return lblMensajeValidacion;
	}

	public void setLblMensajeValidacion(HtmlOutputText lblMensajeValidacion) {
		this.lblMensajeValidacion = lblMensajeValidacion;
	}

	public HtmlDialogWindow getDwConfirmacionProcesoDebitos() {
		return dwConfirmacionProcesoDebitos;
	}

	public void setDwConfirmacionProcesoDebitos(
			HtmlDialogWindow dwConfirmacionProcesoDebitos) {
		this.dwConfirmacionProcesoDebitos = dwConfirmacionProcesoDebitos;
	}

	public HtmlOutputText getLblMensajeConfirmacionCobros() {
		return lblMensajeConfirmacionCobros;
	}

	public void setLblMensajeConfirmacionCobros(
			HtmlOutputText lblMensajeConfirmacionCobros) {
		this.lblMensajeConfirmacionCobros = lblMensajeConfirmacionCobros;
	}
	public HtmlOutputText getLblMensajeConfirmacionCobrosPorNivel() {
		return lblMensajeConfirmacionCobrosPorNivel;
	}
	public void setLblMensajeConfirmacionCobrosPorNivel(
			HtmlOutputText lblMensajeConfirmacionCobrosPorNivel) {
		this.lblMensajeConfirmacionCobrosPorNivel = lblMensajeConfirmacionCobrosPorNivel;
	}
	public HtmlDialogWindow getDwConfirmacionProcesoDebitosPorNivel() {
		return dwConfirmacionProcesoDebitosPorNivel;
	}
	public void setDwConfirmacionProcesoDebitosPorNivel(
			HtmlDialogWindow dwConfirmacionProcesoDebitosPorNivel) {
		this.dwConfirmacionProcesoDebitosPorNivel = dwConfirmacionProcesoDebitosPorNivel;
	}

	public HtmlOutputText getLblMensajeConfirmacionCobroCuotaIndividual() {
		return lblMensajeConfirmacionCobroCuotaIndividual;
	}

	public void setLblMensajeConfirmacionCobroCuotaIndividual(
			HtmlOutputText lblMensajeConfirmacionCobroCuotaIndividual) {
		this.lblMensajeConfirmacionCobroCuotaIndividual = lblMensajeConfirmacionCobroCuotaIndividual;
	}

	public HtmlDialogWindow getDwConfirmarProcesoCuotaIndividual() {
		return dwConfirmarProcesoCuotaIndividual;
	}

	public void setDwConfirmarProcesoCuotaIndividual(
			HtmlDialogWindow dwConfirmarProcesoCuotaIndividual) {
		this.dwConfirmarProcesoCuotaIndividual = dwConfirmarProcesoCuotaIndividual;
	}
	
}

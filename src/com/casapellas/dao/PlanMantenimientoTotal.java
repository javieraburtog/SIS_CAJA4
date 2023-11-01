package com.casapellas.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlInputTextarea;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.casapellas.controles.ConsolidadoDepositosBcoCtrl;
import com.casapellas.controles.DebitosAutomaticosPmtCtrl;
import com.casapellas.controles.MetodosPagoCtrl;
import com.casapellas.controles.PlanMantenimientoTotalCtrl;
import com.casapellas.controles.PlanMantenimientoTotalCtrlV2;
import com.casapellas.controles.TasaCambioCtrl;
import com.casapellas.controles.tmp.AfiliadoCtrl;
import com.casapellas.controles.tmp.BancoCtrl;
import com.casapellas.controles.tmp.CompaniaCtrl;
import com.casapellas.controles.tmp.CtrlCajas;
import com.casapellas.controles.tmp.CtrlPoliticas;
import com.casapellas.controles.tmp.MonedaCtrl;
import com.casapellas.controles.tmp.NumcajaCtrl;
import com.casapellas.controles.tmp.ReciboCtrl;
import com.casapellas.donacion.entidades.GValidate;
import com.casapellas.entidades.Cafiliados;
import com.casapellas.entidades.F55ca014;
import com.casapellas.entidades.F55ca022;
import com.casapellas.entidades.MetodosPago;
import com.casapellas.entidades.Metpago;
import com.casapellas.entidades.Tcambio;
import com.casapellas.entidades.Tpararela;
import com.casapellas.entidades.Unegocio;
import com.casapellas.entidades.Vf0101;
import com.casapellas.entidades.Vf55ca01;
import com.casapellas.entidades.pmt.Vwbitacoracobrospmt;

import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.navegacion.As400Connection;
import com.casapellas.socketpos.bac.transactions.ResponseCodes;
import com.casapellas.util.BitacoraSecuenciaReciboService;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.PropertiesSystem;
import com.casapellas.entidades.ens.Vautoriz;
import com.infragistics.faces.grid.component.RowItem;
import com.infragistics.faces.grid.component.html.HtmlGridView;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
import com.infragistics.faces.shared.component.DataRepeater;
import com.infragistics.faces.shared.component.html.HtmlLink;
import com.infragistics.faces.window.component.html.HtmlDialogWindow;
import com.infragistics.faces.window.component.html.HtmlDialogWindowHeader;

public class PlanMantenimientoTotal {

	public HtmlDialogWindow dwBusquedaClientes;
	
	public HtmlInputText txtBusquedaCodigoCliente;
	public HtmlInputText txtBusquedaNombreCliente;
	public HtmlInputText txtParametroBusquedaCliente;
	public HtmlInputText txtMontoIngresado;
	public HtmlInputText txtReferencia1;
	public HtmlInputText txtReferencia2;
	public HtmlInputText txtReferencia3;
	public HtmlInputText txtMontoAplicadoRecibir;
	public HtmlInputText txtMontoCambio;
	public HtmlInputText txtNumeroContratoValidar;
	public HtmlInputTextarea txtConceptoAnticipo;
	
	public List<SelectItem> lstTipoAnticiposIngreso;
	public List<SelectItem> lstFiltroCompania;
	public List<SelectItem> lstFiltroSucursal;
	public List<SelectItem> lstFiltroUnidadNegocio;
	public List<SelectItem> lstMonedaAplicadaRecibo;
	public List<SelectItem> lstTipoBusquedaCliente ;
	public List<SelectItem> lstFormaDePago  ;
	public List<SelectItem> lstMonedaPago   ;
	public List<SelectItem> lstAfiliado     ;
	public List<SelectItem> lstMarcaTarjeta ;
	public List<SelectItem> lstCodigoBanco  ;
	
	public HtmlDropDownList ddlTipoAnticiposIngreso;
	public HtmlDropDownList ddlFiltroCompania;
	public HtmlDropDownList ddlFiltroSucursal;
	public HtmlDropDownList ddlFiltroUnidadNegocio;
	public HtmlDropDownList ddlMonedaAplicadaRecibo;
	public HtmlDropDownList ddlTipoBusquedaCliente;
	
	public HtmlGridView gvClientesDisponibles;
	public HtmlGridView gvFormasDePagoRecibidas;
	public HtmlGridView gvCuotasPendientesDisponibles;
	
	public List<Vf0101>lstClientesDisponibles;
	public List<MetodosPago> lstFormasDePagoRecibidas;
	public List<Vwbitacoracobrospmt> lstCuotasPendientesDisponibles;

	public HtmlDropDownList ddlFormaDePago  ;
	public HtmlDropDownList ddlMonedaPago   ;
	public HtmlDropDownList ddlAfiliado     ;
	public HtmlDropDownList ddlMarcaTarjeta ;
	public HtmlDropDownList ddlCodigoBanco  ;
	
	public HtmlOutputText lblAfiliado     ;
	public HtmlOutputText lblMarcaTarjeta ;
	public HtmlOutputText lblCodigoBanco  ;
	public HtmlOutputText lblReferencia1  ;
	public HtmlOutputText lblReferencia2  ;
	public HtmlOutputText lblReferencia3  ;
//	public HtmlOutputText lblDisplayTasaOficial;
	public HtmlOutputText lblDisplayTasaParalela;
	public HtmlOutputText lblMontoRecibido;
	public HtmlOutputText lblMontoPendiente;
	public HtmlOutputText lblMontoPendienteNac;
	public HtmlOutputText lblMontoCambioNacional;
	public HtmlOutputText lblMensajeValidacion;
	public HtmlOutputText lblRsmMontoMontoAplicadoAnticipo;
	public HtmlOutputText lblRsmNumeroContrato  ;
	public HtmlOutputText lblRsmNumeroPropuesta ;
	public HtmlOutputText lblRsmNumeroChasis    ;
	public HtmlOutputText lblRsmFechaContrato   ;
	public HtmlOutputText lblRsmElaboradoPor    ;
	
	public HtmlOutputText lblRsmCuotaMonto;
	public HtmlOutputText lblRsmCuotaMontoCordobas;
	public HtmlOutputText lblRsmCuotaNumero;
	public HtmlOutputText lblRsmCuotaFechaPago;
	public HtmlOutputText lblRsmCuotaMoneda;
	
	public String strDisplayTasaOficial ;
	public String strDisplayTasaParalela;
	
	public HtmlDialogWindow dwMensajesValidacion;
	public HtmlDialogWindow dwConfirmacionProcesaRecibo;
	public HtmlDialogWindow dwCuotasPendientesDisponibles;
	
	public HtmlDialogWindowHeader dwTituloMensajeValidacion;
	
	public HtmlPanelGroup tblResumenContrato;
	public HtmlPanelGroup tblResumenCuotaPendiente;
	
	private HtmlLink lnkBuscarCuotasPendientes;
	
	public static String SELECT_ITEM_FIRST_VALUE = "00";
	@SuppressWarnings("rawtypes")
	Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	
	//Nuevos valores para JDE
	String[] valoresJdeNumeracion = (String[]) m.get("valoresJDENumeracionIns");
	String[] valoresJDEInsPMT = (String[]) m.get("valoresJDEInsPMT");
	String[] valoresJdeInsContado = (String[]) m.get("valoresJDEInsContado");
	public void seleccionarCuotaParaProcesar(ActionEvent ev){
		try {
			
			RowItem ri = (RowItem) ev.getComponent().getParent().getParent();
			Vwbitacoracobrospmt cuota = (Vwbitacoracobrospmt)DataRepeater.getDataRow(ri);
			
			BigDecimal montoCordobas = cuota.getMontocobrarentarjeta() ;
			
			if( cuota.getMpbmon().compareTo("USD") == 0 ) {
				
				BigDecimal tasaOficial = tasaOficial();
				montoCordobas =  cuota.getMpbvpag().multiply( tasaOficial ).setScale(2, RoundingMode.HALF_UP) ;
				
			}
			
			String contrato_moneda =String.valueOf( CodeUtil.getFromSessionMap("pmt_MonedaContrato")  );
			
			 //&& ================ Establecer el monto aplicado
			 if(contrato_moneda.compareTo("USD")==0)
			 {
				 String monedaBaseComp = monedaBaseActual();
				 BigDecimal tasaParalela = tasaParalela(monedaBaseComp);
				 
				 txtMontoAplicadoRecibir.setValue(cuota.getMpbvpag());
				 lblRsmCuotaMontoCordobas.setValue( cuota.getMpbvpag().multiply( tasaParalela ).setScale(2, RoundingMode.HALF_UP));

			 }
			 else
			 {
				 txtMontoAplicadoRecibir.setValue(montoCordobas);
				 lblRsmCuotaMontoCordobas.setValue( montoCordobas );
			 }
			 
			 lblRsmCuotaMonto.setValue( cuota.getMpbvpag() );
			 
			 lblRsmCuotaNumero.setValue( cuota.getMpbnpag() );
			 lblRsmCuotaFechaPago.setValue( cuota.getMpbfpag() );
			 lblRsmCuotaMoneda.setValue( cuota.getMpbmon() );
			 
			 CodeUtil.removeFromSessionMap("pmt_montoCuotaUSD");
			 CodeUtil.removeFromSessionMap("pmt_montoCuotaCOR");
			 
			 CodeUtil.putInSessionMap("pmt_montoCuotaUSD", cuota.getMpbvpag());
			 CodeUtil.putInSessionMap("pmt_montoCuotaCOR", montoCordobas);
			 
			 
//			 lblRsmMontoMontoAplicadoAnticipo.setValue( cuota.getMpbvpag() );
			 //Procesar Recibo por Anticipos PMT
//			 lblRsmMontoMontoAplicadoAnticipo.setValue( montoCordobas );
		
			 if(contrato_moneda.compareTo("USD")==0)
				 lblRsmMontoMontoAplicadoAnticipo.setValue( cuota.getMpbvpag() );
			 else
				 lblRsmMontoMontoAplicadoAnticipo.setValue( montoCordobas );
				 
			 dwCuotasPendientesDisponibles.setWindowState("hidden");
			 
			 CodeUtil.refreshIgObjects( new Object[]{
				 dwCuotasPendientesDisponibles,
				 lblRsmMontoMontoAplicadoAnticipo,
				 lblRsmCuotaMonto,
				 lblRsmCuotaMontoCordobas,
				 lblRsmCuotaNumero,
				 lblRsmCuotaFechaPago,
				 lblRsmCuotaMoneda,
				 txtMontoAplicadoRecibir
			 });
			
			 CodeUtil.putInSessionMap("pmt_CuotaPendienteSeleccionada", cuota);
			
			
			 
//			 txtMontoAplicadoRecibir.setValue(montoCordobas);
			 establecerMontoAplicadoRecibo(null);
			 
			 
		} catch (Exception e) {
			e.printStackTrace(); 
		}finally{
			
		}
	}
	public void cerrarSeleccionarCuotaPendiente(ActionEvent ev){
		dwCuotasPendientesDisponibles.setWindowState("hidden");
	}
	public void mostrarCuotasPendientesPorContrato(ActionEvent ev){
		String msgValida = "" ;
		
		try {
			
			CodeUtil.removeFromSessionMap("pmt_CuotaPendienteSeleccionada");
			
			
			if(CodeUtil.getFromSessionMap("pmt_NumeroContratoValido") == null ){
				 msgValida = "No se ha ingresado un cliente/contrato válido para buscar cuotas disponibles";
				 return;
			}
			
			 lstCuotasPendientesDisponibles = (List<Vwbitacoracobrospmt>)CodeUtil.getFromSessionMap("pmt_lstCuotasPendientesDisponibles");
			 
			 if(lstCuotasPendientesDisponibles == null || lstCuotasPendientesDisponibles.isEmpty() ){
				 msgValida = "No se han encontrado cuotas disponibles para el contrato seleccionado";
				 return;
			 }
			
			 gvCuotasPendientesDisponibles.dataBind();
			 gvCuotasPendientesDisponibles.setPageIndex(0);

			
		} catch (Exception e) {
			e.printStackTrace(); 
		}finally{
			
			if(msgValida.isEmpty()){
				 dwCuotasPendientesDisponibles.setWindowState("normal");
			}else{
				dwTituloMensajeValidacion.setCaptionText("Desplegar Cuotas Pendientes");
				dwMensajesValidacion.setWindowState("normal");
				lblMensajeValidacion.setValue(msgValida);
				CodeUtil.refreshIgObjects(dwMensajesValidacion);
			}
		}
	}
	
	public void cambiarTipoDeAnticipo(ValueChangeEvent vce){
		try {
			
			String tipoAnticipo = ddlTipoAnticiposIngreso.getValue().toString();
			
			//&& ============= restablecer todos los campos de la pantalla.
			restablecerCamposEntrada( );
			
			
			//&& ============= habilitar el boton de busqueda de cuotas pendientes.
			ddlTipoAnticiposIngreso.setValue(tipoAnticipo);
			String estiloActualLink = lnkBuscarCuotasPendientes.getStyle();
			
			if( tipoAnticipo.compareTo("002") == 0 ){
				lnkBuscarCuotasPendientes.setStyle( estiloActualLink.replace(" display:none;", "") );
			}else{
				lnkBuscarCuotasPendientes.setStyle(estiloActualLink + "display:none; " );
			}
 
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}finally{
			
		}
	}
	
	public Object[] queryNumberContrato(int numeroContrato, int numeroCliente, String codcomp, String tipoAnticipo){
		Object[] dtaContrato = null ;
		
		try {
			
			String strQuery =
				" select numctto, numprop, cliente, chasis, motor, fecctto, horctto, hechoc  " +
				" from "+PropertiesSystem.QS36F+".sotmpc inner join "+PropertiesSystem.QS36F+".sottab on tbcia = compancto and tbcodi = '001' " +
				" where status = '"+tipoAnticipo+"' and trim(tbclas) = '"+codcomp.trim()+"' and cliente = "+numeroCliente+" and numctto = " + numeroContrato;
			
			@SuppressWarnings("unchecked")
			List<Object[]> result = (ArrayList<Object[]>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(strQuery, true, null);
			
			if( result != null && !result.isEmpty() )
				return dtaContrato = result.get(0);
			
		} catch (Exception e) {
			dtaContrato = null;
			e.printStackTrace(); 
		}
		
		return  dtaContrato ;
		
	}
	
	@SuppressWarnings("static-access")
	public void validarNumeroContrato(ActionEvent ev){
		String strMensajeProceso = "" ;
		String na="N/A";
		
		try {

			CodeUtil.removeFromSessionMap(new String[] {
					"pmt_CodigoClienteContrato", "pmt_NumeroContratoValido",
					"pmt_lstCuotasPendientesDisponibles" });
			
			
			//&& ==== validar que se haya seleccionado cliente.
			if ( txtBusquedaCodigoCliente.getValue() == null || txtBusquedaCodigoCliente.getValue().toString().trim().isEmpty() ){
				strMensajeProceso = " Seleccione el registo de cliente a utilizar ";
				return ;
			}
			if ( txtNumeroContratoValidar.getValue() == null || txtNumeroContratoValidar.getValue().toString().trim().isEmpty() ){
				strMensajeProceso = " Ingrese el numero de contrato ";
				return ;
			}
			
			int clienteCodigo = Integer.parseInt( txtBusquedaCodigoCliente.getValue().toString().trim() );
			String codcomp = ddlFiltroCompania.getValue().toString();
			String tipoAnticipo = ddlTipoAnticiposIngreso.getValue().toString();
		
			int numero_Contrato = Integer.parseInt(txtNumeroContratoValidar.getValue().toString().trim());
					
			Object[] queryNumberContrato = PlanMantenimientoTotalCtrl.queryNumberContrato(numero_Contrato, clienteCodigo, codcomp, "");
			
			
			List<GValidate> listaPagos = PlanMantenimientoTotalCtrl.getListaPagosPMT(numero_Contrato, clienteCodigo);
			
			if(queryNumberContrato == null){
				strMensajeProceso = "No se ha encontrado datos asociados al número de Contrato";
				return ;
			}
			
			BigDecimal montoContrato = BigDecimal.ZERO;
			String monedaContrato = "";
			
			montoContrato = new BigDecimal(String.valueOf(queryNumberContrato[9])) ;
			monedaContrato = String.valueOf(queryNumberContrato[12]);

			CodeUtil.putInSessionMap("pmt_MonedaContrato", monedaContrato);
			CodeUtil.putInSessionMap("pmt_MontoAnticipo", montoContrato);
			
			
			establecerMonedaAplicada();
			
			if( tipoAnticipo.compareTo("001") == 0 ){
				
				if( queryNumberContrato[8].toString().compareTo("001") != 0 && queryNumberContrato[8].toString().compareTo("002") != 0 ){
					strMensajeProceso = "El estado actual del contrato no permite aplicarle recibos desde modulo de caja, Estado: " + queryNumberContrato[8].toString() ;
					return ;
				}
				
				String strSqlQuerySelect = 
						" select * from " +PropertiesSystem.QS36F +".sotmpd " +
						" where status = '002' " +
						" and trim(compan) = '" +String.valueOf( queryNumberContrato[10] ).trim() +"' " +
						" and trim(sucurs) = '"+String.valueOf( queryNumberContrato[11] ).trim() +"' " +
						" and numctto = " + numero_Contrato ;
					
				@SuppressWarnings("unchecked")
				List<Object[]> dtaF0411= (List<Object[]>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSqlQuerySelect, true, null);
				
		
					
				if( dtaF0411 != null && !dtaF0411.isEmpty() ) {
					strMensajeProceso = "No es posible aplicar el recibo: El contrato ya tiene servicios utilizados ";
					return ;
				}

				if(monedaContrato.compareTo("USD")==0)
					 txtMontoAplicadoRecibir.setValue(montoContrato);
				 else
				 {
					 BigDecimal tasaOficial = tasaOficial();
					 BigDecimal montoCordobas =  montoContrato.multiply( tasaOficial ).setScale(2, RoundingMode.HALF_UP) ;
					 txtMontoAplicadoRecibir.setValue(montoCordobas);
				 }
				
				if(listaPagos != null && listaPagos.size()>0) {
					strMensajeProceso = "Prima ya cancelada ";
					return ;
					
				}
				
				establecerMontoAplicadoRecibo(null);
				
			}else{
				
				//&& ================= buscar que el contrato tenga cuotas pendientes. 
				
				List<Vwbitacoracobrospmt> lstCuotasPendientesDisponibles = DebitosAutomaticosPmtCtrl.cuotasPendientesPorClienteContrato(numero_Contrato, clienteCodigo, new Date() );
				if(lstCuotasPendientesDisponibles == null){
					strMensajeProceso = "No se encontraron cuotas pendientes de pago para el Cliente/Contrato seleccionado ";
					return ;
				}
				
				CodeUtil.putInSessionMap("pmt_lstCuotasPendientesDisponibles", lstCuotasPendientesDisponibles);
				
			}
			
			
			
			lblRsmMontoMontoAplicadoAnticipo.setValue( montoContrato );
			lblRsmNumeroContrato.setValue(String.valueOf(queryNumberContrato[0]));
			lblRsmNumeroPropuesta.setValue(String.valueOf(queryNumberContrato[1]));
			lblRsmNumeroChasis.setValue(String.valueOf(queryNumberContrato[3]));
			lblRsmFechaContrato.setValue( new SimpleDateFormat("dd/MM/yyyy").format( new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(queryNumberContrato[5]))));
			lblRsmElaboradoPor.setValue(String.valueOf(queryNumberContrato[7]));
			
			
			CodeUtil.removeFromSessionMap("pmt_MontoAnticipo");
			
			CodeUtil.putInSessionMap("pmt_MonedaContrato", monedaContrato);
			CodeUtil.putInSessionMap("pmt_MontoAnticipo", montoContrato);
			
			
			CodeUtil.putInSessionMap("pmt_NumeroContratoValido", numero_Contrato);
			CodeUtil.putInSessionMap("pmt_CodigoClienteContrato", clienteCodigo);

			
		} catch (Exception e) {
			strMensajeProceso = "El número de contrato ingresado no puede ser validado";
			e.printStackTrace(); 
		}finally{
			
			CodeUtil.refreshIgObjects(new Object[] { lblRsmNumeroContrato,
					lblRsmNumeroPropuesta, lblRsmNumeroChasis,
					lblRsmFechaContrato, lblRsmElaboradoPor, lblRsmMontoMontoAplicadoAnticipo, txtMontoAplicadoRecibir });
			
			
			if(!strMensajeProceso.isEmpty()){
				
				lblRsmMontoMontoAplicadoAnticipo.setValue("0.00");
				lblRsmNumeroContrato.setValue(na);
				lblRsmNumeroPropuesta.setValue(na);
				lblRsmNumeroChasis.setValue(na);
				lblRsmFechaContrato.setValue(na);
				lblRsmElaboradoPor.setValue(na);
				txtMontoAplicadoRecibir.setValue("0.00");
				
				dwTituloMensajeValidacion.setCaptionText("Validación Número de Contrato");
				dwMensajesValidacion.setWindowState("normal");
				lblMensajeValidacion.setValue(strMensajeProceso);
			}
		}
	}
	
	
	
	/*
	 * Grabar los datos del recibo en las tablas de caja y el pasivo en JDE 
	 * Fecha: 26 de Mayo 2016
	 */
	@SuppressWarnings("unchecked")
	public void procesarReciboAnticiposPMT(ActionEvent ev){
		
				
		String strMensajeProceso = "" ;
		boolean aplicado = true;
		
		int clienteCodigo;
		int caid = 0;
		int numrec = 0;
		int cajero_codigo;
		int numrec_fcv;
		int contrato_numero;
		String contrato_moneda;
		BigDecimal montoCuotaUSD = BigDecimal.valueOf(0);
		BigDecimal montoCuotaCOR = BigDecimal.valueOf(0);
		
		String codcomp = "" ;
		String codsuc = "" ;
		String codsucSeleccionada;
		String codunineg;
		String tiporec =valoresJDEInsPMT[0];
		String strClienteNombre;
		String strConceptoPago;
		String strMonedaAplicada;
		final String monedaBaseCompania;
		String strCajeroNombre;
		String strCajeroUsuario;
		
		Date dtFechaGrabacion = new Date();
		
		BigDecimal tasaOficial;
		BigDecimal tasaParalela;
		BigDecimal bdMontoaplicado;
		BigDecimal bdMontoRecibido;
		BigDecimal bdMontoCambioLocal;
		BigDecimal bdMontoCambioExt;
		BigDecimal bdMontoCompraDivisas;
		
		Vautoriz vautoriz;
		Vf55ca01 datos_caja;
			
		Session session = null;
		Transaction transaction = null;
		
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
			
			
			LogCajaService.CreateLog("procesarReciboAnticiposPMT", "INF", "procesarReciboAnticiposPMT - INICIO - " + LogCajaService.getTimeInf());
			
			tasaOficial  = tasaOficial();
			tasaParalela  = tasaParalela("USD");
			//&& =================== cargar datos iniciales basicos para el recibo =================== && //  
			
			vautoriz = ((Vautoriz[])CodeUtil.getFromSessionMap("sevAut") )[0];
			datos_caja = ((List<Vf55ca01>) CodeUtil.getFromSessionMap("lstCajas")).get(0);
			
			
			clienteCodigo = Integer.parseInt( txtBusquedaCodigoCliente.getValue().toString().trim() );
			caid = Integer.parseInt( CodeUtil.getFromSessionMap(("sCajaId") ).toString() );
			cajero_codigo = vautoriz.getId().getCodreg();
			contrato_numero = Integer.parseInt(String.valueOf( CodeUtil.getFromSessionMap("pmt_NumeroContratoValido") ) ) ;
			contrato_moneda =String.valueOf( CodeUtil.getFromSessionMap("pmt_MonedaContrato")  ) ;
			
			String tipoAnticipo = ddlTipoAnticiposIngreso.getValue().toString();
			
			if( tipoAnticipo.compareTo("002") == 0 ){
				montoCuotaUSD =  (BigDecimal) CodeUtil.getFromSessionMap("pmt_montoCuotaUSD") ;
				montoCuotaCOR =(BigDecimal) CodeUtil.getFromSessionMap("pmt_montoCuotaCOR")  ;
			}else{
				montoCuotaUSD =  (BigDecimal) CodeUtil.getFromSessionMap("pmt_MontoAnticipo");
				montoCuotaCOR = ((BigDecimal) CodeUtil.getFromSessionMap("pmt_MontoAnticipo")).multiply(tasaOficial) ;
			}
			
			codsucSeleccionada = CodeUtil.pad(ddlFiltroCompania.getValue().toString().trim(), 5, "0");
			codcomp = ddlFiltroCompania.getValue().toString().trim();//ddlFiltroCompania.getValue().toString();
			codsuc = CodeUtil.pad(datos_caja.getId().getCaco(), 5, "0");//datos_caja.getId().getCaco();
			codunineg = ddlFiltroUnidadNegocio.getValue().toString();
			strClienteNombre = txtBusquedaNombreCliente.getValue().toString();
			strConceptoPago = txtConceptoAnticipo.getValue().toString();
			strMonedaAplicada = ddlMonedaAplicadaRecibo.getValue().toString();
			monedaBaseCompania = monedaBaseActual();
			strCajeroNombre = vautoriz.getId().getEmpnombre().trim();
			strCajeroUsuario = vautoriz.getId().getCoduser();
		
			bdMontoaplicado = new BigDecimal(txtMontoAplicadoRecibir.getValue().toString().trim().replace(",", ""));
			bdMontoRecibido = new BigDecimal(lblMontoRecibido.getValue().toString().trim().replace(",", ""));
			bdMontoCambioLocal = ( (BigDecimal)CodeUtil.getFromSessionMap("pmt_MontoCambioNac") ).abs() ;
			bdMontoCambioExt =  ((BigDecimal)CodeUtil.getFromSessionMap("pmt_MontoCambioExt") ).abs();
			
			lstFormasDePagoRecibidas = (List<MetodosPago>) CodeUtil.getFromSessionMap("pmt_lstFormasDePagoRecibidas");
			List<MetodosPago>lstFCVs = null;
			
			
			
			numrec_fcv = 0;
			
			//&& =================== Numero de recibo
			 numrec = com.casapellas.controles.tmp.ReciboCtrl.generarNumeroRecibo(caid, codcomp);
			if(numrec == 0){
				strMensajeProceso = "Recibo no aplicado: Error al generar numero de recibo"; 
				aplicado = false;
				return ;
			}
			BitacoraSecuenciaReciboService.insertarLogReciboNumerico(caid,numrec,codcomp,CodeUtil.pad(codsuc, 5, "0"),tiporec);

			//&& =================== Generar el numero de ficha de compra venta en caso de haber compra de moneda extranjera
			boolean hayFichaCambio = CodeUtil.getFromSessionMap("pmt_MontoCompraDivisas") != null ;
			bdMontoCompraDivisas = BigDecimal.ZERO;
			if(  hayFichaCambio  ){
				bdMontoCompraDivisas = (BigDecimal)CodeUtil.getFromSessionMap("pmt_MontoCompraDivisas");
				numrec_fcv = new NumcajaCtrl().obtenerNoSiguiente("FICHACV", caid, codcomp, codsuc, strCajeroUsuario);
			}
			
			//&& =================== Conexiones ============================= &&//			
			
			session = HibernateUtilPruebaCn.currentSession();
			transaction = session.beginTransaction();
			
			
			//&& =================== Determinar si el pago trabaja bajo preconciliacion y conservar su referencia original
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
			com.casapellas.controles.BancoCtrl.datosPreconciliacion(lstFormasDePagoRecibidas, caid, codcomp) ;
			
			//&& =================== Grabar el recibo.
			int numrecm = 0 ;
			ReciboCtrl rc = new ReciboCtrl();
			
			aplicado = rc.registrarRecibo(session, transaction, numrec,
								numrecm, codcomp, bdMontoaplicado.doubleValue(), bdMontoRecibido.doubleValue(), 
								bdMontoCambioExt.doubleValue(), strConceptoPago, 
								dtFechaGrabacion, dtFechaGrabacion, clienteCodigo, strClienteNombre,
								strCajeroNombre, caid, codsuc, strCajeroUsuario, tiporec, 0, "",
								numrec_fcv, dtFechaGrabacion, codunineg, "", strMonedaAplicada);
			
			if(!aplicado){
				throw new Exception("Recibo no aplicado: Error al grabar Recibo");				
			}
			
			aplicado = rc.registrarDetalleRecibo(session, transaction, numrec,  
								numrec, codcomp, lstFormasDePagoRecibidas,	
								caid, codsuc, tiporec);
			if(!aplicado){
				throw new Exception("Recibo no aplicado: Error al grabar Detalle de recibo");
				
			}
			
			aplicado = rc.registrarCambio(session, transaction, numrec, codcomp,
						monedaBaseCompania, bdMontoCambioLocal.doubleValue(), caid, codsuc, 
						tasaOficial,tiporec);
			
			if(!aplicado){
				throw new Exception( "Recibo no aplicado: Error al grabar cambios otorgados en el pago");
				
			}
			
			aplicado = rc.registrarCambio(session, transaction, numrec, codcomp,
					"USD", bdMontoCambioExt.doubleValue(), caid, codsuc, 
					tasaOficial,tiporec);
			
			if(!aplicado){
				throw new Exception( "Recibo no aplicado: Error al grabar cambios otorgados en el pago");
				
			}
			
			/* **********************************************************************************************************************/
			/* *********************** no mandar a generar asientos contables por cambios en moneda local   *************************/
			
			//&& ================ Crear el registro en gcpmcaja por compra de moneda extranjera.
			if(numrec_fcv != 0){
				
				lstFCVs = new ArrayList<MetodosPago>();
				lstFCVs.add(
						new MetodosPago(MetodosPagoCtrl.EFECTIVO , "USD", 
							bdMontoCompraDivisas.doubleValue(),	tasaOficial,
							bdMontoCambioLocal.doubleValue(), "", "","", "","0", 0)
						);
				
				aplicado = rc.registrarRecibo(session, transaction, numrec_fcv, 0, codcomp,
						bdMontoCompraDivisas.doubleValue(), bdMontoCompraDivisas.doubleValue(),
						0, "",  dtFechaGrabacion, dtFechaGrabacion,
						clienteCodigo, strClienteNombre, strCajeroNombre, caid,codsuc, strCajeroUsuario, 
						"FCV", 0, "", numrec, dtFechaGrabacion, codunineg,"",
						strMonedaAplicada );
				
				if(!aplicado){
					throw new Exception( "Recibo no aplicado, Error al grabar Recibo por Compra de Divisas");
					
				}
				
				aplicado = rc.registrarDetalleRecibo(session, transaction, numrec_fcv, 0, codcomp, lstFCVs, caid, codsuc, "FCV");
				
				if(!aplicado){
					throw new Exception( "Recibo no aplicado, Error al grabar detalle  de Compra de Divisas");
					
				}
			}
			
			//&& ============== restar los montos por cambio a los pagos registrados.
			MetodosPago mpExtranjero = (MetodosPago) 
			CollectionUtils.find(lstFormasDePagoRecibidas, new Predicate(){
				@Override
				public boolean evaluate(Object o) {
					return  ((MetodosPago)o).getMoneda().compareTo(monedaBaseCompania) != 0 && ((MetodosPago)o).getMetodo().compareTo(MetodosPagoCtrl.EFECTIVO ) == 0 ;
				}
			});
			if(mpExtranjero != null && bdMontoCambioExt.compareTo(BigDecimal.ZERO) == 1 ){
				
				BigDecimal bdCambioRealExt =  ((BigDecimal)CodeUtil.getFromSessionMap( "pmt_MontoCambioRealExt" ));
				BigDecimal bdMonto = new BigDecimal(Double.toString( mpExtranjero.getMonto() ) ).subtract(bdCambioRealExt).setScale(2, RoundingMode.HALF_UP);
				mpExtranjero.setMonto(bdMonto.doubleValue());
				
				
			}
			MetodosPago mpNacional = (MetodosPago) 
				CollectionUtils.find(lstFormasDePagoRecibidas, new Predicate(){
					@Override
					public boolean evaluate(Object o) {
						return  ((MetodosPago)o).getMoneda().compareTo(monedaBaseCompania) == 0 && ((MetodosPago)o).getMetodo().compareTo(MetodosPagoCtrl.EFECTIVO ) == 0 ;
					}
				});
			if(mpNacional != null && bdMontoCambioLocal.compareTo(BigDecimal.ZERO) == 1 ){
				BigDecimal bdMonto = new BigDecimal(Double.toString( mpNacional.getMonto() ) ).subtract(bdMontoCambioLocal).setScale(2, RoundingMode.HALF_UP);
				mpNacional.setMonto(bdMonto.doubleValue());
			}
			
			if(hayFichaCambio) {
				
				BigDecimal montoRecibido = new BigDecimal( Double.toString( mpExtranjero.getMonto() ) );
				BigDecimal montoEquivalente = montoRecibido ;
				
				montoRecibido = montoRecibido.subtract(bdMontoCompraDivisas);
				montoEquivalente = montoRecibido.multiply(mpExtranjero.getTasa());
				
				mpExtranjero.setMonto( montoRecibido.setScale(2, RoundingMode.HALF_UP ).doubleValue() );
				mpExtranjero.setEquivalente( montoEquivalente.setScale(2, RoundingMode.HALF_UP ).doubleValue() );
				 
			}
			
			//&& ============== crear las transacciones en JDE 
			PlanMantenimientoTotalCtrlV2 planMantCtrl = new PlanMantenimientoTotalCtrlV2();
			
			planMantCtrl.moneda_base = monedaBaseCompania;
			planMantCtrl.moneda_aplicada = strMonedaAplicada;
			planMantCtrl.codigo_compania = codcomp;
			planMantCtrl.codigo_sucursal_usuario = codsucSeleccionada;
			planMantCtrl.codigo_unidad_negocio_usuario = codunineg;
			planMantCtrl.monto_aplicado = bdMontoaplicado;
			planMantCtrl.diferencia = bdMontoaplicado.subtract(bdMontoRecibido);
			planMantCtrl.tasaoficial = tasaOficial ;
			planMantCtrl.tasaparalela = tasaParalela ;
			planMantCtrl.codigo_caja = caid;
			planMantCtrl.numero_recibo = numrec;
			
			planMantCtrl.numero_contrato = contrato_numero;
			planMantCtrl.moneda_contrato = contrato_moneda;
			planMantCtrl.cuota_contrato_pmt_cod = montoCuotaUSD.multiply(tasaOficial);
			planMantCtrl.cuota_contrato_pmt_usd = montoCuotaUSD;
			
			planMantCtrl.formas_de_pago = lstFormasDePagoRecibidas;
			planMantCtrl.compra_venta_Cambio = lstFCVs;
			planMantCtrl.numero_recibo_fcv = numrec_fcv;
			
			planMantCtrl.valoresJdeInsContado = valoresJdeInsContado;
			planMantCtrl.valoresJDEInsPMT = valoresJDEInsPMT;
			planMantCtrl.valoresJdeNumeracion = valoresJdeNumeracion;
					
			//&& =============== actualizar el registro de la cuota en Sotmpba
			Vwbitacoracobrospmt v = (Vwbitacoracobrospmt)CodeUtil.getFromSessionMap("pmt_CuotaPendienteSeleccionada") ;
			int numeroCuota = (v == null)? 0: v.getMpbnpag() ;
			
			strMensajeProceso = planMantCtrl.generarComprobantesContablesPMT(dtFechaGrabacion, clienteCodigo, vautoriz, numeroCuota, session);
			aplicado = strMensajeProceso.trim().isEmpty();
			
			if(!aplicado){
				LogCajaService.CreateLog("procesarReciboAnticiposPMT", "VAL", strMensajeProceso.trim());
				throw new Exception("Error en grabacion de comprobante contables PMT");
			}

			//&& ============== Grabar enlaces con jde
			ReciboCtrl recCtrl = new ReciboCtrl();
			List<String[]> numerosBatchDocs = planMantCtrl.numeros_batch_documento;

			Boolean bRegistroReciboJDE = false;
			for (String[] recjde : numerosBatchDocs) {
				aplicado = recCtrl.fillEnlaceMcajaJde( session, transaction, Integer.parseInt(recjde[2]), codcomp, 
							Integer.parseInt(recjde[1]), Integer.parseInt(recjde[0]), caid, codsuc, recjde[3], recjde[4]);
				
				if(!aplicado){
					throw new Exception( "Error al guardar enlace entre caja y JdEdward's");					
				}
				
				bRegistroReciboJDE = true;
			}
			
			if(!bRegistroReciboJDE){
				throw new Exception("Error al guardar enlace entre caja y JdEdward's");
				
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
					"(CAID, CODCOMP, CODSUC, CODUNINEG, CODCLI, NUMREC, NUMEROPROFORMA, NUMEROBATCH, TIPODOC, CODIGOCAJERO, MONEDAAPL, MONTORECIBO, TIPORECIBO, NUMEROCUOTA) "+
					" values "+
					"(@CAID, '@CODCOMP', '@CODSUC', '@CODUNINEG', @CODCLI, @NUMREC, @NUMEROPROFORMA, @NUMEROBATCH, '@TIPODOC', @CODIGOCAJERO, '@MONEDAAPL', @MONTORECIBO, '@TIPORECIBO', @NUMEROCUOTA) ";
				
			strSql = strSql
				.replace("@GCPMCAJA",PropertiesSystem.ESQUEMA )
				.replace("@CAID", Integer.toString(caid) )
				.replace("@CODCOMP", codcomp)	
				.replace("@CODSUC",	 codsucSeleccionada)
				.replace("@CODUNINEG",  codunineg)	
				.replace("@CODCLI",	Integer.toString(clienteCodigo) )
				.replace("@NUMREC", Integer.toString(numrec) )
				.replace("@NUMEROPROFORMA", Integer.toString(contrato_numero) )	
				.replace("@NUMEROBATCH",  dtaBatchPv[0])	
				.replace("@TIPODOC", "PV")
				.replace("@CODIGOCAJERO", Integer.toString(vautoriz.getId().getCodreg() ) )
				.replace("@MONEDAAPL",	strMonedaAplicada)
				.replace("@MONTORECIBO", bdMontoaplicado.toString() )
				.replace("@TIPORECIBO", tiporec)
				.replace("@NUMEROCUOTA", Integer.toString( numeroCuota ));
			
			LogCajaService.CreateLog("procesarReciboAnticiposPMT", "QRY", strSql);
			
			session.createSQLQuery(strSql).executeUpdate() ;
			
			//&& =============== actualizacion de la cuota en caso de que sea pago de cuota y no pago de anticipo.
			if( v != null){
				
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
				v.setNumerobatchpago(Integer.parseInt( dtaBatchPv[0] ) );
				
				v.setResponseCode(ResponseCodes.APROBADO.CODE);
				v.setResponseCodeDescription(ResponseCodes.APROBADO.DESCRIPTION);        
				
				strMensajeProceso = DebitosAutomaticosPmtCtrl.actualizarRegistroCuota(session, v);
				aplicado = strMensajeProceso.isEmpty();
				
			}
		} catch (Exception e) {
			LogCajaService.CreateLog("procesarReciboAnticiposPMT", "ERR", e.getMessage());
			strMensajeProceso = e.getMessage();
			aplicado = false;			
		}finally{
			m.remove("procesandoReciboValidacion");	
			String strMensajeError = "";
			
			try {
				if(aplicado) {
					transaction.commit();
					BitacoraSecuenciaReciboService.actualizarSatisfactorioLogReciboNumerico(caid,numrec,codcomp,CodeUtil.pad(codsuc, 5, "0"),tiporec);	
				}
				
			} catch (Exception e) {
				strMensajeError = "Error en confirmación de registro de valores: Caja, intente nuevamente";
				aplicado = false;
				e.printStackTrace();
				LogCajaService.CreateLog("procesarReciboAnticiposPMT", "ERR", e.getMessage());
			}
			
 
			if(aplicado){
				restablecerCamposEntrada(ev);
				CtrlCajas.imprimirRecibo(caid, numrec, codcomp, codsuc, tiporec, false);
			}else{
				try {
					if (transaction != null)
						transaction.rollback();
				} catch (Exception e) {
					e.printStackTrace();
					LogCajaService.CreateLog("procesarReciboAnticiposPMT", "ERR", e.getMessage());
				}				
			}
			
			LogCajaService.CreateLog("procesarReciboAnticiposPMT", "INF", "procesarReciboAnticiposPMT - FIN - " + LogCajaService.getTimeInf());
			
			//&& =================== cerrar y liberar conexiones.
			HibernateUtilPruebaCn.closeSession(session);

			dwTituloMensajeValidacion.setCaptionText("Procesar Recibo por Anticipos PMT");
			dwMensajesValidacion.setWindowState("normal");
			dwConfirmacionProcesaRecibo.setWindowState("hidden");
			
			if(strMensajeError.isEmpty() && strMensajeProceso.isEmpty() ){
				
				lblMensajeValidacion.setValue("Recibo aplicado correctamente");
			}
			if( !strMensajeError.isEmpty() )
				lblMensajeValidacion.setValue(strMensajeError);
			if( !strMensajeProceso.isEmpty() )
				lblMensajeValidacion.setValue(strMensajeProceso);
			
		}
		
	}
	
	public void cerrarConfirmarProcesoRecibo(ActionEvent ev){
		dwConfirmacionProcesaRecibo.setWindowState("hidden");
	}
	
	public void restablecerCamposEntrada(){
		
		Object[] obToRefresh = null; 
		
		try {

			obToRefresh = new Object[]{
					txtBusquedaCodigoCliente,
					txtBusquedaNombreCliente,
					txtConceptoAnticipo     ,
					ddlTipoAnticiposIngreso ,
					ddlFiltroCompania       ,
					ddlFiltroSucursal       ,
					ddlFiltroUnidadNegocio  ,
					ddlMonedaAplicadaRecibo ,
					gvFormasDePagoRecibidas,
			};
			
			txtBusquedaCodigoCliente.setValue("");
			txtBusquedaNombreCliente.setValue("");
			txtConceptoAnticipo.setValue("");
			
			String[] varToRemove ={
					"pmt_lstTipoAnticiposIngreso",
					"pmt_lstFiltroCompania",
					"pmt_lstFiltroSucursal",
					"pmt_lstFiltroUnidadNegocio",
					"pmt_lstMonedaAplicadaRecibo",
					"pmt_lstTipoBusquedaCliente",
					
					"pmt_CodigoClienteContrato",
					"pmt_NumeroContratoValido",
					"pmt_MontoCambioExt",
					"pmt_MontoCambioNac",
					"pmt_ReciboConMontoAplicado",
					"pmt_lstCuotasPendientesDisponibles",
					"pmt_CuotaPendienteSeleccionada"
			} ;
			
			CodeUtil.removeFromSessionMap(varToRemove);
			
			ddlTipoAnticiposIngreso.dataBind();
			ddlFiltroCompania.dataBind();
			ddlFiltroSucursal.dataBind();
			ddlFiltroUnidadNegocio.dataBind();
			ddlMonedaAplicadaRecibo.dataBind();
			
			lstFormasDePagoRecibidas = new ArrayList<MetodosPago>();
			CodeUtil.putInSessionMap("pmt_lstFormasDePagoRecibidas", lstFormasDePagoRecibidas);
			
			gvFormasDePagoRecibidas.dataBind();
			
			establecerResumenPagos();
			desplegarIngresoFormaPago(MetodosPagoCtrl.EFECTIVO );
			restablecerResumenContrato();
			
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}finally{
			CodeUtil.refreshIgObjects(obToRefresh);
		}
		
	}
	
	public void restablecerCamposEntrada(ActionEvent ev) {
		restablecerCamposEntrada();
		dwMensajesValidacion.setWindowState("normal");
		lblMensajeValidacion.setValue("Campos restablecidos correctamente");
	}
	
	
	public void restablecerResumenContrato(){
		
		try {
			
			String na = "N/A";
			lblRsmMontoMontoAplicadoAnticipo.setValue("0.00");
			lblRsmNumeroContrato.setValue(na);
			lblRsmNumeroPropuesta.setValue(na);
			lblRsmNumeroChasis.setValue(na);
			lblRsmFechaContrato.setValue(na);
			lblRsmElaboradoPor.setValue(na);
			txtNumeroContratoValidar.setValue("");
			
			lblRsmCuotaMonto.setValue("0.00");
			lblRsmCuotaMontoCordobas.setValue("0.00");
			lblRsmCuotaNumero.setValue("00");
			lblRsmCuotaFechaPago.setValue(na);
			lblRsmCuotaMoneda.setValue(na);
			
			CodeUtil.refreshIgObjects(new Object[]{
					lblRsmMontoMontoAplicadoAnticipo,
					txtNumeroContratoValidar,
					lblRsmNumeroContrato,
					lblRsmNumeroPropuesta, 
					lblRsmNumeroChasis,
					lblRsmFechaContrato, 
					lblRsmElaboradoPor,
					lblRsmCuotaMonto,
					lblRsmCuotaMontoCordobas,
					lblRsmCuotaNumero,
					lblRsmCuotaFechaPago,
					lblRsmCuotaMoneda
			}) ;    
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public void procesarDatosIngresados(ActionEvent ev){
		String strMensajeValidacion = "";
		
		try {
			
			if(ddlFiltroSucursal.getValue().toString().compareTo(SELECT_ITEM_FIRST_VALUE) == 0){
				strMensajeValidacion = " Seleccione la sucursal a utilizar ";
				return ;
			}
			if(ddlFiltroUnidadNegocio.getValue().toString().compareTo(SELECT_ITEM_FIRST_VALUE) == 0){
				strMensajeValidacion = " Seleccione la unidad de negocios utilizar ";
				return ;
			}
			
			if(txtBusquedaCodigoCliente.getValue() == null || txtBusquedaCodigoCliente.getValue().toString().trim().isEmpty() ){
				strMensajeValidacion = " Seleccione el registo de cliente a utilizar ";
				return ;
			}
			lstFormasDePagoRecibidas = (List<MetodosPago>) CodeUtil.getFromSessionMap("pmt_lstFormasDePagoRecibidas");
			if( lstFormasDePagoRecibidas == null || lstFormasDePagoRecibidas.isEmpty() ){
				strMensajeValidacion = "No se han agregado formas de pago al recibo";
				return ;
			}
			if(txtConceptoAnticipo.getValue() == null || txtConceptoAnticipo.getValue().toString().trim().isEmpty() ){
				strMensajeValidacion = "Debe Ingresar el concepto aplicado para el recibo ";
				return ;
			}
			
			//&& ============= Validaciones del monto aplicado 
			BigDecimal bdMontoAplicado = new BigDecimal( txtMontoAplicadoRecibir.getValue().toString().trim().replace(",", "") ).setScale(2, RoundingMode.HALF_UP);;
			if( CodeUtil.getFromSessionMap("pmt_ReciboConMontoAplicado") != null ){
				bdMontoAplicado = new BigDecimal ( String.valueOf( CodeUtil.getFromSessionMap("pmt_ReciboConMontoAplicado") ) ) ;
			}
			
			BigDecimal bdMontoRecibido = CodeUtil.sumPropertyValueFromEntityList(lstFormasDePagoRecibidas, "equivalente", false).setScale(2, RoundingMode.HALF_UP);
			if( bdMontoAplicado.compareTo(bdMontoRecibido) == 1 ){
				strMensajeValidacion = "El Monto aplicado no ha sido alcanzado por los pagos registrados";
				return ;
			}
			
			BigDecimal bdCambioExtranjero = ((BigDecimal)CodeUtil.getFromSessionMap("pmt_MontoCambioExt")).setScale(2, RoundingMode.HALF_UP); ;
			BigDecimal bdCambioNacional = ((BigDecimal)CodeUtil.getFromSessionMap("pmt_MontoCambioNac")).setScale(2, RoundingMode.HALF_UP);
			BigDecimal bdCambioExt = new BigDecimal(txtMontoCambio.getValue().toString().replace(",", ""));
			BigDecimal bdCambioNac = new BigDecimal(lblMontoCambioNacional.getValue().toString().replace(",", ""));
 
			if( bdCambioExtranjero.abs().compareTo(bdCambioExt.abs()) != 0 || bdCambioNacional.abs().compareTo(bdCambioNac.abs()) != 0 ) {
				strMensajeValidacion = "Verifique los montos de Cambio correspondientes";
				return ;
			}
			
			//&& ============= Validaciones del numero de contrato
			if ( txtNumeroContratoValidar.getValue() == null || txtNumeroContratoValidar.getValue().toString().trim().isEmpty() ){
				strMensajeValidacion = " Ingrese el numero de contrato ";
				return ;
			}
			if(CodeUtil.getFromSessionMap("pmt_NumeroContratoValido") == null){
				strMensajeValidacion = "El numero de Contrato no ha sido validado";
				return ;
			}
			
			int numero_contrato_valida = Integer.parseInt(txtNumeroContratoValidar.getValue().toString().trim());
			int numero_contrato_original = Integer.parseInt(String.valueOf(CodeUtil.getFromSessionMap("pmt_NumeroContratoValido")));

			if(numero_contrato_valida != numero_contrato_original){
				strMensajeValidacion = "El numero de Contrato actual es distinto al ingresado previamente ";
				return ;
			}
			
			int iCodigoClienteContrato = Integer.parseInt(String.valueOf( CodeUtil.getFromSessionMap("pmt_CodigoClienteContrato") ) ) ;
			int iNumeroClienteActual = Integer.parseInt(  txtBusquedaCodigoCliente.getValue().toString().trim() ) ;
		
			if(iCodigoClienteContrato != iNumeroClienteActual ){
				strMensajeValidacion = "El codigo de cliente actual no corresponde al código de cliente usado para la busqueda del contrato ";
				return ;
			}
			
			//&& ======================= validaciones de cuota seleccionada para pago de cuotas vencidas
			if(ddlTipoAnticiposIngreso.getValue().toString().compareTo("002") == 0 ){
				if(CodeUtil.getFromSessionMap("pmt_CuotaPendienteSeleccionada") == null ){
					strMensajeValidacion = "No se ha seleccionado cuota pendiente para procesar el pago aplicado a la cuota  ";
					return ;
				}
			}
			
			//&& ======================= validar si la caja no esta bloqueada 
			strMensajeValidacion = CtrlCajas.generarMensajeBlk();
			
			if (  !strMensajeValidacion.isEmpty()  ) 
				return;
			
			
		} catch (Exception e) {
			
			strMensajeValidacion = "No se han podido verificar los datos a usar en el recibo";
			e.printStackTrace(); 
			
		}finally{
			
			if( !strMensajeValidacion.isEmpty() ){
				dwMensajesValidacion.setWindowState("normal");
				dwTituloMensajeValidacion.setCaptionText("Calcular Cambio en Moneda Nacional");
				lblMensajeValidacion.setValue(strMensajeValidacion);
				CodeUtil.refreshIgObjects(new Object[]{dwMensajesValidacion, dwTituloMensajeValidacion, lblMensajeValidacion});
				
			}else{
				dwConfirmacionProcesaRecibo.setWindowState("normal");
			}
			
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public void calcularMontoCambioMonedaNacional(ActionEvent ev){
		boolean valido = true;
		String strMensajeValidacion = "";
		
		try {
			
			boolean pagosMonedaNacional = false;
			final String strMonedaBaseCompania = monedaBaseActual();
			
			lstFormasDePagoRecibidas = (ArrayList<MetodosPago>)CodeUtil.getFromSessionMap("pmt_lstFormasDePagoRecibidas");
			if(lstFormasDePagoRecibidas != null && !lstFormasDePagoRecibidas.isEmpty()){
				
				List<MetodosPago> mpMonedaBase = (ArrayList<MetodosPago>)
				CollectionUtils.select(lstFormasDePagoRecibidas, new Predicate(){
					@Override
					public boolean evaluate(Object o) {
						MetodosPago mp = (MetodosPago)o;
						return  mp.getMetodo().compareTo(MetodosPagoCtrl.EFECTIVO ) == 0 &&						
								mp.getMoneda().compareTo(strMonedaBaseCompania) == 0 ;
					}
				});
				pagosMonedaNacional =  (mpMonedaBase != null && !mpMonedaBase.isEmpty() );
			}
			if(pagosMonedaNacional){
				strMensajeValidacion = "Solo puede calcular el cambio cuando es en moneda extranjera";
				return;
			}
			
			
			String strMontoCambio = txtMontoCambio.getValue().toString().trim().replace(",", "");
			
			valido =  txtMontoCambio.getValue() != null &&  !txtMontoAplicadoRecibir.getValue().toString().trim().isEmpty()  ;
			if( !valido ){
				strMensajeValidacion = "Monto de Cambio inválido";
				return;
			}
			BigDecimal bdMontoCambioAplicado = new BigDecimal(strMontoCambio);
			if(  bdMontoCambioAplicado.compareTo(BigDecimal.ZERO) == -1 ){
				strMensajeValidacion = "Monto debe ser mayor o igual a Cero ";
				return;
			}
			
			String strMontoAplicar = txtMontoAplicadoRecibir.getValue().toString().trim().replace(",", "");
			BigDecimal bdMontoAplicado = new BigDecimal(strMontoAplicar);
			BigDecimal bdMontoRecibido = new BigDecimal(lblMontoRecibido.getValue().toString().replace(",", ""));
			BigDecimal bdMontoCambioSugerido = bdMontoRecibido.subtract(bdMontoAplicado);
			
			
			if( bdMontoRecibido.compareTo(bdMontoAplicado) == -1){
				strMensajeValidacion = "El Monto recibido aún es menor que el monto aplicado";
				return;
			}
			
			if( bdMontoCambioAplicado.compareTo( bdMontoRecibido.subtract(bdMontoAplicado).abs() ) == 1 ){
				strMensajeValidacion = "El Monto ingresado es mayor al correspondiente";
				return;
			}
			
			String monedaBase = monedaBaseActual();
			BigDecimal bdMontoCambioNac = BigDecimal.ZERO;
			BigDecimal tasaOficial  = tasaOficial();
		
			boolean bdMonedaAplicadaLocal = ddlMonedaAplicadaRecibo.getValue().toString().compareTo(monedaBase) == 0;
			
			bdMontoCambioSugerido = ( bdMonedaAplicadaLocal ) ? bdMontoCambioSugerido.divide(tasaOficial, 2, RoundingMode.HALF_UP) : bdMontoCambioSugerido ;
			bdMontoCambioNac = bdMontoCambioSugerido.subtract( bdMontoCambioAplicado ).multiply(tasaOficial).setScale(2, RoundingMode.HALF_UP);
			
			CodeUtil.putInSessionMap("pmt_MontoCompraDivisas", bdMontoCambioSugerido.subtract( bdMontoCambioAplicado ) );
			
			lblMontoCambioNacional.setValue(CodeUtil.toStringFormatAmountType(bdMontoCambioNac));
			txtMontoCambio.setValue(CodeUtil.toStringFormatAmountType(bdMontoCambioAplicado));
			
			CodeUtil.putInSessionMap("pmt_MontoCambioExt", bdMontoCambioAplicado);
			CodeUtil.putInSessionMap("pmt_MontoCambioNac", bdMontoCambioNac);
			
			
		} catch (Exception e) {
			strMensajeValidacion = "No se ha podido generar el equivalente de cambio en moneda nacional";
			e.printStackTrace(); 
		}finally{
			
			if( !strMensajeValidacion.isEmpty() ){
				dwMensajesValidacion.setWindowState("normal");
				dwTituloMensajeValidacion.setCaptionText("Calcular Cambio en Moneda Nacional");
				lblMensajeValidacion.setValue(strMensajeValidacion);
				CodeUtil.refreshIgObjects(new Object[]{dwMensajesValidacion, dwTituloMensajeValidacion, lblMensajeValidacion});
				
			}else{
				CodeUtil.refreshIgObjects(new Object[]{lblMontoCambioNacional, txtMontoCambio});
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void establecerMontoAplicadoRecibo(ActionEvent ev){
		boolean valido = true;
		String strMensajeValidacion = "";
		
		try {
			
			CodeUtil.removeFromSessionMap("pmt_ReciboConMontoAplicado");
			
			valido =  txtMontoAplicadoRecibir.getValue() != null &&  !txtMontoAplicadoRecibir.getValue().toString().trim().isEmpty()  ;
			if( !valido ){
				strMensajeValidacion = "Monto inválido";
				return;
			}
			
			String strMontoAplicar = txtMontoAplicadoRecibir.getValue().toString().trim().replace(",", "");
			BigDecimal bdNuevoMontoAplicar = new BigDecimal(strMontoAplicar);
			if(  bdNuevoMontoAplicar.compareTo(BigDecimal.ZERO) == 0 ){
				strMensajeValidacion = "Monto debe ser mayor que Cero ";
				return;
			}
			
			//&& =========== validar que el monto aplicado sea mayor que el monto registrado por las formas de pago 
			lstFormasDePagoRecibidas = (ArrayList<MetodosPago>)CodeUtil.getFromSessionMap("pmt_lstFormasDePagoRecibidas");
			if(lstFormasDePagoRecibidas != null && !lstFormasDePagoRecibidas.isEmpty()){
				
				BigDecimal montoRecibido = CodeUtil.sumPropertyValueFromEntityList(lstFormasDePagoRecibidas, "equivalente", false);
				if( montoRecibido.compareTo(bdNuevoMontoAplicar) == -1){
					strMensajeValidacion = "El monto ingresado es menor que el monto registrado en los pagos";
					return;
				}
			}
			
			
			String monedaBaseComp = monedaBaseActual();
			
			BigDecimal tasaParalela = tasaParalela(monedaBaseComp);
			
			String pendienteLocal = "-"  + CodeUtil.toStringFormatAmountType(bdNuevoMontoAplicar) ;
			if(ddlMonedaAplicadaRecibo.getValue().toString().compareTo(monedaBaseComp) != 0 ){
				pendienteLocal = "-" + CodeUtil.toStringFormatAmountType( (bdNuevoMontoAplicar.multiply( tasaParalela ) ) );
			}
			
			lblMontoRecibido.setValue("0.00");
			
			lblMontoPendiente.setValue("-" + CodeUtil.toStringFormatAmountType(bdNuevoMontoAplicar) );
			lblMontoPendienteNac.setValue(pendienteLocal);
			
			lblMontoPendiente.setStyle( lblMontoPendiente.getStyle() + "; color:red; ");
			lblMontoPendienteNac.setStyle(lblMontoPendienteNac.getStyle() + "; color:red; ");
			
			txtMontoCambio.setValue("0.00");
			lblMontoCambioNacional.setValue("0.00");
			
			CodeUtil.putInSessionMap("pmt_ReciboConMontoAplicado", bdNuevoMontoAplicar);
			
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}finally{
			
			valido = strMensajeValidacion.isEmpty();
			
			if(!valido){
				dwMensajesValidacion.setWindowState("normal");
				dwTituloMensajeValidacion.setCaptionText("Agregar forma de Pago");
				lblMensajeValidacion.setValue(strMensajeValidacion);
				CodeUtil.refreshIgObjects(new Object[]{dwMensajesValidacion, dwTituloMensajeValidacion, lblMensajeValidacion});
				
			}else{
				CodeUtil.refreshIgObjects(new Object[]{lblMontoRecibido, lblMontoPendiente, lblMontoPendienteNac, txtMontoCambio, lblMontoCambioNacional});
			}
			
		}
	}
	
	public String monedaBaseActual(){
		return	String.valueOf( ( ( (Object[])CodeUtil.getFromSessionMap("pmt_OBJCONFIGCOMP") )[1] ) ) ;
	}
	
	
	@SuppressWarnings("unchecked")
	public void removerFormaDePago(ActionEvent ev){
		try {

			RowItem ri = (RowItem) ev.getComponent().getParent().getParent();
			final MetodosPago mpRemover = (MetodosPago)DataRepeater.getDataRow(ri);
			
			lstFormasDePagoRecibidas = (List<MetodosPago>) CodeUtil.getFromSessionMap("pmt_lstFormasDePagoRecibidas");
			CollectionUtils.filter(lstFormasDePagoRecibidas, new Predicate(){
				@Override
				public boolean evaluate(Object o) {
					 return ! ( ( (MetodosPago)o ).getIdentificador() == mpRemover.getIdentificador() ) ;
				}
			});
			
			CodeUtil.putInSessionMap("pmt_lstFormasDePagoRecibidas", lstFormasDePagoRecibidas);
			
			actualizarResumenPagos();
			
			
			gvFormasDePagoRecibidas.dataBind();
			CodeUtil.refreshIgObjects(gvFormasDePagoRecibidas);
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}
	
	/*
	 * Agregar Forma de pago al recibo, validar los datos del pago y validar los montos contra moneda y monto aplicado 
	 * 
	 */
	
	@SuppressWarnings("unchecked")
	public void agregarformaDePago(ActionEvent ev){
		boolean valido = true;
		String strMensajeValidacion = null;
		CtrlPoliticas polCtrl = new CtrlPoliticas();
		
		try {
			
			//&& ==== validar que se haya seleccionado cliente.
			valido =   !( txtBusquedaCodigoCliente.getValue() == null || txtBusquedaCodigoCliente.getValue().toString().trim().isEmpty() ) ;
			if(!valido){
				strMensajeValidacion = " Seleccione el registo de cliente a utilizar ";
				return ;
			}
			
			//VALIDAR QUE EXISTA CONTRATO	
			if(txtNumeroContratoValidar.getValue() == null || txtNumeroContratoValidar.getValue().toString().length() == 0 ) {
				strMensajeValidacion = " Seleccione el contrato a utilizar ";
				return ;
			}
			
			String formadepago = ddlFormaDePago.getValue().toString();
			String monedaFormaPago = ddlMonedaPago.getValue().toString();
			
			strMensajeValidacion = validarFormaDePago(formadepago);
			if( ! ( valido = strMensajeValidacion.isEmpty() )  ){
				return ;
			}
			
			strMensajeValidacion =  validarMontosIngresados(formadepago);
			if( ! ( valido = strMensajeValidacion.isEmpty() )  ){
				return ;
			}
			
			String monedaBaseCompania = monedaBaseActual();
			
			//&& ====================== crear metodo de pago ================== && //
			String referencia1 = "";
			String referencia2 = "";
			String referencia3 = ""; 
			String referencia4 = "";
			String monedaAplicada = ddlMonedaAplicadaRecibo.getValue().toString();
			String formadepagodesc = MetodosPagoCtrl.descripcionMetodoPago(formadepago);
			String codigomarcatarjeta = "";
			String marcatarjeta = "";
			String codigobanco = "" ;
			
			if(formadepago.compareTo(MetodosPagoCtrl.CHEQUE) == 0){
				codigobanco = ddlCodigoBanco.getValue().toString().split("@")[2];
				referencia1 = ddlCodigoBanco.getValue().toString().split("@")[1];
				referencia2 = txtReferencia1.getValue().toString();
				referencia3 = txtReferencia2.getValue().toString();
				referencia4 = txtReferencia3.getValue().toString();
			}
			if (formadepago.compareTo(MetodosPagoCtrl.TARJETA) == 0) {
				referencia1 = ddlAfiliado.getValue().toString()/*.split("@")[1]*/;
				referencia2 = txtReferencia1.getValue().toString();
				referencia3 = txtReferencia2.getValue().toString();
				codigomarcatarjeta = ddlMarcaTarjeta.getValue().toString().split("@")[0];
				marcatarjeta = ddlMarcaTarjeta.getValue().toString().split("@")[1];
				
			}
			if (formadepago.compareTo(MetodosPagoCtrl.TRANSFERENCIA) == 0) {
				
				//validamos si la referencia agregada ya ha sido utilizada para el mismo banco 
				if(polCtrl.validarReferenciaBancaria(1,ddlCodigoBanco.getValue().toString().split("@")[1],  txtReferencia2.getValue().toString(),"T"))
				{
					dwMensajesValidacion.setWindowState("normal");
					dwTituloMensajeValidacion.setCaptionText("Error");
					lblMensajeValidacion.setValue("La referencia ya ha sido utilizada, favor verificar e ingresar una referencia válida");
					
					return ;
				}
				
				codigobanco = ddlCodigoBanco.getValue().toString().split("@")[2];
				referencia1 = ddlCodigoBanco.getValue().toString().split("@")[1];
				referencia2 = txtReferencia1.getValue().toString();
				referencia3 = txtReferencia2.getValue().toString();
			}
			if (formadepago.compareTo(MetodosPagoCtrl.DEPOSITO) == 0) {
				//validamos si la referencia agregada ya ha sido utilizada para el mismo banco 
				if(polCtrl.validarReferenciaBancaria(1,ddlCodigoBanco.getValue().toString().split("@")[1],  txtReferencia2.getValue().toString(),"N"))
				{
					dwMensajesValidacion.setWindowState("normal");
					dwTituloMensajeValidacion.setCaptionText("Error");
					lblMensajeValidacion.setValue("La referencia ya ha sido utilizada, favor verificar e ingresar una referencia válida ");
					
					return ;
				}
				
				codigobanco = ddlCodigoBanco.getValue().toString().split("@")[2];
				referencia1 = ddlCodigoBanco.getValue().toString().split("@")[1];
				referencia2 = txtReferencia2.getValue().toString();
			}
			
			//&& ================ monto ingresado y su equivalencia ================ && //
			String strMontoIngresado = txtMontoIngresado.getValue().toString().trim().replace(",", "");
			BigDecimal montoIngresado = new BigDecimal(strMontoIngresado).setScale(4, RoundingMode.HALF_UP);
			BigDecimal montoEquivalent = montoIngresado;
			BigDecimal tasaOficial  = tasaOficial();
			BigDecimal tasaParalela = tasaParalela(monedaBaseCompania);
			BigDecimal tasaPago = BigDecimal.ONE;
			
			if( monedaAplicada.compareTo(monedaBaseCompania) == 0 ){
				if(monedaFormaPago.compareTo(monedaBaseCompania) != 0){
					montoEquivalent = montoIngresado.multiply(tasaOficial).setScale(4, RoundingMode.HALF_UP);
					tasaPago = tasaOficial;
				} 
			}
			if( monedaAplicada.compareTo(monedaBaseCompania) != 0 ){
				if(monedaFormaPago.compareTo(monedaBaseCompania) == 0){
					montoEquivalent = montoIngresado.divide( tasaParalela, 4, RoundingMode.HALF_UP );
					tasaPago = tasaParalela;
				}
			}
			
			if(CodeUtil.getFromSessionMap("pmt_MontoAjustarDiferencial") != null ){
				BigDecimal bdMontoAjustar = (BigDecimal)CodeUtil.getFromSessionMap("pmt_MontoAjustarDiferencial") ;
				montoEquivalent = montoEquivalent.add(bdMontoAjustar);
			}
			
			final MetodosPago mp = new MetodosPago();
			mp.setMetododescrip(formadepagodesc);
			mp.setMetodo(formadepago);
			mp.setMoneda(monedaFormaPago);
			mp.setMonto( montoIngresado.doubleValue() );
			mp.setTasa( tasaPago );
			mp.setEquivalente(montoEquivalent.doubleValue());
			mp.setCodigobanco(codigobanco);
			mp.setReferencia(referencia1);
			mp.setReferencia2(referencia2);
			mp.setReferencia3(referencia3);
			mp.setReferencia4(referencia4);
			mp.setReferencia5("");
			mp.setReferencia6("");
			mp.setReferencia7("");	
			mp.setNombre( txtBusquedaNombreCliente.getValue().toString() );
			mp.setCodigomarcatarjeta(codigomarcatarjeta);
			mp.setMarcatarjeta(marcatarjeta);
			mp.setMontorecibido( montoIngresado );
			mp.setMontoendonacion(BigDecimal.ZERO);
			mp.setIncluyedonacion(false);
			mp.setIdentificador(Math.abs( ev.hashCode() ) ) ;
			
			lstFormasDePagoRecibidas = (List<MetodosPago>) CodeUtil.getFromSessionMap("pmt_lstFormasDePagoRecibidas");
			if( lstFormasDePagoRecibidas == null ){
				lstFormasDePagoRecibidas = new ArrayList<MetodosPago>();
			}
			
			if( !lstFormasDePagoRecibidas.isEmpty() ){
				
				final MetodosPago mpExistente = (MetodosPago)
				CollectionUtils.find(lstFormasDePagoRecibidas, new Predicate(){
					@Override
					public boolean evaluate(Object o) {
						MetodosPago mpTmp = (MetodosPago)o;
						return
						   mpTmp.getMoneda().compareTo(mp.getMoneda()) == 0 
						&& mpTmp.getMetodo().compareTo(mp.getMetodo()) == 0
						&& mpTmp.getReferencia().compareTo( mp.getReferencia() )   == 0
						&& mpTmp.getReferencia2().compareTo( mp.getReferencia2() ) == 0
						&& mpTmp.getReferencia3().compareTo( mp.getReferencia3() ) == 0
						&& mpTmp.getReferencia4().compareTo( mp.getReferencia4() ) == 0;
					}
				});
				
				if(mpExistente != null ){
					mp.setMontorecibido( mp.getMontorecibido().add( mpExistente.getMontorecibido() ) ) ;
					mp.setMonto( mp.getMonto() +  mpExistente.getMonto() );
					mp.setEquivalente( mp.getEquivalente() + mpExistente.getEquivalente() );
					
					CollectionUtils.filter(lstFormasDePagoRecibidas, new Predicate(){
						@Override
						public boolean evaluate(Object o) {
							 return ! ( ( (MetodosPago)o ).getIdentificador() == mpExistente.getIdentificador() ) ;
						}
					});
				}
			}
			
			lstFormasDePagoRecibidas.add(mp);
			CodeUtil.putInSessionMap("pmt_lstFormasDePagoRecibidas", lstFormasDePagoRecibidas);
			
			actualizarResumenPagos();
			
			desplegarIngresoFormaPago(formadepago);
			
			gvFormasDePagoRecibidas.dataBind();
			txtMontoIngresado.setValue("");
			
			CodeUtil.refreshIgObjects(new Object[]{ gvFormasDePagoRecibidas, txtMontoIngresado });
			
			
		} catch (Exception e) {
			valido = false;
			strMensajeValidacion = "Error de validacion de datos";
			e.printStackTrace(); 
		}finally{
			
			if(!valido){
				dwMensajesValidacion.setWindowState("normal");
				dwTituloMensajeValidacion.setCaptionText("Agregar forma de Pago");
				lblMensajeValidacion.setValue(strMensajeValidacion);
			}
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public String actualizarResumenPagos(){
		String strMensajeValidacion = "";
		
		try {
			
			CodeUtil.removeFromSessionMap(new String[]{"pmt_AplicaCambioExtranjero", "pmt_MontoCompraDivisas"} ) ;
			
			final String strMonedaBase = monedaBaseActual();
			
			boolean bdMonedaAplicadaLocal = ddlMonedaAplicadaRecibo.getValue().toString().compareTo(strMonedaBase) == 0;
			boolean bdPagosMonedaLocal = false; 
			
			
			List<MetodosPago> metodosPago = (ArrayList<MetodosPago>)CodeUtil.getFromSessionMap("pmt_lstFormasDePagoRecibidas");
			
			if(metodosPago != null && !metodosPago.isEmpty()){
				
				List<MetodosPago> mpMonedaBase = (ArrayList<MetodosPago>)
				CollectionUtils.select(metodosPago, new Predicate(){
					@Override
					public boolean evaluate(Object o) {
						MetodosPago mp = (MetodosPago)o;
						return  mp.getMetodo().compareTo(MetodosPagoCtrl.EFECTIVO ) == 0 &&						
								mp.getMoneda().compareTo(strMonedaBase) == 0 ;
					}
				});
				
				bdPagosMonedaLocal =  (mpMonedaBase != null && !mpMonedaBase.isEmpty() );
			}
			
			
			BigDecimal monto_Recibido = CodeUtil.sumPropertyValueFromEntityList(metodosPago, "equivalente", false);
			BigDecimal monto_Aplicado = monto_Recibido;
			
			
			if(CodeUtil.getFromSessionMap("pmt_ReciboConMontoAplicado") != null){
				monto_Aplicado = new BigDecimal ( String.valueOf( CodeUtil.getFromSessionMap("pmt_ReciboConMontoAplicado") ) ) ;
			} 
			
			BigDecimal monto_Pendiente = monto_Aplicado.subtract(monto_Recibido);
			
			//&& ============= Si el monto pendiente es menor que cero, entonces hay cambio, si no faltan pagos al recibo
			
			txtMontoAplicadoRecibir.setValue(CodeUtil.toStringFormatAmountType(monto_Aplicado));
			lblMontoRecibido.setValue(CodeUtil.toStringFormatAmountType(monto_Recibido));
			
			lblMontoPendiente.setValue("0.00" );
			lblMontoPendienteNac.setValue("0.00");
			lblMontoPendiente.setStyle( ( lblMontoPendiente.getStyle() == null )? "" :  lblMontoPendiente.getStyle().replace("color:red;", "") );
			lblMontoPendienteNac.setStyle( ( lblMontoPendienteNac.getStyle() == null )? "" :  lblMontoPendienteNac.getStyle().replace("color:red;", "") );
			
			
			BigDecimal bdCambioNacional = BigDecimal.ZERO;
			BigDecimal bdCambioExtranjero = BigDecimal.ZERO;
			
			if(monto_Pendiente.compareTo(BigDecimal.ZERO) == -1 ){
				
				BigDecimal tasaoficial = tasaOficial();
				BigDecimal tasaparalela = tasaParalela("USD");
				
				if(!bdPagosMonedaLocal){
					
					CodeUtil.putInSessionMap("pmt_AplicaCambioExtranjero", "1");
					bdCambioExtranjero = (bdMonedaAplicadaLocal) ? monto_Pendiente.divide(tasaoficial, 2, RoundingMode.HALF_UP)  : monto_Pendiente;
					
				}else{
					bdCambioNacional =	(bdMonedaAplicadaLocal) ? monto_Pendiente :  monto_Pendiente.multiply( tasaparalela ).setScale(2, RoundingMode.HALF_UP) ;
//					bdCambioNacional =	(bdMonedaAplicadaLocal) ? monto_Pendiente :  monto_Pendiente.multiply( tasaoficial ).setScale(2, RoundingMode.HALF_UP) ;
				}
			}
			
			CodeUtil.putInSessionMap("pmt_MontoCambioRealExt", bdCambioExtranjero.abs());
			CodeUtil.putInSessionMap("pmt_MontoCambioExt", bdCambioExtranjero);
			CodeUtil.putInSessionMap("pmt_MontoCambioNac", bdCambioNacional);
			
			txtMontoCambio.setValue(CodeUtil.toStringFormatAmountType( bdCambioExtranjero.abs() ) );
			lblMontoCambioNacional.setValue( CodeUtil.toStringFormatAmountType( bdCambioNacional.abs() ) );
			
			if(monto_Pendiente.compareTo(BigDecimal.ZERO) == 1 ){
				
				lblMontoPendiente.setValue( "-" + CodeUtil.toStringFormatAmountType( monto_Pendiente )  );
				lblMontoPendienteNac.setValue( "-" + CodeUtil.toStringFormatAmountType( monto_Pendiente )  );
			
				lblMontoPendiente.setStyle( lblMontoPendiente.getStyle() + "; color:red; ");
				lblMontoPendienteNac.setStyle(lblMontoPendienteNac.getStyle() + "; color:red; ");
				
				if( !bdMonedaAplicadaLocal ){
					BigDecimal tasaParalela = tasaParalela(strMonedaBase);
					lblMontoPendienteNac.setValue( "-" + CodeUtil.toStringFormatAmountType( monto_Pendiente.multiply(tasaParalela).setScale(2, RoundingMode.HALF_UP) )  );
				}
			}
			
			CodeUtil.refreshIgObjects(new Object[]{
					txtMontoAplicadoRecibir, lblMontoRecibido, 
					lblMontoPendiente, lblMontoPendienteNac, 
					txtMontoCambio, lblMontoCambioNacional});
			

			
		} catch (Exception e) {
			strMensajeValidacion = "Error al calcular el resumen de pagos en el recibo";
			e.printStackTrace(); 
		}finally{
			
		}
		return strMensajeValidacion;
	}
	
	/*
	 * Validar que la suma de montos no exceda el monto aplicado ( * de haber monto aplicado ) 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public String validarMontosIngresados(String formadepago){
		String strMensajeValidacion = "";
		
		try {
			
			CodeUtil.removeFromSessionMap( "pmt_MontoAjustarDiferencial" ) ; 
			
			String monto = txtMontoIngresado.getValue().toString().replace(",", "");
			
			boolean soloPagosNoAdmitenCambio = formadepago.compareTo(MetodosPagoCtrl.EFECTIVO ) != 0 ;
			
			if( CodeUtil.getFromSessionMap("pmt_ReciboConMontoAplicado") == null || !soloPagosNoAdmitenCambio){
				return "";
			}
			
			String monedaBase = monedaBaseActual();
			String monedaPago = ddlMonedaPago.getValue().toString();
			String monedaRecibo = ddlMonedaAplicadaRecibo.getValue().toString();
		
			BigDecimal monto_Aplicado = new BigDecimal ( String.valueOf( CodeUtil.getFromSessionMap("pmt_ReciboConMontoAplicado") ) ) ;
			BigDecimal monto_registrado =  new BigDecimal( monto.trim() ) ;
			
			if( monedaRecibo.compareTo(monedaBase) != 0 ){
				if( monedaRecibo.compareTo(monedaPago) != 0 ){
					BigDecimal tasaParalela = tasaParalela(monedaBase);
					monto_registrado = monto_registrado.divide(tasaParalela, 2, RoundingMode.HALF_UP);
				}
			}else{
				if( monedaRecibo.compareTo(monedaPago) != 0 ){
					BigDecimal tasaOficial = tasaOficial();
					monto_registrado = monto_registrado.multiply(tasaOficial).setScale(2, RoundingMode.HALF_UP);
				}
			}
			
			lstFormasDePagoRecibidas = (ArrayList<MetodosPago>)CodeUtil.getFromSessionMap("pmt_lstFormasDePagoRecibidas");
			
			if(lstFormasDePagoRecibidas != null && !lstFormasDePagoRecibidas.isEmpty()){
				
				List<MetodosPago> pagosNoAdmitenCambio = (ArrayList<MetodosPago>)
				CollectionUtils.select(lstFormasDePagoRecibidas, new Predicate(){
					@Override
					public boolean evaluate(Object o) {
						return ( (MetodosPago)o ).getMetodo().compareTo(MetodosPagoCtrl.EFECTIVO ) != 0 ;
					}
				});
				
				soloPagosNoAdmitenCambio  = ! ( 
							pagosNoAdmitenCambio == null || 
							pagosNoAdmitenCambio.isEmpty()  ||
							pagosNoAdmitenCambio.size() != lstFormasDePagoRecibidas.size()
							);
				
				if( soloPagosNoAdmitenCambio ){
					monto_registrado = monto_registrado.add( CodeUtil.sumPropertyValueFromEntityList(pagosNoAdmitenCambio, "equivalente", false) ) ;
				}
			} 
			
			if(monto_registrado.compareTo(monto_Aplicado) == 1){
				
				BigDecimal montoDiferencia = monto_Aplicado.subtract(monto_registrado);
				
				if( montoDiferencia.abs().compareTo( new BigDecimal("0.50") ) == 1  ) {
					return strMensajeValidacion ="El total de montos ingresados excede el monto aplicado";
				}
				CodeUtil.putInSessionMap("pmt_MontoAjustarDiferencial", montoDiferencia);
			} 
			
		} catch (Exception e) {
			strMensajeValidacion = "Error al validar sumatoria de montos ingresados " ;
			e.printStackTrace(); 
		}
		
		return strMensajeValidacion;
	}
	
	@SuppressWarnings("unchecked")
	public String validarFormaDePago(String formadepago){
		String strMensajeValidacion = "";
		String regexAlfanum = "^[A-Za-z0-9-\\p{Blank}]{1,150}$";
		
		try {
			
			String monto = txtMontoIngresado.getValue().toString().replace(",", "");
			if( monto.isEmpty() || new BigDecimal(monto.trim()).setScale(2, RoundingMode.HALF_UP).compareTo(BigDecimal.ZERO)  == 0){
				return strMensajeValidacion = "Monto Invalido";
			}
			
			if( CodeUtil.getFromSessionMap("pmt_ReciboConMontoAplicado") != null &&  CodeUtil.getFromSessionMap("pmt_lstFormasDePagoRecibidas") != null ){
				
				lstFormasDePagoRecibidas = (ArrayList<MetodosPago>)CodeUtil.getFromSessionMap("pmt_lstFormasDePagoRecibidas");
				
				BigDecimal monto_Aplicado = new BigDecimal ( String.valueOf( CodeUtil.getFromSessionMap("pmt_ReciboConMontoAplicado") ) ) ;
				BigDecimal monto_Recibido = CodeUtil.sumPropertyValueFromEntityList(lstFormasDePagoRecibidas, "equivalente", false);
				
				if( monto_Recibido.compareTo( monto_Aplicado ) == 1  ){
					return strMensajeValidacion = "El monto aplicado ya fue alcanzado por los pagos registrados";
				}
			}


			String refer1 = txtReferencia1.getValue().toString().trim();
			String refer2 = txtReferencia2.getValue().toString().trim();
			String refer3 = txtReferencia3.getValue().toString().trim();
			
			if(formadepago.compareTo(MetodosPagoCtrl.TARJETA) == 0){
			
				if(!refer1.matches(regexAlfanum)){
					return strMensajeValidacion = "Número de identificación requerido";
				}
				if(!refer2.matches(PropertiesSystem.REGEXP_8DIGTS)){
					return strMensajeValidacion = "8 Dígitos para número de cheque es requerido ";
				}
				return "";
			}

			//&& ================ Cheques
			if(formadepago.compareTo(MetodosPagoCtrl.CHEQUE) == 0){
				if(!refer1.matches(PropertiesSystem.REGEXP_8DIGTS)){
					return strMensajeValidacion = "8 Dígitos para número de cheque es requerido ";
				}
				if(!refer2.matches(regexAlfanum)){
					return strMensajeValidacion = "Datos Invalidos para Emisior de Cheque";
				}
				if(!refer3.matches(regexAlfanum)){
					return strMensajeValidacion = "Datos Invalidos para Portador de Cheque";
				}
				return "";
			}
			//&& ================ Transferencias
			if(formadepago.compareTo(MetodosPagoCtrl.TRANSFERENCIA) == 0){
				if(!refer1.matches(regexAlfanum)){
					return strMensajeValidacion =  "Datos inválidos para el campo Identificaión (1-100 letras)";
				}
				if(!refer2.matches(PropertiesSystem.REGEXP_8DIGTS)){
					return strMensajeValidacion = "Datos inválidos para número de referencia (1 a 8 dígitos)";
				}
			}
			//&& ================ Deposito directo en banco
			if(formadepago.compareTo(MetodosPagoCtrl.DEPOSITO) == 0){
				if(!refer2.matches(PropertiesSystem.REGEXP_8DIGTS)){
					return strMensajeValidacion ="Datos inválidos para número de referencia (1 a 8 dígitos)";
				}
				return "";
			}
			
			
		} catch (Exception e) {
			strMensajeValidacion = "Error de validacion de datos";
			e.printStackTrace(); 
		}
		return strMensajeValidacion;
	}
	
	public void cambiarMonedaAplicadaRecibo(ValueChangeEvent vce){
		try {
			
			lstFormasDePagoRecibidas = new ArrayList<MetodosPago>();
			CodeUtil.putInSessionMap("pmt_lstFormasDePagoRecibidas", lstFormasDePagoRecibidas);
			gvFormasDePagoRecibidas.dataBind();
			CodeUtil.refreshIgObjects(gvFormasDePagoRecibidas);
			
			desplegarIngresoFormaPago(MetodosPagoCtrl.EFECTIVO );
			ddlFormaDePago.setValue(MetodosPagoCtrl.EFECTIVO );
			
			String contrato_moneda =String.valueOf( CodeUtil.getFromSessionMap("pmt_MonedaContrato")  ) ;
			
			if(contrato_moneda != null)
			{
				if(contrato_moneda != "")
				{
					lstMonedaAplicadaRecibo = new ArrayList<SelectItem>();
					lstMonedaAplicadaRecibo.add(new SelectItem(contrato_moneda, contrato_moneda, contrato_moneda));
				}
			}
			
			establecerResumenPagos();
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}
	
	public void cambiarMonedaSeleccionada(ValueChangeEvent vce){
		try {
			
			String formadepago = ddlFormaDePago.getValue().toString();
			
			if(formadepago.compareTo(MetodosPagoCtrl.TARJETA) != 0)
				return;
			
			CodeUtil.removeFromSessionMap("pmt_lstAfiliado")     ;
			ddlAfiliado.dataBind();
			
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}
	
	public void cambiarMetodoPago(ValueChangeEvent vce) {
		try {
			
			String nuevaformadepago = ddlFormaDePago.getValue().toString();
			desplegarIngresoFormaPago(nuevaformadepago);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void desplegarIngresoFormaPago(String formadePago){
		String displayNone = "display:none;";
		Object[] objects = null ;
		
		try {
			
			objects = new Object[]{ ddlFormaDePago,
					lblReferencia1, lblReferencia2, lblReferencia3, 
					txtReferencia1, txtReferencia2, txtReferencia3,
					lblCodigoBanco, ddlCodigoBanco, lblAfiliado,
					ddlAfiliado, lblMarcaTarjeta, ddlMarcaTarjeta};
			
			ddlFormaDePago.setValue(formadePago);
			
			lblReferencia1.setStyle( lblReferencia1.getStyle() +"; "+ displayNone);
			lblReferencia2.setStyle( lblReferencia2.getStyle() +"; "+ displayNone);
			lblReferencia3.setStyle( lblReferencia3.getStyle() +"; "+ displayNone);
			txtReferencia1.setStyle( txtReferencia1.getStyle() +"; "+ displayNone);
			txtReferencia2.setStyle( txtReferencia2.getStyle() +"; "+ displayNone);
			txtReferencia3.setStyle( txtReferencia3.getStyle() +"; "+ displayNone);

			lblCodigoBanco.setStyle( lblCodigoBanco.getStyle() +"; "+ displayNone);
			ddlCodigoBanco.setStyle( ddlCodigoBanco.getStyle() +"; "+ displayNone);
			
			lblAfiliado.setStyle( lblAfiliado.getStyle() +"; "+ displayNone);
			ddlAfiliado.setStyle( ddlAfiliado.getStyle() +"; "+ displayNone);
			
			lblMarcaTarjeta.setStyle( lblMarcaTarjeta.getStyle() +"; "+ displayNone);
			ddlMarcaTarjeta.setStyle( ddlMarcaTarjeta.getStyle() +"; "+ displayNone);
			
			txtReferencia1.setValue("");
			txtReferencia2.setValue("");
			txtReferencia3.setValue("");

			CodeUtil.removeFromSessionMap("pmt_lstFormaDePago")  ;
			CodeUtil.removeFromSessionMap("pmt_lstMonedaPago")   ;
			CodeUtil.removeFromSessionMap("pmt_lstAfiliado")     ;
			CodeUtil.removeFromSessionMap("pmt_lstCodigoBanco")  ;
//			CodeUtil.removeFromSessionMap("pmt_lstMarcaTarjeta") ;
			
			ddlAfiliado.dataBind();
			ddlMarcaTarjeta.dataBind();
			ddlCodigoBanco.dataBind();
			
			if(formadePago.compareTo(MetodosPagoCtrl.TARJETA) == 0){
				lblAfiliado.setStyle(lblAfiliado.getStyle().replace(displayNone, ""));
				ddlAfiliado.setStyle(ddlAfiliado.getStyle().replace(displayNone, ""));
				lblMarcaTarjeta.setStyle(lblMarcaTarjeta.getStyle().replace(displayNone, ""));
				ddlMarcaTarjeta.setStyle(ddlMarcaTarjeta.getStyle().replace(displayNone, ""));
				lblReferencia1.setStyle(lblReferencia1.getStyle().replace(displayNone, ""));
				lblReferencia2.setStyle(lblReferencia2.getStyle().replace(displayNone, ""));
				txtReferencia1.setStyle(txtReferencia1.getStyle().replace(displayNone, ""));
				txtReferencia2.setStyle(txtReferencia2.getStyle().replace(displayNone, ""));
				lblReferencia1.setValue("Identificacion");
				lblReferencia2.setValue("No. Voucher");
				return;
			}
			if(formadePago.compareTo(MetodosPagoCtrl.CHEQUE) == 0){
				lblReferencia1.setStyle(lblReferencia1.getStyle().replace(displayNone, ""));
				lblReferencia2.setStyle(lblReferencia2.getStyle().replace(displayNone, ""));
				lblReferencia3.setStyle(lblReferencia3.getStyle().replace(displayNone, ""));
				txtReferencia1.setStyle(txtReferencia1.getStyle().replace(displayNone, ""));
				txtReferencia2.setStyle(txtReferencia2.getStyle().replace(displayNone, ""));
				txtReferencia3.setStyle(txtReferencia3.getStyle().replace(displayNone, ""));
				lblReferencia1.setValue("Número");
				lblReferencia2.setValue("Emisor");
				lblReferencia3.setValue("Portador");
				lblCodigoBanco.setStyle( ddlCodigoBanco.getStyle().replace(displayNone, ""));
				ddlCodigoBanco.setStyle( ddlCodigoBanco.getStyle().replace(displayNone, "")  );
				return;
			}
			if(formadePago.compareTo(MetodosPagoCtrl.DEPOSITO) == 0){
				lblReferencia2.setStyle(lblReferencia2.getStyle().replace(displayNone, ""));
				txtReferencia2.setStyle(txtReferencia2.getStyle().replace(displayNone, ""));
				lblReferencia2.setValue("Referencia");
				lblCodigoBanco.setStyle( ddlCodigoBanco.getStyle().replace(displayNone, ""));
				ddlCodigoBanco.setStyle( ddlCodigoBanco.getStyle().replace(displayNone, "")  );
				return;
			}
			if(formadePago.compareTo(MetodosPagoCtrl.TRANSFERENCIA) == 0){
				lblReferencia1.setStyle(lblReferencia1.getStyle().replace(displayNone, ""));
				lblReferencia2.setStyle(lblReferencia2.getStyle().replace(displayNone, ""));
				txtReferencia1.setStyle(txtReferencia1.getStyle().replace(displayNone, ""));
				txtReferencia2.setStyle(txtReferencia2.getStyle().replace(displayNone, ""));
				lblReferencia1.setValue("Identificación");
				lblReferencia2.setValue("Referencia");
				lblCodigoBanco.setStyle( ddlCodigoBanco.getStyle().replace(displayNone, ""));
				ddlCodigoBanco.setStyle( ddlCodigoBanco.getStyle().replace(displayNone, "")  );
				return;
			}
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}finally{
			CodeUtil.refreshIgObjects(objects);
		}
	}
	
	
	public void establecerResumenPagos(){
		try {
			
			CodeUtil.removeFromSessionMap("pmt_ReciboConMontoAplicado");
			
			txtMontoAplicadoRecibir.setValue("0.00");
			lblMontoRecibido.setValue("0.00");
			lblMontoPendiente.setValue("0.00" );
			lblMontoPendienteNac.setValue("0.00");
			txtMontoCambio.setValue("0.00");
			lblMontoCambioNacional.setValue("0.00");
			
			lblMontoPendiente.setStyle( ( lblMontoPendiente.getStyle() == null )? "" :  lblMontoPendiente.getStyle().replace("color:red;", "") );
			lblMontoPendienteNac.setStyle( ( lblMontoPendienteNac.getStyle() == null )? "" :  lblMontoPendienteNac.getStyle().replace("color:red;", "") );
			
			CodeUtil.refreshIgObjects(new Object[]{
					txtMontoAplicadoRecibir, lblMontoRecibido, 
					lblMontoPendiente, lblMontoPendienteNac, 
					txtMontoCambio, lblMontoCambioNacional});
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}
	
	
	public void establecerMonedaAplicada() {
		try {

			String monedaDeLinea = "" ;
			
			String codunineg = ddlFiltroUnidadNegocio.getValue().toString().trim();
			
			if(codunineg.compareTo(SELECT_ITEM_FIRST_VALUE) != 0 ){
				String lineaneg = CompaniaCtrl.leerLineaNegocio(codunineg);
				monedaDeLinea = CompaniaCtrl.obtenerMonedaLineaN(lineaneg);
			}

			if (!monedaDeLinea.isEmpty()) {
				lstMonedaAplicadaRecibo = new ArrayList<SelectItem>();
				lstMonedaAplicadaRecibo.add(new SelectItem(monedaDeLinea, monedaDeLinea, monedaDeLinea));
			} else {
				CodeUtil.removeFromSessionMap("pmt_lstMonedaAplicadaRecibo");
				
				String contrato_moneda =String.valueOf( CodeUtil.getFromSessionMap("pmt_MonedaContrato")  ) ;
				
				if(contrato_moneda != null)  //contrato_moneda != null
				{
					if(contrato_moneda != "" && contrato_moneda != "null")
					{
						lstMonedaAplicadaRecibo = new ArrayList<SelectItem>();
						lstMonedaAplicadaRecibo.add(new SelectItem(contrato_moneda, contrato_moneda, contrato_moneda));
					}
					else
						lstMonedaAplicadaRecibo = getLstMonedaAplicadaRecibo();
				}
				else
					lstMonedaAplicadaRecibo = getLstMonedaAplicadaRecibo();
				
				
			}
			
			establecerResumenPagos();
			

		} catch (Exception e) {
			lstMonedaAplicadaRecibo = new ArrayList<SelectItem>();
			e.printStackTrace(); 
		} finally {
			CodeUtil.putInSessionMap("pmt_lstMonedaAplicadaRecibo", lstMonedaAplicadaRecibo);
			ddlMonedaAplicadaRecibo.dataBind();
			CodeUtil.refreshIgObjects(ddlMonedaAplicadaRecibo);
		}
	}
	
	
	public void seleccionarUnidadNegocios(ValueChangeEvent vce) {
			establecerMonedaAplicada();
	}
	public void seleccionarSucursal(ValueChangeEvent vce) {
		List<Object> refreshlist = new ArrayList<Object>();
			
		try {
			
			lstFiltroUnidadNegocio = unidadesPorSucursal(ddlFiltroSucursal.getValue().toString().trim(),ddlFiltroCompania.getValue().toString().trim());
			CodeUtil.putInSessionMap("pmt_lstFiltroUnidadNegocio", lstFiltroUnidadNegocio) ;
			ddlFiltroUnidadNegocio.dataBind();
			refreshlist.add(ddlFiltroUnidadNegocio);
			
			establecerMonedaAplicada();
			
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}finally{
			CodeUtil.refreshIgObjects( refreshlist.toArray() );
		}
	}
	public void seleccionarCompania(ValueChangeEvent vce) {
		List<Object> refreshlist = new ArrayList<Object>();
		
		try {
			
			generarObjConfigComp( ddlFiltroCompania.getValue().toString() );
			
			lstFiltroSucursal = sucursalesPorCompania( ddlFiltroCompania.getValue().toString() );
			CodeUtil.putInSessionMap("pmt_lstFiltroSucursal", lstFiltroSucursal) ;
			ddlFiltroSucursal.dataBind();
			refreshlist.add(ddlFiltroSucursal);
			
			lstFiltroUnidadNegocio = unidadesPorSucursal(ddlFiltroSucursal.getValue().toString().trim(),ddlFiltroCompania.getValue().toString().trim());
			CodeUtil.putInSessionMap("pmt_lstFiltroUnidadNegocio", lstFiltroUnidadNegocio) ;
			ddlFiltroUnidadNegocio.dataBind();
			refreshlist.add(ddlFiltroUnidadNegocio);
			
			establecerMonedaAplicada();
			restablecerResumenContrato();
			
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}finally{
			CodeUtil.refreshIgObjects( refreshlist.toArray() );
		}
	}
	
	
	public void ocultarBusquedaCliente(ActionEvent ev){
		dwBusquedaClientes.setWindowState("hidden");
	}
	public void seleccionarCliente(ActionEvent ev){
		boolean valido = true;
		
		try {
			
			RowItem ri = (RowItem) ev.getComponent().getParent().getParent();
			Vf0101 vf = (Vf0101)DataRepeater.getDataRow(ri);
			
			txtBusquedaCodigoCliente.setValue( vf.getId().getAban8() );
			txtBusquedaNombreCliente.setValue( CodeUtil.capitalize( vf.getId().getAbalph().trim() ) );
			
			restablecerResumenContrato();
			
			CodeUtil.refreshIgObjects(new Object[]{txtBusquedaCodigoCliente,txtBusquedaNombreCliente });
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}finally{
			if(valido){
				dwBusquedaClientes.setWindowState("hidden");
			}
		}
	}
	@SuppressWarnings("unchecked")
	public void buscarClientes(ActionEvent ev){
		
		try{
	 
			if(txtParametroBusquedaCliente.getValue().toString().trim().isEmpty())
				return;
			
			String param = txtParametroBusquedaCliente.getValue().toString().trim();
			String tipob = ddlTipoBusquedaCliente.getValue().toString();
			
			String strSql = "select * from "+PropertiesSystem.ESQUEMA+".Vf0101 where abxab = '' and lower(@CAMPO) like '%"+param.toLowerCase()+"%'";
			
			if(tipob.compareTo("01") == 0){
				strSql = "select * from "+PropertiesSystem.ESQUEMA+".Vf0101 where abxab = '' and aban8 = " +param;
			}
			
			if(tipob.compareTo("02") == 0){
			
				int contar=0;
				String[] valores1 = param.split(" ");
				for (String valor : valores1) {
					if( valor.trim().length() == 1 )
						contar++;
				}
				if(contar == valores1.length){
					strSql = "select * from "+PropertiesSystem.ESQUEMA+".Vf0101 where abxab = '' and lower(abalph) like '%"+param.trim()+"%'";
				}else{
					
					strSql = "select * from "+PropertiesSystem.ESQUEMA+".Vf0101 where abxab = '' ";
					String[] valores = param.split(" ");
					for (String valor : valores) {
						
						if(valor.trim().length() < 3)
							continue;
						
						strSql += " and lower(abalph) like '%"+valor.toLowerCase()+"%'" ;
						
					}
				}
			}
			
			
			strSql += " and aban8 in ( select distinct(cliente) from "+PropertiesSystem.QS36F+".sotmpc ) ";
			
			strSql += " fetch first 100 rows only ";
			
			lstClientesDisponibles = (ArrayList<Vf0101>) ConsolidadoDepositosBcoCtrl.executeSqlQuery( strSql, true, Vf0101.class );
			
			Collections.sort(lstClientesDisponibles, new Comparator<Vf0101>(){
				public int compare(Vf0101 d1, Vf0101 d2) {
					return d1.getId().getAbalph().trim().compareToIgnoreCase(d2.getId().getAbalph().trim() )  ;
				}
			});
			
			CodeUtil.putInSessionMap("pmt_lstClientesDisponibles", lstClientesDisponibles);
			gvClientesDisponibles.dataBind(); 
			
			CodeUtil.refreshIgObjects(gvClientesDisponibles);
			
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}

	public void cerrarVentanaMensajeValidacion(ActionEvent ev){
		dwMensajesValidacion.setWindowState("hidden");
	}
	
	public void mostrarBusquedaClientes(ActionEvent ev){
		boolean mostrar = true;
		
		try {
			
			txtParametroBusquedaCliente.setValue("");
			ddlTipoBusquedaCliente.dataBind();
		
			lstClientesDisponibles = new ArrayList<Vf0101>();
			CodeUtil.putInSessionMap("pmt_lstClientesDisponibles", lstClientesDisponibles);
			gvClientesDisponibles.dataBind();
			
			ddlFormaDePago.setValue(MetodosPagoCtrl.EFECTIVO );
			desplegarIngresoFormaPago(MetodosPagoCtrl.EFECTIVO );
			
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}finally{
			if(mostrar){
				dwBusquedaClientes.setWindowState("normal");
			}
		}
	}
	
	public BigDecimal tasaOficial(){
		BigDecimal tasa = BigDecimal.ONE;
		try {
			Tcambio[] tcambio ;
			if(CodeUtil.getFromSessionMap("pmt_tcambio") == null){
				String fecha = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
				tcambio = new TasaCambioCtrl().obtenerTasaCambioJDExFecha(fecha);
				CodeUtil.putInSessionMap( "pmt_tcambio", tcambio);
			}
			tcambio = (Tcambio[])CodeUtil.getFromSessionMap("pmt_tcambio");
			for(int l = 0; l < tcambio.length;l++){
				if(tcambio[l].getId().getCxcrcd().equals("COR")){
					tasa = tcambio[l].getId().getCxcrrd();
					break;
				}
			}	
		} catch (Exception e) {
			 tasa = BigDecimal.ONE;
		}
		return tasa;
	}

	public BigDecimal tasaParalela(String moneda) {
		BigDecimal tasa = BigDecimal.ONE;
		try {
			
			Tpararela[] tpcambio ;
			if( CodeUtil.getFromSessionMap("tpcambio") == null ){
				tpcambio = TasaCambioCtrl.obtenerTasaCambioParalela();
				CodeUtil.putInSessionMap("tpcambio", tpcambio);
			}
			
			tpcambio = (Tpararela[]) CodeUtil.getFromSessionMap("tpcambio");
			for (int t = 0; t < tpcambio.length; t++) {
				if (tpcambio[t].getId().getCmono().equals(moneda)
						|| tpcambio[t].getId().getCmond().equals(moneda)) {
					tasa = tpcambio[t].getId().getTcambiom();
					break;
				}
			}
		} catch (Exception e) {
			tasa = BigDecimal.ONE;
		}
		return tasa;
	}
	
	public void generarObjConfigComp(String sCodcomp){
		String sMonedaBase = "";
		Object[] objConfigComp = new Object[4];
		String sUsarTasa = "1";
		String[] lstMonedaCC = null;
		
		try {
			
			CodeUtil.removeFromSessionMap("pmt_OBJCONFIGCOMP");
			
			int caid = Integer.parseInt( CodeUtil.getFromSessionMap(("sCajaId") ).toString() );
			F55ca014[] f14_ConfigComp = (F55ca014[])CodeUtil.getFromSessionMap("pmt_F55CA014");
						
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
			CodeUtil.putInSessionMap( "pmt_OBJCONFIGCOMP", objConfigComp);
			
		} catch (Exception e) {
			objConfigComp = null;
			e.printStackTrace(); 
		}
	}
	
	
	
	public HtmlDialogWindow getDwBusquedaClientes() {
		return dwBusquedaClientes;
	}
	public void setDwBusquedaClientes(HtmlDialogWindow dwBusquedaClientes) {
		this.dwBusquedaClientes = dwBusquedaClientes;
	}
	public HtmlInputText getTxtBusquedaCodigoCliente() {
		return txtBusquedaCodigoCliente;
	}
	public void setTxtBusquedaCodigoCliente(HtmlInputText txtBusquedaCodigoCliente) {
		this.txtBusquedaCodigoCliente = txtBusquedaCodigoCliente;
	}
	public HtmlInputText getTxtBusquedaNombreCliente() {
		return txtBusquedaNombreCliente;
	}
	public void setTxtBusquedaNombreCliente(HtmlInputText txtBusquedaNombreCliente) {
		this.txtBusquedaNombreCliente = txtBusquedaNombreCliente;
	}
	public List<SelectItem> getLstTipoAnticiposIngreso() {
		lstTipoAnticiposIngreso = new ArrayList<SelectItem>();
		lstTipoAnticiposIngreso.add(new SelectItem("001"," Anticipo Nuevo Contrato PMT "));
		lstTipoAnticiposIngreso.add(new SelectItem("002","Pago de Cuotas de PMT"));
		return lstTipoAnticiposIngreso;
	}
	public void setLstTipoAnticiposIngreso(List<SelectItem> lstTipoAnticiposIngreso) {
		this.lstTipoAnticiposIngreso = lstTipoAnticiposIngreso;
	}
	@SuppressWarnings("unchecked")
	public List<SelectItem> getLstFiltroCompania() {
		
		try{
		
			if (CodeUtil.getFromSessionMap("pmt_lstFiltroCompania") != null)
				return lstFiltroCompania = (ArrayList<SelectItem>) CodeUtil.getFromSessionMap("pmt_lstFiltroCompania");
			
			int caid = ( (List<Vf55ca01>) CodeUtil.getFromSessionMap( "lstCajas" ) ).get(0).getId().getCaid();
			F55ca014[] lstComxCaja = CompaniaCtrl.obtenerCompaniasxCaja(caid);
			
			lstFiltroCompania = new ArrayList<SelectItem>();
			
			for (F55ca014 comp : lstComxCaja) {
				lstFiltroCompania.add(new SelectItem(comp.getId().getC4rp01().trim(), 
							comp.getId().getC4rp01d1().trim(), 
							comp.getId().getC4rp01().trim() +" " + comp.getId().getC4rp01d1().trim()  ) ) ;
			} 
			
			CodeUtil.putInSessionMap("pmt_F55CA014", lstComxCaja);
			CodeUtil.putInSessionMap("pmt_lstFiltroCompania", lstFiltroCompania) ;
			
			generarObjConfigComp( lstComxCaja[0].getId().getC4rp01().trim() );
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		
		return lstFiltroCompania;
	}
	public void setLstFiltroCompania(List<SelectItem> lstFiltroCompania) {
		this.lstFiltroCompania = lstFiltroCompania;
	}
	@SuppressWarnings("unchecked")
	public List<SelectItem> getLstFiltroSucursal() {
		
		try {
			
			if(CodeUtil.getFromSessionMap("pmt_lstFiltroSucursal") != null){
				return  lstFiltroSucursal = (ArrayList<SelectItem>)CodeUtil.getFromSessionMap("pmt_lstFiltroSucursal") ;
			}
			
			lstFiltroSucursal =  sucursalesPorCompania( ddlFiltroCompania.getValue().toString() );
			
			CodeUtil.putInSessionMap("pmt_lstFiltroSucursal", lstFiltroSucursal ) ;
			
		} catch (Exception e) {
			e.printStackTrace(); 
			lstFiltroSucursal = new ArrayList<SelectItem>();
		}
		
		return lstFiltroSucursal;
	}
	public void setLstFiltroSucursal(List<SelectItem> lstFiltroSucursal) {
		this.lstFiltroSucursal = lstFiltroSucursal;
	}
	
	public List<SelectItem> sucursalesPorCompania(String codcomp){
		List<SelectItem> sucursalesPorCompania =  new ArrayList<SelectItem>();
		

		List<String[]>  unegocio2 = null;
		com.casapellas.controles.SucursalCtrl sucCtrl = new com.casapellas.controles.SucursalCtrl();
		try {
			
			
			sucursalesPorCompania.add(new SelectItem(SELECT_ITEM_FIRST_VALUE, "Seleccione Sucursal" , "Seleccione Sucursal"));
			
			unegocio2 = sucCtrl.obtenerSucursalesxCompania( codcomp);
			
			for (Object[] sucursal : unegocio2) {
			
				String unineg = String.valueOf(sucursal[0]).trim();
				
				sucursalesPorCompania.add(new SelectItem(String.valueOf(sucursal[0]),
						unineg +": "+ String.valueOf(sucursal[2]).trim(),
								String.valueOf(sucursal[2]).trim()));
				
			}
			
		} catch (Exception e) {
			sucursalesPorCompania =  new ArrayList<SelectItem>();
			sucursalesPorCompania.add(new SelectItem(SELECT_ITEM_FIRST_VALUE, "Seleccione Sucursal" , "Seleccione Sucursal"));
			e.printStackTrace(); 
		}
		return sucursalesPorCompania ;
	}
	
	public List<SelectItem> unidadesPorSucursal(String codsuc, String codcomp){
		List<SelectItem> unidades =  new ArrayList<SelectItem>();
		try {
			
			
			unidades.add(new SelectItem(SELECT_ITEM_FIRST_VALUE, "Seleccione Unidad Negocio" , "Seleccione Unidad Negocio"));
			
			if(codsuc.compareTo(SELECT_ITEM_FIRST_VALUE) == 0){
				return unidades ;
			}
			
		
			
			List<String[]>  unegocio2 = null;
			com.casapellas.controles.SucursalCtrl sucCtrl = new com.casapellas.controles.SucursalCtrl();
			
			unegocio2 = sucCtrl.obtenerUninegxSucursal(codsuc,codcomp);
			
			if( unegocio2 == null  ){
				return unidades;
			}
			
			
			for (Object[] unidad : unegocio2) {
				unidades.add(new SelectItem(String.valueOf(unidad[0]),
					String.valueOf(unidad[0]) +": "+ String.valueOf(unidad[2]).trim(),
					"U/N: " +String.valueOf(unidad[2]).trim()));	
		}
			
			
		} catch (Exception e) {
			unidades =  new ArrayList<SelectItem>();
			unidades.add(new SelectItem(SELECT_ITEM_FIRST_VALUE, "Seleccione Unidad Negocio" , "Seleccione Unidad Negocio"));
			e.printStackTrace(); 
		}
		return unidades ;
	}
	
	@SuppressWarnings("unchecked")
	public List<SelectItem> getLstFiltroUnidadNegocio() {
		
		try {
			
			if(CodeUtil.getFromSessionMap("pmt_lstFiltroUnidadNegocio") != null){
				return  lstFiltroUnidadNegocio = (ArrayList<SelectItem>)CodeUtil.getFromSessionMap("pmt_lstFiltroUnidadNegocio") ;
			}
			
			lstFiltroUnidadNegocio =  unidadesPorSucursal( ddlFiltroSucursal.getValue().toString().trim(),ddlFiltroCompania.getValue().toString().trim() ) ;
			
			CodeUtil.putInSessionMap("pmt_lstFiltroUnidadNegocio", lstFiltroUnidadNegocio ) ;
			
		} catch (Exception e) {
			e.printStackTrace(); 
			lstFiltroUnidadNegocio = new ArrayList<SelectItem>();
		}
		
		return lstFiltroUnidadNegocio;
	}
	public void setLstFiltroUnidadNegocio(List<SelectItem> lstFiltroUnidadNegocio) {
		this.lstFiltroUnidadNegocio = lstFiltroUnidadNegocio;
	}
	public HtmlDropDownList getDdlTipoAnticiposIngreso() {
		return ddlTipoAnticiposIngreso;
	}
	public void setDdlTipoAnticiposIngreso(HtmlDropDownList ddlTipoAnticiposIngreso) {
		this.ddlTipoAnticiposIngreso = ddlTipoAnticiposIngreso;
	}
	public HtmlDropDownList getDdlFiltroCompania() {
		return ddlFiltroCompania;
	}
	public void setDdlFiltroCompania(HtmlDropDownList ddlFiltroCompania) {
		this.ddlFiltroCompania = ddlFiltroCompania;
	}
	public HtmlDropDownList getDdlFiltroSucursal() {
		return ddlFiltroSucursal;
	}
	public void setDdlFiltroSucursal(HtmlDropDownList ddlFiltroSucursal) {
		this.ddlFiltroSucursal = ddlFiltroSucursal;
	}
	public HtmlDropDownList getDdlFiltroUnidadNegocio() {
		return ddlFiltroUnidadNegocio;
	}
	public void setDdlFiltroUnidadNegocio(HtmlDropDownList ddlFiltroUnidadNegocio) {
		this.ddlFiltroUnidadNegocio = ddlFiltroUnidadNegocio;
	}
	public HtmlInputText getTxtParametroBusquedaCliente() {
		return txtParametroBusquedaCliente;
	}
	public void setTxtParametroBusquedaCliente(
			HtmlInputText txtParametroBusquedaCliente) {
		this.txtParametroBusquedaCliente = txtParametroBusquedaCliente;
	}
	public List<SelectItem> getLstTipoBusquedaCliente() {
		lstTipoBusquedaCliente = new ArrayList<SelectItem>();
		lstTipoBusquedaCliente.add(new SelectItem("02","Nombre"));
		lstTipoBusquedaCliente.add(new SelectItem("01","Código"));
		return lstTipoBusquedaCliente;
	}
	public void setLstTipoBusquedaCliente(List<SelectItem> lstTipoBusquedaCliente) {
		this.lstTipoBusquedaCliente = lstTipoBusquedaCliente;
	}
	public HtmlDropDownList getDdlTipoBusquedaCliente() {
		return ddlTipoBusquedaCliente;
	}
	public void setDdlTipoBusquedaCliente(HtmlDropDownList ddlTipoBusquedaCliente) {
		this.ddlTipoBusquedaCliente = ddlTipoBusquedaCliente;
	}
	public HtmlGridView getGvClientesDisponibles() {
		return gvClientesDisponibles;
	}
	public void setGvClientesDisponibles(HtmlGridView gvClientesDisponibles) {
		this.gvClientesDisponibles = gvClientesDisponibles;
	}
	@SuppressWarnings("unchecked")
	public List<Vf0101> getLstClientesDisponibles() {
		
		if (CodeUtil.getFromSessionMap("pmt_lstClientesDisponibles") != null)
			lstClientesDisponibles = (ArrayList<Vf0101>) CodeUtil.getFromSessionMap("pmt_lstClientesDisponibles");
		
		if(lstClientesDisponibles == null){
			lstClientesDisponibles = new ArrayList<Vf0101>();
		}
		return lstClientesDisponibles;
	}
	public void setLstClientesDisponibles(List<Vf0101> lstClientesDisponibles) {
		this.lstClientesDisponibles = lstClientesDisponibles;
	}
	public HtmlDropDownList getDdlFormaDePago() {
		return ddlFormaDePago;
	}
	public void setDdlFormaDePago(HtmlDropDownList ddlFormaDePago) {
		this.ddlFormaDePago = ddlFormaDePago;
	}
	public HtmlDropDownList getDdlMonedaPago() {
		return ddlMonedaPago;
	}
	public void setDdlMonedaPago(HtmlDropDownList ddlMonedaPago) {
		this.ddlMonedaPago = ddlMonedaPago;
	}
	public HtmlDropDownList getDdlAfiliado() {
		return ddlAfiliado;
	}
	public void setDdlAfiliado(HtmlDropDownList ddlAfiliado) {
		this.ddlAfiliado = ddlAfiliado;
	}
	public HtmlDropDownList getDdlMarcaTarjeta() {
		return ddlMarcaTarjeta;
	}
	public void setDdlMarcaTarjeta(HtmlDropDownList ddlMarcaTarjeta) {
		this.ddlMarcaTarjeta = ddlMarcaTarjeta;
	}
	public HtmlDropDownList getDdlCodigoBanco() {
		return ddlCodigoBanco;
	}
	public void setDdlCodigoBanco(HtmlDropDownList ddlCodigoBanco) {
		this.ddlCodigoBanco = ddlCodigoBanco;
	}
	
	@SuppressWarnings("unchecked")
	public List<SelectItem> getLstFormaDePago() {
		
		try {
			if(CodeUtil.getFromSessionMap("pmt_lstFormaDePago") != null){
				return lstFormaDePago = (ArrayList<SelectItem>)CodeUtil.getFromSessionMap("pmt_lstFormaDePago");
			}
			
			String codcomp = ddlFiltroCompania.getValue().toString().trim();
			int iCaid = Integer.parseInt( CodeUtil.getFromSessionMap(("sCajaId") ).toString() );
			
			List<Metpago> metodosPagos = com.casapellas.controles.tmp.MetodosPagoCtrl.obtenerMetodosPagoCaja(iCaid, codcomp); 
			
			lstFormaDePago = new ArrayList<SelectItem>();
			for (Metpago mp : metodosPagos) {
				lstFormaDePago.add(new SelectItem(mp.getId().getCodigo().trim(), mp.getId().getMpago().trim())) ;
			}
			
		} catch (Exception e) {
			lstFormaDePago = new ArrayList<SelectItem>();
			e.printStackTrace(); 
		}finally{
			CodeUtil.putInSessionMap("pmt_lstFormaDePago", lstFormaDePago); 
		}
		
		return lstFormaDePago;
	}
	public void setLstFormaDePago(List<SelectItem> lstFormaDePago) {
		this.lstFormaDePago = lstFormaDePago;
	}
	@SuppressWarnings("unchecked")
	public List<SelectItem> getLstMonedaPago() {
		
		try {
			
			if(CodeUtil.getFromSessionMap("pmt_lstMonedaPago") != null){
				return lstMonedaPago = (ArrayList<SelectItem>)CodeUtil.getFromSessionMap("pmt_lstMonedaPago");
			}
			
			String codcomp = ddlFiltroCompania.getValue().toString().trim();
			int iCaid = Integer.parseInt( CodeUtil.getFromSessionMap(("sCajaId") ).toString() );
			String formadepago = ddlFormaDePago.getValue().toString();
			
			String[] monedas = MonedaCtrl.obtenerMonedasxMetodosPago_Caja(iCaid, codcomp, formadepago);
			
			lstMonedaPago = new ArrayList<SelectItem>();
			for (String moneda : monedas) {
				lstMonedaPago.add(new SelectItem(moneda, moneda) );
			}
			
		} catch (Exception e) {
			lstMonedaPago = new ArrayList<SelectItem>();
			e.printStackTrace(); 
		}finally{
			CodeUtil.putInSessionMap("pmt_lstMonedaPago", lstMonedaPago); 
		}
		
		return lstMonedaPago;
	}
	public void setLstMonedaPago(List<SelectItem> lstMonedaPago) {
		this.lstMonedaPago = lstMonedaPago;
	}
	@SuppressWarnings("unchecked")
	public List<SelectItem> getLstAfiliado() {
		
		try {
			
			if(CodeUtil.getFromSessionMap("pmt_lstAfiliado") != null){
				return lstAfiliado = (ArrayList<SelectItem>)CodeUtil.getFromSessionMap("pmt_lstAfiliado");
			}
			
			String codcomp = ddlFiltroCompania.getValue().toString().trim();
			String moneda  = ddlMonedaPago.getValue().toString();
			int caid = Integer.parseInt( CodeUtil.getFromSessionMap(("sCajaId") ).toString() );
			
			lstAfiliado = new ArrayList<SelectItem>();
			
			Cafiliados[] afiliados =  new AfiliadoCtrl().obtenerAfiliadoxCaja_Compania(caid, codcomp, "", moneda);
			 for (Cafiliados ca : afiliados) {
				 lstAfiliado.add( new SelectItem(ca.getId().getCxcafi().trim(), ca.getId().getCxdcafi().trim() +  " "  +  ca.getId().getC6rp07().trim() ) ) ;
			 }
			
		} catch (Exception e) {
			lstAfiliado = new ArrayList<SelectItem>();
			e.printStackTrace(); 
		}finally{
			CodeUtil.putInSessionMap("pmt_lstAfiliado", lstAfiliado); 
		}
		return lstAfiliado;
	}
	public void setLstAfiliado(List<SelectItem> lstAfiliado) {
		this.lstAfiliado = lstAfiliado;
	}
	@SuppressWarnings("unchecked")
	public List<SelectItem> getLstMarcaTarjeta() {
		
		try {
			
			if(CodeUtil.getFromSessionMap("pmt_lstMarcaTarjeta") != null){
				return lstMarcaTarjeta = (ArrayList<SelectItem>)CodeUtil.getFromSessionMap("pmt_lstMarcaTarjeta");
			}
			
			lstMarcaTarjeta = AfiliadoCtrl.obtenerTiposMarcaTarjetas();
			
		} catch (Exception e) {
			lstMarcaTarjeta = new ArrayList<SelectItem>();
			e.printStackTrace(); 
		}finally{
			CodeUtil.putInSessionMap("pmt_lstMarcaTarjeta", lstMarcaTarjeta); 
		}
		
		return lstMarcaTarjeta;
	}
	public void setLstMarcaTarjeta(List<SelectItem> lstMarcaTarjeta) {
		this.lstMarcaTarjeta = lstMarcaTarjeta;
	}
	@SuppressWarnings("unchecked")
	public List<SelectItem> getLstCodigoBanco() {
		
		try {
			
			if(CodeUtil.getFromSessionMap("pmt_lstCodigoBanco") != null){
				return lstCodigoBanco = (ArrayList<SelectItem>)CodeUtil.getFromSessionMap("pmt_lstCodigoBanco");
			}
			
			lstCodigoBanco = new ArrayList<SelectItem>();
			
			F55ca022[] banco = new BancoCtrl().obtenerBancos(); 	
			
        	for (F55ca022 f22 : banco) {
        		lstCodigoBanco.add( new SelectItem( f22.getId().getConciliar()+"@"+ f22.getId().getBanco()+"@"+f22.getId().getCodb(), 
        					    " " + String.valueOf(f22.getId().getCodb()) + " " + f22.getId().getBanco()   ) ) ;
			}
			
		} catch (Exception e) {
			lstCodigoBanco = new ArrayList<SelectItem>();
			e.printStackTrace(); 
		}finally{
			CodeUtil.putInSessionMap("pmt_lstCodigoBanco", lstCodigoBanco); 
		}
		return lstCodigoBanco;
	}
	public void setLstCodigoBanco(List<SelectItem> lstCodigoBanco) {
		this.lstCodigoBanco = lstCodigoBanco;
	}
	public HtmlOutputText getLblAfiliado() {
		return lblAfiliado;
	}
	public void setLblAfiliado(HtmlOutputText lblAfiliado) {
		this.lblAfiliado = lblAfiliado;
	}
	public HtmlOutputText getLblMarcaTarjeta() {
		return lblMarcaTarjeta;
	}
	public void setLblMarcaTarjeta(HtmlOutputText lblMarcaTarjeta) {
		this.lblMarcaTarjeta = lblMarcaTarjeta;
	}
	public HtmlOutputText getLblCodigoBanco() {
		return lblCodigoBanco;
	}
	public void setLblCodigoBanco(HtmlOutputText lblCodigoBanco) {
		this.lblCodigoBanco = lblCodigoBanco;
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
	public HtmlInputText getTxtMontoIngresado() {
		return txtMontoIngresado;
	}
	public void setTxtMontoIngresado(HtmlInputText txtMontoIngresado) {
		this.txtMontoIngresado = txtMontoIngresado;
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

	public HtmlDialogWindow getDwMensajesValidacion() {
		return dwMensajesValidacion;
	}

	public void setDwMensajesValidacion(HtmlDialogWindow dwMensajesValidacion) {
		this.dwMensajesValidacion = dwMensajesValidacion;
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

	public HtmlGridView getGvFormasDePagoRecibidas() {
		return gvFormasDePagoRecibidas;
	}

	public void setGvFormasDePagoRecibidas(HtmlGridView gvFormasDePagoRecibidas) {
		this.gvFormasDePagoRecibidas = gvFormasDePagoRecibidas;
	}

	@SuppressWarnings("unchecked")
	public List<MetodosPago> getLstFormasDePagoRecibidas() {
		
		if (CodeUtil.getFromSessionMap("pmt_lstFormasDePagoRecibidas") != null)
			lstFormasDePagoRecibidas = (ArrayList<MetodosPago>) CodeUtil.getFromSessionMap("pmt_lstFormasDePagoRecibidas");
		
		if(lstFormasDePagoRecibidas == null){
			lstFormasDePagoRecibidas = new ArrayList<MetodosPago>();
		}
		
		return lstFormasDePagoRecibidas;
	}

	public void setLstFormasDePagoRecibidas(
			List<MetodosPago> lstFormasDePagoRecibidas) {
		this.lstFormasDePagoRecibidas = lstFormasDePagoRecibidas;
	}

	@SuppressWarnings("unchecked")
	public List<SelectItem> getLstMonedaAplicadaRecibo() {
		try {
			
			if(CodeUtil.getFromSessionMap("pmt_lstMonedaAplicadaRecibo") != null){
				return lstMonedaAplicadaRecibo = (ArrayList<SelectItem>)CodeUtil.getFromSessionMap("pmt_lstMonedaAplicadaRecibo");
			}
			
			lstMonedaAplicadaRecibo = new ArrayList<SelectItem>();
			lstMonedaAplicadaRecibo.add(new SelectItem("COR", "COR", "COR"));
			
//			lstMonedaAplicadaRecibo.add(new SelectItem(strMoneda, strMoneda, strMoneda));
			
			return lstMonedaAplicadaRecibo ;
			
			
			// *************** la moneda del contrato siempre es dolares, la moneda aplicada siempre debera ser dolares
			/*
			String codunineg = ddlFiltroUnidadNegocio.getValue().toString().trim();
			String lineaneg = CompaniaCtrl.leerLineaNegocio(codunineg);

			String monedaDeLinea = CompaniaCtrl.obtenerMonedaLineaN(lineaneg);

			if (!monedaDeLinea.isEmpty()) {
				lstMonedaAplicadaRecibo = new ArrayList<SelectItem>();
				lstMonedaAplicadaRecibo.add(new SelectItem(monedaDeLinea, monedaDeLinea, monedaDeLinea));
			} else {
			
				int caid = Integer.parseInt( CodeUtil.getFromSessionMap(("sCajaId") ).toString() );
				F55ca014[] companiaConfig = (F55ca014[])CodeUtil.getFromSessionMap("pmt_F55CA014");
				
				String[] monedasConfig = com.casapellas.controles.tmp.MonedaCtrl.obtenerMonedasxCaja_Companias( caid, companiaConfig);
				
				if(monedasConfig == null || monedasConfig.length == 0)
					return lstMonedaAplicadaRecibo = new ArrayList<SelectItem>();
				
				lstMonedaAplicadaRecibo = new ArrayList<SelectItem>();
				for (String moneda : monedasConfig) {
					lstMonedaAplicadaRecibo.add(new SelectItem(moneda, moneda, moneda));
				}
			}*/
			
		} catch (Exception e) {
			e.printStackTrace(); 
			lstMonedaAplicadaRecibo = new ArrayList<SelectItem>();
		}finally{
			CodeUtil.putInSessionMap("pmt_lstMonedaAplicadaRecibo", lstMonedaAplicadaRecibo);
		}
		return lstMonedaAplicadaRecibo;
	}
	public void setLstMonedaAplicadaRecibo(List<SelectItem> lstMonedaAplicadaRecibo) {
		this.lstMonedaAplicadaRecibo = lstMonedaAplicadaRecibo;
	}
	public HtmlDropDownList getDdlMonedaAplicadaRecibo() {
		return ddlMonedaAplicadaRecibo;
	}
	public void setDdlMonedaAplicadaRecibo(HtmlDropDownList ddlMonedaAplicadaRecibo) {
		this.ddlMonedaAplicadaRecibo = ddlMonedaAplicadaRecibo;
	}
//	public HtmlOutputText getLblDisplayTasaOficial() {
//		return lblDisplayTasaOficial;
//	}
//	public void setLblDisplayTasaOficial(HtmlOutputText lblDisplayTasaOficial) {
//		this.lblDisplayTasaOficial = lblDisplayTasaOficial;
//	}
	public HtmlOutputText getLblDisplayTasaParalela() {
		return lblDisplayTasaParalela;
	}
	public void setLblDisplayTasaParalela(HtmlOutputText lblDisplayTasaParalela) {
		this.lblDisplayTasaParalela = lblDisplayTasaParalela;
	}
	public String getStrDisplayTasaOficial() {
		try {
			BigDecimal tasaOficial  = tasaOficial();
			strDisplayTasaOficial = String.format("%1$,.4f", tasaOficial);
		} catch (Exception e) {
			 e.printStackTrace();
			 strDisplayTasaOficial  = "1.0000";
		}
		return strDisplayTasaOficial;
	}
	public void setStrDisplayTasaOficial(String strDisplayTasaOficial) {
		this.strDisplayTasaOficial = strDisplayTasaOficial;
	}
	public String getStrDisplayTasaParalela() {
		
		try {
			BigDecimal tasaOficial  = tasaParalela("COR");
			strDisplayTasaParalela = String.format("%1$,.4f", tasaOficial);
		} catch (Exception e) {
			 e.printStackTrace();
			 strDisplayTasaParalela  = "1.0000";
		}
		
		return strDisplayTasaParalela;
	}
	public void setStrDisplayTasaParalela(String strDisplayTasaParalela) {
		this.strDisplayTasaParalela = strDisplayTasaParalela;
	}
	public HtmlOutputText getLblMontoRecibido() {
		return lblMontoRecibido;
	}
	public void setLblMontoRecibido(HtmlOutputText lblMontoRecibido) {
		this.lblMontoRecibido = lblMontoRecibido;
	}
	public HtmlOutputText getLblMontoPendiente() {
		return lblMontoPendiente;
	}
	public void setLblMontoPendiente(HtmlOutputText lblMontoPendiente) {
		this.lblMontoPendiente = lblMontoPendiente;
	}
	public HtmlOutputText getLblMontoPendienteNac() {
		return lblMontoPendienteNac;
	}
	public void setLblMontoPendienteNac(HtmlOutputText lblMontoPendienteNac) {
		this.lblMontoPendienteNac = lblMontoPendienteNac;
	}
	public HtmlOutputText getLblMontoCambioNacional() {
		return lblMontoCambioNacional;
	}
	public void setLblMontoCambioNacional(HtmlOutputText lblMontoCambioNacional) {
		this.lblMontoCambioNacional = lblMontoCambioNacional;
	}
	public HtmlInputTextarea getTxtConceptoAnticipo() {
		return txtConceptoAnticipo;
	}
	public void setTxtConceptoAnticipo(HtmlInputTextarea txtConceptoAnticipo) {
		this.txtConceptoAnticipo = txtConceptoAnticipo;
	}
	public HtmlInputText getTxtMontoAplicadoRecibir() {
		return txtMontoAplicadoRecibir;
	}
	public void setTxtMontoAplicadoRecibir(HtmlInputText txtMontoAplicadoRecibir) {
		this.txtMontoAplicadoRecibir = txtMontoAplicadoRecibir;
	}
	public HtmlInputText getTxtMontoCambio() {
		return txtMontoCambio;
	}
	public void setTxtMontoCambio(HtmlInputText txtMontoCambio) {
		this.txtMontoCambio = txtMontoCambio;
	}

	public HtmlDialogWindow getDwConfirmacionProcesaRecibo() {
		return dwConfirmacionProcesaRecibo;
	}

	public void setDwConfirmacionProcesaRecibo(
			HtmlDialogWindow dwConfirmacionProcesaRecibo) {
		this.dwConfirmacionProcesaRecibo = dwConfirmacionProcesaRecibo;
	}

	public HtmlInputText getTxtNumeroContratoValidar() {
		return txtNumeroContratoValidar;
	}

	public void setTxtNumeroContratoValidar(HtmlInputText txtNumeroContratoValidar) {
		this.txtNumeroContratoValidar = txtNumeroContratoValidar;
	}

	public HtmlPanelGroup getTblResumenContrato() {
		return tblResumenContrato;
	}

	public void setTblResumenContrato(HtmlPanelGroup tblResumenContrato) {
		this.tblResumenContrato = tblResumenContrato;
	}
	public HtmlOutputText getLblRsmNumeroContrato() {
		return lblRsmNumeroContrato;
	}

	public void setLblRsmNumeroContrato(HtmlOutputText lblRsmNumeroContrato) {
		this.lblRsmNumeroContrato = lblRsmNumeroContrato;
	}

	public HtmlOutputText getLblRsmNumeroPropuesta() {
		return lblRsmNumeroPropuesta;
	}

	public void setLblRsmNumeroPropuesta(HtmlOutputText lblRsmNumeroPropuesta) {
		this.lblRsmNumeroPropuesta = lblRsmNumeroPropuesta;
	}

	public HtmlOutputText getLblRsmNumeroChasis() {
		return lblRsmNumeroChasis;
	}

	public void setLblRsmNumeroChasis(HtmlOutputText lblRsmNumeroChasis) {
		this.lblRsmNumeroChasis = lblRsmNumeroChasis;
	}

	public HtmlOutputText getLblRsmFechaContrato() {
		return lblRsmFechaContrato;
	}

	public void setLblRsmFechaContrato(HtmlOutputText lblRsmFechaContrato) {
		this.lblRsmFechaContrato = lblRsmFechaContrato;
	}

	public HtmlOutputText getLblRsmElaboradoPor() {
		return lblRsmElaboradoPor;
	}

	public void setLblRsmElaboradoPor(HtmlOutputText lblRsmElaboradoPor) {
		this.lblRsmElaboradoPor = lblRsmElaboradoPor;
	}
	
	public HtmlOutputText getLblRsmMontoMontoAplicadoAnticipo() {
		return lblRsmMontoMontoAplicadoAnticipo;
	}
	
	public void setLblRsmMontoMontoAplicadoAnticipo(
			HtmlOutputText lblRsmMontoMontoAplicadoAnticipo) {
		this.lblRsmMontoMontoAplicadoAnticipo = lblRsmMontoMontoAplicadoAnticipo;
	}
	public HtmlLink getLnkBuscarCuotasPendientes() {
		
		try {
			
			if( lnkBuscarCuotasPendientes == null)
				return lnkBuscarCuotasPendientes; 
			
			String tipoAnticipo = ddlTipoAnticiposIngreso.getValue().toString();
			
			String estiloActual = lnkBuscarCuotasPendientes.getStyle();
			
			if(tipoAnticipo.compareTo("002") == 0 ){
				lnkBuscarCuotasPendientes.setStyle( estiloActual.replace("display:none;", "") );
			}else{
				lnkBuscarCuotasPendientes.setStyle(estiloActual + "; display:none; " );
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		return lnkBuscarCuotasPendientes;
	}

	public void setLnkBuscarCuotasPendientes(HtmlLink lnkBuscarCuotasPendientes) {
		this.lnkBuscarCuotasPendientes = lnkBuscarCuotasPendientes;
	}

	public HtmlGridView getGvCuotasPendientesDisponibles() {
		return gvCuotasPendientesDisponibles;
	}

	public void setGvCuotasPendientesDisponibles(
			HtmlGridView gvCuotasPendientesDisponibles) {
		this.gvCuotasPendientesDisponibles = gvCuotasPendientesDisponibles;
	}

	public List<Vwbitacoracobrospmt> getLstCuotasPendientesDisponibles() {
		
		try {
			
			if(CodeUtil.getFromSessionMap("pmt_lstCuotasPendientesDisponibles") != null){
				return lstCuotasPendientesDisponibles = (List<Vwbitacoracobrospmt>)CodeUtil.getFromSessionMap("pmt_lstCuotasPendientesDisponibles");
			}
			
			lstCuotasPendientesDisponibles = new ArrayList<Vwbitacoracobrospmt>();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return lstCuotasPendientesDisponibles;
	}

	public void setLstCuotasPendientesDisponibles(
			List<Vwbitacoracobrospmt> lstCuotasPendientesDisponibles) {
		this.lstCuotasPendientesDisponibles = lstCuotasPendientesDisponibles;
	}

	public HtmlDialogWindow getDwCuotasPendientesDisponibles() {
		return dwCuotasPendientesDisponibles;
	}

	public void setDwCuotasPendientesDisponibles(
			HtmlDialogWindow dwCuotasPendientesDisponibles) {
		this.dwCuotasPendientesDisponibles = dwCuotasPendientesDisponibles;
	}

	public HtmlOutputText getLblRsmCuotaMonto() {
		return lblRsmCuotaMonto;
	}

	public void setLblRsmCuotaMonto(HtmlOutputText lblRsmCuotaMonto) {
		this.lblRsmCuotaMonto = lblRsmCuotaMonto;
	}

	public HtmlOutputText getLblRsmCuotaNumero() {
		return lblRsmCuotaNumero;
	}

	public void setLblRsmCuotaNumero(HtmlOutputText lblRsmCuotaNumero) {
		this.lblRsmCuotaNumero = lblRsmCuotaNumero;
	}

	public HtmlOutputText getLblRsmCuotaFechaPago() {
		return lblRsmCuotaFechaPago;
	}

	public void setLblRsmCuotaFechaPago(HtmlOutputText lblRsmCuotaFechaPago) {
		this.lblRsmCuotaFechaPago = lblRsmCuotaFechaPago;
	}

	public HtmlOutputText getLblRsmCuotaMoneda() {
		return lblRsmCuotaMoneda;
	}

	public void setLblRsmCuotaMoneda(HtmlOutputText lblRsmCuotaMoneda) {
		this.lblRsmCuotaMoneda = lblRsmCuotaMoneda;
	}

	public HtmlPanelGroup getTblResumenCuotaPendiente() {
		return tblResumenCuotaPendiente;
	}

	public void setTblResumenCuotaPendiente(HtmlPanelGroup tblResumenCuotaPendiente) {
		this.tblResumenCuotaPendiente = tblResumenCuotaPendiente;
	}
	public HtmlOutputText getLblRsmCuotaMontoCordobas() {
		return lblRsmCuotaMontoCordobas;
	}
	public void setLblRsmCuotaMontoCordobas(HtmlOutputText lblRsmCuotaMontoCordobas) {
		this.lblRsmCuotaMontoCordobas = lblRsmCuotaMontoCordobas;
	}

}

package com.casapellas.jde.creditos;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.casapellas.controles.ConsolidadoDepositosBcoCtrl;
import com.casapellas.entidades.MetodosPago;
import com.casapellas.util.FechasUtil;

public class ProcesarReciboRU {
	
	public static String msgProceso;
	public static List<String[]> lstSqlsInserts;

	public static CodigosJDE1 estadobatch;
	public static CodigosJDE1 tipodebatch;
	public static String numeroBatchJde;
	public static String numeroFacturaRu;
	public static String numeroReciboRu;
	public static int numeroReciboCaja;

	public static List<String[]> numeroFacturaRecibo;
	
	public static Date fecharecibo;
	public static BigDecimal montoRecibo;
	public static BigDecimal tasaCambioOficial;
	
	public static String monedaRecibo;
	public static String monedaLocal;
	
	public static String usuario;
	public static int codigousuario;
	public static String programa;
	
	private static String montoReciboJde;
	private static String fechajuliana;
	
	public static int codigoCliente;
	public static int codigoClientePadre;
	public static String tipoRecibo;
	
	//public static int numeroFactura;
	
	public static String sucursal;
	public static String unidadNegocio1;
	public static String unidadNegocio2;
	public static String concepto;
	public static String nombrecliente;
	public static String idCuentaContableFactura;
	public static String claseContableCliente;
	public static String numeroCuota;
	public static String categoria08;
	
	 
	public static List<MetodosPago> formasDePago;
	public static List<MetodosPago> formasdepagoProceso;
	public static List<String[]> cuentasFormasPago;
	
	private static BigDecimal montoDomestico;
	private static BigDecimal montoExtranjero;
	private static String cuentaContableRecibo;
	
	public static String numeroContrato;
	public static String tipoContrato;
	
	public ProcesarReciboRU(){
		numeroReciboCaja = 0 ;
		msgProceso = "" ;
		montoReciboJde = "0" ;
		fechajuliana = "" ;
		
		montoDomestico = BigDecimal.ZERO;
		montoExtranjero = BigDecimal.ZERO;
		
		fecharecibo = new Date();
	}
	
	public static void procesarRecibo(Session session){
		
		try {
			
			lstSqlsInserts = new ArrayList<String[]>();
			msgProceso = "" ;
			
			montoReciboJde = montoRecibo.setScale(2,RoundingMode.UP).toString().replace(".", "");
			
			fechajuliana = String.valueOf( FechasUtil.dateToJulian(fecharecibo) );
			

			int contadorFormaPago = 0;
			for (final MetodosPago formaPago : formasDePago) {
				
				numeroFacturaRu = numeroFacturaRecibo.get( contadorFormaPago )[0];
				numeroReciboRu = numeroFacturaRecibo.get(contadorFormaPago++ )[1];
				montoRecibo =  new BigDecimal( Double.toString( formaPago.getEquivalente() ) );
				
				formasdepagoProceso = new ArrayList<MetodosPago>();
				formasdepagoProceso.add(formaPago);
				
				cuentaContableRecibo = ( (String[])
					CollectionUtils.find(cuentasFormasPago, new Predicate(){
						@Override
						public boolean evaluate(Object o) {
							String[] dtaCuenta = (String[])o;
							return dtaCuenta[6].compareTo(formaPago.getMetodo()) == 0  &&  dtaCuenta[7].compareTo(formaPago.getMoneda() ) == 0;
						}
					}) ) [1];
				
				grabarDetalleRecibo();
				if( !msgProceso.isEmpty() ){
					return ;
				}
				
				grabarCabeceraRecibo();
				if( !msgProceso.isEmpty() ){
					return ;
				}
				
				grabarFacturaPorRecibo();
				if( !msgProceso.isEmpty() ){
					return ;
				}
				
			}
			
			
			grabarControlBatch();
			if( !msgProceso.isEmpty() ){
				return ;
			}
			 
			for (String[] querys : lstSqlsInserts) {
				 
				try {
					
					boolean execute = ConsolidadoDepositosBcoCtrl .executeSqlQueryTx(session, querys[0]);
					
					if(!execute){
						msgProceso = "error al procesar: " + querys[1];
						return;
					}
					
					
				} catch (Exception e) {
					
					msgProceso = "fallo en interfaz Edwards "+ querys[1] ;
					
					break;
				}
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
			msgProceso = "Error al procesar metodos para crear recibo en JdEdward's " ;
		}
		
	}
	
	//**********************************************************
	//----------------------------------------------------------
	//++++++++++++++++++      INICIO     +++++++++++++++++++++++
	//----------------------------------------------------------
	//**********************************************************
	//Creado por Luis Alberto Fonseca Mendez 2019-04-23
	
	public static void procesarRecibo(Session sesion, Transaction trans){
		
		try {
			
			lstSqlsInserts = new ArrayList<String[]>();
			msgProceso = "" ;
			
			montoReciboJde = montoRecibo.setScale(2,RoundingMode.UP).toString().replace(".", "");
			
			fechajuliana = String.valueOf( FechasUtil.dateToJulian(fecharecibo) );
			

			int contadorFormaPago = 0;
			for (final MetodosPago formaPago : formasDePago) {
				
				numeroFacturaRu = numeroFacturaRecibo.get( contadorFormaPago )[0];
				numeroReciboRu = numeroFacturaRecibo.get(contadorFormaPago++ )[1];
				montoRecibo =  new BigDecimal( Double.toString( formaPago.getEquivalente() ) );
				
				formasdepagoProceso = new ArrayList<MetodosPago>();
				formasdepagoProceso.add(formaPago);
				
				cuentaContableRecibo = ( (String[])
					CollectionUtils.find(cuentasFormasPago, new Predicate(){
						@Override
						public boolean evaluate(Object o) {
							String[] dtaCuenta = (String[])o;
							return dtaCuenta[6].compareTo(formaPago.getMetodo()) == 0  &&  dtaCuenta[7].compareTo(formaPago.getMoneda() ) == 0;
						}
					}) ) [1];
				
				grabarDetalleRecibo();
				if( !msgProceso.isEmpty() ){
					return ;
				}
				
				grabarCabeceraRecibo();
				if( !msgProceso.isEmpty() ){
					return ;
				}
				
				grabarFacturaPorRecibo();
				if( !msgProceso.isEmpty() ){
					return ;
				}
				
			}
			
			
			grabarControlBatch();
			if( !msgProceso.isEmpty() ){
				return ;
			}
			 
			for (String[] querys : lstSqlsInserts) {
				
				System.out.println("Query " + querys[0]);
				
				try {
					
//					boolean execute = ConsolidadoDepositosBcoCtrl .executeSqlQuery(sesion, trans, querys[0]);
//					
//					if(!execute){
//						msgProceso = "error al procesar: " + querys[1];
//						return;
//					}
					
					int rows = sesion.createSQLQuery( querys[0] ).executeUpdate() ;
					
					if( rows == 0 ){
//						execute = false;
						msgProceso = "error al procesar: " + querys[1];
						return;
					}
					
					
				} catch (Exception e) {
					
					msgProceso = "fallo en interfaz Edwards "+ querys[1] ;
//					LogCrtl.imprimirError(e);
					e.printStackTrace();
					return;
				}
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
			msgProceso = "Error al procesar metodos para crear recibo en JdEdward's " ;
		}
		
	}
	
	
	public static void procesarRecibo2(){
		
		try {
			
			lstSqlsInserts = new ArrayList<String[]>();
			msgProceso = "" ;
			
			montoReciboJde = montoRecibo.setScale(2,RoundingMode.UP).toString().replace(".", "");
			
			fechajuliana = String.valueOf( FechasUtil.dateToJulian(fecharecibo) );
			

			int contadorFormaPago = 0;
			for (final MetodosPago formaPago : formasDePago) {
				
				numeroFacturaRu = numeroFacturaRecibo.get( contadorFormaPago )[0];
				numeroReciboRu = numeroFacturaRecibo.get(contadorFormaPago++ )[1];
				montoRecibo =  new BigDecimal( Double.toString( formaPago.getEquivalente() ) );
				
				formasdepagoProceso = new ArrayList<MetodosPago>();
				formasdepagoProceso.add(formaPago);
				
				cuentaContableRecibo = ( (String[])
					CollectionUtils.find(cuentasFormasPago, new Predicate(){
						@Override
						public boolean evaluate(Object o) {
							String[] dtaCuenta = (String[])o;
							return dtaCuenta[6].compareTo(formaPago.getMetodo()) == 0  &&  dtaCuenta[7].compareTo(formaPago.getMoneda() ) == 0;
						}
					}) ) [1];
				
				grabarDetalleRecibo();
				if( !msgProceso.isEmpty() ){
					return ;
				}
				
				grabarCabeceraRecibo();
				if( !msgProceso.isEmpty() ){
					return ;
				}
				
				grabarFacturaPorRecibo();
				if( !msgProceso.isEmpty() ){
					return ;
				}
				
			}
			
			
			grabarControlBatch();
			if( !msgProceso.isEmpty() ){
				return ;
			}
			 
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
			msgProceso = "Error al procesar metodos para crear recibo en JdEdward's " ;
		}
		
	}
	
	//**********************************************************
	//----------------------------------------------------------
	//++++++++++++++++++       FIN       +++++++++++++++++++++++
	//----------------------------------------------------------
	//**********************************************************
	
	public static void grabarDetalleRecibo(){
		try {
			
			List<ReciboDetalleF03B14> detallesRecibos = new ArrayList<ReciboDetalleF03B14>() ;
			int rowCount = 0;
			
			String strIdCuentaFormaPago = "" ;
			
			boolean reciboMonedaExtranjera = tasaCambioOficial.compareTo(BigDecimal.ZERO) == 1 ;
			String modalidadRecibo =  (reciboMonedaExtranjera)?"F":"D" ;
			BigDecimal tasaDiferencialCambiario;
			
			
			List<MetodosPago> formasDePago = new ArrayList<MetodosPago>(formasdepagoProceso);
			
			for (MetodosPago formadepago : formasDePago) {
				formadepago.setMontoEquivalenteEntero(String.format("%1$.2f", formadepago.getEquivalente() ).replace(".", "") );
			}
			
			
			for (final MetodosPago formapago : formasDePago) {
				
				BigDecimal saldoFormaPago = new BigDecimal( formapago.getMontoEquivalenteEntero() ) ; 
				
				tasaDiferencialCambiario =  ( formapago.getTasa().compareTo(BigDecimal.ONE) == 0 ) ? tasaCambioOficial:  formapago.getTasa() ;
				
				strIdCuentaFormaPago = ( (String[])
				CollectionUtils.find(cuentasFormasPago, new Predicate(){
					@Override
					public boolean evaluate(Object o) {
						String[] dtaCuenta = (String[])o;
						return dtaCuenta[6].compareTo(formapago.getMetodo()) == 0  &&  dtaCuenta[7].compareTo(formapago.getMoneda() ) == 0;
					}
				}) ) [1];
				
				ReciboDetalleF03B14 rc = new ReciboDetalleF03B14(
						 numeroReciboRu,
						 String.valueOf( rowCount++ ),
						 String.valueOf(numeroReciboCaja), //"", // rzcknu  
						 String.valueOf(numeroFacturaRu), //String.valueOf(numeroFactura),
						 tipoRecibo,
						 sucursal,
						 numeroCuota,
						 String.valueOf(codigoCliente),
						 
						 idCuentaContableFactura,  //strIdCuentaFormaPago,
						
						 sucursal,
						 String.valueOf(numeroBatchJde),
						 CodigosJDE1.RECIBOPRIMAS.codigo(),
						 String.valueOf(codigoClientePadre),
						 saldoFormaPago.negate().toString(),
						 monedaLocal,
						 modalidadRecibo,
						 monedaRecibo,
						 tasaCambioOficial.toString(),
						 saldoFormaPago.negate().toString(),
						 monedaRecibo,
						 saldoFormaPago.negate().toString(),
						 unidadNegocio1,
						 concepto,
						 tasaCambioOficial.toString(),
						 usuario,
						 "0", // monto del diferencial cambiario
						 "", // cuenta del diferencial cambiario
						 "0",
						 "0",
						 "0",
						 "0",
						 fecharecibo,
						 claseContableCliente
					);	
				
				rc.unappliedCash = true;
				
				if( reciboMonedaExtranjera  ){
					
					rc.setRzpfap( saldoFormaPago.negate().toString() );
					rc.setRzpaap( String.valueOf( saldoFormaPago.multiply( tasaDiferencialCambiario ).negate().longValue() ) ) ;
//					rc.setRzpaap( String.valueOf( saldoFormaPago.multiply( tasaDiferencialCambiario ).negate().intValue() ) ) ;
					
					BigDecimal montoNacionalTasaFechaFactura = saldoFormaPago.multiply( tasaCambioOficial ) ;
					BigDecimal montoNacionalTasaFechaActual =  saldoFormaPago.multiply( tasaDiferencialCambiario );
					
					BigDecimal diferencialCambiario = montoNacionalTasaFechaActual.subtract(montoNacionalTasaFechaFactura);
					
					if(diferencialCambiario.compareTo(BigDecimal.ZERO) != 0){
						rc.setRzagl( String.valueOf(diferencialCambiario.intValue()));
						rc.setRzaidt(strIdCuentaFormaPago);
					}
					
				}else{
					rc.setRzpaap(saldoFormaPago.negate().toString());
					rc.setRzpfap( "0" ) ;
				}
				
				
				detallesRecibos.add(rc);
				
				lstSqlsInserts.add(new String[]{ rc.insertStatement(), "Grabar detalle de recibo F03B14"});
				
				
			}
			
			//&& ================= sumar los montos aplicados en el detalle de los recibos, para que cuadren con la cabecera del recibo
			
			BigDecimal ryckam = BigDecimal.ZERO;
			BigDecimal ryfcam = BigDecimal.ZERO;
			for (ReciboDetalleF03B14 rc : detallesRecibos) {
				ryckam = ryckam.add(new BigDecimal(rc.getRzpaap()).negate() );
				ryfcam = ryfcam.add(new BigDecimal(rc.getRzpfap()).negate() );
			}
			
			montoDomestico =  ryckam;
			montoExtranjero = ryfcam;	
			
		} catch (Exception e) {
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
			msgProceso = "Error al procesar detalle de recibo jde F03B14 ";
			e.printStackTrace();
		}
	}
	
	
	public static void grabarCabeceraRecibo(){
		try {
			
			String montoLocal =  montoDomestico.toString();
			String montoExtra =  montoExtranjero.toString();
			
			boolean reciboMonedaExtranjera = tasaCambioOficial.compareTo(BigDecimal.ZERO) == 1 ;
			
			ReciboF03B13 f03b13 = new ReciboF03B13(
					 String.valueOf( numeroReciboRu ), 
					 String.valueOf(numeroReciboCaja), //"", // rycknu  
					 String.valueOf(codigoCliente), 
					 String.valueOf(codigoCliente), 
					 sucursal, 
					 String.valueOf(numeroBatchJde),
					 String.valueOf(codigoClientePadre), 
					 montoLocal, 
					 monedaLocal, 
					 (reciboMonedaExtranjera)?"F":"D", 
					 monedaRecibo, 
					 tasaCambioOficial.toString(), 
					 montoExtra, 
					 cuentaContableRecibo, 
					 concepto, 
					 nombrecliente, 
					 usuario, 
					 CodigosJDE1.RECIBOPRIMAS.codigo(),
					 fecharecibo);
			
			if(claseContableCliente == null){
				claseContableCliente = "UC";
			}
			
			f03b13.setRyglc( claseContableCliente );
			f03b13.setRyaid(idCuentaContableFactura);
			 
			 lstSqlsInserts.add(new String[]{ f03b13.insertStatement(), "Grabar Cabecera de recibo F03B13"}); 
			 
			
		} catch (Exception e) {
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
			msgProceso = "Error al procesar encabezado de recibo F03B13 ";
		}
	}
	
	
	public static void grabarFacturaPorRecibo(){
		
		try {
			
			String tipoSolicitud = "" ;
			String numeroSolicitud = "" ;
			String numeroCuota = "001";
			String tipoImpuesto = "" ;
			String codigoTipoImpuesto = "" ;
			
			
			NuevaFacturaF0311 f03b11 = new NuevaFacturaF0311(
					String.valueOf(codigoCliente), tipoRecibo, String.valueOf(numeroFacturaRu), numeroCuota, sucursal, 
					tipodebatch.codigo(), String.valueOf(numeroBatchJde), monedaRecibo, tasaCambioOficial.toString(), 
					tipoImpuesto, codigoTipoImpuesto, unidadNegocio1, fechajuliana, numeroSolicitud,
					tipoSolicitud, concepto, nombrecliente, usuario, unidadNegocio2 ,
					fecharecibo, monedaLocal, idCuentaContableFactura, claseContableCliente
				);
			
			boolean monedaExtranjera = tasaCambioOficial.compareTo(BigDecimal.ZERO) == 1; 
			
			f03b11.setRpag( String.format("%1$.2f", montoRecibo.negate()  ).replace(".", "")  );
			
			if(monedaExtranjera){
				BigDecimal montodom = montoRecibo.multiply(tasaCambioOficial).setScale(2, RoundingMode.HALF_UP).negate();
				f03b11.setRpacr( f03b11.getRpag() );
				f03b11.setRpag( String.format("%1$.2f", montodom).replace(".", "") );
			}
			
			f03b11.setRpar08(categoria08);
			f03b11.setRppa8(String.valueOf( codigoClientePadre ) );
			f03b11.setRppost("D");
			f03b11.setRpistc("1");
			f03b11.setRppyid(numeroReciboRu);
			
			if( numeroContrato == null || numeroContrato.isEmpty() ){
				numeroContrato = "0";
			}
			if( tipoContrato == null ){
				tipoContrato = "";
			}
			
			f03b11.setRppo(numeroContrato);
			f03b11.setRpdcto(tipoContrato);
			
			
			//&& asignacion del monto de la factura a partir de la sumatoria del F03b14
			f03b11.setRpag( montoDomestico.negate().toString() );
			f03b11.setRpaap(montoDomestico.negate().toString() );
			f03b11.setRpacr(montoExtranjero.negate().toString() );
			f03b11.setRpfap(montoExtranjero.negate().toString() );
			
			
			lstSqlsInserts.add( new String[]{f03b11.insertStatement() ,"Crear Factura F03B11 tipo " + tipoRecibo+ " para cliente " + codigoCliente } ) ;
			
			
		} catch (Exception e) {
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
			msgProceso = "Error al crear factura por recibo " ;
		}
		
	}
	
	public static void grabarControlBatch(){
		try {
			
			
			BatchControlF0011 f0011 = new BatchControlF0011(
				tipodebatch.codigo(),
				numeroBatchJde,
				"0",
				usuario,
				montoReciboJde,
				String.valueOf( formasDePago.size()  ),
				"0",
				fecharecibo,
				"RPRIMASR",
				getEstadobatch().codigo()
			); 
			
			lstSqlsInserts.add(new String[]{ f0011.insertStatement(), "Grabar registro de control de batchs F0011"});
			
		} catch (Exception e) {
			e.printStackTrace();
			msgProceso = "Error al generar los datos para el control de Batch F0011" ;
		}
	}

	public static CodigosJDE1 getEstadobatch() {
		
		if (estadobatch == null )
			estadobatch = CodigosJDE1.BATCH_ESTADO_PENDIENTE ;
		return estadobatch;
	}
	
	
}

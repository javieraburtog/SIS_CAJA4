package com.casapellas.jde.creditos;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.hibernate.Session;

import com.casapellas.controles.ConsolidadoDepositosBcoCtrl;
import com.casapellas.entidades.MetodosPago;
import com.casapellas.util.CodeUtil;

public class ProcesarReciboRG {

	public static String msgProceso;
	public static List<String[]> lstSqlsInserts;

	public static CodigosJDE1 estadobatch;
	public static CodigosJDE1 tipodebatch;
	public static String numeroBatchJde;
	public static List<String> numerosRecibo;
	public static int numeroReciboCaja;

	public static Date fechaRecibo;
	public static String monedaRecibo;
	public static String monedaLocal;
	public static BigDecimal montoRecibo;
	public static BigDecimal tasaCambioOficial;
	public static BigDecimal tasaCambioRecibo;

	public static int codigoCliente;
	public static String sucursal;
	public static String unidadNegocioCuentaContable ;
	public static String concepto;
	public static String nombrecliente;
	public static String idCuentaContable;
	public static List<MetodosPago> formasDePago;
	public static List<String[]> cuentasFormasPago;
	
	public static String usuario;
	public static int codigousuario;
	public static String programa;
	
	private static String numeroReciboJde;
	private static String montoReciboJde;
	private static BigDecimal montoDomestico;
	private static BigDecimal montoExtranjero;
	private static List<MetodosPago> formasdepagoProceso;
	private static String idCuentaContableFormaPago;
	
	public static String claseContableCliente;
	
	public ProcesarReciboRG() {
		msgProceso = "";
		lstSqlsInserts = null;

		tipodebatch = null;
		numeroBatchJde = "";
		numerosRecibo = null;

		monedaRecibo = "";
		monedaLocal = "";
		montoRecibo = BigDecimal.ZERO;
		tasaCambioOficial = BigDecimal.ZERO;

		codigoCliente = 0;
		sucursal = "";
		unidadNegocioCuentaContable  = "";
		concepto = "";
		nombrecliente = "";
		idCuentaContable = "";
		formasDePago = null;
		cuentasFormasPago = null;

		montoDomestico = BigDecimal.ZERO;
		montoExtranjero = BigDecimal.ZERO;
		
		numeroReciboCaja = 0 ;
		
		claseContableCliente = "" ;
	}
	
	public static void procesarRecibo(Session session){
		
		try {
			
			lstSqlsInserts = new ArrayList<String[]>();
			msgProceso = "" ;
			
			boolean reciboMonedaExtranjera = tasaCambioOficial.compareTo(BigDecimal.ZERO) == 1 ;
			
			montoReciboJde = montoRecibo.setScale(2,RoundingMode.UP).toString().replace(".", "");
			
			//&& ============== Generar documentos por cada forma de pago
			int contadorFormaPago = 0;
			for (final MetodosPago formaPago : formasDePago) {
				
				numeroReciboJde = numerosRecibo.get(contadorFormaPago++ );
				montoRecibo =  new BigDecimal( Double.toString( formaPago.getEquivalente() ) );
				formasdepagoProceso = new ArrayList<MetodosPago>();
				formasdepagoProceso.add(formaPago);
				
				idCuentaContableFormaPago = ( (String[])
					CollectionUtils.find(cuentasFormasPago, new Predicate(){
						@Override
						public boolean evaluate(Object o) {
							String[] dtaCuenta = (String[])o;
							return dtaCuenta[6].compareTo(formaPago.getMetodo()) == 0  &&  dtaCuenta[7].compareTo(formaPago.getMoneda() ) == 0;
						}
					}) ) [1];
				
				
				tasaCambioRecibo = BigDecimal.ONE;
				if(reciboMonedaExtranjera){
					tasaCambioRecibo = ( formaPago.getTasa().compareTo(BigDecimal.ONE) == 0 ) ? tasaCambioOficial : formaPago.getTasa() ;
				}
 
				grabarDetalleRecibo();
				if( !msgProceso.isEmpty() ){
					return ;
				}
				
				grabarCabeceraRecibo();
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
				fechaRecibo,
				"RINGEXT",
				getEstadobatch().codigo()
			); 
			
			lstSqlsInserts.add(new String[]{ f0011.insertStatement(), "Grabar registro de control de batchs F0011"});
			
		} catch (Exception e) {
			e.printStackTrace();
			msgProceso = "Error al generar los datos para el control de Batch F0011" ;
		}
	}
	
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
				
//				tasaDiferencialCambiario =  ( formapago.getTasa().compareTo(BigDecimal.ONE) == 0 ) ? tasaCambioOficial:  formapago.getTasa() ;
				
				strIdCuentaFormaPago = ( (String[])
				CollectionUtils.find(cuentasFormasPago, new Predicate(){
					@Override
					public boolean evaluate(Object o) {
						String[] dtaCuenta = (String[])o;
						return dtaCuenta[6].compareTo(formapago.getMetodo()) == 0  &&  dtaCuenta[7].compareTo(formapago.getMoneda() ) == 0;
					}
				}) ) [1];
				
				ReciboDetalleF03B14 rc = new ReciboDetalleF03B14(
						numeroReciboJde,
						 String.valueOf(++rowCount),
						 String.valueOf(numeroReciboCaja), //"", // rzcknu  
						 "0", //String.valueOf(numeroFactura),
						 "",
						 sucursal,
						 "",
						 "0", //String.valueOf(codigoCliente),
						 "", //idCuentaContableFactura,  //strIdCuentaFormaPago,
						 sucursal,
						 String.valueOf(numeroBatchJde),
						 CodigosJDE1.RECIBOINGRESOEX.codigo(),
						 "0", //String.valueOf(codigoCliente),
						 saldoFormaPago.negate().toString(),
						 monedaLocal,
						 modalidadRecibo,
						 monedaRecibo,
						 tasaCambioRecibo.toString(),// tasaCambioOficial.toString(),
						 saldoFormaPago.negate().toString(),
						 monedaRecibo,
						 saldoFormaPago.negate().toString(),
						 unidadNegocioCuentaContable ,
						 concepto,
						 tasaCambioRecibo.toString(),
						 usuario,
						 "0", // monto del diferencial cambiario
						 "", // cuenta del diferencial cambiario
						 "0",
						 "0",
						 "0",
						 "0",
						 fechaRecibo,
						 claseContableCliente
					);	
				
				rc.unappliedCash = false;
				
				if( reciboMonedaExtranjera  ){
					
					rc.setRzpfap( saldoFormaPago.negate().toString() );
					rc.setRzpaap( String.valueOf( saldoFormaPago.multiply( tasaCambioRecibo ).negate().longValue() ) ) ;
					
					/*
//					rc.setRzpaap( String.valueOf( saldoFormaPago.multiply( tasaDiferencialCambiario ).negate().intValue() ) ) ;

					BigDecimal montoNacionalTasaFechaFactura = saldoFormaPago.multiply( tasaCambioOficial ) ;
					BigDecimal montoNacionalTasaFechaActual =  saldoFormaPago.multiply( tasaDiferencialCambiario );
					
					BigDecimal diferencialCambiario = montoNacionalTasaFechaActual.subtract(montoNacionalTasaFechaFactura);
					
					if(diferencialCambiario.compareTo(BigDecimal.ZERO) != 0){
						rc.setRzagl( String.valueOf(diferencialCambiario.intValue()));
						rc.setRzaidt(strIdCuentaFormaPago);
					}
					*/
					
					
				}else{
					rc.setRzpaap(saldoFormaPago.negate().toString());
					rc.setRzpfap( "0" ) ;
				}
				
				rc.setRzutic("");
				rc.setRztyin("G");
				rc.setRzaid2(idCuentaContable);
				rc.setRzam2("2");
				rc.setRzmcu( unidadNegocioCuentaContable );
				rc.setRzsbl( CodeUtil.pad( String.valueOf(codigoCliente) , 8 , "0") );
				rc.setRzsblt( "A" );
				
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
		}
	}
	
	public static void grabarCabeceraRecibo(){
		try {
			
			String montoLocal =  montoDomestico.toString();
			String montoExtra =  montoExtranjero.toString();
			
			boolean reciboMonedaExtranjera = tasaCambioOficial.compareTo(BigDecimal.ZERO) == 1 ;
			
			ReciboF03B13 f03b13 = new ReciboF03B13(
					 String.valueOf( numeroReciboJde ), 
					 "", 
					  String.valueOf(codigoCliente), 
					  String.valueOf(codigoCliente), 
					 sucursal, 
					 String.valueOf(numeroBatchJde),
					 "0", // String.valueOf(codigoClientePadre), 
					 montoLocal, 
					 monedaLocal, 
					 (reciboMonedaExtranjera)?"F":"D", 
					 monedaRecibo, 
					 tasaCambioRecibo.toString(), // tasaCambioOficial.toString(), 
					 montoExtra, 
					 idCuentaContableFormaPago, 
					 concepto, 
					 "",//nombrecliente, 
					 usuario, 
					 CodigosJDE1.RECIBOPRIMAS.codigo(),
					 fechaRecibo);

			
			f03b13.setRytyin("G");
			f03b13.setRyglc( "" );
			f03b13.setRyaid( "");
			f03b13.setRyryin( formasdepagoProceso.get(0).getMetodo() ); 
			
			 lstSqlsInserts.add(new String[]{ f03b13.insertStatement(), "Grabar Cabecera de recibo F03B13"}); 
			 
			
		} catch (Exception e) {
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
			msgProceso = "Error al procesar encabezado de recibo F03B13 ";
		}
	}
	

	public static CodigosJDE1 getEstadobatch() {
		
		if (estadobatch  == null )
			estadobatch = CodigosJDE1.BATCH_ESTADO_PENDIENTE ;
		
		return estadobatch;
	}
}
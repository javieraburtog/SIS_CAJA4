package com.casapellas.jde.creditos;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import com.casapellas.controles.ConsolidadoDepositosBcoCtrl;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.PropertiesSystem;

public class ProcesarNuevaFacturaF03B11 {

	public static String nombrecliente;
	public static String tipoSolicitud;       
	public static String numeroSolicitud;
	public static String numeroCuota;
	public static String tipoInteres;
	public static String tipoCuentaIntereses;
	public static String moneda;
	public static String sucursal;
	public static String unidadNegocio1;
	public static String unidadNegocio2;
	public static String usuario;
	public static String tipoBatch;
	public static String tipoImpuesto;
	public static String concepto;
	public static String monedaLocal;
	public static String fechavencimiento;
	public static String claseContableCliente;
	public static String idCuentaContableFactura;
	public static String codigoCategoria08;
	public static String  numeroDocumentoOriginal;
	
	public static BigDecimal montoFactura;
	public static BigDecimal tasaCambio;
	public static BigDecimal montoInteres;
	public static BigDecimal montoImpuesto;
	 
	public static int codigoCliente;
	public static int codigoClientePadre;
	public static int numerobatch;
	public static int numeroFactura;
	public static int codigousuario;
	
	public static boolean saldoFavor;
	
	public static List<DatosComprobanteF0911> lineasComprobante;
	 
	public static Date fecha;
	public static Date fechaFactura;
	 
	private static List<String[]> sqlInsertString = new ArrayList<String[]>();
	public static String strMensajeProceso = "";
	
	public ProcesarNuevaFacturaF03B11() {
		
		nombrecliente = "";
		tipoSolicitud = "";
		numeroSolicitud = "";
		numeroCuota = "";
		tipoInteres = "";
		tipoCuentaIntereses = "";
		moneda = "";
		sucursal = "";
		unidadNegocio1 = "";
		unidadNegocio2 = "";
		usuario = "";
		tipoBatch = "";
		tipoImpuesto = "";
		concepto = "";
		monedaLocal = "";
		fechavencimiento = "";

		montoFactura = BigDecimal.ZERO;
		tasaCambio = BigDecimal.ZERO;
		montoInteres = BigDecimal.ZERO;
		montoImpuesto = BigDecimal.ZERO;

		codigoCliente = 0;
		codigoClientePadre = 0;
		numerobatch = 0;
		numeroFactura = 0;
		fecha = null;
		
		sqlInsertString = new ArrayList<String[]>();
		strMensajeProceso = "";
		idCuentaContableFactura = "" ;
		codigoCategoria08 = "" ;

		saldoFavor = false;
	}
	
	public static void procesarNuevaFactura(Session session){
		
		try {
			
			grabarParteContable(session);
			if( !strMensajeProceso.isEmpty() ){
				return;
			}
			grabarFacturaF03B11();
			if( !strMensajeProceso.isEmpty() ){
				return;
			}
			
			for (String[] querys : sqlInsertString) {
				 
				try {
					
					boolean execute = ConsolidadoDepositosBcoCtrl .executeSqlQueryTx(session, querys[0]);
					
					if( !execute ){ 
						strMensajeProceso = "Error al grabar " + querys[1];
						return;
					}
					
				} catch (Exception e) {
					
					strMensajeProceso = "fallo en interfaz Edwards "+ querys[1] ;
					return;
					 
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public static void grabarParteContable(Session session){
		
		try {
			
			new ProcesarEntradaDeDiario();
			
			ProcesarEntradaDeDiario.monedaComprobante = moneda ;
			ProcesarEntradaDeDiario.monedaLocal = monedaLocal;
			ProcesarEntradaDeDiario.fecharecibo =  fecha;
			ProcesarEntradaDeDiario.tasaCambioParalela = tasaCambio; 
			ProcesarEntradaDeDiario.tasaCambioOficial = tasaCambio; 
			ProcesarEntradaDeDiario.montoComprobante = montoInteres;
			ProcesarEntradaDeDiario.tipoDocumento = tipoInteres;
			ProcesarEntradaDeDiario.conceptoComprobante = nombrecliente ;
			ProcesarEntradaDeDiario.numeroBatchJde = String.valueOf( numerobatch );
			ProcesarEntradaDeDiario.numeroReciboJde = String.valueOf( numeroFactura ) ;
			ProcesarEntradaDeDiario.usuario = usuario;
			ProcesarEntradaDeDiario.codigousuario = codigousuario;
			ProcesarEntradaDeDiario.programaActualiza = PropertiesSystem.CONTEXT_NAME;
			ProcesarEntradaDeDiario.lineasComprobante = lineasComprobante;
			
			ProcesarEntradaDeDiario.tipodebatch = CodigosJDE1.BATCH_FINANCIMIENTO;
			ProcesarEntradaDeDiario.tiposolicitud = tipoSolicitud;
			ProcesarEntradaDeDiario.numerosolicitud = numeroSolicitud;
			ProcesarEntradaDeDiario.numerocuota = String.valueOf(numeroCuota);
			ProcesarEntradaDeDiario.tipointeres = tipoCuentaIntereses;
			ProcesarEntradaDeDiario.codigocliente = String.valueOf( codigoCliente );
			ProcesarEntradaDeDiario.procesarSql = false;
			
			ProcesarEntradaDeDiario.fechafactura = fechaFactura ; 
			
			ProcesarEntradaDeDiario.procesarEntradaDeDiario(session);
			
			if( !ProcesarEntradaDeDiario.msgProceso.isEmpty() ){
				strMensajeProceso = ProcesarEntradaDeDiario.msgProceso;
				return;
			}
			
			sqlInsertString.addAll(ProcesarEntradaDeDiario.lstSqlsInserts);
			
			
		} catch (Exception e) {
			strMensajeProceso = " error al grabar f0911 ";
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
		}
		
	}
	
	public static void grabarFacturaF03B11(){
		
		try {
			
			boolean monedaExtranjera = tasaCambio.compareTo(BigDecimal.ZERO) == 1; 
			
			numeroSolicitud = CodeUtil.pad( numeroSolicitud, 8 , "0" );
			numeroCuota = CodeUtil.pad( String.valueOf( numeroCuota )  , 3 , "0" );  
			
			String codigoTipoImpuesto = (tipoImpuesto.compareTo("EXE") == 0 ) ? "E": (tipoImpuesto.compareTo("IMP") == 0 ) ? "I" : ""  ;
			
			if(tipoImpuesto.compareTo("EXE") == 0 ){
				montoImpuesto = BigDecimal.ZERO;
			}
			
			NuevaFacturaF0311 f03b11 = new NuevaFacturaF0311(
				String.valueOf(codigoCliente), tipoInteres, String.valueOf(numeroFactura), numeroCuota, sucursal, 
				tipoBatch, String.valueOf(numerobatch), moneda, tasaCambio.toString(), 
				tipoImpuesto, codigoTipoImpuesto, unidadNegocio1, fechavencimiento, numeroSolicitud,
				tipoSolicitud, concepto, nombrecliente, usuario, unidadNegocio2 ,
				fecha, monedaLocal, idCuentaContableFactura, claseContableCliente
			);
				
			
			if(saldoFavor){
				montoFactura = montoFactura.negate();
				montoImpuesto = montoImpuesto.negate();
				montoInteres = montoInteres.negate();
			}
			
			f03b11.setRpag( String.format("%1$.2f", montoFactura).replace(".", "")  );
			f03b11.setRpstam( String.format("%1$.2f", montoImpuesto).replace(".", "")   );
			f03b11.setRpatxa( String.format("%1$.2f", montoInteres).replace(".", "") );
			f03b11.setRpatxn( String.format("%1$.2f", montoInteres).replace(".", "") );
			
			if(numeroDocumentoOriginal != null && !numeroDocumentoOriginal.isEmpty()){
				f03b11.setRpodoc(numeroDocumentoOriginal);
			}
			
			
			if(monedaExtranjera){
				BigDecimal montodom;
				
				montodom = montoFactura.multiply(tasaCambio).setScale(2, RoundingMode.HALF_UP);
				f03b11.setRpacr( f03b11.getRpag() );
				f03b11.setRpag( String.format("%1$.2f", montodom).replace(".", "") );
				
				montodom = montoImpuesto.multiply(tasaCambio).setScale(2, RoundingMode.HALF_UP);
				f03b11.setRpctam( f03b11.getRpstam() );
				f03b11.setRpstam( String.format("%1$.2f", montodom).replace(".", "") );
				
				montodom = montoInteres.multiply(tasaCambio).setScale(2, RoundingMode.HALF_UP);
				f03b11.setRpctxa( f03b11.getRpatxa() );
				f03b11.setRpatxa( String.format("%1$.2f", montodom).replace(".", "") );
				
				f03b11.setRpctxn( f03b11.getRpatxn() );
				f03b11.setRpatxn(  String.format("%1$.2f", montodom).replace(".", "") );
				
			}	
			
			 
			f03b11.setRpar08( claseContableCliente ) ;
			f03b11.setRppa8( String.valueOf(codigoClientePadre)) ;
			
			sqlInsertString.add( new String[]{f03b11.insertStatement() ,"Crear Interes tipo " + tipoInteres+ " para cuota " + numeroCuota } ) ;
			
		} catch (Exception e) {
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
			strMensajeProceso = "Error al procesa registro de Interes tipo " + tipoInteres+ " para cuota " + numeroCuota; 
		}
		
	}
	
	
}

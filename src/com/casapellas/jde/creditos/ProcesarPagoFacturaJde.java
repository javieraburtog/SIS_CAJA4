package com.casapellas.jde.creditos;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.hibernate.Session;

import com.casapellas.controles.ConsolidadoDepositosBcoCtrl;
import com.casapellas.entidades.Credhdr;
import com.casapellas.entidades.MetodosPago;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.Divisas;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.PropertiesSystem;

public class ProcesarPagoFacturaJde {

	public static String monedaDomestica;
	public static String codcomp;
	public static String tiporec;
	public static int caid ;
	public static int numrec;
	public static Date fecharecibo;
	public static BigDecimal tasaCambioRecibo;
	public static BigDecimal tasaCambioOficial;
	
	public static String numeroBatchJde;
	public static String numeroReciboJde;
	public static List<String>numerosReciboJde;
	
	public static String msgProceso;
	public static String usuario;
	public static int codigousuario;
	public static String programaActualiza;
	public static String moduloSistema;
	
	public static List<Credhdr> facturas ;
	public static List<MetodosPago> formasdepago;
	public static List<MetodosPago> formasdepagoProceso;
	
	private static List<FacturaF03B11> facturasJde;
	private static String fecharecibojde;
	private static String horarecibojde;
	
	public static String montoReciboLocal;
	public static String montoReciboExtranjero;
	
	public static boolean ajustarMontoAplicado = true;
	
	public static List<String[]> lstSqlsInserts;
	
	private static BigDecimal montoEnteroTotalRecibo = BigDecimal.ZERO;
	
	public static boolean executeQueries = true;
	
	public static CodigosJDE1 estadobatch;
	
	private static List<ReciboF03B13> rcsF03B13 = new ArrayList<ReciboF03B13>() ;
	private static List<ReciboDetalleF03B14> rcsF03B14 = new ArrayList<ReciboDetalleF03B14>() ;
	
	public ProcesarPagoFacturaJde() {
		caid = 0;
		codcomp = "";
		tiporec = "";
		numrec = 0;
		fecharecibo = null;

		facturas = null;
		formasdepago = null;
		tasaCambioRecibo = null;
		tasaCambioOficial = null;

		numeroBatchJde = "";
		numeroReciboJde = "";

		codigousuario = 0;
		usuario = "";
		programaActualiza = PropertiesSystem.CONTEXT_NAME;
		msgProceso = "";
		ajustarMontoAplicado = true;

		montoEnteroTotalRecibo = BigDecimal.ZERO;
		
		executeQueries = true;
		estadobatch = null;
		
		rcsF03B13 = new ArrayList<ReciboF03B13>() ;
		rcsF03B14 = new ArrayList<ReciboDetalleF03B14>() ;
		
	}
	
	public static void procesarPagoFacturaMultipleRecibo(){
		try {
			
			msgProceso = "" ;
			
			fecharecibojde = String.valueOf( FechasUtil.dateToJulian(fecharecibo) ) ;
			horarecibojde = new SimpleDateFormat("HHmmss").format(fecharecibo);
			
			lstSqlsInserts = new ArrayList<String[]>();
			
			crearF03b11FromCredHdr();
			
			if( !msgProceso.isEmpty() ){
				return;
			}
			
			crearHistoricoFacturas();
			
			if( !msgProceso.isEmpty() ){
				return;
			}
			
			grabarControlBatch();
			
			if( !msgProceso.isEmpty() ){
				return;
			}
			
			int contadorFormaPago = 0;
			for (MetodosPago formaPago : formasdepago) {
				
				numeroReciboJde = numerosReciboJde.get(contadorFormaPago++);
				
				formasdepagoProceso = new ArrayList<MetodosPago>();
				formasdepagoProceso.add(formaPago);
				
				grabarReciboEdwards();
				
				if( !msgProceso.isEmpty() ){
					return;
				}
				
			}
			
			actualizarSaldoFacturas();
			if( !msgProceso.isEmpty() ){
				return;
			}
			
			//&& =========== Insert cabecera de recibo.
			for (ReciboF03B13 b13 : rcsF03B13) {
				lstSqlsInserts.add( new String[]{ b13.insertStatement(), "Grabar Cabecera de recibo F03B13"} );
			}
			
			//&& =========== Insert detalle de recibo.
			for (ReciboDetalleF03B14 b14 : rcsF03B14) {
				lstSqlsInserts.add(new String[]{ b14.insertStatement(), "Grabar detalle de recibo F03B14"});
			}
			
			
			actualizarUltimaFechaPago();
			if( !msgProceso.isEmpty() ){
				return;
			}
			
			for (String[] querys : lstSqlsInserts) {
				
				try {
					
				} catch (Exception e) {
					
					msgProceso = "fallo en interfaz Edwards "+ querys[1] ;
					
					break;
				}
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void procesarPagosFacturas(Session session){
		
		try {
			
			msgProceso = "" ;
			
			fecharecibojde = String.valueOf( FechasUtil.dateToJulian(fecharecibo) ) ;
			horarecibojde = new SimpleDateFormat("HHmmss").format(fecharecibo);
			
			
			formasdepagoProceso = new ArrayList<MetodosPago>( formasdepago );
			
			lstSqlsInserts = new ArrayList<String[]>();
			
			crearF03b11FromCredHdr();
			
			if( !msgProceso.isEmpty() ){
				return;
			}
			
			crearHistoricoFacturas();
			
			if( !msgProceso.isEmpty() ){
				return;
			}
			
			grabarControlBatch();
			
			if( !msgProceso.isEmpty() ){
				return;
			}
			
			
			int contadorFormaPago = 0;
			for (MetodosPago formaPago : formasdepago) {
				
				numeroReciboJde = numerosReciboJde.get(contadorFormaPago++);
				
				formasdepagoProceso = new ArrayList<MetodosPago>();
				formasdepagoProceso.add(formaPago);
				
				grabarReciboEdwards();
				
				if( !msgProceso.isEmpty() ){
					return;
				}
				
			}
			
			actualizarSaldoFacturas();
			
			if( !msgProceso.isEmpty() ){
				return;
			}
			
			actualizarUltimaFechaPago();
			if( !msgProceso.isEmpty() ){
				return;
			}
			
			//&& =========== Insert cabecera de recibo.
			for (ReciboF03B13 b13 : rcsF03B13) {
				lstSqlsInserts.add( new String[]{ b13.insertStatement(), "Grabar Cabecera de recibo F03B13"} );
			}
			
			//&& =========== Insert detalle de recibo.
			for (ReciboDetalleF03B14 b14 : rcsF03B14) {
				lstSqlsInserts.add(new String[]{ b14.insertStatement(), "Grabar detalle de recibo F03B14"});
			}
			
			// && =========== use an specific database connection under Hibernate
			
			if( executeQueries ){
			
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
			}
			 
			
		} catch (Exception e) {
			 e.printStackTrace();
			 
			 msgProceso = "Error al aplicar los pagos a las facturas de credito " ;
		}
		
	}

	//&& ===============================================================================//
	//&& ======================= actualizar la fecha de pago  ==========================//
	public static void actualizarUltimaFechaPago(){
		
		try {
			
			String strFechaReciboJulian = String.valueOf( FechasUtil.dateToJulian(fecharecibo) ); 

			String update =
			" update @JDEDTA.F03012 f set " +
			"	AIDLP = @FECHAPAGO,  " +
			"	AIALP = ( case when  AIDLP = @FECHAPAGO then  ( AIALP + @MONTOPAGO ) else @MONTOPAGO end  )," +
			"	AIUPMJ = @FECHAPAGO," +
			"	AIUPMT = @HORAPAGO, " +
			"	AIJOBN = '@SISTEMA'," +
			"	AIPID  = '@SISTEMA', " +
			"	AIUSER = '@CODUSER' " +
			" where aian8 = @CODCLI" ;
			
			update = update
			.replace("@JDEDTA", PropertiesSystem.JDEDTA )
			.replace("@FECHAPAGO",  strFechaReciboJulian  )
			.replace("@MONTOPAGO",  montoEnteroTotalRecibo.toString() ) 
			.replace("@CODCLI",  facturasJde.get(0).getRpan8() )
			
			.replace("@HORAPAGO", horarecibojde )
			.replace("@SISTEMA", programaActualiza )
			.replace("@CODUSER", usuario )
			 
			;
			
			lstSqlsInserts.add(new String[]{update, "Actualización de última fecha de pago del cliente "});
			
			
		} catch (Exception e) {
			msgProceso = " no se ha podido actualizar la ultima fecha de pago del cliente " ;
			e.printStackTrace();
		}
		
		
	}
	
	
	
	public static void grabarControlBatch(){
		try {
			
			BigDecimal totalFacturas = montoEnteroTotalRecibo;
			
			BatchControlF0011 f0011 = new BatchControlF0011(
				 CodigosJDE1.RECIBOCREDITO.codigo(),
				 numeroBatchJde,
				 "0",
				 usuario,
				 totalFacturas.toString(),
				 String.valueOf( numerosReciboJde.size() ),
				"0",
				 fecharecibo,
				 moduloSistema,
				 getEstadobatch().codigo()
			); 
			
			lstSqlsInserts.add(new String[]{ f0011.insertStatement(), "Grabar registro de control de batchs F0011"});
			
		} catch (Exception e) {
			e.printStackTrace();
			
			msgProceso = "Error al generar los datos para el control de Batch F0011" ;
		}
	}
	
	
	public static void grabarReciboEdwards(){
		
		try {
			
			
			List<FacturaF03B11> facturas  = new ArrayList<FacturaF03B11>(facturasJde);
			
			List<MetodosPago> formasPago = new ArrayList<MetodosPago>(formasdepagoProceso);
			
			for (MetodosPago formadepago : formasPago) {
				formadepago.setMontoEquivalenteEntero(String.format("%1$.2f", formadepago.getEquivalente() ).replace(".", "") );
			}
			
			MetodosPago mpTasa = (MetodosPago)
			CollectionUtils.find(formasPago, new Predicate(){
				@Override
				public boolean evaluate(Object o) {
					return ( (MetodosPago)o).getTasa().compareTo(BigDecimal.ONE) != 0;
				}
			});
			
			if( mpTasa != null ){
				tasaCambioRecibo = mpTasa.getTasa();
			}
			
			List<String[]> cuentasFormasPago = Divisas.cuentasFormasPago(formasPago, caid, codcomp);
			if( cuentasFormasPago == null || cuentasFormasPago.isEmpty() ){
				msgProceso = "No se han encontrado las cuentas para los metodos de pago registrados";
				return;
			}

			//&& ==================================================================================== && //
			//&& ====================== crear la cabecera del recibo F03B13 ========================= && //
			//&& ==================================================================================== && //
			FacturaF03B11 f03b11 = facturas.get(0);
			String cuentaContableRecibo = cuentasFormasPago.get(0)[1];
			String descripcionRecibo = "R:"+numrec +" Ca:" +caid +" Com:" + codcomp.trim();
 
			String montoDomestico  = "0";
			String montoExtranjero = "0" ;
			
			boolean reciboMonedaExtranjera = ( new BigDecimal ( f03b11.getRpcrr() ).compareTo(BigDecimal.ZERO) == 1  );
			BigDecimal totalFacturas = CodeUtil.sumPropertyValueFromEntityList(facturas, "montoaplicado", false); 
			
			if(reciboMonedaExtranjera){
				montoDomestico = String.valueOf( totalFacturas.multiply(tasaCambioRecibo).longValue() ) ;
				montoExtranjero = totalFacturas.toString();
			}else{
				montoDomestico = totalFacturas.toString();
			}
			
			ReciboF03B13 f03b13  =  new ReciboF03B13( 
					 numeroReciboJde,
					 String.valueOf(numrec), //"", //rycknu
					 f03b11.getRpan8(),
					 f03b11.getRppyr(),
					 f03b11.getRpco(),
					 numeroBatchJde,
					 f03b11.getRppa8(),
					 montoDomestico,
					 f03b11.getRpbcrc(),
					 f03b11.getRpcrrm(),
					 f03b11.getRpcrcd(),
					 tasaCambioRecibo.toString(),
					 montoExtranjero,
					 cuentaContableRecibo,
					 descripcionRecibo,
					 f03b11.getRpalph(),
					 f03b11.getRpuser(),
					 CodigosJDE1.RECIBOCREDITO.codigo(),
					 fecharecibo
					);
		 
			//&& ================================================================================================================== && //
			//&& ===== distribuir las formas de pago por los saldos de las facturas y generar los registros de F03B14 ============= && //
			//&& ================================================================================================================== && //
			
			int rowCount = 0;
			 
			String strIdCuentaFormaPago ;
			BigDecimal saldoFormaPago;
			BigDecimal saldoFactura;
			BigDecimal tasaDiferencialCambiario;
			
			List<ReciboDetalleF03B14> detallesRecibos = new ArrayList<ReciboDetalleF03B14>() ;
			
			formasDePago:
			for (final MetodosPago formapago : formasPago) {
				
				saldoFormaPago = new BigDecimal( formapago.getMontoEquivalenteEntero() ) ; 
				tasaDiferencialCambiario =  ( formapago.getTasa().compareTo(BigDecimal.ONE) == 0 ) ? tasaCambioOficial:  formapago.getTasa() ; 

				strIdCuentaFormaPago = ( (String[])
				CollectionUtils.find(cuentasFormasPago, new Predicate(){
					@Override
					public boolean evaluate(Object o) {
						String[] dtaCuenta = (String[])o;
						return dtaCuenta[6].compareTo(formapago.getMetodo()) == 0  &&  dtaCuenta[7].compareTo(formapago.getMoneda() ) == 0;
					}
				}) ) [1];
				
				
				CollectionUtils.filter(facturas, new Predicate(){
					@Override
					public boolean evaluate(Object o) {
						return !( new BigDecimal ( ( (FacturaF03B11)o ).getMontoaplicado() ).compareTo(BigDecimal.ZERO) == 0 ) ;
					}
				});
				
				//Si despues de filtrar la lista es cero entonces mandar el error
				if (facturas == null || facturas.size() <= 0) {
					throw new Exception("No se encontraron facturas a aplicar.");
				}
				
				facturasPendientes:
				for (FacturaF03B11 factura : facturas) {
					
					BigDecimal tasaFactura = new BigDecimal( factura.getTasaoriginal() ) ;
					
					boolean facturaMonedaExtranjera = new BigDecimal( factura.getRpcrr() ).compareTo(BigDecimal.ZERO) == 1 ;
					
					saldoFactura = new BigDecimal( factura.getMontoaplicado() ) ; 
					BigDecimal strMontoCorFacturaOriginal = new BigDecimal(factura.getRpaap());
					BigDecimal strMontoDolFacturaOriginal = new BigDecimal(factura.getRpfap());
					
					ReciboDetalleF03B14 rc = new ReciboDetalleF03B14(
						 numeroReciboJde,
						 String.valueOf(++rowCount),
						 String.valueOf(numrec), //"", //rzcknu
						 factura.getRpdoc(),
						 factura.getRpdct(),
						 factura.getRpkco(),
						 factura.getRpsfx(),
						 factura.getRpan8(),
						 
						 factura.getRpaid(), //strIdCuentaFormaPago,
						 
						 factura.getRpco(),
						 numeroBatchJde,
						 CodigosJDE1.RECIBOCREDITO.codigo(),
						 
						 factura.getRppa8(),
						 saldoFactura.negate().toString(),
						 factura.getRpbcrc(),
						 factura.getRpcrrm(),
						 factura.getRpcrcd(),
						 
						 factura.getRpcrr(),
						 saldoFactura.negate().toString(),
						 factura.getRpcrcd(),
						 saldoFactura.negate().toString(),
						 factura.getRpmcu(),
						 
						 descripcionRecibo,
						 //factura.getRpcrr(),
						 factura.getTasaoriginal(),
						 
						 factura.getRptorg(),
						 
						 "0", // monto del diferencial cambiario
						 "", // cuenta del diferencial cambiario
						 
						 factura.getRpdgj(),
						 factura.getRpddj(),
						 factura.getRpddnj(),
						 
						 (factura.isPagoparcial()? "1":"0" ),
						 
						 fecharecibo,
						 factura.getRpglc()
							
					);		
					
					if( saldoFormaPago.compareTo( saldoFactura ) ==  1 ){
						
						rc.setRzpaap("0");
						rc.setRzpfap("0");
						rc.setRztaap( saldoFactura.negate().toString() );
						
						if( facturaMonedaExtranjera  ){
							
							rc.setRzpfap( saldoFactura.negate().toString() );
							rc.setRzpaap( String.valueOf( saldoFactura.multiply( tasaDiferencialCambiario ).negate().longValue() ) ) ;

							BigDecimal montoNacionalTasaFechaFactura =  (saldoFactura.compareTo(strMontoDolFacturaOriginal)==1 || saldoFactura.compareTo(strMontoDolFacturaOriginal)==0 ? strMontoCorFacturaOriginal :  new BigDecimal(String.valueOf(saldoFactura.multiply( tasaFactura ).longValue() )));  //saldoFactura.multiply( tasaFactura ) ;
							BigDecimal montoNacionalTasaFechaActual =  new BigDecimal(String.valueOf(saldoFactura.multiply( tasaDiferencialCambiario ).longValue()));
							
							BigDecimal diferencialCambiario = montoNacionalTasaFechaActual.subtract(montoNacionalTasaFechaFactura);
 
							if(diferencialCambiario.compareTo(BigDecimal.ZERO) != 0){
								
								rc.setRzagl( String.valueOf(diferencialCambiario.longValue()));
								
								if( diferencialCambiario.compareTo(BigDecimal.ZERO ) == 1  ){
									rc.setRzaidt( factura.getGainAccountId()  );
								}else{
									rc.setRzaidt( factura.getLossAccountId() );
								}
								
							}
 
							BigDecimal montoPagoDomestico = new BigDecimal( rc.getRzpaap() ).add( new BigDecimal( rc.getRzagl() ) ).negate() ;
							
							factura.setSumaMontosAplicadosReciboDom(
									new BigDecimal( factura.getSumaMontosAplicadosReciboDom() ).add(montoPagoDomestico) .toString()
							);
							
							
						}else{
							rc.setRzpaap(saldoFactura.negate().toString());
						}
						
						rcsF03B14.add(rc);
						
						factura.setSumaMontosAplicadosRecibo(
								saldoFactura.add( new BigDecimal( factura.getSumaMontosAplicadosRecibo() )  ).toString()
						); 
						
						
						saldoFormaPago = saldoFormaPago.subtract(saldoFactura);
						saldoFactura = BigDecimal.ZERO;
						
						factura.setMontoaplicado( saldoFactura.toString() );
						formapago.setMontoEquivalenteEntero( saldoFormaPago.toString() );
						
						detallesRecibos.add(rc);
						
						continue facturasPendientes;
						
					}
					
					if( saldoFormaPago.compareTo( saldoFactura ) == -1 ){
						
						rc.setRzpaap("0");
						rc.setRzpfap("0");
						rc.setRztaap( saldoFormaPago.negate().toString() );
						
						if( facturaMonedaExtranjera  ){
							
							rc.setRzpfap( saldoFormaPago.negate().toString() );
							rc.setRzpaap( String.valueOf( saldoFormaPago.multiply( tasaDiferencialCambiario ).negate().longValue() ) ) ;
							
							BigDecimal montoNacionalTasaFechaFactura =  (saldoFormaPago.compareTo(strMontoDolFacturaOriginal)==1 || saldoFormaPago.compareTo(strMontoDolFacturaOriginal)==0 ? strMontoCorFacturaOriginal :  new BigDecimal(String.valueOf(saldoFormaPago.multiply( tasaFactura ).longValue() )) );
							BigDecimal montoNacionalTasaFechaActual =  new BigDecimal(String.valueOf(saldoFormaPago.multiply( tasaDiferencialCambiario ).longValue() ));
							
							BigDecimal diferencialCambiario = montoNacionalTasaFechaActual.subtract(montoNacionalTasaFechaFactura);
							 
							if(diferencialCambiario.compareTo(BigDecimal.ZERO) != 0){
								
								rc.setRzagl( String.valueOf(diferencialCambiario.longValue()));
								
								if( diferencialCambiario.compareTo(BigDecimal.ZERO ) == 1  ){
									rc.setRzaidt( factura.getGainAccountId()  );
								}else{
									rc.setRzaidt( factura.getLossAccountId() );
								}
								
							}
							
							BigDecimal montoPagoDomestico = new BigDecimal( rc.getRzpaap() ).add( new BigDecimal( rc.getRzagl() ) ).negate() ;
							
							factura.setSumaMontosAplicadosReciboDom(
									new BigDecimal( factura.getSumaMontosAplicadosReciboDom() ).add(montoPagoDomestico) .toString()
							);
							
						}else{
							rc.setRzpaap(saldoFormaPago.negate().toString());
						}
						
						rcsF03B14.add(rc);
						
						factura.setSumaMontosAplicadosRecibo(
								saldoFormaPago.add( new BigDecimal( factura.getSumaMontosAplicadosRecibo() )  ).toString()
						);
						
						saldoFactura = saldoFactura.subtract( saldoFormaPago );
						saldoFormaPago = BigDecimal.ZERO ;
						
						factura.setMontoaplicado( saldoFactura.toString() );
						formapago.setMontoEquivalenteEntero( saldoFormaPago.toString() );
						
						detallesRecibos.add(rc);
						
						continue formasDePago;
						
					}
					
					if( saldoFormaPago.compareTo( saldoFactura ) == 0 ){
						
						rc.setRzpaap("0");
						rc.setRzpfap("0");
						rc.setRztaap( saldoFactura.negate().toString() );
						
						if( facturaMonedaExtranjera  ){
							
							rc.setRzpfap( saldoFactura.negate().toString());
							rc.setRzpaap( String.valueOf( saldoFactura.multiply( tasaDiferencialCambiario ).negate().longValue() ) ) ;
							
							BigDecimal montoNacionalTasaFechaFactura =  (saldoFactura.compareTo(strMontoDolFacturaOriginal)==1 || saldoFactura.compareTo(strMontoDolFacturaOriginal)==0 ? strMontoCorFacturaOriginal :  new BigDecimal(String.valueOf( saldoFactura.multiply( tasaFactura ).longValue() ) ) );
							BigDecimal montoNacionalTasaFechaActual =  new BigDecimal(String.valueOf( saldoFactura.multiply( tasaDiferencialCambiario ).longValue() ) );
							
							BigDecimal diferencialCambiario = montoNacionalTasaFechaActual.subtract(montoNacionalTasaFechaFactura);
						 
							if(diferencialCambiario.compareTo(BigDecimal.ZERO) != 0){
								rc.setRzagl( String.valueOf(diferencialCambiario.longValue()));

								if( diferencialCambiario.compareTo(BigDecimal.ZERO ) == 1  ){
									rc.setRzaidt( factura.getGainAccountId()  );
								}else{
									rc.setRzaidt( factura.getLossAccountId() );
								}
								
							}
							
							BigDecimal montoPagoDomestico = new BigDecimal( rc.getRzpaap() ).add( new BigDecimal( rc.getRzagl() ) ).negate() ;
							
							factura.setSumaMontosAplicadosReciboDom(
									new BigDecimal( factura.getSumaMontosAplicadosReciboDom() ).add(montoPagoDomestico) .toString()
							);
							
						}else{
							rc.setRzpaap(saldoFactura.negate().toString());
						}
						
						rcsF03B14.add(rc);
						factura.setSumaMontosAplicadosRecibo(
								saldoFactura.add( new BigDecimal( factura.getSumaMontosAplicadosRecibo() )  ).toString()
						);
						
						saldoFormaPago = BigDecimal.ZERO ;
						saldoFactura =  BigDecimal.ZERO;
						
						factura.setMontoaplicado( saldoFactura.toString() );
						formapago.setMontoEquivalenteEntero( saldoFormaPago.toString() );
						
						detallesRecibos.add(rc);
						
						continue formasDePago;
					}
				}
			}
			
			//&& ================= sumar los montos aplicados en el detalle de los recibos, para que cuadren con la cabecera del recibo
			BigDecimal ryckam = BigDecimal.ZERO;
			BigDecimal ryfcam = BigDecimal.ZERO;
			for (ReciboDetalleF03B14 rc : detallesRecibos) {
				ryckam = ryckam.add(new BigDecimal(rc.getRzpaap()).negate() );
				ryfcam = ryfcam.add(new BigDecimal(rc.getRzpfap()).negate() );
			}
			
			f03b13.setRyckam(ryckam.toString());
			f03b13.setRyfcam(ryfcam.toString());
			
			if( ryfcam.compareTo(BigDecimal.ZERO) == 1){
				f03b13.setRycrr( ryckam.divide(ryfcam, 4, RoundingMode.HALF_UP).toString() ) ;
			}else{
				f03b13.setRycrr( "0" ) ;
			}
			
			rcsF03B13.add(f03b13);

			montoReciboLocal = f03b13.getRyckam();
		    montoReciboExtranjero = f03b13.getRyfcam();
			
			
		} catch (Exception e) {
			e.printStackTrace();
			msgProceso = "No se ha podido crear el registro del recibo en Edward's";
		} 
		
	}
	
	private static void actualizarSaldoFacturas(){
		try {
			
			for (FacturaF03B11 f03b11 : facturasJde) {
				
				
				BigDecimal rpaap = new BigDecimal( f03b11.getRpaap() );
				BigDecimal rpfap = new BigDecimal( f03b11.getRpfap() );
				
				BigDecimal aplicadoOrig = new BigDecimal( f03b11.getMontoaplicado() ) ;
				BigDecimal aplicado = new BigDecimal( f03b11.getSumaMontosAplicadosRecibo() ) ;
				
				BigDecimal tasa = new BigDecimal( f03b11.getRpcrr() ) ;
				
				boolean facturaMonedaNacional =  tasa.compareTo( BigDecimal.ZERO ) == 0;
				
				if( facturaMonedaNacional ) {
					
					rpaap = rpaap.subtract( aplicado ) ;
					
				}else{
				
					rpfap = rpfap.subtract( aplicado ) ;
					
					BigDecimal montoDomDesdeSumaPagos = new BigDecimal( f03b11.getSumaMontosAplicadosReciboDom() );
					
					rpaap = rpaap.subtract( montoDomDesdeSumaPagos ).setScale(2, RoundingMode.HALF_UP) ;
					
					if( rpaap.compareTo(BigDecimal.ZERO) != 0  && rpfap.compareTo(BigDecimal.ZERO) == 0 ){
						final String factura = f03b11.getRpdoc();
						
						ReciboDetalleF03B14 rcAjusteDc = (ReciboDetalleF03B14)
						CollectionUtils.find(rcsF03B14, new Predicate(){
							@Override
							public boolean evaluate(Object o) {
								ReciboDetalleF03B14 f14tmp = (ReciboDetalleF03B14)o;
								return 
								f14tmp.getRzdoc().compareTo( factura ) == 0 &&
								f14tmp.getRzsfx().compareTo( f03b11.getRpsfx() ) == 0 &&
								new BigDecimal ( f14tmp.getRzagl() ).compareTo(BigDecimal.ZERO) != 0;	
							}
						}) ;
						
						if( rcAjusteDc != null ){
							rcAjusteDc.setRzagl(  new BigDecimal( rcAjusteDc.getRzagl() ).subtract(rpaap).toString() );
						}
						
						rpaap = BigDecimal.ZERO ;
						
						
					}
					else if( rpaap.compareTo(BigDecimal.ZERO) <= 0  && rpfap.compareTo(BigDecimal.ZERO) > 0 ){
						final String factura = f03b11.getRpdoc();
						
						ReciboDetalleF03B14 rcAjusteDc = (ReciboDetalleF03B14)
						CollectionUtils.find(rcsF03B14, new Predicate(){
							@Override
							public boolean evaluate(Object o) {
								ReciboDetalleF03B14 f14tmp = (ReciboDetalleF03B14)o;
								return 
								f14tmp.getRzdoc().compareTo( factura ) == 0 &&
								f14tmp.getRzsfx().compareTo( f03b11.getRpsfx() ) == 0 &&
								new BigDecimal ( f14tmp.getRzagl() ).compareTo(BigDecimal.ZERO) != 0;	
							}
						}) ;
						
						if( rcAjusteDc != null ){
							rcAjusteDc.setRzagl(  new BigDecimal( rcAjusteDc.getRzagl() ).subtract(rpaap).add(rpfap.multiply(tasa)).toString() );
							
						}
						
						rpaap = rpfap.multiply(tasa) ;
						
						
					}
					
					
				} 
				f03b11.setRpaap( rpaap.toString() );
				f03b11.setRpfap( rpfap.toString() );

				lstSqlsInserts.add(new String[]{ f03b11.updateStatement(), "Actualización de saldo de factura " + f03b11.getRpdoc() });
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			msgProceso = "Error al crear sentencias de actualizacion de saldos de facturas ";
		}
	}
	
	private static void crearHistoricoFacturas(){
		try {
			
 
			String sqlInsert = "INSERT INTO @BDCAJA.HISTORICO_PAGOS_CREDITO " +
			"( RPAN8, RPICU, RPDOC, RPDCT, RPSFX, RPMCU, RPKCO, " +
			"MCRP01, RPCRCD, RPCRR, RPAAP, RPFAP, RPAG, RPACR, RPDIVJ, " +
			"RPPO, RPDCTO, CAID, NUMREC, FECHARECIBO, HORARECIBO, " +
			"CODIGOUSUARIO, USUARIO )" +
			 
			" ( @SELECT_DATA_FROM_F03B11 )" ;
			
			String strSelectCampos =
			" select " +
			" RPAN8, RPICU, RPDOC, RPDCT, RPSFX, RPMCU, RPKCO,  '"+codcomp.trim()+"' MCRP01 , "+
			" RPCRCD, RPCRR, RPAAP, RPFAP, RPAG, RPACR, RPDIVJ, RPPO, RPDCTO, " +
			  caid +" CAID, " + numrec + ", '"+ new SimpleDateFormat("yyyy-MM-dd").format(fecharecibo) +"', " + 
			" '"+ new SimpleDateFormat("HH:mm:ss").format(fecharecibo) +"', "+
			" "+codigousuario+" CODIGOUSUARIO, '"+ usuario +"' USUARIO  " +
			
			" FROM @JDEDTA.F03B11 " +
			" WHERE " ;
			
			String strCondiciones = 
			" RPAN8 = @RPAN8 AND RPICU = @RPICU AND RPDOC = @RPDOC AND RPDCT = '@RPDCT' AND RPSFX = '@RPSFX' AND trim(RPMCU) = '@RPMCU' ";		 

			String strWhere = "";
			
			for (FacturaF03B11 f03b11 : facturasJde) {
				
				strWhere +=  " ( " +
					strCondiciones
						.replace("@RPAN8",  f03b11.getRpan8() )
						.replace("@RPICU", f03b11.getRpicu() )
						.replace("@RPDOC", f03b11.getRpdoc() )
						.replace("@RPDCT", f03b11.getRpdct() )
						.replace("@RPSFX", f03b11.getRpsfx() )
						.replace("@RPMCU", f03b11.getRpmcu().trim() ) + " ) OR ";
			}
			
			int lastOR = strWhere.lastIndexOf("OR");
			strWhere = strWhere.substring(0, lastOR);
			
			String sql = sqlInsert.replace("@SELECT_DATA_FROM_F03B11", (strSelectCampos + strWhere ) ) ;
			
			sql = sql
					.replace("@JDEDTA", PropertiesSystem.JDEDTA )
					.replace("@BDCAJA", PropertiesSystem.ESQUEMA );
			
			lstSqlsInserts.add( new String[]{sql, "Historico de Saldos de Facturas" } );
			
			LogCajaService.CreateLog("crearHistoricoFacturas", "INS", sqlInsert);
		} catch (Exception e) {
			e.printStackTrace();
			LogCajaService.CreateLog("crearHistoricoFacturas", "ERR", e.getMessage());
			msgProceso = "Error al crear el historico de saldos de facturas";
		}
	}
	
	private static void crearF03b11FromCredHdr(){
		try {
			
			facturasJde = new ArrayList<FacturaF03B11>( facturas.size() );
			

			
			for (Credhdr credhr : facturas) {
				
				String fechaactualjde = String.valueOf( FechasUtil.dateToJulian(new Date()) ) ;
				String horaactualjde = new SimpleDateFormat("HHmmss").format(new Date());
				
				facturasJde.add( 	
					new FacturaF03B11(
						credhr.getId().getRppst(),
						credhr.getId().getCpendiente().toString().replace(".", ""),
						credhr.getId().getDpendiente().toString().replace(".", ""),
						usuario,
						usuario,
						fecharecibojde ,
						programaActualiza,
						fechaactualjde,
						horaactualjde,
						credhr.getId().getRppo(),
						credhr.getId().getRpdcto(),
						String.valueOf( credhr.getId().getNofactura() ), 
						credhr.getId().getTipofactura(),
						credhr.getId().getCodsuc(),
						credhr.getId().getPartida(),
						String.valueOf( credhr.getId().getCodcli() ),
						String.valueOf( credhr.getId().getRpdivj() ), 
						credhr.getId().getRpicut(), 
						String.valueOf( credhr.getId().getRpicu() ), 
						credhr.getId().getRpco(),
						String.valueOf( credhr.getId().getRppa8() ), 
						String.valueOf( credhr.getId().getRppyr() ),
						credhr.getId().getCodunineg(),
						credhr.getId().getMoneda(),
						String.format("%1$.2f", credhr.getMontoAplicar() ).replace(".", ""),
						credhr.getId().getTasa().toString(),
						credhr.getId().getNomcli(),
						credhr.getId().getRpbcrc(),
						credhr.getId().getRpcrrm(),
						
						String.valueOf( credhr.getId().getRpdgj() ),
						String.valueOf( credhr.getId().getRpddj() ),
						String.valueOf( credhr.getId().getRpddnj() ),
						credhr.getId().getRpaid(),
						credhr.isPagoparcial(),
						"0",
						credhr.getId().getTasaoriginal().toString(),
						//strCuentaDiferencialCambiario,
						"0",
						credhr.getId().getCompenslm(),
						
						credhr.getId().getLossAccountId(),
						credhr.getId().getGainAccountId()
						
					)
				);
			}
			
			montoEnteroTotalRecibo = CodeUtil.sumPropertyValueFromEntityList(facturasJde, "montoaplicado", false); 
			
			LogCajaService.CreateLog("crearF03b11FromCredHdr", "INS",facturasJde );	
		} catch (Exception e) {
			e.printStackTrace();
			LogCajaService.CreateLog("crearF03b11FromCredHdr", "ERR", e.getMessage());
			msgProceso = "Error al crear F03B11 desde Credhdr " ;
		} 
	}
	
	public static CodigosJDE1 getEstadobatch() {
		
		if (estadobatch  == null )
			estadobatch = CodigosJDE1.BATCH_ESTADO_PENDIENTE ;
		
		return estadobatch;
	}
	
}

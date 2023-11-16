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
import com.casapellas.entidades.Vf0901;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.PropertiesSystem;

public class ProcesarEntradaDeDiarioCustom {
	public static String msgProceso;
	public static List<String[]> lstSqlsInserts;
	
	public static String monedaComprobante;
	public static String monedaLocal;
	public static Date fecharecibo;
	public static BigDecimal tasaCambioParalela;
	public static BigDecimal tasaCambioOficial;
	public static BigDecimal montoComprobante;
	public static String tipoDocumento;
	public static String conceptoComprobante;
	
	public static String numeroBatchJde;
	public static String numeroReciboJde;

	public static String usuario;
	public static int codigousuario;
	public static String programaActualiza;
	
	private static String fecharecibojde;
	
	public static String[] valoresJdeInsContado;

	
	public static List<DatosComprobanteF0911> lineasComprobante;
	
	public static String tiposolicitud;
	public static String numerosolicitud;
	public static String numerocuota;
	public static String tipointeres;
	public static String codigocliente;
	public static Date fechafactura;
	public static boolean procesarSql = true;
	
	public ProcesarEntradaDeDiarioCustom() {
		
		msgProceso = "";
		lstSqlsInserts = new ArrayList<String[]>();
		lineasComprobante = null;
		monedaComprobante = "";
		monedaLocal = "";
		fecharecibo = null;
		tasaCambioParalela = null;
		tasaCambioOficial = null;
		montoComprobante = null;
		tipoDocumento = "";
		conceptoComprobante = "";
		numeroBatchJde = "";
		numeroReciboJde = "";
		usuario = "";
		codigousuario = 0;
		programaActualiza = "";
		fecharecibojde = "";
		
		tiposolicitud = "";
		numerosolicitud = "";
		numerocuota = "";
		tipointeres = "";
		
		 procesarSql = true;
		
	}

	public static void procesarEntradaDeDiarioCustom(Session sesion){
		try {
			
			msgProceso = "" ;
			
			fecharecibojde = String.valueOf( FechasUtil.dateToJulian(fecharecibo) ) ;
			
			grabarControlBatch();
			if( !msgProceso.isEmpty() ){
				return ;
			}
			
			generarEntradasF0911();
			if( !msgProceso.isEmpty() ){
				return ;
			}
			
			
			if(procesarSql){
				
				for (String[] querys : lstSqlsInserts) {
					
					System.out.print ("Query " + querys[0] + "\n descrip: " + querys[1]);
	
					try {
						
						boolean execute = ConsolidadoDepositosBcoCtrl .executeSqlQueryTx(sesion, querys[0]);
					
					} catch (Exception e) {
						
						msgProceso = "fallo en interfaz Edwards "+ querys[1] ;
						return;
						 
					}
				}
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			msgProceso = "Error al procesar los datos para la entrada de diario" ;
		}
	}
	
	
	public static void procesarEntradaDeDiario(Session sesion, Transaction trans){
		try {
			
			msgProceso = "" ;
			
			fecharecibojde = String.valueOf( FechasUtil.dateToJulian(fecharecibo) ) ;
			
			grabarControlBatch();
			if( !msgProceso.isEmpty() ){
				return ;
			}
			
			generarEntradasF0911();
			if( !msgProceso.isEmpty() ){
				return ;
			}
			
			
			if(procesarSql){
				
				for (String[] querys : lstSqlsInserts) {
					
					System.out.print ("Query " + querys[0] + "\n descrip: " + querys[1]);
	
					try {
						
						boolean execute = ConsolidadoDepositosBcoCtrl .executeSqlQuery(sesion, trans,querys[0]);
					
					} catch (Exception e) {
						
						msgProceso = "fallo en interfaz Edwards "+ querys[1] ;
						return;
						 
					}
				}
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			msgProceso = "Error al procesar los datos para la entrada de diario" ;
		}
	}
	
	//**********************************************************
	//----------------------------------------------------------
	//++++++++++++++++++       FIN       +++++++++++++++++++++++
	//----------------------------------------------------------
	//**********************************************************
	
	public static void generarEntradasF0911(){
		
		try {

			boolean comprobanteMonedaExtranjera  = monedaComprobante.compareTo(monedaLocal) != 0;
			
			
			List<Vf0901> cuentasContables = datosCuentasContables() ;

			
			int lineNumber = 0 ;
			 for ( final DatosComprobanteF0911 dtaF0911 : lineasComprobante) {
				
				 Vf0901 cuentaContable = (Vf0901)
				 CollectionUtils.find(cuentasContables, new Predicate(){
					@Override
					public boolean evaluate(Object o) {
						return  ((Vf0901)o).getId().getGmaid().compareTo(dtaF0911.getIdcuenta() ) == 0 ;
					}
				 });
				 
				 BigDecimal monto = dtaF0911.getMonto();
				 
				 EntradaDeDiarioF0911 f0911 = new  EntradaDeDiarioF0911(
						 dtaF0911.getCompaniaComprobante(), 
						 tipoDocumento,
						 numeroReciboJde,
						 fecharecibojde,
						 numeroBatchJde,
						 valoresJdeInsContado[8],
						 cuentaContable.getId().getGmco(),
						 cuentaContable.getId().getGmaid(),
						 cuentaContable.getId().getGmmcu(),
						 cuentaContable.getId().getGmobj(),
						 cuentaContable.getId().getGmsub(),
						 dtaF0911.getSublibro(),
						 dtaF0911.getTipoSublibro(),
						 monedaComprobante,
						 dtaF0911.getMonto().setScale(2, RoundingMode.HALF_UP).toString().replace(".", ""),
						 conceptoComprobante,
						 usuario,
						 tasaCambioOficial.toString(),
						 dtaF0911.getDescripcion(),
						 monedaLocal,
						 (String.valueOf((++lineNumber) ) ),
						 valoresJdeInsContado[5],
						 fecharecibo
						 );
				 
				 
				 f0911.setGlan8(codigocliente);
				 
				 if( tiposolicitud != null && !tiposolicitud.isEmpty() ){
					 f0911.setNumerocuota( Integer.parseInt( numerocuota ) );
					 f0911.setGldcto(tiposolicitud);
					 f0911.setGlpo(numerosolicitud);
					 f0911.setGlglc(tipointeres);
					 f0911.setGlan8(codigocliente);
				 }
					
				 if( fechafactura != null  ){
					 String fechafacturajul = String.valueOf( FechasUtil.dateToJulian(fechafactura) ) ;
					 f0911.setGlivd(fechafacturajul);
				 }
				 
				 
				 if(comprobanteMonedaExtranjera){
					 
					 f0911.setGllt(valoresJdeInsContado[2]);
					 lstSqlsInserts.add(new String[]{ f0911.insertStatement(), "Grabar registro de Entrada de diario F0911"});
					 
					 f0911.setGllt(valoresJdeInsContado[5]);
					 f0911.setMontoExtranjero( monto.setScale(2, RoundingMode.HALF_UP).toString().replace(".", "") ) ;
					 f0911.setGlaa( monto.multiply(tasaCambioOficial).setScale(2, RoundingMode.HALF_UP).toString().replace(".", "") );
					
					 lstSqlsInserts.add(new String[]{ f0911.insertStatement(), "Grabar registro de Entrada de diario F0911"});
					 
				 }else{
					 lstSqlsInserts.add(new String[]{ f0911.insertStatement(), "Grabar registro de Entrada de diario F0911"});
				 }
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			msgProceso = "Error al generar los datos JDE para entradas de diario F0911" ;
		}
		
	}
	
	public static void grabarControlBatch(){
		try {
			
			BatchControlF0011 f0011 = new BatchControlF0011(
					valoresJdeInsContado[8],
				numeroBatchJde,
				"0",
				usuario,
				montoComprobante.setScale(2,RoundingMode.HALF_UP).toString().replace(".", ""),
				"1" ,
				"0",
				fecharecibo,
				programaActualiza,
				valoresJdeInsContado[9]
			); 
			
			lstSqlsInserts.add(new String[]{ f0011.insertStatement(), "Grabar registro de control de batchs F0011"});
			
		} catch (Exception e) {
			e.printStackTrace();
			msgProceso = "Error al generar los datos para el control de Batch F0011" ;
		}
	}

	public static List<Vf0901> datosCuentasContables(){
		List<Vf0901> cuentasContables = null;
		try {
			
			@SuppressWarnings("unchecked")
			List<String> idCuentas = (List<String>) CodeUtil.selectPropertyListFromEntity(lineasComprobante, "idcuenta", true);
			String cuentas = "";
			 
			for (String idcuenta : idCuentas) {
				cuentas += " '"+idcuenta+"', ";
			}
			cuentas = cuentas.substring(0, cuentas.lastIndexOf(","));
			
			String query = " select * from "+PropertiesSystem.ESQUEMA+".vf0901 where gmaid in ("+cuentas+") ";
			
			cuentasContables = (List<Vf0901>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, true, Vf0901.class);
			
		} catch (Exception e) {
			e.printStackTrace();
			cuentasContables = null;
		}
		return cuentasContables;
	}


}

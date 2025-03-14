package com.casapellas.controles;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.hibernate.Session;

import com.casapellas.controles.tmp.ReciboCtrl;
import com.casapellas.entidades.MetodosPago;
import com.casapellas.entidades.Metpago;
import com.casapellas.entidades.Vf0901;
import com.casapellas.jde.creditos.DefaultJdeFieldsValues;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.Divisas;
import com.casapellas.util.DocumuentosTransaccionales;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.PropertiesSystem;
import com.casapellas.entidades.ens.Vautoriz;
import com.ibm.icu.util.Calendar;

public class PlanMantenimientoTotalCtrlV2 {

	public  String moneda_base;
	public  String moneda_aplicada;
	public  String codigo_compania;
	public  String codigo_sucursal_usuario;
	public  String codigo_unidad_negocio_usuario;
	public  BigDecimal monto_aplicado;
	public  BigDecimal diferencia;
	public  BigDecimal tasaoficial;
	public  BigDecimal tasaparalela;
	public  String strMensajeProceso;
	public  int codigo_caja;
	public  int numero_recibo;
	public  int numero_recibo_fcv;
	public  int numero_contrato;
	public  int numero_batch;
	public  int numero_documento;
	public  String moneda_contrato;
	public  BigDecimal cuota_contrato_pmt_usd;
	public  BigDecimal cuota_contrato_pmt_cod;
	public  List<MetodosPago>formas_de_pago;
	public  List<MetodosPago>compra_venta_Cambio;
	public  Connection cn ;
	public  List<String[]> numeros_batch_documento ;
	public String[] valoresJdeNumeracion ;
	public String[] valoresJDEInsPMT ;
	public String[] valoresJdeInsContado;
	
	public  String  comprantesPorCompraVenta(final List<MetodosPago> formas_pago_compraVenta, List<String[]> cuentasPorMetodoPago, 
								Vautoriz vaut, int iClienteCodigo, String unidadNegocioCaja, int numero_recibo_caja, Session session, String unidadNegociosCaja, String codcomp ){
		String strMensajeProceso = "";
		boolean hecho = true;
		long lngMontoFormaDePagoExt;
		long lngMontoFormaDePagoNac;
		
		String msgLog = "" ;
		
		try {
			
			LogCajaService.CreateLog("comprantesPorCompraVenta", "INF", "INICIO - Registrando Compra Venta");
			String companiaRpkco =  codcomp;
			double lineadoc = 0 ;
			Date fechaRecibo = new Date();
			String tipodocjde = valoresJdeInsContado[1];
			String concepto = "RC:"+numero_recibo+" C:"+codigo_caja+" PMT:"+numero_contrato;
			String observacion ;
			
			String[] dtactaExt;
			String[] dtactaNac;
			String ctaBnfcuenta ;
			String ctaBnfGmaid  ;
			String ctaBnfMcu    ;
			String ctaBnfObj    ;
			String ctaBnfSub    ;
			
			msgLog = concepto;
			int numero_batch = Divisas.numeroSiguienteJdeE1( );
			int numero_documento = Divisas.numeroSiguienteJdeE1Custom(valoresJdeNumeracion[8], valoresJdeNumeracion[9] );
			
			if(numero_batch == 0) {
				 hecho = false;
				 return  strMensajeProceso = "Error al generar consecutivo n�mero de batchs para el recibo";
			}
			if (numero_documento == 0) {
				 hecho = false;
				 return  strMensajeProceso = "Error al generar consecutivo n�mero de documento para el recibo";
			}
			
			numeros_batch_documento.add(new String[]{
					Integer.toString( numero_batch ), 
					Integer.toString( numero_documento ),
					Integer.toString( numero_recibo_caja ),
					"A",
					"FCV",
					tipodocjde
				});
			
			
			ReciboCtrl recCtrl = new ReciboCtrl();
			
			BigDecimal bdTotalCompraVenta = CodeUtil.sumPropertyValueFromEntityList(formas_pago_compraVenta, "monto", false);
			
			lngMontoFormaDePagoExt = Long.parseLong( String.format("%1$.2f", bdTotalCompraVenta.doubleValue() ).replace(".", "")   );
		
			hecho = recCtrl.registrarBatchA92Custom(session, fechaRecibo, valoresJdeInsContado[8], numero_batch, lngMontoFormaDePagoExt, vaut.getId().getLogin(), 1, "PMT", valoresJdeInsContado[9] ); 
			
			if(!hecho){
				return strMensajeProceso = "No se puede generar asiento de diario por compra Venta de Moneda Extranjera";
			}
			
			for (final MetodosPago mp : formas_pago_compraVenta) {
				
				dtactaExt = (String[])
					CollectionUtils.find(cuentasPorMetodoPago, new Predicate(){
						@Override
						public boolean evaluate(Object o) {
							String[] dtacta = (String[])o;
							return 
							dtacta[8].compareTo("USD") == 0 && 
							dtacta[9].compareTo(mp.getMetodo()) == 0 ; 
						}
				});
				
				dtactaNac = (String[])
					CollectionUtils.find(cuentasPorMetodoPago, new Predicate(){
						@Override
						public boolean evaluate(Object o) {
							String[] dtacta = (String[])o;
							return 
							dtacta[8].compareTo(moneda_base) == 0 && 
							dtacta[9].compareTo(mp.getMetodo()) == 0 ; 
						}
				});
				
				if( dtactaExt == null || dtactaNac == null ){
					return strMensajeProceso = "No se encontro la cuenta de caja correspondiente para compra venta de dolares ";
				}
						
				ctaBnfcuenta = dtactaExt[0].trim();
				ctaBnfGmaid = dtactaExt[1];
				ctaBnfMcu = dtactaExt[3];
			    ctaBnfObj = dtactaExt[4];
				ctaBnfSub = dtactaExt[5];
				
				observacion = "Mp: " + obtenerDescripcionMetodoPago(mp); // mp.getMetododescrip();
				lngMontoFormaDePagoExt = (mp.getMoneda().trim().compareTo(moneda_base)==0 && mp.getMoneda().trim().compareTo(moneda_contrato)==0) ? 
						Long.parseLong( String.format("%1$.2f", mp.getMonto() ).replace(".", "")) : 
							mp.getMoneda().trim().compareTo(moneda_contrato)==0 ? 
									Long.parseLong( String.format("%1$.2f", mp.getMonto() ).replace(".", "")) :
										moneda_contrato.trim().compareTo(moneda_base)==0 ? 
											Long.parseLong( String.format("%1$.2f", mp.getMonto() ).replace(".", "")) :
												Long.parseLong( String.format("%1$.2f", (mp.getMonto()/mp.getTasa().doubleValue()) ).replace(".", ""));
							
				lngMontoFormaDePagoNac = mp.getMoneda().compareTo(moneda_base)==0 && mp.getMoneda().compareTo(moneda_contrato)==0 ? 
						Long.parseLong( String.format("%1$.2f", mp.getMonto() ).replace(".", "")) : 
							mp.getMoneda().compareTo(moneda_contrato)==0 ? 
									Long.parseLong( String.format("%1$.2f", BigDecimal.valueOf(mp.getMonto()).multiply(tasaoficial)).replace(".", "")) : 
										moneda_contrato.trim().compareTo(moneda_base)==0 ? 
											Long.parseLong( String.format("%1$.2f", BigDecimal.valueOf(mp.getMonto()).multiply(tasaoficial)).replace(".", "")) :
												Long.parseLong( String.format("%1$.2f", BigDecimal.valueOf(mp.getMonto())).replace(".", ""));
				
				//Cambio hecho 2019-04-25 por lfonseca
				if(moneda_contrato.compareTo(moneda_base)==0 ) {
					lngMontoFormaDePagoNac = lngMontoFormaDePagoNac + Long.parseLong( String.format("%1$.2f", mp.getDiferenciaCor()).replace(".", ""));
					
					hecho = recCtrl.registrarAsientoDiarioLogs(session, msgLog, fechaRecibo, companiaRpkco, tipodocjde, numero_documento, 
								( ++lineadoc ), numero_batch, ctaBnfcuenta, ctaBnfGmaid, ctaBnfMcu, ctaBnfObj, ctaBnfSub, "CA",
								moneda_contrato.compareTo(moneda_base)==0 ? mp.getMoneda() : moneda_contrato, lngMontoFormaDePagoExt, concepto, vaut.getId().getLogin(), vaut.getId().getCodapp(), 
								mp.getTasa(),  //tasaoficial, //
								"", observacion, codcomp,"","", moneda_contrato.compareTo(moneda_base)==0 ? moneda_base : moneda_contrato, //mp.getMoneda(), 
								companiaRpkco, "F", 0 );
					
					
					if(!hecho){
						return strMensajeProceso = "No se puede generar asiento de diario por compra Venta de Moneda Extranjera para metodo de pago " + mp.getMetododescrip().trim();
					}
					
					hecho = recCtrl.registrarAsientoDiarioLogs(session, msgLog, fechaRecibo, companiaRpkco, tipodocjde, numero_documento, 
							( lineadoc ), numero_batch, ctaBnfcuenta, ctaBnfGmaid, ctaBnfMcu, ctaBnfObj, ctaBnfSub, "AA",
							moneda_contrato.compareTo(moneda_base)==0 ? mp.getMoneda() : moneda_contrato, lngMontoFormaDePagoNac, concepto, vaut.getId().getLogin(), vaut.getId().getCodapp(), 
							mp.getTasa(), //tasaoficial, //
							"", observacion, codcomp,"","", moneda_base, companiaRpkco, "F", lngMontoFormaDePagoExt );
					
					if(!hecho){
						return strMensajeProceso = "No se puede generar asiento de diario por compra Venta de Moneda Extranjera para metodo de pago " + mp.getMetododescrip().trim();
					}
					
					ctaBnfcuenta = dtactaNac[0].trim();
					ctaBnfGmaid = dtactaNac[1];
					ctaBnfMcu = dtactaNac[3];
					ctaBnfObj = dtactaNac[4];
					ctaBnfSub = dtactaNac[5];
				
					hecho = recCtrl.registrarAsientoDiarioLogs(session, msgLog, fechaRecibo, companiaRpkco, tipodocjde, numero_documento, 
							( ++lineadoc ), numero_batch, ctaBnfcuenta, ctaBnfGmaid, ctaBnfMcu, ctaBnfObj, ctaBnfSub, "CA",
							moneda_contrato.compareTo(moneda_base)==0 ? mp.getMoneda() : moneda_contrato, (lngMontoFormaDePagoExt*-1), concepto, vaut.getId().getLogin(), vaut.getId().getCodapp(), 
							mp.getTasa(), //tasaoficial, //
							"", observacion, codcomp,"","", moneda_contrato.compareTo(moneda_base)==0 ? moneda_base : moneda_contrato, //mp.getMoneda(), 
							companiaRpkco, "F", 0);
					
					if(!hecho){
						return strMensajeProceso = "No se puede generar asiento de diario por compra Venta de Moneda Extranjera para metodo de pago " + mp.getMetododescrip().trim();
					}
	
					hecho = recCtrl.registrarAsientoDiarioLogs(session, msgLog, fechaRecibo, companiaRpkco, tipodocjde, numero_documento, 
							( lineadoc ), numero_batch, ctaBnfcuenta, ctaBnfGmaid, ctaBnfMcu, ctaBnfObj, ctaBnfSub, "AA",
							moneda_contrato.compareTo(moneda_base)==0 ? mp.getMoneda() : moneda_contrato, (lngMontoFormaDePagoNac*-1), concepto, vaut.getId().getLogin(), vaut.getId().getCodapp(), 
							mp.getTasa(), //tasaoficial,//
							"", observacion, codcomp,"","", moneda_base, companiaRpkco, "F", (lngMontoFormaDePagoExt*-1) );
	
					if(!hecho){
						return strMensajeProceso = "No se puede generar asiento de diario por compra Venta de Moneda Extranjera para metodo de pago " + mp.getMetododescrip().trim();
					}
				}
				else
				{
					BigDecimal tasaCalculadaActual = new BigDecimal((double)lngMontoFormaDePagoNac/lngMontoFormaDePagoExt).setScale(4, RoundingMode.HALF_UP);
					
					BigDecimal monto1 = new BigDecimal((double)lngMontoFormaDePagoExt/100).multiply(tasaCalculadaActual).setScale(2, RoundingMode.HALF_UP);
					BigDecimal monto2 = new BigDecimal((double)lngMontoFormaDePagoExt/100).multiply(tasaoficial).setScale(2, RoundingMode.HALF_UP);
					
					long lngMontoFormaDePago = Long.parseLong( String.format("%1$.2f", (monto1.subtract(monto2)) ).replace(".", "")   );
					
					
					hecho = recCtrl.registrarAsientoDiarioLogs(session, msgLog, fechaRecibo, companiaRpkco, tipodocjde, numero_documento, 
								( ++lineadoc ), numero_batch, ctaBnfcuenta, ctaBnfGmaid, ctaBnfMcu, ctaBnfObj, ctaBnfSub, "CA",
								moneda_contrato.compareTo(moneda_base)==0 ? mp.getMoneda() : moneda_contrato, (lngMontoFormaDePagoExt*-1), concepto, vaut.getId().getLogin(), vaut.getId().getCodapp(), 
								mp.getTasa(),  //tasaoficial, //
								"", observacion, codcomp,"","", moneda_contrato.compareTo(moneda_base)==0 ? moneda_base : moneda_contrato, //mp.getMoneda(), 
								companiaRpkco, "F", 0 );
					
					
					if(!hecho){
						return strMensajeProceso = "No se puede generar asiento de diario por compra Venta de Moneda Extranjera para metodo de pago " + mp.getMetododescrip().trim();
					}
					
					hecho = recCtrl.registrarAsientoDiarioLogs(session, msgLog, fechaRecibo, companiaRpkco, tipodocjde, numero_documento, 
							( lineadoc ), numero_batch, ctaBnfcuenta, ctaBnfGmaid, ctaBnfMcu, ctaBnfObj, ctaBnfSub, "AA",
							moneda_contrato.compareTo(moneda_base)==0 ? mp.getMoneda() : moneda_contrato, ((lngMontoFormaDePagoNac-lngMontoFormaDePago)*-1), concepto, vaut.getId().getLogin(), vaut.getId().getCodapp(), 
							mp.getTasa(), //tasaoficial, //
							"", observacion, codcomp,"","", moneda_base, companiaRpkco, "F", lngMontoFormaDePagoExt*-1 );
					
					if(!hecho){
						return strMensajeProceso = "No se puede generar asiento de diario por compra Venta de Moneda Extranjera para metodo de pago " + mp.getMetododescrip().trim();
					}
					
					 ctaBnfcuenta = dtactaNac[0].trim();
					 ctaBnfGmaid = dtactaNac[1];
					 ctaBnfMcu = dtactaNac[3];
					 ctaBnfObj = dtactaNac[4];
					 ctaBnfSub = dtactaNac[5];
				
					hecho = recCtrl.registrarAsientoDiarioLogs(session, msgLog, fechaRecibo, companiaRpkco, tipodocjde, numero_documento, 
							( ++lineadoc ), numero_batch, ctaBnfcuenta, ctaBnfGmaid, ctaBnfMcu, ctaBnfObj, ctaBnfSub, "CA",
							moneda_contrato.compareTo(moneda_base)==0 ? mp.getMoneda() : moneda_contrato, (lngMontoFormaDePagoExt), concepto, vaut.getId().getLogin(), vaut.getId().getCodapp(), 
							mp.getTasa(), //tasaoficial, //
							"", observacion, codcomp,"","", moneda_contrato.compareTo(moneda_base)==0 ? moneda_base : moneda_contrato, //mp.getMoneda(), 
							companiaRpkco, "F", 0);
					
					if(!hecho){
						return strMensajeProceso = "No se puede generar asiento de diario por compra Venta de Moneda Extranjera para metodo de pago " + mp.getMetododescrip().trim();
					}
	
					hecho = recCtrl.registrarAsientoDiarioLogs(session, msgLog, fechaRecibo, companiaRpkco, tipodocjde, numero_documento, 
							( lineadoc ), numero_batch, ctaBnfcuenta, ctaBnfGmaid, ctaBnfMcu, ctaBnfObj, ctaBnfSub, "AA",
							moneda_contrato.compareTo(moneda_base)==0 ? mp.getMoneda() : moneda_contrato, (lngMontoFormaDePagoNac-lngMontoFormaDePago), concepto, vaut.getId().getLogin(), vaut.getId().getCodapp(), 
							mp.getTasa(), //tasaoficial,//
							"", observacion, codcomp,"","", moneda_base, companiaRpkco, "F", (lngMontoFormaDePagoExt) );
	
					if(!hecho){
						return strMensajeProceso = "No se puede generar asiento de diario por compra Venta de Moneda Extranjera para metodo de pago " + mp.getMetododescrip().trim();
					}

					lngMontoFormaDePago = lngMontoFormaDePago - Long.parseLong( String.format("%1$.2f", mp.getDiferenciaCor()).replace(".", ""));
					
					if(lngMontoFormaDePago!=0)
					{
						String comp = Integer.parseInt(codcomp)+"";
						String[] fcvCuentaPerdiav2 = DocumuentosTransaccionales.obtenerCuentasFCVPerdida(comp).split(",",-1);
						
						String sCtaObDif = fcvCuentaPerdiav2[0];
						String sCtaSubDif = fcvCuentaPerdiav2[1];
						Vf0901 cuenta_diferencial  = new Divisas().validarCuentaF0901( unidadNegociosCaja, sCtaObDif, sCtaSubDif);
						
						if(cuenta_diferencial == null){
							return strMensajeProceso = "No se encuentra la cuenta " +  unidadNegociosCaja+"."+sCtaObDif+"."+sCtaSubDif 
									+ " para ajuste por diferencial cambiario";
						}
						
						String ctaBnfGmaidD = cuenta_diferencial.getId().getGmaid();
						String ctaBnfMcuD = cuenta_diferencial.getId().getGmmcu();
						String ctaBnfObjD = cuenta_diferencial.getId().getGmobj();
						String ctaBnfSubD = cuenta_diferencial.getId().getGmsub(); 
						String ctaBnfcuentaD = ctaBnfMcuD+"."+ctaBnfObjD+"."+ctaBnfSubD;
						String companiaCuenta = cuenta_diferencial.getId().getGmco().trim();
						
						hecho = recCtrl.registrarAsientoDiarioLogs(session, msgLog, fechaRecibo, companiaRpkco, tipodocjde, numero_documento, 
								( ++lineadoc ), numero_batch, ctaBnfcuenta, ctaBnfGmaid, ctaBnfMcu, ctaBnfObj, ctaBnfSub, "AA",
								mp.getMoneda(), (lngMontoFormaDePago), concepto, vaut.getId().getLogin(), vaut.getId().getCodapp(), 
								new BigDecimal(0), //tasaoficial,//
								"", "Dif/Camb Met: " + mp.getMetodo()+" c:"+numero_contrato, companiaCuenta,"","", moneda_base, companiaRpkco, "D", 0 );
	
						if(!hecho){
							return strMensajeProceso = "No se puede generar asiento de diario de diferencial cambiario por compra Venta de Moneda Extranjera para metodo de pago " + mp.getMetododescrip().trim();
						}
						
						hecho = recCtrl.registrarAsientoDiarioLogs(session, msgLog, fechaRecibo, companiaRpkco, tipodocjde, numero_documento, 
								( ++lineadoc ), numero_batch, ctaBnfcuentaD, ctaBnfGmaidD, ctaBnfMcuD, ctaBnfObjD, ctaBnfSubD, "AA",
								mp.getMoneda(), (-1*lngMontoFormaDePago), concepto, vaut.getId().getLogin(), vaut.getId().getCodapp(), 
								new BigDecimal(0), //tasaoficial,//
								"", "Dif/Camb Met: " + mp.getMetodo()+" c:"+numero_contrato, companiaCuenta,"","", moneda_base, companiaRpkco, "D", 0 );
	
						if(!hecho){
							return strMensajeProceso = "No se puede generar asiento de diario de diferencial cambiario por compra Venta de Moneda Extranjera para metodo de pago " + mp.getMetododescrip().trim();
						}
						
					}
				}
			}
			
		} catch (Exception e) {
			strMensajeProceso = "Error al grabar comprobante de compra venta de Monedas Extranjeras"; 
			LogCajaService.CreateLog("comprantesPorCompraVenta", "ERR", e.getMessage());
			e.printStackTrace(); 
		}finally{
			LogCajaService.CreateLog("comprantesPorCompraVenta", "INF", "FIN - Registrando Compra Venta");
		}
		return strMensajeProceso;
	}
	
	 long lngMontoAplicadoForaneo = 0;
	public  String generarComprobantesContablesPMT(Date fechaRecibo, int iClienteCodigo, 
			Vautoriz vaut, int numero_cuota, Session session  ){
		
		String strMensajeProceso = "";
		
		String tipodocjde = valoresJDEInsPMT[1];
		String tipocliente = "";
		String codbnf_aux = ""; 
		String tipoAuxiliar = "";
		
		ReciboCtrl rcCtrl = new ReciboCtrl();
		
		boolean hecho;
		long lngMontoAplicadoLocal;
		long lngMontoFormaDePago;
		long lngMontoFormaDePagoEq;
		
		try {
			
			numeros_batch_documento = new ArrayList<String[]>();
			
			if( moneda_aplicada.compareTo(moneda_base) != 0 ){
				lngMontoAplicadoForaneo = Long.parseLong( String.format("%1$.2f", monto_aplicado ).replace(".", "")   );
//				monto_aplicado = monto_aplicado.multiply( tasaoficial ).setScale(2, RoundingMode.HALF_UP);
			}
			lngMontoAplicadoLocal =  moneda_aplicada.compareTo(moneda_base) != 0 ? 
					Long.parseLong( String.format("%1$.2f", monto_aplicado.multiply( tasaoficial ).setScale(2, RoundingMode.HALF_UP) ).replace(".", "")   ) :
				Long.parseLong( String.format("%1$.2f", monto_aplicado.setScale(2, RoundingMode.HALF_UP) ).replace(".", "")   );
			
			//&& ========== Unidad de negocios de la caja donde se aplica el pago
			String strQueryExecute = " select IFNULL( (cast( c4cjmcu  as varchar(12) ccsid 37 )),'2499')  from "
					+PropertiesSystem.ESQUEMA+".f55ca014 where c4rp01 = '"+codigo_compania+"'  and c4id = " + codigo_caja; 
			
			@SuppressWarnings("unchecked")
			String unidadNegociosCaja = ( ( ArrayList<String>) ConsolidadoDepositosBcoCtrl.executeSqlQuery( strQueryExecute, true, null ) ).get(0) ;
			
			
			//&& ========== verificar si hay pagos en dolares, convertirlos a cordobas y hacer asientos contables por compra venta 
			
			List<String[]> cuentasPorMetodoPago = cuentasPorMetodoPago();
			final List<MetodosPago> formas_pago_compraVenta = new ArrayList<MetodosPago>();
			
			CollectionUtils.forAllDo(formas_de_pago, new Closure() {
				@Override
				public void execute(Object o) {
					 MetodosPago mp = (MetodosPago)o;
					 
					 if( mp.getMoneda().compareTo(moneda_contrato) != 0) {						 
						 formas_pago_compraVenta.add( mp.clone() ); 						 
					 }

				}
			}); 

		
			
			if(formas_pago_compraVenta != null && !formas_pago_compraVenta.isEmpty() ){
				
				strMensajeProceso = comprantesPorCompraVenta(formas_pago_compraVenta, cuentasPorMetodoPago, vaut, iClienteCodigo, 
						unidadNegociosCaja, numero_recibo, session, unidadNegociosCaja,codigo_sucursal_usuario);
				
				if( !strMensajeProceso.isEmpty() )
					return 	strMensajeProceso ;
				
			}
			
			//&& ====================== comprobante de pasivo
			
			numero_batch = Divisas.numeroSiguienteJdeE1(  );
			numero_documento = Divisas.numeroSiguienteJdeDocumentoE1Custom(valoresJdeNumeracion[4],valoresJdeNumeracion[5]);
		
			if(numero_batch == 0) {
				hecho = false;
				return strMensajeProceso = "Error al generar consecutivo n�mero de batchs para el recibo";
			}
			if (numero_documento == 0) {
				hecho = false;
				return strMensajeProceso = "Error al generar consecutivo n�mero de documento para el recibo";
			}
			
			numeros_batch_documento.add(new String[]{
					Integer.toString( numero_batch ), 
					Integer.toString( numero_documento ),
					Integer.toString( numero_recibo ),
					"R",
					"PM",
					tipodocjde
				});
			
			
			//&& ================ grabar el encabezado del batch.

			hecho = rcCtrl.registrarBatchA92Custom(session, fechaRecibo, valoresJDEInsPMT[8], numero_batch, 
					(moneda_contrato.compareTo(moneda_base)==0 ? 
							Long.parseLong( String.format("%1$.2f", cuota_contrato_pmt_cod).replace(".", "")   ) : 
							Long.parseLong( String.format("%1$.2f", cuota_contrato_pmt_usd).replace(".", "")   )) ,
					vaut.getId().getLogin(), 1, "PMT", valoresJDEInsPMT[9] );
			
			if(!hecho) {
				return 	strMensajeProceso =" Error al grabar encabezado de Comprobante(F0011)" ;
			}

			//&& ================ Grabar las transacciones en F0911 por cada metodo de pago.
			
			String companiaRpkco = codigo_sucursal_usuario;
			String unidadNegCtaLiquida  = CodeUtil.pad(codigo_unidad_negocio_usuario.trim(), 12, " ");  
			
			
			//&& =========== Grabar el registro en F0411 
			
			String numerocuota = (numero_cuota == 0) ? "" :  "-"+Integer.toString(numero_cuota);
			String companiaRpoc  =   companiaRpkco  ;
			String cdtaObjCtaLiquida = "";
			String cdtaSubCtaLiquida = "";
			String numeroContrato = "C-"+numero_contrato + numerocuota ;
			String observacion = "RC:"+numero_recibo+" C:"+codigo_caja+" PMT:"+numeroContrato;
			
			//parametrizacion de la cueta de banco
			int idcuenta = IDCUENTABANCOPV("ID_CUENTA_BANCO_PV",codigo_compania);
			
			hecho = registrarF0411PorPMT(session,  companiaRpkco, companiaRpoc, numero_documento, tipodocjde, iClienteCodigo,
						fechaRecibo, numero_batch, moneda_contrato, moneda_base, (moneda_contrato.compareTo(moneda_base)==0 ? 
								cuota_contrato_pmt_cod : cuota_contrato_pmt_usd) , //monto_aplicado,
						tasaoficial, idcuenta, unidadNegCtaLiquida, cdtaObjCtaLiquida,
						cdtaSubCtaLiquida, numeroContrato, vaut.getId().getLogin(), observacion,
						Integer.toString(numero_contrato), " ",  "#", "PMT" ) ;
			
			if(!hecho){
				return strMensajeProceso = "Error al grabar documento de cuentas por pagar";
			}
			
			int lineadoc = 0;
			String concepto;
			String tipoDocxMon = "D" ;
			String ctaBnfcuenta;
			String ctaBnfMcu   ;
			String ctaBnfObj   ;
			String ctaBnfSub   ;
			String ctaBnfGmaid ;
			
			concepto = "PMT:"+numero_contrato +" RC:"+numero_recibo+" C:"+codigo_caja ;
			observacion = "PMT:"+numero_contrato +" RC:" + numero_recibo+" C:" + codigo_caja ;
			
			if( !codbnf_aux.isEmpty() ){
				tipoAuxiliar = "A";
			}
			///Validacion de descuadre  
			boolean cuadrar=false;
			long  montoCuadrar = 0;
			if(formas_de_pago.size()>1) {
				long sumadeequivalencias=0;
				for(final MetodosPago mp : formas_de_pago) {
					
					lngMontoFormaDePago = mp.getMoneda().compareTo(moneda_base)==0 ? 
							mp.getMoneda().compareTo(moneda_contrato)==0 ? 
									Long.parseLong( String.format("%1$.2f", BigDecimal.valueOf(mp.getMonto()/tasaoficial.doubleValue())).replace(".", "")) : 
									Long.parseLong( String.format("%1$.2f", BigDecimal.valueOf(mp.getMonto()/tasaparalela.doubleValue())).replace(".", "")) :
							Long.parseLong( String.format("%1$.2f", BigDecimal.valueOf(mp.getMonto())).replace(".", ""));
					
					sumadeequivalencias+=	 mp.getMoneda().compareTo(moneda_base)==0 && mp.getMoneda().compareTo(moneda_contrato)==0 ? 
							Long.parseLong( String.format("%1$.2f", BigDecimal.valueOf(mp.getMonto())).replace(".", ""))
							: 
							Long.parseLong( String.format("%1$.2f", BigDecimal.valueOf(((double)lngMontoFormaDePago/100)*tasaoficial.doubleValue())).replace(".", ""));
				}
				
				lngMontoFormaDePagoEq =  Long.parseLong( String.format("%1$.2f", cuota_contrato_pmt_cod).replace(".", ""));
				
				long diferenciaExistente =sumadeequivalencias-lngMontoFormaDePagoEq;
				
				if(diferenciaExistente!=0) {
					double montoMaximopermitido = Double.parseDouble(DocumuentosTransaccionales.VALORMAXIMODESCUADRE());
					long maximopermitodo = (long) (montoMaximopermitido * 100);
					 long calculoDescuadre = maximopermitodo - (diferenciaExistente);
					if(calculoDescuadre>0) {
						cuadrar=true;
						montoCuadrar=  diferenciaExistente ;
					}
				}
				
			}
		
			
			
			
			for (final MetodosPago mp : formas_de_pago) {
				
				String[] dtacta = (String[])
						CollectionUtils.find(cuentasPorMetodoPago, new Predicate(){
							@Override
							public boolean evaluate(Object o) {
								String[] dtacta = (String[])o;
								return 
								dtacta[8].compareTo(moneda_contrato) == 0 && 
								dtacta[9].compareTo(mp.getMetodo()) == 0 ; 
							}
						});
				
				if(dtacta == null){
					return strMensajeProceso = "No se encontro la cuenta de caja correspondiente ";
				}
				
				ctaBnfcuenta = dtacta[0];
				ctaBnfGmaid = dtacta[1];
				ctaBnfMcu = dtacta[3];
				ctaBnfObj = dtacta[4];
				ctaBnfSub = dtacta[5];
				

				if(formas_de_pago.size()==1)
				{
					lngMontoFormaDePago = Long.parseLong( String.format("%1$.2f", cuota_contrato_pmt_usd).replace(".", ""));
					
					lngMontoFormaDePagoEq =  Long.parseLong( String.format("%1$.2f", cuota_contrato_pmt_cod).replace(".", ""));
												
	
					lngMontoFormaDePagoEq = lngMontoFormaDePagoEq + Long.parseLong( String.format("%1$.2f", mp.getDiferenciaCor()).replace(".", ""));
				}
				else
				{
					lngMontoFormaDePago = mp.getMoneda().compareTo(moneda_base)==0 ? 
							mp.getMoneda().compareTo(moneda_contrato)==0 ? 
									Long.parseLong( String.format("%1$.2f", BigDecimal.valueOf(mp.getMonto()/tasaoficial.doubleValue())).replace(".", "")) : 
									Long.parseLong( String.format("%1$.2f", BigDecimal.valueOf(mp.getMonto()/tasaparalela.doubleValue())).replace(".", "")) :
							Long.parseLong( String.format("%1$.2f", BigDecimal.valueOf(mp.getMonto())).replace(".", ""));
					
					lngMontoFormaDePagoEq =  mp.getMoneda().compareTo(moneda_base)==0 && mp.getMoneda().compareTo(moneda_contrato)==0 ? 
									Long.parseLong( String.format("%1$.2f", BigDecimal.valueOf(mp.getMonto())).replace(".", ""))
									: 
									Long.parseLong( String.format("%1$.2f", BigDecimal.valueOf(((double)lngMontoFormaDePago/100)*tasaoficial.doubleValue())).replace(".", ""));
					if(cuadrar) {					
						lngMontoFormaDePagoEq = lngMontoFormaDePagoEq - montoCuadrar;							
						cuadrar = false;
					}							
	
					lngMontoFormaDePagoEq = lngMontoFormaDePagoEq ;
				}
				//&& =========== Grabar el registro en F0911 
				//Ajustado por LFonseca
				//Fecha: 2020-11-28
				//Se agregaron los parametros de session y trans
				hecho = registrarF0911PMT( session, fechaRecibo, companiaRpkco, tipodocjde, numero_documento, 
						(++lineadoc), numero_batch, ctaBnfcuenta, ctaBnfGmaid, ctaBnfMcu, ctaBnfObj, ctaBnfSub, 
						"AA", moneda_contrato, lngMontoFormaDePago, concepto, vaut.getId().getLogin(),
						vaut.getId().getCodapp(), tasaoficial, tipocliente, observacion, 
						companiaRpkco, codbnf_aux, tipoAuxiliar, moneda_base, companiaRpkco, tipoDocxMon, 
						"V", iClienteCodigo, numeroContrato, moneda_base, lngMontoFormaDePagoEq);
				
				if(!hecho){
					return strMensajeProceso = "Error al grabar el detalle para metodo de pago " + mp.getMetododescrip();
				}
			}
			 

			/* ********************************************************************************************************************* */
			/* *********************** no mandar a generar asientos contables por cambios en moneda local   ************************ */
			
			if(compra_venta_Cambio != null && !compra_venta_Cambio.isEmpty()){
				
				strMensajeProceso = comprantesPorCompraVenta(compra_venta_Cambio, cuentasPorMetodoPago, 
											vaut, iClienteCodigo, unidadNegociosCaja, numero_recibo_fcv, session, unidadNegociosCaja,codigo_sucursal_usuario) ;
				
				if( !(hecho = strMensajeProceso.isEmpty() ) ){
					return strMensajeProceso = "Error al grabar el ajuste por diferencial cambiario por cambio Mixto ";
				}
				System.out.println( "F comprantesPorCompraVenta : " +  new SimpleDateFormat("HH:mm:ss.SSS").format(new Date() ) );
			}
			
			/* **********************************************************************************************************************/
			
			
		} catch (Exception e) {
			strMensajeProceso = "No se ha podido crear el comprobante contable";
			LogCajaService.CreateLog("generarComprobantesContablesPMT", "ERR", e.getMessage());
			e.printStackTrace(); 
		}

		return strMensajeProceso;
	}
	
	public  List<String[]> cuentasPorMetodoPago(){
		List<String[]> dtaCuentaFromQuery = null ;
		
		try {
			
			
			
			String strSqlOr = " (c1id = @CAID and c1rp01 = '@CODCOMP'  and c1crcd = '@MONEDA' and c1ryin = '@FORMADEPAGO') or ";
			
			String strSqlSelect = 
				"select gmmcu ||'.'|| trim(gmobj) ||'.'|| trim(gmsub)   ||'@@@'||  trim(gmaid) ||'@@@'|| "+
						" right( trim( gmco ),  2 ) ||'@@@'|| "+
						" trim(gmmcu) ||'@@@'|| "+
						" trim(gmobj) ||'@@@'|| "+ 
						" trim(gmsub) ||'@@@'|| "+  
						" '"+codigo_compania+"' ||'@@@'|| "+  
						" trim(gmco) "+
						" ||'@@@'|| trim( cast( c1crcd as varchar(3) ccsid 37 ) ) " + 
						" ||'@@@'|| trim( cast( c1ryin as varchar(1) ccsid 37 ) )" + 
						
				" from @BDMCAJA.f55ca011 mp inner join  @BDMCAJA.vf0901 cta on trim(mp.c1mcu) =trim(cta.gmmcu)" +
				"	and trim(c1obj) = trim(cta.gmobj) and trim(mp.c1sub) = trim(cta.gmsub)" +
				" @WHERE"  ;

			String strWhere = " where  ";
			for (MetodosPago mp : formas_de_pago) {
				
				
				strWhere += strSqlOr.replace("@CAID", Integer.toString(codigo_caja))
						.replace("@CODCOMP", codigo_compania)
						.replace("@MONEDA", mp.getMoneda())
						.replace("@FORMADEPAGO", mp.getMetodo() );
				
				if( mp.getMoneda().compareTo(moneda_base) !=0 ){
					strWhere += strSqlOr.replace("@CAID", Integer.toString(codigo_caja))
							.replace("@CODCOMP", codigo_compania)
							.replace("@MONEDA", moneda_base )
							.replace("@FORMADEPAGO", mp.getMetodo() );
				}
				
				if( mp.getMoneda().compareTo(moneda_contrato) !=0 ){
					strWhere += strSqlOr.replace("@CAID", Integer.toString(codigo_caja))
							.replace("@CODCOMP", codigo_compania)
							.replace("@MONEDA", moneda_contrato )
							.replace("@FORMADEPAGO", mp.getMetodo() );
				}
				
			}
			strWhere = strWhere.substring(0, strWhere.lastIndexOf("or"));
			
			strSqlSelect = strSqlSelect.replace("@BDMCAJA", PropertiesSystem.ESQUEMA).replace("@WHERE", strWhere);
			
			@SuppressWarnings("unchecked")
			List<String> dtaQueryExecuted = (ArrayList<String>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSqlSelect, true, null)  ; 
			
			dtaCuentaFromQuery =  new ArrayList<String[]>();
			for (String dtaCta : dtaQueryExecuted) {
				dtaCuentaFromQuery.add(dtaCta.split("@@@"));
			}
			
		} catch (Exception e) {
			LogCajaService.CreateLog("cuentasPorMetodoPago", "ERR", e.getMessage());
			e.printStackTrace(); 
			dtaCuentaFromQuery = null;
		}
		
		return dtaCuentaFromQuery;
		
	}
	
	public  List<String[]> cuentasPorMetodoPago_PV(){
		List<String[]> dtaCuentaFromQuery = null ;
		
		try {
			
			String strSqlOr = " (c1id = @CAID and c1rp01 = '@CODCOMP'  and c1crcd = '@MONEDA' and c1ryin = '@FORMADEPAGO') or ";
			
			String strSqlSelect = 
				"select gmmcu ||'.'|| trim(gmobj) ||'.'|| trim(gmsub)   ||'@@@'||  trim(gmaid) ||'@@@'|| "+
						" right( trim( gmco ),  2 ) ||'@@@'|| "+
						" trim(gmmcu) ||'@@@'|| "+
						" trim(gmobj) ||'@@@'|| "+ 
						" trim(gmsub) ||'@@@'|| "+  
						" '"+codigo_compania+"' ||'@@@'|| "+  
						" trim(gmco) "+
						" ||'@@@'|| trim( cast( c1crcd as varchar(3) ccsid 37 ) ) " + 
						" ||'@@@'|| trim( cast( c1ryin as varchar(1) ccsid 37 ) )" + 
						
				" from @BDMCAJA.f55ca011 mp inner join  @BDMCAJA.vf0901 cta on trim(mp.c1mcu) =trim(cta.gmmcu)" +
				"	and trim(c1obj) = trim(cta.gmobj) and trim(mp.c1sub) = trim(cta.gmsub)" +
				" @WHERE"  ;

			String strWhere = " where  ";
			for (MetodosPago mp : formas_de_pago) {
				
				
				strWhere += strSqlOr.replace("@CAID", Integer.toString(codigo_caja))
						.replace("@CODCOMP", codigo_compania)
						.replace("@MONEDA", moneda_contrato)
						.replace("@FORMADEPAGO", mp.getMetodo() );				
			}
			strWhere = strWhere.substring(0, strWhere.lastIndexOf("or"));
			
			strSqlSelect = strSqlSelect.replace("@BDMCAJA", PropertiesSystem.ESQUEMA).replace("@WHERE", strWhere);
			
			@SuppressWarnings("unchecked")
			List<String> dtaQueryExecuted = (ArrayList<String>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSqlSelect, true, null)  ; 
			
			dtaCuentaFromQuery =  new ArrayList<String[]>();
			for (String dtaCta : dtaQueryExecuted) {
				dtaCuentaFromQuery.add(dtaCta.split("@@@"));
			}
			
		} catch (Exception e) {
			e.printStackTrace(); 
			dtaCuentaFromQuery = null;
		}
		
		return dtaCuentaFromQuery;
		
	}
	
	public  boolean registrarF0911PMT(Session sesion,  Date dtFechaAsiento, 
			String companiadoc, String sTipodoc, int iNoDocumento,
			double dLineaJDE, int iNoBatch, String sCuenta, String sIdCuenta,
			String sCodUnineg, String sCuentaObj, String sCuentaSub,
			String sTipoAsiento, String sMoneda, long iMonto, String sConcepto,
			String sUsuario, String sCodApp, BigDecimal dTasa,
			String sTipoCliente, String sObservacion, String sCodSucCuenta,
			String sGlsbl, String sGlsblt, String sGlbcrc, String sGlhco, String sGlcrrm,  
			String codigotipobatch, int codigobeneficiario, String descrip_factura_arqueo, 
			String sMonedaBase, long iMontoCordoba) {
		
		boolean aplicado = true;
		
		try {
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dtFechaAsiento);
			
			String companiacuenta = CodeUtil.pad(sCodSucCuenta.trim(), 5 , "0");
			
			String hora = new SimpleDateFormat("HHmmss").format(dtFechaAsiento); 
			String fechajulian = String.valueOf( FechasUtil.dateToJulian(dtFechaAsiento) );
		
			if(sConcepto.length() > 30)
				sConcepto = sConcepto.substring(0, 30);
		
			descrip_factura_arqueo = (descrip_factura_arqueo.length() > 25) ?
						descrip_factura_arqueo.substring(0, 25 ) :
						descrip_factura_arqueo ;
						
			String sqlInsert = "";
			String insertFields = "";
			String insertValues = "";
			
			//--------------------------------------------
			//Ajuste hecho por Luis Fonseca,
			//
			if(sMoneda.compareTo(sMonedaBase) == 0)
			{
				sqlInsert = "insert into @JDEDTA.F0911 ( @FIELDS_TO_INSERT ) VALUES (@VALUES_TO_INSERT) " ;
				
				insertFields = DefaultJdeFieldsValues.F0911_CONTADO_INSERT_COLUMNS;
				insertValues = DefaultJdeFieldsValues.F0911_CONTADO_INSERT_COLUMNS_VALUES;
				
				insertFields += ", " +  DefaultJdeFieldsValues.F0911_CONTADO_COLUMN_NAMES_DEFAULT;
				insertValues += ", " +  DefaultJdeFieldsValues.F0911_CONTADO_COLUMN_NAMES_DEFAULT_VALUES;			
				
				sCodUnineg = CodeUtil.pad(sCodUnineg.trim(), 12, " ");
				
				insertValues =  insertValues
					.replace("@GLKCO@", companiadoc )
					.replace("@GLDCT@", sTipodoc.trim() )
					.replace("@GLDOC@", Integer.toString(iNoDocumento) )
					.replace("@GLDGJ@", fechajulian)
					.replace("@GLJELN@", Double.toString( dLineaJDE ) )
					.replace("@GLICU@",  Integer.toString( iNoBatch ) )
					.replace("@GLICUT@", codigotipobatch)
					.replace("@GLDICJ@", fechajulian )
					.replace("@GLDSYJ@", fechajulian )
					.replace("@GLTICU@", hora )
					.replace("@GLCO@", companiacuenta)
					.replace("@GLANI@", sCuenta )
					.replace("@GLAM@", "2" )
					.replace("@GLAID@", sIdCuenta )
					.replace("@GLMCU@", sCodUnineg )
					.replace("@GLOBJ@", sCuentaObj )
					.replace("@GLSUB@", sCuentaSub)
					.replace("@GLSBL@", sGlsbl )
					.replace("@GLSBLT@", sGlsblt )
					.replace("@GLLT@", sTipoAsiento )  //AA o CA en dependencia
					.replace("@GLPN@", Integer.toString( calendar.get(Calendar.MONTH) + 1 ))
					.replace("@GLCTRY@", Integer.toString(calendar.get(Calendar.YEAR) / 100))
					.replace("@GLFY@", Integer.toString(calendar.get(Calendar.YEAR) % 100))
					.replace("@GLCRCD@", sMoneda )
					.replace("@GLAA@",  Long.toString(iMontoCordoba) ) //.replace("@GLAA@",  Long.toString(iMonto) )
					.replace("@GLEXA@",  sConcepto )
					.replace("@GLDKJ@", fechajulian)
					.replace("@GLDSVJ@", fechajulian )
					.replace("@GLTORG@", sUsuario )
					.replace("@GLUSER@", sUsuario )
					.replace("@GLPID@",  PropertiesSystem.ESQUEMA )
					.replace("@GLJOBN@", PropertiesSystem.ESQUEMA )
					.replace("@GLUPMJ@", fechajulian )
					.replace("@GLUPMT@", hora )
					.replace("@GLCRR@", dTasa.toString())
					.replace("@GLGLC@", sTipoCliente)
					.replace("@GLEXR@", sObservacion)
					
					.replace("@GLBCRC@", moneda_base)
					.replace("@GLHCO@", sGlhco)
					.replace("@GLCRRM@",  "D")
					
					.replace("@GLAN8@", Integer.toString(codigobeneficiario) )
					.replace("@GLVINV@", descrip_factura_arqueo )
					.replace("@GLIVD@", fechajulian )
					.replace("@GLPKCO@", "00000")
					.replace("@GLACR@", "0") ;
				
				sqlInsert = sqlInsert
						.replace("@JDEDTA", PropertiesSystem.JDEDTA)
						.replace("@FIELDS_TO_INSERT", insertFields)
						.replace("@VALUES_TO_INSERT", insertValues);
					
					
				try {
					//Ajuste Hecho por LFonseca
					//Fecha: 2020-11-28
					//Se envia por medio de parametros la session para el manejo de control de compromiso
					aplicado = ConsolidadoDepositosBcoCtrl .executeSqlQueryTx(  sesion, sqlInsert );
				} catch (Exception e) {
					e.printStackTrace();
					aplicado = false;
				}
			
			}
			else
			{
				//Cordobas
				sqlInsert = "insert into @JDEDTA.F0911 ( @FIELDS_TO_INSERT ) VALUES (@VALUES_TO_INSERT) " ;
				
				insertFields = DefaultJdeFieldsValues.F0911_CONTADO_INSERT_COLUMNS;
				insertValues = DefaultJdeFieldsValues.F0911_CONTADO_INSERT_COLUMNS_VALUES;
				
				insertFields += ", " +  DefaultJdeFieldsValues.F0911_CONTADO_COLUMN_NAMES_DEFAULT;
				insertValues += ", " +  DefaultJdeFieldsValues.F0911_CONTADO_COLUMN_NAMES_DEFAULT_VALUES;			
				
				sCodUnineg = CodeUtil.pad(sCodUnineg.trim(), 12, " ");
				
				insertValues =  insertValues
					.replace("@GLKCO@", companiadoc )
					.replace("@GLDCT@", sTipodoc.trim() )
					.replace("@GLDOC@", Integer.toString(iNoDocumento) )
					.replace("@GLDGJ@", fechajulian)
					.replace("@GLJELN@", Double.toString( dLineaJDE ) )
					.replace("@GLICU@",  Integer.toString( iNoBatch ) )
					.replace("@GLICUT@", codigotipobatch)
					.replace("@GLDICJ@", fechajulian )
					.replace("@GLDSYJ@", fechajulian )
					.replace("@GLTICU@", hora )
					.replace("@GLCO@", companiacuenta)
					.replace("@GLANI@", sCuenta )
					.replace("@GLAM@", "2" )
					.replace("@GLAID@", sIdCuenta )
					.replace("@GLMCU@", sCodUnineg )
					.replace("@GLOBJ@", sCuentaObj )
					.replace("@GLSUB@", sCuentaSub)
					.replace("@GLSBL@", sGlsbl )
					.replace("@GLSBLT@", sGlsblt )
					.replace("@GLLT@", sTipoAsiento )  //AA o CA en dependencia
					.replace("@GLPN@", Integer.toString( calendar.get(Calendar.MONTH) + 1 ))
					.replace("@GLCTRY@", Integer.toString(calendar.get(Calendar.YEAR) / 100))
					.replace("@GLFY@", Integer.toString(calendar.get(Calendar.YEAR) % 100))
					.replace("@GLCRCD@", sMoneda )
					.replace("@GLAA@",  Long.toString(iMontoCordoba) )
					.replace("@GLEXA@",  sConcepto )
					.replace("@GLDKJ@", fechajulian)
					.replace("@GLDSVJ@", fechajulian )
					.replace("@GLTORG@", sUsuario )
					.replace("@GLUSER@", sUsuario )
					.replace("@GLPID@",  PropertiesSystem.ESQUEMA )
					.replace("@GLJOBN@", PropertiesSystem.ESQUEMA )
					.replace("@GLUPMJ@", fechajulian )
					.replace("@GLUPMT@", hora )
					.replace("@GLCRR@", dTasa.toString())
					.replace("@GLGLC@", sTipoCliente)
					.replace("@GLEXR@", sObservacion)
					
					.replace("@GLBCRC@", moneda_base) //sGlbcrc)
					.replace("@GLHCO@", sGlhco)
					.replace("@GLCRRM@", "F")
					
					.replace("@GLAN8@", Integer.toString(codigobeneficiario) )
					.replace("@GLVINV@", descrip_factura_arqueo )
					.replace("@GLIVD@", fechajulian )
					.replace("@GLPKCO@", "00000")
					.replace("@GLACR@", Long.toString(iMonto)) ;
				
				sqlInsert = sqlInsert
						.replace("@JDEDTA", PropertiesSystem.JDEDTA)
						.replace("@FIELDS_TO_INSERT", insertFields)
						.replace("@VALUES_TO_INSERT", insertValues);
				
				try {
					aplicado = ConsolidadoDepositosBcoCtrl .executeSqlQueryTx( sesion,  sqlInsert );
				} catch (Exception e) {
					e.printStackTrace();
					aplicado = false;
				}
				
				//Dolares
				if(aplicado==true)
				{
					sqlInsert = "insert into @JDEDTA.F0911 ( @FIELDS_TO_INSERT ) VALUES (@VALUES_TO_INSERT) " ;
					
					insertFields = DefaultJdeFieldsValues.F0911_CONTADO_INSERT_COLUMNS;
					insertValues = DefaultJdeFieldsValues.F0911_CONTADO_INSERT_COLUMNS_VALUES;
					
					insertFields += ", " +  DefaultJdeFieldsValues.F0911_CONTADO_COLUMN_NAMES_DEFAULT;
					insertValues += ", " +  DefaultJdeFieldsValues.F0911_CONTADO_COLUMN_NAMES_DEFAULT_VALUES;			
					
					sCodUnineg = CodeUtil.pad(sCodUnineg.trim(), 12, " ");
					
					insertValues =  insertValues
						.replace("@GLKCO@", companiadoc )
						.replace("@GLDCT@", sTipodoc.trim() )
						.replace("@GLDOC@", Integer.toString(iNoDocumento) )
						.replace("@GLDGJ@", fechajulian)
						.replace("@GLJELN@", Double.toString( dLineaJDE ) )
						.replace("@GLICU@",  Integer.toString( iNoBatch ) )
						.replace("@GLICUT@", codigotipobatch)
						.replace("@GLDICJ@", fechajulian )
						.replace("@GLDSYJ@", fechajulian )
						.replace("@GLTICU@", hora )
						.replace("@GLCO@", companiacuenta)
						.replace("@GLANI@", sCuenta )
						.replace("@GLAM@", "2" )
						.replace("@GLAID@", sIdCuenta )
						.replace("@GLMCU@", sCodUnineg )
						.replace("@GLOBJ@", sCuentaObj )
						.replace("@GLSUB@", sCuentaSub)
						.replace("@GLSBL@", sGlsbl )
						.replace("@GLSBLT@", sGlsblt )
						.replace("@GLLT@", "CA" )  //AA o CA en dependencia
						.replace("@GLPN@", Integer.toString( calendar.get(Calendar.MONTH) + 1 ))
						.replace("@GLCTRY@", Integer.toString(calendar.get(Calendar.YEAR) / 100))
						.replace("@GLFY@", Integer.toString(calendar.get(Calendar.YEAR) % 100))
						.replace("@GLCRCD@", sMoneda )
						.replace("@GLAA@",  Long.toString(iMonto) )
						.replace("@GLEXA@",  sConcepto )
						.replace("@GLDKJ@", fechajulian)
						.replace("@GLDSVJ@", fechajulian )
						.replace("@GLTORG@", sUsuario )
						.replace("@GLUSER@", sUsuario )
						.replace("@GLPID@",  PropertiesSystem.ESQUEMA )
						.replace("@GLJOBN@", PropertiesSystem.ESQUEMA )
						.replace("@GLUPMJ@", fechajulian )
						.replace("@GLUPMT@", hora )
						.replace("@GLCRR@", dTasa.toString())
						.replace("@GLGLC@", sTipoCliente)
						.replace("@GLEXR@", sObservacion)
						
						.replace("@GLBCRC@", moneda_base) //sGlbcrc)
						.replace("@GLHCO@", sGlhco)
						.replace("@GLCRRM@", "F")
						
						.replace("@GLAN8@", Integer.toString(codigobeneficiario) )
						.replace("@GLVINV@", descrip_factura_arqueo )
						.replace("@GLIVD@", fechajulian )
						.replace("@GLPKCO@", "00000")
						.replace("@GLACR@", "0") ;
					
					sqlInsert = sqlInsert
							.replace("@JDEDTA", PropertiesSystem.JDEDTA)
							.replace("@FIELDS_TO_INSERT", insertFields)
							.replace("@VALUES_TO_INSERT", insertValues);
						
					try {
						aplicado = ConsolidadoDepositosBcoCtrl .executeSqlQueryTx( sesion,   sqlInsert );
					} catch (Exception e) {
						e.printStackTrace();
						aplicado = false;
					}
				}
			}
		} catch (Exception ex) {
			aplicado = false;
			ex.printStackTrace(); 
		}
		return aplicado;
	}
	
	public  boolean registrarF0911PMT(Date dtFechaAsiento, 
			String companiadoc, String sTipodoc, int iNoDocumento,
			double dLineaJDE, int iNoBatch, String sCuenta, String sIdCuenta,
			String sCodUnineg, String sCuentaObj, String sCuentaSub,
			String sTipoAsiento, String sMoneda, long iMonto, String sConcepto,
			String sUsuario, String sCodApp, BigDecimal dTasa,
			String sTipoCliente, String sObservacion, String sCodSucCuenta,
			String sGlsbl, String sGlsblt, String sGlbcrc, String sGlhco, String sGlcrrm,  
			String codigotipobatch, int codigobeneficiario, String descrip_factura_arqueo, Session session) {
		
		boolean aplicado = true;
		
		try {
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dtFechaAsiento);
			
			String companiacuenta = CodeUtil.pad(sCodSucCuenta.trim(), 5 , "0");
			
			String hora = new SimpleDateFormat("HHmmss").format(dtFechaAsiento); 
			String fechajulian = String.valueOf( FechasUtil.dateToJulian(dtFechaAsiento) );
		
			if(sConcepto.length() > 30)
				sConcepto = sConcepto.substring(0, 30);
		
			descrip_factura_arqueo = (descrip_factura_arqueo.length() > 25) ?
						descrip_factura_arqueo.substring(0, 25 ) :
						descrip_factura_arqueo ;
						
			String sqlInsert = "insert into @JDEDTA.F0911 ( @FIELDS_TO_INSERT ) VALUES (@VALUES_TO_INSERT) " ;
			
			String insertFields = DefaultJdeFieldsValues.F0911_CONTADO_INSERT_COLUMNS;
			String insertValues = DefaultJdeFieldsValues.F0911_CONTADO_INSERT_COLUMNS_VALUES;
			
			insertFields += ", " +  DefaultJdeFieldsValues.F0911_CONTADO_COLUMN_NAMES_DEFAULT;
			insertValues += ", " +  DefaultJdeFieldsValues.F0911_CONTADO_COLUMN_NAMES_DEFAULT_VALUES;			

			sCodUnineg = CodeUtil.pad(sCodUnineg.trim(), 12, " ");
			
			insertValues =  insertValues
				.replace("@GLKCO@", companiadoc )
				.replace("@GLDCT@", sTipodoc.trim() )
				.replace("@GLDOC@", Integer.toString(iNoDocumento) )
				.replace("@GLDGJ@", fechajulian)
				.replace("@GLJELN@", Double.toString( dLineaJDE ) )
				.replace("@GLICU@",  Integer.toString( iNoBatch ) )
				.replace("@GLICUT@", codigotipobatch)
				.replace("@GLDICJ@", fechajulian )
				.replace("@GLDSYJ@", fechajulian )
				.replace("@GLTICU@", hora )
				.replace("@GLCO@", companiacuenta)
				.replace("@GLANI@", sCuenta )
				.replace("@GLAM@", "2" )
				.replace("@GLAID@", sIdCuenta )
				.replace("@GLMCU@", sCodUnineg )
				.replace("@GLOBJ@", sCuentaObj )
				.replace("@GLSUB@", sCuentaSub)
				.replace("@GLSBL@", sGlsbl )
				.replace("@GLSBLT@", sGlsblt )
				.replace("@GLLT@", sTipoAsiento )
				.replace("@GLPN@", Integer.toString( calendar.get(Calendar.MONTH) + 1 ))
				.replace("@GLCTRY@", Integer.toString(calendar.get(Calendar.YEAR) / 100))
				.replace("@GLFY@", Integer.toString(calendar.get(Calendar.YEAR) % 100))
				.replace("@GLCRCD@", sMoneda )
				.replace("@GLAA@",  Long.toString(iMonto) )
				.replace("@GLEXA@",  sConcepto )
				.replace("@GLDKJ@", fechajulian)
				.replace("@GLDSVJ@", fechajulian )
				.replace("@GLTORG@", sUsuario )
				.replace("@GLUSER@", sUsuario )
				.replace("@GLPID@",  PropertiesSystem.ESQUEMA )
				.replace("@GLJOBN@", PropertiesSystem.ESQUEMA )
				.replace("@GLUPMJ@", fechajulian )
				.replace("@GLUPMT@", hora )
				.replace("@GLCRR@", dTasa.toString())
				.replace("@GLGLC@", sTipoCliente)
				.replace("@GLEXR@", sObservacion)
				
				.replace("@GLBCRC@", sGlbcrc)
				.replace("@GLHCO@", sGlhco)
				.replace("@GLCRRM@", sGlcrrm)
				
				.replace("@GLAN8@", Integer.toString(codigobeneficiario) )
				.replace("@GLVINV@", descrip_factura_arqueo )
				.replace("@GLIVD@", fechajulian )
				.replace("@GLPKCO@", "00000")
				.replace("@GLACR@", "0") ;
			
			sqlInsert = sqlInsert
					.replace("@JDEDTA", PropertiesSystem.JDEDTA)
					.replace("@FIELDS_TO_INSERT", insertFields)
					.replace("@VALUES_TO_INSERT", insertValues);
				
			try {
				
				int rows = session.createSQLQuery(sqlInsert).executeUpdate();
				aplicado = rows == 1 ;
				
			} catch (Exception e) {
				e.printStackTrace();
				aplicado = false;
			}
			
		} catch (Exception ex) {
			aplicado = false;
			ex.printStackTrace(); 
		}
		return aplicado;
	}
	
	public  boolean insertConfigProveedor(Session sesion, int codigoCliente, String usuario, String newApClass ){
		boolean insert = true;
		
		try {
			
			String sqlInsert = 
			" INSERT INTO @JDEDTA.F0401 " +
			"(A6AN8, A6APC, A6DCAP, A6TAWH, A6PCWH, A6SCK, A6SNTO, A6FLD, A6SQNL, A6CRCA, A6AYPD, A6APPD, A6ABAM, " +
			"A6ABA1, A6APRC, A6MINO, A6MAXO, A6AN8R, A6BADT, A6ANCR, A6CARS, A6LTDT, A6INVC, A6PLST, A6EDPM, A6EDQD, " +
			"A6EDAD, A6EDF1, A6MNSC, A6ATO, A6RVNT, A6URDT, A6URAT, A6URAB, A6USER, A6PID, A6JOBN, A6UPMJ, A6UPMT, A6AVCH, A6PYIN " +
			" ) " +
			"VALUES " +
			"(@A6AN8C, '@A6APC', @A6DCAP, @A6TAWH, @A6PCWH, '@A6SCK', @A6SNTO, @A6FLD, @A6SQNL, '@A6CRCA', @A6AYPD, @A6APPD, @A6ABAM, " +
			"@A6ABA1, @A6APRC, @A6MINO, @A6MAXO, @A6AN8R, '@A6BADT', @A6ANCR, @A6CARS, @A6LTDT, @A6INVC, '@A6PLST', '@A6EDPM', @A6EDQD, " +
			"@A6EDAD, '@A6EDF1', @A6MNSC, '@A6ATO', '@A6RVNT', @A6URDT, @A6URAT, @A6URAB, '@A6USER', '@A6PID', '@A6JOBN', @A6UPMJ, @A6UPMT, '@A6AVCH', '@A6PYIN' " +
			" ) ";
			
			sqlInsert = sqlInsert
					
			.replace("@JDEDTA", PropertiesSystem.JDEDTA )
			.replace("@A6AN8C", Integer.toString(codigoCliente) )
			.replace("@A6APC", newApClass )
			.replace("@A6DCAP", "0" )
			.replace("@A6TAWH", "0" )
			.replace("@A6PCWH", "0" )
			.replace("@A6SCK",  "N" )
			.replace("@A6SNTO", "0" )
			.replace("@A6FLD", "0" )
			.replace("@A6SQNL", "6" )
			.replace("@A6CRCA", "COR" )
			.replace("@A6AYPD", "0" )
			.replace("@A6APPD", "0" )
			.replace("@A6ABAM", "0" )
			.replace("@A6ABA1", "0" )
			.replace("@A6APRC", "0" )
			.replace("@A6MINO", "0" )
			.replace("@A6MAXO", "0" )
			.replace("@A6AN8R", "0" )
			.replace("@A6BADT", "X" )
			.replace("@A6ANCR", "0" )
			.replace("@A6CARS", "0" )
			.replace("@A6LTDT", "0" )
			.replace("@A6INVC", "0" )
			.replace("@A6PLST", "Y" )
			.replace("@A6EDPM", "I" )
			.replace("@A6EDQD", "0")
			.replace("@A6EDAD", "0" )
			.replace("@A6EDF1", "N" )
			.replace("@A6MNSC", "1" )
			.replace("@A6ATO", "N" )
			.replace("@A6RVNT", "N" )
			.replace("@A6URDT", "0" )
			.replace("@A6URAT", "0" )
			.replace("@A6URAB", "0" )
			.replace("@A6USER", usuario )
			.replace("@A6PID", PropertiesSystem.CONTEXT_NAME)
			.replace("@A6JOBN", PropertiesSystem.CONTEXT_NAME  )
			.replace("@A6UPMJ", Integer.toString( FechasUtil.dateToJulian(new Date() ) ) )
			.replace("@A6UPMT", new SimpleDateFormat("HHmmss").format(new Date() ) )
			.replace("@A6AVCH", "N" )
			.replace("@A6PYIN", MetodosPagoCtrl.TRANSFERENCIA );
			
			try {
				insert = ConsolidadoDepositosBcoCtrl .executeSqlQueryTx( sesion,  sqlInsert );
			} catch (Exception e) {
				e.printStackTrace();
				insert = false;
			}
			
		} catch (Exception e) {
			e.printStackTrace(); 
			insert = false;
		}
		return insert;
	}
	public  boolean insertConfigProveedor(int codigoCliente, String usuario, String newApClass, Session session ){
		boolean insert = true;
		
		try {
			
			String sqlInsert = 
			" INSERT INTO @JDEDTA.F0401 " +
			"(A6AN8, A6APC, A6DCAP, A6TAWH, A6PCWH, A6SCK, A6SNTO, A6FLD, A6SQNL, A6CRCA, A6AYPD, A6APPD, A6ABAM, " +
			"A6ABA1, A6APRC, A6MINO, A6MAXO, A6AN8R, A6BADT, A6ANCR, A6CARS, A6LTDT, A6INVC, A6PLST, A6EDPM, A6EDQD, " +
			"A6EDAD, A6EDF1, A6MNSC, A6ATO, A6RVNT, A6URDT, A6URAT, A6URAB, A6USER, A6PID, A6JOBN, A6UPMJ, A6UPMT, A6AVCH, A6PYIN " +
			" ) " +
			"VALUES " +
			"(@A6AN8C, '@A6APC', @A6DCAP, @A6TAWH, @A6PCWH, '@A6SCK', @A6SNTO, @A6FLD, @A6SQNL, '@A6CRCA', @A6AYPD, @A6APPD, @A6ABAM, " +
			"@A6ABA1, @A6APRC, @A6MINO, @A6MAXO, @A6AN8R, '@A6BADT', @A6ANCR, @A6CARS, @A6LTDT, @A6INVC, '@A6PLST', '@A6EDPM', @A6EDQD, " +
			"@A6EDAD, '@A6EDF1', @A6MNSC, '@A6ATO', '@A6RVNT', @A6URDT, @A6URAT, @A6URAB, '@A6USER', '@A6PID', '@A6JOBN', @A6UPMJ, @A6UPMT, '@A6AVCH', '@A6PYIN' " +
			" ) ";
			
			sqlInsert = sqlInsert
					
			.replace("@JDEDTA", PropertiesSystem.JDEDTA )
			.replace("@A6AN8C", Integer.toString(codigoCliente) )
			.replace("@A6APC", newApClass )
			.replace("@A6DCAP", "0" )
			.replace("@A6TAWH", "0" )
			.replace("@A6PCWH", "0" )
			.replace("@A6SCK",  "N" )
			.replace("@A6SNTO", "0" )
			.replace("@A6FLD", "0" )
			.replace("@A6SQNL", "6" )
			.replace("@A6CRCA", "COR" )
			.replace("@A6AYPD", "0" )
			.replace("@A6APPD", "0" )
			.replace("@A6ABAM", "0" )
			.replace("@A6ABA1", "0" )
			.replace("@A6APRC", "0" )
			.replace("@A6MINO", "0" )
			.replace("@A6MAXO", "0" )
			.replace("@A6AN8R", "0" )
			.replace("@A6BADT", "X" )
			.replace("@A6ANCR", "0" )
			.replace("@A6CARS", "0" )
			.replace("@A6LTDT", "0" )
			.replace("@A6INVC", "0" )
			.replace("@A6PLST", "Y" )
			.replace("@A6EDPM", "I" )
			.replace("@A6EDQD", "0")
			.replace("@A6EDAD", "0" )
			.replace("@A6EDF1", "N" )
			.replace("@A6MNSC", "1" )
			.replace("@A6ATO", "N" )
			.replace("@A6RVNT", "N" )
			.replace("@A6URDT", "0" )
			.replace("@A6URAT", "0" )
			.replace("@A6URAB", "0" )
			.replace("@A6USER", usuario )
			.replace("@A6PID", PropertiesSystem.CONTEXT_NAME)
			.replace("@A6JOBN", PropertiesSystem.CONTEXT_NAME  )
			.replace("@A6UPMJ", Integer.toString( FechasUtil.dateToJulian(new Date() ) ) )
			.replace("@A6UPMT", new SimpleDateFormat("HHmmss").format(new Date() ) )
			.replace("@A6AVCH", "N" )
			.replace("@A6PYIN", MetodosPagoCtrl.TRANSFERENCIA )
			
			;
			
			try {
				
				int rows = session.createSQLQuery(sqlInsert).executeUpdate();
				insert = rows == 1 ;				
			} catch (Exception e) {
				e.printStackTrace();
				insert = false;
			}
			
		} catch (Exception e) {
			e.printStackTrace(); 
			insert = false;
		}
		return insert;
	}
	
	@SuppressWarnings("unchecked")
	public  boolean registrarF0411PorPMT(Session sesion,  String companiaRpkco, String companiaRpco,
			int nodocumento, String tipodocumento, int codigobeneficiario,
			Date fechaArqueo, int nobatch, String moneda, String monedaBase,
			BigDecimal monto, BigDecimal tasaCambio, int idcuenta,
			String unidadNegocio, String ctaobj, String ctaSub, 
			String numfactura_descrip, String usrA400, String observacionpago,
			String numeroContrato, String tipoDocLiga, String payStatusCode, String newApClass) {
		
		boolean aplicado = true;
		
		try{

			numfactura_descrip = (numfactura_descrip.length() > 25) ? numfactura_descrip.substring(0, 25 ) : numfactura_descrip ;
			observacionpago    = (observacionpago.length() > 30) ?  observacionpago.substring(0, 30 ) : observacionpago ;
			
			String numerocuota = "001" ;
			String strFechaActualJuliana = String.valueOf( FechasUtil.dateToJulian(fechaArqueo) );
				
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(fechaArqueo);
			
			String hora = new SimpleDateFormat("HHmmss").format(fechaArqueo);
			
			int montoDomestico = 0;
			int montoForaneo = 0;
			String currencyMode;
			String paymentInstrument ="";
			String glapclass = "";
			
			String strF0101 = "select " +
				"IFNULL( (cast( abac01  as varchar(3) ccsid 37 )),'') ," +
				"IFNULL( (cast( abac02  as varchar(3) ccsid 37 )),'') ," +
				"IFNULL( (cast( abac03  as varchar(3) ccsid 37 )),'') ," +
				"IFNULL( (cast( abac04  as varchar(3) ccsid 37 )),'') ," +
				"IFNULL( (cast( abac05  as varchar(3) ccsid 37 )),'') ," +
				"IFNULL( (cast( abac06  as varchar(3) ccsid 37 )),'') ," +
				"IFNULL( (cast( abac07  as varchar(3) ccsid 37 )),'') ," +
				"IFNULL( (cast( abac08  as varchar(3) ccsid 37 )),'') ," +
				"IFNULL( (cast( abac09  as varchar(3) ccsid 37 )),'') ," +
				"IFNULL( (cast( abac10 as varchar(3) ccsid 37 )),'') " +
				" from "+PropertiesSystem.JDEDTA+".f0101 where aban8 = " + codigobeneficiario;
			
			List<Object[]>dtaF0101 = (ArrayList<Object[]>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(strF0101, true,null );
			
			if(dtaF0101 == null || dtaF0101.isEmpty() ){
				System.out.println("strF0101 is null " + strF0101);
				return aplicado = false;
			}
			
			String rpyc01 = String.valueOf( dtaF0101.get(0)[0] ) ;
			String rpyc02 = String.valueOf( dtaF0101.get(0)[1] ) ;
			String rpac03 = String.valueOf( dtaF0101.get(0)[2] ) ;
			String rpyc04 = String.valueOf( dtaF0101.get(0)[3] ) ;
			String rpyc05 = String.valueOf( dtaF0101.get(0)[4] ) ;
			String rpac06 = String.valueOf( dtaF0101.get(0)[5] ) ;
			String rpyc07 = String.valueOf( dtaF0101.get(0)[6] ) ;
			String rpyc08 = String.valueOf( dtaF0101.get(0)[7] ) ;
			String rpac09 = String.valueOf( dtaF0101.get(0)[8] ) ;
			String rpac10 = String.valueOf( dtaF0101.get(0)[9] ) ;
			
			if( moneda.compareTo(monedaBase) == 0){
				tasaCambio = BigDecimal.ZERO;
				currencyMode = "D" ;
				montoDomestico = Integer.parseInt( String.format("%1$.2f", monto).replace(".", "")  ); 
			}else{
				currencyMode = "F" ;
				montoForaneo = Integer.parseInt( String.format("%1$.2f", monto).replace(".", "")  ); 
				montoDomestico = Integer.parseInt( String.format("%1$.2f", monto.multiply(tasaCambio) ).replace(".", "")  ); 
			}
			
			String strSqlPayInstruent = "select " +
					"IFNULL( '"+newApClass+"', '"+newApClass+"' ) a6apc, " +
					"IFNULL( (cast( a6pyin as varchar(1) ccsid 37 )), '"+MetodosPagoCtrl.TRANSFERENCIA+"' )  a6pyin from " +
					PropertiesSystem.JDEDTA+".f0401 where a6an8 = " + codigobeneficiario ;

			List<Object[]> dtaBnfF0401 = ( ArrayList<Object[]> )ConsolidadoDepositosBcoCtrl.executeSqlQuery( strSqlPayInstruent, true,null ) ;
			
			//&& =============== crear la configuracion de proveedor al cliente si no existe 
			if(dtaBnfF0401 == null || dtaBnfF0401.isEmpty() ){
				
				insertConfigProveedor( sesion,    codigobeneficiario, usrA400, newApClass );
				
				System.out.println("no hay configuracion en F0401 strSqlPayInstruent  "+strSqlPayInstruent);
				
				glapclass = newApClass;
				paymentInstrument = MetodosPagoCtrl.TRANSFERENCIA;
				
			} else{
				glapclass = String.valueOf( dtaBnfF0401.get(0)[0] ) ;
				paymentInstrument = String.valueOf( dtaBnfF0401.get(0)[1] ) ;
			}
			
			
			String strSqlInsertF0411 = "insert into @JDEDTA.F0411 ( @FIELDS_TO_INSERT ) VALUES (@VALUES_TO_INSERT) " ;
			
			String insertFields = DefaultJdeFieldsValues.F0411_INSERT_COLUMNS;
			String insertValues = DefaultJdeFieldsValues.F0411_INSERT_COLUMNS_VALUES;
			
			insertFields += ", " +  DefaultJdeFieldsValues.F0411_COLUMN_NAMES_DEFAULT;
			insertValues += ", " +  DefaultJdeFieldsValues.F0411_COLUMN_NAMES_DEFAULT_VALUES;
			
			insertValues = 	insertValues
				 .replace("@RPKCO@",  companiaRpkco) 
				 .replace("@RPDOC@",  Integer.toString(nodocumento)) 
				 .replace("@RPDCT@",  tipodocumento)  
				 .replace("@RPSFX@",  numerocuota)  
				 .replace("@RPAN8@",  Integer.toString(codigobeneficiario))
				 .replace("@RPPYE@",  Integer.toString(codigobeneficiario))
				 .replace("@RPDIVJ@", strFechaActualJuliana)
				 .replace("@RPDSVJ@", strFechaActualJuliana)
				 .replace("@RPDDJ@",  strFechaActualJuliana)
				 .replace("@RPDDNJ@", strFechaActualJuliana)
				 .replace("@RPDGJ@",  strFechaActualJuliana)
				 .replace("@RPFY@",   Integer.toString(calendar.get(Calendar.YEAR) % 100))
				 .replace("@RPCTRY@", Integer.toString(calendar.get(Calendar.YEAR) / 100))
				 .replace("@RPPN@" ,  Integer.toString( calendar.get(Calendar.MONTH) + 1 ))
				 .replace("@RPCO@",   companiaRpco)  
				 .replace("@RPICU@",  Integer.toString(nobatch))
				 .replace("@RPICUT@", valoresJDEInsPMT[8])
				 .replace("@RPDICJ@", strFechaActualJuliana)  
				 .replace("@RPBALJ@", "Y") 
				 .replace("@RPPST@",  payStatusCode)  
				 .replace("@RPAG@",   Integer.toString(montoDomestico))
				 .replace("@RPAAP@",  Integer.toString(montoDomestico))
				 .replace("@RPATXN@", Integer.toString(montoDomestico))
				 .replace("@RPTXA1@", "EXE")  
				 .replace("@RPEXR1@", "E")  
				 .replace("@RPCRRM@", currencyMode)  
				 .replace("@RPCRCD@", moneda)  
				 .replace("@RPCRR@",  tasaCambio.toString() )  
				 .replace("@RPACR@",  Integer.toString(montoForaneo))
				 .replace("@RPFAP@",  Integer.toString(montoForaneo))
				 .replace("@RPCTXN@", Integer.toString(montoForaneo))
				 .replace("@RPGLC@",  glapclass)  
				 .replace("@RPGLBA@", Integer.toString(idcuenta))
				 .replace("@RPAM@",   "2")
				 .replace("@RPMCU@",  unidadNegocio)
				 .replace("@RPOBJ@",  ctaobj)
				 .replace("@RPSUB@",  ctaSub)
				 .replace("@RPVINV@", numfactura_descrip )  
				 .replace("@RPPYIN@", paymentInstrument )  
				 .replace("@RPAC07@", rpyc07)  
				 .replace("@RPBCRC@", monedaBase)  
				 
				 .replace("@RPYC01@", rpyc01)
				 .replace("@RPYC02@", rpyc02)
				 .replace("@RPYC03@", rpac03)
				 .replace("@RPYC04@", rpyc04)
				 .replace("@RPYC05@", rpyc05)
				 .replace("@RPYC06@", rpac06)
				 .replace("@RPYC07@", rpyc07)
				 .replace("@RPYC08@", rpyc08)
				 .replace("@RPYC09@", rpac09)
				 .replace("@RPYC10@", rpac10)
 
				 .replace("@RPTORG@", usrA400)  
				 .replace("@RPUSER@", usrA400)  
				 .replace("@RPPID@",  PropertiesSystem.ESQUEMA )  
				 .replace("@RPUPMJ@", strFechaActualJuliana)  
				 .replace("@RPUPMT@", hora)  
				 .replace("@RPJOBN@", PropertiesSystem.ESQUEMA)  
				 .replace("@RPRMK@",  observacionpago)
				 .replace("@RPPO@", CodeUtil.pad(numeroContrato, 8, "0"))
				.replace("@RPPDCT@", " ");			
			
				strSqlInsertF0411 = strSqlInsertF0411
						.replace("@JDEDTA", PropertiesSystem.JDEDTA)
						.replace("@FIELDS_TO_INSERT", insertFields)
						.replace("@VALUES_TO_INSERT", insertValues);
				
				try {
					aplicado = ConsolidadoDepositosBcoCtrl .executeSqlQueryTx( sesion,    strSqlInsertF0411 );
				} catch (Exception e) {
					e.printStackTrace();
					aplicado = false;
				}
				
		} catch (Exception e) {
			aplicado = false;
			e.printStackTrace(); 
		} 
		return aplicado;
	}
	
	@SuppressWarnings("unchecked")
	public  boolean registrarF0411PorPMT(String companiaRpkco, String companiaRpco,
			int nodocumento, String tipodocumento, int codigobeneficiario,
			Date fechaArqueo, int nobatch, String moneda, String monedaBase,
			BigDecimal monto, BigDecimal tasaCambio, int idcuenta,
			String unidadNegocio, String ctaobj, String ctaSub, 
			String numfactura_descrip, String usrA400, String observacionpago,
			String numeroContrato, String tipoDocLiga, String payStatusCode, String newApClass, 
			Session session) {
		
		boolean aplicado = true;
		
		try{

			numfactura_descrip = (numfactura_descrip.length() > 25) ? numfactura_descrip.substring(0, 25 ) : numfactura_descrip ;
			observacionpago    = (observacionpago.length() > 30) ?  observacionpago.substring(0, 30 ) : observacionpago ;
			
			String numerocuota = "001" ;
			String strFechaActualJuliana = String.valueOf( FechasUtil.dateToJulian(fechaArqueo) );
				
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(fechaArqueo);
			
			String hora = new SimpleDateFormat("HHmmss").format(fechaArqueo);
			
			int montoDomestico = 0;
			int montoForaneo = 0;
			String currencyMode;
			String paymentInstrument ="";
			String glapclass = "";
			
			String strF0101 = "select " +
				"IFNULL( (cast( abac01  as varchar(3) ccsid 37 )),'') ," +
				"IFNULL( (cast( abac02  as varchar(3) ccsid 37 )),'') ," +
				"IFNULL( (cast( abac03  as varchar(3) ccsid 37 )),'') ," +
				"IFNULL( (cast( abac04  as varchar(3) ccsid 37 )),'') ," +
				"IFNULL( (cast( abac05  as varchar(3) ccsid 37 )),'') ," +
				"IFNULL( (cast( abac06  as varchar(3) ccsid 37 )),'') ," +
				"IFNULL( (cast( abac07  as varchar(3) ccsid 37 )),'') ," +
				"IFNULL( (cast( abac08  as varchar(3) ccsid 37 )),'') ," +
				"IFNULL( (cast( abac09  as varchar(3) ccsid 37 )),'') ," +
				"IFNULL( (cast( abac10 as varchar(3) ccsid 37 )),'') " +
				" from "+PropertiesSystem.JDEDTA+".f0101 where aban8 = " + codigobeneficiario;
			
			List<Object[]>dtaF0101 = (ArrayList<Object[]>)session.createSQLQuery(strF0101).list();
			
			if(dtaF0101 == null || dtaF0101.isEmpty() ){
				System.out.println("strF0101 is null " + strF0101);
				return aplicado = false;
			}
			
			String rpyc01 = String.valueOf( dtaF0101.get(0)[0] ) ;
			String rpyc02 = String.valueOf( dtaF0101.get(0)[1] ) ;
			String rpac03 = String.valueOf( dtaF0101.get(0)[2] ) ;
			String rpyc04 = String.valueOf( dtaF0101.get(0)[3] ) ;
			String rpyc05 = String.valueOf( dtaF0101.get(0)[4] ) ;
			String rpac06 = String.valueOf( dtaF0101.get(0)[5] ) ;
			String rpyc07 = String.valueOf( dtaF0101.get(0)[6] ) ;
			String rpyc08 = String.valueOf( dtaF0101.get(0)[7] ) ;
			String rpac09 = String.valueOf( dtaF0101.get(0)[8] ) ;
			String rpac10 = String.valueOf( dtaF0101.get(0)[9] ) ;
			
			if( moneda.compareTo(monedaBase) == 0){
				tasaCambio = BigDecimal.ZERO;
				currencyMode = "D" ;
				montoDomestico = Integer.parseInt( String.format("%1$.2f", monto).replace(".", "")  ); 
			}else{
				currencyMode = "F" ;
				montoForaneo = Integer.parseInt( String.format("%1$.2f", monto).replace(".", "")  ); 
				montoDomestico = Integer.parseInt( String.format("%1$.2f", monto.multiply(tasaCambio) ).replace(".", "")  ); 
			}
			
			String strSqlPayInstruent = "select " +
					"IFNULL( '"+newApClass+"', '"+newApClass+"' ) a6apc, " +
					"IFNULL( (cast( a6pyin as varchar(1) ccsid 37 )), '"+MetodosPagoCtrl.TRANSFERENCIA+"' )  a6pyin from " +
					PropertiesSystem.JDEDTA+".f0401 where a6an8 = " + codigobeneficiario ;

			List<Object[]> dtaBnfF0401 = ( ArrayList<Object[]> )session.createSQLQuery(strSqlPayInstruent).list() ;
			
			//&& =============== crear la configuracion de proveedor al cliente si no existe 
			if(dtaBnfF0401 == null || dtaBnfF0401.isEmpty() ){
				
				insertConfigProveedor( session,  codigobeneficiario, usrA400, newApClass );
				
				System.out.println("no hay configuracion en F0401 strSqlPayInstruent  "+strSqlPayInstruent);
				
				glapclass = newApClass;
				paymentInstrument = MetodosPagoCtrl.TRANSFERENCIA;
				
			} else{
				glapclass = String.valueOf( dtaBnfF0401.get(0)[0] ) ;
				paymentInstrument = String.valueOf( dtaBnfF0401.get(0)[1] ) ;
			}
			
			
			String strSqlInsertF0411 = "insert into @JDEDTA.F0411 ( @FIELDS_TO_INSERT ) VALUES (@VALUES_TO_INSERT) " ;
			
			String insertFields = DefaultJdeFieldsValues.F0411_INSERT_COLUMNS;
			String insertValues = DefaultJdeFieldsValues.F0411_INSERT_COLUMNS_VALUES;
			
			insertFields += ", " +  DefaultJdeFieldsValues.F0411_COLUMN_NAMES_DEFAULT;
			insertValues += ", " +  DefaultJdeFieldsValues.F0411_COLUMN_NAMES_DEFAULT_VALUES;
			
			insertValues = 	insertValues
				 .replace("@RPKCO@",  companiaRpkco) 
				 .replace("@RPDOC@",  Integer.toString(nodocumento)) 
				 .replace("@RPDCT@",  tipodocumento)  
				 .replace("@RPSFX@",  numerocuota)  
				 .replace("@RPAN8@",  Integer.toString(codigobeneficiario))
				 .replace("@RPPYE@",  Integer.toString(codigobeneficiario))
				 .replace("@RPDIVJ@", strFechaActualJuliana)
				 .replace("@RPDSVJ@", strFechaActualJuliana)
				 .replace("@RPDDJ@",  strFechaActualJuliana)
				 .replace("@RPDDNJ@", strFechaActualJuliana)
				 .replace("@RPDGJ@",  strFechaActualJuliana)
				 .replace("@RPFY@",   Integer.toString(calendar.get(Calendar.YEAR) % 100))
				 .replace("@RPCTRY@", Integer.toString(calendar.get(Calendar.YEAR) / 100))
				 .replace("@RPPN@" ,  Integer.toString( calendar.get(Calendar.MONTH) + 1 ))
				 .replace("@RPCO@",   companiaRpco)  
				 .replace("@RPICU@",  Integer.toString(nobatch))
				 .replace("@RPICUT@", valoresJDEInsPMT[8])
				 .replace("@RPDICJ@", strFechaActualJuliana)  
				 .replace("@RPBALJ@", "Y") 
				 .replace("@RPPST@",  payStatusCode)  
				 .replace("@RPAG@",   Integer.toString(montoDomestico))
				 .replace("@RPAAP@",  Integer.toString(montoDomestico))
				 .replace("@RPATXN@", Integer.toString(montoDomestico))
				 .replace("@RPTXA1@", "EXE")  
				 .replace("@RPEXR1@", "E")  
				 .replace("@RPCRRM@", currencyMode)  
				 .replace("@RPCRCD@", moneda)  
				 .replace("@RPCRR@",  tasaCambio.toString() )  
				 .replace("@RPACR@",  Integer.toString(montoForaneo))
				 .replace("@RPFAP@",  Integer.toString(montoForaneo))
				 .replace("@RPCTXN@", Integer.toString(montoForaneo))
				 .replace("@RPGLC@",  glapclass)  
				 .replace("@RPGLBA@", Integer.toString(idcuenta))
				 .replace("@RPAM@",   "2")
				 .replace("@RPMCU@",  unidadNegocio)
				 .replace("@RPOBJ@",  ctaobj)
				 .replace("@RPSUB@",  ctaSub)
				 .replace("@RPVINV@", numfactura_descrip )  
				 .replace("@RPPYIN@", paymentInstrument )  
				 .replace("@RPAC07@", rpyc07)  
				 .replace("@RPBCRC@", monedaBase)  
				 
				 .replace("@RPYC01@", rpyc01)
				 .replace("@RPYC02@", rpyc02)
				 .replace("@RPYC03@", rpac03)
				 .replace("@RPYC04@", rpyc04)
				 .replace("@RPYC05@", rpyc05)
				 .replace("@RPYC06@", rpac06)
				 .replace("@RPYC07@", rpyc07)
				 .replace("@RPYC08@", rpyc08)
				 .replace("@RPYC09@", rpac09)
				 .replace("@RPYC10@", rpac10)
 
				 .replace("@RPTORG@", usrA400)  
				 .replace("@RPUSER@", usrA400)  
				 .replace("@RPPID@",  PropertiesSystem.ESQUEMA )  
				 .replace("@RPUPMJ@", strFechaActualJuliana)  
				 .replace("@RPUPMT@", hora)  
				 .replace("@RPJOBN@", PropertiesSystem.ESQUEMA)  
				 .replace("@RPRMK@",  observacionpago)
				 .replace("@RPPO@", CodeUtil.pad(numeroContrato, 8, "0"))
				.replace("@RPPDCT@", " ");
			
				strSqlInsertF0411 = strSqlInsertF0411
						.replace("@JDEDTA", PropertiesSystem.JDEDTA)
						.replace("@FIELDS_TO_INSERT", insertFields)
						.replace("@VALUES_TO_INSERT", insertValues);
				
				try {
					
					int rows = session.createSQLQuery(strSqlInsertF0411).executeUpdate();
					aplicado = rows == 1 ;
					
					
				} catch (Exception e) {
					e.printStackTrace();
					aplicado = false;
				}
				
		} catch (Exception e) {
			aplicado = false;
			e.printStackTrace(); 
		} 
		return aplicado;
	}
	
	public int IDCUENTABANCOPV(String tcod, String codcompany )
	{
		String query="SELECT TVALALF FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TCOD = '"+ tcod + "' AND COD_COMPANIA = '"+ codcompany +"' ";
		
		List<Object> cuenta= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
		
		return Integer.parseInt(cuenta.get(0).toString());		
	}
	
	public String obtenerDescripcionMetodoPago(MetodosPago mp) {
		String metPagoDesc = "";
		try {
			//Obtener la descripcion del metodo de pago
			Metpago[] listaMetPago = null;
			
			MetodosPagoCtrl ctrlMetPago = new MetodosPagoCtrl();
			listaMetPago = ctrlMetPago.obtenerDescripcionMetodosPago(mp.getMetodo());
			
			if (listaMetPago != null && listaMetPago.length > 0) {
				Metpago itm = listaMetPago[0];
				
				metPagoDesc = itm != null && itm.getId() != null && itm.getId().getMpago() != null ? itm.getId().getMpago().trim() : "";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return metPagoDesc.trim();
	}
}

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
import org.hibernate.Transaction;

import com.casapellas.controles.tmp.ReciboCtrl;
import com.casapellas.donacion.entidades.GValidate;
import com.casapellas.entidades.HistoricoReservasProformas;
import com.casapellas.entidades.MetodosPago;
import com.casapellas.entidades.Recibo;
import com.casapellas.entidades.Vf0901;
import com.casapellas.jde.creditos.CodigosJDE1;
import com.casapellas.jde.creditos.DefaultJdeFieldsValues;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.Divisas;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.PropertiesSystem;
import com.casapellas.entidades.ens.Vautoriz;
import com.ibm.icu.util.Calendar;

public class PlanMantenimientoTotalCtrl {

	public static String moneda_base;
	public static String moneda_aplicada;
	public static String codigo_compania;
	public static String codigo_sucursal_usuario;
	public static String codigo_unidad_negocio_usuario;
	public static BigDecimal monto_aplicado;
	public static BigDecimal diferencia;
	public static BigDecimal tasaoficial;
	public static BigDecimal tasaparalela;
	public static String strMensajeProceso;
	public static int codigo_caja;
	public static int numero_recibo;
	public static int numero_recibo_fcv;
	public static int numero_contrato;
	public static int numero_batch;
	public static int numero_documento;
	public static String moneda_contrato;
	public static BigDecimal cuota_contrato_pmt_usd;
	public static BigDecimal cuota_contrato_pmt_cod;
	public static List<MetodosPago>formas_de_pago;
	public static List<MetodosPago>compra_venta_Cambio;
	public static Connection cn ;
	public static List<String[]> numeros_batch_documento ;
	
	
	
	@SuppressWarnings("unchecked")
	public static List<HistoricoReservasProformas>  detalleContratoPMT(Recibo rc){
		 
		 List<HistoricoReservasProformas>detalleContratoPmt=null;
		 
		try {
			
			String strQuery = "select * from @BDCAJA.HISTORICO_RESERVAS_PROFORMAS where caid = @CAID " +
					"and TRIM(codcomp) = '@CODCOMP' and codcli = @CODCLIE and numrec = @NUMREC" ;
			
			strQuery = strQuery
				.replace("@BDCAJA", PropertiesSystem.ESQUEMA )
				.replace("@CAID", Integer.toString( rc.getId().getCaid() ) )
				.replace("@CODCOMP", rc.getId().getCodcomp().trim() )
				.replace("@CODCLIE", Integer.toString( rc.getCodcli() ) )
				.replace("@NUMREC", Integer.toString( rc.getId().getNumrec() ) );
			
			LogCajaService.CreateLog("detalleContratoPMT", "QRY", strQuery);
			
			 return detalleContratoPmt = (ArrayList<HistoricoReservasProformas>)
							ConsolidadoDepositosBcoCtrl.executeSqlQuery(strQuery, true, HistoricoReservasProformas.class);
			
		} catch (Exception e) {
			LogCajaService.CreateLog("detalleContratoPMT", "ERR", e.getMessage());
			detalleContratoPmt = null;
			e.printStackTrace(); 
		}
		 return detalleContratoPmt ;
	}
	
	
	
	public static  Object[] queryNumberContrato(int numeroContrato, int numeroCliente, String codcomp, String tipoAnticipo){
		Object[] dtaContrato = null ;
		
		try {
			
			String strQuery =
				" select numctto, numprop, cliente, trim(chasis), trim(motor), fecctto, horctto, trim(hechoc), trim(status), tvalrcto,  " +
				" compancto, sucurscto, codigo_moneda " +
				" from "+PropertiesSystem.QS36F+".sotmpc inner join "+PropertiesSystem.QS36F+".sottab on tbespe = compancto and tbcodi = '001' " +
				" where  trim(tbclas) = '"+codcomp.trim()+"' and cliente = "+numeroCliente+" and numctto = " + numeroContrato;
			
			if(!tipoAnticipo.isEmpty()){
				strQuery +=  " and status = '"+tipoAnticipo+"'";
			}
			
			LogCajaService.CreateLog("queryNumberContrato", "QRY", strQuery);
			
			@SuppressWarnings("unchecked")
			List<Object[]> result = (ArrayList<Object[]>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(strQuery, true, null);
			
			if( result != null && !result.isEmpty() )
				return dtaContrato = result.get(0);
			
		} catch (Exception e) {
			LogCajaService.CreateLog("queryNumberContrato", "ERR", e.getMessage());
			dtaContrato = null;
			e.printStackTrace(); 
		}
		
		return  dtaContrato ;
		
	}
	
	 public static List<GValidate> getListaPagosPMT(int numeroContrato, int numeroCliente){
		 
			try {
				
				String strQuery =
					" SELECT MONEDAAPL Moneda ,SUM(MONTORECIBO) Monto FROM "+PropertiesSystem.ESQUEMA+".HISTORICO_RESERVAS_PROFORMAS  " +
					" WHERE " +
					" CODCLI = "+ numeroCliente  + " AND NUMEROPROFORMA = " + numeroContrato +
					" AND ESTADO = 1 AND TIPORECIBO = 'PM'  GROUP BY MONEDAAPL";
				
				
				
				@SuppressWarnings("unchecked")
				List<GValidate>  returnList = (List<GValidate>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(strQuery, true, null);
				
				 return returnList;
				
			} catch (Exception e) {				
				e.printStackTrace(); 
			}
		 
			 return null;
	 }
	
	public static String  comprantesPorCompraVenta(final List<MetodosPago> formas_pago_compraVenta, List<String[]> cuentasPorMetodoPago, 
								Vautoriz vaut, int iClienteCodigo, String unidadNegocioCaja, int numero_recibo_caja, Session session, String unidadNegociosCaja ){
		String strMensajeProceso = "";
		boolean hecho = true;
		long lngMontoFormaDePagoExt;
		long lngMontoFormaDePagoNac;
		
		String msgLog = "" ;
		
		try {
			
			
			String companiaRpkco =  unidadNegocioCaja.substring(0,2);
			double lineadoc = 0 ;
			Date fechaRecibo = new Date();
			String tipodocjde = PropertiesSystem.TIPODOC_REFER_P9;
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
			
//			int numero_batch = Divisas.numeroSiguienteJdeE1( CodigosJDE1.NUMEROBATCH );
			int numero_batch = Divisas.numeroSiguienteJdeE1( );
			int numero_documento = Divisas.numeroSiguienteJdeE1( CodigosJDE1.NUMERO_DOC_CONTAB_GENERAL );
			
			if(numero_batch == 0) {
				 hecho = false;
				 return  strMensajeProceso = "Error al generar consecutivo número de batchs para el recibo";
			}
			if (numero_documento == 0) {
				 hecho = false;
				 return  strMensajeProceso = "Error al generar consecutivo número de documento para el recibo";
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
		
			//hecho = recCtrl.registrarBatchA92(cn, "G", numero_batch, lngMontoFormaDePagoExt, vaut.getId().getLogin(), 1,"N");
			hecho = recCtrl.registrarBatchA92(session, fechaRecibo, CodigosJDE1.RECIBOCONTADO, numero_batch, lngMontoFormaDePagoExt, vaut.getId().getLogin(), 1, "PMT", CodigosJDE1.BATCH_ESTADO_PENDIENTE ); 
			
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
//							dtacta[8].compareTo(mp.getMoneda()) == 0 && 
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
				
				observacion = "Mp: " + mp.getMetododescrip();
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
								"", observacion, ctaBnfMcu,"","", moneda_contrato.compareTo(moneda_base)==0 ? moneda_base : moneda_contrato, //mp.getMoneda(), 
								companiaRpkco, "F", 0 );
					
					
					if(!hecho){
						return strMensajeProceso = "No se puede generar asiento de diario por compra Venta de Moneda Extranjera para metodo de pago " + mp.getMetododescrip().trim();
					}
					
					hecho = recCtrl.registrarAsientoDiarioLogs(session, msgLog, fechaRecibo, companiaRpkco, tipodocjde, numero_documento, 
							( lineadoc ), numero_batch, ctaBnfcuenta, ctaBnfGmaid, ctaBnfMcu, ctaBnfObj, ctaBnfSub, "AA",
							moneda_contrato.compareTo(moneda_base)==0 ? mp.getMoneda() : moneda_contrato, lngMontoFormaDePagoNac, concepto, vaut.getId().getLogin(), vaut.getId().getCodapp(), 
							mp.getTasa(), //tasaoficial, //
							"", observacion, ctaBnfMcu,"","", moneda_base, companiaRpkco, "F", lngMontoFormaDePagoExt );
					
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
							"", observacion, ctaBnfMcu,"","", moneda_contrato.compareTo(moneda_base)==0 ? moneda_base : moneda_contrato, //mp.getMoneda(), 
							companiaRpkco, "F", 0);
					
					if(!hecho){
						return strMensajeProceso = "No se puede generar asiento de diario por compra Venta de Moneda Extranjera para metodo de pago " + mp.getMetododescrip().trim();
					}
	
					hecho = recCtrl.registrarAsientoDiarioLogs(session, msgLog, fechaRecibo, companiaRpkco, tipodocjde, numero_documento, 
							( lineadoc ), numero_batch, ctaBnfcuenta, ctaBnfGmaid, ctaBnfMcu, ctaBnfObj, ctaBnfSub, "AA",
							moneda_contrato.compareTo(moneda_base)==0 ? mp.getMoneda() : moneda_contrato, (lngMontoFormaDePagoNac*-1), concepto, vaut.getId().getLogin(), vaut.getId().getCodapp(), 
							mp.getTasa(), //tasaoficial,//
							"", observacion, ctaBnfMcu,"","", moneda_base, companiaRpkco, "F", (lngMontoFormaDePagoExt*-1) );
	
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
								"", observacion, ctaBnfMcu,"","", moneda_contrato.compareTo(moneda_base)==0 ? moneda_base : moneda_contrato, //mp.getMoneda(), 
								companiaRpkco, "F", 0 );
					
					
					if(!hecho){
						return strMensajeProceso = "No se puede generar asiento de diario por compra Venta de Moneda Extranjera para metodo de pago " + mp.getMetododescrip().trim();
					}
					
					hecho = recCtrl.registrarAsientoDiarioLogs(session, msgLog, fechaRecibo, companiaRpkco, tipodocjde, numero_documento, 
							( lineadoc ), numero_batch, ctaBnfcuenta, ctaBnfGmaid, ctaBnfMcu, ctaBnfObj, ctaBnfSub, "AA",
							moneda_contrato.compareTo(moneda_base)==0 ? mp.getMoneda() : moneda_contrato, ((lngMontoFormaDePagoNac-lngMontoFormaDePago)*-1), concepto, vaut.getId().getLogin(), vaut.getId().getCodapp(), 
							mp.getTasa(), //tasaoficial, //
							"", observacion, ctaBnfMcu,"","", moneda_base, companiaRpkco, "F", lngMontoFormaDePagoExt );
					
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
							"", observacion, ctaBnfMcu,"","", moneda_contrato.compareTo(moneda_base)==0 ? moneda_base : moneda_contrato, //mp.getMoneda(), 
							companiaRpkco, "F", 0);
					
					if(!hecho){
						return strMensajeProceso = "No se puede generar asiento de diario por compra Venta de Moneda Extranjera para metodo de pago " + mp.getMetododescrip().trim();
					}
	
					hecho = recCtrl.registrarAsientoDiarioLogs(session, msgLog, fechaRecibo, companiaRpkco, tipodocjde, numero_documento, 
							( lineadoc ), numero_batch, ctaBnfcuenta, ctaBnfGmaid, ctaBnfMcu, ctaBnfObj, ctaBnfSub, "AA",
							moneda_contrato.compareTo(moneda_base)==0 ? mp.getMoneda() : moneda_contrato, (lngMontoFormaDePagoNac-lngMontoFormaDePago), concepto, vaut.getId().getLogin(), vaut.getId().getCodapp(), 
							mp.getTasa(), //tasaoficial,//
							"", observacion, ctaBnfMcu,"","", moneda_base, companiaRpkco, "F", (lngMontoFormaDePagoExt*-1) );
	
					if(!hecho){
						return strMensajeProceso = "No se puede generar asiento de diario por compra Venta de Moneda Extranjera para metodo de pago " + mp.getMetododescrip().trim();
					}

					lngMontoFormaDePago = lngMontoFormaDePago - Long.parseLong( String.format("%1$.2f", mp.getDiferenciaCor()).replace(".", ""));
					
					if(lngMontoFormaDePago!=0)
					{
						String sCtaObDif = "66000";
						String sCtaSubDif = "01";
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
			
		}
		return strMensajeProceso;
	}
	
	static long lngMontoAplicadoForaneo = 0;
	public static String generarComprobantesContablesPMT(Date fechaRecibo, int iClienteCodigo, 
			Vautoriz vaut, int numero_cuota, Session session  ){
		
		String strMensajeProceso = "";
		
		String tipodocjde = "PV";
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
					 
//					 if( mp.getMoneda().compareTo(moneda_base) != 0) {
					 if( mp.getMoneda().compareTo(moneda_contrato) != 0) {

//						 if( moneda_aplicada.compareTo(moneda_base) != 0 ){
//							 monto_aplicado = monto_aplicado.multiply( tasaoficial ).setScale(2, RoundingMode.HALF_UP);
//						 }
						 
						 formas_pago_compraVenta.add( mp.clone() ); 

						 
					 }

				}
			}); 
			

			//Se agrego para ver diferencias de centavos
			//------  lfonseca --------
			if(formas_pago_compraVenta != null && !formas_pago_compraVenta.isEmpty() ){
				
				long lngMontoFormaDePagoT = 0;
				long lngMontoFormaDePagoT2 = 0;
				long lngMontoFormaDePagoEqT = 0;
				
				for (final MetodosPago mp : formas_de_pago) {
					
					lngMontoFormaDePagoT += mp.getMoneda().compareTo(moneda_base)==0 ? 
							mp.getMoneda().compareTo(moneda_contrato)==0 ? 
									Long.parseLong( String.format("%1$.2f", BigDecimal.valueOf(mp.getMonto()/tasaoficial.doubleValue())).replace(".", "")) : 
									Long.parseLong( String.format("%1$.2f", BigDecimal.valueOf(mp.getMonto()/tasaparalela.doubleValue())).replace(".", "")) :
							Long.parseLong( String.format("%1$.2f", BigDecimal.valueOf(mp.getMonto())).replace(".", ""));
					
					lngMontoFormaDePagoT2 = mp.getMoneda().compareTo(moneda_base)==0 ? 
							mp.getMoneda().compareTo(moneda_contrato)==0 ? 
									Long.parseLong( String.format("%1$.2f", BigDecimal.valueOf(mp.getMonto()/tasaoficial.doubleValue())).replace(".", "")) : 
									Long.parseLong( String.format("%1$.2f", BigDecimal.valueOf(mp.getMonto()/tasaparalela.doubleValue())).replace(".", "")) :
							Long.parseLong( String.format("%1$.2f", BigDecimal.valueOf(mp.getMonto())).replace(".", ""));
									
													
					lngMontoFormaDePagoEqT +=  mp.getMoneda().compareTo(moneda_base)==0 && mp.getMoneda().compareTo(moneda_contrato)==0 ? 
									Long.parseLong( String.format("%1$.2f", BigDecimal.valueOf(mp.getMonto())).replace(".", ""))
									: 
									Long.parseLong( String.format("%1$.2f", BigDecimal.valueOf(((double)lngMontoFormaDePagoT2/100)*tasaoficial.doubleValue())).replace(".", ""));
												
				}
				
				long diferenciaT = 0;
				if(lngMontoFormaDePagoEqT!=Long.parseLong( String.format("%1$.2f", cuota_contrato_pmt_cod).replace(".", "")))
				{
					diferenciaT = Long.parseLong( String.format("%1$.2f", cuota_contrato_pmt_cod).replace(".", "")) - lngMontoFormaDePagoEqT;
				}
				
				if(diferenciaT!=0)
				{
					int i = 0;
					for (final MetodosPago mp : formas_de_pago) {
						
						if(mp.getMoneda().compareTo(moneda_contrato)!=0)
						{
							formas_de_pago.get(i).setDiferenciaCor(new BigDecimal((double)diferenciaT/100));
							break;
						}
						
						i++;
					}
					
					i = 0;
					for (final MetodosPago mp2 : formas_pago_compraVenta) {
						
						if(mp2.getMoneda().compareTo(moneda_contrato)!=0)
						{
							formas_pago_compraVenta.get(i).setDiferenciaCor(new BigDecimal((double)diferenciaT/100));
							break;
						}
						
						i++;
					}
				}
				
			}
			
			//Se agrego para ver diferencias de centavos
			//------  lfonseca --------
			//Termino
			
			if(formas_pago_compraVenta != null && !formas_pago_compraVenta.isEmpty() ){
				
				strMensajeProceso = comprantesPorCompraVenta(formas_pago_compraVenta, cuentasPorMetodoPago, vaut, iClienteCodigo, 
						unidadNegociosCaja, numero_recibo, session, unidadNegociosCaja);
				
				if( !strMensajeProceso.isEmpty() )
					return 	strMensajeProceso ;
				
			}
			
			//&& ====================== comprobante de pasivo
			
			numero_batch = Divisas.numeroSiguienteJdeE1(  );
			numero_documento = Divisas.numeroSiguienteJdeE1( CodigosJDE1.NUMEROPAGOVOUCHER );
		
			if(numero_batch == 0) {
				hecho = false;
				return strMensajeProceso = "Error al generar consecutivo número de batchs para el recibo";
			}
			if (numero_documento == 0) {
				hecho = false;
				return strMensajeProceso = "Error al generar consecutivo número de documento para el recibo";
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

			hecho = rcCtrl.registrarBatchA92(session, fechaRecibo, CodigosJDE1.BATCH_ANTICIPO_PMT, numero_batch, 
					//lngMontoAplicadoLocal, 
					(moneda_contrato.compareTo(moneda_base)==0 ? 
							Long.parseLong( String.format("%1$.2f", cuota_contrato_pmt_cod).replace(".", "")   ) : 
							Long.parseLong( String.format("%1$.2f", cuota_contrato_pmt_usd).replace(".", "")   )) ,
					vaut.getId().getLogin(), 1, "PMT", CodigosJDE1.BATCH_ESTADO_PENDIENTE );
			
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
//						Integer.toString(numero_contrato), "LC",  "#", "PMT" ) ;
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
					
					lngMontoFormaDePagoEq =  Long.parseLong( String.format("%1$.2f", cuota_contrato_pmt_cod).replace(".", ""));;
												
	
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
												
	
					lngMontoFormaDePagoEq = lngMontoFormaDePagoEq + Long.parseLong( String.format("%1$.2f", mp.getDiferenciaCor()).replace(".", ""));
				}
				//&& =========== Grabar el registro en F0911 
				//Ajustado por LFonseca
				//Fecha: 2020-11-28
				//Se agregaron los parametros de session y trans
				hecho = registrarF0911PMT( session, fechaRecibo, companiaRpkco, tipodocjde, numero_documento, 
						(++lineadoc), numero_batch, ctaBnfcuenta, ctaBnfGmaid, ctaBnfMcu, ctaBnfObj, ctaBnfSub, 
						"AA", moneda_contrato, lngMontoFormaDePago, concepto, vaut.getId().getLogin(),
						vaut.getId().getCodapp(), tasaoficial, tipocliente, observacion, 
						ctaBnfMcu, codbnf_aux, tipoAuxiliar, moneda_base, companiaRpkco, tipoDocxMon, 
						"V", iClienteCodigo, numeroContrato, moneda_base, lngMontoFormaDePagoEq);
				
				if(!hecho){
					return strMensajeProceso = "Error al grabar el detalle para metodo de pago " + mp.getMetododescrip();
				}
				
//				break;
				
			}
			 

			/* ********************************************************************************************************************* */
			/* *********************** no mandar a generar asientos contables por cambios en moneda local   ************************ */
			
			if(compra_venta_Cambio != null && !compra_venta_Cambio.isEmpty()){
				
				strMensajeProceso = comprantesPorCompraVenta(compra_venta_Cambio, cuentasPorMetodoPago, 
											vaut, iClienteCodigo, unidadNegociosCaja, numero_recibo_fcv, session, unidadNegociosCaja) ;
				
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
//		finally{
//			System.out.println( "Finaliza generarComprobantesContablesPMT : " +  new SimpleDateFormat("HH:mm:ss.SSS").format(new Date() ) );
//		}
		return strMensajeProceso;
	}
	
	public static List<String[]> cuentasPorMetodoPago(){
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
	
	public static List<String[]> cuentasPorMetodoPago_PV(){
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
				
//				if( mp.getMoneda().compareTo(moneda_base) !=0 ){
//					strWhere += strSqlOr.replace("@CAID", Integer.toString(codigo_caja))
//							.replace("@CODCOMP", codigo_compania)
//							.replace("@MONEDA", moneda_base )
//							.replace("@FORMADEPAGO", mp.getMetodo() );
//				}
				
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
	
	public static boolean registrarF0911PMT(Session sesion,  Date dtFechaAsiento, 
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
					
//					.replace("@GLBCRC@", sGlbcrc)
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
			
			/*
			PreparedStatement ps = null;
			try {

				ps = cn.prepareStatement(strExecute);
				int rs = ps.executeUpdate();
				aplicado = rs == 1;

			} catch (Exception e) {
				e.printStackTrace();
				aplicado = false;
			}
			
			ps.close();
			*/
			
		} catch (Exception ex) {
			aplicado = false;
			ex.printStackTrace(); 
		}
		return aplicado;
	}
	public static boolean registrarF0911PMT(Date dtFechaAsiento, 
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
	
	public static boolean insertConfigProveedor(Session sesion, int codigoCliente, String usuario, String newApClass ){
		boolean insert = true;
		
		try {
			
			String sqlInsert = 
			" INSERT INTO @JDEDTA.F0401 " +
			"(A6AN8, A6APC, A6DCAP, A6TAWH, A6PCWH, A6SCK, A6SNTO, A6FLD, A6SQNL, A6CRCA, A6AYPD, A6APPD, A6ABAM, " +
			"A6ABA1, A6APRC, A6MINO, A6MAXO, A6AN8R, A6BADT, A6ANCR, A6CARS, A6LTDT, A6INVC, A6PLST, A6EDPM, A6EDQD, " +
			"A6EDAD, A6EDF1, A6MNSC, A6ATO, A6RVNT, A6URDT, A6URAT, A6URAB, A6USER, A6PID, A6JOBN, A6UPMJ, A6UPMT, A6AVCH, A6PYIN " +
			" ) " +
//			"A6RMTA, A6TORG, A6FHD3, A6FHD4) " +
			"VALUES " +
			"(@A6AN8C, '@A6APC', @A6DCAP, @A6TAWH, @A6PCWH, '@A6SCK', @A6SNTO, @A6FLD, @A6SQNL, '@A6CRCA', @A6AYPD, @A6APPD, @A6ABAM, " +
			"@A6ABA1, @A6APRC, @A6MINO, @A6MAXO, @A6AN8R, '@A6BADT', @A6ANCR, @A6CARS, @A6LTDT, @A6INVC, '@A6PLST', '@A6EDPM', @A6EDQD, " +
			"@A6EDAD, '@A6EDF1', @A6MNSC, '@A6ATO', '@A6RVNT', @A6URDT, @A6URAT, @A6URAB, '@A6USER', '@A6PID', '@A6JOBN', @A6UPMJ, @A6UPMT, '@A6AVCH', '@A6PYIN' " +
			" ) ";
//			"@A6RMTA, '@A6TORG', @A6FHD3, @A6FHD4)";
			
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
//			.replace("@A6RMTA",  "0" )
//			.replace("@A6TORG",  usuario )
//			.replace("@A6FHD3",  "0" )
//			.replace("@A6FHD4",  "0" ) ;
			
			try {
				insert = ConsolidadoDepositosBcoCtrl .executeSqlQueryTx( sesion,  sqlInsert );
			} catch (Exception e) {
				e.printStackTrace();
				insert = false;
			}
			
			/*
			PreparedStatement ps = null;
			try {

				ps = cn.prepareStatement(sqlInsert);
				int rs = ps.executeUpdate();
				insert = rs == 1;

			} catch (Exception e) {
				e.printStackTrace();
				insert = false;
			}
			
			ps.close();
			*/
			
		} catch (Exception e) {
			e.printStackTrace(); 
			insert = false;
		}
		return insert;
	}
	public static boolean insertConfigProveedor(int codigoCliente, String usuario, String newApClass, Session session ){
		boolean insert = true;
		
		try {
			
			String sqlInsert = 
			" INSERT INTO @JDEDTA.F0401 " +
			"(A6AN8, A6APC, A6DCAP, A6TAWH, A6PCWH, A6SCK, A6SNTO, A6FLD, A6SQNL, A6CRCA, A6AYPD, A6APPD, A6ABAM, " +
			"A6ABA1, A6APRC, A6MINO, A6MAXO, A6AN8R, A6BADT, A6ANCR, A6CARS, A6LTDT, A6INVC, A6PLST, A6EDPM, A6EDQD, " +
			"A6EDAD, A6EDF1, A6MNSC, A6ATO, A6RVNT, A6URDT, A6URAT, A6URAB, A6USER, A6PID, A6JOBN, A6UPMJ, A6UPMT, A6AVCH, A6PYIN " +
			" ) " +
//			"A6RMTA, A6TORG, A6FHD3, A6FHD4) " +
			"VALUES " +
			"(@A6AN8C, '@A6APC', @A6DCAP, @A6TAWH, @A6PCWH, '@A6SCK', @A6SNTO, @A6FLD, @A6SQNL, '@A6CRCA', @A6AYPD, @A6APPD, @A6ABAM, " +
			"@A6ABA1, @A6APRC, @A6MINO, @A6MAXO, @A6AN8R, '@A6BADT', @A6ANCR, @A6CARS, @A6LTDT, @A6INVC, '@A6PLST', '@A6EDPM', @A6EDQD, " +
			"@A6EDAD, '@A6EDF1', @A6MNSC, '@A6ATO', '@A6RVNT', @A6URDT, @A6URAT, @A6URAB, '@A6USER', '@A6PID', '@A6JOBN', @A6UPMJ, @A6UPMT, '@A6AVCH', '@A6PYIN' " +
			" ) ";
//			"@A6RMTA, '@A6TORG', @A6FHD3, @A6FHD4)";
			
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
				//insert = session.create  ConsolidadoDepositosBcoCtrl .executeSqlQuery( sqlInsert );
				
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
	public static boolean registrarF0411PorPMT(Session sesion,  String companiaRpkco, String companiaRpco,
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
//				 .replace("@RPPYE@",  "B01")
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
				 .replace("@RPICUT@", CodigosJDE1.BATCH_ANTICIPO_PMT.codigo() )
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
				
				 //&& ===== en E1 va en blanco
				 //.replace("@RPPDCT@", tipoDocLiga);
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
	public static boolean registrarF0411PorPMT(String companiaRpkco, String companiaRpco,
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
			
			//List<Object[]>dtaF0101 = (ArrayList<Object[]>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(strF0101, true,null );
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

			//List<Object[]> dtaBnfF0401 = ( ArrayList<Object[]> )ConsolidadoDepositosBcoCtrl.executeSqlQuery( strSqlPayInstruent, true,null ) ;
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
				 .replace("@RPICUT@", CodigosJDE1.BATCH_ANTICIPO_PMT.codigo() )
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
				
				 //&& ===== en E1 va en blanco
				 //.replace("@RPPDCT@", tipoDocLiga);
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
	
	public static int IDCUENTABANCOPV(String tcod, String codcompany )
	{
		String query="SELECT TVALALF FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TCOD = '"+ tcod + "' AND COD_COMPANIA = '"+ codcompany +"' ";
		
		List<Object> cuenta= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
		
		return Integer.parseInt(cuenta.get(0).toString());		
	}
	
}

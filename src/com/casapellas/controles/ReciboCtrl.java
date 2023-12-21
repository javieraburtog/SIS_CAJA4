package com.casapellas.controles;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.casapellas.entidades.F55ca03;
import com.casapellas.entidades.Arqueo;
import com.casapellas.entidades.Arqueorec;
import com.casapellas.entidades.Bien;
import com.casapellas.entidades.Cambiodet;
import com.casapellas.entidades.CambiodetId;
import com.casapellas.entidades.Credhdr;
import com.casapellas.entidades.Dfactura;
import com.casapellas.entidades.F55ca011;
import com.casapellas.entidades.F55ca018;
import com.casapellas.entidades.F55ca024;
import com.casapellas.entidades.FacturaxRecibo;
import com.casapellas.entidades.Hfactjdecon;
import com.casapellas.entidades.Hfactura;
import com.casapellas.entidades.Hfacturajde;
import com.casapellas.entidades.MetodosPago;
import com.casapellas.entidades.Recibo;
import com.casapellas.entidades.ReciboId;
import com.casapellas.entidades.Recibodet;
import com.casapellas.entidades.RecibodetId;
import com.casapellas.entidades.Recibofac;
import com.casapellas.entidades.RecibofacId;
import com.casapellas.entidades.Recibojde;
import com.casapellas.entidades.RecibojdeId;
import com.casapellas.entidades.Recibolog;
import com.casapellas.entidades.Reintegro;
import com.casapellas.entidades.ReintegroId;
import com.casapellas.entidades.ReporteIR;
import com.casapellas.entidades.Usuario;
import com.casapellas.entidades.Vf0101;
import com.casapellas.entidades.Vf0101Id;
import com.casapellas.entidades.Vf0311fn;
import com.casapellas.entidades.Vmarca;
import com.casapellas.entidades.Vmodelo;
import com.casapellas.entidades.Vmonedafactrec;
import com.casapellas.entidades.Vrecfac;
import com.casapellas.entidades.VrecibosxtipompagoId;
import com.casapellas.entidades.Vretencion;
import com.casapellas.entidades.VtipoProd;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.jde.creditos.BatchControlF0011;
import com.casapellas.jde.creditos.DatosComprobanteF0911;
import com.casapellas.jde.creditos.DefaultJdeFieldsValues;
import com.casapellas.jde.creditos.ProcesarEntradaDeDiario;
import com.casapellas.jde.creditos.ProcesarEntradaDeDiarioCustom;
import com.casapellas.navegacion.As400Connection;
import com.casapellas.util.*;

/**
 * CASA PELLAS S.A.
 * Creado por.........: Juan Carlos Ñamendi Pineda
 * Fecha de Creación..: 22/06/2009
 * Última modificación: 09/07/2012
 * Modificado por.....:	Juan Carlos Ñamendi Pineda
 * Detalle Anterior ..: Correccion al grabar el detalle dell recibo
 * 
 */
public class ReciboCtrl {
	public Exception errorDetalle;
	public Exception error;
	
	static Map<String, Object> m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();	
	static String[] valoresJdeNumeracion = (String[]) m.get("valoresJDENumeracionIns");
	static String[] valoresJDEInsContado = (String[]) m.get("valoresJDEInsContado");
	
	
	public Exception getError() {
		return error;
	}
	public void setError(Exception error) {
		this.error = error;
	}	
	public Exception getErrorDetalle() {
		return errorDetalle;
	}
	public void setErrorDetalle(Exception errorDetalle) {
		this.errorDetalle = errorDetalle;
	}
	
	public static String batchCompraVentaCambios(Session session, int numeroficha, int numrec, int caid, 
			String codcomp, String codsuc, BigDecimal montoCambio, 
			BigDecimal tasaoficial, Date fecharecibo, String usuario,
			int codigousuario, String monedaLocal){

		
		String msgEstadoProceso = "";
		boolean aplicado = true;
		String companiaDocumento = "00000";
	
		try {
		
			int numeroBatchJde = Divisas.numeroSiguienteJdeE1( );
			int numeroReciboJde = Divisas.numeroSiguienteJdeE1Custom(valoresJdeNumeracion[8], valoresJdeNumeracion[9]);
			
			if(numeroBatchJde == 0) {
				 aplicado = false;
				 return msgEstadoProceso = "Error al generar consecutivo número de batchs para el recibo";
				 
			}
			if (numeroReciboJde == 0) {
				aplicado = false;
				return msgEstadoProceso = "Error al generar consecutivo número de documento para el recibo";
			}
			
			List<MetodosPago>formasPago = new ArrayList<MetodosPago>();
			MetodosPago mpDolares = new MetodosPago();
			mpDolares.setMoneda("USD");
			mpDolares.setMetodo(MetodosPagoCtrl.EFECTIVO);
			formasPago.add(mpDolares);
			
			MetodosPago mpCordobas = new MetodosPago();
			mpCordobas.setMoneda("COR");
			mpCordobas.setMetodo(MetodosPagoCtrl.EFECTIVO);
			formasPago.add(mpCordobas);
			 
			List<String[]> cuentasContables = Divisas.cuentasFormasPago(formasPago, caid, codcomp);
			
			String[] dtaCuentaDolares =  (String[])
				CollectionUtils.find(cuentasContables, new Predicate(){
					@Override
					public boolean evaluate(Object o) {
						String[] dtaCuenta = (String[])o;
						return   dtaCuenta[7].compareTo( "USD" ) == 0;
					}
				});
			String[] dtaCuentaCordobas =  (String[])
				CollectionUtils.find(cuentasContables, new Predicate(){
					@Override
					public boolean evaluate(Object o) {
						String[] dtaCuenta = (String[])o;
						return   dtaCuenta[7].compareTo( "COR" ) == 0;
					}
				});
			
			String concepto = "Ficha Rc:"+numrec + " Ca:"+caid +" Comp:"+codcomp;
			companiaDocumento =  dtaCuentaDolares[2] ;
		
			//&& ========= informacion para las lineas del f0911
			 List<DatosComprobanteF0911> lineasComprobante = new ArrayList<DatosComprobanteF0911>();
			 DatosComprobanteF0911 dtaDolares = new DatosComprobanteF0911(
					 dtaCuentaDolares[1],
					 montoCambio, 
					 concepto,
					 tasaoficial,
					 "",
					 "",
					 companiaDocumento );
			 lineasComprobante.add(dtaDolares);
			 
			 DatosComprobanteF0911 dtaCordobas = new DatosComprobanteF0911(
					 dtaCuentaCordobas[1],
					 montoCambio.negate(), 
					 concepto,
					 tasaoficial,
					 "",
					 "",
					 companiaDocumento);
			 lineasComprobante.add(dtaCordobas);
			 
			 new ProcesarEntradaDeDiarioCustom();
			 ProcesarEntradaDeDiarioCustom.procesarSql = false;
			 ProcesarEntradaDeDiarioCustom.monedaComprobante = "USD";
			 ProcesarEntradaDeDiarioCustom.monedaLocal = monedaLocal;
			 ProcesarEntradaDeDiarioCustom.fecharecibo = fecharecibo;
			 ProcesarEntradaDeDiarioCustom.tasaCambioParalela = tasaoficial; 
			 ProcesarEntradaDeDiarioCustom.tasaCambioOficial = tasaoficial; 
			 ProcesarEntradaDeDiarioCustom.montoComprobante = montoCambio;
			 ProcesarEntradaDeDiarioCustom.conceptoComprobante = "Ficha:" + numeroficha + " C:"+caid +" Comp:"+codcomp;
			 ProcesarEntradaDeDiarioCustom.numeroBatchJde = String.valueOf( numeroBatchJde );
			 ProcesarEntradaDeDiarioCustom.numeroReciboJde = String.valueOf( numeroReciboJde ) ;
			 ProcesarEntradaDeDiarioCustom.usuario = usuario;
			 ProcesarEntradaDeDiarioCustom.codigousuario = codigousuario;
			 ProcesarEntradaDeDiarioCustom.lineasComprobante = lineasComprobante;
			 ProcesarEntradaDeDiarioCustom.tipoDocumento=valoresJDEInsContado[1];
			 ProcesarEntradaDeDiarioCustom.valoresJdeInsContado = valoresJDEInsContado; 
			 ProcesarEntradaDeDiarioCustom.procesarEntradaDeDiario(session,session.getTransaction());
			 
			 msgEstadoProceso = ProcesarEntradaDeDiarioCustom.msgProceso;
			 aplicado = msgEstadoProceso.isEmpty();
			 
			 if(!aplicado){
				 return msgEstadoProceso ;
			 }
			 
			 List<String[]> querysToExecute = ProcesarEntradaDeDiarioCustom.lstSqlsInserts ;
			 if( querysToExecute == null || querysToExecute.isEmpty() ){
				 return msgEstadoProceso =  "Error al generar datos para grabar batch por compra venta de cambio " ;
			 }
			 
			 for (String[] querys : querysToExecute ) {
					
					
	
					try {
						
						LogCajaService.CreateLog("batchCompraVentaCambios - " + querys[1], "QRY", querys[0]);
						
						int rows = session.createSQLQuery(querys[0]).executeUpdate() ;
		
					} catch (Exception e) {
						LogCajaService.CreateLog("batchCompraVentaCambios - " + querys[1], "ERR", e.getMessage());
						e.printStackTrace(); 
						return  msgEstadoProceso = "fallo en interfaz Edwards "+ querys[1] ;
					}
				}
			 
			 aplicado = crearRegistroReciboCajaJde(session, caid, numeroficha, codcomp, codsuc,  "FCV", "A", numeroBatchJde, Arrays.asList(new String[]{String.valueOf(numeroReciboJde)}));
			 
			 if(!aplicado){
				 msgEstadoProceso = ProcesarEntradaDeDiario.msgProceso = "No se ha creado el enlace entre documentos de caja y edwards para la ficha de cambio " ;
				 return msgEstadoProceso;
			 }
			 
			 
		} catch (Exception e) {
			LogCajaService.CreateLog("batchCompraVentaCambios", "ERR", e.getMessage());
			e.printStackTrace();
			aplicado = false;
			msgEstadoProceso = "No se ha podido grabar la ficha de compra venta por cambio";
		}
		return msgEstadoProceso;
	
	}
	

	public static String batchCompraVentaCambios(int numeroficha, int numrec, int caid, 
			String codcomp, String codsuc, BigDecimal montoCambio, 
			BigDecimal tasaoficial, Date fecharecibo, String usuario,
			int codigousuario, Session session1, String monedaLocal){

		
		String msgEstadoProceso = "";
		boolean aplicado = true;
		String companiaDocumento = "00000";
	
		try {
			
			int numeroBatchJde = Divisas.numeroSiguienteJdeE1( );
			int numeroReciboJde = Divisas.numeroSiguienteJdeE1Custom(valoresJdeNumeracion[8], valoresJdeNumeracion[9]);
			
			if(numeroBatchJde == 0) {
				 aplicado = false;
				 return msgEstadoProceso = "Error al generar consecutivo número de batchs para el recibo";
				 
			}
			if (numeroReciboJde == 0) {
				aplicado = false;
				return msgEstadoProceso = "Error al generar consecutivo número de documento para el recibo";
			}
			
			List<MetodosPago>formasPago = new ArrayList<MetodosPago>();
			MetodosPago mpDolares = new MetodosPago();
			mpDolares.setMoneda("USD");
			mpDolares.setMetodo(MetodosPagoCtrl.EFECTIVO);
			formasPago.add(mpDolares);
			
			MetodosPago mpCordobas = new MetodosPago();
			mpCordobas.setMoneda("COR");
			mpCordobas.setMetodo(MetodosPagoCtrl.EFECTIVO);
			formasPago.add(mpCordobas);
			 
			List<String[]> cuentasContables = Divisas.cuentasFormasPago(formasPago, caid, codcomp);
			
			String[] dtaCuentaDolares =  (String[])
				CollectionUtils.find(cuentasContables, new Predicate(){
					@Override
					public boolean evaluate(Object o) {
						String[] dtaCuenta = (String[])o;
						return   dtaCuenta[7].compareTo( "USD" ) == 0;
					}
				});
			String[] dtaCuentaCordobas =  (String[])
				CollectionUtils.find(cuentasContables, new Predicate(){
					@Override
					public boolean evaluate(Object o) {
						String[] dtaCuenta = (String[])o;
						return   dtaCuenta[7].compareTo( "COR" ) == 0;
					}
				});
			
			String concepto = "Ficha Rc:"+numrec + " Ca:"+caid +" Comp:"+codcomp;
			companiaDocumento =  dtaCuentaDolares[2] ;
		
			//&& ========= informacion para las lineas del f0911
			 List<DatosComprobanteF0911> lineasComprobante = new ArrayList<DatosComprobanteF0911>();
			 DatosComprobanteF0911 dtaDolares = new DatosComprobanteF0911(
					 dtaCuentaDolares[1],
					 montoCambio, 
					 concepto,
					 tasaoficial,
					 "",
					 "",
					 companiaDocumento );
			 lineasComprobante.add(dtaDolares);
			 
			 DatosComprobanteF0911 dtaCordobas = new DatosComprobanteF0911(
					 dtaCuentaCordobas[1],
					 montoCambio.negate(), 
					 concepto,
					 tasaoficial,
					 "",
					 "",
					 companiaDocumento);
			 lineasComprobante.add(dtaCordobas);
			 
			 new ProcesarEntradaDeDiarioCustom();
			 ProcesarEntradaDeDiarioCustom.monedaComprobante = "USD";
			 ProcesarEntradaDeDiarioCustom.monedaLocal = monedaLocal;
			 ProcesarEntradaDeDiarioCustom.fecharecibo = fecharecibo;
			 ProcesarEntradaDeDiarioCustom.tasaCambioParalela = tasaoficial; 
			 ProcesarEntradaDeDiarioCustom.tasaCambioOficial = tasaoficial; 
			 ProcesarEntradaDeDiarioCustom.montoComprobante = montoCambio;
			 ProcesarEntradaDeDiarioCustom.conceptoComprobante = "Ficha:" + numeroficha + " C:"+caid +" Comp:"+codcomp;
			 ProcesarEntradaDeDiarioCustom.numeroBatchJde = String.valueOf( numeroBatchJde );
			 ProcesarEntradaDeDiarioCustom.numeroReciboJde = String.valueOf( numeroReciboJde ) ;
			 ProcesarEntradaDeDiarioCustom.usuario = usuario;
			 ProcesarEntradaDeDiarioCustom.codigousuario = codigousuario;
			 ProcesarEntradaDeDiarioCustom.programaActualiza = PropertiesSystem.CONTEXT_NAME;
			 ProcesarEntradaDeDiarioCustom.lineasComprobante = lineasComprobante;
			 ProcesarEntradaDeDiarioCustom.valoresJdeInsContado = valoresJDEInsContado;
			 
			 ProcesarEntradaDeDiarioCustom.procesarEntradaDeDiario(session1,session1.getTransaction());
			 
			 
			 msgEstadoProceso = ProcesarEntradaDeDiarioCustom.msgProceso;
			 aplicado = msgEstadoProceso.isEmpty();
			 
			 if(!aplicado){
				 return msgEstadoProceso ;
			 }
			 
			 aplicado = crearRegistroReciboCajaJde(caid, numeroficha, codcomp, codsuc,  "FCV", "A", numeroBatchJde, Arrays.asList(new String[]{String.valueOf(numeroReciboJde)}));
			 
			 if(!aplicado){
				 msgEstadoProceso = ProcesarEntradaDeDiarioCustom.msgProceso = "No se ha creado el enlace entre documentos de caja y edwards para la ficha de cambio " ;
				 return msgEstadoProceso;
			 }
			 
			 
		} catch (Exception e) {
			LogCajaService.CreateLog("batchCompraVentaCambios", "QRY", e.getMessage());
			e.printStackTrace();
			aplicado = false;
			msgEstadoProceso = "No se ha podido grabar la ficha de compra venta por cambio";
		}
		return msgEstadoProceso;
	
	}
	
	//**********************************************************
	//----------------------------------------------------------
	//++++++++++++++++++      INICIO     +++++++++++++++++++++++
	//----------------------------------------------------------
	//**********************************************************
	//Creado por Luis Alberto Fonseca Mendez 2019-04-24
	
	public static String batchCompraVentaCambios(Session sesion, Transaction trans, int numeroficha, int numrec, int caid, 
			String codcomp, String codsuc, BigDecimal montoCambio, 
			BigDecimal tasaoficial, Date fecharecibo, String usuario,
			int codigousuario,  String monedaLocal){

		
		String msgEstadoProceso = "";
		boolean aplicado = true;
		String companiaDocumento = "00000";
	
		try {
			
			int numeroBatchJde = Divisas.numeroSiguienteJdeE1( );
			int numeroReciboJde = Divisas.numeroSiguienteJdeE1Custom( valoresJdeNumeracion[8], valoresJdeNumeracion[9] );
			
			if(numeroBatchJde == 0) {
				 aplicado = false;
				 return msgEstadoProceso = "Error al generar consecutivo número de batchs para el recibo";
				 
			}
			if (numeroReciboJde == 0) {
				aplicado = false;
				return msgEstadoProceso = "Error al generar consecutivo número de documento para el recibo";
			}
			
			List<MetodosPago>formasPago = new ArrayList<MetodosPago>();
			MetodosPago mpDolares = new MetodosPago();
			mpDolares.setMoneda("USD");
			mpDolares.setMetodo(MetodosPagoCtrl.EFECTIVO);
			formasPago.add(mpDolares);
			
			MetodosPago mpCordobas = new MetodosPago();
			mpCordobas.setMoneda("COR");
			mpCordobas.setMetodo(MetodosPagoCtrl.EFECTIVO);
			formasPago.add(mpCordobas);
			 
			List<String[]> cuentasContables = Divisas.cuentasFormasPago(formasPago, caid, codcomp);
			
			String[] dtaCuentaDolares =  (String[])
				CollectionUtils.find(cuentasContables, new Predicate(){
					@Override
					public boolean evaluate(Object o) {
						String[] dtaCuenta = (String[])o;
						return   dtaCuenta[7].compareTo( "USD" ) == 0;
					}
				});
			String[] dtaCuentaCordobas =  (String[])
				CollectionUtils.find(cuentasContables, new Predicate(){
					@Override
					public boolean evaluate(Object o) {
						String[] dtaCuenta = (String[])o;
						return   dtaCuenta[7].compareTo( "COR" ) == 0;
					}
				});
			
			String concepto = "Ficha Rc:"+numrec + " Ca:"+caid +" Comp:"+codcomp;
			companiaDocumento =  dtaCuentaDolares[2] ;
		
			//&& ========= informacion para las lineas del f0911
			 List<DatosComprobanteF0911> lineasComprobante = new ArrayList<DatosComprobanteF0911>();
			 DatosComprobanteF0911 dtaDolares = new DatosComprobanteF0911(
					 dtaCuentaDolares[1],
					 montoCambio, 
					 concepto,
					 tasaoficial,
					 "",
					 "",
					 companiaDocumento );
			 lineasComprobante.add(dtaDolares);
			 
			 DatosComprobanteF0911 dtaCordobas = new DatosComprobanteF0911(
					 dtaCuentaCordobas[1],
					 montoCambio.negate(), 
					 concepto,
					 tasaoficial,
					 "",
					 "",
					 companiaDocumento);
			 lineasComprobante.add(dtaCordobas);
			 
			 new ProcesarEntradaDeDiarioCustom();
			 ProcesarEntradaDeDiarioCustom.monedaComprobante = "USD";
			 ProcesarEntradaDeDiarioCustom.monedaLocal = monedaLocal;
			 ProcesarEntradaDeDiarioCustom.fecharecibo = fecharecibo;
			 ProcesarEntradaDeDiarioCustom.tasaCambioParalela = tasaoficial; 
			 ProcesarEntradaDeDiarioCustom.tasaCambioOficial = tasaoficial; 
			 ProcesarEntradaDeDiarioCustom.montoComprobante = montoCambio;
			 ProcesarEntradaDeDiarioCustom.conceptoComprobante = "Ficha:" + numeroficha + " C:"+caid +" Comp:"+codcomp;
			 ProcesarEntradaDeDiarioCustom.numeroBatchJde = String.valueOf( numeroBatchJde );
			 ProcesarEntradaDeDiarioCustom.numeroReciboJde = String.valueOf( numeroReciboJde ) ;
			 ProcesarEntradaDeDiarioCustom.usuario = usuario;
			 ProcesarEntradaDeDiarioCustom.codigousuario = codigousuario;
			 ProcesarEntradaDeDiarioCustom.programaActualiza = PropertiesSystem.CONTEXT_NAME;
			 ProcesarEntradaDeDiarioCustom.lineasComprobante = lineasComprobante;
			 ProcesarEntradaDeDiarioCustom.valoresJdeInsContado = valoresJDEInsContado;
			 
			
			 ProcesarEntradaDeDiarioCustom.procesarEntradaDeDiario(sesion, trans);
			 
			 msgEstadoProceso = ProcesarEntradaDeDiarioCustom.msgProceso;
			 aplicado = msgEstadoProceso.isEmpty();
			 
			 if(!aplicado){
				 return msgEstadoProceso ;
			 }
			 
			 aplicado = crearRegistroReciboCajaJde(sesion, trans, caid, numeroficha, codcomp, codsuc,  "FCV", "A", numeroBatchJde, Arrays.asList(new String[]{String.valueOf(numeroReciboJde)}));
			 
			 if(!aplicado){
				 msgEstadoProceso = ProcesarEntradaDeDiarioCustom.msgProceso = "No se ha creado el enlace entre documentos de caja y edwards para la ficha de cambio " ;
				 return msgEstadoProceso;
			 }
			 
			//**********************************************************
			//----------------------------------------------------------
			//++++++++++++++++++       FIN       +++++++++++++++++++++++
			//----------------------------------------------------------
			//**********************************************************
			 
		} catch (Exception e) {
			e.printStackTrace();
			aplicado = false;
			msgEstadoProceso = "No se ha podido grabar la ficha de compra venta por cambio";
		}
		return msgEstadoProceso;
	
	}
	
	//**********************************************************
	//----------------------------------------------------------
	//++++++++++++++++++       FIN       +++++++++++++++++++++++
	//----------------------------------------------------------
	//**********************************************************
	
	public static boolean updEstadoRecibolog( int caid, String codcomp, int codcli,
			String tiporec, long idproceso, String usuario, Date fechahora,
			int codemp, int estadoini, int estadosig) {
		Recibolog rl = null;
		Session sesion = null;
		Transaction trans = null;
		boolean valido = true;
		
		try {
			
			sesion = HibernateUtilPruebaCn.currentSession();
			
			
			Criteria cr = sesion.createCriteria(Recibolog.class)
					.add(Restrictions.eq("noregistro", idproceso))
					.add(Restrictions.eq("caid", caid))
					.add(Restrictions.eq("codcomp", codcomp))
					.add(Restrictions.eq("codclie", codcli))
					.add(Restrictions.eq("estado", estadoini))
					.add(Restrictions.eq("fecha", fechahora))
					.add(Restrictions.eq("usuario", usuario)).setMaxResults(1);
			
			rl = (Recibolog) cr.uniqueResult();
			
			if(rl == null ){
//				LogCrtl.sendLogInfo("Actualizacion fallida: Criteria de busqueda: " +  cr.toString()) ;
				return false;
			}
			
			rl.setEstado(estadosig);
			rl.setHorafinaliza(fechahora);
			sesion.update(rl);
			
		}catch(Exception e){
			valido=false; 
		}
		return valido;	
	}
	
	public static boolean grabarReciboLog(int caid, String codcomp, int codcli,
			String tiporec, long idproceso, String usuario, Date fecha, 
			int codemp, String parametros ,BigDecimal monto) {
		Session sesion = null;
		
		boolean valido = true;
		
		try {
			
			sesion = HibernateUtilPruebaCn.currentSession();
		
			Recibolog rl = new Recibolog(idproceso, caid, codcomp, codcli, parametros, 0,
					usuario, codemp, fecha, fecha, fecha,monto);
			
			sesion.save(rl);
			
		} catch (Exception e) {
			e.printStackTrace();
			valido = false;
		}
		return valido;
	}
	

	public static boolean actualizarRecibofac(List<String[]> noFacturas,
			int caid, String codcomp, int fecha, int codcli, String tiporec,
			int numrecTmp, int numrecReal) {

		Session sesion = null;
		
		boolean aplicado = true;
		Date inicio = new Date();
		String parametros = new String("");
		
		try {
			parametros = "caid: " + caid + " codcomp:'" + codcomp + "' fecha:"
				+ fecha + " codcli:" + codcli + " tiporec:'" + tiporec + "'";
			    
			sesion = HibernateUtilPruebaCn.currentSession();
		

			String update = "update " + PropertiesSystem.ESQUEMA
					+ ".Recibofac as r set r.numrec = " + numrecReal  + ", r.hora = 0 "
					+ " where r.numrec = " + numrecTmp + " and r.caid =" + caid
					+ " and r.tiporec = '" + tiporec
					+ "' and trim(r.codcomp) = '" + codcomp.trim()
					+ "' and r.estado = '' and r.fecha = " + fecha;

			LogCajaService.CreateLog("actualizarRecibofac", "QRY", update);
			
			int iCanUpdt = sesion.createSQLQuery(update).executeUpdate();
			
			if (iCanUpdt != noFacturas.size()) {
				aplicado = false;
			}
			update = null;

		} catch (Exception e) {
			LogCajaService.CreateLog("actualizarRecibofac", "ERR", e.getMessage());			
			aplicado = false;
		}
		return aplicado;
	}
	
	@SuppressWarnings("unchecked")
	public int  procesoPagoFactura(List<String[]> noFacturas, int caid,
			String codcomp, int fecha, int codcli, String tiporec) {

		Session sesion = null;
		Transaction trans = null;
		int iNumrecTmp = 0;
		Date tmInicia = new Date();
		String parametros = new String("");
		
		try {
			
			
			sesion = HibernateUtilPruebaCn.currentSession();
			trans = sesion.beginTransaction();
			
			
			
			parametros = "caid: " + caid + " codcomp:'" + codcomp + "' fecha:"
					+ fecha + " codcli:" + codcli + " tiporec:'" + tiporec
					+ "'";
			
			String sql = "( ( numfac = " + noFacturas.get(0)[0]
					+ " and monto = " + noFacturas.get(0)[1] 
					+ " and partida = '"+noFacturas.get(0)[2] + "') ";
			
			for (int i = 1; i < noFacturas.size(); i++) {
				String[] df = noFacturas.get(i);
				sql += " or ( numfac = "+df[0]+"  and  monto = "+df[1]+"  and partida = '"+df[2]+"' )" ;
			}
			sql += ")";
			
			Criteria cr = sesion
					.createCriteria(Recibofac.class)
					.add(Restrictions.eq("estado", ""))
					.add(Restrictions.eq("id.caid", caid))
					.add(Restrictions.eq("id.codcomp", codcomp))
					.add(Restrictions.eq("id.fecha", fecha))
					.add(Restrictions.eq("id.tiporec", tiporec))
					.add(Restrictions.sqlRestriction(sql));
			
			if(tiporec.compareTo("CR") == 0){
				cr.add(Restrictions.gt("id.hora", 0));
			}
			
			String sqlCriteria = LogCajaService.toSql(cr);
			LogCajaService.CreateLog("procesoPagoFactura", "QRY", sqlCriteria);
			
			List<Recibofac> rfacs  = (ArrayList<Recibofac>) cr.list(); 
			cr = null;
			
//			LogCrtl.sendLogInfo("Consulta de Recibofac "
//					+ FechasUtil.diferenciaEntreHoras(tmInicia, new Date())
//					+ " || " + parametros);
			
			if(rfacs != null && !rfacs.isEmpty() ){
//				LogCrtl.sendLogInfo(" Registros de Facturas ya grabados en recibofac: "
//						+ parametros + " || " + sql);
				return iNumrecTmp = 0 ;
			}
			
			//&& ======== grabar los registros en recibofac con un numero de recibo aleatorio.
			iNumrecTmp = Integer.parseInt(String.valueOf(Math
					.round(10000000 + Math.random() * 90000000)));

			SimpleDateFormat sdfx = new SimpleDateFormat("HHmmss");
			
			Date timeTmp = new Date();
			int i = 0;
			for (String[] df : noFacturas) {
				
				Recibofac rf = new Recibofac();
				RecibofacId id = new RecibofacId();
				
//				System.out.println("Facturas pagadas ----> " + df[0]);
				
				id.setNumfac(Integer.parseInt( df[0] ) );				
				id.setNumrec(iNumrecTmp);						
				id.setTipofactura(df[5]);			
				id.setCodcomp(codcomp);				
				id.setPartida(df[2].trim());
				id.setCaid(caid);
				id.setCodsuc(CodeUtil.pad( df[4].trim(), 5, "0") );
				id.setCodunineg(df[3].trim());
				id.setTiporec(tiporec);
				id.setCodcli(codcli);
				id.setFecha(fecha);
				id.setHora(Integer.parseInt(sdfx.format(new Date())));
				id.setOrden(i);
				
				rf.setId(id);						
				rf.setMonto(new BigDecimal( df[1] ) ); 
				rf.setEstado("");
				
				i++;
				try{
					LogCajaService.CreateLog("procesoPagoFactura", "HQRY", rf);
					sesion.save(rf);
					
				}catch(Exception e){
					e.printStackTrace();
					return iNumrecTmp = 0;
				}
			}
			
//			LogCrtl.sendLogInfo("Grabar registro en de Recibofac " 	+ FechasUtil.diferenciaEntreHoras(timeTmp, new Date())	+ " || " + parametros);
			
		} catch (Exception e) {
			e.printStackTrace();
			LogCajaService.CreateLog("procesoPagoFactura", "ERR", e.getMessage());
			iNumrecTmp = 0 ;
		}finally{
			
			if( iNumrecTmp != 0 ){
				try { trans.commit(); } 
				catch (Exception e2) { e2.printStackTrace(); iNumrecTmp = 0 ; }
			}else{
				try { trans.rollback(); } 
				catch (Exception e2) { e2.printStackTrace(); iNumrecTmp = 0 ; }
			}
			
			try {
				sesion.close(); 
			}catch(Exception e) {
				e.printStackTrace();
				iNumrecTmp = 0 ;
			}
			
			//HibernateUtilPruebaCn.closeSession();
			
//			LogCrtl.sendLogInfo(" Tiempo de Graba Temporal Recibofac  "
//					+ noFacturas.get(0)[0] + ":  "
//					+ FechasUtil.diferenciaEntreHoras(tmInicia, new Date()));
			
		}
		return iNumrecTmp;
	}
	
///
	
	@SuppressWarnings("unchecked")
	public Boolean  procesoPagoFacturaV2(List<String[]> noFacturas, int caid,
			String codcomp, int fecha, int codcli, String tiporec,int numrec) {

		Session sesion = null;
		Transaction trans = null;
		Boolean returnValue = true;
		Date tmInicia = new Date();
		String parametros = new String("");
		
		try {
			
			
			sesion = HibernateUtilPruebaCn.currentSession();
			trans = sesion.beginTransaction();
			
			
			
			parametros = "caid: " + caid + " codcomp:'" + codcomp + "' fecha:"
					+ fecha + " codcli:" + codcli + " tiporec:'" + tiporec
					+ "'";
			
			String sql = "( ( numfac = " + noFacturas.get(0)[0]
					+ " and monto = " + noFacturas.get(0)[1] 
					+ " and partida = '"+noFacturas.get(0)[2] + "') ";
			
			for (int i = 1; i < noFacturas.size(); i++) {
				String[] df = noFacturas.get(i);
				sql += " or ( numfac = "+df[0]+"  and  monto = "+df[1]+"  and partida = '"+df[2]+"' )" ;
			}
			sql += ")";
			
			Criteria cr = sesion
					.createCriteria(Recibofac.class)
					.add(Restrictions.eq("estado", ""))
					.add(Restrictions.eq("id.caid", caid))
					.add(Restrictions.eq("id.codcomp", codcomp))
					.add(Restrictions.eq("id.fecha", fecha))
					.add(Restrictions.eq("id.tiporec", tiporec))
					.add(Restrictions.sqlRestriction(sql));
			
			if(tiporec.compareTo("CR") == 0){
				cr.add(Restrictions.gt("id.hora", 0));
			}
			
			String sqlCriteria = LogCajaService.toSql(cr);
			LogCajaService.CreateLog("procesoPagoFactura", "QRY", sqlCriteria);
			
			List<Recibofac> rfacs  = (ArrayList<Recibofac>) cr.list(); 
			cr = null;
			
//			LogCrtl.sendLogInfo("Consulta de Recibofac "
//					+ FechasUtil.diferenciaEntreHoras(tmInicia, new Date())
//					+ " || " + parametros);
			
			if(rfacs != null && !rfacs.isEmpty() ){
//				LogCrtl.sendLogInfo(" Registros de Facturas ya grabados en recibofac: "
//						+ parametros + " || " + sql);
				returnValue = false;
			}
			

			SimpleDateFormat sdfx = new SimpleDateFormat("1HHmmss");
			
			Date timeTmp = new Date();
			int i = 0;
			for (String[] df : noFacturas) {
				
				Recibofac rf = new Recibofac();
				RecibofacId id = new RecibofacId();
				
//				System.out.println("Facturas pagadas ----> " + df[0]);
				
				id.setNumfac(Integer.parseInt( df[0] ) );				
				id.setNumrec(numrec);						
				id.setTipofactura(df[5]);			
				id.setCodcomp(codcomp);				
				id.setPartida(df[2].trim());
				id.setCaid(caid);
				id.setCodsuc(CodeUtil.pad( df[4].trim(), 5, "0") );
				id.setCodunineg(df[3].trim());
				id.setTiporec(tiporec);
				id.setCodcli(codcli);
				id.setFecha(fecha);
				id.setHora(Integer.parseInt(sdfx.format(new Date())));
				id.setOrden(i);
				
				rf.setId(id);						
				rf.setMonto(new BigDecimal( df[1] ) ); 
				rf.setEstado("");
				
				i++;
				try{
					LogCajaService.CreateLog("procesoPagoFactura", "HQRY", rf);
					sesion.save(rf);
					
				}catch(Exception e){
					e.printStackTrace();
					returnValue = false;
				}
			}
			
//			LogCrtl.sendLogInfo("Grabar registro en de Recibofac " 	+ FechasUtil.diferenciaEntreHoras(timeTmp, new Date())	+ " || " + parametros);
			
		} catch (Exception e) {
			e.printStackTrace();
			LogCajaService.CreateLog("procesoPagoFactura", "ERR", e.getMessage());
			returnValue = false;
		}finally{
			
			if( returnValue ){
				try { trans.commit(); } 
				catch (Exception e2) { e2.printStackTrace(); returnValue = false; }
			}else{
				try { trans.rollback(); } 
				catch (Exception e2) { e2.printStackTrace(); returnValue = false; }
			}
			
			try {
				sesion.close(); 
			}catch(Exception e) {
				e.printStackTrace();
				returnValue = false;
			}
			
			//HibernateUtilPruebaCn.closeSession();
			
//			LogCrtl.sendLogInfo(" Tiempo de Graba Temporal Recibofac  "
//					+ noFacturas.get(0)[0] + ":  "
//					+ FechasUtil.diferenciaEntreHoras(tmInicia, new Date()));
			
		}
		return returnValue;
	}
	
	public Boolean  procesoPagoFacturaV2(List<String[]> noFacturas, int caid,
			String codcomp, int fecha, int codcli, String tiporec,Session sesion,int numrec) {

		
		Boolean retunVal = true;
		
		try {
			
			
			SimpleDateFormat sdfx = new SimpleDateFormat("1HHmmss");
			
			
			int i = 0;
			for (String[] df : noFacturas) {
				
				Recibofac rf = new Recibofac();
				RecibofacId id = new RecibofacId();
				
//				System.out.println("Facturas pagadas ----> " + df[0]);
				
				id.setNumfac(Integer.parseInt( df[0] ) );				
				id.setNumrec(numrec);						
				id.setTipofactura(df[5]);			
				id.setCodcomp(codcomp);				
				id.setPartida(df[2].trim());
				id.setCaid(caid);
				id.setCodsuc(CodeUtil.pad( df[4].trim(), 5, "0") );
				id.setCodunineg(df[3].trim());
				id.setTiporec(tiporec);
				id.setCodcli(codcli);
				id.setFecha(fecha);
				id.setHora(Integer.parseInt(sdfx.format(new Date())));
				id.setOrden(i);
				
				rf.setId(id);						
				rf.setMonto(new BigDecimal( df[1] ) ); 
				rf.setEstado("");
				
				i++;
				try{
					LogCajaService.CreateLog("procesoPagoFactura", "HQRY", rf);
					sesion.save(rf);
					
				}catch(Exception e){
					e.printStackTrace();
					retunVal = false;
				}
			}
			
//			LogCrtl.sendLogInfo("Grabar registro en de Recibofac " 	+ FechasUtil.diferenciaEntreHoras(timeTmp, new Date())	+ " || " + parametros);
			
		} catch (Exception e) {
			e.printStackTrace();
			LogCajaService.CreateLog("procesoPagoFactura", "ERR", e.getMessage());
			retunVal = false ;
			
		}
		return retunVal;
	}
	@SuppressWarnings("unchecked")
	public Boolean  procesoPagoFacturaWithSession(List<String[]> noFacturas, int caid,
			String codcomp, int fecha, int codcli, String tiporec,Session s,int numrec) {
		
		SimpleDateFormat sdfx = new SimpleDateFormat("HHmmss");
		Boolean returValue= true;
		try {
			int i=0;
			for (String[] df : noFacturas) {		
				
				
String query="INSERT INTO "+PropertiesSystem.ESQUEMA+".RECIBOFAC( NUMREC, NUMFAC, ORDEN, CODCOMP, PARTIDA, MONTO, TIPOFACTURA,\r\n" + 
		"  CODSUC, CAID, ESTADO, CODUNINEG, TIPOREC, CODCLI, FECHA, HORA)\r\n" + 
		"  VALUES ("+numrec+","+Integer.parseInt( df[0])+","+i+",'"+codcomp+"',"
				+ "'"+df[2].trim()+"',"+df[1]+",'"+df[5]+"','"+CodeUtil.pad( df[4].trim(), 5, "0")+"',"
				+ caid+",'','"+df[3].trim()+"','"+tiporec+"',"+codcli+","+fecha+","+Integer.parseInt(sdfx.format(new Date()))+")";
returValue=ConsolidadoDepositosBcoCtrl.executeSqlQueryTx(s, query);					
		i++;		
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			LogCajaService.CreateLog("procesoPagoFacturaWithSession", "ERR", e.getMessage());
			returValue= false;
			
		}
		
		return returValue;
	}

///
	
	//Hecho por LFonseca 2020-02-07
	//Validador para evitar que se cancele dos veces una factura
	@SuppressWarnings("unchecked")
	public int  procesoPagoFactura2(List<String[]> noFacturas, int caid,
			String codcomp, int fecha, int codcli, String tiporec) {

		Session sesion = null;		
		int iNumrecTmp = 0;
		@SuppressWarnings("unused")
		String parametros = new String("");
		
		try {
			
			
			sesion = HibernateUtilPruebaCn.currentSession();
						
			parametros = "caid: " + caid + " codcomp:'" + codcomp + "' fecha:"
					+ fecha + " codcli:" + codcli + " tiporec:'" + tiporec
					+ "'";
			
			String sql = "( ( numfac = " + noFacturas.get(0)[0]
					+ " and monto = " + noFacturas.get(0)[1] 
					+ " and partida = '"+noFacturas.get(0)[2] + "') ";
			
			for (int i = 1; i < noFacturas.size(); i++) {
				String[] df = noFacturas.get(i);
				sql += " or ( numfac = "+df[0]+"  and  monto = "+df[1]+"  and partida = '"+df[2]+"' )" ;
			}
			sql += ")";
			
			Criteria cr = sesion
					.createCriteria(Recibofac.class)
					.add(Restrictions.eq("estado", ""))
					.add(Restrictions.eq("id.caid", caid))
					.add(Restrictions.eq("id.codcomp", codcomp))
					.add(Restrictions.eq("id.fecha", fecha))
					.add(Restrictions.eq("id.tiporec", tiporec))
					.add(Restrictions.sqlRestriction(sql));
			
			if(tiporec.compareTo("CR") == 0){
				cr.add(Restrictions.gt("id.hora", 0));
			}
			
			LogCajaService.CreateLog("procesoPagoFactura2", "HQRY", LogCajaService.toSql(cr));
			
			List<Recibofac> rfacs  = (ArrayList<Recibofac>) cr.list(); 
			cr = null;
			

			if(rfacs != null && !rfacs.isEmpty() ){
				return iNumrecTmp = 0 ;
			}
			
			//&& ======== grabar los registros en recibofac con un numero de recibo aleatorio.
			iNumrecTmp = Integer.parseInt(String.valueOf(Math
					.round(10000000 + Math.random() * 90000000)));
			
		} catch (Exception e) {
			LogCajaService.CreateLog("procesoPagoFactura2", "ERR", e.getMessage());
			iNumrecTmp = 0 ;
		}
		
		return iNumrecTmp;
	}
	
	/**
	 * Funcion encargada de busrcar todos los recibos temporales
	 * @param iCaid Numero de caja en la que se procesaron los recibos
	 * @return Lista de arreglos de objetos [numero recibo y hora de grabacion].
	 */
	@SuppressWarnings({ "unchecked" })
	public List<Object[]> buscarRecibosTemporales(int iCaid){
		List<Object[]> lstReturn = new ArrayList<Object[]>();
		 Session session = null;
		
		 
		 try{
			
			 session = HibernateUtilPruebaCn.currentSession();
			
			Query q = session
					.createSQLQuery("SELECT NUMREC, HORA FROM  "
							+ PropertiesSystem.ESQUEMA
							+ ".RECIBOFAC WHERE CAID = :iCaid AND HORA <> 0"
							+ " ORDER BY NUMREC DESC");
			
			q.setInteger("iCaid", iCaid);
		     lstReturn = q.list();
			
		 }catch(Exception ex){
			 LogCajaService.CreateLog("buscarRecibosTemporales", "ERR", ex.getMessage());
		 }
		return lstReturn;
	}
	
	
	
	/**************************************************
	 *  Método: CRPMCAJA / com.casapellas.controles /existeDevolucionRecibo
	 *  Descrp: 
	 *	Fecha:  Oct 3, 2012 
	 *  Autor:  CarlosHernandez
	 ***/
	public Recibo existeDevolucionRecibo(Date dtFecha, int iNumrec, int iCaid, 
							String sCodcomp, String sCodsuc, String sTiporec ) {
		Recibo rDevolucion = null;
		Session session = null;
		Transaction trans = null;
		boolean newCn = false;
		
		try {
			
			session = HibernateUtilPruebaCn.currentSession();
			
			Criteria cr = session.createCriteria(Recibo.class);
			cr.add(Restrictions.eq("nodoco",  iNumrec));
			cr.add(Restrictions.eq("tipodoco",sTiporec ));
			cr.add(Restrictions.eq("fecha", dtFecha));
			cr.add(Restrictions.eq("estado", ""));
			cr.add(Restrictions.eq("id.codsuc",sCodsuc ));
			cr.add(Restrictions.eq("id.caid", iCaid));
			cr.add(Restrictions.eq("id.codcomp", sCodcomp));
			cr.setMaxResults(1);			
			
			rDevolucion = (Recibo)cr.uniqueResult();
			
		} catch (Exception e) {
			rDevolucion = null;
			e.printStackTrace(); 
		}
		
		return rDevolucion;
	}
	/* ******************** fin de metodo existeDevolucionRecibo ****************************/
	
	public boolean actualizarRecibofac(Session sesion, int iCaid,
			String sCodcomp, int iNumfac, String sTipofac, String sTiporec,
			int iNumrec, String sEstado, String sCodunineg, int iCodcli, int iFecha) {
		boolean bHecho = true;

		try {
			Criteria cr = sesion.createCriteria(Recibofac.class);
			cr.add(Restrictions.eq("id.numfac", iNumfac));
			cr.add(Restrictions.eq("id.tipofactura", sTipofac));
			cr.add(Restrictions.eq("id.caid", iCaid));
			cr.add(Restrictions.eq("id.codcomp", sCodcomp));
			cr.add(Restrictions.eq("id.numrec", iNumrec));
			cr.add(Restrictions.eq("id.tiporec", sTiporec));
			cr.add(Restrictions.sqlRestriction( " trim(codunineg) = '"+sCodunineg.trim()+"'" ) );
			cr.add(Restrictions.eq("id.codcli", iCodcli));
			cr.add(Restrictions.eq("id.fecha", iFecha));
			cr.add(Restrictions.eq("id.partida", ""));

			Recibofac rf = (Recibofac) cr.uniqueResult();
			if (rf == null)
				return false;

			rf.setEstado(sEstado);
			sesion.update(rf);

		} catch (Exception e) {
			bHecho = false;
		}
		return bHecho;
	}
	public Recibofac getRecibofac(Session sesion, int iCaid, String sCodcomp,
			int iNumfac, String sTipofac, String sCodunineg, int iCodcli, int iFecha) {
		Recibofac rf = null;

		try {

			Criteria cr = sesion.createCriteria(Recibofac.class)
			.add(Restrictions.eq("id.numfac", iNumfac))
			.add(Restrictions.eq("id.tipofactura", sTipofac))
			.add(Restrictions.eq("id.caid", iCaid))
			.add(Restrictions.eq("id.codcomp", sCodcomp))
			.add(Restrictions.eq("id.partida", ""))
			.add(Restrictions.eq("id.codcli", iCodcli))
			.add(Restrictions.eq("id.fecha", iFecha))
			.add(Restrictions.eq("estado", ""))
			.add(Restrictions.sqlRestriction(" trim(codunineg) = '"+sCodunineg.trim()+"'" ));

			rf = (Recibofac) cr.uniqueResult();

		} catch (Exception e) {
			rf = null;
		}
		return rf;
	}
/*******************************************************************************************************************************************/
	 public boolean actualizarEstadoRecibo(int iNumrec,int iCaid,String sCodsuc, String sCodcomp,String sCodUsera,String sMotivo, String sTipoRec){
		 boolean bActualizado = true;
		 Session session = null;
		 Transaction tx = null;
		 
		 try{
			 session = HibernateUtilPruebaCn.currentSession();
			 tx = session.beginTransaction();
			 Query q = session
					.createQuery("update Recibo as r set r.estado" +
							" =:sEstado, r.codusera=:sCodUsera, " +
							"r.motivo=:sMotivo where r.id.numrec=:iNumrec " +
							"and r.id.caid=:iCaid and r.id.codcomp=:sCodcomp" +
							" and r.id.codsuc=:sCodsuc and r.id.tiporec=:sTipoRec");
		     q.setString("sEstado", "A");
		     q.setString("sCodUsera", sCodUsera);
		     q.setString("sMotivo", sMotivo);
		        
		     q.setInteger("iNumrec",iNumrec);
		     q.setInteger("iCaid",iCaid);
		     q.setString("sCodcomp", sCodcomp);
		     q.setString("sCodsuc", sCodsuc);
		     q.setString("sTipoRec", sTipoRec);
		       
		     int rowCount = q.executeUpdate();
		     if (rowCount == 0){
		       bActualizado = false;
		      }
			 tx.commit();
		 }catch(Exception ex){
			 
			 bActualizado = false;
		 }
		 return bActualizado;
	 }
/*******Obtiene La info DETALLE DE RECIBO************************************************************************************************************************************/
		public Recibodet leerPagoRecibo(int iCaid,String sCodSuc,String sCodComp,int iNumrec, String sTipoRec,String pago,
										String ref1,String ref2,String ref3,String ref4,String moneda){
			Recibodet rd = null;
			Session session = HibernateUtilPruebaCn.currentSession();
			
			String sql = "";
			try{
				sql = "from Recibodet as r where r.id.caid = "+iCaid+" and r.id.codsuc = '" +sCodSuc+"'" +
				" and r.id.codcomp = '"+sCodComp.trim()+"' and r.id.numrec = " + iNumrec +" and r.id.tiporec = '" +sTipoRec+"' and " +
				" r.id.moneda = '" + moneda + "' and trim(r.id.refer1) = '" +ref1.trim()+ "' and trim(r.id.refer2) = '" +ref2.trim()+"' " +
				" and trim(r.id.refer3) ='" + ref3.trim() + "' and trim(r.id.refer4) ='"+ref4.trim()+"' and r.id.moneda = '" +moneda+"'";
				
			
				rd = (Recibodet)session.createQuery(sql).uniqueResult();
			
				
			}catch(Exception ex){
				
			}
			return rd;
		}
	/******************************************************************************************************/
	public boolean actualizarReferenciasRecibo(int iNumrec,int iCajaId,String sCodComp,String scodsuc,
										String tiporec,List<MetodosPago> lstMetodosPago){
		String sql = "";
		boolean hecho = true;
		Session sesion = null;
		try{
			
			sesion = HibernateUtilPruebaCn.currentSession();
			Transaction trans = sesion.beginTransaction();

			Query q = null;
			int iActualizado = 0;
			for (MetodosPago mp : lstMetodosPago) {
				if(mp.getMetodo().equals(MetodosPagoCtrl.TARJETA) && mp.getVmanual().compareTo("2") == 0){
					sql  = " UPDATE "+PropertiesSystem.ESQUEMA+".RECIBODET SET ";
					sql += " REFER3 = '" + mp.getReferencia3() +"',"; 
					sql += " REFER4 = '" + mp.getReferencia4() +"',";
					sql += " REFER5 = '" + mp.getReferencia5() +"',";
					sql += " REFER6 = '" + mp.getReferencia6() +"',";
					sql += " REFER7 = '" + mp.getReferencia7() +"',";
					sql += " NOMBRE = '" +mp.getNombre()+"'";
					sql += " WHERE numrec = " + iNumrec;
					sql += " and caid = " + iCajaId + " and codcomp = '" +sCodComp+"'";
					sql += " and codsuc = '" +scodsuc+"' and tiporec = '" +tiporec+"'";
					sql += " and mpago = '" +mp.getMetodo()+ "'";
					sql += " and moneda = '" +mp.getMoneda()+"'";
					sql += " and monto = " + mp.getMontopos();
			
					q = sesion.createSQLQuery(sql);
					iActualizado = q.executeUpdate();
					if(iActualizado != 1){
						error = new Exception("@Error de actualizacion de parametros de recibo con pago de Socket Pos");
						hecho = false;
						break;
					}
				}
			}
			if(hecho)
				trans.commit();
			else
				trans.rollback();

			
			
		}catch(Exception ex){
			hecho = false;
		
		}
		return hecho;
	}
/******************************************************************************************************/
	@SuppressWarnings("unchecked")
	public Recibo getReciboDevolucionxRecibo(int caid,int numrec,String codcomp,String codsuc,String tiporec,Date dtfecha){
		Session s = null;
		
		String sql = "";
		Recibo r = null;
		try{
			
			sql = "from Recibo r where r.id.caid = "+caid+
				  " and r.estado <> 'A'"+
				  " and r.nodoco = "+numrec+" and r.id.codcomp = '"+codcomp+"'" +
				  " and r.id.codsuc = '"+codsuc+"' and r.id.tiporec = '"+tiporec+"'"+
				  " and r.fecha = '"+new FechasUtil().formatDatetoString(dtfecha, "yyyy-MM-dd")+"'" ;
			
			s = HibernateUtilPruebaCn.currentSession();

 			List<Recibo> lista = s.createQuery(sql).setMaxResults(1).list();

			if(lista != null && !lista.isEmpty() )
				r = lista.get(0);
				
				
		}catch(Exception ex){
			r = null;
		
		}
		return r;
	}
/******************************************************************************************************/
	public boolean aplicaVariasFacturas(int caid,int numrec,String codcomp,String codsuc,String tiporec){
		boolean existe = false;
		PreparedStatement ps = null;
		String sql = "";
		Connection cn = null;
		int cant = 0;
		try{
			sql = "select count(*) CANT from "+PropertiesSystem.ESQUEMA+".recibofac where " +
					"caid = "+caid+" and numrec = "+numrec+" and codcomp = '"+codcomp+"' and codsuc = '"+codsuc+"' and tiporec = '"+tiporec+"'";
			cn = As400Connection.getJNDIConnection("DSMCAJAR");
			
			ps = cn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if (rs.next()){
				cant = rs.getInt("CANT");	
			}
			
			if(cant > 1)existe=true;
		}catch(Exception ex){
		
		}finally {
			try {
				ps.close();
				cn.close();
			} catch (Exception se2) {
			
			}
		}
		return existe;
	}
/******************************************************************************************************/
	public String leerTipodocRecibo(int nobatch){
		String tipodoc = "";
		PreparedStatement ps = null;
		String sql = " select cast(rpdct as varchar(2) CCSID 37) RPDCT from "+PropertiesSystem.JDEDTA+".f0311 where rpicu =  " +nobatch + " and rpdctm = 'RC'";
		Connection cn = null;
		try{
			cn = As400Connection.getJNDIConnection("DSMCAJAR");
			
			ps = cn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if (rs.next()){
				tipodoc = rs.getString("RPDCT");	
			}
		}catch(Exception ex){
		
		}finally {
			try {
				ps.close();
				cn.close();
			} catch (Exception se2) {
			
			}
		}
		return tipodoc;
	}
/******************************************************************************************************/
	public boolean editarReintegro(Session s, Reintegro r){
		boolean hecho = true;
		
		try{			
			s.update(r);
		}catch(Exception ex){
			hecho = false;
			error = new Exception("@LOGCAJA: error de sistema al editar el reintegro");
			errorDetalle = ex;
			ex.printStackTrace(); 
		}
		return hecho;
	}
/******************************************************************************************************/
	public static Reintegro verificarSiTieneReintegro(int noarqueo,int caid,String codcomp,String codsuc, String sMoneda){	
		Reintegro r = null;
 
		try{
			
			String sql = " from Reintegro as r where r.id.caid = " + caid
					+ " and r.id.codcomp = '" + codcomp
					+ "' and r.id.codusc = " + "'" + codsuc
					+ "' and r.narqueo = " + noarqueo + " and r.moneda = '"
					+ sMoneda + "'";
			
			LogCajaService.CreateLog("verificarSiTieneReintegro", "QRY", sql);
			r = ConsolidadoDepositosBcoCtrl.executeSqlQueryUnique(sql, Reintegro.class, false);
			
		}catch(Exception ex){ 
			LogCajaService.CreateLog("verificarSiTieneReintegro", "ERR", ex.getMessage());
		}
		return r;
	}
/******************************************************************************************************/
	public boolean grabarReintegro(Session s,Transaction tx,int caid,String codcomp, String codsuc, int noreint, double monto, 
					int nobatch,int nodoc,String usrmod, int noarqueo,String moneda){
		boolean hecho = false;
		Reintegro r = null;
		ReintegroId rid = null;
		try{
			r = new Reintegro();
			rid = new ReintegroId();
			
			rid.setCaid(caid);
			rid.setCodcomp(codcomp);
			rid.setCodusc(codsuc);
			rid.setNoreint(noreint);
			
			r.setId(rid);
			
			r.setEstado(false);
			r.setFecha(new Date());
			r.setFechareint(new Date());
			r.setMonto(new BigDecimal(monto));
			r.setNobatch(nobatch);
			r.setNodoc(nodoc);
			r.setUsrmod(usrmod);
			r.setNarqueo(noarqueo);
			r.setMoneda(moneda);
			
			s.save(r);
			hecho = true;
		}catch(Exception ex){
		
		}
		return hecho;
	}
	
/******************************************************************************************/
/** Método: Obtener el periodo fiscal de un asiento de diario. 
 *	Fecha:  12/10/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	@SuppressWarnings("unchecked")
	public int[] obtenerPeriodoMesFiscalBatch(int iNobatch, int iNodocumento){
		int iAnioMesPerFiscal[] = null;
		List<Object[]> lstResulF0911 = null;
		String sql = "";
		Session sesionCajaR = null;
		
		try {
			sql = " SELECT ";
			sql += " CAST(GLFY AS NUMERIC (2) ),";
			sql += " CAST(GLPN AS NUMERIC (2) )";
			sql += " FROM ALTDTA.F0911 F09";
			sql += " WHERE F09.GLICU = "+iNobatch+" AND F09.GLDOC = "+iNodocumento;
		
			sesionCajaR = HibernateUtilPruebaCn.currentSession();
			
			
			lstResulF0911 = sesionCajaR.createSQLQuery(sql).list();
			if(lstResulF0911 == null || lstResulF0911.size()==0 ){
				lstResulF0911 = null;
				error = new Exception("@F0911: No se ha podido obtener datos de anio y mes fiscal para batch # "+iNobatch); 
				errorDetalle = error;
			}else{
				iAnioMesPerFiscal = new int[2];	
				iAnioMesPerFiscal[0] = Integer.parseInt( String.valueOf( lstResulF0911.get(0)[0] ) );
				iAnioMesPerFiscal[1] = Integer.parseInt( String.valueOf( lstResulF0911.get(0)[1] ) );
			}
			
		
		} catch (Exception e) {
			iAnioMesPerFiscal = null;
			errorDetalle = e; 
			error = new Exception("@F0911: Error de sistema al intentar obtener año y mes fiscal del batch #"+iNobatch);
			System.out.println(":ReciboCtrl():  Excepción capturada en obtenerPeriodoMesFiscalBatch(): "+e);
		}
		return iAnioMesPerFiscal;
	}
/******************************************************************************************************/
/** Método: Actualizar el valor para la referencia del depósito transferencias y depositos( minuta de dep).
 *	Fecha:  02/12/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 **/
	public boolean actualizarReferencia8N(VrecibosxtipompagoId id, String sReferVieja){
		boolean bHecho = true;
 
		try {
			
			String sql = " UPDATE "+PropertiesSystem.ESQUEMA+".RECIBODET r \n";
			
			if(id.getMpago().compareTo(MetodosPagoCtrl.TRANSFERENCIA) == 0 )
				sql += " SET r.refer3 = '" +id.getRefer2()+ "' ";
			else
				sql += " SET r.refer2 = '" +id.getRefer2()+ "' ";
			
			sql += ", referencenumber = " + id.getRefer2() ;
			
			sql += " where r.caid = "  +id.getCaid() +" and r.numrec = " +id.getNumrec();
			sql += " and trim(r.codcomp) ='" +id.getCodcomp().trim() + "' and r.tiporec = '" +id.getTiporec()+ "'";
			sql += " and r.monto = " +id.getMonto().toString() + " and r.refer1 = '" +id.getRefer1()+ "'";
			sql += " and r.mpago = '"  +id.getMpago()+  "' and r.moneda = '"  +id.getMoneda()+ "'";
			sql += " and r.referencenumber = " +sReferVieja ; 
		 
			bHecho = ConsolidadoDepositosBcoCtrl.executeSqlQueryTx(null, sql);
			
			if(!bHecho){
				error = new Exception("@RECIBODET: No hay registros afectados en la actualización");
				return bHecho ;
			}

		} catch (Exception e) {
			bHecho = false;
			error = new Exception("@RECIBODET: Error de aplicacion, no se pudo actualizar el registro ");
			errorDetalle = e;
			e.printStackTrace();
		} 
		return bHecho;
	}
/******************************************************************************************************************************/
	public boolean verificarSitieneLiga(int iNobatch, int iNodoc, int iCodcli, String sLigaDoc){
		boolean tieneLiga = false;
		Connection cn = null;
		PreparedStatement ps = null;
		String sql = "";
		int iNodocumento = 0;
		try{
			cn = As400Connection.getJNDIConnection("DSMCAJAR");
			
			sql = "select rpdoc from "+PropertiesSystem.JDEDTA+".f0311 where rpan8 = "+iCodcli+" and rpdocm = "+iNodoc+" and rpicu = "+iNobatch+" and rpdct = 'RU'";
			
			ps = cn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()){
				iNodocumento = rs.getInt("rpdoc");
			}
			if(iNodocumento > 0){
				sql = "select rpdoc from "+PropertiesSystem.JDEDTA+".f0311 where rpan8 = "+iCodcli+" and rpdoc = "+iNodocumento+" and rpdct = 'RU' and  rpdctm = '"+sLigaDoc+"'";
				ps = cn.prepareStatement(sql);
				rs = ps.executeQuery();
				if (rs.next()){
					tieneLiga = true;
				}
			}
		}catch(Exception ex){
			tieneLiga = true;
			System.out.println("Se capturo una excepcion en ReciboCtrl.verificarSitieneLiga: " + ex);
		}finally {
			try {
				ps.close();
				cn.close();
			} catch (Exception se2) {
				System.out.println("ERROR: Failed to close connection en ReciboCtrl.verificarSitieneLiga: " + se2);
			}
		}
		return tieneLiga;
	}
/*********************************************************************************************************/
	public List<String> getTransAfiliadosxDia(int iCaid, String sCodcomp,Date Fecha, String sMoneda,Date dtHini){
		List<String> lstResult = new ArrayList<String>();
		String sql = "",sFecha = "",sHini = "", sHfin = "";
		FechasUtil f = new FechasUtil();
		Connection cn = null;
		PreparedStatement ps = null;
		try{
			cn = As400Connection.getJNDIConnection("DSMCAJAR");
			
			sFecha = f.formatDatetoString(Fecha,  "yyyy-MM-dd");
			sql = "select distinct refer1 from "+PropertiesSystem.ESQUEMA+".recibo r inner join " +
					""+PropertiesSystem.ESQUEMA+".recibodet rd on (r.tiporec = rd.tiporec and " +
					"r.codsuc = rd.codsuc and r.caid = rd.caid  and r.codcomp" +
					" = rd.codcomp and r.numrec = rd.numrec ) where r.caid = "
					+iCaid+" and r.codcomp = '"+sCodcomp+"' and rd.mpago = '"+MetodosPagoCtrl.TARJETA+"'" +
					" and rd.vmanual = '2' and r.fecha = '"+sFecha+"' " +
					"and rd.moneda = '"+sMoneda+"'";
			
			if(dtHini!=null){			
				sHini = f.formatDatetoString(dtHini, "HH.mm.ss");
				sHfin = f.formatDatetoString(Fecha,"HH.mm.ss");
				sql = sql + " and r.hora >= '"+sHini+"' and r.hora <= '"+sHfin+"'";	 
			}
			
			
			ps = cn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()){
				lstResult.add(rs.getString("refer1"));
			}
		}catch(Exception ex){
			System.out.println("Se capturo una excepcion en ReciboCtrl.getTransAfiliadosxDia: " + ex);
		}finally {
			try {
				ps.close();
				cn.close();
			} catch (Exception se2) {
				System.out.println("ERROR: Failed to close connection en ReciboCtrl.getTransAfiliadosxDia: " + se2);
			}
		}
		return lstResult;
	}
	/*********************************************************************************************************/
	public boolean validarSiExisteRM(int iNodoc,String sCodsuc, String sTipodoc ){
		boolean existe = false;
		Connection cn = null;
		PreparedStatement ps = null;
		String sql = "";
		try{
			sql = "select * from "+PropertiesSystem.JDEDTA+".f0311 where rpdoc = " + iNodoc
					+ " and rpdct = '" + sTipodoc + "' and rpkco = '" 
					+ sCodsuc+ "'";
			cn = As400Connection.getJNDIConnection("DSMCAJAR");
			ps = cn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				existe = true;
		}catch(Exception ex){
			existe = false;
			System.out.println("Se capturo una excepcion en ReciboCtrl.validarSiExisteRM: " + ex);
		}finally{
			try{cn.close();}catch(Exception ex2){ex2.printStackTrace();};
		}
		return existe;
	}
	
	public String leerEstadoDetalleAD(int iNobatch, int iCodigoCli, String tipo){
		String estado = new String("");
		
		try {
 
			String sql = " SELECT DISTINCT ( GLPOST ) FROM "+PropertiesSystem.JDEDTA
					+".F0911 WHERE GLICU =" +iNobatch + " AND GLAN8 = "+iCodigoCli
					+" AND GLICUT = '"+tipo+"'";
			
			Object ob = ConsolidadoDepositosBcoCtrl.executeSqlQueryUnique(sql, null, true);
			estado = String.valueOf( ob );
			
		} catch (Exception e) { 
			e.printStackTrace();
		} 
		return estado;
	}
	/*****OBTIENE EL ESTADO DEL DETALLE DE UN RECIBO EN EL JDE******************************************************************************************************************/
	public String leerEstadoDetalleAsiento(int iNoBatch){
		String sEstado = null;
		
		 
		try{
			String sql = "SELECT DISTINCT ( GLPOST ) FROM "+PropertiesSystem.JDEDTA+".F0911 WHERE GLICU =" +iNoBatch + " AND GLICUT = 'G'";
			
			return sEstado = (String)ConsolidadoDepositosBcoCtrl.executeSqlQueryUnique(sql, null, true);
			 
		}catch(Exception ex){
			ex.printStackTrace(); 
		} 
		return sEstado;
	}
/******************************************************************************************************/
/*****OBTIENE EL ESTADO DEL DETALLE DE UN RECIBO EN EL JDE******************************************************************************************************************/
	public String leerEstadoDetalleRec(int iNoBatch){
		String sEstado = null;
		PreparedStatement ps = null;
		String sql = "SELECT DISTINCT CAST(RPPOST AS VARCHAR (1) CCSID 37) AS RPPOST FROM "+PropertiesSystem.JDEDTA+".F0311 WHERE RPICU =" +iNoBatch;
		Connection cn = null;
		try{
			cn = As400Connection.getJNDIConnection("DSMCAJAR");
			 
			ps = cn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if (rs.next()){
				sEstado = rs.getString("RPPOST");	
			}
		}catch(Exception ex){
			System.out.println("Se capturo una excepcion en ReciboCtrl.leerEstadoDetalleRec: " + ex);
		}finally {
			try {
				ps.close();
				cn.close();
			} catch (Exception se2) {
				System.out.println("ERROR: Failed to close connection en ReciboCtrl.leerEstadoDetalleRec: " + se2);
			}
		}
		return sEstado;
	}
	
	public String leerEstadoDetalleRec(int iNoBatch, String sTipoRec){
		String sEstado = new String();
		Session sesion = null;
		Transaction trans = null;
		boolean bNuevaSesion = false;
		
		try {
			sesion = HibernateUtilPruebaCn.currentSession();
			if(sesion.getTransaction().isActive())
				trans = sesion.getTransaction();
			else{
				trans = sesion.beginTransaction();
				bNuevaSesion = true;
			}
			
			String sql = "SELECT DISTINCT CAST(RPPOST AS VARCHAR (1) CCSID 37)" +
							" AS RPPOST FROM "+PropertiesSystem.JDEDTA+
							".F0311 WHERE RPICU =" +iNoBatch +" and RPICUT =" +
							" '"+sTipoRec+"' FETCH FIRST ROWS ONLY ";
			sEstado = String.valueOf(sesion.createSQLQuery(sql).uniqueResult());
			
		} catch (Exception e) {
			System.out.println("CRPMCAJA.Error en reciboCtrl.leerEstadoDetalleRec ");
			e.printStackTrace();
		}finally{
			if(bNuevaSesion){
				try{trans.commit();}catch(Exception e){}
				try{HibernateUtilPruebaCn.closeSession(sesion);}catch(Exception e){}
			}
		}
		return sEstado;
	}
	
	
	public String leerEstadoDetalleRec(int iNoBatch, String sTipoRec, int codigocli){
		String sEstado = new String();
		
		try {
			
			String sql = "SELECT DISTINCT RPPOST FROM "+PropertiesSystem.JDEDTA+
							".F03B11 WHERE RPICU =" +iNoBatch  + 
							" and RPAN8 = "+codigocli + 
							" and RPICUT =" +
							" '"+sTipoRec+"' FETCH FIRST ROWS ONLY ";
			
			System.out.println("sql anulacion: "+sql);
			
			Object ob = ConsolidadoDepositosBcoCtrl.executeSqlQueryUnique(sql, null, true);
			sEstado = String.valueOf( ob );
			
			
		} catch (Exception e) {
			e.printStackTrace(); 
		} 
		return sEstado;
	}
	
/******************************************************************************************************/
/******************************************************************************************************/
	
	public boolean verificarReciboArqueo(int caid,String tiporec,int iNumrec,String sCodsuc, 
										 String sCodcomp, String sTipoDoc, Date dtFecha, String sMoneda){
		boolean bExiste = true;
		
		
		try {
			
			String sql = " select ar.* from "+PropertiesSystem.ESQUEMA+".arqueo a inner join "+PropertiesSystem.ESQUEMA+".arqueorec ar"
			+ " on a.caid = ar.caid and a.codcomp = ar.codcomp and a.codsuc = ar.codsuc and a.noarqueo = ar.noarqueo"
			+ " where ar.caid = "+caid+" and a.fecha = '"+  FechasUtil.formatDatetoString(dtFecha, "yyyy-MM-dd")+"'"
			+ " and trim(ar.codcomp) = '"+sCodcomp.trim()+"' and ar.numrec = "+iNumrec+ " and ar.codsuc = '" +sCodsuc + "'"
			+ " and ar.tiporec = '"+ tiporec+ "' and ar.tipodoc = '" + sTipoDoc +"'"
			+ " and a.estado <>'R' "  ;
			
			if( !sMoneda.isEmpty() )
				sql +=  " and a.moneda = '"+sMoneda+"'";
			
			@SuppressWarnings("unchecked")
			List<Arqueo> arqueos = (List<Arqueo>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, false, Arqueo.class);
			
			if(arqueos == null)
				return bExiste = false;
			else
				return bExiste = true;
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		
		return bExiste;
	}
/******************************************************************************************************/
/** Método: actualizar un registro de recibodet .
 *	Fecha:  24/09/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 **/
	public boolean actualizarRecibodet(Recibodet rd,String sRfrNueva){
		boolean bHecho = true;
		Session sesion = null;
		Transaction trans = null;
		String sql="";
		RecibodetId rdId = new RecibodetId();		
		
		try {
			rdId = rd.getId(); 
			
			sql = " UPDATE "+PropertiesSystem.ESQUEMA+".RECIBODET r \n";
			sql +=" SET r.refer1 = '" +sRfrNueva+ "' \n";
			sql +=" where r.caid = "  +rdId.getCaid() +" and r.numrec = " +rdId.getNumrec() +" and r.monto = "+rd.getMonto();
			sql +=" and r.codcomp ='" +rdId.getCodcomp()+ "' and r.codsuc = '" +rdId.getCodsuc()+ "'";
			sql +=" and r.codsuc = '" +rdId.getCodsuc()+ "' and r.tiporec = '" +rdId.getTiporec()+ "'";
			sql +=" and r.refer4 = '" +rdId.getRefer4()+ "' and r.refer3 = '" +rdId.getRefer3()+ "'";
			sql +=" and r.refer2 = '" +rdId.getRefer2()+ "' and r.refer1 = '" +rdId.getRefer1()+ "'";
			sql +=" and r.mpago = '"  +rdId.getMpago()+ "' and r.moneda = '"  +rdId.getMoneda()+ "'";
			
			sesion = HibernateUtilPruebaCn.currentSession();
			trans = sesion.beginTransaction();
			Query q = sesion.createSQLQuery(sql);
			int iActualizado = q.executeUpdate();
			trans.commit();
			sesion.close();
			
			if(iActualizado!=1){
				bHecho = false;
			}
		} catch (Exception error) {
			bHecho = false;
			System.out.println("Error en ReciboCtrl.actualizarRecibodet " + error);
		}		
		return bHecho;
	}
/*****realizar busqueda de recibos pagados con tarjetas para reporte de retencion de IR*****/
	public List<ReporteIR> buscarRecibosPagadosTC(String caid,String sMoneda,Date Fechaini,Date FechaFin){
		List<ReporteIR> lstResult = new ArrayList<ReporteIR>();
		List lstR = new ArrayList();
		Session s = HibernateUtilPruebaCn.currentSession();
		
		String sql = "from Vretencion as v where v.id.fecha >= :pFechai and v.id.fecha <= :pFechaf and v.id.moneda = :pMoneda ";
		ReporteIR ir = null;
		Vretencion vret = null;
		double dmontoComision = 0;
		Divisas d = new Divisas();
		TasaCambioCtrl tc = new TasaCambioCtrl();
		try{
			if(!caid.equals("SCA")){
				sql = sql + " and v.id.caid = " + caid;
			}
			sql = sql + " order by v.id.codcomp, v.id.codunineg";
		
			lstR = s.createQuery(sql)
						.setDate("pFechai", Fechaini)
						.setDate("pFechaf", FechaFin)
						.setString("pMoneda", sMoneda)
						.list();
		
			
			for(int i = 0 ; i < lstR.size(); i ++){
				vret = (Vretencion)lstR.get(i);
				ir = new ReporteIR();
				ir.setNo(i+1);
				ir.setCaid(vret.getId().getCaid());
				ir.setCodcomp(vret.getId().getCodcomp());
				ir.setCodunineg(vret.getId().getCodunineg().trim());
				ir.setUnineg(vret.getId().getUnineg());
				ir.setCodcomp(vret.getId().getCodcomp());
				ir.setNomcomp(vret.getId().getNomcomp());
				ir.setTasa(vret.getId().getTasa().doubleValue());
				
				if(vret.getId().getTiporec().equals("CO")){
					
					ir.setReferencia(vret.getId().getNofactura().intValue());
					ir.setTipodocumento(vret.getId().getTipofactura().trim());
					if(vret.getId().getIva().doubleValue() == 0){
						ir.setExenta(0);
						ir.setExonerada(vret.getId().getTotal().doubleValue());
						ir.setVenta(0);
						ir.setIva(0);
						ir.setTotal(0);
					}else{
						ir.setExenta(0);
						ir.setExonerada(0);
						ir.setVenta(vret.getId().getSubtotal().doubleValue());
						ir.setIva(vret.getId().getIva().doubleValue());
						ir.setTotal(vret.getId().getTotal().doubleValue());
					}
					ir.setIngresoAbonoPrima(0);
					
				}else{
					ir.setTipodocumento(vret.getId().getTiporec());
					ir.setReferencia(vret.getId().getNumrec());					
					ir.setVenta(0);
					ir.setIva(0);
					ir.setTotal(0);
					ir.setExenta(0);
					ir.setExonerada(0);
					
					ir.setIngresoAbonoPrima(vret.getId().getMontotc().doubleValue());
									
				}	
				ir.setIngresoSujetoRetencion(ir.getTotal() + ir.getExenta() + ir.getExonerada() + ir.getIngresoAbonoPrima());
				
				dmontoComision  = d.roundDouble((ir.getVenta() + ir.getExenta() + ir.getExonerada() + ir.getIngresoAbonoPrima()) * (vret.getId().getComision().divide(new BigDecimal("100"))).doubleValue());
				ir.setMontoComisionVenta(dmontoComision);	
				
				dmontoComision = d.roundDouble(ir.getIva() * (vret.getId().getComision().divide(new BigDecimal("100"))).doubleValue()); 
				ir.setMontoComisionIVA(dmontoComision);	
				
				if(vret.getId().getMoneda().equals("COR")){					
					
					ir.setRetencionAnticipoIR( (d.roundDouble(ir.getIngresoSujetoRetencion() - ir.getIva()) * 0.01));
					
					ir.setRetencionIVA(d.roundDouble(ir.getIva() * 0.01));
					
					ir.setTotalRetencion(d.roundDouble(ir.getRetencionAnticipoIR() + ir.getRetencionIVA()));
				}else{
					tc.obtenerTasaJDExFecha("COR", "USD", vret.getId().getFecha());
					
					ir.setRetencionAnticipoIR(d.roundDouble(((ir.getIngresoSujetoRetencion() - ir.getIva())*0.01)*vret.getId().getTasa().doubleValue()));
					
					ir.setRetencionIVA( d.roundDouble((ir.getIva() * 0.01) * vret.getId().getTasa().doubleValue()));
					
					ir.setTotalRetencion(d.roundDouble(( ir.getRetencionAnticipoIR() + ir.getRetencionIVA() )* vret.getId().getTasa().doubleValue()));
				}
				
				lstResult.add(ir);
			}
		}catch(Exception ex){
			System.out.println("Se capturo excepcion en ReciboCtrl.buscarRecibosPagadosTC: " + ex);
		}
		return lstResult;
	}
/********************************************************************************************************/
	public boolean actualizarEnlaceReciboFac(int iNumfac,String sTipofactura,String sCodcomp, int iNumrec, int iCaid){
		 String sql = "update "+PropertiesSystem.ESQUEMA+".recibofac set estado = 'A' where numrec = "+iNumrec + " and caid = " +iCaid + " and tipofactura = '"+
		 sTipofactura+"' and trim(codcomp) = '"+sCodcomp.trim()+"' and numfac = " +iNumfac;
		 PreparedStatement ps = null;
		 boolean bBorrado = true;
		 Connection cn = null;
		 try{
			 cn = As400Connection.getJNDIConnection("DSMCAJA2");
			 ps = cn.prepareStatement(sql);
				int rs = ps.executeUpdate();
				if (rs == 0){
					bBorrado = false;
				}
			cn.commit();
			
		 }catch(Exception ex){
			 System.out.println("Se capturo una excepcion en ReciboCtrl.borrarEnlaceReciboFac: " + ex);
			 bBorrado = false;
		 }finally{
				try{cn.close();}catch(Exception ex2){ex2.printStackTrace();};
		 }
		 return bBorrado;
	 }
	
	/** Busqueda de recibos por filtros de usuario **/
	@SuppressWarnings("unchecked")
	public static List<Recibo> buscarRecibo(int caid, String codcomp, String tiporec,
			String valor, int filtrarPor, Date desde, Date hasta,
			boolean manuales,String estado, List<String[]> sumaPagos ) {
		
		List<Recibo>recibos = null;
		Session sesion = null;
		
		List<Integer>recibosxfacs = new ArrayList<Integer>();
		String numrecsxfacs = new String("");
		
		try {
			sesion = HibernateUtilPruebaCn.currentSession();
			
			
			//&& ======== Consulta por numero de factura.
			if (filtrarPor == 4 && valor.matches(PropertiesSystem.REGEXP_NUMBER)) {
				Criteria crRf = sesion.createCriteria(Recibofac.class);
				crRf.add(Restrictions.eq("id.numfac", Integer.parseInt(valor)));
				crRf.setProjection(Projections.property("id.numrec"));
				recibosxfacs = (List<Integer>)crRf.list();
				
				if(recibosxfacs == null || recibosxfacs.isEmpty())
					return null ;
				
				StringBuilder sb = new StringBuilder( recibosxfacs.toString() );
				sb.replace(0, 1, "(").replace(sb.length()-1, sb.length(), ")");
				numrecsxfacs = sb.toString() ;
			}
			//&& ===== Obtener la lista de detalles de recibo.
			String selecRecs = " select r.* ";
			String selecSums = " select rd.mpago, sum(monto), moneda ";
			
			String sqlFromHdrs = " from " + PropertiesSystem.ESQUEMA+".recibo r " ;
			String sqlFromDets = " from " + PropertiesSystem.ESQUEMA
					+ ".recibo r inner join " + PropertiesSystem.ESQUEMA
					+ ".recibodet rd on "
					+ " r.caid = rd.caid and r.codcomp = rd.codcomp "
					+ " and r.codsuc = rd.codsuc and r.tiporec = rd.tiporec "
					+ " and r.numrec = rd.numrec";
			
			String sqlWhere = " where r.estado = '" + estado + "'";
			if(caid != 0)
				sqlWhere += " and r.caid = "+caid;
			if(codcomp.compareTo("01") != 0)
				sqlWhere += " and r.codcomp = '"+codcomp+"'";
			if(tiporec.compareTo("01") != 0)
				sqlWhere += " and r.tiporec = '"+tiporec+"'";
			if(manuales)
				sqlWhere += " and r.numrecm > 0 ";
			
		 
			String fechaini = "" ;
			if(desde != null){
				fechaini = FechasUtil.formatDatetoString(desde, "yyyy-MM-dd");
			}
			String fechafin = "" ;
			if(hasta != null){
				fechafin = FechasUtil.formatDatetoString(hasta, "yyyy-MM-dd");
			}
			
			if(!fechaini.isEmpty() && fechaini.compareTo(fechafin) == 0 ){
				sqlWhere += " and r.fecha =  '"+fechaini+"'";
			}else{
				if( !fechaini.isEmpty() )
					sqlWhere += " and r.fecha >= '"+ fechaini +"'";
				if( !fechafin.isEmpty() )
					sqlWhere += " and r.fecha <= '"+ fechafin +"'";
			}
			
			valor = valor.trim().toLowerCase();
		
			if(filtrarPor == 1 && valor.matches(PropertiesSystem.REGEXP_DESCRIPTION))
				sqlWhere += " and lower(cliente) like '%"+valor+"%'";
			
			if(filtrarPor > 1 && valor.matches(PropertiesSystem.REGEXP_NUMBER)){
				if(filtrarPor == 2)
					sqlWhere += " and codcli = " + valor;
				if(filtrarPor == 3)
					sqlWhere += " and r.numrec = " + valor;
			}
			
			if(filtrarPor == 4 && numrecsxfacs.compareTo("") != 0 )
				sqlWhere += " and r.numrec in " +numrecsxfacs;
			String emptySearch=" where r.estado = ''";
			if(sqlWhere.trim().equals(emptySearch.trim()) ) {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.MONTH, -1);
				Date modifiedDate = cal.getTime();
				String fechaB = FechasUtil.formatDatetoString(modifiedDate, "yyyy-MM-dd");
				Query query = sesion.createSQLQuery(selecRecs + sqlFromHdrs + sqlWhere + "  and r.tiporec <> 'FCV' AND r.fecha >='"+fechaB+"'" ).addEntity(Recibo.class);				
				 recibos=query.list();
			}else
			//&& ===== Lista de encabezados de recibo
			recibos = sesion.createSQLQuery(selecRecs + sqlFromHdrs + sqlWhere + "  and r.tiporec <> 'FCV'" ).addEntity(Recibo.class).list(); 

			String strLoginUser="";
			UsuarioCtrl uctrl = new UsuarioCtrl();
			
			if(recibos != null && !recibos.isEmpty() ){
				
				String[][] strCodUsuarioCreo = new String[recibos.size()][2];
				int ii = 0;
				//&& ======== Asignar propiedades para mostrar el grid
				for (Recibo recibo : recibos) {
					recibo.setCaid(recibo.getId().getCaid());
					recibo.setNumrec(recibo.getId().getNumrec());
					recibo.setCodcomp(recibo.getId().getCodcomp());
					recibo.setCodsuc(recibo.getId().getCodsuc());
					recibo.setTiporec(recibo.getId().getTiporec());
					
					if(ii==0)
					{
						
						strCodUsuarioCreo[ii][0] = recibo.getCoduser();
						strLoginUser = uctrl.getLoginXCodUsuario(recibo.getCoduser());
						strLoginUser = strLoginUser==null ? "" : strLoginUser;
						strCodUsuarioCreo[ii][1] = strLoginUser.trim().equals("") ? recibo.getCoduser() : strLoginUser;
						
						recibo.setUsuariocreo(strCodUsuarioCreo[ii][1]);
						
						
						ii++;
					}
					else
					{
						boolean asigno = false;
						for(int j=0; j<ii;j++)
						{
							if(strCodUsuarioCreo[j][0].trim().equals(recibo.getCoduser().trim()))
							{
//								System.out.println(i + " -->r login " + strCodUsuarioCreo[j][1] );
								recibo.setUsuariocreo(strCodUsuarioCreo[j][1]);
								asigno=true;
								break;
							}
						}
						
						if(!asigno)
						{
							strCodUsuarioCreo[ii][0] = recibo.getCoduser();
							strLoginUser = uctrl.getLoginXCodUsuario(recibo.getCoduser());
							strLoginUser = strLoginUser==null ? "" : strLoginUser;
							strCodUsuarioCreo[ii][1] = strLoginUser.trim().equals("") ? recibo.getCoduser() : strLoginUser;
							
							recibo.setUsuariocreo(strCodUsuarioCreo[ii][1]);
							
							ii++;
						}
					}
				}
				
				
				//&& ========= listado para ventana de resumen
				String selecSumsIngresos = selecSums + sqlFromDets + sqlWhere	+ " and r.tiporec not in ('DCO', 'FCV') " + " group by mpago, moneda ";

				List<Object[]> sumpagos = (List<Object[]>)sesion.createSQLQuery(selecSumsIngresos).list();
				for (Object[] oSum : sumpagos) {
					sumaPagos.add(new String[]{
							String.valueOf(oSum[0]),
							String.valueOf(oSum[1]),
							String.valueOf(oSum[2])} 
							);
				}
				
				//&& ============ restar el monto por devoluciones aplicadas.
				String selecSumsDevoluciones = selecSums + sqlFromDets + sqlWhere	+ " and r.tiporec = 'DCO' " + " group by mpago, moneda ";

				List<Object[]> sumDevoluciones = (List<Object[]>)sesion.createSQLQuery(selecSumsDevoluciones).list();
				if( sumDevoluciones != null && !sumDevoluciones.isEmpty() ){
					
					 for (int i = 0; i < sumaPagos.size(); i++) {
						final String[] ingresoMpago  = sumaPagos.get(i);
						 
						Object[] devolucionMpago = (Object[])
						CollectionUtils.find(sumDevoluciones, new Predicate(){
							@Override
							public boolean evaluate(Object o) {
								Object[] oDev = (Object[]) o;
								return 
								String.valueOf(oDev[0]).compareTo(ingresoMpago[0]) == 0  && 
								String.valueOf(oDev[2]).compareTo(ingresoMpago[2]) == 0 ; 
							}
						})  ;
						
						if( devolucionMpago == null ) continue;
						
						sumaPagos.get(i)[1] = 
						new BigDecimal( ingresoMpago[1] ).subtract(new BigDecimal(String.valueOf(devolucionMpago[1]))).toString();
					}
				}
				//&& ============ restar los cambios.
				String selectTotalCambios = " select '" + MetodosPagoCtrl.EFECTIVO + "', sum(cambio), moneda ";
				String queryTablasCambios = 
				" from "+PropertiesSystem.ESQUEMA+".recibo r inner join "+PropertiesSystem.ESQUEMA+".cambiodet rd on r.caid =rd.caid and r.codcomp = rd.codcomp and r.numrec = rd.numrec and r.tiporec = rd.tiporec " ;
				
				String selectSumCambios = selectTotalCambios + queryTablasCambios +  sqlWhere	
					+ " and r.tiporec not in ('DCO', 'FCV')  and cambio > 0 " 
					+ " group by '"+MetodosPagoCtrl.EFECTIVO+"',  moneda ";
				
				
				List<Object[]> sumCambios = (List<Object[]>)sesion.createSQLQuery(selectSumCambios).list();
				if( sumCambios != null && !sumCambios.isEmpty() ){
					
					 for (int i = 0; i < sumaPagos.size(); i++) {
						final String[] ingresoMpago  = sumaPagos.get(i);
						 
						Object[] cambio = (Object[])
						CollectionUtils.find(sumCambios, new Predicate(){
							@Override
							public boolean evaluate(Object o) {
								Object[] oDev = (Object[]) o;
								return 
								String.valueOf(oDev[0]).compareTo(ingresoMpago[0]) == 0  && 
								String.valueOf(oDev[2]).compareTo(ingresoMpago[2]) == 0 ; 
							}
						})  ;
						
						if( cambio == null ) continue;
						
						sumaPagos.get(i)[1] = 
						new BigDecimal( ingresoMpago[1] ).subtract(new BigDecimal(String.valueOf(cambio[1]))).toString();
					}
				}
			}
			
		} catch (Exception e) { 
			e.printStackTrace();
		}finally{
			
		
			recibosxfacs = null;
			numrecsxfacs = null;
		}
		return recibos;
	}
	
/*********************************************************************************************************/	
	public List getRecibosxParametros(int iCaid, String sCodComp,String sTipoRecibo,String sParametro,int iTipousqueda,
			  Date dDesde,Date dHasta,String sEstado,boolean manuales){
		List lstRecibos = null;
		String sql = "from Recibo as r where r.id.tiporec <> 'FCV' ";
		Session session = HibernateUtilPruebaCn.currentSession();		
		Vrecfac vrecfac = null;
		Recibo recibo = null;
		ReciboId reciboId = null;
		int[] iNobatch = null;
		String sNobatch = "";
		try{
			switch (iTipousqueda){
				case(1)://busqueda por nombre de cliente
					if(!sParametro.equals("")){
						sql = sql + " and r.cliente like '%"+sParametro.toUpperCase().trim()+"%'";
					}
					break;
				case(2)://busqueda por codigo de cliente
					if(!sParametro.equals("")){
						sql = sql + " and cast(r.codcli as string) like '"+sParametro.trim()+"%'";
					}
					break;
				case(3)://busqueda por numero de recibo
					if(!sParametro.equals("")){
						sql = sql + " and cast(r.id.numrec as string) like '"+sParametro.trim()+"%'";
					}
					break;
				case(4)://busqueda por numero de factura
					sql = "from Vrecfac as r where r.id.estado = '"+sEstado+"' ";
					if(!sParametro.equals("")){						
						sql = sql + " and cast(r.id.numfac as string) like '"+sParametro.trim()+"%'";
					}
					break;
			}
			//codigo de caja
			if(iCaid > 0){
				sql = sql + " and r.id.caid = "+iCaid+"";
			}
			//codigo de compania
			if(!sCodComp.equals("01")){
				sql = sql + " and r.id.codcomp = '"+sCodComp+"'";
			}
			//tipo de recibo
			if(!sTipoRecibo.equals("01")){
				sql = sql + " and r.id.tiporec = '"+sTipoRecibo+"'";
			}
			//manuales
			if(manuales){
				sql = sql + " and numrecm > 0 ";
			}
			//estado
			if(iTipousqueda<4){
				sql = sql + " and r.estado = '"+sEstado+"'";			
				//fechas
				if(dDesde != null){
					sql = sql + " and r.fecha >= :pDesde";
				}
				if(dHasta != null){
					sql = sql + " and r.fecha <= :pHasta";
				}
			}else{
				//fechas
				if(dDesde != null){
					sql = sql + " and r.id.fecha >= :pDesde";
				}
				if(dHasta != null){
					sql = sql + " and r.id.fecha <= :pHasta";
				}
			}
			
				
			Query q = session.createQuery(sql);
			if(dDesde != null){q.setDate("pDesde", dDesde);}
			if(dHasta != null){q.setDate("pHasta", dHasta);}
					
			lstRecibos = q.list();
			
			if(iTipousqueda == 4){
				for(int i = 0; i < lstRecibos.size();i++){
					vrecfac = (Vrecfac)lstRecibos.get(i);
					recibo = new Recibo();
					reciboId = new ReciboId();
					 
					reciboId.setCaid(vrecfac.getId().getCaid());
					reciboId.setNumrec(vrecfac.getId().getNumrec());
					reciboId.setCodcomp(vrecfac.getId().getCodcomp());
					reciboId.setCodsuc(vrecfac.getId().getCodsuc());
					reciboId.setTiporec(vrecfac.getId().getTiporec());
					
					recibo.setId(reciboId);
					
					recibo.setMontoapl(vrecfac.getId().getMontoapl());
					recibo.setMontorec(vrecfac.getId().getMontorec());
					recibo.setConcepto(vrecfac.getId().getConcepto());
					recibo.setFecha(vrecfac.getId().getFecha());
					recibo.setCliente(vrecfac.getId().getCliente());
					recibo.setCodcli(vrecfac.getId().getCodcli());
					recibo.setCajero(vrecfac.getId().getCajero());	
					recibo.setHora(vrecfac.getId().getHora());
					recibo.setNumrecm(vrecfac.getId().getNumrecm());
					recibo.setRecjde(vrecfac.getId().getRecjde());	
					recibo.setEstado(vrecfac.getId().getEstado());
					recibo.setMotivo(vrecfac.getId().getMotivo());
					recibo.setCodusera(vrecfac.getId().getCodusera());
					recibo.setHoramod(vrecfac.getId().getHoramod());
					recibo.setCoduser(vrecfac.getId().getCoduser());
					
					recibo.setCaid(vrecfac.getId().getCaid());
					recibo.setNumrec(vrecfac.getId().getNumrec());
					recibo.setCodcomp(vrecfac.getId().getCodcomp());
					recibo.setCodsuc(vrecfac.getId().getCodsuc());
					recibo.setTiporec(vrecfac.getId().getTiporec());
					
					/*iNobatch = obtenerBatchxRecibo2(recibo.getId().getNumrec(),recibo.getId().getCaid(),recibo.getId().getCodsuc(),recibo.getId().getCodcomp(),recibo.getId().getTiporec());
					sNobatch = "";
					for(int j = 0; j < iNobatch.length; j++){
						sNobatch = sNobatch +" " +iNobatch[j];						
					}*/
					recibo.setNobatch(sNobatch);
					
					lstRecibos.set(i, recibo);
				}
				}else{
				for(int i = 0; i < lstRecibos.size();i++){
					recibo = new Recibo();
					recibo = (Recibo)lstRecibos.get(i);
					//iNobatch = obtenerBatchxRecibo2(recibo.getId().getNumrec(),recibo.getId().getCaid(),recibo.getId().getCodsuc(),recibo.getId().getCodcomp(),recibo.getId().getTiporec());
					recibo.setCaid(recibo.getId().getCaid());
					recibo.setNumrec(recibo.getId().getNumrec());
					recibo.setCodcomp(recibo.getId().getCodcomp());
					recibo.setCodsuc(recibo.getId().getCodsuc());
					recibo.setTiporec(recibo.getId().getTiporec());
					
					lstRecibos.set(i, recibo);
				}
			}
			
			//&&
			
			
			
			
		}catch(Exception ex){
			System.out.println("Se capturo una excepcion en ReciboCtrl.getRecibosxParametros: " + ex);
		}finally{
			session.close();
		}
		return lstRecibos;
	}
	
	/******************************************************************/
	public double[] getMontoPendienteF05503am(Connection cn,String sCodsuc, int iNosol,String sTiposol,int iNocuota,int iCodcli){
		String sql  = "SELECT sum(ATAAP)  as CPENDIENTE," + 
              			"sum(ATFAP ) as DPENDIENTE " + 
              			"FROM "+PropertiesSystem.JDEDTA+".F5503AM " +
              			"WHERE ATDOCO = "+iNosol+" AND ATDCTO = '"+sTiposol+"'  and ATAN8 = "+iCodcli+" and atdfr = "+iNocuota+" AND ATKCOO = '000"+sCodsuc+"'";
		double[] dResultado = new double[2];
		PreparedStatement ps = null;
		try{
			ps = cn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				dResultado[0] = pasarEnteroADouble(rs.getInt("CPENDIENTE"));
				dResultado[1] = pasarEnteroADouble(rs.getInt("DPENDIENTE"));
			}
		}catch(Exception ex){
			System.out.println("Se capturo una excepcion en ReciboCtrl.getMontoAplicadoF0311: " + ex);
		}finally {
			try {
				ps.close();
				//cn.close();
			} catch (Exception se2) {
				System.out.println("ERROR: Failed to close connection en ReciboCtrl.getMontoAplicadoF0311:  " + se2);
			}
		}
		return dResultado;
	}
/********************************************************************************/
	public double pasarEnteroADouble(int iMonto){
		double dMonto = 0;
		String sMonto = "",s1= "",s2 = "";
		try{
			if (iMonto > 0){
				sMonto = String.valueOf(iMonto);
				if(sMonto.length()>2){
					s1 = sMonto.substring(0, sMonto.length() - 2);
					s2 = sMonto.substring(sMonto.length() - 2,sMonto.length());
					sMonto = s1 + "." + s2;
					dMonto = Double.parseDouble(sMonto);
				}else if(sMonto.length() == 2){
					sMonto = "0." + sMonto;
					dMonto = Double.parseDouble(sMonto);
				}else if(sMonto.length() == 1){
					sMonto = "0.0" + sMonto;
					dMonto = Double.parseDouble(sMonto);
				}else{
					dMonto = 0;
				}
			}
		}catch(Exception ex){
			System.out.println("Se capturo una excepcion en ReciboCtrl.pasarEnteroADouble: " + ex);
		}
		return dMonto;
	}
	/************************   Leer y actualizar el número de batch *****************************************/
	public int leerActualizarNoAsiento(){
		int iNodoco = -1,iActualizado;
		Session sesion = HibernateUtilPruebaCn.currentSession();
		Transaction trans = null;
		String sql = "";
		Object ob = null;
		
		try {
			//--------- leer el número de documento a utilizar.
			sql = "select f.id.nnn002 from Vf0002 f where trim(f.id.nnsy) = '09'";
			trans = sesion.beginTransaction();
			ob = sesion.createQuery(sql).uniqueResult();
			if(ob != null){
				iNodoco = Integer.parseInt(ob.toString());
				
				//--------- Actualizar el número de documento.
				sql = "UPDATE "+PropertiesSystem.JDECOM+".F0002 SET NNN002 = " + (iNodoco+1) + " where NNSY = '09'";
				Query q = sesion.createSQLQuery(sql);
				iActualizado = q.executeUpdate();
				if(iActualizado != 1)
					iNodoco = -1;
			}
			trans.commit();
		} catch (Exception error) {
			iNodoco = -1;
			System.out.println("Error en ReciboCtrl.leerActualizarNoDocJDE  " + error);
		} finally {
			try {
				sesion.close();
			} catch (Exception e) {
				iNodoco = -1;
				System.out.println("Error al cerrar sesion en ReciboCtrl.leerActualizarNoDocJDE  " + e);
			}
		}
		return iNodoco;
	}
	
	
/************************************************************************************************/

/** Obtener el registro de las factura de financimiento en F0311 pagadas en el recibo		**/
	public List leerFacturasReciboFN(int iCaid,String sCodComp,int iNumrec,String sTiporec){
		
		List lstFacturasRecibo = new ArrayList(), lstRecibofac = null;
		FacturaxRecibo facturaRec = null;
		
		Recibofac rf = null;
		Session session = HibernateUtilPruebaCn.currentSession();
		
		String sql = "",sFecha = "";
		JulianToCalendar fecha = null;
		Credhdr crFN = null;
		FechasUtil f = new FechasUtil();
		
		
		try{
			sql =  " from Recibofac as r where r.id.caid = "+iCaid +" and r.id.codcomp = '"+sCodComp.trim()+"'";
			sql += " and r.id.numrec = " + iNumrec + " and r.id.tiporec = '" +sTiporec+"'";	
			
		
			lstRecibofac = session.createQuery(sql).list();
		
			
			for(int i = 0; i < lstRecibofac.size();i++){
				rf = (Recibofac)lstRecibofac.get(i);
				
				if(rf!=null){
					crFN = getInfoFactFinanciamiento(rf.getId().getNumfac(),sCodComp,
											rf.getId().getTipofactura() ,rf.getId().getPartida(),rf.getId().getCodunineg());
					sFecha = f.formatDatetoString(crFN.getId().getFecha(), "dd/MM/yyyy");
					facturaRec = new FacturaxRecibo(crFN.getId().getNofactura(), crFN.getId().getTipofactura(),
													crFN.getId().getUnineg(), crFN.getMontoAplicar(), 
													crFN.getId().getMoneda(),sFecha,crFN.getId().getPartida());
				}
				lstFacturasRecibo.add(facturaRec);
			}
		}catch(Exception ex){
			System.out.println("Se capturo una excepcion en ReciboCtrl.leerFacturasReciboCredito: " + ex);
		}finally{
			session.close();
		}
		return lstFacturasRecibo;
	}
/*********************************************************************************************/	
/** 	 			Obtiene información de facturas de financimiento						**/
	public Credhdr getInfoFactFinanciamiento(int iNumfac,String sCodComp,String sTipoDocumento, String sPartida,String sCodunineg){
		Credhdr chrFactFN = null;
		String sql = "";
		Transaction trans = null;
		Session sesion = HibernateUtilPruebaCn.currentSession();
		
		try {		
			sql =  " SELECT";
			sql += " CAST(TRIM(RPDCT) AS VARCHAR(2) CCSID 37) TIPOFACTURA,";
			sql += " RPDOC AS NOFACTURA,";
			sql += " CAST (RPSFX AS VARCHAR(3) CCSID 37) AS PARTIDA,";
			sql += " RPAN8 AS CODCLI, ";
			sql += " RPALPH AS NOMCLI, ";
			sql += " CAST(TRIM(RPMCU) AS VARCHAR(12) CCSID 37) CODUNINEG,"; 
			sql += " UN.MCDL01 AS UNINEG, ";
			sql += " CAST(RPKCO AS VARCHAR(5) CCSID 37) CODSUC,";
			sql += " (SELECT MCDL01 FROM "+PropertiesSystem.JDEDTA+".F0006 WHERE SUBSTRING(RPKCO,4,5) = SUBSTRING(MCMCU,11,12) AND MCSTYL = 'BS') AS NOMSUC,";	 
			sql += " CAST(TRIM(UN.MCRP01) AS VARCHAR(3) CCSID 37) CODCOMP, ";
			sql += " CO.DRDL01 AS NOMCOMP, ";
			sql += " CAST(TRIM(RPCRCD) AS VARCHAR(3) CCSID 37) MONEDA,"; 
			sql += " RPCRR AS TASA,";
			sql += " CAST(RPAAP/100 AS DECIMAL (10,2)) AS CPENDIENTE,";
			sql += " CAST(RPFAP/100 AS DECIMAL (10,2)) AS DPENDIENTE,";
			sql += " CAST ((CAST(DATE(CHAR(1900000 + RPDIVJ)) AS TIMESTAMP)) AS DATE) FECHA,";
			sql += " CAST ((CAST(DATE(CHAR(1900000 + RPDDJ)) AS TIMESTAMP)) AS DATE) FECHAVENC";
	
			sql += " FROM "+PropertiesSystem.JDEDTA+".F0311";
			sql += " INNER JOIN "+PropertiesSystem.JDEDTA+".F0006 UN ON RPMCU = UN.MCMCU"; 
			sql += " INNER JOIN "+PropertiesSystem.JDECOM+".F0005 CO ON (UN.MCRP01 = SUBSTRING(CO.DRKY,8,10) AND CO.DRSY = '00' AND CO.DRRT = '01' AND CO.DRDL02 = 'F')"; 
			sql += " WHERE TRIM(RPDCTO) <> ''  AND TRIM(RPPO) <> ''";
			sql += " and RPDOC = "+iNumfac+" AND TRIM(UN.MCRP01) = '"+sCodComp.trim()+"' AND  TRIM(RPDCT)= '"+sTipoDocumento+"'";
			sql += " and RPSFX = '"+sPartida+"' and TRIM(RPMCU) = '"+sCodunineg+"'";
			
			trans = sesion.beginTransaction();
			Object ob = sesion.createSQLQuery(sql).addEntity(Credhdr.class).uniqueResult();
			trans.commit();
			
			if(ob!=null)
				chrFactFN = (Credhdr)ob;
		
		} catch (Exception error) {
			chrFactFN = null;
			System.out.println("Error en ReciboCtrl.getInfoFactFinanciamiento " + error);
		} finally {
			sesion.close();
		}
		return chrFactFN;
	}
/*********************************************************************************************/	
/** 	Elimina los registros de F0311 generados al recibo de primas  		******************/
	public boolean eliminarRegistros311xTipo(int iNobatch,String sRpdct,Connection cn){
		boolean bHecho = true;
		PreparedStatement ps = null;
		String sql = "";
		int iResul=0;
		
		try {
			sql =  " DELETE FROM "+PropertiesSystem.JDEDTA+".F0311 F WHERE F.RPICU =  "+iNobatch;
			sql += " AND F.RPDCT = '"+sRpdct+"' AND RPICUT = 'R' ";
			ps = cn.prepareStatement(sql);
			iResul = ps.executeUpdate();
			
			if(iResul<=0)
				bHecho = false;
			
		} catch (Exception error) {
			bHecho = false;
			System.out.println("Error en ReciboCtrl.eliminarRegistros311ru " + error);
		} finally {
			try {
				ps.close();
			} catch (Exception e) {
				bHecho = false;
				System.out.println("Error en ReciboCtrl.eliminarRegistros311ru " + e);
			}
		}
		return bHecho;
	}
	
	
	
/*********************************************************************************************/	
/**  				Obtiene los datos del bien pagado en el recibo de primas.  		
/********************/	
	public List<FacturaxRecibo> leerDatosBien(int iNumrec, int iCaid, String sCodcomp, String sCodsuc){
		Session sesion = null;
		List<FacturaxRecibo> lstDatosBien = new ArrayList<FacturaxRecibo>();
 
		try {
			String sql = "  SELECT numrec, codcomp,codsuc,caid, "
			+ "IFNULL( (select CAST(drdl01 AS VARCHAR(30) CCSID 37) from "
			+ PropertiesSystem.JDECOM + ".f0005 where trim(drky) = B.TIPOPROD " +
					"and DRRT='P1' AND trim(DRSY) ='41' AND DRDL02 <> '' " +
					"fetch first rows only), b.tipoprod) tipoprod,"
			+ "IFNULL( (select CAST(drdl01 AS VARCHAR(30) CCSID 37) from "
			+ PropertiesSystem.JDECOM + ".f0005 WHERE trim(drky) = B.MARCA" +
					" and trim(DRSY) = '41' AND DRRT='02' " +
					" fetch first rows only),b.marca) marca ,"
			+ "IFNULL( (select CAST(drdl01 AS VARCHAR(30) CCSID 37) from "
			+ PropertiesSystem.JDECOM + ".f0005 where trim(drky) = B.MODELO " +
					"and trim(DRSY) = '41' AND DRRT='P2' " +
					" fetch first rows only), b.modelo) modelo  "
			+ ", Noitem "
			+ ", sucursal_Documento "
			+ " FROM " + PropertiesSystem.ESQUEMA + ".BIEN B "
			+ " WHERE B.CAID =" + iCaid + " AND B.NUMREC = " + iNumrec
			+ " and B.CODCOMP = '" + sCodcomp + "'";
			 
			sesion = HibernateUtilPruebaCn.currentSession();
			
			LogCajaService.CreateLog("leerDatosBien", "QRY", sql);

			@SuppressWarnings("unchecked")
			List<Bien> lstBien = (ArrayList<Bien>)sesion.createSQLQuery(sql)
								.addEntity(Bien.class).list();
			if(lstBien == null || lstBien.isEmpty())
				return null;
			
			lstDatosBien.add(new FacturaxRecibo(0,
					lstBien.get(0).getTipoprod(), lstBien.get(0).getMarca(),
					lstBien.get(0).getModelo(), lstBien.get(0).getNoitem()));
			
		} catch (Exception error) {
			LogCajaService.CreateLog("leerDatosBien", "ERR", error.getMessage());
			error.printStackTrace(); 
		}

		return lstDatosBien;
	}
	
/*********************************************************************************************/	
/**  				Guardar registro de recibo de Ingresos extraordinarios EX             	**/
	public boolean guardarRcEx(Connection cn,String sCodsuc,int iCodcli,String sTipoDoc,int iRpdoc,String sRpdctm,int iRpdocm,
								int iFechaact,int iNobatch,String sPayStatus, long iMtompagodom, String sModofd, String sMoneda,
								double dTasaOf, long iMtoForaneo,String sIdctaTO,String sidCtaMpago, String sRPAM2, String sRPSBLT,
								String sRPSBL,String sRPTRTC, String sCodUnineg, String sConcepto, String sUsuario,String sAplicacion){
		boolean bHecho = true;
		String sql = "",sNombrePc = "",sHora[],sFecha[];
		PreparedStatement ps = null;
		Date dFechaHora = new Date();
		SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		EmpleadoCtrl empCtrl = new EmpleadoCtrl();
		Vf0101 vf = null;
		int iFecha,iHora, iAnio,iMes;
		String sNomcli = "",sCadValida ="";
		Divisas d = new Divisas();
		
		try {
 
			
			iFecha =  FechasUtil.dateToJulian( new Date() );
			
			sHora  = (dfHora.format(dFechaHora)).split(":");		//obtener hora en enteros
			sFecha = (sdf.format(dFechaHora)).split("/");			//obtener partes de la fecha
			iHora  = Integer.parseInt(sHora[0] + sHora[1] + sHora[2]);
			iAnio  = Integer.parseInt(sFecha[2].substring(2, 4));
			iMes   = Integer.parseInt(sFecha[1]);
			
			//sCodUnineg = sCodUnineg.length() == 2? "          "+sCodUnineg:"        "+sCodUnineg;
			
			sCodUnineg = com.casapellas.util.CodeUtil.pad(sCodUnineg.trim(), 12," ");
			
			if(sConcepto.length() > 30)
				sConcepto = sConcepto.substring(0,29);
			
			//sNombrePc = "SERVER"; InetAddress.getLocalHost().getHostName();
			sNombrePc = PropertiesSystem.ESQUEMA;
			if(sNombrePc.length() > 9)
				sNombrePc =	sNombrePc.substring(0, 9);

			vf = empCtrl.buscarEmpleadoxCodigo2(iCodcli);
			Vf0101Id vi = vf.getId();
			sNomcli = vi.getAbalph().trim();
			sCadValida = d.remplazaCaractEspeciales(sNomcli, "'", "-");
			if(!sCadValida.equals("")){
				sNomcli = sCadValida;
			}
			sql =  "INSERT INTO "+PropertiesSystem.JDEDTA+".F0311 (";
			sql += "RPKCO,RPAN8,RPDCT,RPDOC,RPSFX,RPDIVJ,RPDCTM,RPDOCM,   RPDMTJ,RPDGJ,RPFY,RPCTRY,RPPN,RPCO,RPICUT,RPICU,";
			sql += "RPDICJ,   RPPST,RPAG,RPCRRM,RPCRCD,RPCRR,RPACR,   RPDSVJ,RPGLBA,RPAM,RPAID2,RPAM2,RPMCU,RPSBLT,RPSBL,   RPDDJ,";
			sql += "RPDDNJ,RPTRTC,RPRMK,RPALPH,RPAC01,RPAC02,RPAC03,RPAC04,RPAC05,RPAC06,RPAC07,RPAC08,RPAC09,RPAC10,";
			sql += "RPATE,RPATR,RPATP,RPATPR,RPAT1,RPAT2,RPAT3,RPAT4,RPAT5,RPTORG,RPUSER,RPPID,RPUPMJ,RPUPMT,RPJOBN) VALUES(";
			
			sql += "'"+sCodsuc+"',"+ iCodcli + ",'"+sTipoDoc+"',"+ iRpdoc +"," + "'001',"+ 0 +"," +"'"+sRpdctm +"'," + iRpdocm + ",";
			sql += iFecha +"," + iFecha + "," +iAnio +", 20 ," +iMes + ", '" +sCodsuc+"'," + "'R'," + iNobatch + "," +iFecha + ",";
			sql += "'"+sPayStatus+"'" +"," +iMtompagodom+"," +"'"+sModofd+"'"+"," +"'"+sMoneda+"'," +dTasaOf+"," +iMtoForaneo+"," ;
			sql += iFecha+"," + "'"+sidCtaMpago+"',"  +"'2'," + "'"+sIdctaTO+"'," +"'"+sRPAM2+"'," +"'"+sCodUnineg+"'," +"'"+sRPSBLT+"'," +"'"+ sRPSBL + "',";
			sql += iFecha+"," +iFecha+"," +"'"+sRPTRTC+"'," +"'"+sConcepto+"'," + "'"+sNomcli+"'," 	+ "'"+vi.getAbac01()+"'," + "'"+vi.getAbac02()+"',"; ;
			sql += "'"+vi.getAbac03()+"'," +"'"+vi.getAbac04()+"'," +"'"+vi.getAbac05()+"'," +"'"+vi.getAbac06()+"'," +"'"+vi.getAbac07()+"',";
			sql += "'"+vi.getAbac08()+"'," +"'"+vi.getAbac09()+"'," +"'"+vi.getAbac10()+"'," +"'"+vi.getAbate()+"'," +"'"+vi.getAbatr()+"',";
			sql += "'"+vi.getAbatp()+"'," +"'"+vi.getAbatpr()+"'," +"'"+vi.getAbat1()+"'," +"'"+vi.getAbat2()+"',"  +"'"+vi.getAbat3()+"'," ;
			sql += "'"+vi.getAbat4()+"'," +"'"+vi.getAbat5()+"'," +"'"+sUsuario+"'," +"'"+sUsuario+"',"+ "'" + sAplicacion + "'," +iFecha+"," +iHora+"," +"'"+sNombrePc+"'" + ")";		
				
			ps = cn.prepareStatement(sql);
			int rs = ps.executeUpdate();
			if (rs != 1){
				bHecho = false;
			}
			
		} catch (Exception error) {
			error.printStackTrace(); 
			bHecho = false;
		} 
		return bHecho;
	}
	
	/*********************************************************************************************/	
	/**  				Guardar registro de recibo de primas RU 		
	/********************/
		public boolean guardarRU(Connection cn,String sCodsuc,int iCodcli,String sTipoDoc,int iRpdoc,String sRpdctm,int iRpdocm,
						int iFechaact,int iNobatch,String sRppost,String sPayStatus, int iMtompagodom,int iRpaap,String sModofd,
						String sMoneda, double dTasaOf, int iMtoForaneo,int iRpfap, String sIdCuenta,String sCodUnineg,	
						String sConcepto, String sUsuario,String sAplicacion, String iNocontrato,String sTipocont){
			boolean bHecho = true;
			String sql = "",sNombrePc = "",sHora[],sFecha[];
			PreparedStatement ps = null;
			Date dFechaHora = new Date();
			SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			EmpleadoCtrl empCtrl = new EmpleadoCtrl();
			Vf0101 vf = null;
			Calendar cFecha;
			CalendarToJulian fecha;
			int iFecha,iHora, iAnio,iMes;
			String sCadValida= "",sNomcli = "";Divisas d = new Divisas();
			try {
				sql =  "INSERT INTO "+PropertiesSystem.JDEDTA+".F0311 (";
				sql += "RPKCO,RPAN8,RPDCT,RPDOC,RPSFX,RPDIVJ,RPDCTM,RPDOCM,RPDMTJ,RPDGJ,RPFY,RPCTRY,RPPN,RPCO,RPICUT,RPICU,";
				sql += "RPDICJ,RPPA8,RPAN8J,RPPOST,RPPST,RPAG,RPAAP,RPCRRM,RPCRCD,RPCRR,RPACR,RPFAP,RPDSVJ,RPGLC,RPGLBA,";
				sql += "RPAM,RPMCU,RPDDJ,RPDDNJ,RPTRTC,RPRMK,RPALPH,RPAC01,RPAC02,RPAC03,RPAC04,RPAC05,RPAC06,RPAC07,RPAC08,";
				sql += "RPAC09,RPAC10,RPATE,RPATR,RPATP,RPATPR,RPAT1,RPAT2,RPAT3,RPAT4,RPAT5,RPTORG,RPUSER,RPPID,RPUPMJ,RPUPMT,RPJOBN,RPPO,RPDCTO) VALUES(";
					
				cFecha = Calendar.getInstance();
				fecha  = new CalendarToJulian(cFecha);
				iFecha = fecha.getDate();
				
				sNombrePc = "SERVER";//InetAddress.getLocalHost().getHostName();
				sHora  = (dfHora.format(dFechaHora)).split(":");		//obtener hora en enteros
				sFecha = (sdf.format(dFechaHora)).split("/");		//obtener partes de la fecha
				iHora  = Integer.parseInt(sHora[0] + sHora[1] + sHora[2]);
				iAnio  = Integer.parseInt(sFecha[2].substring(2, 4));
				iMes   = Integer.parseInt(sFecha[1]);
				
				sCodUnineg = sCodUnineg.length() == 2? "          "+sCodUnineg:"        "+sCodUnineg;
				if(sConcepto.length() > 30)
					sConcepto = sConcepto.substring(0,29);
				if(sNombrePc.length() > 9)
					sNombrePc =	sNombrePc.substring(0, 9);

				vf = empCtrl.buscarEmpleadoxCodigo2(iCodcli);
				Vf0101Id vi = vf.getId();
				sNomcli = vi.getAbalph().trim();
				sCadValida = d.remplazaCaractEspeciales(sNomcli, "'", "-");
				if(!sCadValida.equals("")){
					sNomcli = sCadValida;
				}
				
				sql += "'"+sCodsuc+"',"+ iCodcli + ",'"+sTipoDoc+"',"+ iRpdoc +"," + "'001',"+iFecha +"," +"'"+sRpdctm +"'," + iRpdocm + ",";
				sql += iFechaact +"," + iFecha + "," +iAnio +", 20 ," +iMes + ", '" +sCodsuc+"'," + "'R'," + iNobatch + "," +iFecha + ",";
				sql += iCodcli + "," +iCodcli + "," + "'"+sRppost +"'" + ","+"'"+sPayStatus+"'" +"," +iMtompagodom+"," +iRpaap+"," +"'"+sModofd+"'"+",";
				sql += "'"+sMoneda+"'," +dTasaOf+"," +iMtoForaneo+"," +iRpfap+"," +iFecha+"," +"'UC'," +"'"+sIdCuenta+"'," +"'2'," +"'"+sCodUnineg+"',";
				sql += iFecha+"," +iFecha+"," +"'U'," +"'"+sConcepto+"'," + "'"+sNomcli+"'," 	+ "'"+vi.getAbac01()+"'," + "'"+vi.getAbac02()+"',";
				sql += "'"+vi.getAbac03()+"'," +"'"+vi.getAbac04()+"'," +"'"+vi.getAbac05()+"'," +"'"+vi.getAbac06()+"'," +"'"+vi.getAbac07()+"',";
				sql += "'"+vi.getAbac08()+"'," +"'"+vi.getAbac09()+"'," +"'"+vi.getAbac10()+"'," +"'"+vi.getAbate()+"'," +"'"+vi.getAbatr()+"',";
				sql += "'"+vi.getAbatp()+"'," +"'"+vi.getAbatpr()+"'," +"'"+vi.getAbat1()+"'," +"'"+vi.getAbat2()+"',"  +"'"+vi.getAbat3()+"'," ;
				sql += "'"+vi.getAbat4()+"'," +"'"+vi.getAbat5()+"'," +"'"+sUsuario+"'," +"'"+sUsuario+"',";	
				sql += "'" + sAplicacion + "'," +iFecha+"," +iHora+"," +"'"+sNombrePc+"'" + ", '"+iNocontrato+"', '" + sTipocont +"')";		

				ps = cn.prepareStatement(sql);
				int rs = ps.executeUpdate();
				if (rs != 1){
					bHecho = false;
				}
				
			} catch (Exception error) {
				System.out.println("Error en ReciboCtrl.guardarRU " + error);
				bHecho = false;
			}		
			return bHecho;
		}
/*********************************************************************************************/	
/**   Obtiene y actualiza el número RPDOC a utilizar en cada registro del Recibo de primas (RU) 		
/*********/	
	public int leerNumeroRpdoc(boolean bActualizar){
		int iNoRPDOC = -1;
		Session sesion = HibernateUtilPruebaCn.currentSession();
		
		String sql= "";
		
		try {
			sql = "select f.id.nnn002 from Vf0002 f where trim(f.id.nnsy) = '03'";
			
			Object ob = sesion.createQuery(sql).uniqueResult();
			if(ob!=null){
				iNoRPDOC = Integer.parseInt(ob.toString());
				if(bActualizar){
					sql = "UPDATE "+PropertiesSystem.JDECOM+".F0002 SET NNN002 = " + (iNoRPDOC+1) + " where NNSY = '03'";
					Query q = sesion.createSQLQuery(sql);
					q.executeUpdate();
				}
			}
			
		} catch (Exception error) {
			System.out.println("Error en ReciboCtrl.leerNumeroRpdoc " + error);
			iNoRPDOC = -1;
		} 
		return iNoRPDOC;
	}
	/*********************************************************************************/	
	/**   Obtiene y actualiza el número a utilizar en RPDOCM para el Recibo de primas (RU) 		
	/*********/	
		public int leerNumeroRpdocm(boolean bActualizar,String sCodcomp){
			int iNoRPDOCM = -1;
			Session sesion = HibernateUtilPruebaCn.currentSession();
			
			String sql= "";
			String sTabla = "",sPosicion = "";
			try {
				
				if (sCodcomp.equals("E01")) {
					sTabla = "03";
					sPosicion = "005";
				} else if (sCodcomp.equals("E02")) {
					sTabla = "5555";
					sPosicion = "001";
				} else if (sCodcomp.equals("E08")) {
					sTabla = "5555";
					sPosicion = "003";
				} else if (sCodcomp.equals("E03")) {
					sTabla = "5803";
					sPosicion = "005";
				} else if (sCodcomp.equals("E10")) {
					sTabla = "5655";
					sPosicion = "001";
				}
				
				sql = "select f.id.nnn"+sPosicion+" from Vf0002 f where trim(f.id.nnsy) = '"+sTabla+"'";
			
				Object ob = sesion.createQuery(sql).uniqueResult();
				if(ob!=null){
					iNoRPDOCM = Integer.parseInt(ob.toString());
					if(bActualizar){
						sql = "UPDATE "+PropertiesSystem.JDECOM+".F0002 SET NNN"+sPosicion+" = " + (iNoRPDOCM+1) + " where trim(NNSY) = '"+sTabla+"'";
						Query q = sesion.createSQLQuery(sql);
						q.executeUpdate();
					}
				}else{
					error = new Exception("@LOGCAJA: No se pudo leer el numero de recibo en jde!!!;Compañia:" + sCodcomp +";Tabla:"+sTabla+";Posicion:"+sPosicion);
					errorDetalle = error;
				}
				
			} catch (Exception error) {
				error = new Exception("@LOGCAJA: No se pudo leer el numero de recibo en jde!!!;Compañia:" + sCodcomp +";Tabla:"+sTabla+";Posicion:"+sPosicion);
				errorDetalle = error;
				System.out.println("Error en ReciboCtrl.leerNumeroRpdocm " + error);
				iNoRPDOCM = -1;
			}
			return iNoRPDOCM;
		}
		
	/*********************************************************************************/	


		/*********************************************************************************************/	
		public int leerNumeroRpdocmRM(boolean bActualizar){
			int iNoRPDOCM = -1;
			Session sesion = HibernateUtilPruebaCn.currentSession();
			Transaction trans = null;
			String sql= "";
			
			try {
				sql = "select f.id.nnn007 from Vf0002 f where trim(f.id.nnsy) = '03'";
				trans = sesion.beginTransaction();
				Object ob = sesion.createQuery(sql).uniqueResult();
				if(ob!=null){
					iNoRPDOCM = Integer.parseInt(ob.toString());
					if(bActualizar){
						sql = "UPDATE "+PropertiesSystem.JDECOM+".F0002 SET NNN007 = " + (iNoRPDOCM+1) + " where NNSY = '03'";
						Query q = sesion.createSQLQuery(sql);
						q.executeUpdate();
					}
				}
				trans.commit();	
			} catch (Exception error) {
				System.out.println("Error en ReciboCtrl.leerNumeroRpdocm " + error);
				iNoRPDOCM = -1;
			} finally {
				sesion.close();
			}
			return iNoRPDOCM;
		}
/*********************************************************************************/	
/**   Actualiza el número de recibo en F55ca014 por caja y compañía con hibernate 		
/*********/
	public void actualizarNoRecibo(int iNumrecNuevo,int iCaid, String sCodcomp){
		String sql="";
		Session sesion = HibernateUtilPruebaCn.currentSession();
		Transaction trans = null;
		
		try {
			sql =  " update "+PropertiesSystem.ESQUEMA+".F55ca014 set c4nncu = "+iNumrecNuevo;
			sql += " where c4id = "+iCaid+" and c4rp01 = '"+sCodcomp+"'";			
			trans = sesion.beginTransaction();
			Query q = sesion.createSQLQuery(sql);
			q.executeUpdate();
			trans.commit();
			
		} catch (Exception error) {
			System.out.println("Error en ReciboCtrl.actualizarNoRecibo" + error);
		} finally {
			sesion.close();
		}
	}
/*************BUSCAR FICHA POR PARAMETROS***************************************************************/
	public static Recibo obtenerFichaCV(int numrec, int iCaid, String sCodcomp,
			String sCodsuc, String tiporec) {
		Recibo ficha = null;

		try {

			String sql = "from Recibo as r where r.id.numrec = " + numrec
					+ " and  r.id.caid = " + iCaid + " and r.id.codcomp = '"
					+ sCodcomp + "'";

			if (tiporec.trim().compareTo("") != 0)
				sql += " and r.id.tiporec = '" + tiporec + "' ";

			LogCajaService.CreateLog("obtenerFichaCV", "QRY", sql);
			
			@SuppressWarnings("unchecked")
			List<Recibo> recibos = (List<Recibo>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, false, Recibo.class);
			
			if(recibos == null || recibos.isEmpty() ){
				return null; 
			}
			
			ficha = recibos.get(0);

		} catch (Exception ex) {
			LogCajaService.CreateLog("obtenerFichaCV", "ERR", ex.getMessage());
			ex.printStackTrace(); 
		} 
		return ficha;
	}
/*******************************************************************************************************/
/**************ACTUALIZAR EL NUMERO DE FICHA AL RECIBO*************************************************/
	public boolean actualizarNoFicha(Session session,Transaction tx,int Numrec, int iNoFicha, int iCaid, String sCodcomp, String sCodsuc, String sTipoRec){
		boolean bActualizado = true;
		String sql = "update Recibo as r set r.recjde = "+iNoFicha+" where r.id.numrec = "+Numrec+" and r.id.caid ="+iCaid+" and r.id.tiporec = '"+sTipoRec+"' and r.id.codcomp = '" +sCodcomp+"' and r.id.codsuc = '"+sCodsuc+"'";		
		try{
			tx = session.beginTransaction();
			Query q = session.createQuery(sql);
			int rowCount = q.executeUpdate();
		    if (rowCount == 0){
		    	bActualizado = false;
		    }		
		}catch (Exception ex) {
			bActualizado = false;
			System.out.print("Se capturo una excepcion en el ReciboCtrl.actualizarNoFicha: " + ex);
		}
		return bActualizado;
	}
/******************************************************************************************************/
/**************ACTUALIZAR EL NUMERO DE FICHA AL RECIBO*************************************************/
	public boolean actualizarNoFicha(Connection cn,int Numrec, int iNoFicha, int iCaid, String sCodcomp, String sCodsuc, String sTipoRec){
		boolean bActualizado = true;
		PreparedStatement ps = null;
		String sql = "update "+PropertiesSystem.ESQUEMA+".Recibo as r set r.recjde = "+iNoFicha+" where r.numrec = "+Numrec+" and r.caid ="+iCaid+" and r.tiporec = '"+sTipoRec+"' and r.codcomp = '" +sCodcomp+"' and r.codsuc = '"+sCodsuc+"'";		
		try{
			ps = cn.prepareStatement(sql);
			int rs = ps.executeUpdate();
			
			if (rs != 1){
				bActualizado = false;
			}	
		}catch (Exception ex) {
			bActualizado = false;
			System.out.print("Se capturo una excepcion en el ReciboCtrl.actualizarNoFicha: " + ex);
		}finally {
			try {
				ps.close();
				//cn.close();
			} catch (Exception se2) {
				System.out.println("ERROR: Failed to close connection en ReciboCtrl.actualizarNoFicha: " + se2);
			}
		}
		return bActualizado;
	}
	
	/******************************************************************************************************/
	/**************ACTUALIZAR EL NUMERO DE FICHA AL RECIBO*************************************************/
		public boolean actualizarNoFichaWithSession(Session s,int Numrec, int iNoFicha, int iCaid, String sCodcomp, String sCodsuc, String sTipoRec){
			boolean bActualizado = true;
			
			String sql = "update "+PropertiesSystem.ESQUEMA+".Recibo as r set r.recjde = "+iNoFicha+" where r.numrec = "+Numrec+" and r.caid ="+iCaid+" and r.tiporec = '"+sTipoRec+"' and r.codcomp = '" +sCodcomp+"' and r.codsuc = '"+sCodsuc+"'";		
			try{
				ConsolidadoDepositosBcoCtrl.executeSqlQueryTx(s, sql);
			}catch (Exception ex) {
				bActualizado = false;
				System.out.print("Se capturo una excepcion en el ReciboCtrl.actualizarNoFicha: " + ex);
			}
			return bActualizado;
		}
/*******************OBTENER CUENTA DE VENTA DE CONTADO DE CAJA******************************************/
	public F55ca018 obtenerCuentaVentaContadoBK(int iCodCaja, String sCodComp, String sCodUnineg){
		F55ca018 f55ca018 = null;
		Session session = HibernateUtilPruebaCn.currentSession();
		
		String sql = "from F55ca018 as f where f.id.c8id = "+iCodCaja+" and trim(f.id.c8rp01) = '"+sCodComp.trim()+"' and trim(f.id.c8mcu) = '"+sCodUnineg.trim()+"' and f.id.c8stat = 'A'";
		try{
		
			f55ca018 = (F55ca018)session.createQuery(sql).uniqueResult();
		
		}catch(Exception ex){
			System.out.println("Se capturo una excepcion en ReciboContadoCtrl.obtenerCuentaVentaContadoBK: " + ex);
		}
		return f55ca018;
	}
/*****************************************************************************************************/
/*******************OBTENER CUENTA DE COMPRA VENTA DE CAJA******************************************/
	public F55ca024 obtenerCuentaCV(String sCodsuc, String sMoneda){
		F55ca024 f55ca024 = null;
		Session session = HibernateUtilPruebaCn.currentSession();
		
		String sql = "from F55ca024 as f where trim(f.id.d4mcu) = '"+sCodsuc+"' and f.id.d4crcd = '"+sMoneda+"' and f.id.d4stat = 'A'";
		try{
		
			f55ca024 = (F55ca024)session.createQuery(sql).uniqueResult();
		
		}catch(Exception ex){
			System.out.println("Se capturo una excepcion en ReciboCtrl.obtenerCuentaCV: " + ex);
		}
		return f55ca024;
	}
/*****************************************************************************************************/
/*****OBTIENE EL NUMERO DE BATCH EN EL QUE FUE PROCESADO EL RECIBO POR CAJA,SUCURSAL,COMPANIA Y NUMERO DE RECIBO****************************************************************************************************************************************/
	public Recibojde[] leerEnlaceReciboJDE(int iCaid,String sCodSuc,String sCodComp,int iNumrec, String sTipoDoc, String sTiporec){
		Session session = HibernateUtilPruebaCn.currentSession();
		
		String sql = "from Recibojde as r where r.id.caid = "+iCaid+" and r.id.codsuc = '" +sCodSuc+"'" +
						" and r.id.codcomp = '"+sCodComp+"' and r.id.numrec = " + iNumrec + " and r.id.tipodoc = '" + sTipoDoc + "' and r.id.tiporec = '" +sTiporec+ "'";
		List lstResult = new ArrayList();
		Recibojde[] recibojde = null;
		try{
		
			lstResult = session.createQuery(sql).list();
			recibojde = new Recibojde[lstResult.size()];
			for (int i = 0; i < lstResult.size(); i ++){
				recibojde[i] = (Recibojde)lstResult.get(i);		
			}
		
		}catch(Exception ex){
			System.out.println("Se capturo una excepcion en ReciboCtrl.leerNoBatchRecibo: " + ex);
		}finally{
			session.close();
		}
		return recibojde;
	}
	public static Recibojde[] leerEnlaceReciboJDE(int iCaid,String sCodSuc,String sCodComp,int iNumrec,String sTiporec){
		Session sesion = null;
		Recibojde[] recibojde = null;
		
		try{
			sesion = HibernateUtilPruebaCn.currentSession();
			
			String sql = "from Recibojde as r where r.id.caid = " + iCaid
					+ " and r.id.codcomp = '" + sCodComp
					+ "' and r.id.numrec = " + iNumrec ;
				
			if(sTiporec.compareTo("PM") == 0){
				sql += " and r.id.tiporec in ('PM', 'FCV') ";
			}else{
				sql +=  " and r.id.tiporec = '" + sTiporec + "'";
				
			}
	  
			LogCajaService.CreateLog("leerEnlaceReciboJDE", "QRY", sql);

			@SuppressWarnings("unchecked")
			List<Recibojde>lstResult = (ArrayList<Recibojde>)sesion.createQuery(sql).list();
			
			if(lstResult == null || lstResult.isEmpty())
				return null;
			
			recibojde = new Recibojde[lstResult.size()];
			lstResult.toArray(recibojde);
	 
		}catch(Exception ex){ 
			LogCajaService.CreateLog("leerEnlaceReciboJDE", "ERR", ex.getMessage());
			ex.printStackTrace(); 
		}

		return recibojde;
	}
/********************LLENAR ENLACE ENTRE RECIBO Y FACTURA*********************************************************************************/
	public boolean fillEnlaceReciboFac(Session session,Transaction tx,int iNumRec,
				String codcomp, int iNumFac,double Monto,String sTipoDoc, 
				int iCaId,String sCodSuc,String sPartida, String sCodunineg,
				String sTiporec, int iCodcli, int iFecha){
		boolean filled = false;
		
		//Session session = HibernateUtil.getSessionFactoryMCAJA().openSession();
		//Transaction tx = null;
		
		try {
			//tx = session.beginTransaction();
				Recibofac recibofac = new Recibofac();
				RecibofacId recibofacid = new RecibofacId();
				
				recibofacid.setNumfac(iNumFac);					//Numero factura
				recibofacid.setNumrec(iNumRec);					//Numero Recibo
				recibofacid.setTipofactura(sTipoDoc);			//Tipo de documento
				recibofacid.setCodcomp(codcomp);				//Cod. Compañía
				recibofacid.setPartida(sPartida);				//Partida en blanco para contado
				recibofacid.setCaid(iCaId);
				recibofacid.setCodsuc(sCodSuc);
				recibofacid.setCodunineg(sCodunineg.trim());
				recibofacid.setTiporec(sTiporec);
				recibofacid.setCodcli(iCodcli);
				recibofacid.setFecha(iFecha) ;
				
				recibofac.setId(recibofacid);					//Recibo
				recibofac.setMonto(BigDecimal.valueOf(Monto));  //Monto
				recibofac.setEstado("");
				session.save(recibofac);						//Factura
			//tx.commit();
			filled = true;	
		}catch(Exception ex){
			System.out.print("Exception -> ReciboContadoCtrl.fillEnlaceReciboFac: " + ex);
		}/*finally{
			session.close();
		}*/
		return filled;
	}

/********* 10  cargar los recibos pagados con monedas distintas del arqueo ******************/
	public List cargarRecibosOtrosIngEg(boolean bIngreso,int iCaid,String sCodcomp,String sCodsuc,String numRecibos,String sMoneda,Date dtFecha ){
		List lstCambios = new ArrayList();
		Session sesion = HibernateUtilPruebaCn.currentSession();
		
		String sConsulta = "";
		
		try{
			sConsulta = " from Vmonedafactrec as v where v.id.caid = "+iCaid+ " and v.id.codsuc ='"+sCodsuc+"'";			
			sConsulta += " and trim(v.id.codcomp) = '"+sCodcomp.trim()+"' and v.id.rmoneda <> '"+sMoneda+"'";
			sConsulta += " and v.id.mpago = '"+MetodosPagoCtrl.EFECTIVO+"' and v.id.fmoneda = '"+sMoneda+"'";
			sConsulta += " and v.id.fecha = '"+dtFecha+"'order by v.id.numrec";
			sConsulta += " and v.id.forarecibido > 0";
			
		
			lstCambios = sesion.createQuery(sConsulta).list();
					
			
		}catch(Exception error){
			System.out.println("Error en ReciboCtrl.cargarRecibosOtrosIngEg "+error);
		}	
		return lstCambios;
	}
	
	
/******** EGRESOS: obtener recibos pagados con moneda distinta del arqueo, distinta de la factura *********************/
//	public List obtenerEgresosRecMonEx(int iCaid, String sCodcomp,String sCodsuc,String sMoneda,Date dtFecha,Date dtHora,Date dtHini){
	public List obtenerEgresosRecMonEx(int iCaid, String sCodcomp,String sCodsuc,String sMoneda,
										Date dtFechaArqueo,Date dtHoraInicio,Date dtHoraFin,Session sesion){
		List lstRecibos = null;
//		List<Integer>lstNumeroRecibos = null;
		try{

			
			Criteria cr = sesion.createCriteria(Vmonedafactrec.class);
			cr.add(Restrictions.eq("id.caid"		,iCaid));
			cr.add(Restrictions.eq("id.codsuc"		,sCodsuc ));
			cr.add(Restrictions.eq("id.codcomp"		,sCodcomp ));
			cr.add(Restrictions.ne("id.rmoneda"		,sMoneda ));
			cr.add(Restrictions.eq("id.fmoneda"		,sMoneda ));
			cr.add(Restrictions.eq("id.fecha"		,dtFechaArqueo ));
			cr.add(Restrictions.ne("id.restado"		,"A" ));
			cr.add(Restrictions.gt("id.forarecibido",new BigDecimal("0")));
			cr.add(Restrictions.between("id.hora"	,dtHoraInicio,dtHoraFin));
			cr.addOrder(Order.asc("id.numrec"));

			
			lstRecibos = cr.list();
			if(lstRecibos==null)
				lstRecibos = new ArrayList(0);
			
					
		}catch(Exception error){
			lstRecibos = null;
			System.out.println("Error en ReciboCtrl.obtenerEgresosRecMonEx "+error);
		}
		return lstRecibos;
	}	
/*********** obtener recibos pagados con moneda de arqueo y distinta de la factura *********************/
	public List obtenerIngresoRecMonEx(int iCaid, String sCodcomp,String sCodsuc,String sMoneda,
										Date dtFechaArqueo,Date dtHoraInicio, Date dtHoraFin,Session sesion){
		List lstRecibos = new ArrayList();
//		Session sesion = HibernateUtil.getSessionFactoryMCAJA().openSession();
//		Transaction trans = null;
		String sConsulta = "",sFecha = "", sHini,sHfin; ;
		FechasUtil f = new FechasUtil();
		
		try{
			sFecha = f.formatDatetoString(dtFechaArqueo, "yyyy-MM-dd");
			sConsulta = " from Vmonedafactrec as v where v.id.caid = "+iCaid+ " and v.id.codsuc ='"+sCodsuc+"'";
			sConsulta += " and trim(v.id.codcomp) = '"+sCodcomp.trim()+"'";
			sConsulta += " and v.id.rmoneda = '"+sMoneda+"' and v.id.fmoneda <> '"+sMoneda+"'";
			sConsulta += " and v.id.fecha = '"+sFecha+"' and v.id.restado <>'A'";
			sConsulta += " and v.id.forarecibido > 0";
			
			sHini = f.formatDatetoString(dtHoraInicio,"HH.mm.ss");
			sHfin = f.formatDatetoString(dtHoraFin, "HH.mm.ss");
			sConsulta += " and v.id.hora >='"+sHini+"' and v.id.hora<='"+sHfin+"'";
			sConsulta += " and v.id.restado <>'A' order by v.id.numrec";
			
//			trans = sesion.beginTransaction();
			lstRecibos = sesion.createQuery(sConsulta).list();
//			trans.commit();			
			
		}catch(Exception error){
			lstRecibos = null;
			System.out.println("Error en ReciboCtrl.obtenerIngresoRecMonEx "+error);
		}finally{
//			sesion.close();
		}
		return lstRecibos;
	}	
/******** obtener el registro de un recibo a partir de numero,caja,compañia y sucursal ********************/
	public Recibo obtenerRegRecibo(int iNumrec,int iCaid,String sCodcomp,String sCodsuc, String tiporec){
		Recibo recibo = new Recibo();
		Session sesion = HibernateUtilPruebaCn.currentSession();
		
		String consulta = "";
		
		try{
		
			
			consulta = " from Recibo as r where r.id.numrec='" + iNumrec
					+ "' and r.id.caid='" + iCaid + "' "
					+ " and r.id.codcomp = '" + sCodcomp
					+ "' and r.id.codsuc = '" + sCodsuc + "'" 
					+ " and r.id.tiporec = '"+tiporec+"'";

			recibo = (Recibo)sesion.createQuery(consulta).uniqueResult();
				
			
		}catch(Exception error){
			System.out.println("Error en ReciboCtrl.obtenerRegRecibo "+error);
		}
		return recibo;
	}	
/***************  obtener el total de ingresos por tipo de recibo generado *******************************/
	public String obtenerTotalxTipoIngreso(int sCaid, String sCodcomp,String Moneda,String Tiporec){
		String total = "",consulta = "";
		Session sesion = HibernateUtilPruebaCn.currentSession();
		
		try{
		
			
			if(Moneda.equals("COR"))
				consulta = "select v.id.cord ";
			else
				consulta = "select v.id.usd ";	
			
			//filtros de la consulta.
			consulta += " from Vtotaltipoingreso as v where v.id.caid ='"+sCaid+"' and v.id.codcomp = '"+sCodcomp+"' " +
						" and v.id.tiporec = '"+Tiporec+"' ";			
			consulta += " and v.id.fecha = current_date";
						
			Object result = sesion.createQuery(consulta).uniqueResult();
			if(result != null){
				total = result.toString();
			}else
				total = "0";
			
		
			
		}catch(Exception error){
			System.out.println("Error en ReciboCtrl.obtenerTotalxTipoIngreso "+error);
		}
		return total;
	} 
		
/***************  obtener lista con los recibos del dia por tipo de ingresos  ***************************/
	public List obtenerRecibosxTipoIngreso(int sCaid,String sCodsuc, String sCodcomp, String Moneda, String Tiporec,
											Date dtFechaArqueo,Date dtHoraInicio,Date dtHoraFin,Session sesion){
		String consulta ="",sFecha="";
		List lstRec = new ArrayList();
		FechasUtil fechas = new FechasUtil();
//		Transaction trans = null;
//		Session sesion = HibernateUtil.getSessionFactoryMCAJA().openSession();
				
		try{
			sFecha = fechas.formatDatetoString(dtFechaArqueo, "yyyy-MM-dd");			
			consulta =  " from Vrecibosxtipoingreso as v where v.id.caid ="+sCaid;
			consulta +=	" and v.id.codcomp = '"+sCodcomp+"' and v.id.codsuc = '"+sCodsuc+"'";
			consulta += " and v.id.moneda ='"+Moneda+"' and v.id.tiporec ='"+Tiporec+"'";			
			consulta += " and v.id.fecha = '"+sFecha+"'";
			
			//--- Agregar filtro de intervalo de horas en caso de arqueos previos.
			String sHini = "", sHfin="";
			sHini = fechas.formatDatetoString(dtHoraInicio, "HH.mm.ss");
			sHfin = fechas.formatDatetoString(dtHoraFin,"HH.mm.ss");
			consulta += " and v.id.hora >='"+sHini+"' and v.id.hora<='"+sHfin+"'";
			consulta += " order by v.id.numrec";
//			trans = sesion.beginTransaction();
			lstRec = sesion.createQuery(consulta).list();			
//			trans.commit();			
			
		}catch(Exception error){
			lstRec = null;
			System.out.println("Error en ReciboCtrl.obtenerRecibosxTipoIngreso " +error);
		}
		return lstRec;
	}

/***********  verificar que existan recibos en el día, por caja,comp y moneda **************************/
	public boolean consultarExistRecibosHoy(int sCaid,String sCodcomp,String sTipMoneda){
		boolean existen = false;
		Session sesion = HibernateUtilPruebaCn.currentSession();
		
				
		try{
		
			
			String sql =
				" select count(*) from "+PropertiesSystem.ESQUEMA+".recibo as r inner join "+PropertiesSystem.ESQUEMA+".recibodet as rd on r.numrec = rd.numrec " +
				" and r.codcomp = rd.codcomp and r.codsuc = rd.codsuc and r.caid = rd.caid" +
				" where r.codcomp = '"+sCodcomp+"' and rd.moneda = '"+sTipMoneda+"' and r.caid = '"+sCaid+"'"; // and r.fecha = current_date";
					
			String cantSql = sesion.createSQLQuery(sql).uniqueResult().toString();			
		
			
			if(!cantSql.equals("0"))
				existen = true;
			
		}catch(Exception error){
			System.out.println("error en ReciboCtrl.consultarExistRecibosHoy() "+error);
		}
		return existen;
	}
/**********************************************************************************************/
	public int[] obtenerBatchxRecibo(int iNumrec, int iCaid, String sCodSuc, String sCodcomp, String sTipoDoc){
		List lstRecibojde = null;
		String sql = "select distinct r.id.nobatch from Recibojde as r where r.id.numrec = "+iNumrec+" and r.id.caid = "+iCaid+" and r.id.codsuc = '"+sCodSuc+"' and r.id.codcomp = '"+sCodcomp+"' and r.id.tipodoc = '" +sTipoDoc+"'";
		Session session = HibernateUtilPruebaCn.currentSession();
		
		int[] iNoBatch = null;
		try{
			
			lstRecibojde = session.createQuery(sql).list();
		
			
			iNoBatch = new int[lstRecibojde.size()];
			for (int i = 0; i < lstRecibojde.size(); i++){
				iNoBatch[i] = Integer.parseInt((lstRecibojde.get(i)).toString());
			}
		}catch(Exception ex){
			System.out.println("Se capturo una excepcion en ReciboCtrl.obtenerBatchxRecibo: " + ex);
		}
		return iNoBatch;
	}
	public static Integer[] obtenerBatchxRecibo2(int iNumrec, int iCaid, String sCodSuc, String sCodcomp,String sTiporec){
		Session sesion = null;
		
		boolean newCn = false;
		Integer[] iNoBatch = null;
		
		try{
			sesion = HibernateUtilPruebaCn.currentSession();
					
			@SuppressWarnings("unchecked")
			List<Integer> lstRecibojde = (ArrayList<Integer>) sesion
				.createCriteria(Recibojde.class)
				.add(Restrictions.eq("id.numrec", iNumrec))
				.add(Restrictions.eq("id.caid", iCaid))
				.add(Restrictions.eq("id.codcomp", sCodcomp))
				.add(Restrictions.eq("id.tiporec", sTiporec))
				.setProjection( Projections.distinct(Projections
							.property("id.nobatch"))).list();
			
			if(lstRecibojde == null || lstRecibojde.isEmpty())
				return null;
			
			iNoBatch = new Integer[lstRecibojde.size()];
			lstRecibojde.toArray(iNoBatch); 
			
			lstRecibojde = null;
			
			
		}catch(Exception ex){ 
			ex.printStackTrace(); 
		}
		return iNoBatch;
	}
	
/******************************************************************************************************/
/** Método: consultar los número de batch por documento pasando sesion como parámetro.
 *	Fecha:  08/01/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 **/
	public List<Object> obtenerBatchxRecibo2(int iNumrec, int iCaid, String sCodSuc, String sCodcomp,String sTiporec,Session sesion){

		List<Object> lstRecibojde = null;
		String sql = "";
		
		try {
			sql = sql.concat("select distinct r.id.nobatch from Recibojde as r where r.id.numrec = ");
			sql = sql.concat(String.valueOf(iNumrec)).concat("and r.id.caid = ").concat(String.valueOf(iCaid));
			sql = sql.concat("	and r.id.codsuc = '").concat(sCodSuc).concat("' and r.id.codcomp = '");
			sql = sql.concat(sCodcomp).concat("' and r.id.tiporec = '").concat(sTiporec).concat("'");
			
			lstRecibojde = (ArrayList<Object>)sesion.createQuery(sql).list();
					
		} catch (Exception error) {
			System.out.println("Error en  ReciboCtrl.obtenerBatchxRecibo2 " + error);
		} 
		return lstRecibojde;
	}
/******************************************************************************************************/
/** Método: Obtiene los recibos del día para una caja.
 *	Fecha:  08/01/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 **/
	@SuppressWarnings("unchecked")
	public List<Recibo> getAllRecibosCaja(int iCaid, String sCodSuc, int iMaxResult){
		List<Recibo> lstRecibos = null; 
		List<Object>lstNumBatch = null;
//		String sql = "";
		Session session = null;
		

		String sNobatch = "";
		
		try{
			session = HibernateUtilPruebaCn.currentSession();
			
			
			Criteria cr = session.createCriteria(Recibo.class);
			cr.add(Restrictions.eq("estado", ""));
			cr.add(Restrictions.eq("fecha", new Date()));
			cr.add(Restrictions.eq("id.caid", iCaid));
			cr.add(Restrictions.eq("id.codsuc", sCodSuc));
			cr.add(Restrictions.ne("id.tiporec", "FCV"));
			cr.addOrder(Order.desc("id.numrec"));
			if(iMaxResult > 0 )
				cr.setMaxResults(iMaxResult);
			lstRecibos = cr.list();
			
			//&& ======= Asignar Numero de batch asociados al recibo.
			Recibo r = null;
			for (int i = 0; i < lstRecibos.size(); i++) {
				 r  = lstRecibos.get(i);
				 sNobatch = "" ;
				 lstNumBatch = obtenerBatchxRecibo2(r.getId().getNumrec(), iCaid, sCodSuc, 
						 							r.getId().getCodcomp(), r.getId().getTiporec(), session);
				if (lstNumBatch != null && lstNumBatch.size() > 0) {
					for (Object ob : lstNumBatch)
						sNobatch = sNobatch.concat(String.valueOf(ob));
				}
				r.setNobatch(sNobatch);
				lstRecibos.set(i, r);
			}

		
			
		}catch(Exception ex){
			session.close();
			System.out.println("Se capturo una excepcion en ReciboCtrl.getAllRecibos: " + ex);
		}
		return lstRecibos;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Recibo> getRecibosCaja(int iCaid,Date dtFecha){
		List<Recibo> lstRecibos = null; 
		
		Session session = null;
		
		
		try{
			
			session = HibernateUtilPruebaCn.currentSession();
			
			Criteria cr = session.createCriteria(Recibo.class)
			.add(Restrictions.eq("fecha", dtFecha))
			.add(Restrictions.eq("id.caid", iCaid))
			.add(Restrictions.ne("id.tiporec", "FCV"))
			.addOrder(Order.desc("id.numrec"));
			
			lstRecibos = cr.list();
			
			if(lstRecibos == null ) 
				return new ArrayList<Recibo>();
			
			String[][] strCodUsuarioCreo = new String[lstRecibos.size()][2];
			int i = 0;
			for (Recibo rc : lstRecibos)
			{
				rc.setEstadodesc((rc.getEstado().compareTo("") == 0) ?	"Aplicado" : "Anulado");
				String strLoginUser="";
				UsuarioCtrl uctrl = new UsuarioCtrl();
				
//				System.out.println(i + " --> cod usuario " + rc.getCoduser() );
				if(i==0)
				{
					
					strCodUsuarioCreo[i][0] = rc.getCoduser();
					strLoginUser = uctrl.getLoginXCodUsuario(rc.getCoduser());
					strLoginUser = strLoginUser==null?"":strLoginUser;
					strCodUsuarioCreo[i][1] = strLoginUser.trim().equals("") ? rc.getCoduser() : strLoginUser;
					
					rc.setUsuariocreo(strCodUsuarioCreo[i][1]);
					
					
					i++;
				}
				else
				{
					boolean asigno = false;
					for(int j=0; j<i;j++)
					{
						if(strCodUsuarioCreo[j][0].trim().equals(rc.getCoduser().trim()))
						{
//							System.out.println(i + " -->r login " + strCodUsuarioCreo[j][1] );
							rc.setUsuariocreo(strCodUsuarioCreo[j][1]);
							asigno=true;
							break;
						}
					}
					
					if(!asigno)
					{
						strCodUsuarioCreo[i][0] = rc.getCoduser();
						strLoginUser = uctrl.getLoginXCodUsuario(rc.getCoduser());
						strLoginUser = strLoginUser==null?"":strLoginUser;
						strCodUsuarioCreo[i][1] = strLoginUser.trim().equals("") ? rc.getCoduser() : strLoginUser;
						
						rc.setUsuariocreo(strCodUsuarioCreo[i][1]);
						
						i++;
					}
				}

			}
			
			
		}catch(Exception ex){
			ex.printStackTrace();
		
		}
		return lstRecibos;
	}
	
	
/***********obtienen una lista de todos los recibos de caja********************************************************************************/
	public List getAllRecibos(int iCaid, String sCodSuc){
		List lstRecibos = null;
		Date d = new Date();
		String sql = "from Recibo as r where r.fecha = :pFecha and r.estado = '' and r.id.caid = "+iCaid+" and r.id.codsuc = '"+sCodSuc+"' and r.id.tiporec <> 'FCV'";
		Session session = HibernateUtilPruebaCn.currentSession();
		
		Recibo recibo = null;
		Integer[] iNobatch = null;
		String sNobatch = "";
		try{
				
			lstRecibos = session.createQuery(sql).setDate("pFecha", d)
			.list();
			
			
			for(int i = 0; i < lstRecibos.size();i++){
				recibo = (Recibo)lstRecibos.get(i);
				sNobatch = "";
				iNobatch = obtenerBatchxRecibo2(recibo.getId().getNumrec(),recibo.getId().getCaid(),recibo.getId().getCodsuc(),recibo.getId().getCodcomp(),recibo.getId().getTiporec());
				for(int j = 0; j < iNobatch.length; j++){
					sNobatch = sNobatch +" " +iNobatch[j];
					
				}
				recibo.setNobatch(sNobatch);
				lstRecibos.remove(i);
				lstRecibos.add(i, recibo);
			}
		}catch(Exception ex){
			System.out.println("Se capturo una excepcion en ReciboCtrl.getAllRecibos: " + ex);
		}
		return lstRecibos;
	}
	public List getAllRecibos(int iCaid, String sCodSuc,String sCodComp){
		List lstRecibos = null;
		Date d = new Date();
		String sql = "from Recibo as r where r.id.fecha = :pFecha and r.id.estado = '' and r.id.caid = "+iCaid+" and r.id.codsuc = '"+sCodSuc+"' and r.id.codcomp = '"+sCodComp+"' and r.id.tiporec <> 'FCV'";
		Session session = HibernateUtilPruebaCn.currentSession();
		
		try{
			
			lstRecibos = session.createQuery(sql).setDate("pFecha", d).list();
		
			
		}catch(Exception ex){
			System.out.println("Se capturo una excepcion en ReciboCtrl.getAllRecibos: " + ex);
		}
		return lstRecibos;
	}
/************Obtiene los numeros de recibo contenidos en un rango de numeros recibos*****************************************************************************/
	public int[] getNumerosRecibos(int iCaid, String sCodSuc, String sNumRec){
		int[] iNumeros = null;
		List lstRecibos = null;
		String sql = "select distinct r.id.numrec from Recibo as r where r.fecha = current_date and r.estado = '' and r.id.caid = "+iCaid+" and r.id.codsuc = '"+sCodSuc+"' and cast(r.id.numrec as string) like '" + sNumRec.trim()+"%'";
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();	
			lstRecibos = session.createQuery(sql).list();
			tx.commit();
			iNumeros = new int[lstRecibos.size()];
			for(int i = 0; i < lstRecibos.size(); i++){
				iNumeros[i] = Integer.parseInt(lstRecibos.get(i).toString());
			}
		}catch(Exception ex){
			System.out.println("Se capturo una excepcion en ReciboCtrl.getRecibosxNumero: " + ex);
		}finally{
			session.close();
		}
		return iNumeros;
	}
/*********obtiene los numeros de factura parecidos al numero ingresado***********************************************************************************/
	public int[] getNumerosFactura(int iCaid, String sCodSuc, String sNumfac){
		int[] iNumeros = null;
		List lstRecibos = null;
		String sql = "select distinct r.id.numfac from Vrecfac as r where r.id.fecha = current_date and r.id.caid = "+iCaid+" and r.id.codsuc = '"+sCodSuc+"' and cast(r.id.numfac as string) like '" + sNumfac.trim()+"%'";
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();	
			lstRecibos = session.createQuery(sql).list();
			tx.commit();
			iNumeros = new int[lstRecibos.size()];
			for(int i = 0; i < lstRecibos.size(); i++){
				iNumeros[i] = Integer.parseInt(lstRecibos.get(i).toString());
			}
		}catch(Exception ex){
			System.out.println("Se capturo una excepcion en ReciboCtrl.getNumerosFactura: " + ex);
		}finally{
			session.close();
		}
		return iNumeros;
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<Recibo> buscarRecibo(int caid, String codcomp, String tiporec,
			String valor, int filtrarPor, Date dtFecha) {
		ArrayList<Recibo>recibos = null;
		Session sesion = null;
		List<Integer>recibosxfacs = new ArrayList<Integer>();

		try {
			
			sesion = HibernateUtilPruebaCn.currentSession();
			
			//&& ======== Consulta por numero de factura.
			if (filtrarPor == 4 && valor.matches(PropertiesSystem.REGEXP_NUMBER)) {
				Criteria crRf = sesion.createCriteria(Recibofac.class);
				crRf.add(Restrictions.eq("id.numfac",Integer
						.parseInt(valor)));
				crRf.setProjection(Projections.property("id.numrec"));
				recibosxfacs = (List<Integer>)crRf.list();
				
				if(recibosxfacs == null || recibosxfacs.isEmpty())
					return null ;
			}
			Criteria cr = sesion.createCriteria(Recibo.class)
					.add(Restrictions.eq("fecha", dtFecha))
					.add(Restrictions.eq("id.caid", caid))
					.add(Restrictions.ne("id.tiporec", "FCV"));

			valor = valor.trim().toLowerCase();
			boolean paramValido = 
				(valor.matches(PropertiesSystem.REGEXP_AMOUNT )&& filtrarPor == 5 ) ||
				(valor.matches(PropertiesSystem.REGEXP_NUMBER )&& filtrarPor > 1 && filtrarPor < 5) ||
				(valor.matches(PropertiesSystem.REGEXP_DESCRIPTION) && filtrarPor == 1 );
		
			if(  valor.compareTo("") != 0 && !paramValido ) 
				return new ArrayList<Recibo>();
			
			if(paramValido){
				if(filtrarPor == 1)
					cr.add(Restrictions.sqlRestriction("lower(cliente) like '%"+valor+"%'"));
				if(filtrarPor == 2 )
					cr.add(Restrictions.eq("codcli", Integer.parseInt(valor)));
				if(filtrarPor == 3 )
					cr.add(Restrictions.eq("id.numrec", Integer.parseInt(valor)));
				if(filtrarPor == 4 )
					cr.add(Restrictions.in("id.numrec", recibosxfacs)) ;
				if(filtrarPor == 5 ){
					BigDecimal max = new BigDecimal(valor).add(BigDecimal.TEN);
					BigDecimal min = new BigDecimal(valor).subtract(BigDecimal.TEN);
					if(min.compareTo(BigDecimal.ZERO) == -1)
						min = BigDecimal.ZERO;
					cr.add(Restrictions.between("montoapl", min, max));
					cr.addOrder(Order.desc("montoapl"));
				}
			}
			if(codcomp.compareTo("01") != 0)
				cr.add(Restrictions.eq("id.codcomp", codcomp));
			if(tiporec.compareTo("01") != 0)
				cr.add(Restrictions.eq("id.tiporec", tiporec));
			
			LogCajaService.CreateLog("buscarRecibo", "HQRY", LogCajaService.toSql(cr));
			
			recibos = (ArrayList<Recibo>)cr.list();
			String strLoginUser="";
			UsuarioCtrl uctrl = new UsuarioCtrl();
			
			if(recibos != null && !recibos.isEmpty()){
				String[][] strCodUsuarioCreo = new String[recibos.size()][2];
				int i = 0;
				for (Recibo rc : recibos) {
					rc.setEstadodesc((rc.getEstado().compareTo("") == 0) ?
							"Aplicado" : "Anulado");
					
					if(i==0)
					{
						
						strCodUsuarioCreo[i][0] = rc.getCoduser();
						strLoginUser = uctrl.getLoginXCodUsuario(rc.getCoduser());
						strCodUsuarioCreo[i][1] = (strLoginUser == null || strLoginUser.trim().equals("")) ? rc.getCoduser() : strLoginUser;
						rc.setUsuariocreo(strCodUsuarioCreo[i][1]);
						i++;
					}
					else
					{
						boolean asigno = false;
						for(int j=0; j<i;j++)
						{
							if(strCodUsuarioCreo[j][0].trim().equals(rc.getCoduser().trim()))
							{
								rc.setUsuariocreo(strCodUsuarioCreo[j][1]);
								asigno=true;
								break;
							}
						}
						
						if(!asigno)
						{
							strCodUsuarioCreo[i][0] = rc.getCoduser();
							strLoginUser = uctrl.getLoginXCodUsuario(rc.getCoduser());
							strCodUsuarioCreo[i][1] = (strLoginUser == null || strLoginUser.trim().equals("")) ? rc.getCoduser() : strLoginUser;
							rc.setUsuariocreo(strCodUsuarioCreo[i][1]);							
							i++;
						}
					}
				}
			}
			cr = null;
		} catch (Exception e) {
			e.printStackTrace();
			LogCajaService.CreateLog("buscarRecibo", "ERR", e.getMessage());
		}finally{
			HibernateUtilPruebaCn.closeSession(sesion);
		}
		return recibos;
	}
/***************OBTIENE LOS RECIBOS DEL DIA POR CAJA,COMPANIA,TIPO DE RECIBO Y Y TIPO DE BUSQUEDA***************************************************************************************************************/
	public List getRecibosxParametros(int iCaid, String sCodsuc, String sCodComp,String sTipoRecibo,String sParametro,int iTipousqueda){
		List lstRecibos = null;
		String sql = "";
		Session session = HibernateUtilPruebaCn.currentSession();
	
		Vrecfac vrecfac = null;
		Recibo recibo = null;
		ReciboId reciboId = null;
		int[] iNobatch = null;
		String sNobatch = "";
		try{
			
			String fecha = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			
			
			
			sql = "from Recibo as r  where fecha = '"+fecha+"'" +
					" and r.estado = '' and r.id.caid = "
					+ iCaid
					+ " and r.id.codsuc = '"
					+ sCodsuc
					+ "' and r.id.tiporec <> 'FCV' ";
			
			switch (iTipousqueda){
				case(1)://busqueda por nombre de cliente
					if(!sParametro.equals("")){
						sql = sql + " and r.cliente like '%"+sParametro.toUpperCase().trim()+"%'";
					}
					break;
				case(2)://busqueda por codigo de cliente
					if(!sParametro.equals("")){
						sql = sql + " and cast(r.codcli as string) like '"+sParametro.trim()+"%'";
					}
					break;
				case(3)://busqueda por numero de recibo
					if(!sParametro.equals("")){
						sql = sql + " and cast(r.id.numrec as string) like '"+sParametro.trim()+"%'";
					}
					break;
				case(4)://busqueda por numero de factura
					sql = "from Vrecfac as r where r.id.fecha = current_date and r.id.estado = '' and r.id.caid = "+iCaid+" and r.id.codsuc = '"+sCodsuc+"' ";
					if(!sParametro.equals("")){						
						sql = sql + " and cast(r.id.numfac as string) like '"+sParametro.trim()+"%'";
					}
					break;
			}
			if(!sCodComp.equals("01")){
				sql = sql + " and r.id.codcomp = '"+sCodComp+"'";
			}
			if(!sTipoRecibo.equals("01")){
				sql = sql + " and r.id.tiporec = '"+sTipoRecibo+"'";
			}
				
			
			lstRecibos = session.createQuery(sql)
						.setFirstResult(0)
						.list();
			
			
			if(iTipousqueda == 4){
				for(int i = 0; i < lstRecibos.size();i++){
					vrecfac = (Vrecfac)lstRecibos.get(i);
					recibo = new Recibo();
					reciboId = new ReciboId();
					 
					reciboId.setCaid(vrecfac.getId().getCaid());
					reciboId.setNumrec(vrecfac.getId().getNumrec());
					reciboId.setCodcomp(vrecfac.getId().getCodcomp());
					reciboId.setCodsuc(vrecfac.getId().getCodsuc());
					reciboId.setTiporec(vrecfac.getId().getTiporec());
					
					recibo.setId(reciboId);
					
					recibo.setMontoapl(vrecfac.getId().getMontoapl());
					recibo.setMontorec(vrecfac.getId().getMontorec());
					recibo.setConcepto(vrecfac.getId().getConcepto());
					recibo.setFecha(vrecfac.getId().getFecha());
					recibo.setCliente(vrecfac.getId().getCliente());
					recibo.setCodcli(vrecfac.getId().getCodcli());
					recibo.setCajero(vrecfac.getId().getCajero());	
					recibo.setHora(vrecfac.getId().getHora());
					recibo.setNumrecm(vrecfac.getId().getNumrecm());
					recibo.setRecjde(vrecfac.getId().getRecjde());	
					recibo.setEstado(vrecfac.getId().getEstado());
					recibo.setMotivo(vrecfac.getId().getMotivo());
					recibo.setCodusera(vrecfac.getId().getCodusera());
					recibo.setHoramod(vrecfac.getId().getHoramod());
					recibo.setCoduser(vrecfac.getId().getCoduser());
					
					iNobatch = obtenerBatchxRecibo(recibo.getId().getNumrec(),recibo.getId().getCaid(),recibo.getId().getCodsuc(),recibo.getId().getCodcomp(),recibo.getId().getTiporec());
					sNobatch = "";
					for(int j = 0; j < iNobatch.length; j++){
						sNobatch = sNobatch +" " +iNobatch[j];
						
					}
					recibo.setNobatch(sNobatch);
					
					lstRecibos.remove(i);
					lstRecibos.add(i, recibo);
				}
			}
		}catch(Exception ex){
			LogCajaService.CreateLog("getRecibosxParametros", "ERR", ex.getMessage());
		}finally{
			session.close();
		}
		return lstRecibos;
	}
/*****OBTIENE EL NUMERO DE BATCH EN EL QUE FUE PROCESADO EL RECIBO POR CAJA,SUCURSAL,COMPANIA Y NUMERO DE RECIBO****************************************************************************************************************************************/
	public int leerNoBatchRecibo(int iCaid,String sCodSuc,String sCodComp,int iNumrec,String sTipodoc, String sTipoRec){
		Session session = HibernateUtilPruebaCn.currentSession();
		
		String sql = "select distinct r.id.nobatch from Recibojde as r where r.id.caid = "+iCaid+" and r.id.codsuc = '" +sCodSuc+"'" +
						" and r.id.codcomp = '"+sCodComp+"' and r.id.numrec = " + iNumrec + " and r.id.tipodoc = '"+sTipodoc+"' and r.id.tiporec = '" +sTipoRec+ "'";
		int iNoBatch = 0;
		try{
			
			iNoBatch = Integer.parseInt(session.createQuery(sql).uniqueResult().toString());
		
		}catch(Exception ex){
			LogCajaService.CreateLog("leerNoBatchRecibo", "ERR", ex.getMessage());
		}finally{
			session.close();
		}
		return iNoBatch;
	}
/*******OBTIENE UNA LISTA DEL DETALLE DE RECIBO************************************************************************************************************************************/
	@SuppressWarnings("unchecked")
	public static  List<Recibodet> leerDetalleRecibo(int iCaid,String sCodSuc,String sCodComp,int iNumrec, String sTipoRec){
		
		List<Recibodet> lstDetalleRec = new ArrayList<Recibodet>();
		Session sesion = null;
		
		
		
		try {
			sesion = HibernateUtilPruebaCn.currentSession();			
			
			lstDetalleRec = (ArrayList<Recibodet>)sesion
				.createCriteria(Recibodet.class)
				.add(Restrictions.eq("id.caid",iCaid ))
				.add(Restrictions.eq("id.codcomp",sCodComp ))
				.add(Restrictions.eq("id.numrec", iNumrec ))
				.add(Restrictions.eq("id.tiporec", sTipoRec)).list();
			
		} catch (Exception e) {
			LogCajaService.CreateLog("leerDetalleRecibo", "ERR", e.getMessage());
			e.printStackTrace();
		}
		return lstDetalleRec;
	}
/*******************************************************************************************************************************************/
/*******OBTIENE EL DETALLE DEL CAMBIO GENERADO EN EL RECIBO************************************************************************************************************************************/
	public static Cambiodet[] leerDetalleCambio(int iCaid,String sCodSuc,String sCodComp,int iNumrec, String tiporec){
		Session sesion = null;
		Cambiodet[] cambio = null;
		
		try{
			
			String sql = "from Cambiodet as r where r.id.caid = " + iCaid
					+ " and r.id.codcomp = '" + sCodComp
					+ "' and r.id.numrec = " + iNumrec 
					+ " and r.id.tiporec = '"+tiporec+"' " ;
			
			sesion = HibernateUtilPruebaCn.currentSession();
			LogCajaService.CreateLog("leerDetalleCambio", "QRY", sql);
			
			@SuppressWarnings("unchecked")
			List<Cambiodet>lstDetalleCambio = (ArrayList<Cambiodet>) sesion.createQuery(sql).list();

			if(lstDetalleCambio == null || lstDetalleCambio.isEmpty())
				return null;
			
			cambio = new Cambiodet[lstDetalleCambio.size()];
			lstDetalleCambio.toArray(cambio);
			
		}catch(Exception ex){
			LogCajaService.CreateLog("leerDetalleCambio", "ERR", ex.getMessage());
			ex.printStackTrace(); 
			
		}

		return cambio;
	}
/*********OBTIENE LAS FACTURAS QUE FUERON PROCESADAS EN UN RECIBO(RECIBOFAC)**********************************************************************************************************************************/
	@SuppressWarnings({"unchecked","rawtypes"})
	public List leerFacturasRecibo(int iCaid,String sCodComp,int iNumrec, String sTiporec, int iCodcli, int iFecha){
		
		List lstFacturasRecibo = new ArrayList(), lstRecibofac = null;
		FacturaxRecibo facturaRec = null;
		Hfactjdecon hfac = null;
		Recibofac recFac = null;
		Session session = HibernateUtilPruebaCn.currentSession();
		
		
		String sql = "from Recibofac as r where r.id.caid = " + iCaid
				+ " and r.id.codcomp = '" + sCodComp.trim()
				+ "' and r.id.numrec = " + iNumrec + " and r.id.tiporec = '"
				+ sTiporec + "' and r.id.codcli = "+iCodcli +" and r.id.fecha = "+iFecha;
		
		try{
			
			lstRecibofac = session.createQuery(sql).list();
			
			for(int i = 0; i < lstRecibofac.size();i++){
				recFac = (Recibofac)lstRecibofac.get(i);
				
				hfac = getInfoFactura(recFac.getId().getNumfac(),sCodComp, 
							recFac.getId().getTipofactura(),
							recFac.getId().getCodunineg(),iCodcli, iFecha);
				
				JulianToCalendar fecha = new JulianToCalendar(hfac.getId().getFecha());
				facturaRec = new FacturaxRecibo(hfac.getId().getNofactura(),
							hfac.getId().getTipofactura(),hfac.getId().getUnineg(),
							recFac.getMonto(),hfac.getId().getMoneda(),
							fecha.toString(),"");
				
				lstFacturasRecibo.add(facturaRec);
			}
			
		}catch(Exception ex){
			LogCajaService.CreateLog("leerFacturasRecibo", "ERR", ex.getMessage());
		}finally{
			session.close();
		}
		return lstFacturasRecibo;
	}
	
	public Hfactjdecon getInfoFactura(int iNumfac,String sCodComp,String sTipoDocumento,String sCodunineg, int iCodcli, int iFecha){
		Hfactjdecon factura = null;
		FacturaxRecibo facturaRec = null;
		Session session = HibernateUtilPruebaCn.currentSession();
		
	
		String sql = "from Hfactjdecon as hf where hf.id.nofactura = "
				+ iNumfac + " and hf.id.codcomp = '" + sCodComp.trim()
				+ "' and hf.id.tipofactura = '" + sTipoDocumento + "' and "
				+ "trim(hf.id.codunineg) = '" + sCodunineg + "'" 
				+" and hf.id.codcli = "+iCodcli +" and hf.id.fecha = "+iFecha;
		
		try{
			
			
			factura = (Hfactjdecon)session.createQuery(sql).uniqueResult();
		
			
		}catch(Exception ex){
			LogCajaService.CreateLog("leerFacturasRecibo", "ERR", ex.getMessage());
		}finally{
			session.close();
		}
		return factura;
	}
	
	
/**************************************************************************************************************/
	public List leerFacturasReciboCredito(int iCaid,String sCodComp,int iNumrec,String sTiporec, int iCodcli){
		
		List lstFacturasRecibo = new ArrayList(), lstRecibofac = null;
		FacturaxRecibo facturaRec = null;
		Hfacturajde hfac = null;
		Recibofac recFac = null;
		Session session = HibernateUtilPruebaCn.currentSession();
		
		String sql = "";
		try{
			sql = "from Recibofac as r where r.id.caid = " + iCaid
					+ " and r.id.codcomp = '" + sCodComp.trim()
					+ "' and r.id.numrec = " + iNumrec
					+ " and r.id.tiporec = '" + sTiporec + "'" 
					+" and r.id.codcli = " +iCodcli;
			
			
			lstRecibofac = session.createQuery(sql).list();
			
			
			for(int i = 0; i < lstRecibofac.size();i++){
				recFac = (Recibofac)lstRecibofac.get(i);
				
				hfac = getInfoFacturaCredito(recFac.getId().getNumfac(),sCodComp, 
							recFac.getId().getTipofactura(),
							recFac.getId().getPartida(),
							recFac.getId().getCodunineg(),
							recFac.getId().getCodcli());
				
				JulianToCalendar fecha = new JulianToCalendar(hfac.getId().getFecha());
				facturaRec = new FacturaxRecibo(hfac.getId().getNofactura(),hfac.getId().getTipofactura(),hfac.getId().getUnineg(),recFac.getMonto(),hfac.getId().getMoneda(),fecha.toString(),hfac.getId().getPartida());
				lstFacturasRecibo.add(facturaRec);
			}
		}catch(Exception ex){
			LogCajaService.CreateLog("leerFacturasReciboCredito", "ERR", ex.getMessage());
		}
		return lstFacturasRecibo;
	}
	///
public List leerFacturasReciboCredito2(int iCaid,String sCodComp,int iNumrec,
						String sTiporec, int iCodcli){
		
		List<Hfacturajde> lstFacturasRecibo = new ArrayList<Hfacturajde>();
		List<Recibofac> lstRecibofac = null;
		FacturaxRecibo facturaRec = null;
		Hfacturajde hfac = null;
		Recibofac recFac = null;
		Session session = HibernateUtilPruebaCn.currentSession();
		
		
		String sql = "from Recibofac as r where r.id.caid = " + iCaid
				+ " and r.id.codcomp = '" + sCodComp.trim()
				+ "' and r.id.numrec = " + iNumrec + " and r.id.tiporec = '"
				+ sTiporec + "' and r.id.codcli = "+iCodcli;
		try{
			
			lstRecibofac = session.createQuery(sql).list();
			
			
			for(int i = 0; i < lstRecibofac.size();i++){
				
				recFac = (Recibofac)lstRecibofac.get(i);
				hfac = getInfoFacturaCredito(recFac.getId().getNumfac(),
						sCodComp,recFac.getId().getTipofactura(), 
						recFac.getId().getPartida(),
						recFac.getId().getCodunineg(),
						iCodcli);
				
				JulianToCalendar fecha = new JulianToCalendar(hfac.getId().getFecha());
				lstFacturasRecibo.add(hfac);
			}
		}catch(Exception ex){
			LogCajaService.CreateLog("leerFacturasReciboCredito2", "ERR", ex.getMessage());
		}
		return lstFacturasRecibo;
	}
/**************************************************************************************************************/
	public Hfactjdecon getInfoFactura(int iNumfac,String sCodComp,String sTipoDocumento,String sCodunineg, int iCodcli){
		Hfactjdecon factura = null;
		FacturaxRecibo facturaRec = null;
		Session session = HibernateUtilPruebaCn.currentSession();
		
		String sql = "from Hfactjdecon as hf where hf.id.nofactura = "
				+ iNumfac + " and hf.id.codcomp = '" + sCodComp.trim()
				+ "' and hf.id.tipofactura = '" + sTipoDocumento + "' and "
				+ "trim(hf.id.codunineg) = '" + sCodunineg + "'" 
				+" and hf.id.codcli = "+iCodcli ;
		try{
			
			factura = (Hfactjdecon)session.createQuery(sql).uniqueResult();
		
		}catch(Exception ex){
			LogCajaService.CreateLog("getInfoFactura", "ERR", ex.getMessage());
		}finally{
			session.close();
		}
		return factura;
	}
/**************************************************************************************************************/
	public Hfacturajde getInfoFacturaCredito(int iNumfac,String sCodComp,
								String sTipoDocumento, String sPartida,
								String sCodunineg, int iCodcli){
		Hfacturajde factura = null;
		FacturaxRecibo facturaRec = null;
		Session session = HibernateUtilPruebaCn.currentSession();
		
		
		String sql = "from Hfacturajde as hf where hf.id.nofactura = "
				+ iNumfac + " and hf.id.codcomp = '" + sCodComp.trim()
				+ "' and hf.id.tipofactura = '" + sTipoDocumento
				+ "' and hf.id.partida = '" + sPartida
				+ "' and hf.id.ctotal > 0 and trim(codunineg) = '" + sCodunineg.trim()
				+ "' and hf.id.codcli = "+iCodcli;
		try{
			
			factura = (Hfacturajde)(session.createQuery(sql).list()).get(0);
			
		}catch(Exception ex){
			LogCajaService.CreateLog("getInfoFacturaCredito", "ERR", ex.getMessage());
		}finally{
			session.close();
		}
		return factura;
	}
/******ACTUALIZA EL ESTADO DEL RECIBO A ANULADO********************************************************************************************************/
	 public boolean actualizarEstadoRecibo(Session session,Transaction tx,int iNumrec,int iCaid,String sCodsuc, String sCodcomp,String sCodUsera,String sMotivo, String sTipoRec){
		 boolean bActualizado = true; 
		 boolean bUnico = false;
		 
		 try{
			 
			 if(session == null){
				 session = HibernateUtilPruebaCn.currentSession();
				 tx = session.beginTransaction();
				 bUnico = true;
			 }
			String sUpdate = "update Recibo as r set r.estado=:sEstado, " +
					"r.codusera=:sCodUsera, r.motivo=:sMotivo, " +
					"r.horamod = current_time, fecham = current_date " +
					"where r.id.numrec=:iNumrec and r.id.caid=:iCaid and " +
					"r.id.codcomp=:sCodcomp and r.id.codsuc=:sCodsuc " +
					"and r.id.tiporec=:sTipoRec";
			
			 Query q = session.createQuery(sUpdate);
		     q.setString("sEstado", "A");
		     q.setString("sCodUsera", sCodUsera);
		     q.setString("sMotivo", sMotivo);
		        
		     q.setInteger("iNumrec",iNumrec);
		     q.setInteger("iCaid",iCaid);
		     q.setString("sCodcomp", sCodcomp);
		     q.setString("sCodsuc", sCodsuc);
		     q.setString("sTipoRec", sTipoRec);
		       
		     int rowCount = q.executeUpdate();
		     if (rowCount == 0) bActualizado = false;
		     
		     if(bUnico) tx.commit();
		     
		 }catch(Exception ex){
			 bActualizado = false;
			 LogCajaService.CreateLog("actualizarEstadoRecibo", "ERR", ex.getMessage());
		 }finally{
			 try{if(bUnico) session.close();}catch(Exception ex2){ex2.printStackTrace();};
		 }
		 return bActualizado;
	 }
	/******BORRA EL REGISTRO CORRESPONDIENTE AL ENLACE RECIBO-FACTURA*********************************************************************************************************/
	 public boolean actualizarEnlaceReciboFac(Session session, Transaction tx,Recibofac recibofac){
	
		 boolean bBorrado = true,activo = false;
		 try{
			 if(session == null){
				 session = HibernateUtilPruebaCn.currentSession();
				 tx = session.beginTransaction();
				 activo = true;
			 }
			
			 session.update(recibofac);
			 if(activo)tx.commit();	 
		 }catch(Exception ex){
			 LogCajaService.CreateLog("actualizarEnlaceReciboFac", "ERR", ex.getMessage());
			 bBorrado = false;
		 }finally{
			 if(activo)
				 try{session.close();}catch(Exception ex2){ex2.printStackTrace();};
		 }
		 return bBorrado;
	 }
	 
	 public boolean actualizarEnlaceReciboFac(Connection cn, Hfacturajde hFac, 
			 		int iNumrec, int iCaid, int iCodcli){
	
		 String sql = "update " + PropertiesSystem.ESQUEMA
				+ ".recibofac set estado = 'A' where numrec = " + iNumrec
				+ " and caid = " + iCaid + " and tipofactura = '"
				+ hFac.getId().getTipofactura() + "' and partida = '"
				+ hFac.getId().getPartida() + "' and codcomp = '"
				+ hFac.getId().getCodcomp().trim() + "' and numfac = "
				+ hFac.getId().getNofactura() +" and codcli = " + iCodcli;
		 
		 PreparedStatement ps = null;
		 boolean bBorrado = true;
		 try{
			 
			 ps = cn.prepareStatement(sql);
				int rs = ps.executeUpdate();
				if (rs == 0){
					bBorrado = false;
				}
		 }catch(Exception ex){
			 LogCajaService.CreateLog("actualizarEnlaceReciboFac", "ERR", ex.getMessage());
			 bBorrado = false;
		 }
		 return bBorrado;
	 }
	 public boolean actualizarEnlaceReciboFac(Connection cn, Vf0311fn hFac, int iNumrec, 
			 					int iCaid,String sCodcomp, int iCodcli){
		 
		String sql = "update " + PropertiesSystem.ESQUEMA
				+ ".recibofac set estado = 'A' where numrec = " + iNumrec
				+ " and caid = " + iCaid + " and tipofactura = '"
				+ hFac.getId().getTipofactura() + "' and partida = '"
				+ hFac.getId().getPartida() + "' and codcomp = '"
				+ sCodcomp.trim() + "' and numfac = "
				+ hFac.getId().getNofactura() +" and codcli = " + iCodcli;
		
		 PreparedStatement ps = null;
		 boolean bBorrado = true;
		 
		 try{
			 
			 ps = cn.prepareStatement(sql);
				int rs = ps.executeUpdate();
				if (rs == 0){
					bBorrado = false;
				}
		 }catch(Exception ex){
			 LogCajaService.CreateLog("actualizarEnlaceReciboFac", "ERR", ex.getMessage());
			 bBorrado = false;
		 }
		 return bBorrado;
	 }
/********OBTIENE LA INFORMACION DEL ENLACE ENTRE RECIBO DE CAJA Y JDE (RECIBOJDE)*****************************************************************************************************/
	 public List getEnlaceReciboJDE(int iCaid,String sCodSuc,String sCodComp,int iNumrec,String sTipoDoc,String sTipoRec){
		 Session session3 = HibernateUtilPruebaCn.currentSession();
		 Transaction tx3 = null;
		String sql = "from Recibojde as r where r.id.caid = " + iCaid
				+ " and r.id.codsuc = '" + sCodSuc + "'"
				+ " and r.id.codcomp = '" + sCodComp + "' and r.id.numrec = "
				+ iNumrec + " and r.id.tipodoc = '" + sTipoDoc
				+ "' and r.id.tiporec = '" + sTipoRec + "'";
		 List lstRecibojde = null;
		 try{
			 tx3 = session3.beginTransaction();
			 lstRecibojde = session3.createQuery(sql).list();
			 tx3.commit();
		 }catch(Exception ex){
			 LogCajaService.CreateLog("getEnlaceReciboJDE", "ERR", ex.getMessage());
		 }finally{
			 try{session3.close();}catch(Exception ex2){ex2.printStackTrace();};
		 }
		 return lstRecibojde;
	 }
	 
	@SuppressWarnings("unchecked")
	public static List<Recibojde> numerosBatchsPorRecibo(int iCaid, String sCodSuc,
			String sCodComp, int iNumrec, String sTipoRec) {
		List<Recibojde> lstRecibojde = null;

		try {

			String sql = "from Recibojde as r " 
					+ " where r.id.caid = " + iCaid
					+ " and r.id.codsuc = '" + sCodSuc + "'"
					+ " and r.id.codcomp = '" + sCodComp +"' "
					+ " and r.id.numrec = " + iNumrec
					+ " and r.id.tiporec  in ('SBR','" + sTipoRec + "' ) ";

			lstRecibojde = (List<Recibojde>) ConsolidadoDepositosBcoCtrl
					.executeSqlQuery(sql, false, Recibojde.class);

		} catch (Exception ex) {
			lstRecibojde = null;
			LogCajaService.CreateLog("numerosBatchsPorRecibo", "ERR", ex.getMessage());
		}
		return lstRecibojde;
	}
	 
	 public List<Recibojde> getEnlaceReciboJDE(int iCaid,String sCodSuc,String sCodComp,int iNumrec,String sTipoRec){
		 List<Recibojde> lstRecibojde = null;
			
		 try{

			String sql = "from Recibojde as r where r.id.caid = "+iCaid+" and r.id.codsuc = '" +sCodSuc+"'" +
						" and r.id.codcomp = '"+sCodComp+"' and r.id.numrec = " + iNumrec + " and r.id.tiporec = '" +sTipoRec+ "'";
						
			lstRecibojde = ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, Recibojde.class, false);
				
			}catch(Exception ex){
				lstRecibojde = null;
				LogCajaService.CreateLog("getEnlaceReciboJDE", "ERR", ex.getMessage());
			} 
		 
		 
		 return lstRecibojde;
	 }
	 
/*******************************************************************************************************************************************/
		@SuppressWarnings("unchecked")
		public static List<Recibofac> leerFacturasxRecibo(int iCaid,String sCodComp,int iNumrec, 
				String sTiporec, int iCodcli, int iFecha){			
			
			Session sesion = null;			
			
			List<Recibofac> lstFacturasRecibo = new ArrayList<Recibofac>();

			try{
			 
				sesion = HibernateUtilPruebaCn.currentSession();
				
				lstFacturasRecibo = (ArrayList<Recibofac>)sesion.
						createCriteria(Recibofac.class)
						.add(Restrictions.eq("id.caid", iCaid))
						.add(Restrictions.eq("id.codcomp", sCodComp.trim()))
						.add(Restrictions.eq("id.numrec",iNumrec ))
						.add(Restrictions.eq("id.tiporec", sTiporec))
						.add(Restrictions.eq("id.codcli", iCodcli))
						.add(Restrictions.eq("id.fecha", iFecha))
						.addOrder(Order.asc("id.orden"))
						.list();

			}catch(Exception ex){
				LogCajaService.CreateLog("leerFacturasxRecibo", "ERR", ex.getMessage());
			}
			return lstFacturasRecibo;
		}
/*****OBTIENE EL ESTADO DE UN DETERMINADO BATCH EN EL JDE******************************************************************************************************************/
		
		public static String leerEstadoBatch(Connection cn, int iNoBatch){
			String sEstado = null;
			PreparedStatement ps = null;
			String sql = "SELECT ICIST FROM "+PropertiesSystem.JDEDTA+".F0011 WHERE ICICU = " +iNoBatch;
			try{
				ps = cn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();
				if (rs.next()){
					sEstado = new String(rs.getBytes("ICIST"), "Cp1047");
					
				}
			}catch(Exception ex){
				LogCajaService.CreateLog("leerEstadoBatch", "ERR", ex.getMessage());
			}
			return sEstado;
		}
		
		public static String leerEstadoBatch(int iNoBatch, String sTipoBatch){
			String sEstado = new String();
			
			try {
				
				String sql = "SELECT ICIST " +
					"FROM "+PropertiesSystem.JDEDTA +
					".F0011 WHERE ICICU = " +iNoBatch +" and ICICUT = " +
					"'"+sTipoBatch+"' FETCH FIRST ROWS ONLY ";

				Object ob = ConsolidadoDepositosBcoCtrl.executeSqlQueryUnique(sql, null, true);
				sEstado = String.valueOf( ob );
								
			} catch (Exception e) {
				e.printStackTrace(); 
			} 
			return sEstado;
		}
/*************************************************************************************************************************/
/************************************************************************************************************************/
		public Vrecfac getReciboFac(int iCaid, String sCodSuc, String sCodComp,
									int iNoFactura, String sTipoFactura, String sCodunineg) {			
			Vrecfac recFac = null;
			Session session = HibernateUtilPruebaCn.currentSession();
			Transaction tx = null;
			String sql = "from Vrecfac as r where r.id.caid = " + iCaid
				+ " and r.id.codsuc = '" + sCodSuc + "'"
				+ " and r.id.codcomp = '" + sCodComp + "' and r.id.numfac = "
				+ iNoFactura + " and r.id.tipofactura = '" + sTipoFactura
				+ "' and r.id.estado = '' and trim(r.id.codunineg) ='"+sCodunineg.trim()+"'";

			try{
				tx = session.beginTransaction();
				recFac = (Vrecfac)session.createQuery(sql).uniqueResult();
				tx.commit();
			}catch(Exception ex){
				LogCajaService.CreateLog("getReciboFac", "ERR", ex.getMessage());
			}finally{
				session.close();
			}
			return recFac;
		}

/**********************************************************************************************************/
/**   Guarda los datos de la cabecera del recibo de cualquier tipo de operación con su unidad de negocio **/
	public boolean registrarRecibo(Session session,Transaction tx,int iNumRec, int iNumRecm, String sCodComp,
									double dMontoAplicar, double dMontoRec,double dCambio, String sConcepto,
									Date dFecha,Date dHora,int iCodCli,String sNomCli, String sCajero, int iCaId,
									String sCodSuc,String sCodUser,String sTipoRecibo,int iNodoco, String sTipodoco,
									int iRecjde, Date dtFechaM,String sCodunineg,String sMotivoCT, String monedaapl){
		boolean registrado = false;
		try{ 
			Recibo recibo = new Recibo();
			ReciboId reciboid = new ReciboId();
			
			reciboid.setNumrec(iNumRec);
			reciboid.setCodcomp(sCodComp.trim());
			reciboid.setCaid(iCaId);
			reciboid.setCodsuc(sCodSuc);
			reciboid.setTiporec(sTipoRecibo);
			recibo.setId(reciboid);
								
			recibo.setMontoapl(BigDecimal.valueOf(dMontoAplicar));//Monto Aplicar
			recibo.setMontorec(BigDecimal.valueOf(dMontoRec));	//Monto Recibido
			recibo.setConcepto(sConcepto);						//Concepto
			recibo.setFecha(dFecha);							//Fecha
			recibo.setHora(dHora);								//Hora
			recibo.setCodcli(iCodCli);							//Cod Cliente
			recibo.setCliente(sNomCli);							//Nombre Cliente
			recibo.setCajero(sCajero);							//Cajero					
				
			recibo.setNumrecm(iNumRecm);						//Numero de recibo manual
			recibo.setEstado("");
			recibo.setHoramod(dHora);
			recibo.setCodusera("");
			recibo.setMotivo("");
			recibo.setCoduser(sCodUser);
			recibo.setNodoco(iNodoco);
			recibo.setTipodoco(sTipodoco);				
			recibo.setRecjde(iRecjde);
			recibo.setFecham(dtFechaM);
			recibo.setCodunineg(sCodunineg);
			recibo.setMotivoct(sMotivoCT);
			recibo.setMonedaapl(monedaapl);
			
			session.save(recibo);
			registrado = true;
			
		}catch(Exception ex){
			LogCajaService.CreateLog("registrarRecibo", "ERR", ex.getMessage());
		}
		return registrado;
	}
/**********************REGISTRAR EL RECIBO DE FACTURA DE CONTADO**********************************************************/
		public boolean registrarRecibo(Session session,Transaction tx,int iNumRec, int iNumRecm, String sCodComp,double dMontoAplicar, double 
										dMontoRec,double dCambio, String sConcepto,Date dFecha,Date dHora,int iCodCli,String sNomCli, String sCajero,
										int iCaId, String sCodSuc,String sCodUser,String sTipoRecibo,int iNodoco, String sTipodoco, int iRecjde, Date dtFechaM){
			boolean registrado = false;
			try{ 
				Recibo recibo = new Recibo();
				ReciboId reciboid = new ReciboId();
				
				reciboid.setNumrec(iNumRec);
				reciboid.setCodcomp(sCodComp.trim());
				reciboid.setCaid(iCaId);
				reciboid.setCodsuc(sCodSuc);
				reciboid.setTiporec(sTipoRecibo);
				
				recibo.setId(reciboid);
									
				recibo.setMontoapl(BigDecimal.valueOf(dMontoAplicar));//Monto Aplicar
				recibo.setMontorec(BigDecimal.valueOf(dMontoRec));	//Monto Recibido
				recibo.setConcepto(sConcepto);						//Concepto
				recibo.setFecha(dFecha);							//Fecha
				recibo.setHora(dHora);								//Hora
				recibo.setCodcli(iCodCli);							//Cod Cliente
				recibo.setCliente(sNomCli);							//Nombre Cliente
				recibo.setCajero(sCajero);							//Cajero					
					
				recibo.setNumrecm(iNumRecm);						//Numero de recibo manual
				recibo.setEstado("");
				recibo.setHoramod(dHora);
				recibo.setCodusera("");
				recibo.setMotivo("");
				recibo.setCoduser(sCodUser);
				recibo.setNodoco(iNodoco);
				recibo.setTipodoco(sTipodoco);				
				recibo.setRecjde(iRecjde);
				recibo.setFecham(dtFechaM);
				
				session.save(recibo);		
				//tx.commit();
				registrado = true;
			}catch(Exception ex){
				LogCajaService.CreateLog("registrarRecibo", "ERR", ex.getMessage());
			
			}/*finally {			
				session.close();
			}*/
			return registrado;
		}		
/*********************************************************************************************************************/
/**********************REGISTRAR EL DETALLE DEL RECIBO DE FACTURA DE CONTADO******************************************************/
		public boolean registrarDetalleRecibo(Session session,Transaction tx, int iNumrec, int iNumrecm, String codcomp, List lstMetodosPago,int iCaId,String sCodSuc, String sTpoRec){
			boolean registrado = false;
			Divisas divisas = new Divisas();
			
			MetodosPago mPago = null;
			try{
				for (int i = 0; i < lstMetodosPago.size();i++){
					mPago = (MetodosPago)lstMetodosPago.get(i);
					Recibodet recibodet = new Recibodet();
					RecibodetId recibodetid = new RecibodetId();
					
					recibodetid.setNumrec(iNumrec);						//No. Recibo Automatico				
					recibodetid.setCaid(iCaId);
					recibodetid.setCodsuc(sCodSuc);
					recibodetid.setCodcomp(codcomp);					//Compania	
					recibodetid.setMoneda(mPago.getMoneda());			//Moneda			
					recibodetid.setMpago(mPago.getMetodo());			//Metodo
					recibodetid.setRefer1(mPago.getReferencia());
					recibodetid.setRefer2(mPago.getReferencia2());
					recibodetid.setRefer3(mPago.getReferencia3());
					recibodetid.setRefer4(mPago.getReferencia4());
					if(mPago.getReferencia5()==null){
						recibodet.setRefer5("");
						recibodet.setRefer6("");
						recibodet.setRefer7("");
						recibodet.setNombre("");
					}else{	
						recibodet.setRefer5(mPago.getReferencia5());
						recibodet.setRefer6(mPago.getReferencia6());
						recibodet.setRefer7(mPago.getReferencia7());
						if(mPago.getNombre()!=null){
							recibodet.setNombre(mPago.getNombre());}
						else{
							recibodet.setNombre("");
						}
					}
					
					recibodetid.setTiporec(sTpoRec);
					recibodet.setId(recibodetid);
					
					recibodet.setNumrecm(iNumrecm);					//No. Recibo Manual				
					recibodet.setTasa(BigDecimal.valueOf((divisas.formatStringToDouble(mPago.getTasa().toString()))));
					recibodet.setMonto(BigDecimal.valueOf((mPago.getMonto())));
					recibodet.setEquiv(BigDecimal.valueOf((mPago.getEquivalente())));
					recibodet.setVmanual(  (mPago.getVmanual()==null)?"0":mPago.getVmanual()  );
					recibodet.setCaidpos(  (mPago.getICaidpos()==0)? iCaId: mPago.getICaidpos() );

					recibodet.setMarcatarjeta( ""  );
					recibodet.setCodigomarcatarjeta ( "" );
					recibodet.setLiquidarpormarca( 0 );
									
					session.save(recibodet);
					registrado = true;
				}
				 	
			}catch(Exception ex){
				LogCajaService.CreateLog("registrarDetalleRecibo", "ERR", ex.getMessage());
				
			} 
			return registrado;
		}
/******************************************************************************************************************/
/***************OBTENER ULTIMO NUMERO DE RECIBO DE F55CA014*******************************************************************/
	public static int ultimoRecioCajaCompania(int caid, String codcomp){
		int ultimo = 0;	
		
		try {
			
			String query = "SELECT max(f55.id.c4nncu) FROM F55ca014 as f55 where f55.id.c4id = " + caid + " and trim(f55.id.c4rp01) = '"+codcomp.trim()+"'" ; 
			
			LogCajaService.CreateLog("ultimoRecioCajaCompania", "QRY", query);
			
			List<Long>numeroRecibo =  ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, false ) ;
			
			if(numeroRecibo == null || numeroRecibo.isEmpty() ){
				return ultimo = 0;
			}
			
			return ultimo = numeroRecibo.get(0).intValue();
			
		} catch (Exception e) {
			LogCajaService.CreateLog("ultimoRecioCajaCompania", "ERR", e.getMessage());
			ultimo = 0 ;
		}
		return ultimo ;
	}	
		
		
	public int obtenerUltimoRecibo(Session session,Transaction tx,int iCaid,String sCodComp){		
		int ultimo = 0;	
		Long result;
		Transaction trans = null;
		String sConsulta="";
		try{
			sConsulta = "SELECT max(f55.id.c4nncu) FROM F55ca014 as f55 ";				
			sConsulta += "where f55.id.c4id = " + iCaid + " and trim(f55.id.c4rp01) = '"+sCodComp.trim()+"'";

			if(tx==null){
				session = HibernateUtilPruebaCn.currentSession();
				trans = session.beginTransaction();
			}				
			Object ob = session.createQuery(sConsulta).uniqueResult();
			if(ob!=null){
				result = (Long)ob;
				ultimo = result.intValue();	
			}else{
				ultimo=0;
			}
			if(tx==null){
				trans.commit();
				session.close();
			}
		}catch(Exception ex){
			ultimo = 0;
			System.out.print("Se capturo una excepcion en ReciboContadoCtrl.obtenerUltimoRecibo: " + ex);
		}	
		return ultimo;
	}
	/*	public int obtenerUltimoRecibo(Connection cn,int iCaid,String sCodComp){		
			int ultimo = 0;	
			PreparedStatement ps = null;
			String sql = "SELECT max(f55.c4nncu) c4nncu FROM "+PropertiesSystem.ESQUEMA+".F55ca014 as f55 where f55.c4id = " + iCaid + " and trim(f55.c4rp01) = '"+sCodComp.trim()+"'";
			
			try{
				ps = cn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();	
				if(rs.next()){
					ultimo = rs.getInt("c4nncu");
				}
			}catch(Exception ex){
				ultimo = 0;
				System.out.print("Se capturo una excepcion en ReciboContadoCtrl.obtenerUltimoRecibo: " + ex);
			}try {
				ps.close();
				//cn.close();
			} catch (Exception se2) {
				System.out.println("ERROR: Failed to close connection en ReciboContadoCtrl.obtenerUltimoRecibo: " + se2);
			}
			return ultimo;
		}*/
		public int obtenerUltimoRecibo(int iCaid,String sCodComp){		
			int ultimo = -1;				
			String sql = "SELECT max(f55.c4nncu) c4nncu FROM "+PropertiesSystem.ESQUEMA+".F55ca014 as f55 where f55.c4id = " + iCaid + " and trim(f55.c4rp01) = '"+sCodComp.trim()+"'";
			try{
				Session session = HibernateUtilPruebaCn.currentSession();
				Transaction tx = session.beginTransaction();
				
				Object ob = session.createSQLQuery(sql).uniqueResult();
				
				if(ob!=null){					
					ultimo = Integer.parseInt(ob.toString());	
				}
				tx.commit();
				session.close();
			}catch(Exception ex){
				error = new Exception("@LOGCAJA: No se pudo obtener el numero de recibo: Caja=" + iCaid + " comp: " + sCodComp );
				errorDetalle = ex;
				System.out.print("Se capturo una excepcion en ReciboCtrl.obtenerUltimoRecibo: " + ex);
			}
			return ultimo;
		}
	/******************************************************************************************************************/
		/******************************ACTUALIZA EL NUMERO DE RECIBO EN EL F55CA014************************************************************************************/
		public boolean actualizarNumeroRecibo(Connection cn, int iCajaId, String sCodCom, int iNumRecActual){
			String sql = "update "+PropertiesSystem.ESQUEMA+".F55ca014 set c4nncu = "+iNumRecActual+" WHERE c4id = "+iCajaId+" and c4rp01 = '"+sCodCom+"'";
			PreparedStatement ps = null;
			boolean actualizado = true;
			try {				
				
				ps = cn.prepareStatement(sql);
				int rs = ps.executeUpdate();
				if (rs != 1){
					 actualizado = false;
				}
			} catch (Exception ex) {
				actualizado = false;
				System.out.print("Se capturo una excepcion en el actualizarNumeroRecibo: " + ex);
			}finally {
				try {
					ps.close();
					//cn.close();
				} catch (Exception se2) {
					System.out.println("ERROR: Failed to close connection en ReciboContadoCtrl.actualizarNumeroRecibo: " + se2);
				}
			}
			return actualizado;
		}
		public boolean actualizarNumeroRecibo(int iCajaId, String sCodCom, int iNumRecActual){
			String sql = "update "+PropertiesSystem.ESQUEMA+".F55ca014 set c4nncu = "+iNumRecActual+" WHERE c4id = "+iCajaId+" and c4rp01 = '"+sCodCom+"'";
			PreparedStatement ps = null;
			boolean actualizado = true;
			Connection cn = null;
			try {				
				// obtener conexion del datasource
				cn = As400Connection.getJNDIConnection("DSMCAJA2");
				
				ps = cn.prepareStatement(sql);
				int rs = ps.executeUpdate();
				if (rs != 1){
					 actualizado = false;
				}
			} catch (Exception ex) {
				actualizado = false;
				error = new Exception("@LOGCAJA: No se puedo actualizar el numero de recibo de caja!!! " + iNumRecActual +";" + sCodCom + ";" + iCajaId);
				errorDetalle = ex;
				System.out.print("Se capturo una excepcion en el actualizarNumeroRecibo: " + ex);
			}finally {
				try {
					ps.close();
					cn.close();
				} catch (Exception se2) {
					System.out.println("ERROR: Failed to close connection en ReciboContadoCtrl.actualizarNumeroRecibo: " + se2);
				}
			}
			return actualizado;
		}
	/******************************************************************************************************************/
		/********************LLENAR ENLACE ENTRE RECIBO Y FACTURA*********************************************************************************/
		public boolean fillEnlaceReciboFac(Session session,Transaction tx,int iNumRec, 
						String codcomp, int[] iNumFac,double[] Monto,String[] sTipoDoc, 
						int iCaId,String sCodSuc[],String[] sPartida,String[] sCodunineg,
						String sTiporec, int iCodcli, int iFecha){
			boolean filled = false;
			
			//Session session = HibernateUtil.getSessionFactoryMCAJA().openSession();
			//Transaction tx = null;
			int nofactura = 0;
			try {
				//tx = session.beginTransaction();
				for (int i = 0; i < iNumFac.length; i++){
					if(iNumFac[i]>0){
						Recibofac recibofac = new Recibofac();
						RecibofacId recibofacid = new RecibofacId();
						nofactura = iNumFac[i];
						recibofacid.setNumfac(nofactura);					//Numero factura
						recibofacid.setNumrec(iNumRec);						//Numero Recibo
						recibofacid.setTipofactura(sTipoDoc[i]);			//Tipo de documento
						recibofacid.setCodcomp(codcomp);					//Cod. Compañía
						recibofacid.setPartida(sPartida[i]);				//Partida en blanco para contado
						recibofacid.setCaid(iCaId);
						
						recibofacid.setCodsuc( CodeUtil.pad( sCodSuc[i].trim(), 5, "0") );
//						recibofacid.setCodsuc("000"+sCodSuc[i]);
						
						recibofacid.setCodunineg(sCodunineg[i].trim());
						recibofacid.setTiporec(sTiporec);
						recibofacid.setCodcli(iCodcli);
						recibofacid.setFecha(iFecha);
						
						recibofac.setId(recibofacid);						//Recibo
						recibofac.setMonto(BigDecimal.valueOf(Monto[i]));   //Monto
						recibofac.setEstado("");
						session.save(recibofac);							//Factura
					}
				}
				//tx.commit();
				filled = true;	
			}catch(Exception ex){
				error = new Exception("@LOGCAJA: error de sistema al llenar el enlace entre Recibo y factura!!! No. Factura = " + nofactura);
				errorDetalle = ex;
				System.out.print("Exception -> ReciboContadoCtrl.fillEnlaceReciboFac: " + ex);
			}/*finally{
				session.close();
			}*/
			return filled;
		}
	/*********************GRABAR CAMBIO EN CAMBIODET********************************************************************************/
		public boolean registrarCambio(Session session,Transaction tx,int iNumRec,String sCodComp, String sMoneda, double dCambio,int iCaId,String sCodSuc,BigDecimal bdTasa,String sTiporec){
			boolean registrado = false;
			//Session session = HibernateUtil.getSessionFactoryMCAJA().openSession();
			//Transaction tx = null;
			try{
				//tx = session.beginTransaction();
				Cambiodet cambiodet = new Cambiodet();
				CambiodetId cambiodetId = new CambiodetId();
				
				cambiodetId.setCodcomp(sCodComp);
				cambiodetId.setNumrec(iNumRec);
				cambiodetId.setMoneda(sMoneda);
				cambiodetId.setCaid(iCaId);
				cambiodetId.setCodsuc(sCodSuc);
				cambiodetId.setTiporec(sTiporec);
				
				cambiodet.setCambio(BigDecimal.valueOf(dCambio));
				cambiodet.setTasa(bdTasa);
				cambiodet.setId(cambiodetId);
				
				session.save(cambiodet);
				//tx.commit();
				registrado = true;
			}catch(Exception ex){
				registrado = false;
				System.out.println("Se capturo una excepcion en ReciboContadoCtrl.registrarCambio: " + ex);
			}
			return registrado;
		}
	/**********************************************************************************************************/
		/*****************OBTENER CUENTA DE METODO DE PAGO, MONEDA, COMPANIA Y CAJA********************************/
		public F55ca011 obtenerCuenta(Session session,Transaction tx,int iCajaId,String sCodComp,String sCodMoneda,String sMetodo){
			F55ca011 f55ca011 = null;
			//Session session = HibernateUtil.getSessionFactoryMCAJA().openSession();
			//Transaction tx = null;	
			try{
				//tx = session.beginTransaction();	
				f55ca011 = (F55ca011)session
					.createQuery("from F55ca011 f where f.id.c1id = :pCajaId and f.id.c1rp01 = :pCodComp and f.id.c1crcd = :pCodMon and f.id.c1ryin = :pMetodo")
					.setParameter("pCajaId", iCajaId)
					.setParameter("pCodComp", sCodComp)
					.setParameter("pCodMon", sCodMoneda)
					.setParameter("pMetodo", sMetodo)			
					.uniqueResult();
				//tx.commit();
			}catch(Exception ex){
				System.out.print("=>Excepcion capturada en obtenerBatchActual: " + ex);
			}/*finally{
	        	session.close();        
			}*/
			return f55ca011;
		}
	/****************************************************************************************************/
	/************************OBTENER ID DE CUENTA DEL F0901********************************************/
		public String obtenerIdCuenta(Session session,Transaction tx,String sMCU, String sOBJ, String sSUB){
			String idCuenta = null;			
			boolean bNuevaSesionCaja = false;
			String sqlCtaCaja = "";
			
			try{
				
				sqlCtaCaja = " select f.id.gmaid from Vf0901 f where trim(f.id.gmmcu) = '"
						+ sMCU.trim() + "' and trim(f.id.gmobj) = '" + sOBJ.trim()
						+ "' and trim(f.id.gmsub) = '" + sSUB.trim() + "'";
				
				LogCajaService.CreateLog("obtenerIdCuenta", "QRY", sqlCtaCaja);				
				
			
				idCuenta = (String) session.createQuery(sqlCtaCaja ).uniqueResult();
				
			}catch(Exception ex){
				LogCajaService.CreateLog("obtenerIdCuenta", "ERR", ex.getMessage());				 
				idCuenta = null;
			}
			return idCuenta;
		}
	/****************************************************************************************************/
	/*******************OBTENER CUENTA DE VENTA DE CONTADO DE CAJA******************************************/
		public F55ca018 obtenerCuentaVentaContado(int iCodCaja, String sCodComp, String sCodUnineg){
			F55ca018 f55ca018 = null;
			Session session = HibernateUtilPruebaCn.currentSession();
			Transaction tx = null;
			String sql = "from F55ca018 as f where f.id.c8id = "+iCodCaja+" and trim(f.id.c8rp01) = '"+sCodComp.trim()+"' and trim(f.id.c8mcu) = '"+sCodUnineg.trim()+"' and f.id.c8stat = 'A'";
			try{
				tx = session.beginTransaction();
				f55ca018 = (F55ca018)session.createQuery(sql).uniqueResult();
				tx.commit();
			}catch(Exception ex){
				System.out.println("Se capturo una excepcion en ReciboContadoCtrl.obtenerCuentaVentaContado: " + ex);
			}finally{
				session.close();
			}
			return f55ca018;
		}
	/*****************************************************************************************************/
	/************************OBTENER NUMERO DE BATCH ACTUAL**************************************************/
		public int obtenerNoBatchActual(Session session,Transaction tx ,Connection cn){
			int iLastBatch = 0;		
			try{
				iLastBatch = Integer.parseInt((session
					.createQuery("select f.id.nnn001 from Vf0002 f where trim(f.id.nnsy) = '00'")
					.uniqueResult()).toString());
			}catch(Exception ex){
				System.out.print("=>Excepcion capturada en ReciboContadoCtrl.obtenerBatchActual: " + ex);
			}
			return iLastBatch;
		}
		
	/****************************************************************************************************/
	/************************ACTUALIZAR NUMERO DE BATCH*********************************************/
		public boolean actualizarNoBatch(Connection cn,int iNumeroBatch){
			boolean bActualizado = false;
			PreparedStatement ps = null;
			String sql = "UPDATE "+PropertiesSystem.JDECOM+".F0002 SET NNN001 = "+ (iNumeroBatch + 1)+" WHERE TRIM(NNSY) = '00'";
			try{
				ps = cn.prepareStatement(sql);
				int rs = ps.executeUpdate();
				if (rs != 1){
					bActualizado = false;
				}
				bActualizado = true;			
			}catch(Exception ex){
				bActualizado = false;
				System.out.print("=>Excepcion capturada en ReciboContadoCtrl.actualizarBatch: " + ex);
			}finally {
				try {
					ps.close();
					//cn.close();
				} catch (Exception se2) {
					System.out.println("ERROR: Failed to close connection en ReciboContadoCtrl.actualizarBatch: " + se2);
				}
			}
			return bActualizado;
		}
		
		/****************************************************************/
		/****** Leer el número de documento
		 */
		public int leerNumeroDocumento(){
			int iNumDoc = 0;
			Session session = HibernateUtilPruebaCn.currentSession();
			Transaction tx = null;
			try{
				tx = session.beginTransaction();
				iNumDoc = Integer.parseInt((session
						.createQuery("select f.id.nnn002 from Vf0002 f where trim(f.id.nnsy) = '09'")
						.uniqueResult()).toString());
				tx.commit();
			}catch(Exception ex){
				iNumDoc = 0;
				System.out.println("Se capturo una Excepcion en ReciboCtrl.leerNumeroDocumento: " + ex);
			}finally{
				session.close();
			}
			return iNumDoc;
		}
		
		
		
	/****************************************************************************************************/
	/************************LEER NUMERO DE DOCUMENTO A USAR*********************************************/	
		public int leerNumeroDocumento(Session session,Transaction tx){
			int iNumDoc = 0;
			//Session session = HibernateUtil.getSessionFactoryENS().openSession();
			//Transaction tx = null;
			try{
				//iNumDoc=tx = session.beginTransaction();
				iNumDoc = Integer.parseInt((session
						.createQuery("select f.id.nnn002 from Vf0002 f where trim(f.id.nnsy) = '09'")
						.uniqueResult()).toString());
					//tx.commit();
			}catch(Exception ex){
				iNumDoc = 0;
				System.out.println("Se capturo una Excepcion en ReciboCtrl.leerNumeroDocumento: " + ex);
			}
			return iNumDoc;
		}
	/**********************************************************************************************************/
	/************************LEER NUMERO DE RECIBO DEL JDE A USAR*********************************************/	
		public int leerNumeroReciboJDE(Session session,Transaction tx){
			int iNumDoc = 0;
			//Session session = HibernateUtil.getSessionFactoryENS().openSession();
			//Transaction tx = null;
			try{
				//iNumDoc=tx = session.beginTransaction();
				iNumDoc = Integer.parseInt((session
						.createQuery("select f.id.nnn005 from Vf0002 f where trim(f.id.nnsy) = '03'")
						.uniqueResult()).toString());
					//tx.commit();
			}catch(Exception ex){
				iNumDoc = 0;
				System.out.println("Se capturo una Excepcion en ReciboCtrl.leerNumeroReciboJDE: " + ex);
			}
			return iNumDoc;
		}
		
		
		
		
		public boolean registrarAsientoDiarioLogs(Session session, 
				String logsMsg,	Date dtFechaAsiento,  String sCodSuc,
				String sTipodoc, int iNoDocumento, double dLineaJDE, int iNoBatch,
				String sCuenta, String sIdCuenta, String sCodUnineg,
				String sCuentaObj, String sCuentaSub, String sTipoAsiento,
				String sMoneda, long iMonto, String sConcepto, String sUsuario,
				String sCodApp, BigDecimal dTasa, String sTipoCliente,
				String sObservacion, String sCodSucCuenta, String sGlsbl,
				String sGlsblt, String sGlbcrc, String sGlhco, String sGlcrrm, long iMontoFor) {

			boolean bRegistrado = true;
			SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			
			try {
				
				if(iNoBatch == 0 ||iNoDocumento == 0 ){
					error = new Exception( "@Error de aplicacion al  registrarAsientoDiario el numero de batch a utilizar es invalido (#0) " );
					errorDetalle = error;
					
//					LogCrtl.sendLogInfo(" NUMERO BATCH EN CERO : registrarAsientoDiario: Linea: " + dLineaJDE + " ||| " + logsMsg + " ||| <-------- [" + sdf.format(new Date()) + " : " 	+ dfHora.format(new Date()) + "] ----- >");
					return 	bRegistrado = false;
				}
				
				
				String fechaBatch = String.valueOf( FechasUtil.dateToJulian(dtFechaAsiento) );
				String horaBatch = new SimpleDateFormat("HHmmss").format( new Date() );
				String sNombrePc = PropertiesSystem.ESQUEMA;
				
				String glctry = new SimpleDateFormat("yyyy").format(dtFechaAsiento).substring(0,2);
				String glfy   = new SimpleDateFormat("yyyy").format(dtFechaAsiento).substring(2,4);
				String glpn   = new SimpleDateFormat("MM").format(dtFechaAsiento);
				
				sCodUnineg = com.casapellas.util.CodeUtil.pad(sCodUnineg.trim(), 12," ");
				
				 
				if (sConcepto.length() > 30) {
					sConcepto = sConcepto.substring(0, 29);
				}
				if (sTipoCliente.equals("E")) {
					sTipoCliente = "EMP";
				} else {
					sTipoCliente = "";
				}
				if (sNombrePc.length() > 9) {
					sNombrePc = sNombrePc.substring(0, 9);
				}
				if (sObservacion.length() > 30) {
					sObservacion = sObservacion.substring(0, 29);
				}

				sCodSuc = CodeUtil.pad( sCodSuc.trim(), 5, "0"); 
				sCodSucCuenta = CodeUtil.pad( sCodSucCuenta.trim(), 5, "0"); 
				sGlhco = CodeUtil.pad( sGlhco.trim(), 5, "0");
				
				
				String glacr =  "0" ;
				if( sTipoAsiento.compareTo("AA") == 0 && dTasa.compareTo(BigDecimal.ZERO) == 1  ){
					//glacr = String.valueOf( new BigDecimal( String.valueOf(iMonto) ).divide(dTasa, 2, RoundingMode.HALF_UP).intValue() );
					glacr = String.valueOf( iMontoFor ) ;
				}
				
				
				String sqlInsert = "insert into @JDEDTA.F0911 ( @FIELDS_TO_INSERT ) VALUES (@VALUES_TO_INSERT) " ;
				
				String insertFields = DefaultJdeFieldsValues.F0911_CONTADO_INSERT_COLUMNS;
				String insertValues = DefaultJdeFieldsValues.F0911_CONTADO_INSERT_COLUMNS_VALUES;
				
				insertFields += ", " +  DefaultJdeFieldsValues.F0911_CONTADO_COLUMN_NAMES_DEFAULT;
				insertValues += ", " +  DefaultJdeFieldsValues.F0911_CONTADO_COLUMN_NAMES_DEFAULT_VALUES;
				
				sCuenta = sCodUnineg+"."+sCuentaObj + (  (sCuentaSub.trim().isEmpty() )? "": "."+ sCuentaSub  ) ;
				
				insertValues = insertValues
				 .replace("@GLKCO@", sCodSuc)
				 .replace("@GLDCT@", sTipodoc)
				 .replace("@GLDOC@", String.valueOf( iNoDocumento) )
				 .replace("@GLDGJ@", fechaBatch)
				 .replace("@GLJELN@", String.valueOf( dLineaJDE ) )
				 .replace("@GLICU@", String.valueOf( iNoBatch ) ) 
				 .replace("@GLICUT@", valoresJDEInsContado[8] )
				 .replace("@GLDICJ@", fechaBatch )
				 .replace("@GLDSYJ@", fechaBatch)
				 .replace("@GLTICU@", horaBatch)
				 .replace("@GLCO@", sCodSucCuenta)
				 .replace("@GLANI@", sCuenta)
				 .replace("@GLAM@", "2")
				 .replace("@GLAID@", sIdCuenta)
				 .replace("@GLMCU@", sCodUnineg)
				 .replace("@GLOBJ@", sCuentaObj)
				 .replace("@GLSUB@", sCuentaSub)
				 .replace("@GLSBL@", sGlsbl)
				 .replace("@GLSBLT@",sGlsblt)
				 .replace("@GLLT@", sTipoAsiento)
				 .replace("@GLPN@", glpn)
				 .replace("@GLCTRY@", glctry )
				 .replace("@GLFY@", glfy )
				 .replace("@GLCRCD@", sMoneda)
				 .replace("@GLAA@", String.valueOf( iMonto ) )
				 .replace("@GLEXA@",sConcepto )
				 .replace("@GLDKJ@", fechaBatch)
				 .replace("@GLDSVJ@", fechaBatch)
				 .replace("@GLTORG@", sUsuario )
				 .replace("@GLUSER@", sUsuario )
				 .replace("@GLPID@",  sCodApp)
				 .replace("@GLJOBN@", sNombrePc )
				 .replace("@GLUPMJ@", fechaBatch )
				 .replace("@GLUPMT@", horaBatch)
				 .replace("@GLCRR@", String.valueOf( dTasa ) )
				 .replace("@GLGLC@", sTipoCliente)
				 .replace("@GLEXR@", sObservacion)
				 .replace("@GLBCRC@", sGlbcrc )
				 .replace("@GLHCO@",  sGlhco)
				 .replace("@GLCRRM@", sGlcrrm)
				 .replace("@GLACR@",  glacr)
				  .replace("@GLAN8@",  "0" )
				 .replace("@GLVINV@", "")
				 .replace("@GLIVD@",  "0" )
				 .replace("@GLPKCO@", "")
				 ;
				 
				sqlInsert = sqlInsert
					.replace("@JDEDTA", PropertiesSystem.JDEDTA)
					.replace("@FIELDS_TO_INSERT", insertFields)
					.replace("@VALUES_TO_INSERT", insertValues);
				
				try {
					bRegistrado = ConsolidadoDepositosBcoCtrl .executeSqlQueryTx( session, sqlInsert );
				} catch (Exception e) {
					e.printStackTrace();
					bRegistrado = false;
				}
				
//				LogCrtl.sendLogInfo("(F) registrarAsientoDiario: Linea: " + dLineaJDE + " ||| " + logsMsg + " ||| <-------- [" + sdf.format(new Date()) + " : " 	+ dfHora.format(new Date()) + "] ----- >");

			} catch (Exception ex) {
				
				ex.printStackTrace();
 
				bRegistrado = false;
				errorDetalle = ex;
				error = new Exception( "@Error de aplicacion al guardar el asiento de diario@@ "+ error);
				
				if (ex.toString().trim().startsWith( "com.ibm.websphere.ce.cm.DuplicateKeyException:"))
					error = new Exception("@Ya existe en JDE un documento con el numero de referencia: "+ iNoDocumento);
			
			}  
			
			return bRegistrado;
		}
		
		public boolean registrarAsientoDiarioLogsSession( Session session, 
				String logsMsg,	Date dtFechaAsiento,  String sCodSuc,
				String sTipodoc, int iNoDocumento, double dLineaJDE, int iNoBatch,
				String sCuenta, String sIdCuenta, String sCodUnineg,
				String sCuentaObj, String sCuentaSub, String sTipoAsiento,
				String sMoneda, long iMonto, String sConcepto, String sUsuario,
				String sCodApp, BigDecimal dTasa, String sTipoCliente,
				String sObservacion, String sCodSucCuenta, String sGlsbl,
				String sGlsblt, String sGlbcrc, String sGlhco, String sGlcrrm, long iMontoFor) {

			boolean bRegistrado = true;
			SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			
			try {
				
				if(iNoBatch == 0  ){
					error = new Exception( "@Error de aplicacion al  registrarAsientoDiario el numero de batch a utilizar es invalido (#0) " );
					errorDetalle = error;
					
//					LogCrtl.sendLogInfo(" NUMERO BATCH EN CERO : registrarAsientoDiario: Linea: " + dLineaJDE + " ||| " + logsMsg + " ||| <-------- [" + sdf.format(new Date()) + " : " 	+ dfHora.format(new Date()) + "] ----- >");
					return 	bRegistrado = false;
				}
				if( iNoDocumento == 0 ){
					error = new Exception( "@Error de aplicacion al  registrarAsientoDiario el numero de documento a utilizar es invalido (#0) " );
					errorDetalle = error;
					
//					LogCrtl.sendLogInfo(" NUMERO DOCUMENTO EN CERO : registrarAsientoDiario: Linea: " + dLineaJDE + " ||| " + logsMsg + " ||| <-------- [" + sdf.format(new Date()) + " : " 	+ dfHora.format(new Date()) + "] ----- >");
					return 	bRegistrado = false;
				}
				
				String fechaBatch = String.valueOf( FechasUtil.dateToJulian(dtFechaAsiento) );
				String horaBatch = new SimpleDateFormat("HHmmss").format( new Date() );
				String sNombrePc = PropertiesSystem.ESQUEMA;
				
				String glctry = new SimpleDateFormat("yyyy").format(dtFechaAsiento).substring(0,2);
				String glfy   = new SimpleDateFormat("yyyy").format(dtFechaAsiento).substring(2,4);
				String glpn   = new SimpleDateFormat("MM").format(dtFechaAsiento);
				
				sCodUnineg = com.casapellas.util.CodeUtil.pad(sCodUnineg.trim(), 12," ");
				
				 
				if (sConcepto.length() > 30) {
					sConcepto = sConcepto.substring(0, 29);
				}
				if (sTipoCliente.equals("E")) {
					sTipoCliente = "EMP";
				} else {
					sTipoCliente = "";
				}
				if (sNombrePc.length() > 9) {
					sNombrePc = sNombrePc.substring(0, 9);
				}
				if (sObservacion.length() > 30) {
					sObservacion = sObservacion.substring(0, 29);
				}

				sCodSuc = CodeUtil.pad( sCodSuc.trim(), 5, "0"); 
				sCodSucCuenta = CodeUtil.pad( sCodSuc.trim(), 5, "0"); 
				sGlhco = CodeUtil.pad( sGlhco.trim(), 5, "0");
				
				
				String glacr =  "0" ;
				if( sTipoAsiento.compareTo("AA") == 0 && dTasa.compareTo(BigDecimal.ZERO) == 1  ){
					glacr = String.valueOf( iMontoFor ) ;
				}
				
				String sqlInsert = "insert into @JDEDTA.F0911 ( @FIELDS_TO_INSERT ) VALUES (@VALUES_TO_INSERT) " ;
				
				String insertFields = DefaultJdeFieldsValues.F0911_CONTADO_INSERT_COLUMNS;
				String insertValues = DefaultJdeFieldsValues.F0911_CONTADO_INSERT_COLUMNS_VALUES;
				
				insertFields += ", " +  DefaultJdeFieldsValues.F0911_CONTADO_COLUMN_NAMES_DEFAULT;
				insertValues += ", " +  DefaultJdeFieldsValues.F0911_CONTADO_COLUMN_NAMES_DEFAULT_VALUES;
				
				sCuenta = sCodUnineg+"."+sCuentaObj + (  (sCuentaSub.trim().isEmpty() )? "": "."+ sCuentaSub  ) ;
				
				insertValues = insertValues
				 .replace("@GLKCO@", sCodSuc)
				 .replace("@GLDCT@", sTipodoc)
				 .replace("@GLDOC@", String.valueOf( iNoDocumento) )
				 .replace("@GLDGJ@", fechaBatch)
				 .replace("@GLJELN@", String.valueOf( dLineaJDE ) )
				 .replace("@GLICU@", String.valueOf( iNoBatch ) ) 
				 .replace("@GLICUT@", valoresJDEInsContado[8] )
				 .replace("@GLDICJ@", fechaBatch )
				 .replace("@GLDSYJ@", fechaBatch)
				 .replace("@GLTICU@", horaBatch)
				 .replace("@GLCO@", sCodSucCuenta)
				 .replace("@GLANI@", sCuenta)
				 .replace("@GLAM@", "2")
				 .replace("@GLAID@", sIdCuenta)
				 .replace("@GLMCU@", sCodUnineg)
				 .replace("@GLOBJ@", sCuentaObj)
				 .replace("@GLSUB@", sCuentaSub)
				 .replace("@GLSBL@", sGlsbl)
				 .replace("@GLSBLT@",sGlsblt)
				 .replace("@GLLT@", sTipoAsiento)
				 .replace("@GLPN@", glpn)
				 .replace("@GLCTRY@", glctry )
				 .replace("@GLFY@", glfy )
				 .replace("@GLCRCD@", sMoneda)
				 .replace("@GLAA@", String.valueOf( iMonto ) )
				 .replace("@GLEXA@",sConcepto )
				 .replace("@GLDKJ@", fechaBatch)
				 .replace("@GLDSVJ@", fechaBatch)
				 .replace("@GLTORG@", sUsuario )
				 .replace("@GLUSER@", sUsuario )
				 .replace("@GLPID@",  sCodApp)
				 .replace("@GLJOBN@", sNombrePc )
				 .replace("@GLUPMJ@", fechaBatch )
				 .replace("@GLUPMT@", horaBatch)
				 .replace("@GLCRR@", String.valueOf( dTasa ) )
				 .replace("@GLGLC@", sTipoCliente)
				 .replace("@GLEXR@", sObservacion)
				 .replace("@GLBCRC@", sGlbcrc )
				 .replace("@GLHCO@",  sGlhco)
				 .replace("@GLCRRM@", sGlcrrm)
				 .replace("@GLACR@",  glacr)
				  .replace("@GLAN8@",  "0" )
				 .replace("@GLVINV@", "")
				 .replace("@GLIVD@",  "0" )
				 .replace("@GLPKCO@", "")
				 ;
				 
				sqlInsert = sqlInsert
					.replace("@JDEDTA", PropertiesSystem.JDEDTA)
					.replace("@FIELDS_TO_INSERT", insertFields)
					.replace("@VALUES_TO_INSERT", insertValues);
				
				try {
					
					int rows = session.createSQLQuery(sqlInsert).executeUpdate();
					bRegistrado = rows == 1 ;
					
				} catch (Exception e) {
					e.printStackTrace();
					bRegistrado = false;
				}
				
//				LogCrtl.sendLogInfo("(F) registrarAsientoDiario: Linea: " + dLineaJDE + " ||| " + logsMsg + " ||| <-------- [" + sdf.format(new Date()) + " : " 	+ dfHora.format(new Date()) + "] ----- >");

			} catch (Exception ex) {
				
				ex.printStackTrace(); 
				bRegistrado = false;
				errorDetalle = ex;
				error = new Exception( "@Error de aplicacion al guardar el asiento de diario@@ "+ error);
				
				if (ex.toString().trim().startsWith( "com.ibm.websphere.ce.cm.DuplicateKeyException:"))
					error = new Exception("@Ya existe en JDE un documento con el numero de referencia: "+ iNoDocumento);
			
			}  
			
			return bRegistrado;
		}
		
		
		
		
/****************************************************************************************/
/** Método: Guardar Registro en el GCPPRRDTA.F0011 para asientos de diario.
 * 		  : Versión para JD Edward's 9.2
 *	Fecha:  15/08/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 **/

	public boolean registrarAsientoDiarioWithSession(Date dtFechaAsiento,Session s,String sCodSuc,String sTipodoc,int iNoDocumento,
			  double dLineaJDE, int iNoBatch, String sCuenta, String sIdCuenta, String sCodUnineg,
			  String sCuentaObj,String sCuentaSub,String sTipoAsiento,String sMoneda, long iMonto, 
			  String sConcepto,String sUsuario,String sCodApp, BigDecimal dTasa, String sTipoCliente, 
			  String sObservacion,String sCodSucCuenta,String sGlsbl,String sGlsblt,
			  String sGlbcrc,String sGlhco, String sGlcrrm){
		boolean bRegistrado = true;
		String sNombrePc = null;
		String sql = "";		
		Date dHora = new Date();
		SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try{
		Calendar cFecha = Calendar.getInstance();
		cFecha.setTime(dtFechaAsiento);
		CalendarToJulian fecha = new CalendarToJulian(cFecha);
		int iFecha = fecha.getDate();
		
		sNombrePc = "SERVER";//InetAddress.getLocalHost().getHostName();
		//obtener hora en enteros
		String[] sHora = (dfHora.format(dHora)).split(":");
		int iHora = Integer.parseInt(sHora[0] + sHora[1] + sHora[2]);
		//obtener partes de la fecha
		String[] sFecha = (sdf.format(dtFechaAsiento)).split("/");
		
		
		sCodUnineg = com.casapellas.util.CodeUtil.pad(sCodUnineg.trim(), 12," ");
		
		
		
		if(sCuenta == null || sCuenta.trim().isEmpty() ) {
		sCuenta = sCodUnineg + "." + sCuentaObj + ( sCuentaSub.trim().isEmpty() ? "" : "."  + sCuentaSub ) ;
		}
		
		if(sConcepto.length() > 30){
		sConcepto = sConcepto.substring(0,29);
		}
		if(sTipoCliente.compareTo( "E" ) == 0 ){
		sTipoCliente = "EMP";
		}else{
		sTipoCliente = "";
		}
		if(sNombrePc.length() > 9){
		sNombrePc =	sNombrePc.substring(0, 9);
		}
		if(sObservacion.length() > 30){
		sObservacion = sObservacion.substring(0,29);
		}
		
		String strNumDocJde = Integer.toString( iNoDocumento ); 
		if( strNumDocJde.length() > 8){
		strNumDocJde = strNumDocJde.substring(strNumDocJde.length() - 8, strNumDocJde.length() );
		iNoDocumento = Integer.parseInt(strNumDocJde);
		}
		
		sql =  " INSERT INTO "+PropertiesSystem.JDEDTA+".F0911 (GLKCO,GLDCT,GLDOC,GLDGJ,GLJELN,GLICU,GLICUT,GLDICJ,";
		sql += " GLDSYJ,GLTICU,GLCO,GLANI,GLAM,GLAID,GLMCU,GLOBJ,GLSUB, GLSBL,GLSBLT, GLLT,GLPN,";
		sql += " GLCTRY,GLFY,GLCRCD,GLAA,GLEXA,GLDKJ,GLDSVJ,GLTORG,GLUSER,GLPID,GLJOBN,GLUPMJ,GLUPMT,";
		sql += " GLCRR,GLGLC,GLEXR,GLBCRC,GLCRRM,GLEXTL) VALUES(" ;
		
		
		sCodSuc = CodeUtil.pad( sCodSuc, 5, "0"); 
		sCodSucCuenta = CodeUtil.pad( sCodSucCuenta, 5, "0"); 
		sGlhco = CodeUtil.pad( sGlhco, 5, "0"); 
		
		sql+=
		"'"+sCodSuc+"'," +			
		"'"+sTipodoc +"'," +
		""+iNoDocumento+"," +
		""+iFecha+"," +
		""+dLineaJDE+"," +
		""+iNoBatch+"," +
		"'G'," +
		""+iFecha+"," +
		""+iFecha+"," +
		""+iHora+"," +
		
		"'"+sCodSucCuenta+"'," +
		
		"'"+sCuenta+"'," +
		"'2'," +
		"'"+sIdCuenta+"'," +
		"'"+sCodUnineg+"'," +
		"'"+sCuentaObj+"'," +
		"'"+sCuentaSub+"'," +
		"'"+sGlsbl+"',"+
		"'"+sGlsblt+"',"+
		"'"+sTipoAsiento+"'," +
		""+Integer.parseInt(sFecha[1])+"," +
		"20," +
		""+Integer.parseInt(sFecha[2].substring(2, 4))+"," +
		"'"+sMoneda+"'," +
		""+iMonto+"," +
		"'"+sConcepto+"'," +
		""+iFecha+"," +
		""+iFecha+"," +
		"'"+sUsuario+"'," +
		"'"+sUsuario+"'," +
		"'"+sCodApp+"'," +
		"'"+sNombrePc+"'," +
		""+iFecha+"," +
		""+iHora+"," +
		""+dTasa.setScale(2, RoundingMode.HALF_UP).toString() +"," +
		"'"+sTipoCliente+"'," +
		"'"+sObservacion+"'," +
		"'"+sGlbcrc+"'," +				
		"'"+sGlcrrm+"'," +
		"''"+
		")";
	
		ConsolidadoDepositosBcoCtrl.executeSqlQueryTx(s, sql);
		
		}catch(Exception ex){
		
		bRegistrado = false;
		errorDetalle = ex;
		error = new Exception("@No pudo grabarse Batch en JDE, favor intente de nuevo");
		
		if( ex.toString().trim().toLowerCase().contains("duplicatekeyexception") ||
		ex.toString().trim().toLowerCase().contains("sql0803") || 
		ex.toString().trim().toLowerCase().contains("SQLIntegrityConstraintViolationException")){
		error = new Exception("@Ya existe en JDE un Batch con el numero de referencia: "
		+iNoDocumento +" Asigne un nuevo número e intente nuevamente ");
		}
		
		String invocado =  new Exception().getStackTrace()[1].getClassName() +":"+ new Exception().getStackTrace()[1].getMethodName() ;
		ex.printStackTrace();
		
		}
		return bRegistrado;
}
/**********************************************************************************************************/
/*************** REGISTRAR ASIENTO DE DIARIO EN EL "+PropertiesSystem.JDEDTA+".F0911 (método para salidas CH) ***************/
	public boolean registrarAsientoDiario11(Date dtFechaAsiento,Connection cn,String sCodSuc,String sTipodoc,int iNoDocumento,
										  double dLineaJDE, int iNoBatch, String sCuenta, String sIdCuenta, String sCodUnineg,
										  String sCuentaObj,String sCuentaSub,String sTipoAsiento,String sMoneda, int iMonto, 
										  String sConcepto,String sUsuario,String sCodApp, BigDecimal dTasa, String sTipoCliente, 
										  String sObservacion,String sCodSucCuenta,String sGlsbl,String sGlsblt ){
		boolean bRegistrado = true;
		String sNombrePc = null;
		String sql = "";
		PreparedStatement ps = null;
		Date dHora = new Date();
		SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try{
			Calendar cFecha = Calendar.getInstance();
			cFecha.setTime(dtFechaAsiento);
			CalendarToJulian fecha = new CalendarToJulian(cFecha);
			int iFecha = fecha.getDate();
			
			sNombrePc = "SERVER";//InetAddress.getLocalHost().getHostName();
			//obtener hora en enteros
			String[] sHora = (dfHora.format(dHora)).split(":");
			int iHora = Integer.parseInt(sHora[0] + sHora[1] + sHora[2]);
			//obtener partes de la fecha
			String[] sFecha = (sdf.format(dtFechaAsiento)).split("/");
			//agregar blancos a unineg
			if(sCodUnineg.length() == 2){
				sCodUnineg = "          "+sCodUnineg;
				sCuenta = "          "+sCuenta;
			}else{
				sCodUnineg = "        "+sCodUnineg;
				sCuenta = "        "+sCuenta;
			}
			if(sConcepto.length() > 30){
				sConcepto = sConcepto.substring(0,29);
			}
			if(sTipoCliente.equals("E")){
				sTipoCliente = "EMP";
			}else{
				sTipoCliente = "";
			}
			if(sNombrePc.length() > 9){
				sNombrePc =	sNombrePc.substring(0, 9);
			}
			if(sObservacion.length() > 30){
				sObservacion = sObservacion.substring(0,29);
			}
			
			sql = "INSERT INTO "+PropertiesSystem.JDEDTA+".F0911 (GLKCO,GLDCT,GLDOC,GLDGJ,GLJELN,GLICU,GLICUT,GLDICJ,GLDSYJ,GLTICU,GLCO,GLANI,GLAM,GLAID,GLMCU,GLOBJ,GLSUB, GLSBL,GLSBLT, GLLT,GLPN,GLCTRY,GLFY,GLCRCD,GLAA,GLEXA,GLDKJ,GLDSVJ,GLTORG,GLUSER,GLPID,GLJOBN,GLUPMJ,GLUPMT,GLCRR,GLGLC,GLEXR) VALUES(" +
			"'000"+sCodSuc+"'," +
			"'"+sTipodoc +"'," +
			""+iNoDocumento+"," +
			""+iFecha+"," +
			""+dLineaJDE+"," +
			""+iNoBatch+"," +
			"'G'," +
			""+iFecha+"," +
			""+iFecha+"," +
			""+iHora+"," +
			"'000"+sCodSucCuenta+"'," +
			"'"+sCuenta+"'," +
			"'2'," +
			"'"+sIdCuenta+"'," +
			"'"+sCodUnineg+"'," +
			"'"+sCuentaObj+"'," +
			"'"+sCuentaSub+"'," +
			"'"+sGlsbl+"',"+
			"'"+sGlsblt+"',"+
			"'"+sTipoAsiento+"'," +
			""+Integer.parseInt(sFecha[1])+"," +
			"20," +
			""+Integer.parseInt(sFecha[2].substring(2, 4))+"," +
			"'"+sMoneda+"'," +
			""+iMonto+"," +
			"'"+sConcepto+"'," +
			""+iFecha+"," +
			""+iFecha+"," +
			"'"+sUsuario+"'," +
			"'"+sUsuario+"'," +
			"'"+sCodApp+"'," +
			"'"+sNombrePc+"'," +
			""+iFecha+"," +
			""+iHora+"," +
			""+dTasa+"," +
			"'"+sTipoCliente+"'," +
			"'"+sObservacion+"'" +
			")";
			ps = cn.prepareStatement(sql);			
			int rs = ps.executeUpdate();
			if (rs != 1){
				bRegistrado = false;
			}
		}catch(Exception ex){
			bRegistrado = false;
			System.out.println("Se capturo una excepcion en ReciboCtrl.registrarAsientoDiario: " + ex);
		}finally {
			try {
				ps.close();
				//cn.close();
			} catch (Exception se2) {
				System.out.println("ERROR: Failed to close connection en ReciboCtrl.registrarAsientoDiario: " + se2);
			}
		}
		return bRegistrado;
	}
	/**********************************************************************************************************/
	/****************REGISTRAR ASIENTO DE DIARIO EN EL "+PropertiesSystem.JDEDTA+".F0911   (método básico JC)  ******************/
		public boolean registrarAsientoDiario222(Connection cn,String sCodSuc,int iNoDocumento,double dLineaJDE, int iNoBatch, String sCuenta, String sIdCuenta, String sCodUnineg,String sCuentaObj,String sCuentaSub,String sTipoAsiento,String sMoneda, int iMonto, String sConcepto,String sUsuario,String sCodApp, BigDecimal dTasa, String sTipoCliente, String sObservacion,String sCodSucCuenta){
			boolean bRegistrado = true;
			String sNombrePc = null;
			String sql = "";
			PreparedStatement ps = null;
			Date dHora = new Date();
			SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			try{
				Calendar cFecha = Calendar.getInstance();
				CalendarToJulian fecha = new CalendarToJulian(cFecha);
				int iFecha = fecha.getDate();
				sNombrePc = "SERVER";//InetAddress.getLocalHost().getHostName();
				//obtener hora en enteros
				String[] sHora = (dfHora.format(dHora)).split(":");
				int iHora = Integer.parseInt(sHora[0] + sHora[1] + sHora[2]);
				//obtener partes de la fecha
				Date dFecha = new Date();
				String[] sFecha = (sdf.format(dFecha)).split("/");
				//agregar blancos a unineg
				if(sCodUnineg.length() == 2){
					sCodUnineg = "          "+sCodUnineg;
					sCuenta = "          "+sCuenta;
				}else{
					sCodUnineg = "        "+sCodUnineg;
					sCuenta = "        "+sCuenta;
				}
				if(sConcepto.length() > 30){
					sConcepto = sConcepto.substring(0,29);
				}
				if(sTipoCliente.equals("E")){
					sTipoCliente = "EMP";
				}else{
					sTipoCliente = "";
				}
				if(sNombrePc.length() > 9){
					sNombrePc =	sNombrePc.substring(0, 9);
				}
				if(sObservacion.length() > 30){
					sObservacion = sObservacion.substring(0,29);
				}
				
				sql = "INSERT INTO "+PropertiesSystem.JDEDTA+".F0911 (GLKCO,GLDCT,GLDOC,GLDGJ,GLJELN,GLICU,GLICUT,GLDICJ,GLDSYJ,GLTICU,GLCO,GLANI,GLAM,GLAID,GLMCU,GLOBJ,GLSUB,GLLT,GLPN,GLCTRY,GLFY,GLCRCD,GLAA,GLEXA,GLDKJ,GLDSVJ,GLTORG,GLUSER,GLPID,GLJOBN,GLUPMJ,GLUPMT,GLCRR,GLGLC,GLEXR) VALUES(" +
				"'000"+sCodSuc+"'," +
				"'P9'," +
				""+iNoDocumento+"," +
				""+iFecha+"," +
				""+dLineaJDE+"," +
				""+iNoBatch+"," +
				"'G'," +
				""+iFecha+"," +
				""+iFecha+"," +
				""+iHora+"," +
				"'000"+sCodSucCuenta+"'," +
				"'"+sCuenta+"'," +
				"'2'," +
				"'"+sIdCuenta+"'," +
				"'"+sCodUnineg+"'," +
				"'"+sCuentaObj+"'," +
				"'"+sCuentaSub+"'," +
				"'"+sTipoAsiento+"'," +
				""+Integer.parseInt(sFecha[1])+"," +
				"20," +
				""+Integer.parseInt(sFecha[2].substring(2, 4))+"," +
				"'"+sMoneda+"'," +
				""+iMonto+"," +
				"'"+sConcepto+"'," +
				""+iFecha+"," +
				""+iFecha+"," +
				"'"+sUsuario+"'," +
				"'"+sUsuario+"'," +
				"'"+sCodApp+"'," +
				"'"+sNombrePc+"'," +
				""+iFecha+"," +
				""+iHora+"," +
				""+dTasa+"," +
				"'"+sTipoCliente+"'," +
				"'"+sObservacion+"'" +
				")";
				ps = cn.prepareStatement(sql);			
				int rs = ps.executeUpdate();
				if (rs != 1){
					bRegistrado = false;
				}
			}catch(Exception ex){
				bRegistrado = false;
				System.out.println("Se capturo una excepcion en ReciboCtrl.registrarAsientoDiario: " + ex);
			}finally {
				try {
					ps.close();
					//cn.close();
				} catch (Exception se2) {
					System.out.println("ERROR: Failed to close connection en ReciboCtrl.registrarAsientoDiario: " + se2);
				}
			}
			return bRegistrado;
		}
	/*************************************************************************************************************/
	/****************ACTUALIZAR EL NUMERO DE DOCUMENTO EN "+PropertiesSystem.JDECOM+".F0002****************************************/
		public boolean actualizarNumeroDocumento(Connection cn,int iNoDocumento){
			boolean bActualizado = true;
			String sql = "UPDATE "+PropertiesSystem.JDECOM+".F0002 SET NNN002 = " + iNoDocumento + " where NNSY = '09'";
			PreparedStatement ps = null;
			try{
				ps = cn.prepareStatement(sql);
				int rs = ps.executeUpdate();
				if (rs != 1){
					bActualizado = false;
				}
			}catch(Exception ex){
				bActualizado = false;
				System.out.println("Se capturo una excepcion en ReciboCtrl.actualizarNumeroDocumento: " + ex);
			}finally {
				try {
					ps.close();
					//cn.close();
				} catch (Exception se2) {
					System.out.println("ERROR: Failed to close connection en ReciboCtrl.actualizarNumeroRecibo: " + se2);
				}
			}
			return bActualizado;
		}
	/*************************************************************************************************************/
		/****************ACTUALIZAR EL NUMERO DE RECIBO DE JDE EN "+PropertiesSystem.JDECOM+".F0002****************************************/
		public boolean actualizarNumeroReciboJde(Connection cn,int iNoDocumento){
			boolean bActualizado = true;
			String sql = "UPDATE "+PropertiesSystem.JDECOM+".F0002 SET NNN005 = " + iNoDocumento + " where NNSY = '03'";
			PreparedStatement ps = null;
			try{
				ps = cn.prepareStatement(sql);
				int rs = ps.executeUpdate();
				if (rs != 1){
					bActualizado = false;
				}
			}catch(Exception ex){
				bActualizado = false;
				System.out.println("Se capturo una excepcion en ReciboCtrl.actualizarNumeroReciboJde: " + ex);
			}finally {
				try {
					ps.close();
					//cn.close();
				} catch (Exception se2) {
					System.out.println("ERROR: Failed to close connection en ReciboCtrl.actualizarNumeroReciboJde: " + se2);
				}
			}
			return bActualizado;
		}
/*************************************************************************************************************/
		/*******************REGISTRAR TRANSACCION EN BATCH "+PropertiesSystem.JDEDTA+".F0011********************************************/
		public boolean registrarBatch(Connection cn,String sTipoBatch,int iNoBatch,int iTotalTransaccion, String sUsuario, int iCantDocs,String sIcpob){
			boolean registrado = true;
			Date dHora = new Date();
			SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			String sql = "";
			PreparedStatement ps = null;
			String sNombrePc = null;
			try{
				Calendar cFecha = Calendar.getInstance();
				CalendarToJulian fecha = new CalendarToJulian(cFecha);
				int iFecha = fecha.getDate();
				sNombrePc = "SERVER";//InetAddress.getLocalHost().getHostName();
				//obtener hora en enteros
				String[] sHora = (dfHora.format(dHora)).split(":");
				int iHora = Integer.parseInt(sHora[0] + sHora[1] + sHora[2]);
				//obtener partes de la fecha
				Date dFecha = new Date();
				String[] sFecha = (sdf.format(dFecha)).split("/");
				
				if(sNombrePc.length() > 9){
					sNombrePc =	sNombrePc.substring(0, 9);
				}
				
				/*sql = "INSERT INTO "+PropertiesSystem.JDEDTA+".F0011 VALUES(" +  sentencia para la nueva version del jde
				"'G'," +
				"+"+iNoBatch+"," +
				"''," +
				"''," +
				"0," +
				"'"+sUsuario+"'," +
				""+iFecha+"," +
				"0," +
				"'Y'," +
				"''," +
				
				""+iCantDocs+"," +
				"'"+sUsuario+"'," +
				"''," +
				"''," +	
				""+iTotalTransaccion+"," +
				"0," +
				"'"+sNombrePc+"'"  +
				")";*/
				
				sql = "INSERT INTO "+PropertiesSystem.JDEDTA+".F0011 VALUES(" +  //version viejita
				"'"+sTipoBatch+"'," +        
				"+"+iNoBatch+"," +
				"''," +
				"''," +
				"0," +
				"'"+sUsuario+"'," +
				""+iFecha+"," +
				"0," +
				"'Y'," +
				"''," +
				
				""+iTotalTransaccion+"," +	
				""+iCantDocs+"," +
				"''," +
				"'" +sIcpob +"'," +
				"''" +	
				")";
				
				ps = cn.prepareStatement(sql);
				int rs = ps.executeUpdate();
				if (rs != 1){
					registrado = false;
				}
			}catch(Exception ex){
				registrado = false;
				System.out.println("Se capturo una excepcion en ReciboCtrl.registrarBatch: " + ex);
			}finally {
				try {
					ps.close();
					//cn.close();
				} catch (Exception se2) {
					System.out.println("ERROR: Failed to close connection en ReciboCtrl.registrarBatch: " + se2);
				}
			}
			return registrado;
		}
/****************************************************************************************/
/*******************REGISTRAR TRANSACCION EN BATCH "+PropertiesSystem.JDEDTA+".F0011********************************************/
		public boolean registrarBatchA92(Connection cn,String sTipoBatch,int iNoBatch, long iTotalTransaccion, String sUsuario, int iCantDocs,String sIcpob){
			boolean registrado = true;
			Date dHora = new Date();
			SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			String sql = "";
			PreparedStatement ps = null;
			String sNombrePc = null;
			try{
				Calendar cFecha = Calendar.getInstance();
				CalendarToJulian fecha = new CalendarToJulian(cFecha);
				int iFecha = fecha.getDate();
				
				//obtener hora en enteros
				String[] sHora = (dfHora.format(dHora)).split(":");
				int iHora = Integer.parseInt(sHora[0] + sHora[1] + sHora[2]);
				
				//obtener partes de la fecha
				Date dFecha = new Date();
				String[] sFecha = (sdf.format(dFecha)).split("/");
				
				sNombrePc = PropertiesSystem.ESQUEMA;
				if(sNombrePc.length() > 9){
					sNombrePc =	sNombrePc.substring(0, 9);
				}
				
				sql = "INSERT INTO "+PropertiesSystem.JDEDTA+".F0011 VALUES(" +
				"'"+sTipoBatch+"'," +
				"+"+iNoBatch+"," +
				"''," +
				"''," +
				"0," +
				"'"+sUsuario+"'," +
				""+iFecha+"," +
				"0," +
				"'Y'," +
				"''," +
				
				""+iCantDocs+"," +
				"'"+sUsuario+"'," +
				"''," +
				"''," +	
				""+iTotalTransaccion+"," +
				"0," +
				"'"+sNombrePc+"'"  +
				")";
				
				ps = cn.prepareStatement(sql);
				int rs = ps.executeUpdate();
				if (rs != 1){
					registrado = false;
				}
			}catch(Exception ex){
				registrado = false;
				error = new Exception("@LOGCAJA: error No se pudo grabar el encabezado del batch:"+iNoBatch+ ";"+sTipoBatch+";");
				errorDetalle = ex;
				System.out.println("Se capturo una excepcion en ReciboCtrl.registrarBatchA92: " + ex);
			}finally {
				try {
					ps.close();
					//cn.close();
				} catch (Exception se2) {					
					System.out.println("ERROR: Failed to close connection en ReciboCtrl.registrarBatchA92: " + se2);
				}
			}
			return registrado;
		}
		
		/****************************************************************************************/
		/*******************REGISTRAR TRANSACCION EN BATCH "+PropertiesSystem.JDEDTA+".F0011********************************************/
				public boolean registrarBatchA92WithSession(Session s,String sTipoBatch,int iNoBatch, long iTotalTransaccion, String sUsuario, int iCantDocs,String sIcpob){
					boolean registrado = true;
					Date dHora = new Date();
					SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					String sql = "";					
					String sNombrePc = null;
					try{
						Calendar cFecha = Calendar.getInstance();
						CalendarToJulian fecha = new CalendarToJulian(cFecha);
						int iFecha = fecha.getDate();
						
						//obtener hora en enteros
						String[] sHora = (dfHora.format(dHora)).split(":");
						int iHora = Integer.parseInt(sHora[0] + sHora[1] + sHora[2]);
						
						//obtener partes de la fecha
						Date dFecha = new Date();
						String[] sFecha = (sdf.format(dFecha)).split("/");
						
						sNombrePc = PropertiesSystem.ESQUEMA;
						if(sNombrePc.length() > 9){
							sNombrePc =	sNombrePc.substring(0, 9);
						}
						
						sql = "INSERT INTO "+PropertiesSystem.JDEDTA+".F0011 VALUES(" +
						"'"+sTipoBatch+"'," +
						"+"+iNoBatch+"," +
						"''," +
						"''," +
						"0," +
						"'"+sUsuario+"'," +
						""+iFecha+"," +
						"0," +
						"'Y'," +
						"''," +
						
						""+iCantDocs+"," +
						"'"+sUsuario+"'," +
						"''," +
						"''," +	
						""+iTotalTransaccion+"," +
						"0," +
						"'"+sNombrePc+"'"  +
						")";
						
						ConsolidadoDepositosBcoCtrl.executeSqlQueryTx(s, sql);
					}catch(Exception ex){
						registrado = false;
						error = new Exception("@LOGCAJA: error No se pudo grabar el encabezado del batch:"+iNoBatch+ ";"+sTipoBatch+";");
						errorDetalle = ex;
						System.out.println("Se capturo una excepcion en ReciboCtrl.registrarBatchA92: " + ex);
					}
					return registrado;
				}
/****************************************************************************************/
/** Método: Guardar Registro en el GCPPRRDTA.F0311 con parámetro de fecha del registro.
 * 			(Variante del método RegistrarBatch de JC)
 *	Fecha:  27/07/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 **/


		public boolean registrarBatch(Date dtFecha,Connection cn,String sTipoBatch,int iNoBatch,int iTotalTransaccion, String sUsuario, int iCantDocs,String sIcpob){
				boolean registrado = true;
				String sql = "";
				PreparedStatement ps = null;
				String sNombrePc = null;
				FechasUtil f = new FechasUtil();
				int iFecha = 0;
				
				try{
					iFecha = f.obtenerFechaJulianaDia(dtFecha);
					sNombrePc = "SERVER";//InetAddress.getLocalHost().getHostName();			
					if(sNombrePc.length() > 9){
						sNombrePc =	sNombrePc.substring(0, 9);
					}
					sql = "INSERT INTO "+PropertiesSystem.JDEDTA+".F0011 VALUES(" + 
					"'"+sTipoBatch+"'," +        
					"+"+iNoBatch+"," +
					"''," +
					"''," +
					"0," +
					"'"+sUsuario+"'," +
					""+iFecha+"," +
					"0," +
					"'Y'," +
					"''," +
					""+iTotalTransaccion+"," +	
					""+iCantDocs+"," +
					"''," +
					"'" +sIcpob +"'," +
					"''" +	
					")";
					
					ps = cn.prepareStatement(sql);
					int rs = ps.executeUpdate();
					if (rs != 1){
						registrado = false;
					}
				}catch(Exception ex){
					registrado = false;
					System.out.println("Se capturo una excepcion en ReciboCtrl.registrarBatch: " + ex);
				}finally {
					try {
						ps.close();
					} catch (Exception se2) {
						System.out.println("ERROR: Failed to close connection en ReciboCtrl.registrarBatch: " + se2);
					}
				}
				return registrado;
		}
			/***************************************************************************************************************/
	
		public boolean registrarBatchA92(Session session, Date fecha, String cjTipoRecibo, int iNoBatch, long iTotalTransaccion, String sUsuario, int iCantDocs, String jobname, String statusBatch ) {
			boolean update = true;
			
			try {
				
				if( iNoBatch == 0 ){
					return update = false;
				}

				BatchControlF0011 f0011 = new BatchControlF0011(
						cjTipoRecibo,
						String.valueOf(iNoBatch),
						"0",
						sUsuario,
						String.valueOf(iTotalTransaccion),
						String.valueOf( iCantDocs ),
						"0",
						fecha,
						jobname,
						statusBatch
					); 
					
				String sqlInsert =  f0011.insertStatement();
				
				try {
					
					ConsolidadoDepositosBcoCtrl .executeSqlQueryTx( session, sqlInsert );
					
				} catch (Exception e) { 
					e.printStackTrace();
				}
				
				
			} catch (Exception e) {
				e.printStackTrace(); 
			}
			return update;
		}
		public boolean registrarBatchA92Session(Session session, Date fecha, String cjTipoRecibo, int iNoBatch, long iTotalTransaccion, String sUsuario, int iCantDocs, String jobname, String statusBatch ) {
			boolean update = true;
			
			try {
				
				if( iNoBatch == 0 ){
					return update = false;
				}

				BatchControlF0011 f0011 = new BatchControlF0011(
						cjTipoRecibo,
						String.valueOf(iNoBatch),
						"0",
						sUsuario,
						String.valueOf(iTotalTransaccion),
						String.valueOf( iCantDocs ),
						"0",
						fecha,
						jobname,
						statusBatch
						); 
					
				String sqlInsert =  f0011.insertStatement();
				
				try {
					
					int rows = session.createSQLQuery(sqlInsert).executeUpdate();
					update = rows ==1 ;
					
				} catch (Exception e) { 
					e.printStackTrace();
				}
				
				
			} catch (Exception e) {
				update = false;
				e.printStackTrace(); 
			}
			return update;
		}
		
	public boolean registrarBatchA92(Date dtFecha,Connection cn,String sTipoBatch,int iNoBatch, long iTotalTransaccion, String sUsuario, int iCantDocs,String sIcpob){
		boolean registrado = true;
		String sql = "";
		PreparedStatement ps = null;
		String sNombrePc = null;
		FechasUtil f = new FechasUtil();
		int iFecha = 0;
		
		try{
			
			iFecha = f.obtenerFechaJulianaDia(dtFecha);
			
			//InetAddress.getLocalHost().getHostName();	
			sNombrePc = PropertiesSystem.ESQUEMA;		
			if(sNombrePc.length() > 9){
				sNombrePc =	sNombrePc.substring(0, 9);
			}	
			
			sql = "INSERT INTO "+PropertiesSystem.JDEDTA+".F0011 VALUES(" +
			"'"+sTipoBatch+"', " +  
			iNoBatch+"," +
			"''," +
			"''," +
			"0," +
			"'"+sUsuario+"'," +
			""+iFecha+"," +
			"0," +
			"'Y'," +
			"''," +
			
			""+iCantDocs+"," +
			"'"+sUsuario+"'," +
			"''," +
			"''," +	
			""+iTotalTransaccion+"," +
			"0," +
			"'"+sNombrePc+"'"  +
			")";
			
			ps = cn.prepareStatement(sql);
			int rs = ps.executeUpdate();
			if (rs != 1){
				registrado = false;
			}
		}catch(Exception ex){
			registrado = false;
			String invocado =  new Exception().getStackTrace()[1].getClassName() +":"+ new Exception().getStackTrace()[1].getMethodName() ;
 
			ex.printStackTrace();
		}finally {
			try {
				ps.close();
			} catch (Exception se2) {
				se2.printStackTrace();
			}
		}
		return registrado;
	}
	public boolean registrarBatchA92(Date dtFecha,Session s,String sTipoBatch,int iNoBatch, long iTotalTransaccion, String sUsuario, int iCantDocs,String sIcpob, String estadoBatch){
		boolean registrado = true;
		String sql = "";		
		String sNombrePc = null;
		FechasUtil f = new FechasUtil();
		int iFecha = 0;
		
		try{
			
			iFecha = f.obtenerFechaJulianaDia(dtFecha);
			
			//InetAddress.getLocalHost().getHostName();	
			sNombrePc = PropertiesSystem.ESQUEMA;		
			if(sNombrePc.length() > 9){
				sNombrePc =	sNombrePc.substring(0, 9);
			}	
			
			sql = "INSERT INTO "+PropertiesSystem.JDEDTA+".F0011 VALUES(" +
			"'"+sTipoBatch+"', " +  
			iNoBatch+"," +
			
			" '"+estadoBatch+"', " + // estado del batch;
			" '"+estadoBatch+"', " + // estado del batch;
			 
			"0," +
			"'"+sUsuario+"'," +
			""+iFecha+"," +
			"0," +
			"'Y'," +
			"''," +
			
			""+iCantDocs+"," +
			"'"+sUsuario+"'," +
			"'"+sIcpob+"'," +
			"''," +	
			""+iTotalTransaccion+"," +
			"0," +
			"'"+sNombrePc+"'"  +
			")";
			
			ConsolidadoDepositosBcoCtrl.executeSqlQueryTx(s, sql);
		}catch(Exception ex){
			registrado = false;
			String invocado =  new Exception().getStackTrace()[1].getClassName() +":"+ new Exception().getStackTrace()[1].getMethodName() ;
 
			ex.printStackTrace();
		}
		return registrado;
	}
	/***************************************************************************************************************/
	/***********LLENAR ENLACE ENTRE RECIBO DE CAJA Y RECIBO O DOCUMENTO DEL JDE*************************************/
	public boolean fillEnlaceMcajaJde(Session session, Transaction tx,
			int iNumrec, String sCodComp, int iRecJde, int iNobatch, int iCaId,
			String sCodSuc, String sTipodoc, String sTiporec) {
		boolean bRegistrado = true;
		String parametro = "";

		try {
			parametro = "Recibojde: caja:'" + iCaId + "' || codcomp:'"
					+ sCodComp + "' || codsuc:'" + sCodSuc + "' || numrec:'"
					+ iNumrec + "' || tiporec:'" + sTiporec + "' || iNobatch:'"
					+ iNobatch + "' || tipodoc:'" + sTipodoc + "' ";
			
//			if(sTiporec.compareTo("FN") == 0)
//				LogCrtl.sendLogInfo("Grabar en : " + parametro);

			Recibojde recibojde = new Recibojde();
			RecibojdeId recibojdeid = new RecibojdeId();
			recibojdeid.setNumrec(iNumrec);
			recibojdeid.setCodcomp(sCodComp);
			recibojdeid.setNobatch(iNobatch);
			recibojdeid.setCaid(iCaId);
			recibojdeid.setCodsuc(sCodSuc);
			recibojdeid.setRecjde(iRecJde);
			recibojdeid.setTipodoc(sTipodoc);
			recibojdeid.setTiporec(sTiporec);
			recibojde.setId(recibojdeid);
			session.save(recibojde);

		} catch (Exception ex) {
			bRegistrado = false;
			ex.printStackTrace();

			if (sTiporec.compareTo("FN") == 0) {
//				LogCrtl.sendLogInfo("Error al grabar en Recibojde: "
//						+ parametro);
				StringWriter errors = new StringWriter();
				ex.printStackTrace(new PrintWriter(errors));
//				LogCrtl.sendLogInfo(errors.toString());
			}
			error = new Exception(
					"@LOGCAJA: No se pudo grabar el enlace entre caja y JDE "
							+ sCodComp + ";" + iNumrec + ";recjde=" + iRecJde
							+ ";iNobatch=" + iNobatch);
			errorDetalle = ex;
		}
		return bRegistrado;
	}
	
	
	public static boolean crearRegistroReciboCajaJde(int caid, int numrec, String codcomp, String codsuc, String tiporec, String tipodoc, long nobatch, List<String>numRecibosjde ){
		boolean done = true;
		
		try {
			
			String queryValues = "(@NUMREC, '@CODCOMP', @RECJDE, @NOBATCH, @CAID, '@CODSUC', '@TIPODOC', '@TIPOREC' ),"; 
			
			queryValues = queryValues
					.replace("@NUMREC", String.valueOf(numrec) )
					.replace("@CODCOMP", codcomp) 
					.replace("@NOBATCH", String.valueOf(nobatch))
					.replace("@CAID",  String.valueOf(caid) )
					.replace("@CODSUC", codsuc)
					.replace("@TIPODOC", tipodoc)
					.replace("@TIPOREC", tiporec) ;
			
			String insert = "";
			for (String numDocJde : numRecibosjde) {
				insert += queryValues.replace("@RECJDE", numDocJde) ;
			}
					
			insert = "insert into "+PropertiesSystem.ESQUEMA+".RECIBOJDE VALUES "  +  insert.substring(0, insert.lastIndexOf(",")) ;
			
			LogCajaService.CreateLog("crearRegistroReciboCajaJde", "QRY", insert);
			
			done = ConsolidadoDepositosBcoCtrl.executeSqlQueryTx(null, insert);
			
		} catch (Exception e) {
			LogCajaService.CreateLog("crearRegistroReciboCajaJde", "ERR", e.getMessage());
			done = false;
			e.printStackTrace(); 
		} 
		return done;
	}
	
	//**********************************************************
	//----------------------------------------------------------
	//++++++++++++++++++      INICIO     +++++++++++++++++++++++
	//----------------------------------------------------------
	//**********************************************************
	//Creado por Luis Alberto Fonseca Mendez 2019-04-23
	public static boolean crearRegistroReciboCajaJde(Session sesion, Transaction trans, 
													 int caid, int numrec, String codcomp, 
													 String codsuc, String tiporec, String tipodoc, 
													 long nobatch, List<String>numRecibosjde )
	{
		boolean done = true;
		
		try {
			
			String queryValues = "(@NUMREC, '@CODCOMP', @RECJDE, @NOBATCH, @CAID, '@CODSUC', '@TIPODOC', '@TIPOREC' ),"; 
			
			queryValues = queryValues
					.replace("@NUMREC", String.valueOf(numrec) )
					.replace("@CODCOMP", codcomp) 
					.replace("@NOBATCH", String.valueOf(nobatch))
					.replace("@CAID",  String.valueOf(caid) )
					.replace("@CODSUC", codsuc)
					.replace("@TIPODOC", tipodoc)
					.replace("@TIPOREC", tiporec) ;
			
			String insert = "";
			for (String numDocJde : numRecibosjde) {
				insert += queryValues.replace("@RECJDE", numDocJde) ;
			}
					
			insert = "insert into "+PropertiesSystem.ESQUEMA+".RECIBOJDE VALUES "  +  insert.substring(0, insert.lastIndexOf(",")) ;
			
			done = ConsolidadoDepositosBcoCtrl.executeSqlQuery(sesion, trans, insert);
			
		} catch (Exception e) {
			done = false;
			e.printStackTrace(); 
		} 
		return done;
	}
	//**********************************************************
	//----------------------------------------------------------
	//++++++++++++++++++       FIN       +++++++++++++++++++++++
	//----------------------------------------------------------
	//**********************************************************
	
	public static boolean crearRegistroReciboCajaJde(Session session, int caid, int numrec, String codcomp, String codsuc, String tiporec, String tipodoc, long nobatch, List<String>numRecibosjde ){
		boolean done = true;
		
		try {
			
			String queryValues = "(@NUMREC, '@CODCOMP', @RECJDE, @NOBATCH, @CAID, '@CODSUC', '@TIPODOC', '@TIPOREC' ),"; 
			
			queryValues = queryValues
					.replace("@NUMREC", String.valueOf(numrec) )
					.replace("@CODCOMP", codcomp) 
					.replace("@NOBATCH", String.valueOf(nobatch))
					.replace("@CAID",  String.valueOf(caid) )
					.replace("@CODSUC", codsuc)
					.replace("@TIPODOC", tipodoc)
					.replace("@TIPOREC", tiporec) ;
			
			String insert = "";
			for (String numDocJde : numRecibosjde) {
				insert += queryValues.replace("@RECJDE", numDocJde) ;
			}
					
			insert = "insert into "+PropertiesSystem.ESQUEMA+".RECIBOJDE VALUES "  +  insert.substring(0, insert.lastIndexOf(",")) ;
			
			LogCajaService.CreateLog("crearRegistroReciboCajaJde", "QRY", insert);
			
			int rows = session.createSQLQuery(insert).executeUpdate() ;
			
			done = rows > 0 ;
			
		} catch (Exception e) {
			LogCajaService.CreateLog("crearRegistroReciboCajaJde", "ERR", e.getMessage());
			done = false;
		} 
		return done;
	}
	
	
	public static boolean grabarAsociacionCajaJDE(Session session,  
			int iNumrec, String sCodComp, int iRecJde, int iNobatch, int iCaId,
			String sCodSuc, String sTipodoc, String sTiporec) {
		
		boolean bRegistrado = true;
		
		try {
			
			Recibojde recibojde = new Recibojde();
			RecibojdeId recibojdeid = new RecibojdeId();
			
			recibojdeid.setNumrec(iNumrec);
			recibojdeid.setCodcomp(sCodComp);
			recibojdeid.setNobatch(iNobatch);
			recibojdeid.setCaid(iCaId);
			recibojdeid.setCodsuc(sCodSuc);
			recibojdeid.setRecjde(iRecJde);
			recibojdeid.setTipodoc(sTipodoc);
			recibojdeid.setTiporec(sTiporec);
			recibojde.setId(recibojdeid);
			
			session.save(recibojde);
		
		} catch (Exception ex) {
			
			ex.printStackTrace(); 
			bRegistrado = false;
		}
		return bRegistrado;
	}
	
/***************************************************************************************************************/	
		public boolean borrarAsientodeDiario(Session session, int iNoDocumento, int iNoBatch){
			boolean bBorrado = true;

			try{
			 
				String sql = "delete from "+PropertiesSystem.JDEDTA+".F0911 where gldoc = " + iNoDocumento + " and glicu = " + iNoBatch;
				
				LogCajaService.CreateLog("borrarAsientodeDiario", "QRY", sql);
				
				bBorrado = ConsolidadoDepositosBcoCtrl.executeSqlQueryTx(session, sql);
			
			}catch(Exception ex){
				LogCajaService.CreateLog("borrarAsientodeDiario", "ERR", ex.getMessage());
				bBorrado = false;
			} 
			
			return bBorrado;
		}
	/****************************************************************************************************************************/
		public boolean borrarBatch(Session session, int iNoBatch, String sTipoBatch){
			boolean bBorrado = true ;
			String sql = "delete from "+PropertiesSystem.JDEDTA+".F0011 where icicu = " +iNoBatch + " and icicut = '" +sTipoBatch+"'";
		 
			try{
				LogCajaService.CreateLog("borrarBatch", "QRY", sql);
				
				bBorrado = ConsolidadoDepositosBcoCtrl.executeSqlQueryTx(session, sql);
			}catch(Exception ex){
				LogCajaService.CreateLog("borrarBatch", "ERR", ex.getMessage());
				bBorrado = false;
			} 
			return bBorrado;
		}
/*****************************************************************************************************************************************/
		public boolean grabarRC(Connection cn,String sCodsuc, int iCodcli,String sTipoDoc,int iNoFactura,String sPartida,int iFechaFactura, String sTipoRecibo,int iNoreciboJDE,
								int iFechaRecibo, String sTipoBatch, int iNobatch, double dMontoAplicar,String sModoMoneda,String sMoneda,double dTasa,
								double dMontoAplicarForaneo,String sGlOffset,String sIdCuenta, String sCodUnineg,String sTipoPago,String sTipoEntrada, String sConcepto,
								String sCashBasis, String sNomcli,String sUsuario,String sAplicacion,int iFechaVecto,String rppo,String rpdcto){
			boolean registrado = true;
			String sql  = "INSERT INTO "+PropertiesSystem.JDEDTA+".F0311 (RPKCO,RPAN8,RPDCT,RPDOC,RPSFX,RPDIVJ,RPDCTM,RPDOCM,RPDMTJ,RPDGJ,RPFY,RPCTRY,RPPN,RPCO,RPICUT,RPICU," +
							"RPDICJ,RPPA8,RPAN8J,RPBALJ,RPPST,RPAG,RPAAP,RPCRRM,RPCRCD,RPCRR,RPACR,RPFAP,RPDSVJ,RPGLC,RPGLBA,RPAM,RPMCU,RPPTC,RPDDJ,RPDDNJ," +
							"RPTRTC,RPRMK,RPALT6,RPALPH,RPATE,RPATR,RPATP,RPATO,RPATPR,RPAT1,RPAT2,RPAT3,RPAT4,RPAT5,RPTORG,RPUSER,RPPID,RPUPMJ,RPUPMT,RPJOBN," +
							"RPAC01,RPAC02,RPAC03,RPAC04,RPAC05,RPAC06,RPAC07,RPAC08,RPAC09,RPAC10,RPPO,RPDCTO) VALUES(";
			PreparedStatement ps = null;
			Date dHora = new Date();
			SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			String sNombrePc = null;
			EmpleadoCtrl empCtrl = new EmpleadoCtrl();
			Vf0101 vf0101 = null;
			Divisas d = new Divisas();
			String sCadValida = "";
			try{
				Calendar cFecha = Calendar.getInstance();
				CalendarToJulian fecha = new CalendarToJulian(cFecha);
				int iFecha = fecha.getDate();
				sNombrePc = "SERVER";//InetAddress.getLocalHost().getHostName();
				//obtener hora en enteros
				String[] sHora = (dfHora.format(dHora)).split(":");
				int iHora = Integer.parseInt(sHora[0] + sHora[1] + sHora[2]);
				//obtener partes de la fecha
				Date dFecha = new Date();
				String[] sFecha = (sdf.format(dFecha)).split("/");
				//agregar blancos a unineg
				if(sCodUnineg.length() == 2){
					sCodUnineg = "          "+sCodUnineg;
				}else{
					sCodUnineg = "        "+sCodUnineg;
				}
				if(sConcepto.length() > 30){
					sConcepto = sConcepto.substring(0,29);
				}
				if(sNombrePc.length() > 9){
					sNombrePc =	sNombrePc.substring(0, 9);
				}
				//BUSCAR INFO DEL CLIENTE
				vf0101 = empCtrl.buscarEmpleadoxCodigo2(iCodcli);
				//validar comilla  simple 
				sCadValida = d.remplazaCaractEspeciales(sNomcli, "'", "-");
				if(!sCadValida.equals("")){
					sNomcli = sCadValida;
				}
				if(rppo == null)rppo = "";
				
				if(rpdcto == null)rpdcto = "";
				
				sql = sql +
					"'"+sCodsuc+"'," + iCodcli + "," + "'"+sTipoDoc+"'," + iNoFactura+"," + "'"+sPartida+"'," + iFechaFactura +"," + "'"+sTipoRecibo+"'," +
					iNoreciboJDE + "," + iFechaRecibo + "," + iFechaRecibo + "," + ""+Integer.parseInt(sFecha[2].substring(2, 4))+"," +"20," + ""+Integer.parseInt(sFecha[1])+"," +
					"'"+sCodsuc+"'," + "'"+sTipoBatch+"'," + iNobatch+"," + iFechaRecibo + "," + iCodcli + "," + iCodcli + ",'Y'," + "'P'," + d.pasarAentero(dMontoAplicar * -1) + ",0," + 
					"'"+sModoMoneda+"'," + "'"+sMoneda+"'," + dTasa + "," + d.pasarAentero(dMontoAplicarForaneo * -1) + ",0," + iFechaRecibo + "," + "'"+sGlOffset+"'," +
					"'"+sIdCuenta+"'," + "'2'," + "'"+sCodUnineg+"'," + "'"+sTipoPago+"'," + iFechaVecto + "," + iFechaVecto + "," + "'" + sTipoEntrada + "'," +
					"'" + sConcepto + "'," + "'" + sCashBasis + "'," + "'" + sNomcli + "'," + "'" + vf0101.getId().getAbate() + "'," + "'" + vf0101.getId().getAbatr() + "'," +
					"'" + vf0101.getId().getAbatp() + "'," + "'Y'," + "'" + vf0101.getId().getAbatpr() + "'," + "'" + vf0101.getId().getAbat1() + "'," +
					"'" + vf0101.getId().getAbat2() + "'," + "'" + vf0101.getId().getAbat3() + "'," + "'" + vf0101.getId().getAbat4() + "'," + "'" + vf0101.getId().getAbat5() + "'," +
					"'" + sUsuario + "'," + "'" + sUsuario + "'," + "'" + sAplicacion + "'," + iFecha + "," + iHora + "," +"'"+sNombrePc+"'," + 
					"'" + vf0101.getId().getAbac01() + "'," + "'" + vf0101.getId().getAbac02() + "'," + "'" + vf0101.getId().getAbac03() + "'," + "'" + vf0101.getId().getAbac04() + "'," +
					"'" + vf0101.getId().getAbac05() + "'," + "'" + vf0101.getId().getAbac06() + "'," + "'" + vf0101.getId().getAbac07() + "'," + "'" + vf0101.getId().getAbac08() + "'," +
					"'" + vf0101.getId().getAbac09() + "'," + "'" + vf0101.getId().getAbac10() + "'," + "'" + rppo + "'," + "'" + rpdcto + "'" + 
					")";	
				ps = cn.prepareStatement(sql);
				int rs = ps.executeUpdate();
				if (rs != 1){
					registrado = false;
				}
			}catch(Exception ex){
				registrado = false;
				error = new Exception("@LOGCAJA: No se pudo grabar el Recibo en el JDE!!!;factura:"+iNoFactura+";tipo:" +sTipoDoc+";Partida:" + sPartida+";Suc:"+sCodsuc);
				errorDetalle = ex;			
				System.out.println("Se capturo una excepcion en ReciboCtrl.grabarRC: " + ex);
			}finally {
				try {
					ps.close();
					//cn.close();
				} catch (Exception se2) {
					System.out.println("ERROR: Failed to close connection en ReciboCtrl.grabarRC: " + se2);
				}
			}
			return registrado;
		}
		
		
		/*****************************************************************************************************************************************/
		public boolean grabarRCWithSession(Session s,String sCodsuc, int iCodcli,String sTipoDoc,int iNoFactura,String sPartida,int iFechaFactura, String sTipoRecibo,int iNoreciboJDE,
								int iFechaRecibo, String sTipoBatch, int iNobatch, double dMontoAplicar,String sModoMoneda,String sMoneda,double dTasa,
								double dMontoAplicarForaneo,String sGlOffset,String sIdCuenta, String sCodUnineg,String sTipoPago,String sTipoEntrada, String sConcepto,
								String sCashBasis, String sNomcli,String sUsuario,String sAplicacion,int iFechaVecto,String rppo,String rpdcto){
			boolean registrado = true;
			String sql  = "INSERT INTO "+PropertiesSystem.JDEDTA+".F0311 (RPKCO,RPAN8,RPDCT,RPDOC,RPSFX,RPDIVJ,RPDCTM,RPDOCM,RPDMTJ,RPDGJ,RPFY,RPCTRY,RPPN,RPCO,RPICUT,RPICU," +
							"RPDICJ,RPPA8,RPAN8J,RPBALJ,RPPST,RPAG,RPAAP,RPCRRM,RPCRCD,RPCRR,RPACR,RPFAP,RPDSVJ,RPGLC,RPGLBA,RPAM,RPMCU,RPPTC,RPDDJ,RPDDNJ," +
							"RPTRTC,RPRMK,RPALT6,RPALPH,RPATE,RPATR,RPATP,RPATO,RPATPR,RPAT1,RPAT2,RPAT3,RPAT4,RPAT5,RPTORG,RPUSER,RPPID,RPUPMJ,RPUPMT,RPJOBN," +
							"RPAC01,RPAC02,RPAC03,RPAC04,RPAC05,RPAC06,RPAC07,RPAC08,RPAC09,RPAC10,RPPO,RPDCTO) VALUES(";
			
			Date dHora = new Date();
			SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			String sNombrePc = null;
			EmpleadoCtrl empCtrl = new EmpleadoCtrl();
			Vf0101 vf0101 = null;
			Divisas d = new Divisas();
			String sCadValida = "";
			try{
				Calendar cFecha = Calendar.getInstance();
				CalendarToJulian fecha = new CalendarToJulian(cFecha);
				int iFecha = fecha.getDate();
				sNombrePc = "SERVER";//InetAddress.getLocalHost().getHostName();
				//obtener hora en enteros
				String[] sHora = (dfHora.format(dHora)).split(":");
				int iHora = Integer.parseInt(sHora[0] + sHora[1] + sHora[2]);
				//obtener partes de la fecha
				Date dFecha = new Date();
				String[] sFecha = (sdf.format(dFecha)).split("/");
				//agregar blancos a unineg
				if(sCodUnineg.length() == 2){
					sCodUnineg = "          "+sCodUnineg;
				}else{
					sCodUnineg = "        "+sCodUnineg;
				}
				if(sConcepto.length() > 30){
					sConcepto = sConcepto.substring(0,29);
				}
				if(sNombrePc.length() > 9){
					sNombrePc =	sNombrePc.substring(0, 9);
				}
				//BUSCAR INFO DEL CLIENTE
				vf0101 = empCtrl.buscarEmpleadoxCodigo2(iCodcli);
				//validar comilla  simple 
				sCadValida = d.remplazaCaractEspeciales(sNomcli, "'", "-");
				if(!sCadValida.equals("")){
					sNomcli = sCadValida;
				}
				if(rppo == null)rppo = "";
				
				if(rpdcto == null)rpdcto = "";
				
				sql = sql +
					"'"+sCodsuc+"'," + iCodcli + "," + "'"+sTipoDoc+"'," + iNoFactura+"," + "'"+sPartida+"'," + iFechaFactura +"," + "'"+sTipoRecibo+"'," +
					iNoreciboJDE + "," + iFechaRecibo + "," + iFechaRecibo + "," + ""+Integer.parseInt(sFecha[2].substring(2, 4))+"," +"20," + ""+Integer.parseInt(sFecha[1])+"," +
					"'"+sCodsuc+"'," + "'"+sTipoBatch+"'," + iNobatch+"," + iFechaRecibo + "," + iCodcli + "," + iCodcli + ",'Y'," + "'P'," + d.pasarAentero(dMontoAplicar * -1) + ",0," + 
					"'"+sModoMoneda+"'," + "'"+sMoneda+"'," + dTasa + "," + d.pasarAentero(dMontoAplicarForaneo * -1) + ",0," + iFechaRecibo + "," + "'"+sGlOffset+"'," +
					"'"+sIdCuenta+"'," + "'2'," + "'"+sCodUnineg+"'," + "'"+sTipoPago+"'," + iFechaVecto + "," + iFechaVecto + "," + "'" + sTipoEntrada + "'," +
					"'" + sConcepto + "'," + "'" + sCashBasis + "'," + "'" + sNomcli + "'," + "'" + vf0101.getId().getAbate() + "'," + "'" + vf0101.getId().getAbatr() + "'," +
					"'" + vf0101.getId().getAbatp() + "'," + "'Y'," + "'" + vf0101.getId().getAbatpr() + "'," + "'" + vf0101.getId().getAbat1() + "'," +
					"'" + vf0101.getId().getAbat2() + "'," + "'" + vf0101.getId().getAbat3() + "'," + "'" + vf0101.getId().getAbat4() + "'," + "'" + vf0101.getId().getAbat5() + "'," +
					"'" + sUsuario + "'," + "'" + sUsuario + "'," + "'" + sAplicacion + "'," + iFecha + "," + iHora + "," +"'"+sNombrePc+"'," + 
					"'" + vf0101.getId().getAbac01() + "'," + "'" + vf0101.getId().getAbac02() + "'," + "'" + vf0101.getId().getAbac03() + "'," + "'" + vf0101.getId().getAbac04() + "'," +
					"'" + vf0101.getId().getAbac05() + "'," + "'" + vf0101.getId().getAbac06() + "'," + "'" + vf0101.getId().getAbac07() + "'," + "'" + vf0101.getId().getAbac08() + "'," +
					"'" + vf0101.getId().getAbac09() + "'," + "'" + vf0101.getId().getAbac10() + "'," + "'" + rppo + "'," + "'" + rpdcto + "'" + 
					")";	
				ConsolidadoDepositosBcoCtrl.executeSqlQueryTx(s, sql);
			}catch(Exception ex){
				registrado = false;
				error = new Exception("@LOGCAJA: No se pudo grabar el Recibo en el JDE!!!;factura:"+iNoFactura+";tipo:" +sTipoDoc+";Partida:" + sPartida+";Suc:"+sCodsuc);
				errorDetalle = ex;			
				System.out.println("Se capturo una excepcion en ReciboCtrl.grabarRC: " + ex);
			}
			return registrado;
		}
/***MODIFICAR FACTURA EN EL F0311***/
		public boolean aplicarMontoF0311(Connection cn,String sEstadoPago,double dMontopendienteDom,double dMontoPendienteForaneo,String sUsuario,
										String sCodSuc, String sTipoFactura,int iNofactura, String sPartida, int iCodclie){
			boolean registrado = true;
			String sql  = "";
			PreparedStatement ps = null;
			Date dHora = new Date();
			SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Divisas d = new Divisas();
			try{
				Calendar cFecha = Calendar.getInstance();
				CalendarToJulian fecha = new CalendarToJulian(cFecha);
				int iFecha = fecha.getDate();
				//obtener hora en enteros
				String[] sHora = (dfHora.format(dHora)).split(":");
				int iHora = Integer.parseInt(sHora[0] + sHora[1] + sHora[2]);
				//obtener partes de la fecha
				Date dFecha = new Date();
				String[] sFecha = (sdf.format(dFecha)).split("/");
				sql = "UPDATE "+PropertiesSystem.JDEDTA+".F0311 SET RPPST = '"+ sEstadoPago +"', RPAAP = " + d.pasarAentero(dMontopendienteDom) + ",RPFAP = " + d.pasarAentero(dMontoPendienteForaneo) + "," + 
						"RPUSER = '" + sUsuario +"', RPUPMJ = " + iFecha + ", RPUPMT = " + iHora + " WHERE RPKCO = '"+ sCodSuc + "' AND RPDCT ='" + sTipoFactura + "'" +
								" AND RPDOC = " + iNofactura + " AND RPSFX = '" + sPartida + "' AND TRIM(RPDCTM) = '' AND RPDOCM = 0 AND RPAN8 = "+iCodclie;
				ps = cn.prepareStatement(sql);
				int rs = ps.executeUpdate();
				if (rs != 1){
					error = new Exception("@LOGCAJA: No encuentro registro de factura original !!! Factura:"+iNofactura+";Partida:"+sPartida+";Sucursal:"+sCodSuc + "  ");					
					registrado = false;
				}/*else{
					cn.commit();
				}*/
			}catch(Exception ex){
				registrado = false;
				System.out.println("Se capturo una excepcion en ReciboCtrl.aplicarMontoF0311: " + ex);
				error = new Exception("@LOGCAJA: No se pudo aplicar el monto a factura!!!nofactura:"+iNofactura+";Partida:"+sPartida+";Sucursal:"+sCodSuc + "  ");
				errorDetalle = ex;
			}finally {
				try {
					ps.close();
					//cn.close();
				} catch (Exception se2) {
					System.out.println("ERROR: Failed to close connection en ReciboCtrl.aplicarMontoF0311: " + se2);
				}
			}
			return registrado;
		}
		
		/***MODIFICAR FACTURA EN EL F0311***/
		public boolean aplicarMontoF0311WithSession(Session s,String sEstadoPago,double dMontopendienteDom,double dMontoPendienteForaneo,String sUsuario,
										String sCodSuc, String sTipoFactura,int iNofactura, String sPartida, int iCodclie){
			boolean registrado = true;
			String sql  = "";			
			Date dHora = new Date();
			SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Divisas d = new Divisas();
			try{
				Calendar cFecha = Calendar.getInstance();
				CalendarToJulian fecha = new CalendarToJulian(cFecha);
				int iFecha = fecha.getDate();
				//obtener hora en enteros
				String[] sHora = (dfHora.format(dHora)).split(":");
				int iHora = Integer.parseInt(sHora[0] + sHora[1] + sHora[2]);
				//obtener partes de la fecha
				Date dFecha = new Date();
				String[] sFecha = (sdf.format(dFecha)).split("/");
				sql = "UPDATE "+PropertiesSystem.JDEDTA+".F0311 SET RPPST = '"+ sEstadoPago +"', RPAAP = " + d.pasarAentero(dMontopendienteDom) + ",RPFAP = " + d.pasarAentero(dMontoPendienteForaneo) + "," + 
						"RPUSER = '" + sUsuario +"', RPUPMJ = " + iFecha + ", RPUPMT = " + iHora + " WHERE RPKCO = '"+ sCodSuc + "' AND RPDCT ='" + sTipoFactura + "'" +
								" AND RPDOC = " + iNofactura + " AND RPSFX = '" + sPartida + "' AND TRIM(RPDCTM) = '' AND RPDOCM = 0 AND RPAN8 = "+iCodclie;
				ConsolidadoDepositosBcoCtrl.executeSqlQueryTx(s, sql);
			}catch(Exception ex){
				registrado = false;
				System.out.println("Se capturo una excepcion en ReciboCtrl.aplicarMontoF0311: " + ex);
				error = new Exception("@LOGCAJA: No se pudo aplicar el monto a factura!!!nofactura:"+iNofactura+";Partida:"+sPartida+";Sucursal:"+sCodSuc + "  ");
				errorDetalle = ex;
			}
			return registrado;
		}
		
	public boolean aplicarMontoF0311_fdc(Connection cn, double dMonto, String sMoneda, String sMonedaBase, String sUsuario, 
			                             String sCodSuc, String sTipoFactura, int iNofactura, String sPartida, int iCodclie) {
		boolean lExito = false;
		String sql = "", sTmp;
		PreparedStatement ps = null;
		Date dHora = new Date();
		SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Divisas d = new Divisas();
		int iRegs = 0, iPendiente = 0;
		
		try {
			Calendar cFecha = Calendar.getInstance();
			CalendarToJulian fecha = new CalendarToJulian(cFecha);
			int iFecha = fecha.getDate();
			// obtener hora en enteros
			String[] sHora = (dfHora.format(dHora)).split(":");
			int iHora = Integer.parseInt(sHora[0] + sHora[1] + sHora[2]);
			// obtener partes de la fecha
			Date dFecha = new Date();
			//String[] sFecha = (sdf.format(dFecha)).split("/");
			
			if (sMoneda.equals(sMonedaBase)) {
				sTmp = " RPAAP = RPAAP - ";
			} else {
				sTmp = " RPFAP = RPFAP - ";
			}
			
			sql = "UPDATE "+PropertiesSystem.JDEDTA+".F0311 SET " + sTmp + d.pasarAentero(dMonto) + ", " + "RPUSER = '" + sUsuario + "', RPUPMJ = " + iFecha
			                                    + ", RPUPMT = " + iHora + " WHERE RPKCO = '" + sCodSuc + "' AND RPDCT ='" + sTipoFactura 
			                                    + "'" + " AND RPDOC = "	+ iNofactura + " AND RPSFX = '" + sPartida 
			                                    + "' AND TRIM(RPDCTM) = '' AND RPDOCM = 0 AND RPAN8 = "	+ iCodclie;
							
			ps = cn.prepareStatement(sql);
			iRegs = ps.executeUpdate();
			if (iRegs == 1) {				
				if (sMoneda.equals(sMonedaBase)) {
					sTmp = "RPAAP";
				} else {
					sTmp = "RPFAP";
				}
				
				sql = "SELECT " + sTmp + " FROM "+PropertiesSystem.JDEDTA+".F0311 WHERE RPKCO = '" + sCodSuc + "' AND RPDCT ='" + sTipoFactura 
								+ "'" + " AND RPDOC = " + iNofactura + " AND RPSFX = '" + sPartida 
								+ "' AND TRIM(RPDCTM) = '' AND RPDOCM = 0 AND RPAN8 = " + iCodclie;
				
				ps = cn.prepareStatement(sql);
				ResultSet rsPendiente = ps.executeQuery();
				if(rsPendiente.next()){
					iPendiente = rsPendiente.getInt(sTmp);
					if (iPendiente > 0 || iPendiente < 0) {
						sql = "UPDATE "+PropertiesSystem.JDEDTA+".F0311 SET RPPST = 'A' WHERE RPKCO = '" + sCodSuc + "' AND RPDCT ='" + sTipoFactura 
							+ "'" + " AND RPDOC = " + iNofactura + " AND RPSFX = '" + sPartida 
							+ "' AND TRIM(RPDCTM) = '' AND RPDOCM = 0 AND RPAN8 = " + iCodclie;
					} else {
						sql = "UPDATE "+PropertiesSystem.JDEDTA+".F0311 SET RPPST = 'P' WHERE RPKCO = '" + sCodSuc + "' AND RPDCT ='" + sTipoFactura 
							+ "'" + " AND RPDOC = " + iNofactura + " AND RPSFX = '" + sPartida 
							+ "' AND TRIM(RPDCTM) = '' AND RPDOCM = 0 AND RPAN8 = " + iCodclie;
					}
					ps = cn.prepareStatement(sql);
					iRegs = ps.executeUpdate();
					if (iRegs == 1) {
						lExito = true;
					}
				}
			} else {
				error = new Exception("@LOGCAJA: No Encuentro Factura. No Puedo Aplicar Monto. Factura: "+ iNofactura + ", Partida: " + sPartida + ", Sucursal: " + sCodSuc + " ");
			}
		} catch (Exception ex) {
			System.out.println("Se capturo una excepcion en ReciboCtrl.aplicarMontoF0311: " + ex);
			error = new Exception("@LOGCAJA: No se pudo aplicar el monto a factura!!!nofactura: "
							+ iNofactura + ";Partida:" + sPartida + ";Sucursal:" + sCodSuc + "  ");
			errorDetalle = ex;
		} finally {
			try {
				ps.close();
			} catch (Exception se2) {
				System.out.println("ERROR: Failed to close connection en ReciboCtrl.aplicarMontoF0311: " + se2);
			}
		}
		return lExito;
	}

/*********lee el monto aplicado en un recibo en f0311**********************************/
		public double[] getMontoAplicadoF0311(Connection cn, int iNofactura,String sTipoDocumento, int iNoReciboJDE, String sNopartida,int iCodcli){
			String sql  = "SELECT sum(rpag*-1)  as cpagado,"+
							"sum(rpacr*-1 ) as dpagado " +
							"FROM "+PropertiesSystem.JDEDTA+".F0311 " +
							"WHERE RPDOC = "+iNofactura + " AND RPDCT = '"+sTipoDocumento+"' AND RPDOCM = "+iNoReciboJDE+" and rpdctm = 'RC' and rpsfx = '"+sNopartida+"' and rpan8 = " + iCodcli;
			double[] dResultado = new double[2];
			PreparedStatement ps = null;
			try{
				ps = cn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();
				if(rs.next()){
					dResultado[0] = pasarEnteroADouble(rs.getInt("CPAGADO"));
					dResultado[1] = pasarEnteroADouble(rs.getInt("DPAGADO"));
				}
			}catch(Exception ex){
				System.out.println("Se capturo una excepcion en ReciboCtrl.getMontoAplicadoF0311: " + ex);
			}finally {
				try {
					ps.close();
					//cn.close();
				} catch (Exception se2) {
					System.out.println("ERROR: Failed to close connection en ReciboCtrl.getMontoAplicadoF0311:  " + se2);
				}
			}
			return dResultado;
		}
/******borrar RC del f0311***********************************************************/
		public boolean borrarRC(Connection cn,int iNofactura, String sTipofactura, int iNoReciboJDE, String sPartida,int iCodcli){
			boolean bBorrado = true;
			
			PreparedStatement ps = null;
			
			try{
			
				
				String sql = " from "+PropertiesSystem.JDEDTA+".F0311 " +
						"WHERE RPDOC = "+iNofactura+" AND RPDCT = '"+sTipofactura+"' " +
						"AND RPDOCM ="+ iNoReciboJDE +" and rpsfx = '"+sPartida+"' " +
						"and rpan8 = " + iCodcli;
				
				int cantrows = 0;
				String strExecute =  "select count(*) cantrows " + sql  ;
				
				ps = cn.prepareStatement(strExecute);
				ResultSet rs = ps.executeQuery();
				if (rs.next()){
					cantrows = rs.getInt("cantrows");	
				}
				ps.close();
				
				if( cantrows == 0 ) {
					return bBorrado = true;
				}
				
				 
				strExecute =  " delete " + sql  ;
				
				ps = cn.prepareStatement(strExecute);
				int rowsAffected = ps.executeUpdate();
				ps.close();
				
				bBorrado = rowsAffected != 0 ;

				
			}catch(Exception ex){
				ex.printStackTrace(); 
			}
			return bBorrado;
		}
/****************************VALIDAR EXISTENCIA DE NUMERO RECIBO MANUAL*********************************************/
		public boolean verificarNumeroRecibo(int iCaId,String sCodComp, int iNumRecm, String sCodsuc){
			Session session = HibernateUtilPruebaCn.currentSession();
			
			boolean existe = false;	
			try{
				
				List result = (List) session
				.createQuery("from Recibo r where r.id.codcomp = '"+sCodComp+"' and r.id.caid = "+iCaId+  " and r.id.codsuc = '"+ sCodsuc+"' and r.numrecm = "+iNumRecm)
				.list();
				
				if(!result.isEmpty()){
					existe = true;
				}
			}catch(Exception ex){
				System.out.print("Se capturo una excepcion en ReciboPrimaCtrl.verificarNumeroRecibo: " + ex);
			}	
			return existe;
		}
/********************************************************************************************************************/
		/**INSERTAR RM EN EL F0311 DEL JDE***********************************************************************************/
		public boolean insertarRM(Connection cn, Hfactura fh, String sTipodoc,int iNodoc, int iFecha,int iNobatch,BigDecimal bdTasa,
									String sUsuario,String sAplicacion,int iCaja,Vf0101 vf0101, String sMonedaBase,String iNodoco, String sTipodoco){
			boolean bHecho = true;
			String sql = "INSERT INTO "+PropertiesSystem.JDEDTA+".F0311(RPKCO,RPAN8,RPDCT,RPDOC,RPSFX,RPDIVJ,RPDGJ,RPFY,RPCTRY,RPPN,RPCO,RPICUT,RPICU,RPDICJ,RPPA8," +
						"RPAN8J,RPBALJ,RPPST,RPAG,RPAAP,RPATXA,RPSTAM,RPCRRM,RPCRCD,RPCRR,RPACR,RPFAP,RPCTXA,RPCTAM,RPTXA1,RPEXR1,RPDSVJ,RPGLC,RPMCU,RPCM,RPDDJ,RPDDNJ," +
						"RPRMK,RPALPH,RPAC01,RPAC02,RPAC03,RPAC04,RPAC05,RPAC06,RPAC07,RPAC08,RPAC09,RPAC10,RPATE,RPATR,RPATP,RPATPR," +
						"RPAT1,RPAT2,RPAT3,RPAT4,RPAT5,RPTORG,RPUSER,RPPID,RPUPMJ,RPUPMT,RPJOBN,RPPKCO,RPPO,RPDCTO) VALUES(";
			Date dHora = new Date();
			SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			String sNombrePc = null;
			EmpleadoCtrl empCtrl = new EmpleadoCtrl();
			String sConcepto = "Dev:",sCodunineg="", sNocuota = "001", sCadValida = "", sNomcli = "";
			Divisas d = new Divisas();
			BigDecimal bdMontoD = new BigDecimal(0);	
			PreparedStatement ps = null;
			try{
				sNombrePc = "SERVER";//InetAddress.getLocalHost().getHostName();
				//obtener hora en enteros
				String[] sHora = (dfHora.format(dHora)).split(":");
				int iHora = Integer.parseInt(sHora[0] + sHora[1] + sHora[2]);
				//obtener partes de la fecha
				Date dFecha = new Date();
				String[] sFecha = (sdf.format(dFecha)).split("/");
				//agregar blancos a unineg
				sCodunineg = fh.getCodunineg().trim();
				if(sCodunineg.length() == 2){
					sCodunineg = "          "+sCodunineg;
				}else{
					sCodunineg = "        "+sCodunineg;
				}
				sConcepto = sConcepto+fh.getNofactura()+" " + fh.getTipofactura() + " C:" + iCaja;
				if(sConcepto.length() > 30){
					sConcepto = sConcepto.substring(0,29);
				}
				if(sNombrePc.length() > 9){
					sNombrePc =	sNombrePc.substring(0, 9);
				}
				
				//validar comilla  simple 
				sCadValida = d.remplazaCaractEspeciales(vf0101.getId().getAbalph().trim(), "'", "-");
				if(!sCadValida.equals("")){
					sNomcli = sCadValida;
				}
								
				sql = sql + 
					  "'000" +fh.getCodsuc()+"'," + 
					  vf0101.getId().getAban8() +"," +
					  "'" +sTipodoc+"'," +
					  iNodoc +"," +
					  "'" +sNocuota+"'," +
					  iFecha +"," +
					  iFecha +"," +
					  Integer.parseInt(sFecha[2].substring(2, 4))+"," +
					  "20," +
					  ""+Integer.parseInt(sFecha[1])+"," +
					  "'000" +fh.getCodsuc()+"'," + 
					  "'I'," +
					  iNobatch +"," +
					  iFecha +"," +
					  vf0101.getId().getAban8()  +"," +
					  vf0101.getId().getAban8()  +"," +
					  "'Y'," +
					  "'A',"; 
					  if(fh.getMoneda().equals(sMonedaBase)){		
						  bdMontoD = new BigDecimal((fh.getTotal())*-1);
						  sql = sql + 
						  d.pasarAentero(bdMontoD.doubleValue()) + "," +
						  d.pasarAentero(bdMontoD.doubleValue()) + "," +
						  0 + "," +
						  0 + "," +
						  "'D'," + 
						  "'"+sMonedaBase+"'," + 
						  0 + "," +
						  0 + "," +	
						  0 + "," +
						  0 + "," +
						  0 + "," ;	
					  }else{
						  bdMontoD = new BigDecimal((fh.getTotal())*-1);
						  sql = sql + 
						  d.pasarAentero(d.roundDouble((bdMontoD.multiply(bdTasa)).doubleValue())) + "," +
						  d.pasarAentero(d.roundDouble((bdMontoD.multiply(bdTasa)).doubleValue())) + "," +
						  0 + "," +
						  0 + "," +
						  "'F'," + 
						  "'"+fh.getMoneda()+"'," + 
						  bdTasa + "," +
						  d.pasarAentero(bdMontoD.doubleValue()) + "," +
						  d.pasarAentero(bdMontoD.doubleValue()) + "," +
						  0 + "," +
						  0 + ",";
					  }
					  
				sql = sql + 
				    "'EXE'," + 
				    "'E'," + 
				    iFecha +"," +
			    	"'"+vf0101.getId().getA5arc()+"'," +
			    	"'"+sCodunineg+"'," +
			    	"'"+vf0101.getId().getAbcm()+"'," +
			    	iFecha + "," +
			    	iFecha + "," +
			    	"'"+sConcepto+"'," +
			    	"'"+sNomcli+"'," +
			    	"'" + vf0101.getId().getAbac01() + "'," + "'" + vf0101.getId().getAbac02() + "'," + "'" + vf0101.getId().getAbac03() + "'," + "'" + vf0101.getId().getAbac04() + "'," +
					"'" + vf0101.getId().getAbac05() + "'," + "'" + vf0101.getId().getAbac06() + "'," + "'" + vf0101.getId().getAbac07() + "'," + "'" + vf0101.getId().getAbac08() + "'," +
					"'" + vf0101.getId().getAbac09() + "'," + "'" + vf0101.getId().getAbac10() + "'," +
					"'" + vf0101.getId().getAbate() + "'," + "'" + vf0101.getId().getAbatr() + "'," +
					"'" + vf0101.getId().getAbatp() + "','" + vf0101.getId().getAbatpr() + "'," +
					"'" + vf0101.getId().getAbat1() + "'," +
					"'" + vf0101.getId().getAbat2() + "'," + "'" + vf0101.getId().getAbat3() + "'," + "'" + vf0101.getId().getAbat4() + "'," + "'" + vf0101.getId().getAbat5() + "'," +
					"'" + sUsuario + "'," + "'" + sUsuario + "'," + "'"+sAplicacion+"'," + iFecha + "," + iHora + "," +"'"+sNombrePc + "','000" +fh.getCodsuc()+"'" +
					", '"+iNodoco+"', '"+sTipodoco+"')";
				
				    ps = cn.prepareStatement(sql);
					int rs = ps.executeUpdate();
					if (rs != 1){
						bHecho = false;
					}
			}catch(Exception ex){
				bHecho = false;
				System.out.println("Se capturo una excepcion en CuotaCtrl.insertarMF: " + ex);
			}
			return bHecho;
		}

/****************************************************************************************/
/** Método: Guardar Registro en el GCPPRRDTA.F0911 para RM, en módulo de Financimiento.
 * 			(Variante del método RegistrarBatch de JC)
 *	Fecha:  16/08/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 **/
	public boolean insertarAsientoRM(Connection cn,Hfactura fh, String  sTipodoc,int iNodoc,
									 int iFecha,int iNobatch,BigDecimal bdTasa,String sUsuario,String sAplicacion,
									 String[] sCuenta, String sTipolibro,int iMonto, int iCaja,Vf0101 c,
									 int iNolinea,String sGlbcrc,String sGlhco, String sGlcrrm){
		boolean bHecho = true;
		PreparedStatement ps = null;
		String sql = "INSERT INTO "+PropertiesSystem.JDEDTA+".F0911 (GLKCO,GLDCT,GLDOC,GLDGJ,GLJELN,GLICU,GLICUT,GLDICJ,GLDSYJ,GLTICU,GLCO,GLANI,GLAM,GLAID,GLMCU,GLOBJ,GLSUB,GLLT,GLPN,GLCTRY,GLFY," +
												   "GLCRCD,GLCRR,GLAA,GLGLC,GLEXA,GLEXR,GLR1,GLR2,GLAN8,GLDKJ,GLVINV,GLIVD," +
												   "GLPO,GLDCTO,GLLNID,GLDSVJ,GLTORG,GLUSER,GLPID,GLJOBN,GLUPMJ,GLUPMT,"+
												   "GLBCRC,GLHCO,GLCRRM ) VALUES(";
		Date dHora = new Date();
		SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String sNombrePc = null;	
		String sCodunineg = "";
		String sConcepto = "";
		String sNomcli = "", sCadValida = "";
		Divisas d = new Divisas();
		try{	
			sNombrePc = "SERVER";//InetAddress.getLocalHost().getHostName();
			//obtener hora en enteros
			String[] sHora = (dfHora.format(dHora)).split(":");
			int iHora = Integer.parseInt(sHora[0] + sHora[1] + sHora[2]);
			//obtener partes de la fecha
			Date dFecha = new Date();
			String[] sFecha = (sdf.format(dFecha)).split("/");
			if(sNombrePc.length() > 9){
				sNombrePc =	sNombrePc.substring(0, 9);
			}
			sCodunineg = sCuenta[3];
			if(sCodunineg.length() == 2){
				sCodunineg = "          "+sCodunineg;
				sCuenta[0] = "          "+sCuenta[0];
			}else{
				sCodunineg = "        "+sCodunineg;
				sCuenta[0] = "        "+sCuenta[0];
			}
			sConcepto = sConcepto+fh.getNofactura()+" " + fh.getTipofactura() + " C:" + iCaja;
			
			sNomcli = c.getId().getAbalph();
			if(sNomcli.length() > 29){
				sNomcli = sNomcli.substring(0, 29);
			}
			//validar comilla  simple 
			sCadValida = d.remplazaCaractEspeciales(sNomcli, "'", "-");
			if(!sCadValida.equals("")){
				sNomcli = sCadValida;
			}
			
			sql = sql +
			"'000"+fh.getCodsuc()+"'," +
			"'"+sTipodoc+"'," +
			""+iNodoc+"," +
			""+iFecha+"," +
			""+((iNolinea)*1.0)+"," +
			""+iNobatch+"," +
			"'I'," +
			""+iFecha+"," +
			""+iFecha+"," +
			""+iHora+"," +
			"'000"+sCuenta[2]+"'," +
			"'"+sCuenta[0]+"'," +
			"'2'," +
			"'"+sCuenta[1]+"'," +
			"'"+sCodunineg+"'," +
			"'"+sCuenta[4]+"'," +
			"'"+sCuenta[5]+"'," +
			"'"+sTipolibro+"'," +		
			""+Integer.parseInt(sFecha[1])+"," +
			"20," +				
			Integer.parseInt(sFecha[2].substring(2, 4))+"," +
			"'"+fh.getMoneda()+"'," +
			bdTasa + "," +
			iMonto + "," +
			"''," +
			"'"+sNomcli+"'," +
			"'"+sConcepto+"'," +
			"''," +
			"''," +
			c.getId().getAban8() + "," +
			""+iFecha+"," +
			"''," +
			""+0+"," +
			"''," +
			"''," +
			"0," +
			""+iFecha+"," +
			"'"+sUsuario+"'," +
			"'"+sUsuario+"'," +
			"'"+sAplicacion+"'," +
			"'"+sAplicacion+"'," +
			""+iFecha+"," +
			""+iHora+"," +
			"'"+sGlbcrc+"'," +
			"'000"+sGlhco+"'," +
			"'"+sGlcrrm+"'" +
			")";
			
			ps = cn.prepareStatement(sql);
			int rs = ps.executeUpdate();
			if (rs != 1){
				bHecho = false;
			}
		}catch(Exception ex){
			bHecho = false;
			System.out.println("Se capturo una excepcion en CuotaCtrl.insertarAsientoMF: " + ex);
		}
		return bHecho;
	}
/*********************************************************************************************************************/	
/**Insertar Asiento de RM******************************************************/
		public boolean insertarAsientoRM1(Connection cn,Hfactura fh,Dfactura fd,String  sTipodoc,int iNodoc,int iFecha,int iNobatch,BigDecimal bdTasa,String sUsuario,String sAplicacion,String[] sCuenta,
											String sTipolibro,int iMonto, int iCaja,Vf0101 c,int iNolinea){
			boolean bHecho = true;
			PreparedStatement ps = null;
			String sql = "INSERT INTO "+PropertiesSystem.JDEDTA+".F0911 (GLKCO,GLDCT,GLDOC,GLDGJ,GLJELN,GLICU,GLICUT,GLDICJ,GLDSYJ,GLTICU,GLCO,GLANI,GLAM,GLAID,GLMCU,GLOBJ,GLSUB,GLLT,GLPN,GLCTRY,GLFY," +
													   "GLCRCD,GLCRR,GLAA,GLGLC,GLEXA,GLEXR,GLR1,GLR2,GLAN8,GLDKJ,GLVINV,GLIVD," +
													   "GLPO,GLDCTO,GLLNID,GLDSVJ,GLTORG,GLUSER,GLPID,GLJOBN,GLUPMJ,GLUPMT) VALUES(";
			Date dHora = new Date();
			SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			String sNombrePc = null;	
			String sCodunineg = "";
			String sConcepto = "";
			String sNomcli = "";
			try{	
				sNombrePc = "SERVER";//InetAddress.getLocalHost().getHostName();
				//obtener hora en enteros
				String[] sHora = (dfHora.format(dHora)).split(":");
				int iHora = Integer.parseInt(sHora[0] + sHora[1] + sHora[2]);
				//obtener partes de la fecha
				Date dFecha = new Date();
				String[] sFecha = (sdf.format(dFecha)).split("/");
				if(sNombrePc.length() > 9){
					sNombrePc =	sNombrePc.substring(0, 9);
				}
				sCodunineg = sCuenta[3];
				if(sCodunineg.length() == 2){
					sCodunineg = "          "+sCodunineg;
					sCuenta[0] = "          "+sCuenta[0];
				}else{
					sCodunineg = "        "+sCodunineg;
					sCuenta[0] = "        "+sCuenta[0];
				}
				sConcepto = sConcepto+fh.getNofactura()+" " + fd.getTipofactura() + " C:" + iCaja;
				
				sNomcli = c.getId().getAbalph();
				if(sNomcli.length() > 29){
					sNomcli = sNomcli.substring(0, 29);
				}
				
				sql = sql +
				"'000"+fh.getCodsuc()+"'," +
				"'"+sTipodoc+"'," +
				""+iNodoc+"," +
				""+iFecha+"," +
				""+((iNolinea)*1.0)+"," +
				""+iNobatch+"," +
				"'I'," +
				""+iFecha+"," +
				""+iFecha+"," +
				""+iHora+"," +
				"'000"+sCuenta[2]+"'," +
				"'"+sCuenta[0]+"'," +
				"'2'," +
				"'"+sCuenta[1]+"'," +
				"'"+sCodunineg+"'," +
				"'"+sCuenta[4]+"'," +
				"'"+sCuenta[5]+"'," +
				"'"+sTipolibro+"'," +		
				""+Integer.parseInt(sFecha[1])+"," +
				"20," +				
				Integer.parseInt(sFecha[2].substring(2, 4))+"," +
				"'"+fh.getMoneda()+"'," +
				bdTasa + "," +
				iMonto + "," +
				"''," +
				"'"+sNomcli+"'," +
				"'"+sConcepto+"'," +
				"''," +
				"''," +
				c.getId().getAban8() + "," +
				""+iFecha+"," +
				"''," +
				""+0+"," +
				"''," +
				"''," +
				"0," +
				""+iFecha+"," +
				"'"+sUsuario+"'," +
				"'"+sUsuario+"'," +
				"'"+sAplicacion+"'," +
				"'"+sAplicacion+"'," +
				""+iFecha+"," +
				""+iHora+")";
				
				ps = cn.prepareStatement(sql);
				int rs = ps.executeUpdate();
				if (rs != 1){
					bHecho = false;
				}
			}catch(Exception ex){
				bHecho = false;
			
			}
			return bHecho;
		}
		
		//Byte
		public boolean registrarAsientoDiario(Date dtFechaAsiento,Connection cn,String sCodSuc,String sTipodoc,int iNoDocumento,
				  double dLineaJDE, int iNoBatch, String sCuenta, String sIdCuenta, String sCodUnineg,
				  String sCuentaObj,String sCuentaSub,String sTipoAsiento,String sMoneda, long iMonto, 
				  String sConcepto,String sUsuario,String sCodApp, BigDecimal dTasa, String sTipoCliente, 
				  String sObservacion,String sCodSucCuenta,String sGlsbl,String sGlsblt,
				  String sGlbcrc,String sGlhco, String sGlcrrm){
						boolean bRegistrado = true;
						String sNombrePc = null;
						String sql = "";
						PreparedStatement ps = null;
						Date dHora = new Date();
						SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
						try{
						Calendar cFecha = Calendar.getInstance();
						cFecha.setTime(dtFechaAsiento);
						CalendarToJulian fecha = new CalendarToJulian(cFecha);
						int iFecha = fecha.getDate();
						
						sNombrePc = "SERVER";//InetAddress.getLocalHost().getHostName();
						//obtener hora en enteros
						String[] sHora = (dfHora.format(dHora)).split(":");
						int iHora = Integer.parseInt(sHora[0] + sHora[1] + sHora[2]);
						//obtener partes de la fecha
						String[] sFecha = (sdf.format(dtFechaAsiento)).split("/");
						
						
						sCodUnineg = com.casapellas.util.CodeUtil.pad(sCodUnineg.trim(), 12," ");
						
						/*
						if(sCodUnineg.length() == 2){
						sCodUnineg = "          "+sCodUnineg;
						sCuenta = "          "+sCuenta;
						}else{
						sCodUnineg = "        "+sCodUnineg;
						sCuenta = "        "+sCuenta;
						}
						*/
						
						if(sCuenta == null || sCuenta.trim().isEmpty() ) {
						sCuenta = sCodUnineg + "." + sCuentaObj + ( sCuentaSub.trim().isEmpty() ? "" : "."  + sCuentaSub ) ;
						}
						
						if(sConcepto.length() > 30){
						sConcepto = sConcepto.substring(0,29);
						}
						if(sTipoCliente.compareTo( "E" ) == 0 ){
						sTipoCliente = "EMP";
						}else{
						sTipoCliente = "";
						}
						if(sNombrePc.length() > 9){
						sNombrePc =	sNombrePc.substring(0, 9);
						}
						if(sObservacion.length() > 30){
						sObservacion = sObservacion.substring(0,29);
						}
						
						String strNumDocJde = Integer.toString( iNoDocumento ); 
						if( strNumDocJde.length() > 8){
						strNumDocJde = strNumDocJde.substring(strNumDocJde.length() - 8, strNumDocJde.length() );
						iNoDocumento = Integer.parseInt(strNumDocJde);
						}
						
						sql =  " INSERT INTO "+PropertiesSystem.JDEDTA+".F0911 (GLKCO,GLDCT,GLDOC,GLDGJ,GLJELN,GLICU,GLICUT,GLDICJ,";
						sql += " GLDSYJ,GLTICU,GLCO,GLANI,GLAM,GLAID,GLMCU,GLOBJ,GLSUB, GLSBL,GLSBLT, GLLT,GLPN,";
						sql += " GLCTRY,GLFY,GLCRCD,GLAA,GLEXA,GLDKJ,GLDSVJ,GLTORG,GLUSER,GLPID,GLJOBN,GLUPMJ,GLUPMT,";
						sql += " GLCRR,GLGLC,GLEXR,GLBCRC,GLHCO,GLCRRM) VALUES(" ;
						
						
						sCodSuc = CodeUtil.pad( sCodSuc, 5, "0"); 
						sCodSucCuenta = CodeUtil.pad( sCodSucCuenta, 5, "0"); 
						sGlhco = CodeUtil.pad( sGlhco, 5, "0"); 
						
						sql+=
						"'"+sCodSuc+"'," +			
						"'"+sTipodoc +"'," +
						""+iNoDocumento+"," +
						""+iFecha+"," +
						""+dLineaJDE+"," +
						""+iNoBatch+"," +
						"'G'," +
						""+iFecha+"," +
						""+iFecha+"," +
						""+iHora+"," +
						
						"'"+sCodSucCuenta+"'," +
						
						"'"+sCuenta+"'," +
						"'2'," +
						"'"+sIdCuenta+"'," +
						"'"+sCodUnineg+"'," +
						"'"+sCuentaObj+"'," +
						"'"+sCuentaSub+"'," +
						"'"+sGlsbl+"',"+
						"'"+sGlsblt+"',"+
						"'"+sTipoAsiento+"'," +
						""+Integer.parseInt(sFecha[1])+"," +
						"20," +
						""+Integer.parseInt(sFecha[2].substring(2, 4))+"," +
						"'"+sMoneda+"'," +
						""+iMonto+"," +
						"'"+sConcepto+"'," +
						""+iFecha+"," +
						""+iFecha+"," +
						"'"+sUsuario+"'," +
						"'"+sUsuario+"'," +
						"'"+sCodApp+"'," +
						"'"+sNombrePc+"'," +
						""+iFecha+"," +
						""+iHora+"," +
						""+dTasa.setScale(2, RoundingMode.HALF_UP).toString() +"," +
						"'"+sTipoCliente+"'," +
						"'"+sObservacion+"'," +
						"'"+sGlbcrc+"'," +
						
						"'"+sGlhco+"'," +
						
						"'"+sGlcrrm+"'" +
						")";
						ps = cn.prepareStatement(sql);			
						int rs = ps.executeUpdate();
						if (rs != 1){
						bRegistrado = false;
						}
						}catch(Exception ex){
						
						bRegistrado = false;
						errorDetalle = ex;
						error = new Exception("@No pudo grabarse Batch en JDE, favor intente de nuevo");
						
						if( ex.toString().trim().toLowerCase().contains("duplicatekeyexception") ||
						ex.toString().trim().toLowerCase().contains("sql0803") || 
						ex.toString().trim().toLowerCase().contains("SQLIntegrityConstraintViolationException")){
						error = new Exception("@Ya existe en JDE un Batch con el numero de referencia: "
						+iNoDocumento +" Asigne un nuevo número e intente nuevamente ");
						}
						
						String invocado =  new Exception().getStackTrace()[1].getClassName() +":"+ new Exception().getStackTrace()[1].getMethodName() ;
						ex.printStackTrace();
						
						}finally {
						try {
						ps.close();
						
						} catch (Exception se2) {
						se2.printStackTrace();
						
						}
						}
		return bRegistrado;
	}
		public boolean registrarReciboNoSession(
				int iNumRec, int iNumRecm, String sCodComp, double dMontoAplicar,
				double dMontoRec, double dCambio, String sConcepto, Date dFecha,
				Date dHora, int iCodCli, String sNomCli, String sCajero, int iCaId,
				String sCodSuc, String sCodUser, String sTipoRecibo, int iNodoco,
				String sTipodoco, int iRecjde, Date dtFechaM, String sCodunineg,
				String sMotivoCT, String monedaapl,Session s) {
						
			SimpleDateFormat date = new SimpleDateFormat("MM/dd/yyyy");  
			SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");  
	String fecha= date.format(dFecha);
			
			String hora = time.format(dHora);
			try {  
			     String strSqlQuery = "INSERT INTO "+PropertiesSystem.ESQUEMA+".RECIBO(NUMREC,MONTOAPL,MONTOREC,CONCEPTO,TIPOREC," + 
			     		"FECHA,CLIENTE,CODCLI,CAJERO,CODCOMP,HORA,NUMRECM,RECJDE,CAID," + 
			     		"CODSUC,ESTADO,MOTIVO,CODUSERA,HORAMOD,CODUSER,NODOCO,TIPODOCO,FECHAM,CODUNINEG," + 
			     		"MOTIVOCT,MONEDAAPL) VALUES(" + 
			     		""+iNumRec+","+dMontoAplicar+","+dMontoRec +",'"+ sConcepto+"','"+sTipoRecibo+"','"+
			     		fecha+"','"+ sNomCli+"',"+iCodCli+",'"+sCajero+"','"+sCodComp+"','"+hora+"',"+iNumRecm+","+iRecjde+","+iCaId+",'"+
			     		sCodSuc+"',"+"'A'"+","+"'Pago BYTE'"+",'"+"'"+",'"+hora+"','"+sCodUser+"','"+iNodoco+"','"+sTipodoco+"','"+fecha+"','"+sCodunineg+
			     		"','"+sMotivoCT+"','"+monedaapl+"') ";
			     
			   ConsolidadoDepositosBcoCtrl.executeSqlQueryTx(s, strSqlQuery);
				
			} catch (Exception ex) { 
				LogCajaService.CreateLog("registrarReciboNoSession", "ERR", ex.getMessage());
			
			}
			return true;
		}
		
		public boolean registrarDetalleReciboNoSession(
				int iNumrec, int iNumrecm, String codcomp, List lstMetodosPago,
				int iCaId, String sCodSuc, String sTpoRec,Session s) {
			boolean registrado = false;
			Divisas divisas = new Divisas();
		
			MetodosPago mPago = null;
			try {
				
				for (int i = 0; i < lstMetodosPago.size(); i++) {
					MetodosPago mPagoTmp = (MetodosPago) lstMetodosPago.get(i);
					
					if( mPagoTmp.getMetodo().compareTo(MetodosPagoCtrl.TRANSFERENCIA)  == 0 ){
						if( mPagoTmp.getReferencia3().trim().length() > 8 )
							 mPagoTmp.setReferencia3(  mPagoTmp.getReferencia3().substring(0, 7) ) ;
					}
					if( mPagoTmp.getMetodo().compareTo(MetodosPagoCtrl.DEPOSITO)  == 0 ){
						if( mPagoTmp.getReferencia2().trim().length() > 8 )
							 mPagoTmp.setReferencia2(  mPagoTmp.getReferencia2().substring(0, 7) ) ;
					}
					
				}
				
				for (int i = 0; i < lstMetodosPago.size(); i++) {
					mPago = (MetodosPago) lstMetodosPago.get(i);
					Recibodet recibodet = new Recibodet();
					RecibodetId recibodetid = new RecibodetId();

					recibodetid.setNumrec(iNumrec);
					recibodetid.setCaid(iCaId);
					recibodetid.setCodsuc(sCodSuc);
					recibodetid.setCodcomp(codcomp);
					recibodetid.setMoneda(mPago.getMoneda());
					recibodetid.setMpago(mPago.getMetodo());
					recibodetid.setRefer1(mPago.getReferencia());
					recibodetid.setRefer2(mPago.getReferencia2());
					recibodetid.setRefer3(mPago.getReferencia3());
					recibodetid.setRefer4(mPago.getReferencia4());

					recibodet.setRefer5("");
					recibodet.setRefer6("");
					recibodet.setRefer7("");
					recibodet.setNombre("");

					if (mPago.getVmanual() != null
							&& mPago.getVmanual().compareTo("2") == 0) {

						recibodet.setRefer5(mPago.getReferencia5());
						recibodet.setRefer6(mPago.getReferencia6());
						recibodet.setRefer7(mPago.getReferencia7());
						recibodet.setNombre(mPago.getNombre());
					}

					recibodetid.setTiporec(sTpoRec);
					recibodet.setId(recibodetid);

					recibodet.setNumrecm(iNumrecm);
					recibodet.setTasa(BigDecimal.valueOf((divisas.formatStringToDouble(mPago.getTasa().toString()))));
					recibodet.setMonto(BigDecimal.valueOf((mPago.getMonto())));
					recibodet.setEquiv(BigDecimal.valueOf((mPago.getEquivalente())));
					recibodet.setVmanual((mPago.getVmanual() == null) ? "0" : mPago.getVmanual());
					recibodet.setCaidpos((mPago.getICaidpos() == 0) ? iCaId : mPago.getICaidpos());

					recibodet.setMarcatarjeta( mPago.getMarcatarjeta().toLowerCase() );
					recibodet.setCodigomarcatarjeta ( mPago.getCodigomarcatarjeta().toLowerCase()  );
					
					int clasificapormarca = 0 ;
					if(mPago.getMetodo().compareTo(MetodosPagoCtrl.TARJETA) == 0 ) {
						F55ca03 f03 = AfiliadoCtrl.obtenerAfiliadoxId( mPago.getReferencia() );
						if(f03 != null)
							clasificapormarca = f03.getId().getCxclasificamarca() ;
					}
					recibodet.setLiquidarpormarca(clasificapormarca);
					
					recibodet.setReferencenumber(mPago.getReferencenumber());
					recibodet.setDepctatran(mPago.getDepctatran());
					
					LogCajaService.CreateLog("registrarDetalleRecibo", "HQRY", recibodet);
					s.save(recibodet);
				
					
					registrado = true;
					
				}
			} catch (Exception ex) {
				LogCajaService.CreateLog("registrarDetalleRecibo", "ERR", ex.getMessage());
				registrado = false;
			
			}
			
			return registrado;
		}
		
		public boolean registrarReciboByte(int numRec,int caID,String company,String codSuc,long recByte,String numPrestamo,String json,Session s) throws Exception {
			
		
				boolean returnInfo = false;
			try {  
			     String strSqlQuery = "INSERT INTO "+PropertiesSystem.ESQUEMA+".RECIBOBYTE (NUMREC,CAID,CODCOMP,CODSUC,RECBYTE,NUMPRESTAMO,SOLICITUD) "
			     		+ "VALUES ("+numRec+","+caID+",'"+company+"','"+codSuc+"','"+recByte+"','"+numPrestamo+"','"+json+"') ";
			    
			     returnInfo = ConsolidadoDepositosBcoCtrl.executeSqlQueryTx(s, strSqlQuery);
			} catch (Exception ex) { 
				throw new Exception(ex.getMessage());
			} 
			
			return returnInfo;
			
		}
		
		
		public int  procesoPagoFacturaByte(List<String[]> noFacturas, int caid,
				String codcomp, int fecha, int codcli, String tiporec,int numRec,Session s) {
		
			int iNumrecTmp = 0;		
			try {			
				String sql = "";	
			
				//&& ======== grabar los registros en recibofac con un numero de recibo aleatorio.
				iNumrecTmp = Integer.parseInt(String.valueOf(Math
						.round(10000000 + Math.random() * 90000000)));

				Date timeTmp = new Date();
				SimpleDateFormat sdfx = new SimpleDateFormat("HHmmss");
				String hora = sdfx.format(timeTmp);
				
				int i = 0;
				for (String[] df : noFacturas) {
					

					String strSqlQuery = "INSERT INTO "+PropertiesSystem.ESQUEMA+".Recibofac(NUMREC,NUMFAC,ORDEN,CODCOMP,PARTIDA,MONTO,TIPOFACTURA,CODSUC,CAID,ESTADO,CODUNINEG,TIPOREC,CODCLI,FECHA,HORA) "
				     		+ "VALUES ("+numRec+","+Integer.parseInt( df[0]) +","+i+",'"+codcomp+"','"+df[2].trim()+"',"+df[1] +",'"+df[5]+"','"+CodeUtil.pad( df[4].trim(), 5, "0")+"',"+caid
				     		+",'','"+df[3].trim()+"','"+tiporec+"',"+codcli+","+fecha+","+hora+")";
					ConsolidadoDepositosBcoCtrl.executeSqlQueryTx(s, strSqlQuery);
					i++;
					try{
						LogCajaService.CreateLog("procesoPagoFactura", "HQRY", strSqlQuery);
						//sesion.save(rf);
						
					}catch(Exception e){
						e.printStackTrace();
						return iNumrecTmp = 0;
					}
				}
				
//				LogCrtl.sendLogInfo("Grabar registro en de Recibofac " 	+ FechasUtil.diferenciaEntreHoras(timeTmp, new Date())	+ " || " + parametros);
				
			} catch (Exception e) {
				
				LogCajaService.CreateLog("procesoPagoFactura", "ERR", e.getMessage());
				iNumrecTmp = 0 ;
			}
				

			return iNumrecTmp;
		}
		
		public boolean registrarReciboF0011(String strSqlQuery, Session s) {
			
		boolean returnInf = false;


			try {  
			     
				returnInf=  ConsolidadoDepositosBcoCtrl.executeSqlQueryTx(s, strSqlQuery);
				
			} catch (Exception ex) { 
				LogCajaService.CreateLog("registrarReciboF0011", "ERR", ex.getMessage());
			}
			return true;
			
		}
		
		public boolean actualizarEstadoRecibo(int numRec,int caID,String company) {
			
			PreparedStatement ps = null;


			try {  
			     String strSqlQuery = "UPDATE  "+PropertiesSystem.ESQUEMA+".RECIBO SET ESTADO = '' WHERE NUMREC = "+numRec+" AND CODCOMP = '"+company+"' AND CAID = "+caID 
			    		 + " AND TIPOREC='FN'"   ;
			 
			     ConsolidadoDepositosBcoCtrl.executeSqlQueryTx(null, strSqlQuery);
			     
			} catch (Exception ex) { 
				ex.printStackTrace(); 
			} 
			return true;
			
		}
		public boolean registrarCambioNoSession(Session session,Transaction tx,int iNumRec,String sCodComp, String sMoneda, double dCambio,int iCaId,String sCodSuc,BigDecimal bdTasa,String sTiporec){
			boolean registrado = false;
			
			try{
				
				Cambiodet cambiodet = new Cambiodet();
				CambiodetId cambiodetId = new CambiodetId();
				
				cambiodetId.setCodcomp(sCodComp);
				cambiodetId.setNumrec(iNumRec);
				cambiodetId.setMoneda(sMoneda);
				cambiodetId.setCaid(iCaId);
				cambiodetId.setCodsuc(sCodSuc);
				cambiodetId.setTiporec(sTiporec);
				
				cambiodet.setCambio(BigDecimal.valueOf(dCambio));
				cambiodet.setTasa(bdTasa);
				cambiodet.setId(cambiodetId);
				
				session.save(cambiodet);
			
				registrado = true;
			}catch(Exception ex){
				registrado = false;
				LogCajaService.CreateLog("registrarCambioNoSession", "ERR", ex.getMessage());
			}
			return registrado;
		}
		
		public String getNumeroPrestamoByte(int numRecCaja,int caID, String codComp) {
			String numeroPrestamo="";
			
			String Query="SELECT RECBYTE from "+PropertiesSystem.ESQUEMA+".RECIBOBYTE"+
			" WHERE "+
			"NUMREC="+numRecCaja+" AND CAID ="+caID+" AND CODCOMP= '"+codComp+"'";
			try {
				@SuppressWarnings("unchecked")
				List<Object> numREC = (List<Object>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(Query, true, null);
				if(numREC.size()>0)
					numeroPrestamo = numREC.get(0).toString();
			}catch(Exception e) {
				e.printStackTrace();
			}
			return numeroPrestamo;
		}
		public String getRequestPrestamoByte(int numRecCaja,int caID, String codComp) {
			String numeroPrestamo="";
			
			String Query="SELECT NUMPRESTAMO from "+PropertiesSystem.ESQUEMA+".RECIBOBYTE"+
			" WHERE "+
			"NUMREC="+numRecCaja+" AND CAID ="+caID+" AND CODCOMP= '"+codComp+"'";
			try {
				@SuppressWarnings("unchecked")
				List<Object> numREC = (List<Object>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(Query, true, null);
				if(numREC.size()>0)
					numeroPrestamo = numREC.get(0).toString();
			}catch(Exception e) {
				e.printStackTrace();
			}
			return numeroPrestamo;
		}
		
		public String getJsonPrestamoByte(int numRecCaja,int caID, String codComp) {
			String numeroPrestamo="";
			
			String Query="SELECT SOLICITUD from "+PropertiesSystem.ESQUEMA+".RECIBOBYTE"+
			" WHERE "+
			"NUMREC="+numRecCaja+" AND CAID ="+caID+" AND CODCOMP= '"+codComp+"'";
			try {
				@SuppressWarnings("unchecked")
				List<Object> numREC = (List<Object>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(Query, true, null);
				if(numREC.size()>0)
					numeroPrestamo = numREC.get(0).toString();
			}catch(Exception e) {
				e.printStackTrace();
			}
			return numeroPrestamo;
		}
}

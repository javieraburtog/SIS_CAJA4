package com.casapellas.conciliacion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.casapellas.conciliacion.entidades.ConsolidadoCoincidente;
import com.casapellas.conciliacion.entidades.PcdConsolidadoDepositosBanco;
import com.casapellas.controles.ConfirmaDepositosCtrl;
import com.casapellas.controles.ReciboCtrl;
import com.casapellas.controles.RevisionArqueoCtrl;
import com.casapellas.entidades.Deposito;
import com.casapellas.entidades.Deposito_Report;
import com.casapellas.entidades.F55ca014;
import com.casapellas.entidades.ens.Vautoriz;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.jde.creditos.CodigosJDE1;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.Divisas;
import com.casapellas.util.DocumuentosTransaccionales;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.PropertiesSystem;
import com.ibm.icu.util.Calendar;

public class ProcesarConsolidadoDepositos {
	
	static String msgFromConnection;
	public static Session sesionForQuery;
	static Transaction transForQuery;
	static boolean connectionActive;
	
	private static String msgErrorProceso = "";
	private static int NobatchToUse = 0; 
	
	private List<F55ca014> lstCompaniasPorCaja ;
	private List<String[]> dtaCuentaContableXCuentaBanco ;
	private List<String[]> dtaCuentaTransitoriaxBanco ;
	private List<String[]> dtaCuentaSobrantePorUnidadNegocio ;
	private List<String[]> dtaCuentaFaltantePorUnidadNegocio ;
	private List<String[]> dtaCuentaOtrosGastosPorUnidadNegocio ;
	private List<String[]> dtaCuentaAjustesPorExcepcion ;
	
	public int[] periodofiscal;
	
	static Map<String, Object> m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();	
	static String[] valoresJDEInsContado = (String[]) m.get("valoresJDEInsContado");
	

	@SuppressWarnings("unchecked")
	public ProcesarConsolidadoDepositos() {
		super();
		try {
			
			//&& ===================== Datos de las companias a utilizar
			 lstCompaniasPorCaja =  (ArrayList<F55ca014>)CodeUtil.getFromSessionMap( "pcd_lstCompaniasPorCaja" );
			
			//&& ===================== Datos de las cuentas contables por cuenta de banco
			 dtaCuentaContableXCuentaBanco = (ArrayList<String[]>) CodeUtil.getFromSessionMap("pcd_dtaCuentaContableXCuentaBanco");	
			
			//&& ======================  datos de las cuentas transitorias 
			 dtaCuentaTransitoriaxBanco = (ArrayList<String[]>) CodeUtil.getFromSessionMap("pcd_dtaCuentaTransitoriaxBanco");	
 
			 //&& ===================== Datos de las cuentas por sobrantes por unidad de negocios
			 dtaCuentaSobrantePorUnidadNegocio = (ArrayList<String[]>) CodeUtil.getFromSessionMap("pcd_dtaCuentaSobrantePorUnidadNegocio");	
			 
			//&& ===================== Datos de las cuentas por faltantes por unidad de negocios
			 dtaCuentaFaltantePorUnidadNegocio   = (ArrayList<String[]>) CodeUtil.getFromSessionMap("pcd_dtaCuentaFaltantePorUnidadNegocio");	
			 
			//&& ===================== Datos de las cuentas por ajustes por excepcion en proceso automatico
			 dtaCuentaAjustesPorExcepcion = (ArrayList<String[]>) CodeUtil.getFromSessionMap("pcd_dtaCuentaAjustePorExcepcion");	
			 
			//&& ===================== Datos de las cuentas de Otros Gastos  por unidad de negocios
			 dtaCuentaOtrosGastosPorUnidadNegocio = (ArrayList<String[]>) CodeUtil.getFromSessionMap("pcd_dtaCuentaOtrosGastosPorUnidadNegocio");
			 
			 if(periodofiscal == null){
				 periodofiscal = FechasUtil.obtenerPeriodoFiscalActual("");
			 }
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
	}
	@SuppressWarnings("unchecked")
	public boolean realizarConfirmacionDepositosBancoVsCaja(
			ConsolidadoCoincidente coincidencia,
			Vautoriz vaut, String sTipoEmpleado,
			int iTipoConfirma, Date dtFechaConfirma ){
		
		boolean bHecho = true;
	
		ReciboCtrl rcCtrl = new ReciboCtrl();
		String[] sCtaTBanco, sCtaBco;
		String[] sCtaCredAjuste = null;

		int iNoDocumento=0;
		String sTipoDoc = DocumuentosTransaccionales.TIPODOCREFERZX();
		String sConcepto = "";
		String sCodSucAsiento = "";
		double dLineaDocs = 1.0;
		long iMontoTotal=0;
		long iMontoDeps=0;
		String sMonedaBase = DocumuentosTransaccionales.MONEDABASE();
		String contadorUsuario ="";
		String sEstadoDeposito="";
		String usuarioPreconcilacion ="";
		String sTipoConfirma = "";
		BigDecimal bdTasaJDE;
		String sUninegCaja = DocumuentosTransaccionales.UNIDADNEGOCIOBASE()  ;
		int codigousr = 0;
		int contadorCodigo = 0;
		
		//Connection cn = null;
		
		long totalDomDepsCaja = 0L ;
		long totalDomDepsBanco = 0L ;
		long totalDepositoBanco = 0L;
		long totalDepositoCaja = 0L;
		
		Session session = null;
		Transaction transaction = null ;
		
		try {
			
			msgErrorProceso = "";
			NobatchToUse = 0;
			codigousr = vaut.getId().getCodreg();
			
			//&& ====== Establecer el tipo de confirmacion y estado del deposito de caja y deposito de banco
			switch (iTipoConfirma) {
			case 32:
				sTipoConfirma = DocumuentosTransaccionales.CFRAUTO() ;
				break;
			case 34:
				sTipoConfirma = DocumuentosTransaccionales.CFRMANUAL()  ;
				break;
			}

			sEstadoDeposito = DocumuentosTransaccionales.DPCONFIRMADO()  ;
			
			
			//&& ===================== Determinar las fechas de confirmacion en base a los dias permitidos
			
			int diasPermitidos = Integer.parseInt( String.valueOf(CodeUtil.getFromSessionMap("pcd_diasPermitidosMes") ) ) ;
			dtFechaConfirma = validarFechaBatch(dtFechaConfirma, diasPermitidos) ;
			
			
			
			session = HibernateUtilPruebaCn.currentSession();
			transaction = session.beginTransaction();
			
			List<PcdConsolidadoDepositosBanco> lstDepositosBanco = coincidencia.getDepositosbanco();
			Deposito_Report dpCaja = coincidencia.getDepositoscaja().get(0);
			
			final int caid = dpCaja.getCaid();
			final String codcomp = dpCaja.getCodcomp().trim();
			final int numerodecuenta = (int) coincidencia.getDepositosbanco().get(0).getNumerocuenta() ;
			
			final String monedaConicidencia = coincidencia.getMoneda();
			final long idbanco = coincidencia.getCodigobanco() ;
			
			bdTasaJDE = dpCaja.getTasa();
			
			/***********************  ===== Realizar transacciones en Edward's. ======= *****************************/
			sConcepto = "Confirmacion Deps No: " + coincidencia.getReferenciabanco();
			contadorUsuario = dpCaja.getCoduser();
			contadorCodigo =  dpCaja.getUsrcreate() ;
			usuarioPreconcilacion = DocumuentosTransaccionales.USUARIOPRECONCILIACION();
			
			F55ca014 dtaF14CompaniaCaja = (F55ca014)CollectionUtils.find(lstCompaniasPorCaja, new Predicate(){
				public boolean evaluate(Object o) {
					return ((F55ca014)o).getId().getC4id() == caid && ((F55ca014)o).getId().getC4rp01().trim().compareTo(codcomp) == 0 ;
				}
			});
			
			if(dtaF14CompaniaCaja == null){
				msgErrorProceso = "@F55CA014<>No se encontró configuración para caja/compania: "	+caid+"/"+codcomp;
				return bHecho = false;
			}
			
			sUninegCaja =  dtaF14CompaniaCaja.getId().getC4cjmcu().trim();
			if( sUninegCaja.matches("^[0]{1,4}$"))
				sUninegCaja = DocumuentosTransaccionales.UNIDADNEGOCIOBASE()  ;
			
			NobatchToUse = Divisas.numeroSiguienteJdeE1( );
			if(NobatchToUse == 0){
				msgErrorProceso = "@F0002<>No se pudo obtener numero de batch o documento para el batch";
				return bHecho =  false;
			}
			
			//&& =============== Guardar encabezado de asientos de diario F0011
			
			iMontoTotal = Divisas.pasarAenteroLong( coincidencia.getMontoBanco().add( coincidencia.getMontorporajuste() ).doubleValue() );
			bHecho = rcCtrl.registrarBatchA92(session, dtFechaConfirma, valoresJDEInsContado[8], NobatchToUse, iMontoTotal, usuarioPreconcilacion, 1,"PRECONCIL", valoresJDEInsContado[10] );
			
			if(!bHecho){
				msgErrorProceso = "@F0011 <> No se pudo grabar encabezado de batch";
				return bHecho;
			}
			
			//&& =============== cuenta de caja a debitarse
			sCtaTBanco =   (String[])
				CollectionUtils.find(dtaCuentaTransitoriaxBanco, new Predicate(){
					public boolean evaluate(Object o) {
						return 
						((String[])o)[6].trim().compareTo(codcomp) == 0 && 
						((String[])o)[7].trim().compareTo(monedaConicidencia.trim())  == 0 && 
						((String[])o)[8].trim().compareTo(Long.toString(idbanco).trim()) == 0  ;
					}
				});
					
			if(sCtaTBanco == null){
				msgErrorProceso = "@F0011 <> No se pudo obtener la configuracion de cuenta transitoria de caja ";
				return false;
			}
			
			sCodSucAsiento = sCtaTBanco[2];
			
			//&& =============== cuenta de banco a acreditarse
			sCtaBco = (String[])
				CollectionUtils.find(dtaCuentaContableXCuentaBanco, new Predicate(){
					public boolean evaluate(Object o) {
						return  ( (String[])o )[7].trim().compareTo(Integer.toString( numerodecuenta ) ) == 0 ;
					}
				}) ;
				
			if(sCtaBco==null){
				msgErrorProceso = "@F0011 <> No se pudo obtener la configuracion de cuenta banco  ";
				return bHecho = false;
			}
			
			//&& =============== moneda base para la compania
			sMonedaBase = dtaF14CompaniaCaja.getId().getC4bcrcd().trim();
			if( sMonedaBase.isEmpty() ){
				msgErrorProceso = "@F55CA014 <> No se ha podido obtener la moneda base para la compania "+sCtaBco[6];
				return bHecho = false;
			}
			
			//&& =============== hacer proceso de creacion de referencia 
			iNoDocumento = RevisionArqueoCtrl.generarReferenciaDeposito(
							(int)coincidencia.getReferenciabanco(), 
							dpCaja.getMpagodep().trim(),
							sCtaTBanco[2], coincidencia.getMoneda());
			
			coincidencia.setReferenciacomprobante(iNoDocumento) ;
			
			//&& ============== determinar el tipo de documento en dependencia del tipo de deposito
			final String mpago = dpCaja.getMpagodep();
			sTipoDoc = (String)
				CollectionUtils.find(Arrays.asList(PropertiesSystem.TipodocJdeTipoDocCaja), new Predicate(){
				 
					public boolean evaluate(Object o) {
						 String dta = String.valueOf(o);
						 return dta.startsWith(mpago);
					}
				});
			
			if(sTipoDoc == null){
				sTipoDoc = DocumuentosTransaccionales.TIPODOCREFERZX();
			}else{
				sTipoDoc = sTipoDoc.split(PropertiesSystem.SPLIT_CHAR)[1];
			}
			
			coincidencia.setTipodocumentojde(sTipoDoc);
			
			//&& ============== crear el numero que se va aplicar como sublibro a la cuenta subsidiara.
			String tipoAuxiliarCtaTrans = DocumuentosTransaccionales.CODIGOTIPOAUXILIARCT();
			
			String strSubLibroCuenta  = String.valueOf( numerodecuenta );
			if(strSubLibroCuenta.length() >= 5){
				strSubLibroCuenta = strSubLibroCuenta.substring(strSubLibroCuenta.length()-5, strSubLibroCuenta.length());
			}
			strSubLibroCuenta = String.valueOf( caid ) + strSubLibroCuenta;
			if(strSubLibroCuenta.length() >= 8){
				strSubLibroCuenta = strSubLibroCuenta.substring(
					strSubLibroCuenta.length()-8, strSubLibroCuenta.length());
			}
			strSubLibroCuenta = CodeUtil.pad(strSubLibroCuenta, 8 , "0"); 
			
			
			//&& ============== construir el total del monto en cordobas para validar contra la primera linea
			
			long lnMontoTotalDomesticoDetalle = 0;
			
			for (PcdConsolidadoDepositosBanco pcd : lstDepositosBanco) {
				
				if(  monedaConicidencia.compareTo( sMonedaBase ) == 0 )
					continue;
				
				lnMontoTotalDomesticoDetalle += Divisas.pasarAenteroLong( Divisas.roundDouble(pcd.getMontooriginal().multiply(bdTasaJDE).doubleValue()));
				 
			}
			 
			
			//&& ============== primera linea del documento, consolidado total de monto en caja 
			
			String sDescrip = "Dp:"+ coincidencia.getIdresumenbanco()+" Rf: "+coincidencia.getReferenciabanco() +" Cja "+dpCaja.getCaid() ;
			String msgLogs = sDescrip ;
			iMontoDeps = Divisas.pasarAentero ( coincidencia.getMontoBanco().doubleValue() );
			
			if( monedaConicidencia.compareTo( sMonedaBase) == 0 ){
				
				totalDepositoCaja = iMontoDeps;
				
				bHecho = rcCtrl.registrarAsientoDiarioLogs(session, msgLogs, dtFechaConfirma, sCodSucAsiento, sTipoDoc,
								iNoDocumento, dLineaDocs, NobatchToUse, sCtaTBanco[0], sCtaTBanco[1], sCtaTBanco[3],
								sCtaTBanco[4], sCtaTBanco[5], "AA", monedaConicidencia, (iMontoDeps * -1 ), sConcepto, 
								usuarioPreconcilacion, vaut.getId().getCodapp(), BigDecimal.ZERO, sTipoEmpleado, 
								sDescrip, sCtaTBanco[2], strSubLibroCuenta, tipoAuxiliarCtaTrans, 
								monedaConicidencia, sCodSucAsiento, "D", 0);
				if(!bHecho){
					return bHecho;
				}

			}else{
				
				
				long iMontoDepsdom = Divisas.pasarAenteroLong( Divisas.roundDouble( coincidencia.getMontoBanco().multiply(bdTasaJDE).doubleValue() ) );
				totalDepositoCaja = iMontoDeps;
				
				//&& =========== la sumatoria del detalle es distinta al monto domestico
				if( iMontoDepsdom != lnMontoTotalDomesticoDetalle && lnMontoTotalDomesticoDetalle != 0){
					iMontoDepsdom = lnMontoTotalDomesticoDetalle;
				}
				
				totalDomDepsCaja = iMontoDepsdom;
				
				bHecho = rcCtrl.registrarAsientoDiarioLogs(session, msgLogs, dtFechaConfirma, sCodSucAsiento, sTipoDoc,
						iNoDocumento, dLineaDocs, NobatchToUse, sCtaTBanco[0], sCtaTBanco[1], sCtaTBanco[3],
						sCtaTBanco[4], sCtaTBanco[5], "CA", monedaConicidencia, (iMontoDeps * -1 ), sConcepto, 
						usuarioPreconcilacion, vaut.getId().getCodapp(), bdTasaJDE, sTipoEmpleado, 
						sDescrip, sCtaTBanco[2], strSubLibroCuenta, tipoAuxiliarCtaTrans, sMonedaBase, sCodSucAsiento, "F", 0);
				
				if(!bHecho){
					return bHecho;
				}
				
				bHecho = rcCtrl.registrarAsientoDiarioLogs(session, msgLogs, dtFechaConfirma, sCodSucAsiento, sTipoDoc,
							iNoDocumento, dLineaDocs, NobatchToUse, sCtaTBanco[0], sCtaTBanco[1], sCtaTBanco[3],
							sCtaTBanco[4], sCtaTBanco[5], "AA", monedaConicidencia, (iMontoDepsdom * -1 ), sConcepto, 
							usuarioPreconcilacion, vaut.getId().getCodapp(), bdTasaJDE, sTipoEmpleado, 
							sDescrip, sCtaTBanco[2], strSubLibroCuenta, tipoAuxiliarCtaTrans, sMonedaBase, sCodSucAsiento, "F", (iMontoDeps * -1 ) );
				if(!bHecho){
					return bHecho;
				}

			}
			
			//&& ==============  recorrer los depositos de banco y crear sus lineas.
			for (int i = 0; i < lstDepositosBanco.size(); i++) {
				dLineaDocs++;
				
				PcdConsolidadoDepositosBanco pcd = lstDepositosBanco.get(i);
				
				sDescrip = "Dp Banco " + pcd.getReferenciaoriginal();
				
				iMontoTotal = Divisas.pasarAentero ( pcd.getMontooriginal().doubleValue() );
				
				if(  monedaConicidencia.compareTo( sMonedaBase ) == 0 ){
					
					totalDepositoBanco += iMontoTotal;
					
					bHecho = rcCtrl.registrarAsientoDiarioLogs(session, msgLogs, dtFechaConfirma, sCodSucAsiento, sTipoDoc,
											iNoDocumento, dLineaDocs, NobatchToUse, sCtaBco[0], sCtaBco[1], sCtaBco[3],
											sCtaBco[4], sCtaBco[5], "AA", monedaConicidencia,  iMontoTotal  , sConcepto,
											usuarioPreconcilacion, vaut.getId().getCodapp(), BigDecimal.ZERO, sTipoEmpleado, 
											sDescrip, sCtaBco[2], "", "", sMonedaBase, sCodSucAsiento, "D", 0);
					if(!bHecho){
						msgErrorProceso = rcCtrl.error.toString().split("@")[1] ;
						return bHecho;
					}
					
				}else{
					
					long iMontoDepsdom = Divisas.pasarAenteroLong( Divisas.roundDouble(pcd.getMontooriginal().multiply(bdTasaJDE).doubleValue()));
					
					totalDomDepsBanco += iMontoDepsdom;
					
					totalDepositoBanco += iMontoTotal;
					
					bHecho = rcCtrl.registrarAsientoDiarioLogs(session, msgLogs, dtFechaConfirma, sCodSucAsiento, sTipoDoc,
							iNoDocumento, dLineaDocs, NobatchToUse, sCtaBco[0], sCtaBco[1], sCtaBco[3],
							sCtaBco[4],sCtaBco[5], "CA", monedaConicidencia, iMontoTotal , sConcepto,
							usuarioPreconcilacion, vaut.getId().getCodapp(), bdTasaJDE, sTipoEmpleado, 
							sDescrip, sCtaBco[2], "", "", sMonedaBase, sCodSucAsiento, "F", 0);
					
					if(!bHecho){
						msgErrorProceso = rcCtrl.error.toString().split("@")[1] ;
						return bHecho;
					}
					
					bHecho = rcCtrl.registrarAsientoDiarioLogs(session, msgLogs, dtFechaConfirma, sCodSucAsiento, sTipoDoc,
								iNoDocumento, dLineaDocs, NobatchToUse, sCtaBco[0], sCtaBco[1], sCtaBco[3],
								sCtaBco[4],sCtaBco[5], "AA", monedaConicidencia, iMontoDepsdom , sConcepto,
								usuarioPreconcilacion, vaut.getId().getCodapp(), bdTasaJDE, sTipoEmpleado, 
								sDescrip, sCtaBco[2], "", "", sMonedaBase, sCodSucAsiento, "F",  iMontoTotal);
					if(!bHecho){
						msgErrorProceso = rcCtrl.error.toString().split("@")[1] ;
						return bHecho;
					}

				}
			}
			
			/* ***************************  Realizar transacciones por ajustes ***************************************/
			
			if( totalDepositoBanco != totalDepositoCaja  && coincidencia.getMontorporajuste().compareTo(BigDecimal.ZERO) == 0 ){
				coincidencia.setMontorporajuste( 
					new BigDecimal ( Long.toString( totalDepositoBanco - totalDepositoCaja ) ).divide(new BigDecimal("100") )
				);
			}
			
			double dajuste = 0 ;
			if( coincidencia.getMontorporajuste().compareTo(BigDecimal.ZERO) != 0 ){
				
				sDescrip ="";
				String sCodEmpleado = "";
				String sTipoAuxiliar = "";
				String[] sCtaTemp = null;
				String sObj = "", sSub = "";
			
				
				//&& ====== Si el ajuste es positivo es sobrante, buscar cuenta de otros ingresos
				
				if (coincidencia.getMontorporajuste().compareTo(BigDecimal.ZERO) == 1) {
					String[] cuentaOtrosingresos = DocumuentosTransaccionales.CTAOTROSINGRESOS().split(",");
					
					sObj = cuentaOtrosingresos[1] ;
					sSub = cuentaOtrosingresos[2] ;
					
					// && ============= Buscar la cuenta sobrantes
					final String gmmcu = sUninegCaja.trim();
					final String gmobj = sObj.trim() ;
					final String gmsub = sSub.trim() ;
					
					sCtaTemp = (String[]) CollectionUtils.find( dtaCuentaSobrantePorUnidadNegocio,  new Predicate() {
						public boolean evaluate(Object o) {
							return 
							((String[])o)[3].trim().compareTo(gmmcu) == 0 && 
							((String[])o)[4].trim().compareTo(gmobj) == 0 && 
							((String[])o)[5].trim().compareTo(gmsub) == 0 &&
							((String[])o)[6].trim().compareTo(codcomp) == 0 ;
						}
					});
					
				} else {
					
					//&& ============== validar si el monto del faltante debe cargarse al cajero
					List<String[]> cuentaContableFaltante ;
					boolean cargarFaltanteCajero = true;
					
					final String monedaCoincidencia = coincidencia.getMoneda().trim().toUpperCase();
					List<String[]> montosPermitidos = Arrays.asList(PropertiesSystem.MONTOS_FALTANTES_PERMITIDOS);
					String[] montos = (String[]) CollectionUtils.find(montosPermitidos, new Predicate() {
								public boolean evaluate(Object o) {
									return ((String[]) o)[0].trim().toUpperCase().compareTo(monedaCoincidencia) == 0;
								}
							});
					
					if(montos != null){
						cargarFaltanteCajero = coincidencia.getMontorporajuste().abs().compareTo( new BigDecimal(montos[1]) ) == 1;
					}
					
					if (cargarFaltanteCajero) {
						
					
							sUninegCaja = DocumuentosTransaccionales.CTADEUDORESVARIOSUNINEG(codcomp.trim());
						
	
						sObj = DocumuentosTransaccionales.CTADEUDORESVARIOSOB() ;
						sSub = DocumuentosTransaccionales.CTADEUDORESVARIOSSB() ;
						sTipoAuxiliar = DocumuentosTransaccionales.CODIGOTIPOAUXILIARFE();
	
						sCodEmpleado = CodeUtil.pad( String.valueOf( dpCaja.getCodcajero() ),	8, "0");
						
						cuentaContableFaltante  = dtaCuentaFaltantePorUnidadNegocio;
							
					} else {
						String[] cuentaOtrosGastos= DocumuentosTransaccionales.CTAGASTOSDIVERSOS().split(",");
						sObj = cuentaOtrosGastos[1];
						sSub = cuentaOtrosGastos[2];
						sCodEmpleado = "";
						sTipoAuxiliar = "";

						cuentaContableFaltante = dtaCuentaOtrosGastosPorUnidadNegocio;
					}
					
					// && ============= Buscar la cuenta de faltantes
					final String gmmcu = sUninegCaja.trim();
					final String gmobj = sObj.trim() ;
					final String gmsub = sSub.trim() ;
					
					sCtaTemp = (String[]) CollectionUtils.find( cuentaContableFaltante,  new Predicate() {
						public boolean evaluate(Object o) {
							
							boolean equal =
									((String[])o)[3].trim().compareTo(gmmcu) == 0 && 
									((String[])o)[4].trim().compareTo(gmobj) == 0 ;
							
							if( !gmsub.isEmpty() ){
								equal  = equal && ((String[])o)[5].trim().compareTo(gmsub) == 0 ;
							}
							
							return equal;
							
						}
					});
				}
				
				//&& ====== Si el ajuste es positivo es sobrante, poner monto en negativo y pasar a cuenta de otros ingresos
				if ( coincidencia.getMontorporajuste().signum() == 1 ){
					sDescrip = "Sobrante PreConcil";
					dajuste = coincidencia.getMontorporajuste().negate().doubleValue();
				} 
				if ( coincidencia.getMontorporajuste().signum() == -1 ){
					sDescrip = "Faltante PreConcil";
					dajuste = coincidencia.getMontorporajuste().abs().doubleValue();
				}
				
				//&& ============= cuentas para ajustes por excepcion.
				if( coincidencia.isProcesarAjustePorExcepcion() ){
					
					final String gmaidCtaExcepcion = coincidencia.getCuentaAjusteExcepcionId() ;
					
					sCtaTemp = (String[]) CollectionUtils.find( dtaCuentaAjustesPorExcepcion,  new Predicate() {
						public boolean evaluate(Object o) {
							return  ((String[])o)[1].trim().compareTo(gmaidCtaExcepcion) == 0 ;
						}
					});
					
				  sCodEmpleado = "";
				  sTipoAuxiliar = "";
				  sDescrip = "Diferencia a Revision " ;
					
				}
				
				if(sCtaTemp == null){
					msgErrorProceso = "VF0901: No se ha podido obtener la cuenta '"+sUninegCaja+"."+sObj+"."+sSub+" en maestro de cuentas";
					return bHecho = false;
				}
				
				//&& ===== Asignacion de cuenta en dependencia del tipo de ajuste.
				sCtaCredAjuste = sCtaTemp;
				
				//&& ============= Generacion de lineas de asientos.
				iMontoDeps = Divisas.pasarAenteroLong(dajuste);
				dLineaDocs += 1;
				
				if( monedaConicidencia.compareTo(sMonedaBase) == 0){
					
					bHecho = rcCtrl.registrarAsientoDiarioLogs(session, msgLogs, dtFechaConfirma, sCodSucAsiento, sTipoDoc,
							iNoDocumento, dLineaDocs, NobatchToUse, sCtaCredAjuste[0], sCtaCredAjuste[1], sCtaCredAjuste[3],
							sCtaCredAjuste[4],sCtaCredAjuste[5], "AA", monedaConicidencia, iMontoDeps, sConcepto,
							usuarioPreconcilacion, vaut.getId().getCodapp(), BigDecimal.ZERO, sTipoEmpleado, sDescrip, 
							sCtaCredAjuste[2], sCodEmpleado, sTipoAuxiliar , sMonedaBase, sCodSucAsiento, "D", 0);
					
					if(!bHecho){
						return bHecho;
					}
				}else{
					long iMontoDepsdom = Divisas.pasarAenteroLong( Divisas.roundDouble(new BigDecimal(Double.toString( dajuste ) ).multiply(bdTasaJDE).doubleValue()));
					
					iMontoDepsdom = totalDomDepsCaja - totalDomDepsBanco;

					bHecho = rcCtrl.registrarAsientoDiarioLogs(session, msgLogs, dtFechaConfirma, sCodSucAsiento, sTipoDoc,
							iNoDocumento, dLineaDocs, NobatchToUse, sCtaCredAjuste[0], sCtaCredAjuste[1], sCtaCredAjuste[3],
							sCtaCredAjuste[4], sCtaCredAjuste[5], "CA", monedaConicidencia, iMontoDeps, sConcepto, 
							usuarioPreconcilacion, vaut.getId().getCodapp(), bdTasaJDE, sTipoEmpleado, sDescrip,
							sCtaCredAjuste[2], sCodEmpleado, sTipoAuxiliar, sMonedaBase, sCodSucAsiento, "F", 0);
					if(!bHecho){
						msgErrorProceso = "@F0911: Error al grabar ajuste de Confirmacion en JDE";
						return bHecho;
					}
					
					bHecho = rcCtrl.registrarAsientoDiarioLogs(session, msgLogs, dtFechaConfirma, sCodSucAsiento, sTipoDoc,
							iNoDocumento, dLineaDocs, NobatchToUse, sCtaCredAjuste[0], sCtaCredAjuste[1], sCtaCredAjuste[3],
							sCtaCredAjuste[4], sCtaCredAjuste[5], "AA", monedaConicidencia, iMontoDepsdom, sConcepto, 
							usuarioPreconcilacion, vaut.getId().getCodapp(), bdTasaJDE, sTipoEmpleado, sDescrip, 
							sCtaCredAjuste[2], sCodEmpleado, sTipoAuxiliar, sMonedaBase, sCodSucAsiento, "F", iMontoDeps);
					if(!bHecho){
						msgErrorProceso = "@F0911: Error al grabar ajuste de Confirmacion en JDE";
						return bHecho;
					}
				}
			}
			
			
			/***********************  ===== Realizar transacciones en modulo de caja. ======= *****************************/
			List<String>queries = new ArrayList<String>();
			String queryString = "";
			
			
			//&& ==================== 0 Actualizar el deposito de caja.
			queryString =
				" update "+PropertiesSystem.ESQUEMA +".Deposito " 
					+ " set estadocnfr = '" + sEstadoDeposito + "', tipoconfr = '" +sTipoConfirma.trim()
					+ "', referdep = '"+String.valueOf( coincidencia.getReferenciabanco() )+"', fechamod = '"
					+FechasUtil.formatDatetoString(new Date(), "yyyy-MM-dd")
					+"', usrconfr = "+codigousr + ", horamod = '"
					+FechasUtil.formatDatetoString(new Date(), "HH.mm.ss")
					+"' where consecutivo = "+dpCaja.getConsecutivo() 
					+" and caid = "+ dpCaja.getCaid() +" and trim(codcomp) ='"
					+dpCaja.getCodcomp().trim()+"' and nodeposito = " + dpCaja.getNodeposito();
			
			queries.add(queryString);
			
			
			//&& ==================== 1 Actualizar el registro del consolidado.
			
			List<Integer>idsConsolidadoBanco = (ArrayList<Integer>)CodeUtil.selectPropertyListFromEntity(lstDepositosBanco, "idresumenbanco", false);
			
			queryString = "update "+PropertiesSystem.ESQUEMA+".pcd_consolidado_depositos_banco " +
				"set referenciajde = "+ iNoDocumento +", montoajustado = " + coincidencia.getMontoAjustado()+"," +
				" estadoconfirmacion = 1, usuarioactualiza = "+ codigousr +", " +
				" usuarioultimacomparacion = " + codigousr +", fechamodconsolida = current_timestamp,  " +
				" numerobatch = " + NobatchToUse + ", tipodocumentojde = '"+sTipoDoc+"'" +
				" where idresumenbanco in " + idsConsolidadoBanco.toString().replace("[", "(").replace("]", ")" );
			queries.add(queryString);
			
			//&& ==================== 2 Actualizar estado del deposito de banco.
			idsConsolidadoBanco = (ArrayList<Integer>)CodeUtil.selectPropertyListFromEntity(lstDepositosBanco, "iddepbcodet", false);
			
			queryString = " update "+PropertiesSystem.ESQUEMA+".Depbancodet set idtipoconfirm = "+iTipoConfirma +
				  ", idestadocnfr = "+ 35 +", fechamod = current_timestamp, usrmod = " +
				  codigousr +", referencia = "+iNoDocumento +", historicomod =' ' " +
				 "where iddepbcodet in "+  idsConsolidadoBanco.toString().replace("[", "(").replace("]", ")" ) ;
			queries.add(queryString); 
			
			//&& ==================== 3 Actualizar estado del archivo.
			idsConsolidadoBanco = (ArrayList<Integer>)CodeUtil.selectPropertyListFromEntity(lstDepositosBanco, "idarchivo", false);
			
			queryString = " update "+PropertiesSystem.ESQUEMA 
					+ ".Archivo set  usrmodi = " +codigousr +", depositosrestantes = (depositosrestantes - 1) " 
					+", fechamod = current_timestamp  where idarchivo in "+  idsConsolidadoBanco.toString().replace("[", "(").replace("]", ")" ) ;
			queries.add(queryString); 
			
			//&& ==================== 4 Crear el registro de conciliacion.
			String fechahora = FechasUtil.formatDatetoString(new Date(), "yyyy-MM-dd HH:mm:ss.SSSSSS" );
			
			queryString =  "insert into " + PropertiesSystem.ESQUEMA+".CONCILIACION "+
		        "(IDCONCILIACION, IDARCHIVO, MONTO, NOBATCH, NOREFERENCIA, MONEDA, " +
		        "TIPODOC, IDCUENTA, ESTADO, USRCREA, USRMOD, FECHAMOD, FECHACREA, " +
		        "RNOBATCH, RNODOC,USUARIOCOMPROBANTE, CODIGOUSRCOMPROBANTE ) values " +
		        "(default, " + idsConsolidadoBanco.get(0) + ","+coincidencia.getMontoBanco()+"," +
		         NobatchToUse+", "+iNoDocumento+", '"+coincidencia.getMoneda()+"','"+sTipoDoc+"'," +
		         numerodecuenta+", "+46+", "+codigousr+", "+codigousr+
		         ", '"+fechahora+"', '"+fechahora+"' , 0 ,0, '"+contadorUsuario+"'," + contadorCodigo + " )";
			
			queries.add(queryString); 

			String ssIdConcilia = " ( select idconciliacion from conciliacion where fechacrea = '"
							+fechahora+"' and noreferencia = "+iNoDocumento +")" ;
			
			//&& ==================== 5 Crear el registro detalle de la conciliacion.
			queryString = 
				"insert into @GCPMCAJA.CONCILIADET "+
				"( IDDEPOSITOBCO, REFERDCAJA, IDCONCILIACION, NODEPOSITO, CODSUC, CODCOMP, CAID, USRCREA, USRMOD,  IDDEPOSITOCJA ) " +
				"values  " +
				"( @IDDEPOSITOBCO, @REFERDCAJA, @IDCONCILIACION, @NODEPOSITO, '@CODSUC', '@CODCOMP', @CAID, @USRCREA, @USRMOD, @IDDEPOSITOCJA)";
			
			for (PcdConsolidadoDepositosBanco pcd : lstDepositosBanco) {
				queries.add(
					queryString
					.replace("@GCPMCAJA", PropertiesSystem.ESQUEMA)
					.replace("@IDDEPOSITOBCO", String.valueOf(pcd.getIddepbcodet()) )
					.replace("@REFERDCAJA", String.valueOf(pcd.getReferenciaoriginal()) )
					.replace("@IDCONCILIACION", ssIdConcilia )
					.replace("@NODEPOSITO", String.valueOf(dpCaja.getNodeposito() ) )
					.replace("@CODSUC", dpCaja.getCodsuc().trim() )
					.replace("@CODCOMP", dpCaja.getCodcomp().trim() )
					.replace("@CAID", String.valueOf(pcd.getIddepbcodet()) )
					.replace("@USRCREA", String.valueOf( codigousr ) )
					.replace("@USRMOD", String.valueOf( codigousr ) )
					.replace("@IDDEPOSITOCJA", String.valueOf( dpCaja.getConsecutivo() ) )
				);
			}
			
			//&& ==================== 6 Guardar el detalle del ajuste, en caso de haberse generado.
			if( coincidencia.getMontorporajuste().compareTo(BigDecimal.ZERO) != 0 ){
				int iCodCargo = (coincidencia.getMontorporajuste().compareTo(BigDecimal.ZERO) == 1 )? 0 : dpCaja.getCodcajero(); 
				queryString = 
				" insert into "+PropertiesSystem.ESQUEMA+".AJUSTECONC " +
				"(IDAJUSTEC, IDCONCILIACION, NOBATCH, NODOCUMENTO, MONTO, CARGOA, IDCUENTA, " +
				" IDTIPOAJUSTE, TIPODOC, USRCREA, USRMOD, FECHAMOD, FECHACREA, ESTADO)"+ 
				" values " +
				" ( default,"+ssIdConcilia+", "+NobatchToUse+", "+iNoDocumento
					+", '"+ dajuste +"', "+iCodCargo+","+sCtaCredAjuste[1]+" , "
					+ ( (dajuste > 0 )? 49 : 48 )+ ",'"
					+sTipoDoc+"', "+ codigousr +", "+ codigousr
					+", current_timestamp, current_timestamp,   46  ) ";
				
				queries.add(queryString); 
			}
			/*
			//&& ==================== 6 Actualizacion del numero de batch para contabilizacion
			queryString =
			" update "+PropertiesSystem.JDECOM+".f98301 set DESVL = " + NobatchToUse +
			" WHERE DEPID = '" + PropertiesSystem.CONTABLIZA_PROGRAMA_ID +"'" +
			"  and DEVERS = '" + PropertiesSystem.CONTABILIZA_PROGRAMA_VERSION +"' " +
			"  and DEFLDN = 'GLICU' " ;
			
			queries.add(queryString); 
			*/
			
			int rowsAffected = 0;
			int iQueryIndex = 0;
			for (int i = 0; i < queries.size(); i++) {
				iQueryIndex = i;
				
				try {
					rowsAffected  = session.createSQLQuery(queries.get(i)).executeUpdate();
					
					if(rowsAffected == 0){
						msgErrorProceso = "@Consulta no afecta registros ";
						bHecho = false;
						break;
					}
					
				} catch (Exception e) {
					msgErrorProceso = "@Consulta con errores ";
					bHecho = false;
					e.printStackTrace();
					break;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			bHecho = false;
			msgErrorProceso = "@Error de sistema al realizar confirmacion del deposito ";
		}finally{
		
			 try {
				
				 if( bHecho ){
					 transaction.commit();
				 }else{
					 transaction.rollback();
				 }
				 
			} catch (Exception e) {
				e.printStackTrace();
			}
			 
			try {
				HibernateUtilPruebaCn.closeSession(session);
			} catch (Exception e) {
				e.printStackTrace();
			}
			 
			
		}
		return bHecho;
	}

	public static int cantidadDiasHabilesMes( Date fechaActual, int diasPermitidos){
		int diasHabilesPosteriorCierre = diasPermitidos ;
		
		try {
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(fechaActual);
			
			cal.add( Calendar.MONTH, -1 );
			cal.set( Calendar.DAY_OF_MONTH, cal.getActualMaximum( Calendar.DAY_OF_MONTH) );
			
			for (int i = 0; i < diasPermitidos; i++) {
				
				cal.add(Calendar.DAY_OF_MONTH, 1);
				
				if( cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ){
					diasPermitidos ++;
					continue;
				}
				
				boolean diahabil = FechasUtil.esDiaLaboralHabil(cal.getTime());
				
				if( !diahabil ){
					diasPermitidos ++;
					continue;
				}
			}
			
			diasHabilesPosteriorCierre = diasPermitidos ;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return diasHabilesPosteriorCierre;
	}
	
	public static Date validarFechaBatch(Date fechaDeposito, int diasPermitidos) {
		Date fechaBatch = new Date();
		
		try {
			
			fechaBatch = fechaDeposito ;
			
			Date fechaAntes = fechaDeposito;
			Date fechaHoy =  new Date();
			
			boolean antes = fechaAntes.before( fechaHoy ) ;
			
			if(!antes) {
				return  fechaBatch = fechaAntes;
			}
				 
			Calendar calfechaAntes = Calendar.getInstance();
			Calendar calFechaActual = Calendar.getInstance();
			
			calfechaAntes.setTime(fechaAntes);
			calFechaActual.setTime(fechaHoy);
			 
			boolean mesesDistintos = calFechaActual.get(Calendar.MONTH) != calfechaAntes.get(Calendar.MONTH) ;
			boolean aniosDistintos = calFechaActual.get(Calendar.YEAR) != calfechaAntes.get(Calendar.YEAR) ;
			
			
			//&& mismo mes en anios diferentes
			if( aniosDistintos && !mesesDistintos) {
				return fechaBatch = fechaHoy;
			}  
			
			if(!mesesDistintos) {
				return fechaBatch = fechaAntes;
			}
			
			//&& ============ validar que la fecha del deposito no sea anterior a un mes atras 
			Calendar calFechaMesAnterior = Calendar.getInstance();
			calFechaMesAnterior.setTime(fechaHoy);
			calFechaMesAnterior.add(Calendar.MONTH, -1);
			calFechaMesAnterior.set(Calendar.DAY_OF_MONTH, calFechaMesAnterior.getActualMinimum(Calendar.DAY_OF_MONTH) ) ;
			
			int diasTranscurridosMesAnterior = FechasUtil.obtenerDiferenciaDias(calFechaMesAnterior.getTime(), calFechaActual.getTime() ) ;
			int diasDesdeHoyHastaDeposito = FechasUtil.obtenerDiferenciaDias(calfechaAntes.getTime(), calFechaActual.getTime() ) ;
			boolean diasMasDeUnMesAtras = ( diasDesdeHoyHastaDeposito > diasTranscurridosMesAnterior );
			
			if( diasMasDeUnMesAtras ) {
				return fechaBatch = fechaHoy;
			}
					
			Calendar fechaInicioMesActual = Calendar.getInstance();
			fechaInicioMesActual.set(Calendar.DAY_OF_MONTH,  fechaInicioMesActual.getActualMinimum(Calendar.DAY_OF_MONTH));
			
			int diasTranscurridos = FechasUtil.obtenerDiferenciaDias(fechaInicioMesActual.getTime(), calFechaActual.getTime() ) ;
			
			if( diasTranscurridos > diasPermitidos ){
				return fechaBatch  = new Date();
			}

			
		}catch(Exception e) {
			e.printStackTrace();
			fechaBatch = new Date();
		}
		return fechaBatch;
	}
	
	
	
	//***************************************************************************************************************************************//	
	/******************************************************************************************/
	/** Método: buscar registros Deposito, de depositos de caja a partir de los parametros obtenidos.
	 *	Fecha:  23/08/2011
	 *  Nombre: Carlos Manuel Hernández Morrison.
	 */
		public boolean realizarConfirmacionDepositos(
							ConsolidadoCoincidente coincidencia,
							PcdConsolidadoDepositosBanco pcd, 
							Vautoriz vaut, String sTipoEmpleado,
							int iTipoConfirma, Date dtFechaConfirma, 
							int idarchivo ){
			
			boolean bHecho = true;
			ReciboCtrl rcCtrl = new ReciboCtrl();
			String[] sCtaTBanco, sCtaBco;
			String[] sCtaCredAjuste = null;

			int iNoDocumento=0;
			String sTipoDoc = DocumuentosTransaccionales.TIPODOCREFERZX();
			String sConcepto = "";
			String sCodSucAsiento = "";
			double dLineaDocs = 1.0;
			long iMontoTotal=0;
			long iMontoDeps=0;
			String sMonedaBase = DocumuentosTransaccionales.MONEDABASE() ;
			String contadorUsuario ="";
			String sEstadoDeposito="";
			String usuarioPreconcilacion ="";
			String sTipoConfirma = "";
			ConfirmaDepositosCtrl cdc = new ConfirmaDepositosCtrl();
			BigDecimal bdTasaJDE = null;
			String sUninegCaja = DocumuentosTransaccionales.UNIDADNEGOCIOBASE()  ;
			int codigousr = 0;
			int contadorCodigo = 0;
			 
			
			long totalDomDepsCaja = 0 ;
			long totalDomDepsBanco = 0 ;
			long totalDepositoBanco = 0L;
			long totalDepositoCaja = 0L;
			
			Session session = null;
			Transaction transaction = null;
			
			try {
				
				msgErrorProceso = "";
				NobatchToUse = 0;
				codigousr = vaut.getId().getCodreg();
				
				//&& ====== Establecer el tipo de confirmacion y estado del deposito de caja y deposito de banco
				switch (iTipoConfirma) {
				case 32:
					sTipoConfirma = DocumuentosTransaccionales.CFRAUTO()  ;
					break;
				case 34:
					sTipoConfirma = DocumuentosTransaccionales.CFRMANUAL()  ;
					break;
				}

				sEstadoDeposito = DocumuentosTransaccionales.DPCONFIRMADO()  ;
				
				//&& ===================== Determinar las fechas de confirmacion en base a los dias permitidos
				
				int diasPermitidos = Integer.parseInt( String.valueOf(CodeUtil.getFromSessionMap("pcd_diasPermitidosMes") ) ) ;
				dtFechaConfirma = validarFechaBatch(dtFechaConfirma, diasPermitidos) ;
				 
				//&& ===================== conexiones
				session = HibernateUtilPruebaCn.currentSession();
				transaction = session.beginTransaction();
				
				List<Deposito_Report> lstDepsCaja = coincidencia.getDepositoscaja();
				//List<Deposito> lstDepsCajaR = coincidencia.getDepositoscajaR();
				final int caid = lstDepsCaja.get(0).getCaid();
				final String codcomp = lstDepsCaja.get(0).getCodcomp().trim();
				final int numerodecuenta = (int)pcd.getNumerocuenta();
				
				/***********************  ===== Realizar transacciones en Edward's. ======= *****************************/
				sConcepto = "Confirmacion Deps No: " + coincidencia.getReferenciabanco();
				contadorUsuario = lstDepsCaja.get(0).getCoduser();
				contadorCodigo =  lstDepsCaja.get(0).getUsrcreate() ;
				usuarioPreconcilacion = DocumuentosTransaccionales.USUARIOPRECONCILIACION();
				String msgLogs = sConcepto;
				
				F55ca014 dtaF14CompaniaCaja = (F55ca014)CollectionUtils.find(lstCompaniasPorCaja, new Predicate(){
					public boolean evaluate(Object o) {
						return ((F55ca014)o).getId().getC4id() == caid && ((F55ca014)o).getId().getC4rp01().trim().compareTo(codcomp) == 0 ;
					}
				});
				
				if(dtaF14CompaniaCaja == null){
					msgErrorProceso = "@F55CA014<>No se encontró configuración para caja/compania: "	+caid+"/"+codcomp;
					return bHecho = false;
				}
				
				sUninegCaja =  dtaF14CompaniaCaja.getId().getC4cjmcu().trim();
				if( sUninegCaja.matches("^[0]{1,4}$"))
					sUninegCaja = DocumuentosTransaccionales.UNIDADNEGOCIOBASE()  ;
				
//				NobatchToUse = Divisas.numeroSiguienteJdeE1(CodigosJDE1.NUMEROBATCH );
				NobatchToUse = Divisas.numeroSiguienteJdeE1( );
				if(NobatchToUse == 0){
					msgErrorProceso = "@F0002<>No se pudo obtener numero de batch o documento para el batch";
					return bHecho = false;
				}
				
				//&& =============== Guardar encabezado de asientos de diario F0011
				iMontoTotal = Divisas.pasarAenteroLong( coincidencia.getMontoBanco().doubleValue() );
				bHecho = rcCtrl.registrarBatchA92(session, dtFechaConfirma, valoresJDEInsContado[8], NobatchToUse, iMontoTotal, usuarioPreconcilacion, 1, "PRECONCIL", valoresJDEInsContado[10] );
				if(!bHecho){
					msgErrorProceso = "@F0011 <> No se pudo grabar encabezado de batch";
					return bHecho;
				}

				sCtaBco = (String[])
					CollectionUtils.find(dtaCuentaContableXCuentaBanco, new Predicate(){
						public boolean evaluate(Object o) {
							return  ( (String[])o )[7].trim().compareTo(Integer.toString( numerodecuenta ) ) == 0 ;
						}
					}) ;
				
				if(sCtaBco==null){
					msgErrorProceso = "@F0011 <> No se pudo obtener la configuracion de cuenta banco  ";
					return bHecho =  false;
				}
				
				sCodSucAsiento = sCtaBco[2];
				sMonedaBase = dtaF14CompaniaCaja.getId().getC4bcrcd().trim();
				if( sMonedaBase.isEmpty() ){
					msgErrorProceso = "@F55CA014 <> No se ha podido obtener la moneda base para la compania "+sCtaBco[6];
					return bHecho = false;
				}
				
				//************************ hacer proceso de creacion de referencia 
				iNoDocumento = RevisionArqueoCtrl.generarReferenciaDeposito(
								(int)coincidencia.getReferenciabanco(), 
								lstDepsCaja.get(0).getMpagodep().trim(),
								sCtaBco[2], coincidencia.getMoneda());
				
				coincidencia.setReferenciacomprobante(iNoDocumento) ;
				
				//&& ============== determinar el tipo de documento en dependencia del tipo de deposito
				final String mpago = lstDepsCaja.get(0).getMpagodep();
				sTipoDoc = (String)
				CollectionUtils.find(Arrays.asList(PropertiesSystem.TipodocJdeTipoDocCaja), new Predicate(){
					 
					public boolean evaluate(Object o) {
						 String dta = String.valueOf(o);
						 return dta.startsWith(mpago);
					}
				});
				if(sTipoDoc == null){
					sTipoDoc = DocumuentosTransaccionales.TIPODOCREFERZX();
				}else{
					sTipoDoc = sTipoDoc.split(PropertiesSystem.SPLIT_CHAR)[1];
				}
				
				coincidencia.setTipodocumentojde(sTipoDoc);
				
				
				//&& ============== comprobacion del monto en cordobas para transacciones en dolares.
				long montodomcaja = 0 ;
				
				if( coincidencia.getMoneda().compareTo( sMonedaBase ) != 0 && coincidencia.getMontorporajuste().compareTo(BigDecimal.ZERO) == 0){
					
					bdTasaJDE = lstDepsCaja.get(0).getTasa();
					
					for (Deposito_Report dpCaja : lstDepsCaja) {
						montodomcaja += Divisas.pasarAenteroLong( Divisas.roundDouble(dpCaja.getMonto().multiply(bdTasaJDE).doubleValue()));
					}
					
				}
				
				
				//&& ==============  Grabar la primera linea del asiento de diario por el total notificado por banco.
				String sDescrip = "Depósito de banco "+ coincidencia.getReferenciabanco();
				
				if(pcd.getMoneda().compareTo( sMonedaBase ) == 0 ){
					
					totalDepositoBanco  = iMontoTotal;
					
					bHecho = rcCtrl.registrarAsientoDiarioLogs(session, msgLogs, dtFechaConfirma,  sCodSucAsiento, sTipoDoc,
											iNoDocumento, dLineaDocs, NobatchToUse, sCtaBco[0], sCtaBco[1], sCtaBco[3],
											sCtaBco[4],sCtaBco[5], "AA", pcd.getMoneda(), iMontoTotal, sConcepto,
											usuarioPreconcilacion, vaut.getId().getCodapp(), BigDecimal.ZERO, sTipoEmpleado, 
											sDescrip, sCtaBco[2], "", "", sMonedaBase, sCtaBco[2], "D", 0);
					if(!bHecho){
						msgErrorProceso = rcCtrl.error.toString().split("@")[1] ;
						return bHecho;
					}
				}else{
					
					//&& ================== Utilizar la tasa de cambio oficial del deposito que es la misma que se grabo al aprobar el arqueo.
					bdTasaJDE = lstDepsCaja.get(0).getTasa();
					
					long iMontoDepsdom = Divisas.pasarAenteroLong( Divisas.roundDouble(coincidencia.getMontoBanco().multiply(bdTasaJDE).doubleValue()));
					totalDomDepsBanco = iMontoDepsdom;
					
					if( iMontoDepsdom != montodomcaja  && coincidencia.getMontorporajuste().compareTo(BigDecimal.ZERO) == 0 ){
						iMontoDepsdom = montodomcaja;
					}
					
					totalDepositoBanco = iMontoTotal;
					
					bHecho = rcCtrl.registrarAsientoDiarioLogs(session, msgLogs, dtFechaConfirma,  sCodSucAsiento, sTipoDoc,
								iNoDocumento, dLineaDocs, NobatchToUse, sCtaBco[0], sCtaBco[1], sCtaBco[3],
								sCtaBco[4],sCtaBco[5], "AA", pcd.getMoneda(), iMontoDepsdom, sConcepto,
								usuarioPreconcilacion, vaut.getId().getCodapp(), bdTasaJDE, sTipoEmpleado, 
								sDescrip, sCtaBco[2], "", "", sMonedaBase, sCtaBco[2], "F", iMontoTotal );
					if(!bHecho){
						msgErrorProceso = rcCtrl.error.toString().split("@")[1] ;
						return bHecho;
					}
					bHecho = rcCtrl.registrarAsientoDiarioLogs(session, msgLogs, dtFechaConfirma, sCodSucAsiento, sTipoDoc,
								iNoDocumento, dLineaDocs, NobatchToUse, sCtaBco[0], sCtaBco[1], sCtaBco[3],
								sCtaBco[4],sCtaBco[5], "CA", pcd.getMoneda(), iMontoTotal, sConcepto,
								usuarioPreconcilacion, vaut.getId().getCodapp(), bdTasaJDE, sTipoEmpleado, 
								sDescrip, sCtaBco[2], "", "", sMonedaBase, sCtaBco[2], "F", 0);
					if(!bHecho){
						msgErrorProceso = rcCtrl.error.toString().split("@")[1] ;
						return bHecho;
					}
				}
				
				//&& ===== crear el numero que se va aplicar como sublibro a la cuenta subsidiara.
				String tipoAuxiliarCtaTrans = DocumuentosTransaccionales.CODIGOTIPOAUXILIARCT();
				
				String strSubLibroCuenta  = String.valueOf( numerodecuenta );
				if(strSubLibroCuenta.length() >= 5){
					strSubLibroCuenta = strSubLibroCuenta.substring(strSubLibroCuenta.length()-5, strSubLibroCuenta.length());
				}
				strSubLibroCuenta = String.valueOf( caid ) + strSubLibroCuenta;
				if(strSubLibroCuenta.length() >= 8){
					strSubLibroCuenta = strSubLibroCuenta.substring(
						strSubLibroCuenta.length()-8, strSubLibroCuenta.length());
				}
				strSubLibroCuenta = CodeUtil.pad(strSubLibroCuenta, 8 , "0"); 
				

				final String moneda = pcd.getMoneda();
				final long idbanco = pcd.getCodigobanco() ;
				
				//&& ===== Recorrer depositos de caja para generar las lineas de los asientos de diario.
				for (int i = 0; i < lstDepsCaja.size(); i++) {
					dLineaDocs++;
					Deposito_Report dpCaja = lstDepsCaja.get(i);
					
					sCtaTBanco =   (String[])
					CollectionUtils.find(dtaCuentaTransitoriaxBanco, new Predicate(){
						public boolean evaluate(Object o) {
							return 
							((String[])o)[6].trim().compareTo(codcomp) == 0 && 
							((String[])o)[7].trim().compareTo(moneda.trim())  == 0 && 
							((String[])o)[8].trim().compareTo(Long.toString(idbanco).trim()) == 0  ;
						}
					});
					
					if(sCtaTBanco == null){
						msgErrorProceso = "Cuenta transitoria a banco no encontrada ";
						return bHecho = false;
					}
					
					sDescrip = "Dp:"+dpCaja.getConsecutivo()+" Rf: "+dpCaja.getReferencia() +" Cja "+dpCaja.getCaid() ;
					iMontoDeps = Divisas.pasarAenteroLong(dpCaja.getMonto().doubleValue());
					
					if( pcd.getMoneda().compareTo( sMonedaBase) == 0 ){
						
						totalDepositoCaja += iMontoDeps;
						
						bHecho = rcCtrl.registrarAsientoDiarioLogs(session, msgLogs, dtFechaConfirma, sCodSucAsiento, sTipoDoc,
										iNoDocumento, dLineaDocs, NobatchToUse, sCtaTBanco[0], sCtaTBanco[1], sCtaTBanco[3],
										sCtaTBanco[4], sCtaTBanco[5], "AA", pcd.getMoneda(), iMontoDeps*(-1), sConcepto, 
										usuarioPreconcilacion, vaut.getId().getCodapp(), BigDecimal.ZERO, sTipoEmpleado, 
										sDescrip, sCtaTBanco[2], strSubLibroCuenta, tipoAuxiliarCtaTrans, 
										sMonedaBase, sCodSucAsiento , "D", 0);
						if(!bHecho){
							return bHecho;
						}

					}else{
						long iMontoDepsdom = Divisas.pasarAenteroLong( Divisas.roundDouble(dpCaja.getMonto().multiply(bdTasaJDE).doubleValue()));
						totalDomDepsCaja += iMontoDepsdom;
						
						totalDepositoCaja += iMontoDeps;
						
						bHecho = rcCtrl.registrarAsientoDiarioLogs(session, msgLogs, dtFechaConfirma, sCodSucAsiento, sTipoDoc,
									iNoDocumento, dLineaDocs, NobatchToUse, sCtaTBanco[0], sCtaTBanco[1], sCtaTBanco[3],
									sCtaTBanco[4], sCtaTBanco[5], "AA", pcd.getMoneda(), iMontoDepsdom*(-1), sConcepto, 
									usuarioPreconcilacion, vaut.getId().getCodapp(), bdTasaJDE, sTipoEmpleado, 
									sDescrip, sCtaTBanco[2], strSubLibroCuenta, tipoAuxiliarCtaTrans, sMonedaBase, sCodSucAsiento, "F", (iMontoDeps*(-1)) );
						if(!bHecho){
							return bHecho;
						}
						bHecho = rcCtrl.registrarAsientoDiarioLogs(session, msgLogs, dtFechaConfirma, sCodSucAsiento, sTipoDoc,
								iNoDocumento, dLineaDocs, NobatchToUse, sCtaTBanco[0], sCtaTBanco[1], sCtaTBanco[3],
								sCtaTBanco[4], sCtaTBanco[5], "CA", pcd.getMoneda(), iMontoDeps*(-1), sConcepto, 
								usuarioPreconcilacion, vaut.getId().getCodapp(), bdTasaJDE, sTipoEmpleado, 
								sDescrip, sCtaTBanco[2], strSubLibroCuenta, tipoAuxiliarCtaTrans, sMonedaBase, sCodSucAsiento , "F", 0);
						if(!bHecho){
							return bHecho;
						}
					}
					//&& ===============  Actualizar tabla deposito en caja.
					bHecho = cdc.actualizarEstadoDeposito(dpCaja, sEstadoDeposito, sTipoConfirma, String.valueOf( coincidencia.getReferenciabanco() ), codigousr );
					if(!bHecho){
						return bHecho;
					}
				}
				//&& ===============  Registrar lineas para reflejar montos por ajustes.
				double dajuste = 0 ;
				
				if( totalDepositoBanco != totalDepositoCaja  && coincidencia.getMontorporajuste().compareTo(BigDecimal.ZERO) == 0 ){
					coincidencia.setMontorporajuste( 
						new BigDecimal ( Long.toString( totalDepositoBanco - totalDepositoCaja ) ).divide(new BigDecimal("100") )
					);
				}
				
				if( coincidencia.getMontorporajuste().compareTo(BigDecimal.ZERO) != 0 ){
					
					sDescrip ="";
					String sCodEmpleado = "";
					String sTipoAuxiliar = "";
					String[] sCtaTemp = null;
					String sObj = "", sSub = "";
				
					if (coincidencia.getMontorporajuste().compareTo(BigDecimal.ZERO) == 1) {
						String[] cuentaOtrosingresos = DocumuentosTransaccionales.CTAOTROSINGRESOS().split(",");
						
						sObj = cuentaOtrosingresos[1] ;
						sSub = cuentaOtrosingresos[2] ;
						
						// && ============= Buscar la cuenta sobrantes
						final String gmmcu = sUninegCaja.trim();
						final String gmobj = sObj.trim() ;
						final String gmsub = sSub.trim() ;
						
						sCtaTemp = (String[]) CollectionUtils.find( dtaCuentaSobrantePorUnidadNegocio,  new Predicate() {
							public boolean evaluate(Object o) {
								return 
								((String[])o)[3].trim().compareTo(gmmcu) == 0 && 
								((String[])o)[4].trim().compareTo(gmobj) == 0 && 
								((String[])o)[5].trim().compareTo(gmsub) == 0 &&
								((String[])o)[6].trim().compareTo(codcomp) == 0 ;
							}
						});
						
					} else {
						
						//&& ====== validar si el monto del faltante debe cargarse al cajero
						List<String[]> cuentaContableFaltante ;
						boolean cargarFaltanteCajero = true;
						
						final String monedaCoincidencia = coincidencia.getMoneda().trim().toUpperCase();
						List<String[]> montosPermitidos = Arrays.asList(PropertiesSystem.MONTOS_FALTANTES_PERMITIDOS);
						String[] montos = (String[]) CollectionUtils.find(montosPermitidos, new Predicate() {
									public boolean evaluate(Object o) {
										return ((String[]) o)[0].trim().toUpperCase().compareTo(monedaCoincidencia) == 0;
									}
								});
						
						if(montos != null){
							cargarFaltanteCajero = coincidencia.getMontorporajuste().abs().compareTo( new BigDecimal(montos[1]) ) == 1;
						}
						
						if (cargarFaltanteCajero) {
							
							sUninegCaja = DocumuentosTransaccionales.CTADEUDORESVARIOSUNINEG(codcomp.trim());
				
							sObj =DocumuentosTransaccionales.CTADEUDORESVARIOSOB() ;
							sSub = DocumuentosTransaccionales.CTADEUDORESVARIOSSB() ;
							sTipoAuxiliar = DocumuentosTransaccionales.CODIGOTIPOAUXILIARFE();
		
							sCodEmpleado = CodeUtil.pad( String.valueOf(lstDepsCaja.get(0).getCodcajero()),	8, "0");
							
							cuentaContableFaltante  = dtaCuentaFaltantePorUnidadNegocio;
								
						} else {
							String[] cuentaOtrosGastos= DocumuentosTransaccionales.CTAGASTOSDIVERSOS().split(",");
							sObj = cuentaOtrosGastos[1];
							sSub = cuentaOtrosGastos[2];
							sCodEmpleado = "";
							sTipoAuxiliar = "";
	
							cuentaContableFaltante = dtaCuentaOtrosGastosPorUnidadNegocio;
						}
						
						// && ============= Buscar la cuenta de faltantes
						final String gmmcu = sUninegCaja.trim();
						final String gmobj = sObj.trim() ;
						final String gmsub = sSub.trim() ;
						
						sCtaTemp = (String[]) CollectionUtils.find( cuentaContableFaltante,  new Predicate() {
							public boolean evaluate(Object o) {
								
								boolean equal =
										((String[])o)[3].trim().compareTo(gmmcu) == 0 && 
										((String[])o)[4].trim().compareTo(gmobj) == 0 ;
								
								if( !gmsub.isEmpty() ){
									equal  = equal && ((String[])o)[5].trim().compareTo(gmsub) == 0 ;
								}
								
								return equal;
								
							}
						});
					}
					
					if ( coincidencia.getMontorporajuste().compareTo(BigDecimal.ZERO) == 1) {
						dajuste = Double.parseDouble( "-".concat(coincidencia.getMontorporajuste().toString() ) ) ;
						sDescrip = "Sobrante de confirmación";
					}else{
						dajuste = coincidencia.getMontorporajuste().abs().doubleValue();
						sDescrip = "Faltante en confirmación";
					}
					
					//&& ============= cuentas para ajustes por excepcion.
					if(coincidencia.isProcesarAjustePorExcepcion()){
						
						final String gmaidCtaExcepcion = coincidencia.getCuentaAjusteExcepcionId() ;
						
						sCtaTemp = (String[]) CollectionUtils.find( dtaCuentaAjustesPorExcepcion,  new Predicate() {
							public boolean evaluate(Object o) {
								return  ((String[])o)[1].trim().compareTo(gmaidCtaExcepcion) == 0 ;
							}
						});
						
					  sCodEmpleado = "";
					  sTipoAuxiliar = "";
					  sDescrip = "Ajuste a Revision " + contadorUsuario ;
					  sConcepto = contadorUsuario + " > Revision Ajuste" ;
						
					}
					
					if(sCtaTemp == null){
						msgErrorProceso = "VF0901: No se ha podido obtener la cuenta '"+sUninegCaja+"."+sObj+"."+sSub+" en maestro de cuentas";
						return bHecho = false;
					}
					
					//&& ===== Asignacion de cuenta en dependencia del tipo de ajuste.
					sCtaCredAjuste = sCtaTemp;
					
					//&& ============= Generacion de lineas de asientos.
					iMontoDeps = Divisas.pasarAenteroLong(dajuste);
					dLineaDocs += 1;
					
					msgLogs = sDescrip;
					
					if(pcd.getMoneda().compareTo(sMonedaBase) == 0){
						
						bHecho = rcCtrl.registrarAsientoDiarioLogs(session, msgLogs, dtFechaConfirma, sCodSucAsiento, sTipoDoc,
								iNoDocumento, dLineaDocs, NobatchToUse, sCtaCredAjuste[0], sCtaCredAjuste[1], sCtaCredAjuste[3],
								sCtaCredAjuste[4],sCtaCredAjuste[5], "AA", pcd.getMoneda(), iMontoDeps, sConcepto,
								usuarioPreconcilacion, vaut.getId().getCodapp(), BigDecimal.ZERO, sTipoEmpleado, sDescrip, 
								sCtaCredAjuste[2], sCodEmpleado, sTipoAuxiliar , sMonedaBase, sCodSucAsiento, "D" , 0);
						if(!bHecho){
							 
							return bHecho;
						}
					}else{
						
						long iMontoDepsdom = totalDomDepsBanco - totalDomDepsCaja;

						iMontoDepsdom = (iMontoDepsdom > 0)? Integer.parseInt("-"+iMontoDepsdom) : Math.abs(iMontoDepsdom) ;
						
						bHecho = rcCtrl.registrarAsientoDiarioLogs(session, msgLogs, dtFechaConfirma, sCodSucAsiento, sTipoDoc,
								iNoDocumento, dLineaDocs, NobatchToUse, sCtaCredAjuste[0], sCtaCredAjuste[1], sCtaCredAjuste[3],
								sCtaCredAjuste[4], sCtaCredAjuste[5], "AA", pcd.getMoneda(), iMontoDepsdom, sConcepto, 
								usuarioPreconcilacion, vaut.getId().getCodapp(), bdTasaJDE, sTipoEmpleado, sDescrip, 
								sCtaCredAjuste[2], sCodEmpleado, sTipoAuxiliar, sMonedaBase,sCodSucAsiento, "F", iMontoDeps);
						if(!bHecho){
							 
							return bHecho;
						}
						// ------------ 
						bHecho = rcCtrl.registrarAsientoDiarioLogs(session, msgLogs, dtFechaConfirma, sCodSucAsiento, sTipoDoc,
								iNoDocumento, dLineaDocs, NobatchToUse, sCtaCredAjuste[0], sCtaCredAjuste[1], sCtaCredAjuste[3],
								sCtaCredAjuste[4], sCtaCredAjuste[5], "CA", pcd.getMoneda(), iMontoDeps, sConcepto, 
								usuarioPreconcilacion, vaut.getId().getCodapp(), bdTasaJDE, sTipoEmpleado, sDescrip,
								sCtaCredAjuste[2], sCodEmpleado, sTipoAuxiliar, sMonedaBase, sCodSucAsiento, "F", 0 );
						if(!bHecho){
							 
							return bHecho;
						}
					}
				}
				/***********************  ===== Realizar transacciones en modulo de caja. ======= *****************************/
				List<String>queries = new ArrayList<String>();
				String queryString = "";
				
				//&& ==================== 1 Actualizar el registro del consolidado.
				queryString = "update "+PropertiesSystem.ESQUEMA+".pcd_consolidado_depositos_banco " +
					"set referenciajde = "+ iNoDocumento +", montoajustado = " + coincidencia.getMontoAjustado()+"," +
					" estadoconfirmacion = 1, usuarioactualiza = "+ codigousr +", " +
					" usuarioultimacomparacion = " + codigousr +", fechamodconsolida = current_timestamp,  " +
					" numerobatch = " + NobatchToUse + ", tipodocumentojde = '"+sTipoDoc+"'" +
					" where idresumenbanco = " + coincidencia.getIdresumenbanco();
				queries.add(queryString); 		
				
				//&& ==================== 2 Actualizar estado del deposito de banco.
				queryString = " update "+PropertiesSystem.ESQUEMA+".Depbancodet set idtipoconfirm = "+iTipoConfirma +
					  ", idestadocnfr = "+ 35 +", fechamod = current_timestamp, usrmod = " +
					  codigousr +", referencia = "+iNoDocumento +", historicomod =' ' " +
					 "where iddepbcodet = "+ coincidencia.getIddepbcodet() +" and idarchivo = "+ idarchivo ;
				queries.add(queryString); 
				
				//&& ==================== 3 Actualizar estado del archivo.
				queryString = " update "+PropertiesSystem.ESQUEMA 
						+ ".Archivo set  usrmodi = " +codigousr +", depositosrestantes = (depositosrestantes - 1) " 
						+", fechamod = current_timestamp  where idarchivo = "+ idarchivo ;
				queries.add(queryString); 
				
				//&& ==================== 4 Crear el registro de conciliacion.
				String fechahora = FechasUtil.formatDatetoString(new Date(), "yyyy-MM-dd HH:mm:ss.SSSSSS" );
				
				queryString =  "insert into " + PropertiesSystem.ESQUEMA+".CONCILIACION "+
			        "(IDCONCILIACION, IDARCHIVO, MONTO, NOBATCH, NOREFERENCIA, MONEDA, " +
			        "TIPODOC, IDCUENTA, ESTADO, USRCREA, USRMOD, FECHAMOD, FECHACREA, " +
			        "RNOBATCH, RNODOC,USUARIOCOMPROBANTE, CODIGOUSRCOMPROBANTE ) values " +
			        "(default, "+idarchivo+","+coincidencia.getMontoBanco()+"," +
			         NobatchToUse+", "+iNoDocumento+", '"+coincidencia.getMoneda()+"','"+sTipoDoc+"'," +
			         numerodecuenta+", "+46+", "+codigousr+", "+codigousr+
			         ", '"+fechahora+"', '"+fechahora+"' , 0 ,0, '"+contadorUsuario+"'," + contadorCodigo + " )";
				
				queries.add(queryString); 

				String ssIdConcilia = " ( select idconciliacion from conciliacion where fechacrea = '"
								+fechahora+"' and noreferencia = "+iNoDocumento +")" ;
				
				//&& ==================== 5 Crear el registro detalle de la conciliacion.
				for (Deposito_Report dp : lstDepsCaja) {
					
					queryString = 
					"insert into " + PropertiesSystem.ESQUEMA+".CONCILIADET "+
					"(IDCONCILIADET, IDDEPOSITOBCO, REFERDCAJA, IDCONCILIACION,  "+
					"NODEPOSITO,CODSUC,CODCOMP,CAID,USRCREA,USRMOD,FECHAMOD, "+
					"FECHACREA,IDDEPOSITOCJA) values "+
					"(default, "+coincidencia.getIddepbcodet()+", " + dp.getReferencenumber()+", "+
					ssIdConcilia+", "+dp.getNodeposito()+", '"+
					dp.getCodsuc()+"', '"+dp.getCodcomp()+"', "+dp.getCaid()+", "+
					codigousr+", "+codigousr
					+", current_timestamp, current_timestamp, "+ dp.getConsecutivo() +")" ;
					
					queries.add(queryString); 
				}
				
				//&& ==================== 6 Guardar el detalle del ajuste, en caso de haberse generado.
				
				if( coincidencia.getMontorporajuste().compareTo(BigDecimal.ZERO) != 0 ){
					int iCodCargo = (coincidencia.getMontorporajuste().compareTo(BigDecimal.ZERO) == 1 )? 0 : lstDepsCaja.get(0).getCodcajero(); 
					queryString = 
					" insert into "+PropertiesSystem.ESQUEMA+".AJUSTECONC " +
					"(IDAJUSTEC, IDCONCILIACION, NOBATCH, NODOCUMENTO, MONTO, CARGOA, IDCUENTA, " +
					" IDTIPOAJUSTE, TIPODOC, USRCREA, USRMOD, FECHAMOD, FECHACREA, ESTADO)"+ 
					" values " +
					" ( default,"+ssIdConcilia+", "+NobatchToUse+", "+iNoDocumento
						+", '"+ dajuste +"', "+iCodCargo+","+sCtaCredAjuste[1]+" , "
						+ ( (dajuste > 0 )? 49 : 48 )+ ",'"
						+sTipoDoc+"', "+ codigousr +", "+ codigousr
						+", current_timestamp, current_timestamp,   46  ) ";
					
					queries.add(queryString); 
				}
				
				int rowsAffected = 0;
				int iQueryIndex = 0;
				for (int i = 0; i < queries.size(); i++) {
					iQueryIndex = i;
					
					try {
						rowsAffected  = session.createSQLQuery(queries.get(i)).executeUpdate();
						
						if(rowsAffected == 0){
							msgErrorProceso = "@Consulta no afecta registros ";
							bHecho = false;
							break;
						}
						
					} catch (Exception e) {
						msgErrorProceso = "@Consulta con errores ";
						bHecho = false;
						e.printStackTrace();
						break;
					}
				}
				
				/*
				//&& ========== Contabilizar batch
				String strUpdatePgmJdeContabiliza =
				" update "+PropertiesSystem.JDECOM+".f98301 set DESVL = " + NobatchToUse +
				" WHERE DEPID ='P09800' and DEVERS ='PRE_CONCIL' AND DEFLDN='GLICU' " ;      
				
				rowsAffected  = sesionForQuery.createSQLQuery(strUpdatePgmJdeContabiliza).executeUpdate();
				*/
				
				
				
			} catch (Exception e) {
				e.printStackTrace();
				bHecho = false;
				msgErrorProceso = "@Error de sistema al realizar confirmacion del deposito ";
			}finally{
			
				 try {
						
					 if( bHecho ){
						 transaction.commit();
					 }else{
						 transaction.rollback();
					 }
					 
				} catch (Exception e) {
					e.printStackTrace();
				}
				 
				try {
					HibernateUtilPruebaCn.closeSession(session);
				} catch (Exception e) {
					e.printStackTrace();
				}
				 
			}
			return bHecho;
		}

//**************************************************************************************************************************************//
	public static String getMsgErrorProceso() {
		return msgErrorProceso;
	}
	public static void setMsgErrorProceso(String msgErrorProceso) {
		ProcesarConsolidadoDepositos.msgErrorProceso = msgErrorProceso;
	}
	public static boolean getConnection(){
		boolean connected = true;
		
		try {
			
			sesionForQuery = HibernateUtilPruebaCn.currentSession();
			transForQuery = (connectionActive = !(sesionForQuery.getTransaction().isActive())) ?
					sesionForQuery.beginTransaction() :
					sesionForQuery.getTransaction();
			
		} catch (Exception e) {
			e.printStackTrace(); 
			return connected  = false;
		}
		return connected;
	}
	public static boolean closeConnection(boolean committransaction){
		boolean connectionClosed = true;
		
		try {
			
			if(transForQuery == null){
				String origen = PropertiesSystem.CONTEXT_NAME+": "
						+ new Exception().getStackTrace()[1].getClassName() +":"
						+ new Exception().getStackTrace()[1].getMethodName() ;
			}
			
			if( connectionActive && transForQuery != null && sesionForQuery != null ){
				try {
					if(committransaction)
						transForQuery.commit();
					else
						transForQuery.rollback();
				}catch (Exception e2) {
					e2.printStackTrace(); 
				}
				try {  
					HibernateUtilPruebaCn.closeSession(sesionForQuery); 
				}
				catch (Exception e2) {
					e2.printStackTrace(); 
				}
			}
			sesionForQuery = null;
			transForQuery = null;
			
		} catch (Exception e) {
			e.printStackTrace(); 
			connectionClosed = false;
		}
		return connectionClosed ;
	}
	 
	public static boolean isConnectionActive() {
		return connectionActive;
	}

	public static void setConnectionActive(boolean connectionActive) {
		ProcesarConsolidadoDepositos.connectionActive = connectionActive;
	}

	public static int getNobatchToUse() {
		return NobatchToUse;
	}

	public static void setNobatchToUse(int nobatchToUse) {
		NobatchToUse = nobatchToUse;
	}

	public static String getMsgFromConnection() {
		return msgFromConnection;
	}

	public static void setMsgFromConnection(String msgFromConnection) {
		ProcesarConsolidadoDepositos.msgFromConnection = msgFromConnection;
	}
}

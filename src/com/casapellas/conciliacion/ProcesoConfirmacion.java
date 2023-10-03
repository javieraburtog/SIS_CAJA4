package com.casapellas.conciliacion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.hibernate.Session;

import com.casapellas.conciliacion.entidades.Conciliacion;
import com.casapellas.conciliacion.entidades.PcdConsolidadoDepositosBanco;
import com.casapellas.controles.CompaniaCtrl;
import com.casapellas.controles.ConfirmaDepositosCtrl;
import com.casapellas.controles.CtrlCajas;
import com.casapellas.controles.MetodosPagoCtrl;
import com.casapellas.controles.ReciboCtrl;
import com.casapellas.controles.TasaCambioCtrl;
import com.casapellas.conciliacion.entidades.Archivo;
import com.casapellas.conciliacion.entidades.Conciliadet;
import com.casapellas.conciliacion.entidades.Depbancodet;
import com.casapellas.entidades.Deposito;
import com.casapellas.entidades.F55ca014;
import com.casapellas.navegacion.As400Connection;
import com.casapellas.entidades.Vf0901;
import com.casapellas.entidades.ens.Vautoriz;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.Divisas;
import com.casapellas.util.PropertiesSystem;

/**
 * CASA PELLAS S.A.
 * Creado por.........: Carlos Manuel Hernández Morrison
 * Fecha de Creación..: 30/08/2011
 * Última modificación: Carlos Manuel Hernández Morrison
 * Modificado por.....:	12/10/2010
 * Descripción:.......: Proceso para la confirmacion de depositos. 
 */

public class ProcesoConfirmacion {
	Map<String, Object> m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	public Exception error;
	public Exception errorDetalle;
	
/******************************************************************************************/
/** Método: Anular la confirmacion de depositos registradas.
 *	Fecha:  06/10/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public boolean revertirConfirmacionDepositos(int idConciliacion, 
			Vautoriz vaut, String sTipoEmpleado, Date dtFechaAsiento,
			int iNuevaRefer) {
		
		boolean bHecho = true;
		ReciboCtrl rcCtrl = new ReciboCtrl();
		ConfirmaDepositosCtrl cdc = new ConfirmaDepositosCtrl();
		Divisas dv = new Divisas();
		int[] iNobatchNodoc = new int[]{0,0};
		String sConcepto="";
		String sLogin="";
		int iNobatchConfirm=0;
		int iNoDocsConfirma=0;
		int coduser = 0;
		
		
		try {
			
			coduser = vaut.getId().getCodreg();
			
			ProcesarConsolidadoDepositos.getConnection();
			
			
			String queryString = "select * from " + PropertiesSystem.ESQUEMA
					+".conciliacion where idconciliacion = " + idConciliacion ;
			
			Conciliacion confirmacion = (Conciliacion) ProcesarConsolidadoDepositos
					.sesionForQuery.createSQLQuery(queryString).addEntity(Conciliacion.class).uniqueResult() ;
			
			
			//&& =========== Hacer batch para la reversion, con movimientos inversos.
			if(m.get("ccd_HacerReversion") != null && String.valueOf(m.get("ccd_HacerReversion")).compareTo("1")==0 ){
				sConcepto = "Reversion Deps No: "+confirmacion.getNoreferencia();
				iNobatchConfirm = confirmacion.getNobatch();
				iNoDocsConfirma = confirmacion.getNoreferencia();
				
				List<Conciliadet>lstConciliaDets = new ArrayList<Conciliadet>(confirmacion.getConciliadets());
				Deposito dpCaja  = lstConciliaDets.get(0).getDeposito();
				sLogin = dpCaja.getCoduser();
				
				//&& ========== Transacciones en JDE para la reversion.
				iNobatchNodoc = dv.obtenerNobatchNodoco();
				if(iNobatchNodoc==null){
					error = new Exception("@No se pudo obtener numero de batch y documento para el asiento de diario");
					return false;
				}
				//&& ============  Guardar el numero de batch a utilizar.
				m.put("ccd_Nobatch", iNobatchNodoc[0]);
				
				//&& ============ Encabezado del asiento de diario para reversion.
				int iMontoTotal = dv.pasarAentero(confirmacion.getMonto().doubleValue());
				bHecho = rcCtrl.registrarBatchA92(dtFechaAsiento, ProcesarConsolidadoDepositos
						.sesionForQuery,"G", iNobatchNodoc[0], iMontoTotal, sLogin, 1, MetodosPagoCtrl.DEPOSITO, "");
				if(!bHecho){
					error = rcCtrl.getError();
					return false;
				}
				//&& ============ Generar lineas de asientos de diario inversas a las de la confirmacion.
				List<Object[]>lstDetalle = cdc.obtenerLineasF0911(iNobatchConfirm, iNoDocsConfirma );
				if(lstDetalle == null){
					errorDetalle = cdc.getErrorDetalle();
					error = cdc.getError();
					return false;
				}
				String sSucAsiento = "";
				for (Object[] obCampos : lstDetalle) {
					String GLCO	= String.valueOf(obCampos[3]).trim();
					double dLineaDocs = Double.valueOf(String.valueOf(obCampos[2]));
					if(dLineaDocs == 2.0){
						sSucAsiento = GLCO.substring(GLCO.length()-2,GLCO.length());
						break;
					}
				}
				for (Object[] obCampos : lstDetalle) {
					String GLKCO	= String.valueOf(obCampos[0]).trim(); 
					String GLDCT	= String.valueOf(obCampos[1]).trim();
					String GLCO		= String.valueOf(obCampos[3]).trim();
					String GLANI	= String.valueOf(obCampos[4]).trim();
					String GLAID	= String.valueOf(obCampos[5]).trim();
					String GLMCU	= String.valueOf(obCampos[6]).trim();
					String GLOBJ	= String.valueOf(obCampos[7]).trim();
					String GLSUB	= String.valueOf(obCampos[8]).trim();
					String GLLT		= String.valueOf(obCampos[9]).trim();
					String GLCRCD	= String.valueOf(obCampos[10]).trim();
					String GLEXR	= String.valueOf(obCampos[13]).trim();
					String GLCRRM   = String.valueOf(obCampos[14]).trim();
					String GLSBL 	= String.valueOf(obCampos[15]).trim();
					String GlSBLT	= String.valueOf(obCampos[16]).trim();
					
					double dLineaDocs = Double.valueOf(String.valueOf(obCampos[2]));
					BigDecimal bdTasa = new BigDecimal(String.valueOf(obCampos[11]));
					int iMonto		  = Integer.valueOf(String.valueOf(obCampos[12]));
					
					GLKCO = GLKCO.substring(GLKCO.length()-2,GLKCO.length());
					GLCO = GLCO.substring(GLCO.length()-2,GLCO.length());
					
					bHecho = rcCtrl.registrarAsientoDiario(dtFechaAsiento, ProcesarConsolidadoDepositos.sesionForQuery
							, sSucAsiento, GLDCT, iNobatchNodoc[1], 
											dLineaDocs, iNobatchNodoc[0], GLANI, GLAID, GLMCU,
											GLOBJ, GLSUB, GLLT, GLCRCD, iMonto*(-1), sConcepto,
											sLogin, vaut.getId().getCodapp(), bdTasa, sTipoEmpleado, 
											GLEXR, GLCO, GLSBL, GlSBLT, GLCRCD, GLCO, GLCRRM);
					if(!bHecho){
						errorDetalle = rcCtrl.getErrorDetalle();
						error = rcCtrl.getError();
						return false;
					}
				}
			}
			//&& =========== Borrar los asientos de diario anteriores.
			else{
				//bHecho = rcCtrl.borrarAsientodeDiario(cn, confirmacion.getNoreferencia(), confirmacion.getNobatch());
				if(!bHecho){
					error = cdc.getError();
					errorDetalle = cdc.getErrorDetalle();
					return false;
				}
				//bHecho = rcCtrl.borrarBatch(cn,  confirmacion.getNobatch(),"G");
				if(!bHecho){
					error = cdc.getError();
					errorDetalle = cdc.getErrorDetalle();
					return false;
				}
			}
			
			//&& ================================= Actualizaciones del modulo de caja.
			//&& ======== 1. Actualizar conciliacion (detalle conciliacion conciliadet).
			//&& ======== 2. Actualizar Ajustes en conciliacion.
			//&& ======== 3. Actualizar Consolidado Depositos.
			//&& ======== 4. Actualizar Depositos de caja.
			//&& ======== 5. Actualizar Detalle de estado de cuenta (depbcodet)
			//&& ======== 6. Actualizar Archivo (estado de cuentas)
			//&& =================================
			
			List<String>queries = new ArrayList<String>();
			
			queryString = "select * from " + PropertiesSystem.ESQUEMA 
					+".pcd_consolidado_depositos_banco where iddepbcodet = " 
					+ new ArrayList<Conciliadet>(confirmacion.getConciliadets()).get(0).getDepbancodet().getIddepbcodet()
					+ " fetch first rows only ";
			
			PcdConsolidadoDepositosBanco cd = (PcdConsolidadoDepositosBanco)
					ProcesarConsolidadoDepositos.sesionForQuery.createSQLQuery(queryString)
					.addEntity(PcdConsolidadoDepositosBanco.class).uniqueResult() ;
			
			//&& ======== 1. Actualizar conciliacion y detalle conciliacion.
			queryString = " update " + PropertiesSystem.ESQUEMA
					+".Conciliacion set estado = 47, usrmod = " 
					+ coduser +", fechamod = current_timestamp," 
					+ " rnobatch = " +iNobatchNodoc[0] +", rnodoc = "
					+ iNobatchNodoc[1] +" where idconciliacion = " 
					+confirmacion.getIdconciliacion() ;
			queries.add(queryString);
			
			queryString = " update " + PropertiesSystem.ESQUEMA
					+".Conciliadet set usrmod =  " + coduser + ", " 
					+" fechamod = current_timestamp where idconciliacion = " 
					+ confirmacion.getIdconciliacion() ;
			queries.add(queryString);
			
			//&& ======== 2. Actualizar Ajustes en conciliacion.
			if(cd.getMontoajustado().compareTo(cd.getMontooriginal()) != 0){
				queryString = " update " + PropertiesSystem.ESQUEMA
						+".Ajusteconc set fechamod = current_timestamp, "
						+" usrmod = " + coduser +", estado = 47 where idconciliacion = " 
						+ confirmacion.getIdconciliacion() ;
				queries.add(queryString);
			}
			
			//&& ======== 3. Actualizar Consolidado Depositos.
			queryString = "update "+PropertiesSystem.ESQUEMA+".pcd_consolidado_depositos_banco " +
					"set estadoconfirmacion = 0, usuarioactualiza = "+ coduser +
					", usuarioultimacomparacion = " + coduser +", fechamodconsolida = current_timestamp,  " +
					" numerobatch = " + 0 + " where idresumenbanco = " + cd.getIdresumenbanco();
			queries.add(queryString);
			
			//&& ======== 4. Actualizar Depositos de caja.
			queryString = "";
			
			for(Conciliadet cdd : confirmacion.getConciliadets() ){
				queryString += cdd.getDeposito().getConsecutivo() +",";
			}queryString = queryString.trim();
			queryString = "( " + queryString.substring(0, queryString.length() - 1)+ " )" ;
			
			queryString = " update "+PropertiesSystem.ESQUEMA+".Deposito set estadocnfr = 'SCR', fechamod = current_date, "
					+ " horamod = current_time, usrconfr = " + coduser +" where consecutivo in " + queryString ;
			queries.add(queryString);
			
			//&& ======== 5. Actualizar Detalle de estado de cuenta (depbcodet)
			queryString = " update "+PropertiesSystem.ESQUEMA+".Depbancodet set idestadocnfr = 36," +
					" idtipoconfirm = 32, fechamod = current_timestamp," +
					" referencia = " + cd.getReferenciaoriginal() + ", usrmod = " 
					+ coduser +", historicomod = '' where iddepbcodet = " + cd.getIddepbcodet() ; 
			queries.add(queryString);
					 
			//&& ======== 6. Actualizar Archivo (estado de cuentas)		
			queryString = " update "+PropertiesSystem.ESQUEMA 
					+ ".Archivo set  usrmodi = " + coduser  +", depositosrestantes = (depositosrestantes + 1) "
					+", fechamod = current_timestamp where idarchivo = "+confirmacion.getArchivo().getIdarchivo() ;
			queries.add(queryString);

			//&& ================== Ejecutar las consultas .
			String msgErrorProceso = "";
			int rowsAffected = 0;
			int iQueryIndex = 0;
			
			
			for (int i = 0; i < queries.size(); i++) {
				iQueryIndex = i;
				
				
				try {
					rowsAffected  = ProcesarConsolidadoDepositos.sesionForQuery.createSQLQuery(queries.get(i)).executeUpdate();
					
					if(rowsAffected == 0){
						msgErrorProceso = " Actualizacion de Indice  ("+i+") no afecta registros ";
						break;
					}
					
				} catch (Exception e) {
					msgErrorProceso = "Consulta con errores ";
					bHecho = false;
					e.printStackTrace();
					break;
				}
			}
			ProcesarConsolidadoDepositos.closeConnection(bHecho);
			
			if( !bHecho ) {
				error = new Exception("@ Error en actualizacion de datos Caja : "+ msgErrorProceso );
				return false;
			}
			
			
		} catch (Exception e) {
			bHecho = false;
			errorDetalle = new Exception("@Mensaje de error: "+e);
			error = new Exception("@Error de sistema al realizar reversion sobre confirmacion de depositos caja banco ");
			e.printStackTrace();
		}finally{
			try {				
				
				ProcesarConsolidadoDepositos.closeConnection( bHecho );
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return bHecho;
	}
	
/******************************************************************************************/
/** Método: buscar registros Deposito, de depositos de caja a partir de los parametros obtenidos.
 *	Fecha:  23/08/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public boolean realizarConfirmacionDepositos(Archivo ar,List<Deposito> lstDepsCaja, Depbancodet depBanco,
								Session sesionCajaR, Session sesionCajaW1, Connection cn,
								Vautoriz vaut,String sTipoEmpleado, double dAjuste,
								int iTipoConfirma, int iEstadoDeposito, 
								Date dtFechaConfirma, int iDigConfrma ){
		
		boolean bHecho = true;
		ReciboCtrl rcCtrl = new ReciboCtrl();
		String[] sCtaTBanco, sCtaBco;
		String[] sCtaCredAjuste = null;
		Divisas dv = new Divisas();
		int iNoBatch=0;
		int iNoDocum=0;
		String sTipoDoc = PropertiesSystem.TIPODOC_REFER_ZX;
		String sConcepto = "";
		String sCodSucAsiento = "";
		double dLineaDocs = 1.0;
		int iMontoTotal=0;
		int iMontoDeps=0;
		String sMonedaBase= PropertiesSystem.MONEDA_BASE ;
		String sLogin="";
		String sEstadoDeposito="";
		String sTipoConfirma = "";
		ConfirmaDepositosCtrl cdc = new ConfirmaDepositosCtrl();
		BigDecimal bdTasaJDE = null;
		String sUninegCaja = PropertiesSystem.UNIDAD_NEGOCIO_BASE  ;
		boolean bHayCambioRefer = false;
		
		try {
			//&& ====== Establecer el tipo de confirmacion y estado del deposito de caja y deposito de banco
			switch (iTipoConfirma) {
			case 32:
				sTipoConfirma = PropertiesSystem.CFR_AUTO  ;
				break;
			case 34:
				sTipoConfirma = PropertiesSystem.CFR_MANUAL  ;
				break;
			}
			switch (iEstadoDeposito) {
			case 35:
				sEstadoDeposito = PropertiesSystem.DP_CONFIRMADO  ;
				break;
			case 36:
				sEstadoDeposito = PropertiesSystem.DP_NOCONFIRMADO;
				break;
			}
			
			m.remove("cdb_CambioReferencia"); 
			m.remove("pcd_Nobatch");
			
			/***********************  ===== Realizar transacciones en Edward's. ======= *****************************/
			sConcepto   = "Confirmacion Deps No: "+depBanco.getReferencia();
			sLogin      = lstDepsCaja.get(0).getCoduser();	
			
			F55ca014 f14DtCaja = CompaniaCtrl.obtenerF55ca014(lstDepsCaja.get(0).getCaid(), lstDepsCaja.get(0).getCodcomp());
			if(f14DtCaja == null){
				error = new Exception("@F55CA014 No se encontró configuración para caja/compania: "
										+lstDepsCaja.get(0).getCaid()+"/"+lstDepsCaja.get(0).getCodcomp());
				return false;
			}
			sUninegCaja =  f14DtCaja.getId().getC4cjmcu().trim();
			if( sUninegCaja.matches("^[0]{1,4}$"))
				sUninegCaja = PropertiesSystem.UNIDAD_NEGOCIO_BASE  ;
			
			iNoBatch = dv.leerActualizarNoBatch();
			if(iNoBatch==0){
				error = new Exception("@No se pudo obtener numero de batch o documento para el registro");
				return false;
			}
			//&& ==== Guardar el numero de batch a utilizar.
			m.put("pcd_Nobatch", iNoBatch);
			
			String sRef = String.valueOf(depBanco.getReferencia());
			if(sRef.length() > iDigConfrma)
				sRef = sRef.substring(sRef.length()- iDigConfrma, sRef.length());
			iNoDocum = Integer.parseInt(sRef);
			
			//&& ======= Guardar encabezado de asientos de diario F0011
			iMontoTotal = dv.pasarAentero(depBanco.getMtocredito().doubleValue());
			bHecho = rcCtrl.registrarBatchA92(dtFechaConfirma, sesionCajaR,"G", iNoBatch, iMontoTotal, sLogin, 1, MetodosPagoCtrl.DEPOSITO, "");
			if(!bHecho){
				error = rcCtrl.getError();
				return false;
			}
//			sCtaBco = dv.obtenerCtaBancoxNoCta(depBanco.getNocuenta(), ar.getIdbanco());
			sCtaBco = dv.obtenerCtaBancoxNoCta(depBanco.getNocuenta() );
			if (sCtaBco == null) {
				error = dv.getError();
				return false;
			}
			sCodSucAsiento = sCtaBco[2];
			sMonedaBase = CompaniaCtrl.obtenerMonedaBasexComp(sCtaBco[6]);
			if(sMonedaBase.equals("")){
				error = new Exception("@F55CA014: No se ha podido obtener la moneda base para la compania "+sCtaBco[6]);
				return false;
			}
			
			// && ======= Oct 17, 2012: realizarConfirmacionDepositos: Validacion de # de referencia. 
			// Obtener un numero de referencia que no se repita.
			int iReferOriginal = iNoDocum;
			boolean bExiste = true;
			bHayCambioRefer = false;
			while (bExiste) {
				bExiste = CtrlCajas.validarReferenciaJDE(iNoDocum, sTipoDoc, sCodSucAsiento, sesionCajaR);
				if(bExiste){
					bHayCambioRefer = true;
					sTipoDoc = PropertiesSystem.TIPODOC_REFER_ZZ;
					iNoDocum = dv.leerActualizarNoDocJDE();
				}
			}
			if(bHayCambioRefer)
				m.put("cdb_CambioReferencia", iReferOriginal+"@"+iNoDocum);

			//&& ====== Grabar la primera linea del asiento de diario por el total notificado por banco.
			String sDescrip = "Depósito de banco "+iReferOriginal;
			if(ar.getMoneda().equals(sMonedaBase)){
				bHecho = rcCtrl.registrarAsientoDiario(dtFechaConfirma, sesionCajaR, sCodSucAsiento, sTipoDoc,
										iNoDocum, dLineaDocs, iNoBatch, sCtaBco[0], sCtaBco[1], sCtaBco[3],
										sCtaBco[4],sCtaBco[5], "AA", ar.getMoneda(), iMontoTotal, sConcepto,
										sLogin, vaut.getId().getCodapp(), new BigDecimal(0), sTipoEmpleado, 
										sDescrip, sCtaBco[2], "", "", ar.getMoneda(), sCtaBco[2], "D");
				if(!bHecho){
					error = rcCtrl.getError();
					return false;
				}
			}else{
				
				bdTasaJDE =   TasaCambioCtrl.obtenerTasaJDExFecha (ar.getMoneda(), sMonedaBase, dtFechaConfirma);
				
				int iMontoDepsdom = dv.pasarAentero( dv.roundDouble(depBanco.getMtocredito().multiply(bdTasaJDE).doubleValue()));
				
				bHecho = rcCtrl.registrarAsientoDiario(dtFechaConfirma, sesionCajaR, sCodSucAsiento, sTipoDoc,
										iNoDocum, dLineaDocs, iNoBatch, sCtaBco[0], sCtaBco[1], sCtaBco[3],
										sCtaBco[4],sCtaBco[5], "AA", ar.getMoneda(), iMontoDepsdom, sConcepto,
										sLogin, vaut.getId().getCodapp(), bdTasaJDE, sTipoEmpleado, 
										sDescrip, sCtaBco[2], "", "", sMonedaBase, sCtaBco[2], "F");
				if(!bHecho){
					error = rcCtrl.getError();
					return false;
				}
				bHecho = rcCtrl.registrarAsientoDiario(dtFechaConfirma, sesionCajaR, sCodSucAsiento, sTipoDoc,
										iNoDocum, dLineaDocs, iNoBatch, sCtaBco[0], sCtaBco[1], sCtaBco[3],
										sCtaBco[4],sCtaBco[5], "CA", ar.getMoneda(), iMontoTotal, sConcepto,
										sLogin, vaut.getId().getCodapp(), bdTasaJDE, sTipoEmpleado, 
										sDescrip, sCtaBco[2], "", "", ar.getMoneda(), sCtaBco[2], "F");
				if(!bHecho){
					error = rcCtrl.getError();
					return false;
				}
			}
			
			//&& ===== crear el numero que se va aplicar como sublibro a la cuenta subsidiara.
			String tipoAuxiliarCtaTrans = PropertiesSystem.CODIGO_TIPO_AUXILIAR_CT;
			String strSubLibroCuenta =  ConfirmaDepositosCtrl.constructSubLibroCtaTbanco
					(depBanco.getNocuenta(),0, lstDepsCaja.get(0).getCaid(), "", "");
			
			//&& ===== Recorrer depositos de caja para generar las lineas de los asientos de diario.
			for (int i = 0; i < lstDepsCaja.size(); i++) {
				dLineaDocs++;
				Deposito dpCaja = lstDepsCaja.get(i);
				
				sCtaTBanco = dv.obtenerCuentaTransitoBanco(ar.getMoneda(), dpCaja.getCodcomp(), dpCaja.getIdbanco(), sesionCajaR);
				if(sCtaTBanco == null){
					error = dv.getError();
					return false;
				}
				sDescrip = "Dp:"+dpCaja.getConsecutivo()+" Rf: "+dpCaja.getReferencia() +" Cja "+dpCaja.getCaid() ;
				iMontoDeps = dv.pasarAentero(dpCaja.getMonto().doubleValue());
				
				if(ar.getMoneda().equals(sMonedaBase)){
					bHecho = rcCtrl.registrarAsientoDiario(dtFechaConfirma, sesionCajaR, sCodSucAsiento, sTipoDoc,
									iNoDocum, dLineaDocs, iNoBatch, sCtaTBanco[0], sCtaTBanco[1], sCtaTBanco[3],
									sCtaTBanco[4], sCtaTBanco[5], "AA", ar.getMoneda(), iMontoDeps*(-1), sConcepto, 
									sLogin, vaut.getId().getCodapp(), new BigDecimal(0), sTipoEmpleado, 
									sDescrip, sCtaTBanco[2], strSubLibroCuenta, tipoAuxiliarCtaTrans, ar.getMoneda(), sCtaTBanco[2], "D");
					if(!bHecho){
						error = rcCtrl.getError();
						return false;
					}

				}else{
					int iMontoDepsdom = dv.pasarAentero( dv.roundDouble(dpCaja.getMonto().multiply(bdTasaJDE).doubleValue()));
					
					bHecho = rcCtrl.registrarAsientoDiario(dtFechaConfirma, sesionCajaR, sCodSucAsiento, sTipoDoc,
									iNoDocum, dLineaDocs, iNoBatch, sCtaTBanco[0], sCtaTBanco[1], sCtaTBanco[3],
									sCtaTBanco[4], sCtaTBanco[5], "AA", ar.getMoneda(), iMontoDepsdom*(-1), sConcepto, 
									sLogin, vaut.getId().getCodapp(), bdTasaJDE, sTipoEmpleado, 
									sDescrip, sCtaTBanco[2], strSubLibroCuenta, tipoAuxiliarCtaTrans, sMonedaBase, sCtaTBanco[2], "F");
					if(!bHecho){
						error = rcCtrl.getError();
						return false;
					}
					bHecho = rcCtrl.registrarAsientoDiario(dtFechaConfirma, sesionCajaR, sCodSucAsiento, sTipoDoc,
									iNoDocum, dLineaDocs, iNoBatch, sCtaTBanco[0], sCtaTBanco[1], sCtaTBanco[3],
									sCtaTBanco[4], sCtaTBanco[5], "CA", ar.getMoneda(), iMontoDeps*(-1), sConcepto, 
									sLogin, vaut.getId().getCodapp(), bdTasaJDE, sTipoEmpleado, 
									sDescrip, sCtaTBanco[2], strSubLibroCuenta, tipoAuxiliarCtaTrans, ar.getMoneda(), sCtaTBanco[2], "F");
					if(!bHecho){
						error = rcCtrl.getError();
						return false;
					}
				}
				//&& ===============  Actualizar tabla deposito en caja.
				bHecho = cdc.actualizarEstadoDeposito(dpCaja, sEstadoDeposito, sTipoConfirma,  String.valueOf(depBanco.getReferencia()), vaut.getId().getCodreg() );
				if(!bHecho){
					error = cdc.getError();
					errorDetalle = cdc.getErrorDetalle();
					return false;
				}
			}
			//&& ===============  Registrar lineas para reflejar montos por ajustes.
			if(dAjuste != 0){ 
				sDescrip ="";
				String sCodEmpleado = "";
				String sTipoAuxiliar = "";
				String[] sCtaTemp;
				String sObj = "", sSub = "";
			
				if (dAjuste > 0) {
					
					sObj = PropertiesSystem.CTA_OTROS_INGRESOS_OB ;
					sSub = PropertiesSystem.CTA_OTROS_INGRESOS_SB ;
				
				} else{
					
					if( lstDepsCaja.get(0).getCodcomp().trim().toUpperCase().compareTo("E01") == 0 )
						sUninegCaja = PropertiesSystem.CTA_DEUDORES_VARIOS_UNE01 ;
					if( lstDepsCaja.get(0).getCodcomp().trim().toUpperCase().compareTo("E02") == 0 )
						sUninegCaja = PropertiesSystem.CTA_DEUDORES_VARIOS_UNE02 ;
					if( lstDepsCaja.get(0).getCodcomp().trim().toUpperCase().compareTo("E03") == 0 )
						sUninegCaja = PropertiesSystem.CTA_DEUDORES_VARIOS_UNE03 ;
					if( lstDepsCaja.get(0).getCodcomp().trim().toUpperCase().compareTo("E08") == 0 )
						sUninegCaja = PropertiesSystem.CTA_DEUDORES_VARIOS_UNE08 ;
					
					sObj= PropertiesSystem.CTA_DEUDORES_VARIOS_OB ;
					sSub = "";
					sTipoAuxiliar = "A";
					sCodEmpleado = CodeUtil.pad(String.valueOf( lstDepsCaja.get(0).getCodcajero() ),  8,"0") ; // Divisas.rellenarCadena((lstDepsCaja.get(0).getCodcajero() + "").trim(), "0", 8);
					if (sCodEmpleado.compareToIgnoreCase("") == 0)
						sCodEmpleado = "00000000";
					
				}
				Vf0901 vCtaSbrt  = dv.validarCuentaF0901(sUninegCaja,sObj,sSub);
				if(vCtaSbrt==null){
					error = new Exception("@VF0901: No se ha podido obtener la cuenta '"+sUninegCaja+"."+sObj+"."+sSub+" en maestro de cuentas");
					return false;
				}
				sCtaTemp = new String[6];
				sCtaTemp[0] = sUninegCaja+"."+sObj+ ( (sSub.trim().length()>0)? "."+sSub:"");
				sCtaTemp[1] = vCtaSbrt.getId().getGmaid().trim();
				sCtaTemp[2] = vCtaSbrt.getId().getGmmcu().trim().length()==4? 
								vCtaSbrt.getId().getGmmcu().trim().substring(0,2):
								vCtaSbrt.getId().getGmmcu().trim();
				sCtaTemp[3] = vCtaSbrt.getId().getGmmcu().trim();
				sCtaTemp[4] = vCtaSbrt.getId().getGmobj().trim();
				sCtaTemp[5] = vCtaSbrt.getId().getGmsub().trim();
				
				//&& ===== Asignacion de cuenta en dependencia del tipo de ajuste.
				sCtaCredAjuste = sCtaTemp;
				if (dAjuste > 0) {
					dAjuste = dAjuste*-1;
					sDescrip = "Sobrante de confirmación";
				}else{
					dAjuste = Math.abs(dAjuste);
					sDescrip = "Faltante en confirmación";
				}
				//&& ===== Generacion de lineas de asientos.
				iMontoDeps = dv.pasarAentero(dAjuste);
				dLineaDocs += 1;
				
				if(ar.getMoneda().equals(sMonedaBase)){
					
					bHecho = rcCtrl.registrarAsientoDiario(dtFechaConfirma, sesionCajaR, sCodSucAsiento, sTipoDoc,
							iNoDocum, dLineaDocs, iNoBatch, sCtaCredAjuste[0], sCtaCredAjuste[1], sCtaCredAjuste[3],
							sCtaCredAjuste[4],sCtaCredAjuste[5], "AA", ar.getMoneda(), iMontoDeps, sConcepto,
							sLogin, vaut.getId().getCodapp(), new BigDecimal("0"), sTipoEmpleado, sDescrip, 
							sCtaCredAjuste[2], sCodEmpleado,sTipoAuxiliar , sMonedaBase, sCtaCredAjuste[2], "D");
					if(!bHecho){
						error = rcCtrl.getError();
						return false;
					}
				}else{
					int iMontoDepsdom = dv.pasarAentero( dv.roundDouble(new BigDecimal(dAjuste).multiply(bdTasaJDE).doubleValue()));
					
					bHecho = rcCtrl.registrarAsientoDiario(dtFechaConfirma, sesionCajaR, sCodSucAsiento, sTipoDoc,
							iNoDocum, dLineaDocs, iNoBatch, sCtaCredAjuste[0], sCtaCredAjuste[1], sCtaCredAjuste[3],
							sCtaCredAjuste[4], sCtaCredAjuste[5], "AA", ar.getMoneda(), iMontoDepsdom, sConcepto, 
							sLogin, vaut.getId().getCodapp(), bdTasaJDE, sTipoEmpleado, sDescrip, 
							sCtaCredAjuste[2], sCodEmpleado, sTipoAuxiliar, sMonedaBase, sCtaCredAjuste[2], "F");
					if(!bHecho){
						error = rcCtrl.getError();
						return false;
					}
					// ------------ 
					bHecho = rcCtrl.registrarAsientoDiario(dtFechaConfirma, sesionCajaR, sCodSucAsiento, sTipoDoc,
							iNoDocum, dLineaDocs, iNoBatch, sCtaCredAjuste[0], sCtaCredAjuste[1], sCtaCredAjuste[3],
							sCtaCredAjuste[4], sCtaCredAjuste[5], "CA", ar.getMoneda(), iMontoDeps, sConcepto, 
							sLogin, vaut.getId().getCodapp(), bdTasaJDE, sTipoEmpleado, sDescrip,
							sCtaCredAjuste[2], sCodEmpleado, sTipoAuxiliar, ar.getMoneda(), sCtaCredAjuste[2], "F");
					if(!bHecho){
						error = rcCtrl.getError();
						return false;
					}
				}
			}
			/***********************  ===== Realizar transacciones en modulo de caja. ======= *****************************/
			
			//&& ======= Actualizar estado del deposito de banco.
			bHecho = cdc.confirmarDepositoBanco(depBanco, sesionCajaW1, iEstadoDeposito,
												iTipoConfirma, vaut.getId().getCodreg(),
												depBanco.getReferencia(), cn);
			if(!bHecho){
				error = cdc.getError();
				errorDetalle = cdc.getErrorDetalle();
				return false;
			}
			//&& ======= Actualizar estado del archivo.
			bHecho = cdc.actualizarEstadoArchivo(ar, sesionCajaW1, vaut.getId().getCodreg(), cn);
			if(!bHecho){
				error = cdc.getError();
				errorDetalle = cdc.getErrorDetalle();
				return false;
			}
			
			//&& ======= Guardar los registros de conciliacion y su detalle.
			bHecho = cdc.registrarConciliacion(ar, depBanco, lstDepsCaja, sTipoDoc, iNoBatch, 
											iNoDocum, vaut.getId().getCodreg(), sesionCajaW1);
			if(!bHecho){
				error = cdc.getError();
				errorDetalle = cdc.getErrorDetalle();
				return false;
			}
			//&& ======= Guardar el detalle de los ajustes registrados en caso de haber.
			if(dAjuste!=0){
				int iCodCargo = (dAjuste>0)?0:lstDepsCaja.get(0).getUsrcreate(); 
				Conciliacion conciliacion = (Conciliacion)m.get("cdb_RegistroConciliacion");
				bHecho = cdc.guardarDetalleAjuste(sesionCajaW1, conciliacion, iNoBatch, iNoDocum, 
												 new BigDecimal(String.valueOf(dAjuste)), iCodCargo,
												 Integer.parseInt(sCtaCredAjuste[1]), sTipoDoc, 
												 vaut.getId().getCodreg(), dtFechaConfirma);
				if(!bHecho){
					error = cdc.getError();
					errorDetalle = cdc.getErrorDetalle();
					return false;
				}
			}
			
						
		} catch (Exception e) {
			e.printStackTrace(); 
			bHecho = false;
			errorDetalle = new Exception("@Mensaje de error: "+e);
			error = new Exception("@Error de sistema al realizar confirmacion del deposito ");
		}
		return bHecho;
	}
	//&& ============== REALIZAR LOS GETTERS Y SETTERS ============== //
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
	public ProcesoConfirmacion(){
	}
}

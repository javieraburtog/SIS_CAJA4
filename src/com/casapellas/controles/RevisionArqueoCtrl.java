package com.casapellas.controles;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.casapellas.donacion.entidades.DncCierreDonacion;
import com.casapellas.entidades.Arqueo;
import com.casapellas.entidades.ArqueoId;
import com.casapellas.entidades.Arqueofact;
import com.casapellas.entidades.Arqueorec;
import com.casapellas.entidades.ArqueorecId;
import com.casapellas.entidades.B64strfile;
import com.casapellas.entidades.Ctaxdeposito;
import com.casapellas.entidades.F55ca011;
import com.casapellas.entidades.F55ca014;
import com.casapellas.entidades.F55ca023;
import com.casapellas.entidades.Hfactura;
import com.casapellas.entidades.Numcaja;
import com.casapellas.entidades.Recibo;
import com.casapellas.entidades.ResumenAfiliado;
import com.casapellas.entidades.Tcambio;
import com.casapellas.entidades.Valorcatalogo;
import com.casapellas.entidades.Varqueo;
import com.casapellas.entidades.Vmonedasxcontador;
import com.casapellas.entidades.Vrecibo;
import com.casapellas.entidades.Vrecibodevrecibo;
import com.casapellas.entidades.ens.Vautoriz;
import com.casapellas.entidades.Vcompaniaxcontador;
import com.casapellas.entidades.Vf0901;
import com.casapellas.entidades.Vfacturaxdevolucion;
import com.casapellas.entidades.Vhfactura;
import com.casapellas.entidades.Vrecibosxtipoegreso;
import com.casapellas.entidades.Vrecibosxtipoingreso;
import com.casapellas.entidades.Vrecibosxtipompago;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.jde.creditos.CodigosJDE1;
import com.casapellas.navegacion.As400Connection;
import com.casapellas.reportes.Rptmcaja004Sumary;
import com.casapellas.util.CalendarToJulian;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.Divisas;
import com.casapellas.util.DocumuentosTransaccionales;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.LiquidacionCheque;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.PropertiesSystem;
import com.casapellas.util.SqlUtil;

/**
 * CASA PELLAS S.A.
 * Creado por.........: Carlos Manuel Hernández Morrison
 * Fecha de Creación..: 01/09/2009
 * Última modificación: Carlos Manuel Hernández Morrison
 * Modificado por.....:	19/03/2010
 * Descripcion:.......: Acceso a los datos de arqueos pendientes de revisión.
 * 
 */

public class RevisionArqueoCtrl {
	BigDecimal bdivaTrader = new BigDecimal(1.13);
	
	
	Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	private final static String Esquema = PropertiesSystem.ESQUEMA;
	
	String[] valoresJDEInsCredito = (String[]) m.get("valoresJDEInsCredito");
	String[] valoresJdeInsContado = (String[]) m.get("valoresJDEInsContado");
		
	String[] valoresJdeNumeracion = (String[]) m.get("valoresJDENumeracionIns");
	
	public Exception errorArqueoCtlr;
	public Exception getErrorArqueoCtlr() {
		return errorArqueoCtlr;
	}
	public void setErrorArqueoCtlr(Exception errorArqueoCtlr) {
		this.errorArqueoCtlr = errorArqueoCtlr;
	}
	
	public static int  obtenerNumeroDocumento(String tipodoc, String codcomp, String moneda ){
		int numerodocumento = 0;
		Connection cn = null;
		
		try {
			
			codcomp = CodeUtil.pad( String.valueOf( Integer.parseInt(codcomp) ), 5, "0" );
			
			String sql = "select NLN001 from " + PropertiesSystem.JDECOM
					+ ".f00021 where NLDCT = '" + tipodoc + "' and NLKCO = '"
					+ codcomp + "' fetch first rows only ";

			cn = As400Connection.getJNDIConnection("");
			PreparedStatement ps = cn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			if (rs.next())
				numerodocumento = rs.getInt(1);
			
			rs.close();
			ps.close();
			
			if(numerodocumento == 0){
				numerodocumento = new Divisas().leerActualizarNoDocJDE() ;
				return numerodocumento = (numerodocumento == -1)? 0 : numerodocumento;
			}
			
			sql = "update  " + PropertiesSystem.JDECOM
					+ ".f00021 set NLN001 = (NLN001 + 1) where NLDCT = '"
					+ tipodoc + "' and NLKCO = '" + codcomp + "'";
			
			ps = cn.prepareStatement(sql);
			ps.executeUpdate();
			ps.close();
			
		} catch (Exception e ) {
			
			LogCajaService.CreateLog("ObtenerNumeroDocumento", "ERR", e.getMessage());
		} finally{
			try {
				if(cn != null && !cn.isClosed())
					cn.commit();
			} catch (Exception e2) {
				
				LogCajaService.CreateLog("ObtenerNumeroDocumento", "ERR", e2.getMessage());
			}

		}
		return numerodocumento;
	}
	
	public static boolean validarNumeroReferencia(int numero, String tipodoc, String codcomp, String moneda) {
		boolean valido = true;

		try {
			
			codcomp = CodeUtil.pad( String.valueOf( Integer.parseInt(codcomp) ), 5, "0" );  
			
			String sql = " SELECT * FROM " + PropertiesSystem.JDEDTA
					+ ".F0911 WHERE GLDOC = " + numero + " and gldct = '"
					+ tipodoc + "'  AND GLKCO = '" + codcomp + "'";

			List<Object[]> lstdtaF0911 = ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, null, true);
			
			valido = (lstdtaF0911 == null || lstdtaF0911.isEmpty());

		} catch (Exception e) {
			valido = false;
			
			LogCajaService.CreateLog("validarNumeroReferencia", "ERR", e.getMessage());
		}  
		return valido;
	}
	public static boolean validarNumeroReferencia(int numero, String tipodoc, String codcomp, String moneda, Session session) {
		boolean valido = true;

		try {
			
			codcomp = CodeUtil.pad( String.valueOf( Integer.parseInt(codcomp) ), 5, "0" );  
			
			String sql = " SELECT * FROM " + PropertiesSystem.JDEDTA
					+ ".F0911 WHERE GLDOC = " + numero + " and gldct = '"
					+ tipodoc + "'  AND GLKCO = '" + codcomp + "'";

			@SuppressWarnings("unchecked")
			List<Object[]> lstdtaF0911 = session.createSQLQuery(sql).list() ;
			
			valido = (lstdtaF0911 == null || lstdtaF0911.isEmpty());

		} catch (Exception e) {
			valido = false;
			LogCajaService.CreateLog("validarNumeroReferencia", "ERR", e.getMessage());
		}  
		return valido;
	}
	
	public static int generarReferenciaDeposito(int reference, String tipopago, String codcomp, String moneda){
		int referencia = reference;
		String tipodocjde = "";
		ClsParametroCaja cajaparm = new ClsParametroCaja();
		
		try {
			
			String strReference = String.valueOf(reference);
			
			if(strReference.length() > 8){
				strReference = strReference.substring(strReference.length() - 8, strReference.length());
			}
			
			boolean valido = false;
			int evalua = 0;
			
			//&& ======== Determinar el tipo de documento asociado al metodo de pago del deposito.
			String codigoJDE = "";
			
			if (tipopago.compareTo(MetodosPagoCtrl.EFECTIVO ) == 0){
				tipodocjde = cajaparm.getParametros("34", "0", "TIPODOC_JDE_DEP_5").getValorAlfanumerico().toString();
				codigoJDE = cajaparm.getParametros("34", "0", "TIPODOC_JDE_5").getValorAlfanumerico().toString();
			}
			if (tipopago.compareTo(MetodosPagoCtrl.TRANSFERENCIA) == 0){
				tipodocjde = cajaparm.getParametros("34", "0", "TIPODOC_JDE_DEP_8").getValorAlfanumerico().toString();//PropertiesSystem.TIPODOC_JDE_DEP_8;
				codigoJDE =  cajaparm.getParametros("34", "0", "TIPODOC_JDE_8").getValorAlfanumerico().toString();
			}
			if (tipopago.compareTo(MetodosPagoCtrl.TARJETA) == 0){
				tipodocjde = cajaparm.getParametros("34", "0", "TIPODOC_JDE_DEP_H").getValorAlfanumerico().toString();//PropertiesSystem.TIPODOC_JDE_DEP_H;
				codigoJDE = cajaparm.getParametros("34", "0", "TIPODOC_JDE_H").getValorAlfanumerico().toString();
			}
			if (tipopago.compareTo(MetodosPagoCtrl.DEPOSITO) == 0){
				tipodocjde = cajaparm.getParametros("34", "0", "TIPODOC_JDE_DEP_N").getValorAlfanumerico().toString(); //PropertiesSystem.TIPODOC_JDE_DEP_N;
				codigoJDE = cajaparm.getParametros("34", "0", "TIPODOC_JDE_N").getValorAlfanumerico().toString();
			}
			if (tipopago.compareTo("ZX") == 0){
				tipodocjde = cajaparm.getParametros("37", "0", "ARQ_TDOCREFER").getValorAlfanumerico().toString();//PropertiesSystem.TIPODOC_REFER_ZX;
				codigoJDE =  cajaparm.getParametros("34", "0", "TIPODOC_JDE_5").getValorAlfanumerico().toString();;
				
			}
			
		
			while( evalua <= 4 && !valido){
				
				valido = validarNumeroReferencia( Integer.parseInt( strReference ), tipodocjde, codcomp, moneda);
				
				if(valido)
					break;
				
				strReference = (strReference.length() <= 4) ? strReference : strReference.substring(evalua, strReference.length()) ;
				strReference = (strReference.length() == 8) ? strReference : String.format("1%07d", Integer.parseInt( strReference ) );
				evalua++;
				
				if(Integer.parseInt(strReference) != reference ){
				}
			}
			
			//&& ======== Generar el numero de documento para el deposito (cambio de referencias).
			if(!valido){
				
				reference =  Divisas.numeroSiguienteJdeE1Custom(codigoJDE, codcomp );//Divisas.numeroSiguienteJde( codigoJDE, codcomp );
				
				if( reference == 0 ){
					reference = Divisas.numeroSiguienteJdeE1(CodigosJDE1.NUMERO_DOC_CONTAB_GENERAL );
				}
				
				return reference  ;
				
			}
			
			referencia = Integer.parseInt(strReference);
			
		} catch (Exception e) {
			referencia = reference  ;
			LogCajaService.CreateLog("generarReferenciaDeposito", "ERR", e.getMessage());
		}  
		return referencia;
	}
	public static int generarReferenciaDeposito(int reference, String tipopago, String codcomp, String moneda, Session session){
		int referencia = reference;
		String tipodocjde = "";
		ClsParametroCaja cajaparm = new ClsParametroCaja();
		
		try {
			
			String strReference = String.valueOf(reference);
			
			if(strReference.length() > 8){
				strReference = strReference.substring(strReference.length() - 8, strReference.length());
			}
			
			boolean valido = false;
			int evalua = 0;
			
			//&& ======== Determinar el tipo de documento asociado al metodo de pago del deposito.
			String codigoJDE ="";
			
			if (tipopago.compareTo(MetodosPagoCtrl.EFECTIVO ) == 0){
				tipodocjde = cajaparm.getParametros("34", "0", "TIPODOC_JDE_DEP_5").getValorAlfanumerico().toString();//PropertiesSystem.TIPODOC_JDE_DEP_5;
				codigoJDE = cajaparm.getParametros("34", "0", "TIPODOC_JDE_5").getValorAlfanumerico().toString();
			}
			if (tipopago.compareTo(MetodosPagoCtrl.TRANSFERENCIA) == 0){
				tipodocjde = cajaparm.getParametros("34", "0", "TIPODOC_JDE_DEP_8").getValorAlfanumerico().toString();//PropertiesSystem.TIPODOC_JDE_DEP_8;
				codigoJDE =  cajaparm.getParametros("34", "0", "TIPODOC_JDE_8").getValorAlfanumerico().toString();
			}
			if (tipopago.compareTo(MetodosPagoCtrl.TARJETA) == 0){
				tipodocjde = cajaparm.getParametros("34", "0", "TIPODOC_JDE_DEP_H").getValorAlfanumerico().toString();//PropertiesSystem.TIPODOC_JDE_DEP_H;
				codigoJDE = cajaparm.getParametros("34", "0", "TIPODOC_JDE_H").getValorAlfanumerico().toString();;
			}
			if (tipopago.compareTo(MetodosPagoCtrl.DEPOSITO) == 0){
				tipodocjde = cajaparm.getParametros("34", "0", "TIPODOC_JDE_DEP_N").getValorAlfanumerico().toString(); //PropertiesSystem.TIPODOC_JDE_DEP_N;
				codigoJDE = cajaparm.getParametros("34", "0", "TIPODOC_JDE_N").getValorAlfanumerico().toString();
			}
			if (tipopago.compareTo("ZX") == 0){
				tipodocjde = cajaparm.getParametros("37", "0", "ARQ_TDOCREFER").getValorAlfanumerico().toString();//opertiesSystem.TIPODOC_REFER_ZX;
				codigoJDE =  cajaparm.getParametros("34", "0", "TIPODOC_JDE_5").getValorAlfanumerico().toString();;
			}
			
			while( evalua <= 4 && !valido){
				
				valido = validarNumeroReferencia( Integer.parseInt( strReference ), tipodocjde, codcomp, moneda, session);
				
				if(valido)
					break;
				
				strReference = (strReference.length() <= 4) ? strReference : strReference.substring(evalua, strReference.length()) ;
				strReference = (strReference.length() == 8) ? strReference : String.format("1%07d", Integer.parseInt( strReference ) );
				evalua++;
				
				if(Integer.parseInt(strReference) != reference ){

				}
			}
			
			//&& ======== Generar el numero de documento para el deposito (cambio de referencias).
			if(!valido){
				
				reference =  Divisas.numeroSiguienteJdeDocumentoE1Custom( codigoJDE, codcomp );
				
				if( reference == 0 ){
					reference = Divisas.numeroSiguienteJdeE1(CodigosJDE1.NUMERO_DOC_CONTAB_GENERAL );
				}
				
				return reference  ;
				
			}
			
			referencia = Integer.parseInt(strReference);
			
		} catch (Exception e) {
			referencia = reference  ;
		
			LogCajaService.CreateLog("generarReferenciaDeposito", "ERR", e.getMessage());
		}  
		return referencia;
	}
	
	
	public static String  getBase64StringFromFile(long rowidParent, int itemtype, String parentrowid){
		StringBuilder sb = new StringBuilder("");
		Session sesion = null;
		Transaction trans = null;
		boolean newCn = false;
		
		try {
			
			sesion = HibernateUtilPruebaCn.currentSession();
			trans = (newCn = !(sesion.getTransaction().isActive())) ? sesion
					.beginTransaction() : sesion.getTransaction();
			
			String query = "select * from " + PropertiesSystem.ESQUEMA
					+ ".b64strfile where idrowfile = " + rowidParent
					+ " and itemtype = " + itemtype ;
			
			if( !parentrowid.isEmpty() );
				query += " and idparentstr = '"+parentrowid+"'";
			
			query += " order by index asc";		
			
			@SuppressWarnings("unchecked")	
			List<B64strfile> strs = sesion.createSQLQuery(query).addEntity(B64strfile.class).list();
					
			if(strs == null || strs.isEmpty())		
				return "";
			
			for (B64strfile b64strfile : strs) {
				sb.append(b64strfile.getStrb64part()) ;
			}
			
		} catch (Exception e) {
			LogCajaService.CreateLog("getBase64StringFromFile", "ERR", e.getMessage());
		}finally{
			if(newCn){
				try {  trans.commit(); } 
				catch (Exception e2) { }
				try {  HibernateUtilPruebaCn.closeSession(sesion); }
				catch (Exception e2) { }
			}
		}
		return sb.toString();
	}
	
	
/******************************************************************************/
/** Método: Cargar la lista de recibos por metodo de pago y moneda.
 *	Fecha:  30/11/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 **/
	@SuppressWarnings("unchecked")
	/******************* 8 generar una lista con registros de tipo Vrecibosxtipompago *****************/
	public List obtenerRecibosMpago8N(List<Vrecibosxtipompago> lstRecibos,String sMpago,int iCaid, 
									  String sCodcomp,String sCodsuc,String sMoneda){
		
		List<Integer>lstRecibosConsulta = new ArrayList<Integer>();
		List<Vrecibosxtipompago> lstRecibostmp = new ArrayList<Vrecibosxtipompago>();
		
		try{
			for (Vrecibosxtipompago vrmpago : lstRecibos) 
				lstRecibosConsulta.add(vrmpago.getId().getNumrec());
			
			Session sesion = HibernateUtilPruebaCn.currentSession();
			
			Criteria cr = sesion.createCriteria(Vrecibosxtipompago.class);
			
			cr.add(Restrictions.eq("id.caid", iCaid));
			cr.add(Restrictions.eq("id.codsuc", sCodsuc));
			cr.add(Restrictions.eq("id.codcomp", sCodcomp));
			cr.add(Restrictions.in("id.numrec", lstRecibosConsulta));
			cr.add(Restrictions.eq("id.mpago", sMpago));
			cr.add(Restrictions.eq("id.moneda", sMoneda));
			cr.add(Restrictions.ne("id.tiporec", "FCV"));
			
			lstRecibostmp = cr.list();
			
			sesion.close();
			
			if(lstRecibostmp != null && lstRecibos.size()>0){
				String sReferencia = "";
				for(int i=0; i<lstRecibostmp.size();i++){
					Vrecibosxtipompago vrmp = lstRecibostmp.get(i);

					//&& ======== Conservar en el refer2 la referencia de ambos metodos, para poder actualizar su valor 
					if(sMpago.compareTo(MetodosPagoCtrl.TRANSFERENCIA) == 0){
						sReferencia = vrmp.getId().getRefer2().trim();
						vrmp.getId().setRefer2(vrmp.getId().getRefer3().trim());
						vrmp.getId().setRefer3(sReferencia);
					}
					
					if (vrmp.getId().getTiporec().trim().toUpperCase().equals("DCO")) {
						vrmp.setMonto(vrmp.getId().getMonto().multiply(BigDecimal.valueOf(-1)));
					} else {
						vrmp.setMonto(vrmp.getId().getMonto());
					}
					
					lstRecibostmp.set(i, vrmp);
				}
			}
			
		}catch(Exception error){
			LogCajaService.CreateLog("obtenerRecibosMpago8N", "ERR", error.getMessage());
		}		
		return lstRecibostmp;
	}
	
	
	/******** 18.1 Guardar la Nota de débito al cajero en concepto de faltante de efectivo en el arqueo ****/	
	public boolean generarNotadeDebito( Session session,  int iNoarqueo, int iCaid, String sCodsuc, String sCodcomp, 
			Date dtFecha, BigDecimal dMonto, String sMoneda, Vautoriz vaut, 
			int iCodCajero, String tipoEmpleado, BigDecimal tasaOficial ){
		
		boolean bHecho = true;
		int  iTotalTransaccion=0, iMontoDom=0;
		String sMsjErrorjde="",sCodEmpleado ;
		String sCuenta5[] = null, sCuentaFE = "", sCompCtaFE="", sConcepto="",sFecha="",sTasa="1.0";
		ClsParametroCaja cajaparm = new ClsParametroCaja();
		
		Vf0901 vCtaFE = null;
		Divisas dv = new Divisas();
		ReciboCtrl rcCtrl = new ReciboCtrl();
		F55ca014 f14 = null;
		
		try {
			
			sConcepto = "Nota Débito x Faltante en Caja";
			sCodEmpleado = CodeUtil.pad(String.valueOf(iCodCajero), 8,"0");
			String logs =  sConcepto;
			
			f14 = CompaniaCtrl.obtenerF55ca014(iCaid, sCodcomp);
			if(f14==null){
				CodeUtil.putInSessionMap("ac_sMsjErrorJde", "Error al obtener la configuración de la compañía "+sCodcomp+" para la caja "+iCaid);
				return false;
			}
			String monedaBaseCompania = f14.getId().getC4bcrcd() ;
			 
			//&& =========================== Verificar que exista la cuenta de Funcionarios y Empleados.
			
			String sUN = ""; String sCtaOb; String sSubCta;					
			String deudorCfg = DocumuentosTransaccionales.CTADEUDORESVARIOSUNINEG(sCodcomp.trim());
			String[] CtaCfg = deudorCfg.split(",");
			
			sUN = CtaCfg[0].trim();
			sCtaOb = CtaCfg[1].trim();
			sSubCta = CtaCfg.length == 3 && CtaCfg[2] != null ? CtaCfg[2].trim() : "";
			
			vCtaFE  = dv.validarCuentaF0901(sUN,sCtaOb,sSubCta);
			
			if(vCtaFE!=null){
				sCuentaFE = sUN + "." + sCtaOb + (sSubCta.length() > 0 ? "." + sSubCta : "");
				sCompCtaFE = vCtaFE.getId().getGmco().trim();
			}else{
				sMsjErrorjde = "No se ha podido obtener la cuenta de Deudores Varios: '"+sUN+"."+sCtaOb+"'";
				return bHecho = false; 
			}
			
			sCuenta5 = dv.obtenerCuentaCaja(iCaid, sCodcomp, MetodosPagoCtrl.EFECTIVO , sMoneda, session, null, null, null);
			if(sCuenta5 == null){
				sMsjErrorjde = "Error al obtener la cuenta de caja para Efectivo "+sMoneda+", Caja: "+iCaid+ ", Suc: " + sCodsuc+", Comp: "+sCodcomp;
				return bHecho = false;
			}
			
			String sAsientoSuc = sCompCtaFE;
			String tipoDocJde = DocumuentosTransaccionales.CIERREFALTANTETIPODOC(); //CodigosJDE1.BATCH_CONTADO.codigo();

			int numerobatch = Divisas.numeroSiguienteJdeE1( );
			int numeroDocumento =Divisas.numeroSiguienteJdeE1Custom(valoresJdeNumeracion[8],valoresJdeNumeracion[9] );

			iTotalTransaccion =  Integer.parseInt( String.format("%1$.2f", dMonto ).replace(".", "")  ); 
			bHecho = rcCtrl.registrarBatchA92( session, dtFecha, valoresJdeInsContado[8], numerobatch, iTotalTransaccion, vaut.getId().getLogin(), 1, "APBRARQUEO", valoresJdeInsContado[9] );
			
			if(!bHecho){
				sMsjErrorjde = "No se ha podido grabar Batch por faltante al  cajero " ;
				return bHecho = false;
			}
			
			if( sMoneda.compareTo( monedaBaseCompania )  == 0 ){
				
 				bHecho = rcCtrl.registrarAsientoDiarioLogs(session, logs, dtFecha, sAsientoSuc, tipoDocJde, numeroDocumento, 1.0, numerobatch, sCuentaFE,
						vCtaFE.getId().getGmaid(), vCtaFE.getId().getGmmcu().trim(), vCtaFE.getId().getGmobj().trim(), 
						vCtaFE.getId().getGmsub().trim(), "AA",	sMoneda, iTotalTransaccion, sConcepto, vaut.getId().getLogin(),
						vaut.getId().getCodapp(), BigDecimal.ZERO, tipoEmpleado, "Débito Cajero Efectivo "+sMoneda,
						sCompCtaFE, sCodEmpleado, "A",sMoneda,sCompCtaFE,"D", 0);
				
				if(!bHecho){
					sMsjErrorjde = "No se ha podido grabar Batch por faltante al  cajero " ;
					return bHecho = false;
				}
				
				bHecho = rcCtrl.registrarAsientoDiarioLogs(session, logs, dtFecha,  sAsientoSuc, tipoDocJde, numeroDocumento, 2.0, numerobatch, 
						sCuenta5[0], sCuenta5[1], sCuenta5[3], sCuenta5[4], sCuenta5[5], "AA", sMoneda,
						iTotalTransaccion*-1, sConcepto, vaut.getId().getLogin(), vaut.getId().getCodapp(),
						BigDecimal.ZERO, tipoEmpleado,"Crédito Caja, Efectivo "+ sMoneda, sCuenta5[2], 
						"", "",sMoneda,sCompCtaFE,"D", 0);
				
				if(!bHecho){
					sMsjErrorjde = "No se ha podido grabar Batch por faltante al  cajero " ;
					return bHecho = false;
				}
				
			}else{
				
				iMontoDom =	Integer.parseInt( String.format("%1$.2f", 	dMonto.multiply( tasaOficial )   ).replace(".", "")  ); 
				
				bHecho = rcCtrl.registrarAsientoDiarioLogs(session, logs, dtFecha,  sAsientoSuc, tipoDocJde , numeroDocumento, 1.0, numerobatch, sCuentaFE,
						vCtaFE.getId().getGmaid(), vCtaFE.getId().getGmmcu().trim(), vCtaFE.getId().getGmobj().trim(), 
						vCtaFE.getId().getGmsub().trim(), "AA", sMoneda, iMontoDom, sConcepto, vaut.getId().getLogin(), 
						vaut.getId().getCodapp(), tasaOficial, tipoEmpleado, "Débito Cajero Efectivo "+sMoneda,
						sCompCtaFE, sCodEmpleado, "A", monedaBaseCompania, sCompCtaFE,"F", iTotalTransaccion);
				
				if(!bHecho){
					sMsjErrorjde = "No se ha podido grabar Batch por faltante al  cajero " ;
					return bHecho = false;
				}
				
				
				bHecho = rcCtrl.registrarAsientoDiarioLogs(session, logs, dtFecha, sAsientoSuc, tipoDocJde, numeroDocumento, 1.0, numerobatch, sCuentaFE,
						vCtaFE.getId().getGmaid(), vCtaFE.getId().getGmmcu().trim(), vCtaFE.getId().getGmobj().trim(),
						vCtaFE.getId().getGmsub().trim(),"CA", sMoneda, iTotalTransaccion, sConcepto, vaut.getId().getLogin(),
						vaut.getId().getCodapp(), tasaOficial, tipoEmpleado, "Débito Cajero Efectivo "+sMoneda,
						sCompCtaFE, sCodEmpleado, "A",monedaBaseCompania, sCompCtaFE,"F", 0);
				
				if(!bHecho){
					sMsjErrorjde = "No se ha podido grabar Batch por faltante al  cajero " ;
					return bHecho = false;
				}
				
				bHecho = rcCtrl.registrarAsientoDiarioLogs(session, logs, dtFecha, sAsientoSuc, tipoDocJde, numeroDocumento,  2.0, numerobatch, sCuenta5[0],
						sCuenta5[1], sCuenta5[3], sCuenta5[4], sCuenta5[5], "AA", sMoneda, iMontoDom*-1,
						sConcepto, vaut.getId().getLogin(), vaut.getId().getCodapp(), tasaOficial,
						tipoEmpleado, "Crédito Caja,Efectivo "+sMoneda, sCuenta5[2], sCodEmpleado, "A", monedaBaseCompania, sCompCtaFE, "F", (iTotalTransaccion*-1) );
				
				if(!bHecho){
					sMsjErrorjde = "No se ha podido grabar Batch por faltante al  cajero " ;
					return bHecho = false;
				}
				
				bHecho = rcCtrl.registrarAsientoDiarioLogs(session, logs, dtFecha, sAsientoSuc, tipoDocJde, numeroDocumento, 2.0, numerobatch, sCuenta5[0],
						sCuenta5[1], sCuenta5[3], sCuenta5[4], sCuenta5[5], "CA", sMoneda, iTotalTransaccion*-1, 
						sConcepto, vaut.getId().getLogin(), vaut.getId().getCodapp(), tasaOficial, 
						tipoEmpleado,"Crédito Salida,Efectivo "+sMoneda, sCuenta5[2], sCodEmpleado, "A", monedaBaseCompania, sCompCtaFE, "F", 0);
				
				if(!bHecho){
					sMsjErrorjde = "No se ha podido grabar Batch por faltante al  cajero " ;
					return bHecho = false;
				}
				
			}
		 
		
			//&& ====== Guardar las cuentas asociadas al deposito.
			ArrayList<Ctaxdeposito> lstCtasxDeps = new ArrayList<Ctaxdeposito>();
			Ctaxdeposito ctaxDeps  = new Ctaxdeposito();
			ctaxDeps.setMonto(new BigDecimal(String.valueOf(dMonto)));
			ctaxDeps.setTipomov("C");
			ctaxDeps.setIdcuenta(vCtaFE.getId().getGmaid());
			ctaxDeps.setGmmcu(vCtaFE.getId().getGmmcu().trim());
			ctaxDeps.setGmobj(vCtaFE.getId().getGmobj().trim());
			ctaxDeps.setGmsub(vCtaFE.getId().getGmsub().trim());
			lstCtasxDeps.add(ctaxDeps);
			
			//&& ========== Objeto para debito, caja.
			ctaxDeps = new Ctaxdeposito();
			ctaxDeps.setMonto(new BigDecimal(String.valueOf(dMonto)));
			ctaxDeps.setTipomov("D");
			ctaxDeps.setIdcuenta(sCuenta5[1]);
			ctaxDeps.setGmmcu(sCuenta5[3]);
			ctaxDeps.setGmobj(sCuenta5[4]);
			ctaxDeps.setGmsub(sCuenta5[5]);
			lstCtasxDeps.add(ctaxDeps);
			
			
			bHecho = rcCtrl.fillEnlaceMcajaJde(session, null, iNoarqueo, sCodcomp, numeroDocumento, numerobatch, iCaid, sCodsuc, "D", "F");
			if(!bHecho){
				sMsjErrorjde = "No se ha podido grabar asociación entre modulo de caja y Edward's para el batch por faltante al cajero" ;
				return bHecho = false;
			}
			
			Numcaja n = Divisas.obtenerNumeracionCaja("NDEPOSITO", iCaid, sCodcomp, sCodsuc, true, vaut.getId().getLogin(),session );
			
			if(!bHecho){
				sMsjErrorjde = "no hay numeración de depósito configurada para caja "+iCaid+" compañía "  + sCodcomp ;
				return bHecho = false;
			}
			
			bHecho = dv.registrarDeposito( n.getNosiguiente(), iCaid, sCodsuc, sCodcomp, dtFecha, dtFecha, dMonto ,
						sMoneda, Integer.toString( iNoarqueo ) , vaut.getId().getLogin(), "D",
						tasaOficial, vaut.getId().getCodreg().intValue(), "X", f14.getId().getC4bnc(),
						session, iCodCajero, lstCtasxDeps, numerobatch,  0,  f14.getId().getC4bcrcd() );

			if(!bHecho){
				sMsjErrorjde = "no se ha podido grabar el registro por deposito generado por el faltante al cajero"  ;
				return bHecho = false;
			}
			 
		} catch (Exception error) {
			errorArqueoCtlr = error;
			sMsjErrorjde = "Nota de débito al cajero por faltante no pudo grabarse en JDE,  intente nuevamente ";
			LogCajaService.CreateLog("generarNotadeDebito", "ERR", error.getMessage());
		}finally{
			
			if(!bHecho)
				CodeUtil.putInSessionMap( "ac_sMsjErrorJde", sMsjErrorjde );
			
		}
		return bHecho;
	}
	
	
	
/****************************************************************************************/
/** Método: Consulta los totales por banco de pagos con cheques en la caja.
 *	Fecha:  13/06/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 **/
	public List<LiquidacionCheque> obtenerTotalesChequexBanco(int iCaid,String sMoneda,Date dtFecha,
															String sCodcomp, String sCodsuc,String sLstRecibos){
		List<LiquidacionCheque>lstTotalChk = null;
		List<Object[]>lstTotales=null;
		String sql="";
		String sFecha="";
		Session sesion = HibernateUtilPruebaCn.currentSession();
		Transaction trans = sesion.beginTransaction();
		
		try {
			sFecha = new FechasUtil().formatDatetoString(dtFecha, "yyyy-MM-dd");
			
			sql = " select count(*)  cantidad, sum(rd.monto) as TotalBanco, rd.refer1, CAST(banco AS VARCHAR(30) ccsid 37), ";
			sql+=" CAST(conciliar as numeric(8) )";
			sql+=" from "+Esquema+".recibo r inner join "+Esquema+".recibodet rd on r.caid = rd.caid and r.codcomp= rd.codcomp " ;
			sql+=" and r.codsuc = rd.codsuc and r.tiporec = rd.tiporec and r.numrec = rd.numrec ";
			sql+=" inner join "+Esquema+".F55ca022 bco on bco.codb = rd.refer1";
			sql+=" where r.estado<>'A' and r.tiporec<>'DCO' and rd.mpago = '"+MetodosPagoCtrl.CHEQUE+"' and rd.moneda='"+sMoneda+"' ";
			sql+=" and r.numrec in "+sLstRecibos;
			sql+=" and r.caid = "+iCaid+" and r.fecha = '"+sFecha+"' and r.codcomp = '"+sCodcomp+"'";
			
			sql+=" and r.codsuc ='"+sCodsuc+"' and r.numrec not in (\n";
			sql+=" select r1.nodoco from "+Esquema+".recibo r1 inner join "+Esquema+".recibodet rd1 on ";
			sql+=" r1.caid = rd1.caid and r1.codcomp= rd1.codcomp and r1.codsuc = rd1.codsuc and r1.tiporec = rd1.tiporec ";	
			sql+=" and r1.numrec = rd1.numrec ";
			sql+=" and r1.numrec in "+sLstRecibos;
			sql+=" where rd1.mpago = rd.mpago and rd1.moneda = rd.moneda  and r1.caid = r.caid and r1.codsuc= r.codsuc ";
			sql+=" and r1.codcomp = r.codcomp and r1.fecha = r.fecha and r1.estado<>'A'";
			sql+=" and r1.nodoco = r.numrec  and r1.tipodoco = r.tiporec )\n";
			
			sql+=" group by refer1,banco,conciliar ";
			lstTotales = (ArrayList<Object[]>)sesion.createSQLQuery(sql).list();
			trans.commit();
			sesion.close();
			
			if(lstTotales!=null && lstTotales.size()>0){
				lstTotalChk = new ArrayList<LiquidacionCheque>(lstTotales.size());
				for (Object[] ob : lstTotales) {
					LiquidacionCheque lck = new LiquidacionCheque();
					lck.setCaid(iCaid);
					lck.setCodsuc(sCodsuc);
					lck.setCodcomp(sCodcomp);
					lck.setCodigoBanco(String.valueOf(ob[2]));
					lck.setIcodigobanco(Integer.parseInt(String.valueOf(ob[2])));
					
					lck.setNombreBanco(String.valueOf(ob[3].toString()));
					lck.setCantidadCheque(Integer.parseInt(String.valueOf(ob[0])));
					lck.setMontototal(String.valueOf(ob[1]));
					lck.setBtotalBanco(new BigDecimal(String.valueOf(ob[1])));
					lck.setMoneda(sMoneda);
					lck.setReferenciaBanco("");
					lck.setNommoneda("");
					lck.setMetodopago(MetodosPagoCtrl.CHEQUE);
					lck.setConciliaauto(String.valueOf(ob[4]).trim());
					
					if(sMoneda.equals("COR"))
						lck.setNommoneda("CORDOBAS");
					else
					if(sMoneda.equals("USD"))
						lck.setNommoneda("DOLARES");
				
					lstTotalChk.add(lck);
				}
			}
		} catch (Exception e) {
			lstTotalChk = null;
			errorArqueoCtlr = e;
			LogCajaService.CreateLog("obtenerTotalesChquexBanco", "ERR", e.getMessage());
		}
		return lstTotalChk;
	}

	@SuppressWarnings("unchecked")
	public List<ResumenAfiliado> consultaMontoPOSxMarca(int iCaid,String sCodcomp, String sMoneda, String sListaRec,
											Date dtFecha, List<DncCierreDonacion> cierresDnc){
		
		BigDecimal bdRetencion = BigDecimal.ZERO;
		BigDecimal bdTasaRetencion = BigDecimal.ONE;
		List<Object[]> lstPOSdncs = null; 
		List<ResumenAfiliado> lstPos = new ArrayList<ResumenAfiliado>();
		Divisas dv = new Divisas();
		ClsParametroCaja cajaparm = new ClsParametroCaja();
		String sTrader = "0";
		String sAlpesa = "0";
		
		try {
			
			String sFecha = FechasUtil.formatDatetoString(dtFecha, "yyyy-MM-dd");
			
			sTrader = cajaparm.getParametros("38", "0", "TRADERSA").getCodigoCompania().toString().trim();
			sAlpesa = cajaparm.getParametros("38", "0", "ALPESA").getCodigoCompania().toString().trim();
			
			String sql = 
			"SELECT sum(rd.monto) as MONTOTOTAL,  " +
				" (SELECT CXCOMI FROM "+PropertiesSystem.ESQUEMA+".F55CA03 AF WHERE CXCAFI =  RD.REFER1 AND CXRP01 =R.CODCOMP FETCH FIRST ROWS ONLY  )  AS COMISION, " +
				" (SELECT TRIM(CXDCAFI) FROM "+PropertiesSystem.ESQUEMA+".F55CA03 AF WHERE CXCAFI = RD.REFER1 AND CXRP01 =R.CODCOMP FETCH FIRST ROWS ONLY  ) AS NOMBREPOS, " +
				" RD.REFER1 AS IDPOS, RD.VMANUAL, R.CAID, R.CODCOMP, R.CODSUC, R.FECHA, RD.CAIDPOS, "  +
				" (SELECT CODB FROM "+PropertiesSystem.ESQUEMA+".F55CA03 AF WHERE CXCAFI = RD.REFER1 AND CXRP01 = R.CODCOMP FETCH FIRST ROWS ONLY  ) AS CODIGOBANCO," +
				" RD.MARCATARJETA, RD.LIQUIDARPORMARCA, RD.CODIGOMARCATARJETA " + 
				
			" FROM "+PropertiesSystem.ESQUEMA+".RECIBO R INNER JOIN "+PropertiesSystem.ESQUEMA+".RECIBODET RD ON R.CAID = RD.CAID " + 
				" AND R.CODCOMP = RD.CODCOMP AND R.NUMREC = RD.NUMREC AND R.TIPOREC = RD.TIPOREC " + 
			" WHERE R.CAID = "+iCaid+"  AND R.FECHA = '"+sFecha+"' AND RD.MPAGO = '"+MetodosPagoCtrl.TARJETA+"'  " +
				" AND RD.MONEDA = '" +sMoneda+"'AND trim(R.CODCOMP) = '" + sCodcomp.trim() +"' AND R.NUMREC IN " +sListaRec ; 
			
			String sqlDev = sql + " AND R.TIPOREC = 'DCO'";
			
			sql += " AND R.TIPOREC NOT IN ('FCV','DCO') ";
			sql += " GROUP BY RD.REFER1, RD.VMANUAL, R.CAID, R.CODCOMP, R.CODSUC, R.FECHA, RD.CAIDPOS, RD.MARCATARJETA, RD.LIQUIDARPORMARCA, RD.CODIGOMARCATARJETA ";
			
			
			//&& ============================ Incluir donaciones 
			String strSqlDonacion = "";
			if( cierresDnc != null){
				
				List<Integer>idsCierresDnc = (ArrayList<Integer>)CodeUtil.selectPropertyListFromEntity(cierresDnc, "idcierredonacion", true);
				String idsIn = idsCierresDnc.toString().replace("[", "(").replace("]", ")");
				
				strSqlDonacion =
				
				" select  sum(montorecibido) total, comisionpos, "+
				"	(select  trim(cxdcafi) from " +PropertiesSystem.ESQUEMA+ ".f55ca03 " +
					"where cast( cxcafi as varchar(20) ccsid 37)  = cast(codigopos as varchar(20) ccsid 37)   " +
					" and trim(cxrp01) = trim(codcomp) fetch first rows only ) nombrepos ,"+
				
				"	codigopos,  0, caid, codcomp, '00000' codsuc, date(fechacrea), caid caidpos," +
						
				"  (select codb from "+PropertiesSystem.ESQUEMA+".F55CA03 af " +
					"where cast( cxcafi as varchar(20) ccsid 37)  = cast(codigopos as varchar(20) ccsid 37)  " +
					" and trim(cxrp01) = trim(codcomp) fetch first rows only ) codigobanco,  " +
				
				" marcatarjeta, liquidarpormarca, codigomarcatarjeta  "  +
				 
				" from "+PropertiesSystem.ESQUEMA+".dnc_donacion d where iddonacion in ( "+
				"	select  iddonacion  "+
				"	from "+PropertiesSystem.ESQUEMA+".dnc_cierre_detalle "+
				"	where idcierrednc in " + idsIn +" and  formapago = '"+MetodosPagoCtrl.TARJETA+"'  ) "+
				" group by comisionpos, codigopos, caid, codcomp, date(fechacrea), marcatarjeta, liquidarpormarca, codigomarcatarjeta " ;
				
			}
			
			
			F55ca014 f14 = CompaniaCtrl.obtenerF55ca014(iCaid, sCodcomp);
			if(f14 == null){
				return null;
			}
			bdTasaRetencion = f14.getId().getC4trir();
			
			LogCajaService.CreateLog("consultaMontoPOSxMarca", "QRY", sql);
			List<Object[]> lstPOSrec = (ArrayList<Object[]>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, true, null ) ;
			
			if (cierresDnc != null) {
				LogCajaService.CreateLog("consultaMontoPOSxMarca", "QRY", strSqlDonacion);
				lstPOSdncs = (ArrayList<Object[]>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSqlDonacion, true, null ) ;
				
				if(lstPOSdncs != null && !lstPOSdncs.isEmpty())
					CodeUtil.putInSessionMap("rva_TotalesDonacionAfiliado", lstPOSdncs);
			}
			
			//&& ================= Si no se registraron transacciones con tarjetas
			if(lstPOSrec == null && lstPOSdncs == null ){
				return new ArrayList<ResumenAfiliado>();
			}
			
			//&& ================= transacciones de ambos tipos fusionar ambos registros 
			List<Object[]> noEnRcs = new ArrayList<Object[]> () ;
			if( lstPOSrec != null && !lstPOSrec.isEmpty() && lstPOSdncs != null &&!lstPOSdncs.isEmpty()  ){
				
				for (final Object[] Posrec : lstPOSdncs) {
				
					Object[] objPagosRc = (Object[])
					CollectionUtils.find(lstPOSrec, new Predicate(){
						public boolean evaluate(Object o) {
							return
							String.valueOf( ((Object[])o)[3] ) .compareTo( String.valueOf( Posrec[3]) ) == 0  &&
							String.valueOf( ((Object[])o)[4] ) .compareTo( String.valueOf( Posrec[4]) ) == 0  &&
							String.valueOf( ((Object[])o)[13] ).compareTo( String.valueOf( Posrec[13]) ) == 0 &&
							String.valueOf( ((Object[])o)[12] ).compareTo( String.valueOf( Posrec[12]) ) == 0 ;
						}
					});
					
					if( objPagosRc == null ){
						noEnRcs.add( Posrec );
						continue;
					}
					
					objPagosRc[0] = ( (BigDecimal)Posrec[0] ).add( (BigDecimal)objPagosRc[0] ) ;
					
				}
				if( !noEnRcs.isEmpty() ){
					lstPOSrec.addAll(noEnRcs) ;
				}  
			}
			
			//&& =========== transacciones solo con donaciones 
			if( (lstPOSrec == null || lstPOSrec.isEmpty() ) && lstPOSdncs != null  && !lstPOSdncs.isEmpty() ){
				lstPOSrec = lstPOSdncs;
			}
			
			lstPos = new ArrayList<ResumenAfiliado>(lstPOSrec.size());
			
			for (Object[] Posrec : lstPOSrec) {
				
				ResumenAfiliado ra = new ResumenAfiliado();
				String sCodigo, sComision, sMontoneto, sMtoxcomis, sNombre,sMontototal, sIvacomision = "0.00";
				BigDecimal bdNeto, bdTotal, bdComision,bdMtoComis, bdmontoComisionable = BigDecimal.ZERO, bdivaComision =  BigDecimal.ZERO;
				BigDecimal bdMontodev = BigDecimal.ZERO;
				String sVmanual = "";
				int iCaidPos = 0;
				int codigobanco = f14.getId().getC4bnc();
				
				//---- Operaciones para obtener los datos de la consulta.
				bdTotal 	= (BigDecimal)Posrec[0];
				sNombre     = String.valueOf(Posrec[2]).toLowerCase();
				sCodigo     = String.valueOf(Posrec[3]);
				sVmanual    = String.valueOf(Posrec[4]);
				iCaidPos    = Integer.parseInt(String.valueOf(Posrec[9]));
				codigobanco = Integer.parseInt(String.valueOf(Posrec[10]));
				
				int liquidarxmarca  = Integer.parseInt(String.valueOf(Posrec[12])); 
				String marcatarjeta = String.valueOf(Posrec[11]);
				String codigomarcatarjeta = String.valueOf(Posrec[13]);

				//--- Buscar Devoluciones para el POS
				String sqlD = sqlDev;
				sqlD +=  " AND RD.VMANUAL = '" +sVmanual+ "' \n";
				sqlD +=  " AND RD.REFER1 = '"+sCodigo.trim()+"'\n";
				sqlD +=  " AND RD.CODIGOMARCATARJETA = '"+codigomarcatarjeta+"'\n";
				sqlD +=  " AND RD.LIQUIDARPORMARCA = "+liquidarxmarca+"\n";
				sqlD +=  " GROUP BY RD.REFER1, RD.VMANUAL, R.CAID, R.CODCOMP, R.CODSUC, R.FECHA, RD.CAIDPOS, RD.MARCATARJETA, RD.LIQUIDARPORMARCA, RD.CODIGOMARCATARJETA ";
			 
				LogCajaService.CreateLog("consultaMontoPOSxMarca", "QRY", sqlD);
				
				List<Object[]> lstDev = (ArrayList<Object[]>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(sqlD, true, null ); 
				
				if(lstDev!=null && lstDev.size()>0){
					for (Object[] PosDev : lstDev) {
						bdMontodev = (BigDecimal)PosDev[0];
						bdTotal = bdTotal.subtract(bdMontodev);
					}
				}
				
				boolean bDiferenciaPermitida =  bdTotal.abs().compareTo(new BigDecimal("0.10") )  == -1  ;
				if(bdTotal.compareTo(BigDecimal.ZERO) == 0 || bDiferenciaPermitida ){
					continue;
				}
				 
				//&& =============== Calcular comision segun compania 
				if(sCodcomp.trim().equals(sTrader)){
					
					//calcular el monto comisionable
					bdmontoComisionable =  new BigDecimal(dv.roundDouble(bdTotal.doubleValue()/bdivaTrader.doubleValue()));				
				 
					bdComision 	= (BigDecimal)Posrec[1];
					bdMtoComis  = bdmontoComisionable.multiply(bdComision.divide(new BigDecimal("100"))); 
					bdNeto 		= bdTotal.subtract(bdMtoComis);		
					bdRetencion = bdmontoComisionable.multiply(bdTasaRetencion.divide(new BigDecimal("100"))); 
					bdNeto      = bdNeto.subtract(bdRetencion);
					bdivaComision = bdMtoComis.multiply(new BigDecimal(0.13));
					bdNeto      = bdNeto.subtract(bdivaComision);
					//
					bdmontoComisionable = Divisas.roundBigDecimal(bdmontoComisionable);
						
					bdTotal       = Divisas.roundBigDecimal(bdTotal);
					bdComision    = Divisas.roundBigDecimal(bdComision);
					bdMtoComis    = Divisas.roundBigDecimal(bdMtoComis);
					bdNeto	      = Divisas.roundBigDecimal(bdNeto);
					bdRetencion   = Divisas.roundBigDecimal(bdRetencion);
					bdivaComision = Divisas.roundBigDecimal(bdivaComision);
					//
					sMontototal  = dv.formatDouble(bdTotal.doubleValue());
					sComision 	 = dv.formatDouble(bdComision.doubleValue());
					sMontoneto   = dv.formatDouble(bdNeto.doubleValue());
					sMtoxcomis   = dv.formatDouble(bdMtoComis.doubleValue());
					sIvacomision = dv.formatDouble(bdivaComision.doubleValue());
					
				}
				else{
					bdComision 	= (BigDecimal)Posrec[1];
					bdMtoComis  = bdTotal.multiply(bdComision.divide(new BigDecimal("100"))); 
					bdNeto 		= bdTotal.subtract(bdMtoComis);		
					bdRetencion = bdNeto.multiply(bdTasaRetencion.divide(new BigDecimal("100"))); 
					bdNeto      = bdNeto.subtract(bdRetencion);
					
					//EL AJUSTE SE RESTA AL MONTO NETO Y LA COMISION SIEMPRE SE CALCULA SOBRE EL TOTAL 
					//LA RETENCION SE CALCULA SOBRE EL NETO (TOTAL - COMISION)
					
					bdTotal    = Divisas.roundBigDecimal(bdTotal);
					bdComision = Divisas.roundBigDecimal(bdComision);
					bdMtoComis = Divisas.roundBigDecimal(bdMtoComis);
					bdNeto	   = Divisas.roundBigDecimal(bdNeto);
					bdRetencion= Divisas.roundBigDecimal(bdRetencion);//retencion de 1% sobre tarjeta
					
					//--- Asignar los datos a tipo String para 
					sMontototal = dv.formatDouble(bdTotal.doubleValue());
					sComision 	= dv.formatDouble(bdComision.doubleValue());
					sMontoneto  = dv.formatDouble(bdNeto.doubleValue());
					sMtoxcomis  = dv.formatDouble(bdMtoComis.doubleValue());
				}
				
				String sTipo = "";
				if(sVmanual.equals("1"))
					sTipo = "Manual";
				else if(sVmanual.equals("0"))
					sTipo = "Auto";
				else if(sVmanual.equals("2"))
					sTipo = "Socketpos";
				
				ra.setCodigo(sCodigo);
				ra.setMontototal(sMontototal);
				ra.setComision(sComision);
				ra.setMontoneto(sMontoneto);
				ra.setMontoxcomision(sMtoxcomis);
				ra.setNombre(sNombre);
				ra.setReferencia("");
				ra.setMoneda(sMoneda);
				ra.setMontototal( sMontototal);
				ra.setDmontoneto(  bdNeto.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
				ra.setDmontototal( bdTotal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
				ra.setDmontoxcomision(bdMtoComis.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
				ra.setMontoRetencion(bdRetencion);
				ra.setVmanual(sTipo);
				ra.setScodvmanual(sVmanual);
				ra.setIcaidpos(iCaidPos);
				ra.setMontoComisionable(bdmontoComisionable);
				ra.setIvaComision(bdivaComision);
				ra.setSivacomision(sIvacomision);
				ra.setCodigobancopos(codigobanco);
				
				ra.setPorcentajecomision(bdComision);
				ra.setPorcentajeretencion(bdTasaRetencion);
				
				ra.setLiquidarpormarca(liquidarxmarca);
				ra.setMarcatarjeta(marcatarjeta);
				ra.setCodigomarcatarjeta( codigomarcatarjeta ) ;
				
				try {
					
					if( sVmanual.compareTo("1") == 0){
						ra.setDepositoctatransitoria( 0 ) ;
					}else{
						boolean preconciliacion = BancoCtrl.ingresoBajoPreconciliacion( codigobanco , iCaid,  sCodcomp);
						ra.setDepositoctatransitoria( (preconciliacion ? 1 : 0) );
					}

				} catch (Exception e) {
					LogCajaService.CreateLog("consultaMontoPOSxMarca", "ERR", e.getMessage());
					 e.printStackTrace(); 
				}
				
				lstPos.add(ra);
			}
		
			if( lstPos == null || lstPos.isEmpty() )
				return lstPos;
			
			//&& ================== separa los registros de afiliados que no clasifican por marcas
			List<ResumenAfiliado> posNoClasificado = (ArrayList<ResumenAfiliado>)
				CollectionUtils.select(lstPos, new Predicate(){
					public boolean evaluate(Object o) {
						return ((ResumenAfiliado)o).getLiquidarpormarca() == 0	;				
					}
				});
			 
			lstPos = (List<ResumenAfiliado>) CollectionUtils.subtract(lstPos, posNoClasificado);			
			
			//&& =============== consolidar los registros segregados por las marcas
			List<ResumenAfiliado> consolidadoNoClasificado = new ArrayList<ResumenAfiliado>();
			
			for (int i = 0; i < posNoClasificado.size(); i++) {
				
				final ResumenAfiliado ra =  posNoClasificado.get(i);
				ra.setMarcatarjeta("Genérico");
				
				List<ResumenAfiliado> posSegregados =  (ArrayList<ResumenAfiliado>)
						CollectionUtils.select( posNoClasificado, new Predicate(){
							public boolean evaluate(Object o) {
								return
								((ResumenAfiliado)o).getCodigo().compareTo(ra.getCodigo() ) == 0 &&
								((ResumenAfiliado)o).getVmanual().trim().compareTo(ra.getVmanual().trim() ) == 0 &&
								((ResumenAfiliado)o).getCodigomarcatarjeta().trim().compareToIgnoreCase( ra.getCodigomarcatarjeta().trim() ) != 0 ;
							}
						});
				
				//&& ======== Es un unico registro: Marcar el resumen de afiliado como listo para asignar referencia.
				if(posSegregados == null || posSegregados.isEmpty() ){
					consolidadoNoClasificado.add(ra);
					continue;
				}
				
				//&& ======== Hay mas de una marca de tarjeta, consolidar el monto en un unico resultado.
				for (ResumenAfiliado raSegregado : posSegregados) {
					
					ra.setDmontoneto( ra.getDmontoneto() + raSegregado.getDmontoneto() );
					ra.setDmontototal( ra.getDmontototal() + raSegregado.getDmontototal() );
					ra.setDmontoxcomision( ra.getDmontoxcomision() + raSegregado.getDmontoxcomision()  );
					ra.setMontoRetencion( ra.getMontoRetencion().add(raSegregado.getMontoRetencion())  ); 
					
					ra.setMontoComisionable( ra.getMontoComisionable().add(raSegregado.getMontoComisionable() ) );
					ra.setIvaComision( ra.getIvaComision().add(raSegregado.getIvaComision() ) );
					
					ra.setMontototal( String.format("%1$,.2f", ra.getDmontototal() ) );
					ra.setMontoneto( String.format("%1$,.2f", ra.getDmontoneto() ) );
					ra.setMontoxcomision( String.format("%1$,.2f", ra.getDmontoxcomision() ) );
					ra.setSivacomision( String.format("%1$,.2f", ra.getIvaComision() ) );
					
				}
				
				posNoClasificado = (List<ResumenAfiliado>) CollectionUtils.subtract( posNoClasificado, posSegregados ); 
				consolidadoNoClasificado.add(ra);
				
			}
			
			if( consolidadoNoClasificado != null &&  !consolidadoNoClasificado.isEmpty() ){
				lstPos.addAll( consolidadoNoClasificado );
			}
			
			
		} catch (Exception e) {
			LogCajaService.CreateLog("consultaMontoPOSxMarca", "ERR", e.getMessage());
		} 
		
		return lstPos;
	}
	
/****************************************************************************************/
/** Método: Consulta los pagos y trae el resumen por POS para que se le pueda asignar
 * 			la referencia que entrega el banco, que será usada como GLDOC en los asientos. 
 *	Fecha:  13/08/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 **/
	public List<ResumenAfiliado> consultarMontosPOS(
			int iCaid,String sCodcomp,String sCodsuc,String sMoneda,
			String sListaRec,Date dtFecha, List<DncCierreDonacion> cierresDnc ){
		return consultaMontoPOSxMarca(iCaid, sCodcomp, sMoneda, sListaRec, dtFecha, cierresDnc);
	}		
	 
/****************************************************************************************/
/****************************************************************************************/
/** Método: Consulta los pagos y trae el resumen por POS para que se le pueda asignar
 * 			la referencia que entrega el banco, que será usada como GLDOC en los asientos.  (sin usar) 
 *	Fecha:  14/05/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 **/
	public List consultarMontosPOS1(int iCaid,String sCodcomp,String sCodsuc,String sMoneda,String sListaRec,Date dtFecha){
		List lstMontos = null, lstPos = null;
		Transaction trans = null;
		Session sesion = HibernateUtilPruebaCn.currentSession();
		Divisas dv = null;
		FechasUtil f = new FechasUtil();
		String sConsulta,sFecha;
		BigDecimal bdRetencion = new BigDecimal(0),bdTasaRetencion = new BigDecimal(1);
		try {
			sFecha = f.formatDatetoString(dtFecha, "yyyy-MM-dd");
			
			sConsulta = "  select sum(v.id.montoneto),v.id.afcomision,v.id.afnombre,v.id.refer1";
			sConsulta += " from Vrecibodevrecibo as v where v.id.fecha = '"+sFecha+"' and v.id.caid = "+iCaid;
			sConsulta += " and v.id.codsuc = '"+sCodsuc+"' and v.id.codcomp = '"+sCodcomp+"' and tiporec not in ('FCV','DCO','DCR')";
			sConsulta += " and v.id.mpago  = '"+MetodosPagoCtrl.TARJETA+"' and (v.id.devolucion = 0 or v.id.montoneto <> v.id.monto)";
			sConsulta += " and v.id.moneda = '"+sMoneda+" 'and v.id.numrec in "+sListaRec; 
			sConsulta += " group by v.id.refer1,v.id.afcomision,v.id.afnombre";
			
			trans = sesion.beginTransaction();
			lstMontos = sesion.createQuery(sConsulta).list();
			trans.commit();
			
			//--- Crear la lista de objetos de Tipo ResumenAfiliado.
			if(lstMontos!=null && lstMontos.size()>0){
				lstPos = new ArrayList(lstMontos.size());
				for(int i=0; i<lstMontos.size();i++){
					ResumenAfiliado ra = new ResumenAfiliado();
					dv = new Divisas();
					Object ob[] = (Object[])lstMontos.get(i);
					String sCodigo, sComision, sMontoneto, sMtoxcomis, sNombre,sMontototal;
					BigDecimal bdNeto, bdTotal, bdComision,bdMtoComis;
					
					//---- Operaciones para obtener los datos de la consulta.
					bdTotal 	= (BigDecimal)ob[0];
					bdComision 	= (BigDecimal)ob[1];
					bdMtoComis  = bdTotal.multiply(bdComision.divide(new BigDecimal("100"))); 
					bdNeto 		= bdTotal.subtract(bdMtoComis);		
					bdRetencion = bdNeto.multiply(bdTasaRetencion.divide(new BigDecimal("100"))); 
					bdNeto      = bdNeto.subtract(bdRetencion);
					//EL AJUSTE SE RESTA AL MONTO NETO Y LA COMISION SIEMPRE SE CALCULA SOBRE EL TOTAL 
					//LA RETENCION SE CALCULA SOBRE EL NETO (TOTAL - COMISION)
					
					bdTotal    = dv.roundBigDecimal(bdTotal);
					bdComision = dv.roundBigDecimal(bdComision);
					bdMtoComis = dv.roundBigDecimal(bdMtoComis);
					bdNeto	   = dv.roundBigDecimal(bdNeto);
					bdRetencion= dv.roundBigDecimal(bdRetencion);//retencion de 1% sobre tarjeta
					
					//--- Asignar los datos a tipo String para 
					sMontototal = dv.formatDouble(bdTotal.doubleValue());
					sComision 	= dv.formatDouble(bdComision.doubleValue());
					sMontoneto  = dv.formatDouble(bdNeto.doubleValue());
					sMtoxcomis  = dv.formatDouble(bdMtoComis.doubleValue());
					sNombre 	= ob[2].toString().trim();
					sCodigo		= ob[3].toString().trim();
					
					ra.setCodigo(sCodigo);
					ra.setMontototal(sMontototal);
					ra.setComision(sComision);
					ra.setMontoneto(sMontoneto);
					ra.setMontoxcomision(sMtoxcomis);
					ra.setNombre(sNombre);
					ra.setReferencia("");
					ra.setMoneda(sMoneda);
					ra.setMontototal(sMontototal);
					ra.setDmontoneto(bdNeto.doubleValue());
					ra.setDmontototal(bdTotal.doubleValue());
					ra.setDmontoxcomision(bdMtoComis.doubleValue());
					ra.setMontoRetencion(bdRetencion);
					//ra.setAfiliado();
					lstPos.add(ra);
				}
			}	
			
			
		} catch (Exception error) {
			LogCajaService.CreateLog("consultarMontosPO1", "ERR", error.getMessage());
		} finally {
			sesion.close();
		}
		return lstPos;
	}	
/*********************************************************************/
/** 		 Actualizar el estado de un arqueo de caja 				 */
	public boolean actualizarEstadoArqueo(Session sesion, Varqueo v, int iCodEstado, String sMotivo, int codusermod){
		boolean bHecho = true;
		String strNuevoEstado = "P" ;
		
		try {
			
			Valorcatalogo vc = Divisas.leerValorCatalogo(1, iCodEstado);
			
			if(vc != null )
				strNuevoEstado = vc.getCodigointerno().trim();
			
			String strSqlUpdate = "update " + PropertiesSystem.ESQUEMA +".arqueo " +
					"set estado = '@NUEVOESTADO', fechamod = current_date, horamod = current_time, " +
					" CODUSERMOD = @CODUSERMOD, " +
					" DATAPCINFO = IFNULL(DATAPCINFO, '' ) || '>> upd: ' || CAST(CURRENT_TIMESTAMP AS VARCHAR(26)) ||  '@DATAPCINFO', " +
					" motivo = '"+sMotivo+"', referdep = '@REFERDEP',  referencenumber = @REFERENCENUMBER " +
					" where caid = @CAID and trim(codcomp) = '@CODCOMP' and noarqueo = @NOARQUEO " +
					" and moneda = '@MONEDA' and estado = 'P' " +
					" and fecha = '"+FechasUtil.formatDatetoString( v.getId().getFecha(), "yyyy-MM-dd")+"' " ; 
			
			String updateArqueo = strSqlUpdate.replace("@NUEVOESTADO", strNuevoEstado)
					.replace("@CAID", Integer.toString(v.getId().getCaid() ) )
					.replace("@CODCOMP", v.getId().getCodcomp().trim() )
					.replace("@NOARQUEO", Integer.toString(v.getId().getNoarqueo() ))
					.replace("@MONEDA", v.getId().getMoneda() )
					.replace("@REFERDEP", v.getId().getReferdep() )
					.replace("@REFERENCENUMBER", v.getId().getReferdep() )
					.replace("@CODUSERMOD",  Integer.toString(codusermod) )
					.replace("@DATAPCINFO",  PropertiesSystem.getDataFromPcClient() ) ;
			
			LogCajaService.CreateLog("actualizarEstadoArqueo", "QRY", updateArqueo);
			int rowsUpdated = sesion.createSQLQuery(updateArqueo).executeUpdate() ;
			
			if(rowsUpdated != 1 ){ 
				return bHecho = false;
			}
			
		} catch (Exception error) {
			bHecho = false;
			LogCajaService.CreateLog("actualizarEstadoArqueo", "ERR", error.getMessage());
			
		}
		return bHecho;
	}
	
/***********************************************************************************************/
/**		obtener los montos totales por unidad de negocio de los recibos incluidos en el arqueo */
	public static List<Rptmcaja004Sumary> obtenerTotalxUnineg(int iCaid, String sCodsuc, String sCodcomp, String sListarec,Date dtFecha, String sCasucur){
		List<Rptmcaja004Sumary> lstUnineg = null;
		
		try {
		
			String sFecha = FechasUtil.formatDatetoString(dtFecha, "yyyy-MM-dd");

			String sql = " select CAST(r.codunineg AS VARCHAR(12) CCSID 37) codunineg, CAST(MCDL01 AS VARCHAR(50) CCSID 37) AS NOMUNINEG, ";
			sql += " sum ((case when rd.mpago = '"+MetodosPagoCtrl.EFECTIVO+"' and r.tiporec not in ('PR','FCV') then rd.monto - (select cd.cambio from "+PropertiesSystem.ESQUEMA+".cambiodet cd ";
			sql += " where r.caid = cd.caid and r.codcomp = cd.codcomp and r.codsuc = cd.codsuc and r.tiporec = cd.tiporec and  r.numrec = cd.numrec ";
			sql += " and rd.moneda = cd.moneda) else rd.monto end )) total";
			sql += " from "+PropertiesSystem.ESQUEMA+".recibo r  inner join "+PropertiesSystem.ESQUEMA+".recibodet rd on r.caid = rd.caid and r.codcomp = rd.codcomp";
			sql += " and r.codsuc = rd.codsuc and r.numrec = rd.numrec and r.tiporec = rd.tiporec";
			sql += " inner join "+PropertiesSystem.JDEDTA+".F0006 on trim(MCMCU) = trim(codunineg) /*and MCSTYL  = 'IS'*/";
			sql += " and r.caid = "+iCaid+" and /*r.codsuc = '"+sCodsuc+"' and */ r.codcomp = '"+sCodcomp+"'";
			sql += " and r.fecha = '"+sFecha+"' and r.numrec in " + sListarec;
			sql += " group by r.codunineg, MCDL01";
			 
			List<Object[]> lstUnineg1 = ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, null, true);
			 
			if(lstUnineg1!=null && lstUnineg1.size()>0){	
				
				lstUnineg = new ArrayList<Rptmcaja004Sumary>() ;
				
				for(int i=0; i<lstUnineg1.size();i++){
					Object ob[] = (Object[])lstUnineg1.get(i);
					Rptmcaja004Sumary rs = new Rptmcaja004Sumary();
					rs.setCaid(iCaid);
					rs.setCodcomp(sCodcomp);
					rs.setCodsuc(sCasucur);
					rs.setCodunineg(ob[0].toString());
					rs.setNomunineg(ob[1].toString());
					rs.setMontototal((new BigDecimal(ob[2].toString()).doubleValue()));
					lstUnineg.add(rs);
				}
			}
			
		} catch (Exception error) {
			LogCajaService.CreateLog("obtenerTotalxUnineg", "ERR", error.getMessage());
		} 
		return lstUnineg;
	}
	
/*****************************************************************************/
/**  	Cargar datos de los recibos a partir de lista de recibos			**/
	public static List<Vrecibo> obtenerVrecibos(int iCaid,String sCodsuc,String sCodcomp,String sMoneda,String sLstRecibos,Date dtFechaAr){
		String sql = "",sFecha;
		List<Vrecibo> lstRecibos = null; 
		
		try {
			
			sFecha = FechasUtil.formatDatetoString(dtFechaAr, "yyyy-MM-dd");			
			sql =  " from Vrecibo v where v.id.caid = "+iCaid+ " and v.id.codcomp = '"+sCodcomp+"'"; 
			sql += " and v.id.fecha = '"+sFecha+"' ";
			sql += " and v.id.moneda = '"+sMoneda+"' and v.id.numrec in "+sLstRecibos;
			
			lstRecibos =  ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, Vrecibo.class, false); 
			
		} catch (Exception error) {
			LogCajaService.CreateLog("obtenerVrecibos", "ERR", error.getMessage());
		}  
		return lstRecibos;
	}
/********* 1. Cargar la lista de monedas de las cajas del contador ***********/
	public static List<Vmonedasxcontador> cargarMonedasxContador(int iCodContador){
		
		List<Vmonedasxcontador> lstMonedas = null;
		
		try{	
			
			String consulta = "from Vmonedasxcontador as v where v.id.contador ="+iCodContador;
			lstMonedas = ConsolidadoDepositosBcoCtrl.executeSqlQuery(consulta, Vmonedasxcontador.class, false);
 
		}catch(Exception error){
			LogCajaService.CreateLog("cargarMonedasxContador", "ERR", error.getMessage());
		} 
		return lstMonedas;
	}
/************ 2. Cargar la lista de compañías a cargo de contador *********************/
	public static List<Vcompaniaxcontador> cargarCompaniaxContador(int iCodContador){
		List<Vcompaniaxcontador> lstMonedas = null;
		
		try{
			
			String consulta =  " SELECT DISTINCT(CO.C4RP01D1) AS COMPANIA, C4RP01 AS CODCOMP, C.CACONT AS CONTADOR, '' AS CAID " 
			 +	" FROM "+PropertiesSystem.ESQUEMA+".F55CA01 C INNER JOIN "+PropertiesSystem.ESQUEMA+".F55CA014 CO ON C.CAID = CO.C4ID "
			 + 	" WHERE C.CACONT = "+iCodContador;
		 
			lstMonedas = ConsolidadoDepositosBcoCtrl.executeSqlQuery(consulta, Vcompaniaxcontador.class, true);
			 
		}catch(Exception error){
			LogCajaService.CreateLog("cargarCompaniaxContador", "ERR", error.getMessage());
		}
		return lstMonedas;
	}
	
/************ 3. Cargar la lista de facturas incluidas en el arqueo *********************/
	public List cargarFacturasArqueo(int noarqueo,int caid,String codcomp,String codsuc,Transaction trans,Session sesion){
		List lstArqueofact = new ArrayList();
		List<Vhfactura> lstVhfact = new ArrayList<Vhfactura>();
		String consulta = "";		
		
		try{
			consulta = " from Arqueofact as a where a.id.noarqueo = "+noarqueo+" and a.id.caid =" +caid+
					   " and a.id.codcomp= '"+codcomp+"' and a.id.codsuc = '"+codsuc+"'";
			
			lstArqueofact = sesion.createQuery(consulta).list();
			if(lstArqueofact == null)
				lstArqueofact = new ArrayList();
			else{
				//obtener el registro completo de la factura desde vhfactura.				
				for(int i=0; i<lstArqueofact.size();i++){
					Arqueofact a = (Arqueofact)lstArqueofact.get(i);
					Vhfactura vh = obtenerVhfactura(a.getId().getNumfac(), a.getId().getTipo(),
									a.getId().getCodunineg().trim(),codcomp, codsuc, sesion, trans);
					lstVhfact.add(i,vh);
				}			
			}
		}catch(Exception error){
			LogCajaService.CreateLog("cargarFacturasArqueo", "ERR", error.getMessage());
		}
		return lstVhfact;
	}
	
/************ 4. obtener un registro de vhfactura por numero, caja, compañía, sucursal *********************/
	public Vhfactura obtenerVhfactura(int nofact,String tipofact,String sCodunineg, String codcomp,String codsuc,Session sesion, Transaction trans){		
		Vhfactura factura = new Vhfactura();		
		String consulta = "";
		boolean bUnico = false;
		
		try{
			consulta =  " from Vhfactura as f where f.id.nofactura = "+nofact;
			consulta += " and f.id.codcomp = '"+codcomp.trim()+"' and trim(f.id.codunineg) = '"+sCodunineg+"' ";
			consulta += " and f.id.tipofactura ='"+tipofact.trim()+"'";		
			
			if(trans == null){
				bUnico = true;
				sesion = HibernateUtilPruebaCn.currentSession();
				trans = sesion.beginTransaction();
			}
			Object ob = sesion.createQuery(consulta).uniqueResult();
			
			if(bUnico)
				trans.commit();
			
			if(ob!=null)
				factura = (Vhfactura)ob;
			
		}catch(Exception error){
			LogCajaService.CreateLog("obtenerVhfactura", "ERR", error.getMessage());
		}finally{
			try{if(bUnico){trans = null; sesion.close();} }catch(Exception ex2){ex2.printStackTrace();};
		}
		return factura;
	}
/** SIN USAR ****************** 5. Cargar los recibos incluidos en el arqueo ***************************************/
	@SuppressWarnings("unchecked")
	public List cargarRecibosArqueo(int noarqueo,int caid,String codcomp,String codsuc,
									Transaction trans,Session sesion){
		ArrayList<Arqueorec> lstArqueorec = new ArrayList<Arqueorec>();
		ArrayList<Recibo>lstRecibos = null;
		Criteria cr = null;
		
		try{
			cr = sesion.createCriteria(Arqueorec.class)
							.add(Restrictions.eq("id.noarqueo",noarqueo))
							.add(Restrictions.eq("id.caid",caid))
							.add(Restrictions.eq("id.codcomp",codcomp))
							.add(Restrictions.eq("id.codsuc",codsuc));
			lstArqueorec =(ArrayList<Arqueorec>)cr.list();
			if(lstArqueorec!=null && lstArqueorec.size()>0){
				lstRecibos = new ArrayList<Recibo>(lstArqueorec.size());
				for (Arqueorec ar : lstArqueorec) {
					Recibo r  =  obtenerRecibo(ar.getId().getNumrec(), caid, codcomp, codsuc, sesion, trans);
					lstRecibos.add(r);
				}
			}
		}catch(Exception error){
			LogCajaService.CreateLog("cargarRecibosArqueo", "ERR", error.getMessage());
		}
		return lstRecibos;
	}
/*************************************************************************************************/
/** cargar lista con los numeros de recibos incluidos en el arqueo
 *********/	
	@SuppressWarnings("unchecked")
	public static String cargarRecibosArqueo(int noarqueo,int caid,String codcomp,String codsuc,Session sesion){
		List<Integer> lstArqueorec = new ArrayList<Integer>();
		String sLstRecibos = "";
		Criteria cr;
		
		try{
			cr = sesion.createCriteria(Arqueorec.class)
							.add(Restrictions.eq("id.noarqueo", noarqueo))
							.add(Restrictions.ilike("id.codcomp", codcomp.trim().toLowerCase(), MatchMode.ANYWHERE))
							.add(Restrictions.ilike("id.codsuc",  codsuc.trim().toLowerCase(),  MatchMode.ANYWHERE))
							.add(Restrictions.eq("id.caid", caid))
							.add(Restrictions.in("id.tipodoc", new String[]{"R","S"}));
			cr.setProjection(Projections.property("id.numrec"));
			lstArqueorec =(ArrayList<Integer>)cr.list();
			
			if(lstArqueorec!=null && lstArqueorec.size()>0){
				
				sLstRecibos = lstArqueorec.toString().replace("[","(").replace("]",")") ;
				
			}
		}catch(Exception error){
			sLstRecibos = "";
			LogCajaService.CreateLog("cargarRecibosArqueo", "ERR", error.getMessage());
		}
		return sLstRecibos;
	}	
/****************************** 6. Obtener Registro de recibo  ***************************************/
	public Recibo obtenerRecibo(int numrec,int caid, String codcomp,String codsuc,Session sesion, Transaction trans){
		Recibo re = new Recibo();
		Transaction tx = null;
		String consulta = "";
		boolean bUnico = false;
		
		try{
			consulta = " from Recibo as r where r.id.numrec ="+numrec+" and r.id.caid = "+caid+
					   " and r.id.codcomp = '"+codcomp+"' and r.id.codsuc = '"+codsuc+"' and r.tiporec <> 'FCV'";
			
			if(trans==null){
				bUnico = false;
				sesion = HibernateUtilPruebaCn.currentSession();
				tx = sesion.beginTransaction();
			}
			Object ob = sesion.createQuery(consulta).uniqueResult();
					
			if(ob!=null){
				re = (Recibo)ob;
				consulta = " from Recibodet as r where r.id.numrec ="+re.getId().getNumrec()+" and r.id.caid = "+re.getId().getCaid()+
						   " and r.id.codcomp = '"+re.getId().getCodcomp()+"' and r.id.codsuc = '"+re.getId().getCodsuc()+"'";
				List lstrd = sesion.createQuery(consulta).list();
				if(lstrd !=null)
					re.setMetodosPago(lstrd);
			}else
				re = new Recibo();
			
			if(trans == null)
				tx.commit();
						
		}catch(Exception error){
			LogCajaService.CreateLog("obtenerRecibo", "ERR", error.getMessage());
		}finally{
			try{sesion.close();}catch(Exception ex2){LogCajaService.CreateLog("obtenerRecibo", "ERR", ex2.getMessage());};
		}
		return re;
	}
/******************* 7 generar una lista con registros de tipo vrecibosxtipoegreso *****************/
	public List obtenerRecibostmp(List lstRecibos,String sMpago,int iCaid, String sCodcomp,String sCodsuc){
		List lstRecibostmp = new ArrayList();
		String consulta = "",sRecibos="(";
		Session sesion = HibernateUtilPruebaCn.currentSession();
		Transaction trans = null;
		
		try{
			if(lstRecibos != null && lstRecibos.size()>0){
				for(int i=0; i<lstRecibos.size();i++){				
					if(i==(lstRecibos.size()-1))
						sRecibos += ((Vrecibosxtipoegreso)lstRecibos.get(i)).getId().getNumrec() + ")";
					else
						sRecibos += ((Vrecibosxtipoegreso)lstRecibos.get(i)).getId().getNumrec() + ",";				
				}
				consulta =  " from Vrecibosxtipoegreso as v where v.id.caid = "+iCaid + " and ";
				consulta += " v.id.codcomp = '"+sCodcomp+"' and v.id.codsuc = '"+sCodsuc+"' and";
				consulta += " v.id.numrec in "+sRecibos + "and v.id.mpago = '"+sMpago+"' and v.id.tiporec <> 'FCV'";
				
				trans = sesion.beginTransaction();
				lstRecibostmp = sesion.createQuery(consulta).list();
				trans.commit();
				
				if(lstRecibostmp != null && lstRecibos.size()>0){
					for(int i=0; i<lstRecibostmp.size();i++){
						Vrecibosxtipoegreso v = (Vrecibosxtipoegreso)lstRecibostmp.get(i);
						v.setMonto(v.getId().getMonto());
						lstRecibostmp.remove(i);
						lstRecibostmp.add(i,v);
					}
				}
			}	
		}catch(Exception error){
			LogCajaService.CreateLog("obtenerRecibostmp", "ERR", error.getMessage());
		}finally{
			try{sesion.close();}catch(Exception ex2){ex2.printStackTrace();};
		}		
		return lstRecibostmp;
	}
	
/******************* 8 generar una lista con registros de tipo Vrecibosxtipompago *****************/
	@SuppressWarnings("unchecked")
	public List obtenerRecibosmpago(List lstRecibos,String sMpago,int iCaid, String sCodcomp,String sCodsuc,String sMoneda){
		List lstRecibostmp = new ArrayList();
		String consulta = "",sRecibos="(";
		Session sesion = HibernateUtilPruebaCn.currentSession();

		try{				
			for(int i=0; i<lstRecibos.size();i++){				
				if(i==(lstRecibos.size()-1))
					sRecibos += ((Vrecibosxtipompago)lstRecibos.get(i)).getId().getNumrec() + ")";
				else
					sRecibos += ((Vrecibosxtipompago)lstRecibos.get(i)).getId().getNumrec() + ",";				
			}
			consulta =  " from Vrecibosxtipompago as v where v.id.caid = "+iCaid + " and ";
			consulta += " v.id.codcomp = '"+sCodcomp+"' and v.id.codsuc = '"+sCodsuc+"' and";
			consulta += " v.id.numrec in "+sRecibos + " and v.id.mpago = '"+sMpago+"' and";
			consulta += " v.id.moneda = '"+sMoneda+"' and v.id.tiporec <> 'FCV'";
			
			LogCajaService.CreateLog("obtenerRecibosmpago", "QRY", consulta);
			
			lstRecibostmp = sesion.createQuery(consulta).list();

			if(lstRecibostmp != null && lstRecibos.size()>0){
				for(int i=0; i<lstRecibostmp.size();i++){
					Vrecibosxtipompago v = (Vrecibosxtipompago)lstRecibostmp.get(i);
					
					if (v.getId().getTiporec().trim().toUpperCase().equals("DCO")) {
						v.setMonto(v.getId().getMonto().multiply(BigDecimal.valueOf(-1)));
					} else {
						v.setMonto(v.getId().getMonto());
					}
					
					lstRecibostmp.remove(i);
					lstRecibostmp.add(i,v);
				}
			}
			
		}catch(Exception error){
			LogCajaService.CreateLog("obtenerRecibosmpago", "ERR", error.getMessage());
		}

		return lstRecibostmp;
	}
/******************* 9. generar una lista con registros de tipo Vrecibosxtipoingreso *****************/
	public List obtenerRecibosxTipo(List lstRecibos,String sTipo,int iCaid, String sCodcomp,String sCodsuc){
		List lstRecibostmp = new ArrayList();
		String consulta = "",sRecibos="(";
		Session sesion = HibernateUtilPruebaCn.currentSession();
		Transaction trans = null;
		
		try{				
			for(int i=0; i<lstRecibos.size();i++){				
				if(i==(lstRecibos.size()-1))
					sRecibos += ((Vrecibosxtipoegreso)lstRecibos.get(i)).getId().getNumrec() + ")";
				else
					sRecibos += ((Vrecibosxtipoegreso)lstRecibos.get(i)).getId().getNumrec() + ",";				
			}
			consulta =  " from Vrecibosxtipoingreso as v where v.id.caid = "+iCaid + " and ";
			consulta += " v.id.codcomp = '"+sCodcomp+"' and v.id.codsuc = '"+sCodsuc+"' and";
			consulta += " v.id.numrec in "+sRecibos + " and v.id.tiporec = '"+sTipo+"'";
			
			trans = sesion.beginTransaction();
			lstRecibostmp = sesion.createQuery(consulta).list();
			trans.commit();
			
			if(lstRecibostmp != null && lstRecibos.size()>0){
				for(int i=0; i<lstRecibostmp.size();i++){
					Vrecibosxtipoingreso v = (Vrecibosxtipoingreso)lstRecibostmp.get(i);
					v.setMonto(v.getId().getMonto());
					lstRecibostmp.remove(i);
					lstRecibostmp.add(i,v);
				}
			}
			
		}catch(Exception error){
			LogCajaService.CreateLog("obtenerRecibosxTipo", "ERR", error.getMessage());
		}	
		finally{
			try{sesion.close();}catch(Exception ex2){LogCajaService.CreateLog("obtenerRecibosxTipo", "ERR", ex2.getMessage());};
		}	
		return lstRecibostmp;
	}
/******************* 10. Obtener la cuenta de caja por moneda ************************************/
	public F55ca011 obtenerCuentaCajaxMoneda(int iCaid,String sCodsuc,String sCodcomp,String sMpago,
											String sMoneda,Session sesion,Transaction trans){
		String sConsulta = "";
		F55ca011 f55ca011 = null;		
		Transaction tx = null;
		
		try{
			sConsulta =  "from F55ca011 as f where f.id.c1id = "+iCaid + " and trim(f.id.c1mcu) = '"+sCodsuc.substring(3)+"'";
			sConsulta += " and trim(f.id.c1rp01) = '"+sCodcomp.trim()+"' and f.id.c1crcd = '"+sMoneda+"'";
			sConsulta += " and f.id.c1stat = 'A' and f.id.c1ryin  ='"+sMpago+"'";
			
			if(trans == null){
				sesion = HibernateUtilPruebaCn.currentSession();
				tx = sesion.beginTransaction();
			}			
			Object ob = sesion.createQuery(sConsulta).uniqueResult();			
				
			if(ob != null)
				f55ca011 = (F55ca011)ob;
			
		}catch(Exception error){
			LogCajaService.CreateLog("obtenerCuentaCajaxMoneda", "ERR", error.getMessage());
		}finally{
			if(trans == null){
				tx.commit();
				sesion.close();
			}
		}
		return f55ca011;
	}
	
/******************* 11. Obtener la cuenta de tránsito a banco ************************************/
	public F55ca023 obtenerCtaTranBanco(String sCodcomp,String sMoneda,Session sesion,Transaction trans){
		F55ca023 cuenta = null;		
		Transaction tx = null;
		String sConsulta = "";
		
		try{
			sConsulta =  " from F55ca023 as f where trim(f.id.d3rp01) = '" + sCodcomp.trim()+"'";
			sConsulta += " and trim(f.id.d3rp01) = '" + sCodcomp.trim()+"' and f.id.d3crcd = '"+sMoneda+"' and f.id.d3stat = 'A'";
						
			if(trans == null){
				sesion = HibernateUtilPruebaCn.currentSession();
				tx = sesion.beginTransaction();
			}			
			Object ob = sesion.createQuery(sConsulta).uniqueResult();			
			
			if(ob != null)
				cuenta = (F55ca023)ob;
			
		}catch(Exception error){
			LogCajaService.CreateLog("obtenerCtaTranBanco", "ERR", error.getMessage());	
		}finally{
			if(trans == null){
				tx.commit();
				sesion.close();
			}
		}
		return cuenta;
	}
/************************OBTENER NUMERO DE BATCH ACTUAL**************************************************/
	public int obtenerNoBatchActual(){
		int iLastBatch = 0;	
		Session sesion = HibernateUtilPruebaCn.currentSession();
		Transaction trans = null;
		
		try{
			trans = sesion.beginTransaction();
			Object ob = sesion.createQuery("select f.id.nnn001 from Vf0002 f where trim(f.id.nnsy) = '00'").uniqueResult();
			trans.commit();
			
			if(ob != null)			
				iLastBatch = Integer.parseInt(ob.toString());
			
		}catch(Exception ex){
			LogCajaService.CreateLog("obtenerNoBatchActual", "ERR", ex.getMessage());	
		}finally{
			try{sesion.close();}catch(Exception ex2){ex2.printStackTrace();};
		}
		return iLastBatch;
	}
/************************LEER NUMERO DE DOCUMENTO A USAR*********************************************/	
	public int leerNumeroDocumento(){
		int iNumDoc = 0;
		Session sesion = HibernateUtilPruebaCn.currentSession();
		Transaction trans = null;
		try{
			trans = sesion.beginTransaction();
			Object ob = sesion.createQuery("select f.id.nnn002 from Vf0002 f where trim(f.id.nnsy) = '09'").uniqueResult();
			trans.commit();
			
			if(ob != null)
				iNumDoc = Integer.parseInt(ob.toString());			
			
		}catch(Exception ex){			
			LogCajaService.CreateLog("leerNumeroDocumento", "ERR", ex.getMessage());	
		}finally{
			sesion.close();
		}
		return iNumDoc;
	}
/*** SIN USAR **** OBTENER LOS RECIBOS QUE CORRESPONDEN A DEVOLUCIONES DE FACTURA DEL DIA A PARTIR DE UNA LISTA DE RECIBOS *****/
	public List obtenerRecibosxdevolucion(boolean bEgresos,int iCaid,String sCodsuc,String sCodcomp,String sTiporec,String sMoneda,Date fecha,String sListaRec){
		List lstDev = new ArrayList();
		Session sesion = HibernateUtilPruebaCn.currentSession();
		Transaction trans = null;
		String sConsulta = "",sFecha = "";
		int iFechajul = 0;
		Date dtFecha;
		Calendar cal;		
		
		try{
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			sFecha = format.format(fecha);	
			
			dtFecha = (Date)format.parse(sFecha);
			cal = Calendar.getInstance();
		    cal.setTime(dtFecha);
			
		    CalendarToJulian ctj = new CalendarToJulian(cal);
			iFechajul = ctj.getDate();			
	
			sConsulta = " from Vreciboxdevolucion as v where v.id.caid = "+iCaid+" and v.id.codcomp = '"+sCodcomp+"'";
			sConsulta += " and v.id.codsuc = '"+sCodsuc+"' and v.id.fecha = '"+sFecha+"'";
			sConsulta += " and v.id.fechadev ="+iFechajul+ " and v.id.fechafact = " +iFechajul;
			sConsulta += " and v.id.tiporec = '"+sTiporec+"'and v.id.moneda = '"+sMoneda+"' and v.id.estado <> 'A'";
			sConsulta += " and v.id.numrec in "+sListaRec;
						
			if(bEgresos)
				sConsulta += " and v.id.monfac = '"+sMoneda+"'" ;
			
			trans = sesion.beginTransaction();
			lstDev = sesion.createQuery(sConsulta).list();
			trans.commit();
			
		}catch(Exception error){
			LogCajaService.CreateLog("obtenerRecibosxdevolucion", "ERR", error.getMessage());	
		}finally{
			sesion.close();
		}
		return lstDev;
	}
/************************************************************************************/
/** obtener todos los recibos q no se pagaron con efectivo
 ********/
	public List obtenerRecpagBanco(int iCaid, String sCodsuc,String sCodcomp, String sMoneda,
									Date dtFecha,Date dtHora,String sLstNumrec,Session sesion){
		List lstRecibos = new ArrayList();
		String sConsulta = "",sFecha = "",sHora = "";
		SimpleDateFormat format;
		
		try{
			format = new SimpleDateFormat("yyyy-MM-dd");
			sFecha = format.format(dtFecha);	
			
			format = new SimpleDateFormat("HH.mm.ss");
			sHora = format.format(dtHora);
			
			/*
			 * 28/04/2010
			 * Cambio agregado: quitar el filtro de que busque recibos de igual moneda de la factura.
			 * Para que en la sección de egresos salgan los métodos pagados en banco de monedas de 
			 * distintas a la de la factura.
			 * También se permite que ingresos por moneda extragera se incluyen todos los métodos de pago.
			 */
			
			sConsulta =  " from Vrecibosxtipoegreso as v where v.id.caid = "+iCaid + " and v.id.codcomp = '"+sCodcomp+"'";
			sConsulta += " and v.id.codsuc = '"+sCodsuc+"' and v.id.rfecha = '"+sFecha+"' and v.id.hora <= '"+sHora+"'";
			sConsulta += " and v.id.rmoneda = '"+sMoneda+"' and v.id.tiporec <> 'FCV'";
			sConsulta += " and v.id.numrec in " +sLstNumrec;
			
			lstRecibos = sesion.createQuery(sConsulta).list();
			
		}catch(Exception error){
			lstRecibos = null;
			LogCajaService.CreateLog("obtenerRecpagBanco", "ERR", error.getMessage());
		}		
		return lstRecibos;
	}
	
/******* OBTENER LOS RECIBOS QUE CORRESPONDEN A DEVOLUCIONES DE FACTURA DEL DIA *****/
	public List obtenerRecibosxdevolucion(boolean bEgresos,int iCaid,String sCodsuc,
											String sCodcomp,String sTiporec,String sMoneda,
											Date dtFecha,Date dtHora,String sLstNumrec,Session sesion){
		List lstDev = new ArrayList();
		String sConsulta = "",sFecha = "",sHora = "";
		int iFechajul = 0;
		Calendar cal;		
		SimpleDateFormat format;
		
		try{
			format = new SimpleDateFormat("HH.mm.ss");
			sHora = format.format(dtHora);
			format = new SimpleDateFormat("yyyy-MM-dd");
			sFecha = format.format(dtFecha);
			iFechajul = new FechasUtil().obtenerFechaJulianaDia(dtFecha);
			
			/* 
			 * Cambio 07/05/2010: Incluir los pagos a devoluciones de facturas de días anteriores.
			 */
			sConsulta = " from Vreciboxdevolucion as v where v.id.caid = "+iCaid+" and v.id.codcomp = '"+sCodcomp+"'";
			sConsulta += " and v.id.codsuc = '"+sCodsuc+"' and v.id.fecha = '"+sFecha+"' and v.id.hora <= '"+sHora+"'";
			sConsulta += " and v.id.fechadev ="+iFechajul; //+ " and v.id.fechafact = " +iFechajul;
			sConsulta += " and v.id.tiporec = '"+sTiporec+"'and v.id.moneda = '"+sMoneda+"'";
			sConsulta += " and v.id.numrec in "+ sLstNumrec;			
			
			if(bEgresos)
				sConsulta += " and v.id.monfac = '"+sMoneda+"'" ;
			
			lstDev = sesion.createQuery(sConsulta).list();
			
		}catch(Exception error){
			LogCajaService.CreateLog("obtenerRecibosxdevolucion", "ERR", error.getMessage());
		}
		return lstDev;
	}
/**** 1. cargar los datos para los cambios otorgados x pago de recibos ************************
 * 	  se incluyen todos los recibos por los cambios de pago con 1 moneda diferente de la fact *
 * ******************/
	public List cargarDetCambio(int iCaid,String sCodcomp,String sCodsuc,
								String sMoneda,Date dtFecha,Date dtHora,
								String sLstnumrec,Session sesion){
		List lstCambios = new ArrayList();
		String consulta = "",sFecha = "",sHora = "";
		SimpleDateFormat format;
		
		try{
			format = new SimpleDateFormat("HH.mm.ss");
			sHora = format.format(dtHora);
			
			format = new SimpleDateFormat("yyyy-MM-dd");
			sFecha = format.format(dtFecha);

			consulta =  " from Vdetallecambiorecibo as v where v.id.caid = "+iCaid;
			consulta += " and v.id.codcomp = '"+sCodcomp+"' and v.id.codsuc = '"+sCodsuc+"'";
			consulta += " and v.id.monedacamb = '"+sMoneda+"' and v.id.cambio > 0";
			consulta += " and v.id.fecha = '"+sFecha+"'	and v.id.hora <= '"+sHora+"' and v.id.tiporec <> 'FCV' ";
			consulta += " and v.id.numrec in "+sLstnumrec;
			lstCambios = sesion.createQuery(consulta).list();		
			
		}catch(Exception error){
			lstCambios = null;
			LogCajaService.CreateLog("cargarDetCambio", "ERR", error.getMessage());
		}	
		return lstCambios;
	}
/******************************************************************************/	
/** Obtener recibos pagados con moneda de arqueo y distinta de la factura *****
 * ****************/
	public List obtenerIngEgrRecMonEx(boolean bIngreso,int iCaid, String sCodcomp,String sCodsuc,
									String sMoneda,Date dtFecha,Date dtHora,
									String sLstnumrec,Session sesion){
		List lstRecibos = new ArrayList();
		String sConsulta,sFecha,sHora;
		SimpleDateFormat format;
		
		try{
			format = new SimpleDateFormat("HH.mm.ss");
			sHora  = format.format(dtHora);
			
			format = new SimpleDateFormat("yyyy-MM-dd");
			sFecha = format.format(dtFecha);

			sConsulta =  " from Vmonedafactrec as v where v.id.caid = "+iCaid+ " and v.id.codsuc ='"+sCodsuc+"'";
			sConsulta += " and trim(v.id.codcomp) = '"+sCodcomp.trim()+"' and v.id.tiporec <> 'FCV'";
			sConsulta += " and v.id.fecha = '"+sFecha+"' and v.id.horamod <= '"+sHora+"'";
			
			if(bIngreso)
				sConsulta += " and v.id.rmoneda = '"+sMoneda+"'and v.id.fmoneda <> '"+sMoneda+"'";
			else
				sConsulta += " and v.id.rmoneda <> '"+sMoneda+"'and v.id.fmoneda = '"+sMoneda+"'";
			
			sConsulta += " and v.id.forarecibido > 0";
			sConsulta += " and v.id.numrec in "+ sLstnumrec+ " order by v.id.numrec";
			
			lstRecibos = sesion.createQuery(sConsulta).list();
			
		}catch(Exception error){
			LogCajaService.CreateLog("obtenerIngEgrRecMonEx", "ERR", error.getMessage());
		}
		return lstRecibos;
	}
	
	/************************************************************************/
	/** Obtener los recibos que aplican cambios mixtos **********************
	 * ***********************/
	public List obtenerRecibosxcambiomixto(int iCaid,String sCodcomp,String sCodsuc,
											String sMoneda,Date dtFecha,Date dtHora,
											String sLstnumrec,Session sesion){
		List lstRec = new ArrayList();
		String sConsulta,sFecha,sHora;
		
		try{	
			sHora = new SimpleDateFormat("HH.mm.ss").format(dtHora);
			sFecha = new SimpleDateFormat("yyyy-MM-dd").format(dtFecha);
			
			sConsulta =  " from Vdetallecambiorecibo as v where v.id.caid = "+iCaid ;
			sConsulta += " and v.id.codsuc = '"+sCodsuc+"' and v.id.codcomp = '"+sCodcomp+"'";
			sConsulta += " and v.id.monedafac = '"+sMoneda+"' and v.id.monedarec = '"+sMoneda+"'";
			sConsulta += " and v.id.cantmon = 1 and v.id.monedacamb <> '"+sMoneda+"'";
			sConsulta += " and v.id.fecha = '"+sFecha+"' and v.id.hora <= '"+sHora+"'";
			sConsulta += " and v.id.cambio > 0 and v.id.numrec in "+sLstnumrec;
			sConsulta += " and v.id.tiporec not in ('EX','FCV')";
			
			lstRec = sesion.createQuery(sConsulta).list();
						
		}catch(Exception error){
			lstRec = null;
			LogCajaService.CreateLog("obtenerRecibosxcambiomixto", "ERR", error.getMessage());
		}		
		return lstRec;
	}
/****************** Obtener los recibos generados por egresos  por tipo y mpago ********************/
	public List cargarRecibosxMetodoPago(int iCaid, String sCodsuc,String sCodcomp, String sMoneda,
										 Date dtFecha,Date dtHora,String sLstnumrec,Session sesion){
		List recibos = new ArrayList();
		String sConsulta,sFecha,sHora;
		
		try{			
			sHora = new SimpleDateFormat("HH.mm.ss").format(dtHora);
			sFecha =  new SimpleDateFormat("yyyy-MM-dd").format(dtFecha);
			
			sConsulta  =  " from Vrecibosxtipompago as v where v.id.caid = "+iCaid+" and v.id.codsuc ='"+sCodsuc+"'";
			sConsulta +=  " and v.id.codcomp='"+sCodcomp+"' and v.id.moneda = '"+sMoneda+"'";
			sConsulta +=  " and v.id.fecha = '"+sFecha+"' and v.id.hora <= '"+sHora+"' and v.id.tiporec <> 'FCV' ";
			sConsulta +=  " and v.id.numrec in "+sLstnumrec + "and lower(v.id.tiporec) not like 'd%' order by v.id.numrec";			 
			
			recibos = sesion.createQuery(sConsulta).list();
			
		}catch(Exception error){
			recibos = null;
			LogCajaService.CreateLog("cargarRecibosxMetodoPago", "ERR", error.getMessage());
		}	
		return recibos;
	}
/********* obtener los recibos que aplicaron cambios en el día de cualquier tipo CR, CO.********************/
	public List obtenerRecibosxcambios(int iCaid,String sCodcomp,String sCodsuc,String sMoneda,Date dtFecha,
									 	Date dtHora,String sLstnumrec,Session sesion){
		List lstRec = new ArrayList();
		String sConsulta,sFecha,sHora;
		
		try{	
			sHora = new SimpleDateFormat("HH.mm.ss").format(dtHora);
			sFecha = new SimpleDateFormat("yyyy-MM-dd").format(dtFecha);
			sConsulta =  " from Vdetallecambiorec as v where v.id.caid = "+iCaid;
			sConsulta += " and v.id.codsuc = '"+sCodsuc+"' and v.id.codcomp = '"+sCodcomp+"'";
			sConsulta += " and v.id.monedacamb = '"+sMoneda+"' and v.id.cambio > 0 and v.id.tiporec <> 'FCV'";
			sConsulta += " and v.id.fecha = '"+sFecha+"' and v.id.hora <= '"+sHora+"' and v.id.numrec in "+sLstnumrec;
			
			lstRec = sesion.createQuery(sConsulta).list();
						
		}catch(Exception error){
			LogCajaService.CreateLog("obtenerRecibosxcambios", "ERR", error.getMessage());
		}		
		return lstRec;
	}
	/************************************************************************************/
	/** obtener los recibos por pago de devoluciones con moneda distinta de la factura.
	 *  obtener recibos por devolución pagadas con moneda extranjera (diferente a moneda fac)
	 ********/
	public List obtenerRecxDevmonex(boolean bIngreso,int iCaid,String sCodsuc,String sCodcomp,
									String sMoneda,String sTiporec,Date dtFecha,Date dtHora,
									String sLstnumrec,Session sesion){
		List lstDev = new ArrayList();
		String sConsulta,sFecha,sHora;
		int iFechajul = 0;
		
		try{			
			sHora	  = new SimpleDateFormat("HH.mm.ss").format(dtHora);
			sFecha	  = new SimpleDateFormat("yyyy-MM-dd").format(dtFecha);	
			iFechajul = new FechasUtil().obtenerFechaJulianaDia(dtFecha);
			
			sConsulta =  " from Vreciboxdevolucion as v where v.id.caid = "+iCaid+" and v.id.codsuc = '"+sCodsuc+"'";
			sConsulta += " and v.id.codcomp = '"+sCodcomp+"' and v.id.fecha = '"+sFecha+"' and v.id.hora <= '"+sHora+"'";			
			sConsulta += " and v.id.fechadev = "+iFechajul+" and v.id.fechafact = "+iFechajul;
			sConsulta += " and v.id.tiporec = '"+sTiporec+"' and v.id.numrec in "+sLstnumrec;
			
			if(bIngreso)
				sConsulta += " and v.id.mpago = '"+MetodosPagoCtrl.EFECTIVO+"' and v.id.moneda = '"+sMoneda+"' and v.id.monfac <> '"+sMoneda+"'";
			else
				sConsulta += " and v.id.moneda <> '"+sMoneda+"' and v.id.monfac = '"+sMoneda+"'";
			
			lstDev = sesion.createQuery(sConsulta).list();
			
		}catch(Exception error){
			lstDev  = null;
			LogCajaService.CreateLog("obtenerRecxDevmonex", "ERR", error.getMessage());
		}
		return lstDev;
	}
/**
 * Obtener el total por devoluciones del dia a partir de una lista de devoluciones.
 * @param lstDev lista de devoluciones
 * @param dtFecha fecha a consultar
 * @return	total por devoluciones en la lista.
 */
	@SuppressWarnings("unchecked")
	public double obtenerTotalxdevdia(List<Hfactura> lstDev,Date dtFecha){
		double dTotal = 0;
		Session sesion = null;
		Transaction trans = null;
		boolean newCn = false;
		
		try{
			String sLstNumDev = "" ;
			for (Hfactura hf : lstDev) {
				sLstNumDev += hf.getNofactura()+",";
			}
			sLstNumDev = "("+sLstNumDev.substring(0, sLstNumDev.length()-1 )+")";
			
			String sConsulta = " from Vfacturaxdevolucion as v where v.id.nodev in "
								+ sLstNumDev + " and v.id.fechadev = " 
								+FechasUtil.DateToJulian(dtFecha);
			
			sesion = HibernateUtilPruebaCn.currentSession();
			trans = (newCn = !(sesion.getTransaction().isActive())) ? sesion
					.beginTransaction() : sesion.getTransaction();
			
			List<Vfacturaxdevolucion>lstFacsDev = sesion.createQuery(sConsulta).list();
			
			if(lstFacsDev == null || lstFacsDev.isEmpty())
				return 0;
				
			
			BigDecimal bdTotal = BigDecimal.ZERO;
				
			for (Vfacturaxdevolucion vf : lstFacsDev)
				bdTotal = bdTotal.add(new BigDecimal(
						Long.toString(vf.getId().getTotal())));
			
			dTotal = bdTotal.divide(new BigDecimal("100"), 2,
					RoundingMode.HALF_UP).doubleValue();
				
			
		}catch(Exception error){
			LogCajaService.CreateLog("obtenerTotalxdevdia", "ERR", error.getMessage());
			error.printStackTrace();
		}finally{
			
			if(newCn){
				try {  trans.commit(); } 
				catch (Exception e2) { }
				try {  HibernateUtilPruebaCn.closeSession(sesion); }
				catch (Exception e2) { }
			}

		}
		return dTotal;
	}
/*************************************************************************************************/
/** Obtener los recibos que se no se pagaron en efectivo.. sin devoluciones                      */	
	public List<Vrecibodevrecibo> obtenerRecibosSinDev(int iCaid,String sCodcomp,String sCodsuc,String sMoneda,String sMpago,String sListaRec,Date dtFecha){
		List<Vrecibodevrecibo> lstRecibos = null;
		
		String sConsulta = "",sFecha;		
		SimpleDateFormat format;
		
		try{
			
			
			format = new SimpleDateFormat("yyyy-MM-dd");
			sFecha = format.format(dtFecha);	
			
			sConsulta =  " from Vrecibodevrecibo as v where v.id.fecha = '"+sFecha+"' and v.id.caid = "+iCaid ;
			sConsulta += " and v.id.codsuc = '"+sCodsuc+"' and v.id.codcomp = '"+sCodcomp+"' and tiporec not in ('FCV','DCO','DCR')";
			sConsulta += " and v.id.mpago in "+sMpago + "and (v.id.devolucion = 0 or v.id.montoneto <> v.id.monto)";
			sConsulta += " and v.id.moneda = '"+sMoneda+"' and v.id.numrec in "+sListaRec +" order by v.id.mpago";
			
			lstRecibos = ConsolidadoDepositosBcoCtrl.executeSqlQuery(sConsulta, Vrecibodevrecibo.class, false);
			
			

			
		} catch (Exception error) {
			LogCajaService.CreateLog("obtenerRecibosSinDev", "ERR", error.getMessage());
		}
		return lstRecibos;
	}
	@SuppressWarnings("unchecked")
	public List<Vrecibodevrecibo> obtenerRecibosSinDev(int iCaid,String sCodcomp,String sCodsuc,String sMoneda,String sMpago,String sListaRec,Date dtFecha, Session session){
		List<Vrecibodevrecibo> lstRecibos = null;
		String sConsulta = "",sFecha;		
		SimpleDateFormat format;
		
		try{
			
			
			format = new SimpleDateFormat("yyyy-MM-dd");
			sFecha = format.format(dtFecha);	
			
			sConsulta =  " from Vrecibodevrecibo as v where v.id.fecha = '"+sFecha+"' and v.id.caid = "+iCaid ;
			sConsulta += " and v.id.codsuc = '"+sCodsuc+"' and v.id.codcomp = '"+sCodcomp+"' and tiporec not in ('FCV','DCO','DCR')";
			sConsulta += " and v.id.mpago in "+sMpago + "and (v.id.devolucion = 0 or v.id.montoneto <> v.id.monto)";
			sConsulta += " and v.id.moneda = '"+sMoneda+"' and v.id.numrec in "+sListaRec +" order by v.id.mpago";
			
			lstRecibos = session.createQuery(sConsulta).list();

			
		} catch (Exception error) {
			LogCajaService.CreateLog("obtenerRecibosSinDev", "ERR", error.getMessage());
		} 
		return lstRecibos;
	}
/****************************************************************************************/
/** Método: Consulta los recibos pagados con tarjeta de crédito, y obtiene las sumas netas
 * 			por unidades de negocio. 
 *	Fecha:  04/01/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 **/
	@SuppressWarnings("unchecked")
	public List<Object[]> obtenerMontoPOSxUN(int iCaid,String sCodcomp,String sCodsuc,String sMoneda,
			String sListaRec,Date dtFecha, List<DncCierreDonacion> cierresDnc){
		
		List<Object[]>lstRecibos = null;
		Session sesion = null;
		Transaction trans = null;
		String sql="", sFecha = "";
 
		
		try {
 
			
			String strSql = 
				" select sum(v.monto - v.totaldev), v.afcomision, v.afnombre, v.refer1, trim(v.codunineg), v.caidpos, v.marcatarjeta " +
				"from  @BDESQUEMA.Vdetrecibodev v " +
				"where v.caid = @CAID and v.codcomp = '@CODCOMP' and v.tiporec not in ('FCV','DCO') " +
				"and v.mpago  = '"+MetodosPagoCtrl.TARJETA+"' and v.moneda = '@MONEDA' and v.numrec in @NUMEROS_RECIBO " +
				"and v.liquidarpormarca = 1 and v.fecha = '@FECHAARQUEO'"  +
				"group by v.refer1, v.afcomision, v.afnombre, v.codunineg, v.caidpos, v.marcatarjeta " +
				
				"union all " + 

				" select sum(v.monto - v.totaldev), v.afcomision, v.afnombre, v.refer1, trim(v.codunineg), v.caidpos, 'Genérico' " +
				"from  @BDESQUEMA.Vdetrecibodev v " +
				"where v.caid = @CAID and v.codcomp = '@CODCOMP' and v.tiporec not in ('FCV','DCO') " +
				"and v.mpago  = '"+MetodosPagoCtrl.TARJETA+"' and v.moneda = '@MONEDA' and v.numrec in @NUMEROS_RECIBO " +
				"and v.liquidarpormarca = 0 and v.fecha = '@FECHAARQUEO'"  +
				"group by v.refer1, v.afcomision, v.afnombre, v.codunineg, v.caidpos" ;
			
			
			String strQueryExecute = strSql.replace("@BDESQUEMA", PropertiesSystem.ESQUEMA  )
					.replace("@CAID", Integer.toString( iCaid ) )
					.replace("@CODCOMP", sCodcomp  )
					.replace("@MONEDA", sMoneda )
					.replace("@FECHAARQUEO", FechasUtil.formatDatetoString(dtFecha, "yyyy-MM-dd") )
					.replace("@NUMEROS_RECIBO",  sListaRec) ;
			
			
			lstRecibos = (ArrayList<Object[]>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(strQueryExecute, true, null ) ;
			
			
 

		} catch (Exception error) {
			lstRecibos = new ArrayList<Object[]>();
			LogCajaService.CreateLog("obtenerMontoPOSxUN", "ERR", error.getMessage());
		}
		return lstRecibos;
	}
	@SuppressWarnings("unchecked")
	public List<Object[]> obtenerMontoPOSxUN(int iCaid,String sCodcomp,String sCodsuc,String sMoneda,
			String sListaRec,Date dtFecha, List<DncCierreDonacion> cierresDnc, Session session){
		
		List<Object[]>lstRecibos = null;
		
		try {
 
			
			String strSql = 
				" select sum(v.monto - v.totaldev), v.afcomision, v.afnombre, v.refer1, trim(v.codunineg), v.caidpos, v.marcatarjeta " +
				"from  @BDESQUEMA.Vdetrecibodev v " +
				"where v.caid = @CAID and v.codcomp = '@CODCOMP' and v.tiporec not in ('FCV','DCO') " +
				"and v.mpago  = '"+MetodosPagoCtrl.TARJETA+"' and v.moneda = '@MONEDA' and v.numrec in @NUMEROS_RECIBO " +
				"and v.liquidarpormarca = 1 and v.fecha = '@FECHAARQUEO'"  +
				"group by v.refer1, v.afcomision, v.afnombre, v.codunineg, v.caidpos, v.marcatarjeta " +
				
				"union all " + 

				" select sum(v.monto - v.totaldev), v.afcomision, v.afnombre, v.refer1, trim(v.codunineg), v.caidpos, 'Genérico' " +
				"from  @BDESQUEMA.Vdetrecibodev v " +
				"where v.caid = @CAID and v.codcomp = '@CODCOMP' and v.tiporec not in ('FCV','DCO') " +
				"and v.mpago  = '"+MetodosPagoCtrl.TARJETA+"' and v.moneda = '@MONEDA' and v.numrec in @NUMEROS_RECIBO " +
				"and v.liquidarpormarca = 0 and v.fecha = '@FECHAARQUEO'"  +
				"group by v.refer1, v.afcomision, v.afnombre, v.codunineg, v.caidpos" ;
			
			
			String strQueryExecute = strSql.replace("@BDESQUEMA", PropertiesSystem.ESQUEMA  )
					.replace("@CAID", Integer.toString( iCaid ) )
					.replace("@CODCOMP", sCodcomp  )
					.replace("@MONEDA", sMoneda )
					.replace("@FECHAARQUEO", FechasUtil.formatDatetoString(dtFecha, "yyyy-MM-dd") )
					.replace("@NUMEROS_RECIBO",  sListaRec) ;
			
			lstRecibos = (ArrayList<Object[]>)session.createSQLQuery(strQueryExecute).list();
			

		} catch (Exception error) {
			lstRecibos = new ArrayList<Object[]>();
			LogCajaService.CreateLog("obtenerMontoPOSxUN", "ERR", error.getMessage());
		} 
		return lstRecibos;
	}
/*************************************************************************************************/
/** Obtener los recibos que se no se pagaron en efectivo.. sin devoluciones                      */	
	public List obtenerMontosxAfiliados(int iCaid,String sCodcomp,String sCodsuc,String sMoneda,String sListaRec,Date dtFecha){
		List lstRecibos = new ArrayList();
		Session sesion = HibernateUtilPruebaCn.currentSession();
		Transaction trans = null;
		String sConsulta = "",sFecha;		
		SimpleDateFormat format;
		
		try{
			format = new SimpleDateFormat("yyyy-MM-dd");
			sFecha = format.format(dtFecha);	
			
			/**
			 * 02/05/2010
			 * Agregado: Filtro por moneda del arqueo, solo leerá los pagos por método y moneda del arqueo.
			 */			
			sConsulta = "  select sum(v.id.montoneto),v.id.afcomision,v.id.afnombre,v.id.refer1,v.id.codunineg ";
			sConsulta += " from Vrecibodevrecibo as v where v.id.fecha = '"+sFecha+"' and v.id.caid = "+iCaid;
			sConsulta += " and v.id.codsuc = '"+sCodsuc+"' and v.id.codcomp = '"+sCodcomp+"' and tiporec not in ('FCV','DCO','DCR')";
			sConsulta += " and v.id.mpago  = '"+MetodosPagoCtrl.TARJETA+"' and (v.id.devolucion = 0 or v.id.montoneto <> v.id.monto)";
			sConsulta += " and v.id.moneda = '"+sMoneda+" 'and v.id.numrec in "+sListaRec; 
			sConsulta += " group by v.id.refer1,v.id.afcomision,v.id.afnombre, v.id.codunineg";
			
			trans = sesion.beginTransaction();
			lstRecibos = sesion.createQuery(sConsulta).list();
			trans.commit();
			
		} catch (Exception error) {
			LogCajaService.CreateLog("obtenerMontosxAfiliados", "ERR", error.getMessage());
		} finally {
			sesion.close();
		}
		return lstRecibos;		
	}
/**************************************************************************************************/
/** Guardar 1 registro en recibo JDE y Arqueorec cuando se haga un depósito 
 *********************************/
	public boolean guardarRegistroDeposito(int iCaid, String sCodsuc,String sCodcomp,int iNobatch,int iNodoco,int iNoarqueo,
			String sMoneda, BigDecimal bdMonto,Date dtFecha,Date dtHora, String sCoduser,
			String sRefer,String sTipodep,String sLogin, String sTipoPago, BigDecimal bdTasa,
			int iCodreg, int iCodigoBanco, Session sesion,  int iCodcajero,
			ArrayList<Ctaxdeposito>lstCtsxDeps, int referdep , int depctatran, String monedabase ){
		
		
		boolean bHecho = true;
		ReciboCtrl rcCtrl = new ReciboCtrl();
		Divisas dv = new Divisas();
		int iNodep=0;
		Numcaja nc = null;
		
		try {			
			
			if(monedabase == null )
				monedabase = "COR";
			
			nc = Divisas.obtenerNumeracionCaja("NDEPOSITO", iCaid, sCodcomp, sCodsuc, true, sLogin,sesion);
			
			if(nc == null){
				return bHecho = false;
			}
			
			iNodep = nc.getNosiguiente();
			
			//&& ===================== guardar el registro en la tabla de depósito.
			
			bHecho = dv.registrarDeposito(iNodep, iCaid, sCodsuc, sCodcomp,
					  dtFecha, dtHora, bdMonto, sMoneda,
					  String.valueOf(iNodoco), sCoduser, sTipodep, bdTasa,
					  iCodreg,sTipoPago,iCodigoBanco, sesion, 
					  iCodcajero, lstCtsxDeps, referdep , depctatran, monedabase);
			
			if(!bHecho){
 
				return bHecho = false;
			}
			
			//&& ===================== Guardar registro de ReciboJDE
			bHecho = rcCtrl.fillEnlaceMcajaJde(sesion, null, iNodep, sCodcomp, iNodoco,  iNobatch, iCaid, sCodsuc, "D", sTipoPago);
			
			if(!bHecho){
 
				return bHecho = false;
			}
			
			//&& ===================== guardar el registro en Arqueorec
			Arqueorec ar = new Arqueorec();
			ArqueorecId arid = new ArqueorecId();
			arid.setCaid(iCaid);
			arid.setCodcomp(sCodcomp);
			arid.setCodsuc(sCodsuc);
			arid.setNoarqueo(iNoarqueo);
			arid.setNumrec(iNodep);
			arid.setTipodoc("D");
			arid.setTiporec("D");
			ar.setId(arid);
			sesion.save(ar);
			
		} catch (Exception error) {
			LogCajaService.CreateLog("guardarRegistroDeposito", "ERR", error.getMessage());
			bHecho = false;
		} 
		return bHecho;
	}
	
	
/**************************************************************************************************/
/** Guardar el registro en recibo JDE y Arqueorec cuando se haga un depósito 
 *********************************/
	public boolean guardarListaDepositos1(List lstDepositos,int iCaid, String sCodcomp,String sCodsuc,int iCodreg, int iCodigoBanco){
		boolean bHecho = true;
		Session sesion = HibernateUtilPruebaCn.currentSession();
		Transaction trans = null;
		String sConsulta = "";
		ReciboCtrl rcCtrl = new ReciboCtrl();
		Divisas dv = new Divisas();
		int iNodep=0,iNobatch,iNodoco,iNoarqueo;
		String sMoneda,sCoduser,sRefer,sTipodep;
		BigDecimal bdMonto,bdTasa = new BigDecimal(0);
		Date dtFecha,dtHora;
		String sTipoPago = "";
		
		try {
			sConsulta  = " from Numcaja as n where n.id.codnumeracion = 'NDEPOSITO'"; 
			sConsulta += " and n.id.caid ='"+iCaid+"' and n.id.codcomp = '"+sCodcomp+"' and n.id.codsuc = '"+sCodsuc+"'";
			trans = sesion.beginTransaction();
			Object ob = sesion.createQuery(sConsulta).uniqueResult();
			
			if(ob!=null){
				Numcaja nc = (Numcaja)ob;
				iNodep = nc.getNosiguiente();
					
			for(int i=0; i<lstDepositos.size();i++){
				Object oDep[] = (Object[])lstDepositos.get(i);						
				iCaid 		= Integer.parseInt(oDep[0].toString());
				sCodsuc		= oDep[1].toString();
				sCodcomp	= oDep[2].toString();
				iNobatch 	= Integer.parseInt(oDep[3].toString());
				iNodoco		= Integer.parseInt(oDep[4].toString());
				iNoarqueo	= Integer.parseInt(oDep[5].toString());
				sMoneda		= oDep[6].toString();
				bdMonto		= (BigDecimal)oDep[7];
				dtFecha		= (Date)oDep[8];
				dtHora		= (Date)oDep[9];
				sCoduser	= oDep[10].toString();
				sRefer		= oDep[11].toString();
				sTipodep	= oDep[12].toString();
				sTipoPago	= oDep[14].toString();
				bdTasa      = new BigDecimal(oDep[15].toString());
				
				if(bHecho){
					//guardar registro de ReciboJDE
					bHecho = rcCtrl.fillEnlaceMcajaJde(sesion, trans,iNodep, sCodcomp, iNodoco, iNobatch, iCaid, sCodsuc, "D","D");
					
					if(bHecho){
						//guardar el registro en Arqueorec
						Arqueorec ar = new Arqueorec();
						ArqueorecId arid = new ArqueorecId();
						arid.setCaid(iCaid);
						arid.setCodcomp(sCodcomp);
						arid.setCodsuc(sCodsuc);
						arid.setNoarqueo(iNoarqueo);
						arid.setNumrec(iNodep);
						arid.setTipodoc("D");
						ar.setId(arid);
						sesion.save(ar);
						
						//actualizar el número de depósito en Numcaja.
						nc.setNosiguiente(nc.getNosiguiente() + 1);
						nc.setUsuariomodificacion(sCoduser);
						nc.setFechamodificacion(new Date());				
						sesion.update(nc);
						
					}else{
						
						LogCajaService.CreateLog("guardarListaDepositos1", "ERR", "Error: No se puede registrar el deposito "+iNodep+" en ReciboJDE ");
						break;
					}
				}else
				{
					
					LogCajaService.CreateLog("guardarListaDepositos1", "ERR", "Error: No se puede registrar el deposito "+iNodep+" en Depósito");
					
					break;
				}
			}
			}else{
					
					LogCajaService.CreateLog("guardarListaDepositos1", "ERR", "Error: No se puede obtener el número siguiente de depósitos en Divisas.guardarRegistroDeposito");
					
					bHecho = false;
				}			
			
			//confirmar los registros.
			if(bHecho)
				trans.commit();
			else
				trans.rollback();
			
		} catch (Exception error) {
			
			LogCajaService.CreateLog("guardarListaDepositos1", "ERR", error.getMessage());
			
			bHecho = false;
			trans.rollback();
		} finally {
			sesion.close();
		}
		return bHecho;
	}
}

package com.casapellas.controles;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.faces.model.SelectItem;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.casapellas.donacion.entidades.DncBeneficiario;
import com.casapellas.donacion.entidades.DncBeneficiarioCuentas;
import com.casapellas.donacion.entidades.DncCierreDonacion;
import com.casapellas.donacion.entidades.DncDonacion;
import com.casapellas.donacion.entidades.DncDonacionJde;
import com.casapellas.donacion.entidades.DncIngresoDonacion;
import com.casapellas.entidades.MetodosPago;
import com.casapellas.entidades.Tcambio;
import com.casapellas.entidades.Vf0901;
import com.casapellas.entidades.Vf55ca012;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.jde.creditos.CodigosJDE1;
import com.casapellas.jde.creditos.DatosComprobanteF0911;
import com.casapellas.jde.creditos.ProcesarEntradaDeDiario;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.Divisas;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.PropertiesSystem;
import com.casapellas.entidades.ens.Vautoriz;
import com.ibm.icu.util.Calendar;
 

public class DonacionesCtrl {

	private static int nobatch;
	private static int nodocjde;
	public static String msgProceso;
	//public static Connection cn; 
	private static List<int[]>noBatchsAndDocs;
	public static int caid;
	public static int numrec;
	public static String codcomp;
	public static String tiporecibo ;
	public static int iddncrsm = 0 ;
	
	
	public static String totalDonacionxMoneda( List<MetodosPago>selectedMet ){
		String totalesxmoneda = "" ;
		
		try {
			
			List<DncIngresoDonacion> dncEnRecibo = new ArrayList<DncIngresoDonacion>();
			for (MetodosPago pagosEnRecibo : selectedMet) {
				if(pagosEnRecibo.getDonaciones() != null && !pagosEnRecibo.getDonaciones().isEmpty() )
					dncEnRecibo.addAll(pagosEnRecibo.getDonaciones() );
			}
			
			@SuppressWarnings("unchecked")
			List<String>monedasdnc = (ArrayList<String>)CodeUtil.selectPropertyListFromEntity(dncEnRecibo, "moneda", true);
			for (final String monedaDnc : monedasdnc) {
				
				@SuppressWarnings("unchecked")
				List<DncIngresoDonacion> dncsxmoneda = (ArrayList<DncIngresoDonacion>)
				CollectionUtils.select(dncEnRecibo, new Predicate(){
					public boolean evaluate(Object o) {
						return ((DncDonacion)o).getMoneda().compareTo(monedaDnc) == 0 ;
					}
				});
				
				BigDecimal bdMontoxMoneda = CodeUtil.sumPropertyValueFromEntityList(dncsxmoneda, "montorecibido", false) ;
				totalesxmoneda += monedaDnc +":  " + String.format("%1$,.2f", bdMontoxMoneda) + "<BR>"; 
			}
			
			totalesxmoneda = totalesxmoneda.substring(0, totalesxmoneda.length() - 4);
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return totalesxmoneda;
	}
	
	
	public static boolean comprobantesCierreDonaciones(
			List<DncCierreDonacion>cierreDonaciones, String moneda,
			int noarqueo, Date fecha, String monedabase, 
			Vautoriz vaut, Session session) {
		boolean hecho = true;
		
		List<String> sqlsUpdateCiere = new ArrayList<String>(); 

		try {
			
			String strSqlUpdateCierre = "update " + PropertiesSystem.ESQUEMA
				+".dnc_cierre_donacion set usrmod = @USRMOD, fechamodificacion = current_timestamp, " +
				"batch_Aprobacion = @NOBATCH, documento_Aprobacion = @NODOCJDE," +
				" tipo_Documento = '@TIPODOCJDE', estado = 2 " +
				" where idcierredonacion = @IDCIERREDNC and noarqueo = @NOARQUEO  "; 

			for (DncCierreDonacion dncCierre : cierreDonaciones) {

//				nobatch = Divisas.numeroSiguienteJdeE1(CodigosJDE1.NUMEROBATCH );
				nobatch = Divisas.numeroSiguienteJdeE1(  );
				nodocjde = Divisas.numeroSiguienteJdeE1( CodigosJDE1.NUMEROPAGOVOUCHER );

				hecho = generarAsientosContablesCierreDonacion(dncCierre, monedabase, vaut, session);

				if (!hecho) {
					break;
				}
				
				sqlsUpdateCiere.add(
					strSqlUpdateCierre.replace("@USRMOD", Integer.toString( vaut.getId().getCodreg() ) )
						.replace("@NOBATCH",  Integer.toString( nobatch ) )
						.replace("@NODOCJDE", Integer.toString( nodocjde ) )
						.replace("@TIPODOCJDE", CodigosJDE1.BATCH_ANTICIPO_PMT.codigo())
						.replace("@NOARQUEO ",Integer.toString( noarqueo ) ) 
						.replace("@IDCIERREDNC", Integer.toString( dncCierre.getIdcierredonacion() ) ) 
				);
			}
			
			if(!hecho)
				return false;

			//hecho = ConsolidadoDepositosBcoCtrl.executeSqlQueries(sqlsUpdateCiere );
			int rows = session.createSQLQuery(sqlsUpdateCiere.get(0)).executeUpdate();
			hecho = rows == 1 ;

			if(!hecho){
				msgProceso = "Actualizacion de datos de donacion no procesada " ;
			}

		} catch (Exception e) {
			msgProceso = "Error en proceso de comprobantes por donaciones " ;
			hecho = false;
			e.printStackTrace(); 
		} finally {

		}
		return hecho;
	}
	
	@SuppressWarnings("unchecked")
	public static boolean generarAsientosContablesCierreDonacion(
			DncCierreDonacion dnc, String monedabase, Vautoriz vaut, Session session ){
		
		boolean hecho = true;
		
		String tipodocjde = "PV";
		String tipocliente = "";
		String codbnf_aux = ""; 
		
		Vf0901 vfBnfCuenta = null;
		DncBeneficiarioCuentas dncBnfCtaCfg ;
		
		int montoentero = 0;
		
		ReciboCtrl rcCtrl = new ReciboCtrl();
		Date fechadoc = dnc.getFechacierre();
		BigDecimal tasaoficial = BigDecimal.ONE;
		
		try {
			
			msgProceso =  "" ;
			
			String strSqlTcambio = "select * from "+PropertiesSystem.ESQUEMA
					+".tcambio where cxeft = '"+new SimpleDateFormat("yyyy-MM-dd").format(fechadoc)
					+"' and cxcrcd = 'COR'" ;
			
			List<Tcambio>tasas = (ArrayList<Tcambio>) session.createSQLQuery(strSqlTcambio).addEntity(Tcambio.class).list();
			//List<Tcambio>tasas = (ArrayList<Tcambio>)ConsolidadoDepositosBcoCtrl.executeSqlQuery( strSqlTcambio, true, Tcambio.class );
			if(tasas == null || tasas.isEmpty() ){
				msgProceso = "tasa de cambio oficial no configurada ";
				return false;
			}
			tasaoficial = tasas.get(0).getId().getCxcrrd();
			
			String strQueryExecute;
			
			//&& ============== Datos del beneficiario.
			strQueryExecute = " select * from " + PropertiesSystem.ESQUEMA 
					+".dnc_beneficiario where  estado = 1 and codigo = " 
					+ dnc.getCodigobeneficiario() +" and idbeneficiario = " 
					+ dnc.getIdbeneficiario()  ;
			
			List<DncBeneficiario> lstbeneficiarios = ( ArrayList<DncBeneficiario>) session.createSQLQuery(strQueryExecute ).addEntity( DncBeneficiario.class ).list();
			//List<DncBeneficiario> lstbeneficiarios = ( ArrayList<DncBeneficiario>) ConsolidadoDepositosBcoCtrl.executeSqlQuery (strQueryExecute, true, DncBeneficiario.class );
			
			DncBeneficiario bnf = lstbeneficiarios.get(0);
			
			String strSqlCtaBnf = "select * from " + PropertiesSystem.ESQUEMA +
					".dnc_beneficiario_cuentas  where idbeneficiario = @BNF_ID " +
					"and codigobnf = @BNF_CODIGO and moneda = '@MONEDA' " +
					"and trim(codcomp) = '@CODCOMP' fetch first rows only ";
			
			String strSqlF0901 = " select * from "+ PropertiesSystem.ESQUEMA +
					".Vf0901 where trim(gmmcu) = '@GMMCU' and trim( gmobj ) = '@GMOBJ' " +
					"and trim(gmsub) = '@GMSUB' and trim(gmpec) <> 'N' fetch first rows only ";
			
			strQueryExecute = 
					strSqlCtaBnf.replace("@BNF_ID", String.valueOf( dnc.getIdbeneficiario() ) )
					.replace("@BNF_CODIGO", String.valueOf( dnc.getCodigobeneficiario() ) )
					.replace("@MONEDA", dnc.getMoneda() )
					.replace("@CODCOMP", codcomp);
				
			List<DncBeneficiarioCuentas> bnf_cta = (ArrayList<DncBeneficiarioCuentas>)session.createSQLQuery(strQueryExecute).addEntity(DncBeneficiarioCuentas.class ).list();
			//List<DncBeneficiarioCuentas> bnf_cta = (ArrayList<DncBeneficiarioCuentas>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(strQueryExecute, true, DncBeneficiarioCuentas.class );
				
			if(bnf_cta == null ||  bnf_cta.isEmpty() ){
				msgProceso = "No hay cuenta contable configurada para el beneficiario  " + bnf.getNombrecorto() ;
				return false;
			}
			
			dncBnfCtaCfg = bnf_cta.get(0);
			tipodocjde = dncBnfCtaCfg.getTipodocumento();
			
			strQueryExecute = 
				strSqlF0901.replace("@GMMCU", dncBnfCtaCfg.getUnegocio().trim() )
					.replace("@GMOBJ",dncBnfCtaCfg.getObjeto().trim() )
					.replace("@GMSUB",dncBnfCtaCfg.getSubsidiaria().trim() ) ;
			
			List<Vf0901>bnf_CuentasF0901 = ( ArrayList<Vf0901>)session.createSQLQuery(strQueryExecute).addEntity( Vf0901.class ).list();
			//List<Vf0901>bnf_CuentasF0901 = ( ArrayList<Vf0901>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(strQueryExecute, true, Vf0901.class );
			
			if(bnf_CuentasF0901 == null || bnf_CuentasF0901.isEmpty()){
				msgProceso = "No se encontro en JDE cuenta contable para la confirucion de cuenta para beneficiario " + bnf.getNombrecorto() ;
				return false;
			}
			
			codbnf_aux = String.format("%08d", dnc.getCodigobeneficiario() ) ;
			
			vfBnfCuenta = bnf_CuentasF0901.get(0);
			int lineadoc = 0;
			
			String ctaBnfMcu = vfBnfCuenta.getId().getGmmcu().trim();
			String ctaBnfObj = vfBnfCuenta.getId().getGmobj().trim();
			String ctaBnfSub = vfBnfCuenta.getId().getGmsub().trim();
			String ctaBnfGmaid  = vfBnfCuenta.getId().getGmaid() ;
			
			ctaBnfMcu = CodeUtil.pad( ctaBnfMcu.trim(), 12, " ");
			
			String ctaBnfcuenta = ctaBnfMcu+"."+ctaBnfObj +  ( (ctaBnfSub == null || ctaBnfSub.trim().isEmpty())? "" : "." + ctaBnfSub.trim() );
			
			String obsDnc = "Caja:"+ caid + " Arqueo # " + dnc.getNoarqueo() ;		
			String concepto = "Consolidado Donacion: " +  bnf.getNombrecorto()  ;
			String observpago = "Donaciones Caja:" + caid  +" Arq:" +  dnc.getNoarqueo();
			String numfactura = "Dnc Caja:" + caid  +" Arq:" +  dnc.getNoarqueo();
			
			montoentero = Integer.parseInt( String.format("%1$.2f", dnc.getMontoneto() ).replace(".", "")  ); 
			
			int montoAA = montoentero;
			int montoCA = montoentero;
			String monedaAA = dnc.getMoneda();
			String monedaCA = dnc.getMoneda();
			String tipoDocxMon = "D" ;
			
			BigDecimal tasacambio = BigDecimal.ONE;
			
			if(dnc.getMoneda().compareTo(monedabase) != 0 ){
				tasacambio = tasaoficial;
				tipoDocxMon= "F";
			}
			
			montoAA = Integer.parseInt( String.format("%1$.2f",  dnc.getMontoneto().multiply(tasacambio) ).replace(".", "")  ); 
			
			if(dnc.getMoneda().compareTo(monedabase) == 0 ){
				tasacambio =  BigDecimal.ZERO;
			}
			
			//&& =========================== Grabar el encabezado del batch

			hecho = rcCtrl.registrarBatchA92Session(session, fechadoc, CodigosJDE1.BATCH_ANTICIPO_PMT, nobatch, montoCA, vaut.getId().getLogin(), 1, "Donacion", CodigosJDE1.BATCH_ESTADO_PENDIENTE);
			
			if(!hecho) {
				msgProceso =" no se pudo grabar cargo a cuenta de beneficiario en jde (encabezado de batch) " ;
				return false;
			}
			
			
			strQueryExecute = " select IFNULL( (cast( c4cjmcu  as varchar(12) ccsid 37 )),'2499')  from "
					+PropertiesSystem.ESQUEMA+".f55ca014 where c4rp01 = '"+dnc.getCodcomp()+"'  and c4id = " + dnc.getCaid() ; 
			
			//String unidadNegCtaLiquida = ( ( ArrayList<String>) ConsolidadoDepositosBcoCtrl.executeSqlQuery( strQueryExecute, true, null ) ).get(0) ;
			String unidadNegCtaLiquida = ( ( ArrayList<String>) session.createSQLQuery( strQueryExecute ).list() ).get(0) ;
			unidadNegCtaLiquida = unidadNegCtaLiquida.trim();

			String companiaRpkco = CodeUtil.pad( vfBnfCuenta.getId().getGmco().trim(), 5 , "0"); 
			unidadNegCtaLiquida  = CodeUtil.pad( unidadNegCtaLiquida, 12, " ");  
			
			String tipoDocumento =  CodigosJDE1.BATCH_ANTICIPO_PMT.codigo();
			
			//&& =========================== Grabar linea para el debito al beneficiario
 
			hecho = PlanMantenimientoTotalCtrl.registrarF0911PMT(fechadoc, companiaRpkco, tipodocjde, nodocjde, 
					(++lineadoc), nobatch, ctaBnfcuenta, ctaBnfGmaid, ctaBnfMcu, ctaBnfObj, ctaBnfSub, 
					"AA", monedaAA, ( montoAA  ) , concepto, vaut.getId().getLogin(),
					vaut.getId().getCodapp(), tasacambio, tipocliente, obsDnc, 
					ctaBnfMcu, codbnf_aux, "A", monedabase, companiaRpkco, tipoDocxMon, 
					tipoDocumento, dnc.getCodigobeneficiario(), numfactura, session );
			
			
			if(!hecho) {
				msgProceso =" no se pudo grabar cargo a cuenta de beneficiario en jde " ;
				return false;
			}
			
			if(dnc.getMoneda().compareTo(monedabase) != 0 ){
				
				hecho = PlanMantenimientoTotalCtrl.registrarF0911PMT(fechadoc, companiaRpkco, tipodocjde, nodocjde, 
						(lineadoc), nobatch, ctaBnfcuenta, ctaBnfGmaid, ctaBnfMcu, ctaBnfObj, ctaBnfSub, 
						"CA", monedaCA, ( montoCA  ), concepto, vaut.getId().getLogin(),
						vaut.getId().getCodapp(), tasacambio, tipocliente, obsDnc,
						ctaBnfMcu, codbnf_aux, "A", monedabase, companiaRpkco, tipoDocxMon,
						tipoDocumento, dnc.getCodigobeneficiario(), numfactura, session );
				
			}
			
			if(!hecho) {
				msgProceso =" no se pudo grabar cargo a cuenta de beneficiario en jde " ;
				return false;
			}
			
			String companiaRpoc  =   companiaRpkco  ;
			String cdtaObjCtaLiquida = "";
			String cdtaSubCtaLiquida = "";
			
			int idcuenta = dncBnfCtaCfg.getCuentabancodnc();
			
			hecho = PlanMantenimientoTotalCtrl.registrarF0411PorPMT(companiaRpkco, companiaRpoc, nodocjde, tipodocjde, dnc.getCodigobeneficiario(),
					fechadoc, nobatch, dnc.getMoneda(), monedabase, dnc.getMontoneto(),
					tasacambio, idcuenta, unidadNegCtaLiquida, cdtaObjCtaLiquida,
					cdtaSubCtaLiquida, numfactura, vaut.getId().getLogin(), observpago, "0", "",  "A", "VAR", session ) ;
			
			if(!hecho) {
				msgProceso =" no se pudo grabar cargo a cuenta de beneficiario en jde " ;
				return false;
			}
			
			
		} catch (Exception e) {
			hecho = false;
			e.printStackTrace(); 
		}
		return hecho;
	}
	
	@SuppressWarnings("unchecked")
	public static boolean registrarDonacionF0411(String companiaRpkco, String companiaRpco,
			int nodocumento, String tipodocumento, int codigobeneficiario,
			Date fechaArqueo, int nobatch, String moneda, String monedaBase,
			BigDecimal monto, BigDecimal tasaCambio, int idcuenta,
			String unidadNegocio, String ctaobj, String ctaSub, 
			String numfactura_descrip, String usrA400, String observacionpago,Session s ) {
		
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
					"IFNULL( (cast( a6apc as varchar(4) ccsid 37 )), 'VAR' ) a6apc, " +
					"IFNULL( (cast( a6pyin as varchar(1) ccsid 37 )), '"+MetodosPagoCtrl.TRANSFERENCIA+"' )  a6pyin from " +
					PropertiesSystem.JDEDTA+".f0401 where a6an8 = " + codigobeneficiario ;
			
			List<Object[]> dtaBnfF0401 = ( ArrayList<Object[]> )ConsolidadoDepositosBcoCtrl.executeSqlQuery( strSqlPayInstruent, true,null ) ;
			glapclass = String.valueOf( dtaBnfF0401.get(0)[0] ) ;
			paymentInstrument = String.valueOf( dtaBnfF0401.get(0)[1] ) ;
			
			String strSqlInsertF0411 =
				"insert into "+PropertiesSystem.JDEDTA + ".F0411 (" +
				"RPKCO, RPDOC, RPDCT, RPSFX, RPAN8, RPPYE, RPDIVJ, RPDSVJ, "+
			    "RPDDJ, RPDDNJ, RPDGJ, RPFY, RPCTRY, RPPN, RPCO, RPICU, "+
			    "RPICUT, RPDICJ, RPBALJ, RPPST, RPAG, RPAAP, RPATXN, "+
			    "RPTXA1, RPEXR1, RPCRRM, RPCRCD, RPCRR, RPACR, RPFAP, "+
			    "RPCTXN, RPGLC, RPGLBA, RPAM, RPMCU, RPOBJ, RPSUB, RPVINV, RPPYIN, "+
			    "RPAC07, RPBCRC, RPYC01, RPYC02, RPYC03, RPYC04, RPYC05, RPYC06, RPYC07, RPYC08, RPYC09, RPYC10, " +
			    "RPTORG, RPUSER, RPPID, RPUPMJ, RPUPMT, RPJOBN, RPPA8, RPRMK ) "+
			    " values (" +
			    "'@RPKCO', @RPDOC, '@RPDCT', '@RPSFX', @RPAN8, @RPPYE, @RPDIVJ, @RPDSVJ, "+
			    "@RPDDJ, @RPDDNJ, @RPDGJ, @RPFY, @RPCTRY, @RPPN, '@RPCO', @RPICU, "+
			    "'@TIPOBATCH', @RPDICJ, '@RPBALJ', '@RPPST', @RPAG, @RPAAP, @RPATXN, "+
			    "'@RPTXA1', '@RPEXR1', '@RPCRRM', '@RPCRCD', @RPCRR, '@RPACR', @RPFAP, "+
			    "@RPCTXN, '@RPGLC', '@RPGLBA', '@RPAM', '@RPMCU', '@RPOBJ','@RPSUB', '@RPVINV', '@RPPYIN', "+
			    "'@RPAC07', '@RPBCRC', " +
			    "'@RPYC01', '@RPYC02', '@RPYC03', '@RPYC04', '@RPYC05', '@RPYC06', '@RPYC07', '@RPYC08', '@RPYC09', '@RPYC10', " +
			    "'@RPTORG', '@RPUSER', '@RPPID', @RPUPMJ, @RPUPMT, '@RPJOBN', @RPPA8, '@RPRMK' ) ";
						
				String sqlExecute = 
					strSqlInsertF0411 
					 .replace("@RPKCO",  companiaRpkco) 
					 .replace("@RPDOC",  Integer.toString(nodocumento)) 
					 .replace("@RPDCT",  tipodocumento)  
					 .replace("@RPSFX",  numerocuota)  
					 .replace("@RPAN8",  Integer.toString(codigobeneficiario))
					 .replace("@RPPYE",  Integer.toString(codigobeneficiario))
					 .replace("@RPDIVJ", strFechaActualJuliana)
					 .replace("@RPDSVJ", strFechaActualJuliana)
					 .replace("@RPDDJ",  strFechaActualJuliana)
					 .replace("@RPDDNJ", strFechaActualJuliana)
					 .replace("@RPDGJ",  strFechaActualJuliana)
					 .replace("@RPFY",   Integer.toString(calendar.get(Calendar.YEAR) % 100))
					 .replace("@RPCTRY", Integer.toString(calendar.get(Calendar.YEAR) / 100))
					 .replace("@RPPN" ,  Integer.toString( calendar.get(Calendar.MONTH) + 1 ))
					 .replace("@RPCO",   companiaRpco)  
					 .replace("@RPICU",  Integer.toString(nobatch))
					 .replace("@TIPOBATCH","V")
					 .replace("@RPDICJ", strFechaActualJuliana)  
					 .replace("@RPBALJ", "Y")  
					 .replace("@RPPST",  "A")  
					 .replace("@RPAG",   Integer.toString(montoDomestico))
					 .replace("@RPAAP",  Integer.toString(montoDomestico))
					 .replace("@RPATXN", Integer.toString(montoDomestico))
					 .replace("@RPTXA1", "EXE")  
					 .replace("@RPEXR1", "E")  
					 .replace("@RPCRRM", currencyMode)  
					 .replace("@RPCRCD", moneda)  
					 .replace("@RPCRR",  tasaCambio.toString() )  
					 .replace("@RPACR",  Integer.toString(montoForaneo))
					 .replace("@RPFAP",  Integer.toString(montoForaneo))
					 .replace("@RPCTXN", Integer.toString(montoForaneo))
					 .replace("@RPGLC",  glapclass)  
					 .replace("@RPGLBA", Integer.toString(idcuenta))
					 .replace("@RPAM",   "2")
					 .replace("@RPMCU",  unidadNegocio)
					 .replace("@RPOBJ",  ctaobj)
					 .replace("@RPSUB",  ctaSub)
					 .replace("@RPVINV", numfactura_descrip )  
					 .replace("@RPPYIN", paymentInstrument )  
					 .replace("@RPAC07", rpyc07)  
					 .replace("@RPBCRC", monedaBase)  
					 
					 .replace("@RPYC01", rpyc01)
					 .replace("@RPYC02", rpyc02)
					 .replace("@RPYC03", rpac03)
					 .replace("@RPYC04", rpyc04)
					 .replace("@RPYC05", rpyc05)
					 .replace("@RPYC06", rpac06)
					 .replace("@RPYC07", rpyc07)
					 .replace("@RPYC08", rpyc08)
					 .replace("@RPYC09", rpac09)
					 .replace("@RPYC10", rpac10)
 
					 .replace("@RPTORG", usrA400)  
					 .replace("@RPUSER", usrA400)  
					 .replace("@RPPID",  PropertiesSystem.ESQUEMA )  
					 .replace("@RPUPMJ", strFechaActualJuliana)  
					 .replace("@RPUPMT", hora)  
					 .replace("@RPJOBN", PropertiesSystem.ESQUEMA)  
					 .replace("@RPPA8",  Integer.toString(codigobeneficiario))
					 .replace("@RPRMK",  observacionpago);
				
				
				try {

					ConsolidadoDepositosBcoCtrl.executeSqlQueryTx(s,sqlExecute);
					aplicado = true;

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
	
	public static boolean registrarDonacionF0911(Date dtFechaAsiento, Connection cn,
			String companiadoc, String sTipodoc, int iNoDocumento,
			double dLineaJDE, int iNoBatch, String sCuenta, String sIdCuenta,
			String sCodUnineg, String sCuentaObj, String sCuentaSub,
			String sTipoAsiento, String sMoneda, int iMonto, String sConcepto,
			String sUsuario, String sCodApp, BigDecimal dTasa,
			String sTipoCliente, String sObservacion, String sCodSucCuenta,
			String sGlsbl, String sGlsblt, String sGlbcrc, String sGlhco, String sGlcrrm,  
			String codigotipobatch, int codigobeneficiario, String descrip_factura_arqueo) {
		
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
			
			
			String strInsert = 
			"INSERT INTO "+PropertiesSystem.JDEDTA+".F0911 " +
				"(GLKCO, GLDCT, GLDOC, GLDGJ, GLJELN, GLICU, GLICUT, GLDICJ, GLDSYJ," +
				" GLTICU, GLCO, GLANI, GLAM, GLAID, GLMCU, GLOBJ, GLSUB, GLSBL,GLSBLT, GLLT," +
				" GLPN, GLCTRY, GLFY, GLCRCD, GLAA, GLEXA, GLDKJ, GLDSVJ, GLTORG, GLUSER, " +
				" GLPID, GLJOBN, GLUPMJ, GLUPMT, GLCRR, GLGLC, GLEXR, GLBCRC, GLHCO, GLCRRM, " +
				" GLAN8, GLVINV, GLIVD, GLPKCO )" +
			" values " +
				"('@GLKCO', '@GLDCT', @GLDOC, @GLDGJ, @GLJELN, @GLICU, '@TIPOBATCH', @GLDICJ,@GLDSYJ," +
				" @GLTICU, '@GLCO', '@GLANI', '@GLAM', '@GLAID', '@GLMCU', '@GLOBJ', '@GLSUB', '@GLSBL', '@TIPOSUBLIBRO', '@GLLT'," +
				" @GLPN,  @GLCTRY, @GLFY, '@GLCRCD', @GLAA, '@GLEXA', @GLDKJ, @GLDSVJ, '@GLTORG', '@GLUSER', " +
				" '@GLPID', '@GLJOBN', @GLUPMJ, @GLUPMT, @GLCRR, '@GLGLC', '@GLEXR', '@GLBCRC', '@GLHCO', '@CURRENCYMODE', " +
				" @GLAN8, '@GLVINV', @GLIVD, '@GLPKCO' )" ;
			
			String strExecute = 
				strInsert
					.replace("@GLKCO", companiadoc )
					.replace("@GLDCT", sTipodoc.trim() )
					.replace("@GLDOC", Integer.toString(iNoDocumento) )
					.replace("@GLDGJ", fechajulian)
					.replace("@GLJELN", Double.toString( dLineaJDE ) )
					.replace("@GLICU",  Integer.toString( iNoBatch ) )
					.replace("@TIPOBATCH", codigotipobatch)
					.replace("@GLDICJ", fechajulian )
					.replace("@GLDSYJ", fechajulian )
					.replace("@GLTICU", hora )
					.replace("@GLCO", companiacuenta)
					.replace("@GLANI", sCuenta )
					.replace("@GLAM", "2" )
					.replace("@GLAID", sIdCuenta )
					.replace("@GLMCU", sCodUnineg )
					.replace("@GLOBJ", sCuentaObj )
					.replace("@GLSUB", sCuentaSub)
					.replace("@GLSBL", sGlsbl )
					.replace("@TIPOSUBLIBRO", sGlsblt )
					.replace("@GLLT", sTipoAsiento )
					.replace("@GLPN", Integer.toString( calendar.get(Calendar.MONTH) + 1 ))
					.replace("@GLCTRY", Integer.toString(calendar.get(Calendar.YEAR) / 100))
					.replace("@GLFY", Integer.toString(calendar.get(Calendar.YEAR) % 100))
					.replace("@GLCRCD", sMoneda )
					.replace("@GLAA", Integer.toString(iMonto) )
					.replace("@GLEXA",  sConcepto )
					.replace("@GLDKJ", fechajulian)
					.replace("@GLDSVJ", fechajulian )
					.replace("@GLTORG", sUsuario )
					.replace("@GLUSER", sUsuario )
					.replace("@GLPID", "GCPMCAJA" )
					.replace("@GLJOBN", "GCPMCAJA" )
					.replace("@GLUPMJ", fechajulian )
					.replace("@GLUPMT", hora )
					.replace("@GLCRR", dTasa.toString())
					.replace("@GLGLC", sTipoCliente)
					.replace("@GLEXR", sObservacion)
					
					.replace("@GLBCRC", sGlbcrc)
					.replace("@GLHCO", sGlhco)
					.replace("@CURRENCYMODE", sGlcrrm)
					
					.replace("@GLAN8", Integer.toString(codigobeneficiario) )
					.replace("@GLVINV", descrip_factura_arqueo )
					.replace("@GLIVD", fechajulian )
					.replace("@GLPKCO", "00000"); 	
			
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
			
		} catch (Exception ex) {
			aplicado = false;
			ex.printStackTrace(); 
		}
		return aplicado;
	}
	
	@SuppressWarnings("unchecked")
	public static List<DncDonacion> obtenerDonaciones(int caid, String codcomp, String moneda, Date dtFechaArqueo, Date fechaini, Date fechafin){
		List<DncDonacion> donaciones = null ;
		
		try {
			
			
			String sql = " select *  from "+PropertiesSystem.ESQUEMA+".dnc_donacion "+
			"where caid = "+caid+" and codcomp = '"+codcomp+"' and moneda = '"+moneda+"'  " +
			"and date(fechacrea) = '" + new SimpleDateFormat("yyyy-MM-dd").format( dtFechaArqueo )+ "' and time(fechacrea) " +
			"between  '"+new SimpleDateFormat("HH.mm.ss").format(fechaini)  + 
			"' and '"+new SimpleDateFormat("HH.mm.ss").format(fechafin) +"' and estado = 1 ";
 
			donaciones = (List<DncDonacion>)  ConsolidadoDepositosBcoCtrl.executeSqlQuery( sql, true, DncDonacion.class );
			
			
		} catch (Exception e) {
			LogCajaService.CreateLog("obtenerDonaciones", "ERR", e.getMessage());
		}finally{
			
			if(donaciones == null)
				donaciones = new ArrayList<DncDonacion>();
			
		}
		return donaciones;
	}
	
	
	public static String revertirDonacion(List<DncDonacionJde>noBatchsDnc, Vautoriz vaut){
		String msg = "" ;
		ReciboCtrl rcCtrl = new ReciboCtrl();
		boolean done = true;
		
		try {
			
			Session session = HibernateUtilPruebaCn.currentSession();
			
			//&& =================== Grabar encabezado de batch; 
			String strSqlF0011 = 
				"SELECT ICAIPT  FROM "+PropertiesSystem.JDEDTA+".F0011 WHERE ICICU = @NOBATCH FETCH FIRST ROWS ONLY ";
			
			String strSqlF0911  = "SELECT "  
				+ " CAST(GLKCO AS VARCHAR (5) CCSID 37), "
				+ " CAST(GLDCT AS VARCHAR (2) CCSID 37),"
				+ " CAST(GLJELN AS  NUMERIC (7,2) ),"
				+ " CAST(GLCO AS VARCHAR (5) CCSID 37),"
				+ " CAST(GLANI AS VARCHAR (29) CCSID 37),"
				+ " CAST(GLAID AS VARCHAR (8) CCSID 37),"
				+ " CAST(GLMCU AS VARCHAR (12) CCSID 37),"
				+ " CAST(GLOBJ AS VARCHAR (6) CCSID 37),"
				+ " CAST(GLSUB AS VARCHAR (8) CCSID 37),"
				+ " CAST(GLLT AS VARCHAR (2) CCSID 37),"
				+ " CAST(GLCRCD AS VARCHAR (3) CCSID 37),"
				+ " CAST(GLCRR AS DECIMAL (15,2) ),"
				+ " CAST(GLAA AS NUMERIC (15) ),"
				+ " CAST(GLEXR AS VARCHAR (30) CCSID 37),"
				+ " CAST(GLCRRM AS VARCHAR (30) CCSID 37),"
				+ " CAST(  (IFNULL(GLSBL,'UNO'))  AS VARCHAR (8) CCSID 37 ) AS GLSBL, "
				+ " CAST(  (IFNULL(GlSBLT,'UNO')) AS VARCHAR (1) CCSID 37 ) AS GlSBLT,"
				+ " GLACR, "
				+ " GLBCRC, "
				+ " GLGLC "
				
				
				+ " FROM "+PropertiesSystem.JDEDTA+".F0911" 
				+ " WHERE GLICU =  @NOBATCH AND GLDOC = @NODOCJDE AND GLDCT <> 'AE'" ;
			
			
			List<String> queriesjde = new ArrayList<String>();
			String strQueryUpdDncJde = " update " + PropertiesSystem.ESQUEMA+".dnc_donacion_jde " +
					"set estado = 2, NOBATCHDEVOLUCION = @NOBATCHDEVOL, NODOCJDEDEVOLUCION = @NODOCJDEDEV, " +
					" NUMRECDEV = @NUMRECDEV, TIPORECDEV = '@TIPORECDEV'" +
					"  where id_donacion_jde = @IDDONACIONJDE ";
			
			int[] iNobatchNodoc ;
			String strSqlExecute ;
			
			for (DncDonacionJde dncjde : noBatchsDnc) {
				
				//&& =================== Grabar encabezado de batch; 
				strSqlExecute = strSqlF0011.replace("@NOBATCH", Integer.toString( dncjde.getNumerobatch() ) );
				
				@SuppressWarnings("unchecked")
				List<Integer>dtaF0011 = (List<Integer>)ConsolidadoDepositosBcoCtrl.executeSqlQuery( strSqlExecute, true, null );
				
				if(dtaF0011 == null || dtaF0011.isEmpty() ){
					return msg = "Error al leer datos de encabezado de batch #"+ dncjde.getNumerobatch()+" para reversion de donacion "  ;
				}
				
				//iNobatchNodoc = new Divisas().obtenerNobatchNodoco();
//				int iNoBatch 	 = Divisas.numeroSiguienteJdeE1(CodigosJDE1.NUMEROBATCH );
				int iNoBatch 	 = Divisas.numeroSiguienteJdeE1(  );
				int iNoDocumento = Divisas.numeroSiguienteJdeE1(CodigosJDE1.NUMERO_DOC_CONTAB_GENERAL );
				iNobatchNodoc = new int[]{iNoBatch, iNoDocumento };
				
				int montototal = Integer.parseInt(String.valueOf( dtaF0011.get(0) ) ) ;
				
				//done = rcCtrl.registrarBatchA92(new Date(), cn,"G", iNobatchNodoc[0], montototal, vaut.getId().getLogin(), 1, "N");
				done = rcCtrl.registrarBatchA92(session, new Date(), CodigosJDE1.RECIBOCONTADO, iNobatchNodoc[0], montototal, vaut.getId().getLogin(), 1 , "Donacion", CodigosJDE1.BATCH_ESTADO_PENDIENTE );
				
				
				if(!done){
					return msg = "Error al crear encabezado de reversion de batch " + dncjde.getNumerobatch() ;
				}
				
				//&& =================== Grabar detalle de batch; 
				strSqlExecute = 
						strSqlF0911.replace("@NOBATCH", Integer.toString( dncjde.getNumerobatch() ) )
						   .replace(" @NODOCJDE", Integer.toString( dncjde.getNumerodocjde()));
				
				@SuppressWarnings("unchecked")
				List<Object[]>dtaF0911 = (List<Object[]>)ConsolidadoDepositosBcoCtrl.executeSqlQuery( strSqlExecute, true, null );
				
				if(dtaF0911 == null || dtaF0911.isEmpty() ){
					return msg = "Error al leer detalle de batch #"+ dncjde.getNumerobatch() +" para reversion de donacion "  ;
				}
				
				Object[] dtaRow2 =  (Object[])
					CollectionUtils.find(dtaF0911, new Predicate(){
						public boolean evaluate(Object o) {
							Object[] obCampos = (Object[])o;
							return Double.parseDouble( String.valueOf(obCampos[2] ) ) == 1 ;
						}
					});
				
				String compBatch = String.valueOf(dtaRow2[3]).trim();
				compBatch =  compBatch.substring(compBatch.length()-2, compBatch.length());
				
				for (Object[] obCampos : dtaF0911) {
					
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
					String GLACR 	= String.valueOf(obCampos[17]).trim();
					String GLBCRC 	= String.valueOf(obCampos[18]).trim();
					String GLGLC	= String.valueOf(obCampos[19]).trim();
					
					double dLineaDocs = Double.valueOf(String.valueOf(obCampos[2]));
					BigDecimal bdTasa = new BigDecimal(String.valueOf(obCampos[11]));
					int iMonto		  = Integer.valueOf(String.valueOf(obCampos[12]));
					BigDecimal bdGlacr = new BigDecimal(GLACR);
					
					GLKCO = GLKCO.substring(GLKCO.length()-2,GLKCO.length());
					GLCO = GLCO.substring(GLCO.length()-2,GLCO.length());
					
					/*
					done = rcCtrl.registrarAsientoDiario(new Date(), cn, compBatch, GLDCT, iNobatchNodoc[1], 
								dLineaDocs, iNobatchNodoc[0], GLANI, GLAID, GLMCU,
								GLOBJ, GLSUB, GLLT, GLCRCD, iMonto*(-1), "Dev. Donacion Batch #"+ iNobatchNodoc[0],
								vaut.getId().getLogin(), "GCPMCAJA", bdTasa, "E", 
								GLEXR, GLCO, GLSBL, GlSBLT, GLCRCD, GLCO, GLCRRM);
					*/
					
					done = rcCtrl.registrarAsientoDiarioLogs(session," devolucion Donacion" , new Date(), compBatch, GLDCT, iNobatchNodoc[1], 
							dLineaDocs, iNobatchNodoc[0], GLANI, GLAID, GLMCU,
							GLOBJ, GLSUB, GLLT, GLCRCD, iMonto*(-1), "Dev. Donacion Batch #"+ iNobatchNodoc[0],
							vaut.getId().getLogin(), PropertiesSystem.ESQUEMA, bdTasa, GLGLC, 
							GLEXR, GLCO, GLSBL, GlSBLT, GLBCRC, GLCO, GLCRRM, bdGlacr.negate().intValue() );
					
					
					if(!done){
						return msg = "Error al crear detalle de reversion de batch " + dncjde.getNumerobatch() ;
					}
				}
				
				queriesjde.add( 
					strQueryUpdDncJde.replace("@IDDONACIONJDE", Integer.toString( dncjde.getIdDonacionJde()))
					.replace("@NOBATCHDEVOL", Integer.toString( iNobatchNodoc[0] ))
					.replace("@NODOCJDEDEV", Integer.toString( iNobatchNodoc[1] ))
					.replace("@NUMRECDEV", Integer.toString( numrec))
					.replace("@TIPORECDEV", tiporecibo  )
				);
				
			}
			
			//&& ==================== Marcar las donaciones como inactivas.
			List<String> queries = new ArrayList<String>();
			int iddncrsm = noBatchsDnc.get(0).getIddonacion() ;
			
			queries.add(
					" update " + PropertiesSystem.ESQUEMA+".dnc_donacion " +
					"set estado = 2, fechamod = current_timestamp, observacion = 'DEVOLUCION CONTADO'  " +
					"where iddncrsm = "+ iddncrsm 
				) ;
			
			queries.addAll(queriesjde);
			
			queries.add(" update " + PropertiesSystem.ESQUEMA+".dnc_donacion_rsm " +
				" set estado = 2, fechamod = current_timestamp, usrmod = "  + vaut.getId().getCodreg() +
				" where iddonacionrsm = "+ iddncrsm 
				);
				
			boolean executed = ConsolidadoDepositosBcoCtrl.executeSqlQueries( queries );
			if(!executed){
				return msg = "Error en actualizacion de donacion en gcpmcaja";
			}
			
				
		} catch (Exception e) {
			e.printStackTrace(); 
			msg = "Error al crear reversion de batch por donacion " ;
		}
		return msg;
	}
	

	public static String anularDonacion(int caid, String codcomp, String tiporec, int numrec, 
					String motivoanula, int coduser, boolean aplicadorecibo, int clientecodigo, 
					boolean devolucion, int iddnsrsm){
		
		String msg = "" ;
		
		try {
			
			String strDnc = "";
			String estadodncjde = "1"; 
			String batchfield = "numerobatch" ;
			String estadosigdncjde = "0" ;
			
			if(aplicadorecibo){
				strDnc = "select * from "+PropertiesSystem.ESQUEMA+".dnc_donacion_jde " +
						"where clientecodigo = "+clientecodigo+" and caid = "+ caid +" and trim(codcomp) = '"+codcomp.trim()+"' " +
						" and trim(tiporec) = '"+tiporec.trim()+"' and numrec = "+numrec+" and estado = @ESTADODNC " ;
			}else{
				strDnc = "select * from "+PropertiesSystem.ESQUEMA+".dnc_donacion_jde " +
						" where clientecodigo = "+clientecodigo+" and caid = "+ caid +" and trim(codcomp) = '"+codcomp.trim()+"' " +
						" and iddonacion = "+iddnsrsm+" and trim(tiporec) = '" +tiporec.trim() + "' and estado = @ESTADODNC" ;
			}
			
			if (devolucion) {
				estadodncjde = "2";
				batchfield = "nobatchdevolucion";
				estadosigdncjde = "1" ;
			}
						
			@SuppressWarnings("unchecked")
			List<DncDonacionJde> dncRsm = (ArrayList<DncDonacionJde>) ConsolidadoDepositosBcoCtrl 
					.executeSqlQuery(strDnc.replace("@ESTADODNC", estadodncjde) , true, DncDonacionJde.class );
			

			boolean aplicadonacion = dncRsm != null && !dncRsm.isEmpty() ; 
			
			if(!aplicadonacion){
				return msg = "Datos de Donación no encontrados para el proceso de anulación";
			}
			
			List<String> queries = new ArrayList<String>();
			List<String> queriesAnula = new ArrayList<String>();
			
			@SuppressWarnings("unchecked")
			List<Integer>batchs = (List<Integer>) CodeUtil.selectPropertyListFromEntity(dncRsm, batchfield, false);
			String nobatch = batchs.toString().replace("[", "(").replace("]", ")"); 
		 
			strDnc = " SELECT ICICU, CAST(ICIST AS VARCHAR(10) CCSID 37) ICIST FROM "
					+ PropertiesSystem.JDEDTA + ".F0011 WHERE ICICU IN "+nobatch+" and ICICUT = 'G'" ;
				
			@SuppressWarnings("unchecked")
			List<Object[]>estadobatch = (List<Object[]>) ConsolidadoDepositosBcoCtrl.executeSqlQuery( strDnc, true, null );
			
			for (Object[] oBatch : estadobatch) {
				
				if(oBatch[1] == null)
					continue;
				
				if(String.valueOf(oBatch[1]).compareTo("P") == 0 || String.valueOf(oBatch[1]).compareTo("D") == 0){
					msg = "No se puede anular, el batch # " + String.valueOf(oBatch[0]) +" por donación se encuentra contabilizado "; 
					return msg;
				}
			}
			
			String strQueryDelete = "delete from " + PropertiesSystem.JDEDTA+".F0011 where ICICU IN "+nobatch+" and ICICUT = 'G'";
			queries.add(strQueryDelete);
			
			strQueryDelete = "delete from " + PropertiesSystem.JDEDTA+".F0911 where GLICU IN "+nobatch+" and GLICUT = 'G' AND GLDCT = 'P9'";
			queries.add(strQueryDelete);
			
			int iddncrsm = dncRsm.get(0).getIddonacion() ;
			
			strQueryDelete = " update " + PropertiesSystem.ESQUEMA+".dnc_donacion " +
					"set estado = "+estadosigdncjde+", fechamod = current_timestamp, observacion = '"+motivoanula+"'  " +
					"where iddncrsm = "+ iddncrsm;
			queries.add(strQueryDelete);
			queriesAnula.add(strQueryDelete);
			
			strQueryDelete = " update " + PropertiesSystem.ESQUEMA+".dnc_donacion_jde set estado = "+estadosigdncjde+" where iddonacion = "+ iddncrsm;
			queries.add(strQueryDelete);
			queriesAnula.add(strQueryDelete);
			
			strQueryDelete = " update " + PropertiesSystem.ESQUEMA+".dnc_donacion_rsm " +
					" set estado = "+estadosigdncjde+", fechamod = current_timestamp, usrmod = " + coduser +
					" where iddonacionrsm = "+ iddncrsm;
			queries.add(strQueryDelete);
			queriesAnula.add(strQueryDelete);
				
			
			CodeUtil.putInSessionMap("an_QueriesToExecute", queriesAnula);
			
			boolean executed = ConsolidadoDepositosBcoCtrl.executeSqlQueries( queries );
			if(!executed){
				return msg = ConsolidadoDepositosBcoCtrl.getMsgStatus();
			}
				
		} catch (Exception e) {
			e.printStackTrace(); 
			msg = "Error al aplicar actualización de donaciones";
		} 
		return msg;
	}
	
	public static boolean grabarDonacion(Session session, List<MetodosPago>pagodonacion, Vautoriz vaut, int codigocliente){
		
		boolean done = true;
		String strSqlInsert = "" ;
	
	
		try {
			
			if(codcomp == null)
				codcomp = "";
			if(tiporecibo == null)
				tiporecibo = "";
			
			msgProceso =  "" ;
			
			List<String>queries = new ArrayList<String>();
			
			String current_timestamp_dncrsm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.")
					.format(new Date()) + String.valueOf( (100000 + new Random().nextInt(900000)) );
			
			//&& ============  grabar en el maestro de donaciones
			String strSqlSelectDncMstr = "(select iddonacionrsm from "+PropertiesSystem.ESQUEMA
					+".dnc_donacion_rsm where fecha = '"+current_timestamp_dncrsm+"' )" ;
			
			String strSqlInsertDncMstr = "insert into " +PropertiesSystem.ESQUEMA +".dnc_donacion_rsm "+
			"(iddonacionrsm, caid,numrec, codcomp, tiporec, clientecodigo, clientenombre, fecha, codigocajero, estado, fechamod, usrmod )"+
			" values "+
			"( DEFAULT, @caid, @numrec, '@codcomp', '@tiporec', @clientecodigo, '@clientenombre', '@fecha', @codigocajero, 1, '@fecha', @usrmod )";
			
			queries.add( 
				strSqlInsertDncMstr
					.replace("@caid",  Integer.toString(caid) )
					.replace("@numrec", Integer.toString( numrec ))
					.replace("@codcomp", codcomp )
					.replace("@tiporec", tiporecibo)
					.replace("@clientecodigo", Integer.toString(pagodonacion.get(0).getDonaciones().get(0).getCodigocliente()) )
					.replace("@clientenombre", pagodonacion.get(0).getDonaciones().get(0).getNombrecliente() )
					.replace("@fecha", current_timestamp_dncrsm)
					.replace("@codigocajero", Integer.toString(pagodonacion.get(0).getDonaciones().get(0).getCodigocajero()))
					.replace("@fechamod", current_timestamp_dncrsm ) 
					.replace("@usrmod",Integer.toString(pagodonacion.get(0).getDonaciones().get(0).getCodigocajero()))
			);
			
			String current_timestamp = "";
			String strQueryComisionPos = "( select cxcomi from "
				+PropertiesSystem.ESQUEMA +".f55ca03 " +
				"where trim(cxcafi) = '@CODIGOPOS' fetch first rows only )" ;	
			
			String strQueryPosClasificaMarcas = "( select cxclasificamarca from "
					+PropertiesSystem.ESQUEMA +".f55ca03 " +
					"where trim(cxcafi) = '@CODIGOPOS' fetch first rows only )" ;
			
			
			//&& ============ 1. grabar en transaccional de donaciones
			
			strSqlInsert = 
			" insert into "+PropertiesSystem.ESQUEMA+".dnc_donacion " +
			"( IDDONACION, IDBENFICIARIO, CODIGO, CLIENTECODIGO, CLIENTENOMBRE, MONTORECIBIDO, " +
			"MONTOAPLICADO, MONEDA, FORMADEPAGO, CLIENTEID, CODCAJERO, NUMREC, TIPOREC, CAID, " +
			"CODCOMP, REFERENCIA1, REFERENCIA2, SOCKETPOS, CODIGOPOS, COMISIONPOS, " +
			"ESTADO, FECHACREA, FECHAMOD, OBSERVACION, BENEFICIARIONOMBRE, IDDNCRSM, " +
			"CODIGOMARCATARJETA, MARCATARJETA, LIQUIDARPORMARCA ) " +
			"values " +
			"( DEFAULT, @IDBENFICIARIO, @BNF_CODIGO, @CLIENTECODIGO, '@CLIENTENOMBRE', @MONTORECIBIDO, " +
			"@MONTOAPLICADO, '@MONEDA', '@FORMADEPAGO', '@CLIENTEID', @CODCAJERO, @NUMREC, '@TIPOREC', @CAID, " +
			"'@CODCOMP', '@REFERENCIA1', '@REFERENCIA2', @SOCKETPOS, @CODIGOPOS, @COMISIONPOS, " +
			" 1, '@FECHACREA', '@FECHAMOD', '@OBSERVACION', '@BENEFICIARIONOMBRE', @IDDNCRSM," +
			" '@CODIGOMARCATARJETA', '@MARCATARJETA', @LIQUIDARPORMARCA )";
			
			String strQueryInsertForJde =
				" insert into "+PropertiesSystem.ESQUEMA+".dnc_donacion_jde " +
				" ( ID_DONACION_JDE,CAID,CODCOMP,NUMREC,TIPOREC,IDDONACION,CLIENTECODIGO,FECHA, " +
				" NUMEROBATCH, NUMERODOCJDE,TIPODOCUMENTO,CODIGOCAJERO,ESTADO ) "+ 
				"values " +
				"( DEFAULT, @CAID, '@CODCOMP', @NUMREC,'@TIPOREC',@IDDONACION, @CLIENTECODIGO,'@FECHA', " +
				" @NUMEROBATCH, @NUMERODOCJDE, '@TIPODOCUMENTO',@CODIGOCAJERO, 1) "; 
			
			String queryForPos;
			String queryForPosClasifica;
			
			int codigocajero = vaut.getId().getCodreg();
			
			for (MetodosPago mp : pagodonacion) {
				
				List<DncIngresoDonacion>donaciones = mp.getDonaciones();
				
				for (DncIngresoDonacion dnc : donaciones) {
					
					current_timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.").format(new Date())
											+ String.valueOf( (100000 + new Random().nextInt(900000)) );
					
					queryForPos = (mp.getMetodo().compareTo(MetodosPagoCtrl.TARJETA) == 0)?	strQueryComisionPos
									.replace("@CODIGOPOS", mp.getReferencia().trim() ) :"0" ;
									
					queryForPosClasifica = (mp.getMetodo().compareTo(MetodosPagoCtrl.TARJETA) == 0)? strQueryPosClasificaMarcas
							.replace("@CODIGOPOS", mp.getReferencia().trim() ) : "0" ;	
					
					queries.add(
						strSqlInsert
						.replace("@IDBENFICIARIO", Integer.toString(dnc.getIdbenficiario()))
						.replace("@BNF_CODIGO",  Integer.toString( dnc.getCodigo() ) )
						.replace("@CLIENTECODIGO", Integer.toString( dnc.getCodigocliente() ) ) 
						.replace("@CLIENTENOMBRE", dnc.getNombrecliente() ) 
						.replace("@MONTORECIBIDO", dnc.getMontorecibido().toString()) 
						.replace("@MONTOAPLICADO", dnc.getMontorecibido().toString()) 
						.replace("@MONEDA", dnc.getMoneda() ) 
						.replace("@FORMADEPAGO", mp.getMetodo() )
						.replace("@CLIENTEID", mp.getReferencia2())
						.replace("@CODCAJERO", Integer.toString( dnc.getCodigocajero() ) )
						.replace("@NUMREC",  Integer.toString( numrec ))
						.replace("@TIPOREC", tiporecibo)
						.replace("@CAID", Integer.toString(caid) )
						.replace("@CODCOMP", codcomp )
						.replace("@REFERENCIA1", mp.getReferencia2())
						.replace("@REFERENCIA2", mp.getReferencia3() )
						.replace("@SOCKETPOS", ( mp.getVmanual().compareTo("2") == 0)? "1":"0" )
						.replace("@CODIGOPOS", ((mp.getReferencia().isEmpty() )? "0" : mp.getReferencia()) )
						.replace("@COMISIONPOS", queryForPos  )
						.replace("@FECHACREA", current_timestamp )
						.replace("@FECHAMOD", current_timestamp )
						.replace("@OBSERVACION", "")
						.replace("@BENEFICIARIONOMBRE", dnc.getNombrecorto() )
						.replace("@IDDNCRSM", strSqlSelectDncMstr )
						
						.replace("@CODIGOMARCATARJETA", dnc.getCodigomarcatarjeta().toLowerCase() )
						.replace("@MARCATARJETA", dnc.getMarcatarjeta().toLowerCase() )
						.replace("@LIQUIDARPORMARCA", queryForPosClasifica )
					);
				}
			}
				
			noBatchsAndDocs = new ArrayList<int[]>();
			
			done = crearAsientoContableDonacion(session, pagodonacion, vaut);
			if(!done){
				return done;
			}
			
			//&& ============ crear el enlace entre caja y jde
			strQueryInsertForJde = strQueryInsertForJde 
				.replace("@CAID", Integer.toString(caid) )
				.replace("@CODCOMP", codcomp ) 
				.replace("@NUMREC",  Integer.toString( numrec ) ) 
				.replace("@TIPOREC", tiporecibo ) 
				.replace("@IDDONACION", strSqlSelectDncMstr )
				.replace("@CLIENTECODIGO", Integer.toString( codigocliente ) ) 
				.replace("@FECHA", new SimpleDateFormat("yyyy-MM-dd").format(new Date() ) ) 
				.replace("@TIPODOCUMENTO", "P9" ) 
				.replace("@CODIGOCAJERO",  Integer.toString( codigocajero ) );

			for (int[] batchAndDoc : noBatchsAndDocs) {
				
				queries.add(
						strQueryInsertForJde 
						.replace("@NUMEROBATCH",  Integer.toString(batchAndDoc[0]) )
						.replace("@NUMERODOCJDE", Integer.toString(batchAndDoc[1]) )
					); 
			}
			
			for (String query : queries) {
				LogCajaService.CreateLog("grabarDonacion", "QRY", query);
				
				int rows = session.createSQLQuery(query).executeUpdate() ;
				done = rows > 0;
				
				if(!done){
					msgProceso = "Error al grabar datos de donacion en gcpmcaja";
					return done;
				}
				
			}
			// && ======== Obtener el id del maestro de donaciones.
			LogCajaService.CreateLog("grabarDonacion", "QRY", strSqlSelectDncMstr.replace("(", "").replace(")", ""));
			
			@SuppressWarnings("unchecked")
			List<Integer> idsDncRsm = (ArrayList<Integer>) session.createSQLQuery(strSqlSelectDncMstr.replace("(", "").replace(")", "") ).list();
			iddncrsm = idsDncRsm.get(0).intValue();
 
		} catch (Exception e) {
			LogCajaService.CreateLog("grabarDonacion", "ERR", e.getMessage());
			e.printStackTrace();
			done = false;
			msgProceso ="Error intero en proceso de insercion en bd" ; 
		}finally{
			
			done = msgProceso == null || msgProceso.isEmpty(); 
			
		}
		return done;
	}
	
	public static boolean grabarDonacion(List<MetodosPago>pagodonacion,	Vautoriz vaut, int codigocliente){
		
		boolean done = true;
		String strSqlInsert = "" ;
	
	
		try {
			
			if(codcomp == null)
				codcomp = "";
			if(tiporecibo == null)
				tiporecibo = "";
			
			msgProceso =  "" ;
			
			List<String>queries = new ArrayList<String>();
			
			String current_timestamp_dncrsm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.")
					.format(new Date()) + String.valueOf( (100000 + new Random().nextInt(900000)) );
			
			//&& ============  grabar en el maestro de donaciones
			String strSqlSelectDncMstr = "(select iddonacionrsm from "+PropertiesSystem.ESQUEMA
					+".dnc_donacion_rsm where fecha = '"+current_timestamp_dncrsm+"' )" ;
			
			String strSqlInsertDncMstr = "insert into " +PropertiesSystem.ESQUEMA +".dnc_donacion_rsm "+
			"(iddonacionrsm, caid,numrec, codcomp, tiporec, clientecodigo, clientenombre, fecha, codigocajero, estado, fechamod, usrmod )"+
			" values "+
			"( DEFAULT, @caid, @numrec, '@codcomp', '@tiporec', @clientecodigo, '@clientenombre', '@fecha', @codigocajero, 1, '@fecha', @usrmod )";
			
			queries.add( 
				strSqlInsertDncMstr
					.replace("@caid",  Integer.toString(caid) )
					.replace("@numrec", Integer.toString( numrec ))
					.replace("@codcomp", codcomp )
					.replace("@tiporec", tiporecibo)
					.replace("@clientecodigo", Integer.toString(pagodonacion.get(0).getDonaciones().get(0).getCodigocliente()) )
					.replace("@clientenombre", pagodonacion.get(0).getDonaciones().get(0).getNombrecliente() )
					.replace("@fecha", current_timestamp_dncrsm)
					.replace("@codigocajero", Integer.toString(pagodonacion.get(0).getDonaciones().get(0).getCodigocajero()))
					.replace("@fechamod", current_timestamp_dncrsm ) 
					.replace("@usrmod",Integer.toString(pagodonacion.get(0).getDonaciones().get(0).getCodigocajero()))
			);
			
			String current_timestamp = "";
			String strQueryComisionPos = "( select cxcomi from "
				+PropertiesSystem.ESQUEMA +".f55ca03 " +
				"where trim(cxcafi) = '@CODIGOPOS' fetch first rows only )" ;	
			
			String strQueryPosClasificaMarcas = "( select cxclasificamarca from "
					+PropertiesSystem.ESQUEMA +".f55ca03 " +
					"where trim(cxcafi) = '@CODIGOPOS' fetch first rows only )" ;
			
			
			//&& ============ 1. grabar en transaccional de donaciones
			
			strSqlInsert = 
			" insert into "+PropertiesSystem.ESQUEMA+".dnc_donacion " +
			"( IDDONACION, IDBENFICIARIO, CODIGO, CLIENTECODIGO, CLIENTENOMBRE, MONTORECIBIDO, " +
			"MONTOAPLICADO, MONEDA, FORMADEPAGO, CLIENTEID, CODCAJERO, NUMREC, TIPOREC, CAID, " +
			"CODCOMP, REFERENCIA1, REFERENCIA2, SOCKETPOS, CODIGOPOS, COMISIONPOS, " +
			"ESTADO, FECHACREA, FECHAMOD, OBSERVACION, BENEFICIARIONOMBRE, IDDNCRSM, " +
			"CODIGOMARCATARJETA, MARCATARJETA, LIQUIDARPORMARCA ) " +
			"values " +
			"( DEFAULT, @IDBENFICIARIO, @BNF_CODIGO, @CLIENTECODIGO, '@CLIENTENOMBRE', @MONTORECIBIDO, " +
			"@MONTOAPLICADO, '@MONEDA', '@FORMADEPAGO', '@CLIENTEID', @CODCAJERO, @NUMREC, '@TIPOREC', @CAID, " +
			"'@CODCOMP', '@REFERENCIA1', '@REFERENCIA2', @SOCKETPOS, @CODIGOPOS, @COMISIONPOS, " +
			" 1, '@FECHACREA', '@FECHAMOD', '@OBSERVACION', '@BENEFICIARIONOMBRE', @IDDNCRSM," +
			" '@CODIGOMARCATARJETA', '@MARCATARJETA', @LIQUIDARPORMARCA )";
			
			String strQueryInsertForJde =
				" insert into "+PropertiesSystem.ESQUEMA+".dnc_donacion_jde " +
				" ( ID_DONACION_JDE,CAID,CODCOMP,NUMREC,TIPOREC,IDDONACION,CLIENTECODIGO,FECHA, " +
				" NUMEROBATCH, NUMERODOCJDE,TIPODOCUMENTO,CODIGOCAJERO,ESTADO ) "+ 
				"values " +
				"( DEFAULT, @CAID, '@CODCOMP', @NUMREC,'@TIPOREC',@IDDONACION, @CLIENTECODIGO,'@FECHA', " +
				" @NUMEROBATCH, @NUMERODOCJDE, '@TIPODOCUMENTO',@CODIGOCAJERO, 1) "; 
			
			String queryForPos;
			String queryForPosClasifica;
			
			int codigocajero = vaut.getId().getCodreg();
			
			for (MetodosPago mp : pagodonacion) {
				
				List<DncIngresoDonacion>donaciones = mp.getDonaciones();
				
				for (DncIngresoDonacion dnc : donaciones) {
					
					current_timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.").format(new Date())
											+ String.valueOf( (100000 + new Random().nextInt(900000)) );
					
					queryForPos = (mp.getMetodo().compareTo(MetodosPagoCtrl.TARJETA) == 0)?	strQueryComisionPos
									.replace("@CODIGOPOS", mp.getReferencia().trim() ) :"0" ;
									
					queryForPosClasifica = (mp.getMetodo().compareTo(MetodosPagoCtrl.TARJETA) == 0)? strQueryPosClasificaMarcas
							.replace("@CODIGOPOS", mp.getReferencia().trim() ) : "0" ;	
					
					queries.add(
						strSqlInsert
						.replace("@IDBENFICIARIO", Integer.toString(dnc.getIdbenficiario()))
						.replace("@BNF_CODIGO",  Integer.toString( dnc.getCodigo() ) )
						.replace("@CLIENTECODIGO", Integer.toString( dnc.getCodigocliente() ) ) 
						.replace("@CLIENTENOMBRE", dnc.getNombrecliente() ) 
						.replace("@MONTORECIBIDO", dnc.getMontorecibido().toString()) 
						.replace("@MONTOAPLICADO", dnc.getMontorecibido().toString()) 
						.replace("@MONEDA", dnc.getMoneda() ) 
						.replace("@FORMADEPAGO", mp.getMetodo() )
						.replace("@CLIENTEID", mp.getReferencia2())
						.replace("@CODCAJERO", Integer.toString( dnc.getCodigocajero() ) )
						.replace("@NUMREC",  Integer.toString( numrec ))
						.replace("@TIPOREC", tiporecibo)
						.replace("@CAID", Integer.toString(caid) )
						.replace("@CODCOMP", codcomp )
						.replace("@REFERENCIA1", mp.getReferencia2())
						.replace("@REFERENCIA2", mp.getReferencia3() )
						.replace("@SOCKETPOS", ( mp.getVmanual().compareTo("2") == 0)? "1":"0" )
						.replace("@CODIGOPOS", ((mp.getReferencia().isEmpty() )? "0" : mp.getReferencia()) )
						.replace("@COMISIONPOS", queryForPos  )
						.replace("@FECHACREA", current_timestamp )
						.replace("@FECHAMOD", current_timestamp )
						.replace("@OBSERVACION", "")
						.replace("@BENEFICIARIONOMBRE", dnc.getNombrecorto() )
						.replace("@IDDNCRSM", strSqlSelectDncMstr )
						
						.replace("@CODIGOMARCATARJETA", dnc.getCodigomarcatarjeta().toLowerCase() )
						.replace("@MARCATARJETA", dnc.getMarcatarjeta().toLowerCase() )
						.replace("@LIQUIDARPORMARCA", queryForPosClasifica )
					);
				}
			}
				
			noBatchsAndDocs = new ArrayList<int[]>();
			
			done = crearAsientoContableDonacion(pagodonacion, vaut);
			if(!done){
				return done;
			}
			
			//&& ============ crear el enlace entre caja y jde
			strQueryInsertForJde = strQueryInsertForJde 
				.replace("@CAID", Integer.toString(caid) )
				.replace("@CODCOMP", codcomp ) 
				.replace("@NUMREC",  Integer.toString( numrec ) ) 
				.replace("@TIPOREC", tiporecibo ) 
				.replace("@IDDONACION", strSqlSelectDncMstr )
				.replace("@CLIENTECODIGO", Integer.toString( codigocliente ) ) 
				.replace("@FECHA", new SimpleDateFormat("yyyy-MM-dd").format(new Date() ) ) 
				.replace("@TIPODOCUMENTO", "P9" ) 
				.replace("@CODIGOCAJERO",  Integer.toString( codigocajero ) );

			for (int[] batchAndDoc : noBatchsAndDocs) {
				
				queries.add(
						strQueryInsertForJde 
						.replace("@NUMEROBATCH",  Integer.toString(batchAndDoc[0]) )
						.replace("@NUMERODOCJDE", Integer.toString(batchAndDoc[1]) )
					); 
			}
			
			done = ConsolidadoDepositosBcoCtrl.executeSqlQueries( queries ) ;
			if(!done){
				msgProceso = "Error al grabar datos de donacion en gcpmcaja";
			}
			
			// && ======== Obtener el id del maestro de donaciones.
			@SuppressWarnings("unchecked")
			List<Integer>idsDncRsm = (ArrayList<Integer>) ConsolidadoDepositosBcoCtrl.executeSqlQuery( strSqlSelectDncMstr, true, null );
			iddncrsm = idsDncRsm.get(0).intValue();
			
			
		} catch (Exception e) {
			LogCajaService.CreateLog("grabarDonacion", "ERR", e.getMessage());
			e.printStackTrace();
			done = false;
			msgProceso ="Error intero en proceso de insercion en bd" ; 
		}finally{
			
			done = msgProceso == null || msgProceso.isEmpty(); 
			
		}
		return done;
	}
	
	
	//**********************************************************
	//----------------------------------------------------------
	//++++++++++++++++++      INICIO     +++++++++++++++++++++++
	//----------------------------------------------------------
	//**********************************************************
	//Creado por Luis Alberto Fonseca Mendez 2019-04-24
	public static boolean grabarDonacion(Session sesion, Transaction trans, List<MetodosPago>pagodonacion,	Vautoriz vaut, int codigocliente){
		
		boolean done = true;
		String strSqlInsert = "" ;
	
	
		try {
			
			if(codcomp == null)
				codcomp = "";
			if(tiporecibo == null)
				tiporecibo = "";
			
			msgProceso =  "" ;
			
			List<String>queries = new ArrayList<String>();
			
			String current_timestamp_dncrsm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.")
					.format(new Date()) + String.valueOf( (100000 + new Random().nextInt(900000)) );
			
			//&& ============  grabar en el maestro de donaciones
			String strSqlSelectDncMstr = "(select iddonacionrsm from "+PropertiesSystem.ESQUEMA
					+".dnc_donacion_rsm where fecha = '"+current_timestamp_dncrsm+"' )" ;
			
			String strSqlInsertDncMstr = "insert into " +PropertiesSystem.ESQUEMA +".dnc_donacion_rsm "+
			"(iddonacionrsm, caid,numrec, codcomp, tiporec, clientecodigo, clientenombre, fecha, codigocajero, estado, fechamod, usrmod )"+
			" values "+
			"( DEFAULT, @caid, @numrec, '@codcomp', '@tiporec', @clientecodigo, '@clientenombre', '@fecha', @codigocajero, 1, '@fecha', @usrmod )";
			
			queries.add( 
				strSqlInsertDncMstr
					.replace("@caid",  Integer.toString(caid) )
					.replace("@numrec", Integer.toString( numrec ))
					.replace("@codcomp", codcomp )
					.replace("@tiporec", tiporecibo)
					.replace("@clientecodigo", Integer.toString(pagodonacion.get(0).getDonaciones().get(0).getCodigocliente()) )
					.replace("@clientenombre", pagodonacion.get(0).getDonaciones().get(0).getNombrecliente() )
					.replace("@fecha", current_timestamp_dncrsm)
					.replace("@codigocajero", Integer.toString(pagodonacion.get(0).getDonaciones().get(0).getCodigocajero()))
					.replace("@fechamod", current_timestamp_dncrsm ) 
					.replace("@usrmod",Integer.toString(pagodonacion.get(0).getDonaciones().get(0).getCodigocajero()))
			);
			
			String current_timestamp = "";
			String strQueryComisionPos = "( select cxcomi from "
				+PropertiesSystem.ESQUEMA +".f55ca03 " +
				"where trim(cxcafi) = '@CODIGOPOS' fetch first rows only )" ;	
			
			String strQueryPosClasificaMarcas = "( select cxclasificamarca from "
					+PropertiesSystem.ESQUEMA +".f55ca03 " +
					"where trim(cxcafi) = '@CODIGOPOS' fetch first rows only )" ;
			
			
			//&& ============ 1. grabar en transaccional de donaciones
			
			strSqlInsert = 
			" insert into "+PropertiesSystem.ESQUEMA+".dnc_donacion " +
			"( IDDONACION, IDBENFICIARIO, CODIGO, CLIENTECODIGO, CLIENTENOMBRE, MONTORECIBIDO, " +
			"MONTOAPLICADO, MONEDA, FORMADEPAGO, CLIENTEID, CODCAJERO, NUMREC, TIPOREC, CAID, " +
			"CODCOMP, REFERENCIA1, REFERENCIA2, SOCKETPOS, CODIGOPOS, COMISIONPOS, " +
			"ESTADO, FECHACREA, FECHAMOD, OBSERVACION, BENEFICIARIONOMBRE, IDDNCRSM, " +
			"CODIGOMARCATARJETA, MARCATARJETA, LIQUIDARPORMARCA ) " +
			"values " +
			"( DEFAULT, @IDBENFICIARIO, @BNF_CODIGO, @CLIENTECODIGO, '@CLIENTENOMBRE', @MONTORECIBIDO, " +
			"@MONTOAPLICADO, '@MONEDA', '@FORMADEPAGO', '@CLIENTEID', @CODCAJERO, @NUMREC, '@TIPOREC', @CAID, " +
			"'@CODCOMP', '@REFERENCIA1', '@REFERENCIA2', @SOCKETPOS, @CODIGOPOS, @COMISIONPOS, " +
			" 1, '@FECHACREA', '@FECHAMOD', '@OBSERVACION', '@BENEFICIARIONOMBRE', @IDDNCRSM," +
			" '@CODIGOMARCATARJETA', '@MARCATARJETA', @LIQUIDARPORMARCA )";
			
			String strQueryInsertForJde =
				" insert into "+PropertiesSystem.ESQUEMA+".dnc_donacion_jde " +
				" ( ID_DONACION_JDE,CAID,CODCOMP,NUMREC,TIPOREC,IDDONACION,CLIENTECODIGO,FECHA, " +
				" NUMEROBATCH, NUMERODOCJDE,TIPODOCUMENTO,CODIGOCAJERO,ESTADO ) "+ 
				"values " +
				"( DEFAULT, @CAID, '@CODCOMP', @NUMREC,'@TIPOREC',@IDDONACION, @CLIENTECODIGO,'@FECHA', " +
				" @NUMEROBATCH, @NUMERODOCJDE, '@TIPODOCUMENTO',@CODIGOCAJERO, 1) "; 
			
			String queryForPos;
			String queryForPosClasifica;
			
			int codigocajero = vaut.getId().getCodreg();
			
			for (MetodosPago mp : pagodonacion) {
				
				List<DncIngresoDonacion>donaciones = mp.getDonaciones();
				
				for (DncIngresoDonacion dnc : donaciones) {
					
					current_timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.").format(new Date())
											+ String.valueOf( (100000 + new Random().nextInt(900000)) );
					
					queryForPos = (mp.getMetodo().compareTo(MetodosPagoCtrl.TARJETA) == 0)?	strQueryComisionPos
									.replace("@CODIGOPOS", mp.getReferencia().trim() ) :"0" ;
									
					queryForPosClasifica = (mp.getMetodo().compareTo(MetodosPagoCtrl.TARJETA) == 0)? strQueryPosClasificaMarcas
							.replace("@CODIGOPOS", mp.getReferencia().trim() ) : "0" ;	
					
					queries.add(
						strSqlInsert
						.replace("@IDBENFICIARIO", Integer.toString(dnc.getIdbenficiario()))
						.replace("@BNF_CODIGO",  Integer.toString( dnc.getCodigo() ) )
						.replace("@CLIENTECODIGO", Integer.toString( dnc.getCodigocliente() ) ) 
						.replace("@CLIENTENOMBRE", dnc.getNombrecliente() ) 
						.replace("@MONTORECIBIDO", dnc.getMontorecibido().toString()) 
						.replace("@MONTOAPLICADO", dnc.getMontorecibido().toString()) 
						.replace("@MONEDA", dnc.getMoneda() ) 
						.replace("@FORMADEPAGO", mp.getMetodo() )
						.replace("@CLIENTEID", mp.getReferencia2())
						.replace("@CODCAJERO", Integer.toString( dnc.getCodigocajero() ) )
						.replace("@NUMREC",  Integer.toString( numrec ))
						.replace("@TIPOREC", tiporecibo)
						.replace("@CAID", Integer.toString(caid) )
						.replace("@CODCOMP", codcomp )
						.replace("@REFERENCIA1", mp.getReferencia2())
						.replace("@REFERENCIA2", mp.getReferencia3() )
						.replace("@SOCKETPOS", ( mp.getVmanual().compareTo("2") == 0)? "1":"0" )
						.replace("@CODIGOPOS", ((mp.getReferencia().isEmpty() )? "0" : mp.getReferencia()) )
						.replace("@COMISIONPOS", queryForPos  )
						.replace("@FECHACREA", current_timestamp )
						.replace("@FECHAMOD", current_timestamp )
						.replace("@OBSERVACION", "")
						.replace("@BENEFICIARIONOMBRE", dnc.getNombrecorto() )
						.replace("@IDDNCRSM", strSqlSelectDncMstr )
						
						.replace("@CODIGOMARCATARJETA", dnc.getCodigomarcatarjeta().toLowerCase() )
						.replace("@MARCATARJETA", dnc.getMarcatarjeta().toLowerCase() )
						.replace("@LIQUIDARPORMARCA", queryForPosClasifica )
					);
				}
			}
				
			noBatchsAndDocs = new ArrayList<int[]>();
			
			//cambio hecho por lfonseca
//			done = crearAsientoContableDonacion(pagodonacion, vaut);
			done = crearAsientoContableDonacion(sesion, trans, pagodonacion, vaut);
			if(!done){
				return done;
			}
			
			//&& ============ crear el enlace entre caja y jde
			strQueryInsertForJde = strQueryInsertForJde 
				.replace("@CAID", Integer.toString(caid) )
				.replace("@CODCOMP", codcomp ) 
				.replace("@NUMREC",  Integer.toString( numrec ) ) 
				.replace("@TIPOREC", tiporecibo ) 
				.replace("@IDDONACION", strSqlSelectDncMstr )
				.replace("@CLIENTECODIGO", Integer.toString( codigocliente ) ) 
				.replace("@FECHA", new SimpleDateFormat("yyyy-MM-dd").format(new Date() ) ) 
				.replace("@TIPODOCUMENTO", "P9" ) 
				.replace("@CODIGOCAJERO",  Integer.toString( codigocajero ) );

			for (int[] batchAndDoc : noBatchsAndDocs) {
				
				queries.add(
						strQueryInsertForJde 
						.replace("@NUMEROBATCH",  Integer.toString(batchAndDoc[0]) )
						.replace("@NUMERODOCJDE", Integer.toString(batchAndDoc[1]) )
					); 
			}
			
			//cambio hecho por lfonseca
//			done = ConsolidadoDepositosBcoCtrl.executeSqlQueries( queries ) ;
			done = ConsolidadoDepositosBcoCtrl.executeSqlQueries( sesion, trans, queries ) ;
			
			if(!done){
				msgProceso = "Error al grabar datos de donacion en gcpmcaja";
			}
			
			// && ======== Obtener el id del maestro de donaciones.
			@SuppressWarnings("unchecked")
			List<Integer>idsDncRsm = (ArrayList<Integer>) ConsolidadoDepositosBcoCtrl.executeSqlQuery( strSqlSelectDncMstr, true, null );
			iddncrsm = idsDncRsm.get(0).intValue();
			
			
		} catch (Exception e) {
			e.printStackTrace();
			done = false;
			msgProceso ="Error intero en proceso de insercion en bd" ; 
		}finally{
			
			done = msgProceso == null || msgProceso.isEmpty(); 
			
		}
		return done;
	}
	
	//cambio hecho por lfonseca
	@SuppressWarnings("unchecked")
	public static boolean crearAsientoContableDonacion(Session sesion, Transaction trans, List<MetodosPago>pagodonacion, Vautoriz vaut ){
		boolean hecho = true;
		List<DncIngresoDonacion> donaciones = new ArrayList<DncIngresoDonacion>();
		List<Integer>codigosBeneficiario;
		List<String> monedasbeneficiario;
		
		try {
			
			msgProceso =  "" ;
	 
			//&& ========== 0. Crear lista con todos los registros de donacion.
			for (MetodosPago pago : pagodonacion) {
				donaciones.addAll(pago.getDonaciones());
			}
			
			//&& ========== 1. separar los codigos de los beneficiarios.
			codigosBeneficiario = (ArrayList<Integer>)CodeUtil.selectPropertyListFromEntity(donaciones, "codigo", true);
			for (final Integer codbnf : codigosBeneficiario) {
				
				List<DncIngresoDonacion> donacionesxbnf = (List<DncIngresoDonacion>)
				CollectionUtils.select( donaciones, new Predicate(){
					public boolean evaluate(Object o) {
						return ((DncIngresoDonacion)o).getCodigo() == codbnf;
					}
				}); 
				
				//&& ========== 2. separar por moneda los pagos recibidos por beneficiarios.
				monedasbeneficiario = (ArrayList<String>)CodeUtil.selectPropertyListFromEntity(donacionesxbnf, "moneda", true);
				
				for (final String monedaxbnf : monedasbeneficiario) {
					
					List<DncIngresoDonacion> donacionesxbnfxmon = (List<DncIngresoDonacion>) CollectionUtils
							.select(donacionesxbnf, new Predicate() {
								public boolean evaluate(Object o) {
									return ((DncIngresoDonacion) o).getMoneda().trim().compareTo(monedaxbnf.trim()) == 0 ;
								}
							});
					//&& ========== 3. obtener total por transacciones para el beneficiario en la moneda.
					BigDecimal totaldncxbnfxmon = BigDecimal.ZERO;
					for (DncIngresoDonacion dncxbnfxmon : donacionesxbnfxmon) {
						totaldncxbnfxmon = totaldncxbnfxmon.add(dncxbnfxmon.getMontorecibido());
					}
					 
					//&& ============ Numero de batch y de documento para el asiento contable
				
					nobatch = Divisas.numeroSiguienteJdeE1( );
					nodocjde = Divisas.numeroSiguienteJdeE1( CodigosJDE1.NUMEROPAGORECIBO );
					
					if(nobatch == 0 || nodocjde == 0) {
						return hecho = false;
					}
					
					int[] iNobatchNodoc  = new int[]{nobatch, nodocjde };
					
					noBatchsAndDocs.add(iNobatchNodoc);
					
					//&& ========== 4. Crear el asiento de diario
					//cambio hecho por lfonseca
//					hecho = grabarDonacionEnJDE(donacionesxbnfxmon, totaldncxbnfxmon, vaut, "COR");
					hecho = grabarDonacionEnJDE(sesion, trans, donacionesxbnfxmon, totaldncxbnfxmon, vaut, "COR");
					
					if(!hecho){
						return hecho;
					}
					
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			hecho = false;
		}finally{
		}
		return hecho;
	}
	
	
	//**********************************************************
	//----------------------------------------------------------
	//++++++++++++++++++       FIN       +++++++++++++++++++++++
	//----------------------------------------------------------
	//**********************************************************
	
	
	@SuppressWarnings("unchecked")
	public static boolean crearAsientoContableDonacion( List<MetodosPago>pagodonacion, Vautoriz vaut ){
		boolean hecho = true;
		List<DncIngresoDonacion> donaciones = new ArrayList<DncIngresoDonacion>();
		List<Integer>codigosBeneficiario;
		List<String> monedasbeneficiario;
		
		try {
			
			msgProceso =  "" ;
	 
			//&& ========== 0. Crear lista con todos los registros de donacion.
			for (MetodosPago pago : pagodonacion) {
				donaciones.addAll(pago.getDonaciones());
			}
			
			//&& ========== 1. separar los codigos de los beneficiarios.
			codigosBeneficiario = (ArrayList<Integer>)CodeUtil.selectPropertyListFromEntity(donaciones, "codigo", true);
			for (final Integer codbnf : codigosBeneficiario) {
				
				List<DncIngresoDonacion> donacionesxbnf = (List<DncIngresoDonacion>)
				CollectionUtils.select( donaciones, new Predicate(){
					public boolean evaluate(Object o) {
						return ((DncIngresoDonacion)o).getCodigo() == codbnf;
					}
				}); 
				
				//&& ========== 2. separar por moneda los pagos recibidos por beneficiarios.
				monedasbeneficiario = (ArrayList<String>)CodeUtil.selectPropertyListFromEntity(donacionesxbnf, "moneda", true);
				
				for (final String monedaxbnf : monedasbeneficiario) {
					
					List<DncIngresoDonacion> donacionesxbnfxmon = (List<DncIngresoDonacion>) CollectionUtils
							.select(donacionesxbnf, new Predicate() {
								public boolean evaluate(Object o) {
									return ((DncIngresoDonacion) o).getMoneda().trim().compareTo(monedaxbnf.trim()) == 0 ;
								}
							});
					//&& ========== 3. obtener total por transacciones para el beneficiario en la moneda.
					BigDecimal totaldncxbnfxmon = BigDecimal.ZERO;
					for (DncIngresoDonacion dncxbnfxmon : donacionesxbnfxmon) {
						totaldncxbnfxmon = totaldncxbnfxmon.add(dncxbnfxmon.getMontorecibido());
					}
					 
					//&& ============ Numero de batch y de documento para el asiento contable
				
					nobatch = Divisas.numeroSiguienteJdeE1( );
					nodocjde = Divisas.numeroSiguienteJdeE1( CodigosJDE1.NUMEROPAGORECIBO );
					
					if(nobatch == 0 || nodocjde == 0) {
						return hecho = false;
					}
					
					int[] iNobatchNodoc  = new int[]{nobatch, nodocjde };
					
					noBatchsAndDocs.add(iNobatchNodoc);
					
					//&& ========== 4. Crear el asiento de diario
					hecho = grabarDonacionEnJDE(donacionesxbnfxmon, totaldncxbnfxmon, vaut, "COR");
					if(!hecho){
						return hecho;
					}
					
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			hecho = false;
		}finally{
		}
		return hecho;
	}
	
	@SuppressWarnings("unchecked")
	public static boolean crearAsientoContableDonacion( Session session, List<MetodosPago>pagodonacion, Vautoriz vaut ){
		boolean hecho = true;
		List<DncIngresoDonacion> donaciones = new ArrayList<DncIngresoDonacion>();
		List<Integer>codigosBeneficiario;
		List<String> monedasbeneficiario;
		
		try {
			
			msgProceso =  "" ;
	 
			//&& ========== 0. Crear lista con todos los registros de donacion.
			for (MetodosPago pago : pagodonacion) {
				donaciones.addAll(pago.getDonaciones());
			}
			
			//&& ========== 1. separar los codigos de los beneficiarios.
			codigosBeneficiario = (ArrayList<Integer>)CodeUtil.selectPropertyListFromEntity(donaciones, "codigo", true);
			for (final Integer codbnf : codigosBeneficiario) {
				
				List<DncIngresoDonacion> donacionesxbnf = (List<DncIngresoDonacion>)
				CollectionUtils.select( donaciones, new Predicate(){
					public boolean evaluate(Object o) {
						return ((DncIngresoDonacion)o).getCodigo() == codbnf;
					}
				}); 
				
				//&& ========== 2. separar por moneda los pagos recibidos por beneficiarios.
				monedasbeneficiario = (ArrayList<String>)CodeUtil.selectPropertyListFromEntity(donacionesxbnf, "moneda", true);
				
				for (final String monedaxbnf : monedasbeneficiario) {
					
					List<DncIngresoDonacion> donacionesxbnfxmon = (List<DncIngresoDonacion>) CollectionUtils
							.select(donacionesxbnf, new Predicate() {
								public boolean evaluate(Object o) {
									return ((DncIngresoDonacion) o).getMoneda().trim().compareTo(monedaxbnf.trim()) == 0 ;
								}
							});
					//&& ========== 3. obtener total por transacciones para el beneficiario en la moneda.
					BigDecimal totaldncxbnfxmon = BigDecimal.ZERO;
					for (DncIngresoDonacion dncxbnfxmon : donacionesxbnfxmon) {
						totaldncxbnfxmon = totaldncxbnfxmon.add(dncxbnfxmon.getMontorecibido());
					}
					 
					//&& ============ Numero de batch y de documento para el asiento contable
				
					nobatch = Divisas.numeroSiguienteJdeE1( );
					nodocjde = Divisas.numeroSiguienteJdeE1( CodigosJDE1.NUMEROPAGORECIBO );
					
					if(nobatch == 0 || nodocjde == 0) {
						return hecho = false;
					}
					
					int[] iNobatchNodoc  = new int[]{nobatch, nodocjde };
					
					noBatchsAndDocs.add(iNobatchNodoc);
					
					//&& ========== 4. Crear el asiento de diario
					hecho = grabarDonacionEnJDE(session, donacionesxbnfxmon, totaldncxbnfxmon, vaut, "COR");
					if(!hecho){
						return hecho;
					}
					
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			hecho = false;
		}finally{
		}
		return hecho;
	}
	
	
	@SuppressWarnings("unchecked")
	public static boolean grabarDonacionEnJDE(Session session, List<DncIngresoDonacion> donacionesxbnfxmon, BigDecimal totaldonacion,Vautoriz vaut, String monedabase ){
		boolean hecho = true;
		
		Vf0901 vfBnfCuenta = null;
		DncBeneficiarioCuentas dncBnfCtaCfg ;
		Date fechadoc = new Date();
		BigDecimal tasaoficial = BigDecimal.ONE;
		
		try {
			
			msgProceso =  "" ;
			
			String strSqlTcambio = "select * from "+PropertiesSystem.ESQUEMA
					+".tcambio where cxeft = '"+new SimpleDateFormat("yyyy-MM-dd").format(fechadoc)
					+"' and cxcrcd = 'COR'" ;
			
			LogCajaService.CreateLog("grabarDonacionEnJDE", "QRY", strSqlTcambio);
			
			List<Tcambio>tasas = (ArrayList<Tcambio>)ConsolidadoDepositosBcoCtrl.executeSqlQuery( strSqlTcambio, true, Tcambio.class );
			if(tasas == null || tasas.isEmpty() ){
				msgProceso = "tasa de cambio oficial no configurada ";
				return false;
			}
			tasaoficial = tasas.get(0).getId().getCxcrrd();
			
			String strQueryExecute;
			
			String strSqlCtaBnf = "select * from " + PropertiesSystem.ESQUEMA +
					".dnc_beneficiario_cuentas  where idbeneficiario = @BNF_ID " +
					"and codigobnf = @BNF_CODIGO and moneda = '@MONEDA' " +
					"and trim(codcomp) = '@CODCOMP' fetch first rows only ";
			
			String strSqlF0901 = " select * from "+ PropertiesSystem.ESQUEMA +
					".Vf0901 where trim(gmmcu) = '@GMMCU' and trim( gmobj ) = '@GMOBJ' " +
					"and trim(gmsub) = '@GMSUB' and trim(gmpec) <> 'N' fetch first rows only ";
			
			String strSqlCtaformaPago = 
			" select  c1mcu, c1obj, c1sub, "+ 
			"ifnull( (select gmaid  from "+PropertiesSystem.JDEDTA+".F0901" +
				" where trim(gmmcu) = trim(c1mcu) and trim(gmobj) = trim(c1obj) " +
				"and trim(gmsub) = trim(c1sub) ), 0 ),"+ 
			"( trim(c1mcu)||'.'|| trim(c1obj)||'.'||trim(c1sub)) ,"+ 
			"( case when length(trim(c1mcu) ) = 4 then substr(trim(c1mcu), 1, 2) else trim(c1mcu) end )"+ 
			"from "+PropertiesSystem.ESQUEMA+".f55ca011 "+ 
			"where c1id = @CAID and trim(c1rp01) = '@CODCOMP' and c1crcd = '@MONEDA' " +
			"and c1ryin ='@MPAGO' and c1stat = 'A' "; 
			
			DncIngresoDonacion dnc = donacionesxbnfxmon.get(0);
			
			strQueryExecute = 
					strSqlCtaBnf.replace("@BNF_ID", String.valueOf( dnc.getIdbenficiario() ) )
					.replace("@BNF_CODIGO", String.valueOf( dnc.getCodigo() ) )
					.replace("@MONEDA", dnc.getMoneda() )
					.replace("@CODCOMP", codcomp);
				
			LogCajaService.CreateLog("grabarDonacionEnJDE", "QRY", strQueryExecute);
			
			List<DncBeneficiarioCuentas> bnf_cta = (ArrayList<DncBeneficiarioCuentas>)
					ConsolidadoDepositosBcoCtrl.executeSqlQuery(strQueryExecute, true, DncBeneficiarioCuentas.class );
			
				
			if(bnf_cta == null ||  bnf_cta.isEmpty() ){
				msgProceso = "No hay cuenta contable configurada para el beneficiario  " + dnc.getNombrecorto() ;
				return false;
			}
				
			dncBnfCtaCfg = bnf_cta.get(0);
			
			strQueryExecute = 
				strSqlF0901.replace("@GMMCU", dncBnfCtaCfg.getUnegocio().trim() )
					.replace("@GMOBJ",dncBnfCtaCfg.getObjeto().trim() )
					.replace("@GMSUB",dncBnfCtaCfg.getSubsidiaria().trim() ) ;
			
			LogCajaService.CreateLog("grabarDonacionEnJDE", "QRY", strQueryExecute);
			
			List<Vf0901>bnf_CuentasF0901 = ( ArrayList<Vf0901>) ConsolidadoDepositosBcoCtrl.executeSqlQuery( strQueryExecute, true, Vf0901.class );
			if(bnf_CuentasF0901 == null || bnf_CuentasF0901.isEmpty()){
				msgProceso = "No se encontro en JDE cuenta contable para la configuracion de cuenta para beneficiario " + dnc.getNombrecorto() ;
				return false;
			}
			
			vfBnfCuenta = bnf_CuentasF0901.get(0);
			 
			
			String dtaCjaRec = 
					( (numrec == 0)? "": ",Rc:" + numrec) +
					( (tiporecibo.isEmpty())?"Dnc Independ.": ", Tp:" + tiporecibo );
					 
			String obsDnc = "Caja:"+ caid +  ((dtaCjaRec.isEmpty() )? "" : dtaCjaRec)   ;		
			String concepto = "Donacion: "+ dnc.getNombrecorto().toUpperCase()  ;
			
			String codsucdoc = vfBnfCuenta.getId().getGmco();
			String ctaBnfGmaid  = vfBnfCuenta.getId().getGmaid() ;
			String codbnf_aux = String.format("%08d", dnc.getCodigo() ) ;
			
			//&& =========== datos para generar el asiento contable
			List<DatosComprobanteF0911> lineasComprobante = new ArrayList<DatosComprobanteF0911>();
			
			DatosComprobanteF0911 dtaCtaBeneficiario = new DatosComprobanteF0911(
					ctaBnfGmaid,
					totaldonacion.negate(), 
					obsDnc,
					tasaoficial,
					codbnf_aux,
					"A",
					codsucdoc );
			 lineasComprobante.add(dtaCtaBeneficiario);
			
			//&& =========================== crear lineas por metodo de pago
				List<Object[]> lstDtaCtaMpago ;
				Object[] ctaMpago; 
				
				for (DncIngresoDonacion dncEvalua : donacionesxbnfxmon) {
					
					strQueryExecute = 
						strSqlCtaformaPago.replace("@MONEDA", dnc.getMoneda())
						.replace("@CODCOMP", dnc.getCodcomp().trim() )
						.replace("@MPAGO", dnc.getCodigoformapago().trim()) 
						.replace("@CAID", Integer.toString( dnc.getCaid() ) ) ;
					
					LogCajaService.CreateLog("grabarDonacionEnJDE", "QRY", strQueryExecute);
				
					lstDtaCtaMpago = (ArrayList<Object[]> )ConsolidadoDepositosBcoCtrl.executeSqlQuery(strQueryExecute, true, null );
					
					if(lstDtaCtaMpago == null || lstDtaCtaMpago.isEmpty() ){
						msgProceso = "No se encontro la configuracion de cuenta contable para forma de pago " + dnc.getFormadepago() ;
						return false;
					}
					
					ctaMpago = lstDtaCtaMpago.get(0);
					String mpagoGmaid  = String.valueOf( ctaMpago[3] ).trim() ;
					String observacion = "Fp:" + dncEvalua.getFormadepago().trim() ;
			
					DatosComprobanteF0911 dtaCtaFormaPago = new DatosComprobanteF0911(
							mpagoGmaid,
							dncEvalua.getMontorecibido(), 
							observacion,
							tasaoficial,
							"",
							"",
							codsucdoc );
					 lineasComprobante.add(dtaCtaFormaPago);
			
				}
			
				 new ProcesarEntradaDeDiario();
				 ProcesarEntradaDeDiario.procesarSql = false;
				 ProcesarEntradaDeDiario.monedaComprobante = dnc.getMoneda() ;
				 ProcesarEntradaDeDiario.monedaLocal = monedabase;
				 ProcesarEntradaDeDiario.fecharecibo = fechadoc;
				 ProcesarEntradaDeDiario.tasaCambioParalela = tasaoficial; 
				 ProcesarEntradaDeDiario.tasaCambioOficial = tasaoficial; 
				 ProcesarEntradaDeDiario.montoComprobante = totaldonacion;
				 ProcesarEntradaDeDiario.tipoDocumento = "P9";
				 ProcesarEntradaDeDiario.conceptoComprobante = concepto ;
				 ProcesarEntradaDeDiario.numeroBatchJde = String.valueOf( nobatch );
				 ProcesarEntradaDeDiario.numeroReciboJde = String.valueOf( nodocjde ) ;
				 ProcesarEntradaDeDiario.usuario = vaut.getId().getLogin();
				 ProcesarEntradaDeDiario.codigousuario = vaut.getId().getCodreg();
				 ProcesarEntradaDeDiario.programaActualiza = PropertiesSystem.CONTEXT_NAME;
				 ProcesarEntradaDeDiario.lineasComprobante = lineasComprobante;
				 ProcesarEntradaDeDiario.tipodebatch = CodigosJDE1.RECIBOCONTADO; 
				 ProcesarEntradaDeDiario.estadobatch = CodigosJDE1.BATCH_ESTADO_PENDIENTE ;
				 ProcesarEntradaDeDiario.procesarEntradaDeDiario(session);
				 msgProceso = ProcesarEntradaDeDiario.msgProceso;	
				
				 hecho = msgProceso.isEmpty();
				 if(!hecho){
					 return false;
				 }
				 
				 List<String[]> lstQueriesInsert = ProcesarEntradaDeDiario.lstSqlsInserts;
				 if( lstQueriesInsert == null || lstQueriesInsert.isEmpty() ){
					 msgProceso = "No se han generado datos para batch por donacion ";
					 return hecho = false;
				 }
				 
				 for (String[] querys : lstQueriesInsert ) {
						
						try {
							LogCajaService.CreateLog("grabarDonacionEnJDE - " + querys[1], "QRY", querys[0]);
							
							int rows = session.createSQLQuery(querys[0]).executeUpdate() ;

						} catch (Exception e) {
							LogCajaService.CreateLog("grabarDonacionEnJDE - " + querys[1], "ERR", e.getMessage());
							e.printStackTrace(); 
							msgProceso = "fallo en interfaz Edwards "+ querys[1] ;
							return hecho = false;
						}
					}
				 
				 
				 
		 
		} catch (Exception e) {
			LogCajaService.CreateLog("grabarDonacionEnJDE", "ERR", e.getMessage());
			e.printStackTrace();  
			hecho = false;
		}finally{
			
			hecho = ( msgProceso == null || msgProceso.isEmpty() ) ;
			
		}
		return hecho;
	}
	
	@SuppressWarnings("unchecked")
	public static boolean grabarDonacionEnJDE( List<DncIngresoDonacion> donacionesxbnfxmon, BigDecimal totaldonacion,Vautoriz vaut, String monedabase ){
		boolean hecho = true;
		
		Vf0901 vfBnfCuenta = null;
		DncBeneficiarioCuentas dncBnfCtaCfg ;
		Date fechadoc = new Date();
		BigDecimal tasaoficial = BigDecimal.ONE;
		
		try {
			
			msgProceso =  "" ;
			
			String strSqlTcambio = "select * from "+PropertiesSystem.ESQUEMA
					+".tcambio where cxeft = '"+new SimpleDateFormat("yyyy-MM-dd").format(fechadoc)
					+"' and cxcrcd = 'COR'" ;
			
			List<Tcambio>tasas = (ArrayList<Tcambio>)ConsolidadoDepositosBcoCtrl.executeSqlQuery( strSqlTcambio, true, Tcambio.class );
			if(tasas == null || tasas.isEmpty() ){
				msgProceso = "tasa de cambio oficial no configurada ";
				return false;
			}
			tasaoficial = tasas.get(0).getId().getCxcrrd();
			
			String strQueryExecute;
			
			String strSqlCtaBnf = "select * from " + PropertiesSystem.ESQUEMA +
					".dnc_beneficiario_cuentas  where idbeneficiario = @BNF_ID " +
					"and codigobnf = @BNF_CODIGO and moneda = '@MONEDA' " +
					"and trim(codcomp) = '@CODCOMP' fetch first rows only ";
			
			String strSqlF0901 = " select * from "+ PropertiesSystem.ESQUEMA +
					".Vf0901 where trim(gmmcu) = '@GMMCU' and trim( gmobj ) = '@GMOBJ' " +
					"and trim(gmsub) = '@GMSUB' and trim(gmpec) <> 'N' fetch first rows only ";
			
			String strSqlCtaformaPago = 
			" select  c1mcu, c1obj, c1sub, "+ 
			"ifnull( (select gmaid  from "+PropertiesSystem.JDEDTA+".F0901" +
				" where trim(gmmcu) = trim(c1mcu) and trim(gmobj) = trim(c1obj) " +
				"and trim(gmsub) = trim(c1sub) ), 0 ),"+ 
			"( trim(c1mcu)||'.'|| trim(c1obj)||'.'||trim(c1sub)) ,"+ 
			"( case when length(trim(c1mcu) ) = 4 then substr(trim(c1mcu), 1, 2) else trim(c1mcu) end )"+ 
			"from "+PropertiesSystem.ESQUEMA+".f55ca011 "+ 
			"where c1id = @CAID and trim(c1rp01) = '@CODCOMP' and c1crcd = '@MONEDA' " +
			"and c1ryin ='@MPAGO' and c1stat = 'A' "; 
			
			DncIngresoDonacion dnc = donacionesxbnfxmon.get(0);
			
			strQueryExecute = 
					strSqlCtaBnf.replace("@BNF_ID", String.valueOf( dnc.getIdbenficiario() ) )
					.replace("@BNF_CODIGO", String.valueOf( dnc.getCodigo() ) )
					.replace("@MONEDA", dnc.getMoneda() )
					.replace("@CODCOMP", codcomp);
				
			List<DncBeneficiarioCuentas> bnf_cta = (ArrayList<DncBeneficiarioCuentas>)
					ConsolidadoDepositosBcoCtrl.executeSqlQuery(strQueryExecute, true, DncBeneficiarioCuentas.class );
			
				
			if(bnf_cta == null ||  bnf_cta.isEmpty() ){
				msgProceso = "No hay cuenta contable configurada para el beneficiario  " + dnc.getNombrecorto() ;
				return false;
			}
				
			dncBnfCtaCfg = bnf_cta.get(0);
			
			strQueryExecute = 
				strSqlF0901.replace("@GMMCU", dncBnfCtaCfg.getUnegocio().trim() )
					.replace("@GMOBJ",dncBnfCtaCfg.getObjeto().trim() )
					.replace("@GMSUB",dncBnfCtaCfg.getSubsidiaria().trim() ) ;
			
			List<Vf0901>bnf_CuentasF0901 = ( ArrayList<Vf0901>) ConsolidadoDepositosBcoCtrl.executeSqlQuery( strQueryExecute, true, Vf0901.class );
			if(bnf_CuentasF0901 == null || bnf_CuentasF0901.isEmpty()){
				msgProceso = "No se encontro en JDE cuenta contable para la configuracion de cuenta para beneficiario " + dnc.getNombrecorto() ;
				return false;
			}
			
			vfBnfCuenta = bnf_CuentasF0901.get(0);
			 
			
			String dtaCjaRec = 
					( (numrec == 0)? "": ",Rc:" + numrec) +
					( (tiporecibo.isEmpty())?"Dnc Independ.": ", Tp:" + tiporecibo );
					 
			String obsDnc = "Caja:"+ caid +  ((dtaCjaRec.isEmpty() )? "" : dtaCjaRec)   ;		
			String concepto = "Donacion: "+ dnc.getNombrecorto().toUpperCase()  ;
			
			String codsucdoc = vfBnfCuenta.getId().getGmco();
			String ctaBnfGmaid  = vfBnfCuenta.getId().getGmaid() ;
			String codbnf_aux = String.format("%08d", dnc.getCodigo() ) ;
			
			//&& =========== datos para generar el asiento contable
			List<DatosComprobanteF0911> lineasComprobante = new ArrayList<DatosComprobanteF0911>();
			
			DatosComprobanteF0911 dtaCtaBeneficiario = new DatosComprobanteF0911(
					ctaBnfGmaid,
					totaldonacion.negate(), 
					obsDnc,
					tasaoficial,
					codbnf_aux,
					"A",
					codsucdoc );
			 lineasComprobante.add(dtaCtaBeneficiario);
			
			//&& =========================== crear lineas por metodo de pago
				List<Object[]> lstDtaCtaMpago ;
				Object[] ctaMpago; 
				
				for (DncIngresoDonacion dncEvalua : donacionesxbnfxmon) {
					
					strQueryExecute = 
						strSqlCtaformaPago.replace("@MONEDA", dnc.getMoneda())
						.replace("@CODCOMP", dnc.getCodcomp().trim() )
						.replace("@MPAGO", dnc.getCodigoformapago().trim()) 
						.replace("@CAID", Integer.toString( dnc.getCaid() ) ) ;
				
					lstDtaCtaMpago = (ArrayList<Object[]> )ConsolidadoDepositosBcoCtrl.executeSqlQuery(strQueryExecute, true, null );
					
					if(lstDtaCtaMpago == null || lstDtaCtaMpago.isEmpty() ){
						msgProceso = "No se encontro la configuracion de cuenta contable para forma de pago " + dnc.getFormadepago() ;
						return false;
					}
					
					ctaMpago = lstDtaCtaMpago.get(0);
					String mpagoGmaid  = String.valueOf( ctaMpago[3] ).trim() ;
					String observacion = "Fp:" + dncEvalua.getFormadepago().trim() ;
			
					DatosComprobanteF0911 dtaCtaFormaPago = new DatosComprobanteF0911(
							mpagoGmaid,
							dncEvalua.getMontorecibido(), 
							observacion,
							tasaoficial,
							"",
							"",
							codsucdoc );
					 lineasComprobante.add(dtaCtaFormaPago);
			
				}
			
				 new ProcesarEntradaDeDiario();
				 ProcesarEntradaDeDiario.monedaComprobante = dnc.getMoneda() ;
				 ProcesarEntradaDeDiario.monedaLocal = monedabase;
				 ProcesarEntradaDeDiario.fecharecibo = fechadoc;
				 ProcesarEntradaDeDiario.tasaCambioParalela = tasaoficial; 
				 ProcesarEntradaDeDiario.tasaCambioOficial = tasaoficial; 
				 ProcesarEntradaDeDiario.montoComprobante = totaldonacion;
				 ProcesarEntradaDeDiario.tipoDocumento = "P9";
				 ProcesarEntradaDeDiario.conceptoComprobante = concepto ;
				 ProcesarEntradaDeDiario.numeroBatchJde = String.valueOf( nobatch );
				 ProcesarEntradaDeDiario.numeroReciboJde = String.valueOf( nodocjde ) ;
				 ProcesarEntradaDeDiario.usuario = vaut.getId().getLogin();
				 ProcesarEntradaDeDiario.codigousuario = vaut.getId().getCodreg();
				 ProcesarEntradaDeDiario.programaActualiza = PropertiesSystem.CONTEXT_NAME;
				 ProcesarEntradaDeDiario.lineasComprobante = lineasComprobante;
				 ProcesarEntradaDeDiario.tipodebatch = CodigosJDE1.RECIBOCONTADO; 
				 ProcesarEntradaDeDiario.estadobatch = CodigosJDE1.BATCH_ESTADO_PENDIENTE ;
				 ProcesarEntradaDeDiario.procesarEntradaDeDiario(null);
				 msgProceso = ProcesarEntradaDeDiario.msgProceso;	
				 hecho = msgProceso.isEmpty();	
				
				 if(!hecho){
					 return false;
				 }
				 
				
			
		} catch (Exception e) {
			e.printStackTrace(); 
			msgProceso = "Error de proceso interno " ;
			hecho = false;
		}finally{
			
			hecho = ( msgProceso == null || msgProceso.isEmpty() ) ;
			
		}
		return hecho;
	}
	
	
	//**********************************************************
	//----------------------------------------------------------
	//++++++++++++++++++      INICIO     +++++++++++++++++++++++
	//----------------------------------------------------------
	//**********************************************************
	//Creado por Luis Alberto Fonseca Mendez 2019-04-24
	@SuppressWarnings("unchecked")
	public static boolean grabarDonacionEnJDE(Session sesion, Transaction trans, List<DncIngresoDonacion> donacionesxbnfxmon, BigDecimal totaldonacion,Vautoriz vaut, String monedabase ){
		boolean hecho = true;
		
		Vf0901 vfBnfCuenta = null;
		DncBeneficiarioCuentas dncBnfCtaCfg ;
		Date fechadoc = new Date();
		BigDecimal tasaoficial = BigDecimal.ONE;
		
		try {
			
			msgProceso =  "" ;
			
			String strSqlTcambio = "select * from "+PropertiesSystem.ESQUEMA
					+".tcambio where cxeft = '"+new SimpleDateFormat("yyyy-MM-dd").format(fechadoc)
					+"' and cxcrcd = 'COR'" ;
			
			List<Tcambio>tasas = (ArrayList<Tcambio>)ConsolidadoDepositosBcoCtrl.executeSqlQuery( strSqlTcambio, true, Tcambio.class );
			if(tasas == null || tasas.isEmpty() ){
				msgProceso = "tasa de cambio oficial no configurada ";
				return false;
			}
			tasaoficial = tasas.get(0).getId().getCxcrrd();
			
			String strQueryExecute;
			
			String strSqlCtaBnf = "select * from " + PropertiesSystem.ESQUEMA +
					".dnc_beneficiario_cuentas  where idbeneficiario = @BNF_ID " +
					"and codigobnf = @BNF_CODIGO and moneda = '@MONEDA' " +
					"and trim(codcomp) = '@CODCOMP' fetch first rows only ";
			
			String strSqlF0901 = " select * from "+ PropertiesSystem.ESQUEMA +
					".Vf0901 where trim(gmmcu) = '@GMMCU' and trim( gmobj ) = '@GMOBJ' " +
					"and trim(gmsub) = '@GMSUB' and trim(gmpec) <> 'N' fetch first rows only ";
			
			String strSqlCtaformaPago = 
			" select  c1mcu, c1obj, c1sub, "+ 
			"ifnull( (select gmaid  from "+PropertiesSystem.JDEDTA+".F0901" +
				" where trim(gmmcu) = trim(c1mcu) and trim(gmobj) = trim(c1obj) " +
				"and trim(gmsub) = trim(c1sub) ), 0 ),"+ 
			"( trim(c1mcu)||'.'|| trim(c1obj)||'.'||trim(c1sub)) ,"+ 
			"( case when length(trim(c1mcu) ) = 4 then substr(trim(c1mcu), 1, 2) else trim(c1mcu) end )"+ 
			"from "+PropertiesSystem.ESQUEMA+".f55ca011 "+ 
			"where c1id = @CAID and trim(c1rp01) = '@CODCOMP' and c1crcd = '@MONEDA' " +
			"and c1ryin ='@MPAGO' and c1stat = 'A' "; 
			
			DncIngresoDonacion dnc = donacionesxbnfxmon.get(0);
			
			strQueryExecute = 
					strSqlCtaBnf.replace("@BNF_ID", String.valueOf( dnc.getIdbenficiario() ) )
					.replace("@BNF_CODIGO", String.valueOf( dnc.getCodigo() ) )
					.replace("@MONEDA", dnc.getMoneda() )
					.replace("@CODCOMP", codcomp);
				
			List<DncBeneficiarioCuentas> bnf_cta = (ArrayList<DncBeneficiarioCuentas>)
					ConsolidadoDepositosBcoCtrl.executeSqlQuery(strQueryExecute, true, DncBeneficiarioCuentas.class );
			
				
			if(bnf_cta == null ||  bnf_cta.isEmpty() ){
				msgProceso = "No hay cuenta contable configurada para el beneficiario  " + dnc.getNombrecorto() ;
				return false;
			}
				
			dncBnfCtaCfg = bnf_cta.get(0);
			
			strQueryExecute = 
				strSqlF0901.replace("@GMMCU", dncBnfCtaCfg.getUnegocio().trim() )
					.replace("@GMOBJ",dncBnfCtaCfg.getObjeto().trim() )
					.replace("@GMSUB",dncBnfCtaCfg.getSubsidiaria().trim() ) ;
			
			List<Vf0901>bnf_CuentasF0901 = ( ArrayList<Vf0901>) ConsolidadoDepositosBcoCtrl.executeSqlQuery( strQueryExecute, true, Vf0901.class );
			if(bnf_CuentasF0901 == null || bnf_CuentasF0901.isEmpty()){
				msgProceso = "No se encontro en JDE cuenta contable para la configuracion de cuenta para beneficiario " + dnc.getNombrecorto() ;
				return false;
			}
			
			vfBnfCuenta = bnf_CuentasF0901.get(0);
			 
			
			String dtaCjaRec = 
					( (numrec == 0)? "": ",Rc:" + numrec) +
					( (tiporecibo.isEmpty())?"Dnc Independ.": ", Tp:" + tiporecibo );
					 
			String obsDnc = "Caja:"+ caid +  ((dtaCjaRec.isEmpty() )? "" : dtaCjaRec)   ;		
			String concepto = "Donacion: "+ dnc.getNombrecorto().toUpperCase()  ;
			
			String codsucdoc = vfBnfCuenta.getId().getGmco();
			String ctaBnfGmaid  = vfBnfCuenta.getId().getGmaid() ;
			String codbnf_aux = String.format("%08d", dnc.getCodigo() ) ;
			
			//&& =========== datos para generar el asiento contable
			List<DatosComprobanteF0911> lineasComprobante = new ArrayList<DatosComprobanteF0911>();
			
			DatosComprobanteF0911 dtaCtaBeneficiario = new DatosComprobanteF0911(
					ctaBnfGmaid,
					totaldonacion.negate(), 
					obsDnc,
					tasaoficial,
					codbnf_aux,
					"A",
					codsucdoc );
			 lineasComprobante.add(dtaCtaBeneficiario);
			
			//&& =========================== crear lineas por metodo de pago
				List<Object[]> lstDtaCtaMpago ;
				Object[] ctaMpago; 
				
				for (DncIngresoDonacion dncEvalua : donacionesxbnfxmon) {
					
					strQueryExecute = 
						strSqlCtaformaPago.replace("@MONEDA", dnc.getMoneda())
						.replace("@CODCOMP", dnc.getCodcomp().trim() )
						.replace("@MPAGO", dnc.getCodigoformapago().trim()) 
						.replace("@CAID", Integer.toString( dnc.getCaid() ) ) ;
				
					lstDtaCtaMpago = (ArrayList<Object[]> )ConsolidadoDepositosBcoCtrl.executeSqlQuery(strQueryExecute, true, null );
					
					if(lstDtaCtaMpago == null || lstDtaCtaMpago.isEmpty() ){
						msgProceso = "No se encontro la configuracion de cuenta contable para forma de pago " + dnc.getFormadepago() ;
						return false;
					}
					
					ctaMpago = lstDtaCtaMpago.get(0);
					String mpagoGmaid  = String.valueOf( ctaMpago[3] ).trim() ;
					String observacion = "Fp:" + dncEvalua.getFormadepago().trim() ;
			
					DatosComprobanteF0911 dtaCtaFormaPago = new DatosComprobanteF0911(
							mpagoGmaid,
							dncEvalua.getMontorecibido(), 
							observacion,
							tasaoficial,
							"",
							"",
							codsucdoc );
					 lineasComprobante.add(dtaCtaFormaPago);
			
				}
			
				 new ProcesarEntradaDeDiario();
				 ProcesarEntradaDeDiario.monedaComprobante = dnc.getMoneda() ;
				 ProcesarEntradaDeDiario.monedaLocal = monedabase;
				 ProcesarEntradaDeDiario.fecharecibo = fechadoc;
				 ProcesarEntradaDeDiario.tasaCambioParalela = tasaoficial; 
				 ProcesarEntradaDeDiario.tasaCambioOficial = tasaoficial; 
				 ProcesarEntradaDeDiario.montoComprobante = totaldonacion;
				 ProcesarEntradaDeDiario.tipoDocumento = "P9";
				 ProcesarEntradaDeDiario.conceptoComprobante = concepto ;
				 ProcesarEntradaDeDiario.numeroBatchJde = String.valueOf( nobatch );
				 ProcesarEntradaDeDiario.numeroReciboJde = String.valueOf( nodocjde ) ;
				 ProcesarEntradaDeDiario.usuario = vaut.getId().getLogin();
				 ProcesarEntradaDeDiario.codigousuario = vaut.getId().getCodreg();
				 ProcesarEntradaDeDiario.programaActualiza = PropertiesSystem.CONTEXT_NAME;
				 ProcesarEntradaDeDiario.lineasComprobante = lineasComprobante;
				 ProcesarEntradaDeDiario.tipodebatch = CodigosJDE1.RECIBOCONTADO; 
				 ProcesarEntradaDeDiario.estadobatch = CodigosJDE1.BATCH_ESTADO_PENDIENTE ;
				 
				 //cambio hecho por lfonseca
//				 ProcesarEntradaDeDiario.procesarEntradaDeDiario();
				 ProcesarEntradaDeDiario.procesarEntradaDeDiario(sesion, trans);
				 msgProceso = ProcesarEntradaDeDiario.msgProceso;	
				 hecho = msgProceso.isEmpty();	
				
				 if(!hecho){
					 return false;
				 }
				 
				
			
		} catch (Exception e) {
			e.printStackTrace(); 
			msgProceso = "Error de proceso interno " ;
			hecho = false;
		}finally{
			
			hecho = ( msgProceso == null || msgProceso.isEmpty() ) ;
			
		}
		return hecho;
	}
	
	//**********************************************************
	//----------------------------------------------------------
	//++++++++++++++++++       FIN       +++++++++++++++++++++++
	//----------------------------------------------------------
	//**********************************************************
	
	
	

	public static String getMsgProceso() {
		if(msgProceso == null)
			msgProceso = "" ;
		return msgProceso;
	}
	public static void setMsgProceso(String msgProceso) {
		DonacionesCtrl.msgProceso = msgProceso;
	}
	
	public static <E> List<?> obtenerBeneficiariosConfigurados(boolean combolist, boolean todos){
		List<?> lstObjectReturn = null;
		
		try {
			
			String strQuery = "select * from "+PropertiesSystem.ESQUEMA+".dnc_beneficiario where estado = 1 "; 
			
			if(todos){
				strQuery = "select * from "+PropertiesSystem.ESQUEMA+".dnc_beneficiario ";
			}
			
			
			@SuppressWarnings("unchecked")
			List<DncBeneficiario> dnc_bnf =  (List<DncBeneficiario>) ConsolidadoDepositosBcoCtrl
							.executeSqlQuery( strQuery, true, DncBeneficiario.class );
			
			
			if(dnc_bnf == null || dnc_bnf.isEmpty())
				return lstObjectReturn = new ArrayList<E>();
			
			
			if( combolist ){
				List<SelectItem> si = new ArrayList<SelectItem>();
				
				for (DncBeneficiario bnf : dnc_bnf) {
					si.add(new SelectItem(
						String.valueOf(bnf.getIdbeneficiario()) + "@" + String.valueOf( bnf.getCodigo() ), 
						bnf.getNombrecorto().toLowerCase(), bnf.getNombre()+" <> "+bnf.getDescripcion())) ;
				}
				
				return si;
			}
			
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return lstObjectReturn;
	}

	public static List<Vf55ca012> obtenerMpagosConfiguradosCaja(String codcomp, int caid){
		List<Vf55ca012>metodospago = null;
		try {
			
			//&& ================ Obtener la lista de metodos de pago configurados para la caja y compania.
			String sql = " select * "
			 		+" from "+PropertiesSystem.ESQUEMA+".VF55ca012 as f where f.c2id = " + caid
					+ " and trim(f.c2rp01) = '" + codcomp.trim()
					+ "' and f.c2stat = 'A'";
			
			LogCajaService.CreateLog("obtenerMpagosConfiguradosCaja", "QRY", sql);
			
			 metodospago =  ConsolidadoDepositosBcoCtrl.executeSqlQuery( sql, Vf55ca012.class, true );
			 
		} catch (Exception e) {			
			LogCajaService.CreateLog("obtenerMpagosConfiguradosCaja", "ERR", e.getMessage());
		}finally{
			if( metodospago == null)
				metodospago  = new ArrayList<Vf55ca012>();
		}
		return metodospago;
	}

	public static int getIddncrsm() {
		return iddncrsm;
	}

	public static void setIddncrsm(int iddncrsm) {
		DonacionesCtrl.iddncrsm = iddncrsm;
	}
	
	
}

package com.casapellas.controles;
/**
 * CASA PELLAS S.A.
 * Creado por.........: Juan Carlos Ñamendi Pineda
 * Fecha de Creación..: 21/12/2009
 * Última modificación: 17/03/2011
 * Modificado por.....:	Juan Carlos Ñamendi Pineda
 * 
 */
import java.math.BigDecimal;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
//import org.apache.commons.mail.MultiPartEmail;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.casapellas.entidades.Credhdr;
import com.casapellas.entidades.Elementofin;
import com.casapellas.entidades.F55ca014;
import com.casapellas.entidades.FacturaxRecibo;
import com.casapellas.entidades.Finandet;
import com.casapellas.entidades.FinandetId;
import com.casapellas.entidades.Finanhdr;
import com.casapellas.entidades.Hfinan;
import com.casapellas.entidades.Producto;
import com.casapellas.entidades.Recibofac;
import com.casapellas.entidades.RecibofacId;
import com.casapellas.entidades.Vf0101;
import com.casapellas.entidades.Vf0311fn;
import com.casapellas.entidades.Vmora;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.hibernate.util.RoQueryManager;
import com.casapellas.jde.creditos.CodigosJDE1;
import com.casapellas.jde.creditos.DatosComprobanteF0911;
import com.casapellas.jde.creditos.ProcesarNuevaFacturaF03B11;
import com.casapellas.navegacion.As400Connection;
import com.casapellas.util.CalendarToJulian;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.CustomEmailAddress;
import com.casapellas.util.Divisas;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.JulianToCalendar;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.MailHelper;
import com.casapellas.util.PropertiesSystem;

public class CuotaCtrl {
	
	
	
	public static List<String[]> idCuentaLibroMayorUC(String sucursal ){
		List< String[] > strIdCuenta = new ArrayList<String[]>();
		
		try{
			
			sucursal = CodeUtil.pad(sucursal.trim(), 5, "0");
			
			String strSql = 
					
			" SELECT "+   
			" (select gmaid from @JDEDTA.f0901 where "+ 
			" 	trim(gmmcu) = (case when trim(kgmcu) = '' then '"+Integer.parseInt( sucursal ) +"' else trim(kgmcu) end )  "+       
			" 	and trim(gmobj) = trim(kgobj) "+ 
			" 	and trim(gmsub) = trim(kgsub) "+ 
			" ) || '@@' ||  "+ 
			
			" trim(kgitem) || '@@' || "+ 
			
			" ( case when kgco = '00000' then '@KGCO'  else kgco end ) "+ 
			 
			" from @JDEDTA.f0012 ica "+ 
			 
			" where  kgco <> '' and ( kgco in ( '@KGCO', '00000') and upper( kgitem ) = '@KGITEM' )  " +
			 
			" order by kgco desc ";
			
			strSql = strSql
				.replace("@JDEDTA", PropertiesSystem.JDEDTA)
				.replace("@KGCO", sucursal)
				.replace("@KGITEM", CodigosJDE1.ICA_RU_ITEM_NUMBER.codigo() );
			
			LogCajaService.CreateLog("idCuentaLibroMayorUC", "QRY", strSql);
			 
			List<String> cuentas = ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSql, null, true);
			
			if(cuentas == null || cuentas.isEmpty() )
				return null;
						
			for (String idcuenta : cuentas) {
				strIdCuenta.add(  idcuenta.split("@@") ) ;
			}	
			
		} catch (Exception e) {
			LogCajaService.CreateLog("idCuentaLibroMayorUC", "ERR", e.getMessage());
			e.printStackTrace();
			return null;
		}
		return strIdCuenta;
	}
	
	public static List< String[] > idCuentaFacturaTipoClienteSucursal( List<String> codigosSucursal, String claseContable, String prefijo ){
		 List< String[] > strIdCuenta = new ArrayList<String[]>();
		
		try {
			
			for (int i = 0; i < codigosSucursal.size(); i++) {
				codigosSucursal.set(i, CodeUtil.pad(codigosSucursal.get(i).trim(), 5, "0") ) ;
			}
			
			String strSql = 
					
				" SELECT "+   
				" (select gmaid from @JDEDTA.f0901 where "+ 
				" 	trim(gmmcu) = (case when trim(kgmcu) = '' then '"+Integer.parseInt( codigosSucursal.get(0) ) +"' else trim(kgmcu) end )  "+       
				" 	and trim(gmobj) = trim(kgobj) "+ 
				" 	and trim(gmsub) = trim(kgsub) "+ 
				" ) || '@@' ||  "+ 
				
				" trim(kgitem) || '@@' || "+ 
				
				" ( case when kgco = '00000' then  '"+ codigosSucursal.get(0)  + "'  else kgco end ) "+ 
				 
				" from @JDEDTA.f0012 ica "+ 
				 
				" where  kgco <> '' and @DTASUCURSAL" +
				 
				" order by kgco desc ";
		
			List<String> sucursalClaseContable = new ArrayList<String>(); 
			for (String codigoSuc : codigosSucursal) {
				sucursalClaseContable.add( codigoSuc + "<>" + prefijo.toUpperCase() +""+ claseContable.toUpperCase()  );
			}
			
			String strSqlOr = "";
			
			Set<String> sucxClaseContable = new HashSet<String>(sucursalClaseContable);
			for (String dtaSucClaseCtbl : sucxClaseContable) {
			 
				String[] dtaSuc = dtaSucClaseCtbl.split("<>");
				
				strSqlOr += " (  kgco in ( '"+dtaSuc[0]+"', '00000') and upper( kgitem ) = '"+dtaSuc[1]+"' ) or "  ;
				
			}
			
			strSqlOr = strSqlOr.substring(0, strSqlOr.lastIndexOf("or")) ;
		 
			strSql = strSql.replace("@DTASUCURSAL", strSqlOr).replace("@JDEDTA", PropertiesSystem.JDEDTA);
			 
			List<String> cuentas = ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSql, null, true);
			
			if(cuentas == null || cuentas.isEmpty() )
				return null;
						
			for (String idcuenta : cuentas) {
				if(idcuenta!=null)
				strIdCuenta.add(  idcuenta.split("@@") ) ;
			}			
			
		} catch (Exception e) {
			LogCajaService.CreateLog("idCuentaFacturaTipoClienteSucursal", "ERR", e.getMessage());
			return null;
		}
		return strIdCuenta;
	}
	
	
	public static List<String[] > idCuentaContablePagoFactura(List<Finandet> cuotas){
		 List< String[] > strIdCuenta = new ArrayList<String[]>();
		
		try {
			
			String strSql = 
					
				" SELECT "+   
				" (select gmaid from @JDEDTA.f0901 where "+ 
				" 	trim(gmmcu) = (case when trim(kgmcu) = '' then '"+Integer.parseInt( cuotas.get(0).getId().getCodsuc() ) +"' else trim(kgmcu) end )  "+       
				" 	and trim(gmobj) = trim(kgobj) "+ 
				" 	and trim(gmsub) = trim(kgsub) "+ 
				" ) || '@@' ||  "+ 
				
				" trim(kgitem) || '@@' || "+ 
				
				" ( case when kgco = '00000' then  '"+ cuotas.get(0).getId().getCodsuc()  + "'  else kgco end ) "+ 
				 
				" from @JDEDTA.f0012 ica "+ 
				 
				" where  kgco <> '' and @DTASUCURSAL" +
				 
				" order by kgco desc ";
		
			List<String> sucursalClaseContable = new ArrayList<String>(); 
			for (Finandet cuota : cuotas) {
				sucursalClaseContable.add( cuota.getId().getCodsuc() + "<>RC" + cuota.getId().getClasecontable().trim().toUpperCase()  );
			}
			
			String strSqlOr = "";
			
			Set<String> sucxClaseContable = new HashSet<String>(sucursalClaseContable);
			for (String dtaSucClaseCtbl : sucxClaseContable) {
			 
				String[] dtaSuc = dtaSucClaseCtbl.split("<>");
				
				strSqlOr += " (  kgco in ( '"+dtaSuc[0]+"', '00000') and upper( kgitem ) = '"+dtaSuc[1]+"' ) or "  ;
				
			}
			
			strSqlOr = strSqlOr.substring(0, strSqlOr.lastIndexOf("or")) ;
		 
			strSql = strSql.replace("@DTASUCURSAL", strSqlOr).replace("@JDEDTA", PropertiesSystem.JDEDTA);
			
			List<String> cuentas = ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSql, null, true);
			
			if(cuentas == null || cuentas.isEmpty() )
				return null;
						
			for (String idcuenta : cuentas) {
				if(idcuenta!=null)
				strIdCuenta.add(  idcuenta.split("@@") ) ;
			}			
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return strIdCuenta;
	}
	
	
	public static String crearInteresCuota(Finanhdr finanhdr, Finandet finandet, 
			String tipoInteres, int numeroCuota,  int numeroPrestamo, 
			BigDecimal montoInteres, BigDecimal tasaoficial,
			String usuario,  String monedaNacional, String idCuentaContableFactura,
			List<String[]> cuentaIntereses,int codigoClientePadre, String codigoCategoria08 ){
		
		String tipoCuentaIntereses = "" ;
		String conceptoInteres = "" ;
		
		String msgProceso = "";
		
		try {
			
			CodigosJDE1 NUM_TIPO_INTERES_SUCURSAL = null;
			CodigosJDE1 NN_TIPO_INTERES = null ;

			if(tipoInteres.compareTo("IF") == 0){
				
				tipoCuentaIntereses = "FI00";
				NUM_TIPO_INTERES_SUCURSAL = CodigosJDE1.NUM_DOC_CO_INTERRES_CORRIENTE;
				NN_TIPO_INTERES = CodigosJDE1.NUMERO_DOC_INTERES_CORRIENTE;
				
				conceptoInteres = "C:Int+Imp,Doc:"+finanhdr.getId().getNosol()+" Cuot:" + numeroCuota  ;
				
			}
			
			if(tipoInteres.compareTo("MF") == 0){
				
				tipoCuentaIntereses = "FI05";
				NUM_TIPO_INTERES_SUCURSAL = CodigosJDE1.NUM_DOC_CO_INTERRES_MORATORIO; 
				NN_TIPO_INTERES = CodigosJDE1.NUMERO_DOC_INTERES_MORATORIO;
				
				conceptoInteres = "C:IntMorat,Doc"+finanhdr.getId().getNosol()+" Cuot:" + numeroCuota  ;
			}
			
			//&& ====== Crear la partida contable.
			//int numeroBatchJde = Divisas.numeroSiguienteJdeE1(CodigosJDE1.NUMEROBATCH );
			int numeroBatchJde = Divisas.numeroSiguienteJdeE1(  );
			int numeroDocumentoInteres = Divisas.numeroSiguienteJde(NUM_TIPO_INTERES_SUCURSAL, finanhdr.getId().getCodsuc());
			
			if( numeroDocumentoInteres == 0 ){
				numeroDocumentoInteres = Divisas.numeroSiguienteJdeE1(NN_TIPO_INTERES);
			} 
			
			if ( numeroBatchJde == 0 ) {
				return msgProceso = " No es posible  crear interés " + tipoInteres + " para cuota " + numeroCuota + ", no se ha podido generar numero de batch ";
			}
			if(numeroDocumentoInteres == 0) {
				return msgProceso = " No es posible  crear interés " + tipoInteres + " para cuota " + numeroCuota + ", no se ha podido generar numero de documento ";
			} 
			
			DatosComprobanteF0911 dtaCuentaInteres = new DatosComprobanteF0911(
					cuentaIntereses.get(0)[1],
					montoInteres.negate(), 
					conceptoInteres,
					tasaoficial,
					"",
					"",
					cuentaIntereses.get(0)[2]);
			
			List<DatosComprobanteF0911> lineasComprobante = new ArrayList<DatosComprobanteF0911>();
			lineasComprobante.add(dtaCuentaInteres);

			 
			//&& ======================== Crear el registro de la factura en F03B11
			String uninegOriginal = Integer.parseInt( finanhdr.getId().getCodsuc() ) + finanhdr.getId().getLinea() ;
			String codunineg =  CompaniaCtrl.leerUnidadNegocioPorLineaSucursal( finanhdr.getId().getCodsuc(), "52");
			if(codunineg.isEmpty()){
				codunineg = uninegOriginal;
			}
			
			BigDecimal cuotaInteres = BigDecimal.ZERO;
			BigDecimal cuotaImpuesto = BigDecimal.ZERO;
			
			if(tipoInteres.compareTo("IF") == 0){
				
				cuotaInteres = finandet.getId().getInteres();
				cuotaImpuesto = finandet.getId().getImpuesto(); 
				
				finandet.setNumeroBatchIF(String.valueOf( numeroBatchJde ) );
				
			}
			if(tipoInteres.compareTo("MF") == 0){
			
				cuotaInteres = finandet.getId().getMora();
				cuotaImpuesto = finandet.getId().getMora();
				
				finandet.setNumeroBatchMF(String.valueOf( numeroBatchJde ) );
				finandet.setDiasVencidos(  String.valueOf( finandet.getId().getDiasven() + finandet.getId().getDiasmora() ) ) ;
			 
			}
			
			new ProcesarNuevaFacturaF03B11();
			ProcesarNuevaFacturaF03B11.nombrecliente  = finanhdr.getId().getNomcli().trim();
			ProcesarNuevaFacturaF03B11.tipoSolicitud  = finanhdr.getId().getTiposol();       
			ProcesarNuevaFacturaF03B11.numeroSolicitud = String.valueOf( finanhdr.getId().getNosol() );
			ProcesarNuevaFacturaF03B11.numeroCuota  = String.valueOf(numeroCuota);
			ProcesarNuevaFacturaF03B11.tipoInteres  = tipoInteres;
			ProcesarNuevaFacturaF03B11.moneda  = finandet.getId().getMoneda();
			ProcesarNuevaFacturaF03B11.sucursal  = finanhdr.getId().getCodsuc();
			ProcesarNuevaFacturaF03B11.unidadNegocio1  = codunineg;
			ProcesarNuevaFacturaF03B11.unidadNegocio2  = uninegOriginal;
			ProcesarNuevaFacturaF03B11.usuario  = usuario;
			ProcesarNuevaFacturaF03B11.tipoBatch  = CodigosJDE1.BATCH_FINANCIMIENTO.codigo();
			ProcesarNuevaFacturaF03B11.tipoImpuesto  = finanhdr.getId().getAttxa1().trim();
			ProcesarNuevaFacturaF03B11.fechavencimiento = String.valueOf( finandet.getId().getAtpayd() ) ;
			ProcesarNuevaFacturaF03B11.monedaLocal = monedaNacional;
			ProcesarNuevaFacturaF03B11.montoFactura  = montoInteres ;
			ProcesarNuevaFacturaF03B11.tasaCambio  = tasaoficial;
			ProcesarNuevaFacturaF03B11.montoInteres  = cuotaInteres;
			ProcesarNuevaFacturaF03B11.montoImpuesto  = cuotaImpuesto;
			 
			ProcesarNuevaFacturaF03B11.codigoCliente  = finanhdr.getId().getCodcli() ;
			ProcesarNuevaFacturaF03B11.codigoClientePadre = codigoClientePadre;
			ProcesarNuevaFacturaF03B11.numerobatch  = numeroBatchJde;
			ProcesarNuevaFacturaF03B11.numeroFactura  = numeroDocumentoInteres;
			ProcesarNuevaFacturaF03B11.concepto  = conceptoInteres;
			ProcesarNuevaFacturaF03B11.lineasComprobante = lineasComprobante;
			ProcesarNuevaFacturaF03B11.claseContableCliente = finandet.getId().getClasecontable();
			ProcesarNuevaFacturaF03B11.idCuentaContableFactura = idCuentaContableFactura;
			ProcesarNuevaFacturaF03B11.codigoCategoria08 = codigoCategoria08;
			
			ProcesarNuevaFacturaF03B11.tipoCuentaIntereses = tipoCuentaIntereses; 
			
			
			ProcesarNuevaFacturaF03B11.fecha = new Date();
			
			ProcesarNuevaFacturaF03B11.procesarNuevaFactura(null);
			
			msgProceso = ProcesarNuevaFacturaF03B11.strMensajeProceso;
			
			
		} catch (Exception e) {
			
			msgProceso = " error al procesar el registro de nueva factura  " 
					+ tipoInteres +", Cuota " 
					+numeroCuota + ", solicitud " 
					+ numeroPrestamo ;
			
			e.printStackTrace(); 
		} 
		return msgProceso ;
	}
	
/********************************************************************************/
	public void validarSiestanPagadas(List lstFacturasSelected){
		Connection cn = null;
		PreparedStatement ps = null;
		String sql = "";	
		Finandet fd = null;
		int iCpendiente = 0, iDpendiente= 0;
		try{
			As400Connection as400connection = new As400Connection();
			cn = as400connection.getJNDIConnection("DSMCAJA2");
			for(int i = 0;i < lstFacturasSelected.size();i++){
				fd = (Finandet)lstFacturasSelected.get(i);
				sql = "SELECT RPAAP, RPFAP " +
						" "+PropertiesSystem.JDEDTA+".f03B11 INNER JOIN "+PropertiesSystem.JDEDTA+".F0006 UN ON RPMCU = UN.MCMCU " +
						" where " + 
						" rpan8 = "+fd.getId().getCodcli()+" and rpaap > 0 and rpdct not in (select COD_DOCUMENTO from " + PropertiesSystem.GCPCXC + ".TIPOS_DOCUMENTOS_FINANCIAMIENTO "
								+ "where TIPO_AGRUPACION in ('IMO', 'ICR') and cod_compania = CAST(TRIM(UN.MCRP01) AS VARCHAR(3) CCSID 37) "
								+ "order by ORDEN_PROCESAMIENTO asc) and RPDCTO = '"+fd.getId().getTiposol()+
						"' and CAST(RPPO AS NUMERIC(8)) = "+fd.getId().getNosol()+" and CAST(RPSFX AS NUMERIC(8))  = "+ fd.getId().getNocuota();
				ps = cn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();
				
				if(rs.next()){
					iCpendiente = rs.getInt("RPAAP");
					iDpendiente = rs.getInt("RPFAP");
				}else{
					
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			try {
				ps.close();
				cn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}
	}
/***********************************************************************************/
	@SuppressWarnings("unchecked")
	public List<Credhdr> leerDocumentosxCuota(Finandet fd, Finanhdr fh,F55ca014[] f14){
		List<Credhdr> lstCuotas = new ArrayList(),lstResult = null;
 
		String sql = "",sqlSol = "",sqlSuc = "", sqlNosol = "", sql1 = "", sql2 = "", sql3 = "";
		Object[] obj = null;	
		FinandetId nFdid = null;
		
		try{
			sql1 = "(SELECT CAST(TRIM(RPDCT) AS VARCHAR(2) CCSID 37) TIPOFACTURA,RPDOC AS NOFACTURA,CAST (RPSFX AS VARCHAR(3) CCSID 37) AS PARTIDA,RPAN8 AS CODCLI, RPALPH AS NOMCLI, CAST(TRIM(RPMCU) AS VARCHAR(12) CCSID 37) CODUNINEG, UN.MCDL01 AS UNINEG, CAST(RPKCO AS VARCHAR(5) CCSID 37) CODSUC,"+
				  " (SELECT MCDL01 FROM "+PropertiesSystem.JDEDTA+".F0006 WHERE SUBSTRING(RPKCO,4,5) = SUBSTRING(MCMCU,11,12) AND MCSTYL = 'BS' FETCH FIRST ROWS ONLY) AS NOMSUC,CAST(TRIM(UN.MCRP01) AS VARCHAR(3) CCSID 37) CODCOMP, CO.DRDL01 AS NOMCOMP, CAST(TRIM(RPCRCD) AS VARCHAR(3) CCSID 37) MONEDA, RPCRR AS TASA, "+
				  " CAST(RPAAP/100 AS DECIMAL (12,2)) AS CPENDIENTE,CAST(RPFAP/100 AS DECIMAL (12,2)) AS DPENDIENTE,CAST(RPAG/100 AS DECIMAL (12,2)) AS CTOTAL,CAST(RPACR/100 AS DECIMAL (12,2)) AS DTOTAL,CAST(RPSTAM/100 AS DECIMAL (12,2)) AS CIMPUESTO,CAST(RPCTAM/100 AS DECIMAL (12,2)) AS DIMPUESTO," +
				  " CAST( (RPATXA + RPATXN ) /100 AS DECIMAL (12,2)) AS CSUBTOTAL,CAST(  (RPCTXA + RPCTXN ) / 100 AS DECIMAL (12,2)) AS DSUBTOTAL, CAST((CAST(( CASE WHEN RPCRCD = 'COR' THEN RPAG ELSE RPACR END) AS DECIMAL(12,2))/100) AS DECIMAL(12,2)) MONTO,CAST((CAST(( CASE WHEN RPCRCD = 'COR' THEN RPAAP ELSE RPFAP END) AS DECIMAL(12,2))/100) AS DECIMAL(12,2)) MONTOPEND," +
				  " CAST((CAST(( CASE WHEN RPCRCD = 'COR' THEN (RPATXA + RPATXN ) ELSE ( RPCTXA + RPCTXN ) END) AS DECIMAL(12,2))/100) AS DECIMAL(12,2)) SUBTOTAL,CAST((CAST(( CASE WHEN RPCRCD = 'COR' THEN RPSTAM ELSE RPCTAM END) AS DECIMAL(12,2))/100) AS DECIMAL(12,2)) IMPUESTO,CAST ((CAST(DATE(CHAR(1900000 + RPDIVJ)) AS TIMESTAMP)) AS DATE) FECHA," +
				  " CAST ((CAST(DATE(CHAR(1900000 + RPDDJ)) AS TIMESTAMP)) AS DATE) FECHAVENC,CAST(RPGLC AS VARCHAR(4) CCSID 37) AS COMPENSLM,CAST(TRIM(RPPTC) AS VARCHAR(3) CCSID 37) TIPOPAGO, RPDIVJ,RPDDJ ,CAST (RPPO AS VARCHAR(8) CCSID 37) RPPO, CAST (RPDCTO AS VARCHAR(2) CCSID 37) RPDCTO " +
				  " from "+PropertiesSystem.JDEDTA+".F03B11 INNER JOIN "+PropertiesSystem.JDEDTA+".F0006 UN ON RPMCU = UN.MCMCU INNER JOIN "+PropertiesSystem.JDECOM+".F0005 CO ON (UN.MCRP01 = SUBSTRING(CO.DRKY,8,10) AND CO.DRSY = '00' AND CO.DRRT = '01' AND CO.DRDL02 = 'F') " +
				  "where rpaap > 0 and rpdct IN (select COD_DOCUMENTO from " + PropertiesSystem.GCPCXC + ".TIPOS_DOCUMENTOS_FINANCIAMIENTO "
				  		+ "where TIPO_AGRUPACION = 'IMO' and cod_compania = CAST(TRIM(UN.MCRP01) AS VARCHAR(3) CCSID 37) "
				  		+ "order by ORDEN_PROCESAMIENTO asc) "; 	
			
			sql2 =	  "(SELECT CAST(TRIM(RPDCT) AS VARCHAR(2) CCSID 37) TIPOFACTURA,RPDOC AS NOFACTURA,CAST (RPSFX AS VARCHAR(3) CCSID 37) AS PARTIDA,RPAN8 AS CODCLI, RPALPH AS NOMCLI, CAST(TRIM(RPMCU) AS VARCHAR(12) CCSID 37) CODUNINEG, UN.MCDL01 AS UNINEG, CAST(RPKCO AS VARCHAR(5) CCSID 37) CODSUC,"+
				  " (SELECT MCDL01 FROM "+PropertiesSystem.JDEDTA+".F0006 WHERE SUBSTRING(RPKCO,4,5) = SUBSTRING(MCMCU,11,12) AND MCSTYL = 'BS' FETCH FIRST ROWS ONLY ) AS NOMSUC,CAST(TRIM(UN.MCRP01) AS VARCHAR(3) CCSID 37) CODCOMP, CO.DRDL01 AS NOMCOMP, CAST(TRIM(RPCRCD) AS VARCHAR(3) CCSID 37) MONEDA, RPCRR AS TASA, "+
				  " CAST(RPAAP/100 AS DECIMAL (12,2)) AS CPENDIENTE,CAST(RPFAP/100 AS DECIMAL (12,2)) AS DPENDIENTE,CAST(RPAG/100 AS DECIMAL (12,2)) AS CTOTAL,CAST(RPACR/100 AS DECIMAL (12,2)) AS DTOTAL,CAST(RPSTAM/100 AS DECIMAL (12,2)) AS CIMPUESTO,CAST(RPCTAM/100 AS DECIMAL (12,2)) AS DIMPUESTO," +
				  " CAST(  (RPATXA + RPATXN )/100 AS DECIMAL (12,2)) AS CSUBTOTAL,CAST( ( RPCTXA + RPCTXN ) /100 AS DECIMAL (12,2)) AS DSUBTOTAL, CAST((CAST(( CASE WHEN RPCRCD = 'COR' THEN RPAG ELSE RPACR END) AS DECIMAL(12,2))/100) AS DECIMAL(12,2)) MONTO,CAST((CAST(( CASE WHEN RPCRCD = 'COR' THEN RPAAP ELSE RPFAP END) AS DECIMAL(12,2))/100) AS DECIMAL(12,2)) MONTOPEND," +
				  " CAST((CAST(( CASE WHEN RPCRCD = 'COR' THEN (RPATXA + RPATXN ) ELSE (RPCTXA + RPCTXN ) END) AS DECIMAL(12,2))/100) AS DECIMAL(12,2)) SUBTOTAL,CAST((CAST(( CASE WHEN RPCRCD = 'COR' THEN RPSTAM ELSE RPCTAM END) AS DECIMAL(12,2))/100) AS DECIMAL(12,2)) IMPUESTO,CAST ((CAST(DATE(CHAR(1900000 + RPDIVJ)) AS TIMESTAMP)) AS DATE) FECHA," +
				  " CAST ((CAST(DATE(CHAR(1900000 + RPDDJ)) AS TIMESTAMP)) AS DATE) FECHAVENC,CAST(RPGLC AS VARCHAR(4) CCSID 37) AS COMPENSLM,CAST(TRIM(RPPTC) AS VARCHAR(3) CCSID 37) TIPOPAGO, RPDIVJ,RPDDJ ,CAST (RPPO AS VARCHAR(8) CCSID 37) RPPO, CAST (RPDCTO AS VARCHAR(2) CCSID 37) RPDCTO " +
				  " from "+PropertiesSystem.JDEDTA+".F03B11 INNER JOIN "+PropertiesSystem.JDEDTA+".F0006 UN ON RPMCU = UN.MCMCU INNER JOIN "+PropertiesSystem.JDECOM+".F0005 CO ON (UN.MCRP01 = SUBSTRING(CO.DRKY,8,10) AND CO.DRSY = '00' AND CO.DRRT = '01' AND CO.DRDL02 = 'F') " +
				  " where rpaap > 0 and rpdct IN (select COD_DOCUMENTO from " + PropertiesSystem.GCPCXC + ".TIPOS_DOCUMENTOS_FINANCIAMIENTO "
				  		+ "where TIPO_AGRUPACION = 'ICR' and cod_compania = CAST(TRIM(UN.MCRP01) AS VARCHAR(3) CCSID 37) "
				  		+ "order by ORDEN_PROCESAMIENTO asc) ";
			
			
			sql3 =	  "(SELECT CAST(TRIM(RPDCT) AS VARCHAR(2) CCSID 37) TIPOFACTURA,RPDOC AS NOFACTURA,CAST (RPSFX AS VARCHAR(3) CCSID 37) AS PARTIDA,RPAN8 AS CODCLI, RPALPH AS NOMCLI, CAST(TRIM(RPMCU) AS VARCHAR(12) CCSID 37) CODUNINEG, UN.MCDL01 AS UNINEG, CAST(RPKCO AS VARCHAR(5) CCSID 37) CODSUC,"+
				  " (SELECT MCDL01 FROM "+PropertiesSystem.JDEDTA+".F0006 WHERE SUBSTRING(RPKCO,4,5) = SUBSTRING(MCMCU,11,12) AND MCSTYL = 'BS' FETCH FIRST ROWS ONLY) AS NOMSUC,CAST(TRIM(UN.MCRP01) AS VARCHAR(3) CCSID 37) CODCOMP, CO.DRDL01 AS NOMCOMP, CAST(TRIM(RPCRCD) AS VARCHAR(3) CCSID 37) MONEDA, RPCRR AS TASA, "+
				  " CAST(RPAAP/100 AS DECIMAL (12,2)) AS CPENDIENTE,CAST(RPFAP/100 AS DECIMAL (12,2)) AS DPENDIENTE,CAST(RPAG/100 AS DECIMAL (12,2)) AS CTOTAL,CAST(RPACR/100 AS DECIMAL (12,2)) AS DTOTAL,CAST(RPSTAM/100 AS DECIMAL (12,2)) AS CIMPUESTO,CAST(RPCTAM/100 AS DECIMAL (12,2)) AS DIMPUESTO," +
				  " CAST( (RPATXA + RPATXN )/100 AS DECIMAL (12,2)) AS CSUBTOTAL,CAST((RPCTXA + RPCTXN )/100 AS DECIMAL (12,2)) AS DSUBTOTAL, CAST((CAST(( CASE WHEN RPCRCD = 'COR' THEN RPAG ELSE RPACR END) AS DECIMAL(12,2))/100) AS DECIMAL(12,2)) MONTO,CAST((CAST(( CASE WHEN RPCRCD = 'COR' THEN RPAAP ELSE RPFAP END) AS DECIMAL(12,2))/100) AS DECIMAL(12,2)) MONTOPEND," +
				  " CAST((CAST(( CASE WHEN RPCRCD = 'COR' THEN (RPATXA + RPATXN ) ELSE (RPCTXA + RPCTXN ) END) AS DECIMAL(12,2))/100) AS DECIMAL(12,2)) SUBTOTAL,CAST((CAST(( CASE WHEN RPCRCD = 'COR' THEN RPSTAM ELSE RPCTAM END) AS DECIMAL(12,2))/100) AS DECIMAL(12,2)) IMPUESTO,CAST ((CAST(DATE(CHAR(1900000 + RPDIVJ)) AS TIMESTAMP)) AS DATE) FECHA," +
				  " CAST ((CAST(DATE(CHAR(1900000 + RPDDJ)) AS TIMESTAMP)) AS DATE) FECHAVENC,CAST(RPGLC AS VARCHAR(4) CCSID 37) AS COMPENSLM,CAST(TRIM(RPPTC) AS VARCHAR(3) CCSID 37) TIPOPAGO, RPDIVJ,RPDDJ ,CAST (RPPO AS VARCHAR(8) CCSID 37) RPPO, CAST (RPDCTO AS VARCHAR(2) CCSID 37) RPDCTO " +
				  " from "+PropertiesSystem.JDEDTA+".F03B11 INNER JOIN "+PropertiesSystem.JDEDTA+".F0006 UN ON RPMCU = UN.MCMCU INNER JOIN "+PropertiesSystem.JDECOM+".F0005 CO ON (UN.MCRP01 = SUBSTRING(CO.DRKY,8,10) AND CO.DRSY = '00' AND CO.DRRT = '01' AND CO.DRDL02 = 'F') " +
				  " where rpaap > 0 and rpdct not in (select COD_DOCUMENTO from " + PropertiesSystem.GCPCXC + ".TIPOS_DOCUMENTOS_FINANCIAMIENTO where "
				  		+ "TIPO_AGRUPACION IN ('IMO', 'ICR') and cod_compania = CAST(TRIM(UN.MCRP01) AS VARCHAR(3) CCSID 37) "
				  		+ "order by ORDEN_PROCESAMIENTO asc) ";
			
			//parametros de la cabeceras de creditos de finan
			sqlSol = sqlSol + " and RPDCTO = ";
			sqlSuc = sqlSuc + " and RPKCO = ";
			sqlNosol = sqlNosol + " and CAST(RPPO AS NUMERIC(8)) = ";
		
			sqlSol = sqlSol + "'" + fh.getId().getTiposol() + "'";
			sqlSuc = sqlSuc + "'" + fh.getId().getCodsuc()+ "'";
			sqlNosol = sqlNosol + fh.getId().getNosol();						
			
			sql = sql + sqlSuc +  " and rpan8 = " + fh.getId().getCodcli()  + sqlNosol + sqlSol;
			//Parametros para los numeros cuotas
			sql = sql + " and CAST(RPSFX AS NUMERIC(8)) = ";
			sql = sql + fd.getId().getNocuota();			
			sql = sql1 + sql + " order by RPSFX) union " + sql2 + sql + " order by RPSFX) union " + sql3 + sql + " order by RPSFX)";
		 
			lstCuotas= (ArrayList<Credhdr>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, true, Credhdr.class);
			
			if(lstCuotas!=null && lstCuotas.size()>0)
				lstCuotas = llenarInfoFactura(lstCuotas, f14);
			
		}catch(Exception ex){
			ex.printStackTrace(); 
		} 
		return lstCuotas;
	}
/****************************************************************************************/
/**************************************************************************/
	public static List<Credhdr> llenarInfoFactura( List<Credhdr> lstFacturasCredito, final F55ca014[] f14){
		
		try{
			
			CollectionUtils.forAllDo(lstFacturasCredito, new Closure(){

				@Override
				public void execute(Object factura) {
					Credhdr f =  ( Credhdr )factura ;
					
					f.setMoneda(f.getId().getMoneda());
					f.setNofactura(f.getId().getNofactura());
					f.setTipofactura(f.getId().getTipofactura());
					f.setPartida(f.getId().getPartida());
					f.setNomcli(f.getId().getNomcli().trim());
					f.setUnineg(f.getId().getUnineg());
					f.setFecha(f.getId().getFecha());
					f.setFechavenc(f.getId().getFechavenc());
					f.setMontoAplicar(BigDecimal.ZERO);
				
					String sMonedaBase = CompaniaCtrl.sacarMonedaBase( f14, f.getId().getCodcomp() );
					
					if(f.getId().getMoneda().equals(sMonedaBase)){
						f.setMontoPendiente(f.getId().getCpendiente());
						f.getId().setImpuesto(f.getId().getCimpuesto());
						f.getId().setSubtotal(f.getId().getCsubtotal());
						f.getId().setMonto(f.getId().getCtotal());
					}else{
						f.setMontoPendiente(f.getId().getDpendiente());
						f.getId().setImpuesto(f.getId().getDimpuesto());
						f.getId().setSubtotal(f.getId().getDsubtotal());
						f.getId().setMonto(f.getId().getDtotal());
					}
				}
			}) ;
			
			 
		}catch(Exception ex){
			ex.printStackTrace(); 
		}
		return lstFacturasCredito;
	}
	
	
	@SuppressWarnings("unchecked")
	public static List<Credhdr> buscarFinancimientosAsociados( int codigocliente, String codcomp, String strNumeroSolicitud, String codsuc, F55ca014[] f14){
		List<Credhdr> otrosfinan = null;
		
		
		try {
			
			String rppo = CodeUtil.pad(strNumeroSolicitud.trim(), 8, "0");
			
			String sql = "select * from @BDCAJA.vwcreditosporfinanciamientos b " +
					" where b.codcli = @CODCLI and b.codcomp = '@CODCOMP' " +
					" and b.codsuc = '@CODSUC' and trim(b.rppo) = '@RPPO' " +
					" and trim(b.rpdcto) = '@RPDCTO' ";
			
			sql = sql.replace("@BDCAJA", PropertiesSystem.ESQUEMA )
					.replace("@CODCLI", String.valueOf(codigocliente) )
					.replace("@CODCOMP", codcomp.trim() )
					.replace("@CODSUC", codsuc.trim())
					.replace("@RPPO", rppo)
					.replace("@RPDCTO", "X9" ) ;
			
			//otrosfinan = (List<Credhdr>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, true, Credhdr.class);
			
			otrosfinan =  ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql,  Credhdr.class, true);
			
			if(otrosfinan == null || otrosfinan.isEmpty() )
				return otrosfinan;
			
			llenarInfoFactura(otrosfinan, f14);
			
			CollectionUtils.forAllDo(otrosfinan, new Closure(){
				public void execute(Object o) {
					Credhdr c =	(Credhdr)o;

					boolean vencida = c.getId().getFechavenc().before( new Date() ) ;
					boolean tieneSaldo =  c.getId().getDpendiente().compareTo(BigDecimal.ZERO) == 1 ;
					
					if( vencida && tieneSaldo ){
						c.setEstadopago("V");
						c.setEstadoPagoDescripcion("Vencida");
						c.setStyleClass("frmLabel2Error");
					}
					if( !vencida && tieneSaldo ){
						c.setEstadopago("P");
						c.setEstadoPagoDescripcion("Pendiente");
						c.setStyleClass("frmLabel2");
					}
					if( !tieneSaldo){
						c.setEstadopago("C");
						c.setEstadoPagoDescripcion("Cancelada");
						c.setStyleClass("frmLabel2Success");
					}
					
					
				}
			});
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		
		return otrosfinan ;
	} 
	
	
	@SuppressWarnings("unchecked")
	public static List<Credhdr> buscarFinancimientosAsociados( boolean adelantocuota, 
			int ultimaCuotaSeleccionada, int codigocliente, 
			String strNumeroSolicitud, String codcomp,
			String codsuc, F55ca014[] f14, Date fechaPrimeraCuotaNoVencida){
		
		List<Credhdr> otrosfinan = null;
		try {
			
			String rppo = CodeUtil.pad(strNumeroSolicitud.trim(), 8, "0");
			
			String sql = "select * from @BDCAJA.vwcreditosporfinanciamientos b " +
					" where b.codcli = @CODCLI and b.codcomp = '@CODCOMP' " +
					//" and b.codsuc = '@CODSUC' and trim(b.rppo) = '@RPPO' " +
					" and trim(b.rppo) = '@RPPO' " +
					" and trim(b.rpdcto) = '@RPDCTO' and montopend > 0 ";
			
			sql = sql.replace("@BDCAJA", PropertiesSystem.ESQUEMA )
					.replace("@CODCLI", String.valueOf(codigocliente) )
					.replace("@CODCOMP", codcomp.trim() )
					//.replace("@CODSUC", codsuc.trim())
					.replace("@RPPO", rppo)
					.replace("@RPDCTO", "X9" ) ;
			
			//&& ================== Si es adelanto de cuota buscar la siguiente cuota pendiente
			if(adelantocuota){
				sql += " and cast( partida as integer) > "+ultimaCuotaSeleccionada+"  fetch first rows only ";
			}else{ 
			
				
				String mesactual = new SimpleDateFormat("MM").format(new Date());
				String mesPrimeraCuotaNoVencida = new SimpleDateFormat("MM").format(fechaPrimeraCuotaNoVencida);
						
				
				String vencidas = sql + " and b.fechavenc < current_date  " ;
				String proximas ="( "  + sql + " and b.fechavenc >= current_date " +
						" and MONTH(b.fechavenc) between "+mesactual+" and " + mesPrimeraCuotaNoVencida +
						//" and MONTH(b.fechavenc) >= "+ mesactual +" and MONTH(b.fechavenc) <= "  + mesPrimeraCuotaNoVencida +
						" fetch first rows only ) " ;
						
				sql = vencidas +" union all "+ proximas + "  order by partida asc ";
				
				
				//----
				/*
				String mesactual = new SimpleDateFormat("MM").format(new Date());
				
				String vencidas = sql + " and b.fechavenc < current_date  " ;
				String proximas ="( "  + sql + " and b.fechavenc >= current_date  and MONTH(b.fechavenc) = "+ mesactual +" fetch first rows only ) " ;
						
				sql = vencidas +" union all "+ proximas + "  order by partida asc ";
				*/
			}
			LogCajaService.CreateLog("buscarFinancimientosAsociados", "QRY", sql);
			
			//otrosfinan = (List<Credhdr>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, true, Credhdr.class);
			otrosfinan = new RoQueryManager().executeSqlQuery(sql, Credhdr.class, true);
			
			if(otrosfinan == null || otrosfinan.isEmpty() )
				return otrosfinan;
			
			llenarInfoFactura(otrosfinan, f14);
			
			CollectionUtils.forAllDo(otrosfinan, new Closure(){
				public void execute(Object o) {
					( (Credhdr)o).setFinancimientoAsociado(true);		
				}
			});
			
			
		} catch (Exception e) {
			e.printStackTrace(); 
			LogCajaService.CreateLog("buscarFinancimientosAsociados", "ERR", e.getMessage());
		}finally{
			
		}
		return otrosfinan;
	}
	
	public List<Credhdr> documentosPorCuota( List<Finandet> lstFinandet, Finanhdr fh, F55ca014[] f14){
		 List<Credhdr> documentosPorCuota = null ;
		 
		 try {
			
			 String sql  = 
			" select @Reemp c, @BDCAJA.credhdr.* from @BDCAJA.credhdr " +
			" where codcli = @CODCLI and rpdcto = '@RPDCTO' and codsuc = '@RPKCO' " +
//			" and CAST(rppo as numeric(8) ) = @RPPO and CAST(PARTIDA AS NUMERIC(8) ) in (@NUMEROCUOTAS) " +
			" and rppo = concat(substring('00000000',0,9-length(trim(@RPPO))),trim(@RPPO )) and CAST(PARTIDA AS NUMERIC(8) ) in (@NUMEROCUOTAS) " +
			" and tipofactura @TIPOFACTURA " ; 
 
			 String numerosCuotas = "" ;
			 for (Finandet finandet : lstFinandet) {
				
				 if(finandet.getId().getNosol() != fh.getId().getNosol() )
					 continue;
				 numerosCuotas  +=  finandet.getId().getNocuota() + ", ";
				 
			}
			numerosCuotas = numerosCuotas.substring(0, numerosCuotas.lastIndexOf(",")) ;
			
			sql = sql
			.replace("@BDCAJA", PropertiesSystem.ESQUEMA )
			.replace("@CODCLI", String.valueOf(fh.getId().getCodcli()) )
			.replace("@RPPO",   String.valueOf( fh.getId().getNosol() ) ) 
			.replace("@RPDCTO", fh.getId().getTiposol() )
			.replace("@RPKCO",  fh.getId().getCodsuc() ) 
			.replace("@NUMEROCUOTAS", numerosCuotas )  ;
			
			 String sql2  = 
						" select ORDEN_PROCESAMIENTO c, @BDCAJA.credhdr.* from @BDCAJA.credhdr " +
						"inner join " + PropertiesSystem.GCPCXC + ".TIPOS_DOCUMENTOS_FINANCIAMIENTO " +
						"on COD_DOCUMENTO = tipofactura and codcomp = cod_compania  " +
						" where codcli = @CODCLI and rpdcto = '@RPDCTO' and codsuc = '@RPKCO' " +
//						" and CAST(rppo as numeric(8) ) = @RPPO and CAST(PARTIDA AS NUMERIC(8) ) in (@NUMEROCUOTAS) " +
						" and rppo = concat(substring('00000000',0,9-length(trim(@RPPO))),trim(@RPPO )) and CAST(PARTIDA AS NUMERIC(8) ) in (@NUMEROCUOTAS) " +
						" and TIPO_AGRUPACION <> 'PRI' " ; 
			
			 sql2 = sql2
						.replace("@BDCAJA", PropertiesSystem.ESQUEMA )
						.replace("@CODCLI", String.valueOf(fh.getId().getCodcli()) )
						.replace("@RPPO",   String.valueOf( fh.getId().getNosol() ) ) 
						.replace("@RPDCTO", fh.getId().getTiposol() )
						.replace("@RPKCO",  fh.getId().getCodsuc() ) 
						.replace("@NUMEROCUOTAS", numerosCuotas )  ;
			 
			String queryUnion =  
					"select * from (" +
//				sql.replace("@TIPOFACTURA", " = 'MF' ").replace("@Reemp", "1")  +" \n union " +
				sql2 +" \n union " +
				sql.replace("@TIPOFACTURA", " not in (select COD_DOCUMENTO from " + PropertiesSystem.GCPCXC + ".TIPOS_DOCUMENTOS_FINANCIAMIENTO "
						+ "where TIPO_AGRUPACION <> 'PRI' and cod_compania = codcomp order by ORDEN_PROCESAMIENTO asc)  ").replace("@Reemp", "999") + ") t order by t.partida, t.c " ;
			
//			System.out.println(queryUnion);
			//documentosPorCuota = ConsolidadoDepositosBcoCtrl.executeSqlQuery( queryUnion, Credhdr.class, true);
			
			LogCajaService.CreateLog("documentosPorCuota", "QRY", queryUnion);
			
			documentosPorCuota = new RoQueryManager().executeSqlQuery( queryUnion, Credhdr.class, true);
			
			if( documentosPorCuota == null || documentosPorCuota.isEmpty() )
				return null;
			 
			llenarInfoFactura(documentosPorCuota, f14);
	 
		} catch (Exception e) {
			e.printStackTrace(); 
			LogCajaService.CreateLog("documentosPorCuota", "ERR", e.getMessage());
		}
		 return documentosPorCuota ;
	}
	
	
	/************************************************************************************************************************/
	public List<Credhdr> leerDocumentosxCuotas(List lstFinandet, Finanhdr fh,F55ca014[] f14){
		List<Credhdr> lstCuotas = new ArrayList(),lstResult = null;
		Session s = null;
		Transaction tx = null;
		String sql = "",sqlSol = "",sqlSuc = "", sqlNosol = "", sql1 = "", sql2 = "", sql3 = "";
		Object[] obj = null;
		Finandet fd = null;
		FinandetId nFdid = null;
		//Finanhdr fh = null;
		try{
			//fh = (Finanhdr)lstFinanheader.get(0);
			sql1 = "(SELECT CAST(TRIM(RPDCT) AS VARCHAR(2) CCSID 37) TIPOFACTURA,RPDOC AS NOFACTURA,CAST (RPSFX AS VARCHAR(3) CCSID 37) AS PARTIDA,RPAN8 AS CODCLI, RPALPH AS NOMCLI, CAST(TRIM(RPMCU) AS VARCHAR(12) CCSID 37) CODUNINEG, UN.MCDL01 AS UNINEG, CAST(RPKCO AS VARCHAR(5) CCSID 37) CODSUC,"+
				  " (SELECT MCDL01 FROM "+PropertiesSystem.JDEDTA+".F0006 WHERE SUBSTRING(RPKCO,4,5) = SUBSTRING(MCMCU,11,12) AND MCSTYL = 'BS' FETCH FIRST ROWS ONLY ) AS NOMSUC,CAST(TRIM(UN.MCRP01) AS VARCHAR(3) CCSID 37) CODCOMP, CO.DRDL01 AS NOMCOMP, CAST(TRIM(RPCRCD) AS VARCHAR(3) CCSID 37) MONEDA, RPCRR AS TASA, "+
				  " CAST(RPAAP/100 AS DECIMAL (12,2)) AS CPENDIENTE,CAST(RPFAP/100 AS DECIMAL (12,2)) AS DPENDIENTE,CAST(RPAG/100 AS DECIMAL (12,2)) AS CTOTAL,CAST(RPACR/100 AS DECIMAL (12,2)) AS DTOTAL,CAST(RPSTAM/100 AS DECIMAL (12,2)) AS CIMPUESTO,CAST(RPCTAM/100 AS DECIMAL (12,2)) AS DIMPUESTO," +
				  " CAST( (RPATXA + RPATXN ) /100 AS DECIMAL (12,2)) AS CSUBTOTAL,CAST((RPCTXA + RPCTXN )/100 AS DECIMAL (12,2)) AS DSUBTOTAL, CAST((CAST(( CASE WHEN RPCRCD = 'COR' THEN RPAG ELSE RPACR END) AS DECIMAL(12,2))/100) AS DECIMAL(12,2)) MONTO,CAST((CAST(( CASE WHEN RPCRCD = 'COR' THEN RPAAP ELSE RPFAP END) AS DECIMAL(12,2))/100) AS DECIMAL(12,2)) MONTOPEND," +
				  " CAST((CAST(( CASE WHEN RPCRCD = 'COR' THEN (RPATXA + RPATXN ) ELSE (RPCTXA + RPCTXN ) END) AS DECIMAL(12,2))/100) AS DECIMAL(12,2)) SUBTOTAL,CAST((CAST(( CASE WHEN RPCRCD = 'COR' THEN RPSTAM ELSE RPCTAM END) AS DECIMAL(12,2))/100) AS DECIMAL(12,2)) IMPUESTO,CAST ((CAST(DATE(CHAR(1900000 + RPDIVJ)) AS TIMESTAMP)) AS DATE) FECHA," +
				  " CAST ((CAST(DATE(CHAR(1900000 + RPDDJ)) AS TIMESTAMP)) AS DATE) FECHAVENC,CAST(RPGLC AS VARCHAR(4) CCSID 37) AS COMPENSLM,CAST(TRIM(RPPTC) AS VARCHAR(3) CCSID 37) TIPOPAGO, RPDIVJ,RPDDJ,CAST (RPPO AS VARCHAR(8) CCSID 37) RPPO, CAST (RPDCTO AS VARCHAR(2) CCSID 37) RPDCTO " +
				  " from "+PropertiesSystem.JDEDTA+".f03B11 INNER JOIN "+PropertiesSystem.JDEDTA+".F0006 UN ON RPMCU = UN.MCMCU INNER JOIN "+PropertiesSystem.JDECOM+".F0005 CO ON (UN.MCRP01 = SUBSTRING(CO.DRKY,8,10) AND CO.DRSY = '00' AND CO.DRRT = '01' AND CO.DRDL02 = 'F') " +
//				  "where rpaap > 0 and rpdct = 'MF' ";
				  "where rpaap > 0 and rpdct in (select COD_DOCUMENTO from " + PropertiesSystem.GCPCXC + ".TIPOS_DOCUMENTOS_FINANCIAMIENTO "
				  		+ "where TIPO_AGRUPACION = 'IMO' and cod_compania = CAST(TRIM(UN.MCRP01) AS VARCHAR(3) CCSID 37) order by ORDEN_PROCESAMIENTO asc) ";
			
			sql2 =	  "(SELECT CAST(TRIM(RPDCT) AS VARCHAR(2) CCSID 37) TIPOFACTURA,RPDOC AS NOFACTURA,CAST (RPSFX AS VARCHAR(3) CCSID 37) AS PARTIDA,RPAN8 AS CODCLI, RPALPH AS NOMCLI, CAST(TRIM(RPMCU) AS VARCHAR(12) CCSID 37) CODUNINEG, UN.MCDL01 AS UNINEG, CAST(RPKCO AS VARCHAR(5) CCSID 37) CODSUC,"+
				  " (SELECT MCDL01 FROM "+PropertiesSystem.JDEDTA+".F0006 WHERE SUBSTRING(RPKCO,4,5) = SUBSTRING(MCMCU,11,12) AND MCSTYL = 'BS' FETCH FIRST ROWS ONLY ) AS NOMSUC,CAST(TRIM(UN.MCRP01) AS VARCHAR(3) CCSID 37) CODCOMP, CO.DRDL01 AS NOMCOMP, CAST(TRIM(RPCRCD) AS VARCHAR(3) CCSID 37) MONEDA, RPCRR AS TASA, "+
				  " CAST(RPAAP/100 AS DECIMAL (12,2)) AS CPENDIENTE,CAST(RPFAP/100 AS DECIMAL (12,2)) AS DPENDIENTE,CAST(RPAG/100 AS DECIMAL (12,2)) AS CTOTAL,CAST(RPACR/100 AS DECIMAL (12,2)) AS DTOTAL,CAST(RPSTAM/100 AS DECIMAL (12,2)) AS CIMPUESTO,CAST(RPCTAM/100 AS DECIMAL (12,2)) AS DIMPUESTO," +
				  " CAST( (RPATXA + RPATXN ) /100 AS DECIMAL (12,2)) AS CSUBTOTAL,CAST((RPCTXA + RPCTXN )/100 AS DECIMAL (12,2)) AS DSUBTOTAL, CAST((CAST(( CASE WHEN RPCRCD = 'COR' THEN RPAG ELSE RPACR END) AS DECIMAL(12,2))/100) AS DECIMAL(12,2)) MONTO,CAST((CAST(( CASE WHEN RPCRCD = 'COR' THEN RPAAP ELSE RPFAP END) AS DECIMAL(12,2))/100) AS DECIMAL(12,2)) MONTOPEND," +
				  " CAST((CAST(( CASE WHEN RPCRCD = 'COR' THEN (RPATXA + RPATXN ) ELSE (RPCTXA + RPCTXN ) END) AS DECIMAL(12,2))/100) AS DECIMAL(12,2)) SUBTOTAL,CAST((CAST(( CASE WHEN RPCRCD = 'COR' THEN RPSTAM ELSE RPCTAM END) AS DECIMAL(12,2))/100) AS DECIMAL(12,2)) IMPUESTO,CAST ((CAST(DATE(CHAR(1900000 + RPDIVJ)) AS TIMESTAMP)) AS DATE) FECHA," +
				  " CAST ((CAST(DATE(CHAR(1900000 + RPDDJ)) AS TIMESTAMP)) AS DATE) FECHAVENC,CAST(RPGLC AS VARCHAR(4) CCSID 37) AS COMPENSLM,CAST(TRIM(RPPTC) AS VARCHAR(3) CCSID 37) TIPOPAGO, RPDIVJ,RPDDJ,CAST (RPPO AS VARCHAR(8) CCSID 37) RPPO, CAST (RPDCTO AS VARCHAR(2) CCSID 37) RPDCTO " +
				  " from "+PropertiesSystem.JDEDTA+".f03B11 INNER JOIN "+PropertiesSystem.JDEDTA+".F0006 UN ON RPMCU = UN.MCMCU INNER JOIN "+PropertiesSystem.JDECOM+".F0005 CO ON (UN.MCRP01 = SUBSTRING(CO.DRKY,8,10) AND CO.DRSY = '00' AND CO.DRRT = '01' AND CO.DRDL02 = 'F') " +
//				  " where rpaap > 0 and rpdct = 'IF' ";
				  " where rpaap > 0 and rpdct in select COD_DOCUMENTO from " + PropertiesSystem.GCPCXC + ".TIPOS_DOCUMENTOS_FINANCIAMIENTO "
				  		+ "where TIPO_AGRUPACION = 'ICR' and cod_compania = CAST(TRIM(UN.MCRP01) AS VARCHAR(3) CCSID 37) order by ORDEN_PROCESAMIENTO asc ";
			
			sql3 =	  "(SELECT CAST(TRIM(RPDCT) AS VARCHAR(2) CCSID 37) TIPOFACTURA,RPDOC AS NOFACTURA,CAST (RPSFX AS VARCHAR(3) CCSID 37) AS PARTIDA,RPAN8 AS CODCLI, RPALPH AS NOMCLI, CAST(TRIM(RPMCU) AS VARCHAR(12) CCSID 37) CODUNINEG, UN.MCDL01 AS UNINEG, CAST(RPKCO AS VARCHAR(5) CCSID 37) CODSUC,"+
				  " (SELECT MCDL01 FROM "+PropertiesSystem.JDEDTA+".F0006 WHERE SUBSTRING(RPKCO,4,5) = SUBSTRING(MCMCU,11,12) AND MCSTYL = 'BS' FETCH FIRST ROWS ONLY ) AS NOMSUC,CAST(TRIM(UN.MCRP01) AS VARCHAR(3) CCSID 37) CODCOMP, CO.DRDL01 AS NOMCOMP, CAST(TRIM(RPCRCD) AS VARCHAR(3) CCSID 37) MONEDA, RPCRR AS TASA, "+
				  " CAST(RPAAP/100 AS DECIMAL (12,2)) AS CPENDIENTE,CAST(RPFAP/100 AS DECIMAL (12,2)) AS DPENDIENTE,CAST(RPAG/100 AS DECIMAL (12,2)) AS CTOTAL,CAST(RPACR/100 AS DECIMAL (12,2)) AS DTOTAL,CAST(RPSTAM/100 AS DECIMAL (12,2)) AS CIMPUESTO,CAST(RPCTAM/100 AS DECIMAL (12,2)) AS DIMPUESTO," +
				  " CAST( (RPATXA + RPATXN ) /100 AS DECIMAL (12,2)) AS CSUBTOTAL,CAST((RPCTXA + RPCTXN )/100 AS DECIMAL (12,2)) AS DSUBTOTAL, CAST((CAST(( CASE WHEN RPCRCD = 'COR' THEN RPAG ELSE RPACR END) AS DECIMAL(12,2))/100) AS DECIMAL(12,2)) MONTO,CAST((CAST(( CASE WHEN RPCRCD = 'COR' THEN RPAAP ELSE RPFAP END) AS DECIMAL(12,2))/100) AS DECIMAL(12,2)) MONTOPEND," +
				  " CAST((CAST(( CASE WHEN RPCRCD = 'COR' THEN (RPATXA + RPATXN ) ELSE (RPCTXA + RPCTXN ) END) AS DECIMAL(12,2))/100) AS DECIMAL(12,2)) SUBTOTAL,CAST((CAST(( CASE WHEN RPCRCD = 'COR' THEN RPSTAM ELSE RPCTAM END) AS DECIMAL(12,2))/100) AS DECIMAL(12,2)) IMPUESTO,CAST ((CAST(DATE(CHAR(1900000 + RPDIVJ)) AS TIMESTAMP)) AS DATE) FECHA," +
				  " CAST ((CAST(DATE(CHAR(1900000 + RPDDJ)) AS TIMESTAMP)) AS DATE) FECHAVENC,CAST(RPGLC AS VARCHAR(4) CCSID 37) AS COMPENSLM,CAST(TRIM(RPPTC) AS VARCHAR(3) CCSID 37) TIPOPAGO, RPDIVJ,RPDDJ,CAST (RPPO AS VARCHAR(8) CCSID 37) RPPO, CAST (RPDCTO AS VARCHAR(2) CCSID 37) RPDCTO " +
				  " from "+PropertiesSystem.JDEDTA+".f03B11 INNER JOIN "+PropertiesSystem.JDEDTA+".F0006 UN ON RPMCU = UN.MCMCU INNER JOIN "+PropertiesSystem.JDECOM+".F0005 CO ON (UN.MCRP01 = SUBSTRING(CO.DRKY,8,10) AND CO.DRSY = '00' AND CO.DRRT = '01' AND CO.DRDL02 = 'F') " +
//				  " where rpaap > 0 and rpdct not in ('MF','IF', 'RI') ";
				  " where rpaap > 0 and rpdct not in (select COD_DOCUMENTO from " + PropertiesSystem.GCPCXC + ".TIPOS_DOCUMENTOS_FINANCIAMIENTO "
				  		+ "where TIPO_AGRUPACION IN ('IMO', 'ICR', 'SEG') and cod_compania = CAST(TRIM(UN.MCRP01) AS VARCHAR(3) CCSID 37) order by ORDEN_PROCESAMIENTO asc) ";
			
			//parametros de la cabeceras de creditos de finan
			sqlSol = sqlSol + " and RPDCTO = '" + fh.getId().getTiposol() + "'";
			sqlSuc = sqlSuc + " and RPKCO = '" + fh.getId().getCodsuc()+ "'";
			sqlNosol = sqlNosol + " and CAST(RPPO AS NUMERIC(8)) = "+ fh.getId().getNosol();
			/*for(int i = 0; i < lstFinanheader.size(); i++){
				fh = (Finanhdr)lstFinanheader.get(i);
				if (i == lstFinanheader.size() - 1){
					 sqlSol = sqlSol + "'" + fh.getId().getTiposol() + "'";
					sqlSuc = sqlSuc + "'" + fh.getId().getCodsuc()+ "'";
					sqlNosol = sqlNosol + fh.getId().getNosol();
				}else{
					sqlSol = sqlSol + "'" + fh.getId().getTiposol() + "',";
					sqlSuc = sqlSuc + "'" + fh.getId().getCodsuc() + "',";
					sqlNosol = sqlNosol + fh.getId().getNosol() + ",";
				}
			}
			sqlSol = sqlSol + ") "; 
			sqlSuc = sqlSuc + ") ";
			sqlNosol = sqlNosol + ") ";*/
			
			sql = sql + sqlSuc +  " and rpan8 = " + fh.getId().getCodcli()  + sqlNosol + sqlSol;
			
			
			
			//Parametros para los numeros cuotas
			if(lstFinandet != null && !lstFinandet.isEmpty()){
				sql = sql + "and CAST(RPSFX AS NUMERIC(8)) in (";
			}
			
			for(int j = 0; j < lstFinandet.size(); j++){
				fd = (Finandet)lstFinandet.get(j);
				if(fh.getId().getNosol() == fd.getId().getNosol()){
					if (j == lstFinandet.size() - 1){
						sql = sql + fd.getId().getNocuota();
					}else{
						sql = sql + fd.getId().getNocuota() + ",";
					}
				}else{
					if (j == lstFinandet.size() - 1){
						sql = sql + "0";
					}else{
						sql = sql + "0" + ",";
					}
				}
			}
			if(lstFinandet != null && !lstFinandet.isEmpty()) 
				sql = sql + ") "; 
			
			sql = sql1 + sql + " order by RPSFX) union " + sql2 + sql + "order by RPSFX) union " + sql3 + sql + "order by RPSFX)";
			s = HibernateUtilPruebaCn.currentSession();
			tx = s.beginTransaction();			
			
			lstCuotas = s.createSQLQuery(sql).addEntity(Credhdr.class).list();	
			
			
			if(lstCuotas!=null && lstCuotas.size()>0)
				lstCuotas = llenarInfoFactura(lstCuotas, f14);
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			try{tx.commit();}catch(Exception e){e.printStackTrace();}
			try{HibernateUtilPruebaCn.closeSession(s);}catch(Exception e){e.printStackTrace();}
		}
		return lstCuotas;
	}
/****************************************************************************************/
/*********************************************************************************************************************/
	public Finandet buscarAnteriorCuota(String sCodcomp, String sCodsuc, final int iNosol,String sTiposol, int iCodcli,List lstSelectedCuotas){
		Finandet f = null;
 
		try{
			
			String sql = "select * from "+PropertiesSystem.ESQUEMA+".finandet" +
						" where nosol = "+iNosol + " and codcli = "+iCodcli+" and codcomp = '"+sCodcomp+"' and codsuc = '"+sCodsuc+"' " +
						" and montopend > 0 "; 
				
			@SuppressWarnings("unchecked")
			List<Finandet> cuotasIncluidas = (ArrayList<Finandet>)
			CollectionUtils.select(lstSelectedCuotas, new Predicate(){
				public boolean evaluate(Object o) {
					return ((Finandet)o).getId().getNosol() == iNosol;
				}
			});
			
			if( cuotasIncluidas != null && !cuotasIncluidas.isEmpty()  ){
				String numeros = "";
				
				for (Finandet finandet : cuotasIncluidas) {
					numeros += finandet.getId().getNocuota() +", ";
				}
				numeros = numeros.substring(0, numeros.lastIndexOf(","));
				sql += " and nocuota not in ("+numeros +")";
			}
			
			sql = sql + " order by nocuota desc FETCH FIRST 1 ROWS only OPTIMIZE FOR 1 ROWS";
			
			f = ConsolidadoDepositosBcoCtrl.executeSqlQueryUnique(sql, Finandet.class, true);
			
			 
		}catch(Exception ex){
			ex.printStackTrace(); 
		} 
		
		return f;
	}
	
/********************************************************************************************/
	public List<Finandet> getUltimaCuota( List<Finanhdr> lstCreditos){
		List<Finandet> lstCuotas = null;
		String sql = "";
		
		try{
			
			for (int i = 0; i < lstCreditos.size();i++){
				Finanhdr fh = (Finanhdr)lstCreditos.get(i);
				if(i == 0){
					sql = "select * from "+PropertiesSystem.ESQUEMA+".Finandet as f where f.codcomp = '" +fh.getId().getCodcomp()+"' and f.codsuc = '" +fh.getId().getCodsuc()+"' and f.nosol = " +fh.getId().getNosol()+" and f.tiposol = '"+fh.getId().getTiposol()+"' and f.codcli = " +fh.getId().getCodcli() + " and f.montopend > 0 order by nocuota desc fetch first 1 rows only";								
				}
				else{
					sql = sql + " union ";
					sql = sql + "select * from "+PropertiesSystem.ESQUEMA+".Finandet as f where f.codcomp = '" +fh.getId().getCodcomp()+"' and f.codsuc = '" +fh.getId().getCodsuc()+"' and f.nosol = " +fh.getId().getNosol()+" and f.tiposol = '"+fh.getId().getTiposol()+"' and f.codcli = " +fh.getId().getCodcli() + " and f.montopend > 0 order by nocuota desc fetch first 1 rows only";
				}
			}
			 
			lstCuotas = ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, Finandet.class, true);
			
		}catch(Exception ex){
			ex.printStackTrace(); 
		} 
		
		return lstCuotas;
	}
	
	/*********** 14. Enviar un correo electrónico al contador de la caja ***********************************/
	public boolean  enviarCorreo(String sEncabezado,String sPieCorreo,String sSolicitud, String sTo, String sFrom, String sCc,
								String sSubject, int iCuota,String sCliente,int iNobatch,String sNodoc,
							 	String sMonto,String sCaja,String sSucursal,String sCompania,String sUrl,Date dFecha){
		//MultiPartEmail email = new MultiPartEmail();		
		String sHora = "";
		String sFecha = "";
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
		Divisas dv = new Divisas();
		boolean enviado = true;
		
		try {
			sFecha = sdf.format(dFecha);
			sHora = dfHora.format(dFecha);
			
			String shtml = "<table width=\"410px\" style=\"border: 1px #7a7a7a solid\" align=\"center\" cellspacing=\"0\" cellpadding=\"3\">" +
							"<tr>"+
							"<th colspan=\"2\" style=\"border-bottom: 1px #7a7a7a solid; background: #3e68a4\">" +
								"<font face=\"Arial\" size=\"2\" color=\"white\"><b>"+sEncabezado+"</b></font>" +
							"</th>" +
							"</tr>"+
							"<tr>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"20%\"><b>Solicitud #: &nbsp;</b></td>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"80%\">"+sSolicitud+"</td>" +
							"</tr>"+
							"<tr>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"20%\"><b>Cuota: &nbsp;</b></td>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"80%\">"+iCuota+"</td>" +
							"</tr>"+
							"<tr>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"20%\"><b>Cliente: &nbsp;</b></td>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"80%\">"+ sCliente + "</td>" +
							"</tr>"+
							"<tr>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"20%\"><b>No. Batch: &nbsp;</b></td>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"80%\">"+ iNobatch +"</td>" +
							"</tr>"+
							"<tr>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"20%\"><b>No. Documento: &nbsp;</b></td>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"80%\">"+sNodoc+"</td>" +
							"</tr>" +
							"<tr>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"20%\"><b>Monto: &nbsp;</b></td>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"80%\">"+sMonto +"</td>" +
							"</tr>"+
							"<tr>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"20%\"><b>Caja: &nbsp;</b></td>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"80%\">"+sCaja +"</td>" +
							"</tr>"+
							"<tr>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"20%\"><b>Ubicación: &nbsp;</b></td>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"80%\">"+sSucursal +"</td>" +
							"</tr>"+
							"<tr>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"20%\"><b>Compañia: &nbsp;</b></td>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"80%\">"+sCompania +"</td>" +
							"</tr>"+
							"<tr>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"20%\"><b>Fecha: &nbsp;</b></td>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"80%\">"+ sFecha + " / " + sHora +"</td>" +
							"</tr>" +
							"<tr>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"20%\"><b>Enlace: &nbsp;</b></td>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"80%\">"+ sUrl +"</td>" +
							"</tr>"+
							
							"<tr>" +
								"<td align=\"center\" colspan=\"2\" style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px; color: #3e68a4; border-bottom: 1px ##1a1a1a solid; \">" +
									"<b>"+sPieCorreo+"</b>" +
								"</td>" +
							"</tr>" +
							
							"<tr>" +
								"<td align=\"center\" colspan=\"2\" style=\"font-family: Arial, Helvetica, sans-serif;font-size: 10px;color: black; border-bottom: 1px ##1a1a1a solid; \">" +
									"<b>Casa Pellas, S. A. - Módulo de Caja</b>" +
								"</td>" +
							"</tr>" +
						"</table>";
			
			MailHelper.SendHtmlEmail(
					new CustomEmailAddress(sFrom),
					new CustomEmailAddress(sTo),
					sSubject, shtml);
			 
		}catch(Exception ex){
			ex.printStackTrace();
			enviado = false;			
		}		
		return enviado;
	}	

	/**
	 * Modificado por: lfonseca -> 2020-12-10
	 * @param codcli
	 * @param caid
	 * @param numrec
	 * @param fecharecibo
	 * @param codcomp
	 * @param codunineg
	 * @return
	 */
	public static List<String[]> restaurarSaldoInicialCuota(int codcli, int caid, int numrec, Date fecharecibo, String codcomp, String codunineg  ){
		 
		List<String[]> strUpdateSql = new ArrayList<String[]>() ;
		
		try {
			
			String strSqlHistorico = 
				"select ATAG, ATAAP, ATACR, ATFAP, ATINTD, ATINTF, IDHISTORICO, NUMEROSOLICITUD, NUMEROCUOTA, CODSUC  " +
				"from @GCPMCAJA.HISTORICO_CUOTAS_FINAN  hf " +
				"where caid = @CAID and trim(codcomp) = '@CODCOMP' " +
				"and numrec = @NUMREC AND codigocliente = @CODCLIE " +
				"and TRIM(CODUNINEG) = '@CODUNINEG' " +
				"and estado = 1 and fecharecibo = '@FECHARECIBO' " ;
					
			strSqlHistorico = strSqlHistorico
				.replace("@GCPMCAJA", PropertiesSystem.ESQUEMA )
				.replace("@CAID", Integer.toString( caid ) )
				.replace("@CODCOMP", codcomp)
				.replace("@NUMREC", Integer.toString( numrec ))
				.replace("@CODCLIE", Integer.toString( codcli ) )
				.replace("@CODUNINEG", codunineg.trim())
				.replace("@FECHARECIBO", new SimpleDateFormat("yyyy-MM-dd").format(fecharecibo));
 
			List<Object[]> lstDtaHistorico = ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSqlHistorico, null, true);
			
			if(lstDtaHistorico == null || lstDtaHistorico.isEmpty() ){
				return strUpdateSql = null;
			}
			
			String strSqlUpdate = " update "+PropertiesSystem.GCPCXC+".F5503AM F set  " +
//				"ATAG  = @MONTODOM, ATAAP = @PNDDOM, " +
//				"ATACR = @MONTOFOR, ATFAP = @PNDFOR, " +
				"ATAAP = @PNDDOM, " +
				"ATFAP = @PNDFOR, " +
				"ATINTD = @INTDOM, ATINTF = @INTFOR "  +
				"WHERE ATKCOO = '@CODSUC' AND ATDOCO = @NOSOL " +
				"AND ATDFR = @NOCUOTA AND ATAN8 = @CODCLI " ;
			
			
			List<String[]> sqlUpdateCuota = new ArrayList<String[]>() ;
			List<String[]> sqlUpdateHistorico = new ArrayList<String[]>() ;
			
			for (Object[] dataHistorico : lstDtaHistorico) {
				
				sqlUpdateCuota.add(
					new String[]{
						strSqlUpdate
							.replace("@MONTODOM", String.valueOf( dataHistorico[0] ) ) 
							.replace("@PNDDOM", String.valueOf( dataHistorico[1] ) ) 
							.replace("@MONTOFOR", String.valueOf( dataHistorico[2] ) )  
							.replace("@PNDFOR", String.valueOf( dataHistorico[3] ) ) 
							.replace("@INTDOM", String.valueOf( dataHistorico[4] ) ) 
							.replace("@INTFOR",  String.valueOf( dataHistorico[5] ) ) 
							.replace("@CODSUC",  String.valueOf( dataHistorico[9] ) )
							.replace("@NOSOL",   String.valueOf( dataHistorico[7] ) )
							.replace("@NOCUOTA", String.valueOf( dataHistorico[8] ) )
							.replace("@CODCLI",  Integer.toString( codcli ) ),
							
							"Restauracion de Saldo de cuota " + String.valueOf( dataHistorico[8] ) 
						}
					);
				
				sqlUpdateHistorico.add(
						new String[]{
							"update " +PropertiesSystem.ESQUEMA+".HISTORICO_CUOTAS_FINAN set estado = 0 where IDHISTORICO = " + dataHistorico[6],
							" Restauracion de historico "+ dataHistorico[6] + " Saldo de cuota " + String.valueOf( dataHistorico[8] ) 
						}
					);
				
			}
			
			strUpdateSql.addAll(sqlUpdateCuota);
			strUpdateSql.addAll(sqlUpdateHistorico);
			
		} catch (Exception e) {
			strUpdateSql = null;
			e.printStackTrace(); 
		}
		
		return strUpdateSql ;
	}
	
	
	
	public boolean aplicarMontoF5503AM_1(Connection cn, double dMontopendienteDom, double dMontoPendienteForaneo, 
			Vf0311fn f, int caid, int numrec, Date fecharecibo, String codunineg ){
		boolean done = true;
		
		
		try {
		
			//&& ============== consultar si el recibo tiene historico para pago de abono a principal.
			String strSqlHistorico = 
			"select ATAG, ATAAP, ATACR, ATFAP, ATINTD, ATINTF, IDHISTORICO " +
			"from @GCPMCAJA.HISTORICO_CUOTAS_FINAN  hf " +
			"where caid = @CAID and trim(codcomp) = '@CODCOMP' " +
			"and numrec = @NUMREC AND codigocliente = @CODCLIE " +
			"and codsuc = '@CODSUC' AND TRIM(CODUNINEG) = '@CODUNINEG' " +
			"and pagoprincipal = 1 and estado = 1 and fecharecibo = '@FECHARECIBO' " +
			"and numerosolicitud = @NOSOL and numerocuota = @NOCUOTA" ;
			
			strSqlHistorico = strSqlHistorico
			.replace("@GCPMCAJA", PropertiesSystem.ESQUEMA )
			.replace("@CAID", Integer.toString( caid ) )
			.replace("@CODCOMP", f.getId().getCodcomp().trim() )
			.replace("@NUMREC", Integer.toString( numrec ))
			.replace("@CODCLIE", Integer.toString( f.getId().getCodcli() ) )
			.replace("@CODSUC", "000"+f.getId().getCodsuc().trim() )
			.replace("@CODUNINEG", codunineg.trim())
			.replace("@FECHARECIBO", new SimpleDateFormat("yyyy-MM-dd").format(fecharecibo))
			.replace("@NOSOL", Integer.toString( f.getId().getNosol() ) )
			.replace("@NOCUOTA", Integer.toString( f.getId().getNocuota() ) ) ;
			
			@SuppressWarnings("unchecked")
			List<Object[]> lstDtaHistorico = (List<Object[]>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSqlHistorico, true, null);
			
			if( lstDtaHistorico != null && !lstDtaHistorico.isEmpty() ){
			
				Object[] dataHistorico = lstDtaHistorico.get(0);
				
				String strSqlUpdate = " update "+PropertiesSystem.GCPCXC+".F5503AM F set  " +
//				"ATAG  = @MONTODOM, ATAAP = @PNDDOM, " +
//				"ATACR = @MONTOFOR, ATFAP = @PNDFOR, " +
				"ATAAP = @PNDDOM, " +
				"ATFAP = @PNDFOR, " +
				"ATINTD = @INTDOM, ATINTF = @INTFOR "  +
				"WHERE ATKCOO = '@CODSUC' AND ATDOCO = @NOSOL " +
				"AND ATDFR = @NOCUOTA AND ATAN8 = @CODCLI " ;
				
				String[] queryUpdate  = {
			
					strSqlUpdate
						.replace("@MONTODOM", String.valueOf( dataHistorico[0] ) ) 
						.replace("@PNDDOM", String.valueOf( dataHistorico[1] ) ) 
						.replace("@MONTOFOR", String.valueOf( dataHistorico[2] ) )  
						.replace("@PNDFOR", String.valueOf( dataHistorico[3] ) ) 
						.replace("@INTDOM", String.valueOf( dataHistorico[4] ) ) 
						.replace("@INTFOR", String.valueOf( dataHistorico[5] ) ) 
						.replace("@CODSUC", "000"+f.getId().getCodsuc()  )
						.replace("@NOSOL", Integer.toString( f.getId().getNosol() ) )
						.replace("@NOCUOTA",Integer.toString( f.getId().getNocuota()) )
						.replace("@CODCLI", Integer.toString( f.getId().getCodcli() ) ),
						
						"update " +PropertiesSystem.ESQUEMA+".HISTORICO_CUOTAS_FINAN set estado = 0 where IDHISTORICO = " + dataHistorico[6]
						
					} ;
			
				done = ConsolidadoDepositosBcoCtrl.executeSqlQueries(Arrays.asList(queryUpdate));
			
				return done;
		
			}else{
			
				PreparedStatement ps = null;
				String sql = "UPDATE "+PropertiesSystem.GCPCXC+".F5503AM SET ATAAP = " 
				+ Divisas.pasarAentero(dMontopendienteDom) + ",ATFAP = " 
				+ Divisas.pasarAentero(dMontoPendienteForaneo) +  
				" WHERE ATKCOO = '000"+ f.getId().getCodsuc() + "' AND ATDCTO ='" + f.getId().getTiposol() + "'" +
				" AND ATDOCO = " + f.getId().getNosol() + " AND ATDFR = " + f.getId().getNocuota() + " AND ATAN8 = " +f.getId().getCodcli();
				ps = cn.prepareStatement(sql);
				done = ps.executeUpdate() == 1 ;
				
			}
		
		} catch (Exception e) {
			done = false ;
			e.printStackTrace(); 
		}
		return done;
}
	
/**********MODIFICAR FACTURA EN EL F5503AM*******************************************************************/
	public boolean aplicarMontoF5503AM(Connection cn,double dMontopendienteDom,double dMontoPendienteForaneo,Vf0311fn f){
		boolean registrado = true;
		String sql  = "";
		PreparedStatement ps = null;
		
		Divisas d = new Divisas();
		try{
			Calendar cFecha = Calendar.getInstance();
			CalendarToJulian fecha = new CalendarToJulian(cFecha);
			int iFecha = fecha.getDate();
			//obtener hora en enteros
			
			sql = "UPDATE "+PropertiesSystem.GCPCXC+".F5503AM SET ATAAP = " + d.pasarAentero(dMontopendienteDom) + ",ATFAP = " + d.pasarAentero(dMontoPendienteForaneo) +  
					" WHERE ATKCOO = '000"+ f.getId().getCodsuc() + "' AND ATDCTO ='" + f.getId().getTiposol() + "'" +
							" AND ATDOCO = " + f.getId().getNosol() + " AND ATDFR = " + f.getId().getNocuota() + " AND ATAN8 = " +f.getId().getCodcli();
			ps = cn.prepareStatement(sql);
			int rs = ps.executeUpdate();
			if (rs != 1){
				registrado = false;
			}/*else{
				cn.commit();
			}*/
		}catch(Exception ex){
			registrado = false;
			ex.printStackTrace();
		}finally {
			try {
				ps.close();
				//cn.close();
			} catch (Exception se2) {
				se2.printStackTrace();
			}
		}
		return registrado;
	}
	/**************************************************************************************************************/
	public Vf0311fn getInfoFacturaFinan(int iNumfac,String sCodComp,String sTipoDocumento, String sPartida,String sCodunineg){
		Vf0311fn factura = null;
		FacturaxRecibo facturaRec = null;
		Session session = HibernateUtilPruebaCn.currentSession();
		
		//String sql = "from Hfacturajde as hf where hf.id.nofactura = " +iNumfac+" and hf.id.codcomp = '"+sCodComp.trim()+"' and hf.id.tipofactura = '"+sTipoDocumento+"' and hf.id.partida = '"+sPartida+"' and hf.id.ctotal > 0 and hf.id.cimpuesto > 0";
		String sql = "from Vf0311fn as hf where hf.id.nofactura = " +iNumfac+" and hf.id.tipofactura = '"+sTipoDocumento+"' and hf.id.partida = '"+sPartida+"' and hf.id.ctotal > 0  and trim(codunineg) = '" +sCodunineg+"'";
		try{
		
			factura = (Vf0311fn)(session.createQuery(sql).list()).get(0);
		
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			session.close();
		}
		return factura;
	}
/**********************************************************************************************/
public List leerFacturasReciboFinan2(int iCaid,String sCodComp,int iNumrec, 
						String sTiporec, int iCodcli){
		
		List lstFacturasRecibo = new ArrayList(), lstRecibofac = null;
		FacturaxRecibo facturaRec = null;
		Vf0311fn hfac = null;
		Recibofac recFac = null;
		Session session = HibernateUtilPruebaCn.currentSession();
		
		
		String sql = "from Recibofac as r where r.id.caid = " + iCaid
				+ " and r.id.codcomp = '" + sCodComp.trim()
				+ "' and r.id.numrec = " + iNumrec + " and r.id.tiporec = '"
				+ sTiporec + "' and r.id.codcli = " + iCodcli;
		try{
		
			lstRecibofac = session.createQuery(sql).list();
		
			for(int i = 0; i < lstRecibofac.size();i++){
				recFac = (Recibofac)lstRecibofac.get(i);
				hfac = getInfoFacturaFinan(recFac.getId().getNumfac(),sCodComp,recFac.getId().getTipofactura(),recFac.getId().getPartida(),recFac.getId().getCodunineg());
				JulianToCalendar fecha = new JulianToCalendar(hfac.getId().getFecha());
				//facturaRec = new FacturaxRecibo(hfac.getId().getNofactura(),hfac.getId().getTipofactura(),hfac.getId().getUnineg(),recFac.getMonto(),hfac.getId().getMoneda(),fecha.toString(),hfac.getId().getPartida());
				lstFacturasRecibo.add(hfac);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			session.close();
		}
		return lstFacturasRecibo;
	}
/**********************************************************************************************/
	public Recibofac getInfoCuotaFinan(Recibofac rf){
		String sql = "";
		Object[] obj = null;
		Session session = null;
		try{
			session = HibernateUtilPruebaCn.currentSession();
			
			sql = "SELECT CAST ((CAST(DATE(CHAR(1900000 + RPDIVJ)) AS TIMESTAMP)) AS DATE) FECHA," + 
					"CAST(rpcrcd as varchar(3) CCSID 37) MONEDA " +
					"from "+PropertiesSystem.JDEDTA+".F03B11 " +
					"where rpkco = '"+rf.getId().getCodsuc().trim()+"' and  rpdct = '"
						+rf.getId().getTipofactura()+"' and rpdcto = 'X9' and rpdoc = "
						+rf.getId().getNumfac()+" and rpsfx = '"
						+rf.getId().getPartida()+"' and rpdctm = ''" 
						+" and rpan8 = " + rf.getId().getCodcli();
			
			
			obj = (Object[])session.createSQLQuery(sql).uniqueResult();
			
			
			if(obj != null){
				rf.setFecha(obj[0].toString());
				rf.setMoneda(obj[1].toString());
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			session.close();
		}
		return rf;
	}
/**********************************************************************************************/
	public List leerFacturasReciboFinan(int iCaid,String sCodComp,int iNumrec,
						String sTiporec,int iCodcli){
		List lstResult = new ArrayList();
		List lstFacturasRecibo = new ArrayList(), lstRecibofac = null;
		Session session = null;
		try{
			
			FacturaxRecibo facturaRec = null;
			Finandet hfac = null;
			Recibofac recFac = null;
			session = HibernateUtilPruebaCn.currentSession();
			
			String sql = "";
			
			sql = "from Recibofac as r where r.id.caid = " + iCaid
					+ " and r.id.codcomp = '" + sCodComp.trim() + "' "
					+ " and r.id.numrec = " + iNumrec
					+ " and r.id.tiporec = '" + sTiporec + "' "
					+ " and r.id.codcli = " + iCodcli;
			
			
				lstRecibofac = session.createQuery(sql).list();
			
				for(int i = 0; i < lstRecibofac.size();i++){
					recFac = (Recibofac)lstRecibofac.get(i);
					recFac = getInfoCuotaFinan(recFac);
					facturaRec = new FacturaxRecibo(recFac.getId().getNumfac(),recFac.getId().getTipofactura(),recFac.getId().getCodunineg(),recFac.getMonto(),recFac.getMoneda(),recFac.getFecha(),recFac.getId().getPartida());
					lstFacturasRecibo.add(facturaRec);
				}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			session.close();
		}
		return lstFacturasRecibo;
	}
/**********************************************************************************************/
	public BigDecimal buscarInteresCorrientePend(Finandet f){
		String sql = "";
		Object obj = null;
		BigDecimal bdMonto = BigDecimal.ZERO;
		
		try{
			
			sql = 
			"select   (CASE WHEN RPCRCD = 'COR' THEN CAST(RPAAP/100 AS DECIMAL(10,2) ) ELSE CAST(RPFAP/100 AS DECIMAL(10,2) ) END) MONTOPEND " +
			" from " + PropertiesSystem.JDEDTA+".F03B11 "
					+ "INNER JOIN "+PropertiesSystem.JDEDTA+".F0006 UN ON RPMCU = UN.MCMCU  " +
			" where rpan8 = "+ f.getId().getCodcli()+" and rpaap > 0  and rpdct IN (select COD_DOCUMENTO from " + PropertiesSystem.GCPCXC + ".TIPOS_DOCUMENTOS_FINANCIAMIENTO "
					+ "where TIPO_AGRUPACION = 'ICR' and cod_compania = CAST(TRIM(UN.MCRP01) AS VARCHAR(3) CCSID 37) "
					+ "order by ORDEN_PROCESAMIENTO asc) " +
			" and cast(rppo as numeric(8) ) = " + f.getId().getNosol() + 
			" and rpdcto = '"+ f.getId().getTiposol()+"' " +
			" and rpkco = '" + f.getId().getCodsuc()+"' " +
			" and cast( rpsfx as numeric(8) ) in ("+ f.getId().getNocuota() + " ) "  ;
 
			//obj = ConsolidadoDepositosBcoCtrl.executeSqlQueryUnique(sql, null, true);
			
			LogCajaService.CreateLog("buscarInteresCorrientePend", "QRY", sql);
			
			obj = new RoQueryManager().executeSqlQueryUnique(sql, null, true);
			
			return (obj == null)? BigDecimal.ZERO :  new BigDecimal( String.valueOf( obj ) ) ;
			
		}catch(Exception ex){
			
			bdMonto = BigDecimal.ONE.negate();
			LogCajaService.CreateLog("buscarInteresCorrientePend", "ERR", ex.getMessage());
			ex.printStackTrace(); 
		} 
		return bdMonto;
	}
	/********************APLICAR PAGO A CUOTA EN EL MODULO DE FINANCIAMIENTO*******************************************************************/
	public boolean aplicarPagoCuotaModFinan1(Connection cn,Finandet fd,int iMontoD,int iMontoF){
		boolean bHecho = true;
		String sql  = "";
		PreparedStatement ps = null;
		try{
			
			if(iMontoD < 0) iMontoD = 0 ;
			if(iMontoF < 0) iMontoF = 0 ;
			
			sql = "UPDATE " + PropertiesSystem.GCPCXC + ".F5503AM set ataap = "
					+ iMontoD + ",atfap = " + iMontoF + " where atan8 = "
					+ fd.getId().getCodcli() + " and atkcoo = '"
					+ fd.getId().getCodsuc() + "' and trim(atdcto) = '"
					+ fd.getId().getTiposol() + "' and atdfr = "
					+ fd.getId().getNocuota() + " and atdoco = "
					+ fd.getId().getNosol();
			
			ps = cn.prepareStatement(sql);
			int rs = ps.executeUpdate();
			if (rs != 1){
				bHecho = false;
			}
		}catch(Exception ex){
			bHecho = false;
			ex.printStackTrace();
		}finally {
			try {
				ps.close();
			} catch (Exception se2) {
				se2.printStackTrace();
				 
			}
		}
		return bHecho;
	}
	public boolean aplicarPagoCuotaModFinan(Session session, Finandet fd,int iMontoD,int iMontoF, String usuario){
		boolean bHecho = true;
		
		try{
			
			if(iMontoD < 0) iMontoD = 0 ;
			if(iMontoF < 0) iMontoF = 0 ;
			
			String update = 
				" UPDATE @BDGCPCXC@.F5503AM  " +
				" SET " +
					" ATAAP = '@ATAAP@' ," +
					" ATFAP = '@ATFAP@' ," +
					" ATUSER = '@ATUSER@' ," +
					" ATUSERM ='@ATUSER@' ,"  +
					" ATUPMJ = '@ATUPMJ@' ," +
					" ATUPMT = '@ATUPMT@' ," +
					" ATUJOB = '@ATUJOB@' ," +
					" ATUPID = '@ATUPID@' ," +
					" ATUPIDM ='@ATUPID@' " +
				"WHERE " +
					" atan8 = " + fd.getId().getCodcli() + 
					" and atkcoo = '" + fd.getId().getCodsuc() + "'" +
					" and trim(atdcto) = '" + fd.getId().getTiposol() + "'" +
					" and atdfr = " + fd.getId().getNocuota() + 
					" and atdoco = " + fd.getId().getNosol();
			
			update = update
				.replace("@BDGCPCXC@", PropertiesSystem.GCPCXC )
				.replace("@ATAAP@",  String.valueOf(iMontoD) )
				.replace("@ATFAP@",  String.valueOf(iMontoF) )
				.replace("@ATUSER@", usuario.trim().toUpperCase() )
				.replace("@ATUPMJ@", String.valueOf( FechasUtil.dateToJulian(new Date() ) ) )
				.replace("@ATUPMT@", new SimpleDateFormat("HHmmss").format(new Date() ) )
				.replace("@ATUJOB@", "RFINANCIA" )
				.replace("@ATUPID@", PropertiesSystem.CONTEXT_NAME  ) ;
			
			
			//atuser, atupmj, atupmt, atujob, atupid
//			String sql  = "UPDATE " 
//					+ PropertiesSystem.GCPCXC + ".F5503AM set ataap = "
//					+ iMontoD + ",atfap = " + iMontoF + " where atan8 = "
//					+ fd.getId().getCodcli() + " and atkcoo = '"
//					+ fd.getId().getCodsuc() + "' and trim(atdcto) = '"
//					+ fd.getId().getTiposol() + "' and atdfr = "
//					+ fd.getId().getNocuota() + " and atdoco = "
//					+ fd.getId().getNosol();
			
			LogCajaService.CreateLog("aplicarPagoCuotaModFinan", "QRY", update);
			
			int rows = session.createSQLQuery(update).executeUpdate() ;
		 
			if (rows != 1){
				bHecho = false;
			}
			
		}catch(Exception ex){
			LogCajaService.CreateLog("aplicarPagoCuotaModFinan", "ERR", ex.getMessage());
			bHecho = false;
			ex.printStackTrace(); 
		} 
		return bHecho;
	}
	
	/********************LLENAR ENLACE ENTRE RECIBO Y FACTURA*********************************************************************************/
	public boolean fillEnlaceReciboFac(Session session,Transaction tx,int iNumRec,
					String codcomp, int iNumFac,double Monto,String sTipoDoc,
					int iCaId,String sCodSuc,int iPartida, String sCodunineg,
					String sTiporec, int iCodcli, int iFecha){
		boolean filled = false;
		
		//Session session = HibernateUtil.getSessionFactoryMCAJA().openSession();
		//Transaction tx = null;
		
		try {
				String sPartida = this.formatNocuota(iPartida);

				Recibofac recibofac = new Recibofac();
				RecibofacId recibofacid = new RecibofacId();
				
				recibofacid.setNumfac(iNumFac);					//Numero factura
				recibofacid.setNumrec(iNumRec);						//Numero Recibo
				recibofacid.setTipofactura(sTipoDoc);			//Tipo de documento
				recibofacid.setCodcomp(codcomp);					//Cod. Compañía
				recibofacid.setPartida(sPartida);				//Partida en blanco para contado
				recibofacid.setCaid(iCaId);
				recibofacid.setCodsuc(sCodSuc);
				recibofacid.setCodunineg(sCodunineg.trim());
				recibofacid.setTiporec(sTiporec);
				recibofacid.setCodcli(iCodcli);
				recibofacid.setFecha(iFecha);
				
				recibofac.setId(recibofacid);						//Recibo
				recibofac.setMonto(BigDecimal.valueOf(Monto));   //Monto
				recibofac.setEstado("");
				session.save(recibofac);							//Factura
			
			//tx.commit();
			filled = true;	
		}catch(Exception ex){
			ex.printStackTrace();
		}/*finally{
			session.close();
		}*/
		return filled;
	}
/*************APLICAR PAGO A FACTURA EN EL F03B11**************************/
	public boolean aplicarMontoF03B11(Connection cn,String sEstadoPago,int dMontopendienteDom,int dMontoPendienteForaneo,String sUsuario,
									String sCodSuc, String sTipoFactura,int iNofactura, int iPartida){
		boolean registrado = true;
		String sql  = "";
		PreparedStatement ps = null;
		Date dHora = new Date();
		SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try{
			String sPartida = formatNocuota(iPartida);
			
			Calendar cFecha = Calendar.getInstance();
			CalendarToJulian fecha = new CalendarToJulian(cFecha);
			int iFecha = fecha.getDate();
			//obtener hora en enteros
			String[] sHora = (dfHora.format(dHora)).split(":");
			int iHora = Integer.parseInt(sHora[0] + sHora[1] + sHora[2]);
			//obtener partes de la fecha
			Date dFecha = new Date();
			String[] sFecha = (sdf.format(dFecha)).split("/");
			sql = "UPDATE "+PropertiesSystem.JDEDTA+".F03B11 SET RPPST = '"+ sEstadoPago +"', RPAAP = " + dMontopendienteDom  + ",RPFAP = " + dMontoPendienteForaneo + "," + 
					"RPUSER = '" + sUsuario +"', RPUPMJ = " + iFecha + ", RPUPMT = " + iHora + " WHERE RPKCO = '"+ sCodSuc + "' AND RPDCT ='" + sTipoFactura + "'" +
							" AND RPDOC = " + iNofactura + " AND RPSFX = '" + sPartida + "' AND TRIM(RPDCTM) = '' AND RPDOCM = 0";
			ps = cn.prepareStatement(sql);
			int rs = ps.executeUpdate();
			if (rs != 1){
				registrado = false;
			}/*else{
				cn.commit();
			}*/
		}catch(Exception ex){
			registrado = false;
			ex.printStackTrace();
		}finally {
			try {
				ps.close();
				//cn.close();
			} catch (Exception se2) {
				se2.printStackTrace();
			}
		}
		return registrado;
	}	
/************************************************************************************************************************/
public boolean grabarRCFinan(Connection cn,Finandet fd, String sTipoRecibo,int iNoreciboJDE,int iFechaRecibo, String sTipoBatch, int iNobatch, int dMontoAplicar,String sModoMoneda,double dTasa,
			int dMontoAplicarForaneo,
			String sGlOffset,String sIdCuenta, String sTipoPago,String sTipoEntrada, String sConcepto,String sCashBasis,String sUsuario,String sAplicacion){
	boolean registrado = true;
	String sql  = "INSERT INTO "+PropertiesSystem.JDEDTA+".F03B11 (RPKCO,RPAN8,RPDCT,RPDOC,RPSFX,RPDIVJ,RPDCTM,RPDOCM,RPDMTJ,RPDGJ,RPFY,RPCTRY,RPPN,RPCO,RPICUT,RPICU," +
		"RPDICJ,RPPA8,RPAN8J,RPBALJ,RPPST,RPAG,RPAAP,RPCRRM,RPCRCD,RPCRR,RPACR,RPFAP,RPDSVJ,RPGLC,RPGLBA,RPAM,RPMCU,RPPTC,RPDDJ,RPDDNJ," +
		"RPTRTC,RPRMK,RPALT6,RPALPH,RPATE,RPATR,RPATP,RPATO,RPATPR,RPAT1,RPAT2,RPAT3,RPAT4,RPAT5,RPTORG,RPUSER,RPPID,RPUPMJ,RPUPMT,RPJOBN," +
		"RPAC01,RPAC02,RPAC03,RPAC04,RPAC05,RPAC06,RPAC07,RPAC08,RPAC09,RPAC10) VALUES(";
	PreparedStatement ps = null;
	Date dHora = new Date();
	SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	String sNombrePc = null;
	EmpleadoCtrl empCtrl = new EmpleadoCtrl();
	Vf0101 vf0101 = null;
	String sCadValida = "";
	Divisas d = new Divisas();
	try{	
		Calendar cFecha = Calendar.getInstance();
		CalendarToJulian fecha = new CalendarToJulian(cFecha);
		int iFecha = fecha.getDate();
		
		String sPartida = formatNocuota(fd.getId().getNocuota());
		
		sNombrePc = InetAddress.getLocalHost().getHostName();
		//obtener hora en enteros
		String[] sHora = (dfHora.format(dHora)).split(":");
		int iHora = Integer.parseInt(sHora[0] + sHora[1] + sHora[2]);
		//obtener partes de la fecha
		Date dFecha = new Date();
		String[] sFecha = (sdf.format(dFecha)).split("/");
		
		if(sConcepto.length() > 30){
			sConcepto = sConcepto.substring(0,29);
		}
		if(sNombrePc.length() > 9){
			sNombrePc =	sNombrePc.substring(0, 9);
		}
		//BUSCAR INFO DEL CLIENTE
		vf0101 = empCtrl.buscarEmpleadoxCodigo2(fd.getId().getCodcli());
		
		String sNomcli = vf0101.getId().getAbalph();
		sCadValida = d.remplazaCaractEspeciales(sNomcli, "'", "-");
		if(!sCadValida.equals("")){
			sNomcli = sCadValida;
		}
		sql = sql +
		"'"+fd.getId().getCodsuc()+"'," + fd.getId().getCodcli() + "," + "'"+ fd.getRpdct() +"'," + fd.getRpdoc()+"," + "'"+sPartida+"'," + fd.getRpdivj()+"," + "'"+sTipoRecibo+"'," +
		iNoreciboJDE + "," + iFechaRecibo + "," + iFechaRecibo + "," + ""+Integer.parseInt(sFecha[2].substring(2, 4))+"," +"20," + ""+Integer.parseInt(sFecha[1])+"," +
		"'"+fd.getId().getCodsuc()+"'," + "'"+sTipoBatch+"'," + iNobatch+"," + iFechaRecibo + "," + fd.getId().getCodcli() + "," + fd.getId().getCodcli() + ",'Y'," + "'P'," + (dMontoAplicar * -1) + ",0," + 
		"'"+sModoMoneda+"'," + "'"+ fd.getId().getMoneda()+"'," + dTasa + "," + (dMontoAplicarForaneo* -1)  + ",0," + iFechaRecibo + "," + "'"+sGlOffset+"'," +
		"'"+sIdCuenta+"'," + "'2'," + "'"+ fd.getRpmcu()+"'," + "'"+sTipoPago+"'," + fd.getId().getAtpayd() + "," + fd.getId().getAtpayd() + "," + "'" + sTipoEntrada + "'," +
		"'" + sConcepto + "'," + "'" + sCashBasis + "'," + "'" + sNomcli+ "'," + "'" + vf0101.getId().getAbate() + "'," + "'" + vf0101.getId().getAbatr() + "'," +
		"'" + vf0101.getId().getAbatp() + "'," + "'Y'," + "'" + vf0101.getId().getAbatpr() + "'," + "'" + vf0101.getId().getAbat1() + "'," +
		"'" + vf0101.getId().getAbat2() + "'," + "'" + vf0101.getId().getAbat3() + "'," + "'" + vf0101.getId().getAbat4() + "'," + "'" + vf0101.getId().getAbat5() + "'," +
		"'" + sUsuario + "'," + "'" + sUsuario + "'," + "'" + sAplicacion + "'," + iFechaRecibo + "," + iHora + "," +"'"+sNombrePc+"'," + 
		"'" + vf0101.getId().getAbac01() + "'," + "'" + vf0101.getId().getAbac02() + "'," + "'" + vf0101.getId().getAbac03() + "'," + "'" + vf0101.getId().getAbac04() + "'," +
		"'" + vf0101.getId().getAbac05() + "'," + "'" + vf0101.getId().getAbac06() + "'," + "'" + vf0101.getId().getAbac07() + "'," + "'" + vf0101.getId().getAbac08() + "'," +
		"'" + vf0101.getId().getAbac09() + "'," + "'" + vf0101.getId().getAbac10() + "'" +
		")";	
		ps = cn.prepareStatement(sql);
		int rs = ps.executeUpdate();
		if (rs != 1){
			registrado = false;
		}
	}catch(Exception ex){
		registrado = false;
		ex.printStackTrace();
	}finally {
		try {
			ps.close();
			//cn.close();
		} catch (Exception se2) {
			se2.printStackTrace();
		}
	}
	return registrado;
}
/*********************************************************************************************/	
/************************************************************************************************************************/
	public List leerDocumentosxCuota(Finandet fd){
		List lstCuotas = new ArrayList(),lstResult = null;
		Session s = null;
		Transaction tx = null;
		String sql = "";
		Object[] obj = null;
		Finandet nFd = null;
		FinandetId nFdid = null;
		try{
			sql = "select rpdoc,CAST(TRIM(RPDCT) AS VARCHAR(2) CCSID 37) RPDCT,rpdivj,CAST(RPGLC AS VARCHAR(4) CCSID 37) AS COMPENSLM,CAST(rpmcu as varchar(12) CCSID 37) as rpmcu,CAST(TRIM(RPPTC) AS VARCHAR(3) CCSID 37) TIPOPAGO,cast(rpalph as varchar(40) CCSID 37) rpalph,"+
				  " (CASE WHEN RPCRCD = 'COR' THEN CAST(RPAAP/100 AS DECIMAL(10,2)) ELSE CAST(RPFAP/100 AS DECIMAL(10,2)) END) MONTOPEND,RPCRR,CAST(RPAAP/100 AS DECIMAL(10,2)) "+
				  " from "+PropertiesSystem.JDEDTA+".F03B11 INNER JOIN "+PropertiesSystem.JDEDTA+".F0006 UN ON RPMCU = UN.MCMCU   where rpdcto = '"+fd.getId().getTiposol()+"' and rpaap > 0 and rpkco = '"+fd.getId().getCodsuc()+"' and rpan8 = "+fd.getId().getCodcli()+" and CAST(rpsfx AS NUMERIC(8)) = "+fd.getId().getNocuota()+" and rpdct IN (select COD_DOCUMENTO from " + PropertiesSystem.GCPCXC + ".TIPOS_DOCUMENTOS_FINANCIAMIENTO where TIPO_AGRUPACION = 'IMO' and cod_compania = CAST(TRIM(UN.MCRP01) AS VARCHAR(3) CCSID 37) order by ORDEN_PROCESAMIENTO asc) and CAST(RPPO AS NUMERIC(8)) = "+fd.getId().getNosol() + 
				  " union " +
				  "select rpdoc,CAST(TRIM(RPDCT) AS VARCHAR(2) CCSID 37) RPDCT,rpdivj,CAST(RPGLC AS VARCHAR(4) CCSID 37) AS COMPENSLM,CAST(rpmcu as varchar(12) CCSID 37) as rpmcu,CAST(TRIM(RPPTC) AS VARCHAR(3) CCSID 37) TIPOPAGO,cast(rpalph as varchar(40) CCSID 37) rpalph,"+
				  " (CASE WHEN RPCRCD = 'COR' THEN CAST(RPAAP/100 AS DECIMAL(10,2)) ELSE CAST(RPFAP/100 AS DECIMAL(10,2)) END) MONTOPEND,RPCRR,CAST(RPAAP/100 AS DECIMAL(10,2)) "+
				  " from "+PropertiesSystem.JDEDTA+".F03B11 INNER JOIN "+PropertiesSystem.JDEDTA+".F0006 UN ON RPMCU = UN.MCMCU   where rpdcto = '"+fd.getId().getTiposol()+"' and rpaap > 0 and rpkco = '"+fd.getId().getCodsuc()+"' and rpan8 = "+fd.getId().getCodcli()+" and CAST(rpsfx AS NUMERIC(8)) = "+fd.getId().getNocuota()+" and rpdct IN (select COD_DOCUMENTO from " + PropertiesSystem.GCPCXC + ".TIPOS_DOCUMENTOS_FINANCIAMIENTO where TIPO_AGRUPACION = 'ICR' and cod_compania = CAST(TRIM(UN.MCRP01) AS VARCHAR(3) CCSID 37) order by ORDEN_PROCESAMIENTO asc) and CAST(RPPO AS NUMERIC(8)) = "+fd.getId().getNosol() + 
				  " union " +
				  "select rpdoc,CAST(TRIM(RPDCT) AS VARCHAR(2) CCSID 37) RPDCT,rpdivj,CAST(RPGLC AS VARCHAR(4) CCSID 37) AS COMPENSLM,CAST(rpmcu as varchar(12) CCSID 37) as rpmcu,CAST(TRIM(RPPTC) AS VARCHAR(3) CCSID 37) TIPOPAGO,cast(rpalph as varchar(40) CCSID 37) rpalph,"+
				  " (CASE WHEN RPCRCD = 'COR' THEN CAST(RPAAP/100 AS DECIMAL(10,2)) ELSE CAST(RPFAP/100 AS DECIMAL(10,2)) END) MONTOPEND,RPCRR,CAST(RPAAP/100 AS DECIMAL(10,2)) "+
				  " from "+PropertiesSystem.JDEDTA+".F03B11 INNER JOIN "+PropertiesSystem.JDEDTA+".F0006 UN ON RPMCU = UN.MCMCU   where rpdcto = '"+fd.getId().getTiposol()+"' and rpaap > 0 and rpkco = '"+fd.getId().getCodsuc()+"' and rpan8 = "+fd.getId().getCodcli()+" and CAST(rpsfx AS NUMERIC(8)) = "+fd.getId().getNocuota()+" and rpdct not in (select COD_DOCUMENTO from " + PropertiesSystem.GCPCXC + ".TIPOS_DOCUMENTOS_FINANCIAMIENTO where TIPO_AGRUPACION IN ('IMO', 'ICR') and cod_compania = CAST(TRIM(UN.MCRP01) AS VARCHAR(3) CCSID 37) order by ORDEN_PROCESAMIENTO asc) and CAST(RPPO AS NUMERIC(8)) = "+fd.getId().getNosol();
			s = HibernateUtilPruebaCn.currentSession();
			
			lstResult = (List)s.createSQLQuery(sql).list();			
			
			for(int i = 0;i < lstResult.size();i++){
				nFd = new Finandet();
				nFdid = new FinandetId();
				nFdid = fd.getId();
				nFd.setId(nFdid);
				nFd.setConsolidado(fd.getConsolidado());
				nFd.setMontoAplicar(fd.getMontoAplicar());
				nFd.setMontopend(fd.getMontopend());
				nFd.setMora(fd.getMora());
				
				obj = (Object[])lstResult.get(i);
				
				nFd.setRpdoc(Integer.parseInt(obj[0].toString()));
				nFd.setRpdct(obj[1].toString());
				nFd.setRpdivj(Integer.parseInt(obj[2].toString()));
				nFd.setCompenslm(obj[3].toString());
				nFd.setRpmcu(obj[4].toString());
				nFd.setTipopago(obj[5].toString());
				nFd.setNomcli(obj[6].toString());
				nFd.setMontopJDE(new BigDecimal(obj[7].toString()));
				nFd.setTasaJDE(new BigDecimal(obj[8].toString()));
				nFd.setCpendiente(new BigDecimal(obj[9].toString()));
				lstCuotas.add(nFd);
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			s.close();
		}
		return lstCuotas;
	}
/****************************************************************************************/
/** Método: Guardar Registro en el GCPPRRDTA.F0011 para financiamientos.
 * 		  : Versión para JD Edward's 9.2
 *	Fecha:  16/08/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 **/
	public boolean insertarAsientoInteres(Connection cn,Finanhdr fh,Finandet fd,String  sTipodoc,int iNodoc,
			int iFecha, int iNobatch,BigDecimal bdTasa,String sUsuario,String sAplicacion,
			String[] sCuenta,String sTipolibro,int iMonto, String sGlglc,
			String sGlbcrc,String sGlhco, String sGlcrrm){
		boolean bHecho = true;
		PreparedStatement ps = null;
		String sql = "INSERT INTO "+PropertiesSystem.JDEDTA+".F0911 (GLKCO,GLDCT,GLDOC,GLDGJ,GLJELN,GLICU,GLICUT,GLDICJ,GLDSYJ,GLTICU,GLCO,GLANI,"+
							   "GLAM,GLAID,GLMCU,GLOBJ,GLSUB,GLLT,GLPN,GLCTRY,GLFY,GLCRCD,GLCRR,GLAA," +
							   "GLGLC,GLEXA,GLEXR,GLR1,GLR2,GLAN8,GLDKJ,GLVINV,GLIVD,GLPO,GLDCTO," +
							   "GLLNID,GLDSVJ,GLTORG,GLUSER,GLPID,GLJOBN,GLUPMJ,GLUPMT," +
							   "GLBCRC,GLHCO,GLCRRM ) VALUES(";
		Date dHora = new Date();
		SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String sNombrePc = null;	
		String sCodunineg = "";
		String sConcepto = "",sNosol = "";
		String sNomcli = "";
	
		try{	
			
			if(sTipodoc.equals("MF")){
				sConcepto = "C:Int.morat,Doc:";
			}else{
				sConcepto = "C:Int+ Imp,Doc:";
			}
			
			sNombrePc = InetAddress.getLocalHost().getHostName();
			 
			String[] sHora = (dfHora.format(dHora)).split(":");
			int iHora = Integer.parseInt(sHora[0] + sHora[1] + sHora[2]);
		 
			Date dFecha = new Date();
			String[] sFecha = (sdf.format(dFecha)).split("/");
			if(sNombrePc.length() > 9){
				sNombrePc =	sNombrePc.substring(0, 9);
			}
			
			//&& ================ Generar la unidad de negocios a utilizar. (52 codigo configurado para financimientos de credi-pellas)
			sCodunineg =  CompaniaCtrl.leerUnidadNegocioPorLineaSucursal(fh.getId().getCodsuc(), "52" );
			if( sCodunineg.isEmpty() ){
				sCodunineg = fh.getId().getCodsuc().substring(3, 5) + fh.getId().getLinea();
			}
			sCodunineg = CodeUtil.pad(sCodunineg.trim(), 12);
			
			sConcepto = sConcepto+fh.getId().getNosol()+" Cuot:" + fd.getId().getNocuota();
			//no de solicitud
			sNosol = formatNosolicitud(fh.getId().getNosol());
			//nombre de cliente
			sNomcli = fh.getId().getNomcli().trim();
			if(sNomcli.length() > 29){
				sNomcli = sNomcli.substring(0, 29);
			}
		
			sql = sql +
					"'"+fh.getId().getCodsuc()+"'," +
					"'"+sTipodoc+"'," +
					""+iNodoc+"," +
					""+iFecha+"," +
					""+((fd.getId().getNocuota())*1.0)+"," +
					""+iNobatch+"," +
					"'I'," +
					""+iFecha+"," +
					""+iFecha+"," +
					""+iHora+"," +
					"'"+fh.getId().getCodsuc()+"'," +
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
					"'"+fh.getId().getMoneda()+"'," +
					bdTasa + "," +
					iMonto + "," +
					"'"+sGlglc+"'," +
					"'"+sNomcli+"'," +
					"'"+sConcepto+"'," +
					"'"+sNosol+"'," +
					"'MCAJA'," +
					fh.getId().getCodcli() + "," +
					""+iFecha+"," +
					"'"+iNodoc+"'," +
					""+iFecha+"," +
					"'"+sNosol+"'," +
					"'"+fh.getId().getTiposol()+"'," +
					""+1000+"," +
					""+iFecha+"," +
					"'"+sUsuario+"'," +
					"'"+sUsuario+"'," +
					"'"+sAplicacion+"'," +
					"'"+sAplicacion+"'," +
					""+iFecha+"," +
					""+iHora+"," +
					"'"+sGlbcrc+"'," +
					"'"+fh.getId().getCodsuc()+"'," +     //"'000"+sGlhco+"'," +
					"'"+sGlcrrm+"'" +
					")";
			
			ps = cn.prepareStatement(sql);
			int rs = ps.executeUpdate();
			if (rs != 1){
				bHecho = false;
			}
		}catch(Exception ex){
			ex.printStackTrace(); 
			bHecho = false;
		}
		return bHecho;
	}
/**Insertar Asiento de MF******************************************************/
	public boolean insertarAsientoInteres1(Connection cn,Finanhdr fh,Finandet fd,String  sTipodoc,int iNodoc,
										int iFecha, int iNobatch,BigDecimal bdTasa,String sUsuario,String sAplicacion,
										String[] sCuenta,String sTipolibro,int iMonto, String sGlglc){
		boolean bHecho = true;
		PreparedStatement ps = null;
		String sql = "INSERT INTO "+PropertiesSystem.JDEDTA+".F0911 (GLKCO,GLDCT,GLDOC,GLDGJ,GLJELN,GLICU,GLICUT,GLDICJ,GLDSYJ,GLTICU,GLCO,GLANI,"+
												   "GLAM,GLAID,GLMCU,GLOBJ,GLSUB,GLLT,GLPN,GLCTRY,GLFY,GLCRCD,GLCRR,GLAA," +
												   "GLGLC,GLEXA,GLEXR,GLR1,GLR2,GLAN8,GLDKJ,GLVINV,GLIVD,GLPO,GLDCTO," +
												   "GLLNID,GLDSVJ,GLTORG,GLUSER,GLPID,GLJOBN,GLUPMJ,GLUPMT ) VALUES(";
		Date dHora = new Date();
		SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String sNombrePc = null;	
		String sCodunineg = "";
		String sConcepto = "",sNosol = "";
		String sNomcli = "";
		try{	
			if(sTipodoc.equals("MF")){
				sConcepto = "Int.morat,Doc:";
			}else{
				sConcepto = "Int+ Imp,Doc:";
			}
			sNombrePc = InetAddress.getLocalHost().getHostName();
			//obtener hora en enteros
			String[] sHora = (dfHora.format(dHora)).split(":");
			int iHora = Integer.parseInt(sHora[0] + sHora[1] + sHora[2]);
			//obtener partes de la fecha
			Date dFecha = new Date();
			String[] sFecha = (sdf.format(dFecha)).split("/");
			if(sNombrePc.length() > 9){
				sNombrePc =	sNombrePc.substring(0, 9);
			}
			//agregar blancos a unineg
			sCodunineg = fh.getId().getCodsuc().substring(3, 5) + fh.getId().getLinea();
			if(sCodunineg.length() == 2){
				sCodunineg = "          "+sCodunineg;
			}else{
				sCodunineg = "        "+sCodunineg;
			}
			sConcepto = sConcepto+fh.getId().getNosol()+" Cuot:" + fd.getId().getNocuota();
			//no de solicitud
			sNosol = formatNosolicitud(fh.getId().getNosol());
			//nombre de cliente
			sNomcli = fh.getId().getNomcli().trim();
			if(sNomcli.length() > 29){
				sNomcli = sNomcli.substring(0, 29);
			}
			
			sql = sql +
			"'"+fh.getId().getCodsuc()+"'," +
			"'"+sTipodoc+"'," +
			""+iNodoc+"," +
			""+iFecha+"," +
			""+((fd.getId().getNocuota())*1.0)+"," +
			""+iNobatch+"," +
			"'I'," +
			""+iFecha+"," +
			""+iFecha+"," +
			""+iHora+"," +
			"'"+fh.getId().getCodsuc()+"'," +
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
			"'"+fh.getId().getMoneda()+"'," +
			bdTasa + "," +
			iMonto + "," +
			"'"+sGlglc+"'," +
			"'"+sNomcli+"'," +
			"'"+sConcepto+"'," +
			"'"+sNosol+"'," +
			"'MCAJA'," +
			fh.getId().getCodcli() + "," +
			""+iFecha+"," +
			"'"+iNodoc+"'," +
			""+iFecha+"," +
			"'"+sNosol+"'," +
			"'"+fh.getId().getTiposol()+"'," +
			""+1000+"," +
			""+iFecha+"," +
			"'"+sUsuario+"'," +
			"'"+sUsuario+"'," +
			"'"+sAplicacion+"'," +
			"'"+sAplicacion+"'," +
			""+iFecha+"," +
			""+iHora +
			")";

			
			ps = cn.prepareStatement(sql);
			int rs = ps.executeUpdate();
			if (rs != 1){
				bHecho = false;
			}
		}catch(Exception ex){
			bHecho = false;
			ex.printStackTrace();
		}
		return bHecho;
	}
/**buscar cuenta a usar para asiento de interes moratorio**********************************************************/
	
	public static List<String[]> cuentaIntereses(Finanhdr fh, String tipoInteres ){
		List<String[]> cuentasFormasPago = null;
		
		try {
		
			String unineg =  CompaniaCtrl.leerUnidadNegocioPorLineaSucursal( fh.getId().getCodsuc(), "52" );
			if( unineg.isEmpty() ){
				unineg = fh.getId().getCodsuc().substring(3, 5) + fh.getId().getLinea();
			}
			String sCodunineg = CodeUtil.pad(unineg, 12);
			
			String query =  
			"select  "+
			" '"+sCodunineg+"' ||'.'|| trim( mlobj  ) ||'.'||  trim( mlsub ) ||'@@@' || "+
			"(select gmaid from "+PropertiesSystem.ESQUEMA+".vf0901 where trim('"+sCodunineg+"') = trim(gmmcu) and trim( mlobj ) = trim(gmobj) and trim( mlsub ) = trim(gmsub) ) ||'@@@' || "+
			"(select gmco from  "+PropertiesSystem.ESQUEMA+".vf0901 where trim('"+sCodunineg+"') = trim(gmmcu) and trim( mlobj ) = trim(gmobj) and trim( mlsub ) = trim(gmsub) ) ||'@@@'  || "+
			" '"+sCodunineg+"' ||'@@@'||   "+
			" trim( mlobj ) ||'@@@'||  "+
			" trim( mlsub ) ||'@@@'  "+
			" from  "+PropertiesSystem.JDEDTA+".f4095  "+
			" where mlanum = 4390 and mldct = '"+fh.getId().getTiposol() +"'  and mlglpt = '"+tipoInteres+"' and trim(mldcto)  = '' and mlco = '00000'  " ;
			
			@SuppressWarnings("unchecked")
			List<String> dtaQueryExecuted = (ArrayList<String>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, true, null);
 
			cuentasFormasPago =  new ArrayList<String[]>();
			for (String dtaCta : dtaQueryExecuted) {
				cuentasFormasPago.add(dtaCta.split("@@@"));
			}
		} catch (Exception e) {
			cuentasFormasPago = null;
			e.printStackTrace();
		}
		return cuentasFormasPago;
		
	}
	
	public String[] buscarCuentaIntereses(Finanhdr fh, String sMlglpt){
		String sCuentaMF[] = new String[6];
		String sql ="";
		String sCompCtaPos, sIdCtaPos, sCuenta;
		String sCxmcu, sCxobj,sCxsub;
		
		Session sesion = null;
		Transaction trans = null;
		boolean newCn = false;
		String sCodunineg = "";
		
		try{
			
			sesion = HibernateUtilPruebaCn.currentSession();
			trans = (newCn = !(sesion.getTransaction().isActive())) ? sesion
					.beginTransaction() : sesion.getTransaction();
			
			//&& ================ Generar la unidad de negocios a utilizar. (52 codigo configurado para financimientos de credi-pellas)
		 	String unineg =  CompaniaCtrl.leerUnidadNegocioPorLineaSucursal( fh.getId().getCodsuc(), "52" );
			if( unineg.isEmpty() ){
				unineg = fh.getId().getCodsuc().substring(3, 5) + fh.getId().getLinea();
			}
			sCodunineg = CodeUtil.pad(unineg, 12);	
					
			
			sql = "select cast(mlmcu as varchar(12) ccsid 37)mlmcu,cast(mlobj as varchar(10) ccsid 37) mlobj,cast(mlsub as varchar(10) ccsid 37) mlsub" +
					" from "+PropertiesSystem.JDEDTA+".f4095lb where mlanum = 4390 and mldct = '"+ fh.getId().getTiposol() + "' and mlglpt = '"+sMlglpt+"' and trim(mldcto) = '' and mlco = '"+fh.getId().getCodsuc()+"'";			
			
			Object[] obj = (Object[]) sesion.createSQLQuery(sql).uniqueResult();
			
			if(obj!=null){
				
				
				sCxmcu = obj[0].toString().trim();
				sCxobj = obj[1].toString().trim();
				sCxsub = obj[2].toString().trim();
				
				//&& ================ Asignar la unidad de negocios para la cuenta 50 Credipellas
				if( !sCodunineg.isEmpty() )
					sCxmcu = sCodunineg ;
				
				//compañía de la cuenta.
				if(sCxmcu.trim().length()==4)
					sCompCtaPos = sCxmcu.trim().substring(0,2);
				else
					sCompCtaPos = sCxmcu.trim();
		
				//cuenta completa: UN + Objeto + Subsidiaria.
				sCuenta  = sCxmcu +"."+sCxobj;				
				if(sCxsub != null && !sCxsub.equals(""))
					sCuenta += "."+sCxsub;
				else
					sCxsub = "";
				
				//obtener el id de la cuenta de caja.
				ReciboCtrl rcCtrl = new ReciboCtrl();
				sIdCtaPos = rcCtrl.obtenerIdCuenta(sesion, trans, sCxmcu, sCxobj, sCxsub);
				
				if(sIdCtaPos != null &&(sCuenta != null && sIdCtaPos != null && sCompCtaPos != null && sCxmcu != null && sCxobj != null && sCxsub != null)){
					sCuentaMF[0] = sCuenta;
					sCuentaMF[1] = sIdCtaPos;
					sCuentaMF[2] = sCompCtaPos;
					sCuentaMF[3] = sCxmcu;
					sCuentaMF[4] = sCxobj;
					sCuentaMF[5] = sCxsub;			
				}else{
					sCuentaMF = null;
					System.out.println("Error al leer el id de la cuenta de venta ");
				}				
				
			}else{
				
				sql = "select cast(mlmcu as varchar(12) ccsid 37)mlmcu,cast(mlobj as varchar(10) ccsid 37) mlobj,cast(mlsub as varchar(10) ccsid 37) mlsub" +
				" from "+PropertiesSystem.JDEDTA+".f4095lb where mlanum = 4390 and mldct = '"+fh.getId().getTiposol()+"' and mlglpt = '"+sMlglpt+"' and trim(mldcto) = '' and mlco = '00000'";	
				
				Object[] obj2 = (Object[])sesion.createSQLQuery(sql).uniqueResult();
				
				if(obj2!=null){
					
					sCxmcu = obj2[0].toString().trim();
					sCxobj = obj2[1].toString().trim();
					sCxsub = obj2[2].toString().trim();
					
					sCxmcu = fh.getId().getCodsuc().substring(3, 5) + fh.getId().getLinea();
					sCxmcu = CodeUtil.pad(sCxmcu, 12);

					//&& ================ Asignar la unidad de negocios para la cuenta 50 Credipellas
					if( !sCodunineg.isEmpty())
						sCxmcu = sCodunineg ;
					
					
					//compañía de la cuenta.
					if(sCxmcu.trim().length() == 4)
						sCompCtaPos = sCxmcu.trim().substring(0,2);
					else
						sCompCtaPos = sCxmcu.trim();
			
					//cuenta completa: UN + Objeto + Subsidiaria.
					sCuenta  = sCxmcu +"."+sCxobj;				
					if(sCxsub != null && !sCxsub.equals(""))
						sCuenta += "."+sCxsub;
					else
						sCxsub ="";
					
					//obtener el id de la cuenta de caja.
					ReciboCtrl rcCtrl = new ReciboCtrl();
					sIdCtaPos = rcCtrl.obtenerIdCuenta(null, null, sCxmcu.trim(), sCxobj, sCxsub);
					
					if(sIdCtaPos != null &&(sCuenta != null && sIdCtaPos != null && sCompCtaPos != null && sCxmcu != null && sCxobj != null && sCxsub != null)){
						sCuentaMF[0] = sCuenta;
						sCuentaMF[1] = sIdCtaPos;
						sCuentaMF[2] = sCompCtaPos;
						sCuentaMF[3] = sCxmcu;
						sCuentaMF[4] = sCxobj;
						sCuentaMF[5] = sCxsub;			
					}else{
						sCuentaMF = null;
						System.out.println("Error al leer el id de la cuenta de venta ");
					}
				}
			}
		}catch(Exception ex ){
			
			ex.printStackTrace(); 
			sCuentaMF  = null;
			
		}finally{
			
			if(newCn){
				try {  trans.commit(); } 
				catch (Exception e2) { }
				try {  HibernateUtilPruebaCn.closeSession(sesion); }
				catch (Exception e2) { }
			}

		}
		return sCuentaMF;
	}
/**ACTUALIZAR EL F5503AM*********************************************************************************************/
	public boolean actualizarInteresFinanciamiento(Connection cn,Finandet fd, int iNobatch,String sUsuario,String sAplicacion,int iFecha){
		boolean bHecho = true;
		Date dHora = new Date();
		SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
		String sql = "UPDATE "+PropertiesSystem.GCPCXC+".F5503AM SET ";
		PreparedStatement ps = null;
		try{
			//obtener hora en enteros
			String[] sHora = (dfHora.format(dHora)).split(":");
			int iHora = Integer.parseInt(sHora[0] + sHora[1] + sHora[2]);
			sql = sql + " ATICU = " +iNobatch+ ", ATUSER = '" +sUsuario+"', ATUPID = '"+sAplicacion+"', ATICUT = 'I', ATDIVJ = " +iFecha + ", ATHORA = "+iHora;
			sql = sql + " WHERE ATKCOO = '"+fd.getId().getCodsuc()+"' AND ATDOCO = " + fd.getId().getNosol() + " AND ATDCTO = '" +fd.getId().getTiposol()+"' AND ATAN8 = " + fd.getId().getCodcli() + " AND ATDFR = " + fd.getId().getNocuota();
			ps = cn.prepareStatement(sql);
			int rs = ps.executeUpdate();
			if (rs != 1){
				bHecho = false;
			}			
		}catch(Exception ex){
			bHecho = false;
			ex.printStackTrace();
		}
		return bHecho;
	}
/**INSERTAR MF EN EL F03B11 DEL JDE***********************************************************************************/
	public boolean insertarIF(Connection cn, Finanhdr fh, Finandet fd,
			String sTipodoc, int iNodoc, int iFecha, int iNobatch,
			BigDecimal bdTasa, String sUsuario, String sAplicacion,
			String sMonedaBase) {
		boolean bHecho = true;
		
		String campoMontoGravableDom = "RPATXN";
		String campoMontoGravableFor = "RPCTXN";
		
		if(fh.getId().getAttxa1().trim().compareTo("IMP") == 0){
			campoMontoGravableDom = "RPATXA";
			campoMontoGravableFor = "RPCTXA" ;
		}
		
		String sql = "INSERT INTO "
			+ PropertiesSystem.JDEDTA
			+ ".F03B11(RPKCO,RPAN8,RPDCT,RPDOC,RPSFX,RPDIVJ,RPDGJ,RPFY,RPCTRY,RPPN,RPCO,RPICUT,RPICU,RPDICJ,RPPA8,"
			+ "RPAN8J,RPBALJ,RPPST, " +
			" RPAG, RPAAP, "+campoMontoGravableDom+", RPSTAM, RPCRRM,RPCRCD,RPCRR,RPACR,RPFAP, "+campoMontoGravableFor+", RPCTAM, RPTXA1,RPEXR1,RPDSVJ,RPGLC,RPMCU,RPCM,RPDDJ,RPDDNJ,RPPO,RPDCTO,"
			+ "RPRMK,RPALPH,RPAC01,RPAC02,RPAC03,RPAC04,RPAC05,RPAC06,RPAC07,RPAC08,RPAC09,RPAC10,RPATE,RPATR,RPATP,RPATPR,"
			+ "RPAT1,RPAT2,RPAT3,RPAT4,RPAT5,RPTORG,RPUSER,RPPID,RPUPMJ,RPUPMT,RPJOBN, RPPKCO, RPODOC, RPODCT, RPOSFX, RPOKCO, RPMCU2 ) VALUES (";
		
		Date dHora = new Date();
		SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String sNombrePc = null;
		Vf0101 vf0101 = null;
		String sConcepto = "C:Int/Imp,Doc:", sCodunineg = "", sNocuota = "", sNosol = "";
		Divisas d = new Divisas();
		BigDecimal bdImpuestoD = BigDecimal.ZERO, bdTotalD = BigDecimal.ZERO, bdInteresD = BigDecimal.ZERO;
		double bdImpuestoC = 0,  bdInteresC = 0, dTot = 0;
		PreparedStatement ps = null;
		String UninegOriginal = "";
		
		
		try {
			
			 
			String[] sHora = (dfHora.format(dHora)).split(":");
			int iHora = Integer.parseInt(sHora[0] + sHora[1] + sHora[2]);
			 
			Date dFecha = new Date();
			String[] sFecha = (sdf.format(dFecha)).split("/");
			
			//&& ================ Generar la unidad de negocios a utilizar.
			sCodunineg = fh.getId().getCodsuc().substring(3, 5) + fh.getId().getLinea() ;
			UninegOriginal = fh.getId().getCodsuc().substring(3, 5) + fh.getId().getLinea() ;
			sCodunineg =  CompaniaCtrl.leerUnidadNegocioPorLineaSucursal(fh.getId().getCodsuc(), "52");
			if( sCodunineg.isEmpty() ){
				sCodunineg = UninegOriginal;
			}
			
			sCodunineg = CodeUtil.pad(sCodunineg, 12);
			UninegOriginal = CodeUtil.pad(UninegOriginal, 12);
			
			//&& ================ pc origen de la transaccion
			sNombrePc = InetAddress.getLocalHost().getHostName();
			String nombrepclog = sNombrePc;
			
			sConcepto = sConcepto + fh.getId().getNosol() + " Cuot:" + fd.getId().getNocuota();
			if (sConcepto.length() > 30) {
				sConcepto = sConcepto.substring(0, 29);
			}
			if (sNombrePc.length() > 9) {
				sNombrePc = sNombrePc.substring(0, 9);
			}

//			LogCrtl.sendLogInfo("Nombre de la pc desde IF :" + nombrepclog	+ " ||| " + sNombrePc);

			// no de solicitud
			sNosol = formatNosolicitud(fh.getId().getNosol());
			sNocuota = formatNocuota(fd.getId().getNocuota());
			
		
			vf0101 = EmpleadoCtrl.buscarEmpleadoxCodigo2(fh.getId().getCodcli());

			sql = sql + "'" + fh.getId().getCodsuc() + "',"
					+ fh.getId().getCodcli() + "," + "'" + sTipodoc + "',"
					+ iNodoc + "," + "'" + sNocuota + "'," + iFecha + ","
					+ iFecha + ","
					+ Integer.parseInt(sFecha[2].substring(2, 4)) + "," + "20,"
					+ "" + Integer.parseInt(sFecha[1]) + "," + "'"
					+ fh.getId().getCodsuc() + "'," + "'I'," + iNobatch + ","
					+ iFecha + "," + fh.getId().getCodcli() + ","
					+ fh.getId().getCodcli() + "," + "'Y'," + "'A',";
			
			BigDecimal valTemp = new BigDecimal(0);
		
			if (fh.getId().getMoneda().equals(sMonedaBase)) {
				
				bdTotalD = fd.getId().getInteres().add(fd.getId().getImpuesto());
				valTemp = bdTotalD;
				
				sql = sql
						+ d.pasarAentero(bdTotalD.doubleValue())
						+ ","
						+ d.pasarAentero(bdTotalD.doubleValue())
						+ ","
						+ d.pasarAentero(fd.getId().getInteres().doubleValue())
						+ ","
						+ d.pasarAentero(fd.getId().getImpuesto().doubleValue())
						+ "," + "'D'," + "'" + sMonedaBase + "'," + 0 + "," + 0
						+ "," + 0 + "," + 0 + "," + 0 + ",";
			} else {

				bdTotalD = fd.getId().getInteres().add(fd.getId().getImpuesto());
				bdImpuestoD = fd.getId().getImpuesto();
				bdInteresD = fd.getId().getInteres();

				bdImpuestoC = d.roundDouble((bdImpuestoD.multiply(bdTasa)).doubleValue());
				bdInteresC = d.roundDouble((bdInteresD.multiply(bdTasa)).doubleValue());

				dTot = d.roundDouble(bdImpuestoC + bdInteresC);
				valTemp = BigDecimal.valueOf(dTot);
				
				
				sql = sql
						+ d.pasarAentero(dTot)
						+ ","
						+ d.pasarAentero(dTot)
						+ ","
						+ d.pasarAentero(bdInteresC)
						+ ","
						+ d.pasarAentero(bdImpuestoC)
						+ ","
						+ "'F',"
						+ "'"
						+ fh.getId().getMoneda()
						+ "',"
						+ bdTasa
						+ ","
						+ d.pasarAentero(bdTotalD.doubleValue())
						+ ","
						+ d.pasarAentero(bdTotalD.doubleValue())
						+ ","
						+ d.pasarAentero(fd.getId().getInteres().doubleValue())
						+ ","
						+ d.pasarAentero(fd.getId().getImpuesto().doubleValue())
						+ ",";
			}

			sql = sql + "'" + fh.getId().getAttxa1() + "'," + "'"
					+ fh.getId().getAtexr1() + "'," + iFecha + "," + "' ',"
					+ "'" + sCodunineg + "'," + "' '," + fd.getId().getAtpayd()
					+ "," + fd.getId().getAtpayd() + "," + "'" + sNosol + "',"
					+ "'" + fh.getId().getTiposol() + "'," + "'" + sConcepto
					+ "'," + "'" + fh.getId().getNomcli() + "'," + "'"
					+ vf0101.getId().getAbac01() + "'," + "'"
					+ vf0101.getId().getAbac02() + "'," + "'"
					+ vf0101.getId().getAbac03() + "'," + "'"
					+ vf0101.getId().getAbac04() + "'," + "'"
					+ vf0101.getId().getAbac05() + "'," + "'"
					+ vf0101.getId().getAbac06() + "'," + "'"
					+ vf0101.getId().getAbac07() + "'," + "'"
					+ vf0101.getId().getAbac08() + "'," + "'"
					+ vf0101.getId().getAbac09() + "'," + "'"
					+ vf0101.getId().getAbac10() + "'," + "'"
					+ vf0101.getId().getAbate() + "'," + "'"
					+ vf0101.getId().getAbatr() + "'," + "'"
					+ vf0101.getId().getAbatp() + "','"
					+ vf0101.getId().getAbatpr() + "'," + "'"
					+ vf0101.getId().getAbat1() + "'," + "'"
					+ vf0101.getId().getAbat2() + "'," + "'"
					+ vf0101.getId().getAbat3() + "'," + "'"
					+ vf0101.getId().getAbat4() + "'," + "'"
					+ vf0101.getId().getAbat5() + "'," + "'" + sUsuario + "',"
					+ "'" + sUsuario + "'," + "'" + sAplicacion + "'," + iFecha
					+ "," + iHora + "," + "'" + sNombrePc + "','"
					+ fh.getId().getCodsuc() + "', " +
					
//								" " + iNodoc +", "+
//								"'" + sTipodoc+"', " +
					" " + fh.getId().getNosol() +", "+
					"'" + fh.getId().getTiposol()+"', " +
					"'" + sNocuota+"', " +
					"'" + fh.getId().getCodsuc()+"', " +
					"'" + UninegOriginal + "' " +
					")";

			String strSql = "SELECT RPAN8 FROM " + PropertiesSystem.JDEDTA
					+ ".F03B11 " + " WHERE RPAN8 = " + fh.getId().getCodcli()
					+ " AND TRIM(RPDCT) = 'IF'" + " AND RPDIVJ = " + iFecha
					+ " AND RPAG = " + d.pasarAentero(valTemp.doubleValue())
					+ " " + " AND TRIM(RPPO) ='" + sNosol + "'  AND RPSFX ='"
					+ sNocuota + "' ";
			ps = cn.prepareStatement(strSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ResultSet Rtemp = ps.executeQuery();
			Rtemp.last();
			
			if (Rtemp.getRow() == 0) {
				ps = cn.prepareStatement(sql);
				int rs = ps.executeUpdate();
				if (rs != 1) {
					bHecho = false;
				}
			} else {
//				System.out.println(" ===>> TRANSACCION NO APICADA PORQUE REGISTROS YA EXISTEN IF: " + fh.getId().getCodcli() + " ==>> " + iFecha + " ==> " 
//				+ sNosol 	+ " ==> " + sNocuota + " ==> sConcepto: " + sConcepto 	+ " ==> sUsuario: " + sUsuario + " ==> sConcepto: "  + sConcepto + " ==> sUsuario: " + sUsuario);
				
				sendMail( " ===>> TRANSACCION NO APICADA PORQUE REGISTROS YA EXISTEN IF cliente: "
								+ fh.getId().getCodcli() + " ==>> fecha: "
								+ iFecha + " ==> solicidutd: " + sNosol
								+ " ==> cuota: " + sNocuota
								+ " ==> sConcepto: " + sConcepto
								+ " ==> sUsuario: " + sUsuario,
						"Duplicacion de intereses detectada - Ambiente: "
								+ PropertiesSystem.JDEDTA + " server: "
								+ new PropertiesSystem().URLSIS);
			}
		} catch (Exception ex) {
			ex.printStackTrace(); 
			bHecho = false;
		}
		return bHecho;
	}
/*********************************************************************************************************************/	
	
	private void sendMail(String strBody,String strTitle) {
		try {
			
			String strListEmail = PropertiesSystem.MAIL_INTERNAL_ADDRESS;
			String[] listEmail = strListEmail.split(",");
			
			List<CustomEmailAddress> toList = new ArrayList<CustomEmailAddress>();
			for (String strEmail : listEmail) {
				toList.add(new CustomEmailAddress(strEmail));
			}

			if (toList.size() > 0) {
				MailHelper.SendHtmlEmail(
						new CustomEmailAddress(PropertiesSystem.WEBMASTER_EMAIL_ADRESS, "Módulo de caja"),
						toList, null, new CustomEmailAddress(PropertiesSystem.MAIL_BOUNCEADDRESS), 
						strTitle, strBody);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
/*********************************************************************************************************************/
	public int leerActualizarNoCorriente(String sCodsuc){
		int iNodoc = 0, iActualizado = 0;
		String sql = "";
		Session sesion = HibernateUtilPruebaCn.currentSession();
		Transaction trans = null;
		Object ob = null;
		try{
			//-------- Leer el número de documento del F00021
			sql = "select f.nln001 from "+PropertiesSystem.JDECOM+".f00021 f where trim(f.nlkco) = '"+ sCodsuc +"' and trim(f.nldct) = 'IF' and f.nlctry = 0 and f.nlfy = 0";
			trans = sesion.beginTransaction();
			ob = sesion.createSQLQuery(sql).uniqueResult();
			if(ob != null){
				iNodoc = Integer.parseInt(ob.toString());
				
				//------Actualizar el número de documento
				sql = "UPDATE "+PropertiesSystem.JDECOM+".f00021 f SET NLN001 = "+ (iNodoc + 1)+" where trim(f.nlkco) = '"+ sCodsuc +"' and trim(f.nldct) = 'IF' and f.nlctry = 0 and f.nlfy = 0";
				Query q = sesion.createSQLQuery(sql);
				iActualizado = q.executeUpdate();
				if(iActualizado != 1)
					iNodoc = -1;
			}else{
				//-------- Leer el número de documento del F0002
				sql = "SELECT NNN002 FROM "+PropertiesSystem.JDECOM+".F0002 WHERE NNSY = '55MF'";
				ob = sesion.createSQLQuery(sql).uniqueResult();
				if(ob != null){
					iNodoc = Integer.parseInt(ob.toString());
					//------Actualizar el número de documento
					sql = "UPDATE "+PropertiesSystem.JDECOM+".f0002 SET NNN002 = "+ (iNodoc + 1)+" WHERE NNSY = '55MF'";
					Query q = sesion.createSQLQuery(sql);
					iActualizado = q.executeUpdate();
					if(iActualizado != 1)
						iNodoc = -1;
				}
			}
			trans.commit();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return iNodoc;
	}
/*********************************************************************************************************************/
/*************************************************************************************/
	public String formatNocuota(int iNocuota){
		String sNocuota = "";
		if(iNocuota > 9){
			sNocuota = "0" + iNocuota;
		}else if (iNocuota < 10){
			sNocuota = "00" + iNocuota;
		}else{
			sNocuota = "" + iNocuota;
		}
		return sNocuota;
	}
/*************************************************************************************/
	public String formatNosolicitud(int iNosol){
		String sNosol="";
		try{
			int l = String.valueOf(iNosol).length();
			switch (l){
				case 1:
					sNosol = "0000000"+iNosol;
					break;
				case 2:
					sNosol = "000000"+iNosol;
					break;
				case 3:
					sNosol = "00000"+iNosol;
					break;
				case 4:
					sNosol = "0000"+iNosol;
					break;
				case 5:
					sNosol = "000"+iNosol;
					break;
				case 6:
					sNosol = "00"+iNosol;
					break;
				case 7:
					sNosol = "0"+iNosol;
					break;
				case 8:
					sNosol = ""+iNosol;
					break;
					
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return sNosol;
	}
/**ACTUALIZAR EL F5503AM*********************************************************************************************/
	public boolean actualizarMoraFinanciamiento(Connection cn,Finandet fd, int iNobatch,String sUsuario,String sAplicacion,int iFecha){
		boolean bHecho = true;
		Date dHora = new Date();
		SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
		String sql = "UPDATE "+PropertiesSystem.GCPCXC+".F5503AM SET ";
		PreparedStatement ps = null;
		try{
			//obtener hora en enteros
			String[] sHora = (dfHora.format(dHora)).split(":");
			int iHora = Integer.parseInt(sHora[0] + sHora[1] + sHora[2]);
			sql = sql + " ATDVEN = " +(fd.getId().getDiasven() + fd.getId().getDiasmora())+ ", ATICUM = " +iNobatch+ ", ATUSERM = '" +sUsuario+"', ATUPIDM = '"+sAplicacion+"', ATICUTM = 'I', ATDIVJM = " +iFecha + ", ATHORAM = "+iHora;
			sql = sql + " WHERE ATKCOO = '"+fd.getId().getCodsuc()+"' AND ATDOCO = " + fd.getId().getNosol() + " AND ATDCTO = '" +fd.getId().getTiposol()+"' AND ATAN8 = " + fd.getId().getCodcli() + " AND ATDFR = " + fd.getId().getNocuota();
			ps = cn.prepareStatement(sql);
			int rs = ps.executeUpdate();
			if (rs != 1){
				bHecho = false;
			}			
		}catch(Exception ex){
			bHecho = false;
			ex.printStackTrace();
		}
		return bHecho;
	}
/**INSERTAR MF EN EL F03B11 DEL JDE***********************************************************************************/
	public boolean insertarMF(Connection cn, Finanhdr fh,Finandet fd,String sTipodoc,int iNodoc, int iFecha,int iNobatch,BigDecimal bdTasa,
			String sUsuario,String sAplicacion, String sMonedaBase){
		boolean bHecho = true;
		String sql = "INSERT INTO "+PropertiesSystem.JDEDTA+".F03B11(RPKCO,RPAN8,RPDCT,RPDOC,RPSFX,RPDIVJ,RPDGJ,RPFY,RPCTRY,RPPN,RPCO,RPICUT,RPICU,RPDICJ,RPPA8," + /*15*/
											/*21*/								   /*27*/
		"RPAN8J,RPBALJ,RPPST,RPAG,RPAAP,RPATXN,RPCRRM,RPCRCD,RPCRR,RPACR,RPFAP,RPCTXN,RPTXA1,RPEXR1,RPDSVJ,RPGLC,RPMCU,RPCM,RPDDJ,RPDDNJ,RPPO,RPDCTO," +
		
		"RPRMK,RPALPH,RPAC01,RPAC02,RPAC03,RPAC04,RPAC05,RPAC06,RPAC07,RPAC08,RPAC09,RPAC10,RPATE,RPATR,RPATP,RPATPR," +
		
		"RPAT1,RPAT2,RPAT3,RPAT4,RPAT5,RPTORG,RPUSER,RPPID,RPUPMJ,RPUPMT,RPJOBN, RPPKCO, RPODOC, RPODCT, RPOSFX, RPOKCO, RPMCU2 ) VALUES(";

		Date dHora = new Date();
		SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String sNombrePc = null;
		Vf0101 vf0101 = null;
		String sConcepto = "C:Int.Morat,Doc:", sCodunineg = "", sNocuota = "", sNosol = "";
		Divisas d = new Divisas();
		BigDecimal bdMontoD = BigDecimal.ZERO;
		PreparedStatement ps = null;
		String UninegOriginal = "";

		try {

			String[] sHora = (dfHora.format(dHora)).split(":");
			int iHora = Integer.parseInt(sHora[0] + sHora[1] + sHora[2]);

			Date dFecha = new Date();
			String[] sFecha = (sdf.format(dFecha)).split("/");

			// && ================ Generar la unidad de negocios a utilizar. (52
			// codigo configurado para financimientos de credi-pellas)

			sCodunineg = fh.getId().getCodsuc().substring(3, 5)	+ fh.getId().getLinea();
			UninegOriginal = fh.getId().getCodsuc().substring(3, 5)	+ fh.getId().getLinea();
			sCodunineg = CompaniaCtrl.leerUnidadNegocioPorLineaSucursal(fh.getId().getCodsuc(), "52");
			if (sCodunineg.isEmpty()) {
				sCodunineg = UninegOriginal;
			}

			sCodunineg = CodeUtil.pad(sCodunineg, 12);
			UninegOriginal = CodeUtil.pad(UninegOriginal, 12);

			// && ================ pc origen de la transaccion
			sNombrePc = InetAddress.getLocalHost().getHostName();
			String nombrepclog = sNombrePc;
			sConcepto = sConcepto + fh.getId().getNosol() + " Cuot:" + fd.getId().getNocuota();
			if (sConcepto.length() > 30) {
				sConcepto = sConcepto.substring(0, 29);
			}
			if (sNombrePc.length() > 9) {
				sNombrePc = sNombrePc.substring(0, 9);
			}
//			LogCrtl.sendLogInfo("Nombre de la pc desde IF :" + nombrepclog	+ " ||| " + sNombrePc);

			//no de solicitud
			sNosol = formatNosolicitud(fh.getId().getNosol());
			sNocuota = formatNocuota(fd.getId().getNocuota());
			//BUSCAR INFO DEL CLIENTE
			vf0101 = EmpleadoCtrl.buscarEmpleadoxCodigo2(fh.getId().getCodcli());

			sql = sql + 
					"'" +fh.getId().getCodsuc()+"'," + 
					fh.getId().getCodcli() +"," +
					"'" +sTipodoc+"'," +
					iNodoc +"," +
					"'" +sNocuota+"'," +
					iFecha +"," +
					iFecha +"," +
					Integer.parseInt(sFecha[2].substring(2, 4))+"," +
					"20," +
					""+Integer.parseInt(sFecha[1])+"," +
					"'" +fh.getId().getCodsuc()+"'," + 
					"'I'," +
					iNobatch +"," +
					iFecha +"," +
					fh.getId().getCodcli() +"," +
					fh.getId().getCodcli() +"," +
					"'Y'," +
					"'A',"; /*18*/ 

					BigDecimal bdMontoD2 = BigDecimal.ZERO;	;
					
					if(fh.getId().getMoneda().equals(sMonedaBase)){
						  bdMontoD2 = BigDecimal.valueOf(fd.getId().getMora().doubleValue());
						  sql = sql + 					  
						  d.pasarAentero(fd.getId().getMora().doubleValue()) + "," +
						  d.pasarAentero(fd.getId().getMora().doubleValue()) + "," +
						  d.pasarAentero(fd.getId().getMora().doubleValue()) + "," +
						  "'D'," + 
						  "'"+sMonedaBase+"'," + 
						  0 + "," +
						  0 + "," +	
						  0 + "," +
						  0 + "," ;	
					}else{
						  bdMontoD = d.roundBigDecimal(fd.getId().getMora().multiply(bdTasa));
						  bdMontoD2 = bdMontoD;
						  sql = sql + 
						  d.pasarAentero(bdMontoD.doubleValue()) + "," +
						  d.pasarAentero(bdMontoD.doubleValue()) + "," +
						  d.pasarAentero(bdMontoD.doubleValue()) + "," +
						  "'F'," + 
						  "'"+fh.getId().getMoneda()+"'," + 
						  bdTasa + "," +
						  d.pasarAentero(fd.getId().getMora().doubleValue()) + "," +
						  d.pasarAentero(fd.getId().getMora().doubleValue()) + "," +
						  d.pasarAentero(fd.getId().getMora().doubleValue()) + ",";
					}

			sql = sql + 
				"'EXE'," + 
				"'E'," + 
				iFecha +"," +
				"'"+vf0101.getId().getA5arc()+"'," +
				"'"+sCodunineg+"'," +
				"'"+vf0101.getId().getAbcm()+"'," +
				fd.getId().getAtpayd() + "," +
				fd.getId().getAtpayd() + "," +
				"'" + sNosol+"'," +
				"'" + fh.getId().getTiposol()+"'," +
				"'" + sConcepto+"'," +
				"'" + fh.getId().getNomcli().trim()+"'," +
				"'" + vf0101.getId().getAbac01() + "'," + "'" + vf0101.getId().getAbac02() + "'," + "'" + vf0101.getId().getAbac03() + "'," + "'" + vf0101.getId().getAbac04() + "'," +
				"'" + vf0101.getId().getAbac05() + "'," + "'" + vf0101.getId().getAbac06() + "'," + "'" + vf0101.getId().getAbac07() + "'," + "'" + vf0101.getId().getAbac08() + "'," +
				"'" + vf0101.getId().getAbac09() + "'," + "'" + vf0101.getId().getAbac10() + "'," +
				"'" + vf0101.getId().getAbate() + "'," + "'" + vf0101.getId().getAbatr() + "'," +
				"'" + vf0101.getId().getAbatp() + "','" + vf0101.getId().getAbatpr() + "'," +
				"'" + vf0101.getId().getAbat1() + "'," +
				"'" + vf0101.getId().getAbat2() + "'," + "'" + vf0101.getId().getAbat3() + "'," + "'" + vf0101.getId().getAbat4() + "'," + "'" + vf0101.getId().getAbat5() + "'," +
				"'" + sUsuario + "'," + "'" + sUsuario + "'," + "'"+sAplicacion+"'," + iFecha + "," + iHora + "," +"'"+sNombrePc + "','" +fh.getId().getCodsuc()+"', " +
				
				//" " + iNodoc +", "+
				//"'" + sTipodoc+"', " +
				" " + fh.getId().getNosol() +", "+
				"'" + fh.getId().getTiposol()+"', " +
				"'" + sNocuota+"', " +
				"'" + fh.getId().getCodsuc()+"', " +
				"'" + UninegOriginal + "' " +
				")";

				String strSql = "SELECT RPAN8 FROM "+PropertiesSystem.JDEDTA+".F03B11 "
						+ "INNER JOIN "+PropertiesSystem.JDEDTA+".F0006 UN ON RPMCU = UN.MCMCU   " +
				" WHERE RPAN8 = "+fh.getId().getCodcli()+" AND TRIM(RPDCT) IN (select COD_DOCUMENTO from " + PropertiesSystem.GCPCXC + ".TIPOS_DOCUMENTOS_FINANCIAMIENTO where TIPO_AGRUPACION = 'IMO' and cod_compania = CAST(TRIM(UN.MCRP01) AS VARCHAR(3) CCSID 37) order by ORDEN_PROCESAMIENTO asc)" +  
				" AND RPDIVJ = "+iFecha+" AND RPAG = "+d.pasarAentero(bdMontoD2.doubleValue())+" " +
				" AND TRIM(RPPO) ='"+sNosol+"'  AND RPSFX ='"+sNocuota+"' "; 
				ps = cn.prepareStatement(strSql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				ResultSet Rtemp = ps.executeQuery();          
				Rtemp.last();
				
			if (Rtemp.getRow() == 0) {
				ps = cn.prepareStatement(sql);
				int rs = ps.executeUpdate();
				if (rs != 1) {
					bHecho = false;
				}
			} else {
//				System.out.println(" ===>> TRANSACCION NO APICADA PORQUE REGISTROS YA EXISTEN MF cliente: "+fh.getId().getCodcli()+" ==>> fecha: "+iFecha+ " ==> solicidutd: "+sNosol+ " ==> cuota: "+sNocuota+" ==> sConcepto: "+sConcepto+" ==> sUsuario: "+sUsuario);
				sendMail(" ===>> TRANSACCION NO APICADA PORQUE REGISTROS YA EXISTEN MF cliente: "+fh.getId().getCodcli()+" ==>> fecha: "+iFecha+ " ==> solicidutd: "+sNosol+ " ==> cuota: "+sNocuota+" ==> sConcepto: "+sConcepto+" ==> sUsuario: "+sUsuario,"Duplicacion de intereses detectada - Ambiente: "+PropertiesSystem.JDEDTA+" server: "+new PropertiesSystem().URLSIS);
			}
		} catch (Exception ex) {
			bHecho = false;
			ex.printStackTrace(); 
		}
		return bHecho;
	}
/*********************************************************************************************************************/
	public int leerActualizarNoMoratorio(String sCodsuc){
		int iNodoc = 0, iActualizado = 0;
		String sql = "";
		Session sesion = HibernateUtilPruebaCn.currentSession();
		Transaction trans = null;
		Object ob = null;
		try{
			//-------- Leer el número de documento del F00021
			sql = "select f.nln001 from "+PropertiesSystem.JDECOM+".f00021 f where trim(f.nlkco) = '"+ sCodsuc +"' and trim(f.nldct) = 'MF' and f.nlctry = 0 and f.nlfy = 0";
			trans = sesion.beginTransaction();
			ob = sesion.createSQLQuery(sql).uniqueResult();
			if(ob != null){
				iNodoc = Integer.parseInt(ob.toString());
				
				//------Actualizar el número de documento
				sql = "UPDATE "+PropertiesSystem.JDECOM+".f00021 f SET NLN001 = "+ (iNodoc + 1)+" where trim(f.nlkco) = '"+ sCodsuc +"' and trim(f.nldct) = 'MF' and f.nlctry = 0 and f.nlfy = 0";
				Query q = sesion.createSQLQuery(sql);
				iActualizado = q.executeUpdate();
				if(iActualizado != 1)
					iNodoc = -1;
			}else{
				//-------- Leer el número de documento del F0002
				sql = "SELECT NNN003 FROM "+PropertiesSystem.JDECOM+".F0002 WHERE NNSY = '55MF'";
				ob = sesion.createSQLQuery(sql).uniqueResult();
				if(ob != null){
					iNodoc = Integer.parseInt(ob.toString());
					//------Actualizar el número de documento
					sql = "UPDATE "+PropertiesSystem.JDECOM+".f0002 SET NNN003 = "+ (iNodoc + 1)+" WHERE NNSY = '55MF'";
					Query q = sesion.createSQLQuery(sql);
					iActualizado = q.executeUpdate();
					if(iActualizado != 1)
						iNodoc = -1;
				}
			}
			trans.commit();
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			sesion.close();
		}
		return iNodoc;
	}
/*********************************************************************************************************************/
	public Finandet buscarSiguienteCuota(String sCodcomp, String sCodsuc, final int iNosol,String sTiposol, int iCodcli, List<Finandet> lstSelectedCuotas){
		Finandet f = null;

		try{

			String sql = "select * from " + PropertiesSystem.ESQUEMA
					+ ".finandet" + " where nosol = " + iNosol
					+ " and codcli = " + iCodcli + " and codcomp = '"
					+ sCodcomp + "' and codsuc = '" + sCodsuc + "' "
					+ " and montopend > 0 and fechapago >= '"
					+ FechasUtil.formatDatetoString(new Date(), "yyyy-MM-dd")
					+ "' ";
			
			@SuppressWarnings("unchecked")
			List<Finandet> cuotasIncluidas = (ArrayList<Finandet>)
			CollectionUtils.select(lstSelectedCuotas, new Predicate(){
				public boolean evaluate(Object o) {
					return ((Finandet)o).getId().getNosol() == iNosol;
				}
			});
			
			if( cuotasIncluidas != null && !cuotasIncluidas.isEmpty()  ){
				String numeros = "";
				
				for (Finandet finandet : cuotasIncluidas) {
					numeros += finandet.getId().getNocuota() +", ";
				}
				numeros = numeros.substring(0, numeros.lastIndexOf(","));
				sql += " and nocuota not in ("+numeros +")";
			}
			 
			sql = sql + " order by nocuota FETCH FIRST 1 ROWS only OPTIMIZE FOR 1 ROWS ";
			
			LogCajaService.CreateLog("buscarSiguienteCuota", "QRY", sql);
			
//			System.out.println(sql);
			//f = ConsolidadoDepositosBcoCtrl.executeSqlQueryUnique(sql, Finandet.class, true);
			f = new RoQueryManager().executeSqlQueryUnique(sql, Finandet.class, true);
			
		}catch(Exception ex){
			ex.printStackTrace(); 
			LogCajaService.CreateLog("buscarSiguienteCuota", "ERR", ex.getMessage());
		} 
		
		return f;
	}
/***********************************************************************************************/
	public Finandet buscarMoraExistente(Finandet f){
		 
		
//		Vmora vmora = null;
		BigDecimal moraPendiente = BigDecimal.ZERO;
		try{	
			
			String sql = "from Vmora as v where v.id.nosol = " + f.getId().getNosol() + 
					" and v.id.codcli = " +f.getId().getCodcli()+" " +
					" and v.id.codsuc = '" +f.getId().getCodsuc()+"' " +
					" and v.id.nocuota = " +f.getId().getNocuota();
			
//			System.out.println("Consulta HQL: "+sql);
			
			List<Vmora> lstMoraAsociada = ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, Vmora.class, false);
			
			if( lstMoraAsociada != null && !lstMoraAsociada.isEmpty() ) {
				moraPendiente = lstMoraAsociada.get(0).getId().getMorapend();
			}
			
			if(f.getId().getMora().compareTo(BigDecimal.ZERO) == 1 ){
				moraPendiente = moraPendiente.add( f.getId().getMora() );
			}
			
			f.setMora( moraPendiente );
			f.setMontopend(f.getId().getMontopend().add(moraPendiente) );
			f.setMontoAplicar(BigDecimal.ZERO);
			 
		}catch(Exception ex){
			 System.out.println("Metodo de busqueda de mora existente genero catch: " + ex.getMessage()); 
		} 
		
		return f;
	}
/***********************************************************************************************/
	public BigDecimal buscarMora311(int iNosol, String sCodsuc,int iCodcli){
 
		BigDecimal mora = BigDecimal.ZERO;
		
		try{

			String sql  = 
			" SELECT CAST((CAST(( CASE WHEN RPCRCD = 'COR' THEN SUM(RPAAP) ELSE SUM(RPFAP) END)  AS DECIMAL(10,2))/100) AS DECIMAL(10,2))"+	
			" FROM "+PropertiesSystem.JDEDTA+".F03B11 INNER JOIN "+PropertiesSystem.JDEDTA+".F0006 UN ON RPMCU = UN.MCMCU   " +
			" WHERE RPDCT IN (select COD_DOCUMENTO from " + PropertiesSystem.GCPCXC + ".TIPOS_DOCUMENTOS_FINANCIAMIENTO "
					+ "where TIPO_AGRUPACION = 'IMO' and cod_compania = CAST(TRIM(UN.MCRP01) AS VARCHAR(3) CCSID 37) "
					+ "order by ORDEN_PROCESAMIENTO asc) AND RPAAP > 0 AND RPAN8 = " +  iCodcli + 
			" AND RPKCO = '"+sCodsuc+"' AND  " +
				"trim(RPPO) = LPAD(" + iNosol + ", 8, '0')" +
				" GROUP BY RPKCO,RPDCT,RPPO,RPDCTO,RPAN8,RPALPH,RPMCU,RPCRCD " +
				"FETCH FIRST ROWS ONLY";
			
			Object obMontoMora = ConsolidadoDepositosBcoCtrl.executeSqlQueryUnique(sql, null, true);
			if(obMontoMora == null )
				return  mora = BigDecimal.ZERO;
			
			mora = new BigDecimal( String.valueOf( obMontoMora ) ) ;

		}catch(Exception ex){
			ex.printStackTrace();
			mora = BigDecimal.ZERO;
		} 
		return mora;
	}
	

	public void buscarMoraDeCuotas(List<Finandet>lstCuotas){
		try {
			
			Finandet f = lstCuotas.get(0);
			
			String sql =  
			" from Vmora as v where v.id.nosol = " + f.getId().getNosol()  + 
			" and v.id.codcli = "  + f.getId().getCodcli()+" " +
			" and v.id.codsuc = '" + f.getId().getCodsuc()+"' " +
			" and v.id.nocuota in (@NUMEROS_CUOTA)" ;
			
			String numeros = "";
		 
			for (Finandet cuota : lstCuotas) {
				numeros += String.valueOf( cuota.getId().getNocuota() ).concat(", ") ;
			}
			numeros = numeros .substring(0,numeros.lastIndexOf(",") );

			sql = sql.replace("@NUMEROS_CUOTA", numeros);
			
			//&& ======= lista de moras ya existentes.
			//final List<Vmora> moras = ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, Vmora.class, false);
			final List<Vmora> moras = new RoQueryManager().executeSqlQuery(sql, Vmora.class, false);
			
			LogCajaService.CreateLog("buscarMoraDeCuotas", "QRY", sql);
					
			sql = 
			"select cast( rpsfx as numeric(8) ),  (CASE WHEN RPCRCD = 'COR' THEN CAST(RPAAP/100 AS DECIMAL(10,2) ) ELSE CAST(RPFAP/100 AS DECIMAL(10,2) ) END) MONTOPEND " +
			" from " + PropertiesSystem.JDEDTA+".F03B11 "
					+ "INNER JOIN "+PropertiesSystem.JDEDTA+".F0006 UN ON RPMCU = UN.MCMCU " +
			" where rpan8 = "+ f.getId().getCodcli()+" and rpaap > 0  and rpdct IN (select COD_DOCUMENTO from " + PropertiesSystem.GCPCXC + ".TIPOS_DOCUMENTOS_FINANCIAMIENTO "
					+ "where TIPO_AGRUPACION = 'ICR' and cod_compania = CAST(TRIM(UN.MCRP01) AS VARCHAR(3) CCSID 37) "
					+ "order by ORDEN_PROCESAMIENTO asc)" +
			" and cast(rppo as numeric(8) ) = " + f.getId().getNosol() + 
			" and rpdcto = '"+ f.getId().getTiposol()+"' " +
			" and rpkco = '" + f.getId().getCodsuc()+"' " +
			" and cast( rpsfx as numeric(8) ) in ("+ numeros+ " ) "  ;
			
			LogCajaService.CreateLog("buscarMoraDeCuotas", "QRY", sql);
			
			//final List<Object[]> interesesCorrientes = ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql,null, true);
			final List<Object[]> interesesCorrientes = new RoQueryManager().executeSqlQuery(sql,null, true);
			
			final boolean hayInteresesCorrientes = (interesesCorrientes != null && !interesesCorrientes.isEmpty() ) ;
			
			CollectionUtils.forAllDo(lstCuotas, new Closure() {
			 
				public void execute(Object o) {
					final Finandet cuota = (Finandet)o;
					
					BigDecimal moraTotalCuota = cuota.getId().getMora();
					
					if(moras != null && !moras.isEmpty() ){
						Vmora moraGenerada = (Vmora)				
						CollectionUtils.find(moras, new Predicate(){
							public boolean evaluate(Object o) {
								return ((Vmora)o).getId().getNocuota() == cuota.getId().getNocuota();
							}
						});
								
						if(moraGenerada != null){
							moraTotalCuota = moraTotalCuota.add(moraGenerada.getId().getMorapend());
						}	
					}
					
					cuota.setInteresPend(BigDecimal.ZERO);
					cuota.setMontoAplicar(BigDecimal.ZERO);
					cuota.setMora(moraTotalCuota);
					cuota.setMontopend( cuota.getId().getMontopend().add(moraTotalCuota) ) ;
					
					if( cuota.getMontopend().compareTo( cuota.getId().getMonto() ) == 1  && hayInteresesCorrientes ){
						
						Object[] dtaInteres = (Object[])
						CollectionUtils.find( interesesCorrientes, new Predicate(){
							public boolean evaluate(Object o) {
								Object[] dtaInteres = (Object[])o;
								return	Integer.parseInt( String.valueOf( dtaInteres[0] ) ) ==  (int) cuota.getId().getNocuota()  ;
							}
						});
						
						if( dtaInteres != null ) {
							cuota.setInteresPend( new BigDecimal(String.valueOf( dtaInteres[1] ) ) );
						} 
					}
				}
			});
 
			
		} catch (Exception e) {
			e.printStackTrace(); 
			LogCajaService.CreateLog("buscarMoraDeCuotas", "ERR", e.getMessage());
		}
	}
	
/************************************************************************************/
	public List buscarMoraExistente(List lstCuotas){
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = null;	
		String sql = "";
		Vmora vmora = null;
		Finandet f = null;
		BigDecimal bdInteres = BigDecimal.ZERO;
		try{			
			tx = session.beginTransaction();
			for(int i = 0 ; i < lstCuotas.size();i++){
				f = (Finandet)lstCuotas.get(i);
				sql = " from Vmora as v where v.id.nosol = " + f.getId().getNosol() + " and v.id.codcli = " +f.getId().getCodcli()+" and v.id.codsuc = '" +f.getId().getCodsuc()+"' and v.id.nocuota = " +f.getId().getNocuota(); 
				vmora = (Vmora)session.createQuery(sql).uniqueResult();
				if(vmora!=null){
					f.setMora(vmora.getId().getMorapend().add(f.getId().getMora()));
					f.setMontopend(vmora.getId().getMorapend().add(f.getId().getMora().add(f.getId().getMontopend())));
					f.setMontoAplicar(BigDecimal.ZERO);
				}else{
					f.setMora(f.getId().getMora());
					f.setMontopend(f.getId().getMontopend().add(f.getId().getMora()));
					f.setMontoAplicar(BigDecimal.ZERO);
				}
				//buscar pendiente IF 
				if(!(f.getMontopend().doubleValue() < f.getId().getMonto().doubleValue())){
					bdInteres = buscarInteresCorrientePend(f);
				}
				f.setInteresPend(bdInteres);
				lstCuotas.set(i, f);
			}
						
			tx.commit();
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			session.close();
		}
		return lstCuotas;
	}
/********************************************************************************************/
	public List<Finandet> getCuotasVencidas(List<Finanhdr> lstCreditos){
		List<Finandet> lstCuotas = null;
		String sql = "";
		Finanhdr fh = null;
		
		try{
			for (int i = 0; i < lstCreditos.size();i++){
				fh = (Finanhdr)lstCreditos.get(i);
				if(i == 0){
					sql = "select * from "+PropertiesSystem.ESQUEMA+".Finandet as f where f.codcomp = '" 
						+fh.getId().getCodcomp()+"' and f.codsuc = '" 
						+fh.getId().getCodsuc()+"' and f.nosol = " 
						+fh.getId().getNosol()+" and f.tiposol = '"
						+fh.getId().getTiposol()+"' and f.codcli = " 
						+fh.getId().getCodcli() +" and f.montopend > 0 "
						+ " and date(f.fechapago) < '"
						+FechasUtil.formatDatetoString(new Date(), "yyyy-MM-dd")+"'  " ;
				}
				else{
					sql = sql + " union ";
					sql = sql
						+"select * from "+PropertiesSystem.ESQUEMA+".Finandet as f where f.codcomp = '" 
						+fh.getId().getCodcomp()+"' and f.codsuc = '" 
						+fh.getId().getCodsuc()+"' and f.nosol = " 
						+fh.getId().getNosol()+" and f.tiposol = '"
						+fh.getId().getTiposol()+"' and f.codcli = " 
						+fh.getId().getCodcli() +" and f.montopend > 0 "
						+ " and date(f.fechapago) < '"
						+FechasUtil.formatDatetoString(new Date(), "yyyy-MM-dd")+"'  " ;
				}
			}
			
//			System.out.println("Consulta SQLNativ Finandet" + sql);
			//lstCuotas = ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, Finandet.class, true);
			lstCuotas = new RoQueryManager().executeSqlQuery(sql, Finandet.class, true);
			
			LogCajaService.CreateLog("getCuotasVencidas", "QRY", sql);
			
		}catch(Exception ex){
			ex.printStackTrace(); 
			LogCajaService.CreateLog("getCuotasVencidas", "ERR", ex.getMessage());
			 
		} 
		return lstCuotas;
	}
/********************************************************************************************/
	public List<Finandet> getDetalleSolicitud(String sCodcomp, String sCodsuc, int iNosol, String sTiposol, int iCodcli){
		List<Finandet> lstResult = null;
		
		try {
			
			String sql = " from Finandet as f where f.id.codcomp = '" +sCodcomp+"' " +
					" and f.id.codsuc = '" +sCodsuc+"' and f.id.nosol = " +iNosol+
					" and f.id.tiposol = '"+sTiposol+"' and f.id.codcli = " +iCodcli;
			
//			System.out.println("Consulta HQL Detalle Solicitud: " + sql);
			return lstResult = ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, Finandet.class, false);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
  
		return lstResult;
	}
/************OBTENER LOS INTERESES DE LAS CUOTAS**********************************************/
	public List getInteresesxCuota(Hfinan hfinan){
		List lstIntereses = null;
		Hfinan nhfinan = hfinan;
		Session session = HibernateUtilPruebaCn.currentSession();
		
		String sql = 	" from Hfinan as h where h.id.cuota = '"+ hfinan.getId().getCuota()+"' and h.id.codcli = " + hfinan.getId().getCodcli()  +" and h.id.nosol = "+hfinan.getId().getNosol()+
						" and h.id.codcomp = '"+hfinan.getId().getCodcomp()+"' and  h.id.tipofactura in('IF','MF') and h.id.montopend > 0 order by h.id.fecha";
		try{
		
			lstIntereses = session.createQuery(sql).list();
		
			
			lstIntereses = llenarHfinan(lstIntereses);
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			session.close();
		}
		return lstIntereses;
	}
/********************************************************************************************/		
/*****OBTENER EL CONSOLIDADO POR CUOTAS********************/
	public Hfinan getConsolidadoxCuota(Hfinan hfinan){
		Object[] o = null;
		Hfinan nhfinan = hfinan;
		Session session = HibernateUtilPruebaCn.currentSession();
		
		String sql = "select sum (h.id.montopend) as intereses,  (select sum (h.id.montopend) from Hfinan as h where h.id.codcli =" + hfinan.getId().getCodcli()  +
						" and h.id.cuota = '"+ hfinan.getId().getCuota()+"' and h.id.nosol = "+hfinan.getId().getNosol()+" and h.id.codcomp = '"+hfinan.getId().getCodcomp()+"' and  h.id.tipofactura in('IF','MF','"+hfinan.getId().getTipofactura()+"') and " +
						" h.id.montopend > 0 group by h.id.codcli, h.id.cuota, h.id.nosol, h.id.codcomp) as consolidado " + 
						" from Hfinan as h where h.id.codcli = " + hfinan.getId().getCodcli()  +" and h.id.cuota = '"+ hfinan.getId().getCuota()+"' and h.id.nosol = "+hfinan.getId().getNosol()+
						" and h.id.codcomp = '"+hfinan.getId().getCodcomp()+"' and  h.id.tipofactura in('IF','MF') and h.id.montopend > 0 " + 
						" group by h.id.codcli, h.id.cuota, h.id.nosol, h.id.codcomp";
		try{			
		
			o = (Object[])session.createQuery(sql).uniqueResult();
		
			
			nhfinan.setIntereses(new BigDecimal(o[0].toString()));
			nhfinan.setConsolidado(new BigDecimal(o[1].toString()));
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			session.close();
		}
		return nhfinan;
	}
/********************************************************************************************/	
/**LLENAR EL DETALLE DE ELEMENTOS A FINANCIAR POR SOLICITUD*****************/
	public List leerElementosxSolicitud(String sCodsuc, int iNosol, String sTiposol, int iCodcli){
		List lstResult = null;
		Session session = HibernateUtilPruebaCn.currentSession();
		
		Elementofin el = null;
		String sql = "from Elementofin as e where e.id.codsuc = '"+sCodsuc+"' and e.id.nosol = " +iNosol+" and e.id.tiposol = '" +sTiposol+"' and e.id.codcli = " +iCodcli;
		try{
		
			lstResult = session.createQuery(sql).list();
		
			for(int i = 0; i < lstResult.size(); i++){
				el = (Elementofin)lstResult.get(i);
				el.setTotal(el.getId().getTotal());
				lstResult.remove(i);
				lstResult.add(i, el);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			session.close();
		}
		return lstResult;
	}
/****LLENAR EL DETALLE DE PRODUCTOS POR SOLICITUD DE FINANCIAMIENTO*******************/
	public List<Producto> leerProductosxSolicitud(String sCodsuc,int iNosol,String sTiposol, int iCodcli){
		List<Producto> lstResult = null;
		
		try {
			
			String sql = "from Producto as p where p.id.codsuc = '"+sCodsuc+"' and p.id.nosol = " +iNosol+ " and p.id.tiposol = '"+sTiposol +"' and p.id.codcli = " +iCodcli;
			
			lstResult = ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, Producto.class, false);
			
		} catch (Exception e) {
			lstResult = null;
			e.printStackTrace();
		}
		
		return lstResult;
	}
/*****LLENAR LAS LISTAS DE ELEMENTOS CON LA INFO PARA EL GRID************/
	public List<Finanhdr> llenarHfinan(List<Finanhdr> lstCuotas){
		try{
			
			BigDecimal mora;
			Finanhdr f = null;
			
			for(int i = 0; i < lstCuotas.size();i++){
			
				f = (Finanhdr)lstCuotas.get(i);
				
		
				f.setTipofactura(f.getId().getTiposol());				
				f.setNosol(f.getId().getNosol());
				
				f.setNomcli(f.getId().getNomcli());
				f.setUnineg(f.getId().getNomlinea());
				f.setFecha(f.getId().getFecha());
				f.setMoneda(f.getId().getMoneda());
				
				f.setSelected(false);
				 
				mora = buscarMora311(f.getId().getNosol(),f.getId().getCodsuc(),f.getId().getCodcli());
				
				if(  f.getId().getMora().compareTo(BigDecimal.ZERO) == 1 ){
					mora = mora.add( f.getId().getMora() );
				}
				
				f.setMontopend( f.getId().getMontopend().add( mora ) );
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return lstCuotas;
	}
/*****OBTENER TODAS LAS CUOTAS PENDIENTES DE PAGO y VENCIDAS********************/
	public List getAllCuotas(){
		List lstCuotas = null;
		Session session = HibernateUtilPruebaCn.currentSession();
				
		try{
		
			lstCuotas = session.createQuery("from Hfinan as h where h.id.tipofactura not in('IF','MF') and h.id.atdven > 0 order by h.id.fechavenc asc").list();
		
			lstCuotas = llenarHfinan(lstCuotas);
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			session.close();
		}
		return lstCuotas;
	}
/********************************************************************************************/
	@SuppressWarnings("unchecked")
	public List buscarSegunTipoBusqueda(int iTipo,String sParametro){
		List lstResult =  null, sugerencias = new ArrayList();
		Session session = null;
		
		String sql = "";
		try{
			
			session = HibernateUtilPruebaCn.currentSession();
			
			
			switch (iTipo){
				case(1):
					
					//busqueda por nombre de cliente
					sql = "from Vf0101 as f where f.id.abalph like '"+sParametro.toUpperCase()+"%'";
					List<Vf0101> result  =  session.createQuery(sql)
												.setTimeout(10)
												.setMaxResults(35).list();
					for (Vf0101 vf : result) 
						sugerencias.add( vf.getId().getAbalph().trim() );
					break;
					
				case(2):

					//busqueda por codigo de cliente
					if(!sParametro.trim().matches("^[0-9]{1,8}$"))
						break;
					sql = "from Vf0101 as f where f.id.aban8 = "+sParametro ;	
					List<Vf0101> codsclts = session.createQuery(sql)
												.setTimeout(10)
												.setMaxResults(35).list();
					for (Vf0101 vf : codsclts) 
						sugerencias.add( String.valueOf(vf.getId().getAban8()) ) ;
					break;
					
				case(3):
					
					//busqueda por no. de solicitud
					sql = "from Finanhdr as f where f.id.nosol  = "+sParametro+"  and f.id.montopend > 0";
					List<Finanhdr>lstfinan = session.createQuery(sql)
												.setTimeout(10)
												.setMaxResults(35).list();
					for (Finanhdr fh : lstfinan) 
						sugerencias.add( String.valueOf(fh.getId().getNosol()) ); 
					break;
					
			}
			
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return sugerencias;
	}
/************************************************************************************************************************/
	public List buscarCuotasxParametros(int iTipo, String sParametro,String sCodcomp, String sMoneda){
		List lstResult =  null;
		Session session = HibernateUtilPruebaCn.currentSession();
		
		String sql = "from Finanhdr as f where ";
		
		
		
		try{
			
		
			//session.connection().setTransactionIsolation(Connection.TRANSACTION_NONE);
			
			switch (iTipo){
				case(1)://busqueda por nombre de cliente				
					if(!sParametro.trim().equals("")){//parametro primero
						sql = sql + " f.id.nomcli like '"+sParametro.toUpperCase()+"%' and ";
						if(!sCodcomp.equals("01")){
							sql = sql + " f.id.codcomp = '"+sCodcomp+"' and"; 
							if(!sMoneda.equals("01")){
								sql = sql + " f.id.moneda = '"+sMoneda+"' and"; 
							}
						}else{//no hay compania
							if(!sMoneda.equals("01")){
								sql = sql + " f.id.moneda = '"+sMoneda+"' and"; 
							}
						}
					}else{//no hay parametro compania va primero
						if(!sCodcomp.equals("01")){
							sql = sql + " f.id.codcomp = '"+sCodcomp+"' and"; 
							if(!sMoneda.equals("01")){
								sql = sql + " f.id.moneda = '"+sMoneda+"' and"; 
							}
						}else{//no hay compania
							if(!sMoneda.equals("01")){
								sql = sql + " f.id.moneda = '"+sMoneda+"' and"; 
							}
						}
					}				
					break;
				case(2)://busqueda por codigo de cliente
					if(!sParametro.trim().equals("")){//parametro primero
						sql = sql + " cast(f.id.codcli as string) like '"+sParametro+"%' and ";
						if(!sCodcomp.equals("01")){
							sql = sql + " f.id.codcomp = '"+sCodcomp+"' and"; 
							if(!sMoneda.equals("01")){
								sql = sql + " f.id.moneda = '"+sMoneda+"' and"; 
							}
						}else{//no hay compania
							if(!sMoneda.equals("01")){
								sql = sql + " f.id.moneda = '"+sMoneda+"' and"; 
							}
						}
					}else{//no hay parametro compania va primero
						if(!sCodcomp.equals("01")){
							sql = sql + " f.id.codcomp = '"+sCodcomp+"' and"; 
							if(!sMoneda.equals("01")){
								sql = sql + " f.id.moneda = '"+sMoneda+"' and"; 
							}
						}else{//no hay compania
							if(!sMoneda.equals("01")){
								sql = sql + " f.id.moneda = '"+sMoneda+"' and"; 
							}
						}
					}
					break;
				case(3)://busqueda por no. de solicitud
					if(!sParametro.trim().equals("")){//parametro primero
						sql = sql + " cast(f.id.nosol as string) like '"+sParametro+"%' and ";
						if(!sCodcomp.equals("01")){
							sql = sql + " f.id.codcomp = '"+sCodcomp+"' and"; 
							if(!sMoneda.equals("01")){
								sql = sql + " f.id.moneda = '"+sMoneda+"' and"; 
							}
						}else{//no hay compania
							if(!sMoneda.equals("01")){
								sql = sql + " f.id.moneda = '"+sMoneda+"' and"; 
							}
						}
					}else{//no hay parametro compania va primero
						if(!sCodcomp.equals("01")){
							sql = sql + " f.id.codcomp = '"+sCodcomp+"' and"; 
							if(!sMoneda.equals("01")){
								sql = sql + " f.id.moneda = '"+sMoneda+"' and"; 
							}
						}else{//no hay compania
							if(!sMoneda.equals("01")){
								sql = sql + " f.id.moneda = '"+sMoneda+"' and"; 
							}
						}
					}
					break;
				/*case(4)://busqueda por no. de factura
					if(!sParametro.trim().equals("")){//parametro primero
						sql = sql + " cast(f.id.nofactura as string) like '"+sParametro+"%' and ";
						if(!sCodcomp.equals("01")){
							sql = sql + " f.id.codcomp = '"+sCodcomp+"' and"; 
							if(!sMoneda.equals("01")){
								sql = sql + " f.id.moneda = '"+sMoneda+"' and"; 
							}
						}else{//no hay compania
							if(!sMoneda.equals("01")){
								sql = sql + " f.id.moneda = '"+sMoneda+"' and"; 
							}
						}
					}else{//no hay parametro compania va primero
						if(!sCodcomp.equals("01")){
							sql = sql + " f.id.codcomp = '"+sCodcomp+"' and"; 
							if(!sMoneda.equals("01")){
								sql = sql + " f.id.moneda = '"+sMoneda+"' and"; 
							}
						}else{//no hay compania
							if(!sMoneda.equals("01")){
								sql = sql + " f.id.moneda = '"+sMoneda+"' and"; 
							}
						}
					}
					break;*/
			}		
			sql = sql + " f.id.montopend > 0";
			lstResult = session.createQuery(sql).list();
			
		
			
			lstResult = llenarHfinan(lstResult);
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			try { session.close(); } catch (Exception e2){}
		}
		return lstResult;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Finanhdr> buscarFinancimiento(int iTipo, String sParametro, String sCodcomp, String sMoneda){
		Session sesion = null;

		List<Finanhdr>lstFinans = null;
		int parameter = 0;
		
		try {
			 
			sesion = HibernateUtilPruebaCn.currentSession();

			Criteria cr = sesion.createCriteria(Finanhdr.class);
			cr.add(Restrictions.gt("id.montopend", BigDecimal.ZERO));
			
			if( sCodcomp.compareTo("01") != 0){
				cr.add(Restrictions.eq("id.codcomp", sCodcomp));
				parameter ++ ;
			}
			if( sMoneda.compareTo("01") != 0  ){
				cr.add(Restrictions.eq("id.moneda", sMoneda));
				parameter ++ ;
			}
			
			switch(iTipo){
			case(1):
				if( !sParametro.trim().isEmpty() ) {
					cr.add(Restrictions.ilike("id.nomcli", sParametro.trim()
											.toLowerCase(),MatchMode.START));
					parameter ++ ;
				}
				break;
			case(2):
				if(sParametro.trim().matches("^[0-9]{1,8}$")){
					cr.add(Restrictions.eq("id.codcli",Integer.valueOf(sParametro.trim()).intValue()));
					parameter ++ ;
				}
				break;
			case(3):
				if(sParametro.trim().matches("^[0-9]+")) {
					cr.add(Restrictions.eq("id.nosol",Integer.valueOf(sParametro.trim()).intValue()));
					parameter ++ ;
				}
				break;
			}
			
			if( parameter == 0){
				cr.setMaxResults(50);
			}
			
			// Log
			String sqlCriteria = LogCajaService.toSql(cr);
			LogCajaService.CreateLog("buscarFinancimiento", "QRY", sqlCriteria);
			
			lstFinans =	(ArrayList<Finanhdr>)cr.list();
			
			if(lstFinans == null  || lstFinans.isEmpty() )
				return null;
			
			lstFinans =   llenarHfinan(lstFinans);
			
		} catch (Exception e) {
			e.printStackTrace();
			LogCajaService.CreateLog("buscarFinancimiento", "ERR", e.getMessage());
		}

		return lstFinans;
	}
	
	public String buscarFinanciamientoSpec(int codcli, String codcomp, String codsuc, int numrecibo ) {
		
		String returnValue = "";
		String consulta ="SELECT NUMEROSOLICITUD FROM "+PropertiesSystem.ESQUEMA+".HISTORICO_CUOTAS_FINAN WHERE " + 
				"CODIGOCLIENTE = "+codcli+" AND CODCOMP = '"+codcomp.trim()+"' AND NUMREC="+numrecibo;
		
		@SuppressWarnings("unchecked")
		List<Object> numREC =(List<Object>)  ConsolidadoDepositosBcoCtrl.executeSqlQuery(consulta, true, null);
		if(numREC.size()>0)
			returnValue = numREC.get(0).toString();
		
		return returnValue;
	}
	
}
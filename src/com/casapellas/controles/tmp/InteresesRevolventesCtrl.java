package com.casapellas.controles.tmp;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.casapellas.controles.ConsolidadoDepositosBcoCtrl;
import com.casapellas.controles.CuotaCtrl;
import com.casapellas.entidades.Credhdr;
import com.casapellas.entidades.F55ca014;
import com.casapellas.entidades.F55diaven;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.entidades.F55cabintr;
import com.casapellas.entidades.F55detinte;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.PropertiesSystem;


public class InteresesRevolventesCtrl {
/**************************************************************************************************************************/	
	public boolean verificarSiCobraInteres(int codcli){
		boolean cobra = false;
		String sql = "";
 
		try{
			
			sql =  " select  aiafc from " +PropertiesSystem.JDEDTA+".f03012  where aian8 = "+codcli;
			
			LogCajaService.CreateLog("verificarSiCobraInteres", "QRY", sql);
		 
			@SuppressWarnings("unchecked")
			List<Object> dtaCliente = (List<Object>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, true, null);
			
			return cobra = (dtaCliente != null && !dtaCliente.isEmpty() && String.valueOf( dtaCliente.get(0) ) .compareTo("Y") == 0 ) ;
			
		}catch(Exception ex){
			LogCajaService.CreateLog("verificarSiCobraInteres", "ERR", ex.getMessage());
			cobra = false;
			ex.printStackTrace(); 
		} 
		return cobra;
	}
	
	@SuppressWarnings("unchecked")
	public List<Credhdr> leerInteresesRevxCliente(String codsuc, int codcli, String tiposol,String moneda,F55ca014[] f14){
		List<Credhdr> lstCuotas = null;
		
		try {
			
			String sql  = " select * from @BDCAJA.credhdr " +
					" where CODCLI = @CODCLI " +
					" and trim(MONEDA) = '@MONEDA' " +
					" and trim(CODSUC) = '@CODSUC' " +
					" and trim(RPDCTO) = '@RPDCTO' " +
					" and TIPOFACTURA in ('IF', 'MF')";
					
			sql = sql.replace("@BDCAJA", PropertiesSystem.ESQUEMA )
					.replace("@CODCLI", String.valueOf(codcli) )
					.replace("@MONEDA", moneda )
					.replace("@CODSUC", codsuc.trim() )
					.replace("@RPDCTO", tiposol ) ;
			 
			LogCajaService.CreateLog("leerInteresesRevxCliente", "QRY", sql);
			
			lstCuotas = (List<Credhdr>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, true, Credhdr.class);		
			
			if(lstCuotas == null || lstCuotas.isEmpty() ){
				return null;
			}
			
			
			CuotaCtrl.llenarInfoFactura(lstCuotas, f14);  
			
		} catch (Exception e) {
			LogCajaService.CreateLog("leerInteresesRevxCliente", "ERR", e.getMessage());
			e.printStackTrace(); 
		}
		
		return lstCuotas;
	}
		
	
/**************************************************************************************************************************/	
	@SuppressWarnings("unchecked")
	public List<Credhdr> leerInteresesRevxCliente1(String codsuc, int codcli, String tiposol,String moneda,F55ca014[] f14){
		
		List<Credhdr> lstCuotas = new ArrayList<Credhdr>();

		try{
/*			sql = "SELECT CAST(TRIM(RPDCT) AS VARCHAR(2) CCSID 37) TIPOFACTURA,RPDOC AS NOFACTURA,CAST (RPSFX AS VARCHAR(3) CCSID 37) AS PARTIDA," +
					"RPAN8 AS CODCLI, RPALPH AS NOMCLI, CAST(TRIM(RPMCU) AS VARCHAR(12) CCSID 37) CODUNINEG, " +
					" UN.MCDL01 AS UNINEG, CAST(RPKCO AS VARCHAR(5) CCSID 37) CODSUC, " + 
					 "(SELECT MCDL01 FROM "+PropertiesSystem.JDEDTA+".F0006 WHERE SUBSTRING(RPKCO,4,5) = SUBSTRING(MCMCU,11,12) AND MCSTYL = 'BS') AS NOMSUC, " +
					 "CAST(TRIM(UN.MCRP01) AS VARCHAR(3) CCSID 37) CODCOMP, " + 
					" CO.DRDL01 AS NOMCOMP, CAST(TRIM(RPCRCD) AS VARCHAR(3) CCSID 37) MONEDA, RPCRR AS TASA, CAST(RPAAP/100 AS DECIMAL (12,2)) AS CPENDIENTE," +
					"CAST(RPFAP/100 AS DECIMAL (12,2)) AS DPENDIENTE, " +
					" CAST(RPAG/100 AS DECIMAL (12,2)) AS CTOTAL,CAST(RPACR/100 AS DECIMAL (12,2)) AS DTOTAL,CAST(RPSTAM/100 AS DECIMAL (12,2)) AS CIMPUESTO," +
					"CAST(RPCTAM/100 AS DECIMAL (12,2)) AS DIMPUESTO, " +
					"CAST(RPATXA/100 AS DECIMAL (12,2)) AS CSUBTOTAL,CAST(RPCTXA/100 AS DECIMAL (12,2)) AS DSUBTOTAL, " +
					"CAST((CAST(( CASE WHEN RPCRCD = 'COR' " +
					"THEN RPAG ELSE RPACR END) AS DECIMAL(12,2))/100) AS DECIMAL(12,2)) MONTO," +
					"CAST((CAST(( CASE WHEN RPCRCD = 'COR' THEN RPAAP ELSE RPFAP END) AS DECIMAL(12,2))/100) AS DECIMAL(12,2)) MONTOPEND, " +
					 "CAST((CAST(( CASE WHEN RPCRCD = 'COR' THEN RPATXA ELSE RPCTXA END) AS DECIMAL(12,2))/100) AS DECIMAL(12,2)) SUBTOTAL," +
					 "CAST((CAST(( CASE WHEN RPCRCD = 'COR' THEN RPSTAM ELSE RPCTAM END) " + 
							 "AS DECIMAL(12,2))/100) AS DECIMAL(12,2)) IMPUESTO,CAST ((CAST(DATE(CHAR(1900000 + RPDIVJ)) AS TIMESTAMP)) AS DATE) FECHA, " +
					"CAST ((CAST(DATE(CHAR(1900000 + RPDDJ)) AS TIMESTAMP)) AS DATE) FECHAVENC,CAST(RPGLC AS VARCHAR(4) CCSID 37) AS COMPENSLM," +
					"CAST(TRIM(RPPTC) AS VARCHAR(3) CCSID 37) TIPOPAGO, " +
					"RPDIVJ,RPDDJ,CAST (RPPO AS VARCHAR(8) CCSID 37) RPPO, CAST (RPDCTO AS VARCHAR(2) CCSID 37) RPDCTO " +
					 "from "+PropertiesSystem.JDEDTA+".f0311 INNER JOIN "+PropertiesSystem.JDEDTA+".F0006 UN ON RPMCU = UN.MCMCU INNER JOIN "+PropertiesSystem.JDECOM+".F0005 CO " +
					 "ON (UN.MCRP01 = SUBSTRING(CO.DRKY,8,10) AND CO.DRSY = '00' AND CO.DRRT = '01' AND CO.DRDL02 = 'F') " +
						"where  rpkco = '"+codsuc+"' and rpan8 = "+codcli+" and rpdct = 'MF' and rpaap > 0  and  rpdcto = '"+tiposol+"' and rpcrcd = '"+moneda+"' " +
					"UNION "+
					"SELECT CAST(TRIM(RPDCT) AS VARCHAR(2) CCSID 37) TIPOFACTURA,RPDOC AS NOFACTURA,CAST (RPSFX AS VARCHAR(3) CCSID 37) AS PARTIDA,RPAN8 AS CODCLI," +
					" RPALPH AS NOMCLI, CAST(TRIM(RPMCU) AS VARCHAR(12) CCSID 37) CODUNINEG, UN.MCDL01 AS UNINEG, " +
					"CAST(RPKCO AS VARCHAR(5) CCSID 37) CODSUC, (SELECT MCDL01 FROM "+PropertiesSystem.JDEDTA+".F0006 WHERE SUBSTRING(RPKCO,4,5) = SUBSTRING(MCMCU,11,12) AND MCSTYL = 'BS') AS NOMSUC," +
					"CAST(TRIM(UN.MCRP01) AS VARCHAR(3) CCSID 37) CODCOMP, CO.DRDL01 AS NOMCOMP, CAST(TRIM(RPCRCD) AS VARCHAR(3) CCSID 37) MONEDA," +
					" RPCRR AS TASA, CAST(RPAAP/100 AS DECIMAL (12,2)) AS CPENDIENTE,CAST(RPFAP/100 AS DECIMAL (12,2)) AS DPENDIENTE, "+
					"CAST(RPAG/100 AS DECIMAL (12,2)) AS CTOTAL,CAST(RPACR/100 AS DECIMAL (12,2)) AS DTOTAL," +
					"CAST(RPSTAM/100 AS DECIMAL (12,2)) AS CIMPUESTO,CAST(RPCTAM/100 AS DECIMAL (12,2)) AS DIMPUESTO, " +
					"CAST(RPATXA/100 AS DECIMAL (12,2)) AS CSUBTOTAL,CAST(RPCTXA/100 AS DECIMAL (12,2)) AS DSUBTOTAL, " +
					"CAST((CAST(( CASE WHEN RPCRCD = 'COR' THEN RPAG ELSE RPACR END) AS DECIMAL(12,2))/100) AS DECIMAL(12,2)) MONTO," +
					"CAST((CAST(( CASE WHEN RPCRCD = 'COR' THEN RPAAP ELSE RPFAP END) AS DECIMAL(12,2))/100) AS DECIMAL(12,2)) MONTOPEND, "+
					 "CAST((CAST(( CASE WHEN RPCRCD = 'COR' THEN RPATXA ELSE RPCTXA END) AS DECIMAL(12,2))/100) AS DECIMAL(12,2)) SUBTOTAL," +
					 "CAST((CAST(( CASE WHEN RPCRCD = 'COR' THEN RPSTAM ELSE RPCTAM END) "+
							 "	AS DECIMAL(12,2))/100) AS DECIMAL(12,2)) IMPUESTO,CAST ((CAST(DATE(CHAR(1900000 + RPDIVJ)) AS TIMESTAMP)) AS DATE) FECHA, " +
					"CAST ((CAST(DATE(CHAR(1900000 + RPDDJ)) AS TIMESTAMP)) AS DATE) FECHAVENC,CAST(RPGLC AS VARCHAR(4) CCSID 37) AS COMPENSLM," +
					"CAST(TRIM(RPPTC) AS VARCHAR(3) CCSID 37) TIPOPAGO, " + 
					"RPDIVJ,RPDDJ,CAST (RPPO AS VARCHAR(8) CCSID 37) RPPO, CAST (RPDCTO AS VARCHAR(2) CCSID 37) RPDCTO " +
					 "from "+PropertiesSystem.JDEDTA+".f0311 INNER JOIN "+PropertiesSystem.JDEDTA+".F0006 UN ON RPMCU = UN.MCMCU INNER JOIN "+PropertiesSystem.JDECOM+".F0005 CO ON " +
					 "(UN.MCRP01 = SUBSTRING(CO.DRKY,8,10) AND CO.DRSY = '00' AND CO.DRRT = '01' AND CO.DRDL02 = 'F') " +
						"where  rpkco = '"+codsuc+"' and rpan8 = "+codcli+" and rpdct = 'IF' and rpaap > 0  and  rpdcto = '"+tiposol+"' and rpcrcd = '"+moneda+"' ";*/
			
			String sql  = 
					"SELECT CAST(TRIM(RPDCT) AS VARCHAR(2) CCSID 37) TIPOFACTURA,RPDOC AS NOFACTURA,CAST (RPSFX AS VARCHAR(3) CCSID 37) AS PARTIDA," +
					"RPAN8 AS CODCLI, RPALPH AS NOMCLI, CAST(TRIM(RPMCU) AS VARCHAR(12) CCSID 37) CODUNINEG, " +
					" UN.MCDL01 AS UNINEG, CAST(RPKCO AS VARCHAR(5) CCSID 37) CODSUC, " + 
					
					"(SELECT MCDL01 FROM "+PropertiesSystem.JDEDTA+".F0006 WHERE SUBSTRING(RPKCO,4,5) = SUBSTRING(MCMCU,11,12) AND MCSTYL = 'BS' FETCH FIRST ROWS ONLY ) AS NOMSUC,	 "+
					
					"CAST(TRIM(UN.MCRP01) AS VARCHAR(3) CCSID 37) CODCOMP, " + 
					"CO.DRDL01 AS NOMCOMP, CAST(TRIM(RPCRCD) AS VARCHAR(3) CCSID 37) MONEDA, RPCRR AS TASA, CAST(RPAAP/100 AS DECIMAL (12,2)) AS CPENDIENTE," +
					"CAST(RPFAP/100 AS DECIMAL (12,2)) AS DPENDIENTE, " +
					"CAST(RPAG/100 AS DECIMAL (12,2)) AS CTOTAL,CAST(RPACR/100 AS DECIMAL (12,2)) AS DTOTAL,CAST(RPSTAM/100 AS DECIMAL (12,2)) AS CIMPUESTO," +
					"CAST(RPCTAM/100 AS DECIMAL (12,2)) AS DIMPUESTO, " +
					"CAST(RPATXA/100 AS DECIMAL (12,2)) AS CSUBTOTAL,CAST(RPCTXA/100 AS DECIMAL (12,2)) AS DSUBTOTAL, " +
					"CAST((CAST(( CASE WHEN RPCRCD = 'COR' THEN RPAG ELSE RPACR END) AS DECIMAL(12,2))/100) AS DECIMAL(12,2)) MONTO," +
					"CAST((CAST(( CASE WHEN RPCRCD = 'COR' THEN RPAAP ELSE RPFAP END) AS DECIMAL(12,2))/100) AS DECIMAL(12,2)) MONTOPEND, " +
					"CAST((CAST(( CASE WHEN RPCRCD = 'COR' THEN RPATXA ELSE RPCTXA END) AS DECIMAL(12,2))/100) AS DECIMAL(12,2)) SUBTOTAL," +
					"CAST((CAST(( CASE WHEN RPCRCD = 'COR' THEN RPSTAM ELSE RPCTAM END) AS DECIMAL(12,2))/100) AS DECIMAL(12,2)) IMPUESTO," +
					"CAST ((CAST(DATE(CHAR(1900000 + RPDIVJ)) AS TIMESTAMP)) AS DATE) FECHA, " +
					"CAST ((CAST(DATE(CHAR(1900000 + RPDDJ)) AS TIMESTAMP)) AS DATE) FECHAVENC, " +
					"CAST(RPGLC AS VARCHAR(4) CCSID 37) AS COMPENSLM, " +
					"CAST(TRIM(RPPTC) AS VARCHAR(3) CCSID 37) TIPOPAGO, " +
					"RPDIVJ,RPDDJ,CAST (RPPO AS VARCHAR(8) CCSID 37) RPPO, " +
					"CAST (RPDCTO AS VARCHAR(2) CCSID 37) RPDCTO " +
					"from "+PropertiesSystem.JDEDTA+".f0311 INNER JOIN "+PropertiesSystem.JDEDTA+".F0006 UN ON RPMCU = UN.MCMCU INNER JOIN "+PropertiesSystem.JDECOM+".F0005 CO " +
					"ON (UN.MCRP01 = SUBSTRING(CO.DRKY,8,10) AND CO.DRSY = '00' AND CO.DRRT = '01' AND CO.DRDL02 = 'F') " +
					"where  rpkco = '"+codsuc+"' and rpan8 = "+codcli+" and rpdct = 'MF' and rpaap > 0  and  rpdcto = '"+tiposol+"' and rpcrcd = '"+moneda+"' " +
					
					" UNION "+
					
					"SELECT CAST(TRIM(RPDCT) AS VARCHAR(2) CCSID 37) TIPOFACTURA, RPDOC AS NOFACTURA, " +
					"CAST (RPSFX AS VARCHAR(3) CCSID 37) AS PARTIDA,RPAN8 AS CODCLI," +
					"RPALPH AS NOMCLI, CAST(TRIM(RPMCU) AS VARCHAR(12) CCSID 37) CODUNINEG, UN.MCDL01 AS UNINEG, " +
					"CAST(RPKCO AS VARCHAR(5) CCSID 37) CODSUC, " +
					"(SELECT MCDL01 FROM "+PropertiesSystem.JDEDTA+".F0006 WHERE SUBSTRING(RPKCO,4,5) = SUBSTRING(MCMCU,11,12) AND MCSTYL = 'BS') AS NOMSUC," +
					"CAST(TRIM(UN.MCRP01) AS VARCHAR(3) CCSID 37) CODCOMP, CO.DRDL01 AS NOMCOMP, " +
					"CAST(TRIM(RPCRCD) AS VARCHAR(3) CCSID 37) MONEDA," +
					"RPCRR AS TASA, CAST(RPAAP/100 AS DECIMAL (12,2)) AS CPENDIENTE," +
					"CAST(RPFAP/100 AS DECIMAL (12,2)) AS DPENDIENTE, "+
					"CAST(RPAG/100 AS DECIMAL (12,2)) AS CTOTAL," +
					"CAST(RPACR/100 AS DECIMAL (12,2)) AS DTOTAL," +
					"CAST(RPSTAM/100 AS DECIMAL (12,2)) AS CIMPUESTO," +
					"CAST(RPCTAM/100 AS DECIMAL (12,2)) AS DIMPUESTO, " +
					"CAST(RPATXA/100 AS DECIMAL (12,2)) AS CSUBTOTAL," +
					"CAST(RPCTXA/100 AS DECIMAL (12,2)) AS DSUBTOTAL, " +
					"CAST((CAST(( CASE WHEN RPCRCD = 'COR' THEN RPAG ELSE RPACR END) AS DECIMAL(12,2))/100) AS DECIMAL(12,2)) MONTO," +
					"CAST((CAST(( CASE WHEN RPCRCD = 'COR' THEN RPAAP ELSE RPFAP END) AS DECIMAL(12,2))/100) AS DECIMAL(12,2)) MONTOPEND, "+
					"CAST((CAST(( CASE WHEN RPCRCD = 'COR' THEN RPATXA ELSE RPCTXA END) AS DECIMAL(12,2))/100) AS DECIMAL(12,2)) SUBTOTAL," +
					"CAST((CAST(( CASE WHEN RPCRCD = 'COR' THEN RPSTAM ELSE RPCTAM END) AS DECIMAL(12,2))/100) AS DECIMAL(12,2)) IMPUESTO," +
					"CAST ((CAST(DATE(CHAR(1900000 + RPDIVJ)) AS TIMESTAMP)) AS DATE) FECHA, " +
					"CAST ((CAST(DATE(CHAR(1900000 + RPDDJ)) AS TIMESTAMP)) AS DATE) FECHAVENC," +
					"CAST(RPGLC AS VARCHAR(4) CCSID 37) AS COMPENSLM," +
					"CAST(TRIM(RPPTC) AS VARCHAR(3) CCSID 37) TIPOPAGO, " + 
					"RPDIVJ,RPDDJ,CAST (RPPO AS VARCHAR(8) CCSID 37) RPPO, CAST (RPDCTO AS VARCHAR(2) CCSID 37) RPDCTO " +
					
					"FROM "+PropertiesSystem.JDEDTA+".f0311 INNER JOIN "+PropertiesSystem.JDEDTA+".F0006 UN ON RPMCU = UN.MCMCU INNER JOIN "+PropertiesSystem.JDECOM+".F0005 CO ON " +
					"(UN.MCRP01 = SUBSTRING(CO.DRKY,8,10) AND CO.DRSY = '00' AND CO.DRRT = '01' AND CO.DRDL02 = 'F') " +
					"WHERE  rpkco = '"+codsuc+"' and rpan8 = "+codcli+" and rpdct = 'IF' and rpaap > 0  and  rpdcto = '"+tiposol+"' and rpcrcd = '"+moneda+"' ";
			
			lstCuotas = (ArrayList<Credhdr>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, true, Credhdr.class) ;
			
			if( lstCuotas  == null || lstCuotas.isEmpty() ){
				return lstCuotas = new ArrayList<Credhdr>() ;
			}
			
			 
			lstCuotas = CuotaCtrl.llenarInfoFactura(lstCuotas, f14);
			
			
		} catch (Exception ex) {
			ex.printStackTrace(); 
		} 
		
		return lstCuotas;
	}
/*********************************************************************************/	
	public List<F55diaven> getInteresesPendientes(){
		List<F55diaven> lstResult = new ArrayList<F55diaven>();
		String sql = "";
		Session s = null;
		Transaction tx = null;
		try{
			sql = "from F55diaven";
			s = HibernateUtilPruebaCn.currentSession();
			
			lstResult = s.createQuery(sql).list();
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return lstResult;
	}
	
/*********************************************************************************/	
	public List<F55cabintr> getInteresesAplicados(){
		List<F55cabintr> lstResult = new ArrayList<F55cabintr>();
		String sql = "";
		Session s = null;
		Transaction tx = null;
		try{
			sql = "from F55cabintr";
			s = HibernateUtilPruebaCn.currentSession();
			
			lstResult = s.createQuery(sql).list();
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return lstResult;
	}
	
/*********************************************************************************/	
	public List<F55detinte> getDetalleInteresesAplicados(){
		List<F55detinte> lstResult = new ArrayList<F55detinte>();
		String sql = "";
		Session s = null;
		Transaction tx = null;
		try{
			sql = "from F55detinte";
			s = HibernateUtilPruebaCn.currentSession();
			
			lstResult = s.createQuery(sql).list();
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return lstResult;
	}
	
/*********************************************************************************/
	
}

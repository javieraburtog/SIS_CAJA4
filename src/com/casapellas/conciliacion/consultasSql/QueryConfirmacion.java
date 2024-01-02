package com.casapellas.conciliacion.consultasSql;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import sun.util.logging.resources.logging;
import com.casapellas.controles.ConfirmaDepositosCtrl;
import com.casapellas.controles.ConsolidadoDepositosBcoCtrl;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.util.PropertiesSystem;

public class QueryConfirmacion {

	public  int totalRegs  = 0;
	  


	@SuppressWarnings("unchecked")
	public List<Object[]> cargarDepositoCaja(String strFechaIni, String strFechaFin,int pagina, int cantxpagina,String strSortName, String strSortOrden,String strQtype,String strQuery,List<Object[]> lstConfCta,List<Object[]> lstConfC){
		Session sesion = null;
		Transaction trans = null; 
		List<Object[]> result  = new ArrayList<Object[]>();  
		boolean newCn = false;
		
		try{
			
			sesion = HibernateUtilPruebaCn.currentSession() ;
			trans = (newCn = !(sesion.getTransaction().isActive())) ? sesion
					.beginTransaction() : sesion.getTransaction();  
					
					String strCondicionCuenta = "";
					String strCondicionMoneda = "";
					String strCondicionCompania = "";
					for (Iterator iterator = lstConfCta.iterator(); iterator.hasNext();) {
						Object[] objects = (Object[]) iterator.next();
						strCondicionCuenta+= objects[0];
						strCondicionMoneda+= "'"+objects[3]+"'";
						strCondicionCompania += "'"+objects[2]+"'";
						if(iterator.hasNext()){  
							strCondicionCuenta+= ",";
							strCondicionMoneda+= ",";
							strCondicionCompania+= ",";
						}
					}  	
					String strInCuentaMoneda = "";
					if(lstConfCta.size()>0){
						strInCuentaMoneda = " AND IDBANCO IN("+strCondicionCuenta+") AND MONEDA IN("+strCondicionMoneda+") AND COMPANIA IN("+strCondicionCompania+") " ;
					}	
 
				String strCampos = 
					" IDCUENTA CUENTA, REFERENCIA, M.MONTO, " +
					" IFNULL( (SELECT BANCO FROM "+PropertiesSystem.ESQUEMA+".F55CA022 WHERE CODB =  IDBANCO FETCH FIRST ROWS ONLY),'') BANCO, " +
					" MONEDA, FECHA, IDBANCO, " +
					" IFNULL((SELECT F.CAID||' - '|| F.CANAME FROM  "+PropertiesSystem.ESQUEMA+".F55CA01 F WHERE F.CAID = M.CAID FETCH FIRST ROWS ONLY),'') CAJA, " +
					" CONSECUTIVO, ESTADOCNFR, TRIM(GMMCU)||'.'||TRIM(GMOBJ)||'.'||TRIM(GMSUB) CUENTAB, CODCOMP COMPANIA, M.CAID CCAJA, TIPOMOV  ";	

				String strWhere = 
					" WHERE estadocnfr = 'CFR' AND TIPOMOV = 'C' " + 
					 strInCuentaMoneda  + ( strFechaIni.compareToIgnoreCase("") != 0 ? 
					" AND  FECHA BETWEEN '"+strFechaIni+"' AND '" +(strFechaFin.compareToIgnoreCase("") != 0 ? strFechaFin:strFechaIni )+ "'" : "" ) + 
					( strQuery.trim().isEmpty() ? "" : " and lower("+strQtype+") like lower('%"+strQuery+"%')" ) ;	
					
				String strSqlQuery = " SELECT @camposC FROM (SELECT @camposT FROM "+PropertiesSystem.ESQUEMA+".DEPOSITO M INNER JOIN " +
						PropertiesSystem.ESQUEMA+".CTAXDEPSCJ  D  ON( M.CONSECUTIVO = D.IDDEPOSITO)  " +
						" and depctatran = 1 and tipodep = 'D' AND trim(MPAGODEP) <> 'X' and tipoconfr = 'DSE') AS RESULTADOS @where  @order" ;
				
				String queryExecute = strSqlQuery
						.replace("@camposC", "*")
						.replace("@camposT", strCampos) 
						.replace("@order","ORDER BY "+( !strSortName.trim().isEmpty() ? strSortName : "fecha") +" "+ strSortOrden)
						.replace("@where",strWhere) ;
				
				result = (List<Object[]>) sesion.createSQLQuery(queryExecute).setFirstResult( (pagina-1)*cantxpagina ).setMaxResults(cantxpagina).list(); 
				totalRegs = (Integer)(sesion.createSQLQuery(strSqlQuery.replace("@camposC", " count(*) ").replace("@camposT", strCampos).replace("@order", "").replace("@where",strWhere)).list()).get(0);
				
		}catch(Exception ex){    
			ex.printStackTrace();
		}finally{
			
			if(newCn){
				try {  trans.commit(); } 
				catch (Exception e2) { }
				try {  HibernateUtilPruebaCn.closeSession(sesion); }
				catch (Exception e2) { }
			}
			sesion = null;
			trans = null;
		}	
		return result;
	}  
	
	@SuppressWarnings("unchecked")
	public List<Object[]> cargarDepositoBanco(String strFechaIni, String strFechaFin,int pagina, int cantxpagina,String strSortName, String strSortOrden,String strQtype,String strQuery,List<Object[]> lstConf){
		Session sesion = null;
		Transaction trans = null; 
		List<Object[]> result  = new ArrayList<Object[]>();  
		boolean newCn = false;
		
		try{
			
			sesion = HibernateUtilPruebaCn.currentSession() ;
			trans = (newCn = !(sesion.getTransaction().isActive())) ? sesion
					.beginTransaction() : sesion.getTransaction(); 
					
			String strCondicionCuenta = "";
			String strCondicionMoneda = "";
			
			if(lstConf!=null) {
			for (Iterator iterator = lstConf.iterator(); iterator.hasNext();) {
				Object[] objects = (Object[]) iterator.next();
				strCondicionCuenta+= objects[0];
				strCondicionMoneda+= "'"+objects[3]+"'";
				if(iterator.hasNext()){
					strCondicionCuenta+= ",";
					strCondicionMoneda+= ",";
				}
			}  	
			String strInCuentaMoneda = "";
			if(lstConf.size()>0){
				strInCuentaMoneda = " AND IDBANCO IN("+strCondicionCuenta+") AND MONEDA IN("+strCondicionMoneda+") " ;
			}

			String strDateCond = (strFechaIni.compareToIgnoreCase("") != 0 ? " and  fecha >= '"+strFechaIni+ "'" :"") + " " + 
								 (strFechaFin.compareToIgnoreCase("") != 0 ? " and  fecha <= '"+strFechaFin+ "'" :"") + " " ;
			
			String strOrderBy = (strSortName.trim().compareToIgnoreCase("") != 0 ? strSortName: "fecha" ) +" "+ strSortOrden ;
			
			String strFilterby = ( strQuery.trim().isEmpty() ) ?"" : " and lower ("+strQtype+")  like lower ('%"+strQuery+"%')"; 
			
			String strQueryTmp = 
				" select " +
				" pcd.numerocuenta CUENTA,  pcd.referenciaoriginal REFERENCIA, pcd.montooriginal MONTO, " +
				" pcd.nombrebanco  BANCO, pcd.moneda , pcd.fechadeposito FECHA, pcd.codigobanco IDBANCO,  " +
				" pcd.IDDEPBCODET, pcd.estadoconfirmacion IDESTADOCNFR,  " +
				" IFNULL((SELECT  TRIM(D3MCU)||'.'||TRIM(D3OBJ)||'.'||TRIM(D3SUB) CUENTA  FROM "+PropertiesSystem.ESQUEMA+".F55CA023 WHERE TRIM(D3CODB) = TRIM(pcd.codigobanco) " + 
				" AND  TRIM(D3RP01) = TRIM(pcd.CODCOMP)	AND  TRIM(D3CRCD) = TRIM(pcd.MONEDA) FETCH FIRST ROWS ONLY),'') CUENTAB, " +
				" pcd.IDRESUMENBANCO, " + 
				" pcd.codcomp " +
				" from "+PropertiesSystem.ESQUEMA+".pcd_consolidado_depositos_banco pcd" ;
			
			String strQueryResult = " select * from (@QueryInner) as RESULTADOS where  IDESTADOCNFR = 3 "  +
						strInCuentaMoneda +
						strDateCond + 
						strFilterby +
						" order by " + strOrderBy;
			
			result = (List<Object[]>)sesion.createSQLQuery(strQueryResult.replace("@QueryInner", strQueryTmp) ).list() ;
			totalRegs = (result == null )? 0 : result.size() ;
			
			result = (List<Object[]>) sesion.createSQLQuery(strQueryResult
					.replace("@QueryInner", strQueryTmp) )
					.setFirstResult((pagina-1)*cantxpagina)
					.setMaxResults(cantxpagina).list(); 
			}
		}catch(Exception ex){    
			ex.printStackTrace();
		}finally{
			
			if(newCn){
				try {  trans.commit(); } 
				catch (Exception e2) { }
				try {  HibernateUtilPruebaCn.closeSession(sesion); }
				catch (Exception e2) { }
			}
			sesion = null;
			trans = null;  
		}	
		return result;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Object[]> cargarConfiguracionAjustes_Contador(String strFechaIni, String strFechaFin,int pagina, int cantxpagina,String strSortName, String strSortOrden,String strQtype,String strQuery,String strKind,String strRestriction){
		Session sesion = null;
		Transaction trans = null; 
		List<Object[]> result  = new ArrayList<Object[]>();  
		boolean newCn = false;
		
		try{
			
			sesion = HibernateUtilPruebaCn.currentSession() ;
			trans = (newCn = !(sesion.getTransaction().isActive())) ? sesion.beginTransaction() : sesion.getTransaction(); 
			
			/*String strWhere = "WHERE "+strRestriction+" ESTADO= "+Integer.parseInt(strKind)+(strFechaIni.compareToIgnoreCase("")!=0?" AND  FECHA BETWEEN '"+strFechaIni+"'  AND '"+(strFechaFin.compareToIgnoreCase("")!=0?strFechaFin:strFechaIni)+"'":"");
			strWhere+= (strQuery.trim().compareToIgnoreCase("")!=0?" AND LOWER("+strQtype+") LIKE LOWER('%"+strQuery+"%')" :"") ;
			*/
			String strSql =
			" select " +
			" fechacrea," +                                                                     
			" (select login from ens.usuario where codreg = usercrea fetch first rows only)," +   
			" monto_Transaccion," +                                                                    
			" moneda," +                                                             
			" codcomp," +
			" (select trim(drdl01) from " + PropertiesSystem.ENS + ".vcompania where drky = CODCOMP)," +
			" idajusteexcepcion," +
			" cantidad_documentos " + 
			" from "+PropertiesSystem.ESQUEMA+".PCD_MT_AJUSTE_EXCEPCION_DEPOSITO" +                           
			" where estado = 1 " ;   
			
			/*String strSql =
			" SELECT * FROM (select " +
			" to_char(fechacrea , 'YYYY-MM-DD') as FechaCrea," +                                                                     
			" (select login from ens.usuario where codreg = usercrea fetch first rows only) AS Usuario," +   
			" monto_Transaccion," +                                                                    
			" moneda," +                                                             
			" codcomp," +
			" coalesce((select trim(drdl01) from " + PropertiesSystem.ENS2 + ".vcompania where drky = CODCOMP), '') AS COMPANIA," +
			" idajusteexcepcion," +
			" cantidad_documentos," + 
			" estado " +
			" from "+PropertiesSystem.ESQUEMA+".PCD_MT_AJUSTE_EXCEPCION_DEPOSITO) AS RESULTADOS " +   
			strWhere  ;   */              

			result = (List<Object[]>) sesion.createSQLQuery( strSql )
					.setFirstResult( (pagina-1) * cantxpagina )
					.setMaxResults(cantxpagina)
					.list(); 
			
			totalRegs = (Integer)sesion.createSQLQuery(strSql).list().size()  ;
	
		}catch(Exception ex){    
			
			ex.printStackTrace();  
		}finally{
			
			if(newCn){
				try {  trans.commit(); } 
				catch (Exception e2) { }
				try {  HibernateUtilPruebaCn.closeSession(sesion); }
				catch (Exception e2) { }
			}
			sesion = null;
			trans = null;  
		}	
		return result;
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	public List<Object[]> cargarConfiguracionAjustes(String strFechaIni, String strFechaFin,int pagina, int cantxpagina,String strSortName, String strSortOrden,String strQtype,String strQuery,String strKind,String strRestriction){
		Session sesion = null;
		Transaction trans = null; 
		List<Object[]> result  = new ArrayList<Object[]>();  
		boolean newCn = false;
		
		try{
			
			sesion = HibernateUtilPruebaCn.currentSession() ;
			trans = (newCn = !(sesion.getTransaction().isActive())) ? sesion.beginTransaction() : sesion.getTransaction(); 
			
			
				String strWhere = 
					"WHERE "+strRestriction+" ESTADO = " + Integer.parseInt(strKind) + (strFechaIni.compareToIgnoreCase("")!=0? " AND  FECHA BETWEEN '"+strFechaIni+"'  AND '"+(strFechaFin.compareToIgnoreCase("")!=0?strFechaFin:strFechaIni)+"'":"")  + 
				  (strQuery.trim().compareToIgnoreCase("")!=0?" AND LOWER("+strQtype+") LIKE LOWER('%"+strQuery+"%')" :"") ;
				
				String strCampos = 
					" CUENTA, REFERENCIA, MONTO, CAJA, ORIGEN, " +
					" IFNULL((SELECT BANCO FROM "+PropertiesSystem.ESQUEMA+".F55CA022 WHERE CAST(CODB AS VARCHAR(20) CCSID 37)  = c.banco ),'') BANCO, " +
					" MONEDA, FECHA, ORIGEN AJUSTE, ESTADO, ID, ID_ARCHIVO_FUENTE," +
					" IFNULL( (SELECT (case when lower(origen) = 'banco' then cuenta_ajuste_origen else cuenta_ajuste_destino end )  FROM "+PropertiesSystem.ESQUEMA+".AJUSTES_PRECONCILIACION WHERE ID=C.ID),'')  CUENTA_G, " +
					" IFNULL( (SELECT OBSERVACION FROM "+PropertiesSystem.ESQUEMA+".DETALLE_AJUSTES_PRECONCILIACION WHERE ID_AJUSTE = C.ID FETCH FIRST ROWS ONLY),'') COMENTARIO," +
					" USUARIO," +
					" IFNULL( (SELECT (case when lower(origen) = 'banco' then cuenta_ajuste_destino else cuenta_ajuste_origen end )  FROM "+PropertiesSystem.ESQUEMA+".AJUSTES_PRECONCILIACION WHERE ID=C.ID),'')  CUENTA_O   ";	  
			    
				String strSqlQuery = "SELECT @camposC FROM(SELECT @camposT FROM "+PropertiesSystem.ESQUEMA+".AJUSTES_PRECONCILIACION C ) AS RESULTADOS @where  @order" ;
				
				String strQueryExec = strSqlQuery
					.replace("@camposC", "*")
					.replace("@camposT", strCampos)
					.replace("@order", "ORDER BY "+(strSortName.compareToIgnoreCase("")!=0? strSortName : "fecha") +" "+ strSortOrden)
					.replace("@where", strWhere) ;	
					
				result = (List<Object[]>) sesion.createSQLQuery( strQueryExec )
							.setFirstResult( (pagina-1) * cantxpagina )
							.setMaxResults(cantxpagina)
							.list(); 

			
				
				totalRegs = (Integer)(sesion.createSQLQuery(strSqlQuery.replace("@camposC", " count(*) ").replace("@camposT", strCampos).replace("@order", "").replace("@where",strWhere)).list()).get(0);  
		
		}catch(Exception ex){    
			
			ex.printStackTrace();  
		}finally{
			
			if(newCn){
				try {  trans.commit(); } 
				catch (Exception e2) { }
				try {  HibernateUtilPruebaCn.closeSession(sesion); }
				catch (Exception e2) { }
			}
			sesion = null;
			trans = null;  
		}	
		return result;
	}
	
	public List<Object[]> cargarAjustesAprobados(String strFechaIni, String strFechaFin,int pagina, int cantxpagina,String strSortName, String strSortOrden,String strQtype,String strQuery,String strKind,String strRestriction){
		Session sesion = null;
		Transaction trans = null; 
		List<Object[]> result  = new ArrayList<Object[]>();  
		boolean newCn = false;
		
		try{
			sesion = HibernateUtilPruebaCn.currentSession() ;
			trans = (newCn = !(sesion.getTransaction().isActive())) ? sesion
					.beginTransaction() : sesion.getTransaction();        
					
			String strWhere = "WHERE "+strRestriction+" ESTADO= "+Integer.parseInt(strKind)+(strFechaIni.compareToIgnoreCase("")!=0?" AND  FECHA BETWEEN '"+strFechaIni+"'  AND '"+(strFechaFin.compareToIgnoreCase("")!=0?strFechaFin:strFechaIni)+"'":"");
			strWhere+= (strQuery.trim().compareToIgnoreCase("")!=0?" AND LOWER("+strQtype+") LIKE LOWER('%"+strQuery+"%')" :"") ;
			String strCampos = " CUENTA,REFERENCIA,MONTO,CAJA,ORIGEN,IFNULL((SELECT BANCO FROM "+PropertiesSystem.ESQUEMA+".F55CA022 WHERE CAST(CODB AS VARCHAR(20) CCSID 37)  = '100001'),'')  BANCO,MONEDA, FECHA,  ORIGEN AJUSTE,ESTADO,ID,ID_ARCHIVO_FUENTE," +
					"  IFNULL((SELECT CUENTA_AJUSTE_DESTINO FROM "+PropertiesSystem.ESQUEMA+".AJUSTES_PRECONCILIACION WHERE ID=C.ID),'')  CUENTA_G , IFNULL((SELECT OBSERVACION FROM "+PropertiesSystem.ESQUEMA+".DETALLE_AJUSTES_PRECONCILIACION WHERE ID_AJUSTE = C.ID FETCH FIRST ROWS ONLY),'') COMENTARIO,USUARIO ";	  
		    String strSqlQuery = "SELECT @camposC FROM(SELECT @camposT FROM "+PropertiesSystem.ESQUEMA+".AJUSTES_PRECONCILIACION C ) AS RESULTADOS @where  @order" ;

			result = (List<Object[]>) sesion.createSQLQuery(strSqlQuery.replace("@camposC", "*").replace("@camposT", strCampos).replace("@order","ORDER BY "+(strSortName.compareToIgnoreCase("")!=0?strSortName:"fecha")+" "+strSortOrden).replace("@where",strWhere))						 
					.setFirstResult((pagina-1)*cantxpagina).setMaxResults(cantxpagina).list(); 

			totalRegs = (Integer)(sesion.createSQLQuery(strSqlQuery.replace("@camposC", " count(*) ").replace("@camposT", strCampos).replace("@order", "").replace("@where",strWhere)).list()).get(0);
			
		}catch(Exception ex){    
			ex.printStackTrace();  
		}finally{
			
			if(newCn){
				try {  trans.commit(); } 
				catch (Exception e2) { }
				try {  HibernateUtilPruebaCn.closeSession(sesion); }
				catch (Exception e2) { }
			}
			sesion = null;
			trans = null;  
		}	
		return result;
	}
	
	public int getTotalRegs() {
		return totalRegs;
	}

	public void setTotalRegs(int totalRegs) {
		this.totalRegs = totalRegs;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Object[]> filtrarCuentasF0901WC( List<String[]> ctaxconciliador ){
		List<Object[]> lstCuentas = null;
		Session sesion = HibernateUtilPruebaCn.currentSession();
		try {
			
			String query = "select trim(unineg), trim(cuentaobj), trim(cuentasub), trim(descripcion) " +
					" from " + PropertiesSystem.ESQUEMA+".cuentas_preconciliacion" +
					" where estado = 1 and tipocuenta = 1 " ;
			
			List<String[]>dta = new ArrayList<String[]>();
			dta.add(new String[]{"codigobanco","0"});
			dta.add(new String[]{"codcomp","2"});
			dta.add(new String[]{"moneda","3"});
			
			String sqlOrCtaConc = ConfirmaDepositosCtrl.constructSqlOrCtaxCon(dta, ctaxconciliador);
			if( sqlOrCtaConc != null && !sqlOrCtaConc.isEmpty() ){
				query += "and " + sqlOrCtaConc ;
			}
			
			return lstCuentas =sesion.createSQLQuery(query).list();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return lstCuentas ;
	}
	
	
	public List filtrarCuentasF0901WC1(String sUN,String sCobj,String sCsub){
		List lstCuentas = new ArrayList();
		Session sesion = HibernateUtilPruebaCn.currentSession();
		
		String sql = "";
		
		try {
			
			
			
			sql = "SELECT TRIM(GMMCU) ,TRIM(GMOBJ)  , TRIM(GMSUB) ,TRIM(GMDL01) FROM "+PropertiesSystem.ESQUEMA+".Vf0901 WHERE CASE WHEN TRIM(GMSUB)=''   THEN  TRIM(GMMCU)||'.'||TRIM(GMOBJ)  ELSE  TRIM(GMMCU)||'.'||TRIM(GMOBJ)||'.'||TRIM(GMSUB) END " +
					"  IN (SELECT NOCUENTA FROM "+PropertiesSystem.ESQUEMA+".CUENTAS_PRECONCILIACION)";//and trim(v.id.gmcrcd)<>''

			
			lstCuentas = sesion.createSQLQuery(sql).list();//  
			
			
		} catch (Exception error) {
			error.printStackTrace();
		} 
		return lstCuentas;		
	}
}

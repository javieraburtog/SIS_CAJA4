package com.businessobjects.samples;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

 

public class ActualizacionE1 {
	
	static Connection cn;
	static ResultSet rs;
	
	static PrintWriter writer ;
	static int rows ;
	static int caja_actual;
	static String fechaini ;
	static String fechafin ;
	static String codcomp = "'E01'";
	static String[] companias =  { "'E01'", "'E02'", "'E03'", "'E08'", "E10"   };
	static String strLogFileName = "LogActualiza.txt";
	static List<Integer> codigosCajaConDatos = new ArrayList<Integer>();
	static boolean systemoutprint; 
	
	static String BD_DESTINO = "E1GCPMCAJA";
	static String BD_ORIGEN = "GCPMCAJA";
	
	public static boolean runUpdate() {
		boolean done = true;

		try {

//			done = configuracionesCaja();
//			if(!done)
//				return done;
			

//			done = moduloPreconciliacion();
//			if(!done)
//			return done;

			recibosCaja();
			if(!done)
			return done;
			

		} catch (Exception e) {
			e.printStackTrace();
			done = false;
		} finally {
		}
		return done;
	}
	
	public static boolean facturas(){
		boolean done = true;
		try {
			
			List<String> queries = new ArrayList<String>();
			String queryDelete = "";
			String queryInsert = "";
			
			queryDelete = "delete from @BDE1CAJA.a03factco where fecha between @FECHAINI and @FECHAFIN ";
			queries.add(queryDelete);
			
			queryDelete = "delete from @BDE1CAJA.a02factco where fecha between @FECHAINI and @FECHAFIN ";
			queries.add(queryDelete);
			
			queryInsert = "insert into @BDE1CAJA.a02factco select * from @BDCAJAORIGEN.a02factco where fecha between @FECHAINI and @FECHAFIN ";
			queries.add(queryInsert);
			
			queryInsert = "insert into @BDE1CAJA.a03factco select * from @BDCAJAORIGEN.a03factco where fecha between @FECHAINI and @FECHAFIN ";
			queries.add(queryInsert);
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			done = false;
		}
		return done;
	}
	
	
	
	public static boolean recibosCaja() {
		boolean done = true;

		try {
			
			/*
			 * tablas:
			 * Recibo
			 * Recibodet
			 * Cambiodet
			 * Recibofac
			 * Recibojde
			 */
			
			List<String> queries = new ArrayList<String>();
			String queryDelete = "";
			String queryInsert = "";
			
//			queryDelete = 
//					 " merge into @BDE1CAJA.recibodet rd "
//					+" using ("
//					+" select  * "
//					+" from @BDE1CAJA.recibo where fecha between @FECHAINI and @FECHAFIN " 
//					+" ) r "
//					+" on r.caid = rd.caid and r.codcomp = rd.codcomp and r.tiporec = rd.tiporec and r.numrec = rd.numrec"
//					+" WHEN MATCHED then delete " ; 
//			queries.add(queryDelete);
//			
//			queryDelete = 
//					 " merge into @BDE1CAJA.cambiodet rd "
//					+" using ("
//					+" select  * "
//					+" from @BDE1CAJA.recibo where fecha between @FECHAINI and @FECHAFIN " 
//					+" ) r "
//					+" on r.caid = rd.caid and r.codcomp = rd.codcomp and r.tiporec = rd.tiporec and r.numrec = rd.numrec"
//					+" WHEN MATCHED then delete " ; 
//			queries.add(queryDelete);
//			
//			queryDelete = 
//					 " merge into @BDE1CAJA.recibofac rd "
//					+" using ("
//					+" select  * "
//					+" from @BDE1CAJA.recibo where fecha between @FECHAINI and @FECHAFIN " 
//					+" ) r "
//					+" on r.caid = rd.caid and r.codcomp = rd.codcomp and r.tiporec = rd.tiporec and r.numrec = rd.numrec"
//					+" WHEN MATCHED then delete " ; 
//			queries.add(queryDelete);
//			
//			queryDelete = 
//					 " merge into @BDE1CAJA.recibojde rd "
//					+" using ("
//					+" select  * "
//					+" from @BDE1CAJA.recibo where fecha between @FECHAINI and @FECHAFIN " 
//					+" ) r "
//					+" on r.caid = rd.caid and r.codcomp = rd.codcomp and r.tiporec = rd.tiporec and r.numrec = rd.numrec and rd.tipodoc <> 'D'"
//					+" WHEN MATCHED then delete " ; 
//			queries.add(queryDelete);
//			
//			queryDelete = "delete from @BDE1CAJA.recibo where fecha between @FECHAINI and @FECHAFIN ";
//			
//			queryInsert = "insert into @BDE1CAJA.recibo select * from @BDCAJAORIGEN.recibo where fecha between @FECHAINI and @FECHAFIN " ;
//			queries.add(queryInsert);
			/*
			queryInsert =
			 " insert into @BDE1CAJA.recibodet "
			+" select  rd.* "
			+" from @BDCAJAORIGEN.recibo r inner join @BDCAJAORIGEN.recibodet rd on " 
			+" r.caid = rd.caid and r.codcomp = rd.codcomp and r.numrec = rd.numrec and r.tiporec = rd.tiporec "
			+" where r.fecha between @FECHAINI and @FECHAFIN " ;  
			queries.add(queryInsert);
			
			queryInsert =
			 " insert into @BDE1CAJA.cambiodet "
			+" select  rd.* "
			+" from @BDCAJAORIGEN.recibo r inner join @BDCAJAORIGEN.cambiodet rd on " 
			+" r.caid = rd.caid and r.codcomp = rd.codcomp and r.numrec = rd.numrec and r.tiporec = rd.tiporec "
			+" where r.fecha between @FECHAINI and @FECHAFIN " ;  
			queries.add(queryInsert);
			*/
			
			/*
			queryInsert =
			 " insert into @BDE1CAJA.recibojde "
			+" select  rd.* "
			+" from @BDCAJAORIGEN.recibo r inner join @BDCAJAORIGEN.recibojde rd on " 
			+" r.caid = rd.caid and r.codcomp = rd.codcomp and r.numrec = rd.numrec and r.tiporec = rd.tiporec "
			+" where r.fecha between @FECHAINI and @FECHAFIN  and rd.tipodoc <> 'D'" ;  
			queries.add(queryInsert);
			*/
			
			
					
			queryInsert =
			 " insert into @BDE1CAJA.recibofac "
			+" select  rd.* "
			+" from @BDCAJAORIGEN.recibo r inner join @BDCAJAORIGEN.recibofac rd on " 
			+" r.caid = rd.caid and r.codcomp = rd.codcomp and r.numrec = rd.numrec and r.tiporec = rd.tiporec "
			+" where r.fecha between @FECHAINI and @FECHAFIN " ;  
			queries.add(queryInsert);
			
			
			
			
			int i = 0;
			for (String queryToExecute : queries) {
				
				String strSqlCurrentQuery = queryToExecute
						.replace("@BDE1CAJA", BD_DESTINO )
						.replace("@BDCAJAORIGEN", BD_ORIGEN )
						.replace("@FECHAINI", fechaini )
						.replace("@FECHAFIN", fechafin );
				
				rows = cn.createStatement().executeUpdate(strSqlCurrentQuery);
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			done = false;
		}
		return done;
	}
	
	
	
	public static boolean moduloPreconciliacion(){
		boolean done = true;
		
		try {
			
			/*
			 * tablas:
			 * deposito, ctaxdeposito, recibojde 
			 * archivo, depbcodet, 
			 * conciliacion, conciliadet
			 * ajustes_preconciliacion
			 * pcd_consolidado_depositos_banco
			 * pcd_comparaciones_deposito
			 * pcd_consolidado_depositos_confirmados
			 * pcd_mt_ajuste_excepcion_deposito
			 * pcd_dt_ajuste_excepcion_deposito
			 * pcd_notificacion_excepciones
			 * 
			 */
			
			String tablesIdentity[] = {
					"conciliacion@idconciliacion",
					"conciliadet@idconciliadet",
					"deposito@consecutivo",
					"ctaxdeposito@idctaxdep",
					"archivo@idarchivo",
					"depbcodet@iddepbcodet",
					"pcd_consolidado_depositos_banco@idresumenbanco",
					"ajustes_preconciliacion@id",
					"pcd_mt_ajuste_excepcion_deposito@idajusteexcepcion",
					"pcd_dt_ajuste_excepcion_deposito@iddtaed"
				};
			
			List<String> queries = new ArrayList<String>();
			String queryDelete = "";
			String queryInsert = "";
			
			
			// =============================================== conciliacion (borrado)
			queryDelete = "delete from @BDE1CAJA.conciliadet where idconciliacion in ( select idconciliacion from @BDE1CAJA.conciliacion where date(fechacrea) between @FECHAINI and @FECHAFIN ) ";
			queries.add(queryDelete);
			
			queryDelete = "delete from @BDE1CAJA.conciliacion where date(fechacrea) between @FECHAINI and @FECHAFIN ";
			queries.add(queryDelete);
			
			
			// =============================================== depositos de caja 
			
			queryDelete =
			  " merge into @BDE1CAJA.recibojde A "
			+ " using("
			+ " select rj.* "
			+ " from @BDCAJAORIGEN.deposito d inner join @BDCAJAORIGEN.recibojde rj on d.caid = rj.caid and d.codcomp = rj.codcomp and d.nodeposito = rj.numrec and rj.tiporec = d.mpagodep" 
			+ " where  date(d.createdaterow) between @FECHAINI and @FECHAFIN and rj.tipodoc = 'D'  and d.mpagodep = rj.tiporec" 
			+ " group by    rj.numrec , rj.codcomp,  rj.recjde,  rj.nobatch,  rj.caid,  rj.codsuc,  rj.tipodoc,  rj.tiporec"
			+ " HAVING COUNT(*) = 1" 
			+ " )  B "
			+ " on a.numrec = b.numrec  and a.codcomp = b.codcomp and a.recjde = b.recjde and a.nobatch = b.nobatch and a.caid = b.caid and a.tipodoc = b.tipodoc and a.tiporec = b.tiporec" 
			+ "  WHEN  MATCHED then  delete " ;
			queries.add(queryDelete);
			/*
			queryDelete =  "delete from @BDE1CAJA.recibojde where nobatch in ("  
					+" select rj.nobatch " 
					+" from @BDE1CAJA.deposito d inner join @BDE1CAJA.recibojde rj" 
					+" on rj.numrec = d.nodeposito and d.caid = rj.caid and d.codcomp = rj.codcomp   " 
					+" where rj.tipodoc = 'D' "
					+" and  date(createdaterow) between @FECHAINI and @FECHAFIN " +
					" ) " ;  
			queries.add(queryDelete);
			*/
			
			queryDelete = "delete from @BDE1CAJA.ctaxdeposito where iddeposito in (" +
							" select consecutivo from @BDE1CAJA.deposito where date(createdaterow) between  @FECHAINI and @FECHAFIN " +
							")" ;
			queries.add(queryDelete);
			
			queryDelete = "delete from @BDE1CAJA.deposito where date(createdaterow) between @FECHAINI and @FECHAFIN " ;
			queries.add(queryDelete);
			
			      
			queryInsert = "insert into @BDE1CAJA.deposito overriding SYSTEM value " +
					" select  * from  @BDCAJAORIGEN.deposito where date(createdaterow) between  @FECHAINI and @FECHAFIN  ";
			queries.add(queryInsert);
			
			queryInsert = " insert into @BDE1CAJA.ctaxdeposito overriding SYSTEM value "  +
					"select  * from  @BDCAJAORIGEN.ctaxdeposito where iddeposito in( select consecutivo from  @BDCAJAORIGEN.deposito where date(createdaterow) between  @FECHAINI and @FECHAFIN   )";
			queries.add(queryInsert);
			
			/*
			queryInsert = "insert into @BDE1CAJA.recibojde  "
					+" select  rj.* "
					+" from @BDCAJAORIGEN.deposito d inner join @BDCAJAORIGEN.recibojde rj " 
					+" 	on rj.numrec = d.nodeposito and d.caid = rj.caid and d.codcomp = rj.codcomp "
					+" where rj.tipodoc = 'D' "  
					+" and  date(createdaterow) between @FECHAINI and @FECHAFIN " ;
			queries.add(queryInsert);
			*/
			queryInsert =
				  " merge into @BDE1CAJA.recibojde A "
				+ " using("
				+ " select rj.* "
				+ " from @BDCAJAORIGEN.deposito d inner join @BDCAJAORIGEN.recibojde rj on d.caid = rj.caid and d.codcomp = rj.codcomp and d.nodeposito = rj.numrec and rj.tiporec = d.mpagodep" 
				+ " where  date(d.createdaterow) between @FECHAINI and @FECHAFIN and rj.tipodoc = 'D'  and d.mpagodep = rj.tiporec" 
				+ " group by    rj.numrec , rj.codcomp,  rj.recjde,  rj.nobatch,  rj.caid,  rj.codsuc,  rj.tipodoc,  rj.tiporec"
				+ " HAVING COUNT(*) = 1" 
				+ " )  B "
				+ " on a.numrec = b.numrec  and a.codcomp = b.codcomp and a.recjde = b.recjde and a.nobatch = b.nobatch and a.caid = b.caid and a.tipodoc = b.tipodoc and a.tiporec = b.tiporec" 
				+ " WHEN not MATCHED then" 
				+ " insert values ( b.numrec , b.codcomp, b.recjde, b.nobatch, b.caid, b.codsuc, b.tipodoc, b.tiporec )" ;
			queries.add(queryInsert);
			
			// ===============================================  archivos de estados de cuenta
			queryDelete = "delete from @BDE1CAJA.depbcodet where idarchivo in ( select idarchivo from @BDE1CAJA.archivo where date(fechacrea)between @FECHAINI and @FECHAFIN )" ;
			queries.add(queryDelete);
			
			queryDelete = "delete from @BDE1CAJA.archivo where date(fechacrea) between @FECHAINI and @FECHAFIN " ;
			queries.add(queryDelete);
			
			queryInsert =  "insert into @BDE1CAJA.archivo overriding SYSTEM value" +
					" select * from @BDCAJAORIGEN.archivo where date(fechacrea) between @FECHAINI and @FECHAFIN ";
			queries.add(queryInsert);
			
			queryInsert =  "insert into @BDE1CAJA.depbcodet overriding SYSTEM value" +
					" select * from @BDCAJAORIGEN.depbcodet where idarchivo in ( select idarchivo from @BDCAJAORIGEN.archivo where date(fechacrea) between @FECHAINI and @FECHAFIN ) ";
			queries.add(queryInsert);
			
			// =============================================== tablas de preconciliacion
			queryDelete = "delete from @BDE1CAJA.pcd_consolidado_depositos_banco where date(fechagrabaconsolida) between @FECHAINI and @FECHAFIN";
			queries.add(queryDelete);
			
			queryInsert =  "insert into @BDE1CAJA.pcd_consolidado_depositos_banco overriding SYSTEM value" +
					" select * from @BDCAJAORIGEN.pcd_consolidado_depositos_banco where date(fechagrabaconsolida) between @FECHAINI and @FECHAFIN";
			queries.add(queryInsert);
			
			
			// =============================================== conciliacion (insercion)
		
			queryInsert = "insert into @BDE1CAJA.conciliacion overriding SYSTEM value" +
					" select * from @BDCAJAORIGEN.conciliacion where date(fechacrea) between @FECHAINI and @FECHAFIN ";
			queries.add(queryInsert);
			
			queryInsert = "insert into @BDE1CAJA.conciliadet overriding SYSTEM value" +
					" select * from @BDCAJAORIGEN.conciliadet where idconciliacion in ( select idconciliacion from @BDCAJAORIGEN.conciliacion where date(fechacrea) between @FECHAINI and @FECHAFIN ) ";
			queries.add(queryInsert);
			
			
			// =============================================== ajustes ()
			queryDelete = "delete from @BDE1CAJA.ajustes_preconciliacion where date(fecha_creacion) between @FECHAINI and @FECHAFIN";
			queries.add(queryDelete);
			
			queryInsert = "insert into @BDE1CAJA.ajustes_preconciliacion overriding SYSTEM value" +
					" select * from @BDCAJAORIGEN.ajustes_preconciliacion where date(fecha_creacion) between @FECHAINI and @FECHAFIN ";
			queries.add(queryInsert);
			
			queryDelete = "delete from @BDE1CAJA.pcd_mt_ajuste_excepcion_deposito where date(date_insert_row) between @FECHAINI and @FECHAFIN";
			queries.add(queryDelete);
			
			queryInsert = "insert into @BDE1CAJA.pcd_mt_ajuste_excepcion_deposito overriding SYSTEM value" +
					" select * from @BDCAJAORIGEN.pcd_mt_ajuste_excepcion_deposito where date(date_insert_row) between @FECHAINI and @FECHAFIN ";
			queries.add(queryInsert);
			
			queryDelete = "delete from @BDE1CAJA.pcd_dt_ajuste_excepcion_deposito where id_mt_ajuste_excepcion in " +
					"( select idajusteexcepcion from @BDE1CAJA.pcd_mt_ajuste_excepcion_deposito where date(date_insert_row) between @FECHAINI and @FECHAFIN ) " ;
			queries.add(queryDelete);
			
			queryInsert = "insert into @BDE1CAJA.pcd_dt_ajuste_excepcion_deposito overriding SYSTEM value " +
					" select * from @BDCAJAORIGEN.pcd_dt_ajuste_excepcion_deposito where id_mt_ajuste_excepcion in  " +
					"( select idajusteexcepcion from @BDCAJAORIGEN.pcd_mt_ajuste_excepcion_deposito where date(date_insert_row) between @FECHAINI and @FECHAFIN )  ";
			queries.add(queryInsert);
			
			
			// =============================================== arqueos de caja 
			queryDelete = 
			 " merge into @BDE1CAJA.arqueorec ar "
			+" using ("
			+" select  * "
			+" from @BDE1CAJA.arqueo where fecha between @FECHAINI and @FECHAFIN " 
			+" ) a "
			+" on a.caid = ar.caid and a.codcomp = ar.codcomp and a.noarqueo = ar.noarqueo "
			+" WHEN MATCHED then delete " ; 
			queries.add(queryDelete);
			
			queryDelete = 
			 " merge into @BDE1CAJA.arqueofact ar "
			+" using ("
			+" select  * "
			+" from @BDE1CAJA.arqueo where fecha between @FECHAINI and @FECHAFIN " 
			+" ) a "
			+" on a.caid = ar.caid and a.codcomp = ar.codcomp and a.noarqueo = ar.noarqueo "
			+" WHEN MATCHED then delete " ; 
			queries.add(queryDelete);
			
			queryDelete = "delete from @BDE1CAJA.arqueo where fecha between @FECHAINI and @FECHAFIN ";
			queries.add(queryDelete);
			
			
			queryInsert = "insert into @BDE1CAJA.arqueo select * from @BDCAJAORIGEN.arqueo where fecha between @FECHAINI and @FECHAFIN ";
			queries.add(queryInsert);
			
			queryInsert =  
			 " INSERT INTO @BDE1CAJA.ARQUEOREC "
			+" select ar.* "
			+" from @BDCAJAORIGEN.arqueo a inner join @BDCAJAORIGEN.arqueorec ar  "
			+" 	on a.caid = ar.caid and a.codcomp = ar.codcomp and a.noarqueo = ar.noarqueo "
			+" where a.fecha between @FECHAINI and @FECHAFIN  " ;
			queries.add(queryInsert);
			
			queryInsert =  
			 " INSERT INTO @BDE1CAJA.ARQUEOFACT "
			+" select ar.* "
			+" from @BDCAJAORIGEN.arqueo a inner join @BDCAJAORIGEN.arqueofact ar  "
			+" 	on a.caid = ar.caid and a.codcomp = ar.codcomp and a.noarqueo = ar.noarqueo "
			+" where a.fecha between @FECHAINI and @FECHAFIN  " ;
			queries.add(queryInsert);
			
			
			// =============================================== actualizacion de identitys
			for (String tableName : tablesIdentity) {
				
				String nextNumberIdentity = updateNextNumberTable(tableName); 
				if(nextNumberIdentity.isEmpty())
					continue;
				
				queries.add( nextNumberIdentity ) ;
				
			}
			
			int i = 0;
			for (String queryToExecute : queries) {
				
				String strSqlCurrentQuery = queryToExecute
						.replace("@BDE1CAJA", BD_DESTINO )
						.replace("@BDCAJAORIGEN", BD_ORIGEN )
						.replace("@FECHAINI", fechaini )
						.replace("@FECHAFIN", fechafin );
				
				rows = cn.createStatement().executeUpdate(strSqlCurrentQuery);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			done = false;
		}
		return done;
	}
	
	
	
	public static boolean configuracionesCaja(){
		
		boolean done = true;
		String strSqlCurrentQuery = "";
		List<String> queriesToExec   = new ArrayList<String>();
	
		try {
			
			List<String> tablesNameConfg = new ArrayList<String>();
			
			strSqlCurrentQuery = " select table_name FROM QSYS2.SYSTSTAT wHERE TABLE_SCHEMA =  '" + BD_ORIGEN + "' AND LOWER(TABLE_NAME) LIKE  'f55%' ";
			rs = cn.createStatement().executeQuery(strSqlCurrentQuery);
				 
			while(rs.next()){
				tablesNameConfg.add( rs.getString(1) );
			}
			
			//&& =========== tablas que no tienen campos identity
			String tablesNames[] = { 
				"numcaja", 
				"tiporecibo", 
				"cajaparm", 
				"kcajaparm", 
				"recibosec", 
				"ruc" 
			} ;
			tablesNameConfg.addAll(Arrays.asList(tablesNames)) ;
			
			String queryDelete     = "delete from " + BD_DESTINO + ".@TableName  " ; 
			String queryInsertInto = "insert into " + BD_DESTINO + ".@TableName select * from " + BD_ORIGEN + ".@TableName " ; 
			
			for (String tableName : tablesNameConfg) {
				queriesToExec.add( queryDelete.replace("@TableName", tableName) );
				queriesToExec.add(queryInsertInto.replace("@TableName", tableName) );
			}	
			
			//&& =========== tablas que  si tienen campos identity
			String tablesIdentity[] = {
				"cierrecajahora@IDROW", 
				"cajaxconciliador@IDCAJAXCONCILIADOR", 
				"cuentas_preconciliacion@ID", 
				"cuentaxconciliador@IDCTAXCONCILIADOR", 
				"dnc_beneficiario@IDBENEFICIARIO", 
				"dnc_beneficiario_cuentas@IDCUENTABENEFICIARIO", 
				"equivtipodocs@IDEQUIV", 
				"ftpconciliacion@IDFTPURL", 
				"mindpxcaja@IDCFGXMDP", 
				"minutadp@IDMINUTA", 
				"monedaxlinea@IDMONEDAXLINEA", 
				"pcd_cajas_preconciliar@IDCAJAPRECONCIL", 
				"pcd_notificacion_excepciones@ID_EMP_CONFIG_NOT", 
				"tasacambio_cliente@IDTASACLIENTE", 
				"term_afl@ROWNUMBER" 
			};
			
			String nextNumberIdentity = ""  ;
			queryInsertInto = "insert into " + BD_DESTINO + ".@TableName overriding SYSTEM value select * from " + BD_ORIGEN + ".@TableName " ; 
			
			for (String tableName : tablesIdentity) {
			
				queriesToExec.add( queryDelete.replace("@TableName", tableName.split("@")[0]) );
				queriesToExec.add( queryInsertInto.replace("@TableName", tableName.split("@")[0]) );
				
				nextNumberIdentity = updateNextNumberTable(tableName); 
				if(nextNumberIdentity.isEmpty())
					continue;
				
				queriesToExec.add( nextNumberIdentity ) ;
				
			}
			
			String [] codigosFormasPago = {
					".@5", "?@H", "!@Q", "T@8", "N@N"
			} ;
			String queryUpdateMpago = "update " + BD_DESTINO + ".f55ca011 set c1ryin = '@CODIGO_ACTUAL' where c1ryin = '@CODIGO_NUEVO' " ;
			String queryUpdateCtaMpago = "update " + BD_DESTINO + ".f55ca012 set c2ryin = '@CODIGO_ACTUAL' where c2ryin = '@CODIGO_NUEVO' " ;
			
			for (String codigoFormaPago : codigosFormasPago) {
				queriesToExec.add(queryUpdateMpago.replace( "@CODIGO_ACTUAL", codigoFormaPago.split("@")[0]).replace( "@CODIGO_NUEVO", codigoFormaPago.split("@")[1]) );
				queriesToExec.add(queryUpdateCtaMpago.replace( "@CODIGO_ACTUAL", codigoFormaPago.split("@")[0]).replace( "@CODIGO_NUEVO", codigoFormaPago.split("@")[1]) );
			}	
			
			int i = 0;
			for (String queryToExecute : queriesToExec) {
				
				strSqlCurrentQuery = queryToExecute;
				
				rows = cn.createStatement().executeUpdate(strSqlCurrentQuery);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			done = false;
		}
		
		return done;
	}
	
	
	
	public static String updateNextNumberTable( String table ){
		String statement = "" ; 
		try {
			
			String strQueryMaxValue = "select max(@FielName) from " + BD_DESTINO + ".@TableName " ;
			String queryUpdateIdentityField = "alter table " + BD_DESTINO + ".@TableName alter column @FielName restart with @RestarValue ";
			
			ResultSet rs = null;
			long maxValue = 0 ;
			
			String tablename = table.split("@")[0]; 
			String fieldname = table.split("@")[1] ;
			
			String strSqlCurrentQuery = strQueryMaxValue.replace("@TableName",tablename ).replace( "@FielName", fieldname )  ;
				 
			 rs =  cn.createStatement().executeQuery(strSqlCurrentQuery) ;
			
			 if(!rs.next())
				return statement = "" ;
				 
			 maxValue = rs.getLong(1) + 1 ;
				 
			 return  queryUpdateIdentityField
						.replace("@TableName", tablename)
						.replace("@FielName",  fieldname)
						.replace("@RestarValue", Long.toString(maxValue)) ;
			
		} catch (Exception e) {
			e.printStackTrace();
			statement = "" ;
		}
		return statement;
		
	}
	
	public static int dateToJulian (Date date) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR) - 1900;
		int month = calendar.get(Calendar.DAY_OF_YEAR);
		return Integer.parseInt(String.format("%03d%03d", year, month));

	}
	
	public ActualizacionE1(PrintWriter writer, String fechaini,
			String fechafin, String codcomp, String[] companias,
			boolean systemoutprint, String bdDestino, String bdOrigen,
			Connection cn) {
		try {

			ActualizacionE1.writer = writer;
			ActualizacionE1.fechaini = fechaini;
			ActualizacionE1.fechafin = fechafin;
			ActualizacionE1.codcomp = codcomp;
			ActualizacionE1.companias = companias;
			ActualizacionE1.systemoutprint = systemoutprint;
			ActualizacionE1.BD_DESTINO = bdDestino;
			ActualizacionE1.BD_ORIGEN = bdOrigen;
			ActualizacionE1.cn = cn;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 
	
}

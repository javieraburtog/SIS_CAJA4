package com.casapellas.conciliacion.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.casapellas.controles.ConfirmaDepositosCtrl;
import com.casapellas.controles.ConsolidadoDepositosBcoCtrl;
import com.casapellas.controles.RevisionArqueoCtrl;
import com.casapellas.controles.tmp.ReciboCtrl;
import com.casapellas.controles.tmp.TasaCambioCtrl;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.jde.creditos.CodigosJDE1;
import com.casapellas.reportes.Rptmcaja012Pdf;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.Divisas;
import com.casapellas.util.DocumuentosTransaccionales;
import com.casapellas.util.PropertiesSystem;
import com.casapellas.util.jdbcTransaction;
import com.casapellas.entidades.ens.Vautoriz;

/**
 * Servlet implementation class svltProcesarAjuste
 */
public class svltProcesarAjuste extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	List<Object[]> lstBathProcesados ;
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain;charset=UTF-8");
		 PrintWriter out = response.getWriter();		
	     HttpSession shttp = (HttpSession)request.getSession();
	     String strRows = request.getParameter("chks");
	     String strMode = request.getParameter("mode");
	     String strRowid = request.getParameter("rowid");
	     String strComents = request.getParameter("idObject");
	     jdbcTransaction trans = new jdbcTransaction();
	     lstBathProcesados = new ArrayList<Object[]>();
		try {
			
			
			if(shttp.getAttribute("sevAut")!=null){
			    
				Vautoriz vaut =   ((Vautoriz[]) shttp.getAttribute("sevAut"))[0];
				String strUser = vaut.getId().getLogin();
				
			 String Rows[]  = strRows.split(",");
			 int regProcesados  = 0;
			 int state  = 1;
			 String strMessage = "";
			 if(strMode.compareToIgnoreCase("1")==0){  // INSERCION DE AJUSTES
			 trans.openConnection();
			
			 for (int i = 0; i < Rows.length; i++) {  
				try {
					
					 String Row[]  = Rows[i].split("->")[0].split("@");
					 String parameters[][] = new String[Row.length+3][2];
					 parameters[0][0] =  Row[0]; parameters[0][1] =  "0";
					 parameters[1][0] =  Row[1]; parameters[1][1] =  "0";
					 parameters[2][0] =  Row[2]; parameters[2][1] =  "1";
					 parameters[3][0] =  Row[3]; parameters[3][1] =  "0";
					 parameters[4][0] =  Row[4]; parameters[4][1] =  "0";
					 parameters[5][0] =  Row[5]; parameters[5][1] =  "2";
					 parameters[6][0] =  Row[6]; parameters[6][1] =  "0";
					 parameters[7][0] =  Rows[i].split("->")[1]; parameters[7][1] =  "0"; 
					 parameters[8][0] =  strUser; parameters[8][1] =  "0";  
					 parameters[9][0] = Row[7];parameters[9][1] = "0";      
					 parameters[10][0] = Row[8];parameters[10][1] = "0";   
					 parameters[11][0] = Row[9];parameters[11][1] = "0";   
					 
					if( String.valueOf(Rows[i].split("->")[1]).compareToIgnoreCase("Banco") == 0  ){
						regProcesados += trans.EjecutarTransaccion("INSERT INTO "+PropertiesSystem.ESQUEMA+".AJUSTES_PRECONCILIACION (CUENTA,REFERENCIA,MONTO,BANCO,MONEDA,FECHA,CAJA,ORIGEN,USUARIO, ID_ARCHIVO_FUENTE, CUENTA_AJUSTE_DESTINO, CODCOMP) " +
								" VALUES(?,?,?,?,?,?,?,?,?,?,?,?)", parameters);
						
					}else{
						regProcesados += trans.EjecutarTransaccion("INSERT INTO "+PropertiesSystem.ESQUEMA+".AJUSTES_PRECONCILIACION (CUENTA,REFERENCIA,MONTO,BANCO,MONEDA,FECHA,CAJA,ORIGEN,USUARIO, ID_ARCHIVO_FUENTE, CUENTA_AJUSTE_ORIGEN, CODCOMP) " +
								" VALUES(?,?,?,?,?,?,?,?,?,?,?,?)", parameters);
					}
					
					
					parameters = new String[1][2];  
					parameters[0][0] = Row[7];
					parameters[0][1] = "1";  
					
					String strSqlQueryUpdate = "" ;
					String strSqlUpdateConsolidado = ""  ;
					
					if( String.valueOf(Rows[i].split("->")[1]).compareToIgnoreCase("Banco") == 0  ){
						strSqlQueryUpdate = "UPDATE "+PropertiesSystem.ESQUEMA+".DEPBCODET SET IDESTADOCNFR = 70 WHERE  IDDEPBCODET = ?" ;
						strSqlUpdateConsolidado = "UPDATE "+PropertiesSystem.ESQUEMA+".PCD_CONSOLIDADO_DEPOSITOS_BANCO SET estadoconfirmacion = 4 WHERE IDDEPBCODET = ?" ;
					}else{
						strSqlQueryUpdate = "UPDATE "+PropertiesSystem.ESQUEMA+".DEPOSITO SET ESTADOCNFR = 'AJS' WHERE  CONSECUTIVO = ?" ;
					}
					
					trans.EjecutarTransaccion( strSqlQueryUpdate, parameters);
					
					if(!strSqlUpdateConsolidado.isEmpty())
						trans.EjecutarTransaccion( strSqlUpdateConsolidado, parameters);
					
					trans.getCnx().commit();
			
				
				} catch (Exception e) {
					e.printStackTrace();
					try {
						trans.getCnx().rollback();
						regProcesados = 0;
						state = 0;
						strMessage = "Ha ocurrido un error durante el procesamiento de los registros";
					} catch (Exception e2) {  
						e2.printStackTrace();
					}
				} 		
				
			}  // FIN INSERCION DE AJUSTES 
			 trans.closeConnection();
			
			
		 }else if(strMode.compareToIgnoreCase("2")==0){  //Eliminar Ajuste
			 trans.openConnection();
			 try {
				 String Row[]  =  strRowid.split("@");
				 String parameters[][] = new String[1][2];
				 parameters[0][0] = Row[0];  parameters[0][1] = "1" ; 
				
				 regProcesados += trans.EjecutarTransaccion("DELETE FROM "+PropertiesSystem.ESQUEMA+".AJUSTES_PRECONCILIACION WHERE ID=?" , parameters);
				 
				 parameters[0][0] = Row[1];  parameters[0][1] = "1" ;
				 trans.EjecutarTransaccion((String.valueOf(Row[2]).compareToIgnoreCase("Banco")==0?"UPDATE "+PropertiesSystem.ESQUEMA+".DEPBCODET SET IDESTADOCNFR = 36 WHERE  IDDEPBCODET = ?":"UPDATE "+PropertiesSystem.ESQUEMA+".DEPOSITO SET ESTADOCNFR = 'SCR' WHERE  CONSECUTIVO = ?"), parameters);
				 trans.getCnx().commit();
			} catch (Exception e) {
				e.printStackTrace();
				try {
					trans.getCnx().rollback();
					regProcesados = 0;  
					state = 0;
					strMessage = "Ha ocurrido un error durante el procesamiento de los registros";
				} catch (Exception e2) {  
					e2.printStackTrace();
				}
			}
			
			 trans.closeConnection();
			//FIN Eliminar Ajuste	
		 }	else if(strMode.compareToIgnoreCase("3")==0){ //Inicio insercion de Ajuste Contable					 
			 
			 boolean bupdate = true;
			 
			 for (int i = 0; i < Rows.length; i++) {  
				 String Row[]  =  Rows[i].split("@");
				 if( Row.length!=5 || String.valueOf( Row[4]).trim().compareToIgnoreCase("") == 0 ){  
					 strMessage = "Debe Seleccionar la cuenta destino de los ajustes. El depósito con referencia: "+getReferenceNumber(shttp,Row[0])+" no tiene una cuenta destino seleccionada.";
					 state = 0;
					 bupdate = false;
					 break;
				 }
				 if( Row.length!=5 || String.valueOf( Row[3]).trim().compareToIgnoreCase("") == 0 ){  
					 strMessage = "Debe Agregar el concepto/Motivo para la excepción de referencia  "+getReferenceNumber(shttp,Row[0]) ;
					 state = 0;
					 bupdate = false;
					 break;
				 }
			}
			 
			trans.openConnection();
			 
			 try {
				 
				 if(bupdate){
					 
				 for (int i = 0; i < Rows.length; i++) {  
					
					 
					 String Row[]  =  Rows[i].split("@");					 
					 String parameters[][] = new String[2][2];
					 parameters[0][0] = Row[4];  parameters[0][1] = "0" ;
					 parameters[1][0] = Row[0];  parameters[1][1] = "1" ; 	
					 
					 
					String strSql = "select origen from " +PropertiesSystem.ESQUEMA+".AJUSTES_PRECONCILIACION where id = " + Row[0] ;
					@SuppressWarnings("unchecked")
					List<String> lstOrigenAjuste = (ArrayList<String>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSql, true, null);
					 
					 if(lstOrigenAjuste.get(0).trim().compareToIgnoreCase("banco") == 0 ){
						 regProcesados += trans.EjecutarTransaccion("UPDATE "+PropertiesSystem.ESQUEMA+".AJUSTES_PRECONCILIACION SET CUENTA_AJUSTE_ORIGEN = ?, ESTADO=2 WHERE ID = ?",parameters);
					 }else{
						 regProcesados += trans.EjecutarTransaccion("UPDATE "+PropertiesSystem.ESQUEMA+".AJUSTES_PRECONCILIACION SET CUENTA_AJUSTE_DESTINO = ?, ESTADO=2 WHERE ID = ?",parameters);
					 }
					 
					 parameters  = new String[4][2];
					 parameters[0][0] = Row[0];  parameters[0][1] = "1" ;
					 parameters[1][0] = Row[3];  parameters[1][1] = "0" ;   
					 parameters[2][0] = strUser;  parameters[2][1] = "0" ;     
					 parameters[3][0] = "2";  	 parameters[3][1] = "1" ;
					 
					 trans.EjecutarTransaccion("INSERT INTO "+PropertiesSystem.ESQUEMA+".DETALLE_AJUSTES_PRECONCILIACION  (ID_AJUSTE,OBSERVACION,USUARIO,ESTADO) VALUES (?,?,?,?)",parameters);
					 
					 parameters  = new String[1][2];
					 parameters[0][0] = Row[0];  parameters[0][1] = "1" ;
					//LEVANTAMOS LA INFORMACION DEL REGISTRO DEL AJUSTE
					 
					 /*
					 String  Results[] = trans.getData("SELECT MONTO,MONEDA,CUENTA_AJUSTE_ORIGEN,CUENTA_AJUSTE_DESTINO,REFERENCIA," +  
					 		"  BANCO,IFNULL((SELECT BANCO FROM "+PropertiesSystem.ESQUEMA+".F55CA022 WHERE CODB ='100001' FETCH FIRST ROWS ONLY ),'')NOMBRE_BANCO,CASE WHEN ORIGEN = 'Banco' THEN  IFNULL((SELECT  CXCRR FROM "+PropertiesSystem.ESQUEMA+".TCAMBIO WHERE CXEFT = (SELECT FECHAPROCESO FROM "+PropertiesSystem.ESQUEMA+".DEPBCODET WHERE IDDEPBCODET = ID_ARCHIVO_FUENTE  FETCH FIRST ROWS ONLY) AND CXCRDC = 'COR' FETCH FIRST ROWS ONLY) ,0.00) " +
					 		"  ELSE IFNULL((SELECT  CXCRR FROM "+PropertiesSystem.ESQUEMA+".TCAMBIO WHERE CXEFT = (SELECT FECHA FROM "+PropertiesSystem.ESQUEMA+".DEPOSITO  WHERE CONSECUTIVO=ID_ARCHIVO_FUENTE FETCH FIRST ROWS ONLY) AND CXCRDC = 'COR' FETCH FIRST ROWS ONLY) ,0.00) END TASA_DIA," +
					 		"  IFNULL((SELECT CAST(TRIM(GMDL01) AS VARCHAR(100) CCSID 37)  FROM "+PropertiesSystem.ESQUEMA+".Vf0901 WHERE CASE WHEN TRIM(GMSUB)=''   THEN  TRIM(GMMCU)||'.'||TRIM(GMOBJ)  ELSE  TRIM(GMMCU)||'.'||TRIM(GMOBJ)||'.'||TRIM(GMSUB) END  = CUENTA_AJUSTE_ORIGEN),'')NOMBRE_CUENTA_ORIGEN, " +
					 		"  IFNULL((SELECT CAST(TRIM(GMDL01) AS VARCHAR(100) CCSID 37)  FROM "+PropertiesSystem.ESQUEMA+".Vf0901 WHERE CASE WHEN TRIM(GMSUB)=''   THEN  TRIM(GMMCU)||'.'||TRIM(GMOBJ)  ELSE  TRIM(GMMCU)||'.'||TRIM(GMOBJ)||'.'||TRIM(GMSUB) END  = CUENTA_AJUSTE_DESTINO),'')NOMBRE_CUENTA_DESTINO," +
					 		"  IFNULL((SELECT OBSERVACION FROM "+PropertiesSystem.ESQUEMA+".DETALLE_AJUSTES_PRECONCILIACION WHERE  ID_AJUSTE = P.ID ORDER BY ID DESC FETCH FIRST ROWS ONLY) , '')COMENTARIO, "+
					 		"  IFNULL((SELECT CAST(TRIM(GMAID) AS VARCHAR(20) CCSID 37)  FROM "+PropertiesSystem.ESQUEMA+".Vf0901 WHERE TRIM(GMMCU)||'.'||TRIM(GMOBJ)||'.'||TRIM(GMSUB)  = CUENTA_AJUSTE_DESTINO),'')GMAID_CUENTA_DESTINO," +
					 		"  IFNULL((SELECT CAST(TRIM(GMAID) AS VARCHAR(20) CCSID 37)  FROM "+PropertiesSystem.ESQUEMA+".Vf0901 WHERE TRIM(GMMCU)||'.'||TRIM(GMOBJ)||'.'||TRIM(GMSUB)  = CUENTA_AJUSTE_ORIGEN),'')GMAID_CUENTA_ORIGEN, "+
					 		"  IFNULL((SELECT COUNT(*) FROM "+PropertiesSystem.ESQUEMA+".AJUSTES_PRECONCILIACION WHERE YEAR(FECHA_CREACION) = YEAR(P.FECHA_CREACION)  AND MONTH(FECHA_CREACION) =  MONTH(P.FECHA_CREACION) ),1) CANT_AJE, "  +
					 		"  CAJA,ORIGEN "+  
					 		"  FROM  "+PropertiesSystem.ESQUEMA+".AJUSTES_PRECONCILIACION P WHERE ID = ?" , parameters) ; 
					 
					
					 new com.casapellas.util.GeneratePdf(Results[2], Results[3], Results[8], Results[9],String.valueOf(Results[6]).trim() +" Código Contable: "+Results[2] , Results[0], Results[1], Results[10],shttp,Results[13]);
					 */
					 } 	
				 	trans.getCnx().commit();  
				 
				 
				 }  
			} catch (Exception e) {
				e.printStackTrace();
				try {
					trans.getCnx().rollback();
					regProcesados = 0;
					state = 0;
					strMessage = "Ha ocurrido un error durante el procesamiento de los registros";
				} catch (Exception e2) {  
					e2.printStackTrace();
				}
			}
			 trans.closeConnection();			 
		 }	else if(strMode.compareToIgnoreCase("4")==0){ // aca es cuando el supervisor conciliador aprueba la propuesta de ajuste por preconciliadores			 
			
			 
			 List<String> idsAjustes = new ArrayList<String>();
			 
			 boolean bupdate = true;
			 
			 for (int i = 0; i < Rows.length; i++) {  
				 String Row[]  =  Rows[i].split("@");
				 if(Row.length!=5 || String.valueOf(Row[4]).trim().compareToIgnoreCase("")==0   ){  
					 strMessage = " Debe Seleccionar la cuenta destino de los ajustes. El depósito con referencia: "+getReferenceNumber(shttp,Row[0])+" no tiene una cuenta destino seleccionada.";
					 state = 0;
					 bupdate = false;
					 break;
				 }
			 }
			 trans.openConnection();
			 
			 try {
				 
				 if(bupdate){
					 
					 
					 
					 List<Integer> idPropuestasAjuste = new ArrayList<Integer>();
					 
					 for (int i = 0; i < Rows.length; i++) {  
						 String Row[]  =  Rows[i].split("@");
						 
						 String parameters[][] = new String[2][2];
						 parameters[0][0] = Row[4];  parameters[0][1] = "0" ; // Row[4] = cuenta 
						 parameters[1][0] = Row[0];  parameters[1][1] = "1" ; // Row[1] = id del ajuste
						 
						 String strSql = "select origen from " +PropertiesSystem.ESQUEMA+".AJUSTES_PRECONCILIACION where id = " + Row[0] ;
						 @SuppressWarnings("unchecked")
						 List<String> lstOrigenAjuste = (ArrayList<String>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSql, true, null);
						 
						 if(lstOrigenAjuste.get(0).trim().compareToIgnoreCase("banco") == 0 ){
							 regProcesados += trans.EjecutarTransaccion("UPDATE "+PropertiesSystem.ESQUEMA
									 +".AJUSTES_PRECONCILIACION SET CUENTA_AJUSTE_ORIGEN = ?, ESTADO=3 WHERE ID = ?", parameters);
						 }else{
							 regProcesados += trans.EjecutarTransaccion("UPDATE "+PropertiesSystem.ESQUEMA
									 +".AJUSTES_PRECONCILIACION SET CUENTA_AJUSTE_DESTINO = ?, ESTADO=3 WHERE ID = ?",parameters);
						 }
						 
						 idsAjustes.add( Row[0] );
						 
						 idPropuestasAjuste.add( Integer.parseInt(Row[0]) );
						 
						 parameters  = new String[2][2];
						 
						 parameters[0][0] = Row[3];  parameters[0][1] = "0" ; // Row[1] = descripcion del ajuste
						 parameters[1][0] = Row[0];  parameters[1][1] = "1" ; 
						 
						 trans.EjecutarTransaccion( "update "+PropertiesSystem.ESQUEMA+".DETALLE_AJUSTES_PRECONCILIACION set estado = 3, observacion = ? where id = ? ", parameters);
						 
						 trans.getCnx().commit();
					 }
					 
					 if( !idPropuestasAjuste.isEmpty() ){
						 
						 shttp.removeAttribute("aex_idmaster"); // controla el ID de maestro de ajustes por excepcion para mandar a generar batch
						 
						 String absolutePath = request.getServletContext().getRealPath("/"+PropertiesSystem.CARPETA_DOCUMENTOS_EXPORTAR+"/");
						 
						 boolean done = crearAjustesPorExcepcion( idPropuestasAjuste, vaut.getId().getCodreg(), absolutePath, vaut.getId().getEmpnombre(), shttp) ;
						 
						 if(!done){
							regProcesados = 0;
							state = 0;
							strMessage = "Ha ocurrido un error durante el procesamiento de los registros";
							out.println("{ \"totalRegs\": \""+regProcesados+"\",\"message\": \""+strMessage+"\" ,\"state\": \""+state+"\" }");
							return;
						 }
						 
						 //	&& ======================= hacer el batch asiento de diario por excepcion.
						 
						 int idmaster = Integer.parseInt( String.valueOf( shttp.getAttribute("aex_idmaster") ) ) ;
						 
						 String[] rowsToProcess = {idmaster+"@"+idmaster}; 
						
						 done = procesarAjustesAprobados(rowsToProcess, vaut);
						 
						 if(done){					  	 
							
							 regProcesados = Rows.length ; 
							 
						 }else{
							 regProcesados = 0;
							 state = 0;
							 lstBathProcesados = new ArrayList<Object[]>();
							 strMessage = "Ha ocurrido un error durante la creacion de los asientos contables";
						 }
						 
						 out.println("{ \"totalRegs\": \""+regProcesados+"\",\"message\": \""+strMessage+"\" ,\"state\": \""+state+"\" }");
		 
						 return;
					 }
					 
				 }
				 
				 
			} catch (Exception e) {
				e.printStackTrace();
				try {
					trans.getCnx().rollback();
					regProcesados = 0;
					state = 0;
					strMessage = "Ha ocurrido un error durante el procesamiento de los registros";
				} catch (Exception e2) {  
					e2.printStackTrace();
				}
			}
			
			trans.closeConnection();
			 
			 
		 }else if(strMode.compareToIgnoreCase("5")==0 || strMode.compareToIgnoreCase("6")==0){  //Eliminar Ajuste
			 trans.openConnection();
			 try {
				 String Row[]  =  strRowid.split("@");
				 if(String.valueOf(strComents).trim().compareToIgnoreCase("")!=0){
					 String strEstado = "5";
					 if(strMode.compareToIgnoreCase("6")==0){
						 strEstado = "2";
					 }	 
					 String parameters[][] = new String[1][2];
					 parameters[0][0] = Row[0];  parameters[0][1] = "1" ;
					 regProcesados += trans.EjecutarTransaccion("UPDATE "+PropertiesSystem.ESQUEMA+".AJUSTES_PRECONCILIACION SET  ESTADO="+strEstado+" WHERE ID = ?",parameters);
					 
					 parameters  = new String[4][2];
					 parameters[0][0] = Row[0];  parameters[0][1] = "1" ;
					 parameters[1][0] = strComents;  parameters[1][1] = "0" ;     
					 parameters[2][0] = strUser;  parameters[2][1] = "0" ;     
					 parameters[3][0] = strEstado;  	 parameters[3][1] = "1" ;   
					 trans.EjecutarTransaccion("INSERT INTO "+PropertiesSystem.ESQUEMA+".DETALLE_AJUSTES_PRECONCILIACION  (ID_AJUSTE,OBSERVACION,USUARIO,ESTADO) VALUES (?,?,?,?)",parameters);
					 
					 if(strMode.compareToIgnoreCase("6")!=0){
						 parameters  = new String[1][2];  
						 parameters[0][0] = Row[1];  parameters[0][1] = "1" ;
						 trans.EjecutarTransaccion((String.valueOf(Row[2]).compareToIgnoreCase("Banco")==0?"UPDATE "+PropertiesSystem.ESQUEMA+".DEPBCODET SET IDESTADOCNFR = 36 WHERE  IDDEPBCODET = ?":"UPDATE "+PropertiesSystem.ESQUEMA+".DEPOSITO SET ESTADOCNFR = 'SCR' WHERE  CONSECUTIVO = ?"), parameters);
					 }					 
					 trans.getCnx().commit();
				 }else{
					 strMessage = " Para eliminar la propuesta necesita ingresar un breve comentario.";
					 state = 0;
				 }  
			} catch (Exception e) {
				e.printStackTrace();
				try {
					trans.getCnx().rollback();
					regProcesados = 0;
					state = 0;
					strMessage = "Ha ocurrido un error durante el procesamiento de los registros";
				} catch (Exception e2) {  
					e2.printStackTrace();
				}
			}
			
			 trans.closeConnection();
			//FIN Eliminar Ajuste	
		 }else if(strMode.compareToIgnoreCase("7")==0){ //Inicio insercion de Ajuste Contable					 
			 boolean bupdate = true;
			 for (int i = 0; i < Rows.length; i++) {  
				 String Row[]  =  Rows[i].split("@");
				 if(String.valueOf(Row[2]).trim().compareToIgnoreCase("false")==0){  
					 if(String.valueOf(Row[3]).trim().compareToIgnoreCase("undefined")==0){  
						 strMessage = " Debe escribir un breve comentario en la cancelación del depósito con referencia: "+getReferenceNumber(shttp,Row[0]);
						 state = 0;
						 bupdate = false;
						 break;
						 
					 }
					
				 }
			}
			
			 
			 try {
				 
				 bupdate = procesarAjustesAprobados(Rows, vaut);
				 
				 if(bupdate){					  	 
					 regProcesados = Rows.length ; 
				 }else{
					 regProcesados = 0;
					 state = 0;
					 lstBathProcesados = new ArrayList<Object[]>();
					 strMessage = "Ha ocurrido un error durante la creacion de los asientos contables";
				 }
				 
				 out.println("{ \"totalRegs\": \""+regProcesados+"\",\"message\": \""+strMessage+"\" ,\"state\": \""+state+"\" }");
 
				 if(bupdate){		
					 return;
				 }
				 
 
				 if(bupdate){
					 
				 for (int i = 0; i < Rows.length; i++) {  
						 String Row[]  =  Rows[i].split("@");	
						 String strEstado = "";
						 if(String.valueOf(Row[2]).trim().compareToIgnoreCase("false")==0){
							 strEstado = "6";
						 }else{
							 strEstado = "4";
						 }
						 String parameters[][] = new String[1][2];
						 parameters[0][0] = Row[0];  parameters[0][1] = "1" ;   				  	
						 regProcesados += trans.EjecutarTransaccion("UPDATE "+PropertiesSystem.ESQUEMA+".AJUSTES_PRECONCILIACION SET  ESTADO="+strEstado+" WHERE ID = ?",parameters);
						   
						 //LEVANTAMOS LA INFORMACION DEL REGISTRO DEL AJUSTE
						 String  Results[] = trans.getData("SELECT MONTO,MONEDA,CUENTA_AJUSTE_ORIGEN,CUENTA_AJUSTE_DESTINO,REFERENCIA, " +
						 		"  BANCO,IFNULL((SELECT BANCO FROM "+PropertiesSystem.ESQUEMA+".F55CA022 WHERE CODB ='100001' FETCH FIRST ROWS ONLY ),'')NOMBRE_BANCO,CASE WHEN ORIGEN = 'Banco' THEN  IFNULL((SELECT  CXCRR FROM "+PropertiesSystem.ESQUEMA+".TCAMBIO WHERE CXEFT = (SELECT FECHAPROCESO FROM "+PropertiesSystem.ESQUEMA+".DEPBCODET " +
						 		"  WHERE IDDEPBCODET = ID_ARCHIVO_FUENTE  FETCH FIRST ROWS ONLY) AND CXCRDC = 'COR' FETCH FIRST ROWS ONLY) ,0.00) " +
					 		    "  ELSE IFNULL((SELECT  CXCRR FROM "+PropertiesSystem.ESQUEMA+".TCAMBIO WHERE CXEFT = (SELECT FECHA FROM "+PropertiesSystem.ESQUEMA+".DEPOSITO  WHERE CONSECUTIVO=ID_ARCHIVO_FUENTE FETCH FIRST ROWS ONLY) AND CXCRDC = 'COR' FETCH FIRST ROWS ONLY) ,0.00) END TASA_DIA," +
						 		"  IFNULL((SELECT CAST(TRIM(GMDL01) AS VARCHAR(100) CCSID 37)  FROM "+PropertiesSystem.ESQUEMA+".Vf0901 WHERE CASE WHEN TRIM(GMSUB)=''   THEN  TRIM(GMMCU)||'.'||TRIM(GMOBJ)  ELSE  TRIM(GMMCU)||'.'||TRIM(GMOBJ)||'.'||TRIM(GMSUB) END  = CUENTA_AJUSTE_ORIGEN),'')NOMBRE_CUENTA_ORIGEN, " +
						 		"  IFNULL((SELECT CAST(TRIM(GMDL01) AS VARCHAR(100) CCSID 37)  FROM "+PropertiesSystem.ESQUEMA+".Vf0901 WHERE CASE WHEN TRIM(GMSUB)=''   THEN  TRIM(GMMCU)||'.'||TRIM(GMOBJ)  ELSE  TRIM(GMMCU)||'.'||TRIM(GMOBJ)||'.'||TRIM(GMSUB) END  = CUENTA_AJUSTE_DESTINO),'')NOMBRE_CUENTA_DESTINO," +
						 		"  IFNULL((SELECT OBSERVACION FROM "+PropertiesSystem.ESQUEMA+".DETALLE_AJUSTES_PRECONCILIACION WHERE  ID_AJUSTE = P.ID ORDER BY ID DESC FETCH FIRST ROWS ONLY) , '')COMENTARIO, "+
						 		"  IFNULL((SELECT CAST(TRIM(GMAID) AS VARCHAR(20) CCSID 37)  FROM "+PropertiesSystem.ESQUEMA+".Vf0901 WHERE CASE WHEN TRIM(GMSUB)=''   THEN  TRIM(GMMCU)||'.'||TRIM(GMOBJ)  ELSE  TRIM(GMMCU)||'.'||TRIM(GMOBJ)||'.'||TRIM(GMSUB) END  = CUENTA_AJUSTE_DESTINO),'')GMAID_CUENTA_DESTINO," +
						 		"  IFNULL((SELECT CAST(TRIM(GMAID) AS VARCHAR(20) CCSID 37)  FROM "+PropertiesSystem.ESQUEMA+".Vf0901 WHERE CASE WHEN TRIM(GMSUB)=''   THEN  TRIM(GMMCU)||'.'||TRIM(GMOBJ)  ELSE  TRIM(GMMCU)||'.'||TRIM(GMOBJ)||'.'||TRIM(GMSUB) END  = CUENTA_AJUSTE_ORIGEN),'')GMAID_CUENTA_ORIGEN, "+
						 		"  IFNULL((SELECT COUNT(*) FROM "+PropertiesSystem.ESQUEMA+".AJUSTES_PRECONCILIACION WHERE YEAR(FECHA_CREACION) = YEAR(P.FECHA_CREACION)  AND MONTH(FECHA_CREACION) =  MONTH(P.FECHA_CREACION) ),1) CANT_AJE, "  +
						 		"  CAJA,ORIGEN "+  
						 		" FROM  "+PropertiesSystem.ESQUEMA+".AJUSTES_PRECONCILIACION P WHERE ID = ?" , parameters) ;   		      
						 
						 parameters  = new String[4][2];
						 parameters[0][0] = Row[0];  parameters[0][1] = "1" ;
						 parameters[1][0] = (String.valueOf(Row[2]).trim().compareToIgnoreCase("false")==0?Row[3]:"");  parameters[1][1] = "0" ;     
						 parameters[2][0] = strUser;  parameters[2][1] = "0" ;     
						 parameters[3][0] = strEstado;  	 parameters[3][1] = "1" ;     
						 trans.EjecutarTransaccion("INSERT INTO "+PropertiesSystem.ESQUEMA+".DETALLE_AJUSTES_PRECONCILIACION  (ID_AJUSTE,OBSERVACION,USUARIO,ESTADO) VALUES (?,?,?,?)",parameters);
						   
						 parameters  = new String[1][2];
						 parameters[0][0] = Row[1];  parameters[0][1] = "1" ;
						 String strState[][] = new String[1][2];
						 if(String.valueOf(Row[2]).trim().compareToIgnoreCase("true")==0){
							 strState[0][0] = "35";
							 strState[0][1] = "CFR";
						 }else{
							 strState[0][0] = "36";
							 strState[0][1] = "SCR";  
						 }
						 trans.EjecutarTransaccion((String.valueOf(Row[2]).compareToIgnoreCase("Banco")==0?"UPDATE "+PropertiesSystem.ESQUEMA+".DEPBCODET SET IDESTADOCNFR = "+strState[0][0]+" WHERE  IDDEPBCODET = ?":"UPDATE "+PropertiesSystem.ESQUEMA+".DEPOSITO SET ESTADOCNFR = '"+strState[0][1]+"' WHERE  CONSECUTIVO = ?"), parameters);
						 //GENERAR ASIENTOS DE DIARIO
						 boolean done = true;
						 if(String.valueOf(Row[2]).trim().compareToIgnoreCase("true")==0){	  			 
							  done = generateAccountingRows(trans.getCnx(),Double.parseDouble(Results[0]),Results[2],Results[3],"Excp Asiento/diario Ajuste ",shttp,Results[1],Results[7],trans,Row[0], Results[11], Results[12],(Results[15]+" ")+Results[14].split("-")[0]+" REF "+Results[4]); 
							
						 }
						 if(done){					  	 
							 trans.getCnx().commit();
						 }else{
							 trans.getCnx().rollback();
							 regProcesados = 0;
							 state = 0;
							 lstBathProcesados = new ArrayList<Object[]>();
							 strMessage = "Ha ocurrido un error durante la creacion de los asientos contables";
						 }
						 
					 } 		 
				     //enviar notifiaciones
				    if(lstBathProcesados.size()>0){  
				    	 new ConfirmaDepositosCtrl().bEnviaCorreoBatchExcepcion(lstBathProcesados, vaut.getId().getCodreg() );    
				    }
				   
				 }
			} catch (Exception e) {
				e.printStackTrace();
				try {
					trans.getCnx().rollback();
					regProcesados = 0;
					state = 0;
					strMessage = "Ha ocurrido un error durante el procesamiento de los registros";
				} catch (Exception e2) {  
					e2.printStackTrace();
				}
			}
			 trans.closeConnection();  				 
		 }else if(strMode.compareToIgnoreCase("8")==0){ //Eliminamos un ajuste aprobado no contabilizado				 
			 trans.openConnection();
			 
			 try {
					 String Row[]  =  strRowid.split("@");
					 String parameters[][] = new String[1][2];
					 parameters[0][0] = Row[0];  parameters[0][1] = "1" ;		 
					 regProcesados = trans.EjecutarTransaccion("UPDATE "+PropertiesSystem.ESQUEMA+".AJUSTES_PRECONCILIACION SET   ESTADO=7 WHERE ID = ?",parameters);
					 parameters[0][0] = Row[1];  parameters[0][1] = "1" ;
					 regProcesados = trans.EjecutarTransaccion((String.valueOf(Row[2]).compareToIgnoreCase("Banco")==0?"UPDATE "+PropertiesSystem.ESQUEMA+".DEPBCODET SET IDESTADOCNFR = 36 WHERE  IDDEPBCODET = ?":"UPDATE "+PropertiesSystem.ESQUEMA+".DEPOSITO SET ESTADOCNFR = 'SCR' WHERE  CONSECUTIVO = ?"), parameters);
					 
					 parameters[0][0] = Row[0];  parameters[0][1] = "1" ;	
					//LEVANTAMOS LA INFORMACION DEL REGISTRO DEL AJUSTE
					 String  Results[] = trans.getDataRe("SELECT MONTO,MONEDA,CUENTA_AJUSTE_ORIGEN,CUENTA_AJUSTE_DESTINO,REFERENCIA, " +
					 		"  BANCO,IFNULL((SELECT BANCO FROM "+PropertiesSystem.ESQUEMA+".F55CA022 WHERE CODB ='100001' FETCH FIRST ROWS ONLY ),'')NOMBRE_BANCO,CASE WHEN ORIGEN = 'Banco' THEN  IFNULL((SELECT  CXCRR FROM "+PropertiesSystem.ESQUEMA+".TCAMBIO WHERE CXEFT = (SELECT FECHAPROCESO FROM "+PropertiesSystem.ESQUEMA+".DEPBCODET " +
					 		"  WHERE IDDEPBCODET = ID_ARCHIVO_FUENTE  FETCH FIRST ROWS ONLY) AND CXCRDC = 'COR' FETCH FIRST ROWS ONLY) ,0.00) " +
				 		    "  ELSE IFNULL((SELECT  CXCRR FROM "+PropertiesSystem.ESQUEMA+".TCAMBIO WHERE CXEFT = (SELECT FECHA FROM "+PropertiesSystem.ESQUEMA+".DEPOSITO  WHERE CONSECUTIVO=ID_ARCHIVO_FUENTE FETCH FIRST ROWS ONLY) AND CXCRDC = 'COR' FETCH FIRST ROWS ONLY) ,0.00) END TASA_DIA," +
					 		"  IFNULL((SELECT CAST(TRIM(GMDL01) AS VARCHAR(100) CCSID 37)  FROM "+PropertiesSystem.ESQUEMA+".Vf0901 WHERE CASE WHEN TRIM(GMSUB)=''   THEN  TRIM(GMMCU)||'.'||TRIM(GMOBJ)  ELSE  TRIM(GMMCU)||'.'||TRIM(GMOBJ)||'.'||TRIM(GMSUB) END  = CUENTA_AJUSTE_ORIGEN),'')NOMBRE_CUENTA_ORIGEN, " +
					 		"  IFNULL((SELECT CAST(TRIM(GMDL01) AS VARCHAR(100) CCSID 37)  FROM "+PropertiesSystem.ESQUEMA+".Vf0901 WHERE CASE WHEN TRIM(GMSUB)=''   THEN  TRIM(GMMCU)||'.'||TRIM(GMOBJ)  ELSE  TRIM(GMMCU)||'.'||TRIM(GMOBJ)||'.'||TRIM(GMSUB) END  = CUENTA_AJUSTE_DESTINO),'')NOMBRE_CUENTA_DESTINO," +
					 		"  IFNULL((SELECT OBSERVACION FROM "+PropertiesSystem.ESQUEMA+".DETALLE_AJUSTES_PRECONCILIACION WHERE  ID_AJUSTE = P.ID ORDER BY ID DESC FETCH FIRST ROWS ONLY) , '')COMENTARIO, "+
					 		"  IFNULL((SELECT CAST(TRIM(GMAID) AS VARCHAR(20) CCSID 37)  FROM "+PropertiesSystem.ESQUEMA+".Vf0901 WHERE CASE WHEN TRIM(GMSUB)=''   THEN  TRIM(GMMCU)||'.'||TRIM(GMOBJ)  ELSE  TRIM(GMMCU)||'.'||TRIM(GMOBJ)||'.'||TRIM(GMSUB) END  = CUENTA_AJUSTE_DESTINO),'')GMAID_CUENTA_DESTINO," +
					 		"  IFNULL((SELECT CAST(TRIM(GMAID) AS VARCHAR(20) CCSID 37)  FROM "+PropertiesSystem.ESQUEMA+".Vf0901 WHERE CASE WHEN TRIM(GMSUB)=''   THEN  TRIM(GMMCU)||'.'||TRIM(GMOBJ)  ELSE  TRIM(GMMCU)||'.'||TRIM(GMOBJ)||'.'||TRIM(GMSUB) END  = CUENTA_AJUSTE_ORIGEN),'')GMAID_CUENTA_ORIGEN, "+
					 		"  IFNULL((SELECT COUNT(*) FROM "+PropertiesSystem.ESQUEMA+".AJUSTES_PRECONCILIACION WHERE YEAR(FECHA_CREACION) = YEAR(P.FECHA_CREACION)  AND MONTH(FECHA_CREACION) =  MONTH(P.FECHA_CREACION) ),1) CANT_AJE, "  +
					 		"  CAJA,ORIGEN , (SELECT  COUNT(*)  FROM "+PropertiesSystem.JDEDTA+".F0911 WHERE GLICU = NOBATCH  AND  TRIM(GLPOST) = 'D' ) CONTABILIZADO, NOBATCH "+  
					 		" FROM  "+PropertiesSystem.ESQUEMA+".AJUSTES_PRECONCILIACION P WHERE ID = ?" , parameters) ;   
					 
					 if(Integer.parseInt(String.valueOf(Results[16]))==0 && regProcesados > 0){  
						 parameters[0][0] = String.valueOf(Results[17]);  parameters[0][1] = "1" ;  
						 trans.EjecutarTransaccion("DELETE FROM "+PropertiesSystem.JDEDTA+".F0911 WHERE GLICU = ? ",parameters);  
						 trans.getCnx().commit();  
					 }else if(Integer.parseInt(String.valueOf(Results[14]))>0){
						 trans.getCnx().rollback();
						 regProcesados = 0;
						 state = 0;
					     strMessage = "No se pueden eliminar ajustes contabilizados!";
					 }else{
						 trans.getCnx().rollback();
					 } 
					 
			} catch (Exception e) {
				e.printStackTrace();
				try {
					trans.getCnx().rollback();
					regProcesados = 0;
					state = 0;
					strMessage = "Ha ocurrido un error durante el procesamiento de los registros";
				} catch (Exception e2) {  
					e2.printStackTrace();
				}
			}
			 trans.closeConnection();			 
		 }
			out.println("{ \"totalRegs\": \""+regProcesados+"\",\"message\": \""+strMessage+"\" ,\"state\": \""+state+"\" }");
			
		}else{
			out.println("{ \"totalRegs\": \"1\",\"message\": \"Su sesion ha caducado\" ,\"state\": \"-1\" }");
		}
			
		} catch (Exception e) {  
			e.printStackTrace();
		} finally {
			out.close();
			
		}
	}
	
	/**
	 * procesar el ajuste y crear el asiento de diario en JDE 
	 */
	@SuppressWarnings("unchecked")
	public boolean procesarAjustesAprobados( String Rows[], Vautoriz vaut){
		boolean done = true;
		
		try {
			
			String strSqlQuerySelect;
			
			List<Object[]> dtaAjusteMaestro;
			List<Object[]> dtaAjusteDetalle;
			
			List<String> UpdatesCaja;
			
			
			for (int i = 0; i < Rows.length; i++) {  
				
				
				String Row[]  =  Rows[i].split("@");	
				 
				 strSqlQuerySelect = "select fechamod, usermod, cantidad_documentos, moneda, monto_transaccion, codcomp, idajusteexcepcion " +
				 		" from "+PropertiesSystem.ESQUEMA+".PCD_MT_AJUSTE_EXCEPCION_DEPOSITO where idajusteexcepcion = " + Row[0] ;
				 dtaAjusteMaestro = (ArrayList<Object[]>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSqlQuerySelect, true, null);
				 
				 strSqlQuerySelect = "select  " +
				 		" monto, id_cuenta_origen, id_cuenta_destino, " +
				 		" cuenta_origen, cuenta_destino, observacion, iddtaed, " +
				 		" id_propuesta_ajuste, cuenta_bancaria, tipo," +
				 		" ( case when tipo = 2 then (select caid from "+PropertiesSystem.ESQUEMA+".deposito where consecutivo = rowidbancocaja ) else rowidbancocaja end ) caid " +
				 		" from "+PropertiesSystem.ESQUEMA+".PCD_DT_AJUSTE_EXCEPCION_DEPOSITO where id_mt_ajuste_excepcion = " + Row[0] ;
				 dtaAjusteDetalle = (ArrayList<Object[]>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSqlQuerySelect, true, null);
						 
				 UpdatesCaja = new ArrayList<String>();
				 
				 //&& ========== crear batch en jde
				 done = crearBatchPorAjuste(dtaAjusteMaestro.get(0), dtaAjusteDetalle, vaut, UpdatesCaja) ;
			
				// && ========== actualizacion en caja
				if (done) {
					done = ConsolidadoDepositosBcoCtrl.executeSqlQueries(UpdatesCaja);
				}
					
				// && ========== mandar a contabilizar el batch
				/*
				if (done) {
					 P5509800Input input = new P5509800Input();
					 P5509800_Service service = new P5509800_Service();
					 P5509800PortType port = service.getP5509800HttpSoap11Endpoint();
					 port.p5509800(input);
				}
				*/
			}
			
		} catch (Exception e) {
			done = false;
			e.printStackTrace();
		}finally{
			
		}
		
		return done;
	}
	
	
	@SuppressWarnings("unchecked")
	public boolean crearBatchPorAjuste( Object[]dtaAjusteMaestro, List<Object[]>  dtaAjusteDetalle, Vautoriz vaut, List<String> updateDetalles){
		boolean hecho = true;
		
		int NobatchToUse;
		long iMontoTotal;
		
		int numerodocjde ;
		String tipodocumento;
		
		String monedabase="COR";
		
		Session session = null;
		Transaction transaction = null;
		com.casapellas.controles.ReciboCtrl rcCtrl = new com.casapellas.controles.ReciboCtrl();
		
		try {
			
			//&& ========== Conexion para JDE
			session = HibernateUtilPruebaCn.currentSession();
			transaction = session.beginTransaction();
			
			
			//&& ========== Datos del encabezado del ajuste.
			Date fechabatch =  new Date() ;
			String moneda_ajuste = String.valueOf( dtaAjusteMaestro[3] );
			BigDecimal monto_ajuste = (BigDecimal)dtaAjusteMaestro[4] ;
			String codcomp = String.valueOf( dtaAjusteMaestro[5] );
			String idajuste = String.valueOf(dtaAjusteMaestro[6]) ;
			String usuariobatch = vaut.getId().getLogin();
			tipodocumento = DocumuentosTransaccionales.TIPODOCREFERZX() ;
			
			usuariobatch = DocumuentosTransaccionales.USUARIOPRECONCILIACION();
			 
			
			//&& ========== crear encabezado del batch F0011
			NobatchToUse = Divisas.numeroSiguienteJdeE1( );
			
			iMontoTotal = Divisas.pasarAenteroLong(  monto_ajuste.doubleValue() );
			String[] valoresJDEInsContado = DocumuentosTransaccionales.getValuesJDEContado().split(",");
			hecho = rcCtrl.registrarBatchA92(session, fechabatch, valoresJDEInsContado[8], NobatchToUse, iMontoTotal, usuariobatch, 1, "AJUSTE-EXC", valoresJDEInsContado[9] );
			
			if(!hecho)
				return false;
			
			long monto_linea;
			BigDecimal monto_linea_ajuste ;
			double numero_linea = 0 ;
			
			String id_cuenta_origen;
			String id_cuenta_destino;
			
			String[] cuenta_origen;
			String[] cuenta_destino;
			
			String sCodSucAsiento;
			String observacion ;
			
			int caidDepsCaja ;
			int cuenta_bancaria;			
			String strSubLibroCuenta;
			String strTipoAuxiliarCt;
			
			
			String f0901_doc = "select trim( gmco ) from " +PropertiesSystem.ESQUEMA +".vf0901 where gmaid = " + String.valueOf( dtaAjusteDetalle.get(0)[1] ) +" fetch first rows only "; 
			sCodSucAsiento = ((List<String>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(f0901_doc, true, null)).get(0);		
			
			String update = 
				"update @GCPMCAJA.pcd_mt_ajuste_excepcion_deposito " +
				"set numerobatch = @NOBATCH, tipodoc ='@TIPODOC', codigo_sucursal_documento = '@CODSUCDOC', " +
				"estado = @NUEVOESTADO, usermod = @USRMOD, fechamod = current_date where idajusteexcepcion = @IDAJUSTE"  ;
			
			updateDetalles.add( 
				update.replace("@GCPMCAJA", PropertiesSystem.ESQUEMA )
					.replace("@NOBATCH", Integer.toString( NobatchToUse )  )
					.replace("@TIPODOC", tipodocumento)
					.replace("@CODSUCDOC", sCodSucAsiento)
					.replace("@NUEVOESTADO", "2")
					.replace("@USRMOD", Integer.toString( vaut.getId().getCodreg() ) )
					.replace("@IDAJUSTE", idajuste) 
				);
			
			BigDecimal  tasa_cambio_oficial = TasaCambioCtrl.obtenerTasaOficialPorFecha( fechabatch, moneda_ajuste,  monedabase) ;
			
			//&& ========== crear las lineas para cada ajuste incluido en el batch
			for (int i = 0; i < dtaAjusteDetalle.size(); i++) {
				
				Object[] lineaDetalleAjuste = dtaAjusteDetalle.get(i);
				
				
				//&& ============= crear el auxiliar de la cuenta transitoria para casos de excepciones tipo Caja
				if( Integer.parseInt(String.valueOf( lineaDetalleAjuste[9] ) ) == 2){
					
					caidDepsCaja = Integer.parseInt(String.valueOf( lineaDetalleAjuste[10] ) ) ;
					cuenta_bancaria =  Integer.parseInt( String.valueOf( lineaDetalleAjuste[8] ) ) ;
					strSubLibroCuenta = ConfirmaDepositosCtrl.constructSubLibroCtaTbanco (cuenta_bancaria, 0, caidDepsCaja, "", "" );
					strTipoAuxiliarCt = DocumuentosTransaccionales.CODIGOTIPOAUXILIARCT() ;
					
				}else{
					caidDepsCaja = 0 ;
					cuenta_bancaria =  Integer.parseInt( String.valueOf( lineaDetalleAjuste[8] ) ) ;
					strSubLibroCuenta = "";
					strTipoAuxiliarCt = "" ;
				}
				
				//numerodocjde = RevisionArqueoCtrl.obtenerNumeroDocumento(tipodocumento, codcomp, moneda_ajuste);
				
				numerodocjde = Divisas.numeroSiguienteJde( CodigosJDE1.NUM_TIPODOC_JDE_DEP_5, codcomp );
				if(numerodocjde == 0){
					numerodocjde = Divisas.numeroSiguienteJdeE1( CodigosJDE1.NUMERO_DOC_CONTAB_GENERAL );
				}
				
				numerodocjde = RevisionArqueoCtrl.generarReferenciaDeposito(numerodocjde, tipodocumento, sCodSucAsiento, moneda_ajuste);
				
				monto_linea_ajuste = (BigDecimal)lineaDetalleAjuste[0] ;
				id_cuenta_origen = String.valueOf(lineaDetalleAjuste[1]);
				id_cuenta_destino = String.valueOf(lineaDetalleAjuste[2]);
				
				observacion = String.valueOf(lineaDetalleAjuste[5]);
				observacion = "Ajst: "+ String.valueOf(dtaAjusteMaestro[6]) + " Excepcion: "+ String.valueOf(lineaDetalleAjuste[6]);
				
				cuenta_origen  = obtenerDatosCuenta(id_cuenta_origen, codcomp);
				cuenta_destino = obtenerDatosCuenta(id_cuenta_destino, codcomp);
				
				monto_linea = Divisas.pasarAenteroLong( monto_linea_ajuste.doubleValue() );
				
				String msgLogs = observacion;
				
				if(moneda_ajuste.compareTo( monedabase ) == 0 ){
					
					hecho = rcCtrl.registrarAsientoDiarioLogs(session, msgLogs, fechabatch, sCodSucAsiento, tipodocumento,
								numerodocjde, (++numero_linea), NobatchToUse, cuenta_destino[0], cuenta_destino[1], cuenta_destino[3],
								cuenta_destino[4], cuenta_destino[5], "AA", moneda_ajuste, monto_linea, observacion,
								usuariobatch, vaut.getId().getCodapp(), BigDecimal.ZERO, "E", 
								observacion, cuenta_destino[2], "", "", monedabase, sCodSucAsiento, "D", 0);
					
					if(!hecho){
						return false;
					}
					
					hecho = rcCtrl.registrarAsientoDiarioLogs( session, msgLogs, fechabatch,  sCodSucAsiento, tipodocumento,
							numerodocjde, (++numero_linea), NobatchToUse, cuenta_origen[0], cuenta_origen[1], cuenta_origen[3],
							cuenta_origen[4], cuenta_origen[5], "AA", moneda_ajuste, (-1*monto_linea), observacion,
							usuariobatch, vaut.getId().getCodapp(), BigDecimal.ZERO, "E", 
							observacion, cuenta_origen[2], strSubLibroCuenta, strTipoAuxiliarCt, monedabase, sCodSucAsiento, "D", 0);
					
					if(!hecho){
						return false;
					}
					
				}else{
					
					long iMontoDepsdom = Divisas.pasarAenteroLong( Divisas.roundDouble(monto_linea_ajuste.multiply( tasa_cambio_oficial ).doubleValue()));
					
					//&& ============ cuenta destino (acreditar)
					hecho = rcCtrl.registrarAsientoDiarioLogs( session, msgLogs, fechabatch, sCodSucAsiento, tipodocumento,
								numerodocjde,  (++numero_linea), NobatchToUse, cuenta_destino[0], cuenta_destino[1], cuenta_destino[3],
								cuenta_destino[4], cuenta_destino[5], "AA", moneda_ajuste, iMontoDepsdom, observacion,
								usuariobatch, vaut.getId().getCodapp(), tasa_cambio_oficial, "E", 
								observacion, cuenta_destino[2], "", "", monedabase, sCodSucAsiento, "F",  monto_linea );
					if(!hecho){
						return false;
					}
					hecho = rcCtrl.registrarAsientoDiarioLogs( session, msgLogs,  fechabatch,  sCodSucAsiento, tipodocumento,
								numerodocjde, (numero_linea), NobatchToUse, cuenta_destino[0], cuenta_destino[1], cuenta_destino[3],
								cuenta_destino[4],cuenta_destino[5], "CA", moneda_ajuste, monto_linea, observacion,
								usuariobatch, vaut.getId().getCodapp(), tasa_cambio_oficial, "E", 
								observacion, cuenta_destino[2], "", "", monedabase, sCodSucAsiento, "F", 0 );
					if(!hecho){
						return false;
					}
					//&& ========== cuenta origen (debitar)
					hecho = rcCtrl.registrarAsientoDiarioLogs( session, msgLogs, fechabatch, sCodSucAsiento, tipodocumento,
							numerodocjde,  (++numero_linea), NobatchToUse, cuenta_origen[0], cuenta_origen[1], cuenta_origen[3],
							cuenta_origen[4], cuenta_origen[5], "AA", moneda_ajuste, (-1*iMontoDepsdom), observacion,
							usuariobatch, vaut.getId().getCodapp(), tasa_cambio_oficial, "E", 
							observacion, cuenta_origen[2], strSubLibroCuenta, strTipoAuxiliarCt, monedabase, sCodSucAsiento, "F" ,(-1*monto_linea));
					if(!hecho){
						return false;
					}
					hecho = rcCtrl.registrarAsientoDiarioLogs( session, msgLogs, fechabatch, sCodSucAsiento, tipodocumento,
								numerodocjde, (numero_linea), NobatchToUse, cuenta_origen[0], cuenta_origen[1], cuenta_origen[3],
								cuenta_origen[4],cuenta_origen[5], "CA", moneda_ajuste, (-1*monto_linea), observacion,
								usuariobatch, vaut.getId().getCodapp(), tasa_cambio_oficial, "E", 
								observacion, cuenta_origen[2], strSubLibroCuenta, strTipoAuxiliarCt, monedabase, sCodSucAsiento, "F", 0);
					if(!hecho){
						return false;
					}
				}
				
				updateDetalles.add( " update " + PropertiesSystem.ESQUEMA +".pcd_dt_ajuste_excepcion_deposito set numero_documento = " + numerodocjde  +" where iddtaed = " + lineaDetalleAjuste[6] ) ;
				updateDetalles.add( " update " + PropertiesSystem.ESQUEMA +".ajustes_preconciliacion set estado = 4 where id =  " + lineaDetalleAjuste[7]  ) ;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			hecho = false;
		}finally{
			
			try {
				
				 if( hecho ){
					 transaction.commit();
				 }else{
					 transaction.rollback();
				 }
				
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
			try {
				HibernateUtilPruebaCn.closeSession(session);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		return hecho;
		
	}
	
	public String[] obtenerDatosCuenta(String gmaid, String codcomp){
		String[] dtaCuenta  = null;
		
		try {
		
			// && ========================== cuentas para ajustes por excepcion en proceso de generacion automatica de comprobantes
			String strSqlQueryExecute =
				"select trim(gmmcu) ||'.'|| trim(gmobj) ||'.'|| trim(gmsub)   ||'@@@'|| "+
					"trim(gmaid) ||'@@@'||  "+
					"right( trim( gmco ),  2 ) ||'@@@'||  "+
					"trim(gmmcu) ||'@@@'|| "+
					"trim(gmobj) ||'@@@'|| "+
					"trim(gmsub) ||'@@@'|| "+
					"'"+codcomp.trim()+"' ||'@@@'|| " +
					"trim(gmco)" +
				"from "+PropertiesSystem.ESQUEMA+".vf0901  " +
				"where trim(gmaid)  = " + gmaid ; 
					 
			@SuppressWarnings("unchecked")
			List<String>dtaQueryExecuted = (ArrayList<String>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSqlQueryExecute, true, null)  ; 
		
			dtaCuenta = dtaQueryExecuted.get(0).split("@@@") ;
		 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtaCuenta;
	}
	
	
	
	
	
	
	
	@SuppressWarnings("unchecked")
	private boolean crearAjustesPorExcepcion( List<Integer> idPropuestasAjuste, int codigousuario, 
								String absolutePath, String nombreUsuario,  HttpSession shttp  ){
		
		Session session = null;
		Transaction trans = null;
		boolean newCn = false;
		boolean done = true;
		
		try {
			 
			session = HibernateUtilPruebaCn.currentSession();
			trans = (newCn = !(session.getTransaction().isActive())) ? session.beginTransaction() : session.getTransaction();
			
			long idmaster = 0 ;
			 
			String strSelectQuery = 
				"select MONTO, MONEDA, CODCOMP, " +
				"(select trim(drdl01) from @CRPMCAJA.vcompania where drky = CODCOMP ) "  +
				"from @CRPMCAJA.AJUSTES_PRECONCILIACION ap where id in " +  idPropuestasAjuste.toString().replace("[", "(") .replace("]", ")");
			
			strSelectQuery = strSelectQuery.replace("@CRPMCAJA", PropertiesSystem.ESQUEMA);
			
			List<Object[]> lstSelectedinQuery =  (ArrayList<Object[]>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSelectQuery, true, null);
			
			String moneda  = String.valueOf( lstSelectedinQuery.get(0)[1]  ) ;
			String codcomp = String.valueOf( lstSelectedinQuery.get(0)[2]  ) ;
			String nombrecomp = String.valueOf( lstSelectedinQuery.get(0)[3] ) ;
			
			BigDecimal montoTotal = BigDecimal.ZERO;
			for (Object[] monto : lstSelectedinQuery) {
				montoTotal = montoTotal.add( ((BigDecimal) monto[0] )  );
			}
			
			String dateInsert = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS").format(new Date());
			
			String strInsertMaster = 
				"insert into @CRPMCAJA.PCD_MT_AJUSTE_EXCEPCION_DEPOSITO " +
				"(USERCREA, USERMOD, CANTIDAD_DOCUMENTOS, MONEDA, MONTO_TRANSACCION, CODCOMP, DATE_INSERT_ROW)" +
				"values ( @USERCREA, @USERMOD, @CANTIDAD_DOCUMENTOS, '@MONEDA', @MONTO_TRANSACCION, '@CODCOMP', '@DATE_INSERT_ROW' )"  ;
			
			strInsertMaster = strInsertMaster
				.replace("@CRPMCAJA", PropertiesSystem.ESQUEMA )
				.replace("@USERCREA", Integer.toString(codigousuario) )
				.replace("@USERMOD",  Integer.toString(codigousuario) )
				.replace("@CANTIDAD_DOCUMENTOS",  Integer.toString(idPropuestasAjuste.size() ) )
				.replace("@MONEDA",  moneda )
				.replace("@MONTO_TRANSACCION", montoTotal.toString() )
				.replace("@MONEDA", moneda )
				.replace("@CODCOMP", codcomp )
				.replace("@DATE_INSERT_ROW", dateInsert ) ;
			
			done = ConsolidadoDepositosBcoCtrl.executeSqlQueries ( Arrays.asList( new String[]{strInsertMaster} ) );
			
			if(!done){
				return done;
			}
			
			strSelectQuery = 
				"select IDAJUSTEEXCEPCION, ESTADO " + 
				"from @CRPMCAJA.PCD_MT_AJUSTE_EXCEPCION_DEPOSITO where DATE_INSERT_ROW = '" + dateInsert +"' " ;
			
			strSelectQuery = strSelectQuery.replace("@CRPMCAJA", PropertiesSystem.ESQUEMA);
			lstSelectedinQuery = (ArrayList<Object[]>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSelectQuery, true, null);
			
			idmaster = Long.parseLong( String.valueOf( lstSelectedinQuery.get(0)[0] ) ) ;
			
			//&& ========== conservar el id del maestro de ajuste para luego crear el batch en jde
			shttp.setAttribute("aex_idmaster", idmaster);
			
			String insertDetails = 
			" insert into @CRPMCAJA.PCD_DT_AJUSTE_EXCEPCION_DEPOSITO " +
			" (ID_MT_AJUSTE_EXCEPCION, TIPO, ORIGEN, MONTO, ID_CUENTA_ORIGEN, ID_CUENTA_DESTINO, " +
			" CUENTA_ORIGEN, CUENTA_DESTINO, USUARIO_PROPUESTA, ROWIDBANCOCAJA, " +
			" ID_PROPUESTA_AJUSTE, CUENTA_BANCARIA, REFERENCIA, OBSERVACION) ";
			
			String strSelectDetails = 
			"select " +
			 idmaster + ", " +
			" (case when trim(caja) = '' then 1 else 2 end ) tipo," +
			" origen, monto, " +
			" ( select gmaid from @CRPMCAJA.vf0901 where (trim(gmmcu) || '.' || trim(gmobj) || ( case when trim(gmsub) = '' then '' else '.'|| trim(gmsub) end ) ) = trim(cuenta_ajuste_origen) ) idcuentaorigen," +
			" ( select gmaid from @CRPMCAJA.vf0901 where (trim(gmmcu) || '.' || trim(gmobj) || ( case when trim(gmsub) = '' then '' else '.'|| trim(gmsub) end ) ) = trim(cuenta_ajuste_destino) ) idcuentadestino," +
			" cuenta_ajuste_origen," + 
			" cuenta_ajuste_destino," +
			" (select codreg from "+PropertiesSystem.ENS+ ".usuario where lower(trim(login)) = lower(trim(usuario)) ) usuariopropuesta," +
			" id_archivo_fuente, " +
			" id," +
			
			//" cuenta," +
			
			"(" +
			" case when trim(caja) <> '' " +
			" then (select ifnull( codigo_cuenta_bancaria, cuenta) from @CRPMCAJA.f55ca033 f where f.b3codb = ap.banco and f.b3rp01 = ap.codcomp and f.b3crcd = ap.moneda  )" +
			" else ap.cuenta end" +
			" ) cuenta, " +
			
			" referencia," +
			" ifnull( (select observacion from @CRPMCAJA.detalle_ajustes_preconciliacion where id_ajuste = AP.id ), '' ) observacion" +
			" from @CRPMCAJA.AJUSTES_PRECONCILIACION AP" +
			" where id in " +  idPropuestasAjuste.toString().replace("[", "(") .replace("]", ")" ) ;
			
			insertDetails += "("+ strSelectDetails +")" ;
			
			insertDetails = insertDetails.replace("@CRPMCAJA", PropertiesSystem.ESQUEMA);
			
			done = ConsolidadoDepositosBcoCtrl.executeSqlQueries(  Arrays.asList( new String[]{insertDetails} ) ) ;
			
			 // && ============== Generar el pdf 
			if(done){
				
				String strCodReg = "select distinct(USUARIO_PROPUESTA) from @CRPMCAJA.PCD_DT_AJUSTE_EXCEPCION_DEPOSITO " +
						"where ID_PROPUESTA_AJUSTE in "+  idPropuestasAjuste.toString().replace("[", "(") .replace("]", ")" ) ;
				
				strSelectQuery =  
					"select lower(trim(nombre_usuario) ), lower(trim(cargo_firma)), lower(trim(nombre_usuario) ) || ' <> ' || lower(trim(correo_envio)), tipo_usuario " +
					"from @CRPMCAJA.PCD_NOTIFICACION_EXCEPCIONES WHERE estado = 1 and  " +
					" (codigo_usuario in (@CODUSERPROP) and tipo_usuario = 0 ) or " +
					" (codigo_usuario = @CODUSERAPR and tipo_usuario = 1 ) or " +
					" tipo_usuario = 2  order by tipo_usuario asc" ;
				
				strSelectQuery = strSelectQuery
						.replace("@CODUSERPROP", strCodReg)
						.replace("@CODUSERAPR", Integer.toString(codigousuario) )
						.replace("@CRPMCAJA", PropertiesSystem.ESQUEMA);
				
				List<Object[]> dataNotifc = (List<Object[]>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSelectQuery, true, null) ;
				
				List<String> firmas = new ArrayList<String>() ;
				List<String> enviosCorreo = new ArrayList<String>() ;
				String nombre ;
				
				for (int i = 0; i < dataNotifc.size(); i++) {
					
					nombre = String.valueOf( dataNotifc.get(i)[0] ).trim() ;
					nombre = (nombre.length() > 28) ? nombre.substring(0, 27) : nombre;
					
					firmas.add( nombre +" \n "+  String.valueOf( dataNotifc.get(i)[1] ) ) ;
					
					if( String.valueOf( dataNotifc.get(i)[3] ).trim().compareTo("0") == 0 ||  String.valueOf( dataNotifc.get(i)[3] ).trim().compareTo("1") == 0  )
						enviosCorreo.add( String.valueOf( dataNotifc.get(i)[2] ) ) ;
					 
				}
				
				absolutePath = absolutePath + "Propuesta_Ajuste_Excepcion_" + CodeUtil.pad(Long.toString( idmaster ) , 3, "0")+".pdf" ;
				
				Rptmcaja012Pdf rpt =  new Rptmcaja012Pdf(codcomp, nombrecomp, nombreUsuario, firmas, absolutePath , idmaster,  enviosCorreo ) ;
				rpt.generarHojaAutorizacion();
				
			}
			
			
			
		} catch (Exception e) {
			done = false;
			e.printStackTrace();
		}finally{
		
			if (done) {
				trans.commit();
			} else {
				trans.rollback();
			}
			try {
				HibernateUtilPruebaCn.closeSession(session);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			session = null;
			trans = null;
		}
		return done;
	}
	
	
	private String getReferenceNumber( HttpSession shttp,String id){
		String strResult = "";
		try {
			List Rows = (List)shttp.getAttribute("depositoConf");
			
			for (Iterator iterator = Rows.iterator(); iterator.hasNext();) {
				Object object[] = (Object[]) iterator.next();
				if(String.valueOf(object[10]).compareToIgnoreCase(id)==0){
					strResult =  String.valueOf(object[1]);
				}				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return strResult;
	}
	  
	
	private boolean generateAccountingRows(Connection cn,Double dMonto,String strCuentaCuentaOrigen,String strCuentaDestino,String sConcepto,HttpSession shttp,String strMonedaDeposito,String strTasa,jdbcTransaction trans,String idTbAjuste,String strGmaidCuentaDestino,String strGmaidCuentaOrigen,String strDescri){
		boolean result = false;
		ReciboCtrl recCtrl = new ReciboCtrl();
		Divisas d = new Divisas();
		int iNoBatch = 0,iNoDocForaneo = 0;
		try {
			Vautoriz[] vaut = (Vautoriz[])  shttp.getAttribute("sevAut");
			
			//inicio
			iNoBatch =  d.leerActualizarNoBatch();
			// leer y actualizar los asientos para los documentos foraneos
						iNoDocForaneo = d.leerActualizarNoDocJDE();
			int iContadorDom = 1;	
			//obtener el id de la cuenta de caja.
			ReciboCtrl rcCtrl = new ReciboCtrl();
			  
				
				if(strMonedaDeposito.compareToIgnoreCase("COR")==0){          
					
				result = recCtrl.registrarAsientoDiario( new Date(), cn,String.valueOf(strCuentaDestino.split("\\.")[0]).substring(0,2),"ZX", iNoDocForaneo,(iContadorDom) * 1.0,iNoBatch,strCuentaDestino,strGmaidCuentaDestino,String.valueOf(strCuentaDestino.split("\\.")[0]).substring(0,2),
						strCuentaDestino.split("\\.")[1],(strCuentaDestino.split("\\.").length>2 ? strCuentaDestino.split("\\.")[2] :""),"AA",strMonedaDeposito,d.pasarAentero(dMonto),
						sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),BigDecimal.ZERO,"",strDescri,   
						String.valueOf(strCuentaDestino.split("\\.")[0]).substring(0,2),"","",strMonedaDeposito,String.valueOf(strCuentaDestino.split("\\.")[0]).substring(0,2),"D");
					if(!result){
						return result;
					}
				iContadorDom++;
				result = recCtrl.registrarAsientoDiario( new Date(), cn,String.valueOf(strCuentaDestino.split("\\.")[0]).substring(0,2), "ZX", iNoDocForaneo,(iContadorDom ) * 1.0,iNoBatch,strCuentaCuentaOrigen,strGmaidCuentaOrigen,String.valueOf(strCuentaCuentaOrigen.split("\\.")[0]).substring(0,2),
						strCuentaCuentaOrigen.split("\\.")[1],(strCuentaCuentaOrigen.split("\\.").length>2 ? strCuentaCuentaOrigen.split("\\.")[2] :""),"AA",strMonedaDeposito,(-1)* (d.pasarAentero(dMonto)),
						sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),BigDecimal.ZERO,"",strDescri,
						String.valueOf(strCuentaCuentaOrigen.split("\\.")[0]).substring(0,2),"","",strMonedaDeposito,String.valueOf(strCuentaCuentaOrigen.split("\\.")[0]).substring(0,2),"D");  
					if(!result){
						return result;
					}
					  
					result = rcCtrl.registrarBatchA92(cn,"G" ,iNoBatch,d.pasarAentero(dMonto), vaut[0].getId().getLogin(), 1, "N");//REGISTRO DE BATCH EN MAESTRO
					if(!result){
						return result;
					}
				}else{
					BigDecimal btasa = new BigDecimal(strTasa);
					
					//generacion de asiento de carga a cuenta  
					result = recCtrl.registrarAsientoDiario( new Date(), cn,String.valueOf(strCuentaDestino.split("\\.")[0]).substring(0,2),"ZX", iNoDocForaneo,(iContadorDom) * 1.0,iNoBatch,strCuentaDestino,strGmaidCuentaDestino,String.valueOf(strCuentaDestino.split("\\.")[0]).substring(0,2),
							strCuentaDestino.split("\\.")[1],(strCuentaDestino.split("\\.").length>2 ? strCuentaDestino.split("\\.")[2] :""),"AA",strMonedaDeposito,d.pasarAentero(dMonto * btasa.doubleValue()),
							sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),btasa,"",strDescri,     
							String.valueOf(strCuentaDestino.split("\\.")[0]).substring(0,2),"","","COR",String.valueOf(strCuentaDestino.split("\\.")[0]).substring(0,2),"D");
						if(!result){
							return result;
						}
						
						result = recCtrl.registrarAsientoDiario( new Date(), cn,String.valueOf(strCuentaDestino.split("\\.")[0]).substring(0,2), "ZX", iNoDocForaneo,(iContadorDom) * 1.0,iNoBatch,strCuentaDestino,strGmaidCuentaDestino,String.valueOf(strCuentaDestino.split("\\.")[0]).substring(0,2),
								strCuentaDestino.split("\\.")[1],(strCuentaDestino.split("\\.").length>2 ? strCuentaDestino.split("\\.")[2] :""),"CA",strMonedaDeposito,d.pasarAentero(dMonto),
								sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),btasa,"",strDescri,   
								String.valueOf(strCuentaDestino.split("\\.")[0]).substring(0,2),"","",strMonedaDeposito,String.valueOf(strCuentaDestino.split("\\.")[0]).substring(0,2),"D");  
						if(!result){
							return result; 
						}  
						
						iContadorDom ++;
						//generacion de asientos de descarga 
					    result = recCtrl.registrarAsientoDiario( new Date(), cn,String.valueOf(strCuentaDestino.split("\\.")[0]).substring(0,2), "ZX", iNoDocForaneo,(iContadorDom ) * 1.0,iNoBatch,strCuentaCuentaOrigen,strGmaidCuentaOrigen,String.valueOf(strCuentaCuentaOrigen.split("\\.")[0]).substring(0,2),
							strCuentaCuentaOrigen.split("\\.")[1],(strCuentaCuentaOrigen.split("\\.").length>2 ? strCuentaCuentaOrigen.split("\\.")[2] :""),"AA",strMonedaDeposito,(-1)* (d.pasarAentero(dMonto * btasa.doubleValue())),
							sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),btasa,"",strDescri,
							String.valueOf(strCuentaCuentaOrigen.split("\\.")[0]).substring(0,2),"","","COR",String.valueOf(strCuentaCuentaOrigen.split("\\.")[0]).substring(0,2),"D");  
						if(!result){
							return result;
						}
				 
					
					
					result = recCtrl.registrarAsientoDiario( new Date(), cn,String.valueOf(strCuentaDestino.split("\\.")[0]).substring(0,2), "ZX", iNoDocForaneo,(iContadorDom ) * 1.0,iNoBatch,strCuentaCuentaOrigen,strGmaidCuentaOrigen,String.valueOf(strCuentaCuentaOrigen.split("\\.")[0]).substring(0,2),
							strCuentaCuentaOrigen.split("\\.")[1],(strCuentaCuentaOrigen.split("\\.").length>2 ? strCuentaCuentaOrigen.split("\\.")[2] :""), "CA",strMonedaDeposito,(-1)* (d.pasarAentero(dMonto)),
							sConcepto,vaut[0].getId().getLogin(),vaut[0].getId().getCodapp(),btasa,"",strDescri,  
							String.valueOf(strCuentaCuentaOrigen.split("\\.")[0]).substring(0,2),"","",strMonedaDeposito,String.valueOf(strCuentaCuentaOrigen.split("\\.")[0]).substring(0,2),"D");    
					
					if(!result){    
						return result;
					}
					result = rcCtrl.registrarBatchA92(cn,"G" ,iNoBatch,d.pasarAentero(dMonto), vaut[0].getId().getLogin(), 1, "N");//REGISTRO DE BATCH EN MAESTRO
				}	
				
				Object obj[] = new Object[6];
				obj[0] = iNoBatch+"";
				obj[1] = iNoDocForaneo+"";
				obj[2] = "ZX";
				obj[3] = dMonto+"";
				obj[4] = strMonedaDeposito+"";
				obj[5] = vaut[0].getId().getLogin();
				lstBathProcesados.add(obj); 
				
			
			 String parameters[][]  = new String[2][2];
			 parameters[0][0] = iNoBatch+"";  parameters[0][1] = "0" ; 
			 parameters[1][0] = idTbAjuste;  parameters[1][1] = "1" ;			
			 trans.EjecutarTransaccion("UPDATE "+PropertiesSystem.ESQUEMA+".AJUSTES_PRECONCILIACION SET NOBATCH  =?   WHERE ID = ?",parameters);		
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
		
	}
	
	
	
		
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
}

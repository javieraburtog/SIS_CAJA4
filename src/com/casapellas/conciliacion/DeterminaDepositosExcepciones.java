package com.casapellas.conciliacion;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.mail.internet.InternetAddress;

import com.casapellas.util.CustomEmailAddress;
import com.casapellas.util.MailHelper;
import com.casapellas.util.PropertiesSystem;

//import org.apache.commons.mail.MultiPartEmail;

public class DeterminaDepositosExcepciones {
	

//	static String propFileName = "c:\\CriteriosExcepciones.properties";
	public static String propFileName = "CriteriosExcepciones.properties";
	public static Properties prop = new Properties();
	public static String ESQUEMA ;
	public static String USUARIO ;
	public static String PASSWRD = "appcp1810";
	static Connection cn ;
	public static String msgError;
	public static String iCodigoUsuarioCrea ;
	public static String ipservermail;	
	public static String esquema_jde_com;
	
	public static String CLASSFORNAME;
	public static String URLSERVER;
	
	public DeterminaDepositosExcepciones() {

	}
 
	public static void evaluarExcepcionesBanco(){
		
		List<String> criteriosbanco = new ArrayList<String>();
		List<String> criteriosbancodescrip = new ArrayList<String>();
		
		try {
			msgError = "";
			
			String charComodinSeparacion = prop.getProperty("deposito.criterio.comodin");
			if(charComodinSeparacion == null)
				return;
			
			String[] descripcionParametros = {
					"deposito.banco.criterio.fechacreacion.descrip",
					"deposito.banco.criterio.montodeposito.descrip",
					"deposito.banco.criterio.referencia.descrip",
					"deposito.banco.criterio.comparaciones.descrip"
			};
			for (String propsBco : descripcionParametros) {
				if(prop.getProperty(propsBco) == null)
					continue;
				criteriosbancodescrip.add( prop.getProperty(propsBco) );
			}

			String[] nombrePropiedadBanco = new String[]{
					"deposito.banco.criterio.fechacreacion.valor",
					"deposito.banco.criterio.montodeposito.valor",
					"deposito.banco.criterio.referencia.valor",
					"deposito.banco.criterio.comparaciones.valor"
			};
			
			for (String propsBco : nombrePropiedadBanco) {
				if(prop.getProperty(propsBco) == null)
					continue;
				criteriosbanco.add( prop.getProperty(propsBco) );
			}
			if(criteriosbanco == null || criteriosbanco.isEmpty()){
				msgError = " no hay criterios para delimitar la consulta";
				return;
			}
			
			//&& ============ Crear la consulta que trae los depositos sometidos a excepciones.
			String strSqlInsertExcep =
				" INSERT INTO "+ESQUEMA+".PCD_EXCEPCION_DEPOSITO ( IDDEPOSITO, NUMEROREFERENCIA, TIPODEPOSITO," +
				" NUMEROCUENTA, CODIGOBANCO, MONTO, MONEDA, FECHADEPOSITO, TIPOSOLICITUD, DESCRIPCION, " +
				" EXCEPCIONVALIDA, USUARIOVALIDA, NUMEROCOMPARACIONES, FECHAULTIMACOMPARACION, FECHACREA, " +
				" USUARIOCREA, DIASTRANSCURRIDOS, MOTIVOEXCEPCION, OBSERVACION) "+
				"( @DTAFROMCONSOLIDADO ) " ;
			
			String sqlConsultaBanco = 
				" select  iddepbcodet, referenciaoriginal, 1, numerocuenta, codigobanco, montooriginal, moneda, fechadeposito," +
				" 'PRCD', descripcion, 0, @USUARIOCREA, cantidadcomparacion, date(fechamodconsolida),  current_timestamp,  @USUARIOCREA," +
				" timestampdiff (16, char( current timestamp - cast( fechadeposito as timestamp) )), '@MOTIVOEX',''  " +
				" FROM @ESQUEMA.PCD_CONSOLIDADO_DEPOSITOS_BANCO ";
			
			String strSqlUpdate = 
				" update @ESQUEMA.PCD_CONSOLIDADO_DEPOSITOS_BANCO " +
				" set estadoconfirmacion = 3, usuarioactualiza = @USUARIOCREA, " +
				" fechamodconsolida = current timestamp \n @CONDITION_WHERE ";

			String strSqlQueryExecute = "";
			String sqlWhereBanco = ""; 
			
			ResultSet rs1 = null;
			int rows = 0;
			
			String descripcionPr ;
			String criterioValor;
			for (int i = 0; i < criteriosbanco.size(); i++) {
			
				criterioValor = criteriosbanco.get(i);
				descripcionPr = criteriosbancodescrip.get(i);
				
				//&& =========== verificacion de los depositos
				sqlWhereBanco = " WHERE estadoconfirmacion = 0 \n and (\n "
								+ criterioValor.replace( charComodinSeparacion, " ") +"  \n) " ;
				
				strSqlQueryExecute = sqlConsultaBanco.replace("@USUARIOCREA", String.valueOf(iCodigoUsuarioCrea))
							.replace("@MOTIVOEX", descripcionPr).replace("@ESQUEMA", ESQUEMA) + sqlWhereBanco;
				
				rs1 = cn.createStatement().executeQuery(strSqlQueryExecute);
				if( !rs1.next() ) 
					continue;
				
				//&& =========== grabar los depositos en el consolidado
				rows = cn.createStatement().executeUpdate( strSqlInsertExcep.replace("@DTAFROMCONSOLIDADO", strSqlQueryExecute)) ;
				
				if(rows == 0){
					continue;
				}
				//&& =========== actualizar los estados y valores de los depositos
			    strSqlQueryExecute = strSqlUpdate.replace("@USUARIOCREA", String.valueOf(iCodigoUsuarioCrea))
						.replace("@ESQUEMA", ESQUEMA).replace("@CONDITION_WHERE", sqlWhereBanco ) ;
			    
			    rows =  cn.createStatement().executeUpdate( strSqlQueryExecute ) ;
				if(rows == 0){
					continue;
				}
			}
			
		} catch (Exception e) {
			msgError ="Error en metodo de evaluacion de depositos de banco "+ e;
			e.printStackTrace();
		} 
	}
	
	public static void evaluarExcepcionesCaja(){	
		List<String> criteriosCaja = new ArrayList<String>();
		List<String> criteriosCajaDescrip = new ArrayList<String>();
		
		try {
			msgError = "";
			
			String[] descripcionParametros = {
					"deposito.caja.criterio.fechacreacion.descrip",
					"deposito.caja.criterio.montodeposito.descrip",
					"deposito.caja.criterio.referencia.descrip",
					"deposito.caja.criterio.comparaciones.descrip"
			};
			for (String propsCaja : descripcionParametros) {
				if(prop.getProperty(propsCaja) == null)
					continue;
				criteriosCajaDescrip.add( prop.getProperty(propsCaja) );
			}
			
			String charComodinSeparacion = prop.getProperty("deposito.criterio.comodin");
			if(charComodinSeparacion == null)
				return;
			
			String[] nombrePropiedadCaja = new String[]{
					"deposito.caja.criterio.fechacreacion.valor",
					"deposito.caja.criterio.montodeposito.valor",
					"deposito.caja.criterio.referencia.valor",
			};
			for (String propsCaja : nombrePropiedadCaja) {
				if(prop.getProperty(propsCaja) == null)
					continue;
				criteriosCaja.add( prop.getProperty(propsCaja) );
			}

			if(criteriosCaja == null || criteriosCaja.isEmpty()){
				msgError = " no hay criterios para delimitar la consulta en depositos de caja ";
				return;
			}
			
			//&& ===================== Cargar datos que aplican los criterios para que sea excepcion
			String mpagoDesc =  "ifnull( ( select trim(drdl01)ss from "+esquema_jde_com+".F0005 F0005 WHERE F0005.DRSY = '00 ' " +
					"AND F0005.DRRT = 'RY' and CAST(TRIM(F0005.DRKY) AS VARCHAR(10) CCSID 37) = @CODIGO_MPAGO  ) , mpagodep )"; 
			
			String strSqlSelectQuery = 
			" select  consecutivo, referencenumber, 2,  \n " +
			" ifnull( (select d3nocuenta from "+ESQUEMA+".f55ca023 where d3codb = idbanco fetch first rows only ), 0 ) nocuenta, \n " +
			" idbanco, monto,moneda, fecha,'PRCD',  \n" +
			"'Caja: ' ||  RIGHT(REPEAT('0',3) || caid, 3) || ', Compania: ' || trim(codcomp)|| ', Pago: (' || " + mpagoDesc.replace("@CODIGO_MPAGO", "mpagodep") + " || '), Contador: ' || lower(trim(coduser)),  \n" +
			" 0,  "+iCodigoUsuarioCrea+", 0, date(fechamod), current_timestamp,  "+iCodigoUsuarioCrea+",  \n " +
			" timestampdiff (16, char( current timestamp - cast( fecha as timestamp ) )), \n" +
			" '@MOTIVOEX','' "+
			" from "+ESQUEMA+".DEPOSITO  \n" ; 
			
			String strSqlQueryWhere = " where depctatran = 1 and mpagodep <> 'X' " +
					" and monto > 0  and estadocnfr = 'SCR' and tipodep = 'D' " +
					" and @CRITERIO_FILTRO_CAJA ";
			
			String strSqlInsertQuery = 
					" INSERT INTO "+ESQUEMA+".PCD_EXCEPCION_DEPOSITO ( IDDEPOSITO, NUMEROREFERENCIA, TIPODEPOSITO," +
					" NUMEROCUENTA, CODIGOBANCO, MONTO, MONEDA, FECHADEPOSITO, TIPOSOLICITUD, DESCRIPCION, " +
					" EXCEPCIONVALIDA, USUARIOVALIDA, NUMEROCOMPARACIONES, FECHAULTIMACOMPARACION, FECHACREA, " +
					" USUARIOCREA, DIASTRANSCURRIDOS, MOTIVOEXCEPCION, OBSERVACION ) "+
					"( @VALUES_FROM_SELECT ) " ;
			
			String strSqUpdateQuery =
				" UPDATE "+ESQUEMA+".DEPOSITO set" +
				" tipoconfr = 'DSE', estadocnfr = 'CFR', usrconfr = "+iCodigoUsuarioCrea +
				", fechamod = current date, horamod = current time \n @QUERY_WHERE " ;
					
			
			int rows = 0 ;
			String strSqlQueryExecute = "";
			ResultSet rs ;
			String criterioValor;
			String descripcionPr;
			
			for (int i = 0; i < criteriosCaja.size(); i++) {
				
				criterioValor = criteriosCaja.get(i);
				descripcionPr = criteriosCajaDescrip.get(i);
			
				strSqlQueryExecute = strSqlSelectQuery.replace("@MOTIVOEX", descripcionPr) + strSqlQueryWhere.replace("@CRITERIO_FILTRO_CAJA",
										criterioValor.replace(charComodinSeparacion, " "));
				rs = cn.createStatement().executeQuery(strSqlQueryExecute);
 
				if( !rs.next() ) continue;
				
				//&& =========== grabar los depositos en el consolidado
				rows = cn.createStatement().executeUpdate( strSqlInsertQuery.replace("@VALUES_FROM_SELECT", strSqlQueryExecute) ) ;
				
				if(rows == 0){
					continue;
				}
				
				rows = cn.createStatement().executeUpdate(
						strSqUpdateQuery.replace("@QUERY_WHERE", 
								strSqlQueryWhere.replace("@CRITERIO_FILTRO_CAJA",
								criterioValor.replace(charComodinSeparacion, " ")) )
							);
				
				if(rows == 0){
					continue;
				}	
			}
			
		} catch (Exception e) {
			msgError ="Error en metodo de evaluacion de depositos de banco "+ e;
			e.printStackTrace();
		}
	}
	public static void enviarCorreosNotificacion(){
		ResultSet rs = null;
		String[] tipodeposito = {"1","2"} ;
		String[] strTipodeposito = {"Banco","Caja"} ;
		List<StringBuilder> mailContent = new ArrayList<StringBuilder>();
		
		try {
			
//			String padding = " padding: 3px 10px; ";
			String font = " font-family: Arial,Helvetica, sans-serif;font-size: 11px;color: #1a1a1a; ";
			String aLeft = " text-align:left;";
			String aRight = " text-align:right;";
			String aCenter = " text-align:center;";
			String border= " border: 1px dashed silver; ";
			String cssTitulo = "text-align: center; background-color: #5e89b5; color: white; font-family: Arial,Helvetica,sans-serif; font-size: 12px;" ;
			
			StringBuilder tabla = new StringBuilder();		
			
			tabla.append( "<div style =\"margin: 0px auto; width: 85%;\" >") 
				.append( "<table cellpadding=\"2\"  style = \"width: auto; margin-top: 15px; "+ font +"\"> ")
					.append( "<tr>" )
						.append("<th style= \" ").append(cssTitulo).append(" \" >No</th> " ) 
						.append("<th style= \" ").append(cssTitulo).append(" \" >Monto</th> " ) 
						.append("<th style= \" ").append(cssTitulo).append(" \" >Moneda</th> " ) 
						.append("<th style= \" ").append(cssTitulo).append(" \" >Referencia</th> " ) 
						.append("<th style= \" ").append(cssTitulo).append(" \" >Banco</th> " ) 
						.append("<th style= \" ").append(cssTitulo).append(" \" >Cuenta</th> " ) 
						.append("<th style= \" ").append(cssTitulo).append(" \" >Fecha</th> " ) 
						.append("<th style= \" ").append(cssTitulo).append(" \" >Evaluado</th> " ) 
						.append("<th style= \" ").append(cssTitulo).append(" \" >Motivo</th> " ) 
						.append("<th style= \" ").append(cssTitulo).append(" \" >Descripción</th> " ) 
					.append("</tr> \n" )
					.append("@TABLE_BODY")
					.append(" \n</table>")
			.append("</div>");
			
			String excepcionesCreadas = 
				" select monto, moneda, numeroreferencia, " +
				"  ifnull( (select lower(banco) from "+ESQUEMA+".F55CA022 where codb = codigobanco ), cast(codigobanco as varchar(20) ) ), " +
				" numerocuenta, fechadeposito, numerocomparaciones, motivoexcepcion, descripcion, " +
				" ROW_NUMBER() OVER(order by idexcepciondeposito) " +
				" from "+ESQUEMA+".PCD_EXCEPCION_DEPOSITO " +
				" where DATE(fechacrea) = current_date and usuariocrea = " +iCodigoUsuarioCrea +
				" and tipodeposito = @TIPO_DEPOSITO "+
//				" fetch first 10 rows only " ;
				" order by fechadeposito asc";

			for (int i = 0; i < tipodeposito.length; i++) {
				
				
				rs = cn.createStatement().executeQuery( excepcionesCreadas.replace("@TIPO_DEPOSITO", tipodeposito[i]) ) ;
				
				StringBuilder sb = new StringBuilder("");
				while (rs.next()) {
					sb.append( "<tr>" )
					.append("<td style= \" ").append(aLeft  ).append(border).append(" \" >").append( (rs.getString(10))).append("</td>")
					.append("<td style= \" ").append(aRight ).append( border ).append(" \" >").append( String.format("%1$,.2f", rs.getBigDecimal(1) ) ).append("</td>")
					.append("<td style= \" ").append(aCenter).append(border).append(" \" >").append(rs.getString(2)).append("</td>") 
					.append("<td style= \" ").append(aRight ).append(border).append(" \" >").append( String.format("%0"+ (9-rs.getString(3).length()) +"d%s", 0, rs.getString(3)) ).append("</td>") 
					.append("<td style= \" ").append(aRight ).append(border).append(" \" >").append(rs.getString(4)).append("</td>") 
					.append("<td style= \" ").append(aRight ).append(border).append(" \" >").append(rs.getString(5)).append("</td>") 
					.append("<td style= \" ").append(aCenter).append(border).append(" \" >").append(rs.getString(6)).append("</td>") 
					.append("<td style= \" ").append(aRight ).append(border).append(" \" >").append(rs.getString(7)).append("</td>") 
					.append("<td style= \" ").append(aLeft  ).append(border).append(" \" >").append(rs.getString(8)).append("</td>") 
					.append("<td style= \" ").append(aLeft  ).append(border).append(" \" >").append(rs.getString(9)).append("</td>") 
					.append("</tr>");
				}
			
				if(sb.toString().isEmpty())
					continue;

				mailContent.add( new StringBuilder(  tabla.toString().replace("@TABLE_BODY", sb.toString()))); 
			}
			
			String comodinCuenta = prop.getProperty("depositos.notificacion.comodincuenta");
			String comodinDatos  = prop.getProperty("depositos.notificacion.comodindatos");
			String cuentasCorreo = prop.getProperty("depositos.notificacion.cuentacorreos");
			
			for (int i = 0; i < mailContent.size(); i++) {
				
				StringBuilder mcont = mailContent.get(i);
				
				List<CustomEmailAddress> toList = new ArrayList<CustomEmailAddress>();						
				for (String cuentacorreo : cuentasCorreo.split(comodinCuenta)) {
					toList.add(new CustomEmailAddress(cuentacorreo.split(comodinDatos)[1],cuentacorreo.split(comodinDatos)[0]));
				}
				
				MailHelper.SendHtmlEmail(
						new CustomEmailAddress(PropertiesSystem.WEBMASTER_EMAIL_ADRESS, "Módulo de Caja: Confirmación depósitos"),
						toList, null, new CustomEmailAddress(PropertiesSystem.MAIL_BOUNCEADDRESS), 
						"Sugerencia de Excepciones por depósitos de "+strTipodeposito[i]+" no confirmados.", mcont.toString());
			
				/*
				MultiPartEmail email = new MultiPartEmail();
				email.setHostName(ipservermail);
				email.setFrom("webmaster@casapellas.com.ni","Módulo de Caja: Confirmación depósitos");
				email.setBounceAddress("lfonseca@casapellas.com.ni");
				email.setSubject("Sugerencia de Excepciones por depósitos de "+strTipodeposito[i]+" no confirmados.");
				email.addPart( mcont.toString(), "text/html");
				
				for (String cuentacorreo : cuentasCorreo.split(comodinCuenta)) {
					email.setTo( Arrays.asList( new InternetAddress( cuentacorreo.split(comodinDatos)[1], 
								cuentacorreo.split(comodinDatos)[0])));
					try{
						email.send();
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				*/
			}

		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}
	
	public static void main(String[] args) {
		
		try {
			
			loadProperties();
			
			if(prop == null)
				return;
			
			retrieveGenericsProperties();
			
			if((cn = getConnection()) == null){
				msgError = "No se pudo crear conexion con base de datos" ;
				return;
			}
			
			evaluarExcepcionesBanco();
			evaluarExcepcionesCaja();
			enviarCorreosNotificacion();
		
			
		} catch (Exception e) {
			msgError = "Error en el main "+e.getMessage();
			e.printStackTrace();
		}finally{
			
			try {
				if (cn == null || cn.isClosed())
					return;
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				cn.commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				cn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			cn = null;
		}
	}

	public static void loadProperties(){
		try {
			
//			InputStream input = new FileInputStream("/"+propFileName);
//			prop.load(input);
//			prop.load( DeterminaDepositosExcepciones.class.getResourceAsStream("CriteriosExcepciones.properties") ) ;
			
			
			prop.load( DeterminaDepositosExcepciones.class
					.getResourceAsStream("/com/casapellas/conciliacion/CriteriosExcepciones.properties") ) ;
		
			
			
			
		} catch (Exception e) {
			prop = null;
			msgError = "Error al cargar properties de valores de Criterios ";
			e.printStackTrace();
		}
	}
	public static void retrieveGenericsProperties(){
		try {
			iCodigoUsuarioCrea =  prop.getProperty("depositos.notificacion.usuario.codigo" );
			ipservermail = prop.getProperty("depositos.notificacion.ipservermail"); 
			ESQUEMA = prop.getProperty("depositos.notificacion.esquema.caja");
			USUARIO = prop.getProperty("depositos.notificacion.usuario.id") ;
			esquema_jde_com = prop.getProperty("depositos.notificacion.esquema.jde.com") ;
			URLSERVER  = prop.getProperty("depositos.notificacion.ur_server_as400") ;
			CLASSFORNAME = prop.getProperty("depositos.notificacion.class_for_name") ;
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    public static Connection getConnection(){
        try {
            Class.forName( CLASSFORNAME );
            cn = DriverManager.getConnection( URLSERVER, USUARIO, PASSWRD); 
        }catch(Exception e){
        	e.printStackTrace();
        	cn = null;
        }
        return cn;
    }
	 
	 
	
}

package com.casapellas.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.casapellas.entidades.ens.Vautoriz;


public class BitacoraSecuenciaReciboService {
	private static Connection Create() {
		try {
			Class.forName("com.ibm.as400.access.AS400JDBCDriver");

			return DriverManager.getConnection("jdbc:as400://" + PropertiesSystem.IPSERVERDB2 + "/" + PropertiesSystem.ESQUEMA + ";prompt=false;translate binary=true",
					PropertiesSystem.CN_USRNAME, PropertiesSystem.CN_USRPWD);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static void ExecuteCommand(String strQuery) {
		Connection connection = null;
		
		try {
			connection = Create(); 
			Statement stmt = connection.createStatement();

			stmt.executeUpdate(strQuery);

			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null && !connection.isClosed()) {
					connection.close();	
				}
			} catch (Exception e) { //SQLException
				System.out.println("Hubo una excepcion al intentar cerrar la conexion nativa: " + e.getMessage());
			}
		}
	}
	
	@SuppressWarnings("unused")
	private static Integer CastDateToNumber(Date dt, String format) {
		String strDate = CastDateToString(dt, format);

		return Integer.parseInt(strDate);
	}

	private static String CastDateToString(Date dt, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);

		return formatter.format(dt);
	}

	@SuppressWarnings("unused")
	private static Integer CastTimeToNumber(Date dt) {
		SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
		return Integer.parseInt(sdf.format(dt));
	}
	
	private static String CastTimeToString(Date dt) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH.mm.ss");
		return sdf.format(dt);
	}

	public static void insertarLogReciboNumerico(int noCaja,int noRecibo,String codCompania,String codSucursal,String tipoRecibo) {
		try {
			Object objSec = CodeUtil.getFromSessionMap("sevAut");
			Vautoriz vaut = ((Vautoriz[]) objSec)[0];
			String usuario = vaut != null && vaut.getId() != null && vaut.getId().getLogin() != null ? vaut.getId().getLogin() : "";
			
			Date dt = new Date();
			String strFecha = CastDateToString(dt, "yyyy-MM-dd");
			String strHora = CastTimeToString(dt);

			String insertQuery= "INSERT INTO "+ PropertiesSystem.ESQUEMA +".BITACORA_SECUENCIA_RECIBO"
					+ "(NO_CAJA,NO_RECIBO,COD_COMPANIA,TIPO_RECIBO,COD_SUCURSAL,COD_USUARIO,FECHA_CREACION,HORA_CREACION,MOTIVO,ESTADO)"
					+ " VALUES("+
					noCaja+","+noRecibo+",'"+codCompania+"','"+tipoRecibo+"','"+codSucursal+"','"+usuario+"','" + strFecha + "','" + strHora + "',"+
					"'ERROR','ERROR DEL SISTEMA' )";
			
			LogCajaService.CreateLog("insertarLogReciboNumerico", "QRY", insertQuery);
			
			ExecuteCommand(insertQuery);
		} catch (Exception e) {
			e.printStackTrace();
			LogCajaService.CreateLog("insertarLogReciboNumerico", "ERR", e.getMessage());
		}
	}
	
	public static void actualizarSatisfactorioLogReciboNumerico(int noCaja,int noRecibo,String codCompania,String codSucursal,String tipoRecibo) {
		try {
			Date dt = new Date();
			String strFecha = CastDateToString(dt, "yyyy-MM-dd");
			String strHora = CastTimeToString(dt);
			
			String updateQuery= "UPDATE  "+ PropertiesSystem.ESQUEMA +".BITACORA_SECUENCIA_RECIBO " + 
					"SET " + 
					"FECHA_MODIFICACION = '" + strFecha + "'," + 
					"HORA_MODIFICACION = '" + strHora +  "'," + 
					"ESTADO='SATISFACTORIO'," + 
					"MOTIVO = 'SATISFACTORIO' " + 
					"WHERE " + 
					"NO_CAJA = "+noCaja+" AND " + 
					"NO_RECIBO = "+noRecibo+" AND " + 
					"COD_COMPANIA = '"+codCompania+"' AND " + 
					"TIPO_RECIBO = '"+tipoRecibo+"'";
			
			LogCajaService.CreateLog("actualizarSatisfactorioLogReciboNumerico", "QRY", updateQuery);
			ExecuteCommand(updateQuery);
		} catch (Exception e) {
			e.printStackTrace();
			LogCajaService.CreateLog("actualizarSatisfactorioLogReciboNumerico", "ERR", e.getMessage());
		}
	}
}

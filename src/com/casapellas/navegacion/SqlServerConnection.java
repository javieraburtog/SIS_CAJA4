package com.casapellas.navegacion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import javax.faces.context.FacesContext;

public class SqlServerConnection {
	public Connection getConnection(String sUsuario,String sClave, String sBd) {
		String sServer = "192.168.1.120";	
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		Connection cn = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");  		    
			String connectionUrl = "jdbc:sqlserver://"+sServer+":1433;databaseName="+sBd+";user="+sUsuario+";password="+sClave+";";
			cn = DriverManager.getConnection(connectionUrl);
		}catch(SQLException e){	
			System.out.print("Se capturo esta excepcion:  " + e.getErrorCode()  + "::::"+ e.getSQLState() + "\n");
			System.out.print("Se capturo esta excepcion:  " + e);
			 e.printStackTrace();
			m.put("sqlState", e.getSQLState());
			m.put("msgConnection",e.getMessage().trim());			
		} catch (ClassNotFoundException e) {
			
		}	
		return cn;
	}
}

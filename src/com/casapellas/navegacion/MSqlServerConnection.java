package com.casapellas.navegacion;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class MSqlServerConnection {

	public Connection getConnection(String sUsuario,String sClave) {
		String sServer = "192.168.1.252";	
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		Connection cnMSqlServer = null;
		try {			
		    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
		    cnMSqlServer = DriverManager.getConnection("jdbc:sqlserver://" + sServer + ";integratedSecurity=true;");
		}catch(SQLException e){	
			System.out.print("Se capturo esta excepcion:  " + e.getErrorCode()  + "::::"+ e.getSQLState());
			m.put("sqlStateMSqlServer", e.getSQLState());
			m.put("msgConnectionMSqlServer",e.getMessage().trim());
		}catch(InstantiationException ie){
			
		} catch (IllegalAccessException e) {
			
		} catch (ClassNotFoundException e) {
			
		}	
		return cnMSqlServer;
	}
	
	public static Connection getJNDIConnection(String sDataSource){
	    String DATASOURCE_CONTEXT = sDataSource;
	    
	    Connection result = null;
	    try {
	      Context initialContext = new InitialContext();
	      if ( initialContext == null){
	        log("JNDI problem. Cannot get InitialContext.");
	      }
	      DataSource datasource = (DataSource)initialContext.lookup(DATASOURCE_CONTEXT);
	      if (datasource != null) {
	        result = datasource.getConnection();
	      }
	      else {
	        log("Failed to lookup datasource.");
	      }
	    }
	    catch ( NamingException ex ) {
	      log("Cannot get connection: " + ex);
	    }
	    catch(SQLException ex){
	      log("Cannot get connection: " + ex);
	    }
	    return result;
	  }
	
	private static void log(Object aObject){
	    //System.out.println(aObject);
	  }
}

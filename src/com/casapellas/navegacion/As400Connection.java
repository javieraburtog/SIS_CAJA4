package com.casapellas.navegacion;


import java.lang.String;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.sql.*;
import javax.sql.*;

import com.casapellas.util.AllConectionMngt;
import com.casapellas.util.PropertiesSystem;



public class As400Connection {

	public final static String classForNameAs400 = "com.ibm.as400.access.AS400JDBCDriver" ;
	
	
	public static Connection getConnectionForBatchNumber(){
    	Connection cn = null;
        try {
        	
            Class.forName( classForNameAs400 );
           
            String URLSERVER = "jdbc:as400://@SERVERIP; libraries = @INV, @JDECOM; prompt=false ";
    		URLSERVER = URLSERVER
    				.replace("@SERVERIP", PropertiesSystem.IPSERVERDB2)
    				.replace("@JDECOM", PropertiesSystem.JDECOM)
    				.replace("@INV", PropertiesSystem.INV) ;
    		
			cn =  DriverManager.getConnection(URLSERVER,  PropertiesSystem.CN_USRNAME, PropertiesSystem.CN_USRPWD);
            
        }catch(Exception e){
        	cn = null;
        	e.printStackTrace();
        }
        return cn;
    }
	
	public static String getInfoAS400JOB(Connection cnAs400){
		String dtaConnection = "";
		try {
			
			java.sql.Statement stmt = cnAs400.createStatement();

			 stmt.executeUpdate("CALL QSYS.QCMDEXC('QSYS/DSPJOBLOG OUTPUT(*OUTFILE)" +
			 		" OUTFILE(QTEMP/QAUGDBJOBN) OUTMBR(*FIRST *REPLACE)',0000000081.00000)");

			 java.sql.ResultSet rs = stmt.executeQuery ("SELECT SUBSTRING(QMHJOB,1,10) AS JOBNAME, " +
			 		"SUBSTRING(QMHJOB,11,10) AS USERNAME, " +
			 		"CAST(SUBSTRING(QMHMDT,0,10)  AS VARCHAR(10) CCSID 37) AS USERACTUAL," + 
			 		"SUBSTRING(QMHJOB,20,7) AS JOBNUM " +
			 		"FROM QTEMP.QAUGDBJOBN WHERE QMHMID = 'CPIAD02'");

			 rs.next();
			 
			dtaConnection = "JOBNAME: '" + rs.getString("JOBNAME") + " || "
					+ "USERNAME: '" + rs.getString("USERNAME") + "' || "
					+ "USUARIO: '" + rs.getString("USERACTUAL") + "' || "
					+ "JOBNUMBER: '" + rs.getString("JOBNUM") + "'";
			 
			 rs = null;
			 stmt = null;
			 
		} catch (Exception e) {
			e.printStackTrace();
			dtaConnection = "" ;
		}
		return dtaConnection;
	}
	
	
	@SuppressWarnings("unchecked")
	public Connection getConnection(String sUsuario, String sClave) {
		String sServer = PropertiesSystem.IPSERVERDB2;
		Map m = FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap();
		Connection cnAs400 = null;
		try {
			Class.forName(classForNameAs400).newInstance();
			cnAs400 = DriverManager.getConnection("jdbc:as400://" + sServer	+ ";prompt=false", sUsuario, sClave);

		} catch (SQLException e) {
			e.printStackTrace();
			
			m.put("sqlState", e.getSQLState());
			m.put("msgConnection", e.getMessage().trim());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cnAs400;
	}
	public static Connection getJNDIConnection(){
		Connection cn = null;
		try {
			AllConectionMngt connectionDb2 = new AllConectionMngt();
			cn = connectionDb2.getCustomDriverConnection(PropertiesSystem.CN_USRNAME, 
											PropertiesSystem.CN_USRPWD);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cn;
	}
	
	public static Connection getJNDIConnection(String uno){
		Connection cn = null;
		try {
			AllConectionMngt connectionDb2 = new AllConectionMngt();
			cn = connectionDb2.getCustomDriverConnection(PropertiesSystem.CN_USRNAME,  PropertiesSystem.CN_USRPWD);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cn;
	}
	
	public static Connection getJNDIConnection1(String sDataSource){
	    String DATASOURCE_CONTEXT = sDataSource;
	    Context initialContext = null;
	    Connection result = null;
	    try {
	    	
	      initialContext = new InitialContext();
	      
			DataSource datasource = (DataSource) initialContext
					.lookup(DATASOURCE_CONTEXT);
			if (datasource != null) {
				result = datasource.getConnection();
			} else {
				log("Failed to lookup datasource.");
			}
	    }
	    catch( Exception ex){
	      log("Cannot get connection: " + ex);
	    }
	    return result;
	  }
	
	private static void log(Object aObject){
	    System.out.println(aObject);
	  }
	
}

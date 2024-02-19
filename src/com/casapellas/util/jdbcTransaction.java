package com.casapellas.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.ibm.ws.batch.xJCL.beans.resultsAlgorithm;


public class jdbcTransaction {
	private java.sql.Connection  cnx = null ;	
	
	public void openConnection(){
		String driver = "com.ibm.as400.access.AS400JDBCDriver";
		String url = "jdbc:as400:192.168.1.3;prompt=false";   		
		try {
			Class.forName(driver);			
			setCnx(java.sql.DriverManager.getConnection(url, PropertiesSystem.CN_USRNAME, PropertiesSystem.CN_USRPWD));
			cnx.setAutoCommit(false);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public void closeConnection(){
		try {
			getCnx().close();
		} catch (Exception e2) {}	
	}  
	
	public int EjecutarTransaccion(String queryString,String parameters[][]) throws Exception{
		 PreparedStatement ppe = null;
		 SimpleDateFormat sdfF = new SimpleDateFormat("yyyy-MM-dd");
		 int hecho =0 ;
		try {
			
			ppe = getCnx().prepareStatement(queryString);
			for (int i = 0; i < parameters.length; i++) {
				if(String.valueOf(parameters[i][1]).compareToIgnoreCase("0")==0){					
					ppe.setString(i+1, parameters[i][0]);
				}else if(String.valueOf(parameters[i][1]).compareToIgnoreCase("1")==0){
					ppe.setDouble(i+1, Double.parseDouble(parameters[i][0]));					
				}else if(String.valueOf(parameters[i][1]).compareToIgnoreCase("2")==0){
					Calendar cal = Calendar.getInstance();  
					cal.setTime(sdfF.parse(parameters[i][0]));
					ppe.setDate(i+1, new java.sql.Date(cal.getTimeInMillis()));					
				}
				  
			}  
			 hecho = ppe.executeUpdate();	  		
			
			ppe.close();  
		} catch (Exception e) {   
			e.printStackTrace();
			 throw new Exception();  
		}
		return hecho;	
	}
	
	public String[]getData(String SqlQuery,String parameters[][]){
		 PreparedStatement ppe = null;
		 ResultSet rs = null;
		 SimpleDateFormat sdfF = new SimpleDateFormat("yyyy-MM-dd");
		 String Results [] = new String [16];
		try {
			
			ppe = getCnx().prepareStatement(SqlQuery,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);	  
			
			for (int i = 0; i < parameters.length; i++) {
				if(String.valueOf(parameters[i][1]).compareToIgnoreCase("0")==0){					
					ppe.setString(i+1, parameters[i][0]);
				}else if(String.valueOf(parameters[i][1]).compareToIgnoreCase("1")==0){
					ppe.setDouble(i+1, Double.parseDouble(parameters[i][0]));					
				}else if(String.valueOf(parameters[i][1]).compareToIgnoreCase("2")==0){
					Calendar cal = Calendar.getInstance();  
					cal.setTime(sdfF.parse(parameters[i][0]));
					ppe.setDate(i+1, new java.sql.Date(cal.getTimeInMillis()));					
				}	    			  
			} 
			rs = ppe.executeQuery();	
			rs.first();  
			
			for(int i=0;i<Results.length;i++){
				Results[i] = rs.getString(i+1);
			}
			
			
			ppe.close();
		} catch (Exception e) {  
			e.printStackTrace();
		}
		return Results;
	}
	
	public String[]getDataRe(String SqlQuery,String parameters[][]){
		 PreparedStatement ppe = null;
		 ResultSet rs = null;
		 SimpleDateFormat sdfF = new SimpleDateFormat("yyyy-MM-dd");
		 String Results [] = new String [18];
		try {
			
			ppe = getCnx().prepareStatement(SqlQuery,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);	  
			
			for (int i = 0; i < parameters.length; i++) {
				if(String.valueOf(parameters[i][1]).compareToIgnoreCase("0")==0){					
					ppe.setString(i+1, parameters[i][0]);
				}else if(String.valueOf(parameters[i][1]).compareToIgnoreCase("1")==0){
					ppe.setDouble(i+1, Double.parseDouble(parameters[i][0]));					
				}else if(String.valueOf(parameters[i][1]).compareToIgnoreCase("2")==0){
					Calendar cal = Calendar.getInstance();  
					cal.setTime(sdfF.parse(parameters[i][0]));
					ppe.setDate(i+1, new java.sql.Date(cal.getTimeInMillis()));					
				}	    			  
			} 
			rs = ppe.executeQuery();	
			rs.first();  
			
			for(int i=0;i<Results.length;i++){
				Results[i] = rs.getString(i+1);
			}
			
			
			ppe.close();
		} catch (Exception e) {  
			e.printStackTrace();
		}
		return Results;
	}

	public java.sql.Connection getCnx() {
		return cnx;
	}

	public void setCnx(java.sql.Connection cnx) {
		this.cnx = cnx;
	}
	
	
	
	
}

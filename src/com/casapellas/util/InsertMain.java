/**
 * 
 */
package com.casapellas.util;

import java.sql.*;
import java.util.*;

import com.casapellas.conciliacion.LecturaConciliacion;

/**
 * @author CarlosHernandez
 *
 */
public class InsertMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		insert();
		

	}
	
	public static void insert() {
		List<Integer> caids = new ArrayList<Integer>();
		
		 try {
			 
			 
			 Class.forName(LecturaConciliacion.CLASSFORNAME);
			 Connection cn = DriverManager.getConnection(LecturaConciliacion.URLSERVER, LecturaConciliacion.USUARIO, LecturaConciliacion.PASSWRD); 
			 
			 String queryInsert =
		 		"insert into gcpmcaja.pcd_cajas_preconciliar ( caid, codcomp, codigobanco, moneda,estado, usrcrea, usrmod  ) " + 
		 		"select  @CODIGOCAJA, codcomp, codigobanco, moneda,estado, usrcrea, usrmod  from gcpmcaja.pcd_cajas_preconciliar where estado = 1 and caid  = 8";
			 
			 
			String strCaid = "47, 49, 63, 64, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 80, 84, 85, 86, 97, 114, 115, 116, 134, 135, 136, 137, 138, 139, 140, 142, 172, 173, 174, 192, 193, 194, 195"; 
			 
			 StringTokenizer st = new StringTokenizer(strCaid, ",");
		     while (st.hasMoreTokens()) {
		    	 
		    	 String statement = queryInsert.replace("@CODIGOCAJA", st.nextToken() ) ;		    	 
		         
		         PreparedStatement ps = cn.prepareStatement(statement);
		         int rs = ps.executeUpdate();
		         
		         System.out.println("Insert: " +rs+  " > on Execute :" + statement );
		         
		         ps.close();
		         
		     }
		     
			cn.commit();
			cn.close();
			cn = null;
			
			
        }
        catch (Exception ex) {
	       ex.printStackTrace();
        }
		
		
		
	}

}

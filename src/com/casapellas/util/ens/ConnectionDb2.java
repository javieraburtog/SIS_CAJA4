/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.casapellas.util.ens;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Francisco Landeros
 */
public class ConnectionDb2 {
    
    public static boolean DEBUG_MODE = true;
    
    //private String errConection;
    private String sqlState;
    private String messageConection;;
    
    public ConnectionDb2(){}
    
    public boolean validateLogin(String user, String password){
        boolean bReturn = false;
        Connection cnAs400 = null;
        try {            
            cnAs400 = getConnection(user, password);
            if(cnAs400 != null){
                bReturn = true;
            }            
        } catch (Exception ex) {            
            ex.printStackTrace();
        } finally {
            try {
                closeConnection(cnAs400);
            } catch (SQLException ex) {
                Logger.getLogger(ConnectionDb2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return bReturn;
    }
    
    public Connection getConnection(String user, String password) throws Exception{
        Connection conn = null;
        try {
//            Class.forName(LoadProperties.queryProps.getProperty("crp.jdbc.driver.db2"));
//            conn = DriverManager.getConnection(LoadProperties.queryProps.getProperty("crp.jdbc.conection.url"), user, password);
        	
            Class.forName(LoadProperties.queryProps.getProperty("jdbc.driver.db2"));
            conn = DriverManager.getConnection(LoadProperties.queryProps.getProperty("jdbc.conection.url"), user, password);
        	
        	
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new Exception(e);
        } catch (SQLException e) {  
            sqlState = e.getSQLState();
            messageConection = e.getMessage().trim();
            throw new Exception(e);
        }
        return conn;
    }
    
    public String getMessageError(){
        String strReturn = "";
        
        if(this.messageConection.equals("The application server rejected the connection. (Password is expired.)")){
            strReturn = "REDIRECT_TO_CHANGE_PASSWORD";
        } else if (this.messageConection.equals("The application server rejected the connection. (User ID is not known.)")) {
            strReturn = "Credenciales incorrectas";
        } else if (this.messageConection.equals("The application server rejected the connection. (User ID is disabled.)")) {
            strReturn = "Usuario deshabilitado, consulte a informatica";
        } else if (this.messageConection.equals("The application server rejected the connection. (Password length is not valid.)")) {
            strReturn = "Credenciales incorrectas";
        } else if (this.messageConection.equals("The application server rejected the connection. (Password is incorrect.)")) {
            strReturn = "Credenciales incorrectas";
        } else if (this.messageConection.equals("")) {
        	strReturn = "Usuario o contraseña inválida";
        } else {
        	strReturn = "Usuario o contraseña inválido";
        }
        return strReturn;
    }
    
    private static void closeConnection(Connection conn) throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
    
    public void printLog(String log){
        if(DEBUG_MODE){
            System.out.println(log);
        }
    }

    /**
     * @return the sqlState
     */
    public String getSqlState() {
        return sqlState;
    }

    /**
     * @param sqlState the sqlState to set
     */
    public void setSqlState(String sqlState) {
        this.sqlState = sqlState;
    }

    /**
     * @return the messageConection
     */
    public String getMessageConection() {
        return messageConection;
    }

    /**
     * @param messageConection the messageConection to set
     */
    public void setMessageConection(String messageConection) {
        this.messageConection = messageConection;
    }
}

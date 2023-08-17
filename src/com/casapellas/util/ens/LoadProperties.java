package com.casapellas.util.ens;


import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoadProperties {
    
    public static final Properties queryProps = new Properties();
    
    static {
        try { 
	
        	queryProps.load( ENSUtilValidation.class.getClassLoader().getResourceAsStream("PropertiesSystemGcp.properties")); 
        	
        } catch (Exception ex) {
            Logger.getLogger(ENSUtilValidation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

package com.casapellas.hibernate.util;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import com.casapellas.util.LogCajaService;

public class HibernateUtilPruebaCn {


	
	//&& ===== Conexiones con GCPMCAJA
	public static final ThreadLocal<Session> MAP = new ThreadLocal<Session>();
	private static ServiceRegistry serviceRegistryPruebaCn;
	private static SessionFactory sessionFactoryPruebaCn = buildSessionFactoryPruebaCn();
	
	/** Abrir Conexion con la base de Datos de GCPMCAJA */
	public static Session currentSession() {
		Session session = (Session)MAP.get();
		
		if (session == null || !session.isOpen()) {
			if (sessionFactoryPruebaCn == null) {
				buildSessionFactoryPruebaCn();
			}
			session = (sessionFactoryPruebaCn != null) ? sessionFactoryPruebaCn.openSession() : null;
			MAP.set(session);
		}
		
		return session;
	}
	
	/** Cerrar Conexion con la base de Datos de GCPMCAJA */
	public static void closeSession(Session s){
		try {
			MAP.set(null);
			if (s != null && s.isOpen()){
				s.close();	
			}			
				
		} catch (Exception e) { 
			LogCajaService.CreateLog("HibernateUtilPruebaCn-CierreSession", "ERR", e.getMessage());
		}
    }
	
	public static void closeSession() {
		try {
			Session s = currentSession();//Obtener la session actual
			MAP.set(null);
			if (s != null && s.isOpen()){
				s.close();	
			}			
				
		} catch (Exception e) { 
			LogCajaService.CreateLog("HibernateUtilPruebaCn-CierreSession", "ERR", e.getMessage());
		}
	}
	
	@SuppressWarnings("deprecation")
	private static SessionFactory buildSessionFactoryPruebaCn() {
		try {
			Configuration configuration = new Configuration();
		
    	    configuration.configure("/hibernatePruebaCn.cfg.xml");
    	    sessionFactoryPruebaCn = configuration
    	    				.buildSessionFactory();   	   

    	    
    	    return sessionFactoryPruebaCn;
    	    
		} catch (Throwable ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
			ex.printStackTrace();
			throw new ExceptionInInitializerError(ex);
		}
	}
	public static SessionFactory getSessionFactoryPruebaCn() {
		return sessionFactoryPruebaCn;
	}
	
	
}

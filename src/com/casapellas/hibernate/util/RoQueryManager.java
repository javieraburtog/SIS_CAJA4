package com.casapellas.hibernate.util;

import java.util.List;

import org.hibernate.Session;

public class RoQueryManager {

	private Session currentSession;

	
	private  Session getCurrentSession(){
		try {
			
			currentSession = HibernateUtilPruebaCn.currentSession();
					
		} catch (Exception e) {
//			LogCrtl.sendLogDebgs(e);
			e.printStackTrace();
		}
		
		return currentSession;
	}
	
//	private static boolean closeCurrentSession( boolean commit ){
//		boolean done = true;
//		 
//		try {
//			
//			if( currentSession == null || !currentSession.isOpen() )
//				return done ;
//			 
//			HibernateUtilPruebaCn.currentSession().close();
//		 
//		} catch (Exception e) {
//			done = false;
//			e.printStackTrace();
////			LogCrtl.sendLogDebgs(e);
//		}
//		
//		return done;
//	}
	
	@SuppressWarnings("unchecked")
	public synchronized <T> List<T> executeSqlQuery(String query, Class<T> entityClass, boolean nativeQuery  ){
		List<T>lstObjectInQuery = null;
		 
		try {
			
			getCurrentSession();
			 
			if(!nativeQuery)
				lstObjectInQuery = currentSession.createQuery( query ).list() ;
			
			if(nativeQuery && entityClass != null )
				lstObjectInQuery = currentSession.createSQLQuery( query ).addEntity(entityClass).list();
			
			if(nativeQuery && entityClass == null )
				lstObjectInQuery = currentSession.createSQLQuery( query ).list();
			
		} catch (Exception e) {
			lstObjectInQuery = null;
			e.printStackTrace();
		}
//		 
//		finally{
//			
//			closeCurrentSession(true);
//			
//		}
		
		return lstObjectInQuery;
	}
	
	public <T> T executeSqlQueryUnique( String query, Class<T> entityClass, boolean nativeQuery  ){

		List<T>lstObjectInQuery = executeSqlQuery(  query,  entityClass,  nativeQuery  ) ;
		
		if ( lstObjectInQuery == null || lstObjectInQuery.isEmpty() )
			return null;
		else
			return lstObjectInQuery.get(0);
	}
	
	
}

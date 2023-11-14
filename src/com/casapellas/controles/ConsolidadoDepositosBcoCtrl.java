package com.casapellas.controles;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.casapellas.entidades.Finanhdr;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.util.AllConectionMngt;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.PropertiesSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ConsolidadoDepositosBcoCtrl {
	private static int affectedRowsOnExecute;
	private static String msgStatus ;
	
 
	public static Object[] getSessionForQuery( ){
		boolean isNewConnection = false;
		Object[] dtaCn = new Object[3];
		
		try {
			
			Session session = HibernateUtilPruebaCn.currentSession();
			Transaction trans = (isNewConnection = !(session.getTransaction().isActive())) ? 
						session.beginTransaction() : 
						session.getTransaction();
						
			dtaCn[0] = isNewConnection;	
			dtaCn[1] = session;
			dtaCn[2] = trans;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtaCn ;
	}
	public static void closeSessionForQuery( boolean committransaction, Object[] dtaCn ){
		try {
			
			boolean newConnection = (Boolean) dtaCn[0] ;
			Session session = (Session) dtaCn[1] ;
			Transaction trans = (Transaction) dtaCn[2] ;

			if (newConnection && trans.isActive() && session.isOpen()  ) {

				try {

					if (committransaction )
						trans.commit();
					else
						trans.rollback();

				} catch (Exception e2) {
					e2.printStackTrace();
				}
				

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static Session currentSession;
	private static Transaction transaction;
	public static boolean isNewSession;
	
	public static Session getCurrentSession(){
		try {
			
			currentSession = HibernateUtilPruebaCn.currentSession();
			transaction = (isNewSession = !(currentSession.getTransaction().isActive())) ? 
					currentSession.beginTransaction() : 
					currentSession.getTransaction();
					
		} catch (Exception e) {
			
			isNewSession = false;
			e.printStackTrace();
		}
		
		return currentSession;
	}
	
	public static boolean closeCurrentSession( boolean commit ){
		boolean done = true;
		 
		try {
			
			if( !isNewSession || !transaction.isActive() || !currentSession.isOpen() )
				return done ;
		
			try {
				
				if(commit){
					transaction.commit();
				}else{
					transaction.rollback();
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				done = false;
				try {
					transaction.rollback();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			

			
		} catch (Exception e) {
			done = false;
			e.printStackTrace();
		}
		
		return done;
	}
	
	public static <T> T executeSqlQueryUnique( String query, Class<T> entityClass, boolean nativeQuery  ){

		List<T>lstObjectInQuery = executeSqlQuery(  query,  entityClass,  nativeQuery  ) ;
		
		if ( lstObjectInQuery == null || lstObjectInQuery.isEmpty() )
			return null;
		else
			return lstObjectInQuery.get(0);
	}
	
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> executeSqlQuery(String query, Class<T> entityClass, boolean nativeQuery  ){
		List<T>lstObjectInQuery = null;
		 
		try {
			
			//getCurrentSession();
			currentSession= HibernateUtilPruebaCn.currentSession();
			
			LogCajaService.CreateLog("executeSqlQuery", "QRY", query);
			
			if(!nativeQuery)
				lstObjectInQuery = currentSession.createQuery( query ).list() ;
			
			if(nativeQuery && entityClass != null )
				lstObjectInQuery = currentSession.createSQLQuery( query ).addEntity(entityClass).list();
			
			if(nativeQuery && entityClass == null )
				lstObjectInQuery = currentSession.createSQLQuery( query ).list();
			
			
		} catch (Exception e) {
			LogCajaService.CreateLog("executeSqlQuery", "ERR", e.getMessage());
			lstObjectInQuery = null;
			e.printStackTrace();
		}
		
		
		return lstObjectInQuery;
	}
	
	public static <E> List<?> executeSqlQuery( String query, boolean nativeQuery, Class<?> entityClass){
		List<?>lstObjectInQuery = null;
		
		Session session = null;

		
		try {
			session = HibernateUtilPruebaCn.currentSession();
			

			LogCajaService.CreateLog("executeSqlQuery", "QRY", query);
			
			if(!nativeQuery)
				lstObjectInQuery = session.createQuery( query ).list() ;
			if(nativeQuery && entityClass != null )
				lstObjectInQuery = session.createSQLQuery( query ).addEntity(entityClass).list();
			if(nativeQuery && entityClass == null )
				lstObjectInQuery = session.createSQLQuery( query ).list();
			
		} catch (Exception e) {
			LogCajaService.CreateLog("executeSqlQuery", "ERR", e.getMessage());
			e.printStackTrace();
		}
		return lstObjectInQuery ;
	}

	/**
	 * Execute a generic Query
	 * Created Mar 19 2015
	 * 
	 * @author Carlos Hernandez
	 * @category Database Connection
	 * @param query Query String
	 * @return boolean representing successfull execute
	 * 
	 */
	public static boolean executeQueries(List<String>queries, boolean commit, boolean closeconnection){
		int rowsAffected = 0;
		boolean executed = true;
		int iQueryIndex = 0 ;
		int iTotalRowsAffectedByQuery= 0 ; 
		
		try {
			
			setAffectedRowsOnExecute(0);
			setMsgStatus("") ;
			
			if(closeconnection)
				getConnection();
			
			for (int i = 0; i < queries.size(); i++) {
				iQueryIndex = i;
				
//				System.out.println("Query executed : " + queries.get(i) ) ;
				
				try {
					rowsAffected  = sesionForQuery11.createSQLQuery(queries.get(i)).executeUpdate();
					
					LogCajaService.CreateLog("executeQueries", "QRY", queries.get(i));
				} catch (Exception e) {
					LogCajaService.CreateLog("executeQueries", "ERR", e.getMessage());
					
					executed = false;
					e.printStackTrace(); 
					setMsgStatus("Query for position " + iQueryIndex + " executed with error : "+ e.getMessage()  ) ;
					break;
				}
				if(rowsAffected == 0 ){
					executed = false;
					setMsgStatus(" Not rows affected by Query for position " + iQueryIndex  ) ;
					break;
				}
				iTotalRowsAffectedByQuery += rowsAffected ;
				
//				System.out.println(" rows afected " + rowsAffected  );
				
			}
			
		} catch (Exception e) {
			LogCajaService.CreateLog("executeQueries", "ERR", e.getMessage());
			executed = false;
			setMsgStatus("Error on execute Query for position " + iQueryIndex + "  >> error : "+ e.getMessage()  ) ;
			
			e.printStackTrace();
		}finally{
			
			if(closeconnection)
				closeConnection( executed && commit);
			
			setAffectedRowsOnExecute(iTotalRowsAffectedByQuery);
		}
		
		return executed;
	}
	
	public static boolean executeSqlQueries( List<String>queries ){
		int rowsAffected = 0;
		boolean executed = true;
		int iQueryIndex = 0 ;
		int iTotalRowsAffectedByQuery= 0 ; 
		String queryToExecute = "" ;
		
		Session session = null;
		Transaction trans = null;
		boolean newCn = false;
		
		try {
			
			setAffectedRowsOnExecute(0);
			setMsgStatus("") ;
			
			session = HibernateUtilPruebaCn.currentSession();
			trans = (newCn = !(session.getTransaction().isActive())) ? session
					.beginTransaction() : session.getTransaction();
					
			for (int i = 0; i < queries.size(); i++) {
				iQueryIndex = i;
				queryToExecute = queries.get(i) ;

				try {
					LogCajaService.CreateLog("executeSqlQueries", "QRY", queryToExecute);
					
					rowsAffected  = session.createSQLQuery( queryToExecute ).executeUpdate();
					
				} catch (Exception e) {
					LogCajaService.CreateLog("executeSqlQueries", "ERR", e.getMessage());
					executed = false;
					setMsgStatus("Query for position " + iQueryIndex + " executed with error : "+ e.getMessage()  ) ;
					break;
				}
				if(rowsAffected == 0 ){
					executed = false;
					setMsgStatus(" Not rows affected by Query for position " + iQueryIndex  ) ;
					break;
				}
				iTotalRowsAffectedByQuery += rowsAffected ;
				
				
			}
			
		} catch (Exception e) {
			LogCajaService.CreateLog("executeSqlQueries", "ERR", e.getMessage());
			executed = false;
			setMsgStatus("Error on execute Query for position " + iQueryIndex + "  >> error : "+ e.getMessage()  ) ;
		}finally{
			
			if( newCn && trans.isActive() && session.isOpen()  ){
				 
				//&& ================== committ or rollback
				try {
					if( executed ){
						trans.commit();
					}else{
						trans.rollback();
					}
				} catch (Exception e2) {
					executed = false;
					LogCajaService.CreateLog("executeSqlQueries", "ERR", e2.getMessage());
				}
			
			}
			setAffectedRowsOnExecute(iTotalRowsAffectedByQuery);
		}
		return executed;
	}
	
	

	public static boolean executeSqlQueries( Session sesion, Transaction trans, List<String>queries ){
		int rowsAffected = 0;
		boolean executed = true;
		int iQueryIndex = 0 ;
		int iTotalRowsAffectedByQuery= 0 ; 
		String queryToExecute = "" ;
		

		
		try {
			
			setAffectedRowsOnExecute(0);
			setMsgStatus("") ;

			for (int i = 0; i < queries.size(); i++) {
				iQueryIndex = i;
				queryToExecute = queries.get(i) ;

				try {
					
					rowsAffected  = sesion.createSQLQuery( queryToExecute ).executeUpdate();
					
					LogCajaService.CreateLog("executeSqlQueries", "QRY", queryToExecute);
					
				} catch (Exception e) {
					LogCajaService.CreateLog("executeSqlQueries", "ERR", e.getMessage());
					executed = false;
					e.printStackTrace(); 
					setMsgStatus("Query for position " + iQueryIndex + " executed with error : "+ e.getMessage()  ) ;
					break;
				}
				if(rowsAffected == 0 ){
					executed = false;
					setMsgStatus(" Not rows affected by Query for position " + iQueryIndex  ) ;
					break;
				}
				iTotalRowsAffectedByQuery += rowsAffected ;
				
				
			}
			
		} catch (Exception e) {
			LogCajaService.CreateLog("executeSqlQueries", "ERR", e.getMessage());
			executed = false;
			setMsgStatus("Error on execute Query for position " + iQueryIndex + "  >> error : "+ e.getMessage()  ) ;
			
			e.printStackTrace();
			
		}finally{
			
			setAffectedRowsOnExecute(iTotalRowsAffectedByQuery);
		}
		return executed;
	}
	
	
	public static boolean executeSqlQueryTx( Session session,   String queries ){
		int rowsAffected = 0;
		boolean executed = true;
		int iQueryIndex = 0 ;
		int iTotalRowsAffectedByQuery= 0 ; 
		String queryToExecute = "" ;
		
	
		queryToExecute = queries ;
		
		
		if(session !=null) {
			try {
				
				setAffectedRowsOnExecute(0);
				setMsgStatus("") ;
				
				try {
					LogCajaService.CreateLog("executeSqlQueryTx", "QRY", queryToExecute);
					rowsAffected  = session.createSQLQuery( queryToExecute ).executeUpdate();
				} catch (Exception e) {
					LogCajaService.CreateLog("executeSqlQueryTx", "ERR", e.getMessage());
					executed = false;
					setMsgStatus("Query for position " + iQueryIndex + " executed with error : "+ e.getMessage()  ) ;
					return executed;
				}
				
				if(rowsAffected == 0 ){
					executed = false;
					setMsgStatus(" Not rows affected by Query for position " + iQueryIndex  ) ;
					return executed ;
				}
					
			} catch (Exception e) {
				LogCajaService.CreateLog("executeSqlQueryTx", "ERR", e.getMessage());
				executed = false;
				setMsgStatus("Error on execute Query for position " + iQueryIndex + "  >> error : "+ e.getMessage()  ) ;
			}finally{
				setAffectedRowsOnExecute(iTotalRowsAffectedByQuery);
			}
		}else {
			try {
				AllConectionMngt conectionMngt = new AllConectionMngt();
				Connection cn = conectionMngt.getSimpleDriverConnection();
				
				LogCajaService.CreateLog("executeSqlQueryTx", "QRY", queryToExecute);
				PreparedStatement stmt = cn.prepareStatement(queryToExecute);				
				
				stmt.executeUpdate();				
				stmt.close();
				
				cn.close();
				executed=true;
				
			}catch(Exception e) {
				executed=false;
				LogCajaService.CreateLog("executeSqlQueryTx", "ERR", e.getMessage());
			}
			
		}
			
		return executed;
	}
	
	//**********************************************************
	//----------------------------------------------------------
	//++++++++++++++++++      INICIO     +++++++++++++++++++++++
	//----------------------------------------------------------
	//**********************************************************
	//Creado por Luis Alberto Fonseca Mendez 2019-04-23
	public static boolean executeSqlQuery(Session sesion, Transaction trans, String queries ){
		int rowsAffected = 0;
		boolean executed = true;
		int iQueryIndex = 0 ;
		int iTotalRowsAffectedByQuery= 0 ; 
		String queryToExecute = "" ;
		
		try {
			
			setAffectedRowsOnExecute(0);
			setMsgStatus("") ;
			
			queryToExecute = queries ;
			
			try {
//				System.out.println(queries);
				rowsAffected  = sesion.createSQLQuery( queryToExecute ).executeUpdate();
				LogCajaService.CreateLog("executeSqlQuery", "QRY", queryToExecute);
				
			} catch (Exception e) {
				LogCajaService.CreateLog("executeSqlQuery", "ERR", e.getMessage());
				executed = false;
				e.printStackTrace(); 
				setMsgStatus("Query for position " + iQueryIndex + " executed with error : "+ e.getMessage()  ) ;
				return executed;
			}
			
			if(rowsAffected == 0 ){
				executed = false;
				setMsgStatus(" Not rows affected by Query for position " + iQueryIndex  ) ;
				return executed ;
			}
				
		} catch (Exception e) {
			LogCajaService.CreateLog("executeSqlQuery", "ERR", e.getMessage());
			executed = false;
			setMsgStatus("Error on execute Query for position " + iQueryIndex + "  >> error : "+ e.getMessage()  ) ;
			
			e.printStackTrace();
			
		}finally{
			
			setAffectedRowsOnExecute(iTotalRowsAffectedByQuery);
		}
		return executed;
	}
	
	//**********************************************************
	//----------------------------------------------------------
	//++++++++++++++++++       FIN       +++++++++++++++++++++++
	//----------------------------------------------------------
	//**********************************************************
	
	
	
	/**
	 * Execute a generic Query
	 * Created Mar 19 2015
	 * 
	 * @author Carlos Hernandez
	 * @category Database Connection
	 * @param query Query String
	 * @return boolean representing successfull execute
	 * 
	 */
	public static boolean executeQueryUpdate(String strSqlUpdate, boolean committ) {
		int rowsAffected = 0;
		boolean executed = true;
		
		try {
			setMsgStatus("") ;

			getConnection();
			
			rowsAffected  = sesionForQuery11.createSQLQuery(strSqlUpdate).executeUpdate();
			
			LogCajaService.CreateLog("executeQueryUpdate", "QRY", strSqlUpdate);
			
		} catch (Exception e) {
			LogCajaService.CreateLog("executeQueryUpdate", "ERR", e.getMessage());
			
			setMsgStatus(e.getMessage()) ;
			rowsAffected = 0;
			executed = false;
			e.printStackTrace();
		} finally {

			closeConnection(executed);
			
			setAffectedRowsOnExecute(rowsAffected);
		}
		return executed;
	}
	/**
	 * Execute a generic Query
	 * Created Feb 18 2015
	 * 
	 * @author Carlos Hernandez
	 * @category Database Connection
	 * @param query Query String
	 * @param nativeQuery boolean for native query
	 * @param entityClass entity Class type if native query is set true
	 * @return List<?> generic object List in query
	 * 
	 */
	public static  <E> List<?> executeQuery(String query, boolean nativeQuery, Class<?> entityClass, boolean closeConnection){
		List<?>lstObjectInQuery = null;
		try {
			
			if( closeConnection ) 
				getConnection();
			
			if(sesionForQuery11 == null || !transForQuery.isActive() )
				return lstObjectInQuery = new ArrayList<E>(); 
			
//			System.out.println("Query Execute " + query);
			
			if(!nativeQuery)
				lstObjectInQuery = sesionForQuery11.createQuery( query ).list() ;
			if(nativeQuery && entityClass != null )
				lstObjectInQuery = sesionForQuery11.createSQLQuery( query ).addEntity(entityClass).list();
			if(nativeQuery && entityClass == null )
				lstObjectInQuery = sesionForQuery11.createSQLQuery( query ).list();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			if( closeConnection ) 
				closeConnection(true);
			
			if(lstObjectInQuery == null)
				lstObjectInQuery = new ArrayList<E>();
			
		}
		return lstObjectInQuery ;
	} 
	
	/**
	 * @author Carlos Hernandez
	 * @category Database Connection
	 * @Description Create Hibernate DataBaseConnection
	 * @Created Feb 18 2015
	 */
	
	static Session sesionForQuery11;
	static Transaction transForQuery;
	static boolean connectionActive;
	
	public static  boolean getConnection(){
		boolean connected = true;
		
		try {
			
			sesionForQuery11 = HibernateUtilPruebaCn.currentSession();
			
			connectionActive = sesionForQuery11.getTransaction().isActive() ;
			if(connectionActive){
				transForQuery = sesionForQuery11.getTransaction();
			}else{
				transForQuery = sesionForQuery11.beginTransaction() ;
				connectionActive = true;
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return connected  = false;
		}
		return connected;
	}
	public static boolean closeConnection(boolean committransaction){
		boolean connectionClosed = true;
		String origen = ""; 
		try {
			
			
			  origen = PropertiesSystem.CONTEXT_NAME+": "
					+ new Exception().getStackTrace()[1].getClassName() +":"
					+ new Exception().getStackTrace()[2].getMethodName() ;
			
			if( connectionActive && transForQuery != null && sesionForQuery11 != null &&  transForQuery.isActive() && sesionForQuery11.isOpen() ){
				try {
					if(committransaction)
						transForQuery.commit();
					else
						transForQuery.rollback();
				}catch (Exception e2) {
					e2.printStackTrace();
				}
				
				connectionActive = false;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			connectionClosed = false;
			msgStatus = "Error on connection closing process " + e.getMessage();
		}
		return connectionClosed ;
	}
	public static int getAffectedRowsOnExecute() {
		return affectedRowsOnExecute;
	}
	public static void setAffectedRowsOnExecute(int affectedRowsOnExecute) {
		ConsolidadoDepositosBcoCtrl.affectedRowsOnExecute = affectedRowsOnExecute;
	}
	public static String getMsgStatus() {
		return msgStatus;
	}
	public static void setMsgStatus(String msgStatus) {
		ConsolidadoDepositosBcoCtrl.msgStatus = msgStatus;
	}
 
}

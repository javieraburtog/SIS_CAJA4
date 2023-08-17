package com.casapellas.controles;
/**
 * CASA PELLAS S.A.
 * Creado por.........: Juan Carlos �amendi Pineda
 * Fecha de Creaci�n..: 02/07/2009
 * �ltima modificaci�n: 02/07/2009
 * Modificado por.....:	Juan Carlos �amendi Pineda
 * 
 */
import org.hibernate.Session;
import org.hibernate.Transaction;


import com.casapellas.hibernate.util.HibernateUtilPruebaCn;

public class AplicacionCtrl {
	/**OBTIENE EL CODIGO DE LA APLICACION RECIBIENDO COMO PARAMETRO SU NOMBRE CORTO*************************************************/
	public String getCodigoAplicacionxNomcorto(String sNomCorto){
		String sCodApp = null;
		Session session = HibernateUtilPruebaCn.currentSession();
		
		
		try{
			
			sCodApp = (String)session.createQuery("select a.codapp from Aplicacion" +
					" as a where upper(trim(a.nomcorto)) = '"
					+sNomCorto.trim()+"'").uniqueResult();
			
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return sCodApp;
	}
/********************************************************************************************************************/
}


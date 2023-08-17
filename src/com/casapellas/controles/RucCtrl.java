package com.casapellas.controles;
/**
 * CASA PELLAS S.A.
 * Creado por.........: Juan Carlos �amendi Pineda
 * Fecha de Creaci�n..: 15/01/2009
 * �ltima modificaci�n: 06/03/2009
 * Modificado por.....:	Juan Carlos �amendi Pineda
 * 
 */
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;


import com.casapellas.hibernate.util.HibernateUtilPruebaCn;

public class RucCtrl {
	public List getRucxCodigo(String sCodComp){
		List Rucs = new ArrayList();
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			String sql = "from Ruc r where trim(r.codcomp) = '"+sCodComp+ "'";
			Rucs = (List)session
			.createQuery(sql)			
			.list();
			tx.commit();
		}catch(Exception ex){
			System.out.print("==> Excepci�n capturada en getAllRuc: " + ex);
		}finally {			
			session.close();
		}
		return Rucs;
	}
}

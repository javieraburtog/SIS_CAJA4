package com.casapellas.util;

import java.sql.Connection;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.casapellas.entidades.Vf0101;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
/**
 * CASA PELLAS S.A.
 * Creado por.........: Juan Carlos �amendi Pineda
 * Fecha de Creaci�n..: 28/02/2009
 * �ltima modificaci�n: 06/03/2009
 * Modificado por.....:	Juan Carlos �amendi Pineda
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
public class SugerenciasCodigo extends AbstractMap{
	
	public Object get(Object key) {
		ArrayList sugerencias = new ArrayList();
		String sql = null;
		Session session = null;
		Transaction tx = null;
		
		try{
			session = HibernateUtilPruebaCn.currentSession();
			tx = ( session.getTransaction().isActive() ) ?
					session.beginTransaction():
					session.getTransaction();
					
			String currentValue = key.toString();
			sql = "from Vf0101 as f where cast(f.id.aban8 as string) like '"+currentValue+"%'";	
			List result = session.createQuery(sql).list();
			
			tx.commit();
			
			for (int i = 0; i < result.size(); i++)
				sugerencias.add(((Vf0101)(result.get(i))).getId().getAban8() + "");
			
		}catch(Exception e){
			if(session.getTransaction().isActive())
				tx.commit();
			System.out.print("==> Excepci�n capturada en Sugerencias: " + e);
		}finally{
			try {
				HibernateUtilPruebaCn.closeSession(session);
			} catch (Exception e2){e2.printStackTrace();}
		}
		return sugerencias;
	}

	public Set<String> entrySet() {
		Set<String> instanceSet = Collections.newSetFromMap(new IdentityHashMap<String,Boolean>());
		return instanceSet;
	}
}

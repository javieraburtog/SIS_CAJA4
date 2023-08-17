package com.casapellas.reportes;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.casapellas.hibernate.util.HibernateUtilPruebaCn;

public class UpdateRecibodet {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			
			Session sesion = HibernateUtilPruebaCn.getSessionFactoryPruebaCn().getCurrentSession();
			Transaction trans = sesion.beginTransaction();
			
			trans.commit();
			sesion.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

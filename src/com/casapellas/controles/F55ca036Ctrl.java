package com.casapellas.controles;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.casapellas.entidades.Vf55ca036;

import com.casapellas.hibernate.util.HibernateUtilPruebaCn;

public class F55ca036Ctrl {
/***************************************************************************************************************/
	public List<Vf55ca036> buscarDocsxLinea(String codcomp, String linea){
		List<Vf55ca036> lstResult = null;
		Session s = null;
		
		try{
			s = HibernateUtilPruebaCn.currentSession();
			
			String sql = "from Vf55ca036 as f where f.id.b6rp01 = '"+ codcomp + "' and f.id.b6ln = '" + linea + "'"; 

			lstResult = s.createQuery(sql).list();	
			
		
		}catch(Exception ex){
			System.out.println("Se capturo una excepcion en CuotaCtrl.buscarSiguienteCuota: " + ex);
		}finally{
			s.close();
		}
		return lstResult;
	}
/***************************************************************************************************************/
}

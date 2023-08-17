package com.casapellas.controles.tmp;

import java.util.List;

import org.hibernate.Session;
import com.casapellas.entidades.Vf55ca036;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.util.LogCajaService;

public class F55ca036Ctrl {
/***************************************************************************************************************/
	@SuppressWarnings("unchecked")
	public List<Vf55ca036> buscarDocsxLinea(String codcomp, String linea){
		List<Vf55ca036> lstResult = null;
		Session s = null;
		
		try{
			s = HibernateUtilPruebaCn.currentSession();
			String sql = "from Vf55ca036 as f where f.id.b6sts = 'A' and f.id.b6rp01 = '" 
						+ codcomp + "' and f.id.b6ln = '" + linea + "'"; 

			lstResult = s.createQuery(sql).list();
			
		}catch(Exception ex){
			LogCajaService.CreateLog("buscarDocsxLinea", "ERR", ex.getMessage());
			ex.printStackTrace(); 
		}

		return lstResult;
	}
/***************************************************************************************************************/
}

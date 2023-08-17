package com.casapellas.controles;

import java.util.List;
import com.casapellas.entidades.Reintegro;

public class ReintegroCtrl {
/****************************************************************************************************/
	public List<Reintegro> getReintegrosxParametro(int caid, String codcomp, String estado){
		List<Reintegro> lstResult = null;
 
		String sql = "";
		
		try {
			sql = " from Reintegro as r where r.nobatch > 0 ";
			
			if (caid > 0)		
				sql += " and r.id.caid = " +  caid;
			 
			if(!codcomp.equals("") && !codcomp.equals("01"))
				sql += " and r.id.codcomp = '" + codcomp + "' ";
			
			if(estado.equals("01")){
				sql += " and r.estado = 0 ";
			}else{
				sql += " and r.estado = 1 ";
			}
			 
			lstResult = ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, Reintegro.class, false);
 						
		} catch (Exception error) {
			error.printStackTrace(); 
		}  
		return lstResult;
	}
/*******************************************************************************************************/
}

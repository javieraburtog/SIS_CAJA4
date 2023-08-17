package com.casapellas.controles;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.util.CatalogoGenerico;
import com.casapellas.util.PropertiesSystem;

public class FinanciamientoCtrl {

	public Object[] obtenerListaBusqueda(){
		try {
			Session session = HibernateUtilPruebaCn.currentSession();
		SQLQuery query= session.createSQLQuery("select TCOD VALOR,TDESC DESCRIPCION from "+PropertiesSystem.ESQUEMA+".CAJAPARM" + 
				"  where" + 
				"  tparm = '13'");
		
		Object[] returnInfo = query.list().toArray();
		
		return returnInfo;
		}catch(Exception E) {
			E.printStackTrace();
		}
		return null;
	}
	public Object[] obtenerListaTipoPago(){
		try {
			Session session = HibernateUtilPruebaCn.currentSession();
		SQLQuery query= session.createSQLQuery("select TCOD VALOR,TDESC DESCRIPCION from "+PropertiesSystem.ESQUEMA+".CAJAPARM" + 
				"  where" + 
				"  tparm = '15' ORDER BY TVALNUM");
		
		Object[] returnInfo = query.list().toArray();
		
		return returnInfo;
		}catch(Exception E) {
			E.printStackTrace();
		}
		return null;
	}
	
	public CatalogoGenerico obtenerValoresSpe(String parametro,String valor){
		try {
			Session session = HibernateUtilPruebaCn.currentSession();
		SQLQuery query= session.createSQLQuery("select TCOD VALOR,TDESC DESCRIPCION from "+PropertiesSystem.ESQUEMA+".CAJAPARM" + 
				"  where" + 
				"  tparm = '"+parametro+"' and TCOD='"+valor+"'");
		@SuppressWarnings("unchecked")
		List<Object[]> rows = query.list();
		 
		CatalogoGenerico returnInfo = new CatalogoGenerico();
		 for(Object[] row : rows){
			 returnInfo.setVALOR(row[0].toString());
			 returnInfo.setDESCRIPCION(row[1].toString());
			 System.out.println(returnInfo.getDESCRIPCION());
		 }
		return returnInfo;
		}catch(Exception E) {
			E.printStackTrace();
		}
		return null;
	}
}

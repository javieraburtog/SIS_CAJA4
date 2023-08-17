package com.casapellas.util;

import java.sql.Connection;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.context.FacesContext;

import org.hibernate.Session;
import org.hibernate.Transaction;
import com.casapellas.entidades.Vf0101;
import com.casapellas.entidades.Vsolecheque;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;

public class SugerenciaCheques extends AbstractMap {
	@SuppressWarnings("unchecked")
	public Object get(Object key) {
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		ArrayList<String> sugerencias = new ArrayList<String>();
		String sql = null;
		Session session = null;
		Transaction tx = null;
		int busqueda=1;
		boolean bOtroParam = false;
		
		try{
			
			session = HibernateUtilPruebaCn.currentSession();
			tx = ( session.getTransaction().isActive() ) ?
					session.beginTransaction():
					session.getTransaction();
			
			if (m.get("rsc_strBusquedaCheques") != null)
				busqueda = Integer.parseInt((String)m.get("rsc_strBusquedaCheques"));
			
			String currentValue = key.toString();
			List result = new ArrayList();
			
			switch(busqueda){
				//caso 1: busqueda por nombre; caso 2: búsqueda por código.
				case 1:  sql = "from Vsolecheque as v where trim(lower(v.id.cliente)) like '%"+currentValue.trim().toLowerCase()+"%'"; break;
				case 2:  sql = "from Vsolecheque as v where v.id.codcli = "+currentValue.trim(); break;
				default: bOtroParam = true; break;
			}
			if(!bOtroParam){
				
				result = session.createQuery(sql).setMaxResults(70).list();
				
				if(result!=null && result.size()>0){
					String sNombre = "";
					Divisas dv = new Divisas();
					for (int i = 0; i < result.size(); i++){
						Vsolecheque v = (Vsolecheque)result.get(i);
						sNombre = dv.remplazaCaractEspeciales( v.getId().getCliente().trim(), "&", "&amp;");
						sugerencias.add(v.getId().getCodcli()+" => "+sNombre);
					}
				}else{
					result =  new ArrayList<String>();
				}
			}
			
			tx.commit();
			
		}catch(Exception e){
			if(session.getTransaction().isActive())
				tx.commit();
			System.out.print("==> Excepción capturada en SugerenciasCheque: " + e);
			sugerencias = new ArrayList<String>();
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

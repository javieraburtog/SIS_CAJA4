package com.casapellas.util;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.context.FacesContext;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.casapellas.entidades.Hfacturajde;
import com.casapellas.entidades.Vf0101;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;

@SuppressWarnings({  "rawtypes" })
public class SugerenciasCred extends AbstractMap {
	@SuppressWarnings("unchecked")
	public Object get(Object key) {
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		ArrayList<String> sugerencias = new ArrayList<String>();
		String sql = null;
		Session session = null;
		Transaction tx = null;
		int busqueda = 1;
		
		try{
			session =  HibernateUtilPruebaCn.currentSession();
			tx = ( session.getTransaction().isActive() )?
					session.getTransaction():
					session.beginTransaction();
			
			if (m.get("strBusquedaCredito") != null)
				busqueda = Integer.parseInt((String)m.get("strBusquedaCredito"));
			
			String currentValue = key.toString();
			switch (busqueda){
				case(1):
					
					//Busqueda por nombre de cliente
					sql = "from Vf0101 as f where f.id.abalph like '"+currentValue.toUpperCase()+"%'";
					List<Vf0101> result  =  session.createQuery(sql)
												.setTimeout(10)
												.setMaxResults(35).list();
					for (Vf0101 vf : result) 
						sugerencias.add( vf.getId().getAbalph().trim() );
					break;
					
				case(2):
					
					//Busqueda por codigo de cliente
					if(!currentValue.trim().matches("^[0-9]{1,8}$"))
						break;
				
					sql = "from Vf0101 as f where f.id.aban8 = "+currentValue ;	
					List<Vf0101> codsclts = session.createQuery(sql)
												.setTimeout(10)
												.setMaxResults(35).list();
					for (Vf0101 vf : codsclts) 
						sugerencias.add( String.valueOf(vf.getId().getAban8()) ) ;
					break;
					
				case(3):
					
					//Busqueda por numero de factura
					if(!currentValue.trim().matches("^[0-9]{1,15}$"))
						break;
					
					sql = "from Hfacturajde as f where f.id.nofactura  = " +currentValue ;	
					List<Hfacturajde> lista = session.createQuery(sql)
													.setTimeout(10)
													.setMaxResults(35).list();
					for (Hfacturajde hj : lista) 
						sugerencias.add( String.valueOf(hj.getId().getNofactura()) ) ;
					break;
			}
			
			tx.commit();
			
		}catch(Exception e){
			
			sugerencias = new ArrayList<String>();
			System.out.print("==> Excepción capturada en SugerenciasCredito: " + e);
			e.printStackTrace();
			
		}finally{
			try {
				HibernateUtilPruebaCn.closeSession(session);
			} catch (Exception e2){}
		}
		return sugerencias;
	}

	public Set<String> entrySet() {
		Set<String> instanceSet = Collections.newSetFromMap(new IdentityHashMap<String,Boolean>());
		return instanceSet;
	}
	
}

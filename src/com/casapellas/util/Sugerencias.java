package com.casapellas.util;
/**
 * CASA PELLAS S.A.
 * Creado por.........: Juan Carlos Ñamendi Pineda
 * Fecha de Creación..: 28/02/2009
 * Última modificación: 21/05/2009
 * Modificado por.....:	Juan Carlos Ñamendi Pineda
 * 
 */
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

import com.casapellas.controles.CtrlCajas;
import com.casapellas.entidades.F55ca01;
import com.casapellas.entidades.Hfactjdecon;
import com.casapellas.entidades.Vf0101;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.infragistics.faces.shared.smartrefresh.SmartRefreshManager;
	
public class Sugerencias extends AbstractMap {
	@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
	public Object get(Object key) {
		SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		ArrayList sugerencias = new ArrayList();
		String sql = null;
		Session session = null;
		Transaction tx = null;
		int busqueda = 1;
		
		try{
			if (m.get("strBusqueda") != null)
				busqueda = Integer.parseInt((String)m.get("strBusqueda"));
			
			session = HibernateUtilPruebaCn.currentSession();
			tx = ( session.getTransaction().isActive() ) ?
					session.beginTransaction():
					session.getTransaction();
			
			String currentValue = key.toString();
			List result = new ArrayList();
			switch (busqueda){
				case(1):
					//Busqueda por nombre de cliente
					sql = "from Vf0101 as f where f.id.abalph like '"+currentValue.toUpperCase()+"%'";
					if(m.get("tipoUsuario") != null) sql += " and f.id.abat1='E'";
					result = session.createQuery(sql).list();

					for (int i = 0; i < result.size(); i++)
						sugerencias.add(((Vf0101)(result.get(i))).getId().getAbalph().trim() + "");
					
					break;
				case(2):
					//Busqueda por codigo de cliente
					sql = "from Vf0101 as f where cast(f.id.aban8 as string) like '"+currentValue+"%'";
					if(m.get("tipoUsuario") != null) sql += " and f.id.abat1='E'";
					result = session.createQuery(sql).list();
					for (int i = 0; i < result.size(); i++)
						sugerencias.add(((Vf0101)(result.get(i))).getId().getAban8() + "");
					
					break;
				case(3):
					List lstCajas = (List)m.get("lstCajas");
					F55ca01 f55ca01 = ((F55ca01)lstCajas.get(0));
					String sCodSuc = f55ca01.getId().getCaco().substring(3,5);
					//Busqueda por numero de factura
					CtrlCajas cajasCtrl = new CtrlCajas();
					String[] sTiposDoc = (String[])m.get("sTiposDoc");
					int iFechaActual = Integer.parseInt(m.get("iFechaActual").toString());

					result = cajasCtrl.leerFacturasDelDiaxNumero(iFechaActual, sTiposDoc, currentValue,sCodSuc);
					for (int i = 0; i < result.size(); i++){
						sugerencias.add(((Hfactjdecon)(result.get(i))).getId().getNofactura() + "");
					}
					break;
			}		
			tx.commit();
			
		}catch(Exception e){
			if(session.getTransaction().isActive())
				tx.commit();
			System.out.print("==> Excepción capturada en Sugerencias: " + e);
		}finally{
			try {
				HibernateUtilPruebaCn.closeSession(session);
			}
			catch (Exception e2){}
		}
		return sugerencias;
	}

	public Set<String> entrySet() {
		Set<String> instanceSet = Collections.newSetFromMap(new IdentityHashMap<String,Boolean>());
		return instanceSet;
	}

}

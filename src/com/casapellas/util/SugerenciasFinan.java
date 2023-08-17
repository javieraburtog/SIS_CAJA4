package com.casapellas.util;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.context.FacesContext;

import com.casapellas.controles.CtrlCajas;
import com.casapellas.controles.CuotaCtrl;
import com.casapellas.entidades.F55ca01;
import com.casapellas.entidades.Hfactjdecon;
import com.casapellas.entidades.Vf0101;

public class SugerenciasFinan extends AbstractMap{
	public Object get(Object key) {
		List sugerencias = new ArrayList();
		CuotaCtrl cuotaCtrl = new CuotaCtrl();
		try{
			Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
			int busqueda = 1;
			if (m.get("strBusquedaFinan") != null){
				busqueda = Integer.parseInt((String)m.get("strBusquedaFinan"));
			}
			String currentValue = key.toString();
			sugerencias = cuotaCtrl.buscarSegunTipoBusqueda(busqueda, currentValue);
		}catch(Exception ex){
			System.out.println("Se capturo una excepcion en SugerenciasFinan: " + ex);
			ex.printStackTrace();
		}
		return sugerencias;
	}
	public Set<String> entrySet() {
		Set<String> instanceSet = Collections.newSetFromMap(new IdentityHashMap<String,Boolean>());
		return instanceSet;
	}
}

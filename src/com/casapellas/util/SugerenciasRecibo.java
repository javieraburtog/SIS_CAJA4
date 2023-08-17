package com.casapellas.util;
/**
 * CASA PELLAS S.A.
 * Creado por.........: Juan Carlos Ñamendi Pineda
 * Fecha de Creación..: 22/06/2009
 * Última modificación: 23/06/2009
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

import com.casapellas.controles.EmpleadoCtrl;
import com.casapellas.controles.ReciboCtrl;
import com.casapellas.entidades.F55ca01;
import com.casapellas.entidades.Vf0101;
import com.casapellas.entidades.Vf55ca01;

@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
public class SugerenciasRecibo extends AbstractMap {
	public Object get(Object key) {
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		ArrayList sugerenciasRecibo = new ArrayList();
		int busqueda = 1;
		
		try{
			
			if (m.get("strBusquedaRecibo") != null)
				busqueda = Integer.parseInt((String)m.get("strBusquedaRecibo"));
			
			String currentValue = key.toString();
			List result = new ArrayList();
			switch (busqueda){
				case(1):
					String[] sNombres = null;
					sNombres = new EmpleadoCtrl().buscarPersonaxNombre(currentValue);
					for (int i = 0; i < sNombres.length; i++)
						sugerenciasRecibo.add(sNombres[i].trim() + "");
					break;
				case(2):
					//Busqueda por numero de cliente
					int[] iCodigos = null;
					iCodigos =  new EmpleadoCtrl().buscarPersonaxCodigo(currentValue);
					for (int i = 0; i < iCodigos.length; i++)
						sugerenciasRecibo.add(iCodigos[i] + "");
					break;	
				case(3):
					//Busqueda por numero de recibo
					int[] iNumRec = null;
					List lstCajas = (List)m.get("lstCajas");
					Vf55ca01 f55ca01 = ((Vf55ca01)lstCajas.get(0));
					iNumRec = new ReciboCtrl().getNumerosRecibos(f55ca01.getId().getCaid(), f55ca01.getId().getCaco(), currentValue);
					for (int i = 0; i < iNumRec.length; i++)
						sugerenciasRecibo.add(iNumRec[i] + "");
					break;
				case(4):
					//Busqueda por numero de factura
					int[] iNumFac = null;
					List lstCajas2 = (List)m.get("lstCajas");
					Vf55ca01 f55ca012 = ((Vf55ca01)lstCajas2.get(0));
					iNumFac = new ReciboCtrl().getNumerosFactura(f55ca012.getId()
							.getCaid(), f55ca012.getId().getCaco(), currentValue);
					for (int h = 0; h < iNumFac.length; h++)
						sugerenciasRecibo.add(iNumFac[h] + "");
					break;
			}
			
		}catch(Exception e){
			System.out.print("==> Excepción capturada en SugerenciasRecibo: " + e);
		}		
		return sugerenciasRecibo;
	}

	public Set<String> entrySet() {
		Set<String> instanceSet = Collections.newSetFromMap(new IdentityHashMap<String,Boolean>());
		return instanceSet;
	}
}

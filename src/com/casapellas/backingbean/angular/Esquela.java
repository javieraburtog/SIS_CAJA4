/**
 * 
 */
package com.casapellas.backingbean.angular;

import java.util.Map;
import javax.faces.context.FacesContext;
import com.casapellas.entidades.ens.Vautoriz;

public class Esquela {
	@SuppressWarnings("rawtypes")
	public Esquela() {
		try {
			Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
			Vautoriz[] vAut = (Vautoriz[]) m.get("sevAut");

			this.setUsername(vAut[0].getId().getLogin().toString());

		} catch (Exception e) {
				e.printStackTrace();
		}
	}
	String submodulo;
	String username;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}

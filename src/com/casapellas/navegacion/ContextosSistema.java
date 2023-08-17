package com.casapellas.navegacion;

import com.casapellas.util.PropertiesSystem;

public class ContextosSistema {

	
	private String contexto = "";

	public String getContexto() {
		return  PropertiesSystem.CONTEXT_NAME;
	}

	public void setContexto(String contexto) {
		this.contexto = contexto;
	}
	
	
}

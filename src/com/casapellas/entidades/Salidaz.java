

/**
 * 
 	Creado por: Alberto Zapata
 	Fecha de Cracion: 09/12/2008
 	Ultima Modificacion: 10/12/2008
 	Departamento: Informatica
 * 
 */

package com.casapellas.entidades;

import java.math.BigDecimal;
import java.security.Timestamp;

public class Salidaz {
	private String moneda;
	private String sucu;
	private int numsal;	
	private BigDecimal monto;	
	
	public Salidaz(String sucu) {
		super();
		this.sucu = sucu;
	}

	public Salidaz(BigDecimal monto) {
		super();
		this.monto = monto;
	}

	public Salidaz(int numsal) {
		super();
		this.numsal = numsal;
	}

	public Salidaz(String moneda, String sucu, int numsal, BigDecimal monto) {
		super();
		this.moneda = moneda;
		this.sucu = sucu;
		this.numsal = numsal;
		this.monto = monto;
	}
	
	public Salidaz() {
		// TODO Auto-generated constructor stub
	}


	public Salidaz(String string, BigDecimal decimal) {
		// TODO Auto-generated constructor stub
	}

	public String getMoneda() {
		return moneda;
	}
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}
	public BigDecimal getMonto() {
		return monto;
	}
	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}
	public int getNumsal() {
		return numsal;
	}
	public void setNumsal(int numsal) {
		this.numsal = numsal;
	}
	public String getSucu() {
		return sucu;
	}
	public void setSucu(String sucu) {
		this.sucu = sucu;
	}
		
}

package com.casapellas.donacion.entidades;

import java.math.BigDecimal;

public class GValidate {
	
	private BigDecimal Monto;
	private String Moneda;
	public BigDecimal getMonto() {
		return Monto;
	}
	public String getMoneda() {
		return Moneda;
	}
	public void setMonto(BigDecimal monto) {
		Monto = monto;
	}
	public void setMoneda(String moneda) {
		Moneda = moneda;
	}

}

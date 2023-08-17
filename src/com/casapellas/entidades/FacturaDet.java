package com.casapellas.entidades;

import java.util.Date;




public class FacturaDet {

	private String factura;
	private Date fecha;
	private Double monto;
	

	public FacturaDet(String factura, Date fecha, Double monto){
		this.factura = factura;
		this.fecha = fecha;
		this.monto = monto;		
	}


	public String getFactura() {
		return factura;
	}


	public void setFactura(String factura) {
		this.factura = factura;
	}


	public Date getFecha() {
		return fecha;
	}


	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}


	public Double getMonto() {
		return monto;
	}


	public void setMonto(Double monto) {
		this.monto = monto;
	}	
	
}

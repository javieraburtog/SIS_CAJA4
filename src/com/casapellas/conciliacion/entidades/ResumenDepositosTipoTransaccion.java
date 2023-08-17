package com.casapellas.conciliacion.entidades;

import java.math.BigDecimal;

public class ResumenDepositosTipoTransaccion  implements java.io.Serializable{

	private static final long serialVersionUID = -3859015229006483838L;
	
	private String montototal;
	private String moneda;
	private String codigotransaccionbco;
	private String cantidadTransacciones;
	private String descripciondebanco;
	private String descripciondecaja;
	private BigDecimal totalportransaccion;
	private int transacciones;
	
	public ResumenDepositosTipoTransaccion() {
		super();
	}
	
	

	public ResumenDepositosTipoTransaccion(int transacciones, String codigotransaccionbco,
			String cantidadTransacciones, String descripciondebanco, String descripciondecaja ) {
		super();
		this.codigotransaccionbco = codigotransaccionbco;
		this.cantidadTransacciones = cantidadTransacciones;
		this.descripciondebanco = descripciondebanco;
		this.descripciondecaja = descripciondecaja;
		this.transacciones = transacciones;
	}



	public ResumenDepositosTipoTransaccion(String montototal, String moneda,
			String codigotransaccionbco, String cantidadTransacciones,
			String descripciondebanco, String descripciondecaja,
			BigDecimal totalportransaccion, int transacciones) {
		super();
		this.montototal = montototal;
		this.moneda = moneda;
		this.codigotransaccionbco = codigotransaccionbco;
		this.cantidadTransacciones = cantidadTransacciones;
		this.descripciondebanco = descripciondebanco;
		this.descripciondecaja = descripciondecaja;
		this.totalportransaccion = totalportransaccion;
		this.transacciones = transacciones;
	}

	public String getMontototal() {
		return montototal;
	}

	public void setMontototal(String montototal) {
		this.montototal = montototal;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public String getCodigotransaccionbco() {
		return codigotransaccionbco;
	}

	public void setCodigotransaccionbco(String codigotransaccionbco) {
		this.codigotransaccionbco = codigotransaccionbco;
	}

	public String getCantidadTransacciones() {
		return cantidadTransacciones;
	}

	public void setCantidadTransacciones(String cantidadTransacciones) {
		this.cantidadTransacciones = cantidadTransacciones;
	}

	public String getDescripciondebanco() {
		return descripciondebanco;
	}

	public void setDescripciondebanco(String descripciondebanco) {
		this.descripciondebanco = descripciondebanco;
	}

	public String getDescripciondecaja() {
		return descripciondecaja;
	}

	public void setDescripciondecaja(String descripciondecaja) {
		this.descripciondecaja = descripciondecaja;
	}
	public BigDecimal getTotalportransaccion() {
		return totalportransaccion;
	}
	public void setTotalportransaccion(BigDecimal totalportransaccion) {
		this.totalportransaccion = totalportransaccion;
	}
	public int getTransacciones() {
		return transacciones;
	}
	public void setTransacciones(int transacciones) {
		this.transacciones = transacciones;
	}

}

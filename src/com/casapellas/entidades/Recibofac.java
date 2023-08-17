package com.casapellas.entidades;

// Generated Jun 26, 2013 11:17:48 AM by Hibernate Tools 3.2.1.GA

import java.math.BigDecimal;

public class Recibofac implements java.io.Serializable {

	private static final long serialVersionUID = -1429820467480656252L;
	private RecibofacId id;
	private BigDecimal monto;
	private String estado;

	private BigDecimal tasafactura;

	private String fecha;

	private String moneda;
	private int orden;

	public Recibofac() {
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public Recibofac(RecibofacId id, BigDecimal monto, String estado) {
		this.id = id;
		this.monto = monto;
		this.estado = estado;
	}

	public RecibofacId getId() {
		return this.id;
	}

	public void setId(RecibofacId id) {
		this.id = id;
	}

	public BigDecimal getMonto() {
		return this.monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public BigDecimal getTasafactura() {
		return tasafactura;
	}

	public void setTasafactura(BigDecimal tasafactura) {
		this.tasafactura = tasafactura;
	}

	public int getOrden() {
		return orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}

}

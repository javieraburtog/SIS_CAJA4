package com.casapellas.jde.creditos;

import java.math.BigDecimal;

public class DatosComprobanteF0911 {
	
	private String companiaComprobante;
	private String idcuenta;
	private BigDecimal monto;
	private String descripcion;
	private BigDecimal tasaCambio;
	private String sublibro;
	private String tipoSublibro;
	
	public DatosComprobanteF0911(String idcuenta, BigDecimal monto,
			String descripcion, BigDecimal tasaCambio,
			String sublibro, String tipoSublibro,  String companiaComprobante) {
		super();
		this.idcuenta = idcuenta;
		this.monto = monto;
		this.descripcion = descripcion;
		this.tasaCambio = tasaCambio;
		this.sublibro = sublibro;
		this.tipoSublibro = tipoSublibro;
		this.companiaComprobante = companiaComprobante;
	}
	public String getIdcuenta() {
		return idcuenta;
	}
	public void setIdcuenta(String idcuenta) {
		this.idcuenta = idcuenta;
	}
	public BigDecimal getMonto() {
		return monto;
	}
	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public BigDecimal getTasaCambio() {
		return tasaCambio;
	}
	public void setTasaCambio(BigDecimal tasaCambio) {
		this.tasaCambio = tasaCambio;
	}
	public String getSublibro() {
		return sublibro;
	}
	public void setSublibro(String sublibro) {
		this.sublibro = sublibro;
	}
	public String getTipoSublibro() {
		return tipoSublibro;
	}
	public void setTipoSublibro(String tipoSublibro) {
		this.tipoSublibro = tipoSublibro;
	}
	public String getCompaniaComprobante() {
		return companiaComprobante;
	}
	public void setCompaniaComprobante(String companiaComprobante) {
		this.companiaComprobante = companiaComprobante;
	}

}

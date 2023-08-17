package com.casapellas.entidades;

import java.math.BigDecimal;

public class ResumenCobroAutomatico implements java.io.Serializable {

    private static final long serialVersionUID = 7862279072610530857L;
	
	private String companiaCodigo;
	private String companiaNombre;
	private String moneda;
	private String codigoterminal;
	private String codigoafiliado;
	private String nombreterminal;
	private String nombreafiliado;
	private BigDecimal montototal;
	private int cantidadtransacciones;
	private int cantidadexcluidas;
	private int cantidadprocesadas;
	private int codigocaja;
	
	private String observaciones;
	private boolean procesado;
	
	
	public ResumenCobroAutomatico(String codigoterminal, String codigoafiliado,
			String nombreterminal, String companiaCodigo, String moneda,
			int codigocaja) {
		this.codigoterminal = codigoterminal;
		this.codigoafiliado = codigoafiliado;
		this.nombreterminal = nombreterminal;
		this.companiaCodigo = companiaCodigo;
		this.moneda = moneda;
		this.codigocaja = codigocaja;
	}
	
	
	public ResumenCobroAutomatico(String companiaCodigo, String companiaNombre,
			String moneda, String codigoterminal, String codigoafiliado, String nombreafiliado,
			BigDecimal montototal, int cantidadtransacciones,
			int cantidadexcluidas, int cantidadprocesadas, int codigocaja, String nombreterminal) {
		super();
		this.companiaCodigo = companiaCodigo;
		this.companiaNombre = companiaNombre;
		this.moneda = moneda;
		this.codigoterminal = codigoterminal;
		this.codigoafiliado = codigoafiliado;
		this.nombreafiliado = nombreafiliado;
		this.montototal = montototal;
		this.cantidadtransacciones = cantidadtransacciones;
		this.cantidadexcluidas = cantidadexcluidas;
		this.cantidadprocesadas = cantidadprocesadas;
		this.codigocaja = codigocaja;
		this.nombreterminal = nombreterminal;
	}
	public String getCompaniaNombre() {
		return companiaNombre;
	}
	public void setCompaniaNombre(String companiaNombre) {
		this.companiaNombre = companiaNombre;
	}
	public String getCompaniaCodigo() {
		return companiaCodigo;
	}
	public void setCompaniaCodigo(String companiaCodigo) {
		this.companiaCodigo = companiaCodigo;
	}
	public String getMoneda() {
		return moneda;
	}
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}
	public String getCodigoterminal() {
		return codigoterminal;
	}
	public void setCodigoterminal(String codigoterminal) {
		this.codigoterminal = codigoterminal;
	}
	public String getCodigoafiliado() {
		return codigoafiliado;
	}
	public void setCodigoafiliado(String codigoafiliado) {
		this.codigoafiliado = codigoafiliado;
	}
	public BigDecimal getMontototal() {
		return montototal;
	}
	public void setMontototal(BigDecimal montototal) {
		this.montototal = montototal;
	}
	public int getCantidadtransacciones() {
		return cantidadtransacciones;
	}
	public void setCantidadtransacciones(int cantidadtransacciones) {
		this.cantidadtransacciones = cantidadtransacciones;
	}
	public int getCantidadexcluidas() {
		return cantidadexcluidas;
	}
	public void setCantidadexcluidas(int cantidadexcluidas) {
		this.cantidadexcluidas = cantidadexcluidas;
	}
	public int getCantidadprocesadas() {
		return cantidadprocesadas;
	}
	public void setCantidadprocesadas(int cantidadprocesadas) {
		this.cantidadprocesadas = cantidadprocesadas;
	}
	public String getNombreafiliado() {
		return nombreafiliado;
	}
	public void setNombreafiliado(String nombreafiliado) {
		this.nombreafiliado = nombreafiliado;
	}
	public int getCodigocaja() {
		return codigocaja;
	}
	public void setCodigocaja(int codigocaja) {
		this.codigocaja = codigocaja;
	}
	public String getNombreterminal() {
		return nombreterminal;
	}
	public void setNombreterminal(String nombreterminal) {
		this.nombreterminal = nombreterminal;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public boolean isProcesado() {
		return procesado;
	}
	public void setProcesado(boolean procesado) {
		this.procesado = procesado;
	}

}

package com.casapellas.entidades;

import java.math.BigDecimal;
import java.util.Date;

public class FacturaxRecibo implements java.io.Serializable{
	private static final long serialVersionUID = -5783349796125089403L;
	private int nofactura;
	private String tipofactura;
	private String unineg;
	private BigDecimal monto;
	private String moneda;
	private String fecha;
	private String partida;
	
	private String nofacturaorigen;
	public String getNofacturaorigen() {
		return nofacturaorigen;
	}
	public void setNofacturaorigen(String nofacturaorigen) {
		this.nofacturaorigen = nofacturaorigen;
	}
	private String tipofacturaorigen;
	
	public FacturaxRecibo(){
	}
	public FacturaxRecibo(int nofactura, String tipofactura, String unineg, BigDecimal monto, String moneda, String fecha, String partida) {
		super();
		this.nofactura = nofactura;
		this.tipofactura = tipofactura;
		this.unineg = unineg;
		this.monto = monto;
		this.moneda = moneda;
		this.fecha = fecha;
		this.partida = partida;
	}
	//------- Constructor para datos del BIEN pagado con primas.
	public FacturaxRecibo(int nofactura,String tipoproducto,String marca,String modelo,String referencia){
		super();
		this.nofactura = nofactura;
		this.tipofactura = tipoproducto;
		this.unineg = marca;		
		this.moneda = modelo;
		this.fecha = referencia;
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
	public BigDecimal getMonto() {
		return monto;
	}
	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}
	public int getNofactura() {
		return nofactura;
	}
	public void setNofactura(int nofactura) {
		this.nofactura = nofactura;
	}
	public String getPartida() {
		return partida;
	}
	public void setPartida(String partida) {
		this.partida = partida;
	}
	public String getTipofactura() {
		return tipofactura;
	}
	public void setTipofactura(String tipofactura) {
		this.tipofactura = tipofactura;
	}
	public String getUnineg() {
		return unineg;
	}
	public void setUnineg(String unineg) {
		this.unineg = unineg;
	}
	public String getTipofacturaorigen() {
		return tipofacturaorigen;
	}
	public void setTipofacturaorigen(String tipofacturaorigen) {
		this.tipofacturaorigen = tipofacturaorigen;
	}
	
	
	
}

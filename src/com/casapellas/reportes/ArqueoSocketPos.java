package com.casapellas.reportes;

import java.math.BigDecimal;
import java.sql.Date;

public class ArqueoSocketPos implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9137264260902514061L;
	private int nocaja;
	private String moneda;
	private int noarqueo;
	private Date fechaarqueo;
	private String sucursal;
	
	private String afiliado;
	private String termId;
	private String autorizacion;
	private int purshtrans;
	private double purshamount;
	private int rettrans;
	private double retamount;
	public int getNocaja() {
		return nocaja;
	}
	public void setNocaja(int nocaja) {
		this.nocaja = nocaja;
	}
	public String getMoneda() {
		return moneda;
	}
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}
	public int getNoarqueo() {
		return noarqueo;
	}
	public void setNoarqueo(int noarqueo) {
		this.noarqueo = noarqueo;
	}
	public Date getFechaarqueo() {
		return fechaarqueo;
	}
	public void setFechaarqueo(Date fechaarqueo) {
		this.fechaarqueo = fechaarqueo;
	}
	public String getSucursal() {
		return sucursal;
	}
	public void setSucursal(String sucursal) {
		this.sucursal = sucursal;
	}
	public String getAfiliado() {
		return afiliado;
	}
	public void setAfiliado(String afiliado) {
		this.afiliado = afiliado;
	}
	public String getTermId() {
		return termId;
	}
	public void setTermId(String termId) {
		this.termId = termId;
	}
	public String getAutorizacion() {
		return autorizacion;
	}
	public void setAutorizacion(String autorizacion) {
		this.autorizacion = autorizacion;
	}
	public int getPurshtrans() {
		return purshtrans;
	}
	public void setPurshtrans(int purshtrans) {
		this.purshtrans = purshtrans;
	}
	public double getPurshamount() {
		return purshamount;
	}
	public void setPurshamount(double purshamount) {
		this.purshamount = purshamount;
	}
	public int getRettrans() {
		return rettrans;
	}
	public void setRettrans(int rettrans) {
		this.rettrans = rettrans;
	}
	public double getRetamount() {
		return retamount;
	}
	public void setRetamount(double retamount) {
		this.retamount = retamount;
	}
	
	
}

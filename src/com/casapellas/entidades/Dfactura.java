package com.casapellas.entidades;

import java.math.BigDecimal;


public class Dfactura implements java.io.Serializable {
	
	private static final long serialVersionUID = -1113384084256804800L;
	private int nofactura;
	private String tipofactura;
	private String coditem;
	private String descitem;
	private double preciounit;
	private String moneda;
	private BigDecimal tasa;
	private long cant;
	private String impuesto;
	private double factor;
	
	public Dfactura() {
	}

	public Dfactura(int nofactura, String tipofactura, String coditem,
			String descitem, double preciounit, long cant, String impuesto,
			double factor, String moneda, BigDecimal tasa) {
		this.nofactura = nofactura;
		this.tipofactura = tipofactura;
		this.coditem = coditem;
		this.descitem = descitem;
//		System.out.println("Cod ITEM: " + coditem + " --> Precio: " + preciounit);
		this.preciounit = preciounit;
		this.cant = cant;
		this.impuesto = impuesto;
		this.factor = factor;
		this.moneda = moneda;
		this.tasa = tasa;
	}
	
	public long getCant() {
		return cant;
	}
	public void setCant(long cant) {
		this.cant = cant;
	}
	public String getCoditem() {
		return coditem;
	}
	public void setCoditem(String coditem) {
		this.coditem = coditem;
	}
	public String getDescitem() {
		return descitem;
	}
	public void setDescitem(String descitem) {
		this.descitem = descitem;
	}
	public double getFactor() {
		return factor;
	}
	public void setFactor(double factor) {
		this.factor = factor;
	}
	public String getTipofactura() {
		return tipofactura;
	}
	public void setTipofactura(String tipofactura) {
		this.tipofactura = tipofactura;
	}
	public String getImpuesto() {
		return impuesto;
	}
	public void setImpuesto(String impuesto) {
		this.impuesto = impuesto;
	}
	public int getNofactura() {
		return nofactura;
	}
	public void setNofactura(int nofactura) {
		this.nofactura = nofactura;
	}
	public double getPreciounit() {
		return preciounit;
	}
	public void setPreciounit(double preciounit) {
		this.preciounit = preciounit;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public BigDecimal getTasa() {
		return tasa;
	}

	public void setTasa(BigDecimal tasa) {
		this.tasa = tasa;
	}
	
	
}

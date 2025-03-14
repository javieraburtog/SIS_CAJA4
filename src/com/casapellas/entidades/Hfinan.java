package com.casapellas.entidades;

import java.math.BigDecimal;
import java.util.Date;

// Generated 07/01/2010 11:59:23 AM by Hibernate Tools 3.2.0.b9

/**
 * Hfinan generated by hbm2java
 */
public class Hfinan implements java.io.Serializable {
	private int nofactura;
	private String tipofactura;
	private String cuota;
	private int nosol;
	private String nomcli;
	private String unineg;
	private BigDecimal montopend;
	private Date fecha;
	private Date fechavenc;
	private String moneda;
	private BigDecimal consolidado;
	private BigDecimal intereses;
	private boolean selected;
	private HfinanId id;

	public Hfinan() {
	}
	public Hfinan(HfinanId id) {
		this.id = id;
	}
	public Hfinan(int nofactura, String tipofactura, String cuota, int nosol,
			String nomcli, String unineg, BigDecimal montopend, Date fecha,
			Date fechavenc, String moneda,BigDecimal consolidado,BigDecimal intereses,boolean selected, HfinanId id) {
		super();
		this.nofactura = nofactura;
		this.tipofactura = tipofactura;
		this.cuota = cuota;
		this.nosol = nosol;
		this.nomcli = nomcli;
		this.unineg = unineg;
		this.montopend = montopend;
		this.fecha = fecha;
		this.fechavenc = fechavenc;
		this.moneda = moneda;
		this.consolidado = consolidado;
		this.intereses = intereses;
		this.selected = selected;
		this.id = id;
	}

	public int getNofactura() {
		return nofactura;
	}

	public void setNofactura(int nofactura) {
		this.nofactura = nofactura;
	}

	public String getTipofactura() {
		return tipofactura;
	}

	public void setTipofactura(String tipofactura) {
		this.tipofactura = tipofactura;
	}

	public String getCuota() {
		return cuota;
	}

	public void setCuota(String cuota) {
		this.cuota = cuota;
	}

	public int getNosol() {
		return nosol;
	}

	public void setNosol(int nosol) {
		this.nosol = nosol;
	}

	public String getNomcli() {
		return nomcli;
	}

	public void setNomcli(String nomcli) {
		this.nomcli = nomcli;
	}

	public String getUnineg() {
		return unineg;
	}

	public void setUnineg(String unineg) {
		this.unineg = unineg;
	}

	public BigDecimal getMontopend() {
		return montopend;
	}

	public void setMontopend(BigDecimal montopend) {
		this.montopend = montopend;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Date getFechavenc() {
		return fechavenc;
	}

	public void setFechavenc(Date fechavenc) {
		this.fechavenc = fechavenc;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}
	public BigDecimal getConsolidado() {
		return consolidado;
	}

	public void setConsolidado(BigDecimal consolidado) {
		this.consolidado = consolidado;
	}

	public BigDecimal getIntereses() {
		return intereses;
	}

	public void setIntereses(BigDecimal intereses) {
		this.intereses = intereses;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public HfinanId getId() {
		return this.id;
	}

	public void setId(HfinanId id) {
		this.id = id;
	}

}

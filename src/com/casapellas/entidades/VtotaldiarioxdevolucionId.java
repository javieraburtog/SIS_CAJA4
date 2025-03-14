package com.casapellas.entidades;

// Generated 07-27-2009 02:37:21 PM by Hibernate Tools 3.2.0.b9

import java.math.BigDecimal;
import java.util.Date;

/**
 * VtotaldiarioxdevolucionId generated by hbm2java
 */
public class VtotaldiarioxdevolucionId implements java.io.Serializable {

	private int caid;

	private String codcomp;

	private String codsuc;

	private Date fecha;

	private BigDecimal cor;

	private BigDecimal usd;

	public VtotaldiarioxdevolucionId() {
	}

	public VtotaldiarioxdevolucionId(int caid, String codsuc, Date fecha) {
		this.caid = caid;
		this.codsuc = codsuc;
		this.fecha = fecha;
	}

	public VtotaldiarioxdevolucionId(int caid, String codcomp, String codsuc,
			Date fecha, BigDecimal cor, BigDecimal usd) {
		this.caid = caid;
		this.codcomp = codcomp;
		this.codsuc = codsuc;
		this.fecha = fecha;
		this.cor = cor;
		this.usd = usd;
	}

	public int getCaid() {
		return this.caid;
	}

	public void setCaid(int caid) {
		this.caid = caid;
	}

	public String getCodcomp() {
		return this.codcomp;
	}

	public void setCodcomp(String codcomp) {
		this.codcomp = codcomp;
	}

	public String getCodsuc() {
		return this.codsuc;
	}

	public void setCodsuc(String codsuc) {
		this.codsuc = codsuc;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public BigDecimal getCor() {
		return this.cor;
	}

	public void setCor(BigDecimal cor) {
		this.cor = cor;
	}

	public BigDecimal getUsd() {
		return this.usd;
	}

	public void setUsd(BigDecimal usd) {
		this.usd = usd;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof VtotaldiarioxdevolucionId))
			return false;
		VtotaldiarioxdevolucionId castOther = (VtotaldiarioxdevolucionId) other;

		return (this.getCaid() == castOther.getCaid())
				&& ((this.getCodcomp() == castOther.getCodcomp()) || (this
						.getCodcomp() != null
						&& castOther.getCodcomp() != null && this.getCodcomp()
						.equals(castOther.getCodcomp())))
				&& ((this.getCodsuc() == castOther.getCodsuc()) || (this
						.getCodsuc() != null
						&& castOther.getCodsuc() != null && this.getCodsuc()
						.equals(castOther.getCodsuc())))
				&& ((this.getFecha() == castOther.getFecha()) || (this
						.getFecha() != null
						&& castOther.getFecha() != null && this.getFecha()
						.equals(castOther.getFecha())))
				&& ((this.getCor() == castOther.getCor()) || (this.getCor() != null
						&& castOther.getCor() != null && this.getCor().equals(
						castOther.getCor())))
				&& ((this.getUsd() == castOther.getUsd()) || (this.getUsd() != null
						&& castOther.getUsd() != null && this.getUsd().equals(
						castOther.getUsd())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getCaid();
		result = 37 * result
				+ (getCodcomp() == null ? 0 : this.getCodcomp().hashCode());
		result = 37 * result
				+ (getCodsuc() == null ? 0 : this.getCodsuc().hashCode());
		result = 37 * result
				+ (getFecha() == null ? 0 : this.getFecha().hashCode());
		result = 37 * result
				+ (getCor() == null ? 0 : this.getCor().hashCode());
		result = 37 * result
				+ (getUsd() == null ? 0 : this.getUsd().hashCode());
		return result;
	}

}

package com.casapellas.entidades;

// Generated May 28, 2010 7:56:47 AM by Hibernate Tools 3.2.0.b9

import java.util.Date;

/**
 * TrasladofacId generated by hbm2java
 */
public class TrasladofacId implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1851204887128494122L;

	private int nofactura;

	private String tipofactura;

	private String codsuc;

	private String codcomp;

	private String codunineg;

	private int caidorig;

	private int caiddest;

	private Date fecha;

	private String estadotr;

	private String estadof;

	private String moneda;

	public TrasladofacId() {
	}

	public TrasladofacId(int nofactura, String tipofactura, String codsuc,
			String codcomp, String codunineg, int caidorig, int caiddest,
			Date fecha, String estadotr, String estadof, String moneda) {
		this.nofactura = nofactura;
		this.tipofactura = tipofactura;
		this.codsuc = codsuc;
		this.codcomp = codcomp;
		this.codunineg = codunineg;
		this.caidorig = caidorig;
		this.caiddest = caiddest;
		this.fecha = fecha;
		this.estadotr = estadotr;
		this.estadof = estadof;
		this.moneda = moneda;
	}

	public int getNofactura() {
		return this.nofactura;
	}

	public void setNofactura(int nofactura) {
		this.nofactura = nofactura;
	}

	public String getTipofactura() {
		return this.tipofactura;
	}

	public void setTipofactura(String tipofactura) {
		this.tipofactura = tipofactura;
	}

	public String getCodsuc() {
		return this.codsuc;
	}

	public void setCodsuc(String codsuc) {
		this.codsuc = codsuc;
	}

	public String getCodcomp() {
		return this.codcomp;
	}

	public void setCodcomp(String codcomp) {
		this.codcomp = codcomp;
	}

	public String getCodunineg() {
		return this.codunineg;
	}

	public void setCodunineg(String codunineg) {
		this.codunineg = codunineg;
	}

	public int getCaidorig() {
		return this.caidorig;
	}

	public void setCaidorig(int caidorig) {
		this.caidorig = caidorig;
	}

	public int getCaiddest() {
		return this.caiddest;
	}

	public void setCaiddest(int caiddest) {
		this.caiddest = caiddest;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getEstadotr() {
		return this.estadotr;
	}

	public void setEstadotr(String estadotr) {
		this.estadotr = estadotr;
	}

	public String getEstadof() {
		return this.estadof;
	}

	public void setEstadof(String estadof) {
		this.estadof = estadof;
	}

	public String getMoneda() {
		return this.moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TrasladofacId))
			return false;
		TrasladofacId castOther = (TrasladofacId) other;

		return (this.getNofactura() == castOther.getNofactura())
				&& ((this.getTipofactura() == castOther.getTipofactura()) || (this
						.getTipofactura() != null
						&& castOther.getTipofactura() != null && this
						.getTipofactura().equals(castOther.getTipofactura())))
				&& ((this.getCodsuc() == castOther.getCodsuc()) || (this
						.getCodsuc() != null
						&& castOther.getCodsuc() != null && this.getCodsuc()
						.equals(castOther.getCodsuc())))
				&& ((this.getCodcomp() == castOther.getCodcomp()) || (this
						.getCodcomp() != null
						&& castOther.getCodcomp() != null && this.getCodcomp()
						.equals(castOther.getCodcomp())))
				&& ((this.getCodunineg() == castOther.getCodunineg()) || (this
						.getCodunineg() != null
						&& castOther.getCodunineg() != null && this
						.getCodunineg().equals(castOther.getCodunineg())))
				&& (this.getCaidorig() == castOther.getCaidorig())
				&& (this.getCaiddest() == castOther.getCaiddest())
				&& ((this.getFecha() == castOther.getFecha()) || (this
						.getFecha() != null
						&& castOther.getFecha() != null && this.getFecha()
						.equals(castOther.getFecha())))
				&& ((this.getEstadotr() == castOther.getEstadotr()) || (this
						.getEstadotr() != null
						&& castOther.getEstadotr() != null && this
						.getEstadotr().equals(castOther.getEstadotr())))
				&& ((this.getEstadof() == castOther.getEstadof()) || (this
						.getEstadof() != null
						&& castOther.getEstadof() != null && this.getEstadof()
						.equals(castOther.getEstadof())))
				&& ((this.getMoneda() == castOther.getMoneda()) || (this
						.getMoneda() != null
						&& castOther.getMoneda() != null && this.getMoneda()
						.equals(castOther.getMoneda())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getNofactura();
		result = 37
				* result
				+ (getTipofactura() == null ? 0 : this.getTipofactura()
						.hashCode());
		result = 37 * result
				+ (getCodsuc() == null ? 0 : this.getCodsuc().hashCode());
		result = 37 * result
				+ (getCodcomp() == null ? 0 : this.getCodcomp().hashCode());
		result = 37 * result
				+ (getCodunineg() == null ? 0 : this.getCodunineg().hashCode());
		result = 37 * result + this.getCaidorig();
		result = 37 * result + this.getCaiddest();
		result = 37 * result
				+ (getFecha() == null ? 0 : this.getFecha().hashCode());
		result = 37 * result
				+ (getEstadotr() == null ? 0 : this.getEstadotr().hashCode());
		result = 37 * result
				+ (getEstadof() == null ? 0 : this.getEstadof().hashCode());
		result = 37 * result
				+ (getMoneda() == null ? 0 : this.getMoneda().hashCode());
		return result;
	}

}

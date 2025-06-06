package com.casapellas.entidades;

// Generated 05-03-2012 09:01:28 AM by Hibernate Tools 3.4.0.CR1

/**
 * ReintegroId generated by hbm2java
 */
public class ReintegroId implements java.io.Serializable {

	private int noreint;
	private int caid;
	private String codcomp;
	private String codusc;

	public ReintegroId() {
	}

	public ReintegroId(int noreint, int caid, String codcomp, String codusc) {
		this.noreint = noreint;
		this.caid = caid;
		this.codcomp = codcomp;
		this.codusc = codusc;
	}

	public int getNoreint() {
		return this.noreint;
	}

	public void setNoreint(int noreint) {
		this.noreint = noreint;
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

	public String getCodusc() {
		return this.codusc;
	}

	public void setCodusc(String codusc) {
		this.codusc = codusc;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ReintegroId))
			return false;
		ReintegroId castOther = (ReintegroId) other;

		return (this.getNoreint() == castOther.getNoreint())
				&& (this.getCaid() == castOther.getCaid())
				&& ((this.getCodcomp() == castOther.getCodcomp()) || (this
						.getCodcomp() != null && castOther.getCodcomp() != null && this
						.getCodcomp().equals(castOther.getCodcomp())))
				&& ((this.getCodusc() == castOther.getCodusc()) || (this
						.getCodusc() != null && castOther.getCodusc() != null && this
						.getCodusc().equals(castOther.getCodusc())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getNoreint();
		result = 37 * result + this.getCaid();
		result = 37 * result
				+ (getCodcomp() == null ? 0 : this.getCodcomp().hashCode());
		result = 37 * result
				+ (getCodusc() == null ? 0 : this.getCodusc().hashCode());
		return result;
	}

}

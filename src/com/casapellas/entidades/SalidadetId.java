package com.casapellas.entidades;

// Generated 02-09-2009 11:21:48 PM by Hibernate Tools 3.2.0.b9

/**
 * SalidadetId generated by hbm2java
 */
public class SalidadetId implements java.io.Serializable {

	private String moneda;

	private String sucu;

	private int numsal;

	public SalidadetId() {
	}

	public SalidadetId(String moneda, String sucu, int numsal) {
		this.moneda = moneda;
		this.sucu = sucu;
		this.numsal = numsal;
	}

	public String getMoneda() {
		return this.moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public String getSucu() {
		return this.sucu;
	}

	public void setSucu(String sucu) {
		this.sucu = sucu;
	}

	public int getNumsal() {
		return this.numsal;
	}

	public void setNumsal(int numsal) {
		this.numsal = numsal;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof SalidadetId))
			return false;
		SalidadetId castOther = (SalidadetId) other;

		return ((this.getMoneda() == castOther.getMoneda()) || (this
				.getMoneda() != null
				&& castOther.getMoneda() != null && this.getMoneda().equals(
				castOther.getMoneda())))
				&& ((this.getSucu() == castOther.getSucu()) || (this.getSucu() != null
						&& castOther.getSucu() != null && this.getSucu()
						.equals(castOther.getSucu())))
				&& (this.getNumsal() == castOther.getNumsal());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getMoneda() == null ? 0 : this.getMoneda().hashCode());
		result = 37 * result
				+ (getSucu() == null ? 0 : this.getSucu().hashCode());
		result = 37 * result + this.getNumsal();
		return result;
	}

}

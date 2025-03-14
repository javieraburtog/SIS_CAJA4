package com.casapellas.entidades;

// Generated 27/02/2009 12:28:20 by Hibernate Tools 3.2.0.b9

/**
 * UneglineaId generated by hbm2java
 */
public class UneglineaId implements java.io.Serializable {

	private String mcmcu;

	private String mcdl01;

	private String drky;

	private String drdl01;

	public UneglineaId() {
	}

	public UneglineaId(String mcmcu, String mcdl01, String drky, String drdl01) {
		this.mcmcu = mcmcu;
		this.mcdl01 = mcdl01;
		this.drky = drky;
		this.drdl01 = drdl01;
	}

	public String getMcmcu() {
		return this.mcmcu;
	}

	public void setMcmcu(String mcmcu) {
		this.mcmcu = mcmcu;
	}

	public String getMcdl01() {
		return this.mcdl01;
	}

	public void setMcdl01(String mcdl01) {
		this.mcdl01 = mcdl01;
	}

	public String getDrky() {
		return this.drky;
	}

	public void setDrky(String drky) {
		this.drky = drky;
	}

	public String getDrdl01() {
		return this.drdl01;
	}

	public void setDrdl01(String drdl01) {
		this.drdl01 = drdl01;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof UneglineaId))
			return false;
		UneglineaId castOther = (UneglineaId) other;

		return ((this.getMcmcu() == castOther.getMcmcu()) || (this.getMcmcu() != null
				&& castOther.getMcmcu() != null && this.getMcmcu().equals(
				castOther.getMcmcu())))
				&& ((this.getMcdl01() == castOther.getMcdl01()) || (this
						.getMcdl01() != null
						&& castOther.getMcdl01() != null && this.getMcdl01()
						.equals(castOther.getMcdl01())))
				&& ((this.getDrky() == castOther.getDrky()) || (this.getDrky() != null
						&& castOther.getDrky() != null && this.getDrky()
						.equals(castOther.getDrky())))
				&& ((this.getDrdl01() == castOther.getDrdl01()) || (this
						.getDrdl01() != null
						&& castOther.getDrdl01() != null && this.getDrdl01()
						.equals(castOther.getDrdl01())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getMcmcu() == null ? 0 : this.getMcmcu().hashCode());
		result = 37 * result
				+ (getMcdl01() == null ? 0 : this.getMcdl01().hashCode());
		result = 37 * result
				+ (getDrky() == null ? 0 : this.getDrky().hashCode());
		result = 37 * result
				+ (getDrdl01() == null ? 0 : this.getDrdl01().hashCode());
		return result;
	}

}

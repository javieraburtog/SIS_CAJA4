package com.casapellas.entidades;

// Generated 07-07-2010 03:22:27 PM by Hibernate Tools 3.2.0.b9

/**
 * Cafiliados generated by hbm2java
 */
public class Cafiliados implements java.io.Serializable {

	private CafiliadosId id;
	private String termid;

	public Cafiliados() {
	}

	public Cafiliados(CafiliadosId id) {
		this.id = id;
	}

	public CafiliadosId getId() {
		return this.id;
	}

	public void setId(CafiliadosId id) {
		this.id = id;
	}

	public String getTermid() {
		return termid;
	}

	public void setTermid(String termid) {
		this.termid = termid;
	}
	
}

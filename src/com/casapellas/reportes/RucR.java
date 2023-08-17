package com.casapellas.reportes;

public class RucR {
	private String codcomp;
	private String ruc;
	
	public RucR(String codcomp, String ruc) {
		super();
		this.codcomp = codcomp;
		this.ruc = ruc;
	}
	public String getCodcomp() {
		return codcomp;
	}
	public void setCodcomp(String codcomp) {
		this.codcomp = codcomp;
	}
	public String getRuc() {
		return ruc;
	}
	public void setRuc(String ruc) {
		this.ruc = ruc;
	}
}

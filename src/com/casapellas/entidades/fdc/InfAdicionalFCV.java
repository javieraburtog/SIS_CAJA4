package com.casapellas.entidades.fdc;

import java.math.BigDecimal;

public class InfAdicionalFCV {
		
	private int numRec;	
	private String codComp;
	private int caId;
	private String codSuc;	
	private String tipoRec;
	private int numFicha;
	private String moneda; 
	private BigDecimal tasaDeCambio;
	public int getNumRec() {
		return numRec;
	}
	public void setNumRec(int numRec) {
		this.numRec = numRec;
	}
	public String getCodComp() {
		return codComp;
	}
	public void setCodComp(String codComp) {
		this.codComp = codComp;
	}
	public int getCaId() {
		return caId;
	}
	public void setCaId(int caId) {
		this.caId = caId;
	}
	public String getCodSuc() {
		return codSuc;
	}
	public void setCodSuc(String codSuc) {
		this.codSuc = codSuc;
	}
	public String getTipoRec() {
		return tipoRec;
	}
	public void setTipoRec(String tipoRec) {
		this.tipoRec = tipoRec;
	}
	public int getNumFicha() {
		return numFicha;
	}
	public void setNumFicha(int numFicha) {
		this.numFicha = numFicha;
	}
	public String getMoneda() {
		return moneda;
	}
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}
	public BigDecimal getTasaDeCambio() {
		return tasaDeCambio;
	}
	public void setTasaDeCambio(BigDecimal tasaDeCambio) {
		this.tasaDeCambio = tasaDeCambio;
	}
	
	
}
package com.casapellas.reportes;


public class ReciboFacR {
	private int numfac;
	private int numrec;
	private String monto;
	private String montoPendiente;
	
	public ReciboFacR(int numfac, int numrec, String monto, String montoPendiente) {
		super();
		this.numfac = numfac;
		this.numrec = numrec;
		this.monto = monto;
		this.montoPendiente = montoPendiente;
	}
	
	public String getMonto() {
		return monto;
	}
	public void setMonto(String monto) {
		this.monto = monto;
	}
	public int getNumfac() {
		return numfac;
	}
	public void setNumfac(int numfac) {
		this.numfac = numfac;
	}
	public int getNumrec() {
		return numrec;
	}
	public void setNumrec(int numrec) {
		this.numrec = numrec;
	}
	public String getMontoPendiente() {
		return montoPendiente;
	}
	public void setMontoPendiente(String montoPendiente) {
		this.montoPendiente = montoPendiente;
	}
}

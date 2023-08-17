package com.casapellas.reportes;


public class ReciboDetR {
	private int numrec;
	private String monto;
	private String moneda;
	private String mpago;
	private String tasa;
	private String equiv;
	private String refer1;
	private String refer2;
	private String refer3;
	private String refer4;
	
	
	public ReciboDetR(int numrec, String monto, String moneda, String mpago, String tasa, String equiv, String refer1, String refer2, String refer3, String refer4) {
		super();
		this.numrec = numrec;
		this.monto = monto;
		this.moneda = moneda;
		this.mpago = mpago;
		this.tasa = tasa;
		this.equiv = equiv;
		this.refer1 = refer1;
		this.refer2 = refer2;
		this.refer3 = refer3;
		this.refer4 = refer4;
	}
	public String getEquiv() {
		return equiv;
	}
	public void setEquiv(String equiv) {
		this.equiv = equiv;
	}
	public String getMoneda() {
		return moneda;
	}
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}
	public String getMonto() {
		return monto;
	}
	public void setMonto(String monto) {
		this.monto = monto;
	}
	public String getMpago() {
		return mpago;
	}
	public void setMpago(String mpago) {
		this.mpago = mpago;
	}
	public int getNumrec() {
		return numrec;
	}
	public void setNumrec(int numrec) {
		this.numrec = numrec;
	}
	public String getRefer1() {
		return refer1;
	}
	public void setRefer1(String refer1) {
		this.refer1 = refer1;
	}
	public String getRefer2() {
		return refer2;
	}
	public void setRefer2(String refer2) {
		this.refer2 = refer2;
	}
	public String getRefer3() {
		return refer3;
	}
	public void setRefer3(String refer3) {
		this.refer3 = refer3;
	}
	public String getRefer4() {
		return refer4;
	}
	public void setRefer4(String refer4) {
		this.refer4 = refer4;
	}
	public String getTasa() {
		return tasa;
	}
	public void setTasa(String tasa) {
		this.tasa = tasa;
	}
	
}

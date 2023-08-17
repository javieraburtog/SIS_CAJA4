package com.casapellas.entidades;


public class ReporteIR {
	private int caid;
	private int no;
	private String codunineg;
	private String unineg;
	private String codcomp;
	private String nomcomp;
	
	private String tipodocumento;
	private int referencia;
	private double venta;
	private double iva;
	private double total;
	private double exenta;
	private double exonerada;
	private double ingresoAbonoPrima;
	private double ingresoSujetoRetencion;
	private double montoComisionIVA;
	private double montoComisionVenta;
	
	private double retencionAnticipoIR;
	private double retencionIVA;
	private double totalRetencion;
	private double tasa;
	
	private String moneda;
	private String fechaInicio;
	private String fechaFin;
	
	public ReporteIR(){
		
	}

	public ReporteIR(int caid,int no, String codunineg, String unineg, String codcomp,
			String nomcomp, String tipodocumento, int referencia, double venta,
			double iva, double total, double exenta, double exonerada,
			double ingresoAbonoPrima, double ingresoSujetoRetencion,
			double montoComisionIVA, double montoComisionVenta,
			double retencionAnticipoIR, double retencionIVA,
			double totalRetencion, double tasa, String moneda,
			String fechaInicio, String fechaFin) {
		super();
		this.caid = caid;
		this.no = no;
		this.codunineg = codunineg;
		this.unineg = unineg;
		this.codcomp = codcomp;
		this.nomcomp = nomcomp;
		this.tipodocumento = tipodocumento;
		this.referencia = referencia;
		this.venta = venta;
		this.iva = iva;
		this.total = total;
		this.exenta = exenta;
		this.exonerada = exonerada;
		this.ingresoAbonoPrima = ingresoAbonoPrima;
		this.ingresoSujetoRetencion = ingresoSujetoRetencion;
		this.montoComisionIVA = montoComisionIVA;
		this.montoComisionVenta = montoComisionVenta;
		this.retencionAnticipoIR = retencionAnticipoIR;
		this.retencionIVA = retencionIVA;
		this.totalRetencion = totalRetencion;
		this.tasa = tasa;
		this.moneda = moneda;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
	}



	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getCodunineg() {
		return codunineg;
	}

	public void setCodunineg(String codunineg) {
		this.codunineg = codunineg;
	}

	public String getUnineg() {
		return unineg;
	}

	public void setUnineg(String unineg) {
		this.unineg = unineg;
	}

	public String getCodcomp() {
		return codcomp;
	}

	public void setCodcomp(String codcomp) {
		this.codcomp = codcomp;
	}

	public String getNomcomp() {
		return nomcomp;
	}

	public void setNomcomp(String nomcomp) {
		this.nomcomp = nomcomp;
	}

	public String getTipodocumento() {
		return tipodocumento;
	}

	public void setTipodocumento(String tipodocumento) {
		this.tipodocumento = tipodocumento;
	}

	public int getReferencia() {
		return referencia;
	}

	public void setReferencia(int referencia) {
		this.referencia = referencia;
	}

	public double getVenta() {
		return venta;
	}

	public void setVenta(double venta) {
		this.venta = venta;
	}

	public double getIva() {
		return iva;
	}

	public void setIva(double iva) {
		this.iva = iva;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public double getExenta() {
		return exenta;
	}

	public void setExenta(double exenta) {
		this.exenta = exenta;
	}

	public double getExonerada() {
		return exonerada;
	}

	public void setExonerada(double exonerada) {
		this.exonerada = exonerada;
	}

	public double getIngresoAbonoPrima() {
		return ingresoAbonoPrima;
	}

	public void setIngresoAbonoPrima(double ingresoAbonoPrima) {
		this.ingresoAbonoPrima = ingresoAbonoPrima;
	}

	public double getIngresoSujetoRetencion() {
		return ingresoSujetoRetencion;
	}

	public void setIngresoSujetoRetencion(double ingresoSujetoRetencion) {
		this.ingresoSujetoRetencion = ingresoSujetoRetencion;
	}

	public double getMontoComisionIVA() {
		return montoComisionIVA;
	}

	public void setMontoComisionIVA(double montoComisionIVA) {
		this.montoComisionIVA = montoComisionIVA;
	}

	public double getMontoComisionVenta() {
		return montoComisionVenta;
	}

	public void setMontoComisionVenta(double montoComisionVenta) {
		this.montoComisionVenta = montoComisionVenta;
	}

	public double getRetencionAnticipoIR() {
		return retencionAnticipoIR;
	}

	public void setRetencionAnticipoIR(double retencionAnticipoIR) {
		this.retencionAnticipoIR = retencionAnticipoIR;
	}

	public double getRetencionIVA() {
		return retencionIVA;
	}

	public void setRetencionIVA(double retencionIVA) {
		this.retencionIVA = retencionIVA;
	}

	public double getTotalRetencion() {
		return totalRetencion;
	}

	public void setTotalRetencion(double totalRetencion) {
		this.totalRetencion = totalRetencion;
	}

	public double getTasa() {
		return tasa;
	}

	public void setTasa(double tasa) {
		this.tasa = tasa;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public String getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public String getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}

	public int getCaid() {
		return caid;
	}

	public void setCaid(int caid) {
		this.caid = caid;
	}
	
	
}

package com.casapellas.reportes;


public class Rptmcaja008 {
	
	private String sFechaSolicitud;
	private int iNosolicitud;
	private String sCompaniaNombre;
	private String sContador;
	private String sSupervisor;
	private String sConcepto;
	
	private int iNofactura;
	private double dMontofac;
	private String sTipofac;
	private String sFechafac;
	private String sMonedafac;
	private String sDatosFactura;
	
	private int iNoDevolucion;
	private double dMontodev;
	private String sTipodev;
	private String sFechadev;	
	private String sMonedadev;
	private String sDatosDevolucion;
	
	private int caid;
	private int codcajero;
	private int noarqueo;
	private int nocliente;
	private String codsuc;
	private String codcomp;
	private String nombrecaja;
	private String nombrecomp;
	private String nombresuc;	
	private String nombrecajero;
	private String snombrecliente;
	
	
	public Rptmcaja008() {
	}
	public Rptmcaja008(String fechaSolicitud, int nosolicitud, String companiaNombre, String contador, String supervisor, String concepto, int nofactura, double montofac, String tipofac, String fechafac, String monedafac, String datosFactura, int noDevolucion, double montodev, String tipodev, String fechadev, String monedadev, String datosDevolucion, int caid, int codcajero, int noarqueo, int nocliente, String codsuc, String codcomp, String nombrecaja, String nombrecomp, String nombresuc, String nombrecajero, String snombrecliente) {
		super();
		sFechaSolicitud = fechaSolicitud;
		iNosolicitud = nosolicitud;
		sCompaniaNombre = companiaNombre;
		sContador = contador;
		sSupervisor = supervisor;
		sConcepto = concepto;
		iNofactura = nofactura;
		dMontofac = montofac;
		sTipofac = tipofac;
		sFechafac = fechafac;
		sMonedafac = monedafac;
		sDatosFactura = datosFactura;
		iNoDevolucion = noDevolucion;
		dMontodev = montodev;
		sTipodev = tipodev;
		sFechadev = fechadev;
		sMonedadev = monedadev;
		sDatosDevolucion = datosDevolucion;
		this.caid = caid;
		this.codcajero = codcajero;
		this.noarqueo = noarqueo;
		this.nocliente = nocliente;
		this.codsuc = codsuc;
		this.codcomp = codcomp;
		this.nombrecaja = nombrecaja;
		this.nombrecomp = nombrecomp;
		this.nombresuc = nombresuc;
		this.nombrecajero = nombrecajero;
		this.snombrecliente = snombrecliente;
	}
	public int getCaid() {
		return caid;
	}
	public void setCaid(int caid) {
		this.caid = caid;
	}
	public int getCodcajero() {
		return codcajero;
	}
	public void setCodcajero(int codcajero) {
		this.codcajero = codcajero;
	}
	public String getCodcomp() {
		return codcomp;
	}
	public void setCodcomp(String codcomp) {
		this.codcomp = codcomp;
	}
	public String getCodsuc() {
		return codsuc;
	}
	public void setCodsuc(String codsuc) {
		this.codsuc = codsuc;
	}
	public double getDMontodev() {
		return dMontodev;
	}
	public void setDMontodev(double montodev) {
		dMontodev = montodev;
	}
	public double getDMontofac() {
		return dMontofac;
	}
	public void setDMontofac(double montofac) {
		dMontofac = montofac;
	}
	public int getINoDevolucion() {
		return iNoDevolucion;
	}
	public void setINoDevolucion(int noDevolucion) {
		iNoDevolucion = noDevolucion;
	}
	public int getINofactura() {
		return iNofactura;
	}
	public void setINofactura(int nofactura) {
		iNofactura = nofactura;
	}
	public int getINosolicitud() {
		return iNosolicitud;
	}
	public void setINosolicitud(int nosolicitud) {
		iNosolicitud = nosolicitud;
	}
	public int getNoarqueo() {
		return noarqueo;
	}
	public void setNoarqueo(int noarqueo) {
		this.noarqueo = noarqueo;
	}
	public int getNocliente() {
		return nocliente;
	}
	public void setNocliente(int nocliente) {
		this.nocliente = nocliente;
	}
	public String getNombrecaja() {
		return nombrecaja;
	}
	public void setNombrecaja(String nombrecaja) {
		this.nombrecaja = nombrecaja;
	}
	public String getNombrecajero() {
		return nombrecajero;
	}
	public void setNombrecajero(String nombrecajero) {
		this.nombrecajero = nombrecajero;
	}
	public String getNombrecomp() {
		return nombrecomp;
	}
	public void setNombrecomp(String nombrecomp) {
		this.nombrecomp = nombrecomp;
	}
	public String getNombresuc() {
		return nombresuc;
	}
	public void setNombresuc(String nombresuc) {
		this.nombresuc = nombresuc;
	}
	public String getSCompaniaNombre() {
		return sCompaniaNombre;
	}
	public void setSCompaniaNombre(String companiaNombre) {
		sCompaniaNombre = companiaNombre;
	}
	public String getSConcepto() {
		return sConcepto;
	}
	public void setSConcepto(String concepto) {
		sConcepto = concepto;
	}
	public String getSContador() {
		return sContador;
	}
	public void setSContador(String contador) {
		sContador = contador;
	}
	public String getSDatosDevolucion() {
		return sDatosDevolucion;
	}
	public void setSDatosDevolucion(String datosDevolucion) {
		sDatosDevolucion = datosDevolucion;
	}
	public String getSDatosFactura() {
		return sDatosFactura;
	}
	public void setSDatosFactura(String datosFactura) {
		sDatosFactura = datosFactura;
	}
	public String getSFechadev() {
		return sFechadev;
	}
	public void setSFechadev(String fechadev) {
		sFechadev = fechadev;
	}
	public String getSFechafac() {
		return sFechafac;
	}
	public void setSFechafac(String fechafac) {
		sFechafac = fechafac;
	}
	public String getSFechaSolicitud() {
		return sFechaSolicitud;
	}
	public void setSFechaSolicitud(String fechaSolicitud) {
		sFechaSolicitud = fechaSolicitud;
	}
	public String getSMonedadev() {
		return sMonedadev;
	}
	public void setSMonedadev(String monedadev) {
		sMonedadev = monedadev;
	}
	public String getSMonedafac() {
		return sMonedafac;
	}
	public void setSMonedafac(String monedafac) {
		sMonedafac = monedafac;
	}
	public String getSnombrecliente() {
		return snombrecliente;
	}
	public void setSnombrecliente(String snombrecliente) {
		this.snombrecliente = snombrecliente;
	}
	public String getSSupervisor() {
		return sSupervisor;
	}
	public void setSSupervisor(String supervisor) {
		sSupervisor = supervisor;
	}
	public String getSTipodev() {
		return sTipodev;
	}
	public void setSTipodev(String tipodev) {
		sTipodev = tipodev;
	}
	public String getSTipofac() {
		return sTipofac;
	}
	public void setSTipofac(String tipofac) {
		sTipofac = tipofac;
	}
	
	
	
	
	
}

package com.casapellas.reportes;

public class RptmcajaHeader {
	
	private int caid;
	private int codcajero;
	private int noarqueo;
	private String codsuc;
	private String codcomp;
	private String nombrecaja;
	private String nombrecomp;
	private String nombresuc;
	private String sfechainicial;
	private String sfechafinal;
	private String sfechareporte;	
	private String moneda;
	private String nombrecajero;
	
	private String casucur;
	private String casucurname;
	
	public RptmcajaHeader(){
		
	}
	public RptmcajaHeader(int caid, int codcajero, int noarqueo, String codsuc, String codcomp, 
						  String nombrecaja, String nombrecomp, String nombresuc, String sfechainicial,
						  String sfechafinal, String sfechareporte, String moneda, String nombrecajero) {
		super();
		this.caid = caid;
		this.codcajero = codcajero;
		this.noarqueo = noarqueo;
		this.codsuc = codsuc;
		this.codcomp = codcomp;
		this.nombrecaja = nombrecaja;
		this.nombrecomp = nombrecomp;
		this.nombresuc = nombresuc;
		this.sfechainicial = sfechainicial;
		this.sfechafinal = sfechafinal;
		this.sfechareporte = sfechareporte;
		this.moneda = moneda;
		this.nombrecajero = nombrecajero;
	}
	
	
	public RptmcajaHeader(int caid, int codcajero, int noarqueo, String codsuc, String codcomp, String nombrecaja,
			String nombrecomp, String nombresuc, String sfechainicial, String sfechafinal, String sfechareporte,
			String moneda, String nombrecajero, String casucur, String casucurname) {
		super();
		this.caid = caid;
		this.codcajero = codcajero;
		this.noarqueo = noarqueo;
		this.codsuc = codsuc;
		this.codcomp = codcomp;
		this.nombrecaja = nombrecaja;
		this.nombrecomp = nombrecomp;
		this.nombresuc = nombresuc;
		this.sfechainicial = sfechainicial;
		this.sfechafinal = sfechafinal;
		this.sfechareporte = sfechareporte;
		this.moneda = moneda;
		this.nombrecajero = nombrecajero;
		this.casucur = casucur;
		this.casucurname = casucurname;
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
	public String getMoneda() {
		return moneda;
	}
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}
	public int getNoarqueo() {
		return noarqueo;
	}
	public void setNoarqueo(int noarqueo) {
		this.noarqueo = noarqueo;
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
	public String getSfechafinal() {
		return sfechafinal;
	}
	public void setSfechafinal(String sfechafinal) {
		this.sfechafinal = sfechafinal;
	}
	public String getSfechainicial() {
		return sfechainicial;
	}
	public void setSfechainicial(String sfechainicial) {
		this.sfechainicial = sfechainicial;
	}
	public String getSfechareporte() {
		return sfechareporte;
	}
	public void setSfechareporte(String sfechareporte) {
		this.sfechareporte = sfechareporte;
	}
	public String getCasucur() {
		return casucur;
	}
	public void setCasucur(String casucur) {
		this.casucur = casucur;
	}
	public String getCasucurname() {
		return casucurname;
	}
	public void setCasucurname(String casucurname) {
		this.casucurname = casucurname;
	}
}

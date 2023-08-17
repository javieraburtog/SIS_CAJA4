package com.casapellas.reportes;
/**
 * CASA PELLAS S.A.
 * Creado por.........: Carlos Manuel Hernández Morrison
 * Fecha de Creación..: 24/03/2010
 * Última modificación: Carlos Manuel Hernández Morrison
 * Modificado por.....:	24/03/2010
 * Descripción:.......: Datos para el encabezado de los reportes de caja Rptmcaja00x.  
 */
public class TransJdeHeaderR {
	
	private int caid;
	private String codsuc;
	private String codcomp;
	private String nombrecaja;
	private String nombrecomp;
	private String nombresuc;
	private String sfechainicial;
	private String sfechafinal;
	private String sfechatrans;	
	
	public TransJdeHeaderR(){
		
	}
	public TransJdeHeaderR(int caid, String codsuc, String codcomp, String nombrecaja, String nombrecomp,
				String nombresuc, String sfechainicial, String sfechafinal,String sfechatrans) {
		super();
		this.caid = caid;
		this.codsuc = codsuc;
		this.codcomp = codcomp;
		this.nombrecaja = nombrecaja;
		this.nombrecomp = nombrecomp;
		this.nombresuc = nombresuc;
		this.sfechainicial = sfechainicial;
		this.sfechafinal = sfechafinal;
		this.sfechatrans = sfechatrans;
	}
	
	public int getCaid() {
		return caid;
	}
	public void setCaid(int caid) {
		this.caid = caid;
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
	public String getNombrecaja() {
		return nombrecaja;
	}
	public void setNombrecaja(String nombrecaja) {
		this.nombrecaja = nombrecaja;
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
	public String getSfechatrans() {
		return sfechatrans;
	}
	public void setSfechatrans(String sfechatrans) {
		this.sfechatrans = sfechatrans;
	}
}

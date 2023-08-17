package com.casapellas.reportes;

import java.sql.Date;


public class TransJdeDetalleR {
	
	private long numfac;
	private String numrefer;
	private int numrecdep;
	private int nodocumento;
	private int nobatch;
	private String tipodocumento;
	private String tiporec;
	private int caid;
	private String codsuc;
	private String codcomp;
	private double monto;
	private String restado;
	private Date fecharec;
	private Date horarec;
	private String nombrecaja;
	private String nombrecomp;
	private String nombresuc;
	private String sfechainicial;
	private String sfechafinal;
	private String sfechatrans;
	
	public TransJdeDetalleR(){	
	}
	public TransJdeDetalleR(long numfac, String numrefer, int numrecdep, int nodocumento, int nobatch, String 
							 tipodocumento, String tiporec, int caid, String codsuc,String codcomp, double monto,
							 String restado, Date fecharec, Date horarec, String nombrecaja, String nombrecomp, 
							 String nombresuc, String sfechainicial, String sfechafinal,String sfechatrans) {
		super();
		this.numfac = numfac;
		this.numrefer = numrefer;
		this.numrecdep = numrecdep;
		this.nodocumento = nodocumento;
		this.nobatch = nobatch;
		this.tipodocumento = tipodocumento;
		this.tiporec = tiporec;
		this.caid = caid;
		this.codsuc = codsuc;
		this.codcomp = codcomp;
		this.monto = monto;
		this.restado = restado;
		this.fecharec = fecharec;
		this.horarec = horarec;
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
	public Date getFecharec() {
		return fecharec;
	}
	public void setFecharec(Date fecharec) {
		this.fecharec = fecharec;
	}
	public Date getHorarec() {
		return horarec;
	}
	public void setHorarec(Date horarec) {
		this.horarec = horarec;
	}
	public double getMonto() {
		return monto;
	}
	public void setMonto(double monto) {
		this.monto = monto;
	}
	public int getNobatch() {
		return nobatch;
	}
	public void setNobatch(int nobatch) {
		this.nobatch = nobatch;
	}
	public int getNodocumento() {
		return nodocumento;
	}
	public void setNodocumento(int nodocumento) {
		this.nodocumento = nodocumento;
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
	public long getNumfac() {
		return numfac;
	}
	public void setNumfac(long numfac) {
		this.numfac = numfac;
	}
	public int getNumrecdep() {
		return numrecdep;
	}
	public void setNumrecdep(int numrecdep) {
		this.numrecdep = numrecdep;
	}
	public String getNumrefer() {
		return numrefer;
	}
	public void setNumrefer(String numrefer) {
		this.numrefer = numrefer;
	}
	public String getRestado() {
		return restado;
	}
	public void setRestado(String restado) {
		this.restado = restado;
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
	public String getTipodocumento() {
		return tipodocumento;
	}
	public void setTipodocumento(String tipodocumento) {
		this.tipodocumento = tipodocumento;
	}
	public String getTiporec() {
		return tiporec;
	}
	public void setTiporec(String tiporec) {
		this.tiporec = tiporec;
	}
	public String getSfechatrans() {
		return sfechatrans;
	}
	public void setSfechatrans(String sfechatrans) {
		this.sfechatrans = sfechatrans;
	}
}


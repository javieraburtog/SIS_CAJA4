package com.casapellas.entidades;

import java.math.BigDecimal;

public class FacturaCredito implements java.io.Serializable{
	private static final long serialVersionUID = -5836335414258140635L;
	private String tipofactura;
	private int nofactura;
	private String partida;
	private String fecha;
	private String fechavenc;
	private int codcli;
	private String nomcli;
	private String codunineg;
	private String unineg;
	private String codsuc;
	private String nomsuc;
	private String codcomp;
	private String nomcomp;
	private double csubtotal;
	private double dsubtotal;
	private String cimpuesto;
	private String dimpuesto;
	private double ctotal;
	private double dtotal;
	private String moneda;
	private BigDecimal tasa;
	private String tipopago;
	private double cpendiente;
	private double dpendiente;
	private String codepago;
	private String descepago;
	private String fechagrab;
	private String hechopor;
	private String pantalla;
	private String fechalm;
	private String fechaimp;
	private String compenslm;
	private int nobatch;
	private String fechabatch;
	private String observaciones;
	private String referencia;
	private int numorden;
	private String tipoorden;
	private String cdescdisp;
	private String cdesctom;
	private String ddescdisp;
	private String ddesctom;
	private String codcont;	
	private String desctpago;	
	private double total;
	private double equiv;
	private double montoAplicar;
	private boolean selected;
	private double pendiente;
	private double impuesto;
	private double subtotal;
	public FacturaCredito(){
	}
	public FacturaCredito(String tipofactura, int nofactura, String partida, String fecha, String fechavenc, int codcli, String nomcli, String codunineg, String unineg, String codsuc, String nomsuc, String codcomp, String nomcomp, double csubtotal, double dsubtotal, String cimpuesto, String dimpuesto, double ctotal, double dtotal, String moneda, BigDecimal tasa, String tipopago, double cpendiente, double dpendiente, String codepago, String descepago, String fechagrab, String hechopor, String pantalla, String fechalm, String fechaimp, String compenslm, int nobatch, String fechabatch, String observaciones, String referencia, int numorden, String tipoorden, String cdescdisp, String cdesctom, 
							String ddescdisp, String ddesctom, String codcont,String desctpago, double total,double equiv,double montoAplicar,boolean selected,double pendiente,double impuesto,double subtotal) {
		super();
		this.tipofactura = tipofactura;
		this.nofactura = nofactura;
		this.partida = partida;
		this.fecha = fecha;
		this.fechavenc = fechavenc;
		this.codcli = codcli;
		this.nomcli = nomcli;
		this.codunineg = codunineg;
		this.unineg = unineg;
		this.codsuc = codsuc;
		this.nomsuc = nomsuc;
		this.codcomp = codcomp;
		this.nomcomp = nomcomp;
		this.csubtotal = csubtotal;
		this.dsubtotal = dsubtotal;
		this.cimpuesto = cimpuesto;
		this.dimpuesto = dimpuesto;
		this.ctotal = ctotal;
		this.dtotal = dtotal;
		this.moneda = moneda;
		this.tasa = tasa;
		this.tipopago = tipopago;
		this.cpendiente = cpendiente;
		this.dpendiente = dpendiente;
		this.codepago = codepago;
		this.descepago = descepago;
		this.fechagrab = fechagrab;
		this.hechopor = hechopor;
		this.pantalla = pantalla;
		this.fechalm = fechalm;
		this.fechaimp = fechaimp;
		this.compenslm = compenslm;
		this.nobatch = nobatch;
		this.fechabatch = fechabatch;
		this.observaciones = observaciones;
		this.referencia = referencia;
		this.numorden = numorden;
		this.tipoorden = tipoorden;
		this.cdescdisp = cdescdisp;
		this.cdesctom = cdesctom;
		this.ddescdisp = ddescdisp;
		this.ddesctom = ddesctom;
		this.codcont = codcont;
		this.desctpago = desctpago;
		this.total = total;
		this.equiv = equiv;
		this.montoAplicar = montoAplicar;
		this.selected = selected;
		this.pendiente = pendiente;
		this.impuesto = impuesto;
		this.subtotal = subtotal;
	}

	public String getCdescdisp() {
		return cdescdisp;
	}

	public void setCdescdisp(String cdescdisp) {
		this.cdescdisp = cdescdisp;
	}

	public String getCdesctom() {
		return cdesctom;
	}

	public void setCdesctom(String cdesctom) {
		this.cdesctom = cdesctom;
	}

	public String getCimpuesto() {
		return cimpuesto;
	}

	public void setCimpuesto(String cimpuesto) {
		this.cimpuesto = cimpuesto;
	}

	public int getCodcli() {
		return codcli;
	}

	public void setCodcli(int codcli) {
		this.codcli = codcli;
	}

	public String getCodcomp() {
		return codcomp;
	}

	public void setCodcomp(String codcomp) {
		this.codcomp = codcomp;
	}

	public String getCodcont() {
		return codcont;
	}

	public void setCodcont(String codcont) {
		this.codcont = codcont;
	}

	public String getCodepago() {
		return codepago;
	}

	public void setCodepago(String codepago) {
		this.codepago = codepago;
	}

	public String getCodsuc() {
		return codsuc;
	}

	public void setCodsuc(String codsuc) {
		this.codsuc = codsuc;
	}

	public String getCodunineg() {
		return codunineg;
	}

	public void setCodunineg(String codunineg) {
		this.codunineg = codunineg;
	}

	public String getCompenslm() {
		return compenslm;
	}

	public void setCompenslm(String compenslm) {
		this.compenslm = compenslm;
	}

	public double getCpendiente() {
		return cpendiente;
	}

	public void setCpendiente(double cpendiente) {
		this.cpendiente = cpendiente;
	}

	public double getCsubtotal() {
		return csubtotal;
	}

	public void setCsubtotal(double csubtotal) {
		this.csubtotal = csubtotal;
	}

	public double getCtotal() {
		return ctotal;
	}

	public void setCtotal(double ctotal) {
		this.ctotal = ctotal;
	}

	public String getDdescdisp() {
		return ddescdisp;
	}

	public void setDdescdisp(String ddescdisp) {
		this.ddescdisp = ddescdisp;
	}

	public String getDdesctom() {
		return ddesctom;
	}

	public void setDdesctom(String ddesctom) {
		this.ddesctom = ddesctom;
	}

	public String getDescepago() {
		return descepago;
	}

	public void setDescepago(String descepago) {
		this.descepago = descepago;
	}

	public String getDimpuesto() {
		return dimpuesto;
	}

	public void setDimpuesto(String dimpuesto) {
		this.dimpuesto = dimpuesto;
	}

	public double getDpendiente() {
		return dpendiente;
	}

	public void setDpendiente(double dpendiente) {
		this.dpendiente = dpendiente;
	}

	public double getDsubtotal() {
		return dsubtotal;
	}

	public void setDsubtotal(double dsubtotal) {
		this.dsubtotal = dsubtotal;
	}

	public double getDtotal() {
		return dtotal;
	}

	public void setDtotal(double dtotal) {
		this.dtotal = dtotal;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getFechabatch() {
		return fechabatch;
	}

	public void setFechabatch(String fechabatch) {
		this.fechabatch = fechabatch;
	}

	public String getFechagrab() {
		return fechagrab;
	}

	public void setFechagrab(String fechagrab) {
		this.fechagrab = fechagrab;
	}

	public String getFechaimp() {
		return fechaimp;
	}

	public void setFechaimp(String fechaimp) {
		this.fechaimp = fechaimp;
	}

	public String getFechalm() {
		return fechalm;
	}

	public void setFechalm(String fechalm) {
		this.fechalm = fechalm;
	}

	public String getFechavenc() {
		return fechavenc;
	}

	public void setFechavenc(String fechavenc) {
		this.fechavenc = fechavenc;
	}

	public String getHechopor() {
		return hechopor;
	}

	public void setHechopor(String hechopor) {
		this.hechopor = hechopor;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public int getNobatch() {
		return nobatch;
	}

	public void setNobatch(int nobatch) {
		this.nobatch = nobatch;
	}

	public int getNofactura() {
		return nofactura;
	}

	public void setNofactura(int nofactura) {
		this.nofactura = nofactura;
	}

	public String getNomcli() {
		return nomcli;
	}

	public void setNomcli(String nomcli) {
		this.nomcli = nomcli;
	}

	public String getNomcomp() {
		return nomcomp;
	}

	public void setNomcomp(String nomcomp) {
		this.nomcomp = nomcomp;
	}

	public String getNomsuc() {
		return nomsuc;
	}

	public void setNomsuc(String nomsuc) {
		this.nomsuc = nomsuc;
	}

	public int getNumorden() {
		return numorden;
	}

	public void setNumorden(int numorden) {
		this.numorden = numorden;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getPantalla() {
		return pantalla;
	}

	public void setPantalla(String pantalla) {
		this.pantalla = pantalla;
	}

	public String getPartida() {
		return partida;
	}

	public void setPartida(String partida) {
		this.partida = partida;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public BigDecimal getTasa() {
		return tasa;
	}

	public void setTasa(BigDecimal tasa) {
		this.tasa = tasa;
	}

	public String getTipofactura() {
		return tipofactura;
	}

	public void setTipofactura(String tipofactura) {
		this.tipofactura = tipofactura;
	}

	public String getTipoorden() {
		return tipoorden;
	}

	public void setTipoorden(String tipoorden) {
		this.tipoorden = tipoorden;
	}

	public String getTipopago() {
		return tipopago;
	}

	public void setTipopago(String tipopago) {
		this.tipopago = tipopago;
	}

	public String getUnineg() {
		return unineg;
	}

	public void setUnineg(String unineg) {
		this.unineg = unineg;
	}
	public String getDesctpago() {
		return desctpago;
	}
	public void setDesctpago(String desctpago) {
		this.desctpago = desctpago;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public double getEquiv() {
		return equiv;
	}
	public void setEquiv(double equiv) {
		this.equiv = equiv;
	}
	public double getMontoAplicar() {
		return montoAplicar;
	}
	public void setMontoAplicar(double montoAplicar) {
		this.montoAplicar = montoAplicar;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public double getPendiente() {
		return pendiente;
	}
	public void setPendiente(double pendiente) {
		this.pendiente = pendiente;
	}
	public double getImpuesto() {
		return impuesto;
	}
	public void setImpuesto(double impuesto) {
		this.impuesto = impuesto;
	}
	public double getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}
}

package com.casapellas.entidades;

// Generated Aug 2, 2011 11:36:52 AM by Hibernate Tools 3.2.2.GA

import java.math.BigDecimal;
import java.util.Date;

/**
 * Salida generated by hbm2java
 */
public class Salida implements java.io.Serializable {

	private static final long serialVersionUID = -5934499167505657292L;
	private SalidaId id;
	private int codsol;
	private int codcaj;
	private int codapr;
	private String concepto;
	private String estado;
	private Date fsolicitud;
	private Date faproba;
	private Date fproceso;
	private String moneda;
	private BigDecimal monto;
	private String operacion;
	private BigDecimal tasa;
	private BigDecimal equiv;
	private String refer1;
	private String refer2;
	private String refer3;
	private String refer4;

	public Salida() {
	}

	public Salida(SalidaId id, int codsol, int codcaj, int codapr,
			String concepto, String estado, Date fsolicitud, Date faproba,
			Date fproceso, String moneda, BigDecimal monto, String operacion,
			BigDecimal tasa, BigDecimal equiv) {
		this.id = id;
		this.codsol = codsol;
		this.codcaj = codcaj;
		this.codapr = codapr;
		this.concepto = concepto;
		this.estado = estado;
		this.fsolicitud = fsolicitud;
		this.faproba = faproba;
		this.fproceso = fproceso;
		this.moneda = moneda;
		this.monto = monto;
		this.operacion = operacion;
		this.tasa = tasa;
		this.equiv = equiv;
	}

	public Salida(SalidaId id, int codsol, int codcaj, int codapr,
			String concepto, String estado, Date fsolicitud, Date faproba,
			Date fproceso, String moneda, BigDecimal monto, String operacion,
			BigDecimal tasa, BigDecimal equiv, String refer1, String refer2,
			String refer3, String refer4) {
		this.id = id;
		this.codsol = codsol;
		this.codcaj = codcaj;
		this.codapr = codapr;
		this.concepto = concepto;
		this.estado = estado;
		this.fsolicitud = fsolicitud;
		this.faproba = faproba;
		this.fproceso = fproceso;
		this.moneda = moneda;
		this.monto = monto;
		this.operacion = operacion;
		this.tasa = tasa;
		this.equiv = equiv;
		this.refer1 = refer1;
		this.refer2 = refer2;
		this.refer3 = refer3;
		this.refer4 = refer4;
	}

	public SalidaId getId() {
		return this.id;
	}

	public void setId(SalidaId id) {
		this.id = id;
	}

	public int getCodsol() {
		return this.codsol;
	}

	public void setCodsol(int codsol) {
		this.codsol = codsol;
	}

	public int getCodcaj() {
		return this.codcaj;
	}

	public void setCodcaj(int codcaj) {
		this.codcaj = codcaj;
	}

	public int getCodapr() {
		return this.codapr;
	}

	public void setCodapr(int codapr) {
		this.codapr = codapr;
	}

	public String getConcepto() {
		return this.concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFsolicitud() {
		return this.fsolicitud;
	}

	public void setFsolicitud(Date fsolicitud) {
		this.fsolicitud = fsolicitud;
	}

	public Date getFaproba() {
		return this.faproba;
	}

	public void setFaproba(Date faproba) {
		this.faproba = faproba;
	}

	public Date getFproceso() {
		return this.fproceso;
	}

	public void setFproceso(Date fproceso) {
		this.fproceso = fproceso;
	}

	public String getMoneda() {
		return this.moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public BigDecimal getMonto() {
		return this.monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public String getOperacion() {
		return this.operacion;
	}

	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}

	public BigDecimal getTasa() {
		return this.tasa;
	}

	public void setTasa(BigDecimal tasa) {
		this.tasa = tasa;
	}

	public BigDecimal getEquiv() {
		return this.equiv;
	}

	public void setEquiv(BigDecimal equiv) {
		this.equiv = equiv;
	}

	public String getRefer1() {
		return this.refer1;
	}

	public void setRefer1(String refer1) {
		this.refer1 = refer1;
	}

	public String getRefer2() {
		return this.refer2;
	}

	public void setRefer2(String refer2) {
		this.refer2 = refer2;
	}

	public String getRefer3() {
		return this.refer3;
	}

	public void setRefer3(String refer3) {
		this.refer3 = refer3;
	}

	public String getRefer4() {
		return this.refer4;
	}

	public void setRefer4(String refer4) {
		this.refer4 = refer4;
	}

}

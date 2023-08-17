package com.casapellas.donacion.entidades;

import java.math.BigDecimal;
import java.util.Date;

public class DncResumenDonacion {

	private int beneficiariocodigo;
	private String beneficiarionombre;
	private String moneda ; 
	private BigDecimal montototal;
	private int cantidadtransacciones;
	private Date fechainicial;
	private Date fechafinal;
	private String formadepago;
	private BigDecimal montoefectivo;
	private BigDecimal montotarjetacred;
	private BigDecimal montootros;
	
	private BigDecimal montorecibido;
	private BigDecimal montoneto;
	private int nobatchaprobacion; 
	private int nodocumentoaprobacion;
	private String tipodocumento;
	private int noarqueo;
	private int caid;
	private String codcomp ;
	
	public DncResumenDonacion(int beneficiariocodigo,
			String beneficiarionombre, String moneda, BigDecimal montototal,
			int cantidadtransacciones, Date fechainicial, String formadepago,
			BigDecimal montorecibido, BigDecimal montoneto,
			int nobatchaprobacion, int nodocumentoaprobacion,
			String tipodocumento, int noarqueo, int caid, String codcomp) {
		super();
		this.beneficiariocodigo = beneficiariocodigo;
		this.beneficiarionombre = beneficiarionombre;
		this.moneda = moneda;
		this.montototal = montototal;
		this.cantidadtransacciones = cantidadtransacciones;
		this.fechainicial = fechainicial;
		this.formadepago = formadepago;
		this.montorecibido = montorecibido;
		this.montoneto = montoneto;
		this.nobatchaprobacion = nobatchaprobacion;
		this.nodocumentoaprobacion = nodocumentoaprobacion;
		this.tipodocumento = tipodocumento;
		this.noarqueo = noarqueo;
		this.caid = caid;
		this.codcomp = codcomp;
	}

	public DncResumenDonacion(int beneficiariocodigo,
			String beneficiarionombre, String moneda, BigDecimal montototal,
			int cantidadtransacciones, Date fechainicial, Date fechafinal,
			String formadepago, BigDecimal montoefectivo,
			BigDecimal montotarjetacred, BigDecimal montootros) {
		super();
		this.beneficiariocodigo = beneficiariocodigo;
		this.beneficiarionombre = beneficiarionombre;
		this.moneda = moneda;
		this.montototal = montototal;
		this.cantidadtransacciones = cantidadtransacciones;
		this.fechainicial = fechainicial;
		this.fechafinal = fechafinal;
		this.formadepago = formadepago;
		this.montoefectivo = montoefectivo;
		this.montotarjetacred = montotarjetacred;
		this.montootros = montootros;
	}
	public int getBeneficiariocodigo() {
		return beneficiariocodigo;
	}
	public void setBeneficiariocodigo(int beneficiariocodigo) {
		this.beneficiariocodigo = beneficiariocodigo;
	}
	public String getBeneficiarionombre() {
		return beneficiarionombre;
	}
	public void setBeneficiarionombre(String beneficiarionombre) {
		this.beneficiarionombre = beneficiarionombre;
	}
	public String getMoneda() {
		return moneda;
	}
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}
	public BigDecimal getMontototal() {
		return montototal;
	}
	public void setMontototal(BigDecimal montototal) {
		this.montototal = montototal;
	}
	public int getCantidadtransacciones() {
		return cantidadtransacciones;
	}
	public void setCantidadtransacciones(int cantidadtransacciones) {
		this.cantidadtransacciones = cantidadtransacciones;
	}
	public Date getFechainicial() {
		return fechainicial;
	}
	public void setFechainicial(Date fechainicial) {
		this.fechainicial = fechainicial;
	}
	public Date getFechafinal() {
		return fechafinal;
	}
	public void setFechafinal(Date fechafinal) {
		this.fechafinal = fechafinal;
	}
	public String getFormadepago() {
		return formadepago;
	}
	public void setFormadepago(String formadepago) {
		this.formadepago = formadepago;
	}
	public BigDecimal getMontoefectivo() {
		return montoefectivo;
	}
	public void setMontoefectivo(BigDecimal montoefectivo) {
		this.montoefectivo = montoefectivo;
	}
	public BigDecimal getMontotarjetacred() {
		return montotarjetacred;
	}
	public void setMontotarjetacred(BigDecimal montotarjetacred) {
		this.montotarjetacred = montotarjetacred;
	}
	public BigDecimal getMontootros() {
		return montootros;
	}
	public void setMontootros(BigDecimal montootros) {
		this.montootros = montootros;
	}
	 
	public String toString1() {
		return "DncResumenDonacion [beneficiariocodigo=" + beneficiariocodigo
				+ ", beneficiarionombre=" + beneficiarionombre + ", moneda="
				+ moneda + ", montototal=" + montototal
				+ ", cantidadtransacciones=" + cantidadtransacciones
				+ ", fechainicial=" + fechainicial + ", fechafinal="
				+ fechafinal + ", formadepago=" + formadepago
				+ ", montoefectivo=" + montoefectivo + ", montotarjetacred="
				+ montotarjetacred + ", montootros=" + montootros + "]";
	}
	@Override
	public String toString() {
		return "DncResumenDonacion [beneficiariocodigo=" + beneficiariocodigo
				+ ", beneficiarionombre=" + beneficiarionombre + ", moneda="
				+ moneda + ", montototal=" + montototal
				+ ", cantidadtransacciones=" + cantidadtransacciones
				+ ", fechainicial=" + fechainicial + ", formadepago="
				+ formadepago + ", montorecibido=" + montorecibido
				+ ", montoneto=" + montoneto + ", nobatchaprobacion="
				+ nobatchaprobacion + ", nodocumentoaprobacion="
				+ nodocumentoaprobacion + ", tipodocumento=" + tipodocumento
				+ "]";
	}
	
	public BigDecimal getMontorecibido() {
		return montorecibido;
	}
	public void setMontorecibido(BigDecimal montorecibido) {
		this.montorecibido = montorecibido;
	}
	public BigDecimal getMontoneto() {
		return montoneto;
	}
	public void setMontoneto(BigDecimal montoneto) {
		this.montoneto = montoneto;
	}
	public int getNobatchaprobacion() {
		return nobatchaprobacion;
	}
	public void setNobatchaprobacion(int nobatchaprobacion) {
		this.nobatchaprobacion = nobatchaprobacion;
	}
	public int getNodocumentoaprobacion() {
		return nodocumentoaprobacion;
	}
	public void setNodocumentoaprobacion(int nodocumentoaprobacion) {
		this.nodocumentoaprobacion = nodocumentoaprobacion;
	}
	public String getTipodocumento() {
		return tipodocumento;
	}
	public void setTipodocumento(String tipodocumento) {
		this.tipodocumento = tipodocumento;
	}

	public int getNoarqueo() {
		return noarqueo;
	}

	public void setNoarqueo(int noarqueo) {
		this.noarqueo = noarqueo;
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
	
}

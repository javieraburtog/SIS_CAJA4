package com.casapellas.util;

import java.math.BigDecimal;

/**
 * @author Carlos Hernandez
 * Descripcion: Permite asignar valores para la liquidacion de depositos en bancos por pagos con cheques.
 *
 */
public class LiquidacionCheque {
	
	private int caid;
	
	private String codsuc;
	
	private String codcomp;
	
	private String codigoBanco;
	
	private String nombreBanco;
	
	private String referenciaBanco;
	
	private int cantidadCheque;
	
	private int icodigobanco;
	
	private String montototal;
	
	private BigDecimal btotalBanco;

	private String moneda;
	
	private String nommoneda;
	
	private String metodopago;
	
	private String conciliaauto;
	
	public LiquidacionCheque() {
	}

	public LiquidacionCheque(int caid, String codsuc, String codcomp,
			String codigoBanco, String nombreBanco, String referenciaBanco,
			int cantidadCheque, int icodigobanco, String montototal,
			BigDecimal btotalBanco, String moneda, String nommoneda,
			String metodopago, String conciliaauto) {
		super();
		this.caid = caid;
		this.codsuc = codsuc;
		this.codcomp = codcomp;
		this.codigoBanco = codigoBanco;
		this.nombreBanco = nombreBanco;
		this.referenciaBanco = referenciaBanco;
		this.cantidadCheque = cantidadCheque;
		this.icodigobanco = icodigobanco;
		this.montototal = montototal;
		this.btotalBanco = btotalBanco;
		this.moneda = moneda;
		this.nommoneda = nommoneda;
		this.metodopago = metodopago;
		this.conciliaauto = conciliaauto;
	}

	public int getCaid() {
		return caid;
	}

	public void setCaid(int caid) {
		this.caid = caid;
	}

	public String getCodsuc() {
		return codsuc;
	}

	public void setCodsuc(String codsuc) {
		this.codsuc = codsuc;
	}

	public String getCodcomp() {
		return codcomp;
	}

	public void setCodcomp(String codcomp) {
		this.codcomp = codcomp;
	}

	public String getCodigoBanco() {
		return codigoBanco;
	}

	public void setCodigoBanco(String codigoBanco) {
		this.codigoBanco = codigoBanco;
	}

	public String getNombreBanco() {
		return nombreBanco;
	}

	public void setNombreBanco(String nombreBanco) {
		this.nombreBanco = nombreBanco;
	}

	public String getReferenciaBanco() {
		return referenciaBanco;
	}

	public void setReferenciaBanco(String referenciaBanco) {
		this.referenciaBanco = referenciaBanco;
	}

	public int getCantidadCheque() {
		return cantidadCheque;
	}

	public void setCantidadCheque(int cantidadCheque) {
		this.cantidadCheque = cantidadCheque;
	}

	public String getMontototal() {
		return montototal;
	}

	public void setMontototal(String montototal) {
		this.montototal = montototal;
	}

	public BigDecimal getBtotalBanco() {
		return btotalBanco;
	}

	public void setBtotalBanco(BigDecimal btotalBanco) {
		this.btotalBanco = btotalBanco;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public String getNommoneda() {
		return nommoneda;
	}

	public void setNommoneda(String nommoneda) {
		this.nommoneda = nommoneda;
	}

	public String getMetodopago() {
		return metodopago;
	}

	public void setMetodopago(String metodopago) {
		this.metodopago = metodopago;
	}

	public int getIcodigobanco() {
		return icodigobanco;
	}

	public void setIcodigobanco(int icodigobanco) {
		this.icodigobanco = icodigobanco;
	}

	public String getConciliaauto() {
		return conciliaauto;
	}

	public void setConciliaauto(String conciliaauto) {
		this.conciliaauto = conciliaauto;
	}
	
	
}

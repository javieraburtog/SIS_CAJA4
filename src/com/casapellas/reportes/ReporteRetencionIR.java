package com.casapellas.reportes;

import java.math.BigDecimal;
import java.util.Date;

public class ReporteRetencionIR {
	
	
	private int codigocaja;
	private int numerorecibo;
	private String codigocompania;
	private String codunineg;
	private String tiporecibo;
	private String tipodocumento;
	private String moneda;
	private int liquidacion;
	private int nodocumento;
	private BigDecimal tasa;
	private BigDecimal tasaoficialdia;
	private BigDecimal tasafactura;
	private BigDecimal montopago;
	private BigDecimal montocntdo;
	private BigDecimal montoabono;
	private BigDecimal montoexonerado;
	private BigDecimal montoexento;
	private Date fecha;
	private BigDecimal comision;
	
	private BigDecimal totalsininva;
	private BigDecimal totaliva;
	private BigDecimal comisionvta;
	private BigDecimal comisioniva;
	private BigDecimal aretencionvta;
	private BigDecimal aretencioniva;
	private BigDecimal retencionvta;
	private BigDecimal retencioniva;
	private BigDecimal totalretencion;
	
	private BigDecimal prcntCmsIr;
	private BigDecimal prcntCmsIva;
	private String noafiliado;
	
	
	public ReporteRetencionIR() {
		totalsininva = BigDecimal.ZERO;
		totaliva = BigDecimal.ZERO;
		comisionvta = BigDecimal.ZERO;
		comisioniva = BigDecimal.ZERO;
		aretencionvta = BigDecimal.ZERO;
		aretencioniva = BigDecimal.ZERO;
		retencionvta = BigDecimal.ZERO;
		retencioniva = BigDecimal.ZERO;
		totalretencion = BigDecimal.ZERO;
	}
	public String getCodunineg() {
		return codunineg;
	}
	public void setCodunineg(String codunineg) {
		this.codunineg = codunineg;
	}
	public String getTiporecibo() {
		return tiporecibo;
	}
	public void setTiporecibo(String tiporecibo) {
		this.tiporecibo = tiporecibo;
	}
	public String getMoneda() {
		return moneda;
	}
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}
	public int getLiquidacion() {
		return liquidacion;
	}
	public void setLiquidacion(int liquidacion) {
		this.liquidacion = liquidacion;
	}
	public int getNodocumento() {
		return nodocumento;
	}
	public void setNodocumento(int nodocumento) {
		this.nodocumento = nodocumento;
	}
	public BigDecimal getTasa() {
		return tasa;
	}
	public void setTasa(BigDecimal tasa) {
		this.tasa = tasa;
	}
	public BigDecimal getMontocntdo() {
		return montocntdo;
	}
	public void setMontocntdo(BigDecimal montocntdo) {
		this.montocntdo = montocntdo;
	}
	public BigDecimal getMontoabono() {
		return montoabono;
	}
	public void setMontoabono(BigDecimal montoabono) {
		this.montoabono = montoabono;
	}
	public BigDecimal getMontoexonerado() {
		return montoexonerado;
	}
	public void setMontoexonerado(BigDecimal montoexonerado) {
		this.montoexonerado = montoexonerado;
	}
	public BigDecimal getMontoexento() {
		return montoexento;
	}
	public void setMontoexento(BigDecimal montoexento) {
		this.montoexento = montoexento;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public String getTipodocumento() {
		return tipodocumento;
	}
	public void setTipodocumento(String tipodocumento) {
		this.tipodocumento = tipodocumento;
	}
	public BigDecimal getComision() {
		return comision;
	}
	public void setComision(BigDecimal comision) {
		this.comision = comision;
	}
	public BigDecimal getTotalsininva() {
		return totalsininva;
	}
	public void setTotalsininva(BigDecimal totalsininva) {
		this.totalsininva = totalsininva.setScale(2,BigDecimal.ROUND_HALF_UP);
	}
	public BigDecimal getTotaliva() {
		return totaliva;
	}
	public void setTotaliva(BigDecimal totaliva) {
		this.totaliva = totaliva.setScale(2,BigDecimal.ROUND_HALF_UP);
	}
	public BigDecimal getComisionvta() {
		return comisionvta;
	}
	public void setComisionvta(BigDecimal comisionvta) {
		this.comisionvta = comisionvta.setScale(2,BigDecimal.ROUND_HALF_UP);
	}
	public BigDecimal getComisioniva() {
		return comisioniva;
	}
	public void setComisioniva(BigDecimal comisioniva) {
		this.comisioniva = comisioniva.setScale(2,BigDecimal.ROUND_HALF_UP);
	}
	public BigDecimal getAretencionvta() {
		return aretencionvta;
	}
	public void setAretencionvta(BigDecimal aretencionvta) {
		this.aretencionvta = aretencionvta.setScale(2,BigDecimal.ROUND_HALF_UP);
	}
	public BigDecimal getAretencioniva() {
		return aretencioniva;
	}
	public void setAretencioniva(BigDecimal aretencioniva) {
		this.aretencioniva = aretencioniva.setScale(2,BigDecimal.ROUND_HALF_UP);
	}
	public BigDecimal getRetencionvta() {
		return retencionvta;
	}
	public void setRetencionvta(BigDecimal retencionvta) {
		this.retencionvta = retencionvta.setScale(2,BigDecimal.ROUND_HALF_UP);
	}
	public BigDecimal getRetencioniva() {
		return retencioniva;
	}
	public void setRetencioniva(BigDecimal retencioniva) {
		this.retencioniva = retencioniva.setScale(2,BigDecimal.ROUND_HALF_UP);
	}
	public BigDecimal getTotalretencion() {
		return totalretencion;
	}
	public void setTotalretencion(BigDecimal totalretencion) {
		this.totalretencion = totalretencion.setScale(2,BigDecimal.ROUND_HALF_UP);
	}
	
	public String toString(){
		String sToString = "";
		
		sToString += moneda + ", "+ tipodocumento + ", "+ nodocumento + " " + 
		 totalsininva +", "+
		 totaliva +", "+
		 comisionvta +", "+
		 comisioniva +", "+
		 aretencionvta +", "+
		 aretencioniva +", "+
		 retencionvta +", "+
		 retencioniva +", "+
		 totalretencion; 
		
		return sToString;
	}
	public BigDecimal getTasaoficialdia() {
		return tasaoficialdia;
	}
	public void setTasaoficialdia(BigDecimal tasaoficialdia) {
		this.tasaoficialdia = tasaoficialdia;
	}
	public int getNumerorecibo() {
		return numerorecibo;
	}
	public void setNumerorecibo(int numerorecibo) {
		this.numerorecibo = numerorecibo;
	}
	public BigDecimal getMontopago() {
		return montopago;
	}
	public void setMontopago(BigDecimal montopago) {
		this.montopago = montopago;
	}
	public BigDecimal getPrcntCmsIr() {
		return prcntCmsIr;
	}
	public void setPrcntCmsIr(BigDecimal prcntCmsIr) {
		this.prcntCmsIr = prcntCmsIr;
	}
	public BigDecimal getPrcntCmsIva() {
		return prcntCmsIva;
	}
	public void setPrcntCmsIva(BigDecimal prcntCmsIva) {
		this.prcntCmsIva = prcntCmsIva;
	}
	public String getNoafiliado() {
		return noafiliado;
	}
	public void setNoafiliado(String noafiliado) {
		this.noafiliado = noafiliado;
	}
	public BigDecimal getTasafactura() {
		return tasafactura;
	}
	public void setTasafactura(BigDecimal tasafactura) {
		this.tasafactura = tasafactura;
	}
	public int getCodigocaja() {
		return codigocaja;
	}
	public void setCodigocaja(int codigocaja) {
		this.codigocaja = codigocaja;
	}
	public String getCodigocompania() {
		return codigocompania;
	}
	public void setCodigocompania(String codigocompania) {
		this.codigocompania = codigocompania;
	}
}

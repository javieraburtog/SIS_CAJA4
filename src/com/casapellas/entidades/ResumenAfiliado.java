package com.casapellas.entidades;

import java.math.BigDecimal;

/**
 * CASA PELLAS S.A.
 * Creado por.........: Carlos Manuel Hernández Morrison
 * Fecha de Creación..: 14/05/2009
 * Última modificación: Carlos Manuel Hernández Morrison
 * Modificado por.....:	09/07/2010
 * Descripcion:.......: Carga los datos para mostrar un resumen por afiliado 
 * 						y permitir asignarle la referencia del banco.
 */
public class ResumenAfiliado implements java.io.Serializable { 

	private static final long serialVersionUID = 8607068741198711944L;

	private String codigo;
	
	private String nombre;
	
	private String referencia;
	
	private String montoneto;
	
	private String montoxcomision;
	
	private String montototal;
	
	private String comision;
	
	private String moneda;
	
	private double dmontoneto;
	
	private double dmontoxcomision;
	
	private double dmontototal;
	
	private BigDecimal montoRetencion;
	
	private String afiliado;
	
	private String vmanual;
	
	private String scodvmanual;
	
	private int icaidpos;	
	
	private BigDecimal montoComisionable;
	
	private BigDecimal ivaComision;
	
	private String sivacomision;
	
	private int codigobancopos;
	
	private BigDecimal porcentajecomision;
	private BigDecimal porcentajeretencion;
	 
	private String marcatarjeta;
	private String codigomarcatarjeta;
	private int liquidarpormarca;
	
	private int depositoctatransitoria;
	
	public ResumenAfiliado(){
		
	}

	public ResumenAfiliado(String codigo, String nombre, String referencia,
			String montoneto, String montoxcomision, String montototal,
			String comision, String moneda, double dmontoneto,
			double dmontoxcomision, double dmontototal,
			BigDecimal montoRetencion, String afiliado, String vmanual,
			String scodvmanual, int icaidpos, BigDecimal montoComisionable,
			BigDecimal ivaComision, String sivacomision, int codigobancopos) {
		super();
		this.codigo = codigo;
		this.nombre = nombre;
		this.referencia = referencia;
		this.montoneto = montoneto;
		this.montoxcomision = montoxcomision;
		this.montototal = montototal;
		this.comision = comision;
		this.moneda = moneda;
		this.dmontoneto = dmontoneto;
		this.dmontoxcomision = dmontoxcomision;
		this.dmontototal = dmontototal;
		this.montoRetencion = montoRetencion;
		this.afiliado = afiliado;
		this.vmanual = vmanual;
		this.scodvmanual = scodvmanual;
		this.icaidpos = icaidpos;
		this.montoComisionable = montoComisionable;
		this.ivaComision = ivaComision;
		this.sivacomision = sivacomision;
		this.codigobancopos = codigobancopos;
	}

	public ResumenAfiliado(String codigo, String nombre, String referencia, String montoneto, String montoxcomision, String montototal, String comision) {
		super();
		this.codigo = codigo;
		this.nombre = nombre;
		this.referencia = referencia;
		this.montoneto = montoneto;
		this.montoxcomision = montoxcomision;
		this.montototal = montototal;
		this.comision = comision;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getComision() {
		return comision;
	}

	public void setComision(String comision) {
		this.comision = comision;
	}

	public String getMontoneto() {
		return montoneto;
	}

	public void setMontoneto(String montoneto) {
		this.montoneto = montoneto;
	}

	public String getMontototal() {
		return montototal;
	}

	public void setMontototal(String montototal) {
		this.montototal = montototal;
	}

	public String getMontoxcomision() {
		return montoxcomision;
	}

	public void setMontoxcomision(String montoxcomision) {
		this.montoxcomision = montoxcomision;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public double getDmontoneto() {
		return dmontoneto;
	}

	public void setDmontoneto(double dmontoneto) {
		this.dmontoneto = dmontoneto;
	}

	public double getDmontototal() {
		return dmontototal;
	}

	public void setDmontototal(double dmontototal) {
		this.dmontototal = dmontototal;
	}

	public double getDmontoxcomision() {
		return dmontoxcomision;
	}

	public void setDmontoxcomision(double dmontoxcomision) {
		this.dmontoxcomision = dmontoxcomision;
	}

	public BigDecimal getMontoRetencion() {
		return montoRetencion;
	}

	public void setMontoRetencion(BigDecimal montoRetencion) {
		this.montoRetencion = montoRetencion;
	}

	public String getAfiliado() {
		return afiliado;
	}

	public void setAfiliado(String afiliado) {
		this.afiliado = afiliado;
	}

	public String getScodvmanual() {
		return scodvmanual;
	}

	public void setScodvmanual(String scodvmanual) {
		this.scodvmanual = scodvmanual;
	}

	public String getVmanual() {
		return vmanual;
	}

	public void setVmanual(String vmanual) {
		this.vmanual = vmanual;
	}

	public int getIcaidpos() {
		return icaidpos;
	}

	public void setIcaidpos(int icaidpos) {
		this.icaidpos = icaidpos;
	}

	public BigDecimal getMontoComisionable() {
		return montoComisionable;
	}

	public void setMontoComisionable(BigDecimal montoComisionable) {
		this.montoComisionable = montoComisionable;
	}

	public BigDecimal getIvaComision() {
		return ivaComision;
	}

	public void setIvaComision(BigDecimal ivaComision) {
		this.ivaComision = ivaComision;
	}

	public String getSivacomision() {
		return sivacomision;
	}

	public void setSivacomision(String sivacomision) {
		this.sivacomision = sivacomision;
	}

	/**
	 * @return the codigobancopos
	 */
	public int getCodigobancopos() {
		return codigobancopos;
	}

	/**
	 * @param codigobancopos the codigobancopos to set
	 */
	public void setCodigobancopos(int codigobancopos) {
		this.codigobancopos = codigobancopos;
	}

	public BigDecimal getPorcentajecomision() {
		return porcentajecomision;
	}

	public void setPorcentajecomision(BigDecimal porcentajecomision) {
		this.porcentajecomision = porcentajecomision;
	}

	public BigDecimal getPorcentajeretencion() {
		return porcentajeretencion;
	}

	public void setPorcentajeretencion(BigDecimal porcentajeretencion) {
		this.porcentajeretencion = porcentajeretencion;
	}

	public String getMarcatarjeta() {
		return marcatarjeta;
	}

	public void setMarcatarjeta(String marcatarjeta) {
		this.marcatarjeta = marcatarjeta;
	}

	public String getCodigomarcatarjeta() {
		return codigomarcatarjeta;
	}

	public void setCodigomarcatarjeta(String codigomarcatarjeta) {
		this.codigomarcatarjeta = codigomarcatarjeta;
	}

	public int getLiquidarpormarca() {
		return liquidarpormarca;
	}

	public void setLiquidarpormarca(int liquidarpormarca) {
		this.liquidarpormarca = liquidarpormarca;
	}

	public int getDepositoctatransitoria() {
		return depositoctatransitoria;
	}

	public void setDepositoctatransitoria(int depositoctatransitoria) {
		this.depositoctatransitoria = depositoctatransitoria;
	}
 
}

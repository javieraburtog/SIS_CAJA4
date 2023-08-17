package com.casapellas.entidades;

/**
 * 
 * @author Luis Fonseca
 *
 */
public class CajaParametro {
	private String codParametro;
	private Integer noCaja;
	private String codigo;
	private String descripcion;
	private Double valorNumerico;
	private String valorAlfanumerico;
	
	public String getCodParametro() {
		return codParametro;
	}
	public void setCodParametro(String codParametro) {
		this.codParametro = codParametro;
	}
	public Integer getNoCaja() {
		return noCaja;
	}
	public void setNoCaja(Integer noCaja) {
		this.noCaja = noCaja;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Double getValorNumerico() {
		return valorNumerico;
	}
	public void setValorNumerico(Double valorNumerico) {
		this.valorNumerico = valorNumerico;
	}
	public String getValorAlfanumerico() {
		return valorAlfanumerico;
	}
	public void setValorAlfanumerico(String valorAlfanumerico) {
		this.valorAlfanumerico = valorAlfanumerico;
	}
		
}

package com.casapellas.util.bt.entidades;

public class CuentaEstado {
	
	private int codigo;
    private String descripcion;
	/**
	 * @return the codigo
	 */
	public int getCodigo() {
		return codigo;
	}
	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	/**
	 * 
	 */
	public CuentaEstado() {
		this.codigo = 0;
		this.descripcion = "";
	}
	/**
	 * @param codigo
	 * @param descripcion
	 */
	public CuentaEstado(int codigo, String descripcion) {
		this.codigo = codigo;
		this.descripcion = descripcion;
	}
}

package com.casapellas.util.bt.entidades;

public class ByteResponse {
	private Integer codigo;
	private String descripcion;
	private boolean errorCore;

	public Integer getCodigo() {
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public boolean getErrorCore() {
		return errorCore;
	}
	public void setErrorCore(boolean errorCore) {
		this.errorCore = errorCore;
	}
}

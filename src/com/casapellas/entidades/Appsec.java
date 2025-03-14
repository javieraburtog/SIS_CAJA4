package com.casapellas.entidades;

// Generated 2/07/2009 11:36:12 by Hibernate Tools 3.2.0.b9

/**
 * Appsec generated by hbm2java
 */
public class Appsec implements java.io.Serializable {

	private AppsecId id;

	private Seccion seccion;

	private Aplicacion aplicacion;

	private String activo;

	private Integer orden;

	private String cont;

	public Appsec() {
	}

	public Appsec(AppsecId id, Seccion seccion, Aplicacion aplicacion) {
		this.id = id;
		this.seccion = seccion;
		this.aplicacion = aplicacion;
	}

	public Appsec(AppsecId id, Seccion seccion, Aplicacion aplicacion,
			String activo, Integer orden, String cont) {
		this.id = id;
		this.seccion = seccion;
		this.aplicacion = aplicacion;
		this.activo = activo;
		this.orden = orden;
		this.cont = cont;
	}

	public AppsecId getId() {
		return this.id;
	}

	public void setId(AppsecId id) {
		this.id = id;
	}

	public Seccion getSeccion() {
		return this.seccion;
	}

	public void setSeccion(Seccion seccion) {
		this.seccion = seccion;
	}

	public Aplicacion getAplicacion() {
		return this.aplicacion;
	}

	public void setAplicacion(Aplicacion aplicacion) {
		this.aplicacion = aplicacion;
	}

	public String getActivo() {
		return this.activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

	public Integer getOrden() {
		return this.orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	public String getCont() {
		return this.cont;
	}

	public void setCont(String cont) {
		this.cont = cont;
	}

}

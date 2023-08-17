/**
 * Nombre       : GCPJPA
 * Autor        : Luis Fonseca
 * Nombre Clase : TipoDocumentoFinanciamiento.java
 * 
 */
package com.casapellas.entidades;

/**
 * <pre>
 * Nombre         : GCPJPA
 * Nombre Clase   : TipoDocumentoFinanciamiento.java
 * Objetivo       : Obtener la estructura de datos de Tipo de documento financiamiento
 * Autor          : Luis Fonseca
 * Fecha Creación : Aug 19, 2020
 * Modificado Por :
 * </pre>
 *
 */
public class TipoDocumentoFinanciamiento {
	private Integer id;
	private String cod_documento;
	private String tipo_agrupacion;
	private Integer orden_procesamiento;
	private String cod_compania;
	private String clase_contable;
	
	public String getCod_compania() {
		return cod_compania;
	}
	public void setCod_compania(String cod_compania) {
		this.cod_compania = cod_compania;
	}
	public String getClase_contable() {
		return clase_contable;
	}
	public void setClase_contable(String clase_contable) {
		this.clase_contable = clase_contable;
	}
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the cod_documento
	 */
	public String getCod_documento() {
		return cod_documento;
	}
	/**
	 * @param cod_documento the cod_documento to set
	 */
	public void setCod_documento(String cod_documento) {
		this.cod_documento = cod_documento;
	}
	/**
	 * @return the tipo_agrupacion
	 */
	public String getTipo_agrupacion() {
		return tipo_agrupacion;
	}
	/**
	 * @param tipo_agrupacion the tipo_agrupacion to set
	 */
	public void setTipo_agrupacion(String tipo_agrupacion) {
		this.tipo_agrupacion = tipo_agrupacion;
	}
	/**
	 * @return the orden_procesamiento
	 */
	public Integer getOrden_procesamiento() {
		return orden_procesamiento;
	}
	/**
	 * @param orden_procesamiento the orden_procesamiento to set
	 */
	public void setOrden_procesamiento(Integer orden_procesamiento) {
		this.orden_procesamiento = orden_procesamiento;
	}
}


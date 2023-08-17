

/**
 * 
 	Creado por: Alberto Zapata
 	Fecha de Cracion: 09/12/2008
 	Ultima Modificacion: 10/12/2008
 	Departamento: Informatica
 * 
 */

package com.casapellas.entidades;

import java.util.Date;


public class XSalida {

	private SalidaId id;
	private int numsal;
	private String sucu;
	private String cajero;
	private String cargo;
	private String cliente;
	private String cobj;
	private String concepto;
	private String csub;
	private String estado;
	private Date fecha;
	private String uneg;
	
	
	public XSalida(int numsal, String sucu, String cajero, String cargo, String cliente, String cobj, String concepto, String csub, String estado, Date fecha, String uneg) {
		super();
		this.numsal = numsal;
		this.sucu = sucu;
		this.cajero = cajero;
		this.cargo = cargo;
		this.cliente = cliente;
		this.cobj = cobj;
		this.concepto = concepto;
		this.csub = csub;
		this.estado = estado;
		this.fecha = fecha;
		this.uneg = uneg;
	}
	public String getCajero() {
		return cajero;
	}
	public void setCajero(String cajero) {
		this.cajero = cajero;
	}
	public String getCargo() {
		return cargo;
	}
	public void setCargo(String cargo) {
		this.cargo = cargo;
	}
	public String getCliente() {
		return cliente;
	}
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	public String getCobj() {
		return cobj;
	}
	public void setCobj(String cobj) {
		this.cobj = cobj;
	}
	public String getConcepto() {
		return concepto;
	}
	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}
	public String getCsub() {
		return csub;
	}
	public void setCsub(String csub) {
		this.csub = csub;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public String getUneg() {
		return uneg;
	}
	public void setUneg(String uneg) {
		this.uneg = uneg;
	}
	public int getNumsal() {
		return numsal;
	}
	public void setNumsal(int numsal) {
		this.numsal = numsal;
	}
	public String getSucu() {
		return sucu;
	}
	public void setSucu(String sucu) {
		this.sucu = sucu;
	}
	public SalidaId getId() {
		return id;
	}
	public void setId(SalidaId id) {
		this.id = id;
	}
	
	
}

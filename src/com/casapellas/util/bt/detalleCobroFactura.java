package com.casapellas.util.bt;

public class detalleCobroFactura {
private int noFactura;
private String tipoFactura;
private double cuota;
private double saldo;
private double aplicado;
private String fecha;
private int orden;
public int getOrden() {
	return orden;
}
public void setOrden(int orden) {
	this.orden = orden;
}
public int getNoFactura() {
	return noFactura;
}
public void setNoFactura(int noFactura) {
	this.noFactura = noFactura;
}
public String getTipoFactura() {
	return tipoFactura;
}
public void setTipoFactura(String tipoFactura) {
	this.tipoFactura = tipoFactura;
}
public double getCuota() {
	return cuota;
}
public void setCuota(double cuota) {
	this.cuota = cuota;
}
public double getSaldo() {
	return saldo;
}
public void setSaldo(double saldo) {
	this.saldo = saldo;
}
public double getAplicado() {
	return aplicado;
}
public void setAplicado(double aplicado) {
	this.aplicado = aplicado;
}
public String getFecha() {
	return fecha;
}
public void setFecha(String fecha) {
	this.fecha = fecha;
}
}

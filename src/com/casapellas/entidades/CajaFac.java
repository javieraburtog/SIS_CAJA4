package com.casapellas.entidades;

/**
 * 
 * @author luisfonseca
 *
 */
public class CajaFac {
	private Integer numeroCaja;
	private Integer codigoCliente;
	private Integer numeroFactura;
	private String numeroCuota;
	private String tipofactura;
	private String fecha;
	private Integer cajero;
	private String usuario;
	private String ip_maquina;
	private String ip_host;
	
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getIp_maquina() {
		return ip_maquina;
	}
	public void setIp_maquina(String ip_maquina) {
		this.ip_maquina = ip_maquina;
	}
	public String getNavegador() {
		return navegador;
	}
	public void setNavegador(String navegador) {
		this.navegador = navegador;
	}
	private String navegador;
	
	public Integer getNumeroCaja() {
		return numeroCaja;
	}
	public void setNumeroCaja(Integer numeroCaja) {
		this.numeroCaja = numeroCaja;
	}
	public Integer getCodigoCliente() {
		return codigoCliente;
	}
	public void setCodigoCliente(Integer codigoCliente) {
		this.codigoCliente = codigoCliente;
	}
	public Integer getNumeroFactura() {
		return numeroFactura;
	}
	public void setNumeroFactura(Integer numeroFactura) {
		this.numeroFactura = numeroFactura;
	}
	public String getTipofactura() {
		return tipofactura;
	}
	public void setTipofactura(String tipofactura) {
		this.tipofactura = tipofactura;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public Integer getCajero() {
		return cajero;
	}
	public void setCajero(Integer cajero) {
		this.cajero = cajero;
	}
	public String getNumeroCuota() {
		return numeroCuota;
	}
	public void setNumeroCuota(String numeroCuota) {
		this.numeroCuota = numeroCuota;
	}
	public String getIp_host() {
		return ip_host;
	}
	public void setIp_host(String ip_host) {
		this.ip_host = ip_host;
	}
}

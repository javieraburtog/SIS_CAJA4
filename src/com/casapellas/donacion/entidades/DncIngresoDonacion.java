package com.casapellas.donacion.entidades;

import java.math.BigDecimal;

public class DncIngresoDonacion implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 303963296483391412L;
	private int idbenficiario;
	private int codigo;
	private String nombrebeneficiario;
	private String nombrecorto;
	private BigDecimal montorecibido;
	private String moneda;
	private String formadepago;
	private String referencia1;
	private String referencia2;
	private int socketpos;
	private int codigopos;
	private int codigocliente;
	private String nombrecliente;
	private int codigocajero;
	private String codcomp;
	private int caid;
	private String codigoformapago;
	private String codigomarcatarjeta;
	private String marcatarjeta;

	public DncIngresoDonacion(int idbenficiario, int codigo,
			String nombrebeneficiario, String nombrecorto,
			BigDecimal montorecibido, String moneda, String formadepago,
			String referencia1, String referencia2, int socketpos,
			int codigopos, int codigocliente, String nombrecliente,
			int codigocajero, String codcomp, int caid, String codigoformapago,
			String codigomarcatarjeta, String marcatarjeta ) {
		super();
		this.idbenficiario = idbenficiario;
		this.codigo = codigo;
		this.nombrebeneficiario = nombrebeneficiario;
		this.nombrecorto = nombrecorto;
		this.montorecibido = montorecibido;
		this.moneda = moneda;
		this.formadepago = formadepago;
		this.referencia1 = referencia1;
		this.referencia2 = referencia2;
		this.socketpos = socketpos;
		this.codigopos = codigopos;
		this.codigocliente = codigocliente;
		this.nombrecliente = nombrecliente;
		this.codigocajero = codigocajero;
		this.codcomp = codcomp;
		this.caid = caid;
		this.codigoformapago = codigoformapago;
		this.codigomarcatarjeta = codigomarcatarjeta; 
		this.marcatarjeta = marcatarjeta; 

	}

	public String getNombrecorto() {
		return nombrecorto;
	}

	public void setNombrecorto(String nombrecorto) {
		this.nombrecorto = nombrecorto;
	}

	public int getIdbenficiario() {
		return idbenficiario;
	}

	public void setIdbenficiario(int idbenficiario) {
		this.idbenficiario = idbenficiario;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getNombrebeneficiario() {
		return nombrebeneficiario;
	}

	public void setNombrebeneficiario(String nombrebeneficiario) {
		this.nombrebeneficiario = nombrebeneficiario;
	}

	public BigDecimal getMontorecibido() {
		return montorecibido;
	}

	public void setMontorecibido(BigDecimal montorecibido) {
		this.montorecibido = montorecibido;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public String getFormadepago() {
		return formadepago;
	}

	public void setFormadepago(String formadepago) {
		this.formadepago = formadepago;
	}

	public String getReferencia1() {
		if(referencia1 == null)
			referencia1 = "" ;
		return referencia1;
	}

	public void setReferencia1(String referencia1) {
		this.referencia1 = referencia1;
	}

	public String getReferencia2() {
		if(referencia2 == null)
			referencia2 = "" ;
		return referencia2;
	}

	public void setReferencia2(String referencia2) {
		this.referencia2 = referencia2;
	}

	public int getSocketpos() {
		return socketpos;
	}

	public void setSocketpos(int socketpos) {
		this.socketpos = socketpos;
	}

	public int getCodigopos() {
		return codigopos;
	}

	public void setCodigopos(int codigopos) {
		this.codigopos = codigopos;
	}

	public int getCodigocliente() {
		return codigocliente;
	}

	public void setCodigocliente(int codigocliente) {
		this.codigocliente = codigocliente;
	}

	public String getNombrecliente() {
		return nombrecliente;
	}

	public void setNombrecliente(String nombrecliente) {
		this.nombrecliente = nombrecliente;
	}

	public int getCodigocajero() {
		return codigocajero;
	}

	public void setCodigocajero(int codigocajero) {
		this.codigocajero = codigocajero;
	}

	public String getCodcomp() {
		return codcomp;
	}

	public void setCodcomp(String codcomp) {
		this.codcomp = codcomp;
	}

	public int getCaid() {
		return caid;
	}

	public void setCaid(int caid) {
		this.caid = caid;
	}

	public String getCodigoformapago() {
		return codigoformapago;
	}

	public void setCodigoformapago(String codigoformapago) {
		this.codigoformapago = codigoformapago;
	}
	
	public String getCodigomarcatarjeta() {
		return codigomarcatarjeta;
	}

	public void setCodigomarcatarjeta(String codigomarcatarjeta) {
		this.codigomarcatarjeta = codigomarcatarjeta;
	}

	public String getMarcatarjeta() {
		return marcatarjeta;
	}

	public void setMarcatarjeta(String marcatarjeta) {
		this.marcatarjeta = marcatarjeta;
	}
}

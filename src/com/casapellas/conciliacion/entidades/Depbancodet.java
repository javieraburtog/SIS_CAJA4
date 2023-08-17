package com.casapellas.conciliacion.entidades;

// Generated Oct 7, 2011 4:09:23 PM by Hibernate Tools 3.2.2.GA

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

 
public class Depbancodet implements java.io.Serializable {
 
	private static final long serialVersionUID = -5913837436867510679L;
	private int iddepbcodet;
	private Archivo archivo;
	private Date fechaproceso;
	private Date fechavalor;
	private String codtransaccion;
	private String descripcion;
	private int referencia;
	private int nocuenta;
	private BigDecimal mtodebito;
	private BigDecimal mtocredito;
	private BigDecimal balance;
	private short codsucursal;
	private char tiporegistro;
	private int idtipoconfirm;
	private int idestadocnfr;
	private Date fechamod;
	private int usrmod;
	private Set<Conciliadet> conciliadets = new HashSet<Conciliadet>(0);
	private String historicomod;

	public Depbancodet() {
	}

	public Depbancodet(int iddepbcodet, Archivo archivo, Date fechaproceso,
			Date fechavalor, String codtransaccion, String descripcion,
			int referencia, int nocuenta, BigDecimal mtodebito,
			BigDecimal mtocredito, BigDecimal balance, short codsucursal,
			char tiporegistro, int idtipoconfirm, int idestadocnfr,
			Date fechamod, int usrmod,String historicomod) {
		this.iddepbcodet = iddepbcodet;
		this.archivo = archivo;
		this.fechaproceso = fechaproceso;
		this.fechavalor = fechavalor;
		this.codtransaccion = codtransaccion;
		this.descripcion = descripcion;
		this.referencia = referencia;
		this.nocuenta = nocuenta;
		this.mtodebito = mtodebito;
		this.mtocredito = mtocredito;
		this.balance = balance;
		this.codsucursal = codsucursal;
		this.tiporegistro = tiporegistro;
		this.idtipoconfirm = idtipoconfirm;
		this.idestadocnfr = idestadocnfr;
		this.fechamod = fechamod;
		this.usrmod = usrmod;
		this.historicomod = historicomod;
	}

	public Depbancodet(int iddepbcodet, Archivo archivo, Date fechaproceso,
			Date fechavalor, String codtransaccion, String descripcion,
			int referencia, int nocuenta, BigDecimal mtodebito,
			BigDecimal mtocredito, BigDecimal balance, short codsucursal,
			char tiporegistro, int idtipoconfirm, int idestadocnfr,
			Date fechamod, int usrmod, Set<Conciliadet> conciliadets,String historicomod) {
		this.iddepbcodet = iddepbcodet;
		this.archivo = archivo;
		this.fechaproceso = fechaproceso;
		this.fechavalor = fechavalor;
		this.codtransaccion = codtransaccion;
		this.descripcion = descripcion;
		this.referencia = referencia;
		this.nocuenta = nocuenta;
		this.mtodebito = mtodebito;
		this.mtocredito = mtocredito;
		this.balance = balance;
		this.codsucursal = codsucursal;
		this.tiporegistro = tiporegistro;
		this.idtipoconfirm = idtipoconfirm;
		this.idestadocnfr = idestadocnfr;
		this.fechamod = fechamod;
		this.usrmod = usrmod;
		this.conciliadets = conciliadets;
		this.historicomod = historicomod;
	}

	public int getIddepbcodet() {
		return this.iddepbcodet;
	}

	public void setIddepbcodet(int iddepbcodet) {
		this.iddepbcodet = iddepbcodet;
	}

	public Archivo getArchivo() {
		return this.archivo;
	}

	public void setArchivo(Archivo archivo) {
		this.archivo = archivo;
	}

	public Date getFechaproceso() {
		return this.fechaproceso;
	}

	public void setFechaproceso(Date fechaproceso) {
		this.fechaproceso = fechaproceso;
	}

	public Date getFechavalor() {
		return this.fechavalor;
	}

	public void setFechavalor(Date fechavalor) {
		this.fechavalor = fechavalor;
	}

	public String getCodtransaccion() {
		return this.codtransaccion;
	}

	public void setCodtransaccion(String codtransaccion) {
		this.codtransaccion = codtransaccion;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getReferencia() {
		return this.referencia;
	}

	public void setReferencia(int referencia) {
		this.referencia = referencia;
	}

	public int getNocuenta() {
		return this.nocuenta;
	}

	public void setNocuenta(int nocuenta) {
		this.nocuenta = nocuenta;
	}

	public BigDecimal getMtodebito() {
		return this.mtodebito;
	}

	public void setMtodebito(BigDecimal mtodebito) {
		this.mtodebito = mtodebito;
	}

	public BigDecimal getMtocredito() {
		return this.mtocredito;
	}

	public void setMtocredito(BigDecimal mtocredito) {
		this.mtocredito = mtocredito;
	}

	public BigDecimal getBalance() {
		return this.balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public short getCodsucursal() {
		return this.codsucursal;
	}

	public void setCodsucursal(short codsucursal) {
		this.codsucursal = codsucursal;
	}

	public char getTiporegistro() {
		return this.tiporegistro;
	}

	public void setTiporegistro(char tiporegistro) {
		this.tiporegistro = tiporegistro;
	}

	public int getIdtipoconfirm() {
		return this.idtipoconfirm;
	}

	public void setIdtipoconfirm(int idtipoconfirm) {
		this.idtipoconfirm = idtipoconfirm;
	}

	public int getIdestadocnfr() {
		return this.idestadocnfr;
	}

	public void setIdestadocnfr(int idestadocnfr) {
		this.idestadocnfr = idestadocnfr;
	}

	public Date getFechamod() {
		return this.fechamod;
	}

	public void setFechamod(Date fechamod) {
		this.fechamod = fechamod;
	}

	public int getUsrmod() {
		return this.usrmod;
	}

	public void setUsrmod(int usrmod) {
		this.usrmod = usrmod;
	}

	public Set<Conciliadet> getConciliadets() {
		return this.conciliadets;
	}

	public void setConciliadets(Set<Conciliadet> conciliadets) {
		this.conciliadets = conciliadets;
	}

	public String getHistoricomod() {
		return historicomod;
	}

	public void setHistoricomod(String historicomod) {
		this.historicomod = historicomod;
	}
}

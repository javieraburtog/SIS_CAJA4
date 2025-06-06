package com.casapellas.entidades;

// Generated Dec 21, 2012 3:34:25 PM by Hibernate Tools 3.2.4.GA

import java.math.BigDecimal;
import java.util.Date;

/**
 * VrecibostcirId generated by hbm2java
 */
public class VrecibostcirId implements java.io.Serializable {


	private static final long serialVersionUID = 4961716724743504521L;
	private int numrec;
	private String tiporec;
	private int caid;
	private String codcomp;
	private String codsuc;
	private Date fecha;
	private BigDecimal monto;
	private BigDecimal tasa;
	private BigDecimal equiv;
	private String moneda;
	private String afiliado;
	private BigDecimal comision;
	private String codunineg;
	private String nomunineg;
	private int noliquidacion;
	private String estado;
	private Date hora;
	private BigDecimal montoapl;
	private String cliente;
	private String cajero;
	private BigDecimal tasaoficialdia;

	public VrecibostcirId() {
	}

	public VrecibostcirId(int numrec, String tiporec, int caid, String codsuc,
			Date fecha, BigDecimal monto, BigDecimal tasa, BigDecimal equiv,
			String moneda, String afiliado, String codunineg, String nomunineg,
			int noliquidacion, String estado, Date hora, BigDecimal montoapl,
			String cliente, String cajero) {
		this.numrec = numrec;
		this.tiporec = tiporec;
		this.caid = caid;
		this.codsuc = codsuc;
		this.fecha = fecha;
		this.monto = monto;
		this.tasa = tasa;
		this.equiv = equiv;
		this.moneda = moneda;
		this.afiliado = afiliado;
		this.codunineg = codunineg;
		this.nomunineg = nomunineg;
		this.noliquidacion = noliquidacion;
		this.estado = estado;
		this.hora = hora;
		this.montoapl = montoapl;
		this.cliente = cliente;
		this.cajero = cajero;
	}

	public VrecibostcirId(int numrec, String tiporec, int caid, String codcomp,
			String codsuc, Date fecha, BigDecimal monto, BigDecimal tasa,
			BigDecimal equiv, String moneda, String afiliado,
			BigDecimal comision, String codunineg, String nomunineg,
			int noliquidacion, String estado, Date hora, BigDecimal montoapl,
			String cliente, String cajero) {
		this.numrec = numrec;
		this.tiporec = tiporec;
		this.caid = caid;
		this.codcomp = codcomp;
		this.codsuc = codsuc;
		this.fecha = fecha;
		this.monto = monto;
		this.tasa = tasa;
		this.equiv = equiv;
		this.moneda = moneda;
		this.afiliado = afiliado;
		this.comision = comision;
		this.codunineg = codunineg;
		this.nomunineg = nomunineg;
		this.noliquidacion = noliquidacion;
		this.estado = estado;
		this.hora = hora;
		this.montoapl = montoapl;
		this.cliente = cliente;
		this.cajero = cajero;
	}

	public int getNumrec() {
		return this.numrec;
	}

	public void setNumrec(int numrec) {
		this.numrec = numrec;
	}

	public String getTiporec() {
		return this.tiporec;
	}

	public void setTiporec(String tiporec) {
		this.tiporec = tiporec;
	}

	public int getCaid() {
		return this.caid;
	}

	public void setCaid(int caid) {
		this.caid = caid;
	}

	public String getCodcomp() {
		return this.codcomp;
	}

	public void setCodcomp(String codcomp) {
		this.codcomp = codcomp;
	}

	public String getCodsuc() {
		return this.codsuc;
	}

	public void setCodsuc(String codsuc) {
		this.codsuc = codsuc;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public BigDecimal getMonto() {
		return this.monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public BigDecimal getTasa() {
		return this.tasa;
	}

	public void setTasa(BigDecimal tasa) {
		this.tasa = tasa;
	}

	public BigDecimal getEquiv() {
		return this.equiv;
	}

	public void setEquiv(BigDecimal equiv) {
		this.equiv = equiv;
	}

	public String getMoneda() {
		return this.moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public String getAfiliado() {
		return this.afiliado;
	}

	public void setAfiliado(String afiliado) {
		this.afiliado = afiliado;
	}

	public BigDecimal getComision() {
		return this.comision;
	}

	public void setComision(BigDecimal comision) {
		this.comision = comision;
	}

	public String getCodunineg() {
		return this.codunineg;
	}

	public void setCodunineg(String codunineg) {
		this.codunineg = codunineg;
	}

	public String getNomunineg() {
		return this.nomunineg;
	}

	public void setNomunineg(String nomunineg) {
		this.nomunineg = nomunineg;
	}

	public int getNoliquidacion() {
		return this.noliquidacion;
	}

	public void setNoliquidacion(int noliquidacion) {
		this.noliquidacion = noliquidacion;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getHora() {
		return this.hora;
	}

	public void setHora(Date hora) {
		this.hora = hora;
	}

	public BigDecimal getMontoapl() {
		return this.montoapl;
	}

	public void setMontoapl(BigDecimal montoapl) {
		this.montoapl = montoapl;
	}

	public String getCliente() {
		return this.cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public String getCajero() {
		return this.cajero;
	}

	public void setCajero(String cajero) {
		this.cajero = cajero;
	}

	public BigDecimal getTasaoficialdia() {
		return tasaoficialdia;
	}

	public void setTasaoficialdia(BigDecimal tasaoficialdia) {
		this.tasaoficialdia = tasaoficialdia;
	}

	/*	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof VrecibostcirId))
			return false;
		VrecibostcirId castOther = (VrecibostcirId) other;

		return (this.getNumrec() == castOther.getNumrec())
				&& ((this.getTiporec() == castOther.getTiporec()) || (this
						.getTiporec() != null
						&& castOther.getTiporec() != null && this.getTiporec()
						.equals(castOther.getTiporec())))
				&& (this.getCaid() == castOther.getCaid())
				&& ((this.getCodcomp() == castOther.getCodcomp()) || (this
						.getCodcomp() != null
						&& castOther.getCodcomp() != null && this.getCodcomp()
						.equals(castOther.getCodcomp())))
				&& ((this.getCodsuc() == castOther.getCodsuc()) || (this
						.getCodsuc() != null
						&& castOther.getCodsuc() != null && this.getCodsuc()
						.equals(castOther.getCodsuc())))
				&& ((this.getFecha() == castOther.getFecha()) || (this
						.getFecha() != null
						&& castOther.getFecha() != null && this.getFecha()
						.equals(castOther.getFecha())))
				&& ((this.getMonto() == castOther.getMonto()) || (this
						.getMonto() != null
						&& castOther.getMonto() != null && this.getMonto()
						.equals(castOther.getMonto())))
				&& ((this.getTasa() == castOther.getTasa()) || (this.getTasa() != null
						&& castOther.getTasa() != null && this.getTasa()
						.equals(castOther.getTasa())))
				&& ((this.getEquiv() == castOther.getEquiv()) || (this
						.getEquiv() != null
						&& castOther.getEquiv() != null && this.getEquiv()
						.equals(castOther.getEquiv())))
				&& ((this.getMoneda() == castOther.getMoneda()) || (this
						.getMoneda() != null
						&& castOther.getMoneda() != null && this.getMoneda()
						.equals(castOther.getMoneda())))
				&& ((this.getAfiliado() == castOther.getAfiliado()) || (this
						.getAfiliado() != null
						&& castOther.getAfiliado() != null && this
						.getAfiliado().equals(castOther.getAfiliado())))
				&& ((this.getComision() == castOther.getComision()) || (this
						.getComision() != null
						&& castOther.getComision() != null && this
						.getComision().equals(castOther.getComision())))
				&& ((this.getCodunineg() == castOther.getCodunineg()) || (this
						.getCodunineg() != null
						&& castOther.getCodunineg() != null && this
						.getCodunineg().equals(castOther.getCodunineg())))
				&& ((this.getNomunineg() == castOther.getNomunineg()) || (this
						.getNomunineg() != null
						&& castOther.getNomunineg() != null && this
						.getNomunineg().equals(castOther.getNomunineg())))
				&& (this.getNoliquidacion() == castOther.getNoliquidacion())
				&& ((this.getEstado() == castOther.getEstado()) || (this
						.getEstado() != null
						&& castOther.getEstado() != null && this.getEstado()
						.equals(castOther.getEstado())))
				&& ((this.getHora() == castOther.getHora()) || (this.getHora() != null
						&& castOther.getHora() != null && this.getHora()
						.equals(castOther.getHora())))
				&& ((this.getMontoapl() == castOther.getMontoapl()) || (this
						.getMontoapl() != null
						&& castOther.getMontoapl() != null && this
						.getMontoapl().equals(castOther.getMontoapl())))
				&& ((this.getCliente() == castOther.getCliente()) || (this
						.getCliente() != null
						&& castOther.getCliente() != null && this.getCliente()
						.equals(castOther.getCliente())))
				&& ((this.getCajero() == castOther.getCajero()) || (this
						.getCajero() != null
						&& castOther.getCajero() != null && this.getCajero()
						.equals(castOther.getCajero())))
				&& ((this.getTasaoficialdia() == castOther.getTasaoficialdia()) || (this
						.getTasaoficialdia() != null
						&& castOther.getTasaoficialdia() != null && this
						.getTasaoficialdia().equals(
								castOther.getTasaoficialdia())));
	}*/

	/*	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getNumrec();
		result = 37 * result
				+ (getTiporec() == null ? 0 : this.getTiporec().hashCode());
		result = 37 * result + this.getCaid();
		result = 37 * result
				+ (getCodcomp() == null ? 0 : this.getCodcomp().hashCode());
		result = 37 * result
				+ (getCodsuc() == null ? 0 : this.getCodsuc().hashCode());
		result = 37 * result
				+ (getFecha() == null ? 0 : this.getFecha().hashCode());
		result = 37 * result
				+ (getMonto() == null ? 0 : this.getMonto().hashCode());
		result = 37 * result
				+ (getTasa() == null ? 0 : this.getTasa().hashCode());
		result = 37 * result
				+ (getEquiv() == null ? 0 : this.getEquiv().hashCode());
		result = 37 * result
				+ (getMoneda() == null ? 0 : this.getMoneda().hashCode());
		result = 37 * result
				+ (getAfiliado() == null ? 0 : this.getAfiliado().hashCode());
		result = 37 * result
				+ (getComision() == null ? 0 : this.getComision().hashCode());
		result = 37 * result
				+ (getCodunineg() == null ? 0 : this.getCodunineg().hashCode());
		result = 37 * result
				+ (getNomunineg() == null ? 0 : this.getNomunineg().hashCode());
		result = 37 * result + this.getNoliquidacion();
		result = 37 * result
				+ (getEstado() == null ? 0 : this.getEstado().hashCode());
		result = 37 * result
				+ (getHora() == null ? 0 : this.getHora().hashCode());
		result = 37 * result
				+ (getMontoapl() == null ? 0 : this.getMontoapl().hashCode());
		result = 37 * result
				+ (getCliente() == null ? 0 : this.getCliente().hashCode());
		result = 37 * result
				+ (getCajero() == null ? 0 : this.getCajero().hashCode());
		result = 37 * result
				+ (getTasaoficialdia() == null ? 0 : this.getTasaoficialdia().hashCode());
		return result;
	}*/

}

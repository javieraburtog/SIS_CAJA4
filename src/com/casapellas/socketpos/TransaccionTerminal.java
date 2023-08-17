package com.casapellas.socketpos;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.casapellas.entidades.Transactsp;

public class TransaccionTerminal implements java.io.Serializable {
	private static final long serialVersionUID = 3097474726812218445L;
	
	private boolean term_cerrada;
	private String terminalid;
	private String afiliado;
	private int cant_transacciones;
	private int cant_aprobadas;
	private int cant_anuladas;
	private BigDecimal totalcierre;
	private String nombreterminal;
	private Date trans_fechaDesde ;
	private Date trans_fechaHasta ;
	private String codcomp;
	private String moneda;
	private int cant_Creditos;
	private int cant_Reembolsos;
	private BigDecimal mto_Creditos;
	private BigDecimal mto_Reembolsos ;
	private List<Transactsp>transacciones;
	private int caid;
	private String coduser;
	private int usrCodreg;
	private int batchnumber;
	
	private String rutarealreporte;
	private byte[] dtaReporteCierre;
	private String nombrereporte;
	
	
	
	public TransaccionTerminal() {
	}
	public TransaccionTerminal(boolean term_cerrada, String terminalid,
			String afiliado, int cant_transacciones, int cant_aprobadas,
			int cant_anuladas, BigDecimal totalcierre, String nombreterminal,
			Date trans_fechaDesde, Date trans_fechaHasta, String codcomp,
			String moneda, int cant_Creditos, int cant_Reembolsos,
			BigDecimal mto_Creditos, BigDecimal mto_Reembolsos,
			List<Transactsp> transacciones, int caid, String coduser,
			int usrCodreg) {
		super();
		this.term_cerrada = term_cerrada;
		this.terminalid = terminalid;
		this.afiliado = afiliado;
		this.cant_transacciones = cant_transacciones;
		this.cant_aprobadas = cant_aprobadas;
		this.cant_anuladas = cant_anuladas;
		this.totalcierre = totalcierre;
		this.nombreterminal = nombreterminal;
		this.trans_fechaDesde = trans_fechaDesde;
		this.trans_fechaHasta = trans_fechaHasta;
		this.codcomp = codcomp;
		this.moneda = moneda;
		this.cant_Creditos = cant_Creditos;
		this.cant_Reembolsos = cant_Reembolsos;
		this.mto_Creditos = mto_Creditos;
		this.mto_Reembolsos = mto_Reembolsos;
		this.transacciones = transacciones;
		this.caid = caid;
		this.coduser = coduser;
		this.usrCodreg = usrCodreg;
	}

	public String getTerminalid() {
		return terminalid;
	}
	public void setTerminalid(String terminalid) {
		this.terminalid = terminalid;
	}
	public String getAfiliado() {
		return afiliado;
	}
	public void setAfiliado(String afiliado) {
		this.afiliado = afiliado;
	}
	public int getCant_transacciones() {
		return cant_transacciones;
	}
	public void setCant_transacciones(int cant_transacciones) {
		this.cant_transacciones = cant_transacciones;
	}
	public int getCant_aprobadas() {
		return cant_aprobadas;
	}
	public void setCant_aprobadas(int cant_aprobadas) {
		this.cant_aprobadas = cant_aprobadas;
	}
	public int getCant_anuladas() {
		return cant_anuladas;
	}
	public void setCant_anuladas(int cant_anuladas) {
		this.cant_anuladas = cant_anuladas;
	}
	public BigDecimal getTotalcierre() {
		return totalcierre;
	}
	public void setTotalcierre(BigDecimal totalcierre) {
		this.totalcierre = totalcierre;
	}
	public String getNombreterminal() {
		return nombreterminal;
	}
	public void setNombreterminal(String nombreterminal) {
		this.nombreterminal = nombreterminal;
	}
	public Date getTrans_fechaDesde() {
		return trans_fechaDesde;
	}
	public void setTrans_fechaDesde(Date trans_fechaDesde) {
		this.trans_fechaDesde = trans_fechaDesde;
	}
	public Date getTrans_fechaHasta() {
		return trans_fechaHasta;
	}
	public void setTrans_fechaHasta(Date trans_fechaHasta) {
		this.trans_fechaHasta = trans_fechaHasta;
	}
	public boolean isTerm_cerrada() {
		return term_cerrada;
	}
	public void setTerm_cerrada(boolean term_cerrada) {
		this.term_cerrada = term_cerrada;
	}
	public String getCodcomp() {
		return codcomp;
	}
	public void setCodcomp(String codcomp) {
		this.codcomp = codcomp;
	}
	public String getMoneda() {
		return moneda;
	}
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}
	public List<Transactsp> getTransacciones() {
		return transacciones;
	}
	public void setTransacciones(List<Transactsp> transacciones) {
		this.transacciones = transacciones;
	}
	public int getCaid() {
		return caid;
	}
	public void setCaid(int caid) {
		this.caid = caid;
	}
	public String getCoduser() {
		return coduser;
	}
	public void setCoduser(String coduser) {
		this.coduser = coduser;
	}
	public int getUsrCodreg() {
		return usrCodreg;
	}
	public void setUsrCodreg(int usrCodreg) {
		this.usrCodreg = usrCodreg;
	}
	public int getCant_Creditos() {
		return cant_Creditos;
	}
	public void setCant_Creditos(int cant_Creditos) {
		this.cant_Creditos = cant_Creditos;
	}
	public int getCant_Reembolsos() {
		return cant_Reembolsos;
	}
	public void setCant_Reembolsos(int cant_Reembolsos) {
		this.cant_Reembolsos = cant_Reembolsos;
	}
	public BigDecimal getMto_Creditos() {
		return mto_Creditos;
	}
	public void setMto_Creditos(BigDecimal mto_Creditos) {
		this.mto_Creditos = mto_Creditos;
	}
	public BigDecimal getMto_Reembolsos() {
		return mto_Reembolsos;
	}
	public void setMto_Reembolsos(BigDecimal mto_Reembolsos) {
		this.mto_Reembolsos = mto_Reembolsos;
	}
	public int getBatchnumber() {
		return batchnumber;
	}
	public void setBatchnumber(int batchnumber) {
		this.batchnumber = batchnumber;
	}
	public byte[] getDtaReporteCierre() {
		return dtaReporteCierre;
	}
	public void setDtaReporteCierre(byte[] dtaReporteCierre) {
		this.dtaReporteCierre = dtaReporteCierre;
	}
	public String getRutarealreporte() {
		return rutarealreporte;
	}
	public void setRutarealreporte(String rutarealreporte) {
		this.rutarealreporte = rutarealreporte;
	}
	public String getNombrereporte() {
		return nombrereporte;
	}
	public void setNombrereporte(String nombrereporte) {
		this.nombrereporte = nombrereporte;
	}
	
}

package com.casapellas.reportes;

public class ReciboR {
	private int numrec;
	private String montoapl;
	private String montorec;
	private String cambio;
	private String concepto;
	private String tiporec;
	private String fecha;
	private String cliente;
	private int codcli;
	private String cajero;
	private String nomcomp;
	private String codcomp;
	private String nomsuc;
	private String hora;
	
	public ReciboR(int numrec, String montoapl, String montorec, String cambio, String concepto, String tiporec, 
					String fecha, String cliente, int codcli, String cajero,String nomcomp,String codcomp, String nomsuc,String hora) {
		super();
		this.numrec = numrec;
		this.montoapl = montoapl;
		this.montorec = montorec;
		this.cambio = cambio;
		this.concepto = concepto;
		this.tiporec = tiporec;
		this.fecha = fecha;
		this.cliente = cliente;
		this.codcli = codcli;
		this.cajero = cajero;
		this.nomcomp = nomcomp;
		this.codcomp = codcomp;
		this.nomsuc = nomsuc;
		this.hora = hora;
	}

	public String getCajero() {
		return cajero;
	}

	public void setCajero(String cajero) {
		this.cajero = cajero;
	}

	public String getCambio() {
		return cambio;
	}

	public void setCambio(String cambio) {
		this.cambio = cambio;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public int getCodcli() {
		return codcli;
	}

	public void setCodcli(int codcli) {
		this.codcli = codcli;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getMontoapl() {
		return montoapl;
	}

	public void setMontoapl(String montoapl) {
		this.montoapl = montoapl;
	}

	public String getMontorec() {
		return montorec;
	}

	public void setMontorec(String montorec) {
		this.montorec = montorec;
	}

	public int getNumrec() {
		return numrec;
	}

	public void setNumrec(int numrec) {
		this.numrec = numrec;
	}

	public String getTiporec() {
		return tiporec;
	}

	public void setTiporec(String tiporec) {
		this.tiporec = tiporec;
	}

	public String getNomcomp() {
		return nomcomp;
	}

	public void setNomcomp(String nomcomp) {
		this.nomcomp = nomcomp;
	}

	public String getCodcomp() {
		return codcomp;
	}

	public void setCodcomp(String codcomp) {
		this.codcomp = codcomp;
	}

	public String getNomsuc() {
		return nomsuc;
	}

	public void setNomsuc(String nomsuc) {
		this.nomsuc = nomsuc;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}
	
}

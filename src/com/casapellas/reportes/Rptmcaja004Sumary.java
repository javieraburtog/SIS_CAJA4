package com.casapellas.reportes;

/**
 * CASA PELLAS S.A.
 * Creado por.........: Carlos Manuel Hernández Morrison
 * Fecha de Creación..: 27/03/2010
 * Última modificación: Carlos Manuel Hernández Morrison
 * Modificado por.....:	27/03/2010
 * Descripcion:.......: Datos necesarios para mostrar el cuadro
 * 						resumen de monto por unidad de negocio.
 */

public class Rptmcaja004Sumary {

	public String codunineg;
	
	public String nomunineg;
	
	public int caid;
	
	public String codcomp;
	
	public String codsuc;
	
	public double montototal;
		
	public Rptmcaja004Sumary(){
		
	}
	public Rptmcaja004Sumary(String codunineg, String nomunineg, int caid, String codcomp, String codsuc, double montototal) {
		super();
		this.codunineg = codunineg;
		this.nomunineg = nomunineg;
		this.caid = caid;
		this.codcomp = codcomp;
		this.codsuc = codsuc;
		this.montototal = montototal;
	}

	public int getCaid() {
		return caid;
	}

	public void setCaid(int caid) {
		this.caid = caid;
	}

	public String getCodcomp() {
		return codcomp;
	}

	public void setCodcomp(String codcomp) {
		this.codcomp = codcomp;
	}

	public String getCodsuc() {
		return codsuc;
	}

	public void setCodsuc(String codsuc) {
		this.codsuc = codsuc;
	}

	public String getCodunineg() {
		return codunineg;
	}

	public void setCodunineg(String codunineg) {
		this.codunineg = codunineg;
	}

	public double getMontototal() {
		return montototal;
	}

	public void setMontototal(double montototal) {
		this.montototal = montototal;
	}

	public String getNomunineg() {
		return nomunineg;
	}

	public void setNomunineg(String nomunineg) {
		this.nomunineg = nomunineg;
	}

	
}

package com.casapellas.conciliacion;
import com.casapellas.conciliacion.entidades.Archivo;
import com.casapellas.conciliacion.entidades.Depbancodet;
import com.casapellas.entidades.Deposito;

/**
 * CASA PELLAS S.A.
 * Creado por.........: Carlos Manuel Hernández Morrison
 * Fecha de Creación..: 26/08/2011
 * Última modificación: Carlos Manuel Hernández Morrison
 * Modificado por.....:	26/08/2011
 * Descripción:.......: Manejo de asociaciones entre deposito banco deposito caja por archivo.
 */ 
public class CoincidenciaDeposito {
	
	public CoincidenciaDeposito() {
		super();
	}

	public CoincidenciaDeposito(Archivo archivo, Deposito deposito,
			Depbancodet depbancodet, boolean ajuste, double montoXajuste,
			String rangoajuste, int nobatch, String observacion,
			int digitosCompara) {
		super();
		
		this.archivo = archivo;
		this.deposito = deposito;
		this.depbancodet = depbancodet;
		this.ajuste = ajuste;
		this.montoXajuste = montoXajuste;
		this.rangoajuste = rangoajuste;
		this.nobatch = nobatch;
		this.observacion = observacion;
		this.digitosCompara = digitosCompara;
	}
	
	private Archivo archivo;
	
	private Deposito deposito;
	
	private Depbancodet depbancodet;
	
	private boolean ajuste;
	
	private double montoXajuste;
	
	private String rangoajuste;
	
	private int nobatch;

	private String observacion;
	
	private int digitosCompara;
	
	public Archivo getArchivo() {
		return archivo;
	}

	public void setArchivo(Archivo archivo) {
		this.archivo = archivo;
	}

	public Deposito getDeposito() {
		return deposito;
	}

	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}

	public Depbancodet getDepbancodet() {
		return depbancodet;
	}

	public void setDepbancodet(Depbancodet depbancodet) {
		this.depbancodet = depbancodet;
	}

	public boolean isAjuste() {
		return ajuste;
	}

	public void setAjuste(boolean ajuste) {
		this.ajuste = ajuste;
	}

	public double getMontoXajuste() {
		return montoXajuste;
	}

	public void setMontoXajuste(double montoXajuste) {
		this.montoXajuste = montoXajuste;
	}

	public int getNobatch() {
		return nobatch;
	}

	public void setNobatch(int nobatch) {
		this.nobatch = nobatch;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getRangoajuste() {
		return rangoajuste;
	}

	public void setRangoajuste(String rangoajuste) {
		this.rangoajuste = rangoajuste;
	}
	public int getDigitosCompara() {
		return digitosCompara;
	}

	public void setDigitosCompara(int digitosCompara) {
		this.digitosCompara = digitosCompara;
	}
}

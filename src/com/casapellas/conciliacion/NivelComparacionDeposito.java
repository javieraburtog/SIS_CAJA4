package com.casapellas.conciliacion;

public class NivelComparacionDeposito {

	private int nivelComparacion;
	private String nombrenivel;
	private String descripcion;
	private int cantidadcoincidencias;
	private int prioridad;
	private String parametrosComparacion;
	private int coincidenciasIndividiuales;
	private int coincidenciasMultiples;
	private int cantidaddepositoscaja;
	private int cantidaddepositosbanco;
	private int cantidadprocesados;
	private int cantidadpendientes;
	
	
	public NivelComparacionDeposito(int nivelComparacion, String nombrenivel,
			String descripcion, int cantidadcoincidencias, int prioridad,
			String parametrosComparacion, int coincidenciasIndividiuales,
			int coincidenciasMultiples, int cantidaddepositoscaja, 
			int cantidaddepositosbanco,  int cantidadprocesados ,
			int cantidadpendientes ) {
		super();
		this.nivelComparacion = nivelComparacion;
		this.nombrenivel = nombrenivel;
		this.descripcion = descripcion;
		this.cantidadcoincidencias = cantidadcoincidencias;
		this.prioridad = prioridad;
		this.parametrosComparacion = parametrosComparacion;
		this.coincidenciasIndividiuales = coincidenciasIndividiuales;
		this.coincidenciasMultiples = coincidenciasMultiples;
		this.cantidaddepositoscaja = cantidaddepositoscaja; 
		this.cantidaddepositosbanco= cantidaddepositosbanco ; 
		this.cantidadprocesados = cantidadprocesados; 
		this.cantidadpendientes = cantidadpendientes ; 
	}
	
	public int getNivelComparacion() {
		return nivelComparacion;
	}
	public void setNivelComparacion(int nivelComparacion) {
		this.nivelComparacion = nivelComparacion;
	}
	public String getNombrenivel() {
		return nombrenivel;
	}
	public void setNombrenivel(String nombrenivel) {
		this.nombrenivel = nombrenivel;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public int getCantidadcoincidencias() {
		return cantidadcoincidencias;
	}
	public void setCantidadcoincidencias(int cantidadcoincidencias) {
		this.cantidadcoincidencias = cantidadcoincidencias;
	}
	public int getPrioridad() {
		return prioridad;
	}
	public void setPrioridad(int prioridad) {
		this.prioridad = prioridad;
	}
	public String getParametrosComparacion() {
		return parametrosComparacion;
	}
	public void setParametrosComparacion(String parametrosComparacion) {
		this.parametrosComparacion = parametrosComparacion;
	}
	public int getCoincidenciasIndividiuales() {
		return coincidenciasIndividiuales;
	}
	public void setCoincidenciasIndividiuales(int coincidenciasIndividiuales) {
		this.coincidenciasIndividiuales = coincidenciasIndividiuales;
	}
	public int getCoincidenciasMultiples() {
		return coincidenciasMultiples;
	}
	public void setCoincidenciasMultiples(int coincidenciasMultiples) {
		this.coincidenciasMultiples = coincidenciasMultiples;
	}

	public int getCantidaddepositoscaja() {
		return cantidaddepositoscaja;
	}

	public void setCantidaddepositoscaja(int cantidaddepositoscaja) {
		this.cantidaddepositoscaja = cantidaddepositoscaja;
	}

	public int getCantidaddepositosbanco() {
		return cantidaddepositosbanco;
	}

	public void setCantidaddepositosbanco(int cantidaddepositosbanco) {
		this.cantidaddepositosbanco = cantidaddepositosbanco;
	}

	public int getCantidadprocesados() {
		return cantidadprocesados;
	}

	public void setCantidadprocesados(int cantidadprocesados) {
		this.cantidadprocesados = cantidadprocesados;
	}

	public int getCantidadpendientes() {
		return cantidadpendientes;
	}

	public void setCantidadpendientes(int cantidadpendientes) {
		this.cantidadpendientes = cantidadpendientes;
	}
	
}

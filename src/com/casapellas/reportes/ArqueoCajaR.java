package com.casapellas.reportes;


import java.sql.Date;

public class ArqueoCajaR implements java.io.Serializable  {
	private static final long serialVersionUID = 5112541609977443707L;
	
	private int nocaja;
	private String nombrecaja;
	private String compania;
	private String moneda;
	private String nombrecajero;
	private int noarqueo;
	private Date fechaarqueo;
	private String sucursal;
	private String fechahora;
	
	/***** INGRESOS ****/
	private double ventastotales;
	private double devoluciones;
	private double ventascredito;
	private double ventasnetas;
	private double abonos;
	private double primasreservas;
	private double ingpagosdifmonedas;
	private double otrosingresos;
	private double totalingresos;
	
	/***** EGRESOS ****/
	private double ventaspagbanco;
	private double abonospagbanco;
	private double primaspagbanco;
	private double otrosegresos;
	private double totalegresos;
	
	
	/***** CALCULO DEL DEPOSITO ****/
	private double efecnetorecibido;
	private double minimoencaja;
	private double depositosugerido;
	private double efecencaja;
	private double faltantesobrante;
	private double depositofinal;
	
	/**** TOTAL X METODO DE PAGO ****/
	private double tefectivo;
	private double tcheque;
	private double ttcredito;
	private double tdepositobanco;
	private double ttransfbanco;
	private double tformaspago;
	private double ttcreditom;
	private double ttcreditosktpos;
	
	/**** CAMPOS AGREGADOS *****/
	private String sPagoIngresosExt;
	private String sPagoFinanciamiento;
	private String sCambioOtraMoneda;
	private String sFinanciamientosBanco;
	private String sIngresosExtBanco;
	private String sReferenciaDeposito;
	private String sTipoImpresionRpt;
	
	public ArqueoCajaR(){
		
	}
	
//-------------------------------------------------------------//
	/**
	 * @param ventastotales	 	* @param devoluciones	 * @param ventascredito	 		 * @param ventasnetas
	 * @param abonos	 		* @param primasreservas	 * @param ingpagosdifmonedas	 * @param otrosingresos
	 * @param totalingresos	 	* @param ventaspagbanco	 * @param abonospagbanco		 * @param primaspagbanco
	 * @param otrosegresos	 	* @param totalegresos	 * @param efecnetorecibido		 * @param minimoencaja
	 * @param depositosugerido	* @param efecencaja	 	 * @param faltantesobrante		 * @param depositofinal
	 * @param tefectivo			* @param tcheque		 * @param ttcredito				 * @param tdepositobanco 
	 * @param ttransfbanco 		* @param tformaspago
	 */
	public ArqueoCajaR(int nocaja,String moneda,String nombrecajero,Date fechaarqueo, String nombrecaja,String compania,String sucursal,
					   double ventastotales, double devoluciones, double ventascredito, double ventasnetas, double abonos,
					   double primasreservas, double ingpagosdifmonedas, double otrosingresos, double totalingresos, 
					   double ventaspagbanco, double abonospagbanco, double primaspagbanco, double otrosegresos, 
					   double totalegresos, double efecnetorecibido, double minimoencaja, double depositosugerido,
					   double efecencaja, double faltantesobrante, double depositofinal, double tefectivo, double tcheque,
					   double ttcredito, double tdepositobanco, double ttransfbanco, double tformaspago, int noarqueo) {
		super();
		
		this.nocaja = nocaja;
		this.moneda = moneda;
		this.nombrecajero = nombrecajero;
		this.fechaarqueo = fechaarqueo;
		this.noarqueo = noarqueo;
		this.nombrecaja = nombrecaja;
		this.compania = compania;
		this.sucursal = sucursal;
		
		this.ventastotales = ventastotales;
		this.devoluciones = devoluciones;
		this.ventascredito = ventascredito;
		this.ventasnetas = ventasnetas;
		this.abonos = abonos;
		this.primasreservas = primasreservas;
		this.ingpagosdifmonedas = ingpagosdifmonedas;
		this.otrosingresos = otrosingresos;
		this.totalingresos = totalingresos;
		this.ventaspagbanco = ventaspagbanco;
		this.abonospagbanco = abonospagbanco;
		this.primaspagbanco = primaspagbanco;
		this.otrosegresos = otrosegresos;
		this.totalegresos = totalegresos;
		this.efecnetorecibido = efecnetorecibido;
		this.minimoencaja = minimoencaja;
		this.depositosugerido = depositosugerido;
		this.efecencaja = efecencaja;
		this.faltantesobrante = faltantesobrante;
		this.depositofinal = depositofinal;
		this.tefectivo = tefectivo;
		this.tcheque = tcheque;
		this.ttcredito = ttcredito;
		this.tdepositobanco = tdepositobanco;
		this.ttransfbanco = ttransfbanco;
		this.tformaspago = tformaspago;
	}
	
	
/******** CONSTRUCTOR PARA EL REPORTE PRELIMINAR SIN EL CAMPO DE NUMERO DE ARQUEO *********/
	public ArqueoCajaR(int nocaja,String moneda,String nombrecajero,String fechahora, String nombrecaja,String compania,String sucursal,
			   double ventastotales, double devoluciones, double ventascredito, double ventasnetas, double abonos,
			   double primasreservas, double ingpagosdifmonedas, double otrosingresos, double totalingresos, 
			   double ventaspagbanco, double abonospagbanco, double primaspagbanco, double otrosegresos, 
			   double totalegresos, double efecnetorecibido, double minimoencaja, double depositosugerido,
			   double efecencaja, double faltantesobrante, double depositofinal, double tefectivo, double tcheque,
			   double ttcredito, double tdepositobanco, double ttransfbanco, double tformaspago) {
		super();
		
		this.nocaja = nocaja;
		this.moneda = moneda;
		this.nombrecajero = nombrecajero;
		this.fechahora = fechahora;
		this.nombrecaja = nombrecaja;
		this.compania = compania;
		this.sucursal = sucursal;
		
		this.ventastotales = ventastotales;
		this.devoluciones = devoluciones;
		this.ventascredito = ventascredito;
		this.ventasnetas = ventasnetas;
		this.abonos = abonos;
		this.primasreservas = primasreservas;
		this.ingpagosdifmonedas = ingpagosdifmonedas;
		this.otrosingresos = otrosingresos;
		this.totalingresos = totalingresos;
		this.ventaspagbanco = ventaspagbanco;
		this.abonospagbanco = abonospagbanco;
		this.primaspagbanco = primaspagbanco;
		this.otrosegresos = otrosegresos;
		this.totalegresos = totalegresos;
		this.efecnetorecibido = efecnetorecibido;
		this.minimoencaja = minimoencaja;
		this.depositosugerido = depositosugerido;
		this.efecencaja = efecencaja;
		this.faltantesobrante = faltantesobrante;
		this.depositofinal = depositofinal;
		this.tefectivo = tefectivo;
		this.tcheque = tcheque;
		this.ttcredito = ttcredito;
		this.tdepositobanco = tdepositobanco;
		this.ttransfbanco = ttransfbanco;
		this.tformaspago = tformaspago;
	}

	public double getAbonos() {
		return abonos;
	}

	public void setAbonos(double abonos) {
		this.abonos = abonos;
	}

	public double getAbonospagbanco() {
		return abonospagbanco;
	}

	public void setAbonospagbanco(double abonospagbanco) {
		this.abonospagbanco = abonospagbanco;
	}

	public double getDepositofinal() {
		return depositofinal;
	}

	public void setDepositofinal(double depositofinal) {
		this.depositofinal = depositofinal;
	}

	public double getDepositosugerido() {
		return depositosugerido;
	}

	public void setDepositosugerido(double depositosugerido) {
		this.depositosugerido = depositosugerido;
	}

	public double getDevoluciones() {
		return devoluciones;
	}

	public void setDevoluciones(double devoluciones) {
		this.devoluciones = devoluciones;
	}

	public double getEfecencaja() {
		return efecencaja;
	}

	public void setEfecencaja(double efecencaja) {
		this.efecencaja = efecencaja;
	}

	public double getEfecnetorecibido() {
		return efecnetorecibido;
	}

	public void setEfecnetorecibido(double efecnetorecibido) {
		this.efecnetorecibido = efecnetorecibido;
	}

	public double getFaltantesobrante() {
		return faltantesobrante;
	}

	public void setFaltantesobrante(double faltantesobrante) {
		this.faltantesobrante = faltantesobrante;
	}

	public double getIngpagosdifmonedas() {
		return ingpagosdifmonedas;
	}

	public void setIngpagosdifmonedas(double ingpagosdifmonedas) {
		this.ingpagosdifmonedas = ingpagosdifmonedas;
	}

	public double getMinimoencaja() {
		return minimoencaja;
	}

	public void setMinimoencaja(double minimoencaja) {
		this.minimoencaja = minimoencaja;
	}

	public double getOtrosegresos() {
		return otrosegresos;
	}

	public void setOtrosegresos(double otrosegresos) {
		this.otrosegresos = otrosegresos;
	}

	public double getOtrosingresos() {
		return otrosingresos;
	}

	public void setOtrosingresos(double otrosingresos) {
		this.otrosingresos = otrosingresos;
	}

	public double getPrimaspagbanco() {
		return primaspagbanco;
	}

	public void setPrimaspagbanco(double primaspagbanco) {
		this.primaspagbanco = primaspagbanco;
	}

	public double getPrimasreservas() {
		return primasreservas;
	}

	public void setPrimasreservas(double primasreservas) {
		this.primasreservas = primasreservas;
	}

	public double getTcheque() {
		return tcheque;
	}

	public void setTcheque(double tcheque) {
		this.tcheque = tcheque;
	}
	public double getTdepositobanco() {
		return tdepositobanco;
	}

	public void setTdepositobanco(double tdepositobanco) {
		this.tdepositobanco = tdepositobanco;
	}

	public double getTefectivo() {
		return tefectivo;
	}

	public void setTefectivo(double tefectivo) {
		this.tefectivo = tefectivo;
	}

	public double getTformaspago() {
		return tformaspago;
	}

	public void setTformaspago(double tformaspago) {
		this.tformaspago = tformaspago;
	}

	public double getTotalegresos() {
		return totalegresos;
	}

	public void setTotalegresos(double totalegresos) {
		this.totalegresos = totalegresos;
	}

	public double getTotalingresos() {
		return totalingresos;
	}

	public void setTotalingresos(double totalingresos) {
		this.totalingresos = totalingresos;
	}

	public double getTtcredito() {
		return ttcredito;
	}

	public void setTtcredito(double ttcredito) {
		this.ttcredito = ttcredito;
	}

	public double getTtransfbanco() {
		return ttransfbanco;
	}

	public void setTtransfbanco(double ttransfbanco) {
		this.ttransfbanco = ttransfbanco;
	}

	public double getVentascredito() {
		return ventascredito;
	}

	public void setVentascredito(double ventascredito) {
		this.ventascredito = ventascredito;
	}

	public double getVentasnetas() {
		return ventasnetas;
	}

	public void setVentasnetas(double ventasnetas) {
		this.ventasnetas = ventasnetas;
	}

	public double getVentaspagbanco() {
		return ventaspagbanco;
	}

	public void setVentaspagbanco(double ventaspagbanco) {
		this.ventaspagbanco = ventaspagbanco;
	}

	public double getVentastotales() {
		return ventastotales;
	}

	public void setVentastotales(double ventastotales) {
		this.ventastotales = ventastotales;
	}

	public Date getFechaarqueo() {
		return fechaarqueo;
	}

	public void setFechaarqueo(Date fechaarqueo) {
		this.fechaarqueo = fechaarqueo;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public int getNocaja() {
		return nocaja;
	}

	public void setNocaja(int nocaja) {
		this.nocaja = nocaja;
	}

	public String getNombrecajero() {
		return nombrecajero;
	}

	public void setNombrecajero(String nombrecajero) {
		this.nombrecajero = nombrecajero;
	}

	public String getCompania() {
		return compania;
	}

	public void setCompania(String compania) {
		this.compania = compania;
	}

	public String getNombrecaja() {
		return nombrecaja;
	}

	public void setNombrecaja(String nombrecaja) {
		this.nombrecaja = nombrecaja;
	}

	public String getSucursal() {
		return sucursal;
	}

	public void setSucursal(String sucursal) {
		this.sucursal = sucursal;
	}

	public int getNoarqueo() {
		return noarqueo;
	}

	public void setNoarqueo(int noarqueo) {
		this.noarqueo = noarqueo;
	}

	public String getFechahora() {
		return fechahora;
	}

	public void setFechahora(String fechahora) {
		this.fechahora = fechahora;
	}

	public String getSCambioOtraMoneda() {
		return sCambioOtraMoneda;
	}

	public void setSCambioOtraMoneda(String cambioOtraMoneda) {
		sCambioOtraMoneda = cambioOtraMoneda;
	}

	public String getSFinanciamientosBanco() {
		return sFinanciamientosBanco;
	}

	public void setSFinanciamientosBanco(String financiamientosBanco) {
		sFinanciamientosBanco = financiamientosBanco;
	}

	public String getSIngresosExtBanco() {
		return sIngresosExtBanco;
	}

	public void setSIngresosExtBanco(String ingresosExtBanco) {
		sIngresosExtBanco = ingresosExtBanco;
	}

	public String getSPagoFinanciamiento() {
		return sPagoFinanciamiento;
	}

	public void setSPagoFinanciamiento(String pagoFinanciamiento) {
		sPagoFinanciamiento = pagoFinanciamiento;
	}

	public String getSPagoIngresosExt() {
		return sPagoIngresosExt;
	}

	public void setSPagoIngresosExt(String pagoIngresosExt) {
		sPagoIngresosExt = pagoIngresosExt;
	}

	public String getSReferenciaDeposito() {
		return sReferenciaDeposito;
	}

	public void setSReferenciaDeposito(String referenciaDeposito) {
		sReferenciaDeposito = referenciaDeposito;
	}

	public String getSTipoImpresionRpt() {
		return sTipoImpresionRpt;
	}

	public void setSTipoImpresionRpt(String tipoImpresionRpt) {
		sTipoImpresionRpt = tipoImpresionRpt;
	}

	public double getTtcreditom() {
		return ttcreditom;
	}

	public void setTtcreditom(double ttcreditom) {
		this.ttcreditom = ttcreditom;
	}

	public double getTtcreditosktpos() {
		return ttcreditosktpos;
	}

	public void setTtcreditosktpos(double ttcreditosktpos) {
		this.ttcreditosktpos = ttcreditosktpos;
	}
	
	
}

package com.casapellas.entidades;

import java.math.BigDecimal;
import java.util.List;

public class Hfactura implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7283754572115012057L;
	
	private int nofactura;
	private String tipofactura;
	private int codcli;
	private String nomcli;
	private String codunineg;
	private String unineg;
	private String codsuc;
	private String nomsuc;
	private String codcomp;
	private String nomcomp;
	private String fecha;
	private double subtotal;
	private String moneda;
	
	private BigDecimal tasa;
	private String tipopago;
	private double cpendiente;
	private double dpendiente;	
	
	private String fechagrab;
	//private int horagrab;
	private String hechopor;
	private String pantalla;
	private double iva;
	private double total;
	private String equiv;
//	private String equiv2;
	private List<Dfactura> dfactura;
	private String montoAplicar;
	private String partida;
	
	private int codVendedor;

	
	private int nodoco;
	private String tipodoco;
	private String estado;
	private String pago;
	
	private int nodev;
	private int nofact;	
	private int fechadev;
	private int fechafac;
	
	private double totalf;
	private double subtotalf;
	
	private BigDecimal totaldomtmp;
	private int fechajulian ;
	
	private String sdlocn;
	private String tipofacdev;
	private String estadodesc;
	
	 private Integer fechadoco;
	 
	 private int horaEntera;
	
	public Hfactura() {
	}
	
	public Hfactura(int nofactura, String tipofactura, int codcli,
			String nomcli, String codunineg, String unineg, String codsuc,
			String nomsuc, String codcomp, String nomcomp, String fecha,
			double subtotal, String moneda, String fechagrab, /*int horagrab,*/
			String hechopor, String pantalla) {
		this.nofactura = nofactura;
		this.tipofactura = tipofactura;
		this.codcli = codcli;
		this.nomcli = nomcli;
		this.codunineg = codunineg;
		this.unineg = unineg;
		this.codsuc = codsuc;
		this.nomsuc = nomsuc;
		this.codcomp = codcomp;
		this.nomcomp = nomcomp;
		this.fecha = fecha;
		this.subtotal = subtotal;
		this.moneda = moneda;
		this.fechagrab = fechagrab;
		//this.horagrab = horagrab;
		this.hechopor = hechopor;
		this.pantalla = pantalla;
	}
	public Hfactura(int nofactura, String tipofactura, int codcli,
			String nomcli, String codunineg, String unineg, String codsuc,
			String nomsuc, String codcomp, String nomcomp, String fecha,
			double subtotal, String moneda,BigDecimal tasa,String tipopago,double cpendiente, double dpendiente, String fechagrab, /*int horagrab,*/
			String hechopor, String pantalla,double iva, double total,String equiv,String partida, List dfactura, int nodoco,String tipodoco,
			String estado,String pago,int codVendedor,double totalf, int fechajulian,String sdlocn,String tipofacdev,String estadodesc,BigDecimal totaldomtmp) {
		this.nofactura = nofactura;
		this.tipofactura = tipofactura;
		this.codcli = codcli;
		this.nomcli = nomcli;
		this.codunineg = codunineg;
		this.unineg = unineg;
		this.codsuc = codsuc;
		this.nomsuc = nomsuc;
		this.codcomp = codcomp;
		this.nomcomp = nomcomp;
		this.fecha = fecha;
		this.subtotal = subtotal;
		this.moneda = moneda; 
		this.tasa = tasa;
		this.tipopago = tipopago;
		this.cpendiente = cpendiente;
		this.dpendiente = dpendiente;
		this.fechagrab = fechagrab;
		//this.horagrab = horagrab;
		this.hechopor = hechopor;
		this.pantalla = pantalla;
		this.iva = iva;
		this.total = total;
		this.equiv = equiv;
		this.partida = partida;
		this.dfactura = dfactura;
		this.nodoco = nodoco;
		this.tipodoco = tipodoco;
		this.estado = estado;
		this.pago = pago;
		this.codVendedor = codVendedor;
		this.totalf = totalf;
		this.fechajulian = fechajulian;
		this.sdlocn = sdlocn;
		this.tipofacdev = tipofacdev;
		this.estadodesc = estadodesc;
		this.totaldomtmp = totaldomtmp;
	}
	
	public Hfactura(int nofactura, String tipofactura, int codcli,
			String nomcli, String codunineg, String unineg, String codsuc,
			String nomsuc, String codcomp, String nomcomp, String fecha,
			double subtotal, double subtotalf, String moneda, BigDecimal tasa,
			String tipopago, double cpendiente, double dpendiente,
			String fechagrab, String hechopor, String pantalla, double iva,
			double total, String equiv, String partida, List dfactura,
			int nodoco, String tipodoco, String estado, String pago,
			int codVendedor, double totalf, int fechajulian, String sdlocn,
			String tipofacdev, String estadodesc, BigDecimal totaldomtmp, int fechadoco) {
		this.nofactura = nofactura;
		this.tipofactura = tipofactura;
		this.codcli = codcli;
		this.nomcli = nomcli;
		this.codunineg = codunineg;
		this.unineg = unineg;
		this.codsuc = codsuc;
		this.nomsuc = nomsuc;
		this.codcomp = codcomp;
		this.nomcomp = nomcomp;
		this.fecha = fecha;
		this.subtotal = subtotal;
		this.subtotalf = subtotalf;
		this.moneda = moneda;
		this.tasa = tasa;
		this.tipopago = tipopago;
		this.cpendiente = cpendiente;
		this.dpendiente = dpendiente;
		this.fechagrab = fechagrab;
		this.hechopor = hechopor;
		this.pantalla = pantalla;
		this.iva = iva;
		this.total = total;
		this.equiv = equiv; 
		this.partida = partida;
		this.dfactura = dfactura;
		this.nodoco = nodoco;
		this.tipodoco = tipodoco;
		this.estado = estado;
		this.pago = pago;
		this.codVendedor = codVendedor;
		this.totalf = totalf;
		this.fechajulian = fechajulian;
		this.sdlocn = sdlocn;
		this.tipofacdev = tipofacdev;
		this.estadodesc = estadodesc;
		this.totaldomtmp = totaldomtmp;
		this.fechadoco = fechadoco;
	}
	
	public Hfactura(int nofactura, String tipofactura, int codcli,
			String nomcli, String codunineg, String unineg, String codsuc,
			String nomsuc, String codcomp, String nomcomp, String fecha,
			double subtotal, String moneda,BigDecimal tasa,String tipopago,double cpendiente, double dpendiente, String fechagrab, /*int horagrab,*/
			String hechopor, String pantalla,double iva, double total,String equiv,String partida, List dfactura, int nodoco,String tipodoco,
			String estado,String pago,int codVendedor,double totalf, BigDecimal totaldomtmp, int fechajulian, int horaEntera) {
		this.nofactura = nofactura;
		this.tipofactura = tipofactura;
		this.codcli = codcli;
		this.nomcli = nomcli;
		this.codunineg = codunineg;
		this.unineg = unineg;
		this.codsuc = codsuc;
		this.nomsuc = nomsuc;
		this.codcomp = codcomp;
		this.nomcomp = nomcomp;
		this.fecha = fecha;
		this.subtotal = subtotal;
		this.moneda = moneda;
		this.tasa = tasa;
		this.tipopago = tipopago;
		this.cpendiente = cpendiente;
		this.dpendiente = dpendiente;
		this.fechagrab = fechagrab;
		//this.horagrab = horagrab;
		this.hechopor = hechopor;
		this.pantalla = pantalla;
		this.iva = iva;
		this.total = total;
		this.equiv = equiv; 
		this.partida = partida;
		this.dfactura = dfactura;
		this.nodoco = nodoco;
		this.tipodoco = tipodoco;
		this.estado = estado;
		this.pago = pago;
		this.codVendedor = codVendedor;
		this.totalf = totalf;
		this.totaldomtmp = totaldomtmp;
		this.fechajulian = fechajulian;
		this.horaEntera = horaEntera;
	}
 
	
	
	public Hfactura(int nofactura, String tipofactura, int codcli,
			String nomcli, String codunineg, String unineg, String codsuc,
			String nomsuc, String codcomp, String nomcomp, String fecha,
			double subtotal, String moneda,BigDecimal tasa,String tipopago,double cpendiente, double dpendiente, String fechagrab, /*int horagrab,*/
			String hechopor, String pantalla,double iva, double total,String equiv, List dfactura,String montoAplicar, String partida,double totalf) {
		this.nofactura = nofactura;
		this.tipofactura = tipofactura;
		this.codcli = codcli;
		this.nomcli = nomcli;
		this.codunineg = codunineg;
		this.unineg = unineg;
		this.codsuc = codsuc;
		this.nomsuc = nomsuc;
		this.codcomp = codcomp;
		this.nomcomp = nomcomp;
		this.fecha = fecha;
		this.subtotal = subtotal;
		this.moneda = moneda;
		this.tasa = tasa;
		this.tipopago = tipopago;
		this.cpendiente = cpendiente;
		this.dpendiente = dpendiente;
		this.fechagrab = fechagrab;
		//this.horagrab = horagrab;
		this.hechopor = hechopor;
		this.pantalla = pantalla;
		this.iva = iva;
		this.total = total;
		this.equiv = equiv; 
		this.montoAplicar = montoAplicar;
		this.partida = partida;
		this.dfactura = dfactura;
		this.totalf = totalf;
	}
	
/** ********************** CONSTRUCTOR PARA FORMATO DE DEVOLUCIÓN ********************************* **/
	public Hfactura(int nofactura, String tipofactura, int codcli,
			String nomcli, String codunineg, String unineg, String codsuc,
			String nomsuc, String codcomp, String nomcomp, String fecha,
			double subtotal, String moneda,BigDecimal tasa,String tipopago,double cpendiente, double dpendiente, String fechagrab, 
			String hechopor, String pantalla,double iva, double total,String equiv,String partida, List dfactura, int nodoco,
			String tipodoco,String estado,String pago,int nodev,int nofact,int fechadev,int fechafac,double totalf, BigDecimal totaldomtmp, int fechajulian) {
		this.nofactura = nofactura;
		this.tipofactura = tipofactura;
		this.codcli = codcli;
		this.nomcli = nomcli;
		this.codunineg = codunineg;
		this.unineg = unineg;
		this.codsuc = codsuc;
		this.nomsuc = nomsuc;
		this.codcomp = codcomp;
		this.nomcomp = nomcomp;
		this.fecha = fecha;
		this.subtotal = subtotal;
		this.moneda = moneda;
		this.tasa = tasa;
		this.tipopago = tipopago;
		this.cpendiente = cpendiente;
		this.dpendiente = dpendiente;
		this.fechagrab = fechagrab;	
		this.hechopor = hechopor;
		this.pantalla = pantalla;
		this.iva = iva;
		this.total = total;
		this.equiv = equiv; 
		this.partida = partida;
		this.dfactura = dfactura;
		this.nodoco = nodoco;
		this.tipodoco = tipodoco;
		this.estado = estado;
		this.pago = pago;
		this.nodev = nodev;
		this.nofact = nofact;
		this.fechadev = fechadev;
		this.fechafac = fechafac;
		this.totalf = totalf;
		this.totaldomtmp = totaldomtmp;
		this.fechajulian = fechajulian;
	}
	
	
public Hfactura(int nofactura, String tipofactura, int codcli, String nomcli,
		String codunineg, String unineg, String codsuc, String nomsuc,
		String codcomp, String nomcomp, String fecha, double subtotal,
		String moneda, BigDecimal tasa, String tipopago, double cpendiente,
		double dpendiente, String fechagrab, String hechopor, String pantalla,
		double iva, double total, String equiv, List dfactura,
		String montoAplicar, String partida, int codVendedor, int nodoco,
		String tipodoco, String estado, String pago, int nodev, int nofact,
		int fechadev, int fechafac, double totalf, double subtotalf,
		BigDecimal totaldomtmp, int fechajulian) {
	super();
	this.nofactura = nofactura;
	this.tipofactura = tipofactura;
	this.codcli = codcli;
	this.nomcli = nomcli;
	this.codunineg = codunineg;
	this.unineg = unineg;
	this.codsuc = codsuc;
	this.nomsuc = nomsuc;
	this.codcomp = codcomp;
	this.nomcomp = nomcomp;
	this.fecha = fecha;
	this.subtotal = subtotal;
	this.moneda = moneda;
	this.tasa = tasa;
	this.tipopago = tipopago;
	this.cpendiente = cpendiente;
	this.dpendiente = dpendiente;
	this.fechagrab = fechagrab;
	this.hechopor = hechopor;
	this.pantalla = pantalla;
	this.iva = iva;
	this.total = total;
	this.equiv = equiv; 
	this.dfactura = dfactura;
	this.montoAplicar = montoAplicar;
	this.partida = partida;
	this.codVendedor = codVendedor;
	this.nodoco = nodoco;
	this.tipodoco = tipodoco;
	this.estado = estado;
	this.pago = pago;
	this.nodev = nodev;
	this.nofact = nofact;
	this.fechadev = fechadev;
	this.fechafac = fechafac;
	this.totalf = totalf;
	this.subtotalf = subtotalf;
	this.totaldomtmp = totaldomtmp;
	this.fechajulian = fechajulian;
}

	@Override
	public Hfactura clone() {
		return new Hfactura(nofactura, tipofactura, codcli, nomcli, codunineg,
				codunineg, codsuc, nomsuc, codcomp, nomcomp, fecha, subtotal,
				moneda, tasa, tipopago, cpendiente, dpendiente, fechagrab,
				hechopor, pantalla, iva, subtotal, equiv, dfactura,
				montoAplicar, partida, codVendedor, nodoco, tipodoco, estado,
				pago, nodev, nofact, fechadev, fechafac, totalf, subtotalf,
				totaldomtmp, fechajulian);
	}

	public int getCodcli() {
		return codcli;
	}

	public void setCodcli(int codcli) {
		this.codcli = codcli;
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

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getFechagrab() {
		return fechagrab;
	}

	public void setFechagrab(String fechagrab) {
		this.fechagrab = fechagrab;
	}

	public String getHechopor() {
		return hechopor;
	}

	public void setHechopor(String hechopor) {
		this.hechopor = hechopor;
	}

	/*public int getHoragrab() {
		return horagrab;
	}

	public void setHoragrab(int horagrab) {
		this.horagrab = horagrab;
	}*/

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public int getNofactura() {
		return nofactura;
	}

	public void setNofactura(int nofactura) {
		this.nofactura = nofactura;
	}

	public String getNomcli() {
		return nomcli;
	}

	public void setNomcli(String nomcli) {
		this.nomcli = nomcli;
	}

	public String getNomcomp() {
		return nomcomp;
	}

	public void setNomcomp(String nomcomp) {
		this.nomcomp = nomcomp;
	}

	public String getNomsuc() {
		return nomsuc;
	}

	public void setNomsuc(String nomsuc) {
		this.nomsuc = nomsuc;
	}

	public String getPantalla() {
		return pantalla;
	}

	public void setPantalla(String pantalla) {
		this.pantalla = pantalla;
	}

	public double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	public String getTipofactura() {
		return tipofactura;
	}

	public void setTipofactura(String tipofactura) {
		this.tipofactura = tipofactura;
	}

	public String getUnineg() {
		return unineg;
	}

	public void setUnineg(String unineg) {
		this.unineg = unineg;
	}

 

	public List<Dfactura> getDfactura() {
		return dfactura;
	}

	public void setDfactura(List<Dfactura> dfactura) {
		this.dfactura = dfactura;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public double getIva() {
		return iva;
	}

	public void setIva(double iva) {
		this.iva = iva;
	}

	public double getCpendiente() {
		return cpendiente;
	}

	public void setCpendiente(double cpendiente) {
		this.cpendiente = cpendiente;
	}

	public double getDpendiente() {
		return dpendiente;
	}

	public void setDpendiente(double dpendiente) {
		this.dpendiente = dpendiente;
	}

	public BigDecimal getTasa() {
		return tasa;
	}

	public void setTasa(BigDecimal tasa) {
		this.tasa = tasa;
	}

	public String getTipopago() {
		return tipopago;
	}

	public void setTipopago(String tipopago) {
		this.tipopago = tipopago;
	}

	public String getEquiv() {
		return equiv;
	}

	public void setEquiv(String equiv) {
		this.equiv = equiv;
	}

//	public String getEquiv2() {
//		return equiv2;
//	}

//	public void setEquiv2(String equiv2) {
//		this.equiv2 = equiv2;
//	}
	
	public String getMontoAplicar() {
		return montoAplicar;
	}

	public void setMontoAplicar(String montoAplicar) {
		this.montoAplicar = montoAplicar;
	}

	public String getPartida() {
		return partida;
	}

	public void setPartida(String partida) {
		this.partida = partida;
	}

	public int getNodoco() {
		return nodoco;
	}

	public void setNodoco(int nodoco) {
		this.nodoco = nodoco;
	}

	public String getTipodoco() {
		return tipodoco;
	}

	public void setTipodoco(String tipodoco) {
		this.tipodoco = tipodoco;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getPago() {
		return pago;
	}

	public void setPago(String pago) {
		this.pago = pago;
	}

	public int getNodev() {
		return nodev;
	}

	public void setNodev(int nodev) {
		this.nodev = nodev;
	}

	public int getNofact() {
		return nofact;
	}

	public void setNofact(int nofact) {
		this.nofact = nofact;
	}

	public int getFechadev() {
		return fechadev;
	}

	public void setFechadev(int fechadev) {
		this.fechadev = fechadev;
	}

	public int getFechafac() {
		return fechafac;
	}

	public void setFechafac(int fechafac) {
		this.fechafac = fechafac;
	}

	public int getCodVendedor() {
		return codVendedor;
	}

	public void setCodVendedor(int codVendedor) {
		this.codVendedor = codVendedor;
	}

	public double getTotalf() {
		return totalf;
	}

	public void setTotalf(double totalf) {
		this.totalf = totalf;
	}

	public double getSubtotalf() {
		return subtotalf;
	}

	public void setSubtotalf(double subtotalf) {
		this.subtotalf = subtotalf;
	}

	public BigDecimal getTotaldomtmp() {
		return totaldomtmp;
	}

	public void setTotaldomtmp(BigDecimal totaldomtmp) {
		this.totaldomtmp = totaldomtmp;
	}

	public int getFechajulian() {
		return fechajulian;
	}

	public void setFechajulian(int fechajulian) {
		this.fechajulian = fechajulian;
	}

	public String getSdlocn() {
		return sdlocn;
	}

	public void setSdlocn(String sdlocn) {
		this.sdlocn = sdlocn;
	}

	public String getTipofacdev() {
		return tipofacdev;
	}

	public void setTipofacdev(String tipofacdev) {
		this.tipofacdev = tipofacdev;
	}

	public String getEstadodesc() {
		return estadodesc;
	}

	public void setEstadodesc(String estadodesc) {
		this.estadodesc = estadodesc;
	}

	public Integer getFechadoco() {
		return fechadoco;
	}

	public void setFechadoco(Integer fechadoco) {
		this.fechadoco = fechadoco;
	}
	public int getHoraEntera() {
		return horaEntera;
	}

	public void setHoraEntera(int p_horaEntera) {
		horaEntera = p_horaEntera;
	}
	
}


package com.casapellas.entidades;

import java.util.Date;
import java.util.List;

/**
 * CASA PELLAS S.A.
 * Creado por.........: Carlos Manuel Hernández Morrison
 * Fecha de Creación..: 03/08/2009
 * Última modificación: Carlos Manuel Hernández Morrison 
 * Modificado por.....:	08/03/2010
 * Descripcion:.......: Datos que conforman toda la estructura del arqueo.
 * 
 */

public class DetalleArqueoCaja implements java.io.Serializable {
	
	private static final long serialVersionUID = -184479470605041288L;
	private int caid,noarqueo;
	private String codcomp;
	private String codsuc;
	private String moneda;
	private Date fechaarqueo;
	private Date horaarqueo;
	private double depositoFinal;
	private String depositoRefer;
	
	//----- LISTAS DE FACTURAS Y RECIBOS ----------//
	private List TodasFacturas, TodasVentas, FacCredito, FacContado, TodasDev,DevCredito, DevContado;	
	private List TodosRecibos, RecibosContado, RecibosCredito,RecibosPrimas, recibosIngEx,RecibosCambio;	
	private List recotc,recodb,recotb,recrtc,recrdb,recrtb,reca,reprtc, reprdb, reprtb,reextc,reexdb,reextb;
	private List recibosFinan,refntc,refndb,refntb,vrefntc,vrefndb,vrefntb;
	private List vrecotc,vrecodb,vrecotb,vrecrtc,vrecrdb,vrecrtb, vreprtc,vreprdb,vreprtb, vreietc,vreiedb,vreietb;

	// ---- TOTALES POR RECIBOS Y FACTURAS -------//
	private double totalFact,totalFactco, totalFactcr;
	private double totalDev,totalDevco,totalDevcr;
	private double totalreco,totalrecr,totalcambio,totalrepr,totalreex, totalrefn;
	private double totalcotc,totalcodb,totalcotb;
	private double totalcrtc,totalcrdb,totalcrtb;
	private double totalprtc,totalprdb,totalprtb;
	private double totalextc,totalexdb,totalextb;
	private double totalfntc,totalfndb,totalfntb;
	private double totalventaspb, totalabonospb,totalotrosegresos,totalprimaspb,totalIngextpb;
	private double totalfinanpb;
	private double totalotrosingresos;
	private List lstotrosingresos;
	
	// ---- TOTALES POR MÉTODO DE PAGO ---------//	
	private double tefectivo,tcheque,ttarcredito,ttarcreditom,ttarcreditos ,tdepbanco,ttransbanco;
	private List refectivo, rcheque, rtcredito, rtcreditom, rtcreditos ,rdepbanco, rtransbanco;
	private List vrefectivo,vrcheque, vrtcredito, vrtcreditom, vrtcreditos, vrdepbanco, vrtransbanco;

	
	// ---- LISTAS Y TOTALES DE RECIBOS PAGADOS CON MONEDA DISTINTA DE LA FACTURA --//
	private List ingrepagotramon,egrgrepagotramon;
	private double totalinrcpom,totalegrcpom;
	
	private List reccambiomixto;
	private double totalcambiomixto;
	private double dTotalsalidas,dTotalSalidaQ,dTotalSalida5;				
	private List lstSalidas;
	
	private int referencenumber;
	
	//&& ============= adaptacion pmt
	private List recibosAnticiposPMT;
	private List repmtc,repmdb,repmtb ;
	
	private double totalrepm;
	private double totalpmtc,totalpmdb,totalpmtb;
	private double totalpmpb;
	
	public DetalleArqueoCaja(){		
	}
	
	public DetalleArqueoCaja(List todasFacturas, List todasVentas, List facCredito, List facContado,List todasDev, 
							 List devCredito, List devContado, List todosRecibos, List recibosContado, 
							 List recibosCredito, List recibosCambio,List recibosIngEx, List recotc, List recodb,
							 List recotb, List recrtc, List recrdb, List recrtb, List reca,
							 List vrecotc,List vrecodb,List vrecotb,List vrecrtc,List vrecrdb,List vrecrtb,
							 List refectivo, List rcheque, List rtcredito, List rdepbanco, List rtransbanco,
							 List vrefectivo, List vrcheque, List vrtcredito, List vrdepbanco, List vrtransbanco,							 
							 double totalFact, double totalFactco, double totalFactcr, double totalDev, 
							 double totalcambio, double totalcotc, double totalcodb, double totalcotb, 
							 double totalcrtc, double totalcrdb, double totalcrtb,double totalreco,double totalrecr,
							 double totalventaspb, double totalabonospb, double totalotrosegresos,
							 double tefectivo, double tcheque, double ttarcredito,double tdepbanco,double ttransbanco,
							 double totalDevco,double totalDevcr,List ingrepagotramon, List egrgrepagotramon,
							 double totalinrcpom,double totalegrcpom,String moneda,List reccambiomixto,
							 double totalcambiomixto,double totalotrosingresos,List lstotrosingresos,double totalreex ) {
		super();
		TodasFacturas = todasFacturas;
		FacCredito = facCredito;
		FacContado = facContado; 
		TodasDev   = todasDev;
		DevCredito = devCredito;
		DevContado = devContado;
		TodosRecibos = todosRecibos;
		RecibosContado = recibosContado;
		RecibosCredito = recibosCredito;
		RecibosCambio = recibosCambio;
		TodasVentas = todasVentas;
		this.recibosIngEx = recibosIngEx;
		
		this.recotc = recotc;
		this.recodb = recodb;
		this.recotb = recotb;
		this.recrtc = recrtc;
		this.recrdb = recrdb;
		this.recrtb = recrtb;
		
		this.vrecotc = vrecotc;
		this.vrecodb = vrecodb;
		this.vrecotb = vrecotb; 
		this.vrecrtc = vrecrtc;
		this.vrecrdb = vrecrdb;
		this.vrecrtb = vrecrtb;
		
		this.refectivo	 = refectivo;
		this.rcheque 	 = rcheque ;
		this.rtcredito 	 = rtcredito;
		this.rdepbanco   = rdepbanco;
		this.rtransbanco = rtransbanco;
		this.vrefectivo	 = vrefectivo;
		this.vrcheque	 = vrcheque;
		this.vrtcredito	 = vrtcredito;
		this.vrdepbanco  = vrdepbanco;
		this.vrtransbanco = vrtransbanco;	
			
		this.reca = reca;
		this.totalFact = totalFact;
		this.totalFactco = totalFactco;
		this.totalFactcr = totalFactcr;
		this.totalDev = totalDev;
		this.totalcambio = totalcambio;
		this.totalcotc = totalcotc;
		this.totalcodb = totalcodb;
		this.totalcotb = totalcotb;
		this.totalcrtc = totalcrtc;
		this.totalcrdb = totalcrdb;
		this.totalcrtb = totalcrtb;
		this.totalreco = totalreco;
		this.totalrecr = totalrecr;
		this.totalreex = totalreex;
		this.totalventaspb = totalventaspb; 
		this.totalabonospb = totalabonospb;
		this.totalotrosegresos = totalotrosegresos;
		this.tefectivo = tefectivo;
		this.tcheque = tcheque;
		this.ttarcredito = ttarcredito;
		this.tdepbanco = tdepbanco;
		this.ttransbanco = ttransbanco;
		this.totalDevco = totalDevco;
		this.totalDevcr = totalDevcr;
		
		this.ingrepagotramon = ingrepagotramon;
		this.egrgrepagotramon = egrgrepagotramon;
		this.totalinrcpom 	  = totalinrcpom;
		this.totalegrcpom 	  = totalegrcpom;
		this.moneda 		  = moneda;
		
		this.reccambiomixto   = reccambiomixto;
		this.totalcambiomixto = totalcambiomixto;
		this.totalotrosingresos  = totalotrosingresos;
		this.lstotrosingresos = lstotrosingresos;
	}

	public List getDevContado() {
		return DevContado;
	}
	public void setDevContado(List devContado) {
		DevContado = devContado;
	}
	public List getDevCredito() {
		return DevCredito;
	}
	public void setDevCredito(List devCredito) {
		DevCredito = devCredito;
	}
	public List getFacContado() {
		return FacContado;
	}
	public void setFacContado(List facContado) {
		FacContado = facContado;
	}
	public List getFacCredito() {
		return FacCredito;
	}
	public void setFacCredito(List facCredito) {
		FacCredito = facCredito;
	}
	public List getReca() {
		return reca;
	}
	public void setReca(List reca) {
		this.reca = reca;
	}
	public List getRecibosCambio() {
		return RecibosCambio;
	}
	public void setRecibosCambio(List recibosCambio) {
		RecibosCambio = recibosCambio;
	}
	public List getRecibosContado() {
		return RecibosContado;
	}
	public void setRecibosContado(List recibosContado) {
		RecibosContado = recibosContado;
	}
	public List getRecibosCredito() {
		return RecibosCredito;
	}
	public void setRecibosCredito(List recibosCredito) {
		RecibosCredito = recibosCredito;
	}
	public List getRecodb() {
		return recodb;
	}
	public void setRecodb(List recodb) {
		this.recodb = recodb;
	}
	public List getRecotb() {
		return recotb;
	}
	public void setRecotb(List recotb) {
		this.recotb = recotb;
	}
	public List getRecotc() {
		return recotc;
	}
	public void setRecotc(List recotc) {
		this.recotc = recotc;
	}
	public List getRecrdb() {
		return recrdb;
	}
	public void setRecrdb(List recrdb) {
		this.recrdb = recrdb;
	}
	public List getRecrtb() {
		return recrtb;
	}
	public void setRecrtb(List recrtb) {
		this.recrtb = recrtb;
	}
	public List getRecrtc() {
		return recrtc;
	}
	public void setRecrtc(List recrtc) {
		this.recrtc = recrtc;
	}
	public List getTodasFacturas() {
		return TodasFacturas;
	}
	public void setTodasFacturas(List todasFacturas) {
		TodasFacturas = todasFacturas;
	}
	public List getTodosRecibos() {
		return TodosRecibos;
	}
	public void setTodosRecibos(List todosRecibos) {
		TodosRecibos = todosRecibos;
	}
	public double getTotalcambio() {
		return totalcambio;
	}
	public void setTotalcambio(double totalcambio) {
		this.totalcambio = totalcambio;
	}
	public double getTotalcodb() {
		return totalcodb;
	}
	public void setTotalcodb(double totalcodb) {
		this.totalcodb = totalcodb;
	}
	public double getTotalcotb() {
		return totalcotb;
	}
	public void setTotalcotb(double totalcotb) {
		this.totalcotb = totalcotb;
	}
	public double getTotalcotc() {
		return totalcotc;
	}
	public void setTotalcotc(double totalcotc) {
		this.totalcotc = totalcotc;
	}
	public double getTotalcrdb() {
		return totalcrdb;
	}
	public void setTotalcrdb(double totalcrdb) {
		this.totalcrdb = totalcrdb;
	}
	public double getTotalcrtb() {
		return totalcrtb;
	}
	public void setTotalcrtb(double totalcrtb) {
		this.totalcrtb = totalcrtb;
	}
	public double getTotalcrtc() {
		return totalcrtc;
	}
	public void setTotalcrtc(double totalcrtc) {
		this.totalcrtc = totalcrtc;
	}
	public double getTotalDev() {
		return totalDev;
	}
	public void setTotalDev(double totalDev) {
		this.totalDev = totalDev;
	}
	public double getTotalFact() {
		return totalFact;
	}
	public void setTotalFact(double totalFact) {
		this.totalFact = totalFact;
	}
	public double getTotalFactco() {
		return totalFactco;
	}
	public void setTotalFactco(double totalFactco) {
		this.totalFactco = totalFactco;
	}
	public double getTotalFactcr() {
		return totalFactcr;
	}
	public void setTotalFactcr(double totalFactcr) {
		this.totalFactcr = totalFactcr;
	}
	public List getTodasDev() {
		return TodasDev;
	}
	public void setTodasDev(List todasDev) {
		TodasDev = todasDev;
	}
	public double getTotalreco() {
		return totalreco;
	}
	public void setTotalreco(double totalreco) {
		this.totalreco = totalreco;
	}
	public double getTotalrecr() {
		return totalrecr;
	}
	public void setTotalrecr(double totalrecr) {
		this.totalrecr = totalrecr;
	}
	public double getTotalabonospb() {
		return totalabonospb;
	}
	public void setTotalabonospb(double totalabonospb) {
		this.totalabonospb = totalabonospb;
	}
	public double getTotalotrosegresos() {
		return totalotrosegresos;
	}
	public void setTotalotrosegresos(double totalotrosegresos) {
		this.totalotrosegresos = totalotrosegresos;
	}
	public double getTotalventaspb() {
		return totalventaspb;
	}
	public void setTotalventaspb(double totalventaspb) {
		this.totalventaspb = totalventaspb;
	}
	public double getTcheque() {
		return tcheque;
	}
	public void setTcheque(double tcheque) {
		this.tcheque = tcheque;
	}
	public double getTdepbanco() {
		return tdepbanco;
	}
	public void setTdepbanco(double tdepbanco) {
		this.tdepbanco = tdepbanco;
	}
	public double getTefectivo() {
		return tefectivo;
	}
	public void setTefectivo(double tefectivo) {
		this.tefectivo = tefectivo;
	}
	public double getTtarcredito() {
		return ttarcredito;
	}
	public void setTtarcredito(double ttarcredito) {
		this.ttarcredito = ttarcredito;
	}
	public double getTtransbanco() {
		return ttransbanco;
	}
	public void setTtransbanco(double ttransbanco) {
		this.ttransbanco = ttransbanco;
	}
	public double getTotalDevco() {
		return totalDevco;
	}
	public void setTotalDevco(double totalDevco) {
		this.totalDevco = totalDevco;
	}
	public double getTotalDevcr() {
		return totalDevcr;
	}
	public void setTotalDevcr(double totalDevcr) {
		this.totalDevcr = totalDevcr;
	}
	public List getVrecodb() {
		return vrecodb;
	}
	public void setVrecodb(List vrecodb) {
		this.vrecodb = vrecodb;
	}
	public List getVrecotb() {
		return vrecotb;
	}
	public void setVrecotb(List vrecotb) {
		this.vrecotb = vrecotb;
	}
	public List getVrecotc() {
		return vrecotc;
	}
	public void setVrecotc(List vrecotc) {
		this.vrecotc = vrecotc;
	}
	public List getVrecrdb() {
		return vrecrdb;
	}
	public void setVrecrdb(List vrecrdb) {
		this.vrecrdb = vrecrdb;
	}
	public List getVrecrtb() {
		return vrecrtb;
	}
	public void setVrecrtb(List vrecrtb) {
		this.vrecrtb = vrecrtb;
	}
	public List getVrecrtc() {
		return vrecrtc;
	}
	public void setVrecrtc(List vrecrtc) {
		this.vrecrtc = vrecrtc;
	}
	public int getCaid() {
		return caid;
	}
	public void setCaid(int caid) {
		this.caid = caid;
	}
	public int getNoarqueo() {
		return noarqueo;
	}
	public void setNoarqueo(int noarqueo) {
		this.noarqueo = noarqueo;
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
	public List getRcheque() {
		return rcheque;
	}
	public void setRcheque(List rcheque) {
		this.rcheque = rcheque;
	}
	public List getRdepbanco() {
		return rdepbanco;
	}
	public void setRdepbanco(List rdepbanco) {
		this.rdepbanco = rdepbanco;
	}
	public List getRefectivo() {
		return refectivo;
	}
	public void setRefectivo(List refectivo) {
		this.refectivo = refectivo;
	}
	public List getRtcredito() {
		return rtcredito;
	}
	public void setRtcredito(List rtcredito) {
		this.rtcredito = rtcredito;
	}
	public List getRtransbanco() {
		return rtransbanco;
	}
	public void setRtransbanco(List rtransbanco) {
		this.rtransbanco = rtransbanco;
	}
	public List getVrcheque() {
		return vrcheque;
	}
	public void setVrcheque(List vrcheque) {
		this.vrcheque = vrcheque;
	}
	public List getVrdepbanco() {
		return vrdepbanco;
	}
	public void setVrdepbanco(List vrdepbanco) {
		this.vrdepbanco = vrdepbanco;
	}
	public List getVrefectivo() {
		return vrefectivo;
	}
	public void setVrefectivo(List vrefectivo) {
		this.vrefectivo = vrefectivo;
	}
	public List getVrtcredito() {
		return vrtcredito;
	}
	public void setVrtcredito(List vrtcredito) {
		this.vrtcredito = vrtcredito;
	}
	public List getVrtransbanco() {
		return vrtransbanco;
	}
	public void setVrtransbanco(List vrtransbanco) {
		this.vrtransbanco = vrtransbanco;
	}
	public List getTodasVentas() {
		return TodasVentas;
	}
	public void setTodasVentas(List todasVentas) {
		TodasVentas = todasVentas;
	}
	public List getEgrgrepagotramon() {
		return egrgrepagotramon;
	}
	public void setEgrgrepagotramon(List egrgrepagotramon) {
		this.egrgrepagotramon = egrgrepagotramon;
	}
	public List getIngrepagotramon() {
		return ingrepagotramon;
	}
	public void setIngrepagotramon(List ingrepagotramon) {
		this.ingrepagotramon = ingrepagotramon;
	}
	public String getMoneda() {
		return moneda;
	}
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}
	public double getTotalegrcpom() {
		return totalegrcpom;
	}
	public void setTotalegrcpom(double totalegrcpom) {
		this.totalegrcpom = totalegrcpom;
	}
	public double getTotalinrcpom() {
		return totalinrcpom;
	}
	public void setTotalinrcpom(double totalinrcpom) {
		this.totalinrcpom = totalinrcpom;
	}
	public List getReprdb() {
		return reprdb;
	}
	public void setReprdb(List reprdb) {
		this.reprdb = reprdb;
	}
	public List getReprtb() {
		return reprtb;
	}
	public void setReprtb(List reprtb) {
		this.reprtb = reprtb;
	}
	public List getReprtc() {
		return reprtc;
	}
	public void setReprtc(List reprtc) {
		this.reprtc = reprtc;
	}
	public double getTotalprdb() {
		return totalprdb;
	}
	public void setTotalprdb(double totalprdb) {
		this.totalprdb = totalprdb;
	}
	public double getTotalprimaspb() {
		return totalprimaspb;
	}
	public void setTotalprimaspb(double totalprimaspb) {
		this.totalprimaspb = totalprimaspb;
	}
	public double getTotalprtb() {
		return totalprtb;
	}
	public void setTotalprtb(double totalprtb) {
		this.totalprtb = totalprtb;
	}
	public double getTotalprtc() {
		return totalprtc;
	}
	public void setTotalprtc(double totalprtc) {
		this.totalprtc = totalprtc;
	}
	public List getVreprdb() {
		return vreprdb;
	}
	public void setVreprdb(List vreprdb) {
		this.vreprdb = vreprdb;
	}
	public List getVreprtb() {
		return vreprtb;
	}
	public void setVreprtb(List vreprtb) {
		this.vreprtb = vreprtb;
	}
	public List getVreprtc() {
		return vreprtc;
	}
	public void setVreprtc(List vreprtc) {
		this.vreprtc = vreprtc;
	}
	public List getRecibosPrimas() {
		return RecibosPrimas;
	}
	public void setRecibosPrimas(List recibosPrimas) {
		RecibosPrimas = recibosPrimas;
	}

	public double getTotalrepr() {
		return totalrepr;
	}
	public void setTotalrepr(double totalrepr) {
		this.totalrepr = totalrepr;
	}
	public Date getFechaarqueo() {
		return fechaarqueo;
	}
	public void setFechaarqueo(Date fechaarqueo) {
		this.fechaarqueo = fechaarqueo;
	}
	public double getDepositoFinal() {
		return depositoFinal;
	}
	public void setDepositoFinal(double depositoFinal) {
		this.depositoFinal = depositoFinal;
	}

	public List getReccambiomixto() {
		return reccambiomixto;
	}

	public void setReccambiomixto(List reccambiomixto) {
		this.reccambiomixto = reccambiomixto;
	}

	public double getTotalcambiomixto() {
		return totalcambiomixto;
	}

	public void setTotalcambiomixto(double totalcambiomixto) {
		this.totalcambiomixto = totalcambiomixto;
	}

	public double getTotalotrosingresos() {
		return totalotrosingresos;
	}

	public void setTotalotrosingresos(double totalotrosingresos) {
		this.totalotrosingresos = totalotrosingresos;
	}

	public List getLstotrosingresos() {
		return lstotrosingresos;
	}

	public void setLstotrosingresos(List lstotrosingresos) {
		this.lstotrosingresos = lstotrosingresos;
	}

	public Date getHoraarqueo() {
		return horaarqueo;
	}

	public void setHoraarqueo(Date horaarqueo) {
		this.horaarqueo = horaarqueo;
	}

	public String getDepositoRefer() {
		return depositoRefer;
	}

	public void setDepositoRefer(String depositoRefer) {
		this.depositoRefer = depositoRefer;
	}

	public double getTotalreex() {
		return totalreex;
	}

	public void setTotalreex(double totalreex) {
		this.totalreex = totalreex;
	}

	public List getRecibosIngEx() {
		return recibosIngEx;
	}

	public void setRecibosIngEx(List recibosIngEx) {
		this.recibosIngEx = recibosIngEx;
	}

	public double getTotalexdb() {
		return totalexdb;
	}

	public void setTotalexdb(double totalexdb) {
		this.totalexdb = totalexdb;
	}

	public double getTotalextb() {
		return totalextb;
	}

	public void setTotalextb(double totalextb) {
		this.totalextb = totalextb;
	}

	public double getTotalextc() {
		return totalextc;
	}

	public void setTotalextc(double totalextc) {
		this.totalextc = totalextc;
	}

	public List getReexdb() {
		return reexdb;
	}

	public void setReexdb(List reexdb) {
		this.reexdb = reexdb;
	}

	public List getReextb() {
		return reextb;
	}

	public void setReextb(List reextb) {
		this.reextb = reextb;
	}

	public List getReextc() {
		return reextc;
	}

	public void setReextc(List reextc) {
		this.reextc = reextc;
	}

	public double getTotalIngextpb() {
		return totalIngextpb;
	}

	public void setTotalIngextpb(double totalIngextpb) {
		this.totalIngextpb = totalIngextpb;
	}

	public List getVreiedb() {
		return vreiedb;
	}

	public void setVreiedb(List vreiedb) {
		this.vreiedb = vreiedb;
	}

	public List getVreietb() {
		return vreietb;
	}

	public void setVreietb(List vreietb) {
		this.vreietb = vreietb;
	}

	public List getVreietc() {
		return vreietc;
	}

	public void setVreietc(List vreietc) {
		this.vreietc = vreietc;
	}

	public double getDTotalSalida5() {
		return dTotalSalida5;
	}

	public void setDTotalSalida5(double totalSalida5) {
		dTotalSalida5 = totalSalida5;
	}

	public double getDTotalSalidaQ() {
		return dTotalSalidaQ;
	}

	public void setDTotalSalidaQ(double totalSalidaQ) {
		dTotalSalidaQ = totalSalidaQ;
	}

	public double getDTotalsalidas() {
		return dTotalsalidas;
	}

	public void setDTotalsalidas(double totalsalidas) {
		dTotalsalidas = totalsalidas;
	}

	public List getLstSalidas() {
		return lstSalidas;
	}

	public void setLstSalidas(List lstSalidas) {
		this.lstSalidas = lstSalidas;
	}

	public List getRecibosFinan() {
		return recibosFinan;
	}

	public void setRecibosFinan(List recibosFinan) {
		this.recibosFinan = recibosFinan;
	}

	public List getRefndb() {
		return refndb;
	}

	public void setRefndb(List refndb) {
		this.refndb = refndb;
	}

	public List getRefntb() {
		return refntb;
	}

	public void setRefntb(List refntb) {
		this.refntb = refntb;
	}

	public List getRefntc() {
		return refntc;
	}

	public void setRefntc(List refntc) {
		this.refntc = refntc;
	}

	public double getTotalfinanpb() {
		return totalfinanpb;
	}

	public void setTotalfinanpb(double totalfinanpb) {
		this.totalfinanpb = totalfinanpb;
	}

	public double getTotalfndb() {
		return totalfndb;
	}

	public void setTotalfndb(double totalfndb) {
		this.totalfndb = totalfndb;
	}

	public double getTotalfntb() {
		return totalfntb;
	}

	public void setTotalfntb(double totalfntb) {
		this.totalfntb = totalfntb;
	}

	public double getTotalfntc() {
		return totalfntc;
	}

	public void setTotalfntc(double totalfntc) {
		this.totalfntc = totalfntc;
	}

	public List getVrefndb() {
		return vrefndb;
	}

	public void setVrefndb(List vrefndb) {
		this.vrefndb = vrefndb;
	}

	public List getVrefntb() {
		return vrefntb;
	}

	public void setVrefntb(List vrefntb) {
		this.vrefntb = vrefntb;
	}

	public List getVrefntc() {
		return vrefntc;
	}

	public void setVrefntc(List vrefntc) {
		this.vrefntc = vrefntc;
	}

	public double getTotalrefn() {
		return totalrefn;
	}

	public void setTotalrefn(double totalrefn) {
		this.totalrefn = totalrefn;
	}

	public List getRtcreditom() {
		return rtcreditom;
	}

	public void setRtcreditom(List rtcreditom) {
		this.rtcreditom = rtcreditom;
	}

	public double getTtarcreditom() {
		return ttarcreditom;
	}

	public void setTtarcreditom(double ttarcreditom) {
		this.ttarcreditom = ttarcreditom;
	}

	public List getVrtcreditom() {
		return vrtcreditom;
	}

	public void setVrtcreditom(List vrtcreditom) {
		this.vrtcreditom = vrtcreditom;
	}

	public double getTtarcreditos() {
		return ttarcreditos;
	}

	public void setTtarcreditos(double ttarcreditos) {
		this.ttarcreditos = ttarcreditos;
	}

	public List getRtcreditos() {
		return rtcreditos;
	}

	public void setRtcreditos(List rtcreditos) {
		this.rtcreditos = rtcreditos;
	}

	public List getVrtcreditos() {
		return vrtcreditos;
	}

	public void setVrtcreditos(List vrtcreditos) {
		this.vrtcreditos = vrtcreditos;
	}
	
	public int getReferencenumber() {
		return referencenumber;
	}

	public void setReferencenumber(int referencenumber) {
		this.referencenumber = referencenumber;
	}
	
	//&& ============ adaptacion pmt
	public List getRecibosAnticiposPMT() {
		return recibosAnticiposPMT;
	}

	public void setRecibosAnticiposPMT(List recibosAnticiposPMT) {
		this.recibosAnticiposPMT = recibosAnticiposPMT;
	}

	public List getRepmtc() {
		return repmtc;
	}

	public void setRepmtc(List repmtc) {
		this.repmtc = repmtc;
	}

	public List getRepmdb() {
		return repmdb;
	}

	public void setRepmdb(List repmdb) {
		this.repmdb = repmdb;
	}

	public List getRepmtb() {
		return repmtb;
	}

	public void setRepmtb(List repmtb) {
		this.repmtb = repmtb;
	}

	public double getTotalrepm() {
		return totalrepm;
	}

	public void setTotalrepm(double totalrepm) {
		this.totalrepm = totalrepm;
	}

	public double getTotalpmtc() {
		return totalpmtc;
	}

	public void setTotalpmtc(double totalpmtc) {
		this.totalpmtc = totalpmtc;
	}

	public double getTotalpmdb() {
		return totalpmdb;
	}

	public void setTotalpmdb(double totalpmdb) {
		this.totalpmdb = totalpmdb;
	}

	public double getTotalpmtb() {
		return totalpmtb;
	}

	public void setTotalpmtb(double totalpmtb) {
		this.totalpmtb = totalpmtb;
	}
	public double getTotalpmpb() {
		return totalpmpb;
	}

	public void setTotalpmpb(double totalpmpb) {
		this.totalpmpb = totalpmpb;
	}
}

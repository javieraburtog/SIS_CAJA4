package com.casapellas.entidades;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.casapellas.donacion.entidades.DncIngresoDonacion;
import com.casapellas.socketpos.PosCtrl;



public class MetodosPago implements java.io.Serializable{

	private static final long serialVersionUID = -4716626063569323402L;
	private String metododescrip;
	private String metodo;
	private String moneda;
	private double monto;
	private BigDecimal tasa;
	private double equivalente;
	private String referencia;
	private String referencia2;
	private String referencia3;
	private String referencia4;
	private String referencia5;
	private String referencia6;
	private String referencia7;
	private String track;
	private String vmanual;
	private int iCaidpos;
	private String terminal;
	private double montopos;
	private String nombre;
	private String responsecode = new String("");
	private String fechavencetc = new String("");
	private String statuspago   = new String("");
	private BigDecimal excedente;
	private BigDecimal montoendonacion;
	private BigDecimal montorecibido;
	private boolean incluyedonacion;
	
	private BigDecimal diferenciaCor = BigDecimal.ZERO;
	
	public BigDecimal getDiferenciaCor() {
		return diferenciaCor;
	}
	public void setDiferenciaCor(BigDecimal diferenciaCor) {
		this.diferenciaCor=BigDecimal.ZERO;
		this.diferenciaCor = diferenciaCor;
	}
	private int referencenumber;
	private int depctatran;
	
	private List<DncIngresoDonacion>donaciones;
	
	private String marcatarjeta;
	private String codigomarcatarjeta;
	
	private long identificador;
	private String codigobanco ;
	
	private String montoEquivalenteEntero;
	
	public MetodosPago(String metododescrip, String metodo, String moneda,
			double monto, BigDecimal tasa, double equivalente,
			String referencia, String referencia2, String referencia3,
			String referencia4, String referencia5, String referencia6,
			String referencia7, String track, String vmanual, int iCaidpos,
			String terminal, double montopos, String nombre,
			String responsecode, String fechavencetc, String statuspago,
			BigDecimal montoendonacion, BigDecimal montorecibido,
			boolean incluyedonacion,  List<DncIngresoDonacion>donaciones,
			String marcatarjeta, String codigomarcatarjeta,
			int referencenumber, int depctatran ) {
		
		super();
		this.metododescrip = metododescrip;
		this.metodo = metodo;
		this.moneda = moneda;
		this.monto = monto;
		this.tasa = tasa;
		this.equivalente = equivalente;
		this.referencia = referencia;
		this.referencia2 = referencia2;
		this.referencia3 = referencia3;
		this.referencia4 = referencia4;
		this.referencia5 = referencia5;
		this.referencia6 = referencia6;
		this.referencia7 = referencia7;
		this.track = track;
		this.vmanual = vmanual;
		this.iCaidpos = iCaidpos;
		this.terminal = terminal;
		this.montopos = montopos;
		this.nombre = nombre;
		this.responsecode = responsecode;
		this.fechavencetc = fechavencetc;
		this.statuspago = statuspago;
		this.montoendonacion = montoendonacion;
		this.montorecibido = montorecibido;
		this.incluyedonacion = incluyedonacion;
		this.donaciones = donaciones;
		this.marcatarjeta = marcatarjeta;
		this.codigomarcatarjeta = codigomarcatarjeta;
		this.referencenumber = referencenumber;
		this.depctatran = depctatran;

	}
	public MetodosPago(String metododescrip, String metodo, String moneda, double monto, BigDecimal tasa, double equivalente, String referencia, String referencia2, String referencia3, String referencia4, String referencia5, String referencia6, String referencia7, String track, String vmanual) {
		super();
		this.metododescrip = metododescrip;
		this.metodo = metodo;
		this.moneda = moneda;
		this.monto = monto;
		this.tasa = tasa;
		this.equivalente = equivalente;
		this.referencia = referencia;
		this.referencia2 = referencia2;
		this.referencia3 = referencia3;
		this.referencia4 = referencia4;
		this.referencia5 = referencia5;
		this.referencia6 = referencia6;
		this.referencia7 = referencia7;
		this.track = track;
		this.vmanual = vmanual;
	}
	public MetodosPago(String metododescrip,String metodo, String moneda, double monto, BigDecimal tasa, double equivalente, String referencia, String referencia2, String referencia3, String referencia4,String vmanual,int iCaidpos) {
		this.metododescrip = metododescrip;
		this.metodo = metodo;
		this.moneda = moneda;
		this.monto = monto;
		this.tasa = tasa;
		this.equivalente = equivalente;		
		this.referencia = referencia;
		this.referencia2 = referencia2;
		this.referencia3 = referencia3;
		this.referencia4 = referencia4;
		this.vmanual = vmanual;
		this.iCaidpos = iCaidpos;
	}
	
	public MetodosPago(String metododescrip,String metodo, String moneda, 
					double monto, BigDecimal tasa, double equivalente, 
					String referencia, String referencia2, String referencia3,
					String referencia4,String vmanual,int iCaidpos,
				    String referencia5, String referencia6, String referencia7,
				    double montopos ) {
		this.metododescrip = metododescrip;
		this.metodo = metodo;
		this.moneda = moneda;
		this.monto = monto;
		this.tasa = tasa;
		this.equivalente = equivalente;		
		this.referencia = referencia;
		this.referencia2 = referencia2;
		this.referencia3 = referencia3;
		this.referencia4 = referencia4 == null? new String(""): referencia4;
		this.referencia5 = referencia5 == null? new String(""): referencia5;
		this.referencia6 = referencia6 == null? new String(""): referencia6;
		this.referencia7 = referencia7 == null? new String(""): referencia7;
		this.vmanual = vmanual;
		this.iCaidpos = iCaidpos;
		this.montopos = montopos;
	}
	
	
	
	public MetodosPago(String metodo, String moneda, double monto, BigDecimal tasa, double equivalente, String referencia, String referencia2, String referencia3, String referencia4,String vmanual,int iCaidpos) {
		this.metodo = metodo;
		this.moneda = moneda;
		this.monto = monto;
		this.tasa = tasa;
		this.equivalente = equivalente;		
		this.referencia = referencia;
		this.referencia2 = referencia2;
		this.referencia3 = referencia3;
		this.referencia4 = referencia4;
		this.vmanual = vmanual;
		this.iCaidpos = iCaidpos;
	}
	public MetodosPago(String metodo, String moneda, double monto, BigDecimal tasa, double equivalente, String referencia, String referencia2, String referencia3, String referencia4){
		this.metodo = metodo;
		this.moneda = moneda;
		this.monto = monto;
		this.tasa = tasa;
		this.equivalente = equivalente;		
		this.referencia = referencia;
		this.referencia2 = referencia2;
		this.referencia3 = referencia3;
		this.referencia4 = referencia4;
	}
	public MetodosPago(String metododescrip,String metodo, String moneda, double monto, BigDecimal tasa, double equivalente, String referencia, String referencia2, String referencia3, String referencia4){
		this.metododescrip = metododescrip;
		this.metodo = metodo;
		this.moneda = moneda;
		this.monto = monto;
		this.tasa = tasa;
		this.equivalente = equivalente;		
		this.referencia = referencia;
		this.referencia2 = referencia2;
		this.referencia3 = referencia3;
		this.referencia4 = referencia4;
	}
	public MetodosPago() {
		// TODO Auto-generated constructor stub
	}

	public double getEquivalente() {
		return equivalente;
	}
	public void setEquivalente(double equivalente) {
		this.equivalente = equivalente;
	}
	public String getMetodo() {
		return metodo;
	}
	public void setMetodo(String metodo) {
		this.metodo = metodo;
	}
	public String getMoneda() {
		return moneda;
	}
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}
	public double getMonto() {
		return monto;
	}
	public void setMonto(double monto) {
		this.monto = monto;
	}
	public String getReferencia() {
		return referencia;
	}
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	public String getReferencia2() {
		return (referencia2 == null)? "": referencia2;
	}
	public void setReferencia2(String referencia2) {
		this.referencia2 = referencia2;
	}
	public String getReferencia3() {
		return (referencia3 == null)? "": referencia3;
	}
	public void setReferencia3(String referencia3) {
		this.referencia3 = referencia3;
	}
	public BigDecimal getTasa() {
		return tasa;
	}
	public void setTasa(BigDecimal tasa) {
		this.tasa = tasa;
	}
	public String getReferencia4() {
		return referencia4;
	}
	public void setReferencia4(String referencia4) {
		this.referencia4 = referencia4;
	}
	public String getMetododescrip() {
		return metododescrip;
	}
	public void setMetododescrip(String metododescrip) {
		this.metododescrip = metododescrip;
	}
	public String getReferencia5() {
		return referencia5;
	}
	public void setReferencia5(String referencia5) {
		this.referencia5 = referencia5;
	}
	public String getReferencia6() {
		return referencia6;
	}
	public void setReferencia6(String referencia6) {
		this.referencia6 = referencia6;
	}
	public String getReferencia7() {
		return referencia7;
	}
	public void setReferencia7(String referencia7) {
		this.referencia7 = referencia7;
	}
	public String getTrack() {
		return track;
	}
	public void setTrack(String track) {
		this.track = track;
	}
	public String getVmanual() {
		return vmanual;
	}
	public void setVmanual(String vmanual) {
		this.vmanual = vmanual;
	}
	public int getICaidpos() {
		return iCaidpos;
	}
	public void setICaidpos(int caidpos) {
		iCaidpos = caidpos;
	}
	public String getTerminal() {
		return terminal;
	}
	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}
	public double getMontopos() {
		return montopos;
	}
	public void setMontopos(double montopos) {
		this.montopos = montopos;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getResponsecode() {
		return responsecode;
	}
	public void setResponsecode(String responsecode) {
		this.responsecode = responsecode;
	}
	public String getFechavencetc() {
		return fechavencetc;
	}
	public void setFechavencetc(String fechavencetc) {
		this.fechavencetc = fechavencetc;
	}
	public String getStatuspago() {
		return statuspago;
	}
	public void setStatuspago(String statuspago) {
		this.statuspago = statuspago;
	}
	public MetodosPago copyFromReciboDet(Recibodet rd){
		MetodosPago pago = new MetodosPago();
	
		try{
			
			pago.setMetododescrip(rd.getId().getMpago());
			pago.setMetodo(rd.getId().getMpago());
			pago.setMoneda(rd.getId().getMoneda());
			pago.setMonto(rd.getMonto().doubleValue());
			pago.setTasa(rd.getTasa());
			pago.setEquivalente(rd.getEquiv().doubleValue());
			pago.setReferencia(rd.getId().getRefer1());
			pago.setReferencia2(rd.getId().getRefer2());
			pago.setReferencia3(rd.getId().getRefer3());
			pago.setReferencia4(rd.getId().getRefer4());
			pago.setReferencia5(rd.getRefer5());
			pago.setReferencia6(rd.getRefer6());
			pago.setReferencia7(rd.getRefer7());
			pago.setTrack(new String(""));
			pago.setVmanual(rd.getVmanual());
			pago.setICaidpos(rd.getCaidpos());
			pago.setTerminal(rd.getTerminal());
			pago.setMontopos(rd.getMonto().doubleValue());
			pago.setNombre(rd.getNombre());
			pago.setResponsecode( new String("") );
			pago.setFechavencetc( new String("") ) ;
			pago.setStatuspago( new String("") ) ;
			
		}catch(Exception e){
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
		}
		return pago;
	}
	public static MetodosPago copyFromTransactSp(Transactsp tsp){
		MetodosPago pago = new MetodosPago();
		
		try{
			
			String refer6 = PosCtrl.formatFechaSocket(new SimpleDateFormat
					("MMddyyyy") .format(tsp.getTransdate()), 
					new SimpleDateFormat("hhmmss").format(tsp.getTranstime()));
			
			pago.setMetododescrip("H");
			pago.setMetodo("H");
			pago.setMoneda(tsp.getCurrency());
			pago.setMonto(tsp.getAmount().doubleValue());
			pago.setTasa(BigDecimal.ONE);
			pago.setEquivalente(tsp.getAmount().doubleValue());
			pago.setReferencia( tsp.getAcqnumber() );
			pago.setReferencia2("");
			pago.setReferencia3(tsp.getCardnumber());
			pago.setReferencia4(tsp.getReferencenumber());
			pago.setReferencia5(tsp.getAuthorizationid());
			pago.setReferencia6(refer6);
			pago.setReferencia7( tsp.getSystraceno() );
			pago.setTrack(new String(""));
			pago.setVmanual("2");
			pago.setICaidpos(tsp.getCaid());
			pago.setTerminal(tsp.getTermid());
			pago.setMontopos(tsp.getAmount().doubleValue());
			pago.setNombre(tsp.getClientname());
			pago.setResponsecode( new String("") );
			pago.setFechavencetc( new String("") ) ;
			pago.setStatuspago( tsp.getStatus() ) ;
			
		}catch(Exception e){
//			LogCrtl.imprimirError(e);
			pago = null;
		}
		return pago;
	}
	
	@Override
	public MetodosPago clone() {
		return new MetodosPago(metododescrip, metodo, moneda, monto, tasa,
				equivalente, referencia, referencia2, referencia3, referencia4,
				referencia5, referencia6, referencia7, track, vmanual,
				iCaidpos, terminal, montopos, nombre, responsecode,
				fechavencetc, statuspago, montoendonacion, montorecibido,
				incluyedonacion,donaciones, marcatarjeta, codigomarcatarjeta,  
				referencenumber, depctatran);
	}
	@Override
	public boolean equals(Object o) {
		boolean equals = true;
		try{
			MetodosPago m = (MetodosPago)o;
			equals = equals && this.monto == m.getMonto();
			equals = equals && this.equivalente == m.getEquivalente();
			equals = equals && this.tasa.compareTo(m.getTasa()) == 0 ;
			equals = equals && this.metodo.compareTo(m.getMetodo()) == 0;
			equals = equals && this.referencia.compareTo(m.getReferencia()) == 0 ;
			equals = equals && this.referencia2.compareTo(m.getReferencia2()) == 0 ;
			equals = equals && this.referencia3.compareTo(m.getReferencia3()) == 0 ;
			equals = equals && this.referencia4.compareTo(m.getReferencia4()) == 0 ;
			equals = equals && this.referencia5.compareTo(m.getReferencia5()) == 0 ;
			equals = equals && this.vmanual.compareTo(m.getVmanual()) == 0 ;

		}catch(Exception e){
//			LogCrtl.imprimirError(e);
		}
		return equals;
	}
	public int getiCaidpos() {
		return iCaidpos;
	}
	public void setiCaidpos(int iCaidpos) {
		this.iCaidpos = iCaidpos;
	}
	public BigDecimal getExcedente() {
		return excedente;
	}
	public void setExcedente(BigDecimal excedente) {
		this.excedente = excedente;
	}
	public BigDecimal getMontoendonacion() {
		return montoendonacion;
	}
	public void setMontoendonacion(BigDecimal montoendonacion) {
		this.montoendonacion = montoendonacion;
	}
	public BigDecimal getMontorecibido() {
		return montorecibido;
	}
	public void setMontorecibido(BigDecimal montorecibido) {
		this.montorecibido = montorecibido;
	}
	public boolean isIncluyedonacion() {
		return incluyedonacion;
	}
	public void setIncluyedonacion(boolean incluyedonacion) {
		this.incluyedonacion = incluyedonacion;
	}
	public List<DncIngresoDonacion> getDonaciones() {
		if(donaciones == null )
			donaciones = new ArrayList<DncIngresoDonacion>();
		return donaciones;
	}
	public void setDonaciones(List<DncIngresoDonacion> donaciones) {
		this.donaciones = donaciones;
	}
	public String getMarcatarjeta() {
		
		if(marcatarjeta == null )
			marcatarjeta = ""; 
		
		return marcatarjeta;
	}
	public void setMarcatarjeta(String marcatarjeta) {
		this.marcatarjeta = marcatarjeta;
	}
	public String getCodigomarcatarjeta() {
		if(codigomarcatarjeta == null )
			codigomarcatarjeta = ""; 
		return codigomarcatarjeta;
	}
	public void setCodigomarcatarjeta(String codigomarcatarjeta) {
		this.codigomarcatarjeta = codigomarcatarjeta;
	}
	public int getReferencenumber() {
		return referencenumber;
	}
	public void setReferencenumber(int referencenumber) {
		this.referencenumber = referencenumber;
	}
	public int getDepctatran() {
		return depctatran;
	}
	public void setDepctatran(int depctatran) {
		this.depctatran = depctatran;
	}
	public long getIdentificador() {
		return identificador;
	}
	public void setIdentificador(long identificador) {
		this.identificador = identificador;
	}
	public String getCodigobanco() {
		return codigobanco;
	}
	public void setCodigobanco(String codigobanco) {
		this.codigobanco = codigobanco;
	}
	public String getMontoEquivalenteEntero() {
		return montoEquivalenteEntero;
	}
	public void setMontoEquivalenteEntero(String montoEquivalenteEntero) {
		this.montoEquivalenteEntero = montoEquivalenteEntero;
	}
	
}

package com.casapellas.conciliacion.entidades;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.casapellas.entidades.Deposito;
import com.casapellas.entidades.Deposito_Report;

public class ConsolidadoCoincidente implements java.io.Serializable {

	private static final long serialVersionUID = 915552341370131658L;
	private int idresumenbanco;
	private long numerocuenta;
	private String moneda;
	private long codigobanco;
	private Date fechabanco;
	private long referenciabanco;
	private BigDecimal montoBanco;
	private BigDecimal montoAjustado;
	private int iddepbcodet;
	private String codigotransaccionbco;
	private int iddepositocaja;
	private Date fechacaja;
	private long referenciacaja;
	private BigDecimal montoCaja;
	private String tipotransaccionjde;
	private int codigocajero;
	private int codigocontador;
	private String usuariocajero;
	private String usuariocontador;
	private String nombrebanco;
	private int nivelcomparacion;
	private List<Deposito_Report>depositoscaja;
	private List<Deposito>depositoscajaR;
	private String descriptransactbanco;
	private String descriptransjde;
	private String conceptodepbco;
	private BigDecimal montorporajuste;
	private int cantdepositoscaja;
	private int excluircoincidencia;
	private String motivoexclusion;
	private boolean enconflicto;
	private String dtaConsolidadoConflicto;
	private String dtaDepositoCajaConflicto;
	private String dtaIdsDepsBcoCnfl;
	private String dtaIdDepsBancoConflicto;
	private int statusValidaConflicto;
	
	private boolean comprobanteaplicado;
	private String observaciones;
	int nobatch;
	private String tipodocumentojde;
	private int referenciacomprobante;
	private boolean permiteConflictos;
	
	private String idsdepscaja;
	private String referencesdepscaja;
	
	private boolean procesarAjustePorExcepcion ;
	private String cuentaAjusteExcepcionId;
	
	private boolean multipleBancoPorUnoCaja;
	private List<PcdConsolidadoDepositosBanco> depositosbanco;
	
	private String estadobatchdescrip ;
	
	public ConsolidadoCoincidente(int idresumenbanco, long numerocuenta,
			String moneda, long codigobanco, Date fechabanco,
			long referenciabanco, BigDecimal montoBanco,
			BigDecimal montoAjustado, int iddepbcodet,
			String codigotransaccionbco, int iddepositocaja, Date fechacaja,
			long referenciacaja, BigDecimal montoCaja,
			String tipotransaccionjde, int codigocajero, int codigocontador,
			String usuariocajero, String usuariocontador, String nombrebanco, 
			int nivelcomparacion, List<Deposito_Report>depositoscaja, 
			String descriptransactbanco, String descriptransjde, 
			String conceptodepbco, BigDecimal montorporajuste,
			int cantdepositoscaja, int excluircoincidencia, String motivoexclusion,
			boolean enconflicto, String dtaConsolidadoConflicto,  String dtaDepositoCajaConflicto,
			String dtaIdsDepsBcoCnfl,String dtaIdDepsBancoConflicto, 
			int statusValidaConflicto, boolean comprobanteaplicado, 
			String observaciones, int nobatch, String tipodocumentojde, 
			int referenciacomprobante,  boolean permiteConflictos, String idsdepscaja,
			String referencesdepscaja, String estadobatchdescrip  ) {
		super();
		this.idresumenbanco = idresumenbanco;
		this.numerocuenta = numerocuenta;
		this.moneda = moneda;
		this.codigobanco = codigobanco;
		this.fechabanco = fechabanco;
		this.referenciabanco = referenciabanco;
		this.montoBanco = montoBanco;
		this.montoAjustado = montoAjustado;
		this.iddepbcodet = iddepbcodet;
		this.codigotransaccionbco = codigotransaccionbco;
		this.iddepositocaja = iddepositocaja;
		this.fechacaja = fechacaja;
		this.referenciacaja = referenciacaja;
		this.montoCaja = montoCaja;
		this.tipotransaccionjde = tipotransaccionjde;
		this.codigocajero = codigocajero;
		this.codigocontador = codigocontador;
		this.usuariocajero = usuariocajero;
		this.usuariocontador = usuariocontador;
		this.nombrebanco = nombrebanco;
		this.nivelcomparacion = nivelcomparacion;
		this.depositoscaja = depositoscaja;
		this.descriptransactbanco = descriptransactbanco ;
		this.descriptransjde = descriptransjde; 
		this.conceptodepbco = conceptodepbco; 
		this.montorporajuste = montorporajuste;
		this.cantdepositoscaja = cantdepositoscaja; 
		this.excluircoincidencia = excluircoincidencia;
		this.motivoexclusion =  motivoexclusion;
		this.enconflicto =enconflicto ;
		this.dtaConsolidadoConflicto = dtaConsolidadoConflicto;
		this.dtaDepositoCajaConflicto = dtaDepositoCajaConflicto;
		this.dtaIdsDepsBcoCnfl = dtaIdsDepsBcoCnfl;
		this.dtaIdDepsBancoConflicto = dtaIdDepsBancoConflicto;
		this.statusValidaConflicto = statusValidaConflicto;
		this.comprobanteaplicado = comprobanteaplicado ;
		this.observaciones = observaciones;
		this.nobatch = nobatch;
		this.tipodocumentojde = tipodocumentojde ;
		this.referenciacomprobante = referenciacomprobante;
		this.permiteConflictos = permiteConflictos ;
		this.idsdepscaja = idsdepscaja;
		this.referencesdepscaja = referencesdepscaja;
		this.estadobatchdescrip = estadobatchdescrip;
	}

	public ConsolidadoCoincidente() {
		super();
	}

	public int getIdresumenbanco() {
		return idresumenbanco;
	}

	public void setIdresumenbanco(int idresumenbanco) {
		this.idresumenbanco = idresumenbanco;
	}

	public long getNumerocuenta() {
		return numerocuenta;
	}

	public void setNumerocuenta(long numerocuenta) {
		this.numerocuenta = numerocuenta;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public long getCodigobanco() {
		return codigobanco;
	}

	public void setCodigobanco(long codigobanco) {
		this.codigobanco = codigobanco;
	}

	public Date getFechabanco() {
		return fechabanco;
	}

	public void setFechabanco(Date fechabanco) {
		this.fechabanco = fechabanco;
	}

	public long getReferenciabanco() {
		return referenciabanco;
	}

	public void setReferenciabanco(long referenciabanco) {
		this.referenciabanco = referenciabanco;
	}

	public BigDecimal getMontoBanco() {
		return montoBanco;
	}

	public void setMontoBanco(BigDecimal montoBanco) {
		this.montoBanco = montoBanco;
	}

	public BigDecimal getMontoAjustado() {
		return montoAjustado;
	}

	public void setMontoAjustado(BigDecimal montoAjustado) {
		this.montoAjustado = montoAjustado;
	}

	public int getIddepbcodet() {
		return iddepbcodet;
	}

	public void setIddepbcodet(int iddepbcodet) {
		this.iddepbcodet = iddepbcodet;
	}

	public String getCodigotransaccionbco() {
		return codigotransaccionbco;
	}

	public void setCodigotransaccionbco(String codigotransaccionbco) {
		this.codigotransaccionbco = codigotransaccionbco;
	}

	public int getIddepositocaja() {
		return iddepositocaja;
	}

	public void setIddepositocaja(int iddepositocaja) {
		this.iddepositocaja = iddepositocaja;
	}

	public Date getFechacaja() {
		return fechacaja;
	}

	public void setFechacaja(Date fechacaja) {
		this.fechacaja = fechacaja;
	}

	public long getReferenciacaja() {
		return referenciacaja;
	}

	public void setReferenciacaja(long referenciacaja) {
		this.referenciacaja = referenciacaja;
	}

	public BigDecimal getMontoCaja() {
		return montoCaja;
	}

	public void setMontoCaja(BigDecimal montoCaja) {
		this.montoCaja = montoCaja;
	}

	public String getTipotransaccionjde() {
		return tipotransaccionjde;
	}

	public void setTipotransaccionjde(String tipotransaccionjde) {
		this.tipotransaccionjde = tipotransaccionjde;
	}

	public int getCodigocajero() {
		return codigocajero;
	}

	public void setCodigocajero(int codigocajero) {
		this.codigocajero = codigocajero;
	}

	public int getCodigocontador() {
		return codigocontador;
	}

	public void setCodigocontador(int codigocontador) {
		this.codigocontador = codigocontador;
	}

	public String getUsuariocajero() {
		return usuariocajero;
	}

	public void setUsuariocajero(String usuariocajero) {
		this.usuariocajero = usuariocajero;
	}

	public String getUsuariocontador() {
		return usuariocontador;
	}

	public void setUsuariocontador(String usuariocontador) {
		this.usuariocontador = usuariocontador;
	}

	public String getNombrebanco() {
		return nombrebanco;
	}
	public void setNombrebanco(String nombrebanco) {
		this.nombrebanco = nombrebanco;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public int getNivelcomparacion() {
		return nivelcomparacion;
	}
	public void setNivelcomparacion(int nivelcomparacion) {
		this.nivelcomparacion = nivelcomparacion;
	}
	public List<Deposito_Report> getDepositoscaja() {
		return depositoscaja;
	}
	public List<Deposito> getDepositoscajaR() {
		return depositoscajaR;
	}
	public void setDepositoscaja(List<Deposito_Report> depositoscaja) {
		this.depositoscaja = depositoscaja;
	}

	public String getDescriptransactbanco() {
		return descriptransactbanco;
	}

	public void setDescriptransactbanco(String descriptransactbanco) {
		this.descriptransactbanco = descriptransactbanco;
	}

	public String getDescriptransjde() {
		return descriptransjde;
	}

	public void setDescriptransjde(String descriptransjde) {
		this.descriptransjde = descriptransjde;
	}

	public String getConceptodepbco() {
		return conceptodepbco;
	}

	public void setConceptodepbco(String conceptodepbco) {
		this.conceptodepbco = conceptodepbco;
	}

	public BigDecimal getMontorporajuste() {
		return montorporajuste;
	}

	public void setMontorporajuste(BigDecimal montorporajuste) {
		this.montorporajuste = montorporajuste;
	}

	public int getCantdepositoscaja() {
		return cantdepositoscaja;
	}

	public void setCantdepositoscaja(int cantdepositoscaja) {
		this.cantdepositoscaja = cantdepositoscaja;
	}

	public int getExcluircoincidencia() {
		return excluircoincidencia;
	}

	public void setExcluircoincidencia(int excluircoincidencia) {
		this.excluircoincidencia = excluircoincidencia;
	}

	public String getMotivoexclusion() {
		return motivoexclusion;
	}

	public void setMotivoexclusion(String motivoexclusion) {
		this.motivoexclusion = motivoexclusion;
	}

	public boolean isEnconflicto() {
		return enconflicto;
	}

	public void setEnconflicto(boolean enconflicto) {
		this.enconflicto = enconflicto;
	}

	public String getDtaConsolidadoConflicto() {
		if(dtaConsolidadoConflicto == null)
			dtaConsolidadoConflicto = "";
		return dtaConsolidadoConflicto;
	}

	public void setDtaConsolidadoConflicto(String dtaConsolidadoConflicto) {
		this.dtaConsolidadoConflicto = dtaConsolidadoConflicto;
	}

	public String getDtaDepositoCajaConflicto() {
		if(dtaDepositoCajaConflicto == null)
			dtaDepositoCajaConflicto = "";
		return dtaDepositoCajaConflicto;
	}

	public void setDtaDepositoCajaConflicto(String dtaDepositoCajaConflicto) {
		this.dtaDepositoCajaConflicto = dtaDepositoCajaConflicto;
	}

	public String getDtaIdsDepsBcoCnfl() {
		
		if(dtaIdsDepsBcoCnfl == null )
			dtaIdsDepsBcoCnfl = "" ;
		return dtaIdsDepsBcoCnfl;
	}

	public void setDtaIdsDepsBcoCnfl(String dtaIdsDepsBcoCnfl) {
		this.dtaIdsDepsBcoCnfl = dtaIdsDepsBcoCnfl;
	}
	public String getDtaIdDepsBancoConflicto() {
		if(dtaIdDepsBancoConflicto == null )
			dtaIdDepsBancoConflicto = "";
		return dtaIdDepsBancoConflicto;
	}

	public void setDtaIdDepsBancoConflicto(String dtaIdDepsBancoConflicto) {
		this.dtaIdDepsBancoConflicto = dtaIdDepsBancoConflicto;
	}
	public int getStatusValidaConflicto() {
		return statusValidaConflicto;
	}
	public void setStatusValidaConflicto(int statusValidaConflicto) {
		this.statusValidaConflicto = statusValidaConflicto;
	}
	public boolean isComprobanteaplicado() {
		return comprobanteaplicado;
	}
	public void setComprobanteaplicado(boolean comprobanteaplicado) {
		this.comprobanteaplicado = comprobanteaplicado;
	}
	public String getObservaciones() {
		if(observaciones == null)
			observaciones = "Sin Procesar";
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public int getNobatch() {
		return nobatch;
	}
	public void setNobatch(int nobatch) {
		this.nobatch = nobatch;
	}
	public String getTipodocumentojde() {
		return tipodocumentojde;
	}
	public void setTipodocumentojde(String tipodocumentojde) {
		if(tipodocumentojde == null)
			tipodocumentojde = "." ;
		this.tipodocumentojde = tipodocumentojde;
	}
	
	public int getReferenciacomprobante() {
		return referenciacomprobante;
	}

	public void setReferenciacomprobante(int referenciacomprobante) {
		this.referenciacomprobante = referenciacomprobante;
	}

	public boolean isPermiteConflictos() {
		return permiteConflictos;
	}

	public void setPermiteConflictos(boolean permiteConflictos) {
		this.permiteConflictos = permiteConflictos;
	}
	
	public String getIdsdepscaja() {
		if(idsdepscaja == null)
			idsdepscaja =  "" ;
		return idsdepscaja;
	}

	public void setIdsdepscaja(String idsdepscaja) {
		this.idsdepscaja = idsdepscaja;
	}

	public String getReferencesdepscaja() {
		if(referencesdepscaja == null)
			referencesdepscaja =  "" ;
		return referencesdepscaja;
	}

	public void setReferencesdepscaja(String referencesdepscaja) {
		this.referencesdepscaja = referencesdepscaja;
	}


	@Override
	public ConsolidadoCoincidente clone()  {
		 
		return new ConsolidadoCoincidente(idresumenbanco, numerocuenta, moneda,
				codigobanco, fechabanco, referenciabanco, montoBanco,
				montoAjustado, iddepbcodet, codigotransaccionbco,
				iddepositocaja, fechacaja, referenciacaja, montoCaja,
				tipotransaccionjde, codigocajero, codigocontador,
				usuariocajero, usuariocontador, nombrebanco, nivelcomparacion,
				depositoscaja, descriptransactbanco, descriptransjde,
				conceptodepbco, montorporajuste, cantdepositoscaja,
				excluircoincidencia, motivoexclusion, enconflicto,
				dtaConsolidadoConflicto,dtaDepositoCajaConflicto, dtaIdsDepsBcoCnfl, 
				dtaIdDepsBancoConflicto, statusValidaConflicto, 
				comprobanteaplicado, observaciones, nobatch, tipodocumentojde, 
				referenciacomprobante, permiteConflictos, idsdepscaja, referencesdepscaja, estadobatchdescrip);
	}
	
	public String dataToExcel(String concat){
		return 
		nivelcomparacion +concat+ 
		cantdepositoscaja +concat+ 
		nombrebanco +concat+ 
		numerocuenta +concat+ 
		moneda +concat+ 
		new SimpleDateFormat("dd/MM/yyyy").format(fechabanco)  + concat + 
		new SimpleDateFormat("dd/MM/yyyy").format(fechacaja)  + concat + 
		referenciabanco +concat+ 
		referenciacaja +concat+ 
		montoBanco +concat+ 
		montoCaja +concat+ 
		montorporajuste +concat+ 
		usuariocontador +concat+ 
		conceptodepbco +concat+ 
		descriptransjde;
	}
	
	public String dataToExcelProcessed(String concat){
		return
			cantdepositoscaja +concat+ 
			nombrebanco +concat+ 
			numerocuenta +concat+ 
			moneda +concat+ 
			new SimpleDateFormat("dd/MM/yyyy").format(fechabanco)  + concat + 
			new SimpleDateFormat("dd/MM/yyyy").format(fechacaja)  + concat + 
			referenciabanco +concat+
			referenciacomprobante  +concat+
			nobatch +concat+
			montoBanco +concat+ 
			montoCaja +concat+ 
			montorporajuste +concat+ 
			( procesarAjustePorExcepcion? "SI":"NO" ) +concat+ 
			tipodocumentojde +concat+ 
			conceptodepbco.trim()  +concat+ 
			( comprobanteaplicado? "SI":"NO" ) +concat+ 
			observaciones +concat+ 
			estadobatchdescrip
			;
	}

	public boolean isProcesarAjustePorExcepcion() {
		return procesarAjustePorExcepcion;
	}

	public void setProcesarAjustePorExcepcion(boolean procesarAjustePorExcepcion) {
		this.procesarAjustePorExcepcion = procesarAjustePorExcepcion;
	}

	public String getCuentaAjusteExcepcionId() {
		return cuentaAjusteExcepcionId;
	}

	public void setCuentaAjusteExcepcionId(String cuentaAjusteExcepcionId) {
		this.cuentaAjusteExcepcionId = cuentaAjusteExcepcionId;
	}

	public boolean isMultipleBancoPorUnoCaja() {
		return multipleBancoPorUnoCaja;
	}

	public void setMultipleBancoPorUnoCaja(boolean multipleBancoPorUnoCaja) {
		this.multipleBancoPorUnoCaja = multipleBancoPorUnoCaja;
	}

	public List<PcdConsolidadoDepositosBanco> getDepositosbanco() {
		return depositosbanco;
	}

	public void setDepositosbanco(List<PcdConsolidadoDepositosBanco> depositosbanco) {
		this.depositosbanco = depositosbanco;
	}

	public String getEstadobatchdescrip() {
		if(estadobatchdescrip == null)
			estadobatchdescrip = "" ;
		return estadobatchdescrip;
	}

	public void setEstadobatchdescrip(String estadobatchdescrip) {
		this.estadobatchdescrip = estadobatchdescrip;
	}

}

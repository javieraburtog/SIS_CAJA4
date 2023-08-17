package com.casapellas.jde.creditos;

import java.math.BigDecimal;

import com.casapellas.util.PropertiesSystem;

public class FacturaF03B11 {

	private String rppst   ;
	private String rpaap   ;
	private String rpfap   ;
	private String rptorg  ;
	private String rpuser  ;
	private String rpjcl   ;
	private String rppid   ;
	private String rpupmj  ;
	private String rpupmt  ;
	private String rppo    ;
	private String rpdcto  ;
	private String rpdoc   ;
	private String rpdct   ;
	private String rpkco   ;
	private String rpsfx   ;
	private String rpan8   ;
	private String rpdivj  ;
	private String rpicut  ;
	private String rpicu   ;
	private String rpco    ;
	private String rppa8   ;
	private String rppyr   ;
	private String rpmcu   ;
	private String rpcrcd  ;
	private String rpcrr   ;
	private String rpalph  ;
	private String rpbcrc;
	private String rpcrrm;
	
	private String rpdgj;
	private String rpddj;
	private String rpddnj;
	private String rpaid;
	
	private String montoaplicado;
	private boolean pagoparcial;
	
	private String sumaMontosAplicadosRecibo;
	private String sumaMontosAplicadosReciboDom;
	private String tasaoriginal; 
	private String idCuentaDiferencialCambiario;
	private String saldoInicialDomestico;
	
	private String rpglc;
	
	private String lossAccountId;
	private String gainAccountId;
	
	public FacturaF03B11() {
		super();
	}

	public FacturaF03B11(String rppst, String rpaap, String rpfap,
			String rptorg, String rpuser, String rpjcl, String rppid,
			String rpupmj, String rpupmt, String rppo, String rpdcto,
			String rpdoc, String rpdct, String rpkco, String rpsfx,
			String rpan8, String rpdivj, String rpicut, String rpicu,
			String rpco, String rppa8, String rppyr, String rpmcu,
			String rpcrcd, String montoaplicado, String rpcrr, String rpalph,
			String rpbcrc, String rpcrrm, String rpdgj, String rpddj,
			String rpddnj, String rpaid, boolean pagoparcial, 
			String sumaMontosAplicadosRecibo , String tasaoriginal,
			//String idCuentaDiferencialCambiario, String sumaMontosAplicadosReciboDom,
			String sumaMontosAplicadosReciboDom,
			String rpglc, String lossAccountId,  String gainAccountId

	) {
		super();
		this.rppst = rppst;
		this.rpaap = rpaap;
		this.rpfap = rpfap;
		this.rptorg = rptorg;
		this.rpuser = rpuser;
		this.rpjcl = rpjcl;
		this.rppid = rppid;
		this.rpupmj = rpupmj;
		this.rpupmt = rpupmt;
		this.rppo = rppo;
		this.rpdcto = rpdcto;
		this.rpdoc = rpdoc;
		this.rpdct = rpdct;
		this.rpkco = rpkco;
		this.rpsfx = rpsfx;
		this.rpan8 = rpan8;
		this.rpdivj = rpdivj;
		this.rpicut = rpicut;
		this.rpicu = rpicu;
		this.rpco = rpco;
		this.rppa8 = rppa8;
		this.rppyr = rppyr;
		this.rpmcu = rpmcu;
		this.rpcrcd = rpcrcd ;
		this.montoaplicado = montoaplicado ;
		this.rpcrr = rpcrr ;
		this.rpalph = rpalph;
		this.rpbcrc = rpbcrc;
		this.rpcrrm = rpcrrm;
		
		this.rpdgj = rpdgj ;
		this.rpddj = rpddj ;
		this.rpddnj = rpddnj ;
		this.rpaid = rpaid ;
		
		this.pagoparcial = pagoparcial;
		this.sumaMontosAplicadosRecibo = sumaMontosAplicadosRecibo ;
		this.sumaMontosAplicadosReciboDom = sumaMontosAplicadosReciboDom;
		this.tasaoriginal = tasaoriginal;
		
		//this.idCuentaDiferencialCambiario = idCuentaDiferencialCambiario ;
		
		this.saldoInicialDomestico = rpaap ;
		this.rpglc = rpglc ;
		this. lossAccountId  = lossAccountId;
		this. gainAccountId = gainAccountId;
		
		
	}
	
	public String updateStatement(){
		try {
			
			
//			if( new BigDecimal(rpaap).compareTo(BigDecimal.ZERO) == -1 ){
//				rpaap = "0";
//			}
			if( new BigDecimal(rpfap).compareTo(BigDecimal.ZERO) == -1 ){
				rpfap = "0";
			}
			
			rppst = ( new BigDecimal(rpaap).compareTo(BigDecimal.ZERO) == 0 ) ? "P" : "A" ;
			rpjcl = ( new BigDecimal(rpaap).compareTo(BigDecimal.ZERO) == 0 ) ? rpupmj: "0" ;
			
			String query =   
			
			" UPDATE @JDEDTA.F03B11 SET "

			+ "  RPPST =  '@RPPST' "
			+ ", RPAAP =  '@RPAAP' "
			+ ", RPFAP =  '@RPFAP' "
			+ ", RPTORG = '@RPTORG' "
			+ ", RPUSER = '@RPUSER' "
			+ ", RPJCL =  '@RPJCL' " 
			+ ", RPPID  = '@RPPID' " 
			+ ", RPUPMJ = '@RPUPMJ' " 
			+ ", RPUPMT = '@RPUPMT' " 
			+ ", RPPO   = '@RPPO' " 
			+ ", RPDCTO = '@RPDCTO' "
          
			+ " WHERE " 
 
			+ "     RPDOC =  '@RPDOC' "
			+ " AND RPDCT =  '@RPDCT' "
			+ " AND RPKCO =  '@RPKCO' "
			+ " AND RPSFX =  '@RPSFX' "
			+ " AND RPAN8 =  '@RPAN8' "
			+ " AND RPDIVJ = '@RPDIVJ' "
			+ " AND RPICUT = '@RPICUT' "
			+ " AND RPICU =  '@RPICU' "
			+ " AND RPCO =   '@RPCO' "
			+ " AND RPPA8 =  '@RPPA8' "
			+ " AND RPPYR =  '@RPPYR' " ; 
			
			return query
				.replace("@JDEDTA", PropertiesSystem.JDEDTA)
				
				.replace("@RPPST", rppst)
				.replace("@RPAAP", rpaap)
				.replace("@RPFAP", rpfap)
				.replace("@RPTORG", rptorg )
				.replace("@RPUSER", rpuser)
				.replace("@RPJCL", rpjcl)
				.replace("@RPPID", rppid)
				.replace("@RPUPMJ", rpupmj )
				.replace("@RPUPMT", rpupmt)
				.replace("@RPPO", rppo )
				.replace("@RPDCTO", rpdcto)
				
				.replace("@RPDOC", rpdoc)
				.replace("@RPDCT", rpdct)
				.replace("@RPKCO", rpkco)
				.replace("@RPSFX", rpsfx)
				.replace("@RPAN8", rpan8)
				.replace("@RPDIVJ", rpdivj)
				.replace("@RPICUT", rpicut)
				.replace("@RPICU", rpicu) 
				.replace("@RPCO", rpco)
				.replace("@RPPA8", rppa8) 
				.replace("@RPPYR", rppyr) ;
			
		} catch (Exception e) {
			e.printStackTrace();
			return "" ;
		}
	}
	
	public String getRppst() {
		return rppst;
	}
	public void setRppst(String rppst) {
		this.rppst = rppst;
	}
	public String getRpaap() {
		return rpaap;
	}
	public void setRpaap(String rpaap) {
		this.rpaap = rpaap;
	}
	public String getRpfap() {
		return rpfap;
	}
	public void setRpfap(String rpfap) {
		this.rpfap = rpfap;
	}
	public String getRptorg() {
		return rptorg;
	}
	public void setRptorg(String rptorg) {
		this.rptorg = rptorg;
	}
	public String getRpuser() {
		return rpuser;
	}
	public void setRpuser(String rpuser) {
		this.rpuser = rpuser;
	}
	public String getRpjcl() {
		return rpjcl;
	}
	public void setRpjcl(String rpjcl) {
		this.rpjcl = rpjcl;
	}
	public String getRppid() {
		return rppid;
	}
	public void setRppid(String rppid) {
		this.rppid = rppid;
	}
	public String getRpupmj() {
		return rpupmj;
	}
	public void setRpupmj(String rpupmj) {
		this.rpupmj = rpupmj;
	}
	public String getRpupmt() {
		return rpupmt;
	}
	public void setRpupmt(String rpupmt) {
		this.rpupmt = rpupmt;
	}
	public String getRppo() {
		return rppo;
	}
	public void setRppo(String rppo) {
		this.rppo = rppo;
	}
	public String getRpdcto() {
		return rpdcto;
	}
	public void setRpdcto(String rpdcto) {
		this.rpdcto = rpdcto;
	}
	public String getRpdoc() {
		return rpdoc;
	}
	public void setRpdoc(String rpdoc) {
		this.rpdoc = rpdoc;
	}
	public String getRpdct() {
		return rpdct;
	}
	public void setRpdct(String rpdct) {
		this.rpdct = rpdct;
	}
	public String getRpkco() {
		return rpkco;
	}
	public void setRpkco(String rpkco) {
		this.rpkco = rpkco;
	}
	public String getRpsfx() {
		return rpsfx;
	}
	public void setRpsfx(String rpsfx) {
		this.rpsfx = rpsfx;
	}
	public String getRpan8() {
		return rpan8;
	}
	public void setRpan8(String rpan8) {
		this.rpan8 = rpan8;
	}
	public String getRpdivj() {
		return rpdivj;
	}
	public void setRpdivj(String rpdivj) {
		this.rpdivj = rpdivj;
	}
	public String getRpicut() {
		return rpicut;
	}
	public void setRpicut(String rpicut) {
		this.rpicut = rpicut;
	}
	public String getRpicu() {
		return rpicu;
	}
	public void setRpicu(String rpicu) {
		this.rpicu = rpicu;
	}
	public String getRpco() {
		return rpco;
	}
	public void setRpco(String rpco) {
		this.rpco = rpco;
	}
	public String getRppa8() {
		return rppa8;
	}
	public void setRppa8(String rppa8) {
		this.rppa8 = rppa8;
	}
	public String getRppyr() {
		return rppyr;
	}
	public void setRppyr(String rppyr) {
		this.rppyr = rppyr;
	}

	public String getRpmcu() {
		return rpmcu;
	}

	public void setRpmcu(String rpmcu) {
		this.rpmcu = rpmcu;
	}

	public String getRpcrcd() {
		return rpcrcd;
	}

	public void setRpcrcd(String rpcrcd) {
		this.rpcrcd = rpcrcd;
	}

	public String getMontoaplicado() {
		return montoaplicado;
	}

	public void setMontoaplicado(String montoaplicado) {
		this.montoaplicado = montoaplicado;
	}

	public String getRpcrr() {
		return rpcrr;
	}

	public void setRpcrr(String rpcrr) {
		this.rpcrr = rpcrr;
	}

	public String getRpalph() {
		
		if(rpalph == null ) rpalph = "" ;
		
		try{
			rpalph = rpalph.replace("'", "");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return rpalph;
	}

	public void setRpalph(String rpalph) {
		this.rpalph = rpalph;
	}

	public String getRpbcrc() {
		return rpbcrc;
	}

	public void setRpbcrc(String rpbcrc) {
		this.rpbcrc = rpbcrc;
	}

	public String getRpcrrm() {
		return rpcrrm;
	}

	public void setRpcrrm(String rpcrrm) {
		this.rpcrrm = rpcrrm;
	}

	public String getRpdgj() {
		return rpdgj;
	}

	public void setRpdgj(String rpdgj) {
		this.rpdgj = rpdgj;
	}

	public String getRpddj() {
		return rpddj;
	}

	public void setRpddj(String rpddj) {
		this.rpddj = rpddj;
	}

	public String getRpddnj() {
		return rpddnj;
	}

	public void setRpddnj(String rpddnj) {
		this.rpddnj = rpddnj;
	}

	public String getRpaid() {
		return rpaid;
	}

	public void setRpaid(String rpaid) {
		this.rpaid = rpaid;
	}

	public boolean isPagoparcial() {
		return pagoparcial;
	}

	public void setPagoparcial(boolean pagoparcial) {
		this.pagoparcial = pagoparcial;
	}

	public String getSumaMontosAplicadosRecibo() {
		return sumaMontosAplicadosRecibo;
	}

	public void setSumaMontosAplicadosRecibo(String sumaMontosAplicadosRecibo) {
		this.sumaMontosAplicadosRecibo = sumaMontosAplicadosRecibo;
	}

	public String getTasaoriginal() {
		return tasaoriginal;
	}

	public void setTasaoriginal(String tasaoriginal) {
		this.tasaoriginal = tasaoriginal;
	}

	public String getIdCuentaDiferencialCambiario() {
		return idCuentaDiferencialCambiario;
	}

	public void setIdCuentaDiferencialCambiario(String idCuentaDiferencialCambiario) {
		this.idCuentaDiferencialCambiario = idCuentaDiferencialCambiario;
	}

	public String getSumaMontosAplicadosReciboDom() {
		return sumaMontosAplicadosReciboDom;
	}

	public void setSumaMontosAplicadosReciboDom(String sumaMontosAplicadosReciboDom) {
		this.sumaMontosAplicadosReciboDom = sumaMontosAplicadosReciboDom;
	}

	public String getSaldoInicialDomestico() {
		return saldoInicialDomestico;
	}

	public void setSaldoInicialDomestico(String saldoInicialDomestico) {
		this.saldoInicialDomestico = saldoInicialDomestico;
	}

	public String getRpglc() {
		return rpglc;
	}

	public void setRpglc(String rpglc) {
		this.rpglc = rpglc;
	}

	public String getLossAccountId() {
		return lossAccountId;
	}

	public void setLossAccountId(String lossAccountId) {
		this.lossAccountId = lossAccountId;
	}

	public String getGainAccountId() {
		return gainAccountId;
	}

	public void setGainAccountId(String gainAccountId) {
		this.gainAccountId = gainAccountId;
	}
 
}

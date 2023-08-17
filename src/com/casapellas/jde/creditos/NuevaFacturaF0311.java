package com.casapellas.jde.creditos;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.casapellas.util.CodeUtil;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.PropertiesSystem;

public class NuevaFacturaF0311 {
	private String rpkco;
	private String rpan8;
	private String rpdct;
	private String rpdoc;
	private String rpsfx;
	private String rpdivj;
	private String rpdgj;
	private String rpfy;
	private String rpctry;
	private String rppn;
	private String rpco;
	private String rpicut;
	private String rpicu;
	private String rpdicj;
	private String rppa8;
	private String rpan8j;
	private String rpbalj;
	private String rppst;
	private String rpag;
	private String rpaap;
	private String rpstam;
	private String rpcrrm;
	private String rpcrcd;
	private String rpcrr;
	private String rpacr;
	private String rpfap;
	private String rpctam;
	private String rptxa1;
	private String rpexr1;
	private String rpdsvj;
	private String rpglc;
	private String rpmcu;
	private String rpddj;
	private String rpddnj;
	private String rppo;
	private String rpdcto;
	private String rprmk;
	private String rpalph;
	private String rptorg;
	private String rpuser;
	private String rppid;
	private String rpupmj;
	private String rpupmt;
	private String rpjobn;
	private String rppkco;
	private String rpodoc;
	private String rpodct;
	private String rposfx;
	private String rpokco;
	private String rpmcu2;
	
	private String rpatxn;
	private String rpctxn;
	private String rpatxa;
	private String rpctxa;
	
	private String rppyr;
	private String rpbcrc; 
	
	private String rpatad;
	private String rpctad;
	private String rpdnlt;
	
	private Date fechafactura;

	private String rpcoll ; 
	private String rpafc  ; 
	private String rpryin ; 
	private String rpomod ; 
	private String rperdj ; 
	private String rpnetst;
	private String rpaid;
	private String rpar08; 
	
	private String rppost;
	private String rpistc;
	private String rppyid;
	
	public String insertStatement(){
		String sqlInsert = "";
		
		try {
			
			String strFechaFacturaJulian = String.valueOf( FechasUtil.dateToJulian(fechafactura) ); 
			
			sqlInsert = "insert into @JDEDTA.F03B11 ( @FIELDS_TO_INSERT ) VALUES (@VALUES_TO_INSERT) " ;
			
			String insertFields = DefaultJdeFieldsValues.F03B11_INSERT_COLUMNS;
			String insertValues = DefaultJdeFieldsValues.F03B11_INSERT_COLUMNS_VALUES;
			
			insertFields += ", " +  DefaultJdeFieldsValues.F03B11_COLUMN_NAMES_DEFAULT;
			insertValues += ", " +  DefaultJdeFieldsValues.F03B11_COLUMN_NAMES_DEFAULT_VALUES;
			
			BigDecimal tasaCambioFactura = new BigDecimal(rpcrr);
			boolean facturaMonedaExtranjera =  tasaCambioFactura.compareTo(BigDecimal.ZERO) == 1 ;
			
			if(facturaMonedaExtranjera){
				rpcrrm = "F";
			}else{
				rpcrrm = "D";
			}
			
			rpdivj = strFechaFacturaJulian;
			rpdgj  = rpdivj;
			rpdicj = rpdivj;
			rpdsvj = rpdivj;
					
			rpctry = new SimpleDateFormat("yyyy").format(fechafactura).substring(0,2);
			rpfy   = new SimpleDateFormat("yyyy").format(fechafactura).substring(2,4);
			rppn   = new SimpleDateFormat("MM").format(fechafactura);
			
			if(rppa8 == null ){
				rppa8 = rpan8;
			} 
			 
			rpan8j = rpan8;
			rppyr = rpan8;
			rpbalj = "Y";
			rppst  = "P" ;
			rpdnlt = "Y";
			
			if(rpacr == null)
				rpacr = "0";
			if(rpatxa == null)
				rpatxa = "0";
			if(rpatxn == null)
				rpatxn = "0";
			if(rpacr == null)
				rpacr = "0";
			if(rpfap == null)
				rpfap = "0";
			if(rpctxa == null)
				rpctxa = "0";
			if(rpctxn == null)
				rpctxn = "0";
			
			if(rpstam == null || rpstam.trim().isEmpty() )
				rpstam  = "0";
			
			if(rpctam == null || rpctam.trim().isEmpty() )
				rpctam = "0";
			
			rpaap = rpag;
			rpfap = rpacr;
			
			rpatad = rpag ;
			rpctad = rpacr;
			
			rpddnj = rpddj;
			rpuser = rptorg;
			
			rppid = PropertiesSystem.CONTEXT_NAME;	
			rpupmj = rpdivj;	
			rpupmt =  new SimpleDateFormat("HHmmss").format(fechafactura); 	
			rpjobn =  PropertiesSystem.CONTEXT_NAME;
		
			rpkco = rpco;  
			rppkco = rpco;	
			
			if(rpodoc == null || rpodoc.isEmpty() ){
				if( rppo == null || rppo.isEmpty() ){
					rpodoc = rpdoc;
				}else{
					rpodoc = String.valueOf( Integer.parseInt(rppo) );
				}
			}
			
			rpodct = rpdct;
			rposfx = rpsfx;
			rpokco = rpco;
			
			if( rptxa1.compareTo("IMP") == 0 ){
				rpatxn = "0";
				rpctxn = "0";
			} 
			if( rptxa1.compareTo("EXE") == 0 ){
				rpatxa = "0";
				rpctxa = "0";
			} 
			if( rptxa1.trim().isEmpty() ){
				rpatxa = "0";
				rpctxa = "0";
				rpatxn = "0";
				rpctxn = "0";
				rpstam = "0";
				rpctam = "0" ; 
			} 
			
			rpmcu = CodeUtil.pad(rpmcu.trim(), 12, " ");
			rpmcu2 = CodeUtil.pad(rpmcu2.trim(), 12, " ");
			
			rpcoll  = "Y";
			rpafc   = "N";
			rpryin  = "A";
			rpomod  = "0";
			rperdj  = rpdivj;
			rpnetst = "0";
			
			if(rpar08 == null || rpar08.isEmpty() ){
				rpar08 = rpglc;
			}
			
			if(rpglc.compareTo("UC") == 0 ){
				rppst = "A" ;
				rpafc = "Y";
			} 
			
			if(rppost == null)
				rppost = "";
			if(rpistc == null)
				rpistc = "";
			if(rppyid == null)
				rppyid = "0";
			
			if( Long.parseLong(rpaap) != 0 ){
				rppst  = "A" ;
			}else{
				rppst  = "P" ;
			}
			
			//&& ========== 2018-05-28 : Consideraciones de distribucion del monto gravable (indicaciones Humberto Cisne)
			//rpatad = rpag ;
			//rpctad = rpacr;
			rpatad = new BigDecimal( rpag ).subtract(new BigDecimal( rpstam ) ).toString();
			rpctad = new BigDecimal( rpacr ).subtract(new BigDecimal( rpctam ) ).toString();

			//&& ========== 2018-05-05 : consideraciones para RU  por indicaciones Humberto Cisne
			if(rpglc.compareTo("UC") == 0 ){
				rpodoc = rpdoc;
				rpodct = rpdct;
				rpokco = rpkco;
				rposfx = rpsfx;
				rpryin = ""; 
				rpatad = "0" ;
				rpctad = "0" ;
				rperdj = "0" ;
			}
			
			insertValues = insertValues
			.replace("@RPKCO@", rpkco)
			.replace("@RPAN8@", rpan8)
			.replace("@RPDCT@", rpdct )
			.replace("@RPDOC@", rpdoc)
			.replace("@RPSFX@", rpsfx)
			.replace("@RPDIVJ@",rpdivj )
			.replace("@RPDGJ@", rpdgj )
			.replace("@RPFY@", rpfy)
			.replace("@RPCTRY@", rpctry)
			.replace("@RPPN@", rppn)
			.replace("@RPCO@", rpco)
			.replace("@RPICUT@",rpicut )
			.replace("@RPICU@", rpicu)
			.replace("@RPDICJ@", rpdicj)
			.replace("@RPPA8@", rppa8)
			.replace("@RPAN8J@", rpan8j)
			.replace("@RPBALJ@", rpbalj)
			.replace("@RPPST@", rppst)
			.replace("@RPAG@", rpag)
			.replace("@RPAAP@", rpaap)
			.replace("@RPSTAM@", rpstam )
			.replace("@RPCRRM@", rpcrrm )
			.replace("@RPCRCD@", rpcrcd)
			.replace("@RPCRR@", rpcrr)
			.replace("@RPACR@", rpacr)
			.replace("@RPFAP@", rpfap)
			.replace("@RPCTAM@", rpctam)
			.replace("@RPTXA1@", rptxa1)
			.replace("@RPEXR1@", rpexr1)
			.replace("@RPDSVJ@", rpdsvj)
			.replace("@RPGLC@", rpglc)
			.replace("@RPMCU@", rpmcu)
			.replace("@RPDDJ@", rpddj)
			.replace("@RPDDNJ@", rpddj )
			.replace("@RPPO@", rppo)
			.replace("@RPDCTO@", rpdcto)
			.replace("@RPRMK@", rprmk)
			.replace("@RPALPH@",rpalph )
			.replace("@RPTORG@",rptorg )
			.replace("@RPUSER@",rpuser)
			.replace("@RPPID@", rppid)
			.replace("@RPUPMJ@", rpupmj)
			.replace("@RPUPMT@", rpupmt)
			.replace("@RPJOBN@", rpjobn)
			.replace("@RPPKCO@", rppkco)
			.replace("@RPODOC@", rpodoc )
			.replace("@RPODCT@", rpodct )
			.replace("@RPOSFX@", rposfx)
			.replace("@RPOKCO@", rpokco)
			.replace("@RPMCU2@", rpmcu2)
			.replace("@RPATXN@", rpatxn)
			.replace("@RPCTXN@", rpctxn)
			.replace("@RPATXA@", rpatxa)
			.replace("@RPCTXA@", rpctxa)
			.replace("@RPPYR@", rppyr)
			.replace("@RPBCRC@", rpbcrc)
			
			.replace("@RPATAD@", rpatad)
			.replace("@RPCTAD@", rpctad)
			.replace("@RPDNLT@", rpdnlt)
			
			.replace("@RPCOLL@", rpcoll)
			.replace("@RPAFC@", rpafc)
			.replace("@RPRYIN@", rpryin)
			.replace("@RPOMOD@", rpomod)
			.replace("@RPERDJ@", rperdj)
			.replace("@RPNETST@", rpnetst)
			.replace("@RPAID@", rpaid)
			.replace("@RPAR08@", rpar08)
			
			.replace("@RPPOST@", rppost)
			.replace("@RPISTC@", rpistc)
			.replace("@RPPYID@", rppyid)
			
			;
			
			return 
			sqlInsert
				.replace("@JDEDTA", PropertiesSystem.JDEDTA)
				.replace("@FIELDS_TO_INSERT", insertFields)
				.replace("@VALUES_TO_INSERT", insertValues);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return sqlInsert;
	}
	
	
	public NuevaFacturaF0311() {
		super();
	}
	
	public NuevaFacturaF0311(String rpan8, String rpdct, String rpdoc,
			String rpsfx, String rpco, String rpicut,
			String rpicu, String rpag, String rpatxn, String rpcrrm,
			String rpcrcd, String rpcrr, String rpacr, String rpctxn,
			String rptxa1, String rpexr1, String rpmcu, String rpddj,
			String rppo, String rpdcto, String rprmk, String rpalph,
			String rptorg, String rpmcu2){

		this.rpan8 = rpan8;
		this.rpdct = rpdct;
		this.rpdoc = rpdoc;
		this.rpsfx = rpsfx;
		this.rpco = rpco;
		this.rpicut = rpicut;
		this.rpicu = rpicu;
		this.rpag = rpag;
		this.rpatxn = rpatxn;
		this.rpcrrm = rpcrrm;
		this.rpcrcd = rpcrcd;
		this.rpcrr = rpcrr;
		this.rpacr = rpacr;
		this.rpctxn = rpctxn;
		this.rptxa1 = rptxa1;
		this.rpexr1 = rpexr1;
		this.rpmcu = rpmcu;
		this.rpddj = rpddj;
		this.rppo = rppo;
		this.rpdcto = rpdcto;
		this.rprmk = rprmk;
		this.rpalph = rpalph;
		this.rptorg = rptorg;
		this.rpmcu2 = rpmcu2;
	}
	// constructor
	public NuevaFacturaF0311(String rpan8, String rpdct, String rpdoc,
			String rpsfx, String rpco, String rpicut, String rpicu,
			String rpcrcd, String rpcrr, String rptxa1,
			String rpexr1, String rpmcu, String rpddj, String rppo,
			String rpdcto, String rprmk, String rpalph, String rptorg,
			String rpmcu2, Date fechafactura, String rpbcrc, 
			String rpaid, String rpglc ) {

		this.rpan8 = rpan8;
		this.rpdct = rpdct;
		this.rpdoc = rpdoc;
		this.rpsfx = rpsfx;
		this.rpco = rpco;
		this.rpicut = rpicut;
		this.rpicu = rpicu;
		this.rpcrcd = rpcrcd;
		this.rpcrr = rpcrr;
		this.rptxa1 = rptxa1;
		this.rpexr1 = rpexr1;
		this.rpmcu = rpmcu;
		this.rpddj = rpddj;
		this.rppo = rppo;
		this.rpdcto = rpdcto;
		this.rprmk = rprmk;
		this.rpalph = rpalph;
		this.rptorg = rptorg;
		this.rpmcu2 = rpmcu2;
		this.rpbcrc = rpbcrc;
		this.fechafactura = fechafactura;
		this.rpaid = rpaid;
		this.rpglc = rpglc ;
	}
	
	public NuevaFacturaF0311(String rpkco, String rpan8, String rpdct,
			String rpdoc, String rpsfx, String rpdivj, String rpdgj,
			String rpfy, String rpctry, String rppn, String rpco,
			String rpicut, String rpicu, String rpdicj, String rppa8,
			String rpan8j, String rpbalj, String rppst, String rpag,
			String rpaap, String rpatxn, String rpstam, String rpcrrm,
			String rpcrcd, String rpcrr, String rpacr, String rpfap,
			String rpctxn, String rpctam, String rptxa1, String rpexr1,
			String rpdsvj, String rpglc, String rpmcu, String rpddj,
			String rpddnj, String rppo, String rpdcto, String rprmk,
			String rpalph, String rptorg, String rpuser, String rppid,
			String rpupmj, String rpupmt, String rpjobn, String rppkco,
			String rpodoc, String rpodct, String rposfx, String rpokco,
			String rpmcu2, String rpatxa, String rpctxa, String uno) {
		super();
		this.rpkco = rpkco;
		this.rpan8 = rpan8;
		this.rpdct = rpdct;
		this.rpdoc = rpdoc;
		this.rpsfx = rpsfx;
		this.rpdivj = rpdivj;
		this.rpdgj = rpdgj;
		this.rpfy = rpfy;
		this.rpctry = rpctry;
		this.rppn = rppn;
		this.rpco = rpco;
		this.rpicut = rpicut;
		this.rpicu = rpicu;
		this.rpdicj = rpdicj;
		this.rppa8 = rppa8;
		this.rpan8j = rpan8j;
		this.rpbalj = rpbalj;
		this.rppst = rppst;
		this.rpag = rpag;
		this.rpaap = rpaap;
		this.rpatxn = rpatxn;
		this.rpstam = rpstam;
		this.rpcrrm = rpcrrm;
		this.rpcrcd = rpcrcd;
		this.rpcrr = rpcrr;
		this.rpacr = rpacr;
		this.rpfap = rpfap;
		this.rpctxn = rpctxn;
		this.rpctam = rpctam;
		this.rptxa1 = rptxa1;
		this.rpexr1 = rpexr1;
		this.rpdsvj = rpdsvj;
		this.rpglc = rpglc;
		this.rpmcu = rpmcu;
		this.rpddj = rpddj;
		this.rpddnj = rpddnj;
		this.rppo = rppo;
		this.rpdcto = rpdcto;
		this.rprmk = rprmk;
		this.rpalph = rpalph;
		this.rptorg = rptorg;
		this.rpuser = rpuser;
		this.rppid = rppid;
		this.rpupmj = rpupmj;
		this.rpupmt = rpupmt;
		this.rpjobn = rpjobn;
		this.rppkco = rppkco;
		this.rpodoc = rpodoc;
		this.rpodct = rpodct;
		this.rposfx = rposfx;
		this.rpokco = rpokco;
		this.rpmcu2 = rpmcu2;
		this.rpatxa = rpatxa;
		this.rpctxa = rpctxa;
	}
	public String getRpkco() {
		return rpkco;
	}
	public void setRpkco(String rpkco) {
		this.rpkco = rpkco;
	}
	public String getRpan8() {
		return rpan8;
	}
	public void setRpan8(String rpan8) {
		this.rpan8 = rpan8;
	}
	public String getRpdct() {
		return rpdct;
	}
	public void setRpdct(String rpdct) {
		this.rpdct = rpdct;
	}
	public String getRpdoc() {
		return rpdoc;
	}
	public void setRpdoc(String rpdoc) {
		this.rpdoc = rpdoc;
	}
	public String getRpsfx() {
		return rpsfx;
	}
	public void setRpsfx(String rpsfx) {
		this.rpsfx = rpsfx;
	}
	public String getRpdivj() {
		return rpdivj;
	}
	public void setRpdivj(String rpdivj) {
		this.rpdivj = rpdivj;
	}
	public String getRpdgj() {
		return rpdgj;
	}
	public void setRpdgj(String rpdgj) {
		this.rpdgj = rpdgj;
	}
	public String getRpfy() {
		return rpfy;
	}
	public void setRpfy(String rpfy) {
		this.rpfy = rpfy;
	}
	public String getRpctry() {
		return rpctry;
	}
	public void setRpctry(String rpctry) {
		this.rpctry = rpctry;
	}
	public String getRppn() {
		return rppn;
	}
	public void setRppn(String rppn) {
		this.rppn = rppn;
	}
	public String getRpco() {
		return rpco;
	}
	public void setRpco(String rpco) {
		this.rpco = rpco;
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
	public String getRpdicj() {
		return rpdicj;
	}
	public void setRpdicj(String rpdicj) {
		this.rpdicj = rpdicj;
	}
	public String getRppa8() {
		return rppa8;
	}
	public void setRppa8(String rppa8) {
		this.rppa8 = rppa8;
	}
	public String getRpan8j() {
		return rpan8j;
	}
	public void setRpan8j(String rpan8j) {
		this.rpan8j = rpan8j;
	}
	public String getRpbalj() {
		return rpbalj;
	}
	public void setRpbalj(String rpbalj) {
		this.rpbalj = rpbalj;
	}
	public String getRppst() {
		return rppst;
	}
	public void setRppst(String rppst) {
		this.rppst = rppst;
	}
	public String getRpag() {
		return rpag;
	}
	public void setRpag(String rpag) {
		this.rpag = rpag;
	}
	public String getRpaap() {
		return rpaap;
	}
	public void setRpaap(String rpaap) {
		this.rpaap = rpaap;
	}
	public String getRpatxn() {
		return rpatxn;
	}
	public void setRpatxn(String rpatxn) {
		this.rpatxn = rpatxn;
	}
	public String getRpstam() {
		return rpstam;
	}
	public void setRpstam(String rpstam) {
		this.rpstam = rpstam;
	}
	public String getRpcrrm() {
		return rpcrrm;
	}
	public void setRpcrrm(String rpcrrm) {
		this.rpcrrm = rpcrrm;
	}
	public String getRpcrcd() {
		return rpcrcd;
	}
	public void setRpcrcd(String rpcrcd) {
		this.rpcrcd = rpcrcd;
	}
	public String getRpcrr() {
		return rpcrr;
	}
	public void setRpcrr(String rpcrr) {
		this.rpcrr = rpcrr;
	}
	public String getRpacr() {
		return rpacr;
	}
	public void setRpacr(String rpacr) {
		this.rpacr = rpacr;
	}
	public String getRpfap() {
		return rpfap;
	}
	public void setRpfap(String rpfap) {
		this.rpfap = rpfap;
	}
	public String getRpctxn() {
		return rpctxn;
	}
	public void setRpctxn(String rpctxn) {
		this.rpctxn = rpctxn;
	}
	public String getRpctam() {
		return rpctam;
	}
	public void setRpctam(String rpctam) {
		this.rpctam = rpctam;
	}
	public String getRptxa1() {
		return rptxa1;
	}
	public void setRptxa1(String rptxa1) {
		this.rptxa1 = rptxa1;
	}
	public String getRpexr1() {
		return rpexr1;
	}
	public void setRpexr1(String rpexr1) {
		this.rpexr1 = rpexr1;
	}
	public String getRpdsvj() {
		return rpdsvj;
	}
	public void setRpdsvj(String rpdsvj) {
		this.rpdsvj = rpdsvj;
	}
	public String getRpglc() {
		return rpglc;
	}
	public void setRpglc(String rpglc) {
		this.rpglc = rpglc;
	}
	public String getRpmcu() {
		return rpmcu;
	}
	public void setRpmcu(String rpmcu) {
		this.rpmcu = rpmcu;
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
	public String getRprmk() {
		return rprmk;
	}
	public void setRprmk(String rprmk) {
		this.rprmk = rprmk;
	}
	public String getRpalph() {
		return rpalph;
	}
	public void setRpalph(String rpalph) {
		this.rpalph = rpalph;
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
	public String getRpjobn() {
		return rpjobn;
	}
	public void setRpjobn(String rpjobn) {
		this.rpjobn = rpjobn;
	}
	public String getRppkco() {
		return rppkco;
	}
	public void setRppkco(String rppkco) {
		this.rppkco = rppkco;
	}
	public String getRpodoc() {
		return rpodoc;
	}
	public void setRpodoc(String rpodoc) {
		this.rpodoc = rpodoc;
	}
	public String getRpodct() {
		return rpodct;
	}
	public void setRpodct(String rpodct) {
		this.rpodct = rpodct;
	}
	public String getRposfx() {
		return rposfx;
	}
	public void setRposfx(String rposfx) {
		this.rposfx = rposfx;
	}
	public String getRpokco() {
		return rpokco;
	}
	public void setRpokco(String rpokco) {
		this.rpokco = rpokco;
	}
	public String getRpmcu2() {
		return rpmcu2;
	}
	public void setRpmcu2(String rpmcu2) {
		this.rpmcu2 = rpmcu2;
	}
	public String getRpatxa() {
		return rpatxa;
	}
	public void setRpatxa(String rpatxa) {
		this.rpatxa = rpatxa;
	}
	public String getRpctxa() {
		return rpctxa;
	}
	public void setRpctxa(String rpctxa) {
		this.rpctxa = rpctxa;
	}
	public Date getFechafactura() {
		return fechafactura;
	}
	public void setFechafactura(Date fechafactura) {
		this.fechafactura = fechafactura;
	}
	public String getRppyr() {
		return rppyr;
	}
	public void setRppyr(String rppyr) {
		this.rppyr = rppyr;
	}
	public String getRpbcrc() {
		return rpbcrc;
	}
	public void setRpbcrc(String rpbcrc) {
		this.rpbcrc = rpbcrc;
	}
	public String getRpatad() {
		return rpatad;
	}

	public void setRpatad(String rpatad) {
		this.rpatad = rpatad;
	}
	public String getRpctad() {
		return rpctad;
	}
	public void setRpctad(String rpctad) {
		this.rpctad = rpctad;
	}
	public String getRpdnlt() {
		return rpdnlt;
	}
	public void setRpdnlt(String rpdnlt) {
		this.rpdnlt = rpdnlt;
	}
	public String getRpaid() {
		return rpaid;
	}
	public void setRpaid(String rpaid) {
		this.rpaid = rpaid;
	}
	public String getRpar08() {
		return rpar08;
	}
	public void setRpar08(String rpar08) {
		this.rpar08 = rpar08;
	}

	public String getRppost() {
		return rppost;
	}

	public void setRppost(String rppost) {
		this.rppost = rppost;
	}

	public String getRpistc() {
		return rpistc;
	}

	public void setRpistc(String rpistc) {
		this.rpistc = rpistc;
	}

	public String getRppyid() {
		return rppyid;
	}

	public void setRppyid(String rppyid) {
		this.rppyid = rppyid;
	}
	
}

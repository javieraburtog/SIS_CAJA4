package com.casapellas.jde.creditos;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.casapellas.util.FechasUtil;
import com.casapellas.util.PropertiesSystem;

public class ReciboF03B13 {
	
	private String rypyid;
	private String rycknu;
	private String ryan8;
	private String rypyr;
	private String rydmtj;
	private String rydgj;
	private String ryvldt;
	private String rypost;
	private String ryeulp;
	private String ryglc;
	private String ryaid;
	private String ryctry;
	private String ryfy;
	private String rypn;
	private String ryco;
	private String ryicut;
	private String ryicu;
	private String rydicj;
	private String rypa8;
	private String ryckam;
	private String ryaap;
	private String rybcrc;
	private String rycrrm;
	private String rycrcd;
	private String rycrr;
	private String ryerdj;
	private String rycrcm;
	private String rycrr1;
	private String rycrr2;
	private String ryfcam;
	private String ryfap;
	private String ryglba;
	private String ryam;
	private String rytyin;
	private String ryexr;
	private String ryalph;
	private String rykcog;
	private String ryvdgj;
	private String rytorg;
	private String ryuser;
	private String rypid;
	private String ryupmj;
	private String ryupmt;
	private String ryjobn;
	private String ryryin;
	
	private Date fecharecibo;
	
	public ReciboF03B13() {
		super();
	}
	

	public ReciboF03B13(String rypyid, String rycknu, String ryan8,
			String rypyr, String ryco, String ryicu, String rypa8,
			String ryckam, String rybcrc, String rycrrm, String rycrcd, String rycrr,
			String ryfcam, String ryglba, String ryexr,
			String ryalph, String ryuser, String ryicut, Date fecharecibo) {
		super();

		this.rypyid = rypyid;
		this.rycknu = rycknu;
		this.ryan8 = ryan8;
		this.rypyr = rypyr;
		this.ryco = ryco;
		this.ryicu = ryicu;
		this.rypa8 = rypa8;
		this.ryckam = ryckam;
		this.rybcrc = rybcrc;
		this.rycrrm = rycrrm;
		this.rycrcd = rycrcd;
		this.rycrr = rycrr;
		this.ryfcam = ryfcam;
		this.ryglba = ryglba;
		this.ryexr = ryexr;
		this.ryalph = ryalph;
		this.ryuser = ryuser;
		this.ryicut = ryicut ;
		this.fecharecibo = fecharecibo;
		
	}

	public String insertStatement(){
		String sqlInsert = "" ;
		
		try {
			
			String strFechaReciboJulian = String.valueOf( FechasUtil.dateToJulian(fecharecibo) ); 
			
			sqlInsert = "insert into @JDEDTA.F03B13 ( @FIELDS_TO_INSERT ) VALUES (@VALUES_TO_INSERT) " ;
			
			String insertFields = DefaultJdeFieldsValues.F03B13_INSERT_COLUMNS_CR;
			String insertValues = DefaultJdeFieldsValues.F03B13_INSERT_COLUMNS_VALUES_CR;
			
			insertFields += ", " +  DefaultJdeFieldsValues.F03B13_COLUMN_NAMES_DEFAULT;
			insertValues += ", " +  DefaultJdeFieldsValues.F03B13_COLUMN_NAMES_DEFAULT_VALUES;
			
			BigDecimal tasaCambioFactura = new BigDecimal(rycrr);
			boolean facturaMonedaExtranjera =  tasaCambioFactura.compareTo(BigDecimal.ZERO) == 1 ;
			
			ryaap = "0"; 
			ryfap = "0";
			rytorg = ryuser ;
			
			rypost = ""; // "P"
			ryeulp = ""; // "U"
			
			ryam = "2";
			
			if(rytyin == null || rytyin.isEmpty())
				rytyin = "A" ;
					
			ryctry = new SimpleDateFormat("yyyy").format(fecharecibo).substring(0,2);
			ryfy   = new SimpleDateFormat("yyyy").format(fecharecibo).substring(2,4);
			rypn   = new SimpleDateFormat("MM").format(fecharecibo);
			
			rycrcm = ( facturaMonedaExtranjera )? "Y" : "";
			
			rydmtj = strFechaReciboJulian;
			rydgj  = strFechaReciboJulian;
			rydicj = strFechaReciboJulian;
			ryerdj = strFechaReciboJulian;
			
			ryupmj = strFechaReciboJulian;
			ryupmt = new SimpleDateFormat("HHmmss").format(fecharecibo);
			rypid = PropertiesSystem.CONTEXT_NAME;
			ryjobn = PropertiesSystem.CONTEXT_NAME;
			
			if(ryglc == null)
				ryglc = "" ;
			if(ryaid == null)
				ryaid = "" ;
			
			if( ryglc.compareTo("UC") == 0){
				ryaap = ryckam ;
				ryfap = ryfcam ;
			}
			
			if( ryryin == null)
				ryryin = "";
			
			//&& ========== 2018-05-05 : consideraciones para RU  por indicaciones Humberto Cisne
			if( ryglc.compareTo("UC") == 0){
				ryeulp = "U";
				rytyin = "U"; 
			}
			
			
			insertValues = insertValues
			.replace("@RYPYID", rypyid)
			.replace("@RYCKNU", rycknu)
			.replace("@RYAN8", ryan8)
			.replace("@RYPYR", rypyr)
			.replace("@RYDMTJ", rydmtj)
			.replace("@RYDGJ", rydgj) 
			
			.replace("@RYPOST", rypost)
			.replace("@RYEULP", ryeulp)			
			.replace("@RYCTRY",ryctry )
			.replace("@RYFY", ryfy)
			.replace("@RYPN", rypn)
			.replace("@RYCO", ryco)
			
			.replace("@RYICUT", ryicut)
			.replace("@RYICU", ryicu)
			.replace("@RYDICJ", rydicj)
			.replace("@RYPA8", rypa8)
			
			.replace("@RYCKAM", ryckam )
			.replace("@RYAAP", ryaap)
			
			.replace("@RYBCRC", rybcrc)
			.replace("@RYCRCD", rycrcd)
			.replace("@RYERDJ", ryerdj)
			.replace("@RYCRCM", rycrcm)			
			.replace("@RYCRRM", rycrrm)
			.replace("@RYCRR", rycrr)
			
			.replace("@RYFCAM", ryfcam)
			.replace("@RYFAP", ryfap)
			.replace("@RYGLBA", ryglba)
			.replace("@RYAM", ryam)
			.replace("@RYTYIN", rytyin)
			.replace("@RYEXR", ryexr)
			
			.replace("@RYALPH", ryalph)
			.replace("@RYTORG", rytorg)
			.replace("@RYUSER", ryuser)
			.replace("@RYPID", rypid)
			.replace("@RYUPMJ", ryupmj)
			.replace("@RYUPMT", ryupmt)
			.replace("@RYJOBN", ryjobn)
			
			.replace("@RYGLC", ryglc)
			.replace("@RYAID", ryaid)
			.replace("@RYRYIN", ryryin)
			
			;

			return 
			sqlInsert
				.replace("@JDEDTA", PropertiesSystem.JDEDTA)
				.replace("@FIELDS_TO_INSERT", insertFields)
				.replace("@VALUES_TO_INSERT", insertValues);
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			sqlInsert = "";
		}
		return sqlInsert;
	}
	

	public ReciboF03B13(String rypyid, String rycknu, String ryan8,
			String rypyr, String rydmtj, String rydgj,
			String rypost, String ryeulp, String ryglc, String ryaid,
			String ryctry, String ryfy, String rypn, String ryco,
			String ryicut, String ryicu, String rydicj, String rypa8,
			String ryckam, String ryaap, String rybcrc, String rycrrm,
			String rycrcd, String rycrr, String ryerdj, String rycrcm,
			String rycrr1, String rycrr2, String ryfcam, String ryfap,
			String ryglba, String ryam, String rytyin, String ryexr,
			String ryalph, String rykcog, String ryvdgj, String rytorg,
			String ryuser, String rypid, String ryupmj, String ryupmt,
			String ryjobn) {
		
		super();
		this.rypyid = rypyid;
		this.rycknu = rycknu;
		this.ryan8 = ryan8;
		this.rypyr = rypyr;
		this.rydmtj = rydmtj;
		this.rydgj = rydgj;
		this.rypost = rypost;
		this.ryeulp = ryeulp;
		this.ryglc = ryglc;
		this.ryaid = ryaid;
		this.ryctry = ryctry;
		this.ryfy = ryfy;
		this.rypn = rypn;
		this.ryco = ryco;
		this.ryicut = ryicut;
		this.ryicu = ryicu;
		this.rydicj = rydicj;
		this.rypa8 = rypa8;
		this.ryckam = ryckam;
		this.ryaap = ryaap;
		this.rybcrc = rybcrc;
		this.rycrrm = rycrrm;
		this.rycrcd = rycrcd;
		this.rycrr = rycrr;
		this.ryerdj = ryerdj;
		this.rycrcm = rycrcm;
		this.rycrr1 = rycrr1;
		this.rycrr2 = rycrr2;
		this.ryfcam = ryfcam;
		this.ryfap = ryfap;
		this.ryglba = ryglba;
		this.ryam = ryam;
		this.rytyin = rytyin;
		this.ryexr = ryexr;
		this.ryalph = ryalph;
		this.rykcog = rykcog;
		this.ryvdgj = ryvdgj;
		this.rytorg = rytorg;
		this.ryuser = ryuser;
		this.rypid = rypid;
		this.ryupmj = ryupmj;
		this.ryupmt = ryupmt;
		this.ryjobn = ryjobn;
	}
	
	public String getRypyid() {
		return rypyid;
	}
	public void setRypyid(String rypyid) {
		this.rypyid = rypyid;
	}
	public String getRycknu() {
		return rycknu;
	}
	public void setRycknu(String rycknu) {
		this.rycknu = rycknu;
	}
	public String getRyan8() {
		return ryan8;
	}
	public void setRyan8(String ryan8) {
		this.ryan8 = ryan8;
	}
	public String getRypyr() {
		return rypyr;
	}
	public void setRypyr(String rypyr) {
		this.rypyr = rypyr;
	}
	public String getRydmtj() {
		return rydmtj;
	}
	public void setRydmtj(String rydmtj) {
		this.rydmtj = rydmtj;
	}
	public String getRydgj() {
		return rydgj;
	}
	public void setRydgj(String rydgj) {
		this.rydgj = rydgj;
	}
	public String getRyvldt() {
		return ryvldt;
	}
	public void setRyvldt(String ryvldt) {
		this.ryvldt = ryvldt;
	}
	public String getRypost() {
		return rypost;
	}
	public void setRypost(String rypost) {
		this.rypost = rypost;
	}
	public String getRyeulp() {
		return ryeulp;
	}
	public void setRyeulp(String ryeulp) {
		this.ryeulp = ryeulp;
	}
	public String getRyglc() {
		return ryglc;
	}
	public void setRyglc(String ryglc) {
		this.ryglc = ryglc;
	}
	public String getRyaid() {
		return ryaid;
	}
	public void setRyaid(String ryaid) {
		this.ryaid = ryaid;
	}
	public String getRyctry() {
		return ryctry;
	}
	public void setRyctry(String ryctry) {
		this.ryctry = ryctry;
	}
	public String getRyfy() {
		return ryfy;
	}
	public void setRyfy(String ryfy) {
		this.ryfy = ryfy;
	}
	public String getRypn() {
		return rypn;
	}
	public void setRypn(String rypn) {
		this.rypn = rypn;
	}
	public String getRyco() {
		return ryco;
	}
	public void setRyco(String ryco) {
		this.ryco = ryco;
	}
	public String getRyicut() {
		return ryicut;
	}
	public void setRyicut(String ryicut) {
		this.ryicut = ryicut;
	}
	public String getRyicu() {
		return ryicu;
	}
	public void setRyicu(String ryicu) {
		this.ryicu = ryicu;
	}
	public String getRydicj() {
		return rydicj;
	}
	public void setRydicj(String rydicj) {
		this.rydicj = rydicj;
	}
	public String getRypa8() {
		return rypa8;
	}
	public void setRypa8(String rypa8) {
		this.rypa8 = rypa8;
	}
	public String getRyckam() {
		return ryckam;
	}
	public void setRyckam(String ryckam) {
		this.ryckam = ryckam;
	}
	public String getRyaap() {
		return ryaap;
	}
	public void setRyaap(String ryaap) {
		this.ryaap = ryaap;
	}
	public String getRybcrc() {
		return rybcrc;
	}
	public void setRybcrc(String rybcrc) {
		this.rybcrc = rybcrc;
	}
	public String getRycrrm() {
		return rycrrm;
	}
	public void setRycrrm(String rycrrm) {
		this.rycrrm = rycrrm;
	}
	public String getRycrcd() {
		return rycrcd;
	}
	public void setRycrcd(String rycrcd) {
		this.rycrcd = rycrcd;
	}
	public String getRycrr() {
		return rycrr;
	}
	public void setRycrr(String rycrr) {
		this.rycrr = rycrr;
	}
	public String getRyerdj() {
		return ryerdj;
	}
	public void setRyerdj(String ryerdj) {
		this.ryerdj = ryerdj;
	}
	public String getRycrcm() {
		return rycrcm;
	}
	public void setRycrcm(String rycrcm) {
		this.rycrcm = rycrcm;
	}
	public String getRycrr1() {
		return rycrr1;
	}
	public void setRycrr1(String rycrr1) {
		this.rycrr1 = rycrr1;
	}
	public String getRycrr2() {
		return rycrr2;
	}
	public void setRycrr2(String rycrr2) {
		this.rycrr2 = rycrr2;
	}
	public String getRyfcam() {
		return ryfcam;
	}
	public void setRyfcam(String ryfcam) {
		this.ryfcam = ryfcam;
	}
	public String getRyfap() {
		return ryfap;
	}
	public void setRyfap(String ryfap) {
		this.ryfap = ryfap;
	}
	public String getRyglba() {
		return ryglba;
	}
	public void setRyglba(String ryglba) {
		this.ryglba = ryglba;
	}
	public String getRyam() {
		return ryam;
	}
	public void setRyam(String ryam) {
		this.ryam = ryam;
	}
	public String getRytyin() {
		return rytyin;
	}
	public void setRytyin(String rytyin) {
		this.rytyin = rytyin;
	}
	public String getRyexr() {
		return ryexr;
	}
	public void setRyexr(String ryexr) {
		this.ryexr = ryexr;
	}
	public String getRyalph() {
		return ryalph;
	}
	public void setRyalph(String ryalph) {
		this.ryalph = ryalph;
	}
	public String getRykcog() {
		return rykcog;
	}
	public void setRykcog(String rykcog) {
		this.rykcog = rykcog;
	}
	public String getRyvdgj() {
		return ryvdgj;
	}
	public void setRyvdgj(String ryvdgj) {
		this.ryvdgj = ryvdgj;
	}
	public String getRytorg() {
		return rytorg;
	}
	public void setRytorg(String rytorg) {
		this.rytorg = rytorg;
	}
	public String getRyuser() {
		return ryuser;
	}
	public void setRyuser(String ryuser) {
		this.ryuser = ryuser;
	}
	public String getRypid() {
		return rypid;
	}
	public void setRypid(String rypid) {
		this.rypid = rypid;
	}
	public String getRyupmj() {
		return ryupmj;
	}
	public void setRyupmj(String ryupmj) {
		this.ryupmj = ryupmj;
	}
	public String getRyupmt() {
		return ryupmt;
	}
	public void setRyupmt(String ryupmt) {
		this.ryupmt = ryupmt;
	}
	public String getRyjobn() {
		return ryjobn;
	}
	public void setRyjobn(String ryjobn) {
		this.ryjobn = ryjobn;
	}
	public String getRyryin() {
		return ryryin;
	}
	public void setRyryin(String ryryin) {
		this.ryryin = ryryin;
	}
}

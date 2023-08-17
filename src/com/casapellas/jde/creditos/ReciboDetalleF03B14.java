package com.casapellas.jde.creditos;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.casapellas.util.CodeUtil;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.PropertiesSystem;

public class ReciboDetalleF03B14 {

	private String rzpyid   ;
	private String  rzrc5   ;
	private String  rzcknu  ;
	private String  rzdoc   ;
	private String  rzdct   ;
	private String  rzkco   ;
	private String  rzsfx   ;
	private String  rzan8   ;
	private String  rzdctm  ;
	private String  rzdmtj  ;
	private String  rzdgj   ;
	private String  rzaid   ;
	private String  rzctry  ;
	private String  rzfy    ;
	private String  rzpn    ;
	private String  rzco    ;
	private String  rzicut  ;
	private String  rzicu   ;
	private String  rzdicj  ;
	private String  rzpa8   ;
	private String  rzrpid  ;
	private String  rzpaap  ;
	private String  rzbcrc  ;
	private String  rzcrrm  ;
	private String  rzcrcd  ;
	private String  rzcrr   ;
	private String  rzpfap  ;
	private String  rzagl   ;
	private String  rzaidt  ;
	private String  rztcrc  ;
	private String  rztaap  ;
	private String  rztyin  ;
	private String  rzutic  ;
	private String  rzmcu   ;
	private String  rzrmk   ;
	private String  rzhcrr  ;
	private String  rzpdlt  ;
	private String  rzddj   ;
	private String  rzddnj  ;
	private String  rzidgj  ;
	private String  rztorg  ;
	private String  rzuser  ;
	private String  rzpid   ;
	private String  rzupmj  ;
	private String  rzupmt  ;
	private String  rzjobn  ;
	private String  rzglc   ;
	
	private String rzaid2 ;
	private String rzam2  ;
	private String rzsbl  ;
	private String rzsblt ;
	
	private Date fecharecibo;
	public boolean unappliedCash; 

	public ReciboDetalleF03B14() {
		super();
		unappliedCash = false;
	}

	public ReciboDetalleF03B14(String rzpyid, String rzrc5, String rzcknu,
			String rzdoc, String rzdct, String rzkco, String rzsfx,
			String rzan8, String rzaid, String rzco, String rzicu,
			String rzicut, String rzpa8, String rzpaap, String rzbcrc,
			String rzcrrm, String rzcrcd, String rzcrr, String rzpfap,
			String rztcrc, String rztaap, String rzmcu, String rzrmk,
			String rzhcrr, String rztorg, String rzagl, String rzaidt,
			String rzidgj, String rzddj, String rzddnj, String rzpdlt,
			Date fecharecibo, String rzglc) {
		
		super();
		this.rzpyid = rzpyid;
		this.rzrc5 = rzrc5;
		this.rzcknu = rzcknu;
		this.rzdoc = rzdoc;
		this.rzdct = rzdct;
		this.rzkco = rzkco;
		this.rzsfx = rzsfx;
		this.rzan8 = rzan8;
		this.rzaid = rzaid;
		this.rzco = rzco;
		this.rzicu = rzicu;
		this.rzicut = rzicut;
		this.rzpa8 = rzpa8;
		this.rzpaap = rzpaap;
		this.rzbcrc = rzbcrc;
		this.rzcrrm = rzcrrm;
		this.rzcrcd = rzcrcd;
		this.rzcrr = rzcrr;
		this.rzpfap = rzpfap;
		this.rztcrc = rztcrc;
		this.rztaap = rztaap;
		this.rzmcu = rzmcu;
		this.rzrmk = rzrmk;
		this.rzhcrr = rzhcrr;
		this.rztorg = rztorg;
		this.rzagl = rzagl ;
		this.rzaidt = rzaidt ;
		
		this.rzidgj = rzidgj;
		this.rzddj  = rzddj;
		this.rzddnj = rzddnj;
		this.rzpdlt = rzpdlt;
		
		this.fecharecibo = fecharecibo;
		
		this.rzglc = rzglc;

	}
	
	public ReciboDetalleF03B14(String rzpyid, String rzrc5, String rzcknu,
			String rzdoc, String rzdct, String rzkco, String rzsfx,
			String rzan8, String rzdctm, String rzdmtj, String rzdgj,
			String rzaid, String rzctry, String rzfy, String rzpn, String rzco,
			String rzicut, String rzicu, String rzdicj, String rzpa8,
			String rzrpid, String rzpaap, String rzbcrc, String rzcrrm,
			String rzcrcd, String rzcrr, String rzpfap, String rzagl,
			String rzaidt, String rztcrc, String rztaap, String rztyin,
			String rzutic, String rzmcu, String rzrmk, String rzhcrr,
			String rzpdlt, String rzddj, String rzddnj, String rzidgj,
			String rztorg, String rzuser, String rzpid, String rzupmj,
			String rzupmt, String rzjobn) {
		super();
		this.rzpyid = rzpyid;
		this.rzrc5 = rzrc5;
		this.rzcknu = rzcknu;
		this.rzdoc = rzdoc;
		this.rzdct = rzdct;
		this.rzkco = rzkco;
		this.rzsfx = rzsfx;
		this.rzan8 = rzan8;
		this.rzdctm = rzdctm;
		this.rzdmtj = rzdmtj;
		this.rzdgj = rzdgj;
		this.rzaid = rzaid;
		this.rzctry = rzctry;
		this.rzfy = rzfy;
		this.rzpn = rzpn;
		this.rzco = rzco;
		this.rzicut = rzicut;
		this.rzicu = rzicu;
		this.rzdicj = rzdicj;
		this.rzpa8 = rzpa8;
		this.rzrpid = rzrpid;
		this.rzpaap = rzpaap;
		this.rzbcrc = rzbcrc;
		this.rzcrrm = rzcrrm;
		this.rzcrcd = rzcrcd;
		this.rzcrr = rzcrr;
		this.rzpfap = rzpfap;
		this.rzagl = rzagl;
		this.rzaidt = rzaidt;
		this.rztcrc = rztcrc;
		this.rztaap = rztaap;
		this.rztyin = rztyin;
		this.rzutic = rzutic;
		this.rzmcu = rzmcu;
		this.rzrmk = rzrmk;
		this.rzhcrr = rzhcrr;
		this.rzpdlt = rzpdlt;
		this.rzddj = rzddj;
		this.rzddnj = rzddnj;
		this.rzidgj = rzidgj;
		this.rztorg = rztorg;
		this.rzuser = rzuser;
		this.rzpid = rzpid;
		this.rzupmj = rzupmj;
		this.rzupmt = rzupmt;
		this.rzjobn = rzjobn;
	}
	
	public String insertStatement(){
		String insertStatement = "";
		
		try {
			
			String strFechaReciboJulian = String.valueOf( FechasUtil.dateToJulian(fecharecibo) ); 
			
			insertStatement = "insert into @JDEDTA.F03B14 ( @FIELDS_TO_INSERT ) VALUES (@VALUES_TO_INSERT) " ;
			
			String insertFields = DefaultJdeFieldsValues.F03B14_INSERT_COLUMNS_CR;
			String insertValues = DefaultJdeFieldsValues.F03B14_INSERT_COLUMNS_VALUES_CR ;
			
			insertFields += ", " +  DefaultJdeFieldsValues.F03B14_COLUMN_NAMES_DEFAULT;
			insertValues += ", " +  DefaultJdeFieldsValues.F03B14_COLUMN_NAMES_DEFAULT_VALUES;
			
			//&& ============= valores por omision
			rzdmtj = strFechaReciboJulian;
			rzdgj  = strFechaReciboJulian;
			rzdicj = strFechaReciboJulian;
			rzupmj = strFechaReciboJulian;
			
			rzrpid  = rzpyid;
			rzuser = rztorg;
			rzdctm = "RC";
			rzctry = new SimpleDateFormat("yyyy").format(fecharecibo).substring(0,2);
			rzfy   = new SimpleDateFormat("yyyy").format(fecharecibo).substring(2,4);
			rzpn   = new SimpleDateFormat("MM").format(fecharecibo);

			if( rzglc == null )
				rzglc = "" ;
			
			if(rztyin == null || rztyin.isEmpty() )
				rztyin = "A";
			if(rzutic == null || rzutic.isEmpty() )
				rzutic = "10";
			
			rzupmt = new SimpleDateFormat("HHmmss").format(fecharecibo);
			rzpid = PropertiesSystem.CONTEXT_NAME;
			rzjobn = PropertiesSystem.CONTEXT_NAME;
			
			if( unappliedCash ){
				rzdctm = rzdct;
				rzglc = "UC";
				rztyin = "U";
				rzutic = "";
			}
			
			rzmcu = CodeUtil.pad(rzmcu.trim(), 12, " ");
			
			if( rzaid2 == null )
				rzaid2 = "";
			if( rzam2 == null )
				rzam2 = "";
			if( rzsbl == null )
				rzsbl = "";
			if( rzsblt == null )
				rzsblt = "";
			
			
			if(rzagl == null || rzagl.isEmpty() || rzagl.trim().compareTo("0") == 0 ){
				rzaidt = "" ; 
			}
			
			insertValues = insertValues
				
				.replace("@RZPYID@", rzpyid)
				.replace("@RZRC5@", rzrc5)
				.replace("@RZCKNU@",rzcknu )
				.replace("@RZDOC@", rzdoc)				
				.replace("@RZKCO@", rzkco)
				.replace("@RZSFX@", rzsfx)
				
				.replace("@RZAN8@", rzan8)
				.replace("@RZDMTJ@", rzdmtj)
				.replace("@RZDGJ@", rzdgj)				
				.replace("@RZCTRY@", rzctry)
				.replace("@RZFY@", rzfy)
				.replace("@RZPN@", rzpn)
				
				.replace("@RZCO@", rzco)				
				.replace("@RZDICJ@", rzdicj)
				.replace("@RZPA8@", rzpa8)
				.replace("@RZRPID@", rzrpid)
				.replace("@RZPAAP@", rzpaap )
				.replace("@RZBCRC@", rzbcrc)
				
				.replace("@RZCRCD@", rzcrcd)				
				.replace("@RZPFAP@", rzpfap)
				.replace("@RZAGL@", rzagl)				
				.replace("@RZTCRC@", rztcrc)
				.replace("@RZTAAP@", rztaap)
				.replace("@RZTYIN@", rztyin)
				
				.replace("@RZUTIC@", rzutic)
				.replace("@RZMCU@", rzmcu)
				.replace("@RZRMK@", rzrmk)
				.replace("@RZHCRR@", rzhcrr)
				.replace("@RZPDLT@", rzpdlt )
				.replace("@RZDDJ@", rzddj)
				
				.replace("@RZDDNJ@", rzddnj)
				.replace("@RZIDGJ@", rzidgj)
				.replace("@RZTORG@", rztorg)
				.replace("@RZUSER@", rzuser)
				.replace("@RZPID@", rzpid)
				.replace("@RZUPMJ@", rzupmj)
				
				.replace("@RZUPMT@", rzupmt)				
				.replace("@RZJOBN@", rzjobn)
				
				.replace("@RZDCTM@", rzdctm )
				.replace("@RZDCT@", rzdct)
				 
				.replace("@RZAIDT@", rzaidt)
				.replace("@RZAID@", rzaid)  
				
				.replace("@RZICUT@", rzicut)
				.replace("@RZICU@", rzicu) 
				
				.replace("@RZCRRM@", rzcrrm )
				.replace("@RZCRR@", rzcrr) 
				.replace("@RZGLC@", rzglc) 
				
				.replace("@RZAID2@", rzaid2) 
				.replace("@RZAM2@", rzam2) 
				.replace("@RZSBL@", rzsbl) 
				.replace("@RZSBLT@", rzsblt) 
				;
			
			return 
			insertStatement
				.replace("@JDEDTA", PropertiesSystem.JDEDTA)
				.replace("@FIELDS_TO_INSERT", insertFields)
				.replace("@VALUES_TO_INSERT", insertValues);
			
		} catch (Exception e) {
			e.printStackTrace();
			insertStatement = "" ;
		}
		return insertStatement;
	}

	public String getRzpyid() {
		return rzpyid;
	}

	public void setRzpyid(String rzpyid) {
		this.rzpyid = rzpyid;
	}

	public String getRzrc5() {
		return rzrc5;
	}

	public void setRzrc5(String rzrc5) {
		this.rzrc5 = rzrc5;
	}

	public String getRzcknu() {
		return rzcknu;
	}

	public void setRzcknu(String rzcknu) {
		this.rzcknu = rzcknu;
	}

	public String getRzdoc() {
		return rzdoc;
	}

	public void setRzdoc(String rzdoc) {
		this.rzdoc = rzdoc;
	}

	public String getRzdct() {
		return rzdct;
	}

	public void setRzdct(String rzdct) {
		this.rzdct = rzdct;
	}

	public String getRzkco() {
		return rzkco;
	}

	public void setRzkco(String rzkco) {
		this.rzkco = rzkco;
	}

	public String getRzsfx() {
		return rzsfx;
	}

	public void setRzsfx(String rzsfx) {
		this.rzsfx = rzsfx;
	}

	public String getRzan8() {
		return rzan8;
	}

	public void setRzan8(String rzan8) {
		this.rzan8 = rzan8;
	}

	public String getRzdctm() {
		return rzdctm;
	}

	public void setRzdctm(String rzdctm) {
		this.rzdctm = rzdctm;
	}

	public String getRzdmtj() {
		return rzdmtj;
	}

	public void setRzdmtj(String rzdmtj) {
		this.rzdmtj = rzdmtj;
	}

	public String getRzdgj() {
		return rzdgj;
	}

	public void setRzdgj(String rzdgj) {
		this.rzdgj = rzdgj;
	}

	public String getRzaid() {
		return rzaid;
	}

	public void setRzaid(String rzaid) {
		this.rzaid = rzaid;
	}

	public String getRzctry() {
		return rzctry;
	}

	public void setRzctry(String rzctry) {
		this.rzctry = rzctry;
	}

	public String getRzfy() {
		return rzfy;
	}

	public void setRzfy(String rzfy) {
		this.rzfy = rzfy;
	}

	public String getRzpn() {
		return rzpn;
	}

	public void setRzpn(String rzpn) {
		this.rzpn = rzpn;
	}

	public String getRzco() {
		return rzco;
	}

	public void setRzco(String rzco) {
		this.rzco = rzco;
	}

	public String getRzicut() {
		return rzicut;
	}

	public void setRzicut(String rzicut) {
		this.rzicut = rzicut;
	}

	public String getRzicu() {
		return rzicu;
	}

	public void setRzicu(String rzicu) {
		this.rzicu = rzicu;
	}

	public String getRzdicj() {
		return rzdicj;
	}

	public void setRzdicj(String rzdicj) {
		this.rzdicj = rzdicj;
	}

	public String getRzpa8() {
		return rzpa8;
	}

	public void setRzpa8(String rzpa8) {
		this.rzpa8 = rzpa8;
	}

	public String getRzrpid() {
		return rzrpid;
	}

	public void setRzrpid(String rzrpid) {
		this.rzrpid = rzrpid;
	}

	public String getRzpaap() {
		return rzpaap;
	}

	public void setRzpaap(String rzpaap) {
		this.rzpaap = rzpaap;
	}

	public String getRzbcrc() {
		return rzbcrc;
	}

	public void setRzbcrc(String rzbcrc) {
		this.rzbcrc = rzbcrc;
	}

	public String getRzcrrm() {
		return rzcrrm;
	}

	public void setRzcrrm(String rzcrrm) {
		this.rzcrrm = rzcrrm;
	}

	public String getRzcrcd() {
		return rzcrcd;
	}

	public void setRzcrcd(String rzcrcd) {
		this.rzcrcd = rzcrcd;
	}

	public String getRzcrr() {
		return rzcrr;
	}

	public void setRzcrr(String rzcrr) {
		this.rzcrr = rzcrr;
	}

	public String getRzpfap() {
		return rzpfap;
	}

	public void setRzpfap(String rzpfap) {
		this.rzpfap = rzpfap;
	}

	public String getRzagl() {
		if ( rzagl == null || rzagl.isEmpty() )
			rzagl = "0";
		return rzagl;
	}

	public void setRzagl(String rzagl) {
		this.rzagl = rzagl;
	}

	public String getRzaidt() {
		return rzaidt;
	}

	public void setRzaidt(String rzaidt) {
		this.rzaidt = rzaidt;
	}

	public String getRztcrc() {
		return rztcrc;
	}

	public void setRztcrc(String rztcrc) {
		this.rztcrc = rztcrc;
	}

	public String getRztaap() {
		return rztaap;
	}

	public void setRztaap(String rztaap) {
		this.rztaap = rztaap;
	}

	public String getRztyin() {
		return rztyin;
	}

	public void setRztyin(String rztyin) {
		this.rztyin = rztyin;
	}

	public String getRzutic() {
		return rzutic;
	}

	public void setRzutic(String rzutic) {
		this.rzutic = rzutic;
	}

	public String getRzmcu() {
		return rzmcu;
	}

	public void setRzmcu(String rzmcu) {
		this.rzmcu = rzmcu;
	}

	public String getRzrmk() {
		return rzrmk;
	}

	public void setRzrmk(String rzrmk) {
		this.rzrmk = rzrmk;
	}

	public String getRzhcrr() {
		return rzhcrr;
	}

	public void setRzhcrr(String rzhcrr) {
		this.rzhcrr = rzhcrr;
	}

	public String getRzpdlt() {
		return rzpdlt;
	}

	public void setRzpdlt(String rzpdlt) {
		this.rzpdlt = rzpdlt;
	}

	public String getRzddj() {
		return rzddj;
	}

	public void setRzddj(String rzddj) {
		this.rzddj = rzddj;
	}

	public String getRzddnj() {
		return rzddnj;
	}

	public void setRzddnj(String rzddnj) {
		this.rzddnj = rzddnj;
	}

	public String getRzidgj() {
		return rzidgj;
	}

	public void setRzidgj(String rzidgj) {
		this.rzidgj = rzidgj;
	}

	public String getRztorg() {
		return rztorg;
	}

	public void setRztorg(String rztorg) {
		this.rztorg = rztorg;
	}

	public String getRzuser() {
		return rzuser;
	}

	public void setRzuser(String rzuser) {
		this.rzuser = rzuser;
	}

	public String getRzpid() {
		return rzpid;
	}

	public void setRzpid(String rzpid) {
		this.rzpid = rzpid;
	}

	public String getRzupmj() {
		return rzupmj;
	}

	public void setRzupmj(String rzupmj) {
		this.rzupmj = rzupmj;
	}

	public String getRzupmt() {
		return rzupmt;
	}

	public void setRzupmt(String rzupmt) {
		this.rzupmt = rzupmt;
	}

	public String getRzjobn() {
		return rzjobn;
	}

	public void setRzjobn(String rzjobn) {
		this.rzjobn = rzjobn;
	}

	public Date getFecharecibo() {
		return fecharecibo;
	}

	public void setFecharecibo(Date fecharecibo) {
		this.fecharecibo = fecharecibo;
	}

	public String getRzglc() {
		return rzglc;
	}

	public void setRzglc(String rzglc) {
		this.rzglc = rzglc;
	}

	public String getRzaid2() {
		return rzaid2;
	}

	public void setRzaid2(String rzaid2) {
		this.rzaid2 = rzaid2;
	}

	public String getRzam2() {
		return rzam2;
	}

	public void setRzam2(String rzam2) {
		this.rzam2 = rzam2;
	}

	public String getRzsbl() {
		return rzsbl;
	}

	public void setRzsbl(String rzsbl) {
		this.rzsbl = rzsbl;
	}

	public String getRzsblt() {
		return rzsblt;
	}

	public void setRzsblt(String rzsblt) {
		this.rzsblt = rzsblt;
	} 
	
}

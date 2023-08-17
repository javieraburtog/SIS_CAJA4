package com.casapellas.jde.creditos;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.casapellas.util.FechasUtil;
import com.casapellas.util.PropertiesSystem;

public class BatchControlF0011 {

	private String icicut;
	private String icicu;
	private String icist;
	private String iciapp;
	private String icaicu;
	private String icuser;
	private String icdicj;
	private String icndo;
	private String icbal;
	private String icbalj;
	private String icame;
	private String icdocn;
	private String icausr;
	private String icpob;
	private String iciboi;
	private String icaipt;
	private String icoffp;
	private String icpid;
	private String icjobn;
	private String icupmj;
	private String icupmt;
	private String icdrsp;
	private String ic52pp;
	private String iccbp;
	
	private Date fecharecibo;
	
	public BatchControlF0011() {
		super();
	}
	
	public BatchControlF0011(String icicut, String icicu, String icaicu,
			String icuser, String icame, String icdocn,
			String icaipt, Date fecharecibo, String icjobn, String icist) {

		this.icicut = icicut;
		this.icicu = icicu;
		this.icaicu = icaicu;
		this.icuser = icuser;
		this.icame = icame;
		this.icdocn = icdocn;
		this.icaipt = icaipt;
		this.fecharecibo = fecharecibo;
		this.icjobn = icjobn;
		this.icist = icist;
	}
	
	public String insertStatement(){
		String insertStatement = "";
		
		try {
			
			String strFechaReciboJulian = String.valueOf( FechasUtil.dateToJulian(fecharecibo) ); 
			
			insertStatement = "insert into @JDEDTA.F0011 ( @FIELDS_TO_INSERT ) VALUES (@VALUES_TO_INSERT) " ;
			
			String insertFields = DefaultJdeFieldsValues.F0011_INSERT_COLUMNS;
			String insertValues = DefaultJdeFieldsValues.F0011_INSERT_COLUMNS_VALUES ;
			
			insertFields += ", " +  DefaultJdeFieldsValues.F0011_COLUMN_NAMES_DEFAULT;
			insertValues += ", " +  DefaultJdeFieldsValues.F0011_COLUMN_NAMES_DEFAULT_VALUES;
			
			icdicj = strFechaReciboJulian;
			icpob = "N";
			icbal = "Y";
			icbalj = "Y";
			iciapp = "A";
			
			icpid =  PropertiesSystem.ESQUEMA ;
			icupmj = strFechaReciboJulian;
			icupmt = new SimpleDateFormat("HHmmss").format( new Date() ) ; 
			
			insertValues = insertValues
				.replace("@ICICUT",  icicut)
				.replace("@ICICU",  icicu)
				.replace("@ICIST", getIcist() )
				.replace("@ICIAPP",  iciapp)
				.replace("@ICAICU",  icaicu)
				.replace("@ICUSER", icuser )
				.replace( "@ICDICJ",  icdicj)
				.replace("@ICBALJ", icbalj )
				.replace("@ICBAL",  icbal)
				.replace("@ICAME", icame )
				.replace("@ICDOCN", icdocn )
				.replace( "@ICPOB",  icpob)
				.replace("@ICAIPT" , icaipt ) 
				
				.replace("@ICPID" , icpid )
				.replace("@ICJOBN" , getIcjobn() )
				.replace("@ICUPMJ" , icupmj )
				.replace("@ICUPMT" , icupmt )
				
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
	
	
	
	public BatchControlF0011(String icicut, String icicu, String icist,
			String iciapp, String icaicu, String icuser, String icdicj,
			String icndo, String icbal, String icbalj, String icame,
			String icdocn, String icausr, String icpob, String iciboi,
			String icaipt, String icoffp, String icpid, String icjobn,
			String icupmj, String icupmt, String icdrsp, String ic52pp,
			String iccbp) {
		super();
		this.icicut = icicut;
		this.icicu = icicu;
		this.icist = icist;
		this.iciapp = iciapp;
		this.icaicu = icaicu;
		this.icuser = icuser;
		this.icdicj = icdicj;
		this.icndo = icndo;
		this.icbal = icbal;
		this.icbalj = icbalj;
		this.icame = icame;
		this.icdocn = icdocn;
		this.icausr = icausr;
		this.icpob = icpob;
		this.iciboi = iciboi;
		this.icaipt = icaipt;
		this.icoffp = icoffp;
		this.icpid = icpid;
		this.icjobn = icjobn;
		this.icupmj = icupmj;
		this.icupmt = icupmt;
		this.icdrsp = icdrsp;
		this.ic52pp = ic52pp;
		this.iccbp = iccbp;
	}
	public String getIcicut() {
		return icicut;
	}
	public void setIcicut(String icicut) {
		this.icicut = icicut;
	}
	public String getIcicu() {
		return icicu;
	}
	public void setIcicu(String icicu) {
		this.icicu = icicu;
	}
	public String getIcist() {
		if( icist == null )
			icist = "" ;
		return icist;
	}
	public void setIcist(String icist) {
		this.icist = icist;
	}
	public String getIciapp() {
		return iciapp;
	}
	public void setIciapp(String iciapp) {
		this.iciapp = iciapp;
	}
	public String getIcaicu() {
		return icaicu;
	}
	public void setIcaicu(String icaicu) {
		this.icaicu = icaicu;
	}
	public String getIcuser() {
		return icuser;
	}
	public void setIcuser(String icuser) {
		this.icuser = icuser;
	}
	public String getIcdicj() {
		return icdicj;
	}
	public void setIcdicj(String icdicj) {
		this.icdicj = icdicj;
	}
	public String getIcndo() {
		return icndo;
	}
	public void setIcndo(String icndo) {
		this.icndo = icndo;
	}
	public String getIcbal() {
		return icbal;
	}
	public void setIcbal(String icbal) {
		this.icbal = icbal;
	}
	public String getIcbalj() {
		return icbalj;
	}
	public void setIcbalj(String icbalj) {
		this.icbalj = icbalj;
	}
	public String getIcame() {
		return icame;
	}
	public void setIcame(String icame) {
		this.icame = icame;
	}
	public String getIcdocn() {
		return icdocn;
	}
	public void setIcdocn(String icdocn) {
		this.icdocn = icdocn;
	}
	public String getIcausr() {
		return icausr;
	}
	public void setIcausr(String icausr) {
		this.icausr = icausr;
	}
	public String getIcpob() {
		return icpob;
	}
	public void setIcpob(String icpob) {
		this.icpob = icpob;
	}
	public String getIciboi() {
		return iciboi;
	}
	public void setIciboi(String iciboi) {
		this.iciboi = iciboi;
	}
	public String getIcaipt() {
		return icaipt;
	}
	public void setIcaipt(String icaipt) {
		this.icaipt = icaipt;
	}
	public String getIcoffp() {
		return icoffp;
	}
	public void setIcoffp(String icoffp) {
		this.icoffp = icoffp;
	}
	public String getIcpid() {
		return icpid;
	}
	public void setIcpid(String icpid) {
		this.icpid = icpid;
	}
	public String getIcjobn() {
		
		if( icjobn == null ) {
			icjobn = PropertiesSystem.ESQUEMA;
		}
		
		return icjobn;
	}
	public void setIcjobn(String icjobn) {
		this.icjobn = icjobn;
	}
	public String getIcupmj() {
		return icupmj;
	}
	public void setIcupmj(String icupmj) {
		this.icupmj = icupmj;
	}
	public String getIcupmt() {
		return icupmt;
	}
	public void setIcupmt(String icupmt) {
		this.icupmt = icupmt;
	}
	public String getIcdrsp() {
		return icdrsp;
	}
	public void setIcdrsp(String icdrsp) {
		this.icdrsp = icdrsp;
	}
	public String getIc52pp() {
		return ic52pp;
	}
	public void setIc52pp(String ic52pp) {
		this.ic52pp = ic52pp;
	}
	public String getIccbp() {
		return iccbp;
	}
	public void setIccbp(String iccbp) {
		this.iccbp = iccbp;
	}
	
}

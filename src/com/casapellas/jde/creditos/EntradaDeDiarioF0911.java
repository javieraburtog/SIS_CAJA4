package com.casapellas.jde.creditos;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.casapellas.util.CodeUtil;
import com.casapellas.util.PropertiesSystem;

public class EntradaDeDiarioF0911 {

	private String glkco;
	private String gldct;
	private String gldoc;
	private String gldgj;
	private String gljeln;
	private String glicu;
	private String glicut;
	private String gldicj;
	private String gldsyj;
	private String glticu;
	private String glco;
	private String glani;
	private String glam;
	private String glaid;
	private String glmcu;
	private String globj;
	private String glsub;
	private String glsbl;
	private String glsblt;
	private String gllt;
	private String glpn;
	private String glctry;
	private String glfy;
	private String glcrcd;
	private String glaa;
	private String glexa;
	private String gldkj;
	private String gldsvj;
	private String gltorg;
	private String gluser;
	private String glpid;
	private String gljobn;
	private String glupmj;
	private String glupmt;
	private String glcrr;
	private String glglc;
	private String glexr;
	private String glbcrc;
	private String glcrrm;
	
	private String glpo;
	private String gldcto;
	private String glvinv;
	private String glr1;
	private String glr2;
	private String gllnid;
	private String glan8;
	private String glacr; 
	
	private String glivd;
	
	private Date fecharecibo;
	private int numerocuota;
	private String linenumber;
	private String montoExtranjero;
	
	public String insertStatement(){
		String sqlInsert = "";
		
		try {
			
			sqlInsert = "insert into @JDEDTA.F0911 ( @FIELDS_TO_INSERT ) VALUES (@VALUES_TO_INSERT) " ;
			
			String insertFields = DefaultJdeFieldsValues.F0911_INSERT_COLUMNS;
			String insertValues = DefaultJdeFieldsValues.F0911_INSERT_COLUMNS_VALUES;
			
			insertFields += ", " +  DefaultJdeFieldsValues.F0911_COLUMN_NAMES_DEFAULT;
			insertValues += ", " +  DefaultJdeFieldsValues.F0911_COLUMN_NAMES_DEFAULT_VALUES;
			
			
			glmcu = CodeUtil.pad(glmcu.trim(), 12, " ");
			glkco = CodeUtil.pad(glkco.trim(), 5, "0");
			glco = CodeUtil.pad(glco.trim(), 5, "0");
			
			if (!glsbl.trim().isEmpty()) {

				if (UtilsAmount.numberInString(glsbl.trim())) {
					glsbl = CodeUtil.pad(glsbl.trim(), 8, "0");
				} else {
					glsbl = CodeUtil.pad(glsbl.trim(), 8, " ");
				}
			}
			
			
			gldicj = gldgj;
			gldsyj = gldgj;
			gldkj  = gldgj;
			glticu = new SimpleDateFormat("HHmmss").format(fecharecibo);
			
			gldsvj = gldgj;
			glupmj = gldgj;
			glupmt = glticu ;

			gluser = gltorg;			
			glpid = PropertiesSystem.CONTEXT_NAME;
			gljobn = PropertiesSystem.CONTEXT_NAME;
			
			
			glani = ( glmcu +"." +globj.trim()+"."+glsub.trim());
			
			if(glani.endsWith("."))
				glani = glani.substring(0, glani.lastIndexOf("."));
			
			glctry = new SimpleDateFormat("yyyy").format(fecharecibo).substring(0,2);
			glfy   = new SimpleDateFormat("yyyy").format(fecharecibo).substring(2,4);
			glpn   = new SimpleDateFormat("MM").format(fecharecibo);
			
			glam = "2";
			
			glcrrm = (glbcrc.compareTo(glcrcd) == 0) ? "D" :"F";
			
			gllnid = String.valueOf( Integer.parseInt(linenumber) * 1000 );
			
			if( glpo != null  && !glpo.isEmpty() ){
				glpo = CodeUtil.pad(glpo.trim(), 8, "0");
				glr1 = glpo;
				glr2 =  "MCAJA" ;  
				glvinv = gldoc;
				gljeln = String.valueOf( Integer.parseInt(linenumber) * numerocuota );
			}else{
				glpo = "";
				gldcto = "";
				glvinv = "";
				glr1 = "";
				glr2 = "";
			}
			
			if(glglc == null)
				glglc = "" ;
			if(glan8 == null)
				glan8 = "0";
			
			
			if(  glbcrc.compareTo(glcrcd) != 0 && gllt.compareTo("AA") == 0  ){
				montoExtranjero = ( montoExtranjero== null || montoExtranjero.isEmpty() ) ? "0" : montoExtranjero;
				glacr = montoExtranjero;
			}else{
				glacr =  "0" ;
			}
			
			if(glexa.trim().length() > 30 ){
				glexa = glexa.trim().substring(0, 29);
			}
			
			if( glivd == null)
				glivd = "0";
			
			if(glexr.trim().length() > 30) {
				glexr = glexr.trim().substring(0, 29);
			}
			
			insertValues = insertValues
				.replace("@GLKCO@", glkco)
				.replace("@GLDCT@", gldct)
				.replace("@GLDOC@", gldoc)
				.replace("@GLDGJ@", gldgj)
				.replace("@GLJELN@",gljeln )
				.replace("@GLICU@", glicu)
				.replace("@GLICUT@", glicut)
				.replace("@GLDICJ@", gldicj )
				.replace("@GLDSYJ@", gldsyj)
				.replace("@GLTICU@", glticu)
				.replace("@GLCO@", glco)
				.replace("@GLANI@", glani)
				.replace("@GLAM@", glam)
				.replace("@GLAID@", glaid )
				.replace("@GLMCU@", glmcu )
				.replace("@GLOBJ@", globj)
				.replace("@GLSUB@", glsub)
				.replace("@GLSBL@", glsbl)
				.replace("@GLSBLT@", glsblt)
				.replace("@GLLT@", gllt)
				.replace("@GLPN@", glpn)
				.replace("@GLCTRY@", glctry)
				.replace("@GLFY@", glfy)
				.replace("@GLCRCD@", glcrcd)
				.replace("@GLAA@", glaa)
				.replace("@GLEXA@", glexa)
				.replace("@GLDKJ@", gldkj)
				.replace("@GLDSVJ@", gldsvj)
				.replace("@GLTORG@", gltorg)
				.replace("@GLUSER@", gluser)
				.replace("@GLPID@", glpid)
				.replace("@GLJOBN@", gljobn)
				.replace("@GLUPMJ@", glupmj)
				.replace("@GLUPMT@", glupmt)
				.replace("@GLCRR@", glcrr)
				.replace("@GLEXR@", glexr)
				.replace("@GLBCRC@", glbcrc)
				.replace("@GLCRRM@", glcrrm ) 
				
				.replace("@GLPO@",  glpo )
				.replace("@GLDCTO@", gldcto)
				.replace("@GLVINV@", glvinv )
				.replace("@GLR1@", glr1) 
				.replace("@GLR2@", glr2)
				.replace("@GLLNID@", gllnid)
				.replace("@GLGLC@", glglc)
				.replace("@GLAN8@", glan8)
				.replace("@GLACR@", glacr)
				.replace("@GLIVD@", glivd)
				
				
				;
				
			return 
			sqlInsert
				.replace("@JDEDTA", PropertiesSystem.JDEDTA)
				.replace("@FIELDS_TO_INSERT", insertFields)
				.replace("@VALUES_TO_INSERT", insertValues);
			
			
		} catch (Exception e) {
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
		}
		
		return sqlInsert;
	}
	
	
	
	public EntradaDeDiarioF0911() {
		super();
	}
	
	//&& ======== constructor usado en generacion de intereses corrientes y moratorios.
	public EntradaDeDiarioF0911(String glkco, String gldct, String gldoc,
			String gldgj, String glicu, String glicut, String glco,
			String glaid, String glmcu, String globj, String glsub,
			String glsbl, String glsblt, String glcrcd, String glaa,
			String glexa, String gltorg, String glcrr, String glexr,
			String glbcrc, String gljeln, String gllt, Date fecharecibo,
			String glglc, String glpo, String gldcto, String glvinv,
			String glr1, String glr2, int numerocuota ) {

		super();
		this.glkco = glkco;
		this.gldct = gldct;
		this.gldoc = gldoc;
		this.gldgj = gldgj;
		this.glicu = glicu;
		this.glicut = glicut;
		this.glco = glco;
		this.glaid = glaid;
		this.glmcu = glmcu;
		this.globj = globj;
		this.glsub = glsub;
		this.glsbl = glsbl;
		this.glsblt = glsblt;
		this.glcrcd = glcrcd;
		this.glaa = glaa;
		this.glexa = glexa;
		this.gltorg = gltorg;
		this.glcrr = glcrr;
		this.glexr = glexr;
		this.glbcrc = glbcrc;
		this.gljeln = gljeln ;
		this.gllt = gllt;
		this.fecharecibo = fecharecibo;
		
		this.glglc = glglc;
		this.glpo = glpo;
		this.gldcto = gldcto;
		this.glvinv = glvinv;
		this.glr1 = glr1;
		this.glr2 = glr2;
		this.numerocuota  = numerocuota ;

	}
	
	
	
	//&& ========= constructor usado en pago de facturas de  credito
	public EntradaDeDiarioF0911(String glkco, String gldct, String gldoc,
			String gldgj, String glicu, String glicut,
			String glco, String glaid,
			String glmcu, String globj, String glsub, String glsbl,
			String glsblt, String glcrcd, String glaa, String glexa,
			String gltorg, String glcrr, String glexr,
			String glbcrc, String gljeln, String gllt, Date fecharecibo ) {

		super();
		this.glkco = glkco;
		this.gldct = gldct;
		this.gldoc = gldoc;
		this.gldgj = gldgj;
		this.glicu = glicu;
		this.glicut = glicut;
		this.glco = glco;
		this.glaid = glaid;
		this.glmcu = glmcu;
		this.globj = globj;
		this.glsub = glsub;
		this.glsbl = glsbl;
		this.glsblt = glsblt;
		this.glcrcd = glcrcd;
		this.glaa = glaa;
		this.glexa = glexa;
		this.gltorg = gltorg;
		this.glcrr = glcrr;
		this.glexr = glexr;
		this.glbcrc = glbcrc;
		this.gljeln = gljeln ;
		this.gllt = gllt;
		this.fecharecibo = fecharecibo;
		linenumber = gljeln;
	}

	public EntradaDeDiarioF0911(String glkco, String gldct, String gldoc,
			String gldgj, String gljeln, String glicu, String glicut,
			String gldicj, String gldsyj, String glticu, String glco,
			String glani, String glam, String glaid, String glmcu,
			String globj, String glsub, String glsbl, String glsblt,
			String gllt, String glpn, String glctry, String glfy,
			String glcrcd, String glaa, String glexa, String gldkj,
			String gldsvj, String gltorg, String gluser, String glpid,
			String gljobn, String glupmj, String glupmt, String glcrr,
			String glglc, String glexr, String glbcrc, String glhco,
			String glcrrm) {
		
		super();
		this.glkco = glkco;
		this.gldct = gldct;
		this.gldoc = gldoc;
		this.gldgj = gldgj;
		this.gljeln = gljeln;
		this.glicu = glicu;
		this.glicut = glicut;
		this.gldicj = gldicj;
		this.gldsyj = gldsyj;
		this.glticu = glticu;
		this.glco = glco;
		this.glani = glani;
		this.glam = glam;
		this.glaid = glaid;
		this.glmcu = glmcu;
		this.globj = globj;
		this.glsub = glsub;
		this.glsbl = glsbl;
		this.glsblt = glsblt;
		this.gllt = gllt;
		this.glpn = glpn;
		this.glctry = glctry;
		this.glfy = glfy;
		this.glcrcd = glcrcd;
		this.glaa = glaa;
		this.glexa = glexa;
		this.gldkj = gldkj;
		this.gldsvj = gldsvj;
		this.gltorg = gltorg;
		this.gluser = gluser;
		this.glpid = glpid;
		this.gljobn = gljobn;
		this.glupmj = glupmj;
		this.glupmt = glupmt;
		this.glcrr = glcrr;
		this.glglc = glglc;
		this.glexr = glexr;
		this.glbcrc = glbcrc;
		this.glcrrm = glcrrm;
	}
	
	public String getGlkco() {
		return glkco;
	}
	public void setGlkco(String glkco) {
		this.glkco = glkco;
	}
	public String getGldct() {
		return gldct;
	}
	public void setGldct(String gldct) {
		this.gldct = gldct;
	}
	public String getGldoc() {
		return gldoc;
	}
	public void setGldoc(String gldoc) {
		this.gldoc = gldoc;
	}
	public String getGldgj() {
		return gldgj;
	}
	public void setGldgj(String gldgj) {
		this.gldgj = gldgj;
	}
	public String getGljeln() {
		return gljeln;
	}
	public void setGljeln(String gljeln) {
		this.gljeln = gljeln;
	}
	public String getGlicu() {
		return glicu;
	}
	public void setGlicu(String glicu) {
		this.glicu = glicu;
	}
	public String getGlicut() {
		return glicut;
	}
	public void setGlicut(String glicut) {
		this.glicut = glicut;
	}
	public String getGldicj() {
		return gldicj;
	}
	public void setGldicj(String gldicj) {
		this.gldicj = gldicj;
	}
	public String getGldsyj() {
		return gldsyj;
	}
	public void setGldsyj(String gldsyj) {
		this.gldsyj = gldsyj;
	}
	public String getGlticu() {
		return glticu;
	}
	public void setGlticu(String glticu) {
		this.glticu = glticu;
	}
	public String getGlco() {
		return glco;
	}
	public void setGlco(String glco) {
		this.glco = glco;
	}
	public String getGlani() {
		return glani;
	}
	public void setGlani(String glani) {
		this.glani = glani;
	}
	public String getGlam() {
		return glam;
	}
	public void setGlam(String glam) {
		this.glam = glam;
	}
	public String getGlaid() {
		return glaid;
	}
	public void setGlaid(String glaid) {
		this.glaid = glaid;
	}
	public String getGlmcu() {
		return glmcu;
	}
	public void setGlmcu(String glmcu) {
		this.glmcu = glmcu;
	}
	public String getGlobj() {
		return globj;
	}
	public void setGlobj(String globj) {
		this.globj = globj;
	}
	public String getGlsub() {
		return glsub;
	}
	public void setGlsub(String glsub) {
		this.glsub = glsub;
	}
	public String getGlsbl() {
		return glsbl;
	}
	public void setGlsbl(String glsbl) {
		this.glsbl = glsbl;
	}
	public String getGlsblt() {
		return glsblt;
	}
	public void setGlsblt(String glsblt) {
		this.glsblt = glsblt;
	}
	public String getGllt() {
		return gllt;
	}
	public void setGllt(String gllt) {
		this.gllt = gllt;
	}
	public String getGlpn() {
		return glpn;
	}
	public void setGlpn(String glpn) {
		this.glpn = glpn;
	}
	public String getGlctry() {
		return glctry;
	}
	public void setGlctry(String glctry) {
		this.glctry = glctry;
	}
	public String getGlfy() {
		return glfy;
	}
	public void setGlfy(String glfy) {
		this.glfy = glfy;
	}
	public String getGlcrcd() {
		return glcrcd;
	}
	public void setGlcrcd(String glcrcd) {
		this.glcrcd = glcrcd;
	}
	public String getGlaa() {
		return glaa;
	}
	public void setGlaa(String glaa) {
		this.glaa = glaa;
	}
	public String getGlexa() {
		return glexa;
	}
	public void setGlexa(String glexa) {
		this.glexa = glexa;
	}
	public String getGldkj() {
		return gldkj;
	}
	public void setGldkj(String gldkj) {
		this.gldkj = gldkj;
	}
	public String getGldsvj() {
		return gldsvj;
	}
	public void setGldsvj(String gldsvj) {
		this.gldsvj = gldsvj;
	}
	public String getGltorg() {
		return gltorg;
	}
	public void setGltorg(String gltorg) {
		this.gltorg = gltorg;
	}
	public String getGluser() {
		return gluser;
	}
	public void setGluser(String gluser) {
		this.gluser = gluser;
	}
	public String getGlpid() {
		return glpid;
	}
	public void setGlpid(String glpid) {
		this.glpid = glpid;
	}
	public String getGljobn() {
		return gljobn;
	}
	public void setGljobn(String gljobn) {
		this.gljobn = gljobn;
	}
	public String getGlupmj() {
		return glupmj;
	}
	public void setGlupmj(String glupmj) {
		this.glupmj = glupmj;
	}
	public String getGlupmt() {
		return glupmt;
	}
	public void setGlupmt(String glupmt) {
		this.glupmt = glupmt;
	}
	public String getGlcrr() {
		return glcrr;
	}
	public void setGlcrr(String glcrr) {
		this.glcrr = glcrr;
	}
	public String getGlglc() {
		return glglc;
	}
	public void setGlglc(String glglc) {
		this.glglc = glglc;
	}
	public String getGlexr() {
		return glexr;
	}
	public void setGlexr(String glexr) {
		this.glexr = glexr;
	}
	public String getGlbcrc() {
		return glbcrc;
	}
	public void setGlbcrc(String glbcrc) {
		this.glbcrc = glbcrc;
	}
	public String getGlcrrm() {
		return glcrrm;
	}
	public void setGlcrrm(String glcrrm) {
		this.glcrrm = glcrrm;
	}

	public String getGlpo() {
		return glpo;
	}

	public void setGlpo(String glpo) {
		this.glpo = glpo;
	}

	public String getGldcto() {
		return gldcto;
	}

	public void setGldcto(String gldcto) {
		this.gldcto = gldcto;
	}

	public String getGlvinv() {
		return glvinv;
	}

	public void setGlvinv(String glvinv) {
		this.glvinv = glvinv;
	}

	public String getGlr1() {
		return glr1;
	}

	public void setGlr1(String glr1) {
		this.glr1 = glr1;
	}

	public String getGlr2() {
		return glr2;
	}

	public void setGlr2(String glr2) {
		this.glr2 = glr2;
	}

	public int getNumerocuota() {
		return numerocuota;
	}
	public void setNumerocuota(int numerocuota) {
		this.numerocuota = numerocuota;
	}
	
	public String getGlan8() {
		return glan8;
	}

	public void setGlan8(String glan8) {
		this.glan8 = glan8;
	}

	public String getMontoExtranjero() {
		return montoExtranjero;
	}

	public void setMontoExtranjero(String montoExtranjero) {
		this.montoExtranjero = montoExtranjero;
	}

	public String getGlivd() {
		return glivd;
	}

	public void setGlivd(String glivd) {
		this.glivd = glivd;
	}
	
}

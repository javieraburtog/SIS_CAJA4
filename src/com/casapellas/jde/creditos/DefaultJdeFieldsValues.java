package com.casapellas.jde.creditos;

import java.util.ArrayList;
import java.util.List;

import com.casapellas.controles.ConsolidadoDepositosBcoCtrl;
import com.casapellas.util.PropertiesSystem;

enum TABLES_JDE {
	F03B11,
	F03B13,
	F03B14,
	F0011,
	F0911,
	F0411
}

public class DefaultJdeFieldsValues {

	
	public static String F0911_CONTADO_INSERT_COLUMNS =
		" GLKCO,GLDCT,GLDOC,GLDGJ,GLJELN,GLICU,GLICUT,GLDICJ," + 
		" GLDSYJ,GLTICU,GLCO,GLANI,GLAM,GLAID, GLMCU,GLOBJ, GLSUB, " +
		" GLSBL,GLSBLT, GLLT,GLPN, GLCTRY,GLFY,GLCRCD,GLAA,GLEXA,GLDKJ," +
		" GLDSVJ,GLTORG,GLUSER,GLPID,GLJOBN,GLUPMJ,GLUPMT, " +
		" GLCRR,GLGLC,GLEXR,GLBCRC, GLCRRM, GLACR , GLREG#," +
		" GLAN8, GLVINV, GLIVD, GLPKCO  ";
	
	public static String F0911_CONTADO_INSERT_COLUMNS_VALUES =
		" '@GLKCO@', '@GLDCT@', '@GLDOC@', '@GLDGJ@', '@GLJELN@', '@GLICU@', '@GLICUT@', '@GLDICJ@', " +
		" '@GLDSYJ@', '@GLTICU@', '@GLCO@', '@GLANI@', '@GLAM@', '@GLAID@', '@GLMCU@', '@GLOBJ@', '@GLSUB@', " +
		" '@GLSBL@', '@GLSBLT@', '@GLLT@', '@GLPN@', '@GLCTRY@', '@GLFY@', '@GLCRCD@', '@GLAA@', '@GLEXA@', '@GLDKJ@', " +
		" '@GLDSVJ@', '@GLTORG@', '@GLUSER@', '@GLPID@', '@GLJOBN@', '@GLUPMJ@', '@GLUPMT@', " +
		" '@GLCRR@', '@GLGLC@', '@GLEXR@', '@GLBCRC@', '@GLCRRM@', '@GLACR@',  '0'," +
		" '@GLAN8@', '@GLVINV@', '@GLIVD@', '@GLPKCO@' ";
	
	public static String F0911_CONTADO_COLUMN_NAMES_DEFAULT ;
	public static String F0911_CONTADO_COLUMN_NAMES_DEFAULT_VALUES ;

	
	public static String F0911_COLUMN_NAMES_DEFAULT ;
	public static String F0911_COLUMN_NAMES_DEFAULT_VALUES ;
	public static String F0911_INSERT_COLUMNS =
			" GLKCO,GLDCT,GLDOC,GLDGJ,GLJELN,GLICU," +
			" GLICUT,GLDICJ, GLDSYJ,GLTICU,GLCO,GLANI," +
			" GLAM,GLAID,GLMCU,GLOBJ,GLSUB, GLSBL," +
			" GLSBLT, GLLT, GLPN, GLCTRY,GLFY,GLCRCD," +
			" GLAA,GLEXA,GLDKJ,GLDSVJ,GLTORG,GLUSER, " +
			" GLPID,GLJOBN,GLUPMJ,GLUPMT, GLCRR, " +
			" GLEXR,GLBCRC,GLCRRM, GLREG#,  " +
			" GLPO, GLDCTO, GLVINV, GLR1, GLR2, GLLNID," +
			" GLGLC, GLAN8, GLACR, GLIVD " ;
	 
	public static String F0911_INSERT_COLUMNS_VALUES = 
			" '@GLKCO@', '@GLDCT@','@GLDOC@','@GLDGJ@','@GLJELN@','@GLICU@'," +
			" '@GLICUT@','@GLDICJ@','@GLDSYJ@','@GLTICU@','@GLCO@','@GLANI@'," +
			" '@GLAM@','@GLAID@','@GLMCU@','@GLOBJ@','@GLSUB@','@GLSBL@'," +
			" '@GLSBLT@','@GLLT@','@GLPN@','@GLCTRY@','@GLFY@','@GLCRCD@'," +
			" '@GLAA@','@GLEXA@','@GLDKJ@','@GLDSVJ@','@GLTORG@','@GLUSER@'," +
			" '@GLPID@','@GLJOBN@','@GLUPMJ@','@GLUPMT@','@GLCRR@'," +
			" '@GLEXR@','@GLBCRC@','@GLCRRM@', '0', " + 
			" '@GLPO@', '@GLDCTO@', '@GLVINV@', '@GLR1@', '@GLR2@', '@GLLNID@'," +
			" '@GLGLC@', '@GLAN8@', '@GLACR@', '@GLIVD@'" ;

	public static String F0011_COLUMN_NAMES_DEFAULT ;
	public static String F0011_COLUMN_NAMES_DEFAULT_VALUES ;
	public static String F0011_INSERT_COLUMNS =
			" ICICUT, ICICU, ICIST, ICIAPP, ICAICU, ICUSER, " +
			" ICDICJ, ICBAL, ICBALJ, ICAME, ICDOCN, " +
			" ICPOB, ICAIPT, ICPID, ICJOBN, ICUPMJ, ICUPMT" ;
	public static String F0011_INSERT_COLUMNS_VALUES =
			" '@ICICUT', '@ICICU', '@ICIST', '@ICIAPP', '@ICAICU', '@ICUSER', " +
			" '@ICDICJ', '@ICBAL', '@ICBALJ', '@ICAME',  '@ICDOCN', " +
			" '@ICPOB',  '@ICAIPT', '@ICPID', '@ICJOBN', '@ICUPMJ', '@ICUPMT'  " ;
	
	
	public static String F03B13_COLUMN_NAMES_DEFAULT ;
	public static String F03B13_COLUMN_NAMES_DEFAULT_VALUES ;
	public static String F03B13_INSERT_COLUMNS_CR =
			" RYPYID, RYCKNU, RYAN8, RYPYR, RYDMTJ, RYDGJ, " +
			" RYPOST, RYEULP, RYCTRY, RYFY, RYPN, RYCO, " +
			" RYICUT, RYICU, RYDICJ, RYPA8, RYCKAM, RYAAP," +
			" RYBCRC, RYCRRM, RYCRCD, RYCRR, RYERDJ, RYCRCM," +
			" RYFCAM, RYFAP, RYGLBA, RYAM, RYTYIN, RYEXR, " +
			" RYALPH, RYTORG, RYUSER, RYPID, RYUPMJ, RYUPMT, " +
			" RYJOBN, RYGLC, RYAID, RYRYIN" ;
	public static String F03B13_INSERT_COLUMNS_VALUES_CR = 
			"'@RYPYID', '@RYCKNU', '@RYAN8',  '@RYPYR', '@RYDMTJ', '@RYDGJ', " +  
			"'@RYPOST', '@RYEULP', '@RYCTRY', '@RYFY',  '@RYPN',   '@RYCO', " +
			"'@RYICUT', '@RYICU',  '@RYDICJ', '@RYPA8', '@RYCKAM', '@RYAAP',  " +
			"'@RYBCRC', '@RYCRRM', '@RYCRCD', '@RYCRR', '@RYERDJ', '@RYCRCM', " +
			"'@RYFCAM', '@RYFAP',  '@RYGLBA', '@RYAM',  '@RYTYIN', '@RYEXR', " +
			"'@RYALPH', '@RYTORG', '@RYUSER', '@RYPID', '@RYUPMJ', '@RYUPMT',  " +
			"'@RYJOBN', '@RYGLC', '@RYAID', '@RYRYIN'    " ;
	
	
	public static String F03B14_COLUMN_NAMES_DEFAULT ;
	public static String F03B14_COLUMN_NAMES_DEFAULT_VALUES ;
	public static String F03B14_INSERT_COLUMNS_CR =
			" RZPYID, RZRC5, RZCKNU, RZDOC, RZDCT, RZKCO, " +
			" RZSFX, RZAN8, RZDCTM, RZDMTJ, RZDGJ, RZAID," +
			" RZCTRY, RZFY, RZPN, RZCO, RZICUT, RZICU, " +
			" RZDICJ, RZPA8, RZRPID, RZPAAP, RZBCRC, RZCRRM, " +
			" RZCRCD, RZCRR, RZPFAP, RZAGL, RZAIDT, RZTCRC," +
			" RZTAAP, RZTYIN, RZUTIC, RZMCU, RZRMK, RZHCRR, " +
			" RZPDLT, RZDDJ, RZDDNJ, RZIDGJ, RZTORG, RZUSER, " +
			" RZPID, RZUPMJ, RZUPMT, RZJOBN, RZGLC, " +
			" RZAID2, RZAM2, RZSBL, RZSBLT" ;
	
	
	public static String F03B14_INSERT_COLUMNS_VALUES_CR =  
			" '@RZPYID@', '@RZRC5@', '@RZCKNU@', '@RZDOC@', '@RZDCT@', '@RZKCO@', " +
			" '@RZSFX@', '@RZAN8@', '@RZDCTM@', '@RZDMTJ@', '@RZDGJ@', '@RZAID@', " +
			" '@RZCTRY@', '@RZFY@', '@RZPN@', '@RZCO@', '@RZICUT@', '@RZICU@', " +
			" '@RZDICJ@', '@RZPA8@', '@RZRPID@', '@RZPAAP@', '@RZBCRC@', '@RZCRRM@', " +
			" '@RZCRCD@', '@RZCRR@', '@RZPFAP@', '@RZAGL@', '@RZAIDT@', '@RZTCRC@', " +
			" '@RZTAAP@', '@RZTYIN@', '@RZUTIC@', '@RZMCU@', '@RZRMK@', '@RZHCRR@', " +
			" '@RZPDLT@', '@RZDDJ@', '@RZDDNJ@', '@RZIDGJ@', '@RZTORG@', '@RZUSER@', " +
			" '@RZPID@', '@RZUPMJ@', '@RZUPMT@', '@RZJOBN@', '@RZGLC@'," +
			" '@RZAID2@', '@RZAM2@', '@RZSBL@','@RZSBLT@'   " ;
	
	
	public static String F03B11_COLUMN_NAMES_DEFAULT ;
	public static String F03B11_COLUMN_NAMES_DEFAULT_VALUES ;
	public static String F03B11_INSERT_COLUMNS =
			"RPKCO, RPAN8, RPDCT, RPDOC, RPSFX, RPDIVJ, " +
			"RPDGJ, RPFY, RPCTRY, RPPN, RPCO, RPICUT, " +
			"RPICU, RPDICJ, RPPA8, RPAN8J, RPBALJ, RPPST, " +
			"RPAG, RPAAP, RPSTAM, RPCRRM, RPCRCD, RPCRR, " +
			"RPACR, RPFAP, RPCTAM, RPTXA1, RPEXR1, RPDSVJ, " +
			"RPGLC, RPMCU, RPDDJ, RPDDNJ, RPPO, RPDCTO, " +
			"RPRMK, RPALPH, RPTORG, RPUSER, RPPID, RPUPMJ, " +
			"RPUPMT, RPJOBN, RPPKCO, RPODOC, RPODCT, RPOSFX, " +
			"RPOKCO, RPMCU2, RPATXN, RPCTXN, RPATXA, RPCTXA," +
			"RPPYR, RPBCRC, RPATAD, RPCTAD, RPDNLT," +
			"RPCOLL, RPAFC, RPRYIN, RPOMOD, RPERDJ, RPNETST, " +
			"RPAID, RPAR08, RPPOST, RPISTC,	RPPYID " ;
	 
	public static String F03B11_INSERT_COLUMNS_VALUES = 
			"'@RPKCO@', '@RPAN8@', '@RPDCT@', '@RPDOC@', '@RPSFX@', '@RPDIVJ@', " +
			"'@RPDGJ@', '@RPFY@', '@RPCTRY@', '@RPPN@', '@RPCO@', '@RPICUT@', " +
			"'@RPICU@', '@RPDICJ@', '@RPPA8@', '@RPAN8J@', '@RPBALJ@', '@RPPST@', " +
			"'@RPAG@',  '@RPAAP@', '@RPSTAM@', '@RPCRRM@', '@RPCRCD@', '@RPCRR@', " +
			"'@RPACR@', '@RPFAP@', '@RPCTAM@', '@RPTXA1@', '@RPEXR1@', '@RPDSVJ@', " +
			"'@RPGLC@', '@RPMCU@', '@RPDDJ@', '@RPDDNJ@', '@RPPO@', '@RPDCTO@', " +
			"'@RPRMK@', '@RPALPH@', '@RPTORG@', '@RPUSER@', '@RPPID@', '@RPUPMJ@', " +
			"'@RPUPMT@','@RPJOBN@', '@RPPKCO@', '@RPODOC@', '@RPODCT@', '@RPOSFX@', " +
			"'@RPOKCO@','@RPMCU2@', '@RPATXN@', '@RPCTXN@', '@RPATXA@', '@RPCTXA@', " +
			"'@RPPYR@', '@RPBCRC@', '@RPATAD@', '@RPCTAD@', '@RPDNLT@', "  +
			"'@RPCOLL@', '@RPAFC@', '@RPRYIN@', '@RPOMOD@', '@RPERDJ@', '@RPNETST@'," +
			"'@RPAID@',  '@RPAR08@', '@RPPOST@', '@RPISTC@', '@RPPYID@'  " ;
	
	
	// 8888888888888888888888
	public static String F0411_COLUMN_NAMES_DEFAULT ;
	public static String F0411_COLUMN_NAMES_DEFAULT_VALUES ;
	public static String F0411_INSERT_COLUMNS =
			"RPKCO, RPDOC, RPDCT, RPSFX, RPAN8, RPPYE, RPDIVJ, RPDSVJ, "+
		    "RPDDJ, RPDDNJ, RPDGJ, RPFY, RPCTRY, RPPN, RPCO, RPICU, "+
		    "RPICUT, RPDICJ, RPBALJ, RPPST, RPAG, RPAAP, RPATXN, "+
		    "RPTXA1, RPEXR1, RPCRRM, RPCRCD, RPCRR, RPACR, RPFAP, "+
		    "RPCTXN, RPGLC, RPGLBA, RPAM, RPMCU, RPOBJ, RPSUB, RPVINV, RPPYIN, "+
		    "RPAC07, RPBCRC, RPYC01, RPYC02, RPYC03, RPYC04, RPYC05, RPYC06, RPYC07, RPYC08, RPYC09, RPYC10, " +
		    "RPTORG, RPUSER, RPPID, RPUPMJ, RPUPMT, RPJOBN, RPRMK, RPPO, RPPDCT ";

	public static String F0411_INSERT_COLUMNS_VALUES = 
			"'@RPKCO@', '@RPDOC@', '@RPDCT@', '@RPSFX@', '@RPAN8@', '@RPPYE@', '@RPDIVJ@', '@RPDSVJ@', " +
			"'@RPDDJ@', '@RPDDNJ@', '@RPDGJ@', '@RPFY@', '@RPCTRY@', '@RPPN@', '@RPCO@', '@RPICU@', " +
			"'@RPICUT@', '@RPDICJ@', '@RPBALJ@', '@RPPST@', '@RPAG@', '@RPAAP@', '@RPATXN@', " +
			"'@RPTXA1@', '@RPEXR1@', '@RPCRRM@', '@RPCRCD@', '@RPCRR@', '@RPACR@', '@RPFAP@', " +
			"'@RPCTXN@', '@RPGLC@', '@RPGLBA@', '@RPAM@', '@RPMCU@', '@RPOBJ@', '@RPSUB@', '@RPVINV@', '@RPPYIN@', " +
			"'@RPAC07@', '@RPBCRC@', '@RPYC01@', '@RPYC02@', '@RPYC03@', '@RPYC04@', '@RPYC05@', '@RPYC06@', '@RPYC07@', '@RPYC08@', '@RPYC09@', '@RPYC10@', " +
			"'@RPTORG@', '@RPUSER@', '@RPPID@', '@RPUPMJ@', '@RPUPMT@', '@RPJOBN@', '@RPRMK@', '@RPPO@', '@RPPDCT@' ";
	
	static {
		try {
			
			boolean nuevaConexion = true;
			ConsolidadoDepositosBcoCtrl.getCurrentSession();
			nuevaConexion = ConsolidadoDepositosBcoCtrl.isNewSession;
			
			List<String> defaultValues = sqlDefaultValues(F03B13_INSERT_COLUMNS_CR, TABLES_JDE.F03B13);
			F03B13_COLUMN_NAMES_DEFAULT = defaultValues.get(0);
			F03B13_COLUMN_NAMES_DEFAULT_VALUES = defaultValues.get(1);
			
			defaultValues = sqlDefaultValues(F03B14_INSERT_COLUMNS_CR, TABLES_JDE.F03B14);
			F03B14_COLUMN_NAMES_DEFAULT = defaultValues.get(0);
			F03B14_COLUMN_NAMES_DEFAULT_VALUES = defaultValues.get(1);
			
			defaultValues = sqlDefaultValues(F0011_INSERT_COLUMNS, TABLES_JDE.F0011);
			F0011_COLUMN_NAMES_DEFAULT = defaultValues.get(0);
			F0011_COLUMN_NAMES_DEFAULT_VALUES = defaultValues.get(1);
			
			defaultValues = sqlDefaultValues(F0911_INSERT_COLUMNS, TABLES_JDE.F0911);
			F0911_COLUMN_NAMES_DEFAULT = defaultValues.get(0);
			F0911_COLUMN_NAMES_DEFAULT_VALUES = defaultValues.get(1);
			
			defaultValues = sqlDefaultValues(F03B11_INSERT_COLUMNS, TABLES_JDE.F03B11);
			F03B11_COLUMN_NAMES_DEFAULT = defaultValues.get(0);
			F03B11_COLUMN_NAMES_DEFAULT_VALUES = defaultValues.get(1);
			
			defaultValues = sqlDefaultValues(F0911_CONTADO_INSERT_COLUMNS, TABLES_JDE.F0911);
			F0911_CONTADO_COLUMN_NAMES_DEFAULT = defaultValues.get(0);
			F0911_CONTADO_COLUMN_NAMES_DEFAULT_VALUES = defaultValues.get(1);
			
			defaultValues = sqlDefaultValues(F0411_INSERT_COLUMNS, TABLES_JDE.F0411);
			F0411_COLUMN_NAMES_DEFAULT = defaultValues.get(0);
			F0411_COLUMN_NAMES_DEFAULT_VALUES = defaultValues.get(1);
			
			ConsolidadoDepositosBcoCtrl.isNewSession = nuevaConexion;
			ConsolidadoDepositosBcoCtrl.closeCurrentSession(true);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static List<String> sqlDefaultValues(String fieldToExclude, TABLES_JDE table){
		List<String> defaultValues  = new ArrayList<String>();
		
		try {
			
			String query = 
			"select "+
			" c.column_name, "+
			" (CASE WHEN ( c.data_type ='DECIMAL' OR c.data_type ='NUMERIC') THEN '0' else  '''''' end ) defaultvalue "+
			" from sysibm.tables t join sysibm.columns c on t.table_schema = c.table_schema and t.table_name = c.table_name"+
			" where t.table_schema = '"+PropertiesSystem.JDEDTA+"' and t.table_name = '"+ table +"' ";
		
			if( !fieldToExclude.isEmpty() ){
				
				String notInValue = "";
				String[] campos = fieldToExclude.split(",");
				for (String campo : campos) {
					notInValue += "'"+campo.trim()+"', ";
				}
				notInValue = notInValue.substring(0,  notInValue.lastIndexOf(","));

				query += " and column_name not in (" +notInValue+ ") " ;
			}
			
			@SuppressWarnings("unchecked")
			List<Object[]> dataValues = (ArrayList<Object[]>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, true, null);
			
			if(dataValues == null || dataValues.isEmpty() ){
				return null;
			}
			
			String fieldname  = "" ;
			String fieldValue = "" ;
			for (Object[] dtaValues : dataValues) {
				
				if( String.valueOf( dtaValues[0] ).toLowerCase().contains("ñ") || String.valueOf( dtaValues[0] ).toLowerCase().contains("#") ){
					continue;
				} 
				
				fieldname  +=  String.valueOf( dtaValues[0] ) + ", "; 
				fieldValue +=  String.valueOf( dtaValues[1] ) + ", "; 
			}
			
			fieldname  = fieldname.substring(0, fieldname.lastIndexOf(",") );
			fieldValue = fieldValue.substring(0, fieldValue.lastIndexOf(", ") );
			
			defaultValues.add(0, fieldname);
			defaultValues.add(1, fieldValue);
				
		} catch (Exception e) {
			e.printStackTrace() ;
			return null;
		}
		return defaultValues;
	}
	
	
}

package com.casapellas.util;

import java.util.List;

import com.casapellas.controles.ConsolidadoDepositosBcoCtrl;

public class DocumuentosTransaccionales {
	
	public static CatalogoGenerico gettipoTRX() {
		
	String query="SELECT TVALALF AS VALOR, TDESC AS DESCRIPCION FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroCaja + "' AND TCOD = '"+ PropertiesSystem.tipoTrxContado +"' ";     
		
		List<CatalogoGenerico> tipoTRX= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, CatalogoGenerico.class, true);
		
		if(tipoTRX.size()>0)
		return tipoTRX.get(0);
		else
		return null;
	}
	
	public static String getValuesJDEContado() {
		
		String query="SELECT TVALALF FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroCaja + "' AND TCOD = '"+ PropertiesSystem.valuesJDEContado +"' ";     
			
			List<Object> tipoTRX= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
			
			if(tipoTRX.size()>0)
			return tipoTRX.get(0).toString();
			else
			return null;
		}
	public static String getValuesJDENumeracion() {
		
		String query="SELECT TVALALF FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroCaja + "' AND TCOD = '"+ PropertiesSystem.valuesJDENumeracion +"' ";     
			
			List<Object> tipoTRX= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
			
			if(tipoTRX.size()>0)
			return tipoTRX.get(0).toString();
			else
			return null;
		}
	public static String getValuesJDEDevolucionContado() {
		
		String query="SELECT TVALALF FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroCaja + "' AND TCOD = '"+ PropertiesSystem.valuesJDEDevolucionContado +"' ";     
			
			List<Object> tipoTRX= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
			
			if(tipoTRX.size()>0)
			return tipoTRX.get(0).toString();
			else
			return null;
		}
	public static String valoresJDEInsCredito() {
		
		String query="SELECT TVALALF FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroCaja + "' AND TCOD = '"+ PropertiesSystem.valuesJDECredito +"' ";     
			
			List<Object> tipoTRX= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
			
			if(tipoTRX.size()>0)
			return tipoTRX.get(0).toString();
			else
			return null;
		}
	public static String valoresJDEInsPrimaReservas() {
		
		String query="SELECT TVALALF FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroCaja + "' AND TCOD = '"+ PropertiesSystem.valuesJDEPrimaReserva +"' ";     
			
			List<Object> tipoTRX= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
			
			if(tipoTRX.size()>0)
			return tipoTRX.get(0).toString();
			else
			return null;
		}
	
	public static String valoresJDEInsPMT() {
		
		String query="SELECT TVALALF FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroCaja + "' AND TCOD = '"+ PropertiesSystem.valuesJDEPMT +"' ";     
			
			List<Object> tipoTRX= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
			
			if(tipoTRX.size()>0)
			return tipoTRX.get(0).toString();
			else
			return null;
		}
	public static String valoresJDEInsFCV() {
		
		String query="SELECT TVALALF FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroCaja + "' AND TCOD = '"+ PropertiesSystem.valuesJDEFCV +"' ";     
			
			List<Object> tipoTRX= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
			
			if(tipoTRX.size()>0)
			return tipoTRX.get(0).toString();
			else
			return null;
		}
public static String valoresJDEInsFinanciamiento() {
		
		String query="SELECT TVALALF FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroCaja + "' AND TCOD = '"+ PropertiesSystem.valuesJDEFinanciamiento +"' ";     
			
			List<Object> tipoTRX= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
			
			if(tipoTRX.size()>0)
			return tipoTRX.get(0).toString();
			else
			return null;
		}

public static String obtenerCuentasFCVGanancia(String compania) {
	
	String query="SELECT CONCAT(CONCAT( COD_CUENTA_OBJETO,',' ),IFNULL(COD_SUBCUENTA,'')) CUENTA  FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroFCVGanancia + "' AND COD_COMPANIA = '"+ compania +"' ";     
		
		List<Object> cuentaGanancia= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
		
		if(cuentaGanancia.size()>0)
		return cuentaGanancia.get(0).toString();
		else
		return null;
	}

public static String obtenerCuentasFCVPerdida(String compania) {
	
	String query="SELECT CONCAT(CONCAT( COD_CUENTA_OBJETO,',' ),IFNULL(COD_SUBCUENTA,'')) CUENTA  FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroFCVPerdida + "' AND COD_COMPANIA = '"+ compania +"' ";     
		
		List<Object> cuentaPerdida= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
		
		if(cuentaPerdida.size()>0)
		return cuentaPerdida.get(0).toString();
		else
		return null;
	}

public static String obtenerIDCuentasFCVGanancia(String compania,String cuenta) {
	
	String qryCuentaObjeto="SELECT IFNULL(COD_CUENTA_OBJETO,'')  CUENTA  FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroFCVGanancia + "' AND COD_COMPANIA = '"+ compania +"' ";     
	String qrySubcuenta = "SELECT IFNULL(COD_SUBCUENTA,'')  CUENTA  FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroFCVGanancia + "' AND COD_COMPANIA = '"+ compania +"' ";
	List<Object> cuentaObjeto= ConsolidadoDepositosBcoCtrl.executeSqlQuery(qryCuentaObjeto, null, true);
	List<Object> subCuenta= ConsolidadoDepositosBcoCtrl.executeSqlQuery(qrySubcuenta, null, true);
	
	String qryidCuenta = "SELECT " + 
			"		CAST(x.GMAID AS varchar(37) CCSID 37) " + 
			"	FROM " + 
			"		"+PropertiesSystem.JDEDTA+".F0901 x" + 
			"	WHERE " + 
			"		TRIM(x.GMMCU) = '"+cuenta+"'" + 
			"			AND TRIM(x.GMOBJ) = '"+cuentaObjeto.get(0).toString()+"'" + 
			"				AND TRIM(x.GMSUB) = '"+subCuenta.get(0).toString()+"'" + 
			"FETCH FIRST 1 ROWS ONLY";
	List<Object> idCuentaGanancia = ConsolidadoDepositosBcoCtrl.executeSqlQuery(qryidCuenta, null, true);
		if(idCuentaGanancia.size()>0)
		return idCuentaGanancia.get(0).toString();
		else
		return null;
	}

public static String obtenerIDCuentasFCVPerdida(String compania,String cuenta) {
	
	String qryCuentaObjeto="SELECT IFNULL(COD_CUENTA_OBJETO,'')  CUENTA  FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroFCVPerdida + "' AND COD_COMPANIA = '"+ compania +"' ";     
	String qrySubcuenta = "SELECT IFNULL(COD_SUBCUENTA,'')  CUENTA  FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroFCVPerdida + "' AND COD_COMPANIA = '"+ compania +"' ";
	List<Object> cuentaObjeto= ConsolidadoDepositosBcoCtrl.executeSqlQuery(qryCuentaObjeto, null, true);
	List<Object> subCuenta= ConsolidadoDepositosBcoCtrl.executeSqlQuery(qrySubcuenta, null, true);
	
	String qryidCuenta = "SELECT " + 
			"		CAST(x.GMAID AS varchar(37) CCSID 37) " + 
			"	FROM " + 
			"		"+PropertiesSystem.JDEDTA+".F0901 x" + 
			"	WHERE " + 
			"		TRIM(x.GMMCU) = '"+cuenta+"'" + 
			"			AND TRIM(x.GMOBJ) = '"+cuentaObjeto.get(0).toString()+"'" + 
			"				AND TRIM(x.GMSUB) = '"+subCuenta.get(0).toString()+"'" + 
			"FETCH FIRST 1 ROWS ONLY";
	
		List<Object> idCuentaPerdida= ConsolidadoDepositosBcoCtrl.executeSqlQuery(qryidCuenta, null, true);
		
		if(idCuentaPerdida.size()>0)
		return idCuentaPerdida.get(0).toString();
		else
		return null;
	}
public static String obteneConfNumeracionRU(String compania) {
	
	String query="SELECT TVALALF  FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroNumeracionRU + "' AND COD_COMPANIA = '"+ compania +"' ";     
		
		List<Object> configuracion= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
		
		if(configuracion.size()>0)
		return configuracion.get(0).toString();
		else
		return null;
	}
}

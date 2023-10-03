package com.casapellas.util;

import java.util.List;

import com.casapellas.controles.ConsolidadoDepositosBcoCtrl;

public class DocumuentosTransaccionales {
	
	public static CatalogoGenerico getTipoTRXContado() {
		
	String query="SELECT TVALALF AS VALOR, TDESC AS DESCRIPCION FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroCaja + "' AND TCOD = '"+ PropertiesSystem.tipoTrxContado +"' ";     
		
		List<CatalogoGenerico> tipoTRXContado= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, CatalogoGenerico.class, true);
		
		if(tipoTRXContado.size()>0)
		return tipoTRXContado.get(0);
		else
		return null;
	}
	
	public static String getValuesJDEContado() {
		
		String query="SELECT TVALALF FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroCaja + "' AND TCOD = '"+ PropertiesSystem.valuesJDEContado +"' ";     
			
			List<Object> tipoTRXContado= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
			
			if(tipoTRXContado.size()>0)
			return tipoTRXContado.get(0).toString();
			else
			return null;
		}
	public static String getValuesJDENumeracion() {
		
		String query="SELECT TVALALF FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroCaja + "' AND TCOD = '"+ PropertiesSystem.valuesJDENumeracion +"' ";     
			
			List<Object> tipoTRXContado= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
			
			if(tipoTRXContado.size()>0)
			return tipoTRXContado.get(0).toString();
			else
			return null;
		}
	public static String getValuesJDEDevolucionContado() {
		
		String query="SELECT TVALALF FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroCaja + "' AND TCOD = '"+ PropertiesSystem.valuesJDEDevolucionContado +"' ";     
			
			List<Object> tipoTRXContado= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
			
			if(tipoTRXContado.size()>0)
			return tipoTRXContado.get(0).toString();
			else
			return null;
		}
	public static String valoresJDEInsCredito() {
		
		String query="SELECT TVALALF FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroCaja + "' AND TCOD = '"+ PropertiesSystem.valuesJDECredito +"' ";     
			
			List<Object> tipoTRXContado= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
			
			if(tipoTRXContado.size()>0)
			return tipoTRXContado.get(0).toString();
			else
			return null;
		}
	public static String valoresJDEInsPrimaReservas() {
		
		String query="SELECT TVALALF FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroCaja + "' AND TCOD = '"+ PropertiesSystem.valuesJDEPrimaReserva +"' ";     
			
			List<Object> tipoTRXContado= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
			
			if(tipoTRXContado.size()>0)
			return tipoTRXContado.get(0).toString();
			else
			return null;
		}
public static String valoresJDEInsFinanciamiento() {
		
		String query="SELECT TVALALF FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroCaja + "' AND TCOD = '"+ PropertiesSystem.valuesJDEFinanciamiento +"' ";     
			
			List<Object> tipoTRXContado= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
			
			if(tipoTRXContado.size()>0)
			return tipoTRXContado.get(0).toString();
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
}

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
}

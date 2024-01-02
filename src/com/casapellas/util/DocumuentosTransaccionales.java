package com.casapellas.util;

import java.util.List;

import org.hibernate.Session;

import com.casapellas.controles.ConsolidadoDepositosBcoCtrl;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;

public class DocumuentosTransaccionales {
	Session sesion = HibernateUtilPruebaCn.currentSession();
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
	int conpaniaEnviada= Integer.parseInt(compania);
	
	String query="SELECT CONCAT(CONCAT( COD_CUENTA_OBJETO,',' ),IFNULL(COD_SUBCUENTA,' ')) CUENTA  FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroFCVGanancia + "' AND COD_COMPANIA = '"+ conpaniaEnviada +"' ";     
		
		List<Object> cuentaGanancia= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
		
		if(cuentaGanancia.size()>0)
		return cuentaGanancia.get(0).toString();
		else
		return null;
	}

public static String obtenerCuentasFCVPerdida(String compania) {
	
	int conpaniaEnviada= Integer.parseInt(compania);
	String query="SELECT CONCAT(CONCAT( COD_CUENTA_OBJETO,',' ),IFNULL(COD_SUBCUENTA,' ')) CUENTA  FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroFCVPerdida + "' AND COD_COMPANIA = '"+ conpaniaEnviada +"' ";     
		
		List<Object> cuentaPerdida= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
		
		if(cuentaPerdida.size()>0)
		return cuentaPerdida.get(0).toString();
		else
		return null;
	}

public static String obtenerIDCuentasFCVGanancia(String compania,String cuenta) {
	
	int conpaniaEnviada= Integer.parseInt(compania);
	String qryCuentaObjeto="SELECT IFNULL(COD_CUENTA_OBJETO,'')  CUENTA  FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroFCVGanancia + "' AND COD_COMPANIA = '"+ conpaniaEnviada +"' ";     
	String qrySubcuenta = "SELECT IFNULL(COD_SUBCUENTA,'')  CUENTA  FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroFCVGanancia + "' AND COD_COMPANIA = '"+ conpaniaEnviada +"' ";
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
	int conpaniaEnviada= Integer.parseInt(compania);
	String qryCuentaObjeto="SELECT IFNULL(COD_CUENTA_OBJETO,'')  CUENTA  FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroFCVPerdida + "' AND COD_COMPANIA = '"+ conpaniaEnviada +"' ";     
	String qrySubcuenta = "SELECT IFNULL(COD_SUBCUENTA,'')  CUENTA  FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroFCVPerdida + "' AND COD_COMPANIA = '"+ conpaniaEnviada +"' ";
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
	int conpaniaEnviada= Integer.parseInt(compania);
	String query="SELECT TVALALF  FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroNumeracionRU + "' AND COD_COMPANIA = '"+ conpaniaEnviada +"' ";     
		
		List<Object> configuracion= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
		
		if(configuracion.size()>0)
		return configuracion.get(0).toString();
		else
		return null;
	}

public static String obtenerCuentaSalida(String compania) {
	int conpaniaEnviada= Integer.parseInt(compania);
	String query="SELECT CONCAT(IFNULL(COD_UNIDAD_NEGOCIO,''),CONCAT(',',CONCAT(CONCAT( COD_CUENTA_OBJETO,',' ),IFNULL(COD_SUBCUENTA,''))))"
			+ " CUENTA  FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroCuentaSalida +
			"' AND COD_COMPANIA = '"+ conpaniaEnviada +"' ";     
		
		List<Object> cuentaPerdida= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
		
		if(cuentaPerdida.size()>0)
		return cuentaPerdida.get(0).toString();
		else
		return null;
	}

public static String IDDPCONFIRMADO() {
	
	String query="SELECT TVALALF FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroConciliacion + "' AND TCOD = '"+ PropertiesSystem.IDDPCONFIRMADO +"' ";     
		
		List<Object> tipoTRX= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
		
		if(tipoTRX.size()>0)
		return tipoTRX.get(0).toString();
		else
		return null;
	}

public static int IDDPNOCONFIRMADO() {
	
	String query="SELECT TVALALF FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroConciliacion + "' AND TCOD = '"+ PropertiesSystem.IDDPNOCONFIRMADO +"' ";     
		
		List<Object> tipoTRX= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
		
		if(tipoTRX.size()>0)
		return Integer.parseInt(tipoTRX.get(0).toString());
		else
		return 0;
	}

public static int IDCRFAUTOMATICA() {
	
	String query="SELECT TVALALF FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroConciliacion + "' AND TCOD = '"+ PropertiesSystem.IDCRFAUTOMATICA +"' ";     
		
		List<Object> tipoTRX= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
		
		if(tipoTRX.size()>0)
		return Integer.parseInt(tipoTRX.get(0).toString());
		else
		return 0;
	}

public static String CFRAUTO() {
	
	String query="SELECT TVALALF FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroConciliacion + "' AND TCOD = '"+ PropertiesSystem.CFRAUTO +"' ";     
		
		List<Object> tipoTRX= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
		
		if(tipoTRX.size()>0)
		return tipoTRX.get(0).toString();
		else
		return null;
	}

public static String CFRMANUAL() {
	
	String query="SELECT TVALALF FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroConciliacion + "' AND TCOD = '"+ PropertiesSystem.CFRMANUAL +"' ";     
		
		List<Object> tipoTRX= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
		
		if(tipoTRX.size()>0)
		return tipoTRX.get(0).toString();
		else
		return null;
	}
public static String DPCONFIRMADO() {
	
	String query="SELECT TVALALF FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroConciliacion + "' AND TCOD = '"+ PropertiesSystem.DPCONFIRMADO +"' ";     
		
		List<Object> tipoTRX= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
		
		if(tipoTRX.size()>0)
		return tipoTRX.get(0).toString();
		else
		return null;
	}
public static String DPNOCONFIRMADO() {
	
	String query="SELECT TVALALF FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroConciliacion + "' AND TCOD = '"+ PropertiesSystem.DPNOCONFIRMADO +"' ";     
		
		List<Object> tipoTRX= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
		
		if(tipoTRX.size()>0)
		return tipoTRX.get(0).toString();
		else
		return "";
	}
public static String TIPODOCREFERP9() {
	
	String query="SELECT TVALALF FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroConciliacion + "' AND TCOD = '"+ PropertiesSystem.TIPODOCREFERP9 +"' ";     
		
		List<Object> tipoTRX= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
		
		if(tipoTRX.size()>0)
		return tipoTRX.get(0).toString();
		else
		return null;
	}
public static String TIPODOCREFERZX() {
	
	String query="SELECT TVALALF FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroConciliacion + "' AND TCOD = '"+ PropertiesSystem.TIPODOCREFERZX +"' ";     
		
		List<Object> tipoTRX= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
		
		if(tipoTRX.size()>0)
		return tipoTRX.get(0).toString();
		else
		return null;
	}

public static String TIPODOCREFERZZ() {
	
	String query="SELECT TVALALF FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroConciliacion + "' AND TCOD = '"+ PropertiesSystem.TIPODOCREFERZZ +"' ";     
		
		List<Object> tipoTRX= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
		
		if(tipoTRX.size()>0)
		return tipoTRX.get(0).toString();
		else
		return null;
	}
public static String TIPODOCREFERXG() {
	
	String query="SELECT TVALALF FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroConciliacion + "' AND TCOD = '"+ PropertiesSystem.TIPODOCREFERXG +"' ";     
		
		List<Object> tipoTRX= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
		
		if(tipoTRX.size()>0)
		return tipoTRX.get(0).toString();
		else
		return null;
	}
public static String MONEDABASE() {
	
	String query="SELECT TVALALF FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroConciliacion + "' AND TCOD = '"+ PropertiesSystem.MONEDABASE +"' ";     
		
		List<Object> tipoTRX= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
		
		if(tipoTRX.size()>0)
		return tipoTRX.get(0).toString();
		else
		return null;
	}
public static String UNIDADNEGOCIOBASE() {
	
	String query="SELECT TVALALF FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroConciliacion + "' AND TCOD = '"+ PropertiesSystem.UNIDADNEGOCIOBASE +"' ";     
		
		List<Object> tipoTRX= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
		
		if(tipoTRX.size()>0)
		return tipoTRX.get(0).toString();
		else
		return null;
	}
public static String CODIGOTIPOAUXILIARCT() {
	
	String query="SELECT TVALALF FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroConciliacion + "' AND TCOD = '"+ PropertiesSystem.CODIGOTIPOAUXILIARCT +"' ";     
		
		List<Object> tipoTRX= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
		
		if(tipoTRX.size()>0)
		return tipoTRX.get(0).toString();
		else
		return null;
	}
public static String CODIGOTIPOAUXILIARFE() {
	
	String query="SELECT TVALALF FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroConciliacion + "' AND TCOD = '"+ PropertiesSystem.CODIGOTIPOAUXILIARFE +"' ";     
		
		List<Object> tipoTRX= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
		
		if(tipoTRX.size()>0)
		return tipoTRX.get(0).toString();
		else
		return null;
	}
public static String CODIGOCREDOMATIC() {
	
	String query="SELECT TVALALF FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroConciliacion + "' AND TCOD = '"+ PropertiesSystem.CODIGOCREDOMATIC +"' ";     
		
		List<Object> tipoTRX= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
		
		if(tipoTRX.size()>0)
		return tipoTRX.get(0).toString();
		else
		return null;
	}

public static String USUARIOPRECONCILIACION() {
	Session sesion = HibernateUtilPruebaCn.currentSession();
	String query="SELECT TVALALF FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroConciliacion + "' AND TCOD = '"+ PropertiesSystem.USUARIOPRECONCILIACION +"' ";     
		
	@SuppressWarnings("unchecked")
	List<Object> tipoTRX= sesion.createSQLQuery(query).list();
		
		if(tipoTRX.size()>0)
		return tipoTRX.get(0).toString();
		else
		return null;
	}

public static String ENSADMINISTRADORCAJA() {
	Session sesion = HibernateUtilPruebaCn.currentSession();
	String query="SELECT TVALALF FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroConciliacion + "' AND TCOD = '"+ PropertiesSystem.ENSADMINISTRADORCAJA +"' ";     
		
	@SuppressWarnings("unchecked")
	List<Object> tipoTRX= sesion.createSQLQuery(query).list();
		
		if(tipoTRX.size()>0)
		return tipoTRX.get(0).toString();
		else
		return null;
	}
public static String ENSCONCILIADORPRINCIPAL() {
	Session sesion = HibernateUtilPruebaCn.currentSession();
	String query="SELECT TVALALF FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroConciliacion + "' AND TCOD = '"+ PropertiesSystem.ENSCONCILIADORPRINCIPAL +"' ";     
		
		@SuppressWarnings("unchecked")
		List<Object> tipoTRX= sesion.createSQLQuery(query).list();
		
		if(tipoTRX.size()>0)
		return tipoTRX.get(0).toString();
		else
		return null;
	}
public static String ENSCONCILIADORSUPERVISOR() {
	Session sesion = HibernateUtilPruebaCn.currentSession();
	String query="SELECT TVALALF FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroConciliacion + "' AND TCOD = '"+ PropertiesSystem.ENSCONCILIADORSUPERVISOR +"' ";     
		
	@SuppressWarnings("unchecked")
	List<Object> tipoTRX= sesion.createSQLQuery(query).list();
		
		if(tipoTRX.size()>0)
		return tipoTRX.get(0).toString();
		else
		return null;
	}
public static String CTAOTROSINGRESOS() {
	
	String query="SELECT CONCAT(IFNULL(COD_UNIDAD_NEGOCIO,''),CONCAT(',',CONCAT(CONCAT( COD_CUENTA_OBJETO,',' ),IFNULL(COD_SUBCUENTA,''))))"
			+ " CUENTA  FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+PropertiesSystem.parametroConciliacion + "' AND TCOD = '"+ PropertiesSystem.CTAOTROSINGRESOS +"' ";     
		
		List<Object> cuentaPerdida= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
		
		if(cuentaPerdida.size()>0)
		return cuentaPerdida.get(0).toString();
		else
		return null;
	}

public static String CTAGASTOSDIVERSOS() {
	
	String query="SELECT CONCAT(IFNULL(COD_UNIDAD_NEGOCIO,''),CONCAT(',',CONCAT(CONCAT( COD_CUENTA_OBJETO,',' ),IFNULL(COD_SUBCUENTA,''))))"
			+ " CUENTA  FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+PropertiesSystem.parametroConciliacion + "' AND TCOD = '"+ PropertiesSystem.CTAGASTOSDIVERSOS +"' ";     
		
		List<Object> cuentaPerdida= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
		
		if(cuentaPerdida.size()>0)
		return cuentaPerdida.get(0).toString();
		else
		return null;
	}

public static String CTADEUDORESVARIOSUNINEG(String codCompania) {
		
		String query="SELECT COD_UNIDAD_NEGOCIO CONCAT ',' CONCAT COD_CUENTA_OBJETO CONCAT ',' CONCAT IFNULL(COD_SUBCUENTA, '') FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroCtaDeudores + "' AND COD_COMPANIA = '"+ codCompania +"' ";     
			
			List<Object> tipoTRX= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
			
			if(tipoTRX.size()>0)
			return tipoTRX.get(0).toString();
			else
			return null;
		}
public static String CTADEUDORESVARIOSUNINEGTODAS() {
	
	String query="SELECT LISTAGG(COD_UNIDAD_NEGOCIO,',') FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroCtaDeudores + "'";     
		
		List<Object> tipoTRX= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
		
		if(tipoTRX.size()>0)
		return tipoTRX.get(0).toString();
		else
		return null;
	}
public static String companiaCASAPELLAS() {
	
	String query="SELECT COD_COMPANIA FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroCompania + "' AND TCOD = '"+ PropertiesSystem.companiaCASAPELLAS +"' ";     
		
		List<Object> tipoTRX= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
		
		if(tipoTRX.size()>0)
		return tipoTRX.get(0).toString();
		else
		return null;
	}
public static String companiaALPESA() {
	
	String query="SELECT COD_COMPANIA FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroCompania + "' AND TCOD = '"+ PropertiesSystem.companiaALPESA +"' ";     
		
		List<Object> tipoTRX= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
		
		if(tipoTRX.size()>0)
		return tipoTRX.get(0).toString();
		else
		return null;
	}
public static String companiaKIPESA() {
	
	String query="SELECT COD_COMPANIA FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroCompania + "' AND TCOD = '"+ PropertiesSystem.companiaKIPESA +"' ";     
		
		List<Object> tipoTRX= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
		
		if(tipoTRX.size()>0)
		return tipoTRX.get(0).toString();
		else
		return null;
	}
public static String companiaCAPESA() {
	
	String query="SELECT COD_COMPANIA FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroCompania + "' AND TCOD = '"+ PropertiesSystem.companiaCAPESA +"' ";     
		
		List<Object> tipoTRX= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
		
		if(tipoTRX.size()>0)
		return tipoTRX.get(0).toString();
		else
		return null;
	}

public static String CIERREFALTANTETIPODOC() {
	
	String query="SELECT TVALALF FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroCierre + "' AND TCOD = '"+ PropertiesSystem.CIERRE_FALTANTE_TIPODOC +"' ";     
		
		List<Object> tipoTRX= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
		
		if(tipoTRX.size()>0)
		return tipoTRX.get(0).toString();
		else
		return null;
	}
public static String VALORMAXIMODESCUADRE() {
	
	String query="SELECT TVALALF FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroConfiguracionMaximoPermisible + "' AND TCOD = '"+ PropertiesSystem.maximoPermitidoPMT +"' ";     
		
		List<Object> tipoTRX= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
		
		if(tipoTRX.size()>0)
		return tipoTRX.get(0).toString();
		else
		return null;
	}
public static String tipoBatchDevolucionCredito() {
	
	String query="SELECT TVALALF FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ PropertiesSystem.parametroTipoBatchDevolucion + "' AND TCOD = '"+ PropertiesSystem.tipoBatchDevolucionCredito +"' ";     
		
		List<Object> tipoTRX= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
		
		if(tipoTRX.size()>0)
		return tipoTRX.get(0).toString();
		else
		return null;
	}
}

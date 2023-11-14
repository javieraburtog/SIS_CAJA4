package com.casapellas.controles;

import com.casapellas.entidades.CajaParametro;
import com.casapellas.util.CajaConfiguracionJde;
import com.casapellas.util.LogCajaService;

public class ClsCajaConfiguracionJde {
	public CajaConfiguracionJde obtenerConfiguracionCajaJde(String pCodigo) {
		CajaConfiguracionJde config = null;
		
		try {
			ClsParametroCaja servicio = new ClsParametroCaja();
			CajaParametro cajaParametro = servicio.getParametros("30", "0", pCodigo);
			
			/*
			 * CO,YL,CA,USD,F,AA,COR,D,G,,A,ICA
			 * */
			if (cajaParametro != null) {
				config = new CajaConfiguracionJde();
				String[] valores = cajaParametro.getValorAlfanumerico().trim().split(",");
				config.setTipoRecibo(isValidIndex(valores, 0) ? valores[0].trim().toUpperCase() : "");
				config.setTipoBatch(isValidIndex(valores, 8) ? valores[8].trim().toUpperCase() : "");
				config.setContDocJdeKey("");
				config.setContDocJdeIdx("");
				config.setTipoDocContJde(isValidIndex(valores, 1) ? valores[1].trim().toUpperCase() : "");
				config.setCodLibroJdeForaneo(isValidIndex(valores, 2) ? valores[2].trim().toUpperCase() : "");
				config.setCodMonedaForanea(isValidIndex(valores, 3) ? valores[3].trim().toUpperCase() : "");
				config.setCodLineaJdeForaneo(isValidIndex(valores, 4) ? valores[4].trim().toUpperCase() : "");
				config.setCodLibroJdeLocal(isValidIndex(valores, 5) ? valores[5].trim().toUpperCase() : "");
				config.setCodMonedaLocal(isValidIndex(valores, 6) ? valores[6].trim().toUpperCase() : "");
				config.setCodLineaJdeLocal(isValidIndex(valores, 7) ? valores[7].trim().toUpperCase() : "");
				config.setCodICA(isValidIndex(valores, 11) ? valores[9].trim().toUpperCase() : "");
				
				// Obtener el codigo para la generacion del numero de documento
				cajaParametro = servicio.getParametros("30", "0", "NUM_JDE_CONF");
				if (cajaParametro == null) {
					throw new Exception("No se encontro la configuracion para la generacion numeros de documentos JDE");
				}
				
				String[] cfgNoRecibo =  cajaParametro.getValorAlfanumerico().trim().split(",");
				switch (config.getTipoRecibo()) {
				case "CO":
				case "FCV":
					config.setContDocJdeKey(cfgNoRecibo[9].trim().toUpperCase());
					config.setContDocJdeIdx(cfgNoRecibo[8].trim().toUpperCase());
					break;
				case "CR":
					config.setContDocJdeKey(cfgNoRecibo[0].trim().toUpperCase());
					config.setContDocJdeIdx(cfgNoRecibo[1].trim().toUpperCase());	
					break;
				case "PR":
					config.setContDocJdeKey("");
					config.setContDocJdeIdx("");
					break;
				case "FN":
					config.setContDocJdeKey("");
					config.setContDocJdeIdx("");
					break;
				case "PM":
					config.setContDocJdeKey(cfgNoRecibo[5].trim().toUpperCase());
					config.setContDocJdeIdx(cfgNoRecibo[4].trim().toUpperCase());
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			LogCajaService.CreateLog("obtenerConfiguracionCajaJde", "ERR", e.getMessage());
		}
		
		return config;
	}
	
	private boolean isValidIndex(String[] arr, int index) {
		return index >= 0 && index < arr.length;
	}
}

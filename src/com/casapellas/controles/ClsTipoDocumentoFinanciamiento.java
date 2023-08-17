package com.casapellas.controles;

import java.io.IOException;

import com.casapellas.util.LogCajaService;
import com.google.gson.Gson;

import ni.com.casapellas.apikey.general.authenticator.GCPMCAJAAccountsServiceAuthenticator;
import ni.com.casapellas.apikey.resource.key.ApiKey;
import ni.com.casapellas.client.config.ConfigConnection;
import ni.com.casapellas.client.config.Parameter;
import ni.com.casapellas.tool.restful.connection.RestConnection;
import ni.com.casapellas.tool.restful.connection.RestResponse;

public class ClsTipoDocumentoFinanciamiento {
	
	private static ApiKey apiKey = null;
	
	 static {
	        apiKey = new GCPMCAJAAccountsServiceAuthenticator();
	    }
	
	 /**
	  * 
	  * @param strIdTipoDocumentoFinanciamiento
	  * @param strCodigoDocumento
	  * @param strTipoAgrupacion
	  * @return
	  * @throws IOException
	  */
	public RestResponse getConfiguracionTipoDocumento(String strIdTipoDocumentoFinanciamiento, 
													   String strCodigoDocumento,
													   String strTipoAgrupacion) throws IOException
	{

		Gson gson = new Gson();
    	
    	Parameter pIdTipoDocumentoFinanciamiento = new Parameter();
    	pIdTipoDocumentoFinanciamiento.addValue(strIdTipoDocumentoFinanciamiento);
    	String strOIdTipoDocumentoFinanciamiento = pIdTipoDocumentoFinanciamiento != null ? gson.toJson(pIdTipoDocumentoFinanciamiento) : "";
    	
    	Parameter pCodigoDocumento = new Parameter();
    	pCodigoDocumento.addValue(strCodigoDocumento);
    	String strOCodigoDocumento = pCodigoDocumento != null ? gson.toJson(pCodigoDocumento) : "";
    	
    	Parameter pTipoAgrupacion = new Parameter();
    	pTipoAgrupacion.addValue(strTipoAgrupacion);
    	String strOTipoAgrupacion = pTipoAgrupacion != null ? gson.toJson(pTipoAgrupacion) : "";
    	
		String[][] queryHeaders = new String[][] {
			{ "IdTipoDocumentoFinanciamiento", strOIdTipoDocumentoFinanciamiento != null ? strOIdTipoDocumentoFinanciamiento : "" },
			{ "CodigoDocumento", strOCodigoDocumento != null ? strOCodigoDocumento : "" },
			{ "TipoAgrupacion", strOTipoAgrupacion != null ? strOTipoAgrupacion : "" }};
		
		String baseUrl = ConfigConnection.PROTOCOL_WS_FINAN + "://" +ConfigConnection.SERVER_WS_FINAN + (ConfigConnection.PORT_WS_FINAN.compareToIgnoreCase("") == 0 ? "" : ":") + 
				ConfigConnection.PORT_WS_FINAN + "/" + ConfigConnection.CONTEXT_NAME_WS_FINANCIAMIENTO + "/" + ConfigConnection.CONTEXT_NAME_FINANCIAMIENTO + 
				"/TipoDocumentoFinanciamientoRest/getTipoDocumentoFinanciamiento";
		
		LogCajaService.CreateLog("getConfiguracionTipoDocumento", "WSCALL", baseUrl);
			
		RestConnection conn = new RestConnection(apiKey, baseUrl);
		
		RestResponse response = conn.get(queryHeaders, RestConnection.CONTENT_TYPE_JSON);
		conn.closeConection();
		return response;
	}
}

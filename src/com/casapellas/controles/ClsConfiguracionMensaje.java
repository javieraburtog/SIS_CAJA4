package com.casapellas.controles;

import java.io.IOException;

import com.google.gson.Gson;

import ni.com.casapellas.apikey.general.authenticator.GCPMCAJAAccountsServiceAuthenticator;
import ni.com.casapellas.apikey.resource.key.ApiKey;
import ni.com.casapellas.client.config.ConfigConnection;
import ni.com.casapellas.client.config.Parameter;
import ni.com.casapellas.tool.restful.connection.RestConnection;
import ni.com.casapellas.tool.restful.connection.RestResponse;

/**
 * 
 * @author luisfonseca
 *
 */
public class ClsConfiguracionMensaje {
	private static ApiKey apiKey = null;
	
	 static {
	        apiKey = new GCPMCAJAAccountsServiceAuthenticator();
	    }
	 
	 /**
	  * @author luisfonseca
	  * @param strIdSistema
	  * @param strTipo1
	  * @param strTipo2
	  * @param strIdMensaje
	  * @return
	  * @throws IOException
	  */
	 public static RestResponse getConfiguracionMensajeList(String strIdSistema, 
															    String strTipo1, 
															    String strTipo2,
															    String strIdMensaje) throws IOException
		{
	    	Gson gson = new Gson();
	    	
	    	Parameter pIdSistema = new Parameter();
	    	pIdSistema.addValue(strIdSistema);
	    	String strOIdSistema = pIdSistema != null ? gson.toJson(pIdSistema) : "";
	    	
	    	Parameter pTipo1 = new Parameter();
	    	pTipo1.addValue(strTipo1);
	    	String strOTipo1 = pTipo1 != null ? gson.toJson(pTipo1) : "";
	    	
	    	Parameter pTipo2 = new Parameter();
	    	pTipo2.addValue(strTipo2);
	    	String strOTipo2 = pTipo2 != null ? gson.toJson(pTipo2) : "";
	    	
	    	Parameter pIdMensaje = new Parameter();
	    	pIdMensaje.addValue(strIdMensaje);
	    	String strOIdMensaje = pIdMensaje != null ? gson.toJson(pIdMensaje) : "";
	    	
	    	String[][] queryHeaders = new String[][] {
					{ "IdSistema", strOIdSistema != null ? strOIdSistema : "" },
					{ "Tipo1", strOTipo1 != null ? strOTipo1 : "" },
					{ "Tipo2", strOTipo2 != null ? strOTipo2 : "" },
					{ "IdMensaje", strOIdMensaje != null ? strOIdMensaje : "" }};
					
			String str = ConfigConnection.PROTOCOL_WS_CAJA_N + "://" +ConfigConnection.SERVER_WS_CAJA_N + (ConfigConnection.PORT_WS_CAJA_N.compareToIgnoreCase("") == 0 ? "" : ":") + 
					ConfigConnection.PORT_WS_CAJA_N + "/" + ConfigConnection.CONTEXT_NAME_WS_CAJA_N + "/" + ConfigConnection.CONTEXT_NAME_CAJA_N + 
					"/ConfiguracionMensajeRest/getConfiguracionMensajeList";
			
			System.out.println(str);
			
			RestConnection conn = new RestConnection(apiKey, str);
			RestResponse response = conn.get(queryHeaders, RestConnection.CONTENT_TYPE_JSON);
		    conn.closeConection();
		    return response;
		}
}

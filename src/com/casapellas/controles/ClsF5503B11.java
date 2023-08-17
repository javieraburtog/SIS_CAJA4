package com.casapellas.controles;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ni.com.casapellas.apikey.general.authenticator.GCPMCAJAAccountsServiceAuthenticator;
import ni.com.casapellas.apikey.resource.key.ApiKey;
import ni.com.casapellas.client.config.ConfigConnection;
import ni.com.casapellas.client.config.Parameter;
import ni.com.casapellas.tool.restful.connection.RestConnection;
import ni.com.casapellas.tool.restful.connection.RestResponse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.casapellas.jde.creditos.CodigosJDE1;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.PropertiesSystem;
import com.google.gson.Gson;

/**
 * 
 * @author luisfonseca
 *
 */
public class ClsF5503B11 {
	
	private static ApiKey apiKey = null;
	
	 static {
	        apiKey = new GCPMCAJAAccountsServiceAuthenticator();
	    }

	/**
	 * @author luisfonseca
	 * @param strNoCaja
	 * @param strCodCliente
	 * @param strCodFactura
	 * @param strTipoFactura
	 * @param strCuota
	 * @param strCajero
	 * @param strUsuario
	 * @param strIpMaquina
	 * @param strNavegador
	 * @param strIpHost
	 * @return
	 */
	public String[] ActualizarPlanFinanciamiento(int intCodCliente,
												 String strCodSucursal,
												 int intNumeroSolicitud,
												 String strTipoSolicitud,
												 int intCuotas,
												 int intDiasVencimiento,
												 int intBatchIF,			 
												 int intBatchMF, 
												 String strUsuario)
	{
		String[] strResultado = new String[] {"N", "", "", "", "", ""};
		
		String DRIVER = "com.ibm.as400.access.AS400JDBCDriver"; 
		String URL = "jdbc:as400:" + PropertiesSystem.IPSERVERDB2 + "/" + PropertiesSystem.ESQUEMA + ";prompt=false;translate binary=true";
		Connection conn = null;
		
		
		try
		{
			Class.forName(DRIVER); 
			conn = DriverManager.getConnection(URL, PropertiesSystem.CN_USRNAME, PropertiesSystem.CN_USRPWD); 

			
			String strQuery = 
				" UPDATE " + PropertiesSystem.GCPCXC + " .F5503AM SET " +
					" ATDVEN = ?, ATICUM = ?, ATICU = ?," +
					" ATUSER = ?, ATUPID = ?, ATICUT = ?," +
					" ATDIVJ = ?, ATHORA = ?, ATUSERM = ?," +
					" ATUPIDM = ?, ATICUTM = ?, ATDIVJM = ?, " +
					" ATHORAM = ? " +
					"WHERE ATAN8 = ? and ATKCOO = ? and ATDOCO = ? and ATDCTO = ? and ATDFR = ? " ;
				
			PreparedStatement pstmt = conn.prepareStatement(strQuery);
			pstmt.setInt(1, intDiasVencimiento);
			pstmt.setLong(2, intBatchMF);
			pstmt.setLong(3, intBatchIF);
			pstmt.setString(4, strUsuario);
			pstmt.setString(5, PropertiesSystem.CONTEXT_NAME);
			pstmt.setString(6, CodigosJDE1.BATCH_FINANCIMIENTO.codigo());
			pstmt.setInt(7, FechasUtil.dateToJulian(new Date()) );
			pstmt.setInt(8, Integer.valueOf(new SimpleDateFormat("HHmmss").format( new Date())) );
			pstmt.setString(9, strUsuario);
			pstmt.setString(10, PropertiesSystem.CONTEXT_NAME);
			pstmt.setString(11, CodigosJDE1.BATCH_FINANCIMIENTO.codigo());
			pstmt.setInt(12, FechasUtil.dateToJulian(new Date()) );
			pstmt.setInt(13, Integer.valueOf(new SimpleDateFormat("HHmmss").format( new Date() )) );
			pstmt.setInt(14, intCodCliente );
			pstmt.setString(15, strCodSucursal);
			pstmt.setInt(16, intNumeroSolicitud );
			pstmt.setString(17, strTipoSolicitud );
			pstmt.setInt(18, intCuotas );
			pstmt.execute();
			
			strResultado[0] = "S";
			strResultado[1] = String.valueOf(intCodCliente);
			strResultado[2] = String.valueOf(intNumeroSolicitud);
			strResultado[3] = String.valueOf(intCuotas);
			strResultado[4] = strCodSucursal;
			strResultado[5] = strTipoSolicitud;

		}
		catch(Exception e)
		{
			e.printStackTrace();
			//throw new Exception("No se pudo insertar secuencia de facturas en bitacora correspondiente");
		}
		finally
		{
			try
			{
				conn.close();
			}
			catch(Exception e){}
		}
		
		return strResultado;
	}
	
	
	/**
     * @author Luis Fonseca
     * @param paramCodigoCajero
     * @param paramCodigoCliente
     * @param paramCodigoCompania
     * @param paramMonedaPago
     * @param paramMontoRecibido
     * @param paramConceptoRecibo
     * @param paramCodigoSucursal
     * @param paramNumeroDocumento
     * @param paramTipoDocumento
     * @param paramCuota
     * @param paramLineaProduccion
     * @param paramMetodoPago
     * @param paramMontoPago
     * @param paramMarcaTarjeta
     * @param paramCodigoMarcaTarjeta
     * @param paramNumeroReferencia
     * @param paramUsuario
     * @param paramPantalla
     * @param paramPrograma
     * @return
     * @throws IOException
     */
    public RestResponse procesarGenerarFacturaInteres(Parameter paramCodigoCliente,      
													  Parameter paramCodigoCompania,       
													  Parameter paramCodigoSucursal,     
													  Parameter paramNumeroDocumento,   
													  Parameter paramTipoDocumento,        
													  Parameter paramCuota,              
													  Parameter paramUsuario,            
													  Parameter paramPantalla,           
													  Parameter paramPrograma) throws IOException
	{
    	String[][] queryHeaders = new String[][] {};
		Gson gson = new Gson();
		
		Map<String, Parameter> map = new HashMap<String, Parameter>();
		
		map.put("CodigoCliente", paramCodigoCliente);
		map.put("CodigoCompania", paramCodigoCompania);					map.put("CodigoSucursal", paramCodigoSucursal);
		map.put("TipoDocumento", paramTipoDocumento);					map.put("NumeroDocumento", paramNumeroDocumento);
		map.put("Cuota", paramCuota);									
		//map.put("LineaNegocio", paramLineaProduccion);				
		
		map.put("Usuario", paramUsuario);
		map.put("Pantalla", paramPantalla);								map.put("Programa", paramPrograma);

	
		String json = gson.toJson(map);	
		String baseUrl = ConfigConnection.PROTOCOL + "://" +ConfigConnection.SERVER + (ConfigConnection.PORT.compareToIgnoreCase("") == 0 ? "" : ":") + 
				ConfigConnection.PORT + "/" + ConfigConnection.CONTEXT_NAME_WS_CAJA + "/" + ConfigConnection.CONTEXT_NAME_CAJA + 
				"/ProcesarFinanciamientoRest/procesarGenerarFacturaInteres";
		
		LogCajaService.CreateLog("procesarGenerarFacturaInteres", "WSCALL", baseUrl, json);
		
		RestConnection conn = new RestConnection(apiKey, baseUrl);
		RestResponse response = conn.post(queryHeaders, json, RestConnection.CONTENT_TYPE_JSON);
		conn.closeConection();
		return response;
	}
    
}

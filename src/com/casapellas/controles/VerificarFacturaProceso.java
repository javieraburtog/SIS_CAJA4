package com.casapellas.controles;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.casapellas.entidades.CajaFac;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.PropertiesSystem;

/**
 * 
 * @author luisfonseca
 *
 */
public class VerificarFacturaProceso {
	
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
	public String[] verificarFactura(String strNoCaja,
									 String strCodCliente, 
									 String strCodFactura,
									 String strTipoFactura,
									 String strCuota,
									 String strCajero,
									 String strUsuario,
									 String strIpMaquina,
									 String strNavegador,
									 String strIpHost)
	{
		String[] strResultado = new String[] {"N", "", "", "", "", "", "",""};
		
		String DRIVER = "com.ibm.as400.access.AS400JDBCDriver"; 
		String URL = "jdbc:as400:" + PropertiesSystem.IPSERVERDB2 + "/" + PropertiesSystem.ESQUEMA + ";prompt=false;translate binary=true";
		Connection conn = null;
		
		
		try
		{
			Class.forName(DRIVER); 
			conn = DriverManager.getConnection(URL, PropertiesSystem.CN_USRNAME, PropertiesSystem.CN_USRPWD); 

			//Limpiar todos los registros que dejaron en proceso para este servidor, navegador y ordenador.
			String strQuery = "update " + PropertiesSystem.ESQUEMA + ".cajafac set estado = 0 "
																				+ "where estado = 1 and "
																					  + "ip_maquina = '" + strIpMaquina + "' and "
																					  + "navegador = '" + strNavegador + "' and "
																					  + "ip_host = '" + strIpHost + "'";

			PreparedStatement pstmt = conn.prepareStatement(strQuery);
			pstmt.execute();
			
			//Guarda Log
			LogCajaService.CreateLog("verificarFactura", "QRY", strQuery);
	
			//Verificar que factura, cliente y cuota no este siendo procesado en otro ordenador y/o navegador
			strQuery = "select caid numeroCaja, codcliente codigoCliente, nofactura numeroFactura, "
								   + "partida numeroCuota, tipofactura, fecha, cajero, usuario, ip_maquina, "
								   + "navegador, ip_host "
								   + "from " + PropertiesSystem.ESQUEMA + ".cajafac "
								   + "where codcliente = " + strCodCliente + " and "
								   		 + "nofactura = " + strCodFactura + " and "
								   		 + "partida = '" + strCuota + "'  and "
								   		 + "tipofactura = '" + strTipoFactura + "' and "
								   		 + "estado = 1 and fecha = current date";

	
			pstmt = conn.prepareStatement(strQuery);
			ResultSet rs = pstmt.executeQuery();
			
			//Guarda Log
			LogCajaService.CreateLog("verificarFactura", "QRY", strQuery);
			
//			List<CajaFac> lstCajaFac = new ArrayList<CajaFac>();
			String strUsuarioT = "";
			String strIPMaquinaT = "";
			String strNavegadorT = "";
			String strIPHostT = "";
			
			while (rs.next()) {
				CajaFac cajaFac = new CajaFac();
				cajaFac.setCajero(rs.getInt("cajero"));
				cajaFac.setCodigoCliente(rs.getInt("codigoCliente"));
				cajaFac.setFecha(rs.getString("fecha"));
				cajaFac.setNumeroCaja(rs.getInt("numeroCaja"));
				cajaFac.setNumeroCuota(rs.getString("numeroCuota"));
				cajaFac.setNumeroFactura(rs.getInt("numeroFactura"));
				cajaFac.setTipofactura(rs.getString("tipofactura"));
				
				strUsuarioT = rs.getString("usuario");
				strIPMaquinaT = rs.getString("ip_maquina");
				strNavegadorT = rs.getString("navegador");
				strIPHostT = rs.getString("ip_host");
				
//				lstCajaFac.add(cajaFac);
				
				strResultado[0] = "N";
				strResultado[1] = String.valueOf(rs.getInt("numeroCaja"));
				strResultado[2] = String.valueOf(rs.getInt("numeroFactura"));
				strResultado[3] = String.valueOf(rs.getString("numeroCuota"));
				strResultado[4] = String.valueOf(rs.getString("tipofactura"));
				strResultado[5] = String.valueOf(strUsuarioT);
				strResultado[6] = String.valueOf(strNavegadorT);
				strResultado[7] = String.valueOf(strIPHostT);
				
				if(!strUsuario.trim().equalsIgnoreCase(strUsuarioT.trim()) || 
						!strIpMaquina.trim().equalsIgnoreCase(strIPMaquinaT.trim()) || 
						!strNavegador.trim().equalsIgnoreCase(strNavegadorT.trim()) ||
						!strIpHost.trim().equalsIgnoreCase(strIPHostT.trim()))
					return strResultado;
				else
				{
					strResultado[0] = "S";
					return strResultado;
				}
				
			}
			
			//Insertar registro de bloqueo de factura que luego se verificara.
			strQuery = "insert into " + PropertiesSystem.ESQUEMA + ".cajafac(caid, codcliente, nofactura, partida, tipofactura, estado, fecha, cajero, usuario, ip_maquina, navegador, ip_host) "
							+ "values(" + strNoCaja + ", " + strCodCliente + ", " + strCodFactura + ", '" + strCuota + "', '" + strTipoFactura + "', "
							+ "1, current date, " + strCajero + ", '" + strUsuario + "', '" + strIpMaquina + "', '" + strNavegador + "', '" + strIpHost + "')";

			pstmt = conn.prepareStatement(strQuery);
			pstmt.execute();
			
			//Guarda Log
			LogCajaService.CreateLog("verificarFactura", "QRY", strQuery);
			
			strResultado[0] = "S";
			strResultado[1] = strNoCaja;
			strResultado[2] = strCodFactura;
			strResultado[3] = strCuota;
			strResultado[4] = strTipoFactura;
			strResultado[5] = strUsuario;
			strResultado[6] = strNavegador;
			strResultado[7] = strIpHost;
			

		}
		catch(Exception e)
		{			
			//Guarda Log
			LogCajaService.CreateLog("verificarFactura", "ERR", e.getMessage());
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
	 * @author luisfonseca
	 * @param strNoCaja
	 * @param strCodCliente
	 * @param strCodFactura
	 * @param strTipoFactura
	 * @param strCuota
	 * @param strCajero
	 * @return
	 * @throws Exception
	 */
	public String[] actualizarVerificarFactura(String strNoCaja,
									 String strCodCliente, 
									 String strCodFactura,
									 String strTipoFactura,
									 String strCuota,
									 String strCajero)
	{
		String[] strResultado = new String[] {"N", "", "", "", ""};
		
		String DRIVER = "com.ibm.as400.access.AS400JDBCDriver"; 
		String URL = "jdbc:as400:" + PropertiesSystem.IPSERVERDB2 + "/" + PropertiesSystem.ESQUEMA + ";prompt=false;translate binary=true";
		Connection conn = null;
		
		
		try
		{
			Class.forName(DRIVER); 
			conn = DriverManager.getConnection(URL, PropertiesSystem.CN_USRNAME, PropertiesSystem.CN_USRPWD); 

			
			String strQuery = "update " + PropertiesSystem.ESQUEMA + ".cajafac set estado = 0 "
								   + "where codcliente = " + strCodCliente + " and "
								   		 + "nofactura = " + strCodFactura + " and "
								   		 + "partida = '" + strCuota + "'  and "
								   		 + "tipofactura = '" + strTipoFactura + "' and "
								   		 + "estado = 1 and fecha = current date";

			LogCajaService.CreateLog("actualizarVerificarFactura", "QRY", strQuery);
			
			PreparedStatement pstmt = conn.prepareStatement(strQuery);
			pstmt.execute();
			
			strResultado[0] = "S";
			strResultado[1] = strNoCaja;
			strResultado[2] = strCodFactura;
			strResultado[3] = strCuota;
			strResultado[4] = strTipoFactura;
			
//			return strResultado;

		}
		catch(Exception e)
		{
			LogCajaService.CreateLog("actualizarVerificarFactura", "ERR", e.getMessage());
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
}

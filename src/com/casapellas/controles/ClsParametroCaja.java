package com.casapellas.controles;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


import com.casapellas.entidades.CajaParametro;
import com.casapellas.util.PropertiesSystem;

import ni.com.casapellas.util.net.DB2Connection;


/**
 * 
 * @author Luis Fonseca
 *
 */
public class ClsParametroCaja {
	
	/**
	 * @author luisfonseca
	 * @param strCodParam
	 * @param strNoCaja
	 * @param strCodigo
	 * @return
	 * @throws Exception
	 */
	public List<CajaParametro> getParametrosCaja(String strCodParam,
												 String strNoCaja,
												 String strCodigo) throws Exception
	{
		List<CajaParametro> lstMC = new ArrayList<CajaParametro>();

		String DRIVER = "com.ibm.as400.access.AS400JDBCDriver"; 
		String URL = "jdbc:as400:" + PropertiesSystem.IPSERVERDB2 + "/" + PropertiesSystem.ESQUEMA + ";prompt=false;translate binary=true";
		Connection conn = null;
		
		try
		{
			Class.forName(DRIVER); 
			conn = DriverManager.getConnection(URL, PropertiesSystem.CN_USRNAME, PropertiesSystem.CN_USRPWD); 
			
			String strQuery = "";
			String strWhere = "";
			
			if(strCodParam.isEmpty())
				throw new Exception("Codigo de parametro es obligatorio, error de logica, favor contactar con informatica");
			
			strWhere = "Where tparm = '" + strCodigo + "'"; 
			
			if(!strNoCaja.isEmpty())
				strWhere += " and caid = " + strNoCaja;
			
			if(!strNoCaja.isEmpty())
				strWhere += " and tcod = " + strCodigo;

			strQuery = "select tparm codParametro, " +
							  "caid noCaja, " +
							  "tcod codigo, " +
							  "tdesc descripcion, " +
							  "tvalnum valorNumerico, " +
							  "tvalalf valorAlfanumerico " +
					   "from " + PropertiesSystem.ESQUEMA + ".cajaparm  " + strWhere + " " +
					   "fetch first 5000 rows only  ";

			PreparedStatement pstmt = conn.prepareStatement(strQuery);
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				CajaParametro cp = new CajaParametro();
				
				cp.setCodigo(rs.getString("codigo"));
				cp.setCodParametro(rs.getString("codParametro"));
				cp.setDescripcion(rs.getString("descripcion"));
				cp.setNoCaja(rs.getInt("noCaja"));
				cp.setValorNumerico(rs.getDouble("valorNumerico"));
				cp.setValorAlfanumerico(rs.getString("valorAlfanumerico"));
				
				lstMC.add(cp);
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new Exception("Error al momento de realizar la consulta de Maestro de paramteros de caja");
		}
		
		return lstMC;
	}
	
	/**
	 * @author luisfonseca
	 * @param strTipoParametro
	 * @param strNumeroCaja
	 * @param strCodigoParm
	 * @return
	 * @throws Exception
	 */
	public CajaParametro getParametros(String strTipoParametro, String strNumeroCaja, String strCodigoParm) throws Exception
	{
		DB2Connection dbC = new DB2Connection();
		Connection conn = null;
		CajaParametro cp = new CajaParametro();
		try
		{
			
			conn =  dbC.openConnection(PropertiesSystem.IPSERVERDB2, 
									   PropertiesSystem.ESQUEMA,
									   PropertiesSystem.CN_USRNAME,
									   PropertiesSystem.CN_USRPWD);
			
			String strQuery = "select  tparm codParametro," + 
									  "caid noCaja," + 
									  "tcod codigo," + 
									  "tdesc descripcion," + 
									  "tvalnum valorNumerico," + 
									  "tvalalf valorAlfanumerico " +
							  "from cajaparm where tparm = '" + strTipoParametro + "' and " +
							  					  "caid = " + strNumeroCaja + " and "
							  					+ "tcod = '" + strCodigoParm + "' ";
			
			System.out.println(strQuery);
			ResultSet rs = dbC.executeQuery(conn, strQuery);
			
			if(rs==null) throw new Exception("No esta configurado los parametros solicitados");
			
			while (rs.next()) {
				cp.setCodigo(rs.getString("codigo"));
				cp.setCodParametro(rs.getString("codParametro"));
				cp.setDescripcion(rs.getString("descripcion"));
				cp.setNoCaja(rs.getInt("noCaja"));
				cp.setValorNumerico(rs.getDouble("valorNumerico"));
				cp.setValorAlfanumerico(rs.getString("valorAlfanumerico"));
				
				break;
			}
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		finally
		{
			try
			{
			dbC.closedConnection(conn);
			}
			catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		return cp;
	}
	
	/**
	 * @author luisfonseca
	 * @param strTipoParametro
	 * @param strNumeroCaja
	 * @param strCodigoParm
	 * @return
	 * @throws Exception
	 */
	public boolean setParametros(String strTipoParametro, 
									   String strNumeroCaja, 
									   String strCodigoParm,
									   String strDescripcion,
									   Integer intValorNumerico,
									   String strValorAlfaNumerico)
	{
		DB2Connection dbC = new DB2Connection();
		Connection conn = null;
		try
		{
			
			conn =  dbC.openConnection(PropertiesSystem.IPSERVERDB2, 
									   PropertiesSystem.ESQUEMA,
									   PropertiesSystem.CN_USRNAME,
									   PropertiesSystem.CN_USRPWD);
			
			
			if(strDescripcion==null && intValorNumerico==null && strValorAlfaNumerico==null)
				throw new Exception("Para el metodo setParametros de la Clase ParametroCaja se necesita al "
						+ "menos un valor en algunos de los campos, descripcion, valor numerico o valor alfanumerico");
				
			String strQuery = "update cajaparm set " +
									  //(strDescripcion==null ? "" : "tdesc = " + strDescripcion) + 
									  (intValorNumerico==null ? "" : "tvalnum = " + intValorNumerico) + 
									 // (strValorAlfaNumerico==null ? "" : "tvalalf = " + strValorAlfaNumerico) + 
							  " where tparm = '" + strTipoParametro + "' and " +
							  					  "caid = " + strNumeroCaja + " and "
							  					+ "tcod = '" + strCodigoParm + "'";
			
			System.out.println(strQuery);
			dbC.executeUpdate(conn, strQuery);
			
			return true;
			
		}
		catch(Exception e)
		{
			return false;
		}
		finally
		{
			dbC.closedConnection(conn);
		}
		
	}
}

package com.casapellas.util;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Criteria;
import org.hibernate.engine.spi.LoadQueryInfluencers;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.internal.SessionImpl;
import org.hibernate.loader.OuterJoinLoader;
import org.hibernate.loader.criteria.CriteriaLoader;
import org.hibernate.loader.criteria.CriteriaQueryTranslator;
import org.hibernate.persister.entity.OuterJoinLoadable;

import com.casapellas.entidades.ens.Vautoriz;
import com.google.gson.Gson;

public class LogCajaService {
	private static Connection connection = null;

	private static Connection Create() {
		try {
			Class.forName("com.ibm.as400.access.AS400JDBCDriver");

			return DriverManager.getConnection("jdbc:as400://" + PropertiesSystem.IPSERVERDB2 + "/QS36F;prompt=false",
					PropertiesSystem.CN_USRNAME, PropertiesSystem.CN_USRPWD);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static Connection getConnection() {
		try {
			if (connection != null && !connection.isClosed()) {
				return connection;
			}

			return Create();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void CreateLog(String pMetodo, String pTipo, String pContenido, String pContenidoAd) {
		try {

			HttpServletRequest request= CodeUtil.getCurrentRequest();
			String computerName = request == null ? "" : request.getRemoteHost();			
			String userAgent = request == null ? "" : CodeUtil.getUserAgent();


			Date dt = new Date();
			Integer intDate = CastDateToNumber(dt, "yyyyMMdd");
			Integer intTime = CastTimeToNumber(dt);
			
			InsertLog(
					getUserFromSession(),
					intDate, 
					intTime, 
					sanitizeString(pMetodo), 
					sanitizeString(pTipo), 
					sanitizeString(pContenido), 
					sanitizeString(pContenidoAd),  
					fullFormatedDate(), 
					sanitizeString(computerName), 
					sanitizeString(userAgent));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getTimeInf() {
		  Date date = new Date();
		  Timestamp dateInf = new Timestamp(date.getTime());
		  return dateInf.toString();
	}
	
	public static void CreateLog(String pMetodo, String pTipo, String pContenido) {
		try {
			
			HttpServletRequest request= CodeUtil.getCurrentRequest();
			String computerName = request == null ? "" : request.getRemoteHost();			
			String userAgent = request == null ? "" : CodeUtil.getUserAgent();

			Date dt = new Date();
			Integer intDate = CastDateToNumber(dt, "yyyyMMdd");
			Integer intTime = CastTimeToNumber(dt);

			InsertLog(
					getUserFromSession(),
					intDate, 
					intTime, 
					sanitizeString(pMetodo), 
					sanitizeString(pTipo), 
					sanitizeString(pContenido), 
					"", 
					fullFormatedDate(), 
					sanitizeString(computerName), 
					sanitizeString(userAgent));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void CreateLog(String pMetodo, String pTipo, Object pEntity) {
		try {

			Date dt = new Date();
			Integer intDate = CastDateToNumber(dt, "yyyyMMdd");
			Integer intTime = CastTimeToNumber(dt);
			String jsonObj = toJson(pEntity);
			HttpServletRequest request= CodeUtil.getCurrentRequest();
			String computerName = request == null ? "" : request.getRemoteHost();			
			String userAgent = CodeUtil.getUserAgent();
			
			InsertLog(
					getUserFromSession(),
					intDate, 
					intTime, 
					sanitizeString(pMetodo), 
					sanitizeString(pTipo), 
					sanitizeString(jsonObj), 
					"", 
					fullFormatedDate(), 
					sanitizeString(computerName), 
					sanitizeString(userAgent));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void InsertLog(String pLogUsuario, Integer pLogFecha, Integer pLogHora, String pLogMetodo, String pLogTipo, String pLogContenido, String pLogContenido2, String pFechaCompleta, String pDispositivo, String pPrograma) {
		try {
			connection = getConnection();
			
			String strQuery = "INSERT INTO " + PropertiesSystem.ESQUEMA + ".LOG_CAJA(LOGUSUARIO, LOGFECHA, LOGHORA, LOGMETODO, LOGTIPO, LOGCONTENIDO, LOGCONTENIDO2, FECHACOMPLETA, DISPOSITIVO, PROGRAMA) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
			PreparedStatement stmt = connection.prepareStatement(strQuery);
			stmt.setString(1, pLogUsuario);
			stmt.setInt(2, pLogFecha);
			stmt.setInt(3, pLogHora);
			stmt.setString(4, pLogMetodo);
			stmt.setString(5, pLogTipo);
			stmt.setString(6, pLogContenido == null ? "" : pLogContenido);
			stmt.setString(7, pLogContenido2 == null ? "" : pLogContenido2);
			stmt.setString(8, pFechaCompleta);
			stmt.setString(9, pDispositivo);
			stmt.setString(10, pPrograma);
			
			stmt.executeUpdate();
			
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* Estos metodos fueron implementados en otra clase para separar funcionalidad
	public static void insertarLogReciboNumerico(int noCaja,int noRecibo,String codCompania,String codSucursal,String tipoRecibo) {
		try {
			connection = getConnection();

			Object objSec = CodeUtil.getFromSessionMap("sevAut");
			Vautoriz vaut = ((Vautoriz[]) objSec)[0];

			Date dHora = new Date();
			SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

			String insertQuery= "INSERT INTO "+PropertiesSystem.ESQUEMA+".BITACORA_SECUENCIA_RECIBO"
					+ "(NO_CAJA,NO_RECIBO,COD_COMPANIA,TIPO_RECIBO,COD_SUCURSAL,COD_USUARIO,FECHA_CREACION,HORA_CREACION,MOTIVO,ESTADO)"
					+ " VALUES("+
					noCaja+","+noRecibo+",'"+codCompania+"','"+tipoRecibo+"','"+codSucursal+"','"+vaut.getId().getLogin()+"',TO_DATE('"+sdf.format(dHora)+"','dd/mm/YYYY'),'"+dfHora.format(dHora)+"',"+
					"'ERROR','ERROR DEL SISTEMA' )";
			
			ExecuteCommand(insertQuery);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void actualizarSatisfactorioLogReciboNumerico(int noCaja,int noRecibo,String codCompania,String codSucursal,String tipoRecibo) {
		try {
			connection = getConnection();

			Date dHora = new Date();
			SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			String insertQuery= "UPDATE  "+PropertiesSystem.ESQUEMA+".BITACORA_SECUENCIA_RECIBO " + 
					"SET " + 
					"FECHA_MODIFICACION = TO_DATE('"+sdf.format(dHora)+"','dd/mm/YYYY')," + 
					"HORA_MODIFICACION = '"+dfHora.format(dHora)+"'," + 
					"ESTADO='SATISFACTORIO'," + 
					"MOTIVO = 'SATISFACTORIO' " + 
					"WHERE " + 
					"NO_CAJA = "+noCaja+" AND " + 
					"NO_RECIBO = "+noRecibo+" AND " + 
					"COD_COMPANIA = '"+codCompania+"' AND " + 
					"TIPO_RECIBO = '"+tipoRecibo+"'";
			
			
			ExecuteCommand(insertQuery);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	*/
	
	public static String toJson(Object obj) {
		return new Gson().toJson(obj);
	}

	@SuppressWarnings("rawtypes")
	public static String toSql(Criteria criteria) {
		String sql = "";
		Object[] parameters = null;

		try {
			CriteriaImpl criteriaImp = (CriteriaImpl) criteria;
			SessionImpl sessionImpl = (SessionImpl) criteriaImp.getSession();
			SessionFactoryImplementor factory = sessionImpl.getSessionFactory();
			String[] implementors = factory.getImplementors(criteriaImp.getEntityOrClassName());
			OuterJoinLoadable persister = (OuterJoinLoadable) factory.getEntityPersister(implementors[0]);
			LoadQueryInfluencers loadQueryInfluencers = new LoadQueryInfluencers();
			CriteriaLoader loader = new CriteriaLoader(persister, factory, criteriaImp, implementors[0].toString(),
					loadQueryInfluencers);

			Field f = OuterJoinLoader.class.getDeclaredField("sql");
			f.setAccessible(true);
			sql = (String) f.get(loader);
			Field fp = CriteriaLoader.class.getDeclaredField("translator");
			fp.setAccessible(true);

			CriteriaQueryTranslator translator = (CriteriaQueryTranslator) fp.get(loader);
			parameters = translator.getQueryParameters().getPositionalParameterValues();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (sql != null) {
			int fromPosition = sql.indexOf(" from ");
			sql = "\nSELECT * " + sql.substring(fromPosition);

			if (parameters != null && parameters.length > 0) {
				for (Object val : parameters) {
					String value = "%";
					if (val instanceof Boolean) {
						value = ((Boolean) val) ? "1" : "0";
					} else if (val instanceof String) {
						value = "'" + val + "'";
					} else if (val instanceof Number) {
						value = val.toString();
					} else if (val instanceof Class) {
						value = "'" + ((Class) val).getCanonicalName() + "'";
					} else if (val instanceof Date) {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
						value = "'" + sdf.format((Date) val) + "'";
					} else if (val instanceof Enum) {
						value = "" + ((Enum) val).ordinal();
					} else {
						value = val.toString();
					}
					sql = sql.replaceFirst("\\?", value);
				}
			}
		}
		return sql.replaceAll("left outer join", "\nleft outer join").replaceAll(" and ", "\nand ")
				.replaceAll(" on ", "\non ").replaceAll("<>", "!=").replaceAll("<", " < ").replaceAll(">", " > ");
	}

	private static void ExecuteCommand(String strQuery) {
		try {
			Statement stmt = connection.createStatement();

			stmt.execute(strQuery);

			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Integer CastDateToNumber(Date dt, String format) {
		String strDate = CastDateToString(dt, format);

		return Integer.parseInt(strDate);
	}

	private static String CastDateToString(Date dt, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);

		return formatter.format(dt);
	}

	private static Integer CastTimeToNumber(Date dt) {
		SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
		return Integer.parseInt(sdf.format(dt));
	}
	
	private static String fullFormatedDate() {
		SimpleDateFormat formatterStandar = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
		Date date = new Date();		
		
		return formatterStandar.format(date);		
	}

	private static String sanitizeString(String str) {
		return str;
		// return str.replace("'", "''");
	}
	
	private static String getUserFromSession() {
		String usuario = "";
		try {
			Object objSec = CodeUtil.getFromSessionMap("sevAut");
			if(objSec != null) {
				Vautoriz vaut = ((Vautoriz[]) objSec)[0];
				usuario = vaut.getId().getLogin(); 
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return usuario;
	}
}

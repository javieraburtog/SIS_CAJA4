package com.casapellas.controles;
/**
 * CASA PELLAS S.A.
 * Creado por.........: Juan Carlos �amendi Pineda
 * Fecha de Creaci�n..: 28/02/2009
 * �ltima modificaci�n: 11/10/2011
 * Modificado por.....:	Juan Carlos �amendi Pineda
 * 
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.casapellas.entidades.Cafiliados;
import com.casapellas.entidades.F55ca03;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.navegacion.As400Connection;
import com.casapellas.navegacion.SqlServerConnection;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.PropertiesSystem;


public class AfiliadoCtrl {
	
	
	public static List<SelectItem> obtenerTiposMarcaTarjetas(){
		List<SelectItem> marcas = new ArrayList<SelectItem>();
		
		try {
			
			
			String strSql = "select CAST(TRIM(F0005.DRKY) AS VARCHAR(10) CCSID 37) DRKY, " +
					"CAST(TRIM(F0005.drdl01) AS VARCHAR(30) CCSID 37) drdl01  from "+
					PropertiesSystem.JDECOM+".f0005 F0005 where F0005.DRSY = '55CA' AND F0005.DRRT = 'TA' "; 
			
			@SuppressWarnings("unchecked")
			List<Object[]> dtaTiposTarjetas =  (ArrayList<Object[]>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSql, true, null ) ;

			if( dtaTiposTarjetas == null )
				return new ArrayList<SelectItem>();
			
			for (Object[] dtaTipo  : dtaTiposTarjetas) {
				marcas.add(  new SelectItem( String.valueOf(dtaTipo[0]) + "@" + String.valueOf( dtaTipo[1] ), 
						CodeUtil.capitalize( String.valueOf( dtaTipo[1] ) ) ) ) ;
			}
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return marcas;
	}
	
	
	/******************************************************************************************************/
	public String getTerminalSocketPos(String sAfiliado){
		Connection cn = null;
		PreparedStatement ps = null;
		String sql = "", sTerminal = "";
		ResultSet rs = null;
		try{
			As400Connection as400connection = new As400Connection();
			cn = as400connection.getJNDIConnection("DSMCAJAR");		 
			sql = "select term_id from "+PropertiesSystem.ESQUEMA+".term_afl where cast(card_acq_id as numeric(15)) = "+sAfiliado+" and status = 'A'";
				
			ps = cn.prepareStatement(sql);		
			rs = ps.executeQuery();
			if (rs.next()){
				sTerminal =  rs.getString("term_id");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally {
			try {
				ps.close();
				cn.close();
			} catch (Exception se2) {
				se2.printStackTrace();
			}
		}
		return sTerminal;
	}
/******************************************************************************************************/
	public List<Cafiliados> getTerminalSocketPos(Cafiliados[] cafiliado){
		SqlServerConnection sqlServer = new SqlServerConnection();
		Connection cn = null;
		PreparedStatement ps = null;
		String sql = "";
		ResultSet rs = null;
		List<Cafiliados> lstCafiliados = new ArrayList<Cafiliados>();
		try{
			 //As400Connection as400connection = new As400Connection();
			 //cn = as400connection.getJNDIConnection("DSMCAJAR");
			cn = sqlServer.getConnection("sa", "Sqlserver2008", "SOCKET_POS");
			  
			 for(int i = 0;i < cafiliado.length;i++){				
				 //sql = "select term_id from crpmcaja.term_afl where cast(card_acq_id as numeric(15)) = "+cafiliado[i].getId().getC6afil()+" and status = 'A'";
				 sql = "select term_id from [SOCKET_POS].[dbo].term_afl  where cast(card_acq_id as numeric(15)) = "+cafiliado[i].getId().getC6afil().trim()+" and status = 'A'";
				 ps = cn.prepareStatement(sql);		
				 rs = ps.executeQuery();
				 if (rs.next()){
					 cafiliado[i].setTermid( rs.getString("term_id"));
					 lstCafiliados.add(cafiliado[i]);
				 }
			 }
		}catch(Exception ex){
			ex.printStackTrace();
		}finally {
			try {
				ps.close();
				cn.close();
			} catch (Exception se2) {
				se2.printStackTrace();
			}
		}
		return lstCafiliados;
	}
/******************************************************************************************************/
/** M�todo: Buscar un afiliado para una caja
 *	Fecha:  24/09/2010
 *  Nombre: Carlos Manuel Hern�ndez Morrison.
 **/
	public Cafiliados buscarPOSxCajaCompania(int iCodPos,int iCaid,String sCodcomp,String sMoneda){
		Session sesion = null;
		
		Cafiliados pos = null;
		String sql = "";
	   
	    try {   
	    	sql  = " from Cafiliados ca where ca.id.c6stat='A'";
	    	sql += " and ca.id.c6id = "+iCaid+" and trim(ca.id.c6rp01) = '"+sCodcomp.trim()+"'";
	    	sql += " and ca.id.d7crcd = '"+sMoneda+"' and ca.id.cxcafi =  '" +iCodPos +"' "; 
	    	
	    	sesion = HibernateUtilPruebaCn.currentSession();
	    	
	    	List result = sesion.createQuery(sql).list();			  
			
			   	
	    	
			if(result != null && result.size()>0) 				
				pos = (Cafiliados)result.get(0);
			result.clear();
	    }catch(Exception ex){
	    	ex.printStackTrace();
		}
		return pos;
	}
/**************************************************************************************/
/** 		Obtener los datos de un afiliado a partir de su c�digo					**/
	public static F55ca03 obtenerAfiliadoxId(String sCodAfiliado, String codcomp){
		 
		F55ca03 f03 = null;
		
		try {
		
			String sql = "from F55ca03 f where trim(f.id.cxcafi) = '"+sCodAfiliado.trim()+"' and trim(f.id.cxrp01) = '" +codcomp.trim()+ "' and f.id.cxstat = 'A'";
			
			f03 = ConsolidadoDepositosBcoCtrl.executeSqlQueryUnique(sql, F55ca03.class, false);
			
		} catch (Exception error) {
			f03 = null;
			error.printStackTrace(); 
		} 
		return f03;
	} 
	public static F55ca03 obtenerAfiliadoxId(String sCodAfiliado, String codcomp, Session session){
		 
		F55ca03 f03 = null;
		
		try {
		
			String sql = "from F55ca03 f where trim(f.id.cxcafi) = '"+sCodAfiliado.trim()+"' and trim(f.id.cxrp01) = '" +codcomp.trim()+ "' and f.id.cxstat = 'A'";
			f03 = (F55ca03) session.createQuery(sql).uniqueResult();
			
		} catch (Exception error) {
			f03 = null;
			error.printStackTrace(); 
		} 
		return f03;
	} 
	
	
	
/***************OBTIENE LOS AFILIADOS DE LA CAJA POR CODIGO DE CAJA Y CODIGO DE COMPANIA******************/
	public Cafiliados[] obtenerAfiliadoxCaja_Compania(int iCaId, String sCodComp,String sMoneda){
		
	    Session session = HibernateUtilPruebaCn.currentSession();
	    Cafiliados[] afiliados = null;
	    try {    	
	    	List result = (List) session
				.createQuery("from Cafiliados ca where ca.id.c6stat='A' and ca.id.c6id = "+iCaId+" and trim(ca.id.c6rp01) = '"+sCodComp.trim()+"' and ca.id.d7crcd = '"+sMoneda+"'")
				.list();				  
				
			if(result != null) {				
				afiliados = new Cafiliados[result.size()];
				
				for(int i=0; i<result.size(); i++) {
					afiliados[i] = (Cafiliados) result.get(i);
				}
			}					
	    }catch(Exception ex){
	    	ex.printStackTrace();
			
		}
		return afiliados;
	}
	
	
	@SuppressWarnings("unchecked")
	public Cafiliados[] obtenerAfiliadoxCaja_Compania(int caid, String codcomp, final String linea, String moneda ){
		Session sesion = null; 
		Cafiliados[] afiliados = null;
		
		try {
			
			sesion = HibernateUtilPruebaCn.currentSession();
					
			List<Cafiliados> cafils = (ArrayList<Cafiliados>) sesion
					.createCriteria(Cafiliados.class)
					.add(Restrictions.eq("id.c6stat", 'A'))
					.add(Restrictions.eq("id.c6id", caid))
					.add(Restrictions.eq("id.c6rp01", codcomp))
					.add(Restrictions.eq("id.d7crcd", moneda)).list();
			
			List<Cafiliados> afilSinLinea = (ArrayList<Cafiliados>) CollectionUtils
					.select(cafils, new Predicate() {
						public boolean evaluate(Object aflParam) {
							return ((Cafiliados) aflParam).getId().getC6rp07()
									.trim().isEmpty();
						}
					});
			List<Cafiliados> afilConLinea = (ArrayList<Cafiliados>) CollectionUtils
					.select(cafils, new Predicate() {
						public boolean evaluate(Object aflParam) {
							return ((Cafiliados) aflParam).getId().getC6rp07()
									.trim().compareTo(linea.trim()) == 0;
						}
					});
			afiliados =  new Cafiliados[afilSinLinea.size()];
			afiliados = afilSinLinea.toArray(afiliados);
			if(!linea.trim().isEmpty() && !afilConLinea.isEmpty()){
				afiliados =  new Cafiliados[afilConLinea.size()];
				afiliados = afilConLinea.toArray(afiliados);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			afiliados = null;
		}
		
		return afiliados;
	}
/****************************************************************************************************/
/***************OBTIENE LOS AFILIADOS DE LA CAJA POR CODIGO DE CAJA Y CODIGO DE COMPANIA******************/
	public Cafiliados[] obtenerAfiliadoxCaja_Compania(int iCaId, String sCodComp){		
	    Session session = HibernateUtilPruebaCn.currentSession();
	    Cafiliados[] afiliados = null;
	    try {    	
	    	List result = (List) session
				.createQuery("from Cafiliados ca where ca.id.c6stat='A' and ca.id.c6id = :pCaja and ca.id.c6rp01 = :pCom")
				.setParameter("pCaja",iCaId)
				.setParameter("pCom", sCodComp.trim())
				.list();				  
				
			if(result != null) {				
				afiliados = new Cafiliados[result.size()];
				
				for(int i=0; i<result.size(); i++) {
					afiliados[i] = (Cafiliados) result.get(i);
				}
			}					
	    }catch(Exception ex){
	    	afiliados = null;
	    	ex.printStackTrace();
		}
		return afiliados;
	}
/****************************************************************************************************/
	
	/**************************************************************************************/
	/** 		Obtener los datos de un afiliado a partir de su c�digo					**/
		public static F55ca03 obtenerAfiliadoxId( String sCodAfiliado ){
			 
			F55ca03 f03 = null;
			
			try {
				 
				String sql = " select * from " + PropertiesSystem.ESQUEMA +".F55ca03 where trim(cxcafi) = '"+sCodAfiliado.trim()+"' "; 
				
				@SuppressWarnings("unchecked")
				List<F55ca03> afiliados = (ArrayList<F55ca03>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, true, F55ca03.class );		
			 
				if( afiliados == null || afiliados.isEmpty() )
					return null;
				
				 return f03 = afiliados.get(0);
				
			} catch (Exception error) {
				f03 = null;
				error.printStackTrace();
			}  
			
			return f03;
		} 
		
}

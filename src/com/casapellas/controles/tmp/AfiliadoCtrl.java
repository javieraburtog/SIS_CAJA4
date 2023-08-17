package com.casapellas.controles.tmp;
/**
 * CASA PELLAS S.A.
 * Creado por.........: Juan Carlos Ñamendi Pineda
 * Fecha de Creación..: 28/02/2009
 * Última modificación: 11/10/2011
 * Modificado por.....:	Juan Carlos Ñamendi Pineda
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
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.casapellas.controles.ConsolidadoDepositosBcoCtrl;
import com.casapellas.entidades.Cafiliados;
import com.casapellas.entidades.F55ca03;
import com.casapellas.entidades.TermAfl;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.navegacion.As400Connection;
import com.casapellas.navegacion.SqlServerConnection;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.PropertiesSystem;


public class AfiliadoCtrl {
	
	public static List<SelectItem> obtenerTiposMarcaTarjetas(){
		List<SelectItem> marcas = new ArrayList<SelectItem>();
		
		try {
			
			
			String strSql = "select CAST(TRIM(F0005.DRKY) AS VARCHAR(10) CCSID 37) DRKY, CAST(TRIM(F0005.drdl01) AS VARCHAR(30) CCSID 37) drdl01  from "+PropertiesSystem.JDECOM+".f0005 F0005 where F0005.DRSY = '55CA' AND F0005.DRRT = 'TA' "; 
			
			@SuppressWarnings("unchecked")
			List<Object[]> dtaTiposTarjetas =  (ArrayList<Object[]>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSql, true, null ) ;

			if( dtaTiposTarjetas == null )
				return new ArrayList<SelectItem>();
			
			
			for (Object[] dtaTipo  : dtaTiposTarjetas) {
				
				marcas.add(  new SelectItem( String.valueOf(dtaTipo[0]) + "@" + String.valueOf( dtaTipo[1] ), CodeUtil.capitalize( String.valueOf( dtaTipo[1] ) ) ) ) ;
				
/*				marcas.add(new SelectItem("MC@Mastercard", "Mastercard")) ;
				marcas.add(new SelectItem("VS@VISA", "VISA")) ;
				marcas.add(new SelectItem("AMEX@American Express", "American Express")) ;
				marcas.add(new SelectItem("DNC@Diners Club", "Diners Club")) ;*/
				
			}
			
			
		} catch (Exception e) {
			LogCajaService.CreateLog("obtenerTiposMarcaTarjetas", "ERR", e.getMessage());
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
			cn = As400Connection.getJNDIConnection("DSMCAJAR");		 
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
	
	public static List<Cafiliados> getTerminalSocketPos(Cafiliados[] afiliados){
		List<Cafiliados> lstTerminales = new ArrayList<Cafiliados>(afiliados.length);
		Session sesion = null;
		Transaction trans = null;
		boolean bNuevaCn = false;
		
		try {
			sesion = HibernateUtilPruebaCn.currentSession();
			if(sesion.getTransaction().isActive())
				trans = sesion.getTransaction();
			else{
				trans = sesion.beginTransaction();
				bNuevaCn = true;
			}
			for (Cafiliados ca : afiliados) {
				
				String termid = (String)sesion.createCriteria(TermAfl.class)
					.setProjection(Projections.property("id.termId"))
					.add(Restrictions.sqlRestriction( "card_Acq_Id like '%"
								+ca.getId().getC6afil().trim()+"%'"))
					.add(Restrictions.eq("id.status", "A"))
					.setMaxResults(1).uniqueResult();
				
				if(termid == null || termid.compareTo("") == 0)
					continue;
				
				ca.setTermid(termid) ;
				lstTerminales.add(ca);
			}
			
		} catch (Exception ex) {
			lstTerminales = new ArrayList<Cafiliados>();
			ex.printStackTrace();
		}finally{
			if(bNuevaCn){
				try{trans.commit();}catch(Exception e){e.printStackTrace();}
				try{HibernateUtilPruebaCn.closeSession(sesion);}
				catch(Exception e){e.printStackTrace();}
			}
			trans = null;
			sesion = null;
		}
		return lstTerminales;
	}
/******************************************************************************************************/
	public List<Cafiliados> getTerminalSocketPos1(Cafiliados[] cafiliado){
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
/** Método: Buscar un afiliado para una caja
 *	Fecha:  24/09/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 **/
	public Cafiliados buscarPOSxCajaCompania(int iCodPos,int iCaid,String sCodcomp,String sMoneda){
		Session sesion = null;
		Transaction trans = null;
		Cafiliados pos = null;
		String sql = "";
	   
	    try {   
	    	sql  = " from Cafiliados ca where ca.id.c6stat='A'";
	    	sql += " and ca.id.c6id = "+iCaid+" and trim(ca.id.c6rp01) = '"+sCodcomp.trim()+"'";
	    	sql += " and ca.id.d7crcd = '"+sMoneda+"' and ca.id.cxcafi =  " +iCodPos; 
	    	
	    	
	    	sesion = HibernateUtilPruebaCn.currentSession();
	    	trans = sesion.beginTransaction();
	    	List result = sesion.createQuery(sql).list();			  
			trans.commit();
			   	
			if(result != null && result.size()>0) 				
				pos = (Cafiliados)result.get(0);
			result.clear();
			
	    }catch(Exception ex){
			ex.printStackTrace();
		}finally{
			try {HibernateUtilPruebaCn.closeSession(sesion); } catch (Exception e2) {e2.printStackTrace(); }
		}
		return pos;
	}
/**************************************************************************************/
/** 		Obtener los datos de un afiliado a partir de su código					**/
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
			LogCajaService.CreateLog("obtenerAfiliadoxId", "ERR", error.getMessage());
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
	public Cafiliados[] obtenerAfiliadoxCaja_Compania(int caid, String codcomp, final String linea, String moneda){
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
			LogCajaService.CreateLog("obtenerAfiliadoxCaja_Compania", "ERR", e.getMessage());
			afiliados = null;
		}

		return afiliados;
	}
	
	/**
	 * No utilizandose a partir de 2014-03-08
	 */
	public Cafiliados[] obtenerAfiliadoxCaja_Compania1(int iCaId, String sCodComp,String sLinea,String sMoneda){
		
	    Session session = HibernateUtilPruebaCn.currentSession();
	    Cafiliados[] afiliados = null;
	    String sql = "";
	    boolean conLinea = false;
	    try {    
			if (sLinea.trim().equals("")) {
				sql = "from Cafiliados ca where ca.id.c6stat='A' and ca.id.c6id = "
						+ iCaId
						+ " and trim(ca.id.c6rp01) = '"
						+ sCodComp.trim()
						+ "' and (trim(ca.id.c6rp07) = '') and ca.id.d7crcd = '"
						+ sMoneda + "'";
			} else {
				sql = "from Cafiliados ca where ca.id.c6stat='A' and ca.id.c6id = "
						+ iCaId
						+ " and trim(ca.id.c6rp01) = '"
						+ sCodComp.trim()
						+ "' and (trim(ca.id.c6rp07) = '"
						+ sLinea.trim()
						+ "') and ca.id.d7crcd = '"
						+ sMoneda
						+ "'";
				conLinea = true;
			}
	    	List result = (List) session.createQuery(sql).list();				  
				
			if(result != null && result.size() > 0) {				
				afiliados = new Cafiliados[result.size()];
				
				for(int i=0; i<result.size(); i++) {
					afiliados[i] = (Cafiliados) result.get(i);
				}
			}else if (conLinea){
				sql = "from Cafiliados ca where ca.id.c6stat='A' and ca.id.c6id = "+iCaId+" and trim(ca.id.c6rp01) = '"+sCodComp.trim()+"' and (trim(ca.id.c6rp07) = '') and ca.id.d7crcd = '"+sMoneda+"'";
				result = (List) session.createQuery(sql).list();	
				if(result != null && result.size() > 0) {				
					afiliados = new Cafiliados[result.size()];
					
					for(int i=0; i<result.size(); i++) {
						afiliados[i] = (Cafiliados) result.get(i);
					}
				}		
			}
			
	    }catch(Exception ex){
			ex.printStackTrace();
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
}

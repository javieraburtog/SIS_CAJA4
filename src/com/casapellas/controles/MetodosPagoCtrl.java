package com.casapellas.controles;
/**
 * CASA PELLAS S.A.
 * Creado por.........: Juan Carlos Ñamendi Pineda
 * Fecha de Creación..: 10/02/2009
 * Última modificación: 04/01/2010
 * Modificado por.....:	Juan Carlos Ñamendi Pineda
 * 
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.casapellas.entidades.MetodosPago;
import com.casapellas.entidades.Metpago;
import com.casapellas.entidades.Vf55ca012;
import com.casapellas.entidades.Vf55ca012Id;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.PropertiesSystem;

public class MetodosPagoCtrl {
	
	public static String EFECTIVO = ".";
	public static String TARJETA = "?";
	public static String CHEQUE = "!";
	public static String DEPOSITO = "N";
	public static String TRANSFERENCIA = "T";
	
	private static String[][] mpagocod = new String[][] { { EFECTIVO, "Efectivo" },
		{ TRANSFERENCIA, "Transferencia" }, { TARJETA, "Tarjeta/Crédito" },
		{ DEPOSITO, "Depósito" }, { CHEQUE, "Cheque" } };
	
	
	@SuppressWarnings("unchecked")
	public static List<MetodosPago> formasPagoConfiguradas(){
		List<MetodosPago> formasPago = new ArrayList<MetodosPago>();
		
		try {
			
			String query =
			" select * from "+PropertiesSystem.ESQUEMA+".metpago where codigo in ( select distinct(c2ryin) from "+PropertiesSystem.ESQUEMA+".f55ca012 ) ";
			
			return formasPago = (List<MetodosPago>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, true, Metpago.class) ;
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}finally{
			if(formasPago == null){
				formasPago = new ArrayList<MetodosPago>();
			}
		}
		return formasPago;
	}
	
	public static Metpago metodoPagoCodigo(final String codigo){
		Metpago metpago = null;
		
		try {
			
			metpago = (Metpago)
				CollectionUtils.find(PropertiesSystem.formasPagoConfiguradas, new Predicate(){
					@Override
					public boolean evaluate(Object o) {
						 return ((Metpago)o).getId().getCodigo().trim().compareTo(codigo.trim()) == 0 ;
					}
			}) ;
			
			return metpago;
			
		} catch (Exception e) {
			 LogCajaService.CreateLog("metodoPagoCodigo", "ERR", e.getMessage());
			 return metpago = null;
		}
		
	}
	
	public static String descripcionMetodoPago(final String codigo) {
		String nombrePago = "";
	
		try {
			
			Metpago metpago =  metodoPagoCodigo( codigo );
			
			nombrePago = (metpago == null)? "Sin Descripción": CodeUtil.capitalize( metpago.getId().getMpago().trim() ) ;
			
			return nombrePago ;
			
		} catch (Exception e) {
			LogCajaService.CreateLog("descripcionMetodoPago", "ERR", e.getMessage());;
			nombrePago = "Sin Clasificación";
		}
		return nombrePago;
	}
	
	
	
	public static void removerPago(ArrayList<MetodosPago>mpRegistrados, final MetodosPago pago){
		try {
			if(mpRegistrados == null)
				mpRegistrados = new ArrayList<MetodosPago>();
			
			CollectionUtils.filter(mpRegistrados, new Predicate() {
				public boolean evaluate(Object mpRegistrados) {
					MetodosPago mp = (MetodosPago) mpRegistrados;
					return !mp.equals(pago);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(mpRegistrados == null)
				mpRegistrados = new ArrayList<MetodosPago>();
		}
	}
/******************************************************************************************/
/** Método: Obtiene metodos de pago configurados para los pagos en caja.
 *	Fecha:  12/08/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	@SuppressWarnings("unchecked")
	public List<Metpago> obtenerMetodosPagoCaja(){
		List<Metpago>lstMetodosPago = null;
		
		Session sesion = HibernateUtilPruebaCn.currentSession();
		
		String sql = "";
		boolean bNuevaSesionENS = false;
		Transaction trans = null;
		
		try {
			sql =  "from Metpago m where m.id.codigo in (select distinct(f12.id.c2ryin) from F55ca012 f12) ";
			
			if( sesion.getTransaction().isActive() )
				trans = sesion.getTransaction();
			else{
				trans = sesion.beginTransaction();
				bNuevaSesionENS = true;
			}
			
			lstMetodosPago = (ArrayList<Metpago>)sesion.createQuery(sql).list();
			
			if(bNuevaSesionENS)
				trans.commit();
			
		} catch (Exception e) {
			lstMetodosPago = null;
			e.printStackTrace();
		}finally{
			try {
				if(bNuevaSesionENS){
					HibernateUtilPruebaCn.closeSession(sesion);
				}
			}
			catch (Exception e) { e.printStackTrace(); }
		}
		return lstMetodosPago;
	}
/**********OBTIENE LOS METODOS DE PAGO CONFIGURADOS POR CAJA Y COMPANIA**********************************************************************/
	public Vf55ca012[] obtenerMetodosPagoxCaja_Compania2(int iCaid,String sCodComp){
		List<Object[]> lstMetodos = null;
	
		Vf55ca012[] f55ca012 = null;
		Vf55ca012Id vf55ca012id = null;
		Object[] row = null;
	
		try{
		 						
			String sql = "select distinct f.id.codigo,f.id.mpago,f.id.orden from Vf55ca012 as f " +
					" where f.id.c2id = "+iCaid +" and f.id.c2rp01 = '"+sCodComp+"' and f.id.c2stat = 'A' " +
					" order by f.id.orden desc";
			
			LogCajaService.CreateLog("obtenerMetodosPagoxCaja_Compania2", "QRY", sql);
			
			lstMetodos = ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, null, false);
			
			f55ca012 = new Vf55ca012[lstMetodos.size()];
			for(int i = 0; i < lstMetodos.size(); i++){
				row = lstMetodos.get(i);
				
				f55ca012[i] = new Vf55ca012(); vf55ca012id = new Vf55ca012Id();
				vf55ca012id.setMpago(row[1].toString());
				vf55ca012id.setCodigo(row[0].toString());
				f55ca012[i].setId(vf55ca012id);
			}
			
			
		}catch(Exception ex){
			ex.printStackTrace();
			LogCajaService.CreateLog("obtenerMetodosPagoxCaja_Compania2", "ERR", ex.getMessage());
		} 
		return f55ca012;
	}
/*******************************************************************************************************************/
/**********OBTIENE LOS CODIGOS DE LOS METODOS DE PAGO CONFIGURADOS POR CAJA Y COMPANIA******************************/
	@SuppressWarnings("unchecked")
	public String[] obtenerMetodosPagoxCaja_Compania(int iCaid,String sCodComp){
		List<Object[]> lstMetodos = null;
		Session session = HibernateUtilPruebaCn.currentSession();
		
		String[] f55ca012 = null;
		try{
			//String sql = "select distinct cast(f.id.c2ryin as string) from F55ca012 as f where f.id.c2id = "+iCaid +" and f.id.c2rp01 = '"+sCodComp+"' and f.id.c2stat = 'A' order by cast(f.id.c2ryin as string) desc";
		
			String sql;
			sql =  " select distinct (f.id.c2ryin),";
			sql += "   (select count(*) from Recibodet r where r.id.caid="+iCaid+" and r.id.tiporec not in ('FCV','DCO') and r.id.mpago = f.id.c2ryin )";
			sql += " from F55ca012 as f where f.id.c2id = "+iCaid +"  and f.id.c2rp01 = '"+sCodComp+"' and f.id.c2stat = 'A'";	
			sql += " order by 2 desc";
			
			lstMetodos = (List<Object[]>)session.createQuery(sql).list();
		
			
			f55ca012 = new String[lstMetodos.size()];
			for(int i = 0; i < lstMetodos.size(); i++){
				f55ca012[i] =  String.valueOf((lstMetodos.get(i))[0]); 
			}
		}catch(Exception ex){
			System.out.print("Excepcion capturada en MetodosPagoCtrl.obtenerMetodosPagoxCaja_Compania: " + ex);
		}
		return f55ca012;
	}
/*******************************************************************************************************************/
/**********OBTIENE LOS METODOS DE PAGO POR EL CODIGO************************************************************/
	@SuppressWarnings("rawtypes")
	public Metpago[] obtenerDescripcionMetodosPago(String sCodMet){
		List lstMetodos = null;
		Session session = HibernateUtilPruebaCn.currentSession();
		
		Metpago[] metpago = null;
		
		try{												
			String sql = "from Metpago as m where m.id.codigo = '"+sCodMet +"'";
			
			LogCajaService.CreateLog("obtenerDescripcionMetodosPago", "QRY", sql);
			
			lstMetodos = (List)session.createQuery(sql).list();
			
			metpago = new Metpago[lstMetodos.size()];
			for(int i = 0; i < lstMetodos.size(); i++){
				metpago[i] = (Metpago)lstMetodos.get(i); 
			}
			
		}catch(Exception ex){
			LogCajaService.CreateLog("obtenerDescripcionMetodosPago", "ERR", ex.getMessage());
			lstMetodos = null;
			ex.printStackTrace();
		}

		return metpago;
	}
/*******************************************************************************************************************/
/**********OBTIENE LOS METODOS DE PAGO POR CAJA, MONEDA y COMPANIA**************************************************/
	@SuppressWarnings("unchecked")
	public String[] obtenerMetodoPagoxCaja_Moneda(int iCaid,String sCodComp, String sCodMon){
		List<Object[]> lstMetodos = null;
		Session session = HibernateUtilPruebaCn.currentSession();
		
		String[] sMetodoPago = null;
		try{
			
			String sql;
			sql =  " select distinct (f.id.c2ryin),";
			sql += "   (select count(*) from Recibodet r where r.id.caid="+iCaid+" and r.id.tiporec not in ('FCV','DCO') and r.id.mpago = f.id.c2ryin )";
			sql += " from F55ca012 as f where f.id.c2id = "+iCaid +"  and f.id.c2rp01 = '"+sCodComp+"' and f.id.c2stat = 'A' and f.id.c2crcd ='"+sCodMon+"'";	
			sql += " order by 2 desc";
			
			lstMetodos = (List<Object[]>)session.createQuery(sql).list();
			
			
			sMetodoPago = new String[lstMetodos.size()];
			for(int i = 0; i < lstMetodos.size(); i++){
				sMetodoPago[i] =  String.valueOf((lstMetodos.get(i))[0]); 
			}

		}catch(Exception ex){
			System.out.print("Excepcion capturada en MetodosPagoCtrl.obtenerMetodoPagoxCaja_Moneda2: " + ex);
		}
		return sMetodoPago;
	}
/**********OBTIENE LOS METODOS DE PAGO POR CAJA, MONEDA y COMPANIA************************************************************/
	public Vf55ca012[] obtenerMetodoPagoxCaja_Moneda2(int iCaid,String sCodComp, String sCodMon){
		List lstMetodos = null;
		Session session = HibernateUtilPruebaCn.currentSession();
		
		Vf55ca012[] f55ca012 = null;
		Vf55ca012Id vf55ca012id = null;
		Object[] row = null;
		try{
										
			String sql = "select distinct f.id.codigo,f.id.mpago,f.id.orden from Vf55ca012 as f where f.id.c2id = "+iCaid +" and f.id.c2rp01 = '"+sCodComp+"' and f.id.c2crcd ='"+sCodMon+"' and f.id.c2stat = 'A' order by f.id.orden desc";
			lstMetodos = (List)session.createQuery(sql).list();
			
			f55ca012 = new Vf55ca012[lstMetodos.size()];
			for(int i = 0; i < lstMetodos.size(); i++){
				row = (Object[])lstMetodos.get(i);
				f55ca012[i] = new Vf55ca012(); vf55ca012id = new Vf55ca012Id();
				vf55ca012id.setMpago(row[1].toString());
				vf55ca012id.setCodigo(row[0].toString());
				f55ca012[i].setId(vf55ca012id); 
			}
		}catch(Exception ex){
			System.out.print("Excepcion capturada en MetodosPagoCtrl.obtenerMetodoPagoxCaja_Moneda2: " + ex);
		}
		return f55ca012;
	}
/****************************************************************************************************************************************/
	public String obtenerDescripcionMetodoPago(String sCodMet){
		Session session = HibernateUtilPruebaCn.currentSession();
		
		Transaction tx = null;
		String metpago = null;
		boolean bNuevaSesionENS = false;
		
		try{
			
			if( session.getTransaction().isActive() )
				tx = session.getTransaction();
			else{
				tx = session.beginTransaction();
				bNuevaSesionENS = true;
			}

			String sql = "select m.id.mpago from Metpago as m where m.id.codigo = '"+sCodMet +"'";
			metpago = (String)session.createQuery(sql).uniqueResult();
			
			if(bNuevaSesionENS)
				tx.commit();
			
		}catch(Exception ex){
			ex.printStackTrace();
		}finally {			
			try {
				if(bNuevaSesionENS){
					HibernateUtilPruebaCn.closeSession(session);
				}
			}
			catch (Exception e) { e.printStackTrace(); }
		}
		return metpago;
	}
}

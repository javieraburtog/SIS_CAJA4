package com.casapellas.controles.tmp;
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

import com.casapellas.controles.ConsolidadoDepositosBcoCtrl;
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
	
	public static String equivalenciaTipoPago(String codigoformapago ){
		
		return 
		codigoformapago.compareTo(".") == 0 ?  MetodosPagoCtrl.EFECTIVO :
		codigoformapago.compareTo("!") == 0 ? MetodosPagoCtrl.CHEQUE:
		codigoformapago.compareTo("?") == 0 ? MetodosPagoCtrl.TARJETA:
		codigoformapago.compareTo("T") == 0 ? MetodosPagoCtrl.TRANSFERENCIA:
		codigoformapago ;
	}
	
	private static String[][] mpagocod = new String[][] { { EFECTIVO, "Efectivo" },
			{ TRANSFERENCIA, "Transferencia" }, { TARJETA, "Tarjeta/Crédito" },
			{ DEPOSITO, "Depósito" }, { CHEQUE, "Cheque" } };

	
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
		boolean bNuevaSesionENS = false;	
		Transaction trans = null;
//		Session sesion = HibernateUtilPruebaCn.currentSessionENS();
		Session sesion = HibernateUtilPruebaCn.currentSession();
		
		String sql = "";
		
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
				if(bNuevaSesionENS)
//					HibernateUtilPruebaCn.closeSessionENS();					
					HibernateUtilPruebaCn.closeSession(sesion);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return lstMetodosPago;
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	public static List<Metpago> obtenerMetodosPagoCaja(int caid, String codcomp){
		List<Metpago>lstMetodosPago = null;

		try {
			String sql =  "select * from "+PropertiesSystem.ESQUEMA+".Metpago m where m.codigo in (select distinct(f12.c2ryin) from "
					+PropertiesSystem.ESQUEMA+".F55ca012 f12 where c2id = "+caid+" and c2rp01 = '"+codcomp+"'  ) ";
			
			lstMetodosPago = (ArrayList<Metpago>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, true, Metpago.class);
			
		} catch (Exception e) {
			lstMetodosPago = null;
			e.printStackTrace(); 
		} 
		
		return lstMetodosPago;
	}
	
/**********OBTIENE LOS METODOS DE PAGO CONFIGURADOS POR CAJA Y COMPANIA**********************************************************************/
	public Vf55ca012[] obtenerMetodosPagoxCaja_Compania2(int iCaid,String sCodComp){
		List lstMetodos = null;
		Session session = null;
		Vf55ca012[] f55ca012 = null;
		Vf55ca012Id vf55ca012id = null;
		Object[] row = null;
	
		try{
			session = HibernateUtilPruebaCn.currentSession();
			String sql = "select distinct f.id.codigo,f.id.mpago,f.id.orden from Vf55ca012 as f where f.id.c2id = "
					+ iCaid
					+ " and f.id.c2rp01 = '"
					+ sCodComp
					+ "' and f.id.c2stat = 'A' order by f.id.orden desc";
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
			System.out.print("Excepcion capturada en MetodosPagoCtrl.obtenerMetodosPagoxCaja_Compania2: " + ex);
		}finally {			
			try {HibernateUtilPruebaCn.closeSession(session); } catch (Exception e2) {e2.printStackTrace(); }
		}
		return f55ca012;
	}
/*******************************************************************************************************************/
/**********OBTIENE LOS CODIGOS DE LOS METODOS DE PAGO CONFIGURADOS POR CAJA Y COMPANIA******************************/
	@SuppressWarnings("unchecked")
	public String[] obtenerMetodosPagoxCaja_Compania(int iCaid,String sCodComp){
		List<Object[]> lstMetodos = null;
		String[] f55ca012 = null;
		
		try{
			
			String sql = " select distinct (f.id.c2ryin),";
			sql += "   (select count(*) from Recibodet r where r.id.caid="
					+ iCaid + " and r.id.tiporec not in ('FCV','DCO') " +
							"and r.id.mpago = f.id.c2ryin )";
			sql += " from F55ca012 as f where f.id.c2id = " + iCaid
					+ "  and f.id.c2rp01 = '" + sCodComp
					+ "' and f.id.c2stat = 'A'";
			sql += " order by 2 desc";
			
			LogCajaService.CreateLog("obtenerMetodosPagoxCaja_Compania", "QRY", sql);
			
			lstMetodos = (List<Object[]>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, false, null);
			
			if(lstMetodos == null || lstMetodos.isEmpty() )
				return null;
			
			f55ca012 = new String[lstMetodos.size()];
			for(int i = 0; i < lstMetodos.size(); i++){
				f55ca012[i] =  String.valueOf((lstMetodos.get(i))[0]); 
			}
		}catch(Exception ex){
			LogCajaService.CreateLog("obtenerMetodosPagoxCaja_Compania", "ERR", ex.getMessage());			
			f55ca012 = null;
		} 
		return f55ca012;
	}
/*******************************************************************************************************************/
/**********OBTIENE LOS METODOS DE PAGO POR EL CODIGO************************************************************/
	public Metpago[] obtenerDescripcionMetodosPago(String sCodMet){
		List lstMetodos = null;
//		Session session = HibernateUtilPruebaCn.currentSessionENS();
		Session session = HibernateUtilPruebaCn.currentSession();
		
		Transaction tx = null;
		Metpago[] metpago = null;
		boolean bNuevaSesionENS = false;	
		
		try{												
			String sql = "from Metpago as m where trim(m.id.codigo) = '"+sCodMet +"'";
			
			if(session.getTransaction().isActive() )
				tx= session.getTransaction();
			else{
				tx = session.beginTransaction();
				bNuevaSesionENS = true;
			}
			
			lstMetodos = (List)session.createQuery(sql).list();
			
			if(bNuevaSesionENS)
				tx.commit();
			
			metpago = new Metpago[lstMetodos.size()];
			for(int i = 0; i < lstMetodos.size(); i++){
				metpago[i] = (Metpago)lstMetodos.get(i); 
			}
		}catch(Exception ex){
			lstMetodos = null;
			System.out.print("Excepcion capturada en MetodosPagoCtrl.obtenerDescripcionMetodosPago: " + ex);
		}finally {			
			try {
				if(bNuevaSesionENS)
//					HibernateUtilPruebaCn.closeSessionENS();
					HibernateUtilPruebaCn.closeSession(session);
			}
			catch (Exception e) { e.printStackTrace(); }
		}
		return metpago;
	}
/*******************************************************************************************************************/
/**********OBTIENE LOS METODOS DE PAGO POR CAJA, MONEDA y COMPANIA**************************************************/
	@SuppressWarnings("unchecked")
	public String[] obtenerMetodoPagoxCaja_Moneda(int iCaid,String sCodComp, String sCodMon){
		List<Object[]> lstMetodos = null;
		Session session = null;
		String[] sMetodoPago = null;
	
		try{
			session = HibernateUtilPruebaCn.currentSession();
			
			String sql =  " select distinct (f.id.c2ryin),";
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
		}finally {			
			try {HibernateUtilPruebaCn.closeSession(session); } catch (Exception e2) {e2.printStackTrace(); }
		}
		return sMetodoPago;
	}
/**********OBTIENE LOS METODOS DE PAGO POR CAJA, MONEDA y COMPANIA************************************************************/
	public Vf55ca012[] obtenerMetodoPagoxCaja_Moneda2(int iCaid,String sCodComp, String sCodMon){
		List lstMetodos = null;
		Session session = null;
		Vf55ca012[] f55ca012 = null;
		Vf55ca012Id vf55ca012id = null;
		Object[] row = null;
		
		try{
			session = HibernateUtilPruebaCn.currentSession();
			
			String sql = "select distinct f.id.codigo,f.id.mpago,f.id.orden " +
					"from Vf55ca012 as f where f.id.c2id = "+ iCaid
					+ " and f.id.c2rp01 = '" + sCodComp + "' and f.id.c2crcd ='"
					+ sCodMon + "' and f.id.c2stat = 'A' order by f.id.orden desc";
			
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
		}finally {			
			try {HibernateUtilPruebaCn.closeSession(session); } catch (Exception e2) {e2.printStackTrace(); }
		}
		return f55ca012;
	}
/****************************************************************************************************************************************/
	public String obtenerDescripcionMetodoPago(String sCodMet){
//		Session session = HibernateUtilPruebaCn.currentSessionENS();
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
			System.out.print("Excepcion capturada en MetodosPagoCtrl.obtenerDescripcionMetodoPago: " + ex);
		}finally {			
			try {
				if(bNuevaSesionENS)
//					HibernateUtilPruebaCn.closeSessionENS();
					HibernateUtilPruebaCn.closeSession(session);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return metpago;
	}
}

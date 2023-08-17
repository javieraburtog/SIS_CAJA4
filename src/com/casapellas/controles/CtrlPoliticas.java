

/**
 * 
 	Creado por: Alberto Zapata
 	Fecha de Cracion: 27/11/2008
 	Ultima Modificacion: 07/04/2009
 	Modificado por: Juan Carlos Ñamendi Pineda
 * 
 */

package com.casapellas.controles;

import java.util.List;

import javax.faces.event.ValueChangeEvent;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.casapellas.entidades.F55ca012;
import com.casapellas.entidades.Recibodet;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.PropertiesSystem;


public class CtrlPoliticas {

	/******* VALIDA MONTO MAXIMO Y MINIMO PERMITIDO EN LA CAJA ********************************************************/
	@SuppressWarnings("unchecked")
	public boolean validarMonto(int iCaId, String sCodComp, String sCodMon,
			String sCodMet, double dMonto) {
		boolean permitido = false;
		Session session = HibernateUtilPruebaCn.currentSession();
		
		String sql = "";
		try {
			dMonto = dMonto * 100;
			sql = "from F55ca012 f where f.id.c2id = " + iCaId
					+ " and f.id.c2rp01 = '" + sCodComp
					+ "' and f.id.c2crcd = '" + sCodMon
					+ "' and f.id.c2ryin = '" + sCodMet
					+ "' and f.id.c2miam <= " + dMonto + " and f.id.c2mxam >= "
					+ dMonto + " and " + dMonto + " > 0";
		
			List<F55ca012> result = (List<F55ca012>) session.createQuery(sql)
					.list();
			if (result != null && !result.isEmpty()) {
				permitido = true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return permitido;
	}
	
	
	/******* VALIDA REFERENCIA BANCARIAS, POR CAJA Y BANCO ********************************************************/
	@SuppressWarnings("unchecked")
	public boolean validarReferenciaBancaria(int iCaId, String sCodBanco, String sReferencia, String sMpago) {
		boolean permitido = false;
		Session session = null;
		String sql = "";
		
		try {
			
			if (GetCajaParm("26", "VALIDA_REF")== 1 )
			{
				session = HibernateUtilPruebaCn.currentSession();
				
				
				sql = "from Recibodet f where " 
						+ " f.id.refer1 = (SELECT fc.id.codb FROM F55ca022 fc WHERE TRIM(fc.id.banco)= '" +sCodBanco.trim() 
						+ "') and f.referencenumber = " + sReferencia +""
						+ " and f.id.mpago = '" + sMpago +"'";
				 
				List<Recibodet> result = (List<Recibodet>) session.createQuery(sql).list();
				
				if (result != null && !result.isEmpty()) {
					permitido = true;
				}
			}
		} catch (Exception ex) {
			 LogCajaService.CreateLog("validarMonto", "ERR", ex.getMessage());
		
		}
		return permitido;
	}
	
	
	//Valida Monto Minimo & Maximo
	public boolean verificarMontos(String scaja, String smon, String scom, String spago, Double dmonto) {
		boolean bmonto = false;
		List result = null;
        Session session = HibernateUtilPruebaCn.currentSession();
                
        try {
        	
//        	System.out.println("Param # " + scaja + " " + smon + " " + scom + " " + spago + " " + dmonto);
        	Query query = session
	        	.createQuery("from F55ca012 pc where pc.id.c2stat='A' and pc.id.c2id = :pCaja and pc.id.c2rp01 = :pCom and pc.id.c2ryin = :pMet and pc.id.c2crcd = :pMon")
	        	.setParameter("pCaja", Integer.valueOf(scaja))
	        	.setParameter("pCom", scom)
	        	.setParameter("pMon", smon)
	        	.setParameter("pMet", spago);        
        	result = query.list();
        	
        	F55ca012[] MetodosPago = null;
        	
        	if(result != null) {
        		MetodosPago = new F55ca012[result.size()]; 
	        	for(int i=0; i<result.size(); i++) {
	        		MetodosPago[i] = (F55ca012) result.get(i);
	        		if(MetodosPago[i].getId().getC2mxam() >= dmonto && MetodosPago[i].getId().getC2miam() <= dmonto) {
	        			bmonto = true;
	        		}
	        	}
	        } 	
        }catch(Exception ex){
//			System.out.println("Excepcion en verificarMontos # " + ex);
        	ex.printStackTrace();
		} finally {			
			try{session.close();}catch(Exception ex2){ex2.printStackTrace();};
		}		
		return bmonto;
	}
	//Montos por la caja
	@SuppressWarnings({ "unused", "rawtypes" })
	public void setMontosCaja(ValueChangeEvent e) {
        Session session = HibernateUtilPruebaCn.currentSession();
       
        try {
        	Query query = session
        	.createQuery("select A.C2MIAM, A.C2MXAM from F55CA012" +
        			" A where C2ID = :pCaja and C2RP01 = :pComp" +
        			" and C2CRCD = :pMon and C2RYIN = :pMet")
        	.setParameter("pCaja", "1")
        	.setParameter("pComp", "E01")
        	.setParameter("pMon", "COR")
        	.setParameter("pMet",  MetodosPagoCtrl.EFECTIVO );        
        	List result = query.list();
        
        }catch(Exception ex){
//			System.out.println("Excepcion en getCajas # " + ex);
        	ex.printStackTrace();
		} finally {			
			try{session.close();}catch(Exception ex2){ex2.printStackTrace();};
		}
	}
	
	public static int GetCajaParm(String tcod, String tparm )
	{
		
		String query="SELECT TVALALF FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = '"+ tcod + "' AND TCOD = '"+ tparm +"' ";     
		
		List<Object> cuenta= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
		
		return Integer.parseInt(cuenta.get(0).toString());		
	}
	
}

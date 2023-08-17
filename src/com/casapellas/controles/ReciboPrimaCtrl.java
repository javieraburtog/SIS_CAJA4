package com.casapellas.controles;
/**
 * CASA PELLAS S.A.
 * Creado por.........: Juan Carlos Ñamendi Pineda
 * Fecha de Creación..: 28/02/2009
 * Última modificación: 06/03/2009
 * Modificado por.....:	Juan Carlos Ñamendi Pineda
 * 
 */
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.casapellas.entidades.F55ca014;
import com.casapellas.entidades.MetodosPago;
import com.casapellas.entidades.Recibo;
import com.casapellas.entidades.ReciboId;
import com.casapellas.entidades.Recibodet;
import com.casapellas.entidades.RecibodetId;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.navegacion.As400Connection;
import com.casapellas.util.Divisas;
import com.casapellas.util.PropertiesSystem;

public class ReciboPrimaCtrl {
	
/******************************************************************************************/
/** Método: Determinar la moneda base de la compañía seleccionada.
 *	Fecha:  16/02/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public F55ca014 buscarF14Session(String sCodcomp,F55ca014[] f014){
		F55ca014 f14 = null;
		
		try {
			for (F55ca014 fComp : f014) {
				if(fComp.getId().getC4rp01().trim().equals(sCodcomp.trim())){
					f14 = fComp;
					break;
				}
			}
		} catch (Exception error) {
			f14 = null;
			error.printStackTrace();
//			System.out.println("===>GCPMCAJA: Error en REciboPrimaCtrl.buscarF14Session "+error );
		}
		return f14;
	}
	
/**********************REGISTRAR EL RECIBO DE PRIMA**********************************************************/
	public boolean registrarRecibo(int iNumRec, int iNumRecm, String sCodComp, double dMontoRec, String sConcepto,Date dFecha,Date dHora,int iCodCli,String sNomCli, String sCajero,int iCaId, String sCodSuc){
		boolean registrado = false;
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = null; 
		try{ 
			tx = session.beginTransaction();
			
			Recibo recibo = new Recibo();
			ReciboId reciboid = new ReciboId();
			
			reciboid.setCaid(iCaId);								//Numero de caja
			reciboid.setCodsuc(sCodSuc);
			reciboid.setNumrec(iNumRec);
			reciboid.setCodcomp(sCodComp.trim());
			reciboid.setTiporec("PR");
			recibo.setId(reciboid);
								
			recibo.setMontoapl(BigDecimal.valueOf(0));			//Monto Aplicar
			recibo.setMontorec(BigDecimal.valueOf(dMontoRec));	//Monto Recibido
			recibo.setConcepto(sConcepto);						//Concept
			recibo.setFecha(dFecha);							//Fecha
			recibo.setHora(dHora);								//Hora
			recibo.setCodcli(iCodCli);							//Cod Cliente
			recibo.setCliente(sNomCli);							//Nombre Cliente
			recibo.setCajero(sCajero);							//Cajero					
			
			recibo.setNumrecm(iNumRecm);
			session.save(recibo);
			
			tx.commit();
			registrado = true;
		}catch(Exception ex){
			System.out.print("se capturo una excepcion en ReciboPrimaCtrl.registrarRecibo: " + ex);
		}finally {			
			session.close();
		}
		return registrado;
	}
/**********************REGISTRAR EL DETALLE DEL RECIBO DE PRIMA******************************************************/
	public boolean registrarDetalleRecibo(int iNumrec, int iNumrecm, String codcomp, List lstMetodosPago, int iCaId, String sCodSuc){
		boolean registrado = false;
		Divisas divisas = new Divisas();
		
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = null; 
		
		MetodosPago mPago = null;
		try{
			tx = session.beginTransaction();
			for (int i = 0; i < lstMetodosPago.size();i++){
				mPago = (MetodosPago)lstMetodosPago.get(i);
				Recibodet recibodet = new Recibodet();
				RecibodetId recibodetid = new RecibodetId();
				
				recibodetid.setNumrec(iNumrec);						//No. Recibo Automatico				
				recibodetid.setCaid(iCaId);
				recibodetid.setCodsuc(sCodSuc);
				recibodetid.setCodcomp(codcomp);					//Compania	
				recibodetid.setMoneda(mPago.getMoneda());			//Moneda			
				recibodetid.setMpago(mPago.getMetodo());			//Metodo
				recibodetid.setRefer1(mPago.getReferencia());
				recibodetid.setRefer2(mPago.getReferencia2());
				recibodetid.setRefer3(mPago.getReferencia3());
				recibodetid.setRefer4(mPago.getReferencia4());
				recibodet.setId(recibodetid);
								
				recibodet.setNumrecm(iNumrecm);					//No. Recibo Manual
				recibodet.setTasa(BigDecimal.valueOf((divisas.formatStringToDouble(mPago.getTasa().toString()))));
				recibodet.setMonto(BigDecimal.valueOf((mPago.getMonto())));
				recibodet.setEquiv(BigDecimal.valueOf((mPago.getEquivalente())));
								
				session.save(recibodet);
			}
			
			tx.commit();
			registrado = true;
		}catch(Exception ex){
			System.out.print("Se capturo una excepcion en ReciboPrimaCtrl.registrarDetalleRecibo: " + ex);
		}finally {			
			session.close();
		}
		return registrado;
	}
/******************************************************************************************************************/
/***************OBTENER ULTIMO NUMERO DE RECIBO DE F55CA014*******************************************************************/
	public int obtenerUltimoRecibo(int iCaid,String sCodComp){		
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = session.beginTransaction();
		int ultimo = 0;	
		try{
			Long result = (Long) session
			.createQuery("SELECT max(f55.id.c4nncu) FROM F55ca014 as f55 where f55.id.c4id = " + iCaid + " and trim(f55.id.c4rp01) = '"+sCodComp.trim()+"'")
			.uniqueResult();
			tx.commit();
			ultimo = result.intValue();
			
		}catch(Exception ex){
			System.out.print("Se capturo una excepcion en obtenerUltimoRecibo: " + ex);
		}finally{
        	session.close();        
		}	
		return ultimo;
	}
/******************************************************************************************************************/
/******************************ACTUALIZA EL NUMERO DE RECIBO EN EL F55CA014************************************************************************************/
	public boolean actualizarNumeroRecibo(int iCajaId, String sCodCom, int iNumRecActual){
		/*boolean actualizado = false;
		Session session = HibernateUtil.getSessionFactoryMCAJA().openSession();
		Transaction tx = session.beginTransaction();
		
		try{
			Vf55ca014 rango = (Vf55ca014) session
			.createQuery("FROM Vf55ca014 f55 WHERE f55.id.c4id = :pCod and f55.id.c4rp01 = :pCom")
			.setParameter("pCod", iCajaId)
			.setParameter("pCom", sCodCom)
			.uniqueResult();
			
			rango.getId().setC4nncu(((long)iNumRecActual));			
			
			session.update(rango);
			tx.commit();	
			if(tx.wasCommitted()){
				System.out.print("actualizado");
			}
			actualizado = true;
		}catch(Exception ex){
			tx.rollback();
			System.out.print("Se capturo una excepcion en el actualizarNumeroRecibo: " + ex);
		}finally{
        	session.close();        
		}	
		return actualizado;*/
		Connection cn = null;
		As400Connection as400connection = new As400Connection();
		
		String sql = "update "+PropertiesSystem.ESQUEMA+".F55ca014 set c4nncu = "+iNumRecActual+" WHERE c4id = "+iCajaId+" and c4rp01 = '"+sCodCom+"'";
		PreparedStatement ps = null;
		boolean actualizado = true;
		try {
			cn = as400connection.getJNDIConnection("DSMCAJA2");
			ps = cn.prepareStatement(sql);
			int rs = ps.executeUpdate();
			if (rs != 1){
				 actualizado = false;
			}
		} catch (Exception ex) {
			System.out.print("Se capturo una excepcion en el actualizarNumeroRecibo: " + ex);
		}finally {
			try {
				ps.close();
				cn.close();
			} catch (Exception se2) {
				System.out.println("ERROR: Failed to close connection en actualizarContador: " + se2);
			}
		}
		return actualizado;
	}
/******************************************************************************************************************/
/****************************VALIDAR EXISTENCIA DE NUMERO RECIBO MANUAL*********************************************/
	public boolean verificarNumeroRecibo(int iCaId,String sCodComp, int iNumRecm){
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx =null; 
		boolean existe = false;	
		try{
			tx = session.beginTransaction();
			List result = (List) session
			.createQuery("from Recibo r where r.id.codcomp = '"+sCodComp+"' and r.caid = "+iCaId+" and r.id.numrecm = "+iNumRecm)
			.list();
			tx.commit();
			if(!result.isEmpty()){
				existe = true;
			}
		}catch(Exception ex){
			System.out.print("Se capturo una excepcion en ReciboPrimaCtrl.verificarNumeroRecibo: " + ex);
		}finally{
        	session.close();        
		}	
		return existe;
	}
	
}

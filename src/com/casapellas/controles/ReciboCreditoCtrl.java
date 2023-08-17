package com.casapellas.controles;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.casapellas.entidades.Cambiodet;
import com.casapellas.entidades.CambiodetId;
import com.casapellas.entidades.MetodosPago;
import com.casapellas.entidades.Recibo;
import com.casapellas.entidades.ReciboId;
import com.casapellas.entidades.Recibodet;
import com.casapellas.entidades.RecibodetId;
import com.casapellas.entidades.Recibofac;
import com.casapellas.entidades.RecibofacId;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.navegacion.As400Connection;
import com.casapellas.util.Divisas;
import com.casapellas.util.PropertiesSystem;
/**
 * CASA PELLAS S.A.
 * Creado por.........: Juan Carlos Ñamendi Pineda
 * Fecha de Creación..: 29/02/2009
 * Última modificación: 20/04/2009
 * Modificado por.....:	Juan Carlos Ñamendi Pineda
 * 
 */
public class ReciboCreditoCtrl {
	/**********************REGISTRAR EL RECIBO DE FACTURA DE CONTADO**********************************************************/
	public boolean registrarRecibo(Session session,Transaction tx,int iNumRec, int iNumRecm, String sCodComp,double dMontoAplicar, double dMontoRec,double dCambio, String sConcepto,Date dFecha,Date dHora,int iCodCli,String sNomCli, String sCajero,int iCaId, String sCodSuc,String sCodUser){
		boolean registrado = false;
		//Session session = HibernateUtil.getSessionFactoryMCAJA().openSession();
		//Transaction tx = null; 
		try{ 
			//tx = session.beginTransaction();
			
			Recibo recibo = new Recibo();
			ReciboId reciboid = new ReciboId();
			
			reciboid.setNumrec(iNumRec);
			reciboid.setCodcomp(sCodComp.trim());
			reciboid.setCaid(iCaId);
			reciboid.setCodsuc(sCodSuc);
			reciboid.setTiporec("CR");
			recibo.setId(reciboid);
								
			recibo.setMontoapl(BigDecimal.valueOf(dMontoAplicar));			//Monto Aplicar
			recibo.setMontorec(BigDecimal.valueOf(dMontoRec));	//Monto Recibido
			recibo.setConcepto(sConcepto);						//Concepto
			recibo.setFecha(dFecha);							//Fecha
			recibo.setHora(dHora);								//Hora
			recibo.setCodcli(iCodCli);							//Cod Cliente
			recibo.setCliente(sNomCli);							//Nombre Cliente
			recibo.setCajero(sCajero);							//Cajero					
				
			recibo.setNumrecm(iNumRecm);						//Numero de recibo manual
			recibo.setEstado("");
			recibo.setHoramod(dHora);
			recibo.setCodusera("");
			recibo.setMotivo("");
			recibo.setCoduser(sCodUser);
			session.save(recibo);					
			
			
			session.save(recibo);
			
			//tx.commit();
			registrado = true;
		}catch(Exception ex){
			System.out.print("se capturo una excepcion en ReciboCreditoCtrl.registrarRecibo: " + ex);
		}/*finally {			
			session.close();
		}*/
		return registrado;
	}
/**********************REGISTRAR EL DETALLE DEL RECIBO DE FACTURA DE CONTADO******************************************************/
	public boolean registrarDetalleRecibo(Session session,Transaction tx, int iNumrec, int iNumrecm, String codcomp, List lstMetodosPago,int iCaId,String sCodSuc){
		boolean registrado = false;
		Divisas divisas = new Divisas();
		
		//Session session = HibernateUtil.getSessionFactoryMCAJA().openSession();
		//Transaction tx = null; 
		
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
			
			//tx.commit();
			registrado = true;
		}catch(Exception ex){
			System.out.print("Se capturo una excepcion en ReciboCreditoCtrl.registrarDetalleRecibo: " + ex);
		}/*finally {			
			session.close();
		}*/
		return registrado;
	}
/******************************************************************************************************************/
/***************OBTENER ULTIMO NUMERO DE RECIBO DE F55CA014*******************************************************************/
	public int obtenerUltimoRecibo(int iCaid,String sCodComp){		
		Session session = HibernateUtilPruebaCn.currentSession();
		
		int ultimo = 0;	
		try{
			Long result = (Long) session
			.createQuery("SELECT max(f55.id.c4nncu) FROM F55ca014 as f55 where f55.id.c4id = " + iCaid + " and trim(f55.id.c4rp01) = '"+sCodComp.trim()+"'")
			.uniqueResult();
			
			ultimo = result.intValue();
			
		}catch(Exception ex){
			System.out.print("Se capturo una excepcion en ReciboCreditoCtrl.obtenerUltimoRecibo: " + ex);
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
				System.out.println("ERROR: Failed to close connection en ReciboCreditoCtrl.actualizarNumeroRecibo: " + se2);
			}
		}
		return actualizado;
	}
/******************************************************************************************************************/
/****************************VALIDAR EXISTENCIA DE NUMERO RECIBO MANUAL DE FACTURA DE CONTADO*********************************************/
	public boolean verificarNumeroRecibo(int iCaId,String sCodComp, int iNumRecm){
		Session session = HibernateUtilPruebaCn.currentSession();
		
		boolean existe = false;	
		try{
		
			List result = (List) session
			.createQuery("from Recibo r where r.id.codcomp = '"+sCodComp+"' and r.caid = "+iCaId+" and r.id.numrecm = "+iNumRecm)
			.list();
		
			if(!result.isEmpty()){
				existe = true;
			}
		}catch(Exception ex){
			System.out.print("Se capturo una excepcion en ReciboCreditoCtrl.verificarNumeroRecibo: " + ex);
		}finally{
        	session.close();        
		}	
		return existe;
	}
/******************************************************************************************************************/
/********************LLENAR ENLACE ENTRE RECIBO Y FACTURA*********************************************************************************/
	public boolean fillEnlaceReciboFac(Session session,Transaction tx,
				int iNumRec, String[] sPartida, String codcomp, 
				int[] iNumFac,double[] Monto, String sTipoDoc,
				int iCaId,String sCodSuc, int iCodcli, int iFecha){
		
		boolean filled = false;
		
		//Session session = HibernateUtil.getSessionFactoryMCAJA().openSession();
		//Transaction tx = session.beginTransaction();
		
		try {
			
			for (int i = 0; i < iNumFac.length; i++){
				Recibofac recibofac = new Recibofac();
				RecibofacId recibofacid = new RecibofacId();
				
				recibofacid.setNumfac(iNumFac[i]);					//Numero factura
				recibofacid.setNumrec(iNumRec);						//Numero Recibo
				recibofacid.setTipofactura(sTipoDoc);				//Tipo de documento
				recibofacid.setCodcomp(codcomp);					//Cod. Compañía
				recibofacid.setPartida(sPartida[i]);							//Partida en blanco para contado
				recibofacid.setCaid(iCaId);
				recibofacid.setCodsuc(sCodSuc);
				recibofacid.setCodcli(iCodcli);
				recibofacid.setFecha(iFecha);
				
				recibofac.setId(recibofacid);						//Recibo
				recibofac.setMonto(BigDecimal.valueOf(Monto[i]));	//Monto
				session.save(recibofac);							//Factura
			}
			//tx.commit();
			filled = true;
			
		}catch(Exception ex){
			System.out.print("Exception -> ReciboCreditoCtrl.fillEnlaceReciboFac: " + ex);
			
		}/*finally{
			session.close();
		}*/
		
		return filled;
	}
/*********************GRABAR CAMBIO EN CAMBIODET********************************************************************************/
	public boolean registrarCambio(Session session,Transaction tx,int iNumRec,String sCodComp, String sMoneda, double dCambio,int iCaId,String sCodSuc){
		boolean registrado = false;
		//Session session = HibernateUtil.getSessionFactoryMCAJA().openSession();
		//Transaction tx = null;
		try{
			tx = session.beginTransaction();
			Cambiodet cambiodet = new Cambiodet();
			CambiodetId cambiodetId = new CambiodetId();
			
			cambiodetId.setCodcomp(sCodComp);
			cambiodetId.setNumrec(iNumRec);
			cambiodetId.setMoneda(sMoneda);
			cambiodetId.setCaid(iCaId);
			cambiodetId.setCodsuc(sCodSuc);
			
			cambiodet.setCambio(BigDecimal.valueOf(dCambio));
			cambiodet.setId(cambiodetId);
			
			session.save(cambiodet);
			//tx.commit();
			registrado = true;
		}catch(Exception ex){
			System.out.println("Se capturo una excepcion en ReciboCreditoCtrl.registrarCambio: " + ex);
		}
		return registrado;
	}
/******************************************************************************************************************/
/****************OBTENER NUMERO DE RECIBO DE JDE******************************************************************/
	public int obtenerNoReciboJDE(){
		int iNumrecJDE = 0;
		Session session = HibernateUtilPruebaCn.currentSession();
		
		Transaction tx = null;
		boolean bNuevaSesionENS = false;
		
		try{
			if( session.getTransaction().isActive() )
				tx  = session.getTransaction();
			else{
				tx  = session.beginTransaction();
				bNuevaSesionENS = true;
			}
			
			iNumrecJDE = Integer.parseInt((session
					.createQuery("select f.id.nnn005 from Vf0002 f where trim(f.id.nnsy) = '03'")
					.uniqueResult()).toString());
			
			if (bNuevaSesionENS)
				tx.commit();
			
		}catch(Exception ex){
			System.out.println("Se capturo una excepcion en ReciboCreditoCtrl.obtenerNoReciboJDE: " + ex);
		}finally{
			try {
				if (bNuevaSesionENS){
					HibernateUtilPruebaCn.closeSession(session);
				}
			}
			catch (Exception e) { e.printStackTrace(); }
		}
		return iNumrecJDE;
	}
/*************************************************************************************************************/
/****************ACTUALIZAR EL NUMERO DE RECIBO EN "+PropertiesSystem.JDECOM+".F0002****************************************/
	public boolean actualizarNoReciboJDE(Connection cn,int iNoDocumento){
		boolean bActualizado = true;
		String sql = "UPDATE "+PropertiesSystem.JDECOM+".F0002 SET NNN005 = " + iNoDocumento + " where NNSY = '03'";
		PreparedStatement ps = null;
		try{
			ps = cn.prepareStatement(sql);
			int rs = ps.executeUpdate();
			if (rs != 1){
				bActualizado = false;
			}
		}catch(Exception ex){
			System.out.println("Se capturo una excepcion en ReciboContadoCtrl.actualizarNoReciboJDE: " + ex);
		}finally {
			try {
				ps.close();
				//cn.close();
			} catch (Exception se2) {
				System.out.println("ERROR: Failed to close connection en ReciboContadoCtrl.actualizarNoReciboJDE: " + se2);
			}
		}
		return bActualizado;
	}
/*************************************************************************************************************/
}

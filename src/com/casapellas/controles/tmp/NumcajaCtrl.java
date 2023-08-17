package com.casapellas.controles.tmp;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.casapellas.entidades.Numcaja;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.util.LogCajaService;

/**
 * CASA PELLAS S.A.
 * Creado por.........: Juan Carlos Ñamendi Pineda
 * Fecha de Creación..: 01/12/2009
 * Última modificación: 01/12/2009
 * Modificado por.....:	Juan Carlos Ñamendi Pineda
 * 
 */
public class NumcajaCtrl {
	public Exception error;
	public Exception errorDetalle;

public Exception getError() {
		return error;
	}

	public void setError(Exception error) {
		this.error = error;
	}

	public Exception getErrorDetalle() {
		return errorDetalle;
	}

	public void setErrorDetalle(Exception errorDetalle) {
		this.errorDetalle = errorDetalle;
	}

/**OBTENER EL NUMERO SIGUIENTE POR CODIGO DE NUMERACION DE CAJA************************************************************/
	public int obtenerNoSiguiente(String sCodnumeracion,int iCaid, String sCodcomp, String sCodsuc,String sCoduser){
		String sql = "from Numcaja as n where n.id.codnumeracion = '"+sCodnumeracion+"'" + 
		   " and n.id.caid ='"+iCaid+"' and n.id.codcomp = '"+sCodcomp+"' and n.id.codsuc = '"+sCodsuc+"'";
		int iNosiguiente = 0;
		Session session=null;
		Numcaja numcaja = null;
		
		try{
			session = HibernateUtilPruebaCn.currentSession();
			
			
			LogCajaService.CreateLog("obtenerNoSiguiente", "QRY", sql);
			
			numcaja = (Numcaja)session.createQuery(sql).uniqueResult();
			if(numcaja != null){
				iNosiguiente = numcaja.getNosiguiente();
				numcaja.setNosiguiente(iNosiguiente+1);
				numcaja.setUsuariomodificacion(sCoduser);
				numcaja.setFechamodificacion(new Date());
				
				LogCajaService.CreateLog("obtenerNoSiguiente", "HQRY", numcaja);
				
				session.update(numcaja);
				
			}	
		}catch(Exception ex){
			LogCajaService.CreateLog("obtenerNoSiguiente", "ERR", ex.getMessage());
			iNosiguiente = 0;
			ex.printStackTrace(); 
		}
		return iNosiguiente;
	}
/**************************************************************************************************************************/
	
/***********ACTUALIZAR EL NUMERO SIGUIENTE DE CAJA***************************************************************************/
	public boolean actualizarNoSiguiente(Numcaja numcaja){
		boolean bActualizado = true;
		Transaction tx  = null;
		Session session = null;
		try{
			session = HibernateUtilPruebaCn.currentSession();
			tx  = session.beginTransaction();
			session.save(numcaja);
			tx.commit();
		}catch(Exception ex){
			tx.rollback();
			bActualizado = false;
			ex.printStackTrace();
		}finally{
			try{session.close();}catch(Exception ex2){ex2.printStackTrace();};
		}
		return bActualizado;
	}	
/**************************************************************************************************************************/
	
}

package com.casapellas.controles;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.casapellas.entidades.Numcaja;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.PropertiesSystem;

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
	public int obtenerNoSiguiente(String sCodnumeracion,int iCaid, String sCodcomp, String sCodsuc,String sCoduser,Session session){
		String sql = "from Numcaja as n where n.id.codnumeracion = '"+sCodnumeracion+"'" + 
		   " and n.id.caid ='"+iCaid+"' and n.id.codcomp = '"+sCodcomp+"' and n.id.codsuc = '"+sCodsuc+"'";
		int iNosiguiente = 0;
		
		Numcaja numcaja = null;
		
		try{		
			
			numcaja = (Numcaja)session.createQuery(sql).uniqueResult();
			if(numcaja != null){
				iNosiguiente = numcaja.getNosiguiente();
				numcaja.setNosiguiente(iNosiguiente+1);
				numcaja.setUsuariomodificacion(sCoduser);
				numcaja.setFechamodificacion(new Date());		
				session.update(numcaja);
				
			}	
			LogCajaService.CreateLog("obtenerNoSiguiente", "INS", numcaja);
		}catch(Exception ex){
			iNosiguiente = 0;
			System.out.println("Se capturo una excepcion en NumcajaCtrl.obtenerNoSiguiente: " + ex);
			LogCajaService.CreateLog("obtenerNoSiguiente", "ERR", ex.getMessage());
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
			LogCajaService.CreateLog("actualizarNoSiguiente", "UPT", numcaja);
		}catch(Exception ex){
			LogCajaService.CreateLog("actualizarNoSiguiente", "ERR", ex.getMessage());
			tx.rollback();
			bActualizado = false;
			System.out.println("Se capturo una excepcion en NumcajaCtrl.actualizarNoSiguiente: " + ex);
		}finally{
			try{session.close();}catch(Exception ex2){ex2.printStackTrace();};
		}
		return bActualizado;
	}	
/**
 * @throws Exception ************************************************************************************************************************/
	
	public int obtenerNoSiguiente(String sCodnumeracion,int iCaid, String sCodcomp, String sCodsuc,String sCoduser) throws Exception{
		String sql = "from Numcaja as n where n.id.codnumeracion = '"+sCodnumeracion+"'" + 
		   " and n.id.caid ='"+iCaid+"' and n.id.codcomp = '"+sCodcomp+"' and n.id.codsuc = '"+sCodsuc+"'";
		int iNosiguiente = 0;
		
		
		
		try{
			String getnumcaja = "SELECT NOSIGUIENTE FROM "+PropertiesSystem.ESQUEMA+".NUMCAJA " + 
					"WHERE CODNUMERACION  = '"+sCodnumeracion+"' AND CAID = "+iCaid+" AND CODCOMP = '"+sCodcomp+"' AND CODSUC='"+sCodsuc+"';";
			
			iNosiguiente = Integer.parseInt( ConsolidadoDepositosBcoCtrl.executeSqlQueryUnique(getnumcaja, null, true));
			
			if( iNosiguiente >0){
				String getnumcajaUdp = "UPDATE "+PropertiesSystem.ESQUEMA+".NUMCAJA  SET"
						+ " NOSIGUIENTE="+ (iNosiguiente+1)+						
						"WHERE CODNUMERACION  = '"+sCodnumeracion+"' AND CAID = "+iCaid+" AND CODCOMP = '"+sCodcomp+"' AND CODSUC='"+sCodsuc+"';";
				
				ConsolidadoDepositosBcoCtrl.executeSqlQueryTx(null, getnumcajaUdp);
			}	
		}catch(Exception ex){
			iNosiguiente = 0;
			throw new Exception("Se capturo una excepcion en NumcajaCtrl.obtenerNoSiguiente: " + ex.getMessage());
		}
		return iNosiguiente;
	}
	
}

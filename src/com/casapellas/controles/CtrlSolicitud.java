/**
 * CASA PELLAS S.A.
 * Creado por.........: Juan Carlos Ñamendi Pineda
 * Fecha de Creación..: 28/02/2009
 * Última modificación: 15/12/2009
 * Modificado por.....:	Carlos Manuel Hernández Morrison.
 */

package com.casapellas.controles;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

//import org.apache.commons.mail.MultiPartEmail;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.casapellas.entidades.Solicitud;
import com.casapellas.entidades.SolicitudId;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.util.CustomEmailAddress;
import com.casapellas.util.Divisas;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.MailHelper;

public class CtrlSolicitud {
	public Exception errorDetalle;
	public Exception error;
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
/***********************************************************************************************/
/** 		Ingresar el registro de una solicitud con sesión y transacción de parámetros	
 ***************/
	public boolean registrarSolicitud(Session sesion, Transaction trans,int numsol, int numrec,String tiporec, 
					int caid,String codcomp,String codsuc,String referencia,int autoriza,Date fecha,String obs,
					String mpago, BigDecimal monto, String moneda){
		
		boolean bHecho = true; 
		Solicitud sol;
		SolicitudId sId;
		
		try {
			sId = new SolicitudId();
			sId.setCaid(caid);
			sId.setCodcomp(codcomp);
			sId.setCodsuc(codsuc);
			sId.setNumrec(numrec);
			sId.setNumsol(numsol);
			sId.setReferencia(referencia);
			sId.setTiporec(tiporec);		
			
			sol = new Solicitud();
			sol.setId(sId);
			sol.setAutoriza(autoriza);
			sol.setFecha(fecha);
			sol.setMoneda(moneda);
			sol.setMpago(mpago);
			sol.setMonto(monto);
			sol.setObs(obs);

			LogCajaService.CreateLog("registrarSolicitud", "HQRY", sol);
			sesion.save(sol);

		} catch (Exception ex) {
			LogCajaService.CreateLog("registrarSolicitud", "ERR", ex.getMessage());
			bHecho = false;
			error = new Exception("@LOGCAJA: error No se pudo grabar la solicitud de monto!!! " + codcomp + ";"+referencia+";" +tiporec + ";" + monto+";" + mpago);
			errorDetalle = ex;
			ex.printStackTrace(); 
		} 
		return bHecho;
	}	
	
	
/*********************************************************************/	
/**					 Obtiene Numero de Solicitud 					**
 *******************/
	
	public Integer getNumeroSolicitud() {	
		Session sessionX = HibernateUtilPruebaCn.currentSession();
		
		Integer numsol = 0;				
		try{			
						
			Integer result = (Integer) sessionX
				.createQuery("select max(sol.id.numsol) from Solicitud sol")
				.uniqueResult();
			
			if(result != null) numsol = result.intValue();
			
			numsol++;
		}catch(Exception ex){
			numsol = 0;
			System.out.println("Se capturo una excepcion en CtrlSolicitud.getNumeroSolicitud: " + ex);
		}finally{
			try{sessionX.close();}catch(Exception ex2){ex2.printStackTrace();};
		}		
		
		return numsol;
	}
	
/****************************REGISTRAR SOLICITUD DE AUTORIZACION********************************/
	public boolean registrarSolicitud(int iNumRec, int iNumRecm, int iNumSol, String sCodComp,String sAutoriza, String sReferencia,Date dFecha, String sObs,int iCaId,String sCodSuc){
		Session sessionX = HibernateUtilPruebaCn.currentSession();
		Transaction tx = null;    
		boolean bRegistrado = false;
        try {
        	tx = sessionX.beginTransaction();
        	Solicitud solicitud = new Solicitud();
        	SolicitudId solicitId = new SolicitudId();
        	
        	solicitId.setNumsol(iNumSol);
        	solicitId.setNumrec(iNumRec);       	
        	solicitId.setCodcomp(sCodComp);
        	solicitId.setCaid(iCaId);
        	solicitId.setCodsuc(sCodSuc);
        	solicitud.setId(solicitId);
        	
//        	solicitud.setAutoriza(sAutoriza);		        	
//        	solicitud.setObs(sObs);
//        	solicitud.setReferencia(sReferencia);
//			solicitud.setFecha(dFecha); 
//			solicitud.setNumrecm(iNumRecm);
//			      	
			sessionX.save(solicitud);
			bRegistrado = true;
        	tx.commit();     	
        }catch(Exception ex){
        	tx.rollback();
        	bRegistrado = false;
			System.out.println("Excepcion en CtrlSolicitud.registrarSolicitud :" + ex);	
		}finally {			
			try{sessionX.close();}catch(Exception ex2){ex2.printStackTrace();};
		}
        return bRegistrado;
	}
/**************************ENVIA CORREO ELECTRONICO****************************************************************/
	public void enviarCorreo(String sTo, String sFrom,String sNombreFrom,List lstCc, String sReferencia, String sObs, String sSubject,
							String sCajero,String sAutorizador,String sSucursal, String sCaja,String sMonto,String sMetodoPago){
		//MultiPartEmail email = new MultiPartEmail();
		Date dFecha = new Date();
		String sHora = "";
		String sFecha = "";
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
		try {
			sFecha = sdf.format(dFecha);
			sHora = dfHora.format(dFecha);
			String sNombreCajero = new Divisas().ponerCadenaenMayuscula(sNombreFrom);
			sNombreCajero = (sNombreCajero.equals(""))?sNombreFrom:sNombreCajero;
			
			
			String shtml = 
				"<table width=\"400px\" style=\"border: 1px #7a7a7a solid\" align=\"center\" cellspacing=\"0\" cellpadding=\"3\">" +
						"<tr>" +
							"<th colspan=\"2\" style=\"border-bottom: 1px #7a7a7a solid; background: #3e68a4\">" +
								"<font face=\"Arial\" size=\"2\" color=\"white\"><b>Solicitud de Autorización</b></font>" +
							"</th>" +
						"</tr>" +
						"<tr>" +
							"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"25%\"><b>Cajero: &nbsp;</b></td>" +
							"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"75%\">"+ sNombreCajero +"</td>" +
						"</tr>" +
						"<tr>" +
							"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"25%\"><b>Referencia: &nbsp;</b></td>" +
							"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"75%\">"+ sReferencia +"</td>" +
						"</tr>" +
						"<tr>" +
							"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"25%\"><b>Autorizado por: &nbsp;</b></td>" +
							"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"75%\">"+ sAutorizador +"</td>" +
						"</tr>" +
						"<tr>" +
							"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"25%\"><b>Sucursal: &nbsp;</b></td>" +
							"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"75%\">"+ sSucursal +"</td>" +
						"</tr>" +
						"<tr>" +
							"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"25%\"><b>Caja: &nbsp;</b></td>" +
							"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"75%\">"+ sCaja +"</td>" +
						"</tr>" +
						"<tr>" +
							"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"25%\"><b>Método de pago: &nbsp;</b></td>" +
							"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"75%\">"+ sMetodoPago + "</td>" +
						"</tr>" +
						"<tr>" +
							"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"25%\"><b>Monto Autorizado: &nbsp;</b></td>" +
							"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"75%\">" + sMonto + "</td>" +
						"</tr>" +
						"<tr>" +
							"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"25%\"><b>Fecha/Hora: &nbsp;</b></td>" +
							"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"75%\">"+ sFecha + " " + sHora +"</td>" +
						"</tr>" +
						"<tr>" +
							"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"25%\" valign=\"top\"><b>Observaciones: &nbsp;</b></td>" +
							"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"75%\">" +
								"<textarea rows=\"5\" cols=\"35\" readonly style=\"color: #1a1a1a; font-family: Arial, Helvetica, sans-serif; border-color:  #7a7a7a; font-size: 12px; border-width: 1px\">" +
									sObs +
								"</textarea>" +
							"</td>" +
						"</tr>" +
						"<tr>" +
							"<td align=\"center\" colspan=\"2\" style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\">" +
								"<b>"+new Divisas().obtenerURL()+"</b>" +
							"</td>" +
						"</tr>" +
						"<tr>" +
							"<td align=\"center\" colspan=\"2\" style=\"font-family: Arial, Helvetica, sans-serif;font-size: 10px;color: #1a1a1a;\">" +
								"<b>Casa Pellas, S. A. - Módulo de Caja</b>" +
							"</td>" +
						"</tr>" +
					"</table>";
			
			List<CustomEmailAddress> ccList = new ArrayList<CustomEmailAddress>();
			if(lstCc!=null && lstCc.size()>0){
				 for (@SuppressWarnings("rawtypes")
				Iterator iter = lstCc.iterator(); iter.hasNext();)
					 ccList.add(new CustomEmailAddress((String)iter.next()));
			 }
			
			MailHelper.SendHtmlEmail(
					new CustomEmailAddress(sFrom,sNombreCajero),
					new CustomEmailAddress(sTo), ccList, 
					sSubject, shtml);
		}catch(Exception ex){
			System.out.println("=>Correo no enviado CtrlSolicitud.enviarCorreo:"+ ex);
		}		
	}
/***********************************************************************************************/
/** 		Ingresar el registro de una solicitud con sesión y transacción de parámetros	
 ***************/
	public boolean ingresarSolicitud(Session sesion, Transaction trans,int iNumRec, int iNumRecm, int iNumSol, 
					String sCodComp,String sAutoriza, String sReferencia,Date dFecha, String sObs,int iCaId,String sCodSuc){
		Transaction tx = null;    
		boolean bRegistrado = true;
		boolean bUnico = false;
		
        try {
        	
        	Solicitud solicitud = new Solicitud();
        	SolicitudId solicitId = new SolicitudId();
        	
        	solicitId.setNumsol(iNumSol);
        	solicitId.setNumrec(iNumRec);       	
        	solicitId.setCodcomp(sCodComp);
        	solicitId.setCaid(iCaId);
        	solicitId.setCodsuc(sCodSuc);
        	solicitud.setId(solicitId);
        	
//        	solicitud.setAutoriza(sAutoriza);		        	
//        	solicitud.setObs(sObs);
//        	solicitud.setReferencia(sReferencia);
//			solicitud.setFecha(dFecha); 
//			solicitud.setNumrecm(iNumRecm);
			      	
			if(trans == null){
				bUnico = true;
				sesion = HibernateUtilPruebaCn.currentSession();
				tx  = sesion.beginTransaction();
			}
			sesion.save(solicitud);
        	     	
        }catch(Exception ex){
        	if(bUnico)
        		tx.rollback();
        	bRegistrado = false;
			System.out.println("Excepcion en CtrlSolicitud.registrarSolicitud :" + ex);	
		}finally {
			try{
				if(bUnico && bRegistrado){
					tx.commit();
					sesion.close();
				}
			}catch(Exception ex2){ex2.printStackTrace();};
		}
        return bRegistrado;
	}
}
package com.casapellas.controles.tmp;
/**
 * CASA PELLAS S.A.
 * Creado por.........: Juan Carlos Ñamendi Pineda
 * Fecha de Creación..: 23/09/2009
 * Última modificación: 20/04/2010
 * Modificado por.....: Carlos Manuel Hernández Morrison.
 * Detalle............: Método para enviar correos por solicitud o aprobación de emisiones de cheques.
 * 
 */

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import org.apache.commons.mail.MultiPartEmail;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.casapellas.entidades.Numcaja;
import com.casapellas.entidades.Solecheque;
import com.casapellas.entidades.SolechequeId;
import com.casapellas.entidades.Valorcatalogo;
import com.casapellas.entidades.Vsolecheque;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.util.CustomEmailAddress;
import com.casapellas.util.Divisas;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.MailHelper;
import com.casapellas.util.PropertiesSystem;

public class SolechequeCtrl {
	
/*********************************************************************************************/
/**		Método: Procedimiento guardar una emisión de cheque o de carta a credomatic
 * 		Nombre: Carlos Manuel Hernández Morrison 	         
 * 		Fecha: 05/05/2010 
 */
	public boolean grabarSolecheque(Session sesion, int iCaid,String sCodcomp,String sCodsuc,String sCodunineg,
									int iNosolicitud, int iCodemp, String sEstado,Date dtFechaHora, int iNumfac,String sTipofac,
									String sObser,int iCodUsrMod,String sMpago,String sTipo,String sMoneda,BigDecimal bdMonto,
									String sLogin,String sIdAfiliado,String sFechaPago,BigDecimal bdTasaCambio, int iCodcli, int iFechaFac){
		boolean bGrabado = true;
		try{
			Solecheque s = new Solecheque();
			SolechequeId sid = new SolechequeId();
			
			s.setCodemp(iCodemp);
			s.setEstado(sEstado);
			s.setFecha(dtFechaHora);
			s.setFechamod(dtFechaHora);
			s.setHora(dtFechaHora);
			s.setNumfac(iNumfac);
			s.setObservacion(sObser);
			s.setTipofactura(sTipofac);
			s.setUsuariomod(iCodUsrMod);
			s.setMoneda(sMoneda);
			s.setTipoemision(sTipo);
			s.setMpago(sMpago);
			s.setMonto(bdMonto);
			
			s.setIdafiliado(sIdAfiliado);
			s.setFechapago(sFechaPago);
			s.setTasacambio(bdTasaCambio);
			s.setCodautoriz("");
			s.setNotarjeta("");
			s.setCodcli(iCodcli);
			s.setFechafac(iFechaFac);
			
			sid.setCaid(iCaid);
			sid.setCodcomp(sCodcomp);
			sid.setCodsuc( (sCodsuc.trim().length() == 2) ? "000"+sCodsuc.trim():sCodsuc );
			sid.setCodunineg(sCodunineg);
			sid.setNosol(iNosolicitud);
			s.setId(sid);
			
			LogCajaService.CreateLog("grabarSolecheque", "HQRY", LogCajaService.toJson(s));
			
			sesion.save(s);
			
		}catch(Exception ex){
			bGrabado = false;
			LogCajaService.CreateLog("grabarSolecheque", "ERR", ex.getMessage());
		}
		
		return bGrabado;
	}
/**************************************************************************************/
/**		 Enviar correos de solicitud o de aprobación de la emisión de cheques 		 **/
	public boolean  enviarCorreo(String sEncabezado,String sPieCorreo, String sTo, String sFrom,String sNombreFrom, String sCc,String sSubject,
								 int iNosol, String sCaja, String sUbicacion, String sCompania, String sUnineg, String sCliente,
								 String sDocumentos, String sMontodev, String sFechaDev,String sUrl){
//		MultiPartEmail email = new MultiPartEmail();
		boolean enviado = true;
		String shtml = "";
		
		try {
			shtml = "<table width=\"410px\" style=\"border: 1px #7a7a7a solid\" align=\"center\" cellspacing=\"0\" cellpadding=\"3\">" +
					"<tr>"+
						"<th colspan=\"2\" style=\"border-bottom: 1px #7a7a7a solid; background: #3e68a4\">" +
							"<font face=\"Arial\" size=\"2\" color=\"white\"><b>"+sEncabezado+"</b></font>" +
						"</th>" +
					"</tr>"+
					"<tr>" +
						"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"23%\"><b>Solicitud #: &nbsp;</b></td>" +
						"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"77%\">"+iNosol+"</td>" +
					"</tr>"+
					"<tr>" +
						"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"23%\"><b>Caja:&nbsp;</b></td>" +
						"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"77%\">"+sCaja+"</td>" +
					"</tr>"+
					"<tr>" +
						"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"23%\"><b>Ubicacion:&nbsp;</b></td>" +
						"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"77%\">"+sUbicacion+"</td>" +
					"</tr>"+
					"<tr>" +
						"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"23%\"><b>Compañía:&nbsp;</b></td>" +
						"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"77%\">"+ sCompania +"</td>" +
					"</tr>"+
					"<tr>" +
						"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"23%\"><b>U.Negocio:&nbsp;</b></td>" +
						"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"77%\">"+ sUnineg +"</td>" +
					"</tr>"+
					"<tr>" +
						"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"23%\"><b>Cliente: &nbsp;</b></td>" +
						"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"77%\">"+ sCliente +"</td>" +
					"</tr>"+
					"<tr>" +
						"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"23%\"><b>Documentos:&nbsp;</b></td>" +
						"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"77%\">"+ sDocumentos +"</td>" +
					"</tr>"+
					"<tr>" +
						"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"23%\"><b>Monto Dev.: &nbsp;</b></td>" +
						"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"77%\">"+ sMontodev +"</td>" +
					"</tr>"+
					"<tr>"+
						"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"23%\"><b>Enlace: &nbsp;</b></td>" +
						"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"77%\">"+ sUrl +"</td>" +
					"</tr>"+
					
					"<tr>" +
						"<td align=\"center\" colspan=\"2\" style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px; color: #3e68a4; border-bottom: 1px ##1a1a1a solid; \">" +
							"<b>"+sPieCorreo+"</b>" +
						"</td>" +
					"</tr>" +
					
					"<tr>" +
						"<td align=\"center\" colspan=\"2\" style=\"font-family: Arial, Helvetica, sans-serif;font-size: 10px;color: black; border-bottom: 1px ##1a1a1a solid; \">" +
							"<b>Casa Pellas, S. A. - Módulo de Caja</b>" +
						"</td>" +
					"</tr>" +
				"</table>";
			
			Divisas dv = new Divisas();
			String sNombreCajero = dv.ponerCadenaenMayuscula(sNombreFrom);
			sNombreCajero = (sNombreCajero.equals(""))?sNombreFrom:sNombreCajero;
			
			List<CustomEmailAddress> ccList = new ArrayList<CustomEmailAddress>();
			List<CustomEmailAddress> bccList = new ArrayList<CustomEmailAddress>();
			for (int i = 0; i < PropertiesSystem.MAILCCS.length; i++) {
				String[] dtsCc = PropertiesSystem.MAILCCS[i].split(PropertiesSystem.SPLIT_CHAR);
				ccList.add(new CustomEmailAddress(dtsCc[0],dtsCc[1]));
			}
			for (int i = 0; i < PropertiesSystem.MAILBCCS.length; i++) {
				String[] dtsCc = PropertiesSystem.MAILBCCS[i].split(PropertiesSystem.SPLIT_CHAR);
				bccList.add(new CustomEmailAddress(dtsCc[0],dtsCc[1]));
			}
			
			MailHelper.SendHtmlEmail(
					new CustomEmailAddress(sFrom,sNombreCajero),
					new CustomEmailAddress(sTo), ccList, bccList, 
					sSubject, shtml);
		} catch (Exception error) {
			enviado = false;
			error.printStackTrace();
		}
		return enviado;
	}
	
	
/**************************************************************************************/
/**	Permite actualizar una solicitud de cheque a partir de un objeto Vsolecheque	 **/
	public boolean actualizarSolecheque(Vsolecheque v, int iCoduser,String sObservacion,int iCodestado){
		boolean bHecho = true;
		Divisas dv = new Divisas();
		Solecheque sc = new Solecheque();
		SolechequeId scId = new SolechequeId();
		Transaction trans = null;
		Session sesion = HibernateUtilPruebaCn.currentSession();
		
		try {
			scId.setCaid(v.getId().getCaid());
			scId.setCodcomp(v.getId().getCodcomp());
			scId.setCodsuc(v.getId().getCodsuc());
			scId.setCodunineg(v.getId().getCodunineg());
			scId.setNosol(v.getId().getNosol());
			
			sc.setId(scId);
			sc.setCodemp(v.getId().getCodemp());
			sc.setFecha(v.getId().getFecha());
			sc.setFechamod(new Date());
			sc.setHora(v.getId().getHora());
			sc.setNumfac(v.getId().getNumfac());
			sc.setObservacion(sObservacion);
			sc.setTipofactura(v.getId().getTipofactura());
			sc.setUsuariomod(iCoduser);
			sc.setMoneda(v.getId().getMoneda());
			sc.setMonto(v.getId().getMonto());
			sc.setMpago(v.getId().getMpago());
			sc.setTipoemision(v.getId().getTipoemision());
			
			sc.setCodautoriz(v.getId().getCodautoriz());
			sc.setFechapago(v.getId().getFechapago());
			sc.setIdafiliado(v.getId().getIdafiliado());
			sc.setNotarjeta(v.getId().getNotarjeta());
			sc.setTasacambio(v.getId().getTasacambio());
			sc.setCodcli(v.getId().getCodcli());
			sc.setFechafac( new FechasUtil().DateToJulian(v.getId().getFechafac())); 
			
			//------ Obtener el estado de Aprobado de la solicitud.
			Valorcatalogo vc = dv.leerValorCatalogo(9,iCodestado);
			sc.setEstado(vc!=null?vc.getCodigointerno():"P");
			
			trans = sesion.beginTransaction();
			sesion.update(sc);
			trans.commit();
			
			
		} catch (Exception error) {
			bHecho = false;
			error.printStackTrace();
		} finally {
			sesion.close();
		}
		return bHecho;
	}
/**************************************************************************************/
/**	Permite actualizar una solicitud de cheque a partir de un objeto Vsolecheque	 **/
	public boolean actualizarSolecheque(Vsolecheque v, int iCoduser,String sObservacion,int iCodestado,Session sesion){
		boolean bHecho = true;
		Divisas dv = new Divisas();
		Solecheque sc = new Solecheque();
		SolechequeId scId = new SolechequeId();
		
		try {
			scId.setCaid(v.getId().getCaid());
			scId.setCodcomp(v.getId().getCodcomp());
			scId.setCodsuc(v.getId().getCodsuc());
			scId.setCodunineg(v.getId().getCodunineg());
			scId.setNosol(v.getId().getNosol());
			
			sc.setId(scId);
			sc.setCodemp(v.getId().getCodemp()); 
			sc.setFecha(v.getId().getFecha());
			sc.setFechamod(new Date());
			sc.setHora(v.getId().getHora());
			sc.setNumfac(v.getId().getNumfac());
			sc.setObservacion(sObservacion);
			sc.setTipofactura(v.getId().getTipofactura());
			sc.setUsuariomod(iCoduser);
			sc.setMoneda(v.getId().getMoneda());
			sc.setMonto(v.getId().getMonto());
			sc.setMpago(v.getId().getMpago());
			sc.setTipoemision(v.getId().getTipoemision());
			
			sc.setCodautoriz(v.getId().getCodautoriz());
			sc.setFechapago(v.getId().getFechapago());
			sc.setIdafiliado(v.getId().getIdafiliado());
			sc.setNotarjeta(v.getId().getNotarjeta());
			sc.setTasacambio(v.getId().getTasacambio());
			
			sc.setCodcli(v.getId().getCodcli());
			sc.setFechafac( new FechasUtil().DateToJulian(v.getId().getFechafac())); 
			
			//------ Obtener el estado de Aprobado de la solicitud.
			Valorcatalogo vc = dv.leerValorCatalogo(9,iCodestado);
			sc.setEstado(vc!=null?vc.getCodigointerno():"P");
			
			sesion.update(sc);
			
		} catch (Exception error) {
			bHecho = false;
			error.printStackTrace();
		} 
		return bHecho;
	}
/**************************************************************************************/
/**	Permite consultar las solicitudes de cheques a partir de una consulta sql previa **/
	public List obtenerSolicitudes(String sql, int iMaxResult){
		List lstSol = null;
		Transaction trans = null;
		Session sesion = HibernateUtilPruebaCn.currentSession();
		
		try {
			trans = sesion.beginTransaction();
			if(iMaxResult==0)
				lstSol = sesion.createQuery(sql).list();
			else
				lstSol = sesion.createQuery(sql).setMaxResults(iMaxResult).list();
			trans.commit();
			
		} catch (Exception error) {
			lstSol = null;
			error.printStackTrace();
		} finally {
			try {HibernateUtilPruebaCn.closeSession(sesion); } catch (Exception e2) {e2.printStackTrace(); }
		}
		return lstSol;
	}
/*******GRABAR SOLOCITUD DE EMISION DE CHEQUE*******************************************************************/
	public boolean grabarSolecheque(Session sesion, int iCaid,String sCodcomp,String sCodsuc,
									String sCodunineg, int iNosol,int iCodemp,	String sEstado,Date dtFechaHora, 
									int iNumfac,String sTipofac, String sObser,int iCodUsrMod, int iCodcli, int iFechaFac){
		boolean bGrabado = true;
		try{
			Solecheque s = new Solecheque();
			SolechequeId sid = new SolechequeId();
			
			sid.setCaid(iCaid);
			sid.setCodcomp(sCodcomp);
			sid.setCodsuc(sCodsuc);
			sid.setCodunineg(sCodunineg);
			sid.setNosol(iNosol);
			s.setId(sid);
			
			s.setCodemp(iCodemp);
			s.setEstado(sEstado);
			s.setFecha(dtFechaHora);
			s.setFechamod(dtFechaHora);
			s.setHora(dtFechaHora);
			s.setNumfac(iNumfac);
			s.setObservacion(sObser);
			s.setTipofactura(sTipofac);
			s.setUsuariomod(iCodUsrMod);
			
			s.setCodcli(iCodcli);
			s.setFechafac(iFechaFac);
			
			sesion.save(s);
		}catch(Exception ex){
			bGrabado = false;
			ex.printStackTrace();
		}
		return bGrabado;
	}
/**OBTENER EL NUMERO DE SOLICITUD DE EMISION DE CHEQUE POR COMPANIA, SUCURSAL, Y CAJA*******************************************/
	public Numcaja obtenerNoSolicitud(int iCaid,String sCodcomp,String sCodsuc){
		String sql = "from Numcaja as n where n.id.codnumeracion = 'SOLECHEQUE'"
				+ " and n.id.caid ='"
				+ iCaid + "' and n.id.codcomp = '" + sCodcomp 
				+ "' and n.id.codsuc = '" + sCodsuc + "'";
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = null;
		Numcaja numcaja = null;
		try{
			tx = session.beginTransaction();
			numcaja = (Numcaja)session.createQuery(sql).uniqueResult();
			tx.commit();
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			try {HibernateUtilPruebaCn.closeSession(session); } catch (Exception e2) {e2.printStackTrace(); }
		}
		return numcaja;
	}
/**ACTUALIZAR EL NUMERO DE SOLICITUD DE EMISION DE CHEQUES******************************************************************************************/
	public boolean actualizarNoSolicitud(Numcaja numcaja){
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = null;
		boolean bActualizado = true;
		try{
			tx = session.beginTransaction();
			session.update(numcaja);
			tx.commit();
		}catch(Exception ex){
			bActualizado = false;
			ex.printStackTrace();
		}finally{
			session.close();
		}
		return  bActualizado;
	}
/***************************************************************************************************************/
}

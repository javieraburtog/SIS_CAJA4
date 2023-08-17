package com.casapellas.controles;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
/**
 * CASA PELLAS S.A.
 * Creado por.........: Carlos Manuel Hernández Morrison
 * Fecha de Creación..: 18/01/2009
 * Última modificación: 
 * Modificado por.....:	Carlos Manuel Hernández Morrison.
 * Descripcion:.......: Accesos a BD para manejo de traslado de facturas.
 * 
 */
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.casapellas.entidades.Aplicacion;
import com.casapellas.entidades.Hfactura;
import com.casapellas.entidades.Recibofac;
import com.casapellas.entidades.Trasladofac;
import com.casapellas.entidades.ens.Vautoriz;
import com.casapellas.entidades.Vf0101;
import com.casapellas.entidades.Vf55ca01;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.CustomEmailAddress;
import com.casapellas.util.Divisas;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.MailHelper;
import com.casapellas.util.PropertiesSystem;
import com.crystaldecisions.reports.queryengine.collections.Properties;
import com.ibm.ws.bootstrap.LoggingServices;
 

public class TrasladoCtrl {
	public Exception errorTf;
	public Exception getErrorTf() {
		return errorTf;
	}
	public void setErrorTf(Exception errorTf) {
		this.errorTf = errorTf;
	}
	
	
	public List<Recibofac> buscarPagosFactura(List<Hfactura>lstFacturasValidar){
		List<Recibofac>lstPagos = new ArrayList<Recibofac>();

		Session session = null;
		try {
			
			session = HibernateUtilPruebaCn.currentSession();
			
			for (Hfactura h : lstFacturasValidar) {
				
				Recibofac rf = (Recibofac)session.createCriteria(Recibofac.class)
					.add(Restrictions.eq("id.numfac", h.getNofactura()))
					.add(Restrictions.eq("id.tipofactura", h.getTipofactura() ))
					.add(Restrictions.eq("id.codcli", h.getCodcli() ))
					.add(Restrictions.eq("id.fecha", h.getFechajulian() ))
					.add(Restrictions.eq("id.codcomp", h.getCodcomp() ))
					.add(Restrictions.sqlRestriction(" trim(codunineg) = '"
										+h.getCodunineg().trim()+"'" ))
					.add(Restrictions.eq("estado", "" ))
					.add(Restrictions.eq("id.partida", ""))
					.setMaxResults(1).uniqueResult();
				
				
				if(rf == null) continue;
				
				lstPagos.add(rf);
			}
			
			
		} catch (Exception e) {
			LogCajaService.CreateLog("buscarPagosFactura", "ERR", e.getMessage());
		}
		return lstPagos;
	}
	
/***********************************************************************************************/
/***********************************************************************************************/
/** Método: Actualizar el estado de TrasladoFac 
 *	Fecha:  15/11/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 **/
	public static boolean actualizarEstadoTraslado(Session sesion,Trasladofac tFactura,String sNuevoEstado){
		boolean bHecho = true;
		
		try {
			
			Criteria cr = sesion.createCriteria(Trasladofac.class);
			cr.add(Restrictions.eq("consecutivo", tFactura.getConsecutivo()));
			
			LogCajaService.CreateLog("actualizarEstadoTraslado", "HQRY", LogCajaService.toSql(cr));
			
			Object ob = cr.uniqueResult();

			if(ob == null )
				return false;
			
			Trasladofac tfNuevo = (Trasladofac)ob;
			tfNuevo.setEstadotr(sNuevoEstado);
			
			LogCajaService.CreateLog("actualizarEstadoTraslado", "HQRY", LogCajaService.toJson(tfNuevo));
			
			sesion.update(tfNuevo);
			
		} catch (Exception error) {
			LogCajaService.CreateLog("actualizarEstadoTraslado", "ERR", error.getMessage());
			bHecho = false;
			
		}
		return bHecho;
	}
/***********************************************************************************************/
/***********************************************************************************************/
/** Método: Buscar una factura (Hfactura) en TrasladoFac, con Estado: sEstadoT y Destino: iCajaDest
 *	Fecha:  15/11/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 **/
	public Trasladofac buscarTrasladofac(Session sesion, Hfactura hFac, int iCajaDest, int iCajaOrigen, String sEstadoT){
		Trasladofac tf = null;
		
		
		try {		
			
			
			Criteria cr = sesion.createCriteria(Trasladofac.class);
			cr.add(Restrictions.eq("nofactura",   hFac.getNofactura()));
			cr.add(Restrictions.eq("tipofactura", hFac.getTipofactura()));
			cr.add(Restrictions.eq("codsuc", 	hFac.getCodsuc()));
			cr.add(Restrictions.eq("codcomp", 	hFac.getCodcomp()));
			cr.add(Restrictions.eq("codunineg", hFac.getCodunineg()));
			cr.add(Restrictions.eq("codcli", hFac.getCodcli()));
			cr.add(Restrictions.eq("fechafac", hFac.getFechajulian()));
			
			cr.setMaxResults(1);
			
			if(iCajaOrigen != 0)
				cr.add(Restrictions.eq("caidorig", iCajaOrigen));
			if(iCajaDest != 0)
				cr.add(Restrictions.eq("caiddest", iCajaDest));	
			if(sEstadoT.trim().compareTo("") != 0)
				cr.add(Restrictions.eq("estadotr", sEstadoT));
			else
				cr.add(Restrictions.in("estadotr", new String[]{"E","R"}));
			
			LogCajaService.CreateLog("buscarTrasladofac", "HQRY", LogCajaService.toSql(cr));
			
			Object ob = cr.uniqueResult();
			if(ob != null)
				tf = (Trasladofac)ob;
			else
				errorTf = new Exception("@TRASLADOFAC: No se encontro el registro del traslado de la factura ");

			
			
		} catch (Exception error) {
			LogCajaService.CreateLog("buscarTrasladofac", "ERR", error.getMessage());
			tf = null;
		}
		return tf;
	}
	
/***********************************************************************************************/
/***********************************************************************************************/
/** Método: Buscar una factura (Hfactura) en TrasladoFac, con Estado: sEstadoT y Destino: iCajaDest
 *	Fecha:  27/02/2021
 *  Nombre: Luis Alberto Fonseca Mendez
 **/
	public List<Trasladofac> buscarTraslados(Session sesion, Hfactura hFac, int iCajaDest, int iCajaOrigen, String sEstadoT){
		List<Trasladofac> tf = new ArrayList<Trasladofac>();
		boolean bIndepend = false;
		
		try {
			
			Transaction trans = null;
			if(sesion == null){
				sesion = HibernateUtilPruebaCn.currentSession();
				trans = sesion.beginTransaction();
				bIndepend = true;
			}
			Criteria cr = sesion.createCriteria(Trasladofac.class);
			cr.add(Restrictions.eq("nofactura",   hFac.getNofactura()));
			cr.add(Restrictions.eq("tipofactura", hFac.getTipofactura()));
			cr.add(Restrictions.eq("codsuc", 	hFac.getCodsuc()));
			cr.add(Restrictions.eq("codcomp", 	hFac.getCodcomp()));
			cr.add(Restrictions.eq("codunineg", hFac.getCodunineg()));
			cr.add(Restrictions.eq("codcli", hFac.getCodcli()));
			cr.add(Restrictions.eq("fechafac", hFac.getFechajulian()));
			
//			cr.setMaxResults(1);
			
			if(iCajaOrigen != 0)
				cr.add(Restrictions.eq("caidorig", iCajaOrigen));
			if(iCajaDest != 0)
				cr.add(Restrictions.eq("caiddest", iCajaDest));	
			if(sEstadoT.trim().compareTo("") != 0)
				cr.add(Restrictions.eq("estadotr", sEstadoT));
//			else
//				cr.add(Restrictions.in("estadotr", new String[]{"E","R"}));
			
			tf = cr.list();
			
//			Object ob = cr.uniqueResult();
//			if(ob != null)
//				tf = (Trasladofac)ob;
//			else
//				errorTf = new Exception("@TRASLADOFAC: No se encontro el registro del traslado de la factura ");

			if(tf.size() == 0)
				errorTf = new Exception("@TRASLADOFAC: No se encontro el registro del traslado de la factura ");
			
			if(bIndepend){
				trans.commit();
				sesion.close();
			}
			
		} catch (Exception error) {
			tf = null;
			errorTf = new Exception("@TRASLADOFAC: Error de sistema al consultar traslado de factura: "+error.getMessage());
			System.out.println("TRASLADOFACT: Error en TrasladoCtrl.buscarTrasladofac " + error);
		}
		return tf;
	}
/***********************************************************************************************/
/** Método: Buscar una factura (Hfactura) en TrasladoFac, con Estado: sEstadoT y Destino: iCajaDest
 *	Fecha:  28/05/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 **/
	public Trasladofac buscarTrasladofac(int iNofact,String sTipofact,String sCodsuc,String sCodcomp,
										String sCodunineg,int iCajaDest,String sEstadoT, int iCodcli, int iFecha){
		Trasladofac tf = null;
		Transaction trans = null;
		Session sesion = HibernateUtilPruebaCn.currentSession();
		StringBuilder sql = new StringBuilder();
		List lstTraslado = null;
		
		
		try {
			
			//sCodsuc = sCodsuc.trim().length()==5? sCodsuc.substring(3,5) : sCodsuc;
			sCodsuc =  String.valueOf( Integer.parseInt(sCodsuc.trim()) ) ;
			
			sql.append("from Trasladofac t where t.nofactura = "+iNofact);
			sql.append(" and t.tipofactura = '"+sTipofact+"' and t.codsuc = '"+sCodsuc+"'");
			sql.append(" and t.codcomp = '"+sCodcomp+"' and trim(t.codunineg) = '"+sCodunineg+"'");
			sql.append(" and t.codcli = "+iCodcli+" and t.fechafac = "+ iFecha  );
			sql.append(" and t.caiddest = "+iCajaDest + " and t.estadotr = '"+sEstadoT+"'");
			
			trans = sesion.beginTransaction();
			lstTraslado  = sesion.createQuery(sql.toString()).list();
			trans.commit();
			if(lstTraslado!=null && lstTraslado.size()>0)
				tf = (Trasladofac)lstTraslado.get(0); 			
			
		} catch (Exception error) {
			tf = null;
			System.out.println("Error en TrasladoCtrl.buscarTrasladofac " + error);
		} finally {
			sesion.close();
		}
		return tf;
	}
/***********************************************************************************************/
/** Método: Preparar los datos para el envío de correos por traslados de facturas.
 *	Fecha:  04/06/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 **/
	public boolean enviarCorreoTrasladofac(Vautoriz vaut,Hfactura hFac,Vf55ca01 f55Origen, Vf55ca01 f55Dest,boolean bEnviada){
		boolean bEnviado = true,bValido = true;
		String sEncabezado,sPieCorreo,sUrl="";
		String sTo="", sFrom="", sNombreFrom="", sSubject="";
		String sCajaOrigen,sCajaDestino,sCompania, sFactura, sCliente, sMonto, sUnineg;
		List<String> lstScc = new ArrayList<String>();
		Divisas dv = new Divisas();
		
		try {
			
			
			List<Integer>codigosParaEnvioCorreo = new ArrayList<Integer>();
			
			sSubject = "Notificación de Traslado de factura";
			sPieCorreo  = "Ha sido realizado el traslado de factura";
			
			//--- Obtener Dirección URL de la aplicación.
			sUrl = new Divisas().obtenerURL();
			if(sUrl==null || sUrl.equals("")){
				Aplicacion ap = dv.obtenerAplicacion(vaut.getId().getCodapp());
				sUrl = (ap==null)?"http://ap.casapellas.com.ni:9080/GCPMAJA":ap.getUrl().trim();
			}
			
			
			//&& ========== codigo del cajero conectado
			codigosParaEnvioCorreo.add(vaut.getId().getCodreg());
			
			
			//---- Obtener el correo del cajero que esta conectado.(FROM)
			int iCodcajero = vaut.getId().getCodreg();
			Vf0101 f01 = EmpleadoCtrl.buscarEmpleadoxCodigo2(iCodcajero);
			if(f01!=null){
				
				sNombreFrom = f01.getId().getAbalph().trim();
				sFrom = f01.getId().getWwrem1().trim().toUpperCase();
				
				if(!dv.validarCuentaCorreo(sFrom))
					sFrom = "webmaster@casapellas.com.ni";
			}else{
				sFrom = "webmaster@casapellas.com.ni";
				sNombreFrom = f55Origen.getId().getCacatinom().trim();
			}
			
			//---- Asignar cuentas de correo en dependencia si la factura fue enviada o recibida.
			if(bEnviada){
				
				//&& ========== codigo del cajero al que se le envia la factura y supervisor de la caja
				codigosParaEnvioCorreo.add(f55Dest.getId().getCacati() );
				codigosParaEnvioCorreo.add(f55Dest.getId().getCaan8()  );
				
				sEncabezado = "Envío de factura a caja: "+ f55Dest.getId().getCaname().trim();
				sTo = f55Dest.getId().getCacatimail().trim().toUpperCase();
				
			/*	if(dv.validarCuentaCorreo(sTo)){
					//--- Copia al supervisor de caja origen.
					if(dv.validarCuentaCorreo(f55Origen.getId().getCaan8mail().trim().toUpperCase()))
						lstScc.add(f55Origen.getId().getCaan8mail().trim().toUpperCase());
				}else{
					bValido = false;
				}*/
				
				
			}else{ //--- Factura Recibida(opción: Traer Factura)
				
				//&& ========== codigo del cajero y supervisor de la caja de donde se trae la factura
				codigosParaEnvioCorreo.add( f55Origen.getId().getCacati()  );
				codigosParaEnvioCorreo.add( f55Origen.getId().getCaan8()  );
				
				sEncabezado = "Recibo de factura desde caja: "+ f55Origen.getId().getCaname().trim();
				sTo = f55Origen.getId().getCacatimail().trim().toUpperCase();
				
				/*if(dv.validarCuentaCorreo(sTo)){
					//--- Copia al supervisor de caja Destino.						
					if(dv.validarCuentaCorreo(f55Origen.getId().getCaan8mail().trim().toUpperCase()))
						lstScc.add(f55Origen.getId().getCaan8mail().trim().toUpperCase());
				}else{
					bValido = false;
				}*/
				
			}
			//--- Copia a contadores de ambas cajas.
			if(bValido){
				
				//&& ========== codigo del cajero y supervisor de la caja de donde se trae la factura
				codigosParaEnvioCorreo.add( f55Origen.getId().getCacont()  );
				codigosParaEnvioCorreo.add( f55Dest.getId().getCacont() );
				
				if(dv.validarCuentaCorreo(f55Dest.getId().getCacontmail().trim().toUpperCase()))
					lstScc.add(f55Dest.getId().getCacontmail().trim().toUpperCase());
				if(dv.validarCuentaCorreo(f55Origen.getId().getCacontmail().trim().toUpperCase()))
					lstScc.add(f55Origen.getId().getCacontmail().trim().toUpperCase());
				
				sCajaDestino = f55Dest.getId().getCaid()   +" "+ f55Dest.getId().getCaname().trim();
				sCajaOrigen	 = f55Origen.getId().getCaid() +" "+ f55Origen.getId().getCaname().trim();
				sFactura	 = "N°: " + hFac.getNofactura()+ ", Tipo: "+hFac.getTipofactura();
				sCompania    = hFac.getCodcomp().trim() +" "+ hFac.getNomcomp().trim();
				sUnineg		 = hFac.getCodunineg().trim()  +" "+ hFac.getUnineg().trim();
				sCliente	 = hFac.getCodcli() +" "+ hFac.getNomcli().trim(); 
				sMonto		 = dv.formatDouble(hFac.getTotal()) +" "+ hFac.getMoneda();
				
				//--- Enviar el Correo.
				bEnviado = enviarCorreo(sEncabezado, sPieCorreo, sTo, sFrom,sNombreFrom, lstScc, sSubject, sCajaOrigen,
										sCajaDestino, sFactura, sCompania, sCliente, sMonto, sUnineg, sUrl,  codigosParaEnvioCorreo);
				
			}
		} catch (Exception error) {
			bEnviado = false;
			System.out.println("Error en TrasladoCtrl.enviarCorreoTrasladofac " + error);
		}
		return bEnviado;
	}
/***********************************************************************************************/
/** Método: Envio de Correos por traslado de facturas.
 *	Fecha:  04/06/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 **/	
	public boolean enviarCorreo(String sEncabezado,String sPieCorreo,String sTo,String sFrom,String sNombreFrom, List sCc,
											String sSubject, String sCajaOrigen,String sCajaDestino,String sFactura,
											String sComp,String sCliente, String sMonto,String sUnineg,String sUrl,
											List<Integer>codigosUsuario ){
		boolean bEnviado = true;
		// MultiPartEmail email = new MultiPartEmail();
		String shtml = "";
		
		try {
			shtml = 
			"<table width=\"450px\" style=\"border: 1px #7a7a7a solid\" align=\"center\" cellspacing=\"0\" cellpadding=\"3\">" +
				"<tr>"+
					"<th colspan=\"2\" style=\"border-bottom: 1px #7a7a7a solid; background: #3e68a4\">" +
						"<font face=\"Arial\" size=\"2\" color=\"white\"><b>"+sEncabezado+"</b></font>" +
					"</th>" +
				"</tr>"+
				"<tr>" +
					"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"23%\"><b>Caja Origen: &nbsp;</b></td>" +
					"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"77%\">"+sCajaOrigen+"</td>" +
				"</tr>"+
				"<tr>" +
					"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"23%\"><b>Caja Destino: &nbsp;</b></td>" +
					"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"77%\">"+sCajaDestino+"</td>" +
				"</tr>"+
				"<tr>" +
					"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"23%\"><b>Factura: &nbsp;</b></td>" +
					"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"77%\">"+sFactura+"</td>" +
				"</tr>" +
				"<tr>"+
					"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"23%\"><b>U.Negocio: &nbsp;</b></td>" +
					"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"77%\">"+sUnineg+"</td>" +
				"</tr>"+
				"<tr>"+
					"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"23%\"><b>Compañía: &nbsp;</b></td>" +
					"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"77%\">"+sComp+"</td>" +
				"</tr>"+
				"<tr>" +
					"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"23%\"><b>Cliente: &nbsp;</b></td>" +
					"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"77%\">"+sCliente+"</td>" +
				"</tr>"+
				"<tr>" +
					"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"23%\"><b>Monto: &nbsp;</b></td>" +
					"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"77%\">"+sMonto+"</td>" +
				"</tr>"+
				"<tr>" +
					"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"23%\"><b>Enlace: &nbsp;</b></td>" +
					"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"77%\">"+sUrl+"</td>" +
				"</tr>"+
				"<tr>" +
					"<td align=\"center\" colspan=\"2\" style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px; color: #3e68a4; border-bottom: 1px ##1a1a1a solid; \">" +
						"<b>"+sPieCorreo+"</b>" +
					"</td>" +
				"</tr>"+
				"<tr>"+
					"<td align=\"center\" colspan=\"2\" style=\"font-family: Arial, Helvetica, sans-serif;font-size: 10px;color: black; border-bottom: 1px ##1a1a1a solid; \">" +
						"<b>Casa Pellas, S. A. - Módulo de Caja</b>" +
					"</td>" +
				"</tr>" +
			"</table>";
			
			
			if(codigosUsuario != null){
				
				String strSql = "select lower(trim(wwrem1)) || '<>' || lower(trim(abalph) ) from @GCPMCAJA.vf0101 where aban8 in (@CODIGOSUSUARIO)";
						
				strSql = strSql.replace("@GCPMCAJA", PropertiesSystem.ESQUEMA)
						.replace("@CODIGOSUSUARIO", codigosUsuario.toString().replace("[", "").replace("]", ""));
				
				@SuppressWarnings("unchecked")
				List<String> cuentasEnvio = (ArrayList<String>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSql, true, null);
				
				List<CustomEmailAddress> toList = new ArrayList<CustomEmailAddress>();
				for (String dtaCuenta : cuentasEnvio) {
					
					if(dtaCuenta.split("<>")[0].trim().isEmpty()) {
						continue;
					}
				 
					toList.add(new CustomEmailAddress(dtaCuenta.split("<>")[0].trim(), CodeUtil.capitalize(dtaCuenta.split("<>")[1])));
				}
				
				MailHelper.SendHtmlEmail(
						new CustomEmailAddress(PropertiesSystem.WEBMASTER_EMAIL_ADRESS, "Módulo de Caja: Notificación de Traslado de Factura"),
						toList, null, new CustomEmailAddress(PropertiesSystem.MAIL_BOUNCEADDRESS), 
						sSubject, shtml);

			}else{
				
				Divisas dv = new Divisas();
				String sNombreCajero = dv.ponerCadenaenMayuscula(sNombreFrom);
				sNombreCajero = (sNombreCajero.equals(""))?sNombreFrom:sNombreCajero;
				
				List<CustomEmailAddress> copyList = new ArrayList<CustomEmailAddress>();
				if(sCc!=null && sCc.size()>0 ){
					for (int i = 0; i < sCc.size(); i++)
						copyList.add(new CustomEmailAddress((String)sCc.get(i)));
				}
				
				for (int i = 0; i < PropertiesSystem.MAILCCS.length; i++) {
					String[] dtsCc = PropertiesSystem.MAILCCS[i].split(PropertiesSystem.SPLIT_CHAR);
					copyList.add(new CustomEmailAddress(dtsCc[0],dtsCc[1]));
				}
				
				
				List<CustomEmailAddress> bccList = new ArrayList<CustomEmailAddress>();
				for (int i = 0; i < PropertiesSystem.MAILBCCS.length; i++) {
					String[] dtsCc = PropertiesSystem.MAILBCCS[i].split(PropertiesSystem.SPLIT_CHAR);
					bccList.add(new CustomEmailAddress(dtsCc[0],dtsCc[1]));
				}
				
				MailHelper.SendHtmlEmail(
						new CustomEmailAddress(PropertiesSystem.WEBMASTER_EMAIL_ADRESS, "Módulo de Caja: Notificación de Traslado de Factura"),
						new CustomEmailAddress(sTo), 
						copyList, 
						bccList, 
						sSubject, shtml);
			}
			
		} catch (Exception error) {
			error.printStackTrace(); 
		}		
		return bEnviado;
	}
	

/***********************************************************************************************/
/** Método: Actualizar el estado de TrasladoFac, a partir de un objeto Trasladofac.
 *	Fecha:  04/06/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 **/
	
	public boolean actualizarEstadoTraslado(Session sesion,Trasladofac tf,String sEstado,String sEstadoIni){
		boolean bHecho=true;
		boolean bMetodoUnico=false;
		Transaction trans = null;
		StringBuilder sbSql = new StringBuilder();
		
		try {
			
			if(tf!=null){
				sbSql.append(" update "+PropertiesSystem.ESQUEMA+".Trasladofac t ");
				sbSql.append(" set t.estadotr = '"+sEstado+"' ");
				sbSql.append(" where t.nofactura = "+tf.getNofactura()+" and t.tipofactura = '"+tf.getTipofactura()+"'");
				sbSql.append(" and t.codsuc = '"+tf.getCodsuc()+"' and t.codcomp = '"+tf.getCodcomp()+"' ");
				sbSql.append(" and trim(t.codunineg) = '"+tf.getCodunineg().trim()+"' and t.caiddest = "+tf.getCaiddest());
				sbSql.append(" and t.estadotr = 'R' and t.codcli = "+tf.getCodcli() +" and t.fechafac = "+tf.getFechafac());
				
				if(sesion==null){
					bMetodoUnico = true;
					sesion = HibernateUtilPruebaCn.currentSession();
					trans = sesion.beginTransaction();
				}
				Query q = sesion.createSQLQuery(sbSql.toString());
				int iActualizado = q.executeUpdate();
				bHecho = (iActualizado==1)?true:false;
				if(bMetodoUnico){
					if(bHecho)
						trans.commit();
					else
						trans.rollback();
					sesion.close();
				}
			}else bHecho = false;
			
		} catch (Exception error) {
			bHecho = false;
			System.out.println("Error en TrasladoCtrl.actualizarEstadoTraslado  " + error);
		}
		return bHecho;
	}
/***********************************************************************************************/
/** Método: Buscar una factura (Hfactura) en TrasladoFac, con Estado: sEstadoT y Destino: iCajaDest
 *	Fecha:  31/10/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 **/
	public Trasladofac buscarTrasladofac(Hfactura hFac,String sEstadoT,Session sesion){
		Trasladofac tf = null;
		List<Trasladofac> lstTraslado = null;
		
		try {
			
			Criteria cr = sesion.createCriteria(Trasladofac.class);
			cr.add(Restrictions.eq("nofactura",   hFac.getNofactura()));
			cr.add(Restrictions.eq("tipofactura", hFac.getTipofactura()));
			cr.add(Restrictions.eq("codsuc",    hFac.getCodsuc()));
			cr.add(Restrictions.eq("codcomp",   hFac.getCodcomp()));
			cr.add(Restrictions.eq("codunineg", hFac.getCodunineg()));
			cr.add(Restrictions.eq("codcli",  hFac.getCodcli()));
			cr.add(Restrictions.eq("fechafac",  hFac.getFechajulian()));
			
			if(sEstadoT.compareTo("") == 0)
				cr.add(Restrictions.in("estadotr",  new String[]{"E","R"}));
			else
				cr.add(Restrictions.eq("estadotr",  sEstadoT));
			
			lstTraslado  = cr.list();

			if(lstTraslado!=null && lstTraslado.size()>0)
				tf = (Trasladofac)lstTraslado.get(0); 			
			
		} catch (Exception error) {
			tf = null;
			System.out.println("Error en TrasladoCtrl.existeFacturaenTrasladofac " + error);
		}
		return tf;
	}
/***********************************************************************************************/
/** Método: Buscar una factura (Hfactura) en TrasladoFac, con Estado: sEstadoT y Destino: iCajaDest
 *	Fecha:  28/05/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 **/
	public Trasladofac buscarTrasladofac2(Hfactura hFac,int iCajaDest,String sEstadoT){
		Trasladofac tf = null;
		Transaction trans = null;
		Session sesion = HibernateUtilPruebaCn.currentSession();
		StringBuilder sql = new StringBuilder();
		List lstTraslado = null;
		
		try {
			sql.append("from Trasladofac t where t.nofactura = "+hFac.getNofactura());
			sql.append(" and t.tipofactura = '"+hFac.getTipofactura()+"' and t.codsuc = '"+hFac.getCodsuc()+"'");
			sql.append(" and t.codcomp = '"+hFac.getCodcomp()+"' and trim(t.codunineg) = '"+hFac.getCodunineg().trim()+"'");
			sql.append(" and t.caiddest = "+iCajaDest + " and t.estadotr = '"+sEstadoT+"'");
			
			trans = sesion.beginTransaction();
			lstTraslado  = sesion.createQuery(sql.toString()).list();
			trans.commit();
			if(lstTraslado!=null && lstTraslado.size()>0)
				tf = (Trasladofac)lstTraslado.get(0); 			
			
		} catch (Exception error) {
			tf = null;
			System.out.println("Error en TrasladoCtrl.buscarTrasladofac " + error);
		} finally {
			sesion.close();
		}
		return tf;
	}
	
	/***********************************************************************************************/
	/** Método: Eliminar registro de traslado
	 *	Fecha:  28/05/2010
	 *  Nombre: Carlos Manuel Hernández Morrison.
	 **/
		public Boolean deleteTrasladofac(Trasladofac hFac){
			String DRIVER = "com.ibm.as400.access.AS400JDBCDriver"; 
			String URL = "jdbc:as400:" + PropertiesSystem.IPSERVERDB2 + "/" + PropertiesSystem.ESQUEMA + ";prompt=false;translate binary=true";
			Connection conn = null;
			
			Boolean exito =false;
			try {
				
				Class.forName(DRIVER); 
				conn = DriverManager.getConnection(URL, PropertiesSystem.CN_USRNAME, PropertiesSystem.CN_USRPWD); 
				
				String strQuery = "";
				
				strQuery +="delete from Trasladofac  where nofactura = "+hFac.getNofactura() +
							" and trim(tipofactura) = '"+hFac.getTipofactura().trim()+"' "+
							" and trim(codsuc) = '"+hFac.getCodsuc().trim()+"'" +
							" and trim(codcomp) = '"+hFac.getCodcomp().trim()+"' " +
							" and trim(codunineg) = '"+hFac.getCodunineg().trim()+"'" + 
							" and caiddest = "+hFac.getCaiddest() + " and caidorig = "+hFac.getCaidorig()+"";
				
				System.out.println("sql anular traslado: " + strQuery);

				PreparedStatement pstmt = conn.prepareStatement(strQuery);
				pstmt.executeUpdate();
			
				exito=true;
			} catch (Exception error) {

				error.printStackTrace();
			} finally {
				//sesion.close();
			}
			
			return exito;
		}
		
		
	/***********************************************************************************************/
	/** Método: Verificar que una factura que ha sido traslada, se intente pagar en la caja origen.
	 *	Fecha:  28/05/2010
	 *  Nombre: Carlos Manuel Hernández Morrison.
	 **/
		public Trasladofac existeFacturaenTrasladofac(Hfactura hFac,int iCaidOrigen,String sEstadoT){
		
			Session session = null;
			Trasladofac traslado = null;
			
			try {
				 
				session = HibernateUtilPruebaCn.currentSession();

				Criteria cr = session.createCriteria(Trasladofac.class)
				.add(Restrictions.eq("nofactura",+hFac.getNofactura()))
				.add(Restrictions.eq("tipofactura", hFac.getTipofactura()))
				.add(Restrictions.eq("codsuc",  hFac.getCodsuc()))
				.add(Restrictions.eq("codcomp",hFac.getCodcomp()))
				.add(Restrictions.eq("caidorig",iCaidOrigen))
				.add(Restrictions.eq("codcli", hFac.getCodcli()))
				.add(Restrictions.eq("fechafac", hFac.getFechajulian()))
				.add(Restrictions.sqlRestriction(" trim(codunineg) = '"+hFac.getCodunineg().trim()+"'"));
		
				if(sEstadoT.compareTo("") == 0)
					cr.add(Restrictions.in("estadotr", new String[]{"E","R","P"})); //&& valida si está enviada, reenviada o pagada
				else
					cr.add(Restrictions.eq("estadotr", sEstadoT));
				
				@SuppressWarnings("unchecked")
				List<Trasladofac> lstTraslado  = (ArrayList<Trasladofac>) cr.list();
				
				if( lstTraslado != null && !lstTraslado.isEmpty() ){
					traslado =  lstTraslado.get(lstTraslado.size()-1);
				}
				
			} catch (Exception error) {				
				traslado = null;
				LogCajaService.CreateLog("existeFacturaenTrasladofac", "ERR", error.getMessage());				
			} 
			return traslado;
		}
/*********************************************************************************************/
/** Método: Guarda en TrasladoFac facturas seleccionadas a partir de una lista de Hfactura.
 *	Fecha:  31/05/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 **/
	public boolean guardarListaTraslado(Vautoriz vaut,Vf55ca01 f5Origen, Vf55ca01 f5Destino,List lstFacturas,boolean bEnvio){
		boolean bHecho=true;
		int iCaidOr, iCaidDest;
		Hfactura hFac = null;
		StringBuilder sbUpd = null;
		Trasladofac tfv = new Trasladofac();
		Transaction trans = null;
		Session sesion = HibernateUtilPruebaCn.currentSession();
		
		try {
			iCaidOr   = f5Origen.getId().getCaid();
			iCaidDest = f5Destino.getId().getCaid();
			
			trans = sesion.beginTransaction();
			
			for (int i = 0; i < lstFacturas.size(); i++) {
				hFac = (Hfactura)lstFacturas.get(i);
				
				tfv = buscarTrasladofac(hFac, "", sesion);
				if(tfv == null){
					Trasladofac tfi = new Trasladofac();
					tfi.setCaiddest(iCaidDest);
					tfi.setCaidorig(iCaidOr);
					tfi.setCodcomp(hFac.getCodcomp());
					tfi.setCodsuc(hFac.getCodsuc());
					tfi.setCodunineg(hFac.getCodunineg());
					tfi.setEstadof(hFac.getEstado());
					tfi.setEstadotr("E");
					tfi.setFecha(new Date());
					tfi.setMoneda(hFac.getMoneda());
					tfi.setNofactura(hFac.getNofactura());
					tfi.setTipofactura(hFac.getTipofactura());
					tfi.setCaidprop(iCaidOr);
					tfi.setFechafac(hFac.getFechajulian());
					tfi.setCodcli(hFac.getCodcli());
					
					LogCajaService.CreateLog("guardarListaTraslado", "HQRY", LogCajaService.toJson(tfi));
					
					sesion.save(tfi);
				}else{
					//&& ===== Actualizar estado del traslado.
					if ( iCaidDest == tfv.getCaidprop() || 
					   ((iCaidDest == tfv.getCaidorig()) && (iCaidOr == tfv.getCaiddest()))) {
						tfv.setEstadotr("A");
					} else {
						tfv.setEstadotr("R");
					}
					//&& ===== Actualizar caja origen y destino
					tfv.setCaiddest(iCaidDest);
					tfv.setCaidorig(iCaidOr);
					
					LogCajaService.CreateLog("guardarListaTraslado", "HQRY", LogCajaService.toJson(tfv));
					sesion.update(tfv);
				}
				
				trans.commit();
				
				enviarCorreoTrasladofac(vaut,hFac, f5Origen, f5Destino, bEnvio);
			}				
			
		} catch (Exception error) {
			bHecho = false;
			LogCajaService.CreateLog("guardarListaTraslado", "ERR", error.getMessage());
		} finally{
			HibernateUtilPruebaCn.closeSession(sesion);
		}
		return bHecho;
	}
	
}

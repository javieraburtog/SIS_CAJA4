package com.casapellas.controles;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import org.apache.commons.mail.MultiPartEmail;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.casapellas.entidades.Numcaja;
import com.casapellas.entidades.Salida;
import com.casapellas.entidades.SalidaId;
import com.casapellas.entidades.Valorcatalogo;
import com.casapellas.entidades.Vsalida;
import com.casapellas.entidades.VsalidaId;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.util.CustomEmailAddress;
import com.casapellas.util.Divisas;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.MailHelper;
import com.casapellas.util.PropertiesSystem;

/**
 * CASA PELLAS S.A.
 * Creado por.........: Carlos Manuel Hernández Morrison
 * Fecha de Creación..: 16/02/2010
 * Última modificación: 06/03/2010
 * Modificado por.....:	Carlos Manuel Hernández Morrison.
 * Detalle Nuevo .....: Agregar al correo la extensión tel. de la caja
 * 
 */
public class SalidasCtrl {
	
	
	public static List<Vsalida> castSalidaToVSalida(List<Salida>salidas){
		List<Vsalida> vsalidas = null;
		
		try {
			vsalidas = new ArrayList<Vsalida>(salidas.size());
			for (Salida s : salidas) {
				Vsalida v = new Vsalida();
				VsalidaId vid = new VsalidaId(s.getId().getNumsal(), s.getId()
						.getCodcomp(), s.getId().getCodsuc(), s.getId()
						.getCaid(), s.getCodsol(), s.getCodcaj(),
						s.getCodapr(), s.getConcepto(), s.getEstado(),
						s.getFsolicitud(), s.getFaproba(), s.getFproceso(),
						s.getMoneda(), s.getMonto(), s.getOperacion(),
						s.getTasa(), s.getEquiv(), s.getId().getCodcomp(), 
						s.getId().getCodsuc(), s.getCodsol() + "",
						s.getCodapr() + "", s.getCodcaj() + "", s.getId() + "");
				v.setId(vid);	
				vsalidas.add(v);
			}
		} catch (Exception e) {
			e.printStackTrace() ;
		}
		return vsalidas;
	}
	
	
/**************************************************************************************
 * Valida que la caja cuente con efectivo suficiente para cubrir el monto de la salida
 */	
	public double obtenerMontoactualCaja(int iCaid,String sCodsuc,String sCodcomp,Date dtFecha,String sMoneda){
		double dMonto5=0,dMontodev=0;
		String sql = "",sFecha="";
		Object ob = new Object();
		
		try {
			
			sFecha = FechasUtil.formatDatetoString(dtFecha, "yyyy-MM-dd");

			
			sql =  " select sum(rd.monto) from "+PropertiesSystem.ESQUEMA+".recibo r inner join "+PropertiesSystem.ESQUEMA+".recibodet rd on r.caid = rd.caid";
			sql += " and r.codsuc = rd.codsuc and r.codcomp = rd.codcomp and r.numrec = rd.numrec and r.tiporec = rd.tiporec";
			sql += " where r.caid = "+iCaid+" and r.fecha = '"+sFecha+"' and rd.mpago = '"+MetodosPagoCtrl.EFECTIVO+"' and rd.moneda = '"+sMoneda+"' ";
			sql += " and r.codcomp = '"+sCodcomp+"' and r.codsuc = '"+sCodsuc+"' and r.tiporec <> 'DCO'";
			
			//--- Montos recibidos en efectivo.
			ob = ConsolidadoDepositosBcoCtrl.executeSqlQueryUnique(sql, null, true);
			
			if(ob!=null){
				
				dMonto5 = new BigDecimal(ob.toString()).doubleValue();
				
				//--- Montos por cambios en efectivo.
				sql =  " select sum(rd.cambio) from "+PropertiesSystem.ESQUEMA+".recibo r inner join "+PropertiesSystem.ESQUEMA+".cambiodet rd on r.caid = rd.caid";
				sql += " and r.codsuc = rd.codsuc and r.codcomp = rd.codcomp and r.numrec = rd.numrec and r.tiporec = rd.tiporec";
				sql += " where r.caid = "+iCaid+" and r.fecha = '"+sFecha+"' and rd.moneda = '"+sMoneda+"' ";
				sql += " and r.codcomp = '"+sCodcomp+"' and r.codsuc = '"+sCodsuc+"'";
				
				ob =  ConsolidadoDepositosBcoCtrl.executeSqlQueryUnique(sql, null, true);
				if(ob!=null){
					dMontodev = new BigDecimal(ob.toString()).doubleValue();
					dMonto5 -= dMontodev;
					dMonto5 = Divisas.roundDouble(dMonto5);
				}
				
				//--- Montos por devoluciones en efectivo.
				sql =  " select sum(rd.monto) from "+PropertiesSystem.ESQUEMA+".recibo r inner join "+PropertiesSystem.ESQUEMA+".recibodet rd on r.caid = rd.caid";
				sql += " and r.codsuc = rd.codsuc and r.codcomp = rd.codcomp and r.numrec = rd.numrec and r.tiporec = rd.tiporec";
				sql += " where r.caid = "+iCaid+" and r.fecha = '"+sFecha+"' and rd.mpago = '"+MetodosPagoCtrl.EFECTIVO+"' and rd.moneda = '"+sMoneda+"' ";
				sql += " and r.codcomp = '"+sCodcomp+"' and r.codsuc = '"+sCodsuc+"' and r.tiporec = 'DCO'";
				
				ob =  ConsolidadoDepositosBcoCtrl.executeSqlQueryUnique(sql, null, true);
				if(ob!=null){
					dMontodev = new BigDecimal(ob.toString()).doubleValue();
					dMonto5 -= dMontodev;
					dMonto5 = Divisas.roundDouble(dMonto5);
				}
			}
			 
			
		} catch (Exception error) {
			dMonto5=0;
			error.printStackTrace(); 
		} 
		
		return dMonto5;
	}
/*********************************************************************************************/	
/**	 	  guardar o Actualizar registro de Salida en "+PropertiesSystem.ESQUEMA+".SALIDA		 				**/
	public boolean guardarActualizarSalida(Salida salida,boolean bInsertar){
		boolean bHecho = true;
		Session sesion = HibernateUtilPruebaCn.currentSession();
		Transaction trans = null;
		
		try {
			trans = sesion.beginTransaction();
			if(bInsertar) // Registro nuevo.
				sesion.save(salida);
			else
				sesion.update(salida);
			trans.commit();
			
		} catch (Exception error) {
			bHecho = false;
			System.out.println("Error en SalidasCtrl.guardarActualizarSalida " + error);
		} finally {
			try {
				sesion.close();
			} catch (Exception e) {
				bHecho = false;
				System.out.println("Error al cerrar sesion en SalidasCtrl.guardarActualizarSalida " + e);
			}
		}
		return bHecho;
	}
/*********************************************************************************************/	
/** 	 		   Insertar registro de Salidas en  SALIDA		 					**/
	public boolean guardarSalida(int iNosal, int iCaid,String sCodcomp,String sCodsuc,String sLogin, int iCodapr,int iCodcaj, int iCodsol,
								 String sConcepto, BigDecimal bdEequiv, String sMoneda, BigDecimal bdMonto, String sTo, String sRefer1,
								 String sRefer2, String sRefer3, String sRefer4, BigDecimal bdTasa){
		Session sesion = HibernateUtilPruebaCn.currentSession();
		Transaction trans = null;
		boolean bHecho = true;
		
		try {
			Salida s = new Salida();
			SalidaId sId = new SalidaId();

//			iNosal = obtenerNumeracionCaja("NSALIDA", iCaid, sCodcomp, sCodsuc,true,sLogin);
//			if(iNosal>0){			
				//------------- Valores llave.
			sId.setCaid(iCaid);
			sId.setCodcomp(sCodcomp);
			sId.setCodsuc(sCodsuc);
			sId.setNumsal(iNosal);
			
			//------------- Campos no llave.
			s.setCodapr(iCodapr);
			s.setCodcaj(iCodcaj);
			s.setCodsol(iCodsol);
			s.setConcepto(sConcepto);
			s.setEquiv(bdEequiv);
			s.setEstado("E");
			s.setFaproba(new Date());
			s.setFproceso(new Date());
			s.setFsolicitud(new Date());
			s.setMoneda(sMoneda);
			s.setMonto(bdMonto);
			s.setOperacion(sTo);
			s.setRefer1(sRefer1);
			s.setRefer2(sRefer2);
			s.setRefer3(sRefer3);
			s.setRefer4(sRefer4);
			s.setTasa(bdTasa);
			s.setId(sId);
			
			trans = sesion.beginTransaction();
			sesion.save(s);
			trans.commit();
				
		} catch (Exception error) {
			bHecho = false;
			System.out.println("Error en SalidasCtrl.guardarSalida  " + error);
		} finally {
			try {
				sesion.close();
			} catch (Exception e) {
				bHecho = false;
				System.out.println("Error al cerrar sesion en SalidasCtrl.guardarSalida  " + e);
			}
		}
		return bHecho;
	}
	

/*********************************************************************************************/	
/**	  Ejecuta una consulta sql definida en su llamado, que obtiene la salidas registradas	**/
	public static List<Vsalida> obtenerSalidas(String sConsultaSQL,int iMaxResult){
		List<Vsalida> salidas = null;
 
		
		try {
			
			salidas = ConsolidadoDepositosBcoCtrl.executeSqlQuery(sConsultaSQL, Vsalida.class, false);
			
			if(iMaxResult > 0 && salidas !=null && !salidas.isEmpty() && salidas.size() > iMaxResult ){
				salidas = new ArrayList<Vsalida>( salidas.subList(0, iMaxResult));
			}
			
		} catch (Exception error) {
			LogCajaService.CreateLog("obtenerSalidas", "ERR", error.getMessage());
		}  
		return salidas;
	}
	
	 
	
	
/*********************************************************************************************/	
/** Obtiene lista de salidas a partir de caja, comp, suc, numsalida, 
 * 	codigo del sol, moneda, estado y fechas				
 * **/
	public List<Vsalida> obtenerSolicitudSalidas(int iCaid, String sCodcomp,String sCodsuc,int iNumsal,int iCodsol, 
										String sMoneda, String sEstado,int iMaxResult,
										Date dtFechaArqueo, Date dtHoraInicio,Date dtHoraFin){
		List<Vsalida> lstSalidas = new ArrayList<Vsalida>();
		String sql = "",sFecha, sFechasql;
		@SuppressWarnings("unused")
		boolean bEstado = false;
		
		try {
		 
			
			//---- Básico
			sql =  " from Vsalida v where v.id.caid = "+iCaid+" and v.id.codcomp = '"+sCodcomp+"'";
			sql += " and v.id.codsuc = '"+sCodsuc+"'";
			
			//--- Filtrar por número de solicitud.
			if(iNumsal != 0 && iNumsal>0)
				sql += " and v.id.numsal = "+iNumsal;
			
			//--- Filtrar por código del solicitante.
			if(iCodsol != 0 && iCodsol>0)
				sql += " and v.id.codsol = "+iCodsol;
			
			//--- Filtrar por moneda
			if(sMoneda!=null && !sMoneda.equals(""))
				sql += " and v.id.moneda = '"+sMoneda+"'";
			
			//--- Filtrar por Estado
			
			if (sEstado != null && sEstado.trim().compareTo("") != 0 ) {
				sql += " and v.id.estado = '" + sEstado + "'";
				bEstado = true;
			}else{
				sql += " and v.id.estado not in ('X','D')";
			}
			//---Filtrar por fecha con o sin filtro de estado
			if(dtFechaArqueo!=null){
				sFecha = FechasUtil.formatDatetoString(dtFechaArqueo, "yyyy-MM-dd");
				sFechasql  = " and cast(v.id.fsolicitud as date) = '" +sFecha+"'";
				
				sql += sFechasql;
			}
			
			//--- Agregar filtro de intervalo de horas en caso de arqueos previos.
			if(dtHoraInicio!=null){
				String sHini,sHfin; 
				sHini = FechasUtil.formatDatetoString(dtHoraInicio, "HH.mm.ss");
				sHfin = FechasUtil.formatDatetoString(dtHoraFin,"HH.mm.ss");
				sql += " and cast(v.id.fsolicitud as time) >='"+sHini+"'and cast(v.id.fsolicitud as time) <='"+sHfin+"'";
			}
			
			sql += " order by v.id.fsolicitud desc";
			
			lstSalidas = SalidasCtrl.obtenerSalidas(sql, iMaxResult);
			 
		} catch (Exception error) {
			LogCajaService.CreateLog("obtenerSolicitudSalidas", "ERR", error.getMessage());
		}
		return lstSalidas;
	}
/*********************************************************************************************/	
/** 	 		   Leer el lista de valores para tipo de operacion							**/
	@SuppressWarnings("unchecked")
	public List<Valorcatalogo> leerValorCatalogo(int iCatalogoId){
		List<Valorcatalogo> lstTo = null;
		String sql = "";
		
		try {
			
			Session session = HibernateUtilPruebaCn.currentSession();			
			
			sql = "select * from "+PropertiesSystem.ENS+".Valorcatalogo vc where vc.activo = '1' and vc.codcatalogo = "+iCatalogoId;
			lstTo = session.createSQLQuery(sql).addEntity(Valorcatalogo.class).list();			
			
		} catch (Exception error) {
			error.printStackTrace(); 
		} 
		return lstTo;
	}
/*********************************************************************************************/	
/** 	 		   Leer el número de la última salida de caja solicitad 					**/
	public int obtenerNumeracionCaja(String sCodnumero, int iCaid, String sCodcomp,String sCodsuc,boolean bActualizar,String sLogin){
		int iNosol = -1;
		String sql;
		
		try {
		
			sCodnumero = sCodnumero.trim().toLowerCase();
			
			String queryWhere = " trim(lower(n.codnumeracion)) = '"+sCodnumero+"' and n.codsuc = '"+sCodsuc+"' " 
					 + " and n.caid = "+iCaid+" and trim(n.codcomp) = '"+sCodcomp.trim()+"'" ;
			
			sql  = " select * from "+PropertiesSystem.ESQUEMA+".Numcaja  n where  " + queryWhere;
			 
			Numcaja numcaja  = ConsolidadoDepositosBcoCtrl.executeSqlQueryUnique(sql, Numcaja.class, true);
			
			if(numcaja == null){
				return -1 ;
			}
			 
			iNosol = numcaja.getNosiguiente();
				
			if(bActualizar){
				
				sql = " update @BDCAJA.Numcaja n set nosiguiente = ( nosiguiente + 1 ), Usuariomodificacion = '@USRMOD', Fechamodificacion = '@FECHAMOD' where " + queryWhere ;
				sql = sql
				.replace("@BDCAJA", PropertiesSystem.ESQUEMA )
				.replace("@USRMOD", sLogin.toUpperCase() )
				.replace("@FECHAMOD", new SimpleDateFormat("yyyy-MM-dd").format(new Date() ) ) ;
				
				ConsolidadoDepositosBcoCtrl.executeSqlQueryTx(null, sql);
				
			}
			
		} catch (Exception error) {
			iNosol = 0;
			error.printStackTrace(); 
		} 
		return iNosol; 
	}
/*********** 14. Enviar un correo electrónico al supervisor y contador de la caja ***********************************/
	public boolean  enviarCorreo(String sEncabezado,String sPieCorreo,String sTipoOperacion, String sTo, String sFrom, String sNombreFrom, String sCc,
								String sCc1, String sSubject,int sCodSol, String sSolicitante,int iCaid,String sCaja,String sTelefono,
							 	String sCodcomp,String sUbicacion,int noSalida,BigDecimal  bdMonto, String sMoneda,String sUrl,Date dFecha){
		// MultiPartEmail email = new MultiPartEmail();		
		String sHora = "";
		String sFecha = "";
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
		Divisas dv = new Divisas();
		boolean enviado = true;
		
		try {
			sFecha = sdf.format(dFecha);
			sHora = dfHora.format(dFecha);
			
			String shtml = "<table width=\"410px\" style=\"border: 1px #7a7a7a solid\" align=\"center\" cellspacing=\"0\" cellpadding=\"3\">" +
							"<tr>"+
							"<th colspan=\"2\" style=\"border-bottom: 1px #7a7a7a solid; background: #3e68a4\">" +
								"<font face=\"Arial\" size=\"2\" color=\"white\"><b>"+sEncabezado+"</b></font>" +
							"</th>" +
							"</tr>"+
							"<tr>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"20%\"><b>Solicitud #: &nbsp;</b></td>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"80%\">"+noSalida+"</td>" +
							"</tr>"+
							"<tr>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"20%\"><b>Solicitante: &nbsp;</b></td>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"80%\">"+ sCodSol +" / "+sSolicitante+"</td>" +
							"</tr>"+
							"<tr>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"20%\"><b>Operación: &nbsp;</b></td>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"80%\">"+sTipoOperacion+"</td>" +
							"</tr>"+
							"<tr>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"20%\"><b>Caja: &nbsp;</b></td>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"80%\">"+ iCaid +" / "+sCaja+"  Tel: " +sTelefono+ "</td>" +
							"</tr>"+
							"<tr>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"20%\"><b>Ubicación: &nbsp;</b></td>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"80%\">"+ sUbicacion +"</td>" +
							"</tr>"+
							"<tr>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"20%\"><b>Compañía: &nbsp;</b></td>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"80%\">"+sCodcomp+"</td>" +
							"</tr>" +
							"<tr>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"20%\"><b>Efectivo: &nbsp;</b></td>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"80%\">"+dv.formatDouble(bdMonto.doubleValue())+ " "+ sMoneda +"</td>" +
							"</tr>"+
							"<tr>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"20%\"><b>Fecha: &nbsp;</b></td>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"80%\">"+ sFecha + " / " + sHora +"</td>" +
							"</tr>" +
							"<tr>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"20%\"><b>Enlace: &nbsp;</b></td>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"80%\">"+ sUrl +"</td>" +
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

			String sNombreCajero= Divisas.ponerCadenaenMayuscula(sNombreFrom);
			sNombreCajero = (sNombreCajero.equals(""))?sNombreFrom:sNombreCajero;
			
			List<CustomEmailAddress> copyEmail = new ArrayList<CustomEmailAddress>();
			copyEmail.add(new CustomEmailAddress(sCc));
			if(!sCc1.equals("")) {
				copyEmail.add(new CustomEmailAddress(sCc1));
			}
			
			MailHelper.SendHtmlEmail(
					new CustomEmailAddress(sFrom, sNombreCajero),
					new CustomEmailAddress(sTo),
					copyEmail,
					null,
					sSubject, shtml);
			
		}catch(Exception ex){
			ex.printStackTrace();
			enviado = false;			
		}		
		return enviado;
	}
}

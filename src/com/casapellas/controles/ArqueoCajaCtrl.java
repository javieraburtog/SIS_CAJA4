package com.casapellas.controles;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;

import javax.faces.context.FacesContext;

//import org.apache.commons.mail.MultiPartEmail;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.casapellas.dao.FacContadoDAO;
import com.casapellas.entidades.Arqueo;
import com.casapellas.entidades.ArqueoId;
import com.casapellas.entidades.Arqueorec;
import com.casapellas.entidades.B64strfile;
import com.casapellas.entidades.F55ca017;
import com.casapellas.entidades.FacturaxRecibo;
import com.casapellas.entidades.Hfactura;
import com.casapellas.entidades.Hfacturajde;
import com.casapellas.entidades.Mindpxcaja;
import com.casapellas.entidades.Minsemitidas;
import com.casapellas.entidades.Minutadp;
import com.casapellas.entidades.Recibo;
import com.casapellas.entidades.Recibodet;
import com.casapellas.entidades.Recibofac;
import com.casapellas.entidades.Valorcatalogo;
import com.casapellas.entidades.Vdetallecambiorec;
import com.casapellas.entidades.Vdetallecambiorecibo;
import com.casapellas.entidades.Vfacturaxdevolucion;
import com.casapellas.entidades.Vhfactura;
import com.casapellas.entidades.Vrecibosxdevoluciones;
import com.casapellas.entidades.Vrecibosxtipoegreso;
import com.casapellas.entidades.Vrecibosxtipompago;
import com.casapellas.entidades.Vreciboxdevolucion;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.navegacion.As400Connection;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.CustomEmailAddress;
import com.casapellas.util.Divisas;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.JulianToCalendar;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.MailHelper;
import com.casapellas.util.PropertiesSystem;

/**
 * CASA PELLAS S.A.
 * Creado por.........: Carlos Manuel Hernández Morrison
 * Fecha de Creación..: 25/07/2009
 * Última modificación: Carlos Manuel Hernández Morrison 
 * Modificado por.....:	05/06/2010
 * Descripcion:.......: Accesos a los datos en bd de caja para el arqueo de caja.
 * 
 */
@SuppressWarnings("unchecked")
public class ArqueoCajaCtrl {
	public ArqueoCajaCtrl() {
		super();
	}
	
	
	/**
	 * Buscar el arqueo al cual pertenece un recibo de caja
	 */
	public static Arqueo buscarArqueoPorRecibo(Recibo recibo){

		try {
		
			String strSqlQuerySelect = 
				" select a.* "
				 + " from @BDGCPMCAJA.arqueo a inner join @BDGCPMCAJA.arqueorec ar "
				 + " on a.caid = ar.caid and a.codcomp = ar.codcomp and a.noarqueo = ar.noarqueo"
				 + " where a.caid = @CAID and trim(a.codcomp) = '@CODCOMP' and ar.numrec = @NUMREC  and a.fecha = '@FECHA'"
				 + " and ar.tiporec = '@TIPOREC' and ar.tipodoc = 'R' and a.estado <>'R' "  ;
			
			strSqlQuerySelect = strSqlQuerySelect
				.replace("@BDGCPMCAJA", PropertiesSystem.ESQUEMA)
				.replace("@CAID", Integer.toString(recibo.getId().getCaid()))
				.replace("@CODCOMP", recibo.getId().getCodcomp().trim() )
				.replace("@NUMREC", Integer.toString( recibo.getId().getNumrec() ) )
				.replace("@TIPOREC", recibo.getId().getTiporec() ) 
				.replace("@FECHA",  new SimpleDateFormat("yyyy-MM-dd").format(recibo.getFecha() ) );
				 
			List<Arqueo> arqueos = (List<Arqueo>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSqlQuerySelect, true, Arqueo.class);
			
			if( arqueos == null || arqueos.isEmpty() )
				return null;
			
			return arqueos.get(0);
				
			
		} catch (Exception e) {
			e.printStackTrace(); 
			return null;
		}
		 
	}
	
	public static BigDecimal calcularIngresosCaja(Date fechaarqueo, int caid, String  codcomp, 
								String moneda, Date dtHoraIni, Date dtHoraFin){
		BigDecimal montoIgreso ;
		Session sesion = null;
		
		try {
			
			String fecha = new SimpleDateFormat("yyyy-MM-dd").format(fechaarqueo) ;
			String horaini = new SimpleDateFormat("HH:mm:ss").format(dtHoraIni) ;
			String horafin = new SimpleDateFormat("HH:mm:ss").format(dtHoraFin) ;
			
			
			String strSql  = 

			"select  sum(monto) from ( "+
			 
			"select  rd.monto monto, rd.caid, r.codcomp, rd.mpago mpago, r.estado, rd.moneda, r.tiporec, r.fecha, r.hora "+
			"from "+PropertiesSystem.ESQUEMA +".recibo r inner join "+PropertiesSystem.ESQUEMA +
				".recibodet rd on r.caid = rd.caid and r.codcomp = rd.codcomp  "+
				" and r.tiporec = rd.tiporec and r.numrec = rd.numrec "+
			"where  r.tiporec not in ('DCO', 'FCV')  "+
			 
			"union all "+
			 
			"select -1 *  rd.monto monto, rd.caid, r.codcomp, rd.mpago mpago, r.estado, rd.moneda, r.tiporec, r.fecha, r.hora "+
			"from "+PropertiesSystem.ESQUEMA +".recibo r inner join "+PropertiesSystem.ESQUEMA 
				+".recibodet rd on r.caid = rd.caid and r.codcomp = rd.codcomp "+
				" and r.tiporec = rd.tiporec and r.numrec = rd.numrec "+
			"where r.tiporec  in ('DCO')  "+
			 
			"union all "+
			 
			"select  -1 * rd.cambio monto, rd.caid, r.codcomp, '"+MetodosPagoCtrl.EFECTIVO+"' mpago , r.estado , rd.moneda, r.tiporec, r.fecha, r.hora "+
			"from "+PropertiesSystem.ESQUEMA +".recibo r inner join "+PropertiesSystem.ESQUEMA 
				+".cambiodet rd on r.caid = rd.caid and r.codcomp = rd.codcomp "+
				" and r.tiporec = rd.tiporec and r.numrec = rd.numrec "+
			"where  r.tiporec not in ('DCO', 'FCV')  and cambio > 0 "+
			 
			" union all "+
			
			" select  dnc.montoaplicado monto, dnc.caid, dnc.codcomp, dnc.formadepago mpago, " +
			" ( case when  dnc.estado = 1 then '' else 'A'  end) estado, dnc.moneda, dnc.tiporec, date(dnc.fechacrea) fecha, time(dnc.fechacrea) hora " +
			" from " +PropertiesSystem.ESQUEMA +".dnc_donacion dnc " +
			
			") as A  "+
			"where fecha = '"+fecha+"' and caid = "+caid+"  and trim(codcomp) = '"
				+codcomp.trim()+"' and  mpago = '"+MetodosPagoCtrl.EFECTIVO+"' and estado = ''  and moneda = '"
				+moneda+"' and hora between '"+horaini+"' and  '"+horafin+"' " ;
			
			LogCajaService.CreateLog("calcularIngresosCaja", "QRY", strSql);
			
			sesion = HibernateUtilPruebaCn.currentSession();

			 montoIgreso = new BigDecimal ( String.valueOf( sesion.createSQLQuery(strSql).uniqueResult() ) );
					
			
		} catch (Exception e) {
			LogCajaService.CreateLog("calcularIngresosCaja", "ERR", e.getMessage()); 
			montoIgreso = BigDecimal.ZERO;
		}
		
		return montoIgreso;
		
	}
	
	
	
	
	public boolean verificarExisteCierre(List<Object[]>lstNumerosRec, int icaid, 
						String sCodcomp, String sMoneda, int iCodcajero, 
						Date dtFecha, double dDsugerido, double dTingresos, 
						double dTegresos, double dDfinal, double dEfectcaja){
		
		Session sesion = null;
		Transaction trans = null;
		boolean bNuevaSesion = false;
		boolean bCierreExiste = false;
		
		try {
			
			sesion = HibernateUtilPruebaCn.currentSession();
			if(sesion.getTransaction().isActive())
				trans = sesion.getTransaction();
			else{
				bNuevaSesion = true;
				trans = sesion.beginTransaction();
			}
			Criteria cr = sesion.createCriteria(Arqueo.class)
			.add(Restrictions.eq("id.caid", icaid))
			.add(Restrictions.eq("id.codcomp", sCodcomp))
			.add(Restrictions.eq("id.fecha", dtFecha))
			.add(Restrictions.eq("estado", "P"))
			.add(Restrictions.eq("codcajero", iCodcajero))
			.add(Restrictions.eq("tingreso",   new BigDecimal(String.valueOf(dTingresos))))
			.add(Restrictions.eq("tegresos",   new BigDecimal(String.valueOf(dTegresos))))
			.add(Restrictions.eq("dsugerido",  new BigDecimal(String.valueOf(dDsugerido))))
			.add(Restrictions.eq("efectcaja",  new BigDecimal(String.valueOf(dEfectcaja))))
			.add(Restrictions.eq("dfinal", new BigDecimal(String.valueOf(dDfinal))))
			.add(Restrictions.eq("moneda", sMoneda)) ;

			List<Arqueo>lstArqueo = (ArrayList<Arqueo>)cr.list();
			if(lstArqueo != null && !lstArqueo.isEmpty()){
				
				List<Integer>lstRecibos = new ArrayList<Integer>(lstNumerosRec.size());
				for (Object[] ob : lstNumerosRec) 
					lstRecibos.add(Integer.parseInt(String.valueOf(ob[0])));
				
				for (Arqueo a : lstArqueo) {
					cr = sesion.createCriteria(Arqueorec.class)
					.add(Restrictions.eq("id.noarqueo", a.getId().getNoarqueo() ))
					.add(Restrictions.eq("id.codcomp", a.getId().getCodcomp()))
					.add(Restrictions.eq("id.caid", a.getId().getCaid()))
					.add(Restrictions.in("id.numrec", lstRecibos ));
					List<Arqueorec>lstArqueorecs = (ArrayList<Arqueorec>)cr.list();
					
					if(lstArqueorecs != null && !lstArqueorecs.isEmpty()){
						bCierreExiste = true;
						break;
					}
				}
			}
		} catch (Exception e) {
			bCierreExiste = true;
			e.printStackTrace();
		}finally{
			if( bNuevaSesion ){
				try{trans.commit();}catch(Exception e){e.printStackTrace();}
				try{HibernateUtilPruebaCn.closeSession(sesion);}
				catch(Exception e){e.printStackTrace();}
			}
		}
		return bCierreExiste;
	}
	/**************************************************
	 *  Método: CRPMCAJA / com.casapellas.controles /grabarEmisionMinutaDeps
	 *  Descrp: graba el registro de la minuta de deposito
	 *	Fecha:  Oct 26, 2012 
	 *  Autor:  CarlosHernandez
	 ***/
	public static boolean grabarEmisionMinutaDeps(int iCaid, String sCodcomp, 
			int iCodcajero,int iNominuta, Date dtFechaEmite,Date dtHoraEmite,
			String sEnviadoa,File fMinuta, String iNocuenta,String sMotivomn, 
			BigDecimal bdMonto,String sMoneda, int iTipoMin, int iNoarqueo,
			String nombredoc) {
		
		boolean bHecho = true;
		Session sesion = null;
		Transaction trans = null;
		boolean newCn = false;
		
		try {
			
			sesion = HibernateUtilPruebaCn.currentSession();
			trans = (newCn = !(sesion.getTransaction().isActive())) ? sesion
					.beginTransaction() : sesion.getTransaction();
			
			Minsemitidas me = new Minsemitidas();
			me.setCaid(iCaid);
			me.setCajero(iCodcajero);
			me.setCodcomp(sCodcomp);
			me.setCuenta(iNocuenta);
			me.setEnviadoa(sEnviadoa);
			me.setEstado("");
			me.setFechaemision(dtFechaEmite);
			me.setFechareal(new Date());
			me.setHoraemision(dtFechaEmite);
			me.setMonto(bdMonto);
			me.setMoneda(sMoneda);
			me.setMotivomn(sMotivomn);
			me.setNminuta(iNominuta);
			me.setTipominuta(iTipoMin);		
			me.setNoarqueo(iNoarqueo);
			me.setNombredoc(nombredoc);
			me.setFileformat(66);
			me.setItemtype(67);
			
			sesion.save(me);
			
			//&& ========== Crear el reporte en pdf 
			String[] strParts = crearMinutaStringArray(fMinuta);
			if(strParts == null)
				strParts = new String[]{"","",""};			
			for (int i = 0; i < strParts.length; i++) {
				B64strfile b64StrPart =  new B64strfile(strParts[i], i,(int) me.getIdemision(),
						67, String.valueOf(me.getIdemision()));
				try {
					sesion.save(b64StrPart);
				} catch (Exception e) {
					e.printStackTrace(); 
				}
			}	
			
		} catch (Exception e) {
			bHecho = false;
			e.printStackTrace(); 
		}finally{
			if(newCn){
				try {  trans.commit(); } 
				catch (Exception e2) { }
				try {  HibernateUtilPruebaCn.closeSession(sesion); }
				catch (Exception e2) { }
			}
			sesion = null;
			trans = null;
		}
		return bHecho;
	}
	
	public static String[] crearMinutaStringArray(File file) {
		String[] strParts = null;
		int maxLength = 30000;
		try {

			byte[] data = Divisas.byteArrayFromFile( file.getAbsolutePath());
			byte[] encodedBytes = Base64.encodeBase64(data);
			
			String str = new String(encodedBytes, "UTF8");
			int inicio = 0;
			int fin = 0;
			int strEncodeLng = str.length();
			int cantRows = (strEncodeLng / maxLength) + 1;
			strParts = new String[cantRows];

			for (int i = 0; i < cantRows; i++) {

				fin += maxLength;

				if (fin > str.length()) {
					fin = str.length();
				}
				strParts[i] = str.substring(inicio, fin);
				inicio = fin;
			}
			
		} catch (Exception e) {
			LogCajaService.CreateLog("crearMinutaStringArray", "ERR", e.getMessage());
			strParts = null;
		}
		return strParts;
	}
	
	
	
	/* ******************** fin de metodo grabarEmisionMinutaDeps ****************************/
	/**************************************************
	 *  Método: CRPMCAJA / com.casapellas.controles /validarSecuenciaMinuta
	 *  Descrp: 
	 *	Fecha:  Oct 26, 2012 
	 *  Autor:  CarlosHernandez
	 ***/
	@SuppressWarnings("unchecked")
	public static String validarSecuenciaMinuta(int iNoCfgMndp,String sCodcomp,
					String sMoneda) {
		String sValida = ""; 
		Session sesion = null; 
		Transaction trans = null;
		boolean bNuevaSesionCaja = false;
		
		try {
			sesion = HibernateUtilPruebaCn.currentSession();
			if(sesion.getTransaction().isActive() )
				trans = sesion.getTransaction();
			else{
				trans = sesion.beginTransaction();
				bNuevaSesionCaja = true;
			}
			
			Criteria cr = sesion.createCriteria(Minutadp.class);
			cr.add(Restrictions.eq("idminuta", iNoCfgMndp));
			cr.add(Restrictions.eq("moneda", sMoneda));
			cr.add(Restrictions.eq("codcomp", sCodcomp));
			cr.add(Restrictions.eq("estado", true));
			
			Minutadp mp = (Minutadp)cr.uniqueResult();
			if(mp == null)
				return "No se encontro configuracion de minuta para secuencia "+iNoCfgMndp;
			
			cr = sesion.createCriteria(Minsemitidas.class);
			cr.add(Restrictions.eq("codcomp", sCodcomp));
			cr.add(Restrictions.eq("moneda", sMoneda));
			cr.add(Restrictions.eq("cuenta", mp.getCuenta()));
			cr.addOrder(Order.desc("nminuta"));
			
			List<Minsemitidas>lstNumsMax = cr.list(); 
			if(lstNumsMax != null && lstNumsMax.size() > 0){
				
				if(mp.getNosiguiente() < lstNumsMax.get(0).getNminuta() ){
					sValida = "El número generado para minuta automática no " 
							+ "contiene un valor correcto\nGenerado "
							+ mp.getNosiguiente()+"ultimo registrado: "
							+ lstNumsMax.get(0).getNminuta();
				}
			}
			mp = null;
			cr = null;
			trans = null;
			sesion = null;
			lstNumsMax = null;
			
		} catch (Exception e) {
			sValida = "Error de aplciacion al validar consistencia " +
						"en numero de minuta";
			e.printStackTrace();
		}finally{
			try{
				if(bNuevaSesionCaja){
					try{ trans.commit();}
					catch(Exception error){ error.printStackTrace(); };
					try{ HibernateUtilPruebaCn.closeSession(sesion);}
					catch(Exception error){ error.printStackTrace(); };
				}
			}catch(Exception error){ error.printStackTrace(); }
		}
		return sValida;
	}
	/* ******************** fin de metodo validarSecuenciaMinuta ****************************/
	/**************************************************
	 *  Método: CRPMCAJA / com.casapellas.controles /obtenerUltimaMinuta
	 *  Descrp: 
	 *	Fecha:  Oct 26, 2012 
	 *  Autor:  CarlosHernandez
	 ***/
	public static Minutadp obtenerUltimaMinuta(int iCaid, String sCodcomp,	String sMoneda, String usrmod, boolean closeConnection) {
		Minutadp md = null;

		boolean hecho = true;
		Mindpxcaja mdc = null;
		Object[] dtaCn = null; 
		
		try {
			
			dtaCn =  ConsolidadoDepositosBcoCtrl.getSessionForQuery( ) ;
			Session sesion = (Session)dtaCn[1] ;
			
			Criteria cr = sesion.createCriteria(Mindpxcaja.class);
			cr.add(Restrictions.eq("caid", iCaid));
			cr.add(Restrictions.eq("codcomp", sCodcomp));
			cr.add(Restrictions.eq("estado", 1));
			cr.addOrder(Order.desc("fechacrea"));
			cr.setMaxResults(1);
			
			mdc = (Mindpxcaja)cr.uniqueResult();
			
			if(mdc == null){
				hecho = false;
				return md = null;
			}
			
			cr =  sesion.createCriteria(Minutadp.class);
			cr.add(Restrictions.eq("codcomp", sCodcomp));
			cr.add(Restrictions.eq("moneda", sMoneda));
			cr.add(Restrictions.eq("estado", true));
			cr.addOrder(Order.desc("fechacrea"));
			cr.setMaxResults(1);
			
			md = (Minutadp)cr.uniqueResult();
			
			if(md == null){
				hecho = false;
				return md = null;
			}
			
			//&& ========================= Actualizacion  
			List<String>lstQuerys = new ArrayList<String>();
			
			String sql = "update "+PropertiesSystem.ESQUEMA +".Minutadp " 
			+" set nosiguiente = ( nosiguiente + 1) "   
			+ ", fechamod =  '"+ FechasUtil.formatDatetoString(new Date(), "yyyy-MM-dd")+"' "
			+ ", horamod =  '"+ FechasUtil.formatDatetoString(new Date(), "HH.mm.ss")+"' "
			+ ", usrmod =  '"+usrmod+"' "
			+" where idminuta =  " + md.getIdminuta()
			+" and trim(codcomp) = '" +sCodcomp.trim()+"' and moneda = '"+sMoneda+"' ";
							
			lstQuerys.add(sql);
			
			sql = "update "+PropertiesSystem.ESQUEMA +".mindpxcaja " 
				+" set numerosiguiente = ( numerosiguiente + 1) "   
				+ ", fechamod =  '"+ FechasUtil.formatDatetoString(new Date(), "yyyy-MM-dd")+"' "
				+ ", horamod =  '"+ FechasUtil.formatDatetoString(new Date(), "HH.mm.ss")+"' "
				+ ", usrmod =  '"+usrmod+"' "
				+" where IDCFGXMDP =  " + mdc.getIdcfgxmdp() +" and trim(codcomp) = '" +sCodcomp.trim()+"' and caid = " + iCaid ;
			
			lstQuerys.add(sql);
			
			hecho = ConsolidadoDepositosBcoCtrl.executeSqlQueries(lstQuerys );
			
			
		} catch (Exception e) {
			e.printStackTrace() ; 
			hecho = false;
			return md =  null;
		}finally{
			
			if(hecho){
				
				String strNoMinuta =  String.format("%08d", mdc.getNumerosiguiente() );
				strNoMinuta = String.valueOf( iCaid ).concat( strNoMinuta.substring(strNoMinuta.length()-5, strNoMinuta.length() ));
				md.setMindpxcajaNosiguente( Integer.parseInt(strNoMinuta) );
				md.setNosiguienteDisplay(strNoMinuta);
				
			}else{
				md = null;
			}
			
			if(closeConnection)
				ConsolidadoDepositosBcoCtrl.closeSessionForQuery( hecho, dtaCn  ); 
		}
		return md;
	}
	/* ******************** fin de metodo obtenerUltimaMinuta ****************************/
	
	/**************************************************
	 *  Método: CRPMCAJA / com.casapellas.controles /leerConfiguracionMndp
	 *  Descrp: Lee y retorna la configuracion de minuta de 
	 *  		deposito para la compania
	 *	Fecha:  Oct 25, 2012 
	 *  Autor:  CarlosHernandez
	 ***/
	public static Minutadp leerConfiguracionMndp(int iCaid, String sCodcomp,String sMoneda) {
		Minutadp md = null;
		Object[] dtaCn = null ; 
		
		try {
			
			dtaCn =  ConsolidadoDepositosBcoCtrl.getSessionForQuery( ) ;
			Session sesion = (Session)dtaCn[1] ;
			
			Mindpxcaja mdc = (Mindpxcaja) sesion.createCriteria(Mindpxcaja.class) 
					 .add(Restrictions.eq("caid", iCaid))
					 .add(Restrictions.eq("codcomp", sCodcomp))
					 .add(Restrictions.eq("estado", 1))
					 .addOrder(Order.desc("fechacrea"))
					 .setMaxResults(1).uniqueResult();
			
			if(mdc == null )
				return null;
			
			md = (Minutadp) sesion.createCriteria(Minutadp.class) 
					 .add(Restrictions.eq("codcomp", sCodcomp))
					 .add(Restrictions.eq("moneda", sMoneda))
					 .add(Restrictions.eq("estado", true))
					 .addOrder(Order.desc("fechacrea"))
					 .setMaxResults(1).uniqueResult();
			 
			if(md == null){
				return null;
			}
			
			String strNoMinuta =  String.format("%08d", mdc.getNumerosiguiente() );
			
			strNoMinuta = String.valueOf( iCaid ).concat( strNoMinuta.substring(strNoMinuta.length()-5, strNoMinuta.length() ));
		
			md.setMindpxcajaNosiguente( Integer.parseInt(strNoMinuta) );
			md.setNosiguienteDisplay(strNoMinuta);
			 
		} catch (Exception e) {
			md = null;
			e.printStackTrace(); 
		}finally{
			ConsolidadoDepositosBcoCtrl.closeSessionForQuery( true, dtaCn  ); 
		}
		return md;
	}
	/* ******************** fin de metodo leerConfiguracionMndp ****************************/
	
/** **********************************************************************************************
 * Metodo:	Permite que la devolucion total en moneda distinta a la  que se pago la la factura,
 * 		 	incluya los pagos de la factura en su misma moneda, para justificar su equivalente 
 * 			en la moneda de la devolucion. 
 * 
 * **/
	@SuppressWarnings("unchecked")
	public List<Vreciboxdevolucion> obtenerDevParcialMonForaneaIng(int iCaid,String sCodsuc,String sCodcomp,
											String sMoneda,	String sTiporec,Date dtFechaArqueo,
											Date dtHoraInicio,Date dtHoraFin,Session sesion){
		
		List<Vreciboxdevolucion> lstDevol = new ArrayList<Vreciboxdevolucion>();
		List<Vreciboxdevolucion> lstDev = new ArrayList<Vreciboxdevolucion>();
		String sConsulta = "",sFecha = "";
		int iFechajul = 0;
		FechasUtil f = new FechasUtil();
		
		try {
			iFechajul = f.obtenerFechaJulianaDia(dtFechaArqueo);
			sFecha = f.formatDatetoString(dtFechaArqueo, "yyyy-MM-dd");

			sConsulta = "from Vreciboxdevolucion as v";
			sConsulta += " where v.id.caid = " + iCaid + " and v.id.codsuc = '"+ sCodsuc + "' and ";
			sConsulta += " v.id.codcomp = '" + sCodcomp	+ "' and v.id.fecha = '" + sFecha + "' and ";
			sConsulta += " v.id.tiporec = '" + sTiporec	+ "' and v.id.fechadev = " + iFechajul + " and ";
			sConsulta += " v.id.mondev = '" + sMoneda	+ "' and v.id.moneda <> '" + sMoneda + "' and ";
			sConsulta += " v.id.fechadev = v.id.fechafact and v.id.montofact = v.id.montodev and ";
			sConsulta += " v.id.mpago='"+MetodosPagoCtrl.EFECTIVO+"' and ";
			sConsulta += " v.id.hora >='"+ f.formatDatetoString(dtHoraInicio, "HH.mm.ss") + "' and ";
			sConsulta += " v.id.hora <='"+ f.formatDatetoString(dtHoraFin, "HH.mm.ss") + "' and v.id.estado <> 'A'";
			
			lstDev = sesion.createQuery(sConsulta).list();
			
			if(lstDev != null && lstDev.size() > 0 ){
				
				Criteria cr  = null;
				for (Vreciboxdevolucion v : lstDev) {
					
					//&& ==== buscar que la devolucion se haya pagado con un solo metodo y en moneda diferente a la dev y fac
					cr = sesion.createCriteria(Recibodet.class);
					cr.add(Restrictions.eq("id.caid",    v.getId().getCaid()));
					cr.add(Restrictions.eq("id.codcomp", v.getId().getCodcomp()));
					cr.add(Restrictions.eq("id.codsuc",  v.getId().getCodsuc()));
					cr.add(Restrictions.eq("id.numrec",  v.getId().getNumrec()));
					cr.add(Restrictions.eq("id.tiporec", v.getId().getTiporec()));
			
					cr.add(Restrictions.ne("id.moneda",  v.getId().getMondev()));
					cr.add(Restrictions.ne("id.moneda",  v.getId().getMonfac()));
					cr.add(Restrictions.ne("id.moneda",  sMoneda));
					
					cr.add(Restrictions.eq("id.mpago",   MetodosPagoCtrl.EFECTIVO ));

					List<Recibodet>lstDetDev = cr.list();
					
					//&& ==== la devolucion tiene solo un metodo de pago.
					if(lstDetDev.size() == 1){
						Recibodet rd = lstDetDev.get(0);
						
						//&& === buscar en el recibo original, un pago en distinta moneda del recibo. 
						cr = sesion.createCriteria(Recibodet.class);
						cr.add(Restrictions.eq("id.caid",    v.getId().getCaid()));
						cr.add(Restrictions.eq("id.codcomp", v.getId().getCodcomp()));
						cr.add(Restrictions.eq("id.codsuc",  v.getId().getCodsuc()));
						cr.add(Restrictions.eq("id.numrec",  v.getId().getNodoco()));
						cr.add(Restrictions.eq("id.moneda",  sMoneda));
						cr.add(Restrictions.eq("id.mpago",   rd.getId().getMpago()));
						cr.add(Restrictions.sqlRestriction( 						
								"(select count(distinct(moneda)) from "+PropertiesSystem.ESQUEMA+".recibodet "+
								" where caid = "+v.getId().getCaid()+
								" and codcomp = '"+v.getId().getCodcomp() +
								" ' and numrec = "+v.getId().getNodoco()+" ) = 2" ));
						
						
						List<Recibodet>lstRdt = cr.list();
						if(lstRdt != null && lstRdt.size() > 0 ){
							v.getId().setMonto(lstRdt.get(0).getMonto());
							lstDevol.add(v);
							continue;
						}
					}
				}
			}
		} catch (Exception e) {
			lstDevol = null;
			e.printStackTrace();
		}
		return  lstDevol;
	}
/** **********************************************************************************************
 * Metodo:	Permite que la devolucion total en moneda distinta a la  que se pago la la factura,
 * 		 	incluya los pagos de la factura en su misma moneda, para justificar su equivalente 
 * 			en la moneda de la devolucion. 
 * 
 * **/
	@SuppressWarnings("unchecked")
	public List<Vreciboxdevolucion>  obtenerDevParcialMonForanea(int iCaid,String sCodsuc,String sCodcomp,
											String sMoneda,	String sTiporec,Date dtFechaArqueo,
											Date dtHoraInicio,Date dtHoraFin,Session sesion){
		
		List<Vreciboxdevolucion> lstDevol = new ArrayList<Vreciboxdevolucion>();
		List<Vreciboxdevolucion> lstDev = new ArrayList<Vreciboxdevolucion>();
		String sConsulta = "",sFecha = "";
		int iFechajul = 0;
		FechasUtil f = new FechasUtil();
		
		try {
			iFechajul = f.obtenerFechaJulianaDia(dtFechaArqueo);
			sFecha = f.formatDatetoString(dtFechaArqueo, "yyyy-MM-dd");

			sConsulta = "from Vreciboxdevolucion as v";
			sConsulta += " where v.id.caid = "+iCaid+" and v.id.codsuc = '"+sCodsuc+"' and "; 
			sConsulta += " v.id.codcomp = '"+sCodcomp+"' and v.id.fecha = '"+sFecha+"' and ";
			sConsulta += " v.id.tiporec = '"+sTiporec+"' and v.id.fechadev = "+iFechajul +" and "; 
			sConsulta += " v.id.fechafact =  v.id.fechadev and ";
			sConsulta += " v.id.monfac <> '"+sMoneda+"' and v.id.moneda = '"+sMoneda+"' and ";
			sConsulta += " v.id.mpago='"+MetodosPagoCtrl.EFECTIVO+"' and v.id.montofact = v.id.montodev and";
			sConsulta += " v.id.hora >='"+f.formatDatetoString(dtHoraInicio,"HH.mm.ss")+"' and ";
			sConsulta += " v.id.hora <='"+f.formatDatetoString(dtHoraFin,"HH.mm.ss")+"' and v.id.estado <> 'A' ";
			
			lstDev = sesion.createQuery(sConsulta).list();
			
			if(lstDev != null && lstDev.size() > 0 ){
				
				Criteria cr  = null;
				for (Vreciboxdevolucion v : lstDev) {
					
					//&& ==== buscar que la devolucion se haya pagado con un solo metodo y en moneda diferente a la dev y fac
					cr = sesion.createCriteria(Recibodet.class);
					cr.add(Restrictions.eq("id.caid",    v.getId().getCaid()));
					cr.add(Restrictions.eq("id.codcomp", v.getId().getCodcomp()));
					cr.add(Restrictions.eq("id.codsuc",  v.getId().getCodsuc()));
					cr.add(Restrictions.eq("id.numrec",  v.getId().getNumrec()));
					cr.add(Restrictions.eq("id.tiporec", v.getId().getTiporec()));
					cr.add(Restrictions.ne("id.moneda",  v.getId().getMondev()));
					cr.add(Restrictions.ne("id.moneda",  v.getId().getMonfac()));
					cr.add(Restrictions.eq("id.moneda",  sMoneda));
					cr.add(Restrictions.eq("id.mpago",   MetodosPagoCtrl.EFECTIVO ));

					List<Recibodet>lstDetDev = cr.list();
					
					//&& ==== la devolucion tiene solo un metodo de pago.
					if(lstDetDev.size() == 1){
						Recibodet rd = lstDetDev.get(0);
						
						//&& === buscar en el recibo original, un pago en distinta moneda del recibo. 
						cr = sesion.createCriteria(Recibodet.class);
						cr.add(Restrictions.eq("id.caid",    v.getId().getCaid()));
						cr.add(Restrictions.eq("id.codcomp", v.getId().getCodcomp()));
						cr.add(Restrictions.eq("id.codsuc",  v.getId().getCodsuc()));
						cr.add(Restrictions.eq("id.numrec",  v.getId().getNodoco()));
						cr.add(Restrictions.ne("id.moneda",  rd.getId().getMoneda()));
						cr.add(Restrictions.eq("id.mpago",   rd.getId().getMpago()));
						cr.add(Restrictions.sqlRestriction( 						
								"(select count(distinct(moneda)) from "+PropertiesSystem.ESQUEMA+".recibodet "+
								" where caid = "+v.getId().getCaid()+
								" and codcomp = '"+v.getId().getCodcomp() +
								" ' and numrec = "+v.getId().getNodoco()+" ) = 2" ));
						
						List<Recibodet>lstRdt = cr.list();
						if(lstRdt != null && lstRdt.size() > 0 ){
							v.getId().setMonto(lstRdt.get(0).getEquiv().multiply(v.getId().getTasa()));
							lstDevol.add(v);
							continue;
						}
					}
				}
				
			}
			
		} catch (Exception e) {
			lstDevol = null;
			e.printStackTrace();
		}
		return  lstDevol;
	}
	
	/***************************************************************************************************/
	public List<BigDecimal> obtenerRecxDevmonFor(boolean ingreso,int iCaid, String sCodsuc, String sCodcomp, 
											String sMoneda, String sTiporec, Date dtFechaArqueo, 
											Date dtHoraInicio, Date dtHoraFin){
		List<BigDecimal> lstMontos = null;
		Connection cn = null;
		PreparedStatement ps = null;
		String sql = "", sFecha="",sHini = "",sHfin = "";
		FechasUtil f = new FechasUtil();
		try{
			sFecha = f.formatDatetoString(dtFechaArqueo, "yyyy-MM-dd");
			
			sql = "select rd.monto, rf.monto montofac from "+PropertiesSystem.ESQUEMA+".recibo r " +
				  " inner join "+PropertiesSystem.ESQUEMA+".recibo d on (r.caid = d.caid and r.codsuc = d.codsuc and r.codcomp = d.codcomp and r.numrec = d.nodoco  and  d.tiporec = 'DCO') " +
				  " inner join "+PropertiesSystem.ESQUEMA+".recibodet rd on (r.caid = rd.caid and r.codsuc = rd.codsuc and r.codcomp = rd.codcomp and r.numrec = rd.numrec  and  r.tiporec = rd.tiporec) " +
                  " inner join "+PropertiesSystem.ESQUEMA+".recibofac rf on (r.caid = rf.caid and r.codsuc = rf.codsuc and r.codcomp = rf.codcomp and r.numrec = rf.numrec  and  r.tiporec = rf.tiporec) " +
                  " inner join "+PropertiesSystem.ESQUEMA+".a02factco f on (rf.numfac = f.nofactura and rf.tipofactura = f.tipofactura and rf.codsuc = f.codsuc and trim(rf.codunineg) = trim(f.codunineg)) " +
                  " where  (select  count(distinct(rd1.moneda)) from  "+PropertiesSystem.ESQUEMA+".recibodet rd1  " +
                  "		where  rd1.caid = r.caid and rd1.codcomp = rd1.codcomp and " +
                  "		r.tiporec = rd1.tiporec and r.numrec = rd1.numrec) = 1 " +
                  " and r.caid = "+iCaid+" and r.codsuc = '"+sCodsuc+"' and r.codcomp = '"+sCodcomp
                  	+"'  and r.fecha = '"+sFecha+"' and r.estado <> 'A' and d.estado <> 'A' " +
                  " and r.tiporec = 'CO' ";
			
			if(ingreso)
				sql += "and rd.moneda = '"+sMoneda+"' and f.moneda <> '"+sMoneda+"'";
			else
				sql += "and rd.moneda <> '"+sMoneda+"' and f.moneda = '"+sMoneda+"'";
			
			sHini = f.formatDatetoString(dtHoraInicio, "HH.mm.ss");
			sHfin = f.formatDatetoString(dtHoraFin, "HH.mm.ss");
			sql += " and r.hora >='" + sHini + "' and r.hora<='"
					+ sHfin + "'";

			LogCajaService.CreateLog("obtenerRecxDevmonFor", "QRY", sql);
			
			As400Connection as400connection = new As400Connection();
			cn = as400connection.getJNDIConnection("DSMCAJAR");
			ps = cn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = ps.executeQuery();
			rs.last();
			int r = rs.getRow();
			rs.beforeFirst();
			if (r > 0){
				lstMontos = new ArrayList();
				if(ingreso){					
					while(rs.next()){
						lstMontos.add(rs.getBigDecimal("MONTO"));
					}
				}else{
					while(rs.next()){
						lstMontos.add(rs.getBigDecimal("MONTOFAC"));
					}
				}
			}
			
		}catch(Exception ex){
			LogCajaService.CreateLog("obtenerRecxDevmonFor", "ERR", ex.getMessage());
		}finally {
			try {
				ps.close();
				cn.close();
			} catch (Exception se2) {
				LogCajaService.CreateLog("obtenerRecxDevmonFor", "ERR", se2.getMessage());
			}
		}
		return lstMontos;
	}
/***************************************************************************************************/
	public boolean isPagoOriginalFullForaneo(int caid,String codcomp,int numrec,String moneda){
		boolean full = true;
		Connection cn = null;
		PreparedStatement ps = null;
		String sql = "", moneda2="";
		Session sesion = null;
		Transaction trans = null;
		boolean bNuevaSesionCaja = false;
		
		try{
			
			sql = "select distinct moneda from "+PropertiesSystem.ESQUEMA+".recibodet where caid = "
					+ caid +" and codcomp = '" + codcomp
					+ "' and numrec = " + numrec;
			
			sesion = HibernateUtilPruebaCn.currentSession();
			if( sesion.getTransaction().isActive() )
				trans = sesion.getTransaction();
			else{
				trans = sesion.beginTransaction();
				bNuevaSesionCaja = true;
			}
			
			List<String>lstMonedas = (ArrayList<String>)sesion
										.createSQLQuery(sql).list();
		
			if(lstMonedas != null && !lstMonedas.isEmpty()){
				if(lstMonedas.size() > 1 || moneda.compareTo
					(lstMonedas.get(0) ) != 0 )
					full = false;
			}
//			As400Connection as400connection = new As400Connection();
//			cn = as400connection.getJNDIConnection("DSMCAJAR");
//			
//			ps = cn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
//			ResultSet rs = ps.executeQuery();
//			rs.last();
//			int r = rs.getRow();
//			rs.beforeFirst();
//			
//			if (r > 1){full = false;}
//			else {
//				if(rs.next()){
//					moneda2 = rs.getString("moneda");
//					if(!moneda.equals(moneda2))
//						full = false;
//				}
//			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}finally {
			if(bNuevaSesionCaja){
				try{trans.commit();}catch(Exception e){e.printStackTrace();}
				try{HibernateUtilPruebaCn.closeSession(sesion);}
				catch(Exception e){e.printStackTrace();}
			}

		}
		return full;
	}
/***************************************************************************************************/
	public boolean isPagoOriginalMixto(int caid,String codcomp,int numrec,String moneda){
		boolean mixto = false;
		String sql = "";
		Session sesion = null;
		
		try{

			sql = " select count( distinct(moneda) ) from "+PropertiesSystem.ESQUEMA+".recibodet ";
			sql += " where caid = "+caid+" and codcomp = '"+codcomp+"'";
			sql += " and numrec = "+numrec +" and mpago = '"+MetodosPagoCtrl.EFECTIVO+"'";
			
			sesion = HibernateUtilPruebaCn.currentSession();

			LogCajaService.CreateLog("isPagoOriginalMixto", "QRY", sql);
			
			Integer iCant = (Integer)sesion.createSQLQuery(sql).uniqueResult();
			if (iCant > 1)
				mixto = true;

		}catch(Exception ex){
			LogCajaService.CreateLog("isPagoOriginalMixto", "ERR", ex.getMessage());
		}

		return mixto;
	}
/***************************************************************************************************/
/** Obtiene arqueos de caja a la fecha y en un intervalo de horas.
 *  Fecha:  17/01/2011
 *  Hecho: Carlos Manuel Hernández Morrison.
 */	
	public List<Arqueo> obtenerArqueosPorFechaHora(int iCaid, String sCodcomp, String sCodsuc, String sMoneda,
													Date dtFechaArqueo, Date dtHoraInicio, Date dtHoraFin){
		List<Arqueo> lstArqueo = null;
		Session sesion =  null;
		
		try {
			sesion = HibernateUtilPruebaCn.currentSession();
			
			Criteria cr = sesion.createCriteria(Arqueo.class)
				.add(Restrictions.eq("id.caid", iCaid))
				.add(Restrictions.eq("id.codcomp", sCodcomp))
				.add(Restrictions.eq("id.codsuc", sCodsuc))
				.add(Restrictions.eq("moneda", sMoneda))
				.add(Restrictions.ne("estado", "R"))
				.add(Restrictions.eq("id.fecha", dtFechaArqueo))
				.add(Restrictions.between("id.hora", dtHoraInicio, dtHoraFin));
			
			LogCajaService.CreateLog("obtenerArqueosPorFechaHora", "HQRY", LogCajaService.toSql(cr));
			lstArqueo = (ArrayList<Arqueo>)cr.list();

			if(lstArqueo == null)
				lstArqueo = new ArrayList<Arqueo>(0);

		} catch (Exception error) {
			lstArqueo = null;
			LogCajaService.CreateLog("obtenerArqueosPorFechaHora", "ERR", error.getMessage());
		}

		return lstArqueo;
	}
	
	
/****************************************************************************************/
/** Leer las facturas del día, crédito, contado y devoluciones para la caja seleccionada 
 * 	solamente por traslados
 *  Fecha:  01/04/2011
 *  Hecho: Carlos Manuel Hernández Morrison.
 */	
	@SuppressWarnings("unchecked")
	public List<Vhfactura> obtenerFacturasDiaxTraslado(int iCaid, String sCodcomp,Date dtFechaArqueo,String sMoneda,
													Date dtHoraInicio, Date dtHoraFin,Session sesion ){
		String sql = "";
		String sRangoHoraTf="";
		int iFechaActual=0;
		ArrayList<Vhfactura>lstFacturas = null;
		
		try {
			iFechaActual  = new FacturaCrtl().obtenerFechaActualJuliana(dtFechaArqueo);
			sRangoHoraTf += " and cast(tf.fecha as time) between";
			sRangoHoraTf += "'" +new SimpleDateFormat("HH:mm:ss").format(dtHoraInicio)+"' and ";
			sRangoHoraTf += "'" +new SimpleDateFormat("HH:mm:ss").format(dtHoraFin)+"'";	
			
			sql =  " select f.* from "+PropertiesSystem.ESQUEMA+".Vhfactura as f inner join "+PropertiesSystem.ESQUEMA+".trasladofac tf" ; 
			sql += " on  tf.nofactura = f.nofactura and tf.codcomp = f.codcomp and tf.tipofactura = f.tipofactura";
			sql += " and tf.codsuc = f.codsuc and trim(tf.codunineg)  = trim(f.codunineg) and tf.estadotr in ('E','P')";
			sql += " and tf.caiddest = "+iCaid+" and f.fecha = "+ iFechaActual +" and trim(f.estado)<>'A' ";
			sql += " and f.codcomp = '"+sCodcomp+"' and f.moneda = '"+ sMoneda+"'";
			sql += (sRangoHoraTf);
			
			lstFacturas  = (ArrayList<Vhfactura>)sesion.createSQLQuery(sql).addEntity(Vhfactura.class).list();
			
		} catch (Exception e) {
			lstFacturas = null;
			e.printStackTrace();
		}
		return lstFacturas;
	}
/****************************************************************************************/
/** Leer las facturas del día, crédito, contado y devoluciones para la caja seleccionada
 *  Fecha:  02/09/2010
 *  Hecho: Carlos Manuel Hernández Morrison.
 */	
	public List<?> obtenerFacturaDeldia(String[] sTiposDoc ,F55ca017[] f55ca017,List lstLocalizaciones,
											int iCaid, String sMoneda, String sCodsuc,String sCodcomp,
											Date dtFechaArqueo,Date dtHoraInicio, Date dtHoraFin,Session sesion){		
		List<?> lstFac = new ArrayList();
		FacturaCrtl faCtrl = new FacturaCrtl();
		StringBuilder sbSql1 = new StringBuilder();
		StringBuilder sbSql2 = new StringBuilder();
		StringBuilder sbExcluidas = new StringBuilder();
		int iFechaActual=0; 
		String sRangoFecha = "";
		String sRangoHoraTf="";
		boolean bHayLoc = false,bSoloLocs = false;
		List<F55ca017> f17_1 = new ArrayList<F55ca017>(), f17_2 = new ArrayList<F55ca017>();
		
		try {
			iFechaActual = faCtrl.obtenerFechaActualJuliana(dtFechaArqueo);
			
			//---- Filtrar en un rango de fechas.			
			SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
			String sHoraI = dfHora.format(dtHoraInicio);
			String sHoraF = dfHora.format(dtHoraFin);			
			String sHini[],sHfin[];
			sHini = sHoraI.split(":");
			sHfin = sHoraF.split(":");
			int iHini = 0,iHfin = 0;
			iHini = Integer.parseInt(sHini[0]+""+sHini[1]+""+sHini[2]);
			iHfin = Integer.parseInt(sHfin[0]+""+sHfin[1]+""+sHfin[2]);
			sRangoFecha  += " and f.hora >= " +iHini+ " and f.hora <= " +iHfin;
			sRangoHoraTf += " and cast(tf.fecha as time) between '" +sHoraI+ "' and '"+sHoraF+"' ";
			
			sbSql1.append(" select * from "+PropertiesSystem.ESQUEMA+".Vhfactura as f where f.estado <> 'A' ");
			sbSql1.append(" and lower(trim(f.pantalla)) not like '%pocket%'");
			sbSql1.append(" and f.codcomp = '"+sCodcomp+"' and f.fecha =" + iFechaActual);
			sbSql1.append(" and trim(f.moneda) = '"+sMoneda + "'");
			sbSql1.append(sRangoFecha);
			
			//---- separar los que tienen localizaciones 
			for (F55ca017 f17 : f55ca017) {
				if(f17.getId().getC7locn().trim().equals("")){
					f17_1.add(f17);
					bSoloLocs = true;
				}else{
					bHayLoc = true;
					f17_2.add(f17);
				}
			}
			//---- Tipos de Factura.
			if(sTiposDoc!=null && sTiposDoc.length>0){
				sbSql1.append(" and f.tipofactura in (");
				for (int i = 0; i < sTiposDoc.length; i++) 
					sbSql1.append((i == sTiposDoc.length - 1)? 
							 	 ("'" + sTiposDoc[i] + "')") : 
							 	 ("'" + sTiposDoc[i] + "',"));
			}
			sbSql2.append(sbSql1.toString());
			
			//---- agregar unidades de negocio
			sbSql1.append(" and trim(f.sdlocn) = '' and trim(f.codunineg) in (");
			for(byte i = 0; i < f17_1.size(); i++){
				sbSql1.append((i == f17_1.size() - 1)?
								"'" + ((F55ca017)f17_1.get(i)) .getId().getC7mcul().trim() + "')":
								"'" + ((F55ca017)f17_1.get(i)) .getId().getC7mcul().trim() + "',");
			}
			//--- Excluir las facturas que fueron enviadas hacia otra caja. 
			sbExcluidas.append(" and f.nofactura not in( select tf.nofactura from "+PropertiesSystem.ESQUEMA+".trasladofac tf");
			sbExcluidas.append(" where tf.nofactura = f.nofactura and tf.codcomp = f.codcomp and tf.tipofactura = f.tipofactura");
			sbExcluidas.append(" and tf.codsuc = f.codsuc and trim(tf.codunineg)  = trim(f.codunineg)");
			sbExcluidas.append(" and tf.estadotr in ('E','R','P') and tf.caidprop = "+iCaid );
			sbExcluidas.append(" and f.codcomp = '"+sCodcomp+"' and f.moneda = '"+ sMoneda + "' )");
			
			sbSql1.append(sbExcluidas);
			
			//--- Incluir las facturas enviadas desde otra caja.
			sbSql1.append("\n UNION \n");
			sbSql1.append(" select f.* from "+PropertiesSystem.ESQUEMA+".Vhfactura as f inner join "+PropertiesSystem.ESQUEMA+".trasladofac tf"); 
			sbSql1.append(" on  tf.nofactura = f.nofactura and tf.codcomp = f.codcomp and tf.tipofactura = f.tipofactura");
			sbSql1.append(" and tf.codsuc = f.codsuc and trim(tf.codunineg)  = trim(f.codunineg) and tf.estadotr in ('E','P')");
			sbSql1.append(" and tf.caiddest = "+iCaid+" and f.fecha = "+ iFechaActual +" and trim(f.estado)<>'A' ");
			sbSql1.append(" and f.codcomp = '"+sCodcomp+"' and f.moneda = '"+ sMoneda+"'");
			sbSql1.append(" and lower(trim(f.pantalla)) not like '%pocket%'");
			sbSql1.append(sRangoHoraTf);
			
			//---- Localizaciones: Construir segunda consulta.
			if(bHayLoc){
				String sLocals=" and trim(f.sdlocn) in (";
				String sUnineg=" and trim(f.codunineg) in (";
								
				//--- Localizaciones y unidades de negocio.
				for (Iterator iter = f17_2.iterator(); iter.hasNext();) {
					F55ca017 f17 = (F55ca017) iter.next();
					if(!iter.hasNext()){
						sLocals += "'" + f17.getId().getC7locn().trim()+"')";
						sUnineg += "'" + f17.getId().getC7mcul().trim()+"')";
					}else{
						sLocals += "'" +  f17.getId().getC7locn().trim()+"',";
						sUnineg += "'" +  f17.getId().getC7mcul().trim()+"',";
					}
				}
				sbSql2.append(sLocals);
				sbSql2.append(sUnineg);
				sbSql2.append(sbExcluidas);
				sbSql2.append(sRangoFecha);
				
				sbSql1.append("\n UNION \n");
				sbSql1.append(sbSql2);
			}
			if(bSoloLocs){
				lstFac = sesion.createSQLQuery(sbSql1.toString()).addEntity(Vhfactura.class).list();
			}else{
				sbSql2.append(" \n UNION \n");
				sbSql2.append(" select f.* from "+PropertiesSystem.ESQUEMA+".Vhfactura as f inner join "+PropertiesSystem.ESQUEMA+".trasladofac tf"); 
				sbSql2.append(" on  tf.nofactura = f.nofactura and tf.codcomp = f.codcomp and tf.tipofactura = f.tipofactura");
				sbSql2.append(" and tf.codsuc = f.codsuc and trim(tf.codunineg)  = trim(f.codunineg) and tf.estadotr in ('E','P')");
				sbSql2.append(" and tf.caiddest = "+iCaid+" and f.fecha = "+ iFechaActual +" and trim(f.estado)<>'A' ");
				sbSql2.append(" and f.codcomp = '"+sCodcomp+"' and f.moneda = '"+ sMoneda+"'");
				sbSql2.append(" and lower(trim(f.pantalla)) not like '%pocket%'");
				sbSql2.append(sRangoHoraTf);
				lstFac = sesion.createSQLQuery(sbSql2.toString()).addEntity(Vhfactura.class).list();
			}
		} catch (Exception error) {
			lstFac = null;
			error.printStackTrace();
		} finally {

		}
		return lstFac;
	}
	
	
/*************************************************************************************************/
/** cargar lista con los numeros de recibos incluidos en el arqueo
 *********/	
		public String cargarRecibosArqueo(int caid,String codcomp,String codsuc,int noarqueo,Date dtFecha,String sMoneda, Date dtHini){
			List lstArqueorec = new ArrayList();
			Session sesion = HibernateUtilPruebaCn.currentSession();
			List lstRecibos  = new ArrayList() ;
			String consulta = "",sFecha = "",sLstRecibos = "";
			FechasUtil f = new FechasUtil();
			
			try{
				sFecha = f.formatDatetoString(dtFecha,"yyyy-MM-dd");
				consulta =  " select distinct(v.id.numrec) from Vrecibodatos as v where v.id.caid = "+caid+" and v.id.codcomp ='"+codcomp+"'";
				consulta += " and v.id.codsuc = '"+codsuc+"' and v.id.fecha = '"+sFecha+"' and v.id.restado <>'A'";
				consulta += " and (v.id.moneda ='"+sMoneda+"' or v.id.moncam = '"+sMoneda+"' or v.id.monfact = '"+sMoneda+"')";
				
				//--- Agregar filtro de intervalo de horas en caso de arqueos previos.
				if(dtHini!=null){
					String sHini,sHfin;				
					sHini = f.formatDatetoString(dtHini, "HH.mm.ss");
					sHfin = f.formatDatetoString(dtFecha,"HH.mm.ss");
					consulta += " and v.id.hora >='"+sHini+"' and v.id.hora<='"+sHfin+"'";
				}
				lstRecibos = sesion.createQuery(consulta).list();
				
				if(lstRecibos != null && lstRecibos.size()>0){
					sLstRecibos = "(";
					for(int i=0; i<lstArqueorec.size(); i++){					
						sLstRecibos += lstArqueorec.get(i).toString();
						sLstRecibos += (i==lstArqueorec.size()-1)? ")" : ",";
					}
				}			
			}catch(Exception error){
				sLstRecibos = "";
				error.printStackTrace();
			}
			return sLstRecibos;
		}	
	
/***************************************************************************************************/
/** Obtiene los arqueos de una caja por fecha.
 *  Fecha:  05/06/2010
 *  Hecho: Carlos Manuel Hernández Morrison.
 */	

	public List<Arqueo> obtenerArqueosCaja(int iCaid, Date dtFecha){
		List<Arqueo> lstArqueos = null;
		Session sesion = null;
		Transaction trans = null;
		String sql = "",sFecha = "";
		FechasUtil f = new FechasUtil();
		boolean bNuevaSesionCaja = false;
		
		try {
			
			sesion = HibernateUtilPruebaCn.currentSession();
			if( sesion.getTransaction().isActive() )
				trans = sesion.getTransaction();
			else{
				trans = sesion.beginTransaction();
				bNuevaSesionCaja = true;
			}
			
			sFecha = f.formatDatetoString(dtFecha, "yyyy-MM-dd");
			sql += " from Arqueo a where a.id.caid = "+iCaid +" and a.id.fecha = '"+sFecha+"'";
			sql += " order by a.id.hora desc";
			
			lstArqueos = sesion.createQuery(sql).list();
			
			if(bNuevaSesionCaja)
				trans.commit();

		} catch (Exception error) {
			lstArqueos = null;
			error.printStackTrace();
		}finally{
			if(bNuevaSesionCaja){
				try{HibernateUtilPruebaCn.closeSession(sesion);}catch(Exception e){e.printStackTrace();}
			}
		}
		return lstArqueos;
	}
/***************************************************************************************************/
/** Lee los recibos que hayan aplicado un cambio de tasa, para cualquier compañía, sucursal y moneda.
 *  Fecha:  05/06/2010
 *  Hecho: Carlos Manuel Hernández Morrison.
 */	
	public List<Recibo> obtenerRecibosCambioTasa(int iCaid,Date dtFecha,Date dtHini){
		List<Recibo> lstRecibos = null;
		Divisas dv = new Divisas();
		FechasUtil f = new FechasUtil();
		Session sesion = null;
		Transaction trans = null;
		String sql = "",sFecha="";
		int[] iNobatch = null;
		boolean bNuevaSesionCaja = false;
		
		try {
			sesion = HibernateUtilPruebaCn.currentSession();
			if( sesion.getTransaction().isActive() )
				trans = sesion.getTransaction();
			else{
				trans = sesion.beginTransaction();
				bNuevaSesionCaja = true;
			}
			
			sFecha = f.formatDatetoString(dtFecha, "yyyy-MM-dd");
			
			sql +=" from Recibo r where r.id.caid = "+iCaid+" and r.fecha = '"+sFecha+"'";
			sql +=" and r.estado<>'A' and r.motivoct <>''";
			
			if(dtHini!=null){	
				String sHini = "", sHfin="";
				sHini = f.formatDatetoString(dtHini, "HH.mm.ss");
				sHfin = f.formatDatetoString(dtFecha,"HH.mm.ss");
				sql += " and r.hora >='"+sHini+"' and r.hora<='"+sHfin+"'";
			}			
		
			List lstRec = sesion.createQuery(sql).list();
			
			//----- Leer la tasa para aplicada al recibo		
			if(lstRec != null && !lstRec.isEmpty()){
				for(int i=0; i<lstRec.size();i++){
					Recibo r = (Recibo) lstRec.get(i);
					sql =  " select distinct(r.tasa) from Recibodet as r where r.id.caid = "+iCaid;
					sql += " and r.id.codsuc = '" +r.getId().getCodsuc()+ "' and r.id.codcomp = '"+r.getId().getCodcomp()+"'";
					sql += " and r.id.numrec = "  +r.getId().getNumrec()+ "  and r.id.tiporec = '" +r.getId().getTiporec()+"'";
					sql += " and r.tasa >= 1";
					
					String sTasa="";
					List lstRecibodet = sesion.createQuery(sql).list();
					if(lstRecibodet!=null && lstRecibodet.size()>0){
						sTasa = dv.formatDouble((new BigDecimal( lstRecibodet.get(0).toString())).doubleValue());
						r.setMotivo(sTasa);
						lstRec.remove(i);
						lstRec.add(i,r);
					}
				}
			}
			
			//----- Asignar la lista de batch aplicados con el recibo.
			if(lstRec != null && lstRec.size() > 0){
				
				lstRecibos = new ArrayList<Recibo>();
				com.casapellas.controles.tmp.ReciboCtrl rcCtrl = new com.casapellas.controles.tmp.ReciboCtrl();
				
				for (Iterator i = lstRec.iterator(); i.hasNext();) {
					StringBuilder sbNoBatchs = new StringBuilder();
					Recibo r = (Recibo) i.next();
					
					//---- Leer los números de batchs para el recibo.
					iNobatch = rcCtrl.obtenerBatchxRecibo2(r.getId()
							.getNumrec(), r.getId().getCaid(), r.getId()
							.getCodsuc(), r.getId().getCodcomp(), r.getId()
							.getTiporec());
					
					if(iNobatch != null && iNobatch.length > 0){
						for(int j = 0; j < iNobatch.length; j++)
							sbNoBatchs.append(iNobatch[j]);
						r.setNobatch(sbNoBatchs.toString());
					}
					lstRecibos.add(r);
				}
			}
		} catch (Exception error) {
			error.printStackTrace();
		}finally{
			if(bNuevaSesionCaja){
				try{trans.commit();}catch(Exception e){e.printStackTrace();}
				try{HibernateUtilPruebaCn.closeSession(sesion);}
				catch(Exception e){e.printStackTrace();}
			}
		}
		return lstRecibos;
	}
	

	
/****************************************************************************************/
/** Leer las facturas del día, crédito y contado para la caja seleccionada
 *  Fecha:  05/06/2010
 *  Hecho: Carlos Manuel Hernández Morrison.
 */	
	public List<?> obtenerFacturaDeldia1(String[] sTiposDoc ,F55ca017[] f55ca017,List lstLocalizaciones,
								int iCaid, String sMoneda, String sCodsuc,String sCodcomp,Date dtHoraInicio){	
		List<?> lstFac = new ArrayList();		
		Session sesion =HibernateUtilPruebaCn.currentSession();
		FacturaCrtl faCtrl = new FacturaCrtl();
		StringBuilder sbSql = new StringBuilder();
		int iFechaActual=0; 
		String sRangoFecha = "";
		
		try {
			//---- Filtrar en un rango de fechas.			
			if(dtHoraInicio!=null){
				SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
				String sHoraI = dfHora.format(dtHoraInicio);
				String sHoraF = dfHora.format(new Date());			
				String sHini[],sHfin[];
				sHini = sHoraI.split(":");
				sHfin = sHoraF.split(":");
				int iHini = 0,iHfin = 0;
				iHini = Integer.parseInt(sHini[0]+""+sHini[1]+""+sHini[2]);
				iHfin = Integer.parseInt(sHfin[0]+""+sHfin[1]+""+sHfin[2]);
				sRangoFecha += " and f.hora >= " + iHini ;
				sRangoFecha += " and f.hora <= " +iHfin;
			}
			iFechaActual = faCtrl.obtenerFechaActualJuliana();
			sbSql.append(" select * from "+PropertiesSystem.ESQUEMA+".Vhfactura as f where f.estado <> 'A' and");
			sbSql.append(" f.codcomp = '"+sCodcomp+"' and f.fecha =" + iFechaActual);
			sbSql.append(" and trim(f.moneda) = '"+sMoneda + "'");
			if(dtHoraInicio!=null)
				sbSql.append(sRangoFecha);
			
			//---- Tipos de Factura.
			if(sTiposDoc!=null && sTiposDoc.length>0){
				sbSql.append(" and f.tipofactura in (");
				for (int i = 0; i < sTiposDoc.length; i++) 
					sbSql.append((i == sTiposDoc.length - 1)? 
							 	 ("'" + sTiposDoc[i] + "')") : ("'" + sTiposDoc[i] + "',"));
			}
			//---- Unidades de Negocio.
			if(f55ca017!=null && f55ca017.length>0){
				sbSql.append(" and trim(f.codunineg) in (");
				for(int i = 0; i < f55ca017.length; i++)
					sbSql.append((i == f55ca017.length - 1)?
									("'" + f55ca017[i].getId().getC7mcul().trim() + "')"): 
									("'" + f55ca017[i].getId().getC7mcul().trim() + "',"));
			}
			//---- Localizaciones (si hay) 
			if(lstLocalizaciones != null && lstLocalizaciones.size()>0){
				sbSql.append(" and trim(f.sdlocn) in (");
				for (Iterator iter = lstLocalizaciones.iterator(); iter.hasNext();) {
					String sLoc = (String) iter.next();
					sbSql.append(iter.hasNext()? ("'"+ sLoc.trim()+ "',"):("'"+sLoc.trim()+"')"));
				}
			}
			//--- Excluir las facturas que fueron enviadas hacia otra caja. 
			sbSql.append(" and f.nofactura not in( select tf.nofactura from "+PropertiesSystem.ESQUEMA+".trasladofac tf ");
			sbSql.append(" where tf.nofactura = f.nofactura and tf.codcomp = f.codcomp and tf.tipofactura = f.tipofactura");
			sbSql.append(" and tf.codsuc = f.codsuc and trim(tf.codunineg)  = trim(f.codunineg)");
			sbSql.append(" and tf.estadotr in ('E','R','P') and tf.caidorig = "+iCaid );
			sbSql.append(" and f.codcomp = '"+sCodcomp+"' and f.moneda = '"+ sMoneda + "' )");
			//--- Incluir las facturas enviadas desde otra caja.
			sbSql.append(" UNION ");
			sbSql.append(" select f.* from "+PropertiesSystem.ESQUEMA+".Vhfactura as f inner join "+PropertiesSystem.ESQUEMA+".trasladofac tf"); 
			sbSql.append(" on  tf.nofactura = f.nofactura and tf.codcomp = f.codcomp and tf.tipofactura = f.tipofactura");
			sbSql.append(" and tf.codsuc = f.codsuc and trim(tf.codunineg)  = trim(f.codunineg) and tf.estadotr in ('E','P')");
			sbSql.append(" and tf.caiddest = "+iCaid+" and f.fecha = "+ iFechaActual +" and trim(f.estado)<>'A' ");
			sbSql.append(" and f.codcomp = '"+sCodcomp+"' and f.moneda = '"+ sMoneda+"'");
			if(dtHoraInicio!=null)
				sbSql.append(sRangoFecha);
			
			lstFac = sesion.createSQLQuery(sbSql.toString()).addEntity(Vhfactura.class).list();

			
		} catch (Exception error) {
			error.printStackTrace();
		} 
		return lstFac;
	}
/********************************************************************************/
/** Obtener Lista de arqueos por fecha para una caja, ordenados por fecha	  **/
	public List obtenerArqueos(int iCaid, String sCodcomp,String sCodsuc,String sMoneda,Date dtFechaAct,String sEstado){
		List lstArqueos = new ArrayList();
		Session sesion = null;
		String sql = "",sFecha = "";

		try {			
			
			sesion = HibernateUtilPruebaCn.currentSession();

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			sFecha = format.format(dtFechaAct);
			
			sql =  " select * from "+PropertiesSystem.ESQUEMA+".arqueo where caid = "+iCaid +" and codcomp = '" +sCodcomp+ "'";
			sql += " and codsuc = '"+sCodsuc+"' and moneda = '"+sMoneda+"' and fecha = '"+sFecha+"'";
			sql += " and estado in "+sEstado + " order by hora desc";
			
			LogCajaService.CreateLog("obtenerArqueos", "QRY", sql);
			
			lstArqueos = sesion.createSQLQuery(sql).addEntity(Arqueo.class).list();

		} catch (Exception error) {
			lstArqueos = null;
			LogCajaService.CreateLog("obtenerArqueos", "ERR", error.getMessage());
		}

		return lstArqueos;
	}
	
/** Sin uso ****** 1. cargar los datos para los cambios otorgados x pago de recibos ********************/
	public List cargarDetCambios(int iCaid,String sCodcomp,String sCodsuc,String sTipoMoneda){
		List lstCambios = new ArrayList();
		Session sesion =HibernateUtilPruebaCn.currentSession();
		
		String consulta = "";
		
		try{
			consulta =  "from Vdetallecambiorecibo as v where v.id.caid = "+iCaid+
					    "  and v.id.codcomp = '"+sCodcomp+"' and v.id.moneda = '"+sTipoMoneda+
					    "' and v.id.fecha = current_date and v.id.cambio > 0 and v.id.codsuc = '"+sCodsuc+"'";									
			consulta += "  and v.id.tiporec not in ('CO','FCV')  order by v.id.numrec";
			
			
			lstCambios = sesion.createQuery(consulta).list();
			
			if(lstCambios!=null && lstCambios.size()>0){
				for(int i=0;i<lstCambios.size();i++){
					Vdetallecambiorecibo v = (Vdetallecambiorecibo)lstCambios.get(i);
					v.setCambio(v.getId().getCambio());
					lstCambios.remove(i);
					lstCambios.add(i,v);
				}
			}			
			
			
		}catch(Exception error){
			error.printStackTrace();
		}	
		return lstCambios;
	}
	
/********* 1. cargar los datos para los cambios otorgados x pago de recibos *******************
 * 	se incluyen todos los recibos por los cambios de pago con 1 moneda diferente de la fact ***/
//	public List cargarDetCambio(int iCaid,String sCodcomp,String sCodsuc,String sMoneda,Date dtFecha,Date dtHini){
	public List cargarDetCambio(int iCaid,String sCodcomp,String sCodsuc,String sMoneda,
								Date dtFechaArqueo,Date dtHoraInicio,Date dtHoraFin,Session sesion){
		List lstCambios = new ArrayList();
		FechasUtil f = new FechasUtil();
		String sFecha;
		String sHini;
		String sHfin;
		
		try{
			sFecha = f.formatDatetoString(dtFechaArqueo, "yyyy-MM-dd");
			sHini = f.formatDatetoString(dtHoraInicio, "HH.mm.ss");
			sHfin = f.formatDatetoString(dtHoraFin, "HH.mm.ss");
			
			Criteria cr = sesion.createCriteria(Vdetallecambiorecibo.class);
			cr.add(Restrictions.eq("id.caid"		,iCaid ));
			cr.add(Restrictions.eq("id.codcomp"		,sCodcomp ));
			cr.add(Restrictions.eq("id.codsuc"		,sCodsuc ));
			cr.add(Restrictions.eq("id.monedacamb"	,sMoneda ));
			cr.add(Restrictions.gt("id.cambio"		,new BigDecimal(0)));
			cr.add(Restrictions.eq("id.fecha"		,dtFechaArqueo ));
			cr.add(Restrictions.ne("id.estado"		,"A" ));
			cr.add(Restrictions.ne("id.tiporec"		,"FCV" ));
			cr.add(Restrictions.eq("id.mpago"		,MetodosPagoCtrl.EFECTIVO  ));
			cr.add(Restrictions.between("id.hora"	,dtHoraInicio, dtHoraFin));
			cr.addOrder(Order.asc("id.numrec"));
			
			lstCambios = cr.list();
			if(lstCambios==null)
				lstCambios = new ArrayList(0);
			
		
		}catch(Exception error){
			lstCambios = null;
			error.printStackTrace();
		}	
		return lstCambios;
	}	
	
/********* 1.1  cargar los realizados cambios a partir de una lista de recibos  ***************/
	public List cargarRecxCambios(int iCaid,String sCodcomp,String sCodsuc,String numRecibos,Date fecha,String sMoneda){
		List lstCambios = new ArrayList();
		Session sesion = HibernateUtilPruebaCn.currentSession();
		
		String consulta = "";
		
		try{			
			consulta =  " from Vdetallecambiorec as v where v.id.caid = "+iCaid;
			consulta += " and v.id.codsuc = '"+sCodsuc+"' and v.id.codcomp = '"+sCodcomp+"'";
			consulta += " and v.id.fecha = '"+fecha+"' and v.id.cambio >0 and v.id.monedacamb = '"+sMoneda+"'";
			consulta += " and v.id.tiporec <> 'FCV'";
			
		
			lstCambios = sesion.createQuery(consulta).list();
					
			
		}catch(Exception error){
			error.printStackTrace();
		}
		return lstCambios;
	}
/********* 1.2 obtener los recibos de cualquier tipo CR, CO que aplicaron cambios en el día *****/
	//public List obtenerRecibosxcambios(int iCaid,String sCodcomp,String sCodsuc,String sMoneda,Date dtFecha,Date dtHini){
	public List obtenerRecibosxcambios(int iCaid,String sCodcomp,String sCodsuc,String sMoneda,
									Date dtFechaArqueo,Date dtHoraInicio, Date dtHoraFin,Session sesion){
		List<Vdetallecambiorec> lstRec = new ArrayList<Vdetallecambiorec>();
		
		try{
			
			Criteria cr = sesion.createCriteria(Vdetallecambiorec.class);
			cr.add(Restrictions.eq("id.caid",iCaid) );
			cr.add(Restrictions.eq("id.codsuc",sCodsuc) );
			cr.add(Restrictions.eq("id.codcomp", sCodcomp));
			cr.add(Restrictions.eq("id.monedacamb",sMoneda) );
			cr.add(Restrictions.ne("id.estado","A"));
			cr.add(Restrictions.eq("id.fecha",dtFechaArqueo ));
			cr.add(Restrictions.eq("id.fecha",dtFechaArqueo ));
			cr.add(Restrictions.gt("id.cambio",new BigDecimal("0")));
			cr.add(Restrictions.ne("id.tiporec","FCV" ));
			cr.add(Restrictions.between("id.hora", dtHoraInicio, dtHoraFin));
			
			lstRec = (ArrayList<Vdetallecambiorec>)cr.list();
						
		}catch(Exception error){
			lstRec = null;
			error.printStackTrace();
		}		
		return lstRec;
	}	
/********* 1.3 obtener los recibos que aplican cambios mixtos *****/
	public List obtenerRecibosxcambiomixto(int iCaid,String sCodcomp,String sCodsuc,String sMoneda,
											Date dtFechaArqueo,Date dtHoraInicio,Date dtHoraFin,Session sesion){
		List<Vdetallecambiorecibo> lstRec = new ArrayList<Vdetallecambiorecibo>();
		String sConsulta = "",sFecha = "", sHini,sHfin; ;
		FechasUtil f = new FechasUtil();
		List<Vdetallecambiorecibo> lstRecibos = new ArrayList<Vdetallecambiorecibo>();
		
		try{
			sFecha = f.formatDatetoString(dtFechaArqueo, "yyyy-MM-dd");
			
			sConsulta =  " from Vdetallecambiorecibo as v where v.id.caid = "+iCaid;
			sConsulta += " and v.id.codsuc = '"+sCodsuc+"' and v.id.codcomp = '"+sCodcomp+"'";
			sConsulta += " and v.id.monedafac = '"+sMoneda+"' and v.id.monedarec = '"+sMoneda+"'";
			sConsulta += " and  v.id.monedacamb <> '"+sMoneda+"'";
			sConsulta += " and v.id.fecha = '"+sFecha+"' and v.id.cambio > 0 and v.id.estado <> 'A'";
			sConsulta += " and v.id.tiporec = 'CO' ";
			sConsulta += " and v.id.mpago = '"+MetodosPagoCtrl.EFECTIVO+"' ";

			sHini = f.formatDatetoString(dtHoraInicio,"HH.mm.ss");
			sHfin = f.formatDatetoString(dtHoraFin, "HH.mm.ss");
			sConsulta += " and v.id.hora >='"+sHini+"' and v.id.hora<='"+sHfin+"'";
			
			lstRec = sesion.createQuery(sConsulta).list();

			if(lstRec != null && lstRec.size() > 0){
				int iTotalRec = 0 ;
				Criteria cr = null;
				Recibodet rd = null;
				iTotalRec = lstRec.size();
				
				for (int i = 0; i < iTotalRec; i++) {
					Vdetallecambiorecibo v  =  lstRec.get(i);
					if(v.getId().getCantmon() == 2){
						cr = sesion.createCriteria(Recibodet.class);
						cr.add(Restrictions.eq("id.codsuc", v.getId().getCodsuc()));
						cr.add(Restrictions.eq("id.caid", v.getId().getCaid()));
						cr.add(Restrictions.eq("id.codcomp", v.getId().getCodcomp()));
						cr.add(Restrictions.eq("id.numrec", v.getId().getNumrec()));
						cr.add(Restrictions.eq("id.mpago", MetodosPagoCtrl.EFECTIVO ));
						cr.add(Restrictions.eq("id.moneda", v.getId().getMonedacamb()));
						rd = (Recibodet)cr.uniqueResult();
						
						if(rd != null){
							//&& si el monto del pago en moneda distinta es menor que el  cambio, restarlo del cambio.
							if(rd.getMonto().compareTo(v.getId().getCambio()) == -1 ){
								v.getId().setCambio(v.getId().getCambio().subtract(rd.getMonto()));
							}
						}
						lstRecibos.add(v);
					}else{
						lstRecibos.add(v);
					}
				}
			} 
		}catch(Exception error){
			lstRecibos = null;
			error.printStackTrace();
//			System.out.println("Error en ArqueoCajaCtrl.obtenerRecibosxcambiomixto "+error);
		}		
		return lstRecibos;
	}
	
/** Sin usar *************** 2. obtener el total diario por cambios realizados en la caja ********************/
	public String obtenerTotalxCambios(int iCaid, String sCodcomp, String sCodsuc, String sTipoMoneda){
		String total = "";
		Session sesion = HibernateUtilPruebaCn.currentSession();
		
		String consulta = "";
		
		try{
			consulta =  " select sum(v.id.cambio) ";
			consulta += " from Vdetallecambiorecibo as v where v.id.caid = '"+iCaid+"'" +
					    " and v.id.codcomp = '"+sCodcomp+"' and v.id.codsuc = '"+sCodsuc+"'" +
					    " and v.id.moneda = '"+sTipoMoneda+"' and v.id.fecha = current_date";
			consulta += " and v.id.tiporec not in <> ('CO', 'FCV')";
			
			
			Object result = sesion.createQuery(consulta).uniqueResult();
			
			if(result != null){
				total = result.toString();
			}else
				total = "0";
					
			
		}catch(Exception error){
			error.printStackTrace();
		}		
		return total;
	}
/***************** 3. obtener los recibos generados por devoluciones realizadas. ********************/
	public List cargarRecibosxDevoluciones(int iCaid,String sCodsuc,String sCodcomp,String sTipoMoneda){
		List recibos = new ArrayList();
		Session sesion = HibernateUtilPruebaCn.currentSession();
		
		String consulta = "";
		
		try{			
			consulta = " from Vrecibosxdevoluciones as v where v.id.caid='"+iCaid+"' and v.id.codsuc = '"+sCodsuc+"'" +
					   " and v.id.codcomp = '"+sCodcomp+"' and v.id.moneda='"+sTipoMoneda;
			consulta+= " and v.id.tiporec <> 'FCV' and v.id.fecha = current_date order by v.id.numrec";
			
	
			recibos = sesion.createQuery(consulta).list();
			
			if(recibos!=null && recibos.size()>0){
				for(int i=0;i<recibos.size();i++){
					Vrecibosxdevoluciones v = (Vrecibosxdevoluciones)recibos.get(i);
					v.setMonto(v.getId().getMonto());
					recibos.remove(i);
					recibos.add(i,v);					
				}
			}	
			
		}catch(Exception error){
			error.printStackTrace();
		}
		return recibos;		
	}
/***************** 4. obtener el total diario por devoluciones realizadas. ********************/
	public String obtenerTotalxDevoluciones(int iCaid,String sCodcomp, String sCodsuc,String sTipoMoneda){
		String total = "";
		Session sesion =HibernateUtilPruebaCn.currentSession();
		
		String consulta = "";
		
		try{
			if(sTipoMoneda.equals("COR")){
				consulta = "select sum(v.id.cor)";
			}
			if(sTipoMoneda.equals("USD")){
				consulta = "select sum(v.id.usd)";
			}
			consulta += " from Vtotaldiarioxdevolucion as v where v.id.caid = '"+iCaid+"'" +
						" and v.id.codcomp = '"+sCodcomp+"' and v.id.codsuc = '"+sCodsuc+"'" ;	
			
		
			Object resul = sesion.createQuery(consulta).uniqueResult();
		
			
			if(resul ==null)
				total = "0";
			else
				total = resul.toString();
					
		}catch(Exception error){
			error.printStackTrace();
		}	
		return total;
	}
/****************** 5. obtener los recibos generados por egresos realizados ********************/
	public List cargarRecibosxTipoEgreso(int iCaid, String sCodsuc,String sCodcomp, String sMoneda,String sMpago){
		List recibos = new ArrayList();
		Session sesion = HibernateUtilPruebaCn.currentSession();
		
		String consulta = "";
		
		try{
			consulta  =  " from Vrecibosxtipoegreso as v where v.id.caid = '"+iCaid+"' and v.id.codsuc ='"+sCodsuc+"'" +
					     " and v.id.codcomp='"+sCodcomp+"' and v.id.moneda = '"+sMoneda+"' and v.id.mpago = '"+sMpago+"'";
			consulta +=  " and v.id.tiporec <> 'FCV' order by v.id.numrec";
			
		
			recibos = sesion.createQuery(consulta).list();
		
			
			if(recibos!=null && recibos.size()>0){
				for(int i=0; i<recibos.size();i++){
					Vrecibosxtipoegreso v = (Vrecibosxtipoegreso)recibos.get(i);
					v.setMonto(v.getId().getMonto());
					recibos.remove(i);
					recibos.add(i,v);
				}
			}					
		}catch(Exception error){
			error.printStackTrace();
		}
		return recibos;
	}
/****************** 5.1  obtener los recibos generados por egresos realizados  por tipo y mpago ********************/
	public List cargarRecibosxTipoEgreso(int iCaid, String sCodsuc,String sCodcomp, String sMoneda,String sMpago,String sTiporec){
		List recibos = new ArrayList();
		Session sesion = HibernateUtilPruebaCn.currentSession();
		
		String consulta = "";
		
		try{
			consulta  =  " from Vrecibosxtipoegreso as v where v.id.caid = '"+iCaid+"' and v.id.codsuc ='"+sCodsuc+"'" +
					     " and v.id.codcomp='"+sCodcomp+"' and v.id.moneda = '"+sMoneda+"' and v.id.mpago = '"+sMpago+"'" +
					     " and v.id.tiporec = '"+sTiporec+"' and v.id.fecha = current_date";
			consulta +=  " order by v.id.numrec";
			
			recibos = sesion.createQuery(consulta).list();
			
			
		}catch(Exception error){
			error.printStackTrace();
		}
		return recibos;
	}
	public List cargarRecibosxTipoEgreso(int iCaid, String sCodsuc,String sCodcomp, String sMoneda,String sMpago,String sTiporec,String sDev){
		List recibos = new ArrayList();
		Session sesion = HibernateUtilPruebaCn.currentSession();
		String consulta = "";
		
		try{
			consulta  =  " from Vrecibosxtipoegreso as v where v.id.caid = "+iCaid+" and v.id.codsuc ='"+sCodsuc+"'" +
					     " and v.id.codcomp='"+sCodcomp+"' and v.id.moneda = '"+sMoneda+"' and v.id.mpago = '"+sMpago+"'" +
					     " and v.id.tiporec = '"+sTiporec+"' and v.id.fecha = current_date";
			consulta +=  " order by v.id.numrec";					
					  // " and v.id.tiporec = '"+sTiporec+"'and v.id.tiporec <> '"+sDev+"'and v.id.fecha = current_date";

			
			recibos = sesion.createQuery(consulta).list();
								
		}catch(Exception error){
			error.printStackTrace();
		}
		return recibos;
	}	
/****************** 5.2  obtener los recibos generados por egresos  por tipo y mpago ********************/
	//public List cargarRecibosxMetodoPago(int iCaid, String sCodsuc,String sCodcomp, String sMoneda,Date dtFecha,Date dtHini){
	  public List cargarRecibosxMetodoPago(int iCaid, String sCodsuc,String sCodcomp, String sMoneda,
											Date dtFechaArqueo,Date dtHoraInicio, Date dtHoraFin,Session sesion){		
		List<Vrecibosxtipompago> recibos = new ArrayList<Vrecibosxtipompago>();
		String sNotIN[] = new String[]{"DCO","FCV"}; 

		
		try{		
			Criteria cr = sesion.createCriteria(Vrecibosxtipompago.class);
			cr.add(Restrictions.eq("id.caid"	,iCaid));
			cr.add(Restrictions.eq("id.codsuc"	,sCodsuc));
			cr.add(Restrictions.eq("id.codcomp"	,sCodcomp ));
			cr.add(Restrictions.eq("id.moneda"	,sMoneda ));
			cr.add(Restrictions.eq("id.fecha"	,dtFechaArqueo));
			cr.add(Restrictions.ne("id.estado"	,"A"));
			cr.add(Restrictions.between("id.hora", dtHoraInicio,dtHoraFin));
			cr.add(Restrictions.not(Restrictions.in("id.tiporec", sNotIN)));
			cr.addOrder(Order.asc("id.numrec"));
			recibos = (ArrayList<Vrecibosxtipompago>)cr.list();
			
			
		}catch(Exception error){
			recibos = null;
			error.printStackTrace();
		}	
		return recibos;
	}
	
	
/************* 6. obtener el total diario por pagos con tarjetas de credito *******************/
	public String obtenerTotalxTipoEgreso(int iCaid, String sCodsuc,String sCodcomp,String sMoneda,String mPago){
		String total = "";
		Session sesion = HibernateUtilPruebaCn.currentSession();
		
		String consulta = "";
		
		try{
			if(sMoneda.equals("COR")){
				consulta = "select sum(v.id.cor) ";
			}
			if(sMoneda.equals("USD")){
				consulta = "select sum(v.id.usd) ";
			}
			consulta += " from Vtotalxtipoegreso as v where v.id.caid='"+iCaid+"' and v.id.codsuc = '"+sCodsuc+"'" +
						" and v.id.codcomp = '"+sCodcomp+"' and v.id.mpago = '"+mPago+"'";
			consulta += " and v.id.tiporec <> 'FCV' and v.id.fecha = current_date";
			
		
			Object resul = sesion.createQuery(consulta).uniqueResult();
					
			if(resul == null)
				total = "0";				
			else
				total = resul.toString();			
			
		}catch(Exception error){
			error.printStackTrace();
		}	
		return total;
	}
/****************** 7. cargar los registros de todas las facturas del dia ********************************/
	public List cargarFacturasDia(int iFechaActual,String[] sTipoDoc, F55ca017[] f55ca017,List lstLocalizaciones,
								  String sCodsuc,String sCodcomp,String sMoneda){	
		List lstFacturas = new ArrayList();
		Session sesion = HibernateUtilPruebaCn.currentSession();
		
		String sLoc = "";
		
		try{
			
			String sql = "from Hfactjdecon as f where "; //f.id.codsuc = '"+sCodsuc+"' and
			sql += " f.id.codcomp = '"+sCodcomp+"' and f.id.moneda ='"+sMoneda+"' and ";
			sql += " f.id.fecha =" + iFechaActual +" and f.id.subtotal > 0 and trim(f.id.estado)<>'A' and f.id.tipofactura in (";
			
			//agregar tipos de documentos
			for (int i = 0; i < sTipoDoc.length; i++){
				if (i == sTipoDoc.length - 1){
					sql = sql + "'" + sTipoDoc[i] + "'";
				}else{
					sql = sql + "'" + sTipoDoc[i] + "',";
				}
			}
			sql = sql + ")";
			//agregar unidades de negocio
			sql = sql + " and trim(f.id.codunineg) in (";
			for(int a = 0; a < f55ca017.length; a++){
				if (a == f55ca017.length - 1){
					sql = sql + "'" + f55ca017[a].getId().getC7mcul().trim() + "'";
				}else{
					sql = sql + "'" + f55ca017[a].getId().getC7mcul().trim() + "',";
				}
			}
			sql = sql + ")";
			//
			//agregar localizaciones si hay 
			if(lstLocalizaciones != null && !lstLocalizaciones.isEmpty()){
				sql = sql + " and trim(f.id.sdlocn) in (";
				for(int j = 0; j < lstLocalizaciones.size(); j++){
					sLoc = (String)lstLocalizaciones.get(j);
					if (j == lstLocalizaciones.size() - 1){
						sql = sql + "''";
					}else{
						sql = sql + "'" + sLoc.trim() + "',";
					}
				}
				//sql = sql + ") and f.id.nodoco ='0' and f.id.tipodoco ='' order by f.id.nofactura ";
				sql = sql + ") order by f.id.nofactura ";
			}else{
				//sql = sql + " and and f.id.nodoco ='0' and f.id.tipodoco ='' order by f.id.nofactura ";
				sql = sql + " order by f.id.nofactura ";
			}			
			List lstfacjdcon = sesion.createQuery(sql).list();
			
			
			if(lstfacjdcon!=null && lstfacjdcon.size()>0){
				FacContadoDAO facDao = new FacContadoDAO();
				lstFacturas = facDao.formatFactura(lstfacjdcon);	
				
				for(int i=0; i<lstFacturas.size();i++){
					Hfactura hfac = (Hfactura)lstFacturas.get(i);
					hfac.setFecha(hfac.getFecha().substring(10));
					lstFacturas.remove(i);
					lstFacturas.add(i,hfac);
				}				
			}			
		}catch(Exception error){
			error.printStackTrace();
		}	
		return lstFacturas;		
	}	
/****************** 8. cargar los registros de todas las facturas del dia desde hfactura ********************************/
	public List obtenerFacturasDeldia(int iCaid,String[] sTiposDoc ,F55ca017[] f55ca017,List lstLocalizaciones,
									  String sMoneda, String sCodsuc,String sCodcomp,Date dtHoraInicio){
		List lstFacturas = new ArrayList();
		Session session = HibernateUtilPruebaCn.currentSession();
		
		String sLoc = "";
		FacturaCrtl faCtrl = new FacturaCrtl();
		try{
			
			//obtener fecha actual y convertirla a formato juliano		
			int iFechaActual = faCtrl.obtenerFechaActualJuliana();

			String sql = "from Vhfactura as f where f.id.estado <> 'A' and ";
			sql +=	" f.id.codcomp = '"+sCodcomp+"' and ";// f.id.codsuc = '"+sCodsuc+"' and";
			sql +=  " f.id.fecha =" + iFechaActual +" and trim(f.id.moneda) = '"+sMoneda+"' and f.id.tipofactura in (";
			
			for (int i = 0; i < sTiposDoc.length; i++){
				if (i == sTiposDoc.length - 1){
					sql = sql + "'" + sTiposDoc[i] + "'";
				}else{
					sql = sql + "'" + sTiposDoc[i] + "',";
				}
			}
			sql = sql + ")";
			
			//agregar unidades de negocio
			sql = sql + " and trim(f.id.codunineg) in (";
			for(int a = 0; a < f55ca017.length; a++){
				if (a == f55ca017.length - 1){
					sql = sql + "'" + f55ca017[a].getId().getC7mcul().trim() + "'";
				}else{
					sql = sql + "'" + f55ca017[a].getId().getC7mcul().trim() + "',";
				}
			}
			sql = sql + ")";
			//
			//agregar localizaciones si hay 
			if(lstLocalizaciones != null && !lstLocalizaciones.isEmpty()){
				sql = sql + " and trim(f.id.sdlocn) in (";
				for(int j = 0; j < lstLocalizaciones.size(); j++){
					sLoc = (String)lstLocalizaciones.get(j);
					if (j == lstLocalizaciones.size() - 1){
						sql = sql + "'" + sLoc.trim() + "'";
					}else{
						sql = sql + "'" + sLoc.trim() + "',";
					}
				}
				sql = sql + ")";  // order by f.id.fecha desc, f.id.hora desc";
			}
			//--- Excluir las facturas que fueron enviadas hacia otra caja. 
			sql += " and f.nofactura not in( select tf.nofactura from "+PropertiesSystem.ESQUEMA+".trasladofac tf ";
			sql += " where tf.nofactura = f.nofactura and tf.codcomp = f.codcomp and tf.tipofactura = f.tipofactura";
			sql += " and tf.codsuc = f.codsuc and trim(tf.codunineg)  = trim(f.codunineg)";
			sql += " and tf.estadotr in ('E','R','P') and tf.caidorig = "+iCaid + " )";
			
			//--- Incluir las facturas enviadas desde otra caja.
			sql += " UNION ";
			sql += " select f.* from "+PropertiesSystem.ESQUEMA+".Hfactjdecon as f inner join "+PropertiesSystem.ESQUEMA+".trasladofac tf"; 
			sql += " on  tf.nofactura = f.nofactura and tf.codcomp = f.codcomp and tf.tipofactura = f.tipofactura";
			sql += " and tf.codsuc = f.codsuc and trim(tf.codunineg)  = trim(f.codunineg) and tf.estadotr = 'E'";
			sql += " and tf.caiddest = "+iCaid+" and f.fecha = "+ iFechaActual +" and trim(f.estado)<>'A' ";
			
			if(dtHoraInicio!=null){
				SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
				String sHoraI = dfHora.format(dtHoraInicio);
				String sHoraF = dfHora.format(new Date());			
				String sHini[],sHfin[];
				sHini = sHoraI.split(":");
				sHfin = sHoraF.split(":");
				int iHini = 0,iHfin = 0;
				
				iHini = Integer.parseInt(sHini[0]+""+sHini[1]+""+sHini[2]);
				iHfin = Integer.parseInt(sHfin[0]+""+sHfin[1]+""+sHfin[2]);
				
				sql += " and f.id.hora >= " + iHini ;
				sql += " and f.id.hora <= " +iHfin;
				
			}			
			sql = sql + " order by f.id.fecha desc, f.id.hora desc";
			
		
			lstFacturas = session.createQuery(sql).list();
					
		}catch(Exception ex){
			ex.printStackTrace();
		}		
		return lstFacturas;
	}
/************ 9. cargar el detalle de las facturas aplicadas al recibo ****************************/	
	public List leerFacturasRecibo(int iCaid,String sCodSuc,String sCodComp,int iNumrec, int iCodcli){
		
		List lstFacturasRecibo = new ArrayList(), lstRecibofac = null;
		FacturaxRecibo facturaRec = null;
		Hfacturajde hfac = null;
		Recibofac recFac = null;
		Session session = HibernateUtilPruebaCn.currentSession();
		
		
		String sql = "from Recibofac as r where r.id.caid = " + iCaid
				+ " and r.id.codsuc = '" + sCodSuc + "'"
				+ " and r.id.codcomp = '" + sCodComp.trim()
				+ "' and r.id.numrec = " + iNumrec
				+" and r.id.codcli = "+iCodcli ;
		try{
		
			lstRecibofac = session.createQuery(sql).list();
		
			for(int i = 0; i < lstRecibofac.size();i++){
				recFac = (Recibofac)lstRecibofac.get(i);
				hfac = getInfoFactura(recFac.getId().getNumfac(),sCodComp,recFac.getId().getTipofactura(), iCodcli);
				JulianToCalendar fecha = new JulianToCalendar(hfac.getId().getFecha());
				facturaRec = new FacturaxRecibo(hfac.getId().getNofactura(),hfac.getId().getTipofactura(),hfac.getId().getUnineg(),recFac.getMonto(),hfac.getId().getMoneda(),fecha.toString(),"");
				lstFacturasRecibo.add(facturaRec);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return lstFacturasRecibo;
	}
/********************** 10. obtener el detalle de Facturas de Crédito  *******************************************/
	public Hfacturajde getInfoFactura(int iNumfac,String sCodComp,String sTipoDocumento, int iCodcli){
		Hfacturajde factura = null;		
		Session session = HibernateUtilPruebaCn.currentSession();
		
		
		String sql = "from Hfacturajde as hf where hf.id.nofactura = "
				+ iNumfac + " and hf.id.codcomp = '" + sCodComp.trim()
				+ "' and hf.id.tipofactura = '" + sTipoDocumento + "'"
				+ " and hf.id.codcli = "+iCodcli;
		
		try{
		
			factura = (Hfacturajde)session.createQuery(sql).uniqueResult();
		
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return factura;
	}
/************** 11. obtener monto mínimo de efectivo para una caja por moneda  ***********************************/
	public String obtenerMontoMinimodeCaja(int iCaid, String sCodcomp,String sMoneda){
		String monto = "0",consulta = "";
 
		
		try{
		 
			consulta = "  select f.id.c3miam from F55ca013 as f where f.id.c3id = '"+iCaid+
					   "' and f.id.c3rp01 = '"+sCodcomp+"' and f.id.c3crcd = '"+sMoneda+"'";
			
			Object obMonto = ConsolidadoDepositosBcoCtrl.executeSqlQueryUnique(consulta, null, false);
			
			if( obMonto == null ){
				return monto = "0";
			}
				
			return String.valueOf(obMonto);
			
		}catch(Exception error){
			 monto = "0"; 
			error.printStackTrace();
		} 
		return monto;
	}
	
/**************  12. Guardar los datos que conforman el arqueo de caja  ***********************************/
	@SuppressWarnings("unchecked")
	public boolean guardarArqueoCaja(int noarqueo, int codcajero,int caid, int iEstadoId,String moneda,String coduser,String sReferdep,
								String codcomp, String codsuc,Date fecha, Date fechamod,Date hora, Date horamod,
								BigDecimal tingreso,BigDecimal tegresos,BigDecimal netorec,BigDecimal minimo,
								BigDecimal dsugerido,BigDecimal efectcaja,BigDecimal sf,BigDecimal dfinal,BigDecimal tpagos,
								Session sesCaja,Transaction transCaja){
		
		Arqueo arqueo = new Arqueo();
		ArqueoId id = new ArqueoId();
		boolean bHecho = true;
				
		try{
			
			boolean preconciliacion =  BancoCtrl.ingresoBajoPreconciliacion(0, caid, codcomp) ;
			
			arqueo.setCodcajero(codcajero);
			arqueo.setCoduser(coduser);
			arqueo.setDfinal(dfinal);
			arqueo.setDsugerido(dsugerido);
			arqueo.setEfectcaja(efectcaja);			
			arqueo.setFechamod(fechamod);
			arqueo.setHoramod(horamod);
			arqueo.setMinimo(minimo);
			arqueo.setMoneda(moneda);
			arqueo.setNetorec(netorec);
			arqueo.setSf(sf);
			arqueo.setTegresos(tegresos);
			arqueo.setTingreso(tingreso);
			arqueo.setTpagos(tpagos);
			arqueo.setReferdep(sReferdep);
			arqueo.setMotivo("");
			arqueo.setCodusermod(codcajero);
			arqueo.setDatapcinfo(PropertiesSystem.getDataFromPcClient());
			
			arqueo.setDepctatran( preconciliacion ? 1 : 0);
			arqueo.setReferencenumber(Integer.parseInt(sReferdep));
			
			id.setCaid(caid);
			id.setCodcomp(codcomp);
			id.setCodsuc(codsuc);
			id.setFecha(fecha);
			id.setHora(hora);
			
			String sEstado = obtenerEstadoArqueo(iEstadoId);
			arqueo.setEstado(sEstado);
			id.setNoarqueo(noarqueo);
			arqueo.setId(id);	
			
			LogCajaService.CreateLog("guardarArqueoCaja", "HQRY", LogCajaService.toJson(arqueo));
			
			sesCaja.save(arqueo);
			
			CodeUtil.putInSessionMap("ac_ArqueoActual", arqueo) ;
			
		}catch(Exception error){
			bHecho = false;
			LogCajaService.CreateLog("guardarArqueoCaja", "ERR", error.getMessage());
		}
		return bHecho;
	}	
	
/************* 13. Obtener un estado especifico para un arqueo de caja *************************/
	public String obtenerEstadoArqueo(int ValorCatalogoId){
		String sEstado = "P",consulta = "";
		Session sesion = HibernateUtilPruebaCn.currentSession();
		
		
		
		try{
			
			consulta = "from Valorcatalogo as v where v.codvalorcatalogo = "+ ValorCatalogoId;
			
			Valorcatalogo v = (Valorcatalogo)sesion.createQuery(consulta).uniqueResult();
			
			
			if(v!=null)
				sEstado = v.getCodigointerno();
			
		}catch(Exception error){
			error.printStackTrace();
		}		
		return sEstado;
	}	
	
/*********** 14. Enviar un correo electrónico al supervisor y contador de la caja ***********************************/
	public boolean  enviarCorreo(String sTo, String sFrom,String sNombreFrom, String sCc, String sSubject,int sCodCajero, String sCajero,int iCaid,String sCaja,
		 	String sCodcomp,String sUbicacion,int noArqueo,BigDecimal dfinal, String sMoneda,String sUrl,Date dFecha,boolean ajusteMinimo, double MontoReint){
		// MultiPartEmail email = new MultiPartEmail();		
		String sHora = "";
		String sFecha = "";
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
		boolean enviado = true;
		String sDfinal = "";
		Divisas dv = new Divisas();
		
		try {
			sFecha = sdf.format(dFecha);
			sHora = dfHora.format(dFecha);
			sDfinal = dv.formatDouble(dfinal.doubleValue());
			
			String shtml = "<table width=\"400px\" style=\"border: 1px #7a7a7a solid\" align=\"center\" cellspacing=\"0\" cellpadding=\"3\">" +
							"<tr>"+
							"<th colspan=\"2\" style=\"border-bottom: 1px #7a7a7a solid; background: #3e68a4\">" +
								"<font face=\"Arial\" size=\"2\" color=\"white\"><b>Notificación de arqueo de caja.</b></font>" +
							"</th>" +
							"</tr>"+
							"<tr>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"25%\"><b>Arqueo No.: &nbsp;</b></td>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"75%\">"+ noArqueo +" </td>" +
							"</tr>"+
							"<tr>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"25%\"><b>Cajero: &nbsp;</b></td>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"75%\">"+ sCodCajero +" / "+sCajero+"</td>" +
							"</tr>"+
							"<tr>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"25%\"><b>Caja: &nbsp;</b></td>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"75%\">"+ iCaid +" / "+sCaja+"</td>" +
							"</tr>"+
							"<tr>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"25%\"><b>Ubicación: &nbsp;</b></td>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"75%\">"+ sUbicacion +"</td>" +
							"</tr>"+
							"<tr>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"25%\"><b>Compañía: &nbsp;</b></td>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"75%\">"+sCodcomp+"</td>" +
							"</tr>" +
							"<tr>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"25%\"><b>Depósito: &nbsp;</b></td>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"75%\">"+sDfinal+ " "+ sMoneda +"</td>" +
							"</tr>"+
							"<tr>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"25%\"><b>Fecha: &nbsp;</b></td>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"75%\">"+ sFecha + " / " + sHora +"</td>" +
							"</tr>" +
							"<tr>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"25%\"><b>Enlace: &nbsp;</b></td>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"70%\">"+ sUrl +"</td>" +
							"</tr>"+						
							"<tr>" +
								"<td align=\"center\" colspan=\"2\" style=\"font-family: Arial, Helvetica, sans-serif;font-size: 10px;color: black; border-bottom: 1px ##1a1a1a solid; \">" +
									"<b>Casa Pellas, S. A. - Módulo de Caja</b>" +
								"</td>" +
							"</tr>" +
						"</table>";
			
					if(ajusteMinimo){
						shtml += "<table>" +
								"<tr>" +	
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-weight: bold;font-size: 13px;color: #1a1a1a;\" >" +
								"En este Arqueo se realizo una afectacion al monto minimo de caja por :"+ dv.formatDouble(MontoReint)  + 
								"</td>" +
								"</tr>" +
								"</table>";
					}
			String sNombreCajero = dv.ponerCadenaenMayuscula(sNombreFrom);
			sNombreCajero = (sNombreCajero.equals(""))?sNombreFrom:sNombreCajero;
			 
			 MailHelper.SendHtmlEmail(
						new CustomEmailAddress(sFrom,sNombreCajero),
						new CustomEmailAddress(sTo), 
						new CustomEmailAddress(sCc),  
						sSubject, shtml);
			 
		}catch(Exception ex){
			ex.printStackTrace();
			enviado = false;			
		}		
		return enviado;
	}	
/******** 15. Cambiar el estado del arqueo de pendiente a rechazad y actualizar motivo *********************/
	public boolean rechazarArqueo(int iNoarqueo,int iCaid,String sCodcomp, String sCodsuc,String sMotivo, Session s){
		String consulta = "";
		Arqueo arqueo;
		boolean hecho = false;
		
		try{
			consulta = " from Arqueo as a where a.id.noarqueo = "+iNoarqueo+" and a.id.caid = "+iCaid ;
			consulta += " and a.id.codcomp = '"+sCodcomp+"' and a.id.codsuc = '"+sCodsuc+"'";
			
		
			arqueo = (Arqueo)s.createQuery(consulta).uniqueResult();
			
			if(arqueo!=null){
				String sEstado = obtenerEstadoArqueo(2);
				arqueo.setEstado(sEstado);
				arqueo.setMotivo(sMotivo);
				s.update(arqueo);
				hecho = true;
			}
					
		}catch(Exception error){
			error.printStackTrace();
			return false;
		}		
		return hecho;		
	}
/******** 15. Cambiar el estado del arqueo de pendiente a rechazad y actualizar motivo *********************/
	public boolean rechazarArqueo(Session s,Transaction tx, int iNoarqueo,int iCaid,String sCodcomp, String sCodsuc,String sMotivo){
		
		boolean hecho = true;
		
		try{
			String sEstado = obtenerEstadoArqueo(2);
			Arqueo arqueo = (Arqueo) s.createCriteria(Arqueo.class)
				.add(Restrictions.eq("id.noarqueo", iNoarqueo))
				.add(Restrictions.eq("id.caid", iCaid))
				.add(Restrictions.eq("id.codcomp", sCodcomp))
				.add(Restrictions.eq("id.codsuc", sCodsuc))
				.uniqueResult();
			
			arqueo.setEstado(sEstado);
			arqueo.setMotivo(sMotivo);
			
			LogCajaService.CreateLog("rechazarArqueo", "HQRY", LogCajaService.toJson(arqueo));
			
			s.update(arqueo);
			
		}catch(Exception error){
			LogCajaService.CreateLog("rechazarArqueo", "ERR", error.getMessage());
			error.printStackTrace(); 
			return false;
		}
		return hecho;		
	}
/**********************************************************************************************/
/** Método: Obtener solamente las devoluciones por traslados para la caja.
 *	Fecha:  22/10/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 **/
	@SuppressWarnings("unchecked")
	public List<Vfacturaxdevolucion> obtenerDevolucionesxTraslado(int iCaid, String sCodcomp,Date dtFechaArqueo,String sMoneda,
												Date dtHoraInicio, Date dtHoraFin,Session sesion ){
		List<Vfacturaxdevolucion>lstDevolucionxCaja = null;
		try {
			String sql = "";
			String sRangoFecha = "";
			
			//---- obtener fecha actual y convertirla a formato juliano		
			int iFechaActual = new FacturaCrtl().obtenerFechaActualJuliana(dtFechaArqueo);	
			String sHoraI = new FechasUtil().formatDatetoString(dtHoraInicio, "HH:mm:ss");
			String sHoraF = new FechasUtil().formatDatetoString(dtHoraFin, "HH:mm:ss");			
			String sHini[],sHfin[];
			sHini = sHoraI.split(":");
			sHfin = sHoraF.split(":");
			int iHini = 0,iHfin = 0;
			iHini = Integer.parseInt(sHini[0]+""+sHini[1]+""+sHini[2]);
			iHfin = Integer.parseInt(sHfin[0]+""+sHfin[1]+""+sHfin[2]);
			sRangoFecha += " and f.hora >= " + iHini ;
			sRangoFecha += " and f.hora <= " +iHfin;
			
			sql += " select f.* " ; 
			sql += " from "+PropertiesSystem.ESQUEMA+".Vfacturaxdevolucion f inner  " ;
			sql += " join "+PropertiesSystem.ESQUEMA+".trasladofac tf on  tf.nofactura = f.nodev ";
			sql += " and tf.codcomp = f.codcomp and tf.tipofactura = f.tipodev ";
			sql += " and tf.codsuc = f.codsuc and trim(tf.codunineg) = trim(f.codunineg) ";
			sql += " and tf.estadotr in ('E','P') and tf.caiddest = "+iCaid+" and f.fechadev = " +iFechaActual;
			sql += " and trim(f.estado)<>'A'  and f.codcomp = '" +sCodcomp+ "' and f.moneda = '" +sMoneda+ "'";
			sql += sRangoFecha;
			
			lstDevolucionxCaja = sesion.createSQLQuery(sql).addEntity(Vfacturaxdevolucion.class).list();
			
			
		} catch (Exception e) {
			lstDevolucionxCaja = null;
			e.printStackTrace();
		}
		return lstDevolucionxCaja;
	}
/**********************************************************************************************/
/** Método: Obtiene las devoluciones del día que fueron procesadas, omitiendo emisión de cheque
 *	Fecha:  19/05/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 **/
	public List obtenerDevolucionDeldia(String[] sTiposDoc ,F55ca017[] f55ca017,List lstLocalizaciones,
										String sMoneda,String sCodsuc,String sCodcomp,int iCaid,
										Date dtFechaArqueo,Date dtHoraInicio,Date dtHoraFin,Session sesion){
		boolean bSoloLocs=false,bHayLoc=false;
		int iFechaActual = 0;
		String sql1="", sql2="";
		List lstDev = null,f55ca017_1 = new ArrayList(), f55ca017_2 = new ArrayList();
		FacturaCrtl faCtrl = new FacturaCrtl();
		String sRangoFecha = "";
		SimpleDateFormat dfHora =  null;
		
		try {
			//---- obtener fecha actual y convertirla a formato juliano		
			iFechaActual = faCtrl.obtenerFechaActualJuliana(dtFechaArqueo);	
			//---- Filtrar en un rango de fechas.			
			dfHora = new SimpleDateFormat("HH:mm:ss");
			String sHoraI = dfHora.format(dtHoraInicio);
			String sHoraF = dfHora.format(dtHoraFin);			
			String sHini[],sHfin[];
			sHini = sHoraI.split(":");
			sHfin = sHoraF.split(":");
			int iHini = 0,iHfin = 0;
			iHini = Integer.parseInt(sHini[0]+""+sHini[1]+""+sHini[2]);
			iHfin = Integer.parseInt(sHfin[0]+""+sHfin[1]+""+sHfin[2]);
			sRangoFecha += " and f.hora >= " + iHini ;
			sRangoFecha += " and f.hora <= " +iHfin;
				
			//---- Separar en lista las que traen localizaciones.
			for(byte i = 0; i < f55ca017.length; i++){
				if(f55ca017[i].getId().getC7locn().trim().equals("")){
					f55ca017_1.add(f55ca017[i]);
					bSoloLocs = true;
				}else{
					bHayLoc = true;
					f55ca017_2.add(f55ca017[i]);
				}
			}	
			
			sql1 =  " select * from "+PropertiesSystem.ESQUEMA+".Vfacturaxdevolucion as f where f.estado <> 'A' and  ";
			sql1 +=	" f.codcomp = '"+sCodcomp+"' and f.fechadev =" + (iFechaActual) ;
			sql1 += " and trim(f.moneda) = '"+sMoneda+"'"; 
			sql1 += sRangoFecha;
			
			//---- Localizaciones.
			sql1+= " and f.tipodev in (";			
			for (int i = 0; i < sTiposDoc.length; i++)
				sql1+=  (i==sTiposDoc.length - 1)?"'" + sTiposDoc[i] + "')": "'" + sTiposDoc[i] + "',";
			
			sql2 = sql1; //---Crear primera parte de la consulta 2 de unidades de negocio.
						
			//---- Unidades de negocio.
			sql1 += " and trim(f.sdlocn) = '' and trim(f.codunineg) in (";
			for(byte  i = 0; i < f55ca017_1.size(); i++)
				sql1 += (i==f55ca017_1.size() - 1)? 
							"'" + ((F55ca017)f55ca017_1.get(i)) .getId().getC7mcul().trim() + "')":
							"'" + ((F55ca017)f55ca017_1.get(i)) .getId().getC7mcul().trim() + "',";
			
			//---- Agregar Localizaciones a la cosulta 2.
			if(bHayLoc){
				String sLocals=" and trim(f.sdlocn) in (";
				String sUnineg=" and trim(f.codunineg) in (";
				for(byte i = 0; i< f55ca017_2.size(); i++){
					sLocals += "'" + ((F55ca017)f55ca017_2.get(i)).getId().getC7locn().trim();
					sUnineg += "'" + ((F55ca017)f55ca017_2.get(i)).getId().getC7mcul().trim();
					if(i==f55ca017_2.size() - 1){
						sLocals +=  "')";
						sUnineg +=  "')";
					}else{
						sLocals += "',";
						sUnineg += "',";
					}
				}
				sql2+=sLocals;
				sql2+=sUnineg;
			}
			//--- Excluir las facturas que fueron enviadas hacia otra caja. 
			String sExclTf = " and f.nodev not in( select tf.nofactura from "+PropertiesSystem.ESQUEMA+".trasladofac tf";
			sExclTf += " where tf.nofactura = f.nodev and tf.codcomp = f.codcomp and tf.tipofactura = f.tipodev";
			sExclTf += " and tf.codsuc = f.codsuc and trim(tf.codunineg)  = trim(f.codunineg)";
			sExclTf += " and tf.estadotr in ('E','R','P') and tf.caidprop = "+iCaid ;
			sExclTf += " and f.codcomp = '"+sCodcomp+"' and f.moneda = '"+ sMoneda + "' )";
			
			sql1 +=  sExclTf;
			sql2 +=  sExclTf;
			
			String sqlTf = "  UNION ";
			sqlTf += " select f.* " ; 
			sqlTf += " from "+PropertiesSystem.ESQUEMA+".Vfacturaxdevolucion f inner  " ;
			sqlTf += " join "+PropertiesSystem.ESQUEMA+".trasladofac tf on  tf.nofactura = f.nodev ";
			sqlTf += " and tf.codcomp = f.codcomp and tf.tipofactura = f.tipodev ";
			sqlTf += " and tf.codsuc = f.codsuc and trim(tf.codunineg) = trim(f.codunineg) ";
			sqlTf += " and tf.estadotr in ('E','P') and tf.caiddest = "+iCaid+" and f.fechadev = " +iFechaActual;
			sqlTf += " and trim(f.estado)<>'A'  and f.codcomp = '" +sCodcomp+ "' and f.moneda = '" +sMoneda+ "'";
			sqlTf += sRangoFecha;
			
			sql1 +=  sqlTf;
			sql2 +=  sqlTf;
			
			if(bHayLoc)
				sql1 += " union " + sql2;
			
			//--- Validar y ejecutar la consulta a utilizar.
			if(bSoloLocs)
				lstDev = sesion.createSQLQuery(sql1).addEntity(Vfacturaxdevolucion.class).list();	
			else
				lstDev = sesion.createSQLQuery(sql2).addEntity(Vfacturaxdevolucion.class).list();
			
		} catch (Exception error) {
			lstDev = null;
			error.printStackTrace();
		} 
		return lstDev;
	}	
/****************** 16. Obtener todas las facturas del dia por devoluciones ********************************/
	public List obtenerDevolucionesDeldia(String[] sTiposDoc ,F55ca017[] f55ca017,List lstLocalizaciones,
										  String sMoneda,String sCodsuc,String sCodcomp,Date dtHoraInicio){
		List lstFacturas = new ArrayList();
		Session session = HibernateUtilPruebaCn.currentSession();
		
		String sLoc = "";
		FacturaCrtl faCtrl = new FacturaCrtl();
		try{
			//obtener fecha actual y convertirla a formato juliano		
			int iFechaActual = faCtrl.obtenerFechaActualJuliana();
			
			String sql = "from Vfacturaxdevolucion as f where f.id.estado <> 'A' and ";
			sql +=	" f.id.codcomp = '"+sCodcomp+"' and ";// f.id.codsuc = '"+sCodsuc+"' and";
			sql +=  " f.id.fechadev =" + (iFechaActual) +" and f.id.subtotal > 0 and trim(f.id.moneda) = '"+sMoneda+"' and f.id.tipodev in (";
			
			for (int i = 0; i < sTiposDoc.length; i++){
				if (i == sTiposDoc.length - 1){
					sql = sql + "'" + sTiposDoc[i] + "'";
				}else{
					sql = sql + "'" + sTiposDoc[i] + "',";
				}
			}
			sql = sql + ")";
			
			//agregar unidades de negocio
			sql = sql + " and trim(f.id.codunineg) in (";
			for(int a = 0; a < f55ca017.length; a++){
				if (a == f55ca017.length - 1){
					sql = sql + "'" + f55ca017[a].getId().getC7mcul().trim() + "'";
				}else{
					sql = sql + "'" + f55ca017[a].getId().getC7mcul().trim() + "',";
				}
			}
			sql = sql + ")";
			//
			//agregar localizaciones si hay 
			if(lstLocalizaciones != null && !lstLocalizaciones.isEmpty()){
				sql = sql + " and trim(f.id.sdlocn) in (";
				for(int j = 0; j < lstLocalizaciones.size(); j++){
					sLoc = (String)lstLocalizaciones.get(j);
					if (j == lstLocalizaciones.size() - 1){
						sql = sql + "'" + sLoc.trim() + "',''";
					}else{
						sql = sql + "'" + sLoc.trim() + "',";
					}
				}
				sql +=")"; // sql + ") order by f.id.fechadev desc, f.id.hora desc";
			}
						
			//----- Filtrar por rango de horas, (caso de arqueo parcial existente)
			if(dtHoraInicio!=null){				
				int iHini = 0,iHfin = 0;
				FechasUtil f = new FechasUtil();
				iHini = f.formatHoratoInt(dtHoraInicio);
				iHfin = f.formatHoratoInt(new Date());
				
				sql += " and f.id.hora >= " + iHini ;
				sql += " and f.id.hora <= " + iHfin;				
			}
			
			sql += " order by f.id.fechadev desc, f.id.hora desc";
			
		
			lstFacturas = session.createQuery(sql).list();
					
		}catch(Exception ex){
			ex.printStackTrace();
		}		
		return lstFacturas;
	}
/******** Formatear una lista de Vfacturaxdevolucion y mostrarla como Hfactura ************************/
	public ArrayList<Hfactura> formatDevolucion(List lstDevoluciones){
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		Divisas divisas = new Divisas();
		ArrayList<Hfactura> lstResultado = new ArrayList<Hfactura>();		
		Vfacturaxdevolucion[] vhfacxdev = new Vfacturaxdevolucion[lstDevoluciones.size()];
		Hfactura hFac = null;
		double total = 0;
		double ivaTotal = 0;
		double totalfac = 0;
		double equiv = 0;
		String sHora = new String();
		double totalf = 0.0;
		try{
			for (int i = 0; i < lstDevoluciones.size(); i++){
				vhfacxdev[i] = (Vfacturaxdevolucion)lstDevoluciones.get(i);
				JulianToCalendar fechagrab = new JulianToCalendar(vhfacxdev[i].getId().getFechagrab());
				double subtotal = (vhfacxdev[i].getId().getSubtotal().doubleValue())/100.0;

				total = vhfacxdev[i].getId().getTotal()/100.0;		
				totalf = vhfacxdev[i].getId().getTotalf()/100.0;	
				ivaTotal = 0;
				double tasa = 0;
				
				
				String moneda = vhfacxdev[i].getId().getMoneda();
				if (moneda.equals("COR"))
					equiv = total;
				else
					equiv = total*tasa;
				
				totalfac += total;
				m.put("mTotalFactura",totalfac);
				
				//formatear Hora
				sHora = vhfacxdev[i].getId().getHora().toString();
				int iLargoHora = sHora.length();
				if (iLargoHora == 6){
					sHora = sHora.substring(0, 2) + ":" + sHora.substring(2, 4) + ":" + sHora.substring(4, 6);
				}else{
					sHora = "0"+sHora.substring(0, 1) + ":" + sHora.substring(1, 3) + ":" + sHora.substring(3, 5);
				}

				// establecer tipo de factura
				String sPago = "";
				if(vhfacxdev[i].getId().getTipopago().trim().equals("00") || vhfacxdev[i].getId().getTipopago().trim().equals("01") || vhfacxdev[i].getId().getTipopago().trim().equals("001"))
					sPago = "Contado";
				else
					sPago = "Crédito";
				
				hFac = new Hfactura(						
						vhfacxdev[i].getId().getNodev(),
						vhfacxdev[i].getId().getTipodev(),
						vhfacxdev[i].getId().getCodcli(),
						vhfacxdev[i].getId().getNomcli(),
						vhfacxdev[i].getId().getCodunineg(),
						vhfacxdev[i].getId().getUnineg(),
						vhfacxdev[i].getId().getCodsuc(),
						vhfacxdev[i].getId().getNomsuc(),
						vhfacxdev[i].getId().getCodcomp(),
						vhfacxdev[i].getId().getNomcomp(),						
						sHora,
						subtotal,
						vhfacxdev[i].getId().getMoneda(),
						vhfacxdev[i].getId().getTasa(),
						vhfacxdev[i].getId().getTipopago(),
						0,
						0,
						fechagrab.toString(),
						vhfacxdev[i].getId().getHechopor(),
						vhfacxdev[i].getId().getPantalla(),
						ivaTotal,
						total,
						divisas.formatDouble(equiv),
						"",
						new ArrayList(),
						vhfacxdev[i].getId().getNodoco(),
						vhfacxdev[i].getId().getTipodoco(),
						vhfacxdev[i].getId().getEstado(),
						sPago,
						vhfacxdev[i].getId().getNodev(),
						vhfacxdev[i].getId().getNofact(),
						vhfacxdev[i].getId().getFechadev(),
						vhfacxdev[i].getId().getFechafact(),
						totalf,
						new BigDecimal(String.valueOf(vhfacxdev[i].getId().getTotal()/100.0)),
						vhfacxdev[i].getId().getFechadev()
						);	
				lstResultado.add(hFac);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return lstResultado;
	}
/******* OBTENER LOS RECIBOS QUE CORRESPONDEN A DEVOLUCIONES DE FACTURA DEL DIA *****/
	public List obtenerRecibosxdevolucion(boolean bEgresos,int iCaid,String sCodsuc,String sCodcomp,
										  String sTiporec,String sMoneda,Date dtFechaArqueo,
										  Date dtHoraInicio,Date dtHoraFin,Session sesion){
		List lstDev = new ArrayList();
		int iFechajul = 0;
		FechasUtil f = new FechasUtil();
		
		try{
//			sFecha = f.formatDatetoString(dtFechaArqueo, "yyyy-MM-dd");
//			sHini = f.formatDatetoString(dtHoraInicio,"HH.mm.ss");
//			sHfin = f.formatDatetoString(dtHoraFin, "HH.mm.ss");
			
			iFechajul = f.obtenerFechaJulianaDia(dtFechaArqueo);
			Criteria cr = sesion.createCriteria(Vreciboxdevolucion.class);
			cr.add(Restrictions.eq("id.caid"	,iCaid));
			cr.add(Restrictions.eq("id.codcomp"	,sCodcomp));
			cr.add(Restrictions.eq("id.codsuc"	,sCodsuc ));
			cr.add(Restrictions.eq("id.fecha"	,dtFechaArqueo ));
			cr.add(Restrictions.eq("id.fechadev",iFechajul));
			cr.add(Restrictions.eq("id.tiporec",sTiporec));
			cr.add(Restrictions.eq("id.moneda"	,sMoneda));
 			cr.add(Restrictions.ne("id.estado", "A"));
			cr.add(Restrictions.between("id.hora", dtHoraInicio, dtHoraFin));
			
			if(bEgresos)
				cr.add(Restrictions.eq("id.monfac", sMoneda));

			lstDev = cr.list(); 
			
	
		}catch(Exception error){
			lstDev = null;
			error.printStackTrace();
		}
		return lstDev;
	}

/**
 * Obtener el total por devoluciones del dia a partir de una lista de devoluciones.
 * @param lstDev lista de devoluciones
 * @param dtFecha fecha a consultar
 * @return	total por devoluciones en la lista.
 */
	public double obtenerTotalxdevdia(List lstDev,Date dtFecha){
		double dTotal = 0;
		Session sesion = HibernateUtilPruebaCn.currentSession();
		
		String sConsulta = "",sFecha = "",sLstNumDev = "";		
		int iFechajul;
		
		
		try{
			FechasUtil fech = new FechasUtil();
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			sFecha = format.format(dtFecha);			
			iFechajul = fech.StringToJulian(sFecha);
			
			sLstNumDev = "(";
			for(int i=0; i<lstDev.size(); i++){
				Hfactura hFac = (Hfactura)lstDev.get(i);			
				sLstNumDev += (i == (lstDev.size()-1))? hFac.getNofactura() + ")":hFac.getNofactura() + ",";						
			}
			
			sConsulta =  " from Vfacturaxdevolucion as v where v.id.estado <> 'A' ";
			sConsulta += " and v.id.fechadev = '"+iFechajul+"' and v.id.fechafact = '"+iFechajul+"' ";
			sConsulta += " and v.id.nodev in " + sLstNumDev;
			
			
			lstDev = sesion.createQuery(sConsulta).list();
			
			
			if(lstDev != null && lstDev.size()>0){
				for(int i=0; i<lstDev.size(); i++){
					Vfacturaxdevolucion v = (Vfacturaxdevolucion)lstDev.get(i);
					dTotal += v.getId().getTotal()/100;
				}
			}
			
		}catch(Exception error){
			error.printStackTrace();
		}
		return dTotal;
	}
	
/************************************************************************************/
/** obtener todos los recibos q no se pagaron con efectivo
 ********/
	public List obtenerRecpagBanco(int iCaid, String sCodsuc,String sCodcomp, String sMoneda,
									   Date dtFechaArqueo,Date dtHoraInicio, Date dtHoraFin,Session sesion){
		List<Vrecibosxtipoegreso> lstRecibos = new ArrayList<Vrecibosxtipoegreso>();
		List<String>sNotIn = new ArrayList<String>(2);
		
		try{
			sNotIn.add(MetodosPagoCtrl.EFECTIVO );
			sNotIn.add(MetodosPagoCtrl.CHEQUE);
			Criteria cr = sesion.createCriteria(Vrecibosxtipoegreso.class);
			cr.add(Restrictions.eq("id.caid", iCaid)).add(Restrictions.eq("id.codcomp",sCodcomp));
			cr.add(Restrictions.eq("id.codsuc",sCodsuc)).add(Restrictions.eq("id.rfecha",dtFechaArqueo));
			cr.add(Restrictions.eq("id.rmoneda",sMoneda));
			cr.add(Restrictions.ne("id.estado",  "A"));
			cr.add(Restrictions.between("id.hora", dtHoraInicio, dtHoraFin));
			cr.add(Restrictions.not(Restrictions.in("id.mpago", sNotIn)));
			lstRecibos = (ArrayList<Vrecibosxtipoegreso>)cr.list();
			String sReciboin = "";
			
			if(lstRecibos!=null && lstRecibos.size()>0){
				List<Recibodet> lstRecibodet = new ArrayList<Recibodet>();
				
				String	sql1="";
				String sql = "select rd.* ";
				sql = sql.concat(" from "+PropertiesSystem.ESQUEMA+".recibo r inner join "+PropertiesSystem.ESQUEMA+".recibodet rd on ");
				sql = sql.concat(" r.caid = rd.caid and r.codcomp = rd.codcomp and ");
				sql = sql.concat(" r.codsuc = rd.codsuc and r.tiporec = rd.tiporec and r.numrec = rd.numrec");
				sql = sql.concat(" and r.caid = ").concat(String.valueOf(iCaid));
				sql = sql.concat(" and r.codcomp = '"+sCodcomp+"'");
				sql = sql.concat(" and r.codsuc = '"+sCodsuc+"'");
				sql = sql.concat(" and rd.moneda = '"+sMoneda+"'");
				sql = sql.concat(" and r.estado<>'A' and r.tiporec<>'FCV'");				
				
				for (int i = 0; i < lstRecibos.size(); i++) {
					Vrecibosxtipoegreso v = (Vrecibosxtipoegreso)lstRecibos.get(i);
					
					if(sReciboin.contains(v.getId().getNumrec()+"@,")) continue;
					sReciboin += v.getId().getNumrec()+"@,";
					
					sql1 = sql;
					sql1 =  sql1.concat(" and r.nodoco = ").concat(String.valueOf(v.getId().getNumrec()));
					sql1 =  sql1.concat(" and r.tipodoco = '").concat(v.getId().getTiporec()).concat("'");
					sql1 =  sql1.concat(" and rd.mpago = '"+v.getId().getMpago()+"'");
					
					lstRecibodet = (ArrayList<Recibodet>)
									sesion.createSQLQuery(sql1)
									.addEntity(Recibodet.class).list();

					if(lstRecibodet!=null && lstRecibodet.size()>0){
						Divisas dv = new Divisas(); //&& === validar si hay mas de un pago en el mismo recibo
						for (Recibodet rd : lstRecibodet) {
							BigDecimal bdMonto = v.getId().getMonto();
							bdMonto = bdMonto.subtract(rd.getMonto());
							v.getId().setMonto(dv.roundBigDecimal(bdMonto));
							lstRecibos.set(i, v);
						}
					}
				}
			}
		}catch(Exception error){
			lstRecibos = null;
			error.printStackTrace();
		}
		return lstRecibos;
	}
/***************************************************************************************/
/** obtener todos los recibos q no se pagaron con efectivo filtrados por tipo de recibo
 ********/
	public List obtenerRecibosxTipo(int iCaid, String sCodsuc,String sCodcomp, 
									String sMoneda,	String sTiporec,Date dtFechaArqueo,
									Date dtHoraInicio,Date dtHoraFin,Session sesion){
		List lstRecibos = new ArrayList();
		String sFecha = "",sql = "",sHini="",sHfin="";
		FechasUtil f = new FechasUtil();
		
		try{
			sFecha = f.formatDatetoString(dtFechaArqueo, "yyyy-MM-dd");
			
			sql =  " SELECT R.NUMREC,R.CAID,R.CODCOMP,R.CODSUC, R.FECHA RFECHA,R.HORA,R.MONTOAPL,RD.MONTO,RD.MPAGO,RD.EQUIV,";
			sql += " RD.TASA, RD.MONEDA RMONEDA, RD.MONEDA FMONEDA,R.TIPOREC,R.CLIENTE,RD.REFER1, RD.REFER2, RD.REFER3, RD.REFER4,R.ESTADO"; 
			sql += " FROM "+PropertiesSystem.ESQUEMA+".RECIBO R INNER JOIN "+PropertiesSystem.ESQUEMA+".RECIBODET RD ON R.NUMREC = RD.NUMREC AND R.CAID = RD.CAID";
			sql += " AND R.CODCOMP = RD.CODCOMP AND R.CODSUC = RD.CODSUC AND R.TIPOREC = '"+sTiporec+"'";
			sql += " WHERE R.CAID = "+iCaid + " AND R.CODCOMP = '"+sCodcomp+"' AND R.CODSUC =  '"+sCodsuc+"' AND R.TIPOREC = 'EX'"; 
			sql += " AND RD.MONEDA = '"+sMoneda+"' AND R.FECHA = '"+sFecha+"' AND R.ESTADO<>'A'";
			sHini = f.formatDatetoString(dtHoraInicio, "HH.mm.ss");
			sHfin = f.formatDatetoString(dtHoraFin, "HH.mm.ss");
			sql += " AND R.HORA >= '"+sHini+"' AND R.HORA <='"+sHfin+"'";
			
			lstRecibos = sesion.createSQLQuery(sql).addEntity(Vrecibosxtipoegreso.class).list();
			
		}catch(Exception error){
			error.printStackTrace();
			lstRecibos = null;
		}finally{
			f = null;
		}		
		return lstRecibos;
	}
/************************************************************************************/
/** Factura en moneda del arqueo, pagada con la misma moneda, y con devolucion pagada con moneda distinta del arqueo.
 *  Obtener las devoluciones que cumplen esa condicion.
 ********/
	public List obtenerRecxDevmonex5(int iCaid, String sCodsuc, String sCodcomp, 
									String sMoneda, String sTiporec, Date dtFechaArqueo, 
									Date dtHoraInicio, Date dtHoraFin, Session sesion) {
		List<Vreciboxdevolucion> lstDev = new ArrayList();
		List<Vreciboxdevolucion> lstReal = new ArrayList<Vreciboxdevolucion>();

		String sConsulta = "", sFecha = "";
		int iFechajul = 0;
		FechasUtil f = new FechasUtil();
		boolean mixto = false;
		boolean bUnico = false;
		
		
		try {

			
			iFechajul = f.obtenerFechaJulianaDia(dtFechaArqueo);
			sFecha = f.formatDatetoString(dtFechaArqueo, "yyyy-MM-dd");

			sConsulta = "from Vreciboxdevolucion as v";
			sConsulta += " where v.id.caid = " + iCaid + " and v.id.codsuc = '"+ sCodsuc + "' and ";
			sConsulta += " v.id.codcomp = '" + sCodcomp	+ "' and v.id.fecha = '" + sFecha + "' and ";
			sConsulta += " v.id.tiporec = '" + sTiporec	+ "' and v.id.fechadev = " + iFechajul + " and ";
			sConsulta += " v.id.mondev = '" + sMoneda	+ "' and v.id.moneda <> '" + sMoneda + "' and ";
			sConsulta += " v.id.mpago='"+MetodosPagoCtrl.EFECTIVO+"' and ";
			sConsulta += " v.id.fechadev = v.id.fechafact and ";
			sConsulta += " v.id.hora >='"+ f.formatDatetoString(dtHoraInicio, "HH.mm.ss") + "' and ";
			sConsulta += " v.id.hora <='"+ f.formatDatetoString(dtHoraFin, "HH.mm.ss") + "' and v.id.estado <> 'A'";

			LogCajaService.CreateLog("obtenerRecxDevmonex5", "QRY", sConsulta);
			lstDev = sesion.createQuery(sConsulta).list();

			// && ====== Validar que las devoluciones corresponden a facturas de
			// diferente moneda
			if (lstDev != null && lstDev.size() > 0) {
				Criteria cr = null;
				for (Vreciboxdevolucion v : lstDev) {
					cr = sesion.createCriteria(Recibodet.class);
					cr.add(Restrictions.eq("id.caid", v.getId().getCaid()));
					cr.add(Restrictions.eq("id.codcomp", v.getId().getCodcomp()));
					cr.add(Restrictions.eq("id.codsuc", v.getId().getCodsuc()));
					cr.add(Restrictions.eq("id.numrec", v.getId().getNodoco()));
					cr.add(Restrictions.eq("id.moneda", sMoneda));
					cr.add(Restrictions.eq("id.mpago", MetodosPagoCtrl.EFECTIVO ));
					
					LogCajaService.CreateLog("obtenerRecxDevmonex5", "QRY", LogCajaService.toSql(cr));

					List<Recibodet> lstDetFact = cr.list();
					if (lstDetFact != null && lstDetFact.size() > 0){
						//validar que el pago de la factura original no haya sido pago mixto
						mixto = isPagoOriginalMixto(v.getId().getCaid(), v.getId().getCodcomp(),v.getId().getNodoco(),sMoneda);
						if(!mixto){
						
							mixto = isPagoOriginalFullForaneo(v.getId().getCaid(),v.getId().getCodcomp(),v.getId().getNodoco(),sMoneda);
							if(mixto)
								//if(v.getId().getMontodev().doubleValue() == v.getId().getMontofact().doubleValue())
									lstReal.add(v);
						}
					}
						
				}
			}
			
		} catch (Exception error) {
			LogCajaService.CreateLog("obtenerRecxDevmonex5", "ERR", error.getMessage());
		}
		return lstReal;
	}
	
/************************************************************************************/
/** Devolucion a factura con moneda distinta del arqueo y pagada con moneda del arqueo
 *  Ej: Devolucion en dolares de factura en dolares pagada con cordobas.
 ********/
	public List obtenerRecxDevmonex4(boolean bIngreso,int iCaid,String sCodsuc,String sCodcomp,
											String sMoneda,	String sTiporec,Date dtFechaArqueo,
											Date dtHoraInicio,Date dtHoraFin,Session sesion){
		List<Vreciboxdevolucion> lstDev = new ArrayList();
		List<Vreciboxdevolucion>lstReal = new ArrayList<Vreciboxdevolucion>();
		
		String sConsulta = "",sFecha = "";
		int iFechajul = 0;
		FechasUtil f = new FechasUtil();
		boolean mixto = false;
		try{
			iFechajul = f.obtenerFechaJulianaDia(dtFechaArqueo);
			sFecha = f.formatDatetoString(dtFechaArqueo, "yyyy-MM-dd");

			sConsulta = "from Vreciboxdevolucion as v";
			sConsulta += " where v.id.caid = "+iCaid+" and v.id.codsuc = '"+sCodsuc+"' and "; 
			sConsulta += " v.id.codcomp = '"+sCodcomp+"' and v.id.fecha = '"+sFecha+"' and ";
			sConsulta += " v.id.tiporec = '"+sTiporec+"' and v.id.fechadev = "+iFechajul +" and "; 
			sConsulta += " v.id.monfac <> '"+sMoneda+"' and v.id.moneda = '"+sMoneda+"' and ";
			sConsulta += " v.id.mpago='"+MetodosPagoCtrl.EFECTIVO+"' and ";
			sConsulta += " v.id.hora >='"+f.formatDatetoString(dtHoraInicio,"HH.mm.ss")+"' and ";
			sConsulta += " v.id.hora <='"+f.formatDatetoString(dtHoraFin,"HH.mm.ss")+"' and v.id.estado <> 'A' ";
			
			lstDev = sesion.createQuery(sConsulta).list();
			
			//&& ====== Validar que las devoluciones corresponden a facturas de diferente moneda
			if(lstDev!=null && lstDev.size()>0){
				Criteria cr  = null;
				for (Vreciboxdevolucion v : lstDev) {
					
					//&& ==== buscar en recibo original un pago efectivo con moneda distinta a la del arqueo
					cr = sesion.createCriteria(Recibodet.class);
					cr.add(Restrictions.eq("id.caid", v.getId().getCaid()));
					cr.add(Restrictions.eq("id.codcomp", v.getId().getCodcomp()));
					cr.add(Restrictions.eq("id.codsuc", v.getId().getCodsuc()));
					cr.add(Restrictions.eq("id.numrec", v.getId().getNodoco()));
					cr.add(Restrictions.ne("id.moneda", sMoneda));
					cr.add(Restrictions.eq("id.mpago", MetodosPagoCtrl.EFECTIVO ));
					
					List<Recibodet>lstDetFact = cr.list(); 
					if(lstDetFact!=null && lstDetFact.size() > 0){
						
						if(bIngreso){

							//validar que el pago de la factura original no haya sido pago mixto
							mixto = isPagoOriginalMixto(v.getId().getCaid(), v.getId().getCodcomp(),v.getId().getNodoco(),sMoneda);
							
							if(!mixto){
								mixto = isPagoOriginalFullForaneo(v.getId().getCaid(),v.getId().getCodcomp(),v.getId().getNodoco(),sMoneda);
								if(!mixto)
									lstReal.add(v);
							}
						}else{
							
							if(sMoneda.compareTo("COR") == 0 && lstDetFact.size() == 1 ){
								if(v.getId().getMontodev().compareTo(
									v.getId().getMontofact() ) == 0
										&& v.getId().getMontodev().compareTo(
										lstDetFact.get(0).getEquiv()) == 0 ){
									lstReal.add(v);
								}
							}
							if(!sMoneda.equals("COR"))
								lstReal.add(v);
						}
					}
				}
			}
			
		}catch(Exception error){
			lstDev = null;
			error.printStackTrace();
		}
		return lstReal;
	}
/************************************************************************************/
/** obtener los recibos por pago de devoluciones con moneda distinta de la factura.
 *  obtener recibos por devolución pagadas con moneda extranjera (diferente a moneda fac)
 ********/
		public List obtenerRecxDevmonex(boolean bIngreso,int iCaid,String sCodsuc,String sCodcomp,
											String sMoneda,	String sTiporec,Date dtFechaArqueo,
											Date dtHoraInicio,Date dtHoraFin,Session sesion){
		List lstDev = new ArrayList(), lstNewDev = new ArrayList();
		String sConsulta = "",sFecha = "",sHini,sHfin; ;
		int iFechajul = 0;
		FechasUtil f = new FechasUtil();
		boolean mixto = false;
		Vreciboxdevolucion v = null;
		try{
			
			iFechajul = f.obtenerFechaJulianaDia(dtFechaArqueo);
			sFecha = f.formatDatetoString(dtFechaArqueo, "yyyy-MM-dd");
			
			sConsulta =  " from Vreciboxdevolucion as v where v.id.caid = "+iCaid+" and v.id.codsuc = '"+sCodsuc+"'";
			sConsulta += " and v.id.codcomp = '"+sCodcomp+"' and v.id.fecha = '"+sFecha+"' and v.id.tiporec = '"+sTiporec+"'";			
			sConsulta += " and v.id.fechadev = "+iFechajul+" and v.id.fechafact = "+iFechajul + " and v.id.estado <> 'A' and v.id.montoapl < v.id.montofact ";
			
			if(bIngreso)
				sConsulta += " and v.id.mpago = '"+MetodosPagoCtrl.EFECTIVO+"' and v.id.moneda = '"+sMoneda+"' and v.id.monfac <> '"+sMoneda+"'";
			else
				sConsulta += " and v.id.moneda <> '"+sMoneda+"' and v.id.monfac = '"+sMoneda+"'";

			sHini = f.formatDatetoString(dtHoraInicio,"HH.mm.ss");
			sHfin = f.formatDatetoString(dtHoraFin,"HH.mm.ss");
			sConsulta += " and v.id.hora >='"+sHini+"' and v.id.hora<='"+sHfin+"'";
			
			lstDev = sesion.createQuery(sConsulta).list();

		}catch(Exception error){
			lstDev = null;
			error.printStackTrace();
		}
		return lstDev;
	}
		
	public List obtenerRecxDevmonex2(boolean bIngreso, int iCaid,
			String sCodsuc, String sCodcomp, String sMoneda, String sTiporec,
			Date dtFechaArqueo, Date dtHoraInicio, Date dtHoraFin,
			Session sesion) {
		List lstDev = new ArrayList();
		String sConsulta = "", sFecha = "", sHini, sHfin;
		;
		int iFechajul = 0;
		FechasUtil f = new FechasUtil();

		try {

			iFechajul = f.obtenerFechaJulianaDia(dtFechaArqueo);
			sFecha = f.formatDatetoString(dtFechaArqueo, "yyyy-MM-dd");

			sConsulta = " from Vreciboxdevolucion as v where v.id.caid = " + iCaid + " and v.id.codsuc = '" + sCodsuc + "'";
			sConsulta += " and v.id.codcomp = '" + sCodcomp + "' and v.id.fecha = '" + sFecha + "' and v.id.tiporec = '" + sTiporec + "'";
			sConsulta += " and v.id.fechadev = " + iFechajul +" and v.id.fechafact < "+iFechajul + " and v.id.estado <> 'A' ";

			
			sConsulta += " and v.id.mpago = '"+MetodosPagoCtrl.EFECTIVO+"' ";
			
			sConsulta += " and v.id.moneda = '" + sMoneda
						+ "' and v.id.monfac <> '" + sMoneda + "'";

			sHini = f.formatDatetoString(dtHoraInicio, "HH.mm.ss");
			sHfin = f.formatDatetoString(dtHoraFin, "HH.mm.ss");
			sConsulta += " and v.id.hora >='" + sHini + "' and v.id.hora<='"
					+ sHfin + "'";

			lstDev = sesion.createQuery(sConsulta).list();

		} catch (Exception error) {
			lstDev = null;
			error.printStackTrace();
		} 
		return lstDev;
	}
	
	public List obtenerRecxDevmonex3(boolean bIngreso, int iCaid,
			String sCodsuc, String sCodcomp, String sMoneda, String sTiporec,
			Date dtFechaArqueo, Date dtHoraInicio, Date dtHoraFin,
			Session sesion) {
		List lstDev = new ArrayList();
		String sConsulta = "", sFecha = "", sHini, sHfin;
		int iFechajul = 0;
		FechasUtil f = new FechasUtil();

		try {

			iFechajul = f.obtenerFechaJulianaDia(dtFechaArqueo);
			sFecha = f.formatDatetoString(dtFechaArqueo, "yyyy-MM-dd");

			sConsulta = " from Vreciboxdevolucion as v where v.id.caid = " + iCaid + " and v.id.codsuc = '" + sCodsuc + "'";
			sConsulta += " and v.id.codcomp = '" + sCodcomp + "' and v.id.fecha = '" + sFecha + "' and v.id.tiporec = '" + sTiporec + "'";
			sConsulta += " and v.id.fechadev = " + iFechajul +" and v.id.fechafact < "+iFechajul + " and v.id.estado <> 'A'  and v.id.montoapl <= v.id.montofact ";

			
			sConsulta += " and v.id.mpago = '"+MetodosPagoCtrl.EFECTIVO+"' ";
			
			sConsulta += " and v.id.moneda <> '" + sMoneda
						+ "' and v.id.monfac = '" + sMoneda + "'";

			sHini = f.formatDatetoString(dtHoraInicio, "HH.mm.ss");
			sHfin = f.formatDatetoString(dtHoraFin, "HH.mm.ss");
			sConsulta += " and v.id.hora >='" + sHini + "' and v.id.hora<='"
					+ sHfin + "'";

			lstDev = sesion.createQuery(sConsulta).list();

		} catch (Exception error) {
			lstDev = null;
			error.printStackTrace();
		}
		return lstDev;
	}
		
/******************************************************************************************/
/** Método: Construir y enviar el correo de notificación de cambio de tasa en recibos.
 *	Fecha:  28/06/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public boolean enviarCorreoCambioTasa(String sFrom,String sNombreCajero,String sTo, List<String> sCc,String sTablaRecibos,
										  String sEnlace,String sCaja,String sUbicacion,String sCajero,String sFecha){
		boolean bHecho=true;
		// MultiPartEmail email = new MultiPartEmail();
		StringBuilder sbTablaCorreo= new StringBuilder();
		String sEstiloTD1="",sEstiloTD2="";
		
		try {
			sbTablaCorreo.append("<table width=\"480px\" style=\"border: 1px #7a7a7a solid\" align=\"center\" cellspacing=\"0\" cellpadding=\"3\">");
			sbTablaCorreo.append("<tr>"); 
			sbTablaCorreo.append("<th colspan=\"2\" style=\"border-bottom: 1px #7a7a7a solid; background: #3e68a4\">");
			sbTablaCorreo.append("<font face=\"Arial\" size=\"2\" color=\"white\"><b>Notificación de cambio de tasa en pago de recibos.</b></font>");
			sbTablaCorreo.append("</th>");
			sbTablaCorreo.append("</tr>");
		
			sEstiloTD1 = "style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"15%\"";
			sEstiloTD2 = "style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"85%\"";

			sbTablaCorreo.append("<tr><td "+sEstiloTD1 + ">");
			sbTablaCorreo.append("<b>Cajero:</b></td>");
			sbTablaCorreo.append("<td "+sEstiloTD2 + ">");
			sbTablaCorreo.append(sCajero);
			sbTablaCorreo.append("</td>");
			sbTablaCorreo.append("</tr>");
			
			sbTablaCorreo.append("<tr><td "+sEstiloTD1 + ">");
			sbTablaCorreo.append("<b>Caja:</b></td>");
			sbTablaCorreo.append("<td "+sEstiloTD2 + ">");
			sbTablaCorreo.append(sCaja);
			sbTablaCorreo.append("</td>");
			sbTablaCorreo.append("</tr>");
			
			sbTablaCorreo.append("<tr><td "+sEstiloTD1 + ">");
			sbTablaCorreo.append("<b>Ubicación:</b></td>");
			sbTablaCorreo.append("<td "+sEstiloTD2 + ">");
			sbTablaCorreo.append(sUbicacion);
			sbTablaCorreo.append("</td>");
			sbTablaCorreo.append("</tr>");
			
			sbTablaCorreo.append("<tr><td "+sEstiloTD1 + ">");
			sbTablaCorreo.append("<b>Fecha:</b></td>");
			sbTablaCorreo.append("<td "+sEstiloTD2 + ">");
			sbTablaCorreo.append(sFecha);
			sbTablaCorreo.append("</td>");
			sbTablaCorreo.append("</tr>");
			
			sbTablaCorreo.append("<tr><td colspan=\"2\" height=\"10\"></td></tr>");
			sbTablaCorreo.append("<tr>");
			sbTablaCorreo.append("<td colspan=\"2\" align=\"left\" "+sEstiloTD1+">");
			sbTablaCorreo.append("<b>RECIBOS:</b>");
			sbTablaCorreo.append(sTablaRecibos);
			sbTablaCorreo.append("</td>");
			sbTablaCorreo.append("</tr>");
			sbTablaCorreo.append("<tr><td height=\"20\" colspan=\"2\" ></td></tr>");
			
			sbTablaCorreo.append("<td colspan=\"2\" align=\"center\" "+sEstiloTD2 + ">");
			sbTablaCorreo.append(sEnlace);
			sbTablaCorreo.append("</td>");
			sbTablaCorreo.append("</tr>");
			
			sbTablaCorreo.append("<tr>");
			sbTablaCorreo.append("<td align=\"center\" colspan=\"2\" style=\"font-family: Arial, Helvetica, sans-serif;font-size: 10px;");
			sbTablaCorreo.append("color: black; border-bottom: 1px ##1a1a1a solid; \">");
			sbTablaCorreo.append("<b>Casa Pellas, S. A. - Módulo de Caja</b>");
			sbTablaCorreo.append("</td>");	
			sbTablaCorreo.append("</tr>");
			sbTablaCorreo.append("</table>");
			
			Divisas dv = new Divisas();
			String sNombreFrom = dv.ponerCadenaenMayuscula(sNombreCajero);
			sNombreFrom = (sNombreFrom.equals(""))?sNombreCajero:sNombreFrom;
			
			List<CustomEmailAddress> ccCopy = new ArrayList<CustomEmailAddress>();
			if(sCc!=null){
				 for (String sCopia : sCc) 
					 ccCopy.add(new CustomEmailAddress(sCopia));
			}
			
			for (int i = 0; i < PropertiesSystem.MAILBCCS.length; i++) {
				String[] dtsCc = PropertiesSystem.MAILBCCS[i].split(PropertiesSystem.SPLIT_CHAR);
				ccCopy.add(new CustomEmailAddress(dtsCc[0],dtsCc[1]));
			}
			
			MailHelper.SendHtmlEmail(
					new CustomEmailAddress(sFrom,sNombreFrom),
					new CustomEmailAddress(sTo), ccCopy,  
					"Cambio de Tasa de pago en Recibos", sbTablaCorreo.toString());
			
		} catch (Exception error) {
			bHecho = false;
			error.printStackTrace();
		}
		return bHecho;
	}
}

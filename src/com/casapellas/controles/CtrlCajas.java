package com.casapellas.controles;
/**
 * CASA PELLAS S.A.
 * Creado por.........: Juan Carlos Ñamendi Pineda
 * Fecha de Creación..: 27/11/2008
 * Última modificación: Carlos Manuel Hernández Morrison
 * Modificado por.....:	09/03/2011
 * Descripcion:.......: Administración y acceso de los datos de las cajas.
 **/

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.faces.context.FacesContext;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.casapellas.entidades.Arqueo;
import com.casapellas.entidades.F55ca01;
import com.casapellas.entidades.F55ca014;
import com.casapellas.entidades.F55ca017;
import com.casapellas.entidades.F55ca020;
import com.casapellas.entidades.Hfactjdecon;
import com.casapellas.entidades.ens.Vautoriz;
import com.casapellas.entidades.Vf0101;
import com.casapellas.entidades.Vf55ca01;
import com.casapellas.entidades.Vf55ca01Id;
import com.casapellas.entidades.Vrptmcaja005;
import com.casapellas.entidades.Vrptmcaja005Id;
import com.casapellas.entidades.Vrptmcaja006;
import com.casapellas.entidades.Vrptmcaja006Id;
import com.casapellas.entidades.Vtransaccionesjde;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.util.CustomEmailAddress;
import com.casapellas.util.Divisas;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.MailHelper;
import com.casapellas.util.PropertiesSystem;
import com.ibm.icu.util.Calendar;

public class CtrlCajas {
	
	public CtrlCajas() {
		super();
	}

	//Datos de la caja
	public String lblNoCaja;
	public String lblCajero;
	public String lblSucursal;
	public String ambienteDeSistemaCaja;
	public String ambienteDeSistemaJdeDTA;
	public String ambienteDeSistemaJdeCOM;
	public String copyrightSistemaCaja;
	
	//Recibo actual
	public String lblNumeroRecibo;
		
	//Definiciones de cajas
	F55ca01[] cajas = null;
	F55ca014[] cajascom = null;
	
	
	public static List<F55ca01> obtenerCajas(){
		List<F55ca01> lstCajas = null;
	
		try{
			
			String sql = "from F55ca01 as f where f.id.castat = 'A' order by f.id.caid";
			lstCajas = ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, F55ca01.class, false);
			
		}catch(Exception ex){
			ex.printStackTrace();
		} 
		return lstCajas;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public static List<F55ca01> getCajasF55ca01(){
		List<F55ca01> cajas = null;
		Session sesion = null; 
		
		try {
			
			sesion = HibernateUtilPruebaCn.currentSession();
			
			cajas = (ArrayList<F55ca01>) sesion.createCriteria(F55ca01.class)
					.add(Restrictions.eq("id.castat", 'A')).list();

		} catch (Exception e) {
			cajas = null;
			e.printStackTrace();
		}finally{
			
			if(cajas == null)
				cajas = new ArrayList<F55ca01>();			
			
		}
		return cajas;
	}
	
	public static String generarMensajeBlk(){
		String sMensaje = "";
		try {
			Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
			
			if(m.get("BLOQUEOCAJA") != null){
				String sValor = String.valueOf(m.get("BLOQUEOCAJA"));
				sMensaje = "" +
						"No se pueden registrar transacciones.<br>"+
						"La caja se encuentra bajo bloqueo temporal<br> "+
						"Existen "+sValor.split("@")[0]+
						" arqueos pendientes de aprobación.<br>"+
						" Bajo el límite de fecha permitido: "+
						sValor.split("@")[1]+
						"<br><br>Favor comunicarse con el contador de caja";
			}
			
		} catch (Exception e) {
			sMensaje = "";
			e.printStackTrace();
		}
		return sMensaje;
	}
	
	/**************************************************
	 *  Método: GCPMCAJA / com.casapellas.controles /bloquearCaja
	 *  Descrp: Validar si la caja debe ser bloqueada.
	 *	Fecha:  Nov 27, 2012 
	 *  Autor:  CarlosHernandez
	 ***/
	@SuppressWarnings("unchecked")
	public static boolean bloquearCaja(int iCaid) {
		boolean bBloqueo = false;
		int iDias = 3;
		int iAvance = -1;
		int iProxEst = -1;
		Session sesion = null;
		Transaction trans = null;
		
		try {
			
			sesion = HibernateUtilPruebaCn.currentSession();
			trans = sesion.beginTransaction();
			
			Criteria cr = sesion.createCriteria(F55ca01.class);
			cr.add(Restrictions.eq("id.caid", iCaid));
			cr.add(Restrictions.eq("id.castat", 'A'));
			cr.setMaxResults(1);
			
			F55ca01 f5 = (F55ca01)cr.uniqueResult();
			
			String sql ="select * from gcpsiseva2.workcalendar"+
					" where :FECHA  between startdate and enddate " +
					" and plan = :PLAN "+
					" and clocal =  'Nacional' and trim(Ccountry) = 'NI' " ;
			
			//&& ===== Si tiene cero, no validar.
			if(f5.getId().getCadiablk() == 0){
				trans.commit();
				sesion.close();
				trans = null;
				sesion = null;
				return false;
			}
			
			iDias = f5.getId().getCadiablk();
			Calendar cal = Calendar.getInstance();
			while(iDias != 0){
				cal.add(Calendar.DATE, iAvance);
				
				Query q = sesion.createSQLQuery(sql);
				q.setParameter("FECHA", cal.getTime());
				q.setParameter("PLAN", String.valueOf(cal.get(Calendar.YEAR)));
				List<Object[]>lstFeriado = q.list();
				
				if(cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && 
					(lstFeriado == null || lstFeriado.isEmpty()) )  
					iDias--;
			}

			cr = sesion.createCriteria(Arqueo.class);
			cr.add(Restrictions.eq("id.caid", iCaid));
			cr.add(Restrictions.eq("estado","P"));
			cr.add(Restrictions.lt("id.fecha", cal.getTime()));
			cr.addOrder(Order.asc("id.fecha"));
			
			List<Arqueo>lstArqueo = cr.list();
			
			Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
			
			//&& ==== No hay cierre y esta bloqueada: Desbloquear.
			if( (lstArqueo == null || lstArqueo.isEmpty())){
				m.remove("BLOQUEOCAJA");

				if(f5.getId().getCastdblk() == 1 )	
					iProxEst = 0;
				
			}
			//&& ==== Hay cierre y esta desbloqueada: bloquear.
			if(lstArqueo != null && lstArqueo.size() > 0 ){
				m.put("BLOQUEOCAJA", lstArqueo.size()+"@"
						+new SimpleDateFormat("dd/MM/yyyy")
							.format(cal.getTime()));
				if(f5.getId().getCastdblk() == 0 )
					iProxEst = 1;
			}
			
			if(iProxEst != -1){
				String sqlUpd  = " update "+PropertiesSystem.ESQUEMA+".f55ca01 " +
							 " set castdblk = " +iProxEst + 
							 " where caid = "+iCaid;

				Query q = sesion.createSQLQuery(sqlUpd);
				q.executeUpdate();
				
				//&& ====== Correo de notificacion =========
				cr = sesion.createCriteria(Vf55ca01.class);
				cr.add(Restrictions.eq("id.caid", iCaid));
				cr.setMaxResults(1);
				
				Vf55ca01 vf = (Vf55ca01)cr.uniqueResult();
				Vautoriz[] vAut = (Vautoriz[])m.get("sevAut");
				Vf0101 vf01 =  new EmpleadoCtrl()
									.buscarEmpleadoxCodigo2(
											vAut[0].getId().getCodreg());
				String sComodin = "=>";
				String sCorreos = vf.getId().getCacontmail().trim()+ sComodin
								+ vf01.getId().getWwrem1().trim().toLowerCase()+ sComodin 
								+ vf.getId().getCaan8mail().trim();
				String sNombres = vf.getId().getCacontnom().trim()+ sComodin
								+ vf01.getId().getAbalph().trim().toLowerCase()+ sComodin
								+ vf.getId().getCaan8nom().trim();
				
				List<CustomEmailAddress> toList = new ArrayList<CustomEmailAddress>();
				
				String[] lstCorreos = sCorreos.split(sComodin);
				String[] lstNombres = sNombres.split(sComodin);
				
				Divisas dv = new Divisas();
				for (int i = 0; i < lstCorreos.length; i++){
					if(lstCorreos[i].trim().toUpperCase()
							.matches("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$")){
						toList.add(new CustomEmailAddress(lstCorreos[i].toLowerCase(), dv.ponerCadenaenMayuscula(lstNombres[i])));
					}
				}
				String sNota = "";
				String sSubject = "";
				sNota = "<strong>Estimado(a):</strong><em> "+ dv.ponerCadenaenMayuscula(
						vf.getId().getCacontnom().trim())+"</em>:";
				sNota += "<br>";
				sNota += "<br>";
				
				if(iProxEst == 1){
					sSubject = "Bloqueo temporal en procesos de caja "
									+vf.getId().getCaname().toLowerCase();

					sNota += "La caja # "+f5.getId().getCaid();
					sNota += " ("+dv.ponerCadenaenMayuscula(vf.getId().getCaname().trim())+") ";
					sNota += "Se encuentra temporalmente bloqueada";
					sNota += "<br>";
					sNota += "Esto se debe a que actualmente existen "+lstArqueo.size() +" arqueos pendientes ";
					sNota += "<br>";
					sNota += "de aprobación con fechas anteriores a "
								+new SimpleDateFormat("dd/MMMM/yyyy", 
										new Locale("ES","es")).format(cal.getTime());
					sNota += "<br>";
					sNota += "<br>";
					sNota += "<strong>Favor tomar nota, aprobar estos cierres lo antes posible " +
							"y permitir a la caja funcionar con normalidad</strong>";
				}else{
					sSubject = "Bloqueo liberado para procesos de caja "
									+vf.getId().getCaname().toLowerCase();
					sNota += "La caja # "+f5.getId().getCaid();
					sNota += " ("+dv.ponerCadenaenMayuscula(vf.getId().getCaname().trim())+")";
					sNota += " ha reestablecido su normal funcionamiento";
					sNota += "<br>";
					sNota += " Agradecemos su pronta cooperación en el proceso de cierres de caja ";
					sNota += "<br><br>";
					sNota += "<strong>Fecha desbloqueo:</strong>"+
									new SimpleDateFormat("dd MMMM yyyy hh:mm:ss a", 
											new Locale("ES","es")).format(new Date());
				}
				sNota += "<br><br>";
				sNota += "<strong>Saludos.</strong>";
				sNota += "<br><em>Sistema de Pagos en Caja.</em>";
				sNota += "<br><em>Casa Pellas SA.</em>";
				
				MailHelper.SendHtmlEmail(
						new CustomEmailAddress(PropertiesSystem.WEBMASTER_EMAIL_ADRESS, "Módulo de Caja: Notificación de Traslado de Factura"),
						toList, null, new CustomEmailAddress(PropertiesSystem.MAIL_BOUNCEADDRESS), 
						sSubject, sNota);
				
			}
			trans.commit();

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try { sesion.close(); } catch (Exception e2){}
		}
		return bBloqueo;
	}
	/* ******************** fin de metodo bloquearCaja ****************************/
	
	
	/**************************************************
	 *  Método: GCPMCAJA / com.casapellas.controles /validarReferenciaJDE
	 *  Descrp: Verifica que el numero de referencia exista en JDE
	 *	Fecha:  Oct 17, 2012 
	 *  Autor:  CarlosHernandez
	 ***/
	@SuppressWarnings("unchecked")
	public static boolean validarReferenciaJDE(int iReferencia,String sTipodoc,
								String sCompCuenta, Session sesion) {
		boolean bExiste = false;
		int iValorRefer = 0;
		List<Object[]> lstRegist = null;
		
		try {
			iValorRefer = iReferencia;
			
			if(String.valueOf(iReferencia).length() > 8){
				String sNueva = String.valueOf(iReferencia);
				sNueva = sNueva.substring(sNueva.length()-8, sNueva.length());
				iValorRefer = Integer.parseInt(sNueva);
			}
			
			String sql = 
					" SELECT * FROM "+PropertiesSystem.JDEDTA+".F0911 WHERE GLDOC = " 
					+ iValorRefer + " and gldct = '" 
					+ sTipodoc + "'"
					+ " AND GLKCO = '000" 
					+ sCompCuenta + "'";
			
			lstRegist = sesion.createSQLQuery(sql).list();
			if(lstRegist != null && lstRegist.size() > 0)
				bExiste = true;
			
			sql = null;
			
		} catch (Exception e) {
			bExiste = false;
			e.printStackTrace();
			e = null;
		}
		lstRegist = null; 
		return bExiste;
	}
	/* ******************** fin de metodo validarReferenciaJDE ****************************/
	
	
/*******************************************************************************************/
	public List leerTraslados(int iCaid, int iFechaActual){
		StringBuilder sbSql2 = new StringBuilder();
		List lstFacturas = new ArrayList();
		Session session = null; 		
		try{
			session = HibernateUtilPruebaCn.currentSession();
			
			sbSql2.append(" select f.* from "+PropertiesSystem.ESQUEMA+".Hfactjdecon as f inner join "+PropertiesSystem.ESQUEMA+".trasladofac tf"); 
			sbSql2.append(" on  tf.nofactura = f.nofactura and tf.codcomp = f.codcomp and tf.tipofactura = f.tipofactura");
			sbSql2.append(" and tf.codsuc = f.codsuc and trim(tf.codunineg)  = trim(f.codunineg) and tf.estadotr = 'E'");
			sbSql2.append(" and tf.caiddest = "+iCaid+" and f.fecha = "+ iFechaActual +" and trim(f.estado)<>'A' ");
			
			LogCajaService.CreateLog("leerTraslados", "QRY", sbSql2.toString());
			
			lstFacturas = session.createSQLQuery(sbSql2.toString()).addEntity(Hfactjdecon.class).list();
		}catch(Exception ex){
			LogCajaService.CreateLog("leerTraslados", "ERR", ex.getLocalizedMessage());
		}
		return lstFacturas;
	}
	/**********************OBTENER CAJA POR ID DE CAJA*******************************/
	public List obtenerCajaxXCodigo(int iCaid,String sCaco,Connection cn){
		List lstCajas = null;		
		String sql = "SELECT * from "+PropertiesSystem.ESQUEMA+".Vf55ca01 as f where f.caid = "+iCaid+ " and f.caco = '"+sCaco+"' and f.castat = 'A'";
		Vf55ca01 v = null;
		Vf55ca01Id vId = null;
		PreparedStatement ps = null;
		try{		
			ps = cn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = ps.executeQuery();
			rs.last();//move to last row
			int numHeaders = rs.getRow();//rowcount
			rs.beforeFirst();//move before the first row
			
			if (numHeaders > 0){
				lstCajas = new ArrayList();
				while (rs.next()){
					v = new Vf55ca01();
					vId = new Vf55ca01Id();
					vId.setCaid(rs.getInt("CAID"));
					vId.setCaname(rs.getString("CANAME"));
					vId.setCaco(rs.getString("CACO"));
					vId.setCaconom(rs.getString("CACONOM"));
					vId.setCaan8(rs.getInt("CAAN8"));
					vId.setCacati(rs.getInt("CACATI"));
					vId.setCaauti(rs.getInt("CAAUTI"));
					vId.setCaausu(rs.getInt("CAAUSU"));
					vId.setCacont(rs.getInt("CACONT"));
					vId.setCajobn(rs.getString("CAJOBN"));
					vId.setCarmk(rs.getString("CARMK"));
					vId.setCarmk1(rs.getString("CARMK1"));
					vId.setCastat((rs.getString("CASTAT").toCharArray())[0]);
					vId.setCaprnt(rs.getString("CAPRNT"));
					vId.setCacatinom(rs.getString("CACATINOM"));
					vId.setCacatimail(rs.getString("CACATIMAIL"));
					vId.setCaan8nom(rs.getString("CAAN8NOM"));
					vId.setCaan8mail(rs.getString("CAAN8MAIL"));
					vId.setCacontnom(rs.getString("CACONTNOM"));
					vId.setCacontmail(rs.getString("CACONTMAIL"));
					vId.setCaausunom(rs.getString("CAAUSUNOM"));
					vId.setCaausumail(rs.getString("CAAUSUMAIL"));
					vId.setCaautinom(rs.getString("CAAUTINOM"));
					vId.setCaautimail(rs.getString("CAAUTIMAIL"));
					
					v.setId(vId);
					
					lstCajas.add(v);						
				}				
			}	
			rs.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}finally {			
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return lstCajas;
	}
/**************************************************************************************/	
/**********************OBTENER TODAS LAS CAJAS CONFIGURADAS********************************/
	public List getAllCajas(Connection cn){
		List lstCajas = null;
		String sql = "SELECT * from "+PropertiesSystem.ESQUEMA+".Vf55ca01 as f where f.castat = 'A'";
		Vf55ca01 v = null;
		Vf55ca01Id vId = null;
		PreparedStatement ps = null;
		try{
			ps = cn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = ps.executeQuery();
			rs.last();
			int numHeaders = rs.getRow();
			rs.beforeFirst();
			
			if (numHeaders > 0){
				lstCajas = new ArrayList();
				while (rs.next()){
					v = new Vf55ca01();
					vId = new Vf55ca01Id();
					vId.setCaid(rs.getInt("CAID"));
					vId.setCaname(rs.getString("CANAME"));
					vId.setCaco(rs.getString("CACO"));
					vId.setCaconom(rs.getString("CACONOM"));
					vId.setCaan8(rs.getInt("CAAN8"));
					vId.setCacati(rs.getInt("CACATI"));
					vId.setCaauti(rs.getInt("CAAUTI"));
					vId.setCaausu(rs.getInt("CAAUSU"));
					vId.setCacont(rs.getInt("CACONT"));
					vId.setCajobn(rs.getString("CAJOBN"));
					vId.setCarmk(rs.getString("CARMK"));
					vId.setCarmk1(rs.getString("CARMK1"));
					vId.setCastat((rs.getString("CASTAT").toCharArray())[0]);
					vId.setCaprnt(rs.getString("CAPRNT"));
					vId.setCacatinom(rs.getString("CACATINOM"));
					vId.setCacatimail(rs.getString("CACATIMAIL"));
					vId.setCaan8nom(rs.getString("CAAN8NOM"));
					vId.setCaan8mail(rs.getString("CAAN8MAIL"));
					vId.setCacontnom(rs.getString("CACONTNOM"));
					vId.setCacontmail(rs.getString("CACONTMAIL"));
					vId.setCaausunom(rs.getString("CAAUSUNOM"));
					vId.setCaausumail(rs.getString("CAAUSUMAIL"));
					vId.setCaautinom(rs.getString("CAAUTINOM"));
					vId.setCaautimail(rs.getString("CAAUTIMAIL"));
					
					v.setId(vId);
					
					lstCajas.add(v);	
					
				}
				
			}	
			rs.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
		
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return lstCajas;
	}
/**************************************************************************************/
	
/****************************************************************************************/
/** Método: Realiza validaciones para comprobar la disponibilidad de la referencia para NOJDE	
 *  Fecha: 13/09/2010
 *  Hecho: Carlos Manuel Hernández Morrison.
 */
	public String verificarReferenciaPago(String sReferenciaPago, int iNoreferdep,String sTipodoc, int iCodbanco,
										  String sCodcomp,String sMoneda,String sMpago){
		@SuppressWarnings("rawtypes")
		List lstRegist = null;	
		Session sesion = null;		
		String sql = "", sCuenta[];
		Divisas dv = new Divisas();
		String iResul=null;

		try {

			sesion = HibernateUtilPruebaCn.currentSession();	
			
			//--- Leer la cuenta de banco para determinar la sucursal de la cuenta, que se usa GLKCO
			sCuenta = dv.obtenerCuentaBanco(sCodcomp, sMoneda,iCodbanco, sesion, null, sesion, null);
			if(sCuenta!=null){
				
				sql =  " SELECT * FROM "+PropertiesSystem.JDEDTA+".F0911 WHERE GLDOC = "+ iNoreferdep+ " and gldct = '"+sTipodoc+"'";
				sql += " AND GLKCO = '000"+sCuenta[2]+"'";
				
				LogCajaService.CreateLog("verificarReferenciaPago", "QRY", sql);
				
				lstRegist = sesion.createSQLQuery(sql).list();
				if(lstRegist!=null && lstRegist.size()>0)
					iResul = "El número de referencia de Depósito " +iNoreferdep+ " ya ha sido registrado en JDE";
				else{
					//--- Verificar el número de referencia contra los registros de recibos de caja.
					sql = " from Recibodet rd where rd.id.refer1= '"+String.valueOf(iCodbanco)+"' ";
					sql+= " and rd.id.moneda = '" +sMoneda+ "' and rd.id.codcomp = '" +sCodcomp+ "' ";
					sql+= " and (   (rd.id.refer2 = '" +sReferenciaPago+ "') or (rd.id.refer3 = '" +sReferenciaPago+ "')   )";
					
					LogCajaService.CreateLog("verificarReferenciaPago", "QRY", sql);
					
					lstRegist = sesion.createQuery(sql).list();
					if(lstRegist!=null && lstRegist.size()>0)
						iResul = "El número de referencia de Depósito " +iNoreferdep+ " ya ha sido registrado en Recibos - Caja";
					else{						
						//--- Validar contra el arqueo 
						sql = " from Arqueo a where trim(a.referdep) = trim('" +sReferenciaPago+ "') ";
						sql+= " and a.moneda = '" +sMoneda+ "' and a.id.codcomp = '" +sCodcomp+ "'";
						sql+= " and a.estado = 'P'";
						
						LogCajaService.CreateLog("verificarReferenciaPago", "QRY", sql);
						
						lstRegist = sesion.createQuery(sql).list();
						if(lstRegist!=null && lstRegist.size()>0)
							iResul = "El número de referencia de Depósito " +iNoreferdep+ " ya ha sido registrado en Arqueos - Caja";
						else
							iResul = null;
					}
				}				
			}else{
				iResul =  "No se ha podido verificar el número de referencia: ";
				iResul += "la cuenta de banco "+iCodbanco +" no está configurada" ;
			}
			
		} catch (Exception error) {
			iResul = "No se ha podido verificar Referencia de pago:\nError de Sistema=> " +error.getMessage();
			LogCajaService.CreateLog("verificarReferenciaPago", "ERR", error.getMessage());
		}
		return iResul;
	}
/****************************************************************************************/
/** Leer   Actualizar el monto mínimo de la caja. 
 *  Fecha: 23/06/2010
 *  Hecho: Carlos Manuel Hernández Morrison.
 */
	public boolean actualizarMontoMinimo(int iCaid, String sCodcomp, String sMoneda, double dMonto  ){
		boolean bHecho = true ;
		
		try {
			
			String sql = "  UPDATE "+PropertiesSystem.ESQUEMA+".F55CA013 SET C3MIAM =  " + Divisas.roundDouble( dMonto*100 ) +
					 " where c3id = " + iCaid + " and c3rp01 = '" + sCodcomp + "' and c3crcd = '"+sMoneda+"'" ;
			
			bHecho = ConsolidadoDepositosBcoCtrl.executeSqlQueryTx(null,  sql);
			 
		} catch (Exception error) {
			error.printStackTrace();
		} 
		return bHecho;
	}
/****************************************************************************************/
/** Leer los datos para la configuración de la caja, Unineg, Localizaciones, tipos docs.
 *  Fecha:  01/06/2010
 *  Hecho: Carlos Manuel Hernández Morrison.
 */	
	public List<Object> leerConfiguracionCaja(Vf55ca01 vfCaja){
		List<Object> lstDtsCaja = new ArrayList<Object>(3);
		F55ca017[] f55ca017 =  null,f55ca017i = null;
		CtrlCajas cajasCtrl = new CtrlCajas();
		String sLineas[] = null, sTiposDoc[], sCodSuc;
		boolean passed = true;
		
		try {
			sCodSuc = vfCaja.getId().getCaco().substring(3,5);
			
			//----  leer unidades de negocio activas e inactivas para la caja
			f55ca017 = cajasCtrl.obtenerUniNegCaja(vfCaja.getId().getCaid(), vfCaja.getId().getCaco());
			f55ca017i = cajasCtrl.obtenerUniNegCajaInactiva(vfCaja.getId().getCaid(), vfCaja.getId().getCaco());
			
			//---- obtener lineas correspondientes menos las inactivas
			if (f55ca017 != null && f55ca017i != null){
				sLineas = cajasCtrl.obtenerLineas(sCodSuc,f55ca017, f55ca017i);
			}else if(f55ca017 != null && f55ca017i == null){//no hay inactivas
				sLineas = cajasCtrl.obtenerLineas(sCodSuc,f55ca017);
			}else{// no hay unidades de negocio o sucursales configuradas en esta caja
				passed = false;
				lstDtsCaja = null;				
			}
			if (passed){
				//---- obtener tipos de documentos de todas las lineas encontradas
				sTiposDoc = cajasCtrl.obtenerTiposDeDocumento(sLineas);
				if (sTiposDoc != null){					
					
					//---- obtener localizaciones
					List<String> lstLocalizaciones = new ArrayList<String>();
					boolean bEmtpy = false;
					for (int a = 0 ; a < f55ca017.length; a++){
						if (!f55ca017[a].getId().getC7locn().trim().equals("")){
							lstLocalizaciones.add(f55ca017[a].getId().getC7locn().trim());	
						}else{
							if(!bEmtpy){
								lstLocalizaciones.add("");
								bEmtpy = true;
							}
						}
					}
					//--- Almacenar los registros a devolver.
					lstDtsCaja.add(0,sTiposDoc);
					lstDtsCaja.add(1,lstLocalizaciones);
					lstDtsCaja.add(2,f55ca017);
				}else{
					lstDtsCaja = null;
				}
			}else{
				lstDtsCaja = null;
			}
		} catch (Exception error) {
			lstDtsCaja = null;
			error.printStackTrace();
		}
		return lstDtsCaja;
	}
	
	

/****************************************************************************************/
/** Leer las facturas del día para una caja, incluyendo salidas y entradas por traslados
 *  Fecha:  28/05/2010
 *  Hecho: Carlos Manuel Hernández Morrison.
 */	
	public List leerFacturaDelDia(int iFechaActual,String[] sTipoDoc, F55ca017[] f55ca017,List lstLocalizaciones, int iCaid){
		List<Hfactjdecon> lstFacturas = new ArrayList<Hfactjdecon>();
		Session session = HibernateUtilPruebaCn.currentSession();
		
		StringBuilder sbSql1 = new StringBuilder();
		StringBuilder sbSql2 = new StringBuilder();
		StringBuilder sbExcluidas = new StringBuilder();
		
		List f55ca017_1 = new ArrayList(), f55ca017_2 = new ArrayList();
		boolean bHayLoc = false,bSoloLocs = false;
		try{
			//separar los que tienen localizaciones 
			for(int z = 0; z < f55ca017.length;z++){
				if(f55ca017[z].getId().getC7locn().trim().equals("")){
					f55ca017_1.add(f55ca017[z]);
					bSoloLocs = true;
				}else{
					bHayLoc = true;
					f55ca017_2.add(f55ca017[z]);
				}
			}
			sbSql1.append("select * from "+PropertiesSystem.ESQUEMA+".Hfactjdecon " +
					"as f where f.tipopago in ('00','01','001') and f.fecha =" + iFechaActual);
			sbSql1.append(" and trim(f.estado) = '' and f.tipofactura in (");
			
			//--- Tipos de documentos
			for (int i = 0; i < sTipoDoc.length; i++)
				sbSql1.append((i == sTipoDoc.length - 1)?"'" + sTipoDoc[i] + "')":"'" + sTipoDoc[i] + "',");
			
			sbSql2.append(sbSql1.toString());		
			
			//agregar unidades de negocio
			sbSql1.append(" and trim(f.sdlocn) = '' and trim(f.codunineg) in (");
			for(int i = 0; i < f55ca017_1.size(); i++){
				sbSql1.append((i == f55ca017_1.size() - 1)?
								"'" + ((F55ca017)f55ca017_1.get(i)) .getId().getC7mcul().trim() + "')":
								"'" + ((F55ca017)f55ca017_1.get(i)) .getId().getC7mcul().trim() + "',");
			}
			sbSql1.append(" and f.nofactura not in(select rf.numfac from "+PropertiesSystem.ESQUEMA+".Recibofac as rf");
			sbSql1.append(" where rf.numfac = f.nofactura and rf.codcomp = f.codcomp and rf.tipofactura = f.tipofactura");
			sbSql1.append(" and rf.codcli = f.codcli and rf.fecha = f.fecha ");
			sbSql1.append(" and rf.estado = '' and trim(rf.codunineg) = trim(f.codunineg) and trim(rf.partida)='')");
			
			//--- Excluir las facturas que fueron enviadas hacia otra caja. 
			sbExcluidas.append(" and f.nofactura not in( select tf.nofactura from "+PropertiesSystem.ESQUEMA+".trasladofac tf");
			sbExcluidas.append(" where tf.nofactura = f.nofactura and tf.codcomp = f.codcomp and tf.tipofactura = f.tipofactura");
			sbExcluidas.append(" and tf.codsuc = f.codsuc and trim(tf.codunineg)  = trim(f.codunineg)");
			sbExcluidas.append(" and tf.codcli = f.codcli and tf.fechafac = f.fecha");
			sbExcluidas.append(" and tf.estadotr in ('E','R') and tf.caidprop = "+iCaid + " )");
			sbSql1.append(sbExcluidas);
			
			//--- Incluir las facturas enviadas desde otra caja.
			sbSql1.append(" UNION ");
			sbSql1.append(" select f.* from "+PropertiesSystem.ESQUEMA+".Hfactjdecon as f inner join "+PropertiesSystem.ESQUEMA+".trasladofac tf"); 
			sbSql1.append(" on  tf.nofactura = f.nofactura and tf.codcomp = f.codcomp and tf.tipofactura = f.tipofactura");
			sbSql1.append(" and tf.codsuc = f.codsuc and trim(tf.codunineg) = trim(f.codunineg) and tf.estadotr in ('E','R')");
			sbSql1.append(" and tf.codcli = f.codcli and tf.fechafac = f.fecha ");
			sbSql1.append(" and tf.caiddest = "+iCaid+" and f.fecha = "+ iFechaActual +" and trim(f.estado) = '' ");
			
			//---- Localizaciones: Construir segunda consulta.
			if(bHayLoc){
				String sLocals=" and trim(f.sdlocn) in (";
				String sUnineg=" and trim(f.codunineg) in (";
				//--- Localizaciones y unidades de negocio.
				
				for(int i = 0; i < f55ca017_2.size(); i++){
					if(i == f55ca017_2.size() - 1){
						sLocals += "'" + ((F55ca017)f55ca017_2.get(i)).getId().getC7locn().trim()+"')";
						sUnineg += "'" + ((F55ca017)f55ca017_2.get(i)).getId().getC7mcul().trim()+"')";
					}else{
						sLocals += "'" + ((F55ca017)f55ca017_2.get(i)).getId().getC7locn().trim()+"',";
						sUnineg += "'" + ((F55ca017)f55ca017_2.get(i)).getId().getC7mcul().trim()+"',";
					}					
				}
				sbSql2.append(sLocals);
				sbSql2.append(sUnineg);
				sbSql2.append(" and f.nofactura not in(select rf.numfac from "+PropertiesSystem.ESQUEMA+".Recibofac as rf"); 
				sbSql2.append(" where rf.numfac = f.nofactura and rf.codcomp = f.codcomp and");
				sbSql2.append(" rf.codcli = f.codcli and rf.fecha = f.fecha and ");
				sbSql2.append(" rf.tipofactura = f.tipofactura and rf.estado = '' and trim(rf.codunineg) = trim(f.codunineg))");
				sbSql2.append(sbExcluidas);
				
				sbSql1.append(" UNION ");
				sbSql1.append(sbSql2);
			}
			
			if(bSoloLocs){
				lstFacturas = session.createSQLQuery(sbSql1.toString()).addEntity(Hfactjdecon.class).list();
			}else{
				sbSql2.append(" UNION ");
				sbSql2.append(" select f.* from "+PropertiesSystem.ESQUEMA+".Hfactjdecon as f inner join "+PropertiesSystem.ESQUEMA+".trasladofac tf"); 
				sbSql2.append(" on  tf.nofactura = f.nofactura and tf.codcomp = f.codcomp and tf.tipofactura = f.tipofactura");
				sbSql2.append(" and tf.codsuc = f.codsuc and trim(tf.codunineg)  = trim(f.codunineg) and tf.estadotr in ('E','R')");
				sbSql2.append(" and tf.codcli = f.codcli and tf.fechafac = f.fecha ");
				sbSql2.append(" and tf.caiddest = "+iCaid+" and f.fecha = "+ iFechaActual +" and trim(f.estado) = '' ");
				lstFacturas = session.createSQLQuery(sbSql2.toString()).addEntity(Hfactjdecon.class).list();
			}
			
			
			if(lstFacturas != null && lstFacturas.size() > 0 ){
				CollectionUtils.filter(lstFacturas, new Predicate() {
					public boolean evaluate(Object arg0) {
						Hfactjdecon hfac = (Hfactjdecon)arg0;
						
						return ( hfac.getId().getSubtotal() > 0 && 
								 hfac.getId().getTotal() > 0 );
					}
				});
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	return lstFacturas;
}
/********************************************************************************/
/** Obtiene una lista de Cajas Vf55ca01 a partir de una consulta externa.
 *  Fecha:  25/05/2010
 *  Hecho: Carlos Manuel Hernández Morrison.
 */	
	public List obtenerListaCajas(String sql,int iMaxResult){
		List lstCajas = null;		
		Session sesion = HibernateUtilPruebaCn.currentSession();
		try{
			
			if(iMaxResult>0)			
				lstCajas = sesion.createQuery(sql).setMaxResults(iMaxResult).list();
			else
				lstCajas = sesion.createQuery(sql).list();			
			
		}catch(Exception error){
			error.printStackTrace();
		}
		return lstCajas;
	}
/********************************************************************************/
/** Verifica que el número de referencia de depósito no exista en la f0911
 *  Fecha:  12/05/2010
 *  Hecho: Carlos Manuel Hernández Morrison.
 */
	public int verificarReferdeposito911(int iNoreferdep,String sTipodoc, int iCodbanco,String sCodcomp,String sMoneda){
		List lstF0911 = null;		
		Session sesion = HibernateUtilPruebaCn.currentSession();		
		String sql = "", sCuenta[];
		Divisas dv = new Divisas();
		int iResul=0;
		
		try {
			
						
			/**
			 * La cuenta de banco se lee para obtener la sucursal de la cuenta del banco que
			 * será utilizada en los registros de los asientos de diario..
			 */
			
			sCuenta = dv.obtenerCuentaBanco(sCodcomp, sMoneda,iCodbanco, sesion, null, sesion, null);
			if(sCuenta!=null){
				sql =  " SELECT * FROM "+PropertiesSystem.JDEDTA+".F0911 WHERE GLDOC = "+ iNoreferdep+ " and gldct = '"+sTipodoc+"'";
				sql += " AND GLKCO = '000"+sCuenta[2]+"'";
				
				lstF0911 = sesion.createSQLQuery(sql).list();
				if(lstF0911!=null && lstF0911.size()>0)
					iResul = 1;
				else
					iResul =0;
			}else{
				iResul = -1;
			}
			
			
		} catch (Exception error) {
			iResul = -1;
			error.printStackTrace();
		}
		return iResul;
	}
	
	
/*********************************************************************************/
/**	 Obtiene el monto y dias permitidos para procesar devolución en una caja	**/
	public List obtenerF55ca028(int iCaid, String sCodcomp, String sMoneda){
		List lstDatosDev = null;
		String sql = "";		
		Session sesion = HibernateUtilPruebaCn.currentSession();
		
		try {
			sql =  " SELECT CAST(F.D4DPER AS INTEGER) D4DPER, CAST(F.D4MPER AS DECIMAL(10,2)) D4MPER";
			sql += " FROM "+PropertiesSystem.ESQUEMA+".F55CA028 F";
			sql += " WHERE F.D4ID = "+iCaid+" AND F.D4RP01 = '"+sCodcomp+"' AND F.D4CRCD = '"+sMoneda+"'";
			
			
			lstDatosDev = sesion.createSQLQuery(sql).list();
			
			
		
		} catch (Exception error) {
			lstDatosDev = null;
			error.printStackTrace();
		}
		return lstDatosDev;
	}	
/*********************************************************************************/
/**	 		Obtiene los datos de una caja a partir de su código únicamente		**/
	public Vf55ca01 obtenerDatosCaja(int iCaid){
		Vf55ca01 v = null;
		
		Session sesion = HibernateUtilPruebaCn.currentSession();		
		try {
		
			Object ob = sesion.createQuery("from Vf55ca01 v where v.id.caid = "+iCaid).uniqueResult();
		
			if(ob!=null)
				v = (Vf55ca01)ob;
		} catch (Exception error) {
			v = null;
			error.printStackTrace();
		} 
		return v;
	}

/*****************************************************************************************************/
/**	 		Obtiene los datos para generar el reporte de emisión de recibos rptmcaja006				**/
	public List<Vrptmcaja006Id> obtieneRecibosrpt006(int iCaid,String sCodcomp,String sMoneda,Date dtIni,Date dtFin){ 
		List<Vrptmcaja006> lstRecibos = null;
		final List<Vrptmcaja006Id> lstRept =  new ArrayList<Vrptmcaja006Id>();;
		
		try {
			
			String sql = " select * from @BDCAJA.Vrptmcaja006 where caid = @CAID and codcomp = '@CODCOMP' and moneda = '@MONEDA' and estado = '' ";
			
			if(dtIni != null){
				sql += " and fecha >= '"+FechasUtil.formatDatetoString(dtIni, "yyyy-MM-dd") +"'" ;
			}  
			if(dtFin != null){
				sql += " and fecha <= '"+FechasUtil.formatDatetoString(dtFin, "yyyy-MM-dd") +"'" ;
			}  
			
			sql += " order by caid, fecha, numrec " ;
			
			sql = sql
				.replace("@BDCAJA", PropertiesSystem.ESQUEMA)
				.replace("@CAID", String.valueOf(iCaid) )
				.replace("@CODCOMP", sCodcomp)
				.replace("@MONEDA", sMoneda) ;
			 
			lstRecibos = ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, Vrptmcaja006.class, true);
			
			if(lstRecibos == null || lstRecibos.isEmpty() )
				return new ArrayList<Vrptmcaja006Id>() ;
			
			CollectionUtils.forAllDo(lstRecibos, new Closure(){
				 
				public void execute(Object o) {
					Vrptmcaja006 v  = (Vrptmcaja006)o;
					Vrptmcaja006Id vid = v.getId();
					vid.setFecharecibo(FechasUtil.formatDatetoString(vid.getFecha(), "dd/MM/yyyy"));
					vid.setDmontoapl(vid.getMontoapl().doubleValue());
					vid.setCliente(v.getId().getCodcli()+" " + vid.getCliente());
					vid.setTasacambio(vid.getTasa().doubleValue());
					vid.setDmontoapl(vid.getMontoapl().doubleValue());
					vid.setDmontoneto(vid.getMontoneto().doubleValue());
					vid.setDmonto(vid.getMonto().doubleValue());
					vid.setMpago( MetodosPagoCtrl.descripcionMetodoPago( vid.getMpago() ) );
					lstRept.add(vid);
				}
			});
			
			 
		} catch (Exception error) {
			error.printStackTrace();
		} 
		return lstRept;
	}
/*****************************************************************************************************/
/**	 		Obtiene los datos para generar el reporte de emisión de recibos rptmcaja005				**/
	public List obtieneRecibosrpt005(int iCaid, String sCodcomp, String sMoneda,Date dtIni,Date dtFin){
		List<Vrptmcaja005> lstRecibos = null;
		String sql = "",sFechaIni="",sFechaFin="";

		List<Vrptmcaja005Id> lstRecibosRpt = null;
		
		try {
			sFechaIni = FechasUtil.formatDatetoString(dtIni, "yyyy-MM-dd");
			sFechaFin = FechasUtil.formatDatetoString(dtFin, "yyyy-MM-dd");
			
			sql =  " from Vrptmcaja005 v where v.id.caid = "+iCaid + " and v.id.codcomp = '"+sCodcomp+"'";
			sql += " and v.id.moneda = '"+sMoneda+"' and v.id.glcrcd = '"+sMoneda+"'";
			sql += " and v.id.fecha between '"+sFechaIni+"' and '"+sFechaFin+"'";
			
			 
			lstRecibos = ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, Vrptmcaja005.class, false);
			 
			
			//------ Definir los registros de cuales son créditos y cuáles débitos.
			if(lstRecibos!=null && lstRecibos.size()>0){
				
				
				CollectionUtils.filter(lstRecibos, new Predicate(){
					@Override
					public boolean evaluate(Object o) {
						Vrptmcaja005 v = (Vrptmcaja005)o;
						 return v.getId().getGlaa().compareTo(BigDecimal.ZERO) == 1 ;
					}
				}) ;
				
				CollectionUtils.filter(lstRecibos, new Predicate(){
					@Override
					public boolean evaluate(Object o) {
						Vrptmcaja005 v = (Vrptmcaja005)o;
						 return v.getId().getGldct().compareTo("AE") != 0 ;
					}
				}) ;
				
				Set<Vrptmcaja005> recibosFiltrados = new HashSet<Vrptmcaja005>(lstRecibos) ;
				
				
				lstRecibosRpt = new  ArrayList<Vrptmcaja005Id>();
				
				for (Vrptmcaja005 v : recibosFiltrados) {
				 
					Vrptmcaja005Id vid = v.getId();
					
					String sFecharec = FechasUtil.formatDatetoString(vid.getFecha(), "dd-MM-yyyy");
					vid.setFecharecibo(sFecharec);
					
					if(vid.getGlaa().doubleValue()>0){
						vid.setCuentadeb(v.getId().getGlani());
						vid.setCuentacre("--");
						vid.setMontodeb(v.getId().getGlaa().doubleValue());
						vid.setMontocre(0.00);
					}
					else if(vid.getGlaa().doubleValue()<0){
						vid.setCuentacre(v.getId().getGlani());
						vid.setCuentadeb("--");
						vid.setMontocre(v.getId().getGlaa().doubleValue());
						vid.setMontodeb(0.00);
					}
					lstRecibosRpt.add(vid);					
				}
				 
			}
		} catch (Exception error) {
			lstRecibos = null;
			error.printStackTrace();
		} 
		return lstRecibosRpt;
	}
/*******************************************************************************/
/** 	 Obtener información de la caja, desde el F55CA020        **************/	
	public static F55ca020 obtenerInfoCaja(String sCodsuc, String sCodcomp){
		F55ca020 f = null;
		
		try {
			
			String sql  = "from F55ca020 f where f.id.codcomp = '"+sCodcomp+"' and f.id.codsuc = '"+sCodsuc+"'";
		 
			f = ConsolidadoDepositosBcoCtrl.executeSqlQueryUnique(sql, F55ca020.class, false);
			
		} catch (Exception error) {
			error.printStackTrace();
		} 
		 
		return f;		
	}	
/****************************************************************************************************/
/** 	Obtiene la lista fechas sin duplicados que tienen transacciones
 **************/	
	public List obtenerFechasTransaciones(int iCaid,String sCodcomp,Date dtFechaini,Date dtFechafin){
		List lstFechas = new ArrayList();
		Session sesion = HibernateUtilPruebaCn.currentSession();
		
		String sConsulta,sFechaini,sFechafin;
		
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			sFechaini = format.format(dtFechaini);
			sFechafin = format.format(dtFechafin);
			
			sConsulta = "select distinct(v.id.fecharec) from Vtransaccionesjde v";
			sConsulta += " where v.id.caid = "+iCaid+" and v.id.codcomp= '"+sCodcomp+"'";
			sConsulta += " and v.id.fecharec between '"+sFechaini+"' and '"+sFechafin+"'";
			sConsulta += " and v.id.nobatch >0 and v.id.nodocumento >0 and v.id.monto > 0";
			sConsulta += " and v.id.restado <>'A' order by v.id.fecharec";
			
		
			lstFechas = sesion.createQuery(sConsulta).list();
		
			
		} catch (Exception error) {
			error.printStackTrace();
		}		
		return lstFechas;
	}
	
	
/****************************************************************************************************/
/** 	Obtiene la lista de transacciones JDE para la caja y compañía especificada.
 **************/
	
	public List<Vtransaccionesjde> obtenerTransaccionesJDE(int iCaid,String sCodcomp,Date dtFechaini,Date dtFechafin){
		List<Vtransaccionesjde> lstTrans = null;
		String sConsulta,sFechaini,sFechafin;
		
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			sFechaini = format.format(dtFechaini);
			sFechafin = format.format(dtFechafin);
			
			sConsulta = " from Vtransaccionesjde v where v.id.caid = "+iCaid+" and v.id.codcomp= '"+sCodcomp+"'";
			sConsulta += " and v.id.fecharec between '"+sFechaini+"' and '"+sFechafin+"'";
			sConsulta += " and v.id.nobatch >0 and v.id.nodocumento >0 and v.id.monto > 0";
			sConsulta += " and v.id.restado <>'A' order by v.id.nobatch,v.id.tiporec";
			
			lstTrans = ConsolidadoDepositosBcoCtrl.executeSqlQuery(sConsulta, Vtransaccionesjde.class, false);
			
		} catch (Exception error) {
			error.printStackTrace();
		} finally {
		}
		return lstTrans;
	}
	
	
/****************************************************************************************************/
/** 	Obtiene la lista de compañías asignadas al contador.
 **************/
	public static List<Object[]> cargarCompaniaxContador(int iCodContador,int iCaid){
		String consulta = "";
		List<Object[]> lstMonedas = new ArrayList<Object[]>();
		
		try{
			consulta =  " select distinct(co.id.c4rp01d1),co.id.c4rp01";
			consulta += " from F55ca01 c, F55ca014 co where co.id.c4id = c.id.caid and c.id.cacont = "+iCodContador;
			if(iCaid != 0)
				consulta += " and co.id.c4id = "+iCaid;

			lstMonedas = ConsolidadoDepositosBcoCtrl.executeSqlQuery(consulta, null, false) ; 
			
		}catch(Exception error){
			error.printStackTrace();
		}finally{
		}
		return lstMonedas;
	}	
/****************************************************************************************************/
/** 	Obtiene la lista de cajas que tiene asignadas el contador
 **************/
	public List obtenerCajasxContadorF55(int iCodContador){
		List<F55ca01> lstCajas = new ArrayList<F55ca01>();
		Session session = HibernateUtilPruebaCn.currentSession();
		
		try{
		
			String sql = "from F55ca01 as f where f.id.cacont = "+ iCodContador +" and f.id.castat = 'A'";
			lstCajas = (ArrayList<F55ca01>)session.createQuery(sql).list();
			
		}catch(Exception ex){

			ex.printStackTrace();
		}
		return lstCajas;
	}
	/* ******************************************************************* */
	public static List<Vf55ca01> obtenerCajasxContador(int iCodContador){
		List<Vf55ca01> lstCajas = null;
		
		try{
			
			String sql = "from Vf55ca01 as f where f.id.cacont =  " + iCodContador + " and f.id.castat = 'A'  order by f.id.caid";
			lstCajas = ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, Vf55ca01.class, false);
			
			
		}catch(Exception ex){
			LogCajaService.CreateLog("obtenerCajasxContador", "ERR", ex.getMessage());
		} 
		
		return lstCajas;
	}
	public List obtenerCajasxContador1(int iCodContador){
		List lstCajas = new ArrayList();
		Session session = HibernateUtilPruebaCn.currentSession();		
		try{		
			String sql= "select f.id.caid, f.id.caname from F55ca01 as f where f.id.cacont = "+ iCodContador +" and f.id.castat = 'A'";			
			lstCajas = session.createQuery(sql).list();			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return lstCajas;
	}
/*********************************************************************************************/	
	//Carga las definiciones de la caja
	public void getCajas() {
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		String cajero = (String) m.get("NombreCajero");
				
        Session session =  HibernateUtilPruebaCn.currentSession();
        
		try {
		
			List result = null;
			
	        Query query = session
				.createQuery("from F55ca01 where causer = :cCod")
				.setParameter("cCod", "AZAPATA");
	        result = query.list();	        
	        
	        if(result != null) {
	        	cajas = new F55ca01[result.size()]; 
	        	for(int i=0; i<result.size(); i++) {
	        		cajas[i] = (F55ca01) result.get(i);
	        		m.put("NumeroCaja", cajas[i].getId().getCaid());
	        	}
	        	List lstCajas = new ArrayList();
	        	lstCajas.add(cajas);
	        	m.put("lstCajas", lstCajas);
	        }
	        
		}catch(Exception ex){
			ex.printStackTrace();
			
		} 
		
	}
/************************************************************************************************/

	public void getRecibosCaja() {	
		
		
        Session session = HibernateUtilPruebaCn.currentSession();
        
		try {
		
			List result = null;
			
	        Query query = session
				.createQuery("from F55ca014 where c4id = :cCaja and c4rp01 = :cCom")
				.setParameter("cCaja", "1")
	        .setParameter("cCom", "E01");
	        result = query.list();	        
	        
	        if(result != null) {
	        	cajascom = new F55ca014[result.size()]; 
	        	for(int i=0; i<result.size(); i++) {
	        		cajascom[i] = (F55ca014) result.get(i);
	        	}
	        }
	        
		}catch(Exception ex){
			ex.printStackTrace();
			
		} finally {			
			session.close();
		}
		
	}
	/**********************OBTENER TODAS LAS CAJAS CONFIGURADAS PARA fdc*******************************/
	public List getCajasFDC(){
		List lstCajas = new ArrayList();
		Session session = HibernateUtilPruebaCn.currentSession();
		
		try{
			
			String sql = "from Vf55ca01 as f where f.id.castat = 'A' and trim(f.id.caprnt) = 'FDC' order by f.id.caid";
			lstCajas = session
			.createQuery(sql)			
			.list();
				
		}catch(Exception ex){
			ex.printStackTrace();
		}finally {			
			session.close();
		}
		return lstCajas;
	}
/**********************OBTENER TODAS LAS CAJAS CONFIGURADAS PARA DOLLAR*******************************/
	public List getCajasDollar(){
		List lstCajas = new ArrayList();
		Session session = HibernateUtilPruebaCn.currentSession();
		
		try{
		
			String sql = "from Vf55ca01 as f where f.id.castat = 'A' and trim(f.id.caprnt) = 'DOLLAR'  order by f.id.caid";
			lstCajas = session
			.createQuery(sql)			
			.list();
			
		}catch(Exception ex){
			ex.printStackTrace();
		}finally {			
			try{session.close();}catch(Exception ex2){};
		}
		return lstCajas;
	}
/**********************OBTENER TODAS LAS CAJAS CONFIGURADAS********************************/
	public List<Vf55ca01> getAllCajas(){
		List<Vf55ca01> lstCajas =  null;

		try{
			
			String sql = "from Vf55ca01 as f where f.id.castat = 'A' order by f.id.caid";
			
			return lstCajas = ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, Vf55ca01.class, false);
			 
		}catch(Exception ex){
			LogCajaService.CreateLog("getAllCajas", "ERR", ex.getMessage());
		} 
		return lstCajas;
	}
/**************************************************************************************/
/**********************OBTENER CAJA POR LOGIN DE USUARIO*******************************/
	public List obtenerCaja(int sLogin){
		List lstCajas = new ArrayList();
		Session session = HibernateUtilPruebaCn.currentSession();		
		try{
			
			String sql = "from Vf55ca01 as f where f.id.cacati = "+sLogin+ " and f.id.castat = 'A'  order by f.id.caid";
			lstCajas = session
			.createQuery(sql)			
			.list();
				
		}catch(Exception ex){
			LogCajaService.CreateLog("obtenerCaja", "ERR", ex.getMessage());
		}

		return lstCajas;
	}
/**************************************************************************************/
/**********************OBTENER CAJA POR ID DE CAJA*******************************/
	@SuppressWarnings("rawtypes")
	public List obtenerCajaxXCodigo(int iCaid,String sCaco){
		List lstCajas = new ArrayList();
		Session session = null;
		
		try{
			session =  HibernateUtilPruebaCn.currentSession();		
			
			String sql = "from Vf55ca01 as f where f.id.caid = "+iCaid+ " and f.id.caco = '"+sCaco+"' and f.id.castat = 'A'";
			lstCajas = session.createQuery(sql).list();
			
		}catch(Exception ex){
			LogCajaService.CreateLog("obtenerCajaxXCodigo", "ERR", ex.getMessage());
		}

		return lstCajas;
	}
/**************************************************************************************/
/***************LEER UNIDADES DE NEGOCIO CONFIGURADAS DEL F55CA017 activas******************************/
	public F55ca017[] obtenerUniNegCaja(int iCaid,String sCodComp){	
		List lstCajas = new ArrayList();
		F55ca017[] f55ca017 = null;
		Session session = HibernateUtilPruebaCn.currentSession();
		
		try{
		
			String sql = "from F55ca017 as f where f.id.c7id = "+iCaid+ " and f.id.c7co = '"+sCodComp+"' and f.id.c7stat = 'A'";
			
			LogCajaService.CreateLog("obtenerUniNegCaja", "QRY", sql);
			
			lstCajas = session
			.createQuery(sql)			
			.list();
		
			if (lstCajas != null && !lstCajas.isEmpty()){
				f55ca017 = new F55ca017[lstCajas.size()];
				for (int i = 0; i < lstCajas.size(); i++){
					f55ca017[i] = (F55ca017)lstCajas.get(i);
				}
			}
		}catch(Exception ex){
			f55ca017 = null;
			LogCajaService.CreateLog("obtenerUniNegCaja", "ERR", ex.getMessage());
		}

		return f55ca017;
	}
/*********************************************************************************************/
/***************LEER UNIDADES DE NEGOCIO CONFIGURADAS DEL F55CA017 activas******************************/
	@SuppressWarnings("rawtypes")
	public F55ca017[] obtenerUniNegCajaInactiva(int iCaid,String sCodComp){	
		List lstCajas = new ArrayList();
		F55ca017[] f55ca017 = null;
		Session session = HibernateUtilPruebaCn.currentSession();
		
		try{
			
			String sql = "from F55ca017 as f where f.id.c7id = "+iCaid+ " and f.id.c7co = '"+sCodComp+"' and f.id.c7stat <> 'A'";
			lstCajas = session
			.createQuery(sql)			
			.list();
			
			if (lstCajas != null && !lstCajas.isEmpty()){
				f55ca017 = new F55ca017[lstCajas.size()];
				for (int i = 0; i < lstCajas.size(); i++){
					f55ca017[i] = (F55ca017)lstCajas.get(i);
				}
			}
		}catch(Exception ex){
			LogCajaService.CreateLog("obtenerUniNegCajaInactiva", "ERR", ex.getMessage());
		}

		return f55ca017;
	}
/*********************************************************************************************/
/**************************BUSCAR LAS LINEAS*************************************************/
	public String[] obtenerLineas(String sCodComp,F55ca017[] f55ca017,F55ca017[] f55ca017i){
		Session session = HibernateUtilPruebaCn.currentSession();	
		String[] sLinea = null;
		@SuppressWarnings("rawtypes")
		List lstLineas = new ArrayList();
		
		try{
			
			String sql = "select distinct u.id.drky from Uneglinea as u where trim(u.id.mcmcu) like '"+sCodComp+"%' and trim(u.id.mcmcu) not in ('"+sCodComp+"',";
			for (int i = 0; i < f55ca017i.length; i++){
				if (i == f55ca017i.length - 1){
					sql = sql + "'" + f55ca017i[i].getId().getC7mcul().trim() + "'";
				}else{
					sql = sql + "'" + f55ca017i[i].getId().getC7mcul().trim() + "',";
				}
			}
			sql = sql + ")";
			//se incluyen en la lista
			if (f55ca017.length > 1){
				sql = sql + " or trim(u.id.mcmcu) in (";
				for (int h = 0; h < f55ca017.length; h++){
					if(f55ca017[h].getId().getC7mcul().trim().length() > 2){//si es la unidad de negocio
						if (h == f55ca017.length - 1){
							sql = sql + "'" + f55ca017[h].getId().getC7mcul().trim() + "'";
						}else{
							sql = sql + "'" + f55ca017[h].getId().getC7mcul().trim() + "',";
						}
					}
				}
				sql = sql + ")";
			}
			lstLineas = session.createQuery(sql).list();
			
			
			if (lstLineas != null && !lstLineas.isEmpty()){
				sLinea = new String[lstLineas.size()];
				for (int j = 0; j < lstLineas.size(); j++){
					sLinea[j] = (String)lstLineas.get(j);
				}
			}
		}catch(Exception ex){
			LogCajaService.CreateLog("obtenerLineas", "ERR", ex.getMessage());
		}
		
		return sLinea;
	}
/***********************************************************************************/
	//buscar lineas con activas
	public String[] obtenerLineas(String sCodComp,F55ca017[] f55ca017){
		Session session = HibernateUtilPruebaCn.currentSession();		
		String[] sLinea = null;
		List lstLineas = new ArrayList();
		boolean bHayUnidad = false;		
		
		try{
			
			String sql = "select distinct u.id.drky from Uneglinea as u where trim(u.id.mcmcu) not in ('"+sCodComp+"')";
			for (int u = 0; u < f55ca017.length; u++){
				if(f55ca017[u].getId().getC7mcul().trim().length() > 2){
					bHayUnidad = true;
					break;
				}
			}
			if(bHayUnidad){
				//se incluyen en la lista
				sql = sql + " and trim(u.id.mcmcu) in (";
				for (int h = 0; h < f55ca017.length; h++){
					if(f55ca017[h].getId().getC7mcul().trim().length() > 2){//si es la unidad de negocio
						if (h == f55ca017.length - 1){
							sql = sql + "'" + f55ca017[h].getId().getC7mcul().trim() + "'";
						}else{
							sql = sql + "'" + f55ca017[h].getId().getC7mcul().trim() + "',";
						}
					}
				}
				sql = sql + ")";
			}
			lstLineas = session.createQuery(sql).list();
			
			
			if (lstLineas != null && !lstLineas.isEmpty()){
				sLinea = new String[lstLineas.size()];
				for (int j = 0; j < lstLineas.size(); j++){
					sLinea[j] = (String)lstLineas.get(j);
				}
			}
		}catch(Exception ex){
			LogCajaService.CreateLog("obtenerLineas", "ERR", ex.getMessage());
		}
		
		return sLinea;
	}
/*********************************************************************************************/
/************LEER TIPOS DE DOCUMENTOS DE LINEAS***********************************************/
	public String[] obtenerTiposDeDocumento(String[] sLineas){
		String[] sTiposDoc = null;
		Session session = HibernateUtilPruebaCn.currentSession();
		
		List lstDocs = new ArrayList();
		try{
		
			String sql = "select distinct f.id.cadoc from F55ca019 as f where f.id.castat = 'A' and trim(f.id.camcul) in (";
			for (int i = 0; i < sLineas.length; i++){
				if (i == sLineas.length - 1){
					sql = sql + "'" + sLineas[i] + "'";
				}else{
					sql = sql + "'" + sLineas[i] + "',";
				}
			}
			sql = sql + ")";
			
			
			lstDocs = session
			.createQuery(sql)			
			.list();
		
			if(lstDocs != null && !lstDocs.isEmpty()){
				sTiposDoc = new String[lstDocs.size()];
				for (int j = 0; j < lstDocs.size(); j++){
					sTiposDoc[j] = ((String)lstDocs.get(j)).trim();
				}
			}
		}catch(Exception ex){
			LogCajaService.CreateLog("obtenerTiposDeDocumento", "ERR", ex.getMessage());
		}

		return sTiposDoc;
	}

/*********************************************************************************************/
/************LEER FACTURAS DEL DIA CON DETERMINADOS TIPOS DE DOCUMENTOS y suscursal**************************************/
	public List leerFacturasDelDia(int iFechaActual,String[] sTipoDoc, F55ca017[] f55ca017,List lstLocalizaciones, int iCaid){
		List lstFacturas = new ArrayList();
		Session session = HibernateUtilPruebaCn.currentSession();
		
		String sLoc = "";
		String sql = "", sql2 = "";
		List f55ca017_1 = new ArrayList(), f55ca017_2 = new ArrayList();
		boolean bHayLoc = false,bSoloLocs = false;
		try{
			//separar los que tienen localizaciones 
			for(int z = 0; z < f55ca017.length;z++){
				if(f55ca017[z].getId().getC7locn().trim().equals("")){
					f55ca017_1.add(f55ca017[z]);
					bSoloLocs = true;
				}else{
					bHayLoc = true;
					f55ca017_2.add(f55ca017[z]);
				}
			}			
			
		
			sql = "select * from "+PropertiesSystem.ESQUEMA+".Hfactjdecon as f where f.fecha =" + iFechaActual +" and trim(f.estado)<>'A' and f.tipofactura in (";
			//agregar tipos de documentos
			for (int i = 0; i < sTipoDoc.length; i++){
				if (i == sTipoDoc.length - 1){
					sql = sql + "'" + sTipoDoc[i] + "'";
				}else{
					sql = sql + "'" + sTipoDoc[i] + "',";
				}
			}
			sql = sql + ")";
			sql2 = sql;		
			
			//agregar unidades de negocio
			sql = sql + " and trim(f.sdlocn) = '' and trim(f.codunineg) in (";
			for(int a = 0; a < f55ca017_1.size(); a++){
				if (a == f55ca017_1.size() - 1){
					sql = sql + "'" + ((F55ca017)f55ca017_1.get(a)) .getId().getC7mcul().trim() + "'";
				}else{
					sql = sql + "'" + ((F55ca017)f55ca017_1.get(a)) .getId().getC7mcul().trim() + "',";
				}
			}
			sql = sql + ") and f.nofactura not in(select rf.numfac from "+PropertiesSystem.ESQUEMA+".Recibofac as rf where rf.numfac = f.nofactura and rf.codcomp = f.codcomp and rf.tipofactura = f.tipofactura and rf.estado <> 'A' and trim(rf.codunineg) = trim(f.codunineg)) ";
			
			if(bHayLoc){
				sql2 = sql2 + " and (trim(f.sdlocn) in (";
				for(int j = 0; j < f55ca017_2.size(); j++){
					sLoc = ((F55ca017)f55ca017_2.get(j)).getId().getC7locn();
					if (j == f55ca017_2.size() - 1){
						sql2 = sql2 + "'" + sLoc.trim()+ "'";
					}else{
						sql2 = sql2 + "'" + sLoc.trim() + "',";
					}
				}
				sql2 = sql2 + ") and trim(f.codunineg) in (";
				for(int a = 0; a < f55ca017_2.size(); a++){
					if (a == f55ca017_2.size() - 1){
						sql2 = sql2 + "'" + ((F55ca017)f55ca017_2.get(a)).getId().getC7mcul().trim() + "'";
					}else{
						sql2 = sql2 + "'" + ((F55ca017)f55ca017_2.get(a)).getId().getC7mcul().trim() + "',";
					}
				}
				sql2 = sql2 + ")) and f.nofactura not in(select rf.numfac from "+PropertiesSystem.ESQUEMA+".Recibofac as rf where rf.numfac = f.nofactura and rf.codcomp = f.codcomp and rf.tipofactura = f.tipofactura and rf.estado <> 'A' and trim(rf.codunineg) = trim(f.codunineg)) ";		
				sql = sql + " union " + sql2;
			}
			if(bSoloLocs){
				lstFacturas = session.createSQLQuery(sql).addEntity(Hfactjdecon.class).list();
			}else{
				lstFacturas = session.createSQLQuery(sql2).addEntity(Hfactjdecon.class).list();
			}
		
		}catch(Exception ex){
			LogCajaService.CreateLog("leerFacturasDelDia", "ERR", ex.getMessage());
		}

	return lstFacturas;
}
/*********************************************************************************************/
/**LEER FACTURAS DEL DIA CON DETERMINADOS TIPOS DE DOCUMENTOS Y EL NUMERO DE FACTURA PARECIDO*********************/
	public List leerFacturasDelDiaxNumero(int iFechaActual,String[] sTipoDoc, String sNumeroFac, String sCodSuc){
		List lstFacturas = new ArrayList();
		Session session = HibernateUtilPruebaCn.currentSession();
		
		try{
		
			String sql = "from Hfactjdecon as f where f.id.fecha =" + iFechaActual +" and cast(f.id.nofactura as string) like '"+sNumeroFac+"%' and f.id.subtotal > 0 and f.id.tipofactura in (";
			for (int i = 0; i < sTipoDoc.length; i++){
				if (i == sTipoDoc.length - 1){
					sql = sql + "'" + sTipoDoc[i] + "'";
				}else{
					sql = sql + "'" + sTipoDoc[i] + "',";
				}
			}
			sql = sql + ") and f.id.codsuc = '"+sCodSuc+ "' and trim(f.id.estado) = '' and f.id.nofactura not in(select rf.id.numfac from Recibofac as rf where rf.id.numfac = f.id.nofactura and rf.id.codcomp = f.id.codcomp and rf.id.tipofactura = f.id.tipofactura and trim(rf.codunineg) = trim(f.codunineg)) order by f.id.fecha desc, f.id.hora desc ";
			lstFacturas = session
			.createQuery(sql)			
			.list();
		
		}catch(Exception ex){
			LogCajaService.CreateLog("leerFacturasDelDiaxNumero", "ERR", ex.getMessage());
		}

	return lstFacturas;
}
/*********************************************************************************************/
	//Lista nombre del cajero
	public String getLblCajero() {

		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		lblCajero = (String)m.get("sNombreEmpleado");			
		
		return lblCajero;
	}
	public void setLblCajero(String lblCajero) {
		this.lblCajero = lblCajero;
	}
	
	
	//--------------------------------------------------------------------------------
	
	
	//Lista Numero de la caja
	public String getLblNoCaja() {
		List lstCajas = null;
		Vf55ca01 vf55ca01 = null;
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		try{
			lstCajas = (List)m.get("lstCajas");
			vf55ca01 = (Vf55ca01)lstCajas.get(0);
			lblNoCaja = vf55ca01.getId().getCaid() + " " + vf55ca01.getId().getCaname();
		}catch(Exception ex){
			LogCajaService.CreateLog("getLblNoCaja", "ERR", ex.getMessage());
		}		
		return lblNoCaja;
	}	
	public void setLblNoCaja(String lblNoCaja) {
		this.lblNoCaja = lblNoCaja;
	}
	public String getAmbienteDeSistemaCaja() {
		return ambienteDeSistemaCaja = PropertiesSystem.ESQUEMA;
	}
	public void setAmbienteDeSistemaCaja(String ambienteDeSistemaCaja) {
		this.ambienteDeSistemaCaja = ambienteDeSistemaCaja;
	}
	public String getAmbienteDeSistemaJdeDTA() {
		return ambienteDeSistemaJdeDTA = PropertiesSystem.JDEDTA;
	}
	public void setAmbienteDeSistemaJdeDTA(String ambienteDeSistemaJdeDTA) {
		this.ambienteDeSistemaJdeDTA = ambienteDeSistemaJdeDTA;
	}
	public String getAmbienteDeSistemaJdeCOM() {
		return ambienteDeSistemaJdeCOM = PropertiesSystem.JDECOM;
	}
	public void setAmbienteDeSistemaJdeCOM(String ambienteDeSistemaJdeCOM) {
		this.ambienteDeSistemaJdeCOM = ambienteDeSistemaJdeCOM;
	}

	public String getCopyrightSistemaCaja() {
		return copyrightSistemaCaja = " Todos los derechos reservados | Casa Pellas @ "+ new SimpleDateFormat("yyyy").format(new Date())+ "  | ";
	}

	public void setCopyrightSistemaCaja(String copyrightSistemaCaja) {
		this.copyrightSistemaCaja = copyrightSistemaCaja;
	}

	
	//--------------------------------------------------------------------------------
	
	

	//Lista nombre del cajero
	public String getLblSucursal() {
		
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		lblSucursal = (String)m.get("sNombreSucursal");
		
		return lblSucursal;
	}	
	
	
	public void setLblSucursal(String lblSucursal) {
		this.lblSucursal = lblSucursal;
	}
	
	
	//--------------------------------------------------------------------------------	
	
	
	//Listar Numero de Recibo
	public String getLblNumeroRecibo() {
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		if(cajascom == null) {
			getRecibosCaja();
		}
		Long reciboactual = cajascom[0].getId().getC4nncu();
		m.put("ReciboActual", reciboactual);
		lblNumeroRecibo = String.valueOf(reciboactual); 
			
		return lblNumeroRecibo;
	}
	
	public void setLblNumeroRecibo(String lblNumeroRecibo) {
		this.lblNumeroRecibo = lblNumeroRecibo;
	}


}
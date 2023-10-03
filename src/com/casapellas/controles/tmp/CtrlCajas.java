package com.casapellas.controles.tmp;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
// import org.apache.commons.mail.MultiPartEmail;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.casapellas.controles.ConsolidadoDepositosBcoCtrl;
import com.casapellas.entidades.Arqueo;
import com.casapellas.entidades.F55ca01;
import com.casapellas.entidades.F55ca014;
import com.casapellas.entidades.F55ca017;
import com.casapellas.entidades.F55ca020;
import com.casapellas.entidades.Hfactjdecon;

import com.casapellas.entidades.A02factco;
import com.casapellas.entidades.Vf0101;
import com.casapellas.entidades.Vf55ca01;
import com.casapellas.entidades.Vf55ca01Id;
import com.casapellas.entidades.Vrptmcaja005;
import com.casapellas.entidades.Vrptmcaja005Id;
import com.casapellas.entidades.Vrptmcaja006;
import com.casapellas.entidades.Vrptmcaja006Id;
import com.casapellas.entidades.ens.Vautoriz;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.hibernate.util.RoQueryManager;
import com.casapellas.navegacion.As400Connection;
import com.casapellas.rpg.P55RECIBO;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.CustomEmailAddress;
import com.casapellas.util.Divisas;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.MailHelper;
import com.casapellas.util.PropertiesSystem;
import com.casapellas.util.SqlUtil;
import com.ibm.icu.util.Calendar;

public class CtrlCajas {
	public CtrlCajas() {
		super();
	}
	public String lblNoCaja;
	public String lblCajero;
	public String lblSucursal;
	public String lblNumeroRecibo;
	F55ca01[] cajas = null;
	F55ca014[] cajascom = null;
	
	//&& ============== Impresion del recibo oficial de caja.
	private static P55RECIBO p55recibo;
	private static P55RECIBO getP55recibo() {
		p55recibo = (P55RECIBO) FacesContext
				.getCurrentInstance().getELContext().getELResolver()
				.getValue(FacesContext.getCurrentInstance()
					.getELContext(), null, "p55recibo");
		return p55recibo;
	}
	public static void imprimirRecibo(int caid, int numrec, String codcomp,
									String codsuc, String tiporec, boolean reimpresion){
		try{
			
			p55recibo = getP55recibo();
			
//			p55recibo.setConnectionData("192.168.1.3", "APPCP", "APPCP1810");
			
			
			p55recibo.setIDCAJA(new BigDecimal(String.valueOf(caid)));
			p55recibo.setNORECIBO(new BigDecimal(String.valueOf(numrec)));
			p55recibo.setIDEMPRESA(codcomp.trim());
			p55recibo.setIDSUCURSAL(codsuc.trim());
			p55recibo.setTIPORECIBO(tiporec);
			
			//&& ======== impresion de donacion
			if(tiporec.compareTo("DN") == 0 ){
				p55recibo.setRESULTADO("4"); 
			}
			
			//&& ======== reimpresion de donacion 
			if(tiporec.compareTo("DN1") == 0 ){
				p55recibo.setRESULTADO( MetodosPagoCtrl.EFECTIVO ); 
				p55recibo.setTIPORECIBO("DN");
			}
			
			//&& ========= reimpresion de recibos 
			if(reimpresion){
				p55recibo.setRESULTADO("3"); 
			}
			
			
			p55recibo.setCOMANDO("");
			
			p55recibo.invoke();
			
			p55recibo.disconnect() ;
			
			
		}catch (Exception e) { 
			e.printStackTrace();
		}finally{
			try{
				int result = (p55recibo.getRESULTADO().compareTo("") == 0)? 1: Integer.parseInt(p55recibo.getRESULTADO()) ;
				switch (result) {
				case 1: 
						System.err.println("==> "+p55recibo.getErrParm());
					break;
				case 0:
					break;
				}
			}catch (Exception e) { 
				e.printStackTrace();
			}
		}
	}
	
	public static void loadP55Recibo(){
		try {
			
//			p55recibo.setConnectionData("192.168.1.3", "APPCP", "APPCP1810");
//			getP55recibo().setRESULTADO("6");
//			getP55recibo().invoke();
			
			p55recibo = getP55recibo();
			
			p55recibo.invoke();
			
			p55recibo.disconnect() ;
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Vf0101> obtenerCajerosDeCierre(){
		Session sesion = null;
		Transaction trans = null;
		boolean bNuevaCn = false;
		List<Vf0101>lstUsuarios = null;
		
		try {
			sesion = HibernateUtilPruebaCn.currentSession();
			if(sesion.getTransaction().isActive())
				trans = sesion.getTransaction();
			else{
				trans = sesion.beginTransaction();
				bNuevaCn = true;
			}
			
			List<Integer>lstCodsF55 = (ArrayList<Integer>)sesion.createCriteria
						(F55ca01.class).setProjection(Projections
						.distinct( Projections.property("id.cacati"))) 
						.add(Restrictions.eq("id.castat", 'A')).list();
			
			List<Integer>lstCodsArq = (ArrayList<Integer>)sesion.createCriteria
						(Arqueo.class).setProjection(Projections
							.distinct(Projections.property("codcajero")))
							.add(Restrictions.not(Restrictions.in("codcajero", 
									 lstCodsF55))).list();
			
			if(lstCodsF55 != null && lstCodsArq != null && !lstCodsArq.isEmpty() )
				lstCodsF55.addAll(lstCodsArq);
			
			lstUsuarios = sesion.createCriteria(Vf0101.class)
						.add(Restrictions.in("id.aban8", lstCodsF55 )).list();
			
			if( lstUsuarios != null && !lstUsuarios.isEmpty()){
				
				Collections.sort(lstUsuarios, new Comparator<Vf0101>() {
					public int compare(Vf0101 v1, Vf0101 v2) {
						int iCompFecha = ( v1.getId().getAbalph().trim().compareTo(
									 v2.getId().getAbalph().trim() ) < 0)? -1 :
									 ( v1.getId().getAbalph().trim().compareTo(
									  v2.getId().getAbalph().trim() ) > 0)? 1 : 0;
						return iCompFecha;
					}
				});
			}
			
		} catch (Exception e) {
			lstUsuarios = new ArrayList<Vf0101>();
			e.printStackTrace(); 
		}finally{
			if(bNuevaCn){
				try{ trans.commit(); }catch(Exception e){e.printStackTrace();};
				try{ HibernateUtilPruebaCn.closeSession(sesion); }catch(Exception e){e.printStackTrace();};
			}
		}
		return lstUsuarios;
	}
	
	public static String generarMensajeBlk(){
		String sMensaje = "";
		try {
			
			if( CodeUtil.getFromSessionMap("BLOQUEOCAJA")  != null){
				String sValor = String.valueOf(CodeUtil.getFromSessionMap("BLOQUEOCAJA"));
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
		
		
		try {
			
			sesion = HibernateUtilPruebaCn.currentSession();
			
			
			Criteria cr = sesion.createCriteria(F55ca01.class);
			cr.add(Restrictions.eq("id.caid", iCaid));
			cr.add(Restrictions.eq("id.castat", 'A'));
			cr.setMaxResults(1);
			
			F55ca01 f5 = (F55ca01)cr.uniqueResult();
			
			String sql ="select * from "+PropertiesSystem.GCPSISEVA+".workcalendar"+
					" where :FECHA  between startdate and enddate " +
					" and plan = :PLAN "+
					" and clocal =  'Nacional' and trim(Ccountry) = 'NI' " ;
			
			//&& ===== Si tiene cero, no validar.
			if( f5.getId().getCadiablk() == 0 ){
				throw new Exception("No es necasario Validar");
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
			
			//&& ==== No hay cierre y esta bloqueada: Desbloquear.
			if( (lstArqueo == null || lstArqueo.isEmpty())){
				CodeUtil.removeFromSessionMap( "BLOQUEOCAJA" );

				if( f5.getId().getCastdblk() == 1 )	
					iProxEst = 0;
				
			}
			//&& ==== Hay cierre y esta desbloqueada: bloquear.
			if(lstArqueo != null && lstArqueo.size() > 0 ){
				CodeUtil.putInSessionMap( "BLOQUEOCAJA", lstArqueo.size()+ "@" + new SimpleDateFormat("dd/MM/yyyy").format(cal.getTime() ) );
				if(f5.getId().getCastdblk() == 0 )
					iProxEst = 1;
			}
			
			if(iProxEst != -1){
				String sqlUpd  = " update "+PropertiesSystem.ESQUEMA+".f55ca01 " +
							 " set castdblk = " +iProxEst + 
							 " where caid = "+iCaid;
				ConsolidadoDepositosBcoCtrl.executeSqlQueryTx(null, sqlUpd);
				
				
				//&& ====== Correo de notificacion =========
				cr = sesion.createCriteria(Vf55ca01.class);
				cr.add(Restrictions.eq("id.caid", iCaid));
				cr.setMaxResults(1);
				
				Vf55ca01 vf = (Vf55ca01)cr.uniqueResult();
				Vautoriz[] vAut = (Vautoriz[])CodeUtil.getFromSessionMap("sevAut");
				Vf0101 vf01 =  EmpleadoCtrl.buscarEmpleadoxCodigo2( vAut[0].getId().getCodreg() );
				
				String sComodin = "=>";
				String sCorreos = vf.getId().getCacontmail().trim().toLowerCase()+ sComodin
						+ vf01.getId().getWwrem1().trim().toLowerCase()+ sComodin 
						+ vf.getId().getCaan8mail().trim().toLowerCase();
				
				String sNombres = vf.getId().getCacontnom().trim().toLowerCase() + sComodin
						+ vf01.getId().getAbalph().trim().toLowerCase()+ sComodin
						+ vf.getId().getCaan8nom().trim().toLowerCase();
			
				String sNota = "";
				String sSubject = "";
				sNota = "<strong>Estimado(a):</strong><em> "+ CodeUtil.capitalize( vf.getId().getCacontnom().trim() )+"</em>:";
				sNota += "<br>";
				sNota += "<br>";
				
				if(iProxEst == 1){
					sSubject = "Bloqueo temporal en procesos de caja " + vf.getId().getCaname().toLowerCase();

					sNota += "La caja # "+f5.getId().getCaid();
					sNota += " ("+ Divisas.ponerCadenaenMayuscula(vf.getId().getCaname().trim())+") ";
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
					sSubject = "Bloqueo liberado para procesos de caja " +vf.getId().getCaname().toLowerCase();
					sNota += "La caja # "+f5.getId().getCaid();
					sNota += " ("+ Divisas.ponerCadenaenMayuscula(vf.getId().getCaname().trim())+")";
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
				
				//&& ====================== Mandar el correo
				List<CustomEmailAddress> toList = new ArrayList<CustomEmailAddress>();
				String[] lstCorreos = sCorreos.split(sComodin);
				String[] lstNombres = sNombres.split(sComodin);
			
				for (int i = 0; i < lstCorreos.length; i++){
					if( !lstCorreos[i].trim().toUpperCase().matches(PropertiesSystem.REGEXP_EMAIL_ADDRESS)  ) {
						continue;
					}
					toList.add(new CustomEmailAddress(lstCorreos[i].toLowerCase(), CodeUtil.capitalize( lstNombres[i] )));
				}
				
				MailHelper.SendHtmlEmail(
						new CustomEmailAddress(PropertiesSystem.WEBMASTER_EMAIL_ADRESS, "Módulo de Caja"),
						toList, null, new CustomEmailAddress(PropertiesSystem.MAIL_BOUNCEADDRESS), 
						sSubject, sNota.toString());
			}
			 
			
		} catch (Exception e) {
			bBloqueo = false;
			
			//e.printStackTrace(); 
		}finally{
 
			HibernateUtilPruebaCn.closeSession(sesion);
			
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
	@SuppressWarnings("unchecked")
	public static List<A02factco> leerTraslados(int iCaid, int iFechaActual){
		StringBuilder sbSql2 = new StringBuilder();
		List<A02factco> lstFacturas = new ArrayList<A02factco>();
		Session sesion = null; 
		Transaction trans = null;
		boolean newCn = false;
		
		try{
			
			sesion = HibernateUtilPruebaCn.currentSession();
			trans = (newCn = !(sesion.getTransaction().isActive())) ? sesion
					.beginTransaction() : sesion.getTransaction();
			
			sbSql2.append(" select f.* from ")
				.append(PropertiesSystem.ESQUEMA)
				.append(".A02factco as f inner join ")
				.append(PropertiesSystem.ESQUEMA)
				.append(".trasladofac tf on  tf.nofactura = f.nofactura and ")
				.append(" tf.codcomp = f.codcomp and tf.tipofactura = f.tipofactura")
				.append(" and trim(tf.codunineg) = trim(f.codunineg) ")
				.append(" and tf.estadotr in ('E','R') ")
				.append(" and tf.caiddest = ").append(iCaid)
				.append(" and f.fecha = ").append(iFechaActual)
				.append(" and trim(f.estado) = '' ");

			LogCajaService.CreateLog("leerTraslados", "QRY", sbSql2.toString());
			
			lstFacturas = (ArrayList<A02factco>) sesion
					.createSQLQuery(sbSql2.toString())
					.addEntity(A02factco.class).list();
			
			
		}catch(Exception ex){
			ex.printStackTrace();
			LogCajaService.CreateLog("leerTraslados", "ERR", ex.getMessage());
		}finally {			
			if(newCn){
				try {  trans.commit(); } 
				catch (Exception e2) { }
				try {  HibernateUtilPruebaCn.closeSession(sesion); }
				catch (Exception e2) { }
			}
			sbSql2 = null;
			sesion = null;
			trans = null;
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
				// TODO Auto-generated catch block
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
			LogCajaService.CreateLog("getAllCajas", "ERR", ex.getMessage());
		}finally{
		
			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				LogCajaService.CreateLog("getAllCajas", "ERR", e.getMessage());
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
		List lstRegist = null;
		Transaction trans = null;
		
		Session sesion = null;
		
		String sql = "", sCuenta[];
		Divisas dv = new Divisas();
		String iResul = "" ;
		boolean bNuevaSesionENS = false;
		boolean bNuevaSesionCaja = false;
		
		try {
			//--- Conexiones.
			sesion = HibernateUtilPruebaCn.currentSession();
			
			
			if( sesion.getTransaction().isActive() )
				trans = sesion.getTransaction();
			else{
				trans = sesion.beginTransaction();
				bNuevaSesionCaja = true;
			}
			
			//--- Leer la cuenta de banco para determinar la sucursal de la cuenta, que se usa GLKCO
			sCuenta = dv.obtenerCuentaBanco(sCodcomp, sMoneda,iCodbanco, sesion, trans, sesion, trans);
			if(sCuenta!=null){
				
				sql =  " SELECT * FROM "+PropertiesSystem.JDEDTA+".F0911 WHERE GLDOC = "+ iNoreferdep+ " and gldct = '"+sTipodoc+"'";
				sql += " AND GLKCO = '"+CodeUtil.pad(sCuenta[2].trim(), 5, "0")+"'";
//				sql += " AND GLKCO = '000"+sCuenta[2]+"'";
				
				lstRegist = sesion.createSQLQuery(sql).list();
				
				if(lstRegist!=null && lstRegist.size()>0)
					iResul = "El número de referencia de Depósito " +iNoreferdep+ " ya ha sido registrado en JDE";
				else{
					//--- Verificar el número de referencia contra los registros de recibos de caja.
					sql = " from Recibodet rd where rd.id.refer1= '"+String.valueOf(iCodbanco)+"' ";
					sql+= " and rd.id.moneda = '" +sMoneda+ "' and rd.id.codcomp = '" +sCodcomp+ "' ";
					sql+= " and (   (rd.id.refer2 = '" +sReferenciaPago+ "') or (rd.id.refer3 = '" +sReferenciaPago+ "')   )";
					
					lstRegist = sesion.createQuery(sql).list();
					if(lstRegist!=null && lstRegist.size()>0)
						iResul = "El número de referencia de Depósito " +iNoreferdep+ " ya ha sido registrado en Recibos - Caja";
					else{						
						//--- Validar contra el arqueo 
						sql = " from Arqueo a where trim(a.referdep) = trim('" +sReferenciaPago+ "') ";
						sql+= " and a.moneda = '" +sMoneda+ "' and a.id.codcomp = '" +sCodcomp+ "'";
						sql+= " and a.estado = 'P'";
						
						lstRegist = sesion.createQuery(sql).list();
						if(lstRegist!=null && lstRegist.size()>0)
							iResul = "El número de referencia de Depósito " +iNoreferdep+ " ya ha sido registrado en Arqueos - Caja";
						else
							iResul = "";
					}
				}				
			}else{
				iResul =  "No se ha podido verificar el número de referencia: ";
				iResul += "la cuenta de banco "+iCodbanco +" no está configurada" ;
			}

		} catch (Exception error) {
			String sMensaje = " \n Error en CtrlCajas.verificarReferenciaPago ";
			sMensaje += error.getMessage() +"\n";
			sMensaje += error.getCause()!=null? error.getCause():" " +"\n";
			error.printStackTrace();
			iResul = "No se ha podido verificar Referencia de pago:\nError de Sistema=> " +error.getMessage();

		}finally{
			
			if(bNuevaSesionCaja){
				try{ trans.commit(); }catch(Exception e){e.printStackTrace();};
				try{ HibernateUtilPruebaCn.closeSession(sesion); }catch(Exception e){e.printStackTrace();};
			}
		}
		return iResul;
	}

	public boolean actualizarMontoMinimo(int iCaid, String sCodcomp,
						String sMoneda, double dMonto, Session sesion) {
		boolean bHecho = true;
		String sql = new String();

		try {
			 
			sql = " UPDATE "+PropertiesSystem.ESQUEMA+".F55CA013 SET C3MIAM = "
					+ Divisas.roundDouble(dMonto * 100)
					+" where c3id = " + iCaid + " and c3rp01 = '"
					+ sCodcomp + "' and c3crcd = '" + sMoneda + "'" ;
		
			Query q = sesion.createSQLQuery(sql);
			q.executeUpdate();
			
		} catch (Exception error) {
			bHecho = false;
			error.printStackTrace();
		}
		return bHecho;
	}
	
/****************************************************************************************/
/** Leer   Actualizar el monto mínimo de la caja. 
 *  Fecha: 23/06/2010
 *  Hecho: Carlos Manuel Hernández Morrison.
 */
	public boolean actualizarMontoMinimo(int iCaid,String sCodcomp, 
						String sMoneda,double dMonto,Connection cn){
		boolean bHecho = false, local = false;
		StringBuilder sbSql = new StringBuilder();
		Divisas dv = new Divisas();
		PreparedStatement ps = null;
		try {
			sbSql.append(" UPDATE "+PropertiesSystem.ESQUEMA+".F55CA013 SET C3MIAM = "+dv.roundDouble(dMonto*100));
			sbSql.append(" where c3id = "+iCaid+" and c3rp01 = '"+sCodcomp+"' and c3crcd = '"+sMoneda+"'");
			if(cn==null){
				As400Connection as400connection = new As400Connection();
				cn = as400connection.getJNDIConnection("DSMCAJA2");
			    cn.setAutoCommit(false);
			    local = true;
			}
			
			ps = cn.prepareStatement(sbSql.toString());
			int rs = ps.executeUpdate();
			if(rs > 0){
				bHecho = true;
			}
			ps.close();
			if(local){
				cn.commit();
				cn.close();
			}			
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
			LogCajaService.CreateLog("leerConfiguracionCaja", "ERR", error.getMessage());
		}
		return lstDtsCaja;
	}
	
	

/****************************************************************************************/
/** Leer las facturas del día para una caja, incluyendo salidas y entradas por traslados
 *  Fecha:  28/05/2010
 *  Hecho: Carlos Manuel Hernández Morrison.
 */	
	@SuppressWarnings("unchecked")
	public List leerFacturaDelDia1(int iFechaActual,String[] sTipoDoc, F55ca017[] f55ca017,List lstLocalizaciones, int iCaid){
		List<Hfactjdecon> lstFacturas = new ArrayList<Hfactjdecon>();
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = null;
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
			
			//&& ======= Separar las UN con y sin localizacion
			ArrayList<String>UnConLoc = new ArrayList<String>();
			ArrayList<String>UnSinLoc = new ArrayList<String>();
			ArrayList<String>localzn  = new ArrayList<String>();
						
			for (F55ca017 f : f55ca017) {
				
				if(f.getId().getC7locn().trim().compareTo("") == 0 ){
					if(!UnSinLoc.contains(f.getId().getC7mcul().trim()))
						UnSinLoc.add(f.getId().getC7mcul().trim());
				}else{
					if(!UnConLoc.contains(f.getId().getC7mcul().trim()))
						UnConLoc.add(f.getId().getC7mcul().trim());
					if(!localzn.contains(f.getId().getC7locn().trim()))
						localzn.add(f.getId().getC7locn().trim());
				}
			}
			
			//&& ===== Obtener las localizaciones asociadas a la unidad de negocio.
			String sConditionLocs = "";
			for (final String codunineg : UnConLoc) {

				List<F55ca017> locs = (ArrayList<F55ca017>)
				CollectionUtils.select(Arrays.asList(f55ca017),
						new Predicate() {
							public boolean evaluate(Object F17) {
								F55ca017 f = (F55ca017) F17;
								return f.getId().getC7mcul().trim()
										.compareTo(codunineg) == 0
										&& f.getId().getC7locn().trim()
												.compareTo("") != 0;
							}
						});
				List<String>codlocs = new ArrayList<String>();
				for (F55ca017 f : locs) 
					codlocs.add(f.getId().getC7locn().trim());
				
				String val = codlocs.toString().replace("[", "('")
							.replace("]", "')").replace(",", "','")
							.replace(" ", "");
				
				
				if (sConditionLocs.compareTo("") != 0)
					sConditionLocs += " or ";
				sConditionLocs += " ( trim(f.codunineg) ='" + codunineg
						+ "' and trim(f.sdlocn) in " + val + " )\n";
			}
			
			StringBuilder sb = new StringBuilder( UnSinLoc.toString() );
			sb.replace(0, 1, "('").replace(sb.length()-1, sb.length(), "')");
			String sUnSinLoc = sb.toString().replace(", ", "', '") ;
			
			sb = new StringBuilder( UnConLoc.toString() );
			sb.replace(0, 1, "('").replace(sb.length()-1, sb.length(), "')");
			String sUnConLoc = sb.toString().replace(", ", "','") ;
			
			sb = new StringBuilder( localzn.toString() );
			sb.replace(0, 1, "('").replace(sb.length()-1, sb.length(), "')");
			String sLocsn = sb.toString().replace(", ", "', '");
			
			sb = new StringBuilder( Arrays.toString(sTipoDoc) );
			sb.replace(0, 1, "('").replace(sb.length()-1, sb.length(), "')");
			String sTipoDocs = sb.toString().replace(", ", "', '");
			
			sbSql1.append("select * from "+PropertiesSystem.ESQUEMA+".Hfactjdecon " +
					"as f where f.tipopago in ('00','01','001') and f.fecha =" + iFechaActual);
			sbSql1.append(" and trim(f.estado) = '' and f.tipofactura in " + sTipoDocs);
			
			
			sbSql2.append(sbSql1.toString());		
			
			sbSql1.append(" and trim(f.sdlocn) = '' and trim(f.codunineg) in "+ sUnSinLoc);
			
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
				
				sbSql2.append( "and ( " +sConditionLocs +" ) ");
				
				sbSql2.append(" and f.nofactura not in(select rf.numfac from "+PropertiesSystem.ESQUEMA+".Recibofac as rf"); 
				sbSql2.append(" where rf.numfac = f.nofactura and rf.codcomp = f.codcomp and");
				sbSql2.append(" rf.codcli = f.codcli and rf.fecha = f.fecha and ");
				sbSql2.append(" rf.tipofactura = f.tipofactura and rf.estado = '' and trim(rf.codunineg) = trim(f.codunineg))");
				sbSql2.append(sbExcluidas);
				
				sbSql1.append(" UNION ");
				sbSql1.append(sbSql2);
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
			
			tx = session.beginTransaction();
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
		}finally {
			try{tx.commit();}catch(Exception e){}
			try {HibernateUtilPruebaCn.closeSession(session); } catch (Exception e2) {e2.printStackTrace(); }
		}
	return lstFacturas;
}
	
	public static String localizacionPorUnineg( List<F55ca017> lstF55ca017 ){
		String sConditionLocs = "";
		
		try {
			
	        Object[] ss1 = SqlUtil.getListFromFieldF55ca017(lstF55ca017, "c7mcul");
			
	        String includeUN = "";
	        
			for (Object unineg : ss1) {
				final String codunineg = String.valueOf(unineg);
				
	        	if( includeUN.contains(codunineg) ) 
	        		continue;
	        	includeUN += codunineg + ",";
				
				@SuppressWarnings("unchecked")
				List<F55ca017> locs = (ArrayList<F55ca017>)
				CollectionUtils.select(lstF55ca017,
						new Predicate() {
							public boolean evaluate(Object F17) {
								F55ca017 f = (F55ca017) F17;
								return f.getId().getC7mcul().trim()
										.compareTo(codunineg) == 0
										&& f.getId().getC7locn().trim()
												.compareTo("") != 0;
							}
						});
				
		        Object[] ss2 = SqlUtil.getListFromFieldF55ca017(locs, "c7locn");
		        String localizaciones = SqlUtil.constructQuery(false, "trim(f.sdlocn)", true, ss2, null);
				
		        if ( !sConditionLocs.isEmpty() )
					sConditionLocs += " or ";
				sConditionLocs += " ( trim(f.codunineg) ='" + codunineg
						+ "' " + localizaciones + " )\n";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			sConditionLocs= ""; 
		}
		return sConditionLocs;
	}
	
	
	@SuppressWarnings("unchecked")
	public static List<A02factco> leerFacturaDelDia(int fecha, String[] sTipoDoc,
											F55ca017[] f55ca017, int iCaid) {
		
		List<A02factco> lstFacturas = new ArrayList<A02factco>();
		Session sesion = null;
		
		
		//fecha= 123161;
		StringBuilder sqlFacturasContado = new StringBuilder();
		StringBuilder sqlFacturasConLocs = new StringBuilder();
		StringBuilder sqlFacturasSinLocs = new StringBuilder();
		StringBuilder sbFacturasEnviadas = new StringBuilder();
		StringBuilder sqlFromFacturas = new StringBuilder();
		boolean bHayLoc = false,bSoloLocs = false;
		
		try {
	       
			List<F55ca017> lstF55ca017 = new ArrayList<F55ca017>();
			
			if( f55ca017 != null && f55ca017.length > 0 ){
				 lstF55ca017 = new ArrayList<F55ca017>(Arrays.asList(f55ca017));
			}
			 
			List<F55ca017> lstF55ca017Tmp1 = SqlUtil.getFilterListF55ca017(lstF55ca017, "", '=');
	        Object[] ss0 = SqlUtil.getListFromFieldF55ca017(lstF55ca017Tmp1, "c7mcul");
	        if (lstF55ca017Tmp1.size() > 0) {
	            bSoloLocs = true;
	        }
	        List<F55ca017> lstUnidadConLocs = SqlUtil.getFilterListF55ca017(lstF55ca017, "", 'D');
	        if (lstUnidadConLocs.size() > 0) {
	        	bHayLoc = true;
	        }
	        
			String uninegsFacsSinLcn = SqlUtil.constructQuery(false, "trim(f.codunineg)", true, ss0, null);
			sqlFacturasSinLocs.append("select * from ").append(PropertiesSystem.ESQUEMA)
			.append(".A02factco as f").append(" where trim(f.sdlocn) = '' ")
			.append(uninegsFacsSinLcn);
			
			if (bHayLoc) {
				
				//&& ===== Obtener las localizaciones asociadas a la unidad de negocio.
				String sConditionLocs =  localizacionPorUnineg(lstUnidadConLocs);
				
				sqlFacturasConLocs.append("select * from ")
				.append(PropertiesSystem.ESQUEMA).append(".A02factco as f")
				.append(" where ( ").append(sConditionLocs).append(" )");
				
				sqlFromFacturas.append(sqlFacturasConLocs);
				if (bSoloLocs) {
					sqlFromFacturas.append(" \n union  \n")
							.append(sqlFacturasSinLocs);
				}
			} else {
				sqlFromFacturas.append(sqlFacturasSinLocs);
			}
			
			//&& ========= Consulta final.
			String sTipoDocs = SqlUtil.constructQuery(true, "f1.tipofactura", true, sTipoDoc, null);
			sqlFromFacturas = new StringBuilder("select * from (")
					.append(sqlFromFacturas).append(") as f1 ")
					.append(sTipoDocs);
			
			sbFacturasEnviadas.append(" \n union all \n ")
			.append(" select f.* from ")
			.append(PropertiesSystem.ESQUEMA)
			.append(".a02factco as f inner join ")
			.append(PropertiesSystem.ESQUEMA).append(".trasladofac tf ")
			.append(" on  tf.nofactura = f.nofactura ")
			.append(" and tf.codcomp = f.codcomp ")
			.append(" and tf.tipofactura = f.tipofactura ")
			.append(" and trim(tf.codunineg) = trim(f.codunineg) ")
			.append(" and tf.codcli = f.codcli ")
			.append(" and tf.fechafac = f.fecha")
			.append(" where tf.estadotr in ('E','R') ")
			.append(" and tf.caiddest = ").append(iCaid);
			sqlFromFacturas.append(sbFacturasEnviadas);
			
			
			sqlFacturasContado.append("select *  from ( ") 
			.append( sqlFromFacturas )
			.append(" \n) F") 
			.append(" where f.tipopago in ('00','01','001') ")
			.append(" and f.fecha = ").append(fecha)
			.append(" and trim(f.estado) = '' ") 
			.append("\n and nofactura not in ( ")
			.append(" select tf.nofactura from ")
			.append(PropertiesSystem.ESQUEMA).append(".trasladofac tf ")
			.append(" where tf.nofactura = f.nofactura and tf.codcomp = f.codcomp ")
			.append(" and tf.tipofactura = f.tipofactura ")
			.append(" and trim(tf.codunineg) = trim(f.codunineg) ")
			.append(" and tf.codcli = f.codcli and tf.fechafac = f.fecha") 
			.append(" and tf.estadotr in ('E','R') and tf.caidprop = ")
			.append(iCaid).append("	) ") 
			.append(" \n  and nofactura not in ( ")
			.append(" select rf.numfac from ")
			.append(PropertiesSystem.ESQUEMA).append(".Recibofac as rf ")
			.append(" where rf.numfac = f.nofactura and rf.codcomp = f.codcomp ")
			.append(" and rf.codcli = f.codcli and rf.fecha = f.fecha ")
			.append(" and  rf.tipofactura = f.tipofactura and rf.estado = '' ")
			.append(" and trim(rf.codunineg) = trim(f.codunineg) ) ");
			
			LogCajaService.CreateLog("leerFacturaDelDia", "QRY", sqlFacturasContado.toString());
			
			sesion = HibernateUtilPruebaCn.currentSession();
			
			
			lstFacturas = (ArrayList<A02factco>) sesion
					.createSQLQuery(sqlFacturasContado.toString())
					.addEntity(A02factco.class).list();
			
			if(lstFacturas != null && lstFacturas.size() > 0 ){
				CollectionUtils.filter(lstFacturas, new Predicate() {
					public boolean evaluate(Object arg0) {
						A02factco hfac = (A02factco)arg0;
						
						return ( hfac.getSubtotal() > 0 && 
								 hfac.getTotal() > 0 );
					}
				});
			}
			
		} catch (Exception e) {
			LogCajaService.CreateLog("leerFacturaDelDia", "ERR", e.getMessage());
			lstFacturas = null;
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
			LogCajaService.CreateLog("obtenerListaCajas", "ERR", error.getMessage());
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
		boolean bNuevaSesionENS = false;
		
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
	public static List<Object[]> obtenerF55ca028(int iCaid, String sCodcomp, String sMoneda){
		List<Object[]> lstDatosDev = null;

		try {
			
			String sql = " SELECT CAST(F.D4DPER AS INTEGER) D4DPER, CAST(F.D4MPER AS DECIMAL(10,2)) D4MPER";
			sql += " FROM "+PropertiesSystem.ESQUEMA+".F55CA028 F";
			sql += " WHERE F.D4ID = "+iCaid+" AND F.D4RP01 = '"+sCodcomp+"' AND F.D4CRCD = '"+sMoneda+"'";
			
			lstDatosDev = ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, null, true);
			
		} catch (Exception error) {
			LogCajaService.CreateLog("obtenerF55ca028", "ERR", error.getMessage());
			
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
			LogCajaService.CreateLog("obtenerDatosCaja", "ERR", error.getMessage());
		}

		return v;
	}
/*****************************************************************************************************/
/**	 		Obtiene los datos para generar el reporte de emisión de recibos rptmcaja007				**/
	public List obtieneRecibosrpt007(int iCaid, String sCodcomp,Date dtIni,Date dtFin){ 
				return null ;
	}
/*****************************************************************************************************/
/**	 		Obtiene los datos para generar el reporte de emisión de recibos rptmcaja006				**/
	@SuppressWarnings("unchecked")	
	public List<Vrptmcaja006Id> obtieneRecibosrpt006(int iCaid,String sCodcomp,String sMoneda,Date dtIni,Date dtFin){ 
		List<Vrptmcaja006> lstRecibos = null;
		List<Vrptmcaja006Id> lstRept = null;
		Session sesion = HibernateUtilPruebaCn.currentSession();
		try {
			
			Transaction trans = sesion.beginTransaction();

			Criteria cr = sesion.createCriteria(Vrptmcaja006.class);
			if(iCaid != 0)
				cr.add(Restrictions.eq("id.caid", iCaid ));
			cr.add(Restrictions.eq("id.codcomp", sCodcomp));
			cr.add(Restrictions.eq("id.moneda", sMoneda));
			cr.add(Restrictions.ne("id.estado", "A"));
			cr.addOrder(Order.asc("id.numrec"));
			
			if(dtIni != null) cr.add(Restrictions.ge("id.fecha",dtIni ));
			if(dtFin != null) cr.add(Restrictions.le("id.fecha",dtFin ));
			
			lstRecibos = cr.list();
			
			trans.commit();
			
			if(lstRecibos != null && lstRecibos.size()>0){
				lstRept = new ArrayList<Vrptmcaja006Id>();
				FechasUtil f = new FechasUtil();
				for (Vrptmcaja006 v : lstRecibos) {
					Vrptmcaja006Id vid = v.getId();
					vid.setFecharecibo(f.formatDatetoString(vid.getFecha(), "dd/MM/yyyy"));
					vid.setDmontoapl(vid.getMontoapl().doubleValue());
					vid.setCliente(v.getId().getCodcli()+" " + vid.getCliente());
					vid.setTasacambio(vid.getTasa().doubleValue());
					vid.setDmontoapl(vid.getMontoapl().doubleValue());
					vid.setDmontoneto(vid.getMontoneto().doubleValue());
					vid.setDmonto(vid.getMonto().doubleValue());
					lstRept.add(vid);
				}
			}
		} catch (Exception error) {
			lstRecibos = null;
			error.printStackTrace(); 
		} finally{
			try {HibernateUtilPruebaCn.closeSession(sesion); } catch (Exception e2) {e2.printStackTrace(); }
		}
		return lstRept;
	}
/*****************************************************************************************************/
/**	 		Obtiene los datos para generar el reporte de emisión de recibos rptmcaja005				**/
	public List obtieneRecibosrpt005(int iCaid, String sCodcomp, String sMoneda,Date dtIni,Date dtFin){
		List lstRecibos = null;
		String sql = "",sFechaIni="",sFechaFin="";
		FechasUtil f = new FechasUtil();
		Transaction trans = null;
		Session sesion = HibernateUtilPruebaCn.currentSession();
		
		try {
			sFechaIni = f.formatDatetoString(dtIni, "yyyy-MM-dd");
			sFechaFin = f.formatDatetoString(dtFin, "yyyy-MM-dd");
			
			sql =  " from Vrptmcaja005 v where v.id.caid = "+iCaid + " and v.id.codcomp = '"+sCodcomp+"'";
			sql += " and v.id.moneda = '"+sMoneda+"' and v.id.glcrcd = '"+sMoneda+"'";
			sql += " and v.id.fecha between '"+sFechaIni+"' and '"+sFechaFin+"'";
			
			trans = sesion.beginTransaction();
			lstRecibos = sesion.createQuery(sql).list();
			trans.commit();
			
			//------ Definir los registros de cuales son créditos y cuáles débitos.
			if(lstRecibos!=null && lstRecibos.size()>0){
				for(int i=0; i<lstRecibos.size(); i++){
					Vrptmcaja005 v = (Vrptmcaja005)lstRecibos.get(i);
					Vrptmcaja005Id vid = v.getId();
					
					String sFecharec = f.formatDatetoString(vid.getFecha(), "dd-MM-yyyy");
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
					lstRecibos.set(i, vid);					
				}
			}
		} catch (Exception error) {
			lstRecibos = null;
			error.printStackTrace(); 
		} finally {
			try {HibernateUtilPruebaCn.closeSession(sesion); } catch (Exception e2) {e2.printStackTrace(); }
		}
		return lstRecibos;
	}
/*******************************************************************************/
/** 	 Obtener información de la caja, desde el F55CA020        **************/	
	public F55ca020 obtenerInfoCaja(String sCodsuc, String sCodcomp){
		F55ca020 f = null;
		Session sesion = HibernateUtilPruebaCn.currentSession();
		Transaction trans = null;
		List lstF55 = null;
		String sql = "";
		
		try {
			sql = "from F55ca020 f where f.id.codcomp = '"+sCodcomp+"' and f.id.codsuc = '"+sCodsuc+"'";
			trans = sesion.beginTransaction();
			lstF55 = sesion.createQuery(sql).list();
			trans.commit();
			
			if(lstF55!=null && lstF55.size()>0)
				f = (F55ca020)lstF55.get(0);
			
		} catch (Exception error) {
			error.printStackTrace(); 
		} finally {
			try {HibernateUtilPruebaCn.closeSession(sesion); } catch (Exception e2) {e2.printStackTrace(); }
		}
		return f;		
	}	
/****************************************************************************************************/
/** 	Obtiene la lista fechas sin duplicados que tienen transacciones
 **************/	
	public List obtenerFechasTransaciones(int iCaid,String sCodcomp,Date dtFechaini,Date dtFechafin){
		List lstFechas = new ArrayList();
		Session sesion = HibernateUtilPruebaCn.currentSession();
		Transaction trans = null;
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
			
			trans = sesion.beginTransaction();
			lstFechas = sesion.createQuery(sConsulta).list();
			trans.commit();
			
		} catch (Exception error) {
			error.printStackTrace(); 
		}finally{
			try {HibernateUtilPruebaCn.closeSession(sesion); } catch (Exception e2) {e2.printStackTrace(); }
		}	
		return lstFechas;
	}
	
	
/****************************************************************************************************/
/** 	Obtiene la lista de transacciones JDE para la caja y compañía especificada.
 **************/
	
	public List obtenerTransaccionesJDE(int iCaid,String sCodcomp,Date dtFechaini,Date dtFechafin){
		List lstTrans = new ArrayList();
		Session sesion = HibernateUtilPruebaCn.currentSession();
		Transaction trans = null;
		String sConsulta,sFechaini,sFechafin;
		
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			sFechaini = format.format(dtFechaini);
			sFechafin = format.format(dtFechafin);
			
			sConsulta = " from Vtransaccionesjde v where v.id.caid = "+iCaid+" and v.id.codcomp= '"+sCodcomp+"'";
			sConsulta += " and v.id.fecharec between '"+sFechaini+"' and '"+sFechafin+"'";
			sConsulta += " and v.id.nobatch >0 and v.id.nodocumento >0 and v.id.monto > 0";
			sConsulta += " and v.id.restado <>'A' order by v.id.nobatch,v.id.tiporec";
			
			trans = sesion.beginTransaction();
			lstTrans = sesion.createQuery(sConsulta).list();
			trans.commit();
			
		} catch (Exception error) {
			error.printStackTrace(); 
		} finally {
			try {HibernateUtilPruebaCn.closeSession(sesion); } catch (Exception e2) {e2.printStackTrace(); }
		}
		return lstTrans;
	}
	
	
/****************************************************************************************************/
/** 	Obtiene la lista de compañías asignadas al contador.
 **************/
	public List cargarCompaniaxContador(int iCodContador,int iCaid){
		Session sesion = HibernateUtilPruebaCn.currentSession();
		Transaction trans = null;	
		String consulta = "";
		List lstMonedas = new ArrayList();
		
		try{
			consulta =  " select distinct(co.id.c4rp01d1),co.id.c4rp01";//,c.id.cacont ,c.id.caid";
			consulta += " from F55ca01 c, F55ca014 co where co.id.c4id = c.id.caid and c.id.cacont = "+iCodContador;
			if(iCaid != 0)
				consulta += " and co.id.c4id = "+iCaid;

			trans = sesion.beginTransaction();
			lstMonedas = sesion.createQuery(consulta).list();
			trans.commit();
			
		}catch(Exception error){
			error.printStackTrace(); 
		}finally{
			try {HibernateUtilPruebaCn.closeSession(sesion); } catch (Exception e2) {e2.printStackTrace(); }
		}
		return lstMonedas;
	}	
/****************************************************************************************************/
/** 	Obtiene la lista de cajas que tiene asignadas el contador
 **************/
	public List obtenerCajasxContadorF55(int iCodContador){
		List<F55ca01> lstCajas = new ArrayList<F55ca01>();
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			String sql = "from F55ca01 as f where f.id.cacont = "+ iCodContador +" and f.id.castat = 'A'";
			lstCajas = (ArrayList<F55ca01>)session.createQuery(sql).list();
			tx.commit();	
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			session.close();
		}
		return lstCajas;
	}
	/* ******************************************************************* */
	public List obtenerCajasxContador(int iCodContador){
		List lstCajas = new ArrayList();
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = null;
		
		try{
			tx = session.beginTransaction();
			
			String sql = "from Vf55ca01 as f where f.id.cacont = "
					+ iCodContador
					+ " and f.id.castat = 'A'  order by f.id.caid";
			lstCajas = session.createQuery(sql).list();
			
			tx.commit();	
			
		}catch(Exception ex){
			ex.printStackTrace(); 
		}finally{
			try {HibernateUtilPruebaCn.closeSession(session); } catch (Exception e2) {e2.printStackTrace(); }
		}
		return lstCajas;
	}
	public List obtenerCajasxContador1(int iCodContador){
		List lstCajas = new ArrayList();
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			String sql= "select f.id.caid, f.id.caname from F55ca01 as f where f.id.cacont = "+ iCodContador +" and f.id.castat = 'A'";			
			lstCajas = session.createQuery(sql).list();
			tx.commit();	
		}catch(Exception ex){
			ex.printStackTrace(); 
		}finally{
			try {HibernateUtilPruebaCn.closeSession(session); } catch (Exception e2) {e2.printStackTrace(); }
		}
		return lstCajas;
	}
/*********************************************************************************************/	
	//Carga las definiciones de la caja
	public void getCajas() {
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		String cajero = (String) m.get("NombreCajero");
			
        Session session = HibernateUtilPruebaCn.currentSession();
        
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
			
		} finally {			
			session.close();
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
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			String sql = "from Vf55ca01 as f where f.id.castat = 'A' and trim(f.id.caprnt) = 'FDC' order by f.id.caid";
			lstCajas = session
			.createQuery(sql)			
			.list();
			tx.commit();	
		}catch(Exception ex){
			ex.printStackTrace(); 
		}finally {			
			try {HibernateUtilPruebaCn.closeSession(session); } catch (Exception e2) {e2.printStackTrace(); }
		}
		return lstCajas;
	}
/**********************OBTENER TODAS LAS CAJAS CONFIGURADAS PARA DOLLAR*******************************/
	public List getCajasDollar(){
		List lstCajas = new ArrayList();
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			String sql = "from Vf55ca01 as f where f.id.castat = 'A' and trim(f.id.caprnt) = 'DOLLAR'  order by f.id.caid";
			lstCajas = session
			.createQuery(sql)			
			.list();
			tx.commit();	
		}catch(Exception ex){
			ex.printStackTrace(); 
		}finally {			
			try {HibernateUtilPruebaCn.closeSession(session); } catch (Exception e2) {e2.printStackTrace(); }
		}
		return lstCajas;
	}
	
	public static String codigoGrupoCajaPorPerfil( final String codigoperfil ){
		String codigogrupo = "";
		
		try {
			
			List<String[]> perfildata = Arrays.asList(perfilesRotativos);
			
			String[] perfil = (String[]) CollectionUtils.find(perfildata,
					new Predicate() {
						public boolean evaluate(Object cod) {
							return ((String[]) cod)[0].trim().toUpperCase().compareTo( codigoperfil.trim().toUpperCase() ) == 0;
						}
					});
			
			if(perfil == null || perfil.length == 0)
				return "";
			
			codigogrupo = perfil[1].trim().toLowerCase() ;
			
			
		} catch (Exception e) {
			e.printStackTrace(); 
			codigogrupo = "";
		}
		
		return codigogrupo;
	}
	
	public static List<?> cajasPorTipoAgrupacion( String codigoperfil ){
		List<List<?>> listCajas = new ArrayList< List<?> >() ;
		
		try {
		
			//&& ========== buscar que el codigo tenga asociado algun tipo de agrupacion de caja
			
			String codigogrupo = codigoGrupoCajaPorPerfil(codigoperfil);
			
			if(codigogrupo == null || codigogrupo.isEmpty() )
				return null;
			
			String query = " from Vf55ca01 as f where f.id.castat = 'A' and lower(trim(f.id.caprnt)) = '" + codigogrupo.trim().toLowerCase() + "' order by f.id.caid " ;
			
			@SuppressWarnings("unchecked")
			List<Vf55ca01> listaCajas = (List<Vf55ca01>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, false, Vf55ca01.class);
			
			if( listaCajas == null || listaCajas.isEmpty() ) 
				return null;
			
			//&&  ============== crear la lista de selectitem para combos
			List<SelectItem> cajascombos = new ArrayList<SelectItem>( listaCajas.size() );
			
			for (Vf55ca01 v : listaCajas) {
				
				v.getId().setCaname( CodeUtil.capitalize( v.getId().getCaname() ) );
				
				cajascombos.add( new SelectItem(
						v.getId().getCaid() +"-"+ v.getId().getCaco(), 
						v.getId().getCaid() + "  : " + v.getId().getCaname().toLowerCase(),
						" Cajero Titular: " + v.getId().getCacatinom()+"") );
			}
			
			listCajas.add(0, listaCajas);
			listCajas.add(1, cajascombos);
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}finally{
			
		}
		return listCajas;
	}
	
	private static String[][] perfilesRotativos = new String[][] { 
		{ "P000000005", "" },
		{ "P000000032", "DOLLAR" },
		{ "P000000037", "DOLLAR" },
		{ "P000000043", "DICAP" },
		{ "P000000096", "BAC" } 
	};
	
/**********************OBTENER TODAS LAS CAJAS CONFIGURADAS********************************/


	public List<Vf55ca01> getAllCajas(){
		List<Vf55ca01> lstCajas = new ArrayList<Vf55ca01>();
		 
		try{
		 
			String sql = "from Vf55ca01 as f where f.id.castat = 'A' order by f.id.caid";
			
			lstCajas = new RoQueryManager().executeSqlQuery(sql, Vf55ca01.class, false) ;
			
			//lstCajas = (ArrayList<Vf55ca01>)session.createQuery(sql).list();
			
		}catch(Exception ex){
			ex.printStackTrace(); 
		} 
		return lstCajas;
	}
	
	
	 
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
	public List obtenerCajaxXCodigo(int iCaid,String sCaco){
		List lstCajas = new ArrayList();
		Session session = null;
		
		try{
			session =  HibernateUtilPruebaCn.currentSession();
			
			
			String sql = "from Vf55ca01 as f where f.id.caid = "+iCaid+ " and f.id.caco = '"+sCaco+"' and f.id.castat = 'A'";
			lstCajas = session.createQuery(sql).list();
			
			
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return lstCajas;
	}
/**************************************************************************************/
/***************LEER UNIDADES DE NEGOCIO CONFIGURADAS DEL F55CA017 activas******************************/
	@SuppressWarnings("unchecked")
	public static F55ca017[] obtenerUniNegCaja(int iCaid,String sCodComp){	
		List<F55ca017> lstCajas = new ArrayList<F55ca017>();
		F55ca017[] f55ca017 = null;
		Session session = null;
		
		try{
			
			session = HibernateUtilPruebaCn.currentSession();
			
			String sql = "from F55ca017 as f where f.id.c7id = " + iCaid
					+ " and f.id.c7co = '" + sCodComp
					+ "' and f.id.c7stat = 'A'";
			
			LogCajaService.CreateLog("obtenerUniNegCaja", "QRY", sql);
			
			lstCajas = session.createQuery(sql).list();
			
			if (lstCajas != null && !lstCajas.isEmpty())
				f55ca017 = lstCajas.toArray(new F55ca017[lstCajas.size()]);
		
		}catch(Exception ex){
			LogCajaService.CreateLog("obtenerUniNegCaja", "ERR", ex.getMessage());
		}
		return f55ca017;
	}
/*******************************************************************************************************/
/***************LEER UNIDADES DE NEGOCIO CONFIGURADAS DEL F55CA017 activas******************************/
	@SuppressWarnings("unchecked")
	public F55ca017[] obtenerUniNegCajaInactiva(int iCaid,String sCodComp){	
		List<F55ca017> lstCajas = new ArrayList<F55ca017>();
		F55ca017[] f55ca017 = null;
		Session session = null;
		
		try{
			session = HibernateUtilPruebaCn.currentSession();
			
			String sql = "from F55ca017 as f where f.id.c7id = " + iCaid
					+ " and f.id.c7co = '" + sCodComp
					+ "' and f.id.c7stat <> 'A'";
			
			LogCajaService.CreateLog("obtenerUniNegCajaInactiva", "QRY", sql);
			
			lstCajas = session.createQuery(sql).list();
			
			
			if(lstCajas != null && !lstCajas.isEmpty())
				f55ca017 = lstCajas.toArray(
							new F55ca017[lstCajas.size()]);
			
		}catch(Exception ex){ 
			LogCajaService.CreateLog("obtenerUniNegCajaInactiva", "ERR", ex.getMessage());
		}
		return f55ca017;
	}
/*********************************************************************************************/
/**************************BUSCAR LAS LINEAS*************************************************/
	@SuppressWarnings("unchecked")
	public String[] obtenerLineas(String sCodComp,F55ca017[] f55ca017,F55ca017[] f55ca017i){
		Session session = HibernateUtilPruebaCn.currentSession();
		String[] sLinea = null;
		List<String[]> lstLineas = new ArrayList<String[]>();
		
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
			
			LogCajaService.CreateLog("obtenerLineas", "QRY", sql);
			
			lstLineas = session.createQuery(sql).list();			
			
			if (lstLineas != null && !lstLineas.isEmpty())
				sLinea = lstLineas.toArray(new String[lstLineas.size()]);
			
		}catch(Exception ex){
			LogCajaService.CreateLog("obtenerLineas", "ERR", ex.getMessage());
		}
		
		return sLinea;
	}
	
	@SuppressWarnings("unchecked")
	public static String[] lineasPorUnidadNegocio(F55ca017[] f55ca017){
		Session sesion = null;
		
		boolean newCn = false;
		String[] sLinea = null;
		
		try{
			
			if(f55ca017 == null || f55ca017.length == 0)
				return null;
			
//			sesion = HibernateUtilPruebaCn.currentSessionENS();
			sesion = HibernateUtilPruebaCn.currentSession();
					
			Object[] ss2 = SqlUtil.getListFromFieldF55ca017(
					new ArrayList<F55ca017>(Arrays.asList(f55ca017)), "c7mcul");	
				
			StringBuilder sbSql = new StringBuilder();
			sbSql.append("select distinct u.id.drky from Uneglinea as u ")
					.append(SqlUtil.constructQuery(true, "trim(u.id.mcmcu)",
							true, ss2, null));
			 
			List<String> lstLineas = (ArrayList<String>) sesion.createQuery(
					sbSql.toString()).list();
			
			if (lstLineas != null && !lstLineas.isEmpty())
				sLinea =  lstLineas.toArray(new String[lstLineas.size()]);
			
		}catch(Exception ex){
			ex.printStackTrace(); 
		}
		return sLinea;
	}
	
	
/***********************************************************************************/
	//buscar lineas con activas
	public String[] obtenerLineas(String sCodComp,F55ca017[] f55ca017){
//		Session session = HibernateUtilPruebaCn.currentSessionENS();
		Session session = HibernateUtilPruebaCn.currentSession();
		
		
		String[] sLinea = null;
		List<String> lstLineas = new ArrayList<String>();
		boolean bHayUnidad = false;
		boolean bNuevaSesionENS = false;
		
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
			
			
			if (lstLineas != null && !lstLineas.isEmpty())
				sLinea =  lstLineas.toArray(new String[lstLineas.size()]);
			
		}catch(Exception ex){
			LogCajaService.CreateLog("obtenerLineas", "ERR", ex.getMessage());
		}
		return sLinea;
	}
	
	
	@SuppressWarnings("unchecked")
	public static String[] documentosPorLineas(String[] sLineas){
		String[] sTiposDoc = null;
		List<String> lstDocs ;
		Session sesion = null;
		
		boolean newCn = false;
		StringBuilder sb = new StringBuilder();
		
		try{
			
			if(sLineas == null)
				return null;
			
			sesion = HibernateUtilPruebaCn.currentSession();
			
			sb.append( "select distinct (trim(f.id.cadoc)) ")
			.append(" from F55ca019 as f ")
			.append(" where f.id.castat = 'A' ")
			.append( SqlUtil.constructQuery(false, "trim(f.id.camcul)", true,
							sLineas, null));

			lstDocs = sesion.createQuery(sb.toString()).list();
			
			if(lstDocs != null && !lstDocs.isEmpty())
				sTiposDoc = lstDocs.toArray(new String[lstDocs.size()]);
			
		}catch(Exception ex){
			ex.printStackTrace(); 
		}
		return sTiposDoc;
	}
	
/*********************************************************************************************/
/************LEER TIPOS DE DOCUMENTOS DE LINEAS***********************************************/
	@SuppressWarnings("unchecked")
	public String[] obtenerTiposDeDocumento(String[] sLineas){
		String[] sTiposDoc = null;
		Session session = null;
		
		List<String> lstDocs = new ArrayList<String>();
		
		try{
			
			session = HibernateUtilPruebaCn.currentSession();
			
			String sql = "select distinct (trim(f.id.cadoc)) from F55ca019 as f " +
					"where f.id.castat = 'A' and trim(f.id.camcul) in ";
			
			StringBuilder sb = new StringBuilder(Arrays.toString(sLineas) );
			sb.replace(0, 1, "('");
			sb.replace(sb.length()-1, sb.length(), "')");
			sb = new StringBuilder(sb.toString().replace(", ", "','"));
			sql += sb.toString();

			LogCajaService.CreateLog("obtenerTiposDeDocumento", "QRY", sql);
			
			lstDocs = session.createQuery(sql).list();
			
			if(lstDocs != null && !lstDocs.isEmpty())
				sTiposDoc = lstDocs.toArray(new String[lstDocs.size()]);
			
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
		Transaction tx = null;
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
			
			tx = session.beginTransaction();
			sql = "select * from "+PropertiesSystem.ESQUEMA+".Hfactjdecon as f where f.fecha =" + iFechaActual +" and trim(f.estado)<>'A' and f.tipofactura in (";
			//String sql = "from Hfactjdecon as f where f.id.fecha =" + iFechaActual +" and f.id.subtotal > 0 and f.id.tipofactura in (";
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
			tx.commit();
		}catch(Exception ex){ 
			ex.printStackTrace();
		}finally {			
			try {HibernateUtilPruebaCn.closeSession(session); } catch (Exception e2) {e2.printStackTrace(); }
		}
	return lstFacturas;
}
/*********************************************************************************************/
/**LEER FACTURAS DEL DIA CON DETERMINADOS TIPOS DE DOCUMENTOS Y EL NUMERO DE FACTURA PARECIDO*********************/
	public List leerFacturasDelDiaxNumero(int iFechaActual,String[] sTipoDoc, String sNumeroFac, String sCodSuc){
		List lstFacturas = new ArrayList();
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			String sql = "from Hfactjdecon as f where f.id.fecha =" + iFechaActual +" and cast(f.id.nofactura as string) like '"+sNumeroFac+"%' and f.id.subtotal > 0 and f.id.tipofactura in (";
			for (int i = 0; i < sTipoDoc.length; i++){
				if (i == sTipoDoc.length - 1){
					sql = sql + "'" + sTipoDoc[i] + "'";
				}else{
					sql = sql + "'" + sTipoDoc[i] + "',";
				}
			}
			//sql = sql + ") order by f.id.fecha desc, f.id.hora desc ";
			sql = sql + ") and f.id.codsuc = '"+sCodSuc+ "' and trim(f.id.estado) = '' and f.id.nofactura not in(select rf.id.numfac from Recibofac as rf where rf.id.numfac = f.id.nofactura and rf.id.codcomp = f.id.codcomp and rf.id.tipofactura = f.id.tipofactura and trim(rf.codunineg) = trim(f.codunineg)) order by f.id.fecha desc, f.id.hora desc ";
			lstFacturas = session
			.createQuery(sql)			
			.list();
			tx.commit();
		}catch(Exception ex){
			ex.printStackTrace(); 
		}finally {			
			try {HibernateUtilPruebaCn.closeSession(session); } catch (Exception e2) {e2.printStackTrace(); }
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
			ex.printStackTrace();
		}		
		return lblNoCaja;
	}	
	public void setLblNoCaja(String lblNoCaja) {
		this.lblNoCaja = lblNoCaja;
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
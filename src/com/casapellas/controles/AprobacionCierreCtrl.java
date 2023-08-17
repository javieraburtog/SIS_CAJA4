package com.casapellas.controles;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.casapellas.entidades.A02factco;
import com.casapellas.entidades.Arqueofact;
import com.casapellas.entidades.Arqueorec;
import com.casapellas.entidades.Hfactura;
import com.casapellas.entidades.Recibodet;
import com.casapellas.entidades.Vdetallecambiorecibo;
import com.casapellas.entidades.Vhfactura;
import com.casapellas.entidades.Vrecibosxtipoegreso;
import com.casapellas.entidades.Vrecibosxtipompago;
import com.casapellas.entidades.Vreciboxdevolucion;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.util.Divisas;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.PropertiesSystem;


@SuppressWarnings({"rawtypes","unchecked"})
public class AprobacionCierreCtrl {
	
	
	public boolean isPagoOriginalMixto(int caid,String codcomp,int numrec,String moneda){
		boolean mixto = false;
		String sql = "";
		
		Session sesion = null;
		
		boolean bNuevaSesionCaja = false;
		
		try{

			sql = " select count( distinct(moneda) ) from "+PropertiesSystem.ESQUEMA+".recibodet ";
			sql += " where caid = "+caid+" and codcomp = '"+codcomp+"'";
			sql += " and numrec = "+numrec +" and mpago = '"+MetodosPagoCtrl.EFECTIVO+"'";
			
			sesion = HibernateUtilPruebaCn.currentSession();
			
			Integer cant = (Integer)sesion.createSQLQuery(sql).uniqueResult();
			if (cant > 1) 
				mixto = true;
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return mixto;
	}
	
	public List obtenerRecibosxcambiomixto(int iCaid, String sCodcomp,
			String sMoneda, Date dtFechaArqueo, Date dtHoraCierre, String sLstnumrec ) {
		
		List<Integer>numrecs = new  ArrayList<Integer>();
		List<Vdetallecambiorecibo> lstRec = new ArrayList<Vdetallecambiorecibo>();
		List<Vdetallecambiorecibo> lstRecibos = new ArrayList<Vdetallecambiorecibo>();
		Session sesion = null;
		
		
		try {
			
			sLstnumrec = sLstnumrec.substring(1, sLstnumrec.length()-1);
			for (String numero : sLstnumrec.split(",")) 
				numrecs.add(Integer.parseInt(numero.trim()));
			
			sesion = HibernateUtilPruebaCn.currentSession();
			
			Criteria crVdcr = sesion.createCriteria(Vdetallecambiorecibo.class);
			crVdcr.add(Restrictions.eq("id.caid", iCaid))
					.add(Restrictions.eq("id.codcomp", sCodcomp))
					.add(Restrictions.eq("id.monedafac", sMoneda))
					.add(Restrictions.eq("id.monedarec", sMoneda))
					.add(Restrictions.ne("id.monedacamb", sMoneda))
					.add(Restrictions.gt("id.cambio", BigDecimal.ZERO))
					.add(Restrictions.eq("id.tiporec", "CO"))
					.add(Restrictions.eq("id.mpago", MetodosPagoCtrl.EFECTIVO ))
					.add(Restrictions.le("id.hora", dtHoraCierre))
					.add(Restrictions.eq("id.fecha", dtFechaArqueo))
					.add(Restrictions.in("id.numrec", numrecs));

			lstRec = crVdcr.list();

			if ( lstRec != null && !lstRec.isEmpty() ) {
				int iTotalRec = 0;
				Criteria cr = null;
				Recibodet rd = null;
				iTotalRec = lstRec.size();

				for (int i = 0; i < iTotalRec; i++) {
					Vdetallecambiorecibo v = lstRec.get(i);
					
					if (v.getId().getCantmon() == 2) {
					
						cr = sesion.createCriteria(Recibodet.class);
						cr.add(Restrictions.eq("id.caid", v.getId().getCaid()));
						cr.add(Restrictions.eq("id.codcomp", v.getId().getCodcomp()));
						cr.add(Restrictions.eq("id.numrec", v.getId().getNumrec()));
						cr.add(Restrictions.eq("id.mpago", MetodosPagoCtrl.EFECTIVO ));
						cr.add(Restrictions.eq("id.moneda", v.getId().getMonedacamb()));
						rd = (Recibodet) cr.uniqueResult();

						if (rd != null) {
							if (rd.getMonto().compareTo(v.getId().getCambio()) == -1) {
								v.getId().setCambio( v.getId().getCambio()
													.subtract(rd.getMonto()));
							}
						}
						lstRecibos.add(v);
					} else {
						lstRecibos.add(v);
					}
				}
			}

		} catch (Exception error) {
			System.out .println("Error en ArqueoCajaCtrl.obtenerRecibosxcambiomixto ");
			error.printStackTrace();
		}
		return lstRecibos;
	}
	
	public 	List<Vreciboxdevolucion> obtenerRecxDevmonex3(int iCaid,
			String sCodcomp, String sMoneda, String sTiporec,
			Date dtFechaArqueo, Date dtHoraCierre, String sLstnumrec) {
		
		List<Vreciboxdevolucion> lstDev = new ArrayList<Vreciboxdevolucion>();
		int iFechajul = 0;
		Session sesion = null;
		
		List<Integer>numrecs = new  ArrayList<Integer>();
		
		try {

			iFechajul =  new FechasUtil().obtenerFechaJulianaDia(dtFechaArqueo);
		
			sLstnumrec = sLstnumrec.substring(1, sLstnumrec.length()-1);
			for (String numero : sLstnumrec.split(",")) 
				numrecs.add(Integer.parseInt(numero.trim()));

			sesion = HibernateUtilPruebaCn.currentSession();
			
			
			Criteria cr = sesion.createCriteria(Vreciboxdevolucion.class)
				.add(Restrictions.eq("id.caid", iCaid ))
				.add(Restrictions.eq("id.codcomp", sCodcomp))
				.add(Restrictions.eq("id.fecha",  dtFechaArqueo ))
				.add(Restrictions.eq("id.tiporec", sTiporec ))
				.add(Restrictions.eq("id.fechadev", iFechajul ))
				.add(Restrictions.lt("id.fechafact",iFechajul ))
				.add(Restrictions.eq("id.estado", ""))
				.add(Restrictions.eq("id.mpago", MetodosPagoCtrl.EFECTIVO  ))
				.add(Restrictions.ne("id.moneda", sMoneda))
				.add(Restrictions.eq("id.monfac", sMoneda))
				.add(Restrictions.le("id.hora", dtHoraCierre))
				.add(Restrictions.leProperty("id.montoapl", "id.montofact"))
				.add(Restrictions.in("id.numrec", numrecs));
		
			lstDev = cr.list();
			
		} catch (Exception error) {
			error.printStackTrace();
		}
		
		return lstDev;
	}
	
	public List obtenerRecxDevmonex2(boolean bIngreso, int iCaid,
			String sCodsuc, String sCodcomp, String sMoneda, String sTiporec,
			Date dtFechaArqueo, Date dtHoraCierre, String sLstnumrec ) {
		
		List<Vreciboxdevolucion> lstDev = new ArrayList<Vreciboxdevolucion>();
		int iFechajul = 0;
		Session sesion = null;
		
		List<Integer>numrecs = new  ArrayList<Integer>();
		
		try {
			
			iFechajul =  new FechasUtil().obtenerFechaJulianaDia(dtFechaArqueo);
			
			sLstnumrec = sLstnumrec.substring(1, sLstnumrec.length()-1);
			for (String numero : sLstnumrec.split(",")) 
				numrecs.add(Integer.parseInt(numero.trim()));

			sesion = HibernateUtilPruebaCn.currentSession();
			
			
			Criteria cr = sesion.createCriteria(Vreciboxdevolucion.class)
			.add(Restrictions.eq("id.caid", iCaid ))
			.add(Restrictions.eq("id.codcomp", sCodcomp))
			.add(Restrictions.eq("id.fecha",  dtFechaArqueo ))
			.add(Restrictions.eq("id.tiporec", sTiporec ))
			.add(Restrictions.eq("id.fechadev", iFechajul ))
			.add(Restrictions.lt("id.fechafact",iFechajul ))
			.add(Restrictions.eq("id.estado", ""))
			.add(Restrictions.eq("id.mpago", MetodosPagoCtrl.EFECTIVO  ))
			.add(Restrictions.eq("id.moneda", sMoneda))
			.add(Restrictions.ne("id.monfac", sMoneda))
			.add(Restrictions.le("id.hora", dtHoraCierre))
			.add(Restrictions.in("id.numrec", numrecs));

			lstDev = cr.list();

		} catch (Exception error) {
			error.printStackTrace();
		} 
		return lstDev;
	}
	
	public List obtenerRecibosxcambios(int iCaid, String sCodcomp,
			String sCodsuc, String sMoneda, Date dtFecha, Date dtHora,
			String sLstnumrec ) {
		List lstRec = new ArrayList();
		String sConsulta, sFecha, sHora;

		Session sesion = null;
		

		try {
			   sesion = HibernateUtilPruebaCn.currentSession();
			

			sHora = new SimpleDateFormat("HH.mm.ss").format(dtHora);
			sFecha = new SimpleDateFormat("yyyy-MM-dd").format(dtFecha);
			sConsulta = " from Vdetallecambiorec as v where v.id.caid = "
					+ iCaid;
			sConsulta += " and v.id.codsuc = '" + sCodsuc
					+ "' and v.id.codcomp = '" + sCodcomp + "'";
			sConsulta += " and v.id.monedacamb = '" + sMoneda
					+ "' and v.id.cambio > 0 and v.id.tiporec <> 'FCV'";
			sConsulta += " and v.id.fecha = '" + sFecha
					+ "' and v.id.hora <= '" + sHora + "' and v.id.numrec in "
					+ sLstnumrec;

			lstRec = sesion.createQuery(sConsulta).list();

		} catch (Exception error) {
			System.out
					.println("Error en ArqueoCajaCtrl.obtenerRecibosxCambios "
							+ error);
		}
		return lstRec;
	}
	public List cargarRecibosxMetodoPago(int iCaid, String sCodsuc,
			String sCodcomp, String sMoneda, Date dtFecha, Date dtHora,
			String sLstnumrec) {
		List<Vrecibosxtipompago> recibos = new ArrayList();
		String sConsulta, sFecha, sHora;

		Session sesion = null;
		

		try {
			  sesion = HibernateUtilPruebaCn.currentSession();
			
			
			sHora = new SimpleDateFormat("HH.mm.ss").format(dtHora);
			sFecha = new SimpleDateFormat("yyyy-MM-dd").format(dtFecha);

			sConsulta = " from Vrecibosxtipompago as v where v.id.caid = "
					+ iCaid + " and v.id.codsuc ='" + sCodsuc + "'";
			sConsulta += " and v.id.codcomp='" + sCodcomp
					+ "' and v.id.moneda = '" + sMoneda + "'";
			sConsulta += " and v.id.fecha = '" + sFecha
					+ "' and v.id.hora <= '" + sHora
					+ "' and v.id.tiporec <> 'FCV' ";
			sConsulta += " and v.id.numrec in "
					+ sLstnumrec
					+ "and lower(v.id.tiporec) not like 'd%' order by v.id.numrec";

			recibos = (ArrayList<Vrecibosxtipompago>)sesion.createQuery(sConsulta).list();
			
			/*if(recibos != null && !recibos.isEmpty()){
				CollectionUtils.forAllDo(recibos, new Closure(){
					public void execute(Object o) {
						Vrecibosxtipompago v= (Vrecibosxtipompago)o;
						 String normalized = Normalizer.normalize(v.getId().getCliente().trim(), Normalizer.Form.NFD);
						 Pattern pattern = Pattern.compile("\\P{ASCII}+");
						 v.getId().setCliente( pattern.matcher(normalized).replaceAll("") );
					}
				}) ;
			}
			*/
			
		} catch (Exception error) {
			 error.printStackTrace();
		}
		return recibos;
	}
	
	public List obtenerRecibosxcambiomixto(int iCaid, String sCodcomp,
			String sCodsuc, String sMoneda, Date dtFecha, Date dtHora,
			String sLstnumrec ) {
		
		List lstRec = new ArrayList();
		String sConsulta, sFecha, sHora;

		Session sesion = null;
		

		try {

			sesion = HibernateUtilPruebaCn.currentSession();
			

			sHora = new SimpleDateFormat("HH.mm.ss").format(dtHora);
			sFecha = new SimpleDateFormat("yyyy-MM-dd").format(dtFecha);

			sConsulta = " from Vdetallecambiorecibo as v where v.id.caid = "
					+ iCaid;
			sConsulta += " and v.id.codsuc = '" + sCodsuc
					+ "' and v.id.codcomp = '" + sCodcomp + "'";
			sConsulta += " and v.id.monedafac = '" + sMoneda
					+ "' and v.id.monedarec = '" + sMoneda + "'";
			sConsulta += " and v.id.cantmon = 1 and v.id.monedacamb <> '"
					+ sMoneda + "'";
			sConsulta += " and v.id.fecha = '" + sFecha
					+ "' and v.id.hora <= '" + sHora + "'";
			sConsulta += " and v.id.cambio > 0 and v.id.numrec in "
					+ sLstnumrec;
			sConsulta += " and v.id.tiporec not in ('EX','FCV')";

			lstRec = sesion.createQuery(sConsulta).list();

		} catch (Exception error) {
			error.printStackTrace();
		}
		return lstRec;
	}
	
	public List obtenerRecxDevmonex(boolean bIngreso, int iCaid,
			String sCodsuc, String sCodcomp, String sMoneda, String sTiporec,
			Date dtFecha, Date dtHora, String sLstnumrec ) {
		List lstDev = new ArrayList();
		String sConsulta, sFecha, sHora;
		int iFechajul = 0;

		Session sesion = null;
		

		try {

			sesion = HibernateUtilPruebaCn.currentSession();
			

			sHora = new SimpleDateFormat("HH.mm.ss").format(dtHora);
			sFecha = new SimpleDateFormat("yyyy-MM-dd").format(dtFecha);
			iFechajul = new FechasUtil().obtenerFechaJulianaDia(dtFecha);

			sConsulta = " from Vreciboxdevolucion as v where v.id.caid = "
					+ iCaid + " and v.id.codsuc = '" + sCodsuc + "'";
			sConsulta += " and v.id.codcomp = '" + sCodcomp
					+ "' and v.id.fecha = '" + sFecha + "' and v.id.hora <= '"
					+ sHora + "'";
			sConsulta += " and v.id.fechadev = " + iFechajul
					+ " and v.id.fechafact = " + iFechajul;
			
			sConsulta += " and v.id.montoapl < v.id.montofact " ;
			
			sConsulta += " and v.id.tiporec = '" + sTiporec
					+ "' and v.id.numrec in " + sLstnumrec;

			if (bIngreso)				
				sConsulta += " and v.id.moneda = '"+ sMoneda 
							+ "' and v.id.monfac <> '" + sMoneda + "'";	
//				sConsulta += " and v.id.mpago = '"+MetodosPagoCtrl.EFECTIVO+"' and v.id.moneda = '"
//						+ sMoneda + "' and v.id.monfac <> '" + sMoneda + "'";
			else
				sConsulta += " and v.id.moneda <> '" + sMoneda
						+ "' and v.id.monfac = '" + sMoneda + "'";

			lstDev = sesion.createQuery(sConsulta).list();

		} catch (Exception error) { 
			error.printStackTrace();
		}
		return lstDev;
	}
	
	public List obtenerIngEgrRecMonEx(boolean bIngreso, int iCaid,
			String sCodcomp, String sCodsuc, String sMoneda, Date dtFecha,
			Date dtHora, String sLstnumrec ) {
		List lstRecibos = new ArrayList();
		String sConsulta, sFecha, sHora;
		SimpleDateFormat format;

		Session sesion = null;
		

		try {

			sesion = HibernateUtilPruebaCn.currentSession();
		

			format = new SimpleDateFormat("HH.mm.ss");
			sHora = format.format(dtHora);

			format = new SimpleDateFormat("yyyy-MM-dd");
			sFecha = format.format(dtFecha);

			sConsulta = " from Vmonedafactrec as v where v.id.caid = " + iCaid
					+ " and v.id.codsuc ='" + sCodsuc + "'";
			sConsulta += " and trim(v.id.codcomp) = '" + sCodcomp.trim()
					+ "' and v.id.tiporec <> 'FCV'";
			sConsulta += " and v.id.fecha = '" + sFecha
					+ "' and v.id.horamod <= '" + sHora + "'";

			if (bIngreso)
				sConsulta += " and v.id.rmoneda = '" + sMoneda
						+ "'and v.id.fmoneda <> '" + sMoneda + "'";
			else
				sConsulta += " and v.id.rmoneda <> '" + sMoneda
						+ "'and v.id.fmoneda = '" + sMoneda + "'";

			sConsulta += " and v.id.forarecibido > 0";
			sConsulta += " and v.id.numrec in " + sLstnumrec
					+ " order by v.id.numrec";

			lstRecibos = sesion.createQuery(sConsulta).list();

		} catch (Exception error) {
			error.printStackTrace();
		}
		return lstRecibos;
	}
	public List cargarDetCambio(int iCaid, String sCodcomp, String sCodsuc,
			String sMoneda, Date dtFecha, Date dtHora, String sLstnumrec ) {
		List lstCambios = new ArrayList();
		Session sesion = null;
		

		try {

			sesion = HibernateUtilPruebaCn.currentSession();
			
			
			Criteria cr = sesion.createCriteria(Vdetallecambiorecibo.class);
			cr.add(Restrictions.eq("id.caid", iCaid));
			cr.add(Restrictions.eq("id.codcomp", sCodcomp));
			cr.add(Restrictions.eq("id.monedacamb", sMoneda));
			cr.add(Restrictions.gt("id.cambio", BigDecimal.ZERO));
			cr.add(Restrictions.eq("id.fecha", dtFecha));
			cr.add(Restrictions.eq("id.estado", ""));
			cr.add(Restrictions.ne("id.tiporec", "FCV"));
			cr.add(Restrictions.eq("id.mpago", MetodosPagoCtrl.EFECTIVO ));
			cr.add(Restrictions.le("id.hora", dtHora));
			cr.add(Restrictions.sqlRestriction(" numrec in " + sLstnumrec));
			lstCambios = cr.list();


		} catch (Exception error) {
			error.printStackTrace();
		}
		return lstCambios;
	}
	
	public List obtenerRecibosxdevolucion(boolean bEgresos, int iCaid,
			String sCodsuc, String sCodcomp, String sTiporec, String sMoneda,
			Date dtFecha, Date dtHora, String sLstNumrec) {
		List lstDev = new ArrayList();
		String sConsulta = "", sFecha = "", sHora = "";
		int iFechajul = 0;
		Calendar cal;
		SimpleDateFormat format;

		Session sesion = null;
		

		try {

			/*sesion = HibernateUtil.getSessionFactoryMCAJAR().openSession();*/   sesion = HibernateUtilPruebaCn.currentSession();
			
			
			format = new SimpleDateFormat("HH.mm.ss");
			sHora = format.format(dtHora);
			format = new SimpleDateFormat("yyyy-MM-dd");
			sFecha = format.format(dtFecha);
			iFechajul = new FechasUtil().obtenerFechaJulianaDia(dtFecha);

			sConsulta = " from Vreciboxdevolucion as v where v.id.caid = "
					+ iCaid + " and v.id.codcomp = '" + sCodcomp + "'";
			sConsulta += " and v.id.codsuc = '" + sCodsuc
					+ "' and v.id.fecha = '" + sFecha + "' and v.id.hora <= '"
					+ sHora + "'";
			sConsulta += " and v.id.fechadev =" + iFechajul; // +
																// " and v.id.fechafact = "
																// +iFechajul;
			sConsulta += " and v.id.tiporec = '" + sTiporec
					+ "'and v.id.moneda = '" + sMoneda + "'";
			sConsulta += " and v.id.numrec in " + sLstNumrec;

			if (bEgresos)
				sConsulta += " and v.id.monfac = '" + sMoneda + "'";

			lstDev = sesion.createQuery(sConsulta).list();

		} catch (Exception error) {
			System.out
					.println("Error en arqueoCajaCtrl.obtenerRecibosxdevolucion "
							+ error);
		}
		return lstDev;
	}

	
	public static List<Vrecibosxtipoegreso>  obtenerRecibosCierre(int caid, String codcomp, 
				String moneda, Date fechaarqueo, String num_recibos){
		List<Vrecibosxtipoegreso> recibospago = null;
		Session sesion = null;
		Transaction trans = null;
		boolean newCn = false;
		
		try {
			
			String sql_Recibos = " select r.numrec,r.caid,r.codcomp,r.codsuc, r.fecha rfecha, " +
					" r.hora, r.montoapl, rd.monto,  rd.mpago,  rd.equiv,  rd.tasa," +
					" rd.moneda rmoneda, rd.moneda fmoneda,  r.tiporec, r.cliente, " +
					" rd.refer1, rd.refer2, rd.refer3, rd.refer4, r.estado, " +
					" r.codcli, rd.referencenumber, rd.depctatran, rd.codigomarcatarjeta, rd.marcatarjeta" + 
					
					" from " +
					PropertiesSystem.ESQUEMA+".recibo r inner join " +
					PropertiesSystem.ESQUEMA+".recibodet rd " +
					" on (r.numrec = rd.numrec and r.caid = rd.caid and " +
					" r.codcomp = rd.codcomp and r.tiporec = rd.tiporec)" +
					" where lower(r.tiporec) in ('pr',  'ex', 'co', 'fn', 'cr', 'pm')" +
					" and r.caid = " +caid +" and r.codcomp = '"+codcomp+ 
					"' and r.estado = '' and rd.moneda = '"+moneda+"' and r.numrec in " +num_recibos;
				
			
			sesion = HibernateUtilPruebaCn.currentSession();
					
			recibospago = (ArrayList<Vrecibosxtipoegreso>) sesion.createSQLQuery(sql_Recibos).addEntity(Vrecibosxtipoegreso.class).list();	
			
			if(recibospago == null || recibospago.isEmpty() )
				return null ;		
			
			/*
			String sql_Devoluciones = " select nodoco, tipodoco, mpago, monto from " +
				PropertiesSystem.ESQUEMA+".recibo r inner join " +
				PropertiesSystem.ESQUEMA+".recibodet rd " +
				" on (r.numrec = rd.numrec and r.caid = rd.caid and " +
				" r.codcomp = rd.codcomp and r.tiporec = rd.tiporec)" +
				" where nodoco <> 0 and tipodoco <> '' and r.caid = " +caid +
				" and r.codcomp = '"+codcomp+ "' and r.estado = ''" +
				" and rd.moneda = '"+moneda+ "' and fecha = '" +
				FechasUtil.formatDatetoString(fechaarqueo, "yyy-MM-dd")+"'";
			
			List<Object[]> rec_devs = (ArrayList<Object[]>)	sesion.createSQLQuery(sql_Devoluciones).list();			
			
			if(rec_devs != null && !rec_devs.isEmpty()) {
				
				for (final Vrecibosxtipoegreso vr : recibospago) {
				 
					Object[] rdv = (Object[])
					CollectionUtils.find(rec_devs, new Predicate(){
						public boolean evaluate(Object o) {
							Object[] rdv = (Object[])o;
							return
								vr.getId().getNumrec() == Integer.parseInt(String.valueOf(rdv[0])) && 	
								vr.getId().getTiporec().compareTo(String.valueOf(rdv[1])) == 0 &&
								vr.getId().getMpago().compareTo(String.valueOf(rdv[2])) == 0;
						}}) ;
					
					if(rdv == null)
						continue;
					
					vr.getId().setMonto( Divisas.roundBigDecimal( vr.getId().getMonto().subtract(new BigDecimal(String.valueOf(rdv[3])  ) ) ) );
				
				}
			}
			*/
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return recibospago;
	}
	
	
	public List obtenerRecpagBanco(int iCaid, String sCodcomp,
			String sMoneda, Date dtFechaArqueo, Date dtHoraCierre, String sLstnumrec ) {
		
		List<Integer>numrecs = new  ArrayList<Integer>();
		List<Vrecibosxtipoegreso> lstRecibos = new ArrayList<Vrecibosxtipoegreso>();
		Session sesion = null;
		
		
		try {
			
			sLstnumrec = sLstnumrec.substring(1, sLstnumrec.length()-1);
			for (String numero : sLstnumrec.split(",")) 
				numrecs.add(Integer.parseInt(numero.trim()));
			
			sesion = HibernateUtilPruebaCn.currentSession();
			

			Criteria cr = sesion.createCriteria(Vrecibosxtipoegreso.class)
				.add(Restrictions.eq("id.caid", iCaid))
				.add(Restrictions.eq("id.codcomp", sCodcomp))
				.add(Restrictions.eq("id.rmoneda", sMoneda))
				.add(Restrictions.eq("id.estado", ""))
				.add(Restrictions.le("id.hora", dtHoraCierre))
				.add(Restrictions.eq("id.rfecha", dtFechaArqueo))
				.add(Restrictions.ne("id.tiporec", "FCV"))
				.add(Restrictions.in("id.numrec", numrecs));
			
			lstRecibos = (ArrayList<Vrecibosxtipoegreso>) cr.list();
			String sReciboin = "";

			if (lstRecibos != null && lstRecibos.size() > 0) {
				List<Recibodet> lstRecibodet = new ArrayList<Recibodet>();

				String sql1 = "";
				String sql = "select rd.* ";
				sql = sql.concat(" from "+PropertiesSystem.ESQUEMA+".recibo r inner join " +
									""+PropertiesSystem.ESQUEMA+".recibodet rd on ");
				sql = sql.concat(" r.caid = rd.caid and r.codcomp = " +
						"rd.codcomp and ");
				sql = sql.concat(" r.codsuc = rd.codsuc and r.tiporec =" +
						" rd.tiporec and r.numrec = rd.numrec");
				sql = sql.concat(" and r.caid = ")
						.concat(String.valueOf(iCaid));
				sql = sql.concat(" and r.codcomp = '" + sCodcomp + "'");
				sql = sql.concat(" and rd.moneda = '" + sMoneda + "'");
				sql = sql.concat(" and r.estado = '' and r.tiporec<>'FCV'");

				for (int i = 0; i < lstRecibos.size(); i++) {
					Vrecibosxtipoegreso v = lstRecibos.get(i);

					if (sReciboin.contains(v.getId().getNumrec() + "@,"))
						continue;
					sReciboin += v.getId().getNumrec() + "@,";

					sql1 = sql;
					sql1 = sql1.concat(" and r.nodoco = ").concat(
							String.valueOf(v.getId().getNumrec()));
					sql1 = sql1.concat(" and r.tipodoco = '")
							.concat(v.getId().getTiporec()).concat("'");
					sql1 = sql1.concat(" and rd.mpago = '"
							+ v.getId().getMpago() + "'");

					lstRecibodet = (ArrayList<Recibodet>) sesion.createSQLQuery(sql1)
									.addEntity(Recibodet.class).list();

					if (lstRecibodet != null && lstRecibodet.size() > 0) {
						for (Recibodet rd : lstRecibodet) {
							BigDecimal bdMonto = v.getId().getMonto();
							bdMonto = bdMonto.subtract(rd.getMonto());
							v.getId().setMonto(  Divisas.roundBigDecimal(bdMonto));
							lstRecibos.set(i, v);
						}
					}
				}
			}
		} catch (Exception error) {
			error.printStackTrace();
		}
		return lstRecibos;
	}

	public static String cargarRecibosArqueo(int noarqueo,int caid,String codcomp,String codsuc ){
		String sLstRecibos = "";
		Session sesion = null;
		
		
		try{
			sesion = HibernateUtilPruebaCn.currentSession();
			
			Criteria cr = sesion.createCriteria(Arqueorec.class)
				.add(Restrictions.eq("id.noarqueo", noarqueo))
				.add(Restrictions.eq("id.caid", caid))
				.add(Restrictions.eq("id.codcomp", codcomp ))
				.add(Restrictions.eq("id.codsuc",  codsuc ))
				.add(Restrictions.in("id.tipodoc", new String[]{"R","S"}))
				.setProjection(Projections.property("id.numrec"));
			
			LogCajaService.CreateLog("cargarRecibosArqueo", "HQRY", LogCajaService.toSql(cr));
			
			List<Integer> lstArqueorec  =(ArrayList<Integer>)cr.list();
			
			if(lstArqueorec != null && !lstArqueorec.isEmpty()){
				StringBuilder sb = new StringBuilder(lstArqueorec.toString());
				sb.setCharAt(0, '(');
				sb.setCharAt(sb.length()-1, ')');
				sLstRecibos = sb.toString();
			}
			
		}catch(Exception error){ 
			LogCajaService.CreateLog("cargarRecibosArqueo", "ERR", error.getMessage());
		}
		return sLstRecibos;
	}	
	
	
	public static List<Hfactura> cargarFacturasArqueo(int noarqueo,int caid,String codcomp){
		Session sesion = null;
		
		
		List<Hfactura> facturas = null;
		
		try {
			sesion = HibernateUtilPruebaCn.currentSession();
			
			String sql = "select a.* from "+PropertiesSystem.ESQUEMA+".arqueofact af " +
				" inner join "+PropertiesSystem.ESQUEMA+".a02factco a on af.numfac = a.nofactura" +
				" and af.codcomp = a.codcomp and af.codcli = a.codcli " +
				" and af.fecha = a.fecha and af.tipo = a.tipofactura " +
				" and af.codunineg = a.codunineg " +
				" where a.estado = '' and af.caid = "+caid+" and af.noarqueo = "+noarqueo+
				" and af.codcomp = '"+codcomp+"' ";		
					
			LogCajaService.CreateLog("cargarFacturasArqueo", "HQRY", sql);
			
			List<A02factco> facturaA02 = (ArrayList<A02factco>)sesion.createSQLQuery(sql).addEntity(A02factco.class).list();
			
			if(facturaA02 == null || facturaA02.isEmpty() )
				return null;
			
			facturas = FacturaCrtl.copyA02factcoToHfactura(facturaA02);
			
		} catch (Exception e) {
			LogCajaService.CreateLog("cargarFacturasArqueo", "ERR", e.getMessage());
		}finally{
			
			if(facturas == null) 
				facturas = new ArrayList<Hfactura>(1);
		}
		return facturas;
	}
	
	
	public List<Vhfactura> cargarFacturasArqueo(int noarqueo,int caid,String codcomp,String codsuc ){
		List<Arqueofact> lstArqueofact = new ArrayList<Arqueofact>();
		List<Vhfactura> lstVhfact = new ArrayList<Vhfactura>();
		String consulta = "";
		Session sesion = null;
		
		
		try{ 
			sesion = HibernateUtilPruebaCn.currentSession();
			
			
			lstArqueofact = (ArrayList<Arqueofact>)sesion
					.createCriteria(Arqueofact.class)
					.add(Restrictions.eq("id.noarqueo", noarqueo))
					.add(Restrictions.eq("id.caid", caid))
					.add(Restrictions.eq("id.codcomp", codcomp))
					.list();
					
			if(lstArqueofact == null) 
				lstArqueofact = new ArrayList();
			
			
		}catch(Exception error){
			error.printStackTrace();
		}finally{
		
			for(int i=0; i<lstArqueofact.size();i++){
				Arqueofact a = (Arqueofact) lstArqueofact.get(i);
				
				Vhfactura vh = obtenerVhfactura(a.getId().getNumfac(), a
						.getId().getTipo(), a.getId().getCodunineg().trim(),
						codcomp, codsuc, a.getId().getFecha(), a.getId()
								.getCodcli());
				
				if(vh == null)continue;
				
				lstVhfact.add(vh);
			}
		}
		return lstVhfact;
	}
	
	public Vhfactura obtenerVhfactura(int nofact,String tipofact,String sCodunineg, String codcomp,String codsuc, int iFecha, int iCodcli){		
		Vhfactura factura = null;		
		String consulta = "";
		Session sesion = null;
		
		
		try{
			sesion = HibernateUtilPruebaCn.currentSession();			
			
			consulta =  " from Vhfactura as f where f.id.nofactura = "+nofact;
			consulta += " and f.id.codcomp = '"+codcomp.trim()+"' and trim(f.id.codunineg) = '"+sCodunineg+"' ";
			consulta += " and f.id.tipofactura ='"+tipofact.trim()+"'";
			consulta += " and f.id.fecha = "+iFecha +" and f.id.codcli = "+iCodcli;
			
			Object ob = sesion.createQuery(consulta).uniqueResult();
			
			if(ob!=null) factura = (Vhfactura)ob;
			
		}catch(Exception error){
			error.printStackTrace();
		}
		return factura;
	}
	
	
}

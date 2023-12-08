package com.casapellas.controles;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.casapellas.entidades.F55ca017;
import com.casapellas.entidades.Recibodet;
import com.casapellas.entidades.Trasladofac;
import com.casapellas.entidades.Vdetallecambiorec;
import com.casapellas.entidades.Vdetallecambiorecibo;
import com.casapellas.entidades.Vfacturaxdevolucion;
import com.casapellas.entidades.Vhfactura;
import com.casapellas.entidades.Vmonedafactrec;
import com.casapellas.entidades.Vrecibosxtipoegreso;
import com.casapellas.entidades.Vrecibosxtipoingreso;
import com.casapellas.entidades.Vrecibosxtipompago;
import com.casapellas.entidades.Vreciboxdevolucion;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.util.Divisas;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.PropertiesSystem;
import com.casapellas.util.SqlUtil;

@SuppressWarnings( {"unchecked", "rawtypes"})

public class ArqueoControl {
	
	
	public static List selectProperty(Collection from, String propertyName) {
		List result = new ArrayList();
		try {
			for(Object o : from) {
				if(o == null) 
					continue;
				Object value = PropertyUtils.getSimpleProperty(o, propertyName);
				if(result.contains(value)) 
					continue;
				result.add(value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	public List<Vhfactura> obtenerFacturas(final String[] sTiposDoc , F55ca017[] f55ca017, 
			List lstLocalizaciones, int iCaid, final String sMoneda, 
			String sCodsuc,final String sCodcomp, Date dtFechaArqueo,
			Date dtHoraInicio, Date dtHoraFin){
		
		Session sesion = null;
		
		boolean bHayLoc = false;
		boolean bSoloLocs = false;
		
		List<String> f17_sinLocalizacion = new ArrayList<String>();
		final List<String> f17_conLocalizacion = new ArrayList<String>();
		final List<String> lstLocalzcns = new ArrayList<String>();
		
		List<Vhfactura> lstFactsConLocal  = new ArrayList<Vhfactura>();
		List<Vhfactura> lstFctsSinLocal = new ArrayList<Vhfactura>();
		List<Vhfactura> lstFctsTrlsIn = new ArrayList<Vhfactura>();
		
		try {
			
			//---- separar los que tienen localizaciones 
			for (F55ca017 f17 : f55ca017) {
				if(f17.getId().getC7locn().trim().compareTo("") == 0){
					f17_sinLocalizacion.add(f17.getId().getC7mcul());
					bSoloLocs = true;
				}else{
					bHayLoc = true;
					if(!lstLocalzcns.contains(f17.getId().getC7locn().trim())) 
						lstLocalzcns.add(f17.getId().getC7locn().trim());
					if(!f17_conLocalizacion.contains(f17.getId().getC7mcul())) 
						f17_conLocalizacion.add(f17.getId().getC7mcul());
				}
			}
			//&& ===== Obtener las localizaciones asociadas a la unidad de negocio.
			String sConditionLocs = "";
			for (final String codunineg : f17_conLocalizacion) {

				List<F55ca017> locs = (ArrayList<F55ca017>) CollectionUtils
						.select(Arrays.asList(f55ca017), new Predicate() {
							public boolean evaluate(Object F17) {
								F55ca017 f = (F55ca017) F17;
								return f.getId().getC7mcul().trim()
										.compareTo(codunineg.trim()) == 0
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
				sConditionLocs += " ( trim(codunineg) ='" + codunineg.trim()
						+ "' and trim(sdlocn) in " + val + " )\n";
			}
			
			final int iFechaActual = FechasUtil.DateToJulian(dtFechaArqueo) ;
			String dia = new SimpleDateFormat("dd-MM-yyyy").format(dtFechaArqueo) ;
			String ini = new SimpleDateFormat("HH:mm:ss").format(dtHoraInicio);
			String fin = new SimpleDateFormat("HH:mm:ss").format(dtHoraFin);
			
			//&& ========= Consulta que se trae las facturas.
			Date dtInicio = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(dia+" "+ ini);
			Date dtFinal  = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(dia+" "+ fin);
			final Integer iHoraini = Integer.parseInt(new SimpleDateFormat("HHmmss").format(dtHoraInicio));
			final Integer iHoraFin = Integer.parseInt(new SimpleDateFormat("HHmmss").format(dtHoraFin));
			
			sesion = HibernateUtilPruebaCn.currentSession();
			
			//&& ========= Consulta de facturas por traslados enviados
			Criteria crTrlsdEnviados = sesion.createCriteria(Trasladofac.class);
			crTrlsdEnviados.setProjection(Projections.distinct(Projections.property("nofactura")))
			.add(Restrictions.eq("codcomp", sCodcomp ))
			.add(Restrictions.eq("caidprop", iCaid))
			.add(Restrictions.eq("moneda", sMoneda ))
			.add(Restrictions.between("fecha", dtInicio, dtFinal))
			.add(Restrictions.eq("fechafac", iFechaActual))
			.add(Restrictions.in("estadotr", new String[]{"E","R","P"} ));
			
			LogCajaService.CreateLog("obtenerFacturas", "HQRY", LogCajaService.toSql(crTrlsdEnviados));
			List<Integer> lstTrasladosSend = crTrlsdEnviados.list();

			Criteria crTrlsRecibidos = sesion.createCriteria(Trasladofac.class)
				.add(Restrictions.eq("codcomp", sCodcomp ))
				.add(Restrictions.eq("caiddest", iCaid))
				.add(Restrictions.eq("moneda", sMoneda ))
				.add(Restrictions.eq("fechafac", iFechaActual))
				.add(Restrictions.between("fecha", dtInicio, dtFinal))
				.add(Restrictions.in("estadotr", new String[]{"E","P"} ));
			
			LogCajaService.CreateLog("obtenerFacturas", "HQRY", LogCajaService.toSql(crTrlsRecibidos));
			
			List<Trasladofac>trasladosrec = crTrlsRecibidos.list();
			List<Integer> lstTrsldosRcibdos = null;
			List<String> lstSucsxTrlsRec = null;
			
			//&& ======== buscar las facturas completas por traslados recibidos.
			if(trasladosrec != null & !trasladosrec.isEmpty() ){
				
				lstTrsldosRcibdos = selectProperty(trasladosrec, "nofactura");
				lstSucsxTrlsRec   = selectProperty(trasladosrec, "codsuc");
				
				//Reformatear el arreglo
				ArrayList<String> lstSucsxTrlsRec2 = new ArrayList<String>();
				for (int i = 0; i < lstSucsxTrlsRec.size(); i++) {
					lstSucsxTrlsRec2.add(String.format("%05d", Integer.parseInt(lstSucsxTrlsRec.get(i))));
				}
				
				
				Criteria crFctsTrls = sesion.createCriteria(Vhfactura.class)
						.add(Restrictions.in("id.nofactura", lstTrsldosRcibdos))
						.add(Restrictions.in("id.codsuc", lstSucsxTrlsRec2))
						.add(Restrictions.eq("id.estado", ""))
						.add(Restrictions.eq("id.codcomp", sCodcomp))
						.add(Restrictions.eq("id.moneda", sMoneda))
						.add(Restrictions.eq("id.fecha", iFechaActual))
						.add(Restrictions.in("id.tipofactura", sTiposDoc))
						.add(Restrictions.between("id.hora", iHoraini, iHoraFin ));
				
				LogCajaService.CreateLog("obtenerFacturas", "HQRY", LogCajaService.toSql(crFctsTrls));
				lstFctsTrlsIn = crFctsTrls.list();
			}
			
			//&& ======== Crear la factura Numero 2, para las unidades de negocio con localizacion.
			DetachedCriteria cr2 = DetachedCriteria.forClass(Vhfactura.class)
					.add(Restrictions.eq("id.fecha", iFechaActual))
					.add(Restrictions.in("id.tipofactura", sTiposDoc))
					.add(Restrictions.eq("id.estado", ""))
					.add(Restrictions.eq("id.codcomp", sCodcomp))
					.add(Restrictions.eq("id.moneda", sMoneda))
					.add(Restrictions.eq("id.fecha", iFechaActual)) ///ELIMINAR
					.add(Restrictions.between("id.hora", iHoraini, iHoraFin ))
					.add(Restrictions.sqlRestriction( " ( " +sConditionLocs +" )" ));

			if(lstTrasladosSend != null && !lstTrasladosSend.isEmpty())
				cr2.add(Restrictions.not((Restrictions.in("id.nofactura", lstTrasladosSend)))); 
			
			//&& ===== Solamente incluir traslados de localizaciones no configuradas.
			if(lstTrsldosRcibdos != null && !lstTrsldosRcibdos.isEmpty()) {
				ArrayList<Integer> lstNoLocsConfig = new ArrayList<Integer>();
				
				for (Vhfactura v : lstFctsTrlsIn)
					if( !lstLocalzcns.contains(v.getId().getSdlocn()) )
						lstNoLocsConfig.add(v.getId().getNofactura());
				
				if(!lstNoLocsConfig.isEmpty())
					cr2.add(Restrictions.not((Restrictions
							.in("id.nofactura", lstNoLocsConfig))));
			}
			if(bHayLoc) 
				lstFactsConLocal = cr2.getExecutableCriteria(sesion).list();
			
			if(bSoloLocs){
				
				Criteria cr1 = sesion.createCriteria(Vhfactura.class)
				.add(Restrictions.eq("id.fecha", iFechaActual))
				.add(Restrictions.in("id.tipofactura", sTiposDoc))
				.add(Restrictions.eq("id.sdlocn", ""))
				.add(Restrictions.in("id.codunineg", f17_sinLocalizacion))
				
				.add(Restrictions.eq("id.estado", ""))
				.add(Restrictions.eq("id.codcomp", sCodcomp))
				.add(Restrictions.eq("id.moneda", sMoneda))
				.add(Restrictions.eq("id.fecha", iFechaActual))
				.add(Restrictions.between("id.hora", iHoraini, iHoraFin ));
				
				if(lstTrasladosSend != null && !lstTrasladosSend.isEmpty())
					cr1.add(Restrictions.not((Restrictions.in("id.nofactura", lstTrasladosSend))));
				
				LogCajaService.CreateLog("obtenerFacturas", "HQRY", LogCajaService.toSql(cr1));
				
				lstFctsSinLocal = cr1.list();
				if(lstFctsSinLocal == null) lstFctsSinLocal = new ArrayList<Vhfactura>();
				
			}
			//&& ======= Agregar los traslados recibidos.
			if(lstFctsTrlsIn != null && !lstFctsTrlsIn.isEmpty())
				lstFctsSinLocal.addAll(lstFctsTrlsIn);
			
			//&& ======= Agregar las facturas con localizaciones.
			if(lstFactsConLocal != null && !lstFactsConLocal.isEmpty())
				lstFctsSinLocal.addAll(lstFactsConLocal);
			
			lstFctsSinLocal = (List<Vhfactura>) CollectionUtils.select(
					lstFctsSinLocal, new Predicate() {
						public boolean evaluate(Object o) {
							return( !((Vhfactura) o).getId().getPantalla()
									.trim().toLowerCase().contains("pocket")
									);
						}
					});
			
			if(lstFctsSinLocal != null && lstFctsSinLocal.size() > 0 ){
				CollectionUtils.filter(lstFctsSinLocal, new Predicate() {
					public boolean evaluate(Object arg0) {
						Vhfactura hfac = (Vhfactura)arg0;
						return ( hfac.getId().getSubtotal() > 0 && 
								 hfac.getId().getTotal() > 0 );
					}
				});
			}
			
		} catch (Exception e) {
			if(lstFctsSinLocal == null)
				lstFctsSinLocal = new ArrayList<Vhfactura>();
			LogCajaService.CreateLog("obtenerFacturas", "ERR", e.getMessage());
		}

		return lstFctsSinLocal;
	}
	
	public List obtenerRecibosxcambios(int iCaid, String sCodcomp,
			String sCodsuc, String sMoneda, Date dtFechaArqueo,
			Date dtHoraInicio, Date dtHoraFin) {
		List<Vdetallecambiorec> lstRec = new ArrayList<Vdetallecambiorec>();
		Session sesion = null;
		try {
			
			sesion = HibernateUtilPruebaCn.currentSession();
			
			Criteria cr = sesion.createCriteria(Vdetallecambiorec.class);
			cr.add(Restrictions.eq("id.caid", iCaid));
			cr.add(Restrictions.eq("id.codsuc", sCodsuc));
			cr.add(Restrictions.eq("id.codcomp", sCodcomp));
			cr.add(Restrictions.eq("id.monedacamb", sMoneda));
			cr.add(Restrictions.ne("id.estado", "A"));
			cr.add(Restrictions.eq("id.fecha", dtFechaArqueo));
			cr.add(Restrictions.eq("id.fecha", dtFechaArqueo));
			cr.add(Restrictions.gt("id.cambio", BigDecimal.ZERO));
			cr.add(Restrictions.ne("id.tiporec", "FCV"));
			cr.add(Restrictions.between("id.hora", dtHoraInicio, dtHoraFin));
			
			LogCajaService.CreateLog("obtenerRecibosxdevolucion", "HQRY", LogCajaService.toSql(cr));

			lstRec = (ArrayList<Vdetallecambiorec>) cr.list();

		} catch (Exception error) {
			LogCajaService.CreateLog("obtenerRecibosxdevolucion", "ERR", error.getMessage());
		}

		return lstRec;
	}
	
	public List cargarRecibosxMetodoPago(int iCaid, String sCodsuc,
			String sCodcomp, String sMoneda, Date dtFechaArqueo,
			Date dtHoraInicio, Date dtHoraFin) {
		List<Vrecibosxtipompago> recibos = new ArrayList<Vrecibosxtipompago>();
		String sNotIN[] = new String[] { "FCV" }; // "DCO", "FCV"

		Session sesion = null;
		
		try {

		    sesion = HibernateUtilPruebaCn.currentSession();
			 
			Criteria cr = sesion.createCriteria(Vrecibosxtipompago.class);
			cr.add(Restrictions.eq("id.caid", iCaid));
			cr.add(Restrictions.eq("id.codsuc", sCodsuc));
			cr.add(Restrictions.eq("id.codcomp", sCodcomp));
			cr.add(Restrictions.eq("id.moneda", sMoneda));
			cr.add(Restrictions.eq("id.fecha", dtFechaArqueo));
			cr.add(Restrictions.ne("id.estado", "A"));
			cr.add(Restrictions.between("id.hora", dtHoraInicio, dtHoraFin));
			cr.add(Restrictions.not(Restrictions.in("id.tiporec", sNotIN)));
			cr.addOrder(Order.asc("id.numrec"));
			
			LogCajaService.CreateLog("cargarRecibosxMetodoPago", "HQRY", LogCajaService.toSql(cr));
			recibos = (ArrayList<Vrecibosxtipompago>) cr.list();
			
		} catch (Exception error) {
			LogCajaService.CreateLog("cargarRecibosxMetodoPago", "ERR", error.getMessage());
		}

		return recibos;
	}
	

	public List obtenerRecxDevmonex2(boolean bIngreso, int iCaid,
			String sCodsuc, String sCodcomp, String sMoneda, String sTiporec,
			Date dtFechaArqueo, Date dtHoraInicio, Date dtHoraFin) {
		
		List lstDev = new ArrayList();
		String sConsulta = "", sFecha = "", sHini, sHfin;
		int iFechajul = 0;
		FechasUtil f = new FechasUtil();
		Session sesion = null;
		
		try {
			sesion = HibernateUtilPruebaCn.currentSession();

			iFechajul = f.obtenerFechaJulianaDia(dtFechaArqueo);
			sFecha = f.formatDatetoString(dtFechaArqueo, "yyyy-MM-dd");

			sConsulta = " from Vreciboxdevolucion as v where v.id.caid = " + iCaid + " and v.id.codsuc = '" + sCodsuc + "'";
			sConsulta += " and v.id.codcomp = '" + sCodcomp + "' and v.id.fecha = '" + sFecha + "' and v.id.tiporec = '" + sTiporec + "'";
			sConsulta += " and v.id.fechadev = " + iFechajul +" and v.id.fechafact < "+iFechajul + " and v.id.estado = '' ";

			
			sConsulta += " and v.id.mpago = '"+MetodosPagoCtrl.EFECTIVO+"' ";
			
			sConsulta += " and v.id.moneda = '" + sMoneda
						+ "' and v.id.monfac <> '" + sMoneda + "'";

			sHini = f.formatDatetoString(dtHoraInicio, "HH.mm.ss");
			sHfin = f.formatDatetoString(dtHoraFin, "HH.mm.ss");
			sConsulta += " and v.id.hora >='" + sHini + "' and v.id.hora<='"
					+ sHfin + "'";

			LogCajaService.CreateLog("obtenerRecxDevmonex2", "QRY", sConsulta);
			
			lstDev = sesion.createQuery(sConsulta).list();

		} catch (Exception error) {
			LogCajaService.CreateLog("obtenerRecxDevmonex2", "ERR", error.getMessage());
		}

		return lstDev;
	}
	
	public List<Vreciboxdevolucion>  obtenerDevParcialMonForanea(int iCaid,String sCodsuc,String sCodcomp,
											String sMoneda,	String sTiporec,Date dtFechaArqueo,
											Date dtHoraInicio,Date dtHoraFin){
		
		List<Vreciboxdevolucion> lstDevol = new ArrayList<Vreciboxdevolucion>();
		List<Vreciboxdevolucion> lstDev = new ArrayList<Vreciboxdevolucion>();
		String sConsulta = "",sFecha = "";
		int iFechajul = 0;
		FechasUtil f = new FechasUtil();
		
		Session sesion = null;
		
		try {

			sesion = HibernateUtilPruebaCn.currentSession();

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
			
			LogCajaService.CreateLog("obtenerDevParcialMonForanea", "QRY", sConsulta);
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
					
					cr.add(Restrictions.sqlRestriction( 						
							"(select count(*) from "+PropertiesSystem.ESQUEMA+".recibodet "+
							" where caid = "+v.getId().getCaid()+
							" and codcomp = '"+v.getId().getCodcomp() +
							" ' and numrec = "+v.getId().getNumrec() +
							" and tiporec = '"+v.getId().getTiporec() +"' ) = 1" ));

					LogCajaService.CreateLog("obtenerDevParcialMonForanea", "HQRY", LogCajaService.toSql(cr));
					
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
						
						LogCajaService.CreateLog("obtenerDevParcialMonForanea", "HQRY", LogCajaService.toSql(cr));
						
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
			LogCajaService.CreateLog("obtenerDevParcialMonForanea", "ERR", e.getMessage());
		}

		return  lstDevol;
	}
	
	public List obtenerRecxDevmonex4(boolean bIngreso, int iCaid,
			String sCodsuc, String sCodcomp, String sMoneda, String sTiporec,
			Date dtFechaArqueo, Date dtHoraInicio, Date dtHoraFin){
			
		List<Vreciboxdevolucion> lstDev = new ArrayList();
		List<Vreciboxdevolucion> lstReal = new ArrayList<Vreciboxdevolucion>();

		Session sesion = null;

		int iFechajul = 0;
		boolean mixto = false;
		
		try {
			sesion = HibernateUtilPruebaCn.currentSession();

			iFechajul = new FechasUtil().obtenerFechaJulianaDia(dtFechaArqueo);
			
			lstDev = (ArrayList<Vreciboxdevolucion>)sesion.
					createCriteria(Vreciboxdevolucion.class)
						.add(Restrictions.eq("id.caid", iCaid))
						.add(Restrictions.eq("id.codsuc", sCodsuc))
						.add(Restrictions.eq("id.codcomp",sCodcomp ))
						.add(Restrictions.eq("id.fecha", dtFechaArqueo))
						.add(Restrictions.eq("id.tiporec", sTiporec))
						.add(Restrictions.eq("id.fechadev",iFechajul ))
						.add(Restrictions.eqProperty("id.fechafact", "id.fechadev"))
						.add(Restrictions.eq("id.fechafact", iFechajul))
						.add(Restrictions.ne("id.monfac", sMoneda))
						.add(Restrictions.eq("id.moneda", sMoneda))
						.add(Restrictions.eq("id.mpago",MetodosPagoCtrl.EFECTIVO  ))
						.add(Restrictions.between("id.hora",dtHoraInicio,dtHoraFin ))
						.add(Restrictions.eq("id.estado",new String())).list() ;

			// && ====== Validar que las devoluciones corresponden a facturas de
			// diferente moneda
			if (lstDev != null && lstDev.size() > 0) {
				Criteria cr = null;
				for (Vreciboxdevolucion v : lstDev) {

					// && ==== buscar en recibo original un pago efectivo con
					// moneda distinta a la del arqueo
					cr = sesion.createCriteria(Recibodet.class);
					cr.add(Restrictions.eq("id.caid", v.getId().getCaid()));
					cr.add(Restrictions
							.eq("id.codcomp", v.getId().getCodcomp()));
					cr.add(Restrictions.eq("id.codsuc", v.getId().getCodsuc()));
					cr.add(Restrictions.eq("id.numrec", v.getId().getNodoco()));
					cr.add(Restrictions.ne("id.moneda", sMoneda));
					cr.add(Restrictions.eq("id.mpago", MetodosPagoCtrl.EFECTIVO ));

					List<Recibodet> lstDetFact = cr.list();
					if (lstDetFact != null && lstDetFact.size() > 0) {

						if (bIngreso) {

							// validar que el pago de la factura original no
							// haya sido pago mixto
							mixto = isPagoOriginalMixto(v.getId().getCaid(), v
									.getId().getCodcomp(), v.getId()
									.getNodoco(), sMoneda);

							if (!mixto) {
								mixto = isPagoOriginalFullForaneo(v.getId()
										.getCaid(), v.getId().getCodcomp(), v
										.getId().getNodoco(), sMoneda);
								if (!mixto)
									lstReal.add(v);
							}
						} else {

							if (sMoneda.compareTo("COR") == 0
									&& lstDetFact.size() == 1) {
								if (v.getId().getMontodev()
										.compareTo(v.getId().getMontofact()) == 0
										&& v.getId().getMontodev().compareTo(
											lstDetFact.get(0).getEquiv()) <= 0) {
									lstReal.add(v);
								}
								else
									if (sMoneda.compareTo("COR") == 0
											&& lstDetFact.size() == 1) {
										if (v.getId().getMontodev()
												.compareTo(v.getId().getMontofact()) != 0
												 ) {
											lstReal.add(v);
										}
									}
							}
							if (!sMoneda.equals("COR"))
								lstReal.add(v);
						}
					}
				}
			}

		} catch (Exception error) {
			LogCajaService.CreateLog("obtenerRecxDevmonex4", "ERR", error.getMessage());
		}

		return lstReal;
	}
	
	public List obtenerEgresosRecMonEx(int iCaid, String sCodcomp,
			String sCodsuc, String sMoneda, Date dtFechaArqueo,
			Date dtHoraInicio, Date dtHoraFin) {
		List lstRecibos = null;
		Session sesion = null;
		
		try {
			sesion = HibernateUtilPruebaCn.currentSession();
			
			Criteria cr = sesion.createCriteria(Vmonedafactrec.class);
			cr.add(Restrictions.eq("id.caid", iCaid));
			cr.add(Restrictions.eq("id.codsuc", sCodsuc));
			cr.add(Restrictions.eq("id.codcomp", sCodcomp));
			cr.add(Restrictions.ne("id.rmoneda", sMoneda));
			cr.add(Restrictions.eq("id.fmoneda", sMoneda));
			cr.add(Restrictions.eq("id.fecha", dtFechaArqueo));
			cr.add(Restrictions.ne("id.restado", "A"));
			cr.add(Restrictions.gt("id.forarecibido", BigDecimal.ZERO));
			cr.add(Restrictions.between("id.hora", dtHoraInicio, dtHoraFin));
			cr.addOrder(Order.asc("id.numrec"));
			
			LogCajaService.CreateLog("obtenerEgresosRecMonEx", "HQRY", LogCajaService.toSql(cr));
			
			lstRecibos = cr.list();
			if (lstRecibos == null)
				lstRecibos = new ArrayList(0);

		} catch (Exception error) {
			LogCajaService.CreateLog("obtenerEgresosRecMonEx", "ERR", error.getMessage());
		}

		return lstRecibos;
	}
	
	public List cargarDetCambio(int iCaid, String sCodcomp, String sCodsuc,
			final String sMoneda, Date dtFechaArqueo, Date dtHoraInicio,
			Date dtHoraFin) {
		List<Vdetallecambiorecibo> lstCambios = new ArrayList<Vdetallecambiorecibo>();
		Session sesion = null;
		
		try {
			 sesion = HibernateUtilPruebaCn.currentSession();
			
			Criteria cr = sesion.createCriteria(Vdetallecambiorecibo.class);
			cr.add(Restrictions.eq("id.caid", iCaid));
			cr.add(Restrictions.eq("id.codcomp", sCodcomp));
			cr.add(Restrictions.eq("id.monedacamb", sMoneda));
			cr.add(Restrictions.gt("id.cambio", BigDecimal.ZERO));
			cr.add(Restrictions.eq("id.fecha", dtFechaArqueo));
			cr.add(Restrictions.eq("id.estado", ""));
			cr.add(Restrictions.ne("id.tiporec", "FCV"));
			cr.add(Restrictions.eq("id.mpago", MetodosPagoCtrl.EFECTIVO ));
			cr.add(Restrictions.between("id.hora", dtHoraInicio, dtHoraFin));
			
			LogCajaService.CreateLog("cargarDetCambio", "HQRY", LogCajaService.toSql(cr));

			lstCambios = cr.list();
			if (lstCambios == null)
				lstCambios = new ArrayList(0);

			
		} catch (Exception error) {
			LogCajaService.CreateLog("cargarDetCambio", "ERR", error.getMessage());
		}

		return lstCambios;
	}
	
	
	/******* OBTENER LOS RECIBOS QUE CORRESPONDEN A DEVOLUCIONES DE FACTURA DEL DIA *****/
	public List obtenerRecibosxdevolucion(boolean bEgresos,int iCaid,String sCodsuc,String sCodcomp,
										  String sTiporec,String sMoneda,Date dtFechaArqueo,
										  Date dtHoraInicio,Date dtHoraFin){
		List lstDev = new ArrayList();
		int iFechajul = 0;
		FechasUtil f = new FechasUtil();
		Session sesion = null;
		
		try{
			
			sesion = HibernateUtilPruebaCn.currentSession();
			
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

			LogCajaService.CreateLog("obtenerRecibosxdevolucion", "HQRY", LogCajaService.toSql(cr));
			
			lstDev = cr.list();		
			
		}catch(Exception error){
			LogCajaService.CreateLog("obtenerRecibosxdevolucion", "HQRY", error.getMessage());
		}finally{
			f = null;
		}
		
		return lstDev;
	}
	
	public List obtenerRecibosxTipo(int iCaid, String sCodsuc, String sCodcomp,
			String sMoneda, String sTiporec, Date dtFechaArqueo,
			Date dtHoraInicio, Date dtHoraFin ) {
		List lstRecibos = new ArrayList();
		String sFecha = "", sql = "", sHini = "", sHfin = "";
		FechasUtil f = new FechasUtil();
		Session sesion = null;
		 
		try {
			
			sesion = HibernateUtilPruebaCn.currentSession();
			
			sFecha = f.formatDatetoString(dtFechaArqueo, "yyyy-MM-dd");

			sql = " SELECT R.NUMREC,R.CAID,R.CODCOMP,R.CODSUC, R.FECHA RFECHA,R.HORA,R.MONTOAPL,RD.MONTO,RD.MPAGO,RD.EQUIV,";
			sql += " RD.TASA, RD.MONEDA RMONEDA, RD.MONEDA FMONEDA,R.TIPOREC,R.CLIENTE,RD.REFER1, RD.REFER2, RD.REFER3, RD.REFER4,R.ESTADO";
			sql +=  
			", R.codcli " +
			", rd.referencenumber " +
			", rd.depctatran " +
			", rd.codigomarcatarjeta " +
			", rd.marcatarjeta " ;
			
			sql += " FROM "+PropertiesSystem.ESQUEMA+".RECIBO R INNER JOIN "+PropertiesSystem.ESQUEMA+".RECIBODET RD ON R.NUMREC = RD.NUMREC AND R.CAID = RD.CAID";
			sql += " AND R.CODCOMP = RD.CODCOMP AND R.CODSUC = RD.CODSUC AND R.TIPOREC = '"
					+ sTiporec + "'";
			sql += " WHERE R.CAID = " + iCaid + " AND R.CODCOMP = '" + sCodcomp
					+ "' AND R.CODSUC =  '" + sCodsuc
					+ "' AND R.TIPOREC = 'EX'";
			sql += " AND RD.MONEDA = '" + sMoneda + "' AND R.FECHA = '"
					+ sFecha + "' AND R.ESTADO<>'A'";
			sHini = f.formatDatetoString(dtHoraInicio, "HH.mm.ss");
			sHfin = f.formatDatetoString(dtHoraFin, "HH.mm.ss");
			sql += " AND R.HORA >= '" + sHini + "' AND R.HORA <='" + sHfin
					+ "'";

			LogCajaService.CreateLog("obtenerRecibosxTipo", "QRY", sql);
			
			lstRecibos = sesion.createSQLQuery(sql)
					.addEntity(Vrecibosxtipoegreso.class).list();

		} catch (Exception error) {
			LogCajaService.CreateLog("obtenerRecibosxTipo", "ERR", error.getMessage());
		} finally {
			f = null;	
		}
		return lstRecibos;
	}
		
	public List obtenerRecpagBanco(int iCaid, String sCodsuc,String sCodcomp, String sMoneda,
					   Date dtFechaArqueo,Date dtHoraInicio, Date dtHoraFin){
		List<Vrecibosxtipoegreso> lstRecibos = new ArrayList<Vrecibosxtipoegreso>();
		List<String>sNotIn = new ArrayList<String>(2);
		Session sesion = null;
		
		try{
		sesion = HibernateUtilPruebaCn.currentSession();
		
		sNotIn.add(MetodosPagoCtrl.EFECTIVO );
		sNotIn.add(MetodosPagoCtrl.CHEQUE);
		
		Criteria cr = sesion.createCriteria(Vrecibosxtipoegreso.class);
		cr.add(Restrictions.eq("id.caid", iCaid)).add(Restrictions.eq("id.codcomp",sCodcomp));
		cr.add(Restrictions.eq("id.codsuc",sCodsuc)).add(Restrictions.eq("id.rfecha",dtFechaArqueo));
		cr.add(Restrictions.eq("id.rmoneda",sMoneda));
		cr.add(Restrictions.ne("id.estado",  "A"));
		cr.add(Restrictions.between("id.hora", dtHoraInicio, dtHoraFin));
		cr.add(Restrictions.not(Restrictions.in("id.mpago", sNotIn)));
		
		LogCajaService.CreateLog("obtenerRecpagBanco", "HQRY", LogCajaService.toSql(cr));
		lstRecibos = (ArrayList<Vrecibosxtipoegreso>)cr.list();
		
		String sReciboin = "";
		
			if (lstRecibos != null && lstRecibos.size() > 0) {
				List<Recibodet> lstRecibodet = new ArrayList<Recibodet>();
				SimpleDateFormat sdfFecha = new SimpleDateFormat("yyyy-MM-dd");
 
				String sql = 
							" select r.numrec, rd.monto, rd.moneda, rd.mpago, rd.tasa, rd.equiv,"+
							"rd.refer1, rd.refer2, rd.refer3, rd.refer4, r.codcomp,"+
							"rd.numrecm, r.caid, r.codsuc, r.tiporec, rd.vmanual, "+
							"rd.caidpos, rd.refer5, rd.refer6, rd.refer7, rd.nombre,"+
							"rd.refer6, rd.refer7, rd.nombre, rd.referencenumber, depctatran, " +
							"codigomarcatarjeta, marcatarjeta, liquidarpormarca";
				
				sql = sql.concat(" from " + PropertiesSystem.ESQUEMA
						+ ".recibo r inner join " + PropertiesSystem.ESQUEMA
						+ ".recibodet rd on ");
				sql = sql.concat(" r.caid = rd.caid and r.codcomp = rd.codcomp and ");
				sql = sql.concat(" r.codsuc = rd.codsuc and r.tiporec = rd.tiporec and r.numrec = rd.numrec");
				sql = sql.concat(" where r.caid = ").concat(String.valueOf(iCaid));
				sql = sql.concat(" and r.codcomp = '" + sCodcomp + "'");
				sql = sql.concat(" and rd.moneda = '" + sMoneda + "'");
				sql = sql.concat(" and r.nodoco <> 0 and r.tipodoco <> ''");
				sql = sql.concat(" and r.estado = '' and r.tiporec<>'FCV' and r.fecha = '" + sdfFecha.format(dtFechaArqueo) + "'");

				LogCajaService.CreateLog("obtenerRecpagBanco", "QRY", sql);
				lstRecibodet = (ArrayList<Recibodet>) sesion.createSQLQuery(sql).addEntity(Recibodet.class).list();

				for (int i = 0; i < lstRecibos.size(); i++) {
					final Vrecibosxtipoegreso v = (Vrecibosxtipoegreso) lstRecibos.get(i);

					if (sReciboin.contains(v.getId().getNumrec() + "@,"))
						continue;
					sReciboin += v.getId().getNumrec() + "@,";

					
					Recibodet rdDevolucion = (Recibodet)
					CollectionUtils.find(lstRecibodet, new Predicate(){
						public boolean evaluate(Object o) {
							 
							Recibodet rd = (Recibodet) o;
							
							return 
							v.getId().getNumrec() == rd.getId().getNumrec() && 
							v.getId().getTiporec().trim().compareToIgnoreCase( rd.getId().getTiporec().trim() ) == 0 && 
							v.getId().getMpago().trim().compareToIgnoreCase( rd.getId().getRefer1().trim()  ) == 0 &&
							v.getId().getRefer1().trim().compareToIgnoreCase( rd.getId().getRefer2().trim()  ) == 0 &&
							v.getId().getRefer2().trim().compareToIgnoreCase( rd.getId().getRefer3().trim()  ) == 0 &&
							v.getId().getRefer3().trim().compareToIgnoreCase( rd.getId().getRefer4().trim()  ) == 0 
							
							;
						}
						
					});
					
					if( rdDevolucion == null )
						continue;
					
					BigDecimal bdNeto =  v.getId().getMonto().subtract( new BigDecimal( Double.toString( rdDevolucion.getId().getMonto() ) )  ) ;
					
					lstRecibos.get(i).setMonto( Divisas.roundBigDecimal(bdNeto)  ) ; 
				}

			}
			
		} catch (Exception error) {
			LogCajaService.CreateLog("obtenerRecpagBanco", "ERR", error.getMessage());
		}

		return lstRecibos;
	}
	
	public List obtenerRecxDevmonex3(boolean bIngreso, int iCaid,
			String sCodsuc, String sCodcomp, String sMoneda, String sTiporec,
			Date dtFechaArqueo, Date dtHoraInicio, Date dtHoraFin) {
		List lstDev = new ArrayList();
		String sConsulta = "", sFecha = "", sHini, sHfin;
		int iFechajul = 0;
		FechasUtil f = new FechasUtil();
		Session sesion = null;
		try {

			sesion = HibernateUtilPruebaCn.currentSession();
			
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
			
			LogCajaService.CreateLog("obtenerRecxDevmonex3", "QRY", sConsulta);
			
			lstDev = sesion.createQuery(sConsulta).list();
			
		} catch (Exception error) {
			LogCajaService.CreateLog("obtenerRecxDevmonex3", "ERR", error.getMessage());
		}

		return lstDev;
	}
	/** **********************************************************************************************
	 * Metodo:	Permite que la devolucion total en moneda distinta a la  que se pago la la factura,
	 * 		 	incluya los pagos de la factura en su misma moneda, para justificar su equivalente 
	 * 			en la moneda de la devolucion. 
	 * 
	 * **/
		public List<Vreciboxdevolucion> obtenerDevParcialMonForaneaIng(int iCaid,String sCodsuc,String sCodcomp,
												String sMoneda,	String sTiporec,Date dtFechaArqueo,
												Date dtHoraInicio,Date dtHoraFin){
			
			List<Vreciboxdevolucion> lstDevol = new ArrayList<Vreciboxdevolucion>();
			List<Vreciboxdevolucion> lstDev = new ArrayList<Vreciboxdevolucion>();
			String sConsulta = "",sFecha = "";
			int iFechajul = 0;
			FechasUtil f = new FechasUtil();
			Session sesion = null;
			
			try {
				iFechajul = f.obtenerFechaJulianaDia(dtFechaArqueo);
				sFecha = f.formatDatetoString(dtFechaArqueo, "yyyy-MM-dd");

				sConsulta = "from Vreciboxdevolucion as v";
				sConsulta += " where v.id.caid = " + iCaid + " and ";
				sConsulta += " v.id.codcomp = '" + sCodcomp	+ "' and v.id.fecha = '" + sFecha + "' and ";
				sConsulta += " v.id.tiporec = '" + sTiporec	+ "' and v.id.fechadev = " + iFechajul + " and ";
				sConsulta += " v.id.mondev = '" + sMoneda	+ "' and v.id.moneda <> '" + sMoneda + "' and ";
				sConsulta += " v.id.fechadev = v.id.fechafact and v.id.montofact = v.id.montodev and ";
				sConsulta += " v.id.mpago='"+MetodosPagoCtrl.EFECTIVO+"' and ";
				sConsulta += " v.id.hora >='"+ f.formatDatetoString(dtHoraInicio, "HH.mm.ss") + "' and ";
				sConsulta += " v.id.hora <='"+ f.formatDatetoString(dtHoraFin, "HH.mm.ss") + "' and v.id.estado <> 'A'";
				
				LogCajaService.CreateLog("obtenerDevParcialMonForaneaIng", "QRY", sConsulta);
				
				sesion = HibernateUtilPruebaCn.currentSession();

				lstDev = sesion.createQuery(sConsulta).list();
				
				if(lstDev != null && lstDev.size() > 0 ){
					
					Criteria cr  = null;
					for (Vreciboxdevolucion v : lstDev) {
						
						String count = String.valueOf(sesion.createCriteria(Recibodet.class)
								.setProjection(Projections.countDistinct("id.moneda"))
								.add(Restrictions.eq("id.caid",    v.getId().getCaid()))
								.add(Restrictions.eq("id.codcomp", v.getId().getCodcomp()))
								.add(Restrictions.eq("id.codsuc",  v.getId().getCodsuc()))
								.add(Restrictions.eq("id.numrec",  v.getId().getNumrec()))
								.add(Restrictions.eq("id.tiporec", v.getId().getTiporec()))
								.add(Restrictions.ne("id.moneda",  v.getId().getMondev()))
								.add(Restrictions.ne("id.moneda",  v.getId().getMonfac()))
								.add(Restrictions.ne("id.moneda",  sMoneda))
								.add(Restrictions.eq("id.mpago",   MetodosPagoCtrl.EFECTIVO )).uniqueResult());
							
							if(count == null || count.trim().compareTo("") == 0
								|| Integer.parseInt(count) > 1) continue;
						
						
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
						
						
						cr.add(Restrictions.sqlRestriction( 						
								"(select count(*) from "+PropertiesSystem.ESQUEMA+".recibodet "+
								" where caid = "+v.getId().getCaid()+
								" and codcomp = '"+v.getId().getCodcomp() +
								" ' and numrec = "+v.getId().getNumrec() +
								" and tiporec = '"+v.getId().getTiporec() +"' ) = 1" ));
						
						LogCajaService.CreateLog("obtenerDevParcialMonForaneaIng", "QRY", LogCajaService.toSql(cr));
						
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
							
							LogCajaService.CreateLog("obtenerDevParcialMonForaneaIng", "QRY", LogCajaService.toSql(cr));
							
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
				LogCajaService.CreateLog("obtenerDevParcialMonForaneaIng", "ERR", e.getMessage());
			}

			return  lstDevol;
		}
	/************************************************************************************/
	/** obtener los recibos por pago de devoluciones con moneda distinta de la factura.
	 *  obtener recibos por devolución pagadas con moneda extranjera (diferente a moneda fac)
	 ********/
			public List obtenerRecxDevmonex(boolean bIngreso,int iCaid,String sCodsuc,String sCodcomp,
												String sMoneda,	String sTiporec,Date dtFechaArqueo,
												Date dtHoraInicio,Date dtHoraFin){
			List lstDev = new ArrayList();
			String sConsulta = "",sFecha = "",sHini,sHfin; ;
			int iFechajul = 0;
			FechasUtil f = new FechasUtil();
			Session sesion = null;
			
			try{
				
				iFechajul = f.obtenerFechaJulianaDia(dtFechaArqueo);
				sFecha = f.formatDatetoString(dtFechaArqueo, "yyyy-MM-dd");
				
				sConsulta =  " from Vreciboxdevolucion as v where v.id.caid = "+iCaid+" and v.id.codsuc = '"+sCodsuc+"'";
				sConsulta += " and v.id.codcomp = '"+sCodcomp+"' and v.id.fecha = '"+sFecha+"' and v.id.tiporec = '"+sTiporec+"'";			
				sConsulta += " and v.id.fechadev = "+iFechajul+" and v.id.fechafact = "+iFechajul + " and v.id.estado = '' and v.id.montoapl < v.id.montofact ";
				
				if(bIngreso)
					sConsulta += " and v.id.moneda = '"+sMoneda+"' and v.id.monfac <> '"+sMoneda+"'";
				else
					sConsulta += " and v.id.moneda <> '"+sMoneda+"' and v.id.monfac = '"+sMoneda+"'";

				sHini = f.formatDatetoString(dtHoraInicio,"HH.mm.ss");
				sHfin = f.formatDatetoString(dtHoraFin,"HH.mm.ss");
				sConsulta += " and v.id.hora >='"+sHini+"' and v.id.hora<='"+sHfin+"'";
				
				LogCajaService.CreateLog("obtenerRecxDevmonex", "QRY", sConsulta);
				sesion = HibernateUtilPruebaCn.currentSession();				
				lstDev = sesion.createQuery(sConsulta).list();
				

			}catch(Exception error){
				LogCajaService.CreateLog("obtenerRecxDevmonex", "ERR", error.getMessage());
			}

			return lstDev;
		}
	
	/*********** obtener recibos pagados con moneda de arqueo y distinta de la factura *********************/
	public List obtenerIngresoRecMonEx(int iCaid, String sCodcomp,String sCodsuc,String sMoneda,
										Date dtFechaArqueo,Date dtHoraInicio, Date dtHoraFin ){
		List lstRecibos = new ArrayList();
		Session sesion = HibernateUtilPruebaCn.currentSession();
		String sConsulta = "",sFecha = "", sHini,sHfin; ;
		FechasUtil f = new FechasUtil();
		
		try{
			sFecha = f.formatDatetoString(dtFechaArqueo, "yyyy-MM-dd");
			sConsulta = " from Vmonedafactrec as v where v.id.caid = "+iCaid+ " and v.id.codsuc ='"+sCodsuc+"'";
			sConsulta += " and trim(v.id.codcomp) = '"+sCodcomp.trim()+"'";
			sConsulta += " and v.id.rmoneda = '"+sMoneda+"' and v.id.fmoneda <> '"+sMoneda+"'";
			sConsulta += " and v.id.fecha = '"+sFecha+"' and v.id.restado <>'A'";
			sConsulta += " and v.id.forarecibido > 0";
			
			sHini = f.formatDatetoString(dtHoraInicio,"HH.mm.ss");
			sHfin = f.formatDatetoString(dtHoraFin, "HH.mm.ss");
			sConsulta += " and v.id.hora >='"+sHini+"' and v.id.hora<='"+sHfin+"'";
			sConsulta += " and v.id.restado <>'A' order by v.id.numrec";
			
			LogCajaService.CreateLog("obtenerIngresoRecMonEx", "QRY", sConsulta);
			lstRecibos = sesion.createQuery(sConsulta).list();

		}catch(Exception error){
			LogCajaService.CreateLog("obtenerIngresoRecMonEx", "ERR", error.getMessage());
		}

		return lstRecibos;
	}	
	public List obtenerRecibosxcambiomixto(int iCaid,String sCodcomp,String sCodsuc,String sMoneda,
											Date dtFechaArqueo,Date dtHoraInicio,Date dtHoraFin){
		List<Vdetallecambiorecibo> lstRec = new ArrayList<Vdetallecambiorecibo>();
		List<Vdetallecambiorecibo> lstRecibos = new ArrayList<Vdetallecambiorecibo>();
		Session sesion = null;
		
		try{
			
			sesion = HibernateUtilPruebaCn.currentSession();

			Criteria crVdcr = sesion.createCriteria(Vdetallecambiorecibo.class);
			crVdcr.add(Restrictions.eq("id.caid", iCaid))
			.add(Restrictions.eq("id.codsuc", sCodsuc))
			.add(Restrictions.eq("id.codcomp", sCodcomp))
			.add(Restrictions.eq("id.monedafac", sMoneda))
			.add(Restrictions.eq("id.monedarec", sMoneda))
			.add(Restrictions.ne("id.monedacamb",sMoneda))
			.add(Restrictions.gt("id.cambio", BigDecimal.ZERO))
			.add(Restrictions.eq("id.estado", ""))
			.add(Restrictions.eq("id.tiporec", "CO"))
			.add(Restrictions.eq("id.mpago", MetodosPagoCtrl.EFECTIVO ))
			.add(Restrictions.eq("id.fecha", dtFechaArqueo))
			.add( Restrictions.between("id.hora", dtHoraInicio, dtHoraFin));
			
			LogCajaService.CreateLog("obtenerRecibosxcambiomixto", "HQRY", LogCajaService.toSql(crVdcr));

			lstRec = crVdcr.list();

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
						
						LogCajaService.CreateLog("obtenerRecibosxcambiomixto", "HQRY", LogCajaService.toSql(cr));
						
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
			LogCajaService.CreateLog("obtenerRecibosxcambiomixto", "ERR", error.getMessage());
		}

		return lstRecibos;
	}
	
	public List<Vrecibosxtipoingreso> obtenerRecibosxTipoIngresoNew(int sCaid, String sCodsuc,
            String sCodcomp, String Moneda, String[] Tiporec, Date dtFechaArqueo,
            Date dtHoraInicio, Date dtHoraFin) {
        List<Vrecibosxtipoingreso> lstRec = new ArrayList<Vrecibosxtipoingreso>();
        SimpleDateFormat sdf00 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf01 = new SimpleDateFormat("HH.mm.ss");

        StringBuilder sbSql = new StringBuilder();
        boolean newCn = false;
        Session sesion = null;

        try {
            String sTipoRec = SqlUtil.constructQuery(false, "v.id.tiporec", true, Tiporec, null);
            sesion = HibernateUtilPruebaCn.currentSession();

            sbSql.append(" from Vrecibosxtipoingreso as v where v.id.caid =").append(sCaid);
            sbSql.append(" and v.id.codcomp = '").append(sCodcomp).append("'");
            sbSql.append(" and v.id.codsuc = '").append(sCodsuc).append("'");
            sbSql.append(" and v.id.moneda ='").append(Moneda).append("'");
            sbSql.append(sTipoRec);
            sbSql.append(" and v.id.fecha = '").append(sdf00.format(dtFechaArqueo)).append("'");
            // --- Agregar filtro de intervalo de horas en caso de arqueos previos.
            sbSql.append(" and v.id.hora >='").append(sdf01.format(dtHoraInicio)).append("'");
            sbSql.append(" and v.id.hora<='").append(sdf01.format(dtHoraFin)).append("'");
            sbSql.append(" order by v.id.tiporec, v.id.numrec");

            LogCajaService.CreateLog("obtenerRecibosxTipoIngresoNew", "QRY", sbSql.toString());
            lstRec = sesion.createQuery(sbSql.toString()).list();
        } catch (Exception error) {
        	LogCajaService.CreateLog("obtenerRecibosxTipoIngresoNew", "ERR", error.getMessage());
        }

        return lstRec;
    }
	public List obtenerRecibosxTipoIngreso(int sCaid, String sCodsuc,
			String sCodcomp, String Moneda, String Tiporec, Date dtFechaArqueo,
			Date dtHoraInicio, Date dtHoraFin ) {
		
		String consulta = "", sFecha = "";
		List lstRec = new ArrayList();
		FechasUtil fechas = new FechasUtil();
		Session sesion = null;

		try {
			sFecha = fechas.formatDatetoString(dtFechaArqueo, "yyyy-MM-dd");
			consulta = " from Vrecibosxtipoingreso as v where v.id.caid ="
					+ sCaid;
			consulta += " and v.id.codcomp = '" + sCodcomp
					+ "' and v.id.codsuc = '" + sCodsuc + "'";
			consulta += " and v.id.moneda ='" + Moneda
					+ "' and v.id.tiporec ='" + Tiporec + "'";
			consulta += " and v.id.fecha = '" + sFecha + "'";

			// --- Agregar filtro de intervalo de horas en caso de arqueos
			// previos.
			String sHini = "", sHfin = "";
			sHini = fechas.formatDatetoString(dtHoraInicio, "HH.mm.ss");
			sHfin = fechas.formatDatetoString(dtHoraFin, "HH.mm.ss");
			consulta += " and v.id.hora >='" + sHini + "' and v.id.hora<='"
					+ sHfin + "'";
			consulta += " order by v.id.numrec";
			
			/*sesion = HibernateUtil.getSessionFactoryMCAJAR().openSession();*/ sesion = HibernateUtilPruebaCn.currentSession();
			Transaction trans = sesion.beginTransaction();
			lstRec = sesion.createQuery(consulta).list();
			trans.commit();

		} catch (Exception error) {
			error.printStackTrace();
//			System.out.println("Error en ReciboCtrl." +
//					"obtenerRecibosxTipoIngreso " + error);
		}finally{
			try {HibernateUtilPruebaCn.closeSession(sesion); /*HibernateUtil.closeSession();*/  /*sesion.close();*/ } catch (Exception e) {}
		}
		return lstRec;
	}
	
	public List<Vfacturaxdevolucion> obtenerDevolucionesxTraslado(int iCaid,
			String sCodcomp, Date dtFechaArqueo, String sMoneda,
			Date dtHoraInicio, Date dtHoraFin) {
		List<Vfacturaxdevolucion>lstDevolucionxCaja = null;
		Session sesion = null;
		
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
			sql += " and tf.fechafac = f.fechadev ";
			sql += " and tf.estadotr in ('E','P') and tf.caiddest = "+iCaid+" and f.fechadev = " +iFechaActual;
			sql += " and trim(f.estado) = ''  and f.codcomp = '" +sCodcomp+ "' and f.moneda = '" +sMoneda+ "'";
			sql += sRangoFecha;
			
			LogCajaService.CreateLog("obtenerDevolucionesxTraslado", "QRY", sql);
			sesion = HibernateUtilPruebaCn.currentSession();
			
			lstDevolucionxCaja = sesion.createSQLQuery(sql).addEntity(Vfacturaxdevolucion.class).list();

		} catch (Exception e) {
			LogCajaService.CreateLog("obtenerDevolucionesxTraslado", "ERR", e.getMessage());
		}

		return lstDevolucionxCaja;
	}
	
	
	/** Método: Obtiene las devoluciones del día que fueron procesadas, omitiendo emisión de cheque
	 *	Fecha:  19/05/2010
	 *  Nombre: Carlos Manuel Hernández Morrison.
	 **/
	public List obtenerDevolucionDeldia(String[] sTiposDoc,
			F55ca017[] f55ca017, List lstLocalizaciones, String sMoneda,
			String sCodsuc, String sCodcomp, int iCaid, Date dtFechaArqueo,
			Date dtHoraInicio, Date dtHoraFin) {
		
		boolean bSoloLocs = false, bHayLoc = false;
		int iFechaActual = 0;
		List lstDev = null;
		
		StringBuilder sqlFacturasInclude = new StringBuilder();
		StringBuilder sqlFacturasConLocs = new StringBuilder();
		StringBuilder sqlFacturasSinLocs = new StringBuilder();
		StringBuilder sqlFromFacturas = new StringBuilder();
		
		Session sesion = null;
		
		try {
			iFechaActual = FechasUtil.dateToJulian(dtFechaArqueo);
			
			SimpleDateFormat dfHora = new SimpleDateFormat("HHmmss");
	        int iHini = Integer.parseInt(dfHora.format(dtHoraInicio));
	        int iHfin = Integer.parseInt(dfHora.format(dtHoraFin));
	        
	        List<F55ca017> lstF55ca017 = new ArrayList<F55ca017>(Arrays.asList(f55ca017));
	        
	        List<F55ca017> lstF55ca017Tmp1 = SqlUtil.getFilterListF55ca017(lstF55ca017, "", '=');
	        Object[] ss0 = SqlUtil.getListFromFieldF55ca017(lstF55ca017Tmp1, "c7mcul");
	        if (lstF55ca017Tmp1.size() > 0) {
	            bSoloLocs = true;
	        }
	        
	        List<F55ca017> lstF55ca017Tmp2 = SqlUtil.getFilterListF55ca017(lstF55ca017, "", 'D');
	        Object[] ss1 = SqlUtil.getListFromFieldF55ca017(lstF55ca017Tmp2, "c7mcul");
	        if (lstF55ca017Tmp2.size() > 0) {
	            bSoloLocs = true;
	        }
	        
	        List<F55ca017> lstF55ca017Tmp = SqlUtil.getFilterListF55ca017(lstF55ca017Tmp2, "", 'D');
	        Object[] ss2 = SqlUtil.getListFromFieldF55ca017(lstF55ca017Tmp, "c7locn");
	        if (lstF55ca017Tmp.size() > 0) {
	            bHayLoc = true;
	        }
			
			String uninegsFacsSinLcn = SqlUtil.constructQuery(false, "trim(f.codunineg)", true, ss0, null);
			sqlFacturasSinLocs
				.append("select * from ")
				.append(PropertiesSystem.ESQUEMA)
				.append(".Vfacturaxdevolucion as f")
				.append(" where trim(f.sdlocn) = '' ")//and trim(f.codunineg)
				.append(uninegsFacsSinLcn);

			if (bHayLoc) {
				sqlFacturasConLocs.append("select * from ")
						.append(PropertiesSystem.ESQUEMA)
						.append(".Vfacturaxdevolucion as f")
						.append(SqlUtil.constructQuery(true, "trim(f.sdlocn)", true, ss2, null))
						.append(SqlUtil.constructQuery(false, "trim(f.codunineg)", true, ss1, null));

				sqlFromFacturas.append(sqlFacturasConLocs);
				if (bSoloLocs && ss0 != null  && ss0.length > 0) {
					sqlFromFacturas.append(" union ")
							.append(sqlFacturasSinLocs);
				}
			} else {
				sqlFromFacturas.append(sqlFacturasSinLocs);
			}

			sqlFacturasInclude
				.append("Select * From (\n ")
				.append(sqlFromFacturas).append(" \n ) as f ")
				.append(" where  f.estado = '' and f.codcomp = '")
				.append(sCodcomp).append("'").append(" and  f.moneda = '")
				.append(sMoneda).append("' and f.hora between ")
				.append(iHini).append(" and ")
				.append(iHfin).append(" and f.fechadev = ")
				.append(iFechaActual)
				.append(SqlUtil.constructQuery(false, "f.tipodev", true, sTiposDoc, null))
				.append(" and f.nodev not in( ") //CAMBIO
				.append(" select tf.nofactura from ")
				.append(PropertiesSystem.ESQUEMA).append(".trasladofac tf ")
				.append(" where tf.nofactura = f.nodev and tf.codcomp = f.codcomp ")
				.append(" and tf.tipofactura = f.tipodev and tf.codsuc = f.codsuc ")
				.append(" and trim(tf.codunineg)  = trim(f.codunineg) ")
				.append(" and tf.estadotr in ('E','R','P') ")
				.append(" and tf.caidprop = ").append(iCaid)
				.append(" and tf.fechafac = f.fechadev and f.codcomp = '")
				.append(sCodcomp).append("'").append(" and f.moneda = '")
				.append(sMoneda).append("' )");

			sqlFacturasInclude.append(" union ");
			sqlFacturasInclude
				.append(" select f.*  from ")
				.append(PropertiesSystem.ESQUEMA)
				.append(".Vfacturaxdevolucion f inner join ")
				.append(PropertiesSystem.ESQUEMA)
				.append(".trasladofac tf on  tf.nofactura = f.nodev")
				.append(" and tf.codcomp = f.codcomp")
				.append(" and tf.tipofactura = f.tipodev ")
				.append(" and trim(tf.codunineg) = trim(f.codunineg) ")
				.append(" where tf.estadotr in ('E','P','R') and tf.caiddest =")
				.append(iCaid).append(" and f.fechadev = ")
				.append(iFechaActual)
				.append(" and tf.fechafac = f.fechadev and trim(f.estado) = ''")
				.append(" and f.codcomp = '").append(sCodcomp).append("'")
				.append(" and f.moneda = '").append(sMoneda).append("'")
				.append(" and f.hora between ").append(iHini)
				.append(" and ").append(iHfin);

			sesion = HibernateUtilPruebaCn.currentSession();
			
			LogCajaService.CreateLog("obtenerDevolucionDeldia", "QRY", sqlFacturasInclude);
					
			lstDev  = sesion.createSQLQuery(sqlFacturasInclude.toString())
					.addEntity(Vfacturaxdevolucion.class).list();

		} catch (Exception error) {
			LogCajaService.CreateLog("obtenerDevolucionDeldia", "ERR", error.getMessage());
		} finally{
			sqlFacturasConLocs = null;
			sqlFacturasSinLocs = null;
			sqlFromFacturas = null;
		}
		
		return lstDev;
	}	
	
	/****************************************************************************************/
	/** Leer las facturas del día, crédito, contado y devoluciones para la caja seleccionada 
	 * 	solamente por traslados
	 *  Fecha:  01/04/2011
	 *  Hecho: Carlos Manuel Hernández Morrison.
	 */	
		public List<Vhfactura> obtenerFacturasDiaxTraslado(int iCaid, String sCodcomp,Date dtFechaArqueo,String sMoneda,
														Date dtHoraInicio, Date dtHoraFin ){
			String sql = "";
			String sRangoHoraTf="";
			int iFechaActual=0;
			ArrayList<Vhfactura>lstFacturas = null;
			Session sesion = null;
			
			try {				
				iFechaActual  = new FacturaCrtl().obtenerFechaActualJuliana(dtFechaArqueo);
				String strFecha = FechasUtil.getDateToString(dtFechaArqueo);
				sRangoHoraTf += " and cast(tf.fecha as time)  between";
				sRangoHoraTf += "'" +new SimpleDateFormat("HH:mm:ss").format(dtHoraInicio)+"' and ";
				sRangoHoraTf += "'" +new SimpleDateFormat("HH:mm:ss").format(dtHoraFin)+"'";	
				
				String sRangoHoraTf2 = " and tf.fecha  between";
				sRangoHoraTf2 += "'" + strFecha +  " " +new SimpleDateFormat("HH:mm:ss").format(dtHoraInicio)+"' and ";
				sRangoHoraTf2 += "'" + strFecha +  " " +new SimpleDateFormat("HH:mm:ss").format(dtHoraFin)+"'";	
				
				String sqlTrasladoF = "select tf.* from "+PropertiesSystem.ESQUEMA+".trasladofac tf where tf.caiddest = "+iCaid+
						" and tf.codcomp = '"+sCodcomp+"' and tf.estadotr in ('E','P') and tf.moneda = '"+ sMoneda +"'  " + sRangoHoraTf2;
				
				sesion = HibernateUtilPruebaCn.currentSession();
				
				LogCajaService.CreateLog("obtenerFacturasDiaxTraslado", "QRY", sqlTrasladoF);
				
				List<Trasladofac> lstFacturasT  = (ArrayList<Trasladofac>)sesion.createSQLQuery(sqlTrasladoF)
						.addEntity(Trasladofac.class).list();
				
				if(lstFacturasT.size()>0)
				{
					sql =  " select f.* from "+PropertiesSystem.ESQUEMA+".Vhfactura as f inner join "+PropertiesSystem.ESQUEMA+".trasladofac tf" ; 
					sql += " on  tf.nofactura = f.nofactura and tf.codcomp = f.codcomp and tf.tipofactura = f.tipofactura";
					sql += " and tf.codsuc = f.codsuc  and trim(tf.codunineg)  = trim(f.codunineg) and tf.estadotr in ('E','P')";
					sql += " and tf.caiddest = "+iCaid+" and f.fecha = "+ iFechaActual +" and trim(f.estado) = '' ";
					sql += " and f.codcomp = '"+sCodcomp+"' and f.moneda = '"+ sMoneda+"'";
					sql += (sRangoHoraTf);
					
					LogCajaService.CreateLog("obtenerFacturasDiaxTraslado", "QRY", sql);
					
					lstFacturas  = (ArrayList<Vhfactura>)sesion.createSQLQuery(sql)
										.addEntity(Vhfactura.class).list();
				}
				
			} catch (Exception e) {
				LogCajaService.CreateLog("obtenerFacturasDiaxTraslado", "ERR", e.getMessage());
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
												Date dtFechaArqueo,Date dtHoraInicio, Date dtHoraFin){		
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
			Session sesion = null;
			
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
				for(int i = 0; i < f17_1.size(); i++){
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
				
				/*sesion = HibernateUtil.getSessionFactoryMCAJAR().openSession();*/ sesion = HibernateUtilPruebaCn.currentSession();
				Transaction trans = sesion.beginTransaction();
				
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
				trans.commit();
				
			} catch (Exception error) {
				lstFac = null;
				error.printStackTrace();
//				System.out.println("Error en ArqueoCajaCtrl.obtenerFacturaDelDia " + error);
			} finally {
				try {HibernateUtilPruebaCn.closeSession(sesion); /*sesion.close();*/ } catch (Exception e) {}
			}
			return lstFac;
		}
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
				
				Integer cant = (Integer)sesion.createSQLQuery(sql).uniqueResult();
				if (cant > 1) 
					mixto = true;
				
			}catch(Exception ex){
				LogCajaService.CreateLog("isPagoOriginalMixto", "ERR", ex.getMessage());
			}

			return mixto;
		}
		public boolean isPagoOriginalFullForaneo(int caid,String codcomp,int numrec,String moneda){
			boolean full = true;
			String sql = "";
			Session sesion = null;

			try{
				sql = "select distinct moneda from "+PropertiesSystem.ESQUEMA+".recibodet " +
					  "where caid = " + caid + " and codcomp = '"
						+ codcomp + "' and numrec = " + numrec;
				
				sesion = HibernateUtilPruebaCn.currentSession();
				
				LogCajaService.CreateLog("isPagoOriginalFullForaneo", "QRY", sql);

				List<String>lstMonedas = (ArrayList<String>)sesion.createSQLQuery(sql).list();
				
				if(lstMonedas != null  && !lstMonedas.isEmpty()){
					if(lstMonedas.size() > 1 || 
					   lstMonedas.get(0).compareTo(moneda) != 0)
						full = false;
				}
			}catch(Exception ex){
				LogCajaService.CreateLog("isPagoOriginalFullForaneo", "ERR", ex.getMessage());
			}

			return full;
		}
}

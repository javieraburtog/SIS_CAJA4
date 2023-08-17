package com.casapellas.reportes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.application.NavigationHandler;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.hibernate.Criteria;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;

import com.casapellas.controles.CompaniaCtrl;
import com.casapellas.controles.CtrlCajas;
import com.casapellas.controles.MonedaCtrl;
import com.casapellas.controles.ReciboCtrl;
import com.casapellas.entidades.A02factco;
import com.casapellas.entidades.A03factco;
import com.casapellas.entidades.F55ca014;
import com.casapellas.entidades.Recibofac;
import com.casapellas.entidades.Recibojde;
import com.casapellas.entidades.ReporteIR;
import com.casapellas.entidades.Vcompania;
import com.casapellas.entidades.ens.Vautoriz;
import com.casapellas.entidades.Vf55ca01;
import com.casapellas.entidades.Vrecibostcir;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.util.Divisas;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.PropertiesSystem;
import com.ibm.icu.util.Calendar;
import com.infragistics.faces.grid.component.html.HtmlGridView;
import com.infragistics.faces.input.component.html.HtmlDateChooser;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
import com.infragistics.faces.window.component.html.HtmlDialogWindow;

/**
 * CASA PELLAS S.A.
 * Creado por.........: Juan Carlos Ñamendi Pineda
 * Fecha de Creación..: 14/07/2010
 * Última modificación: Juan Carlos Ñamendi Pineda
 * Modificado por.....:	15/07/2010
 * Descripcion:.......: Cambio completo de página, sin visor de pdf, y nuevo tipo de reporte.
 * 
 */

public class Rptmcaja010DAO {
	Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	private HtmlDropDownList  ddlFiltroCompania, ddlFiltroMoneda, ddlFiltroCaja;
	private List<SelectItem>  lstFiltroCompania, lstFiltroMoneda, lstFiltroCajas;
	private HtmlDateChooser dcFechaFinal,dcFechaInicio;
	private Date fechaactual1,fechaactual2;
	private HtmlOutputText lblMsgValidacion;
	private HtmlGridView gvTransacTcIrs;
	private List<ReporteRetencionIR>lstTransacTcIrs;
	private HtmlDialogWindow dwMsgValidacion;
	private HtmlOutputText lblValidarArqueo;

	
	public void cerrarMsgValidacion(ActionEvent ev){
		dwMsgValidacion.setWindowState("hidden");
	}
	
	
	@SuppressWarnings("unchecked")
	public void generarReporteMcaja010(ActionEvent ev) {
		boolean newCn = false;
		Session sesion = null;
		Transaction trans = null;
		
		try {
			
			m.remove("rpt10_Compania");
			m.remove("rpt10_lstTransacTcIrs");
			m.remove("rpt10_lstTransacTcIrsDsply");
			lstTransacTcIrs = new ArrayList<ReporteRetencionIR>();
			
			String sCodigoEx = "EXE";
			String sCodcomp = ddlFiltroCompania.getValue().toString();
			String sMoneda  = ddlFiltroMoneda.getValue().toString();
			int caid = 0;
			
			boolean filtrarCompania = sCodcomp.compareTo("99999") != 0;
			
			if(sMoneda.trim().compareTo("SMO") == 0)
				sMoneda = "";

			Date dtInicio = new Date();
			Date dtFin = new Date();
			Calendar calFechaHoy = Calendar.getInstance();
			
			if(dcFechaInicio.getValue() == null){
				calFechaHoy.add(Calendar.MONTH, -3);
				calFechaHoy.set(Calendar.DATE, calFechaHoy.getActualMinimum(Calendar.DATE));
				dtInicio = calFechaHoy.getTime();
			}else{
				dtInicio =  (Date)dcFechaInicio.getValue(); 
			}
			if(dcFechaFinal.getValue() == null){
				calFechaHoy = Calendar.getInstance();
				calFechaHoy.set(Calendar.DATE, calFechaHoy.getActualMaximum(Calendar.DATE));
				dtFin = calFechaHoy.getTime();
			}else{
				dtFin =  (Date)dcFechaFinal.getValue(); 
			}
			if(dtFin.before(dtInicio)){
				calFechaHoy.setTime(dtInicio);
				dtInicio = dtFin;
				dtFin = calFechaHoy.getTime();
			}
			
			//&& ===== Validar que la cantidad de dias no se mayor de 182, (seis meses)
			GregorianCalendar cIni = (GregorianCalendar) GregorianCalendar.getInstance();
			GregorianCalendar cFin = (GregorianCalendar) GregorianCalendar.getInstance();
			cIni.setTime(dtInicio);
			cFin.setTime(dtFin);
			int rango = 0;
			
			if (cIni.get(Calendar.YEAR) == cFin.get(Calendar.YEAR)) {
				rango = cFin.get(Calendar.DAY_OF_YEAR) - cIni.get(Calendar.DAY_OF_YEAR);
	        } else {
	            int diasAnyo = cIni.isLeapYear(cIni.get(Calendar.YEAR)) ? 366 : 365;
	            int rangoAnyos = cFin.get(Calendar.YEAR) - cIni.get(Calendar.YEAR);
	            rango = (rangoAnyos * diasAnyo) + (cFin.get(Calendar.DAY_OF_YEAR) - cIni.get(Calendar.DAY_OF_YEAR));
	        }
			if(rango > 182){
				calFechaHoy.setTime(dtFin);
				calFechaHoy.add(Calendar.DATE, - 182);
				dtInicio = calFechaHoy.getTime();
			}
			
			//&& ======== Buscar las transacciones y crear el objeto para el reporte y grid
			sesion = HibernateUtilPruebaCn.currentSession();
			trans = (newCn = !(sesion.getTransaction().isActive())) ? sesion
					.beginTransaction() : sesion.getTransaction();
			
			Criteria cr = sesion.createCriteria(Vrecibostcir.class);
			cr.add(Restrictions.eq("id.estado",""));
			
			if(filtrarCompania)
				cr.add(Restrictions.eq("id.codcomp", sCodcomp));
			
			if(sMoneda.compareTo("") != 0)
				cr.add(Restrictions.eq("id.moneda", sMoneda));
			
			List<Integer> cajas = new ArrayList<Integer>();
			String codcajasel = ddlFiltroCaja.getValue().toString().trim();
			if(codcajasel.compareTo("999999") != 0 && codcajasel.compareTo("000") != 0){
				cr.add(Restrictions.eq("id.caid", Integer.parseInt(codcajasel) ));
				cajas.add(Integer.parseInt(codcajasel)) ;
			}			
			if( codcajasel.compareTo("000") == 0){
				cajas = new ArrayList<Integer>();
				for (SelectItem si : ddlFiltroCaja.getSelectItems())
					cajas.add(Integer.parseInt(String.valueOf(si.getValue()))) ;
				cr.add(Restrictions.in("id.caid", cajas));
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(dtInicio != null)
				cr.add(Restrictions.ge("id.fecha", sdf.parse( sdf.format(dtInicio) )));
			if(dtFin != null)
				cr.add(Restrictions.le("id.fecha", 	sdf.parse( sdf.format(dtFin) )));
			
			ArrayList<Vrecibostcir> lstRec = (ArrayList<Vrecibostcir>) cr.list();

			if(lstRec == null || lstRec.isEmpty()){
				dwMsgValidacion.setWindowState("normal");
				lblValidarArqueo.setValue("No se han encontrado resultados");
				return;
			}

			BigDecimal iva = new BigDecimal("15");//TODO CAMBIAR ESTE VALOR LEIDO DE LA TABLA 
			BigDecimal ir =  BigDecimal.ONE;
			
			BigDecimal ivaPrcnt = BigDecimal.ZERO; 
			BigDecimal irPrcnt  = BigDecimal.ZERO;
			
			//&& ===== obtener las tasas IRs para las cajas seleccionadas.
			cr = sesion.createCriteria(F55ca014.class)
					.add(Restrictions.eq("id.c4stat", "A"))
					.add(Restrictions.in("id.c4id", cajas));
			
			if(filtrarCompania) {
				cr.add(Restrictions.eq("id.c4rp01", sCodcomp));
			}
			
			List<F55ca014> tasasIr = (ArrayList<F55ca014>) cr.list();
			
			BigDecimal bmontofac = BigDecimal.ZERO;
			BigDecimal bmontopago =  BigDecimal.ZERO;
			BigDecimal bmontoexon =  BigDecimal.ZERO;
			BigDecimal bmontoexen =  BigDecimal.ZERO;
			
			F55ca014 f14;
			ArrayList<Recibofac> lstFctxRec; 
			List<A03factco> facturaDet;
			BigDecimal bdFacturasPorRecibo;
			ArrayList<Recibojde> lstNoDocxRec;
			BigDecimal BigDecimal_CIEN = BigDecimal.TEN.multiply(BigDecimal.TEN);
			
			SimpleDateFormat sdf_hora = new SimpleDateFormat("HH:mm:ss.SSS");
			int contador = 0;
			long ini=0;
			long fin=0;
			
			String fechaini =  sdf.format(dtInicio) ;
			String fechafin =  sdf.format( dtFin )  ;
			int juliana_ini = FechasUtil.dateToJulian(dtInicio) ;
			int juliana_fin = FechasUtil.dateToJulian(dtFin) ;
			
			
			ArrayList<ReporteRetencionIR> lstDtaXls = new ArrayList<ReporteRetencionIR>();
			
			List<Vrecibostcir> recibosNoContado = new ArrayList<Vrecibostcir>();
			List<Vrecibostcir> recibosContado = new ArrayList<Vrecibostcir>();
			List<A02factco> lstA02factcos = new ArrayList<A02factco>();
			List<A03factco> lstA03factcos = new ArrayList<A03factco>();
			List<Recibojde> lstRecibosJde = new ArrayList<Recibojde>();
			
			//&& ============ separar recibos solo de contado
			recibosContado = (ArrayList<Vrecibostcir>)
			 CollectionUtils.select(lstRec, new Predicate(){
				@Override
				public boolean evaluate(Object o) {
					 return ((Vrecibostcir)o).getId().getTiporec().compareTo("CO") == 0 ;
				}
			});
			//&& ============ separar recibos que no son contado
			recibosNoContado = (ArrayList<Vrecibostcir>)CollectionUtils.subtract(lstRec, recibosContado);
			
			//&& ============ buscar enlaces con recibofac para contado.
			String sqlSelect = " select  rf.* from "+PropertiesSystem.ESQUEMA+".recibofac rf where rf.estado = '' " +
					"and rf.fecha between "+juliana_ini+" and "+juliana_fin+"  and rf.tiporec = 'CO'" +
					" and rf.caid in " +cajas.toString().replace("[", "(").replace("]", ")");
			
			if(filtrarCompania) {
				sqlSelect = sqlSelect + " and trim(rf.codcomp) = '"+sCodcomp.trim()+"'";
			}
			List<Recibofac>lstRecibosfac = (ArrayList<Recibofac>)sesion.createSQLQuery(sqlSelect).addEntity(Recibofac.class).list() ;
					
			//&& ============ buscar enlaces A02factco para el maestro de facturas de contado
			sqlSelect = 
				" select f.* " + 
				" from "+PropertiesSystem.ESQUEMA+".recibofac rf inner join "+PropertiesSystem.ESQUEMA+".a02factco f on " + 
				"	rf.codcli = f.codcli and rf.fecha = f.fecha and rf.numfac = f.nofactura " + 
				"	and rf.tipofactura = f.tipofactura and  trim(rf.codcomp) = Trim(f.codcomp) " + 
				"	and trim(rf.codunineg) = trim(f.codunineg) " +
				" where  rf.estado = '' and rf.fecha between "+juliana_ini+" and "+juliana_fin+"  and rf.tiporec = 'CO' " 
				+ " and rf.caid in " +cajas.toString().replace("[", "(").replace("]", ")" )  ;
			
			if(filtrarCompania) {
				sqlSelect = sqlSelect + " and trim(rf.codcomp) = '"+sCodcomp.trim()+"'";
			}
			
			
			lstA02factcos = (ArrayList<A02factco>) sesion.createSQLQuery(sqlSelect).addEntity(A02factco.class).list() ;
			
			sqlSelect = 
			" select d.* "+
			" from "+PropertiesSystem.ESQUEMA+".recibofac f inner join "+PropertiesSystem.ESQUEMA+".a03factco d on  "+
			"	f.codcli = d.codcli and f.fecha = f.fecha and f.numfac = d.nofactura "+ 
			"	and f.tipofactura = d.tipofactura and trim(f.codunineg) = trim(d.codunineg) "+
			" where f.estado = '' " +
			"	and f.fecha between "+juliana_ini+" and "+juliana_fin+" " +
			"	and f.tiporec = 'CO' " +
			" 	and f.caid in " +cajas.toString().replace("[", "(").replace("]", ")" )  ;
			
			if(filtrarCompania) {
				sqlSelect = sqlSelect + " and trim(f.codcomp) = '"+sCodcomp.trim()+"'";
			}
			
			lstA03factcos = (ArrayList<A03factco>)sesion.createSQLQuery(sqlSelect).addEntity(A03factco.class).list() ;
			
			sqlSelect = 
			" select rj.* " +
			" from "+PropertiesSystem.ESQUEMA+".recibojde rj " + 
			" where rj.tipodoc = 'R'  " + 
			" and (rj.caid, rj.codcomp, rj.numrec, rj.tiporec)  in " + 
			" ( select r.caid, r.codcomp, r.numrec, r.tiporec " +
			"   from "+PropertiesSystem.ESQUEMA+".recibo r " +
			"   where r.fecha between '"+fechaini+"' and '"+fechafin+"' and r.tiporec <> 'CO' and r.estado = ''  and r.caid in "+
			   cajas.toString().replace("[", "(").replace("]", ")" )  + ") " ;
			
			if(filtrarCompania) {
				sqlSelect = sqlSelect + " and trim(rj.codcomp) = '"+sCodcomp.trim()+"'";
			}
			
			List<Recibojde> lstRecibosJdeInicial = (ArrayList<Recibojde>)sesion.createSQLQuery(sqlSelect).addEntity(Recibojde.class).list() ;

			System.out.println(" antes de filtrar " + lstRecibosJdeInicial.size() );
			
			for (final Recibojde rj : lstRecibosJdeInicial) {
				
				Recibojde rj1 = (Recibojde)
				 	CollectionUtils.find(lstRecibosJde, new Predicate(){
						public boolean evaluate(Object o) {
							return rj.getId().getNobatch() == ( (Recibojde) o ).getId().getNobatch() ; 
						}
				 	});
				
				if(rj1 != null ){
					continue;
				}
				lstRecibosJde.add(rj) ;
			}
			
			System.out.println("  filtrados  " + lstRecibosJde.size() );
			
			ini = System.currentTimeMillis(); 
			
			//&& =============== Procesar los recibos de contado.
			for (final Vrecibostcir v : recibosContado) {
				
				f14 = (F55ca014)
						CollectionUtils.find(tasasIr, new Predicate(){
							public boolean evaluate(Object o) {
								F55ca014 comp = (F55ca014)o;
								return
									comp.getId().getC4id() == v.getId().getCaid() && 
									comp.getId().getC4rp01().trim().compareToIgnoreCase( v.getId().getCodcomp().trim()) == 0 ;
							}});
					
				ir = f14.getId().getC4trir();
				
				ivaPrcnt = iva.divide( BigDecimal_CIEN, 4, RoundingMode.HALF_UP);
				irPrcnt  =  ir.divide( BigDecimal_CIEN, 4, RoundingMode.HALF_UP);
				
				lstFctxRec = (ArrayList<Recibofac>) 
					CollectionUtils.select(lstRecibosfac, new Predicate(){
						@Override
						public boolean evaluate(Object o) {
							Recibofac rf =  (Recibofac)o ;
							return   
								rf.getId().getCaid() == v.getId().getCaid()  && 
								rf.getId().getNumrec() == v.getId().getNumrec() &&
								rf.getId().getCodcomp().trim().compareTo( v.getId().getCodcomp().trim() ) == 0  &&
								rf.getId().getTiporec().trim().compareTo( v.getId().getTiporec().trim() ) == 0 ;
						}
					} );
				
				if (lstFctxRec == null || lstFctxRec.isEmpty())
					continue;

				bdFacturasPorRecibo = BigDecimal.valueOf(lstFctxRec.size());
				
				
				// &&& ==== Buscar la factura original y determinar si es exonerada (toda la factura sin IVA )
				int montosexonerado = 0;
				int montosexentos   = 0;
				for (final Recibofac recfac : lstFctxRec) {
					
					A02factco facOriginal = (A02factco)
						CollectionUtils.find(lstA02factcos, new Predicate(){
							@Override
							public boolean evaluate(Object o) {
								A02factco fact =  (A02factco)o ;
								return
								fact.getId().getFecha().intValue() == recfac.getId().getFecha() && 
								fact.getId().getNofactura().intValue() == recfac.getId().getNumfac() &&
								fact.getId().getCodcli().intValue() ==	recfac.getId().getCodcli() &&
								fact.getId().getTipofactura().compareTo(recfac.getId().getTipofactura() ) == 0 &&
								fact.getCodcomp().trim().compareTo(recfac.getId().getCodcomp().trim()) == 0 ;
							}
						} );
					
					if(facOriginal == null)
						continue;

					recfac.setTasafactura(facOriginal.getTasa());
					
					//&& ======== Factura Exonerada
					if (facOriginal.getTotal() == facOriginal.getSubtotal()){
						montosexonerado += facOriginal.getTotal();
					}
					else{
					
						//&& ===== Verificar que la factura tenga detalles exentos de iva
						
						facturaDet = (ArrayList<A03factco>)
							CollectionUtils.select(lstA03factcos, new Predicate(){
								@Override
								public boolean evaluate(Object o) {
									A03factco fact =  (A03factco)o ;
									return
									fact.getId().getFecha() == recfac.getId().getFecha() && 	
									fact.getId().getNofactura() == recfac.getId().getNumfac() &&
									fact.getId().getCodcli() ==	recfac.getId().getCodcli() &&
									fact.getId().getTipofactura().compareTo( recfac.getId().getTipofactura() ) == 0 ;
								}
							} );
						
						if(facturaDet == null || facturaDet.isEmpty())
							continue;
						
						for (A03factco dtFc : facturaDet) {
							if (dtFc.getId().getImpuesto().trim().compareToIgnoreCase( (sCodigoEx) ) == 0){
								montosexentos += dtFc.getId().getPreciounit()* dtFc.getId().getCant();
							}
						}
					}
				}
				bmontoexon = BigDecimal.valueOf(montosexonerado).divide( BigDecimal_CIEN, 2, RoundingMode.HALF_UP);
				bmontoexen = BigDecimal.valueOf(montosexentos).divide( BigDecimal_CIEN, 2, RoundingMode.HALF_UP);
						
				//&& ==== Conservar el monto del pago con el iva aplicado
				bmontofac = v.getId().getMonto();
				
				// && ==== segmentar el pago entre todas las facturas.
				bmontofac  = bmontofac.divide( bdFacturasPorRecibo, 2, RoundingMode.HALF_UP).setScale(4,BigDecimal.ROUND_HALF_UP);
				bmontopago = v.getId().getMonto().divide( bdFacturasPorRecibo, 2, RoundingMode.HALF_UP).setScale(4,BigDecimal.ROUND_HALF_UP);
				bmontoexon = bmontoexon.divide( bdFacturasPorRecibo, 2, RoundingMode.HALF_UP).setScale(4,BigDecimal.ROUND_HALF_UP);
				bmontoexen = bmontoexen.divide( bdFacturasPorRecibo, 2, RoundingMode.HALF_UP).setScale(4,BigDecimal.ROUND_HALF_UP);
				
				// && ==== Crear el registro para el reporte.
				for (Recibofac rf : lstFctxRec) {
					ReporteRetencionIR r = cargarDatosRpt(v);
					r.setTasafactura(rf.getTasafactura());
					r.setTipodocumento(rf.getId().getTipofactura());
					r.setNodocumento(rf.getId().getNumfac());
					r.setMontoexonerado(bmontoexon);
					r.setMontoexento(bmontoexen);
					r.setMontocntdo(bmontofac);
					r.setMontopago(bmontopago);
					r.setTotalsininva(bmontofac);
					r.setTotaliva(bmontofac.multiply(ivaPrcnt));
					r.setPrcntCmsIr(ir);
					r.setPrcntCmsIva(iva);
					lstDtaXls.add(r);
				}
				
				 if( (++contador) % 1000 == 0){
					 fin = System.currentTimeMillis();
					 System.out.println("Registro: " + contador +" > Tiempo " +  ( (fin-ini)/ 1.0E03 ) );
					 ini = fin;
				 }
				
			}
			
			//&& =============== Procesar los recibos que no son de contado.
			
			ini = System.currentTimeMillis(); 
			
			for (final Vrecibostcir v : recibosNoContado ) {
				
				f14 = (F55ca014)
				CollectionUtils.find(tasasIr, new Predicate(){
					public boolean evaluate(Object o) {
						return	((F55ca014)o).getId().getC4id() == v.getId().getCaid() && 
								((F55ca014)o).getId().getC4rp01().trim().compareToIgnoreCase( v.getId().getCodcomp().trim()) == 0 ;
					}});
					
				ir = f14.getId().getC4trir();
				
				ivaPrcnt = iva.divide( BigDecimal_CIEN, 4, RoundingMode.HALF_UP);
				irPrcnt  =  ir.divide( BigDecimal_CIEN, 4, RoundingMode.HALF_UP);
			
				lstNoDocxRec = (ArrayList<Recibojde>)
				CollectionUtils.select(lstRecibosJde, new Predicate(){
					@Override
					public boolean evaluate(Object o) {
						Recibojde rj =  (Recibojde)o ;
						return
							rj.getId().getCaid() == v.getId().getCaid()  && 
							rj.getId().getNumrec() == v.getId().getNumrec() &&
							rj.getId().getCodcomp().trim().compareTo( v.getId().getCodcomp().trim() ) == 0  &&
							rj.getId().getTiporec().trim().compareTo( v.getId().getTiporec().trim() ) == 0 ;
					}
				} );
				
				if (lstNoDocxRec == null || lstNoDocxRec.isEmpty())
					continue;
				
				for (Recibojde rf : lstNoDocxRec) {
					ReporteRetencionIR r = cargarDatosRpt(v);
					r.setTipodocumento("RC");
					r.setNodocumento(rf.getId().getRecjde());
					r.setMontopago(v.getId().getMonto());
					r.setMontoabono(v.getId().getMonto());
					r.setMontocntdo(BigDecimal.ZERO);
					r.setTotalsininva(BigDecimal.ZERO);
					r.setPrcntCmsIr(ir);
					r.setPrcntCmsIva(iva);
					r.setTasafactura(v.getId().getTasa());
					lstDtaXls.add(r);
				}
				
				if( (++contador) % 1000 == 0){
					 fin = System.currentTimeMillis();
					 System.out.println("Registro RC: " + contador +" > Tiempo " +  ( (fin-ini)/ 1.0E03 ) );
					 ini = fin;
				 }
			}
			
			Collections.sort(lstDtaXls, new Comparator<ReporteRetencionIR>() {
				@Override
				public int compare(ReporteRetencionIR r1, ReporteRetencionIR r2) {
					int compare = 
						(r1.getFecha().before(r2.getFecha())) ? -1 :
						(r1.getFecha().after(r2.getFecha())) ? 1 : 0;
					if (compare == 0)
						compare = 
						(r1.getLiquidacion() < r2.getLiquidacion() ) ? -1 : 
						(r1.getLiquidacion() > r2.getLiquidacion()) ? 1 : 0;
					return compare;
				}
			});
			
			try {
				trans.commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				HibernateUtilPruebaCn.closeSession(sesion);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
//			Rptmcaja010Xls xls = new Rptmcaja010Xls(lstDtaXls, "Todas las Companias", "c:\\_TransaccionesTcredito-server.xlsx" );
//			xls.crearXlsTransaccionesIR();
			

			ArrayList<ReporteRetencionIR> lstDtaXls1 = new ArrayList<ReporteRetencionIR>();
			
			
			BigDecimal totalsiniva = BigDecimal.ZERO;
			BigDecimal bdMntExonExen  = BigDecimal.ZERO;
			BigDecimal ivaEnPago =  BigDecimal.ZERO;
			
			for (ReporteRetencionIR ri : lstDtaXls) {
				
				ivaEnPago = new BigDecimal(ivaPrcnt.toString());
						
				//&& ====== Columna de Total sin IVA, credito o contado.
				
				if (ri.getTipodocumento().compareTo("RC") == 0) {
					
					ivaEnPago = BigDecimal.ZERO ;
					totalsiniva = ri.getMontoabono();
					
					if (ri.getMoneda().compareTo("COR") != 0) {
						totalsiniva = totalsiniva.multiply(ri.getTasaoficialdia());
					}
				}
				if (ri.getTipodocumento().compareTo("RC") != 0) {

					totalsiniva = ri.getMontocntdo();

					if (ri.getMoneda().compareTo("COR") != 0) {
						totalsiniva = totalsiniva.multiply(ri.getTasaoficialdia());
					}
					bdMntExonExen = ri.getMontoexonerado().add(ri.getMontoexento());

					if (bdMntExonExen.compareTo(ri.getMontocntdo()) == -1) {
						totalsiniva = totalsiniva.subtract(bdMntExonExen);

						totalsiniva = totalsiniva.divide( BigDecimal.ONE.add(ivaEnPago), 4, RoundingMode.HALF_UP);
					}else{
						ivaEnPago = BigDecimal.ZERO ;
					}
				}
				
				ri.setTotalsininva(totalsiniva);
				ri.setTotaliva(totalsiniva.multiply(ivaEnPago));
				
				ri.setComisionvta(ri.getTotalsininva().multiply( ri.getComision().divide(BigDecimal.valueOf(100))));
				ri.setComisioniva(ri.getTotaliva().multiply( ri.getComision().divide(BigDecimal.valueOf(100))));
				
				ri.setAretencionvta(ri.getTotalsininva() .subtract(ri.getComisionvta()));
				ri.setAretencioniva(ri.getTotaliva() .subtract(ri.getComisioniva()));
				
				ri.setRetencionvta(ri.getAretencionvta().multiply(irPrcnt));
				ri.setRetencioniva(ri.getAretencioniva().multiply(irPrcnt));
				
				ri.setTotalretencion(ri.getRetencionvta().add(ri.getRetencioniva()));
				
				lstDtaXls1.add(ri);
				
			}
			
			String sNombreComp = "";
			SelectItem[] siComps = ddlFiltroCompania.getSelectItems();
			for (SelectItem si : siComps) {
				if(si.getValue().toString().compareTo(sCodcomp) == 0){
					sNombreComp = Divisas.ponerCadenaenMayuscula(si.getLabel().toString());
					break;
				}
			}
			
			m.put("rpt10_Compania", sNombreComp);
			m.put("rpt10_lstTransacTcIrs", lstDtaXls);
			m.put("rpt10_lstTransacTcIrsDsply", lstDtaXls1);
			
			lstTransacTcIrs = lstDtaXls1;
			
			gvTransacTcIrs.dataBind();
			gvTransacTcIrs.setPageIndex(0);
 
			
		} catch (Exception e) {
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
		} 
	}
	
	
	
	
	/**************************************************
	 *  Método: "+PropertiesSystem.ESQUEMA+" / com.casapellas.reportes /generarReporteMcaja010
	 *  Descrp: 
	 *	Fecha:  Jan 4, 2013 
	 *  Autor:  CarlosHernandez
	 ***/
	@SuppressWarnings("unchecked")
	public void generarReporteMcaja010() {
		boolean newCn = false;
		Session sesion = null;
		Transaction trans = null;
		
		try {
			
			m.remove("rpt10_Compania");
			m.remove("rpt10_lstTransacTcIrs");
			m.remove("rpt10_lstTransacTcIrsDsply");
			lstTransacTcIrs = new ArrayList<ReporteRetencionIR>();
			
			String sCodigoEx = "EXE";
			String sCodcomp = ddlFiltroCompania.getValue().toString();
			String sMoneda  = ddlFiltroMoneda.getValue().toString();
			int caid = 0;
			
			if(sMoneda.trim().compareTo("SMO") == 0)
				sMoneda = "";

			Date dtInicio = new Date();
			Date dtFin = new Date();
			Calendar calFechaHoy = Calendar.getInstance();
			
			if(dcFechaInicio.getValue() == null){
				calFechaHoy.add(Calendar.MONTH, -3);
				calFechaHoy.set(Calendar.DATE, calFechaHoy
									.getActualMinimum(Calendar.DATE));
				dtInicio = calFechaHoy.getTime();
			}else{
				dtInicio =  (Date)dcFechaInicio.getValue(); 
			}
			if(dcFechaFinal.getValue() == null){
				calFechaHoy = Calendar.getInstance();
				calFechaHoy.set(Calendar.DATE, calFechaHoy
									.getActualMaximum(Calendar.DATE));
				dtFin = calFechaHoy.getTime();
			}else{
				dtFin =  (Date)dcFechaFinal.getValue(); 
			}
			if(dtFin.before(dtInicio)){
				calFechaHoy.setTime(dtInicio);
				dtInicio = dtFin;
				dtFin = calFechaHoy.getTime();
			}
			
			//&& ===== Validar que la cantidad de dias no se mayor de 182, (seis meses)
			GregorianCalendar cIni = (GregorianCalendar) GregorianCalendar.getInstance();
			GregorianCalendar cFin = (GregorianCalendar) GregorianCalendar.getInstance();
			cIni.setTime(dtInicio);
			cFin.setTime(dtFin);
			int rango = 0;
			
			if (cIni.get(Calendar.YEAR) == cFin.get(Calendar.YEAR)) {
				rango = cFin.get(Calendar.DAY_OF_YEAR) - cIni.get(Calendar.DAY_OF_YEAR);
	        } else {
	            int diasAnyo = cIni.isLeapYear(cIni.get(Calendar.YEAR)) 
	            						? 366 : 365;
	            int rangoAnyos = cFin.get(Calendar.YEAR) - 
	            				 cIni.get(Calendar.YEAR);
	            rango = (rangoAnyos * diasAnyo) + (cFin.get(Calendar.DAY_OF_YEAR)
	            		- cIni.get(Calendar.DAY_OF_YEAR));
	        }
			if(rango > 182){
				calFechaHoy.setTime(dtFin);
				calFechaHoy.add(Calendar.DATE, -182);
				dtInicio = calFechaHoy.getTime();
			}
			
			//&& ======== Buscar las transacciones y crear el objeto para el reporte y grid
			sesion = HibernateUtilPruebaCn.currentSession();
			trans = (newCn = !(sesion.getTransaction().isActive())) ? sesion
					.beginTransaction() : sesion.getTransaction();
			
			Criteria cr = sesion.createCriteria(Vrecibostcir.class);
			cr.add(Restrictions.eq("id.estado",""));
			
			if(sCodcomp.compareTo("99999") != 0)
				cr.add(Restrictions.eq("id.codcomp", sCodcomp));
			
			if(sMoneda.compareTo("") != 0)
				cr.add(Restrictions.eq("id.moneda", sMoneda));
			
			List<Integer> cajas = new ArrayList<Integer>();
			String codcajasel = ddlFiltroCaja.getValue().toString().trim();
			if(codcajasel.compareTo("999999") != 0 && codcajasel.compareTo("000") != 0){
				cr.add(Restrictions.eq("id.caid", Integer.parseInt(codcajasel) ));
				cajas.add(Integer.parseInt(codcajasel)) ;
			}			
			if( codcajasel.compareTo("000") == 0){
				cajas = new ArrayList<Integer>();
				for (SelectItem si : ddlFiltroCaja.getSelectItems())
					cajas.add(Integer.parseInt(String.valueOf(si.getValue()))) ;
				cr.add(Restrictions.in("id.caid", cajas));
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(dtInicio != null)
				cr.add(Restrictions.ge("id.fecha", sdf.parse(
											sdf.format(dtInicio))));
			if(dtFin != null)
				cr.add(Restrictions.le("id.fecha", 
						sdf.parse( sdf.format(dtFin) )));
			
			ArrayList<Vrecibostcir> lstRec = (ArrayList<Vrecibostcir>) cr.list();

			if(lstRec == null || lstRec.isEmpty()){
				dwMsgValidacion.setWindowState("normal");
				lblValidarArqueo.setValue("No se han encontrado resultados");
				return;
			}

/*			Collections.sort(lstRec, new Comparator<Vrecibostcir>() {
				public int compare(Vrecibostcir r1, Vrecibostcir r2) {
					int iCompFecha = (r1.getId().getFecha().before(r2.getId()
							.getFecha())) ? -1 : (r1.getId().getFecha()
							.after(r2.getId().getFecha())) ? 1 : 0;
					if (iCompFecha == 0)
						iCompFecha = (r1.getId().getNoliquidacion() < r2
								.getId().getNoliquidacion()) ? -1 : (r1.getId()
								.getNoliquidacion() > r2.getId()
								.getNoliquidacion()) ? 1 : 0;
					return iCompFecha;
				}
			});*/
			String sqlFact = "from Recibofac r where r.id.caid =? "
					+ "and r.id.codcomp =? and r.id.numrec =? "
					+ "and r.id.tiporec = ? and r.estado = ''";

			Type[] tprRcfac = new Type[] { IntegerType.INSTANCE,
					StringType.INSTANCE, IntegerType.INSTANCE,
					StringType.INSTANCE };

			String sqlRcs = "from Recibojde r where r.id.caid =? "
					+ " and r.id.numrec =? and r.id.codcomp =?"
					+ " and r.id.tiporec = ? and r.id.tipodoc = 'R'";

			Type[] tprRcs = new Type[] { IntegerType.INSTANCE,
					IntegerType.INSTANCE, StringType.INSTANCE,
					StringType.INSTANCE };

			String sqlA02fct = " from A02factco f where f.id.codcli =? "
					+ "and f.id.fecha =? and f.id.nofactura =? "
					+ "and f.id.tipofactura =?  and f.codcomp =? and f.estado = '' ";

			Type[] tprFacs = new Type[] { IntegerType.INSTANCE,
					IntegerType.INSTANCE, IntegerType.INSTANCE,
					StringType.INSTANCE, StringType.INSTANCE };
			
			String sqlA03fct = " from A03factco f where f.id.codcli =? "
					+ "and f.id.fecha =? and f.id.nofactura =? "
					+ "and f.id.tipofactura =? ";
			
			Type[] tpra03Facs = new Type[] { IntegerType.INSTANCE,
					IntegerType.INSTANCE, IntegerType.INSTANCE,
					StringType.INSTANCE };
			
			if (lstRec == null || lstRec.isEmpty())
				return;

			BigDecimal iva = new BigDecimal("15");//TODO CAMBIAR ESTE VALOR LEIDO DE LA TABLA 
			BigDecimal ir =  BigDecimal.ONE;
			
			BigDecimal ivaPrcnt = BigDecimal.ZERO; 
			BigDecimal irPrcnt  = BigDecimal.ZERO;
			
			/*F55ca014 f14 =  CompaniaCtrl.obtenerF55ca014 (caid, sCodcomp);
			if(f14 != null && f14.getId().getC4trir().compareTo(BigDecimal.ZERO) == 1 )
				ir = f14.getId().getC4trir();*/
				
			//&& ===== obtener las tasas IRs para las cajas seleccionadas.
			List<F55ca014> tasasIr = (ArrayList<F55ca014>) 
					sesion.createCriteria(F55ca014.class)
					.add(Restrictions.eq("id.c4stat", "A"))
					.add(Restrictions.in("id.c4id", cajas)).list();
			System.out.println("lista de irs "+tasasIr.toString());
			
			
			BigDecimal bmontofac = BigDecimal.ZERO;
			BigDecimal bmontopago =  BigDecimal.ZERO;
			BigDecimal bmontoexon =  BigDecimal.ZERO;
			BigDecimal bmontoexen =  BigDecimal.ZERO;
			
			Query qrFactura = null;
			Query qrA02fct = null;
			Query qrA03fct = null;
			
			ArrayList<ReporteRetencionIR> lstDtaXls = new ArrayList<ReporteRetencionIR>();
			for (final Vrecibostcir v : lstRec) {
				System.out.println(" transaccion " + v.getId().getCliente() + " >> recibo " +v.getId().getNumrec() +" monto: " + v.getId().getMonto() );
				F55ca014 f14 = (F55ca014)
						CollectionUtils.find(tasasIr, new Predicate(){
							public boolean evaluate(Object o) {
								F55ca014 comp = (F55ca014)o;
								return
									comp.getId().getC4id() == v.getId().getCaid() && 
									comp.getId().getC4rp01().trim().toLowerCase()
									.compareTo(v.getId().getCodcomp()
										.trim().toLowerCase()) == 0 ;
							}});
				ir = f14.getId().getC4trir();
				
				//System.out.println(" tasa de ir "+ir);
				
				ivaPrcnt = iva.divide(new BigDecimal("100"),4, RoundingMode.HALF_UP);
				irPrcnt  =  ir.divide(new BigDecimal("100"),4, RoundingMode.HALF_UP);
				
				// &&& ==== Procedimmiento para tarjetas de contado.
				if (v.getId().getTiporec().compareTo("CO") == 0) {

					qrFactura = sesion.createQuery(sqlFact);
					Object[] oValorP = new Object[] {
							Integer.valueOf(v.getId().getCaid()),
							v.getId().getCodcomp(),
							Integer.valueOf(v.getId().getNumrec()),
							v.getId().getTiporec() };
					qrFactura.setParameters(oValorP, tprRcfac);

					ArrayList<Recibofac> lstFctxRec = 
						(ArrayList<Recibofac>) qrFactura.list();

					if (lstFctxRec == null || lstFctxRec.isEmpty())
						continue;

					// &&& ==== Buscar la factura original y determinar si es exonerada (toda la factura sin IVA )
					int montosexonerado = 0;
					int montosexentos   = 0;
					for (Recibofac recfac : lstFctxRec) {
						
						qrA02fct = sesion.createQuery(sqlA02fct);
						oValorP = new Object[] {
								Integer.valueOf(recfac.getId().getCodcli()),
								Integer.valueOf(recfac.getId().getFecha()),
								Integer.valueOf(recfac.getId().getNumfac()),
								String.valueOf(recfac.getId().getTipofactura()),
								String.valueOf(recfac.getId().getCodcomp()) };
						qrA02fct.setParameters(oValorP, tprFacs);
						
						A02factco facOriginal = (A02factco) qrA02fct
								.setMaxResults(1).uniqueResult();
						
						if(facOriginal == null)
							continue;
						
						recfac.setTasafactura(facOriginal.getTasa());
						
						
						//&& ======== Factura Exonerada
						if (facOriginal.getTotal() == facOriginal.getSubtotal())
							
							montosexonerado += facOriginal.getTotal();
					
						else{
						
							//&& ===== Verificar que la factura tenga detalles exentos de iva
							qrA03fct = sesion.createQuery(sqlA03fct);
							oValorP = new Object[] {
								Integer.valueOf(recfac.getId().getCodcli()),
								Integer.valueOf(recfac.getId().getFecha()),
								Integer.valueOf(recfac.getId().getNumfac()),
								String.valueOf(recfac.getId().getTipofactura()) 
								};
							
							qrA03fct.setParameters(oValorP, tpra03Facs);
							
							List<A03factco> facturaDet = qrA03fct.list();
							
							if(facturaDet == null || facturaDet.isEmpty())
								continue;
							
							for (A03factco dtFc : facturaDet) {
								if (dtFc.getId().getImpuesto().trim().compareToIgnoreCase(
										(sCodigoEx)) == 0)
									montosexentos += dtFc.getId().getPreciounit()
											* dtFc.getId().getCant();
							}
						}
					}
					bmontoexon = new BigDecimal(String.valueOf(BigDecimal
							.valueOf(montosexonerado).divide(
									BigDecimal.valueOf(100), 2,
									RoundingMode.HALF_UP)));
					bmontoexen = new BigDecimal(String.valueOf(BigDecimal
							.valueOf(montosexentos).divide(
									BigDecimal.valueOf(100), 2,
									RoundingMode.HALF_UP)));
					
					//&& ==== Conservar el monto del pago con el iva aplicado
					bmontofac = v.getId().getMonto();
					
					// && ==== segmentar el pago entre todas las facturas.
					bmontofac = bmontofac.divide( new BigDecimal(
								lstFctxRec.size()), 2, RoundingMode.HALF_UP);
					bmontofac = Divisas.roundBigDecimal(bmontofac);
					
					bmontopago = v.getId().getMonto().divide( new BigDecimal(
							lstFctxRec.size()), 2, RoundingMode.HALF_UP);
					bmontopago = Divisas.roundBigDecimal(bmontopago);
					
					bmontoexon = bmontoexon.divide( new BigDecimal(
							lstFctxRec.size()), 2, RoundingMode.HALF_UP);
					bmontoexon = Divisas.roundBigDecimal(bmontoexon);
					
					bmontoexen = bmontoexen.divide( new BigDecimal(
							lstFctxRec.size()), 2, RoundingMode.HALF_UP);
					bmontoexen = Divisas.roundBigDecimal(bmontoexen);
					
					// && ==== Crear el registro para el reporte.
					for (Recibofac rf : lstFctxRec) {
						ReporteRetencionIR r = cargarDatosRpt(v);
						r.setTasafactura(rf.getTasafactura());
						r.setTipodocumento(rf.getId().getTipofactura());
						r.setNodocumento(rf.getId().getNumfac());
						r.setMontoexonerado(bmontoexon);
						r.setMontoexento(bmontoexen);
						r.setMontocntdo(bmontofac);
						r.setMontopago(bmontopago);
						r.setTotalsininva(bmontofac);
						r.setTotaliva(bmontofac.multiply(ivaPrcnt));
						r.setPrcntCmsIr(ir);
						r.setPrcntCmsIva(iva);
						lstDtaXls.add(r);
					}
				
				} else {
					Query qrRecibos = sesion.createQuery(sqlRcs).setMaxResults(1);
					Object[] oValorP = new Object[] {
							Integer.valueOf(v.getId().getCaid()),
							Integer.valueOf(v.getId().getNumrec()),
							v.getId().getCodcomp(), v.getId().getTiporec() };
					qrRecibos.setParameters(oValorP, tprRcs);

					ArrayList<Recibojde> lstNoDocxRec = 
						(ArrayList<Recibojde>) qrRecibos.list();

					if (lstNoDocxRec == null || lstNoDocxRec.isEmpty())
						continue;
					
					for (Recibojde rf : lstNoDocxRec) {
						ReporteRetencionIR r = cargarDatosRpt(v);
						r.setTipodocumento("RC");
						r.setNodocumento(rf.getId().getRecjde());
						r.setMontopago(v.getId().getMonto());
						r.setMontoabono(v.getId().getMonto());
						r.setMontocntdo(BigDecimal.ZERO);
						r.setTotalsininva(BigDecimal.ZERO);
						r.setPrcntCmsIr(ir);
						r.setPrcntCmsIva(iva);
						r.setTasafactura(v.getId().getTasa());
						lstDtaXls.add(r);
					}
				}
			}

			ArrayList<ReporteRetencionIR> lstDtaXls1 = new ArrayList<ReporteRetencionIR>();
			
			
			BigDecimal totalsiniva = BigDecimal.ZERO;
			BigDecimal bdMntExonExen  = BigDecimal.ZERO;
			BigDecimal ivaEnPago =  BigDecimal.ZERO;
			
			for (ReporteRetencionIR ri : lstDtaXls) {
				
				ivaEnPago = new BigDecimal(ivaPrcnt.toString());
						
				//&& ====== Columna de Total sin IVA, credito o contado.
				
				if (ri.getTipodocumento().compareTo("RC") == 0) {
					
					ivaEnPago = BigDecimal.ZERO ;
					totalsiniva = ri.getMontoabono();
					
					if (ri.getMoneda().compareTo("COR") != 0) {
						totalsiniva = totalsiniva.multiply(ri
								.getTasaoficialdia());
					}
				}
				if (ri.getTipodocumento().compareTo("RC") != 0) {

					totalsiniva = ri.getMontocntdo();

					if (ri.getMoneda().compareTo("COR") != 0) {
						totalsiniva = totalsiniva.multiply(ri
								.getTasaoficialdia());
					}
					bdMntExonExen = ri.getMontoexonerado().add(
							ri.getMontoexento());

					if (bdMntExonExen.compareTo(ri.getMontocntdo()) == -1) {
						totalsiniva = totalsiniva.subtract(bdMntExonExen);

						totalsiniva = totalsiniva.divide(
								BigDecimal.ONE.add(ivaEnPago), 4,
								RoundingMode.HALF_UP);
					}else{
						ivaEnPago = BigDecimal.ZERO ;
					}
				}
				
				ri.setTotalsininva(totalsiniva);
				ri.setTotaliva(totalsiniva.multiply(ivaEnPago));
				
				ri.setComisionvta(ri.getTotalsininva().multiply(
						ri.getComision().divide(BigDecimal.valueOf(100))));
				ri.setComisioniva(ri.getTotaliva().multiply(
						ri.getComision().divide(BigDecimal.valueOf(100))));
				
				ri.setAretencionvta(ri.getTotalsininva()
							.subtract(ri.getComisionvta()));
				ri.setAretencioniva(ri.getTotaliva()
						.subtract(ri.getComisioniva()));
				
				ri.setRetencionvta(ri.getAretencionvta().multiply(irPrcnt));
				ri.setRetencioniva(ri.getAretencioniva().multiply(irPrcnt));
				
				ri.setTotalretencion(ri.getRetencionvta()
						.add(ri.getRetencioniva()));
				
				lstDtaXls1.add(ri);
			}
			String sNombreComp = "";
			SelectItem[] siComps = ddlFiltroCompania.getSelectItems();
			for (SelectItem si : siComps) {
				if(si.getValue().toString().compareTo(sCodcomp) == 0){
					sNombreComp = Divisas.ponerCadenaenMayuscula(si.getLabel().toString());
					break;
				}
			}
			
			m.put("rpt10_Compania", sNombreComp);
			m.put("rpt10_lstTransacTcIrs", lstDtaXls);
			m.put("rpt10_lstTransacTcIrsDsply", lstDtaXls1);
			lstTransacTcIrs = lstDtaXls1;
			gvTransacTcIrs.dataBind();
			gvTransacTcIrs.setPageIndex(0);
			
			
			Rptmcaja010Xls xls = new Rptmcaja010Xls(lstDtaXls, "Todas las Companias", "c:\\_TransaccionesTcredito.xlsx" );
			xls.crearXlsTransaccionesIR();
			
			
		} catch (Exception e) {e.printStackTrace();
//			LogCrtl.imprimirError(e);
		e.printStackTrace();
		}finally{
			if(newCn && trans != null && trans.isActive() ){
				try {  trans.commit(); } 
				catch (Exception e2) { }
				try {  HibernateUtilPruebaCn.closeSession(sesion); }
				catch (Exception e2) { }
			}
		}
	}
	/* ******************** fin de metodo generarReporteMcaja010 ****************************/
	public ReporteRetencionIR cargarDatosRpt(Vrecibostcir v ){
		ReporteRetencionIR r = new ReporteRetencionIR(); 
		
		try {
			
			r.setCodunineg(v.getId().getCodunineg());
			r.setMontoexento(BigDecimal.ZERO);
			r.setMontoexonerado(BigDecimal.ZERO);
			r.setFecha(v.getId().getFecha());
			r.setLiquidacion(v.getId().getNoliquidacion());
			r.setMoneda(v.getId().getMoneda());
			r.setNoafiliado(v.getId().getAfiliado().trim());
			
			r.setMontoabono(BigDecimal.ZERO);
			r.setMontocntdo(BigDecimal.ZERO);
			r.setNodocumento(0); 
			
			r.setTasa(v.getId().getTasa());
			r.setTasaoficialdia( v.getId().getTasaoficialdia() );
			r.setTiporecibo(v.getId().getTiporec());
			r.setNumerorecibo(v.getId().getNumrec());
			r.setCodigocaja(v.getId().getCaid());
			r.setCodigocompania(v.getId().getCodcomp().trim());
			
			r.setMontopago(v.getId().getMonto());
			
			r.setComision(v.getId().getComision());
			
		} catch (Exception e) {
			System.out.println(" com.casapellas.socketpos " + new Date());
			System.out
					.println(""+PropertiesSystem.ESQUEMA+": Excepción capturada en :cargarDatosRpt Mensaje:\n "
							+ e);
		}
		return r;
	}
/*********************************************************************************/		
	public void  asdf_generarReporteMcaja010(ActionEvent ev){
		String sCodcomp,sMoneda,sMensaje="";
		String sNomcomp="", sFechaReporte, sFechaIni,sFechaFin;
		boolean bValido = true;
		FechasUtil f = new FechasUtil();
		List lstRptmcaja005Hdr = new ArrayList(1);
		
		try {
			
			
			sMoneda  = ddlFiltroMoneda.getValue().toString();
			
			
			if(sMoneda.equals("SMO")){
				bValido = false;
				sMensaje = "Seleccione el valor de la Moneda a utilizar";
			}
			
			
			if(bValido){
				//------------ Manejar las fechas del reporte.
				Date dtInicio=new Date(), dtFin=new Date();				
				if(dcFechaInicio.getValue()!=null)
					dtInicio = (Date)dcFechaInicio.getValue();
				if(dcFechaFinal.getValue()!=null)
					dtFin = (Date)dcFechaFinal.getValue();
				if(dtInicio.compareTo(dtFin) >0){
					dtInicio = (Date)dcFechaFinal.getValue();
					dtFin    = (Date)dcFechaInicio.getValue();
				}
				dcFechaInicio.setValue(dtInicio);
				dcFechaFinal.setValue(dtFin);
				
				//------------- LLenar los datos para el encabezado del reporte.
				RptmcajaHeader rh = new RptmcajaHeader();				
				rh.setCodcomp("");
				rh.setMoneda(sMoneda);
				rh.setNombrecomp(sNomcomp);
				sFechaIni = FechasUtil.formatDatetoString(dtInicio, "dd 'de' MMMM ");
				sFechaFin = FechasUtil.formatDatetoString(dtFin,	"dd 'de' MMMM 'de' yyyy");
				sFechaReporte = FechasUtil.formatDatetoString(new Date(), "dd/MM/yyyy hh:mm:ss a");
				rh.setSfechainicial(sFechaIni);
				rh.setSfechafinal(sFechaFin);
				rh.setSfechareporte(sFechaReporte);
				lstRptmcaja005Hdr.add(rh);
				
					
				//------------ Obtener los recibos para el cuerpo del reporte.
				ReciboCtrl cc = new ReciboCtrl();
				List<ReporteIR> lstRec = cc.buscarRecibosPagadosTC(ddlFiltroCaja.getValue().toString(),sMoneda,dtInicio,dtFin);
				
				lstRec = lstRec==null? new ArrayList(): lstRec;
				
				//--------- Objetos sesión de reporte.
				m.put("rptmcaja010_hd", lstRptmcaja005Hdr);
				m.put("rptmcaja010_bd", lstRec);
				
				//realizar la navegación sobre la misma página aplicar filtros al reporte.
				FacesContext fcInicio = FacesContext.getCurrentInstance();		
				NavigationHandler nhInicio = fcInicio.getApplication().getNavigationHandler();		
				nhInicio.handleNavigation(fcInicio, null, "rptmcaja010");
				
			}
			lblMsgValidacion.setValue(sMensaje);
		} catch (Exception error) {
			System.out.println("Error en Rptmcaja010.generarReporteMcaja010 " + error);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void cargarCompaniaPorCaja(ValueChangeEvent ev) {
		try {

			m.remove("rpt010_lstFiltroCompania");
			lstFiltroCompania = getLstFiltroCompania();

			m.put("rpt010_lstFiltroCompania", lstFiltroCompania);
			ddlFiltroCompania.dataBind();
			
			int caid = Integer.parseInt(ddlFiltroCaja.getValue().toString());
			String codcomp = ddlFiltroCompania.getValue().toString();
			lstFiltroMoneda = monedasCajaCompania(caid, codcomp);
			
			m.put("rpt010_lstFiltroMoneda", lstFiltroMoneda);
			ddlFiltroMoneda.dataBind();
			
		} catch (Exception e) {
//			System.out.println("Error al cargar companias por caja " + e);
//			LogCrtl.imprimirError(e);
			e.printStackTrace();
		} 
	}
	
	public List<SelectItem> monedasCajaCompania(int caid, String codcomp){
		List<SelectItem> lstFiltroMoneda = null;
		
		try {

			lstFiltroMoneda = new ArrayList<SelectItem>();
			lstFiltroMoneda.add(new SelectItem("SMO", "Todas",
					"Todas las monedas"));

			if (codcomp.compareTo("99999") == 0)
				return lstFiltroMoneda ;

			String[] monedas = new MonedaCtrl().obtenerMonedasxCajaCompania(
					caid, codcomp);

			if (monedas == null || monedas.length == 0)
				return lstFiltroMoneda ;

			for (String moneda : monedas)
				lstFiltroMoneda.add(new SelectItem(moneda, moneda, moneda));

		} catch (Exception error) {
//			LogCrtl.imprimirError(error);
			error.printStackTrace();
		} finally{
			if(lstFiltroMoneda == null)
				lstFiltroMoneda = new ArrayList<SelectItem>();
		}
		return lstFiltroMoneda;
	}
	
	/*********************************************************************************/
	/**  Cargar las monedas configuradas para la caja y compañía Seleccionada 		**/
	@SuppressWarnings("unchecked")
	public void obtenerMonedasxCompania(ValueChangeEvent ev) {
		try {

			int caid = Integer.parseInt(ddlFiltroCaja.getValue().toString());
			String sCodcomp = ddlFiltroCompania.getValue().toString();
			
			lstFiltroMoneda = monedasCajaCompania(caid, sCodcomp);
			
			m.put("rpt010_lstFiltroMoneda", lstFiltroMoneda);
			ddlFiltroMoneda.dataBind();
			
		} catch (Exception error) {
//			LogCrtl.imprimirError(error);
			error.printStackTrace();
		} 
	}
/***********************************************************************************************/



	public HtmlDropDownList getDdlFiltroCompania() {
		return ddlFiltroCompania;
	}



	public void setDdlFiltroCompania(HtmlDropDownList ddlFiltroCompania) {
		this.ddlFiltroCompania = ddlFiltroCompania;
	}



	public HtmlDropDownList getDdlFiltroMoneda() {
		return ddlFiltroMoneda;
	}



	public void setDdlFiltroMoneda(HtmlDropDownList ddlFiltroMoneda) {
		this.ddlFiltroMoneda = ddlFiltroMoneda;
	}



	@SuppressWarnings({ "unchecked" })
	public List<SelectItem> getLstFiltroCompania() {
		try{
			
			if (m.containsKey("rpt010_lstFiltroCompania"))
				return lstFiltroCompania = (ArrayList<SelectItem>) 
						m.get("rpt010_lstFiltroCompania");
			
			lstFiltroCompania = new ArrayList<SelectItem>();
			lstFiltroCompania.add( new SelectItem("99999", "Todas","Todas las compañías") ) ;
			
			int caid = Integer.parseInt(ddlFiltroCaja.getValue().toString());
			
			if(caid == 0){
				List<Vcompania> companias = new CompaniaCtrl().obtenerCompaniasCajaJDE();
				if(companias == null || companias.isEmpty()){
					return lstFiltroCompania;
				}
				for (Vcompania vc : companias) {
					lstFiltroCompania.add( new SelectItem(
							vc.getId().getDrky().trim(), 
							vc.getId().getDrdl01().trim(),
							"Compañía "+vc.getId().getDrky().trim() )
					);
				}
				return lstFiltroCompania;
			}
			
			F55ca014[] lstCompsxCaja = new CompaniaCtrl()
								.obtenerCompaniasxCaja(caid);	
			
			if(lstCompsxCaja == null || lstCompsxCaja.length == 0){
				return lstFiltroCompania;
			}
			
			for (F55ca014 dtaComp : lstCompsxCaja) {
				lstFiltroCompania.add( new SelectItem(
						dtaComp.getId().getC4rp01().trim(), 
						dtaComp.getId().getC4rp01d1().trim(),
						"Compañía "+dtaComp.getId().getC4rp01() )
				);
			}
		
		}catch(Exception error){
//			System.out.println("Error en rpt010_lstFiltroCompania" +
//					".getlstFiltroCompania " + error);
//			LogCrtl.imprimirError(error);
			error.printStackTrace();
		}finally{
			m.put("rpt010_lstFiltroCompania", lstFiltroCompania);
		}
		return lstFiltroCompania;
	}
	public void setLstFiltroCompania(List<SelectItem> lstFiltroCompania) {
		this.lstFiltroCompania = lstFiltroCompania;
	}

	@SuppressWarnings("unchecked")
	public List<SelectItem> getLstFiltroMoneda() {

		try {

			if (m.containsKey("rpt010_lstFiltroMoneda"))
				return lstFiltroMoneda = (ArrayList<SelectItem>) m
						.get("rpt010_lstFiltroMoneda");

			lstFiltroMoneda = new ArrayList<SelectItem>();
			lstFiltroMoneda.add(new SelectItem("SMO", "Todas",
					"Todas las monedas"));
			lstFiltroMoneda.add(new SelectItem("COR", "COR", "Cordobas"));
			lstFiltroMoneda.add(new SelectItem("USD", "USD", "DOlares"));

		} catch (Exception error) {
//			LogCrtl.imprimirError(error);
//			System.out.println("Error en Rptmcaja010DAO:getLstFiltroMoneda");
			error.printStackTrace();
		} finally {
			m.put("rpt010_lstFiltroMoneda", lstFiltroMoneda);
		}
		return lstFiltroMoneda;
	}

	public void setLstFiltroMoneda(List<SelectItem> lstFiltroMoneda) {
		this.lstFiltroMoneda = lstFiltroMoneda;
	}
	public HtmlDateChooser getDcFechaFinal() {
		return dcFechaFinal;
	}
	public void setDcFechaFinal(HtmlDateChooser dcFechaFinal) {
		this.dcFechaFinal = dcFechaFinal;
	}



	public HtmlDateChooser getDcFechaInicio() {
		return dcFechaInicio;
	}



	public void setDcFechaInicio(HtmlDateChooser dcFechaInicio) {
		this.dcFechaInicio = dcFechaInicio;
	}
	public Date getFechaactual1() {
		if(m.get("rpt010_fechaactual1") == null){
			
			Calendar cal = Calendar.getInstance();
			if(cal.get(Calendar.DAY_OF_MONTH) > 15){
				cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DATE));
			}else{
				cal.add(Calendar.MONTH, -1);
				cal.set(Calendar.DATE, 15);
			}
			fechaactual1 = cal.getTime();
			m.put("rpt010_fechaactual1", fechaactual1);
		}
		return fechaactual1;
	}
	public void setFechaactual1(Date fechaactual1) {
		this.fechaactual1 = fechaactual1;
	}
	@SuppressWarnings("unchecked")
	public Date getFechaactual2() {
		if(m.get("rpt010_fechaactual2")==null){
			
			Calendar cal = Calendar.getInstance();
			if(cal.get(Calendar.DAY_OF_MONTH) > 15){
				cal.set(Calendar.DATE, 15);
			}else{
				cal.add(Calendar.MONTH, -1);
				cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
			}
			fechaactual2 = cal.getTime();
			m.put("rpt010_fechaactual2", fechaactual2);
		}
		return fechaactual2;
	}
	public void setFechaactual2(Date fechaactual2) {
		this.fechaactual2 = fechaactual2;
	}


	public HtmlOutputText getLblMsgValidacion() {
		return lblMsgValidacion;
	}


	public void setLblMsgValidacion(HtmlOutputText lblMsgValidacion) {
		this.lblMsgValidacion = lblMsgValidacion;
	}


	public HtmlDropDownList getDdlFiltroCaja() {
		return ddlFiltroCaja;
	}


	public void setDdlFiltroCaja(HtmlDropDownList ddlFiltroCaja) {
		this.ddlFiltroCaja = ddlFiltroCaja;
	}


	@SuppressWarnings("unchecked")
	public List<SelectItem> getLstFiltroCajas() {
		try {
			if(m.get("rpt0010_lstFiltroCajas") == null){
				
				Vautoriz[] vaut = (Vautoriz[]) m.get("sevAut");
				List<Vf55ca01> lstcaja  = (ArrayList<Vf55ca01>)m.get("lstCajas");
				Vf55ca01 caja = (Vf55ca01)lstcaja.get(0);
				List<Vf55ca01> lstCaContador = null;
				
				if(vaut[0].getId().getCodper().compareTo("P000000004") == 0 ||
				   vaut[0].getId().getCodper().compareTo("P000000025") == 0	){
					lstCaContador = new CtrlCajas().getAllCajas();
				}else{
					lstCaContador = new CtrlCajas()
					.obtenerCajasxContador(caja.getId().getCacont());
				}
				lstFiltroCajas = new ArrayList<SelectItem>();
				lstFiltroCajas.add( new SelectItem("000","Todas","Todas las cajas") );
				
				if(lstCaContador!=null && lstCaContador.size()>0){
					for(int i=0;i<lstCaContador.size();i++){
						Vf55ca01 v = (Vf55ca01)lstCaContador.get(i);
						lstFiltroCajas.add( new SelectItem(v.getId().getCaid() + "",
								v.getId().getCaid() + ": "+ v.getId().getCaname().trim(),
								v.getId().getCaid()	+ ": "+ v.getId().getCaname().trim() + ": "
									+ v.getId().getCacatinom().trim()  ));
					}
					m.put("rpt0010_lstFiltroCajas", lstFiltroCajas);
					
				}else{
					lstFiltroCajas.add(new SelectItem("999999","Sin Configuración","No se encontró configuración de caja"));
				}
			}else{
				lstFiltroCajas =(ArrayList<SelectItem>)m.get("rpt0010_lstFiltroCajas"); 
			}
		} catch (Exception error) {
			System.out.println("Error en Rptmcaja0010DAO.getLstFiltroCajas  " + error);
		}
		return lstFiltroCajas;
	}
public void setLstFiltroCajas(List<SelectItem> lstFiltroCajs) {
		this.lstFiltroCajas = lstFiltroCajs;
	}
	public HtmlGridView getGvTransacTcIrs() {
		return gvTransacTcIrs;
	}
	public void setGvTransacTcIrs(HtmlGridView gvTransacTcIrs) {
		this.gvTransacTcIrs = gvTransacTcIrs;
	}
	@SuppressWarnings("unchecked")
	public List<ReporteRetencionIR> getLstTransacTcIrs() {
		try {
			lstTransacTcIrs = (m.get("rpt10_lstTransacTcIrsDsply") == null) ?
						new ArrayList<ReporteRetencionIR>()
						: (ArrayList<ReporteRetencionIR>)
							m.get("rpt10_lstTransacTcIrsDsply");
		} catch (Exception e) {
			System.out.println(" com.casapellas.reportes " + new Date());
			System.out.println(""+PropertiesSystem.ESQUEMA+": Excepción capturada en " +
					":getLstTransacTcIrs Mensaje:\n "+ e);
			lstTransacTcIrs = new ArrayList<ReporteRetencionIR>();
		}
		return lstTransacTcIrs;
	}
	public void setLstTransacTcIrs(List<ReporteRetencionIR> lstTransacTcIrs) {
		this.lstTransacTcIrs = lstTransacTcIrs;
	}
	public HtmlDialogWindow getDwMsgValidacion() {
		return dwMsgValidacion;
	}
	public void setDwMsgValidacion(HtmlDialogWindow dwMsgValidacion) {
		this.dwMsgValidacion = dwMsgValidacion;
	}
	public HtmlOutputText getLblValidarArqueo() {
		return lblValidarArqueo;
	}
	public void setLblValidarArqueo(HtmlOutputText lblValidarArqueo) {
		this.lblValidarArqueo = lblValidarArqueo;
	}	
	
}

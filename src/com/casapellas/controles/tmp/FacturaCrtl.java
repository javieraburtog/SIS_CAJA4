package com.casapellas.controles.tmp;
/**
 * CASA PELLAS S.A.
 * Creado por.........: Juan Carlos Ñamendi Pineda
 * Fecha de Creación..: 28/02/2009
 * Última modificación: 01/04/2011
 * Modificado por.....:	Juan Carlos Ñamendi Pineda
 * 
 */

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.faces.component.UIInput;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.jsp.tagext.TryCatchFinally;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import com.casapellas.dao.FacContadoDAO;
import com.casapellas.entidades.Df4211;
import com.casapellas.entidades.Dfactjdecon;
import com.casapellas.entidades.Dfactura;
import com.casapellas.entidades.F55ca014;
import com.casapellas.entidades.F55ca017;
import com.casapellas.entidades.Hf4211;
import com.casapellas.entidades.Hfactjdecon;
import com.casapellas.entidades.Hfactura;
import com.casapellas.entidades.Hfacturajde;
import com.casapellas.entidades.Vdfactura;
import com.casapellas.entidades.Vf55ca01;
import com.casapellas.entidades.Vhfactura;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.navegacion.As400Connection;
import com.casapellas.util.CalendarToJulian;
import com.casapellas.util.Divisas;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.JulianToCalendar;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.PropertiesSystem;
import com.infragistics.faces.grid.component.Cell;
import com.infragistics.faces.grid.component.GridView;
import com.infragistics.faces.grid.component.RowItem;
import com.infragistics.faces.grid.component.html.HtmlGridView;
import com.infragistics.faces.grid.event.CellValueChangeEvent;
import com.infragistics.faces.grid.event.SelectedRowsChangeEvent;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
import com.infragistics.faces.shared.smartrefresh.SmartRefreshManager;
import com.infragistics.faces.window.component.DialogWindow;

public class FacturaCrtl {
	Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();	
	private DialogWindow dgwDetalleContado;
	private HtmlGridView gvHfacturasContado;
	private List lstHfacturasContado = null;
	private GridView gvDfacturasContado;
	private List lstDfacturasContado = null;
	private UIInput cmbBusqueda;
	private List lstBusqueda = null;
	private UIInput txtParametro;
	
	private HtmlOutputText txtMensaje;
	private String txtFechaFactura = "";
	private String txtNofactura = "";
	private String txtCliente = "";
	private String txtCodigoCliente = "";
	private String txtUnineg = "";
	private String txtSubtotal = "";
	private double txtIva;
	private String txtTotal = "";
	private String sMensaje = "";
	
	private HtmlOutputText lblTasaDetalle;
	private HtmlOutputText txtTasaDetalle;
	private HtmlOutputText lblMonedaDomesticaCon;
	
	private DialogWindow dgwDetalleEnReciboContado;
	private GridView gvDfacturasContadoRecibo;
	
	private HtmlDropDownList cmbMonedaDetalle;
	private List lstMonedasDetalle;
	
	private List lstMonedasDetalleReciboContado;
	private HtmlDropDownList cmbMonedaDetalleReciboContado;
	
	private DialogWindow dgwMensajeDetalleContado;
	
	private HtmlOutputText lblMontoAplicar;
	
	private List lstFiltroFacturas;
	private HtmlDropDownList cmbFiltroFacturas;
///////////////////////////////////////////////////////	
	private DialogWindow dgwDetalleCredito;
	private GridView gvHfacturasCredito;
	private List lstHfacturasCredito = null;
	private GridView gvDfacturasCredito;
	private List lstDfacturasCredito = null;
	private UIInput cmbBusquedaCredito;
	private List lstBusquedaCredito = null;
	private UIInput txtParametroCredito;
	
	private String txtFechaFacturaCred = "";
	private String txtNofacturaCred = "";
	private String txtClienteCred = "";
	private String txtCodigoClienteCred = "";
	private String txtUninegCred = "";
	private String txtSubtotalCred = "";
	private String txtIvaCred = "";
	private String txtTotalCred = "";
	private String txtPendiente = "";

	private GridView gvfacturasSelecCredito;
	private List selectedFacsCredito = null;
	
	private DialogWindow dgwMensajeDetalleCredito;
	private HtmlOutputText lblMontoAplicarCredito;
	
	//Facturas selecciondas - definicion
	private List selectedFacs = null;
	private GridView gvfacturasSelec;
	
	
	private HtmlDropDownList cmbMonedaDetalleCredito;
	
	private DialogWindow dgwDetalleEnReciboCredito;
	private List lstMonedasDetalleReciboCredito;
	
	private HtmlDropDownList cmbMonedaDetalleReciboCredito;
	
	private GridView gvDfacturasCreditoRecibo;
	
	private DialogWindow dgwMensajeDetalleCreditoFac;
/*************************************************************************************************************************/
	@SuppressWarnings("unchecked")
	public void sincronizarFacturas(){
		F55ca017[] f55ca017 = null;
		String sLinea = "";
		FechasUtil fecUtil = new FechasUtil();
		int iFechaActual = 0;
		FacturaContadoCtrl fCtrl = new FacturaContadoCtrl();
		List lstF4211Facs = new ArrayList(),lstDet = new ArrayList(), lstBorrarItems = new ArrayList();
		Hf4211 h = null;
		Df4211 d = null;
		Vhfactura vh = null;
		boolean bHecho = false, bAnular = true;
		Connection cn = null;
		try{
			//unidades de negocios configuradas en caja
			
			//&& ==================== Verificar si debe sincronizar facturas.
			Vf55ca01 f55ca01 = (Vf55ca01) ((List) m.get("lstCajas")).get(0);
			f55ca017 = (m.get("f55ca017") == null)?
					new CtrlCajas().obtenerUniNegCaja(f55ca01.getId().getCaid(), f55ca01.getId().getCaco()):
					(F55ca017[]) m.get("f55ca017");
			m.put("f55ca017", f55ca017);
			
			String sComprobLinea = "";
			boolean bSincroniza = false;
			if(f55ca017 != null){
				for(int i = 0; i < f55ca017.length;i++){
					sComprobLinea = f55ca017[i].getId().getC7mcul().trim().substring(2,4);
					if(sComprobLinea.equals("04")|| sComprobLinea.equals("06") || sComprobLinea.equals("11")|| 
						sComprobLinea.equals("22")|| sComprobLinea.equals("23") || sComprobLinea.equals("24") ){
						bSincroniza = true;
						break;
					}
				}
			}
			if(!bSincroniza)return;

			//&& ====================== =================================================
			
			if(f55ca017 != null){
				cn = As400Connection.getJNDIConnection("DSMCAJA2");
				iFechaActual = fecUtil.obtenerFechaActualJuliana();
				
				for(int i = 0; i < f55ca017.length;i++){

					sLinea = f55ca017[i].getId().getC7mcul().trim().substring(2,4);
					if(sLinea.equals("04")|| sLinea.equals("06") || sLinea.equals("11")|| 
						sLinea.equals("22")|| sLinea.equals("23") || sLinea.equals("24") ){
						
						//buscar facturas en el f4211
						lstF4211Facs = fCtrl.getFacturasFDC(f55ca017[i].getId()
									.getC7mcul(), f55ca017[i].getId().getC7locn(),
									iFechaActual);
						for(int j = 0; j < lstF4211Facs.size();j++){
							h = (Hf4211)lstF4211Facs.get(j);
							
							//comprobar si la factura ya existe en el modulo de caja
							
							vh = fCtrl.comprobarSiExiste(h.getId().getNofactura(), 
									h.getId().getTipofactura(), h.getId().getCodsuc(),
									h.getId().getCodunineg(), h.getId().getCodcli(),
									h.getId().getFecha(),
									h.getId().getHora(),
									h.getId().getEstado());
							
							if(vh == null){// no existe se inserta la factura
								
								//insertarFactura header
								bHecho = fCtrl.insertarFacturaFDC(cn,h);

							}//si existe comprobar q no hayan cambios en la factura
							else{
								lstDet = fCtrl.buscarDetalleFactura(h);
								//1) comprobar si esta totalmente anulada
								for(int a = 0; a < lstDet.size(); a++){
									d = (Df4211)lstDet.get(a);
									if(d.getId().getSdnxtr().equals("999") && d.getId().getSdlttr().equals("980")){//linea anulada
										lstBorrarItems.add(d);
									}else{
										bAnular = false;
									}
								}
								if(bAnular){//Si esta totalmente anulada anular
									bHecho = fCtrl.anularFacturaFDC(cn, h.getId().getNofactura(), 
											h.getId().getTipofactura(), h.getId().getCodsuc(),
											h.getId().getCodunineg(), h.getId().getCodcli(),
											h.getId().getFecha());
								}else{
									
									//&& === Si cambio el monto de la factura en jde, actualizar la factura en caja.
									if(h.getId().getSubtotal().intValue() != vh.getId().getSubtotal().intValue())
										bHecho = 
											fCtrl.actualizarFacturaFDC(cn, h.getId().getNofactura(), 
													h.getId().getTipofactura(), 
													h.getId().getCodsuc(), 
													h.getId().getCodunineg(), 
													h.getId().getCodcli(),
													h.getId().getSubtotal().intValue(),
													h.getId().getTotal().intValue(),
													h.getId().getSubtotalf().intValue(),
													h.getId().getTotalf().intValue(),
													h.getId().getTotalcosto(),
													h.getId().getFecha());	
						
									if(bHecho && lstBorrarItems != null && lstBorrarItems.size() > 0)	
										bHecho = fCtrl.borrarDetalleAnulado(cn,lstBorrarItems);
									
								}
							}
						}
					}
				}		
				//verificar si existe la factura en el a02factco
				cn.commit();
				cn.close();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
/*************************************************************************************************************************/	
	/**********************************************************************************************************************************/
	public List formatDetalle(Hfactura hFac){
		FacturaContadoCtrl facContCtrl = new FacturaContadoCtrl();
		List lstDetalle = null;
		Dfactjdecon[] dFacJde = null;
		Dfactura dFac = null;
		Divisas divisas = new Divisas();
		BigDecimal tasa = new BigDecimal("0");
		double total = 0;
		double ivaTotal = 0;
		double ivaActual = 0;
		double totalfac = 0;
		double equiv = 0;
		String sIvaTotal = "",sTotal = "";
		
		try{
			lstDetalle = facContCtrl.buscarDetalleFactura(hFac.getNofactura(), hFac.getTipofactura(),hFac.getCodsuc(),hFac.getCodunineg());
			dFacJde = new Dfactjdecon[lstDetalle.size()];
			for (int j = 0; j < lstDetalle.size(); j++){
				dFacJde[j] = (Dfactjdecon)lstDetalle.get(j);
				
				double precioUnit = (dFacJde[j].getId().getPreciounit())/100.0;
				//String sPrecionUnit = divisas.formatDouble(precioUnit);
				tasa = hFac.getTasa();
				double factor = dFacJde[j].getId().getFactor()/1000.0;
				dFac = new Dfactura(
						dFacJde[j].getId().getNofactura(),
						dFacJde[j].getId().getTipofactura(),
						dFacJde[j].getId().getCoditem(),
						dFacJde[j].getId().getDescitem(),
						precioUnit,
						dFacJde[j].getId().getCant(),
						dFacJde[j].getId().getImpuesto(),
						factor,
						hFac.getMoneda(),
						tasa
				);
				lstDetalle.remove(j);
				lstDetalle.add(j,dFac);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return lstDetalle;
	}
/**************************************************************************************************************************************/	
	public Hfactura getFacturaOriginal1(int iNodoc,String sTipoFac,String sCodSuc, String sCodComp,String sCodunineg){
		Hfactura hFac = null; 
		Hfactjdecon hFacjde = null;
		List lstFac = null,lstFac2 = null;
		String sql = "from Hfactjdecon as hf where hf.id.nofactura = " +iNodoc+ " and hf.id.tipofactura = '" +sTipoFac+"' and hf.id.codsuc = '"+sCodSuc+"' and hf.id.codcomp = '" +sCodComp+"'" +
				" and trim(hf.id.codunineg) = '" +sCodunineg.trim()+"'";
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = null;	
		
		try{
			tx = session.beginTransaction();
			lstFac = session.createQuery(sql).list();
			tx.commit();
			if(lstFac != null && !lstFac.isEmpty()){
				lstFac2 = FacContadoDAO.formatFactura(lstFac);
				hFac = (Hfactura)lstFac2.get(0);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			try {HibernateUtilPruebaCn.closeSession(session); } catch (Exception e2) {e2.printStackTrace(); }
		}		
		return hFac;
	}
	public static Hfactura getFacturaOriginal(int iNodoc,String sTipoFac,String sCodSuc,
									String sCodComp,String sCodunineg, int fechadoco){
		Hfactura hFac = null; 
		List lstFac = null,lstFac2 = null;
		Session sesion = null;
		
		try{
			
            sesion = HibernateUtilPruebaCn.currentSession();
			
			String sql = " From A02factco f where f.id.nofactura = " + iNodoc
					+ " and f.id.tipofactura = '" + sTipoFac
					+ "' and f.codcomp = '" + sCodComp
					+ "' and trim(f.id.codunineg) = '" + sCodunineg.trim() + "' " ;
			
			if( fechadoco !=  0 )
				sql +=  " and fecha = " + fechadoco ; 
			
			LogCajaService.CreateLog("getFacturaOriginal", "QRY",sql );
			lstFac = sesion.createQuery(sql).list();
			
			if(lstFac != null && !lstFac.isEmpty()){
				lstFac2 = FacContadoDAO.formatFactura(lstFac);
				hFac = (Hfactura)lstFac2.get(0);
			}
		}catch(Exception ex){ 
			LogCajaService.CreateLog("getFacturaOriginal", "ERR", ex.getMessage());
		}		
		return hFac;
	}
	
	
/******************************************************************************************/
/** Método: Obtener una fecha juliana a partir de una fecha tipo Date()
 *	Fecha:  12/01/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public int obtenerFechaActualJuliana(Date dtFecha){
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		int iFechaActual = 0;
		try{
			Calendar calFechaActual = Calendar.getInstance();
			calFechaActual.setTime(dtFecha);
			String sFechaActual = calFechaActual.get(Calendar.DATE)+ "/" + (calFechaActual.get(Calendar.MONTH)+ 1) + "/" + calFechaActual.get(Calendar.YEAR);
			calFechaActual.setTime(new Date(sdf.parse(sFechaActual).getTime()));
			CalendarToJulian julian = new CalendarToJulian(calFechaActual);
			iFechaActual = julian.getDate();	
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return iFechaActual;
	}
/*********************************************************************************************************************/	
	public int obtenerFechaActualJuliana(){
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		int iFechaActual = 0;
		try{
//			obtener fecha actual y convertirla a formato juliano
			Calendar calFechaActual = Calendar.getInstance();
			String sFechaActual = calFechaActual.get(Calendar.DATE)+ "/" + (calFechaActual.get(Calendar.MONTH)+ 1) + "/" + calFechaActual.get(Calendar.YEAR);
			calFechaActual.setTime(new Date(sdf.parse(sFechaActual).getTime()));
			CalendarToJulian julian = new CalendarToJulian(calFechaActual);
			iFechaActual = julian.getDate();	
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return iFechaActual;
	}
/*********************************************************************************************************************/
	/**BUSCAR FACTURAS DE CONTADO POR PARAMETRO, FECHA EN FORMATO JULIANO  Y TIPO DE BUSQUEDA(1 = por nombre de cliente, 2 = por codigo de cliente, 3 = por numero de factura)**/
	public List buscarAllFacturas(String sParametro,String[] sTipoFactura,String sMoneda, int iTipo, F55ca017[] f55ca017, String sEstado,List lstLocalizaciones,String sCompania,Date dDesde,Date dHasta){
		List lstFacturas = null,f55ca017_1 = new ArrayList(), f55ca017_2 = new ArrayList();
		String sql = "", sql2 = "";
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = null;
		boolean bPaso = false;
		String sLoc = "";
		boolean bHayLoc = false,bSoloLocs = false;
		FechasUtil fec = new FechasUtil();
		int iDesde = 0, iHasta = 0;
		try{
			iDesde = fec.DateToJulian(dDesde);
			iHasta = fec.DateToJulian(dHasta);
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
			//
			int iFecha = obtenerFechaActualJuliana();
			sql = "select * from "+PropertiesSystem.ESQUEMA+".Vhfactura as f where ";
			tx = session.beginTransaction();	
			switch (iTipo){
			case(1):
				//Busqueda por nombre de cliente
				if (!sParametro.equals("")){//hay nombre de cliente
					sql = sql + " trim(f.nomcli) like '%"+sParametro.trim().toUpperCase()+"%' ";
					bPaso = true;
				}
				//agregar moneda a consulta
				if (!sParametro.equals("") && !sMoneda.equals("01")){
					sql = sql + " and f.moneda = '"+ sMoneda +"' ";
					bPaso = true;
				}
				else if(sParametro.equals("") && !sMoneda.equals("01")){
					sql = sql + " f.moneda = '"+ sMoneda +"' ";
					bPaso = true;
				}
				//agregar compania a consulta
				if (!sParametro.equals("") && !sCompania.equals("01")){
					sql = sql + " and f.codcomp = '"+ sCompania +"' ";
					bPaso = true;
				}
				else if(sParametro.equals("") && !sCompania.equals("01")){
					sql = sql + " and f.codcomp = '"+ sCompania +"' ";
					bPaso = true;
				}
				//agregar Estado
				if(!sParametro.equals("") && sEstado.equals("02")){//facturas no anuladas
					sql = sql + " and trim(f.estado) = ''";
					bPaso = true;
				}
				else if(!sParametro.equals("") && sEstado.equals("03")){//facturas anuladas
					sql = sql + " and trim(f.estado) = 'A'";
					bPaso = true;
				}
				else if (sParametro.equals("") && sMoneda.equals("01") && sCompania.equals("01")){//Estado va primero
					if(sEstado.equals("02")){//facturas no anuladas
						sql = sql + "trim(f.estado) = ''";
						bPaso = true;
					}
					else if(sEstado.equals("03")){//facturas anuladas
						sql = sql + " trim(f.estado) = 'A'";
						bPaso = true;
					}
				}else {
					if(sEstado.equals("02")){//facturas no anuladas
						sql = sql + " and trim(f.estado) = ''";
						bPaso = true;
					}
					if(sEstado.equals("03")){//facturas anuladas
						sql = sql + " and trim(f.estado) = 'A'";
						bPaso = true;
					}
				}
				break;
			case(2):
				//Busqueda por codigo de cliente
				if (!sParametro.equals("")){//hay codigo de cliente
					sql = sql + " trim(cast(f.codcli as varchar(8))) like '"+sParametro.trim().toUpperCase()+"%' ";
					bPaso = true;
				}
				//agregar moneda a consulta
				if (!sParametro.equals("") && !sMoneda.equals("01")){
					sql = sql + " and f.moneda = '"+ sMoneda +"' ";
					bPaso = true;
				}
				else if(sParametro.equals("") && !sMoneda.equals("01")){
					sql = sql + " f.moneda = '"+ sMoneda +"' ";
					bPaso = true;
				}
				//agregar compania a consulta
				if (!sParametro.equals("") && !sCompania.equals("01")){
					sql = sql + " and f.codcomp = '"+ sCompania +"' ";
					bPaso = true;
				}
				else if(sParametro.equals("") && !sCompania.equals("01")){
					sql = sql + " and f.codcomp = '"+ sCompania +"' ";
					bPaso = true;
				}
				//agregar Estado
				if(!sParametro.equals("") && sEstado.equals("02")){//facturas no anuladas
					sql = sql + " and trim(f.estado) = ''";
					bPaso = true;
				}
				else if(!sParametro.equals("") && sEstado.equals("03")){//facturas anuladas
					sql = sql + " and trim(f.estado) = 'A'";
					bPaso = true;
				}
				else if (sParametro.equals("") && sMoneda.equals("01")){//Estado va primero
					if(sEstado.equals("02")){//facturas no anuladas
						sql = sql + "trim(f.estado) = ''";
						bPaso = true;
					}
					else if(sEstado.equals("03")){//facturas anuladas
						sql = sql + " trim(f.estado) = 'A'";bPaso = true;
					}
				}else {
					if(sEstado.equals("02")){//facturas no anuladas
						sql = sql + " and trim(f.estado) = ''";bPaso = true;
					}
					if(sEstado.equals("03")){//facturas anuladas
						sql = sql + " and trim(f.estado) = 'A'";
						bPaso = true;
					}
				}
				break;
			case(3):
				//Busqueda por numero de factura
				if (!sParametro.equals("")){//hay numero de factura
					sql = sql + " trim(cast(f.nofactura as varchar(8))) like '"+sParametro.trim().toUpperCase()+"%' ";bPaso = true;
				}
				//agregar moneda a consulta
				if (!sParametro.equals("") && !sMoneda.equals("01")){
					sql = sql + " and f.moneda = '"+ sMoneda +"' ";bPaso = true;
				}
				else if(sParametro.equals("") && !sMoneda.equals("01")){
					sql = sql + " f.moneda = '"+ sMoneda +"' ";bPaso = true;
				}
//				agregar compania a consulta
				if (!sParametro.equals("") && !sCompania.equals("01")){
					sql = sql + " and f.codcomp = '"+ sCompania +"' ";
					bPaso = true;
				}
				else if(sParametro.equals("") && !sCompania.equals("01")){
					sql = sql + " and f.codcomp = '"+ sCompania +"' ";
					bPaso = true;
				}
				//agregar Estado
				if(!sParametro.equals("") && sEstado.equals("02")){//facturas no anuladas
					sql = sql + " and trim(f.estado) = ''";bPaso = true;
				}
				else if(!sParametro.equals("") && sEstado.equals("03")){//facturas anuladas
					sql = sql + " and trim(f.estado) = 'A'";bPaso = true;
				}
				else if (sParametro.equals("") && sMoneda.equals("01")){//Estado va primero
					if(sEstado.equals("02")){//facturas no anuladas
						sql = sql + "trim(f.estado) = ''";bPaso = true;
					}
					else if(sEstado.equals("03")){//facturas anuladas
						sql = sql + " trim(f.estado) = 'A'";bPaso = true;
					}
				}else {
					if(sEstado.equals("02")){//facturas no anuladas
						sql = sql + " and trim(f.estado) = ''";bPaso = true;
					}
					if(sEstado.equals("03")){//facturas anuladas
						sql = sql + " and trim(f.estado) = 'A'";bPaso = true;
					}
				}
				break;
			}
			if(bPaso){
				sql = sql + " and f.subtotal > 0 and f.tipofactura in (";
			}else{
				sql = sql + " f.subtotal > 0 and f.tipofactura in (";
			}
			for (int i = 0; i < sTipoFactura.length; i++){
				if (i == sTipoFactura.length - 1){
					sql = sql + "'" + sTipoFactura[i] + "'";
				}else{
					sql = sql + "'" + sTipoFactura[i] + "',";
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
			sql = sql + ") and f.fecha >= " + iDesde + " and f.fecha <= " + iHasta; 
			
			
			//agregar localizaciones si hay
			if(bHayLoc){
				sql2 = sql2 + " and trim(f.sdlocn) in (";
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
				sql2 = sql2 + ") and f.fecha >= " + iDesde + " and f.fecha <= " + iHasta; 	
			}			
			
			if(bHayLoc){
				sql = sql + " union " + sql2;
			}
			if(bSoloLocs){
				lstFacturas = session.createSQLQuery(sql).addEntity(Vhfactura.class).list();
			}else{
				lstFacturas = session.createSQLQuery(sql2).addEntity(Vhfactura.class).list();	
			}
			tx.commit();
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			try {HibernateUtilPruebaCn.closeSession(session); } catch (Exception e2) {e2.printStackTrace(); }
		}
		return lstFacturas;
	}
/*************************************************************************************************************************************************************/
/**OBTIENE LAS DEVOLUCIONES DEL DIA PARA LA VISTA DE VENTAS DE CONTADO****************************************************************************************/
	public List obtenerFacturasDeldia(String[] sTiposDoc ,F55ca017[] f55ca017,List lstLocalizaciones,String sMoneda){
		List lstFacturas = null, f55ca017_1 = new ArrayList(), f55ca017_2 = new ArrayList();
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = null;
		String sLoc = "";
		boolean bHayLoc = false,bSoloLocs = false;
		String sql = "", sql2 = "";
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
			
			//obtener fecha actual y convertirla a formato juliano		
			int iFechaActual = obtenerFechaActualJuliana();	
			
			tx = session.beginTransaction();
			sql = "select * from "+PropertiesSystem.ESQUEMA+".Vhfactura as f where f.fecha =" + iFechaActual +" and f.subtotal > 0 and trim(f.moneda) = '"+sMoneda+"' and f.tipofactura in (";
			for (int i = 0; i < sTiposDoc.length; i++){
				if (i == sTiposDoc.length - 1){
					sql = sql + "'" + sTiposDoc[i] + "'";
				}else{
					sql = sql + "'" + sTiposDoc[i] + "',";
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
			//
			sql = sql + ")";
			
			//agregar localizaciones si hay
			if(bHayLoc){
				sql2 = sql2 + " and trim(f.sdlocn) in (";
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
				sql2 = sql2 + ")";				
			}			
			
			if(bHayLoc){
				sql = sql + " union " + sql2;
			}
			if(bSoloLocs){
				lstFacturas = session.createSQLQuery(sql).addEntity(Vhfactura.class).list();	
			}else{
				lstFacturas = session.createSQLQuery(sql2).addEntity(Vhfactura.class).list();	
			}
			tx.commit();
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			try {HibernateUtilPruebaCn.closeSession(session); } catch (Exception e2) {e2.printStackTrace(); }
		}
		return lstFacturas;
	}
	
/**************************************************************************************************************************************************************/
/***OBTIENE LAS DEVOLUCIONES DEL DIA PARA LA CONSULTA DE LA VENTA DEL DIA**************************************************************************************/
	public List obtenerDevolucionesDeldia(String[] sTiposDoc ,F55ca017[] f55ca017,List lstLocalizaciones){
		List lstFacturas = new ArrayList(), f55ca017_1 = new ArrayList(), f55ca017_2 = new ArrayList();
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = null;
		String sLoc = "", sql ="", sql2 = "";
		boolean bHayLoc = false, bSoloLocs = false;
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
			//obtener fecha actual y convertirla a formato juliano		
			int iFechaActual = obtenerFechaActualJuliana();	
			
			tx = session.beginTransaction();
			sql = "select * from gcpmcja.Vhfactura as f where f.fecha =" + iFechaActual +" and f.subtotal > 0 and f.tipofactura in (";
			for (int i = 0; i < sTiposDoc.length; i++){
				if (i == sTiposDoc.length - 1){
					sql = sql + "'" + sTiposDoc[i] + "'";
				}else{
					sql = sql + "'" + sTiposDoc[i] + "',";
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
			//
			sql = sql + ") and f.nodoco > 0 and f.tipodoco <> ''";
			//
			
			//agregar localizaciones si hay
			if(bHayLoc){
				sql2 = sql2 + " and trim(f.sdlocn) in (";
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
				sql2 = sql2 + ") and f.nodoco > 0 and f.tipodoco <> '' ";				
			}			
			
			if(bHayLoc){
				sql = sql + " union " + sql2;
			}
			if(bSoloLocs){
				lstFacturas = session.createSQLQuery(sql).addEntity(Vhfactura.class).list();
			}else{
				lstFacturas = session.createSQLQuery(sql2).addEntity(Vhfactura.class).list();	
			}		
			tx.commit();
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			try {HibernateUtilPruebaCn.closeSession(session); } catch (Exception e2) {e2.printStackTrace(); }
		}
		return lstFacturas;
	}
	
/*******************************************************************************************/
/**LEER FACTURAS DEL DIA CON DETERMINADOS TIPOS DE DOCUMENTOS Y EL NUMERO DE FACTURA PARECIDO*********************/
	public List leerFacturasDelDiaxNumero(int iFechaActual,String[] sTipoDoc, String sNumeroFac, String sCodSuc){
		List lstFacturas = new ArrayList();
		Session session = null;
		Transaction tx = null;
		
		try{
			session = HibernateUtilPruebaCn.currentSession();
			tx = session.beginTransaction();
			
			String sql = "from Vhfactura as f where f.id.fecha ="
					+ iFechaActual
					+ " and cast(f.id.nofactura as string) like '" + sNumeroFac
					+ "%' and f.id.subtotal > 0 and f.id.tipofactura in (";
			for (int i = 0; i < sTipoDoc.length; i++){
				if (i == sTipoDoc.length - 1){
					sql = sql + "'" + sTipoDoc[i] + "'";
				}else{
					sql = sql + "'" + sTipoDoc[i] + "',";
				}
			}
			sql = sql + ") and f.id.codsuc = '"+sCodSuc+ "' and f.id.nofactura not in(select rf.id.numfac from Recibofac as rf where rf.id.numfac = f.id.nofactura and rf.id.codcomp = f.id.codcomp and rf.id.tipofactura = f.id.tipofactura) order by f.id.fecha desc, f.id.hora desc ";
			
			lstFacturas = session.createQuery(sql).list();
			tx.commit();
			
		}catch(Exception ex){
			LogCajaService.CreateLog("leerFacturasDelDia", "ERR", ex.getMessage());			
		}finally {			
			try {HibernateUtilPruebaCn.closeSession(session); } catch (Exception e2) {LogCajaService.CreateLog("leerFacturasDelDia", "ERR", e2.getMessage());}
		}
	return lstFacturas;
}
	public void mostrarDetalleContado(ActionEvent e){
		dgwDetalleContado.setWindowState("normal");
		boolean mod = dgwDetalleContado.isModal();
		//dgwDetalleContado.setModal(true);
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();		
		try{			
			RowItem ri = (RowItem)e.getComponent().getParent().getParent();
			List lstA = (List) ri.getCells();		
			Cell c = (Cell) lstA.get(2);//Columna a obtener: No. Factura
			HtmlOutputText noFactura = (HtmlOutputText) c.getChildren().get(0);
			int iNoFactura = Integer.parseInt(noFactura.getValue().toString());
			List lstFacsActuales = (List) m.get("lstHfacturasContado");
			Hfactura hFac = new Hfactura();		
			for (int i = 0; i < lstFacsActuales.size(); i++){
				if (((Hfactura)lstFacsActuales.get(i)).getNofactura() == iNoFactura){
					//poner valor a labels del detalle
					hFac = (Hfactura)lstFacsActuales.get(i);
					txtNofactura = iNoFactura+"";
					txtFechaFactura = ((Hfactura)lstFacsActuales.get(i)).getFecha();
					txtCliente = ((Hfactura)lstFacsActuales.get(i)).getNomcli() + " (Nombre)";
					txtCodigoCliente = ((Hfactura)lstFacsActuales.get(i)).getCodcli() + " (Código)";
					txtUnineg = ((Hfactura)lstFacsActuales.get(i)).getUnineg();		
					
					txtTasaDetalle.setValue(((Hfactura)lstFacsActuales.get(i)).getTasa()+"");
					
					
					txtSubtotal = ((Hfactura)lstFacsActuales.get(i)).getSubtotal()+"";
					txtIva = ((Hfactura)lstFacsActuales.get(i)).getIva();
					//txtTotal = ((Hfactura)lstFacsActuales.get(i)).getTotal();
					//actualizar lista de detalle
					lstDfacturasContado = ((Hfactura)lstFacsActuales.get(i)).getDfactura();
					m.put("lstDfacturasContado",lstDfacturasContado);
					//poner monedas
					
					lstMonedasDetalle = new ArrayList();
					String moneda = ((Hfactura)lstFacsActuales.get(i)).getMoneda();
					if(moneda.equals("COR")){
						lstMonedasDetalle.add(new SelectItem("COR","COR"));
						//lblMonedaDomesticaCon.setValue("COR");
						//cmbMonedaDetalle.setStyle("visibility: hidden");
						txtTasaDetalle.setStyle("visibility: hidden");
						lblTasaDetalle.setStyle("visibility: hidden");
						
					}else{
						lstMonedasDetalle.add(new SelectItem(moneda,moneda));
						lstMonedasDetalle.add(new SelectItem("COR","COR"));
						//cmbMonedaDetalle.setStyle("visibility: visible");
						lblTasaDetalle.setValue("Tasa de Cambio: ");
						lblTasaDetalle.setStyle("visibility: visible");
						lblTasaDetalle.setStyleClass("frmLabel2");
						txtTasaDetalle.setStyle("visibility: visible");
						txtTasaDetalle.setStyleClass("frmLabel3");
					}
					m.put("Hfactura", hFac);
					List oldDfac = hFac.getDfactura();
					m.put("oldDfac",oldDfac);
					m.put("lstMonedasDetalle",lstMonedasDetalle);
					cmbMonedaDetalle.dataBind();
					gvDfacturasContado.dataBind();
					break;
				}
			}
			//dgwDetalleContado.setWindowState("normal");
		}catch(Exception ex){
			LogCajaService.CreateLog("mostrarDetalleContado", "ERR", ex.getMessage());
		}
	}
	public void cerrarDetalleContado(ActionEvent e){
		Divisas divisas = new Divisas();
		
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		String monedaActual = cmbMonedaDetalle.getValue().toString();	
		
		Hfactura hFac = (Hfactura)m.get("Hfactura");
		List oldDet = hFac.getDfactura(),newDet = new ArrayList();
		int cant = oldDet.size();
		Dfactura dFac = null;
		try{
		if(monedaActual.equals("COR") && !hFac.getMoneda().equals("COR")){
			for (int i = 0; i < cant; i++){
				dFac = (Dfactura)oldDet.get(i); 
				dFac.setPreciounit(dFac.getPreciounit()/dFac.getTasa().doubleValue());
				newDet.add(dFac);
			}
			m.put("lstDfacturasContado",newDet);
			hFac.setDfactura(newDet);
		}	
		m.put("Hfactura", hFac);
		gvDfacturasContado.dataBind();
		dgwDetalleContado.setWindowState("hidden");
		}catch(Exception ex){
			LogCajaService.CreateLog("cerrarDetalleContado", "ERR", ex.getMessage());			
		}
	}
	/****************DETALLE DE FACTURA DE CONTADO en el popup del recibo*****************/
	public void mostrarDetalleEnReciboContado(ActionEvent e){
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();		
		try{			
			RowItem ri = (RowItem)e.getComponent().getParent().getParent();
			List lstA = (List) ri.getCells();		
			Cell c = (Cell) lstA.get(2);//Columna a obtener: No. Factura
			HtmlOutputText noFactura = (HtmlOutputText) c.getChildren().get(0);
			int iNoFactura = Integer.parseInt(noFactura.getValue().toString());
			List lstFacsActuales = (List) m.get("lstHfacturasContado");
			Hfactura hFac = new Hfactura();		
			
			for (int i = 0; i < lstFacsActuales.size(); i++){
				if (((Hfactura)lstFacsActuales.get(i)).getNofactura() == iNoFactura){
					//poner valor a labels del detalle
					hFac = (Hfactura)lstFacsActuales.get(i);
					txtNofactura = iNoFactura+"";
					txtFechaFactura = ((Hfactura)lstFacsActuales.get(i)).getFecha();
					txtCliente = ((Hfactura)lstFacsActuales.get(i)).getNomcli() + " (Nombre)";
					txtCodigoCliente = ((Hfactura)lstFacsActuales.get(i)).getCodcli() + " (Código)";
					txtUnineg = ((Hfactura)lstFacsActuales.get(i)).getUnineg();		
					txtTasaDetalle.setValue(((Hfactura)lstFacsActuales.get(i)).getTasa()+"");
					
					txtSubtotal = ((Hfactura)lstFacsActuales.get(i)).getSubtotal()+"";
					txtIva = ((Hfactura)lstFacsActuales.get(i)).getIva();
					//txtTotal = ((Hfactura)lstFacsActuales.get(i)).getTotal();
					//actualizar lista de detalle
					lstDfacturasContado = ((Hfactura)lstFacsActuales.get(i)).getDfactura();
					
					//poner monedas
					lstMonedasDetalleReciboContado = new ArrayList();
					String moneda = ((Hfactura)lstFacsActuales.get(i)).getMoneda();
					if(moneda.equals("COR")){
						lstMonedasDetalleReciboContado.add(new SelectItem("COR","COR"));
						txtTasaDetalle.setStyle("visibility: hidden");
						
					}else{
						txtTasaDetalle.setStyle("visibility: visible");
						txtTasaDetalle.setStyleClass("frmLabel3");
						lstMonedasDetalleReciboContado.add(new SelectItem(moneda,moneda));
						lstMonedasDetalleReciboContado.add(new SelectItem("COR","COR"));
					}
					m.put("Hfactura", hFac);
					m.put("oldDfac",hFac.getDfactura());
					m.put("lstMonedasDetalleReciboContado", lstMonedasDetalleReciboContado);
					m.put("lstDfacturasContado",lstDfacturasContado);
					cmbMonedaDetalleReciboContado.dataBind();
					gvDfacturasContadoRecibo.dataBind();
					break;
				}
			}
			dgwDetalleEnReciboContado.setWindowState("normal");
			//dgwDetalleEnReciboContado.setModal(true);
			dgwDetalleEnReciboContado.setStyle("height: 440px; visibility: visible; width: 520px");		
		}catch(Exception ex){
			LogCajaService.CreateLog("mostrarDetalleContado", "ERR", ex.getMessage());	
			
		}
	}
	public void cerrarDetalleEnReciboContado(ActionEvent e){
		
		Divisas divisas = new Divisas();
		
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		String monedaActual = cmbMonedaDetalleReciboContado.getValue().toString();	
		
		Hfactura hFac = (Hfactura)m.get("Hfactura");
		List oldDet = hFac.getDfactura(),newDet = new ArrayList();
		int cant = oldDet.size();
		Dfactura dFac = null;
		try{
		if(monedaActual.equals("COR") && !hFac.getMoneda().equals("COR")){
			for (int i = 0; i < cant; i++){
				dFac = (Dfactura)oldDet.get(i); 
				dFac.setPreciounit(dFac.getPreciounit()/dFac.getTasa().doubleValue());
				newDet.add(dFac);
			}
			m.put("lstDfacturasContado",newDet);
			hFac.setDfactura(newDet);
		}	
		m.put("Hfactura", hFac);
		gvDfacturasContadoRecibo.dataBind();
		dgwDetalleEnReciboContado.setWindowState("hidden");
		}catch(Exception ex){
			System.out.print("=>Excepcon capturada en cerrarDetalleContado" + ex);
		}
	}
	
/*************************************************************************************/	
	/******************QUITAR FACTURA DE DETALLE DE RECIBO DE CONTADO**************************/
	public void quitarFacturaContado(ActionEvent e){
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		Divisas divisas = new Divisas();
		List lstDcontadoFacs = (List)m.get("facturasSelected");
		List lstNewDFacs = new ArrayList();
		try {
			double montoAplicar = 0;//Double.parseDouble(m.get("mTotalFactura").toString());
			
			if (lstDcontadoFacs.size()>1){
				RowItem ri = (RowItem)e.getComponent().getParent().getParent();
				List lstA = (List) ri.getCells();
				//Columna a obtener: No. Factura
				Cell c = (Cell) lstA.get(2);
				HtmlOutputText noFactura = (HtmlOutputText) c.getChildren().get(0);
				int iNoFactura = Integer.parseInt(noFactura.getValue().toString());
				//Columna a obtener:Monto
				Cell c2 = (Cell) lstA.get(4);
				HtmlOutputText monto = (HtmlOutputText) c2.getChildren().get(0);
				String sMonto = monto.getValue().toString();
				Hfactura hFac = null;
				for(int i = 0;i < lstDcontadoFacs.size();i++){
					hFac = (Hfactura)lstDcontadoFacs.get(i);
					//if (hFac.getNofactura() == iNoFactura && (hFac.getTotal().trim()).equals(sMonto.trim())){
						//no las incluye en la lista
					//}else{
						lstNewDFacs.add(hFac);
						//montoAplicar = montoAplicar + divisas.formatStringToDouble(hFac.getTotal());
					//}
				}
				lblMontoAplicar.setValue(divisas.formatDouble(montoAplicar));
				m.put("mTotalFactura",divisas.formatDouble(montoAplicar));
				m.put("facturasSelected", lstNewDFacs);
				gvfacturasSelec.dataBind();
			}else{
				dgwMensajeDetalleContado.setWindowState("normal");
			}		
		}catch(Exception ex){
			System.out.print("Se captura una excepcion en quitarFacturaContado: " + ex);
		}
	}
	public void cerrarMensajeDetallefactura(ActionEvent e){
		dgwMensajeDetalleContado.setWindowState("hidden");
	}
/************************************************************************************/
	/****************LISTAR FACTURAS DE CONTADO DEL DIA*****************/	
	public void listarFacturasContadoDelDia(){
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Calendar calFechaActual = null;
		List lstFacs = new ArrayList();
		F55ca017[] f55ca017 =  null,f55ca017i = null;
		CtrlCajas cajasCtrl = new CtrlCajas();
		int iCaId = 0;
		String sCodComp = "";
		String[] sLineas = null, sTiposDoc;
		boolean passed = true;
		
		try{
//			System.out.println("Listar facturas de contados del dia 1133 factura crtlk");
			List lstCajas = (List)m.get("lstCajas");
			Vf55ca01 f55ca01 = ((Vf55ca01)lstCajas.get(0));
			String sCodSuc = f55ca01.getId().getCaco().substring(3,5);
			
			//obtener fecha actual y convertirla a formato juliano
			calFechaActual = Calendar.getInstance();
			String sFechaActual = calFechaActual.get(Calendar.DATE)+ "/" + (calFechaActual.get(Calendar.MONTH)+ 1) + "/" + calFechaActual.get(Calendar.YEAR);
			calFechaActual.setTime(new Date(sdf.parse(sFechaActual).getTime()));
			CalendarToJulian julian = new CalendarToJulian(calFechaActual);
			int iFechaActual = julian.getDate();
			JulianToCalendar cal = new JulianToCalendar(iFechaActual);
			Calendar dtPrueba = cal.getDate();
			
			
			m.put("iFechaActual", iFechaActual);
			
			if(m.get("sTiposDoc")==null){//buscar Tipos de documentos
				//leer unidades de negocio activas para caja
				f55ca017 = cajasCtrl.obtenerUniNegCaja(f55ca01.getId().getCaid(), f55ca01.getId().getCaco());
				//leer unidades de negocio inactivas para caja
				f55ca017i = cajasCtrl.obtenerUniNegCajaInactiva(f55ca01.getId().getCaid(), f55ca01.getId().getCaco());
				//obtener lineas correspondientes menos las inactivas
				if (f55ca017 != null && f55ca017i != null){
					sLineas = cajasCtrl.obtenerLineas(sCodSuc,f55ca017, f55ca017i);
				}else if(f55ca017 != null && f55ca017i == null){//no hay inactivas
					sLineas = cajasCtrl.obtenerLineas(sCodSuc,f55ca017);
				}else{// no hay unidades de negocio o sucursales configuradas en esta caja
					passed = false;
				}
				if (passed){
					//obtener tipos de documentos de todas las lineas encontradas
					sTiposDoc = cajasCtrl.obtenerTiposDeDocumento(sLineas);
					if (sTiposDoc != null){
						
						m.put("sTiposDoc", sTiposDoc);
						
						//obtener localizaciones
						List lstLocalizaciones = new ArrayList();
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
						m.put("lstLocalizaciones",lstLocalizaciones);
						m.put("f55ca017", f55ca017);
						
						lstFacs = cajasCtrl.leerFacturaDelDia(iFechaActual, sTiposDoc , f55ca017, f55ca01.getId().getCaid());
//						cajasCtrl.leerFacturaDelDia1(iFechaActual, sTiposDoc , f55ca017,lstLocalizaciones,f55ca01.getId().getCaid());
						
						if (lstFacs != null && !lstFacs.isEmpty()){
							FacContadoDAO facDao = new FacContadoDAO();
							lstHfacturasContado = facDao.formatFactura(lstFacs);
							m.put("lstHfacturasContado",lstHfacturasContado);
							m.put("sMensaje", "");
							if(m.get("mostrar")!= null){
								m.remove("mostrar");
							}
						}
						else{
							sMensaje = "No se han realizado facturas para el dia";
							m.put("sMensaje", sMensaje);
							getSMensaje();
							m.put("mostrar", "m");
						}
					}
					else{
						sMensaje = "No hay tipos de documentos configurados para las lineas de negocio de esta sucursal";
						m.put("sMensaje", sMensaje);
						getSMensaje();
						m.put("mostrar", "m");
					}
				}else{
					sMensaje = "No hay unidades de negocio o sucursales configuradas en esta caja";
					m.put("sMensaje", sMensaje);
					getSMensaje();
					m.put("mostrar", "m");
					
					//buscar facturas por traslados
					lstFacs = cajasCtrl.leerTraslados(f55ca01.getId().getCaid(), iFechaActual);
					if (lstFacs != null && !lstFacs.isEmpty()){
						lstHfacturasContado = FacContadoDAO.formatFactura(lstFacs);
						m.put("lstHfacturasContado",lstHfacturasContado);
					}else{
						sMensaje = "No se Encontraron facturas pendientes de pago!!!";
						m.put("sMensaje", sMensaje);
						m.put("mostrar", "m");
					}
				}
			}else{
				sTiposDoc = (String[])m.get("sTiposDoc");
				List lstLocalizaciones = (List) m.get("lstLocalizaciones");
				f55ca017 = (F55ca017[])m.get("f55ca017");
				
				lstFacs = cajasCtrl.leerFacturaDelDia(iFechaActual, sTiposDoc , f55ca017, f55ca01.getId().getCaid());
//				cajasCtrl.leerFacturaDelDia1(iFechaActual, sTiposDoc , f55ca017,lstLocalizaciones,f55ca01.getId().getCaid());
				
				if (lstFacs != null && !lstFacs.isEmpty()){
					FacContadoDAO facDao = new FacContadoDAO();
					lstHfacturasContado = facDao.formatFactura(lstFacs);
					m.put("lstHfacturasContado",lstHfacturasContado);
					m.put("sMensaje", "");
					if(m.get("mostrar")!= null){
						m.remove("mostrar");
					}
				}
				else{
					sMensaje = "No se han realizado facturas para el dia";
					m.put("sMensaje", sMensaje);
					getSMensaje();
					m.put("mostrar", "m");
				}
			}
		}catch(Exception ex){
			System.out.print("==> Excepción capturada en FacturaCtrl.listarFacturasContadoDelDia: " + ex);
		}
	}
	
	
	public void listarFacturasContadoDelDiaTmp(){
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		List lstFacs = null;
		boolean unidadesNegocio = false;
		
		try{
			LogCajaService.CreateLog("listarFacturasContadoDelDiaTmp", "INFO", "INICIO METODO => listarFacturasContadoDelDiaTmp");
			
			int caid = ((ArrayList<Vf55ca01>) m.get("lstCajas"))
						.get(0).getId().getCaid();
			
			int iFechaActual = Integer.parseInt( String.valueOf(
							m.get("iFechaActual").toString() ) );
			
			//int iFechaActual= 123161;
			
			unidadesNegocio = m.containsKey("iFechaActual") &&  m.containsKey("f55ca017");
			
			if(unidadesNegocio){
				String[] sTiposDoc =  (String[])m.get("sTiposDoc");
				F55ca017[] f55ca017 = (F55ca017[])m.get("f55ca017");
				lstFacs = CtrlCajas.leerFacturaDelDia(iFechaActual, sTiposDoc , f55ca017, caid);
			} else {
				lstFacs = CtrlCajas.leerTraslados(caid, iFechaActual);
			}

			if (lstFacs == null || lstFacs.isEmpty()){
				sMensaje = "No se han realizado facturas para el dia";
				m.put("mostrar", "m");
				m.put("sMensaje", sMensaje);
				lstHfacturasContado = new ArrayList();
				return;
			}
			
			lstHfacturasContado = FacContadoDAO.formatFactura(lstFacs);
			
			LogCajaService.CreateLog("listarFacturasContadoDelDiaTmp", "INFO", "FIN METODO => listarFacturasContadoDelDiaTmp");
		}catch(Exception ex){
			LogCajaService.CreateLog("listarFacturasContadoDelDia", "ERR", ex.getMessage());
			System.out.print("==> FacturaCtrl.listarFacturasContadoDelDia: ");
			ex.printStackTrace(); 
		}finally{
			if(lstHfacturasContado == null)
				lstHfacturasContado = new ArrayList();
			m.put("lstHfacturasContado",lstHfacturasContado);
			lstFacs = null;
		}
	}
	
	/****************BUSCAR FACTURAS DE CONTADO DEL DIA POR PARAMETROS*****************/
	public void BuscarFacturasContado(ValueChangeEvent e){
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		String strParametro = null;
		String sql = "from Hfactjdecon as hf ";
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = session.beginTransaction();
		SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
		int iFechaActual = 0;
		try {
			String sMensaje2 = "";
			iFechaActual = Integer.parseInt(m.get("iFechaActual").toString());
			if(txtParametro.getValue() != null && !txtParametro.getValue().toString().trim().equals("")) {
				strParametro = txtParametro.getValue().toString();
				int busqueda = 1;
				if (m.get("strBusqueda") != null){
					busqueda = Integer.parseInt((String)m.get("strBusqueda"));
				}
				List result = new ArrayList();
				switch (busqueda){
					case(1):
						//Busqueda por nombre de cliente
						//sql = sql + "where trim(hf.id.nomcli) = '"+strParametro.trim().toUpperCase()+"' and hf.id.subtotal > 0 and hf.id.fecha = " + iFechaActual;
						sql = sql + "where trim(hf.id.nomcli) = '"+strParametro.trim().toUpperCase()+"' and hf.id.subtotal > 0 and hf.id.fecha = " + 109059;
						result = session
							.createQuery(sql)			
							.list();
						tx.commit();
						if (result == null || result.isEmpty()){
							sMensaje2 = "No se encontraron resultados";
							getSMensaje();
							m.put("mostrar", "m");
						}else{
							if(m.get("mostrar") != null){
								m.remove("mostrar");
							}
							lstHfacturasContado = formatFactura(result);
							m.put("lstHfacturasContado",lstHfacturasContado);
							gvHfacturasContado.dataBind();
						}
						break;
					case(2):
						//Busqueda por codigo de cliente
						sql = sql + "where cast(hf.id.codcli as string) = '"+strParametro.trim()+"' and hf.id.subtotal > 0 and hf.id.fecha = " + iFechaActual;;	
						result = session
							.createQuery(sql)			
							.list();
						tx.commit();
						if (result == null || result.isEmpty()){
							sMensaje2 = "No se encontraron resultados";
							getSMensaje();
							m.put("mostrar", "m");
						}else{
							if(m.get("mostrar") != null){
								m.remove("mostrar");
							}
							lstHfacturasContado = formatFactura(result);
							m.put("lstHfacturasContado",lstHfacturasContado);
							gvHfacturasContado.dataBind();
						}
						break;
					case(3):
						//Busqueda por numero de factura
						sql = sql + "where cast(hf.id.nofactura as string) = '"+strParametro.trim()+"' and hf.id.subtotal > 0 and hf.id.fecha = " + iFechaActual;	
						result = session
							.createQuery(sql)			
							.list();
						tx.commit();
						if (result == null || result.isEmpty()){
							sMensaje2 = "No se encontraron resultados";
							getSMensaje();
							m.put("mostrar", "m");
						}else{
							if(m.get("mostrar") != null){
								m.remove("mostrar");
							}
							lstHfacturasContado = formatFactura(result);
							m.put("lstHfacturasContado",lstHfacturasContado);
							gvHfacturasContado.dataBind();
						}
						break;
				}
			}
			txtMensaje.setValue(sMensaje2);
			m.put("sMensaje", sMensaje2);
			//srm.addSmartRefreshId(txtMensaje.getClientId(FacesContext.getCurrentInstance()));	
		}catch(Exception ex){
			System.out.print("==> Excepción capturada en BuscarFacturasContado: " + ex);
		}finally{
			try {HibernateUtilPruebaCn.closeSession(session); } catch (Exception e2) {e2.printStackTrace(); }
		}
	}
/****************SET BUSQUEDA*****************/	
	public void setBusqueda(ValueChangeEvent e){
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		String strBusqueda = cmbBusqueda.getValue().toString();
		m.put("strBusqueda", strBusqueda);
	}
/************FORMATEAR CAMPOS PARA VISUALIZACION:: recibe una lista de Hfactjdecon y retorna una lista de Hfactura************************/
	public ArrayList formatFactura(List lstFacturas){
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		Divisas d = new Divisas();
		ArrayList lstResultado = new ArrayList();
		Vhfactura[] hFacJde = new Vhfactura[lstFacturas.size()];
		Hfactura hFac = null;
		String sql = "";		
		List result = new ArrayList();
		Session session = null;
		double total = 0;
		double ivaTotal = 0;
		double ivaActual = 0;
		double totalfac = 0;
		double equiv = 0;
		String sHora = "", sMonedaBase="";
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		try{
			
			if(! m.containsKey("cont_f55ca014") || lstFacturas == null || lstFacturas.isEmpty() ) 
				return new ArrayList<Hfactura>();
			
			session = HibernateUtilPruebaCn.currentSession();
			
			sMonedaBase = cCtrl.sacarMonedaBase((F55ca014[])m.get("cont_f55ca014"),((Vhfactura)lstFacturas.get(0)).getId().getCodcomp());
			
			for (int i = 0; i < lstFacturas.size(); i++){
				hFacJde[i] = (Vhfactura)lstFacturas.get(i);
				JulianToCalendar fecha = new JulianToCalendar(hFacJde[i].getId().getFecha());
				JulianToCalendar fechagrab = new JulianToCalendar(hFacJde[i].getId().getFechagrab());
				double subtotal = (hFacJde[i].getId().getSubtotal().doubleValue())/100.0;
				double totalf = d.roundDouble((hFacJde[i].getId().getTotalf())/100.0); 
				String sSubtotal = d.formatDouble(subtotal);
				String sTotal = null;
				String sIvaTotal = null;

				List lstDfactura = new ArrayList();	
				total = hFacJde[i].getId().getTotal()/100.0;
				ivaTotal = 0;
				double tasa = 0;
				//
				sIvaTotal = d.formatDouble(ivaTotal);
				String moneda = hFacJde[i].getId().getMoneda();
				if (moneda.equals(sMonedaBase)){
					equiv = total;
				}
				else{
					equiv = hFacJde[i].getId().getTotal()/100.0;
					total = hFacJde[i].getId().getTotalf()/100.0;
				}
				totalfac += total;
				m.put("mTotalFactura",totalfac);
				
				sHora = d.agregarSeparadorCadena(hFacJde[i].getId().getHora().toString(), ":", 2);				
				
				// establecer tipo de factura
				String sPago = "";
				if(hFacJde[i].getId().getTipopago().trim().equals("00") || hFacJde[i].getId().getTipopago().trim().equals("01") || hFacJde[i].getId().getTipopago().trim().equals("001")){
					sPago = "Contado";
				}else{
					sPago = "Crédito";
				}
				hFac = new Hfactura(
						hFacJde[i].getId().getNofactura(),
						hFacJde[i].getId().getTipofactura(),
						hFacJde[i].getId().getCodcli(),
						hFacJde[i].getId().getNomcli(),
						hFacJde[i].getId().getCodunineg(),
						hFacJde[i].getId().getUnineg(),
						hFacJde[i].getId().getCodsuc(),
						hFacJde[i].getId().getNomsuc(),
						hFacJde[i].getId().getCodcomp(),
						hFacJde[i].getId().getNomcomp(),
						fecha.toString() +" "+ sHora,
						subtotal,
						hFacJde[i].getId().getMoneda(),
						hFacJde[i].getId().getTasa(),
						hFacJde[i].getId().getTipopago(),
						0,
						0,
						fechagrab.toString(),
						hFacJde[i].getId().getHechopor(),
						hFacJde[i].getId().getPantalla(),
						ivaTotal,
						total,
						d.formatDouble(equiv),
						"",
						lstDfactura,
						hFacJde[i].getId().getNodoco(),
						hFacJde[i].getId().getTipodoco(),
						hFacJde[i].getId().getEstado(),
						sPago,
						hFacJde[i].getId().getCodvendor(),
						totalf,
						hFacJde[i].getId().getFecha(),
						hFacJde[i].getId().getSdlocn(),
						(hFacJde[i].getId().getNodoco() != 0)?"Factura":"Devolución",
							
						(hFacJde[i].getId().getEstado().trim().compareTo("")
								== 0)? "Aplicada" : "Anulada",
										
						new BigDecimal(
							String.valueOf(hFacJde[i].getId().getTotal()))
								.divide( new BigDecimal("100"),2, 
									RoundingMode.HALF_UP)			
										
										
						);
					if(hFacJde[i].getId().getMoneda().equals(sMonedaBase)){//domestica
						hFac.setTotal(total);
						hFac.setCpendiente(total);
						hFac.setSubtotal(hFacJde[i].getId().getSubtotal()/100.00);
					}else{
						hFac.setTotal(totalf);
						hFac.setCpendiente(total);
						hFac.setDpendiente(totalf);
						hFac.setSubtotal(hFacJde[i].getId().getSubtotal()/100.00);
						hFac.setSubtotalf(hFacJde[i].getId().getSubtotalf()/100.00);
					}
				lstResultado.add(hFac);
			}
		}catch(Exception ex){
			System.out.print("==> Excepción capturada en FacturaCrtl.formatFactura: " + ex);
		}finally{
			try {HibernateUtilPruebaCn.closeSession(session); } catch (Exception e2) {e2.printStackTrace(); }
		}
		return lstResultado;
	}
/***********************REFRESCAR FACTURAS DE CONTADO DEL DIA********************************/
	public void refrescarFacturasContado(ActionEvent e){
		listarFacturasContadoDelDia();
		gvHfacturasContado.dataBind();
	}
	/************************CAMBIAR MONEDA DEL DETALLE***************************************/
	public void cambiarMonedaDetalle(ValueChangeEvent e){
		Divisas divisas = new Divisas();
		
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		Hfactura hFac = (Hfactura)m.get("Hfactura");
		List lstDetalle = hFac.getDfactura();
		Dfactura dFac = null, newDfac = null;
		List lstNewDetalle = new ArrayList();
		String monedaActual = cmbMonedaDetalle.getValue().toString();
		try{
			if(monedaActual.equals("COR")){
				txtSubtotal = divisas.formatDouble(hFac.getSubtotal() * hFac.getTasa().doubleValue()) + "";
				txtIva =     hFac.getIva() * hFac.getTasa().doubleValue();

				for (int i = 0; i < lstDetalle.size(); i++){
					dFac = (Dfactura)lstDetalle.get(i); 
					dFac.setPreciounit(dFac.getPreciounit() * dFac.getTasa().doubleValue());
					lstNewDetalle.add(dFac);
				}
				
			}else {
				txtSubtotal = divisas.formatDouble(hFac.getSubtotal());
				txtIva = hFac.getIva();
				//txtTotal = 	  divisas.formatDouble(divisas.formatStringToDouble(hFac.getTotal()));
				/*lblTasaDetalle.setValue("");
				txtTasaDetalle.setValue("");
				lblTasaDetalle.setStyle("visibility: visible");
				lblTasaDetalle.setStyleClass("frmLabel2");
				txtTasaDetalle.setStyle("visibility: visible");
				txtTasaDetalle.setStyleClass("frmLabel3");*/
				for (int i = 0; i < lstDetalle.size(); i++){
					dFac = (Dfactura)lstDetalle.get(i); 
					dFac.setPreciounit(dFac.getPreciounit()/dFac.getTasa().doubleValue());
					lstNewDetalle.add(dFac);
				}
			}
			int h = lstDetalle.size();
			m.put("lstDfacturasContado",lstNewDetalle);
			
			gvDfacturasContado.dataBind();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
/************************CAMBIAR MONEDA DEL DETALLE en recibo de contado***************************************/
	public void cambiarMonedaDetalleReciboContado(ValueChangeEvent e){
		Divisas divisas = new Divisas();
		
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		Hfactura hFac = (Hfactura)m.get("Hfactura");
		List lstDetalle = hFac.getDfactura();
		Dfactura dFac = null, newDfac = null;
		List lstNewDetalle = new ArrayList();
		String monedaActual = cmbMonedaDetalleReciboContado.getValue().toString();
		try{
			if(monedaActual.equals("COR")){
				txtSubtotal = divisas.formatDouble(hFac.getSubtotal() * hFac.getTasa().doubleValue()) + "";
				txtIva =      hFac.getIva() * hFac.getTasa().doubleValue();
				//txtTotal = 	  divisas.formatDouble(divisas.formatStringToDouble(hFac.getTotal()) * hFac.getTasa()) + "";
				
				for (int i = 0; i < lstDetalle.size(); i++){
					dFac = (Dfactura)lstDetalle.get(i); 
					dFac.setPreciounit(dFac.getPreciounit() * dFac.getTasa().doubleValue());
					lstNewDetalle.add(dFac);
				}
				
			}else {
				txtSubtotal = divisas.formatDouble(hFac.getSubtotal());
				txtIva = hFac.getIva();
				//txtTotal = 	  divisas.formatDouble(divisas.formatStringToDouble(hFac.getTotal()));
				
				for (int i = 0; i < lstDetalle.size(); i++){
					dFac = (Dfactura)lstDetalle.get(i); 
					dFac.setPreciounit(dFac.getPreciounit()/dFac.getTasa().doubleValue());
					lstNewDetalle.add(dFac);
				}
			}
			m.put("lstDfacturasContado",lstNewDetalle);
			gvDfacturasContadoRecibo.dataBind();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/******METODOS DE CREDITO***********/
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/****************DETALLE DE FACTURA DE CREDITO*****************/	
	public void mostrarDetalleCredito(ActionEvent e){	
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();		
		try{			
			RowItem ri = (RowItem)e.getComponent().getParent().getParent();
			List lstA = (List) ri.getCells();		
			Cell c = (Cell) lstA.get(2);//Columna a obtener: No. Factura
			HtmlOutputText noFactura = (HtmlOutputText) c.getChildren().get(0);
			int iNoFactura = Integer.parseInt(noFactura.getValue().toString());
			List lstFacsActuales = (List) m.get("lstHfacturasCredito");
			Hfactura hFac = new Hfactura();		
			for (int i = 0; i < lstFacsActuales.size(); i++){
				hFac = (Hfactura)lstFacsActuales.get(i);
				if (hFac.getNofactura() == iNoFactura){
					//poner valor a labels del detalle
					txtNofacturaCred = iNoFactura+"";
					txtFechaFacturaCred = hFac.getFecha();
					txtClienteCred = hFac.getNomcli() + " (Nombre)";
					txtCodigoClienteCred = hFac.getCodcli() + " (Código)";
					txtUninegCred = hFac.getUnineg();		
					txtTasaDetalle.setValue(hFac.getTasa()+"");
					txtSubtotalCred = hFac.getSubtotal()+"";
					txtIvaCred = hFac.getIva()+"";
					txtTotalCred = hFac.getTotal()+"";
					if(hFac.getMoneda().equals("USD")){
						txtPendiente =  hFac.getDpendiente()+"";
					}else{
						txtPendiente =  hFac.getCpendiente()+"";
					}
					
					//poner monedas
					lstMonedasDetalle = new ArrayList();
					String moneda = hFac.getMoneda();
					if(moneda.equals("COR")){
						lstMonedasDetalle.add(new SelectItem("COR","COR"));
						txtTasaDetalle.setStyle("visibility: hidden");
						lblTasaDetalle.setStyle("visibility: hidden");
					}else{
						txtTasaDetalle.setStyle("visibility: visible");
						txtTasaDetalle.setStyleClass("frmLabel3");
						lstMonedasDetalle.add(new SelectItem(moneda,moneda));
						lstMonedasDetalle.add(new SelectItem("COR","COR"));
					}
					m.put("HfacturaCredito", hFac);
					m.put("oldDfac",hFac.getDfactura());
					m.put("lstMonedasDetalle",lstMonedasDetalle);
					//actualizar lista de detalle
					lstDfacturasCredito = hFac.getDfactura();
					m.put("lstDfacturasCredito",lstDfacturasCredito);
					cmbMonedaDetalleCredito.dataBind();
					gvDfacturasCredito.dataBind();
					break;
				}
			}
			dgwDetalleCredito.setWindowState("normal");
			//dgwDetalleCredito.setModal(true);
			dgwDetalleCredito.setStyle("height: 460px; visibility: visible; width: 500px");
		}catch(Exception ex){
			System.out.print("==> Excepción capturada en mostrarDetalleCredito: " + ex);
		}
	}
	public void cerrarDetalleCredito(ActionEvent e){
		
		Divisas divisas = new Divisas();
		
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		String monedaActual = cmbMonedaDetalleCredito.getValue().toString();	
		
		Hfactura hFac = (Hfactura)m.get("HfacturaCredito");
		List oldDet = null,newDet = new ArrayList();
		int cant = 0;
		Dfactura dFac = null;
		try{
			oldDet = hFac.getDfactura();
			cant = oldDet.size();
			if(monedaActual.equals("COR") && !hFac.getMoneda().equals("COR")){
				for (int i = 0; i < cant; i++){
					dFac = (Dfactura)oldDet.get(i); 
					dFac.setPreciounit(dFac.getPreciounit()/dFac.getTasa().doubleValue());
					newDet.add(dFac);
				}
				m.put("lstDfacturasCredito",newDet);
				hFac.setDfactura(newDet);
			}	
			m.put("HfacturaCredito", hFac);
			gvDfacturasCredito.dataBind();
			dgwDetalleCredito.setWindowState("hidden");
		}catch(Exception ex){
			System.out.print("=>Excepcon capturada en cerrarDetalleContado" + ex);
		}
	}

/****************DETALLE DE FACTURA DE CREDITO en el popup del recibo*****************/
	public void mostrarDetalleEnReciboCredito(ActionEvent e){
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();		
		try{			
			RowItem ri = (RowItem)e.getComponent().getParent().getParent();
			List lstA = (List) ri.getCells();		
			Cell c = (Cell) lstA.get(2);//Columna a obtener: No. Factura
			HtmlOutputText noFactura = (HtmlOutputText) c.getChildren().get(0);
			int iNoFactura = Integer.parseInt(noFactura.getValue().toString());
			List lstFacsActuales = (List) m.get("lstHfacturasCredito");
			Hfactura hFac = new Hfactura();		
			
			for (int i = 0; i < lstFacsActuales.size(); i++){
				if (((Hfactura)lstFacsActuales.get(i)).getNofactura() == iNoFactura){
					//poner valor a labels del detalle
					hFac = (Hfactura)lstFacsActuales.get(i);
					txtNofactura = iNoFactura+"";
					txtFechaFactura = ((Hfactura)lstFacsActuales.get(i)).getFecha();
					txtCliente = ((Hfactura)lstFacsActuales.get(i)).getNomcli() + " (Nombre)";
					txtCodigoCliente = ((Hfactura)lstFacsActuales.get(i)).getCodcli() + " (Código)";
					txtUnineg = ((Hfactura)lstFacsActuales.get(i)).getUnineg();		
					txtTasaDetalle.setValue(((Hfactura)lstFacsActuales.get(i)).getTasa()+"");
					
					txtSubtotal = ((Hfactura)lstFacsActuales.get(i)).getSubtotal()+"";
					txtIva = ((Hfactura)lstFacsActuales.get(i)).getIva();
					//txtTotal = ((Hfactura)lstFacsActuales.get(i)).getTotal();
					//actualizar lista de detalle
					lstDfacturasCredito = ((Hfactura)lstFacsActuales.get(i)).getDfactura();
					
					//poner monedas
					lstMonedasDetalleReciboCredito = new ArrayList();
					String moneda = ((Hfactura)lstFacsActuales.get(i)).getMoneda();
					if(moneda.equals("COR")){
						lstMonedasDetalleReciboCredito.add(new SelectItem("COR","COR"));
						txtTasaDetalle.setStyle("visibility: hidden");
						
					}else{
						txtTasaDetalle.setStyle("visibility: visible");
						txtTasaDetalle.setStyleClass("frmLabel3");
						lstMonedasDetalleReciboCredito.add(new SelectItem(moneda,moneda));
						lstMonedasDetalleReciboCredito.add(new SelectItem("COR","COR"));
					}
					m.put("HfacturaCredito", hFac);
					m.put("oldDfac",hFac.getDfactura());
					m.put("lstMonedasDetalleReciboCredito", lstMonedasDetalleReciboCredito);
					m.put("lstDfacturasCredito",lstDfacturasCredito);
					cmbMonedaDetalleReciboCredito.dataBind();
					gvDfacturasCreditoRecibo.dataBind();
					break;
				}
			}
			dgwDetalleEnReciboCredito.setWindowState("normal");
			dgwDetalleEnReciboCredito.setModal(true);
			dgwDetalleEnReciboCredito.setStyle("height: 440px; visibility: visible; width: 520px");		
		}catch(Exception ex){
			System.out.print("==> Excepción capturada en mostrarDetalleCredito: " + ex);
		}
	}
	public void cerrarDetalleEnReciboCredito(ActionEvent e){
		
		Divisas divisas = new Divisas();
		
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		String monedaActual = cmbMonedaDetalleReciboCredito.getValue().toString();	
		
		Hfactura hFac = (Hfactura)m.get("HfacturaCredito");
		List oldDet = hFac.getDfactura(),newDet = new ArrayList();
		int cant = oldDet.size();
		Dfactura dFac = null;
		try{
		if(monedaActual.equals("COR") && !hFac.getMoneda().equals("COR")){
			for (int i = 0; i < cant; i++){
				dFac = (Dfactura)oldDet.get(i); 
				dFac.setPreciounit(dFac.getPreciounit()/dFac.getTasa().doubleValue());
				newDet.add(dFac);
			}
			m.put("lstDfacturasCredito",newDet);
			hFac.setDfactura(newDet);
		}	
		m.put("HfacturaCredito", hFac);
		gvDfacturasCreditoRecibo.dataBind();
		dgwDetalleEnReciboCredito.setWindowState("hidden");
		}catch(Exception ex){
			System.out.print("=>Excepcon capturada en cerrarDetalleContado" + ex);
		}
	}
	
/*************************************************************************************/	
/****************LISTAR FACTURAS DE CREDITO PENDIENTES DE PAGO*****************/	
	public void listarFacturasCreditoPendientes(){
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		String sql = "from Hfacturajde as f where f.id.cpendiente > 0 order by f.id.tipofactura, f.id.nofactura ";
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = session.beginTransaction();
		List result = new ArrayList();
		try{	
			result = session
			.createQuery(sql)			
			.list();
			tx.commit();
			//lstHfacturasCredito = formatFacturaCredito(result);
			m.put("lstHfacturasCredito",lstHfacturasCredito);
		}catch(Exception ex){
			System.out.print("==> Excepción capturada en listarFacturasCreditoPendientes: " + ex);
		}finally{
			try {HibernateUtilPruebaCn.closeSession(session); } catch (Exception e2) {e2.printStackTrace(); }
		}
	}
/****************BUSCAR FACTURAS DE CREDITO DEL DIA POR PARAMETROS*****************/
	public void BuscarFacturasCredito(ActionEvent e){
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		String strParametro = null;
		String sql = "from Hfacturajde as hf ";
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = session.beginTransaction();
		try {
			if(txtParametroCredito.getValue() != null && !txtParametroCredito.getValue().toString().trim().equals("")) {
				strParametro = txtParametroCredito.getValue().toString();
				int busqueda = 1;
				if (m.get("strBusquedaCredito") != null){
					busqueda = Integer.parseInt((String)m.get("strBusquedaCredito"));
				}
				List result = new ArrayList();
				switch (busqueda){
					case(1):
						//Busqueda por nombre de cliente
						sql = sql + "where trim(hf.id.nomcli) = '"+strParametro.trim().toUpperCase()+"' and hf.id.cpendiente > 0";
						result = session
							.createQuery(sql)			
							.list();
						tx.commit();
						//lstHfacturasCredito = formatFacturaCredito(result);
						m.put("lstHfacturasCredito",lstHfacturasCredito);
						gvHfacturasCredito.dataBind();
						break;
					case(2):
						//Busqueda por codigo de cliente
						sql = sql + "where cast(hf.id.codcli as string) = '"+strParametro.trim()+"' and hf.id.cpendiente > 0";	
						result = session
							.createQuery(sql)			
							.list();
						tx.commit();
						//lstHfacturasCredito = formatFacturaCredito(result);
						m.put("lstHfacturasCredito",lstHfacturasCredito);
						gvHfacturasCredito.dataBind();
						break;
					case(3):
						//Busqueda por numero de factura
						sql = sql + "where cast(hf.id.nofactura as string) = '"+strParametro.trim()+"' and hf.id.cpendiente > 0";	
						result = session
							.createQuery(sql)			
							.list();
						tx.commit();
						//lstHfacturasCredito = formatFacturaCredito(result);
						m.put("lstHfacturasCredito",lstHfacturasCredito);
						gvHfacturasCredito.dataBind();
						break;
				}
			}
		}catch(Exception ex){
			System.out.print("==> Excepción capturada en BuscarFacturasCredito: " + ex);
		}finally{
			try {HibernateUtilPruebaCn.closeSession(session); } catch (Exception e2) {e2.printStackTrace(); }
		}
	}
/****************SET BUSQUEDA CREDITO*****************/	
	public void setBusquedaCredito(ValueChangeEvent e){
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		String strBusqueda = cmbBusquedaCredito.getValue().toString();
		m.put("strBusquedaCredito", strBusqueda);
	}
/************FORMATEAR CAMPOS DE CREDITO PARA VISUALIZACION:: recibe una lista de Hfacturajde y retorna una lista de Hfactura************************/
	/*public ArrayList formatFacturaCredito(List lstFacturas){
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		Divisas divisas = new Divisas();
		ArrayList lstResultado = new ArrayList();
		Hfacturajde[] hFacJde = new Hfacturajde[lstFacturas.size()];
		Hfactura hFac = null;
		String sql = "";		
		List result = new ArrayList();
		Session session = HibernateUtil.getSessionFactoryMCAJA().openSession();
		double total = 0;
		double ivaTotal = 0;
		double ivaActual = 0;
		String sTotal = null;
		String sIvaTotal = null;
		double tasa = 0,equiv = 0;
		double totalfac = 0;
		
		try{
			for (int i = 0; i < lstFacturas.size(); i++){
				hFacJde[i] = (Hfacturajde)lstFacturas.get(i);
				JulianToCalendar fecha = new JulianToCalendar(hFacJde[i].getId().getFecha());
				JulianToCalendar fechagrab = new JulianToCalendar(hFacJde[i].getId().getFechagrab());
				double subtotal = (hFacJde[i].getId().getSubtotal())/100.0;
				String sSubtotal = divisas.formatDouble(subtotal);
				//****************Anexar Detalle a facturas y formatear*************************/	
		/*		sql = "from Dfacturajde as df where df.id.nofactura = " + hFacJde[i].getId().getNofactura() +  " and df.id.codcli = " + hFacJde[i].getId().getCodcli() + " and df.id.fecha = " + hFacJde[i].getId().getFecha();
				
				Transaction tx = session.beginTransaction();
				result = session
					.createQuery(sql)			
					.list();
				tx.commit();
				Dfacturajde[] dFacJde = new Dfacturajde[result.size()];
				Dfactura dFac = null;
				List lstDfactura = new ArrayList();	
				total = 0;
				ivaTotal = 0;
				for (int j = 0; j < result.size(); j++){
					dFacJde[j] = (Dfacturajde)result.get(j);
					JulianToCalendar fechaDet = new JulianToCalendar(dFacJde[j].getId().getFecha());
					
					double precioUnit = (dFacJde[j].getId().getPreciounit())/100.0;
					String sPrecioUnit = divisas.formatDouble(precioUnit);
					tasa = hFacJde[i].getId().getTasa().doubleValue();
					double factor = dFacJde[j].getId().getFactor()/1000.0;
					dFac = new Dfactura(
							dFacJde[j].getId().getNofactura(),
							fechaDet.toString(),
							new String(dFacJde[j].getId().getCoditem(),"Cp1047"),
							dFacJde[j].getId().getDescitem(),
							sPrecioUnit,
							dFacJde[j].getId().getCant(),
							new String(dFacJde[j].getId().getImpuesto(),"Cp1047"),
							factor,
							new String(hFacJde[i].getId().getMoneda(),"Cp1047"),
							tasa
					);

					lstDfactura.add(dFac);
					factor = factor*0.01;
					ivaActual = precioUnit*factor;
					ivaTotal = ivaTotal + (precioUnit*factor);
					//formatear iva
					sIvaTotal = divisas.formatDouble(ivaTotal);
					total = total + (precioUnit + ivaActual);
					//formatear total
					sTotal = divisas.formatDouble(total);
					//total = Double.parseDouble(dfDigitos.format(total));			
				}
				//
				String moneda = new String(hFacJde[i].getId().getMoneda(),"Cp1047");
				if (moneda.equals("COR")){
					equiv = total;
				}
				else{
					equiv = total*tasa;
				}
				totalfac += total;
				m.put("mTotalFactura",totalfac);
				
				
				hFac = new Hfactura(						
						hFacJde[i].getId().getNofactura(),
						new String(hFacJde[i].getId().getTipofactura(),"Cp1047"),
						hFacJde[i].getId().getCodcli(),
						hFacJde[i].getId().getNomcli(),
						new String(hFacJde[i].getId().getCodunineg(),"Cp1047"),
						hFacJde[i].getId().getUnineg(),
						new String(hFacJde[i].getId().getCodsuc(),"Cp1047"),
						hFacJde[i].getId().getNomsuc(),
						new String(hFacJde[i].getId().getCodcomp(),"Cp1047"),
						hFacJde[i].getId().getNomcomp(),
						fecha.toString(),
						sSubtotal,
						new String(hFacJde[i].getId().getMoneda(),"Cp1047"),
						hFacJde[i].getId().getTasa().doubleValue(),
						new String(hFacJde[i].getId().getTipopago(),"Cp1047"),
						(hFacJde[i].getId().getCpendiente())/100.0,
						(hFacJde[i].getId().getDpendiente())/100.0,
						fechagrab.toString(),
						//hFacJde[i].getId().getHoragrab(),
						new String(hFacJde[i].getId().getHechopor(),"Cp1047"),
						new String(hFacJde[i].getId().getPantalla(),"Cp1047"),
						sIvaTotal,
						sTotal,
						divisas.formatDouble(equiv),
						new String(hFacJde[i].getId().getPartida(),"Cp1047"),
						lstDfactura
						);	
				lstResultado.add(hFac);
			}
		}catch(Exception ex){
			System.out.print("==> Excepción capturada en formatFacturaCredito: " + ex);
		}finally{
			session.close();
		}
		return lstResultado;
	}*/
	
	
	

//--------------------------------------------------------------------------

	
	
	//Registra las facturas seleccionadas
	public void getFacturasSelected(SelectedRowsChangeEvent e) {

		List codigos = null;
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = null;
		boolean selec = false;
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		
		try{
			tx = session.beginTransaction();		
			Iterator selectedRows = getGvHfacturasContado().getSelectedRows().iterator();    
	        
			while (selectedRows.hasNext()) {	
	       	   RowItem rowItem = (RowItem) selectedRows.next();
	           Object dataKey = gvHfacturasContado.getDataKeyValue(rowItem);
	           
	           if(codigos==null) {
	        	   codigos = new ArrayList();           
	           }           
	           codigos.add(dataKey.toString());
	           selec = true;
	        }			
	       
			if(selec == true) {
		        if(codigos != null) {			
					List result = session
						.createQuery("FROM Hfactjdecon WHERE nofactura IN(:pCod)")
						.setParameterList("pCod", codigos)
						.list();
					tx.commit();					
					
					Hfactjdecon[] facturas = new Hfactjdecon[result.size()];					
					
					double total = 0;
					String[] mClienteF = new String[result.size()];
					String[] mMonedaF = new String[result.size()];
					
					for(int i=0; i<result.size(); i++) {
						facturas[i] = ((Hfactjdecon)result.get(i));	
						mClienteF[i] = facturas[i].getId().getNomcli().trim();
						mMonedaF[i] = facturas[i].getId().getMoneda().trim();						
					}
					m.put("mClienteF", mClienteF);
					m.put("mMonedaF", mMonedaF);					
					m.put("mNomCliente", facturas[0].getId().getNomcli());					
					m.put("sCompania", facturas[0].getId().getCodcomp());
					m.put("mTasaCambio", facturas[0].getId().getTasa());					
					selectedFacs = formatFactura(result);
					
					//m.remove("selectedFacsCredito");
					m.put("facturasSelected", selectedFacs);
		        }
		        
			} else {			
				selectedFacs = null;
				m.put("facturasSelected", selectedFacs);
			}
			gvfacturasSelec.dataBind();
		
		}catch(Exception ex){
			if (tx != null)
				tx.rollback();          	
        	System.out.print("=>Se produjo una excepcion en getFacturasSelected: " + ex);
        
		}finally{
			try {HibernateUtilPruebaCn.closeSession(session); } catch (Exception e2) {e2.printStackTrace(); }
		}
	}

	//Facturas de Credito
	public void getFacturasCredito(SelectedRowsChangeEvent e) {

		List codigos = null;
		Session session = null;
		boolean selec = false;
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		
		try{
			Iterator selectedRows = getGvHfacturasCredito().getSelectedRows().iterator();    
	        
			while (selectedRows.hasNext()) {	
	       	   RowItem rowItem = (RowItem) selectedRows.next();
	           Object dataKey = gvHfacturasCredito.getDataKeyValue(rowItem);
	           
	           if(codigos==null) {
	        	   codigos = new ArrayList();           
	           }           
	           codigos.add(dataKey.toString());
	           selec = true;
	        }			
	       
			if(selec == true) {
		        if(codigos != null) {	
		        	session = HibernateUtilPruebaCn.currentSession();
					Transaction tx  =  session.beginTransaction();		
					List result = session
						.createQuery("FROM Hfacturajde WHERE nofactura IN(:pCod)")
						.setParameterList("pCod", codigos)
						.list();
					tx.commit();					
					
					Hfacturajde[] facturas = new Hfacturajde[result.size()];					
					
					double total = 0;
					String[] mClienteF = new String[result.size()];
					String[] mMonedaF = new String[result.size()];
					
					for(int i=0; i<result.size(); i++) {
						facturas[i] = ((Hfacturajde)result.get(i));
						mClienteF[i] = facturas[i].getId().getNomcli().trim();
						//mMonedaF[i] = new String(facturas[i].getId().getMoneda(),"Cp1047").trim();												
					}			
					m.put("mClienteF", mClienteF);
					m.put("mMonedaF", mMonedaF);					
					m.put("mNomCliente", facturas[0].getId().getNomcli());
					//m.put("sCompania", new String(facturas[0].getId().getCodcomp(), "Cp1047"));										
					//selectedFacs = formatFacturaCredito(result);
					
					//m.remove("facturasSelected");
					m.put("selectedFacsCredito", selectedFacs);
					
		        }
		        
			} else {			
				selectedFacs = null;
			}
			gvfacturasSelecCredito.dataBind();			
			
		}catch(Exception ex){
        	System.out.print("=>Se produjo una excepcion en getFacturasSelected: " + ex);
        }finally{
        	try {HibernateUtilPruebaCn.closeSession(session); } catch (Exception e2) {e2.printStackTrace(); }
		}
	}

	/************************CAMBIAR MONEDA DEL DETALLE EN CREDITO***************************************/
		public void cambiarMonedaDetalleCredito(ValueChangeEvent e){
			
			Divisas divisas = new Divisas();
			Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
			Hfactura hFac = (Hfactura)m.get("HfacturaCredito");
			List lstDetalle = hFac.getDfactura();
			Dfactura dFac = null, newDfac = null;
			List lstNewDetalle = new ArrayList();
			String monedaActual = cmbMonedaDetalleCredito.getValue().toString();
			try{
				if(monedaActual.equals("COR")){
					txtSubtotalCred = divisas.formatDouble(hFac.getSubtotal() * hFac.getTasa().doubleValue()) + "";
					txtIvaCred =      divisas.formatDouble(hFac.getIva() * hFac.getTasa().doubleValue()) + "";
					//txtTotalCred = 	  divisas.formatDouble(divisas.formatStringToDouble(hFac.getTotal()) * hFac.getTasa()) + "";
					
					txtPendiente =  divisas.formatDouble(hFac.getCpendiente());
					
					for (int i = 0; i < lstDetalle.size(); i++){
						dFac = (Dfactura)lstDetalle.get(i); 
						dFac.setPreciounit(dFac.getPreciounit() * dFac.getTasa().doubleValue());
						lstNewDetalle.add(dFac);
					}
					
				}else {
					txtSubtotalCred = divisas.formatDouble(hFac.getSubtotal());
					txtIvaCred =      divisas.formatDouble(hFac.getIva());
					//txtTotalCred = 	  divisas.formatDouble(divisas.formatStringToDouble(hFac.getTotal()));
					
					txtPendiente =  divisas.formatDouble(hFac.getDpendiente());
					for (int i = 0; i < lstDetalle.size(); i++){
						dFac = (Dfactura)lstDetalle.get(i); 
						dFac.setPreciounit(dFac.getPreciounit()/dFac.getTasa().doubleValue());
						lstNewDetalle.add(dFac);
					}
				}
				m.put("lstDfacturasCredito",lstNewDetalle);
				
				gvDfacturasCredito.dataBind();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}

/************************CAMBIAR MONEDA DEL DETALLE en recibo de contado***************************************/
		public void cambiarMonedaDetalleReciboCredito(ValueChangeEvent e){
			Divisas divisas = new Divisas();
			
			Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
			Hfactura hFac = (Hfactura)m.get("HfacturaCredito");
			List lstDetalle = hFac.getDfactura();
			Dfactura dFac = null, newDfac = null;
			List lstNewDetalle = new ArrayList();
			String monedaActual = cmbMonedaDetalleReciboCredito.getValue().toString();
			try{
				if(monedaActual.equals("COR")){
					txtSubtotal = divisas.formatDouble(hFac.getSubtotal() * hFac.getTasa().doubleValue()) + "";
					txtIva =     hFac.getIva() * hFac.getTasa().doubleValue();
					//txtTotal = 	  divisas.formatDouble(divisas.formatStringToDouble(hFac.getTotal()) * hFac.getTasa()) + "";
					
					for (int i = 0; i < lstDetalle.size(); i++){
						dFac = (Dfactura)lstDetalle.get(i); 
						dFac.setPreciounit(dFac.getPreciounit() * dFac.getTasa().doubleValue());
						lstNewDetalle.add(dFac);
					}
					
				}else {
					txtSubtotal = divisas.formatDouble(hFac.getSubtotal());
					txtIva =      hFac.getIva();
					//txtTotal = 	  divisas.formatDouble(divisas.formatStringToDouble(hFac.getTotal()));
					
					for (int i = 0; i < lstDetalle.size(); i++){
						dFac = (Dfactura)lstDetalle.get(i); 
						dFac.setPreciounit(dFac.getPreciounit()/dFac.getTasa().doubleValue());
						lstNewDetalle.add(dFac);
					}
				}
				m.put("lstDfacturasCredito",lstNewDetalle);
				gvDfacturasCreditoRecibo.dataBind();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
/****************************************************************************************************************************/		
/*************************************QUITAR FACTURA DE CREDITO DEL DETALLE**********************************************/	
	
	public void	quitarFacturaCredito(ActionEvent e){
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		Divisas divisas = new Divisas();
		List lstDcontadoFacs = (List)m.get("selectedFacsCredito");
		List lstNewDFacs = new ArrayList();
		try {
			double montoAplicar = 0;//Double.parseDouble(m.get("mTotalFactura").toString());
			
			if (lstDcontadoFacs.size()>1){
				RowItem ri = (RowItem)e.getComponent().getParent().getParent();
				List lstA = (List) ri.getCells();
				//Columna a obtener: No. Factura
				Cell c = (Cell) lstA.get(2);
				HtmlOutputText noFactura = (HtmlOutputText) c.getChildren().get(0);
				int iNoFactura = Integer.parseInt(noFactura.getValue().toString());
				//Columna a obtener:Monto
				Cell c2 = (Cell) lstA.get(4);
				HtmlOutputText monto = (HtmlOutputText) c2.getChildren().get(0);
				String sMonto = monto.getValue().toString();
				Hfactura hFac = null;
				for(int i = 0;i < lstDcontadoFacs.size();i++){
					hFac = (Hfactura)lstDcontadoFacs.get(i);
					//if (hFac.getNofactura() == iNoFactura && (hFac.getTotal().trim()).equals(sMonto.trim())){
						//no las incluye en la lista
					//}else{
						lstNewDFacs.add(hFac);
						montoAplicar = montoAplicar + divisas.formatStringToDouble(hFac.getMontoAplicar());
					//}
				}
				lblMontoAplicarCredito.setValue(divisas.formatDouble(montoAplicar));
				m.put("mTotalFactura",divisas.formatDouble(montoAplicar));
				m.put("selectedFacsCredito", lstNewDFacs);
				gvfacturasSelecCredito.dataBind();
			}else{
				dgwMensajeDetalleCredito.setWindowState("normal");
			}		
		}catch(Exception ex){
			System.out.print("Se captura una excepcion en quitarFacturaCredito: " + ex);
		}
	}
	public void cerrarMensajeDetallefacturaCredito(ActionEvent e){
		dgwMensajeDetalleCredito.setWindowState("hidden");
	}

/*****************************SUMAR MONTO A APLICAR*************************************************************/	
	public void sumarMontoAplicar(CellValueChangeEvent e){
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
		
		try{
			//Determine the row and column being updated
	        List rows = gvfacturasSelecCredito.getRows();
	        RowItem row  = (RowItem)rows.get(e.getPosition().getRow());
	        int colpos = e.getPosition().getCol();
	        Divisas divisas = new Divisas();
	        String sEquiv;
	        
	        List lstSelectedFacs = (List)m.get("selectedFacsCredito"), lstNewSelectedFacs = new ArrayList();
	        
	        //Get the current data for that row.
	        Hfactura newFac = (Hfactura)gvfacturasSelecCredito.getDataRow(row);
	        String sNewMontoApl = e.getNewValue().toString();
	        String sOldMontoApl = null; 
	        Hfactura hFac = null;
	        double dFormatedMA = 0;
	       
	        double montoAplicar = 0;
	        if (!sNewMontoApl.equals("")){
	        	sNewMontoApl = divisas.formatDouble(Double.parseDouble(sNewMontoApl));
		        newFac.setMontoAplicar(sNewMontoApl);    
		        
		        for(int i = 0; i < lstSelectedFacs.size();i++){
		        	hFac = (Hfactura)lstSelectedFacs.get(i);
					//if (hFac.getNofactura() == newFac.getNofactura() && (hFac.getTotal().trim()).equals(newFac.getTotal())){
						lstNewSelectedFacs.add(newFac);
						montoAplicar = montoAplicar + divisas.formatStringToDouble(newFac.getMontoAplicar());
					//}else{		
						if (hFac.getMontoAplicar()!= null && !hFac.getMontoAplicar().equals("")){
							dFormatedMA = divisas.formatStringToDouble(hFac.getMontoAplicar());
							montoAplicar = montoAplicar + dFormatedMA;
							hFac.setMontoAplicar(divisas.formatDouble(dFormatedMA));
						}
						lstNewSelectedFacs.add(hFac);
					//}
		        }
	        }else{	
	        	sOldMontoApl = e.getOldValue().toString();
	        	if (!sOldMontoApl.equals("")){
	        		sOldMontoApl = divisas.formatDouble(Double.parseDouble(sOldMontoApl));
	        	}
	        	newFac.setMontoAplicar(sOldMontoApl);
	        	
	        	for(int i = 0; i < lstSelectedFacs.size();i++){
		        	hFac = (Hfactura)lstSelectedFacs.get(i);
					//if (hFac.getNofactura() == newFac.getNofactura() && (hFac.getTotal().trim()).equals(newFac.getTotal())){
						lstNewSelectedFacs.add(newFac);
						montoAplicar = montoAplicar + divisas.formatStringToDouble(newFac.getMontoAplicar());
					//}else{
						if (hFac.getMontoAplicar()!= null && !hFac.getMontoAplicar().equals("")){
							dFormatedMA = divisas.formatStringToDouble(hFac.getMontoAplicar());
							montoAplicar = montoAplicar + dFormatedMA;
							hFac.setMontoAplicar(divisas.formatDouble(dFormatedMA));
						}
						lstNewSelectedFacs.add(hFac);
					//}
		        }
	        	
	        	dgwMensajeDetalleCreditoFac.setWindowState("normal");
	        	
	        }
	        //dgwMensajeDetalleCreditoFac
	        //m.put("selFacsCredPrueba", lstNewSelectedFacs);
	        m.put("selectedFacsCredito", lstNewSelectedFacs);
	        lblMontoAplicarCredito.setValue(divisas.formatDouble(montoAplicar));
	        gvfacturasSelecCredito.dataBind();
	        srm.addSmartRefreshId(lblMontoAplicarCredito.getClientId(FacesContext.getCurrentInstance()));
	        srm.addSmartRefreshId(dgwMensajeDetalleCreditoFac.getClientId(FacesContext.getCurrentInstance()));
	        srm.addSmartRefreshId(gvfacturasSelecCredito.getClientId(FacesContext.getCurrentInstance()));
		}catch(Exception ex){
			System.out.print("Excepcion capturada en sumarMontoAplicar: " + ex);
		}
	}
	public void cerrarMensajeDetalleFacCredito(ActionEvent e){
		dgwMensajeDetalleCreditoFac.setWindowState("hidden");
	}
	
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
/****************GETTERS Y SETTERS*****************/
	public UIInput getCmbBusqueda() {
		return cmbBusqueda;
	}

	public void setCmbBusqueda(UIInput cmbBusqueda) {
		this.cmbBusqueda = cmbBusqueda;
	}

	public List getLstBusqueda() {
		if(lstBusqueda == null){
			lstBusqueda = new ArrayList();	
			lstBusqueda.add(new SelectItem("1","Nombre Cliente","Búsqueda por nombre de cliente"));
			lstBusqueda.add(new SelectItem("2","Código Cliente","Búsqueda por código de cliente"));
			lstBusqueda.add(new SelectItem("3","No. Factura","Búsqueda por número de factura"));
		}
		return lstBusqueda;
	}

	public void setLstBusqueda(List lstBusqueda) {
		this.lstBusqueda = lstBusqueda;
	}
	
	public HtmlGridView getGvHfacturasContado() {
		return gvHfacturasContado;
	}

	public void setGvHfacturasContado(HtmlGridView gvHfacturasContado) {
		this.gvHfacturasContado = gvHfacturasContado;
	}

	public List getLstHfacturasContado() {
		try{
			lstHfacturasContado = (List)m.get("lstHfacturasContado");	
			if(m.get("mostrar") != null){
				gvHfacturasContado.setStyle("visibility:hidden");
			}else{
				gvHfacturasContado.setStyle("visibility:visible");
			}
		}catch(Exception ex){
			System.out.print("Se capturo una excepcion en FacturaCtrl.getLstHfacturasContado: " + ex);
		}
		//SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
		//srm.addSmartRefreshId(txtMensaje.getClientId(FacesContext.getCurrentInstance()));
		return lstHfacturasContado;
	}

	public void setLstHfacturasContado(List lstHfacturasContado) {
		this.lstHfacturasContado = lstHfacturasContado;
	}
	public UIInput getTxtParametro() {
		return txtParametro;
	}
	public void setTxtParametro(UIInput txtParametro) {
		this.txtParametro = txtParametro;
	}
	public DialogWindow getDgwDetalleContado() {
		return dgwDetalleContado;
	}
	public void setDgwDetalleContado(DialogWindow dgwDetalleContado) {
		this.dgwDetalleContado = dgwDetalleContado;
	}
	public List getLstDfacturasContado() {
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		Object o = m.get("bapp2");
		if(o == null){	
			lstDfacturasContado = new ArrayList();
			m.put("lstDfacturasContado",lstDfacturasContado);
			m.put("bapp2", "y");
		}else {
			lstDfacturasContado = (List)m.get("lstDfacturasContado");
		}	
		return lstDfacturasContado;
	}
	public void setLstDfacturasContado(List lstDfacturasContado) {
		this.lstDfacturasContado = lstDfacturasContado;
	}
	public GridView getGvDfacturasContado() {
		return gvDfacturasContado;
	}
	public void setGvDfacturasContado(GridView gvDfacturasContado) {
		this.gvDfacturasContado = gvDfacturasContado;
	}
	public String getTxtCliente() {
		return txtCliente;
	}
	public void setTxtCliente(String txtCliente) {
		this.txtCliente = txtCliente;
	}
	public String getTxtCodigoCliente() {
		return txtCodigoCliente;
	}
	public void setTxtCodigoCliente(String txtCodigoCliente) {
		this.txtCodigoCliente = txtCodigoCliente;
	}
	public String getTxtFechaFactura() {
		return txtFechaFactura;
	}
	public void setTxtFechaFactura(String txtFechaFactura) {
		this.txtFechaFactura = txtFechaFactura;
	}
	public String getTxtNofactura() {
		return txtNofactura;
	}
	public void setTxtNofactura(String txtNofactura) {
		this.txtNofactura = txtNofactura;
	}
	public String getTxtUnineg() {
		return txtUnineg;
	}
	public void setTxtUnineg(String txtUnineg) {
		this.txtUnineg = txtUnineg;
	}
	public double getTxtIva() {
		return txtIva;
	}
	public void setTxtIva(double txtIva) {
		this.txtIva = txtIva;
	}
	public String getTxtSubtotal() {
		return txtSubtotal;
	}
	public void setTxtSubtotal(String txtSubtotal) {
		this.txtSubtotal = txtSubtotal;
	}
	public String getTxtTotal() {
		return txtTotal;
	}
	public void setTxtTotal(String txtTotal) {
		this.txtTotal = txtTotal;
	}
	
public HtmlOutputText getLblTasaDetalle() {
		return lblTasaDetalle;
	}
	public void setLblTasaDetalle(HtmlOutputText lblTasaDetalle) {
		this.lblTasaDetalle = lblTasaDetalle;
	}
public HtmlDropDownList getCmbMonedaDetalle() {
		return cmbMonedaDetalle;
	}
	public void setCmbMonedaDetalle(HtmlDropDownList cmbMonedaDetalle) {
		this.cmbMonedaDetalle = cmbMonedaDetalle;
	}
	public HtmlDropDownList getCmbMonedaDetalleReciboContado() {
		return cmbMonedaDetalleReciboContado;
	}
	public void setCmbMonedaDetalleReciboContado(
			HtmlDropDownList cmbMonedaDetalleReciboContado) {
		this.cmbMonedaDetalleReciboContado = cmbMonedaDetalleReciboContado;
	}
	
	public List getLstMonedasDetalle() {
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		Object o = m.get("bLstMonDetalle");
		if(o == null){	
			lstMonedasDetalle = new ArrayList();
			m.put("lstMonedasDetalle",lstMonedasDetalle);
			m.put("bLstMonDetalle", "y");
		}else {
			lstMonedasDetalle = (List)m.get("lstMonedasDetalle");
		}	
		return lstMonedasDetalle;
	}
	public void setLstMonedasDetalle(List lstMonedasDetalle) {
		this.lstMonedasDetalle = lstMonedasDetalle;
	}
	
	public List getLstMonedasDetalleReciboContado() {
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		Object o = m.get("bLstMonDetalleReciboCon");
		if(o == null){	
			lstMonedasDetalleReciboContado = new ArrayList();
			m.put("lstMonedasDetalleReciboContado",lstMonedasDetalleReciboContado);
			m.put("bLstMonDetalleReciboCon", "y");
		}else {
			lstMonedasDetalleReciboContado = (List)m.get("lstMonedasDetalleReciboContado");
		}	
		return lstMonedasDetalleReciboContado;
	}
	public void setLstMonedasDetalleReciboContado(
			List lstMonedasDetalleReciboContado) {
		this.lstMonedasDetalleReciboContado = lstMonedasDetalleReciboContado;
	}
	
	public DialogWindow getDgwDetalleEnReciboContado() {
		return dgwDetalleEnReciboContado;
	}
	public void setDgwDetalleEnReciboContado(DialogWindow dgwDetalleEnReciboContado) {
		this.dgwDetalleEnReciboContado = dgwDetalleEnReciboContado;
	}
	public GridView getGvDfacturasContadoRecibo() {
		return gvDfacturasContadoRecibo;
	}
	public void setGvDfacturasContadoRecibo(GridView gvDfacturasContadoRecibo) {
		this.gvDfacturasContadoRecibo = gvDfacturasContadoRecibo;
	}
	
	public DialogWindow getDgwMensajeDetalleContado() {
		return dgwMensajeDetalleContado;
	}
	public void setDgwMensajeDetalleContado(DialogWindow dgwMensajeDetalleContado) {
		this.dgwMensajeDetalleContado = dgwMensajeDetalleContado;
	}
	///////////////////////////////////////////////////////////
	public UIInput getCmbBusquedaCredito() {
		return cmbBusquedaCredito;
	}

	public void setCmbBusquedaCredito(UIInput cmbBusquedaCredito) {
		this.cmbBusquedaCredito = cmbBusquedaCredito;
	}

	public List getLstBusquedaCredito() {
		if(lstBusquedaCredito == null){
			lstBusquedaCredito = new ArrayList();	
			lstBusquedaCredito.add(new SelectItem("1","Nombre Cliente","Búsqueda por nombre de cliente"));
			lstBusquedaCredito.add(new SelectItem("2","Código Cliente","Búsqueda por código de cliente"));
			lstBusquedaCredito.add(new SelectItem("3","No. Factura","Búsqueda por número de factura"));
		}
		return lstBusquedaCredito;
	}

	public void setLstBusquedaCredito(List lstBusquedaCredito) {
		this.lstBusquedaCredito = lstBusquedaCredito;
	}
	
	public GridView getGvHfacturasCredito() {
		return gvHfacturasCredito;
	}

	public void setGvHfacturasCredito(GridView gvHfacturasCredito) {
		this.gvHfacturasCredito = gvHfacturasCredito;
	}

	public List getLstHfacturasCredito() {
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		Object o = m.get("bappCred");
		if(o == null){	
			lstHfacturasContado = new ArrayList();
			//listarFacturasCreditoPendientes();
			m.put("bappCred", "y");
		}else {
			lstHfacturasCredito = (List)m.get("lstHfacturasCredito");
		}	
		return lstHfacturasCredito;
	}

	public void setLstHfacturasCredito(List lstHfacturasCredito) {
		this.lstHfacturasCredito = lstHfacturasCredito;
	}
	public UIInput getTxtParametroCredito() {
		return txtParametroCredito;
	}
	public void setTxtParametroCredito(UIInput txtParametroCredito) {
		this.txtParametroCredito = txtParametroCredito;
	}
	public DialogWindow getDgwDetalleCredito() {
		return dgwDetalleCredito;
	}
	public void setDgwDetalleCredito(DialogWindow dgwDetalleCredito) {
		this.dgwDetalleCredito = dgwDetalleCredito;
	}
	public List getLstDfacturasCredito() {
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		Object o = m.get("bappCred2");
		if(o == null){	
			lstDfacturasCredito = new ArrayList();
			m.put("lstDfacturasCredito",lstDfacturasCredito);
			m.put("bappCred2", "y");
		}else {
			lstDfacturasCredito = (List)m.get("lstDfacturasCredito");
		}	
		return lstDfacturasCredito;
	}
	public void setLstDfacturasCredito(List lstDfacturasCredito) {
		this.lstDfacturasCredito = lstDfacturasCredito;
	}
	public GridView getGvDfacturasCredito() {
		return gvDfacturasCredito;
	}
	public void setGvDfacturasCredito(GridView gvDfacturasCredito) {
		this.gvDfacturasCredito = gvDfacturasCredito;
	}
	public String getTxtClienteCred() {
		return txtClienteCred;
	}
	public void setTxtClienteCred(String txtClienteCred) {
		this.txtClienteCred = txtClienteCred;
	}
	public String getTxtCodigoClienteCred() {
		return txtCodigoClienteCred;
	}
	public void setTxtCodigoClienteCred(String txtCodigoClienteCred) {
		this.txtCodigoClienteCred = txtCodigoClienteCred;
	}
	public String getTxtFechaFacturaCred() {
		return txtFechaFacturaCred;
	}
	public void setTxtFechaFacturaCred(String txtFechaFacturaCred) {
		this.txtFechaFacturaCred = txtFechaFacturaCred;
	}
	public String getTxtNofacturaCred() {
		return txtNofacturaCred;
	}
	public void setTxtNofacturaCred(String txtNofacturaCred) {
		this.txtNofacturaCred = txtNofacturaCred;
	}
	public String getTxtUninegCred() {
		return txtUninegCred;
	}
	public void setTxtUninegCred(String txtUninegCred) {
		this.txtUninegCred = txtUninegCred;
	}
	public String getTxtIvaCred() {
		return txtIvaCred;
	}
	public void setTxtIvaCred(String txtIvaCred) {
		this.txtIvaCred = txtIvaCred;
	}
	public String getTxtSubtotalCred() {
		return txtSubtotalCred;
	}
	public void setTxtSubtotalCred(String txtSubtotalCred) {
		this.txtSubtotalCred = txtSubtotalCred;
	}
	public String getTxtTotalCred() {
		return txtTotalCred;
	}
	public void setTxtTotalCred(String txtTotalCred) {
		this.txtTotalCred = txtTotalCred;
	}
	public String getTxtPendiente() {
		return txtPendiente;
	}
	public void setTxtPendiente(String txtPendiente) {
		this.txtPendiente = txtPendiente;
	}
	public GridView getGvfacturasSelec() {
		return gvfacturasSelec;
	}
	public void setGvfacturasSelec(GridView gvfacturasSelec) {
		this.gvfacturasSelec = gvfacturasSelec;
	}
	public List getSelectedFacs() {
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		/*Object o = m.get("appSel");
		if(o == null){	
			selectedFacs = new ArrayList();
			m.put("facturasSelected",selectedFacs);
			m.put("appSel", "y");
		}else{
			selectedFacs = (List) m.get("facturasSelected");
		}*/
		selectedFacs = (List) m.get("facturasSelected");
		return selectedFacs;
	}
	public void setSelectedFacs(List selectedFacs) {
		this.selectedFacs = selectedFacs;
	}
	public HtmlOutputText getTxtTasaDetalle() {
		return txtTasaDetalle;
	}
	public void setTxtTasaDetalle(HtmlOutputText txtTasaDetalle) {
		this.txtTasaDetalle = txtTasaDetalle;
	}
	public HtmlDropDownList getCmbMonedaDetalleCredito() {
		return cmbMonedaDetalleCredito;
	}
	public void setCmbMonedaDetalleCredito(HtmlDropDownList cmbMonedaDetalleCredito) {
		this.cmbMonedaDetalleCredito = cmbMonedaDetalleCredito;
	}
	public HtmlOutputText getLblMontoAplicar() {
		return lblMontoAplicar;
	}
	public void setLblMontoAplicar(HtmlOutputText lblMontoAplicar) {
		this.lblMontoAplicar = lblMontoAplicar;
	}
	
	public GridView getGvfacturasSelecCredito() {
		return gvfacturasSelecCredito;
	}
	public void setGvfacturasSelecCredito(GridView gvfacturasSelecCredito) {
		this.gvfacturasSelecCredito = gvfacturasSelecCredito;
	}
	public List getSelectedFacsCredito() {
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		
		selectedFacsCredito = (List) m.get("selectedFacsCredito");
								
		return selectedFacsCredito;
	}
	public void setSelectedFacsCredito(List selectedFacsCredito) {
		this.selectedFacsCredito = selectedFacsCredito;
	}
	public DialogWindow getDgwMensajeDetalleCredito() {
		return dgwMensajeDetalleCredito;
	}
	public void setDgwMensajeDetalleCredito(DialogWindow dgwMensajeDetalleCredito) {
		this.dgwMensajeDetalleCredito = dgwMensajeDetalleCredito;
	}
	public HtmlOutputText getLblMontoAplicarCredito() {
		return lblMontoAplicarCredito;
	}
	public void setLblMontoAplicarCredito(HtmlOutputText lblMontoAplicarCredito) {
		this.lblMontoAplicarCredito = lblMontoAplicarCredito;
	}
	public DialogWindow getDgwDetalleEnReciboCredito() {
		return dgwDetalleEnReciboCredito;
	}
	public void setDgwDetalleEnReciboCredito(DialogWindow dgwDetalleEnReciboCredito) {
		this.dgwDetalleEnReciboCredito = dgwDetalleEnReciboCredito;
	}
	
	public List getLstMonedasDetalleReciboCredito() {
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		Object o = m.get("bLstMonDetalleReciboCre");
		if(o == null){	
			lstMonedasDetalleReciboCredito = new ArrayList();
			m.put("lstMonedasDetalleReciboCredito",lstMonedasDetalleReciboCredito);
			m.put("bLstMonDetalleReciboCre", "y");
		}else {
			lstMonedasDetalleReciboCredito = (List)m.get("lstMonedasDetalleReciboCredito");
		}	
		return lstMonedasDetalleReciboCredito;
	}
	
	public void setLstMonedasDetalleReciboCredito(
			List lstMonedasDetalleReciboCredito) {
		this.lstMonedasDetalleReciboCredito = lstMonedasDetalleReciboCredito;
	}
	public HtmlDropDownList getCmbMonedaDetalleReciboCredito() {
		return cmbMonedaDetalleReciboCredito;
	}
	public void setCmbMonedaDetalleReciboCredito(
			HtmlDropDownList cmbMonedaDetalleReciboCredito) {
		this.cmbMonedaDetalleReciboCredito = cmbMonedaDetalleReciboCredito;
	}
	public GridView getGvDfacturasCreditoRecibo() {
		return gvDfacturasCreditoRecibo;
	}
	public void setGvDfacturasCreditoRecibo(GridView gvDfacturasCreditoRecibo) {
		this.gvDfacturasCreditoRecibo = gvDfacturasCreditoRecibo;
	}
	
	public DialogWindow getDgwMensajeDetalleCreditoFac() {
		return dgwMensajeDetalleCreditoFac;
	}
	public void setDgwMensajeDetalleCreditoFac(
			DialogWindow dgwMensajeDetalleCreditoFac) {
		this.dgwMensajeDetalleCreditoFac = dgwMensajeDetalleCreditoFac;
	}
	public HtmlOutputText getLblMonedaDomesticaCon() {
		return lblMonedaDomesticaCon;
	}
	public void setLblMonedaDomesticaCon(HtmlOutputText lblMonedaDomesticaCon) {
		this.lblMonedaDomesticaCon = lblMonedaDomesticaCon;
	}
	public HtmlOutputText getTxtMensaje() {
		return txtMensaje;
	}
	public void setTxtMensaje(HtmlOutputText txtMensaje) {
		this.txtMensaje = txtMensaje;
	}
	public String getSMensaje() {
		if (m.get("sMensaje") != null){
			sMensaje = m.get("sMensaje").toString();
		}
		return sMensaje;
	}
	public void setSMensaje(String sMensaje) {
		sMensaje = sMensaje;
	}
	public HtmlDropDownList getCmbFiltroFacturas() {
		return cmbFiltroFacturas;
	}
	public void setCmbFiltroFacturas(HtmlDropDownList cmbFiltroFacturas) {
		this.cmbFiltroFacturas = cmbFiltroFacturas;
	}
	public List getLstFiltroFacturas() {
		if (lstFiltroFacturas == null){
			lstFiltroFacturas = new ArrayList();
			lstFiltroFacturas.add(new SelectItem("01","Facturas del dia"));
			lstFiltroFacturas.add(new SelectItem("02","Facturas de dias anteriores"));
			lstFiltroFacturas.add(new SelectItem("03","Todas"));
		}
		return lstFiltroFacturas;
	}
	public void setLstFiltroFacturas(List lstFiltroFacturas) {
		this.lstFiltroFacturas = lstFiltroFacturas;
	}
	
}



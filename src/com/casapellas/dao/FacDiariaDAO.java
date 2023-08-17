package com.casapellas.dao;
/**
 * CASA PELLAS S.A.
 * Creado por.........: Juan Carlos Ñamendi Pineda
 * Fecha de Creación..: 09/06/2009
 * Última modificación: 13/11/2009
 * Modificado por.....:	Juan Carlos Ñamendi Pineda
 * 
 */
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import com.casapellas.controles.CompaniaCtrl;
import com.casapellas.controles.EmpleadoCtrl;
import com.casapellas.controles.FacturaCrtl;
import com.casapellas.controles.MonedaCtrl;
import com.casapellas.entidades.Dfactura;
import com.casapellas.entidades.F55ca014;
import com.casapellas.entidades.F55ca017;
import com.casapellas.entidades.Hfactura;
import com.casapellas.entidades.Vf55ca01;
import com.casapellas.navegacion.MenuDAO;
import com.casapellas.util.Divisas;
import com.casapellas.util.FechasUtil;
import com.ibm.faces.component.html.HtmlGraphicImageEx;
import com.infragistics.faces.grid.component.GridView;
import com.infragistics.faces.grid.component.RowItem;
import com.infragistics.faces.grid.component.html.HtmlGridView;
import com.infragistics.faces.input.component.html.HtmlDateChooser;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
import com.infragistics.faces.shared.component.DataRepeater;
import com.infragistics.faces.window.component.html.HtmlDialogWindow;

public class FacDiariaDAO {
	Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();	
	private String sMensaje;
	private HtmlGridView gvHfacturasDiario;
	private HtmlDropDownList cmbBusqueda;
	private HtmlInputText txtParametro;
	private List lstHfacturasDiario;
	private HtmlDropDownList cmbFiltroMonedas;
	private List lstFiltroMonedas;
	private List lstBusqueda;
	private List lstFiltroFacturas;
	private HtmlDropDownList cmbFiltroFacturas;
	
	private HtmlGraphicImageEx imgWatermark;
	
	private HtmlDateChooser dcFechaDesde;
	private HtmlDateChooser	dcFechaHasta;
	
	//Detalle de factura 
	private HtmlDialogWindow dgwDetalleDiario;
	private GridView gvDfacturasDiario;
	private List lstDfacturasDiario;
	private HtmlDropDownList cmbMonedaDetalle;
	private List lstMonedasDetalle;
	private String txtFechaFactura = "";
	private String txtNofactura = "";
	private String txtCliente = "";
	private String txtCodigoCliente = "";
	private String txtCodUnineg = "";
	private String txtUnineg = "";
	private double txtSubtotal;
	private double txtIva;
	private String txtTotal = "";
	private HtmlOutputText lblTasaDetalle;
	private HtmlOutputText txtTasaDetalle;
	private HtmlOutputText lblMonedaDomesticaCon;
	
	//devoluciones
	private List lstHDevolucionesDiario;
	private HtmlGridView gvHDevolucionesDiario;
	
	//resumen
	private double totalVentas;
	private double totalDevoluciones;
	private double totalIngresos;
	
	private double totalFacContado, totalFacCredito,
	totalContadoDev, totalCreditoDev, totalIngresoCont, totalIngresoCred;
	
	private int cantFactContado;
	private int cantDevContado;
	private int cantFactCredito;
	private int cantDevCredito;
	
	private String lblVendedorCont;
	private String txtVendedorCont;
	
	private HtmlDropDownList cmbFiltroCompaniaDiario;
	private List lstFiltroCompaniaDiario;
	
	private String txtNoFacturaOriginal  ;
	private String txtTipoFacturaOriginal;
	private String txtFechaFactOriginal  ;
	private String txtMontoFactOriginal  ;
	private String txtMontoEquivFctOriginal ;
	private boolean cargardevolucion;
	
	@SuppressWarnings("unchecked")
	public void cargarDetalleFactura(ActionEvent ev){
		try {
			
			RowItem ri = (RowItem)ev.getComponent().getParent().getParent();
			Hfactura f = (Hfactura)DataRepeater.getDataRow(ri);
			String sMonedaBase = CompaniaCtrl.obtenerMonedaBasexComp(f.getCodcomp());
			m.put("Hfactura", f);
			
			txtNofactura = f.getNofactura()+"";
			txtFechaFactura = f.getFecha();
			txtCliente = f.getNomcli() + " (Nombre)";
			txtCodigoCliente = f.getCodcli() + " (Código)";
			txtCodUnineg = f.getCodunineg();	
			txtUnineg = f.getUnineg();	
			txtTasaDetalle.setValue(String.format("%1$,.4f", f.getTasa()));
			lblVendedorCont = "Hecho por :";
			txtVendedorCont = f.getHechopor();
			
			lstDfacturasDiario = FacturaCrtl.formatDetalle(f);
			f.setDfactura(lstDfacturasDiario);
			m.put("lstDfacturasDiario",lstDfacturasDiario);
			
			txtIva = (f.getTasa().compareTo(BigDecimal.ONE) == 1 )?
					(f.getDpendiente() - f.getSubtotalf() ):
					(f.getCpendiente() - f.getSubtotal() );
					
			txtSubtotal = (f.getTasa().compareTo(BigDecimal.ONE) == 1) ? 
						f.getSubtotalf() : f.getSubtotal();	
					
			txtTotal = 	String.format("%1$,.2f", new BigDecimal(	
						Double.toString(((f.getTasa().compareTo(
								BigDecimal.ONE) == 1) ?
								f.getDpendiente() : f.getCpendiente()))));
			
			txtTasaDetalle.setStyle("visibility: hidden");
			lblTasaDetalle.setStyle("visibility: hidden");
			
			lstMonedasDetalle = new ArrayList<SelectItem>();
			lstMonedasDetalle.add(new SelectItem(f.getMoneda(),f.getMoneda()));
			
			if(f.getMoneda().compareTo (sMonedaBase) != 0 ){
				lstMonedasDetalle.add(new SelectItem(sMonedaBase,sMonedaBase));
				lblTasaDetalle.setValue("Tasa de Cambio: ");
				lblTasaDetalle.setStyle("visibility: visible");
				lblTasaDetalle.setStyleClass("frmLabel2");
				txtTasaDetalle.setStyle("visibility: visible");
				txtTasaDetalle.setStyleClass("frmLabel3");
			}
			m.put("lstMonedasDetalle",lstMonedasDetalle);
			
			
			//&& ====== Datos por devolucion
			cargardevolucion = f.getNodoco() != 0 && f.getTipodoco().compareTo("") != 0 ;
			if(cargardevolucion){

				Hfactura hfo = com.casapellas.controles.tmp.FacturaCrtl.
						getFacturaOriginal(f.getNodoco(), f.getTipodoco(), 
						f.getCodsuc(), f.getCodcomp(), f.getCodunineg(),  f.getFechadoco() );
				
				txtNoFacturaOriginal  = Integer.toString( f.getNodoco() ); 
				txtTipoFacturaOriginal = f.getTipodoco()  ;
				txtFechaFactOriginal =  String.format("%1$,.2f", new BigDecimal(Double.toString(f.getTotal())));
				txtFechaFactOriginal = f.getTotaldomtmp().toString();
				
				 if( hfo != null) {
					 txtFechaFactOriginal  =   FechasUtil.formatDatetoString( FechasUtil.julianToDate(hfo.getFechajulian()), "dd/MM/yyyy") ;
					 txtMontoFactOriginal = String.format("%1$,.2f", new BigDecimal(Double.toString(hfo.getCpendiente())));
					 txtMontoEquivFctOriginal = String.format("%1$,.2f", new BigDecimal(Double.toString(hfo.getDpendiente())));
				 } 
			}
			
			cmbMonedaDetalle.dataBind();
			gvDfacturasDiario.dataBind();
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}finally{
			dgwDetalleDiario.setWindowState("normal");	
		}
	}
	
/*******DETALLE DE FACTURA DE CONTADO***************************************/	
	public void mostrarDetalleDiario(ActionEvent e){
		dgwDetalleDiario.setWindowState("normal");
		List lstFacsActuales = new ArrayList();
		Hfactura hFacClicked = null;
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		String sMonedaBase = "";
		try{			
			RowItem ri = (RowItem)e.getComponent().getParent().getParent();
			
			String sId = e.getComponent().getClientId(FacesContext.getCurrentInstance());
			if(sId.contains("lnkDetalleFacturaContado")){
				lstFacsActuales = (List) m.get("lstHfacturasDiario"); 
				hFacClicked =  (Hfactura)gvHfacturasDiario.getDataRow(ri);
			}else{
				lstFacsActuales = (List) m.get("lstHDevolucionesDiario"); 
				hFacClicked =  (Hfactura)gvHDevolucionesDiario.getDataRow(ri);
			}
			
			//obtener companias x caja
			sMonedaBase = cCtrl.sacarMonedaBase((F55ca014[])m.get("cont_f55ca014"), hFacClicked.getCodcomp());
			Hfactura hFac = new Hfactura();		
			Divisas divisas = new Divisas();
			FacturaCrtl facCtrl = new FacturaCrtl();
			
			for (int i = 0; i < lstFacsActuales.size(); i++){
				hFac = (Hfactura)lstFacsActuales.get(i);
				if (hFac.getNofactura() == hFacClicked.getNofactura() && hFac.getTipofactura().equals(hFacClicked.getTipofactura()) 
					&& hFac.getCodsuc().equals(hFacClicked.getCodsuc()) && hFac.getCodunineg().equals(hFacClicked.getCodunineg())){
					//poner valor a labels del detalle
					txtNofactura = hFac.getNofactura()+"";
					txtFechaFactura = hFac.getFecha();
					txtCliente = hFac.getNomcli() + " (Nombre)";
					txtCodigoCliente = hFac.getCodcli() + " (Código)";
					txtCodUnineg = hFac.getCodunineg();	
					txtUnineg = hFac.getUnineg();		
					
					txtTasaDetalle.setValue(hFac.getTasa()+"");
					
					
					txtSubtotal = hFac.getSubtotal();
					
					if(hFac.getCodVendedor() > 0){
						EmpleadoCtrl emp = new EmpleadoCtrl();
						lblVendedorCont = "Vendedor:";
						txtVendedorCont = emp.buscarEmpleadoxCodigo(hFac.getCodVendedor());
					}else{
						lblVendedorCont = "Facturador:";
						txtVendedorCont = hFac.getHechopor();
					}
					//actualizar lista de detalle
					
					//actualizar lista de detalle buscar detalle
					lstDfacturasDiario = FacturaCrtl.formatDetalle(hFac);
					hFac.setDfactura(lstDfacturasDiario);
					
					m.put("lstDfacturasDiario",lstDfacturasDiario);
					
				
					txtIva = hFac.getTotal() - hFac.getSubtotal();
					hFac.setIva(txtIva);
					txtTotal = divisas.formatDouble(hFac.getTotal());
					
					lstMonedasDetalle = new ArrayList();
					String moneda = ((Hfactura)lstFacsActuales.get(i)).getMoneda();
					if(moneda.equals(sMonedaBase)){
						lstMonedasDetalle.add(new SelectItem(sMonedaBase,sMonedaBase));
						txtTasaDetalle.setStyle("visibility: hidden");
						lblTasaDetalle.setStyle("visibility: hidden");
						
					}else{
						lstMonedasDetalle.add(new SelectItem(moneda,moneda));
						lstMonedasDetalle.add(new SelectItem(sMonedaBase,sMonedaBase));
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
					gvDfacturasDiario.dataBind();
					break;
				}
			}
		}catch(Exception ex){
			ex.printStackTrace(); 
		}
	}
/***************************************************************************************************************************************/
	public void cerrarDetalleDiario(ActionEvent e){
 
		
		try{
			dgwDetalleDiario.setWindowState("hidden");
 
			
		}catch(Exception ex){
			ex.printStackTrace();
			ex.printStackTrace();
		}
	}
	/*****************************************************************************************/
	/************************CAMBIAR MONEDA DEL DETALLE***************************************/
	@SuppressWarnings("unchecked")
	public void cambiarMonedaDetalle(ValueChangeEvent e){
		try{

			Hfactura f = (Hfactura) m.get("Hfactura");
			List<Dfactura> lstDetalle = f.getDfactura();
			
			String monedaActual = cmbMonedaDetalle.getValue().toString();
			String sMonedaBase  = CompaniaCtrl.sacarMonedaBase((F55ca014[])m.get("cont_f55ca014"), f.getCodcomp());
			
			txtSubtotal = (monedaActual.compareTo(sMonedaBase) == 0) ? 
								f.getSubtotal() : f.getSubtotalf();	
								
			txtTotal =  new BigDecimal(
							Double.toString(((monedaActual.compareTo
									(sMonedaBase) == 0) ?  
							f.getCpendiente(): f.getTotalf()) ) ).toString();
			
			txtIva = new BigDecimal(txtTotal).subtract(new BigDecimal(
							Double.toString(txtSubtotal))).doubleValue() ;
			
			txtTotal = String.format("%1$,.2f", new BigDecimal(txtTotal) );
			
			if (monedaActual.compareTo(sMonedaBase) == 0) {
				for (Dfactura df : lstDetalle) {
					df.setPreciounit(new BigDecimal(Double.toString(df
							.getPreciounit())).multiply(df.getTasa())
							.doubleValue());
				}
			} else {
				for (Dfactura df : lstDetalle) {
					df.setPreciounit(new BigDecimal(Double.toString(df
							.getPreciounit())).divide(df.getTasa(), 2,
							RoundingMode.HALF_UP).doubleValue());
				}
			}
			m.put("lstDfacturasDiario",lstDetalle);
			gvDfacturasDiario.dataBind();
			
		}catch(Exception ex){ 
			ex.printStackTrace(); 
		}
	}
/***************************************************************************************************************************************/
	@SuppressWarnings("unchecked")
	public void refrescarVista(ActionEvent ev){
		String[] sTiposDoc = null;
		FacturaCrtl facCtrl = new FacturaCrtl();

		try{
 
			lstHfacturasDiario = FacturaCrtl.obtenerFacturas("",
					"", "", "", 0, new Date(), new Date());
			
			
			//&& ============ Clasificacion de facturas.
			ArrayList<Hfactura>contado = new ArrayList<Hfactura>();
			ArrayList<Hfactura>credito = new ArrayList<Hfactura>();
			ArrayList<Hfactura>faccontado = new ArrayList<Hfactura>();
			ArrayList<Hfactura>devcontado = new ArrayList<Hfactura>();
			ArrayList<Hfactura>faccredito = new ArrayList<Hfactura>();
			ArrayList<Hfactura>devcredito = new ArrayList<Hfactura>();
			
			CollectionUtils.forAllDo(lstHfacturasDiario, new Closure(){
				public void execute(Object o) {
					Hfactura h = (Hfactura)o;
					
					String tipopago = h.getTipopago();
					
					if(tipopago == null || tipopago.trim().isEmpty()  )
						tipopago =  "-1";
					if(tipopago.trim().compareTo("PM") == 0 )
						tipopago =  "3";
					
					h.setTipopago(tipopago);
						
				}
			});
			
			contado = (ArrayList<Hfactura>)CollectionUtils.select(
					lstHfacturasDiario, new Predicate(){
					public boolean evaluate(Object o) {
						Hfactura h = (Hfactura)o;
						int valor =  Integer.parseInt( h.getTipopago().trim() );
						return ( valor == 1 || valor == 0  );
					}
					}
				);
			
			credito = (ArrayList<Hfactura>)CollectionUtils
						.subtract(lstHfacturasDiario, contado);
			
			faccontado = (ArrayList<Hfactura>)CollectionUtils.select(
					contado, new Predicate(){
						public boolean evaluate(Object o) {
							Hfactura h = (Hfactura)o;
							return h.getNodoco() == 0 && h.
								getTipodoco().trim().compareTo("") == 0 ;
						}
					}
				); 
			devcontado = (ArrayList<Hfactura>)CollectionUtils
							.subtract(contado, faccontado);
			
			faccredito = (ArrayList<Hfactura>)CollectionUtils.select(
					credito, new Predicate(){
					public boolean evaluate(Object o) {
						Hfactura h = (Hfactura)o;
						return h.getNodoco() == 0 && h.getTipodoco()
								.trim().compareTo("") == 0 ;
					}
				}
			); 
			devcredito = (ArrayList<Hfactura>)CollectionUtils
								.subtract(credito, faccredito);
			
			BigDecimal totalFacCo = MenuDAO.sumarTotalesFactura(faccontado);
			BigDecimal totalDevCo = MenuDAO.sumarTotalesFactura(devcontado);
			BigDecimal totalFacCr = MenuDAO.sumarTotalesFactura(faccredito);
			BigDecimal totalDevCr = MenuDAO.sumarTotalesFactura(devcredito);
			BigDecimal totalVentas = totalFacCo.add(totalFacCr);
			BigDecimal totalDevols = totalDevCo.add(totalDevCr);	
			
			
			m.put("lstHfacturasDiario", lstHfacturasDiario);
			m.put("dTotalVentas", totalVentas.doubleValue());
			m.put("dTotalDevoluciones", totalDevols.doubleValue());
			m.put("dTotalIngresos", totalVentas.subtract(
					totalDevols).doubleValue());
			
			m.put("dTotalFacContado", totalFacCo.doubleValue());
			m.put("dTotalContadoDev", totalDevCo.doubleValue());
			m.put("dTotalIngresoCont", totalFacCo.subtract(
					totalDevCo).doubleValue());
			
			m.put("dTotalFacCredito", totalFacCr);
			m.put("dTotalCreditoDev", totalDevCr);
			m.put("dTotalIngresoCred",  totalFacCr.subtract(
					totalDevCr).doubleValue());
			
			m.put("iCantFactContado", faccontado.size());
			m.put("iCantDevContado", devcontado.size());
			m.put("iCantFactCredito", faccredito.size());
			m.put("iCantDevCredito", devcredito.size());
			
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			gvHfacturasDiario.setPageIndex(0);
			gvHfacturasDiario.dataBind();
		}
	}
/***************************SET BUSQUEDA**************************************************************************************************/	
	public void setBusqueda(ValueChangeEvent e){
//		m.put("strBusqueda", strBusqueda);
	}
/*****************************************************************************************************************************************/
	/****************BUSCAR FACTURAS DE CONTADO DEL DIA POR PARAMETROS*****************/
	@SuppressWarnings("unchecked")
	public void BuscarFacturas(ActionEvent e){
		
		String strParametro = "";
		String sMoneda = "",sEstado = "",sCompania = "", sDesde,sHasta;
		
		sMensaje = "";
		Date dDesde = null;
		Date dHasta = null;
		
		try {
		
			int busqueda = 0 ;
			
			if(dcFechaDesde.getValue() != null){
				sDesde = dcFechaDesde.getValue().toString();
				if(!sDesde.trim().equals("")){
					dDesde = (Date)(dcFechaDesde.getValue());
				}
			}
			if(dcFechaHasta.getValue() != null){
				sHasta = dcFechaHasta.getValue().toString();
				if(!sHasta.trim().equals("")){
					dHasta = (Date)(dcFechaHasta.getValue());
				}
			}
				
			sMoneda = cmbFiltroMonedas.getValue().toString();
			sEstado = cmbFiltroFacturas.getValue().toString();
			sCompania = cmbFiltroCompaniaDiario.getValue().toString();
			busqueda  = Integer.parseInt(cmbBusqueda.getValue().toString());
			
			if(txtParametro.getValue() != null) 
				strParametro = txtParametro.getValue().toString().trim();
			if (sMoneda.compareTo("01") == 0)
				sMoneda = new String("");
			if (sCompania.compareTo("01") == 0)
				sCompania = new String("");
			if(strParametro.trim().compareTo("") == 0)
				busqueda = 0;
			
			switch (Integer.parseInt(sEstado)) {
			case 1:
				sEstado="000";
				break;
			case 2:
				sEstado="";
				break;
			case 3:
				sEstado="A";
				break;
			default:
				break;
			}		
			
			
			lstHfacturasDiario = FacturaCrtl.obtenerFacturas(sMoneda,
						strParametro, sEstado, sCompania, busqueda, dDesde, dHasta);
			
			if( lstHfacturasDiario == null ){
				return;
			}
			
			//&& ============ Clasificacion de facturas.
			ArrayList<Hfactura>contado = new ArrayList<Hfactura>();
			ArrayList<Hfactura>credito = new ArrayList<Hfactura>();
			ArrayList<Hfactura>faccontado = new ArrayList<Hfactura>();
			ArrayList<Hfactura>devcontado = new ArrayList<Hfactura>();
			ArrayList<Hfactura>faccredito = new ArrayList<Hfactura>();
			ArrayList<Hfactura>devcredito = new ArrayList<Hfactura>();
			
			CollectionUtils.forAllDo(lstHfacturasDiario, new Closure(){
				public void execute(Object o) {
					Hfactura h = (Hfactura)o;
					
					String tipopago = h.getTipopago();
					
					if(tipopago == null || tipopago.trim().isEmpty()  )
						tipopago =  "-1";
					if(tipopago.trim().compareTo("PM") == 0 )
						tipopago =  "3";
					
					h.setTipopago(tipopago);
						
				}
			});
			
			contado = (ArrayList<Hfactura>)CollectionUtils.select(
					lstHfacturasDiario, new Predicate(){
					public boolean evaluate(Object o) {
						Hfactura h = (Hfactura)o;
						int valor =  Integer.parseInt( h.getTipopago().trim() );
						return ( valor == 1 || valor == 0  );
					}
					}
				);
			
			credito = (ArrayList<Hfactura>)CollectionUtils
						.subtract(lstHfacturasDiario, contado);
			
			faccontado = (ArrayList<Hfactura>)CollectionUtils.select(
					contado, new Predicate(){
						public boolean evaluate(Object o) {
							Hfactura h = (Hfactura)o;
							return h.getNodoco() == 0 && h.
								getTipodoco().trim().compareTo("") == 0 ;
						}
					}
				); 
			devcontado = (ArrayList<Hfactura>)CollectionUtils
							.subtract(contado, faccontado);
			
			faccredito = (ArrayList<Hfactura>)CollectionUtils.select(
					credito, new Predicate(){
					public boolean evaluate(Object o) {
						Hfactura h = (Hfactura)o;
						return h.getNodoco() == 0 && h.getTipodoco()
								.trim().compareTo("") == 0 ;
					}
				}
			); 
			devcredito = (ArrayList<Hfactura>)CollectionUtils
								.subtract(credito, faccredito);
			
			BigDecimal totalFacCo = MenuDAO.sumarTotalesFactura(faccontado);
			BigDecimal totalDevCo = MenuDAO.sumarTotalesFactura(devcontado);
			BigDecimal totalFacCr = MenuDAO.sumarTotalesFactura(faccredito);
			BigDecimal totalDevCr = MenuDAO.sumarTotalesFactura(devcredito);
			BigDecimal totalVentas = totalFacCo.add(totalFacCr);
			BigDecimal totalDevols = totalDevCo.add(totalDevCr);	
			
			
			m.put("lstHfacturasDiario", lstHfacturasDiario);
			m.put("dTotalVentas", totalVentas.doubleValue());
			m.put("dTotalDevoluciones", totalDevols.doubleValue());
			m.put("dTotalIngresos", totalVentas.subtract(
					totalDevols).doubleValue());
			
			m.put("dTotalFacContado", totalFacCo.doubleValue());
			m.put("dTotalContadoDev", totalDevCo.doubleValue());
			m.put("dTotalIngresoCont", totalFacCo.subtract(
					totalDevCo).doubleValue());
			
			m.put("dTotalFacCredito", totalFacCr);
			m.put("dTotalCreditoDev", totalDevCr);
			m.put("dTotalIngresoCred",  totalFacCr.subtract(
					totalDevCr).doubleValue());
			
			m.put("iCantFactContado", faccontado.size());
			m.put("iCantDevContado", devcontado.size());
			m.put("iCantFactCredito", faccredito.size());
			m.put("iCantDevCredito", devcredito.size());
			
		}catch(Exception ex){ 
			ex.printStackTrace();
		}finally{
			
			if(lstHfacturasDiario == null){
				lstHfacturasDiario = new ArrayList<Hfactura>();
				m.put("lstHfacturasDiario", lstHfacturasDiario);
			}
			
			sMensaje = lstHfacturasDiario.size()+ " resultados";
			gvHfacturasDiario.setPageIndex(0);
			gvHfacturasDiario.dataBind();
			
		}
	}
/*************************************************************************************************************/
/***********************VALUECHANGELISTENER DE CAMBIO DE MONEDA EN EL FILTRO DE MONEDA**************************/
	public void onFiltrosChange(ValueChangeEvent ev){
		String strParametro = null;
		int iFechaActual = 0;
		FacturaCrtl facCtrl = new FacturaCrtl();
		String sMoneda = "", sEstado = "",sCompania = "",sDesde,sHasta;
		List lstFacs,lstDevs;
		Hfactura hFac = null;
		double dTotalVentas = 0.0, dTotalDevoluciones = 0.0, dTotalIngresos = 0.0,dTotalFacContado = 0.0, dTotalFacCredito = 0.0,
		dTotalContadoDev = 0.0, dTotalCreditoDev = 0.0, dTotalIngresoCont = 0.0, dTotalIngresoCred = 0.0;
		int iCantFactContado = 0, iCantDevContado = 0,iCantFactCredito = 0, iCantDevCredito = 0;
		Date dDesde = new Date(),dHasta = new Date();
		sMensaje = "";
		try {
			String sMensaje2 = "";
			String[] sTiposDoc = (String[])m.get("sTiposDoc");
			F55ca017[] f55ca017 = (F55ca017[])m.get("f55ca017");
			List lstLocalizaciones = (List)m.get("lstLocalizaciones");
			if(txtParametro.getValue() != null) {
				strParametro = txtParametro.getValue().toString().trim();
				sMoneda = cmbFiltroMonedas.getValue().toString();
				sEstado = cmbFiltroFacturas.getValue().toString();
				sCompania = cmbFiltroCompaniaDiario.getValue().toString();
				
				int busqueda = 1;
				if (m.get("strBusqueda") != null){
					busqueda = Integer.parseInt((String)m.get("strBusqueda"));
				}
				List result = new ArrayList();
				lstHfacturasDiario = new ArrayList();
				List lstCajas = (List)m.get("lstCajas");
				Vf55ca01 f55ca01 = ((Vf55ca01)lstCajas.get(0));
				String sCodSuc = f55ca01.getId().getCaco().substring(3,5);
				
				//fechas de factura
				if(dcFechaDesde.getValue()!=null){
					sDesde = dcFechaDesde.getValue().toString();
					if(!sDesde.trim().equals(""))
						dDesde = (Date)(dcFechaDesde.getValue());
				}
				if(dcFechaHasta.getValue()!=null){
					sHasta = dcFechaHasta.getValue().toString();
					if(!sHasta.trim().equals(""))
						dHasta = (Date)(dcFechaHasta.getValue());
				}
			
				result = facCtrl.buscarAllFacturas(strParametro,sTiposDoc,sMoneda,busqueda,f55ca017,sEstado,lstLocalizaciones,sCompania,dDesde,dHasta);
					
				if (result == null || result.isEmpty()){
					sMensaje2 = "No se encontraron resultados";
					getSMensaje();
					m.put("mostrarDiario", "m");
					m.put("lstHfacturasDiario",result);
					m.put("lstHDevolucionesDiario", result);
					//resumen de facturas y devoluciones		
					m.put("dTotalVentas", 0.00);
					m.put("dTotalDevoluciones", 0.00);
					m.put("dTotalIngresos", 0.00);
					
					m.put("dTotalFacContado", 0.00);
					m.put("dTotalContadoDev", 0.00);
					m.put("dTotalIngresoCont", 0.00);
					
					m.put("dTotalFacCredito", 0.00);
					m.put("dTotalCreditoDev", 0.00);
					m.put("dTotalIngresoCred", 0.00);
					
					m.put("iCantFactContado", 0);
					m.put("iCantDevContado", 0);
					m.put("iCantFactCredito", 0);
					m.put("iCantDevCredito", 0);
				}else{
					if(m.get("mostrarDiario") != null){
						m.remove("mostrarDiario");
					}
					lstHfacturasDiario = facCtrl.formatFactura(result);
					//
					lstFacs = new ArrayList();
					lstDevs = new ArrayList();
					for(int i = 0;i < lstHfacturasDiario.size();i++){
						hFac = (Hfactura)lstHfacturasDiario.get(i);
						//factura contado
						if(hFac.getNodoco() == 0 && hFac.getTipodoco().trim().equals("") && !hFac.getEstado().trim().equals("A") && (hFac.getTipopago().trim().equals("01")||hFac.getTipopago().trim().equals("00")||hFac.getTipopago().trim().equals("001"))){//factura
							lstFacs.add(hFac);
							dTotalVentas = dTotalVentas + hFac.getTotal();
							dTotalFacContado = dTotalFacContado + hFac.getTotal();
							iCantFactContado++;
						//devolucion de contado
						}else if (!hFac.getEstado().trim().equals("A") && (hFac.getTipopago().trim().equals("01")||hFac.getTipopago().trim().equals("00")||hFac.getTipopago().trim().equals("001"))){
							lstDevs.add(hFac);
							dTotalDevoluciones = dTotalDevoluciones + hFac.getTotal();
							dTotalContadoDev = dTotalContadoDev + hFac.getTotal();
							iCantDevContado++;
						//factura de credito
						}else if(hFac.getNodoco() == 0 && hFac.getTipodoco().trim().equals("") && !hFac.getEstado().trim().equals("A") && (!hFac.getTipopago().trim().equals("01")||!hFac.getTipopago().trim().equals("00")||!hFac.getTipopago().trim().equals("001"))){
							lstFacs.add(hFac);
							dTotalVentas = dTotalVentas + hFac.getTotal();
							dTotalFacCredito = dTotalFacCredito + hFac.getTotal();
							iCantFactCredito++;
						//devolucion de credito
						}else if (!hFac.getEstado().trim().equals("A") && (!hFac.getTipopago().trim().equals("01")||!hFac.getTipopago().trim().equals("00")||!hFac.getTipopago().trim().equals("001"))){
							lstDevs.add(hFac);
							dTotalDevoluciones = dTotalDevoluciones + hFac.getTotal();
							dTotalCreditoDev = dTotalCreditoDev + hFac.getTotal();
							iCantDevCredito++;
						}
						//factura contado anulada
						else if(hFac.getNodoco() == 0 && hFac.getTipodoco().trim().equals("") && hFac.getEstado().trim().equals("A") && (hFac.getTipopago().trim().equals("01")||hFac.getTipopago().trim().equals("00")||hFac.getTipopago().trim().equals("001"))){//factura
							lstFacs.add(hFac);
							dTotalVentas = dTotalVentas + hFac.getTotal();
							dTotalFacContado = dTotalFacContado + hFac.getTotal();
							iCantFactContado++;
						//devolucion de contado anulada
						}else if (hFac.getEstado().trim().equals("A") && (hFac.getTipopago().trim().equals("01")||hFac.getTipopago().trim().equals("00")||hFac.getTipopago().trim().equals("001"))){
							lstDevs.add(hFac);
							dTotalDevoluciones = dTotalDevoluciones + hFac.getTotal();
							dTotalContadoDev = dTotalContadoDev + hFac.getTotal();
							iCantDevContado++;
						//factura de credito anulada
						}else if(hFac.getNodoco() == 0 && hFac.getTipodoco().trim().equals("") && hFac.getEstado().trim().equals("A") && (!hFac.getTipopago().trim().equals("01")||!hFac.getTipopago().trim().equals("00")||!hFac.getTipopago().trim().equals("001"))){
							lstFacs.add(hFac);
							dTotalVentas = dTotalVentas + hFac.getTotal();
							dTotalFacCredito = dTotalFacCredito + hFac.getTotal();
							iCantFactCredito++;
						//devolucion de credito anulada
						}else if (hFac.getEstado().trim().equals("A") && (!hFac.getTipopago().trim().equals("01")||!hFac.getTipopago().trim().equals("00")||!hFac.getTipopago().trim().equals("001"))){
							lstDevs.add(hFac);
							dTotalDevoluciones = dTotalDevoluciones + hFac.getTotal();
							dTotalCreditoDev = dTotalCreditoDev + hFac.getTotal();
							iCantDevCredito++;
						}
					}
					dTotalIngresos = dTotalVentas - dTotalDevoluciones;
					dTotalIngresoCont = dTotalFacContado - dTotalContadoDev; 
					dTotalIngresoCred = dTotalFacCredito - dTotalCreditoDev;
					//lstDevs = facCtrl.formatFactura(lstDevs);
					m.put("lstHfacturasDiario",lstFacs);
					m.put("lstHDevolucionesDiario", lstDevs);
					if(!sMoneda.equals("01")){//si no son todas las monedas 
						//resumen de facturas y devoluciones
						m.put("dTotalVentas", dTotalVentas);
						m.put("dTotalDevoluciones", dTotalDevoluciones);
						m.put("dTotalIngresos", dTotalIngresos);
						
						m.put("dTotalFacContado", dTotalFacContado);
						m.put("dTotalContadoDev", dTotalContadoDev);
						m.put("dTotalIngresoCont", dTotalIngresoCont);
						
						m.put("dTotalFacCredito", dTotalFacCredito);
						m.put("dTotalCreditoDev", dTotalCreditoDev);
						m.put("dTotalIngresoCred", dTotalIngresoCred);
						
						m.put("iCantFactContado", iCantFactContado);
						m.put("iCantDevContado", iCantDevContado);
						m.put("iCantFactCredito", iCantFactCredito);
						m.put("iCantDevCredito", iCantDevCredito);
					}
				}
			}
			gvHfacturasDiario.setPageIndex(0);
			gvHfacturasDiario.dataBind();
			gvHDevolucionesDiario.setPageIndex(0);
			gvHDevolucionesDiario.dataBind();
			m.put("sMensajeDiario", sMensaje2);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
/*********************************************************************************************************************************************/
	public HtmlDropDownList getCmbBusqueda() {
		return cmbBusqueda;
	}
	public void setCmbBusqueda(HtmlDropDownList cmbBusqueda) {
		this.cmbBusqueda = cmbBusqueda;
	}
	public HtmlDropDownList getCmbFiltroMonedas() {
		return cmbFiltroMonedas;
	}
	public void setCmbFiltroMonedas(HtmlDropDownList cmbFiltroMonedas) {
		this.cmbFiltroMonedas = cmbFiltroMonedas;
	}
	public HtmlGridView getGvHfacturasDiario() {
		return gvHfacturasDiario;
	}
	public void setGvHfacturasDiario(HtmlGridView gvHfacturasDiario) {
		this.gvHfacturasDiario = gvHfacturasDiario;
	}
	public List getLstFiltroMonedas() {
		try{
			if (m.get("lstFiltroMonedasDiario") == null){
				//leer companias configuradas en caja
				List lstCajas = (List)m.get("lstCajas");
				CompaniaCtrl compCtrl = new CompaniaCtrl();
				F55ca014[] f55ca014 = null;
				f55ca014 = compCtrl.obtenerCompaniasxCaja(((Vf55ca01)lstCajas.get(0)).getId().getCaid());
				//leer monedas de companias configuradas
				MonedaCtrl monCtrl =  new MonedaCtrl();
				String[] sMonedas = monCtrl.obtenerMonedasxCaja_Companias(((Vf55ca01)lstCajas.get(0)).getId().getCaid(), f55ca014);
				lstFiltroMonedas = new ArrayList();
				lstFiltroMonedas.add(new SelectItem("01","Todas"));
				for(int i = 0; i < sMonedas.length;i++){
					lstFiltroMonedas.add(new SelectItem(sMonedas[i],sMonedas[i]));
				}
				m.put("lstFiltroMonedasDiario", lstFiltroMonedas);
			}else{
				lstFiltroMonedas = (List) m.get("lstFiltroMonedasDiario");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return lstFiltroMonedas;
	}
	public void setLstFiltroMonedas(List lstFiltroMonedas) {
		this.lstFiltroMonedas = lstFiltroMonedas;
	}
	public List getLstHfacturasDiario() {
		try{
			lstHfacturasDiario = new ArrayList();
			lstHfacturasDiario = (List)m.get("lstHfacturasDiario");	
			
			if(m.get("mostrarDiario") != null){
				//gvHfacturasDiario.setStyle("visibility:hidden;height: 105px;display:none");
				//imgWatermark.setStyle("display:inline");
			}else{
				//gvHfacturasDiario.setStyle("visibility:visible;display:inline");
				//imgWatermark.setStyle("display:none");
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return lstHfacturasDiario;
	}
	public void setLstHfacturasDiario(List lstHfacturasDiario) {
		this.lstHfacturasDiario = lstHfacturasDiario;
	}
	public String getSMensaje() {
		if (m.get("sMensajeDiario") != null){
			sMensaje = m.get("sMensajeDiario").toString();
		}
		return sMensaje;
	}
	public void setSMensaje(String sMensaje) {
		sMensaje = sMensaje;
	}
	public HtmlInputText getTxtParametro() {
		return txtParametro;
	}
	public void setTxtParametro(HtmlInputText txtParametro) {
		this.txtParametro = txtParametro;
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
	
	
	public HtmlDropDownList getCmbMonedaDetalle() {
		return cmbMonedaDetalle;
	}
	public void setCmbMonedaDetalle(HtmlDropDownList cmbMonedaDetalle) {
		this.cmbMonedaDetalle = cmbMonedaDetalle;
	}
	
	public HtmlDialogWindow getDgwDetalleDiario() {
		return dgwDetalleDiario;
	}
	public void setDgwDetalleDiario(HtmlDialogWindow dgwDetalleDiario) {
		this.dgwDetalleDiario = dgwDetalleDiario;
	}
	public GridView getGvDfacturasDiario() {
		return gvDfacturasDiario;
	}
	public void setGvDfacturasDiario(GridView gvDfacturasDiario) {
		this.gvDfacturasDiario = gvDfacturasDiario;
	}
	public List getLstDfacturasDiario() {
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		Object o = m.get("bapp3");
		if(o == null){	
			lstDfacturasDiario = new ArrayList();
			m.put("lstDfacturasDiario",lstDfacturasDiario);
			m.put("bapp3", "y");
		}else {
			lstDfacturasDiario = (List)m.get("lstDfacturasDiario");
		}
		return lstDfacturasDiario;
	}
	public void setLstDfacturasDiario(List lstDfacturasDiario) {
		this.lstDfacturasDiario = lstDfacturasDiario;
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
	public double getTxtIva() {
		return txtIva;
	}
	public void setTxtIva(double txtIva) {
		this.txtIva = txtIva;
	}
	public String getTxtNofactura() {
		return txtNofactura;
	}
	public void setTxtNofactura(String txtNofactura) {
		this.txtNofactura = txtNofactura;
	}
	public double getTxtSubtotal() {
		return txtSubtotal;
	}
	public void setTxtSubtotal(double txtSubtotal) {
		this.txtSubtotal = txtSubtotal;
	}
	public String getTxtTotal() {
		return txtTotal;
	}
	public void setTxtTotal(String txtTotal) {
		this.txtTotal = txtTotal;
	}
	public String getTxtUnineg() {
		return txtUnineg;
	}
	public void setTxtUnineg(String txtUnineg) {
		this.txtUnineg = txtUnineg;
	}
	public HtmlOutputText getLblMonedaDomesticaCon() {
		return lblMonedaDomesticaCon;
	}
	public void setLblMonedaDomesticaCon(HtmlOutputText lblMonedaDomesticaCon) {
		this.lblMonedaDomesticaCon = lblMonedaDomesticaCon;
	}
	public HtmlOutputText getLblTasaDetalle() {
		return lblTasaDetalle;
	}
	public void setLblTasaDetalle(HtmlOutputText lblTasaDetalle) {
		this.lblTasaDetalle = lblTasaDetalle;
	}
	public HtmlOutputText getTxtTasaDetalle() {
		return txtTasaDetalle;
	}
	public void setTxtTasaDetalle(HtmlOutputText txtTasaDetalle) {
		this.txtTasaDetalle = txtTasaDetalle;
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
			lstFiltroFacturas.add(new SelectItem("01","Todas"));
			lstFiltroFacturas.add(new SelectItem("02","No Anuladas"));
			lstFiltroFacturas.add(new SelectItem("03","Anuladas"));
		}
		return lstFiltroFacturas;
	}
	public void setLstFiltroFacturas(List lstFiltroFacturas) {
		this.lstFiltroFacturas = lstFiltroFacturas;
	}
	public String getTxtCodUnineg() {
		return txtCodUnineg;
	}
	public void setTxtCodUnineg(String txtCodUnineg) {
		this.txtCodUnineg = txtCodUnineg;
	}
	public HtmlGraphicImageEx getImgWatermark() {
		return imgWatermark;
	}
	public void setImgWatermark(HtmlGraphicImageEx imgWatermark) {
		this.imgWatermark = imgWatermark;
	}
	public HtmlGridView getGvHDevolucionesDiario() {
		return gvHDevolucionesDiario;
	}
	public void setGvHDevolucionesDiario(HtmlGridView gvHDevolucionesDiario) {
		this.gvHDevolucionesDiario = gvHDevolucionesDiario;
	}
	public List getLstHDevolucionesDiario() {
		try{
			lstHDevolucionesDiario = new ArrayList();
			lstHDevolucionesDiario = (List)m.get("lstHDevolucionesDiario");	
 
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return lstHDevolucionesDiario;
	}
	public void setLstHDevolucionesDiario(List lstHDevolucionesDiario) {
		this.lstHDevolucionesDiario = lstHDevolucionesDiario;
	}
	public double getTotalDevoluciones() {
		try{
			totalDevoluciones  = Double.parseDouble( m.get("dTotalDevoluciones").toString());
		}catch(Exception ex){
			
		}
		return totalDevoluciones;
	}
	public void setTotalDevoluciones(double totalDevoluciones) {
		totalDevoluciones = totalDevoluciones;
	}
	public double getTotalIngresos() {
		try{
			totalIngresos  = Double.parseDouble( m.get("dTotalIngresos").toString());
		}catch(Exception ex){
			
		}
		return totalIngresos;
	}
	public void setTotalIngresos(double totalIngresos) {
		totalIngresos = totalIngresos;
	}
	public double getTotalVentas() {
		try{
			totalVentas  = Double.parseDouble( m.get("dTotalVentas").toString());
		}catch(Exception ex){
			
		}
		return totalVentas;
	}
	public void setTotalVentas(double totalVentas) {
		totalVentas = totalVentas;
	}
	public int getCantDevContado() {
		try{
			cantDevContado  = Integer.parseInt( m.get("iCantDevContado").toString());
		}catch(Exception ex){
			
		}
		return cantDevContado;
	}
	public void setCantDevContado(int cantDevContado) {
		this.cantDevContado = cantDevContado;
	}
	public int getCantDevCredito() {
		try{
			cantDevCredito  = Integer.parseInt( m.get("iCantDevCredito").toString());
		}catch(Exception ex){
			
		}
		return cantDevCredito;
	}
	public void setCantDevCredito(int cantDevCredito) {
		this.cantDevCredito = cantDevCredito;
	}
	public int getCantFactContado() {
		try{
			cantFactContado  = Integer.parseInt( m.get("iCantFactContado").toString());
		}catch(Exception ex){
			
		}
		return cantFactContado;
	}
	public void setCantFactContado(int cantFactContado) {
		this.cantFactContado = cantFactContado;
	}
	public int getCantFactCredito() {
		try{
			cantFactCredito  = Integer.parseInt( m.get("iCantFactCredito").toString());
		}catch(Exception ex){
			
		}
		return cantFactCredito;
	}
	public void setCantFactCredito(int cantFactCredito) {
		this.cantFactCredito = cantFactCredito;
	}
	public String getLblVendedorCont() {
		return lblVendedorCont;
	}
	public void setLblVendedorCont(String lblVendedorCont) {
		this.lblVendedorCont = lblVendedorCont;
	}
	public String getTxtVendedorCont() {
		return txtVendedorCont;
	}
	public void setTxtVendedorCont(String txtVendedorCont) {
		this.txtVendedorCont = txtVendedorCont;
	}
	public double getTotalContadoDev() {
		try{
			totalContadoDev  = Double.parseDouble( m.get("dTotalContadoDev").toString());
		}catch(Exception ex){
			
		}
		return totalContadoDev;
	}
	public void setTotalContadoDev(double totalContadoDev) {
		this.totalContadoDev = totalContadoDev;
	}
	public double getTotalCreditoDev() {
		try{
			totalCreditoDev  = Double.parseDouble( m.get("dTotalCreditoDev").toString());
		}catch(Exception ex){
			
		}
		return totalCreditoDev;
	}
	public void setTotalCreditoDev(double totalCreditoDev) {
		this.totalCreditoDev = totalCreditoDev;
	}
	public double getTotalFacContado() {
		try{
			totalFacContado  = Double.parseDouble( m.get("dTotalFacContado").toString());
		}catch(Exception ex){
			
		}
		return totalFacContado;
	}
	public void setTotalFacContado(double totalFacContado) {
		this.totalFacContado = totalFacContado;
	}
	public double getTotalFacCredito() {
		try{
			totalFacCredito  = Double.parseDouble( m.get("dTotalFacCredito").toString());
		}catch(Exception ex){
			
		}
		return totalFacCredito;
	}
	public void setTotalFacCredito(double totalFacCredito) {
		this.totalFacCredito = totalFacCredito;
	}
	public double getTotalIngresoCont() {
		try{
			totalIngresoCont  = Double.parseDouble( m.get("dTotalIngresoCont").toString());
		}catch(Exception ex){
			
		}
		return totalIngresoCont;
	}
	public void setTotalIngresoCont(double totalIngresoCont) {
		this.totalIngresoCont = totalIngresoCont;
	}
	public double getTotalIngresoCred() {
		try{
			totalIngresoCred  = Double.parseDouble( m.get("dTotalIngresoCred").toString());
		}catch(Exception ex){
			
		}
		return totalIngresoCred;
	}
	public void setTotalIngresoCred(double totalIngresoCred) {
		this.totalIngresoCred = totalIngresoCred;
	}
	public HtmlDropDownList getCmbFiltroCompaniaDiario() {
		return cmbFiltroCompaniaDiario;
	}
	public void setCmbFiltroCompaniaDiario(HtmlDropDownList cmbFiltroCompaniaDiario) {
		this.cmbFiltroCompaniaDiario = cmbFiltroCompaniaDiario;
	}
	public List getLstFiltroCompaniaDiario() {
		try{
			if(m.get("lstFiltroCompaniaDiario")==null){
				CompaniaCtrl cc = new CompaniaCtrl();
				List lstcaja = (ArrayList)m.get("lstCajas");
				Vf55ca01 caja = (Vf55ca01)lstcaja.get(0);			
				int sCajaId = caja.getId().getCaid();
				
				List lstFiltro = new ArrayList();
				F55ca014[] lstComxCaja = cc.obtenerCompaniasxCaja(sCajaId);			
				
				lstFiltro.add(new SelectItem("01","Todas","Seleccione una Compañía"));
				for(int i=0; i<lstComxCaja.length;i++){				
					lstFiltro.add(new SelectItem(lstComxCaja[i].getId().getC4rp01(),lstComxCaja[i].getId().getC4rp01d1().trim(),lstComxCaja[i].getId().getC4rp01()));
				}
				m.put("lstFiltroCompaniaDiario", lstFiltro);	
				lstFiltroCompaniaDiario = lstFiltro;
			}else{
				lstFiltroCompaniaDiario = (List)m.get("lstFiltroCompaniaDiario");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return lstFiltroCompaniaDiario;
	}
	public void setLstFiltroCompaniaDiario(List lstFiltroCompaniaDiario) {
		this.lstFiltroCompaniaDiario = lstFiltroCompaniaDiario;
	}
	public HtmlDateChooser getDcFechaDesde() {
		return dcFechaDesde;
	}
	public void setDcFechaDesde(HtmlDateChooser dcFechaDesde) {
		this.dcFechaDesde = dcFechaDesde;
	}
	public HtmlDateChooser getDcFechaHasta() {
		return dcFechaHasta;
	}
	public void setDcFechaHasta(HtmlDateChooser dcFechaHasta) {
		this.dcFechaHasta = dcFechaHasta;
	}

	public String getTxtNoFacturaOriginal() {
		return txtNoFacturaOriginal;
	}

	public void setTxtNoFacturaOriginal(String txtNoFacturaOriginal) {
		this.txtNoFacturaOriginal = txtNoFacturaOriginal;
	}

	public String getTxtTipoFacturaOriginal() {
		return txtTipoFacturaOriginal;
	}

	public void setTxtTipoFacturaOriginal(String txtTipoFacturaOriginal) {
		this.txtTipoFacturaOriginal = txtTipoFacturaOriginal;
	}

	public String getTxtFechaFactOriginal() {
		return txtFechaFactOriginal;
	}

	public void setTxtFechaFactOriginal(String txtFechaFactOriginal) {
		this.txtFechaFactOriginal = txtFechaFactOriginal;
	}

	public String getTxtMontoFactOriginal() {
		return txtMontoFactOriginal;
	}

	public void setTxtMontoFactOriginal(String txtMontoFactOriginal) {
		this.txtMontoFactOriginal = txtMontoFactOriginal;
	}

	public String getTxtMontoEquivFctOriginal() {
		return txtMontoEquivFctOriginal;
	}

	public void setTxtMontoEquivFctOriginal(String txtMontoEquivFctOriginal) {
		this.txtMontoEquivFctOriginal = txtMontoEquivFctOriginal;
	}

	public boolean isCargardevolucion() {
		return cargardevolucion;
	}

	public void setCargardevolucion(boolean cargardevolucion) {
		this.cargardevolucion = cargardevolucion;
	}
	
}

package com.casapellas.navegacion;
/**
 * CASA PELLAS S.A.
 * Creado por.........: Juan Carlos Ñamendi Pineda
 * Fecha de Creación..: 06/01/2009
 * Última modificación: 17/02/2010
 * Modificado por.....:	Juan Carlos Ñamendi Pineda.
 * Modificación.......: Se agrego la navegacion de financiamientof
 * 
 */
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import com.casapellas.controles.ArqueoCtrl;
import com.casapellas.controles.ClsConfiguracionMensaje;
import com.casapellas.controles.CtrlCajas;
import com.casapellas.controles.FacturaCrtl;
import com.casapellas.controles.ReciboCtrl;
import com.casapellas.controles.ReintegroCtrl;
import com.casapellas.controles.tmp.CompaniaCtrl;
import com.casapellas.controles.tmp.FacturaContadoCtrl;
import com.casapellas.controles.tmp.TasaCambioCtrl;
import com.casapellas.entidades.ConfiguracionMensaje;
import com.casapellas.entidades.Df4211;
import com.casapellas.entidades.Dfactjdecon;
import com.casapellas.entidades.Dfactura;
import com.casapellas.entidades.F55ca014;
import com.casapellas.entidades.F55ca017;
import com.casapellas.entidades.Hf4211;
import com.casapellas.entidades.Hfactjdecon;
import com.casapellas.entidades.Hfactura;
import com.casapellas.entidades.Recibo;
import com.casapellas.entidades.Tcambio;
import com.casapellas.entidades.Vhfactura;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.entidades.ens.Vautoriz;
import com.google.gson.Gson;
import com.casapellas.entidades.Vf55ca01;
import com.casapellas.util.Divisas;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.JsfUtil;
import com.casapellas.util.JulianToCalendar;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.PropertiesSystem;
import com.infragistics.faces.menu.component.MenuItem;
import com.infragistics.faces.window.component.html.HtmlDialogWindow;

public class MenuDAO {
	
	private List menuItems = null;
	private String strDisplayInicio;
	
	private String strstdProc;
//	private String strMensajeValidacion;
	private HtmlDialogWindow dwProcesa;
//	private HtmlOutputText lblMensajeValidacion;
	
	/**
	 * Programmatically create a list of menu items
	 *
	 */
	private List createMenuItems() {
		List items = new ArrayList();	
		MenuItemBean cat = null;
		String sCodSup = null;
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();	
		try{
			Vautoriz[] secciones = (Vautoriz[])m.get("sevAut");
			for (int i = 0; i < secciones.length; i++) {
				if ( secciones[i].getId().getCodsec().equals(secciones[i].getId().getCodsuper()) && secciones[i].getId().getEnmenu().equals("1")){
					//Agregar secciones principales
					if (secciones[i].getId().getIconurl() != null && !(secciones[i].getId().getIconurl()).trim().equals(""))
						cat = new MenuItemBean(secciones[i].getId().getNomsec(),secciones[i].getId().getIconurl());
					else 
						cat = new MenuItemBean(secciones[i].getId().getNomsec());
					
					sCodSup = secciones[i].getId().getCodsec();
					crearItemsInferiores(sCodSup, cat);
					items.add(cat);
				}
			}
			//Se comenta por que se agrega en otro sitio el salir para coinsidir con la interfaz de angualar 
			//Byron Canales 20211110
			/*MenuItemBean miSalir = new MenuItemBean("Salir");
			items.add(miSalir);*/
		}catch(Exception ex){
			System.out.print("=>Excepcion capturada en createMenuItems: " + ex);
		}
		return items;				
	}
/***********CREAR INFERIORES*****************************/
	public void crearItemsInferiores(String sCodSup, MenuItemBean cat){
		MenuItemBean catInferior = null;
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();	
		
		try{
			Vautoriz[] secciones = (Vautoriz[])m.get("sevAut");
			for (int i = 0; i < secciones.length; i++) {
				//String s1 =  secciones[i].getCodigoSuperior();
				String s2 = sCodSup;
				if ( secciones[i].getId().getCodsuper().equals(sCodSup) && secciones[i].getId().getEnmenu().equals("1")&&!secciones[i].getId().getCodsec().equals(secciones[i].getId().getCodsuper())){
					if (secciones[i].getId().getIconurl() != null && !(secciones[i].getId().getIconurl()).trim().equals(""))
						catInferior = new MenuItemBean(secciones[i].getId().getNomsec(),secciones[i].getId().getIconurl());
					else
						catInferior = new MenuItemBean(secciones[i].getId().getNomsec());
					
					sCodSup = secciones[i].getId().getCodsec();
					
					crearItemsInferiores(sCodSup, catInferior);
					sCodSup = s2;
					cat.getMenuItems().add(catInferior);
				}
			}
		}catch(Exception ex){
			System.out.print("=>Exception capturada en crearItemsInferiores:" + ex);
		}
	}
	/**
	 * output component which displays the last item that was clicked-on
	 * This component can be used here since the binding property has been set in the JSF page 
	 */
	private String lastItem = "None";

	public String getLastItem() {
		return lastItem;
	}
	
	/**
	 * update the label of the component that displays the last item that was clicked-on
	 * @param event
	 */
	@SuppressWarnings("unchecked")
	public void onItemClick(ActionEvent event) {
		try{
			//forzar el garbage collector
			System.runFinalization();
			System.gc();
			//
			FacesContext fcMenu = FacesContext.getCurrentInstance();
			NavigationHandler nhMenu = fcMenu.getApplication().getNavigationHandler();
			Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();	
			MenuItem item = (MenuItem) event.getSource();
			FacesContext fc = FacesContext.getCurrentInstance();
			NavigationHandler nh = fc.getApplication().getNavigationHandler();
			
			
			HttpServletResponse response = (HttpServletResponse) 
			fc.getExternalContext().getResponse();
			response.setContentType("text/html; charset=UTF-8");
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Cache-Control", "no-store");
			response.setHeader("Cache-Control", "must-revalidate");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
			
			limpiarSesionNavegacion(); //--- Limpiar las variables en sesion de las secciones visitadas.
			
			if (item!=null) {
				Vautoriz[] secciones = (Vautoriz[])m.get("sevAut");
				lastItem = item.getValue().toString();
				
				for (int i = 0; i < secciones.length;i++){
					
					if(lastItem.equals(secciones[i].getId().getNomsec())){
						
						
						//&& ========  ir a la seccion de anticipos por PMT 
						if(secciones[i].getId().getCodsec().compareTo("S000000462") == 0){
							FacesContext.getCurrentInstance().getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/recibos/AnticiposPMT.faces");
							break;
						}
						
						//&& ========  ir a la seccion de anticipos por PMT 
						if(secciones[i].getId().getCodsec().compareTo("S000000463") == 0){
							FacesContext.getCurrentInstance().getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/recibos/DebitosAutomaticosPMT.faces");
							break;
						}
						
						//&& ======== Consolidado de Confirmacion de depositos.
						if(secciones[i].getId().getCodsec().compareTo("S000000367") == 0){
							fc.getExternalContext().redirect(
									"/" + PropertiesSystem.CONTEXT_NAME
											+ "/Confirmacion/ConsolidadoDepositosBanco.faces");
							break;
						}
						
						//&& ======== Consolidado de Confirmacion de depositos.
						if(secciones[i].getId().getCodsec().compareTo("S000000333") == 0 ){		
							fc.getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/Confirmacion/ExcepcionConfirmaDepositos.faces");
							break;
						}
						
						
						//&& ======== Seccion de transacciones del socket pos
						if(secciones[i].getId().getCodsec().equals("S000000321")){
							limpiarMapaSesion(m,"tsp_");   
							fc.getExternalContext().redirect(
									"/" + PropertiesSystem.CONTEXT_NAME
											+ "/cierre/TransaccionesSocketPos.faces");
							break;
						}
						else
						if(secciones[i].getId().getCodsec().equals("S000000013")){
							
							m.remove("lstHfacturasContado");
							m.remove("strBusqueda");
							m.remove("lstConfiguracionMensaje");
							
							new com.casapellas.controles.tmp.FacturaCrtl().listarFacturasContadoDelDiaTmp();
							
							//Mejora hecho por lfonseca
							//2021-01-05
							try
							{
								String strResultadoConfiguracionMensaje = 
										ClsConfiguracionMensaje.getConfiguracionMensajeList("CAJA", 
												"REC_CONTADO", null, null).getDataAsString();
								
								Gson gson = new Gson();
	
								ConfiguracionMensaje[] lstConfiguracionMensaje = gson.fromJson(strResultadoConfiguracionMensaje, ConfiguracionMensaje[].class);
								m.put("lstConfiguracionMensaje",lstConfiguracionMensaje);

							}
							catch (Exception e) {
								// TODO: handle exception
								ConfiguracionMensaje[] lstConfiguracionMensaje = null;
								m.put("lstConfiguracionMensaje",lstConfiguracionMensaje);
							}
							//-----------------------------------
							
							fc.getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/recibos/pagoFacContado3.faces");
							break;
							
						}else if(secciones[i].getId().getCodsec().equals("S000000014")){
							
							m.remove("lstHfacturasCredito");
							m.remove("strBusquedaCredito");
							fc.getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/recibos/pagoFacCredito2.faces");
							break;
							
						}else if(secciones[i].getId().getCodsec().equals("S000000015")){
							fc.getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/recibos/pagoPrimaReserva2.faces");
							break;
						}else if(secciones[i].getId().getCodsec().equals("S000000032")){
							
							m.remove("lstHfacturasDiario");
							m.remove("lstHDevolucionesDiario");
							irDiaria();
							fc.getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/recibos/factDiaria.faces");
							break;
							
						}else if(secciones[i].getId().getCodsec().equals("S000000043")){							
							fc.getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/cierre/arqueoCaja.faces");
							
							break;
						}else if(secciones[i].getId().getCodsec().equals("S000000044")){							
							cargarArqueosPendientes();
							
							fc.getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/cierre/revisionArqueo.faces");
							break;
						}
						else if(secciones[i].getId().getCodsec().equals("S000000048")){
							fc.getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/reportes/rptmcaja003.faces"); //nh.handleNavigation(fc, "onItemClick", "rptmcaja003");
							break;
						} 
						else if(secciones[i].getId().getCodsec().equals("S000000033")){
							limpiarAnular();
							
							int caid = ((ArrayList<Vf55ca01>)(m.get("lstCajas")))
											.get(0).getId().getCaid();
							List<Recibo> lstRecibos =  ReciboCtrl.getRecibosCaja(caid, new Date());
							
							if(lstRecibos == null) 
								lstRecibos = new ArrayList<Recibo>();
							
							m.put("lstRecibos", lstRecibos);

							fc.getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/recibos/anularRecibo.faces");
							
							break;
						}
						else if(secciones[i].getId().getCodsec().equals("S000000049")){
							m.remove("lstCuotas");
							
							fc.getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/recibos/pagoFinanciamiento.faces");
							
							break;
						}else if(secciones[i].getId().getCodsec().equals("S000000050")){
							
						   fc.getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/recibos/pagoIngExtraordinario.faces");
							break;
							
						}else if(secciones[i].getId().getCodsec().equals("S000000051")){
							m.put("sp_SugerenciaSalida", "S");//Bandera para indicar en SugerenciasPrimas que esta llamada desde salida
							fc.getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/recibos/solicitudsalida.faces");
							break;
							
						}else if(secciones[i].getId().getCodsec().equals("S000000052")){
							fc.getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/recibos/aprobacionsalida.faces");
							break;
						}else if(secciones[i].getId().getCodsec().equals("S000000053")){
							obtenerArqueosCaja();
							fc.getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/reportes/detalleArqueoCaja.faces");
							
							break;
						}
						else if(secciones[i].getId().getCodsec().equals("S000000054")){
							FacesContext.getCurrentInstance().getExternalContext().redirect("/"+ PropertiesSystem.CONTEXT_NAME + "/reportes/rptmcaja005.faces");//nh.handleNavigation(fc, "onItemClick", "rptmcaja005");S000000054
							break;
						}
						else if(secciones[i].getId().getCodsec().equals("S000000055")){
							FacesContext.getCurrentInstance().getExternalContext().redirect("/"+ PropertiesSystem.CONTEXT_NAME + "/reportes/rptmcaja006.faces"); //nh.handleNavigation(fc, "onItemClick", "rptmcaja006");
							break;
						}
						else if(secciones[i].getId().getCodsec().equals("S000000056")){
							
							FacesContext.getCurrentInstance().getExternalContext().redirect("/"+ PropertiesSystem.CONTEXT_NAME + "/reportes/rptmcaja007.faces");
							 
							break;
						}
						else if(secciones[i].getId().getCodsec().equals("S000000060")){
							nh.handleNavigation(fc, "onItemClick", "revsolecheque");
							fc.getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/cierre/revSolicitudCheques.faces");
							break;
						}
						else if(secciones[i].getId().getCodsec().equals("S000000068")){						
							m.put("strMensajeReciboCN",  "Realizar búsqueda de recibos!!!");
							m.remove("mostrarReciboCN");
							
							fc.getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/consultas/consultaRecibos.faces");
							break;
						}
						else if(secciones[i].getId().getCodsec().equals("S000000070")){
							FacesContext.getCurrentInstance().getExternalContext()
									.redirect("/"+ PropertiesSystem.CONTEXT_NAME
											+ "/reportes/rptmcaja010.faces");
							break;
						}
						else if(secciones[i].getId().getCodsec().equals("S000000161")){
							fc.getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/Confirmacion/CrudEquivalenciaDocs.faces");
							break;
						}
						else if(secciones[i].getId().getCodsec().equals("S000000162")){
							fc.getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/Confirmacion/ConfirmaDepositos.faces");
							break;
						}
						else if(secciones[i].getId().getCodsec().equals("S000000196")){
							fc.getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/Confirmacion/consultaConfirmacion.faces");
							break;
						}
						else if(secciones[i].getId().getCodsec().equals("S000000219")){
							fc.getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/Confirmacion/consultaDepositos.faces");
							break;
						}
						else if(secciones[i].getId().getCodsec().equals("S000000198")){
							nh.handleNavigation(fc, "onItemClick", "ligarDocs");
							break;
						}
						else if(secciones[i].getId().getCodsec().equals("S000000201")){
							m.remove("liga_lstHfacturasCredito");
							nh.handleNavigation(fc, "onItemClick", "anularLiga");
							break;
						}
						else if(secciones[i].getId().getCodsec().equals("S000000285")){		
							m.put("lstReintegro",new ReintegroCtrl().getReintegrosxParametro(0,"","01"));
							fc.getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/cierre/fondoMin.faces");
							break;
						}
						else if(secciones[i].getId().getCodsec().equals("S000000286")){		
							fc.getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/Confirmacion/ConsultaCtaTransito.faces");
							break;
						}
						else if(secciones[i].getId().getCodsec().equals("S000000533")){
							//m.remove("lstCuotas");
							
							fc.getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/recibos/pagoFinanciamientoV2.faces");
							
							break;
						}
						//Menú ESQUELA
						else if(secciones[i].getId().getCodsec().equals("S000000530")){
							Vautoriz[] vAut = (Vautoriz[]) m.get("sevAut");
							String url = secciones[i].getId().getUrl() + "&id=" + vAut[0].getId().getLogin().toString();
							System.out.println("Redirige a: " + "/"+PropertiesSystem.CONTEXT_NAME + '/' + url);
							fc.getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME + '/' + url);
							break;
						}
						else if(secciones[i].getId().getCodsec().equals("S000000531")){
							Vautoriz[] vAut = (Vautoriz[]) m.get("sevAut");
							String url = secciones[i].getId().getUrl() + "&id=" + vAut[0].getId().getLogin().toString();
							System.out.println("Redirige a: " + "/"+PropertiesSystem.CONTEXT_NAME + '/' + url);
							fc.getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME + '/' + url);
							break;
						}
						else if(secciones[i].getId().getCodsec().equals("S000000532")){
							Vautoriz[] vAut = (Vautoriz[]) m.get("sevAut");
							String url = secciones[i].getId().getUrl() + "&id=" + vAut[0].getId().getLogin().toString();
							System.out.println("Redirige a: " + "/"+PropertiesSystem.CONTEXT_NAME + '/' + url);
							fc.getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME + '/' + url);
							break;
						}
						else if(secciones[i].getId().getCodsec().equals("S000000547")){
							Vautoriz[] vAut = (Vautoriz[]) m.get("sevAut");
							String url = secciones[i].getId().getUrl() + "&id=" + vAut[0].getId().getLogin().toString();
							System.out.println("Redirige a: " + "/"+PropertiesSystem.CONTEXT_NAME + '/' + url);
							fc.getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME + '/' + url);
							break;
						}
					}else if(lastItem.equals("Salir")){
						HttpSession session = (HttpSession) FacesContext
								.getCurrentInstance().getExternalContext()
								.getSession(false);
						
						if(session == null ) return;
						
						Set s  = m.keySet();
						s.clear();	
						s = m.entrySet();
						s.clear();
						session.invalidate();
						System.runFinalization();
						System.gc();	
						
						fc.getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/login.faces");
						
						break;
					}
				}
			}
		}catch(Exception ex){
			System.out.print("=>Se capturo una Excepcion en MenuDAO.onItemClick: " + ex);
		}
	}
	
	public void navigateAnticiposPMT(ActionEvent ev){
		try {
		
			limpiarSesionNavegacion();
			
			HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
			response.setContentType("text/html; charset=UTF-8");
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Cache-Control", "no-store");
			response.setHeader("Cache-Control", "must-revalidate");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
			
			FacesContext.getCurrentInstance().getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/recibos/AnticiposPMT.faces");
			
		} catch (Exception e) {
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
		}
	}
	
	public void navigateEsquela(ActionEvent ev){
		try {
		
			limpiarSesionNavegacion();
			
			HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
			response.setContentType("text/html; charset=UTF-8");
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Cache-Control", "no-store");
			response.setHeader("Cache-Control", "must-revalidate");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
			Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();	

			Vautoriz[] secciones = (Vautoriz[])m.get("sevAut");
			String url = "GCPCAJAANG/index.html?id=" + secciones[0].getId().getLogin().toString() + "&v=numanejo";
			FacesContext.getCurrentInstance().getExternalContext().redirect("/" + PropertiesSystem.CONTEXT_NAME + '/' + url);
			
		} catch (Exception e) {
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
		}
	}
	
	public void navigateDebitosAutomaticosPMT(ActionEvent ev){
		try {
		
			limpiarSesionNavegacion();
			
			HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
			response.setContentType("text/html; charset=UTF-8");
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Cache-Control", "no-store");
			response.setHeader("Cache-Control", "must-revalidate");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
			
			FacesContext.getCurrentInstance().getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/recibos/DebitosAutomaticosPMT.faces");
			
		} catch (Exception e) {
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
		}
	}
	
	public void navigateDonaciones(ActionEvent ev){
		try {
		
			limpiarSesionNavegacion();
			
			HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
			response.setContentType("text/html; charset=UTF-8");
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Cache-Control", "no-store");
			response.setHeader("Cache-Control", "must-revalidate");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
			
			FacesContext.getCurrentInstance().getExternalContext()
				.redirect("/"+PropertiesSystem.CONTEXT_NAME+"/recibos/Donaciones.faces");
			
		} catch (Exception e) {
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
		}
	}
	
	
	public void navigateFinanV2(ActionEvent ev){
		try {
			System.runFinalization();
			System.gc();
			Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();	
			FacesContext fc = FacesContext.getCurrentInstance();
			
			m.remove("lstCuotas");
			limpiarSesionNavegacion();
			
			HttpServletResponse response = (HttpServletResponse) 
			fc.getExternalContext().getResponse();
			response.setContentType("text/html; charset=UTF-8");
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Cache-Control", "no-store");
			response.setHeader("Cache-Control", "must-revalidate");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);

			fc.getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/recibos/pagoFinanciamientoV2.faces");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

/*********************************************************************************************************************************************/	
	@SuppressWarnings("unchecked")
	public void navigateConsultaRecibos(ActionEvent ev){	
		try{
			//forzar el garbage collector
			System.runFinalization();
			System.gc();
			Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();	
			FacesContext fc = FacesContext.getCurrentInstance();

			limpiarSesionNavegacion();
			m.put("strMensajeReciboCN",  "Realizar búsqueda de recibos!!!");
			m.remove("mostrarReciboCN");
			
			//NavigationHandler nh = fc.getApplication().getNavigationHandler();
			//nh.handleNavigation(fc, "navigateConsultaRecibos", "consultarRecibos");
			
			HttpServletResponse response = (HttpServletResponse) 
			fc.getExternalContext().getResponse();
			response.setContentType("text/html; charset=UTF-8");
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Cache-Control", "no-store");
			response.setHeader("Cache-Control", "must-revalidate");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
			fc.getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/consultas/consultaRecibos.faces");
			
		}catch(Exception ex){
			ex.printStackTrace();
		} 
	}
	@SuppressWarnings("rawtypes")
	public void navigateCont(ActionEvent ev){
		try {
			System.runFinalization();
			System.gc();
			Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();	
			FacesContext fcLogin = FacesContext.getCurrentInstance();
			limpiarSesionNavegacion();
			
			m.remove("lstHfacturasContado");
			m.remove("strBusqueda");
			m.remove("lstConfiguracionMensaje");
			
			//&& ====== cargar las facturas del día
//			new com.casapellas.controles.tmp.FacturaCrtl().listarFacturasContadoDelDiaTmp();
//			new com.casapellas.controles.tmp.FacturaCrtl().listarFacturasContadoDelDia ();
			
			loadDataContado();
			
			//Mejora hecho por lfonseca
			//2021-01-05
			
			try
			{
				String strResultadoConfiguracionMensaje = 
						ClsConfiguracionMensaje.getConfiguracionMensajeList("CAJA", 
								"REC_CONTADO", null, null).getDataAsString();
		
				Gson gson = new Gson();
	
				ConfiguracionMensaje[] lstConfiguracionMensaje = gson.fromJson(strResultadoConfiguracionMensaje, ConfiguracionMensaje[].class);
	//			List<ConfiguracionMensaje> lstConfiguracionMensaje = Arrays.asList(lstConfiguracionMensajeTemp); //new ArrayList<ConfiguracionMensaje>();
		
				m.put("lstConfiguracionMensaje",lstConfiguracionMensaje);
			}
			catch (Exception e) {
				// TODO: handle exception
				ConfiguracionMensaje[] lstConfiguracionMensaje = null;
				m.put("lstConfiguracionMensaje",lstConfiguracionMensaje);
			}
			//---------------------------------------
			
			HttpServletResponse response = (HttpServletResponse) 
			fcLogin.getExternalContext().getResponse();
			response.setContentType("text/html; charset=UTF-8");
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Cache-Control", "no-store");
			response.setHeader("Cache-Control", "must-revalidate");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
			fcLogin.getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/recibos/pagoFacContado3.faces");			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//---------------------- INICIO DE CAMBIO DE CODIGO -------------------------------
	
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public void loadDataContado(){
			
			List lstHfacturasContado = new ArrayList();		
			CtrlCajas cajasCtrl = new CtrlCajas();
			boolean bSincroniza = false;
			F55ca017[] f55ca017 =  null;
			try {		
				LogCajaService.CreateLog("loadDataContado", "INFO", "loadDataContado-INICIO");
				
				//verificar si es linea 22,23,24 microtec para realizar sincronizacion			
				List lstCajas = (List) JsfUtil.getObjectFromSessionMap("lstCajas");
				Vf55ca01 f55ca01 = ((Vf55ca01) lstCajas.get(0));
				f55ca017 = cajasCtrl.obtenerUniNegCaja(f55ca01.getId().getCaid(), f55ca01.getId().getCaco());
				JsfUtil.addObjectToSessionMap("f55ca017", f55ca017);
				
				if(f55ca017 == null || f55ca017.length == 0){
					int iFechaHoy = FechasUtil.DateToJulian(new Date());
					int caid = ((ArrayList<Vf55ca01>) JsfUtil.getObjectFromSessionMap("lstCajas"))
									.get(0).getId().getCaid();
					List<Hfactjdecon> lstFacs = new CtrlCajas()
										.leerTraslados(caid, iFechaHoy);
					if (lstFacs != null && !lstFacs.isEmpty())
						lstHfacturasContado = formatFactura(lstFacs);				
					return;
				}
			
			} catch (Exception ex) {
				LogCajaService.CreateLog("loadDataContado", "ERR", ex.getMessage());
				ex.printStackTrace();
			}	
			
			try {
				//&& ======= Verificar si debe sincronizarse o no facturas.
				for (F55ca017 f017 : f55ca017) {
					String sLinea = CompaniaCtrl.leerLineaNegocio(
										f017.getId().getC7mcul());
					if(sLinea == null || sLinea.trim().compareTo("") == 0)
						continue;
					bSincroniza = PropertiesSystem.LNS_JDEDWARDS
										.contains(sLinea.trim()); 
					if(bSincroniza) 
						break;
				}
				if (bSincroniza)
					sincronizarFacturas();
				
				new com.casapellas.controles.tmp.FacturaCrtl().listarFacturasContadoDelDiaTmp();
				
				//&& ==== Verificar que las facturas cargadas en caja se hayan anulado en jde (FDC Microtec).
				if(JsfUtil.sessionMapContainKey("lstHfacturasContado") && ((ArrayList)JsfUtil.getObjectFromSessionMap("lstHfacturasContado")).size() > 0 && bSincroniza){
					new FacturaContadoCtrl().comprobarFacturaActiva(lstHfacturasContado);
				}
				
			} catch (Exception e) {
				LogCajaService.CreateLog("loadDataContado", "ERR", e.getMessage());
				e.printStackTrace();
			}
			
			HibernateUtilPruebaCn.closeSession();
			LogCajaService.CreateLog("loadDataContado", "INFO", "loadDataContado-FIN");
		}
		
		public ArrayList formatFactura(List lstFacturas) {
			Divisas d = new Divisas();
			ArrayList lstResultado = new ArrayList();
			Hfactjdecon[] hFacJde = new Hfactjdecon[lstFacturas.size()];
			Hfactura hFac = null;
			List result = new ArrayList();
			double total = 0, ivaTotal = 0, ivaActual = 0, totalfac = 0, equiv = 0, totalf = 0;
			String sHora = "";
			CompaniaCtrl cCtrl = new CompaniaCtrl();
			String sMonedaBase = "";

			try {
				hFacJde[0] = (Hfactjdecon) lstFacturas.get(0);
				//obtener companias x caja
				sMonedaBase = cCtrl.sacarMonedaBase((F55ca014[]) JsfUtil.getObjectFromSessionMap("cont_f55ca014"),hFacJde[0].getId().getCodcomp());
				
				//&& ============ Tasa de cambio oficial.
				BigDecimal tasafac;
				BigDecimal tasa = BigDecimal.ONE;
				Tcambio[] tcJDE = new TasaCambioCtrl().obtenerTasaCambioJDEdelDia();
				
				if(tcJDE != null){
					for (Tcambio t : tcJDE) {
						if (t.getId().getCxcrcd().compareTo(sMonedaBase) == 0) {
							tasa = t.getId().getCxcrrd();
							break;
						}
					}
				}
				for (int i = 0; i < lstFacturas.size(); i++) {
					hFacJde[i] = (Hfactjdecon) lstFacturas.get(i);
					JulianToCalendar fecha = new JulianToCalendar(hFacJde[i].getId().getFecha());
					JulianToCalendar fechagrab = new JulianToCalendar(hFacJde[i].getId().getFechagrab());
					double subtotal = (hFacJde[i].getId().getSubtotal().doubleValue()) / 100.0;
					String sSubtotal = d.formatDouble(subtotal);
					String sTotal = null;
					String sIvaTotal = null;
					// ****************Anexar Detalle a facturas y formatear*************************/

					Dfactjdecon[] dFacJde = new Dfactjdecon[result.size()];
					Dfactura dFac = null;
					List lstDfactura = new ArrayList();
					total = d.roundDouble4((hFacJde[i].getId().getTotal()) / 100.0);
					totalf = d.roundDouble4((hFacJde[i].getId().getTotalf()) / 100.0);
					ivaTotal = d.roundDouble4((hFacJde[i].getId().getTotal() - hFacJde[i].getId().getSubtotal()) / 100.0);
					double dTotal = total;
					
					//
					sIvaTotal = d.formatDouble(ivaTotal);
					String moneda = hFacJde[i].getId().getMoneda();
					if (moneda.equals(sMonedaBase)) {
						
						equiv = total;
						dTotal = totalf;
						totalf = hFacJde[i].getId().getTotalf();
						
						tasafac = hFacJde[i].getId().getTasa();
						if(tasafac.compareTo(BigDecimal.ZERO) == 0|| 
							tasafac.compareTo(BigDecimal.ONE) == 0)
							tasafac = tasa;
						
						if(dTotal == 0){
							dTotal = d.roundDouble(total / tasafac.doubleValue());
							totalf = dTotal;
						}
						
						
					} else {
						equiv = total;
					}
					totalfac += total;
					JsfUtil.addObjectToSessionMap("mTotalFactura", totalfac);
					
					//------ formatear Hora
					sHora = new Divisas().rellenarCadena(hFacJde[i].getId().getHora().toString(), "0", 6);
					int iLargoHora = sHora.length();
					if (iLargoHora == 6) {
						sHora = sHora.substring(0, 2) + ":" + sHora.substring(2, 4)	+ ":" + sHora.substring(4, 6);
					} else {
						sHora = "0" + sHora.substring(0, 1) + ":" + sHora.substring(1, 3) + ":" + sHora.substring(3, 5);
					}
					
					// establecer tipo de factura
					String sPago = "";
					if (hFacJde[i].getId().getTipopago().trim().equals("00") || hFacJde[i].getId().getTipopago().trim().equals("01") || hFacJde[i].getId().getTipopago().trim().equals("001")) {
						sPago = "Contado";
					} else {
						sPago = "Crédito";
					}

					hFac = new Hfactura(hFacJde[i].getId().getNofactura(),
							hFacJde[i].getId().getTipofactura(), 
							hFacJde[i].getId().getCodcli(), 
							hFacJde[i].getId().getNomcli(),
							hFacJde[i].getId().getCodunineg(), 
							hFacJde[i].getId().getUnineg(), 
							hFacJde[i].getId().getCodsuc().trim(),
							hFacJde[i].getId().getNomsuc(), 
							hFacJde[i].getId().getCodcomp(), 
							hFacJde[i].getId().getNomcomp(),
							fecha.toString() + " " + sHora, 
							subtotal, 
							hFacJde[i].getId().getMoneda(), 
							hFacJde[i].getId().getTasa(), 
							hFacJde[i].getId().getTipopago(),
							equiv,//cor
							dTotal,//usd
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
							new BigDecimal(String.valueOf( total )), // && Conservar el total en cordobas de la factura.
							hFacJde[i].getId().getFecha(),
							hFacJde[i].getId().getHora()
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
			} catch (Exception ex) {
				System.out.print("==> Excepción capturada en formatFactura: " + ex);
			}
			return lstResultado;
		}
		public void sincronizarFacturas(){//REVISION
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
				f55ca017 = (F55ca017[]) JsfUtil.getObjectFromSessionMap("f55ca017");
				
				if(f55ca017 != null){
					// obtener conexion del datasource
					cn = As400Connection.getJNDIConnection("DSMCAJA2");
					
					iFechaActual = fecUtil.obtenerFechaActualJuliana();
					
					for(int i = 0; i < f55ca017.length;i++){
						
						sLinea = CompaniaCtrl.leerLineaNegocio(
								f55ca017[i].getId().getC7mcul());
						if(sLinea == null || sLinea.compareTo("") == 0)
							continue;
						sLinea = sLinea.trim();
						
						
						//&& ======= Verificar si debe sincronizarse o no facturas.

						if(PropertiesSystem.LNS_JDEDWARDS.contains(sLinea.trim())){
							
							//buscar facturas en el f4211
							lstF4211Facs = fCtrl.getFacturasFDC(f55ca017[i].getId().getC7mcul(), f55ca017[i].getId().getC7locn(), iFechaActual);
							for(int j = 0; j < lstF4211Facs.size();j++){
								h = (Hf4211)lstF4211Facs.get(j);
								
								//comprobar si la factura ya existe en el modulo de caja
								vh = fCtrl.comprobarSiExiste(h.getId().getNofactura(), 
										h.getId().getTipofactura(), h.getId().getCodsuc(),
										h.getId().getCodunineg(), h.getId().getCodcli(),
										h.getId().getFecha(), 
										h.getId().getHora(),
										h.getId().getEstado()
										);
								
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
					cn.commit();
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}finally{
				try {cn.close();} catch (Exception e) {}
			}
		}
		//---------------------- FINAL DE CAMBIO DE CODIGO -------------------------------	
		
	public void navigateCred(ActionEvent ev){
		try {
			System.runFinalization();
			System.gc();
			Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();	
			FacesContext fc = FacesContext.getCurrentInstance();
//			NavigationHandler nh = fc.getApplication().getNavigationHandler();
			m.remove("strBusquedaCredito");
			m.remove("lstHfacturasCredito");
//			nh.handleNavigation(fc, "navigateCred", "facturaCredito");
			limpiarSesionNavegacion();

			HttpServletResponse response = (HttpServletResponse) 
			fc.getExternalContext().getResponse();
			response.setContentType("text/html; charset=UTF-8");
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Cache-Control", "no-store");
			response.setHeader("Cache-Control", "must-revalidate");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
			fc.getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/recibos/pagoFacCredito2.faces");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void navigateFinan(ActionEvent ev){
		try {
			System.runFinalization();
			System.gc();
			Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();	
			FacesContext fc = FacesContext.getCurrentInstance();
	//		NavigationHandler nh = fc.getApplication().getNavigationHandler();
	//		nh.handleNavigation(fc, "navigateFinan", "financiamiento");
			
			m.remove("lstCuotas");
			limpiarSesionNavegacion();
			
			HttpServletResponse response = (HttpServletResponse) 
			fc.getExternalContext().getResponse();
			response.setContentType("text/html; charset=UTF-8");
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Cache-Control", "no-store");
			response.setHeader("Cache-Control", "must-revalidate");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);

			fc.getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/recibos/pagoFinanciamiento.faces");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public void navigatePrima(ActionEvent ev){
		try {
			System.runFinalization();
			System.gc();

			Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
			FacesContext fc = FacesContext.getCurrentInstance();
//			NavigationHandler nh = fc.getApplication().getNavigationHandler();
//			nh.handleNavigation(fc, "navigatePrima", "prima");
			
			limpiarSesionNavegacion();
			
			HttpServletResponse response = (HttpServletResponse) 
			fc.getExternalContext().getResponse();
			response.setContentType("text/html; charset=UTF-8");
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Cache-Control", "no-store");
			response.setHeader("Cache-Control", "must-revalidate");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
			fc.getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/recibos/pagoPrimaReserva2.faces");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void navigateSalida(ActionEvent ev){
		try {
			Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
			FacesContext fc = FacesContext.getCurrentInstance();
			NavigationHandler nh = fc.getApplication().getNavigationHandler();
			nh.handleNavigation(fc, "navigateSalida", "salida");
			
			limpiarSesionNavegacion();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void salir(ActionEvent ev){
		try {
			Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
			HttpSession session = (HttpSession) FacesContext
			.getCurrentInstance().getExternalContext()
			.getSession(false);
	
			Set s = m.keySet();
			s.clear();
			s = m.entrySet();
			s.clear();
			session.invalidate();
			System.runFinalization();
			System.gc();

			FacesContext.getCurrentInstance().getExternalContext()
									.redirect("/"+PropertiesSystem.CONTEXT_NAME+"/login.faces");

		} catch (Exception e) {
				System.out.print("Se capturo una excepcion en MenuDAO.salir: " + e);
				e.printStackTrace();
		}
	}
	public void salir2(ActionEvent ev) {
		try {
			Map m = FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap();
			HttpSession session = (HttpSession) FacesContext
					.getCurrentInstance().getExternalContext()
					.getSession(false);

			Set s = m.keySet();
			s.clear();
			s = m.entrySet();
			s.clear();
			session.invalidate();
			System.runFinalization();
			System.gc();

			FacesContext.getCurrentInstance().getExternalContext()
					.redirect("/"+PropertiesSystem.CONTEXT_NAME+"/login.faces");

		} catch (Exception e) {
				System.out.print("Se capturo una excepcion en MenuDAO.salir: " + e);
				e.printStackTrace();
		}
	}
	public void limpiarFacDiaria(){
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();	
		m.remove("lstHfacturasDiario");
		m.remove("lstHDevolucionesDiario");
		m.remove("lstHfacturasDiario");
		m.remove("lstHDevolucionesDiario");
		m.remove("dTotalVentas");
		m.remove("dTotalDevoluciones");
		m.remove("dTotalIngresos");
		m.remove("iCantFactContado");
		m.remove("iCantDevContado");
		m.remove("iCantFactCredito");
		m.remove("iCantDevCredito");
		
		m.remove("dTotalFacContado");
		m.remove("dTotalContadoDev");
		m.remove("dTotalIngresoCont");
		
		m.remove("dTotalFacCredito");
		m.remove("dTotalCreditoDev");
		m.remove("dTotalIngresoCred");
		m.remove("sMensajeDiario");
		
		m.remove("mostrarDiario");
		m.remove("sMensaje");
		m.remove("lstFiltroMonedasDiario");
		m.remove("lstDfacturasDiario");
		m.remove("bapp3");
		m.remove("lstMonedasDetalle");
		m.remove("lstFiltroCompaniaDiario");
	}
	@SuppressWarnings("unchecked")
	public void irDiaria(){
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();	
		List<Hfactura>lstHfacturasDiarias = null;
		
		try {
			
			limpiarFacDiaria();
			
			new FacturaCrtl().sincronizarFacturas();
			
			lstHfacturasDiarias = FacturaCrtl.obtenerFacturas("",
					"", "", "", 0, new Date(), new Date());
			
			if( lstHfacturasDiarias == null ){
				return;
			}
			
			
			//&& ============ Clasificacion de facturas.
			ArrayList<Hfactura>contado = new ArrayList<Hfactura>();
			ArrayList<Hfactura>credito = new ArrayList<Hfactura>();
			ArrayList<Hfactura>faccontado = new ArrayList<Hfactura>();
			ArrayList<Hfactura>devcontado = new ArrayList<Hfactura>();
			ArrayList<Hfactura>faccredito = new ArrayList<Hfactura>();
			ArrayList<Hfactura>devcredito = new ArrayList<Hfactura>();
			
			CollectionUtils.forAllDo(lstHfacturasDiarias, new Closure(){
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
					lstHfacturasDiarias, new Predicate(){
					public boolean evaluate(Object o) {
						Hfactura h = (Hfactura)o;
						int valor =  Integer.parseInt( h.getTipopago().trim() );
						return ( valor == 1 || valor == 0  );
					}
					}
				);
			credito = (ArrayList<Hfactura>)CollectionUtils
						.subtract(lstHfacturasDiarias, contado);
			
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
			
			BigDecimal totalFacCo = sumarTotalesFactura(faccontado);
			BigDecimal totalDevCo = sumarTotalesFactura(devcontado);
			BigDecimal totalFacCr = sumarTotalesFactura(faccredito);
			BigDecimal totalDevCr = sumarTotalesFactura(devcredito);
			BigDecimal totalVentas = totalFacCo.add(totalFacCr);
			BigDecimal totalDevols = totalDevCo.add(totalDevCr);	
			
			
			m.put("lstHfacturasDiario", lstHfacturasDiarias);
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
				
		} catch (Exception e) {
//			LogCrtl.imprimirError(e);
			e.printStackTrace();
		}finally{
			
			if(lstHfacturasDiarias == null )
				m.put("lstHfacturasDiario", new ArrayList<Hfactura>());
			
		}
	}
	
	public static BigDecimal sumarTotalesFactura(List<Hfactura>facturas){
		BigDecimal total = BigDecimal.ZERO;
		try {
			for (Hfactura h : facturas) 
				total = total.add(new BigDecimal(String.valueOf(h.getTotaldomtmp())));
		} catch (Exception e) {
			total = BigDecimal.ZERO;
		}
		return total;
	}
	
	public void navigateDiaria(ActionEvent ev){
		try{

			System.runFinalization();
			System.gc();
	
//			Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();	
//			FacesContext fc = FacesContext.getCurrentInstance();
//			NavigationHandler nh = fc.getApplication().getNavigationHandler();
//			nh.handleNavigation(fc, "navigateDiaria", "diaria");
			
			limpiarSesionNavegacion();
			irDiaria();
			
			FacesContext.getCurrentInstance().getExternalContext()
				.redirect("/"+PropertiesSystem.CONTEXT_NAME
						+"/recibos/factDiaria.faces");
			
			
		}catch(Exception ex){
//			LogCrtl.imprimirError(ex);
			ex.printStackTrace();
		}
	}
	
	public void navigateAnular(ActionEvent ev){
		try {
			Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();	
			
			//&& ===== Variables de navegacion.
			limpiarAnular();
			limpiarSesionNavegacion();
			
			int caid = ((ArrayList<Vf55ca01>)(m.get("lstCajas")))
						.get(0).getId().getCaid();
			List<Recibo> lstRecibos = ReciboCtrl.getRecibosCaja(caid, new Date());
			
			if(lstRecibos == null) 
				lstRecibos = new ArrayList<Recibo>();
			
			m.put("lstRecibos", lstRecibos);
			
			FacesContext fcLogin = FacesContext.getCurrentInstance();
			HttpServletResponse response = (HttpServletResponse) 
			fcLogin.getExternalContext().getResponse();
			response.setContentType("text/html; charset=UTF-8");
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Cache-Control", "no-store");
			response.setHeader("Cache-Control", "must-revalidate");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
			fcLogin.getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/recibos/anularRecibo.faces");
			
			
		} catch (Exception e) {
//			LogCrtl.imprimirError(e);
			e.printStackTrace();
		}
	}
	
/**************************************************************************************************/
	public void navigateArqueo(ActionEvent ev){
		try{
			//forzar el garbage collector
			System.runFinalization();
			System.gc();
			//
			Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();	
			FacesContext fc = FacesContext.getCurrentInstance();
//			NavigationHandler nh = fc.getApplication().getNavigationHandler();
//			nh.handleNavigation(fc, "navigateArqueo", "arqueoCaja");
			
			HttpServletResponse response = (HttpServletResponse) 
			fc.getExternalContext().getResponse();
			response.setContentType("text/html; charset=UTF-8");
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Cache-Control", "no-store");
			response.setHeader("Cache-Control", "must-revalidate");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
			fc.getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/cierre/arqueoCaja.faces");
			
			limpiarSesionNavegacion();
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public void navigateRevArqueo(ActionEvent ev){
		try{
			System.runFinalization();
			System.gc();
			Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();	
			FacesContext fc = FacesContext.getCurrentInstance();
//			NavigationHandler nh = fc.getApplication().getNavigationHandler();
//			nh.handleNavigation(fc, "navigateRevArqueo", "revArqueo");	
			
			limpiarSesionNavegacion();
			fc.getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/cierre/revisionArqueo.faces");

		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public void navigateMain(ActionEvent ev){
		try{
			System.runFinalization();
			System.gc();
			Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
			FacesContext fc = FacesContext.getCurrentInstance();
			
//			NavigationHandler nh = fc.getApplication().getNavigationHandler();
//			nh.handleNavigation(fc, "navigateMain", "home");	
			
			limpiarSesionNavegacion();
			
			HttpServletResponse response = (HttpServletResponse) 
			fc.getExternalContext().getResponse();
			response.setContentType("text/html; charset=UTF-8");
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Cache-Control", "no-store");
			response.setHeader("Cache-Control", "must-revalidate");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
			fc.getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/main.faces");
			
		}catch(Exception ex){
			ex.printStackTrace();
			ex.printStackTrace();
		}
	}
	public void navigateIngresosextra(ActionEvent ev){
		try{
			System.runFinalization();
			System.gc();
			
			Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();	
			FacesContext fc = FacesContext.getCurrentInstance();
//			NavigationHandler nh = fc.getApplication().getNavigationHandler();
//			nh.handleNavigation(fc, "navigateIngresosextra", "ingresosex");		
			
			limpiarSesionNavegacion();
			
			HttpServletResponse response = (HttpServletResponse) 
			fc.getExternalContext().getResponse();
			response.setContentType("text/html; charset=UTF-8");
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Cache-Control", "no-store");
			response.setHeader("Cache-Control", "must-revalidate");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
			fc.getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/recibos/pagoIngExtraordinario.faces");
			
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public void navigateSalidas(ActionEvent ev){
		try{			
			Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();	
			FacesContext fc = FacesContext.getCurrentInstance();
			NavigationHandler nh = fc.getApplication().getNavigationHandler();
			boolean bApsol = false;
			
			limpiarSesionNavegacion(); // limpiar antes de poner la bandera para la búsqueda.
			
			Vautoriz[] secciones = (Vautoriz[])m.get("sevAut");
			for (int i = 0; i < secciones.length;i++){
				if(secciones[i].getId().getCodaut().equals("A000000043")){
					bApsol = true;
					break;
				}
			}
			String sOutcome ="";
			if(bApsol)
				sOutcome = "aprobarsalida";
			else {
				sOutcome = "solicitarsalida";
				m.put("sp_SugerenciaSalida","S");
			}
			nh.handleNavigation(fc, "navigateSalidas", sOutcome); 
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

/************** limpiar las variables de sesion para el cierre de caja ****************************/
	public void limpiarMapaxCierreCaja(){
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();	
		try{	
			m.remove("ac_LstRptArqueo");
			m.remove("ac_NombreArqueo");
			m.remove("ac_ArqueoPrevio");
			m.remove("ac_lstDetalleSalidas");
			m.remove("ac_LstRptArqueo");
			m.remove("ac_lstDetRecpagMonEx");
			m.remove("ac_Lstrptmcaja002");
			m.remove("ac_recibosPagEfectivo");
			m.remove("ac_recibosPagCheque");
			m.remove("ac_recibosPagTarjCred");
			m.remove("ac_recibosPagTransBanco");
			m.remove("ac_recibosPagDepositoBanco");
			m.remove("ac_totalcheques");
			m.remove("lstRecxTipoyMetpago");
			m.remove("lstDetalleCambios");
			
			m.remove("lstRecibosxIngresos");
			m.remove("ac_recContTarjCred");
			m.remove("ac_recContTransBanco");
			m.remove("ac_recAbonosTarCred");
			m.remove("ac_recAbonosTransBanco");
			m.remove("ac_recAbonosDepositoBanco");
			m.remove("ac_recPrimasTarCred");
			m.remove("ac_recPrimasTransBanco");
			m.remove("ac_recPrimasDepositoBanco");
			m.remove("ac_OtrosEgCambios");
			m.remove("ac_egreRecibosxmonex");
			m.remove("ac_egreRecibosSalidas");
			m.remove("ac_TotalxEgresos");
			
			m.remove("lstDetalleRecibo");
			m.remove("lstFacturasRecibo");
			m.remove("arqueoFacturasDia");
			m.remove("arqueoFacturasCredito");
			m.remove("arqueoFacturasContado");
			m.remove("arqueoDevCredito");
			m.remove("arqueoDevContado");
			m.remove("lstFacturasxVentas");
			m.remove("lstFacturasxDevolucion");
			m.remove("dtotalVtsCredito");
			m.remove("ac_cantFactCo");
			m.remove("ac_cantFactCr");
			m.remove("ac_TotalFacCo");
			m.remove("ac_TotalFacCr");
			m.remove("lstIngresosAbono");
			m.remove("lstIngresoPrimas");
			m.remove("ac_lstriexH");
			m.remove("ac_lstriexN");
			m.remove("ac_lstriex8");
			
			m.remove("ac_recContDepositoBanco");
			m.remove("ac_TotalegIexbanco");
			m.remove("ac_lstRecCambiosOtrMoneda");
			m.remove("ac_ingRecibosxmonex");
			m.remove("ac_HoraArqueo");
			m.remove("ac_TotalxIngresos");
			m.remove("lstTodasFacturas");
			m.remove("ac_CDCefectivoneto");
			m.remove("ac_CDCMontoMinimo");
			m.remove("lstFiltroMoneda");
			m.remove("lstCierreCajaDetfactura");
			m.remove("Hfactura");
			m.remove("oldDfac");
			m.remove("lstMonedasDetalle");
			m.remove("lblEtCantFacC0");
			m.remove("lblEtTotalFacCo");
			m.remove("lblEtCantFacCr");
			m.remove("lblEtTotalFacCr");
			m.remove("lblCantFacCO");
			m.remove("lblTotalFacCo");
			m.remove("lblCantFacCr");
			m.remove("lblTotalFacCr");
			m.remove("lstFacturasRegistradas");
//			m.remove("iFechaActual");
//			m.remove("sTiposDoc");
//			m.remove("lstLocalizaciones");
//			m.remove("f55ca017");	
			
			m.remove("lstFiltroComp");
			
		}catch(Exception error){
			error.printStackTrace();
		}
	}
/************ limpiar las variables de sesion para la revisión de arqueo. *******************/
	public void limpiarMapaxRevArqueo(){
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();	
		try{			
			//
			m.remove("rev_lstDetalleSalidas");
			m.remove("rva_msgArqueos");
			m.remove("lstArqueosPendRev");
			m.remove("lstInicial_Arqueos");
			m.remove("rv_DAC");
			m.remove("rv_ListaRecibosArqueo");
			m.remove("rv_lblEtCantFacC0");
			m.remove("rv_lblEtCantFacCr");
			m.remove("rv_lblEtTotalFacCo");
			m.remove("rv_blEtTotalFacCr");
			m.remove("rv_lblCantFacCO");
			m.remove("rv_lblCantFacCr");
			m.remove("rv_lblTotalFacCo");
			m.remove("rv_lblTotalFacCr");
			m.remove("rv_lstFacturasRegistradas");
			m.remove("rv_lstRecxTipoMetpago");
			m.remove("rv_EgresoSolicitado");
			m.remove("rv_lstDetalleCambios");
			m.remove("rv_lstCierreCajaDetfactura");
			m.remove("Hfactura");
			m.remove("oldDfac");
			m.remove("rv_lstMonedasDetalle");
			m.remove("rv_lstRecibosxIngresos");
			m.remove("rv_lstDetalleRecibo");
			m.remove("rv_lstFacturasRecibo");
			m.remove("rv_lstDetRecpagMonEx");
			m.remove("rv_listaDepositos");
			m.remove("sMensajeError");
			m.remove("rva_lstFiltroCompania");
			m.remove("rva_lstFiltroMoneda");			
		}catch(Exception error){
			error.printStackTrace();
		}
	}
//-------------------------------------------------------------
	public void goHome(ActionEvent ev){
		try{
			
			limpiarSesionNavegacion();
			
			FacesContext fc = FacesContext.getCurrentInstance();
			HttpServletResponse response = (HttpServletResponse) 
								fc.getExternalContext().getResponse();
			response.setContentType("text/html; charset=UTF-8");
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Cache-Control", "no-store");
			response.setHeader("Cache-Control", "must-revalidate");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
			fc.getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/main.faces");
			
			System.runFinalization();
			System.gc();
			
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
/********** variables de sesion para el reporte de transacciones jde ********/
	private void limpiarVarSesRptjde() {
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		try {
			m.remove("rtj_fechaactual");
			m.remove("tj_fechaactual1");
			m.remove("tj_fechaactual2");
			m.remove("rtj_lstCajas");
			m.remove("rtj_lstFiltroCompania");
			m.remove("rtj_lstHdrptmcaja003");
			m.remove("rtj_lstDtcorptmcaja003");
			m.remove("rtj_lstDtcrrptmcaja003");
			m.remove("rtj_lstDtderptmcaja003");
			m.remove("rtj_lstDtprrptmcaja003");
			m.remove("rtj_lstDtsarptmcaja003");
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
/********** variables de sesion para el pago de primas y reservas ************************/	
	private void limpiarPrimasReservas() {
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		try {
			
			m.remove("pr_MensajetasaJde");
			m.remove("pr_lblTasaJde");
			m.remove("pr_valortasajde");
			m.remove("pr_TasaJdeTcambio");
			m.remove("pr_tjdeactobj");
			m.remove("MsgErrorJDE");
			m.remove("pr_MensajetasaJde");
			m.remove("pr_Monedaunineg");
			m.remove("pr_metpago");
			m.remove("pr_lstMetodosPago");
			m.remove("pr_metodospago");
			m.remove("pr_lstAfiliado");
			m.remove("pr_mpagos");
			m.remove("pr_lstPagos");
			m.remove("pr_lstFiltrounineg");
			m.remove("pr_strBusquedaPrima");
			m.remove("sMsgErrorRecPrimas");
			m.remove("pr_ReciboActual");
			m.remove("pr_montoRecibibo");
			m.remove("pr_montoIsertando");
			m.remove("pr_monto");
			m.remove("pr_mPagoOld");
			m.remove("pr_sMontoOld");
			m.remove("pr_mTipoRecibo");
			m.remove("pr_cMpagos");
			m.remove("pr_lstMoneda");
			m.remove("pr_moneda");
			m.remove("pr_lblTasaCambio");
			m.remove("pr_afi");
			m.remove("pr_lstBanco");
			m.remove("pr_banco");
			m.remove("pr_vmarca");
			m.remove("pr_lstMarcas");
			m.remove("pr_lstModelos");
			m.remove("pr_vmodelo");
			m.remove("pr_lstSolicitud");
			m.remove("pr_Mensajetasap");
			m.remove("pr_tpcambio");
			m.remove("pr_tasaparelela");
			m.remove("pr_valortasap");
			m.remove("pr_lblTasaCambio");
			m.remove("pr_tasa");
			m.remove("pr_rCambio");
			m.remove("pr_mTipoRecibo");
			m.remove("pr_vtipoProd");
			m.remove("pr_lstTipoProducto");
			m.remove("pr_marca");
			m.remove("pr_modelo");
			m.remove("pr_lstCompanias");
			m.remove("pr_comp");
			m.remove("pr_met");
			m.remove("pr_lstAutoriza");
			m.remove("pr_lstAutorizadores");
			m.remove("pr_lstFiltrosucursal");
			m.remove("pr_lstFiltrounineg");
			
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
	public void limpiarContado(){
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		try {
			m.remove("iContadorDom");
			m.remove("iContadorFor");
			m.remove("lblMensajeValidacionCon");
			m.remove("iNoFicha");
			m.remove("cn");
			m.remove("iNumRec");
			m.remove("facturasSelected");
			m.remove("mostrar");
			m.remove("sMensaje");
			m.remove("lstDfacturasContado");
			m.remove("Hfactura");
			m.remove("oldDfac");
			m.remove("lstMonedasDetalle");
			m.remove("lstDetalleReciboFactDev");
			m.remove("lstMetodosPagoDev");
			m.remove("lstMonedaDev");
			m.remove("lstDetalleReciboOriginal");
			m.remove("lstDetalleReciboFactDev");
			m.remove("mTotalFactura");
			m.remove("lstPagos");
			m.remove("lstHfacturasContado");
			m.remove("strBusqueda");
			m.remove("lstAfiliado");
			m.remove("lblTasaCambio");
			m.remove("metpago");
			m.remove("mTipoRecibo");
			m.remove("cMpagos");
			m.remove("lstMetodosPago");
			m.remove("montoRecibibo");
			m.remove("mpagos");
			m.remove("lstSolicitud");
			m.remove("montoIsertando");
			m.remove("monto");
			m.remove("mPagoOld");
			m.remove("sMontoOld");	
			m.remove("bapp2");
			m.remove("bLstMonDetalleReciboCon");
			m.remove("lstMonedasDetalleReciboContado");
			m.remove("fmonCon");
			m.remove("lstFiltroMonedas");
			m.remove("tasaCon");
			m.remove("lstBanco");
			m.remove("f55ca022");
			m.remove("lstAutoriza");
			m.remove("metCon");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public void limpiarCredito(){
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		try {
			m.remove("iNoFicha");
			m.remove("lblMensajeValidacionCre");
			m.remove("iContadorDom");
			m.remove("iContadorFor");
			m.remove("lstHfacturasCredito");
			m.remove("mostrarCredito");
			m.remove("lstRefreshFacturas");
			m.remove("sMensajeCre");
			m.remove("selectedFacsCredito");
			m.remove("lstSucursalCred");
			m.remove("lstUninegCred");
			m.remove("lstAgregarFactura");
			m.remove("strBusquedaCredito");
			m.remove("hFac");
			m.remove("lstMonedasDetalle");
			m.remove("lstDfacturasCredito");
			m.remove("lstPagos");
			m.remove("mpagos");
			m.remove("metpago");
			m.remove("lstMonedaCre");
			m.remove("lstAfiliadoCre");
			m.remove("lblTasaCambio");
			m.remove("mTipoRecibo");
			m.remove("cMpagos");
			m.remove("lstMetodosPagoCre");
			m.remove("lstPagos");
			m.remove("montoIsertando");
			m.remove("monto");
			m.remove("dMontoAplicarCre");
			m.remove("lstHfacturasCredito");
			m.remove("iNumRecCredito");
			m.remove("fmonCre");
			m.remove("lstFiltroMonedas");
			m.remove("tasaCon");
			m.remove("lstBancoCre");
			m.remove("lblTasaCambio");
			m.remove("metCre");
			m.remove("lstAutoriza");
			m.remove("lstAutorizadores");
			m.remove("lstDfacturasCredito");
			m.remove("lstCompaniaCre");
			m.remove("fcr_MotivoCambioTasa");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public void limpiarAnulacion(){
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		try {
			m.remove("recibo");
			m.remove("lstDetalleRecibo");
			m.remove("lstFacturasRecibo");
			m.remove("lstRecibos");
			m.remove("strMensajeRecibo");
			m.remove("mostrarRecibo");
			m.remove("strBusquedaRecibo");
			m.remove("lstCompaniaRecibo");
			m.remove("compRecibo");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void limpiarFinanciamiento(){
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		try {
			m.remove("lstMetodosPagoFinan");
			m.remove("lstMonedaFinan");
			m.remove("lstSelectedCuotas");
			m.remove("lstProductos");
			m.remove("lstDetalleSolicitd");
			m.remove("lstIntereses");
			m.remove("lstMonedaDetalle");
			m.remove("lstCompaniaRecibo");
			m.remove("cuota");
			m.remove("strBusquedaFinan");
			m.remove("lstBanco");
			m.remove("f55ca022");
			m.remove("lstDetallesol");
			m.remove("lstAgregarCuota");
			m.remove("fin_lstAutoriza");
			m.remove("fin_lstAutorizadores");
			m.remove("lstPagosFinan");
			m.remove("fin_montoIsertando");
			m.remove("fin_monto");
			m.remove("fin_lstSolicitud");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public void limpiarIngresosEx(){
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		try {
			m.remove("iex_MensajetasaJde");
			m.remove("iex_MsgErrorJDE");
			m.remove("iex_lstFiltrounineg");
			m.remove("iex_VF0901");
			m.remove("iex_VcuentaoperacionSel");
			m.remove("iex_lstCuentasTo");
			m.remove("iex_lstCuentasxoperacion");
			m.remove("iex_Vcuentaoperacion");
			m.remove("pr_strBusquedaPrima");
			m.remove("iex_lstFiltrosucursal");
			m.remove("iex_lstFiltrounineg");
			m.remove("iex_ReciboActudal");
			m.remove("iex_selectedMet");
			m.remove("iex_mpagos");
			m.remove("iex_lstSolicitud");
			m.remove("iex_montoRecibibo");
			m.remove("iex_MontoAplicado");
			m.remove("iex_lblTasaCambio");
			m.remove("iex_lstMetodosPago");
			m.remove("iex_montoIsertando");
			m.remove("iex_monto");
			m.remove("iex_cMpagos");
			m.remove("iex_lstMoneda");
			m.remove("iex_lstFiltroCompanias");
			m.remove("iex_lstFiltroMonapl");
			m.remove("iex_ReciboActual");
			m.remove("iex_lstAfiliado");
			m.remove("iex_lstBanco");
			m.remove("iex_banco");
			m.remove("iex_Mensajetasap");
			m.remove("iex_tpcambio");
			m.remove("iex_tasaparelela");
			m.remove("iex_valortasap");
			m.remove("iex_lblTasaCambio");
			m.remove("iex_tasa");
			m.remove("pr_MensajetasaJde");
			m.remove("iex_lblTasaJde");
			m.remove("iex_valortasajde");
			m.remove("iex_TasaJdeTcambio");
			m.remove("iex_tjdeactobj");
			m.remove("iex_lstTipoOperaciones");
			m.remove("iex_lstAutoriza");
			m.remove("iex_lstAutorizadores");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public void limpiarAprobarSalidas(){
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		try {
			m.remove("aps_lstSalidas");
			m.remove("aps_MsgExistSalidas");
			m.remove("aps_lstFiltroEstado");
			m.remove("aps_lstFiltroMoneda");
			m.remove("aps_lstFiltroCompanias");
			m.remove("aps_CEsalida");
			m.remove("aps_CambiarEstadoS");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public void limpiarSolicitarSalidas(){
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		try {
			m.remove("sp_SugerenciaSalida");
			m.remove("ss_lstFiltroCompanias");
			m.remove("ss_UltimaSalida");
			m.remove("ss_lstBanco");
			m.remove("ss_banco");
			m.remove("ss_lstMoneda");
			m.remove("ss_lstTipoOperacion");
			m.remove("ss_MsgExistSalidas");
			m.remove("ss_lstFiltroEstado");
			m.remove("ss_lstSalidas");
			m.remove("ss_tasaparelela");
			m.remove("ss_Mensajetasap");
			m.remove("ss_tpcambio");
			m.remove("ss_tasaparelela");
			m.remove("ss_lblTasaCambio");
			m.remove("ss_lblTasaJde");
			m.remove("ss_MensajetasaJde");
			m.remove("ss_valortasajde");
			m.remove("ss_TasaJdeTcambio");
			m.remove("ss_tjdeactobj");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	//--------- Reporte de Detalle de arqueo.
	public void limpiarRptmcaja004(){
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		try {
			m.remove("rdta_lstArqueosCaja");
			m.remove("rdta_dtValorFiltroFecha");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	//------------------- Reporte de Emisión de Recibos de Caja (RPTMCAJA005).
	public void limpiarRptmcaja005(){
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		try {
			m.remove("rpt005_fechaactual1");
			m.remove("rpt005_fechaactual2");
			m.remove("rpt005_lstFiltroCompania");
			m.remove("rpt005_lstFiltroMoneda");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	//------------------- Reporte de Recibos de Caja (RPTMCAJA006).
	public void limpiarRptmcaja006(){
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		try {
			m.remove("rptmcaja006_hd");
			m.remove("rptmcaja006_bd");
			m.remove("rpt006_lstFiltroMoneda");
			m.remove("rpt006_fechaactual1");
			m.remove("rpt006_fechaactual2");
			m.remove("rpt006_lstFiltroCompania");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	//------------------- Reporte de Recibos de Caja en secuencia numérica. (RPTMCAJA007).
	public void limpiarRptmcaja007(){
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		try {
			m.remove("rptmcaja007_hd");
			m.remove("rptmcaja007_bd");
			m.remove("rtj_lstFiltroCompania");
			m.remove("rpt007_fechaactual1");
			m.remove("rpt007_fechaactual2");
			m.remove("rpt007_lstFiltroCaja");
			m.remove("rpt007_lstFiltroCompania");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public void limpiarRptmcaja008(){
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		try {
			m.remove("rsc_solicitudSeleccionada");
			m.remove("rptmcaja008_bd");
			m.remove("rptmcaja008_detrec");
			m.remove("rptmcaja008_nombre");
			m.remove("rsc_solicitudSeleccionada");
			m.remove("rsc_lstDetalleDevolucion");
			m.remove("rsc_lstDetalleFactura");
			m.remove("rsc_lstSolicitudesCheques");
			m.remove("rsc_strBusquedaCheques");
			m.remove("rsc_lstFiltroCompania");
			m.remove("rsc_lstFiltroEstado");
			m.remove("rsc_lstFiltroMoneda");
			m.remove("rsc_lstSolicitudesCheques");
			m.remove("rsc_fechaactual1");
			m.remove("rsc_fechaactual2");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public void limpiarAnular(){
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		try {
			m.remove("lstRecibos");
			m.remove("lstCajaConsulta");
			m.remove("lstFacturasRecibo");
			m.remove("lstDetalleRecibo");
			m.remove("lstCompaniaRecibo");
			m.remove("compRecibo");
			m.remove("strMensajeRecibo");
			m.remove("mostrarRecibo");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public void limpiarConsultarRecibo(){
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		try {
			m.remove("lstRecibosCN");
			m.remove("lstCajaConsultaCN");
			m.remove("lstFacturasReciboCN");
			m.remove("lstDetalleReciboCN");
			m.remove("lstCompaniaReciboCN");
			m.remove("compReciboCN");
			m.remove("strMensajeReciboCN");
			m.remove("mostrarReciboCN");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
/***************************************************************************
 *       Limpiar las variables de sesión de las secciones vistadas        */
	public void limpiarSesionNavegacion(){
		Map<String, Object> m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		try {
			//-----------  LIMPIAR VARIABLES DE SESION. ---------------//
			m.remove("sp_SugerenciaSalida");//Bandera para identificar busqueda en salidas de caja.
			m.remove("rptmcaja009");
			m.remove("rptmcaja008");
			
			limpiarMapaxCierreCaja();     // Cierre de caja.
			limpiarMapaxRevArqueo();      // Revisión de arqueo.
			limpiarVarSesRptjde();		  // Reporte Transacciones JDE.
 			limpiarPrimasReservas();// Primas y reservas.
			limpiarContado();			  // Pago a facturas de contado.
			limpiarCredito();			  // Pago a facturas de crédito.
			limpiarFacDiaria();			  // Mostrar Facturación diaria.
			limpiarIngresosEx(); 		  // Ingresos extraordinario.
			limpiarSolicitarSalidas();	  // Solicitud de Salidas.
			limpiarAprobarSalidas();	  // Aprobración de Salidas.
			limpiarAnulacion();			  // Anulación de recibos.
			limpiarFinanciamiento();	  // Pago a Financiamiento.
			limpiarRptmcaja004();		  // Reporte de Detalle de Arqueo.
			limpiarRptmcaja005();		  // Reporte de Emisión de Recibos de Caja.
			limpiarRptmcaja006();		  // Reporte de Recibos de Caja.
			limpiarRptmcaja007();		  // Reporte de Recibos de Caja.
			limpiarRptmcaja008();		  // Revisión de solicitudes y reporte de emisión de cheque.
			limpiarConsultarRecibo();	  // limpiar consulta de recibos
			//---------------------------------------------------------//
			
			limpiarMapaSesion(m,"ac_");    	// Cierre de caja.
			limpiarMapaSesion(m,"rva_");    // Revisión de arqueo.
			limpiarMapaSesion(m,"rev_");    // Revisión de arqueo.
			limpiarMapaSesion(m,"rv_");     // Revisión de arqueo.
			limpiarMapaSesion(m,"rdta_");   // Revisión de arqueo.
			
			limpiarMapaSesion(m,"rtj_"); 	// Reporte Transacciones JDE.
			limpiarMapaSesion(m,"pr_"); 	// Primas y reservas.
			limpiarMapaSesion(m,"fdc_");	// Pago a facturas de contado.
			limpiarMapaSesion(m,"iex_");    // Ingresos extraordinario.
			limpiarMapaSesion(m,"ss_");     // Solicitud de Salidas.
			limpiarMapaSesion(m,"aps_");	// Aprobración de Salidas.			
			limpiarMapaSesion(m,"rdta_");   // Reporte de Detalle de Arqueo.
			limpiarMapaSesion(m,"rpt005_"); // Reporte de Emisión de Recibos de Caja.
			limpiarMapaSesion(m,"rpt006_"); // Reporte de Recibos de Caja.
			limpiarMapaSesion(m,"rpt007_");	// Reporte de Recibos de Caja.
			limpiarMapaSesion(m,"rpt010_");	// Reporte de caja DGI
			limpiarMapaSesion(m,"rsc_");    // Revisión de solicitudes y reporte de emisión de cheque.
			limpiarMapaSesion(m,"cct_");    // Consulta de cuenta transitoria.
			limpiarMapaSesion(m,"dnc_");	// Donaciones en caja
			limpiarMapaSesion(m,"pmt_");	// anticipos por PMT
			
		} catch (Exception e) {
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
		}
	}
/******************************************************************************************/
/** Método: Limpiar las variables de sesion puestas al mapa del navegador.
 *	Fecha:  11/06/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public void limpiarMapaSesion(Map<String, Object> m, String sPrefijoVariable){
		Set s = null;
		Iterator it = null;
		try {
			s = m.entrySet();
			it = s.iterator();
			while (it.hasNext()) {
				Map.Entry me = (Map.Entry) it.next();
				String key = (String) me.getKey();

				if (key.trim().toLowerCase().startsWith(sPrefijoVariable)) {
					m.remove(key); 
				}
			}
			m.remove("AjustaMinimo");
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
/****************************************************************************************/		
/**  Returs a list of menu items   */
	public List getMenuItems() {
		if (menuItems==null) {
			menuItems = createMenuItems();
		}
		return menuItems;		
	}
/***********************************************************************************/
/**	Cargar los arqueos de la caja para luego generar reporte de detalle de arqueo  */
	public void obtenerArqueosCaja(){
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		List lstArqueos = null;
		String sql = "";
		
		try {
			List lstcaja = (ArrayList)m.get("lstCajas");
			Vf55ca01 caja = (Vf55ca01)lstcaja.get(0);
			sql =  " from Varqueo as a where a.id.caid = " +caja.getId().getCaid();
			sql += " order by a.id.noarqueo desc,a.id.fecha desc";
			
			lstArqueos =   new ArqueoCtrl().obtenerListaArqueos(sql, 30);
			if(lstArqueos==null || lstArqueos.size()==0)
				lstArqueos = new ArrayList();
			
			m.put("rdta_lstArqueosCaja", lstArqueos);
			
		} catch (Exception error) {
			error.printStackTrace();
		}
	}	
/***********************************************************************************/
/**		cargar los arqueos pendientes de revisión para el contador conectado 	   */
	public void cargarArqueosPendientes(){
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		List lstArqueosPendRev = new ArrayList();
		
		try{	
			List lstcaja = (ArrayList)m.get("lstCajas");
			Vf55ca01 caja = (Vf55ca01)lstcaja.get(0);
			ArqueoCtrl acCtrl = new ArqueoCtrl();
			String sql="from Varqueo as v where v.id.estado = 'P'";
			
			lstArqueosPendRev = acCtrl.obtenerArqueosPendientes(sql, caja.getId().getCacont(), false, 30);			
			if(lstArqueosPendRev == null || lstArqueosPendRev.size()==0){				
				m.remove("lstArqueosPendRev");
				m.remove("lstInicial_Arqueos");
				m.put("rva_msgArqueos", "No se encontraron arqueos pendientes.");				
			}else{
				m.put("lstArqueosPendRev", lstArqueosPendRev);
				m.put("lstInicial_Arqueos", lstArqueosPendRev);
				m.remove("rva_msgArqueos");
			}		
		}catch(Exception error){
			error.printStackTrace();
		}
	}
	public void irRptmcaja004DAO(ActionEvent ev){
		try{
			Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
			limpiarSesionNavegacion();
			obtenerArqueosCaja();
			
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect("/"+PropertiesSystem.CONTEXT_NAME
							+"/reportes/detalleArqueoCaja.faces");
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	//----------- Navegar hacia la página principal de revisión de solicitudes de cheques.
	public void irRevisionSolecheque(ActionEvent ev){
		try{
			Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
			limpiarSesionNavegacion();
			FacesContext fc = FacesContext.getCurrentInstance();
			NavigationHandler nh = fc.getApplication().getNavigationHandler();
			nh.handleNavigation(fc, "onItemClick", "revsolecheque");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public String getStrDisplayInicio() {		
		Object obj = null;
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		obj =  m.get("sDisplayInicio");
		if (obj != null) {
			strDisplayInicio = (String)obj;
		}		
		return strDisplayInicio;
	}
	public void setStrDisplayInicio(String strDisplayInicio) {
		this.strDisplayInicio = strDisplayInicio;
	}
	public String getStrstdProc() {
		try {
			strstdProc = "hidden";
			String sEstado = CtrlCajas.generarMensajeBlk();
			if(sEstado.compareTo("") != 0)
				strstdProc = "normal";
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strstdProc;
	}
	public void setStrstdProc(String strstdProc) {
		this.strstdProc = strstdProc;
	}
	public void cerrarProcesa(ActionEvent ev){
		dwProcesa.setWindowState("hidden");
	}
	public HtmlDialogWindow getDwProcesa() {
		return dwProcesa;
	}
	public void setDwProcesa(HtmlDialogWindow dwProcesa) {
		this.dwProcesa = dwProcesa;
	}
}


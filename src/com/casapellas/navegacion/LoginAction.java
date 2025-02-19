package com.casapellas.navegacion;
/**
 * CASA PELLAS S.A.
 * Creado por.........: Juan Carlos Ñamendi Pineda
 * Fecha de Creación..: 05/01/2009
 * Última modificación: 05/09/2011
 * Modificado por.....:	Juan Carlos Ñamendi Pineda
 * 
 */

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.component.html.HtmlInputSecret;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.casapellas.controles.CompaniaCtrl;
import com.casapellas.controles.tmp.CtrlCajas;
import com.casapellas.controles.ConfirmaDepositosCtrl;
import com.casapellas.controles.ConsolidadoDepositosBcoCtrl;
import com.casapellas.controles.EmpleadoCtrl;
import com.casapellas.controles.VautorizacionCrtl;
import com.casapellas.entidades.F55ca014;
import com.casapellas.entidades.Vf55ca01;
import com.casapellas.entidades.ens.Vautoriz;
import com.casapellas.util.AllConectionMngt;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.DocumuentosTransaccionales;
import com.casapellas.util.JsfUtil;
import com.casapellas.util.PropertiesSystem;
import com.casapellas.util.ens.ConnectionDb2;
import com.casapellas.util.ens.ENSUtilValidation;
import com.casapellas.util.ens.LoadProperties;
import com.infragistics.faces.grid.component.RowItem;
import com.infragistics.faces.grid.component.html.HtmlGridView;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
import com.infragistics.faces.shared.component.DataRepeater;
import com.sun.xml.internal.messaging.saaj.util.Base64;



public class LoginAction {
	
	@SuppressWarnings("rawtypes")
	Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	private HtmlInputText uiUsuario;
	private HtmlInputSecret uiContrasenia;
	private String strMsgLogin = "";//Lo nombre asi porque sMsgLogin provoca un error(Bug de jsf)
	private Connection cnAs400 = null; 
	@SuppressWarnings("unused")
	private String sNomCorto;
	private Connection cnMSqlServer2008 = null;
	
	/*******seleccion de cajas******/
	@SuppressWarnings("rawtypes")
	private List lstComboCajas;
	private HtmlDropDownList cmbCajas;
	@SuppressWarnings("rawtypes")
	private List lstCajasEncontradas;
	private HtmlGridView gvCajas;	
	
	public String encriptBase64(String encript) {
        byte[] byteText = encript.getBytes();
        StringBuilder stb = new StringBuilder();
        for (int i = 0; i < byteText.length; i++) {
            stb.append((int) byteText[i] & 0xff + 0x100);
            if (i < (byteText.length - 1)) {
                stb.append(",");
            }
        }
        byte[] encodedBytes = Base64.encode(stb.toString().getBytes());
        return new String(encodedBytes);
    }
	
	
/****************LOG IN***************************/
	@SuppressWarnings("unchecked")
	public void login(ActionEvent e) {	
		FacesContext fcLogin = FacesContext.getCurrentInstance();
		ExternalContext ecLogin = fcLogin.getExternalContext();
		NavigationHandler nhLogin = fcLogin.getApplication().getNavigationHandler();
		List<Vf55ca01> lstCajas = new ArrayList<Vf55ca01>();
		CtrlCajas cajasCtrl = new CtrlCajas();
		Vf55ca01 caja = null; 
		boolean validado = true;
		boolean lExistenDatosAProcesar = false;
		F55ca014[] f14 = null;
		CompaniaCtrl cCtrl = new CompaniaCtrl();

		try {			
			//&& ==== Limpiar los archivos que se exportan en consultas.
			
			borrarDescargasExports("Confirmacion");
			
			m.put("sDisplayInicio", "true");
			m.remove("sqlState");
			m.remove("msgConnection");
			
			encriptBase64(uiUsuario.getValue().toString());
			encriptBase64(uiContrasenia.getValue().toString());
			
			//Obtener el url del root y el nombre del sistema
			String sRootUrl = ecLogin.getRequestContextPath();
			sNomCorto = sRootUrl.substring(1);
			
			AllConectionMngt cndb2 = new AllConectionMngt();
			try{
				cnAs400  = cndb2.getCustomDriverConnection(uiUsuario.getValue().toString(),  uiContrasenia.getValue().toString());
				
			}catch(Exception ex){
				strMsgLogin= ex.getMessage();
				cnAs400 = null;
				validado = false;
				
			}

			if(!validado){				
			
				if(strMsgLogin.compareToIgnoreCase("REDIRECT_TO_CHANGE_PASSWORD") == 0){
					try {
						HttpServletResponse response = (HttpServletResponse) 
    							fcLogin.getExternalContext().getResponse();  
						String sUrlChgPws = LoadProperties.queryProps.getProperty("url.changepassword.as400");
						sUrlChgPws = sUrlChgPws.replace("@USERNAME", uiUsuario.getValue().toString() );
						response.sendRedirect(sUrlChgPws);
					} catch (Exception e1) {							
						e1.printStackTrace();
					}		
				}

				return;
			}
			
			try {
				if(!cnAs400.isClosed()) {
					cnAs400.close();					
				}
			}catch(Exception ex ) {
				System.out.println("Fallo en cerrar la conexion de usuario!!");
			}
			
			m.put("seBIsLoggedIn","true");
			m.put("vc","vc");
			
			VautorizacionCrtl vautCtrl = new VautorizacionCrtl();
		
			ENSUtilValidation eNSUtilValidation= new ENSUtilValidation();
			
	        List<Vautoriz> lstVautoriz = (ArrayList<Vautoriz>)eNSUtilValidation.getAutorizProfile(uiUsuario.getValue().toString(), "mcaja");

	        if(lstVautoriz == null || lstVautoriz.isEmpty()){
	        	strMsgLogin = "Usuario sin autorización";
	        	return;
	        }
	         		        
	        Vautoriz[] vAut = lstVautoriz.toArray( new Vautoriz[lstVautoriz.size()] );
	        
	        //&& ================ usuario sin configuracion de perfil en ENS
	    	if (vAut == null || vAut.length == 0 || vAut[0] == null){
				strMsgLogin = "Sin Autorización";	
				fcLogin.getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/login.faces");
				return;
	    	}
	        
			m.put("sevAut", vAut);
				
			borrarMinutasExist("Confirmacion", String.valueOf(vAut[0].getId().getCodreg()));
			
			validado = vautCtrl.verificarPerfil( vAut[0].getId().getCodreg(), vAut[0].getId().getCodper(), vAut[0].getId().getCodapp() );
			
			 //&& ================ el usuario tiene mal configurado el perfil en ENS
			if (!validado){
				strMsgLogin = "El usuario tiene asignado mas de un perfil en ENS";
				fcLogin.getExternalContext().redirect( "/" + PropertiesSystem.CONTEXT_NAME + "/login.faces");
			}
			

			
			
			
			 //&& ================ configuraciones para preconciliacion, perfiles y cuentas por usuario
			if (
				vAut[0].getId().getCodper().compareTo(DocumuentosTransaccionales.ENSCONCILIADORSUPERVISOR()) == 0  ||
				vAut[0].getId().getCodper().compareTo(DocumuentosTransaccionales.ENSCONCILIADORPRINCIPAL()) == 0  ||
				vAut[0].getId().getCodper().compareTo(DocumuentosTransaccionales.ENSADMINISTRADORCAJA()) == 0 ){
				
						ConfirmaDepositosCtrl.cargarConfiguracionConciliador(vAut[0].getId().getCodreg());
						
				}
				
			if (    vAut[0].getId().getCodper().compareTo(DocumuentosTransaccionales.ENSCONCILIADORPRINCIPAL()) == 0  ||
					vAut[0].getId().getCodper().compareTo(DocumuentosTransaccionales.ENSCONCILIADORSUPERVISOR()) == 0
				){
				
				String sql = "select * from " + PropertiesSystem.ESQUEMA +".Vf55ca01 where castat = 'A' fetch first rows only " ; 
				List<Vf55ca01> cajas = (ArrayList<Vf55ca01>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, true, Vf55ca01.class);
			 
				caja = cajas.get(0);
				
				lstCajas.add(caja);
				f14 = CompaniaCtrl.obtenerCompaniasConciliarxCaja( caja.getId().getCaid() );
				
				m.put("cont_f55ca014",f14);
				m.put("iCodCajero", vAut[0].getId().getCodreg());
				m.put("sLogin", vAut[0].getId().getLogin());
				m.put("sNombreEmpleado", CodeUtil.capitalize( vAut[0].getId().getEmpnombre().trim() ) );
				m.put("sCajaId", String.valueOf(caja.getId().getCaid()));
				m.put("sNombreSucursal",caja.getId().getCaconom());
				m.put("lstCajas",lstCajas);
				
				
				fcLogin.getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/main.faces");
			
				return;
			}
			
			
			//&& ================ buscar si el perfil tiene rol de rotativo
			String codigogrupo = CtrlCajas.codigoGrupoCajaPorPerfil( vAut[0].getId().getCodper() );
			
			
			//&& ========= CAJERO ROTATIVO O ADMINISTRADOR
			if ( vAut[0].getId().getCodper().equals( DocumuentosTransaccionales.ENSADMINISTRADORCAJA() ) || vAut[0].getId().getCodper().equals("P000000005")){
				
				lstCajas = cajasCtrl.getAllCajas();
				
				if(lstCajas == null || lstCajas.isEmpty()){
					strMsgLogin = "No se encontraron cajas configuradas";	
					fcLogin.getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/login.faces");
					return;
				}
				
			/*	for (int i = 0; i < lstCajas.size(); i++) {
					caja = lstCajas.get(i);
					caja.getId().setCaname(lstCajas.get(i).getId().getCaname().trim().toLowerCase());				
					lstCajas.set(i,caja);
				}*/

				m.put("lstCajas",lstCajas);
				m.put("lstComboCajas", new ArrayList<SelectItem>(1));
				
				fcLogin.getExternalContext().redirect("/" + PropertiesSystem.CONTEXT_NAME + "/loginCajaActual.faces");
				return;
			}
			//&& ========= CAJERO TITULAR	
			if (vAut[0].getId().getCodper().equals("P000000002") || vAut[0].getId().getCodper().equals("P000000113")){				
				
				lstCajas = cajasCtrl.obtenerCaja(vAut[0].getId().getCodreg());
				
				if(lstCajas != null && !lstCajas.isEmpty()){
					
					caja = (Vf55ca01)lstCajas.get(0);
					String sNombre = caja.getId().getCacatinom();
					String sCajaId = String.valueOf(caja.getId().getCaid());								
					String sNombreSucursal = caja.getId().getCaconom().trim();
					m.put("iCodCajero", vAut[0].getId().getCodreg());
					m.put("sLogin", vAut[0].getId().getLogin());
					
					//obtener companias x caja
					f14 = cCtrl.obtenerCompaniasxCaja(((Vf55ca01)lstCajas.get(0)).getId().getCaid());
					m.put("cont_f55ca014",f14);
					
					if (sNombre != null)
						m.put("sNombreEmpleado", sNombre);
					else
						m.put("sNombreEmpleado", vAut[0].getId().getLogin());
					
					m.put("sCajaId", sCajaId);
					m.put("sNombreSucursal",sNombreSucursal);
					m.put("sImpresora", caja.getId().getCaprnt());
					m.put("lstCajas",lstCajas);
					
					// Verificar si la caja esta bloqueada o no
					CtrlCajas.bloquearCaja(caja.getId().getCaid());
				
					//*********************************/ 
					String sTipoCaja = caja.getId().getCaprnt().trim();
					if (sTipoCaja.equals("DICAP")){
				
						CtrlRutasFDC ctrlRutasFDC = new CtrlRutasFDC();
							
						cnMSqlServer2008 = MSqlServer2008Connection.getJNDIConnection("DSFDC2008");
						if(cnMSqlServer2008 == null){
							fcLogin.addMessage("errorMsgs", new FacesMessage("Usuario No Autenticado en SQL Server 2008 (Windows)!!"));
							throw new Exception(" Usuario No Autenticado en SQL Server 2008!!");
						}	
						
						lExistenDatosAProcesar = ctrlRutasFDC.SiExistenDatosFDCXProcesar(caja, "SQLServer2008");
						if (lExistenDatosAProcesar){
							m.put("sInfoFdc","Existe Información de Ruteo a Procesar...");
							m.put("sDisplayInicio", "false");
							m.put("SQLServer2008", "SQLServer2008");
						} else {
							m.remove("sInfoFdc");
						}
							
					}
					
					if (lExistenDatosAProcesar) {
						nhLogin.handleNavigation(fcLogin, "login", "home_fdc");
					} else {		
						if(lstCajas != null && !lstCajas.isEmpty()){											
							strMsgLogin = "";
							fcLogin.getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/main.faces");
						}else {
							strMsgLogin = "No se encontró la información de cajas";
							fcLogin.getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/login.faces");
						}
					}
				}else{								
					strMsgLogin = "El usuario no tiene caja asignada";	
					nhLogin.handleNavigation(fcLogin, "login", null);
				}	
				
				return ;
			}
			//&& ================= CONTADOR
			if(vAut[0].getId().getCodper().equals( "P000000015" )){
				
				lstCajas = cajasCtrl.obtenerCajasxContador(vAut[0].getId().getCodreg());
				if(lstCajas != null && !lstCajas.isEmpty()){
					caja = (Vf55ca01)lstCajas.get(0);
					String sCajaId = "";
					String sNombreSucursal = "";
					String sNombre = caja.getId().getCacontnom();
					if(lstCajas.size() > 1){
						for(int i = 0;i < lstCajas.size(); i++){
							if(i == lstCajas.size()-1){
								sCajaId = sCajaId + ((Vf55ca01)lstCajas.get(i)).getId().getCaid();
							}else{
								sCajaId = sCajaId +((Vf55ca01)lstCajas.get(i)).getId().getCaid() + ", ";
							}
						}
						sNombreSucursal = "Multiples";
						f14 = cCtrl.obtenerCompaniasxCaja(((Vf55ca01)lstCajas.get(0)).getId().getCaid());
						m.put("cont_f55ca014",f14);
					}else{
						sCajaId = String.valueOf(caja.getId().getCaid());
						sNombreSucursal = caja.getId().getCaconom();
						f14 = cCtrl.obtenerCompaniasxCaja(caja.getId().getCaid());
						m.put("cont_f55ca014",f14);
					}
					
					m.put("iCodCajero", vAut[0].getId().getCodreg());
					m.put("sLogin", vAut[0].getId().getLogin());
					
					if (sNombre != null)
						m.put("sNombreEmpleado", sNombre);
					else
						m.put("sNombreEmpleado", vAut[0].getId().getLogin());
					
					m.put("sCajaId", sCajaId);
					m.put("sNombreSucursal",sNombreSucursal);
					m.put("sImpresora", caja.getId().getCaprnt());
					m.put("lstCajas",lstCajas);

					strMsgLogin = "";
					m.put("lstCajasContador",lstCajas);
					nhLogin.handleNavigation(fcLogin, "login", "home");
				}else{
					strMsgLogin = "El usuario no tiene caja asignada";	
					nhLogin.handleNavigation(fcLogin, "login", null);
				}
				return;
			}
				
			//&& ================= buscar lista de cajas por agrupaciones cajas para rotativos
			
			if( codigogrupo != null  && !codigogrupo.isEmpty() ){
				
				List< List<?> > datosCaja = (List<List<?>>) CtrlCajas.cajasPorTipoAgrupacion( vAut[0].getId().getCodper() );

				if(datosCaja == null ||  datosCaja.isEmpty()){
					strMsgLogin = "No se encontró la información de cajas";	
					fcLogin.getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/login.faces");
					return;
				} 
				
				m.put("lstCajas", datosCaja.get(0)  );
				m.put("lstComboCajas", datosCaja.get(1) );
				 
				fcLogin.getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/loginCajaActual.faces");
				strMsgLogin = "";
				return;
				
			}
			
			else {
				
				lstCajas = cajasCtrl.getAllCajas();
				lstComboCajas = new ArrayList<SelectItem>();
				
				for (Vf55ca01 v : lstCajas) {
					
					v.getId().setCaname( CodeUtil.capitalize( v.getId().getCaname() ) );
					
					lstComboCajas.add( new SelectItem(
							v.getId().getCaid() +"-"+ v.getId().getCaco(), 
							v.getId().getCaid() + "  : " + v.getId().getCaname().toLowerCase(),
							" Cajero Titular: " + v.getId().getCacatinom()+"") );
					
				}
				
				m.put("lstComboCajas", lstComboCajas);
				m.put("lstCajas",lstCajas);
				
				if(lstComboCajas != null && !lstComboCajas.isEmpty()){
					nhLogin.handleNavigation(fcLogin, "login", "login2");
					strMsgLogin = "";
				}else{
					strMsgLogin = "No se encontró la información de cajas";	
					fcLogin.getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/login.faces");
				}
				
			}
			
		}catch(Exception ex){
			strMsgLogin = "No se puede establecer la conexión";
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/login.faces");
			} catch (Exception e2) {}
		}finally{
			   try{
				   if(cnAs400!= null && !cnAs400.isClosed())
				   cnAs400.close(); 
				   }catch(Exception ex1){ 
					   System.out.println("Error en cierre de conexion AS400 en clase LoginAction.java");}
		}
	}	
/*****************************VALIDAR LOGIN********************************************************************/
	public boolean validarLogin(Connection cn400){
		boolean validado = true;
		@SuppressWarnings("rawtypes")
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		String sMensaje = "";
		try{
			if(m.get("msgConnection") != null) {
				sMensaje = (String)m.get("msgConnection");

			}
				
			if(sMensaje.equals("The application server rejected the connection. (Password is expired.)")){
				String urlAdd = "http://"+PropertiesSystem.IPSERVERDB2+":2060/webaccess/iWAChgPwd?user=" + uiUsuario.getValue().toString() + "&expired=true&referer-uri=%2fwebaccess%2fiWAHome";
				
				FacesContext context = FacesContext.getCurrentInstance();   
	        	HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();  
				try {
					
					response.sendRedirect(urlAdd);
					validado = false;
				} catch (IOException e1) {							
					e1.printStackTrace();
				}					
			}else if(sMensaje.equals("The application server rejected the connection. (User ID is not known.)")) {			
				validado = false;
				strMsgLogin = "Credenciales incorrectas";				
			} else if(sMensaje.equals("The application server rejected the connection. (User ID is disabled.)")) {				
				strMsgLogin = "Usuario deshabilitado, consulte a informática";		
				validado = false;
			} else if(sMensaje.equals("The application server rejected the connection. (Password length is not valid.)")) {				
				strMsgLogin = "Credenciales incorrectas";
				validado = false;
			} else if(sMensaje.equals("The application server rejected the connection. (Password is incorrect.)")) {				
				strMsgLogin = "Credenciales incorrectas";
				validado = false;
			} else if(sMensaje.equals("")){				
				validado = true;
			}else {	
				strMsgLogin = "Error de conexión!!!";
				validado = false;
			}
		}catch(Exception ex){
			System.out.print("Se capturo una excepcion en LoginAction.validarLogin: " + ex);
		}
		return validado;
	}
	
/*****************************VALIDAR LOGIN MICROSOFT SQL SERVER **********************************************/
	//Objetivo de parámetro cnMSqlServer ????
		public boolean validarLoginMSqlServer(Connection cnMSqlServer){
			boolean validado = false;
			@SuppressWarnings("rawtypes")
			Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
			
			String sMensaje = "";
			
			try{
				if(m.get("msgConnectionMSqlServer") != null) {
					sMensaje = (String)m.get("msgConnectionMSqlServer");				
				}
				if (sMensaje.equals("")) {
					validado = true;				
				} else {
					validado = false;
					strMsgLogin = "Credenciales incorrectas/Autenticación de Windows Falló!!";
				}				
			}catch(Exception ex){
				System.out.print("Se capturo una excepcion en LoginAction.validarLoginMSqlServer: " + ex.getMessage());
			}
			return validado;
		}

/**********************ACTION LISTENER DEL LINK DE CAJA*********************************/
	@SuppressWarnings("unchecked")
	public void loginCajaActual(ActionEvent e){
		@SuppressWarnings("rawtypes")
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		Vautoriz[] vAut = null;	
		List<Vf55ca01> lstCajas = new ArrayList<Vf55ca01>();
		FacesContext fcLogin = FacesContext.getCurrentInstance();
		NavigationHandler nhLogin = fcLogin.getApplication().getNavigationHandler();
		String sNombre = "";
		F55ca014[] f14 = null;
		
		Vf55ca01 caja = null;
		
		try{
			RowItem ri = (RowItem) e.getComponent().getParent().getParent();
			caja = (Vf55ca01) DataRepeater.getDataRow(ri);
			
			m.put("sDisplayInicio", "true"); //Nuevo x Caja en Pockets.
			m.remove("sqlStateMSqlServer");
			m.remove("msgConnectionMSqlServer"); //Nuevo x Caja en Pockets. Luego en comentario porque no debe ir en crp.
			vAut = (Vautoriz[])m.get("sevAut");

			lstCajas =  new CtrlCajas().obtenerCajaxXCodigo(caja.getId().getCaid(), caja.getId().getCaco());

			String sCajaId = String.valueOf(((Vf55ca01)lstCajas.get(0)).getId().getCaid());
			String sNombreSucursal = ((Vf55ca01)lstCajas.get(0)).getId().getCaconom();
			
			//obtener companias x caja
			f14 = new CompaniaCtrl().obtenerCompaniasxCaja(((Vf55ca01)lstCajas.get(0)).getId().getCaid());
			m.put("cont_f55ca014",f14);
			
			sNombre =  new EmpleadoCtrl().buscarEmpleadoxCodigo(vAut[0].getId().getCodreg());
			if(sNombre == null || sNombre.equals("")){
				sNombre = vAut[0].getId().getLogin();
			}
			m.put("sNombreEmpleado", sNombre);
			m.put("iCodCajero", vAut[0].getId().getCodreg());
			m.put("sCajaId", sCajaId);
			m.put("sNombreSucursal",sNombreSucursal);
			m.put("sImpresora", ((Vf55ca01)lstCajas.get(0)).getId().getCaprnt());
			m.put("lstCajas",lstCajas);
			strMsgLogin = "";
			m.remove("lstComboCajas");
			m.remove("sqlState");


		
			boolean bloqueada= 	CtrlCajas.bloquearCaja(caja.getId().getCaid());
			if(bloqueada) {					
				System.out.println("La caja "+caja.getId().getCaid()+" de encuentra bloqueda!!");
					
			}
			
			//Nuevo x Caja en Pockets.
			caja = (Vf55ca01)lstCajas.get(0);
			caja.getId().getCaprnt().trim();
					
			/*Mandar a  la cookie los valores para Angular*/
			try {
				
				//List lstCajas = (List) JsfUtil.getObjectFromSessionMap("lstCajas");
				Vf55ca01 lstCajasCustom = caja;
				
				String nCaja = lstCajasCustom.getId().getCaid()+ "-" +lstCajasCustom.getId().getCaname();
				String nSucursal= lstCajasCustom.getId().getCaconom();
				
				
				
				
				FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("NombreCaja", nCaja,null);
				FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("NombreSucursal",  nSucursal,null);
				FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("Cajero",  CodeUtil.capitalize( vAut[0].getId().getEmpnombre().trim() ),null);
			
			}catch(Exception e5) {}
			
				HttpServletResponse response = (HttpServletResponse) 
							fcLogin.getExternalContext().getResponse();
				response.setContentType("text/html; charset=UTF-8");
				response.setHeader("Cache-Control", "no-cache");
				response.setHeader("Cache-Control", "no-store");
				response.setHeader("Cache-Control", "must-revalidate");
				response.setHeader("Pragma", "no-cache");
				response.setDateHeader("Expires", 0);
				
				
				fcLogin.getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/main.faces");
			
		}catch(Exception ex){
			ex.printStackTrace();
			nhLogin.handleNavigation(fcLogin, "loginCajaActual", null);
		}
	}
/*************************PROPIEDADES DEL BEAN*************************************/
	public HtmlInputSecret getUiContrasenia() {
		return uiContrasenia;
	}
	public void setUiContrasenia(HtmlInputSecret uiContrasenia) {
		this.uiContrasenia = uiContrasenia;
	}
	public HtmlInputText getUiUsuario() {
		return uiUsuario;
	}
	public void setUiUsuario(HtmlInputText uiUsuario) {
		this.uiUsuario = uiUsuario;
	}
	public String getStrMsgLogin() {
		return strMsgLogin;
	}
	public void setSMsgLogin(String msgLogin) {
		strMsgLogin = msgLogin;
	}
	public HtmlDropDownList getCmbCajas() {
		return cmbCajas;
	}
	public void setCmbCajas(HtmlDropDownList cmbCajas) {
		this.cmbCajas = cmbCajas;
	}
	@SuppressWarnings("rawtypes")
	public List getLstComboCajas() {
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		lstComboCajas = (List) m.get("lstComboCajas");
		return lstComboCajas;
	}
	@SuppressWarnings("rawtypes")
	public void setLstComboCajas(List lstComboCajas) {
		this.lstComboCajas = lstComboCajas;
	}
	@SuppressWarnings("rawtypes")
	public List getLstCajasEncontradas() {
		try {
			lstCajasEncontradas = new ArrayList();
			lstCajasEncontradas = (List) m.get("lstCajas");
		} catch (Exception ex) {
			System.out.print("Se capturo una excepcion en FacContadoDAO.getLstHfacturasContado: "+ ex);
		}
		return lstCajasEncontradas;
	}
	@SuppressWarnings("rawtypes")
	public void setLstCajasEncontradas(List lstCajasEncontradas) {
		this.lstCajasEncontradas = lstCajasEncontradas;
	}
	public HtmlGridView getGvCajas() {
		return gvCajas;
	}
	public void setGvCajas(HtmlGridView gvCajas) {
		this.gvCajas = gvCajas;
	}	
	@SuppressWarnings("deprecation")
	public void borrarDescargasExports(String sNombreCarpeta){
		try {
			HttpServletRequest sHttpRqst  = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
			String sRutaCarpeta = sHttpRqst.getRealPath(File.separatorChar+sNombreCarpeta);
			File directorio = new File(sRutaCarpeta);
			if(!directorio.exists()){
				directorio.mkdirs();
			}else{
				String [] sListaArchivos = directorio.list();
				if(sListaArchivos == null || sListaArchivos.length == 0)
					return;
				for (String sArchivo : sListaArchivos) {
					if(sArchivo.contains(".xls")){
						File archivo = new File(sRutaCarpeta +File.separatorChar+ sArchivo);if(archivo.exists()) archivo.delete();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	@SuppressWarnings({ "deprecation" })
	public void borrarMinutasExist(String sCarpeta, String sCoduser) {
		try {
			HttpServletRequest sHttpRqst = (HttpServletRequest) FacesContext
					.getCurrentInstance().getExternalContext().getRequest();

			String sRutaCarpeta = sHttpRqst.getRealPath(File.separatorChar
					+ sCarpeta);
			File carpeta = new File(sRutaCarpeta);

			String[] sListaArchivos = carpeta.list();
			if (sListaArchivos == null || sListaArchivos.length == 0)
				return;
			for (String sArchivo : sListaArchivos) {

				new File(sRutaCarpeta + File.separatorChar + sArchivo);
				
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}


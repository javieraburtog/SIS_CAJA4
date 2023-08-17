package com.casapellas.navegacion;
/**
 * CASA PELLAS S.A.
 * Creado por.........: Juan Carlos Ñamendi Pineda
 * Fecha de Creación..: 05/02/2009
 * Última modificación: 06/03/2009
 * Modificado por.....:	Juan Carlos Ñamendi Pineda
 * 
 */
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;

public class LogoutAction {

	protected FacesContext fcLogout;
	private static final String strLogout = "logout";
	Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	
	public String logout() {
		try {
			
			fcLogout = FacesContext.getCurrentInstance();
			
			if(fcLogout == null || m == null) return strLogout;
			
			HttpSession session =  (HttpSession) fcLogout.getExternalContext().getSession(false);
			
			if(session == null) return strLogout;
			
			m.keySet().clear();
			m.entrySet().clear();
			
			session.invalidate();
			System.runFinalization();
			System.gc();	
			
		} catch (Exception e) {
				System.out.print("Se capturo una excepcion en LogoutAction.logout: " + e);
		}
		
	return strLogout;
	}
	
	/*************************************************************************************/
	public static void salir() {
		try {
			
			
			if(FacesContext.getCurrentInstance() == null) return;
			
			HttpSession session =  (HttpSession) FacesContext
									.getCurrentInstance().getExternalContext()
									.getSession(false);
			
			if(session != null )session.invalidate();
			
			Map m1 = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
			
			m1.keySet().clear();	
			m1.entrySet().clear();

			System.runFinalization();
			System.gc();	
			
			m1 = null;
			session = null;
			
		} catch (Exception e) {
				System.out.print("Error generado en LogoutAction.Salir:  "
							+new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a")
							.format( new Date()) + e);
				e.printStackTrace();
		}
	}
}


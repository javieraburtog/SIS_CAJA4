package com.casapellas.navegacion;
/**
 * CASA PELLAS S.A.
 * Creado por.........: Juan Carlos Ñamendi Pineda
 * Fecha de Creación..: 10/01/2009
 * Última modificación: 06/03/2009
 * Modificado por.....:	Juan Carlos Ñamendi Pineda
 * 
 */
import java.util.Map;

import javax.faces.application.NavigationHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.casapellas.util.PropertiesSystem;

public class PhaseTracker implements javax.faces.event.PhaseListener {

private static final long serialVersionUID = 1L;

	public void afterPhase(PhaseEvent event) {
		try {

			FacesContext fcListener = event.getFacesContext();
			
			// Check to see if they are on the login page.
			boolean loginPage = fcListener.getViewRoot().getViewId()
					.lastIndexOf("login") > -1 ? true : false;
					
			if (!loginPage && !loggedIn()) {
				LogoutAction.salir();
				event.getFacesContext().getExternalContext()
						.redirect("/"+PropertiesSystem.CONTEXT_NAME+"/login.faces");
				
			}
			
		} catch (Exception se) { 
			se.printStackTrace();
		}
	}

	public void beforePhase(PhaseEvent event) {
		
		try {
			
			//&& ==== Validar que la sesion no este activa en el navegador.
			if (FacesContext.getCurrentInstance() == null) {
				
				ExternalContext ec =  event.getFacesContext().getExternalContext();
				ec.redirect("/"+PropertiesSystem.CONTEXT_NAME+"/login.faces");
				ec.getSessionMap().entrySet().clear();
				ec.getSessionMap().keySet().clear();
				
				HttpSession session = (HttpSession)ec.getSession(false);
				session.invalidate();
				
			} 
			
		} catch (Exception ex) { 
			ex.printStackTrace();
		}
	}
public PhaseId getPhaseId() {
	return PhaseId.RESTORE_VIEW;
}

	private boolean loggedIn() {
		String strIsLoggedIn;
		boolean bIsLoggedIn = false;
		Map m = FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap();
		try {
			if (m.containsKey("seBIsLoggedIn")) {
				strIsLoggedIn = (String) m.get("seBIsLoggedIn");
				if (strIsLoggedIn.equals("true"))
					bIsLoggedIn = true;
				else
					bIsLoggedIn = false;
			} else {
				bIsLoggedIn = false;
			}
		} catch (Exception ex) {
			System.out.print("Se capturo una excepcion en " +
					"PhaseTracker.loggedIn: "+ ex);
		}
		return bIsLoggedIn;
	}
}
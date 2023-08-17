/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.casapellas.util;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;

/**
*
* CASA PELLAS S.A.
* Autor:  Francisco Landeros.
* Fecha Creación: Mar 6, 2014 
* Descripción:  Clase utilitaria para beans jsf.
* 
*/
public class JsfUtil {
	
	public static SelectItem[] getSelectItems(List<?> entities,
			boolean selectOne) {
		int size = selectOne ? entities.size() + 1 : entities.size();
		SelectItem[] items = new SelectItem[size];
		int i = 0;
		if (selectOne) {
			items[0] = new SelectItem("", "---");
			i++;
		}
		for (Object x : entities) {
			items[i++] = new SelectItem(x, x.toString());
		}
		return items;
	}
	
	public static String findMonth(String idMonth) {
		String months[] = new String[] { "Enero", "Febrero", "Marzo", "Abril",
				"Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre",
				"Noviembre", "Diciembre" };
		try {
			return months[Integer.valueOf(idMonth.trim()).intValue() - 1];
		} catch (Exception e) {
			e.printStackTrace();
		}
		return idMonth;
	}

	public static void redirectPage(String url) {
		try {
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect(url.concat(".gcp"));
		} catch (IOException ex) {
			Logger.getLogger(JsfUtil.class.getName()).log(Level.SEVERE, null,
					ex);
		}
	}

	public static void addErrorMessage(Exception ex, String defaultMsg) {
		String msg = ex.getLocalizedMessage();
		if (msg != null && msg.length() > 0) {
			addErrorMessage(msg);
		} else {
			addErrorMessage(defaultMsg);
		}
	}

	public static void addErrorMessages(List<String> messages) {
		for (String message : messages) {
			addErrorMessage(message);
		}
	}

	public static void addErrorMessage(String msg) {
		FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
				msg, msg);
		FacesContext.getCurrentInstance().addMessage(null,facesMsg);
	}

	public static void addSuccessMessage(String msg) {
		FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO,
				msg, msg);
		FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
	}

	public static String getRequestParameter(String key) {
		return FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get(key);
	}

	public static Object getObjectFromRequestParameter(
			String requestParameterName, Converter converter,
			UIComponent component) {
		String theId = JsfUtil.getRequestParameter(requestParameterName);
		return converter.getAsObject(FacesContext.getCurrentInstance(),
				component, theId);
	}

	public static boolean sessionMapContainKey(String key) {
		return FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().containsKey(key);
	}

	public static void removeFromSessionMapContainKey(String key) {
		if (sessionMapContainKey(key)) {
			removeObjectFromSessionMap(key);
		}
	}

	public static Object addObjectToSessionMap(String key, Object value) {
		return FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().put(key, value);
	}

	public static Object getObjectFromSessionMap(String key) {
		return FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get(key);
	}

	public static void removeObjectFromSessionMap(String key) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.remove(key);
	}
	
}

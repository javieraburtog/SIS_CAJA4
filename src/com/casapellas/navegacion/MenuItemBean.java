package com.casapellas.navegacion;
/**
 * CASA PELLAS S.A.
 * Creado por.........: Juan Carlos Ñamendi Pineda
 * Fecha de Creación..: 06/01/2009
 * Última modificación: 06/03/2009
 * Modificado por.....:	Juan Carlos Ñamendi Pineda
 * 
 */
import java.util.List;

/**
 *	Represents a very simple model of a menu item
 */
public class MenuItemBean {
	private String seccion;
	private String icono;
	private List menuItems; //Secciones o Items del Menu
	
	public MenuItemBean() {
		super();
	}
	public MenuItemBean(String seccion,String icono)  {
		this.seccion = seccion;
		this.icono = icono;
	}		
	public MenuItemBean(String seccion)  {
		this.seccion = seccion;
		this.icono = "/theme/icons/blank.gif";
	}		
	public String getSeccion() {
		return seccion;
	}
	public void setSeccion(String string) {
		seccion = string;
	}
	public String getIcono() {
		return icono;
	}
	public void setIcono(String string) {
		this.icono = string;
	}

	public List getMenuItems() {
		if (menuItems == null)
			menuItems = new java.util.ArrayList(); //Collections.EMPTY_LIST;
		return menuItems;
	}
	public void setMenuItems(List list) {
		menuItems = list;
	}
}





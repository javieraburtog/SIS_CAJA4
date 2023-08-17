/*
 * Created on Aug 3, 2007
 */
package com.casapellas.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * CASA PELLAS S.A.
 * Creado por.........: Juan Carlos Ñamendi Pineda
 * Fecha de Creación..: 28/02/2009
 * Última modificación: 06/03/2009
 * Modificado por.....:	Juan Carlos Ñamendi Pineda
 * 
 */
public class JulianToCalendar {
	private Calendar date;

	public JulianToCalendar(int iFecha) {
		//		date = Calendar.getInstance();
		//		date.set(Calendar.DAY_OF_YEAR, j);

		String sFecha = "000000" + iFecha;
		sFecha = sFecha.substring(sFecha.length() - 6);
		String sAnio = "";
		boolean lFlag = false;
		if (sFecha.substring(0, 1).equals("0")) {
			sAnio = "19" + sFecha.substring(1, 3);
			lFlag = true;
		} else if (sFecha.substring(0, 1).equals("1")) {
			sAnio = "20" + sFecha.substring(1, 3);
			lFlag = true;
		}
		if (lFlag){
		int iAnio = Integer.parseInt(sAnio);
		String sDias = sFecha.substring(3);
		int iDias = Integer.parseInt(sDias);

		date = Calendar.getInstance();
		date.set(Calendar.YEAR, iAnio);
		date.set(Calendar.MONTH, Calendar.JANUARY);
		date.set(Calendar.DAY_OF_MONTH, 1);
		date.add(Calendar.DATE, iDias - 1);
		}else{date = null;}
	}

	public String toString() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		return (formatter.format(date.getTime()));
	}

	/**
	 * @return Returns the date.
	 */
	public Calendar getDate() {
		return date;
	}
}
/*
 * Created on Oct 1, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.casapellas.util;
/**
 * CASA PELLAS S.A.
 * Creado por.........: Juan Carlos Ñamendi Pineda
 * Fecha de Creación..: 28/02/2009
 * Última modificación: 06/03/2009
 * Modificado por.....:	Juan Carlos Ñamendi Pineda
 * 
 */
import java.util.Calendar;

/**
 * @author jnamendi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class CalendarToJulian {
	//private Calendar date;
	int julian;

	public CalendarToJulian(Calendar c) {
		int days = c.get(Calendar.DAY_OF_YEAR);
		int anio = c.get(Calendar.YEAR);

		String sDias = "000" + days;
		sDias = sDias.substring(sDias.length() - 3);
		
		String sAnio = "" + anio;
		String sFirst3Digits = null;
		if (sAnio.substring(0,2).equals("19")){
		    sFirst3Digits = "0" + sAnio.substring(2,4);
		}else if(sAnio.substring(0,2).equals("20")){
			sFirst3Digits = "1" + sAnio.substring(2,4);
		}
		julian = Integer.parseInt(sFirst3Digits + sDias);
	}

	/**
	 * @return Returns the date.
	 */
	public int getDate() {
		return julian;
	}
}
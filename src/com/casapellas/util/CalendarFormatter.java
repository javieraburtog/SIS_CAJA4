/*
 * Created on Aug 3, 2007
 */
package com.casapellas.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author jnamendi
 */
public class CalendarFormatter {
	private Calendar date;
	/**
	 * @return Returns the calendarFormatted.
	 */
	public String getCalendarFormatted() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy");
		return (formatter.format(date.getTime()));
	}
	/**
	 * @param date The date to set.
	 */
	public void setDate(Calendar date) {
		this.date = date;
	}
}
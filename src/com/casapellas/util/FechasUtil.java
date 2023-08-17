package com.casapellas.util;
/**
 * CASA PELLAS S.A.
 * Creado por.........: Juan Carlos Ñamendi Pineda
 * Fecha de Creación..: 28/02/2009
 * Última modificación: 08/04/2010
 * Modificado por.....: Carlos Manuel Hernandez Morrison.
 * 
 */
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import com.casapellas.controles.ConsolidadoDepositosBcoCtrl;

public class FechasUtil {
	
	
	public static boolean esDiaLaboralHabil(Date fecha){
		boolean valido = true;
		
		try {
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(fecha);
			
			String sql ="select count(*) from gcpsiseva2.workcalendar"+
					" where '"+ new SimpleDateFormat("yyyy-MM-dd").format(fecha) + "'  between startdate and enddate " +
					" and plan = '" + cal.get(Calendar.YEAR) + "' "+
					" and clocal = 'Nacional' and trim(Ccountry) = 'NI' " ;
			
			 @SuppressWarnings("unchecked")
			List<Integer> existen = (ArrayList<Integer>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, true, null);
			
			if( existen == null || existen.isEmpty() || existen.get(0) == 0 ){
				return valido;
			}
			
			valido = false;
			
			
		} catch (Exception e) {
			 e.printStackTrace();
		}
		return valido;
	}
	
	
	
	public static int[] obtenerPeriodoFiscalActual(String codcomp){
		int[] periodofiscal = null;
		
		try {
			
			//String strSql = " select ccpnc, ccdff from "+PropertiesSystem.JDEDTA+".f0010 where ccco = '00000' " ;
			
			
			String strSql = " select ccpnc, substr(ccdfyj, 2, 2) from "+PropertiesSystem.JDEDTA+".f0010 where ccco = '00000' " ;
			
			@SuppressWarnings("unchecked")
			List<Object[]> dtaPeriodoFiscal = (ArrayList<Object[]>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSql, true, null);
			
			if(dtaPeriodoFiscal == null )
				return null;
			
			return periodofiscal = new int[]{
				Integer.parseInt( String.valueOf( dtaPeriodoFiscal.get(0)[0] ) ),
				Integer.parseInt( String.valueOf( dtaPeriodoFiscal.get(0)[1] ) )
			};
			
			
		} catch (Exception e) {
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
			periodofiscal = new int[]{99,999};
		}	
		return periodofiscal;
	}
	
	
	
	public static Date removeTimeToDate(Date datewithtime){
		Calendar calendar = Calendar.getInstance();
	    calendar.setTime( datewithtime );
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);
	    return calendar.getTime();
	}
	
	
	/**
     * Funcion que convierte un Calendar a fecha juliana entera
     *
     * @param calendar Parametro Calendar que contiene la fecha a convertir
     * @return Retorno de fecha en valor juliano
     */
    public static int calendarToJulian(Calendar calendar) {
        if (calendar != null) {
            int year = calendar.get(Calendar.YEAR) - 1900;
            int month = calendar.get(Calendar.DAY_OF_YEAR);
            return Integer.parseInt(String.format("%03d%03d", year, month));
        }
        return 0;
    }

    /**
     * Funcion que convierte un Date a fecha juliana entera
     *
     * @param date Parametro Date que contiene la fecha a convertir
     * @return Retorno de fecha en valor juliano
     */
    public static int dateToJulian(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return calendarToJulian(cal);
    }

    /**
     * Funcion que convierte una fecha juliana entera a Calendar
     *
     * @param julian Parametro entero que contiene el valos juliano
     * @return Retorno de Calendar conteniendo la fecha juliana del parametro
     */
    public static Calendar julianToCalendar(int julian) {
        String sJulian = String.format("%06d", julian);
        int[] yeardate = new int[]{Integer.parseInt(sJulian.substring(0, 3)) + 1900, Integer.parseInt(sJulian.substring(3, 6))};
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, yeardate[0]);
        calendar.set(Calendar.DAY_OF_YEAR, yeardate[1]);
        return calendar;
    }
    /**
     * Funcion que convierte una fecha juliana entera a tipo Date
     * @param julian Parametro entero que contiene el valos juliano
     * @return Retorno de Date conteniendo la fecha juliana del parametro
     */
    public static Date julianToDate(int juliandate){
    	Calendar cal = julianToCalendar(juliandate);
    	return cal.getTime();
    }
	
	public static String diferenciaEntreHoras(Date inicia, Date finaliza) {

		String mensaje = new String("");

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
			long diffSeconds = (finaliza.getTime() - inicia.getTime()) / 1000 % 60;
			long diffMinutes = (finaliza.getTime() - inicia.getTime())
					/ (60 * 1000) % 60;

			mensaje = "[" + sdf.format(inicia) + " <-> " + sdf.format(finaliza)
					+ "]: Transcurrido: [" + diffMinutes + " Mins : "
					+ diffSeconds + " Segs]";

		} catch (Exception e) {
			e.printStackTrace();
			mensaje = "";
		}
		return mensaje;
	}
/******************************************************************************************/
/** Método: restar o sumar fechas a un objeto date.
 *	Fecha:  17/08/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public static Date quitarAgregarDiasFecha(int iDias,Date dtFechaActual){
		Date dtNuevaFecha = new Date();
		try {
			Calendar cFechaActual = Calendar.getInstance();
			cFechaActual.setTime(dtFechaActual);
			cFechaActual.add(Calendar.DATE, iDias);
			dtNuevaFecha = cFechaActual.getTime();
		} catch (Exception e) {
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
			dtNuevaFecha = new Date();
		}
		return dtNuevaFecha;
	}
/************************************************************************************/
/**  Obtiene la diferencia de días entre dos fechas sin tomar en cuenta el domingo	*/ 	
	public static int obtieneCantDiasHabiles(Date dtFechaIni,Date dtFechaFin){
		int iCantdias=0;
		Calendar calIni = Calendar.getInstance();
		
		try {
			if(dtFechaIni.compareTo(dtFechaFin) >0){
				Date dtTemp = dtFechaIni;
				dtFechaIni = dtFechaFin;
				dtFechaFin = dtTemp;
			}
			calIni.setTime(dtFechaIni);
			while (dtFechaIni.compareTo(dtFechaFin) <= 0) {
				if(calIni.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
					iCantdias++;
				calIni.add(Calendar.DATE, 1);
				dtFechaIni = calIni.getTime();
			}
		} catch (Exception error) {
			iCantdias =0;
			System.out.println("Error en FechasUtil.obtieneCantDiasHabiles " + error);
		}	
		return iCantdias;
	}
/*********************************************************************************/
/** 	Obtener la diferencia de días entre dos fechas del mismo año a partir de 
 * 		objetos Date usando Objetos Date en las operaciones			 		   	 */	
	public static int obtenerDiasEntreFechas(Date dtIni, Date dtFin){
		int iDias = 0;
		
		try {
			long diferencia= ( dtFin.getTime() - dtIni.getTime() );
			iDias = (int)diferencia/(1000*60*60*24);
			
		} catch (Exception error) {
			System.out.println("Error en FechasUtil.ObtenerDiasEntreFechas " + error);
		}
		return iDias;
	}
/*********************************************************************************/
/** 	Obtener la diferencia de días entre dos fechas, a partir de objetos Date y 
 * 		Usando Objetos GregorianCalendar en las operaciones			 		     */	
	public static int obtenerDiferenciaDias(Date dtIni,Date dtFin){
		GregorianCalendar gcFechaIni = new GregorianCalendar();
		GregorianCalendar gcFechaFin = new GregorianCalendar();
		gcFechaIni.setTime(dtIni);
		gcFechaFin.setTime(dtFin);
		int iDias = 0;
    
		try{

		    /* COMPROBAMOS SI ESTAMOS EN EL MISMO AÑO */
		    if (gcFechaIni.get(Calendar.YEAR) == gcFechaFin.get(Calendar.YEAR)) {
		    	iDias = gcFechaFin.get(Calendar.DAY_OF_YEAR) - gcFechaIni.get(Calendar.DAY_OF_YEAR);
		    } else {
		        /* SI ESTAMOS EN DISTINTO AÑO COMPROBAMOS QUE EL AÑO DEL DATEINI NO SEA BISIESTO
		         * SI ES BISIESTO SON 366 DIAS EL ANÑO
		         * SINO SON 365
		         */
		        int diasAnyo = gcFechaIni.isLeapYear(gcFechaIni.get(Calendar.YEAR)) ? 366 : 365;
		
		        /* CALCULAMOS EL RANGO DE ANYOS */
		        int rangoAnyos = gcFechaFin.get(Calendar.YEAR) - gcFechaIni.get(Calendar.YEAR);
		
		        /* CALCULAMOS EL RANGO DE DIAS QUE HAY */
		        iDias = (rangoAnyos * diasAnyo) + (gcFechaFin.get(Calendar.DAY_OF_YEAR) - gcFechaIni.get(Calendar.DAY_OF_YEAR));
		    }
		}catch(Exception error){
//			System.out.println("Error en FechasUtil.obtenerDiferenciaDias "+error);
			error.printStackTrace();
		}
	    return iDias;
	}
	
	public static int diferenciaMeses(Date fechaini, Date fechafin){
		
		Calendar startCalendar = new GregorianCalendar();
		startCalendar.setTime(fechaini);
		Calendar endCalendar = new GregorianCalendar();
		endCalendar.setTime(fechafin);

		int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
		
		return  diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
		
	}
	
	
/*********************************************************************************/
/** 	Retorna un entero de la hora tipo HHmmss a partir de un objeto Date()    */
		public int formatHoratoInt(Date dtHora){
			int iHora = 0;			
			SimpleDateFormat dfHora;
			String sHoraI, sHini[];
			
			try {
				dfHora = new SimpleDateFormat("HH:mm:ss");
				sHoraI = dfHora.format(dtHora);
				sHini  = sHoraI.split(":");
				iHora  = Integer.parseInt(sHini[0]+""+sHini[1]+""+sHini[2]);
			} catch (Exception error) {
				iHora = 0;
				System.out.println("Error en FechasUtil.formatHoratoInt " + error);
			}
			return iHora;
		}
/*********************************************************************************************/
/** 	Convierte un objeto Date() a String en un formato pasado como parámetro			    */
		public static String formatDatetoString(Date dtHora, String sPatron){
			String sHora = "";
			SimpleDateFormat dfHora;
			
			try {
				dfHora = new SimpleDateFormat(sPatron,new Locale("es","ES"));
				sHora  = dfHora.format(dtHora);				
			} catch (Exception error) {
				sHora = "";
				System.out.println("Error en FechasUtil.formatDatetoString " + error);
			}
			return sHora;
		}
	
	/*********************************************************************************/
	public static int DateToJulian(Date dFecha){
		int iFecha = 0;
		Calendar cFecha = Calendar.getInstance();
		try{			
			cFecha.setTime(dFecha);		
			CalendarToJulian fecha = new CalendarToJulian(cFecha);
			iFecha = fecha.getDate();
			
			fecha = null;
			cFecha = null;
			
		}catch(Exception ex){
//			LogCrtl.imprimirError(ex);
			ex.printStackTrace();
		}
		return iFecha;
	}
/**RETORNA LA FECHA EN FORMATO JULIANO DE UN STRING CON EL FORMATO dd/MM/yyyy **/
	public int StringToJulian(String sFecha){
		int iFecha = 0;
		Date date;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Calendar cFecha = Calendar.getInstance();
		try{
			date = (Date)sdf.parse(sFecha);
			cFecha.setTime(date);		
			CalendarToJulian fecha = new CalendarToJulian(cFecha);
			iFecha = fecha.getDate();
		}catch(Exception ex){
			System.out.println("Se capturo una excepcion en FechasUtil.StringToJulian: " + ex);
		}
		return iFecha;
	}
	/**RETORNA LA FECHA JULIANA DE UN TIPO CALENDAR */
	public int CalendarToJulian(Calendar c) {
		int julian = 0;
		try{
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
		}catch(Exception ex){
			System.out.println("Se capturo una excepcion en FechasUtil.CalendarToJulian: " + ex);
		}
		return julian;
	}
/**CONVIERTE UNA FECHA EN FORMATO JULIANO AL TIPO CALENDAR **/
	public Calendar JulianToCalendar(int iFecha) {
		String sFecha = "000000" + iFecha;
		sFecha = sFecha.substring(sFecha.length() - 6);
		String sAnio = "";
		Calendar date = null;
		boolean lFlag = false;
		try{
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
		}catch(Exception ex){
			System.out.println("Se capturo una excepcion en FechasUtil.JulianToCalendar: " + ex);
		}
		return date;
	}
/*********************************************************************************************************************/	
	public int obtenerFechaActualJuliana(){
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		int iFechaActual = 0;
		try{
//			obtener fecha actual y convertirla a formato juliano
			Calendar calFechaActual = Calendar.getInstance();
			String sFechaActual = calFechaActual.get(Calendar.DATE)+ "/" + (calFechaActual.get(Calendar.MONTH)+ 1) + "/" + calFechaActual.get(Calendar.YEAR);
			calFechaActual.setTime(new Date(sdf.parse(sFechaActual).getTime()));
			CalendarToJulian julian = new CalendarToJulian(calFechaActual);
			iFechaActual = julian.getDate();	
		}catch(Exception ex){
			System.out.println("Se capturo una excepcion en FacturaCtrl.obtenerFechaActualJuliana");
		}
		return iFechaActual;
	}
	/*********************************************************************************************************************/	
	public int obtenerFechaJulianaDia(Date dtFecha){
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		int iFechaActual = 0;
		try{
			Calendar calFechaActual = Calendar.getInstance();
			calFechaActual.setTime(dtFecha);
			String sFechaActual = calFechaActual.get(Calendar.DATE)+ "/" + (calFechaActual.get(Calendar.MONTH)+ 1) + "/" + calFechaActual.get(Calendar.YEAR);
			calFechaActual.setTime(new Date(sdf.parse(sFechaActual).getTime()));
			CalendarToJulian julian = new CalendarToJulian(calFechaActual);
			iFechaActual = julian.getDate();	
		}catch(Exception ex){
			System.out.println("Se capturo una excepcion en FacturaCtrl.obtenerFechaActualJuliana");
		}
		return iFechaActual;
	}
/*********************************************************************************************************************/
	/************************************
	 * @throws java.text.ParseException *
	 * **********************************/
	public  static Date getDateFromString(String sValue, String sFormat) {
		Date date = null;
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(sFormat, Locale.US);
			date = sdf.parse(sValue);
			
		} catch (Exception e) {
			 e.printStackTrace();
//			 LogCrtl.imprimirError(e);
			 e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * sDate: Formato YYYY-MM-dd.
	 */
	public static Date ConvertToDate2(String sDate){
		Date currentDate = null;
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
		
		try {
			currentDate = formatDate.parse(sDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return currentDate;
	}

	/**
	 * @Return: Formato YYYY-MM-DD
	 */
	public static String getDateToString(Date d)
	{	
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(d);
	}
	
	public static Date addMinutesToDate(Date date, int minutes) {  
	       Calendar calendarDate = Calendar.getInstance();  
	       calendarDate.setTime(date);  
	       calendarDate.add(Calendar.MINUTE, minutes);  
	       return calendarDate.getTime();  
	   } 
	
	
	public static Date addDaysToDate(Date date, int days) {  
	       return addMinutesToDate(date, 60 * 24 * days);  
	   }
}

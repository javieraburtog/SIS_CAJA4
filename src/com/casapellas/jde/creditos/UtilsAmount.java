package com.casapellas.jde.creditos;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class UtilsAmount {

	public final static BigDecimal bigdecimal_100 = new BigDecimal("100");
	
	public static BigDecimal round( String strNumber ) {
		
		return new BigDecimal( strNumber.replace(",", "") ).setScale(2, RoundingMode.HALF_UP) ;
		
	}
	
	public static boolean numberInString(String value) {
		return value.trim().matches("^[0-9]+\\.?[0-9]*$");
	}
	
}

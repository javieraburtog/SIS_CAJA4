package com.casapellas.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.component.UIComponentBase;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;

import com.infragistics.faces.shared.smartrefresh.SmartRefreshManager;

public class CodeUtil {

	private static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern
			.compile(
					"^[_A-Za-z0-9-\\\\+]+(\\\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\\\.[A-Za-z0-9]+)*(\\\\.[A-Za-z]{2,})$",
					Pattern.CASE_INSENSITIVE);

	public static boolean validate_email_address(String emailStr) {
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
		return matcher.find();
	}
	
	public static String toStringFormatAmountType(Object valuetoformat){
		return String.format(PropertiesSystem.AMOUNT_FORMAT_STRING, valuetoformat);
	}
	
	public static String pad(String value, int length) {
	    return pad(value, length, " ");
	}
	public static String pad(String value, int length, String with) {
	    StringBuilder result = new StringBuilder(length);
	    result.append(value);

	    while (result.length() < length) {
	        result.insert(0, with);
	    }
	    return result.toString();
	}
	
	
	public static String capitalize(String str) {

		char[] delimiters = null;

		int delimLen = (delimiters == null ? -1 : delimiters.length);
		if (str == null || str.length() == 0 || delimLen == 0) {
			return str;
		}

		str = str.trim();
		str = str.toLowerCase();
		int strLen = str.length();
		StringBuffer buffer = new StringBuffer(strLen);
		boolean capitalizeNext = true;
		for (int i = 0; i < strLen; i++) {
			char ch = str.charAt(i);

			if (isDelimiter(ch, delimiters)) {
				buffer.append(ch);
				capitalizeNext = true;
			} else if (capitalizeNext) {
				buffer.append(Character.toTitleCase(ch));
				capitalizeNext = false;
			} else {
				buffer.append(ch);
			}
		}
		return buffer.toString();
	}
	
    public static String capitalize(String str, char[] delimiters) {
        int delimLen = (delimiters == null ? -1 : delimiters.length);
        if (str == null || str.length() == 0 || delimLen == 0) {
            return str;
        }
        
        str = str.toLowerCase();
        int strLen = str.length();
        StringBuffer buffer = new StringBuffer(strLen);
        boolean capitalizeNext = true;
        for (int i = 0; i < strLen; i++) {
            char ch = str.charAt(i);

            if (isDelimiter(ch, delimiters)) {
                buffer.append(ch);
                capitalizeNext = true;
            } else if (capitalizeNext) {
                buffer.append(Character.toTitleCase(ch));
                capitalizeNext = false;
            } else {
                buffer.append(ch);
            }
        }
        return buffer.toString();
    }
    private static boolean isDelimiter(char ch, char[] delimiters) {
        if (delimiters == null) {
            return Character.isWhitespace(ch);
        }
        for (int i = 0, isize = delimiters.length; i < isize; i++) {
            if (ch == delimiters[i]) {
                return true;
            }
        }
        return false;
    }
	
	
	
	public static <E> List<?> selectPropertyListFromEntity(Collection<?> from, String propertyName, boolean useDistinct) {
		List<Object> result = new ArrayList<Object>();
		
		try {
			for(Object o : from) {
				if(o == null ) 
					continue;
				
				Object value = PropertyUtils.getSimpleProperty(o, propertyName);
			
				if(result.contains(value) && useDistinct) 
					continue;
				
				result.add(value);
			}
		} catch (Exception e) {
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
		}
		return result;
	}
	
	
	public static BigDecimal sumPropertyValueFromEntityList(Collection<?> from, String propertyName, boolean useDistinct) {
		BigDecimal total = BigDecimal.ZERO;
		
		try {
		
			for(Object o : from) {
				
				if(o == null ) 
					continue;
				
				Object value = PropertyUtils.getSimpleProperty(o, propertyName);
			
				if(value == null)
					continue;
				
				try {
					
					BigDecimal bd = new BigDecimal(String.valueOf(value));
					total = total.add( bd ) ;
					
				} catch (Exception e) {
					continue;
				}
			}
		} catch (Exception e) {
			total =  BigDecimal.ZERO ;
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
		}finally{
			
			if(total == null)
				total =  BigDecimal.ZERO ;
		}
		return total;
	}
	
	public static void removeFromSessionMap(String[] objectsNameInMap) {
		try {
			for (String varName : objectsNameInMap) {
				if( FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey(varName))
					FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(varName) ;
			}
		} catch (Exception e) {
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
		}
	}
	public static void removeFromSessionMap(String objectsNameInMap) {
		try {
			 
			if( FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey(objectsNameInMap))
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(objectsNameInMap) ;
		 
		} catch (Exception e) {
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
		}
	}
	public static void putInSessionMap(String varname, Object varvalue){
		try {
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(varname, varvalue ) ;
		} catch (Exception e) {
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
		}
	}
	public static Object getFromSessionMap(String varname) {
		Object ob = null;
		
		try {
			ob = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(varname) ;
		} catch (Exception e) {
			LogCajaService.CreateLog("getFromSessionMap", "ERR", e.getMessage());
		}
		return ob; 
	}
	
	public static HttpServletRequest  getCurrentRequest() {
		HttpServletRequest ob = null;
		
		try {
			ob = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		} catch (Exception e) {
			LogCajaService.CreateLog("getCurrentRequest", "ERR", e.getMessage());
		}
		return ob;
	}
	public static String getUserAgent() {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		return externalContext.getRequestHeaderMap().get("User-Agent");
	}
	
	public static void refreshIgObjects(Object[] igObjects){
		try {
			for (Object igComponent : igObjects) { 
				SmartRefreshManager.getCurrentInstance()
					.addSmartRefreshId(((UIComponentBase) igComponent)
					.getClientId(FacesContext.getCurrentInstance()));		
			}
		} catch (Exception e) {
			LogCajaService.CreateLog("refreshIgObjects", "ERR", e.getMessage());
		}
	}
	public static void refreshIgObjects(Object igObject){
		try {
			 
			SmartRefreshManager.getCurrentInstance()
				.addSmartRefreshId(((UIComponentBase) igObject)
				.getClientId(FacesContext.getCurrentInstance()));		
		 
		} catch (Exception e) {
			LogCajaService.CreateLog("refreshIgObjects", "ERR", e.getMessage());
		}
	}
	
	
	
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.casapellas.util.ens;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Francisco Landeros
 */
public class QueryUtils {

    public static boolean DEBUG_MODE = true;

    public QueryUtils() {
    }

    @SuppressWarnings("rawtypes")
	public static String constructStringHqlWhere(Class classType, boolean hasParams, Map<String, Object> mapParams) {
        StringBuilder sbSql = new StringBuilder("select o from " + classType.getSimpleName() + " o ");
        StringBuilder sbWhere = new StringBuilder();
        if (hasParams) {
            sbWhere.append("Where ");
            int i = 0, max = mapParams.size();
            for (Map.Entry<String, Object> entry : mapParams.entrySet()) {
                sbWhere.append(entry.getKey()).append(" = ").append(entry.getValue());
                if (i < max - 1) {
                    sbWhere.append(" and ");
                }
                i++;
            }
            sbSql.append(sbWhere);
        }
        printLog(sbSql.toString());
        return sbSql.toString();
    }

    @SuppressWarnings("rawtypes")
	public static String constructStringHqlWhereIn(Class classType, boolean hasParams, Map<String, Object> mapParams, boolean isChar) {
        StringBuilder sbSql = new StringBuilder("select o from " + classType.getSimpleName() + " o ");
        if (hasParams) {
            StringBuilder sbWhere = new StringBuilder(" where ");
            int i = 0, max = mapParams.size();
            for (Map.Entry<String, Object> entry : mapParams.entrySet()) {
                Object object = entry.getValue();
                sbWhere.append(entry.getKey());
                sbWhere.append(" in (");
                if (object instanceof List || object instanceof ArrayList) {                    
                    List lstObjects = (ArrayList) object;
                    for (Iterator it = lstObjects.iterator(); it.hasNext();) {
                        Object obj = it.next();
                        if (!isChar) {
                            sbWhere.append(obj);
                        } else {
                            sbWhere.append("'").append(obj).append("'");
                        }
                        if (it.hasNext()) {
                            sbWhere.append(",");
                        }
                    }                    
                } else {
                    if (!isChar) {
                        sbWhere.append(object);
                    } else {
                        sbWhere.append("'").append(object).append("'");
                    }
                }
                sbWhere.append(")");
                if (i < max - 1) {
                    sbWhere.append(" and ");
                }
                i++;
            }
            sbSql.append(sbWhere);
        }        
        printLog(sbSql.toString());
        return sbSql.toString();
    }

    public static void printLog(String log) {
        if (DEBUG_MODE) {
            System.out.println(log);
        }
    }
}

package com.casapellas.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import com.casapellas.entidades.F55ca017;
import com.casapellas.entidades.F55ca017Id;
import com.casapellas.entidades.Vrecibosxtipoingreso;

public class SqlUtil {
	
	public static double roundDouble(BigDecimal bigDecimal, int scale) {
        try {
            bigDecimal = bigDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP);
        } catch (Exception ex) {
            System.out.print("==> Excepción capturada en roundDouble: " + ex);
        }
        return bigDecimal.doubleValue();
    }
	
	@SuppressWarnings("unchecked")
	public static List<Vrecibosxtipoingreso> getFilterListVrecibosxtipoingreso(List<Vrecibosxtipoingreso> F55ca017s, final String filter, final char method) {
        return (List<Vrecibosxtipoingreso>) CollectionUtils.select(F55ca017s, new Predicate() {
           // @Override
            public boolean evaluate(Object o) {
                Vrecibosxtipoingreso vrecibosxtipoingreso = (Vrecibosxtipoingreso) o;
                if (method == '=') {
                    return vrecibosxtipoingreso.getId().getTiporec().trim().compareToIgnoreCase(filter) == 0;
                } else {
                    return vrecibosxtipoingreso.getId().getTiporec().trim().compareToIgnoreCase(filter) != 0;
                }
            }
        });
    }

	@SuppressWarnings("unchecked")
	public static List<F55ca017> getFilterListF55ca017(List<F55ca017> F55ca017s, final String filter, final char method) {
        return (List<F55ca017>) CollectionUtils.select(F55ca017s, new Predicate() {
//            @Override
            public boolean evaluate(Object o) {
                F55ca017 f55ca017 = (F55ca017) o;
                if (method == '=') {
                    return f55ca017.getId().getC7locn().trim().compareToIgnoreCase(filter) == 0;
                } else {
                    return f55ca017.getId().getC7locn().trim().compareToIgnoreCase(filter) != 0;
                }
            }
        });
    }

    public static Object[] getListFromFieldF55ca017(List<F55ca017> f55ca017s, String fieldName) {
        List<Object> lstReturn = new ArrayList<Object>();
        for (Iterator<F55ca017> it = f55ca017s.iterator(); it.hasNext();) {
            F55ca017Id f55ca017Id = it.next().getId();
            try {
                lstReturn.add(String.valueOf(getValueFromFieldObject(f55ca017Id, fieldName)).trim());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return lstReturn.toArray(new String[lstReturn.size()]);
    }

    /**
     * Funcion que obtiene el valor de un campo declarado dentro de un objeto
     * cualquiera
     *
     * @param object Objeto inicializado object != null que contiene los campos
     * con valor
     * @param fieldName Nombre del campo del que se requiere el valor
     * @return Objeto obtenido segun el nombre del campo
     */
    public static Object getValueFromFieldObject(Object object, String fieldName) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Funcion que construye el where una consulta sql a partir de listas de
     * parametros de cualquier tipo de dato, desde el inicio del where o
     * consulta de 'and' parcial
     *
     * @param isFirst Parametro que nos dice si es el primer parametro en la
     * consulta
     * @param field Nombre del campo el cual sera evaluado, columna de la tabla
     * @param typeOfString Tipo de dato en consulta el cual sera evaluado, int,
     * varchar etc...
     * @param values Arreglo de objetos el cual sera agregado a la consulta
     * @param sign Signo a evaluar en caso que sea un unico objeto, por defecto
     * sera null.
     * @return Parte de la consulta construida para concatenar con el select
     * inicial
     */
    public static String constructQuery(boolean isFirst, String field, boolean typeOfString, Object[] values, String sign) {
        StringBuilder sqlWhere = new StringBuilder();
        if (isFirst) {
            sqlWhere.append(" WHERE ");
        } else {
            sqlWhere.append(" AND ");
        }
        if (values != null) {
            if (values.length == 1) {
                sqlWhere.append(field).append(" ");
                if (sign != null) {
                    sqlWhere.append(sign).append(" ");
                } else {
                    sqlWhere.append("= ");
                }
                if (typeOfString) {
                    sqlWhere.append("'");
                }
                sqlWhere.append(values[0]);
                if (typeOfString) {
                    sqlWhere.append("'");
                }
            } else {
                sqlWhere.append(field).append(" ").append("IN ( ");
                for (int i = 0; i < values.length; i++) {
                    if (typeOfString) {
                        sqlWhere.append("'");
                    }
                    sqlWhere.append(values[i]);
                    if (typeOfString) {
                        sqlWhere.append("'");
                    }
                    if (i < (values.length - 1)) {
                        sqlWhere.append(",");
                    }
                }
                sqlWhere.append(" )");
            }
        }
        return sqlWhere.toString();
    }
}

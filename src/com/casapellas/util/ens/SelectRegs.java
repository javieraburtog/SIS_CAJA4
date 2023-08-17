/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.casapellas.util.ens;

import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.util.LogCajaService;

/**
 *
 * @author Francisco Landeros
 */
public class SelectRegs<T> {

    public SelectRegs() {
    }
    
    @SuppressWarnings("unchecked")
	public List<Object[]> selectNativeListViewJde(String sSql){
        List<Object[]> lstReturn = null;
        Session sesion = null;
        
        try {
            sesion = HibernateUtilPruebaCn.currentSession();                     
            lstReturn = sesion.createSQLQuery(sSql).list();
            
        } catch (Exception ex) {
           LogCajaService.CreateLog("selectNativeListViewJde", "ERR", ex.getMessage());
        } 
        return lstReturn;      
    }
    
    /**
     * Funcion de tipo generica para consultas a vistas y tabla del JDE
     * @param clase Clase que se desea consultar, equivalente a nombre de tabla
     * @param hasParams True si tiene parametros para comparar en el where
     * @param objects Mapa el cual contiene el nombre y valor por el que se filtrara
     * @return List<T> lista de datos gerericos.
     */
    @SuppressWarnings("unchecked")
	public List<T> selectListViewJde(@SuppressWarnings("rawtypes") Class clase, boolean hasParams, Map<String, Object> mapParams, boolean isIn, boolean isChar){
        List<T> lstReturn = null;
        Session sesion = null;
        try {
            sesion = HibernateUtilPruebaCn.currentSession();            
            String sbSql = new String();
            if(!isIn){
                sbSql = QueryUtils.constructStringHqlWhere(clase, hasParams, mapParams);
            } else {
                sbSql = QueryUtils.constructStringHqlWhereIn(clase, hasParams, mapParams, isChar);
            }            
            lstReturn = sesion.createQuery(sbSql.toString()).list();
           
        } catch (Exception ex) {
            LogCajaService.CreateLog("selectListViewJde", "ERR", ex.getMessage());
        }
        return lstReturn;      
    }
    
}

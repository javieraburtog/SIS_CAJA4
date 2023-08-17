/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.casapellas.util.ens;

import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.util.LogCajaService;
import java.util.List;
import org.hibernate.Session;

public class ENSUtilValidation {

    public ENSUtilValidation(){}
    
    public List<?> getAutorizProfile(String login, String nomcorto){
        Session session = HibernateUtilPruebaCn.currentSession();
        
        List<?> lstVautoriz = null;
        try {            
        	String hql = "FROM Vautoriz E WHERE E.id.login = :pLogin AND E.id.nomcorto=:pNomCorto";
            lstVautoriz = session.createQuery(hql)
                    .setParameter("pLogin", login.toUpperCase())
                    .setParameter("pNomCorto", nomcorto.toUpperCase())
                    .list();
                      
        } catch (Exception e) {
          LogCajaService.CreateLog("getAutorizProfile", "ERR", e.getMessage());
        }
        
        return lstVautoriz;
    }    
    
}

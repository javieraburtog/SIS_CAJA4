package com.casapellas.controles.tmp;

/**
 * CASA PELLAS S.A.
 * Creado por.........: Juan Carlos Ñamendi Pineda
 * Fecha de Creación..: 28/02/2009
 * Última modificación: 09/03/2011
 * Modificado por.....:	Carlos Manuel Hernández Morrison.
 * 
 */
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.casapellas.entidades.F55ca022;
import com.casapellas.entidades.F55ca033;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.util.LogCajaService;

public class BancoCtrl {
	
	
/******************************************************************************************/
/** Método: Buscar cuenta transitoria de banco, por moneda y compania. 
 *	Fecha:  20/12/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */	
	public F55ca033 obtenerCuentaTransitoriaBco(String sCodcomp, String sMoneda, int iIdBanco){
		F55ca033 f33 = null;
		Session sesion = null;
		
		try {
			
			sesion = HibernateUtilPruebaCn.currentSession();
			
			Criteria cr = sesion.createCriteria(F55ca033.class);
			cr.add(Restrictions.eq("id.b3codb", iIdBanco));
			cr.add(Restrictions.eq("id.b3crcd", sMoneda ));
			cr.add(Restrictions.eq("id.b3rp01", sCodcomp));
			
			f33 = (F55ca033)cr.uniqueResult();
			
			
			
			
		} catch (Exception e) {
			f33 = null;
			e.printStackTrace(); 
			
		}
		return f33;
	}
	
	public F55ca022[] obtenerBancosConciliar(){
		F55ca022[] banco = null;
	    Session session = null;
		try{
			session = HibernateUtilPruebaCn.currentSession();
			List result = (List) session
			.createQuery("from F55ca022 as f where f.id.estado = 'A' and f.id.conciliar = 1 order by f.id.orden ")
			.list();	
			banco = new F55ca022[result.size()];
			for(int i = 0; i < result.size(); i++){
				banco[i] = (F55ca022)result.get(i); 
			}
		}catch(Exception ex){
			ex.printStackTrace(); 
		}
		return banco;
	}
/***************************************************************************************/
/**			Obtener el registro de un banco a partir del id del banco				   */
	public F55ca022 obtenerBancoxId(int iBancoId, Session sesion){
		F55ca022 f = null;
		String sql = "";
		
		
		try {
			
			if(sesion == null){
				sesion = HibernateUtilPruebaCn.currentSession();
		
			}
			
			sql = "from F55ca022 f where f.id.codb = " + iBancoId;
			Object ob = sesion.createQuery(sql).uniqueResult();
			if(ob!=null)
				f = (F55ca022)ob;
			
		} catch (Exception error) {
			f = null;
			error.printStackTrace(); 
		}
		return f;
	}
/***************************************************************************************/
/**			Obtener el registro de un banco a partir del id del banco				   */
	public F55ca022 obtenerBancoxId(int iBancoId){
		F55ca022 f = null;
		
		Session sesion = null;
		
		
		try {
			sesion = HibernateUtilPruebaCn.currentSession();
		
			
			Object ob = sesion.createQuery("from F55ca022 f where f.id.codb = "
								+ iBancoId).uniqueResult();
			
			if(ob!=null) f = (F55ca022)ob;
			
		} catch (Exception error) { 
			error.printStackTrace();
		}
		return f;
	}
/*******************OBTENER TODOS LOS BANCOS DE LA TABLA "+PropertiesSystem.ESQUEMA+".BANCO*********************************************/
	public F55ca022[] obtenerBancos(){
		F55ca022[] banco = null;		
	    Session session = HibernateUtilPruebaCn.currentSession();
		try{
			List result = (List) session
			.createQuery("from F55ca022 as f where f.id.estado = 'A' order by f.id.orden")
			.list();	
			banco = new F55ca022[result.size()];
			for(int i = 0; i < result.size(); i++){
				banco[i] = (F55ca022)result.get(i); 
			}
		}catch(Exception ex){
			LogCajaService.CreateLog("obtenerBancos", "ERR", ex.getMessage());
		}
		return banco;
	}
/****************************************************************************************************/
}

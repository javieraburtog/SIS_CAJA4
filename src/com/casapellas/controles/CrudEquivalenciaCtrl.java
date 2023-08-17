package com.casapellas.controles;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.casapellas.entidades.Equivtipodocs;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;


/**
 * CASA PELLAS S.A.
 * Creado por.........: Carlos Manuel Hernández Morrison
 * Fecha de Creación..: 12/08/2011
 * Última modificación: Carlos Manuel Hernández Morrison
 * Modificado por.....:	12/08/2011
 * Descripción:.......: Acceso a base de datos para las equivalencias 
 * 						de banco con tipos de documentos de caja. 
 */
public class CrudEquivalenciaCtrl {
	public Exception error = null;
	public Exception getError() {
		return error;
	}
	public void setError(Exception error) {
		this.error = error;
	}
	
	public Equivtipodocs buscarEquivalenciaxId(String codigo, int idbanco){
		Equivtipodocs eq = null;
		Session sesion = HibernateUtilPruebaCn.currentSession();
		
		
		try {
		
			Criteria cr = sesion.createCriteria(Equivtipodocs.class);
			cr.add(Restrictions.eq("codigo",codigo));
			cr.add(Restrictions.eq("idbanco", idbanco));
			cr.add(Restrictions.eq("activo", true));
			
			Object ob = cr.uniqueResult();
			eq = (ob!=null)? (Equivtipodocs)ob: null;
			
		} catch (Exception e) {
			error = e;
			eq = null;
//			System.out.println("CRPMCAJA:CrudEquivalenciaCtrl Excepción capturada en buscarEquivalenciaxId(): "+e);
			e.printStackTrace();
		}
		return eq;
	}
/******************************************************************************************/
/** Método: Editar la configuraci[on de equivalencias.
 *	Fecha:  25/06/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 */	
	public boolean editarConfiguracionEquiv(Equivtipodocs etdActual, String nombre,String codigo,
											int idbanco,String descripbco,String coddoccaja,
											String nomdoccaja, int usrmod,boolean activo){
		boolean bHecho = true;
		Session sesion = HibernateUtilPruebaCn.currentSession();
		Transaction trans = null;
		
		try {
			trans = sesion.beginTransaction();
			Criteria cr = sesion.createCriteria(Equivtipodocs.class);
			cr.add(Restrictions.eq("codigo", etdActual.getCodigo()));
			cr.add(Restrictions.eq("nombre", etdActual.getNombre()));
			cr.add(Restrictions.eq("idbanco", etdActual.getIdbanco()));
			cr.add(Restrictions.eq("coddoccaja", etdActual.getCoddoccaja()));
			
			Equivtipodocs eq = (Equivtipodocs)cr.uniqueResult();
			eq.setActivo(activo);
			eq.setCoddoccaja(coddoccaja);
			eq.setCodigo(codigo);
			eq.setDescripbco(descripbco);
			eq.setDescripequiv(descripbco);
			eq.setFechamod(new Date());
			eq.setIdbanco(idbanco);
			eq.setNombre(nombre);
			eq.setNomdoccaja(nomdoccaja);
			eq.setUsrmod(usrmod);
			sesion.update(eq);
			
			trans.commit();
			sesion.close();
			
		} catch (Exception e) {
			error = e;
			bHecho = false;
//			System.out.println("CRPMCAJA:CrudEquivalenciaCtrl Excepción capturada en editarConfiguracionEquiv(): "+e);
			e.printStackTrace();
		}
		return bHecho;
	}
/******************************************************************************************/
/** Método: guardar los datos de la configuración.
 *	Fecha:  25/06/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 */	
	public boolean agregarEquivalencia(String nombre,String codigo,int idbanco,String descripbco,
									   String coddoccaja,String nomdoccaja, int usrcrea){
		boolean bHecho = true;
		Session sesion = HibernateUtilPruebaCn.currentSession();
		Transaction trans = null;
		Equivtipodocs eq = null;
		
		try {
			trans = sesion.beginTransaction();
			Criteria cr = sesion.createCriteria(Equivtipodocs.class);
			cr.add(Restrictions.eq("codigo",codigo));
			cr.add(Restrictions.eq("idbanco", idbanco));
			cr.add(Restrictions.eq("activo", false));
			
			Object ob = cr.uniqueResult();
			if(ob!=null){
				eq = (Equivtipodocs)ob;
				eq.setActivo(true);
				eq.setNombre(nombre);
				eq.setDescripbco(descripbco);
				eq.setFechamod(new Date());
				eq.setUsrmod(usrcrea);
				sesion.update(eq);
			}else{
				eq = new Equivtipodocs();
				eq.setActivo(true);
				eq.setCoddoccaja(coddoccaja);
				eq.setCodigo(codigo);
				eq.setDescripbco(descripbco);
				eq.setDescripequiv(descripbco);
				eq.setFechacrea(new Date());
				eq.setFechamod(new Date());
				eq.setIdbanco(idbanco);
				eq.setNombre(nombre);
				eq.setNomdoccaja(nomdoccaja);
				eq.setUsrcrea(usrcrea);
				eq.setUsrmod(usrcrea);
				sesion.save(eq);
			}
			trans.commit();
			sesion.close();
		} catch (Exception e) {
			error = e;
			bHecho = false;
//			System.out.println("CRPMCAJA:CrudEquivalenciaCtrl Excepción capturada en agregarEquivalencia(): "+e);
			e.printStackTrace();
		}
		return bHecho;
	}
	/******************************************************************************************/
	/** Método: obtener la lista de configuraciones registradas.
	 *	Fecha:  25/06/2010
	 *  Nombre: Carlos Manuel Hernández Morrison.
	 */
	@SuppressWarnings("unchecked")
	public List<Equivtipodocs> obtenerConfigEquivalencias(){
		List<Equivtipodocs>lstConfigs = null;
		Session sesion = HibernateUtilPruebaCn.currentSession();
		
		
		try {
						
			Criteria cr = sesion.createCriteria(Equivtipodocs.class);
			cr.add(Restrictions.eq("activo", true));
			cr.setMaxResults(40);
			cr.addOrder(Order.desc("fechacrea"));
			
			List lista = cr.list();
			if(lista == null || lista.isEmpty() ){
				lstConfigs = null;
			}else{
				lstConfigs = (ArrayList<Equivtipodocs>)cr.list();	
			}
			
			
		} catch (Exception e) {
			lstConfigs = null;
			error = e;
//			System.out.println("CRPMCAJA:CrudEquivalenciaCtrl Excepción capturada en obtenerConfigEquivalencias(): "+e);
			e.printStackTrace();
		}
		return lstConfigs;
	}
	
}

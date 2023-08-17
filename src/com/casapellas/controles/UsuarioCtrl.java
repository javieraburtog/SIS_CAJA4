package com.casapellas.controles;
/**
 * CASA PELLAS S.A.
 * Creado por.........: Juan Carlos Ñamendi Pineda
 * Fecha de Creación..: 02/07/2009
 * Última modificación: 02/07/2009
 * Modificado por.....:	Juan Carlos Ñamendi Pineda
 * 
 */
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.casapellas.entidades.Usuario;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;

public class UsuarioCtrl {
	
/******************************************************************************************/
/** Método: Enviar Correo de notificacion
 *	Fecha:  25/10/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public Usuario obtenerUsuarioENSxLogin(String sLogin){
		Usuario usuario = null;
		Session sesion = null;
		
		
		try {
			sesion = HibernateUtilPruebaCn.currentSession();		
			
									
			Criteria cr = sesion.createCriteria(Usuario.class);
			cr.add(Restrictions.sqlRestriction(" trim(lower(login)) = '"+sLogin.trim().toLowerCase()+"'"));
			cr.setMaxResults(1);
			List<Usuario> lstUsuario = cr.list();
			
			if(lstUsuario != null && lstUsuario.size() > 0){
				usuario = lstUsuario.get(0);
			}
		} catch (Exception e) {
			usuario = null;
			System.out.println("CRPMCAJA:UsuarioCtrl(): Excepción capturada en: obtenerUsuarioENSxCodigo  " + e);
		}
		return usuario;
	}
/******************************************************************************************/
/** Método: Enviar Correo de notificacion
 *	Fecha:  25/10/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public static Usuario obtenerUsuarioENSxCodigo(int iCodigoUsuario){
		Usuario usuario = null;
		Session sesion = null;
				
		try {
 
			sesion = HibernateUtilPruebaCn.currentSession();
			
			usuario = (Usuario)sesion.createCriteria(Usuario.class)
					.add(Restrictions.eq("codreg", iCodigoUsuario))
					.setMaxResults(1).uniqueResult() ;
			
		} catch (Exception e) {
			e.printStackTrace();  
		}
		return usuario;
	}
	/**OBTIENE EL CODIGO DE USUARIO RECIBIENDO COMO PARAMETRO SU LOGIN************************************************************************/
	public String getCodigoUsuarioxLogin(String sLogin){
		Session session = HibernateUtilPruebaCn.currentSession();
		
		String sCodUser = null;
		
		
		try{
			
			
			sCodUser = (String)session.createQuery("select u.coduser from Usuario as u where upper(trim(u.login)) = '"+sLogin.trim().toUpperCase()+"'").uniqueResult();
			
			
		}catch(Exception ex){
			sCodUser = "";
			System.out.println("Se capturo una excepcion en UsuarioCtrl.getUsuarioxLogin: " + ex);
		}
		return sCodUser;
	}
	
	
	public  String getLoginXCodUsuario(String sCodUser){
		Session session = HibernateUtilPruebaCn.currentSession();
		
		String sLogin = null;
		
		
		try{
			
			sLogin = (String)session.createQuery("select u.login from Usuario as u where upper(trim(u.coduser)) = '"+sCodUser.trim().toUpperCase()+"'").uniqueResult();
			
		}catch(Exception ex){
			sLogin = "";
			System.out.println("Se capturo una excepcion en UsuarioCtrl.getUsuarioxLogin: " + ex);
		}
		return sLogin;
	}
	
/*************************************************************************************************************************************/
}

package com.casapellas.controles;

/**
 * CASA PELLAS S.A.
 * Creado por.........: Juan Carlos Ñamendi Pineda
 * Fecha de Creación..: 28/02/2009
 * Última modificación: 06/03/2009
 * Modificado por.....:	Juan Carlos Ñamendi Pineda
 * 
 */
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.casapellas.entidades.Vmarca;
import com.casapellas.entidades.Vmodelo;
import com.casapellas.entidades.VtipoProd;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;

public class ProductoCrtl {
	/******* OBTIENE LOS TIPOS DE PRODUCTOS EXISTENETES DEL F0005 ***************************************/
	public VtipoProd[] obtenerTipoProducto() {
		VtipoProd[] vtipoProd = null;
		List lstTipoProd = new ArrayList();
		
//		Session session = HibernateUtilPruebaCn.currentSessionENS();
		Session session = HibernateUtilPruebaCn.currentSession();
		
		Transaction tx = null;
		boolean bNuevaSesionENS = false;

		try {
			String sql = "from VtipoProd";
			
			if( session.getTransaction().isActive() )
				tx =session.getTransaction();
			else{
				tx = session.beginTransaction();
				bNuevaSesionENS = true;
			}
			
			lstTipoProd = (List) session.createQuery(sql).list();
		
			if(bNuevaSesionENS)
				tx.commit();

			vtipoProd = new VtipoProd[lstTipoProd.size()];
			for (int i = 0; i < lstTipoProd.size(); i++) {
				vtipoProd[i] = (VtipoProd) lstTipoProd.get(i);
			}
		} catch (Exception ex) {
			System.out.print("Excepcion capturada en " +
					"ProductoCrtl.obtenerTipoProducto: " + ex);
		} finally {
			try {
				if(bNuevaSesionENS)
//					HibernateUtilPruebaCn.closeSessionENS(); 
					HibernateUtilPruebaCn.closeSession(session); 
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return vtipoProd;
	}

	/*************************************************************************************************/
	/*******
	 * OBTIENE LAS MARCAS EXISTENETES DEPENDIENDO DEL TIPO DE PRODUCTO DEL F0005
	 ***************************************/
	public Vmarca[] obtenerMarcasxTipoProducto(String[] sCodigos) {
		Vmarca[] vmarca = null;
		List lstMarcas = new ArrayList();
		
//		Session session = HibernateUtilPruebaCn.currentSessionENS();
		Session session = HibernateUtilPruebaCn.currentSession();
		
		Transaction tx = null;
		boolean bNuevaSesionENS = false;
		
		try {

			String sql = "from Vmarca ";
			String sAs = null;
			if (sCodigos != null) {
				sAs = "where (";
				for (int i = 0; i < sCodigos.length - 1; i++) {
					if (i == sCodigos.length - 2) {
						sAs = sAs + "drdl02 like '%" + sCodigos[i] + "%'";
					} else {
						sAs = sAs + "drdl02 like '%" + sCodigos[i] + "%' or ";
					}
				}
				sAs = sAs + ")";
			}
			sql = sql + sAs;
			
			if( session.getTransaction().isActive() )
				 tx = session.getTransaction();
			else{
				tx = session.beginTransaction();
				bNuevaSesionENS = true;
			}

			lstMarcas = (List) session.createQuery(sql).list();
			
			if(bNuevaSesionENS)
				tx.commit();

			if (lstMarcas != null && !lstMarcas.isEmpty()) {
				vmarca = new Vmarca[lstMarcas.size()];
				for (int i = 0; i < lstMarcas.size(); i++)
					vmarca[i] = (Vmarca) lstMarcas.get(i);
			}

		} catch (Exception ex) {
			System.out.print("Se capturo una excepcion en" +
					" ProductoCrtl.obtenerMarcasxTipoProducto: " + ex);
		} finally {
			try {
				if(bNuevaSesionENS)
//					HibernateUtilPruebaCn.closeSessionENS(); 
					HibernateUtilPruebaCn.closeSession(session); 
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return vmarca;
	}

	/*************************************************************************************************/
	/*******
	 * OBTIENE LAS MARCAS EXISTENETES DEPENDIENDO DEL TIPO DE PRODUCTO DEL F0005
	 ***************************************/
	public List obtenerMarcasxTipoProd(String[] sCodigos) {
		List lstMarcas = new ArrayList();
//		Session session = HibernateUtilPruebaCn.currentSessionENS();
		Session session = HibernateUtilPruebaCn.currentSession();
		
		Transaction tx = null;
		String sql = "", sCondicion = "";
		boolean bNuevaSesionENS = false;

		try {
			sql = "from Vmarca ";
			if (sCodigos != null) {
				sCondicion = "where (";
				for (int i = 0; i < sCodigos.length - 1; i++) {
					if (i == sCodigos.length - 2)
						sCondicion = sCondicion + "drdl02 like '%"
								+ sCodigos[i] + "%'";
					else
						sCondicion = sCondicion + "drdl02 like '%"
								+ sCodigos[i] + "%' or ";
				}
				sCondicion = sCondicion + ")";
			}
			sql = sql + sCondicion;

			if( session.getTransaction().isActive() )
				tx = session.getTransaction();
			else{
				tx = session.beginTransaction();
				bNuevaSesionENS = true;
			}
			
			lstMarcas = (List) session.createQuery(sql).list();
			
			if(bNuevaSesionENS)
				tx.commit();

		} catch (Exception ex) {
			System.out.print("Se capturo una excepcion en " +
					"ProductoCrtl.obtenerMarcasxTipoProducto: " + ex);
		} finally {
			try {
				if(bNuevaSesionENS)
//					HibernateUtilPruebaCn.closeSessionENS(); 
					HibernateUtilPruebaCn.closeSession(session); 
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return lstMarcas;
	}

	/*************************************************************************************************/
	/******* OBTIENE LOS MODELOS EXISTENTES DEPENDIENDO DE LA MARCA DEL F0005 ***************************************/
	public Vmodelo[] obtenerModelosxMarca(String sCodigo) {
		Vmodelo[] vmodelo = null;
		List lstModelos = new ArrayList();
		
//		Session session = HibernateUtilPruebaCn.currentSessionENS();
		Session session = HibernateUtilPruebaCn.currentSession();
		
		Transaction tx = null;
		boolean bNuevaSesionENS = false;
		
		try {
			
			if( session.getTransaction().isActive() )
				tx = session.getTransaction();
			else{
				tx = session.beginTransaction();
				bNuevaSesionENS = true;
			}
			
			String sql = "from Vmodelo as m where trim(drdl02) = '" + sCodigo + "'";
			lstModelos = (List) session.createQuery(sql).list();
			
			if(bNuevaSesionENS)
				tx.commit();
			
			if (lstModelos != null && !lstModelos.isEmpty()) {
				vmodelo = new Vmodelo[lstModelos.size()];
				for (int i = 0; i < lstModelos.size(); i++) {
					vmodelo[i] = (Vmodelo) lstModelos.get(i);
				}
			}
		} catch (Exception ex) {
			System.out.print("Se capturo una excepcion " +
					"en ProductoCrtl.obtenerModelosxMarca: " + ex);
		} finally {
			try {
				if(bNuevaSesionENS)
//					HibernateUtilPruebaCn.closeSessionENS(); 
					HibernateUtilPruebaCn.closeSession(session); 
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return vmodelo;
	}
}

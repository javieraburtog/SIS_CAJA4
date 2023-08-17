package com.casapellas.controles.tmp;

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

import com.casapellas.controles.ConsolidadoDepositosBcoCtrl;
import com.casapellas.entidades.Vmarca;
import com.casapellas.entidades.Vmodelo;
import com.casapellas.entidades.VtipoProd;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.util.LogCajaService;

public class ProductoCrtl {
	/******* OBTIENE LOS TIPOS DE PRODUCTOS EXISTENETES DEL F0005 ***************************************/
	public VtipoProd[] obtenerTipoProducto() {
		VtipoProd[] vtipoProd = null;
		List lstTipoProd = new ArrayList();

		Session session = HibernateUtilPruebaCn.currentSession();
		
		Transaction tx = null;
		boolean bNuevaSesionENS = false;

		try {
			String sql = "from VtipoProd";
			
			if( session.getTransaction().isActive() )
				tx = session.getTransaction();
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
			System.out
					.print("Excepcion capturada en ProductoCrtl.obtenerTipoProducto: "
							+ ex);
		} finally {
			try {
				if(bNuevaSesionENS)
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

		try {

			String strParamDrdl02 = "" ;
			for (String codigo : sCodigos) {
				strParamDrdl02 += " trim(drdl02) like '%"+codigo.split("@")[0].trim()+"%' or  " ;
			}
			strParamDrdl02 = strParamDrdl02.substring(0, strParamDrdl02.lastIndexOf("or"));
			
			String query= "from Vmarca where (" + strParamDrdl02 +" ) " ; 
			
			List<Vmarca> marcas = ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, Vmarca.class, false);
			
			if(marcas == null) {
				return vmarca = new Vmarca[1];
			}
			
			vmarca = new Vmarca[marcas.size()];
			return vmarca = marcas.toArray(vmarca);

		} catch (Exception ex) {
			LogCajaService.CreateLog("obtenerMarcasxTipoProducto", "ERR", ex.getMessage());
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
				tx  = session.getTransaction();
			else{
				tx  = session.beginTransaction();
				bNuevaSesionENS = true;
			}
			
			lstMarcas = (List) session.createQuery(sql).list();
			
			if(bNuevaSesionENS)
				tx.commit();

		} catch (Exception ex) {
			System.out
					.print("Se capturo una excepcion en ProductoCrtl.obtenerMarcasxTipoProducto: "
							+ ex);
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
			System.out
					.print("Se capturo una excepcion en ProductoCrtl.obtenerModelosxMarca: "
							+ ex);
		} finally {
			try {
				if(bNuevaSesionENS)
					HibernateUtilPruebaCn.closeSession(session); 
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return vmodelo;
	}
}

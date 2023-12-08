package com.casapellas.controles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.casapellas.entidades.F55ca01;
import com.casapellas.entidades.Varqueo;
import com.casapellas.entidades.Vrecibostcir;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.util.LogCajaService;

import pagecode.LoginCajaActual;

/**
 * CASA PELLAS S.A.
 * Creado por.........: Carlos Manuel Hernández Morrison
 * Fecha de Creación..: 31/08/2009
 * Última modificación: Carlos Manuel Hernández Morrison 
 * Modificado por.....:	18/03/2010
 * Descripcion:.......: Accesos a los datos en bd para los datos de arqueos procesados.
 * 
 */

public class ArqueoCtrl {

	
	
	
	
	
/*********************************************************************************/	
/**	Obtiene una lista con arqueos de caja a partir la consulta proporcionada	**/
	public List obtenerListaArqueos(String sql, int iMaxResult){
		List lstArqueos = null;
		
		Session sesion = HibernateUtilPruebaCn.currentSession();
		try {
			
			
			if(iMaxResult == 0)
				lstArqueos = sesion.createQuery(sql).list();
			else
				lstArqueos = sesion.createQuery(sql).setMaxResults(iMaxResult).list();
			
			
			//--Agregar el no de arqueo al encabezado de Arqueo.
			if(lstArqueos!=null && lstArqueos.size()>0){
				for(int i=0; i<lstArqueos.size();i++){
					Varqueo a = (Varqueo)lstArqueos.get(i);
					a.setNoarqueo(a.getId().getNoarqueo());
					lstArqueos.remove(i);
					lstArqueos.add(i,a);
				}
			}
		} catch (Exception error) {
			lstArqueos = null;
			LogCajaService.CreateLog("obtenerListaArqueos", "ERR", error.getMessage());
		}
		return lstArqueos;
	}
	
	
	@SuppressWarnings("unchecked")
	public ArrayList<Varqueo> getArqueosCaja(int iCaidBusq, int iCajaUso ,
					String sCodcomp, String sMoneda, String sEstado, 
					Date dtInicio, Date dtFinal, int iCodcont, int iMaximos){
		Session sesion = null;

		List<Integer>lstCajas = new ArrayList<Integer>();
		ArrayList<Varqueo> arqueos = new ArrayList<Varqueo>();
		
		try {
			sesion = HibernateUtilPruebaCn.currentSession();
			
			Criteria cr = sesion.createCriteria(Varqueo.class);
			cr.add(Restrictions.between("id.fecha", dtInicio, dtFinal));
			
			LogCajaService.CreateLog("getArqueosCaja", "HQRY", LogCajaService.toSql(cr));
			
			if(sEstado.compareTo("") != 0)
				cr.add(Restrictions.eq("id.estado", sEstado));
			if(sCodcomp.compareTo("") != 0)
				cr.add(Restrictions.eq("id.codcomp", sCodcomp));
			if(sMoneda.compareTo("") != 0)
				cr.add(Restrictions.eq("id.moneda", sMoneda));
			if(iCaidBusq != 0)
				lstCajas.add(iCaidBusq);
			
			//&& ==== Consultar las cajas disponibles por contador.
			if(iCaidBusq == 0){
				lstCajas = (ArrayList<Integer>)
						sesion.createCriteria(F55ca01.class)
						.add(Restrictions.eq("id.cacont", iCodcont))
						.setProjection(Projections.distinct(
							Projections.property("id.caid"))).list();
				
			}
			if(iCaidBusq == 0 && ( lstCajas == null || lstCajas.isEmpty() ) ){
				lstCajas = new ArrayList<Integer>();
				lstCajas.add(iCajaUso);
			}
			cr.add(Restrictions.in("id.caid", lstCajas));
			
			arqueos = (ArrayList<Varqueo>)cr.list();
			
			if(arqueos == null || arqueos.isEmpty()) return new ArrayList<Varqueo>();
			
			Collections.sort(arqueos, new Comparator<Varqueo>(){
				public int compare(Varqueo v1, Varqueo v2) {
					int iCompCaja = (v1.getId().getCaid() < v2.getId()
							.getCaid())? -1: (v1.getId().getCaid() >
								v2.getId().getCaid())? 1 : 0;
					if (iCompCaja == 0)
						iCompCaja = (v1.getId().getNoarqueo() < v2.getId()
							.getNoarqueo())? -1: (v1.getId().getNoarqueo() >
								v2.getId().getNoarqueo())? 1 : 0;
					return iCompCaja;
				}
			});
			
			for (int i = 0; i < arqueos.size(); i++) {
				Varqueo v = arqueos.get(i);
				v.setNoarqueo(v.getId().getNoarqueo());
				arqueos.set(i, v);
			}
			
		} catch (Exception e) {			
			LogCajaService.CreateLog("getArqueosCaja", "ERR", e.getMessage());
		}
		return arqueos;
	}
	
/*************** 1. Obtener todos los arqueos en estado pendiente ***************/
	public List obtenerArqueosPendientes(String sql, int iCodcont, boolean bFiltroCaja, int iMaxResult){
		List lstArqueos=new ArrayList();
		Session sesion = HibernateUtilPruebaCn.currentSession();		
		String consulta = "",cajas="";
				
		try{
			
			if(!bFiltroCaja){
				//------ obtener las cajas disponibles para el contador.
				consulta = "from F55ca01 as f where f.id.cacont = "+iCodcont;
				List lstCaid = (ArrayList)sesion.createQuery(consulta).list();
				if(lstCaid!=null){				
					if(lstCaid.size()==1)
						cajas += "("+((F55ca01)lstCaid.get(0)).getId().getCaid()+")";
					else{
						cajas += "("+((F55ca01)lstCaid.get(0)).getId().getCaid()+",";				
						for(int i=1;i<lstCaid.size();i++){
							F55ca01 caja = (F55ca01)lstCaid.get(i);						
							if(i== lstCaid.size()-1)
								cajas += ""+caja.getId().getCaid()+")";
							else					
								cajas+=""+caja.getId().getCaid()+",";					
						}
					}
					sql += " and v.id.caid in "+cajas;
				}
			}
			sql += " order by v.id.caid asc, v.id.noarqueo desc,v.id.fecha desc";
				
			//---- Definir límite para el número de arqueos mostrados.
			if(iMaxResult > 0)
				lstArqueos = sesion.createQuery(sql).setMaxResults(iMaxResult).list();
			else if(iMaxResult == 0)
				lstArqueos = sesion.createQuery(sql).list();
			
			
			
			if(lstArqueos!=null && lstArqueos.size()>0){
				for(int i=0; i<lstArqueos.size();i++){
					Varqueo v = (Varqueo)lstArqueos.get(i);
					v.setNoarqueo(v.getId().getNoarqueo());
					lstArqueos.remove(i);
					lstArqueos.add(i,v);
				}
			}
			
		}catch(Exception error){
			LogCajaService.CreateLog("obtenerArqueosPendientes", "ERR", error.getMessage());
		}		
		return lstArqueos;	
	}
	
/*********** consultar el vf0101 para obtener el nombre del cajero (empleado) ************************/
	public String obtenerNombreEmpleado(int iCodigo,Session sesion, Transaction trans){
		String nombre = "",consulta = "";		
				
		
		try{
			consulta = "select v.id.abalph from Vf0101 as v where v.id.aban8 ="+iCodigo;			
			if(sesion == null){
				sesion = HibernateUtilPruebaCn.currentSession();
				
			}			
			Object ob = sesion.createQuery(consulta).uniqueResult();
					
			if(ob!=null)
				nombre = ob.toString();
			else
				nombre = ""+iCodigo;
			
			
			
		}catch(Exception error){
			LogCajaService.CreateLog("obtenerNombreEmpleado", "ERR", error.getMessage());
		}
		return nombre;
	}
/*********** consultar el vf0101 para obtener el nombre del cajero (empleado) ************************/
	public String obtenerNombreSucursal(String sCodsuc,Session sesion, Transaction trans){
		String nombre = "",consulta = "";		
				
		
		try{
			consulta = "select v.id.caconom from Vf55ca01 as v where v.id.caco ='"+sCodsuc+"'";			
			if(sesion == null){
				sesion = HibernateUtilPruebaCn.currentSession();
				
			}			
			Object ob = sesion.createQuery(consulta).uniqueResult();
					
			if(ob!=null)
				nombre = ob.toString();
			else
				nombre = sCodsuc;
			
			
		}catch(Exception error){
			LogCajaService.CreateLog("obtenerNombreSucursales", "ERR", error.getMessage());
		}	
		return nombre;
	}
/*********** consultar el vf0101 para obtener el nombre del cajero (empleado) ************************/
	public String obtenerNombreCaja(int iCaid,Session sesion, Transaction trans){
		String nombre = "",consulta = "";		
		
		
		try{
			consulta = "select v.id.caname from Vf55ca01 as v where v.id.caid =" +iCaid;			
			if(sesion == null){
				sesion = HibernateUtilPruebaCn.currentSession();
				
			}			
			Object ob = sesion.createQuery(consulta).uniqueResult();
					
			if(ob!=null)
				nombre = ob.toString();
			else
				nombre = "" +iCaid;
			
			
		}catch(Exception error){
			LogCajaService.CreateLog("obtenerNombreCaja", "ERR", error.getMessage());
		}	
		return nombre;
	}	
	
}

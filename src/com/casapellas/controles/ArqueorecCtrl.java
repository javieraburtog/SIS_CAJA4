package com.casapellas.controles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.casapellas.entidades.Arqueo;
import com.casapellas.entidades.Arqueorec;
import com.casapellas.entidades.ArqueorecId;
import com.casapellas.entidades.Salida;
import com.casapellas.entidades.Vrecibodatos;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.PropertiesSystem;

/**
 * CASA PELLAS S.A.
 * Creado por.........: Carlos Manuel Hernández Morrison
 * Fecha de Creación..: 24/07/2009
 * Última modificación: Carlos Manuel Hernández Morrison 
 * Modificado por.....:	24/08/2009
 * Descripcion:.......: Accesos a bd para los recibos por arqueo.
 * 
 */
public class ArqueorecCtrl {

	
	@SuppressWarnings("unchecked")
	public List<String> obtenerChequesArqueo(Arqueo a, Date horaInicio, Date horaFin ){
		List<String> lstCheques = null;
		
		try {
			
			String rangohoras = "'" +FechasUtil.formatDatetoString(horaInicio, "HH.mm.ss") + "' and '"
									+FechasUtil.formatDatetoString(horaFin, "HH.mm.ss")+"'";
			
			String strSql =  
				" select refer2||'@'|| trim(banco)||'@'|| refer3||'@'|| ifnull( sum(monto), 0 ) "+ 
				" from "+PropertiesSystem.ESQUEMA+".recibo r inner join "+PropertiesSystem.ESQUEMA+".recibodet rd on " +
						" r.caid = rd.caid and r.codcomp = rd.codcomp and r.numrec = rd.numrec and r.tiporec = rd.tiporec inner join "
						  + PropertiesSystem.ESQUEMA +".f55ca022 bco on bco.codb = rd.refer1 " +
				" where rd.caid = "+ a.getId().getCaid() + 
				" and rd.moneda = '"+a.getMoneda()+"'"+
				" and rd.codcomp = '"+a.getId().getCodcomp()+"'"+
				" and r.fecha = '"+FechasUtil.formatDatetoString(a.getId().getFecha(), "yyyy-MM-dd")+"'"+
				" and r.hora between " + rangohoras +
				" and rd.mpago = '"+MetodosPagoCtrl.CHEQUE+"' and r.estado = '' and r.tiporec <> 'DCO' "+
				" and r.numrec not in ("+
					"select nodoco from "+PropertiesSystem.ESQUEMA+".recibo r1 where  r.caid = r1.caid and r.codcomp = r1.codcomp  " +
					"and r1.tiporec = 'DCO' and r1.estado = '' and r1.fecha = r.fecha and r1.hora between " + rangohoras + 
				")"+
				" group by rd.refer2, refer3, banco  order by banco, refer2  " ;
			 
			lstCheques = (ArrayList<String>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSql, true, null)  ;
			 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return lstCheques;
	}
	/**************************************************
	 *  Método: GCPMCAJA / com.casapellas.controles /obtenerDetalleCheques
	 *  Descrp: 
	 *	Fecha:  Nov 6, 2012 
	 *  Autor:  CarlosHernandez
	 ***/
	@SuppressWarnings("unchecked")
	public List<String> obtenerChequesArqueo(Arqueo a, List<String>lstRecibos) {
		List<String> lstCheques = null;
		Session sesion = null;
		Transaction trans = null;
		boolean bNuevaSesionCaja = false;
		
		try {
			
			StringBuilder sRecibosIn = new StringBuilder(lstRecibos.toString());
			sRecibosIn.setCharAt(0, '(');
			sRecibosIn.setCharAt(lstRecibos.toString().length()-1, ')');
			 
			sesion = HibernateUtilPruebaCn.currentSession();
			if( sesion.getTransaction().isActive() )
				trans = sesion.getTransaction();
			else{
				trans = sesion.beginTransaction();
				bNuevaSesionCaja = true;
			}
			String sql = 
				"  select  refer2||'@'|| trim(banco)||'@'|| refer3||'@'|| ifnull( sum(monto), 0 )"
				+" from " + PropertiesSystem.ESQUEMA +".recibodet rd inner join "
						  + PropertiesSystem.ESQUEMA +".f55ca022 bco"
				+" on bco.codb = rd.refer1"
				+" where rd.caid = "+a.getId().getCaid() 
				+" and trim(rd.codcomp) = '"+a.getId().getCodcomp().trim()+"' "
				+" and rd.mpago = '"+MetodosPagoCtrl.CHEQUE+"' and rd.moneda = '"+a.getMoneda()+"'"
				+" and rd.numrec in " + sRecibosIn
				+" group by rd.refer2, refer3, banco"
				+" order by banco, refer2" ;
			
			List<Object>lstResult = sesion.createSQLQuery(sql).list();
			
			if(lstResult != null && lstResult.size()> 0){
				lstCheques = new ArrayList<String>();
				for (Object ob : lstResult) 
					lstCheques.add(String.valueOf(ob));	
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(bNuevaSesionCaja){
				try{trans.commit();}catch(Exception e){e.printStackTrace();}
				try{HibernateUtilPruebaCn.closeSession(sesion);}
				catch(Exception e){e.printStackTrace();}
			}
		}
		return lstCheques;
	}
	/* ******************** fin de metodo obtenerDetalleCheques ****************************/
	
	public String[] obtenerResumenBanco(Arqueo a, Date horaInicio, Date horaFin ){
		String[] sRsmnBco = new String[3];
	
		try {
			
			String rangohoras = "'" +FechasUtil.formatDatetoString(horaInicio, "HH.mm.ss") + "' and '"
								 	+FechasUtil.formatDatetoString(horaFin, "HH.mm.ss")+"'";
			
			String strSql  = 
			" select  refer1 ||'@'|| count(distinct(refer2)) ||'@'|| ifnull(sum(monto) , 0 ) , ( case when CHK.refer1 = '100001' then 0 else 1  end) orden   " +
			" From ( " +
			" select  ( case when rd.refer1 = '100001' then  rd.refer1  else  '  '  end)  refer1 , rd.refer2, monto"+ 
			" from "+PropertiesSystem.ESQUEMA+".recibo r inner join "+PropertiesSystem.ESQUEMA+".recibodet rd on " +
					" r.caid = rd.caid and r.codcomp = rd.codcomp and r.numrec = rd.numrec and r.tiporec = rd.tiporec "+
			" where rd.caid = "+ a.getId().getCaid() + 
			" and rd.moneda = '"+a.getMoneda()+"'"+
			" and rd.codcomp = '"+a.getId().getCodcomp()+"'"+
			" and r.fecha = '"+FechasUtil.formatDatetoString(a.getId().getFecha(), "yyyy-MM-dd")+"'"+
			" and r.hora between " + rangohoras +
			" and rd.mpago = '"+MetodosPagoCtrl.CHEQUE+"' and r.estado = '' and r.tiporec <> 'DCO' "+
			" and r.numrec not in ("+
				"select nodoco from "+PropertiesSystem.ESQUEMA+".recibo r1 where  r.caid = r1.caid and r.codcomp = r1.codcomp  " +
				"and r1.tiporec = 'DCO' and r1.estado = '' and r1.fecha = r.fecha and r1.hora between " + rangohoras + " ) " +
			") as CHK  group by refer1 ORDER BY 2 asc " ; 	
			 
			
			@SuppressWarnings("unchecked")
			List<Object[]> lstRsmn = (ArrayList<Object[]>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSql, true, null)  ;
			
			if(lstRsmn == null ||  lstRsmn.isEmpty() ) 
				return sRsmnBco ;
				
			 if(lstRsmn.size() == 1){
				 if(  String.valueOf( lstRsmn.get(0)[1]  ).compareTo("0") == 0  ) {
					 sRsmnBco[0] = String.valueOf( lstRsmn.get(0)[0] );
				 }else{
					 sRsmnBco[1] = String.valueOf( lstRsmn.get(0)[0] );
				 }
				 return sRsmnBco;
			 }
			
			for (int i = 0; i < lstRsmn.size(); i++) {
				sRsmnBco[i] = String.valueOf( lstRsmn.get(i)[0] );
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return sRsmnBco;
	}
	
	/**************************************************
	 *  Método: GCPMCAJA / com.casapellas.controles /obtenerResumenBanco
	 *  Descrp: Resumen de pagos con cheques en el arqueo.
	 *	Fecha:  Nov 6, 2012 
	 *  Autor:  CarlosHernandez
	 ***/
	@SuppressWarnings("unchecked")
	public String[] obtenerResumenBanco(Arqueo a, List<String>lstRecibos) {
		String[] sRsmnBco = new String[3];
		String sql = "";
		boolean bNuevaSessionCaja = false;
		Session sesion = null;
		Transaction trans = null;
				
		try {
			
			StringBuilder sRecibosIn = new StringBuilder(lstRecibos.toString());
			sRecibosIn.setCharAt(0, '(');
			sRecibosIn.setCharAt(lstRecibos.toString().length()-1, ')');

			sesion = HibernateUtilPruebaCn.currentSession();
			if( sesion.getTransaction().isActive() )
				trans = sesion.getTransaction();
			else{
				trans = sesion.beginTransaction();
				bNuevaSessionCaja = true;
			}
			
			//&& ========== Cheques BAC
			sql = "	select refer1 ||'@'|| count(distinct(refer2))  ||'@'||  ifnull(sum(monto) ,0)  "
					+ "	from "+PropertiesSystem.ESQUEMA+".recibodet rd "
					+ " where rd.caid = "+a.getId().getCaid()
					+ " 	and rd.moneda = '"  +a.getMoneda()+ "'" 
					+ "		and rd.codcomp = '" +a.getId().getCodcomp()+ "'" 
					+ "		and rd.mpago = '"+MetodosPagoCtrl.CHEQUE+"'"
					+ "		and rd.refer1 = " + 100001
					+ "		and rd.numrec in " + sRecibosIn
					+ " group by refer1 ";
			
			List<Object> lstRsmn = sesion.createSQLQuery(sql).list(); 
			if(lstRsmn != null && !lstRsmn.isEmpty())
				sRsmnBco[0] = String.valueOf(lstRsmn.get(0));
			

			//&& ========== Cheques NO BAC
			sql = "	select ''||'@'|| count(distinct(refer2)) ||'@'||  ifnull(sum(monto) ,0) "
				+ "	from "+PropertiesSystem.ESQUEMA+".recibodet rd "
				+ " where rd.caid = "+a.getId().getCaid()
				+ " 	and rd.moneda = '"  +a.getMoneda()+ "'" 
				+ "		and rd.codcomp = '" +a.getId().getCodcomp()+ "'" 
				+ "		and rd.mpago = '"+MetodosPagoCtrl.CHEQUE+"'"
				+ "		and rd.refer1 <> " + 100001
				+ "		and rd.numrec in " + sRecibosIn;
			
			lstRsmn = sesion.createSQLQuery(sql).list(); 
			if(lstRsmn != null && !lstRsmn.isEmpty())
				sRsmnBco[1] = String.valueOf(lstRsmn.get(0));
			
		} catch (Exception e) {
			e.printStackTrace();
//			System.out.println(" com.casapellas.controles " + new Date());
//			System.out
//					.println("GCPMCAJA: Excepción capturada en : obtenerResumenBanco Mensaje:\n "
//							+ e);
		}finally{
			if( bNuevaSessionCaja ){
				try{trans.commit();}catch(Exception e){e.printStackTrace();}
				try{HibernateUtilPruebaCn.closeSession(sesion);}
				catch(Exception e){e.printStackTrace();}
			}
		}
		return sRsmnBco;
	}
	/* ******************** fin de metodo obtenerResumenBanco ****************************/
	
	@SuppressWarnings("unchecked")
	public List<Object[]> obtenerRecibosEnCierre(int iCaid, String sCodcomp,
								Date dtFecha, Date dtHoraInicio, String sMoneda){
		List<Object[]>lstNumerosRec = new ArrayList<Object[]>();
		Object[] dtaCn = null;
		
		try {
			
			dtaCn = ConsolidadoDepositosBcoCtrl.getSessionForQuery();
			Session sesion = (Session)dtaCn[1];
				
			Criteria cr = sesion.createCriteria(Vrecibodatos.class)
				.add(Restrictions.eq("id.caid", iCaid))
				.add(Restrictions.eq("id.codcomp", sCodcomp))
				.add(Restrictions.eq("id.fecha", dtFecha))
				.add(Restrictions.eq("id.restado", ""))
				.add(Restrictions.disjunction()
						.add(Restrictions.eq("id.moneda", sMoneda))
						.add(Restrictions.eq("id.moncam", sMoneda))
						.add(Restrictions.eq("id.monfact", sMoneda))
				);
			if(dtHoraInicio != null){
				cr.add(Restrictions.gt("id.hora", dtHoraInicio));
				cr.add(Restrictions.lt("id.hora", dtFecha));
			}
			
			cr.setProjection(Projections.distinct(Projections.projectionList()
							.add(Projections.property("id.numrec"))
							.add(Projections.property("id.tiporec")) 
						));

			lstNumerosRec = (List<Object[]>)cr.list();
			
			if(lstNumerosRec != null && !lstNumerosRec.isEmpty() ){
				Collections.sort(lstNumerosRec, new Comparator<Object[]>() {
					public int compare(Object[] o1, Object[] o2) {
						return
						  ((Integer)o1[0]) > ((Integer)o2[0]) ? 1:
						  ((Integer)o1[0]) < ((Integer)o2[0]) ? -1: 0;
					}
				}) ;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
 
		}finally{
			ConsolidadoDepositosBcoCtrl.closeSessionForQuery(true, dtaCn);
		}
		return lstNumerosRec;
	}
	
/***** 1. Guardar los recibos que se incluyen en el arqueo de caja ***********/
	public boolean guardarRecibosArqueo(int caid,String codcomp,String codsuc,
								int noarqueo, Date dtFecha,String sMoneda, 
								Date dtHini,Session sesion,Transaction trans,
								List<Object[]>lstRecibosCierre){
		boolean agregados = true;
		List<String>lstNumrec = new ArrayList<String>();
		
		try{

			if(lstRecibosCierre != null && !lstRecibosCierre.isEmpty() ){
				
				for (Object ob[] : lstRecibosCierre) {
					
					int iNumrec = (Integer.parseInt(String.valueOf(ob[0].toString())));
					String sTiporec = String.valueOf( ob[1].toString() );					
					
					Arqueorec ar = new Arqueorec();
					ArqueorecId id = new ArqueorecId();
					id.setCaid(caid);
					id.setCodcomp(codcomp);
					id.setCodsuc(codsuc);
					id.setNoarqueo(noarqueo);
					id.setNumrec(iNumrec);
					id.setTipodoc("R");
					id.setTiporec(sTiporec);
					ar.setId(id);
					sesion.save(ar);
					
					//&& ==== Conservar la lista de recibos para la minuta automatica.
					lstNumrec.add(String.valueOf(iNumrec));
				}
				
				Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
				m.remove("ac_RecibosCierre");
				m.put("ac_RecibosCierre", lstNumrec);
			}
		}catch(Exception error){
			agregados = false;
			error.printStackTrace();
		}
		return agregados;
	}
	/***** 1. Guardar los recibos que se incluyen en el arqueo de caja ***********/
	public boolean guardarSalidasArc(int caid,String codcomp,String codsuc,int noarqueo,
									 Date dtFecha,String sMoneda,Date dtHini,Session sesion,Transaction trans){
		boolean agregados = true;
		List lstSalidas  = new ArrayList();
		String consulta = "",sFecha = "";
		FechasUtil f = new FechasUtil();
		
		try{
			sFecha = f.formatDatetoString(dtFecha, "yyyy-MM-dd");
			consulta =  " from Salida as v where v.id.caid = "+caid+" and trim(v.id.codcomp) ='"+codcomp.trim()+"'";
			consulta += " and trim(v.id.codsuc) = '"+codsuc.trim()+"' and cast(v.fproceso as date) = '"+sFecha+"'";
			consulta += " and v.estado = 'P' and v.moneda ='"+sMoneda+"'";			

			//--- Agregar filtro de intervalo de horas en caso de arqueos previos.
			if(dtHini!=null){
				String sHini,sHfin;
				sHini = f.formatDatetoString(dtHini,  "HH.mm.ss");
				sHfin = f.formatDatetoString(dtFecha, "HH.mm.ss");
				consulta += " and cast(v.fproceso as time) >='"+sHini+"' and cast(v.fproceso as time)<='"+sHfin+"'";
			}
			lstSalidas = sesion.createQuery(consulta).list();
			if(lstSalidas!=null && lstSalidas.size()>0){				
				for(int i=0; i<lstSalidas.size();i++){
					Salida s = (Salida)lstSalidas.get(i);
					Arqueorec ar = new Arqueorec();
					ArqueorecId id = new ArqueorecId();								
					id.setCaid(caid);
					id.setCodcomp(codcomp);
					id.setCodsuc(codsuc);
					id.setNoarqueo(noarqueo);
					id.setNumrec(s.getId().getNumsal());
					id.setTipodoc("S");
					id.setTiporec("S");
					ar.setId(id);
					sesion.save(ar);
				}					
			}
		}catch(Exception error){
			agregados = false;
//			System.out.println("Error en ArqueorecCtrl.guardarSalidasArc "+error);
			error.printStackTrace();
		}		
		return agregados;
	}
	
}

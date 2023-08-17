package com.casapellas.controles;
/**
 * CASA PELLAS S.A.
 * Creado por.........: Juan Carlos Ñamendi Pineda
 * Fecha de Creación..: 10/03/2009
 * Última modificación: 25/07/2011
 * Modificado por.....:	Juan Carlos Ñamendi Pineda
 * 					  : Buscar facturas en un rango de fechas en la ventana de agreagar Facturas.   
 * 
 */
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.casapellas.entidades.Credhdr;
import com.casapellas.entidades.Dfactura;
import com.casapellas.entidades.Dfacturajde;
import com.casapellas.entidades.F55ca014;
import com.casapellas.entidades.FacturaCredito;
import com.casapellas.entidades.Hfacturajde;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.util.CalendarToJulian;
import com.casapellas.util.Divisas;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.JulianToCalendar;
import com.casapellas.util.PropertiesSystem;

public class FacturaCreditoCtrl {
	/******************************************************************************************/	
	public List<Credhdr> buscarFacturasNoLigas(String sParametro,String sMoneda,String sCodComp,
													String sCodunineg, String sCodsuc,F55ca014[] f14){
		List lstCredhdr = null;
		String sql = "SELECT CAST(TRIM(RPDCT) AS VARCHAR(2) CCSID 37) TIPOFACTURA,RPDOC AS NOFACTURA,CAST (RPSFX AS VARCHAR(3) CCSID 37) AS PARTIDA "+
					",RPAN8 AS CODCLI,RPALPH AS NOMCLI, CAST(TRIM(RPMCU) AS VARCHAR(12) CCSID 37) CODUNINEG, UN.MCDL01 AS UNINEG, CAST(RPKCO AS VARCHAR(5) CCSID 37) CODSUC, " +
					" (SELECT MCDL01 FROM "+PropertiesSystem.JDEDTA+".F0006 WHERE SUBSTRING(RPKCO,4,5) = SUBSTRING(MCMCU,11,12) AND MCSTYL = 'BS') AS NOMSUC, CAST(TRIM(UN.MCRP01) AS VARCHAR(3) CCSID 37) CODCOMP, " + 
					" CO.DRDL01 AS NOMCOMP, CAST(TRIM(RPCRCD) AS VARCHAR(3) CCSID 37) MONEDA, RPCRR AS TASA, CAST(RPAAP/100 AS DECIMAL (12,2)) AS CPENDIENTE, CAST(RPFAP/100 AS DECIMAL (12,2)) AS DPENDIENTE, " + 
					" CAST(RPAG/100 AS DECIMAL (12,2)) AS CTOTAL,CAST(RPACR/100 AS DECIMAL (12,2)) AS DTOTAL,CAST(RPSTAM/100 AS DECIMAL (12,2)) AS CIMPUESTO, CAST(RPCTAM/100 AS DECIMAL (12,2)) AS DIMPUESTO," +
					" CAST(RPATXA/100 AS DECIMAL (12,2)) AS CSUBTOTAL, CAST(RPCTXA/100 AS DECIMAL (12,2)) AS DSUBTOTAL," + 
					" CAST((CAST(( CASE WHEN RPCRCD = 'COR' THEN RPAG ELSE RPACR END) AS DECIMAL(12,2))/100) AS DECIMAL(12,2)) MONTO,"+
					" CAST((CAST(( CASE WHEN RPCRCD = 'COR' THEN RPAAP ELSE RPFAP END) AS DECIMAL(12,2))/100) AS DECIMAL(12,2)) MONTOPEND," +
					" CAST((CAST(( CASE WHEN RPCRCD = 'COR' THEN RPATXA ELSE RPCTXA END) AS DECIMAL(12,2))/100) AS DECIMAL(12,2)) SUBTOTAL," +
					" CAST((CAST(( CASE WHEN RPCRCD = 'COR' THEN RPSTAM ELSE RPCTAM END) AS DECIMAL(12,2))/100) AS DECIMAL(12,2)) IMPUESTO," +
					" CAST ((CAST(DATE(CHAR(1900000 + RPDIVJ)) AS TIMESTAMP)) AS DATE) FECHA," +
					" CAST ((CAST(DATE(CHAR(1900000 + RPDDJ)) AS TIMESTAMP)) AS DATE) FECHAVENC," +
					" CAST(RPGLC AS VARCHAR(4) CCSID 37) AS COMPENSLM,"  +
					" CAST(TRIM(RPPTC) AS VARCHAR(3) CCSID 37) TIPOPAGO," + 
					" RPDIVJ,"+
					" RPDDJ,"+
					" CAST(TRIM(RPPO) AS VARCHAR(8) CCSID 37)RPPO,"+
					" CAST(TRIM(RPDCTO) AS VARCHAR(2) CCSID 37)RPDCTO"+
					" FROM "+PropertiesSystem.JDEDTA+".F0311"+ 
					" INNER JOIN "+PropertiesSystem.JDEDTA+".F0006 UN ON RPMCU = UN.MCMCU "+ 
					" INNER JOIN "+PropertiesSystem.JDECOM+".F0005 CO ON (UN.MCRP01 = SUBSTRING(CO.DRKY,8,10) AND CO.DRSY = '00' AND CO.DRRT = '01' AND CO.DRDL02 = 'F') " + 
					" WHERE trim(RPDCTO) = '' AND TRIM(RPPO) = '' AND RPAAP > 0 AND CAST((CAST(DATE(CHAR(1900000 + RPDIVJ)) AS TIMESTAMP)) AS DATE) = CURRENT_DATE";
		
		Session s = null;
		Transaction tx = null;
		try {
			//CODIGO DE CLIENTE
			sql = sql + " AND RPAN8 = "+ sParametro;
			//---- Moneda, Compania, Unidad de Negocio, Sucursal.
			if(!sMoneda.trim().equals("01"))
				sql = sql + " AND CAST(TRIM(RPCRCD) AS VARCHAR(3) CCSID 37) = '"+ sMoneda.trim()+"'";
			if(!sCodComp.trim().equals("01"))
				sql = sql + " AND CAST(TRIM(UN.MCRP01) AS VARCHAR(3) CCSID 37) = '"+ sCodComp.trim()+"'";
			if(!sCodsuc.trim().equals("01"))
				sql = sql + " AND CAST(RPKCO AS VARCHAR(5) CCSID 37) = '"+ sCodsuc.trim()+"'";
			if(!sCodunineg.trim().equals("01"))
				sql = sql + " AND CAST(TRIM(RPMCU) AS VARCHAR(12) CCSID 37) = '"+ sCodunineg.trim()+"'";
			
			s = HibernateUtilPruebaCn.currentSession();
			
			lstCredhdr = s.createSQLQuery(sql).addEntity(Credhdr.class).list();
			
			if(lstCredhdr!=null && lstCredhdr.size()>0)
				lstCredhdr = llenarInfoFactura(lstCredhdr, f14);
			
		}catch(Exception ex){
			System.out.println("Se capturo una Excepcion en : " + ex);							
		}finally{
			s.close();
		}
		return lstCredhdr;
	}

/******************************************************************************************/
/** Método: Nuevo método para consultar Facturas de Crédito.
 *	Fecha:  09/03/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	@SuppressWarnings("unchecked")
	public ArrayList<Credhdr> buscarFacturasCredito(int iParametro,String sMoneda,String sCodComp, 
												int iDesde,int iHasta, ArrayList<Credhdr> lstSelectedFacs,F55ca014[] f14){
		ArrayList<Credhdr>lstCredhdr = null;
		Session sesion = HibernateUtilPruebaCn.currentSession();
		
		Criteria ctr = null;
		ArrayList<Integer>iNoFacturas = new ArrayList<Integer>(lstSelectedFacs.size());
		
		try {
			
			ctr = sesion.createCriteria(Credhdr.class);
			ctr.addOrder(Order.asc("id.fechavenc"));
			
			ctr.add(Restrictions.eq("id.codcli", iParametro));
			if (!sMoneda.equals("01"))
				ctr.add(Restrictions.eq("id.moneda", sMoneda));
			if(!sCodComp.equals("01"))
				ctr.add(Restrictions.eq("id.codcomp", sCodComp));
			if(iDesde != 0)
				ctr.add(Restrictions.ge("id.nofactura", iDesde));
			if(iHasta != 0)
				ctr.add(Restrictions.le("id.nofactura", iHasta));
			if(lstSelectedFacs.size()>0){
				for (Credhdr chdr : lstSelectedFacs)
					iNoFacturas.add(chdr.getId().getNofactura());
				ctr.add(Restrictions.not(Restrictions.in("id.nofactura", iNoFacturas)));
			}
			ctr.add(Restrictions.ne("id.tipofactura", "IF"));
			
			ctr.add(Restrictions.ne("id.tipofactura", "MF"));
			
			lstCredhdr = (ArrayList<Credhdr>)ctr.list();
			
			if(lstCredhdr!=null && lstCredhdr.size()>0)
				lstCredhdr = (ArrayList<Credhdr>)llenarInfoFactura(lstCredhdr, f14);
			
		} catch (Exception error) {
			lstCredhdr = null;
			System.out.println(": Excepción capturada en: FacturaCreditoCtrl.buscarFacturaCredito() "+error);
		}finally{
			iNoFacturas = null;
			if(sesion.isOpen())
				sesion.close();
		}
		return lstCredhdr;
	}
	
	/*****************Buscar Facturas de Credito**********************************/
	public List buscarFacturasCredito1(int iParametro,String sMoneda, String sCodComp,int iDesde,int iHasta,List lstSelectedFacs, F55ca014[] f14){
		List lstFacturas = null;
		String sql = "from Credhdr as hf where ";
		Session session = HibernateUtilPruebaCn.currentSession();
		
		Credhdr f = null;
		try{
			
			//Busqueda por codigo de cliente
			sql = sql + " hf.id.codcli = "+iParametro+" ";
				
			//agregar moneda a consulta
			if (!sMoneda.equals("01")){
				sql = sql + " and hf.id.moneda = '"+ sMoneda +"' ";
			}			
			//Agregar Compañia 
			if(!sCodComp.equals("01")){// hay compania seleccionada	
				sql = sql + " and hf.id.codcomp = '"+ sCodComp +"' ";					
			}
			/*//Agregar sucursal 
			if(!sCodsuc.equals("01")){// hay sucursal seleccionada					
				sql = sql + " and trim(hf.id.codsuc) = '"+ sCodsuc.trim() +"' ";
			}
			//Agregar unineg 
			if(!sCodunineg.equals("01")){// hay unineg seleccionada					
				sql = sql + " and trim(hf.id.codunineg) = '"+ sCodunineg.trim() +"' ";				
			}*/
			//agregar factura desde
			if(iDesde > 0){			
				sql = sql + " and hf.id.nofactura >= "+ iDesde ;				
			}
			//agregar factura hasta
			if(iHasta > 0){			
				sql = sql + " and hf.id.nofactura <= "+ iHasta ;				
			}
			
			//no incluir en la lista las facturas de negocio seleccionadas
			sql = sql + " and hf.id.nofactura not in (";
			for(int i = 0; i < lstSelectedFacs.size(); i++){
				if (i == lstSelectedFacs.size() - 1){
					sql = sql  + ((Credhdr)lstSelectedFacs.get(i)).getNofactura() + "";
				}else{
					sql = sql  + ((Credhdr)lstSelectedFacs.get(i)).getNofactura() + ",";
				}
			}
			sql = sql + ") "; 
			
			sql = sql + "order by hf.id.fechavenc";
			lstFacturas = session.createQuery(sql).list();
			
			//llenar resto de info
			lstFacturas = llenarInfoFactura(lstFacturas, f14);
		}catch(Exception ex){
			System.out.println("Se capturo una excepcion en FacturaCreditoCtrl.buscarFacturasCredito: " + ex);
		}finally{
			session.close();
		}
		return lstFacturas;
	}
	
/*****************************************************************************/
	public List<Credhdr> llenarInfoFactura(List<Credhdr> lstFacturas,F55ca014[] f14){
		Credhdr f = null;
		String sMonedaBase = "";
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		List<Credhdr>lstFacs = new ArrayList<Credhdr>();
		
		try{
			
			for(int i = 0; i < lstFacturas.size();i++){
				f = lstFacturas.get(i);
				if(f == null){
					lstFacturas.remove(i);
					continue;
				}
				f.setMoneda(f.getId().getMoneda());
				f.setNofactura(f.getId().getNofactura());
				f.setTipofactura(f.getId().getTipofactura());
				f.setPartida(f.getId().getPartida());
				f.setNomcli(f.getId().getNomcli().trim());
				f.setUnineg(f.getId().getUnineg());
				f.setFecha(f.getId().getFecha());
				f.setFechavenc(f.getId().getFechavenc());
				f.setMontoAplicar(new BigDecimal(0));
				//obtener companias x caja
				sMonedaBase = cCtrl.sacarMonedaBase(f14,f.getId().getCodcomp());
				
				if(f.getId().getMoneda().equals(sMonedaBase)){
					f.setMontoPendiente(f.getId().getCpendiente());
					f.getId().setImpuesto(f.getId().getCimpuesto());
					f.getId().setSubtotal(f.getId().getCsubtotal());
					f.getId().setMonto(f.getId().getCtotal());
				}else{
					f.setMontoPendiente(f.getId().getDpendiente());
					f.getId().setImpuesto(f.getId().getDimpuesto());
					f.getId().setSubtotal(f.getId().getDsubtotal());
					f.getId().setMonto(f.getId().getDtotal());
				}
				lstFacs.add(f);
			}
		}catch(Exception ex){
			lstFacs = new ArrayList<Credhdr>();
			System.out.println("Se capturo una excepcion en FacturaCreditoCtrl.llenarInfoFactura: " + ex);
		}
		return lstFacs;
	}
/******************************************************************************************/
/** Método: Nuevo método para consultar Facturas de Crédito.
 *	Fecha:  08/03/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	@SuppressWarnings({ "unchecked"})
	public List<Credhdr> buscarFacturasCredito(int iTipoBusqueda,String sParametro,String sMoneda,
												Date dFechaDesde,Date dFechaHasta, String sCodComp,
												String sCodunineg, String sCodsuc,F55ca014[] f14,
												boolean bMostrarTodo){
		List<Credhdr>lstFacturaCredito = null;
		Session sesion = null;
		Criteria cr = null;
		
		try {
			sesion = HibernateUtilPruebaCn.currentSession();
			
			
			cr = sesion.createCriteria(Credhdr.class);
			cr.addOrder(Order.asc("id.fechavenc"));
			
			if(!bMostrarTodo || sParametro.trim().compareTo("") == 0)
				cr.setMaxResults(100);

			//---- Nombre cliente,codigo cliente, numero factura.
			switch(iTipoBusqueda){
			case(1):
				if(!sParametro.trim().equals(""))
					cr.add(Restrictions.ilike("id.nomcli", sParametro.trim().toLowerCase(),MatchMode.START));
				break;
			case(2):
				if(sParametro.trim().matches("^[0-9]{1,8}$"))
					cr.add(Restrictions.eq("id.codcli",Integer.valueOf(sParametro.trim()).intValue()));
				break;
			case(3):
				if(sParametro.trim().matches("^[0-9]+"))
					cr.add(Restrictions.eq("id.nofactura",Integer.valueOf(sParametro.trim()).intValue()));
				break;
			}
			//---- Moneda, Compania, Unidad de Negocio, Sucursal.
			if(!sMoneda.trim().equals("01"))
				cr.add(Restrictions.eq("id.moneda", sMoneda.trim()));
			if(!sCodComp.trim().equals("01"))
				cr.add(Restrictions.eq("id.codcomp",sCodComp.trim()));
			if(!sCodsuc.trim().equals("01"))
				cr.add(Restrictions.eq("id.codsuc", sCodsuc.trim()));
			if(!sCodunineg.trim().equals("01"))
				cr.add(Restrictions.eq("id.codunineg", sCodunineg.trim()));
			if(dFechaDesde!=null)
				cr.add(Restrictions.ge("id.fecha", dFechaDesde));
			if(dFechaHasta!=null)
				cr.add(Restrictions.le("id.fecha", dFechaHasta));
			
			cr.add(Restrictions.ne("id.tipofactura", "IF"));
			cr.add(Restrictions.ne("id.tipofactura", "MF"));
			
			lstFacturaCredito = (ArrayList<Credhdr>)cr.list();
			
			
			
			if(lstFacturaCredito!=null && lstFacturaCredito.size()>0)
				lstFacturaCredito = llenarInfoFactura(lstFacturaCredito, f14);
			
		} catch (Exception error) {
			lstFacturaCredito = null;
			System.out.println(":Excepción capturada en: Buscar FacturaCreditoCtrl.buscarFacturasCredito "+error);
		}finally{
			try { sesion.close(); } catch (Exception e2){}
		}
		return lstFacturaCredito;
	}
	
/*****************Buscar Facturas de Credito**********************************/
	public List buscarFacturasCredito1(int iTipoBusqueda,String sParametro,String sMoneda,Date dFechaDesde,Date dFechaHasta, String sCodComp,String sCodunineg, String sCodsuc,F55ca014[] f14){
		List lstFacturas = null;
		String sql = "from Credhdr as hf where ";
		Session session = HibernateUtilPruebaCn.currentSession();
		
		FechasUtil f = new FechasUtil();
		
		try{
			
			switch (iTipoBusqueda){
			case (1):			
				//Busqueda por nombre de cliente
				if (!sParametro.equals("")){//hay nombre de cliente
					sql = sql + " hf.id.nomcli like '%"+sParametro.toUpperCase()+"%' ";
				}
				//agregar moneda a consulta
				if (!sParametro.equals("") && !sMoneda.equals("01")){
					sql = sql + " and hf.id.moneda = '"+ sMoneda +"' ";
				}
				else if(sParametro.equals("") && !sMoneda.equals("01")){
					sql = sql + " hf.id.moneda = '"+ sMoneda +"' ";
				}
				//agregar fecha desde
				if (dFechaDesde != null && sParametro.equals("") && sMoneda.equals("01")){//no hay parametro ni moneda; fecha desde primero
					sql = sql.concat(" hf.id.fecha >='")
							 .concat(f.formatDatetoString(dFechaDesde, "yyyy-MM-dd"))
							 .concat("'");
//					sql = sql + " hf.id.fecha >= "+ dFechaDesde +" ";
				}
				else if (dFechaDesde != null){
					sql = sql.concat(" hf.id.fecha >='")
							 .concat(f.formatDatetoString(dFechaDesde, "yyyy-MM-dd"))
							 .concat("'");
//					sql = sql + " and hf.id.fecha >= "+ dFechaDesde +" ";
				}
				//agregar fecha hasta
				if(dFechaDesde == null && dFechaHasta != null && sParametro.equals("") && sMoneda.equals("01")){//no hay parametro ni moneda ni fecha desde; fecha hasta primero
//					sql = sql + " hf.id.fecha <= "+ dFechaHasta +" ";
					sql = sql.concat(" hf.id.fecha >='")
							 .concat(f.formatDatetoString(dFechaHasta, "yyyy-MM-dd"))
							 .concat("'");
				}
				else if(dFechaHasta != null){
//					sql = sql + " and hf.id.fecha <= "+ dFechaHasta +" ";
					sql = sql.concat(" and hf.id.fecha >='")
							 .concat(f.formatDatetoString(dFechaHasta, "yyyy-MM-dd"))
							 .concat("'");
				}
				//Agregar Compañia 
				if(!sCodComp.equals("01")){// hay compania seleccionada
					if(dFechaDesde == null && dFechaHasta == null && sParametro.equals("") && sMoneda.equals("01")){//compania primero
						sql = sql + " hf.id.codcomp = '"+ sCodComp +"' ";
					}else{
						sql = sql + "and hf.id.codcomp = '"+ sCodComp +"' ";
					}
				}
				//Agregar sucursal 
				if(!sCodsuc.equals("01")){// hay sucursal seleccionada
					if(dFechaDesde == null && dFechaHasta == null && sParametro.equals("") && sMoneda.equals("01") && sCodComp.equals("01")){//sucursal primero
						sql = sql + " trim(hf.id.codsuc) = '"+ sCodsuc.trim() +"' ";
					}else{
						sql = sql + "and trim(hf.id.codsuc) = '"+ sCodsuc.trim() +"' ";
					}
				}
				//Agregar unineg 
				if(!sCodunineg.equals("01")){// hay unineg seleccionada
					if(dFechaDesde == null && dFechaHasta == null && sParametro.equals("") && sMoneda.equals("01") && sCodComp.equals("01") && sCodsuc.equals("01")){//unineg primero
						sql = sql + " trim(hf.id.codunineg) = '"+ sCodunineg.trim() +"' ";
					}else{
						sql = sql + "and trim(hf.id.codunineg) = '"+ sCodunineg.trim() +"' ";
					}
				}
				break;
			case (2):
				//Busqueda por codigo de cliente
				if (!sParametro.equals("")){//hay codigo de cliente
					sql = sql + " hf.id.codcli = "+sParametro+" ";
				}
				//agregar moneda a consulta
				if (!sParametro.equals("") && !sMoneda.equals("01")){
					sql = sql + " and hf.id.moneda = '"+ sMoneda +"' ";
				}
				else if(sParametro.equals("") && !sMoneda.equals("01")){
					sql = sql + " hf.id.moneda = '"+ sMoneda +"' ";
				}
				//agregar fecha desde
				if (dFechaDesde != null && sParametro.equals("") && sMoneda.equals("01")){//no hay parametro ni moneda; fecha desde primero
//					sql = sql + " hf.id.fecha >= "+ dFechaDesde +" ";
					sql = sql.concat(" hf.id.fecha >= '")
							.concat(f.formatDatetoString(dFechaDesde, "yyyy-MM-dd")).concat("'");
				}
				else if (dFechaDesde != null){
//					sql = sql + " and hf.id.fecha >= "+ dFechaDesde +" ";
					sql = sql.concat(" and hf.id.fecha >= '")
							 .concat(f.formatDatetoString(dFechaDesde, "yyyy-MM-dd")).concat("'");
				}
				//agregar fecha hasta
				if(dFechaDesde == null && dFechaHasta != null && sParametro.equals("") && sMoneda.equals("01")){//no hay parametro ni moneda; fecha hasta primero
//					sql = sql + " hf.id.fecha <= "+ dFechaHasta +" ";
					sql = sql.concat(" hf.id.fecha <= '")
							 .concat(f.formatDatetoString(dFechaHasta, "yyyy-MM-dd")).concat("'");
				}
				else if(dFechaHasta != null){
//					sql = sql + " and hf.id.fecha <= "+ dFechaHasta +" ";
					sql = sql.concat(" and hf.id.fecha <= '")
					 		 .concat(f.formatDatetoString(dFechaHasta, "yyyy-MM-dd")).concat("'");
				}
				//Agregar Compañia 
				if(!sCodComp.equals("01")){// hay compania seleccionada
					if(dFechaDesde == null && dFechaHasta == null && sParametro.equals("") && sMoneda.equals("01")){//compania primero
						sql = sql + " hf.id.codcomp = '"+ sCodComp +"' ";
					}else{
						sql = sql + "and hf.id.codcomp = '"+ sCodComp +"' ";
					}
				}
				//Agregar sucursal 
				if(!sCodsuc.equals("01")){// hay sucursal seleccionada
					if(dFechaDesde == null && dFechaHasta == null && sParametro.equals("") && sMoneda.equals("01") && sCodComp.equals("01")){//sucursal primero
						sql = sql + " trim(hf.id.codsuc) = '"+ sCodsuc.trim() +"' ";
					}else{
						sql = sql + "and trim(hf.id.codsuc) = '"+ sCodsuc.trim() +"' ";
					}
				}
				//Agregar unineg 
				if(!sCodunineg.equals("01")){// hay unineg seleccionada
					if(dFechaDesde == null && dFechaHasta == null && sParametro.equals("") && sMoneda.equals("01") && sCodComp.equals("01") && sCodsuc.equals("01")){//unineg primero
						sql = sql + " trim(hf.id.codunineg) = '"+ sCodunineg.trim() +"' ";
					}else{
						sql = sql + "and trim(hf.id.codunineg) = '"+ sCodunineg.trim() +"' ";
					}
				}
				break;
			case(3):
			
				//Busqueda por codigo de factura
				if (!sParametro.equals("")){//hay codigo de factura
					sql = sql + " hf.id.nofactura = "+sParametro+" ";
				}
				//agregar moneda a consulta
				if (!sParametro.equals("") && !sMoneda.equals("01")){
					sql = sql + " and hf.id.moneda = '"+ sMoneda +"' ";
				}
				else if(sParametro.equals("") && !sMoneda.equals("01")){
					sql = sql + " hf.id.moneda = '"+ sMoneda +"' ";
				}
				//agregar fecha desde
				if (dFechaDesde != null && sParametro.equals("") && sMoneda.equals("01")){//no hay parametro ni moneda; fecha desde primero
//					sql = sql + " hf.id.fecha >= "+ dFechaDesde +" ";
					sql = sql.concat(" hf.id.fecha >= '")
							.concat(f.formatDatetoString(dFechaDesde, "yyyy-MM-dd")).concat("'");
				}
				else if (dFechaDesde != null){
//					sql = sql + " and hf.id.fecha >= "+ dFechaDesde +" ";
					sql = sql.concat(" and hf.id.fecha >= '")
							 .concat(f.formatDatetoString(dFechaDesde, "yyyy-MM-dd")).concat("'");
				}
				//agregar fecha hasta
				if(dFechaDesde == null && dFechaHasta != null && sParametro.equals("") && sMoneda.equals("01")){//no hay parametro ni moneda; fecha hasta primero
//					sql = sql + " hf.id.fecha <= "+ dFechaHasta +" ";
					sql = sql.concat(" hf.id.fecha <= '")
					 	.concat(f.formatDatetoString(dFechaHasta, "yyyy-MM-dd")).concat("'");
				}
				else if(dFechaHasta != null){
//					sql = sql + " and hf.id.fecha <= "+ dFechaHasta +" ";
					sql = sql.concat(" and hf.id.fecha <= '")
							 .concat(f.formatDatetoString(dFechaHasta, "yyyy-MM-dd")).concat("'");
				}
				//Agregar Compañia 
				if(!sCodComp.equals("01")){// hay compania seleccionada
					if(dFechaDesde == null && dFechaHasta == null && sParametro.equals("") && sMoneda.equals("01")){//compania primero
						sql = sql + " hf.id.codcomp = '"+ sCodComp +"' ";
					}else{
						sql = sql + "and hf.id.codcomp = '"+ sCodComp +"' ";
					}
				}
				//Agregar sucursal 
				if(!sCodsuc.equals("01")){// hay sucursal seleccionada
					if(dFechaDesde == null && dFechaHasta == null && sParametro.equals("") && sMoneda.equals("01") && sCodComp.equals("01")){//sucursal primero
						sql = sql + " trim(hf.id.codsuc) = '"+ sCodsuc.trim() +"' ";
					}else{
						sql = sql + "and trim(hf.id.codsuc) = '"+ sCodsuc.trim() +"' ";
					}
				}
				//Agregar unineg 
				if(!sCodunineg.equals("01")){// hay unineg seleccionada
					if(dFechaDesde == null && dFechaHasta == null && sParametro.equals("") && sMoneda.equals("01") && sCodComp.equals("01") && sCodsuc.equals("01")){//unineg primero
						sql = sql + " trim(hf.id.codunineg) = '"+ sCodunineg.trim() +"' ";
					}else{
						sql = sql + "and trim(hf.id.codunineg) = '"+ sCodunineg.trim() +"' ";
					}
				}
				break;				
			}
			sql = sql + "order by hf.id.fechavenc";
			lstFacturas = session.createQuery(sql).list();
			
			//llenar resto de info
			lstFacturas = llenarInfoFactura(lstFacturas, f14);
		}catch(Exception ex){
			System.out.println("Se capturo una excepcion en FacturaCreditoCtrl.buscarFacturasCredito: " + ex);
		}finally{
			try{session.close();}catch(Exception ex2){ex2.printStackTrace();};
		}
		return lstFacturas;
	}
/*****************FORMATEAR CAMPOS DE CREDITO PARA VISUALIZACION**********************************/
	public List formatFacturaCredito(List lstFacturas) {
		Divisas divisas = new Divisas();
		List lstResultado = new ArrayList();
		Hfacturajde[] hFacJde = new Hfacturajde[lstFacturas.size()];
		FacturaCredito hFac = null;
		
		try {
			for (int i = 0; i < lstFacturas.size(); i++) {
				hFacJde[i] = (Hfacturajde) lstFacturas.get(i);
				JulianToCalendar fecha = new JulianToCalendar(hFacJde[i].getId().getFecha());
				JulianToCalendar fechavenc = new JulianToCalendar(hFacJde[i].getId().getFechavenc());
				JulianToCalendar fechagrab = new JulianToCalendar(hFacJde[i].getId().getFechagrab());
				JulianToCalendar fechalm = new JulianToCalendar(hFacJde[i].getId().getFechalm());
				JulianToCalendar fechaimp = new JulianToCalendar(hFacJde[i].getId().getFechaimp());
				JulianToCalendar fechabatch = new JulianToCalendar(hFacJde[i].getId().getFechabatch());
				
				hFac = new FacturaCredito();
				hFac.setNofactura(hFacJde[i].getId().getNofactura());
				hFac.setTipofactura(hFacJde[i].getId().getTipofactura());
				hFac.setPartida(hFacJde[i].getId().getPartida());
				hFac.setFecha(fecha.toString());
				hFac.setFechavenc(fechavenc.toString());
				hFac.setCodcli(hFacJde[i].getId().getCodcli());
				hFac.setNomcli(hFacJde[i].getId().getNomcli());
				hFac.setCodunineg(hFacJde[i].getId().getCodunineg());
				hFac.setUnineg(hFacJde[i].getId().getUnineg());
				hFac.setCodsuc(hFacJde[i].getId().getCodsuc());
				hFac.setNomsuc(hFacJde[i].getId().getNomsuc());
				hFac.setCodcomp(hFacJde[i].getId().getCodcomp());
				hFac.setNomcomp(hFacJde[i].getId().getNomcomp());
				hFac.setCsubtotal(hFacJde[i].getId().getCsubtotal()/100.00);
				hFac.setDsubtotal(hFacJde[i].getId().getDsubtotal()/100.00);
				hFac.setCimpuesto(divisas.formatDouble(hFacJde[i].getId().getCimpuesto()/100.00));
				hFac.setDimpuesto(divisas.formatDouble(hFacJde[i].getId().getDimpuesto()/100.00));
				hFac.setCtotal(hFacJde[i].getId().getCtotal()/100.00);
				hFac.setDtotal(hFacJde[i].getId().getDtotal()/100.00);
				hFac.setMoneda(hFacJde[i].getId().getMoneda());
				hFac.setTasa(hFacJde[i].getId().getTasa());
				hFac.setTipopago(hFacJde[i].getId().getTipopago());
				hFac.setDesctpago(hFacJde[i].getId().getDesctpago());
				hFac.setCpendiente(hFacJde[i].getId().getCpendiente()/100.00);
				hFac.setDpendiente(hFacJde[i].getId().getDpendiente()/100.00);
				hFac.setCodepago(hFacJde[i].getId().getCodepago());
				hFac.setDescepago(hFacJde[i].getId().getDescepago());
				hFac.setFechagrab(fechagrab.toString());
				hFac.setHechopor(hFacJde[i].getId().getHechopor());
				hFac.setPantalla(hFacJde[i].getId().getPantalla());
				hFac.setFechalm(fechalm.toString());
				hFac.setFechaimp(fechaimp.toString());
				hFac.setCompenslm(hFacJde[i].getId().getCompenslm());
				hFac.setNobatch(hFacJde[i].getId().getNobatch());
				hFac.setFechabatch(fechabatch.toString());
				hFac.setObservaciones(hFacJde[i].getId().getObservaciones());
				hFac.setReferencia(hFacJde[i].getId().getReferencia());
				hFac.setNumorden(hFacJde[i].getId().getNumorden());
				hFac.setTipoorden(hFacJde[i].getId().getTipoorden());
				hFac.setCdescdisp(divisas.formatDouble(hFacJde[i].getId().getCdescdisp()/100.00));
				hFac.setDdescdisp(divisas.formatDouble(hFacJde[i].getId().getDdescdisp()/100.00));
				hFac.setCdesctom(divisas.formatDouble(hFacJde[i].getId().getCdesctom()/100.00));
				hFac.setDdesctom(divisas.formatDouble(hFacJde[i].getId().getDdesctom()/100.00));
				hFac.setCodcont(hFacJde[i].getId().getCodcont());
				if (hFacJde[i].getId().getMoneda().equals("COR")){
					hFac.setTotal(hFac.getCtotal());
					hFac.setEquiv(hFac.getCtotal());
					hFac.setMontoAplicar(0.00);
				}else{
					hFac.setTotal(hFac.getDtotal());
					hFac.setEquiv(hFac.getCtotal());
					hFac.setMontoAplicar(0.00);
				}
				lstResultado.add(hFac);
			}
		} catch (Exception ex) {
			System.out.println("==> Excepción capturada en FacturaCreditoCtrl.formatFacturaCredito: "+ ex);
		}
		return lstResultado;
	}
/*****************************************************************************************************/	
/***LISTAR FACTURAS DE CREDITO PENDIENTES DE PAGO************************************************************/
	public List listarFacturasCreditoPendientes() {
		String sql = "from Hfacturajde as f where f.id.cpendiente > 0 order by f.id.tipofactura, f.id.nofactura ";
		Session session = null;
		List result = new ArrayList();
		
		try {
			session = HibernateUtilPruebaCn.currentSession();
			
			result = session.createQuery(sql).list();
			
		
			List lstHfacturasCredito = formatFacturaCredito(result);
			
		} catch (Exception ex) {
			System.out.println("==> Excepción capturada en FacturaCreditoCtrl.listarFacturasCreditoPendientes: "+ ex);
		} 
		return result;
	}
/*****BUSCAR FACTURAS DE CREDITO POR TIPO DE BUSQUEDA(1 = por nombre de cliente; 2 = por codigo de cliente; 3 = por codigo de factura) POR EL PARAMETRO EL CUAL DEPENDE DEL TIPO DE BUSQUEDA******/
	public List buscarFacturaxParametros(int iTipoBusqueda, String sParametro){
		List lstResultado = new ArrayList();
		String sql = "from Hfacturajde as hf ";
		Session session = HibernateUtilPruebaCn.currentSession();
		
		try{
		
			switch (iTipoBusqueda){
				case (1):
					//Busqueda por nombre del cliente
					sql = sql + "where trim(hf.id.nomcli) = '"
					+ sParametro.toUpperCase()
					+ "' and hf.id.cpendiente > 0";
					break;
				case (2):
					//Busqueda por codigo de cliente
					sql = sql + "where cast(hf.id.codcli as string) = '"
							+ sParametro
							+ "' and hf.id.cpendiente > 0";
					break;
				case(3):
					sql = sql + "where cast(hf.id.nofactura as string) = '"
					+ sParametro
					+ "' and hf.id.cpendiente > 0";
					break;
			}
			lstResultado = session.createQuery(sql).list();
			
			
		}catch(Exception ex){
			System.out.println("Se capturo una exepcion en FacturaCreditoCtrl.buscarFacturaxParametros: " + ex);
		}
		return lstResultado;
	}
/*****BUSCAR FACTURAS DE CREDITO POR TIPO DE BUSQUEDA(1 = por nombre de cliente; 2 = por codigo de cliente; 3 = por codigo de factura) POR EL PARAMETRO EL CUAL DEPENDE DEL TIPO DE BUSQUEDA******/
	public List buscarFacturaxParametros(int iTipoBusqueda, String sParametro,String sMoneda,Date dFechaDesde,Date dFechaHasta, String sCodComp,String sCodunineg, String sCodsuc){
		List lstResultado = new ArrayList();
		String sql = "from Hfacturajde as hf where ";
		Session session = HibernateUtilPruebaCn.currentSession();
		
		String sFechaDesde = "";
		String sFechaHasta = "";
		Calendar calFechaDesde = Calendar.getInstance(),calFechaHasta = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try{
		
			switch (iTipoBusqueda){
				case (1):			
					//Busqueda por nombre de cliente
					if (!sParametro.equals("")){//hay nombre de cliente
						sql = sql + " trim(hf.id.nomcli) like '%"+sParametro.toUpperCase()+"%' ";
					}
					//agregar moneda a consulta
					if (!sParametro.equals("") && !sMoneda.equals("01")){
						sql = sql + " and hf.id.moneda = '"+ sMoneda +"' ";
					}
					else if(sParametro.equals("") && !sMoneda.equals("01")){
						sql = sql + " hf.id.moneda = '"+ sMoneda +"' ";
					}
					//agregar fecha desde
					if (dFechaDesde != null && sParametro.equals("") && sMoneda.equals("01")){//no hay parametro ni moneda; fecha desde primero
						sFechaDesde = sdf.format(dFechaDesde);
						calFechaDesde.setTime(dFechaDesde);
						CalendarToJulian julian = new CalendarToJulian(calFechaDesde);
						int iFechaDesde = julian.getDate();
						sql = sql + " hf.id.fecha >= "+ iFechaDesde +" ";
					}
					else if (dFechaDesde != null){
						sFechaDesde = sdf.format(dFechaDesde);
						calFechaDesde.setTime(dFechaDesde);
						CalendarToJulian julian = new CalendarToJulian(calFechaDesde);
						int iFechaDesde = julian.getDate();
						sql = sql + " and hf.id.fecha >= "+ iFechaDesde +" ";
					}
					//agregar fecha hasta
					if(dFechaDesde == null && dFechaHasta != null && sParametro.equals("") && sMoneda.equals("01")){//no hay parametro ni moneda ni fecha desde; fecha hasta primero
						sFechaHasta = sdf.format(dFechaHasta);
						calFechaHasta.setTime(dFechaHasta);
						CalendarToJulian julian = new CalendarToJulian(calFechaHasta);
						int iFechaHasta = julian.getDate();
						sql = sql + " hf.id.fecha <= "+ iFechaHasta +" ";
					}
					else if(dFechaHasta != null){
						sFechaHasta = sdf.format(dFechaHasta);
						calFechaHasta.setTime(dFechaHasta);
						CalendarToJulian julian = new CalendarToJulian(calFechaHasta);
						int iFechaHasta = julian.getDate();
						sql = sql + " and hf.id.fecha <= "+ iFechaHasta +" ";
					}
					//Agregar Compañia 
					if(!sCodComp.equals("01")){// hay compania seleccionada
						if(dFechaDesde == null && dFechaHasta == null && sParametro.equals("") && sMoneda.equals("01")){//compania primero
							sql = sql + " hf.id.codcomp = '"+ sCodComp +"' ";
						}else{
							sql = sql + "and hf.id.codcomp = '"+ sCodComp +"' ";
						}
					}
					//Agregar sucursal 
					if(!sCodsuc.equals("01")){// hay sucursal seleccionada
						if(dFechaDesde == null && dFechaHasta == null && sParametro.equals("") && sMoneda.equals("01") && sCodComp.equals("01")){//sucursal primero
							sql = sql + " trim(hf.id.codsuc) = '"+ sCodsuc.trim() +"' ";
						}else{
							sql = sql + "and trim(hf.id.codsuc) = '"+ sCodsuc.trim() +"' ";
						}
					}
					//Agregar unineg 
					if(!sCodunineg.equals("01")){// hay unineg seleccionada
						if(dFechaDesde == null && dFechaHasta == null && sParametro.equals("") && sMoneda.equals("01") && sCodComp.equals("01") && sCodsuc.equals("01")){//unineg primero
							sql = sql + " trim(hf.id.codunineg) = '"+ sCodunineg.trim() +"' ";
						}else{
							sql = sql + "and trim(hf.id.codunineg) = '"+ sCodunineg.trim() +"' ";
						}
					}
					break;
				case (2):
					//Busqueda por codigo de cliente
					if (!sParametro.equals("")){//hay codigo de cliente
						sql = sql + " cast(hf.id.codcli as string) = '"+sParametro+"' ";
					}
					//agregar moneda a consulta
					if (!sParametro.equals("") && !sMoneda.equals("01")){
						sql = sql + " and hf.id.moneda = '"+ sMoneda +"' ";
					}
					else if(sParametro.equals("") && !sMoneda.equals("01")){
						sql = sql + " hf.id.moneda = '"+ sMoneda +"' ";
					}
					//agregar fecha desde
					if (dFechaDesde != null && sParametro.equals("") && sMoneda.equals("01")){//no hay parametro ni moneda; fecha desde primero
						sFechaDesde = sdf.format(dFechaDesde);
						calFechaDesde.setTime(dFechaDesde);
						CalendarToJulian julian = new CalendarToJulian(calFechaDesde);
						int iFechaDesde = julian.getDate();
						sql = sql + " hf.id.fecha >= "+ iFechaDesde +" ";
					}
					else if (dFechaDesde != null){
						sFechaDesde = sdf.format(dFechaDesde);
						calFechaDesde.setTime(dFechaDesde);
						CalendarToJulian julian = new CalendarToJulian(calFechaDesde);
						int iFechaDesde = julian.getDate();
						sql = sql + " and hf.id.fecha >= "+ iFechaDesde +" ";
					}
					//agregar fecha hasta
					if(dFechaDesde == null && dFechaHasta != null && sParametro.equals("") && sMoneda.equals("01")){//no hay parametro ni moneda; fecha hasta primero
						sFechaHasta = sdf.format(dFechaHasta);
						calFechaHasta.setTime(dFechaHasta);
						CalendarToJulian julian = new CalendarToJulian(calFechaHasta);
						int iFechaHasta = julian.getDate();
						sql = sql + " hf.id.fecha <= "+ iFechaHasta +" ";
					}
					else if(dFechaHasta != null){
						sFechaHasta = sdf.format(dFechaHasta);
						calFechaHasta.setTime(dFechaHasta);
						CalendarToJulian julian = new CalendarToJulian(calFechaHasta);
						int iFechaHasta = julian.getDate();
						sql = sql + " and hf.id.fecha <= "+ iFechaHasta +" ";
					}
					//Agregar Compañia 
					if(!sCodComp.equals("01")){// hay compania seleccionada
						if(dFechaDesde == null && dFechaHasta == null && sParametro.equals("") && sMoneda.equals("01")){//compania primero
							sql = sql + " hf.id.codcomp = '"+ sCodComp +"' ";
						}else{
							sql = sql + "and hf.id.codcomp = '"+ sCodComp +"' ";
						}
					}
					//Agregar sucursal 
					if(!sCodsuc.equals("01")){// hay sucursal seleccionada
						if(dFechaDesde == null && dFechaHasta == null && sParametro.equals("") && sMoneda.equals("01") && sCodComp.equals("01")){//sucursal primero
							sql = sql + " trim(hf.id.codsuc) = '"+ sCodsuc.trim() +"' ";
						}else{
							sql = sql + "and trim(hf.id.codsuc) = '"+ sCodsuc.trim() +"' ";
						}
					}
					//Agregar unineg 
					if(!sCodunineg.equals("01")){// hay unineg seleccionada
						if(dFechaDesde == null && dFechaHasta == null && sParametro.equals("") && sMoneda.equals("01") && sCodComp.equals("01") && sCodsuc.equals("01")){//unineg primero
							sql = sql + " trim(hf.id.codunineg) = '"+ sCodunineg.trim() +"' ";
						}else{
							sql = sql + "and trim(hf.id.codunineg) = '"+ sCodunineg.trim() +"' ";
						}
					}
					break;
				case(3):
				
					//Busqueda por codigo de factura
					if (!sParametro.equals("")){//hay codigo de factura
						sql = sql + " cast(hf.id.nofactura as string) = '"+sParametro+"' ";
					}
					//agregar moneda a consulta
					if (!sParametro.equals("") && !sMoneda.equals("01")){
						sql = sql + " and hf.id.moneda = '"+ sMoneda +"' ";
					}
					else if(sParametro.equals("") && !sMoneda.equals("01")){
						sql = sql + " hf.id.moneda = '"+ sMoneda +"' ";
					}
					//agregar fecha desde
					if (dFechaDesde != null && sParametro.equals("") && sMoneda.equals("01")){//no hay parametro ni moneda; fecha desde primero
						sFechaDesde = sdf.format(dFechaDesde);
						calFechaDesde.setTime(dFechaDesde);
						CalendarToJulian julian = new CalendarToJulian(calFechaDesde);
						int iFechaDesde = julian.getDate();
						sql = sql + " hf.id.fecha >= "+ iFechaDesde +" ";
					}
					else if (dFechaDesde != null){
						sFechaDesde = sdf.format(dFechaDesde);
						calFechaDesde.setTime(dFechaDesde);
						CalendarToJulian julian = new CalendarToJulian(calFechaDesde);
						int iFechaDesde = julian.getDate();
						sql = sql + " and hf.id.fecha >= "+ iFechaDesde +" ";
					}
					//agregar fecha hasta
					if(dFechaDesde == null && dFechaHasta != null && sParametro.equals("") && sMoneda.equals("01")){//no hay parametro ni moneda; fecha hasta primero
						sFechaHasta = sdf.format(dFechaHasta);
						calFechaHasta.setTime(dFechaHasta);
						CalendarToJulian julian = new CalendarToJulian(calFechaHasta);
						int iFechaHasta = julian.getDate();
						sql = sql + " hf.id.fecha <= "+ iFechaHasta +" ";
					}
					else if(dFechaHasta != null){
						sFechaHasta = sdf.format(dFechaHasta);
						calFechaHasta.setTime(dFechaHasta);
						CalendarToJulian julian = new CalendarToJulian(calFechaHasta);
						int iFechaHasta = julian.getDate();
						sql = sql + " and hf.id.fecha <= "+ iFechaHasta +" ";
					}
					//Agregar Compañia 
					if(!sCodComp.equals("01")){// hay compania seleccionada
						if(dFechaDesde == null && dFechaHasta == null && sParametro.equals("") && sMoneda.equals("01")){//compania primero
							sql = sql + " hf.id.codcomp = '"+ sCodComp +"' ";
						}else{
							sql = sql + "and hf.id.codcomp = '"+ sCodComp +"' ";
						}
					}
					//Agregar sucursal 
					if(!sCodsuc.equals("01")){// hay sucursal seleccionada
						if(dFechaDesde == null && dFechaHasta == null && sParametro.equals("") && sMoneda.equals("01") && sCodComp.equals("01")){//sucursal primero
							sql = sql + " trim(hf.id.codsuc) = '"+ sCodsuc.trim() +"' ";
						}else{
							sql = sql + "and trim(hf.id.codsuc) = '"+ sCodsuc.trim() +"' ";
						}
					}
					//Agregar unineg 
					if(!sCodunineg.equals("01")){// hay unineg seleccionada
						if(dFechaDesde == null && dFechaHasta == null && sParametro.equals("") && sMoneda.equals("01") && sCodComp.equals("01") && sCodsuc.equals("01")){//unineg primero
							sql = sql + " trim(hf.id.codunineg) = '"+ sCodunineg.trim() +"' ";
						}else{
							sql = sql + "and trim(hf.id.codunineg) = '"+ sCodunineg.trim() +"' ";
						}
					}
					break;				
			}
			sql = sql + " and hf.id.cpendiente <> 0 order by hf.id.fechavenc";
			lstResultado = session.createQuery(sql).list();
		
		}catch(Exception ex){
			System.out.println("Se capturo una exepcion en FacturaCreditoCtrl.buscarFacturaxParametros: " + ex);
		}
		return lstResultado;
	}
/***OBTIENE EL DETALLE DE UNA FACTURA DE CREDITO********************************************************************************************************************/
	public List getDetalleFacturaCredito(int iNofactura,String sTipoFactura,String sCodsuc,String sCodunineg){
		Session session = null;
		List lstResultado = new ArrayList();
		
		String sql = "from Creddet as d where d.id.nofactura = "+iNofactura+" and trim(d.id.tipofactura) = '"+sTipoFactura.trim()+"' " +
				"and trim(d.id.codsuc) = '"+sCodsuc+"' and trim(d.id.codunineg) = '" + sCodunineg + "'";
		try{
			session = HibernateUtilPruebaCn.currentSession();
			
			lstResultado = session.createQuery(sql).list();
			
			
		}catch(Exception ex){
			System.out.println("Se capturo una excepcion en FacturaCreditoCtrl.getDetalleFactura: " + ex);
		}finally{
			try{session.close();}catch(Exception ex2){ex2.printStackTrace();};
		}
		return lstResultado;
	}
/***********************************************************************************************************************/
	public List formatDetalleCredito(List lstDetalle,BigDecimal tasa,String sMoneda){
		Dfactura dFac = null;
		Dfacturajde dFacjde = null;
		try{
			for (int i = 0; i < lstDetalle.size();i++){
				dFacjde = (Dfacturajde)lstDetalle.get(i);
				
				dFac = new Dfactura(
						dFacjde.getId().getNofactura(),
						dFacjde.getId().getTipofactura(),
						dFacjde.getId().getCoditem(),
						dFacjde.getId().getDescitem(),
						dFacjde.getId().getPreciounit()/100.0,
						dFacjde.getId().getCant(),
						dFacjde.getId().getImpuesto(),
						dFacjde.getId().getFactor()/1000.0,
						sMoneda,
						tasa
				);
				lstDetalle.remove(i);
				lstDetalle.add(i, dFac);
			}
		}catch(Exception ex){
			System.out.println("Se capturo una excepcion en FacturaCreditoCtrl.formatDetalleCredito: " + ex);
		}
		return lstDetalle;
	}
/*****buscar facturas de contado por paramtros con datasource****************************************************************************************************/
	public List buscarFacturasxParametros(Connection cn,int iTipoBusqueda, String sParametro,String sMoneda,Date dFechaDesde,Date dFechaHasta, String sCodComp,String sCodunineg, String sCodsuc){
		List lstFacturas = new ArrayList();
		PreparedStatement ps = null;
		FacturaCredito hFac = null;
		Divisas divisas = new Divisas();
		String sql = "SELECT DISTINCT CAST(TRIM(RPDCT) AS VARCHAR(2) CCSID 37) TIPOFACTURA, RPDOC AS NOFACTURA,	CAST (RPSFX AS VARCHAR(3) CCSID 37) AS PARTIDA, " +
				"RPDIVJ AS FECHA, RPDDJ AS FECHAVENC,RPAN8 AS CODCLI, RPALPH AS NOMCLI, CAST(TRIM(RPMCU) AS VARCHAR(12) CCSID 37) CODUNINEG, UN.MCDL01 AS UNINEG, " + 
				"CAST(SUBSTRING(RPKCO,4,5) AS VARCHAR(5) CCSID 37) CODSUC,(SELECT MCDL01 FROM "+PropertiesSystem.JDEDTA+".F0006 WHERE SUBSTRING(RPKCO,4,5) = SUBSTRING(MCMCU,11,12) AND MCSTYL = 'BS') AS NOMSUC, " +	 
				"CAST(TRIM(UN.MCRP01) AS VARCHAR(3) CCSID 37) CODCOMP, CO.DRDL01 AS NOMCOMP, "+
				"(CASE WHEN RPTXA1 = 'EXE' "+
					"THEN "+
						"RPATXN "+
					"ELSE "+ 
						"RPATXA "+
					"END) AS CSUBTOTAL, "+ 
				"(CASE WHEN RPTXA1 = 'EXE' "+
					"THEN "+
						"RPCTXN "+
					"ELSE "+ 
						"RPCTXA "+
					"END) AS DSUBTOTAL, "+ 
				"RPSTAM AS CIMPUESTO,RPCTAM AS DIMPUESTO,RPAG AS CTOTAL,RPACR AS DTOTAL,CAST(TRIM(RPCRCD) AS VARCHAR(3) CCSID 37) MONEDA, RPCRR AS TASA, "+
				"CAST(TRIM(RPPTC) AS VARCHAR(3) CCSID 37) TIPOPAGO, PT.PNPTD AS DESCTPAGO,B.RPAAP AS CPENDIENTE, B.RPFAP AS DPENDIENTE, CAST(RPPST AS VARCHAR(1) CCSID 37) AS CODEPAGO, "+
				"EP.DRDL01 AS DESCEPAGO,RPUPMJ AS FECHAGRAB, CAST(TRIM(RPUSER) AS VARCHAR(10) CCSID 37) HECHOPOR,CAST(TRIM(RPPID) AS VARCHAR(10) CCSID 37) PANTALLA, "+
				"RPDGJ AS FECHALM,RPDSVJ AS FECHAIMP,CAST(RPGLC AS VARCHAR(4) CCSID 37) AS COMPENSLM,RPICU AS NOBATCH,RPDICJ AS FECHABATCH,RPRMK AS OBSERVACIONES, "+
				"CAST(RPVR01 AS VARCHAR(25) CCSID 37) AS REFERENCIA,RPSDOC AS NUMORDEN,CAST (RPSDCT AS VARCHAR(2) CCSID 37) AS TIPOORDEN,RPADSC AS CDESCDISP, "+ 
				"RPADSA AS CDESCTOM,RPCDS AS DDESCDISP,RPCDSA AS DDESCTOM,CAST(RPPOST AS VARCHAR(1) CCSID 37) AS CODCONT "+
				"FROM "+ 
						""+PropertiesSystem.JDEDTA+".F0311 B "+ 
						"INNER JOIN "+PropertiesSystem.JDEDTA+".F0006 UN ON B.RPMCU = UN.MCMCU "+  
					 	"INNER JOIN "+PropertiesSystem.JDECOM+".F0005 CO ON (UN.MCRP01 = SUBSTRING(CO.DRKY,8,10) AND CO.DRSY = '00' AND CO.DRRT = '01' AND CO.DRDL02 = 'F') " +
						"INNER JOIN "+PropertiesSystem.JDECOM+".F0005 EP ON (TRIM(RPPST) = TRIM(EP.DRKY) AND EP.DRSY='00' AND EP.DRRT = 'PS') "+
						"INNER JOIN "+PropertiesSystem.JDEDTA+".F0014 PT ON (TRIM(RPPTC) = TRIM(PNPTC)) "+ 
				"WHERE RPDCTO <> 'X9' AND ";
		String sFechaDesde = "";
		String sFechaHasta = "";
		Calendar calFechaDesde = Calendar.getInstance(),calFechaHasta = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try{
			switch (iTipoBusqueda){
			case (1):			
				//Busqueda por nombre de cliente
				if (!sParametro.equals("")){//hay nombre de cliente
					sql = sql + " trim(RPALPH) like '%"+sParametro.toUpperCase()+"%' ";
				}
				//agregar moneda a consulta
				if (!sParametro.equals("") && !sMoneda.equals("01")){
					sql = sql + " and RPCRCD = '"+ sMoneda +"' ";
				}
				else if(sParametro.equals("") && !sMoneda.equals("01")){
					sql = sql + " RPCRCD = '"+ sMoneda +"' ";
				}
				//agregar fecha desde
				if (dFechaDesde != null && sParametro.equals("") && sMoneda.equals("01")){//no hay parametro ni moneda; fecha desde primero
					sFechaDesde = sdf.format(dFechaDesde);
					calFechaDesde.setTime(dFechaDesde);
					CalendarToJulian julian = new CalendarToJulian(calFechaDesde);
					int iFechaDesde = julian.getDate();
					sql = sql + " RPDIVJ >= "+ iFechaDesde +" ";
				}
				else if (dFechaDesde != null){
					sFechaDesde = sdf.format(dFechaDesde);
					calFechaDesde.setTime(dFechaDesde);
					CalendarToJulian julian = new CalendarToJulian(calFechaDesde);
					int iFechaDesde = julian.getDate();
					sql = sql + " and  RPDIVJ >= "+ iFechaDesde +" ";
				}
				//agregar fecha hasta
				if(dFechaDesde == null && dFechaHasta != null && sParametro.equals("") && sMoneda.equals("01")){//no hay parametro ni moneda ni fecha desde; fecha hasta primero
					sFechaHasta = sdf.format(dFechaHasta);
					calFechaHasta.setTime(dFechaHasta);
					CalendarToJulian julian = new CalendarToJulian(calFechaHasta);
					int iFechaHasta = julian.getDate();
					sql = sql + " RPDIVJ <= "+ iFechaHasta +" ";
				}
				else if(dFechaHasta != null){
					sFechaHasta = sdf.format(dFechaHasta);
					calFechaHasta.setTime(dFechaHasta);
					CalendarToJulian julian = new CalendarToJulian(calFechaHasta);
					int iFechaHasta = julian.getDate();
					sql = sql + " and RPDIVJ <= "+ iFechaHasta +" ";
				}
				//Agregar Compañia 
				if(!sCodComp.equals("01")){// hay compania seleccionada
					if(dFechaDesde == null && dFechaHasta == null && sParametro.equals("") && sMoneda.equals("01")){//compania primero
						sql = sql + " TRIM(UN.MCRP01) = '"+ sCodComp +"' ";
					}else{
						sql = sql + "and TRIM(UN.MCRP01) = '"+ sCodComp +"' ";
					}
				}
				//Agregar sucursal 
				if(!sCodsuc.equals("01")){// hay sucursal seleccionada
					if(dFechaDesde == null && dFechaHasta == null && sParametro.equals("") && sMoneda.equals("01") && sCodComp.equals("01")){//sucursal primero
						sql = sql + " SUBSTRING(RPKCO,4,5)  = '"+ sCodsuc.trim() +"' ";
					}else{
						sql = sql + "and SUBSTRING(RPKCO,4,5)  = '"+ sCodsuc.trim() +"' ";
					}
				}
				//Agregar unineg 
				if(!sCodunineg.equals("01")){// hay unineg seleccionada
					if(dFechaDesde == null && dFechaHasta == null && sParametro.equals("") && sMoneda.equals("01") && sCodComp.equals("01") && sCodsuc.equals("01")){//unineg primero
						sql = sql + " TRIM(RPMCU) = '"+ sCodunineg.trim() +"' ";
					}else{
						sql = sql + "and TRIM(RPMCU) = '"+ sCodunineg.trim() +"' ";
					}
				}
				break;
			case (2):
				//Busqueda por codigo de cliente
				if (!sParametro.equals("")){//hay codigo de cliente
					sql = sql + " CAST(RPAN8 AS VARCHAR(8)) = '"+sParametro+"' ";
				}
				//agregar moneda a consulta
				if (!sParametro.equals("") && !sMoneda.equals("01")){
					sql = sql + " and RPCRCD = '"+ sMoneda +"' ";
				}
				else if(sParametro.equals("") && !sMoneda.equals("01")){
					sql = sql + " RPCRCD = '"+ sMoneda +"' ";
				}
				//agregar fecha desde
				if (dFechaDesde != null && sParametro.equals("") && sMoneda.equals("01")){//no hay parametro ni moneda; fecha desde primero
					sFechaDesde = sdf.format(dFechaDesde);
					calFechaDesde.setTime(dFechaDesde);
					CalendarToJulian julian = new CalendarToJulian(calFechaDesde);
					int iFechaDesde = julian.getDate();
					sql = sql + " RPDIVJ >= "+ iFechaDesde +" ";
				}
				else if (dFechaDesde != null){
					sFechaDesde = sdf.format(dFechaDesde);
					calFechaDesde.setTime(dFechaDesde);
					CalendarToJulian julian = new CalendarToJulian(calFechaDesde);
					int iFechaDesde = julian.getDate();
					sql = sql + " and RPDIVJ >= "+ iFechaDesde +" ";
				}
				//agregar fecha hasta
				if(dFechaDesde == null && dFechaHasta != null && sParametro.equals("") && sMoneda.equals("01")){//no hay parametro ni moneda ni fecha desde; fecha hasta primero
					sFechaHasta = sdf.format(dFechaHasta);
					calFechaHasta.setTime(dFechaHasta);
					CalendarToJulian julian = new CalendarToJulian(calFechaHasta);
					int iFechaHasta = julian.getDate();
					sql = sql + " RPDIVJ <= "+ iFechaHasta +" ";
				}
				else if(dFechaHasta != null){
					sFechaHasta = sdf.format(dFechaHasta);
					calFechaHasta.setTime(dFechaHasta);
					CalendarToJulian julian = new CalendarToJulian(calFechaHasta);
					int iFechaHasta = julian.getDate();
					sql = sql + " and RPDIVJ <= "+ iFechaHasta +" ";
				}
				//Agregar Compañia 
				if(!sCodComp.equals("01")){// hay compania seleccionada
					if(dFechaDesde == null && dFechaHasta == null && sParametro.equals("") && sMoneda.equals("01")){//compania primero
						sql = sql + " TRIM(UN.MCRP01) = '"+ sCodComp +"' ";
					}else{
						sql = sql + "and TRIM(UN.MCRP01) = '"+ sCodComp +"' ";
					}
				}
				//Agregar sucursal 
				if(!sCodsuc.equals("01")){// hay sucursal seleccionada
					if(dFechaDesde == null && dFechaHasta == null && sParametro.equals("") && sMoneda.equals("01") && sCodComp.equals("01")){//sucursal primero
						sql = sql + " SUBSTRING(RPKCO,4,5)  = '"+ sCodsuc.trim() +"' ";
					}else{
						sql = sql + "and SUBSTRING(RPKCO,4,5)  = '"+ sCodsuc.trim() +"' ";
					}
				}
				//Agregar unineg 
				if(!sCodunineg.equals("01")){// hay unineg seleccionada
					if(dFechaDesde == null && dFechaHasta == null && sParametro.equals("") && sMoneda.equals("01") && sCodComp.equals("01") && sCodsuc.equals("01")){//unineg primero
						sql = sql + " TRIM(RPMCU) = '"+ sCodunineg.trim() +"' ";
					}else{
						sql = sql + "and TRIM(RPMCU) = '"+ sCodunineg.trim() +"' ";
					}
				}
				break;
			case(3):			
				//Busqueda por codigo de factura
				if (!sParametro.equals("")){//hay codigo de factura
					sql = sql + " cast(RPDOC as VARCHAR(8)) = '"+sParametro+"' ";
				}
				//agregar moneda a consulta
				if (!sParametro.equals("") && !sMoneda.equals("01")){
					sql = sql + " and RPCRCD = '"+ sMoneda +"' ";
				}
				else if(sParametro.equals("") && !sMoneda.equals("01")){
					sql = sql + " RPCRCD = '"+ sMoneda +"' ";
				}
				//agregar fecha desde
				if (dFechaDesde != null && sParametro.equals("") && sMoneda.equals("01")){//no hay parametro ni moneda; fecha desde primero
					sFechaDesde = sdf.format(dFechaDesde);
					calFechaDesde.setTime(dFechaDesde);
					CalendarToJulian julian = new CalendarToJulian(calFechaDesde);
					int iFechaDesde = julian.getDate();
					sql = sql + " RPDIVJ >= "+ iFechaDesde +" ";
				}
				else if (dFechaDesde != null){
					sFechaDesde = sdf.format(dFechaDesde);
					calFechaDesde.setTime(dFechaDesde);
					CalendarToJulian julian = new CalendarToJulian(calFechaDesde);
					int iFechaDesde = julian.getDate();
					sql = sql + " and RPDIVJ >= "+ iFechaDesde +" ";
				}
				//agregar fecha hasta
				if(dFechaDesde == null && dFechaHasta != null && sParametro.equals("") && sMoneda.equals("01")){//no hay parametro ni moneda ni fecha desde; fecha hasta primero
					sFechaHasta = sdf.format(dFechaHasta);
					calFechaHasta.setTime(dFechaHasta);
					CalendarToJulian julian = new CalendarToJulian(calFechaHasta);
					int iFechaHasta = julian.getDate();
					sql = sql + " RPDIVJ <= "+ iFechaHasta +" ";
				}
				else if(dFechaHasta != null){
					sFechaHasta = sdf.format(dFechaHasta);
					calFechaHasta.setTime(dFechaHasta);
					CalendarToJulian julian = new CalendarToJulian(calFechaHasta);
					int iFechaHasta = julian.getDate();
					sql = sql + " and RPDIVJ <= "+ iFechaHasta +" ";
				}
				//Agregar Compañia 
				if(!sCodComp.equals("01")){// hay compania seleccionada
					if(dFechaDesde == null && dFechaHasta == null && sParametro.equals("") && sMoneda.equals("01")){//compania primero
						sql = sql + " TRIM(UN.MCRP01) = '"+ sCodComp +"' ";
					}else{
						sql = sql + "and TRIM(UN.MCRP01) = '"+ sCodComp +"' ";
					}
				}
				//Agregar sucursal 
				if(!sCodsuc.equals("01")){// hay sucursal seleccionada
					if(dFechaDesde == null && dFechaHasta == null && sParametro.equals("") && sMoneda.equals("01") && sCodComp.equals("01")){//sucursal primero
						sql = sql + " SUBSTRING(RPKCO,4,5)  = '"+ sCodsuc.trim() +"' ";
					}else{
						sql = sql + "and SUBSTRING(RPKCO,4,5)  = '"+ sCodsuc.trim() +"' ";
					}
				}
				//Agregar unineg 
				if(!sCodunineg.equals("01")){// hay unineg seleccionada
					if(dFechaDesde == null && dFechaHasta == null && sParametro.equals("") && sMoneda.equals("01") && sCodComp.equals("01") && sCodsuc.equals("01")){//unineg primero
						sql = sql + " TRIM(RPMCU) = '"+ sCodunineg.trim() +"' ";
					}else{
						sql = sql + "and TRIM(RPMCU) = '"+ sCodunineg.trim() +"' ";
					}
				}
				break;
			}
			sql = sql + " and B.RPAAP <> 0 order by RPDDJ";
			ps = cn.prepareStatement(sql/*, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE*/);
			ResultSet rs = ps.executeQuery();
			//rs.last();//move to last row
			//int numFacs = rs.getRow();//rowcount
			//rs.beforeFirst();//move before the first row
			
			//hFac = new Hfacturajde[numFacs];
			//if(numFacs > 0){
				//int i = 0;
			while (rs.next()){
				hFac = new FacturaCredito();
				//sEstado = new String(rs.getBytes("ICIST"), "Cp1047");
				JulianToCalendar fecha = new JulianToCalendar(rs.getInt("FECHA"));
				JulianToCalendar fechavenc = new JulianToCalendar(rs.getInt("FECHAVENC"));
				JulianToCalendar fechagrab = new JulianToCalendar(rs.getInt("FECHAGRAB"));
				JulianToCalendar fechalm = new JulianToCalendar(rs.getInt("FECHALM"));
				JulianToCalendar fechaimp = new JulianToCalendar(rs.getInt("FECHAIMP"));
				JulianToCalendar fechabatch = new JulianToCalendar(rs.getInt("FECHABATCH"));
				
				hFac = new FacturaCredito();
				hFac.setNofactura(rs.getInt("NOFACTURA"));
				hFac.setTipofactura(rs.getString("TIPOFACTURA"));
				hFac.setPartida(rs.getString("PARTIDA"));
				hFac.setFecha(fecha.toString());
				hFac.setFechavenc(fechavenc.toString());
				hFac.setCodcli(rs.getInt("CODCLI"));
				hFac.setNomcli(rs.getString("NOMCLI"));
				hFac.setCodunineg(rs.getString("CODUNINEG"));
				hFac.setUnineg(rs.getString("UNINEG"));
				hFac.setCodsuc(rs.getString("CODSUC"));
				hFac.setNomsuc(rs.getString("NOMSUC"));
				hFac.setCodcomp(rs.getString("CODCOMP"));
				hFac.setNomcomp(rs.getString("NOMCOMP"));
				hFac.setCsubtotal(rs.getBigDecimal("CSUBTOTAL").doubleValue()/100.00);
				hFac.setDsubtotal(rs.getBigDecimal("DSUBTOTAL").doubleValue()/100.00);
				hFac.setCimpuesto(divisas.formatDouble(rs.getBigDecimal("CIMPUESTO").doubleValue()/100.00));
				hFac.setDimpuesto(divisas.formatDouble(rs.getBigDecimal("DIMPUESTO").doubleValue()/100.00));
				hFac.setCtotal(rs.getBigDecimal("CTOTAL").doubleValue()/100.00);
				hFac.setDtotal(rs.getBigDecimal("DTOTAL").doubleValue()/100.00);
				hFac.setMoneda(rs.getString("MONEDA"));
				hFac.setTasa(rs.getBigDecimal("TASA"));
				hFac.setTipopago(rs.getString("TIPOPAGO"));
				hFac.setDesctpago(rs.getString("DESCTPAGO"));
				hFac.setCpendiente(rs.getBigDecimal("CPENDIENTE").doubleValue()/100.00);
				hFac.setDpendiente(rs.getBigDecimal("DPENDIENTE").doubleValue()/100.00);
				hFac.setCodepago(rs.getString("CODEPAGO"));
				hFac.setDescepago(rs.getString("DESCEPAGO"));
				hFac.setFechagrab(fechagrab.toString());
				hFac.setHechopor(rs.getString("HECHOPOR"));
				hFac.setPantalla(rs.getString("PANTALLA"));
				hFac.setFechalm(fechalm.toString());
				hFac.setFechaimp(fechaimp.toString());
				hFac.setCompenslm(rs.getString("COMPENSLM"));
				hFac.setNobatch(rs.getInt("NOBATCH"));
				hFac.setFechabatch(fechabatch.toString());
				hFac.setObservaciones(rs.getString("OBSERVACIONES"));
				hFac.setReferencia(rs.getString("REFERENCIA"));
				hFac.setNumorden(rs.getInt("NUMORDEN"));
				hFac.setTipoorden(rs.getString("TIPOORDEN"));
				hFac.setCdescdisp(divisas.formatDouble(rs.getBigDecimal("CDESCDISP").doubleValue()/100.00));
				hFac.setDdescdisp(divisas.formatDouble(rs.getBigDecimal("DDESCDISP").doubleValue()/100.00));
				hFac.setCdesctom(divisas.formatDouble(rs.getBigDecimal("CDESCTOM").doubleValue()/100.00));
				hFac.setDdesctom(divisas.formatDouble(rs.getBigDecimal("CDESCDISP").doubleValue()/100.00));
				hFac.setCodcont(rs.getString("CODCONT"));
				if (rs.getString("MONEDA").equals("COR")){
					hFac.setTotal(hFac.getCtotal());
					hFac.setEquiv(hFac.getCtotal());
					hFac.setMontoAplicar(0.00);
					hFac.setPendiente(hFac.getCpendiente());
					hFac.setImpuesto(divisas.formatStringToDouble(hFac.getCimpuesto()));
					hFac.setSubtotal(hFac.getCsubtotal());
				}else{
					hFac.setTotal(hFac.getDtotal());
					hFac.setEquiv(hFac.getCtotal());
					hFac.setMontoAplicar(0.00);
					hFac.setPendiente(hFac.getDpendiente());
					hFac.setImpuesto(divisas.formatStringToDouble(hFac.getDimpuesto()));
					hFac.setSubtotal(hFac.getDsubtotal());
				}
				lstFacturas.add(hFac);
			}		
		}catch(Exception ex){
			System.out.println("Se capturo una excepcion en FacturaCreditoCtrl.buscarFacturasxParametros: " + ex);
		}
		return lstFacturas;
	}
/***********************************************************************************************************************/
}

package com.casapellas.controles;
/**
 * CASA PELLAS S.A.
 * Creado por.........: Juan Carlos Ñamendi Pineda
 * Fecha de Creación..: 28/02/2009
 * Última modificación: 09/03/2011
 * Modificado por.....:	Carlos Manuel Hernández Morrison.
 * 
 */
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
//import java.util.Locale;

//import org.apache.commons.mail.EmailAttachment;
//import org.apache.commons.mail.MultiPartEmail;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.casapellas.entidades.Df4211;
import com.casapellas.entidades.F55ca017;
import com.casapellas.entidades.Hf4211;
import com.casapellas.entidades.Hfactjdecon;
import com.casapellas.entidades.Hfactura;
import com.casapellas.entidades.Vf0101;
import com.casapellas.entidades.Vhfactura;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.navegacion.As400Connection;
import com.casapellas.util.CustomEmailAddress;
import com.casapellas.util.Divisas;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.MailHelper;
import com.casapellas.util.PropertiesSystem;

public class FacturaContadoCtrl {
	

/******************************************************************************************/
/** Método: Verificar que las facturas registradas en caja no hayan sido anuladas en jde
 *	Fecha:  31/08/2002
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	@SuppressWarnings("unchecked")
	public List<Hfactura> comprobarFacturaActiva(List<Hfactura> lstFacsActuales){
		List<Hfactura>lstNuevasFacs = null;
		String sql = "";
		String sConsulta = "";
		FechasUtil f = new FechasUtil();
		int iFechaFacJul = 0;
		Session sesion = null;
		Connection cn = null;
		
		try {
			
			sesion = HibernateUtilPruebaCn.currentSession();
			Transaction trans = sesion.beginTransaction();
			
			new As400Connection();
			cn =  As400Connection.getJNDIConnection("DSMCAJA2");
			
			sql = "select distinct cast(sdnxtr as integer), cast(sdlttr as integer) from "+PropertiesSystem.JDEDTA+".f4211 where ";
			
			Object[] iEstado = null;
			List<Object[]> lstResult = null;
			Hfactura hf = null;
			boolean bAnular = false;
			
			for (int i = 0; i < lstFacsActuales.size(); i++) {
				bAnular = false;
				hf = lstFacsActuales.get(i);
				iFechaFacJul = f.DateToJulian(new SimpleDateFormat("dd/MM/yyyy").parse(hf.getFecha()));
				
				sConsulta = sql ;
				sConsulta += " sddoc =  " + String.valueOf(hf.getNofactura());
				sConsulta += " and trim(sdmcu) = '"+hf.getCodunineg().trim()+"'";
				sConsulta += " and sdivd = " + String.valueOf(iFechaFacJul);
				
				lstResult = sesion.createSQLQuery(sConsulta).list();
				
				if(lstResult == null || lstResult.size() == 0 )
					bAnular = true;
				
				if(lstResult != null && lstResult.size() > 0){
					
					if(lstResult.size() > 1)
						continue;
					
					iEstado = (Object[])lstResult.get(0);
					if( Integer.parseInt(String.valueOf(iEstado[0])) == 999 && 
						Integer.parseInt(String.valueOf(iEstado[1])) == 980 )
						bAnular = true;
				}
				
				if(bAnular){
					hf.setCodsuc("000"+hf.getCodsuc());
					anularFacturaFDC(cn, hf.getNofactura(), 
									hf.getTipofactura(), 
									hf.getCodsuc(), 
									hf.getCodunineg(), 
									hf.getCodcli());
					lstFacsActuales.remove(i);
					
					//&& ==== Correito.
					correo(hf.getNofactura()+" "+hf.getTipofactura()
							+" "+hf.getCodunineg()+" "+hf.getCodcli());
					
				}
			}
			
			trans.commit();
			cn.commit();
			
		} catch (Exception e) {
			System.out.println(" com.casapellas.controles " + new Date());
			System.out.println(": Excepción capturada en " +
					":comprobarFacturaActiva Mensaje:\n " + e);
			lstNuevasFacs  = new ArrayList<Hfactura>(lstFacsActuales);
		}finally{
			try {
				cn.close();
				sesion.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return lstNuevasFacs;
	}
	
	public void correo(String sData){
		try {
			String strListEmail = PropertiesSystem.MAIL_INTERNAL_ADDRESS;
			String[] listEmail = strListEmail.split(",");
			
			List<CustomEmailAddress> toList = new ArrayList<CustomEmailAddress>();
			for (String strEmail : listEmail) {
				toList.add(new CustomEmailAddress(strEmail));
			}
			
			if (toList.size() > 0) {
				MailHelper.SendHtmlEmail(
						new CustomEmailAddress(PropertiesSystem.WEBMASTER_EMAIL_ADRESS, "Módulo de caja"),
						toList,
						"Anulacion de factura " + sData, "");
			}
		} catch (Exception e) {
			System.out.println(" com.casapellas.controles " + new Date());
			System.out
					.println(": Excepción capturada en :correo Mensaje:\n "
							+ e);
		}
	}
	
	
/************************************************************************************/	
	public Vf0101 existeCliente(int iCodcli){
		Session session = HibernateUtilPruebaCn.currentSession();
		
		Vf0101 c = null;
		String sql = "";
		try{
			sql = "from Vf0101 as c where c.id.aban8 = " + iCodcli;
		
			c = (Vf0101)session.createQuery(sql).uniqueResult();
		
		}catch(Exception ex){
			System.out.println("Se capturo una excepcion en FacturasContadoCtrl.validarCliente: " + ex);
		}
		return c;
	}
/************************************************************************************/
	public Hf4211 comprobarSiExisteF4211(int iNofactura,String sTipoFactura, String sCodsuc,String sCodunineg){
		Hf4211 f = null;
		Session session = HibernateUtilPruebaCn.currentSession();
		
		String sql = "";
		try{
			sql = "from Hf4211 as f where f.id.nofactura = " + iNofactura + " and f.id.tipofactura = '" + 
						sTipoFactura + "' and f.id.codsuc = '" +sCodsuc+ "' and trim(f.id.codunineg) = '" +sCodunineg.trim()+"'";
			
			f = (Hf4211)session.createQuery(sql).uniqueResult();
			
		}catch(Exception ex){
			System.out.println("Se capturo una excepcion en FacturasContadoCtrl.comprobarSiExiste: " + ex);
		}
		return f;
	}
/************************************************************************************/	
	public boolean borrarDetalleAnulado(Connection cn, List lstDet){
		boolean bHecho = true;
		PreparedStatement ps = null;
		String sql = "";
		Df4211 d = null;
		try{
			
			for(int i = 0; i < lstDet.size();i++){
				d = (Df4211)lstDet.get(i);
				
				sql = "delete from "+PropertiesSystem.ESQUEMA+".a03factco where nofactura = "
						+ d.getId().getNofactura() + " and tipofactura = '"
						+ d.getId().getTipofactura() + "' and codsuc = '"
						+ d.getId().getCodsuc() + "'"
						+ " and trim(codunineg) = '"
						+ d.getId().getCodunineg().trim()
						+ "' and trim(coditem) = '" + d.getId().getCoditem()
						+ "' and trim (descitem) = '" + d.getId().getDescitem()
						+ "'";
				
				ps = cn.prepareStatement(sql);
				int rs = ps.executeUpdate();
				
				if (rs == 0){
					bHecho = false;
					break;
				}
			}
		}catch(Exception ex){
			bHecho = false;
			System.out.println("Se capturo una excepcion en FacturaContadoCtrl.borrarDetalleAnulado: " + ex);
		}finally {
			try {
				ps.close();
			} catch (Exception se2) {
				System.out.println("ERROR: Failed to close connection en ReciboContadoCtrl.actualizarNumeroRecibo: " + se2);
			}
		}
		return bHecho;
	}
/************************************************************************************/	
	public boolean actualizarFacturaFDC(Connection cn, int iNoFac,
					String sTipoFac, String sCodsuc, String sCodunineg, int iCodcli,
					int iSubtotal, int iTotal, int subtotalf, int totalf,
					BigDecimal bdCosto){
		
		boolean bHecho = true;
		PreparedStatement ps = null;
		String sql = "";
		
		try{
			
			sql = "update "+PropertiesSystem.ESQUEMA+".a02factco set subtotal = " + iSubtotal
					+ ", total =" + iTotal + ", subtotalf = " + subtotalf
					+ ", totalf =" + totalf 
					+ ", totalcosto = "+bdCosto.toString()
					+ " where nofactura = " + iNoFac
					+ " and tipofactura = '" + sTipoFac + "' and codsuc = '"
					+ sCodsuc + "'" + " and trim(codunineg) = '"
					+ sCodunineg.trim() + "' and codcli =" + iCodcli;
			
			ps = cn.prepareStatement(sql);
			int rs = ps.executeUpdate();
			if (rs == 0){
				bHecho = false;
			}
		}catch(Exception ex){
			bHecho = false;
			System.out.println("Se capturo una excepcion en FacturaContadoCtrl.actualizarFacturaFDC: " + ex);
		}finally {
			try {
				ps.close();
			} catch (Exception se2) {
				System.out.println("ERROR: Failed to close connection en ReciboContadoCtrl.actualizarNumeroRecibo: " + se2);
			}
		}
		return bHecho;
	}
/************************************************************************************/	
	public boolean anularFacturaFDC(Connection cn,int iNoFac,String sTipoFac,String sCodsuc,String sCodunineg,int iCodcli){
		boolean bHecho = true;
		PreparedStatement ps = null;
		String sql = "";
		try{
		
			sql = "update "+PropertiesSystem.ESQUEMA+".a02factco set estado = 'A' where nofactura = "
					+ iNoFac
					+ " and tipofactura = '"
					+ sTipoFac
					+ "' and codsuc = '"
					+ sCodsuc
					+ "'"
					+ " and trim(codunineg) = '"
					+ sCodunineg.trim()
					+ "' and codcli =" + iCodcli;
			
			ps = cn.prepareStatement(sql);
			int rs = ps.executeUpdate();
			if (rs == 0){
				bHecho = false;
			}
		}catch(Exception ex){
			bHecho = false;
			System.out.println("Se capturo una excepcion en anularFacturaFDC: " + ex);
		}finally {
			try {
				ps.close();
			} catch (Exception se2) {
				bHecho = false;
				System.out.println("ERROR: Failed to close connection en ReciboContadoCtrl.actualizarNumeroRecibo: " + se2);
			}
		}
		return bHecho;
	}
/************************************************************************************/
	public List buscarDetalleFactura(Hf4211 f){
		List lstResult = new ArrayList();
		Session session = HibernateUtilPruebaCn.currentSession();
		
		String sql = "";
		try{
			sql = "from Df4211 as f where trim(f.id.codunineg) = '"
					+ f.getId().getCodunineg().trim()
					+ "' and f.id.nofactura = " + f.getId().getNofactura()
					+ " and f.id.tipofactura='" + f.getId().getTipofactura()
					+ "' and f.id.codsuc = '" + f.getId().getCodsuc() + "'";
			
		
			lstResult = session.createQuery(sql).list();
		
		}catch(Exception ex){
			System.out.println("Se capturo una excepcion en buscarDetalleFactura: " + ex);
		}
		return lstResult;
	}
/************************************************************************************/
	public boolean insertarDetalleFacturaFDC(Connection cn,List lstDet){
		boolean bHecho = true;
		String sql = "";
		PreparedStatement ps = null;
		Df4211 f = null;
		try{
			for (int i = 0; i < lstDet.size(); i ++){
				f = (Df4211)lstDet.get(i);
				sql = "INSERT INTO "+PropertiesSystem.ESQUEMA+".A03FACTCO VALUES("
						+ f.getId().getNofactura() + ",'"
						+ f.getId().getTipofactura() + "','"
						+ f.getId().getCoditem() + "','"
						+ f.getId().getDescitem() + "',"
						+ f.getId().getPreciounit() + "," 
						+ f.getId().getCant() + ",'" 
						+ f.getId().getImpuesto() + "',"
						+ f.getId().getFactor() + ",'"
						+ f.getId().getCodunineg() + "','"
						+ f.getId().getCodsuc() +"', "
						+ f.getId().getTotalcosto().abs().toString()+", "
						+"0" + ")"; //&& ==== Descuento
				
				ps = cn.prepareStatement(sql);
				int rs = ps.executeUpdate();
				if (rs == 0){
					bHecho = false;
					break;
				}
			}
		}catch(Exception ex){
			bHecho = false;
			System.out.println("Se capturo una excepcion FacturaContadoCtrl.FacturaContadoCtrl: " + ex);
		}finally {
			try {
				ps.close();
				//cn.close();
			} catch (Exception se2) {
				System.out.println("ERROR: Failed to close connection en ReciboCtrl.grabarRC: " + se2);
			}
		}
		return bHecho;
	}
/************************************************************************************/
	public boolean insertarFacturaFDC(Connection cn,Hf4211 f){
		boolean bHecho = true;
		String sql = "INSERT INTO "+PropertiesSystem.ESQUEMA+".A02FACTCO VALUES(";
		PreparedStatement ps = null;
		List lstDet = new ArrayList();
		
		String sNomcli="";
		Divisas dv = new Divisas();
		
		
		try{
			sNomcli = dv.remplazaCaractEspeciales(f.getId().getNomcli(), "'", "''");
			sNomcli = sNomcli.equals("")? f.getId().getNomcli(): sNomcli;
			
			sql = sql + f.getId().getNofactura() + ",'"
					+ f.getId().getTipofactura() + "'," 
					+ f.getId().getCodcli()
					+ ",'" + sNomcli + "','" 
					+ f.getId().getCodunineg() + "','"
					+ f.getId().getUnineg() + "','" 
					+ f.getId().getCodsuc()
					+ "','" + f.getId().getNomsuc() + "','"
					+ f.getId().getCodcomp() + "','" 
					+ f.getId().getNomcomp()
					+ "'," + f.getId().getFecha() + ","
					+ f.getId().getSubtotal().abs() + ",'" 
					+ f.getId().getMoneda()
					+ "'," + f.getId().getTasa().abs() + ",'"
					+ f.getId().getTipopago() + "'," 
					+ f.getId().getFechagrab()
					+ ",'" + f.getId().getHechopor() + "','"
					+ f.getId().getPantalla() + "'," 
					+ f.getId().getHora()
					+ ",'" + f.getId().getEstado() + "',"
					+ f.getId().getNodoco() + ",'" 
					+ f.getId().getTipodoco()
					+ "'," + f.getId().getTotal().abs() + ",'"
					+ f.getId().getSdlocn() + "','" 
					+ f.getId().getProgramaid()
					+ "'," + f.getId().getCodvendor() + ","
					+ f.getId().getSubtotalf().abs() + "," 
					+ f.getId().getTotalf().abs() + ", "
					+ f.getId().getTotalcosto().abs().toString()+","
					+ "0" + ")"; //&& ==== Campos de descuento.
			
			ps = cn.prepareStatement(sql);
			int rs = ps.executeUpdate();
			cn.commit();
			if (rs != 1){
				bHecho = false;
			}else{//insertar el detalle de factura
				lstDet = buscarDetalleFactura(f);
				bHecho = insertarDetalleFacturaFDC(cn,lstDet);
			}
			cn.commit();
		}catch(Exception ex){
			bHecho = false; 
			System.out.println("Se capturo una excepcion FacturaContadoCtrl.FacturaContadoCtrl: " + ex);
		}finally {
			try {
				ps.close();
				//cn.close();
			} catch (Exception se2) {
				System.out.println("ERROR: Failed to close connection en ReciboCtrl.grabarRC: " + se2);
			}
		}
		return bHecho;
	}
/************************************************************************************/
	public Vhfactura comprobarSiExiste(int iNofactura,String sTipoFactura, String sCodsuc,String sCodunineg){
		Vhfactura f = null;
		Session session = HibernateUtilPruebaCn.currentSession();
		

		try{
			sCodsuc = (sCodsuc.length()==5)?sCodsuc.substring(3, 5):sCodsuc.length()==2?
							sCodsuc:sCodsuc.substring(sCodsuc.length()-2, sCodsuc.length());
		
			Criteria cr = session.createCriteria(Vhfactura.class);
			cr.add(Restrictions.eq("id.nofactura",iNofactura));
			cr.add(Restrictions.eq("id.tipofactura",sTipoFactura));
			cr.add(Restrictions.eq("id.codsuc",sCodsuc));
			cr.add(Restrictions.sqlRestriction("trim(codunineg) = '"+sCodunineg.trim()+ "'"));
			f = (Vhfactura)cr.uniqueResult();
		
			
			
		}catch(Exception ex){
			f = null;
			System.out.println("Se capturo una excepcion en FacturasContadoCtrl.comprobarSiExiste: " + ex);
		}finally{
			try{session.close();}catch(Exception ex2){ex2.printStackTrace();};
		}
		return f;
	}
/************************************************************************************/
	@SuppressWarnings("unchecked")
	public List<Hf4211> getFacturasFDC(String sUnineg,String sLoc,int iFecha){
		List<Hf4211> lstResult = new ArrayList<Hf4211>();
		Session sesion = HibernateUtilPruebaCn.currentSession();
//		Transaction trans = null;
		String sql = "";
		boolean bMicro = false;
		String sNotIn = "";
		
		try{
			String sLinea = sUnineg.trim().substring(2,4);
			if(sLinea.compareTo("22") == 0 || sLinea.compareTo("23") == 0 || sLinea.compareTo("24") == 0 )
				bMicro = true;	
			
			sql =  " from Hf4211 as f";
			sql += " where trim(f.id.codunineg) = '"+ sUnineg.trim() +"'"; 
			sql += " and f.id.fecha=" + iFecha;
			
			if(!bMicro)
				sql += " and trim(f.id.sdlocn) = '" +sLoc.trim()+"'";	
			
//			trans = sesion.beginTransaction();
			lstResult = sesion.createQuery(sql).list();
			
			if(lstResult != null && lstResult.size() > 0 && bMicro){

				Criteria cr = null;
				Hf4211 hf = null;
				List<Hf4211>lstDetMicro = null;
				for (int i = 0; i < lstResult.size(); i++) {
					hf = lstResult.get(i);
					
					if(sNotIn.contains(String.valueOf(hf.getId().getNofactura()))){
						lstResult.remove(i);
						continue;
					}
					
					sNotIn += String.valueOf(hf.getId().getNofactura()+",");
					
					cr = sesion.createCriteria(Hf4211.class);
					cr.add(Restrictions.eq("id.codunineg", hf.getId().getCodunineg()));
					cr.add(Restrictions.eq("id.nofactura", hf.getId().getNofactura()));
					cr.add(Restrictions.eq("id.fecha", hf.getId().getFecha()));
					
					lstDetMicro = cr.list();
					int iSbt = 0;
					int iTtl = 0;
					int iSbtf = 0;
					int iTtlf = 0;
					BigDecimal bdCosto = new BigDecimal("0");
					if(lstDetMicro != null && lstDetMicro.size() > 1 ){
						for (Hf4211 hfDt : lstDetMicro) {
							iSbt += hfDt.getId().getSubtotal().intValue();
							iTtl += hfDt.getId().getTotal().intValue();
							iSbtf += hfDt.getId().getSubtotalf().intValue();
							iTtlf += hfDt.getId().getTotalf().intValue();
							bdCosto = bdCosto.add(hfDt.getId().getTotalcosto());
						}
						hf.getId().setSubtotal(new BigDecimal(iSbt));
						hf.getId().setTotal(new BigDecimal(iTtl));
						hf.getId().setSubtotalf(new BigDecimal(iSbtf));
						hf.getId().setTotalf(new BigDecimal(iTtlf));
						hf.getId().setTotalcosto(bdCosto);						
						lstResult.set(i, hf);
					}
				}
			}
//			trans.commit();
			
		}catch(Exception ex){
			System.out.println("Se capturo una excepcion en FaturaContadoCtrl: " + ex);
		}finally{
			sesion.close();
		}
		return lstResult;
	}
/************************************************************************************/	
/**BUSCAR FACTURAS DE CONTADO POR PARAMETRO, FECHA EN FORMATO JULIANO  Y TIPO DE BUSQUEDA(1 = por nombre de cliente, 2 = por codigo de cliente, 3 = por numero de factura)**/
	public List buscarFactura(String sParametro, int iFecha,String[] sTipoFactura,String sMoneda, String sRangoFacs, int iTipo, F55ca017[] f55ca017, List lstLocalizaciones, int iCaid){
		List lstFacturas = null, f55ca017_1 = new ArrayList(), f55ca017_2 = new ArrayList();
		String sql = "", sql2 = "",sFiltro="";
		Session session = HibernateUtilPruebaCn.currentSession();		
		String sLoc = "";
		boolean bHayLoc = false, bSoloLocs = false;
		int iFechaInicio = 110025;
		try{
			//separar los que tienen localizaciones 
			for(int z = 0; z < f55ca017.length;z++){
				if(f55ca017[z].getId().getC7locn().trim().equals("")){
					f55ca017_1.add(f55ca017[z]);
					bSoloLocs = true;
				}else{
					bHayLoc = true;
					f55ca017_2.add(f55ca017[z]);
				}
			}			
			
			sql = "select * from "+PropertiesSystem.ESQUEMA+".Hfactjdecon as f where ";
			
			switch (iTipo){
			case(1):
				//Busqueda por nombre de cliente
				if (!sParametro.equals("")){//hay nombre de cliente
					sql = sql + " trim(f.nomcli) like '%"+sParametro.trim().toUpperCase()+"%' ";
					sFiltro += " and trim(f.nomcli) like '%"+sParametro.trim().toUpperCase()+"%' ";
				}
				//agregar moneda a consulta
				if (!sParametro.equals("") && !sMoneda.equals("01")){
					sql = sql + " and f.moneda = '"+ sMoneda +"' ";
					sFiltro += " and f.moneda = '"+ sMoneda +"' ";
				}
				else if(sParametro.equals("") && !sMoneda.equals("01")){
					sql = sql + " f.moneda = '"+ sMoneda +"' ";
					sFiltro += " and f.moneda = '"+ sMoneda +"' ";
				}
				//agregar rango de fechas a consulta
				if(!sParametro.equals("") && sRangoFacs.equals("01")){//facturas del dia
					sql = sql + " and f.fecha = " + iFecha;
					sFiltro += " and f.fecha = " + iFecha;
				}
				else if(!sParametro.equals("") && sRangoFacs.equals("02")){//facturas de dias anteriores
					sql = sql + " and (f.fecha > 110025 and f.fecha < " + iFecha +")";
					sFiltro += " and (f.fecha > 110025 and f.fecha < " + iFecha +")";
				}
				else if(!sParametro.equals("") && sRangoFacs.equals("03")){//todas
					sql = sql + " and (f.fecha > 110025 and f.fecha <= " + iFecha +")";
					sFiltro += " and (f.fecha > 110025 and f.fecha <= " + iFecha +")";
				}
				else if(!sParametro.equals("") && sRangoFacs.equals("04")){//facturas del dia anuladas
					sql = sql + " and f.fecha = " + iFecha;
					sFiltro += " and f.fecha = " + iFecha;
				}
				else if (sParametro.equals("") && sMoneda.equals("01")){//fechas van primero
					if(sRangoFacs.equals("01")){//facturas del dia
						sql = sql + " f.fecha = " + iFecha;
						sFiltro += " and f.fecha = " + iFecha;
					}
					else if(sRangoFacs.equals("02")){//facturas de dias anteriores
						sql = sql + " (f.fecha > "+iFechaInicio+" and f.fecha < " + iFecha + ")";
						sFiltro += " and (f.fecha > "+iFechaInicio+" and f.fecha < " + iFecha + ")";
					}
					else if(sRangoFacs.equals("04")){//facturas del dia anuladas
						sql = sql + " f.fecha = " + iFecha;
						sFiltro += " and  f.fecha = " + iFecha;
					}
					else {//todas
						sql = sql + " (f.fecha > "+iFechaInicio+" and f.fecha <= " + iFecha + ")";
						sFiltro += " and (f.fecha > "+iFechaInicio+" and f.fecha <= " + iFecha + ")";
					}
				}else {
					if(sRangoFacs.equals("01")){//facturas del dia
						sql = sql + " and f.fecha = " + iFecha;
						sFiltro +=  " and f.fecha = " + iFecha;
					}
					else if(sRangoFacs.equals("02")){//facturas de dias anteriores
						sql = sql + " and (f.fecha > "+iFechaInicio+" and f.fecha < " + iFecha+")";
						sFiltro += " and (f.fecha > "+iFechaInicio+" and f.fecha < " + iFecha+")";
					}
					if(sRangoFacs.equals("04")){//facturas del dia anuladas
						sql = sql + " and f.fecha = " + iFecha;
						sFiltro +=  " and f.fecha = " + iFecha;
					}
					else {//todas
						sql = sql + " and (f.fecha > "+iFechaInicio+" and f.fecha <= " + iFecha+")";
						sFiltro += " and (f.fecha > "+iFechaInicio+" and f.fecha <= " + iFecha+")";
					}
				}
				break;
			case(2):
				//Busqueda por codigo de cliente
				if (!sParametro.equals("")){//hay codigo de cliente
					sql = sql + " trim(cast(f.codcli as varchar(8))) like '"+sParametro.trim().toUpperCase()+"%' ";
					sFiltro += " and trim(cast(f.codcli as varchar(8))) like '"+sParametro.trim().toUpperCase()+"%' ";
				}
				//agregar moneda a consulta
				if (!sParametro.equals("") && !sMoneda.equals("01")){
					sql = sql + " and f.moneda = '"+ sMoneda +"' ";
					sFiltro += " and f.moneda = '"+ sMoneda +"' ";
				}
				else if(sParametro.equals("") && !sMoneda.equals("01")){
					sql = sql + " f.moneda = '"+ sMoneda +"' ";
					sFiltro += " and f.moneda = '"+ sMoneda +"' ";
				}
//				agregar rango de fechas a consulta
				if(!sParametro.equals("") && sRangoFacs.equals("01")){//facturas del dia
					sql = sql + " and f.fecha = " + iFecha;
					sFiltro += " and f.fecha = " + iFecha;
				}
				else if(!sParametro.equals("") && sRangoFacs.equals("02")){//facturas de dias anteriores
					sql = sql + " and (f.fecha > "+iFechaInicio+" and f.fecha < " + iFecha +")";
					sFiltro += " and (f.fecha > "+iFechaInicio+" and f.fecha < " + iFecha +")";
				}
				else if(!sParametro.equals("") && sRangoFacs.equals("04")){//facturas del dia anuladas
					sql = sql + " and f.fecha = " + iFecha;
					sFiltro += " and f.fecha = " + iFecha;
				}
				else if(!sParametro.equals("")){//todas
					sql = sql + " and (f.fecha > "+iFechaInicio+" and f.fecha <= " + iFecha +")";
					sFiltro += " and (f.fecha > "+iFechaInicio+" and f.fecha <= " + iFecha +")";
				}
				else if (sParametro.equals("") && sMoneda.equals("01")){//fechas van primero
					if(sRangoFacs.equals("01")){//facturas del dia
						sql = sql + " f.fecha = " + iFecha;
						sFiltro += " and f.fecha = " + iFecha;
					}
					else if(sRangoFacs.equals("02")){//facturas de dias anteriores
						sql = sql + " (f.fecha > "+iFechaInicio+" and f.fecha < " + iFecha + ")";
						sFiltro += " and (f.fecha > "+iFechaInicio+" and f.fecha < " + iFecha + ")";
					}
					else if(sRangoFacs.equals("04")){//facturas del dia anuladas
						sql = sql + " f.fecha = " + iFecha;
						sFiltro +=  " and f.fecha = " + iFecha;
					}
					else {//todas
						sql = sql + " (f.fecha > "+iFechaInicio+" and f.fecha <= " + iFecha + ")";
						sFiltro += " and (f.fecha > "+iFechaInicio+" and f.fecha <= " + iFecha + ")";
					}
					
				}else {
					if(sRangoFacs.equals("01")){//facturas del dia
						sql = sql + " and f.fecha = " + iFecha;
						sFiltro += " and f.fecha = " + iFecha;
					}
					else if(sRangoFacs.equals("02")){//facturas de dias anteriores
						sql = sql + " and (f.fecha > "+iFechaInicio+" and f.fecha < " + iFecha+")";
						sFiltro += " and (f.fecha > "+iFechaInicio+" and f.fecha < " + iFecha+")";
					}
					else if(sRangoFacs.equals("04")){//facturas del dia anuladas
						sql = sql + " and f.fecha = " + iFecha;
						sFiltro += " and f.fecha = " + iFecha;
					}
					else {//todas
						sql = sql + " and (f.fecha > "+iFechaInicio+" and f.fecha <= " + iFecha+")";
						sFiltro += " and (f.fecha > "+iFechaInicio+" and f.fecha <= " + iFecha+")";
					}
				}	
				break;
			case(3):
				//Busqueda por numero de factura
				if (!sParametro.equals("")){//hay numero de factura
					sql = sql + " trim(cast(f.nofactura as varchar(8))) like '"+sParametro.trim().toUpperCase()+"%' ";
					sFiltro += " and trim(cast(f.nofactura as varchar(8))) like '"+sParametro.trim().toUpperCase()+"%' ";
				}
				//agregar moneda a consulta
				if (!sParametro.equals("") && !sMoneda.equals("01")){
					sql = sql + " and f.moneda = '"+ sMoneda +"' ";
					sFiltro += " and f.moneda = '"+ sMoneda +"' ";
				}
				else if(sParametro.equals("") && !sMoneda.equals("01")){
					sql = sql + " f.moneda = '"+ sMoneda +"' ";
					sFiltro += " f.moneda = '"+ sMoneda +"' ";
				}
//				agregar rango de fechas a consulta
				if(!sParametro.equals("") && sRangoFacs.equals("01")){//facturas del dia
					sql = sql + " and f.fecha = " + iFecha;
					sFiltro += " and f.fecha = " + iFecha;
				}
				else if(!sParametro.equals("") && sRangoFacs.equals("02")){//facturas de dias anteriores
					sql = sql + " and (f.fecha > "+iFechaInicio+" and f.fecha < " + iFecha +")";
					sFiltro +=  " and (f.fecha > "+iFechaInicio+" and f.fecha < " + iFecha +")";
				}
				else if(!sParametro.equals("") && sRangoFacs.equals("04")){//facturas del dia anuladas
					sql = sql + " and f.fecha = " + iFecha;
					sFiltro += " and f.fecha = " + iFecha;
				}
				else if(!sParametro.equals("")){//todas
					sql = sql + " and (f.fecha > "+iFechaInicio+" and f.fecha <= " + iFecha +")";
					sFiltro += " and (f.fecha > "+iFechaInicio+" and f.fecha <= " + iFecha +")";
				}
				
				else if (sParametro.equals("") && sMoneda.equals("01")){//fechas van primero
					if(sRangoFacs.equals("01")){//facturas del dia
						sql = sql + " f.fecha = " + iFecha;
						sFiltro +=  " and f.fecha = " + iFecha;
					}
					else if(sRangoFacs.equals("02")){//facturas de dias anteriores
						sql = sql + " (f.fecha > "+iFechaInicio+" and f.fecha < " + iFecha + ")";
						sFiltro += " and (f.fecha > "+iFechaInicio+" and f.fecha < " + iFecha + ")";
					}
					if(sRangoFacs.equals("04")){//facturas del dia anuladas
						sql = sql + " f.fecha = " + iFecha;
						sFiltro +=  " and f.fecha = " + iFecha;
					}
					else {//todas
						sql = sql + " and (f.fecha > "+iFechaInicio+" and f.fecha <= " + iFecha + ")";
						sFiltro += " and (f.fecha > "+iFechaInicio+" and f.fecha <= " + iFecha + ")";
					}
				}else {
					if(sRangoFacs.equals("01")){//facturas del dia
						sql = sql + " and f.fecha = " + iFecha;
						sFiltro += " and f.fecha = " + iFecha;
					}
					else if(sRangoFacs.equals("02")){//facturas de dias anteriores
						sql = sql + " and (f.fecha > "+iFechaInicio+" and f.fecha < " + iFecha+")";
						sFiltro += " and (f.fecha > "+iFechaInicio+" and f.fecha < " + iFecha+")";
					}
					if(sRangoFacs.equals("04")){//facturas del dia anuladas
						sql = sql + " and f.fecha = " + iFecha;
						sFiltro += " and f.fecha = " + iFecha;
					}
					else {//todas
						sql = sql + " and (f.fecha > "+iFechaInicio+" and f.fecha <= " + iFecha+")";
						sFiltro += " and (f.fecha > "+iFechaInicio+" and f.fecha <= " + iFecha+")";
					}
				}
				break;
			}
			//--- Tipos de 
			sql = sql + " and f.subtotal >= 0 and f.tipofactura in (";
			for (int i = 0; i < sTipoFactura.length; i++){
				if (i == sTipoFactura.length - 1){
					sql = sql + "'" + sTipoFactura[i] + "'";
				}else{
					sql = sql + "'" + sTipoFactura[i] + "',";
				}
			}	
			sql = sql + ")";
			sql2 = sql;	
			//agregar unidades de negocio
			sql = sql + " and trim(f.sdlocn) = '' and trim(f.codunineg) in (";
			for(int a = 0; a < f55ca017_1.size(); a++){
				if (a == f55ca017_1.size() - 1){
					sql = sql + "'" + ((F55ca017)f55ca017_1.get(a)) .getId().getC7mcul().trim() + "'";
				}else{
					sql = sql + "'" + ((F55ca017)f55ca017_1.get(a)) .getId().getC7mcul().trim() + "',";
				}
			}
			//agregar localizaciones si hay
			if(bHayLoc){
				sql2 = sql2 + " and (trim(f.sdlocn) in (";
				for(int j = 0; j < f55ca017_2.size(); j++){
					sLoc = ((F55ca017)f55ca017_2.get(j)).getId().getC7locn();
					if (j == f55ca017_2.size() - 1){
						sql2 = sql2 + "'" + sLoc.trim()+ "'";
					}else{
						sql2 = sql2 + "'" + sLoc.trim() + "',";
					}
				}
				sql2 = sql2 + ") and trim(f.codunineg) in (";
				for(int a = 0; a < f55ca017_2.size(); a++){
					if (a == f55ca017_2.size() - 1){
						sql2 = sql2 + "'" + ((F55ca017)f55ca017_2.get(a)).getId().getC7mcul().trim() + "'";
					}else{
						sql2 = sql2 + "'" + ((F55ca017)f55ca017_2.get(a)).getId().getC7mcul().trim() + "',";
					}
				}
				//sql2 = sql2 + ")) and f.nofactura not in(select rf.numfac from "+PropertiesSystem.ESQUEMA+".Recibofac as rf where rf.numfac = f.nofactura and rf.codcomp = f.codcomp and rf.tipofactura = f.tipofactura and rf.estado <> 'A')";				
			}
			
			
			//
			if (sRangoFacs.equals("04")){
				sql = sql + ") and trim(f.estado) = 'A' and f.nofactura not in(select rf.numfac from "+PropertiesSystem.ESQUEMA+".Recibofac as rf where rf.numfac = f.nofactura and rf.codcomp = f.codcomp and rf.tipofactura = f.tipofactura and rf.estado <> 'A' and trim(rf.codunineg) = trim(f.codunineg)) ";
				sql2 = sql2 + ")) and trim(f.estado) = 'A' and f.nofactura not in(select rf.numfac from "+PropertiesSystem.ESQUEMA+".Recibofac as rf where rf.numfac = f.nofactura and rf.codcomp = f.codcomp and rf.tipofactura = f.tipofactura and rf.estado <> 'A' and trim(rf.codunineg) = trim(f.codunineg)) ";
				sFiltro += " and trim(f.estado)='A'";
			}else {
				sql = sql + ") and trim(f.estado) = '' and f.nofactura not in(select rf.numfac from "+PropertiesSystem.ESQUEMA+".Recibofac as rf where rf.numfac = f.nofactura and rf.codcomp = f.codcomp and rf.tipofactura = f.tipofactura and rf.estado <> 'A' and trim(rf.codunineg) = trim(f.codunineg)) ";
				sql2 = sql2 + ")) and trim(f.estado) = '' and f.nofactura not in(select rf.numfac from "+PropertiesSystem.ESQUEMA+".Recibofac as rf where rf.numfac = f.nofactura and rf.codcomp = f.codcomp and rf.tipofactura = f.tipofactura and rf.estado <> 'A' and trim(rf.codunineg) = trim(f.codunineg)) ";
				sFiltro += " and trim(f.estado)=''";
			}
			/**********************************************************************
			 * Descr: Considerar los traslados de facturas, desde y hacia otras cajas.
			 * Fecha: 31/05/2010
			 * Hecho: Carlos M. Hernández Morrison. 
			 */
			//--- Excluir las facturas enviadas hacia otra caja.
			String sExcl ="";
			sExcl += " and f.nofactura not in( select tf.nofactura from "+PropertiesSystem.ESQUEMA+".trasladofac tf";
			sExcl += " where tf.nofactura = f.nofactura and tf.codcomp = f.codcomp and tf.tipofactura = f.tipofactura";
			sExcl += " and tf.codsuc = f.codsuc and trim(tf.codunineg)  = trim(f.codunineg)";
			sExcl += " and tf.estadotr in ('E','R') and tf.caidorig = "+iCaid + " )";
			sql += sExcl;
			sql2+= sExcl;
			
			//--- Incluir las facturas recibidas desde otra caja.
			sql += " UNION ";
			sql += " select f.* from "+PropertiesSystem.ESQUEMA+".Hfactjdecon as f inner join "+PropertiesSystem.ESQUEMA+".trasladofac tf"; 
			sql += " on  tf.nofactura = f.nofactura and tf.codcomp = f.codcomp and tf.tipofactura = f.tipofactura";
			sql += " and tf.codsuc = f.codsuc and trim(tf.codunineg)  = trim(f.codunineg) and tf.estadotr in ('E','R')";
			sql += " and tf.caiddest = "+iCaid;
			sql += sFiltro;
			
			if(bHayLoc){
				sql = sql + " union " + sql2;
			}
			if(bSoloLocs){
				lstFacturas = session.createSQLQuery(sql).addEntity(Hfactjdecon.class).list();	
			}else{
				sql2 += " UNION ";
				sql2 += " select f.* from "+PropertiesSystem.ESQUEMA+".Hfactjdecon as f inner join "+PropertiesSystem.ESQUEMA+".trasladofac tf"; 
				sql2 += " on  tf.nofactura = f.nofactura and tf.codcomp = f.codcomp and tf.tipofactura = f.tipofactura";
				sql2 += " and tf.codsuc = f.codsuc and trim(tf.codunineg)  = trim(f.codunineg) and tf.estadotr = 'E'";
				sql2 += " and tf.caiddest = "+iCaid;
				sql2 += sFiltro;
				
				lstFacturas = session.createSQLQuery(sql2).addEntity(Hfactjdecon.class).list();	
			}

		}catch(Exception ex){
			System.out.println("Se capturo una excepcion en FacturaContadoCtrl.buscarFactura: " + ex);
		}finally{
			try{session.close();}catch(Exception ex2){ex2.printStackTrace();};
		}
		return lstFacturas;
	}
/*******************************************************************************************************************************/
/**BUSCAR DETALLE DE FACTURA**********************************************/
	public List buscarDetalleFactura(int iNumFac, String sTipoFac,String sCodsuc,String sCodunineg){
		Session session = HibernateUtilPruebaCn.currentSession();
		String sql = "";	
		List result = new ArrayList();
		try{
			sql = "from Dfactjdecon as df where df.id.nofactura = " + iNumFac +  " and df.id.tipofactura = '"+sTipoFac+"' and trim(df.id.codsuc) = '000" +sCodsuc+"' and " +
					" trim(df.id.codunineg) = '" + sCodunineg.trim() + "'";
			
			Transaction tx = session.beginTransaction();
			result = session
				.createQuery(sql)			
				.list();
			tx.commit();
		}catch(Exception ex){
			System.out.println("Se capturo una excepcion en FacturaContadoCtrl.buscarDetalleFactura: " + ex);
		}finally{
			session.close();
		}
		return result;
	}
/*****************************************************************************************************************/
/**DETERMINAR SI FACTURA ESTA ANULADA**********************************************/
	public boolean isFacturaAnulada(int iNoFactura, String sTipoFactura){
		boolean bAnulada = true;
		Session session = HibernateUtilPruebaCn.currentSession();
		Hfactjdecon hFac = null;
		String sql = "";
		try{
			sql = "from Hfactjdecon as f where f.id.nofactura = " + iNoFactura +" and f.id.tipofactura = '" +sTipoFactura+"'";
			
			hFac = (Hfactjdecon) session.createQuery(sql).uniqueResult();
					
			
			if(!hFac.getId().getEstado().equals("A")){
				bAnulada = false;
			}
		}catch(Exception ex){
			System.out.println("Se capturo una excepcion en FacturaContadoCtrl.isFacturaAnulada: " + ex);
		}finally{
			session.close();
		}
		return bAnulada;
	}
/******************************************************************************/
	public boolean enviarCorreoCredyCob(String sTo,String sFrom,String sNombreFrom, String sSubject,List lstCc,
										String sDevolucion,String sFactura,String sCliente,
										int iNobatch, String sNodoc,String sMonto,
										String sCaja,String sCompania,String sSucursal){
		// MultiPartEmail email = new MultiPartEmail();		
		String sHora = "";
		String sFecha = "";
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
		boolean enviado = true;
		Date dFecha = new Date();
		String sUrl = "";
		try {
			sFecha = sdf.format(dFecha);
			sHora = dfHora.format(dFecha);
			sUrl = new Divisas().obtenerURL();
			
			if(sUrl==null || sUrl.equals(""))
				sUrl = "http://ap.casapellas.com.ni:9080/GCPMAJA";
			
			
			String shtml = "<table width=\"410px\" style=\"border: 1px #7a7a7a solid\" align=\"center\" cellspacing=\"0\" cellpadding=\"3\">" +
							"<tr>"+
							"<th colspan=\"2\" style=\"border-bottom: 1px #7a7a7a solid; background: #3e68a4\">" +
								"<font face=\"Arial\" size=\"2\" color=\"white\"><b>"+"Notificación de Emisión de Nota de Crédito"+"</b></font>" +
							"</th>" +
							"</tr>"+
							"<tr>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"23%\"><b>Cliente:</b></td>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"77%\">"+ sCliente + "</td>" +
							"</tr>"+
							"<tr>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"23%\"><b>Factura #:</b></td>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"77%\">"+ sFactura +"</td>" +
							"</tr>"+
							"<tr>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"23%\"><b>Devolución #:</b></td>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"77%\">"+sDevolucion+"</td>" +
							"</tr>"+
							"<tr>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"23%\"><b>No. Batch:</b></td>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"77%\">"+ iNobatch +"</td>" +
							"</tr>"+
							"<tr>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"23%\"><b>Documento:</b></td>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"77%\">"+sNodoc+"</td>" +
							"</tr>" +
							"<tr>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"23%\"><b>Monto:</b></td>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"77%\">"+sMonto +"</td>" +
							"</tr>"+
							"<tr>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"23%\"><b>Caja: </b></td>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"77%\">"+sCaja +"</td>" +
							"</tr>"+
							"<tr>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"23%\"><b>Ubicación:</b></td>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"77%\">"+sSucursal +"</td>" +
							"</tr>"+
							"<tr>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"23%\"><b>Compañia:</b></td>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"77%\">"+sCompania +"</td>" +
							"</tr>"+
							"<tr>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"23%\"><b>Fecha:</b></td>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"77%\">"+ sFecha + " / " + sHora +"</td>" +
							"</tr>" +
							"<tr>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"20%\"><b>Enlace:</b></td>" +
								"<td style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"77%\">"+sUrl+"</td>" +
							"</tr>"+
							
							"<tr>" +
								"<td align=\"center\" colspan=\"2\" style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px; color: #3e68a4; border-bottom: 1px ##1a1a1a solid; \">" +
									"<b>"+"Se ha generado esta nota de crédito a favor del cliente"+"</b>" +
								"</td>" +
							"</tr>" +
							
							"<tr>" +
								"<td align=\"center\" colspan=\"2\" style=\"font-family: Arial, Helvetica, sans-serif;font-size: 10px;color: black; border-bottom: 1px ##1a1a1a solid; \">" +
									"<b>Casa Pellas, S. A. - Módulo de Caja</b>" +
								"</td>" +
							"</tr>" +
						"</table>";
			
			
			Divisas dv = new Divisas();
			String sNombreCajero = dv.ponerCadenaenMayuscula(sNombreFrom);
			sNombreCajero = (sNombreCajero.equals(""))?sNombreFrom:sNombreCajero;
			
			List<CustomEmailAddress> copyEmail = new ArrayList<CustomEmailAddress>();
			
			
			if(lstCc!=null && lstCc.size()>0){
				 for (@SuppressWarnings("rawtypes")
				 Iterator iter = lstCc.iterator(); iter.hasNext();) {
					 copyEmail.add(new CustomEmailAddress((String) iter.next()));
				 }
			 }
			
			MailHelper.SendHtmlEmail(
					new CustomEmailAddress(sFrom,sNombreCajero),
					new CustomEmailAddress(sTo), copyEmail, null, 
					sSubject, shtml);
			 
		}catch(Exception ex){
			System.out.println("=>Correo no enviado CtrlSolicitud.enviarCorreo "+ ex);
			enviado = false;			
		}		
		return enviado;
	}
}

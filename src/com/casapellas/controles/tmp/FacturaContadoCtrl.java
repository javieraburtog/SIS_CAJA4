package com.casapellas.controles.tmp;
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

//import org.apache.commons.mail.MultiPartEmail;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.casapellas.controles.ConsolidadoDepositosBcoCtrl;
import com.casapellas.entidades.A03factco;
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
import com.casapellas.util.LogCajaService;
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
				iFechaFacJul = FechasUtil.DateToJulian(new SimpleDateFormat("dd/MM/yyyy").parse(hf.getFecha()));
				
				sConsulta = sql ;
				sConsulta += " sddoc =  " + String.valueOf(hf.getNofactura());
				sConsulta += " and trim(sdmcu) = '"+hf.getCodunineg().trim()+"'";
				sConsulta += " and sdivd = " + String.valueOf(iFechaFacJul);
				sConsulta += " and sdan8 = "+ hf.getCodcli() ;
				
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
									hf.getCodcli(),
									hf.getFechajulian());
					lstFacsActuales.remove(i);
					
					//&& ==== Correito.
//					correo(hf.getNofactura()+" "+hf.getTipofactura()
//							+" "+hf.getCodunineg()+" "+hf.getCodcli());
					
				}
			}
			
			trans.commit();
			cn.commit();
			
		} catch (Exception e) {
			e.printStackTrace();
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
						new CustomEmailAddress(PropertiesSystem.WEBMASTER_EMAIL_ADRESS, "Módulo de Caja"),
						toList,
						"Anulacion de factura "+sData, "");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
/************************************************************************************/	
	public Vf0101 existeCliente(int iCodcli, String sCodSuc){
		Session session = null;
		Vf0101 c = null;
		String sql = "";
		
		try{
			session = HibernateUtilPruebaCn.currentSession();
			sql = "from Vf0101 as c where c.id.aban8 = " + iCodcli + " and c.id.a5co = '" + sCodSuc + "'";
			
			LogCajaService.CreateLog("existeCliente", "QRY", sql);
			
			c = (Vf0101)session.createQuery(sql).uniqueResult();

		}catch(Exception ex){
			LogCajaService.CreateLog("existeCliente", "ERR", ex.getMessage()); 
		}

		return c;
	}
/************************************************************************************/
	public Hf4211 comprobarSiExisteF4211(int iNofactura,String sTipoFactura, String sCodsuc,String sCodunineg){
		Hf4211 f = null;
		Session session = null;
		String sql = "";
		
		try{
			session = HibernateUtilPruebaCn.currentSession();
			sql = "from Hf4211 as f where f.id.nofactura = " + iNofactura
					+ " and f.id.tipofactura = '" + sTipoFactura
					+ "' and f.id.codsuc = '" + sCodsuc
					+ "' and trim(f.id.codunineg) = '" + sCodunineg.trim()
					+ "'";

			f = (Hf4211)session.createQuery(sql).uniqueResult();

		}catch(Exception ex){
			ex.printStackTrace(); 
		}finally{
			try {HibernateUtilPruebaCn.closeSession(session); } catch (Exception e2) {e2.printStackTrace(); }
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
						+ "' and codcli = "+d.getId().getCodcli() 
						+ "  and fecha = "+d.getId().getFecha();
				
				ps = cn.prepareStatement(sql);
				int rs = ps.executeUpdate();
				
				if (rs == 0){
					bHecho = false;
					break;
				}
			}
		}catch(Exception ex){
			bHecho = false;
			ex.printStackTrace();
		}finally {
			try {
				ps.close();
			} catch (Exception se2) {
				se2.printStackTrace();
			}
		}
		return bHecho;
	}
/************************************************************************************/	
	public boolean actualizarFacturaFDC(Connection cn, int iNoFac,
					String sTipoFac, String sCodsuc, String sCodunineg, int iCodcli,
					int iSubtotal, int iTotal, int subtotalf, int totalf,
					BigDecimal bdCosto, int iFecha){
		
		boolean bHecho = true;
		PreparedStatement ps = null;
		String sql = "";
		
		try{
			
			iSubtotal = Math.abs(iSubtotal);
			iTotal	  = Math.abs(iTotal);
			subtotalf = Math.abs(subtotalf);
			totalf	  = Math.abs(totalf);
			
			sql = "update "+PropertiesSystem.ESQUEMA+".a02factco set subtotal = " + iSubtotal
					+ ", total =" + iTotal + ", subtotalf = " + subtotalf
					+ ", totalf =" + totalf 
					+ ", totalcosto = "+bdCosto.toString()
					+ " where nofactura = " + iNoFac
					+ " and tipofactura = '" + sTipoFac + "' and codsuc = '"
					+ sCodsuc + "'" + " and trim(codunineg) = '"
					+ sCodunineg.trim() + "' and codcli =" + iCodcli
					+ " and fecha = "+iFecha;
			
			LogCajaService.CreateLog("actualizarFacturaFDC", "QRY", sql);
			
			ps = cn.prepareStatement(sql);
			int rs = ps.executeUpdate();
			if (rs == 0){
				bHecho = false;
			}
		}catch(Exception ex){
			bHecho = false;
			LogCajaService.CreateLog("actualizarFacturaFDC", "ERR", ex.getMessage());
		}finally {
			try {
				ps.close();
			} catch (Exception se2) {
				LogCajaService.CreateLog("actualizarFacturaFDC", "ERR", se2);
			}
		}
		return bHecho;
	}
/************************************************************************************/	
	public boolean anularFacturaFDC(Connection cn,int iNoFac,String sTipoFac,
					String sCodsuc,String sCodunineg,int iCodcli, int iFecha){
		boolean bHecho = true;
		PreparedStatement ps = null;
		String sql = "";
		try{
		
			sql = "update " + PropertiesSystem.ESQUEMA
					+ ".a02factco set estado = 'A' where nofactura = " + iNoFac
					+ " and tipofactura = '" + sTipoFac + "' and codsuc = '"
					+ sCodsuc + "'" + " and trim(codunineg) = '"
					+ sCodunineg.trim() + "' and codcli =" + iCodcli
					+ " and fecha = " + iFecha;
			
			LogCajaService.CreateLog("anularFacturaFDC", "QRY", sql);
			
			ps = cn.prepareStatement(sql);
			int rs = ps.executeUpdate();
			if (rs == 0){
				bHecho = false;
			}
		}catch(Exception ex){
			LogCajaService.CreateLog("anularFacturaFDC", "ERR", ex.getMessage());
			bHecho = false;
		}finally {
			try {
				ps.close();
			} catch (Exception se2) {
				bHecho = false;
				LogCajaService.CreateLog("anularFacturaFDC", "ERR", se2.getMessage());
			}
		}
		return bHecho;
	}
/************************************************************************************/
	@SuppressWarnings("unchecked")
	public List<Df4211> buscarDetalleFactura(Hf4211 f){
		List<Df4211> lstResult = null;
		
		try{
			
			String strSql = " select * from " + PropertiesSystem.ESQUEMA + ".@DF4211X " +
					"where trim(codunineg) = '" +f.getId().getCodunineg().trim()+ "' and nofactura = " 
					+ f.getId().getNofactura() + " and tipofactura = '" 
					+ f.getId().getTipofactura() + "' and codsuc =  '" 
					+ f.getId().getCodsuc() + "' and codcli = " 
					+ f.getId().getCodcli() + " and fecha = " + f.getId().getFecha(); ;
			
			lstResult = (ArrayList<Df4211>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSql.replace("@DF4211X", "DF4211"), true, Df4211.class );		
			
			if( lstResult != null && !lstResult.isEmpty() )
				return lstResult;
			
			lstResult = (ArrayList<Df4211>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSql.replace("@DF4211X", "DF42119"), true, Df4211.class );		
			
					
		}catch(Exception ex){
			ex.printStackTrace(); 
		} 
		
		return lstResult;
	}
/************************************************************************************/
	public boolean insertarDetalleFacturaFDC(Connection cn,List lstDet){
		boolean bHecho = true;
		String sql = "";
		PreparedStatement ps = null;
		Df4211 f = null;
		
		Session sesion = null;
		Transaction trans = null;
		boolean bNuevo = false;
		
		try{
			sesion = HibernateUtilPruebaCn.currentSession();
			if(sesion.getTransaction().isActive())
				trans = sesion.getTransaction();
			else{
				trans = sesion.beginTransaction();
				bNuevo = true;
			}
				
			
			for (int i = 0; i < lstDet.size(); i ++){
				f = (Df4211)lstDet.get(i);
				A03factco a3 = null;
				
				//&& ===== Verificar si existe el detalle y si existe actualizarlo.
				a3 = (A03factco)sesion.createCriteria(A03factco.class).setMaxResults(1)
				.add(Restrictions.eq("id.codcli", f.getId().getCodcli() ))
				.add(Restrictions.eq("id.fecha", f.getId().getFecha()))
				.add(Restrictions.eq("id.nofactura",f.getId().getNofactura() ))
				.add(Restrictions.eq("id.tipofactura", f.getId().getTipofactura()))
				.add(Restrictions.eq("id.codsuc",f.getId().getCodsuc()))
				.add(Restrictions.eq("id.codunineg", f.getId().getCodunineg()))
				.add(Restrictions.eq("id.coditem",f.getId().getCoditem() ))
				.add(Restrictions.eq("id.enviadoa",f.getId().getEnviadoa()))
				.add(Restrictions.eq("id.linea",f.getId().getLinea()))
				.uniqueResult();
				
				if(a3 == null ){
					
					sql = "INSERT INTO "+PropertiesSystem.ESQUEMA+".A03FACTCO VALUES("
							+ f.getId().getNofactura() + ",'"
							+ f.getId().getTipofactura() + "','"
							+ f.getId().getCoditem() + "','"
							
							+ new Divisas().remplazaCaractEspeciales(
								f.getId().getDescitem().trim(), "'", new String())+ "',"
							
							+ Math.abs(f.getId().getPreciounit()) + "," 
							+ f.getId().getCant() + ",'" 
							+ f.getId().getImpuesto() + "',"
							+ Math.abs(f.getId().getFactor()) + ",'"
							+ f.getId().getCodunineg() + "','"
							+ f.getId().getCodsuc() +"', "
							+ f.getId().getTotalcosto().abs()+", "
							+"0, " //&& ==== Descuento
							+ f.getId().getCodcli() +", "
							+ f.getId().getFecha() +", "
							+ "'"+ f.getId().getLote().trim() +"', "
							+ ((f.getId().getEnviadoa() == 0)?  f.getId().getCodcli():f.getId().getEnviadoa()) +", " 
							+ f.getId().getLinea() +", "
							
							+ " 0 , " // porisc
							+ " 0 , " // valisc
							+ " 0 , " // desctovta
							+ " 0 , " // desctoisc
							+ " 0 , " // valiscfinal
							+ " '' "  // desgloseisc

							+ " )";
					
				}else{
					sql = "UPDATE "+PropertiesSystem.ESQUEMA+".A03FACTCO SET " +
								"preciounit =  " + Math.abs(f.getId().getPreciounit()) + "," + 
								"cant = " 	   	 + f.getId().getCant() + "," +
								"impuesto = '"   + f.getId().getImpuesto() + "'," +
								"factor = "	   	 + Math.abs(f.getId().getFactor()) + ","+
								"pcosto = "      + f.getId().getTotalcosto().abs()+", "  +
								"descuento = "   + 0 + ","+
								"lote = '"	     +f.getId().getLote().trim() +"'"+
						  " WHERE codcli =  " 	 + f.getId().getCodcli()+
								" and fecha = "  + f.getId().getFecha() +
								" and nofactura =" +f.getId().getNofactura() +
								" and tipofactura = '" +f.getId().getTipofactura()+"'" +
								" and codsuc = '" + f.getId().getCodsuc() +"'" +
								" and codunineg = '" +f.getId().getCodunineg() +"'" +
								" and coditem = '" + f.getId().getCoditem() + "'" + 
								" and enviadoa = " + f.getId().getEnviadoa() +
								" and linea = "+ f.getId().getLinea();
				}
				 
				ps = cn.prepareStatement(sql);
				int rs = ps.executeUpdate();
				if (rs == 0){
					bHecho = false;
					break;
				}
			}
		}catch(Exception ex){
			
			bHecho = false;
			ex.printStackTrace(); 
			
		}finally {
			try {  ps.close(); } catch (Exception se2) {se2.printStackTrace();	}
			if(bNuevo){
				try { trans.commit(); } catch (Exception e) { e.printStackTrace();}
				try { HibernateUtilPruebaCn.closeSession(sesion);}catch (Exception e) { e.printStackTrace();}
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
					+ f.getId().getCodcli()+ ",'" 
					+ sNomcli + "','" 
					+ f.getId().getCodunineg() + "','"
					+ f.getId().getUnineg() + "','" 
					+ f.getId().getCodsuc()+ "','" 
					+ f.getId().getNomsuc() + "','"
					+ f.getId().getCodcomp() + "','" 
					+ f.getId().getNomcomp()+ "',"
					+ f.getId().getFecha() + ","
					+ f.getId().getSubtotal().abs() + ",'" 
					+ f.getId().getMoneda()+ "'," 
					+ f.getId().getTasa().abs() + ",'"
					+ f.getId().getTipopago() + "'," 
					+ f.getId().getFechagrab()+ ",'" 
					+ f.getId().getHechopor() + "','"
					+ f.getId().getPantalla() + "'," 
					+ f.getId().getHora()+ ",'" 
					+ f.getId().getEstado() + "',"
					+ f.getId().getNodoco() + ",'" 
					+ f.getId().getTipodoco()+ "'," 
					+ f.getId().getTotal().abs() + ",'"
					+ f.getId().getSdlocn() + "','" 
					+ f.getId().getProgramaid()+ "'," 
					+ f.getId().getCodvendor() + ","
					+ f.getId().getSubtotalf().abs() + "," 
					+ f.getId().getTotalf().abs() + ", "
					+ f.getId().getTotalcosto().abs()+","
					+ "0, "
					+  ((f.getId().getEnviadoa() == 0)?  f.getId().getCodcli():f.getId().getEnviadoa()) + ", " 
					
					+ " 0 , " // valisc
					+ " 0 , " // desctovta
					+ " 0 , " // desctovta
					+ " 0 , " // valiscfinal
					+ " '', "  // desgloseisc
					+ " '',  "  // exoneraisc
					+  f.getId().getFechadoco()	 // fechadoco, fecha del documento original a devolver
					
					+ " )";  
			
			ps = cn.prepareStatement(sql);
			int rs = ps.executeUpdate();
			
			cn.commit();
			
			if (rs != 1){
				bHecho = false;
			}else{ 
				lstDet = buscarDetalleFactura(f);
				bHecho = insertarDetalleFacturaFDC(cn,lstDet);
			}
			
			cn.commit();
			
			
		}catch(Exception ex){
			bHecho = false; 
			ex.printStackTrace(); 
		}finally {
			
			try {
				ps.close();
			} catch (Exception se2) {
				se2.printStackTrace(); 
			}
		}
		return bHecho;
	}
/************************************************************************************/
	public Vhfactura comprobarSiExiste(int iNofactura,String sTipoFactura, 
							String sCodsuc,String sCodunineg, int iCodcli, int iFecha, int hora, String estado){
		Vhfactura f = null;
		Session session = null;

		try{
			
			session = HibernateUtilPruebaCn.currentSession();
			
			Criteria cr = session.createCriteria(Vhfactura.class);
			cr.add(Restrictions.eq("id.nofactura",iNofactura));
			cr.add(Restrictions.eq("id.tipofactura",sTipoFactura));
			cr.add(Restrictions.sqlRestriction("cast(codsuc as numeric(5,0)) = '" + sCodsuc.trim() + "'"));
			cr.add(Restrictions.eq("id.codcli", iCodcli));
			cr.add(Restrictions.eq("id.fecha",  iFecha));
			cr.add(Restrictions.sqlRestriction("trim(codunineg) = '"+sCodunineg.trim()+ "'"));
			
			LogCajaService.CreateLog("comprobarSiExiste", "HQRY", LogCajaService.toSql(cr));
			
			f = (Vhfactura)cr.uniqueResult();
			
		}catch(Exception ex){
			f = null;
			LogCajaService.CreateLog("comprobarSiExiste", "ERR", ex.getMessage());
		}

		return f;
	}
/************************************************************************************/
	
	@SuppressWarnings("unchecked")
	public List<Hf4211> getFacturasFDC(String sUnineg,String sLoc,int iFecha){ // MODIFICADA
		List<Hf4211> lstHf42119temp = new ArrayList<Hf4211>();
		List<Hf4211> lstHf4211temp = new ArrayList<Hf4211>();
		List<Hf4211> lstHfacturas = new ArrayList<Hf4211>();
		List<Hf4211> lstHf4211Unicas = new ArrayList<Hf4211>();
		
		Session sesion = null;
		Transaction trans = null;
		boolean bNuevaCn = false;
		
		String sql = "";
		boolean bMicro = false;
		String sNotIn = "";
		
		try{
			
			sesion = HibernateUtilPruebaCn.currentSession();
			if(sesion.getTransaction().isActive())
				trans = sesion.getTransaction();
			else{
				trans = sesion.beginTransaction();
				bNuevaCn = true;
			}
			
			String sLinea = sUnineg.trim().substring(2,4);
			if(sLinea.compareTo("22") == 0 || sLinea.compareTo("23") == 0 || sLinea.compareTo("24") == 0 )
				bMicro = true;	
			
			sql =  " from Hf4211 as f";
			sql += " where trim(f.id.codunineg) = '"+ sUnineg.trim() +"'"; 
			sql += " and f.id.fecha=" + iFecha;
			
			if(!bMicro)
				sql += " and trim(f.id.sdlocn) = '" +sLoc.trim()+"'";	
			
			List<Hf4211> lstHf4211Tmp = new ArrayList<Hf4211>();
			lstHf4211Tmp = sesion.createQuery(sql).list();
			
			if(lstHf4211Tmp != null && !lstHf4211Tmp.isEmpty()){
				lstHf4211temp.addAll(lstHf4211Tmp);
				lstHf4211Tmp = null;    
			}
			
			if( bMicro ) {
				List<Hf4211> lstHf42119Tmp = new ArrayList<Hf4211>();
				String sqlTmp = "SELECT HF.* FROM " + PropertiesSystem.ESQUEMA + ".HF42119 HF " +
						"WHERE HF.NOFACTURA NOT IN ( " +
						"  SELECT A02.NOFACTURA FROM "+PropertiesSystem.ESQUEMA+".A02FACTCO A02 " +
						"  WHERE (A02.NOFACTURA = HF.NOFACTURA AND A02.TIPOFACTURA = HF.TIPOFACTURA AND A02.CODCOMP = HF.CODCOMP AND A02.CODUNINEG = HF.CODUNINEG AND A02.FECHA = HF.FECHA AND A02.CODCLI = HF.CODCLI)) " +
						"AND HF.FECHA = " + iFecha + " AND HF.SDNXTR =  999 AND HF.SDLTTR = 620 AND TRIM(HF.CODUNINEG) = '"+ sUnineg.trim() +"' AND HF.TIPOPAGO IN ('00','01','001')";
				lstHf42119Tmp = sesion.createSQLQuery(sqlTmp).addEntity(Hf4211.class).list();
				if(lstHf42119Tmp != null && !lstHf42119Tmp.isEmpty()){
					lstHf42119temp.addAll(lstHf42119Tmp);
					lstHf42119Tmp = null;
				}
			}
			
			//CONSULTAR FACTURAS DEL F4211
			if(lstHf4211temp != null && lstHf4211temp.size() > 0 && bMicro){

				for (int i = 0; i < lstHf4211temp.size(); i++) {
					Hf4211 hf = lstHf4211temp.get(i);
					
					if(sNotIn.contains(String.valueOf(hf.getId().getNofactura()))){
//						lstHf4211temp.remove(i);
						continue;
					}
					
					sNotIn += String.valueOf(hf.getId().getNofactura()+",");
					
					List<Hf4211>lstDetMicro  = (ArrayList<Hf4211>)
						sesion.createCriteria(Hf4211.class)
					.add(Restrictions.eq("id.codunineg", hf.getId().getCodunineg()))
					.add(Restrictions.eq("id.nofactura", hf.getId().getNofactura()))
					.add(Restrictions.eq("id.fecha", hf.getId().getFecha()))
					.list();
					

					int iSbt = 0;
					int iTtl = 0;
					int iSbtf = 0;
					int iTtlf = 0;
					BigDecimal bdCosto = BigDecimal.ZERO;
					if(lstDetMicro != null && lstDetMicro.size() > 0 ){
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
						lstHf4211temp.set(i, hf);
						lstHf4211Unicas.add(hf);
					}
					
//					lstHfacturas.add(lstHf4211temp.get(i));
					lstHfacturas.add( hf );
					
				}
			}
			//FIN CONSULTAS F4211
			
			//CONSULTAR FACTURAS DEL F42119
			if(lstHf42119temp != null && lstHf42119temp.size() > 0 && bMicro){

				for (int i = 0; i < lstHf42119temp.size(); i++) {
					Hf4211 hf = lstHf42119temp.get(i);
					
					if(sNotIn.contains(String.valueOf(hf.getId().getNofactura()))){
						lstHf42119temp.remove(i);
						continue;
					}
					
					sNotIn += String.valueOf(hf.getId().getNofactura()+",");
					
					String strSql = 
						" SELECT * FROM  "+PropertiesSystem.ESQUEMA+".Hf42119 "  + 
						" WHERE TRIM(CODUNINEG) = '" + hf.getId().getCodunineg().trim() + "' " + 
						" AND NOFACTURA = " + hf.getId().getNofactura() + " AND FECHA =" + hf.getId().getFecha() ;
					
					List<Hf4211>lstDetMicro  = (ArrayList<Hf4211>) sesion.createSQLQuery(strSql).addEntity(Hf4211.class).list();

					int iSbt = 0;
					int iTtl = 0;
					int iSbtf = 0;
					int iTtlf = 0;
					BigDecimal bdCosto = BigDecimal.ZERO;
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
						lstHf42119temp.set(i, hf);
					}
					lstHfacturas.add(lstHf42119temp.get(i));
				}
			}
			//FIN CONSULTA F42119
			
			if(lstHfacturas != null && !lstHfacturas.isEmpty() ) {
				for (int i = 0; i < lstHfacturas.size(); i++) {
					Hf4211 hf = lstHfacturas.get(i);
					hf.getId().setTotal(hf.getId().getTotal().abs());
					hf.getId().setTotalf(hf.getId().getTotalf().abs());
					hf.getId().setSubtotal(hf.getId().getSubtotal().abs());
					hf.getId().setSubtotalf(hf.getId().getSubtotalf().abs());
					hf.getId().setTotalcosto(hf.getId().getTotalcosto().abs());
					lstHfacturas.set(i, hf);
				}
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
			lstHf42119temp = new ArrayList<Hf4211>();
		    lstHf4211temp = new ArrayList<Hf4211>();
			lstHfacturas = new ArrayList<Hf4211>();
		}finally{
			if(bNuevaCn){
				try {trans.commit();} catch (Exception e2) {e2.printStackTrace(); }
				try {HibernateUtilPruebaCn.closeSession(sesion); }
				catch (Exception e2) {e2.printStackTrace(); }
			}
		}
		return lstHfacturas;
	}
	
	
/************************************************************************************/	
/**BUSCAR FACTURAS DE CONTADO POR PARAMETRO, FECHA EN FORMATO JULIANO  Y TIPO DE BUSQUEDA(1 = por nombre de cliente, 2 = por codigo de cliente, 3 = por numero de factura)**/
	public List buscarFactura(String sParametro, int iFecha,String[] sTipoFactura,String sMoneda, String sRangoFacs, int iTipo, F55ca017[] f55ca017, List lstLocalizaciones, int iCaid){
		List lstFacturas = null, f55ca017_1 = new ArrayList(), f55ca017_2 = new ArrayList();
		String sql = "", sql2 = "",sFiltro="";
		Session session =  null;
		String sLoc = "";
		boolean bHayLoc = false, bSoloLocs = false;
		int iFechaInicio = 110025;
		
		try{
			 session = HibernateUtilPruebaCn.currentSession();
			
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
								
			}
			
			
			//
			if (sRangoFacs.equals("04")){
				sql = sql + ") and trim(f.estado) = 'A' and f.nofactura not in(" +
						"select rf.numfac from "+PropertiesSystem.ESQUEMA+".Recibofac " +
						"as rf where rf.numfac = f.nofactura and rf.codcomp = f.codcomp " +
						"and rf.tipofactura = f.tipofactura and rf.estado = '' " +
						"and f.fecha = rf.fecha and f.codcli = rf.codcli " +
						"and trim(rf.codunineg) = trim(f.codunineg) ) " ;
				
				sql2 = sql2
						+ ")) and trim(f.estado) = 'A' and f.nofactura not in(" +
						"select rf.numfac from " + PropertiesSystem.ESQUEMA +"" +
						".Recibofac as rf where rf.numfac = f.nofactura and " +
						"rf.codcomp = f.codcomp and rf.tipofactura = f.tipofactura " +
						"and f.fecha = rf.fecha and f.codcli = rf.codcli " +
						"and rf.estado = '' and trim(rf.codunineg) = trim(f.codunineg)) ";
				
				sFiltro += " and trim(f.estado) ='A'";
				
			}else {
				sql = sql + ") and trim(f.estado) = '' and f.nofactura not in(" +
						"select rf.numfac from "+PropertiesSystem.ESQUEMA+
						".Recibofac as rf where rf.numfac = f.nofactura " +
						"and rf.codcomp = f.codcomp and rf.tipofactura = f.tipofactura " +
						"and f.fecha = rf.fecha and f.codcli = rf.codcli " +
						"and rf.estado <> 'A' and trim(rf.codunineg) = trim(f.codunineg)) ";
				
				sql2 = sql2 + ")) and trim(f.estado) = '' and f.nofactura not in(" +
						"select rf.numfac from "+PropertiesSystem.ESQUEMA+
						".Recibofac as rf where rf.numfac = f.nofactura and " +
						"rf.codcomp = f.codcomp and rf.tipofactura = f.tipofactura " +
						"and f.fecha = rf.fecha and f.codcli = rf.codcli " +
						"and rf.estado <> 'A' and trim(rf.codunineg) = trim(f.codunineg)) ";
				
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
			sExcl += " and tf.codcli = f.codcli and tf.fechafac = f.fecha" ;
			sExcl += " and tf.estadotr in ('E','R') and tf.caidorig = "+iCaid + " )";
			sql += sExcl;
			sql2+= sExcl;
			
			//--- Incluir las facturas recibidas desde otra caja.
			sql += " UNION ";
			sql += " select f.* from "+PropertiesSystem.ESQUEMA+".Hfactjdecon as f inner join "+PropertiesSystem.ESQUEMA+".trasladofac tf"; 
			sql += " on  tf.nofactura = f.nofactura and tf.codcomp = f.codcomp and tf.tipofactura = f.tipofactura";
			sql += " and tf.codsuc = f.codsuc and trim(tf.codunineg)  = trim(f.codunineg) and tf.estadotr in ('E','R')";
			sql += " and tf.codcli = f.codcli and tf.fechafac = f.fecha " ;
			sql += " and tf.caiddest = "+iCaid;
			sql += sFiltro;
			
			if(bHayLoc){
				sql = sql + " union " + sql2;
			}
			if(bSoloLocs){
				LogCajaService.CreateLog("buscarFactura", "QRY", sql);
				lstFacturas = session.createSQLQuery(sql).addEntity(Hfactjdecon.class).list();	
			}else{
				sql2 += " UNION ";
				sql2 += " select f.* from "+PropertiesSystem.ESQUEMA+".Hfactjdecon as f inner join "+PropertiesSystem.ESQUEMA+".trasladofac tf"; 
				sql2 += " on  tf.nofactura = f.nofactura and tf.codcomp = f.codcomp and tf.tipofactura = f.tipofactura";
				sql2 += " and tf.codsuc = f.codsuc and trim(tf.codunineg)  = trim(f.codunineg) ";
				sql2 += " and tf.codcli = f.codcli  and tf.fechafac = f.fecha" ;
				sql2 += " and tf.estadotr in ('E','R') and tf.caiddest = "+iCaid;
				sql2 += sFiltro;
				
				LogCajaService.CreateLog("buscarFactura", "QRY", sql2);
				lstFacturas = session.createSQLQuery(sql2).addEntity(Hfactjdecon.class).list();	
			}
		}catch(Exception ex){
			LogCajaService.CreateLog("buscarFactura", "ERR", ex.getMessage());
		}

		return lstFacturas;
	}
/*******************************************************************************************************************************/
/**BUSCAR DETALLE DE FACTURA**********************************************/
	public List buscarDetalleFactura(int iNumFac, String sTipoFac,String sCodsuc,String sCodunineg){
		Session session = null;
		String sql = "";	
		List result = new ArrayList();
		
		try{
			session = HibernateUtilPruebaCn.currentSession();
			sql = "from Dfactjdecon as df where df.id.nofactura = " + iNumFac
					+ " and df.id.tipofactura = '" + sTipoFac
//					+ "' and trim(df.id.codsuc) = concat(substring('0000',0,6-length(trim(" + sCodsuc + "))),trim(" + sCodsuc + "))  and "
					+ "' and df.id.codsuc = " + sCodsuc + "  and "
					+ " trim(df.id.codunineg) = '" + sCodunineg.trim() + "'";
			
			result = session.createQuery(sql).list();
			
		}catch(Exception ex){
			ex.printStackTrace(); 
		}finally{
			try {HibernateUtilPruebaCn.closeSession(session); } catch (Exception e2) {e2.printStackTrace(); }
		}
		return result;
	}
/*****************************************************************************************************************/
/**DETERMINAR SI FACTURA ESTA ANULADA**********************************************/
	public boolean isFacturaAnulada(int iNoFactura, String sTipoFactura){
		boolean bAnulada = true;
		Hfactjdecon hFac = null;
		
		try{
			
			String sql  = "from Hfactjdecon as f where f.id.nofactura = " + iNoFactura + " and f.id.tipofactura = '" + sTipoFactura + "'";
			
			hFac = ConsolidadoDepositosBcoCtrl.executeSqlQueryUnique(sql, Hfactjdecon.class, false);
			
			bAnulada = hFac.getId().getEstado().compareTo( "A") == 0 ;
			
			
		}catch(Exception ex){
			 ex.printStackTrace(); 
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
			
			List<CustomEmailAddress> ccList = new ArrayList<CustomEmailAddress>();
			if(lstCc!=null && lstCc.size()>0){
				 for (Iterator iter = lstCc.iterator(); iter.hasNext();)
					ccList.add(new CustomEmailAddress((String) iter.next()));
			 }
			
			MailHelper.SendHtmlEmail(
					new CustomEmailAddress(sFrom,sNombreCajero),
					new CustomEmailAddress(sTo), ccList, 
					sSubject, shtml);
			 
		}catch(Exception ex){
			ex.printStackTrace();
			enviado = false;			
		}		
		return enviado;
	}
}

package com.casapellas.controles.tmp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.faces.context.FacesContext;

//import org.apache.commons.mail.MultiPartEmail;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.casapellas.controles.*;
import com.casapellas.entidades.Arqueorec;
import com.casapellas.entidades.Bien;
import com.casapellas.entidades.Cambiodet;
import com.casapellas.entidades.CambiodetId;
import com.casapellas.entidades.Credhdr;
import com.casapellas.entidades.Dfactura;
import com.casapellas.entidades.F55ca011;
import com.casapellas.entidades.F55ca014;
import com.casapellas.entidades.F55ca018;
import com.casapellas.entidades.F55ca024;
import com.casapellas.entidades.F55ca03;
import com.casapellas.entidades.FacturaxRecibo;
import com.casapellas.entidades.Hfactjdecon;
import com.casapellas.entidades.Hfactura;
import com.casapellas.entidades.Hfacturajde;
import com.casapellas.entidades.MetodosPago;
import com.casapellas.entidades.Recibo;
import com.casapellas.entidades.ReciboId;
import com.casapellas.entidades.Recibodet;
import com.casapellas.entidades.RecibodetId;
import com.casapellas.entidades.Recibofac;
import com.casapellas.entidades.RecibofacId;
import com.casapellas.entidades.Recibojde;
import com.casapellas.entidades.RecibojdeId;
import com.casapellas.entidades.Reintegro;
import com.casapellas.entidades.ReintegroId;
import com.casapellas.entidades.ReporteIR;
import com.casapellas.entidades.Vf0101;
import com.casapellas.entidades.Vf0101Id;
import com.casapellas.entidades.Vf0311fn;
import com.casapellas.entidades.Vmarca;
import com.casapellas.entidades.Vmodelo;
import com.casapellas.entidades.Vmonedafactrec;
import com.casapellas.entidades.Vrecfac;
import com.casapellas.entidades.VrecibosxtipompagoId;
import com.casapellas.entidades.Vretencion;
import com.casapellas.entidades.VtipoProd;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.jde.creditos.BatchControlF0011;
import com.casapellas.jde.creditos.CodigosJDE1;
import com.casapellas.jde.creditos.DefaultJdeFieldsValues;
import com.casapellas.navegacion.As400Connection;
import com.casapellas.util.*;

/**
 * CASA PELLAS S.A. Creado por.........: Juan Carlos Ñamendi Pineda Fecha de
 * Creación..: 22/06/2009 Última modificación: 09/07/2012 Modificado por.....:
 * Juan Carlos Ñamendi Pineda Detalle Anterior ..: Correccion al grabar el
 * detalle dell recibo
 * 
 */
public class ReciboCtrl {
	public Exception errorDetalle;
	public Exception error;
	Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();

	//Valores de insercion de documentos en JDE
	String[] valoresJdeIns = (String[]) m.get("valoresJDEInsContado");
	public Exception getError() {
		return error;
	}

	public void setError(Exception error) {
		this.error = error;
	}

	public Exception getErrorDetalle() {
		return errorDetalle;
	}

	public void setErrorDetalle(Exception errorDetalle) {
		this.errorDetalle = errorDetalle;
	}

	public static String validatePaymentForInvoice(List<Hfactura>invoices, int caid) {
		String msg = "";
		try {
			
			String numberTypeCondition = "" ;
			for(Hfactura hf: invoices) {
				numberTypeCondition += 
					" (	@NUMEROFACTURA = "+ hf.getNofactura()+
						" and trim(TIPOFACTURA) = '" + hf.getTipofactura().trim() + "'" +
						" and hora = "+hf.getHoraEntera()+ 
					") or  " ;
			}
			numberTypeCondition = numberTypeCondition.substring(0, numberTypeCondition.lastIndexOf("or"));
			
			String query = "select nofactura, tipofactura from @BDCAJA.a02factco " +
					" where fecha = @FECHA and codcli = @CODCLI " +
					" and trim(codcomp) ='@CODCOMP' and estado = 'A' and ( @NUMBER_TYPE )  " ;
			
			query = query
			.replace("@BDCAJA", PropertiesSystem.ESQUEMA  )
			.replace("@FECHA",  String.valueOf( invoices.get(0).getFechajulian() ) )
			.replace("@CODCLI", String.valueOf(  invoices.get(0).getCodcli() ) )
			.replace("@CODCOMP",invoices.get(0).getCodcomp().trim()  )
			.replace("@NUMBER_TYPE", numberTypeCondition )
			.replace("@NUMEROFACTURA", "nofactura" );
			
			@SuppressWarnings("unchecked")
			List<Object[]> lstFacturas = (List<Object[]>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, true, null);
			if( lstFacturas != null && !lstFacturas.isEmpty() ) {
				
				for(Object[] ob: lstFacturas) {
					msg +=" [factura: "+String.valueOf(ob[0])+", Tipo: "+String.valueOf(ob[1])+"]";
				}
				
				return msg = " Una o mas facturas fueron anuladas: " + msg ;
				
			}
			
			query = 
			" select * from @BDCAJA.recibofac " +
			" where caid = @CAID and trim(codcomp) ='@CODCOMP' " +
			" and codcli = @CODCLI and fecha = @FECHA and (@NUMBER_TYPE) and estado = '' " ;
			
			query = query
			.replace("@BDCAJA", PropertiesSystem.ESQUEMA  )
			.replace("@CAID", String.valueOf(caid) )
			.replace("@CODCOMP",invoices.get(0).getCodcomp().trim()  )
			.replace("@CODCLI", String.valueOf(  invoices.get(0).getCodcli() ) )
			.replace("@FECHA", String.valueOf( invoices.get(0).getFechajulian() ) )
			.replace("@NUMBER_TYPE", numberTypeCondition )
			.replace("@NUMEROFACTURA", "numfac" );
			
			LogCajaService.CreateLog("validatePaymentForInvoice", "QEY", query);
			
			@SuppressWarnings("unchecked")
            List<Recibofac> recibofacs = (List<Recibofac>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, true, Recibofac.class);
			
			if(recibofacs == null || recibofacs.isEmpty() )
				return msg = "";
			
			for(Recibofac rf: recibofacs) {
				
				msg = "[ Factura "+ rf.getId().getNumfac() + ", Tipo: " + rf.getId().getTipofactura() + "]  ";
			}
			msg  = " Una o mas factura ya fue procesada -> " + msg;
			
		}catch(Exception e) {
			LogCajaService.CreateLog("validatePaymentForInvoice", "ERR", e.getMessage());
			e.printStackTrace();
			msg += " -> Error: " + e.getMessage();
		}finally {
			
			if(!msg.isEmpty()) {
				return msg = "No es posible procesar el recibo: " + msg ;
			}
		}
		
		
		return msg;
	}
	
	public static String validateReceiptTotalAmounts(
			boolean domestic,long batchnumber, 
			String appliedAmount, int caid, 
			String codcomp, int numrec, 
			int codcli) {
		
		String msg = "";
		
		try {
			
			String fieldAmount = (domestic)? "rpag" : "rpacr" ;
			
			String queryValidate = 
			  " select " 
			+ " (@AMOUNTRECEIPT), " 
			+ " icame, " 
			+ " ( select sum(abs(@AMOUNTFIELD)) from @JDEDTA.f0311 where rpicu = f.icicu and rpan8 = @CODCLI  ) " 
			+ " from  @JDEDTA.f0011 f " 
			+ " where f.icicu = @NOBATCH " ;
					
			queryValidate = queryValidate
				.replace("@BDCAJA", PropertiesSystem.ESQUEMA )
				.replace("@AMOUNTRECEIPT", appliedAmount )
				.replace("@AMOUNTFIELD", fieldAmount)
				.replace("@CODCLI", String.valueOf(codcli) )
				.replace("@JDEDTA", PropertiesSystem.JDEDTA)
				.replace("@NOBATCH",String.valueOf(batchnumber) ); 
			
			@SuppressWarnings("unchecked")
			List<Object[]> dtaAmounts = (List<Object[]>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(queryValidate,true, null);
			
			String amountMcaja= String.valueOf(dtaAmounts.get(0)[0]).replace(".", "");
			String amountF0011= String.valueOf(dtaAmounts.get(0)[1]);
			String amountF0311= String.valueOf(dtaAmounts.get(0)[2]);
			
			if(amountMcaja.compareTo(amountF0011) == 0 && amountMcaja.compareTo(amountF0311) == 0) {
				return msg = "";
			}
			
			if( amountMcaja.compareTo(amountF0011)  != 0 ) {
				 
				int iAmountReceipt = Integer.parseInt( amountMcaja ) ;
				int iAmountJde = Integer.parseInt( amountF0011 ) ;
				boolean jdeEqualsAmounts =  amountF0011.compareTo(amountF0311) == 0;
				
				if( Math.abs( iAmountJde - iAmountReceipt ) <= 1  && jdeEqualsAmounts ) {
					msg = "";
				}else {
					msg = "El monto aplicado en caja {"+amountMcaja+"} por difiere al monto aplicado en batch {"+amountF0011+"} ";
				}
				return msg;
			}
			
			if( amountMcaja.compareTo(amountF0311)  != 0 ) {
				return msg = "El monto aplicado en caja {"+amountMcaja+"} difiere al monto aplicado en saldos de facturas {"+amountF0311+"} ";
			}
			if( amountF0011.compareTo(amountF0311)  != 0 ) {
				return msg = "El monto aplicado en batch {"+amountF0011+"} difiere al monto aplicado en saldos de facturas {"+amountF0311+"} ";
			}
					
					
		}catch(Exception e) {
			e.printStackTrace(); 
		}finally {
			
			if(!msg.isEmpty()) {
				msg += ", favor intente nuevamente ";
			}
			
			if(!msg.isEmpty()) {
			
				try {
					
					String body = 
					"<br>Distribución discrepante de pago a financimiento " +
					"<br>Monto Aplicado: " + appliedAmount +
					"<br>caja:"+caid+", compania"+codcomp+", recibo: "+ numrec +
					"<br>cliente: " + codcli+
					"<br>Mensaje ["+msg+"]";
					
					String subject = "Diferencia de saldos pago Financiamiento" ;
					List<String[]> toAddress = new ArrayList<String[]>();
					toAddress.add( new String[] {"chernandez@casapellas.com.ni","carlos hernandez"} );
					SendMailsCtrl.sendSimpleMail(body, toAddress, subject);
					 
					
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
			
			//&& =========== temporalmente 
			if(!msg.isEmpty()) {
				msg ="";
			}
			
		}
		return msg;
				
	}
	
	/**************************************************
	 * Método: GCPMCAJA / com.casapellas.controles /existeDevolucionRecibo
	 * Descrp: Fecha: Oct 3, 2012 Autor: CarlosHernandez
	 ***/
	public Recibo existeDevolucionRecibo(Date dtFecha, int iNumrec, int iCaid,
			String sCodcomp, String sCodsuc, String sTiporec) {
		Recibo rDevolucion = null;
		Session sesion = null;
		try {
			sesion = HibernateUtilPruebaCn.currentSession();
			Transaction trans = sesion.beginTransaction();

			Criteria cr = sesion.createCriteria(Recibo.class);
			cr.add(Restrictions.eq("nodoco", iNumrec));
			cr.add(Restrictions.eq("tipodoco", sTiporec));
			cr.add(Restrictions.eq("fecha", dtFecha));
			cr.add(Restrictions.eq("estado", ""));
			cr.add(Restrictions.eq("id.codsuc", sCodsuc));
			cr.add(Restrictions.eq("id.caid", iCaid));
			cr.add(Restrictions.eq("id.codcomp", sCodcomp));
			cr.setMaxResults(1);

			rDevolucion = (Recibo) cr.uniqueResult();

			trans.commit();

		} catch (Exception e) {
			rDevolucion = null;
			e.printStackTrace();
		} finally {
			try {
				HibernateUtilPruebaCn.closeSession(sesion);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return rDevolucion;
	}

	/*
	 *  ******************** fin de metodo existeDevolucionRecibo
	 * ***************************
	 */

	public boolean actualizarRecibofac(Session sesion, int iCaid,
			String sCodcomp, int iNumfac, String sTipofac, String sTiporec,
			int iNumrec, String sEstado, String sCodunineg, int iCodcli) {
		boolean bHecho = true;

		try {
			Criteria cr = sesion.createCriteria(Recibofac.class);
			cr.add(Restrictions.eq("id.numfac", iNumfac));
			cr.add(Restrictions.eq("id.tipofactura", sTipofac));
			cr.add(Restrictions.eq("id.caid", iCaid));
			cr.add(Restrictions.eq("id.codcomp", sCodcomp));
			cr.add(Restrictions.eq("id.numrec", iNumrec));
			cr.add(Restrictions.eq("id.tiporec", sTiporec));
			cr.add(Restrictions.eq("id.codunineg", sCodunineg));
			cr.add(Restrictions.eq("id.partida", ""));
			cr.add(Restrictions.eq("id.codcli", iCodcli));

			Recibofac rf = (Recibofac) cr.uniqueResult();
			if (rf == null)
				return false;

			rf.setEstado(sEstado);
			sesion.update(rf);

		} catch (Exception e) {
			e.printStackTrace();
			bHecho = false;
		}
		return bHecho;
	}

	public Recibofac getRecibofac(Session sesion, int iCaid, String sCodcomp,
			int iNumfac, String sTipofac, String sCodunineg, int iCodcli) {
		Recibofac rf = null;

		try {

			Criteria cr = sesion.createCriteria(Recibofac.class);
			cr.add(Restrictions.eq("id.numfac", iNumfac));
			cr.add(Restrictions.eq("id.tipofactura", sTipofac));
			cr.add(Restrictions.eq("id.caid", iCaid));
			cr.add(Restrictions.eq("id.codcomp", sCodcomp));
			cr.add(Restrictions.eq("id.partida", ""));
			cr.add(Restrictions.eq("id.codunineg", sCodunineg));
			cr.add(Restrictions.eq("id.codcli", iCodcli));

			rf = (Recibofac) cr.uniqueResult();

		} catch (Exception e) {
			e.printStackTrace();
			rf = null;
		}
		return rf;
	}

	/*******************************************************************************************************************************************/
	public boolean actualizarEstadoRecibo(int iNumrec, int iCaid,
			String sCodsuc, String sCodcomp, String sCodUsera, String sMotivo,
			String sTipoRec) {
		boolean bActualizado = true;
		Session session = null;
		Transaction tx = null;

		try {
			session = HibernateUtilPruebaCn.currentSession();
			tx = session.beginTransaction();
			Query q = session.createQuery("update Recibo as r set r.estado"
					+ " =:sEstado, r.codusera=:sCodUsera, "
					+ "r.motivo=:sMotivo where r.id.numrec=:iNumrec "
					+ "and r.id.caid=:iCaid and r.id.codcomp=:sCodcomp"
					+ " and r.id.codsuc=:sCodsuc and r.id.tiporec=:sTipoRec");
			q.setString("sEstado", "A");
			q.setString("sCodUsera", sCodUsera);
			q.setString("sMotivo", sMotivo);

			q.setInteger("iNumrec", iNumrec);
			q.setInteger("iCaid", iCaid);
			q.setString("sCodcomp", sCodcomp);
			q.setString("sCodsuc", sCodsuc);
			q.setString("sTipoRec", sTipoRec);

			int rowCount = q.executeUpdate();
			if (rowCount == 0) {
				bActualizado = false;
			}
			tx.commit();
		} catch (Exception ex) {
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.actualizarEstadoRecibo: "
							+ ex);
			bActualizado = false;
		} finally {
			try {
				session.close();
			} catch (Exception ex2) {
				ex2.printStackTrace();
			}
			;
		}
		return bActualizado;
	}

	/******* Obtiene La info DETALLE DE RECIBO ************************************************************************************************************************************/
	public Recibodet leerPagoRecibo(int iCaid, String sCodSuc, String sCodComp,
			int iNumrec, String sTipoRec, String pago, String ref1,
			String ref2, String ref3, String ref4, String moneda) {
		Recibodet rd = null;
		Session session = HibernateUtilPruebaCn.currentSession();
		
		String sql = "";
		try {
			sql = "from Recibodet as r where r.id.caid = " + iCaid
					+ " and r.id.codsuc = '" + sCodSuc + "'"
					+ " and r.id.codcomp = '" + sCodComp.trim()
					+ "' and r.id.numrec = " + iNumrec
					+ " and r.id.tiporec = '" + sTipoRec + "' and "
					+ " r.id.moneda = '" + moneda
					+ "' and trim(r.id.refer1) = '" + ref1.trim()
					+ "' and trim(r.id.refer2) = '" + ref2.trim() + "' "
					+ " and trim(r.id.refer3) ='" + ref3.trim()
					+ "' and trim(r.id.refer4) ='" + ref4.trim()
					+ "' and r.id.moneda = '" + moneda + "'";

			
			rd = (Recibodet) session.createQuery(sql).uniqueResult();
			

		} catch (Exception ex) {
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.leerPagoRecibo: "
							+ ex);
		}
		return rd;
	}

	/******************************************************************************************************/
	public boolean actualizarReferenciasRecibo(int iNumrec, int iCajaId,
			String sCodComp, String scodsuc, String tiporec,
			List<MetodosPago> lstMetodosPago) {
		String sql = "";
		boolean hecho = true;
		Session sesion = null;
		try {

			sesion = HibernateUtilPruebaCn.currentSession();
			Transaction trans = sesion.beginTransaction();

			Query q = null;
			int iActualizado = 0;
			for (MetodosPago mp : lstMetodosPago) {
				if (mp.getMetodo().equals(MetodosPagoCtrl.TARJETA)
						&& mp.getVmanual().compareTo("2") == 0) {
					sql = " UPDATE " + PropertiesSystem.ESQUEMA
							+ ".RECIBODET SET ";
					sql += " REFER3 = '" + mp.getReferencia3() + "',";
					sql += " REFER4 = '" + mp.getReferencia4() + "',";
					sql += " REFER5 = '" + mp.getReferencia5() + "',";
					sql += " REFER6 = '" + mp.getReferencia6() + "',";
					sql += " REFER7 = '" + mp.getReferencia7() + "',";
					sql += " NOMBRE = '" + mp.getNombre() + "'";
					sql += " WHERE numrec = " + iNumrec;
					sql += " and caid = " + iCajaId + " and codcomp = '"
							+ sCodComp + "'";
					sql += " and codsuc = '" + scodsuc + "' and tiporec = '"
							+ tiporec + "'";
					sql += " and mpago = '" + mp.getMetodo() + "'";
					sql += " and moneda = '" + mp.getMoneda() + "'";
					sql += " and monto = " + mp.getMontopos();

					q = sesion.createSQLQuery(sql);
					iActualizado = q.executeUpdate();
					if (iActualizado != 1) {
						error = new Exception(
								"@Error de actualizacion de parametros de recibo con pago de Socket Pos");
						hecho = false;
						break;
					}
				}
			}
			if (hecho)
				trans.commit();
			else
				trans.rollback();

		} catch (Exception ex) {
			hecho = false;
			error = new Exception(
					"@LOGCAJA: error de sistema al grabar las referencias del recibo");
			errorDetalle = ex;
			System.out
					.print(" error de sistema al grabar las referencias del recibo "
							+ ex);
		} finally {
			try {
				sesion.close();
			} catch (Exception ex2) {
				ex2.printStackTrace();
			}
			;
		}
		return hecho;
	}

	/******************************************************************************************************/
	@SuppressWarnings({ "unchecked", "static-access" })
	public Recibo getReciboDevolucionxRecibo(int caid, int numrec,
			String codcomp, String codsuc, String tiporec, Date dtfecha) {
		Session s = null;
		String sql = "";
		Recibo r = null;

		try {

			sql = "from Recibo r where r.id.caid = "
					+ caid
					+ " and r.estado = '' "
					+ " and r.nodoco = "
					+ numrec
					+ " and r.id.codcomp = '"
					+ codcomp
					+ "'"
					+ " and r.id.tiporec = '"
					+ tiporec
					+ "'"
					+ " and r.fecha = '"
					+ new FechasUtil()
							.formatDatetoString(dtfecha, "yyyy-MM-dd") + "'";

			s = HibernateUtilPruebaCn.currentSession();

			LogCajaService.CreateLog("getReciboDevolucionxRecibo", "QRY", sql);
			
			List<Recibo> lista = s.createQuery(sql).setMaxResults(1).list();
			if (lista != null && !lista.isEmpty())
				r = lista.get(0);

		} catch (Exception ex) {
			LogCajaService.CreateLog("getReciboDevolucionxRecibo", "ERR", ex.getMessage());
		}

		return r;
	}

	@SuppressWarnings("unchecked")
	public static List<Recibo> getDevolucionPorRecibo(int caid, int numrec,
			String codcomp, Date dtfecha, String strMoneda) {
		Session sesion = null;
		
		
		List<Recibo> recibos = null;

		try {

			sesion = HibernateUtilPruebaCn.currentSession();
			
			String sql = "from Recibo r where r.id.caid = " + caid
					+ " and r.estado = '' and r.nodoco = " + numrec
					+ " and r.id.codcomp = '" + codcomp
					+ "' and r.id.tiporec = 'DCO'" + " and r.fecha = '"
					+ FechasUtil.formatDatetoString(dtfecha, "yyyy-MM-dd")
					+ "'";

			LogCajaService.CreateLog("getDevolucionPorRecibo", "QRY", sql);
			recibos = (ArrayList<Recibo>) sesion.createQuery(sql).list();
			
			//Ajustes hecho por LFonseca 2019-08-27
			//Las devoluciones tiene que se en moneda distinta al del arqueo
			//para ser tomado en cuenta, por eso se agrego esta validación,
			//dado a que solo se filtraba el recibo de devolución, mas no
			//verificaba si la moneda era igual a la moneda del arqueo,
			//solo aplica para arqueo
			if (recibos != null && !recibos.isEmpty()){
				
				for (Recibo r1 : recibos) {
					if(strMoneda!=null)
					{
						String strSqlDetalle = "from Recibodet r where r.id.caid = " + caid 
								+ " and r.id.codcomp = '" + codcomp + "' and r.id.numrec = " + r1.getId().getNumrec() 
								+ " and r.id.tiporec = 'DCO' and r.id.moneda <> '" + strMoneda + "'";
						
						LogCajaService.CreateLog("getDevolucionPorRecibo", "QRY", strSqlDetalle);
						List<Recibodet> recibosDet = (ArrayList<Recibodet>) sesion.createQuery(strSqlDetalle).list();
						
						if (recibosDet.size()==0){
							 sql = "from Recibo r where r.id.caid = " + caid
									+ " and r.estado = '' and r.nodoco = " + numrec
									+ " and r.id.codcomp = '" + codcomp
									+ "' and r.id.tiporec = 'DCO'" + " and r.fecha = '"
									+ FechasUtil.formatDatetoString(dtfecha, "yyyy-MM-dd")
									+ "' and 1 = 2";

							 LogCajaService.CreateLog("getDevolucionPorRecibo", "QRY", sql);
							recibos = (ArrayList<Recibo>) sesion.createQuery(sql).list();
						}
					}
				}
			}
		} catch (Exception e) { 
			LogCajaService.CreateLog("getDevolucionPorRecibo", "ERR", e.getMessage());
		} 
		return recibos;
	}

	/******************************************************************************************************/
	public boolean aplicaVariasFacturas(int caid, int numrec, String codcomp,
			String codsuc, String tiporec) {
		boolean existe = false;
		PreparedStatement ps = null;
		String sql = "";
		Connection cn = null;
		int cant = 0;
		Session sesion = null;		
		boolean bNuevaSesionCaja = false;

		try {
			sql = "select count(*) CANT from " + PropertiesSystem.ESQUEMA
					+ ".recibofac where " + "caid = " + caid + " and numrec = "
					+ numrec + " and codcomp = '" + codcomp
					+ "' and codsuc = '" + codsuc + "' and tiporec = '"
					+ tiporec + "'";

			sesion = HibernateUtilPruebaCn.currentSession();			
			
			
			cant = (Integer) sesion.createSQLQuery(sql).uniqueResult();
			if (cant > 1)
				existe = true;

		} catch (Exception ex) {
			System.out
					.println("Se capturo una excepcion en aplicaVariasFacturas: "
							+ ex);
		} 
		return existe;
	}

	/******************************************************************************************************/
	public String leerTipodocRecibo(int nobatch) {
		String tipodoc = "";
		PreparedStatement ps = null;
		String sql = " select cast(rpdct as varchar(2) CCSID 37) RPDCT from "
				+ PropertiesSystem.JDEDTA + ".f0311 where rpicu =  " + nobatch
				+ " and rpdctm = 'RC'";
		Connection cn = null;
		try {
			cn = As400Connection.getJNDIConnection("DSMCAJAR");

			ps = cn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				tipodoc = rs.getString("RPDCT");
			}
		} catch (Exception ex) {
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.leerTipodocRecibo: "
							+ ex);
		} finally {
			try {
				ps.close();
				cn.close();
			} catch (Exception se2) {
				System.out
						.println("ERROR: Failed to close connection en ReciboCtrl.leerEstadoDetalleAsiento: "
								+ se2);
			}
		}
		return tipodoc;
	}

	/******************************************************************************************************/
	public boolean editarReintegro(Session s, Transaction tx, Reintegro r) {
		boolean hecho = false;
		try {
			s.update(r);
			hecho = true;
		} catch (Exception ex) {
			error = new Exception(
					"@LOGCAJA: error de sistema al editar el reintegro");
			errorDetalle = ex;
			System.out.print("Se grabarReintegro: " + ex);
		}
		return hecho;
	}

	/******************************************************************************************************/
	public Reintegro verificarSiTieneReintegro(int noarqueo, int caid,
			String codcomp, String codsuc, String sMoneda) {
		Session s = null;
		Transaction tx = null;
		String sql = "";
		Reintegro r = null;
		try {
			sql = " from Reintegro as r where r.id.caid = " + caid
					+ " and r.id.codcomp = '" + codcomp
					+ "' and r.id.codusc = " + "'" + codsuc
					+ "' and r.narqueo = " + noarqueo + " and r.moneda = '"
					+ sMoneda + "'";
			s = HibernateUtilPruebaCn.currentSession();
			tx = s.beginTransaction();

			r = (Reintegro) s.createQuery(sql).uniqueResult();
			tx.commit();
		} catch (Exception ex) {
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.verificarSiTieneReintegro: "
							+ ex);
		} finally {
			try {
				HibernateUtilPruebaCn.closeSession(s);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return r;
	}

	/******************************************************************************************************/
	public boolean grabarReintegro(Session s, int caid, String codcomp,
			String codsuc, int noreint, double monto, int nobatch, int nodoc,
			String usrmod, int noarqueo, String moneda, Date fechaArqueo) {
		boolean hecho = true;
		
		try {
			Reintegro r = new Reintegro();
			ReintegroId rid = new ReintegroId();

			rid.setCaid(caid);
			rid.setCodcomp(codcomp);
			rid.setCodusc(codsuc);
			rid.setNoreint(noreint);

			r.setId(rid);

			r.setEstado(false);
			r.setFecha( fechaArqueo );
			r.setFechareint( fechaArqueo );
			r.setMonto(new BigDecimal(Double.toString( monto ) ));
			r.setNobatch(nobatch);
			r.setNodoc(nodoc);
			r.setUsrmod(usrmod);
			r.setNarqueo(noarqueo);
			r.setMoneda(moneda);

			s.save(r);
			
			
		} catch (Exception ex) {
			hecho = false;
			ex.printStackTrace(); 
		}
		return hecho;
	}

	/******************************************************************************************/
	/**
	 * Método: Obtener el periodo fiscal de un asiento de diario. Fecha:
	 * 12/10/2011 Nombre: Carlos Manuel Hernández Morrison.
	 */
	@SuppressWarnings("unchecked")
	public int[] obtenerPeriodoMesFiscalBatch(int iNobatch, int iNodocumento) {
		int iAnioMesPerFiscal[] = null;
		List<Object[]> lstResulF0911 = null;
		String sql = "";
		Session sesionCajaR = null;

		try {
			sql = " SELECT ";
			sql += " CAST(GLFY AS NUMERIC (2) ),";
			sql += " CAST(GLPN AS NUMERIC (2) )";
			sql += " FROM ALTDTA.F0911 F09";
			sql += " WHERE F09.GLICU = " + iNobatch + " AND F09.GLDOC = "
					+ iNodocumento;

			sesionCajaR = HibernateUtilPruebaCn.currentSession();
			Transaction trans = sesionCajaR.beginTransaction();

			lstResulF0911 = sesionCajaR.createSQLQuery(sql).list();
			if (lstResulF0911 == null || lstResulF0911.size() == 0) {
				lstResulF0911 = null;
				error = new Exception(
						"@F0911: No se ha podido obtener datos de anio y mes fiscal para batch # "
								+ iNobatch);
				errorDetalle = error;
			} else {
				iAnioMesPerFiscal = new int[2];
				iAnioMesPerFiscal[0] = Integer.parseInt(String
						.valueOf(lstResulF0911.get(0)[0]));
				iAnioMesPerFiscal[1] = Integer.parseInt(String
						.valueOf(lstResulF0911.get(0)[1]));
			}
			trans.commit();

		} catch (Exception e) {
			iAnioMesPerFiscal = null;
			errorDetalle = e;
			error = new Exception(
					"@F0911: Error de sistema al intentar obtener año y mes fiscal del batch #"
							+ iNobatch);
			System.out
					.println(":ReciboCtrl():  Excepción capturada en obtenerPeriodoMesFiscalBatch(): "
							+ e);
		} finally {
			try {
				HibernateUtilPruebaCn.closeSession(sesionCajaR);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return iAnioMesPerFiscal;
	}

	/******************************************************************************************************/
	/**
	 * Método: Actualizar el valor para la referencia del depósito
	 * transferencias y depositos( minuta de dep). Fecha: 02/12/2011 Nombre:
	 * Carlos Manuel Hernández Morrison.
	 **/
	public boolean actualizarReferencia8N(VrecibosxtipompagoId id,
			String sReferVieja) {
		boolean bHecho = true;
		Session sesion = null;
		try {
			sesion = HibernateUtilPruebaCn.currentSession();
			Transaction trans = sesion.beginTransaction();

			String sql = " UPDATE " + PropertiesSystem.ESQUEMA
					+ ".RECIBODET r \n";

			if (id.getMpago().compareTo(MetodosPagoCtrl.TRANSFERENCIA) == 0)
				sql += " SET r.refer3 = '" + id.getRefer2() + "' \n";
			else
				sql += " SET r.refer2 = '" + id.getRefer2() + "' \n";

			sql += " where r.caid = " + id.getCaid() + " and r.numrec = "
					+ id.getNumrec();
			sql += " and r.codcomp ='" + id.getCodcomp() + "' and r.codsuc = '"
					+ id.getCodsuc() + "'";
			sql += " and r.codsuc = '" + id.getCodsuc() + "' and r.tiporec = '"
					+ id.getTiporec() + "'";
			sql += " and r.refer4 = '" + id.getRefer4() + "' and r.refer1 = '"
					+ id.getRefer1() + "'";
			sql += " and r.mpago = '" + id.getMpago() + "' and r.moneda = '"
					+ id.getMoneda() + "'";

			if (id.getMpago().compareTo(MetodosPagoCtrl.TRANSFERENCIA) == 0)
				sql += " and r.refer3 = '" + sReferVieja + "' \n";
			else
				sql += " and r.refer2 = '" + sReferVieja + "' \n";

			Query q = sesion.createSQLQuery(sql);
			int iActualizado = q.executeUpdate();

			switch (iActualizado) {
			case 0:
				error = new Exception(
						"@RECIBODET: No hay registros afectados en la actualización");
				bHecho = false;
				break;
			case 1:
				bHecho = true;
				break;
			default:
				error = new Exception("@RECIBODET: No se realiza operación:  "
						+ iActualizado + " actualizaciones involucradas ");
				bHecho = false;
				break;
			}
			trans.commit();

		} catch (Exception e) {
			bHecho = false;
			error = new Exception(
					"@RECIBODET: Error de aplicacion, no se pudo actualizar el registro ");
			errorDetalle = e;
			System.out
					.println("RECIBODET: Excepción capturada en: actualizarReferencia8N "
							+ e);
		} finally {
			try {
				sesion.close();
			} catch (Exception ex2) {
				ex2.printStackTrace();
			}
			;
		}
		return bHecho;
	}

	/******************************************************************************************************************************/
	public boolean verificarSitieneLiga(int iNobatch, int iNodoc, int iCodcli,
			String sLigaDoc) {
		boolean tieneLiga = false;
		Connection cn = null;
		PreparedStatement ps = null;
		String sql = "";
		int iNodocumento = 0;
		try {
			cn = As400Connection.getJNDIConnection("DSMCAJAR");

			sql = "select rpdoc from " + PropertiesSystem.JDEDTA
					+ ".f0311 where rpan8 = " + iCodcli + " and rpdocm = "
					+ iNodoc + " and rpicu = " + iNobatch + " and rpdct = 'RU'";

			ps = cn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				iNodocumento = rs.getInt("rpdoc");
			}
			if (iNodocumento > 0) {
				sql = "select rpdoc from " + PropertiesSystem.JDEDTA
						+ ".f0311 where rpan8 = " + iCodcli + " and rpdoc = "
						+ iNodocumento + " and rpdct = 'RU' and  rpdctm = '"
						+ sLigaDoc + "'";
				ps = cn.prepareStatement(sql);
				rs = ps.executeQuery();
				if (rs.next()) {
					tieneLiga = true;
				}
			}
		} catch (Exception ex) {
			tieneLiga = true;
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.verificarSitieneLiga: "
							+ ex);
		} finally {
			try {
				ps.close();
				cn.close();
			} catch (Exception se2) {
				System.out
						.println("ERROR: Failed to close connection en ReciboCtrl.verificarSitieneLiga: "
								+ se2);
			}
		}
		return tieneLiga;
	}

	/*********************************************************************************************************/
	/** Obtener los afiliados que tuvieron pagos con socket pos */
	@SuppressWarnings("unchecked")
	public List<String> getTransAfiliadosxDia(int iCaid, String sCodcomp,
			Date Fecha, String sMoneda, Date dtHini, Session sesion) {
		List<String> lstAfiliados = null;

		try {

			// && ========= Recibos del dia para la caja.
			Criteria cr = sesion.createCriteria(Recibo.class).setProjection(
					Projections.distinct(Projections.property("id.numrec")));

			cr.add(Restrictions.eq("id.caid", iCaid))
					.add(Restrictions.eq("id.codcomp", sCodcomp))
					.add(Restrictions.eq("fecha", Fecha))
					.add(Restrictions.eq("estado", ""));

			if (dtHini != null) {
				String dia = new SimpleDateFormat("dd-MM-yyyy").format(Fecha);
				String ini = new SimpleDateFormat("HH:mm:ss").format(dtHini);
				String fin = new SimpleDateFormat("HH:mm:ss").format(Fecha);
				Date dtInicio = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
						.parse(dia + " " + ini);
				Date dtFinal = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
						.parse(dia + " " + fin);
				cr.add(Restrictions.between("hora", dtInicio, dtFinal));
			}
			List<Integer> lstRecibos = cr.list();
			if (lstRecibos == null || lstRecibos.isEmpty())
				return null;

			cr = sesion
					.createCriteria(Recibodet.class)
					.setProjection(
							Projections.distinct(Projections
									.property("id.refer1")))
					.add(Restrictions.eq("id.caid", iCaid))
					.add(Restrictions.eq("id.codcomp", sCodcomp))
					.add(Restrictions.eq("id.moneda", sMoneda))
					.add(Restrictions.eq("id.mpago", MetodosPagoCtrl.TARJETA))
					.add(Restrictions.eq("vmanual", "2"))
					.add(Restrictions.in("id.numrec", lstRecibos));
			lstAfiliados = cr.list();

		} catch (Exception e) { 
			e.printStackTrace();
		}
		return lstAfiliados;
	}

	/*********************************************************************************************************/
	public List<String> getTransAfiliadosxDia(int iCaid, String sCodcomp,
			Date Fecha, String sMoneda, Date dtHini) {
		List<String> lstResult = new ArrayList<String>();
		String sql = "", sFecha = "", sHini = "", sHfin = "";
		FechasUtil f = new FechasUtil();
		Connection cn = null;
		PreparedStatement ps = null;
		try {
			cn = As400Connection.getJNDIConnection("DSMCAJAR");

			sFecha = f.formatDatetoString(Fecha, "yyyy-MM-dd");
			sql = "select distinct refer1 from "
					+ PropertiesSystem.ESQUEMA
					+ ".recibo r inner join "
					+ ""
					+ PropertiesSystem.ESQUEMA
					+ ".recibodet rd on (r.tiporec = rd.tiporec and "
					+ "r.codsuc = rd.codsuc and r.caid = rd.caid  and r.codcomp"
					+ " = rd.codcomp and r.numrec = rd.numrec ) where r.caid = "
					+ iCaid + " and r.codcomp = '" + sCodcomp
					+ "' and rd.mpago = '"+MetodosPagoCtrl.TARJETA+"'"
					+ " and rd.vmanual = 2 and r.fecha = '" + sFecha + "' "
					+ "and rd.moneda = '" + sMoneda + "'";

			if (dtHini != null) {
				sHini = f.formatDatetoString(dtHini, "HH.mm.ss");
				sHfin = f.formatDatetoString(Fecha, "HH.mm.ss");
				sql = sql + " and r.hora >= '" + sHini + "' and r.hora <= '"
						+ sHfin + "'";
			}

			ps = cn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				lstResult.add(rs.getString("refer1"));
			}
		} catch (Exception ex) {
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.getTransAfiliadosxDia: "
							+ ex);
		} finally {
			try {
				ps.close();
				cn.close();
			} catch (Exception se2) {
				System.out
						.println("ERROR: Failed to close connection en ReciboCtrl.getTransAfiliadosxDia: "
								+ se2);
			}
		}
		return lstResult;
	}

	/*********************************************************************************************************/
	public boolean validarSiExisteRM(int iNodoc, String sCodsuc, String sTipodoc) {
		boolean existe = false;
		Connection cn = null;
		PreparedStatement ps = null;
		String sql = "";
		try {
			sql = "select * from " + PropertiesSystem.JDEDTA
					+ ".f0311 where rpdoc = " + iNodoc + " and rpdct = '"
					+ sTipodoc + "' and rpkco = '" + sCodsuc + "'";
			cn = As400Connection.getJNDIConnection("DSMCAJAR");
			ps = cn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				existe = true;
		} catch (Exception ex) {
			existe = false;
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.validarSiExisteRM: "
							+ ex);
		} finally {
			try {
				cn.close();
			} catch (Exception ex2) {
				ex2.printStackTrace();
			}
			;
		}
		return existe;
	}

	/***** OBTIENE EL ESTADO DEL DETALLE DE UN RECIBO EN EL JDE ******************************************************************************************************************/
	public String leerEstadoDetalleAsiento(int iNoBatch) {
		String sEstado = null;
		PreparedStatement ps = null;
		String sql = "SELECT DISTINCT CAST(GLPOST AS VARCHAR (1) CCSID 37) AS GLPOST FROM "
				+ PropertiesSystem.JDEDTA
				+ ".F0911 WHERE GLICU ="
				+ iNoBatch
				+ " AND GLICUT = 'G'";
		Connection cn = null;
		try {
			cn = As400Connection.getJNDIConnection("DSMCAJAR");
			ps = cn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				sEstado = rs.getString("GLPOST");
			}
		} catch (Exception ex) {
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.leerEstadoDetalleAsiento: "
							+ ex);
		} finally {
			try {
				ps.close();
				cn.close();
			} catch (Exception se2) {
				System.out
						.println("ERROR: Failed to close connection en ReciboCtrl.leerEstadoDetalleAsiento: "
								+ se2);
			}
		}
		return sEstado;
	}

	/******************************************************************************************************/
	/***** OBTIENE EL ESTADO DEL DETALLE DE UN RECIBO EN EL JDE ******************************************************************************************************************/
	public String leerEstadoDetalleRec(int iNoBatch) {
		String sEstado = null;
		PreparedStatement ps = null;
		String sql = "SELECT DISTINCT CAST(RPPOST AS VARCHAR (1) CCSID 37) AS RPPOST FROM "
				+ PropertiesSystem.JDEDTA + ".F0311 WHERE RPICU =" + iNoBatch;
		Connection cn = null;
		try {
			cn = As400Connection.getJNDIConnection("DSMCAJAR");

			ps = cn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				sEstado = rs.getString("RPPOST");
			}
		} catch (Exception ex) {
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.leerEstadoDetalleRec: "
							+ ex);
		} finally {
			try {
				ps.close();
				cn.close();
			} catch (Exception se2) {
				System.out
						.println("ERROR: Failed to close connection en ReciboCtrl.leerEstadoDetalleRec: "
								+ se2);
			}
		}
		return sEstado;
	}

	/******************************************************************************************************/
	/******************************************************************************************************/
	public boolean verificarReciboArqueo(int caid, String tiporec, int iNumrec,
			String sCodsuc, String sCodcomp, String sTipoDoc, Date dtFecha,
			String sMoneda) {
		boolean bExiste = true;
		Session s = null;
		Transaction t = null;
		Arqueorec ar = null;
		try {

			String sql = " select ar.* from " + PropertiesSystem.ESQUEMA
					+ ".arqueo a inner join " + PropertiesSystem.ESQUEMA
					+ ".arqueorec ar";
			sql += " on a.caid = ar.caid and a.codcomp = ar.codcomp and a.codsuc = ar.codsuc and a.noarqueo = ar.noarqueo";
			sql += " where ar.caid = "
					+ caid
					+ " and a.fecha = '"
					+ new FechasUtil()
							.formatDatetoString(dtFecha, "yyyy-MM-dd") + "'";
			sql += " and trim(ar.codcomp) = '" + sCodcomp.trim()
					+ "' and ar.numrec = " + iNumrec + " and ar.codsuc = '"
					+ sCodsuc + "'";
			sql += " and ar.tiporec = '" + tiporec + "' and ar.tipodoc = '"
					+ sTipoDoc + "'";
			sql += " and a.estado <>'R' and a.moneda = '" + sMoneda + "'";

			s = HibernateUtilPruebaCn.currentSession();
			t = s.beginTransaction();
			ar = (Arqueorec) s.createSQLQuery(sql).addEntity(Arqueorec.class)
					.uniqueResult();

			if (ar == null) {
				bExiste = false;
			}
			t.commit();

		} catch (Exception ex) {
			bExiste = true;
			ex.printStackTrace();
		} finally {
			try {
				HibernateUtilPruebaCn.closeSession(s);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return bExiste;
	}

	/******************************************************************************************************/
	/**
	 * Método: actualizar un registro de recibodet . Fecha: 24/09/2010 Nombre:
	 * Carlos Manuel Hernández Morrison.
	 **/
	public boolean actualizarRecibodet(Recibodet rd, String sRfrNueva) {
		boolean bHecho = true;
		Session sesion = null;
		Transaction trans = null;
		String sql = "";
		RecibodetId rdId = new RecibodetId();

		try {
			rdId = rd.getId();

			sql = " UPDATE " + PropertiesSystem.ESQUEMA + ".RECIBODET r \n";
			sql += " SET r.refer1 = '" + sRfrNueva + "' \n";
			sql += " where r.caid = " + rdId.getCaid() + " and r.numrec = "
					+ rdId.getNumrec();
			sql += " and r.codcomp ='" + rdId.getCodcomp()
					+ "' and r.codsuc = '" + rdId.getCodsuc() + "'";
			sql += " and r.codsuc = '" + rdId.getCodsuc()
					+ "' and r.tiporec = '" + rdId.getTiporec() + "'";
			sql += " and r.refer4 = '" + rdId.getRefer4()
					+ "' and r.refer3 = '" + rdId.getRefer3() + "'";
			sql += " and r.refer2 = '" + rdId.getRefer2()
					+ "' and r.refer1 = '" + rdId.getRefer1() + "'";
			sql += " and r.mpago = '" + rdId.getMpago() + "' and r.moneda = '"
					+ rdId.getMoneda() + "'";

			sesion = HibernateUtilPruebaCn.currentSession();
			trans = sesion.beginTransaction();
			Query q = sesion.createSQLQuery(sql);
			int iActualizado = q.executeUpdate();
			trans.commit();
			sesion.close();

			if (iActualizado != 1) {
				bHecho = false;
			}
		} catch (Exception error) {
			bHecho = false;
			error.printStackTrace();
		}
		return bHecho;
	}

	/*****
	 * realizar busqueda de recibos pagados con tarjetas para reporte de
	 * retencion de IR
	 *****/
	public List<ReporteIR> buscarRecibosPagadosTC(String caid, String sMoneda,
			Date Fechaini, Date FechaFin) {
		List<ReporteIR> lstResult = new ArrayList<ReporteIR>();
		List lstR = new ArrayList();
		Session s = HibernateUtilPruebaCn.currentSession();
		Transaction tx = null;
		String sql = "from Vretencion as v where v.id.fecha >= :pFechai and v.id.fecha <= :pFechaf and v.id.moneda = :pMoneda ";
		ReporteIR ir = null;
		Vretencion vret = null;
		double dmontoComision = 0;
		Divisas d = new Divisas();
		TasaCambioCtrl tc = new TasaCambioCtrl();
		try {
			if (!caid.equals("SCA")) {
				sql = sql + " and v.id.caid = " + caid;
			}
			sql = sql + " order by v.id.codcomp, v.id.codunineg";
			tx = s.beginTransaction();
			lstR = s.createQuery(sql).setDate("pFechai", Fechaini)
					.setDate("pFechaf", FechaFin).setString("pMoneda", sMoneda)
					.list();
			tx.commit();

			for (int i = 0; i < lstR.size(); i++) {
				vret = (Vretencion) lstR.get(i);
				ir = new ReporteIR();
				ir.setNo(i + 1);
				ir.setCaid(vret.getId().getCaid());
				ir.setCodcomp(vret.getId().getCodcomp());
				ir.setCodunineg(vret.getId().getCodunineg().trim());
				ir.setUnineg(vret.getId().getUnineg());
				ir.setCodcomp(vret.getId().getCodcomp());
				ir.setNomcomp(vret.getId().getNomcomp());
				ir.setTasa(vret.getId().getTasa().doubleValue());

				if (vret.getId().getTiporec().equals("CO")) {

					ir.setReferencia(vret.getId().getNofactura().intValue());
					ir.setTipodocumento(vret.getId().getTipofactura().trim());
					if (vret.getId().getIva().doubleValue() == 0) {
						ir.setExenta(0);
						ir.setExonerada(vret.getId().getTotal().doubleValue());
						ir.setVenta(0);
						ir.setIva(0);
						ir.setTotal(0);
					} else {
						ir.setExenta(0);
						ir.setExonerada(0);
						ir.setVenta(vret.getId().getSubtotal().doubleValue());
						ir.setIva(vret.getId().getIva().doubleValue());
						ir.setTotal(vret.getId().getTotal().doubleValue());
					}
					ir.setIngresoAbonoPrima(0);

				} else {
					ir.setTipodocumento(vret.getId().getTiporec());
					ir.setReferencia(vret.getId().getNumrec());
					ir.setVenta(0);
					ir.setIva(0);
					ir.setTotal(0);
					ir.setExenta(0);
					ir.setExonerada(0);

					ir.setIngresoAbonoPrima(vret.getId().getMontotc()
							.doubleValue());

				}
				ir.setIngresoSujetoRetencion(ir.getTotal() + ir.getExenta()
						+ ir.getExonerada() + ir.getIngresoAbonoPrima());

				dmontoComision = d.roundDouble((ir.getVenta() + ir.getExenta()
						+ ir.getExonerada() + ir.getIngresoAbonoPrima())
						* (vret.getId().getComision().divide(new BigDecimal(
								"100"))).doubleValue());
				ir.setMontoComisionVenta(dmontoComision);

				dmontoComision = d.roundDouble(ir.getIva()
						* (vret.getId().getComision().divide(new BigDecimal(
								"100"))).doubleValue());
				ir.setMontoComisionIVA(dmontoComision);

				if (vret.getId().getMoneda().equals("COR")) {

					ir.setRetencionAnticipoIR((d.roundDouble(ir
							.getIngresoSujetoRetencion() - ir.getIva()) * 0.01));

					ir.setRetencionIVA(d.roundDouble(ir.getIva() * 0.01));

					ir.setTotalRetencion(d.roundDouble(ir
							.getRetencionAnticipoIR() + ir.getRetencionIVA()));
				} else {
					tc.obtenerTasaJDExFecha("COR", "USD", vret.getId()
							.getFecha());

					ir.setRetencionAnticipoIR(d.roundDouble(((ir
							.getIngresoSujetoRetencion() - ir.getIva()) * 0.01)
							* vret.getId().getTasa().doubleValue()));

					ir.setRetencionIVA(d.roundDouble((ir.getIva() * 0.01)
							* vret.getId().getTasa().doubleValue()));

					ir.setTotalRetencion(d.roundDouble((ir
							.getRetencionAnticipoIR() + ir.getRetencionIVA())
							* vret.getId().getTasa().doubleValue()));
				}

				lstResult.add(ir);
			}
		} catch (Exception ex) {
			System.out
					.println("Se capturo excepcion en ReciboCtrl.buscarRecibosPagadosTC: "
							+ ex);
		} finally {
			try {
				HibernateUtilPruebaCn.closeSession(s);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return lstResult;
	}

	/********************************************************************************************************/
	public boolean actualizarEnlaceReciboFac(int iNumfac, String sTipofactura,
			String sCodcomp, int iNumrec, int iCaid) {
		String sql = "update " + PropertiesSystem.ESQUEMA
				+ ".recibofac set estado = 'A' where numrec = " + iNumrec
				+ " and caid = " + iCaid + " and tipofactura = '"
				+ sTipofactura + "' and trim(codcomp) = '" + sCodcomp.trim()
				+ "' and numfac = " + iNumfac;
		PreparedStatement ps = null;
		boolean bBorrado = true;
		Connection cn = null;
		try {
			cn = As400Connection.getJNDIConnection("DSMCAJA2");
			ps = cn.prepareStatement(sql);
			int rs = ps.executeUpdate();
			if (rs == 0) {
				bBorrado = false;
			}
			cn.commit();

		} catch (Exception ex) {
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.borrarEnlaceReciboFac: "
							+ ex);
			bBorrado = false;
		} finally {
			try {
				cn.close();
			} catch (Exception ex2) {
				ex2.printStackTrace();
			}
			;
		}
		return bBorrado;
	}

	/*********************************************************************************************************/
	public List getRecibosxParametros(int iCaid, String sCodComp,
			String sTipoRecibo, String sParametro, int iTipousqueda,
			Date dDesde, Date dHasta, String sEstado, boolean manuales) {
		List lstRecibos = null;
		String sql = "from Recibo as r where r.id.tiporec <> 'FCV' ";
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = null;
		Vrecfac vrecfac = null;
		Recibo recibo = null;
		ReciboId reciboId = null;
		int[] iNobatch = null;
		String sNobatch = "";
		try {
			switch (iTipousqueda) {
			case (1):// busqueda por nombre de cliente
				if (!sParametro.equals("")) {
					sql = sql + " and r.cliente like '%"
							+ sParametro.toUpperCase().trim() + "%'";
				}
				break;
			case (2):// busqueda por codigo de cliente
				if (!sParametro.equals("")) {
					sql = sql + " and cast(r.codcli as string) like '"
							+ sParametro.trim() + "%'";
				}
				break;
			case (3):// busqueda por numero de recibo
				if (!sParametro.equals("")) {
					sql = sql + " and cast(r.id.numrec as string) like '"
							+ sParametro.trim() + "%'";
				}
				break;
			case (4):// busqueda por numero de factura
				sql = "from Vrecfac as r where r.id.estado = '" + sEstado
						+ "' ";
				if (!sParametro.equals("")) {
					sql = sql + " and cast(r.id.numfac as string) like '"
							+ sParametro.trim() + "%'";
				}
				break;
			}
			// codigo de caja
			if (iCaid > 0) {
				sql = sql + " and r.id.caid = " + iCaid + "";
			}
			// codigo de compania
			if (!sCodComp.equals("01")) {
				sql = sql + " and r.id.codcomp = '" + sCodComp + "'";
			}
			// tipo de recibo
			if (!sTipoRecibo.equals("01")) {
				sql = sql + " and r.id.tiporec = '" + sTipoRecibo + "'";
			}
			// manuales
			if (manuales) {
				sql = sql + " and numrecm > 0 ";
			}
			// estado
			if (iTipousqueda < 4) {
				sql = sql + " and r.estado = '" + sEstado + "'";
				// fechas
				if (dDesde != null) {
					sql = sql + " and r.fecha >= :pDesde";
				}
				if (dHasta != null) {
					sql = sql + " and r.fecha <= :pHasta";
				}
			} else {
				// fechas
				if (dDesde != null) {
					sql = sql + " and r.id.fecha >= :pDesde";
				}
				if (dHasta != null) {
					sql = sql + " and r.id.fecha <= :pHasta";
				}
			}

			tx = session.beginTransaction();
			Query q = session.createQuery(sql);
			if (dDesde != null) {
				q.setDate("pDesde", dDesde);
			}
			if (dHasta != null) {
				q.setDate("pHasta", dHasta);
			}

			lstRecibos = q.list();
			tx.commit();
			if (iTipousqueda == 4) {
				for (int i = 0; i < lstRecibos.size(); i++) {
					vrecfac = (Vrecfac) lstRecibos.get(i);
					recibo = new Recibo();
					reciboId = new ReciboId();

					reciboId.setCaid(vrecfac.getId().getCaid());
					reciboId.setNumrec(vrecfac.getId().getNumrec());
					reciboId.setCodcomp(vrecfac.getId().getCodcomp());
					reciboId.setCodsuc(vrecfac.getId().getCodsuc());
					reciboId.setTiporec(vrecfac.getId().getTiporec());

					recibo.setId(reciboId);

					recibo.setMontoapl(vrecfac.getId().getMontoapl());
					recibo.setMontorec(vrecfac.getId().getMontorec());
					recibo.setConcepto(vrecfac.getId().getConcepto());
					recibo.setFecha(vrecfac.getId().getFecha());
					recibo.setCliente(vrecfac.getId().getCliente());
					recibo.setCodcli(vrecfac.getId().getCodcli());
					recibo.setCajero(vrecfac.getId().getCajero());
					recibo.setHora(vrecfac.getId().getHora());
					recibo.setNumrecm(vrecfac.getId().getNumrecm());
					recibo.setRecjde(vrecfac.getId().getRecjde());
					recibo.setEstado(vrecfac.getId().getEstado());
					recibo.setMotivo(vrecfac.getId().getMotivo());
					recibo.setCodusera(vrecfac.getId().getCodusera());
					recibo.setHoramod(vrecfac.getId().getHoramod());
					recibo.setCoduser(vrecfac.getId().getCoduser());

					recibo.setCaid(vrecfac.getId().getCaid());
					recibo.setNumrec(vrecfac.getId().getNumrec());
					recibo.setCodcomp(vrecfac.getId().getCodcomp());
					recibo.setCodsuc(vrecfac.getId().getCodsuc());
					recibo.setTiporec(vrecfac.getId().getTiporec());

					/*
					 * iNobatch =
					 * obtenerBatchxRecibo2(recibo.getId().getNumrec()
					 * ,recibo.getId
					 * ().getCaid(),recibo.getId().getCodsuc(),recibo
					 * .getId().getCodcomp(),recibo.getId().getTiporec());
					 * sNobatch = ""; for(int j = 0; j < iNobatch.length; j++){
					 * sNobatch = sNobatch +" " +iNobatch[j]; }
					 */
					recibo.setNobatch(sNobatch);

					lstRecibos.set(i, recibo);
				}
			} else {
				for (int i = 0; i < lstRecibos.size(); i++) {
					recibo = new Recibo();
					recibo = (Recibo) lstRecibos.get(i);
					// iNobatch =
					// obtenerBatchxRecibo2(recibo.getId().getNumrec(),recibo.getId().getCaid(),recibo.getId().getCodsuc(),recibo.getId().getCodcomp(),recibo.getId().getTiporec());
					recibo.setCaid(recibo.getId().getCaid());
					recibo.setNumrec(recibo.getId().getNumrec());
					recibo.setCodcomp(recibo.getId().getCodcomp());
					recibo.setCodsuc(recibo.getId().getCodsuc());
					recibo.setTiporec(recibo.getId().getTiporec());

					lstRecibos.set(i, recibo);
				}
			}
		} catch (Exception ex) {
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.getRecibosxParametros: "
							+ ex);
		} finally {
			try {
				HibernateUtilPruebaCn.closeSession(session);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return lstRecibos;
	}

	/******************************************************************/
	public double[] getMontoPendienteF05503am(Connection cn, String sCodsuc,
			int iNosol, String sTiposol, int iNocuota, int iCodcli) {
		String sql = "SELECT sum(ATAAP)  as CPENDIENTE,"
				+ "sum(ATFAP ) as DPENDIENTE " + "FROM "
				+ PropertiesSystem.JDEDTA + ".F5503AM " + "WHERE ATDOCO = "
				+ iNosol + " AND ATDCTO = '" + sTiposol + "'  and ATAN8 = "
				+ iCodcli + " and atdfr = " + iNocuota + " AND ATKCOO = '000"
				+ sCodsuc + "'";
		double[] dResultado = new double[2];
		PreparedStatement ps = null;
		try {
			ps = cn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				dResultado[0] = pasarEnteroADouble(rs.getInt("CPENDIENTE"));
				dResultado[1] = pasarEnteroADouble(rs.getInt("DPENDIENTE"));
			}
		} catch (Exception ex) {
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.getMontoAplicadoF0311: "
							+ ex);
		} finally {
			try {
				ps.close();
				// cn.close();
			} catch (Exception se2) {
				System.out
						.println("ERROR: Failed to close connection en ReciboCtrl.getMontoAplicadoF0311:  "
								+ se2);
			}
		}
		return dResultado;
	}

	/********************************************************************************/
	public double pasarEnteroADouble(int iMonto) {
		double dMonto = 0;
		String sMonto = "", s1 = "", s2 = "";
		try {
			if (iMonto > 0) {
				sMonto = String.valueOf(iMonto);
				if (sMonto.length() > 2) {
					s1 = sMonto.substring(0, sMonto.length() - 2);
					s2 = sMonto.substring(sMonto.length() - 2, sMonto.length());
					sMonto = s1 + "." + s2;
					dMonto = Double.parseDouble(sMonto);
				} else if (sMonto.length() == 2) {
					sMonto = "0." + sMonto;
					dMonto = Double.parseDouble(sMonto);
				} else if (sMonto.length() == 1) {
					sMonto = "0.0" + sMonto;
					dMonto = Double.parseDouble(sMonto);
				} else {
					dMonto = 0;
				}
			}
		} catch (Exception ex) {
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.pasarEnteroADouble: "
							+ ex);
		}
		return dMonto;
	}

	/************************ Leer y actualizar el número de batch *****************************************/
	public int leerActualizarNoAsiento() {
		int iNodoco = -1, iActualizado;
		Session sesion = HibernateUtilPruebaCn.currentSession();
		Transaction trans = null;
		String sql = "";
		Object ob = null;

		try {
			// --------- leer el número de documento a utilizar.
			sql = "select f.id.nnn002 from Vf0002 f where trim(f.id.nnsy) = '09'";
			trans = sesion.beginTransaction();
			ob = sesion.createQuery(sql).uniqueResult();
			if (ob != null) {
				iNodoco = Integer.parseInt(ob.toString());

				// --------- Actualizar el número de documento.
				sql = "UPDATE " + PropertiesSystem.JDECOM
						+ ".F0002 SET NNN002 = " + (iNodoco + 1)
						+ " where NNSY = '09'";
				Query q = sesion.createSQLQuery(sql);
				iActualizado = q.executeUpdate();
				if (iActualizado != 1)
					iNodoco = -1;
			}
			trans.commit();
		} catch (Exception error) {
			iNodoco = -1;
			error.printStackTrace();
		} finally {
			try {
				sesion.close();
			} catch (Exception e) {
				iNodoco = -1;
				System.out
						.println("Error al cerrar sesion en ReciboCtrl.leerActualizarNoDocJDE  "
								+ e);
			}
		}
		return iNodoco;
	}

	/************************************************************************************************/

	/**
	 * Obtener el registro de las factura de financimiento en F0311 pagadas en
	 * el recibo
	 **/
	public List leerFacturasReciboFN(int iCaid, String sCodComp, int iNumrec,
			String sTiporec, int iCodcli) {

		List lstFacturasRecibo = new ArrayList(), lstRecibofac = null;
		FacturaxRecibo facturaRec = null;

		Recibofac rf = null;
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = null;
		String sql = "", sFecha = "";
		JulianToCalendar fecha = null;
		Credhdr crFN = null;
		FechasUtil f = new FechasUtil();

		try {
			sql = " from Recibofac as r where r.id.caid = " + iCaid
					+ " and r.id.codcomp = '" + sCodComp.trim() + "'"
					+ " and r.id.numrec = " + iNumrec + " and r.id.tiporec = '"
					+ sTiporec + "' and r.id.codcli = " + iCodcli;

			tx = session.beginTransaction();
			lstRecibofac = session.createQuery(sql).list();
			tx.commit();

			for (int i = 0; i < lstRecibofac.size(); i++) {
				rf = (Recibofac) lstRecibofac.get(i);

				if (rf != null) {
					crFN = getInfoFactFinanciamiento(rf.getId().getNumfac(),
							sCodComp, rf.getId().getTipofactura(), rf.getId()
									.getPartida(), rf.getId().getCodunineg());
					sFecha = f.formatDatetoString(crFN.getId().getFecha(),
							"dd/MM/yyyy");
					facturaRec = new FacturaxRecibo(
							crFN.getId().getNofactura(), crFN.getId()
									.getTipofactura(),
							crFN.getId().getUnineg(), crFN.getMontoAplicar(),
							crFN.getId().getMoneda(), sFecha, crFN.getId()
									.getPartida());
				}
				lstFacturasRecibo.add(facturaRec);
			}
		} catch (Exception ex) {
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.leerFacturasReciboCredito: "
							+ ex);
		} finally {
			try {
				HibernateUtilPruebaCn.closeSession(session);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return lstFacturasRecibo;
	}

	/*********************************************************************************************/
	/** Obtiene información de facturas de financimiento **/
	public Credhdr getInfoFactFinanciamiento(int iNumfac, String sCodComp,
			String sTipoDocumento, String sPartida, String sCodunineg) {
		Credhdr chrFactFN = null;
		String sql = "";
		Transaction trans = null;
		Session sesion = HibernateUtilPruebaCn.currentSession();

		try {
			sql = " SELECT";
			sql += " CAST(TRIM(RPDCT) AS VARCHAR(2) CCSID 37) TIPOFACTURA,";
			sql += " RPDOC AS NOFACTURA,";
			sql += " CAST (RPSFX AS VARCHAR(3) CCSID 37) AS PARTIDA,";
			sql += " RPAN8 AS CODCLI, ";
			sql += " RPALPH AS NOMCLI, ";
			sql += " CAST(TRIM(RPMCU) AS VARCHAR(12) CCSID 37) CODUNINEG,";
			sql += " UN.MCDL01 AS UNINEG, ";
			sql += " CAST(RPKCO AS VARCHAR(5) CCSID 37) CODSUC,";
			sql += " (SELECT MCDL01 FROM "
					+ PropertiesSystem.JDEDTA
					+ ".F0006 WHERE SUBSTRING(RPKCO,4,5) = SUBSTRING(MCMCU,11,12) AND MCSTYL = 'BS') AS NOMSUC,";
			sql += " CAST(TRIM(UN.MCRP01) AS VARCHAR(3) CCSID 37) CODCOMP, ";
			sql += " CO.DRDL01 AS NOMCOMP, ";
			sql += " CAST(TRIM(RPCRCD) AS VARCHAR(3) CCSID 37) MONEDA,";
			sql += " RPCRR AS TASA,";
			sql += " CAST(RPAAP/100 AS DECIMAL (10,2)) AS CPENDIENTE,";
			sql += " CAST(RPFAP/100 AS DECIMAL (10,2)) AS DPENDIENTE,";
			sql += " CAST ((CAST(DATE(CHAR(1900000 + RPDIVJ)) AS TIMESTAMP)) AS DATE) FECHA,";
			sql += " CAST ((CAST(DATE(CHAR(1900000 + RPDDJ)) AS TIMESTAMP)) AS DATE) FECHAVENC";

			sql += " FROM " + PropertiesSystem.JDEDTA + ".F0311";
			sql += " INNER JOIN " + PropertiesSystem.JDEDTA
					+ ".F0006 UN ON RPMCU = UN.MCMCU";
			sql += " INNER JOIN "
					+ PropertiesSystem.JDECOM
					+ ".F0005 CO ON (UN.MCRP01 = SUBSTRING(CO.DRKY,8,10) AND CO.DRSY = '00' AND CO.DRRT = '01' AND CO.DRDL02 = 'F')";
			sql += " WHERE TRIM(RPDCTO) <> ''  AND TRIM(RPPO) <> ''";
			sql += " and RPDOC = " + iNumfac + " AND TRIM(UN.MCRP01) = '"
					+ sCodComp.trim() + "' AND  TRIM(RPDCT)= '"
					+ sTipoDocumento + "'";
			sql += " and RPSFX = '" + sPartida + "' and TRIM(RPMCU) = '"
					+ sCodunineg + "'";

			trans = sesion.beginTransaction();
			Object ob = sesion.createSQLQuery(sql).addEntity(Credhdr.class)
					.uniqueResult();
			trans.commit();

			if (ob != null)
				chrFactFN = (Credhdr) ob;

		} catch (Exception error) {
			chrFactFN = null;
			error.printStackTrace();
		} finally {
			try {
				HibernateUtilPruebaCn.closeSession(sesion);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return chrFactFN;
	}

	/*********************************************************************************************/
	/** Elimina los registros de F0311 generados al recibo de primas ******************/
	public boolean eliminarRegistros311xTipo(int iNobatch, String sRpdct,
			Connection cn) {
		boolean bHecho = true;
		PreparedStatement ps = null;
		String sql = "";
		int iResul = 0;

		try {
			sql = " DELETE FROM " + PropertiesSystem.JDEDTA
					+ ".F0311 F WHERE F.RPICU =  " + iNobatch;
			sql += " AND F.RPDCT = '" + sRpdct + "' AND RPICUT = 'R' ";
			ps = cn.prepareStatement(sql);
			iResul = ps.executeUpdate();

			if (iResul <= 0)
				bHecho = false;

		} catch (Exception error) {
			bHecho = false;
			error.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (Exception e) {
				bHecho = false;
				System.out
						.println("Error en ReciboCtrl.eliminarRegistros311ru "
								+ e);
			}
		}
		return bHecho;
	}

	/*********************************************************************************************/
	/**
	 * Obtiene los datos del bien pagado en el recibo de primas. /
	 ********************/
	public List leerDatosBien(int iNumrec, int iCaid, String sCodcomp,
			String sCodsuc) {
		List lstDatosBien = new ArrayList(), lstBien = new ArrayList();
		Session sesion = HibernateUtilPruebaCn.currentSession();
		
		String sConsulta = "", tipoprod = "S/T", marca = "S/M", modelo = "S/M", noitem = "";
		Object ob = null;
		FacturaxRecibo facturaRec = null;		
		boolean bNuevaSesionENS = false;
		Transaction tENS = null;

		try {
			sConsulta = " from Bien as b where b.id.caid = " + iCaid
					+ " and b.id.codsuc = '" + sCodsuc + "'";
			sConsulta += " and b.id.codcomp = '" + sCodcomp
					+ "' and b.id.numrec = " + iNumrec;

			
			lstBien = sesion.createQuery(sConsulta).list();
			

			if (lstBien != null && lstBien.size() > 0) {
				Bien bn = (Bien) lstBien.get(0);
				tipoprod = bn.getTipoprod();
				marca = bn.getMarca();
				modelo = bn.getModelo();
				noitem = bn.getNoitem();

				

				if (!tipoprod.equals("S/T")) {
					sConsulta = "from VtipoProd as v where trim(v.id.drky) = '"
							+ tipoprod + "'";
					ob = sesion.createQuery(sConsulta).uniqueResult();
					tipoprod = ob != null ? ((VtipoProd) ob).getId()
							.getDrdl01().trim() : "S/T";
				}
				if (!marca.equals("S/M")) {
					sConsulta = "from Vmarca as v where trim(v.id.drky) = '"
							+ marca + "'";
					ob = sesion.createQuery(sConsulta).uniqueResult();
					marca = ob != null ? ((Vmarca) ob).getId().getDrdl01()
							.trim() : "S/M";
				}
				if (!modelo.equals("S/M")) {
					sConsulta = "from Vmodelo as v where trim(v.id.drky) = '"
							+ modelo + "'";
					ob = sesion.createQuery(sConsulta).uniqueResult();
					modelo = ob != null ? ((Vmodelo) ob).getId().getDrdl01()
							.trim() : "S/M";
				}
				

			}
			facturaRec = new FacturaxRecibo(0, tipoprod, marca, modelo, noitem);
			lstDatosBien.add(facturaRec);

		} catch (Exception error) {
			error.printStackTrace();
		} 
		return lstDatosBien;
	}

	/*********************************************************************************************/
	/** Guardar registro de recibo de Ingresos extraordinarios EX **/
	public boolean guardarRcEx(Connection cn, String sCodsuc, int iCodcli,
			String sTipoDoc, int iRpdoc, String sRpdctm, int iRpdocm,
			int iFechaact, int iNobatch, String sPayStatus, int iMtompagodom,
			String sModofd, String sMoneda, double dTasaOf, int iMtoForaneo,
			String sIdctaTO, String sidCtaMpago, String sRPAM2, String sRPSBLT,
			String sRPSBL, String sRPTRTC, String sCodUnineg, String sConcepto,
			String sUsuario, String sAplicacion) {
		boolean bHecho = true;
		String sql = "", sNombrePc = "", sHora[], sFecha[];
		PreparedStatement ps = null;
		Date dFechaHora = new Date();
		SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		EmpleadoCtrl empCtrl = new EmpleadoCtrl();
		Vf0101 vf = null;
		Calendar cFecha;
		CalendarToJulian fecha;
		int iFecha, iHora, iAnio, iMes;
		String sNomcli = "", sCadValida = "";
		Divisas d = new Divisas();
		try {
			cFecha = Calendar.getInstance();
			fecha = new CalendarToJulian(cFecha);
			iFecha = fecha.getDate();

			sNombrePc = "SERVER";// InetAddress.getLocalHost().getHostName();
			sHora = (dfHora.format(dFechaHora)).split(":"); // obtener hora en
															// enteros
			sFecha = (sdf.format(dFechaHora)).split("/"); // obtener partes de
															// la fecha
			iHora = Integer.parseInt(sHora[0] + sHora[1] + sHora[2]);
			iAnio = Integer.parseInt(sFecha[2].substring(2, 4));
			iMes = Integer.parseInt(sFecha[1]);

			sCodUnineg = sCodUnineg.length() == 2 ? "          " + sCodUnineg
					: "        " + sCodUnineg;
			if (sConcepto.length() > 30)
				sConcepto = sConcepto.substring(0, 29);
			if (sNombrePc.length() > 9)
				sNombrePc = sNombrePc.substring(0, 9);

			vf = empCtrl.buscarEmpleadoxCodigo2(iCodcli);
			Vf0101Id vi = vf.getId();
			sNomcli = vi.getAbalph().trim();
			sCadValida = d.remplazaCaractEspeciales(sNomcli, "'", "-");
			if (!sCadValida.equals("")) {
				sNomcli = sCadValida;
			}
			sql = "INSERT INTO " + PropertiesSystem.JDEDTA + ".F0311 (";
			sql += "RPKCO,RPAN8,RPDCT,RPDOC,RPSFX,RPDIVJ,RPDCTM,RPDOCM,   RPDMTJ,RPDGJ,RPFY,RPCTRY,RPPN,RPCO,RPICUT,RPICU,";
			sql += "RPDICJ,   RPPST,RPAG,RPCRRM,RPCRCD,RPCRR,RPACR,   RPDSVJ,RPGLBA,RPAM,RPAID2,RPAM2,RPMCU,RPSBLT,RPSBL,   RPDDJ,";
			sql += "RPDDNJ,RPTRTC,RPRMK,RPALPH,RPAC01,RPAC02,RPAC03,RPAC04,RPAC05,RPAC06,RPAC07,RPAC08,RPAC09,RPAC10,";
			sql += "RPATE,RPATR,RPATP,RPATPR,RPAT1,RPAT2,RPAT3,RPAT4,RPAT5,RPTORG,RPUSER,RPPID,RPUPMJ,RPUPMT,RPJOBN) VALUES(";

			sql += "'" + sCodsuc + "'," + iCodcli + ",'" + sTipoDoc + "',"
					+ iRpdoc + "," + "'001'," + 0 + "," + "'" + sRpdctm + "',"
					+ iRpdocm + ",";
			sql += iFecha + "," + iFecha + "," + iAnio + ", 20 ," + iMes
					+ ", '" + sCodsuc + "'," + "'R'," + iNobatch + "," + iFecha
					+ ",";
			sql += "'" + sPayStatus + "'" + "," + iMtompagodom + "," + "'"
					+ sModofd + "'" + "," + "'" + sMoneda + "'," + dTasaOf
					+ "," + iMtoForaneo + ",";
			sql += iFecha + "," + "'" + sidCtaMpago + "'," + "'2'," + "'"
					+ sIdctaTO + "'," + "'" + sRPAM2 + "'," + "'" + sCodUnineg
					+ "'," + "'" + sRPSBLT + "'," + "'" + sRPSBL + "',";
			sql += iFecha + "," + iFecha + "," + "'" + sRPTRTC + "'," + "'"
					+ sConcepto + "'," + "'" + sNomcli + "'," + "'"
					+ vi.getAbac01() + "'," + "'" + vi.getAbac02() + "',";
			;
			sql += "'" + vi.getAbac03() + "'," + "'" + vi.getAbac04() + "',"
					+ "'" + vi.getAbac05() + "'," + "'" + vi.getAbac06() + "',"
					+ "'" + vi.getAbac07() + "',";
			sql += "'" + vi.getAbac08() + "'," + "'" + vi.getAbac09() + "',"
					+ "'" + vi.getAbac10() + "'," + "'" + vi.getAbate() + "',"
					+ "'" + vi.getAbatr() + "',";
			sql += "'" + vi.getAbatp() + "'," + "'" + vi.getAbatpr() + "',"
					+ "'" + vi.getAbat1() + "'," + "'" + vi.getAbat2() + "',"
					+ "'" + vi.getAbat3() + "',";
			sql += "'" + vi.getAbat4() + "'," + "'" + vi.getAbat5() + "',"
					+ "'" + sUsuario + "'," + "'" + sUsuario + "'," + "'"
					+ sAplicacion + "'," + iFecha + "," + iHora + "," + "'"
					+ sNombrePc + "'" + ")";

			ps = cn.prepareStatement(sql);
			int rs = ps.executeUpdate();
			if (rs != 1) {
				bHecho = false;
			}

		} catch (Exception error) {
			error.printStackTrace();
		}
		return bHecho;
	}

	/*********************************************************************************************/
	/**
	 * Guardar registro de recibo de primas RU /
	 ********************/
	public boolean guardarRU(Connection cn, String sCodsuc, int iCodcli,
			String sTipoDoc, int iRpdoc, String sRpdctm, int iRpdocm,
			int iFechaact, int iNobatch, String sRppost, String sPayStatus,
			long iMtompagodom, long iRpaap, String sModofd, String sMoneda,
			double dTasaOf, long iMtoForaneo, long iRpfap, String sIdCuenta,
			String sCodUnineg, String sConcepto, String sUsuario,
			String sAplicacion, String iNocontrato, String sTipocont) {
		
		boolean bHecho = true;
		String sql = "", sNombrePc = "", sHora[], sFecha[];
		PreparedStatement ps = null;
		Date dFechaHora = new Date();
		SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		EmpleadoCtrl empCtrl = new EmpleadoCtrl();
		Vf0101 vf = null;

		int iFecha, iHora, iAnio, iMes;
		String sCadValida = "", sNomcli = "";
		Divisas d = new Divisas();
		
		try {
			sql = "INSERT INTO " + PropertiesSystem.JDEDTA + ".F0311 (";
			sql += "RPKCO,RPAN8,RPDCT,RPDOC,RPSFX,RPDIVJ,RPDCTM,RPDOCM,RPDMTJ,RPDGJ,RPFY,RPCTRY,RPPN,RPCO,RPICUT,RPICU,";
			sql += "RPDICJ,RPPA8,RPAN8J,RPPOST,RPPST,RPAG,RPAAP,RPCRRM,RPCRCD,RPCRR,RPACR,RPFAP,RPDSVJ,RPGLC,RPGLBA,";
			sql += "RPAM,RPMCU,RPDDJ,RPDDNJ,RPTRTC,RPRMK,RPALPH,RPAC01,RPAC02,RPAC03,RPAC04,RPAC05,RPAC06,RPAC07,RPAC08,";
			sql += "RPAC09,RPAC10,RPATE,RPATR,RPATP,RPATPR,RPAT1,RPAT2,RPAT3,RPAT4,RPAT5,RPTORG,RPUSER,RPPID,RPUPMJ,RPUPMT,RPJOBN,RPPO,RPDCTO) VALUES(";

			iFecha =  FechasUtil.dateToJulian( new Date() );

			sHora = (dfHora.format(dFechaHora)).split(":"); // obtener hora en enteros
			sFecha = (sdf.format(dFechaHora)).split("/"); // obtener partes de la fecha
			iHora = Integer.parseInt(sHora[0] + sHora[1] + sHora[2]);
			iAnio = Integer.parseInt(sFecha[2].substring(2, 4));
			iMes = Integer.parseInt(sFecha[1]);

			sCodUnineg = com.casapellas.util.CodeUtil.pad(sCodUnineg.trim(), 12," ");  //sCodUnineg = sCodUnineg.length() == 2 ? "          " + sCodUnineg : "        " + sCodUnineg;
			
			if (sConcepto.length() > 30)
				sConcepto = sConcepto.substring(0, 29);
			
			//sNombrePc = "SERVER"; InetAddress.getLocalHost().getHostName();
			sNombrePc = PropertiesSystem.ESQUEMA;
			if(sNombrePc.length() > 9)
				sNombrePc =	sNombrePc.substring(0, 9);
			if (sNombrePc.length() > 9)
				sNombrePc = sNombrePc.substring(0, 9);

			vf = empCtrl.buscarEmpleadoxCodigo2(iCodcli);
			Vf0101Id vi = vf.getId();
			sNomcli = vi.getAbalph().trim();
			sCadValida = d.remplazaCaractEspeciales(sNomcli, "'", "-");
			if (!sCadValida.equals("")) {
				sNomcli = sCadValida;
			}

			sql += "'" + sCodsuc + "'," + iCodcli + ",'" + sTipoDoc + "',"
					+ iRpdoc + "," + "'001'," + iFecha + "," + "'" + sRpdctm
					+ "'," + iRpdocm + ",";
			sql += iFechaact + "," + iFecha + "," + iAnio + ", 20 ," + iMes
					+ ", '" + sCodsuc + "'," + "'R'," + iNobatch + "," + iFecha
					+ ",";
			sql += iCodcli + "," + iCodcli + "," + "'" + sRppost + "'" + ","
					+ "'" + sPayStatus + "'" + "," + iMtompagodom + ","
					+ iRpaap + "," + "'" + sModofd + "'" + ",";
			sql += "'" + sMoneda + "'," + dTasaOf + "," + iMtoForaneo + ","
					+ iRpfap + "," + iFecha + "," + "'UC'," + "'" + sIdCuenta
					+ "'," + "'2'," + "'" + sCodUnineg + "',";
			sql += iFecha + "," + iFecha + "," + "'U'," + "'" + sConcepto
					+ "'," + "'" + sNomcli + "'," + "'" + vi.getAbac01() + "',"
					+ "'" + vi.getAbac02() + "',";
			sql += "'" + vi.getAbac03() + "'," + "'" + vi.getAbac04() + "',"
					+ "'" + vi.getAbac05() + "'," + "'" + vi.getAbac06() + "',"
					+ "'" + vi.getAbac07() + "',";
			sql += "'" + vi.getAbac08() + "'," + "'" + vi.getAbac09() + "',"
					+ "'" + vi.getAbac10() + "'," + "'" + vi.getAbate() + "',"
					+ "'" + vi.getAbatr() + "',";
			sql += "'" + vi.getAbatp() + "'," + "'" + vi.getAbatpr() + "',"
					+ "'" + vi.getAbat1() + "'," + "'" + vi.getAbat2() + "',"
					+ "'" + vi.getAbat3() + "',";
			sql += "'" + vi.getAbat4() + "'," + "'" + vi.getAbat5() + "',"
					+ "'" + sUsuario + "'," + "'" + sUsuario + "',";
			sql += "'" + sAplicacion + "'," + iFecha + "," + iHora + "," + "'"
					+ sNombrePc + "'" + ", '" + iNocontrato + "', '"
					+ sTipocont + "')";

			ps = cn.prepareStatement(sql);
			int rs = ps.executeUpdate();
			if (rs != 1) {
				bHecho = false;
			}

		} catch (Exception error) {
			error.printStackTrace(); 
			
			bHecho = false;
		}
		return bHecho;
	}

	/*********************************************************************************************/
	/**
	 * Obtiene y actualiza el número RPDOC a utilizar en cada registro del
	 * Recibo de primas (RU) /
	 *********/
	public int leerNumeroRpdoc(boolean bActualizar) {
		int iNoRPDOC = -1;
		Session sesion = HibernateUtilPruebaCn.currentSession();
		Transaction trans = null;
		String sql = "";

		try {
			sql = "select f.id.nnn002 from Vf0002 f where trim(f.id.nnsy) = '03'";
			trans = sesion.beginTransaction();
			Object ob = sesion.createQuery(sql).uniqueResult();
			if (ob != null) {
				iNoRPDOC = Integer.parseInt(ob.toString());
				if (bActualizar) {
					sql = "UPDATE " + PropertiesSystem.JDECOM
							+ ".F0002 SET NNN002 = " + (iNoRPDOC + 1)
							+ " where NNSY = '03'";
					ConsolidadoDepositosBcoCtrl.executeSqlQueryTx(null, sql);
				}
			}
			
		} catch (Exception error) {
			
			iNoRPDOC = -1;
		} 
		return iNoRPDOC;
	}

	/*********************************************************************************/
	/**
	 * Obtiene y actualiza el número a utilizar en RPDOCM para el Recibo de
	 * primas (RU) /
	 *********/
	public int leerNumeroRpdocm(boolean bActualizar, String sCodcomp) {
		int iNoRPDOCM = -1;
		Session sesion = HibernateUtilPruebaCn.currentSession();
		Transaction trans = null;
		String sql = "";
		String sTabla = "", sPosicion = "";
		try {
		String[] documentos=	DocumuentosTransaccionales.obteneConfNumeracionRU(sCodcomp).split(",");
			
		
				sTabla = documentos[0];
				sPosicion = documentos[1];
			

			sql = "select f.id.nnn" + sPosicion
					+ " from Vf0002 f where trim(f.id.nnsy) = '" + sTabla + "'";
			trans = sesion.beginTransaction();
			Object ob = sesion.createQuery(sql).uniqueResult();
			if (ob != null) {
				iNoRPDOCM = Integer.parseInt(ob.toString());
				if (bActualizar) {
					sql = "UPDATE " + PropertiesSystem.JDECOM
							+ ".F0002 SET NNN" + sPosicion + " = "
							+ (iNoRPDOCM + 1) + " where trim(NNSY) = '"
							+ sTabla + "'";
					ConsolidadoDepositosBcoCtrl.executeSqlQueryTx(null, sql);
				}
			} else {
				error = new Exception(
						"@LOGCAJA: No se pudo leer el numero de recibo en jde!!!;Compañia:"
								+ sCodcomp + ";Tabla:" + sTabla + ";Posicion:"
								+ sPosicion);
				errorDetalle = error;
			}

		} catch (Exception error) {
			error = new Exception(
					"@LOGCAJA: No se pudo leer el numero de recibo en jde!!!;Compañia:"
							+ sCodcomp + ";Tabla:" + sTabla + ";Posicion:"
							+ sPosicion);
			errorDetalle = error; 
			
		}
		return iNoRPDOCM;
	}

	/*********************************************************************************/

	/*********************************************************************************************/
	public int leerNumeroRpdocmRM(boolean bActualizar) {
		int iNoRPDOCM = -1;
		Session sesion = HibernateUtilPruebaCn.currentSession();
		Transaction trans = null;
		String sql = "";

		try {
			sql = "select f.id.nnn007 from Vf0002 f where trim(f.id.nnsy) = '03'";
			trans = sesion.beginTransaction();
			Object ob = sesion.createQuery(sql).uniqueResult();
			if (ob != null) {
				iNoRPDOCM = Integer.parseInt(ob.toString());
				if (bActualizar) {
					sql = "UPDATE " + PropertiesSystem.JDECOM
							+ ".F0002 SET NNN007 = " + (iNoRPDOCM + 1)
							+ " where NNSY = '03'";
					ConsolidadoDepositosBcoCtrl.executeSqlQueryTx(null, sql);
				}
			}
			
		} catch (Exception error) {
			
			iNoRPDOCM = -1;
		} 
		return iNoRPDOCM;
	}


	
	/*********************************************************************************/
	/**
	 * Actualiza el número de recibo en F55ca014 por caja y compañía con
	 * hibernate /
	 *********/
	public void actualizarNoReciboV2(Session sesion,int iNumrecNuevo, int iCaid, String sCodcomp) {
		String sql = "";		
		

		try {
			sql = " update " + PropertiesSystem.ESQUEMA
					+ ".F55ca014 set c4nncu = " + iNumrecNuevo;
			sql += " where c4id = " + iCaid + " and c4rp01 = '" + sCodcomp
					+ "'";
			
			LogCajaService.CreateLog("actualizarNoReciboV2", "QRY", sql);
			Query q = sesion.createSQLQuery(sql);
			q.executeUpdate();
			

		} catch (Exception error) {
			LogCajaService.CreateLog("actualizarNoReciboV2", "ERR", error.getMessage());
		} 
	}


	/************* BUSCAR FICHA POR PARAMETROS ***************************************************************/
	public Recibo obtenerFichaCV(int iNumFicha, int iCaid, String sCodcomp,
			String sCodsuc) {
		Recibo ficha = null;
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = null;
		String sql = "from Recibo as r where r.id.numrec = " + iNumFicha
				+ " and  r.id.caid = " + iCaid + " and r.id.codcomp = '"
				+ sCodcomp + "' and r.id.codsuc = '" + sCodsuc
				+ "' and r.id.tiporec = 'FCV'";
		try {
			tx = session.beginTransaction();
			ficha = (Recibo) session.createQuery(sql).uniqueResult();
			tx.commit();
		} catch (Exception ex) {
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.obtenerFichaCV: "
							+ ex);
		} finally {
			try {
				HibernateUtilPruebaCn.closeSession(session);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return ficha;
	}

	/*******************************************************************************************************/
	/************** ACTUALIZAR EL NUMERO DE FICHA AL RECIBO *************************************************/
	public boolean actualizarNoFicha(Session session, Transaction tx,
			int Numrec, int iNoFicha, int iCaid, String sCodcomp,
			String sCodsuc, String sTipoRec) {
		boolean bActualizado = true;
		String sql = "update Recibo as r set r.recjde = " + iNoFicha
				+ " where r.id.numrec = " + Numrec + " and r.id.caid =" + iCaid
				+ " and r.id.tiporec = '" + sTipoRec + "' and r.id.codcomp = '"
				+ sCodcomp + "' and r.id.codsuc = '" + sCodsuc + "'";
		try {
			tx = session.beginTransaction();
			Query q = session.createQuery(sql);
			int rowCount = q.executeUpdate();
			if (rowCount == 0) {
				bActualizado = false;
			}
		} catch (Exception ex) {
			bActualizado = false;
			System.out
					.print("Se capturo una excepcion en el ReciboCtrl.actualizarNoFicha: "
							+ ex);
		}
		return bActualizado;
	}

	/******************* ACTUALIZAR EL NUMERO DE FICHA AL RECIBO *************************************/
	public static void setNoFichaRecibo(int Numrec, int iNoFicha, int iCaid,
			String sCodcomp, String sCodsuc, String sTipoRec, Session sesion) {

		try {
			String sql = "update " + PropertiesSystem.ESQUEMA
					+ ".Recibo as r set r.recjde = " + iNoFicha
					+ " where r.numrec = " + Numrec + " and r.caid =" + iCaid
					+ " and r.tiporec = '" + sTipoRec + "' and r.codcomp = '"
					+ sCodcomp + "' and r.codsuc = '" + sCodsuc + "'";

			int iRegUpdt = sesion.createSQLQuery(sql).executeUpdate();
 
			sql = null;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	
	public static void setNoFichaRecibo(int Numrec, int iNoFicha, int iCaid, String sCodcomp, String sTipoRec) {

		try {
			String sql = "update " + PropertiesSystem.ESQUEMA + ".Recibo as r set r.recjde = " + iNoFicha + " where r.numrec = " + Numrec
			             + " and r.caid =" + iCaid + " and r.tiporec = '" + sTipoRec + "' and r.codcomp = '" + sCodcomp + "'";

			LogCajaService.CreateLog("setNoFichaRecibo", "QRY", sql);
			
			boolean update = ConsolidadoDepositosBcoCtrl.executeSqlQueryTx(null, sql);
			
		}
		catch (Exception ex) {
			LogCajaService.CreateLog("setNoFichaRecibo", "ERR", ex.getMessage());
			ex.printStackTrace(); 
		}
	}
	
	public static void setNoFichaRecibo(int Numrec, int iNoFicha, int iCaid,
			String sCodcomp, String sCodsuc, String sTipoRec) {
		Session sesion = null;
		Transaction trans = null;

		try {
			String sql = "update " + PropertiesSystem.ESQUEMA
					+ ".Recibo as r set r.recjde = " + iNoFicha
					+ " where r.numrec = " + Numrec + " and r.caid =" + iCaid
					+ " and r.tiporec = '" + sTipoRec + "' and r.codcomp = '"
					+ sCodcomp + "' and r.codsuc = '" + sCodsuc + "'";

			sesion = HibernateUtilPruebaCn.currentSession();
			trans = sesion.beginTransaction();

			LogCajaService.CreateLog("setNoFichaRecibo", "QRY", sql);
			sesion.createSQLQuery(sql).executeUpdate();

			sql = null;

		} catch (Exception ex) {
			LogCajaService.CreateLog("setNoFichaRecibo", "ERR", ex.getMessage());
			ex.printStackTrace();
		} finally {

			try {
				trans.commit();
			} catch (Exception ex) {
			}
			try {
				sesion.close();
			} catch (Exception ex) {
			}

			sesion = null;
			trans = null;
		}
	}

	/******************************************************************************************************/
	/************** ACTUALIZAR EL NUMERO DE FICHA AL RECIBO *************************************************/
	public boolean actualizarNoFicha(Connection cn, int Numrec, int iNoFicha,
			int iCaid, String sCodcomp, String sCodsuc, String sTipoRec) {
		boolean bActualizado = true;
		PreparedStatement ps = null;

		String sql = "update " + PropertiesSystem.ESQUEMA
				+ ".Recibo as r set r.recjde = " + iNoFicha
				+ " where r.numrec = " + Numrec + " and r.caid =" + iCaid
				+ " and r.tiporec = '" + sTipoRec + "' and r.codcomp = '"
				+ sCodcomp + "' and r.codsuc = '" + sCodsuc + "'";

		try {
			ps = cn.prepareStatement(sql);
			int rs = ps.executeUpdate();

			if (rs != 1) {
				bActualizado = false;
			}
		} catch (Exception ex) {
			bActualizado = false;
			System.out
					.print("Se capturo una excepcion en el ReciboCtrl.actualizarNoFicha: "
							+ ex);
		} finally {
			try {
				ps.close();
				// cn.close();
			} catch (Exception se2) {
				System.out
						.println("ERROR: Failed to close connection en ReciboCtrl.actualizarNoFicha: "
								+ se2);
			}
		}
		return bActualizado;
	}

	/******************* OBTENER CUENTA DE VENTA DE CONTADO DE CAJA ******************************************/
	public F55ca018 obtenerCuentaVentaContadoBK(int iCodCaja, String sCodComp,
			String sCodUnineg) {
		F55ca018 f55ca018 = null;
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = null;
		String sql = "from F55ca018 as f where f.id.c8id = " + iCodCaja
				+ " and trim(f.id.c8rp01) = '" + sCodComp.trim()
				+ "' and trim(f.id.c8mcu) = '" + sCodUnineg.trim()
				+ "' and f.id.c8stat = 'A'";
		try {
			tx = session.beginTransaction();
			f55ca018 = (F55ca018) session.createQuery(sql).uniqueResult();
			tx.commit();
		} catch (Exception ex) {
			System.out
					.println("Se capturo una excepcion en ReciboContadoCtrl.obtenerCuentaVentaContadoBK: "
							+ ex);
		} finally {
			try {
				HibernateUtilPruebaCn.closeSession(session);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return f55ca018;
	}

	/*****************************************************************************************************/
	/******************* OBTENER CUENTA DE COMPRA VENTA DE CAJA ******************************************/
	public F55ca024 obtenerCuentaCV(String sCodsuc, String sMoneda) {
		F55ca024 f55ca024 = null;
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = null;
		String sql = "from F55ca024 as f where trim(f.id.d4mcu) = '" + sCodsuc
				+ "' and f.id.d4crcd = '" + sMoneda + "' and f.id.d4stat = 'A'";
		try {
			tx = session.beginTransaction();
			f55ca024 = (F55ca024) session.createQuery(sql).uniqueResult();
			tx.commit();
		} catch (Exception ex) {
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.obtenerCuentaCV: "
							+ ex);
		} finally {
			try {
				HibernateUtilPruebaCn.closeSession(session);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return f55ca024;
	}

	/*****************************************************************************************************/
	/*****
	 * OBTIENE EL NUMERO DE BATCH EN EL QUE FUE PROCESADO EL RECIBO POR
	 * CAJA,SUCURSAL,COMPANIA Y NUMERO DE RECIBO
	 ****************************************************************************************************************************************/
	public Recibojde[] leerEnlaceReciboJDE(int iCaid, String sCodSuc,
			String sCodComp, int iNumrec, String sTipoDoc, String sTiporec) {
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = null;
		String sql = "from Recibojde as r where r.id.caid = " + iCaid
				+ " and r.id.codsuc = '" + sCodSuc + "'"
				+ " and r.id.codcomp = '" + sCodComp + "' and r.id.numrec = "
				+ iNumrec + " and r.id.tipodoc = '" + sTipoDoc
				+ "' and r.id.tiporec = '" + sTiporec + "'";
		List lstResult = new ArrayList();
		Recibojde[] recibojde = null;
		try {
			tx = session.beginTransaction();
			lstResult = session.createQuery(sql).list();
			recibojde = new Recibojde[lstResult.size()];
			for (int i = 0; i < lstResult.size(); i++) {
				recibojde[i] = (Recibojde) lstResult.get(i);
			}
			tx.commit();
		} catch (Exception ex) {
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.leerNoBatchRecibo: "
							+ ex);
		} finally {
			try {
				HibernateUtilPruebaCn.closeSession(session);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return recibojde;
	}

	public Recibojde[] leerEnlaceReciboJDE(int iCaid, String sCodSuc,
			String sCodComp, int iNumrec, String sTiporec) {
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = null;
		String sql = "from Recibojde as r where r.id.caid = " + iCaid
				+ " and r.id.codsuc = '" + sCodSuc + "'"
				+ " and r.id.codcomp = '" + sCodComp + "' and r.id.numrec = "
				+ iNumrec + " and r.id.tiporec = '" + sTiporec + "'";
		List lstResult = new ArrayList();
		Recibojde[] recibojde = null;
		try {
			tx = session.beginTransaction();
			lstResult = session.createQuery(sql).list();
			recibojde = new Recibojde[lstResult.size()];
			for (int i = 0; i < lstResult.size(); i++) {
				recibojde[i] = (Recibojde) lstResult.get(i);
			}
			tx.commit();
		} catch (Exception ex) {
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.leerNoBatchRecibo: "
							+ ex);
		} finally {
			try {
				HibernateUtilPruebaCn.closeSession(session);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return recibojde;
	}

	/******************** LLENAR ENLACE ENTRE RECIBO Y FACTURA *********************************************************************************/
	public boolean fillEnlaceReciboFac(Session session, Transaction tx,
			int iNumRec, String codcomp, int iNumFac, double Monto,
			String sTipoDoc, int iCaId, String sCodSuc, String sPartida,
			String sCodunineg, String sTiporec, int iCodcli, int iFecha) {
		boolean filled = false;

		try {
			Recibofac recibofac = new Recibofac();
			RecibofacId recibofacid = new RecibofacId();

			recibofacid.setNumfac(iNumFac); // Numero factura
			recibofacid.setNumrec(iNumRec); // Numero Recibo
			recibofacid.setTipofactura(sTipoDoc); // Tipo de documento
			recibofacid.setCodcomp(codcomp); // Cod. Compañía
			recibofacid.setPartida(sPartida); // Partida en blanco para contado
			recibofacid.setCaid(iCaId);
			recibofacid.setCodsuc(sCodSuc);
			recibofacid.setCodunineg(sCodunineg.trim());
			recibofacid.setTiporec(sTiporec);
			recibofacid.setCodcli(iCodcli);
			recibofacid.setFecha(iFecha);

			recibofac.setId(recibofacid); // Recibo
			recibofac.setMonto(BigDecimal.valueOf(Monto)); // Monto
			recibofac.setEstado("");
			
			LogCajaService.CreateLog("fillEnlaceReciboFac", "HQRY", LogCajaService.toJson(recibofac));
			
			session.save(recibofac); // Factura
			filled = true;
		} catch (Exception ex) {
			LogCajaService.CreateLog("fillEnlaceReciboFac", "ERR", ex.getMessage());
		}

		return filled;
	}

	/********* 10 cargar los recibos pagados con monedas distintas del arqueo ******************/
	public List cargarRecibosOtrosIngEg(boolean bIngreso, int iCaid,
			String sCodcomp, String sCodsuc, String numRecibos, String sMoneda,
			Date dtFecha) {
		List lstCambios = new ArrayList();
		Session sesion = HibernateUtilPruebaCn.currentSession();
		Transaction trans = null;
		String sConsulta = "";

		try {
			sConsulta = " from Vmonedafactrec as v where v.id.caid = " + iCaid
					+ " and v.id.codsuc ='" + sCodsuc + "'";
			sConsulta += " and trim(v.id.codcomp) = '" + sCodcomp.trim()
					+ "' and v.id.rmoneda <> '" + sMoneda + "'";
			sConsulta += " and v.id.mpago = '"+MetodosPagoCtrl.EFECTIVO+"' and v.id.fmoneda = '" + sMoneda
					+ "'";
			sConsulta += " and v.id.fecha = '" + dtFecha
					+ "'order by v.id.numrec";
			sConsulta += " and v.id.forarecibido > 0";

			trans = sesion.beginTransaction();
			lstCambios = sesion.createQuery(sConsulta).list();
			trans.commit();

		} catch (Exception error) {
			error.printStackTrace();
		} finally {
			try {
				HibernateUtilPruebaCn.closeSession(sesion);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return lstCambios;
	}

	/********
	 * EGRESOS: obtener recibos pagados con moneda distinta del arqueo, distinta
	 * de la factura
	 *********************/
	// public List obtenerEgresosRecMonEx(int iCaid, String sCodcomp,String
	// sCodsuc,String sMoneda,Date dtFecha,Date dtHora,Date dtHini){
	public List obtenerEgresosRecMonEx(int iCaid, String sCodcomp,
			String sCodsuc, String sMoneda, Date dtFechaArqueo,
			Date dtHoraInicio, Date dtHoraFin, Session sesion) {
		List lstRecibos = null;
		// List<Integer>lstNumeroRecibos = null;
		try {
			// Criteria cr1 = sesion.createCriteria(Recibo.class);
			// cr1.add(Restrictions.eq("caid", iCaid));
			// cr1.add(Restrictions.eq("codsuc" ,sCodsuc ));
			// cr1.add(Restrictions.eq("codcomp" ,sCodcomp ));
			// cr1.add(Restrictions.eq("fecha" ,dtFechaArqueo ));
			// cr1.add(Restrictions.ne("estado" ,"A" ));
			//
			// List<Recibo>lstRecibos1 = cr1.list();
			// if(lstRecibos1!=null){
			// lstNumeroRecibos = new ArrayList<Integer>(lstRecibos1.size());
			// for (Recibo recibo : lstRecibos1) {
			// lstNumeroRecibos.add(recibo.getNumrec());
			// }
			// }

			Criteria cr = sesion.createCriteria(Vmonedafactrec.class);
			cr.add(Restrictions.eq("id.caid", iCaid));
			cr.add(Restrictions.eq("id.codsuc", sCodsuc));
			cr.add(Restrictions.eq("id.codcomp", sCodcomp));
			cr.add(Restrictions.ne("id.rmoneda", sMoneda));
			cr.add(Restrictions.eq("id.fmoneda", sMoneda));
			cr.add(Restrictions.eq("id.fecha", dtFechaArqueo));
			cr.add(Restrictions.ne("id.restado", "A"));
			cr.add(Restrictions.gt("id.forarecibido", new BigDecimal("0")));
			cr.add(Restrictions.between("id.hora", dtHoraInicio, dtHoraFin));
			cr.addOrder(Order.asc("id.numrec"));

			lstRecibos = cr.list();
			if (lstRecibos == null)
				lstRecibos = new ArrayList(0);

			// sConsulta = " from Vmonedafactrec as v where v.id.caid = "+iCaid+
			// " and v.id.codsuc ='"+sCodsuc+"'";
			// sConsulta +=
			// " and trim(v.id.codcomp) = '"+sCodcomp.trim()+"' and v.id.rmoneda <> '"+sMoneda+"'";
			// sConsulta +=
			// " and v.id.fmoneda = '"+sMoneda+"' and v.id.fecha = '"+sFecha+"'";
			// sConsulta +=
			// " and v.id.horamod <= '"+sHora+"'  and v.id.restado <>'A'";
			// sConsulta += " and v.id.forarecibido > 0";
			// sConsulta += " order by v.id.numrec";
			// lstRecibos = sesion.createQuery(sConsulta).list();

		} catch (Exception error) {
			lstRecibos = null;
			error.printStackTrace();
		}
		return lstRecibos;
	}

	/*********** obtener recibos pagados con moneda de arqueo y distinta de la factura *********************/
	public List obtenerIngresoRecMonEx(int iCaid, String sCodcomp,
			String sCodsuc, String sMoneda, Date dtFechaArqueo,
			Date dtHoraInicio, Date dtHoraFin, Session sesion) {
		List lstRecibos = new ArrayList();
		// Session sesion =
		// HibernateUtil.getSessionFactoryMCAJA().openSession();
		// Transaction trans = null;
		String sConsulta = "", sFecha = "", sHini, sHfin;
		;
		FechasUtil f = new FechasUtil();

		try {
			sFecha = f.formatDatetoString(dtFechaArqueo, "yyyy-MM-dd");
			sConsulta = " from Vmonedafactrec as v where v.id.caid = " + iCaid
					+ " and v.id.codsuc ='" + sCodsuc + "'";
			sConsulta += " and trim(v.id.codcomp) = '" + sCodcomp.trim() + "'";
			sConsulta += " and v.id.rmoneda = '" + sMoneda
					+ "' and v.id.fmoneda <> '" + sMoneda + "'";
			sConsulta += " and v.id.fecha = '" + sFecha
					+ "' and v.id.restado <>'A'";
			sConsulta += " and v.id.forarecibido > 0";

			sHini = f.formatDatetoString(dtHoraInicio, "HH.mm.ss");
			sHfin = f.formatDatetoString(dtHoraFin, "HH.mm.ss");
			sConsulta += " and v.id.hora >='" + sHini + "' and v.id.hora<='"
					+ sHfin + "'";
			sConsulta += " and v.id.restado <>'A' order by v.id.numrec";

			// trans = sesion.beginTransaction();
			lstRecibos = sesion.createQuery(sConsulta).list();
			// trans.commit();

		} catch (Exception error) {
			lstRecibos = null;
			error.printStackTrace();
		} finally {
			// sesion.close();
		}
		return lstRecibos;
	}

	/********
	 * obtener el registro de un recibo a partir de numero,caja,compañia y
	 * sucursal
	 ********************/
	public Recibo obtenerRegRecibo(int iNumrec, int iCaid, String sCodcomp, 	String sCodsuc, String tiporec ) {
		Recibo recibo = new Recibo();
		Session sesion = HibernateUtilPruebaCn.currentSession();
		Transaction trans = null;
		String consulta = "";

		try {
			trans = sesion.beginTransaction();
			consulta = " from Recibo as r where r.id.numrec='" + iNumrec
					+ "' and r.id.caid='" + iCaid + "' "
					+ " and r.id.codcomp = '" + sCodcomp
					+ "' and r.id.codsuc = '" + sCodsuc + "'" 
					+ " and r.id.tiporec = '"+tiporec+"'";

			recibo = (Recibo) sesion.createQuery(consulta).uniqueResult();
			trans.commit();

		} catch (Exception error) {
			error.printStackTrace();
		} finally {
			try {
				HibernateUtilPruebaCn.closeSession(sesion);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return recibo;
	}

	/*************** obtener el total de ingresos por tipo de recibo generado *******************************/
	public String obtenerTotalxTipoIngreso(int sCaid, String sCodcomp,
			String Moneda, String Tiporec) {
		String total = "", consulta = "";
		Session sesion = HibernateUtilPruebaCn.currentSession();
		Transaction trans = null;

		try {
			trans = sesion.beginTransaction();

			if (Moneda.equals("COR"))
				consulta = "select v.id.cord ";
			else
				consulta = "select v.id.usd ";

			// filtros de la consulta.
			consulta += " from Vtotaltipoingreso as v where v.id.caid ='"
					+ sCaid + "' and v.id.codcomp = '" + sCodcomp + "' "
					+ " and v.id.tiporec = '" + Tiporec + "' ";
			consulta += " and v.id.fecha = current_date";

			Object result = sesion.createQuery(consulta).uniqueResult();
			if (result != null) {
				total = result.toString();
			} else
				total = "0";

			trans.commit();

		} catch (Exception error) {
			error.printStackTrace();
		} finally {
			try {
				HibernateUtilPruebaCn.closeSession(sesion);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return total;
	}

	/*************** obtener lista con los recibos del dia por tipo de ingresos ***************************/
	public List obtenerRecibosxTipoIngreso(int sCaid, String sCodsuc,
			String sCodcomp, String Moneda, String Tiporec, Date dtFechaArqueo,
			Date dtHoraInicio, Date dtHoraFin, Session sesion) {
		String consulta = "", sFecha = "";
		List lstRec = new ArrayList();
		FechasUtil fechas = new FechasUtil();
		// Transaction trans = null;
		// Session sesion =
		// HibernateUtil.getSessionFactoryMCAJA().openSession();

		try {
			sFecha = fechas.formatDatetoString(dtFechaArqueo, "yyyy-MM-dd");
			consulta = " from Vrecibosxtipoingreso as v where v.id.caid ="
					+ sCaid;
			consulta += " and v.id.codcomp = '" + sCodcomp
					+ "' and v.id.codsuc = '" + sCodsuc + "'";
			consulta += " and v.id.moneda ='" + Moneda
					+ "' and v.id.tiporec ='" + Tiporec + "'";
			consulta += " and v.id.fecha = '" + sFecha + "'";

			// --- Agregar filtro de intervalo de horas en caso de arqueos
			// previos.
			String sHini = "", sHfin = "";
			sHini = fechas.formatDatetoString(dtHoraInicio, "HH.mm.ss");
			sHfin = fechas.formatDatetoString(dtHoraFin, "HH.mm.ss");
			consulta += " and v.id.hora >='" + sHini + "' and v.id.hora<='"
					+ sHfin + "'";
			consulta += " order by v.id.numrec";
			// trans = sesion.beginTransaction();
			lstRec = sesion.createQuery(consulta).list();
			// trans.commit();

		} catch (Exception error) {
			lstRec = null;
			System.out
					.println("Error en ReciboCtrl.obtenerRecibosxTipoIngreso "
							+ error);
		}
		return lstRec;
	}

	/*********** verificar que existan recibos en el día, por caja,comp y moneda **************************/
	public boolean consultarExistRecibosHoy(int sCaid, String sCodcomp,
			String sTipMoneda) {
		boolean existen = false;
		Session sesion = HibernateUtilPruebaCn.currentSession();
		Transaction trans = null;

		try {
			trans = sesion.beginTransaction();

			String sql = " select count(*) from "
					+ PropertiesSystem.ESQUEMA
					+ ".recibo as r inner join "
					+ PropertiesSystem.ESQUEMA
					+ ".recibodet as rd on r.numrec = rd.numrec "
					+ " and r.codcomp = rd.codcomp and r.codsuc = rd.codsuc and r.caid = rd.caid"
					+ " where r.codcomp = '" + sCodcomp + "' and rd.moneda = '"
					+ sTipMoneda + "' and r.caid = '" + sCaid + "'"; // and
																		// r.fecha
																		// =
																		// current_date";

			String cantSql = sesion.createSQLQuery(sql).uniqueResult()
					.toString();
			trans.commit();

			if (!cantSql.equals("0"))
				existen = true;

		} catch (Exception error) {
			System.out
					.println("error en ReciboCtrl.consultarExistRecibosHoy() "
							+ error);
		} finally {
			try {
				HibernateUtilPruebaCn.closeSession(sesion);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return existen;
	}

	/**********************************************************************************************/
	public int[] obtenerBatchxRecibo(int iNumrec, int iCaid, String sCodSuc,
			String sCodcomp, String sTipoDoc) {
		List lstRecibojde = null;
		String sql = "select distinct r.id.nobatch from Recibojde as r where r.id.numrec = "
				+ iNumrec
				+ " and r.id.caid = "
				+ iCaid
				+ " and r.id.codsuc = '"
				+ sCodSuc
				+ "' and r.id.codcomp = '"
				+ sCodcomp + "' and r.id.tipodoc = '" + sTipoDoc + "'";
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = null;
		int[] iNoBatch = null;
		try {
			tx = session.beginTransaction();
			lstRecibojde = session.createQuery(sql).list();
			tx.commit();

			iNoBatch = new int[lstRecibojde.size()];
			for (int i = 0; i < lstRecibojde.size(); i++) {
				iNoBatch[i] = Integer
						.parseInt((lstRecibojde.get(i)).toString());
			}
		} catch (Exception ex) {
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.obtenerBatchxRecibo: "
							+ ex);
		} finally {
			try {
				HibernateUtilPruebaCn.closeSession(session);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return iNoBatch;
	}

	public int[] obtenerBatchxRecibo2(int iNumrec, int iCaid, String sCodSuc,
			String sCodcomp, String sTiporec) {
		List lstRecibojde = null;
		String sql = new String();
		Session session = null;
		Transaction tx = null;
		int[] iNoBatch = null;
		boolean bNuevaSesionCaja = false;

		try {
			sql = "select distinct r.id.nobatch from Recibojde as r"
					+ " where r.id.numrec = " + iNumrec + " and r.id.caid = "
					+ iCaid + " and r.id.codsuc = '" + sCodSuc + "' and "
					+ "r.id.codcomp = '" + sCodcomp + "' and r.id.tiporec = '"
					+ sTiporec + "'";

			session = HibernateUtilPruebaCn.currentSession();
			if (session.getTransaction().isActive())
				tx = session.getTransaction();
			else {
				tx = session.beginTransaction();
				bNuevaSesionCaja = true;
			}
			lstRecibojde = session.createQuery(sql).list();

			if (lstRecibojde == null || lstRecibojde.isEmpty())
				return null;

			iNoBatch = new int[lstRecibojde.size()];
			for (int i = 0; i < lstRecibojde.size(); i++) {
				iNoBatch[i] = Integer
						.parseInt((lstRecibojde.get(i)).toString());
			}
		} catch (Exception ex) {
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.obtenerBatchxRecibo: ");
			ex.printStackTrace();
		} finally {
			if (bNuevaSesionCaja) {
				try {
					tx.commit();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				try {
					HibernateUtilPruebaCn.closeSession(session);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		return iNoBatch;
	}

	/******************************************************************************************************/
	/**
	 * Método: consultar los número de batch por documento pasando sesion como
	 * parámetro. Fecha: 08/01/2011 Nombre: Carlos Manuel Hernández Morrison.
	 **/
	public List<Object> obtenerBatchxRecibo2(int iNumrec, int iCaid,
			String sCodSuc, String sCodcomp, String sTiporec, Session sesion) {

		List<Object> lstRecibojde = null;
		String sql = "";

		try {
			sql = sql
					.concat("select distinct r.id.nobatch from Recibojde as r where r.id.numrec = ");
			sql = sql.concat(String.valueOf(iNumrec))
					.concat("and r.id.caid = ").concat(String.valueOf(iCaid));
			sql = sql.concat("	and r.id.codsuc = '").concat(sCodSuc)
					.concat("' and r.id.codcomp = '");
			sql = sql.concat(sCodcomp).concat("' and r.id.tiporec = '")
					.concat(sTiporec).concat("'");

			lstRecibojde = (ArrayList<Object>) sesion.createQuery(sql).list();

		} catch (Exception error) {
			error.printStackTrace();
		}
		return lstRecibojde;
	}

	/******************************************************************************************************/
	/**
	 * Método: Obtiene los recibos del día para una caja. Fecha: 08/01/2011
	 * Nombre: Carlos Manuel Hernández Morrison.
	 **/
	@SuppressWarnings("unchecked")
	public List<Recibo> getAllRecibosCaja(int iCaid, String sCodSuc,
			int iMaxResult) {
		List<Recibo> lstRecibos = null;
		List<Object> lstNumBatch = null;
		// String sql = "";
		Session session = null;
		Transaction tx = null;

		String sNobatch = "";

		try {
			session = HibernateUtilPruebaCn.currentSession();
			tx = session.beginTransaction();

			Criteria cr = session.createCriteria(Recibo.class);
			cr.add(Restrictions.eq("estado", ""));
			cr.add(Restrictions.eq("fecha", new Date()));
			cr.add(Restrictions.eq("id.caid", iCaid));
			cr.add(Restrictions.eq("id.codsuc", sCodSuc));
			cr.add(Restrictions.ne("id.tiporec", "FCV"));
			cr.addOrder(Order.desc("id.numrec"));
			if (iMaxResult > 0)
				cr.setMaxResults(iMaxResult);
			lstRecibos = cr.list();

			// && ======= Asignar Numero de batch asociados al recibo.
			Recibo r = null;
			for (int i = 0; i < lstRecibos.size(); i++) {
				r = lstRecibos.get(i);
				sNobatch = "";
				lstNumBatch = obtenerBatchxRecibo2(r.getId().getNumrec(),
						iCaid, sCodSuc, r.getId().getCodcomp(), r.getId()
								.getTiporec(), session);
				if (lstNumBatch != null && lstNumBatch.size() > 0) {
					for (Object ob : lstNumBatch)
						sNobatch = sNobatch.concat(String.valueOf(ob));
				}
				r.setNobatch(sNobatch);
				lstRecibos.set(i, r);
			}

			tx.commit();

		} catch (Exception ex) {
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.getAllRecibos: "
							+ ex);
		} finally {
			try {
				HibernateUtilPruebaCn.closeSession(session);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return lstRecibos;
	}

	/*********** obtienen una lista de todos los recibos de caja ********************************************************************************/
	public List getAllRecibos(int iCaid, String sCodSuc) {
		List lstRecibos = null;
		Date d = new Date();
		String sql = "from Recibo as r where r.fecha = :pFecha and r.estado = '' and r.id.caid = "
				+ iCaid
				+ " and r.id.codsuc = '"
				+ sCodSuc
				+ "' and r.id.tiporec <> 'FCV'";
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = null;
		Recibo recibo = null;
		int[] iNobatch = null;
		String sNobatch = "";
		try {
			tx = session.beginTransaction();
			lstRecibos = session.createQuery(sql).setDate("pFecha", d).list();
			tx.commit();

			for (int i = 0; i < lstRecibos.size(); i++) {
				recibo = (Recibo) lstRecibos.get(i);
				sNobatch = "";
				iNobatch = obtenerBatchxRecibo2(recibo.getId().getNumrec(),
						recibo.getId().getCaid(), recibo.getId().getCodsuc(),
						recibo.getId().getCodcomp(), recibo.getId()
								.getTiporec());
				for (int j = 0; j < iNobatch.length; j++) {
					sNobatch = sNobatch + " " + iNobatch[j];

				}
				recibo.setNobatch(sNobatch);
				lstRecibos.remove(i);
				lstRecibos.add(i, recibo);
			}
		} catch (Exception ex) {
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.getAllRecibos: "
							+ ex);
		} finally {
			try {
				HibernateUtilPruebaCn.closeSession(session);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return lstRecibos;
	}

	public List getAllRecibos(int iCaid, String sCodSuc, String sCodComp) {
		List lstRecibos = null;
		Date d = new Date();
		String sql = "from Recibo as r where r.id.fecha = :pFecha and r.id.estado = '' and r.id.caid = "
				+ iCaid
				+ " and r.id.codsuc = '"
				+ sCodSuc
				+ "' and r.id.codcomp = '"
				+ sCodComp
				+ "' and r.id.tiporec <> 'FCV'";
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			lstRecibos = session.createQuery(sql).setDate("pFecha", d).list();
			tx.commit();

		} catch (Exception ex) {
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.getAllRecibos: "
							+ ex);
		} finally {
			try {
				HibernateUtilPruebaCn.closeSession(session);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return lstRecibos;
	}

	/************ Obtiene los numeros de recibo contenidos en un rango de numeros recibos *****************************************************************************/
	public int[] getNumerosRecibos(int iCaid, String sCodSuc, String sNumRec) {
		int[] iNumeros = null;
		List lstRecibos = null;
		String sql = "select distinct r.id.numrec from Recibo as r where r.fecha = current_date and r.estado = '' and r.id.caid = "
				+ iCaid
				+ " and r.id.codsuc = '"
				+ sCodSuc
				+ "' and cast(r.id.numrec as string) like '"
				+ sNumRec.trim()
				+ "%'";
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			lstRecibos = session.createQuery(sql).list();
			tx.commit();
			iNumeros = new int[lstRecibos.size()];
			for (int i = 0; i < lstRecibos.size(); i++) {
				iNumeros[i] = Integer.parseInt(lstRecibos.get(i).toString());
			}
		} catch (Exception ex) {
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.getRecibosxNumero: "
							+ ex);
		} finally {
			try {
				HibernateUtilPruebaCn.closeSession(session);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return iNumeros;
	}

	/********* obtiene los numeros de factura parecidos al numero ingresado ***********************************************************************************/
	public int[] getNumerosFactura(int iCaid, String sCodSuc, String sNumfac) {
		int[] iNumeros = null;
		List lstRecibos = null;
		String sql = "select distinct r.id.numfac from Vrecfac as r where r.id.fecha = current_date and r.id.caid = "
				+ iCaid
				+ " and r.id.codsuc = '"
				+ sCodSuc
				+ "' and cast(r.id.numfac as string) like '"
				+ sNumfac.trim()
				+ "%'";
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			lstRecibos = session.createQuery(sql).list();
			tx.commit();
			iNumeros = new int[lstRecibos.size()];
			for (int i = 0; i < lstRecibos.size(); i++) {
				iNumeros[i] = Integer.parseInt(lstRecibos.get(i).toString());
			}
		} catch (Exception ex) {
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.getNumerosFactura: "
							+ ex);
		} finally {
			try {
				HibernateUtilPruebaCn.closeSession(session);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return iNumeros;
	}

	/***************
	 * OBTIENE LOS RECIBOS DEL DIA POR CAJA,COMPANIA,TIPO DE RECIBO Y Y TIPO DE
	 * BUSQUEDA
	 ***************************************************************************************************************/
	public List getRecibosxParametros(int iCaid, String sCodsuc,
			String sCodComp, String sTipoRecibo, String sParametro,
			int iTipousqueda) {
		List lstRecibos = null;
		String sql = "from Recibo as r where r.fecha = current_date and r.estado = '' and r.id.caid = "
				+ iCaid
				+ " and r.id.codsuc = '"
				+ sCodsuc
				+ "' and r.id.tiporec <> 'FCV' ";
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = null;
		Vrecfac vrecfac = null;
		Recibo recibo = null;
		ReciboId reciboId = null;
		int[] iNobatch = null;
		String sNobatch = "";
		try {
			switch (iTipousqueda) {
			case (1):// busqueda por nombre de cliente
				if (!sParametro.equals("")) {
					sql = sql + " and r.cliente like '%"
							+ sParametro.toUpperCase().trim() + "%'";
				}
				break;
			case (2):// busqueda por codigo de cliente
				if (!sParametro.equals("")) {
					sql = sql + " and cast(r.codcli as string) like '"
							+ sParametro.trim() + "%'";
				}
				break;
			case (3):// busqueda por numero de recibo
				if (!sParametro.equals("")) {
					sql = sql + " and cast(r.id.numrec as string) like '"
							+ sParametro.trim() + "%'";
				}
				break;
			case (4):// busqueda por numero de factura
				sql = "from Vrecfac as r where r.id.fecha = current_date and r.id.estado = '' and r.id.caid = "
						+ iCaid + " and r.id.codsuc = '" + sCodsuc + "' ";
				if (!sParametro.equals("")) {
					sql = sql + " and cast(r.id.numfac as string) like '"
							+ sParametro.trim() + "%'";
				}
				break;
			}
			if (!sCodComp.equals("01")) {
				sql = sql + " and r.id.codcomp = '" + sCodComp + "'";
			}
			if (!sTipoRecibo.equals("01")) {
				sql = sql + " and r.id.tiporec = '" + sTipoRecibo + "'";
			}
			//
			tx = session.beginTransaction();

			lstRecibos = session.createQuery(sql).setFirstResult(0).list();

			tx.commit();
			if (iTipousqueda == 4) {
				for (int i = 0; i < lstRecibos.size(); i++) {
					vrecfac = (Vrecfac) lstRecibos.get(i);
					recibo = new Recibo();
					reciboId = new ReciboId();

					reciboId.setCaid(vrecfac.getId().getCaid());
					reciboId.setNumrec(vrecfac.getId().getNumrec());
					reciboId.setCodcomp(vrecfac.getId().getCodcomp());
					reciboId.setCodsuc(vrecfac.getId().getCodsuc());
					reciboId.setTiporec(vrecfac.getId().getTiporec());

					recibo.setId(reciboId);

					recibo.setMontoapl(vrecfac.getId().getMontoapl());
					recibo.setMontorec(vrecfac.getId().getMontorec());
					recibo.setConcepto(vrecfac.getId().getConcepto());
					recibo.setFecha(vrecfac.getId().getFecha());
					recibo.setCliente(vrecfac.getId().getCliente());
					recibo.setCodcli(vrecfac.getId().getCodcli());
					recibo.setCajero(vrecfac.getId().getCajero());
					recibo.setHora(vrecfac.getId().getHora());
					recibo.setNumrecm(vrecfac.getId().getNumrecm());
					recibo.setRecjde(vrecfac.getId().getRecjde());
					recibo.setEstado(vrecfac.getId().getEstado());
					recibo.setMotivo(vrecfac.getId().getMotivo());
					recibo.setCodusera(vrecfac.getId().getCodusera());
					recibo.setHoramod(vrecfac.getId().getHoramod());
					recibo.setCoduser(vrecfac.getId().getCoduser());

					iNobatch = obtenerBatchxRecibo(recibo.getId().getNumrec(),
							recibo.getId().getCaid(), recibo.getId()
									.getCodsuc(), recibo.getId().getCodcomp(),
							recibo.getId().getTiporec());
					sNobatch = "";
					for (int j = 0; j < iNobatch.length; j++) {
						sNobatch = sNobatch + " " + iNobatch[j];

					}
					recibo.setNobatch(sNobatch);

					lstRecibos.remove(i);
					lstRecibos.add(i, recibo);
				}
			}
		} catch (Exception ex) {
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.getRecibosxParametros: "
							+ ex);
		} finally {
			try {
				HibernateUtilPruebaCn.closeSession(session);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return lstRecibos;
	}

	/*****
	 * OBTIENE EL NUMERO DE BATCH EN EL QUE FUE PROCESADO EL RECIBO POR
	 * CAJA,SUCURSAL,COMPANIA Y NUMERO DE RECIBO
	 ****************************************************************************************************************************************/
	public int leerNoBatchRecibo(int iCaid, String sCodSuc, String sCodComp,
			int iNumrec, String sTipodoc, String sTipoRec) {
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = null;
		String sql = "select distinct r.id.nobatch from Recibojde as r where r.id.caid = "
				+ iCaid
				+ " and r.id.codsuc = '"
				+ sCodSuc
				+ "'"
				+ " and r.id.codcomp = '"
				+ sCodComp
				+ "' and r.id.numrec = "
				+ iNumrec
				+ " and r.id.tipodoc = '"
				+ sTipodoc
				+ "' and r.id.tiporec = '" + sTipoRec + "'";
		int iNoBatch = 0;
		try {
			tx = session.beginTransaction();
			iNoBatch = Integer.parseInt(session.createQuery(sql).uniqueResult()
					.toString());
			tx.commit();
		} catch (Exception ex) {
			System.out
					.println("Se cpaturo una excepcion en ReciboCtrl.leerNoBatchRecibo: "
							+ ex);
		} finally {
			try {
				HibernateUtilPruebaCn.closeSession(session);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return iNoBatch;
	}

	/******* OBTIENE UNA LISTA DEL DETALLE DE RECIBO ************************************************************************************************************************************/
	public List<Recibodet> leerDetalleRecibo(int iCaid, String sCodSuc, String sCodComp, int iNumrec, String sTipoRec) {
		List<Recibodet> lstDetalleRecibo = null;
		
		try {
			
			String sql = "from Recibodet as r where r.id.caid = " + iCaid
					+ " and r.id.codsuc = '" + sCodSuc + "'"
					+ " and r.id.codcomp = '" + sCodComp + "' and r.id.numrec = "
					+ iNumrec + " and r.id.tiporec = '" + sTipoRec + "'";
			
			lstDetalleRecibo = ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, Recibodet.class, false);
			
			if(lstDetalleRecibo == null)
				lstDetalleRecibo = new ArrayList<Recibodet>();
			
			
		} catch (Exception e) { 
			LogCajaService.CreateLog("leerDetalleRecibo", "ERR", e.getMessage());
		}
		 
		return lstDetalleRecibo;
	}

	/*******************************************************************************************************************************************/
	/******* OBTIENE EL DETALLE DEL CAMBIO GENERADO EN EL RECIBO ************************************************************************************************************************************/
	public Cambiodet[] leerDetalleCambio(int iCaid, String sCodSuc, String sCodComp, int iNumrec) {
		
		Cambiodet[] cambio = null;
		
		try {
			
			String sql = "from Cambiodet as r where r.id.caid = " + iCaid
					+ " and r.id.codcomp = '" + sCodComp + "' and r.id.numrec = "
					+ iNumrec;
			
			LogCajaService.CreateLog("leerDetalleCambio", "QRY", sql);
			List<Cambiodet> cambios = ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, Cambiodet.class, false);
			
			if( cambios == null || cambios.isEmpty() )
				return null;
			
			cambio = new Cambiodet[cambios.size()];
			cambio = cambios.toArray(cambio);
			
		} catch (Exception e) {
			LogCajaService.CreateLog("leerDetalleCambio", "ERR", e.getMessage()); 
		}
		 
		return cambio;
	}

	/********* OBTIENE LAS FACTURAS QUE FUERON PROCESADAS EN UN RECIBO(RECIBOFAC) **********************************************************************************************************************************/
	public List leerFacturasRecibo(int iCaid, String sCodComp, int iNumrec,
			String sTiporec, int iCodcli) {

		List lstFacturasRecibo = new ArrayList(), lstRecibofac = null;
		FacturaxRecibo facturaRec = null;
		Hfactjdecon hfac = null;
		Recibofac recFac = null;
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = null;
		String sql = "from Recibofac as r where r.id.caid = " + iCaid
				+ " and r.id.codcomp = '" + sCodComp.trim()
				+ "' and r.id.numrec = " + iNumrec + " and r.id.tiporec = '"
				+ sTiporec + "' and r.if.codcli = " + iCodcli;
		try {
			tx = session.beginTransaction();
			lstRecibofac = session.createQuery(sql).list();
			tx.commit();
			for (int i = 0; i < lstRecibofac.size(); i++) {
				recFac = (Recibofac) lstRecibofac.get(i);
				hfac = getInfoFactura(recFac.getId().getNumfac(), sCodComp,
						recFac.getId().getTipofactura(), recFac.getId()
								.getCodunineg());
				JulianToCalendar fecha = new JulianToCalendar(hfac.getId()
						.getFecha());
				facturaRec = new FacturaxRecibo(hfac.getId().getNofactura(),
						hfac.getId().getTipofactura(),
						hfac.getId().getUnineg(), recFac.getMonto(), hfac
								.getId().getMoneda(), fecha.toString(), "");
				lstFacturasRecibo.add(facturaRec);
			}
		} catch (Exception ex) {
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.leerFacturasRecibo: "
							+ ex);
		} finally {
			try {
				HibernateUtilPruebaCn.closeSession(session);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return lstFacturasRecibo;
	}

	/**************************************************************************************************************/
	public List leerFacturasReciboCredito(int iCaid, String sCodComp,
			int iNumrec, String sTiporec, int iCodcli) {

		List lstFacturasRecibo = new ArrayList(), lstRecibofac = null;
		FacturaxRecibo facturaRec = null;
		Hfacturajde hfac = null;
		Recibofac recFac = null;
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = null;
		String sql = "";
		try {
			sql = "from Recibofac as r where r.id.caid = " + iCaid
					+ " and r.id.codcomp = '" + sCodComp.trim()
					+ "' and r.id.numrec = " + iNumrec
					+ " and r.id.tiporec = '" + sTiporec + "'"
					+ " and r.id.codcli = " + iCodcli;

			tx = session.beginTransaction();
			lstRecibofac = session.createQuery(sql).list();
			tx.commit();

			for (int i = 0; i < lstRecibofac.size(); i++) {
				recFac = (Recibofac) lstRecibofac.get(i);
				hfac = getInfoFacturaCredito(recFac.getId().getNumfac(),
						sCodComp, recFac.getId().getTipofactura(), recFac
								.getId().getPartida(), recFac.getId()
								.getCodunineg());
				JulianToCalendar fecha = new JulianToCalendar(hfac.getId()
						.getFecha());
				facturaRec = new FacturaxRecibo(hfac.getId().getNofactura(),
						hfac.getId().getTipofactura(),
						hfac.getId().getUnineg(), recFac.getMonto(), hfac
								.getId().getMoneda(), fecha.toString(), hfac
								.getId().getPartida());
				lstFacturasRecibo.add(facturaRec);
			}
		} catch (Exception ex) {
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.leerFacturasReciboCredito: "
							+ ex);
		} finally {
			try {
				HibernateUtilPruebaCn.closeSession(session);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return lstFacturasRecibo;
	}

	// /
	public List leerFacturasReciboCredito2(int iCaid, String sCodComp,
			int iNumrec, String sTiporec, int iCodcli) {

		List lstFacturasRecibo = new ArrayList(), lstRecibofac = null;
		FacturaxRecibo facturaRec = null;
		Hfacturajde hfac = null;
		Recibofac recFac = null;
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = null;

		String sql = "from Recibofac as r where r.id.caid = " + iCaid
				+ " and r.id.codcomp = '" + sCodComp.trim()
				+ "' and r.id.numrec = " + iNumrec + " and r.id.tiporec = '"
				+ sTiporec + "' and r.id.codcli = " + iCodcli;

		try {
			tx = session.beginTransaction();
			lstRecibofac = session.createQuery(sql).list();
			tx.commit();
			for (int i = 0; i < lstRecibofac.size(); i++) {
				recFac = (Recibofac) lstRecibofac.get(i);
				hfac = getInfoFacturaCredito(recFac.getId().getNumfac(),
						sCodComp, recFac.getId().getTipofactura(), recFac
								.getId().getPartida(), recFac.getId()
								.getCodunineg());
				JulianToCalendar fecha = new JulianToCalendar(hfac.getId()
						.getFecha());
				// facturaRec = new
				// FacturaxRecibo(hfac.getId().getNofactura(),hfac.getId().getTipofactura(),hfac.getId().getUnineg(),recFac.getMonto(),hfac.getId().getMoneda(),fecha.toString(),hfac.getId().getPartida());
				lstFacturasRecibo.add(hfac);
			}
		} catch (Exception ex) {
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.leerFacturasReciboCredito2: "
							+ ex);
		} finally {
			try {
				HibernateUtilPruebaCn.closeSession(session);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return lstFacturasRecibo;
	}

	/**************************************************************************************************************/
	public Hfactjdecon getInfoFactura(int iNumfac, String sCodComp,
			String sTipoDocumento, String sCodunineg) {
		Hfactjdecon factura = null;
		FacturaxRecibo facturaRec = null;
		Session session = null;
		Transaction tx = null;
		String sql = "from Hfactjdecon as hf where hf.id.nofactura = "
				+ iNumfac + " and hf.id.codcomp = '" + sCodComp.trim()
				+ "' and hf.id.tipofactura = '" + sTipoDocumento + "' and "
				+ "trim(hf.id.codunineg) = '" + sCodunineg + "'";
		try {
			session = HibernateUtilPruebaCn.currentSession();
			tx = session.beginTransaction();
			factura = (Hfactjdecon) session.createQuery(sql).uniqueResult();
			tx.commit();
		} catch (Exception ex) {
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.getInfoFactura: "
							+ ex);
		} finally {
			try {
				HibernateUtilPruebaCn.closeSession(session);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return factura;
	}

	/**************************************************************************************************************/
	public Hfacturajde getInfoFacturaCredito(int iNumfac, String sCodComp,
			String sTipoDocumento, String sPartida, String sCodunineg) {
		Hfacturajde factura = null;
		FacturaxRecibo facturaRec = null;
		Session session = null;
		Transaction tx = null;

		String sql = "from Hfacturajde as hf where hf.id.nofactura = "
				+ iNumfac + " and hf.id.codcomp = '" + sCodComp.trim()
				+ "' and hf.id.tipofactura = '" + sTipoDocumento
				+ "' and hf.id.partida = '" + sPartida
				+ "' and hf.id.ctotal > 0 and trim(codunineg) = '" + sCodunineg
				+ "'";
		try {

			session = HibernateUtilPruebaCn.currentSession();
			tx = session.beginTransaction();
			factura = (Hfacturajde) (session.createQuery(sql).list()).get(0);
			tx.commit();

		} catch (Exception ex) {
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.getInfoFacturaCredito: "
							+ ex);
		} finally {
			try {
				HibernateUtilPruebaCn.closeSession(session);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return factura;
	}

	/****** ACTUALIZA EL ESTADO DEL RECIBO A ANULADO ********************************************************************************************************/
	public boolean actualizarEstadoRecibo(Session session, Transaction tx,
			int iNumrec, int iCaid, String sCodsuc, String sCodcomp,
			String sCodUsera, String sMotivo, String sTipoRec) {
		boolean bActualizado = true;
		// Session session =
		// HibernateUtil.getSessionFactoryMCAJA().openSession();
		// Transaction tx = null;

		boolean bUnico = false;

		try {

			if (session == null) {
				session = HibernateUtilPruebaCn.currentSession();
				tx = session.beginTransaction();
				bUnico = true;
			}
			String sUpdate = "update Recibo as r set r.estado=:sEstado, "
					+ "r.codusera=:sCodUsera, r.motivo=:sMotivo where "
					+ "r.id.numrec=:iNumrec and r.id.caid=:iCaid and "
					+ "r.id.codcomp=:sCodcomp and r.id.codsuc=:sCodsuc "
					+ "and r.id.tiporec=:sTipoRec";

			Query q = session.createQuery(sUpdate);
			q.setString("sEstado", "A");
			q.setString("sCodUsera", sCodUsera);
			q.setString("sMotivo", sMotivo);

			q.setInteger("iNumrec", iNumrec);
			q.setInteger("iCaid", iCaid);
			q.setString("sCodcomp", sCodcomp);
			q.setString("sCodsuc", sCodsuc);
			q.setString("sTipoRec", sTipoRec);

			int rowCount = q.executeUpdate();
			if (rowCount == 0)
				bActualizado = false;

			if (bUnico)
				tx.commit();

		} catch (Exception ex) {
			bActualizado = false;
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.actualizarEstadoRecibo: "
							+ ex);
			ex.printStackTrace();
		} finally {
			try {
				if (bUnico)
					session.close();
			} catch (Exception ex2) {
				ex2.printStackTrace();
			}
			;
		}
		return bActualizado;
	}

	/****** BORRA EL REGISTRO CORRESPONDIENTE AL ENLACE RECIBO-FACTURA *********************************************************************************************************/
	public boolean actualizarEnlaceReciboFac(Session session, Transaction tx,
			Recibofac recibofac) {
		// Session session =
		// HibernateUtil.getSessionFactoryMCAJA().openSession();
		// Transaction tx = null;
		boolean bBorrado = true, activo = false;
		try {
			if (session == null) {
				session = HibernateUtilPruebaCn.currentSession();
				tx = session.beginTransaction();
				activo = true;
			}

			session.update(recibofac);
			if (activo)
				tx.commit();
		} catch (Exception ex) {
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.borrarEnlaceReciboFac: "
							+ ex);
			bBorrado = false;
		} finally {
			if (activo)
				try {
					session.close();
				} catch (Exception ex2) {
					ex2.printStackTrace();
				}
			;
		}
		return bBorrado;
	}

	public boolean actualizarEnlaceReciboFac(Connection cn, Hfacturajde hFac,
			int iNumrec, int iCaid) {
		String sql = "update " + PropertiesSystem.ESQUEMA
				+ ".recibofac set estado = 'A' where numrec = " + iNumrec
				+ " and caid = " + iCaid + " and tipofactura = '"
				+ hFac.getId().getTipofactura() + "' and partida = '"
				+ hFac.getId().getPartida() + "' and codcomp = '"
				+ hFac.getId().getCodcomp().trim() + "' and numfac = "
				+ hFac.getId().getNofactura();
		PreparedStatement ps = null;
		boolean bBorrado = true;
		try {

			ps = cn.prepareStatement(sql);
			int rs = ps.executeUpdate();
			if (rs == 0) {
				bBorrado = false;
			}
		} catch (Exception ex) {
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.borrarEnlaceReciboFac: "
							+ ex);
			bBorrado = false;
		}
		return bBorrado;
	}

	public boolean actualizarEnlaceReciboFac(Connection cn, Vf0311fn hFac,
			int iNumrec, int iCaid, String sCodcomp) {
		String sql = "update " + PropertiesSystem.ESQUEMA
				+ ".recibofac set estado = 'A' where numrec = " + iNumrec
				+ " and caid = " + iCaid + " and tipofactura = '"
				+ hFac.getId().getTipofactura() + "' and partida = '"
				+ hFac.getId().getPartida() + "' and codcomp = '"
				+ sCodcomp.trim() + "' and numfac = "
				+ hFac.getId().getNofactura();
		PreparedStatement ps = null;
		boolean bBorrado = true;
		try {

			ps = cn.prepareStatement(sql);
			int rs = ps.executeUpdate();
			if (rs == 0) {
				bBorrado = false;
			}
		} catch (Exception ex) {
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.borrarEnlaceReciboFac: "
							+ ex);
			bBorrado = false;
		}
		return bBorrado;
	}

	/******** OBTIENE LA INFORMACION DEL ENLACE ENTRE RECIBO DE CAJA Y JDE (RECIBOJDE) *****************************************************************************************************/
	public List getEnlaceReciboJDE(int iCaid, String sCodSuc, String sCodComp,
			int iNumrec, String sTipoDoc, String sTipoRec) {
		Session session3 = HibernateUtilPruebaCn.currentSession();
		Transaction tx3 = null;
		String sql = "from Recibojde as r where r.id.caid = " + iCaid
				+ " and r.id.codsuc = '" + sCodSuc + "'"
				+ " and r.id.codcomp = '" + sCodComp + "' and r.id.numrec = "
				+ iNumrec + " and r.id.tipodoc = '" + sTipoDoc
				+ "' and r.id.tiporec = '" + sTipoRec + "'";
		List lstRecibojde = null;
		try {
			tx3 = session3.beginTransaction();
			lstRecibojde = session3.createQuery(sql).list();
			tx3.commit();
		} catch (Exception ex) {
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.getEnlaceReciboJDE: "
							+ ex);
		} finally {
			try {
				HibernateUtilPruebaCn.closeSession(session3);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return lstRecibojde;
	}

	public List getEnlaceReciboJDE(int iCaid, String sCodSuc, String sCodComp,
			int iNumrec, String sTipoRec) {

		Session session3 = null;
		Transaction tx3 = null;
		String sql = "from Recibojde as r where r.id.caid = " + iCaid
				+ " and r.id.codsuc = '" + sCodSuc + "'"
				+ " and r.id.codcomp = '" + sCodComp + "' and r.id.numrec = "
				+ iNumrec + " and r.id.tiporec = '" + sTipoRec + "'";
		List lstRecibojde = null;

		try {
			session3 = HibernateUtilPruebaCn.currentSession();
			tx3 = session3.beginTransaction();
			lstRecibojde = session3.createQuery(sql).list();
			tx3.commit();

		} catch (Exception ex) {
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.getEnlaceReciboJDE: "
							+ ex);
		} finally {
			try {
				HibernateUtilPruebaCn.closeSession(session3);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return lstRecibojde;
	}

	/*******************************************************************************************************************************************/
	public List leerFacturasxRecibo(int iCaid, String sCodComp, int iNumrec,
			String sTiporec, int iCodcli) {
		List lstFacturasRecibo = new ArrayList(), lstRecibofac = null;
		Session session = null;
		Transaction tx = null;

		String sql = "from Recibofac as r where r.id.caid = " + iCaid
				+ " and r.id.codcomp = '" + sCodComp.trim()
				+ "' and r.id.numrec = " + iNumrec + " and r.id.tiporec = '"
				+ sTiporec + "' and r.id.codcli = " + iCodcli;

		try {
			session = HibernateUtilPruebaCn.currentSession();
			tx = session.beginTransaction();
			lstFacturasRecibo = session.createQuery(sql).list();
			tx.commit();

		} catch (Exception ex) {
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.leerFacturasRecibo: "
							+ ex);
		} finally {
			try {
				HibernateUtilPruebaCn.closeSession(session);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return lstFacturasRecibo;
	}

	/***** OBTIENE EL ESTADO DE UN DETERMINADO BATCH EN EL JDE ******************************************************************************************************************/
	public String leerEstadoBatch(Connection cn, int iNoBatch, String sTipoBatch) {
		String sEstado = null;
		PreparedStatement ps = null;
		String sql = "SELECT ICIST FROM " + PropertiesSystem.JDEDTA
				+ ".F0011 WHERE ICICU = " + iNoBatch + " and ICICUT = '"
				+ sTipoBatch + "'";
		try {
			ps = cn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				sEstado = new String(rs.getBytes("ICIST"), "Cp1047");

			}
		} catch (Exception ex) {
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.leerEstadoBatch: "
							+ ex);
		}
		return sEstado;
	}

	public String leerEstadoBatch(Connection cn, int iNoBatch) {
		String sEstado = null;
		PreparedStatement ps = null;
		String sql = "SELECT ICIST FROM " + PropertiesSystem.JDEDTA
				+ ".F0011 WHERE ICICU = " + iNoBatch;
		try {
			ps = cn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				sEstado = new String(rs.getBytes("ICIST"), "Cp1047");

			}
		} catch (Exception ex) {
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.leerEstadoBatch: "
							+ ex);
		}
		return sEstado;
	}

	/*************************************************************************************************************************/
	/************************************************************************************************************************/
	public Vrecfac getReciboFac(int iCaid, String sCodSuc, String sCodComp,
			int iNoFactura, String sTipoFactura, String sCodunineg,
			int iCodcli, int iFechafac) {
		Vrecfac recFac = null;
		Session session = null;

		try {
			session = HibernateUtilPruebaCn.currentSession();
			

			recFac = (Vrecfac) session
					.createCriteria(Vrecfac.class)
					.add(Restrictions.eq("id.caid", iCaid))
					.add(Restrictions.eq("id.codcomp", sCodComp))
					.add(Restrictions.eq("id.numfac", iNoFactura))
					.add(Restrictions.eq("id.tipofactura", sTipoFactura))
					.add(Restrictions.eq("id.estado", new String("")))
					.add(Restrictions.eq("id.codcli", iCodcli))
					.add(Restrictions.eq("id.fechafac", iFechafac))
					.add(Restrictions.sqlRestriction("trim(codunineg) ='"
							+ sCodunineg.trim() + "'")).setMaxResults(1)
					.uniqueResult();

		} catch (Exception ex) {
			LogCajaService.CreateLog("getReciboFac", "ERR", ex.getMessage());
		} 
		return recFac;
	}

	/**********************************************************************************************************/
	/**
	 * Guarda los datos de la cabecera del recibo de cualquier tipo de operación
	 * con su unidad de negocio
	 **/
	public boolean registrarRecibo(Session session, Transaction tx,
			int iNumRec, int iNumRecm, String sCodComp, double dMontoAplicar,
			double dMontoRec, double dCambio, String sConcepto, Date dFecha,
			Date dHora, int iCodCli, String sNomCli, String sCajero, int iCaId,
			String sCodSuc, String sCodUser, String sTipoRecibo, int iNodoco,
			String sTipodoco, int iRecjde, Date dtFechaM, String sCodunineg,
			String sMotivoCT, String monedaapl) {
		boolean registrado = false;
		try {

			BigDecimal bdAplicado = new BigDecimal( Double.toString(dMontoAplicar) );
			BigDecimal bdrecibido = new BigDecimal( Double.toString(dMontoRec) );

			if (bdAplicado.compareTo(BigDecimal.ZERO) == 0 || bdrecibido.compareTo(BigDecimal.ZERO) == 0) {
				
				error = new Exception( "@LOGCAJA: Monto aplicado/Recibido debe ser distinto de cero" );
				return false;
			}

			Recibo recibo = new Recibo();
			ReciboId reciboid = new ReciboId();

			reciboid.setNumrec(iNumRec);
			reciboid.setCodcomp(sCodComp.trim());
			reciboid.setCaid(iCaId);
			reciboid.setCodsuc(sCodSuc);
			reciboid.setTiporec(sTipoRecibo);
			recibo.setId(reciboid);

			recibo.setMontoapl(bdAplicado); // Monto Aplicar
			recibo.setMontorec(bdrecibido); // Monto Recibido
			recibo.setConcepto(sConcepto); // Concepto
			recibo.setFecha(dFecha); // Fecha
			recibo.setHora(dHora); // Hora
			recibo.setCodcli(iCodCli); // Cod Cliente
			recibo.setCliente(sNomCli); // Nombre Cliente
			recibo.setCajero(sCajero); // Cajero

			recibo.setNumrecm(iNumRecm); // Numero de recibo manual
			recibo.setEstado("");
			recibo.setHoramod(dHora);
			recibo.setCodusera("");
			recibo.setMotivo("");
			recibo.setCoduser(sCodUser);
			recibo.setNodoco(iNodoco);
			recibo.setTipodoco(sTipodoco);
			recibo.setRecjde(iRecjde);
			recibo.setFecham(dtFechaM);
			recibo.setCodunineg(sCodunineg);
			recibo.setMotivoct(sMotivoCT);
			recibo.setMonedaapl(monedaapl);

			LogCajaService.CreateLog("registrarRecibo", "HQRY", recibo);
			session.save(recibo);
			registrado = true;
			
		} catch (Exception ex) {
			LogCajaService.CreateLog("registrarRecibo", "ERR", ex.getMessage());
			
		}
		return registrado;
	}

	/********************** REGISTRAR EL RECIBO DE FACTURA DE CONTADO **********************************************************/
	public boolean registrarRecibo(Session session, Transaction tx,
			int iNumRec, int iNumRecm, String sCodComp, double dMontoAplicar,
			double dMontoRec, double dCambio, String sConcepto, Date dFecha,
			Date dHora, int iCodCli, String sNomCli, String sCajero, int iCaId,
			String sCodSuc, String sCodUser, String sTipoRecibo, int iNodoco,
			String sTipodoco, int iRecjde, Date dtFechaM) {
		boolean registrado = false;
		try {
			Recibo recibo = new Recibo();
			ReciboId reciboid = new ReciboId();

			reciboid.setNumrec(iNumRec);
			reciboid.setCodcomp(sCodComp.trim());
			reciboid.setCaid(iCaId);
			reciboid.setCodsuc(sCodSuc);
			reciboid.setTiporec(sTipoRecibo);

			recibo.setId(reciboid);

			recibo.setMontoapl(BigDecimal.valueOf(dMontoAplicar));// Monto
																	// Aplicar
			recibo.setMontorec(BigDecimal.valueOf(dMontoRec)); // Monto Recibido
			recibo.setConcepto(sConcepto); // Concepto
			recibo.setFecha(dFecha); // Fecha
			recibo.setHora(dHora); // Hora
			recibo.setCodcli(iCodCli); // Cod Cliente
			recibo.setCliente(sNomCli); // Nombre Cliente
			recibo.setCajero(sCajero); // Cajero

			recibo.setNumrecm(iNumRecm); // Numero de recibo manual
			recibo.setEstado("");
			recibo.setHoramod(dHora);
			recibo.setCodusera("");
			recibo.setMotivo("");
			recibo.setCoduser(sCodUser);
			recibo.setNodoco(iNodoco);
			recibo.setTipodoco(sTipodoco);
			recibo.setRecjde(iRecjde);
			recibo.setFecham(dtFechaM);

			session.save(recibo);
			// tx.commit();
			registrado = true;
		} catch (Exception ex) {
			error = new Exception(
					"@LOGCAJA: Error de sistema al grabar el Encabezado de recibo!!!");
			errorDetalle = ex;
			System.out
					.print("se capturo una excepcion en ReciboCtrl.registrarRecibo: "
							+ ex);
		}/*
		 * finally { session.close(); }
		 */
		return registrado;
	}

	/*********************************************************************************************************************/
	/********************** REGISTRAR EL DETALLE DEL RECIBO DE FACTURA DE CONTADO ******************************************************/
	public boolean registrarDetalleRecibo(Session session, Transaction tx,
			int iNumrec, int iNumrecm, String codcomp, List lstMetodosPago,
			int iCaId, String sCodSuc, String sTpoRec) {
		boolean registrado = false;
		Divisas divisas = new Divisas();

		MetodosPago mPago = null;
		try {
			
			
			for (int i = 0; i < lstMetodosPago.size(); i++) {
				MetodosPago mPagoTmp = (MetodosPago) lstMetodosPago.get(i);
				
				if( mPagoTmp.getMetodo().compareTo(MetodosPagoCtrl.TRANSFERENCIA)  == 0 ){
					if( mPagoTmp.getReferencia3().trim().length() > 8 )
						 mPagoTmp.setReferencia3(  mPagoTmp.getReferencia3().substring(0, 7) ) ;
				}
				if( mPagoTmp.getMetodo().compareTo(MetodosPagoCtrl.DEPOSITO)  == 0 ){
					if( mPagoTmp.getReferencia2().trim().length() > 8 )
						 mPagoTmp.setReferencia2(  mPagoTmp.getReferencia2().substring(0, 7) ) ;
				}
				
			}
			
			for (int i = 0; i < lstMetodosPago.size(); i++) {
				mPago = (MetodosPago) lstMetodosPago.get(i);
				Recibodet recibodet = new Recibodet();
				RecibodetId recibodetid = new RecibodetId();

				recibodetid.setNumrec(iNumrec);
				recibodetid.setCaid(iCaId);
				recibodetid.setCodsuc(sCodSuc);
				recibodetid.setCodcomp(codcomp);
				recibodetid.setMoneda(mPago.getMoneda());
				recibodetid.setMpago(mPago.getMetodo());
				recibodetid.setRefer1(mPago.getReferencia());
				recibodetid.setRefer2(mPago.getReferencia2());
				recibodetid.setRefer3(mPago.getReferencia3());
				recibodetid.setRefer4(mPago.getReferencia4());

				recibodet.setRefer5("");
				recibodet.setRefer6("");
				recibodet.setRefer7("");
				recibodet.setNombre("");

				if (mPago.getVmanual() != null
						&& mPago.getVmanual().compareTo("2") == 0) {

					recibodet.setRefer5(mPago.getReferencia5()==null?"":mPago.getReferencia5());
					recibodet.setRefer6(mPago.getReferencia6()==null?"":mPago.getReferencia6());
					recibodet.setRefer7(mPago.getReferencia7()==null?"":mPago.getReferencia7());
					recibodet.setNombre(mPago.getNombre());
				}

				recibodetid.setTiporec(sTpoRec);
				recibodet.setId(recibodetid);

				recibodet.setNumrecm(iNumrecm);
				recibodet.setTasa(BigDecimal.valueOf((divisas.formatStringToDouble(mPago.getTasa().toString()))));
				recibodet.setMonto(BigDecimal.valueOf((mPago.getMonto())));
				recibodet.setEquiv(BigDecimal.valueOf((mPago.getEquivalente())));
				recibodet.setVmanual((mPago.getVmanual() == null) ? "0" : mPago.getVmanual());
				recibodet.setCaidpos((mPago.getICaidpos() == 0) ? iCaId : mPago.getICaidpos());

				recibodet.setMarcatarjeta( mPago.getMarcatarjeta().toLowerCase() );
				recibodet.setCodigomarcatarjeta ( mPago.getCodigomarcatarjeta().toLowerCase()  );
				
				int clasificapormarca = 0 ;
				if(mPago.getMetodo().compareTo(MetodosPagoCtrl.TARJETA) == 0 ) {
					F55ca03 f03 = AfiliadoCtrl.obtenerAfiliadoxId( mPago.getReferencia() );
					if(f03 != null)
						clasificapormarca = f03.getId().getCxclasificamarca() ;
				}
				recibodet.setLiquidarpormarca(clasificapormarca);
				
				recibodet.setReferencenumber(mPago.getReferencenumber());
				recibodet.setDepctatran(mPago.getDepctatran());
				
				LogCajaService.CreateLog("registrarDetalleRecibo", "HQRY", recibodet);
				session.save(recibodet);
				registrado = true;
				
			}
		} catch (Exception ex) {
			LogCajaService.CreateLog("registrarDetalleRecibo", "ERR", ex.getMessage());
			registrado = false;
		}
		return registrado;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static int generarNumeroRecibo(int caid, String codcomp) {
		int numeroSiguiente = 0;

		try {

			Connection cn = As400Connection.getJNDIConnection("");
			
			LogCajaService.CreateLog("generarNumeroRecibo", "QRY", "{CALL \" + PropertiesSystem.ESQUEMA+ \".SP_GETNUMERORECIBO(?, ?, ?)}");
			
			CallableStatement callableStatement = cn.prepareCall("{CALL " + PropertiesSystem.ESQUEMA+ ".SP_GETNUMERORECIBO(?, ?, ?)}");

			callableStatement.setString(1, codcomp.trim());
			callableStatement.setInt(2, caid);
			callableStatement.registerOutParameter(3, java.sql.Types.NUMERIC);

			callableStatement.execute();

			Object out2 = callableStatement.getObject(3);
			String sResultado = out2.toString();

			try {
				numeroSiguiente = Integer.parseInt(sResultado.trim());
			} catch (Exception e) {
				LogCajaService.CreateLog("generarNumeroRecibo", "ERR", e.getMessage());
				e.printStackTrace();
				numeroSiguiente = 0;
			}

			cn.close();

		} catch (Exception e) {
			LogCajaService.CreateLog("generarNumeroRecibo", "ERR", e.getMessage());
			e.printStackTrace(); 
		}
		return numeroSiguiente;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	public static int generarNumeroRecibo1(int caid, String codcomp) {
		Session sesion = null;
		Transaction trans = null;
		boolean bNuevaCn = false;
		int numrec = 0;

		try {
			sesion = HibernateUtilPruebaCn.currentSession();
			if (sesion.getTransaction().isActive())
				trans = sesion.getTransaction();
			else {
				trans = sesion.beginTransaction();
				bNuevaCn = true;
			}
			F55ca014 comp = (F55ca014) sesion.createCriteria(F55ca014.class)
					.add(Restrictions.eq("id.c4id", caid))
					.add(Restrictions.eq("id.c4rp01", codcomp))
					.setMaxResults(1).uniqueResult();

			if (comp == null)
				return 0;

			numrec = (int) (comp.getId().getC4nncu() + 1);

			String sql = "update " + PropertiesSystem.ESQUEMA + ".F55ca014 "
					+ " set c4nncu = " + numrec + " WHERE c4id = " + caid
					+ " and c4rp01 = '" + codcomp + "'";

			sesion.createSQLQuery(sql).executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		} finally {
			if (bNuevaCn) {
				try {
					trans.commit();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					HibernateUtilPruebaCn.closeSession(sesion);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			sesion = null;
			trans = null;
		}
		return numrec;
	}

	/******************************************************************************************************************/
	/*************** OBTENER ULTIMO NUMERO DE RECIBO DE F55CA014 *******************************************************************/
	public int obtenerUltimoRecibo(Session session, Transaction tx, int iCaid,
			String sCodComp) {
		int ultimo = 0;
		Long result;		
		String sConsulta = "";
		boolean bUnico = false;
		try {
			sConsulta = "SELECT max(f55.id.c4nncu) FROM F55ca014 as f55 ";
			sConsulta += "where f55.id.c4id = " + iCaid
					+ " and trim(f55.id.c4rp01) = '" + sCodComp.trim() + "'";
			
			if (session == null) {
				session = HibernateUtilPruebaCn.currentSession();
			}

			LogCajaService.CreateLog("obtenerUltimoRecibo", "QRY", sConsulta);
			Object ob = session.createQuery(sConsulta).uniqueResult();
			
			if (ob != null) {
				result = (Long) ob;
				ultimo = result.intValue();
			} else {
				ultimo = 0;
			}
		} catch (Exception ex) {
			ultimo = 0;
			LogCajaService.CreateLog("obtenerUltimoRecibo", "ERR", ex.getMessage());
		}
		return ultimo;
	}

 
	public int obtenerUltimoRecibo(int iCaid, String sCodComp) {
		 
		int ultimo = -1;
		
		try{
			
			String sql = "SELECT max(f55.c4nncu) c4nncu FROM "
					+ PropertiesSystem.ESQUEMA
					+ ".F55ca014 as f55 where f55.c4id = " + iCaid
					+ " and trim(f55.c4rp01) = '" + sCodComp.trim() + "'";
	
			LogCajaService.CreateLog("obtenerUltimoRecibo", "QRY", sql);
			
			Object numero = ConsolidadoDepositosBcoCtrl.executeSqlQueryUnique(sql, null, true);
			
			if(numero == null)
				return ultimo = -1 ;
			else 
				return ultimo = Integer.parseInt(String.valueOf(numero));
		 

		} catch (Exception ex) {
			LogCajaService.CreateLog("obtenerUltimoRecibo", "ERR", ex.getMessage());			
		} 
		
		return ultimo;
	}

	/******************************************************************************************************************/
	/****************************** ACTUALIZA EL NUMERO DE RECIBO EN EL F55CA014 ************************************************************************************/
	public boolean actualizarNumeroRecibo(Connection cn, int iCajaId,
			String sCodCom, int iNumRecActual) {
		String sql = "update " + PropertiesSystem.ESQUEMA
				+ ".F55ca014 set c4nncu = " + iNumRecActual + " WHERE c4id = "
				+ iCajaId + " and c4rp01 = '" + sCodCom + "'";
		PreparedStatement ps = null;
		boolean actualizado = true;
		try {

			ps = cn.prepareStatement(sql);
			int rs = ps.executeUpdate();
			if (rs != 1) {
				actualizado = false;
			}
		} catch (Exception ex) {
			actualizado = false;
			System.out
					.print("Se capturo una excepcion en el actualizarNumeroRecibo: "
							+ ex);
		} finally {
			try {
				ps.close();
				// cn.close();
			} catch (Exception se2) {
				System.out
						.println("ERROR: Failed to close connection en ReciboContadoCtrl.actualizarNumeroRecibo: "
								+ se2);
			}
		}
		return actualizado;
	}

	public boolean actualizarNumeroRecibo(int iCajaId, String sCodCom,
			int iNumRecActual) {
		String sql = "update " + PropertiesSystem.ESQUEMA
				+ ".F55ca014 set c4nncu = " + iNumRecActual + " WHERE c4id = "
				+ iCajaId + " and c4rp01 = '" + sCodCom + "'";
		
		boolean actualizado = true;
		
		try {
			actualizado = ConsolidadoDepositosBcoCtrl.executeSqlQueryTx(null,sql);
		} catch (Exception ex) {
			actualizado = false;
			error = new Exception(
					"@LOGCAJA: No se puedo actualizar el numero de recibo de caja!!! "
							+ iNumRecActual + ";" + sCodCom + ";" + iCajaId);
			errorDetalle = ex;
			System.out
					.print("Se capturo una excepcion en el actualizarNumeroRecibo: "
							+ ex);
		} 
		return actualizado;
	}

	/******************************************************************************************************************/
	/******************** LLENAR ENLACE ENTRE RECIBO Y FACTURA *********************************************************************************/
	public boolean fillEnlaceReciboFac(Session session, Transaction tx,
			int iNumRec, String codcomp, int[] iNumFac, double[] Monto,
			String[] sTipoDoc, int iCaId, String sCodSuc[], String[] sPartida,
			String[] sCodunineg, String sTiporec, int iCodcli, int iFecha) {
		boolean filled = false;

		int nofactura = 0;
		try {
			for (int i = 0; i < iNumFac.length; i++) {
				if (iNumFac[i] > 0) {
					Recibofac recibofac = new Recibofac();
					RecibofacId recibofacid = new RecibofacId();
					nofactura = iNumFac[i];
					recibofacid.setNumfac(nofactura); // Numero factura
					recibofacid.setNumrec(iNumRec); // Numero Recibo
					recibofacid.setTipofactura(sTipoDoc[i]); // Tipo de
																// documento
					recibofacid.setCodcomp(codcomp); // Cod. Compañía
					recibofacid.setPartida(sPartida[i]); // Partida en blanco
															// para contado
					recibofacid.setCaid(iCaId);
					recibofacid.setCodsuc( CodeUtil.pad( sCodSuc[i].trim(), 5, "0") );
					recibofacid.setCodunineg(sCodunineg[i].trim());
					recibofacid.setTiporec(sTiporec);
					recibofacid.setCodcli(iCodcli);
					recibofacid.setFecha(iFecha);

					recibofac.setId(recibofacid); // Recibo
					recibofac.setMonto(BigDecimal.valueOf(Monto[i])); // Monto
					recibofac.setEstado("");
					
					LogCajaService.CreateLog("fillEnlaceReciboFac", "HQRY", recibofac);
					
					session.save(recibofac); // Factura
				}
			}

			filled = true;
		} catch (Exception ex) {
			LogCajaService.CreateLog("fillEnlaceReciboFac", "ERR", ex.getMessage());			
		}
		return filled;
	}

	/********************* GRABAR CAMBIO EN CAMBIODET ********************************************************************************/
	public boolean registrarCambio(Session session, Transaction tx,
			int iNumRec, String sCodComp, String sMoneda, double dCambio,
			int iCaId, String sCodSuc, BigDecimal bdTasa, String sTiporec) {
		boolean registrado = false;
		try {
			Cambiodet cambiodet = new Cambiodet();
			CambiodetId cambiodetId = new CambiodetId();

			cambiodetId.setCodcomp(sCodComp);
			cambiodetId.setNumrec(iNumRec);
			cambiodetId.setMoneda(sMoneda);
			cambiodetId.setCaid(iCaId);
			cambiodetId.setCodsuc(sCodSuc);
			cambiodetId.setTiporec(sTiporec);

			cambiodet.setCambio(BigDecimal.valueOf(dCambio));
			cambiodet .setTasa((bdTasa.compareTo(BigDecimal.ZERO) == 0) ? BigDecimal.ONE: bdTasa);
			cambiodet.setId(cambiodetId);

			LogCajaService.CreateLog("registrarCambio", "HQRY", cambiodet);
			session.save(cambiodet);
			registrado = true;
			
		} catch (Exception ex) {
			LogCajaService.CreateLog("registrarCambio", "ERR", ex.getMessage());
			registrado = false; 
			
		}
		return registrado;
	}

	/**********************************************************************************************************/
	/***************** OBTENER CUENTA DE METODO DE PAGO, MONEDA, COMPANIA Y CAJA ********************************/
	public F55ca011 obtenerCuenta(Session session, Transaction tx, int iCajaId,
			String sCodComp, String sCodMoneda, String sMetodo) {
		F55ca011 f55ca011 = null;
		// Session session =
		// HibernateUtil.getSessionFactoryMCAJA().openSession();
		// Transaction tx = null;
		try {
			// tx = session.beginTransaction();
			f55ca011 = (F55ca011) session
					.createQuery(
							"from F55ca011 f where f.id.c1id = :pCajaId and f.id.c1rp01 = :pCodComp and f.id.c1crcd = :pCodMon and f.id.c1ryin = :pMetodo")
					.setParameter("pCajaId", iCajaId)
					.setParameter("pCodComp", sCodComp)
					.setParameter("pCodMon", sCodMoneda)
					.setParameter("pMetodo", sMetodo).uniqueResult();
			// tx.commit();
		} catch (Exception ex) {
			System.out.print("=>Excepcion capturada en obtenerBatchActual: "
					+ ex);
		}/*
		 * finally{ session.close(); }
		 */
		return f55ca011;
	}

	/****************************************************************************************************/
	/************************ OBTENER ID DE CUENTA DEL F0901 ********************************************/
	public String obtenerIdCuenta(Session session, Transaction tx, String sMCU,
			String sOBJ, String sSUB) {
		String idCuenta = null;
		boolean bNuevaSesionCaja = false;

		try {
						
			String query = "select f.id.gmaid from Vf0901 f where "
					+ "trim(f.id.gmmcu) = '" + sMCU.trim()
					+ "' and trim(f.id.gmobj) " + " = '" + sOBJ.trim()
					+ "' and trim(f.id.gmsub) = '" + sSUB.trim() + "'";
			
			LogCajaService.CreateLog("obtenerIdCuenta", "QRY", query);
			idCuenta = (String) session.createQuery(query)
					.uniqueResult();

		} catch (Exception ex) { 
			LogCajaService.CreateLog("obtenerIdCuenta", "QRY", ex.getMessage());
			ex.printStackTrace();
		}
		return idCuenta;
	}

	/****************************************************************************************************/
	/******************* OBTENER CUENTA DE VENTA DE CONTADO DE CAJA ******************************************/
	public F55ca018 obtenerCuentaVentaContado(int iCodCaja, String sCodComp,
			String sCodUnineg) {
		F55ca018 f55ca018 = null;
		Session session = null;
		Transaction tx = null;
		String sql = "from F55ca018 as f where f.id.c8id = " + iCodCaja
				+ " and trim(f.id.c8rp01) = '" + sCodComp.trim()
				+ "' and trim(f.id.c8mcu) = '" + sCodUnineg.trim()
				+ "' and f.id.c8stat = 'A'";
		try {
			session = HibernateUtilPruebaCn.currentSession();
			tx = session.beginTransaction();
			f55ca018 = (F55ca018) session.createQuery(sql).uniqueResult();
			tx.commit();
		} catch (Exception ex) {
			System.out
					.println("Se capturo una excepcion en ReciboContadoCtrl.obtenerCuentaVentaContado: "
							+ ex);
		} finally {
			try {
				HibernateUtilPruebaCn.closeSession(session);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return f55ca018;
	}

	/*****************************************************************************************************/
	/************************ OBTENER NUMERO DE BATCH ACTUAL **************************************************/
	public int obtenerNoBatchActual(Session session, Transaction tx,
			Connection cn) {
		int iLastBatch = 0;
		try {
			iLastBatch = Integer
					.parseInt((session
							.createQuery("select f.id.nnn001 from Vf0002 f where trim(f.id.nnsy) = '00'")
							.uniqueResult()).toString());
		} catch (Exception ex) {
			System.out
					.print("=>Excepcion capturada en ReciboContadoCtrl.obtenerBatchActual: "
							+ ex);
		}
		return iLastBatch;
	}

	/****************************************************************************************************/
	/************************ ACTUALIZAR NUMERO DE BATCH *********************************************/
	public boolean actualizarNoBatch(Connection cn, int iNumeroBatch) {
		boolean bActualizado = false;
		PreparedStatement ps = null;
		String sql = "UPDATE " + PropertiesSystem.JDECOM
				+ ".F0002 SET NNN001 = " + (iNumeroBatch + 1)
				+ " WHERE TRIM(NNSY) = '00'";
		try {
			ps = cn.prepareStatement(sql);
			int rs = ps.executeUpdate();
			if (rs != 1) {
				bActualizado = false;
			}
			bActualizado = true;
		} catch (Exception ex) {
			bActualizado = false;
			System.out
					.print("=>Excepcion capturada en ReciboContadoCtrl.actualizarBatch: "
							+ ex);
		} finally {
			try {
				ps.close();
				// cn.close();
			} catch (Exception se2) {
				System.out
						.println("ERROR: Failed to close connection en ReciboContadoCtrl.actualizarBatch: "
								+ se2);
			}
		}
		return bActualizado;
	}

	/****************************************************************/
	/******
	 * Leer el número de documento
	 */
	public int leerNumeroDocumento() {
		int iNumDoc = 0;
		Session session = null;
		Transaction tx = null;
		try {
			session = HibernateUtilPruebaCn.currentSession();
			tx = session.beginTransaction();
			iNumDoc = Integer
					.parseInt((session
							.createQuery("select f.id.nnn002 from Vf0002 f where trim(f.id.nnsy) = '09'")
							.uniqueResult()).toString());
			tx.commit();
		} catch (Exception ex) {
			iNumDoc = 0;
			System.out
					.println("Se capturo una Excepcion en ReciboCtrl.leerNumeroDocumento: "
							+ ex);
		} finally {
			try {
				HibernateUtilPruebaCn.closeSession(session);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return iNumDoc;
	}

	/****************************************************************************************************/
	/************************ LEER NUMERO DE DOCUMENTO A USAR *********************************************/
	public int leerNumeroDocumento(Session session, Transaction tx) {
		int iNumDoc = 0;
		// Session session = HibernateUtilPruebaCn.currentSessionENS();
		// Transaction tx = null;
		try {
			// iNumDoc=tx = session.beginTransaction();
			iNumDoc = Integer
					.parseInt((session
							.createQuery("select f.id.nnn002 from Vf0002 f where trim(f.id.nnsy) = '09'")
							.uniqueResult()).toString());
			// tx.commit();
		} catch (Exception ex) {
			iNumDoc = 0;
			System.out
					.println("Se capturo una Excepcion en ReciboCtrl.leerNumeroDocumento: "
							+ ex);
		}
		return iNumDoc;
	}

	/**********************************************************************************************************/
	/************************ LEER NUMERO DE RECIBO DEL JDE A USAR *********************************************/
	public int leerNumeroReciboJDE(Session session, Transaction tx) {
		int iNumDoc = 0;
		// Session session = HibernateUtilPruebaCn.currentSessionENS();
		// Transaction tx = null;
		try {
			// iNumDoc=tx = session.beginTransaction();
			iNumDoc = Integer
					.parseInt((session
							.createQuery("select f.id.nnn005 from Vf0002 f where trim(f.id.nnsy) = '03'")
							.uniqueResult()).toString());
			// tx.commit();
		} catch (Exception ex) {
			iNumDoc = 0;
			System.out
					.println("Se capturo una Excepcion en ReciboCtrl.leerNumeroReciboJDE: "
							+ ex);
		}
		return iNumDoc;
	}

	/****************************************************************************************/
	/**
	 * Método: Guardar Registro en el GCPPRRDTA.F0011 para asientos de diario. :
	 * Versión para JD Edward's 9.2 Fecha: 15/08/2010 Nombre: Carlos Manuel
	 * Hernández Morrison.
	 **/
	public boolean registrarAsientoDiario(Date dtFechaAsiento, Connection cn,
			String sCodSuc, String sTipodoc, int iNoDocumento,
			double dLineaJDE, int iNoBatch, String sCuenta, String sIdCuenta,
			String sCodUnineg, String sCuentaObj, String sCuentaSub,
			String sTipoAsiento, String sMoneda, long iMonto, String sConcepto,
			String sUsuario, String sCodApp, BigDecimal dTasa,
			String sTipoCliente, String sObservacion, String sCodSucCuenta,
			String sGlsbl, String sGlsblt, String sGlbcrc, String sGlhco,
			String sGlcrrm) {
		boolean bRegistrado = true;
		String sNombrePc = null;
		String sql = "";
		PreparedStatement ps = null;
		Date dHora = new Date();
		SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Calendar cFecha = Calendar.getInstance();
			cFecha.setTime(dtFechaAsiento);
			CalendarToJulian fecha = new CalendarToJulian(cFecha);
			int iFecha = fecha.getDate();

			sNombrePc = "SERVER";// InetAddress.getLocalHost().getHostName();
			// obtener hora en enteros
			String[] sHora = (dfHora.format(dHora)).split(":");
			int iHora = Integer.parseInt(sHora[0] + sHora[1] + sHora[2]);
			// obtener partes de la fecha
			String[] sFecha = (sdf.format(dtFechaAsiento)).split("/");
			
			
			sCodUnineg = com.casapellas.util.CodeUtil.pad(sCodUnineg.trim(), 12," ");
			/*
			// agregar blancos a unineg
			if (sCodUnineg.length() == 2) {
				sCodUnineg = "          " + sCodUnineg;
				sCuenta = "          " + sCuenta;
			} else {
				sCodUnineg = "        " + sCodUnineg;
				sCuenta = "        " + sCuenta;
			}
			*/
			
			if (sConcepto.length() > 30) {
				sConcepto = sConcepto.substring(0, 29);
			}
			if (sTipoCliente.equals("E")) {
				sTipoCliente = "EMP";
			} else {
				sTipoCliente = "";
			}
			if (sNombrePc.length() > 9) {
				sNombrePc = sNombrePc.substring(0, 9);
			}
			if (sObservacion.length() > 30) {
				sObservacion = sObservacion.substring(0, 29);
			}

			
			sCodSuc = CodeUtil.pad( sCodSuc, 5, "0"); 
			sCodSucCuenta = CodeUtil.pad( sCodSucCuenta, 5, "0"); 
			sGlhco = CodeUtil.pad( sGlhco, 5, "0"); 
			
			sql = " INSERT INTO "
					+ PropertiesSystem.JDEDTA
					+ ".F0911 (GLKCO,GLDCT,GLDOC,GLDGJ,GLJELN,GLICU,GLICUT,GLDICJ,";
			sql += " GLDSYJ,GLTICU,GLCO,GLANI,GLAM,GLAID,GLMCU,GLOBJ,GLSUB, GLSBL,GLSBLT, GLLT,GLPN,";
			sql += " GLCTRY,GLFY,GLCRCD,GLAA,GLEXA,GLDKJ,GLDSVJ,GLTORG,GLUSER,GLPID,GLJOBN,GLUPMJ,GLUPMT,";
			sql += " GLCRR,GLGLC,GLEXR,GLBCRC,GLHCO,GLCRRM) VALUES(";

			sql += "'" + sCodSuc + "'," + "'" + sTipodoc + "'," + ""
					+ iNoDocumento + "," + "" + iFecha + "," + "" + dLineaJDE
					+ "," + "" + iNoBatch + "," + "'G'," + "" + iFecha + ","
					+ "" + iFecha + "," + "" + iHora + "," + "'"
					+ sCodSucCuenta + "'," + "'" + sCuenta + "'," + "'2',"
					+ "'" + sIdCuenta + "'," + "'" + sCodUnineg + "'," + "'"
					+ sCuentaObj + "'," + "'" + sCuentaSub + "'," + "'"
					+ sGlsbl + "'," + "'" + sGlsblt + "'," + "'" + sTipoAsiento
					+ "'," + "" + Integer.parseInt(sFecha[1]) + "," + "20,"
					+ "" + Integer.parseInt(sFecha[2].substring(2, 4)) + ","
					+ "'" + sMoneda + "'," + "" + iMonto + "," + "'"
					+ sConcepto + "'," + "" + iFecha + "," + "" + iFecha + ","
					+ "'" + sUsuario + "'," + "'" + sUsuario + "'," + "'"
					+ sCodApp + "'," + "'" + sNombrePc + "'," + "" + iFecha
					+ "," + "" + iHora + "," + "" + dTasa + "," + "'"
					+ sTipoCliente + "'," + "'" + sObservacion + "'," + "'"
					+ sGlbcrc + "'," + "'" + sGlhco + "'," + "'" + sGlcrrm
					+ "'" + ")";

			ps = cn.prepareStatement(sql);
			int rs = ps.executeUpdate();
			if (rs != 1) {
				bRegistrado = false;
			}

		} catch (Exception ex) {
			bRegistrado = false;
			errorDetalle = ex;
			error = new Exception(
					"@Error de aplicacion al guardar el asiento de diario@@ "
							+ error);
			if (ex.toString()
					.trim()
					.startsWith(
							"com.ibm.websphere.ce.cm.DuplicateKeyException:"))
				error = new Exception(
						"@Ya existe en JDE un documento con el numero de referencia: "
								+ iNoDocumento);

		} finally {
			try {
				ps.close();
				// cn.close();
			} catch (Exception se2) {
				errorDetalle = se2;
				error = new Exception(
						"@Error de aplicacion al guardar el asiento de diario@@ "
								+ se2);
				se2.printStackTrace();
			}
		}
		return bRegistrado;
	}
	
	
	public boolean registrarAsientoDiarioLogs( Session session, 
			String logsMsg,	Date dtFechaAsiento,  String sCodSuc,
			String sTipodoc, int iNoDocumento, double dLineaJDE, int iNoBatch,
			String sCuenta, String sIdCuenta, String sCodUnineg,
			String sCuentaObj, String sCuentaSub, String sTipoAsiento,
			String sMoneda, long iMonto, String sConcepto, String sUsuario,
			String sCodApp, BigDecimal dTasa, String sTipoCliente,
			String sObservacion, String sCodSucCuenta, String sGlsbl,
			String sGlsblt, String sGlbcrc, String sGlhco, String sGlcrrm, long iMontoFor) {

		boolean bRegistrado = true;
		SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		try {
			
			String fechaBatch = String.valueOf( FechasUtil.dateToJulian(dtFechaAsiento) );
			String horaBatch = new SimpleDateFormat("HHmmss").format(dtFechaAsiento);
			String sNombrePc = PropertiesSystem.ESQUEMA;
			
			String glctry = new SimpleDateFormat("yyyy").format(dtFechaAsiento).substring(0,2);
			String glfy   = new SimpleDateFormat("yyyy").format(dtFechaAsiento).substring(2,4);
			String glpn   = new SimpleDateFormat("MM").format(dtFechaAsiento);
			
			sCodUnineg = com.casapellas.util.CodeUtil.pad(sCodUnineg.trim(), 12," ");
			
			 
			if (sConcepto.length() > 30) {
				sConcepto = sConcepto.substring(0, 29);
			}
			if (sTipoCliente.equals("E")) {
				sTipoCliente = "EMP";
			} else {
				sTipoCliente = "";
			}
			if (sNombrePc.length() > 9) {
				sNombrePc = sNombrePc.substring(0, 9);
			}
			if (sObservacion.length() > 30) {
				sObservacion = sObservacion.substring(0, 29);
			}

			sCodSuc = CodeUtil.pad( sCodSuc.trim(), 5, "0"); 
			sCodSucCuenta = CodeUtil.pad( sCodSucCuenta.trim(), 5, "0"); 
			sGlhco = CodeUtil.pad( sGlhco.trim(), 5, "0");
			
			
			String glacr =  "0" ;
			if( sTipoAsiento.compareTo("AA") == 0 && dTasa.compareTo(BigDecimal.ZERO) == 1  ){
				//glacr = String.valueOf( new BigDecimal( String.valueOf(iMonto) ).divide(dTasa, 2, RoundingMode.HALF_UP).intValue() );
				glacr = String.valueOf( iMontoFor ) ;
			}
			
			
			String sqlInsert = "insert into @JDEDTA.F0911 ( @FIELDS_TO_INSERT ) VALUES (@VALUES_TO_INSERT) " ;
			
			String insertFields = DefaultJdeFieldsValues.F0911_CONTADO_INSERT_COLUMNS;
			String insertValues = DefaultJdeFieldsValues.F0911_CONTADO_INSERT_COLUMNS_VALUES;
			
			insertFields += ", " +  DefaultJdeFieldsValues.F0911_CONTADO_COLUMN_NAMES_DEFAULT;
			insertValues += ", " +  DefaultJdeFieldsValues.F0911_CONTADO_COLUMN_NAMES_DEFAULT_VALUES;
			
			insertValues = insertValues
			 .replace("@GLKCO@", sCodSuc)
			 .replace("@GLDCT@", sTipodoc)
			 .replace("@GLDOC@", String.valueOf( iNoDocumento) )
			 .replace("@GLDGJ@", fechaBatch)
			 .replace("@GLJELN@", String.valueOf( dLineaJDE ) )
			 .replace("@GLICU@", String.valueOf( iNoBatch ) ) 
			 .replace("@GLICUT@", valoresJdeIns[8])
			 .replace("@GLDICJ@", fechaBatch )
			 .replace("@GLDSYJ@", fechaBatch)
			 .replace("@GLTICU@", horaBatch)
			 .replace("@GLCO@", sCodSucCuenta)
			 .replace("@GLANI@", sCuenta)
			 .replace("@GLAM@", "2")
			 .replace("@GLAID@", sIdCuenta)
			 .replace("@GLMCU@", sCodUnineg)
			 .replace("@GLOBJ@", sCuentaObj)
			 .replace("@GLSUB@", sCuentaSub)
			 .replace("@GLSBL@", sGlsbl)
			 .replace("@GLSBLT@",sGlsblt)
			 .replace("@GLLT@", sTipoAsiento)
			 .replace("@GLPN@", glpn)
			 .replace("@GLCTRY@", glctry )
			 .replace("@GLFY@", glfy )
			 .replace("@GLCRCD@", sMoneda)
			 .replace("@GLAA@", String.valueOf( iMonto ) )
			 .replace("@GLEXA@",sConcepto )
			 .replace("@GLDKJ@", fechaBatch)
			 .replace("@GLDSVJ@", fechaBatch)
			 .replace("@GLTORG@", sUsuario )
			 .replace("@GLUSER@", sUsuario )
			 .replace("@GLPID@",  sCodApp)
			 .replace("@GLJOBN@", sNombrePc )
			 .replace("@GLUPMJ@", fechaBatch )
			 .replace("@GLUPMT@", horaBatch)
			 .replace("@GLCRR@", String.valueOf( dTasa ) )
			 .replace("@GLGLC@", sTipoCliente)
			 .replace("@GLEXR@", sObservacion)
			 .replace("@GLBCRC@", sGlbcrc )
			 .replace("@GLHCO@",  sGlhco)
			 .replace("@GLCRRM@", sGlcrrm)
			 .replace("@GLACR@",  glacr)
			 
			 .replace("@GLAN8@",  "0" )
			 .replace("@GLVINV@", "")
			 .replace("@GLIVD@",  "0" )
			 .replace("@GLPKCO@", "")
			 ;
			 
			 
			sqlInsert = sqlInsert
				.replace("@JDEDTA", PropertiesSystem.JDEDTA)
				.replace("@FIELDS_TO_INSERT", insertFields)
				.replace("@VALUES_TO_INSERT", insertValues);
			
			
			try {
				LogCajaService.CreateLog("registrarAsientoDiarioLogs", "QRY", sqlInsert);
				
				bRegistrado = ConsolidadoDepositosBcoCtrl.executeSqlQueryTx( session, sqlInsert );
			} catch (Exception e) {
				LogCajaService.CreateLog("registrarAsientoDiarioLogs", "ERR", e.getMessage());
				bRegistrado = false;
			}
			


		} catch (Exception ex) {
			LogCajaService.CreateLog("registrarAsientoDiarioLogs", "ERR", ex.getMessage());
			bRegistrado = false;
		}  
		
		return bRegistrado;
	}
	
	
	public boolean registrarAsientoDiarioLogs_SinUsar_(String logsMsg,
			Date dtFechaAsiento, Connection cn, String sCodSuc,
			String sTipodoc, int iNoDocumento, double dLineaJDE, int iNoBatch,
			String sCuenta, String sIdCuenta, String sCodUnineg,
			String sCuentaObj, String sCuentaSub, String sTipoAsiento,
			String sMoneda, long iMonto, String sConcepto, String sUsuario,
			String sCodApp, BigDecimal dTasa, String sTipoCliente,
			String sObservacion, String sCodSucCuenta, String sGlsbl,
			String sGlsblt, String sGlbcrc, String sGlhco, String sGlcrrm) {

		boolean bRegistrado = true;
		String sNombrePc = null;
		String sql = "";
		PreparedStatement ps = null;
		Date dHora = new Date();
		SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			
			Calendar cFecha = Calendar.getInstance();
			cFecha.setTime(dtFechaAsiento);
			CalendarToJulian fecha = new CalendarToJulian(cFecha);
			int iFecha = fecha.getDate();

			sNombrePc = PropertiesSystem.ESQUEMA;
			String[] sHora = (dfHora.format(dHora)).split(":");
			int iHora = Integer.parseInt(sHora[0] + sHora[1] + sHora[2]);

			String[] sFecha = (sdf.format(dtFechaAsiento)).split("/");

			
			sCodUnineg = com.casapellas.util.CodeUtil.pad(sCodUnineg.trim(), 12," ");
			
			
			/*
			if (sCodUnineg.length() == 2) {
				sCodUnineg = "          " + sCodUnineg;
				sCuenta = "          " + sCuenta;
			} else {
				sCodUnineg = "        " + sCodUnineg;
				sCuenta = "        " + sCuenta;
			}
			*/
			
			if (sConcepto.length() > 30) {
				sConcepto = sConcepto.substring(0, 29);
			}
			if (sTipoCliente.equals("E")) {
				sTipoCliente = "EMP";
			} else {
				sTipoCliente = "";
			}
			if (sNombrePc.length() > 9) {
				sNombrePc = sNombrePc.substring(0, 9);
			}
			if (sObservacion.length() > 30) {
				sObservacion = sObservacion.substring(0, 29);
			}

			sCodSuc = CodeUtil.pad( sCodSuc.trim(), 5, "0"); 
			sCodSucCuenta = CodeUtil.pad( sCodSucCuenta.trim(), 5, "0"); 
			sGlhco = CodeUtil.pad( sGlhco.trim(), 5, "0"); 
			
			sql = " INSERT INTO "
					+ PropertiesSystem.JDEDTA
					+ ".F0911 (GLKCO,GLDCT,GLDOC,GLDGJ,GLJELN,GLICU,GLICUT,GLDICJ,";
			sql += " GLDSYJ,GLTICU,GLCO,GLANI,GLAM,GLAID,GLMCU,GLOBJ,GLSUB, GLSBL,GLSBLT, GLLT,GLPN,";
			sql += " GLCTRY,GLFY,GLCRCD,GLAA,GLEXA,GLDKJ,GLDSVJ,GLTORG,GLUSER,GLPID,GLJOBN,GLUPMJ,GLUPMT,";
			sql += " GLCRR,GLGLC,GLEXR,GLBCRC,GLHCO,GLCRRM) VALUES(";

			sql += "'" + sCodSuc + "'," + "'" + sTipodoc + "'," + ""
					+ iNoDocumento + "," + "" + iFecha + "," + "" + dLineaJDE
					+ "," + "" + iNoBatch + "," + "'G'," + "" + iFecha + ","
					+ "" + iFecha + "," + "" + iHora + "," 
					+ "'" + sCodSucCuenta + "'," + "'" + sCuenta + "'," + "'2',"
					+ "'" + sIdCuenta + "'," + "'" + sCodUnineg + "'," + "'"
					+ sCuentaObj + "'," + "'" + sCuentaSub + "'," + "'"
					+ sGlsbl + "'," + "'" + sGlsblt + "'," + "'" + sTipoAsiento
					+ "'," + "" + Integer.parseInt(sFecha[1]) + "," + "20,"
					+ "" + Integer.parseInt(sFecha[2].substring(2, 4)) + ","
					+ "'" + sMoneda + "'," + "" + iMonto + "," + "'"
					+ sConcepto + "'," + "" + iFecha + "," + "" + iFecha + ","
					+ "'" + sUsuario + "'," + "'" + sUsuario + "'," + "'"
					+ sCodApp + "'," + "'" + sNombrePc + "'," + "" + iFecha
					+ "," + "" + iHora + "," + "" + dTasa + "," + "'"
					+ sTipoCliente + "'," + "'" + sObservacion + "'," + "'"
					+ sGlbcrc + "'," + "'" + sGlhco + "'," + "'" + sGlcrrm
					+ "'" + ")";

//			LogCrtl.sendLogInfo("(I) registrarAsientoDiario: Linea: "
//					+ dLineaJDE + " ||| " + logsMsg + " ||| <-------- ["
//					+ sdf.format(new Date()) + " : "
//					+ dfHora.format(new Date()) + "] ----- >");

			ps = cn.prepareStatement(sql);
			int rs = ps.executeUpdate();
			if (rs != 1) {
				bRegistrado = false;
			}

//			LogCrtl.sendLogInfo("(F) registrarAsientoDiario: Linea: "
//					+ dLineaJDE + " ||| " + logsMsg + " ||| <-------- ["
//					+ sdf.format(new Date()) + " : "
//					+ dfHora.format(new Date()) + "] ----- >");

		} catch (Exception ex) {
			bRegistrado = false;
			errorDetalle = ex;
			error = new Exception(
					"@Error de aplicacion al guardar el asiento de diario@@ "
							+ error);
			if (ex.toString()
					.trim()
					.startsWith(
							"com.ibm.websphere.ce.cm.DuplicateKeyException:"))
				error = new Exception(
						"@Ya existe en JDE un documento con el numero de referencia: "
								+ iNoDocumento);
			ex.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (Exception se2) {
				errorDetalle = se2;
				error = new Exception(
						"@Error de aplicacion al guardar el asiento de diario@@ "
								+ se2);
			}
		}
		return bRegistrado;
	}

	/**********************************************************************************************************/
	/***************
	 * REGISTRAR ASIENTO DE DIARIO EN EL "+PropertiesSystem.JDEDTA+".F0911
	 * (método para salidas CH)
	 ***************/
	public boolean registrarAsientoDiario11(Date dtFechaAsiento, Connection cn,
			String sCodSuc, String sTipodoc, int iNoDocumento,
			double dLineaJDE, int iNoBatch, String sCuenta, String sIdCuenta,
			String sCodUnineg, String sCuentaObj, String sCuentaSub,
			String sTipoAsiento, String sMoneda, int iMonto, String sConcepto,
			String sUsuario, String sCodApp, BigDecimal dTasa,
			String sTipoCliente, String sObservacion, String sCodSucCuenta,
			String sGlsbl, String sGlsblt) {
		boolean bRegistrado = true;
		String sNombrePc = null;
		String sql = "";
		PreparedStatement ps = null;
		Date dHora = new Date();
		SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Calendar cFecha = Calendar.getInstance();
			cFecha.setTime(dtFechaAsiento);
			CalendarToJulian fecha = new CalendarToJulian(cFecha);
			int iFecha = fecha.getDate();

			sNombrePc = "SERVER";// InetAddress.getLocalHost().getHostName();
			// obtener hora en enteros
			String[] sHora = (dfHora.format(dHora)).split(":");
			int iHora = Integer.parseInt(sHora[0] + sHora[1] + sHora[2]);
			// obtener partes de la fecha
			String[] sFecha = (sdf.format(dtFechaAsiento)).split("/");
			// agregar blancos a unineg
			if (sCodUnineg.length() == 2) {
				sCodUnineg = "          " + sCodUnineg;
				sCuenta = "          " + sCuenta;
			} else {
				sCodUnineg = "        " + sCodUnineg;
				sCuenta = "        " + sCuenta;
			}
			if (sConcepto.length() > 30) {
				sConcepto = sConcepto.substring(0, 29);
			}
			if (sTipoCliente.equals("E")) {
				sTipoCliente = "EMP";
			} else {
				sTipoCliente = "";
			}
			if (sNombrePc.length() > 9) {
				sNombrePc = sNombrePc.substring(0, 9);
			}
			if (sObservacion.length() > 30) {
				sObservacion = sObservacion.substring(0, 29);
			}

			sql = "INSERT INTO "
					+ PropertiesSystem.JDEDTA
					+ ".F0911 (GLKCO,GLDCT,GLDOC,GLDGJ,GLJELN,GLICU,GLICUT,GLDICJ,GLDSYJ,GLTICU,GLCO,GLANI,GLAM,GLAID,GLMCU,GLOBJ,GLSUB, GLSBL,GLSBLT, GLLT,GLPN,GLCTRY,GLFY,GLCRCD,GLAA,GLEXA,GLDKJ,GLDSVJ,GLTORG,GLUSER,GLPID,GLJOBN,GLUPMJ,GLUPMT,GLCRR,GLGLC,GLEXR) VALUES("
					+ "'000" + sCodSuc + "'," + "'" + sTipodoc + "'," + ""
					+ iNoDocumento + "," + "" + iFecha + "," + "" + dLineaJDE
					+ "," + "" + iNoBatch + "," + "'G'," + "" + iFecha + ","
					+ "" + iFecha + "," + "" + iHora + "," + "'000"
					+ sCodSucCuenta + "'," + "'" + sCuenta + "'," + "'2',"
					+ "'" + sIdCuenta + "'," + "'" + sCodUnineg + "'," + "'"
					+ sCuentaObj + "'," + "'" + sCuentaSub + "'," + "'"
					+ sGlsbl + "'," + "'" + sGlsblt + "'," + "'" + sTipoAsiento
					+ "'," + "" + Integer.parseInt(sFecha[1]) + "," + "20,"
					+ "" + Integer.parseInt(sFecha[2].substring(2, 4)) + ","
					+ "'" + sMoneda + "'," + "" + iMonto + "," + "'"
					+ sConcepto + "'," + "" + iFecha + "," + "" + iFecha + ","
					+ "'" + sUsuario + "'," + "'" + sUsuario + "'," + "'"
					+ sCodApp + "'," + "'" + sNombrePc + "'," + "" + iFecha
					+ "," + "" + iHora + "," + "" + dTasa + "," + "'"
					+ sTipoCliente + "'," + "'" + sObservacion + "'" + ")";
			ps = cn.prepareStatement(sql);
			int rs = ps.executeUpdate();
			if (rs != 1) {
				bRegistrado = false;
			}
		} catch (Exception ex) {
			bRegistrado = false;
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.registrarAsientoDiario: "
							+ ex);
		} finally {
			try {
				ps.close();
				// cn.close();
			} catch (Exception se2) {
				System.out
						.println("ERROR: Failed to close connection en ReciboCtrl.registrarAsientoDiario: "
								+ se2);
			}
		}
		return bRegistrado;
	}

	/**********************************************************************************************************/
	/****************
	 * REGISTRAR ASIENTO DE DIARIO EN EL "+PropertiesSystem.JDEDTA+".F0911
	 * (método básico JC)
	 ******************/
	public boolean registrarAsientoDiario222(Connection cn, String sCodSuc,
			int iNoDocumento, double dLineaJDE, int iNoBatch, String sCuenta,
			String sIdCuenta, String sCodUnineg, String sCuentaObj,
			String sCuentaSub, String sTipoAsiento, String sMoneda, int iMonto,
			String sConcepto, String sUsuario, String sCodApp,
			BigDecimal dTasa, String sTipoCliente, String sObservacion,
			String sCodSucCuenta) {
		boolean bRegistrado = true;
		String sNombrePc = null;
		String sql = "";
		PreparedStatement ps = null;
		Date dHora = new Date();
		SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Calendar cFecha = Calendar.getInstance();
			CalendarToJulian fecha = new CalendarToJulian(cFecha);
			int iFecha = fecha.getDate();
			sNombrePc = "SERVER";// InetAddress.getLocalHost().getHostName();
			// obtener hora en enteros
			String[] sHora = (dfHora.format(dHora)).split(":");
			int iHora = Integer.parseInt(sHora[0] + sHora[1] + sHora[2]);
			// obtener partes de la fecha
			Date dFecha = new Date();
			String[] sFecha = (sdf.format(dFecha)).split("/");
			// agregar blancos a unineg
			if (sCodUnineg.length() == 2) {
				sCodUnineg = "          " + sCodUnineg;
				sCuenta = "          " + sCuenta;
			} else {
				sCodUnineg = "        " + sCodUnineg;
				sCuenta = "        " + sCuenta;
			}
			if (sConcepto.length() > 30) {
				sConcepto = sConcepto.substring(0, 29);
			}
			if (sTipoCliente.equals("E")) {
				sTipoCliente = "EMP";
			} else {
				sTipoCliente = "";
			}
			if (sNombrePc.length() > 9) {
				sNombrePc = sNombrePc.substring(0, 9);
			}
			if (sObservacion.length() > 30) {
				sObservacion = sObservacion.substring(0, 29);
			}

			sql = "INSERT INTO "
					+ PropertiesSystem.JDEDTA
					+ ".F0911 (GLKCO,GLDCT,GLDOC,GLDGJ,GLJELN,GLICU,GLICUT,GLDICJ,GLDSYJ,GLTICU,GLCO,GLANI,GLAM,GLAID,GLMCU,GLOBJ,GLSUB,GLLT,GLPN,GLCTRY,GLFY,GLCRCD,GLAA,GLEXA,GLDKJ,GLDSVJ,GLTORG,GLUSER,GLPID,GLJOBN,GLUPMJ,GLUPMT,GLCRR,GLGLC,GLEXR) VALUES("
					+ "'000" + sCodSuc + "'," + "'P9'," + "" + iNoDocumento
					+ "," + "" + iFecha + "," + "" + dLineaJDE + "," + ""
					+ iNoBatch + "," + "'G'," + "" + iFecha + "," + "" + iFecha
					+ "," + "" + iHora + "," + "'000" + sCodSucCuenta + "',"
					+ "'" + sCuenta + "'," + "'2'," + "'" + sIdCuenta + "',"
					+ "'" + sCodUnineg + "'," + "'" + sCuentaObj + "'," + "'"
					+ sCuentaSub + "'," + "'" + sTipoAsiento + "'," + ""
					+ Integer.parseInt(sFecha[1]) + "," + "20," + ""
					+ Integer.parseInt(sFecha[2].substring(2, 4)) + "," + "'"
					+ sMoneda + "'," + "" + iMonto + "," + "'" + sConcepto
					+ "'," + "" + iFecha + "," + "" + iFecha + "," + "'"
					+ sUsuario + "'," + "'" + sUsuario + "'," + "'" + sCodApp
					+ "'," + "'" + sNombrePc + "'," + "" + iFecha + "," + ""
					+ iHora + "," + "" + dTasa + "," + "'" + sTipoCliente
					+ "'," + "'" + sObservacion + "'" + ")";
			ps = cn.prepareStatement(sql);
			int rs = ps.executeUpdate();
			if (rs != 1) {
				bRegistrado = false;
			}
		} catch (Exception ex) {
			bRegistrado = false;
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.registrarAsientoDiario: "
							+ ex);
		} finally {
			try {
				ps.close();
				// cn.close();
			} catch (Exception se2) {
				System.out
						.println("ERROR: Failed to close connection en ReciboCtrl.registrarAsientoDiario: "
								+ se2);
			}
		}
		return bRegistrado;
	}

	/*************************************************************************************************************/
	/**************** ACTUALIZAR EL NUMERO DE DOCUMENTO EN "+PropertiesSystem.JDECOM+".F0002 ****************************************/
	public boolean actualizarNumeroDocumento(Connection cn, int iNoDocumento) {
		boolean bActualizado = true;
		String sql = "UPDATE " + PropertiesSystem.JDECOM
				+ ".F0002 SET NNN002 = " + iNoDocumento + " where NNSY = '09'";
		PreparedStatement ps = null;
		try {
			ps = cn.prepareStatement(sql);
			int rs = ps.executeUpdate();
			if (rs != 1) {
				bActualizado = false;
			}
		} catch (Exception ex) {
			bActualizado = false;
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.actualizarNumeroDocumento: "
							+ ex);
		} finally {
			try {
				ps.close();
				// cn.close();
			} catch (Exception se2) {
				System.out
						.println("ERROR: Failed to close connection en ReciboCtrl.actualizarNumeroRecibo: "
								+ se2);
			}
		}
		return bActualizado;
	}

	/*************************************************************************************************************/
	/****************
	 * ACTUALIZAR EL NUMERO DE RECIBO DE JDE EN
	 * "+PropertiesSystem.JDECOM+".F0002
	 ****************************************/
	public boolean actualizarNumeroReciboJde(Connection cn, int iNoDocumento) {
		boolean bActualizado = true;
		String sql = "UPDATE " + PropertiesSystem.JDECOM
				+ ".F0002 SET NNN005 = " + iNoDocumento + " where NNSY = '03'";
		PreparedStatement ps = null;
		try {
			ps = cn.prepareStatement(sql);
			int rs = ps.executeUpdate();
			if (rs != 1) {
				bActualizado = false;
			}
		} catch (Exception ex) {
			bActualizado = false;
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.actualizarNumeroReciboJde: "
							+ ex);
		} finally {
			try {
				ps.close();
				// cn.close();
			} catch (Exception se2) {
				System.out
						.println("ERROR: Failed to close connection en ReciboCtrl.actualizarNumeroReciboJde: "
								+ se2);
			}
		}
		return bActualizado;
	}

	/*************************************************************************************************************/
	/******************* REGISTRAR TRANSACCION EN BATCH "+PropertiesSystem.JDEDTA+".F0011 ********************************************/
	public boolean registrarBatch(Connection cn, String sTipoBatch,
			int iNoBatch, int iTotalTransaccion, String sUsuario,
			int iCantDocs, String sIcpob) {
		boolean registrado = true;
		Date dHora = new Date();
		SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String sql = "";
		PreparedStatement ps = null;
		String sNombrePc = null;
		try {
			Calendar cFecha = Calendar.getInstance();
			CalendarToJulian fecha = new CalendarToJulian(cFecha);
			int iFecha = fecha.getDate();
			sNombrePc = "SERVER";// InetAddress.getLocalHost().getHostName();
			// obtener hora en enteros
			String[] sHora = (dfHora.format(dHora)).split(":");
			int iHora = Integer.parseInt(sHora[0] + sHora[1] + sHora[2]);
			// obtener partes de la fecha
			Date dFecha = new Date();
			String[] sFecha = (sdf.format(dFecha)).split("/");

			if (sNombrePc.length() > 9) {
				sNombrePc = sNombrePc.substring(0, 9);
			}

			/*
			 * sql = "INSERT INTO "+PropertiesSystem.JDEDTA+".F0011 VALUES(" +
			 * sentencia para la nueva version del jde "'G'," + "+"+iNoBatch+","
			 * + "''," + "''," + "0," + "'"+sUsuario+"'," + ""+iFecha+"," + "0,"
			 * + "'Y'," + "''," +
			 * 
			 * ""+iCantDocs+"," + "'"+sUsuario+"'," + "''," + "''," +
			 * ""+iTotalTransaccion+"," + "0," + "'"+sNombrePc+"'" + ")";
			 */

			sql = "INSERT INTO "
					+ PropertiesSystem.JDEDTA
					+ ".F0011 VALUES("
					+ // version viejita
					"'" + sTipoBatch + "'," + "+" + iNoBatch + "," + "'',"
					+ "''," + "0," + "'" + sUsuario + "'," + "" + iFecha + ","
					+ "0," + "'Y'," + "''," +

					"" + iTotalTransaccion + "," + "" + iCantDocs + "," + "'',"
					+ "'" + sIcpob + "'," + "''" + ")";

			ps = cn.prepareStatement(sql);
			int rs = ps.executeUpdate();
			if (rs != 1) {
				registrado = false;
			}
		} catch (Exception ex) {
			registrado = false;
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.registrarBatch: "
							+ ex);
		} finally {
			try {
				ps.close();
				// cn.close();
			} catch (Exception se2) {
				System.out
						.println("ERROR: Failed to close connection en ReciboCtrl.registrarBatch: "
								+ se2);
			}
		}
		return registrado;
	}

	/****************************************************************************************/
	/******************* REGISTRAR TRANSACCION EN BATCH "+PropertiesSystem.JDEDTA+".F0011 ********************************************/
	
	
	public boolean registrarBatchA92(Session session, Date fecha, CodigosJDE1 cjTipoRecibo, int iNoBatch, long iTotalTransaccion, String sUsuario, int iCantDocs, String jobname, CodigosJDE1 estadoBatch ) {
		boolean update = false;
		
		try {
			

			BatchControlF0011 f0011 = new BatchControlF0011(
					cjTipoRecibo.codigo(),
					String.valueOf(iNoBatch),
					"0",
					sUsuario,
					String.valueOf(iTotalTransaccion),
					String.valueOf( iCantDocs ),
					"0",
					fecha,
					jobname,
					estadoBatch.codigo()
				); 
				
			String sqlInsert =  f0011.insertStatement();
			
			LogCajaService.CreateLog("registrarBatchA92", "QRY", sqlInsert);			
			
			update = ConsolidadoDepositosBcoCtrl.executeSqlQueryTx( session, sqlInsert );
			
		} catch (Exception e) {
			LogCajaService.CreateLog("registrarBatchA92", "ERR", e.getMessage());
			e.printStackTrace(); 
		}
		return update;
	}
	
	
	public boolean registrarBatchA92Custom(Session session, Date fecha, String cjTipoRecibo, int iNoBatch, long iTotalTransaccion, String sUsuario, int iCantDocs, String jobname, String estadoBatch ) {
		boolean update = false;
		
		try {
			

			BatchControlF0011 f0011 = new BatchControlF0011(
					cjTipoRecibo,
					String.valueOf(iNoBatch),
					"0",
					sUsuario,
					String.valueOf(iTotalTransaccion),
					String.valueOf( iCantDocs ),
					"0",
					fecha,
					jobname,
					estadoBatch
				); 
				
			String sqlInsert =  f0011.insertStatement();
			
			LogCajaService.CreateLog("registrarBatchA92", "QRY", sqlInsert);			
			
			update = ConsolidadoDepositosBcoCtrl.executeSqlQueryTx( session, sqlInsert );
			
		} catch (Exception e) {
			LogCajaService.CreateLog("registrarBatchA92", "ERR", e.getMessage());
			e.printStackTrace(); 
		}
		return update;
	}

	
	
	public boolean registrarBatchA92(Connection cn, String sTipoBatch, int iNoBatch, long iTotalTransaccion, String sUsuario, int iCantDocs, String sIcpob) {
		boolean registrado = true;
		String sql = "";
		PreparedStatement ps = null;
		String sNombrePc = null;

		try {
			
			Calendar cFecha = Calendar.getInstance();
			CalendarToJulian fecha = new CalendarToJulian(cFecha);
			
			int iFecha = fecha.getDate();
			
			//sNombrePc = "SERVER"; InetAddress.getLocalHost().getHostName();
			sNombrePc = PropertiesSystem.ESQUEMA ;

			if (sNombrePc.length() > 9) {
				sNombrePc = sNombrePc.substring(0, 9);
			}

			sql = "INSERT INTO " + PropertiesSystem.JDEDTA + ".F0011 VALUES("
					+ "'" + sTipoBatch + "'," + iNoBatch + "," + "''," + "'',"
					+ "0," + "'" + sUsuario + "'," + "" + iFecha + "," + "0,"
					+ "'Y'," + "''," +

					"" + iCantDocs + "," + "'" + sUsuario + "'," + "'',"
					+ "''," + "" + iTotalTransaccion + "," + "0," + "'"
					+ sNombrePc + "'" + ")";

			ps = cn.prepareStatement(sql);
			int rs = ps.executeUpdate();

			if (rs != 1) {
				registrado = false;
			}

		} catch (Exception ex) {

			registrado = false;
			error = new Exception(
					"@LOGCAJA: error No se pudo grabar el encabezado del batch:"
							+ iNoBatch + ";" + sTipoBatch + ";");
			errorDetalle = ex;
 

		} finally {
			try {
				ps.close();
			} catch (Exception se2) {
				se2.printStackTrace();
			}
		}
		return registrado;
	}

	/****************************************************************************************/
	/**
	 * Método: Guardar Registro en el GCPPRRDTA.F0311 con parámetro de fecha del
	 * registro. (Variante del método RegistrarBatch de JC) Fecha: 27/07/2010
	 * Nombre: Carlos Manuel Hernández Morrison.
	 **/

	public boolean registrarBatch(Date dtFecha, Connection cn,
			String sTipoBatch, int iNoBatch, int iTotalTransaccion,
			String sUsuario, int iCantDocs, String sIcpob) {
		boolean registrado = true;
		String sql = "";
		PreparedStatement ps = null;
		String sNombrePc = null;
		FechasUtil f = new FechasUtil();
		int iFecha = 0;

		try {
			iFecha = f.obtenerFechaJulianaDia(dtFecha);
			sNombrePc = "SERVER";// InetAddress.getLocalHost().getHostName();
			if (sNombrePc.length() > 9) {
				sNombrePc = sNombrePc.substring(0, 9);
			}
			sql = "INSERT INTO " + PropertiesSystem.JDEDTA + ".F0011 VALUES("
					+ "'" + sTipoBatch + "'," + "+" + iNoBatch + "," + "'',"
					+ "''," + "0," + "'" + sUsuario + "'," + "" + iFecha + ","
					+ "0," + "'Y'," + "''," + "" + iTotalTransaccion + "," + ""
					+ iCantDocs + "," + "''," + "'" + sIcpob + "'," + "''"
					+ ")";

			ps = cn.prepareStatement(sql);
			int rs = ps.executeUpdate();
			if (rs != 1) {
				registrado = false;
			}
		} catch (Exception ex) {
			registrado = false;
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.registrarBatch: "
							+ ex);
		} finally {
			try {
				ps.close();
			} catch (Exception se2) {
				System.out
						.println("ERROR: Failed to close connection en ReciboCtrl.registrarBatch: "
								+ se2);
			}
		}
		return registrado;
	}

	/***************************************************************************************************************/

	public boolean registrarBatchA92(Date dtFecha, Connection cn,
			String sTipoBatch, int iNoBatch, int iTotalTransaccion,
			String sUsuario, int iCantDocs, String sIcpob) {
		boolean registrado = true;
		String sql = "";
		PreparedStatement ps = null;
		String sNombrePc = null;
		FechasUtil f = new FechasUtil();
		int iFecha = 0;

		try {
			iFecha = f.obtenerFechaJulianaDia(dtFecha);
			sNombrePc = "SERVER";// InetAddress.getLocalHost().getHostName();
			if (sNombrePc.length() > 9) {
				sNombrePc = sNombrePc.substring(0, 9);
			}
			sql = "INSERT INTO " + PropertiesSystem.JDEDTA + ".F0011 VALUES("
					+ "'" + sTipoBatch + "'," + "+" + iNoBatch + "," + "'',"
					+ "''," + "0," + "'" + sUsuario + "'," + "" + iFecha + ","
					+ "0," + "'Y'," + "''," +

					"" + iCantDocs + "," + "'" + sUsuario + "'," + "'',"
					+ "''," + "" + iTotalTransaccion + "," + "0," + "'"
					+ sNombrePc + "'" + ")";

			ps = cn.prepareStatement(sql);
			int rs = ps.executeUpdate();
			if (rs != 1) {
				registrado = false;
			}
		} catch (Exception ex) {
			registrado = false;
			ex.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (Exception se2) {
				se2.printStackTrace();
			}
		}
		return registrado;
	}

	/***************************************************************************************************************/
	/*********** LLENAR ENLACE ENTRE RECIBO DE CAJA Y RECIBO O DOCUMENTO DEL JDE *************************************/
	public boolean  fillEnlaceMcajaJde(Session session, Transaction tx,
			int iNumrec, String sCodComp, int iRecJde, int iNobatch, int iCaId,
			String sCodSuc, String sTipodoc, String sTiporec) {
		
		boolean bRegistrado = true;
		
		try {
			
			
			String strSQL = "INSERT INTO " + PropertiesSystem.ESQUEMA + ".RECIBOJDE(NUMREC, CODCOMP, RECJDE, NOBATCH, CAID, CODSUC, TIPODOC, TIPOREC) " + 
					"VALUES(" +iNumrec + ", '" + sCodComp + "', " + iRecJde + ", " + iNobatch + ", " + iCaId + ", '" + sCodSuc + "', '" + sTipodoc + "', '" + sTiporec + "')";
			
			LogCajaService.CreateLog("fillEnlaceMcajaJde", "SQL", strSQL);
			
			ConsolidadoDepositosBcoCtrl .executeSqlQueryTx( session, strSQL );
			
		} catch (Exception ex) {
			LogCajaService.CreateLog("fillEnlaceMcajaJde", "ERR", ex.getMessage());			
			bRegistrado = false; 
		}
		return bRegistrado;
	}
	
	
	public static boolean grabarAsociacionCajaJDE(Session session,  
			int iNumrec, String sCodComp, int iRecJde, int iNobatch, int iCaId,
			String sCodSuc, String sTipodoc, String sTiporec) {
		
		boolean bRegistrado = true;
		
		try {
			
			Recibojde recibojde = new Recibojde();
			RecibojdeId recibojdeid = new RecibojdeId();
			
			recibojdeid.setNumrec(iNumrec);
			recibojdeid.setCodcomp(sCodComp);
			recibojdeid.setNobatch(iNobatch);
			recibojdeid.setCaid(iCaId);
			recibojdeid.setCodsuc(sCodSuc);
			recibojdeid.setRecjde(iRecJde);
			recibojdeid.setTipodoc(sTipodoc);
			recibojdeid.setTiporec(sTiporec);
			recibojde.setId(recibojdeid);
			
			session.save(recibojde);
		
		} catch (Exception ex) {
			
			ex.printStackTrace(); 
			bRegistrado = false;
		}
		return bRegistrado;
	}

	/***************************************************************************************************************/
	public boolean borrarAsientodeDiario(Connection cn, int iNoDocumento,
			int iNoBatch) {
		boolean bBorrado = true;
		boolean activo = false;
		String sql = "delete from " + PropertiesSystem.JDEDTA
				+ ".F0911 where gldoc = " + iNoDocumento + " and glicu = "
				+ iNoBatch;
		PreparedStatement ps = null;
		try {
			if (cn == null) {
				cn = As400Connection.getJNDIConnection("DSMCAJA");
				activo = true;
			}
			ps = cn.prepareStatement(sql);
			ps.executeUpdate();

		} catch (Exception ex) {
			error = new Exception(
					"@F0911: Error al borrar el los asientos de diario para batch # "
							+ iNoBatch);
			errorDetalle = ex;
			System.out
					.println("Se capturo una excecpcion en ReciboContadoCtrl.borrarAsientodeDiario: "
							+ ex);
			bBorrado = false;
		} finally {
			try {
				ps.close();
				if (activo)
					cn.close();
			} catch (Exception se2) {
				error = new Exception(
						"@F0911: Error al borrar el los asientos de diario para batch # "
								+ iNoBatch);
				errorDetalle = se2;
				System.out
						.println("ERROR: Failed to close connection en ReciboContadoCtrl.borrarAsientodeDiario: "
								+ se2);
			}
		}
		return bBorrado;
	}

	/****************************************************************************************************************************/
	public boolean borrarBatch(Connection cn, int iNoBatch, String sTipoBatch) {
		boolean bBorrado = true, activo = false;
		String sql = "delete from " + PropertiesSystem.JDEDTA
				+ ".F0011 where icicu = " + iNoBatch + " and icicut = '"
				+ sTipoBatch + "'";
		PreparedStatement ps = null;
		try {

			if (cn == null) {
				cn = As400Connection.getJNDIConnection("DSMCAJA");
				activo = true;
			}
			ps = cn.prepareStatement(sql);
			ps.executeUpdate();

		} catch (Exception ex) {
			error = new Exception(
					"@F0011: Error al borrar encabezado de batch # " + iNoBatch
							+ " tipo: " + sTipoBatch);
			errorDetalle = ex;
			System.out
					.println("Se capturo una excepcion en ReciboContadoCtrl.borrarBatch: "
							+ ex);
			bBorrado = false;
		} finally {
			try {
				ps.close();
				if (activo)
					cn.close();
			} catch (Exception se2) {
				error = new Exception(
						"@F0011: Error al borrar encabezado de batch # "
								+ iNoBatch + " tipo: " + sTipoBatch);
				errorDetalle = se2;
				System.out
						.println("ERROR: Failed to close connection en ReciboContadoCtrl.borrarBatch: "
								+ se2);
			}
		}
		return bBorrado;
	}

	/*****************************************************************************************************************************************/
	public boolean grabarRC(Connection cn, String sCodsuc, int iCodcli,
			String sTipoDoc, int iNoFactura, String sPartida,
			int iFechaFactura, String sTipoRecibo, int iNoreciboJDE,
			int iFechaRecibo, String sTipoBatch, int iNobatch,
			double dMontoAplicar, String sModoMoneda, String sMoneda,
			double dTasa, double dMontoAplicarForaneo, String sGlOffset,
			String sIdCuenta, String sCodUnineg, String sTipoPago,
			String sTipoEntrada, String sConcepto, String sCashBasis,
			String sNomcli, String sUsuario, String sAplicacion,
			int iFechaVecto, String rppo, String rpdcto) {
		boolean registrado = true;
		String sql = "INSERT INTO "
				+ PropertiesSystem.JDEDTA
				+ ".F0311 (RPKCO,RPAN8,RPDCT,RPDOC,RPSFX,RPDIVJ,RPDCTM,RPDOCM,RPDMTJ,RPDGJ,RPFY,RPCTRY,RPPN,RPCO,RPICUT,RPICU,"
				+ "RPDICJ,RPPA8,RPAN8J,RPBALJ,RPPST,RPAG,RPAAP,RPCRRM,RPCRCD,RPCRR,RPACR,RPFAP,RPDSVJ,RPGLC,RPGLBA,RPAM,RPMCU,RPPTC,RPDDJ,RPDDNJ,"
				+ "RPTRTC,RPRMK,RPALT6,RPALPH,RPATE,RPATR,RPATP,RPATO,RPATPR,RPAT1,RPAT2,RPAT3,RPAT4,RPAT5,RPTORG,RPUSER,RPPID,RPUPMJ,RPUPMT,RPJOBN,"
				+ "RPAC01,RPAC02,RPAC03,RPAC04,RPAC05,RPAC06,RPAC07,RPAC08,RPAC09,RPAC10,RPPO,RPDCTO) VALUES(";
		PreparedStatement ps = null;
		Date dHora = new Date();
		SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String sNombrePc = null;
		EmpleadoCtrl empCtrl = new EmpleadoCtrl();
		Vf0101 vf0101 = null;
		Divisas d = new Divisas();
		String sCadValida = "";
		try {
			Calendar cFecha = Calendar.getInstance();
			CalendarToJulian fecha = new CalendarToJulian(cFecha);
			int iFecha = fecha.getDate();
			sNombrePc = "SERVER";// InetAddress.getLocalHost().getHostName();
			// obtener hora en enteros
			String[] sHora = (dfHora.format(dHora)).split(":");
			int iHora = Integer.parseInt(sHora[0] + sHora[1] + sHora[2]);
			// obtener partes de la fecha
			Date dFecha = new Date();
			String[] sFecha = (sdf.format(dFecha)).split("/");
			
			
			/*
			// agregar blancos a unineg
			if (sCodUnineg.length() == 2) {
				sCodUnineg = "          " + sCodUnineg;
			} else {
				sCodUnineg = "        " + sCodUnineg;
			}
			*/
			
			sCodUnineg = com.casapellas.util.CodeUtil.pad(sCodUnineg.trim(), 12," ");
			
			if (sConcepto.length() > 30) {
				sConcepto = sConcepto.substring(0, 29);
			}
			if (sNombrePc.length() > 9) {
				sNombrePc = sNombrePc.substring(0, 9);
			}
			
			
			// BUSCAR INFO DEL CLIENTE
			vf0101 = empCtrl.buscarEmpleadoxCodigo2(iCodcli);
			// validar comilla simple
			sCadValida = d.remplazaCaractEspeciales(sNomcli, "'", "-");
			if (!sCadValida.equals("")) {
				sNomcli = sCadValida;
			}
			if (rppo == null)
				rppo = "";

			if (rpdcto == null)
				rpdcto = "";

			sql = sql + "'" + sCodsuc + "'," + iCodcli + "," + "'" + sTipoDoc
					+ "'," + iNoFactura + "," + "'" + sPartida + "',"
					+ iFechaFactura + "," + "'" + sTipoRecibo + "',"
					+ iNoreciboJDE + "," + iFechaRecibo + "," + iFechaRecibo
					+ "," + "" + Integer.parseInt(sFecha[2].substring(2, 4))
					+ "," + "20," + "" + Integer.parseInt(sFecha[1]) + ","
					+ "'" + sCodsuc + "'," + "'" + sTipoBatch + "'," + iNobatch
					+ "," + iFechaRecibo + "," + iCodcli + "," + iCodcli
					+ ",'Y'," + "'P'," + d.pasarAenteroLong(dMontoAplicar * -1)
					+ ",0," + "'" + sModoMoneda + "'," + "'" + sMoneda + "',"
					+ dTasa + "," + d.pasarAenteroLong(dMontoAplicarForaneo * -1)
					+ ",0," + iFechaRecibo + "," + "'" + sGlOffset + "'," + "'"
					+ sIdCuenta + "'," + "'2'," + "'" + sCodUnineg + "'," + "'"
					+ sTipoPago + "'," + iFechaVecto + "," + iFechaVecto + ","
					+ "'" + sTipoEntrada + "'," + "'" + sConcepto + "'," + "'"
					+ sCashBasis + "'," + "'" + sNomcli + "'," + "'"
					+ vf0101.getId().getAbate() + "'," + "'"
					+ vf0101.getId().getAbatr() + "'," + "'"
					+ vf0101.getId().getAbatp() + "'," + "'Y'," + "'"
					+ vf0101.getId().getAbatpr() + "'," + "'"
					+ vf0101.getId().getAbat1() + "'," + "'"
					+ vf0101.getId().getAbat2() + "'," + "'"
					+ vf0101.getId().getAbat3() + "'," + "'"
					+ vf0101.getId().getAbat4() + "'," + "'"
					+ vf0101.getId().getAbat5() + "'," + "'" + sUsuario + "',"
					+ "'" + sUsuario + "'," + "'" + sAplicacion + "'," + iFecha
					+ "," + iHora + "," + "'" + sNombrePc + "'," + "'"
					+ vf0101.getId().getAbac01() + "'," + "'"
					+ vf0101.getId().getAbac02() + "'," + "'"
					+ vf0101.getId().getAbac03() + "'," + "'"
					+ vf0101.getId().getAbac04() + "'," + "'"
					+ vf0101.getId().getAbac05() + "'," + "'"
					+ vf0101.getId().getAbac06() + "'," + "'"
					+ vf0101.getId().getAbac07() + "'," + "'"
					+ vf0101.getId().getAbac08() + "'," + "'"
					+ vf0101.getId().getAbac09() + "'," + "'"
					+ vf0101.getId().getAbac10() + "'," + "'" + rppo + "',"
					+ "'" + rpdcto + "'" + ")";
			ps = cn.prepareStatement(sql);
			int rs = ps.executeUpdate();
			if (rs != 1) {
				registrado = false;
			}
		} catch (Exception ex) {
			registrado = false;
			error = new Exception(
					"@LOGCAJA: No se pudo grabar el Recibo en el JDE!!!;factura:"
							+ iNoFactura + ";tipo:" + sTipoDoc + ";Partida:"
							+ sPartida + ";Suc:" + sCodsuc);
			errorDetalle = ex;
			ex.printStackTrace(); 
			
		} finally {
			try {
				ps.close();
				 
			} catch (Exception se2) {
				se2.printStackTrace(); 
			}
		}
		return registrado;
	}

	/*** MODIFICAR FACTURA EN EL F0311 ***/
	public boolean aplicarMontoF0311(Connection cn, String sEstadoPago,
			double dMontopendienteDom, double dMontoPendienteForaneo,
			String sUsuario, String sCodSuc, String sTipoFactura,
			int iNofactura, String sPartida, int iCodclie) {
		boolean registrado = true;
		String sql = "";
		PreparedStatement ps = null;
		Date dHora = new Date();
		SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Divisas d = new Divisas();
		try {
			Calendar cFecha = Calendar.getInstance();
			CalendarToJulian fecha = new CalendarToJulian(cFecha);
			int iFecha = fecha.getDate();
			// obtener hora en enteros
			String[] sHora = (dfHora.format(dHora)).split(":");
			int iHora = Integer.parseInt(sHora[0] + sHora[1] + sHora[2]);
			// obtener partes de la fecha
			Date dFecha = new Date();
			String[] sFecha = (sdf.format(dFecha)).split("/");
			sql = "UPDATE " + PropertiesSystem.JDEDTA + ".F0311 SET RPPST = '"
					+ sEstadoPago + "', RPAAP = "
					+ d.pasarAentero(dMontopendienteDom) + ",RPFAP = "
					+ d.pasarAentero(dMontoPendienteForaneo) + ","
					+ "RPUSER = '" + sUsuario + "', RPUPMJ = " + iFecha
					+ ", RPUPMT = " + iHora + " WHERE RPKCO = '" + sCodSuc
					+ "' AND RPDCT ='" + sTipoFactura + "'" + " AND RPDOC = "
					+ iNofactura + " AND RPSFX = '" + sPartida
					+ "' AND TRIM(RPDCTM) = '' AND RPDOCM = 0 AND RPAN8 = "
					+ iCodclie;
			ps = cn.prepareStatement(sql);
			int rs = ps.executeUpdate();
			if (rs != 1) {
				error = new Exception(
						"@LOGCAJA: No encuentro registro de factura original !!! Factura:"
								+ iNofactura + ";Partida:" + sPartida
								+ ";Sucursal:" + sCodSuc + "  ");
				registrado = false;
			}/*
			 * else{ cn.commit(); }
			 */
		} catch (Exception ex) {
			registrado = false;
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.aplicarMontoF0311: "
							+ ex);
			error = new Exception(
					"@LOGCAJA: No se pudo aplicar el monto a factura!!!nofactura:"
							+ iNofactura + ";Partida:" + sPartida
							+ ";Sucursal:" + sCodSuc + "  ");
			errorDetalle = ex;
		} finally {
			try {
				ps.close();
				// cn.close();
			} catch (Exception se2) {
				System.out
						.println("ERROR: Failed to close connection en ReciboCtrl.aplicarMontoF0311: "
								+ se2);
			}
		}
		return registrado;
	}

	public boolean aplicarMontoF0311_fdc(Connection cn, double dMonto,
			String sMoneda, String sMonedaBase, String sUsuario,
			String sCodSuc, String sTipoFactura, int iNofactura,
			String sPartida, int iCodclie) {
		boolean lExito = false;
		String sql = "", sTmp;
		PreparedStatement ps = null;
		Date dHora = new Date();
		SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Divisas d = new Divisas();
		int iRegs = 0, iPendiente = 0;

		try {
			Calendar cFecha = Calendar.getInstance();
			CalendarToJulian fecha = new CalendarToJulian(cFecha);
			int iFecha = fecha.getDate();
			// obtener hora en enteros
			String[] sHora = (dfHora.format(dHora)).split(":");
			int iHora = Integer.parseInt(sHora[0] + sHora[1] + sHora[2]);
			// obtener partes de la fecha
			Date dFecha = new Date();
			// String[] sFecha = (sdf.format(dFecha)).split("/");

			if (sMoneda.equals(sMonedaBase)) {
				sTmp = " RPAAP = RPAAP - ";
			} else {
				sTmp = " RPFAP = RPFAP - ";
			}

			sql = "UPDATE " + PropertiesSystem.JDEDTA + ".F0311 SET " + sTmp
					+ d.pasarAentero(dMonto) + ", " + "RPUSER = '" + sUsuario
					+ "', RPUPMJ = " + iFecha + ", RPUPMT = " + iHora
					+ " WHERE RPKCO = '" + sCodSuc + "' AND RPDCT ='"
					+ sTipoFactura + "'" + " AND RPDOC = " + iNofactura
					+ " AND RPSFX = '" + sPartida
					+ "' AND TRIM(RPDCTM) = '' AND RPDOCM = 0 AND RPAN8 = "
					+ iCodclie;

			ps = cn.prepareStatement(sql);
			iRegs = ps.executeUpdate();
			if (iRegs == 1) {
				if (sMoneda.equals(sMonedaBase)) {
					sTmp = "RPAAP";
				} else {
					sTmp = "RPFAP";
				}

				sql = "SELECT " + sTmp + " FROM " + PropertiesSystem.JDEDTA
						+ ".F0311 WHERE RPKCO = '" + sCodSuc + "' AND RPDCT ='"
						+ sTipoFactura + "'" + " AND RPDOC = " + iNofactura
						+ " AND RPSFX = '" + sPartida
						+ "' AND TRIM(RPDCTM) = '' AND RPDOCM = 0 AND RPAN8 = "
						+ iCodclie;

				ps = cn.prepareStatement(sql);
				ResultSet rsPendiente = ps.executeQuery();
				if (rsPendiente.next()) {
					iPendiente = rsPendiente.getInt(sTmp);
					if (iPendiente > 0 || iPendiente < 0) {
						sql = "UPDATE "
								+ PropertiesSystem.JDEDTA
								+ ".F0311 SET RPPST = 'A' WHERE RPKCO = '"
								+ sCodSuc
								+ "' AND RPDCT ='"
								+ sTipoFactura
								+ "'"
								+ " AND RPDOC = "
								+ iNofactura
								+ " AND RPSFX = '"
								+ sPartida
								+ "' AND TRIM(RPDCTM) = '' AND RPDOCM = 0 AND RPAN8 = "
								+ iCodclie;
					} else {
						sql = "UPDATE "
								+ PropertiesSystem.JDEDTA
								+ ".F0311 SET RPPST = 'P' WHERE RPKCO = '"
								+ sCodSuc
								+ "' AND RPDCT ='"
								+ sTipoFactura
								+ "'"
								+ " AND RPDOC = "
								+ iNofactura
								+ " AND RPSFX = '"
								+ sPartida
								+ "' AND TRIM(RPDCTM) = '' AND RPDOCM = 0 AND RPAN8 = "
								+ iCodclie;
					}
					ps = cn.prepareStatement(sql);
					iRegs = ps.executeUpdate();
					if (iRegs == 1) {
						lExito = true;
					}
				}
			} else {
				error = new Exception(
						"@LOGCAJA: No Encuentro Factura. No Puedo Aplicar Monto. Factura: "
								+ iNofactura + ", Partida: " + sPartida
								+ ", Sucursal: " + sCodSuc + " ");
			}
		} catch (Exception ex) {
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.aplicarMontoF0311: "
							+ ex);
			error = new Exception(
					"@LOGCAJA: No se pudo aplicar el monto a factura!!!nofactura: "
							+ iNofactura + ";Partida:" + sPartida
							+ ";Sucursal:" + sCodSuc + "  ");
			errorDetalle = ex;
		} finally {
			try {
				ps.close();
			} catch (Exception se2) {
				System.out
						.println("ERROR: Failed to close connection en ReciboCtrl.aplicarMontoF0311: "
								+ se2);
			}
		}
		return lExito;
	}

	/********* lee el monto aplicado en un recibo en f0311 **********************************/
	public double[] getMontoAplicadoF0311(Connection cn, int iNofactura,
			String sTipoDocumento, int iNoReciboJDE, String sNopartida,
			int iCodcli) {
		String sql = "SELECT sum(rpag*-1)  as cpagado,"
				+ "sum(rpacr*-1 ) as dpagado " + "FROM "
				+ PropertiesSystem.JDEDTA + ".F0311 " + "WHERE RPDOC = "
				+ iNofactura + " AND RPDCT = '" + sTipoDocumento
				+ "' AND RPDOCM = " + iNoReciboJDE
				+ " and rpdctm = 'RC' and rpsfx = '" + sNopartida
				+ "' and rpan8 = " + iCodcli;
		double[] dResultado = new double[2];
		PreparedStatement ps = null;
		try {
			ps = cn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				dResultado[0] = pasarEnteroADouble(rs.getInt("CPAGADO"));
				dResultado[1] = pasarEnteroADouble(rs.getInt("DPAGADO"));
			}
		} catch (Exception ex) {
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.getMontoAplicadoF0311: "
							+ ex);
		} finally {
			try {
				ps.close();
				// cn.close();
			} catch (Exception se2) {
				System.out
						.println("ERROR: Failed to close connection en ReciboCtrl.getMontoAplicadoF0311:  "
								+ se2);
			}
		}
		return dResultado;
	}

	/****** borrar RC del f0311 ***********************************************************/
	public boolean borrarRC(Connection cn, int iNofactura, String sTipofactura,
			int iNoReciboJDE, String sPartida, int iCodcli) {
		boolean bBorrado = true;
		String sql = "delete from " + PropertiesSystem.JDEDTA
				+ ".F0311 WHERE RPDOC = " + iNofactura + " AND RPDCT = '"
				+ sTipofactura + "' AND RPDOCM =" + iNoReciboJDE
				+ " and rpsfx = '" + sPartida + "' and rpan8 = " + iCodcli;
		PreparedStatement ps = null;
		try {
			ps = cn.prepareStatement(sql);
			int rs = ps.executeUpdate();
			if (rs == 0) {
				bBorrado = false;
			}
		} catch (Exception ex) {
			System.out
					.println("Se capturo una excepcion en ReciboCtrl.borrarRC: "
							+ ex);
		} finally {
			try {
				ps.close();
				// cn.close();
			} catch (Exception se2) {
				System.out
						.println("ERROR: Failed to close connection en ReciboCtrl.borrarRC: "
								+ se2);
			}
		}
		return bBorrado;
	}

	/**************************** VALIDAR EXISTENCIA DE NUMERO RECIBO MANUAL *********************************************/
	public boolean verificarNumeroRecibo(int iCaId, String sCodComp,
			int iNumRecm, String sCodsuc) {
		Session session = HibernateUtilPruebaCn.currentSession();
		boolean existe = false;
		try {
			List result = (List) session.createQuery(
					"from Recibo r where r.id.codcomp = '" + sCodComp
							+ "' and r.id.caid = " + iCaId
							+ " and r.id.codsuc = '" + sCodsuc
							+ "' and r.numrecm = " + iNumRecm).list();

			if (!result.isEmpty()) {
				existe = true;
			}
		} catch (Exception ex) {
			LogCajaService.CreateLog("verificarNumeroRecibo", "ERR", "Se capturo una excepcion en ReciboPrimaCtrl.verificarNumeroRecibo: " + ex.getMessage());
		}

		return existe;
	}

	/********************************************************************************************************************/
	/** INSERTAR RM EN EL F0311 DEL JDE ***********************************************************************************/
	public boolean insertarRM(Connection cn, Hfactura fh, String sTipodoc,
			int iNodoc, int iFecha, int iNobatch, BigDecimal bdTasa,
			String sUsuario, String sAplicacion, int iCaja, Vf0101 vf0101,
			String sMonedaBase, String iNodoco, String sTipodoco) {
		boolean bHecho = true;
		String sql = "INSERT INTO "
				+ PropertiesSystem.JDEDTA
				+ ".F0311(RPKCO,RPAN8,RPDCT,RPDOC,RPSFX,RPDIVJ,RPDGJ,RPFY,RPCTRY,RPPN,RPCO,RPICUT,RPICU,RPDICJ,RPPA8,"
				+ "RPAN8J,RPBALJ,RPPST,RPAG,RPAAP,RPATXA,RPSTAM,RPCRRM,RPCRCD,RPCRR,RPACR,RPFAP,RPCTXA,RPCTAM,RPTXA1,RPEXR1,RPDSVJ,RPGLC,RPMCU,RPCM,RPDDJ,RPDDNJ,"
				+ "RPRMK,RPALPH,RPAC01,RPAC02,RPAC03,RPAC04,RPAC05,RPAC06,RPAC07,RPAC08,RPAC09,RPAC10,RPATE,RPATR,RPATP,RPATPR,"
				+ "RPAT1,RPAT2,RPAT3,RPAT4,RPAT5,RPTORG,RPUSER,RPPID,RPUPMJ,RPUPMT,RPJOBN,RPPKCO,RPPO,RPDCTO) VALUES(";
		Date dHora = new Date();
		SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String sNombrePc = null;
		EmpleadoCtrl empCtrl = new EmpleadoCtrl();
		String sConcepto = "Dev:", sCodunineg = "", sNocuota = "001", sCadValida = "", sNomcli = "";
		Divisas d = new Divisas();
		BigDecimal bdMontoD = new BigDecimal(0);
		PreparedStatement ps = null;
		
		try {
			
			sNombrePc = PropertiesSystem.ESQUEMA ; //"SERVER";// InetAddress.getLocalHost().getHostName();
			
			
			// obtener hora en enteros
			String[] sHora = (dfHora.format(dHora)).split(":");
			int iHora = Integer.parseInt(sHora[0] + sHora[1] + sHora[2]);
			
			// obtener partes de la fecha
			Date dFecha = new Date();
			String[] sFecha = (sdf.format(dFecha)).split("/");
			
			
			String codsuc = com.casapellas.util.CodeUtil.pad( fh.getCodsuc().trim(), 5,"0");
			
			// agregar blancos a unineg
			
			sCodunineg = fh.getCodunineg().trim();
			sCodunineg = com.casapellas.util.CodeUtil.pad(sCodunineg.trim(), 12," ");
			
			/*
			if (sCodunineg.length() == 2) {
				sCodunineg = "          " + sCodunineg;
			} else {
				sCodunineg = "        " + sCodunineg;
			}
			*/
			
			sConcepto = sConcepto + fh.getNofactura() + " "
					+ fh.getTipofactura() + " C:" + iCaja;
			if (sConcepto.length() > 30) {
				sConcepto = sConcepto.substring(0, 29);
			}
			if (sNombrePc.length() > 9) {
				sNombrePc = sNombrePc.substring(0, 9);
			}

			// validar comilla simple
			sCadValida = d.remplazaCaractEspeciales(vf0101.getId().getAbalph().trim(), "'", "-");
			if (!sCadValida.equals("")) {
				sNomcli = sCadValida;
			}
			
//			sql = sql + "'000" + fh.getCodsuc() + "',"
			sql = sql + "'" + codsuc + "', "
					+ vf0101.getId().getAban8() + "," + "'" + sTipodoc + "',"
					+ iNodoc + "," + "'" + sNocuota + "'," + iFecha + ","
					+ iFecha + ","
					+ Integer.parseInt(sFecha[2].substring(2, 4)) + "," + "20,"
					+ "" + Integer.parseInt(sFecha[1]) + "," 
					+ "'" + codsuc + "'," + "'I'," + iNobatch + "," + iFecha
					+ "," + vf0101.getId().getAban8() + ","
					+ vf0101.getId().getAban8() + "," + "'Y'," + "'A',";
			
			if (fh.getMoneda().equals(sMonedaBase)) {

				bdMontoD = new BigDecimal(Double.toString((fh.getTotal()))).abs().negate();

				sql = sql + d.pasarAentero(bdMontoD.doubleValue()) + ","
						+ d.pasarAentero(bdMontoD.doubleValue()) + "," + 0
						+ "," + 0 + "," + "'D'," + "'" + sMonedaBase + "'," + 0
						+ "," + 0 + "," + 0 + "," + 0 + "," + 0 + ",";
			} else {
				 

				bdMontoD = new BigDecimal(Double.toString((fh.getTotal()))) .abs().negate();

				double monto1 = bdMontoD.multiply(bdTasa).setScale(2, RoundingMode.HALF_UP).doubleValue();
				
				sql = sql
						+
						d.pasarAentero(monto1) + "," + d.pasarAentero(monto1)
						+ "," +

						0 + "," + 0 + "," + "'F'," + "'" + fh.getMoneda()
						+ "'," + bdTasa + ","
						+ d.pasarAentero(bdMontoD.doubleValue()) + ","
						+ d.pasarAentero(bdMontoD.doubleValue()) + "," + 0
						+ "," + 0 + ",";
			}

			sql = sql + "'EXE'," + "'E'," + iFecha + "," + "'"
					+ vf0101.getId().getA5arc() + "'," + "'" + sCodunineg
					+ "'," + "'" + vf0101.getId().getAbcm() + "'," + iFecha
					+ "," + iFecha + "," + "'" + sConcepto + "'," + "'"
					+ sNomcli + "'," + "'" + vf0101.getId().getAbac01() + "',"
					+ "'" + vf0101.getId().getAbac02() + "'," + "'"
					+ vf0101.getId().getAbac03() + "'," + "'"
					+ vf0101.getId().getAbac04() + "'," + "'"
					+ vf0101.getId().getAbac05() + "'," + "'"
					+ vf0101.getId().getAbac06() + "'," + "'"
					+ vf0101.getId().getAbac07() + "'," + "'"
					+ vf0101.getId().getAbac08() + "'," + "'"
					+ vf0101.getId().getAbac09() + "'," + "'"
					+ vf0101.getId().getAbac10() + "'," + "'"
					+ vf0101.getId().getAbate() + "'," + "'"
					+ vf0101.getId().getAbatr() + "'," + "'"
					+ vf0101.getId().getAbatp() + "','"
					+ vf0101.getId().getAbatpr() + "'," + "'"
					+ vf0101.getId().getAbat1() + "'," + "'"
					+ vf0101.getId().getAbat2() + "'," + "'"
					+ vf0101.getId().getAbat3() + "'," + "'"
					+ vf0101.getId().getAbat4() + "'," + "'"
					+ vf0101.getId().getAbat5() + "'," + "'" + sUsuario + "',"
					+ "'" + sUsuario + "'," + "'" + sAplicacion + "'," + iFecha
					+ "," + iHora + "," + "'" + sNombrePc 
//					+ "','000" + fh.getCodsuc() + "'" + ", '" + iNodoco + "', '"
					+ "','" + codsuc + "'" + ", '" + iNodoco + "', '"
					+ sTipodoco + "')";

			ps = cn.prepareStatement(sql);
			int rs = ps.executeUpdate();
			if (rs != 1) {
				bHecho = false;
			}
		} catch (Exception ex) {
			bHecho = false;
			ex.printStackTrace(); 
		}
		return bHecho;
	}

	/****************************************************************************************/
	/**
	 * Método: Guardar Registro en el GCPPRRDTA.F0911 para RM, en módulo de
	 * Financimiento. (Variante del método RegistrarBatch de JC) Fecha:
	 * 16/08/2010 Nombre: Carlos Manuel Hernández Morrison.
	 **/
	public boolean insertarAsientoRM(Connection cn, Hfactura fh,
			String sTipodoc, int iNodoc, int iFecha, int iNobatch,
			BigDecimal bdTasa, String sUsuario, String sAplicacion,
			String[] sCuenta, String sTipolibro, int iMonto, int iCaja,
			Vf0101 c, int iNolinea, String sGlbcrc, String sGlhco,
			String sGlcrrm) {
		boolean bHecho = true;
		PreparedStatement ps = null;
		String sql = "INSERT INTO "
				+ PropertiesSystem.JDEDTA
				+ ".F0911 (GLKCO,GLDCT,GLDOC,GLDGJ,GLJELN,GLICU,GLICUT,GLDICJ,GLDSYJ,GLTICU,GLCO,GLANI,GLAM,GLAID,GLMCU,GLOBJ,GLSUB,GLLT,GLPN,GLCTRY,GLFY,"
				+ "GLCRCD,GLCRR,GLAA,GLGLC,GLEXA,GLEXR,GLR1,GLR2,GLAN8,GLDKJ,GLVINV,GLIVD,"
				+ "GLPO,GLDCTO,GLLNID,GLDSVJ,GLTORG,GLUSER,GLPID,GLJOBN,GLUPMJ,GLUPMT,"
				+ "GLBCRC,GLHCO,GLCRRM ) VALUES(";
		Date dHora = new Date();
		SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String sNombrePc = null;
		String sCodunineg = "";
		String sConcepto = "";
		String sNomcli = "", sCadValida = "";
		Divisas d = new Divisas();
		String  codsuc = "";
		 
		
		try {
		
			
			// obtener hora en enteros
			String[] sHora = (dfHora.format(dHora)).split(":");
			int iHora = Integer.parseInt(sHora[0] + sHora[1] + sHora[2]);
			
			// obtener partes de la fecha
			Date dFecha = new Date();
			String[] sFecha = (sdf.format(dFecha)).split("/");
			
			sNombrePc = PropertiesSystem.ESQUEMA; // "SERVER";// InetAddress.getLocalHost().getHostName();
			if (sNombrePc.length() > 9) {
				sNombrePc = sNombrePc.substring(0, 9);
			}
			
			sCodunineg = com.casapellas.util.CodeUtil.pad( sCuenta[3].trim(), 12," ");
			sCuenta[0] = com.casapellas.util.CodeUtil.pad( sCuenta[0].trim(), 12," ");
			sCuenta[2] = com.casapellas.util.CodeUtil.pad( sCuenta[2].trim(), 5, "0");
			codsuc = com.casapellas.util.CodeUtil.pad( fh.getCodsuc().trim(), 5, "0");
			sGlhco = com.casapellas.util.CodeUtil.pad(sGlhco.trim(), 5, "0");
			
			/*
			if (sCodunineg.length() == 2) {
				sCodunineg = "          " + sCodunineg;
				sCuenta[0] = "          " + sCuenta[0];
			} else {
				sCodunineg = "        " + sCodunineg;
				sCuenta[0] = "        " + sCuenta[0];
			}
			*/
			
			sConcepto = sConcepto + fh.getNofactura() + " " + fh.getTipofactura() + " C:" + iCaja;

			sNomcli = c.getId().getAbalph();
			if (sNomcli.length() > 29) {
				sNomcli = sNomcli.substring(0, 29);
			}
			
			// validar comilla simple
			sCadValida = d.remplazaCaractEspeciales(sNomcli, "'", "-");
			if (!sCadValida.equals("")) {
				sNomcli = sCadValida;
			}

//			sql = sql + "'000" + fh.getCodsuc() + "'," + "'" + sTipodoc + "',"
			sql = sql + "'" + codsuc + "'," + "'" + sTipodoc + "',"
					+ "" + iNodoc + "," + "" + iFecha + "," + ""
					+ ((iNolinea) * 1.0) + "," + "" + iNobatch + "," + "'I',"
					+ "" + iFecha + "," + "" + iFecha + "," + "" + iHora + ","
					
//					+ "'000" + sCuenta[2] + "'," + "'" + sCuenta[0] + "',"
					+ "'" + sCuenta[2] + "'," + "'" + sCuenta[0] + "',"
					
					+ "'2'," + "'" + sCuenta[1] + "'," + "'" + sCodunineg
					+ "'," + "'" + sCuenta[4] + "'," + "'" + sCuenta[5] + "',"
					+ "'" + sTipolibro + "'," + ""
					+ Integer.parseInt(sFecha[1]) + "," + "20,"
					+ Integer.parseInt(sFecha[2].substring(2, 4)) + "," + "'"
					+ fh.getMoneda() + "'," + bdTasa + "," + iMonto + ","
					+ "''," + "'" + sNomcli + "'," + "'" + sConcepto + "',"
					+ "''," + "''," + c.getId().getAban8() + "," + "" + iFecha
					+ "," + "''," + "" + 0 + "," + "''," + "''," + "0," + ""
					+ iFecha + "," + "'" + sUsuario + "'," + "'" + sUsuario
					+ "'," + "'" + sAplicacion + "'," + "'" + sAplicacion
					+ "'," + "" + iFecha + "," + "" + iHora + "," + "'"
					
//					+ sGlbcrc + "'," + "'000" + sGlhco + "'," + "'" + sGlcrrm
					+ sGlbcrc + "'," + "'" + sGlhco + "'," + "'" + sGlcrrm
					
					+ "'" + ")";

			ps = cn.prepareStatement(sql);
			int rs = ps.executeUpdate();
			
			bHecho = ( rs == 1 ) ;
			
//			if (rs != 1) {
//				bHecho = false;
//			}
			
			
		} catch (Exception ex) {
			bHecho = false;
			ex.printStackTrace(); 
		}
		return bHecho;
	}

	/*********************************************************************************************************************/
	/** Insertar Asiento de RM ******************************************************/
	public boolean insertarAsientoRM1(Connection cn, Hfactura fh, Dfactura fd,
			String sTipodoc, int iNodoc, int iFecha, int iNobatch,
			BigDecimal bdTasa, String sUsuario, String sAplicacion,
			String[] sCuenta, String sTipolibro, int iMonto, int iCaja,
			Vf0101 c, int iNolinea) {
		boolean bHecho = true;
		PreparedStatement ps = null;
		String sql = "INSERT INTO "
				+ PropertiesSystem.JDEDTA
				+ ".F0911 (GLKCO,GLDCT,GLDOC,GLDGJ,GLJELN,GLICU,GLICUT,GLDICJ,GLDSYJ,GLTICU,GLCO,GLANI,GLAM,GLAID,GLMCU,GLOBJ,GLSUB,GLLT,GLPN,GLCTRY,GLFY,"
				+ "GLCRCD,GLCRR,GLAA,GLGLC,GLEXA,GLEXR,GLR1,GLR2,GLAN8,GLDKJ,GLVINV,GLIVD,"
				+ "GLPO,GLDCTO,GLLNID,GLDSVJ,GLTORG,GLUSER,GLPID,GLJOBN,GLUPMJ,GLUPMT) VALUES(";
		Date dHora = new Date();
		SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String sNombrePc = null;
		String sCodunineg = "";
		String sConcepto = "";
		String sNomcli = "";
		try {
			sNombrePc = "SERVER";// InetAddress.getLocalHost().getHostName();
			// obtener hora en enteros
			String[] sHora = (dfHora.format(dHora)).split(":");
			int iHora = Integer.parseInt(sHora[0] + sHora[1] + sHora[2]);
			// obtener partes de la fecha
			Date dFecha = new Date();
			String[] sFecha = (sdf.format(dFecha)).split("/");
			if (sNombrePc.length() > 9) {
				sNombrePc = sNombrePc.substring(0, 9);
			}
			sCodunineg = sCuenta[3];
			if (sCodunineg.length() == 2) {
				sCodunineg = "          " + sCodunineg;
				sCuenta[0] = "          " + sCuenta[0];
			} else {
				sCodunineg = "        " + sCodunineg;
				sCuenta[0] = "        " + sCuenta[0];
			}
			sConcepto = sConcepto + fh.getNofactura() + " "
					+ fd.getTipofactura() + " C:" + iCaja;

			sNomcli = c.getId().getAbalph();
			if (sNomcli.length() > 29) {
				sNomcli = sNomcli.substring(0, 29);
			}

			sql = sql + "'000" + fh.getCodsuc() + "'," + "'" + sTipodoc + "',"
					+ "" + iNodoc + "," + "" + iFecha + "," + ""
					+ ((iNolinea) * 1.0) + "," + "" + iNobatch + "," + "'I',"
					+ "" + iFecha + "," + "" + iFecha + "," + "" + iHora + ","
					+ "'000" + sCuenta[2] + "'," + "'" + sCuenta[0] + "',"
					+ "'2'," + "'" + sCuenta[1] + "'," + "'" + sCodunineg
					+ "'," + "'" + sCuenta[4] + "'," + "'" + sCuenta[5] + "',"
					+ "'" + sTipolibro + "'," + ""
					+ Integer.parseInt(sFecha[1]) + "," + "20,"
					+ Integer.parseInt(sFecha[2].substring(2, 4)) + "," + "'"
					+ fh.getMoneda() + "'," + bdTasa + "," + iMonto + ","
					+ "''," + "'" + sNomcli + "'," + "'" + sConcepto + "',"
					+ "''," + "''," + c.getId().getAban8() + "," + "" + iFecha
					+ "," + "''," + "" + 0 + "," + "''," + "''," + "0," + ""
					+ iFecha + "," + "'" + sUsuario + "'," + "'" + sUsuario
					+ "'," + "'" + sAplicacion + "'," + "'" + sAplicacion
					+ "'," + "" + iFecha + "," + "" + iHora + ")";

			ps = cn.prepareStatement(sql);
			int rs = ps.executeUpdate();
			if (rs != 1) {
				bHecho = false;
			}
		} catch (Exception ex) {
			bHecho = false;
			System.out
					.println("Se capturo una excepcion en CuotaCtrl.insertarAsientoMF: "
							+ ex);
		}
		return bHecho;
	}

	
	@SuppressWarnings("unchecked")
	public static List<Object[]> ValidateCustomerAccount(int RPAN8, int RPDOC ,int ncaja){
		   Session sesion = null;
		   Transaction trans = null; 
		   List<Object[]> result  = new ArrayList<Object[]>();
		   boolean newCn = false;
		   int fecha = 0;
		   
		   try{
		    fecha = FechasUtil.dateToJulian(new Date());
		    
		    
		    sesion = HibernateUtilPruebaCn.currentSession() ;
		    trans = (newCn = !(sesion.getTransaction().isActive())) ? sesion
		      .beginTransaction() : sesion.getTransaction();
		    
		    String strSqlQuery = "SELECT CAST(RPAG AS VARCHAR(10) CCSID 37)," +
		     " CAST(RPAAP AS VARCHAR(10) CCSID 37), (RPAG-RPAAP) , REG FROM (SELECT SUM( DECIMAL(RPAG/100,15,2))  RPAG,  SUM(DECIMAL(RPAAP/100,15,2))  RPAAP, " +
		     "  '"+MetodosPagoCtrl.TARJETA+"' REG FROM " + PropertiesSystem.JDEDTA+ ".F0311  "
		     + " WHERE RPAN8 =  :firstp AND RPDOC = :secondp   AND RPTRTC <> 'G' AND RPDCTM NOT IN ('RE','RG')) AS TOTAL "  
		     + " UNION ALL "  
		     + " SELECT K.MONEDA,MPAGO, K.MONTO ,'D' REG FROM "
		     + PropertiesSystem.ESQUEMA + ".RECIBOFAC F INNER JOIN  "  
		     + PropertiesSystem.ESQUEMA + ".RECIBODET K ON  " 
		     + " K.NUMREC = F.NUMREC AND F.TIPOREC = K.TIPOREC "  
		     + " AND F.CAID = K.CAID  AND F.CODCOMP = K.CODCOMP  "
		     + " WHERE NUMFAC=:secondp2 AND F.CAID =:caja AND" 
		     + " ESTADO = '' AND  F.FECHA = "+ fecha;
		     
		   result = (List<Object[]>) sesion.createSQLQuery(strSqlQuery)
		     .setParameter("firstp", RPAN8)
		     .setParameter("secondp", RPDOC)
		     .setParameter("secondp2", RPDOC)
		     .setParameter("caja", ncaja).list();

		   }catch(Exception ex){ 
		    ex.printStackTrace(); 
		   }finally{
		    
		    if(newCn){
		     try {  trans.commit(); } 
		     catch (Exception e2) { }
		     try {  HibernateUtilPruebaCn.closeSession(sesion); }
		     catch (Exception e2) { }
		    }
		    sesion = null;
		    trans = null;
		   } 
		   return result;
		  }
	
	public static void UpdateClientBalance(int RPAN8, int RPDOC, Connection cn,
			String cuota, String strSucursal, String strTipoDoco) {
		
		PreparedStatement ps = null;
		
		try {  
		     String strSqlQuery = "UPDATE "+PropertiesSystem.JDEDTA+".F0311 SET RPAAP = (SELECT SUM(RPAG) FROM "+PropertiesSystem.JDEDTA+".F0311 " +
		       " WHERE RPAN8 = "+RPAN8+"   AND RPDOC = "+RPDOC+" AND RPSFX='"+cuota+"' AND TRIM(RPDCTM) NOT IN('RG','RE') AND  TRIM(RPKCO)='"+strSucursal+"' AND  TRIM(RPDCT)='"+strTipoDoco+"'  ), RPFAP =(SELECT SUM(RPACR) FROM "+PropertiesSystem.JDEDTA+".F0311 " +
		       " WHERE RPAN8 = "+RPAN8+"   AND RPDOC = "+RPDOC+" AND RPSFX='"+cuota+"' AND TRIM(RPDCTM) NOT IN('RG','RE') AND  TRIM(RPKCO)='"+strSucursal+"' AND  TRIM(RPDCT)='"+strTipoDoco+"'  )," +
		         "  RPPST = CASE WHEN (SELECT SUM(RPAG) FROM "+PropertiesSystem.JDEDTA+".F0311  WHERE RPAN8 = "+RPAN8+"  AND RPDOC = "+RPDOC+" AND RPSFX='"+cuota+"' AND TRIM(RPDCTM) NOT IN('RG','RE')  AND  TRIM(RPKCO)='"+strSucursal+"' AND  TRIM(RPDCT)='"+strTipoDoco+"' ) =0 THEN 'P' ELSE 'A' END" +
		         "  WHERE RPAN8 = "+RPAN8+"  AND RPDOC = "+RPDOC+" AND TRIM(RPDCTM) = '' AND RPSFX='"+cuota+"' AND  TRIM(RPKCO)='"+strSucursal+"' AND  TRIM(RPDCT)='"+strTipoDoco+"' ";

		     ps = cn.prepareStatement(strSqlQuery);
			 ps.executeUpdate();
			
		} catch (Exception ex) { 
			ex.printStackTrace(); 
		} finally {
			try {
				ps = null;
				cn.commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void UpdateClientBalanceFinan(int RPAN8, int RPDOC ,Connection cn,String cuota,String noSolicitud,String strSucursal, String strTipoDoco){
		PreparedStatement ps = null;
		
		try {
		   
			String strSqlQuery = "UPDATE "+PropertiesSystem.JDEDTA+".F0311 SET RPAAP = (SELECT SUM(RPAG) FROM "+PropertiesSystem.JDEDTA+".F0311 " +
//		       " WHERE RPAN8 = "+RPAN8+"   AND RPDOC = "+RPDOC+" AND RPSFX='"+cuota+"' AND RPPO = '"+noSolicitud+"' AND TRIM(RPDCTM) NOT IN('RG','RE') AND  TRIM(RPKCO)='"+strSucursal+"' AND  TRIM(RPDCT)='"+strTipoDoco+"' ), RPFAP =(SELECT SUM(RPACR) FROM "+PropertiesSystem.JDEDTA+".F0311 " +
//			   " WHERE RPAN8 = "+RPAN8+"   AND RPDOC = "+RPDOC+" AND RPSFX='"+cuota+"' AND RPPO = '"+noSolicitud+"' AND TRIM(RPDCTM) NOT IN('RG','RE') AND  TRIM(RPKCO)='"+strSucursal+"' AND  TRIM(RPDCT)='"+strTipoDoco+"' )," +
		       " WHERE RPAN8 = "+RPAN8+"   AND RPDOC = "+RPDOC+" AND RPSFX='"+cuota+"' AND TRIM(RPDCTM) NOT IN('RG','RE') AND  TRIM(RPKCO)='"+strSucursal+"' AND  TRIM(RPDCT)='"+strTipoDoco+"' ), RPFAP =(SELECT SUM(RPACR) FROM "+PropertiesSystem.JDEDTA+".F0311 " +
		       " WHERE RPAN8 = "+RPAN8+"   AND RPDOC = "+RPDOC+" AND RPSFX='"+cuota+"' AND TRIM(RPDCTM) NOT IN('RG','RE') AND  TRIM(RPKCO)='"+strSucursal+"' AND  TRIM(RPDCT)='"+strTipoDoco+"' )," +
		         " RPPST = CASE WHEN (SELECT SUM(RPAG) FROM "+PropertiesSystem.JDEDTA+".F0311  WHERE RPAN8 = "+RPAN8+"  AND RPDOC = "+RPDOC+" AND RPSFX='"+cuota+"' AND TRIM(RPDCTM) NOT IN('RG','RE')  AND  TRIM(RPKCO)='"+strSucursal+"' AND  TRIM(RPDCT)='"+strTipoDoco+"' ) =0 THEN 'P' ELSE 'A' END " +
		         "  WHERE RPAN8 = "+RPAN8+"  AND RPDOC = "+RPDOC+" AND TRIM(RPDCTM) = '' AND RPSFX="+cuota+" AND RPPO = '"+noSolicitud+"' AND  TRIM(RPKCO)='"+strSucursal+"' AND  TRIM(RPDCT)='"+strTipoDoco+"' ";
			
			ps = cn.prepareStatement(strSqlQuery);
			ps.executeUpdate();
			
		} catch (Exception ex) { 
			ex.printStackTrace(); 
		} finally {
			try {
				ps = null;
				cn.commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public static void adjustBalanceInvoice(int RPAN8, int RPDOC ,
			Connection cn,String cuota,String strSucursal, 
			String strTipoDoco, int nobatch, int fecha){
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean update = false;
		
		try {
			
			String strSqlSaldoCor = 
			"(SELECT SUM(RPAG) FROM "+PropertiesSystem.JDEDTA+".F0311 " +
			" WHERE RPAN8 = "+RPAN8+" AND RPDOC = "+RPDOC+" AND RPSFX='" +
			 cuota+"' AND TRIM(RPDCTM) NOT IN('RG','RE') AND  TRIM(RPKCO)='" +
			 strSucursal+"' AND  TRIM(RPDCT)='"+strTipoDoco+"') SALDOCOR \n";
				
			String strFactura = 
				" SELECT " + strSqlSaldoCor +", RPAG, RPACR  "+
				" FROM "+PropertiesSystem.JDEDTA+".F0311 " +
				" WHERE RPAN8 = "+RPAN8+"  AND RPDOC = "+RPDOC+
				" AND TRIM(RPDCTM) = '' AND RPSFX='"+cuota+"' " +
				" AND TRIM(RPKCO)='"+strSucursal+"' AND  TRIM(RPDCT)='"+strTipoDoco+"' " +
				" AND RPFAP = 0 AND RPAAP <> 0 AND RPCRR  >  0 "; 
			
			ps = cn.prepareStatement(strFactura);
			rs = ps.executeQuery();
			
			if(!rs.next())
				return;
			
			int sumaMovCor = rs.getInt(1);
			
			if(Math.abs(sumaMovCor) <= 1000 ){
				
				String sqlRcs = 
				" SELECT RPAG,RPAAP, RPACR, RPFAP, RPUPMT  " +
				" FROM "+PropertiesSystem.JDEDTA+".F0311 " +
				" WHERE RPDOC = "+RPDOC+ " AND RPAN8 = "+RPAN8+
				" AND RPDCTM = 'RC' AND RPDGJ = "+fecha +
				" AND RPICU = "+nobatch+" FETCH FIRST ROWS ONLY";
				
				ps = cn.prepareStatement(sqlRcs);  
				rs = ps.executeQuery();
				
				if(!rs.next())
					return;
				
				int mtoPagoCor = rs.getInt(1);
				int mtoSaldoCor = rs.getInt(2);
				int mtoPagoUsd = rs.getInt(3);
				int mtoSaldoUsd = rs.getInt(4);
				int horapago = rs.getInt(5);
				
				int mtoPagoNew = mtoPagoCor ; 
				
				if(sumaMovCor < 0) 
					mtoPagoNew = Math.abs(mtoPagoCor) - Math.abs(sumaMovCor) ;
				if(sumaMovCor > 0) 
					mtoPagoNew = Math.abs(mtoPagoCor) + Math.abs(sumaMovCor) ;
				
				String upd = "UPDATE "+PropertiesSystem.JDEDTA+".F0311" +
					" SET RPAG = -"+ mtoPagoNew +"" +
					" WHERE RPAG = "+ mtoPagoCor  + " AND RPACR = " +mtoPagoUsd+
					" AND RPAAP = " + mtoSaldoCor + " AND RPFAP = " +mtoSaldoUsd+
					" AND RPDOC = " + RPDOC + " AND RPAN8 = "+RPAN8 +
					" AND RPICU = " + nobatch +	" AND RPDCTM = 'RC' " +
					" AND RPDGJ = " + fecha +" AND RPUPMT = "+ horapago ;
					
				ps = cn.prepareStatement(upd);  
				update = ( ps.executeUpdate() == 1 );
				if(!update) return;
				
				upd = "UPDATE "+PropertiesSystem.JDEDTA+".F0311" +
						" SET RPAAP = 0, RPPST = 'P' WHERE RPAN8 = "+RPAN8+
						" AND RPDOC = "+RPDOC+" AND TRIM(RPDCTM) = '' " +
						" AND RPSFX='"+cuota+"' AND  TRIM(RPKCO)='"+strSucursal+"' " +
						" AND  TRIM(RPDCT)='"+strTipoDoco+"' ";
				
				ps = cn.prepareStatement(upd);  
				update = ( ps.executeUpdate() == 1 );
				
				if(!update) return;
				
				//&& ======= Notificacion temporal.
				try {
					String strBody = " Factura # "+ RPDOC +", TipoFactura: '"
						+strTipoDoco +"',  Cliente "+RPAN8 +" || Ajustado con:  "
						+ sumaMovCor +", Fecha:  "+FechasUtil.formatDatetoString(new Date(), "dd/MM/yyyy HH:mm:ss") ;
					
					MailHelper.SendHtmlEmail(
							new CustomEmailAddress(PropertiesSystem.WEBMASTER_EMAIL_ADRESS, "Módulo de Caja"),
							new CustomEmailAddress(PropertiesSystem.MAIL_BOUNCEADDRESS), 
							"Ajuste de Saldo Córdobas Factura Crédito", strBody);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		} catch (Exception e) {
			update = false;
			e.printStackTrace();
		}finally{
			ps = null;
			
			try {
				if(update)
					cn.commit();
				else
					cn.rollback();
				
			} catch (Exception e2) {
				e2.printStackTrace(); 
			}
		}
	}
	
	
}

package com.casapellas.socketpos;
/**
 * CASA PELLAS S.A.
 * Creado por.........: Juan Carlos Ñamendi Pineda
 * Fecha de Creación..: 29/07/2010
 * Última modificación: Juan Carlos Ñamendi Pineda
 * Modificado por.....:	02/09/2011
 * Descripción:.......: 
 */
import java.io.File;
import java.math.BigDecimal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import ni.com.casapellas.bac.ECommerces;
import ni.com.casapellas.db2.pojo.ResultadoECommerce;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.hibernate.*;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;


import com.casapellas.controles.ConsolidadoDepositosBcoCtrl;
import com.casapellas.controles.tmp.AfiliadoCtrl;
import com.casapellas.controles.tmp.CtrlPoliticas;
import com.casapellas.entidades.B64strfile;
import com.casapellas.entidades.Cafiliados;
import com.casapellas.entidades.CierreSktpos;
import com.casapellas.entidades.CierreSpos;
import com.casapellas.entidades.F55ca014;
import com.casapellas.entidades.MetodosPago;
import com.casapellas.entidades.Recibodet;
import com.casapellas.entidades.TermAfl;
import com.casapellas.entidades.Transactsp;
import com.casapellas.entidades.Vcierresktpos;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.navegacion.As400Connection;
import com.casapellas.reportes.RptCierreSocketPos;
import com.casapellas.rpg.P55CA090;
import com.casapellas.socketpos.bac.pojos.ExecuteTransactionResult;
import com.casapellas.socketpos.bac.pojos.Response;
import com.casapellas.socketpos.bac.transactions.*;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.Divisas;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.PropertiesSystem;
import com.sun.jna.Native;
import com.sun.jna.Platform;

public class PosCtrl {	
	
	public static String credomatic_SocketPos_TestConnection() {
		String strMensajeValidacion = "";
		try {
 
			TransactionsSocketPosBac.transaction_type = TransactionsTypes.TEST;
			boolean exec = TransactionsSocketPosBac.executeTransactionRequested();
		
			if (!exec) {
				
				return strMensajeValidacion = "No se ha podido crear interfaz de comunicación con Socket Pos";
				
			}

			ExecuteTransactionResult tr = TransactionsSocketPosBac.transaction_result;
			Response r = TransactionsSocketPosBac.response;

			if (tr == null) {
				
				return strMensajeValidacion = "No se ha podido crear interfaz de comunicación con Socket Pos";
			}
			
			//&& ============ comunicacion establecida
			if( r.getResponseCode().compareTo( ResponseCodes.APROBADO.CODE ) != 0){
				return strMensajeValidacion = "Conexión No establecida, Codigo Respuesta: [" 
						+ r.getResponseCode() +" > " + r.getResponseCodeDescription( ) + "]";
			}
			

		} catch (Exception e) {
			strMensajeValidacion = "Se ha generado un error al tratar de establecer conexion con Socket POS credomatic";
			LogCajaService.CreateLog("credomatic_SocketPos_TestConnection", "ERR", e.getMessage());
		}
		return strMensajeValidacion;
	}
	
	
	
	public static void printEcommerceVoucher(String type, String printer, String companyName, EcommerceTransaction ec) {
		try {
			
			String sVarios =  ( (type.compareTo("A") == 0)?"-":"")+
					
					String.format("%1$,.2f", new BigDecimal(ec.getPaymentAmmount()) ) + ";" 
					+ type + ";" 
					+ printer.trim() + ";" 
					+ ec.getPosCode() + ";"
					+ ec.getCoin() + ";"
					+ ec.getClientName(); 
			
			try {
				
				p55ca090 = getP55ca090();
				
				if(p55ca090 == null)
					return;
				
				p55ca090.setCIA("     "+ companyName);
				p55ca090.setTERMINAL( ec.getTerminalid() );
				p55ca090.setDIGITO( ec.shortCardNumber() );
				p55ca090.setREFERENC( ec.getReferencenumber() );
				p55ca090.setAUTORIZ( ec.getAuthorizationNumber() );
				p55ca090.setFECHA( ec.formatPaymentDate() );
				p55ca090.setVARIOS( sVarios );
				
				p55ca090.invoke();
				p55ca090.disconnect() ;
				
			}catch (Exception ex) {
				ex.printStackTrace();
//				LogCrtl.imprimirError(ex);
//				LogCrtl.sendLogDebgs(ex);
			}finally{
				p55ca090 = null;
			}
			
		}catch(Exception e) {
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
//			LogCrtl.sendLogDebgs( e );
		}
	}
	
	
	public static Object[]  getTransactionEcommerce(Recibodet rd) {
		Object[] dtaTransact = new Object[2];
		
		try {
			
			String query = " select tbtransid,  tbntarjeta, tbmonto, tbcodauth, tbusrc, tbdispksc," +
				" ifnull( (select term_id from @BDCAJA.term_afl where trim(card_acq_id) ='@IDPOS' fetch first rows only), '' ) terminal " + 
				" from @BDCAJA.transactspbanco " +
				" where tbcaid = @TBCAID and tbnumrec = @TBNUMREC " +
				"		and tbtransid = '@TBTRANSID' and tbcodresp = 1" ;
			
			query = query
				.replace("@BDCAJA", PropertiesSystem.ESQUEMA )
				.replace("@TBCAID", String.valueOf(rd.getId().getCaid() ))
				.replace("@TBNUMREC", String.valueOf(rd.getId().getNumrec() )  )
				.replace("@TBTRANSID", rd.getId().getRefer4().trim() )
				.replace("@IDPOS", rd.getId().getRefer1().trim() );
			
//			System.out.println(query);
			@SuppressWarnings("unchecked")
            List<Object[]> dtaPayment = (List<Object[]>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, true, null); 
			
			if( dtaPayment == null || dtaPayment.isEmpty() )
				return null;
			
			return new Object[] {"", new EcommerceTransaction(
					String.valueOf(dtaPayment.get(0)[0]), //transactionid, 
					String.valueOf(dtaPayment.get(0)[1]), //cardNumber, 
					String.valueOf(dtaPayment.get(0)[2]), //paymentAmmount, 
					String.valueOf(dtaPayment.get(0)[3]), //authorizationNumber, 
					String.valueOf(dtaPayment.get(0)[4]), //createUser, 
					String.valueOf(dtaPayment.get(0)[5]), //createProgramm
					String.valueOf(dtaPayment.get(0)[6]) //terminal id) ;
					)
			};
			
		}catch(Exception e){
			dtaTransact[0] = "Error al procesar datos de consulta de Cobro E-commerce ";
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
		}
		return  dtaTransact ;
	}
	public static Object[]  obtenerDatosCobroEcommerce( Recibodet recibodet ) {
		Object[] dtaEcommercePay = null;
		
		try {
			
			if(recibodet.getVmanual().compareTo("2") != 0)
				return dtaEcommercePay ;
			
			dtaEcommercePay = getTransactionEcommerce(recibodet);
			
			if(dtaEcommercePay == null )
				return dtaEcommercePay;
			
			return dtaEcommercePay; 
			
		}catch(Exception e) {
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
			return dtaEcommercePay  = null;
		}

	}
	
	public static String anularCargoPorEcommerce(List<Recibodet> lstFormasPago, String printer, String companyName) {
		String msgProceso = null;
		
		try {
			
			for (Recibodet recibodet : lstFormasPago) {
				
				Object[] dtaEcommercePay = obtenerDatosCobroEcommerce(recibodet);
				
				if( dtaEcommercePay == null)
					continue;
				
				if( !dtaEcommercePay[0].toString().isEmpty() )
					return dtaEcommercePay[0].toString() ;
				
				if( dtaEcommercePay[1] == null )
					return msgProceso = "No se han podido recuperar datos originales de Cobro automatico ecommerce " ;
				
				EcommerceTransaction et = (EcommerceTransaction)dtaEcommercePay[1];
				
				ResultadoECommerce rec = PosCtrl.voidTransactionEcommerce(et) ;
				
				if(rec == null  ) {
					return msgProceso = " No se ha podido aplicar la reversion del cobro en Ecommerce "+  et.maskedCardNumber() +"por " + et.getPaymentAmmount() ;
				}
				if(rec.getCodigoRespuesta().intValue()  != 1 ) {
					return msgProceso = " No se ha podido aplicar la reversion del cobro en Ecommerce " +rec.getDescripcionRespuesta() ;
				}
				
				if(rec.getCodigoRespuesta().intValue() == 1) {
					
					et.setClientName( CodeUtil.capitalize( recibodet.getNombre().trim() ) );
					et.setPosCode( recibodet.getId().getRefer1().trim() ) ;
					et.setReferencenumber( rec.getTransactionId() );
					et.setCoin(recibodet.getId().getMoneda() );
					et.setPaymentDate( new Date() ) ;
					
					CodeUtil.putInSessionMap("pfc_DataPayEcommerce", et);
					
					printEcommerceVoucher("A", printer, companyName, et);
					
					return msgProceso = "" ;
					
				}
				
			}
			
		}catch(Exception e) {
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
			msgProceso = "No se ha podido aplicar reversion de cobro de tarjeta ";
		}
		
		return msgProceso;
	}
	
	public static ResultadoECommerce voidTransactionEcommerce(EcommerceTransaction et) {
		ResultadoECommerce rec = null ;
		
		try {
			
			rec = ECommerces.procesarPagoBAC("AT", 
	            PropertiesSystem.IPSERVERDB2, 
	            PropertiesSystem.ESQUEMA, 
	            PropertiesSystem.CN_USRNAME, 
	            PropertiesSystem.CN_USRPWD,
	            null, //caja
	            null, //recibo
	            null,
	            null,
	            null,
	            et.getCardNumber(), 
	            null, 
	            null, 
	            null,
	            new BigDecimal(et.getPaymentAmmount()).doubleValue(),  
	            et.getTerminalid(), 
	            et.getTransactionid(), 
	            et.getCreateUser(),
	            PropertiesSystem.ESQUEMA,
	            PropertiesSystem.ESQUEMA,
	            "ND");
			
		}catch(Exception e) {
			rec = null ;
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
		}
		return rec ;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Transactsp> cobrosSocketPosPendientes( String terminalid, int stat_cred ){
		List<Transactsp> sppendientes= null;
		
		try {
			
			String query = "select * from "+ PropertiesSystem.ESQUEMA + ".transactsp where status = 'PND' and termid = '"+terminalid+"'and stat_cred = " + stat_cred ;
			
			return sppendientes = (List<Transactsp>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, true, Transactsp.class);
			
		} catch (Exception e) {
			LogCajaService.CreateLog("cobrosSocketPosPendientes", "ERR", e.getMessage());
		}
		
		return sppendientes;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Transactsp> cobrosSocketPosPendientes_cierrePOS( String terminalid, int stat_cred ){
		List<Transactsp> sppendientes= null;
		
		try {
			
			String query = "select * from "+ PropertiesSystem.ESQUEMA + ".transactsp where status = 'PND' and termid = '"+terminalid+"'and stat_cred = " + stat_cred ;
			sppendientes = (List<Transactsp>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, true, Transactsp.class);
			
			if(sppendientes.size()>0)
			{
				Response response =  null;
				response = aplicarCierreTerminal(terminalid);
				
				if(response == null) {
					throw new Exception("Cierre de terminal "+ terminalid +" no se ha podido generar ");
				}
				
				if(response.getResponseCode().compareTo( ResponseCodes.APROBADO.CODE ) != 0 && 
						response.getResponseCode().compareTo( ResponseCodes.RSPC21.CODE ) != 0 ) {
					throw new Exception("Cierre de terminal "+ terminalid 
							+" no ha aplicado, Código de Razón  " + response.getResponseCode() + " " 
							+ response.getResponseCodeDescription() );
				}
				
				String sql = " update " + PropertiesSystem.ESQUEMA
						+ ".Transactsp set stat_cred = 1, status = 'APL'"
						+ " where termid = '" + terminalid
						+ "' and status = 'PND' and stat_cred = 0";
				
				
				
				try {
					Session sesion = HibernateUtilPruebaCn.currentSession();
					
					LogCajaService.CreateLog("cobrosSocketPosPendientes_cierrePOS", "QRY", sql);
					
					sesion.createSQLQuery(sql).executeUpdate();
				} catch (Exception e) {
					LogCajaService.CreateLog("cobrosSocketPosPendientes_cierrePOS", "ERR", e.getMessage());
				}
				
			}
				
			
		} catch (Exception e) {
			LogCajaService.CreateLog("cobrosSocketPosPendientes_cierrePOS", "ERR", e.getMessage());
		}
		
		return sppendientes;
	}
	
	@SuppressWarnings("unchecked")
	public static List<HistoricoCobrosSocketpos> listarCobrosRespuestaPendiente( String terminalid, Date fechavalidar ){
		List<HistoricoCobrosSocketpos> pendientes = null;
		
		try {
			
			String sql = " select * from " + PropertiesSystem.ESQUEMA
					+ ".HISTORICO_COBROS_SOCKETPOS  WHERE TERMINALID = '"
					+ terminalid + "' and date(created) = '"
					+ new SimpleDateFormat("yyyy-MM-dd").format(fechavalidar)
					+ "' and status =  0 ";
			
			LogCajaService.CreateLog("listarCobrosRespuestaPendiente", "QRY", sql);
			 
			return pendientes =  (List<HistoricoCobrosSocketpos>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, true, HistoricoCobrosSocketpos.class);
			
			
		} catch (Exception e) {
			LogCajaService.CreateLog("listarCobrosRespuestaPendiente", "ERR", e.getMessage());
		}  
		return pendientes;
	}
	
	public static void aplicarReversionPagosNoValidados(String terminalid, Date fechavalidar){
		try {
		
			String sql = " select * from " + PropertiesSystem.ESQUEMA
					+ ".HISTORICO_COBROS_SOCKETPOS  WHERE TERMINALID = '"
					+ terminalid + "' and date(created) = '"
					+ new SimpleDateFormat("yyyy-MM-dd").format(fechavalidar)
					+ "' and status =  0 ";
			
			@SuppressWarnings("unchecked")
			List<HistoricoCobrosSocketpos> pendientes =  (List<HistoricoCobrosSocketpos>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, true, HistoricoCobrosSocketpos.class);
			
			if( pendientes == null  || pendientes.isEmpty() )
				return;
		
			List<String> updates = new ArrayList<String>();
		 
			for (HistoricoCobrosSocketpos hc: pendientes) {
				
				TransactionsSocketPosBac.terminalid = terminalid ;
				TransactionsSocketPosBac.transaction_type = TransactionType.REVERSE.TYPE;
				TransactionsSocketPosBac.transaction_invoice_number =  hc.getInvoicenumber() ;
				
				TransactionsSocketPosBac.executeTransactionRequested();
				ExecuteTransactionResult tr = TransactionsSocketPosBac.transaction_result;
				
				if(tr == null){
					System.out.println(" no se ha podido aplicar reversion del cobro aplicado ");
					continue;
				}
				
				hc.setResponsecode( tr.getResponseCode() );
				
				if( tr.getResponseCode().compareTo(ResponseCodes.APROBADO.CODE) == 0){
					hc.setStatus(1);
				} 
				
				updates.add( hc.updateStatment() );
				
			}
			
			if( !updates.isEmpty() )
				return;
			
			ConsolidadoDepositosBcoCtrl.executeSqlQueries(updates);

			
		} catch (Exception e) {
			LogCajaService.CreateLog("aplicarReversionPagosNoValidados", "ERR", e.getMessage());
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public static List<Transactsp> getTransactsp_Filters(int caid,
			String terminales, Date fechaini, Date fechafin, String moneda,
			String estados, String parametro, String valorparametro, int maxresult) {
		
		Session sesion = null; 
		Transaction trans = null;
		List<Transactsp> transaccionesSp = null;
		boolean newCn = false ;
		
		try {
			sesion = HibernateUtilPruebaCn.currentSession();
			trans = (newCn = !(sesion.getTransaction().isActive())) ? sesion
					.beginTransaction() : sesion.getTransaction();

			String sql = "select idtransact, termid, acqnumber, systraceno, "
					+ "authorizationid, cardnumber, amount, c.caid || ' ' ||"
					+ " lower(trim(c.caname))  as expirationdate, "
					+ "referencenumber, transtime, transdate, currency,"
					+ " ts.caid, codcomp, clientname, responsecode, status, "
					+ "tiporec, stat_cred " + "from "
					+ PropertiesSystem.ESQUEMA + ".transactsp ts inner join"
					+ " " + PropertiesSystem.ESQUEMA
					+ ".f55ca01 c on c.caid = ts.caid"
					+ " where idtransact <> 0 ";
			
			if (caid != 0)
				sql += " and ts.caid = " + caid;
			if (terminales != null && !terminales.isEmpty())
				sql += " and termid in (" + terminales + ")";
			if (moneda != null && !moneda.isEmpty())
				sql += " and currency = '" + moneda + "'";
			if (fechaini != null)
				sql += " and transdate >= '"
						+ new SimpleDateFormat("yyyy-MM-dd").format(fechaini)+"'";
			if (fechafin != null)
				sql += " and transdate <= '"
						+ new SimpleDateFormat("yyyy-MM-dd").format(fechafin)+"'";
			if(estados.compareTo("") != 0)
				sql += " and status in ("+estados+")";
			if (!valorparametro.isEmpty())
				sql += " and lower(" + parametro.toLowerCase() + ") like '%"
						+ valorparametro.toLowerCase() + "%' ";
			
			System.out.println("Consulta "+sql);
			
			Query q = sesion.createSQLQuery(sql).addEntity(Transactsp.class) ;
			if(maxresult > 0)
				q.setMaxResults(maxresult);
			transaccionesSp = (ArrayList<Transactsp>) q.list();
			
			Collections.sort(transaccionesSp, new Comparator<Transactsp>() {
//				@Override
				public int compare(Transactsp t1, Transactsp t2) {
					int compare = t2.getTransdate()
							.compareTo(t1.getTransdate());
					if (compare == 0)
						compare = t2.getTranstime()
								.compareTo(t1.getTranstime());
					return compare;
				}
			});
			
		} catch (Exception e) {
			e.printStackTrace(); 
//			LogCrtl.sendLogDebgs(e);
//			LogCrtl.sendLogDebgs(e);
			transaccionesSp = null;
		}finally{
			if (trans != null && trans.isActive() && newCn) {
				try {
					trans.commit();
				} catch (Exception e2) {
					e2.printStackTrace(); 
//					LogCrtl.sendLogDebgs(e2);
				}
				try {
					if(newCn && sesion != null && sesion.isOpen() )
					HibernateUtilPruebaCn.closeSession(sesion);
				} catch (Exception e2) {
					e2.printStackTrace(); 
//					LogCrtl.sendLogDebgs(e2);
				}
			}
			  sesion = null;
			  trans = null;
		}
		return transaccionesSp ;
	}
	public static Transactsp getTransactSpFromMpago(MetodosPago mp, int caid,
			String codcomp, String tiporec) {

		Session sesion = null;
		Transactsp tsp = null;
		
		try {

			sesion = HibernateUtilPruebaCn.currentSession();

			tsp = (Transactsp) sesion.createCriteria(Transactsp.class)
				.add(Restrictions.eq("termid", mp.getTerminal()))
				.add(Restrictions.eq("acqnumber", mp.getReferencia()))
				.add(Restrictions.eq("referencenumber", mp.getReferencia4()))
				.add(Restrictions.eq("authorizationid", mp.getReferencia5()))
				.add(Restrictions.eq("systraceno", mp.getReferencia7()))
				.add(Restrictions.eq("amount", new BigDecimal(Double.toString(mp.getMontopos()))))
				.add(Restrictions.eq("currency", mp.getMoneda()))
				.add(Restrictions.eq("tiporec", tiporec))
				.add(Restrictions.eq("caid", caid))
				.add(Restrictions.eq("codcomp", codcomp)).setMaxResults(1)
				.uniqueResult();

		} catch (Exception e) {
			tsp = null;
			 
		} 
		return tsp;
	}
	public static String[] crearReporteCierre(TransaccionTerminal trm) {
		String filepath = "c:\\Reporte.pdf";
		String[] strParts = null;
		int maxLength = 25000;

		try {

			filepath = trm.getRutarealreporte();
			
			RptCierreSocketPos rs = new RptCierreSocketPos(trm, filepath);
			rs.createRpt_TerminalClosing();

			byte[] data = Divisas.byteArrayFromFile(filepath);
			byte[] encodedBytes = Base64.encodeBase64(data);
			trm.setDtaReporteCierre(encodedBytes);
			
			String str = new String(encodedBytes, "UTF8");
			int inicio = 0;
			int fin = 0;
			int strEncodeLng = str.length();
			int cantRows = (strEncodeLng / maxLength) + 1;
			strParts = new String[cantRows];

			for (int i = 0; i < cantRows; i++) {

				fin += maxLength;

				if (fin > str.length()) {
					fin = str.length();
				}
				strParts[i] = str.substring(inicio, fin);
				inicio = fin;
			}
			
		} catch (Exception e) {
			LogCajaService.CreateLog("crearReporteCierre", "ERR", e.getMessage());
			strParts = null;
		}
		return strParts;
	}
	
	/**
	 * aplicar el cierre de la terminal.
	 */
	public static String cerrarTerminalPOS(TransaccionTerminal trm, boolean actualizacion){
		String msgCierreTerminal = "";
		String autorizacion = "";
		String responsecode ="";
		String reference = "";
		String systraceno = "";
		int batchnumber = 0;
		Session sesion = null;
		Transaction trans = null;
//		ArrayList<String> respuestaCr = null;
		boolean newCn = false;
		Response response =  null;
		
		try {
			
			response = PosCtrl.aplicarCierreTerminal( trm.getTerminalid());
			
			if(response == null) {
				return msgCierreTerminal = "Cierre de terminal "+ trm.getTerminalid() +" no se ha podido generar ";
			}
			
			if(response.getResponseCode().compareTo( ResponseCodes.APROBADO.CODE ) != 0 && 
					response.getResponseCode().compareTo( ResponseCodes.RSPC21.CODE ) != 0 ) {
				return msgCierreTerminal = "Cierre de terminal "+ trm.getTerminalid() 
						+" no ha aplicado, Código de Razón  " + response.getResponseCode() + " " 
						+ response.getResponseCodeDescription() ;
			}
			
			
			/*
			respuestaCr = cerrarTerminal(trm.getTerminalid());
			
			if (respuestaCr == null || respuestaCr.isEmpty() || respuestaCr.get(0).contains("error")) {

				return msgCierreTerminal = "Cierre no aplicado a terminal "
						+ trm.getTerminalid()
						+ ", respuesta inválida de SocketPOS ";
			}
			
			if (respuestaCr.get(0).compareTo("21") == 0) {
				return msgCierreTerminal = "No hay transacciones en la terminal";
			}
			if (respuestaCr.get(0).compareTo("00") != 0) {
				return msgCierreTerminal = "Cierre a terminal "
						+ trm.getTerminalid() + " no aplicado por SocketPOS (RC:'"+respuestaCr.get(0)+"')";
			}*/
			
			trm.setTerm_cerrada(true);
			
			//&& =========== Leer la banda y obtener los datos de compras y devoluciones
//			responsecode = respuestaCr.get(0);
//			autorizacion = respuestaCr.get(5);
			
			responsecode = response.getResponseCode();
			autorizacion = response.getAuthorizationNumber();

			BigDecimal amount_bank = BigDecimal.ZERO;
			BigDecimal prs_Amount = BigDecimal.ZERO;
			BigDecimal ref_Amount = BigDecimal.ZERO;
			
			int pursha_cant = trm.getCant_transacciones();
			int refund_cant = 0;
			
			try {
				
//				prs_Amount  = new BigDecimal(respuestaCr.get(11).trim());	 // purshamount: monto por compras
//				ref_Amount  = new BigDecimal(respuestaCr.get(12).trim());	 // retamount: monto por devoluciones.
//				pursha_cant = Integer.parseInt(respuestaCr.get(13).trim());	 // purshtrans: cantidad de compras
//				refund_cant = Integer.parseInt(respuestaCr.get(14).trim());  // retrans: Cantidad de devoluciones
//				batchnumber = Integer.parseInt(respuestaCr.get(5));			 // batchnumber: Numero de lote
//				amount_bank = prs_Amount;
				
				if(response.getResponseCode().compareTo( ResponseCodes.RSPC21.CODE ) != 0)
				{
					prs_Amount = new BigDecimal( response.getSalesAmount().trim() ) ;
					ref_Amount = new BigDecimal( response.getRefundsAmount().trim() ) ;
					pursha_cant= Integer.parseInt( response.getSalesTransactions().trim() ) ;
					refund_cant= Integer.parseInt( response.getRefundsTransactions().trim() ) ;
					batchnumber = Integer.parseInt( autorizacion.trim() ) ;
				}
				 
				
			} catch (Exception e) {
				e.printStackTrace(); 
//				LogCrtl.sendLogDebgs(e);
			}
			if(prs_Amount.compareTo(BigDecimal.ZERO) == 1){
				prs_Amount = prs_Amount.divide(new BigDecimal("100"), 2, PropertiesSystem.ROUND_MODE);
				ref_Amount = ref_Amount.divide(new BigDecimal("100"), 2, PropertiesSystem.ROUND_MODE);
				amount_bank = prs_Amount;
			}
			
			trm.setCant_Creditos(pursha_cant);
			trm.setCant_Reembolsos(refund_cant);
			trm.setMto_Creditos(prs_Amount);
			trm.setMto_Reembolsos(ref_Amount);
			trm.setBatchnumber(batchnumber);
			
			//&& ======  insertar el registro del cierre en CierreSpos
			sesion = HibernateUtilPruebaCn.currentSession();
			trans = (newCn = !(sesion.getTransaction().isActive())) ? sesion
					.beginTransaction() : sesion.getTransaction();
			
			CierreSpos cs = new CierreSpos(trm.getTerminalid(),
					trm.getAfiliado(), batchnumber, trm.getTrans_fechaHasta(),
					trm.getTrans_fechaHasta(), pursha_cant, prs_Amount,
					(pursha_cant + refund_cant), reference, autorizacion,
					responsecode, systraceno, trm.getCaid(), trm.getCodcomp(),
					trm.getMoneda(), trm.getCoduser(), trm.getUsrCodreg(),
					trm.getTotalcierre(), amount_bank, refund_cant, ref_Amount,
					new Date());
			sesion.save(cs);
			
			if(!actualizacion)
				return msgCierreTerminal;
			
			//&& ========== Actualizar las transacciones, ponerles estado aplicado
			List<Transactsp>transacciones = trm.getTransacciones();
			
			if(transacciones == null)
				transacciones = new ArrayList<Transactsp>();
			
			 
			String sql = " update " + PropertiesSystem.ESQUEMA
					+ ".Transactsp set stat_cred = 1, status = 'APL'"
					+ " where termid = '" + trm.getTerminalid()
					+ "' and status = 'PND' and stat_cred = 0";
			
			try {
				sesion.createSQLQuery(sql).executeUpdate();
			} catch (Exception e) {
				e.printStackTrace(); 
//				LogCrtl.sendLogDebgs(e);
//				LogCrtl.imprimirError(e);
			}
			for (Transactsp ts : transacciones) {
				
				if(ts == null )
					continue;
				
				ts.setStatcred(1);
				ts.setStatus("APL");
			}
			
			//&& ===== Traer las transacciones que estan en el intervalo de fechas y que no fueron aplicadas. 
			@SuppressWarnings("unchecked")
			List<Transactsp> transNoAplicadas = (List<Transactsp>) sesion
					.createCriteria(Transactsp.class)
					.add(Restrictions.eq("termid", trm.getTerminalid()))
					.add(Restrictions.ne("status", "PND"))
					.add(Restrictions.eq("statcred", 0))
					.add(Restrictions.between("transdate",
							trm.getTrans_fechaDesde(),
							trm.getTrans_fechaHasta())).list();
			
			if(transNoAplicadas != null && !transNoAplicadas.isEmpty())
				transacciones.addAll(transNoAplicadas);
			
			trm.setTransacciones(transacciones);
			
			//&& ========== Crear el reporte en pdf 
			String[] strParts = crearReporteCierre(trm);
			if(strParts == null)
				strParts = new String[]{"","",""};
			
			for (int i = 0; i < strParts.length; i++) {
				B64strfile b64StrPart = new B64strfile(strParts[i], i,
										cs.getIdcierrespos(), 68, 
										String.valueOf(cs.getIdcierrespos()));
				try {
					sesion.save(b64StrPart);
				} catch (Exception e) {
					e.printStackTrace(); 
//					LogCrtl.imprimirError(e);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace(); 
//			LogCrtl.sendLogDebgs(e);
//			LogCrtl.imprimirError(e);
			
			msgCierreTerminal = "Error al procesar datos para cierre de "
					+ "Socket POS, favor intente nuevamente";
			
			if(response.getResponseCode().compareTo( ResponseCodes.APROBADO.CODE ) != 0 ) {
				trm.setTerm_cerrada(false);
			}
			
		}finally{
			
			//&& ====== Verificar las conexiones
			if (msgCierreTerminal.isEmpty() && trans != null
					&& trans.isActive() && newCn) {
				try {
					trans.commit();
				} catch (Exception e2) {
					e2.printStackTrace(); 
//					LogCrtl.sendLogDebgs(e2);
				}
				try {
					if(newCn && sesion != null && sesion.isOpen() )
					HibernateUtilPruebaCn.closeSession(sesion);
				} catch (Exception e2) {
					e2.printStackTrace(); 
//					LogCrtl.sendLogDebgs(e2);
				}
			}
		}
		return msgCierreTerminal;
	}
	/**
	 * @param caid: Codigo de la caja
	 */
	@SuppressWarnings("unchecked")
	public static List<TransaccionTerminal> crearResumenPorTerminal(int caid, String coduser, int codemp) {
		Session sesion = null;
		Transaction trans = null;
		boolean cnNueva = false;
		List<TransaccionTerminal> resumenTerminal = new ArrayList<TransaccionTerminal>();
		
		try {
			
			sesion = HibernateUtilPruebaCn.currentSession();
			trans = (cnNueva = !(sesion.getTransaction().isActive())) ? sesion
					.beginTransaction() : sesion.getTransaction();

			// && ===== consultar los afiliados para la caja
//			String sql = " SELECT distinct term_id, term_name, c6rp01, "12345678" c6afil" +
//			String sql = " SELECT distinct term_id, lower(term_name),'', '12345678' c6afil " +

					/*
			String sql = " SELECT distinct term_id, lower(term_name),'', " +
					"SUBSTR(xmlserialize(xmlagg(xmltext(CONCAT( ', ', trim(c6afil) ))) as VARCHAR(1024)), 3) c6afil " +
					
					" from "+PropertiesSystem.ESQUEMA+".F55CA016 afl " +
					" inner join "+PropertiesSystem.ESQUEMA+".term_afl term " +
					" on cast(card_acq_id as integer) = c6afil and status = 'A'" +
					" where term.term_id <> '' ";
			
			if(caid > 0 )
				sql += " and c6id = " + caid;
			*/
			
			String sql = " select term_id, lower(term_name), '' codcomp, card_acq_id " +  
					 " from " + PropertiesSystem.ESQUEMA + ".term_afl where status = 'A' order by rownumber  ";
					
			List<Object[]>objAfiliados = sesion.createSQLQuery(sql).list();
			if(objAfiliados == null || objAfiliados.isEmpty())
				return null;
			
			//&& ====== Terminales por afiliado
			String termid = "";
			String nombreterm = "";
			String codcomp = "";
			String afiliado = "";
			
			List<Transactsp>transacciones = new ArrayList<Transactsp>();
			
			for (Object[] dtaPOS : objAfiliados) {
				termid = String.valueOf(dtaPOS[0]).trim() ;
				nombreterm = String.valueOf(dtaPOS[1]).trim() ;
				codcomp = String.valueOf(dtaPOS[2]).trim() ;
				afiliado = String.valueOf(dtaPOS[3]).trim() ;
				
				if(nombreterm.trim().length() > 30)
					nombreterm = nombreterm.substring(0,30);
				
				
				//&& ======== Lista de Transacciones
				/*
				 * transacciones = (List<Transactsp>) sesion
						.createCriteria(Transactsp.class)
						.add(Restrictions.eq("termid",termid ))
						.add(Restrictions.eq("status", "PND"))
						.add(Restrictions.eq("statcred", 0)).list();
				 * 
				 */
				
				
				Criteria cr = sesion.createCriteria(Transactsp.class)
						.add(Restrictions.eq("termid",termid ))
						.add(Restrictions.eq("status", "PND"))
						.add(Restrictions.eq("statcred", 0))
						.add(Restrictions.sqlRestriction( "trim(acqnumber) = '" + afiliado.trim() +"' "));
				
				if(caid > 0 )
					cr.add(Restrictions.eq("caid", caid));
				
				 transacciones = (List<Transactsp>) cr.list(); 
				
				if(transacciones == null)
					transacciones = new ArrayList<Transactsp>();
				
				//&& ======= Ordenar la lista por fecha
				Collections.sort(transacciones, new Comparator<Transactsp>() {
//					@Override
					public int compare(Transactsp t1, Transactsp t2) {
						return t1.getTransdate().compareTo(t2.getTransdate());
					}
				});
				BigDecimal total = BigDecimal.ZERO;
				for (Transactsp trs  : transacciones) {
					total = total.add(trs.getAmount());
				}
				
				int iCantidadTrans = 0;
				Date fechaini = new Date();
				Date fechafin = new Date();
				
				if (!transacciones.isEmpty()) {
					iCantidadTrans = transacciones.size();
					fechaini = transacciones.get(0).getTransdate();
					fechafin = transacciones.get(transacciones.size() - 1)
							.getTransdate();
				}
				TransaccionTerminal rsmTer = new TransaccionTerminal(false,
						termid, afiliado, iCantidadTrans, iCantidadTrans, 0,
						total, nombreterm, fechaini, fechafin, codcomp, "",
						iCantidadTrans, 0, total, BigDecimal.ZERO,
						transacciones, caid, coduser, codemp);
				
				resumenTerminal.add(rsmTer);
			}
			
		} catch (Exception e) {
			e.printStackTrace(); 
//			LogCrtl.sendLogDebgs(e);
//			LogCrtl.imprimirError(e);
			resumenTerminal = new ArrayList<TransaccionTerminal>();
		}finally{
			if(cnNueva){
				try {  trans.commit(); } 
				catch (Exception e2) { }
				try {  HibernateUtilPruebaCn.closeSession(sesion); }
				catch (Exception e2) { }
			}
			sesion = null;
			trans = null;
		}
		return resumenTerminal;
	}
	/**
	 * obtener transacciones flotantes para socket pos de las terminales de una caja.
	 */
	@SuppressWarnings("unchecked")
	public static List<Transactsp> getTransactionCaid(int caid){
		List<Transactsp> transactions = null;
		Session sesion = null;
		Transaction trans = null;
		boolean newCn = false;
		
		try {
			sesion = HibernateUtilPruebaCn.currentSession();
			trans = (newCn = !(sesion.getTransaction().isActive())) ? sesion
					.beginTransaction() : sesion.getTransaction();
			
			String sql = " select distinct ts.* from  "
					+ PropertiesSystem.ESQUEMA + ".F55CA016 afl inner join " 
					+ PropertiesSystem.ESQUEMA + ".term_afl term on " +
					"cast(card_acq_id as integer) = c6afil inner join "
					+ PropertiesSystem.ESQUEMA + ".transactsp ts on " +
					" ts.termid = term.term_id where c6id = ts.caid " + 
				    " and term.status = 'A' and ts.stat_cred = 0  " +
					" and ts.caid = " + caid+" and ts.status = 'PND' ";
			
			transactions = (ArrayList<Transactsp>) sesion.createSQLQuery(sql)
									.addEntity(Transactsp.class).list();
			
		} catch (Exception e) {
			e.printStackTrace(); 
//			LogCrtl.sendLogDebgs(e);
			transactions = null;
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
		return transactions;
	}
	
	/** operaciones para realizar el cierre de la terminal */
	@SuppressWarnings("unchecked")
	public static String aplicarCierreTerminal(ArrayList<String>terminales, 
						int caid, String codcomp, String codsuc, int noarqueo,
						Date fechahora ){
		
		Session sesion = null;
		Transaction trans = null;
		boolean bNuevacn = false;
		String msgCierre = new String("");
		ArrayList<String>termsapls = new ArrayList<String>() ;
		ArrayList<ArrayList<String>> resultCierres = new ArrayList<ArrayList<String>>();
		
		try {

			//&& ======== Cerrar las terminales.
			for (String termid : terminales) {
				ArrayList<String>datosCierre = cerrarTerminal(termid);
				if(datosCierre == null || datosCierre.get(0).compareTo("00") != 0){
					msgCierre += "Cierre No aplicado para terminal " + termid
							+ ", favor aplicar cierre manual";
					continue;
				}
				resultCierres.add(datosCierre);
			}
			
			if(resultCierres.isEmpty())
				return msgCierre = "00Cierres de terminales no aplicados";
			
			//&& ====== Grabar la informacion de las terminales en caja.
			sesion = HibernateUtilPruebaCn.currentSession();
			
			for (ArrayList<String> datosCierre : resultCierres) {
				
				CierreSktpos cs = new CierreSktpos(caid, codcomp, codsuc,
									noarqueo, 10001, fechahora, fechahora,
									datosCierre.get(2), datosCierre.get(3),
									datosCierre.get(4), datosCierre.get(5),
									datosCierre.get(6), datosCierre.get(11),
									datosCierre.get(12), datosCierre.get(13),
									datosCierre.get(14), ""/*termid*/);
				
				sesion.save(cs);
			}
			
			
			
			//&& ========= Cerrar cada una de las terminales
			for (String termid : terminales) {
				
/*
				//&& ========= grabar el encabezado del cierre.
				CierreSktpos cs = new CierreSktpos(caid, codcomp, codsuc,
									noarqueo, 10001, fechahora, fechahora,
									datosCierre.get(2), datosCierre.get(3),
									datosCierre.get(4), datosCierre.get(5),
									datosCierre.get(6), datosCierre.get(11),
									datosCierre.get(12), datosCierre.get(13),
									datosCierre.get(14), termid);
				sesion.save(cs);
				
				System.out.println("====: Monto total emitido por cierre: "+datosCierre.get(6)
									+", Transacciones: "+datosCierre.get(12));*/
				
				//&& ========= actualizar las transacciones para la terminal
				ArrayList<Transactsp>transactsp = (ArrayList<Transactsp>)
						sesion.createCriteria(Transactsp.class)
						.add(Restrictions.eq("transdate", fechahora ))
						.add(Restrictions.le("transtime", fechahora))
						.add(Restrictions.eq("termid", termid ))
						.add(Restrictions.eq("caid", caid))
						.add(Restrictions.eq("codcomp",codcomp ))
						.add(Restrictions.eq("status", "PND")).list();
				
				if(transactsp != null && !transactsp.isEmpty()){
					BigDecimal amount = BigDecimal.ZERO;
					for (Transactsp ts : transactsp) {
						amount = amount.add(ts.getAmount());
						ts.setStatus("CNL");
						sesion.update(ts);
					}
					System.out.println("====: Monto total emitido por caja: "+amount.toString()
							+", Transacciones: "+transactsp.size());
				}
				termsapls.add(termid);
			}
			
		} catch (Exception e) {
//			LogCrtl.imprimirError(e);
			msgCierre = "Proceso de cierre de terminales no completado," +
						" Terminales aplicadas: "+termsapls.toString();
		}finally{
			if(bNuevacn){
				try {
					trans.commit();
				} catch (Exception e) {
					if( !e.getMessage().toLowerCase().contains("function secuence error") ){
						msgCierre = "01Proceso de cierre de terminales no completado";
					}
				}
				try{HibernateUtilPruebaCn.closeSession(sesion);}catch(Exception e){}
			}
			sesion = null;
			trans = null;
			
			if (terminales != null && termsapls.size() != terminales.size()
					&& msgCierre.compareTo("") == 0) {
				msgCierre = "Cierre de terminales no completado, "
						+ "Aplicadas: " + termsapls.toString();
			}
		}
		return msgCierre;
	}
	
	/** transacciones del socket pos para la caja por fecha hora */
	@SuppressWarnings("unchecked")
	public static ArrayList<String> getTransactSp(int caid, String codcomp, Date fecha,
						Date hora,String status, String moneda){
		ArrayList<String>terminales = null;
		Session sesion = null;
		Transaction trans = null;
		boolean bNuevacn = false;
		
		try {
			sesion = HibernateUtilPruebaCn.currentSession();
			if(sesion.getTransaction().isActive())
				trans = sesion.getTransaction();
			else{
				trans = sesion.beginTransaction();
				bNuevacn = true;
			}
					
			terminales = (ArrayList<String>)sesion.createCriteria
					(Transactsp.class).setProjection(Projections
					.distinct(Projections.property("termid")))
					.add(Restrictions.eq("caid", caid))
					.add(Restrictions.eq("codcomp", codcomp))
					.add(Restrictions.eq("transdate", fecha))
					.add(Restrictions.le("transtime", hora))
					.add(Restrictions.le("status", status))
					.add(Restrictions.le("currency", moneda)).list();

			if(terminales == null)
				terminales = new ArrayList<String>();
			
		} catch (Exception e) {
//			LogCrtl.imprimirError(e);
			e.printStackTrace();
		}finally{
			if(bNuevacn){
				try{trans.commit();}catch(Exception e){}
				try{HibernateUtilPruebaCn.closeSession(sesion);}catch(Exception e){}
			}
			sesion = null;
			trans = null;
		}
		return terminales;
	}
	/** afiliados con socket pos configurados para una caja*/
	public static List<SelectItem> getAfiliadosSp(int iCaid, String sCodcomp, 
					String sLineaFactura,String sMoneda){
		
		List<SelectItem>lstPOS = new ArrayList<SelectItem>();
		
		try {
			lstPOS.add(new SelectItem("01", "--Afiliados--"));
			
			Cafiliados[] cafiliado = new AfiliadoCtrl().
								obtenerAfiliadoxCaja_Compania(iCaid,
								sCodcomp, sLineaFactura, sMoneda);
			if(cafiliado == null || cafiliado.length == 0)
				return lstPOS;
			
			List<Cafiliados> lstCafiliados = AfiliadoCtrl.getTerminalSocketPos(cafiliado);
			if(lstCafiliados == null || lstCafiliados.isEmpty())
				return lstPOS;
			
			for (Cafiliados ca : lstCafiliados) {
				SelectItem si = new SelectItem();
				si.setValue(ca.getId().getCxcafi().trim() + "," + ca.getTermid());
				si.setLabel(ca.getId().getCxdcafi().trim());
				String sDescrip = ca.getId().getCxcafi().trim()+", "+ ca.getId().getCxdcafi().trim();
				sDescrip += ", LN:";
				sDescrip +=	(String.valueOf(ca.getId().getC6rp07()).trim().equalsIgnoreCase(""))?
							"S/L": ca.getId().getC6rp07().trim(); 
				sDescrip += "," + ca.getTermid();
				si.setDescription(sDescrip);
				lstPOS.add(si);
			}
			
		} catch (Exception error) {
			lstPOS = new ArrayList<SelectItem>();
			LogCajaService.CreateLog("getAfiliadosSp", "ERR", error.getMessage());
		}
		return lstPOS;
	}
	
/********* imprimir el voucher del socket pos por cada tarjeta usada ***********/
	public static void imprimirVoucher(List<MetodosPago>lstPagos, String sTipoTrans,
									String sNombrecomp, String sImpresora){
		try {
			
			for (MetodosPago mp : lstPagos) {
				
				if(mp.getMetodo().compareTo("H") != 0 && 
						mp.getVmanual().compareTo("2") != 0 )
						continue;
				
				String sVarios =  ((sTipoTrans.compareTo("A") == 0)?"-":"")+
						
						//String.format("%.2f", mp.getMontopos() ) +
						String.format("%1$,.2f", mp.getMontopos() ) +
						
						";" + sTipoTrans + ";" +sImpresora.trim()  + ";" +
						mp.getReferencia() + ";" + mp.getMoneda() + ";" + 
						mp.getNombre() ;
				
				try {
					
					p55ca090 = getP55ca090();
					
					if(p55ca090 == null)
						return;
					
					p55ca090.setCIA("     "+sNombrecomp);
					p55ca090.setTERMINAL(mp.getTerminal());
					p55ca090.setDIGITO(mp.getReferencia3());
					p55ca090.setREFERENC(mp.getReferencia4());
					p55ca090.setAUTORIZ(mp.getReferencia5());
					p55ca090.setFECHA(mp.getReferencia6());
					p55ca090.setVARIOS(sVarios);
					 
					
					p55ca090.invoke();
					p55ca090.disconnect() ;
					 
					
				}catch (Exception ex) {
//					LogCrtl.imprimirError(ex);
//					LogCrtl.sendLogDebgs(ex);
					ex.printStackTrace();
					continue;
				}finally{
					p55ca090 = null;
				}
			}
		} catch (Exception ex) {
//			LogCrtl.imprimirError(ex);
			ex.printStackTrace();
		}
	}
	/****************** Aplicar el la devolucion de pago con  de socket pos *************************************/	
	@SuppressWarnings("unchecked")
	public String aplicarDevolucionSp(List<Recibodet>lstRecibo, 
									List<MetodosPago>lstPagosDev, 
									F55ca014 compania){
		String msgError = new String("");
		List<MetodosPago> mpAnulados = new ArrayList<MetodosPago> ();
		
		try {
			
			//&& ========= filtrar solamente los pagos que son de socket
			List<Recibodet> pagosSocket = (ArrayList<Recibodet>)
					CollectionUtils.select(lstRecibo, new Predicate() {
						public boolean evaluate(Object o) {
							Recibodet rd = (Recibodet)o;
							return (
								rd.getId().getMpago().compareTo("H")  == 0 &&
								rd.getVmanual().compareTo("2") == 0
							);
						}
				});
			
			//&& ========= anular cada pago con socket.
			for (Recibodet rdFact : pagosSocket) {
				
				TermAfl terminal = getTerminalxAfiliado( rdFact.getId().getRefer1().trim() ) ;
				
				
				if(terminal == null) {
					return msgError = "No se ha encontrado terminal" +
							" para afiliado "+rdFact.getId().getRefer1() ;
				}
				
				
				
				// && ========== Anulacion del cobro en Credomatic
				ExecuteTransactionResult tr = 
						PosCtrl.anularCobroCredomatic(
								terminal.getId().getTermId(), 
								rdFact.getId().getRefer4().trim(),
								rdFact.getRefer5().trim(), 
								rdFact.getRefer7().trim() 
							);
				
				String responseCode = "";
				if(tr != null) {
					responseCode = tr.getResponseCode();
				}
				
				Response rs =  TransactionsSocketPosBac.response;
				
				/*
				List<String> lstRespuesta = anularTransaccionPOS(
								terminal.getId().getTermId(), 
								rdFact.getId().getRefer4(),
								rdFact.getRefer5(), rdFact.getRefer7());
				*/
				//&& ========= conservar datos del pago en la devolucion.
//				if(lstRespuesta.get(0).compareTo("00") == 0){
				
				if(responseCode.compareTo( ResponseCodes.APROBADO.CODE) == 0 ){	
				
					final Recibodet rd = rdFact;
					
					MetodosPago mpDev = (MetodosPago)
							
					CollectionUtils.find( lstPagosDev, new Predicate() {
						public boolean evaluate(Object o) {
							MetodosPago mpDev = (MetodosPago)o;	
							boolean dtaSock = true;
							boolean dtaPago = true;
							 
							if(mpDev.getReferencia4() != null &&  !mpDev.getReferencia4().trim().isEmpty() ){
								
								dtaSock =  
								mpDev.getReferencia4().trim().compareTo( rd.getId().getRefer4().trim()) == 0 &&
								mpDev.getReferencia5().trim().compareTo( rd.getRefer5().trim() ) == 0 ;
							}
							dtaPago = mpDev.getMetodo().compareTo("H") == 0
									&& mpDev.getVmanual().compareTo("2") == 0
									&& mpDev.getMoneda().compareTo( rd.getId().getMoneda()) == 0
									&& new BigDecimal(Double.toString(mpDev .getMonto() ) ).compareTo(rd.getMonto()) == 0
									&& mpDev.getReferencia3().trim().compareTo( rd.getId().getRefer3().trim() ) == 0	;
							
							return dtaSock && dtaPago ;
						}
					});

					
					mpDev.setStatuspago("ANL");
					mpDev.setTerminal( terminal.getId().getTermId() );				//codigo de la terminal.
					mpDev.setReferencia3( rd.getId().getRefer3() );					//ult 4 de tarjeta
					mpDev.setReferencia4( tr.getReferenceNumber().trim() );			//referencia
					mpDev.setReferencia5( tr.getAuthorizationNumber().trim() );		//autorizacion
					mpDev.setReferencia6( rs.getTransactionDate() ) ;				//Fecha socket
					
					//mpDev.setReferencia7( tr.getSystemTraceNumber().trim() );	
					mpDev.setReferencia7( rdFact.getRefer7().trim() );				//systraceno (transaccion original)
					
					mpDev.setNombre( rd.getNombre() );
					mpAnulados.add(mpDev);
					
					
					/*
					mpDev.setStatuspago("ANL");
					mpDev.setTerminal( terminal.getId().getTermId() );				//codigo de la terminal.
					mpDev.setReferencia3( rd.getId().getRefer3() );					//ult 4 de tarjeta
					mpDev.setReferencia4( lstRespuesta.get(4) );						//referencia
					mpDev.setReferencia5(lstRespuesta.get(5));						//autorizacion
					mpDev.setReferencia6(formatFechaSocket( 						//Fecha socket
							lstRespuesta.get(3), lstRespuesta.get(2)));
					mpDev.setReferencia7(rd.getRefer7());							//systraceno (transaccion original)
					mpDev.setNombre(rd.getNombre());
					mpAnulados.add(mpDev);
					
					*/
					
				}else{
					msgError +=  "\nCargo "+rdFact.getId().getMonto()
							+" no revertido para tarjeta #" 
							+ rdFact.getId().getRefer3();
				}
			}
			
		} catch (Exception e) {
//			LogCrtl.imprimirError(e);
			e.printStackTrace();
//			LogCrtl.sendLogDebgs(e);
			msgError = "Error al aplicar anulación de pagos con Tarjeta de Crédito";
		}finally{
			
			//&& ============ Vouchers anulados.
			if( !mpAnulados.isEmpty() )
				imprimirVoucher(mpAnulados, "A", 
						compania.getId().getC4rp01d1(), 
						compania.getId().getC4prt());
			
			//&& ============ Actualizar registros de pagos en TransactPos
			actualizarTransacciones(mpAnulados, lstRecibo.get(0)
						.getId().getCaid(), compania.getId()
						.getC4rp01(), lstRecibo.get(0).getId().getTiporec());
		}
		return msgError;
	}

	/****************** Aplicar el pago de socket pos *************************************/	
	public static String aplicarPagoSocketPos(List<MetodosPago> lstPagos, 
						String nombreCliente, int caid, String codcomp, 
						String tiporec ){
		
		String msgSocket  = new String("");
		String sTerminal  = new String("");
		String sNotarjeta = new String("");
		String nombre     = new String("");
		List<String> lstRespuesta = new ArrayList<String>();
		List<MetodosPago>lstPagados = new ArrayList<MetodosPago>(lstPagos.size());
		
		try {
			
			for (int i = 0; i < lstPagos.size(); i++) {
				MetodosPago mp = lstPagos.get(i);
				
				if(mp.getMetodo().compareTo("H") != 0 && 
					mp.getVmanual().compareTo("2") != 0 )
					continue;
				
				mp.setStatuspago("SCC"); // && Sin clasificacion de cargos
				
				//&& ===== aplicar el pago por lectura de banda
				if(mp.getTrack().compareTo("") != 0 ){
					
					
					List<String>datosBanda = Divisas.obtenerDatosTrack(mp.getTrack());

					if( datosBanda == null || datosBanda.isEmpty() )
						return msgSocket = "Datos leídos incorrectamente, " +
								"favor deslice nuevamente la tarjeta";
					
					mp.setFechavencetc( datosBanda.get((datosBanda.size()-1))
										.substring(0, 4) );

					lstRespuesta = realizarPago( mp.getTerminal(), 
							Double.toString(mp.getMontopos()), 
							mp.getTrack(), datosBanda);
					
					sNotarjeta = datosBanda.get(1);
					nombre     = datosBanda.get(2) + (
							(datosBanda.get(3).trim().matches("^\\d+$"))? 
									"":" "+ datosBanda.get(3).trim());
					
				}else{
					//&& ===== aplicar el pago por ingreso manual
					lstRespuesta =  realizarPagoManual(sTerminal,
									Double.toString(mp.getMonto()),
									mp.getReferencia4(),mp.getReferencia5());
					
					sNotarjeta = mp.getReferencia4();
					nombre = nombreCliente ;
				}
			
				if( lstRespuesta == null || lstRespuesta.isEmpty()){
					return msgSocket = "Proceso Cancelado, Pago a tarjeta: "
									+sNotarjeta +" no aplicado, intente de nuevo.";
				}
				
				//&& ==== Conservar los ultimos 4 digitos de la tarjeta
				sNotarjeta = (sNotarjeta).substring(sNotarjeta.length()-4,
								sNotarjeta.length());
				
				mp.setResponsecode(lstRespuesta.get(0)) ;  		 // Codigo de respuesta del socket;
				
				if(lstRespuesta.get(0).compareTo("00") == 0 || 
					lstRespuesta.get(0).compareTo("08") == 0 ){
					
					mp.setStatuspago("PND");
					mp.setReferencia3(sNotarjeta);				//ult 4 de tarjeta
					mp.setReferencia4(lstRespuesta.get(4));		//referencia
					mp.setReferencia5(lstRespuesta.get(5));		//autorizacion
					
					//&& ======== socket devuelve formato mmddyyyy y debe ser pasada a mmm dd,yy 09012008 = Sep 01, 08
					mp.setReferencia6(formatFechaSocket( lstRespuesta.get(3),
										lstRespuesta.get(2)));
					
					mp.setReferencia7(lstRespuesta.get(6));//systraceno
					mp.setNombre( ( (nombre.isEmpty())? nombreCliente : nombre) );
					lstPagos.set(i, mp);
					lstPagados.add(mp);
					
					continue;
				}
				if(lstRespuesta.get(0).compareTo("error") == 0 || 
					lstRespuesta.get(1).trim().toLowerCase().contains("time out")){
					mp.setStatuspago("NCE"); // comunication error
					return msgSocket = "No se ha podido establecer conexión " +
							"con servidor Credomatic, favor intente nuevamente";
				}else{
					mp.setStatuspago("DNG"); // tarjeta denegada
					return msgSocket =  "Cargo a tarjeta " +sNotarjeta
						+" no autorizado\n Motivo : "+ new Divisas()
							.ponerCadenaenMayuscula(lstRespuesta.get(1));
				}
			}
		} catch (Exception e) {
//			LogCrtl.imprimirError(e);
//			LogCrtl.sendLogDebgs(e);
			e.printStackTrace();
			return msgSocket  = "Cargos no aplicados por error interno de sistema";
		}finally{
			
			//&& ======= revertir los pagos que ya fueron aplicados.
			if( msgSocket.compareTo("") != 0 && !lstPagados.isEmpty() ){
				for (MetodosPago mp : lstPagados) {
					
					lstRespuesta =	anularTransaccionPOS(
							mp.getTerminal(), mp.getReferencia4(), 
							mp.getReferencia5(), mp.getReferencia7() );
					
					if(lstRespuesta.get(0).equals("00")){
						mp.setStatuspago("ANL");
						continue;
					}
					
					msgSocket  +=  "\nReversión manual requerida:  "
							+mp.getReferencia3()+ " "
							+ Double.toString(mp.getMonto());
				}
			}
			guardarTransacciones(lstPagos, caid, codcomp, nombre, tiporec);
			
			sTerminal = null;
			sNotarjeta = null;
			nombre = null;
			lstRespuesta = null;
			lstPagados = null;
			
		}
		return msgSocket;
	}
	
	public static String revertirPagosAplicados(List<MetodosPago>
						lstPagosAplicados, int caid, String codcomp,
						String tiporec){
		String msgValidaSockPos = new String("");
		
		try {
			List<MetodosPago>lstAnulados = new ArrayList<MetodosPago>();
			for (MetodosPago mp : lstPagosAplicados) {
				
				if(mp.getMetodo().compareTo("H") != 0 && 
						mp.getVmanual().compareTo("2") != 0 )
						continue;
				
				List<String>lstRespuesta = anularTransaccionPOS(
						mp.getTerminal(), mp.getReferencia4(),
						mp.getReferencia5(), mp.getReferencia7() );
				
				if(lstRespuesta.get(0).equals("00")){
					mp.setStatuspago("ANL");
					lstAnulados.add(mp);
					continue;
				}
				msgValidaSockPos += "\nReversión manual requerida para:  "
						+mp.getReferencia3()+ " "
						+ Double.toString(mp.getMonto());
			}
			if(!lstAnulados.isEmpty())
				actualizarTransacciones(lstAnulados, caid, codcomp, tiporec);
			
		} catch (Exception e) {
//			LogCrtl.sendLogDebgs(e);
//			LogCrtl.imprimirError(e);
			e.printStackTrace();
			msgValidaSockPos = "Verificar reversión de transacciones de" +
								" pago para tarjeta de crédito ";
		}finally{
		}
		return msgValidaSockPos;
	}
	
	public static void setLogIntoTransactSPBanco(String ReferenceNumber) throws Exception
{
		boolean bHecho = false;
		Connection cn = null;
		PreparedStatement ps = null;
		String sql = "";
		FechasUtil f = new FechasUtil();
		try{
			

			sql = "insert into "+PropertiesSystem.ESQUEMA+".transactspbanco(TBCAID, TBNUMREC, TBIDTRANSACTCP, TBCODCLIENTE, TBNUMCUOTA, TBNUMDOC1, TBCODRESP, TBDESCRESP, TBCODAUTH, TBTRANSID, TBHASH, TBFLGCSEG, TBFLGDIRB, TBMONTO, TBCANTPTS, TBORDERID, TBCODRESP2, TBUNAME, TBTIME, TBTYPE, TBSTATUS, TBDISPKSC, TBUSRC, TBPANTC, TBPRGC, TBFECHC, TBHORAC, TBUSRM, TBPANTM, TBPRGM, TBFECHM, TBHORAM, TBNTARJETA, TBHASHENV, TBTIMEENV) " +
					  "SELECT TBCAID, TBNUMREC, TBIDTRANSACTCP, TBCODCLIENTE, TBNUMCUOTA, TBNUMDOC1, 2, 'TRANSACCION ANULADA EN EL SOCKETPOS', TBCODAUTH, TBTRANSID, TBHASH, TBFLGCSEG, TBFLGDIRB, TBMONTO, TBCANTPTS, TBORDERID, TBCODRESP2, TBUNAME, TBTIME, TBTYPE, TBSTATUS, TBDISPKSC, TBUSRC, TBPANTC, TBPRGC, TBFECHC, TBHORAC, TBUSRM, TBPANTM, TBPRGM, TBFECHM, TBHORAM, TBNTARJETA, TBHASHENV, TBTIMEENV " + 
					  " FROM "+PropertiesSystem.ESQUEMA+".TRANSACTSPBANCO WHERE TBCODAUTH = (SELECT AUTHORIZATIONID FROM "+PropertiesSystem.ESQUEMA+".TRANSACTSP t "
					  + " WHERE t.REFERENCENUMBER = '"+ ReferenceNumber +"' )";
			  
			
			cn = As400Connection.getJNDIConnection("DSMCAJA");
			
			ps = cn.prepareStatement(sql);
			int rs = ps.executeUpdate();
			
			if(rs>0)bHecho = true;			
			
		/*boolean bHecho = false;
		String sql = "";
		FechasUtil f = new FechasUtil();
		Session sesion = null;

		sesion = HibernateUtilPruebaCn.currentSession();
					
		try{
			
			sql = "insert into "+PropertiesSystem.ESQUEMA+".transactspbanco(tbcaid, tbnumrec, tbcodresp, tbdescresp, " +
					  "tbcodauth, tbtransid, tbhash, tbflgcseg, " +
					  "tbflgdirb, tbmonto, tbcantpts, tborderid, " +
					  "tbcodresp2, tbuname, tbtime, tbtype, " +
					  "tbstatus, tbdispksc, tbusrc, tbpantc, " +
					  "tbprgc, tbfechc, tbhorac, tbntarjeta," +
					  "tbhashenv, tbtimeenv, tbnumdoc1, tbnumcuota, " +
					  "tbcodcliente) " +
					  "SELECT TBCAID, TBNUMREC, TBIDTRANSACTCP, TBCODCLIENTE, TBNUMCUOTA, TBNUMDOC1, 2, 'TRANSACCION ANULADA EN EL SOCKETPOS', TBCODAUTH, TBTRANSID, TBHASH, TBFLGCSEG, TBFLGDIRB, TBMONTO, TBCANTPTS, TBORDERID, TBCODRESP2, TBUNAME, TBTIME, TBTYPE, TBSTATUS, TBDISPKSC, TBUSRC, TBPANTC, TBPRGC, TBFECHC, TBHORAC, TBUSRM, TBPANTM, TBPRGM, TBFECHM, TBHORAM, TBNTARJETA, TBHASHENV, TBTIMEENV " + 
					  " FROM "+PropertiesSystem.ESQUEMA+".TRANSACTSPBANCO WHERE TBCODAUTH = (SELECT AUTHORIZATIONID FROM GCPMCAJA.TRANSACTSP t "
					  + " WHERE t.REFERENCENUMBER = '"+ ReferenceNumber +"' )";
			   
			
			Query q = sesion.createQuery(sql);
			q.executeUpdate();
			*/
		}catch(Exception ex){
			bHecho = false;
			System.out.println("Se capturo una excepcion en PosCtrl.insertarCierreSocketPos: " + ex);
			ex.printStackTrace(); 
//			LogCrtl.sendLogDebgs(ex);
		}
}

	
	public static void actualizarTransacciones(List<MetodosPago> lstPagos,	int caid, String codcomp, String tiporec) {
		Session sesion = null;
		Transaction trans = null;
		boolean newCn = false;

		try {
			
			sesion = HibernateUtilPruebaCn.currentSession();
			trans = (newCn = !(sesion.getTransaction().isActive())) ? sesion
					.beginTransaction() : sesion.getTransaction();

			for (MetodosPago mp : lstPagos) {

				Transactsp tsp = getTransactSpFromMpago(mp, caid, codcomp,	tiporec);

				if (tsp == null)
					continue;

				tsp.setStatus(mp.getStatuspago());

				try {
					LogCajaService.CreateLog("actualizarTransacciones", "HQRY", tsp);
					sesion.update(tsp);
				} catch (Exception e) {
//					LogCrtl.imprimirError(e);
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			LogCajaService.CreateLog("actualizarTransacciones", "ERR", e.getMessage());
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
		} finally {
			if (trans != null && trans.isActive() && newCn) {
				try {
					trans.commit();
				} catch (Exception e2) {
					e2.printStackTrace(); 
//					LogCrtl.sendLogDebgs(e2);
				}
				try {
					if(newCn && sesion != null && sesion.isOpen() )
					HibernateUtilPruebaCn.closeSession(sesion);
				} catch (Exception e2) {
					e2.printStackTrace(); 
//					LogCrtl.sendLogDebgs(e2);
				}
			}
			  sesion = null;
			  trans = null;
		}
	}
	@SuppressWarnings("unchecked")
	public static void guardarTransacciones(List<MetodosPago>lstPagos, 
							int caid, String codcomp, String cliente, String tiporec){
		
		Session sesion = null;
		Transaction trans = null;
		
		try {
			sesion = HibernateUtilPruebaCn.currentSession();
			trans = sesion.beginTransaction();
			
			ArrayList<MetodosPago>lstSockets = (ArrayList<MetodosPago>)
				CollectionUtils.select(lstPagos, new Predicate() {
					public boolean evaluate(Object o) {
						MetodosPago mp = (MetodosPago)o;
						return (
							mp.getMetodo().compareTo("H") == 0 && 
							mp.getVmanual().compareTo("2") == 0
						);
					}
			});
			
			for (MetodosPago mp : lstSockets) {
				
				Transactsp tsp = new Transactsp();
				tsp.setAcqnumber(mp.getReferencia().trim());
				tsp.setAmount(new BigDecimal(Double.toString( mp.getMonto())));
				tsp.setTiporec(tiporec);
				tsp.setCaid(caid);
				tsp.setClientname(cliente.trim().toLowerCase());
				tsp.setCodcomp(codcomp);
				tsp.setCurrency(mp.getMoneda());
				tsp.setTermid(mp.getTerminal());
				tsp.setTransdate(new Date());
				tsp.setTranstime(new Date());
				
				tsp.setExpirationdate(mp.getFechavencetc());
				tsp.setCardnumber(mp.getReferencia3());
				tsp.setAuthorizationid(mp.getReferencia5());
				tsp.setReferencenumber(mp.getReferencia4());
				tsp.setSystraceno(mp.getReferencia7());
				
				tsp.setStatus(mp.getStatuspago());
				tsp.setResponsecode(mp.getResponsecode());
				tsp.setStatcred(0);

				try{
					sesion.save(tsp);
				}catch (Exception e) {
//					LogCrtl.imprimirError(e);
//					LogCrtl.sendLogDebgs(e);
					e.printStackTrace();
				}
			}
			
		} catch (Exception e) {
//			LogCrtl.imprimirError(e);
			e.printStackTrace();
		}finally{
			try{trans.commit();}catch(Exception e){
//				LogCrtl.imprimirError(e);
//				LogCrtl.sendLogDebgs(e);
				e.printStackTrace();
			}
			try{HibernateUtilPruebaCn.closeSession(sesion);
			}catch(Exception e){
//				LogCrtl.imprimirError(e);
//				LogCrtl.sendLogDebgs(e);
				e.printStackTrace();
			}
			sesion = null;
			trans = null;
		}
	}
	
/*****************************************************************************************************/
public boolean cargarLibQN(){
	boolean hecho = true;
	try{
		System.loadLibrary("QtNetworkd4");
	}catch (UnsatisfiedLinkError e) {
	    System.err.println("Native code library failed to load.\n" + e);
    }catch(Exception ex){
    	hecho = false;
		System.out.println("Se capturo una excepcion en PosCtrl.//cargarLibQN: " + ex);
	}
    return hecho;
}

/*****************************************************************************************************/
public static List<String> probarConexion(){
    List<String> lstRespuesta = null;

		try{
			socketposfns = null;
			socketposfns = getSocketFunction();
			if(socketposfns == null) 
				return null;
			
			String result = socketposfns.dlltest();	
			
			String[]split = result.split(";");
			split = Arrays.copyOf(split, 7);
			lstRespuesta  = new ArrayList<String>(Arrays.asList(split));

			if (lstRespuesta == null || lstRespuesta.isEmpty()
					|| lstRespuesta.get(0).compareTo("00") != 0)
				return null;
			
		}catch(Exception ex){
			lstRespuesta = null;
//			LogCrtl.imprimirError(ex);
//			LogCrtl.sendLogDebgs(ex);
			ex.printStackTrace();
		}finally{
			socketposfns = null;
			System.runFinalization();
			System.gc();
		}
		return lstRespuesta;
	}
/*****************************************************************************/
/*****************************************************************************/
	@SuppressWarnings("unchecked")
	public List<Vcierresktpos> leerCierreSocketPos(int caid, String sCodcomp, String moneda, int noarqueo){
		Session sesion = null;
		Transaction trans = null;
		List<Vcierresktpos> lstResult = null;
		boolean bNuevaSesionCaja = false;
		
		try{
		
			if(caid != 0)
				return null;
			
			sesion = HibernateUtilPruebaCn.currentSession();
			if( sesion.getTransaction().isActive() )
				trans = sesion.getTransaction();
			else{
				trans = sesion.beginTransaction();
				bNuevaSesionCaja = true;
			}
			
			String[]sParam = new String[]{"CAID2","CODCOMP","MONEDA","NOARQUEO"};
			Object[]oValor = new Object[]{caid,sCodcomp, moneda, noarqueo};
			
			String sql = "select * from "+PropertiesSystem.ESQUEMA+".vcierresktpos " +
					" where caid = :CAID2 and codcomp = :CODCOMP" +
					" and moneda = :MONEDA and noarqueo = :NOARQUEO";
			
			Query q = sesion.createSQLQuery(sql).addEntity(Vcierresktpos.class);
			for (int i = 0; i < oValor.length; i++) 
				q.setParameter(sParam[i], oValor[i]);
			q.setMaxResults(1);
		
			lstResult = q.list();
			
//			Criteria cr =  sesion.createCriteria(Vcierresktpos.class);
//			cr.add(Restrictions.eq("id.caid",caid ));
//			cr.add(Restrictions.eq("id.codcomp",sCodcomp ));
//			cr.add(Restrictions.eq("id.moneda",moneda ));
//			cr.add(Restrictions.eq("id.noarqueo",noarqueo ));
//			cr.setMaxResults(1);
			
//			lstResult = cr.list();
			
			
//			sql = "from Vcierresktpos as v where v.id.caid = " + caid
//					+ " and v.id.codcomp = '" + sCodcomp
//					+ "' and v.id.moneda = '" + moneda
//					+ "' and v.id.noarqueo = " + noarqueo;
//			lstResult = sesion.createQuery(sql).list();
			
		}catch(Exception ex){
//			System.out.println("Se capturo una excepcion en " +
//					"PosCtrl.leerCierreSocketPos: " );
//			ex.printStackTrace(); LogCrtl.sendLogDebgs(ex);
			ex.printStackTrace();
		}finally{
			if(bNuevaSesionCaja){
				try{trans.commit();} catch(Exception e){e.printStackTrace(); 
//				LogCrtl.sendLogDebgs(e);
				}
				try{HibernateUtilPruebaCn.closeSession(sesion);}
				catch(Exception e){e.printStackTrace(); 
//				LogCrtl.sendLogDebgs(e);
				}
			}
		}
		return lstResult;
	}
/*****************************************************************************/
	public boolean insertarCierreSocketPos(int iCaid,String sCodcomp,
					String sCodsuc, int iNoarqueo,int iCodb,Date fecha, 
					List<String> lstResultSocket,String sTerminal, Session sesion){
		
		boolean bHecho = false;
		String sql = "";
		FechasUtil f = new FechasUtil();
	
		try{
			String sFecha = f.formatDatetoString(fecha,"yyyy-MM-dd");
			String sHora  = f.formatDatetoString(fecha,"HH.mm.ss");
		
			sql = "insert into "+PropertiesSystem.ESQUEMA+".cierre_sktpos (caid,codcomp,codsuc,noarqueo,codb,fecha,hora,horapos," +
				"fechapos,referencia,autorizacion,systraceno,purshamount,retamount,purshtrans,rettrans,term_id) " +
				"values ("+iCaid+",'"+sCodcomp+"','"+sCodsuc+"',"+iNoarqueo+","+iCodb+",'"+sFecha+"','"+sHora+"'," +
					"'"+lstResultSocket.get(2)+"'," + "'"+lstResultSocket.get(3)+"'," + "'"+lstResultSocket.get(4)+"'," +
					"'"+lstResultSocket.get(5)+"'," + "'"+lstResultSocket.get(6)+"'," + "'"+lstResultSocket.get(11)+"'," +
					"'"+lstResultSocket.get(12)+"'," + "'"+lstResultSocket.get(13)+"'," + "'"+lstResultSocket.get(14)+"'," +
					"'"+sTerminal+"')";
			Query q = sesion.createQuery(sql);
			q.executeUpdate();
			
		}catch(Exception ex){
			bHecho = false;
			System.out.println("Se capturo una excepcion en PosCtrl.insertarCierreSocketPos: " + ex);
			ex.printStackTrace(); 
//			LogCrtl.sendLogDebgs(ex);
		}
		return bHecho;
	}
/*****************************************************************************/
	public boolean insertarCierreSocketPos(int iCaid,String sCodcomp,String sCodsuc,int iNoarqueo,int iCodb,Date fecha,List<String> lstResultSocket,String sTerminal){
		boolean bHecho = false;
		Connection cn = null;
		PreparedStatement ps = null;
		String sql = "";
		FechasUtil f = new FechasUtil();
		try{
			String sFecha = f.formatDatetoString(fecha,  "yyyy-MM-dd");
			String sHora = f.formatDatetoString(fecha, "HH.mm.ss");
			sql = "insert into "+PropertiesSystem.ESQUEMA+".cierre_sktpos (caid,codcomp,codsuc,noarqueo,codb,fecha,hora,horapos," +
					"fechapos,referencia,autorizacion,systraceno,purshamount,retamount,purshtrans,rettrans,term_id) " +
					"values ("+iCaid+",'"+sCodcomp+"','"+sCodsuc+"',"+iNoarqueo+","+iCodb+",'"+sFecha+"','"+sHora+"'," +
							"'"+lstResultSocket.get(2)+"'," + "'"+lstResultSocket.get(3)+"'," + "'"+lstResultSocket.get(4)+"'," +
							"'"+lstResultSocket.get(5)+"'," + "'"+lstResultSocket.get(6)+"'," + "'"+lstResultSocket.get(11)+"'," +
							"'"+lstResultSocket.get(12)+"'," + "'"+lstResultSocket.get(13)+"'," + "'"+lstResultSocket.get(14)+"'," +
							"'"+sTerminal+"')";
			
			cn = As400Connection.getJNDIConnection("DSMCAJA");
			
			ps = cn.prepareStatement(sql);
			int rs = ps.executeUpdate();
			
			if(rs>0)bHecho = true;			
		}catch(Exception ex){
			System.out.println("Se capturo una excepcion en PosCtrl.insertarCierreSocketPos: " + ex);
		}finally{
			try {
				ps.close();
				cn.close();
			} catch (SQLException e) {
				e.printStackTrace(); 
//				LogCrtl.sendLogDebgs(e);
			}			
		}
		return bHecho;
	}
	
/*****************************************************************************/
	public TermAfl getTerminalxAfiliado(String afiliado){
		TermAfl ta = null;
		Session s = null;
		
		try{
			s = HibernateUtilPruebaCn.currentSession();
			
			ta = (TermAfl)s.createQuery("from TermAfl as ta where ta.id.cardAcqId like '%"+afiliado+"%'").uniqueResult();
			
		}catch(Exception ex){
//			LogCrtl.imprimirError(ex);
			ex.printStackTrace();
		}
		return ta;
	}
	
	public static Response aplicarCierreTerminal(String terminalid) {
		Response response = null; 
		
		try {
			
			TransactionsSocketPosBac.transaction_type = TransactionType.SETTLEMENT.TYPE;
			TransactionsSocketPosBac.terminalid = terminalid ;
			TransactionsSocketPosBac.executeTransactionRequested();
			
			ExecuteTransactionResult tr = TransactionsSocketPosBac.transaction_result;
			
			if (tr == null) {
				System.out .println(" no se ha podido aplicar cierre de terminal ");
				return null;
			}
			
			response = TransactionsSocketPosBac.response;
			
			System.out .println("  respuesta de cierre credomatic " + response.getResponseCode() + " " + response.getResponseCodeDescription() );
			
		}catch(Exception e) {
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
		} 
		
		return response;
	}
	
	
	/** aplicar el cierre por terminal en servidor de credomati **/
	public static ArrayList<String> cerrarTerminal(String termid){
		String result = new String("");
		ArrayList<String> rsptCierre = null;
		
		try {
			
			//&& ===== Aplicar el cierre del socket
			socketposfns = getSocketFunction();
			if(socketposfns == null) 
				return rsptCierre;
			
			result = socketposfns.dllsettlement(termid);
			
			String[]dtsCierre = result.split(";");
			dtsCierre = Arrays.copyOf(dtsCierre, 15);
			
			rsptCierre = new ArrayList<String>(Arrays.asList(dtsCierre));
			System.out.println("Respuesta cierre terminal "+termid+": "+ rsptCierre);
			
		} catch (Exception e) {
			
//			LogCrtl.imprimirError(e);
			rsptCierre = new ArrayList<String>();
			rsptCierre.add("error");
			rsptCierre.add(e.getMessage());
			rsptCierre.add(e.getLocalizedMessage());
			rsptCierre.add(e + "");
			
		}finally{
			result = null;
			socketposfns = null;
		}
		return rsptCierre;
	}
	
/*****************************************************************************/
	public List<String> realizarCierrePOS(String terminalId){
		int index=0, fin =0;
	    String response_code="", response_desc="", trans_time = "", trans_date = "", reference="", authorization="", systraceno="", 
	    		respsec = "",respavspc = "", respavsad="", respavssa= "", purshamount= "", retamount="", purshtrans="", rettrans="";
		List<String> lstRespuesta = null;
		String result = "";
		try{
			//cargarLibQN();
			//Se carga el api con las funciones (sockpos.dll)
	        sockpos functions = (sockpos) Native.loadLibrary("sockpos", sockpos.class);

	        //Se llama la funcion dllpurchase y se carga la respuesta en la variable result.	      
	        result = functions.dllsettlement(terminalId);
	       
	        functions = null;
	        System.runFinalization();
	        System.gc();
	        //Se muestra el resultado en pantalla.
			//System.out.println(result);//System.out.println(out);
	        
	     
	        lstRespuesta = new ArrayList<String>();
	        //Se segrega la respuesta en las diferentes variables.
		    fin = result.indexOf(';');
		    response_code = result.substring(index, fin); //0
		    lstRespuesta.add(response_code);
		    fin = result.indexOf(';', index = fin + 1);
		    response_desc = result.substring(index, fin);//1
		    lstRespuesta.add(response_desc);
		    fin = result.indexOf(';', index = fin + 1);
		    trans_time = result.substring(index, fin);//2
		    lstRespuesta.add(trans_time);
		    fin = result.indexOf(';', index = fin + 1);
		    trans_date = result.substring(index, fin);//3
		    lstRespuesta.add(trans_date);
		    fin = result.indexOf(';', index = fin + 1);
		    reference = result.substring(index, fin);//4
		    lstRespuesta.add(reference);
		    fin = result.indexOf(';', index = fin + 1);
		    authorization = result.substring(index, fin);//5
		    lstRespuesta.add(authorization);
		    fin = result.indexOf(';', index = fin + 1);
		    systraceno = result.substring(index, fin);//6
		    lstRespuesta.add(systraceno);
		    
		    fin = result.indexOf(';', index = fin + 1);
		    respsec  = result.substring(index, fin);//7
		    lstRespuesta.add(respsec);
		    fin = result.indexOf(';', index = fin + 1);
		    respavspc  = result.substring(index, fin);//8
		    lstRespuesta.add(respavspc);
		    fin = result.indexOf(';', index = fin + 1);
		    respavsad  = result.substring(index, fin);//9
		    lstRespuesta.add(respavsad);
		    fin = result.indexOf(';', index = fin + 1);
		    respavssa  = result.substring(index, fin);//10
		    lstRespuesta.add(respavssa);
		    fin = result.indexOf(';', index = fin + 1);
		    purshamount  = result.substring(index, fin);//11
		    lstRespuesta.add(purshamount);
		    fin = result.indexOf(';', index = fin + 1);
		    retamount  = result.substring(index, fin);//12
		    lstRespuesta.add(retamount);
		    fin = result.indexOf(';', index = fin + 1);
		    purshtrans  = result.substring(index, fin);//13
		    lstRespuesta.add(purshtrans);
		    fin = result.indexOf(';', index = fin + 1);
		    rettrans  = result.substring(index, fin);//14
		    lstRespuesta.add(rettrans);
		    //Debe continuarse leyendo los datos hasta el final de la trama.
		    
	        //Se muestran las variables.
//	        System.out.println("Resultado de la transaccion:\n" +
//	                "Codigo de respuesta: " + response_code + "\n" +
//	                "Descripcion: " + response_desc + "\n" +
//	                "Hora de la transaccion(HHmmss): " + trans_time + "\n" +
//	                "Fecha de la transaccion(MMddyyyy): " + trans_date + "\n" +
//	                "Numero de referencia: " + reference + "\n" +
//	                "Autorizacion: " + authorization + "\n" +
//	                "#Transaccion: " + systraceno + "\n" +
//	                "Monto de Ventas: " + purshamount + "\n" +
//	                "Monto de Devoluciones: " + retamount + "\n" +
//	                "Cantidad de ventas: " + purshtrans + "\n" +	           
//	                "Cantidad de Devoluciones " + rettrans + "\n...");
	        
		} catch (UnsatisfiedLinkError e) {
			lstRespuesta = new ArrayList<String>();
			lstRespuesta.add("error");
			lstRespuesta.add(e.getMessage());
			lstRespuesta.add(e.getLocalizedMessage());
			lstRespuesta.add(e + "");
//		    System.err.println("Native code library failed to load.\n" + e);
		    e.printStackTrace();  
	    }catch(Exception ex){
			lstRespuesta = new ArrayList<String>();
			lstRespuesta.add("error");
			lstRespuesta.add(ex.getMessage());
			lstRespuesta.add(ex.getLocalizedMessage());
			lstRespuesta.add(ex + "");
			ex.printStackTrace(); 
//			LogCrtl.sendLogDebgs(ex);
//			System.out.println("Se capturo una excepcion en PosCtrl.anularTransaccionPOS: " + ex);
		}		
		return lstRespuesta;
	}
	
	/*public static String anularCobroCredomatic(String str_TerminalId, String strReferenceNumber, 
					String strAuthorizationNumber, String strSystemTraceNumber ) {
		String responseCode = "";
		
		try {
			
			TransactionsSocketPosBac.transaction_type = TransactionType.VOID.TYPE;
			TransactionsSocketPosBac.terminalid = str_TerminalId ;
			TransactionsSocketPosBac.transaction_authorization_number = strAuthorizationNumber ;
			TransactionsSocketPosBac.transaction_reference_number =  strReferenceNumber ;
			TransactionsSocketPosBac.transaction_system_trace_number =  strSystemTraceNumber ;
			
			boolean exec = TransactionsSocketPosBac.executeTransactionRequested();
			
			if(!exec){
				return responseCode = "" ;
			}
			
			
			ExecuteTransactionResult tr = TransactionsSocketPosBac.transaction_result;
			responseCode = tr.getResponseCode();
			
			System.out.println(" respuesta anulacion " + tr.getResponseCode() + " " + tr.getResponseCodeDescription()   );
			
			
			
		}catch(Exception ex){
			ex.printStackTrace(); 
			LogCrtl.imprimirError(ex);
			return responseCode = "" ;
		} 
		return responseCode;
	}*/
	
	public static ExecuteTransactionResult anularCobroCredomatic(String str_TerminalId, String strReferenceNumber,
	        String strAuthorizationNumber, String strSystemTraceNumber) {
		ExecuteTransactionResult tr = null;

		try {

			TransactionsSocketPosBac.transaction_type = TransactionType.VOID.TYPE;
			TransactionsSocketPosBac.terminalid = str_TerminalId;
			TransactionsSocketPosBac.transaction_authorization_number = strAuthorizationNumber;
			TransactionsSocketPosBac.transaction_reference_number = strReferenceNumber;
			TransactionsSocketPosBac.transaction_system_trace_number = strSystemTraceNumber;

			boolean exec = TransactionsSocketPosBac.executeTransactionRequested();

			if (!exec) {
				return null;
			}

			tr = TransactionsSocketPosBac.transaction_result;

			System.out.println(" respuesta anulacion " + tr.getResponseCode() + " " + tr.getResponseCodeDescription());

		}
		catch (Exception ex) {
			ex.printStackTrace();
//			LogCrtl.imprimirError(ex);
			return null;
		}
		return tr;
	}
	
	
	
	/*********Cancelar TRANSACCION EN DLL DE CREDOMATIC******************************** 
 	 * @param terminalId afiliado,sRefer Referencia,sAut Autorizacion,Systraceno no trans.
	 * @return List  0. response_code,1. response_desc,2. trans_time, 3. trans_date,4. reference,
	 * 				 ,5. authorization ,6. systraceno
	 */
	public static List<String> anularTransaccionPOS(String terminalId,String sRefer, String sAut,String Systraceno){
		
		List<String> lstRespuesta = null;
		
		try{

			socketposfns = getSocketFunction();
			if(socketposfns == null) return null;
			
			String result = socketposfns.dllcanceltrans(terminalId, sRefer, sAut, Systraceno);
	        socketposfns = null;
	        
			String[]sDtsCancel = result.split(";");
			sDtsCancel   = Arrays.copyOf(sDtsCancel, 7);
			lstRespuesta = new ArrayList<String>(Arrays.asList(sDtsCancel));
			
			System.out.println("respuesta anulacion: "+lstRespuesta);
			
		} catch(Exception ex){
			lstRespuesta = new ArrayList<String>();
			lstRespuesta.add("error");
			lstRespuesta.add(ex.getMessage());
			lstRespuesta.add(ex.getLocalizedMessage());
			lstRespuesta.add(ex + "");
			ex.printStackTrace();
//			LogCrtl.sendLogDebgs(ex);
//			LogCrtl.imprimirError(ex);
		}finally{
		}		
		return lstRespuesta;
	}
/************************************************************************************************************************/
	public static List<String> realizarPagoManual(String terminalId,String sMonto,String sNotarjeta,String sFechavence){
		 //Inicializa los valores para la respuesta.
       int index=0, fin =0;
       String response_code="", response_desc="", trans_time = "", trans_date = "", reference="", authorization="", systraceno="";
       List<String> lstRespuesta = null;
       String result = "";
		try{
			 //cargarLibQN();
	        //Se carga el api con las funciones (sockpos.dll)
	        sockpos functions = (sockpos) Native.loadLibrary("sockpos", sockpos.class);

	        //Se llama la funcion dllpurchase y se carga la respuesta en la variable result.
	        //String result = functions.dllpurchase("50001001", 1, 1, "4509982122105726", "000000010000", "1005", "", "", "", "", 0, 0, "", 0, "", "", "");
	      
	        result = functions.dllpurchase(terminalId, 1, 1, sNotarjeta, sMonto, sFechavence, "", "", "", "", 0, 0, "", 0, "", "", "");
	       
	        functions = null;
	        System.runFinalization();
	        System.gc();
	        //Se muestra el resultado en pantalla.
			System.out.println(result);//System.out.println(out);
	        
	     
	        lstRespuesta = new ArrayList<String>();
	        //Se segrega la respuesta en las diferentes variables.
		    fin = result.indexOf(';');
		    response_code = result.substring(index, fin); //0
		    lstRespuesta.add(response_code);
		    fin = result.indexOf(';', index = fin + 1);
		    response_desc = result.substring(index, fin);//1
		    lstRespuesta.add(response_desc);
		    fin = result.indexOf(';', index = fin + 1);
		    trans_time = result.substring(index, fin);//2
		    lstRespuesta.add(trans_time);
		    fin = result.indexOf(';', index = fin + 1);
		    trans_date = result.substring(index, fin);//3
		    lstRespuesta.add(trans_date);
		    fin = result.indexOf(';', index = fin + 1);
		    reference = result.substring(index, fin);//4
		    lstRespuesta.add(reference);
		    fin = result.indexOf(';', index = fin + 1);
		    authorization = result.substring(index, fin);//5
		    lstRespuesta.add(authorization);
		    fin = result.indexOf(';', index = fin + 1);
		    systraceno = result.substring(index, fin);//6
		    lstRespuesta.add(systraceno);
		    //Debe continuarse leyendo los datos hasta el final de la trama.

	        //Se muestran las variables.
	        System.out.println("Resultado de la transaccion:\n" +
	                "Codigo de respuesta: " + response_code + "\n" +
	                "Descripcion: " + response_desc + "\n" +
	                "Hora de la transaccion(HHmmss): " + trans_time + "\n" +
	                "Fecha de la transaccion(MMddyyyy): " + trans_date + "\n" +
	                "Numero de referencia: " + reference + "\n" +
	                "Autorizacion: " + authorization + "\n" +
	                "#Transaccion: " + systraceno + "\n...");
	        
		} catch (UnsatisfiedLinkError e) {
			lstRespuesta = new ArrayList<String>();
			lstRespuesta.add("error");
			lstRespuesta.add(e.getMessage());
			lstRespuesta.add(e.getLocalizedMessage());
			lstRespuesta.add(e + "");
			e.printStackTrace();
//		      System.err.println("Native code library failed to load.\n" + e);
	    }
		catch(Exception ex){
			lstRespuesta = new ArrayList<String>();
			lstRespuesta.add("error");
			lstRespuesta.add(ex.getMessage());
			lstRespuesta.add(ex.getLocalizedMessage());
			lstRespuesta.add(ex + "");
			ex.printStackTrace();
//			System.out.println("Se capturo una excepcion en PosCtrl.realizarPago: " + ex);
		}
		return lstRespuesta;
	}

	/*********
	 * REALIZAR LA OPCION DE PAGO EN LA DLL 
	 * 
	 * @param terminalId
	 *            afiliado,sMonto monto,sTrack track leido de la
	 *            tarjeta,lstDatosTrack lista de datos segregados leidos del
	 *            track
	 * @return List 0. response_code,1. response_desc,2. trans_time, 3.
	 *         trans_date,4. reference, ,5. authorization ,6. systraceno
	 */
	public static List<String> realizarPago(String terminalId,String sMonto,String sTrack,List<String> lstDatosTrack){
        List<String> lstRespuesta = null;

		try{
			String noTarjeta = lstDatosTrack.get(1);
			String fechavence = lstDatosTrack.get((lstDatosTrack.size()-1)).substring(0, 4);
			
			socketposfns = getSocketFunction();
			if(socketposfns == null) return null;
			
			String result = socketposfns.dllpurchase(terminalId, 2, 1, noTarjeta, 
	        						sMonto, fechavence, sTrack, "", "",
	        						"", 0, 0, "", 0, "", "", "");
	        socketposfns = null;
	        
			String[]sDtsPago = result.split(";");
			sDtsPago = Arrays.copyOf(sDtsPago, 7);
			
			if(sDtsPago[1].toLowerCase().contains("fi"))
				sDtsPago[1] = "DENEGADA, FONDOS INSUFICIENTES";
			if(sDtsPago[0].compareTo("41") == 0 || sDtsPago[0].compareTo("43") == 0)
				sDtsPago[1] = " TARJETA INVÁLIDA POR BLOQUEO O POR VENCIMIENTO ";
			
			lstRespuesta = new ArrayList<String>(Arrays.asList(sDtsPago));
			
		}catch(Exception ex){
//			LogCrtl.sendLogDebgs(ex);
//			LogCrtl.imprimirError(ex);
			ex.printStackTrace();
			lstRespuesta = new ArrayList<String>();
			lstRespuesta.add("error");
			lstRespuesta.add(ex.getMessage());
			lstRespuesta.add(ex.getLocalizedMessage());
			lstRespuesta.add(ex + "");
		}finally{
		}
		return lstRespuesta;
	}
	
	/** *************** validar los datos del pago con la tarjeta ****/
	public static String validaPagoSocket(String sMonedaPago, double monto, 
					double dMontoAplicar, boolean pagomanual, String ref1,
					String ref2, String ref3, int iCaid,  String codcomp, 
					String codpos, List<MetodosPago>selectedMet,
					String sTrack, String notarjeta, String fechavence, double montopago){
		
		String strMensajeValidacion = new String();
		String iconMsg = "<br><img width=\"7\" src=\"/"
				+PropertiesSystem.CONTEXT_NAME
				+"/theme/icons/redCircle.jpg\" border=\"0\" /> ";
		
		try {
			if (monto > dMontoAplicar) 
				strMensajeValidacion += iconMsg + "El monto ingresado "
						+ "para este método de pago debe ser menor"
						+ " o igual al monto a aplicar";
			
			if (codpos.compareTo("01") == 0)
				strMensajeValidacion += "Seleccione un afiliado<br>";
			
			if (ref1.trim().compareTo("") == 0) 
				strMensajeValidacion += iconMsg + " Identificación requerida";
			
			if (!ref1.trim().matches(PropertiesSystem.REGEXP_ALFANUMERIC)) 
				strMensajeValidacion += iconMsg
						+ "El campo <b>Identificación<b/>"
						+ " contiene caracteres inválidos";
			
			if (ref1.length() > 150) 
				strMensajeValidacion += iconMsg
						+ " La cantidad de letras "
						+ "del campo <b>Identificación<b/> es muy alta (lim. 150)<br>";

/*			boolean bMontoMaximo =  new CtrlPoliticas().validarMonto(
									iCaid, codcomp, sMonedaPago, "H", monto);*/
			
			boolean bMontoMaximo = new CtrlPoliticas().validarMonto(iCaid,
					codcomp, sMonedaPago, "H", montopago);
			
			//&& ========== Validar en la clase, que si viene -1 el monto maximo se excedio. bMontoMaximo = false;
			if(!bMontoMaximo)
				return strMensajeValidacion = "-1"; 
			
			//&& ========== Consolidado de montos excedido.
			if ( selectedMet != null && !selectedMet.isEmpty() && dMontoAplicar > monto) {
				
				BigDecimal bdTotalMpagos = BigDecimal.ZERO;
				for (MetodosPago mp : selectedMet) 
					bdTotalMpagos =  bdTotalMpagos.add(new BigDecimal(
									Double.toString(mp.getEquivalente())) ) ;
				
				if(new BigDecimal(Double.toString(dMontoAplicar))
								.compareTo(bdTotalMpagos) != 1)
					strMensajeValidacion += iconMsg + " El consolidado de " +
											"pagos excede el monto aplicar ";
			}
			
			if(pagomanual){
				if(notarjeta.trim().matches("^[0-9]{1,8}$"))
					strMensajeValidacion += iconMsg 
							+ " Número  de Tarjeta inválido";
				
				if(fechavence.trim().matches("^[0-9]{1,8}$"))
					strMensajeValidacion += iconMsg 
							+ " Fecha de Vencimiento inválida";
				
			}else{
				if(sTrack.equals("")){ 
					strMensajeValidacion += iconMsg 
							+ "Datos de Tarjeta requeridos";
				}else{
					List<String> lstDatosTrack = Divisas.obtenerDatosTrack(sTrack);
					if (lstDatosTrack == null || lstDatosTrack.isEmpty())
						strMensajeValidacion += iconMsg + "Error en lectura "
								+ "de tarjeta, favor intente de nuevo ";
				}
			}
			
		} catch (Exception e) {
//			LogCrtl.imprimirError(e);
			e.printStackTrace();
			strMensajeValidacion = "Error al validar los datos de la tarjeta para el pago"; 
		}
		return strMensajeValidacion;
	}
	
/*******************************************************************************************/
	public static String formatFechaSocket(String sFecha,String sHora){
		String mes = "",dia = "", anio = "",sH = "",sMin = "",sSeg = "";	
		int i = 0;
		try{
			mes = sFecha.substring(0, 2);
			dia =  sFecha.substring(2, 4);
			anio =  sFecha.substring(6, 8);
			sH = sHora.substring(0, 2);
			sMin = sHora.substring(2, 4);
			sSeg = sHora.substring(4, 6);
			i = Integer.parseInt(mes);	
			switch(i){
			case 1:
				mes = "Ene";
				break;
			case 2:
				mes = "Feb";
				break;
			case 3:
				mes = "Mar";
				break;
			case 4:
				mes = "Apr";
				break;
			case 5:
				mes = "May";
				break;
			case 6:
				mes = "Jun";
				break;
			case 7:
				mes = "Jul";
				break;
			case 8:
				mes = "Ago";
				break;
			case 9:
				mes = "Sep";
				break;
			case 10:
				mes = "Oct";
				break;
			case 11:
				mes = "Nov";
				break;
			case 12:
				mes = "Dic";
				break;
			}
			sFecha = mes + " " + dia + "," + anio + " - " + sH + ":" +sMin+":"+sSeg+"";
		}catch(Exception ex){
//			LogCrtl.imprimirError(ex);
			ex.printStackTrace();
		}finally{
			mes = null;
			dia = null;
			anio = null;
			sH = null;
			sMin = null;
			sSeg = null;
		}
		return sFecha;
	}
	//***************************************************************************************************/
	//*
	//***************************************************************************************************
	private static sockpos socketposfns;
	private static P55CA090 p55ca090;
	
	/** cargar las librerias para imprimir el voucher del socket pos  */
	private static P55CA090 getP55ca090() {
		try{
			
			p55ca090 = null;
			p55ca090 = (P55CA090) FacesContext
					.getCurrentInstance().getELContext()
					.getELResolver().getValue(FacesContext.
						getCurrentInstance().getELContext(),
						null, "p55ca090"); 
			
			
		} catch (Exception ex) {
			p55ca090 = null;
//			LogCrtl.imprimirError(ex);
			ex.printStackTrace();
		}
		return p55ca090;
	}
	
	public static void loadP55ca090(){
		try { 
			
			p55ca090  = getP55ca090();
			
			p55ca090.invoke();
			
			p55ca090.disconnect() ;
			
			/*getP55ca090().setCIA("4");
			getP55ca090().invoke();*/
			
		} catch (Exception e) {
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
			e.printStackTrace();
		}
	}
	
	
	/** cargar Dll para las funciones del socket  */
	public static sockpos getSocketFunction() {
		try {
			// && ======== Verificacion de que existan los archivos dll del
			// socketPos
			String spFolderSpPath = (Platform.is64Bit()) ? PropertiesSystem.JNA_PATHx64
					: PropertiesSystem.JNA_PATHx32;

			String spFilesPath = spFolderSpPath + "\\"
					+ PropertiesSystem.SOCKETPOS_DDLNAME + ".dll";

			if (!new File(spFilesPath).exists())
				return null;

//			LogCrtl.sendLogDebgs( "1.... "+System.getProperties().getProperty(PropertiesSystem.JNA_SOCKETPOS_LIBNAME));
			
			System.getProperties().remove(
					PropertiesSystem.JNA_SOCKETPOS_LIBNAME);
			
//			LogCrtl.sendLogDebgs("2.... "+System.getProperties().getProperty(PropertiesSystem.JNA_SOCKETPOS_LIBNAME));
			
			System.setProperty(PropertiesSystem.JNA_SOCKETPOS_LIBNAME,
					spFolderSpPath);
			
//			LogCrtl.sendLogDebgs("3.... "+System.getProperties().getProperty(PropertiesSystem.JNA_SOCKETPOS_LIBNAME));
			
			socketposfns = null;
			socketposfns = (sockpos) Native.loadLibrary(
					PropertiesSystem.SOCKETPOS_DDLNAME, sockpos.class);

		} catch (Exception e) {
			socketposfns = null;
//			LogCrtl.imprimirError(e);
			e.printStackTrace(); 
//			LogCrtl.sendLogDebgs(e);
			System.out.println("Error al cargar libreria de SocketPos");
		} finally {
		}
		return socketposfns;
	}

}
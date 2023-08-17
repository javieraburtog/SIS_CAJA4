package com.casapellas.navegacion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.casapellas.controles.CompaniaCtrl;
import com.casapellas.controles.CtrlCajas;
import com.casapellas.controles.TasaCambioCtrl;
import com.casapellas.controles.fdc.CtrlA02factcoXx;
import com.casapellas.controles.fdc.CtrlA03factcoXx;
import com.casapellas.controles.fdc.CtrlArqueoXx;
import com.casapellas.controles.fdc.CtrlArqueofactXx;
import com.casapellas.controles.fdc.CtrlArqueorecXx;
import com.casapellas.controles.fdc.CtrlCambiodetXx;
import com.casapellas.controles.fdc.CtrlControlesXx;
import com.casapellas.controles.fdc.CtrlReciboXx;
import com.casapellas.controles.fdc.CtrlRecibodetXx;
import com.casapellas.controles.fdc.CtrlRecibofacXx;
import com.casapellas.dao.FacContadoDAO;
import com.casapellas.dao.FacCreditoDAO;
import com.casapellas.entidades.Credhdr;
import com.casapellas.entidades.F55ca014;
import com.casapellas.entidades.F55ca017;
import com.casapellas.entidades.Hfactura;
import com.casapellas.entidades.MetodosPago;
import com.casapellas.entidades.Tcambio;
import com.casapellas.entidades.Tpararela;
import com.casapellas.entidades.ens.Vautoriz;
import com.casapellas.entidades.Vf55ca01;
import com.casapellas.entidades.fdc.InfAdicionalFCV;
import com.casapellas.entidades.fdc.ReciboXx;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.util.Divisas;
import com.casapellas.util.FechasUtil;

public class CtrlRutasFDC {	
	private String strMessagesFdc = "";
	private int iCaja;
	private String sCia, sSuc2, sSuc5, sdLocn;	
	Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();	
	
	public CtrlRutasFDC() throws Exception {
		F55ca017[] f55ca017 = null;
		F55ca014[] f55ca014 = null;
		List<Vf55ca01> lstCajas = null;
		Vf55ca01 f55ca01 = null;
		
		try {
			Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
			lstCajas = (List<Vf55ca01>) m.get("lstCajas");
			f55ca01 = (Vf55ca01) lstCajas.get(0);
			iCaja = f55ca01.getId().getCaid();
			sSuc2 = f55ca01.getId().getCaco().substring(3);
			sSuc5 = f55ca01.getId().getCaco();
			
			CtrlCajas cc = new CtrlCajas();
			List<Object> lstConfigCaja = cc.leerConfiguracionCaja(f55ca01);
			if(lstConfigCaja == null){
				throw new Exception ("No se han podido obtener los datos básicos de la caja #: " +  f55ca01.getId().getCaid());			
			}
			f55ca017 = (F55ca017[])lstConfigCaja.get(2);			
		
			iCaja = f55ca01.getId().getCaid(); 
			sSuc2 = f55ca017[0].getId().getC7co().substring(3);
			sSuc5 = f55ca017[0].getId().getC7co();
			sdLocn = f55ca017[0].getId().getC7locn().trim();
			
			f55ca014 = (F55ca014[]) m.get("cont_f55ca014");
			sCia = f55ca014[0].getId().getC4rp01();
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());			
		} finally {}
	}
	
	public boolean SiExistenDatosFDCXProcesar(Vf55ca01 caja, String sCaso) throws Exception {		
		boolean result = false;
		Session sesion = null;
		
		
			sesion = HibernateUtilPruebaCn.currentSession();
		

//		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
//		
//		if (m.get("SQLServer2000") != null) {
//			sesion = HibernateUtil.getSessionFactoryFDC().openSession();
//		} else if (m.get("SQLServer2008") != null) {
//			sesion = HibernateUtil.getSessionFactoryFDC2008().openSession();
//		}
		
		try {
			CtrlA02factcoXx ctrlA02factcoXx = new CtrlA02factcoXx();
			result = ctrlA02factcoXx.SiExistenDatosXProcesar(sSuc2, sdLocn, sesion);			
			if (!result){
				CtrlA03factcoXx ctrlA03factcoXx = new CtrlA03factcoXx();
				result = ctrlA03factcoXx.SiExistenDatosXProcesar(sSuc2, sdLocn, sesion);
				if (!result){
					CtrlArqueofactXx ctrlArqueoFactXx = new CtrlArqueofactXx();
					result = ctrlArqueoFactXx.SiExistenDatosXProcesar(iCaja, sesion);
					if (!result){
						CtrlArqueorecXx ctrlArqueoRecXx = new CtrlArqueorecXx();
						result = ctrlArqueoRecXx.SiExistenDatosXProcesar(iCaja, sesion);
						if (!result){
							CtrlArqueoXx ctrlArqueoXx = new CtrlArqueoXx();
							result = ctrlArqueoXx.SiExistenDatosXProcesar(iCaja, sesion);
							if (!result){
								CtrlCambiodetXx ctrlCambiodetXx = new CtrlCambiodetXx();
								result = ctrlCambiodetXx.SiExistenDatosXProcesar(iCaja, sesion);
								if (!result){
									CtrlControlesXx ctrlControlesXx = new CtrlControlesXx();
									result = ctrlControlesXx.SiExistenDatosXProcesar(sSuc2, sdLocn, sesion);
									if (!result){
										CtrlRecibodetXx ctrlReciboDetXx = new CtrlRecibodetXx();
										result = ctrlReciboDetXx.SiExistenDatosXProcesar(iCaja, sesion);
										if (!result){
											CtrlRecibofacXx ctrlReciboFacXx = new CtrlRecibofacXx();
											result = ctrlReciboFacXx.SiExistenDatosXProcesar(iCaja, sesion);
											if (!result){
												CtrlReciboXx ctrlReciboXx = new CtrlReciboXx();
												result = ctrlReciboXx.SiExistenDatosXProcesar(iCaja, sesion);
											}
										}
									}
								}
							}
						}
					}
				}
			}			
		} catch (Exception e) {
			throw new Exception(e.getMessage());			
		} finally {
			sesion.close();
		}
		return result;
	}
		
	@SuppressWarnings("unchecked")
	public void procesarInformacionDeRutas(ActionEvent ev) throws Exception {
		Connection cn = null;
		FacesContext fc = FacesContext.getCurrentInstance();
		Map m = fc.getExternalContext().getSessionMap();
		NavigationHandler nh = fc.getApplication().getNavigationHandler();
		
		//Session sesionFDC = HibernateUtil.getSessionFactoryFDC().openSession();
		
		//Session sesionMCAJA = HibernateUtil.getSessionFactoryMCAJA().openSession();
		
		Session sesionMCAJA = HibernateUtilPruebaCn.currentSession();
		
		String sErrMsgs = "";
		
		m.remove("fc_FacturasPocket");
		
		Session sesionFDC = null;
		
			sesionFDC = HibernateUtilPruebaCn.currentSession();
		

		List<ReciboXx> lstRecibosFdc = null;
		boolean bProcesarRecibosFdc = false, lCommitsFDC, lCommitsCaja, lCommitsISeries;
				
		try {	
			
			sesionFDC.beginTransaction();
			
			try {
				CtrlArqueoXx ctrlArqueoXx = new CtrlArqueoXx();
				ctrlArqueoXx.actualizar_hora_en_arqueo (sesionFDC, this.iCaja);
			} catch (Exception ex) {
				sErrMsgs += ex.getMessage();
			}
			
			sesionFDC.getTransaction().commit();

			sesionFDC.beginTransaction();
			
			try {
				CtrlA02factcoXx ctrlA02factcoXx = new CtrlA02factcoXx(); 
				ctrlA02factcoXx.actualizar_hora_en_facturas(sesionFDC, this.sCia, this.sSuc2, this.iCaja, this.sdLocn);
				
				CtrlReciboXx ctrlReciboXx = new CtrlReciboXx();
				ctrlReciboXx.actualizar_hora_en_recibos(sesionFDC, this.sCia, this.sSuc2, this.iCaja, this.sdLocn);				
			} catch (Exception ex) {
				sErrMsgs += ex.getMessage();
			}
			sesionFDC.getTransaction().commit();
			
			if (sErrMsgs.equals(new String(""))) {
				// obtener conexion del datasource
				As400Connection as400connection = new As400Connection();
				cn = as400connection.getJNDIConnection("DSMCAJA2");
				cn.setAutoCommit(false);
				
				/**************************************************************
				 * INICIO PRIMERA ETAPA: Microsoft SQL Server --> DB2 (MCAJA) *
				 **************************************************************/
				sesionFDC.beginTransaction();
				sesionMCAJA.beginTransaction();
			
				try {
					CtrlA02factcoXx ctrlA02factcoXx = new CtrlA02factcoXx();
					ctrlA02factcoXx.Procesar(sesionFDC, sesionMCAJA, this.sSuc2, this.sdLocn);
				} catch (Exception ex) {
					sErrMsgs += ex.getMessage();
				}
				
				try {
					CtrlA03factcoXx ctrlA03factcoXx = new CtrlA03factcoXx();
					ctrlA03factcoXx.Procesar(sesionFDC, sesionMCAJA, this.sSuc2, this.sdLocn);
				} catch (Exception ex) {
					sErrMsgs += ex.getMessage();
				}
				
				//fn. para verificar que por cada factura en el header existan uno o más registros en el detalle
				
				try {
					CtrlArqueofactXx ctrlArqueofactXx = new CtrlArqueofactXx();
					ctrlArqueofactXx.Procesar(sesionFDC, sesionMCAJA, this.iCaja);
				} catch (Exception ex) {
					sErrMsgs += ex.getMessage();
				}
				
				try {
					CtrlArqueorecXx ctrlArqueorecXx = new CtrlArqueorecXx();
					ctrlArqueorecXx.Procesar(sesionFDC, sesionMCAJA, this.iCaja);
				} catch (Exception ex) {
					sErrMsgs += ex.getMessage();
				}
				
				try {
					CtrlArqueoXx ctrlArqueoXx = new CtrlArqueoXx();
					ctrlArqueoXx.Procesar(sesionFDC, sesionMCAJA, this.iCaja);
				} catch (Exception ex) {
					sErrMsgs += ex.getMessage();
				}
				
				CtrlCambiodetXx ctrlCambiodetXx = new CtrlCambiodetXx();
				try {
					ctrlCambiodetXx.Procesar(sesionFDC, sesionMCAJA, this.iCaja);
				} catch (Exception ex) {
					sErrMsgs += ex.getMessage();
				}
				
				try {
					CtrlRecibodetXx ctrlRecibodetXx = new CtrlRecibodetXx();
					ctrlRecibodetXx.Procesar(sesionFDC, sesionMCAJA, this.iCaja);
				} catch (Exception ex) {
					sErrMsgs += ex.getMessage();
				}
				try {
					CtrlRecibofacXx ctrlRecibofacXx = new CtrlRecibofacXx();
					ctrlRecibofacXx.Procesar(sesionFDC, sesionMCAJA, this.iCaja);
				} catch (Exception ex) {
					sErrMsgs += ex.getMessage();
				}
				
				try {
					CtrlReciboXx ctrlReciboXx = new CtrlReciboXx();
					ctrlReciboXx.Procesar(sesionFDC, sesionMCAJA, this.iCaja);
				} catch (Exception ex) {
					sErrMsgs += ex.getMessage();
				}
				
				try {				
					ctrlCambiodetXx.AddRegsForJoin(sesionFDC, sesionMCAJA, this.iCaja);
				} catch (Exception ex) {
					sErrMsgs += ex.getMessage();
				}
	
				String sUser = (String) m.get("sNombreEmpleado");
				try {
					CtrlControlesXx ctrlControlesXx = new CtrlControlesXx();
					ctrlControlesXx.Procesar(cn, sesionFDC, sesionMCAJA, sUser, this.sCia, this.sSuc5, this.sdLocn, this.iCaja);
				} catch (Exception ex) {
					sErrMsgs += ex.getMessage();
				}
				
				/***********************************************************
				 * FIN PRIMERA ETAPA: Microsoft SQL Server --> DB2 (MCAJA) *
				 ***********************************************************/
				
				lstRecibosFdc = (List<ReciboXx>) m.get("lstRecibosFdc");
				if (sErrMsgs.equals(new String(""))) {
					if (lstRecibosFdc != null) {
						bProcesarRecibosFdc = ProcesarRecibosFdc(cn, sesionMCAJA, sesionFDC, lstRecibosFdc);
					} else {
						bProcesarRecibosFdc = true;
					}
				}
	
				if (sErrMsgs.equals(new String("")) && bProcesarRecibosFdc) {
					lCommitsFDC = false; lCommitsCaja = false; lCommitsISeries = false;
					try {
						sesionMCAJA.getTransaction().commit();
						lCommitsCaja = true;
					} catch (Exception ex) {
						sErrMsgs = "MCAJA: " + ex.getMessage();
					}
					
					if (lCommitsCaja) {
						try {
							cn.commit();
							lCommitsISeries = true;
						} catch (Exception ex) {
							sErrMsgs += "\n iSeries: " + ex.getMessage();
						}
					}
					
					if (lCommitsCaja && lCommitsISeries) {
						try {
							sesionFDC.getTransaction().commit();
							lCommitsFDC = true;
						} catch (Exception ex) {
							sErrMsgs += "\n FDC: " + ex.getMessage();
						}
					}
				} else {
					throw new Exception(sErrMsgs);
				}
			} else {
				throw new Exception(sErrMsgs);
			}
			if (lCommitsCaja && lCommitsISeries && lCommitsFDC) {			
				nh.handleNavigation(fc, "procesarInformacionDeRutas", "home");
			} else {
				throw new Exception(sErrMsgs);
			}
		} catch (Exception ex) {
			
//			LogCrtl.imprimirError(ex);
			ex.printStackTrace();
			
			try{
				if(sesionFDC != null && sesionFDC.isOpen()) 
					sesionFDC.getTransaction().rollback();
				
				if(sesionMCAJA != null && sesionMCAJA.isOpen())
					sesionMCAJA.getTransaction().rollback();
				
				if(cn != null && !cn.isClosed())
					cn.rollback();
				
			}catch(Exception error){
//				LogCrtl.imprimirError(error);
				error.printStackTrace();
			}
			
			m.put("sInfoFdc", ex.getMessage());
			nh.handleNavigation(fc, "procesarInformacionDeRutas", null);

		} finally {
			try{
				
				if(sesionFDC != null && sesionFDC.isOpen()){
					sesionFDC.close();
				} 
				
				if(sesionMCAJA != null && sesionMCAJA.isOpen()){
					//sesionMCAJA.close();
					HibernateUtilPruebaCn.closeSession(sesionMCAJA); 
				}
				
				if(cn != null && !cn.isClosed()){
					cn.close();
				}
				
			}catch(Exception ex){
//				LogCrtl.imprimirError(ex);
				ex.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	private boolean ProcesarRecibosFdc(Connection cn, Session sesionMCAJA, Session sesionFDC, List<ReciboXx> lstRecibosFdc) throws Exception {
		boolean lExito = false;
		List<Hfactura> lstFacturasSelectedCO = new ArrayList<Hfactura>();
		List<Credhdr> lstFacturasSelectedCR = new ArrayList<Credhdr>();
		List <MetodosPago> selectedMet = null;

		List<Object> lstInformacionDeCambio = new ArrayList<Object>();
		TasaCambioCtrl tcCtrl = new TasaCambioCtrl();
		Tcambio[] tcambio = null;
		tcambio = tcCtrl.obtenerTasaCambioJDEdelDia();
		m.put("tcambio", tcambio);
		
		Tpararela[] tpcambio = null;
		tpcambio = tcCtrl.obtenerTasaCambioParalela();
		m.put("tpcambio", tpcambio);
		
		m.remove("facturasSelected");
		m.remove("lstPagos");
		m.remove("lstInformacionDeCambio");

		for(ReciboXx reg : lstRecibosFdc) {
			m.put("iNumRec", reg.getId().getNumRec());
			
			if (reg.getId().getTipoRec().equals("CO") || reg.getId().getTipoRec().equals("DCO")) {								
				
				CtrlRecibofacXx ctrlRecibofacXx = new CtrlRecibofacXx();				
				lstFacturasSelectedCO = ctrlRecibofacXx.getFacturasDeReciboCO(sesionFDC, reg, this.sSuc2, this.sdLocn);
				m.put("facturasSelected", lstFacturasSelectedCO);
				
				CtrlCambiodetXx ctrlCambiodetXx = new CtrlCambiodetXx();
				lstInformacionDeCambio = ctrlCambiodetXx.obtenerInformacionDeCambio(sesionFDC, reg.getId().getCodComp(), reg.getId().getCodSuc(), 
										reg.getId().getCaId(), reg.getId().getNumRec(), reg.getId().getTipoRec(), 
						                 lstFacturasSelectedCO.get(0).getMoneda());
				
				m.put("lstInformacionDeCambio", lstInformacionDeCambio);
				
				CtrlRecibodetXx ctrlRecibodetXx = new CtrlRecibodetXx();
				selectedMet = ctrlRecibodetXx.getMetodosDePagoDeRecibo(sesionFDC, reg);
				m.put("lstPagos", selectedMet);
				
				lExito = procesa_pago_contado_fdc(cn, sesionMCAJA, reg.getId().getTipoRec(), reg.getFecha());
				
				
			} else if (reg.getId().getTipoRec().equals("CR")) {
				
				
				CtrlRecibofacXx ctrlRecibofacXx = new CtrlRecibofacXx();				
				lstFacturasSelectedCR = ctrlRecibofacXx.getFacturasDeReciboCR(sesionFDC, reg, this.sSuc2, this.sdLocn);
				m.put("facturasSelected", lstFacturasSelectedCR);
				
				CtrlCambiodetXx ctrlCambiodetXx = new CtrlCambiodetXx();
				lstInformacionDeCambio = ctrlCambiodetXx.obtenerInformacionDeCambio(sesionFDC, reg.getId().getCodComp(), reg.getId().getCodSuc(), 
						                 reg.getId().getCaId(), reg.getId().getNumRec(), reg.getId().getTipoRec(), 
						                 lstFacturasSelectedCR.get(0).getMoneda());
				m.put("lstInformacionDeCambio", lstInformacionDeCambio);
				
				CtrlRecibodetXx ctrlRecibodetXx = new CtrlRecibodetXx();
				selectedMet = ctrlRecibodetXx.getMetodosDePagoDeRecibo(sesionFDC, reg);
				m.put("lstPagos", selectedMet);
				
				lExito = procesa_pago_credito_fdc(cn, sesionMCAJA, reg.getId().getTipoRec(), reg.getFecha());
			}
			
			//Aquí en este for proceso los tipos CO, DCO y CR, los tipos FCV los proceso a continuación de este for.
			if (reg.getId().getTipoRec().equals("CO") || reg.getId().getTipoRec().equals("DCO") || reg.getId().getTipoRec().equals("CR")) {
				m.remove("facturasSelected");
				m.remove("lstPagos");
				m.remove("lstInformacionDeCambio");
				if (!lExito) {
					break;
				}
			}
		}

		if (lExito) {
			Object obj = m.get("lstFichasCV");
			if (obj != null) {
				List<Object> lstFichasCV = (List<Object>) obj;			
				CtrlReciboXx ctrlRecibo = new CtrlReciboXx();
				lstFichasCV = ctrlRecibo.AgregarObjetosFicha(sesionFDC, lstFichasCV);
				
				CtrlRecibodetXx ctrlRecibodetXx = new CtrlRecibodetXx();
				lstFichasCV = ctrlRecibodetXx.CompletarObjetosRecibo(sesionFDC, lstFichasCV);
				
				lExito = procesa_fichasCV_fdc(cn, sesionMCAJA, lstFichasCV); 
			}
		}
		return lExito;
	}
	
	
	
	private boolean procesa_fichasCV_fdc(Connection cn, Session sesionMCAJA, List<Object> lstFichasCV) throws Exception {
		String[] sCuentaCaja = null;
		String sMonedaBase = "";
		Divisas d = new Divisas();
		boolean bContabilizado = false;
		Hfactura hFac = null;
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		Vautoriz[] vaut = null;
		List lstPagoFicha = null;
		
		Object[][] objFCV = new Object[1][2];
		InfAdicionalFCV infAdicionalFCV = null;
		ReciboXx registroFicha = null;
		
		Transaction tx = null;
		tx = sesionMCAJA.getTransaction();
		if (tx == null || !tx.isActive()) {
			tx = sesionMCAJA.beginTransaction();
		}
		
		Vf55ca01 f55ca01 = (Vf55ca01) ((List) m.get("lstCajas")).get(0);
		vaut = (Vautoriz[]) m.get("sevAut");
		
		for (Object obj : lstFichasCV) {			
			objFCV = (Object[][]) obj;
			infAdicionalFCV = (InfAdicionalFCV) objFCV[0][0];
			registroFicha = (ReciboXx) objFCV[0][1];			
			
			hFac = new Hfactura();
			hFac.setCodcomp(infAdicionalFCV.getCodComp());
			hFac.setCodsuc(infAdicionalFCV.getCodSuc().substring(3));
			
			java.util.Date dFechaDelRecibo = FechasUtil.getDateFromString(registroFicha.getFecha(), "yyyy-MM-dd");

			sMonedaBase = CompaniaCtrl.sacarMonedaBase((F55ca014[])m.get("cont_f55ca014"), hFac.getCodcomp());

			String[] sCuentaCajaDom = d.obtenerCuentaCaja(f55ca01.getId().getCaid(), hFac.getCodcomp(), "5", sMonedaBase, sesionMCAJA, tx, null, null);
			sCuentaCaja = d.obtenerCuentaCaja(f55ca01.getId().getCaid(),hFac.getCodcomp(),"5","USD", sesionMCAJA, tx,null,null);
			int iNoficha = infAdicionalFCV.getNumFicha(); 
			
			MetodosPago metPagoFicha = new MetodosPago("5", infAdicionalFCV.getMoneda(), registroFicha.getMontoRec().doubleValue(), infAdicionalFCV.getTasaDeCambio(), 
                   					   registroFicha.getMontoApl().doubleValue(), "", "", "", "", "0", f55ca01.getId().getCaid());

			
			lstPagoFicha = new ArrayList();
			lstPagoFicha.add(metPagoFicha);
			
			
			FacContadoDAO facContadoDAO = new FacContadoDAO();			
			//bContabilizado = facContadoDAO.generarAsientosFichaCV(sesionMCAJA, tx, cn, lstPagoFicha, f55ca01, hFac, "", vaut[0], sCuentaCaja, sCuentaCajaDom, iNoficha, 0, dFechaDelRecibo);
			bContabilizado = facContadoDAO.generarAsientosFichaCV(sesionMCAJA, tx, lstPagoFicha, f55ca01, hFac, "", vaut[0], sCuentaCaja, sCuentaCajaDom, iNoficha, 0, dFechaDelRecibo);
			
			//bContabilizado = generarAsientosFichaCV(sesionMCAJA, tx, cn, lstPagoFicha, f55ca01, hFac, "", vaut[0], sCuentaCaja, sCuentaCajaDom, iNoficha, iNumrec);
			if (!bContabilizado) {
				break;
			}
		}
		return bContabilizado;
	}

	public boolean procesa_pago_contado_fdc(Connection cn, Session sesionMCAJA, String sTipoRec, String sFechaDelRecibo) throws Exception {
		Divisas divisas = new Divisas();
		MetodosPago[] metPago = null;
		boolean insertado = false;		
		int iNumrec = 0;
		
		//Se usa para registrar los asientos con la fecha del recibo.
		FechasUtil fechasUtil = new FechasUtil();
		java.util.Date dFechaDelRecibo = FechasUtil.getDateFromString(sFechaDelRecibo, "yyyy-MM-dd"); 			
		
		String sMonedaBase = "";
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		double dNewMonto = 0.0, dTotalAplicar = 0.0;;
		FacContadoDAO facContadoDAO = new FacContadoDAO();
		try {
			Transaction tx = sesionMCAJA.getTransaction();

			Vf55ca01 f55ca01 = (Vf55ca01) ((List) m.get("lstCajas")).get(0);

			List lstFacturasSelected = (List) m.get("facturasSelected");
			Hfactura hFac = (Hfactura) lstFacturasSelected.get(0);
			
			List<Object> lstInformacionDeCambio = (List<Object>) m.get("lstInformacionDeCambio");				
			
			List lstMetodosPago = (List) m.get("lstPagos");
			
			sMonedaBase = cCtrl.sacarMonedaBase((F55ca014[])m.get("cont_f55ca014"), hFac.getCodcomp());
						
			iNumrec = Integer.valueOf(m.get("iNumRec").toString());

			//String sTipoDoc = hFac.getTipofactura();
			int iCajaId = Integer.parseInt(m.get("sCajaId").toString());
			String sCodComp = hFac.getCodcomp();
								
			for (int i = 0; i < lstFacturasSelected.size(); i++) {
				Hfactura hFac1 = (Hfactura) lstFacturasSelected.get(i);
				dTotalAplicar += hFac1.getTotal();
			}
				
			if (lstMetodosPago != null && lstMetodosPago.size() > 0) {
				metPago = new MetodosPago[lstMetodosPago.size()];
			} else {
				throw new Exception ("No Existen Métodos de Pago para el Recibo: " + iNumrec);
			}
				
			if (lstInformacionDeCambio != null) { //Se dieron cambios					
				//String[] sMonedasDeCambio = (String[]) lstInformacionDeCambio.get(0);
				BigDecimal[] dValorCambio = (BigDecimal[]) lstInformacionDeCambio.get(1);
					
				//Las posibilidades para FDC son: Pago-5-Efectivo-COR     Cambio-COR
				//                                Pago-5-Efectivo-USD     Cambio-COR
				//
				//Moneda de las facturas: COR
				//     Moneda de los cxc: COR
				//
				//Moneda Base: COR	
				boolean lFlag = true;
				for (int i = 0; i < lstMetodosPago.size(); i++) {
					metPago[i] = (MetodosPago) lstMetodosPago.get(i);
					if (metPago[i].getMetodo().equals("5")) {
						if (metPago[i].getMoneda().equals(sMonedaBase)) { //Pago en Córdobas
							if (lFlag) {
								if (metPago[i].getMonto() - dValorCambio[0].doubleValue() <= 0) {
									dValorCambio[0] = new BigDecimal(dValorCambio[0].doubleValue() - metPago[i].getMonto());
									metPago[i].setMonto(0);
									metPago[i].setEquivalente(0);									
								} else {									
									dNewMonto = metPago[i].getMonto() - dValorCambio[0].doubleValue();
									metPago[i].setMonto(dNewMonto);
									metPago[i].setEquivalente(dNewMonto);
									lFlag = false;
								}
							}
						} else if (lFlag) { //Pago en Dólares
							if (metPago[i].getEquivalente() - dValorCambio[0].doubleValue() <= 0){
								dValorCambio[0] = new BigDecimal(dValorCambio[0].doubleValue() - metPago[i].getEquivalente());
								metPago[i].setMonto(0);
								metPago[i].setEquivalente(0);
							} else {
								dNewMonto = metPago[i].getEquivalente() - dValorCambio[0].doubleValue();
								metPago[i].setMonto(divisas.roundDouble(dNewMonto/metPago[i].getTasa().doubleValue()));
								metPago[i].setEquivalente(dNewMonto);
								lFlag = false;
							}
						}
					}
				}									
			}
		
			if (sTipoRec.equals("CO")) {
				
				insertado = facContadoDAO.generarAsientos(sesionMCAJA, tx, iCajaId, sCodComp, dTotalAplicar, lstFacturasSelected, 
                         lstMetodosPago, iNumrec, false, null, sMonedaBase, dFechaDelRecibo);
				
				
			} else if (sTipoRec.equals("DCO")) {
				
				insertado = facContadoDAO.generarAsientosDevolucion(sesionMCAJA, tx, iCajaId, sCodComp, dTotalAplicar,
						lstFacturasSelected, lstMetodosPago, hFac, iNumrec, sMonedaBase, dFechaDelRecibo);
				
			}
			if(!insertado){
				throw new Exception (facContadoDAO.getStrMensajeValidacion());
			}
			
		} catch (Exception ex) {		
			throw new Exception ("Problemas Generando Asientos de Factura. " + ex.getMessage() + ". " + facContadoDAO.getStrMensajeValidacion());
		}finally{}
		return insertado;
	}
	
	public boolean procesa_pago_credito_fdc(Connection cn, Session sesionMCAJA, String sTipoRec, String sFechaDelRecibo) throws Exception {		
		boolean insertado = false;
		Divisas divisas = new Divisas();
		MetodosPago[] metPago = null;
		List<Credhdr> lstFacturasSelected = null;
		Credhdr hFac = null;
		BigDecimal bdTasaJde, bdTasaPar;
		double dNewMonto = 0.0;

		//Se usa para registrar los asientos con la fecha del recibo.
		FechasUtil fechasUtil = new FechasUtil();
		java.util.Date dFechaDelRecibo = fechasUtil.getDateFromString(sFechaDelRecibo, "yyyy-MM-dd");
		
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		String sMonedaBase = "";
		FacCreditoDAO facCreditoDAO = new FacCreditoDAO();
		try {			
		
			bdTasaJde = facCreditoDAO.obtenerTasaOficial();
			bdTasaPar = facCreditoDAO.obtenerTasaParalela("COR");

			Vf55ca01 f55ca01 = (Vf55ca01)((List)m.get("lstCajas")).get(0);

	    	lstFacturasSelected = (List) m.get("facturasSelected");
			hFac = (Credhdr)lstFacturasSelected.get(0);
			Vautoriz[] vautoriz = (Vautoriz[])m.get("sevAut");
			
			//obtener companias x caja
			sMonedaBase = cCtrl.sacarMonedaBase((F55ca014[])m.get("cont_f55ca014"), hFac.getId().getCodcomp());
			
			Transaction tx = sesionMCAJA.getTransaction();
							
			int iNumRec = Integer.valueOf(m.get("iNumRec").toString());
			List<MetodosPago> lstMetodosPago = (List<MetodosPago>) m.get("lstPagos");
			
			List<Object> lstInformacionDeCambio = (List<Object>) m.get("lstInformacionDeCambio");
			metPago = new MetodosPago[lstMetodosPago.size()];
			
			if (lstInformacionDeCambio != null) { //Se dieron cambios					
				//String[] sMonedasDeCambio = (String[]) lstInformacionDeCambio.get(0);
				BigDecimal[] dValorCambio = (BigDecimal[]) lstInformacionDeCambio.get(1);
					
				//Las posibilidades para FDC son: Pago-5-Efectivo-COR     Cambio-COR
				//                                Pago-5-Efectivo-USD     Cambio-COR
				//
				//Moneda de las facturas: COR
				//     Moneda de los cxc: COR
				//
				//Moneda Base: COR	
				boolean lFlag = true;
				for (int i = 0; i < lstMetodosPago.size(); i++) {
					metPago[i] = (MetodosPago) lstMetodosPago.get(i);
					if (metPago[i].getMetodo().equals("5")) {
						if (metPago[i].getMoneda().equals(sMonedaBase)) { //Pago en Córdobas
							if (lFlag) {
								if (metPago[i].getMonto() - dValorCambio[0].doubleValue() <= 0) {
									dValorCambio[0] = new BigDecimal(dValorCambio[0].doubleValue() - metPago[i].getMonto());
									metPago[i].setMonto(0);
									metPago[i].setEquivalente(0);									
								} else {									
									dNewMonto = metPago[i].getMonto() - dValorCambio[0].doubleValue();
									metPago[i].setMonto(dNewMonto);
									metPago[i].setEquivalente(dNewMonto);
									lFlag = false;
								}
							}
						} else if (lFlag) { //Pago en Dólares
							if (metPago[i].getEquivalente() - dValorCambio[0].doubleValue() <= 0){
								dValorCambio[0] = new BigDecimal(dValorCambio[0].doubleValue() - metPago[i].getEquivalente());
								metPago[i].setMonto(0);
								metPago[i].setEquivalente(0);
							} else {
								dNewMonto = metPago[i].getEquivalente() - dValorCambio[0].doubleValue();
								metPago[i].setMonto(divisas.roundDouble(dNewMonto/metPago[i].getTasa().doubleValue()));
								metPago[i].setEquivalente(dNewMonto);
								lFlag = false;
							}
						}
					}
				}									
			}
			
			//registrar recibo en F0311
			
			//insertado = facCreditoDAO.grabarCredito_fdc(sesionMCAJA, tx, cn, lstMetodosPago, f55ca01, lstFacturasSelected, iNumRec, null, bdTasaJde, bdTasaPar, vautoriz[0], sMonedaBase, dFechaDelRecibo);
			
			//&& ==========================================================================&&//
			//&& cambios para version de E1
			
			for (Credhdr factura : lstFacturasSelected) {
				boolean pagoparcial = 
				 (  (factura.getMoneda().compareTo(sMonedaBase) == 0 && 
					  factura.getMontoAplicar().compareTo(factura.getId().getCpendiente()) != 0) || 
					(factura.getMoneda().compareTo(sMonedaBase) != 0 && 
					 factura.getMontoAplicar().compareTo(factura.getId().getDpendiente()) != 0)
				) ;
				factura.setPagoparcial(pagoparcial);
			}
			
			insertado = facCreditoDAO.grabarReciboCredito_FDC(sesionMCAJA, tx, lstMetodosPago, 
					f55ca01, lstFacturasSelected, iNumRec, null, bdTasaJde, bdTasaPar, 
					vautoriz[0], sMonedaBase, dFechaDelRecibo);
			
			if(!insertado){
				throw new Exception (facCreditoDAO.getStrMensajeValidacion());
			}
			//&& ==========================================================================&&//
			
			
		} catch (Exception ex) {
			throw new Exception ("Problemas Generando Asientos de Factura," +facCreditoDAO.getStrMensajeValidacion());
		}finally{}
		return insertado;
	}
	
	public String getStrMessagesFdc() {
		Object obj = null;
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		obj =  m.get("sInfoFdc");
		if (obj != null) {
			strMessagesFdc = (String)obj;
		}
		return strMessagesFdc;
	}

	public void setStrMessagesFdc(String strMessagesFdc) {
		this.strMessagesFdc = strMessagesFdc;
	}
}
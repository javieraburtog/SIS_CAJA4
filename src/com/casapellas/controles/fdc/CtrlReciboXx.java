package com.casapellas.controles.fdc;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.hibernate.Session;

import com.casapellas.controles.CompaniaCtrl;
import com.casapellas.controles.MonedaCtrl;
import com.casapellas.entidades.Recibo;
import com.casapellas.entidades.ReciboId;
import com.casapellas.entidades.fdc.A02factcoXx;
import com.casapellas.entidades.fdc.InfAdicionalFCV;
import com.casapellas.entidades.fdc.ReciboXx;
import com.casapellas.entidades.fdc.RecibofacXx;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.JulianToCalendar;

public class CtrlReciboXx {
	
	public boolean SiExistenDatosXProcesar(int iCaja, Session sesion) throws Exception{
		boolean existen = false;
		String sql = null;
		Object obj = null;
		Long nRegs = null;
		
		//Session sesion = HibernateUtil.getSessionFactoryFDC().openSession();
						
		try {
			sesion.beginTransaction();
			
			sql = "select count(*) from ReciboXx a where a.id.caId = :pCaja and a.flwSts = '0'";					
			obj = sesion.createQuery(sql)
				  .setParameter("pCaja", iCaja)
				  .uniqueResult();			
			if (obj != null) {
				nRegs = (Long)obj;
			}
			
			sesion.getTransaction().commit();
			
			if (nRegs > 0){
				existen = true;
			}
		} catch(Exception ex){
			throw new Exception(ex.getMessage());
		} finally {
			//sesion.close();
		}
		return existen;
	}
	
	public void Procesar(Session sesionFDC, Session sesionMCAJA, int iCaja) throws Exception{		
		String sql = null, sTmp;		
		List<ReciboXx> lstRegs = null;
		List<ReciboXx> lstRecibosFdc = null;
		ReciboXx regToUpdate = null;
		
		Object[][] objFCV = null; // = new Object[1][2];
		List<Object> lstFichasCV = null;
		boolean lFirstTimeFCV = true;
		
		try {
			Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();

			sql = "From ReciboXx as a where a.id.caId = :pCaja and a.flwSts = '0'";
			lstRegs = (ArrayList<ReciboXx>)sesionFDC.createQuery(sql)
			.setParameter("pCaja", iCaja)
			.list();
			if(lstRegs != null && lstRegs.size() > 0) {
				lstRecibosFdc = lstRegs;
				m.put("lstRecibosFdc", lstRecibosFdc);
				for(ReciboXx reg : lstRegs) {
					if (reg.getRecJde() > 0 && !reg.getId().getTipoRec().equals("FCV")){
						InfAdicionalFCV infAdicionalFCV = new InfAdicionalFCV();
						infAdicionalFCV.setNumRec(reg.getId().getNumRec());
						infAdicionalFCV.setCodComp(reg.getId().getCodComp());
						
//						infAdicionalFCV.setCaId(reg.getId().getCaid());
						infAdicionalFCV.setCaId(reg.getId().getCaId());
						
						infAdicionalFCV.setCodSuc(reg.getId().getCodSuc());
						infAdicionalFCV.setTipoRec(reg.getId().getTipoRec());
						infAdicionalFCV.setNumFicha(reg.getRecJde());
						objFCV = new Object[1][2];
						objFCV[0][0] = infAdicionalFCV;
						if (lFirstTimeFCV) {
							lstFichasCV = new ArrayList<Object>();							
							lFirstTimeFCV = false;
						}
						lstFichasCV.add(objFCV);
					}
					Recibo recibo = new Recibo();
					ReciboId id = new ReciboId();

//					id.setCaid(reg.getId().getCaid());
					
					id.setCaid(reg.getId().getCaId());
					
					id.setCodcomp(reg.getId().getCodComp());
					id.setCodsuc(reg.getId().getCodSuc());
					id.setNumrec(reg.getId().getNumRec());
					id.setTiporec(reg.getId().getTipoRec());					

//					recibo.setCaid(reg.getId().getCaid());
					recibo.setCaid(reg.getId().getCaId());
					
					
					recibo.setCajero(reg.getCajero());
					recibo.setCliente(reg.getCliente());
					recibo.setCodcli(reg.getCodCli());
					recibo.setCodcomp(reg.getId().getCodComp());
					recibo.setCodsuc(reg.getId().getCodSuc());
					recibo.setCodunineg(reg.getCodUniNeg());
					recibo.setCoduser(reg.getCodUser());
					
					sTmp = reg.getCodUserA().trim();					
					if (sTmp.equals("-")) {
						sTmp = "";
					}
					recibo.setCodusera(sTmp);

					recibo.setConcepto(reg.getConcepto());
					
//					sTmp = Character.toString(reg.getEstado());
					sTmp =  reg.getEstado();
					if (sTmp.equals("-")) {
						sTmp = "";
					}					
					recibo.setEstado(sTmp);
					
					FechasUtil fechasutil = new FechasUtil();
					java.util.Date date = fechasutil.getDateFromString(reg.getFecha(), "yyyy-MM-dd");					
					recibo.setFecha(date);
					
					date =  fechasutil.getDateFromString(reg.getFechaM(), "yyyy-MM-dd");
					recibo.setFecham(date);
					
					date =  fechasutil.getDateFromString(reg.getHora(), "hh:mm:ss");
					recibo.setHora(date);
					
					date =  fechasutil.getDateFromString(reg.getHoraMod(), "hh:mm:ss");
					recibo.setHoramod(date);
					
					recibo.setId(id);
					//recibo.setMetodosPago(metodosPago) --> No existe en la tabla
					recibo.setMontoapl(reg.getMontoApl());
					recibo.setMontorec(reg.getMontoRec());
					
					sTmp = reg.getMotivo().trim();
					if (sTmp.equals("-")) {
						sTmp = "";
					}					
					recibo.setMotivo(sTmp);

					sTmp = reg.getMotivoOct().trim();
					if (sTmp.equals("-")) {
						sTmp = "";
					}
					recibo.setMotivoct(sTmp);
										
					//recibo.setNobatch() --> No existe en la tabla
					recibo.setNodoco(reg.getNoDoco());
					recibo.setNumrec(reg.getId().getNumRec());
					recibo.setNumrecm(reg.getNumRecM());
					recibo.setRecjde(reg.getRecJde());
					
					sTmp = reg.getTipoDoco().trim();
					if (sTmp.equals("-")) {
						sTmp = "";
					}
					recibo.setTipodoco(sTmp);

					recibo.setTiporec(reg.getId().getTipoRec());
					
					//&& ==== Campo requerido por CRM. 2013-02-23 
					//&& ==== moneda aplicada al recibo. Leer moneda de unidad de negocio.
					String monedaapl = MonedaCtrl.leerMonedaxUnegocio(CompaniaCtrl
											.leerLineaNegocio(reg.getCodUniNeg()));
 					if(monedaapl.compareTo("") == 0)
 						monedaapl = new CompaniaCtrl().obtenerMonedaBasexComp(
 								reg.getId().getCodComp());
					
 					if(monedaapl.compareTo("") == 0) monedaapl = "COR";
					recibo.setMonedaapl(monedaapl);
					//&& ==============
					
					sesionMCAJA.save(recibo);	
					
					sql = "From ReciboXx as a where a.id.numRec = :pNumRec and a.id.codComp = :pCodComp and a.id.caId = :pCaId and " +
				          "a.id.codSuc = :pCodSuc and a.id.tipoRec = :pTipoRec and a.flwSts = '0'";

				    regToUpdate = (ReciboXx) sesionFDC.createQuery(sql)
					.setParameter("pNumRec", reg.getId().getNumRec())
					.setParameter("pCodComp", reg.getId().getCodComp())
					.setParameter("pCaId", iCaja)
					.setParameter("pCodSuc", reg.getId().getCodSuc())
					.setParameter("pTipoRec", reg.getId().getTipoRec())
					.uniqueResult();
					
					if(regToUpdate != null) {
//						regToUpdate.setFlwSts('1');
						regToUpdate.setFlwSts("1");
						sesionFDC.update(regToUpdate);
					} else {
						throw new Exception ("Compañía: " + reg.getId().getCodComp()
								         + ", Sucursal: " + reg.getId().getCodSuc()
										     + ", Caja: " + iCaja
									   + ", No. Recibo: " + reg.getId().getNumRec()
									  + ", Tipo Recibo: " + reg.getId().getTipoRec());
					}
				}
				if (lstFichasCV != null) {					
					m.put("lstFichasCV", lstFichasCV);
				}
			} else {
				m.remove("lstRecibosFdc");
			}						
		} catch(Exception ex){
			throw new Exception("Error al procesar recibos. " + ex.getMessage());
		} finally {
		}
	}
	
	public List<Object> AgregarObjetosFicha(Session sesionFDC, List<Object> lstFichasCV) throws Exception{		
		String sql = null;
		int i;
		Object[][] objFCV = new Object[1][2];
		ReciboXx registroFicha = null;
		InfAdicionalFCV infAdicionalFCV = null;
							
		try {
			
			for (i = 0; i < lstFichasCV.size(); i++) {
				objFCV = (Object[][]) lstFichasCV.get(i);
				//			}
				//			for(Object obj : lstFichasCV) {
				//				objFCV = (Object[][]) obj;
				infAdicionalFCV = (InfAdicionalFCV) objFCV[0][0];
			
				sql = "From ReciboXx as a where a.id.numRec = :pNumRec and a.id.codComp = :pCodComp and a.id.caId = :pCaId and " +
	                  "a.id.codSuc = :pCodSuc and a.id.tipoRec = :pTipoRec";
			
				registroFicha = (ReciboXx) sesionFDC.createQuery(sql)
				.setParameter("pNumRec", infAdicionalFCV.getNumFicha())
				.setParameter("pCodComp", infAdicionalFCV.getCodComp())
				.setParameter("pCaId", infAdicionalFCV.getCaId())
				.setParameter("pCodSuc", infAdicionalFCV.getCodSuc())
				.setParameter("pTipoRec", "FCV")
				.uniqueResult();

				if (registroFicha != null) {
					objFCV[0][1] = registroFicha;
					lstFichasCV.set(i, objFCV);
					//					lstFichasCV.remove(i);
					//					lstFichasCV.add(i, objFCV);
				} else {
					throw new Exception ("Compañía: " + infAdicionalFCV.getCodComp()
						             + ", Sucursal: " + infAdicionalFCV.getCodSuc()
								         + ", Caja: " + infAdicionalFCV.getCaId()
							        + ", No. Ficha: " + infAdicionalFCV.getNumFicha()
							      + ", Tipo Recibo: FCV");
				}
				//				++i;			
			}						
		} catch(Exception ex){
			throw new Exception("Error al obtener registro de ficha. " + ex.getMessage());
		} finally {}
		return lstFichasCV;
	}

	public void actualizar_hora_en_recibos(Session sesionFDC, String sCia, String sSucursal, int iCaja, String sdLocn) throws Exception {
		String sql = null;		
		List<ReciboXx> lstRegs = null;
		ReciboXx regToUpdate = null;		
		int iArqueo;
						
		try {
			sql = "From ReciboXx as a where a.id.caId = :pCaja";// and a.flwSts = '0'";
			lstRegs = (ArrayList<ReciboXx>)sesionFDC.createQuery(sql)
			.setParameter("pCaja", iCaja)
			.list();
			if(lstRegs != null && lstRegs.size() > 0) {
				for(ReciboXx reg : lstRegs) {
					CtrlArqueorecXx ctrlarqueorec = new CtrlArqueorecXx();
					iArqueo = ctrlarqueorec.get_numero_de_arqueo(sesionFDC, sCia, "000" + sSucursal, iCaja, reg.getId().getNumRec(), 
							                                     reg.getId().getTipoRec(), "R");//reg.getTipoDoco());
					if (iArqueo != 0) {
						String sFecha = reg.getFecha();
						int iYear, iMonth, iDay;
 				        iYear = Integer.parseInt(sFecha.substring(0, 4));
					    iMonth = Integer.parseInt(sFecha.substring(5, 7));
					    iDay = Integer.parseInt(sFecha.substring(8, 10));
						
						CtrlArqueoXx ctrlarqueo = new CtrlArqueoXx();
					    String sHora = ctrlarqueo.get_hora_de_arqueo(sesionFDC, sCia, "000" + sSucursal, iArqueo, iCaja, reg.getFecha());
					    
					    int iHour, iMin, iSeg;
					    iHour = Integer.parseInt(sHora.substring(0, 2));
					    iMin = Integer.parseInt(sHora.substring(3, 5));
					    iSeg = Integer.parseInt(sHora.substring(6, 8));
					    
					    Calendar calendar = new GregorianCalendar(iYear, iMonth, iDay, iHour, iMin, iSeg);
						calendar.add(calendar.MINUTE, -5);
						
						FechasUtil fechasutil = new FechasUtil();
						sHora = fechasutil.formatDatetoString(calendar.getTime(), "HH:mm:ss");
					    
						sql = "From ReciboXx as a where a.id.numRec = :pNumRec and a.id.codComp = :pCodComp and a.id.caId = :pCaId and " +
  			                  "a.id.codSuc = :pCodSuc and a.id.tipoRec = :pTipoRec";// and a.flwSts = '0'";
					    regToUpdate = (ReciboXx) sesionFDC.createQuery(sql)
						.setParameter("pNumRec", reg.getId().getNumRec())
						.setParameter("pCodComp", reg.getId().getCodComp())
						.setParameter("pCaId", iCaja)
						.setParameter("pCodSuc", reg.getId().getCodSuc())
						.setParameter("pTipoRec", reg.getId().getTipoRec())
						.uniqueResult();

						if(regToUpdate != null) {
							regToUpdate.setHora(sHora);
							regToUpdate.setHoraMod(sHora);
							sesionFDC.update(regToUpdate);
						} else {
							throw new Exception ("Compañía: " + reg.getId().getCodComp()
									         + ", Sucursal: " + reg.getId().getCodSuc()
											     + ", Caja: " + iCaja
										   + ", No. Recibo: " + reg.getId().getNumRec()
										  + ", Tipo Recibo: " + reg.getId().getTipoRec());
						}
					} else {
						throw new Exception ("Compañía: " + reg.getId().getCodComp()
						                 + ", Sucursal: " + reg.getId().getCodSuc()
 								             + ", Caja: " + iCaja
									   + ", No. Recibo: " + reg.getId().getNumRec()
									  + ", Tipo Recibo: " + reg.getId().getTipoRec());
					}
				}
			}	
		} catch(Exception ex){
			throw new Exception("Error al actualizar hora en recibo. " + ex.getMessage());
		} finally {}
	}
}

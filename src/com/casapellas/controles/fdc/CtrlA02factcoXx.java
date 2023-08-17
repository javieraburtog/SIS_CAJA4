package com.casapellas.controles.fdc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.hibernate.Session;

import com.casapellas.entidades.A02factco;
import com.casapellas.entidades.A02factcoId;
import com.casapellas.entidades.fdc.A02factcoXx;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.JulianToCalendar;

public class CtrlA02factcoXx {	
	
	public boolean SiExistenDatosXProcesar(String sSucursal, String sZona, Session sesion) throws Exception{
		boolean existen = false;
		String sql = null;
		Object obj = null;
		Long nRegs = null;
		
		//Session sesion = HibernateUtil.getSessionFactoryFDC().openSession();
						
		try {
			sesion.beginTransaction();
			
			//Zona
			sql = "select count(*) from A02factcoXx a where a.id.sucursal = :pSuc and a.id.zona = :pZonaORuta and a.flwSts = '0'";					
			obj = sesion.createQuery(sql)
				  .setParameter("pSuc", sSucursal)
				  .setParameter("pZonaORuta", sZona)
				  .uniqueResult();			
			if (obj != null) {
				nRegs = (Long)obj;
			}
			if ((nRegs != null) && !(nRegs > 0)) {
				//Ruta
				sql = "select count(*) from A02factcoXx a where a.id.sucursal = :pSuc and a.id.ruta = :pZonaORuta and a.flwSts = '0'";			
				obj = sesion.createQuery(sql)
				      .setParameter("pSuc", sSucursal)
				      .setParameter("pZonaORuta", sZona)
				      .uniqueResult();			
				if (obj != null) {
					nRegs += (Long)obj;
				}
				sesion.getTransaction().commit();
			}	
			if ((nRegs != null) && nRegs > 0){
				existen = true;
			}			
			
		} catch(Exception ex){
			throw new Exception(ex.getMessage());
		} finally {
			//sesion.close();
		}
		return existen;
	}
	
	@SuppressWarnings("unchecked")
	public void Procesar(Session sesionFDC, Session sesionMCAJA, String sSucursal, String sdLocn) throws Exception{		
		String sql = null;		
		List<A02factcoXx> lstRegs = null;
		A02factcoXx regToUpdate = null;
		boolean lZona = false, lRuta = false;
		String pZona, pRuta, sTmp;
		ArrayList<A02factco>facturasPocket = new ArrayList<A02factco>();
						
		try {
			//Zona
			sql = "From A02factcoXx as a where a.id.sucursal = :pSucursal and a.id.zona = :pZonaORuta and a.flwSts = '0'";
			lstRegs = (ArrayList<A02factcoXx>)sesionFDC.createQuery(sql)
			.setParameter("pSucursal", sSucursal)
			.setParameter("pZonaORuta", sdLocn)
			.list();
			if(lstRegs != null && lstRegs.size() > 0) {
				lZona = true;
			} else {
				sql = "From A02factcoXx as a where a.id.sucursal = :pSucursal and a.id.ruta = :pZonaORuta and a.flwSts = '0'";
				lstRegs = (ArrayList<A02factcoXx>)sesionFDC.createQuery(sql)
				.setParameter("pSucursal", sSucursal)
				.setParameter("pZonaORuta", sdLocn)
				.list();				
				if(lstRegs != null && lstRegs.size() > 0) {
					lRuta = true;
				}
			}
			if (lZona || lRuta) {
				for(A02factcoXx reg : lstRegs) {
					A02factco a02factco = new A02factco();
					A02factcoId id = new A02factcoId();					
					id.setCodsuc(reg.getId().getCodSuc());
					id.setCodunineg(reg.getId().getCodUniNeg());
					id.setNofactura(reg.getId().getNoFactura());
					id.setTipofactura(reg.getId().getTipoFactura());
					
					//&& ======= Campos nuevos de llave.
					id.setCodcli(reg.getId().getCodCli()) ;
					id.setFecha(reg.getFecha());
					
					//a02factco.setCodcli(reg.getId().getCodCli());
					a02factco.setCodcomp(reg.getCodComp());
					a02factco.setCodvendor(reg.getCodVendor());
					
					sTmp = reg.getEstado();
					if (sTmp.equals("-")) {
						sTmp = "";
					}					
					a02factco.setEstado(sTmp);
					
					//a02factco.setFecha(reg.getFecha());
					a02factco.setFechagrab(reg.getFechaGrab());
					a02factco.setHechopor(reg.getHechoPor());
					a02factco.setHora(reg.getHora());
					a02factco.setId(id);
					a02factco.setMoneda(reg.getMoneda());
					a02factco.setNodoco(reg.getNoDoco());
					a02factco.setNomclie(reg.getNomCli());
					a02factco.setNomcomp(reg.getNomComp());
					a02factco.setNomsuc(reg.getNomSuc());
					a02factco.setPantalla(reg.getPantalla());
					a02factco.setProgramaid(reg.getProgramaId());
					a02factco.setSdlocn(reg.getSdLocn());
					a02factco.setSubtotal((long)reg.getSubTotal());
					a02factco.setTasa(reg.getTasa());
					
					sTmp = reg.getTipoDoco().trim();
					if (sTmp.equals("-")) {
						sTmp = "";
					}					
					a02factco.setTipodoco(sTmp);
					
					a02factco.setTipopago(reg.getTipoPago());
					a02factco.setTotal(reg.getTotal());
					a02factco.setUnineg(reg.getUniNeg());
					
					// && =============== Procesar: Nuevos valores en la entidad. 
					a02factco.setTotalcosto(BigDecimal.ZERO);
					a02factco.setTotaldscto(BigDecimal.ZERO);

					a02factco.setSubtotalf(new Long("0"));
					a02factco.setTotalf(new Long("0"));
					a02factco.setEnviadoa(reg.getId().getCodCli());
					
					
					a02factco.setValisc(reg.getValisc());
					a02factco.setDesctovta(reg.getDesctovta());
					a02factco.setDesctoisc(reg.getDesctoisc());
					a02factco.setValiscfinal(reg.getValiscfinal());
					a02factco.setDesgloseisc(reg.getDesgloseisc());
					a02factco.setExoneraisc(reg.getExoneraisc());
					
					//&& =========== valores para fecha de factura original a devolver.
					a02factco.setFechadoco(0);
					
					sesionMCAJA.save(a02factco);
					
					//&& ==== Guardar facturas para leer datos codcli y fecha en Recibofac y Arqueofact
					facturasPocket.add(a02factco);
					
					if (lZona){
						pZona = sdLocn;
						pRuta = "_";
					} else {
						pZona = "_";
						pRuta = sdLocn;
					}
					
					//&& ======= condicionar mas la consulta por nueva llave.
					sql = "From A02factcoXx as a where a.id.sucursal = :pSucursal and a.id.zona = :pZona and a.id.ruta = :pRuta and " +
					      "a.id.codCli = :pCliente and a.id.noFactura = :pNoFactura and a.id.tipoFactura = :pTipoFactura and " + 
					      "a.id.codUniNeg = :pCodUniNeg and a.id.codSuc = :pCodSuc and a.flwSts = '0' ";
					sql += " and a.fecha = :pFecha ";
					

					regToUpdate = (A02factcoXx)sesionFDC.createQuery(sql)
						.setParameter("pSucursal", sSucursal)
						.setParameter("pZona", pZona)
						.setParameter("pRuta", pRuta)
						.setParameter("pNoFactura", reg.getId().getNoFactura())
						.setParameter("pTipoFactura", reg.getId().getTipoFactura())
						.setParameter("pCodUniNeg", reg.getId().getCodUniNeg())
						.setParameter("pCodSuc", reg.getId().getCodSuc())
						.setParameter("pCliente", reg.getId().getCodCli())
						.setParameter("pFecha", reg.getFecha())						
						.uniqueResult();

					if(regToUpdate != null) {
//						regToUpdate.setFlwSts('1');
						regToUpdate.setFlwSts("1");
						sesionFDC.update(regToUpdate);
					} else {
						throw new Exception ("Sucursal: " + reg.getId().getSucursal()
								             + ", Zona: " + reg.getSdLocn()
						                  + ", Cliente: " + reg.getId().getCodCli() 
						                  + ", Factura: " + reg.getId().getNoFactura()
						             + ", Tipo Factura: " + reg.getId().getTipoFactura());
					}
				}
			}						
		} catch(Exception ex){ 
			facturasPocket = null;
			throw new Exception("Error al procesar header de factura. " + ex.getMessage());
		} finally {
			
			try{
				if(facturasPocket != null  && !facturasPocket.isEmpty()){
					Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
					m.put("fc_FacturasPocket", facturasPocket);
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}

	public void actualizar_hora_en_facturas(Session sesionFDC, String sCia, String sSucursal, int iCaja, String sdLocn) throws Exception {
		String sql = null;		
		List<A02factcoXx> lstRegs = null;
		A02factcoXx regToUpdate = null;
		boolean lZona = false, lRuta = false;
		String pZona, pRuta;
		int iArqueo;

		try{
			//Zona
			sql = "From A02factcoXx as a where a.id.sucursal = :pSucursal and a.id.zona = :pZonaORuta";// and a.flwSts = '0'";
			lstRegs = (ArrayList<A02factcoXx>)sesionFDC.createQuery(sql)
			.setParameter("pSucursal", sSucursal)
			.setParameter("pZonaORuta", sdLocn)
			.list();
			if(lstRegs != null && lstRegs.size() > 0) {
				lZona = true;
			} else {
				sql = "From A02factcoXx as a where a.id.sucursal = :pSucursal and a.id.ruta = :pZonaORuta";// and a.flwSts = '0'";
				lstRegs = (ArrayList<A02factcoXx>)sesionFDC.createQuery(sql)
				.setParameter("pSucursal", sSucursal)
				.setParameter("pZonaORuta", sdLocn)
				.list();				
				if(lstRegs != null && lstRegs.size() > 0) {
					lRuta = true;
				}
			}
			if (lZona || lRuta) {
				for(A02factcoXx reg : lstRegs) {
					CtrlArqueofactXx ctrlarqueofact = new CtrlArqueofactXx();  
					iArqueo = ctrlarqueofact.get_numero_de_arqueo(sesionFDC, sCia, "000" + sSucursal ,iCaja, reg.getId().getNoFactura(), 
							                                      '-', reg.getId().getTipoFactura(), reg.getId().getCodUniNeg());
					if (iArqueo != 0) {
						JulianToCalendar fecha = new JulianToCalendar(reg.getFecha());						
						Calendar c = fecha.getDate();
												
						String sFecha = new FechasUtil().formatDatetoString(c.getTime(), "yyyy-MM-dd");
						
//						new Simplec.getTime();
//						int iAnio = c.get(Calendar.YEAR);
//						String sMonth = "00" + c.get(Calendar.MONTH);
//						String sDay = "00" + c.get(Calendar.DAY_OF_MONTH);
//						
//						sMonth = sMonth.substring(sMonth.length() - 2);
//						sDay = sDay.substring(sDay.length() - 2);
						
						//String sFecha = iAnio + "-" + sMonth + "-" + sDay;

					    CtrlArqueoXx ctrlarqueo = new CtrlArqueoXx();
					    String sHora = ctrlarqueo.get_hora_de_arqueo(sesionFDC, sCia, "000" + sSucursal, iArqueo, iCaja, sFecha);
					    
					    int iYear, iMonth, iDay;
					    iYear = Integer.parseInt(sFecha.substring(0, 4));
					    iMonth = Integer.parseInt(sFecha.substring(5, 7));
					    iDay = Integer.parseInt(sFecha.substring(8, 10));
					    
					    int iHour, iMin, iSeg;
					    iHour = Integer.parseInt(sHora.substring(0, 2));
					    iMin = Integer.parseInt(sHora.substring(3, 5));
					    iSeg = Integer.parseInt(sHora.substring(6, 8));

					    Calendar calendar = new GregorianCalendar(iYear, iMonth, iDay, iHour, iMin, iSeg);
						calendar.add(calendar.MINUTE, -5);
						
						FechasUtil fechasutil = new FechasUtil();
						sHora = fechasutil.formatDatetoString(calendar.getTime(), "HH:mm:ss");

						java.util.Date date = fechasutil.getDateFromString(sHora, "HH:mm:ss");
						
						int iHora = fechasutil.formatHoratoInt(date); 
					    
						if (lZona){
							pZona = sdLocn;
							pRuta = "_";
						} else {
							pZona = "_";
							pRuta = sdLocn;
						}
						
						sql = "From A02factcoXx as a where a.id.sucursal = :pSucursal and a.id.zona = :pZona and a.id.ruta = :pRuta and " +
						      "a.id.codCli = :pCliente and a.id.noFactura = :pNoFactura and a.id.tipoFactura = :pTipoFactura and " + 
						      "a.id.codUniNeg = :pCodUniNeg and a.id.codSuc = :pCodSuc";// and a.flwSts = '0'";

						regToUpdate = (A02factcoXx)sesionFDC.createQuery(sql)
							.setParameter("pSucursal", sSucursal)
							.setParameter("pZona", pZona)
							.setParameter("pRuta", pRuta)
							.setParameter("pCliente", reg.getId().getCodCli())
							.setParameter("pNoFactura", reg.getId().getNoFactura())
							.setParameter("pTipoFactura", reg.getId().getTipoFactura())
							.setParameter("pCodUniNeg", reg.getId().getCodUniNeg())
							.setParameter("pCodSuc", reg.getId().getCodSuc())
							.uniqueResult();

						if(regToUpdate != null) {
							regToUpdate.setHora(iHora);
							sesionFDC.update(regToUpdate);
						} else {
							throw new Exception ("Sucursal: " + reg.getId().getCodSuc()
									             + ", Zona: " + pZona
									             + ", Ruta: " + pRuta
							                  + ", Cliente: " + reg.getId().getCodCli() 
							                  + ", Factura: " + reg.getId().getNoFactura()
							             + ", Tipo Factura: " + reg.getId().getTipoFactura()
							        + ", Unidad de Negocio: " + reg.getId().getCodUniNeg());
						}
					} else {
						//No toda factura tiene un número de arqueo.
						//Si la pocket sólo realiza facturas con promoción líquido gratis (facturas de Contado/Crédito con descuento del 100%)
						//en este caso no hay ingresos, no hay minuta de depósito, no se consume el consecutivo de los No's de arqueo y la factura
						//no tiene No. de arqueo asociado.
						if (reg.getSubTotal() != 0 || reg.getTotal() != 0) {
						    throw new Exception ("Sucursal: " + reg.getId().getCodSuc()
							                     + ", Zona: " + reg.getId().getZona()
							                     + ", Ruta: " + reg.getId().getRuta()
					                          + ", Cliente: " + reg.getId().getCodCli() 
					                          + ", Factura: " + reg.getId().getNoFactura()
					                     + ", Tipo Factura: " + reg.getId().getTipoFactura()
					                + ", Unidad de Negocio: " + reg.getId().getCodUniNeg());
						}
					}
				}
			}						
		} catch(Exception ex){
			throw new Exception("Error al actualizar hora en factura. " + ex.getMessage());
		} finally {}
	}
}

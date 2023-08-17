package com.casapellas.controles.fdc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.hibernate.Session;

import com.casapellas.entidades.MetodosPago;
import com.casapellas.entidades.Recibodet;
import com.casapellas.entidades.RecibodetId;
import com.casapellas.entidades.fdc.InfAdicionalFCV;
import com.casapellas.entidades.fdc.ReciboXx;
import com.casapellas.entidades.fdc.RecibodetXx;
import com.casapellas.util.PropertiesSystem;

public class CtrlRecibodetXx {
	
	public boolean SiExistenDatosXProcesar(int iCaja, Session sesion) throws Exception{
		boolean existen = false;
		String sql = null;
		Object obj = null;
		Long nRegs = null;
		
		//Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		
//		Session sesion = null;
//		if (m.get("SQLServer2000") != null) {
//			sesion = HibernateUtil.getSessionFactoryFDC().openSession();
//		} else if (m.get("SQLServer2008") != null) {
//			sesion = HibernateUtil.getSessionFactoryFDC2008().openSession();
//		}
		
		//Session sesion = HibernateUtil.getSessionFactoryFDC().openSession();
						
		try {
			sesion.beginTransaction();
			
			sql = "select count(*) from RecibodetXx a where a.id.caId = :pCaja and a.flwSts = '0'";					
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
		List<RecibodetXx> lstRegs = null;
		RecibodetXx regToUpdate = null;

		try{
			sql = "From RecibodetXx as a where a.id.caId = :pCaja and a.flwSts = '0'";
		
			lstRegs = (ArrayList<RecibodetXx>)sesionFDC.createQuery(sql).setParameter("pCaja", iCaja).list();
			
			if(lstRegs != null && lstRegs.size() > 0) {
				for(RecibodetXx reg : lstRegs) {
					Recibodet recibodet = new Recibodet();
					RecibodetId id = new RecibodetId();
					
					id.setCaid(reg.getId().getCaId());
					id.setCodcomp(reg.getId().getCodComp());
					id.setCodsuc(reg.getId().getCodSuc());
					id.setMoneda(reg.getId().getMoneda());
					id.setMonto(reg.getMonto().doubleValue());
					id.setMpago(reg.getId().getMpago());
					id.setNumrec(reg.getId().getNumRec());
					
					sTmp = reg.getRefer1().trim();
					if (sTmp.equals("-")) {
						sTmp = "";
					}					
					id.setRefer1(sTmp);
					
					sTmp = reg.getRefer2().trim();
					if (sTmp.equals("-")) {
						sTmp = "";
					}					
					id.setRefer2(sTmp);
					
					sTmp = reg.getRefer3().trim();
					if (sTmp.equals("-")) {
						sTmp = "";
					}					
					id.setRefer3(sTmp);
					
					sTmp = reg.getRefer4().trim();
					if (sTmp.equals("-")) {
						sTmp = "";
					}					
					id.setRefer4(sTmp);
					
					sTmp = reg.getRefer5().trim();
					if (sTmp.equals("-")) {
						sTmp = "";
					}					
					recibodet.setRefer5(sTmp);
					
					sTmp = reg.getRefer6().trim();
					if (sTmp.equals("-")) {
						sTmp = "";
					}					
					recibodet.setRefer6(sTmp);
					
					sTmp = reg.getRefer7().trim();
					if (sTmp.equals("-")) {
						sTmp = "";
					}					
					recibodet.setRefer7(sTmp);
					
					id.setTiporec(reg.getId().getTipoRec());										
					
					recibodet.setCaidpos(iCaja);
					recibodet.setEquiv(reg.getEquiv());
					recibodet.setId(id);
					recibodet.setMonto(reg.getMonto());
					recibodet.setNumrecm(reg.getNumRecM());
					recibodet.setTasa(reg.getTasa());
					recibodet.setVmanual("0");	
					recibodet.setNombre("");
					
					recibodet.setReferencenumber(0);
					recibodet.setDepctatran(0);
					
					
					try {
					
						if( ( id.getMpago().trim().compareTo("8") == 0 || id.getMpago().trim().compareTo("N") == 0 ) &&  
							  id.getRefer1().trim().matches(PropertiesSystem.REGEXP_NUMBER) ){
							
							//&& ================= Determinar si el  pago debe ir en preconciliacion
							boolean preconciliacion = com.casapellas.controles.BancoCtrl.ingresoBajoPreconciliacion(
									Integer.parseInt( id.getRefer1().trim()  ), id.getCaid(), id.getCodcomp() );
							
							recibodet.setDepctatran( (preconciliacion ? 1 : 0) );
							 
							//&& ================= conservar el numero de referencia original
							int iReferenceNumber = 0 ;
							String strReferencenumber =  id.getMpago().compareTo("8") == 0 ? id.getRefer3().trim() : id.getRefer2().trim() ;
							if(strReferencenumber.matches(PropertiesSystem.REGEXP_8DIGTS)) {
								iReferenceNumber = Integer.parseInt( strReferencenumber ) ;
							}
							recibodet.setReferencenumber( iReferenceNumber );
						}
						
					} catch (Exception e) {
						recibodet.setReferencenumber(0);
						recibodet.setDepctatran(0);
					}
					
					recibodet.setCodigomarcatarjeta("");
					recibodet.setMarcatarjeta("");
					recibodet.setLiquidarpormarca(0);
					
					sesionMCAJA.save(recibodet);
					
					sql = "From RecibodetXx as a where a.id.numRec = :pNumRec and a.id.codComp = :pCodComp and a.id.caId = :pCaId and " +
				          "a.id.codSuc = :pCodSuc and a.id.tipoRec = :pTipoRec and a.id.mpago = :pMPago and a.id.moneda = :pMoneda and " +
				          "a.refer1 = :pRefer1 and a.refer2 = :pRefer2 and a.refer3 = :pRefer3 and a.refer4 = :pRefer4 and " +
				          "a.flwSts = '0'";

				    regToUpdate = (RecibodetXx) sesionFDC.createQuery(sql)
					.setParameter("pNumRec", reg.getId().getNumRec())
					.setParameter("pCodComp", reg.getId().getCodComp())
					.setParameter("pCaId", iCaja)
					.setParameter("pCodSuc", reg.getId().getCodSuc())
					.setParameter("pTipoRec", reg.getId().getTipoRec())
					.setParameter("pMPago", reg.getId().getMpago())
					.setParameter("pMoneda", reg.getId().getMoneda())
					.setParameter("pRefer1", reg.getRefer1())
					.setParameter("pRefer2", reg.getRefer2())
					.setParameter("pRefer3", reg.getRefer3())
					.setParameter("pRefer4", reg.getRefer4())
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
									  + ", Tipo Recibo: " + reg.getId().getTipoRec()
										   + ", Moneda: " + reg.getId().getMoneda());
					}
				}
			}						
		} catch(Exception ex){
			throw new Exception("Error al procesar detalle de recibos. " + ex.getMessage());
		} finally {
		}
	}
	
	public List<Object> CompletarObjetosRecibo(Session sesionFDC, List<Object> lstFichasCV) throws Exception{		
		String sql = null;
		int i;
		Object[][] objFCV = new Object[1][2];
		InfAdicionalFCV infAdicionalFCV = null;
		List<RecibodetXx> lstRegs = null;
							
		try {			
			for (i = 0; i < lstFichasCV.size(); i++) {
				objFCV = (Object[][]) lstFichasCV.get(i);
				infAdicionalFCV = (InfAdicionalFCV) objFCV[0][0];
			
				sql = "From RecibodetXx as a where a.id.numRec = :pNumRec and a.id.codComp = :pCodComp and a.id.caId = :pCaId and " +
		              "a.id.codSuc = :pCodSuc and a.id.tipoRec = :pTipoRec and a.id.mpago = :pMPago and a.id.moneda = :pMoneda";

				lstRegs = (List<RecibodetXx>) sesionFDC.createQuery(sql)
				.setParameter("pNumRec", infAdicionalFCV.getNumRec())
				.setParameter("pCodComp", infAdicionalFCV.getCodComp())
				.setParameter("pCaId", infAdicionalFCV.getCaId())
				.setParameter("pCodSuc", infAdicionalFCV.getCodSuc())
				.setParameter("pTipoRec", infAdicionalFCV.getTipoRec())
				.setParameter("pMPago", "5")
				.setParameter("pMoneda", "USD")
				.list();
				if (lstRegs != null) {
					infAdicionalFCV.setMoneda(lstRegs.get(0).getId().getMoneda());
					infAdicionalFCV.setTasaDeCambio(lstRegs.get(0).getTasa());
					objFCV[0][0] = infAdicionalFCV;
					lstFichasCV.set(i, objFCV);
//					lstFichasCV.remove(i);
//					lstFichasCV.add(i, objFCV);
				} else {
					throw new Exception ("Compañía: " + infAdicionalFCV.getCodComp()
				 			         + ", Sucursal: " + infAdicionalFCV.getCodSuc()
							             + ", Caja: " + infAdicionalFCV.getCaId()
						           + ", No. Recibo: " + infAdicionalFCV.getNumRec()
						          + ", Tipo Recibo: " + infAdicionalFCV.getTipoRec()
							           + ", Moneda: " + infAdicionalFCV.getMoneda());
				}
			}						
		} catch(Exception ex){
			throw new Exception("Error al completar tasa de cambio. " + ex.getMessage());
		} finally {}
		return lstFichasCV;
	}
	
	public List<MetodosPago> getMetodosDePagoDeRecibo(Session sesionFDC, ReciboXx reg_ReciboXx) throws Exception { 		
		String sql = null;		
		List<RecibodetXx> lstRegs = null;
		MetodosPago[] mpagos = null;
		
		List<MetodosPago> selectedMet = null; 

		try {
			sql = "From RecibodetXx as a where a.id.numRec = :pNumRec and a.id.codComp = :pCodComp and a.id.caId = :pCaId and " + 
				  "a.id.codSuc = :pCodSuc and a.id.tipoRec = :pTipoRec";
			lstRegs = (ArrayList<RecibodetXx>) sesionFDC.createQuery(sql)
			.setParameter("pNumRec", reg_ReciboXx.getId().getNumRec())
			.setParameter("pCodComp", reg_ReciboXx.getId().getCodComp())
//			.setParameter("pCaId", reg_ReciboXx.getId().getCaid())
			
			.setParameter("pCaId", reg_ReciboXx.getId().getCaId())
			.setParameter("pCodSuc", reg_ReciboXx.getId().getCodSuc())
			.setParameter("pTipoRec", reg_ReciboXx.getId().getTipoRec())
			.list();			
			
			if(lstRegs != null && lstRegs.size() > 0) {
				selectedMet = new ArrayList<MetodosPago>();
				mpagos = new MetodosPago[lstRegs.size()];
				int i = 0;
				for(RecibodetXx reg : lstRegs) {
					mpagos[i] = new MetodosPago(reg.getId().getMpago(), reg.getId().getMoneda(), reg.getMonto().doubleValue(), reg.getTasa(), 
							                    reg.getEquiv().doubleValue(), reg.getRefer1(), reg.getRefer2(), reg.getRefer3(), reg.getRefer4());
					//Implementar mejora en justificación del pago en pockets para que solicite el tipo de afiliado (para determinar si el voucher es manual o automático).
					mpagos[i].setVmanual("0"); 
					selectedMet.add(mpagos[i]);
					++i;
				}
			}						
		} catch(Exception ex){
			throw new Exception("Error al procesar detalle de recibos. " + ex.getMessage());
		} finally {}
		return selectedMet;
	}

}

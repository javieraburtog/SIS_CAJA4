package com.casapellas.controles.fdc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.hibernate.Session;

import com.casapellas.entidades.Cambiodet;
import com.casapellas.entidades.CambiodetId;
import com.casapellas.entidades.Vf55ca01;
import com.casapellas.entidades.fdc.CambiodetXx;
import com.casapellas.entidades.fdc.ReciboXx;

public class CtrlCambiodetXx {
	
	public boolean SiExistenDatosXProcesar(int iCaja, Session sesion) throws Exception{
		boolean existen = false;
		String sql = null;
		Object obj = null;
		Long nRegs = null;
		
		//Session sesion = HibernateUtil.getSessionFactoryFDC().openSession();
						
		try{
			sesion.beginTransaction();
			
			sql = "select count(*) from CambiodetXx a where a.id.caId = :pCaja and a.flwSts = '0'";					
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
		String sql = null;		
		List<CambiodetXx> lstRegs = null;
		CambiodetXx regToUpdate = null;
							
		try{
			sql = "From CambiodetXx as a where a.id.caId = :pCaja and a.flwSts = '0'";
			lstRegs = (ArrayList<CambiodetXx>)sesionFDC.createQuery(sql)
			.setParameter("pCaja", iCaja)
			.list();
			if(lstRegs != null && lstRegs.size() > 0) {
				for(CambiodetXx reg : lstRegs) {
					Cambiodet cambiodet = new Cambiodet();
					CambiodetId id = new CambiodetId();
					
					id.setCaid(reg.getId().getCaId());
					id.setCodcomp(reg.getId().getCodComp());
					id.setCodsuc(reg.getId().getCodSuc());
					id.setMoneda(reg.getId().getMoneda());
					id.setNumrec(reg.getId().getNumRec());
					id.setTiporec(reg.getId().getTipoRec());
					
					cambiodet.setCambio(reg.getCambio());
					cambiodet.setId(id);
					cambiodet.setTasa(reg.getTasa());
					
					sesionMCAJA.save(cambiodet);					

					sql = "From CambiodetXx as a where a.id.numRec = :pNumRec and a.id.codComp = :pCodComp and a.id.caId = :pCaId and " +
				          "a.id.codSuc = :pCodSuc and a.id.tipoRec = :pTipoRec and a.id.moneda = :pMoneda and a.flwSts = '0'";

				    regToUpdate = (CambiodetXx)sesionFDC.createQuery(sql)
					.setParameter("pNumRec", reg.getId().getNumRec())
					.setParameter("pCodComp", reg.getId().getCodComp())
					.setParameter("pCaId", iCaja)
					.setParameter("pCodSuc", reg.getId().getCodSuc())
					.setParameter("pTipoRec", reg.getId().getTipoRec())
					.setParameter("pMoneda", reg.getId().getMoneda())
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
			throw new Exception("Error al procesar información de cambios. " + ex.getMessage());
		} finally {
		}
	}
	
	public List<Object> obtenerInformacionDeCambio(Session sesionFDC, String sCodComp,
			String sCodSuc, int iCaId, int iNumRec, String sTipoRec, String sMonedaDeFactura) throws Exception {
		
		List<Object> lstInformacionDeCambio = null;
		String sql = null;
						
		try {
			
			sql = "Select a.cambio, a.tasa, a.id.moneda From CambiodetXx as a where a.id.numRec = :pNumRec and a.id.codComp = :pCodComp and " +
		          "a.id.caId = :pCaId and a.id.codSuc = :pCodSuc and a.id.tipoRec = :pTipoRec";
			
			List<Object[]> lstObjects = null; //new ArrayList<Object[]>() ;
			
			lstObjects = (ArrayList<Object[]>) sesionFDC.createQuery(sql)
			.setParameter("pNumRec", iNumRec)
			.setParameter("pCodComp", sCodComp)
			.setParameter("pCaId", iCaId)
			.setParameter("pCodSuc", sCodSuc)
			.setParameter("pTipoRec", sTipoRec)
			.list();
	
	        if(lstObjects != null && lstObjects.size() > 0){
	        	String[] sMonedasDeCambio = new String[lstObjects.size()];
			    BigDecimal[] dValorCambio = new BigDecimal[lstObjects.size()];
				BigDecimal[] dTasasDeCambio = new BigDecimal[lstObjects.size()];
				int i = 0;
	            for (Object[] ob : lstObjects) {
				    sMonedasDeCambio[i] = String.valueOf(ob[2]);
					dTasasDeCambio[i] = new BigDecimal(String.valueOf(ob[1]));
					dValorCambio[i] = new BigDecimal(String.valueOf(ob[0]));
					i++;
				}
	    		lstInformacionDeCambio = new ArrayList<Object>();
				lstInformacionDeCambio.add(sMonedasDeCambio);
				lstInformacionDeCambio.add(dValorCambio);
				lstInformacionDeCambio.add(dTasasDeCambio);
	        }
		} catch(Exception ex){
			throw new Exception("Problemas al buscar cambio del recibo #: " + iNumRec);
		} finally {}
		return lstInformacionDeCambio;
	}
	
	public void AddRegsForJoin(Session sesionFDC, Session sesionMCAJA, int iCaja) throws Exception{		
		List<ReciboXx> lstRecibosFdc = null;

		try {
			Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
			lstRecibosFdc = (List<ReciboXx>) m.get("lstRecibosFdc");
			if(lstRecibosFdc != null && lstRecibosFdc.size() > 0) {
				for (ReciboXx reg : lstRecibosFdc) {
					Cambiodet cambiodet = new Cambiodet();
					CambiodetId id = new CambiodetId();

//					id.setCaid(reg.getId().getCaid());
					
					id.setCaid(reg.getId().getCaId());
					
					id.setCodcomp(reg.getId().getCodComp());
					id.setCodsuc(reg.getId().getCodSuc());
					id.setMoneda("COR");
					id.setNumrec(reg.getId().getNumRec());
					id.setTiporec(reg.getId().getTipoRec());
					
					cambiodet.setCambio(new BigDecimal(0.0));
					cambiodet.setId(id);
					cambiodet.setTasa(new BigDecimal(0.0));
					
					try {
						sesionMCAJA.save(cambiodet);
					} catch(Exception ex) {} 
					finally {} 
				}
			}						
		} catch(Exception ex){
			throw new Exception("Error en registros para join. " + ex.getMessage());
		} finally {}
	}
}
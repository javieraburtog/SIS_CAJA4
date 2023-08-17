package com.casapellas.controles.fdc;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.casapellas.entidades.Arqueorec;
import com.casapellas.entidades.ArqueorecId;
import com.casapellas.entidades.fdc.ArqueorecXx;

public class CtrlArqueorecXx {
	
	public boolean SiExistenDatosXProcesar(int iCaja, Session sesion) throws Exception{
		boolean existen = false;
		String sql = null;
		Object obj = null;
		Long nRegs = null;
		
		//Session sesion = HibernateUtil.getSessionFactoryFDC().openSession();
						
		try {
			sesion.beginTransaction();
			
			sql = "select count(*) from ArqueorecXx a where a.id.caId = :pCaja and a.flwSts = '0'";
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
		List<ArqueorecXx> lstRegs = null;
		ArqueorecXx regToUpdate = null;
							
		try {
			sql = "From ArqueorecXx as a where a.id.caId = :pCaja and a.flwSts = '0'";
			lstRegs = (ArrayList<ArqueorecXx>)sesionFDC.createQuery(sql)
			.setParameter("pCaja", iCaja)
			.list();
			if(lstRegs != null && lstRegs.size() > 0) {
				for(ArqueorecXx reg : lstRegs) {
					Arqueorec arqueorec = new Arqueorec();
					ArqueorecId id = new ArqueorecId();					
					id.setCaid(reg.getId().getCaId());
					id.setCodcomp(reg.getId().getCodComp());
					id.setCodsuc(reg.getId().getCodSuc());
					id.setNoarqueo(reg.getId().getNoArqueo());
					id.setNumrec(reg.getId().getNumRec());
					id.setTipodoc(reg.getId().getTipoDoc());
					id.setTiporec(reg.getId().getTipoRec());
					arqueorec.setId(id);
					sesionMCAJA.save(arqueorec);					
					
					sql = "From ArqueorecXx as a where a.id.codComp = :pCodComp and a.id.codSuc = :pCodSuc and a.id.caId = :pCaId and " +
				          "a.id.noArqueo = :pNoArqueo and a.id.numRec = :pNumRec and a.id.tipoRec = :pTipoRec and " + 
				          "a.id.tipoDoc = :pTipoDoc and a.flwSts = '0'";

				    regToUpdate = (ArqueorecXx)sesionFDC.createQuery(sql)
					.setParameter("pCodComp", reg.getId().getCodComp())
					.setParameter("pCodSuc", reg.getId().getCodSuc())
					.setParameter("pCaId", iCaja)
					.setParameter("pNoArqueo", reg.getId().getNoArqueo())
					.setParameter("pNumRec", reg.getId().getNumRec())
					.setParameter("pTipoRec", reg.getId().getTipoRec())
					.setParameter("pTipoDoc", reg.getId().getTipoDoc())
					.uniqueResult();
					
					if(regToUpdate != null) {
//						regToUpdate.setFlwSts('1');
						regToUpdate.setFlwSts("1");
						sesionFDC.update(regToUpdate);
					} else {
						throw new Exception ("Caja: " + iCaja
								   + ", No. Arqueo: " + reg.getId().getNoArqueo()
						           + ", No. Recibo: " + reg.getId().getNumRec()
						          + ", Tipo Recibo: " + reg.getId().getTipoRec());						                                                           
					}
				}
			}						
		} catch(Exception ex){
			throw new Exception("Error al procesar recibos realizados en arqueo. " + ex.getMessage());
		} finally {
		}
	}

	public int get_numero_de_arqueo(Session sesionFDC, String sCia, String sSucursal, int iCaja, int iNumRec, String sTipoRec, String sTipoDoc) throws Exception {
		String sql = null;
		int iArqueo = 0;
		List<ArqueorecXx> lstRegs = null;
		ArqueorecXx arqueorec = null;

		try {
			//Select a.id.noArqueo From 
			sql = "From ArqueorecXx as a where a.id.codComp = :pCia and a.id.codSuc = :pSuc and a.id.caId = :pCaja "
				+ "and a.id.numRec = :pNumRec and a.id.tipoRec = :pTipoRec and a.id.tipoDoc = :pTipoDoc Order By a.id.numRec";
			lstRegs = (ArrayList<ArqueorecXx>) sesionFDC.createQuery(sql)
			        .setParameter("pCia", sCia)
					.setParameter("pSuc", sSucursal)
					.setParameter("pCaja", iCaja)
					.setParameter("pNumRec", iNumRec)
					.setParameter("pTipoRec", sTipoRec)
					.setParameter("pTipoDoc", sTipoDoc)
					.list();
			if(lstRegs != null && lstRegs.size() > 0) {
				arqueorec = lstRegs.get(0);				
				iArqueo = arqueorec.getId().getNoArqueo();				
			} else {
				throw new Exception("Compañía: " + sCia 
						        + ", Sucursal: " + sSucursal
						            + ", Caja: " + iCaja 
						+ ", Número de Recibo: " + iNumRec
						  + ", Tipo de Recibo: " + sTipoRec 
				       + ", Tipo de Documento: " + sTipoDoc);
			}
		} catch (Exception ex) {
			throw new Exception("Error al obtener número de arqueo de recibo. " + ex.getMessage());
		} finally {}
		return iArqueo;
	}
}

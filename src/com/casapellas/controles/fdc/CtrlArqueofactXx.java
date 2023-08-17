package com.casapellas.controles.fdc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.casapellas.entidades.A02factco;
import com.casapellas.entidades.Arqueofact;
import com.casapellas.entidades.ArqueofactId;
import com.casapellas.entidades.fdc.ArqueofactXx;
import com.casapellas.entidades.fdc.ReciboXx;
import com.casapellas.entidades.fdc.RecibofacXx;
import com.casapellas.util.FechasUtil;
import com.ibm.icu.text.SimpleDateFormat;

public class CtrlArqueofactXx {
	
	public boolean SiExistenDatosXProcesar(int iCaja, Session sesion) throws Exception{
		boolean existen = false;
		String sql = null;
		Object obj = null;
		Long nRegs = null;
		
		//Session sesion = HibernateUtil.getSessionFactoryFDC().openSession();
						
		try {
			sesion.beginTransaction();
			
			sql = "select count(*) from ArqueofactXx a where a.id.caId = :pCaja and a.flwSts = '0'";					
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
	
	public static A02factco buscarFacturaOriginal(
			final int numfac, final String tipofac,
			final String codunineg, final String codcomp, 
			ArrayList<A02factco> facturas){
		
		A02factco a = null;
		try {
			
			if(facturas == null ) return null;
			
			a  = (A02factco)CollectionUtils.find(facturas, 
					new Predicate() {
						public boolean evaluate(Object o) {
							A02factco a = (A02factco)o;
							
							boolean result = a.getId().getNofactura()
								.intValue() == numfac && a.getId()
								.getTipofactura().trim().compareTo(
								tipofac.trim()) == 0 && a.getId()
								.getCodunineg().trim().compareTo(
								codunineg.trim()) == 0;
							
							if (codcomp.trim().compareTo("") != 0)
								result = result && a.getCodcomp().trim()
								.compareTo(codcomp.trim()) == 0;
							
							return result ;
						}
					}) ;
		} catch (Exception e) {
			a = null;
		}
		return a ;
	}
	
	@SuppressWarnings("unchecked")
	public void Procesar(Session sesionFDC, Session sesionMCAJA, int iCaja) throws Exception{		
		String sql = null, sTmp;		
		List<ArqueofactXx> lstRegs = null;
		ArqueofactXx regToUpdate = null;
							
		try{
			sql = "From ArqueofactXx as a where a.id.caId = :pCaja and a.flwSts = '0'";
			lstRegs = (ArrayList<ArqueofactXx>) sesionFDC.createQuery(sql)
								.setParameter("pCaja", iCaja).list();
			
			if(lstRegs != null && lstRegs.size() > 0) {
				
				Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();	
				ArrayList<A02factco> facturas = (ArrayList<A02factco>)m.get("fc_FacturasPocket");
				
				for(ArqueofactXx reg : lstRegs) {
				
					Arqueofact arqueofact = new Arqueofact();
					ArqueofactId id = new ArqueofactId();
					
					id.setCaid(reg.getId().getCaId());
					id.setCodcomp(reg.getId().getCodComp());
					id.setCodsuc(reg.getId().getCodSuc());
					id.setCodunineg(reg.getId().getCodUniNeg());
					id.setNoarqueo(reg.getId().getNoArqueo());
					id.setNumfac(reg.getId().getNumFac());

					//&& ==== Fecha de la factura y codigo del cliente.
					int ifecha = 0;
					int icodcli = 0;
					
					//&& == si la factura es de contado buscar fecha y cliente entre las facturas contado
					A02factco a = CtrlArqueofactXx.buscarFacturaOriginal(
							reg.getId().getNumFac(), reg.getId().getTipo(),
							reg.getId().getCodUniNeg(), reg.getId().getCodComp(),
							facturas);

					if (a != null) {
						icodcli = a.getId().getCodcli().intValue();
						ifecha  = a.getId().getFecha().intValue();
					
					}else{

						//&& === buscar el enlace a recibofac
						RecibofacXx rfx = (RecibofacXx)
							sesionFDC.createCriteria(RecibofacXx.class)
							.add(Restrictions.eq("id.codComp", reg.getId().getCodComp()))
							.add(Restrictions.eq("id.numFac", reg.getId().getNumFac()))
							.add(Restrictions.eq("id.caId", reg.getId().getCaId()))
							.add(Restrictions.eq("id.codSuc",reg.getId().getCodSuc() ))
							.add(Restrictions.eq("id.tipoFactura",reg.getId().getTipo() ))
							.add(Restrictions.eq("id.codUniNeg", reg.getId().getCodUniNeg()))
							.setMaxResults(1).uniqueResult();
						if(rfx != null){
							//&& == si es credito usar RPDIVJ = fecha factura.
							try{
								icodcli = FechasUtil.DateToJulian( new SimpleDateFormat
										("yyyy-MM-dd").parse(rfx.getRpdivj() ) );
							}catch(Exception e){}
							
							//&& === Buscar el recibo original
							ReciboXx rcSql = CtrlRecibofacXx.buscarReciboSql
									(sesionFDC, rfx.getId().getNumRec(), 
									 rfx.getId().getCodComp(), rfx.getId().getCaId(),
									 rfx.getId().getCodSuc(), rfx.getId().getTipoRec());
							if(rcSql != null)
								icodcli = rcSql.getCodCli() ;
						}
					}
					id.setFecha(ifecha);
					id.setCodcli(icodcli);
					
					sTmp = reg.getId().getPartida().trim();
					if (sTmp.equals("-")) {
						sTmp = "";
					}					
					id.setPartida(sTmp);
					
					id.setTipo(reg.getId().getTipo());
					arqueofact.setId(id);				
					sesionMCAJA.save(arqueofact);
					
					sql = "From ArqueofactXx as a where a.id.codComp = :pCodComp and a.id.codSuc = :pCodSuc and a.id.caId = :pCaId and " +
				          "a.id.noArqueo = :pNoArqueo and a.id.numFac = :pNumFac and a.id.partida = :pPartida and " + 
				          "a.id.tipo = :pTipo and a.id.codUniNeg = :pCodUniNeg and a.flwSts = '0'";

				regToUpdate = (ArqueofactXx) sesionFDC.createQuery(sql)
					.setParameter("pCodComp", reg.getId().getCodComp())
					.setParameter("pCodSuc", reg.getId().getCodSuc())
					.setParameter("pCaId", iCaja)
					.setParameter("pNoArqueo", reg.getId().getNoArqueo())
					.setParameter("pNumFac", reg.getId().getNumFac())
					.setParameter("pPartida", reg.getId().getPartida())
					.setParameter("pTipo", reg.getId().getTipo())
					.setParameter("pCodUniNeg", reg.getId().getCodUniNeg())
					.uniqueResult();
					
					if(regToUpdate != null) {
						regToUpdate.setFlwSts("1");
						sesionFDC.update(regToUpdate);
					} else {
						throw new Exception ("Caja: " + iCaja
								   + ", No. Arqueo: " + reg.getId().getNoArqueo()
						              + ", Factura: " + reg.getId().getNumFac()
						         + ", Tipo Factura: " + reg.getId().getTipo());						                                                           
					}
				}
			}						
		} catch(Exception ex){
			throw new Exception("Error al procesar facturas incluidas en arqueo. " + ex.getMessage());
		} finally {}
	}
	
	public int get_numero_de_arqueo(Session sesionFDC,	String sCia, String sSuc, int iCaja, int iFactura, char cPartida, 
			                        String sTipo, String sCodUniNeg) throws Exception {
		String sql = null;		
		Integer IArqueo = null;
		int iArqueo = 0;
		Object obj = null;
							
		try {
			sql = "Select a.id.noArqueo From ArqueofactXx as a where a.id.codComp = :pCia and a.id.codSuc = :pSuc and a.id.caId = :pCaja " + 
				  "and a.id.numFac = :pFactura and a.id.partida = :pPartida and a.id.tipo = :pTipo and a.id.codUniNeg = :pCodUniNeg";
			obj = sesionFDC.createQuery(sql)
			      .setParameter("pCia", sCia)
			      .setParameter("pSuc", sSuc)
			      .setParameter("pCaja", iCaja)
			      .setParameter("pFactura", iFactura)
			      .setParameter("pPartida", Character.toString(cPartida))
			      .setParameter("pTipo", sTipo)
			      .setParameter("pCodUniNeg", sCodUniNeg)
			      .uniqueResult();						
			if (obj != null) {
				IArqueo = (Integer) obj;
				iArqueo = IArqueo.intValue();
			} else {
				sql = "Select a.id.noArqueo From ArqueofactXx as a where a.id.codComp = :pCia and a.id.codSuc = :pSuc and a.id.caId = :pCaja " + 
				      "and a.id.numFac = :pFactura and a.id.partida = :pPartida and a.id.tipo = :pTipo and a.id.codUniNeg = :pCodUniNeg";
				obj = sesionFDC.createQuery(sql)
				      .setParameter("pCia", sCia)
				      .setParameter("pSuc", sSuc)
				      .setParameter("pCaja", iCaja)
				      .setParameter("pFactura", iFactura)
				      .setParameter("pPartida", "001")
				      .setParameter("pTipo", sTipo)
				      .setParameter("pCodUniNeg", sCodUniNeg)
				      .uniqueResult();
				if (obj != null) {
					IArqueo = (Integer) obj;
					iArqueo = IArqueo.intValue();
				}
				//No toda factura tiene un número de arqueo.
				//Si la pocket sólo realiza facturas con promoción líquido gratis (facturas de Contado/Crédito con descuento del 100%)
				//en este caso no hay ingresos, no hay minuta de depósito, no se consume el consecutivo de los No's de arqueo y la factura
				//no tiene No. de arqueo asociado.
				//else { 
//					throw new Exception ("Compañía: " + sCia
//									 + ", Sucursal: " + sSuc
//				                         + ", Caja: " + iCaja
//					                  + ", Factura: " + iFactura
//					                  + ", Partida: " + Character.toString(cPartida)
//					                     + ", Tipo: " + sTipo
//					        + ", Unidad de Negocio: " + sCodUniNeg);
//				}
			}
		} catch(Exception ex){
			throw new Exception("Error al obtener número de arqueo de factura. " + ex.getMessage());
		} finally {}
		return iArqueo;
	}
}

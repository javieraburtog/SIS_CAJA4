package com.casapellas.controles.fdc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.hibernate.Session;

import com.casapellas.entidades.A02factco;
import com.casapellas.entidades.A03factco;
import com.casapellas.entidades.A03factcoId;
import com.casapellas.entidades.fdc.A02factcoXx;
import com.casapellas.entidades.fdc.A03factcoXx;

public class CtrlA03factcoXx {
	
	public boolean SiExistenDatosXProcesar(String sSucursal, String sZona, Session sesion) throws Exception{
		boolean existen = false;
		String sql = null;
		Object obj = null;
		Long nRegs = null;
		
		//Session sesion = HibernateUtil.getSessionFactoryFDC().openSession();
						
		try {
			sesion.beginTransaction();
			
			//Zona
			sql = "select count(*) from A03factcoXx a where a.id.sucursal = :pSuc and a.id.zona = :pZonaORuta and a.flwSts = '0'";					
			obj = sesion.createQuery(sql)
				  .setParameter("pSuc", sSucursal)
				  .setParameter("pZonaORuta", sZona)
				  .uniqueResult();			
			if (obj != null) {
				nRegs = (Long)obj;
			}
			
			//Ruta
			sql = "select count(*) from A03factcoXx a where a.id.sucursal = :pSuc and a.id.ruta = :pZonaORuta and a.flwSts = '0'";			
			obj = sesion.createQuery(sql)
			      .setParameter("pSuc", sSucursal)
			      .setParameter("pZonaORuta", sZona)
			      .uniqueResult();			
			if (obj != null) {
				nRegs += (Long)obj;
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
	
	@SuppressWarnings("unchecked")
	public void Procesar(Session sesionFDC, Session sesionMCAJA, String sSucursal, String sdLocn) throws Exception{		
		String sql = null;		
		List<A03factcoXx> lstRegs = null;
		A03factcoXx regToUpdate = null;
		
		A02factcoXx regWithAdditionalData = null;
		
		boolean lZona = false, lRuta = false;
		String pZona, pRuta;		
						
		try{
			//Zona
			sql = "From A03factcoXx as a where a.id.sucursal = :pSucursal and a.id.zona = :pZonaORuta and a.flwSts = '0' Order By NoFactura";
			lstRegs = (ArrayList<A03factcoXx>)sesionFDC.createQuery(sql)
			.setParameter("pSucursal", sSucursal)
			.setParameter("pZonaORuta", sdLocn)
			.list();
			if(lstRegs != null && lstRegs.size() > 0) {
				lZona = true;
			} else {

				sql = "From A03factcoXx as a where a.id.sucursal = :pSucursal and a.id.ruta = :pZonaORuta and a.flwSts = '0' Order By NoFactura";
				lstRegs = (ArrayList<A03factcoXx>)sesionFDC.createQuery(sql)
				.setParameter("pSucursal", sSucursal)
				.setParameter("pZonaORuta", sdLocn)
				.list();				
				if(lstRegs != null && lstRegs.size() > 0) {
					lRuta = true;
				}
			}
			if (lZona || lRuta) {

				int iContLinea = 0, iNoFactura = -1;
				
				for(A03factcoXx reg : lstRegs) {
					sql = "From A02factcoXx as a where a.id.sucursal = :pSucursal and a.id.zona = :pZona and a.id.noFactura = :pNoFactura " +
					      "and a.id.tipoFactura = :pTipoFactura and a.id.codUniNeg = :pCodUniNeg ";
					regWithAdditionalData = (A02factcoXx) sesionFDC.createQuery(sql)
							.setParameter("pSucursal", sSucursal)
							.setParameter("pZona", sdLocn)							
							.setParameter("pNoFactura", reg.getId().getNoFactura())
							.setParameter("pTipoFactura", reg.getId().getTipoFactura())
							.setParameter("pCodUniNeg", reg.getId().getCodUniNeg())
							.uniqueResult();
					if (regWithAdditionalData == null) {
						sql = "From A02factcoXx as a where a.id.sucursal = :pSucursal and a.id.ruta = :pZona and a.id.noFactura = :pNoFactura " +
							      "and a.id.tipoFactura = :pTipoFactura and a.id.codUniNeg = :pCodUniNeg ";
							regWithAdditionalData = (A02factcoXx) sesionFDC.createQuery(sql)
									.setParameter("pSucursal", sSucursal)
									.setParameter("pZona", sdLocn)							
									.setParameter("pNoFactura", reg.getId().getNoFactura())
									.setParameter("pTipoFactura", reg.getId().getTipoFactura())
									.setParameter("pCodUniNeg", reg.getId().getCodUniNeg())
									.uniqueResult();
					}
					if (regWithAdditionalData == null) {						
							throw new Exception ("No Puedo Obtener Datos del Header de Factura. Sucursal: " + reg.getId().getSucursal()
												 + ", ZonaORuta: " + sdLocn
							                  + ", Factura: " + reg.getId().getNoFactura()
							             + ", Tipo Factura: " + reg.getId().getTipoFactura());						
					}										
					
					if (iNoFactura != reg.getId().getNoFactura()) {
						iNoFactura = reg.getId().getNoFactura();
						iContLinea = 0;
					}
					
					++iContLinea;
					
					A03factco a03factco = new A03factco();
					A03factcoId id = new A03factcoId();
					id.setCoditem(reg.getId().getCodItem());
					id.setCodsuc(reg.getId().getCodSuc());
					id.setCodunineg(reg.getId().getCodUniNeg());
					
					String sTmp = reg.getDescItem().trim();					
					if (sTmp.length() > 28) {
						sTmp = sTmp.substring(0,28);
					}
					sTmp += reg.getId().getEmpaque();
					
					id.setDescitem(sTmp);					
					id.setNofactura(reg.getId().getNoFactura());
					id.setTipofactura(reg.getId().getTipoFactura());
					
					//id.setCodcli(0); //
					//id.setFecha(0);  //
					
					id.setCodcli(regWithAdditionalData.getId().getCodCli());
					id.setFecha(regWithAdditionalData.getFecha());
					
					id.setLote((reg.getId().getNlote() == null )?  
							"LOTEXX" : reg.getId().getNlote().trim() ) ;
					
					id.setCant(reg.getCant());					
					id.setFactor(reg.getFactor());
					id.setImpuesto(reg.getImpuesto());
					id.setPreciounit(reg.getPrecioUnit());
					id.setPcosto(BigDecimal.ZERO);
					id.setDescuento(BigDecimal.ZERO);
					
					id.setEnviadoa(regWithAdditionalData.getId().getCodCli());
					id.setLinea(iContLinea);    //CONTEO DE LINEAS X FACTURA.
					
					//Nuevo por ISC
					id.setPorisc(reg.getPorisc());
					id.setValisc(reg.getValisc());
					id.setDesctovta(reg.getDesctovta());
					id.setDesctoisc(reg.getDesctoisc());
					id.setValiscfinal(reg.getValiscfinal());
					id.setDesgloseisc(reg.getDesgloseisc());
					//Fin-Nuevo por ISC
					
					a03factco.setId(id);

					sesionMCAJA.save(a03factco);
					
					if (lZona){
						pZona = sdLocn;
						pRuta = "_";
					} else {
						pZona = "_";
						pRuta = sdLocn;
					}

					sql = "From A03factcoXx as a where a.id.sucursal = :pSucursal " +
							"and a.id.zona = :pZona and a.id.ruta = :pRuta and "
							+ "a.id.noFactura = :pNoFactura and a.id.tipoFactura = " +
							":pTipoFactura and a.id.codUniNeg = :pUniNeg and "
							+ "a.id.codSuc = :pCodSuc and a.id.codItem = :pItem " +
							" and a.id.empaque = :pEmpaque and a.flwSts = '0' " +
							" and a.id.nlote = :pNlote ";

				regToUpdate = (A03factcoXx) sesionFDC.createQuery(sql)
					.setParameter("pSucursal", sSucursal)
					.setParameter("pZona", pZona)
					.setParameter("pRuta", pRuta)
					.setParameter("pNoFactura", reg.getId().getNoFactura())
					.setParameter("pTipoFactura", reg.getId().getTipoFactura())
					.setParameter("pUniNeg", reg.getId().getCodUniNeg())
					.setParameter("pCodSuc", reg.getId().getCodSuc())
					.setParameter("pItem", reg.getId().getCodItem())
					.setParameter("pEmpaque", reg.getId().getEmpaque())
					.setParameter("pNlote", reg.getId().getNlote())
					.uniqueResult();
					
					if(regToUpdate != null) {
						regToUpdate.setFlwSts("1");
						sesionFDC.update(regToUpdate);
					} else {
						throw new Exception ("Sucursal: " + reg.getId().getSucursal()
											 + ", Zona: " + sdLocn
						                  + ", Factura: " + reg.getId().getNoFactura()
						             + ", Tipo Factura: " + reg.getId().getTipoFactura()
						             + ", Lote Numero: " + reg.getId().getNlote()
						                     + ", Item: " + reg.getId().getCodItem()
						                  + ", Empaque: " + reg.getId().getEmpaque());
					}
				}
			}						
		} catch(Exception ex){
			throw new Exception("Error al procesar detalle de factura. " + ex.getMessage());
		} finally {
		}
	}
}

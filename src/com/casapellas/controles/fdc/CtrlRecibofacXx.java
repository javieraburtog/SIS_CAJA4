package com.casapellas.controles.fdc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.casapellas.controles.CompaniaCtrl;
import com.casapellas.entidades.A02factco;
import com.casapellas.entidades.Credhdr;
import com.casapellas.entidades.CredhdrId;
import com.casapellas.entidades.F55ca014;
import com.casapellas.entidades.Hfactura;
import com.casapellas.entidades.Recibofac;
import com.casapellas.entidades.RecibofacId;
import com.casapellas.entidades.fdc.A02factcoXx;
import com.casapellas.entidades.fdc.ReciboXx;
import com.casapellas.entidades.fdc.RecibofacXx;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.JulianToCalendar;
import com.ibm.icu.text.SimpleDateFormat;

public class CtrlRecibofacXx {
	
	public boolean SiExistenDatosXProcesar(int iCaja, Session sesion) throws Exception{
		boolean existen = false;
		String sql = null;
		Object obj = null;
		Long nRegs = null;
		
		//Session sesion = HibernateUtil.getSessionFactoryFDC().openSession();
						
		try {
			sesion.beginTransaction();
			
			sql = "select count(*) from RecibofacXx a where a.id.caId = :pCaja and a.flwSts = '0'";					
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
	
	public static ReciboXx buscarReciboSql(Session sesionSqlSrv, int numrec,
				String codcomp,int caid, String codsuc, String tiporec){
		ReciboXx rcSql = null;
		
		try {
			rcSql = (ReciboXx)sesionSqlSrv.createCriteria(ReciboXx.class)
					.add(Restrictions.eq("id.numRec", numrec))
					.add(Restrictions.eq("id.codComp", codcomp))
					.add(Restrictions.eq("id.caId", caid))
					.add(Restrictions.eq("id.codSuc", codsuc))
					.add(Restrictions.eq("id.tipoRec", tiporec)) 
					.setMaxResults(1).uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rcSql;
	}
	
	
	@SuppressWarnings("unchecked")
	public void Procesar(Session sesionFDC, Session sesionMCAJA, int iCaja) throws Exception{		
		String sql = null, sTmp;
		List<RecibofacXx> lstRegs = null;
		RecibofacXx regToUpdate = null;
		boolean lMsgInterno = false;
		
		try {
			sql = "From RecibofacXx as a where a.id.caId = :pCaja and a.flwSts = '0' order by a.id.codComp, a.id.codSuc, a.id.caId, " +
			      "a.id.numRec, a.id.tipoRec, a.id.numFac, a.id.partida, a.id.tipoFactura, a.id.codUniNeg";
			
			lstRegs = (ArrayList<RecibofacXx>)sesionFDC.createQuery(sql)
			.setParameter("pCaja", iCaja)
			.list();
			
			if (lstRegs != null && lstRegs.size() > 0) {
				
				Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();	
				ArrayList<A02factco> facturas = (ArrayList<A02factco>)m.get("fc_FacturasPocket");
				
				
				for (RecibofacXx reg : lstRegs) {
					
					Recibofac recibofac = new Recibofac();
					RecibofacId id = new RecibofacId();
					
					id.setCaid(reg.getId().getCaId());
					id.setCodcomp(reg.getId().getCodComp());
					id.setCodsuc(reg.getId().getCodSuc());
					id.setCodunineg(reg.getId().getCodUniNeg());
					id.setNumfac(reg.getId().getNumFac());
					id.setNumrec(reg.getId().getNumRec());
					
					sTmp = reg.getId().getPartida().trim();
					if (sTmp.equals("-")) {
						sTmp = "";
					}					
					id.setPartida(sTmp);
					
					id.setTipofactura(reg.getId().getTipoFactura());
					id.setTiporec(reg.getId().getTipoRec());

					//&& ==== Fecha de la factura y codigo del cliente.
					int ifecha = 0;
					int icodcli = 0;
					
					//&& == si la factura es de contado buscar fecha y cliente entre las facturas contado
					A02factco a = CtrlArqueofactXx.buscarFacturaOriginal(
							reg.getId().getNumFac(), reg.getId().getTipoFactura(),
							reg.getId().getCodUniNeg(), reg.getId().getCodComp(),
							facturas);
					
					if (a != null) {
						icodcli = a.getId().getCodcli().intValue();
						ifecha  = a.getId().getFecha().intValue();
						
					}else{
						//&& == si es credito usar RPDIVJ = fecha factura.
						try{
							ifecha = FechasUtil.DateToJulian( new SimpleDateFormat
									("yyyy-MM-dd").parse(reg.getRpdivj() ) );
						}catch(Exception e){}
						
						//&& === Buscar el recibo original
						ReciboXx rcSql = buscarReciboSql(sesionFDC, reg.getId()
									.getNumRec(), reg.getId().getCodComp(),
									reg.getId().getCaId(), reg.getId().getCodSuc(),
									reg.getId().getTipoRec());
						if(rcSql != null)
							icodcli = rcSql.getCodCli() ;
					}
					id.setFecha(ifecha);
					id.setCodcli(icodcli);
					
					sTmp = reg.getEstado();
					if (sTmp.equals("-")) {
						sTmp = "";
					}					
					recibofac.setEstado(sTmp);
					
					//recibofac.setFecha(fecha) --> No está en la tabla
					recibofac.setId(id);
					//recibofac.setMoneda(reg.getId().get) --> No está en la tabla
					recibofac.setMonto(reg.getMonto());					
					sesionMCAJA.save(recibofac);
					
					sql = "From RecibofacXx as a where a.id.numRec = :pNumRec and a.id.codComp = :pCodComp and a.id.caId = :pCaId and " +
				          "a.id.codSuc = :pCodSuc and a.id.tipoRec = :pTipoRec and a.id.codUniNeg = :pCodUniNeg and a.id.tipoFactura = :pTipoFactura and " +
				          "a.id.numFac = :pNumFac and a.id.partida = :pPartida and a.flwSts = '0'";

				    regToUpdate = (RecibofacXx) sesionFDC.createQuery(sql)
					.setParameter("pNumRec", reg.getId().getNumRec())
					.setParameter("pCodComp", reg.getId().getCodComp())
					.setParameter("pCaId", iCaja)
					.setParameter("pCodSuc", reg.getId().getCodSuc())
					.setParameter("pTipoRec", reg.getId().getTipoRec())
					.setParameter("pCodUniNeg", reg.getId().getCodUniNeg())
					.setParameter("pTipoFactura", reg.getId().getTipoFactura())
					.setParameter("pNumFac", reg.getId().getNumFac())
					.setParameter("pPartida", reg.getId().getPartida())
					.uniqueResult();
					
					if(regToUpdate != null) {
						regToUpdate.setFlwSts("1");
						sesionFDC.update(regToUpdate);
					} else {
						lMsgInterno = true;
						throw new Exception ("Compañía: " + reg.getId().getCodComp()
										 + ", Sucursal: " + reg.getId().getCodSuc()
										     + ", Caja: " + iCaja
									   + ", No. Recibo: " + reg.getId().getNumRec()
									  + ", Tipo Recibo: " + reg.getId().getTipoRec()
									  + ", No. Factura: " + reg.getId().getNumFac()
									 + ", Tipo Factura: " + reg.getId().getTipoFactura());
					}
				}
			}
		} catch(Exception ex){
			if (lMsgInterno) {
			    throw new Exception("Error al procesar detalle de facturas en recibos. " + ex.getMessage());
			} else {
				throw new Exception("Error al procesar detalle de facturas en recibos. " + ex.getMessage());
			}
		} finally {
		}
	}

	public List<Hfactura> getFacturasDeReciboCO(Session sesionFDC, ReciboXx reg, String sSuc2, String sdLocn) throws Exception {
		List<Hfactura> lstFacturas = null;
		List<RecibofacXx> lstRegs = null;
		String sql;
		Hfactura hFac = null;
		
		try {
			sql = "From RecibofacXx as a where a.id.numRec = :pNumRec and a.id.codComp = :pCodComp and a.id.caId = :pCaId and " +
   			      "a.id.codSuc = :pCodSuc and a.id.tipoRec = :pTipoRec";

			lstRegs = (List<RecibofacXx>) sesionFDC.createQuery(sql)
					.setParameter("pNumRec", reg.getId().getNumRec())
					.setParameter("pCodComp", reg.getId().getCodComp())
//					.setParameter("pCaId", reg.getId().getCaid())
					.setParameter("pCaId", reg.getId().getCaId())
					.setParameter("pCodSuc", reg.getId().getCodSuc())
					.setParameter("pTipoRec", reg.getId().getTipoRec())					
					.list();
			
			if (lstRegs != null && lstRegs.size() > 0) {
				lstFacturas = new ArrayList <Hfactura>();
				for (RecibofacXx regFac : lstRegs) {
					hFac = getObjetoFacturaCO(sesionFDC, regFac, sSuc2, sdLocn);
					lstFacturas.add(hFac);
				}
			} else {
				throw new Exception ("Compañía: " + reg.getId().getCodComp()
								 + ", Sucursal: " + reg.getId().getCodSuc()
//								 	 + ", Caja: " + reg.getId().getCaid()
							 	  	 + ", Caja: " + reg.getId().getCaId()
							   + ", No. Recibo: " + reg.getId().getNumRec()
					          + ", Tipo Recibo: " + reg.getId().getTipoRec());
			}
			
		} catch(Exception ex){
			throw new Exception("Error al obtener facturas de recibo. " + ex.getMessage());
		} finally {}
		return lstFacturas;
	}
	
	private Hfactura getObjetoFacturaCO(Session sesionFDC, RecibofacXx reg, String sSuc2, String sdLocn) throws Exception {
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		Hfactura hFac = new Hfactura();

		String sql = null;		
		A02factcoXx regA02factcoXx = null;

		try {
			hFac.setCodcomp(reg.getId().getCodComp());
			hFac.setCodsuc(reg.getId().getCodSuc().substring(3));
			hFac.setCodunineg(reg.getId().getCodUniNeg());
			hFac.setNofactura(reg.getId().getNumFac());
			hFac.setTipofactura(reg.getId().getTipoFactura());
			hFac.setPartida(reg.getId().getPartida());

			/* Obtener el código del cliente */
			try {
				sql = "Select a.codCli From ReciboXx as a where a.id.numRec = :pNumRec and a.id.codComp = :pCodComp and a.id.caId = :pCaId and " + 
					  "a.id.codSuc = :pCodSuc and a.id.tipoRec = :pTipoRec";
				Object obj = (Object) sesionFDC.createQuery(sql)
				.setParameter("pNumRec", reg.getId().getNumRec())
				.setParameter("pCodComp", reg.getId().getCodComp())
				.setParameter("pCaId", reg.getId().getCaId())
				.setParameter("pCodSuc", reg.getId().getCodSuc())
				.setParameter("pTipoRec", reg.getId().getTipoRec())
				.uniqueResult();
				if (obj != null) {					
					hFac.setCodcli(Integer.parseInt(obj.toString()));
				} else {
					throw new Exception("No Encuentro Cliente de recibo #: " + reg.getId().getNumRec());
				}								
			} catch(Exception ex){
				throw new Exception("Error al procesar recibo #: " + reg.getId().getNumRec());
			} finally {}
			/* Fin: Obtener el código del cliente */

			try {
				sql = "From A02factcoXx as a where a.id.sucursal = :pSucursal and a.id.zona = :pZona and a.id.ruta = :pRuta and " +
			          "a.id.codCli = :pCliente and a.id.noFactura = :pNoFactura and a.id.tipoFactura = :pTipoFactura and " + 
			          "a.id.codUniNeg = :pCodUniNeg and a.id.codSuc = :pCodSuc";

				regA02factcoXx = (A02factcoXx) sesionFDC.createQuery(sql)
				.setParameter("pSucursal", sSuc2)
				.setParameter("pZona", sdLocn)
				.setParameter("pRuta", "_")
				.setParameter("pCliente", hFac.getCodcli())
				.setParameter("pNoFactura", reg.getId().getNumFac())
				.setParameter("pTipoFactura", reg.getId().getTipoFactura())
				.setParameter("pCodUniNeg", reg.getId().getCodUniNeg())
				.setParameter("pCodSuc", reg.getId().getCodSuc())
				.uniqueResult();

				if (regA02factcoXx == null) {					
					sql = "From A02factcoXx as a where a.id.sucursal = :pSucursal and a.id.zona = :pZona and a.id.ruta = :pRuta and " +
				          "a.id.codCli = :pCliente and a.id.noFactura = :pNoFactura and a.id.tipoFactura = :pTipoFactura and " + 
				          "a.id.codUniNeg = :pCodUniNeg and a.id.codSuc = :pCodSuc";

					regA02factcoXx = (A02factcoXx) sesionFDC.createQuery(sql)
					.setParameter("pSucursal", sSuc2)
					.setParameter("pZona", "_")
					.setParameter("pRuta", sdLocn)
					.setParameter("pCliente", hFac.getCodcli())
					.setParameter("pNoFactura", reg.getId().getNumFac())
					.setParameter("pTipoFactura", reg.getId().getTipoFactura())
					.setParameter("pCodUniNeg", reg.getId().getCodUniNeg())
					.setParameter("pCodSuc", reg.getId().getCodSuc())
					.uniqueResult();
				}

				if (regA02factcoXx != null) {
					hFac.setSubtotal(regA02factcoXx.getSubTotal()/100.0);
					hFac.setTotal(regA02factcoXx.getTotal()/100.0);
					hFac.setCpendiente(regA02factcoXx.getTotal()/100.0);
					hFac.setTasa(regA02factcoXx.getTasa());
					hFac.setUnineg(regA02factcoXx.getUniNeg());
					hFac.setEquiv(String.valueOf(regA02factcoXx.getTotal()/100.0).trim());
					hFac.setEstado(regA02factcoXx.getEstado());
					
					//JulianToCalendar fecha = new JulianToCalendar(regA02factcoXx.getHora());
					JulianToCalendar fecha = new JulianToCalendar(regA02factcoXx.getFecha());
					hFac.setFecha(fecha.toString());
					fecha = new JulianToCalendar(regA02factcoXx.getFechaGrab());
					hFac.setFechagrab(fecha.toString());
					hFac.setHechopor(regA02factcoXx.getHechoPor());
					hFac.setIva(regA02factcoXx.getTotal()/100.0 - regA02factcoXx.getSubTotal()/100.0);
					
					CompaniaCtrl cCtrl = new CompaniaCtrl();
					hFac.setMoneda(cCtrl.sacarMonedaBase((F55ca014[])m.get("cont_f55ca014"), hFac.getCodcomp()));
					
					hFac.setNomcli(regA02factcoXx.getNomCli());
					hFac.setNomcomp(regA02factcoXx.getNomComp());
					hFac.setNomsuc(regA02factcoXx.getNomSuc());
					hFac.setTipopago(regA02factcoXx.getTipoPago());
					if (regA02factcoXx.getTipoPago().equals("001".toString())) {
						hFac.setPago("Contado".toString());						
					} 
					hFac.setPantalla(regA02factcoXx.getPantalla());			

				} else {
					throw new Exception("No Encuentro datos de facturas del recibo #: " + reg.getId().getNumRec());
				}
			} catch(Exception ex){
				throw new Exception("Problemas al buscar datos de facturas del recibo #: " + reg.getId().getNumRec());
			} finally {}
			/* Fin: Obtener datos de factura */				
		} catch (Exception e) {
			throw new Exception("Problemas al buscar datos de facturas del recibo #: " + reg.getId().getNumRec());
		}
		return hFac;
	}
	
	public List<Credhdr> getFacturasDeReciboCR(Session sesionFDC, ReciboXx reg, String sSuc2, String sdLocn) throws Exception {
		List<Credhdr> lstFacturas = null;
		List<RecibofacXx> lstRegs = null;
		String sql;
		Credhdr hFac = null;
		CredhdrId idHFac = null;
		
		try {
			sql = "From RecibofacXx as a where a.id.numRec = :pNumRec and a.id.codComp = :pCodComp and a.id.caId = :pCaId and " +
   			      "a.id.codSuc = :pCodSuc and a.id.tipoRec = :pTipoRec";

			lstRegs = (List<RecibofacXx>) sesionFDC.createQuery(sql)
					.setParameter("pNumRec", reg.getId().getNumRec())
					.setParameter("pCodComp", reg.getId().getCodComp())
//					.setParameter("pCaId", reg.getId().getCaid())
					.setParameter("pCaId", reg.getId().getCaId())
					.setParameter("pCodSuc", reg.getId().getCodSuc())
					.setParameter("pTipoRec", reg.getId().getTipoRec())					
					.list();
			
			if (lstRegs != null && lstRegs.size() > 0) {
				lstFacturas = new ArrayList <Credhdr>();
				for (RecibofacXx regFac : lstRegs) {
					hFac = new Credhdr();
					idHFac = new CredhdrId();
					idHFac.setCodcomp(regFac.getId().getCodComp());
					idHFac.setCodunineg(regFac.getId().getCodUniNeg());
					idHFac.setCodsuc(regFac.getId().getCodSuc());
					idHFac.setCodcli(reg.getCodCli());
					idHFac.setNomcli(regFac.getNombre());
					idHFac.setMoneda("COR");
					idHFac.setNofactura(regFac.getId().getNumFac());
					idHFac.setPartida(regFac.getId().getPartida());
					idHFac.setTipofactura(regFac.getId().getTipoFactura());
					idHFac.setTipopago("002");
					idHFac.setCpendiente(regFac.getMontoIni());
					idHFac.setCompenslm("   ");
					
					FechasUtil fechasutil = new FechasUtil();					
					idHFac.setRpddj(fechasutil.DateToJulian(fechasutil.getDateFromString(regFac.getRpddj(), "yyyy-MM-dd")));
					idHFac.setRpdivj(fechasutil.DateToJulian(fechasutil.getDateFromString(regFac.getRpdivj(), "yyyy-MM-dd")));					
					
					hFac.setNofactura(regFac.getId().getNumFac());
					hFac.setTipofactura(regFac.getId().getTipoFactura());
					hFac.setPartida(regFac.getId().getPartida());					
					hFac.setMoneda("COR");
					hFac.setMontoAplicar(regFac.getMonto());
					
					hFac.setId(idHFac);
					//hFac = getObjetoFacturaCR(sesionFDC, regFac, sSuc2, sdLocn);
					lstFacturas.add(hFac);
				}
			} else {
				throw new Exception ("Compañía: " + reg.getId().getCodComp()
								 + ", Sucursal: " + reg.getId().getCodSuc()
//								 	 + ", Caja: " + reg.getId().getCaid()
								  + ", Caja: " + reg.getId().getCaId()
							   + ", No. Recibo: " + reg.getId().getNumRec()
					          + ", Tipo Recibo: " + reg.getId().getTipoRec());
			}
			
		} catch(Exception ex){
			throw new Exception("Error al obtener facturas de recibo. " + ex.getMessage());
		} finally {}
		return lstFacturas;
	}
	/* No se usa por el momento */
	private Credhdr getObjetoFacturaCR(Session sesionFDC, RecibofacXx reg, String sSuc2, String sdLocn) throws Exception {
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		Credhdr hFac = new Credhdr();
		CredhdrId idHFac = new CredhdrId();
		
		String sql = null;		
		A02factcoXx regA02factcoXx = null;

		try {
			idHFac.setCodcomp(reg.getId().getCodComp());
			idHFac.setCodunineg(reg.getId().getCodUniNeg());
			idHFac.setCodsuc(reg.getId().getCodSuc().substring(3));
			hFac.setNofactura(reg.getId().getNumFac());
			hFac.setTipofactura(reg.getId().getTipoFactura());
			hFac.setPartida(reg.getId().getPartida());

			/* Obtener el código del cliente */
			try {
				sql = "Select a.codCli From ReciboXx as a where a.id.numRec = :pNumRec and a.id.codComp = :pCodComp and a.id.caId = :pCaId and " + 
					  "a.id.codSuc = :pCodSuc and a.id.tipoRec = :pTipoRec";
				Object obj = (Object) sesionFDC.createQuery(sql)
				.setParameter("pNumRec", reg.getId().getNumRec())
				.setParameter("pCodComp", reg.getId().getCodComp())
				.setParameter("pCaId", reg.getId().getCaId())
				.setParameter("pCodSuc", reg.getId().getCodSuc())
				.setParameter("pTipoRec", reg.getId().getTipoRec())
				.uniqueResult();
				if (obj != null) {
					idHFac.setCodcli(Integer.parseInt(obj.toString()));
					hFac.setId(idHFac);
				} else {
					throw new Exception("No Encuentro Cliente de recibo #: " + reg.getId().getNumRec());
				}								
			} catch(Exception ex){
				throw new Exception("Error al procesar recibo #: " + reg.getId().getNumRec());
			} finally {}
			/* Fin: Obtener el código del cliente */

			try {
				sql = "From A02factcoXx as a where a.id.sucursal = :pSucursal and a.id.zona = :pZona and a.id.ruta = :pRuta and " +
			          "a.id.codCli = :pCliente and a.id.noFactura = :pNoFactura and a.id.tipoFactura = :pTipoFactura and " + 
			          "a.id.codUniNeg = :pCodUniNeg and a.id.codSuc = :pCodSuc";

				regA02factcoXx = (A02factcoXx) sesionFDC.createQuery(sql)
				.setParameter("pSucursal", sSuc2)
				.setParameter("pZona", sdLocn)
				.setParameter("pRuta", "_")
				.setParameter("pCliente", idHFac.getCodcli())
				.setParameter("pNoFactura", reg.getId().getNumFac())
				.setParameter("pTipoFactura", reg.getId().getTipoFactura())
				.setParameter("pCodUniNeg", reg.getId().getCodUniNeg())
				.setParameter("pCodSuc", reg.getId().getCodSuc())
				.uniqueResult();

				if (regA02factcoXx == null) {					
					sql = "From A02factcoXx as a where a.id.sucursal = :pSucursal and a.id.zona = :pZona and a.id.ruta = :pRuta and " +
				          "a.id.codCli = :pCliente and a.id.noFactura = :pNoFactura and a.id.tipoFactura = :pTipoFactura and " + 
				          "a.id.codUniNeg = :pCodUniNeg and a.id.codSuc = :pCodSuc";

					regA02factcoXx = (A02factcoXx) sesionFDC.createQuery(sql)
					.setParameter("pSucursal", sSuc2)
					.setParameter("pZona", "_")
					.setParameter("pRuta", sdLocn)
					.setParameter("pCliente", idHFac.getCodcli())
					.setParameter("pNoFactura", reg.getId().getNumFac())
					.setParameter("pTipoFactura", reg.getId().getTipoFactura())
					.setParameter("pCodUniNeg", reg.getId().getCodUniNeg())
					.setParameter("pCodSuc", reg.getId().getCodSuc())
					.uniqueResult();
				}

				if (regA02factcoXx != null) {
					//hFac.setSubtotal(regA02factcoXx.getSubTotal()/100.0);
					//hFac.setTotal(regA02factcoXx.getTotal()/100.0);
					//hFac.setTasa(regA02factcoXx.getTasa());
					//hFac.setUnineg(regA02factcoXx.getUniNeg());
					//hFac.setEquiv(String.valueOf(regA02factcoXx.getTotal()/100.0).trim());
					//hFac.setEstado(regA02factcoXx.getEstado());
					
					//JulianToCalendar fecha = new JulianToCalendar(regA02factcoXx.getHora());
					//fecha = new JulianToCalendar(regA02factcoXx.getFecha());
					//hFac.setFecha(fecha.toString());
					//fecha = new JulianToCalendar(regA02factcoXx.getFechaGrab());
					//hFac.setFechagrab(fecha.toString());
					//hFac.setHechopor(regA02factcoXx.getHechoPor());
					//hFac.setIva(regA02factcoXx.getTotal()/100.0 - regA02factcoXx.getSubTotal()/100.0);
					
					CompaniaCtrl cCtrl = new CompaniaCtrl();
					hFac.setMoneda(cCtrl.sacarMonedaBase((F55ca014[])m.get("cont_f55ca014"), idHFac.getCodcomp()));
					
					//hFac.setNomcli(regA02factcoXx.getNomCli());
					//hFac.setNomcomp(regA02factcoXx.getNomComp());
					//hFac.setNomsuc(regA02factcoXx.getNomSuc());
					//hFac.setTipopago(regA02factcoXx.getTipoPago());
					if (regA02factcoXx.getTipoPago().equals("002".toString())) {						
						//hFac.setPago("Crédito".toString());						
					} 
					//hFac.setPantalla(regA02factcoXx.getPantalla());			
				} else {
					throw new Exception("No Encuentro datos de facturas del recibo #: " + reg.getId().getNumRec());
				}
			} catch(Exception ex){
				throw new Exception("Problemas al buscar datos de facturas del recibo #: " + reg.getId().getNumRec());
			} finally {}
			/* Fin: Obtener datos de factura */				
		} catch (Exception e) {
			throw new Exception("Problemas al buscar datos de facturas del recibo #: " + reg.getId().getNumRec());
		}
		return hFac;
	}

}
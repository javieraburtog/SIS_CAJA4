package com.casapellas.controles.fdc;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.hibernate.Session;

import com.casapellas.entidades.Arqueo;
import com.casapellas.entidades.ArqueoId;
import com.casapellas.entidades.fdc.ArqueoXx;
import com.casapellas.entidades.fdc.ArqueoXxId;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.PropertiesSystem;

public class CtrlArqueoXx {
	
	public boolean SiExistenDatosXProcesar(int iCaja, Session sesion) throws Exception{
		boolean existen = false;
		String sql = null;
		Object obj = null;
		Long nRegs = null;
		
		//Session sesion = HibernateUtil.getSessionFactoryFDC().openSession();
						
		try {
			sesion.beginTransaction();
			
			sql = "select count(*) from ArqueoXx a where a.id.caId = :pCaja and a.flwSts = '0'";					
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
		List<ArqueoXx> lstRegs = null;
		ArqueoXx regToUpdate = null;
							
		try{
			sql = "From ArqueoXx as a where a.id.caId = :pCaja and a.flwSts = '0'";
			lstRegs = (ArrayList<ArqueoXx>)sesionFDC.createQuery(sql)
			.setParameter("pCaja", iCaja)
			.list();
			if(lstRegs != null && lstRegs.size() > 0) {
				for(ArqueoXx reg : lstRegs) {
					if (!((reg.getTingreso().compareTo(BigDecimal.ZERO) == 0) && (reg.getTegresos().compareTo(BigDecimal.ZERO) == 0) && 
						(reg.getNetoRec().compareTo(BigDecimal.ZERO) == 0)  && (reg.getMinimo().compareTo(BigDecimal.ZERO) == 0) && 
						(reg.getDsugerido().compareTo(BigDecimal.ZERO) == 0) && (reg.getEfectCaja().compareTo(BigDecimal.ZERO) == 0) && 
						(reg.getSf().compareTo(BigDecimal.ZERO) == 0) && (reg.getDfinal().compareTo(BigDecimal.ZERO) == 0) &&
						(reg.getTpagos().compareTo(BigDecimal.ZERO) == 0)))  {
						
						Arqueo arqueo = new Arqueo();
						ArqueoId id = new ArqueoId();
						
						id.setCaid(reg.getId().getCaId());
						id.setCodcomp(reg.getId().getCodComp());
						id.setCodsuc(reg.getId().getCodSuc());
						
						 
						java.util.Date date = FechasUtil.getDateFromString(reg.getId().getFecha(), "yyyy-MM-dd");
						
						id.setFecha(date);
						
						date = FechasUtil.getDateFromString(reg.getHora(), "hh:mm:ss");					
						id.setHora(date);
						id.setNoarqueo(reg.getId().getNoArqueo());
	
						arqueo.setCodcajero(reg.getCodCajero());
						arqueo.setCoduser(reg.getCodUser());
						arqueo.setDfinal(reg.getDfinal());
						arqueo.setDsugerido(reg.getDsugerido());
						arqueo.setEfectcaja(reg.getEfectCaja());
						arqueo.setEstado("P");
						
						date = FechasUtil.getDateFromString(reg.getFechaMod(), "yyyy-MM-dd");					
						arqueo.setFechamod(date);
	
						date = FechasUtil.getDateFromString(reg.getHoraMod(), "hh:mm:ss");					
						arqueo.setHoramod(date);
						
						arqueo.setId(id);
						arqueo.setMinimo(reg.getMinimo());
						arqueo.setMoneda(reg.getMoneda());
						arqueo.setMotivo(reg.getMotivo());
						arqueo.setNetorec(reg.getNetoRec());
						
						if (reg.getReferDep() == null) {
							sTmp = "";
						} else {
							sTmp = reg.getReferDep();
							if (sTmp.equals("-")) {
								sTmp = "";
							}
						}
						arqueo.setReferdep(sTmp);
						
						arqueo.setSf(reg.getSf());
						arqueo.setTegresos(reg.getTegresos());
						arqueo.setTingreso(reg.getTingreso());
						arqueo.setTpagos(reg.getTpagos());
						
						arqueo.setDepctatran( 0 );
						arqueo.setReferencenumber( 0 ) ;
						
						try {
							
							boolean preconciliacion = com.casapellas.controles.BancoCtrl.ingresoBajoPreconciliacion( 0, id.getCaid(), id.getCodcomp() );
							arqueo.setDepctatran( (preconciliacion ? 1 : 0) );
							
							int referencenumber = !arqueo.getReferdep().trim().isEmpty() && arqueo.getReferdep().trim().matches(PropertiesSystem.REGEXP_8DIGTS)  ?
									Integer.parseInt(arqueo.getReferdep().trim()) : 0 ;
							
							arqueo.setReferencenumber( referencenumber );
							
						} catch (Exception e) {
							e.printStackTrace();
							arqueo.setDepctatran(0);
							arqueo.setReferencenumber( 0 ) ;
						}
						
						try {
							arqueo.setCodusermod( reg.getCodCajero() );
							arqueo.setDatapcinfo( PropertiesSystem.getDataFromPcClient() );
						} catch (Exception e) {
							arqueo.setCodusermod( 0 );
							arqueo.setDatapcinfo( "" );
						}
						
						sesionMCAJA.save(arqueo);
					}
	
					sql = "From ArqueoXx as a where a.id.codComp = :pCodComp and a.id.codSuc = :pCodSuc and a.id.noArqueo = :pNoArqueo and " +
				          "a.id.caId = :pCaId and a.id.fecha = :pFecha and a.flwSts = '0'";

				    regToUpdate = (ArqueoXx) sesionFDC.createQuery(sql)
					.setParameter("pCodComp", reg.getId().getCodComp())
					.setParameter("pCodSuc", reg.getId().getCodSuc())
					.setParameter("pNoArqueo", reg.getId().getNoArqueo())
					.setParameter("pCaId", iCaja)
					.setParameter("pFecha", reg.getId().getFecha())
					.uniqueResult();
					
					if(regToUpdate != null) {
//						regToUpdate.setFlwSts('1');
						regToUpdate.setFlwSts("1");
						sesionFDC.update(regToUpdate);
					} else {
						throw new Exception ("Compañía: " + reg.getId().getCodComp()
									     + ", Sucursal: " + reg.getId().getCodSuc()
										     + ", Caja: " + iCaja
									   + ", No. Arqueo: " + reg.getId().getNoArqueo());
					}					
				}
			}						
		} catch(Exception ex){
			throw new Exception("Error al procesar información de arqueo. " + ex.getMessage());
		} finally {
		}
	}
	
	public void actualizar_hora_en_arqueo(Session sesionFDC, int iCaja) throws Exception {
		String sql = null;		
		List<ArqueoXx> lstRegs = null;
		ArqueoXx regToUpdate = null;
		int iCont;
		String sHora;

		try {
			sql = "From ArqueoXx as a where a.id.caId = :pCaja";// and a.flwSts = '0'";
			lstRegs = (ArrayList<ArqueoXx>)sesionFDC.createQuery(sql)
			.setParameter("pCaja", iCaja)
			.list();
			if(lstRegs != null && lstRegs.size() > 0) {
				iCont = 0;
				int hours = 18, secs = 0;				
				for(ArqueoXx reg : lstRegs) {					
					int year = Integer.parseInt(reg.getId().getFecha().substring(0, 4));
				    int month = Integer.parseInt(reg.getId().getFecha().substring(5, 7));
				    int day = Integer.parseInt(reg.getId().getFecha().substring(8, 10));				    
				    int mins = iCont * 15;				    
					Calendar calendar = new GregorianCalendar(year, month, day, hours, mins, secs);
					
					SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
					sHora = sdf.format(calendar.getTime());
					
					sql = "From ArqueoXx as a where a.id.codComp = :pCodComp and a.id.codSuc = :pCodSuc and a.id.noArqueo = :pNoArqueo and " +
 			              "a.id.caId = :pCaId and a.id.fecha = :pFecha";// and a.flwSts = '0'";

				    regToUpdate = (ArqueoXx) sesionFDC.createQuery(sql)
					.setParameter("pCodComp", reg.getId().getCodComp())
					.setParameter("pCodSuc", reg.getId().getCodSuc())
					.setParameter("pNoArqueo", reg.getId().getNoArqueo())
					.setParameter("pCaId", iCaja)
					.setParameter("pFecha", reg.getId().getFecha())
					.uniqueResult();

				    if (regToUpdate != null) {
				    	regToUpdate.setHora(sHora);
				    	regToUpdate.setHoraMod(sHora);
				    	sesionFDC.update(regToUpdate);				    	
				    } else {
				    	throw new Exception ("Compañía: " + reg.getId().getCodComp()
				    				     + ", Sucursal: " + reg.getId().getCodSuc()
				    				       + ", Arqueo: " + reg.getId().getNoArqueo()				    				       
									         + ", Caja: " + iCaja
								            + ", Fecha: " + reg.getId().getFecha());
				    }
				    ++iCont;
				}
			}						
		} catch(Exception ex){
			throw new Exception("Error al actualizar hora de arqueo. " + ex.getMessage());
		} finally {}
	}

	public String get_hora_de_arqueo(Session sesionFDC, String sCia, String sSucursal,	int iArqueo, int iCaja, String sFecha) throws Exception {
		String sql = null;		
		List<ArqueoXx> lstRegs = null;
		ArqueoXx regArqueoXx = null;
		int iCont;
		String sHora = "";

		try {
			sql = "From ArqueoXx as a where a.id.codComp = :pCodComp and a.id.codSuc = :pCodSuc and a.id.noArqueo = :pNoArqueo and " +
	              "a.id.caId = :pCaId and a.id.fecha = :pFecha";// and a.flwSts = '0'";
	
			regArqueoXx = (ArqueoXx) sesionFDC.createQuery(sql)
			.setParameter("pCodComp", sCia)
			.setParameter("pCodSuc", sSucursal)
			.setParameter("pNoArqueo", iArqueo)
			.setParameter("pCaId", iCaja)
			.setParameter("pFecha", sFecha)
			.uniqueResult();
	
		    if (regArqueoXx != null) {
		    	sHora = regArqueoXx.getHora();
		    } else {
		    	throw new Exception ("No Existe Información de Arqueo. Compañía: " + sCia
										    				      + ", Sucursal: " + sSucursal
										    				        + ", Arqueo: " + iArqueo				    				       
															          + ", Caja: " + iCaja
														             + ", Fecha: " + sFecha);
		    }
		} catch(Exception ex){
			throw new Exception("Error al actualizar hora de arqueo. " + ex.getMessage());
		} finally {}
		return sHora;
	}
}

package com.casapellas.controles.fdc;

import java.sql.Connection;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.casapellas.controles.ReciboCtrl;
import com.casapellas.entidades.Numcaja;
import com.casapellas.entidades.NumcajaId;
import com.casapellas.entidades.fdc.ControlesXx;

public class CtrlControlesXx {
	
	public boolean SiExistenDatosXProcesar(String sSucursal, String sZona, Session sesion) throws Exception{
		boolean existen = false;
		String sql = null;
		Object obj = null;
		Long nRegs = null;
		
		//Session sesion = HibernateUtil.getSessionFactoryFDC().openSession();
						
		try {
			sesion.beginTransaction();
			
			//Zona
			sql = "select count(*) from ControlesXx a where a.id.sucursal = :pSuc and a.id.zona = :pZonaORuta and a.id.noLiquid = 0 and a.flwSts = '0'";					
			obj = sesion.createQuery(sql)
				  .setParameter("pSuc", sSucursal)
				  .setParameter("pZonaORuta", sZona)
				  .uniqueResult();			
			if (obj != null) {
				nRegs = (Long)obj;
			}
			
			//Ruta
			sql = "select count(*) from ControlesXx a where a.id.sucursal = :pSuc and a.id.ruta = :pZonaORuta and a.id.noLiquid = 0 and a.flwSts = '0'";					
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
	
	public void Procesar(Connection cn, Session sesionFDC, Session sesionMCAJA, String sUsuarioModificacion, String sCompania, 
						 String sSucursal, String sdLocn, int iCaja) throws Exception{
		ReciboCtrl conCtrl = new ReciboCtrl();
		String sql = null;		
		ControlesXx ctrlsMSQLServer = null;
		Object obj = null;
		boolean lFlag = false;

		try {
			sql = "From ControlesXx a where a.id.sucursal = :pSuc and a.id.zona = :pZonaORuta and a.id.noLiquid = 0 and a.flwSts = '0'";
			obj = (ControlesXx) sesionFDC.createQuery(sql)
			.setParameter("pSuc", sSucursal.substring(3))
			.setParameter("pZonaORuta", sdLocn)
			.uniqueResult();
			
			if (obj == null) {
				sql = "From ControlesXx a where a.id.sucursal = :pSuc and a.id.ruta = :pZonaORuta and a.id.noLiquid = 0 and a.flwSts = '0'";
				obj = (ControlesXx) sesionFDC.createQuery(sql)
				.setParameter("pSuc", sSucursal.substring(3))
				.setParameter("pZonaORuta", sdLocn)
				.uniqueResult();
			}
			
			if (obj != null) {
				ctrlsMSQLServer = (ControlesXx) obj;
				
				//Actualizar el número de recibo.
				lFlag = conCtrl.actualizarNumeroRecibo(cn, iCaja, sCompania, ctrlsMSQLServer.getNoPagosF());
				
				if (lFlag) {
					Numcaja numcaja = new Numcaja();
					//NumcajaId id = new NumcajaId();
					
					sql = "From Numcaja as a where a.id.codnumeracion = :pCodigo and a.id.caid = :pCaId and a.id.codcomp = :pCodComp and " +
			              "a.id.codsuc = :pCodSuc";				
					obj = sesionMCAJA.createQuery(sql)
					.setParameter("pCodigo", "FICHACV")
					.setParameter("pCaId", iCaja)
					.setParameter("pCodComp", sCompania)
					.setParameter("pCodSuc", sSucursal)
					.uniqueResult();
					
					if(obj != null) {
						numcaja = (Numcaja) obj;
						numcaja.setNosiguiente(ctrlsMSQLServer.getNoFichaF());
						sesionMCAJA.update(numcaja);
					} else {
						throw new Exception ("Error al actualizar secuencia de ficha. Compañía: " + sCompania
															                     + ", Sucursal: " + sSucursal
	 														                         + ", Caja: " + iCaja);
					}
					
					obj = sesionMCAJA.createQuery(sql)
					.setParameter("pCodigo", "NARQUEO")
					.setParameter("pCaId", iCaja)
					.setParameter("pCodComp", sCompania)
					.setParameter("pCodSuc", sSucursal)
					.uniqueResult();
					
					if(obj != null) {
						numcaja = (Numcaja) obj;
						numcaja.setNosiguiente(ctrlsMSQLServer.getNoArqueoF());
						sesionMCAJA.update(numcaja);
					} else {
						throw new Exception ("Compañía: " + sCompania
										 + ", Sucursal: " + sSucursal
	 										 + ", Caja: " + iCaja);
					}
					
//					ctrlsMSQLServer.setFlwSts('1');
					ctrlsMSQLServer.setFlwSts("1");
					sesionFDC.update(ctrlsMSQLServer);
				} else {
					throw new Exception ("No. de Recibo No Actualizado. Compañía: " + sCompania
										                           + ", Sucursal: " + sSucursal
										                               + ", Caja: " + iCaja);
				}
			} else {
				throw new Exception ("Compañía: " + sCompania
	                             + ", Sucursal: " + sSucursal
	                                 + ", Caja: " + iCaja);
			}
			if (lFlag == false){
				throw new Exception ("Compañía: " + sCompania
                        		 + ", Sucursal: " + sSucursal
                        		     + ", Caja: " + iCaja);
			}
		} catch(Exception ex){
			throw new Exception("Error al procesar secuencias. " + ex.getMessage());
		} finally {}		
	}
}

package com.casapellas.controles.tmp;
/**
 * CASA PELLAS S.A.
 * Creado por.........: Juan Carlos Ñamendi Pineda
 * Fecha de Creación..: 28/02/2009
 * Última modificación: 09/03/2011
 * Modificado por.....:	Carlos Manuel Hernández Morrison.
 * 
 */
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.casapellas.entidades.Bien;
import com.casapellas.entidades.BienId;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.util.LogCajaService;

public class BienCtrl {
	
	
/******************************************************************************/	
/** 	Guarda un registro de BIEN asociado a un recibo de primas o reservas 
 ***************/
	
	public boolean ingresarBien(Session sesion, Transaction trans, int numrec,
			int caid, String codsuc, String codcomp, String tipoprod,
			String marca, String modelo, String noitem, String codsucdoc) {
		boolean bHecho = true;
		Transaction tx = null;
		Bien bien;
		BienId bId;
		boolean bNuevaSesionENS = false;

		try {
			bId = new BienId();
			bId.setCaid(caid);
			bId.setCodcomp(codcomp);
			bId.setCodsuc(codsuc);
			bId.setNumrec(numrec);

			bien = new Bien();
			bien.setId(bId);
			bien.setMarca(marca);
			bien.setModelo(modelo);
			bien.setNoitem(noitem);
			bien.setTipoprod(tipoprod);
			bien.setSucursalDocumento(codsucdoc);

			if (trans == null) {
				bNuevaSesionENS = true;
				sesion = HibernateUtilPruebaCn.currentSession();

				if (sesion.getTransaction().isActive())
					tx = sesion.getTransaction();
				else {
					tx = sesion.beginTransaction();
					bNuevaSesionENS = true;
				}
			}
			sesion.save(bien);

		} catch (Exception error) {
			bHecho = false; 
			error.printStackTrace();
		} finally {
			if (trans == null) {
				try {
					if (bNuevaSesionENS) {
						tx.commit();
						HibernateUtilPruebaCn.closeSession(sesion);
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		return bHecho;
	}
	
	//**********************************************************
	//----------------------------------------------------------
	//++++++++++++++++++      INICIO     +++++++++++++++++++++++
	//----------------------------------------------------------
	//**********************************************************
	//Cambio hecho por lfonseca 2018-04-30
	public boolean ingresarBien2(Session sesion, Transaction trans, int numrec,
			int caid, String codsuc, String codcomp, String tipoprod,
			String marca, String modelo, String noitem, String codsucdoc) {
		boolean bHecho = true;
		Bien bien;
		BienId bId;

		try {
			bId = new BienId();
			bId.setCaid(caid);
			bId.setCodcomp(codcomp);
			bId.setCodsuc(codsuc);
			bId.setNumrec(numrec);

			bien = new Bien();
			bien.setId(bId);
			bien.setMarca(marca);
			bien.setModelo(modelo);
			bien.setNoitem(noitem);
			bien.setTipoprod(tipoprod);
			bien.setSucursalDocumento(codsucdoc);

			sesion.save(bien);
			
			LogCajaService.CreateLog("ingresarBien2", "HQRY", bien);

		} catch (Exception error) {
			LogCajaService.CreateLog("ingresarBien2", "ERR", error.getMessage());
			bHecho = false; 
			error.printStackTrace();
		} 
		
		return bHecho;
	}
	//**********************************************************
	//----------------------------------------------------------
	//+++++++++++++++++++       FIN     ++++++++++++++++++++++++
	//----------------------------------------------------------
	//**********************************************************
	
/**************REGISTRA EL BIEN ASOCIADO A UN RECIBO DE PRIMA O RESERVA*******************************/
	public boolean registrarBien(int iNumRec,int iNumRecm, String sCodComp,String sMarca,String sModelo,String sNoItem, String sTipoProd,int iCaId, String sCodSuc,Session s){
		boolean registrado = false;
		
		try{
			
			Bien bien = new Bien();
			BienId bienId = new BienId();
			
			bienId.setNumrec(iNumRec);
//			bienId.setNoitem(sNoItem.trim());
			
			bienId.setCodcomp(sCodComp);
			bienId.setCaid(iCaId);
			bienId.setCodsuc(sCodSuc);
			
			bien.setId(bienId);
			bien.setMarca(sMarca.trim());
			bien.setModelo(sModelo.trim());
			bien.setNoitem(sNoItem.trim());
//			bien.setNumrecm(iNumRecm);
			bien.setTipoprod(sTipoProd.trim());
			
			s.save(bien);
			
			registrado = true;
			
		}catch(Exception ex){
		}
		return registrado;
	}
/*****************************************************************************************************/
	
	
}

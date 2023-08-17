package com.casapellas.controles;
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


public class BienCtrl {
	
	
/******************************************************************************/	
/** 	Guarda un registro de BIEN asociado a un recibo de primas o reservas 
 ***************/
	public boolean ingresarBien(Session sesion, Transaction trans,int numrec, int caid,String codsuc,
			 					String codcomp, String tipoprod,String marca,String modelo,String noitem){
		boolean bHecho = true;
		Transaction tx= null;
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
			
	
			sesion.save(bien);
			
		} catch (Exception error) {
			bHecho = false;
//			System.out.println("Error en  BienCtrl.ingresarBien" + error);
			error.printStackTrace();
		} 
		return bHecho;
	}
	
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
//			System.out.print("Se capturo una excepcion en BienCtrl.registrarBien: " + ex);
			ex.printStackTrace();
		}
		return registrado;
	}
/*****************************************************************************************************/
	
	
}

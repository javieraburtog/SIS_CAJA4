package com.casapellas.reportes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.casapellas.entidades.Recibo;
import com.casapellas.entidades.Recibodet;
import com.casapellas.entidades.Recibofac;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.util.Divisas;

public class ReporteCtrl {
/*********************FORMATEAR HEADER DEL RECIBO***************************************************************/
	public ReciboR formatRecibo(int iNumRec,String sCodComp, String sNomComp, String sNomSuc, String sTipoRec,String sLogin){
		Divisas divisas = new Divisas();
		ReciboR recibo = null;
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat dfHora = new SimpleDateFormat("hh:mm:ss");
		String sql = null;
		boolean automatico = false;
		try{
			tx = session.beginTransaction();
			if(sTipoRec.trim().equals("AUTOMATICO")){//recibo automatico
				sql = "from Recibo as r where r.id.numrec = "+iNumRec + " and r.id.codcomp = '"+sCodComp +"'";
				automatico = true;
			}else{
				sql = "from Recibo as r where r.id.numrecm = "+iNumRec + " and r.id.codcomp = '"+sCodComp +"'";
			}
			
			Recibo rec = (Recibo)session
			.createQuery(sql)			
			.uniqueResult();
			tx.commit();
			
			Date dRec = rec.getFecha();
			String sFecha = sdf.format(dRec);
			
			Date dHora = rec.getHora();
			String sHora = dfHora.format(dHora);
			
			//determinar numero de recibo
			int numrec = 0;
			if (automatico){
				numrec = rec.getId().getNumrec();
			}else{
				numrec = rec.getNumrecm();
			}
				
			recibo = new ReciboR(
					numrec,
					divisas.formatDouble(rec.getMontoapl().doubleValue()),
					divisas.formatDouble(rec.getMontorec().doubleValue()),
					divisas.formatDouble(0.00),
					rec.getConcepto(),
					rec.getId().getTiporec(),
					sFecha,
					rec.getCliente(),
					rec.getCodcli(),
					rec.getCajero(),
					sNomComp,
					"       " + sCodComp.trim(),
					sNomSuc,
					sHora
			);
		}catch(Exception ex){
			System.out.print("==> Excepción capturada en formatRecibo: " + ex);
		}finally {			
			session.close();
		}
		return recibo;
	}
/**********************************************************************************/
/***********************FORMATEAR DETALLE DEL RECIBO***********************************/
	public List formatRecibodet(int iNumRec, String sCodComp, String sTipoRec){
		Divisas divisas = new Divisas();
		ReciboDetR recibo = null;
		Recibodet rDet = null;
		List detalles = new ArrayList();
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = null;
		String sql = null;
		boolean automatico = false;
		try{
			tx = session.beginTransaction();
			if(sTipoRec.trim().equals("AUTOMATICO")){//automatico
				sql = "from Recibodet as r where r.id.numrec = "+iNumRec + " and r.id.codcomp = '"+ sCodComp +"'";
				automatico = true;
			}else{
				sql = "from Recibodet as r where r.id.numrecm = "+iNumRec + " and r.id.codcomp = '"+ sCodComp +"'";
			}
			List recDets = (List)session
			.createQuery(sql)			
			.list();
			tx.commit();
			
//			determinar numero de recibo
			int numrec = 0;
			
			for (int i = 0; i < recDets.size();i++){
				rDet = (Recibodet)recDets.get(i);
				if (automatico){
					numrec = rDet.getId().getNumrec();
				}else{
					numrec = rDet.getNumrecm();
				}
				recibo = new ReciboDetR(
						numrec,
						divisas.formatDouble(rDet.getMonto().doubleValue()),
						rDet.getId().getMoneda(),
						buscarMetodoxCodigo(rDet.getId().getMpago().trim()),
						divisas.formatDouble(rDet.getTasa().doubleValue()),
						divisas.formatDouble(rDet.getEquiv().doubleValue()),
						rDet.getId().getRefer1(),
						rDet.getId().getRefer2(),
						rDet.getId().getRefer3(),
						rDet.getId().getRefer4()
				);
				detalles.add(recibo);
			}
		}catch(Exception ex){
			System.out.print("==> Excepción capturada en formatRecibodet: " + ex);
		}finally {			
			session.close();
		}
		return detalles;
	}
/**********************************************************************************/
	/***********************FORMATEAR ENLCE RECIBO FACTURA Credito***********************************/
	public List formatRecibofacCredito(int iNumRec,String sCodComp,String sTipoRec, double[] dMontoPendiente){
		Divisas divisas = new Divisas();
		ReciboFacR recibo = null;
		Recibofac rFac = null;
		List detalles = new ArrayList();
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = null;
		String sql = null;
		boolean automatico = false; 
		try{
			tx = session.beginTransaction();		
			if(sTipoRec.trim().equals("AUTOMATICO")){//automatico
				sql = "from Recibofac as r where r.id.numrec = "+iNumRec + " and r.id.codcomp = '"+ sCodComp +"'";
				automatico = true;
			}else{
				sql = "from Recibofac as r where r.id.numrecm = "+iNumRec + " and r.id.codcomp = '"+ sCodComp +"'";
			}
			
			List recFacs = (List)session
			.createQuery(sql)			
			.list();
			tx.commit();
			
//			determinar numero de recibo
			int numrec = 0;
			
			for (int i = 0; i < recFacs.size();i++){
				rFac = (Recibofac)recFacs.get(i);
				if (automatico){
					numrec = rFac.getId().getNumrec();
				}else{
					//numrec = rFac.getId().getNumrecm();
				}
				recibo = new ReciboFacR(
						rFac.getId().getNumfac(),
						numrec,
						divisas.formatDouble(rFac.getMonto().doubleValue()),
						divisas.formatDouble(dMontoPendiente[i])
				);
				detalles.add(recibo);
			}
		}catch(Exception ex){
			System.out.print("==> Excepción capturada en formatRecibofac: " + ex);
		}finally {			
			session.close();
		}
		return detalles;
	}
	/***********************FORMATEAR ENLCE RECIBO FACTURA CONTADO***********************************/
	public List formatRecibofac(int iNumRec,String sCodComp,String sTipoRec){
		Divisas divisas = new Divisas();
		ReciboFacR recibo = null;
		Recibofac rFac = null;
		List detalles = new ArrayList();
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = null;
		String sql = null;
		boolean automatico = false; 
		try{
			tx = session.beginTransaction();		
			if(sTipoRec.trim().equals("AUTOMATICO")){//automatico
				sql = "from Recibofac as r where r.id.numrec = "+iNumRec + " and r.id.codcomp = '"+ sCodComp +"'";
				automatico = true;
			}else{
				sql = "from Recibofac as r where r.id.numrecm = "+iNumRec + " and r.id.codcomp = '"+ sCodComp +"'";
			}
			
			List recFacs = (List)session
			.createQuery(sql)			
			.list();
			tx.commit();
			
//			determinar numero de recibo
			int numrec = 0;
			
			for (int i = 0; i < recFacs.size();i++){
				rFac = (Recibofac)recFacs.get(i);
				if (automatico){
					numrec = rFac.getId().getNumrec();
				}else{
					//numrec = rFac.getId().getNumrecm();
				}
				recibo = new ReciboFacR(
						rFac.getId().getNumfac(),
						numrec,
						divisas.formatDouble(rFac.getMonto().doubleValue()),
						"0.00"
				);
				detalles.add(recibo);
			}
		}catch(Exception ex){
			System.out.print("==> Excepción capturada en formatRecibofac: " + ex);
		}finally {			
			session.close();
		}
		return detalles;
	}
/**********************************************************************************/
	public String buscarMetodoxCodigo(String sCodigoMetodo){
		String sMetodo = "";
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			String sql = "select m.id.mpago from Mpago m where trim(m.id.codigo) = '"+sCodigoMetodo+"'";
			sMetodo = (String)session
			.createQuery(sql)			
			.uniqueResult();
			tx.commit();
		}catch(Exception ex){
			System.out.print("Excepcion capturada en buscarMetodoxCodigo:" + ex);
		}finally {			
			session.close();
		}
		return sMetodo;
	}
}

package com.casapellas.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.casapellas.controles.ArqueoCajaCtrl;
import com.casapellas.controles.CompaniaCtrl;
import com.casapellas.controles.CtrlCajas;
import com.casapellas.controles.EmpleadoCtrl;
import com.casapellas.controles.MetodosPagoCtrl;
import com.casapellas.controles.ReciboCtrl;
import com.casapellas.controles.ReintegroCtrl;
import com.casapellas.entidades.F55ca014;
import com.casapellas.entidades.Reintegro;
import com.casapellas.entidades.ens.Vautoriz;
import com.casapellas.entidades.Vf0901;
import com.casapellas.entidades.Vf55ca01;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.jde.creditos.CodigosJDE1;
import com.casapellas.navegacion.As400Connection;
import com.casapellas.util.Divisas;
import com.infragistics.faces.grid.component.RowItem;
import com.infragistics.faces.grid.component.html.HtmlGridView;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
import com.infragistics.faces.window.component.html.HtmlDialogWindow;

public class ReintegroDAO {
	Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	private List lstCajasCombo;
	private HtmlDropDownList cmbCajasConsulta;
	
	private HtmlGridView gvReintegro;
	private List lstReintegro;
	
	//companias de reintegro
	private HtmlDropDownList cmbCompania;
	private List lstCompania;
	
	//estado de reintegro
	private List lstEstado;
	private HtmlDropDownList cmbEstado;
	
	//confirma proceso
	private HtmlDialogWindow dwConfirmaProceso;
	private HtmlDialogWindow dwValidaReintegro;
	private String lblMensaje;

	private String sObjCtaPuente = "19800";
	private String sSubCtaPuente = "03";
	
	//Valores reimplentacion JDE
	String[] valoresJdeInsContado = (String[]) m.get("valoresJDEInsContado");

/********************************************************************************/		
	public void	BuscarReintegros(ActionEvent ev){
		int caja = 0;
		String comp = "";
		String estado = "";
		try{
			caja = Integer.parseInt(cmbCajasConsulta.getValue().toString());
			comp = cmbCompania.getValue().toString();
			estado = cmbEstado.getValue().toString();
			
			m.put("lstReintegro",new ReintegroCtrl().getReintegrosxParametro(caja,comp,estado));
			gvReintegro.dataBind();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
/********************************************************************************/		
	public boolean reintegrar( Session session ){
		Reintegro r = null;
		String codcomp = "", sCuenta1 = "", sConcepto = "",sTipoCliente = "";
		int iNoBatch = 0, iNoDocumento = 0;
		 
		Divisas dv = new Divisas();
		String[] sCuentaMinimo = null;
		boolean hecho = true;
		Vf0901 vf0901 = null;
		ArqueoCajaCtrl acCtrl = new ArqueoCajaCtrl();
		
		try{
			
			Vautoriz[] vaut = (Vautoriz[]) m.get("sevAut");
			r = (Reintegro)m.get("reintegro");
			
			codcomp = r.getId().getCodcomp().trim();
			
			sCuenta1 = "10";
			
			if (codcomp.equals("E01"))
				sCuenta1 = "10";
			
			if (codcomp.equals("E02"))
				sCuenta1 = "75";
			
			if (codcomp.equals("E03"))
				sCuenta1 = "80";
			
			if (codcomp.equals("E10"))
				sCuenta1 = "11035";
			
			if (codcomp.equals("E12"))
				sCuenta1 = "11224";
			
			Date dtFecha =  new Date();
			ReciboCtrl rCtrl = new ReciboCtrl();
			sConcepto = " Reintegro de fondo ca: " + r.getId().getCaid() + " reint: " + r.getId().getNoreint();
			String msgLogs = sConcepto ;
			 
			sTipoCliente = EmpleadoCtrl.determinarTipoCliente(vaut[0].getId().getCodreg());
			
			 
			iNoBatch = Divisas.numeroSiguienteJdeE1(   );
			if(iNoBatch == 0){					
				lblMensaje = "No se ha podido obtener el Número de batch para registro de Reintegro de fondo minimo ";
				return false;
			}
			
			 
			//iNoDocumento = dv.leerActualizarNoDocJDE();
			iNoDocumento = Divisas.numeroSiguienteJdeE1(CodigosJDE1.NUMERO_DOC_CONTAB_GENERAL );
			if(iNoDocumento == 0){
				lblMensaje = "No se ha podido obtener el Número de Documento para el registro de reintegro de fondo minimo ";
				return false;
			}
			
			//leer la cuenta de fondo minimo por caja
			sCuentaMinimo = dv.obtenerCuentaFondoMinimo(r.getId().getCaid(), r.getId().getCodcomp(), r.getMoneda());
			if(sCuentaMinimo == null){
				lblMensaje="No se ha podido leer la cuenta de fondo minimo para la moneda: " + r.getMoneda();				
				return false;
			}
		 
			
			int iMonto = Divisas.pasarAentero(r.getMonto().doubleValue());
		
			hecho = rCtrl.registrarBatchA92(session, dtFecha, valoresJdeInsContado[8], iNoBatch, iMonto, vaut[0].getId().getLogin(), 1, "REINTEGRO", valoresJdeInsContado[9] );
			
			if(!hecho){
				lblMensaje="No se ha podido grabar el batch";				
				return false;
			}
			
			vf0901 = dv.validarCuentaF0901(sCuenta1, sObjCtaPuente, sSubCtaPuente);
			String tipoDocumento = CodigosJDE1.BATCH_CONTADO.codigo();
			
			hecho = rCtrl.registrarAsientoDiarioLogs(session, msgLogs, dtFecha, sCuenta1, tipoDocumento, iNoBatch, 1.0,
						iNoBatch, sCuentaMinimo[0],	sCuentaMinimo[1], 
						sCuentaMinimo[3], sCuentaMinimo[4], 
						sCuentaMinimo[5], "AA",  r.getMoneda(), iMonto,
						sConcepto, vaut[0].getId().getLogin(), vaut[0].getId().getCodapp(), 
						BigDecimal.ZERO, sTipoCliente,"Reintegro de minimo CTA CA",
						sCuentaMinimo[2], "", "", r.getMoneda(),sCuentaMinimo[2],"D", 0);
			if(hecho){
				hecho = rCtrl.registrarAsientoDiarioLogs(session, msgLogs, dtFecha, sCuenta1, tipoDocumento, iNoBatch, 2.0,
						iNoBatch,vf0901.getCuenta(), vf0901.getId().getGmaid(), 
						vf0901.getId().getGmmcu().trim(), vf0901.getId().getGmobj(), vf0901.getId().getGmsub(),
						"AA", r.getMoneda(), iMonto*-1, 
						sConcepto, vaut[0].getId().getLogin(), vaut[0].getId().getCodapp(), 
						BigDecimal.ZERO, sTipoCliente,"Reintegro de minimo CTA PTE",
						vf0901.getId().getGmco().trim(), "", "", r.getMoneda(),
						vf0901.getId().getGmco().trim(), "D", 0);
				
				if(!hecho){
					lblMensaje="No se pudo registrar la linea 2 del batch!!!";				
					return false;
				}else{
					
					r.setEstado(true);
					r.setBatchreint(iNoBatch);
					r.setDocreint(iNoDocumento);
					
					hecho = rCtrl.editarReintegro(session, r);
				
					if(hecho){
						 
						double dMontomin = Double.parseDouble( acCtrl.obtenerMontoMinimodeCaja( r.getId().getCaid(), r.getId().getCodcomp(), r.getMoneda()));
						dMontomin = dMontomin/100;
						
						CtrlCajas cc = new CtrlCajas();		
						dMontomin = Divisas.roundDouble(dMontomin) + Divisas.roundDouble(r.getMonto().doubleValue());
						
						hecho = cc.actualizarMontoMinimo(r.getId().getCaid(), r.getId().getCodcomp(),  r.getMoneda(), Divisas.roundDouble(dMontomin) );
					
						if(!hecho){
							lblMensaje = "No se ha podido completar la operación: Error de sistema: ";
							lblMensaje += "No se ha podido actualizar el monto mínimo de la caja";
							return false;
						}
					}
				}
			}else{				
				lblMensaje="No se pudo registrar la linea 1 del batch!!!";				
				return false;
			}
			dwConfirmaProceso.setWindowState("hidden");
		}catch(Exception ex){
			ex.printStackTrace(); 
			lblMensaje = "No se ha podido completar la operación: Error de sistema: ";
			lblMensaje += " " + ex;
			hecho = false;
		}
		return hecho;
	}
/********************************************************************************/		
	public void realizarReintegro(ActionEvent ev){
		boolean hecho = true;
		Session session = null;
		Transaction transaction = null;
		
		try{
			session = HibernateUtilPruebaCn.currentSession();
			transaction = session.beginTransaction();
 
			hecho = reintegrar( session );
			
			try {
				
				if(hecho){
					transaction.commit();
				}else{
					transaction.rollback();
				}
				
			} catch (Exception e) {
				hecho = false;
				e.printStackTrace();
			}
			
			if(hecho){
				m.put("lstReintegro",new ReintegroCtrl().getReintegrosxParametro(0,"","01"));
				gvReintegro.dataBind();
				lblMensaje = "Operación Realizada con exito!!!";
			}else{
				lblMensaje = " no se ha podido aplicar el reintegro !";
			}
			
			dwValidaReintegro.setWindowState("normal");
			dwConfirmaProceso.setWindowState("hidden");		
			 
		}catch(Exception ex){
			ex.printStackTrace(); 
		} finally{
			HibernateUtilPruebaCn.closeSession(session) ;
		}
	}	
/********************************************************************************/		
	public void cerrarValidaReintegro(ActionEvent ev){
		dwValidaReintegro.setWindowState("hidden");
	}
	
	public void cancelarReintegro(ActionEvent ev){
		dwConfirmaProceso.setWindowState("hidden");
	}	
/********************************************************************************/		
	public void confirmarReintegro(ActionEvent ev){
		try{			
			RowItem ri = (RowItem) ev.getComponent().getParent().getParent();
			Reintegro r = (Reintegro) gvReintegro.getDataRow(ri);
			if(!r.isEstado()){
				m.put("reintegro", r);
				dwConfirmaProceso.setWindowState("normal");
			}else{
				lblMensaje = "Este Reintegro ya fue aprobado";
				dwValidaReintegro.setWindowState("normal");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}	
/********************************************************************************/	
	
	public HtmlGridView getGvReintegro() {
		return gvReintegro;
	}
	public void setGvReintegro(HtmlGridView gvReintegro) {
		this.gvReintegro = gvReintegro;
	}
/********************************************************************************/	
	public List getLstReintegro() {
		try {
			if(m.get("lstReintegro")!= null){
				lstReintegro = (List) m.get("lstReintegro");
			}else{
				lstReintegro = new ArrayList();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return lstReintegro;
	}
	public void setLstReintegro(List lstReintegro) {
		this.lstReintegro = lstReintegro;
	}
/********************************************************************************/	
	public List getLstCompania() {
		try {
			if(m.get("compReciboRE") == null) {			
				//obtener Companias
				lstCompania = new ArrayList();
				lstCompania.add(new SelectItem("01","Todas"));
				List lstCajas = (List)m.get("lstCajas");
				CompaniaCtrl compCtrl = new CompaniaCtrl();
				F55ca014[] f55ca014 = null;
				f55ca014 = compCtrl.obtenerCompaniasxCaja(((Vf55ca01)lstCajas.get(0)).getId().getCaid());
				for (int i = 0; i < f55ca014.length; i ++){	
					lstCompania.add(new SelectItem(f55ca014[i].getId().getC4rp01(),f55ca014[i].getId().getC4rp01d1()));
				}
				m.put("lstCompaniaReciboRE",lstCompania);
				m.put("f55ca014", f55ca014);
				m.put("compReciboRE","c");
			} else {
				lstCompania = (List)m.get("lstCompaniaReciboRE");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return lstCompania;
	}
/********************************************************************************/
	public List getLstCajasCombo() {
		try{
			if(m.get("lstCajaConsultaRE")==null){
				CtrlCajas cajasCtrl = new CtrlCajas();
				lstCajasCombo = new ArrayList();
				List lstCajas = cajasCtrl.getAllCajas();
				Vf55ca01 c = null; 
				lstCajasCombo.add(new SelectItem(0+"","Todas","Filtra en todas las cajas"));
				for (int i = 0; i < lstCajas.size(); i++){
					c = (Vf55ca01)lstCajas.get(i);
					if(c != null){
						lstCajasCombo.add(new SelectItem(c.getId().getCaid()+"",c.getId().getCaid() + "  : " + c.getId().getCaname(),"Cajero Titular: " + c.getId().getCacatinom()+""));
					}
				}	
				m.put("lstCajaConsultaRE",lstCajasCombo);
			} else {
				lstCajasCombo = (List)m.get("lstCajaConsultaRE");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return lstCajasCombo;
	}
/********************************************************************************/

	public void setLstCajasCombo(List lstCajasCombo) {
		this.lstCajasCombo = lstCajasCombo;
	}
	public HtmlDropDownList getCmbCajasConsulta() {
		return cmbCajasConsulta;
	}

	public void setCmbCajasConsulta(HtmlDropDownList cmbCajasConsulta) {
		this.cmbCajasConsulta = cmbCajasConsulta;
	}
	public HtmlDropDownList getCmbCompania() {
		return cmbCompania;
	}
	public void setCmbCompania(HtmlDropDownList cmbCompania) {
		this.cmbCompania = cmbCompania;
	}
	public void setLstCompania(List lstCompania) {
		this.lstCompania = lstCompania;
	}
	public HtmlDialogWindow getDwConfirmaProceso() {
		return dwConfirmaProceso;
	}
	public void setDwConfirmaProceso(HtmlDialogWindow dwConfirmaProceso) {
		this.dwConfirmaProceso = dwConfirmaProceso;
	}
	public HtmlDialogWindow getDwValidaReintegro() {
		return dwValidaReintegro;
	}
	public void setDwValidaReintegro(HtmlDialogWindow dwValidaReintegro) {
		this.dwValidaReintegro = dwValidaReintegro;
	}
	public String getLblMensaje() {
		return lblMensaje;
	}
	public void setLblMensaje(String lblMensaje) {
		this.lblMensaje = lblMensaje;
	}
	public List getLstEstado() {
		lstEstado = new ArrayList();
		lstEstado.add(new SelectItem("01","Pendiente","Reintegros pendientes"));
		lstEstado.add(new SelectItem("02","Aprobados","Reintegros aprobados"));
		return lstEstado;
	}
	public void setLstEstado(List lstEstado) {
		this.lstEstado = lstEstado;
	}
	public HtmlDropDownList getCmbEstado() {
		return cmbEstado;
	}
	public void setCmbEstado(HtmlDropDownList cmbEstado) {
		this.cmbEstado = cmbEstado;
	}
	
}

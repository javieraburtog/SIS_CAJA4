

/**
 * 
 	Creado por: Juan Carlos Ñamendi Pineda
 	Fecha de Cracion: 06/12/2008
 	Última modificación: 18/05/2009
    Modificado por.....:	Juan Carlos Ñamendi Pineda
 */

package com.casapellas.controles;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.casapellas.entidades.Cambiodet;
import com.casapellas.entidades.CambiodetId;
import com.casapellas.entidades.F55ca01;
import com.casapellas.entidades.F55ca011;
import com.casapellas.entidades.F55ca014;
import com.casapellas.entidades.F55ca021;
import com.casapellas.entidades.Hfactura;
import com.casapellas.entidades.MetodosPago;
import com.casapellas.entidades.Recibo;
import com.casapellas.entidades.ReciboId;
import com.casapellas.entidades.Recibodet;
import com.casapellas.entidades.RecibodetId;
import com.casapellas.entidades.Recibofac;
import com.casapellas.entidades.RecibofacId;
import com.casapellas.entidades.Tcambio;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.util.Divisas;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.PropertiesSystem;
import com.infragistics.faces.input.component.html.HtmlDateChooser;
import com.infragistics.faces.shared.smartrefresh.SmartRefreshManager;
import com.infragistics.faces.window.component.DialogWindow;


public class CtrlRecibosCredito {

	Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		
	//Datos del Cliente
	public String lblcliente;
	private UIInput txtCliente;
	public String lblTasaCambio;
	private UIInput txtTasaCambio;
	public UIOutput lblMontoRecibido2;
	public UIOutput lblCambio2;
	private UIInput txtConcepto;
	
	
	//Datos del Recibo
	public String fechaRecibo;
	public String lblNumeroRecibo;	
	
	//Tipo de recibos
	private UIInput cmbTiporecibo;
	private List lstTiporecibo = null;	
	
	//Datos de la factura
	public Double tasaCambio;
	public Double totalFactura;
	public BigDecimal montoRecibido;
	public BigDecimal iva;
	public Double cambio;

	//Montos
	public String lblTotal = "0.00";
	public String lblMonto ="0.00";
	public String lblCambio = "0.00";	
	
	//Dialogs Window
	private DialogWindow dwRecibo;
	private DialogWindow dwProcesa;
	private DialogWindow dwImprime;	
	private DialogWindow dwSolicitud;
	private DialogWindow dwMensaje;
	private DialogWindow dwValida2;
	
	//Variables Tipo Recibo
	private UIOutput lblNumrec2;
	public UIOutput lblNumeroRecibo2;
	public String lblNumrec = "Último Recibo: ";
	private HtmlDateChooser txtFecham;
	private HtmlInputText txtNumRec;
	
	
	//Validaciones
	private UIOutput lblValidaRecibo;
	private DialogWindow dwValida;
	private UIOutput lblValidaFactura;	
	private DialogWindow dwFactura;
	
	
	//-------------------------------------------------------------------------------
	
	
	//Carga Popup Recibo
	public void mostrarRecibo(ActionEvent e) {		
		
		//Formato de Numeros
		DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
		simbolos.setDecimalSeparator('.');
		simbolos.setGroupingSeparator(',');
		DecimalFormat dfDigitos = new DecimalFormat("#,###,##0.00",simbolos);
		
		
		//Valida Factura
		if(m.get("selectedFacsCredito") != null) {			
			
			//Valida seleccion de factura
			if(validaFacturas() == true) {
				
				List lstFacturas = null;		
				lstFacturas = (List) m.get("facturasSelected");
				
				try {
										
					lblTasaCambio = dfDigitos.format(Double.parseDouble(m.get("mTasaCambio").toString()));					
					//lblTasaCambio = m.get("mTasaCambio").toString();					
					lblcliente = m.get("mNomCliente").toString().trim();					
					lblTotal = dfDigitos.format(Double.parseDouble(m.get("mTotalFactura").toString()));
					lblMonto = "0.00";
					lblCambio = "0.00";	
					
					//Muestra Recibo
					dwRecibo.setWindowState("normal");
					
				}catch(Exception ex){					
					LogCajaService.CreateLog("Exception -> mostrarRecibo:", "ERR", ex.getMessage());
				}
				
			} else {
				//Facturas seleccionadas no cumplen con politica
				getLblValidaFactura().setValue("Las Facturas seleccionadas no cumplen con las políticas.");
				dwFactura.setWindowState("normal");				
			}
		
		} else {
			getLblValidaFactura().setValue("Seleccione la Factura.");
			dwFactura.setWindowState("normal");			
		}
		
	}
		

	
	//-------------------------------------------------------------------------
	
	
	//Valida facturas a cancelar
	@SuppressWarnings({ "unused", "unchecked" })
	public boolean validaFacturas() {
		Session session = null;
		boolean valf = true;
		String tcli = "";
		String tmon = "";
		String vmoneda = "";
		
		if(m.get("mClienteF") != null && m.get("mMonedaF") != null) {
			
			String[] cliente = (String[]) m.get("mClienteF");
			String[] moneda = (String[]) m.get("mMonedaF");
			vmoneda = moneda[0];
			m.put("xMonedaF", vmoneda);
			
			if(cliente.length > 1) {			
			
				for(int i=0; i<cliente.length; i++) {
					tcli = cliente[i];
					for(int j=0; j<cliente.length; j++) {						
						if(tcli.equals(cliente[j]) && (i != j)) {
							tmon = moneda[i];
							if(!(tmon.equals(moneda[i]))) {
								valf = false;
							}
						} else if(!tcli.equals(cliente[j]) && (i != j)) {
							valf = false;
						}
					}
				}			
			}
		}
		
		try {
			
			if(valf==true) {
				
				Date fechatc = new Date();	
				String sdia = fechatc.getDay() + "";
				String smes = fechatc.getMonth() + "";
				String sano = fechatc.getYear() + "";
				String sfechatc = sano + "-" + smes + "-" + sdia;				
				
				Format formatter = new SimpleDateFormat("yyyy-MM-dd");
				sfechatc = formatter.format(fechatc);
				 
				//Toma tasa paralelo
				if(!vmoneda.equals("COR")) {
					List result = null;
					
			        session = HibernateUtilPruebaCn.currentSession();
			        
			        F55ca021 tcparalelo = new F55ca021();
					if(result != null) {
						tcparalelo = (F55ca021) result.get(0);
						lblTasaCambio = tcparalelo.getId().getTcambiod() + "";
						m.put("mTasaCambio", tcparalelo.getId().getTcambiod().doubleValue());
					}
					
					session.close();
					
					
				//Toma tasa JDE	
				} else if(vmoneda.equals("COR")){
					
					// comentariado por que no ejecutaba nada y podia 
					// dejar conexiones colgadas con ENS.
					
					
//			        session = HibernateUtilPruebaCn.currentSessionENS();
//			        List result = null;
//					
//					Tcambio tasacambiojde = new Tcambio();
//					if(result != null) {
//						tasacambiojde = (Tcambio) result.get(0);
//						lblTasaCambio = tasacambiojde.getId().getCxcrr().toString();
//						m.put("mTasaCambio", tasacambiojde.getId().getCxcrr());
//					}
//					try {
//						if (session.getTransaction().isActive())
//							HibernateUtilPruebaCn.closeSessionENS();
//					} catch (Exception e) { e.printStackTrace(); }
					
				}
			}
		}catch(Exception ex){					
			
			LogCajaService.CreateLog("Exepcion en validaFacturas", "ERR", ex.getMessage());
		} 
		return valf;
	}
		
	
	
	//---------------------------------------------------------------------------------------
	
	
	//Procesa Recibo
	@SuppressWarnings("unchecked")
	public void procesarRecibo(ActionEvent e) {	
		Divisas divisas = new Divisas();
		m.get("mTotalFactura");
		m.get("montoRecibido");
		boolean bregistrar = true;
		boolean bAutomatico = true;
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");	//dd/MM/yyyy		
//		String dxfechaq = "^(((0[1-9]|[12]\\d|3[01])\\/(0[13578]|1[02])\\/((1[6-9]|[2-9]\\d)\\d{2}))|((0[1-9]|[12]\\d|30)\\/(0[13456789]|1[012])\\/((1[6-9]|[2-9]\\d)\\d{2}))|((0[1-9]|1\\d|2[0-8])\\/02\\/((1[6-9]|[2-9]\\d)\\d{2}))|(29\\/02\\/((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))))$";	
		String xtiporec = getCmbTiporecibo().getValue().toString();
		
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = session.beginTransaction();
				
		try {
		
			
			//Valida recibo manual
			if(xtiporec.equals("MANUAL")) {
							
				//Valida numero de recibo manual
				if(getTxtNumRec().getValue().equals("")) {
					bregistrar = false;
					getLblValidaRecibo().setValue("Número de Recibo es requerido.");
					dwValida.setWindowState("normal");				
					
				} else {
									
					//Valida numero recibo manual
					String xnumrecm = getTxtNumRec().getValue().toString();
					String pint = "^\\d*$";
					Pattern p = Pattern.compile(pint);							
					Matcher mat = p.matcher(xnumrecm);
					
					if(mat.matches() == false){
						bregistrar = false;					
						getLblValidaRecibo().setValue("Verifique el número de recibo.");
						dwValida.setWindowState("normal");
					}
					
					
				if(bregistrar == true) {	
					
					//Valida fecha de recibo manual
					if(getTxtFecham().getValue() == null) {
						bregistrar = false;
						getLblValidaRecibo().setValue("Fecha de Recibo es requerida.");
						dwValida.setWindowState("normal");
					
					} else {
						sdf = new SimpleDateFormat("dd/MM/yyyy");
						Matcher matFecha = null;
						Date dFecha = null;
						String sFecha =null;
						dFecha = (Date)txtFecham.getValue();
						sFecha = sdf.format(dFecha);
						Pattern pFecha = Pattern.compile( "^[0-3]?[0-9](/|-)[0-2]?[1-9](/|-)[1-2][0-9][0-9][0-9]$" );	
						matFecha = pFecha.matcher(sFecha);
						
						if(mat.matches() == false){			
							bregistrar = false;						
							getLblValidaRecibo().setValue("Verifique la fecha de recibo.");
							dwValida.setWindowState("normal");
						}				
					}				
				}
			}	
		}		
			
		
		//Evalua validaciones de recibo
		if(bregistrar == true) {
			
			//Valida existencia de pagos
			if(m.get("InstPagos") != null) {
			
				//Valida concepto de recibo
				if(!getTxtConcepto().getValue().equals("")) {
			
//					Datos del Recibo
					Double total = (Double) m.get("mTotalFactura");
					Double montoRec = (Double) m.get("montoRecibibo");
					Double cambio = (Double) m.get("mCambio");
					String sConcepto = ""; 	
					String sNomCli = "";
					String sCodComp = "";
					String sNomComp = "";
					String sNombreEmpleado = "";
					Hfactura hFac = null;
					double[] dMonto = null;
					int[] iNoFac = null;
					List lstCajas = new ArrayList();		
					Date fecha = new Date();
					
					
					List lstFacturas = (List) m.get("facturasSelected");
					iNoFac = new int[lstFacturas.size()];
					dMonto = new double[lstFacturas.size()];
										
					lstCajas = (List)m.get("lstCajas");
					sNombreEmpleado = (String)m.get("sNombreEmpleado");
						sConcepto = txtConcepto.getValue().toString();
						int iCodCli = 0;
						
						//obtener el codigo y nombre del cliente
						for(int i = 0; i < lstFacturas.size(); i++){
							hFac = (Hfactura)lstFacturas.get(i);
							iNoFac[i] = hFac.getNofactura();
							dMonto[i] = hFac.getTotal();
							iCodCli = hFac.getCodcli();
							sNomCli = hFac.getNomcli();		
							sCodComp = hFac.getCodcomp();
							sNomComp = hFac.getNomcomp();										
						}
						
						Date dHora = new Date();
						
						//Clase Recibo
						Recibo recibo = new Recibo();
						ReciboId reciboid = new ReciboId();
						
						BigDecimal bdMontoRec = BigDecimal.valueOf(montoRec);					
						int iNumRec = 0;
						int iNumRecm = 0;
						
						//Recibo Automatico
						if(!xtiporec.equals("MANUAL")) {
							
							//Consecutivo en la tabla
							iNumRec = obtenerUltimoRecibo(Integer.valueOf(m.get("sCajaId").toString()), m.get("sCompania").toString()) + 1;
							reciboid.setNumrec(iNumRec);						
							//reciboid.setNumrecm(iNumRecm);						
													
						//Recibo Manual	
						} else {
							
							bAutomatico = false;
							
							//Digitado por el usuario
							iNumRecm = Integer.parseInt(getTxtNumRec().getValue().toString());
							fecha = new Date(getTxtFecham().getValue().toString());
							//reciboid.setNumrecm(iNumRecm);
							reciboid.setNumrec(0);
						}
						reciboid.setCodcomp(sCodComp.trim());
						recibo.setId(reciboid);
											
						recibo.setMontoapl(BigDecimal.valueOf(total));			//Monto Aplicar
						recibo.setMontorec(BigDecimal.valueOf(montoRec));		//Monto Recibido
						recibo.setConcepto(sConcepto);							//Concepto
						//recibo.setTiporec("CR");								//Tipo Recibo
						recibo.setFecha(fecha);									//Fecha
						recibo.setHora(dHora);									//Hora
						recibo.setCodcli(iCodCli);								//Cod Cliente
						recibo.setCliente(sNomCli);								//Nombre Cliente
						recibo.setCajero(sNombreEmpleado);						//Cajero					
						
						session.save(recibo);
											
						//Registra detalle Recibo (Metodos pago)
						boolean insertado = llenarMetodosPago2(iNumRec, iNumRecm, sCodComp.trim());
						boolean actualizado = false;
						
						//Valida Tipo Recibo
						if(!xtiporec.equals("MANUAL")) {
							actualizado = actualizarNumeroRecibo(((F55ca01)lstCajas.get(0)).getId().getCaid(),sCodComp.trim(),iNumRec);
						} else {
							actualizado = true;
						}
						
						//Registra Cambio a Cliente
						if(m.get("rCambio").equals("si")) {
							registrarCambio();
							m.remove("rCambio");
						}
						
						//boolean filled = fillEnlaceReciboFac(iNumRec, iNumRecm, sCodComp, iNoFac, dMonto);					
						
						if (actualizado && insertado /*&& filled*/){
							tx.commit();
							m.put("sCodComp", sCodComp);
							m.put("sNomComp", sNomComp);
							if(bAutomatico){
								m.put("NumeroRecibo", iNumRec);
							}else{
								m.put("NumeroRecibo", iNumRecm);
							}	
							m.put("sTipoRec", xtiporec);
							//Aplicar contabilidad del recibo en JDE contable 
							
							
							//ir al reporte						
							dwRecibo.setWindowState("hidden");
							FacesContext.getCurrentInstance().getExternalContext().redirect("/GCPMCAJA/reportes/reciboCredito.faces");
							
							
						}else{
							tx.rollback();						
						}

					
				} else {
					getLblValidaRecibo().setValue("Concepto de recibo es requerido.");
					dwValida.setWindowState("normal");
					
				}
			
			} else {
				getLblValidaRecibo().setValue("El recibo no tiene pagos.");					
				dwValida.setWindowState("normal");
			}
		}
	
		}catch(Exception ex){
			LogCajaService.CreateLog("Exception -> procesarRecibo:", "ERR", ex.getMessage());
		}finally{
			try{session.close();}catch(Exception ex2){ex2.printStackTrace();};
		}		
	}

/************************APLICAR CONTABILIDAD A JDE**************************************************/
	public void contabilizarCredito(int iCajaId,String sCodComp, double dTotalAplicar){
		Divisas divisas = new Divisas();
		List lstMetodosPago = null, lstFacturas = null;
		MetodosPago[] metPago = null;
		F55ca011[] f55ca011 = null;
		Hfactura[] hFac = null;
		String[] sIdCaja = null;
		double[] dPorcentajeFac = null;
		double[][] montoxMetodo = null;
		double[] montoAplicarFac = null;
		double[] montoMetodo = null,restanteFac = null,restanteMet = null;
		String sMonFactura = null;
		double dTasa = Double.parseDouble(m.get("mTasaCambio").toString());
		//F0311JDBC[] f0311 = null;
		try{
			lstMetodosPago = (List)m.get("mpagos");
			lstFacturas = (List)m.get("selectedFacsCredito");
			int iPagoLargo = lstMetodosPago.size();
			String sCajaId1 = null;
		/*****determinar montos a aplicar segun porcentaje y metodo de factura*****/	
			//Arreglos a usar para la distribucion de montos
			metPago = new MetodosPago[iPagoLargo];
			f55ca011 = new F55ca011[iPagoLargo];
			sIdCaja = new String[iPagoLargo];
			restanteMet = new double[iPagoLargo];
			dPorcentajeFac = new double[lstFacturas.size()];
			hFac = new Hfactura[lstFacturas.size()];
			montoAplicarFac = new double[lstFacturas.size()];
			montoxMetodo = new double[lstFacturas.size()][iPagoLargo];
			restanteFac = new double[lstFacturas.size()];
			sMonFactura = ((Hfactura)lstFacturas.get(0)).getMoneda();
			if(sMonFactura.equals("COR")){//moneda domestica
				for(int i = 0; i < iPagoLargo; i++){
					metPago[i] = (MetodosPago)lstMetodosPago.get(i);
					f55ca011[i] = obtenerCuenta(iCajaId,sCodComp,metPago[i].getMoneda(),metPago[i].getMetodo());
					sIdCaja[i] = obtenerIdCuenta(f55ca011[i].getId().getC1mcu(),f55ca011[i].getId().getC1obj(),f55ca011[i].getId().getC1sub());	
					
					if(metPago[i].getMoneda().equals("COR")){
						montoMetodo[i] = metPago[i].getMonto();
						restanteMet[i] = montoMetodo[i];
					}else{
						montoMetodo[i] = metPago[i].getEquivalente();
						restanteMet[i] = montoMetodo[i];
					}		
				}
				//Determinar porcentaje para cada factura segun total a aplicar
				for(int k = 0; k < lstFacturas.size();k++){
						hFac[k] = (Hfactura)lstFacturas.get(k);
						montoAplicarFac[k] = divisas.formatStringToDouble(hFac[k].getMontoAplicar());
						restanteFac[k] = montoAplicarFac[k];
						//obtener el porcentaje de a aplicar a cada factura
						dPorcentajeFac[k] = (montoAplicarFac[k]*100)/dTotalAplicar;
					for(int l = 0; l < iPagoLargo; l++){
						if(k != lstFacturas.size()-1){
							if(l != iPagoLargo -1){
								//cantidad a aplicar a cada factura
								montoxMetodo[k][l] = (dPorcentajeFac[k]*montoMetodo[l])/100;
										//fac//pago
								restanteMet[l] = restanteMet[l] - montoxMetodo[k][l];
								restanteFac[k] = restanteFac[k] - montoxMetodo[k][l];
							}else{
								montoxMetodo[k][l] = restanteFac[k];
								restanteMet[l] =  restanteFac[k];
							}
						}else{
							montoxMetodo[k][l] = restanteMet[l];
						}
					}
				}
				//convertir los metodos cuadrados a su moneda a aplicar
				for(int m = 0; m < lstFacturas.size();m++){
					for(int n = 0; n < iPagoLargo; n++){
						if(!metPago[n].getMoneda().equals("COR")){
							montoxMetodo[m][n] = montoxMetodo[m][n]*dTasa;
						}
					}
				}
			}else{//moneda foranea
				for(int i = 0; i < iPagoLargo; i++){
					metPago[i] = (MetodosPago)lstMetodosPago.get(i);
					f55ca011[i] = obtenerCuenta(iCajaId,sCodComp,metPago[i].getMoneda(),metPago[i].getMetodo());
					sIdCaja[i] = obtenerIdCuenta(f55ca011[i].getId().getC1mcu(),f55ca011[i].getId().getC1obj(),f55ca011[i].getId().getC1sub());	
					
					if(!metPago[i].getMoneda().equals("COR")){
						montoMetodo[i] = metPago[i].getMonto();
						restanteMet[i] = montoMetodo[i];
					}else{
						montoMetodo[i] = metPago[i].getEquivalente();
						restanteMet[i] = montoMetodo[i];
					}		
				}
				//Determinar porcentaje para cada factura segun total a aplicar
				for(int k = 0; k < lstFacturas.size();k++){
						hFac[k] = (Hfactura)lstFacturas.get(k);
						montoAplicarFac[k] = divisas.formatStringToDouble(hFac[k].getMontoAplicar());
						restanteFac[k] = montoAplicarFac[k];
						//obtener el porcentaje de a aplicar a cada factura
						dPorcentajeFac[k] = (montoAplicarFac[k]*100)/dTotalAplicar;
					for(int l = 0; l < iPagoLargo; l++){
						if(k != lstFacturas.size()-1){
							if(l != iPagoLargo -1){
								//cantidad a aplicar a cada factura
								montoxMetodo[k][l] = (dPorcentajeFac[k]*montoMetodo[l])/100;
										//fac//pago
								restanteMet[l] = restanteMet[l] - montoxMetodo[k][l];
								restanteFac[k] = restanteFac[k] - montoxMetodo[k][l];
							}else{
								montoxMetodo[k][l] = restanteFac[k];
								restanteMet[l] =  restanteFac[k];
							}
						}else{
							montoxMetodo[k][l] = restanteMet[l];
						}
					}
				}
				//convertir los metodos cuadrados a su moneda a aplicar
				for(int m = 0; m < lstFacturas.size();m++){
					for(int n = 0; n < iPagoLargo; n++){
						if(metPago[n].getMoneda().equals("COR")){
							montoxMetodo[m][n] = montoxMetodo[m][n]/dTasa;
						}
					}
				}
			}
			/**********generar registros RC*******************/
			/*f0311 = new F0311JDBC[lstFacturas.size()];
			for (int a = 0; a < lstFacturas.size();a++){
				/*********leer registro(s) de factura(s) del F0311**********************/
				//f0311[a] = obtenerFacturaF0311(hFac[a].getNofactura(),hFac[a].getPartida(), hFac[a].getTipofactura(), hFac[a].getCodcli());
			//}
		}catch(Exception ex){
			LogCajaService.CreateLog("=>Excepcion capturada en contabilizarCredito:", "ERR", ex.getMessage());
		}
	}
	
/****************OBTENER NUMERO DE RECIBO DE JDE******************/
	public int obtenerNumeroReciboJDE(){
		int iLastNumRec = 0;

		Session session = HibernateUtilPruebaCn.currentSession();
		
		Transaction tx = null;
		boolean bNuevaSesionENS = false;
		
		try{	
			if( session.getTransaction().isActive() )
				tx = session.getTransaction();
			else{
				tx = session.beginTransaction();
				bNuevaSesionENS = true;
			}
			
			iLastNumRec = Integer.parseInt((session
				.createQuery("select f.id.nnn005 from F0002 f where f.id.nnsy = '03'")
				.uniqueResult()).toString());
			
			if(bNuevaSesionENS)
				tx.commit();	
			
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			try {
				if (bNuevaSesionENS)
					HibernateUtilPruebaCn.closeSession(session);
			}
			catch (Exception e) { e.printStackTrace(); }
		}
		return iLastNumRec;
	}
/*******************************************************************************/
/*************************OBTENER FACTURA DEL F0311*********************************************/
	/*public F0311JDBC obtenerFacturaF0311(int iNoFactura,String sPartida, String sTipoFact, int iCodCli){
		F0311JDBC f0311 = null;
		Connection cnAs400 = null;
		String sql = null;
		PreparedStatement ps = null;
		//Session session = HibernateUtil.getSessionFactory"+PropertiesSystem.JDEDTA+"().openSession();
		//Transaction tx = session.beginTransaction();	
		try{
			sql = "select * from "+PropertiesSystem.JDEDTA+".f0311 where rpdoc = "+iNoFactura+" AND rpsfx = '"+sPartida+"' AND rpdct = '"+sTipoFact+"' and rpan8 = "+iCodCli;
			cnAs400 = (Connection)m.get("cnAs400");
			ps = cnAs400.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if (rs.next()){
				f0311 = new F0311JDBC(
							new String(rs.getBytes("rpkco"),"Cp1047"),//1
							rs.getInt("rpan8"),//2
							new String(rs.getBytes("rpdct"),"Cp1047"),//3
							rs.getInt("rpdoc"),//4
							new String(rs.getBytes("rpsfx"),"Cp1047"),//5
							rs.getInt("rpdivj"),//6
							new String(rs.getBytes("rpdctm"),"Cp1047"),//7
							rs.getInt("rpdocm"),//8
							new String(rs.getBytes("rpsfxm"),"Cp1047"),//9
							rs.getInt("rpdmtj"),//10
							rs.getInt("rpdgj"),//11
							rs.getInt("rpfy"),//12
							rs.getInt("rpctry"),//13
							rs.getInt("rppn"),//14
							new String(rs.getBytes("rpco"),"Cp1047"),//15
							new String(rs.getBytes("rpicut"),"Cp1047"),//16
							rs.getInt("rpicu"),//17
							rs.getInt("rpdicj"),//18
							rs.getInt("rppa8"),//19
							rs.getInt("rpan8j"),//20
							new String(rs.getBytes("rppost"),"Cp1047"),//21
							new String(rs.getBytes("rpbalj"),"Cp1047"),//22
							new String(rs.getBytes("rppst"),"Cp1047"),//23
							rs.getLong("rppag"),//24
							rs.getLong("rpaap"),//25
							rs.getLong("rpadsc"),//26
							rs.getLong("rpadsa"),//27
							rs.getLong("rpatxa"),//28
							rs.getLong("rpatxn"),//29
							rs.getLong("rpstam"),//30
							new String(rs.getBytes("rpcrrm"),"Cp1047"),//31
							new String(rs.getBytes("rpcrcd"),"Cp1047"),//32
							rs.getBigDecimal("rpcrr").doubleValue(),//33
							rs.getLong("rpacr"),//34
							rs.getLong("rpfap"),//35
							rs.getLong("rpcds"),//36
							rs.getLong("rpcdsa"),//37
							rs.getLong("rpctxa"),//38
							rs.getLong("rpctxn"),//39
							rs.getLong("rpctam"),//40
							new String(rs.getBytes("rptxa1"),"Cp1047"),//41
							new String(rs.getBytes("rpexr1"),"Cp1047"),//42
							rs.getInt("rpdsvj"),//43
							new String(rs.getBytes("rpglc"),"Cp1047"),//44
							new String(rs.getBytes("rpglba"),"Cp1047"),//45
							new String(rs.getBytes("rpam"),"Cp1047"),//46
							new String(rs.getBytes("rpaid2"),"Cp1047"),//47
							new String(rs.getBytes("rpam2"),"Cp1047"),//48
							new String(rs.getBytes("rpmcu"),"Cp1047"),//49
							new String(rs.getBytes("rpobj"),"Cp1047"),//50
							new String(rs.getBytes("rpsub"),"Cp1047"),//51
							new String(rs.getBytes("rpsblt"),"Cp1047"),//52
							new String(rs.getBytes("rpsbl"),"Cp1047"),//53
							new String(rs.getBytes("rpbaid"),"Cp1047"),//54
							new String(rs.getBytes("rpcm"),"Cp1047"),//55
							new String(rs.getBytes("rpptc"),"Cp1047"),//56
							rs.getInt("rpddj"),//57
							rs.getInt("rpddnj"),//58
							rs.getInt("rpdprj"),//59
							rs.getInt("rpsmtj"),//60	
							rs.getInt("rpcldj"),//61
							rs.getInt("rpitij"),//62
							rs.getInt("rprddj"),//63
							rs.getInt("rprdsj"),//64
							new String(rs.getBytes("rpnbrr"),"Cp1047"),//65
							new String(rs.getBytes("rprdrl"),"Cp1047"),//66
							rs.getShort("rprmds"),//67
							new String(rs.getBytes("rpcoll"),"Cp1047"),//68
							new String(rs.getBytes("rpclrc"),"Cp1047"),//69
							new String(rs.getBytes("rpafc"),"Cp1047"),//70
							new String(rs.getBytes("rpnsf"),"Cp1047"),//71
							new String(rs.getBytes("rpar"),"Cp1047"),//72
							new String(rs.getBytes("rptrtc"),"Cp1047"),//73
							new String(rs.getBytes("rpprt1"),"Cp1047"),//74
							rs.getInt("rpodoc"),//75
							new String(rs.getBytes("rpodct"),"Cp1047"),//76
							new String(rs.getBytes("rposfx"),"Cp1047"),//77
							new String(rs.getBytes("rpcrc"),"Cp1047"),//78
							rs.getInt("rpvldt"),//79
							new String(rs.getBytes("rpvinv"),"Cp1047"),//80
							new String(rs.getBytes("rppo"),"Cp1047"),//81
							new String(rs.getBytes("rpdcto"),"Cp1047"),//82
							rs.getInt("rplnid"),//83
							new String(rs.getBytes("rpsfxo"),"Cp1047"),//84
							new String(rs.getBytes("rpsdct"),"Cp1047"),//85
							rs.getInt("rpsdoc"),//86
							rs.getInt("rpopsq"),//87
							rs.getInt("rpcmc1"),//88
							new String(rs.getBytes("rpvr01"),"Cp1047"),//89
							rs.getInt("rpnumb"),//90
							new String(rs.getBytes("rpunit"),"Cp1047"),//91	
							new String(rs.getBytes("rpmcu2"),"Cp1047"),//92
							new String(rs.getBytes("rprmk"),"Cp1047"),//93
							new String(rs.getBytes("rprf"),"Cp1047"),//94
							rs.getShort("rpdrf"),//95
							new String(rs.getBytes("rpctl"),"Cp1047"),//96
							new String(rs.getBytes("rpfnlp"),"Cp1047"),//97
							rs.getLong("rpu"),//98
							new String(rs.getBytes("rpum"),"Cp1047"),//99
							new String(rs.getBytes("rpalt6"),"Cp1047"),//100
							new String(rs.getBytes("rppyin"),"Cp1047"),//101
							new String(rs.getBytes("rprp1"),"Cp1047"),//102
							new String(rs.getBytes("rprp2"),"Cp1047"),//103
							new String(rs.getBytes("rprp3"),"Cp1047"),//104
							new String(rs.getBytes("rpalph"),"Cp1047"),//105
							new String(rs.getBytes("rpac01"),"Cp1047"),//106
							new String(rs.getBytes("rpac02"),"Cp1047"),//107
							new String(rs.getBytes("rpac03"),"Cp1047"),//108
							new String(rs.getBytes("rpac04"),"Cp1047"),//109
							new String(rs.getBytes("rpac05"),"Cp1047"),//110
							new String(rs.getBytes("rpac06"),"Cp1047"),//111
							new String(rs.getBytes("rpac07"),"Cp1047"),//112
							new String(rs.getBytes("rpac08"),"Cp1047"),//113
							new String(rs.getBytes("rpac09"),"Cp1047"),//114
							new String(rs.getBytes("rpac10"),"Cp1047"),//115
							new String(rs.getBytes("rpate"),"Cp1047"),//116
							new String(rs.getBytes("rpatr"),"Cp1047"),//117
							new String(rs.getBytes("rpatp"),"Cp1047"),//118
							new String(rs.getBytes("rpato"),"Cp1047"),//119
							new String(rs.getBytes("rpatpr"),"Cp1047"),//120
							new String(rs.getBytes("rpat1"),"Cp1047"),//121
							new String(rs.getBytes("rpat2"),"Cp1047"),//122
							new String(rs.getBytes("rpat3"),"Cp1047"),//123
							new String(rs.getBytes("rpat4"),"Cp1047"),//124
							new String(rs.getBytes("rpat5"),"Cp1047"),//125
							new String(rs.getBytes("rptorg"),"Cp1047"),//126
							new String(rs.getBytes("rpuser"),"Cp1047"),//127
							new String(rs.getBytes("rppid"),"Cp1047"),//128
							rs.getInt("rpupmj"),//129
							rs.getInt("rpupmt"),//130
							new String(rs.getBytes("rpjobn"),"Cp1047"),//131
							new String(rs.getBytes("rpdmcd"),"Cp1047"),//132
							new String(rs.getBytes("rpskco"),"Cp1047"),//133
							new String(rs.getBytes("rppdct"),"Cp1047"),//134
							new String(rs.getBytes("rppkco"),"Cp1047"),//135
							new String(rs.getBytes("rpokco"),"Cp1047"),//136
							rs.getInt("rpitm"),//137
							new String(rs.getBytes("rpbktr"),"Cp1047"),//138
							rs.getBigDecimal("rphcrr").doubleValue(),	//139
							rs.getInt("rphdgj")//140
				);
			}
			/*f0311 = (F0311)session
				.createQuery("from F0311 f where f.id.rpdoc = :pNoFact and f.id.rpsfx = :pPartida and f.id.rpdct = :pTipoFac and f.id.rpan8 = :pCodCli")
				.setParameter("pNoFact", iNoFactura)
				.setParameter("pPartida", sPartida)
				.setParameter("pTipoFac", sTipoFact)
				.setParameter("pCodCli", iCodCli)		
				.uniqueResult();
			tx.commit();*/	
		/*}catch(Exception ex){
			System.out.print("=>Excepcion capturada en obtenerBatchActual: " + ex);
		}/*finally{
        	session.close();        
		}*/
		/*return f0311;
	}
/************************OBTENER CUENTA DE METODO DE PAGO********************************************/
	public F55ca011 obtenerCuenta(int iCajaId,String sCodComp,String sCodMoneda,String sMetodo){
		F55ca011 f55ca011 = null;
		Session session = HibernateUtilPruebaCn.currentSession();
		
		boolean bBatchUpdated = false;
		try{
		
			f55ca011 = (F55ca011)session
				.createQuery("from F55ca011 f where f.id.c1id = :pCajaId and f.id.c1rp01 = :pCodComp and f.id.c1crcd = :pCodMon and f.id.c1ryin = :pMetodo")
				.setParameter("pCajaId", iCajaId)
				.setParameter("pCodComp", sCodComp)
				.setParameter("pCodMon", sCodMoneda)
				.setParameter("pMetodo", sMetodo)			
				.uniqueResult();
			
		}catch(Exception ex){
			LogCajaService.CreateLog("=>Excepcion capturada en obtenerBatchActual: ", "ERR", ex.getMessage());
			
		}finally{
			try{session.close();}catch(Exception ex2){ex2.printStackTrace();};
		}
		
		return f55ca011;
	}
/****************************************************************************************************/
/************************OBTENER ID DE CUENTA DEL F0901********************************************/
		public String obtenerIdCuenta(String sMCU, String sOBJ, String sSUB){
			String idCaja = null;
			//Session session = HibernateUtil.getSessionFactory"+PropertiesSystem.JDEDTA+"().openSession();
			//Transaction tx = session.beginTransaction();		
			boolean bBatchUpdated = false;
			Connection cnAs400 = null;
			String sql = null;
			PreparedStatement ps = null;
			try{
				sql = "select CAST(GMAID AS CHAR(8) CCSID 37) as GMAID from "+PropertiesSystem.JDEDTA+".f0901 where TRIM(GMMCU) = '"+sMCU.trim()+"'  AND GMOBJ = '"+sOBJ.trim()+"' AND GMSUB = '"+sSUB.trim()+"'";
				cnAs400 = (Connection)m.get("cnAs400");
				ps = cnAs400.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();
				if (rs.next()){
					idCaja = rs.getString("GMAID");
				}
				/*f55ca011 = (F55ca011)session
					.createQuery("select f.id.gmaid from F0109 f where f.id.gmmcu = :pMcu and f.id.gmobj = :pObj and f.id.gmsub = :pSub")
					.setParameter("pMcu", sMCU)
					.setParameter("pObj", sOBJ)
					.setParameter("pSub", sSUB)			
					.uniqueResult();
				tx.commit();*/	
			}catch(Exception ex){
				
				LogCajaService.CreateLog("=>Excepcion capturada en obtenerIdCuenta: ", "ERR", ex.getMessage());

			}/*finally{
	        	session.close();        
			}*/	
			return idCaja;
		}
/****************************************************************************************************/
/************************OBTENER NUMERO DE BATCH ACTUAL**************************************************/
	public int obtenerBatchActual(){
		int iLastBatch = 0;
		
//		Session session = HibernateUtilPruebaCn.currentSessionENS();
		Session session = HibernateUtilPruebaCn.currentSession();
		
		
		Transaction tx = null;
		boolean bNuevaSesionENS = false;
		boolean bBatchUpdated = false;
		
		try{
			if( session.getTransaction().isActive() )
				tx = session.getTransaction();
			else{
				tx = session.beginTransaction();
				bNuevaSesionENS = true;
			}
			
			iLastBatch = Integer.parseInt((session
				.createQuery("select f.id.nnn001 from F0002 f where f.id.nnsy = '00'")
				.uniqueResult()).toString());
			
			if(bNuevaSesionENS)
				tx.commit();	

		}catch(Exception ex){
			LogCajaService.CreateLog("=>Excepcion capturada en obtenerBatchActual: ", "ERR", ex.getMessage());
		}finally{
			try {
				if (bNuevaSesionENS)
//					HibernateUtilPruebaCn.closeSessionENS();
					HibernateUtilPruebaCn.closeSession(session);
			}
			catch (Exception e) { e.printStackTrace(); }
		}
		return iLastBatch;
	}
	

	
/****************************************************************************************************/
	//---------------------------------------------------------------------------------------
	
	
	//Registra Cambio a Cliente
	public void registrarCambio() {
		Divisas divisas = new Divisas();
		
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = session.beginTransaction();
		
		try{

			Cambiodet cambiodet = new Cambiodet();
			CambiodetId cambioid = new CambiodetId();
			
			//Regitrar COR		
			int numcc = getUltimoCambio() + 1;
			cambiodet.setCambio(BigDecimal.valueOf((divisas.formatStringToDouble(m.get("CambioCOR").toString()))));			
			
			cambioid.setNumrec(Integer.parseInt(m.get("ReciboActual").toString()));
			cambioid.setCodcomp(m.get("sCompania").toString());
			cambioid.setMoneda("COR");
			cambiodet.setId(cambioid);		
			
			session.save(cambiodet);
			tx.commit();
			
			//-------------------------------
			
			//Regitra USD
			numcc++;
			cambiodet.setCambio(BigDecimal.valueOf((divisas.formatStringToDouble(m.get("CambioUSD").toString()))));			
			
			cambioid.setNumrec(Integer.parseInt(m.get("ReciboActual").toString()));
			cambioid.setCodcomp(m.get("sCompania").toString());
			cambioid.setMoneda("USD");
			cambiodet.setId(cambioid);		
			
			session.save(cambiodet);
			tx.commit();
						
			
		}catch(Exception ex){
			tx.rollback();
			LogCajaService.CreateLog("No se pudo registrar Cambio a Cliente -> registrarCambio: ", "ERR", ex.getMessage());

		}finally{
			try{session.close();}catch(Exception ex2){ex2.printStackTrace();};   
		}
	}
	
	
	//---------------------------------------------------------------------------------------
		
	
	//Obtiene ultimo Cambio a Cliente
	public int getUltimoCambio() {
		int lastnum = 1;
		
		Session session = HibernateUtilPruebaCn.currentSession();
		
		
		try{
		
			Long result = (Long) session
				.createQuery("select max(cc.numcam) from Cambiodet as cc")
				.uniqueResult();
		
			lastnum = result.intValue();
			
		}catch(Exception ex){
			LogCajaService.CreateLog("No se pudo obtener ultimo cambio -> getUltimoCambio:  ", "ERR", ex.getMessage());

		}finally{
			try{session.close();}catch(Exception ex2){ex2.printStackTrace();};
		}
		return lastnum;
	}
	
	
	//---------------------------------------------------------------------------------------
	
		

//	Obtiene Ultimo Recibo
	public int obtenerUltimoRecibo(int iCaid,String sCodComp){
	
		Session session = HibernateUtilPruebaCn.currentSession();
		
		int ultimo = 0;
		
		try{
			Long result = (Long) session
			.createQuery("SELECT max(f55.c4nncu) FROM F55ca014 as f55 where f55.id.c4id = " + iCaid + " and trim(f55.id.c4rp01) = '"+sCodComp.trim()+"'")
			.uniqueResult();
		
			ultimo = result.intValue();
			
		}catch(Exception ex){
		
			LogCajaService.CreateLog("Se capturo una excepcion en obtenerUltimoRecibo:", "ERR", ex.getMessage());

		}finally{
			try{session.close();}catch(Exception ex2){ex2.printStackTrace();};
		}
		
		return ultimo;
	}
	
	
	//---------------------------------------------------------------------------------------
	
	
	
	//Registra Detalle de Recibo
	public boolean llenarMetodosPago2(int iNumrec, int iNumrecm, String codcomp){
		
		//MetodosPago
		Divisas divisas = new Divisas();
		List metodosPago = (List)m.get("lstPagos");
		Set lstMetodos = new HashSet();
		
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = session.beginTransaction();
		
		MetodosPago mPago = null;
		boolean insertado = false;
	
		try{		
			
			for (int i = 0; i < metodosPago.size();i++){
				mPago = (MetodosPago)metodosPago.get(i);
				Recibodet recibodet = new Recibodet();
				RecibodetId recibodetid = new RecibodetId();
				
				recibodetid.setNumrec(iNumrec);						//No. Recibo Automatico				
				//recibodetid.setNumrecm(iNumrecm);					//No. Recibo Manual
				recibodetid.setCodcomp(codcomp);					//Compania	
				recibodetid.setMoneda(mPago.getMoneda());			//Moneda			
				recibodetid.setMpago(mPago.getMetodo());			//Metodo
				recibodetid.setRefer1(mPago.getReferencia());
				recibodetid.setRefer2(mPago.getReferencia2());
				recibodetid.setRefer3(mPago.getReferencia3());
				recibodetid.setRefer4(mPago.getReferencia4());
				recibodet.setId(recibodetid);
								
				recibodet.setTasa(BigDecimal.valueOf((divisas.formatStringToDouble(mPago.getTasa().toString()))));
				recibodet.setMonto(BigDecimal.valueOf(mPago.getMonto()));
				recibodet.setEquiv(BigDecimal.valueOf(mPago.getEquivalente()));
				
								
				session.save(recibodet);
			}
			
			tx.commit();
			insertado = true;
			
		}catch(Exception ex){
			tx.rollback();
		
			LogCajaService.CreateLog("Excepcion capturada en llenarMetodosPago2 : ", "ERR", ex.getMessage());

		}finally{
			try{session.close();}catch(Exception ex2){ex2.printStackTrace();};
		}
		
		return insertado;
	}

	
	//---------------------------------------------------------------------------------------
	
	
	
	//Actualiza numero de recibo actual
	public boolean actualizarNumeroRecibo(int iCajaId, String sCodCom, int iNumRecActual){
		boolean actualizado = false;
		
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = session.beginTransaction();
		
		try{
			F55ca014 rango = (F55ca014) session
			.createQuery("FROM F55ca014 f55 WHERE f55.id.c4id = :pCod and f55.id.c4rp01 = :pCom")
			.setParameter("pCod", iCajaId)
			.setParameter("pCom", sCodCom)
			.uniqueResult();
		
			rango.getId().setC4nncu(iNumRecActual);			
			
			session.update(rango);			
			tx.commit();	
			actualizado = true;
		}catch(Exception ex){
			tx.rollback();
			System.out.print("Se capturo una excepcion en el actualizarNumeroRecibo: " + ex);
		}finally{
			try{session.close();}catch(Exception ex2){ex2.printStackTrace();};      
		}	
		return actualizado;
	}

	

	//---------------------------------------------------------------------------------------
	
	
	//Tabla Asociativa - FAC -> REC
	public boolean fillEnlaceReciboFac(int iNumRec, String sPartida, String codcomp, int[] iNumFac,double[] Monto, int iCodcli, int iFecha){
		boolean filled = false;
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = session.beginTransaction();
		
		try{
			for (int i = 0; i < iNumFac.length; i++){
				Recibofac recibofac = new Recibofac();
				RecibofacId recibofacid = new RecibofacId();
				
				recibofacid.setNumfac(iNumFac[i]);		//Numero factura
				recibofacid.setNumrec(iNumRec);			//Numero Recibo Automatico
				//recibofacid.setNumrecm(iNumRecm);		//Numero Recibo Manual
				recibofacid.setCodcomp(codcomp);		//Cod. Compañía
				recibofacid.setPartida(sPartida);
				recibofacid.setCodcli(iCodcli);
				recibofacid.setFecha(iFecha);
				
				recibofac.setId(recibofacid);
				recibofac.setMonto(BigDecimal.valueOf(Monto[i]));
				session.save(recibofac);
			}
			tx.commit();
			filled = true;
		}catch(Exception ex){
			System.out.print("Se capturo una excepcion en fillEnlaceReciboFac: " + ex);
		}finally{
			try{session.close();}catch(Exception ex2){ex2.printStackTrace();};
		}
		return filled;
	}
	
	
	
	//---------------------------------------------------------------------------------------
	
	
	//Cancela Recibo
	public void cancelarRecibo(ActionEvent e) {				
		lblcliente = "";
		lblTotal = "";
		lblMonto = "";
		lblCambio = "";
		List xlist = null;		
		m.put("lstPagos", xlist);
		m.remove("lstPagos");		
		dwRecibo.setWindowState("hidden");
	}
	

	//---------------------------------------------------------------------------------------
	
	
	//Cierra popup de validacion
	public void cerrarValida(ActionEvent e) {		
		dwFactura.setWindowState("hidden");
	}
	
	
	//---------------------------------------------------------------------------------------
	
	
	//Cierra ventana mensaje
	public void cerrarProcesa(ActionEvent e) {		
		dwValida.setWindowState("hidden");		
	}

	
	//---------------------------------------------------------------------------------------
	
	
	//Cancela Autorizacion
	public void cancelarAutoriza(ActionEvent e) {
	
		
	}
	
	//Ciera ventana mensaje
	public void cerrarMensaje(ActionEvent e) {
		
		dwValida2.setWindowState("hidden");		
	}

	
	
	//---------------------------------------------------------------------------------------
	
	
	//Registra Pagos de Recibo
	public Set llenarMetodosPago(int iNumrec){
		//MetodosPago
		List metodosPago = (List)m.get("mpagos");
		Set lstMetodos = new HashSet();
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = session.beginTransaction();
		MetodosPago mPago = null;
		
		try{		
			for (int i = 0; i < metodosPago.size();i++){
				mPago = (MetodosPago)metodosPago.get(i);
				Recibodet recibodet = new Recibodet();
				
				//recibodet.setNumrec(iNumrec);
				recibodet.getId().setNumrec(iNumrec);				
				//recibodet.setMoneda(mPago.getMoneda());
				recibodet.getId().setMoneda(mPago.getMoneda());				
				//recibodet.setMpago(mPago.getMetodo());
				recibodet.getId().setMpago(mPago.getMetodo());				
				recibodet.setMonto(BigDecimal.valueOf(mPago.getMonto()));
				recibodet.setEquiv(BigDecimal.valueOf(mPago.getEquivalente()));
				recibodet.getId().setRefer1(mPago.getReferencia());
				recibodet.getId().setRefer2(mPago.getReferencia2());
				recibodet.getId().setRefer3(mPago.getReferencia3());
				recibodet.getId().setRefer4(mPago.getReferencia4());
				
				session.save(recibodet);	
				lstMetodos.add(recibodet);
			}
			tx.commit();
			
		}catch(Exception ex){
			System.out.println("Excepcion capturada en registrarMetodos : " + ex);
		}finally{
			try{session.close();}catch(Exception ex2){ex2.printStackTrace();};
		}
		return lstMetodos;
	}
	

	//--------------------------------------------------------------------
/*********************************************************************************/
	public void cancelarImpresion(ActionEvent e) {
		dwRecibo.setWindowState("hidden");
		dwImprime.setWindowState("hidden");
	}
/*********************************************************************************/
	public void ImprimeReciboCredito(ActionEvent e){
		try {
			dwImprime.setWindowState("hidden");
			dwRecibo.setWindowState("hidden");
			FacesContext.getCurrentInstance().getExternalContext().redirect("/GCPMCAJA/reportes/reciboCredito.faces");
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
/*********************************************************************************/
	
	//Valida tipo de Recibo
	public void setTipoRecibo(ValueChangeEvent e) {
		SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();		
		String tipo = getCmbTiporecibo().getValue().toString();	
		m.put("mTipoRecibo", tipo);
		
		//Tipo Manual
		if(tipo.equals("MANUAL")) {
			
			lblNumrec = "Número Recibo: "; 
			fechaRecibo = "";
			lblNumeroRecibo = "";
			txtNumRec.setStyle("display:inline");
			txtFecham.setValue(new Date());	
			txtFecham.setStyle("display:inline");
			
		} else {
			
			lblNumrec = "Último Recibo: "; 
			getLblNumeroRecibo();
			txtNumRec.setStyle("visibility:hidden");
			txtFecham.setStyle("visibility:hidden");
		}
		srm.addSmartRefreshId(lblNumrec2.getClientId(FacesContext.getCurrentInstance()));
		srm.addSmartRefreshId(lblNumeroRecibo2.getClientId(FacesContext.getCurrentInstance()));
		
	}
	
	
	//-------------------------------------------------------------------------------

	//Imprime Fecha Recibo
	public String getFechaRecibo() {
		if (fechaRecibo == null) {			
			Date fecharecibo = new Date();
			Format formatter = new SimpleDateFormat("dd/MM/yyyy");
			fechaRecibo = formatter.format(fecharecibo);			
		}
		return fechaRecibo;
	}

	
	//TC de Factura
	public String getLblTasaCambio() {
		if(m.get("mTasaCambio") != null) {
			lblTasaCambio = m.get("mTasaCambio").toString();
		}	
		return lblTasaCambio;
	}

	
	//-------------------------------------------------------------
	
	//Imprime Ultimo Recibo
	public String getLblNumeroRecibo() {
				
		if(m.get("ReciboActual") == null) {			
			CtrlCajas xpcaj = new CtrlCajas();
			lblNumeroRecibo = xpcaj.getLblNumeroRecibo();
			
		} else {
			lblNumeroRecibo = m.get("ReciboActual").toString();
		}

		return lblNumeroRecibo;
	}
	
	
	//Guarda Recibo Manual
	public void setNumeroRec(ValueChangeEvent e) {
		if(!getTxtNumRec().getValue().equals("")) {
			m.put("mReciboManual", getTxtNumRec().getValue().toString());
		}		
	}
	
	//----------------------------------------------------------------------------------------
	

	public Double getCambio() {
		return cambio;
	}
	public void setCambio(Double cambio) {
		this.cambio = cambio;
	}
	public DialogWindow getDwImprime() {
		return dwImprime;
	}
	public void setDwImprime(DialogWindow dwImprime) {
		this.dwImprime = dwImprime;
	}
	public DialogWindow getDwMensaje() {
		return dwMensaje;
	}
	public void setDwMensaje(DialogWindow dwMensaje) {
		this.dwMensaje = dwMensaje;
	}
	public DialogWindow getDwProcesa() {
		return dwProcesa;
	}
	public void setDwProcesa(DialogWindow dwProcesa) {
		this.dwProcesa = dwProcesa;
	}
	public DialogWindow getDwRecibo() {
		return dwRecibo;
	}
	public void setDwRecibo(DialogWindow dwRecibo) {
		this.dwRecibo = dwRecibo;
	}
	public DialogWindow getDwSolicitud() {
		return dwSolicitud;
	}
	public void setDwSolicitud(DialogWindow dwSolicitud) {
		this.dwSolicitud = dwSolicitud;
	}
	public DialogWindow getDwValida() {
		return dwValida;
	}
	public void setDwValida(DialogWindow dwValida) {
		this.dwValida = dwValida;
	}	
	public void setFechaRecibo(String fechaRecibo) {
		this.fechaRecibo = fechaRecibo;
	}
	public BigDecimal getIva() {
		return iva;
	}
	public void setIva(BigDecimal iva) {
		this.iva = iva;
	}
	public String getLblCambio() {
		return lblCambio;
	}
	public void setLblCambio(String lblCambio) {
		this.lblCambio = lblCambio;
	}
	public UIOutput getLblCambio2() {
		return lblCambio2;
	}
	public void setLblCambio2(UIOutput lblCambio2) {
		this.lblCambio2 = lblCambio2;
	}
	public String getLblcliente() {
		return lblcliente;
	}
	public void setLblcliente(String lblcliente) {
		this.lblcliente = lblcliente;
	}
	public String getLblMonto() {
		return lblMonto;
	}
	public void setLblMonto(String lblMonto) {
		this.lblMonto = lblMonto;
	}
	public UIOutput getLblMontoRecibido2() {
		return lblMontoRecibido2;
	}
	public void setLblMontoRecibido2(UIOutput lblMontoRecibido2) {
		this.lblMontoRecibido2 = lblMontoRecibido2;
	}
	public void setLblTasaCambio(String lblTasaCambio) {
		this.lblTasaCambio = lblTasaCambio;
	}
	public String getLblTotal() {
		return lblTotal;
	}
	public void setLblTotal(String lblTotal) {
		this.lblTotal = lblTotal;
	}
	public BigDecimal getMontoRecibido() {
		return montoRecibido;
	}
	public void setMontoRecibido(BigDecimal montoRecibido) {
		this.montoRecibido = montoRecibido;
	}
	public Double getTasaCambio() {
		return tasaCambio;
	}
	public void setTasaCambio(Double tasaCambio) {
		this.tasaCambio = tasaCambio;
	}
	public Double getTotalFactura() {
		return totalFactura;
	}
	public void setTotalFactura(Double totalFactura) {
		this.totalFactura = totalFactura;
	}
	public UIInput getTxtTasaCambio() {
		return txtTasaCambio;
	}
	public void setTxtTasaCambio(UIInput txtTasaCambio) {
		this.txtTasaCambio = txtTasaCambio;
	}
	public UIInput getTxtCliente() {
		return txtCliente;
	}
	public void setTxtCliente(UIInput txtCliente) {
		this.txtCliente = txtCliente;
	}
	public void setLblNumeroRecibo(String lblNumeroRecibo) {
		this.lblNumeroRecibo = lblNumeroRecibo;
	}
	public UIInput getCmbTiporecibo() {
		return cmbTiporecibo;
	}
	public void setCmbTiporecibo(UIInput cmbTiporecibo) {
		this.cmbTiporecibo = cmbTiporecibo;
	}
	public List getLstTiporecibo() {
		if(lstTiporecibo == null){
			lstTiporecibo = new ArrayList();	
			lstTiporecibo.add(new SelectItem("AUTOMATICO","AUTOMATICO"));
			lstTiporecibo.add(new SelectItem("MANUAL","MANUAL"));			
		}
		return lstTiporecibo;
	}
	public void setLstTiporecibo(List lstTiporecibo) {
		this.lstTiporecibo = lstTiporecibo;
	}
	public UIInput getTxtConcepto() {
		return txtConcepto;
	}
	public void setTxtConcepto(UIInput txtConcepto) {
		this.txtConcepto = txtConcepto;
	}	
	public UIOutput getLblNumeroRecibo2() {
		return lblNumeroRecibo2;
	}
	public void setLblNumeroRecibo2(UIOutput lblNumeroRecibo2) {
		this.lblNumeroRecibo2 = lblNumeroRecibo2;
	}
	public String getLblNumrec() {
		return lblNumrec;
	}
	public void setLblNumrec(String lblNumrec) {
		this.lblNumrec = lblNumrec;
	}
	public UIOutput getLblNumrec2() {
		return lblNumrec2;
	}
	public void setLblNumrec2(UIOutput lblNumrec2) {
		this.lblNumrec2 = lblNumrec2;
	}
	public HtmlDateChooser getTxtFecham() {
		return txtFecham;
	}
	public void setTxtFecham(HtmlDateChooser txtFecham) {
		this.txtFecham = txtFecham;
	}
	public HtmlInputText getTxtNumRec() {
		return txtNumRec;
	}
	public void setTxtNumRec(HtmlInputText txtNumRec) {
		this.txtNumRec = txtNumRec;
	}
	public DialogWindow getDwValida2() {
		return dwValida2;
	}
	public void setDwValida2(DialogWindow dwValida2) {
		this.dwValida2 = dwValida2;
	}



	public DialogWindow getDwFactura() {
		return dwFactura;
	}



	public void setDwFactura(DialogWindow dwFactura) {
		this.dwFactura = dwFactura;
	}



	public UIOutput getLblValidaFactura() {
		return lblValidaFactura;
	}



	public void setLblValidaFactura(UIOutput lblValidaFactura) {
		this.lblValidaFactura = lblValidaFactura;
	}



	public UIOutput getLblValidaRecibo() {
		return lblValidaRecibo;
	}



	public void setLblValidaRecibo(UIOutput lblValidaRecibo) {
		this.lblValidaRecibo = lblValidaRecibo;
	}

}

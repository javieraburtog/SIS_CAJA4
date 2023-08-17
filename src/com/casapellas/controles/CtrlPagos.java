

/**
 * 
 	Creado por: Alberto Zapata
 	Fecha de Cracion: 09/12/2008
 	Ultima Modificacion: 10/12/2008
 	Departamento: Informatica
 * 
 */

package com.casapellas.controles;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

//import org.apache.commons.mail.MultiPartEmail;
import org.hibernate.Query;
import org.hibernate.Session;
//import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
//import org.w3c.dom.html.HTMLInputElement;

import com.casapellas.entidades.F55ca022;
import com.casapellas.entidades.Cafiliados;
//import com.casapellas.entidades.F55ca012;
//import com.casapellas.entidades.F55ca03;
//import com.casapellas.entidades.Hfactjdecon;
import com.casapellas.entidades.MetodosPago;
import com.casapellas.entidades.Solicitud;
import com.casapellas.entidades.SolicitudId;
import com.casapellas.entidades.Tcambio;
import com.casapellas.entidades.F55ca021;

import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.util.CustomEmailAddress;
import com.casapellas.util.Divisas;
//import com.casapellas.util.JulianToCalendar;
import com.casapellas.util.MailHelper;
//import com.casapellas.util.PropertiesSystem;
import com.infragistics.faces.grid.component.Cell;
import com.infragistics.faces.grid.component.GridView;
import com.infragistics.faces.grid.component.RowItem;
import com.infragistics.faces.grid.event.CellValueChangeEvent;
import com.infragistics.faces.input.component.html.HtmlCheckBox;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
import com.infragistics.faces.shared.smartrefresh.SmartRefreshManager;
import com.infragistics.faces.window.component.DialogWindow;


public class CtrlPagos {

	Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	
	//Datos del Pago
	private UIInput txtMonto;
	private HtmlInputText txtReferencia1;
	private HtmlInputText txtReferencia2;
	private HtmlInputText txtReferencia3;	
	public UIOutput lblReferencia1;
	public UIOutput lblReferencia2;
	public UIOutput lblReferencia3;	
	
	//Unidad de negocio
	private UIInput cmbUnegocio;
	private List lstUnegocio = null;
	
	//Monedas
	private UIInput cmbMoneda;
	private UIInput ddlMoneda;
	private List lstMoneda = null;
	
	//Metodos de pago
	private UIInput cmbMetodosPago;
	private UIInput ddlMetodoPago;
	private List lstMetodosPago = null;		
	
	//Grid metodos pago
	private GridView metodosGrid = null;
	private List selectedMet = null;
	public MetodosPago[] mpagos = null;	
	private GridView gvMetodosPago = null;
	
	//Montos
	public String lblTotal = "0.00";
	public String lblMontoRecibido ="0.00";
	public String lblCambio = "0.00";
	private UIOutput lblMontoRecibido2;
	
	//Afiliado
	private UIInput cmbAfiliado;
	private List lstAfiliado;	
	private HtmlDropDownList ddlAfiliado;
	private UIOutput lblAfiliado;
	
	//Bancos
	private UIInput cmbBanco;
	private List lstBanco;	
	private HtmlDropDownList ddlBanco;
	private UIOutput lblBanco;
	
	//Dialog Windows
	private DialogWindow dwValida;
	private DialogWindow dwAutoriza;	
	
	//Tasa cambio
	private UIInput txtTasaCambio;
	
	//Cambio a Cliente
	private HtmlInputText txtCambio;
	private HtmlCheckBox chkCambio;
	public boolean bCambio = false;	
	public UIOutput lblmonCambio;
	public String monCambio = "";
	public Double rCambio = 0.00;	
	public String lblRestoCOR = "";
	public UIOutput lblRestoCOR2;
	public UIOutput lblCambio2;
	private UIOutput lnkMoneda;
	
	
	//Mensaje de Validacion
	private UIOutput lblMensajeValida;
	private DialogWindow dwMensaje;
	
	
	//Solicitud de autorizacion
	public UIInput txtRefAutoriza;
	public UIInput cmbAutoriza;
	List lstAutoriza = null;
	public UIInput txtFecha;
	public UIInput txtObs;
	private UIOutput lblValidaSolicitud;
	private DialogWindow dwSolicitud;
	
	//Datos del correo
	private String mail = "192.168.1.2";
	private String from = "azapata@casapellas.com";
	private String to = "azapata@casapellas.com";
	private String cc = "azapata@casapellas.com";
	private String mensaje = "Solicitud de Autorización - MODULO DE CAJA.";
	
	
	
	//------------------------------------------------------------------------------------
	
	
	//Registra Metodos de pago
	public void registrarPago(ActionEvent e) {
			
		executePagos();
	}

	
	public void executePagos() {
		Divisas divisas = new Divisas();
		
		//formateador de numeros
		DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
		simbolos.setDecimalSeparator('.');
		simbolos.setGroupingSeparator(',');
		DecimalFormat dfDigitos = new DecimalFormat("#,###,##0.00",simbolos);
		SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
		
		BigDecimal tasa = new BigDecimal("1");
		Double  total, monto, equiv;
		String sTotal;
		int cont = 1;		
		double montoRecibido = 0;
		boolean flgregistrar = true;	
				
		boolean bsolic = true;
		boolean bMoneda = true;
		
		
		try {
			
			CtrlPoliticas xcp = new CtrlPoliticas();		
	
			//Validar monto recibido
			if(!getTxtMonto().getValue().equals("")) {
				
				//Valida Monto registrado
				//(?!^0*$)(?!^0*\\.0*$)^\\d{1,5}(\\.\\d{1,3})?$
				//^[0-9]+\\.[0-9]{1,2})$
				Pattern p = Pattern.compile( "(?!^0*$)(?!^0*\\.0*$)^\\d{1,5}(\\.\\d{1,2})?$" );
				String xmonto = getTxtMonto().getValue().toString();
				Matcher mat = p.matcher(xmonto);
				
				if(mat != null && mat.matches()==true) {	
					
					//Valida politicas para el monto registrado
					if(xcp.verificarMontos(m.get("sCajaId").toString(), m.get("cMoneda").toString(), m.get("sCompania").toString(), m.get("cMpagos").toString(), Double.valueOf(xmonto))==true) {
												
						String metodo = getCmbMetodosPago().getValue().toString();				
						String ref1 = getTxtReferencia1().getValue().toString();
						String ref2 = getTxtReferencia2().getValue().toString();
						String ref3 = getTxtReferencia3().getValue().toString();
						String ref4 = "";			
					
					//Valida CHK
					if(metodo.equals(MetodosPagoCtrl.CHEQUE)) {
						
						//Valida Referencias
						if(ref1.equals("") || ref2.equals("") || ref3.equals("")) {						
							getLblMensajeValida().setValue("Faltan Referencias.");
							flgregistrar = false;
									
						} else {						
							ref4 = getDdlBanco().getValue().toString();
														
							//Verifica que la solicitud se ha grabado
							if(m.get("rSolicitud").toString().equals("no")) {

								//Muestra ventana de solicitud
								dwAutoriza.setWindowState("normal");								
								flgregistrar = false;
								bsolic = false;
							}
						}
					
					//Valida TC	
					} else if(metodo.equals(MetodosPagoCtrl.TARJETA)) {
						
						//Valida Cedula
						if(ref1.equals("")) {
							getLblMensajeValida().setValue("Falta Cédula de Identificación.");
							flgregistrar = false;						
							
						} else {
							ref2 = getDdlAfiliado().getValue().toString();
						}
					}	

					//Valida referencias
					boolean flgregistrar2 = true;
					if(flgregistrar == true) {
							
							total = (Double) m.get("mTotalFactura");
							monto = Double.parseDouble(getTxtMonto().getValue().toString());
							equiv = monto;
												
							String moneda = getDdlMoneda().getValue().toString();						
							String monfactura = m.get("xMonedaF").toString();
							
							//Valida moneda factura/pago -> aplica tasa
							if(!monfactura.equals("COR")) {
	
								if (moneda.equals("COR")){							
									tasa = new BigDecimal(m.get("mTasaCambio").toString());
									total = total * Double.parseDouble(m.get("mTasaCambio").toString());
									equiv = monto/tasa.doubleValue();
									bMoneda = false;
									
								} else {
									equiv = monto;
								}
								
							} else if(monfactura.equals("COR")){
								
								if (!moneda.equals("COR")){							
									tasa =  new BigDecimal(m.get("mTasaCambio").toString());
									equiv = monto*tasa.doubleValue();
								} else {
									equiv = monto;
								}
								bMoneda = false;
							}
							montoRecibido = equiv;
							
							
							int cant = 1;
							boolean flgpagos = true;					
							List selectedMet = (List) m.get("lstPagos");
							
							if(selectedMet != null) {
								
								MetodosPago[] mpagos = new MetodosPago[selectedMet.size()];
								cant = selectedMet.size();								
								
								for(int i=0; i<cant; i++) {
									mpagos[i] = ((MetodosPago)selectedMet.get(i));							
								}
								
								for(int i=0; i<mpagos.length; i++) {							
//									System.out.println(mpagos[i].getMetodo().equals(metodo) + " == " + mpagos[i].getMoneda().equals(moneda));
									if(mpagos[i].getMetodo().equals(metodo) && mpagos[i].getMoneda().equals(moneda)) {
																				
										Double tmonto = monto + mpagos[i].getMonto();
										
										//Valida politica para el nuevo monto acumulado
										if(xcp.verificarMontos(m.get("sCajaId").toString(), m.get("cMoneda").toString(), m.get("sCompania").toString(), m.get("cMpagos").toString(), tmonto)==true) {
											
											monto = monto + mpagos[i].getMonto();
											equiv = equiv + mpagos[i].getEquivalente();									
											
											
											mpagos[i].setMonto(monto);
											
										
											mpagos[i].setEquivalente(equiv);
											flgpagos = false;
											
											montoRecibido = equiv;
											
										} else {
											flgregistrar2 = false;
											getLblMensajeValida().setValue("Nuevo Monto no cumple con políticas.");
											dwValida.setWindowState("normal");
										}
										
									} else {								
										montoRecibido += mpagos[i].getEquivalente();
									}									
								}
								
								
							} else {
								mpagos = new MetodosPago[1];
								selectedMet = new ArrayList();				
								mpagos[0] = new MetodosPago(metodo,moneda,monto,tasa,equiv,ref1,ref2,ref3,ref4);
								selectedMet.add(mpagos[0]);
								
								montoRecibido = equiv;
								flgpagos = false;
							}										
							
							if(flgpagos == true && flgregistrar2 == true) {
								cant++;							
								mpagos = new MetodosPago[cant];							
								mpagos[cant-1] = new MetodosPago(metodo,moneda,monto,tasa,equiv,ref1,ref2,ref3,ref4);							
								selectedMet.add(mpagos[cant-1]);
								//montoRecibido += equiv;							
								metodosGrid.dataBind();
							}						
							
							
							// --------------------------------------------------
							
							
							m.put("montoRecibibo", montoRecibido);					
							lblMontoRecibido = dfDigitos.format(montoRecibido);
							m.put("lstPagos", selectedMet);
							m.put("mpagos", selectedMet);
							m.put("InstPagos",selectedMet);
							
							total = (Double) m.get("mTotalFactura");					
							Double cambio = total - montoRecibido;
							m.put("mCambio", cambio);
							lblCambio = dfDigitos.format(cambio);				
							
//							System.out.println("Moneda #" + bMoneda + " MontoRec #" + montoRecibido + " Total #" + total);
							
							//Muestra la Opcion de Cambio a Cliente
							if(bMoneda == true && montoRecibido >= total) {
								chkCambio.setStyle("display:inline");								
								m.put("rCambio", "si");
								
							} else {
								m.put("rCambio", "no");
							}
//							System.out.println("rCambio en pagos -> " + m.get("rCambio").toString());
//							System.out.println( m.get("mTotalFactura").toString() + " == " + m.get("montoRecibibo").toString());
							
							//Setting Blank
							getTxtMonto().setValue("");
							getTxtReferencia1().setValue("");
							getTxtReferencia2().setValue("");
							getTxtReferencia3().setValue("");
						
							//Actualiza grid de pagos
							metodosGrid.dataBind();
							
											
						}
							
					} else {
						getLblMensajeValida().setValue("Monto no cumple con políticas.");
						dwValida.setWindowState("normal");						
					}
					
				} else {
					getLblMensajeValida().setValue("El Monto no es válido.");
					dwValida.setWindowState("normal");					
				}
				
			} else {
				getLblMensajeValida().setValue("Registre el Monto.");
				dwValida.setWindowState("normal");				
			}
				
			
		}catch(Exception ex){
//			System.out.print("Exception -> registrarPago" + ex);
			ex.printStackTrace();
		}
		
		
	}

	
	//-----------------------------------------------------------------
	
	
	//Elimina metodo de pago
	public void borrarPago(ActionEvent e){
		
		Divisas divisas = new Divisas();
		
		List lstPagos = (List)m.get("mpagos");
		selectedMet = new ArrayList();
		MetodosPago mPago = null, mPagoComp;
		double montoRecibido = 0;
		
		try{
			
			String monedaFactura = m.get("xMonedaF").toString();
			double dTasa =  Double.parseDouble(m.get("mTasaCambio").toString());
			//obtener fila y columnas a eliminar de grid
			RowItem ri = (RowItem)e.getComponent().getParent().getParent();
			List lstA = (List) ri.getCells();	
			//Columna a obtener: Metodo
			Cell c1 = (Cell) lstA.get(1);
			HtmlOutputText metodo = (HtmlOutputText) c1.getChildren().get(0);
			//Columna a obtener: Moneda
			Cell c2 = (Cell) lstA.get(2);
			HtmlOutputText moneda = (HtmlOutputText) c2.getChildren().get(0);
			//Columna a obtener: Monto
			Cell c3 = (Cell) lstA.get(3);
			HtmlOutputText monto = (HtmlOutputText) c3.getChildren().get(0);
			//Columna a obtener: Tasa
			Cell c4 = (Cell) lstA.get(4);
			HtmlOutputText tasa = (HtmlOutputText) c4.getChildren().get(0);
			//Columna a obtener: Equivalente
			Cell c5 = (Cell) lstA.get(5);
			HtmlOutputText equiv = (HtmlOutputText) c5.getChildren().get(0);
			//Columna a obtener: Referencia1
			Cell c6 = (Cell) lstA.get(6);
			HtmlOutputText ref1 = (HtmlOutputText) c6.getChildren().get(0);
			//Columna a obtener: Referencia2
			Cell c7 = (Cell) lstA.get(7);
			HtmlOutputText ref2 = (HtmlOutputText) c7.getChildren().get(0);
			//Columna a obtener: Referencia3
			Cell c8 = (Cell) lstA.get(8);
			HtmlOutputText ref3 = (HtmlOutputText) c8.getChildren().get(0);
			//Columna a obtener: Referencia4
			Cell c9 = (Cell) lstA.get(9);
			HtmlOutputText ref4 = (HtmlOutputText) c9.getChildren().get(0);
			
			mPago = new MetodosPago(
									metodo.getValue().toString(),
									moneda.getValue().toString(),
									Double.valueOf(monto.getValue().toString()),
									new BigDecimal(tasa.getValue().toString()),
									Double.valueOf(equiv.getValue().toString()),
									ref1.getValue().toString(),
									ref2.getValue().toString(),
									ref3.getValue().toString(),
									ref4.getValue().toString()
									);
			
			for (int i = 0; i < lstPagos.size();i++){
				mPagoComp = ((MetodosPago)lstPagos.get(i));
				if (mPago.getEquivalente()==mPagoComp.getEquivalente() && mPago.getMetodo().equals(mPagoComp.getMetodo()) && 
					mPago.getMoneda().equals(mPagoComp.getMoneda()) && mPago.getMonto()==mPagoComp.getMonto() && mPago.getReferencia().equals(mPagoComp.getReferencia()) &&
					mPago.getReferencia2().equals(mPagoComp.getReferencia2())){
					//no lo incluye en la lista
				}else{
					if (monedaFactura.equals("COR")){
						selectedMet.add(mPagoComp);
						//recalcular monto recibido
						
						if(mPagoComp.getMoneda().equals("COR")){
							montoRecibido = montoRecibido + mPagoComp.getMonto();
						}else{
							montoRecibido = montoRecibido + mPagoComp.getEquivalente();
						}
					//
					}else{
						selectedMet.add(mPagoComp);
						//recalcular monto recibido
						
						if(!mPagoComp.getMoneda().equals("COR")){
							montoRecibido = montoRecibido + mPagoComp.getMonto();
						}else{
							montoRecibido = montoRecibido + mPagoComp.getEquivalente();
						}
					}
					
				}
			}
			
			double total = (Double) m.get("mTotalFactura");					
			Double cambio = total - montoRecibido;
			m.put("mCambio", cambio);
			lblCambio = divisas.formatDouble(cambio);
			
			m.put("mpagos", selectedMet);	
			m.put("lstPagos", selectedMet);
			m.put("montoRecibibo", montoRecibido);
			lblMontoRecibido = divisas.formatDouble(montoRecibido);
			metodosGrid.dataBind();
		}catch(Exception ex){
//			System.out.println("Excepcion capturada en borrarPago: " + ex);
			ex.printStackTrace();
		}
	}


	//-----------------------------------------------------------------------------------
	
	
	//Modifica metodo de pago
	public void modificarPago(CellValueChangeEvent e){
		SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
		//Determine the row and column being updated
        List rows = metodosGrid.getRows();
        RowItem row  = (RowItem)rows.get(e.getPosition().getRow());
        int colpos = e.getPosition().getCol();
        double equiv = 0;
        BigDecimal tasa = new BigDecimal("0");
        Divisas divisas = new Divisas();
        String sMonto = "";
        try{
	        //obtener moneda de factura y de metodo de pago y tasa
        	String monedaCombo = cmbMoneda.getValue().toString();						
			String monedaFactura = m.get("xMonedaF").toString();
			tasa =  new BigDecimal(m.get("mTasaCambio").toString());
			
	        // Get the current data for that row.
	        MetodosPago mPago = (MetodosPago)metodosGrid.getDataRow(row);
	        List lstRows = (List)row.getCells();
	        MetodosPago mPagoOld = mPago;
	        
	        MetodosPago mPagoActual = null;
	        MetodosPago mPagoCompare = null;
	        List lstPagos = (List)m.get("mpagos");
			selectedMet = new ArrayList();
			List lstMetodos1 = new ArrayList();
			List lstMetodos2 = new ArrayList();
			double montoRecibido = 0;
			double newMonto = 0;
			
			String sMontoOld = null;
			
			boolean MontoCorrecto = true;
	        // Update the bean.
	        if(colpos==1)mPago.setMetodo(e.getNewValue().toString());
	        else if(colpos==2)mPago.setMoneda(e.getNewValue().toString());
	        else if(colpos==3){
	        	sMonto = e.getNewValue().toString();
	        	if(!e.getNewValue().toString().equals("")){
	        		Pattern p = Pattern.compile( "(?!^0*$)(?!^0*\\.0*$)^\\d{1,5}(\\.\\d{1,3})?$" );
	    			Matcher mat = p.matcher(sMonto);
	    			
	    			if (mat.matches()){
	    				CtrlPoliticas pol = new CtrlPoliticas();
	    				
	    				if(pol.verificarMontos(m.get("sCajaId").toString(), m.get("cMoneda").toString(), m.get("sCompania").toString(), m.get("cMetPago").toString(), Double.valueOf(sMonto)) == true) {	    				
	    					mPago.setMonto(divisas.formatStringToDouble(sMonto));
	    				}
	    				
	    			}else{
	    				MontoCorrecto = false;
	    				sMontoOld = e.getOldValue().toString();
	    			}	
	        	}else{	
	        		MontoCorrecto = false;
	        		sMontoOld = e.getOldValue().toString();
	        	}
	        }
	        else if(colpos==5)mPago.setEquivalente(divisas.formatStringToDouble(e.getNewValue().toString()));
	        else if(colpos==6)mPago.setReferencia(e.getNewValue().toString()); 
	        else if(colpos==7)mPago.setReferencia2(e.getNewValue().toString()); 
	        
	        if(MontoCorrecto){
		       
	        	if (monedaFactura.equals("COR")){
	        		if(!monedaFactura.equals(monedaCombo)){
	        			equiv = mPago.getMonto() * tasa.doubleValue();
						mPago.setEquivalente(equiv);
						mPago.setTasa(tasa);

	        		}
	        		else{
	        			mPago.setEquivalente(mPago.getMonto());
						mPago.setTasa(new BigDecimal(1));
	        		}
	        		
	        		for(int i = 0; i < lstPagos.size(); i++){
	 		        	mPagoActual = (MetodosPago)lstPagos.get(i);
	 		        	if(mPagoOld == mPagoActual){	
	 		        		mPago.setMonto(mPago.getMonto());
	 			        	selectedMet.add(mPago);
	 					    if(mPago.getMoneda().equals("COR")){
	 					    	montoRecibido = montoRecibido +mPago.getMonto();
	 						}else{
	 							montoRecibido = montoRecibido + mPago.getEquivalente();
	 						}
	 		        	}else{   		
	 		        		mPagoActual.setMonto(mPagoActual.getMonto());
	 			        	selectedMet.add(mPagoActual);
	 			        	if(mPagoActual.getMoneda().equals("COR")){
	 							montoRecibido = montoRecibido + mPagoActual.getMonto();
	 						}else{
	 							montoRecibido = montoRecibido + mPagoActual.getEquivalente();
	 						}
	 		        	}
	 		        	
	 		        }

	        	}else {
	        		if(!monedaFactura.equals(monedaCombo)){
	        			equiv = mPago.getMonto() / tasa.doubleValue();
						mPago.setEquivalente(equiv);
						mPago.setTasa(tasa);

	        		}
	        		else{
	        			mPago.setEquivalente(mPago.getMonto());
						mPago.setTasa(new BigDecimal("1"));
	        		}
	        		
	        		for(int i = 0; i < lstPagos.size(); i++){
	 		        	mPagoActual = (MetodosPago)lstPagos.get(i);
	 		        	if(mPagoOld == mPagoActual){	
	 		        		mPago.setMonto(mPago.getMonto());
	 			        	selectedMet.add(mPago);
	 					    if(!mPago.getMoneda().equals("COR")){
	 					    	montoRecibido = montoRecibido + mPago.getMonto();
	 						}else{
	 							montoRecibido = montoRecibido + mPago.getEquivalente();
	 						}
	 		        	}else{   		
	 		        		mPagoActual.setMonto(mPagoActual.getMonto());
	 			        	selectedMet.add(mPagoActual);
	 			        	if(!mPagoActual.getMoneda().equals("COR")){
	 							montoRecibido = montoRecibido + mPagoActual.getMonto();
	 						}else{
	 							montoRecibido = montoRecibido + mPagoActual.getEquivalente();
	 						}
	 		        	}	 		        	
	 		        }
	        	}
	        	
	        	double total = (Double) m.get("mTotalFactura");					
				Double cambio = total - montoRecibido;
				m.put("mCambio", cambio);
				lblCambio = divisas.formatDouble(cambio);
	        	
		        m.put("mpagos", selectedMet);
		        m.put("lstPagos", selectedMet);
		        m.put("montoRecibibo", montoRecibido);
				lblMontoRecibido = divisas.formatDouble(montoRecibido);
				metodosGrid.dataBind();
			
		        srm.addSmartRefreshId(metodosGrid.getClientId(FacesContext.getCurrentInstance()));
				srm.addSmartRefreshId(lblMontoRecibido2.getClientId(FacesContext.getCurrentInstance()));
	        }else{
	        	//selectedMet.add(mPagoOld);
	        	for (int h = 0; h < lstPagos.size(); h ++){
	        		mPagoCompare = (MetodosPago)(lstPagos.get(h));
	        		if (mPagoCompare.getMetodo().equals(mPagoOld.getMetodo()) && mPagoCompare.getEquivalente()==mPagoOld.getEquivalente()){
	        			mPagoCompare.setMonto(Double.valueOf(sMontoOld));
	        		}
	        		selectedMet.add(mPagoCompare);
	        	}
        		m.put("mpagos", selectedMet);
		        m.put("lstPagos", selectedMet);
        		metodosGrid.dataBind();
        		srm.addSmartRefreshId(metodosGrid.getClientId(FacesContext.getCurrentInstance()));
        		
        		dwValida.setWindowState("normal");
        		srm.addSmartRefreshId(dwValida.getClientId(FacesContext.getCurrentInstance()));
	        }

        }catch(Exception ex){
//			System.out.println("Excepcion Capturada en modificar Pago: " + ex);
        	ex.printStackTrace();
		}
		
	}
	

	
	//-------------------------------------------------------------------------
	
		
	//Lista Metodos de Pago por Caja
	public List getLstMetodosPago() {
		
		if(m.get("sCompania") == null) m.put("sCompania", "E01");
		if(m.get("cMoneda") == null) m.put("cMoneda", "COR");		
		if(m.get("rSolicitud") == null)	m.put("rSolicitud", "no");
		
        Session session = HibernateUtilPruebaCn.currentSession();
                
        try {
        	lstMetodosPago = new ArrayList();
        	List result = null;
        	        	
        	Query query = session        		
        		.createQuery("from Pagoscaja pc where pc.id.c2stat='A' and pc.id.c2id = :pCaja and pc.id.c2rp01 = :pCom and pc.id.c2crcd = :pMon")
        		.setParameter("pCaja", Integer.valueOf(m.get("sCajaId").toString()))
	        	.setParameter("pCom", m.get("sCompania").toString())
	        	.setParameter("pMon", m.get("cMoneda").toString());   	
	        result = query.list();   
        	
        }catch(Exception ex){
//			System.out.println("Se produjo una excepcion en getLstMetodosPago: " + ex);
        	ex.printStackTrace();
			
		}finally{
			try{session.close();}catch(Exception ex2){ex2.printStackTrace();};
		}        
      
		return lstMetodosPago;
	}
	
	

	//------------------------------------------------------------------------------
	
	
	//Modifica dependencia de metodos de pago
	public void setMetodosPago(ValueChangeEvent e) {
	
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		String codmetodo = cmbMetodosPago.getValue().toString();
		m.put("cMpagos", codmetodo);
			
		String codcaja = m.get("sCajaId").toString();
		
		try {
			
			//Listar monedas para el Metodo
			listarMonedas(codcaja, codmetodo);			
			
			//metodo = TC
			if(codmetodo.equals(MetodosPagoCtrl.TARJETA)) {			
			
				//Listar POS para la Caja			
				listarPOS(codcaja, codmetodo);
								
				//Set to blank
				lblAfiliado.setValue("Afiliado");
				lblReferencia1.setValue("Cédula:");
				lblReferencia2.setValue("");
				lblReferencia3.setValue("");
				lblBanco.setValue("");
				
				//Set to visible
				ddlAfiliado.setStyle("display:inline");
				ddlBanco.setStyle("visibility:hidden");
				txtReferencia1.setStyle("display:inline");
				txtReferencia2.setStyle("visibility:hidden");
				txtReferencia3.setStyle("visibility:hidden");
				
				
			//metodo = CHK	
			} else if(codmetodo.equals(MetodosPagoCtrl.CHEQUE)) {
				
				//Set to blank
				lblAfiliado.setValue("");
				lblReferencia1.setValue("Número Cheque:");
				lblReferencia2.setValue("Emisor:");
				lblReferencia3.setValue("Portador:");			
				lblBanco.setValue("Banco:");								
				
				//Set to visible
				ddlAfiliado.setStyle("visibility:hidden");
				ddlBanco.setStyle("display:inline");
				txtReferencia1.setStyle("display:inline");
				txtReferencia2.setStyle("display:inline");
				txtReferencia3.setStyle("display:inline");				
				
			//metodo = EFEC	
			} else if(codmetodo.equals( MetodosPagoCtrl.EFECTIVO )) {
				
				//Set to blank
				lblAfiliado.setValue("");
				lblReferencia1.setValue("");
				lblReferencia2.setValue("");
				lblReferencia3.setValue("");
				lblBanco.setValue("");
				
				//Set to not visivble
				ddlAfiliado.setStyle("visibility:hidden");
				ddlBanco.setStyle("visibility:hidden");
				txtReferencia1.setStyle("visibility:hidden");
				txtReferencia2.setStyle("visibility:hidden");
				txtReferencia3.setStyle("visibility:hidden");
			}
			
		}catch(Exception ex){
//			System.out.println("Se produjo una excepcion en setMetodosPago: " + ex);
			ex.printStackTrace();
		}
		
	}
		
	
	//------------------------------------------------------------------------------
	
	
	
	//Lista POS para la Caja
	public void listarPOS(String scaja, String spago) {
        Session session = HibernateUtilPruebaCn.currentSession();
        
        try {
        	
        	List result = null;

			Query query = session
				.createQuery("from Cafiliados ca where ca.id.c6stat='A' and ca.id.c6id = :pCaja and ca.id.c6rp01 = :pCom")
				.setParameter("pCaja", Integer.valueOf(scaja))
				.setParameter("pCom", m.get("sCompania").toString());				  
			result = query.list();
        	
			if(result != null) {				
				Cafiliados[] afiliados = new Cafiliados[result.size()];
				lstAfiliado = new ArrayList();
				
				for(int i=0; i<result.size(); i++) {
					afiliados[i] = (Cafiliados) result.get(i);
					lstAfiliado.add(new SelectItem(afiliados[i].getId().getCxcafi(), afiliados[i].getId().getCxdcafi()));
				}
			}			
        	
			
        }catch(Exception ex){
//			System.out.println("Se produjo una excepcion en listarPOS: " + ex);
        	ex.printStackTrace();
			
		}finally{
			try{session.close();}catch(Exception ex2){ex2.printStackTrace();};
		}
	}	
	
	
	//------------------------------------------------------------------------------	
	
	
	//Lista Monedas -> Metodos Pago
	public void listarMonedas(String scaja, String smetodo) {
		List lstmonedas = null;
        Session session = HibernateUtilPruebaCn.currentSession();
        
        try {
        	lstMoneda = new ArrayList();
        	List result = null;
        	
			Query query = session
				.createQuery("from Mpagos mp where mp.id.c2stat='A' and mp.id.c2rp01 = :pCom and mp.id.c2id = :pCaja and mp.id.c2ryin = :pMet")
				.setParameter("pCaja", Integer.valueOf(scaja))
				.setParameter("pCom", m.get("sCompania").toString())
				.setParameter("pMet", smetodo);				  
			result = query.list();		
        	
        	
        }catch(Exception ex){
//			System.out.println("Se produjo una excepcion en listarMonedas: " + ex);
        	ex.printStackTrace();
			
		}finally{
			try{session.close();}catch(Exception ex2){ex2.printStackTrace();};
		}
	}
	
	//--------------------------------------------------------------------------
	
	//Lista metodos de pago
	public List getSelectedMet() {	
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		Object o = m.get("met");
		
		if(o == null){	
			MetodosPago mPago = new MetodosPago(
					
					);
			m.put("met", "y");
		}else {
			selectedMet = (List)m.get("mpagos");
		}
		
		return selectedMet;
	}	
	
	
	//----------------------------------------------------------------------------
	
	
	//Lista Monedas para la Caja
	public List getLstMoneda() {	
		
		
		if(m.get("sCompania") == null) m.put("sCompania", "E01");
        Session session = HibernateUtilPruebaCn.currentSession();		
		
        try {
        	List result = null;
        	lstMoneda = new ArrayList();
        	
        	Query query = session
				.createQuery("from Cmonedas cm where cm.id.c3stat='A' and cm.id.c3rp01 = :pCom and cm.id.c3id = :pCaja")
				.setParameter("pCom", m.get("sCompania").toString())
				.setParameter("pCaja", Integer.valueOf(m.get("sCajaId").toString()));
	        result = query.list();	        
	        
        		       
        }catch(Exception ex){
//			System.out.println("Se produjo una excepcion en getLstMoneda: " + ex);
        	ex.printStackTrace();
		}finally{
			try{session.close();}catch(Exception ex2){ex2.printStackTrace();};
		}  
                
		return lstMoneda;
	}

	
	
	//----------------------------------------------------------------------
	
	
	//Veririca la moneda seleccionda
	public void setMoneda(ValueChangeEvent e) {
		
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		String codigo = getDdlMoneda().getValue().toString();
		m.put("cMoneda", codigo);
		
		//Lista Metodos Pago
		listarMetodos(codigo);
			    
		String[] moneda = (String[]) m.get("mMonedaF");
		
		Date fecha = new Date();
		
		//Si Factura -> USD
		if(!moneda[0].equals("COR")) {
			
			//Tomar tasa paralelo
			if(codigo.equals("COR")) {
				
				getTasaParalelo(fecha);
			}
			
		//Si Factura -> COR	
		} else if(moneda[0].equals("COR")) {
			
			//Tomar tasa JDE
			if(!codigo.equals("COR")) {
				
				getTasaJde(fecha);
			}
		}	
				
	}
	

	//----------------------------------------------------------------------
	
	
	//Tasa Cambio Paralelo para la fecha
	@SuppressWarnings("unchecked")
	public void getTasaParalelo(Date dfecha) {
        Session session = HibernateUtilPruebaCn.currentSession();		
		
        try {
        	List result = null;
			Query query = session
				.createQuery("from Tpcambio tp order by tp.id.fechaf desc");				 
			result = query.list(); 
			
			F55ca021 tcparalelo = new F55ca021();
			if(result != null) {
				tcparalelo = (F55ca021) result.get(0);				
				m.put("mTasaCambio", tcparalelo.getId().getTcambiod().doubleValue());
			}
        	
        }catch(Exception ex){
//			System.out.println("Se produjo una excepcion en getTasaParalelo: " + ex);
        	ex.printStackTrace();
		}finally{
			try{session.close();}catch(Exception ex2){ex2.printStackTrace();}; 
		}
		
	}
	
	
	//----------------------------------------------------------------------
	
	
	//Tasa Cambio JDE para la fecha
	@SuppressWarnings("unchecked")
	public void getTasaJde(Date fecha) {
        Session session = HibernateUtilPruebaCn.currentSession();	
        boolean bNuevaSesionENS = true;
        
        try {
        	     
        	if( session.getTransaction().isActive() )
        		bNuevaSesionENS = false;
        	
			Query query = session
        		.createQuery("from Tcambio tc where tc.id.cmond='COR' order by tc.id.fecha desc");		
			
			List result = query.list();
			Tcambio tasacambiojde = new Tcambio();
			if(result != null) {
				tasacambiojde = (Tcambio) result.get(0);				
				m.put("mTasaCambio", tasacambiojde.getId().getCxcrr());
			}
        		       
        }catch(Exception ex){
        	ex.printStackTrace();
		}finally{
			try {
				if (!bNuevaSesionENS ){
					HibernateUtilPruebaCn.closeSession(session);
				}
			} 
			catch (Exception e) { e.printStackTrace(); }
		}
	}
	
	//----------------------------------------------------------------------
		
	
	//Lista Metodos por Moneda
	public void listarMetodos(String smoneda) {
        Session session = HibernateUtilPruebaCn.currentSession();		
		
		try {
			lstMetodosPago = new ArrayList();		        	        	
        	List result = null;
        	
        	Query query = session
				.createQuery("from Pmoneda pm where pm.id.c2stat='A' and pm.id.c2id = :pCaja and pm.id.c2crcd = :pMon and pm.id.c2rp01 = :pCom")
				.setParameter("pMon", smoneda)
				.setParameter("pCom", m.get("sCompania").toString())
				.setParameter("pCaja", Integer.valueOf(m.get("sCajaId").toString()));
	        result = query.list();	        
			
		}catch(Exception ex){
//			System.out.println("Se produjo una excepcion en listarMetodos: " + ex);
			ex.printStackTrace();
			
		}finally{
        	session.close();        
		}
	}
	
	//----------------------------------------------------------------------	

	
	//Cerrar Validacion
	public void cerrarValida(ActionEvent e) {			
		dwSolicitud.setWindowState("hidden");		
	}
	
	
	//----------------------------------------------------------------------
	
	
	//Procesa la Solicitud de Autorizacion
	public void procesarSolicitud(ActionEvent e) {
		Integer numsol = 0;
		Integer numrec = 0;
		Integer numrecm = 0;
		String autoriza = "";
		String referencia = "";
		String obs = "";
		String fecha;		
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
		boolean bsolicit = true;		
		String sp = "^(((0[1-9]|[12]\\d|3[01])\\/(0[13578]|1[02])\\/((1[6-9]|[2-9]\\d)\\d{2}))|((0[1-9]|[12]\\d|30)\\/(0[13456789]|1[012])\\/((1[6-9]|[2-9]\\d)\\d{2}))|((0[1-9]|1\\d|2[0-8])\\/02\\/((1[6-9]|[2-9]\\d)\\d{2}))|(29\\/02\\/((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))))$";			
		
		
		//Valida tipo de recibo
		if(m.get("mTipoRecibo") != null) {
			String xptiporec = m.get("mTipoRecibo").toString();
			
			//MANUAL
			if(xptiporec.equals("MANUAL")) {
				numrecm = Integer.parseInt(m.get("mReciboManual").toString());
				
			//AUTOMATICO	
			} else {
				numrec = Integer.parseInt(m.get("ReciboActual").toString());
			}
			
		} else {
			bsolicit = false;
		}
	
		
		//----------------------------------------------------------------------		
		
		
		//Valida datos de la Solcitud
		if(validarSolicitud() == true && bsolicit == true) {
						
			autoriza = getCmbAutoriza().getValue().toString();
			referencia = getTxtRefAutoriza().getValue().toString();
			obs = getTxtObs().getValue().toString();			
			Date dfecha = new Date();
			Format formatter = new SimpleDateFormat("dd/MM/yyyy");
			String sfecha = formatter.format(dfecha);
		
			Pattern p = Pattern.compile(sp);
			Matcher mat = p.matcher(sfecha);
			
			//Valida formato de fecha
			if (mat.matches()){
				numsol = getNumeroSolicitud() + 1;										//Numero Solicitud
				numrec = Integer.parseInt(m.get("ReciboActual").toString()) + 1;		//Numero Recibo
				
				Session session = HibernateUtilPruebaCn.currentSession();;
				Transaction tx = session.beginTransaction();		        
				
		        try {
		        	
		        	Solicitud solicitud = new Solicitud();
		        	SolicitudId solicitid = new SolicitudId();
		        	
		        	solicitud.setObs(obs);
		        	try {
						dfecha = sdf.parse(sfecha);
						solicitud.setFecha(dfecha);
						
					} catch (ParseException e1) {				
						e1.printStackTrace();
					}	
		        	solicitid.setNumsol(numsol);
		        	solicitid.setNumrec(numrec);       	
		        	solicitid.setCodcomp(m.get("sCompania").toString());       	
		        	solicitud.setId(solicitid);
		        	
					session.save(solicitud);
		        	tx.commit();
					
					//Envia correo de alerta
					enviarEmail(autoriza);
										
					//Set to Blank
					getTxtRefAutoriza().setValue("");
					getTxtFecha().setValue("");
					getTxtObs().setValue("");
					
					dwAutoriza.setWindowState("hidden");
					
					m.put("rSolicitud", "si");
					
					CtrlPagos xctrlpagos = new CtrlPagos();
					xctrlpagos.executePagos();					
					
					
		        }catch(Exception ex){
		        	tx.rollback();
//					System.out.println("Excepcion -> procesarSolicitud # " + ex);
		        	ex.printStackTrace();
				} finally {			
					try{session.close();}catch(Exception ex2){ex2.printStackTrace();};
				}
				
			} else {
				getLblValidaSolicitud().setValue("Fecha no es válida.");
				dwSolicitud.setWindowState("normal");				
			}
		}
		
	}

	
	//----------------------------------------------------------------------

	
	//Valida datos de la solicitud
	public boolean validarSolicitud() {
		boolean bsolic = false;		
				
		if(!getTxtRefAutoriza().getValue().equals("")) {
			
			if(getTxtFecha().getValue() != null) {
								
				if(!getTxtObs().getValue().equals("")) {
					bsolic = true;	
					
				} else {
					getLblValidaSolicitud().setValue("Observaciones es requerido.");
					dwSolicitud.setStyle("top:300px; right:300px");
					dwSolicitud.setWindowState("normal");			
				}
			} else {
				getLblValidaSolicitud().setValue("Fecha es requerida.");
				dwSolicitud.setWindowState("normal");			
			}
		} else {
			getLblValidaSolicitud().setValue("Referencia es requerida.");
			dwSolicitud.setWindowState("normal");			
		}
		
		return bsolic;
	}

	
	
	//----------------------------------------------------------------------
	

	//Envia Correo de alerta	
	public void enviarEmail(String suser) {
		// MultiPartEmail email = new MultiPartEmail();
		
		to = getCmbAutoriza().getValue().toString();
		//mensaje = getTxtObs().getValue().toString();		
		String shtml = "<table width=\"680\" border=\"1\" align=\"center\"><tr><th colspan=\"2\">" + 
						"<font size=\"4\" color=\"#0099CC\"><b>Solicitud de Autorización</b></font></th>" +
					    "</tr><tr><td width=\"93\"><font face=\"Arial\" size=\"3\"><b>Cajero:</b></font></td>" +
					    "<td width=\"597\"><font face=\"Arial\" size=\"3\">" + getTxtRefAutoriza().getValue().toString() + "</font></td>" +
						"</tr><tr><td colspan=\"2\"><font face=\"Arial\" size=\"2\">" + 
						"</font></td></tr>" +
						"<tr><td colspan=\"2\" height=\"100\">" + getTxtObs().getValue().toString() + "</td></tr>" +
						"<tr><td align=\"center\" colspan=\"2\"><font face=\"Arial\" size=\"2\"><b>Casa Pellas, S. A. - Módulo de Caja</b></font></td></tr></table>";		
		
		
		try {
			
			MailHelper.SendHtmlEmail(
					new CustomEmailAddress(from),
					new CustomEmailAddress(to), new CustomEmailAddress(cc), 
					mensaje, shtml);
			 
		}catch(Exception ex){
			ex.printStackTrace();
		}		
	}


	//----------------------------------------------------------------------


	//Obtiene Numero de Solicitud
	public Integer getNumeroSolicitud() {		
		Integer numsol = 0;
		
		Session session = HibernateUtilPruebaCn.currentSession();
		
				
		try{			
			Integer result = (Integer) session
				.createQuery("select max(sol.id.numsol) from Solicitud sol")
				.uniqueResult();
		
			if(result != null) numsol = result.intValue();
			
		}catch(Exception ex){
//			System.out.print("Se capturo una excepcion en getNumeroSolicitud: " + ex);
			ex.printStackTrace();
		}finally{
			try{session.close();}catch(Exception ex2){ex2.printStackTrace();};
		}		
		
		return numsol;
	}
	
	
		
	//----------------------------------------------------------------------------
	

	
	//Lista Afiliados para la Caja
	public List getLstAfiliado() {
			
		Session session = HibernateUtilPruebaCn.currentSession();
		
		
		try {				
			
			List result = null;
			
			Query query = session
				.createQuery("from Cafiliados ca where ca.id.c6stat='A' and ca.id.c6id = :pCaja and ca.id.c6rp01 = :pCom")
				.setParameter("pCom", m.get("sCompania").toString())
				.setParameter("pCaja", Integer.valueOf(m.get("sCajaId").toString()));
	        result = query.list();
	        	        
	        Cafiliados[] afiliados = null;	        
	        
	        if(result.size()>0) {
	        	lstAfiliado = new ArrayList();
	        	afiliados = new Cafiliados[result.size()];
	        	
	        	for(int i=0; i<result.size(); i++) {
	        		afiliados[i] = (Cafiliados)result.get(i);	        			
	        		String cod = new String(afiliados[i].getId().getCxcafi());	        		
	        		m.put("mAfiliados", afiliados[i].getId().getCxcafi());
	        		lstAfiliado.add(new SelectItem(cod, afiliados[i].getId().getCxdcafi()));        
	        	}	
	        }	
	        	
			} catch (Exception e) {
							
				e.printStackTrace();
					
			} finally{
				try{session.close();}catch(Exception ex2){ex2.printStackTrace();};
			}        	
		
		return lstAfiliado;
	}	
	
	
	//------------------------------------------------------------------------------------

	
	//Evalua Cambio Cliente
	public void setCambio(ValueChangeEvent e) {			
		SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();		
		bCambio = new Boolean(getChkCambio().getValue().toString());	
//		System.out.println(new Boolean(getChkCambio().getValue().toString()));
				
		lnkMoneda.setValue("C$");		
		monCambio = "C$";
		srm.addSmartRefreshId(lnkMoneda.getClientId(FacesContext.getCurrentInstance()));
		txtCambio.setStyle("display:inline");
	}

	
	//------------------------------------------------------------------------------------
	
	
	//Procesa Cambio Cliente
	public void procCambio(ActionEvent e) {		
		
		//Formato de Numeros
		DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
		simbolos.setDecimalSeparator('.');
		simbolos.setGroupingSeparator(',');
		DecimalFormat dfDigitos = new DecimalFormat("#,###,##0.00",simbolos);
		SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
		
		Pattern p = Pattern.compile( "(?!^0*$)(?!^0*\\.0*$)^\\d{1,5}(\\.\\d{1,2})?$" );
		

		//Vaidar xCambio
		if(!getTxtCambio().getValue().equals("")) {
						
			Matcher mat = p.matcher(getTxtCambio().getValue().toString());
			
			//Valida tipo de dato
			if(mat != null && mat.matches()==true) {
				Double xCambio = Double.parseDouble(getTxtCambio().getValue().toString());
				
				//Valida politica
				Double yCambio = Double.parseDouble(getLblCambio2().getValue().toString());
				if(yCambio <= xCambio) {			
			
					//Cambio USD
					m.put("CambioUSD", xCambio);
					
					//Diferencia USD
					Double nCambio = Double.parseDouble(m.get("mCambio").toString()) - xCambio;
//					System.out.println(Double.parseDouble(m.get("mCambio").toString()) + " - " + xCambio);
					
					//Nuevo Cambio USD
					lblCambio = dfDigitos.format(nCambio);
					
					//Resto USD -> Cambio COR
					Double cCambio = xCambio * Double.parseDouble(m.get("mTasaCambio").toString());
					
					//Cambio COR
					m.put("CambioCOR", cCambio);
					
					//Imprimir Resto COR
					lblRestoCOR = dfDigitos.format(cCambio);
//					System.out.println("lblRestoCOR -> " + lblRestoCOR);
					
					lblRestoCOR2.setValue(lblRestoCOR);			
					srm.addSmartRefreshId(lblCambio2.getClientId(FacesContext.getCurrentInstance()));
					srm.addSmartRefreshId(txtCambio.getClientId(FacesContext.getCurrentInstance()));
					srm.addSmartRefreshId(lblRestoCOR2.getClientId(FacesContext.getCurrentInstance()));
					
				} else {					
					getLblMensajeValida().setValue("Cambio no es válido.");
					dwValida.setWindowState("normal");
				}
				
			} else {
				getLblMensajeValida().setValue("Verifique el Cambio.");
				dwValida.setWindowState("normal");
			}
			
		} else {
			getLblMensajeValida().setValue("Falta Cambio.");
			dwValida.setWindowState("normal");
		}
		
//		System.out.println("procCambio");		
	}
	
	
	//------------------------------------------------------------------------------------
	
	
	//Imprime Lista Bancos
	public List getLstBanco() {
			
        Session session = HibernateUtilPruebaCn.currentSession();
                
        try {
        	
        	List result = null;
        	        	
        	Query query = session        		
        		.createQuery("from Bancos");        		   	
	        result = query.list();  
	        	        
	        F55ca022[] bancos = null;
	        
	        if(result != null) {
	        	lstBanco = new ArrayList();
	        	bancos = new F55ca022[result.size()];
	        	
	        	for(int i=0; i<result.size(); i++) {
	        		bancos[i] = (F55ca022) result.get(i);	        		
	        		
	        		m.put("mBanco", bancos[0].getId().getBanco());
					lstBanco.add(new SelectItem(bancos[i].getId().getBanco(), bancos[i].getId().getBanco()));	        		
	        	}
	        }	        
        	
        }catch(Exception ex){
//			System.out.println("Se produjo una excepcion en getLstBanco: " + ex);
        	ex.printStackTrace();
			
		}finally{
        	session.close();        
		}

		return lstBanco;
	}

	
	//------------------------------------------------------------------------------------
		
	
	//Imprime Total Factura
	public String getLblTotal() {
		if(m.get("mTotalFactura") != null) {
			//lblTotal = m.get("mTotalFactura").toString();
			Double totfact = Double.parseDouble(m.get("mTotalFactura").toString());
			DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
			simbolos.setDecimalSeparator('.');
			simbolos.setGroupingSeparator(',');
			DecimalFormat dfDigitos = new DecimalFormat("#,###,##0.00",simbolos);
			lblTotal = dfDigitos.format(totfact);
		}	
		return lblTotal;
	}


	
	//Lista Autorizadores
	public List getLstAutoriza() {
		if(lstAutoriza == null){
			lstAutoriza = new ArrayList();	
			lstAutoriza.add(new SelectItem("enavarro@casapellas.com","Emilia Navarro"));
			//lstAutoriza.add(new SelectItem("rlopez@casapellas.com","Rubén López"));
			lstAutoriza.add(new SelectItem("dchamorro@casapellas.com","Danilo Chamorro"));	
			lstAutoriza.add(new SelectItem("spellas@casapellas.com","Silvio Pellas"));	
		}
		
		return lstAutoriza;
	}

	
	//------------------------------------------------------------------------------------	
	
	
	public void ocultarCambio() {		
		txtCambio.setStyle("visibility:hidden");
		chkCambio.setStyle("visibility:hidden");		
	}
		
	//Valida Afiliado 
	public void setAfiliado(ValueChangeEvent e) {
	
		
	}
		
	//obtiene las unidades de negocio: 	
	public List getLstUnegocio() {
		return lstUnegocio;
	}	
	
	//Inserta portador de TC
	public void insertarPortador(ValueChangeEvent e) {		
		String emisor = getTxtReferencia2().getValue().toString();
		getTxtReferencia3().setValue(emisor);
	}
		
	//Cierra Mensaje
	public void cerrarMensaje(ActionEvent e) {		
		dwValida.setWindowState("hidden");
	}	
	
	//Cancela Solitud
	public void cancelarSolicitud(ActionEvent e) {		
		dwAutoriza.setWindowState("hidden");		
	}
	
	
	
	//---------------------------------------------------------------------------------------------
	
	
	public UIInput getCmbUnegocio() {
		return cmbUnegocio;
	}
	public void setCmbUnegocio(UIInput cmbUnegocio) {
		this.cmbUnegocio = cmbUnegocio;
	}
	public void setLstUnegocio(List lstUnegocio) {
		this.lstUnegocio = lstUnegocio;
	}
	public UIInput getCmbMoneda() {
		return cmbMoneda;
	}
	public void setCmbMoneda(UIInput cmbMoneda) {
		this.cmbMoneda = cmbMoneda;
	}
	public void setLstMoneda(List lstMoneda) {
		this.lstMoneda = lstMoneda;
	}	
	public void setLstMetodosPago(List lstMetodosPago) {
		this.lstMetodosPago = lstMetodosPago;
	}
	public UIInput getCmbMetodosPago() {
		return cmbMetodosPago;
	}
	public void setCmbMetodosPago(UIInput cmbMetodosPago) {
		this.cmbMetodosPago = cmbMetodosPago;
	}
	public GridView getMetodosGrid() {
		return metodosGrid;
	}
	public void setMetodosGrid(GridView metodosGrid) {
		this.metodosGrid = metodosGrid;
	}
	public void setSelectedMet(List selectedMet) {
		this.selectedMet = selectedMet;
	}
	public UIInput getDdlMetodoPago() {
		return ddlMetodoPago;
	}
	public void setDdlMetodoPago(UIInput ddlMetodoPago) {
		this.ddlMetodoPago = ddlMetodoPago;
	}	
	public HtmlInputText getTxtReferencia2() {
		return txtReferencia2;
	}
	public void setTxtReferencia2(HtmlInputText txtReferencia2) {
		this.txtReferencia2 = txtReferencia2;
	}
	public UIInput getTxtMonto() {
		return txtMonto;
	}
	public void setTxtMonto(UIInput txtMonto) {
		this.txtMonto = txtMonto;
	}	
	public UIInput getDdlMoneda() {
		return ddlMoneda;
	}
	public void setDdlMoneda(UIInput ddlMoneda) {
		this.ddlMoneda = ddlMoneda;
	}
	public GridView getGvMetodosPago() {
		return gvMetodosPago;
	}
	public void setGvMetodosPago(GridView gvMetodosPago) {
		this.gvMetodosPago = gvMetodosPago;
	}	
	public DialogWindow getDwAutoriza() {
		return dwAutoriza;
	}
	public void setDwAutoriza(DialogWindow dwAutoriza) {
		this.dwAutoriza = dwAutoriza;
	}
	public UIOutput getLblReferencia1() {
		return lblReferencia1;
	}
	public void setLblReferencia1(UIOutput lblReferencia1) {
		this.lblReferencia1 = lblReferencia1;
	}
	public UIOutput getLblReferencia2() {
		return lblReferencia2;
	}
	public void setLblReferencia2(UIOutput lblReferencia2) {
		this.lblReferencia2 = lblReferencia2;
	}
	public String getLblMontoRecibido() {
		return lblMontoRecibido;
	}
	public void setLblMontoRecibido(String lblMontoRecibido) {
		this.lblMontoRecibido = lblMontoRecibido;
	}
	public UIOutput getLblMontoRecibido2() {
		return lblMontoRecibido2;
	}
	public void setLblMontoRecibido2(UIOutput lblMontoRecibido2) {
		this.lblMontoRecibido2 = lblMontoRecibido2;
	}
	public UIInput getCmbAfiliado() {
		return cmbAfiliado;
	}
	public void setCmbAfiliado(UIInput cmbAfiliado) {
		this.cmbAfiliado = cmbAfiliado;
	}
	public void setLstAfiliado(List lstAfiliado) {
		this.lstAfiliado = lstAfiliado;
	}
	public HtmlInputText getTxtReferencia3() {
		return txtReferencia3;
	}
	public void setTxtReferencia3(HtmlInputText txtReferencia3) {
		this.txtReferencia3 = txtReferencia3;
	}
	public UIOutput getLblReferencia3() {
		return lblReferencia3;
	}
	public void setLblReferencia3(UIOutput lblReferencia3) {
		this.lblReferencia3 = lblReferencia3;
	}
	public String getLblCambio() {
		return lblCambio;
	}
	public void setLblCambio(String lblCambio) {
		this.lblCambio = lblCambio;
	}	
	public void setLblTotal(String lblTotal) {
		this.lblTotal = lblTotal;
	}
	public HtmlInputText getTxtReferencia1() {
		return txtReferencia1;
	}
	public void setTxtReferencia1(HtmlInputText txtReferencia1) {
		this.txtReferencia1 = txtReferencia1;
	}
	public HtmlDropDownList getDdlAfiliado() {
		return ddlAfiliado;
	}
	public void setDdlAfiliado(HtmlDropDownList ddlAfiliado) {
		this.ddlAfiliado = ddlAfiliado;
	}
	public UIOutput getLblAfiliado() {
		return lblAfiliado;
	}
	public void setLblAfiliado(UIOutput lblAfiliado) {
		this.lblAfiliado = lblAfiliado;
	}
	public UIInput getTxtTasaCambio() {
		return txtTasaCambio;
	}
	public void setTxtTasaCambio(UIInput txtTasaCambio) {
		this.txtTasaCambio = txtTasaCambio;
	}
	public UIInput getCmbBanco() {
		return cmbBanco;
	}
	public void setCmbBanco(UIInput cmbBanco) {
		this.cmbBanco = cmbBanco;
	}
	public HtmlDropDownList getDdlBanco() {
		return ddlBanco;
	}
	public void setDdlBanco(HtmlDropDownList ddlBanco) {
		this.ddlBanco = ddlBanco;
	}
	public void setLstBanco(List lstBanco) {
		this.lstBanco = lstBanco;
	}	
	public UIOutput getLblBanco() {
		return lblBanco;
	}
	public void setLblBanco(UIOutput lblBanco) {
		this.lblBanco = lblBanco;
	}
	public HtmlInputText getTxtCambio() {
		return txtCambio;
	}
	public void setTxtCambio(HtmlInputText txtCambio) {
		this.txtCambio = txtCambio;
	}
	public HtmlCheckBox getChkCambio() {
		return chkCambio;
	}
	public void setChkCambio(HtmlCheckBox chkCambio) {
		this.chkCambio = chkCambio;
	}	
	public boolean isBCambio() {
		return bCambio;
	}
	public void setBCambio(boolean cambio) {
		bCambio = cambio;
	}
	public String getLblRestoCOR() {
		return lblRestoCOR;
	}
	public void setLblRestoCOR(String lblRestoCOR) {
		this.lblRestoCOR = lblRestoCOR;
	}
	public UIOutput getLblRestoCOR2() {
		return lblRestoCOR2;
	}
	public void setLblRestoCOR2(UIOutput lblRestoCOR2) {
		this.lblRestoCOR2 = lblRestoCOR2;
	}
	public UIOutput getLblCambio2() {
		return lblCambio2;
	}
	public void setLblCambio2(UIOutput lblCambio2) {
		this.lblCambio2 = lblCambio2;
	}
	public UIOutput getLblmonCambio() {
		return lblmonCambio;
	}
	public void setLblmonCambio(UIOutput lblmonCambio) {
		this.lblmonCambio = lblmonCambio;
	}
	public UIOutput getLblMensajeValida() {
		return lblMensajeValida;
	}
	public void setLblMensajeValida(UIOutput lblMensajeValida) {
		this.lblMensajeValida = lblMensajeValida;
	}
	public DialogWindow getDwValida() {
		return dwValida;
	}
	public void setDwValida(DialogWindow dwValida) {
		this.dwValida = dwValida;
	}
	public UIOutput getLnkMoneda() {
		return lnkMoneda;
	}
	public void setLnkMoneda(UIOutput lnkMoneda) {
		this.lnkMoneda = lnkMoneda;
	}
	public DialogWindow getDwMensaje() {
		return dwMensaje;
	}
	public void setDwMensaje(DialogWindow dwMensaje) {
		this.dwMensaje = dwMensaje;
	}
	public UIInput getTxtRefAutoriza() {
		return txtRefAutoriza;
	}
	public void setTxtRefAutoriza(UIInput txtRefAutoriza) {
		this.txtRefAutoriza = txtRefAutoriza;
	}
	public UIOutput getLblValidaSolicitud() {
		return lblValidaSolicitud;
	}
	public void setLblValidaSolicitud(UIOutput lblValidaSolicitud) {
		this.lblValidaSolicitud = lblValidaSolicitud;
	}
	public UIInput getCmbAutoriza() {
		return cmbAutoriza;
	}
	public void setCmbAutoriza(UIInput cmbAutoriza) {
		this.cmbAutoriza = cmbAutoriza;
	}
	public void setLstAutoriza(List lstAutoriza) {
		this.lstAutoriza = lstAutoriza;
	}
	public UIInput getTxtFecha() {
		return txtFecha;
	}
	public void setTxtFecha(UIInput txtFecha) {
		this.txtFecha = txtFecha;
	}
	public UIInput getTxtObs() {
		return txtObs;
	}
	public void setTxtObs(UIInput txtObs) {
		this.txtObs = txtObs;
	}
	public DialogWindow getDwSolicitud() {
		return dwSolicitud;
	}
	public void setDwSolicitud(DialogWindow dwSolicitud) {
		this.dwSolicitud = dwSolicitud;
	}

}

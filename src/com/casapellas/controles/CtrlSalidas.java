

/**
 * 
 	Creado por: Juan Carlos Ñamendi
 	Fecha de Cracion: 28/01/2009
 	Ultima Modificacion: 28/01/2009
 	Departamento: Informatica
 * 
 */

package com.casapellas.controles;
import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

// import org.apache.commons.mail.MultiPartEmail;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import com.casapellas.entidades.Salida;
import com.casapellas.entidades.SalidaId;
import com.casapellas.entidades.Salidadet;
import com.casapellas.entidades.SalidadetId;
import com.casapellas.entidades.SalidasId;
import com.casapellas.entidades.Salidaz;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.util.CustomEmailAddress;
import com.casapellas.util.MailHelper;
import com.casapellas.util.PropertiesSystem;
import com.infragistics.faces.grid.component.Cell;
import com.infragistics.faces.grid.component.GridView;
import com.infragistics.faces.grid.component.RowItem;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
import com.infragistics.faces.window.component.DialogWindow;


public class CtrlSalidas {

	//Mapa
	Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	
	//Datos generales
	public String fechaSalida;
	public String numDocumento;
		
	//Solicitudes	
	List lstSolicitud = null;
	GridView gvSolicitud;
	
	//Filtro
	private UIInput ddlSolicitud;
	List lstFiltro = null;
	
	//Detalle de solicitudes
	GridView gvDetalleSalidas;
	List lstDetalleSalidas = null;
	
	//Salidas
	private UIInput cmbBusqueda;
	private List lstBusqueda = null;
	private UIInput txtParametro;
	private UIInput txtMonto;
	private UIInput txtMotivo;
	
	//Dialog Window
	private DialogWindow dwDetalle;
	private DialogWindow dwValida;
	
	//Grid detalle
	GridView gvDetalle2;
	List lstDetalle2 = null;	
	GridView gvSalidas;
	
	//Moneda
	private UIInput ddlMoneda;
	private UIInput ddlMoneda2;
	private List lstMoneda = null;
		
	//Unidad de negocios
	private HtmlDropDownList ddlNegocio;
	private List lstNegocio = null;
	private UIOutput lblNegocio; 
	
	//Cuenta objeto
	private HtmlDropDownList ddlObjeto;
	private List lstObjeto = null;
	private UIOutput lblObjeto;
	
	//Cuenta Auxiliar
	private HtmlDropDownList ddlAuxiliar;
	private List lstAuxiliar = null;
	private UIOutput lblAuxiliar;
	
	//Cargo
	private UIInput ddlCargo;
	private List lstCargo = null;
	
	//Datos de correo
	private String mail = "192.168.1.2";
	private String from = "azapata@casapellas.com";
	private String to = "azapata@casapellas.com";
	private String cc = "azapata@casapellas.com";
	private String mensaje = "Solicitud de Autorización de Salidas de Caja - MODULO DE CAJA.";
	
	
	
	//-----------------------------------------------------------------------------
	
	
	//Agregar metodo
	public void setMetodo(ActionEvent e) {			
		String patt = "^\\d+(?:\\.\\d{0,2})?$";
		Pattern p = Pattern.compile(patt);
		boolean valida = true;
		boolean bcargo = true;
		String smonto = "";
		Double monto;
		
		if(getTxtMonto().getValue() == null) {			
			valida = false;
			
		} else {		
			smonto = getTxtMonto().getValue().toString();
			Matcher mat = p.matcher(smonto);
			
			if(mat.matches() == false) {
				valida = false;
			}
		}
		if(m.get("tipocargo") != null) {
			String xcargo = getDdlCargo().getValue().toString();
			String cargo = (String) m.get("tipocargo");
			if(!xcargo.equals(cargo)) {
				bcargo = false;
			}
		}
		
		
		//Valida Monto
		if(valida == true) {
			
			//Valida el cargo
			if(bcargo == true) {			
			
				Salidadet detalle = new Salidadet();
				SalidadetId id = new SalidadetId();	
				Salidaz[] salida = null;
				List lstsalida = null;
				boolean bmon = true;
				monto = Double.parseDouble(smonto);			
				String moneda = getDdlMoneda().getValue().toString();			
				m.put("tipocargo", getDdlCargo().getValue().toString());
				
				if(m.get("lstDetalle2") != null) {								
					
					lstsalida = (List) m.get("lstDetalle2");
					int cant = lstsalida.size();
					salida = new Salidaz[cant];
					
					for(int i=0; i<cant; i++) {
						salida[i] = (Salidaz) lstsalida.get(i);
						
						if(moneda.equals(salida[i].getMoneda())) {
							monto = monto + salida[i].getMonto().doubleValue();
							salida[i].setMonto(BigDecimal.valueOf(monto));												
							bmon = false;						
						}
					}
								
					if(bmon == true) {
						cant++;
						salida = new Salidaz[cant];
						salida[cant-1] = new Salidaz(moneda,"24",1,BigDecimal.valueOf(monto));
						lstsalida.add(salida[cant-1]);
					}
					
				} else {
					
					salida = new Salidaz[1];
					salida[0] = new Salidaz(moneda,"24",1,BigDecimal.valueOf(monto));
					lstsalida = new ArrayList();				
					lstsalida.add(salida[0]);
									
				}
				m.put("lstDetalle2", lstsalida);			
				gvDetalle2.dataBind();	
				
				//Set to blank
				//getTxtMonto().setValue("");
				
			} else {
				dwValida.setWindowState("normal");
				
			}	
				
		} else {
			dwValida.setWindowState("normal");
		}
		
	}
	
	
	//----------------------------------------------------------------------------
	
	
	//Registra solicitud
	public void registrarSolicitud(ActionEvent e) {
		
		if(validarSolicitud() == true) {
			
			int codclie = 0;
			String nomclie = "";
			String strBusqueda = m.get("strBusqueda").toString();
			nomclie = getTxtParametro().getValue().toString();
			
			//Valida Solicitante
			if(!nomclie.equals("")) {
				
				if(strBusqueda.equals("01")) {				
					codclie = buscarCodCliente(nomclie);
					
				} else {
					codclie = Integer.parseInt(getTxtParametro().getValue().toString());
				}
				
				//Si existen el Solicitante
				if(codclie != 0) {				
				
					Session session = HibernateUtilPruebaCn.currentSession();
					Transaction tx = session.beginTransaction();
					
					try {
						
						Salida salida = new Salida();
						SalidaId salid = new SalidaId();
						
						int numdoc = getUltimoDoc() + 1;			
						//int numdoc = Integer.parseInt(m.get("NumDocumento").toString());
						String cargo = getDdlCargo().getValue().toString();
						
						if(cargo.equals("01")) {
//							salida.setUneg(getDdlNegocio().getValue().toString());			//Unidad de negocio	
//							salida.setCobj(getDdlObjeto().getValue().toString());			//Cuenta objeto
//							salida.setCsub(getDdlAuxiliar().getValue().toString());			//Cuenta auxiliar
						}
//						salida.setCargo(cargo);												//Cargo
										
//						salida.setCajero(codclie+"");										//Cajero
						
						nomclie = m.get("sNombreEmpleado").toString();
						codclie = buscarCodCliente(nomclie);				
//						salida.setSolicitant(codclie+"");									//Solicitante
						salida.setConcepto(getTxtMotivo().getValue().toString());			//Motivo
						salida.setEstado("P");												//Estado de la solicitud				
						Date dfecha = new Date();
//						salida.setFecha(dfecha);											//Fecha								
						salid.setNumsal(numdoc);											//No. documento
						//salid.setSucu("24");
						String nomsuc = (String)m.get("sNombreSucursal");
						String codsuc = buscarCodSucursal(nomsuc);
//						salid.setSucu(codsuc.trim());										//Sucursal									
						salida.setId(salid);				
						session.save(salida);				
						
						//Registrar Detalle			
						registrarDetalle(numdoc);
						
						//Enviar correo responsable de autorizacion
						enviarCorreo();
						
						tx.commit();
						m.put("lstSolicitud", null);
				
						//Actualizar numero documento
						getNumDocumento();
						
					}catch(Exception ex){
						System.out.println("Excepcion -> registrarSolicitud: " + ex);
					}finally{
						try{session.close();}catch(Exception ex2){ex2.printStackTrace();};    
					}
				
				} else {					
					dwValida.setWindowState("normal");
				}
				
			} else {
				dwValida.setWindowState("normal");
			}
							
		} else {
			dwValida.setWindowState("normal");			
		}
	}

	
	//-----------------------------------------------------------------------
	
	
	//Codigo de Sucursal
	private String buscarCodSucursal(String nomsuc) {
		String codsuc = "";
		
		Session session = HibernateUtilPruebaCn.currentSession();
		
		
		try {
			
			List result = null;        	
        	Query query = session        		
        		.createQuery("from Unegocio un where trim(un.id.descrip)=:pSuc")
	        	.setParameter("pSuc", nomsuc);
	        result = query.list();   
			
		}catch(Exception ex){
			
			System.out.println("Excepcion -> buscarCodSucursal: " + ex);
			
		}finally{
        	session.close();        
		}	
		return codsuc;
	}
	//-----------------------------------------------------------------------
	
	
	//Codigo de cliente
	private int buscarCodCliente(String nomclie) {
		int codclie = 0;
		
		Session session = HibernateUtilPruebaCn.currentSession();
		
		
		try {
		
			Integer codigo = (Integer) session
				.createQuery("select cc.id.aban8 from Vf0101 cc where cc.id.abalph like :pNom and cc.id.abat1='E'")
				.setParameter("pNom", nomclie+"%")
				.uniqueResult();
		
			if(codigo != null) codclie = codigo.intValue();
			
		}catch(Exception ex){
			
			System.out.println("Excepcion -> buscarCodCliente: " + ex);
			
		}finally{
        	session.close();        
		}	
		return codclie;
	}
	//----------------------------------------------------------------------------
	
	
	//Registra detalle de solicitud
	public void registrarDetalle(int numdoc) {
		
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = session.beginTransaction();
		
		try {
		
			if(m.get("lstDetalle2") != null) {
				Salidadet saldet = new Salidadet();
				SalidadetId saldetid = new SalidadetId();
				List lstdet = (List) m.get("lstDetalle2");
				Salidaz[] deta = new Salidaz[lstdet.size()];
									
				for(int i=0; i<lstdet.size(); i++) {
					deta[i] = (Salidaz) lstdet.get(i);						
					saldetid.setNumsal(numdoc);
					saldetid.setMoneda(deta[i].getMoneda());
					saldetid.setSucu(deta[i].getSucu());
					saldet.setId(saldetid);
					saldet.setMonto(BigDecimal.valueOf(deta[i].getMonto().doubleValue()));
					session.save(saldet);
				}
				tx.commit();
			}
			
		}catch(Exception ex){
			tx.rollback();
			System.out.println("Excepcion -> registrarSolicitud: " + ex);
		}finally{
			try{session.close();}catch(Exception ex2){ex2.printStackTrace();};
		}	
	}
	
	
	//----------------------------------------------------------------------------
	
	
	//Ultima solicitud
	public int getUltimoDoc() {
		int ultimo = 0;		
		
		Session session = HibernateUtilPruebaCn.currentSession();
				
		try {
		
			Integer maxcta = (Integer) session
				.createQuery("select max(cc.id.numsal) from Salida cc")
				.uniqueResult();
		
			if(maxcta != null) ultimo = maxcta.intValue();
		
		}catch(Exception ex){
			System.out.println("Se produjo una excepcion -> getUltimoDoc: " + ex);
			
		}finally{
			try{session.close();}catch(Exception ex2){ex2.printStackTrace();};
		}
		
		return ultimo;
	}
	
	
	
	//----------------------------------------------------------------------------
	
	
	//Elimina solicitud
	public void eliminarSolictud(ActionEvent e) {
		
		
		Salidaz salidas = null;
		Salidaz[] xsalidas = null;
		List nsalidas = new ArrayList();
		List xsolicit = new ArrayList();
		xsolicit = (List) m.get("lstDetalle2");
		
		
		try {
						
			RowItem ri = (RowItem)e.getComponent().getParent().getParent();
			List lstA = (List) ri.getCells();	
			
			//Monto
			Cell c1 = (Cell) lstA.get(1);
			HtmlOutputText xmonto = (HtmlOutputText) c1.getChildren().get(0);
			String smonto = xmonto.getValue().toString();
			BigDecimal monto = BigDecimal.valueOf(Double.parseDouble(xmonto.getValue().toString()));
			
			//Moneda
			Cell c2 = (Cell) lstA.get(2);
			HtmlOutputText xmoneda = (HtmlOutputText) c2.getChildren().get(0);
			String moneda = xmoneda.getValue().toString();
			
			salidas = new Salidaz(
						moneda,
						monto
					);
						
			xsalidas = new Salidaz[xsolicit.size()];			
			for (int i=0; i<xsolicit.size();i++){
				xsalidas[i] = (Salidaz)xsolicit.get(i);
				
				if(moneda.equals(xsalidas[i].getMoneda()) && smonto.equals(xsalidas[i].getMonto().toString())) {
										
				} else {
					nsalidas.add(xsalidas[i]);
				}
			}
			m.put("lstDetalle2", nsalidas);			
			gvDetalle2.dataBind();
			
		}catch(Exception ex){
			System.out.println("Excepcion -> eliminarSolictud: " + ex);
		}	
		
		System.out.println("eliminarSolictud");		
	}
	
	
	
	//----------------------------------------------------------------------------
	
		
	//Registra salidas
	public void registrarSalida(ActionEvent e) {
		
		List selecc = null;		
		int cant = 0;
		String codsal = "";		

		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = session.beginTransaction();
		
		try {
			
			tx = session.beginTransaction();		
			Iterator selectedRows = getGvSalidas().getSelectedRows().iterator();    
	        
			while (selectedRows.hasNext()) {	
	       	   RowItem rowItem = (RowItem) selectedRows.next();
	           Object dataKey = gvSalidas.getDataKeyValue(rowItem);
	           
	           if(selecc==null) {
	        	   selecc = new ArrayList();           
	           }           
	           //selecc.add(dataKey.toString());
	           codsal = dataKey.toString();	           
	           cant++;
	        }			
			
			
			//Valida cantidad
			if(cant == 1) {
				
				int numsal = 0;
				String sucu = "";
				String stat = "";
				if(m.get("lstSolicitud") != null) {
					List xsolicitud = (List) m.get("lstSolicitud");
					SalidasId[] salidas = new SalidasId[xsolicitud.size()];
					for(int i=0; i<xsolicitud.size()-1; i++) {
						salidas[i] = (SalidasId) xsolicitud.get(i);
						if(codsal.equals(salidas[i].getCodsal())) {
							numsal = salidas[i].getNumsal();
							sucu = salidas[i].getSucu();
							stat = salidas[i].getEstado();
						}
					}
				}
				
				if(numsal != 0 && !sucu.equals("") && stat.equals("A")) {
					
					Salida salida = (Salida) session
						.createQuery("from Salida sa where sa.id.numsal=:pSol and sa.id.sucu=:pSuc and sa.estado=:pStat")
						.setParameter("pSol", numsal)
						.setParameter("pSuc", sucu)
						.setParameter("pStat", stat)
						.uniqueResult();		
					salida.setEstado("V");
					session.update(salida);			
					tx.commit();	
				
					m.put("lstSolicitud", null);
					
				} else {
					dwValida.setWindowState("normal");
				}				
			} else {
				dwValida.setWindowState("normal");				
			}
			
		}catch(Exception ex){
			if (tx != null)
				tx.rollback();          	
        	System.out.print("Excepcion -> registrarSalida: " + ex);
        
		}finally{
			try{session.close();}catch(Exception ex2){ex2.printStackTrace();};
		}
	}
	//Valida Salida
	public boolean validarSolicitud() {
		boolean valida = true;
		String patt = "(^\\d{3,5}\\,\\d{2}$)|(^\\d{3,5}$)";
		Pattern p = Pattern.compile( "(?!^0*$)(?!^0*\\.0*$)^\\d{1,5}(\\.\\d{1,3})?$" );		
				
		if(m.get("lstDetalle2") == null) {			
			valida = false;
			
		} else if(getTxtMotivo().getValue().toString().equals("")){	
			valida = false;
		}
		
		return valida;
	}
	
	//Enviar Correo a responsable de Autorizacion
	public void enviarCorreo() {
		// MultiPartEmail email = new MultiPartEmail();
		
		String strListEmail = PropertiesSystem.MAIL_INTERNAL_ADDRESS;
		String[] listEmail = strListEmail.split(",");
		
		List<CustomEmailAddress> toList = new ArrayList<CustomEmailAddress>();
		for (String strEmail : listEmail) {
			toList.add(new CustomEmailAddress(strEmail));
		}
		
		String shtml = "<table width=\"680\" border=\"1\" align=\"center\"><tr><th colspan=\"2\">" + 
						"<font size=\"4\" color=\"#0099CC\"><b>Solicitud de Autorización</b></font></th>" +
					    "</tr><tr><td width=\"93\"><font face=\"Arial\" size=\"3\"><b>Cajero:</b></font></td>" +
					    "<td width=\"597\"><font face=\"Arial\" size=\"3\">" + "Alberto Zapata" + "</font></td>" +
						"</tr><tr><td colspan=\"2\"><font face=\"Arial\" size=\"2\">" + 
						"</font></td></tr>" +
						"<tr><td colspan=\"2\" height=\"100\">" + getTxtMotivo().getValue().toString() + "</td></tr>" +
						"<tr><td align=\"center\" colspan=\"2\"><font face=\"Arial\" size=\"2\"><b>Casa Pellas, S. A. - Módulo de Caja</b></font></td></tr></table>";		
		
		try {
			
			@SuppressWarnings("serial")
			List<CustomEmailAddress> ccList = new ArrayList<CustomEmailAddress>() {
				{ add(new CustomEmailAddress(cc)); }
			};
			
			if (toList.size() > 0) {
				MailHelper.SendHtmlEmail(
						new CustomEmailAddress(from),
						toList, ccList, null,
						mensaje, shtml, new String[0]);
			}
		}catch(Exception ex){
			System.out.println("=>Correo no enviado enviarEmail \n"+ ex.getStackTrace());
			ex.printStackTrace();
		}
	}
	
	//Cierra Ventana detalle
	public void closeWindow(ActionEvent e) {
		dwDetalle.setWindowState("hidden");
	}
	
	//Listar solicitudes
	public List getLstSolicitud() {
		
		if(m.get("lstSolicitud") == null) {			
			
	        Session session = HibernateUtilPruebaCn.currentSession();
	                
	        try {
	        	
	        	List result = null;
	        	
	        	Query query = session        		
	        		.createQuery("select cc.id from Salidas cc");        		 	
		        result = query.list();   
		        
		        lstSolicitud = result;
		        m.put("lstSolicitud", lstSolicitud);
		        
	        }catch(Exception ex){
				System.out.println("getLstSolicitud -> getLstSolicitud: " + ex);
				
			}finally{
	        	session.close();        
			}
			
		} else {
			lstSolicitud = new ArrayList();
			lstSolicitud = (List) m.get("lstSolicitud");
			
		}
		
		return lstSolicitud;
	}
	
	//Selecciona parametro de busqueda
	public void setBusqueda(ValueChangeEvent e){
				
		String strBusqueda = cmbBusqueda.getValue().toString();
		m.put("strBusqueda", strBusqueda);
		System.out.println("Búsqueda # "+strBusqueda);
	}
	
	//----------------------------------------------------------------------------
	
	//Detalle de solicitud
	public void getDetalle(ActionEvent e) {
				
		dwDetalle.setWindowState("normal");
		dwDetalle.setModalBackgroundCssClass("igdw_ModalBackground");
		boolean mod = dwDetalle.isModal();			
		
		try {
			
			RowItem ri = (RowItem)e.getComponent().getParent().getParent();
			List lstA = (List) ri.getCells();		
			Cell c = (Cell) lstA.get(1);
			HtmlOutputText docItem = (HtmlOutputText) c.getChildren().get(0);			
			int numdoc = Integer.parseInt(docItem.getValue().toString());
			
	        Session session =  HibernateUtilPruebaCn.currentSession();
	                
	        try {
	        	
	        	List result = null;
	        	        	
	        	Query query = session        		
	        		.createQuery("select sd.id, sd.monto from Salidadet sd where sd.id.numsal=:pDoc")
	        		.setParameter("pDoc", numdoc);		        	   	
		        result = query.list();   
		        m.put("lstDetalle", result);
		        lstDetalleSalidas = result;
		        
	        }catch(Exception ex){
				System.out.println("Detalle de Salidas -> getDetalle: " + ex);
				
			}finally{
				try{session.close();}catch(Exception ex2){ex2.printStackTrace();};
			}
			
		}catch(Exception ex){
			System.out.print("Detalle de Salidas -> getDetalle: " + ex);
		}
		
	}
	
	//Lista Monedas
	public List getLstMoneda() {

		if(lstMoneda == null) {
	        Session session = HibernateUtilPruebaCn.currentSession();
	        
	        try {
	        	lstMoneda = new ArrayList();
				Query query = session.createQuery("from Mmoneda");										  
				List result = query.list();	
	        }catch(Exception ex){
				System.out.println("Exepcion -> getLstMoneda: " + ex);
			}finally{
				try{session.close();}catch(Exception ex2){ex2.printStackTrace();};      
			}
		}
		return lstMoneda;
	}

	//Lista unidad de negocios
	public List getLstNegocio() {
		
		
        Session session = HibernateUtilPruebaCn.currentSession();
        
        try {
        
        	List result = null;
        	
			Query query = session
				.createQuery("from Unegocio");										  
			result = query.list();	
			
			//Unegocio[] unegocio = null; 
			
			if(result != null) {
				lstNegocio = new ArrayList();
				//unegocio = new Unegocio[result.size()];
				
				for(int i=0; i<result.size(); i++) {
					//unegocio[i] = (Unegocio) result.get(i);
					
					try {
						
						//String cod = new String(unegocio[i].getId().getCodigo(),"Cp1047");
						//String des = new String(unegocio[i].getId().getDescrip());
						//m.put("cNegocio", new String(unegocio[0].getId().getCodigo(),"Cp1047"));
						//lstNegocio.add(new SelectItem(cod, des));
						
					} catch (Exception e) {				
						e.printStackTrace();
					}
				}
			}	
			
        }catch(Exception ex){
			System.out.println("Excepcion -> getLstNegocio: " + ex);
			
		}finally{
			try{session.close();}catch(Exception ex2){ex2.printStackTrace();};
		}
		
		return lstNegocio;
	}
	
	
	//----------------------------------------------------------------------------
	
	
	//Lista cuenta objeto
	public List getLstObjeto() {
		
		if(lstObjeto == null) {
			lstObjeto = new ArrayList();	
			lstObjeto.add(new SelectItem("11101","11101"));				
		}
		
		return lstObjeto;
	}
	

	//----------------------------------------------------------------------------
	
	
	//Lista cuenta auxiliar
	public List getLstAuxiliar() {
	
		if(lstAuxiliar == null) {
			lstAuxiliar = new ArrayList();	
			lstAuxiliar.add(new SelectItem("01","01"));
			lstAuxiliar.add(new SelectItem("02","02"));
			lstAuxiliar.add(new SelectItem("03","03"));
		}	
		
		return lstAuxiliar;
	}
	
	
	//----------------------------------------------------------------------------
	
	
	//Cambio de Cargo
	public void setCargo(ValueChangeEvent e) {

	
	if(m.get("lstDetalle2") == null) {	
		
			//Cargo a cuenta auxiliar
			if(getDdlCargo().getValue().equals("01")) {
			
				ddlNegocio.setStyle("display:inline");
				lblNegocio.setValue("Unidad de Negocio:");
				ddlObjeto.setStyle("display:inline");
				lblObjeto.setValue("Cuenta Objeto:");
				ddlAuxiliar.setStyle("display:inline");
				lblAuxiliar.setValue("Cuenta Subsidiaria:");
				
			//Cargo a cuenta de cliente	
			} else {
				
				ddlNegocio.setStyle("visibility:hidden");
				lblNegocio.setValue("");
				ddlObjeto.setStyle("visibility:hidden");
				lblObjeto.setValue("");
				ddlAuxiliar.setStyle("visibility:hidden");
				lblAuxiliar.setValue("");
			}		
		
		} else if(getDdlCargo().getValue().toString().equals("02")){		
			lstCargo = new ArrayList();	
			lstCargo.add(new SelectItem("02","Cuenta de Cliente"));
			lstCargo.add(new SelectItem("01","Cuenta Auxiliar"));
			m.put("lstCargo", lstCargo);
			
		} else if(getDdlCargo().getValue().toString().equals("01")){
			lstCargo = new ArrayList();	
			lstCargo.add(new SelectItem("01","Cuenta Auxiliar"));
			lstCargo.add(new SelectItem("02","Cuenta de Cliente"));
			m.put("lstCargo", lstCargo);
		}
	}
	
	
	//----------------------------------------------------------------------------
	
	
	//Lista Cargo a
	public List getLstCargo() {
		
		if(m.get("lstCargo") == null) {
			m.put("tipoUsuario", "E");
			lstCargo = new ArrayList();	
			lstCargo.add(new SelectItem("01","Cuenta Auxiliar"));
			lstCargo.add(new SelectItem("02","Cuenta de Cliente"));
			m.put("lstCargo", lstCargo);			
		
		} else {
			lstCargo = new ArrayList();
			lstCargo = (List) m.get("lstCargo");
		}
		
		return lstCargo;
	}
	
	
	//----------------------------------------------------------------------------	

	
	//Lista salida
	public List getLstDetalle2() {
		
		if(m.get("lstDetalle2") != null) {
			lstDetalle2 = (List) m.get("lstDetalle2");
		}
		
		return lstDetalle2;
	}
	
	
	//----------------------------------------------------------------------------
	
	
	//Tipo Filtro
	public List getLstFiltro() {

		if(lstFiltro == null){
			lstFiltro = new ArrayList();	
			lstFiltro.add(new SelectItem("P","P"));
			lstFiltro.add(new SelectItem("A","A"));
			lstFiltro.add(new SelectItem("D","D"));			
		}
		
		return lstFiltro;
	}

	
	//----------------------------------------------------------------------------
	
	
	//Filtro solicitudes por parametro
	public void filtrarSolicitud(ValueChangeEvent e) {
		
		String tiposol = getDdlSolicitud().getValue().toString();
        Session session = HibernateUtilPruebaCn.currentSession();
                
        try {
        	
        	List result = null;
        	
        	Query query = session        		
        		.createQuery("from Salida sa where sa.estado=:pStat")
        		.setParameter("pStat", tiposol);
	        result = query.list();
	      
	        lstSolicitud = result;
	        m.put("lstSolicitud", lstSolicitud);
	        
        }catch(Exception ex){
			System.out.println("filtrarSolicitud -> filtrarSolicitud: " + ex);
			
		}finally{
			try{session.close();}catch(Exception ex2){ex2.printStackTrace();};      
		}
		
	}
	
	
	//----------------------------------------------------------------------------
	
	
	//Fecha de Salida
	public String getFechaSalida() {
		
		if (fechaSalida == null) {			
			Date fecharecibo = new Date();
			Format formatter = new SimpleDateFormat("dd/MM/yyyy");
			fechaSalida = formatter.format(fecharecibo);			
		}
		
		return fechaSalida;
	}
	public void setFechaSalida(String fechaSalida) {
		this.fechaSalida = fechaSalida;
	}

	
	//----------------------------------------------------------------------------

	
	//Lista Ultimo Documento
	public String getNumDocumento() {
		int lastdoc = 1;
		
		Session session = HibernateUtilPruebaCn.currentSession();
		
		
		try{
			
			Integer result = (Integer) session
				.createQuery("select max(sal.id.numsal) from Salida sal")
				.uniqueResult();
		
			if(result != null) lastdoc = result.intValue() + 1;
			//lastdoc = result.intValue() + 1;
			m.put("NumDocumento",lastdoc);
			
		}catch(Exception ex){
			System.out.print("Exception  -> getNumDocumento: " + ex);
			
		}finally{
			try{session.close();}catch(Exception ex2){ex2.printStackTrace();};  
		}
		
		numDocumento = lastdoc + "";
		
		return numDocumento;
	}

	
	//----------------------------------------------------------------------------
	
	
	//Lista Detalles de solicitud
	public List getLstDetalleSalidas() {
			
		//if(m.get("lstDetalle") == null) {
			lstDetalleSalidas = (List) m.get("lstDetalle");			
		//}
		
		return lstDetalleSalidas;
	}
	
	
	//----------------------------------------------------------------------------
	
	
	//Busqueda de cliente
	public List getLstBusqueda() {
		
		if(lstBusqueda == null){
			lstBusqueda = new ArrayList();	
			lstBusqueda.add(new SelectItem("01","Nombre Solicitante","Búsqueda por nombre de Solicitante"));
			lstBusqueda.add(new SelectItem("02","Código Solicitante","Búsqueda por código de Solicitante"));		
		}
		m.put("strBusqueda", "01");
		return lstBusqueda;
	}
	
	
	//----------------------------------------------------------------------------
	
	
	//Cerrar ventana
	public void cerrarValida(ActionEvent e) {
		
		dwValida.setWindowState("hidden");
	}
	
	
	//----------------------------------------------------------------------------

	
	public void setLstSolicitud(List lstSolicitud) {
		this.lstSolicitud = lstSolicitud;
	}	
	public GridView getGvSolicitud() {
		return gvSolicitud;
	}
	public void setGvSolicitud(GridView gvSolicitud) {
		this.gvSolicitud = gvSolicitud;
	}
	public void setNumDocumento(String numDocumento) {
		this.numDocumento = numDocumento;
	}
	public UIInput getCmbBusqueda() {
		return cmbBusqueda;
	}
	public void setCmbBusqueda(UIInput cmbBusqueda) {
		this.cmbBusqueda = cmbBusqueda;
	}
	public void setLstBusqueda(List lstBusqueda) {
		this.lstBusqueda = lstBusqueda;
	}
	public UIInput getTxtParametro() {
		return txtParametro;
	}
	public void setTxtParametro(UIInput txtParametro) {
		this.txtParametro = txtParametro;
	}
	public DialogWindow getDwDetalle() {
		return dwDetalle;
	}
	public void setDwDetalle(DialogWindow dwDetalle) {
		this.dwDetalle = dwDetalle;
	}
	public GridView getGvDetalleSalidas() {
		return gvDetalleSalidas;
	}
	public void setGvDetalleSalidas(GridView gvDetalleSalidas) {
		this.gvDetalleSalidas = gvDetalleSalidas;
	}
	public void setLstDetalleSalidas(List lstDetalleSalidas) {
		this.lstDetalleSalidas = lstDetalleSalidas;
	}
	public GridView getGvDetalle2() {
		return gvDetalle2;
	}
	public void setGvDetalle2(GridView gvDetalle2) {
		this.gvDetalle2 = gvDetalle2;
	}
	public void setLstDetalle2(List lstDetalle2) {
		this.lstDetalle2 = lstDetalle2;
	}
	public GridView getGvSalidas() {
		return gvSalidas;
	}
	public void setGvSalidas(GridView gvSalidas) {
		this.gvSalidas = gvSalidas;
	}
	public UIInput getDdlMoneda() {
		return ddlMoneda;
	}
	public void setDdlMoneda(UIInput ddlMoneda) {
		this.ddlMoneda = ddlMoneda;
	}
	public void setLstMoneda(List lstMoneda) {
		this.lstMoneda = lstMoneda;
	}	
	public void setLstAuxiliar(List lstAuxiliar) {
		this.lstAuxiliar = lstAuxiliar;
	}
	public void setLstNegocio(List lstNegocio) {
		this.lstNegocio = lstNegocio;
	}
	public void setLstObjeto(List lstObjeto) {
		this.lstObjeto = lstObjeto;
	}
	public UIInput getDdlMoneda2() {
		return ddlMoneda2;
	}
	public void setDdlMoneda2(UIInput ddlMoneda2) {
		this.ddlMoneda2 = ddlMoneda2;
	}
	public UIInput getDdlCargo() {
		return ddlCargo;
	}
	public void setDdlCargo(UIInput ddlCargo) {
		this.ddlCargo = ddlCargo;
	}
	public void setLstCargo(List lstCargo) {
		this.lstCargo = lstCargo;
	}
	public void setDdlAuxiliar(HtmlDropDownList ddlAuxiliar) {
		this.ddlAuxiliar = ddlAuxiliar;
	}
	public void setDdlMoneda(HtmlDropDownList ddlMoneda) {
		this.ddlMoneda = ddlMoneda;
	}
	public void setDdlNegocio(HtmlDropDownList ddlNegocio) {
		this.ddlNegocio = ddlNegocio;
	}
	public void setDdlObjeto(HtmlDropDownList ddlObjeto) {
		this.ddlObjeto = ddlObjeto;
	}
	public HtmlDropDownList getDdlAuxiliar() {
		return ddlAuxiliar;
	}
	public HtmlDropDownList getDdlNegocio() {
		return ddlNegocio;
	}
	public HtmlDropDownList getDdlObjeto() {
		return ddlObjeto;
	}
	public UIOutput getLblNegocio() {
		return lblNegocio;
	}
	public void setLblNegocio(UIOutput lblNegocio) {
		this.lblNegocio = lblNegocio;
	}
	public UIOutput getLblObjeto() {
		return lblObjeto;
	}
	public void setLblObjeto(UIOutput lblObjeto) {
		this.lblObjeto = lblObjeto;
	}
	public UIOutput getLblAuxiliar() {
		return lblAuxiliar;
	}
	public void setLblAuxiliar(UIOutput lblAuxiliar) {
		this.lblAuxiliar = lblAuxiliar;
	}
	public DialogWindow getDwValida() {
		return dwValida;
	}
	public void setDwValida(DialogWindow dwValida) {
		this.dwValida = dwValida;
	}
	public UIInput getTxtMonto() {
		return txtMonto;
	}
	public void setTxtMonto(UIInput txtMonto) {
		this.txtMonto = txtMonto;
	}
	public UIInput getTxtMotivo() {
		return txtMotivo;
	}
	public void setTxtMotivo(UIInput txtMotivo) {
		this.txtMotivo = txtMotivo;
	}
	public UIInput getDdlSolicitud() {
		return ddlSolicitud;
	}
	public void setDdlSolicitud(UIInput ddlSolicitud) {
		this.ddlSolicitud = ddlSolicitud;
	}	
	public void setLstFiltro(List lstFiltro) {
		this.lstFiltro = lstFiltro;
	}
}

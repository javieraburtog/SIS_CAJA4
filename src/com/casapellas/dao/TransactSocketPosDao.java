package com.casapellas.dao;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlInputTextarea;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.casapellas.controles.CtrlCajas;
import com.casapellas.entidades.F55ca01;
import com.casapellas.entidades.F55ca014;
import com.casapellas.entidades.MetodosPago;
import com.casapellas.entidades.TermAfl;
import com.casapellas.entidades.Transactsp;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.socketpos.PosCtrl;
import com.casapellas.socketpos.TransaccionTerminal;
import com.casapellas.socketpos.bac.pojos.ExecuteTransactionResult;
import com.casapellas.socketpos.bac.transactions.ResponseCodes;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.PropertiesSystem;
import com.casapellas.entidades.ens.Vautoriz;
import com.infragistics.faces.grid.component.RowItem;
import com.infragistics.faces.grid.component.html.HtmlGridView;
import com.infragistics.faces.input.component.html.HtmlCheckBox;
import com.infragistics.faces.input.component.html.HtmlDateChooser;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
import com.infragistics.faces.shared.component.DataRepeater;
import com.infragistics.faces.shared.component.html.HtmlLink;
import com.infragistics.faces.shared.event.PageChangeEvent;
import com.infragistics.faces.shared.smartrefresh.SmartRefreshManager;
import com.infragistics.faces.window.component.html.HtmlDialogWindow;

public class TransactSocketPosDao {
	Map<String, Object> m = FacesContext.getCurrentInstance()
			.getExternalContext().getSessionMap(); 
	
	private HtmlGridView gvTransaccionesSocketPos;
	private List<Transactsp> transaccionesSp;
	private HtmlDialogWindow dwMsgValidacionTransactSP;
	private String msgValidacionesTransactsp;
	private HtmlDialogWindow dwProcesarAnulacionTranSp;
	private String  msgProcesarAnulacionTranSp ;
	private HtmlDropDownList ddlf_Cajas;
	private List<SelectItem> lstf_Cajas;
	private HtmlDropDownList ddlf_Monedas;
	private List<SelectItem> lstf_Monedas;
	private HtmlDateChooser dcFechaInicio;
	private HtmlDateChooser dcFichafinal;
	private HtmlDialogWindow dwSeleccionTerminalBuscar;
	private HtmlGridView gvTerminalesBusqueda;
	private List<TermAfl>terminalesBusqueda;
	private HtmlInputTextarea txtSelectTerminal;
	private HtmlCheckBox chkTransPendiente;
	private HtmlCheckBox chkTransAplicadas;
	private HtmlCheckBox chkTransDenegadas;
	private HtmlCheckBox chkTransAnuladas;
	private HtmlDialogWindow dwRsmTransactSocketPos ;
	private HtmlGridView gvRsmTransactTerminales ;
	private List<TransaccionTerminal> rsmTerminales;
	private HtmlDialogWindow dwConfirmaCierreTerminal ;
	private String msgCerrarTerminal;
	private HtmlDialogWindow dwValidaSocketPos;
	private String msgValidaTransacciones;
	private HtmlLink lnkCerrarTermSinTransact;
	private HtmlGridView gvTransaccionesPOS;
	private List<Transactsp> lstTransaccionesPOS;
	private HtmlDialogWindow dwTransaccionesPOS;
	private HtmlCheckBox mostrarTodosBusqueda ;
	private HtmlInputText txtValorParametroBusqueda;
	private HtmlDropDownList ddlf_tipoParametroBuscar;
	private List<SelectItem> lstf_tipoParametroBuscar;
	
	public void cerrarDetalleTransacciones(ActionEvent ev) {
		CodeUtil.removeFromSessionMap("ac_lstTransaccionesPOS");
		dwTransaccionesPOS.setWindowState("hidden");
	}
	public void cerrarValidaTransacciones(ActionEvent ev){
		dwValidaSocketPos.setWindowState("hidden");
	}
	public void cancelarCierreTerminal(ActionEvent ev){
		dwConfirmaCierreTerminal.setWindowState("hidden");
	}
	public void cerrarResumenTerminal(ActionEvent ev){
		CodeUtil.removeFromSessionMap("tsp_rsmTerminales");
		dwRsmTransactSocketPos.setWindowState("hidden");
	}
	public void cerrarSelectTermBusqueda(ActionEvent ev){
		dwSeleccionTerminalBuscar.setWindowState("hidden");
	}
	public void cerrarProcesarAnulacion(ActionEvent ev){
		dwProcesarAnulacionTranSp.setWindowState("hidden");
	}
	public void cerrarMsgValidaciones(ActionEvent ev){
		dwMsgValidacionTransactSP.setWindowState("hidden");
	}
	public void hola(PageChangeEvent ev){
	}
	/**
	 * Detalle de las transacciones cargadas en la terminal seleccionada.
	 * Creado: 13/08/2014
	 * Carlos Hernandez Morrison
	 */
	public void mostrarTransaccionesTerminal(ActionEvent ev){
		try {
			lstTransaccionesPOS = null;
			CodeUtil.removeFromSessionMap("tsp_lstTransaccionesPOS");
			
			RowItem ri = (RowItem) ev.getComponent().getParent().getParent();
			TransaccionTerminal terminal = (TransaccionTerminal) DataRepeater
					.getDataRow(ri);
			
			if(terminal == null)
				return;
			lstTransaccionesPOS = terminal.getTransacciones();
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}finally{
			if(lstTransaccionesPOS == null)
				lstTransaccionesPOS = new ArrayList<Transactsp>();
			CodeUtil.putInSessionMap("tsp_lstTransaccionesPOS", lstTransaccionesPOS);
			
			gvTransaccionesPOS.dataBind();
			gvTransaccionesPOS.setPageIndex(0);
			dwTransaccionesPOS.setWindowState("normal");
		}
		
	}
	
	/**
	 * aplicar cierre a la terminal aunque no tenga transacciones registradas.
	 * Creado: 13/08/2014
	 * Carlos Hernandez Morrison
	 */
	public void cerrarTerminalSinTransacciones(ActionEvent ev){
		String msg = "";
		
		try {
			if(!m.containsKey("tsp_Terminal_Cerrar")){
				msg = "Datos inválidos para cierre, los datos de la terminal" +
						" deben ser nuevamente cargados ";
				return;
			}
			//&& ====== Aplicar el cierre a la terminal
			TransaccionTerminal terminal = (TransaccionTerminal) CodeUtil.getFromSessionMap("tsp_Terminal_Cerrar");
			
			msg = PosCtrl.cerrarTerminalPOS(terminal, false);
			
			if (msg.isEmpty()) {
				msg = "Cierre Aplicado: Lote #"
						+ terminal.getBatchnumber()
						+ ", Transacciones Aplicadas: "
						+ terminal.getCant_Creditos()
						+ ", Monto: "
						+ new DecimalFormat("#,##0.00").format(terminal.getMto_Creditos());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			CodeUtil.removeFromSessionMap("tsp_Terminal_Cerrar");
			lnkCerrarTermSinTransact.setRendered(false);
			msgValidaTransacciones = msg;
			
			SmartRefreshManager.getCurrentInstance().addSmartRefreshId(
					lnkCerrarTermSinTransact.getClientId(FacesContext
							.getCurrentInstance()));
		}
	}
	
	/**
	 * aplicar cierre de terminal.
	 * Creado: 09/08/2014
	 * Carlos Hernandez Morrison
	 */
	@SuppressWarnings("unchecked")
	public void cerrarTerminalSocketPos(ActionEvent ev){
		String msg = "";
		TransaccionTerminal terminal = null ;
		
		try {
			
			if(!m.containsKey("tsp_Terminal_Cerrar")){
				msg = "Datos inválidos para cierre, los datos de la terminal" +
						" deben ser nuevamente cargados ";
				return;
			}
			
			//&& ====== Aplicar el cierre a la terminal
			terminal = (TransaccionTerminal)CodeUtil.getFromSessionMap("tsp_Terminal_Cerrar");
			
			int iSufijo = Integer
					.parseInt((int) Math.round(Math.random() * 100) + ""
							+ (int) Math.round(Math.random() * 10));
			
			String realpath = FacesContext
					.getCurrentInstance().getExternalContext()
					.getRealPath( File.separatorChar + "Confirmacion" + File.separatorChar);
			
			String filaname = "_CierreTerminal_"+terminal.getTerminalid()+".pdf";
			
			terminal.setRutarealreporte(realpath+""+iSufijo+""+filaname);
			terminal.setNombrereporte(+iSufijo+""+filaname);
			
			msg = PosCtrl.cerrarTerminalPOS(terminal, true);
			
			if(msg.compareTo("") != 0) {
				return;
			}
			
			//&& ========== generar Correo de notificacion de cierre de terminal.
			ArqueoCajaDAO.enviarCierreSocketCajero(terminal);
			
			//&& ========== Actualizar la lista de terminales
			rsmTerminales = (ArrayList<TransaccionTerminal>) m
					.get("tsp_rsmTerminales");
			for (int i = 0; i < rsmTerminales.size(); i++) {
				TransaccionTerminal tmp = rsmTerminales.get(i);
				if(tmp.getTerminalid().compareTo(terminal.getTerminalid()) == 0 ){
					rsmTerminales.set(i, terminal);
					break;
				}
			}
			
			CodeUtil.putInSessionMap("tsp_rsmTerminales", rsmTerminales);
			int pagina = gvRsmTransactTerminales.getPageIndex() ;
			gvRsmTransactTerminales.dataBind();
			gvRsmTransactTerminales.setPageIndex(pagina);
			
			CodeUtil.removeFromSessionMap("tsp_transaccionesSp");
			
			if(m.containsKey("tst_TransaccionesPorFiltro")) {
				buscarTransaccionesPorFiltros(ev);
			}
			
			gvTransaccionesSocketPos.dataBind();
			
		} catch (Exception e) { 
			e.printStackTrace();
		}finally{
			
			dwConfirmaCierreTerminal.setWindowState("hidden");
			CodeUtil.removeFromSessionMap("tsp_Terminal_Cerrar");
			
			if (terminal != null && terminal.getRutarealreporte() != null
					&& new File(terminal.getRutarealreporte()).exists())
				new File(terminal.getRutarealreporte()).delete();
			
			//&& ======= Ventana de notificacion del proceso.
			if(msg.isEmpty())
				msg = "Cierre terminal aplicado correctamente";
			
			lnkCerrarTermSinTransact.setRendered(false);
			msgValidaTransacciones = msg;
			dwValidaSocketPos.setWindowState("normal");
		
		}
	}
	/**
	 * validaciones previas al cierre de la terminal
	 * Creado: 09/08/2014
	 * Carlos Hernandez Morrison
	 */
	public void confirmaCierreTerminal(ActionEvent ev) {
		String msg = "";
		TransaccionTerminal terminal = null ;
		
		try {
			
			msg = PosCtrl.credomatic_SocketPos_TestConnection();

			if (!msg.trim().isEmpty()) {
				return;
			}
			
			CodeUtil.removeFromSessionMap("tsp_Terminal_Cerrar");
			
			RowItem ri = (RowItem) ev.getComponent().getParent().getParent();
			terminal = (TransaccionTerminal) DataRepeater.getDataRow(ri);
			
			if (terminal == null) {
				msg = "Cierre de terminal no puede ser procesado, Error al " +
						"obtener datos de terminal";
				return;
			}
			if (terminal.isTerm_cerrada()) {
				msg = "No se puede aplicar cierre a la terminal seleccionada";
				return;
			}
			if (terminal.getTotalcierre().compareTo(BigDecimal.ZERO) <= 0) {
				msg = " No hay Transacciones Registradas para la Terminal ";
				CodeUtil.putInSessionMap("tsp_Terminal_Cerrar", terminal);
				lnkCerrarTermSinTransact.setRendered(true);
				return;
			}
			
			String pruebaConexion = PosCtrl.credomatic_SocketPos_TestConnection();
			 
			if( !pruebaConexion.isEmpty() ) {
				msg = "No se ha podido establecer conexión con Socket Pos Credomatic ";
				return;
			}
			
			/*
			List<String> pruebaConexion = PosCtrl.probarConexion();
			if (pruebaConexion == null || pruebaConexion.isEmpty()
					|| pruebaConexion.get(0).contains("EC")) {
				msg = "Comunicación con Servidor SocketPOS no válida, "
						+ "favor intente nuevamente";
				return;
			}
			*/
			
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = "Cierre no puede ser procesado, datos de cierre no válidos";
		} finally {
			
			if(msg.isEmpty()){
				CodeUtil.putInSessionMap("tsp_Terminal_Cerrar", terminal);
				msgCerrarTerminal = " Se aplicará cierre de Terminal "
						+ terminal.getTerminalid() + " por "
						+ terminal.getTotalcierre();
				dwConfirmaCierreTerminal.setWindowState("normal");
			}else{
				dwValidaSocketPos.setWindowState("normal");
				msgValidaTransacciones = msg;
			}
		}
	}
	/**
	 * Mostrar resumen de las terminales configuradas, para aplicar cierre
	 * Creado: 09/08/2014
	 * Carlos Hernandez Morrison
	 */
	public void mostrarTerminalesCierre(ActionEvent ev){
		try {
			String coduser = ((Vautoriz[])CodeUtil.getFromSessionMap("sevAut"))[0].getId().getLogin();
			int codemp = ((Vautoriz[])CodeUtil.getFromSessionMap("sevAut"))[0].getId().getCodreg();
			
			rsmTerminales = PosCtrl.crearResumenPorTerminal(0,coduser, codemp);
			
			if (rsmTerminales != null && !rsmTerminales.isEmpty()) {
				Collections.sort(rsmTerminales,
						new Comparator<TransaccionTerminal>() {
//							@Override
							public int compare(TransaccionTerminal t1,
									TransaccionTerminal t2) {
								return (t2.getCant_transacciones() > t1
										.getCant_transacciones()) ? 1 : (t2
										.getCant_transacciones() < t1
										.getCant_transacciones()) ? -1 : 0;
							}
						});
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			rsmTerminales = new ArrayList<TransaccionTerminal>();
		}finally{
			CodeUtil.putInSessionMap("tsp_rsmTerminales", rsmTerminales);
			dwRsmTransactSocketPos.setWindowState("normal");
			gvRsmTransactTerminales.dataBind();
			gvRsmTransactTerminales.setPageIndex(0);
		}
	}
	/**
	 * seleccionar las terminales para filtrar resultados
	 * Creado: 08/05/2014
	 * Carlos Hernandez Morrison
	 */
	public void seleccionarTerminales(ActionEvent ev){
		String terminales = "";
		try {
			@SuppressWarnings("unchecked")
			List<RowItem> seleccionados = gvTerminalesBusqueda.getSelectedRows();
			if(seleccionados.isEmpty()){
				terminales = "";
				return;
			}
			for (RowItem ri : seleccionados) {
				terminales += "'"+((TermAfl) DataRepeater.getDataRow(ri)).getId().getTermId() + "',";
			}
			if(terminales.endsWith(","))
				terminales = terminales.substring(0, terminales.length()- 1);
			
		} catch (Exception e) {
			e.printStackTrace();
			terminales = "";
		}finally{
			txtSelectTerminal.setValue(terminales);
			dwSeleccionTerminalBuscar.setWindowState("hidden");
		}
	}
	/**
	 * busqueda de Transacciones segun valores en filtros seleccionados
	 * Creado: 08/05/2014
	 * Carlos Hernandez Morrison
	 */
	public void buscarTransaccionesPorFiltros(ActionEvent ev) {
		try {

			CodeUtil.putInSessionMap("tst_TransaccionesPorFiltro", true);
			CodeUtil.removeFromSessionMap("tsp_transaccionesSp");
			
			int caid = Integer.parseInt(ddlf_Cajas.getValue().toString());
			String moneda = ddlf_Monedas.getValue().toString();
			String terminales = (txtSelectTerminal.getValue() != null) ? 
						txtSelectTerminal.getValue().toString() : "";
			String valorParam = (txtValorParametroBusqueda.getValue() != null) ?
						txtValorParametroBusqueda.getValue().toString().trim() : "";
			String parametro = ddlf_tipoParametroBuscar.getValue().toString();			
			String estados = "";

			Date fechaini = null;
			Date fechafin = null;

			if (dcFechaInicio.getValue() != null)
				fechaini = (Date) dcFechaInicio.getValue();
			if (dcFichafinal.getValue() != null)
				fechafin = (Date) dcFichafinal.getValue();
			if (fechaini != null && fechafin != null
					&& fechaini.compareTo(fechafin) == 1) {
				fechaini = fechafin;
				fechafin = (Date) dcFechaInicio.getValue();
			}
			if (moneda.compareTo("00") == 0)
				moneda = "";
			if (terminales == null || terminales.compareTo("") == 0)
				moneda = "";
			
			if (chkTransPendiente.isChecked())
				estados += "'PND',";
			if (chkTransAplicadas.isChecked())
				estados += "'APL',";
			if (chkTransDenegadas.isChecked())
				estados += "'DNG',";
			if (chkTransAnuladas.isChecked())
				estados += "'ANL',";
			if (estados.endsWith(","))
				estados = estados.substring(0, estados.length() - 1);
			
			if (!valorParam.isEmpty() && valorParam.matches(
					PropertiesSystem.REGEXP_DESCRIPTION)) {
				if (parametro.compareTo("00") == 0)
					parametro = " clientname ";
				if (parametro.compareTo("01") == 0)
					parametro = " cardnumber ";
				if (parametro.compareTo("02") == 0)
					parametro = " amount ";
				if (parametro.compareTo("03") == 0)
					parametro = " acqnumber ";
			}
			
			int imaxResult = 500 ;
			if (mostrarTodosBusqueda.isChecked() )
				imaxResult = 0 ;

			transaccionesSp = PosCtrl.getTransactsp_Filters(caid, terminales,
					fechaini, fechafin, moneda, estados, parametro, valorParam,
					imaxResult);

		} catch (Exception e) {
			e.printStackTrace();
			transaccionesSp = new ArrayList<Transactsp>();
		} finally {
			
			if( transaccionesSp == null )
				transaccionesSp = new ArrayList<Transactsp>();
			 
			CodeUtil.putInSessionMap("tsp_transaccionesSp", transaccionesSp);
			gvTransaccionesSocketPos.dataBind();
			gvTransaccionesSocketPos.setPageIndex(0);
			
			SmartRefreshManager.getCurrentInstance().addSmartRefreshId(
					gvTransaccionesSocketPos.getClientId(FacesContext
							.getCurrentInstance()));
			
		}
	}
	/**
	 * Proceso de Anulacion para la transaccion de SocketPos
	 * Creado: 07/05/2014
	 * Carlos Hernandez Morrison
	 */
	public void procesarAnulacionSp(ActionEvent ev) {
		String msg = new String("");
		Transactsp tsp = null;
		
		try {
			if( !m.containsKey("tsp_TransactspAnular") ){
				msg = "Datos de Transacción no encontrados, favor intente nuevamennte ";
				return;
			}
			tsp = (Transactsp)CodeUtil.getFromSessionMap("tsp_TransactspAnular");
			
			//&& ========== Anular la transaccion
			
			ExecuteTransactionResult tr = 
					PosCtrl.anularCobroCredomatic(
							tsp.getTermid().trim(),
							tsp.getReferencenumber().trim(),
							tsp.getAuthorizationid().trim(), 
							tsp.getSystraceno().trim() 
						);
			
			String responseCode = "";
			if(tr != null) {
				responseCode = tr.getResponseCode();
			}
			if(responseCode.compareTo( ResponseCodes.APROBADO.CODE) != 0 ){
				msg = "Transacción no anulada";
				return;
			} 
			
			/*
			List<String> lstResult = PosCtrl.anularTransaccionPOS(
					tsp.getTermid(), tsp.getReferencenumber(),
					tsp.getAuthorizationid(), tsp.getSystraceno());
			
			if (lstResult.get(0).compareTo("00") != 0) {
				msg = "Transacción no anulada";
				return;
			}
			*/
			
			
			tsp.setStatus("ANL");
			
			List<MetodosPago>lstMpPagoS = new ArrayList<MetodosPago>();
			lstMpPagoS.add( MetodosPago.copyFromTransactSp(tsp));
			
			//&& ========== Imprimir el voucher de anulacion
			F55ca014 dtComp = com.casapellas.controles.tmp.CompaniaCtrl
					.filtrarCompania((F55ca014[])
						CodeUtil.getFromSessionMap("cont_f55ca014"), tsp.getCodcomp());
			
			PosCtrl.imprimirVoucher(lstMpPagoS, "A", 
					dtComp.getId().getC4rp01d1(), 
					dtComp.getId().getC4prt());	
			
			//&& ========== marcar como anuladas las transacciones.
			PosCtrl.actualizarTransacciones(lstMpPagoS, tsp.getCaid(), tsp.getCodcomp(), tsp.getTiporec() );
			
		} catch (Exception e) {
			e.printStackTrace();
			msg =  "";
		} finally {

			dwProcesarAnulacionTranSp.setWindowState("hidden");

			if (msg.compareTo("") == 0){
				msg = "Transacción anulada correctamente";
				int pageIndex = gvTransaccionesSocketPos.getPageIndex();
				gvTransaccionesSocketPos.dataBind();
				gvTransaccionesSocketPos.setPageIndex(pageIndex);
			}
			dwMsgValidacionTransactSP.setWindowState("normal");
			msgValidacionesTransactsp = msg;
		}
	}
	/**
	 * Validaciones previas a la anulacion de transaccion de Socket Pos
	 * Creado: 05/07/2014
	 * Carlos Hernandez Morrison
	 */
	public void validarAnularTransaccion(ActionEvent ev) {
		String msg = "";
		Transactsp tsp = null;
		
		try {
			
			msg = PosCtrl.credomatic_SocketPos_TestConnection() ;
			if(!msg.isEmpty()) {
				return;
			}
			
			CodeUtil.removeFromSessionMap("tsp_TransactspAnular");
			
			tsp = (Transactsp) DataRepeater.getDataRow(((RowItem) ev
					.getComponent().getParent().getParent()));

			if (tsp.getStatcred() == 1) {
				msg = "La transacción ya fue aplicada en Banco, no puede ser anulada";
				return;
			}
			if (tsp.getStatus() != null
					&& tsp.getStatus().compareTo("PND") != 0) {
				msg = "La Transacción debe estar pendiente para poder anularse";
				return;
			}
			if (tsp.getTermid() != null && tsp.getTermid().compareTo("") == 0) {
				msg = "Valor no permitido para el campo IdTerminal: "
						+ tsp.getTermid();
				return;
			}
			if (tsp.getReferencenumber() != null
					&& tsp.getReferencenumber().compareTo("") == 0) {
				msg = "Valor no permitido para el campo ReferenceNumber: "
						+ tsp.getReferencenumber();
				return;
			}
			if (tsp.getAuthorizationid() != null
					&& tsp.getAuthorizationid().compareTo("") == 0) {
				msg = "Valor no permitido para el campo AuthorizationId: "
						+ tsp.getAuthorizationid();
				return;
			}
			if (tsp.getAmount() != null
					&& tsp.getAmount().compareTo(BigDecimal.ZERO) != 1) {
				msg = "Valor no permitido para el campo Amount: "
						+ tsp.getAmount();
				return;
			}
			if (tsp.getSystraceno().compareTo("") == 0) {
				msg = "Valor no permitido para el campo SysTraceNo: "
						+ tsp.getSystraceno();
				return;
			}
			
			String pruebaConexion = PosCtrl.credomatic_SocketPos_TestConnection();
			 
			if( !pruebaConexion.isEmpty() ) {
				msg = "No se ha podido establecer conexión con Socket Pos Credomatic "; 
				return;
			} 
			 

		} catch (Exception e) {
			e.printStackTrace();
			msg = "Error al validar datos de la transacción. "
					+ "Favor intentar nuevamente";
		} finally {
			
			if (msg.compareTo("") != 0) {
				msgValidacionesTransactsp = msg;
				dwMsgValidacionTransactSP.setWindowState("normal");
				return;
			}
			String prefix = tsp.getCurrency().compareTo("COR")==0 ?"C$":"US$";
			
			CodeUtil.putInSessionMap("tsp_TransactspAnular", tsp);
			msgProcesarAnulacionTranSp = "¿ Anular transaccion # "
					+ tsp.getReferencenumber().trim() + ", para tarjeta "
					+ tsp.getCardnumber() + " por "
					+ new DecimalFormat("#,##0.00").format(tsp.getAmount())
					+ " "+prefix + " ?";
			
			dwProcesarAnulacionTranSp.setWindowState("normal");
		}
	}
	public void mostrarTerminalesParaBusqueda(ActionEvent ev){
		try {
			gvTerminalesBusqueda.setPageIndex(0);
			gvTerminalesBusqueda.dataBind();
			dwSeleccionTerminalBuscar.setWindowState("normal");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	public List<Transactsp> getTransaccionesSp() {

		try {

			if (m.containsKey("tsp_transaccionesSp")) {
				return transaccionesSp = (ArrayList<Transactsp>) m
						.get("tsp_transaccionesSp");
			}
			transaccionesSp = PosCtrl.getTransactsp_Filters(0, "", new Date(),
					null, "", "", "", "", 0);

		} catch (Exception e) {
			e.printStackTrace();
			transaccionesSp = new ArrayList<Transactsp>();
		} finally {
			CodeUtil.putInSessionMap("tsp_transaccionesSp", transaccionesSp);
		}

		return transaccionesSp;
	}
	public void setTransaccionesSp(List<Transactsp> transaccionesSp) {
		this.transaccionesSp = transaccionesSp;
	}
	public TransactSocketPosDao() {
	}
	public HtmlDialogWindow getDwMsgValidacionTransactSP() {
		return dwMsgValidacionTransactSP;
	}
	public void setDwMsgValidacionTransactSP(
			HtmlDialogWindow dwMsgValidacionTransactSP) {
		this.dwMsgValidacionTransactSP = dwMsgValidacionTransactSP;
	}
	public HtmlGridView getGvTransaccionesSocketPos() {
		return gvTransaccionesSocketPos;
	}
	public void setGvTransaccionesSocketPos(HtmlGridView gvTransaccionesSocketPos) {
		this.gvTransaccionesSocketPos = gvTransaccionesSocketPos;
	}
	public String getMsgValidacionesTransactsp() {
		if(msgValidacionesTransactsp == null)
			msgValidacionesTransactsp = "";
		return msgValidacionesTransactsp;
	}
	public void setMsgValidacionesTransactsp(String msgValidacionesTransactsp) {
		this.msgValidacionesTransactsp = msgValidacionesTransactsp;
	}
	public HtmlDialogWindow getDwProcesarAnulacionTranSp() {
		return dwProcesarAnulacionTranSp;
	}
	public void setDwProcesarAnulacionTranSp(
			HtmlDialogWindow dwProcesarAnulacionTranSp) {
		this.dwProcesarAnulacionTranSp = dwProcesarAnulacionTranSp;
	}
	public String getMsgProcesarAnulacionTranSp() {
		if(msgProcesarAnulacionTranSp == null)
			msgProcesarAnulacionTranSp = "";
		return msgProcesarAnulacionTranSp;
	}
	public void setMsgProcesarAnulacionTranSp(String msgProcesarAnulacionTranSp) {
		this.msgProcesarAnulacionTranSp = msgProcesarAnulacionTranSp;
	}
	public HtmlDropDownList getDdlf_Cajas() {
		return ddlf_Cajas;
	}
	public void setDdlf_Cajas(HtmlDropDownList ddlf_Cajas) {
		this.ddlf_Cajas = ddlf_Cajas;
	}
	@SuppressWarnings("unchecked")
	public List<SelectItem> getLstf_Cajas() {
		try {
			if (m.containsKey("tsp_ListaCajas")) {
				return lstf_Cajas = (ArrayList<SelectItem>) m
						.get("tsp_ListaCajas");
			}
			lstf_Cajas = new ArrayList<SelectItem>();
			lstf_Cajas.add(new SelectItem("00", "Todas",
					"Todas las cajas disponibles"));

			List<F55ca01> cajas = CtrlCajas.getCajasF55ca01();
			for (F55ca01 caja : cajas) {
				lstf_Cajas.add(new SelectItem(String.valueOf(caja.getId()
						.getCaid()), caja.getId().getCaid() + " "
						+ caja.getId().getCaname().toLowerCase(), caja.getId()
						.getCaname().toLowerCase()));
			}

		} catch (Exception e) {
			lstf_Cajas = new ArrayList<SelectItem>();
			e.printStackTrace();
		} finally {
			CodeUtil.putInSessionMap("tsp_ListaCajas", lstf_Cajas);
		}
		return lstf_Cajas;
	}
	public void setLstf_Cajas(List<SelectItem> lstf_Cajas) {
		this.lstf_Cajas = lstf_Cajas;
	}
	public HtmlDropDownList getDdlf_Monedas() {
		return ddlf_Monedas;
	}
	public void setDdlf_Monedas(HtmlDropDownList ddlf_Monedas) {
		this.ddlf_Monedas = ddlf_Monedas;
	}
	public List<SelectItem> getLstf_Monedas() {
		lstf_Monedas = new ArrayList<SelectItem>();
		lstf_Monedas.add(new SelectItem("00", "Todas", "Todas las monedas configuradas"));
		lstf_Monedas.add(new SelectItem("COR", "Córdobas", ""));
		lstf_Monedas.add(new SelectItem("USD", "Dólares", ""));
		return lstf_Monedas;
	}
	public void setLstf_Monedas(List<SelectItem> lstf_Monedas) {
		this.lstf_Monedas = lstf_Monedas;
	}
	public HtmlDateChooser getDcFechaInicio() {
		return dcFechaInicio;
	}
	public void setDcFechaInicio(HtmlDateChooser dcFechaInicio) {
		this.dcFechaInicio = dcFechaInicio;
	}
	public HtmlDateChooser getDcFichafinal() {
		return dcFichafinal;
	}
	public void setDcFichafinal(HtmlDateChooser dcFichafinal) {
		this.dcFichafinal = dcFichafinal;
	}
	public HtmlDialogWindow getDwSeleccionTerminalBuscar() {
		return dwSeleccionTerminalBuscar;
	}
	public void setDwSeleccionTerminalBuscar(
			HtmlDialogWindow dwSeleccionTerminalBuscar) {
		this.dwSeleccionTerminalBuscar = dwSeleccionTerminalBuscar;
	}
	public HtmlGridView getGvTerminalesBusqueda() {
		return gvTerminalesBusqueda;
	}
	public void setGvTerminalesBusqueda(HtmlGridView gvTerminalesBusqueda) {
		this.gvTerminalesBusqueda = gvTerminalesBusqueda;
	}
	@SuppressWarnings("unchecked")
	public List<TermAfl> getTerminalesBusqueda() {

		try {
			if (m.containsKey("tsp_terminalesBusqueda")) {
				return terminalesBusqueda = (ArrayList<TermAfl>) m
						.get("tsp_terminalesBusqueda");
			}
			terminalesBusqueda = new ArrayList<TermAfl>();
			Session sesion = HibernateUtilPruebaCn.currentSession();
			Transaction trans = sesion.beginTransaction();
			terminalesBusqueda = (ArrayList<TermAfl>) sesion
					.createCriteria(TermAfl.class)
					.add(Restrictions.eq("id.status", "A")).list();
			trans.commit();
			HibernateUtilPruebaCn.closeSession(sesion);

		} catch (Exception e) {
			e.printStackTrace();
			terminalesBusqueda = new ArrayList<TermAfl>();
		} finally {
			CodeUtil.putInSessionMap("tsp_terminalesBusqueda", terminalesBusqueda);
		}
		return terminalesBusqueda;
	}
	public void setTerminalesBusqueda(List<TermAfl> terminalesBusqueda) {
		this.terminalesBusqueda = terminalesBusqueda;
	}
	public HtmlInputTextarea getTxtSelectTerminal() {
		return txtSelectTerminal;
	}
	public void setTxtSelectTerminal(HtmlInputTextarea txtSelectTerminal) {
		this.txtSelectTerminal = txtSelectTerminal;
	}
	public HtmlCheckBox getChkTransPendiente() {
		return chkTransPendiente;
	}
	public void setChkTransPendiente(HtmlCheckBox chkTransPendiente) {
		this.chkTransPendiente = chkTransPendiente;
	}
	public HtmlCheckBox getChkTransAplicadas() {
		return chkTransAplicadas;
	}
	public void setChkTransAplicadas(HtmlCheckBox chkTransAplicadas) {
		this.chkTransAplicadas = chkTransAplicadas;
	}
	public HtmlCheckBox getChkTransDenegadas() {
		return chkTransDenegadas;
	}
	public void setChkTransDenegadas(HtmlCheckBox chkTransDenegadas) {
		this.chkTransDenegadas = chkTransDenegadas;
	}
	public HtmlCheckBox getChkTransAnuladas() {
		return chkTransAnuladas;
	}
	public void setChkTransAnuladas(HtmlCheckBox chkTransAnuladas) {
		this.chkTransAnuladas = chkTransAnuladas;
	}
	public HtmlDialogWindow getDwRsmTransactSocketPos() {
		return dwRsmTransactSocketPos;
	}
	public void setDwRsmTransactSocketPos(HtmlDialogWindow dwRsmTransactSocketPos) {
		this.dwRsmTransactSocketPos = dwRsmTransactSocketPos;
	}
	public HtmlGridView getGvRsmTransactTerminales() {
		return gvRsmTransactTerminales;
	}
	public void setGvRsmTransactTerminales(HtmlGridView gvRsmTransactTerminales) {
		this.gvRsmTransactTerminales = gvRsmTransactTerminales;
	}
	@SuppressWarnings("unchecked")
	public List<TransaccionTerminal> getRsmTerminales() {
		rsmTerminales = new ArrayList<TransaccionTerminal>();
		if (m.containsKey("tsp_rsmTerminales")) {
			rsmTerminales = (ArrayList<TransaccionTerminal>) m
					.get("tsp_rsmTerminales");
		}
		return rsmTerminales;
	}
	public void setRsmTerminales(List<TransaccionTerminal> rsmTerminales) {
		this.rsmTerminales = rsmTerminales;
	}
	public HtmlDialogWindow getDwConfirmaCierreTerminal() {
		return dwConfirmaCierreTerminal;
	}
	public void setDwConfirmaCierreTerminal(
			HtmlDialogWindow dwConfirmaCierreTerminal) {
		this.dwConfirmaCierreTerminal = dwConfirmaCierreTerminal;
	}
	public String getMsgCerrarTerminal() {
		return msgCerrarTerminal;
	}
	public void setMsgCerrarTerminal(String msgCerrarTerminal) {
		this.msgCerrarTerminal = msgCerrarTerminal;
	}
	public HtmlDialogWindow getDwValidaSocketPos() {
		return dwValidaSocketPos;
	}
	public void setDwValidaSocketPos(HtmlDialogWindow dwValidaSocketPos) {
		this.dwValidaSocketPos = dwValidaSocketPos;
	}
	public String getMsgValidaTransacciones() {
		return msgValidaTransacciones;
	}
	public void setMsgValidaTransacciones(String msgValidaTransacciones) {
		this.msgValidaTransacciones = msgValidaTransacciones;
	}
	public HtmlLink getLnkCerrarTermSinTransact() {
		return lnkCerrarTermSinTransact;
	}
	public void setLnkCerrarTermSinTransact(HtmlLink lnkCerrarTermSinTransact) {
		this.lnkCerrarTermSinTransact = lnkCerrarTermSinTransact;
	}
	public HtmlGridView getGvTransaccionesPOS() {
		return gvTransaccionesPOS;
	}
	public void setGvTransaccionesPOS(HtmlGridView gvTransaccionesPOS) {
		this.gvTransaccionesPOS = gvTransaccionesPOS;
	}
	@SuppressWarnings("unchecked")
	public List<Transactsp> getLstTransaccionesPOS() {
		lstTransaccionesPOS = new ArrayList<Transactsp>();
		if (m.containsKey("tsp_lstTransaccionesPOS")) {
			lstTransaccionesPOS = (ArrayList<Transactsp>) m
					.get("tsp_lstTransaccionesPOS");
		}
		return lstTransaccionesPOS;
	}
	public void setLstTransaccionesPOS(List<Transactsp> lstTransaccionesPOS) {
		this.lstTransaccionesPOS = lstTransaccionesPOS;
	}
	public HtmlDialogWindow getDwTransaccionesPOS() {
		return dwTransaccionesPOS;
	}
	public void setDwTransaccionesPOS(HtmlDialogWindow dwTransaccionesPOS) {
		this.dwTransaccionesPOS = dwTransaccionesPOS;
	}
	public HtmlCheckBox getMostrarTodosBusqueda() {
		return mostrarTodosBusqueda;
	}
	public void setMostrarTodosBusqueda(HtmlCheckBox mostrarTodosBusqueda) {
		this.mostrarTodosBusqueda = mostrarTodosBusqueda;
	}
	public HtmlInputText getTxtValorParametroBusqueda() {
		return txtValorParametroBusqueda;
	}
	public void setTxtValorParametroBusqueda(HtmlInputText txtValorParametroBusqueda) {
		this.txtValorParametroBusqueda = txtValorParametroBusqueda;
	}
	public HtmlDropDownList getDdlf_tipoParametroBuscar() {
		return ddlf_tipoParametroBuscar;
	}
	public void setDdlf_tipoParametroBuscar(
			HtmlDropDownList ddlf_tipoParametroBuscar) {
		this.ddlf_tipoParametroBuscar = ddlf_tipoParametroBuscar;
	}
	public List<SelectItem> getLstf_tipoParametroBuscar() {
		
		if(lstf_tipoParametroBuscar != null)
			return lstf_tipoParametroBuscar ;
		
		lstf_tipoParametroBuscar = new ArrayList<SelectItem>();
		lstf_tipoParametroBuscar.add(new SelectItem("00","Nombre Cliente","Nombre del cliente"));
		lstf_tipoParametroBuscar.add(new SelectItem("01","Número Tarjeta","Cuatro últimos dígitos de la tarjeta"));
		lstf_tipoParametroBuscar.add(new SelectItem("02","Monto aplicado","Monto del pago"));
		lstf_tipoParametroBuscar.add(new SelectItem("03","Número afiliado","Número del afiliado"));
		
		return lstf_tipoParametroBuscar;
	}
	public void setLstf_tipoParametroBuscar(
			List<SelectItem> lstf_tipoParametroBuscar) {
		this.lstf_tipoParametroBuscar = lstf_tipoParametroBuscar;
	}
}

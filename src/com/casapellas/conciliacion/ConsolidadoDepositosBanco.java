package com.casapellas.conciliacion;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.faces.component.UIComponentBase;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlInputTextarea;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.casapellas.conciliacion.entidades.ConsolidadoCoincidente;
import com.casapellas.conciliacion.entidades.PcdConsolidadoDepositosBanco;
import com.casapellas.conciliacion.entidades.ResumenDepositosTipoTransaccion;
import com.casapellas.controles.BancoCtrl;
import com.casapellas.controles.ClsParametroCaja;
import com.casapellas.controles.ConfirmaDepositosCtrl;
import com.casapellas.controles.ConsolidadoDepositosBcoCtrl;

import com.casapellas.entidades.CajaParametro;
import com.casapellas.entidades.Deposito;
//import com.casapellas.entidades.Deposito;
import com.casapellas.entidades.Deposito_Report;
import com.casapellas.entidades.F55ca014;
import com.casapellas.entidades.F55ca022;
import com.casapellas.entidades.F55ca023;
import com.casapellas.entidades.ens.Vautoriz;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.PropertiesSystem;
import com.infragistics.faces.grid.component.RowItem;
import com.infragistics.faces.grid.component.html.HtmlGridView;
import com.infragistics.faces.input.component.html.HtmlCheckBox;
import com.infragistics.faces.input.component.html.HtmlDateChooser;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
import com.infragistics.faces.shared.component.DataRepeater;
import com.infragistics.faces.shared.component.html.HtmlLink;
import com.infragistics.faces.shared.smartrefresh.SmartRefreshManager;
import com.infragistics.faces.window.component.html.HtmlDialogWindow;
import com.infragistics.faces.window.component.html.HtmlDialogWindowHeader;
import com.prueba.ws.P5509800Input;
import com.prueba.ws.P5509800PortType;
import com.prueba.ws.P5509800_Service;

public class ConsolidadoDepositosBanco {
	
	public ConsolidadoDepositosBanco() {
	}
	private HtmlGridView gvConsolidadoDepositosBanco;
	private List<PcdConsolidadoDepositosBanco> lstConsolidaDepbsBco;
	private HtmlGridView gvResumenTipoTransaccion;
	private List<ResumenDepositosTipoTransaccion> lstResumenTipoTransaccion;
	private HtmlDateChooser dcFechaDesde;
	private HtmlDateChooser dcFechaHasta;
	private HtmlInputText txtMontoInicial;
	private HtmlInputText txtMontoFinal;
	private HtmlInputText txtNumeroReferencia ;
	private HtmlDropDownList cmbTransaccionesJde;
	private HtmlDropDownList cmbTransaccionesBanco;
	private List<SelectItem> lstTransaccionesBanco ;
	private List<SelectItem> lstTransaccionesJde ;
	private HtmlDropDownList cmbBancosPreconfirmacion;
	private HtmlDropDownList cmbMonedasDeposito;
	private List<SelectItem> lstBancosPreconfirmacion ;
	private List<SelectItem> lstMonedasDeposito ;
	private HtmlOutputText lblResultadoFiltrarDepositos;
	final static String valorInicialListaCombo = "00";
	private HtmlGridView gvResumenConsolidadoDepositos ;
	private List<ResumenDepositosTipoTransaccion> ltResumenConsolidadoDepositos;
	private HtmlInputText txtCodigosFiltroBanco;
	private HtmlDialogWindow dwComparacionDepositos;
	private HtmlGridView gvDepositoEnCoincidencia;
	private List<ConsolidadoCoincidente> lstDepositoEnCoincidencia ;
	private List<ConsolidadoCoincidente> lstDepositoEnCoincidenciaConflicto ;
	private HtmlOutputText rsmTotalDepBco;
	private HtmlOutputText rsmTotalCoincidenciasBco;
	private HtmlOutputText rsmTotalDepCaja;
	private HtmlOutputText rsmTotalCoincidenciasCaja;
	private HtmlOutputText rsmCoincidentesUnoAuno;
	private HtmlOutputText rsmCoincidentesUnoAMuchos;
	private HtmlInputTextarea txtNivelesCompara ;
	private String sesmapvarDepsCoincidencias = "sesmapvarDepsCoincidencias" ;
	private String sesmapvarDepsCoincidenciasConflicto = "sesmapvarDepsCoincidenciasConflicto" ;
	private HtmlDialogWindow dwExcluirCoincidencia ;
	private HtmlOutputText  msgConfirmaExcluirCoincidencia ;
	private HtmlInputTextarea txtMotivoExclusionConDeps ;
	private HtmlDialogWindow  dwInvocarComparacionDepositos ;
	private HtmlOutputText  rsmRangoFechasBanco ;
	private HtmlOutputText  rsmRangoFechasCaja ;
	private HtmlOutputText totalCoincidenciaEnConflicto;

	private HtmlDialogWindow dwValidacionConflictos ;
	private HtmlGridView gvConflictosCoincidenciaDepositos;
	private HtmlDialogWindow dwMensajesValidacion ;
	private HtmlOutputText lblMensajeValidacionProcesos ;
	private HtmlDialogWindow dwConfirmarConflictoValido ;
	private HtmlOutputText lblmsgConfirmaConflictoValido;
	private HtmlDialogWindow dwConfirmarConflictoNoValido ;
	private HtmlOutputText lblmsgConfirmaConflictoNoValido ;
	private HtmlOutputText msgValidaResultadoComparacion ;
	
	private HtmlOutputText strRsmTotalDepBco;
	private HtmlOutputText strRsmTotalCoincidenciasBco;
	private HtmlOutputText strRsmTotalDepCaja;
	private HtmlOutputText strRsmTotalCoincidenciasCaja;
	private HtmlOutputText strRsmCoincidentesUnoAuno;
	private HtmlOutputText strRsmCoincidentesUnoAMuchos;
	private HtmlOutputText strTotalCoincidenciaEnConflicto;
	private HtmlOutputText strRsmRangoFechasBanco;
	private HtmlOutputText strRsmRangoFechasCaja;
	private HtmlInputTextarea txtNivelesComparaValue;
	private HtmlLink lnkValidarConflictos;
	
	private HtmlDialogWindow dwConfirmacionProcesarcoincidencia ;
	private HtmlOutputText lblMsgConfirmacionProcesaCoincidencia;
	private HtmlDialogWindow dwResultadoGeneracionComprobante;
	
	private HtmlDialogWindow dwValidaConfirmaDepositoIndividual; 
	private HtmlOutputText lblmsgConfirmaComprobanteIndividual;
	
	private HtmlDialogWindow dwResumenCoincidenciasPorNiveles ;
	private HtmlGridView gvResumenCoincidenciasPorNiveles;
	private List<NivelComparacionDeposito> lstResumenCoincidenciasPorNiveles;
	private HtmlOutputText lblResumenComparacionNivel ;
	
	private HtmlDialogWindow  dwProcesarDepositosCoincidentes ;
	private HtmlGridView gvProcesarDepositosCoincidentes;
	private List<ConsolidadoCoincidente> lstProcesarDepositosCoincidentes;
	private HtmlDialogWindowHeader dwHeaderProcesarDepositosCoincidentes;

	private HtmlDialogWindow  dwSometerCoincidenciaAExcepcion ;
	private HtmlInputTextarea txtMotivoSometeExcepcion ;
	private HtmlCheckBox chkExcluirBanco;
	private HtmlCheckBox chkExcluirCaja;
	
	private HtmlDialogWindow dwProcesarEnAjustePorExcepcion;
	private HtmlDropDownList cmbCuentaAjustePorExcepcion;
	private List<SelectItem> lstCuentaAjustePorExcepcion;
	private HtmlInputTextarea txtMotivoProcesarAjustePorExcepcion ;
	
	private String hdrColmCoincidenciaMtoBanco;
	private String hdrColmCoincidenciaMtoCaja;
	
	private HtmlDropDownList cmbCuentasBancoDisponibles;
	private List<SelectItem> lstCuentasBancoDisponibles;
	
	public void confirmarProcesarAjustePorExcepcion(ActionEvent ev) {
		boolean hecho = true;
		try {

			txtMotivoProcesarAjustePorExcepcion.setStyleClass("frmInput2") ;
			
			if(!txtMotivoProcesarAjustePorExcepcion.getValue().toString().matches(PropertiesSystem.REGEXP_DESCRIPTION)){
				txtMotivoProcesarAjustePorExcepcion.setStyleClass("frmInput2Error") ; 
				hecho = false;
				return;
			}
			
			final ConsolidadoCoincidente ccSeleccionado = (ConsolidadoCoincidente)CodeUtil.getFromSessionMap("pcd_CoincidenciaAjusteExcepcion");
			
			@SuppressWarnings("unchecked")
			List<ConsolidadoCoincidente> coincidencias = (List<ConsolidadoCoincidente>)getFromSessionMap( sesmapvarDepsCoincidencias );
			
			ConsolidadoCoincidente ccOriginal = (ConsolidadoCoincidente )
			CollectionUtils.find(coincidencias, new Predicate(){
			 
				public boolean evaluate(Object o) {
					ConsolidadoCoincidente cc = (ConsolidadoCoincidente)o;
					return	cc.getIdresumenbanco() == ccSeleccionado.getIdresumenbanco() ;
				}
				
			}) ;
			
			ccOriginal.setProcesarAjustePorExcepcion(true);
			ccOriginal.setCuentaAjusteExcepcionId(cmbCuentaAjustePorExcepcion.getValue().toString());
			ccOriginal.setMotivoexclusion(txtMotivoProcesarAjustePorExcepcion.getValue().toString());
			
			putInSessionMap(sesmapvarDepsCoincidencias, coincidencias);

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			if(hecho){
				CodeUtil.removeFromSessionMap("pcd_CoincidenciaAjusteExcepcion");
				dwProcesarEnAjustePorExcepcion.setWindowState("hidden");
				dwProcesarEnAjustePorExcepcion = null; 
			} 
		
			CodeUtil.refreshIgObjects(txtMotivoProcesarAjustePorExcepcion);
			
		}
	}

	public void cerrarProcesarAjustePorExcepcion(ActionEvent ev) {
		CodeUtil.removeFromSessionMap("pcd_CoincidenciaAjusteExcepcion");
		dwProcesarEnAjustePorExcepcion.setWindowState("hidden");
		dwProcesarEnAjustePorExcepcion = null; 
	}
  
	public void mostrarProcesarAjustePorExcepcion(ActionEvent ev){

		try {

			CodeUtil.removeFromSessionMap("pcd_CoincidenciaAjusteExcepcion");
			
			ConsolidadoCoincidente cc = (ConsolidadoCoincidente)DataRepeater.getDataRow( ((RowItem)ev.getComponent().getParent().getParent()));
			if(cc.getExcluircoincidencia() == 1 ){
				lblMensajeValidacionProcesos.setValue("La coincidencia ha sido sometida a exclusión");
				dwMensajesValidacion.setWindowState("normal");
				CodeUtil.refreshIgObjects(dwMensajesValidacion) ;
				return;
			}
			
			CodeUtil.putInSessionMap("pcd_CoincidenciaAjusteExcepcion", cc) ;
			
			@SuppressWarnings("unchecked")
			List<String[]> cuentasPorExcepcion =  (ArrayList<String[]>) CodeUtil.getFromSessionMap("pcd_dtaCuentaAjustePorExcepcion");
			lstCuentaAjustePorExcepcion = new ArrayList<SelectItem>(cuentasPorExcepcion.size());
			
			for (String[] dtaCuenta : cuentasPorExcepcion) {
				lstCuentaAjustePorExcepcion.add( new SelectItem(dtaCuenta[1], dtaCuenta[0] )  ) ; 
			}
			
			CodeUtil.putInSessionMap("pcd_lstCuentaAjustePorExcepcion", lstCuentaAjustePorExcepcion) ;
			
			dwProcesarEnAjustePorExcepcion.setWindowState("normal");
			txtMotivoProcesarAjustePorExcepcion.setValue("");
			cmbCuentaAjustePorExcepcion.dataBind();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
  
	public void mostrarSometerCoincidenciaExcepcion(ActionEvent ev){
		String msg = "" ;
		
		try {
			
			ConsolidadoCoincidente cc = (ConsolidadoCoincidente)DataRepeater.getDataRow( ((RowItem)ev.getComponent().getParent().getParent()));
			if(cc.getExcluircoincidencia() == 1 ){
				msg = " La coincidencia ha sido sometida a exclusión ";
				return;
			}
			
			chkExcluirBanco.setChecked(true);
			chkExcluirCaja.setChecked(true);
			txtMotivoSometeExcepcion.setValue("");
			CodeUtil.putInSessionMap("pcd_CoincidenciaSometidaAExcepcion", cc);
			
			cc = null;
			
			
		} catch (Exception e) {
			msg = "No se puede someter la coincidencia a excepcion";
			e.printStackTrace();
		}finally{
			
			if(msg.isEmpty()){ 
				dwSometerCoincidenciaAExcepcion.setWindowState("normal");
			}else{
				lblMensajeValidacionProcesos.setValue(msg);
				dwMensajesValidacion.setWindowState("normal");
			}
		}
		
	}
	public void someterCoincidenciaExcepcion(ActionEvent ev){
		String msg = "";
		List<String> queries = new ArrayList<String>();
		
		try {
			
			
			String motivo = txtMotivoSometeExcepcion.getValue().toString()  ;
			
			if(	motivo.trim().isEmpty() ){
				msg = "Debe ingresar el motivo" ;
				return;
			}
			
			Vautoriz vaut = ((Vautoriz[])CodeUtil.getFromSessionMap("sevAut"))[0] ;
			
			ConsolidadoCoincidente cc = (ConsolidadoCoincidente)CodeUtil.getFromSessionMap("pcd_CoincidenciaSometidaAExcepcion");
			long idresumenBanco = cc.getIdresumenbanco();
			String[] consecutivosCaja = cc.getIdsdepscaja().split(",") ;
			
			int iCodigoUsuarioMod = vaut.getId().getCodreg();
			
			if( cc.isMultipleBancoPorUnoCaja() ){
				
				if(chkExcluirCaja.isChecked()){
					queries.add(
						"update "+PropertiesSystem.ESQUEMA+".deposito set tipoconfr = 'DSE', estadocnfr = 'CFR', " +
						"usrconfr = "+iCodigoUsuarioMod+", fechamod = current date, horamod = current time " +
						"where consecutivo in " + cc.getIdresumenbanco() ) ;
				}

				if(chkExcluirBanco.isChecked()){
					queries.add(
						"update "+PropertiesSystem.ESQUEMA+".pcd_consolidado_depositos_banco set estadoconfirmacion = 3, " +
						"usuarioactualiza = "+iCodigoUsuarioMod+", " +
						"fechamodconsolida = current timestamp " +
						"where idresumenbanco in " + Arrays.asList(consecutivosCaja).toString().replace("[", "(").toString().replace("]", ")" )  
					);
				}
			}else{
				
				if(chkExcluirBanco.isChecked()){
					queries.add(
						"update "+PropertiesSystem.ESQUEMA+".pcd_consolidado_depositos_banco set estadoconfirmacion = 3, " +
						"usuarioactualiza = "+iCodigoUsuarioMod+", " +
						"fechamodconsolida = current timestamp " +
						"where idresumenbanco = " + idresumenBanco 
					);
				}
				if(chkExcluirCaja.isChecked()){
					queries.add(
						"update "+PropertiesSystem.ESQUEMA+".deposito set tipoconfr = 'DSE', estadocnfr = 'CFR', " +
						"usrconfr = "+iCodigoUsuarioMod+", fechamod = current date, horamod = current time " +
						"where consecutivo in " + Arrays.asList(consecutivosCaja).toString().replace("[", "(").toString().replace("]", ")" ) ) ;
				}
				
			}
			
			boolean aplicado = ConsolidadoDepositosBcoCtrl.executeSqlQueries( queries );
			
			if(!aplicado){
				msg = "No se han aplicado las actualizaciones de estado para la coinciencia " ;
				return;
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			if(msg.isEmpty()){ 
				dwSometerCoincidenciaAExcepcion.setWindowState("hidden");
				lblMensajeValidacionProcesos.setValue("Procesado Correctamente");
				dwMensajesValidacion.setWindowState("normal");
			} 
		}
		
		
		
	}
	public void cerrarSometerExcepcion(ActionEvent ev){
		dwSometerCoincidenciaAExcepcion.setWindowState("hidden");
		CodeUtil.removeFromSessionMap("pcd_CoincidenciaSometidaAExcepcion");
		dwSometerCoincidenciaAExcepcion = null;
		txtMotivoSometeExcepcion = null;
	}
	
	public  void sacarObjetosDeListas( final ConsolidadoCoincidente cc ){
	
		try {
			
			@SuppressWarnings("unchecked")
			List<PcdConsolidadoDepositosBanco> lstDepositosBanco =  new ArrayList<PcdConsolidadoDepositosBanco> 
			((List<PcdConsolidadoDepositosBanco>) getFromSessionMap(ConsolidadoDepsBcoDAO.lstConsolidadoDpsTodos));
			
/*			//&& ============= Remover del grid la coincidencia procesada.
			@SuppressWarnings("unchecked")
			List<ConsolidadoCoincidente> coincidencias = (ArrayList<ConsolidadoCoincidente>)getFromSessionMap(sesmapvarDepsCoincidencias);
			CollectionUtils.filter(coincidencias, new Predicate(){
					@Override
					public boolean evaluate(Object o) {
						return	!(cc.getIdresumenbanco() == ((ConsolidadoCoincidente)o).getIdresumenbanco()) ;
					}
				}) ;
			putInSessionMap(sesmapvarDepsCoincidencias, coincidencias);*/
			
			//&& ===================== Remover de la lista el resumen de consolidado.
			CollectionUtils.filter(lstDepositosBanco, new Predicate(){
				 
				public boolean evaluate(Object o) {
					return	!(cc.getIdresumenbanco() == ((PcdConsolidadoDepositosBanco)o).getIdresumenbanco()) ;
				}
			}) ;
			putInSessionMap(ConsolidadoDepsBcoDAO.lstConsolidadoDpsTodos, lstDepositosBanco );
			
			@SuppressWarnings("unchecked")
			List<PcdConsolidadoDepositosBanco>consolidadoGrid = (ArrayList<PcdConsolidadoDepositosBanco>)
								getFromSessionMap( ConsolidadoDepsBcoDAO.lstConsolidadoDpsGrid ) ;
			CollectionUtils.filter(consolidadoGrid, new Predicate(){
				 
				public boolean evaluate(Object o) {
					return	!(cc.getIdresumenbanco() == ((PcdConsolidadoDepositosBanco)o).getIdresumenbanco()) ;
				}
			}) ;
			
			putInSessionMap(ConsolidadoDepsBcoDAO.lstConsolidadoDpsGrid, consolidadoGrid);
			
			//&& ============= Remover de la lista de depositos de caja
			@SuppressWarnings("unchecked")
			List<Deposito_Report>depositoscaja = new ArrayList<Deposito_Report>( (ArrayList<Deposito_Report>)getFromSessionMap(ConsolidadoDepsBcoDAO.lstTodosDepositosCaja) );
			 
			for (final Deposito_Report dcRemove : cc.getDepositoscaja() ) {
				CollectionUtils.filter(depositoscaja, new Predicate(){
					 
					public boolean evaluate(Object o) {
						return	!(dcRemove.getConsecutivo() == ((Deposito_Report)o).getConsecutivo() ) ;
					}
				}) ;
			}
			CodeUtil.putInSessionMap(ConsolidadoDepsBcoDAO.lstTodosDepositosCaja, depositoscaja);
			
		} catch (Exception e) {
			 e.printStackTrace();
		}
	}
	
	public void generarComprobanteIndividualDeposito(ActionEvent ev){
		String msg = "" ;
		
		try {
			
			//&& ============ todos los registros de consolidados para hacer busqueda
			@SuppressWarnings("unchecked")
			List<PcdConsolidadoDepositosBanco> lstDepositosBanco =  new ArrayList<PcdConsolidadoDepositosBanco> 
			((List<PcdConsolidadoDepositosBanco>) CodeUtil.getFromSessionMap(ConsolidadoDepsBcoDAO.lstConsolidadoDpsTodos));
			
			Vautoriz vaut = ((Vautoriz[])CodeUtil.getFromSessionMap("sevAut"))[0] ;
			PcdConsolidadoDepositosBanco consolidadoOriginal;
			ProcesarConsolidadoDepositos pcd = new ProcesarConsolidadoDepositos();
			Date dtFechaComprobante ;
			boolean aplicado = true;
			
			final ConsolidadoCoincidente cc = (ConsolidadoCoincidente)CodeUtil.getFromSessionMap("coincidenciaIndividualAConfirmar") ;
			
			//&& ============= validacion del periodo fiscal
			
			int[] iMesAnioPeriodoActual = (int[])CodeUtil.getFromSessionMap("pcd_PeriodoFiscalActual");
			pcd.periodofiscal = iMesAnioPeriodoActual ;
			
			if(cc.isMultipleBancoPorUnoCaja()){
				
				dtFechaComprobante = cc.getFechabanco();
				
				aplicado = pcd.realizarConfirmacionDepositosBancoVsCaja(cc, vaut, "", 32, dtFechaComprobante ); 
				
			}else{
				
				consolidadoOriginal = (PcdConsolidadoDepositosBanco)	
						CollectionUtils.find(lstDepositosBanco, new Predicate(){
							 
							public boolean evaluate(Object o) {
								return cc.getIdresumenbanco() == ((PcdConsolidadoDepositosBanco)o).getIdresumenbanco() ;
							}
						});
				
				Collections.sort(cc.getDepositoscaja(),
						new Comparator<Deposito_Report>() {
							 
							public int compare(Deposito_Report d1, Deposito_Report d2) {
								return d2.getFecha().compareTo(d1.getFecha()) ; 
							}
						});
						
				dtFechaComprobante  = cc.getDepositoscaja().get(0).getFecha();   
				
				aplicado = pcd.realizarConfirmacionDepositos(cc, consolidadoOriginal, vaut, 
					"", 32, dtFechaComprobante, consolidadoOriginal.getIdarchivo() ); 
				
			}
			
			
			@SuppressWarnings("unchecked")
			List<ConsolidadoCoincidente> coincidencias =  (ArrayList<ConsolidadoCoincidente>)CodeUtil.getFromSessionMap("pcd_lstProcesarDepositosCoincidentes") ;
			
			//&& ============== actualizar  el elemento de la lista
			ConsolidadoCoincidente ccNew = (ConsolidadoCoincidente)
				CollectionUtils.find(coincidencias, new Predicate(){
					public boolean evaluate(Object o) {
						return cc.getIdresumenbanco() == ((ConsolidadoCoincidente)o).getIdresumenbanco()   ;
					}
				});
		
			ccNew.setComprobanteaplicado(aplicado);
			
			String estado_batch = "" ;
			
			if(aplicado){
				ccNew.setTipodocumentojde(cc.getTipodocumentojde() );
				ccNew.setReferenciacomprobante(cc.getReferenciacomprobante());
				ccNew.setNobatch(ProcesarConsolidadoDepositos.getNobatchToUse() ) ;
				ccNew.setObservaciones( "Deposito Aprobado en Batch: "+ ProcesarConsolidadoDepositos.getNobatchToUse()) ; 
				
//				P5509800Input input = new P5509800Input();
//				P5509800_Service service = new P5509800_Service();
//				P5509800PortType port = service.getP5509800HttpSoap11Endpoint();
				//port.p5509800(input);
				
				/*// && ========== mandar a contabilizar el batch
				if (!ccNew.isProcesarAjustePorExcepcion()) {
				
					P5509800Input input = new P5509800Input();
					P5509800_Service service = new P5509800_Service();
					P5509800PortType port = service.getP5509800HttpSoap11Endpoint();
					port.p5509800(input);
				}*/
				
			}else{
				ccNew.setObservaciones( ProcesarConsolidadoDepositos.getMsgErrorProceso() ); 
				estado_batch = "Coincidencia no procesada " ;
			}
			
			
			if( aplicado ) {
				
				String strSql = " select ifnull ( ( case " +
					"when icist = 'E' then 'Error' " +
					"when icist = 'D'  then 'Contabilizado' " +
					"when icist = 'U' then 'En Uso'  " +
					"when icist = 'A' then 'Aprobado' " +
					"when trim(icist) = '' then 'Pendiente' " +
					"else 'Otros' end ), 'Sin Estado' ), cast(icist as varchar(3) ccsid 37 ) estado  " +
					" from " +PropertiesSystem.JDEDTA+".F0011 " +
					"where icicu = " +  ProcesarConsolidadoDepositos.getNobatchToUse() ;
				
//				estado_batch = ( (List<String>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSql, true, null)).get(0).toString() ;
				
				@SuppressWarnings("unchecked")
				List<Object[]> dtaBatch = ( (List<Object[]>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSql, true, null))  ;
				
				estado_batch = String.valueOf( dtaBatch.get(0)[0] ) ;
				String codestadobatch =  String.valueOf( dtaBatch.get(0)[1] ) ;
				
				String updateEstadoBatch = 
						" update " + PropertiesSystem.ESQUEMA+".conciliacion " +
						"set estadobatch = '"+codestadobatch+"' " +
						"where nobatch = " +   ProcesarConsolidadoDepositos.getNobatchToUse() ;
			
				//Ajustado por lfonseca para manejo de sessiones
				//valores enviados por defecto null
				ConsolidadoDepositosBcoCtrl.executeSqlQueryTx( null, updateEstadoBatch);
				
				
			}
			
			ccNew.setEstadobatchdescrip(estado_batch);
			
			CodeUtil.putInSessionMap("pcd_lstProcesarDepositosCoincidentes", coincidencias);
			coincidencias = null;
			
			//&& ================= Enviar notificacion de correo.
			if(aplicado){
				List<ConsolidadoCoincidente> procesados = new ArrayList<ConsolidadoCoincidente>() ;
				procesados.add(ccNew) ;
				ConfirmaDepositosCtrl.enviarCorreoPorConsolidadosProcesados(procesados,	vaut.getId().getCodreg() );
				procesados = null;
			}
		
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = "No se ha generado el Comprobante contable por coincidencia de depósitos";
		}finally{
		
			if(msg.isEmpty()){
				msg = "Comprobante generado correctamente ";
			}
			
			dwValidaConfirmaDepositoIndividual.setWindowState("hidden");
			gvProcesarDepositosCoincidentes.dataBind();
			lblMensajeValidacionProcesos.setValue(msg);
			dwMensajesValidacion.setWindowState("normal");
			gvConsolidadoDepositosBanco.dataBind();
			
			dwValidaConfirmaDepositoIndividual = null;
			
			refreshIgObjects(new Object[]{gvConsolidadoDepositosBanco, gvProcesarDepositosCoincidentes}) ;
		}
	}
	
	
	public void mostrarConfirmarDepositoIndividual (ActionEvent ev ){
		String msg ="" ;
		
		try {
			
			ConsolidadoCoincidente cc = (ConsolidadoCoincidente)DataRepeater.getDataRow( ((RowItem)ev.getComponent().getParent().getParent()));
			
			if(cc.getExcluircoincidencia() == 1 ){
				msg = " La coincidencia ha sido sometida a exclusión ";
				return;
			}
			if(cc.isComprobanteaplicado() ){
				msg = " La coincidencia ya fue procesada bajo batch # "+ cc.getNobatch() ;
				return;
			}
			
			//&& ============== Buscar los depositos de caja por id(consecutivo) en la lista inicial de depositos.
			
			/*
			List<Deposito> depositosDeCaja = new ArrayList<Deposito>();
			
			@SuppressWarnings("unchecked")
			List<Deposito> lstDepositosCajaTmps = (ArrayList<Deposito>)CodeUtil.getFromSessionMap(ConsolidadoDepsBcoDAO.lstTodosDepositosCaja) ;
			String[] idsDpCaja = cc.getIdsdepscaja().split(",");
					
			for (final String consecutivo : idsDpCaja) {
				
				Deposito d = (Deposito)
					CollectionUtils.find(lstDepositosCajaTmps, new Predicate(){
						public boolean evaluate(Object o) {
							return ((Deposito)o).getConsecutivo() ==  Integer.parseInt( consecutivo.trim() ) ;
						}
					});
				if(d == null)
					break;
				
				depositosDeCaja.add(d);
			}
			
			if( depositosDeCaja.isEmpty() ){
				msg = "Depósitos de caja no vinculados ";
				return ;
			}
			
			cc.setDepositoscaja(depositosDeCaja);
			*/
			
			lblmsgConfirmaComprobanteIndividual.setValue("¿Generar comprobantes para el depósito referencia " + cc.getReferenciabanco() +"?" );
			dwValidaConfirmaDepositoIndividual.setWindowState("normal");
			
			CodeUtil.putInSessionMap("coincidenciaIndividualAConfirmar", cc);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if( !msg.isEmpty() ){
				lblMensajeValidacionProcesos.setValue(msg);
				dwMensajesValidacion.setWindowState("normal");
			}
		}
	}
	public void cerrarGenerarComprobanteIndividualDepositos(ActionEvent ev){
		dwValidaConfirmaDepositoIndividual.setWindowState("hidden");
	}
	
	

	@SuppressWarnings("unchecked")
	public void procesarCoincidenciasDepositos(ActionEvent ev){
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.sss");
		
		try {
			
			//&& ============ separar las coincidencias que fueron excluidas.
			List<ConsolidadoCoincidente> todasCoincidencias =  (ArrayList<ConsolidadoCoincidente>)CodeUtil.getFromSessionMap("pcd_lstProcesarDepositosCoincidentes") ;
			
			List<ConsolidadoCoincidente> coincidenciasProcesadas = new ArrayList<ConsolidadoCoincidente>(todasCoincidencias.size());
			
			List<ConsolidadoCoincidente> coincidenciasNoExcluidas = new ArrayList<ConsolidadoCoincidente>( 
				CollectionUtils.select(todasCoincidencias, new Predicate(){
					 
					public boolean evaluate(Object o) {
						return ((ConsolidadoCoincidente)o).getExcluircoincidencia() == 0 ;
					}
				}) );
			
			//&& ============ separar las coincidencias que ya fueron procesadas.
			coincidenciasNoExcluidas = new ArrayList<ConsolidadoCoincidente>( 
				CollectionUtils.select(coincidenciasNoExcluidas, new Predicate(){
					public boolean evaluate(Object o) {
						return !((ConsolidadoCoincidente)o).isComprobanteaplicado() ;
					}
				}) );
			 
			if(coincidenciasNoExcluidas.size() == 0 ){
				lblMensajeValidacionProcesos.setValue(" No hay coincidencia para procesar, todas han sido excluidas");
				dwMensajesValidacion.setWindowState("normal");
				return ;
			}
			
			
			//&& ============ todos los registros de consolidados para hacer busqueda
			List<PcdConsolidadoDepositosBanco> lstDepositosBanco =  new ArrayList<PcdConsolidadoDepositosBanco> 
			((List<PcdConsolidadoDepositosBanco>) CodeUtil.getFromSessionMap(ConsolidadoDepsBcoDAO.lstConsolidadoDpsTodos));
			
			Vautoriz vaut = ((Vautoriz[])getFromSessionMap("sevAut"))[0] ;
			PcdConsolidadoDepositosBanco consolidadoOriginal;
			ProcesarConsolidadoDepositos pcd = new ProcesarConsolidadoDepositos();
			
			int[] iMesAnioPeriodoActual = (int[])CodeUtil.getFromSessionMap("pcd_PeriodoFiscalActual");
			pcd.periodofiscal = iMesAnioPeriodoActual ;
			
			Date dtFechaComprobante ;
			boolean aplicado = true;
			
			//&& ============ recorrer cada elemento de coincidencia y crear sus asientos de diario.
			
			int cantcoincide = 0;
			long tiempoini = 0;
			
			int totalTransacciones = coincidenciasNoExcluidas.size();
			int porcentajeProcesado = 0;
			int numeroIteracion = 0;
			
//			P5509800Input input = new P5509800Input();
//			P5509800_Service service = new P5509800_Service();
//			P5509800PortType port = service.getP5509800HttpSoap11Endpoint();			
			
			String estado_batch = "";
			String strSqlEstadoBatch = " select ifnull ( ( case " +
					"when icist = 'E' then 'Error' " +
					"when icist = 'D'  then 'Contabilizado' " +
					"when icist = 'U' then 'En Uso'  " +
					"when icist = 'A' then 'Aprobado' " +
					"else 'Otros' end ), 'Sin Estado' ) , cast(icist as varchar(3) ccsid 37 ) estado " +
					" from " +PropertiesSystem.JDEDTA+".F0011 " +
				"where icicu = @NUMERO_BATCH ";
			
			
			for (final ConsolidadoCoincidente coincidencia : coincidenciasNoExcluidas) {
				
				
				//&& =================== Manejo del progressbar 
				tiempoini = System.currentTimeMillis();
				numeroIteracion ++ ;
				porcentajeProcesado =  ( ( numeroIteracion * 100)  /  totalTransacciones) + 1  ;
				
				if(porcentajeProcesado >= 98)
					porcentajeProcesado = 98;
				
				CodeUtil.putInSessionMap("porcentajeavanceproceso", porcentajeProcesado );				
				
				
				if(coincidencia.isMultipleBancoPorUnoCaja()){
					
					dtFechaComprobante = coincidencia.getFechabanco();
					aplicado = pcd.realizarConfirmacionDepositosBancoVsCaja(coincidencia, vaut, "", 32, dtFechaComprobante );
					
				}else{
					consolidadoOriginal = (PcdConsolidadoDepositosBanco)	
						CollectionUtils.find(lstDepositosBanco, new Predicate(){
							 
							public boolean evaluate(Object o) {
								return coincidencia.getIdresumenbanco() == ((PcdConsolidadoDepositosBanco)o).getIdresumenbanco() ;
							}
						});
					
					Collections.sort(coincidencia.getDepositoscaja(),
							new Comparator<Deposito_Report>() {
								public int compare(Deposito_Report d1, Deposito_Report d2) {
									return d2.getFecha().compareTo(d1.getFecha()) ; 
								}
							});
					
					dtFechaComprobante = coincidencia.getDepositoscaja().get(0).getFecha() ;
					
					aplicado = pcd.realizarConfirmacionDepositos(coincidencia, consolidadoOriginal, vaut, 
									"", 32, dtFechaComprobante, consolidadoOriginal.getIdarchivo() ); 
					
				}
				
				coincidencia.setComprobanteaplicado(aplicado);
				
				if(aplicado){
					
					coincidencia.setNobatch(ProcesarConsolidadoDepositos.getNobatchToUse() ) ;
					coincidencia.setObservaciones( "Deposito Aprobado en Batch: "+ ProcesarConsolidadoDepositos.getNobatchToUse()) ; 
					
					//port.p5509800(input);
					
					/*
					// && ========== mandar a contabilizar el batch
					if (!coincidencia.isProcesarAjustePorExcepcion()) {
						port.p5509800(input);
					}
					*/
					
				}else{
					coincidencia.setObservaciones( ProcesarConsolidadoDepositos.getMsgErrorProceso() );  
					estado_batch = "Coincidencia no procesada " ;
				}
				
				if( aplicado ) {
					
					String strSql = strSqlEstadoBatch.replace("@NUMERO_BATCH", Integer.toString( ProcesarConsolidadoDepositos.getNobatchToUse() ) ) ;
					
					List<Object[]> dtaBatch = ( (List<Object[]>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSql, true, null))  ;
					
					estado_batch = String.valueOf( dtaBatch.get(0)[0] ) ;
					String codestadobatch =  String.valueOf( dtaBatch.get(0)[1] ) ;
					
					String updateEstadoBatch = 
							" update " + PropertiesSystem.ESQUEMA+".conciliacion " +
							"set estadobatch = '"+codestadobatch+"' " +
							"where nobatch = " +   ProcesarConsolidadoDepositos.getNobatchToUse() ;
				
					//Ajustado por Lfonseca
					//2020-11-28
					//Manejo de sessiones para este caso null
					ConsolidadoDepositosBcoCtrl.executeSqlQueryTx(null, updateEstadoBatch);
					
				}
				
				coincidencia.setEstadobatchdescrip(estado_batch);
				
				//&& ================== Manejo de documentos confirmados y no confirmados.
				coincidenciasProcesadas.add( coincidencia.clone() );
				
				
			}
			
			if( !coincidenciasProcesadas.isEmpty() ){
			
				ConfirmaDepositosCtrl.enviarCorreoPorConsolidadosProcesados( coincidenciasProcesadas, vaut.getId().getCodreg() );
				CodeUtil.putInSessionMap("pcd_lstProcesarDepositosCoincidentes", todasCoincidencias);
				
				todasCoincidencias = null;
				
			}
			CodeUtil.putInSessionMap("porcentajeavanceproceso", 100 );		
			
			gvProcesarDepositosCoincidentes.dataBind();
			gvConsolidadoDepositosBanco.dataBind();
			
			lblMensajeValidacionProcesos.setValue(" ...finaliza el proceso... ");
			dwMensajesValidacion.setWindowState("normal");
			
		} catch (Exception e) {
			e.printStackTrace();
			CodeUtil.putInSessionMap("porcentajeavanceproceso", 100 );
		}finally{
			
			CodeUtil.putInSessionMap("porcentajeavanceproceso", 100 );
			
			refreshIgObjects(new Object[]{ 
					gvProcesarDepositosCoincidentes, 
					gvConsolidadoDepositosBanco, 
					dwConfirmacionProcesarcoincidencia,
					dwMensajesValidacion }) ;
			
			dwConfirmacionProcesarcoincidencia.setWindowState("hidden");
			dwConfirmacionProcesarcoincidencia = null;
			
			
		}
	}
	
	
	public void confirmacionProcesarCoincidencias(ActionEvent ev){
		
		CodeUtil.removeFromSessionMap("porcentajeavanceproceso") ;
		
		
		lblMsgConfirmacionProcesaCoincidencia.setValue("¿ Procesar los datos de las coincidencias de depósitos ? "); 
		dwConfirmacionProcesarcoincidencia.setWindowState("normal");
	}
	public void cerrarConfirmacionProcesarCoincidencias(ActionEvent ev){
		dwConfirmacionProcesarcoincidencia.setWindowState("hidden");
	}
	
	@SuppressWarnings("unchecked")
	public void marcarConflictoNoComoValido(ActionEvent ev){
		String msg = "";
		try {
			
			final ConsolidadoCoincidente conflictoNoValido = (ConsolidadoCoincidente)getFromSessionMap("conflictoSeleccionadoNoValido");
			
			List<ConsolidadoCoincidente> conflictos = (ArrayList<ConsolidadoCoincidente>)getFromSessionMap(sesmapvarDepsCoincidenciasConflicto);
			 
			//&& ============== remover el elemento de la lista
			CollectionUtils.filter(conflictos, new Predicate(){
				 
				public boolean evaluate(Object o) {
					ConsolidadoCoincidente cc = (ConsolidadoCoincidente)o;
					return !( 
							conflictoNoValido.getIdresumenbanco() == cc.getIdresumenbanco() && 
							conflictoNoValido.getDtaIdDepsBancoConflicto().compareTo( 
									cc.getDtaIdDepsBancoConflicto() ) == 0 ) ;
				}
			});
			
			putInSessionMap(sesmapvarDepsCoincidenciasConflicto, conflictos);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			if(msg.compareTo("") == 0){
				gvConflictosCoincidenciaDepositos.dataBind();
				dwConfirmarConflictoNoValido.setWindowState("hidden");
				refreshIgObjects(new Object[]{gvConflictosCoincidenciaDepositos}) ;
			} 
			 else{
				lblMensajeValidacionProcesos.setValue(msg);
				dwMensajesValidacion.setWindowState("normal");
			}
		}
	}
	public void cerrarConfirmacionConflictoNoValido(ActionEvent ev){
		dwConfirmarConflictoNoValido.setWindowState("hidden");
	}

	public void  mostrarConfirmacionConflictoNoValido(ActionEvent ev){
		String msg = ""; 
		try {
			
			ConsolidadoCoincidente cc = (ConsolidadoCoincidente)DataRepeater.getDataRow(((RowItem)ev.getComponent().getParent().getParent()));
			putInSessionMap("conflictoSeleccionadoNoValido", cc) ;

			
			if(cc.getStatusValidaConflicto() != 0 ){
				msg = " La coincidencia ya ha sido procesada " ;
				return;
			}
			
			lblmsgConfirmaConflictoNoValido.setValue("¿ Invalidar la coincidencia de Referencia " +cc.getReferenciabanco() +" por "+cc.getMontoBanco() +"?");
			dwConfirmarConflictoNoValido.setWindowState("normal");
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if( !msg.isEmpty() ){ 
				lblMensajeValidacionProcesos.setValue(msg);
				dwMensajesValidacion.setWindowState("normal");
			}
		}
	}
	@SuppressWarnings("unchecked")
	public void marcarConflictoComoValido(ActionEvent ev){
		String msg = "" ;
		try {
			
			final ConsolidadoCoincidente conflictoValido = (ConsolidadoCoincidente)getFromSessionMap("conflictoSeleccionado");
			
			if(conflictoValido.getStatusValidaConflicto() != 0 ){
				msg = " La coincidencia ya ha sido validada " ;
				return;
			}
			
			List<ConsolidadoCoincidente> coincidencias = (ArrayList<ConsolidadoCoincidente>)getFromSessionMap(sesmapvarDepsCoincidencias);
			List<ConsolidadoCoincidente> conflictos = (ArrayList<ConsolidadoCoincidente>)getFromSessionMap(sesmapvarDepsCoincidenciasConflicto);
			
			//&& =============== quitar marca de coincidencia en conflicto y agregarlo a la lista de coincidencias.
			conflictoValido.setStatusValidaConflicto(1);
			conflictoValido.setEnconflicto(false);
			coincidencias.add(conflictoValido);
			
			//&& =============== Marcar todos los conflictos que usen el mismo deposito de caja como no validos.
			final int iddepcajaValido = Integer.parseInt(conflictoValido.getDtaDepositoCajaConflicto() ); 
			
			List<ConsolidadoCoincidente> conflictosExlcuir = (ArrayList<ConsolidadoCoincidente>)
			CollectionUtils.select( conflictos, new Predicate(){
				 
				public boolean evaluate(Object o) {
					return iddepcajaValido ==   Integer.parseInt(((ConsolidadoCoincidente)o).getDtaDepositoCajaConflicto() ); 
				}
			});	
			for (ConsolidadoCoincidente cc : conflictosExlcuir) {
				cc.setEnconflicto(false);
				cc.setStatusValidaConflicto(2);
			}
 
			 //&& ============== remover ambos elementos de la lista de conflictos.
			ConsolidadoCoincidente ccValido = (ConsolidadoCoincidente)
					CollectionUtils.find(conflictos, new Predicate(){
						 
						public boolean evaluate(Object o) {
							ConsolidadoCoincidente cc = (ConsolidadoCoincidente)o;
							return 
						conflictoValido.getIdresumenbanco() == cc.getIdresumenbanco() && 
						conflictoValido.getDtaIdDepsBancoConflicto().compareTo( cc.getDtaIdDepsBancoConflicto() ) == 0 ;
						}
				});
			ccValido.setEnconflicto(false);
			ccValido.setStatusValidaConflicto(1);
			
			putInSessionMap(sesmapvarDepsCoincidencias, coincidencias);
			putInSessionMap(sesmapvarDepsCoincidenciasConflicto, conflictos);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			if(msg.compareTo("") == 0){
				gvConflictosCoincidenciaDepositos.dataBind();
				dwConfirmarConflictoValido.setWindowState("hidden");
				refreshIgObjects(new Object[]{gvConflictosCoincidenciaDepositos}) ;
			} else{
				lblMensajeValidacionProcesos.setValue(msg);
				dwMensajesValidacion.setWindowState("normal");
			}
		}
	}
	public void cerrarConfirmacionConflictoValido(ActionEvent ev){
		dwConfirmarConflictoValido.setWindowState("hidden");
	}
	public void mostrarConfirmacionConflictoValido(ActionEvent ev){
		String msg = "";
		try {
			
			ConsolidadoCoincidente cc = (ConsolidadoCoincidente)DataRepeater.getDataRow(((RowItem)ev.getComponent().getParent().getParent()));
			
			if(cc.getStatusValidaConflicto() != 0 ){
				msg =  " La coincidencia ya ha sido procesada ";
				return;
			}
			
			putInSessionMap("conflictoSeleccionado", cc) ;

			lblmsgConfirmaConflictoValido.setValue("¿ Validar la coincidencia de Referencia " 
						+ cc.getReferenciabanco() +" por "+cc.getMontoBanco() +"?");
			
			dwConfirmarConflictoValido.setWindowState("normal");
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if( msg.compareTo("") != 0){ 
				lblMensajeValidacionProcesos.setValue(msg);
				dwMensajesValidacion.setWindowState("normal");
			}
		}
	}
	public void cerrarMensajeValidacion(ActionEvent ev){
		dwMensajesValidacion.setWindowState("hidden");
	}
	
	public void mostrarValidacionDeConflictos(ActionEvent ev){
		try {
			
			msgValidaResultadoComparacion.setValue("");
			refreshIgObjects(new Object[]{msgValidaResultadoComparacion}) ;
			
			dwValidacionConflictos.setWindowState("normal");
			gvConflictosCoincidenciaDepositos.dataBind();
			gvConflictosCoincidenciaDepositos.setPageIndex(0);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void cerrarValidarConflicto(ActionEvent ev){
		dwValidacionConflictos.setWindowState("hidden");
	}
	
	
	public void cerrardwProcesarDepositosCoincidentes(ActionEvent ev){
		try {
		
			dwProcesarDepositosCoincidentes.setWindowState("hidden");
			CodeUtil.removeFromSessionMap("pcd_lstProcesarDepositosCoincidentes");
			
			lstProcesarDepositosCoincidentes = null;
			gvProcesarDepositosCoincidentes = null;
			
			mostrarResultadosComparacion(ev);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void mostrarCoincidenciasPorNivel(ActionEvent ev){
		boolean valido = true;
		String msg = "";
		
		try {
			
			CodeUtil.removeFromSessionMap("pcd_lstProcesarDepositosCoincidentes");
			lstProcesarDepositosCoincidentes = null;
			
			final NivelComparacionDeposito datosNivel = (NivelComparacionDeposito)DataRepeater.getDataRow(((RowItem)ev.getComponent().getParent().getParent()));
			List<ConsolidadoCoincidente> lstDepsSiCoinciden = (ArrayList<ConsolidadoCoincidente>) CodeUtil.getFromSessionMap(sesmapvarDepsCoincidencias);
			
			
			List<ConsolidadoCoincidente> coincidenciasPorNivel =  (ArrayList<ConsolidadoCoincidente>)
			CollectionUtils.select(lstDepsSiCoinciden, new Predicate(){
				public boolean evaluate(Object o) {
					return datosNivel.getNivelComparacion() == ((ConsolidadoCoincidente)o).getNivelcomparacion();
				}
			}) ;
					
			
			if (coincidenciasPorNivel == null || coincidenciasPorNivel.isEmpty() ) {
				valido = false;
				msg = " No hay datos por nivel para mostrar ";
				return ;
			}
			
			
			//&& ============= asignar el objeto Deposito para depositos de caja asociados a la coincidencia.
			String[] idsDpCaja ;
			List<Deposito_Report> depositosDeCaja;
			List<Deposito_Report> lstDepositosCajaTmps = (ArrayList<Deposito_Report>)CodeUtil.getFromSessionMap(ConsolidadoDepsBcoDAO.lstTodosDepositosCaja) ;
		
			List<PcdConsolidadoDepositosBanco>depositosBanco ;
			List<PcdConsolidadoDepositosBanco> letDepositosBancoTmp = 
					(ArrayList<PcdConsolidadoDepositosBanco>)CodeUtil.getFromSessionMap(ConsolidadoDepsBcoDAO.lstConsolidadoDpsTodos) ;
			
			
			hdrColmCoincidenciaMtoCaja  = (coincidenciasPorNivel.get(0).isMultipleBancoPorUnoCaja() ) ? "Banco" : "Caja" ;
			hdrColmCoincidenciaMtoBanco = (coincidenciasPorNivel.get(0).isMultipleBancoPorUnoCaja() ) ? "Caja"  : "Banco";
			
			
			for (final ConsolidadoCoincidente cc : coincidenciasPorNivel) {
				
				idsDpCaja = cc.getIdsdepscaja().split(",");
				
				//&& ============= asociar los objetos por depositos de banco (pcd_consolidado_depositos_banco) 
				if(cc.isMultipleBancoPorUnoCaja()){
					
//					cc.setMontorporajuste( cc.getMontorporajuste().multiply(new BigDecimal( "-1" ) ) );
					
					depositosBanco = new ArrayList<PcdConsolidadoDepositosBanco>(idsDpCaja.length);
					
					for (final String consecutivo : idsDpCaja) {

						PcdConsolidadoDepositosBanco pcd = (PcdConsolidadoDepositosBanco) CollectionUtils.find(
								letDepositosBancoTmp, new Predicate() {
									public boolean evaluate(Object o) {
										return ((PcdConsolidadoDepositosBanco) o).getIdresumenbanco() == Integer.parseInt(consecutivo.trim());
									}
								});
						
						if(pcd == null)
							continue;
						
						depositosBanco.add(pcd);
					}
					
					if(!depositosBanco.isEmpty()){
						cc.setDepositosbanco( depositosBanco ) ;
					}
					
					//&& ======= Cargar los datos del deposito de caja 
					Deposito_Report d = (Deposito_Report) CollectionUtils.find(lstDepositosCajaTmps, new Predicate(){
						public boolean evaluate(Object o) {
							return ((Deposito_Report)o).getConsecutivo() == cc.getIdresumenbanco()  ;
						}
					});
					
					if(d == null)
						break;
					
					depositosDeCaja = new ArrayList<Deposito_Report>();
					depositosDeCaja.add(d);
					cc.setDepositoscaja(depositosDeCaja);
					
				}else{
					
					depositosDeCaja = new ArrayList<Deposito_Report>(idsDpCaja.length);
					
					for (final String consecutivo : idsDpCaja) {
	
						Deposito_Report d = (Deposito_Report) CollectionUtils.find(lstDepositosCajaTmps, new Predicate(){
									public boolean evaluate(Object o) {
										return ((Deposito_Report)o).getConsecutivo() ==  Integer.parseInt( consecutivo.trim() ) ;
								}
							});
						if(d == null)
							break;
						
						d.setConsecutivobanco(cc.getIdresumenbanco());
						d.setReferenciabanco(cc.getReferenciabanco());
						d.setFormadepago(cc.getDescriptransjde());
						
						depositosDeCaja.add(d);
						
					}
					
					if( depositosDeCaja.isEmpty() ){
						continue;
					}
					
					cc.setDepositoscaja(depositosDeCaja);
				}
			}
			
			idsDpCaja = null;
			depositosDeCaja  = null;
			lstDepositosCajaTmps = null;
			depositosBanco = null;
			letDepositosBancoTmp = null;
			
			CodeUtil.putInSessionMap("pcd_lstProcesarDepositosCoincidentes", coincidenciasPorNivel) ;
			gvProcesarDepositosCoincidentes.dataBind() ;
			gvProcesarDepositosCoincidentes.setPageIndex(0);
			dwHeaderProcesarDepositosCoincidentes.setCaptionText(" Nivel: " + datosNivel.getNivelComparacion() + " " 
						+ datosNivel.getParametrosComparacion() +" :  " + coincidenciasPorNivel.size() +" resultado(s)"  );
			
			refreshIgObjects(new Object[]{dwResumenCoincidenciasPorNiveles, gvProcesarDepositosCoincidentes, 
					dwProcesarDepositosCoincidentes, dwMensajesValidacion, lblMensajeValidacionProcesos}) ;		
			
			
			lstDepsSiCoinciden = null;
			coincidenciasPorNivel = null;
			
			
		} catch (Exception e) {
			e.printStackTrace();
			valido = false;
			msg = " Error al cargar depósitos coincidentes por nivel ";
			
		}finally{
			
			dwResumenCoincidenciasPorNiveles.setWindowState("hidden");
			
			if(valido){
				dwProcesarDepositosCoincidentes.setWindowState("normal");
			}else{
				dwMensajesValidacion.setWindowState("normal");
				lblMensajeValidacionProcesos.setValue(msg) ;
			}
			
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public void mostrarResultadosComparacion(ActionEvent ev){
		String msg = "";
		
		try {
			
			CodeUtil.removeFromSessionMap("pcd_lstProcesarDepositosCoincidentes");
			List<ConsolidadoCoincidente> lstDepsSiCoinciden = (ArrayList<ConsolidadoCoincidente>) CodeUtil.getFromSessionMap(sesmapvarDepsCoincidencias);
			
			if(lstDepsSiCoinciden == null || lstDepsSiCoinciden.isEmpty()) {
				msg = "No se encontraron coincidencias para mostrar" ;
				return;
			}

			List<NivelComparacionDeposito> lstNivelesComparacion = new ArrayList<NivelComparacionDeposito>();
/*			String[] definicionnivel = new String[6];
			definicionnivel[0] = "Monto Depósito, Número Referencia, Fecha Depósito ";
			definicionnivel[1] = "Monto Depósito, Número Referencia";
			definicionnivel[2] = "Fecha Depósito, Número Referencia";
			definicionnivel[3] = "Monto Depósito, Número Referencia (últimos 4)";
			definicionnivel[4] = "Número Referencia (últimos 4) (Agrupación Caja)";
			definicionnivel[5] = "Número Referencia (últimos 6) (Agrupación Banco)";*/
			 
			String[] definicionnivel = new String[6];
			definicionnivel[0] = "Monto Depósito, Número Referencia, Fecha Depósito ";
			definicionnivel[1] = "Monto Depósito, Número Referencia";
			definicionnivel[2] = "Número Referencia, Fecha Depósito";
			definicionnivel[3] = "Monto Depósito, Número Referencia (últimos 4)";
			definicionnivel[4] = "Número Referencia (últimos 4) (Agrupación Caja)";
			definicionnivel[5] = "Número Referencia (últimos 6) (Agrupación Banco)";
			
			//&& ============= buscar por niveles dentro de los resultados
			BigDecimal cantDepsCaja ;
			int cantidadAgrupados = 0; 
			int cantidadProcesados = 0;
			int cantidadPendientes = 0;
			
			int cantCoincidenciasCaja = 0 ;
			int cantCoincidenciasBanco = 0 ;
			
			
			for (int i = 0; i < definicionnivel.length; i++) {
						
				final int numeronivel = i+1;
				
				List<ConsolidadoCoincidente> coincidenciaPorNivel = (ArrayList<ConsolidadoCoincidente>)CollectionUtils.select(lstDepsSiCoinciden, new Predicate(){
					public boolean evaluate(Object o) {
						return ((ConsolidadoCoincidente)o).getNivelcomparacion() == numeronivel;
					} 
				});
				
				if( coincidenciaPorNivel == null  ){
					coincidenciaPorNivel = new ArrayList<ConsolidadoCoincidente>();
				}
				
				//&& =============== consolidacion de depositos de caja 
				List<ConsolidadoCoincidente> lstccAgrupados = (ArrayList<ConsolidadoCoincidente>)
				CollectionUtils.select(coincidenciaPorNivel, new Predicate(){
					public boolean evaluate(Object o) {
						return ((ConsolidadoCoincidente)o).getCantdepositoscaja() > 1 ;
					}
				});
				
				if( lstccAgrupados != null  ){
					cantidadAgrupados = lstccAgrupados.size() ;
				}
				
				
				//&& ============= condiciones especiales para comparacion con caja como referencia (uno de caja - multiples banco ) 
				if(numeronivel == definicionnivel.length ){
					
					CollectionUtils.forAllDo(coincidenciaPorNivel, new Closure(){
						public void execute(Object o) {
						 ( (ConsolidadoCoincidente)o).setMultipleBancoPorUnoCaja(true) ;
//						 ( (ConsolidadoCoincidente)o).setMontorporajuste( ((ConsolidadoCoincidente)o).getMontorporajuste().multiply(new BigDecimal( "-1" ) ) ) ;
						}
					}) ;
					
					cantCoincidenciasCaja  += coincidenciaPorNivel.size() ;
					cantCoincidenciasBanco += CodeUtil.sumPropertyValueFromEntityList(coincidenciaPorNivel, "cantdepositoscaja", true).intValue() ;
					
					cantDepsCaja = new BigDecimal(Integer.toString( coincidenciaPorNivel.size() ) ) ;
					
				}else{
					
					cantCoincidenciasBanco += coincidenciaPorNivel.size() ;
					cantCoincidenciasCaja  += CodeUtil.sumPropertyValueFromEntityList(coincidenciaPorNivel, "cantdepositoscaja", true).intValue() ;
					
					cantDepsCaja = CodeUtil.sumPropertyValueFromEntityList(coincidenciaPorNivel, "cantdepositoscaja", true);
				}
				
				//&& =============== procesados y pendientes.
				lstccAgrupados = (ArrayList<ConsolidadoCoincidente>)
					CollectionUtils.select(coincidenciaPorNivel, new Predicate(){
						public boolean evaluate(Object o) {
							return ((ConsolidadoCoincidente)o).isComprobanteaplicado();
						}
					});
				
				cantidadProcesados = lstccAgrupados.size();
				cantidadPendientes = coincidenciaPorNivel.size() - cantidadProcesados;
				
				lstNivelesComparacion.add( 
					new NivelComparacionDeposito(numeronivel, "Nivel "+ ( i + 1 ), definicionnivel[i], 
							coincidenciaPorNivel.size(), ( i + 1 ), definicionnivel[i], 
							coincidenciaPorNivel.size(), cantidadAgrupados, 
							cantDepsCaja.intValue(), coincidenciaPorNivel.size(), 
							cantidadProcesados , cantidadPendientes)
				);
				
			}
			
			CodeUtil.putInSessionMap("pcd_lstResumenCoincidenciasPorNiveles", lstNivelesComparacion); 
			gvResumenCoincidenciasPorNiveles.dataBind();
			 
			/*int totaldepscaja = ((ArrayList<Deposito>)CodeUtil.getFromSessionMap(ConsolidadoDepsBcoDAO.lstTodosDepositosCaja) ).size();
			int totaldepcajaCoincidente = CodeUtil.sumPropertyValueFromEntityList(lstDepsSiCoinciden, "cantdepositoscaja", false).intValue();
			int totaldepcajapendiente   = totaldepscaja - totaldepcajaCoincidente;
			
			int totaldepsbanco = ( (ArrayList<PcdConsolidadoDepositosBanco>)CodeUtil.getFromSessionMap(ConsolidadoDepsBcoDAO.lstConsolidadoDpsTodos) ).size() ;
			int totaldepbancoCoincidente = ( (ArrayList<Integer>)CodeUtil.selectPropertyListFromEntity( lstDepsSiCoinciden, "idresumenbanco", false) ).size() ;
			int totaldepbancopendiente   = totaldepsbanco - totaldepbancoCoincidente;*/
			
			int totaldepscaja = ((ArrayList<Deposito_Report>)CodeUtil.getFromSessionMap(ConsolidadoDepsBcoDAO.lstTodosDepositosCaja) ).size();
			int totaldepcajaCoincidente = cantCoincidenciasCaja;
			int totaldepcajapendiente = totaldepscaja - cantCoincidenciasCaja ;
			
			int totaldepsbanco = ( (ArrayList<PcdConsolidadoDepositosBanco>)CodeUtil.getFromSessionMap(ConsolidadoDepsBcoDAO.lstConsolidadoDpsTodos) ).size() ;
			int totaldepbancoCoincidente = cantCoincidenciasBanco ;
			int totaldepbancopendiente  = totaldepsbanco - cantCoincidenciasBanco;
			
			String msgEstado = 
				"c > Total: <b>" + String.format("%05d",totaldepscaja )+"</b> " +
				"Enlazado: <b>" + String.format("%05d", totaldepcajaCoincidente) +"</b> " +
				"Restante: <b>" + String.format("%05d", totaldepcajapendiente)   +"</b><br>" +
				"b > Total: <b>" + String.format("%05d",totaldepsbanco )+"</b> " +
				"Enlazado: <b>" + String.format("%05d", totaldepbancoCoincidente) +"</b> " +
				"Restante: <b>" + String.format("%05d", totaldepbancopendiente)   +"</b>";
 
			lblResumenComparacionNivel.setValue(msgEstado);
			
			//&& =============== Actualizacion de los estados de los depositos utilizados en comparacion
			List<String> strSqlQueriesUpdate = (ArrayList<String>)CodeUtil.getFromSessionMap("pcd_SqlQueriesUpdateStatusDps") ;
			
			if(strSqlQueriesUpdate != null && !strSqlQueriesUpdate.isEmpty()){
				ConsolidadoDepositosBcoCtrl.executeSqlQueries( strSqlQueriesUpdate );
				CodeUtil.removeFromSessionMap("pcd_SqlQueriesUpdateStatusDps") ;
			}
			
			ClsParametroCaja clsParametroCaja = new ClsParametroCaja();
			CajaParametro cp = clsParametroCaja.getParametros("05", "0", "DIASVC");
			
			//&& =============== buscar la cantidad de dias habiles posterior al fin de mes
			int diasPermitidosMes = 3;
			
			if(cp==null)
			{
				throw new Exception("No se puede hacer consolidado de deposito, "
						+ "parametros dias permitidos para confirmar deposito no "
						+ "esta configurado");
			}
			
			if(cp.getValorAlfanumerico()!=null)
			{
				if(cp.getValorAlfanumerico().trim().equals("1"))
					diasPermitidosMes = ProcesarConsolidadoDepositos.cantidadDiasHabilesMes(new Date(), cp.getValorNumerico().intValue());
				else
					diasPermitidosMes = cp.getValorNumerico().intValue();
			}
			else
				diasPermitidosMes = cp.getValorNumerico().intValue();
				
			CodeUtil.putInSessionMap("pcd_diasPermitidosMes", diasPermitidosMes);
			
			
			
			
		} catch (Exception e) {
			msg = "El procedimiento de carga resumen de comparación no pude ser completado";
			e.printStackTrace();
		}finally{
			
			msgValidaResultadoComparacion.setValue(msg);
			
			if (msg.isEmpty()) {
				dwResumenCoincidenciasPorNiveles.setWindowState("normal");
				dwInvocarComparacionDepositos.setWindowState("hidden");
			}  
			
			refreshIgObjects(new Object[] { gvResumenCoincidenciasPorNiveles,
					lblResumenComparacionNivel, msgValidaResultadoComparacion,
					dwResumenCoincidenciasPorNiveles,
					dwInvocarComparacionDepositos });
			
		}
	}
	
	public void cerrarResumenCoincidenciasPorNiveles(ActionEvent ev){
		
		try {
			
			CodeUtil.removeFromSessionMap("pcd_lstResumenCoincidenciasPorNiveles") ; 
			CodeUtil.removeFromSessionMap("pcd_lstProcesarDepositosCoincidentes") ; 
			
			dwResumenCoincidenciasPorNiveles.setWindowState("hidden");
			
			dwResumenCoincidenciasPorNiveles = null;
			gvResumenCoincidenciasPorNiveles = null;
			lstResumenCoincidenciasPorNiveles = null;
			lblResumenComparacionNivel = null;
			lstProcesarDepositosCoincidentes = null;
			
			System.gc();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void cancelarIniciarComparacion(ActionEvent ev){
		
		CodeUtil.removeFromSessionMap(new String[]{sesmapvarDepsCoincidencias, sesmapvarDepsCoincidenciasConflicto });
		
		lstDepositoEnCoincidenciaConflicto = null;
		lstDepositoEnCoincidencia = null;
		
		dwInvocarComparacionDepositos.setWindowState("hidden"); 
	}
	
	public void borrarDocumentosExistentes(String strFolderName, String strSearchIn){
		try {
			
			HttpServletRequest sHttpRqst  = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
			String strFolderPath = sHttpRqst.getServletContext().getRealPath(  File.separatorChar + strFolderName ) ;
			
			
			File folder = new File( strFolderPath );
			
			if(!folder.exists()){
				folder.mkdirs();
				return;
			}
			
			if(folder.list() == null || folder.list().length == 0 )
				return;
			
			String [] sListaArchivos = folder.list();
		 
			for (String strFileName : sListaArchivos) {
				if( strFileName.toLowerCase().trim().contains( strSearchIn.trim().toLowerCase() ) ){
					File file = new File( strFolderPath + File.separatorChar + strFileName);
					if( file.exists() )
						file.delete();
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void invocarCompararDepositos(ActionEvent ev){
		
		cmbCuentasBancoDisponibles.dataBind();
		CodeUtil.refreshIgObjects(cmbCuentasBancoDisponibles);
		
		invocarCompararDepositos();
		
	}
	public void cambiarCuentaBancoSeleccionada(ValueChangeEvent ev){
		invocarCompararDepositos();
	}
	@SuppressWarnings("unchecked")
	public void invocarCompararDepositos(){
		String msg = "";
		
		Session session = null;
		Transaction trans = null;
		boolean newCn = false;
		
		try {
			
			/*
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, -1);
			String strSearchIn = new SimpleDateFormat("ddMMyyyy").format( cal.getTime() ) ;
			
			borrarDocumentosExistentes(PropertiesSystem.CARPETA_DOCUMENTOS_EXPORTAR, strSearchIn);
			*/
			
			session = HibernateUtilPruebaCn.currentSession();
			trans = (newCn = !(session.getTransaction().isActive())) ? session
					.beginTransaction() : session.getTransaction();
			
			CodeUtil.removeFromSessionMap(new String[]{sesmapvarDepsCoincidencias, sesmapvarDepsCoincidenciasConflicto });
			
			lstDepositoEnCoincidenciaConflicto = null;
			lstDepositoEnCoincidencia = null;
			
			final String strBancoCuentaComparar = cmbCuentasBancoDisponibles.getValue().toString();
			final String strBancoSeleccionado = strBancoCuentaComparar.split("@")[0];
			final String strCuentaBancoSeleccionado = strBancoCuentaComparar.split("@")[1];
			final String strMonedaCuentaBancoSeleccionado = strBancoCuentaComparar.split("@")[2];
			
			List<PcdConsolidadoDepositosBanco> lstDepositosBancoIniciales =  new ArrayList<PcdConsolidadoDepositosBanco> 
			((List<PcdConsolidadoDepositosBanco>)getFromSessionMap(ConsolidadoDepsBcoDAO.lstConsolidadoDpsBcoIniciales));
			
			List<PcdConsolidadoDepositosBanco> lstDepositosBanco  =  ( ArrayList<PcdConsolidadoDepositosBanco>) 
			CollectionUtils.select(lstDepositosBancoIniciales, new Predicate(){
				@Override
				public boolean evaluate(Object o) {
					PcdConsolidadoDepositosBanco db = (PcdConsolidadoDepositosBanco)o;
					return 
					String.valueOf( db.getCodigobanco() ) .compareTo(strBancoSeleccionado) == 0  &&
					String.valueOf( db.getNumerocuenta() ) .compareTo(strCuentaBancoSeleccionado) == 0 &&
					db.getMoneda().compareTo(strMonedaCuentaBancoSeleccionado) == 0;
				}
			});
			
			CodeUtil.putInSessionMap(ConsolidadoDepsBcoDAO.lstConsolidadoDpsTodos, lstDepositosBanco) ;
			
			if(lstDepositosBanco == null || lstDepositosBanco.isEmpty()){
				msg = "No hay depositos de banco para comparar ";
				return ;
			}
			
			//&& =============== cargar los datos de la compania a utilizar por las cajas con depositos por conciliar..
			String moneda = lstDepositosBanco.get(0).getMoneda();
			String codcomp =  lstDepositosBanco.get(0).getCodcomp().trim();
			long codigobanco = lstDepositosBanco.get(0).getCodigobanco() ;	
			
			//&& =============== depositos de caja a comparar 
			//List<Deposito> lstDepositosCaja_Report = ConsolidadoDepsBcoDAO.consultarDepositosCajaComparar(codcomp, strBancoSeleccionado, strMonedaCuentaBancoSeleccionado );
			
			//&& =============== depositos de caja a comparar 
			List<Deposito_Report> lstDepositosCaja = ConsolidadoDepsBcoDAO.consultarDepositosCajaComparar_Report(codcomp, strBancoSeleccionado, strMonedaCuentaBancoSeleccionado );
			
			
			if(lstDepositosCaja == null || lstDepositosCaja.isEmpty()){
				msg = " No hay depositos de caja para comparar ";
				return;
			}
			
			List<Integer>idsCajaConDepositos = (ArrayList<Integer>)CodeUtil.selectPropertyListFromEntity(lstDepositosCaja, "caid", true);
			List<Integer>idsCajaConDatos = new ArrayList<Integer>(idsCajaConDepositos) ; 
			
			
			String strSqlQueryExecute = 
						"select * from "+PropertiesSystem.ESQUEMA+".f55ca014 where c4id in ( " 
						+ idsCajaConDepositos.toString().replace("[", "").replace("]", "") 
						+" ) and c4stat = 'A'  and trim(c4rp01) = '"+codcomp+"' ";
			
			List<F55ca014>lstCompaniasPorCaja =  (ArrayList<F55ca014>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSqlQueryExecute, true, F55ca014.class)  ;
			CodeUtil.putInSessionMap("pcd_lstCompaniasPorCaja", lstCompaniasPorCaja);
		
			//&& =============== cuentas de banco a utilizar 
			idsCajaConDepositos = (ArrayList<Integer>)CodeUtil.selectPropertyListFromEntity(lstDepositosBanco, "numerocuenta", true);
			
			strSqlQueryExecute = 
				"select ( " +
				"   trim( cast ( f23.d3mcu  as varchar(12) ccsid 37 ) ) ||'.'|| trim( cast ( f23.d3obj  as varchar(6) ccsid 37) ) ||'.'||  trim(cast ( f23.d3sub  as varchar(8) ccsid 37 ) ) ||'@@@'|| "+
				"	trim( cast ( f09.gmaid as varchar(8) ccsid 37 ) )    ||'@@@'|| "+ 
				"	trim( right( trim( cast ( f23.d3mcu as varchar(12) ccsid 37 ) ),  2 ) )  ||'@@@'|| "+
				"	trim( cast ( f23.d3mcu  as varchar(12) ccsid 37 ) )  ||'@@@'|| "+
				"	trim( cast ( f23.d3obj  as varchar(6) ccsid 37 ) )   ||'@@@'|| "+
				"	trim( cast ( f23.d3sub  as varchar(8) ccsid 37 ) )   ||'@@@'|| "+
				"	trim( cast ( f23.d3rp01 as varchar(12) ccsid 37 ) )  ||'@@@'|| "+
				"	trim( f23.d3nocuenta ) )  as DATA"+ 
				" from  "+PropertiesSystem.ESQUEMA+".F55ca023 f23  inner join "+PropertiesSystem.ESQUEMA+".vf0901 f09 on "+ 
				"	trim(f23.d3mcu) = trim(f09.gmmcu) and  "+
				"	trim(f23.d3obj) = trim(f09.gmobj)  and  "+
				"	trim(f23.d3sub) = trim(f09.gmsub) "+
				" where f23.d3stat = 'A' and f23.d3nocuenta in ( "+idsCajaConDepositos.toString().replace("[", "").replace("]", "")+" ) " ;
					
			List<String> dtaQueryExecuted = (ArrayList<String>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSqlQueryExecute, true, null)  ; 
			
			List<String[]>dtaCuentaFromQuery =  new ArrayList<String[]>();
			for (String dtaCta : dtaQueryExecuted) {
				dtaCuentaFromQuery.add(dtaCta.split("@@@"));
			}
			CodeUtil.putInSessionMap("pcd_dtaCuentaContableXCuentaBanco", dtaCuentaFromQuery);
			
			//&& =================== Cuentas transitorias por banco
			idsCajaConDepositos = (ArrayList<Integer>)CodeUtil.selectPropertyListFromEntity(lstDepositosBanco, "numerocuenta", true);
			
			strSqlQueryExecute = 
				" select 	" +
				" trim( cast ( f09.gmmcu  as varchar(12) ccsid 37 ) ) ||'.'|| trim( cast ( f09.gmobj  as varchar(6) ccsid 37) ) ||'.'||  trim(cast ( f09.gmsub  as varchar(8) ccsid 37 ) ) ||'@@@'||  "+
				" trim( cast ( f09.gmaid as varchar(8) ccsid 37 ) )  ||'@@@'|| "+
				" trim( right( trim( cast ( f09.gmmcu as varchar(12) ccsid 37 ) ),  2 ) )  ||'@@@'|| "+
				" trim( cast ( f09.gmmcu  as varchar(12) ccsid 37 ) ) ||'@@@'|| "+
				" trim( cast ( f09.gmobj  as varchar(6) ccsid 37 )  ) ||'@@@'|| "+
				" trim( cast ( f09.gmsub  as varchar(8) ccsid 37 )  ) ||'@@@'|| "+
				" trim( cast ( F33.b3rp01 as varchar(12) ccsid 37 ) ) ||'@@@'|| "+
				" trim( cast ( F33.b3crcd as varchar(3) ccsid 37 ) )  ||'@@@'|| "+
				" trim( cast ( F33.b3codb as varchar(6) ccsid 37 ) ) " +
				
				" from  "+PropertiesSystem.ESQUEMA+".F55ca033 f33  inner join "+PropertiesSystem.ESQUEMA+".vf0901 f09 on "+ 	 
				"   trim(f09.gmaid) = trim(f33.b3ctat) "+
				" where b3codb = "+codigobanco+"  and b3crcd = '"+moneda+"' and trim(b3rp01) = '"+codcomp+"' " ;
			
			dtaQueryExecuted = (ArrayList<String>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSqlQueryExecute, true, null)  ; 
			
			dtaCuentaFromQuery =  new ArrayList<String[]>();
			for (String dtaCta : dtaQueryExecuted) {
				dtaCuentaFromQuery.add(dtaCta.split("@@@"));
			}
			CodeUtil.putInSessionMap("pcd_dtaCuentaTransitoriaxBanco", dtaCuentaFromQuery);
			
			// && ========================== cuentas para ajustes por sobrantes en los depositos. 
			strSqlQueryExecute =
				"select trim(gmmcu) ||'.'|| trim(gmobj) ||'.'|| trim(gmsub)   ||'@@@'|| "+
					"trim(gmaid) ||'@@@'||  "+
					"right( trim( gmco ),  2 ) ||'@@@'||  "+
					"trim(gmmcu) ||'@@@'|| "+
					"trim(gmobj) ||'@@@'|| "+
					"trim(gmsub) ||'@@@'|| "+
					"'"+codcomp.trim()+"' ||'@@@'|| " +
					"trim(gmco)" +
				"from "+PropertiesSystem.ESQUEMA+".vf0901  " +
				"where trim(gmobj) = '" + PropertiesSystem.CTA_OTROS_INGRESOS_OB +"' " + 
				 " and trim(gmsub) = '" + PropertiesSystem.CTA_OTROS_INGRESOS_SB +"' " + 
				 " and trim(gmmcu) in ( "+
						" select trim(c4cjmcu) from "+PropertiesSystem.ESQUEMA+".f55ca014 where trim(c4rp01) = '"+codcomp.trim() +"' " +
						" and c4stat = 'A' and c4id in ( "+ idsCajaConDatos.toString().replace("[", "").replace("]", "") + " )  "+
						")  " ;
			dtaQueryExecuted = (ArrayList<String>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSqlQueryExecute, true, null)  ; 
		
			dtaCuentaFromQuery =  new ArrayList<String[]>();
			for (String dtaCta : dtaQueryExecuted) {
				dtaCuentaFromQuery.add(dtaCta.split("@@@"));
			}
			CodeUtil.putInSessionMap("pcd_dtaCuentaSobrantePorUnidadNegocio", dtaCuentaFromQuery);
			
			
			// && ========================== cuentas para ajustes por faltantes en los depositos. 
			strSqlQueryExecute =
				"select trim(gmmcu) ||'.'|| trim(gmobj) || (case when trim(gmsub) = '' then '' else '.' || trim(gmsub) end )  ||'@@@'|| "+
					"trim(gmaid) ||'@@@'||  "+
					"right( trim( gmmcu),  2 ) ||'@@@'||  "+
					"trim(gmmcu) ||'@@@'|| "+
					"trim(gmobj) ||'@@@'|| "+
					"  (case when trim(gmsub) = '' then '    @@@' else  trim(gmsub) end ) " +
				"from "+PropertiesSystem.ESQUEMA+".vf0901  " +
				"where trim(gmobj) = '"+ PropertiesSystem.CTA_DEUDORES_VARIOS_OB  +"' " + 
				 " and trim(gmsub) = '"+ PropertiesSystem.CTA_DEUDORES_VARIOS_SB +"' " + 
				 " and trim(gmmcu) in ( " +
						 "'" + PropertiesSystem.CTA_DEUDORES_VARIOS_UNE01 + "', " + 
						 "'" + PropertiesSystem.CTA_DEUDORES_VARIOS_UNE02 + "', " + 
						 "'" + PropertiesSystem.CTA_DEUDORES_VARIOS_UNE03 + "', " + 
						 "'" + PropertiesSystem.CTA_DEUDORES_VARIOS_UNE08 + "'" +
			 		" ) " ;
			
			dtaQueryExecuted = (ArrayList<String>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSqlQueryExecute, true, null)  ; 
		
			dtaCuentaFromQuery =  new ArrayList<String[]>();
			for (String dtaCta : dtaQueryExecuted) {
				dtaCuentaFromQuery.add(dtaCta.split("@@@"));
			}
			CodeUtil.putInSessionMap("pcd_dtaCuentaFaltantePorUnidadNegocio", dtaCuentaFromQuery);
		
			
			// && ========================== cuentas para ajustes por sobrantes en los depositos. 
			strSqlQueryExecute =
				"select trim(gmmcu) ||'.'|| trim(gmobj) ||'.'|| trim(gmsub)   ||'@@@'|| "+
					"trim(gmaid) ||'@@@'||  "+
					"right( trim( gmco ),  2 ) ||'@@@'||  "+
					"trim(gmmcu) ||'@@@'|| "+
					"trim(gmobj) ||'@@@'|| "+
					"trim(gmsub) ||'@@@'|| "+
					"'"+codcomp.trim()+"' ||'@@@'|| " +
					"trim(gmco)" +
				"from "+PropertiesSystem.ESQUEMA+".vf0901  " +
				"where trim(gmobj) = '" + PropertiesSystem.CTA_GASTOS_DIVERSOS_OB  +"' " + 
				 " and trim(gmsub) = '" + PropertiesSystem.CTA_GASTOS_DIVERSOS_SB +"' " + 
				 " and trim(gmmcu) in ( "+
						" select trim(c4cjmcu) from "+PropertiesSystem.ESQUEMA+".f55ca014 where trim(c4rp01) = '"+codcomp.trim() +"' " +
						" and c4stat = 'A' and c4id in ( "+ idsCajaConDatos.toString().replace("[", "").replace("]", "") + " )  "+
						")  " ;
			dtaQueryExecuted = (ArrayList<String>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSqlQueryExecute, true, null)  ; 
		
			dtaCuentaFromQuery =  new ArrayList<String[]>();
			for (String dtaCta : dtaQueryExecuted) {
				dtaCuentaFromQuery.add(dtaCta.split("@@@"));
			}
			CodeUtil.putInSessionMap("pcd_dtaCuentaOtrosGastosPorUnidadNegocio", dtaCuentaFromQuery);
			
			
			// && ========================== cuentas para ajustes por excepcion en proceso de generacion automatica de comprobantes
			String queryCuentaAjuste = 
			 " select cuentaid from @BDCAJA.cuentas_preconciliacion cp " +
			 " where cp.estado = 1 and cp.tipocuenta = 1 and trim(cp.codcomp) = '@CODCOMP' " +
			 " and cp.moneda = '@MONEDA' and cp.codigobanco = @IDBANCO";			
			
			strSqlQueryExecute =
				"select trim(gmmcu) ||'.'|| trim(gmobj) ||'.'|| trim(gmsub)   ||'@@@'|| "+
					"trim(gmaid) ||'@@@'||  "+
					"right( trim( gmco ),  2 ) ||'@@@'||  "+
					"trim(gmmcu) ||'@@@'|| "+
					"trim(gmobj) ||'@@@'|| "+
					"trim(gmsub) ||'@@@'|| "+
					"'@CODCOMP' ||'@@@'|| " +
					"trim(gmco)" +
				"from  @BDCAJA.vf0901  " +
				"where trim(gmaid)  in (@IDSCUENTAAJUSTES) ";
					 
			strSqlQueryExecute = strSqlQueryExecute
					.replace("@IDSCUENTAAJUSTES", queryCuentaAjuste ) 
					.replace("@BDCAJA", PropertiesSystem.ESQUEMA )
					.replace("@CODCOMP", codcomp.trim() )
					.replace("@MONEDA",  moneda)
					.replace("@IDBANCO", String.valueOf(codigobanco ) ); 
					
			dtaQueryExecuted = (ArrayList<String>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSqlQueryExecute, true, null)  ; 
		
			dtaCuentaFromQuery =  new ArrayList<String[]>();
			for (String dtaCta : dtaQueryExecuted) {
				dtaCuentaFromQuery.add(dtaCta.split("@@@"));
			}
			CodeUtil.putInSessionMap("pcd_dtaCuentaAjustePorExcepcion", dtaCuentaFromQuery);
			
			
			dtaCuentaFromQuery = null;
			dtaQueryExecuted = null;
			
			// && ========================== Conservar los depositos de caja
			CodeUtil.putInSessionMap(ConsolidadoDepsBcoDAO.lstTodosDepositosCaja, lstDepositosCaja);
		//CodeUtil.putInSessionMap(ConsolidadoDepsBcoDAO.lstTodosDepositosCaja, lstDepositosCaja_Report);
			ConsolidadoDepsBcoDAO.createCompareLevels();
			
			txtNivelesComparaValue.setValue(ConsolidadoDepsBcoDAO.getDfnNivelesCompara());
			strRsmTotalDepBco.setValue(String.valueOf(lstDepositosBanco.size()));
			strRsmTotalCoincidenciasBco.setValue("0");
			strRsmTotalDepCaja.setValue(String.valueOf(lstDepositosCaja.size()));
			//strRsmTotalDepCaja.setValue(String.valueOf(lstDepositosCaja_Report.size()));
			strRsmTotalCoincidenciasCaja.setValue("0");
			strRsmCoincidentesUnoAuno.setValue(String.valueOf(lstDepositosBanco.size()));
			strRsmCoincidentesUnoAMuchos.setValue("0");
			strTotalCoincidenciaEnConflicto.setValue("0");
			msgValidaResultadoComparacion.setValue("");
			
			strRsmRangoFechasBanco.setValue( 
				" Desde " +  FechasUtil.formatDatetoString( lstDepositosBanco.get(0).getFechadeposito(), "dd/MM/yyyy") + 
				" Hasta " + FechasUtil.formatDatetoString( lstDepositosBanco.get(lstDepositosBanco.size()-1).getFechadeposito() , "dd/MM/yyyy")  ) ;
			
			strRsmRangoFechasCaja.setValue(
				" Desde " + FechasUtil.formatDatetoString( lstDepositosCaja.get(0).getFecha(), "dd/MM/yyyy") + 
				" Hasta " + FechasUtil.formatDatetoString( lstDepositosCaja.get(lstDepositosCaja.size()-1).getFecha() , "dd/MM/yyyy") ) ;
			 
			lnkValidarConflictos.setStyle("display:none;");
			
			dwInvocarComparacionDepositos.setWindowState("normal");
			
			refreshIgObjects(new Object[] { 
				lnkValidarConflictos, strRsmTotalDepBco,
				strRsmTotalCoincidenciasBco, strRsmTotalDepCaja,
				strRsmTotalCoincidenciasCaja, strRsmCoincidentesUnoAuno,
				strRsmCoincidentesUnoAMuchos, strTotalCoincidenciaEnConflicto, 
				strRsmRangoFechasBanco,	strRsmRangoFechasCaja,
				txtNivelesComparaValue, dwInvocarComparacionDepositos, 
				msgValidaResultadoComparacion});
			
			lstDepositosCaja = null;
			lstDepositosBanco = null;
			//lstDepositosCaja_Report = null;
			
			
		} catch (Exception e) {
			msg = " Error al cargar datos resumen del proceso";
			e.printStackTrace();
		}finally{
			
			if( !msg.isEmpty() ){
				dwMensajesValidacion.setWindowState("normal");
				lblMensajeValidacionProcesos.setValue(msg);
				
				msgValidaResultadoComparacion.setValue(msg);
				CodeUtil.refreshIgObjects(msgValidaResultadoComparacion);
			}
			
			if( newCn && trans.isActive() && session.isOpen()  ){
				try {
					trans.commit();
				} catch (Exception e) { }
				try {
					HibernateUtilPruebaCn.closeSession(session);
				} catch (Exception e) { }
				session = null;
				trans = null;
			}
			
			//contabilizarBatchPerdientes();
		}
	}
	
	public static void contabilizarBatchPerdientes(){
		try {
			
			
			String upEstadosNuevos = 
			" update " + 
			" @BDCAJA.conciliacion f set  " + 
			" estadobatch = ifnull( ( select icist from @JDEDTA.f0011 where icicu = f.nobatch  fetch first rows only), 'E' )" +
			" where f.estadobatch = 'A'  " ; 
			
			upEstadosNuevos = upEstadosNuevos
			.replace("@BDCAJA", PropertiesSystem.ESQUEMA )
			.replace("@JDEDTA", PropertiesSystem.JDEDTA ) ;
				
			try {
				//Ajustado por LFonseca
				//2020-11-28
				//Manejo de sessiones para este caso es null
				ConsolidadoDepositosBcoCtrl.executeSqlQuery(null, null, upEstadosNuevos) ;
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			
			String strUpdatePgmJdeContabiliza =
					" update "+PropertiesSystem.JDECOM+".f98301 set DESVL = @NUMEROBATCH " +
					"WHERE DEPID ='P09800' and DEVERS ='PRE_CONCIL' AND DEFLDN='GLICU' "  ;    
			
			String sql = " select cast( icicu  as integer) from "+PropertiesSystem.JDEDTA+".f0011 where icist = 'A'  and icicu in" +
					" ( select distinct(nobatch) from "+PropertiesSystem.ESQUEMA+".conciliacion c where c.estadobatch = 'A'  )  " ;
			
			@SuppressWarnings("unchecked")
			List<Integer> numerosbatch = (List<Integer>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, true, null);
			
			if(numerosbatch == null || numerosbatch.isEmpty() )
				return;
			
//			P5509800Input input = new P5509800Input();
//			P5509800_Service service = new P5509800_Service();
//			P5509800PortType port = service.getP5509800HttpSoap11Endpoint();
			
			Thread.currentThread();
			
			List<Integer> contabilizados = new ArrayList<Integer>();
			
			for (int i = 0; i <numerosbatch.size();  i++) {
				
				Thread.sleep(300);
				
				int numerobatch = numerosbatch.get(i);
				String queryUpdate = strUpdatePgmJdeContabiliza.replace("@NUMEROBATCH", String.valueOf(numerobatch) ); 
				boolean update = ConsolidadoDepositosBcoCtrl.executeSqlQuery(null, null, queryUpdate) ;
				
				if( !update ){
					continue;
				}
//				port.p5509800(input);
				
				contabilizados.add(numerobatch);
				
			}
			/*
			if( !contabilizados.isEmpty() ) {
				
				String updateStatusCn = 
				"update " + 
				"@BDCAJA.conciliacion f set  " + 
				" estadobatch = ifnull( ( select icist from @JDEDTA.f0011 where icicu = f.nobatch  fetch first rows only), 'E' ) " + 
				" where nobatch in  @NUMEROSBATCH  " ;
				
				String numeros = contabilizados.toString().replace("[", "(").replace("]",")" );
				
				updateStatusCn = updateStatusCn
					.replace("@BDCAJA", PropertiesSystem.ESQUEMA )
					.replace("@JDEDTA", PropertiesSystem.JDEDTA )
					.replace("@NUMEROSBATCH", numeros ) ;
				
				boolean update = ConsolidadoDepositosBcoCtrl.executeSqlQuery(updateStatusCn) ;
				
				
			}
*/
			
			
		} catch (Exception e) {
			 e.printStackTrace();
		}
	}
	
	
	public void mostrarConfirmarDepositos(ActionEvent ev){
		try {
			
			ConsolidadoCoincidente cc = (ConsolidadoCoincidente)DataRepeater.getDataRow(((RowItem)ev.getComponent().getParent().getParent()));
			
			if(cc.getExcluircoincidencia() == 1 ){
				return;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void excluirCoincidenciasDeposito(ActionEvent ev) {
		try {
			
			final ConsolidadoCoincidente dpExcluir = (ConsolidadoCoincidente)getFromSessionMap("coincidenciaAExcluir") ;
			
			@SuppressWarnings("unchecked")
			List<ConsolidadoCoincidente> coincidencias = (List<ConsolidadoCoincidente>)getFromSessionMap( sesmapvarDepsCoincidencias );
			
			ConsolidadoCoincidente cc = (ConsolidadoCoincidente )
			CollectionUtils.find(coincidencias, new Predicate(){
			 
				public boolean evaluate(Object o) {
					ConsolidadoCoincidente cc = (ConsolidadoCoincidente)o;
					return	cc.getIdresumenbanco() == dpExcluir.getIdresumenbanco() ;
				}
			}) ;
			
			cc.setExcluircoincidencia(1);
			cc.setMotivoexclusion(txtMotivoExclusionConDeps.getValue().toString());
			
			putInSessionMap(sesmapvarDepsCoincidencias, coincidencias);
			
			gvDepositoEnCoincidencia.dataBind();        
			dwExcluirCoincidencia.setWindowState("hidden");
			
			refreshIgObjects(new Object[]{gvDepositoEnCoincidencia});
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void cancelarExcluirCoincidencia(ActionEvent ev) {
		dwExcluirCoincidencia.setWindowState("hidden");
	}
	public void mostrarExcluirCoincidencia(ActionEvent ev) {
		String msg = "" ;
		
		try {
			
			ConsolidadoCoincidente cc = (ConsolidadoCoincidente)DataRepeater.getDataRow(((RowItem)ev.getComponent().getParent().getParent()));
			
			if(cc.getExcluircoincidencia() == 1 ){
				msg = " La coincidencia ha sido sometida a exclusión ";
				return;
			}
			if(cc.isComprobanteaplicado() ){
				msg = " La coincidencia ya fue procesada bajo batch # "+ cc.getNobatch() ;
				return;
			}
			
			txtMotivoExclusionConDeps.setValue("");
			msgConfirmaExcluirCoincidencia.setValue("¿Excluir la comprobación para el depósito referencia " + cc.getReferenciabanco() +"?");
			dwExcluirCoincidencia.setWindowState("normal");
			putInSessionMap("coincidenciaAExcluir", cc);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if( !msg.isEmpty() ){
				lblMensajeValidacionProcesos.setValue(msg);
				dwMensajesValidacion.setWindowState("normal");
			}
		}
	}
	
	public void excluirCoincidenciaDeposito(ActionEvent ev){
		String msg = "" ;
		
		try {
			
			final ConsolidadoCoincidente ccExcluir = (ConsolidadoCoincidente)DataRepeater.getDataRow(((RowItem)ev.getComponent().getParent().getParent()));
			if(ccExcluir.isComprobanteaplicado() ){
				msg = " La coincidencia ya fue procesada bajo batch # "+ ccExcluir.getNobatch() ;
				return;
			}
			
			@SuppressWarnings("unchecked")
			List<ConsolidadoCoincidente> coincidencias =  (ArrayList<ConsolidadoCoincidente>)CodeUtil.getFromSessionMap("pcd_lstProcesarDepositosCoincidentes") ;
			
			ConsolidadoCoincidente ccUpdateVal = (ConsolidadoCoincidente )
					CollectionUtils.find(coincidencias, new Predicate(){
						public boolean evaluate(Object o) {
							ConsolidadoCoincidente cc = (ConsolidadoCoincidente)o;
							return	cc.getIdresumenbanco() == ccExcluir.getIdresumenbanco() ;
						}
					}) ;
			
			if( ccUpdateVal.getExcluircoincidencia() == 1 )
				ccUpdateVal.setExcluircoincidencia(0);
			else
			if( ccUpdateVal.getExcluircoincidencia() == 0 )
				ccUpdateVal.setExcluircoincidencia(1);
			
			CodeUtil.putInSessionMap("pcd_lstProcesarDepositosCoincidentes", coincidencias);
			
			gvProcesarDepositosCoincidentes.dataBind();        
			
			refreshIgObjects(new Object[]{gvProcesarDepositosCoincidentes});
			coincidencias = null;
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			if( !msg.isEmpty() ){
				lblMensajeValidacionProcesos.setValue(msg);
				dwMensajesValidacion.setWindowState("normal");
			}
 
		}
	}
	
	
	
	
	
	public void buscarCoincidenciasConsolidado(ActionEvent ev){
		String msg = "" ;
		
		try {
			
			CodeUtil.removeFromSessionMap("pcd_PeriodoFiscalActual");
			
			//&& =============== Consultar el periodo fiscal actual.
			int[] periodofiscal = FechasUtil.obtenerPeriodoFiscalActual("E01");
			CodeUtil.putInSessionMap("pcd_PeriodoFiscalActual", periodofiscal);
			
			
			CodeUtil.removeFromSessionMap(new String[] {
					sesmapvarDepsCoincidencias,
					sesmapvarDepsCoincidenciasConflicto,
					"pcd_lstDepositosCajaNoCoincidentes",
					"pcd_lstDepositosBancoNoCoincidentes" 
				});
			
			
			int codigoempleado = ((Vautoriz[])getFromSessionMap("sevAut"))[0].getId().getCodreg();
			
			ConsolidadoDepsBcoDAO.setCodigousuario(codigoempleado) ;
			
			ConsolidadoDepsBcoDAO.matchDeposits() ;
			
			if(ConsolidadoDepsBcoDAO.getStatusMessage().compareTo("") != 0 ){
				msg = ConsolidadoDepsBcoDAO.getStatusMessage() ; 
				return;
			}
			
			lstDepositoEnCoincidencia = new ArrayList<ConsolidadoCoincidente>(ConsolidadoDepsBcoDAO.getCoincidencias());
			if(lstDepositoEnCoincidencia == null || lstDepositoEnCoincidencia.isEmpty()){
				lstDepositoEnCoincidencia = new ArrayList<ConsolidadoCoincidente>() ;
			}
			CodeUtil.putInSessionMap(sesmapvarDepsCoincidencias, lstDepositoEnCoincidencia);
			 

			strRsmTotalDepBco .setValue( String.valueOf( String.valueOf(ConsolidadoDepsBcoDAO.getRsmTotalDepBco() ) ));
			strRsmTotalCoincidenciasBco .setValue( String.valueOf(ConsolidadoDepsBcoDAO.getRsmTotalCoincidenciasBco()));
			strRsmTotalDepCaja .setValue( String.valueOf(ConsolidadoDepsBcoDAO.getRsmTotalDepCaja()));
			strRsmTotalCoincidenciasCaja .setValue( String.valueOf(ConsolidadoDepsBcoDAO.getRsmTotalCoincidenciasCaja()));
			strRsmCoincidentesUnoAuno .setValue( String.valueOf(ConsolidadoDepsBcoDAO.getRsmCoincidentesUnoAuno())) ;
			strRsmCoincidentesUnoAMuchos .setValue( String.valueOf(ConsolidadoDepsBcoDAO.getRsmCoincidentesUnoAMuchos())) ;
 
			refreshIgObjects(new Object[] { 
					strRsmTotalDepBco,
					strRsmTotalCoincidenciasBco, strRsmTotalDepCaja,
					strRsmTotalCoincidenciasCaja, strRsmCoincidentesUnoAuno,
					strRsmCoincidentesUnoAMuchos, strRsmRangoFechasBanco,	
					strRsmRangoFechasCaja, txtNivelesComparaValue, 
					lblMensajeValidacionProcesos });
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			if(!msg.isEmpty()){
				dwMensajesValidacion.setWindowState("normal");
			}
			refreshIgObjects(new Object[] { lblMensajeValidacionProcesos } );
			
		}
	}
	
	
	public void buscarCoincidenciasConsolidado(){
		String msg = "" ;
		try {
			
			removeFromSessionMap( sesmapvarDepsCoincidencias );
			removeFromSessionMap(sesmapvarDepsCoincidenciasConflicto);
			
			int codigoempleado = ((Vautoriz[])getFromSessionMap("sevAut"))[0].getId().getCodreg();
			
			ConsolidadoDepsBcoDAO.setCodigousuario(codigoempleado) ;
			
			ConsolidadoDepsBcoDAO.matchDeposits() ;
			
			if(ConsolidadoDepsBcoDAO.getStatusMessage().compareTo("") != 0 ){
				msg = ConsolidadoDepsBcoDAO.getStatusMessage() ; 
				return;
			}
			
			lstDepositoEnCoincidencia = new ArrayList<ConsolidadoCoincidente>(ConsolidadoDepsBcoDAO.getCoincidencias());
			if(lstDepositoEnCoincidencia == null || lstDepositoEnCoincidencia.isEmpty()){
				lstDepositoEnCoincidencia = new ArrayList<ConsolidadoCoincidente>() ;
			}
			putInSessionMap(sesmapvarDepsCoincidencias, lstDepositoEnCoincidencia);
			
			lstDepositoEnCoincidenciaConflicto = new ArrayList<ConsolidadoCoincidente>(ConsolidadoDepsBcoDAO.getCoincidenciasConflicto());
			if(lstDepositoEnCoincidenciaConflicto == null || lstDepositoEnCoincidenciaConflicto.isEmpty()){
				lstDepositoEnCoincidenciaConflicto = new ArrayList<ConsolidadoCoincidente>() ;
			}
			putInSessionMap(sesmapvarDepsCoincidenciasConflicto, lstDepositoEnCoincidenciaConflicto);

			strRsmTotalDepBco .setValue( String.valueOf( String.valueOf(ConsolidadoDepsBcoDAO.getRsmTotalDepBco() ) ));
			strRsmTotalCoincidenciasBco .setValue( String.valueOf(ConsolidadoDepsBcoDAO.getRsmTotalCoincidenciasBco()));
			strRsmTotalDepCaja .setValue( String.valueOf(ConsolidadoDepsBcoDAO.getRsmTotalDepCaja()));
			strRsmTotalCoincidenciasCaja .setValue( String.valueOf(ConsolidadoDepsBcoDAO.getRsmTotalCoincidenciasCaja()));
			strRsmCoincidentesUnoAuno .setValue( String.valueOf(ConsolidadoDepsBcoDAO.getRsmCoincidentesUnoAuno())) ;
			strRsmCoincidentesUnoAMuchos .setValue( String.valueOf(ConsolidadoDepsBcoDAO.getRsmCoincidentesUnoAMuchos())) ;
			/*strTotalCoincidenciaEnConflicto.setValue( String.valueOf(ConsolidadoDepsBcoDAO.getRsmCoincidenciaEnConflicto() ) );*/
			
			strTotalCoincidenciaEnConflicto.setValue( 
					String.valueOf(ConsolidadoDepsBcoDAO.getRsmCoincidenciaEnConflicto() ) +" / " +
					String.valueOf(ConsolidadoDepsBcoDAO.getRsmCoincidenciaCajaEnConflicto() )
				);
			
			lnkValidarConflictos.setStyle( lstDepositoEnCoincidenciaConflicto.isEmpty()? "display:none": "display:inline;  color:red; " );
			
			refreshIgObjects(new Object[] { 
					lnkValidarConflictos , strRsmTotalDepBco,
					strRsmTotalCoincidenciasBco, strRsmTotalDepCaja,
					strRsmTotalCoincidenciasCaja, strRsmCoincidentesUnoAuno,
					strRsmCoincidentesUnoAMuchos, strTotalCoincidenciaEnConflicto, 
					strRsmRangoFechasBanco,	strRsmRangoFechasCaja,
					txtNivelesComparaValue, lblMensajeValidacionProcesos });
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			if(!msg.isEmpty()){
				dwMensajesValidacion.setWindowState("normal");
			}
			refreshIgObjects(new Object[] { lblMensajeValidacionProcesos } );
			
		}
	}

	public void cerrarDialogComparacion(ActionEvent ev){
		try {
			
			dwInvocarComparacionDepositos.setWindowState("normal");
			dwComparacionDepositos.setWindowState("hidden");
			
			refreshIgObjects(new Object[] { dwInvocarComparacionDepositos } );
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void recargarConsolidados(ActionEvent ev){
		try {
			 
			lstConsolidaDepbsBco = ConsolidadoDepsBcoDAO.getConsolidadoDepositos(true);
			lstResumenTipoTransaccion = ConsolidadoDepsBcoDAO.generarResumenTransacciones(); 
			ltResumenConsolidadoDepositos = ConsolidadoDepsBcoDAO.getDatosResumenConsolidadoDepositos();
			
			gvConsolidadoDepositosBanco.dataBind();
			gvResumenTipoTransaccion.dataBind();
			gvResumenConsolidadoDepositos.dataBind();
			
			refreshIgObjects(new Object[]{gvConsolidadoDepositosBanco, gvResumenTipoTransaccion,gvResumenConsolidadoDepositos});
			
		} catch (Exception e) {
			 e.printStackTrace();
		}
	}
	
	public void filtrarConsolidadoBanco(ActionEvent ev){
		try {
 
			
			String tipotransjde = cmbTransaccionesJde.getValue().toString();
			String moneda = cmbMonedasDeposito.getValue().toString();
			String codigobanco = cmbBancosPreconfirmacion.getValue().toString();
			String montominimo = txtMontoInicial.getValue().toString().trim();
			String montomaximo = txtMontoFinal.getValue().toString().trim();
			String referencia = txtNumeroReferencia.getValue().toString().trim();

			String tipotransbco = txtCodigosFiltroBanco.getValue().toString(); 
			if(tipotransbco.trim().compareTo("") != 0){
				List<String> values = new ArrayList<String>(tipotransbco.split(",").length);
				for (String codigobco : tipotransbco.split(",")) {
					values.add( "'"+codigobco.trim()+"'");
				}
				tipotransbco = values.toString().replace("[", "(").replace("]", ")" );
			} 
			
			List<String> valores = new ArrayList<String>();
			if(codigobanco.compareTo(valorInicialListaCombo) != 0)
				valores.add("codigobanco@" + codigobanco +"@_");
			
			if(tipotransjde.compareTo(valorInicialListaCombo) != 0)
				valores.add("tipotransaccionjde@" + tipotransjde+"@_");
			
			if(tipotransbco.compareTo("") != 0)
				valores.add("codigotransaccionbco@" + tipotransbco +" @ in ");
			
			if(moneda.compareTo(valorInicialListaCombo) != 0)
				valores.add("moneda@" + moneda + "@_");
			if(dcFechaDesde.getValue() != null){
				Calendar cal1 = Calendar.getInstance();
				cal1.setTime( (Date) dcFechaDesde.getValue() );
				cal1.add(Calendar.DATE, -1);
				valores.add("fechadeposito@" + new SimpleDateFormat("yyyy-MM-dd").format( cal1.getTime() )+"@ >= ");
			}
			if(dcFechaHasta.getValue() != null){
				Calendar cal1 = Calendar.getInstance();
				cal1.setTime( (Date) dcFechaHasta.getValue() );
				cal1.add(Calendar.DATE, +1);
				valores.add("fechadeposito@" + new SimpleDateFormat("yyyy-MM-dd").format( cal1.getTime() )+"@ <= ");
			}
			if(montominimo.trim().compareTo("") != 0)
				valores.add("montooriginal@" + new BigDecimal(montominimo).subtract(BigDecimal.ONE).toString()+"@ >= ");
			if(montomaximo.trim().compareTo("") != 0)
				valores.add("montooriginal@" + new BigDecimal(montomaximo).add(BigDecimal.ONE).toString()+"@ <= ");
			
			if(referencia.trim().compareTo("") != 0){
				if(referencia.matches(PropertiesSystem.REGEXP_NUMBER))
					referencia = Integer.valueOf(referencia.trim()).toString();
				valores.add("referenciaoriginal@" + referencia+"@ <= ");
			}
				
			lstConsolidaDepbsBco = ConsolidadoDepsBcoDAO.filtrarConsolidadoDepositos( valores );
			gvConsolidadoDepositosBanco.dataBind();
			gvResumenTipoTransaccion.dataBind();
			gvResumenConsolidadoDepositos.dataBind();
			gvConsolidadoDepositosBanco.setPageIndex(0);
			
			lblResultadoFiltrarDepositos.setValue("Resultado(s) Encontrado(s): " + 	ConsolidadoDepsBcoDAO.getiResultadosBusqueda() );
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void refreshIgObjects(Object[] igObjects){
		try {
			for (Object igComponent : igObjects) {
				SmartRefreshManager.getCurrentInstance()
					.addSmartRefreshId(((UIComponentBase) igComponent)
					.getClientId(FacesContext.getCurrentInstance()));		
			}
		} catch (Exception e) {
			 e.printStackTrace();
		}
	}
	public static void putInSessionMap(String varname, Object varvalue){
		try {
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(varname, varvalue ) ;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static Object getFromSessionMap(String varname) {
		Object ob = null;
		
		try {
			ob = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(varname) ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ob; 
	}
	
	public static void removeFromSessionMap(String varname) {
		try {
			 FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(varname) ;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public HtmlDropDownList getCmbBancosPreconfirmacion() {
		return cmbBancosPreconfirmacion;
	}
	public void setCmbBancosPreconfirmacion(
			HtmlDropDownList cmbBancosPreconfirmacion) {
		this.cmbBancosPreconfirmacion = cmbBancosPreconfirmacion;
	}
	public HtmlDropDownList getCmbMonedasDeposito() {
		return cmbMonedasDeposito;
	}
	public void setCmbMonedasDeposito(HtmlDropDownList cmbMonedasDeposito) {
		this.cmbMonedasDeposito = cmbMonedasDeposito;
	}
	public List<SelectItem> getLstBancosPreconfirmacion() {
		return lstBancosPreconfirmacion = ConsolidadoDepsBcoDAO.getBancosPreconciliar();
	}
	public void setLstBancosPreconfirmacion(
			List<SelectItem> lstBancosPreconfirmacion) {
		this.lstBancosPreconfirmacion = lstBancosPreconfirmacion;
	}
	public List<SelectItem> getLstMonedasDeposito() {
		return lstMonedasDeposito = ConsolidadoDepsBcoDAO.getMonedasConfiguradas();
	}
	public void setLstMonedasDeposito(List<SelectItem> lstMonedasDeposito) {
		this.lstMonedasDeposito = lstMonedasDeposito;
	}
	public HtmlDateChooser getDcFechaDesde() {
		return dcFechaDesde;
	}
	public void setDcFechaDesde(HtmlDateChooser dcFechaDesde) {
		this.dcFechaDesde = dcFechaDesde;
	}
	public HtmlDateChooser getDcFechaHasta() {
		return dcFechaHasta;
	}
	public void setDcFechaHasta(HtmlDateChooser dcFechaHasta) {
		this.dcFechaHasta = dcFechaHasta;
	}
	public HtmlInputText getTxtMontoInicial() {
		return txtMontoInicial;
	}
	public void setTxtMontoInicial(HtmlInputText txtMontoInicial) {
		this.txtMontoInicial = txtMontoInicial;
	}
	public HtmlInputText getTxtMontoFinal() {
		return txtMontoFinal;
	}
	public void setTxtMontoFinal(HtmlInputText txtMontoFinal) {
		this.txtMontoFinal = txtMontoFinal;
	}
	public HtmlDropDownList getCmbTransaccionesJde() {
		return cmbTransaccionesJde;
	}
	public void setCmbTransaccionesJde(HtmlDropDownList cmbTransaccionesJde) {
		this.cmbTransaccionesJde = cmbTransaccionesJde;
	}
	public HtmlDropDownList getCmbTransaccionesBanco() {
		return cmbTransaccionesBanco;
	}
	public void setCmbTransaccionesBanco(HtmlDropDownList cmbTransaccionesBanco) {
		this.cmbTransaccionesBanco = cmbTransaccionesBanco;
	}
	public List<SelectItem> getLstTransaccionesBanco() {
		return lstTransaccionesBanco = ConsolidadoDepsBcoDAO.getTipoTransaccionesBanco();
	}
	public void setLstTransaccionesBanco(List<SelectItem> lstTransaccionesBanco) {
		this.lstTransaccionesBanco = lstTransaccionesBanco;
	}
	public List<SelectItem> getLstTransaccionesJde() {
		return lstTransaccionesJde = ConsolidadoDepsBcoDAO.getTipoTransaccionesJDE();
	}
	public void setLstTransaccionesJde(List<SelectItem> lstTransaccionesJde) {
		this.lstTransaccionesJde = lstTransaccionesJde;
	}
	public HtmlGridView getGvResumenTipoTransaccion() {
		return gvResumenTipoTransaccion;
	}
	public void setGvResumenTipoTransaccion(HtmlGridView gvResumenTipoTransaccion) {
		this.gvResumenTipoTransaccion = gvResumenTipoTransaccion;
	}
	public List<ResumenDepositosTipoTransaccion> getLstResumenTipoTransaccion() {
		return lstResumenTipoTransaccion = ConsolidadoDepsBcoDAO.generarResumenTransacciones();
	}
	public void setLstResumenTipoTransaccion(
			List<ResumenDepositosTipoTransaccion> lstResumenTipoTransaccion) {
		this.lstResumenTipoTransaccion = lstResumenTipoTransaccion;
	}
	public HtmlGridView getGvConsolidadoDepositosBanco() {
		return gvConsolidadoDepositosBanco;
	}
	public void setGvConsolidadoDepositosBanco(
			HtmlGridView gvConsolidadoDepositosBanco) {
		this.gvConsolidadoDepositosBanco = gvConsolidadoDepositosBanco;
	}
	public List<PcdConsolidadoDepositosBanco> getLstConsolidaDepbsBco() {
		return lstConsolidaDepbsBco = ConsolidadoDepsBcoDAO.getConsolidadoDepositos(false);
	}
	public void setLstConsolidaDepbsBco(
			List<PcdConsolidadoDepositosBanco> lstConsolidaDepbsBco) {
		this.lstConsolidaDepbsBco = lstConsolidaDepbsBco;
	}
	public HtmlInputText getTxtNumeroReferencia() {
		return txtNumeroReferencia;
	}
	public void setTxtNumeroReferencia(HtmlInputText txtNumeroReferencia) {
		this.txtNumeroReferencia = txtNumeroReferencia;
	}
	public HtmlOutputText getLblResultadoFiltrarDepositos() {
		return lblResultadoFiltrarDepositos;
	}
	public void setLblResultadoFiltrarDepositos(
			HtmlOutputText lblResultadoFiltrarDepositos) {
		this.lblResultadoFiltrarDepositos = lblResultadoFiltrarDepositos;
	}
	public HtmlGridView getGvResumenConsolidadoDepositos() {
		return gvResumenConsolidadoDepositos;
	}
	public void setGvResumenConsolidadoDepositos(
			HtmlGridView gvResumenConsolidadoDepositos) {
		this.gvResumenConsolidadoDepositos = gvResumenConsolidadoDepositos;
	}
	public List<ResumenDepositosTipoTransaccion> getLtResumenConsolidadoDepositos() {
		return ltResumenConsolidadoDepositos = ConsolidadoDepsBcoDAO.getDatosResumenConsolidadoDepositos();
	}
	public void setLtResumenConsolidadoDepositos(
			List<ResumenDepositosTipoTransaccion> ltResumenConsolidadoDepositos) {
		this.ltResumenConsolidadoDepositos = ltResumenConsolidadoDepositos;
	}
	public HtmlInputText getTxtCodigosFiltroBanco() {
		return txtCodigosFiltroBanco;
	}
	public void setTxtCodigosFiltroBanco(HtmlInputText txtCodigosFiltroBanco) {
		this.txtCodigosFiltroBanco = txtCodigosFiltroBanco;
	}
	public HtmlDialogWindow getDwComparacionDepositos() {
		return dwComparacionDepositos;
	}
	public void setDwComparacionDepositos(HtmlDialogWindow dwComparacionDepositos) {
		this.dwComparacionDepositos = dwComparacionDepositos;
	}
	public HtmlGridView getGvDepositoEnCoincidencia() {
		return gvDepositoEnCoincidencia;
	}
	public void setGvDepositoEnCoincidencia(HtmlGridView gvDepositoEnCoincidencia) {
		this.gvDepositoEnCoincidencia = gvDepositoEnCoincidencia;
	}
	@SuppressWarnings("unchecked")
	public List<ConsolidadoCoincidente> getLstDepositoEnCoincidencia() {
		
		try {
			
			if( FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey(sesmapvarDepsCoincidencias) ){
				lstDepositoEnCoincidencia = (ArrayList<ConsolidadoCoincidente>)CodeUtil.getFromSessionMap( sesmapvarDepsCoincidencias ) ;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(lstDepositoEnCoincidencia == null)
				lstDepositoEnCoincidencia = new ArrayList<ConsolidadoCoincidente>() ;
		}
		return lstDepositoEnCoincidencia;
	}
	public void setLstDepositoEnCoincidencia(
			List<ConsolidadoCoincidente> lstDepositoEnCoincidencia) {
		this.lstDepositoEnCoincidencia = lstDepositoEnCoincidencia;
	}

	public HtmlOutputText getRsmTotalDepBco() {
		return rsmTotalDepBco;
	}

	public void setRsmTotalDepBco(HtmlOutputText rsmTotalDepBco) {
		this.rsmTotalDepBco = rsmTotalDepBco;
	}

	public HtmlOutputText getRsmTotalCoincidenciasBco() {
		return rsmTotalCoincidenciasBco;
	}

	public void setRsmTotalCoincidenciasBco(HtmlOutputText rsmTotalCoincidenciasBco) {
		this.rsmTotalCoincidenciasBco = rsmTotalCoincidenciasBco;
	}

	public HtmlOutputText getRsmTotalDepCaja() {
		return rsmTotalDepCaja;
	}

	public void setRsmTotalDepCaja(HtmlOutputText rsmTotalDepCaja) {
		this.rsmTotalDepCaja = rsmTotalDepCaja;
	}

	public HtmlOutputText getRsmTotalCoincidenciasCaja() {
		return rsmTotalCoincidenciasCaja;
	}
	public void setRsmTotalCoincidenciasCaja(
			HtmlOutputText rsmTotalCoincidenciasCaja) {
		this.rsmTotalCoincidenciasCaja = rsmTotalCoincidenciasCaja;
	}
	public HtmlOutputText getRsmCoincidentesUnoAuno() {
		return rsmCoincidentesUnoAuno;
	}
	public void setRsmCoincidentesUnoAuno(HtmlOutputText rsmCoincidentesUnoAuno) {
		this.rsmCoincidentesUnoAuno = rsmCoincidentesUnoAuno;
	}
	public HtmlOutputText getRsmCoincidentesUnoAMuchos() {
		return rsmCoincidentesUnoAMuchos;
	}
	public void setRsmCoincidentesUnoAMuchos(
			HtmlOutputText rsmCoincidentesUnoAMuchos) {
		this.rsmCoincidentesUnoAMuchos = rsmCoincidentesUnoAMuchos;
	}
	public HtmlInputTextarea getTxtNivelesCompara() {
		return txtNivelesCompara;
	}
	public void setTxtNivelesCompara(HtmlInputTextarea txtNivelesCompara) {
		this.txtNivelesCompara = txtNivelesCompara;
	}
	public String getSesmapvarDepsCoincidencias() {
		return sesmapvarDepsCoincidencias;
	}
	public void setSesmapvarDepsCoincidencias(String sesmapvarDepsCoincidencias) {
		this.sesmapvarDepsCoincidencias = sesmapvarDepsCoincidencias;
	}
	public HtmlDialogWindow getDwExcluirCoincidencia() {
		return dwExcluirCoincidencia;
	}
	public void setDwExcluirCoincidencia(HtmlDialogWindow dwExcluirCoincidencia) {
		this.dwExcluirCoincidencia = dwExcluirCoincidencia;
	}
	public HtmlOutputText getMsgConfirmaExcluirCoincidencia() {
		return msgConfirmaExcluirCoincidencia;
	}
	public void setMsgConfirmaExcluirCoincidencia(
			HtmlOutputText msgConfirmaExcluirCoincidencia) {
		this.msgConfirmaExcluirCoincidencia = msgConfirmaExcluirCoincidencia;
	}
	public HtmlInputTextarea getTxtMotivoExclusionConDeps() {
		return txtMotivoExclusionConDeps;
	}
	public void setTxtMotivoExclusionConDeps(
			HtmlInputTextarea txtMotivoExclusionConDeps) {
		this.txtMotivoExclusionConDeps = txtMotivoExclusionConDeps;
	}
	public HtmlDialogWindow getDwInvocarComparacionDepositos() {
		return dwInvocarComparacionDepositos;
	}
	public void setDwInvocarComparacionDepositos(
			HtmlDialogWindow dwInvocarComparacionDepositos) {
		this.dwInvocarComparacionDepositos = dwInvocarComparacionDepositos;
	}
	public HtmlOutputText getRsmRangoFechasBanco() {
		return rsmRangoFechasBanco;
	}
	public void setRsmRangoFechasBanco(HtmlOutputText rsmRangoFechasBanco) {
		this.rsmRangoFechasBanco = rsmRangoFechasBanco;
	}
	public HtmlOutputText getRsmRangoFechasCaja() {
		return rsmRangoFechasCaja;
	}
	public void setRsmRangoFechasCaja(HtmlOutputText rsmRangoFechasCaja) {
		this.rsmRangoFechasCaja = rsmRangoFechasCaja;
	}

	public HtmlOutputText getTotalCoincidenciaEnConflicto() {
		return totalCoincidenciaEnConflicto;
	}
	public void setTotalCoincidenciaEnConflicto(
			HtmlOutputText totalCoincidenciaEnConflicto) {
		this.totalCoincidenciaEnConflicto = totalCoincidenciaEnConflicto;
	}
	public HtmlOutputText getStrRsmTotalDepBco() {
		return strRsmTotalDepBco;
	}
	public void setStrRsmTotalDepBco(HtmlOutputText strRsmTotalDepBco) {
		this.strRsmTotalDepBco = strRsmTotalDepBco;
	}
	public HtmlOutputText getStrRsmTotalCoincidenciasBco() {
		return strRsmTotalCoincidenciasBco;
	}
	public void setStrRsmTotalCoincidenciasBco(
			HtmlOutputText strRsmTotalCoincidenciasBco) {
		this.strRsmTotalCoincidenciasBco = strRsmTotalCoincidenciasBco;
	}
	public HtmlOutputText getStrRsmTotalDepCaja() {
		return strRsmTotalDepCaja;
	}
	public void setStrRsmTotalDepCaja(HtmlOutputText strRsmTotalDepCaja) {
		this.strRsmTotalDepCaja = strRsmTotalDepCaja;
	}
	public HtmlOutputText getStrRsmTotalCoincidenciasCaja() {
		return strRsmTotalCoincidenciasCaja;
	}
	public void setStrRsmTotalCoincidenciasCaja(
			HtmlOutputText strRsmTotalCoincidenciasCaja) {
		this.strRsmTotalCoincidenciasCaja = strRsmTotalCoincidenciasCaja;
	}
	public HtmlOutputText getStrRsmCoincidentesUnoAuno() {
		return strRsmCoincidentesUnoAuno;
	}
	public void setStrRsmCoincidentesUnoAuno(
			HtmlOutputText strRsmCoincidentesUnoAuno) {
		this.strRsmCoincidentesUnoAuno = strRsmCoincidentesUnoAuno;
	}
	public HtmlOutputText getStrRsmCoincidentesUnoAMuchos() {
		return strRsmCoincidentesUnoAMuchos;
	}
	public void setStrRsmCoincidentesUnoAMuchos(
			HtmlOutputText strRsmCoincidentesUnoAMuchos) {
		this.strRsmCoincidentesUnoAMuchos = strRsmCoincidentesUnoAMuchos;
	}
	public HtmlOutputText getStrTotalCoincidenciaEnConflicto() {
		return strTotalCoincidenciaEnConflicto;
	}
	public void setStrTotalCoincidenciaEnConflicto(
			HtmlOutputText strTotalCoincidenciaEnConflicto) {
		this.strTotalCoincidenciaEnConflicto = strTotalCoincidenciaEnConflicto;
	}
	public HtmlOutputText getStrRsmRangoFechasBanco() {
		return strRsmRangoFechasBanco;
	}
	public void setStrRsmRangoFechasBanco(HtmlOutputText strRsmRangoFechasBanco) {
		this.strRsmRangoFechasBanco = strRsmRangoFechasBanco;
	}
	public HtmlOutputText getStrRsmRangoFechasCaja() {
		return strRsmRangoFechasCaja;
	}
	public void setStrRsmRangoFechasCaja(HtmlOutputText strRsmRangoFechasCaja) {
		this.strRsmRangoFechasCaja = strRsmRangoFechasCaja;
	}
	public HtmlInputTextarea getTxtNivelesComparaValue() {
		return txtNivelesComparaValue;
	}
	public void setTxtNivelesComparaValue(HtmlInputTextarea txtNivelesComparaValue) {
		this.txtNivelesComparaValue = txtNivelesComparaValue;
	}
	@SuppressWarnings("unchecked")
	public List<ConsolidadoCoincidente> getLstDepositoEnCoincidenciaConflicto() {
		
		try {
			if(getFromSessionMap(sesmapvarDepsCoincidenciasConflicto) != null){
				lstDepositoEnCoincidenciaConflicto =  (ArrayList<ConsolidadoCoincidente>)
							getFromSessionMap(sesmapvarDepsCoincidenciasConflicto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(lstDepositoEnCoincidenciaConflicto == null)
				lstDepositoEnCoincidenciaConflicto = new ArrayList<ConsolidadoCoincidente>() ;
		}
		
		return lstDepositoEnCoincidenciaConflicto;
	}
	public void setLstDepositoEnCoincidenciaConflicto(
			List<ConsolidadoCoincidente> lstDepositoEnCoincidenciaConflicto) {
		this.lstDepositoEnCoincidenciaConflicto = lstDepositoEnCoincidenciaConflicto;
	}
	public HtmlLink getLnkValidarConflictos() {
		return lnkValidarConflictos;
	}
	public void setLnkValidarConflictos(HtmlLink lnkValidarConflictos) {
		this.lnkValidarConflictos = lnkValidarConflictos;
	}
	public HtmlDialogWindow getDwValidacionConflictos() {
		return dwValidacionConflictos;
	}
	public void setDwValidacionConflictos(HtmlDialogWindow dwValidacionConflictos) {
		this.dwValidacionConflictos = dwValidacionConflictos;
	}
	public HtmlGridView getGvConflictosCoincidenciaDepositos() {
		return gvConflictosCoincidenciaDepositos;
	}
	public void setGvConflictosCoincidenciaDepositos(
			HtmlGridView gvConflictosCoincidenciaDepositos) {
		this.gvConflictosCoincidenciaDepositos = gvConflictosCoincidenciaDepositos;
	}
	public HtmlDialogWindow getDwMensajesValidacion() {
		return dwMensajesValidacion;
	}
	public void setDwMensajesValidacion(HtmlDialogWindow dwMensajesValidacion) {
		this.dwMensajesValidacion = dwMensajesValidacion;
	}
	public HtmlOutputText getLblMensajeValidacionProcesos() {
		return lblMensajeValidacionProcesos;
	}
	public void setLblMensajeValidacionProcesos(
			HtmlOutputText lblMensajeValidacionProcesos) {
		this.lblMensajeValidacionProcesos = lblMensajeValidacionProcesos;
	}
	public HtmlDialogWindow getDwConfirmarConflictoValido() {
		return dwConfirmarConflictoValido;
	}
	public void setDwConfirmarConflictoValido(
			HtmlDialogWindow dwConfirmarConflictoValido) {
		this.dwConfirmarConflictoValido = dwConfirmarConflictoValido;
	}
	public HtmlOutputText getLblmsgConfirmaConflictoValido() {
		return lblmsgConfirmaConflictoValido;
	}
	public void setLblmsgConfirmaConflictoValido(
			HtmlOutputText lblmsgConfirmaConflictoValido) {
		this.lblmsgConfirmaConflictoValido = lblmsgConfirmaConflictoValido;
	}
	public HtmlDialogWindow getDwConfirmarConflictoNoValido() {
		return dwConfirmarConflictoNoValido;
	}
	public void setDwConfirmarConflictoNoValido(
			HtmlDialogWindow dwConfirmarConflictoNoValido) {
		this.dwConfirmarConflictoNoValido = dwConfirmarConflictoNoValido;
	}
	public HtmlOutputText getLblmsgConfirmaConflictoNoValido() {
		return lblmsgConfirmaConflictoNoValido;
	}
	public void setLblmsgConfirmaConflictoNoValido(
			HtmlOutputText lblmsgConfirmaConflictoNoValido) {
		this.lblmsgConfirmaConflictoNoValido = lblmsgConfirmaConflictoNoValido;
	}
	public HtmlOutputText getMsgValidaResultadoComparacion() {
		return msgValidaResultadoComparacion;
	}
	public void setMsgValidaResultadoComparacion(
			HtmlOutputText msgValidaResultadoComparacion) {
		this.msgValidaResultadoComparacion = msgValidaResultadoComparacion;
	}
	public String getSesmapvarDepsCoincidenciasConflicto() {
		return sesmapvarDepsCoincidenciasConflicto;
	}
	public void setSesmapvarDepsCoincidenciasConflicto(
			String sesmapvarDepsCoincidenciasConflicto) {
		this.sesmapvarDepsCoincidenciasConflicto = sesmapvarDepsCoincidenciasConflicto;
	}
	public HtmlDialogWindow getDwConfirmacionProcesarcoincidencia() {
		return dwConfirmacionProcesarcoincidencia;
	}
	public void setDwConfirmacionProcesarcoincidencia(
			HtmlDialogWindow dwConfirmacionProcesarcoincidencia) {
		this.dwConfirmacionProcesarcoincidencia = dwConfirmacionProcesarcoincidencia;
	}
	public static String getValoriniciallistacombo() {
		return valorInicialListaCombo;
	}
	public HtmlOutputText getLblMsgConfirmacionProcesaCoincidencia() {
		return lblMsgConfirmacionProcesaCoincidencia;
	}
	public void setLblMsgConfirmacionProcesaCoincidencia(
			HtmlOutputText lblMsgConfirmacionProcesaCoincidencia) {
		this.lblMsgConfirmacionProcesaCoincidencia = lblMsgConfirmacionProcesaCoincidencia;
	}
	public HtmlDialogWindow getDwResultadoGeneracionComprobante() {
		return dwResultadoGeneracionComprobante;
	}
	public void setDwResultadoGeneracionComprobante(
			HtmlDialogWindow dwResultadoGeneracionComprobante) {
		this.dwResultadoGeneracionComprobante = dwResultadoGeneracionComprobante;
	}
	public HtmlDialogWindow getDwValidaConfirmaDepositoIndividual() {
		return dwValidaConfirmaDepositoIndividual;
	}
	public void setDwValidaConfirmaDepositoIndividual(
			HtmlDialogWindow dwValidaConfirmaDepositoIndividual) {
		this.dwValidaConfirmaDepositoIndividual = dwValidaConfirmaDepositoIndividual;
	}
	public HtmlOutputText getLblmsgConfirmaComprobanteIndividual() {
		return lblmsgConfirmaComprobanteIndividual;
	}
	public void setLblmsgConfirmaComprobanteIndividual(
			HtmlOutputText lblmsgConfirmaComprobanteIndividual) {
		this.lblmsgConfirmaComprobanteIndividual = lblmsgConfirmaComprobanteIndividual;
	}

	public HtmlDialogWindow getDwResumenCoincidenciasPorNiveles() {
		return dwResumenCoincidenciasPorNiveles;
	}

	public void setDwResumenCoincidenciasPorNiveles(
			HtmlDialogWindow dwResumenCoincidenciasPorNiveles) {
		this.dwResumenCoincidenciasPorNiveles = dwResumenCoincidenciasPorNiveles;
	}

	public HtmlGridView getGvResumenCoincidenciasPorNiveles() {
		return gvResumenCoincidenciasPorNiveles;
	}

	public void setGvResumenCoincidenciasPorNiveles(
			HtmlGridView gvResumenCoincidenciasPorNiveles) {
		this.gvResumenCoincidenciasPorNiveles = gvResumenCoincidenciasPorNiveles;
	}

	@SuppressWarnings("unchecked")
	public List<NivelComparacionDeposito> getLstResumenCoincidenciasPorNiveles() {
		
		if(CodeUtil.getFromSessionMap("pcd_lstResumenCoincidenciasPorNiveles") != null){
			lstResumenCoincidenciasPorNiveles =  (ArrayList<NivelComparacionDeposito>)CodeUtil.getFromSessionMap("pcd_lstResumenCoincidenciasPorNiveles");
		}
		
		return lstResumenCoincidenciasPorNiveles;
	}

	public void setLstResumenCoincidenciasPorNiveles(
			List<NivelComparacionDeposito> lstResumenCoincidenciasPorNiveles) {
		this.lstResumenCoincidenciasPorNiveles = lstResumenCoincidenciasPorNiveles;
	}

	public HtmlOutputText getLblResumenComparacionNivel() {
		return lblResumenComparacionNivel;
	}

	public void setLblResumenComparacionNivel(
			HtmlOutputText lblResumenComparacionNivel) {
		this.lblResumenComparacionNivel = lblResumenComparacionNivel;
	}

	public HtmlDialogWindow getDwProcesarDepositosCoincidentes() {
		return dwProcesarDepositosCoincidentes;
	}

	public void setDwProcesarDepositosCoincidentes(
			HtmlDialogWindow dwProcesarDepositosCoincidentes) {
		this.dwProcesarDepositosCoincidentes = dwProcesarDepositosCoincidentes;
	}

	public HtmlGridView getGvProcesarDepositosCoincidentes() {
		return gvProcesarDepositosCoincidentes;
	}

	public void setGvProcesarDepositosCoincidentes(
			HtmlGridView gvProcesarDepositosCoincidentes) {
		this.gvProcesarDepositosCoincidentes = gvProcesarDepositosCoincidentes;
	}

	@SuppressWarnings("unchecked")
	public List<ConsolidadoCoincidente> getLstProcesarDepositosCoincidentes() {
		
		if(CodeUtil.getFromSessionMap("pcd_lstProcesarDepositosCoincidentes") != null){
			lstProcesarDepositosCoincidentes =  (ArrayList<ConsolidadoCoincidente>)CodeUtil.getFromSessionMap("pcd_lstProcesarDepositosCoincidentes");
		}
		
		return lstProcesarDepositosCoincidentes;
	}

	public void setLstProcesarDepositosCoincidentes(
			List<ConsolidadoCoincidente> lstProcesarDepositosCoincidentes) {
		this.lstProcesarDepositosCoincidentes = lstProcesarDepositosCoincidentes;
	}

	public HtmlDialogWindowHeader getDwHeaderProcesarDepositosCoincidentes() {
		return dwHeaderProcesarDepositosCoincidentes;
	}

	public void setDwHeaderProcesarDepositosCoincidentes(
			HtmlDialogWindowHeader dwHeaderProcesarDepositosCoincidentes) {
		this.dwHeaderProcesarDepositosCoincidentes = dwHeaderProcesarDepositosCoincidentes;
	}

	public HtmlDialogWindow getDwSometerCoincidenciaAExcepcion() {
		return dwSometerCoincidenciaAExcepcion;
	}

	public void setDwSometerCoincidenciaAExcepcion(
			HtmlDialogWindow dwSometerCoincidenciaAExcepcion) {
		this.dwSometerCoincidenciaAExcepcion = dwSometerCoincidenciaAExcepcion;
	}

	public HtmlInputTextarea getTxtMotivoSometeExcepcion() {
		return txtMotivoSometeExcepcion;
	}

	public void setTxtMotivoSometeExcepcion(
			HtmlInputTextarea txtMotivoSometeExcepcion) {
		this.txtMotivoSometeExcepcion = txtMotivoSometeExcepcion;
	}

	public HtmlCheckBox getChkExcluirBanco() {
		return chkExcluirBanco;
	}

	public void setChkExcluirBanco(HtmlCheckBox chkExcluirBanco) {
		this.chkExcluirBanco = chkExcluirBanco;
	}

	public HtmlCheckBox getChkExcluirCaja() {
		return chkExcluirCaja;
	}

	public void setChkExcluirCaja(HtmlCheckBox chkExcluirCaja) {
		this.chkExcluirCaja = chkExcluirCaja;
	}

	public HtmlDialogWindow getDwProcesarEnAjustePorExcepcion() {
		return dwProcesarEnAjustePorExcepcion;
	}

	public void setDwProcesarEnAjustePorExcepcion(
			HtmlDialogWindow dwProcesarEnAjustePorExcepcion) {
		this.dwProcesarEnAjustePorExcepcion = dwProcesarEnAjustePorExcepcion;
	}

	public HtmlDropDownList getCmbCuentaAjustePorExcepcion() {
		return cmbCuentaAjustePorExcepcion;
	}

	public void setCmbCuentaAjustePorExcepcion(
			HtmlDropDownList cmbCuentaAjustePorExcepcion) {
		this.cmbCuentaAjustePorExcepcion = cmbCuentaAjustePorExcepcion;
	}

	@SuppressWarnings("unchecked")
	public List<SelectItem> getLstCuentaAjustePorExcepcion() {
		
		lstCuentaAjustePorExcepcion = (ArrayList<SelectItem>)CodeUtil.getFromSessionMap( "pcd_lstCuentaAjustePorExcepcion" ) ;
		
		if(lstCuentaAjustePorExcepcion == null)
			lstCuentaAjustePorExcepcion = new ArrayList<SelectItem>();
		
		return lstCuentaAjustePorExcepcion;
	}

	public void setLstCuentaAjustePorExcepcion(
			List<SelectItem> lstCuentaAjustePorExcepcion) {
		this.lstCuentaAjustePorExcepcion = lstCuentaAjustePorExcepcion;
	}

	public HtmlInputTextarea getTxtMotivoProcesarAjustePorExcepcion() {
		return txtMotivoProcesarAjustePorExcepcion;
	}

	public void setTxtMotivoProcesarAjustePorExcepcion(
			HtmlInputTextarea txtMotivoProcesarAjustePorExcepcion) {
		this.txtMotivoProcesarAjustePorExcepcion = txtMotivoProcesarAjustePorExcepcion;
	}

	@SuppressWarnings("unchecked")
	public String getHdrColmCoincidenciaMtoBanco() {
		
		try {

			if( ( hdrColmCoincidenciaMtoBanco == null || hdrColmCoincidenciaMtoBanco.isEmpty() )  &&  
				CodeUtil.getFromSessionMap("pcd_lstProcesarDepositosCoincidentes") != null ){
				lstProcesarDepositosCoincidentes =  (ArrayList<ConsolidadoCoincidente>)CodeUtil.getFromSessionMap("pcd_lstProcesarDepositosCoincidentes");
				hdrColmCoincidenciaMtoBanco = ( lstProcesarDepositosCoincidentes.get(0).isMultipleBancoPorUnoCaja() ) ? "Caja" : "Banco";
			} 
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(hdrColmCoincidenciaMtoBanco == null)
				hdrColmCoincidenciaMtoBanco = "";
		}
		
		return hdrColmCoincidenciaMtoBanco;
	}

	public void setHdrColmCoincidenciaMtoBanco(String hdrColmCoincidenciaMtoBanco) {
		this.hdrColmCoincidenciaMtoBanco = hdrColmCoincidenciaMtoBanco;
	}
	
	@SuppressWarnings("unchecked")
	public String getHdrColmCoincidenciaMtoCaja() {
		
		try {

			if( (hdrColmCoincidenciaMtoCaja == null || hdrColmCoincidenciaMtoCaja.isEmpty() ) &&  
				CodeUtil.getFromSessionMap("pcd_lstProcesarDepositosCoincidentes") != null ){
				lstProcesarDepositosCoincidentes =  (ArrayList<ConsolidadoCoincidente>)CodeUtil.getFromSessionMap("pcd_lstProcesarDepositosCoincidentes");
				hdrColmCoincidenciaMtoCaja = (lstProcesarDepositosCoincidentes.get(0).isMultipleBancoPorUnoCaja() ) ? "Banco" : "Caja";
			} 
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(hdrColmCoincidenciaMtoCaja == null)
				hdrColmCoincidenciaMtoCaja = "";
		}
		
		return hdrColmCoincidenciaMtoCaja;
	}

	public void setHdrColmCoincidenciaMtoCaja(String hdrColmCoincidenciaMtoCaja) {
		this.hdrColmCoincidenciaMtoCaja = hdrColmCoincidenciaMtoCaja;
	}

	public HtmlDropDownList getCmbCuentasBancoDisponibles() {
		return cmbCuentasBancoDisponibles;
	}

	public void setCmbCuentasBancoDisponibles(
			HtmlDropDownList cmbCuentasBancoDisponibles) {
		this.cmbCuentasBancoDisponibles = cmbCuentasBancoDisponibles;
	}

	@SuppressWarnings("unchecked")
	public List<SelectItem> getLstCuentasBancoDisponibles() {
		
		try {
			
			if(CodeUtil.getFromSessionMap("pcd_lstCuentasBancoDisponibles") != null )
				return lstCuentasBancoDisponibles = ( List<SelectItem> ) CodeUtil.getFromSessionMap("pcd_lstCuentasBancoDisponibles") ;
			
			lstCuentasBancoDisponibles = new ArrayList<SelectItem>();
			
			F55ca022[] bancosConcilia = BancoCtrl.obtenerBancosConciliar();
			for (F55ca022 f22 : bancosConcilia) {
				
				String nombre = f22.getId().getBanco().trim().split(" ")[0];
				nombre  = (nombre.length() > 10 ) ? nombre.substring(0,10) : nombre;
				
				List<F55ca023> dtaBco =  ConfirmaDepositosCtrl.obtenerF55ca023xBanco( f22.getId().getCodb() ) ;
				
				for (F55ca023 f23 : dtaBco) {
					lstCuentasBancoDisponibles.add( 
						new SelectItem( f23.getId().getD3codb() +"@"+ f23.getId().getD3nocuenta()+"@"+f23.getId().getD3crcd(),  
							f23.getId().getD3nocuenta() + " " + f23.getId().getD3crcd() + " " + nombre, 
							f23.getId().getD3mcu().trim()+"."+f23.getId().getD3obj().trim()+"."+f23.getId().getD3sub().trim() )
					);
				}
			}
			CodeUtil.putInSessionMap("pcd_lstCuentasBancoDisponibles", lstCuentasBancoDisponibles) ;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return lstCuentasBancoDisponibles;
	}

	public void setLstCuentasBancoDisponibles(
			List<SelectItem> lstCuentasBancoDisponibles) {
		this.lstCuentasBancoDisponibles = lstCuentasBancoDisponibles;
	}
	
}

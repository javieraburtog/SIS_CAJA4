package com.casapellas.conciliacion;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import com.casapellas.conciliacion.entidades.Ajusteconc;
import com.casapellas.conciliacion.entidades.Conciliacion;
import com.casapellas.conciliacion.entidades.Vwconciliacion;
import com.casapellas.controles.BancoCtrl;
import com.casapellas.controles.CompaniaCtrl;
import com.casapellas.controles.ConfirmaDepositosCtrl;
import com.casapellas.controles.CtrlCajas;
import com.casapellas.controles.EmpleadoCtrl;

import com.casapellas.controles.MonedaCtrl;
import com.casapellas.controles.ReciboCtrl;
import com.casapellas.entidades.F55ca014;
import com.casapellas.entidades.F55ca022;
import com.casapellas.entidades.F55ca023;
import com.casapellas.entidades.Vcompania;
import com.casapellas.entidades.Vdeposito;
import com.casapellas.entidades.Vf55ca01;
import com.casapellas.entidades.ens.Vautoriz;
import com.casapellas.util.Divisas;
import com.infragistics.faces.grid.component.RowItem;
import com.infragistics.faces.grid.component.html.HtmlGridView;
import com.infragistics.faces.input.component.html.HtmlDateChooser;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
import com.infragistics.faces.window.component.html.HtmlDialogWindow;
import com.infragistics.faces.window.component.html.HtmlDialogWindowHeader;


/**
 * CASA PELLAS S.A.
 * Creado por.........: Carlos Manuel Hernández Morrison
 * Fecha de Creación..: 29/09/2011
 * Última modificación: Carlos Manuel Hernández Morrison
 * Modificado por.....:	29/09/2010
 * Descripción:.......: Manejo de consultas de confirmacion de 
 * 						depositos y reversion de confirmaciones 
 */
@SuppressWarnings({"unchecked", "static-access"})
public class ConsultaConfirmacionDep {
	Map<String, Object> m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	private List<Vwconciliacion> lstConfirmacionesRegistradas;
	private HtmlGridView gvConfirmacionesRegistradas;
	private HtmlDialogWindow dwDetalleConfirmacionDeposito; 
	private HtmlOutputText lblDtCdNombreArchivo, lblDtCdMontoDepsBco;
	private HtmlOutputText lblDtCdReferencia, lblDtCdBanco;
	private HtmlOutputText lblDtCdCuenta,lblDtCdDescripcion;
	private HtmlOutputText lblDtCdTipoConfirma,lblDtCdFechaConfirma;
	private HtmlOutputText lblDtCdNoBatch,lblDtCdUsuario;
	private HtmlOutputText lblDtCdAjustes,lblDtCdDocumento;
	private List<Vdeposito> lstDtConDepositosCaja;
	private HtmlGridView gvDtConDepositosCaja;
	
	private HtmlDialogWindow dwFiltrarDepsCaja;	
	private List<SelectItem> lstfcCajas,lstfcCompania,lstfcSucursal,lstfcMoneda;
	private HtmlDropDownList ddlfcCajas,ddlfcCompania,ddlfcSucursal,ddlfcMoneda;
	private HtmlDateChooser txtfcFechaIni,txtfcFechaFin;
	private HtmlInputText txtfcNoReferencia,txtfcMontoDep;
	private HtmlOutputText lblMsgResultBusquedaDepCaja;
	private List<SelectItem> lstfbBancos, lstfbCuentaxBanco;
	private HtmlDropDownList ddlfbBancos,ddlfbCuentaxBanco;
	private HtmlInputText txtfbEtMontoBanco, txtfbReferenciaDep;
	private HtmlDateChooser	txtfbFechaIni, txtfbFechaFin;
	private HtmlDateChooser txtfcnFechaIni,txtfcnFechaFin;
	private HtmlDropDownList ddlDetCfFcRangoMonto;
	private List<SelectItem> lstDetCfFcRangoMonto;
	private HtmlDialogWindow dwNotifErrorConsultaConfirma, dwValidaReversionConfirmacion;	
	private HtmlOutputText lblMsgNotErrorConsultaConfirma, lblMsgConfirmacionReversion;
	private HtmlDialogWindowHeader hdrDwNotErrorConsultaConfirma;
	private HtmlOutputText lblEtNuevaReferBco,lblMsgErrorUpdReferBco;
	private HtmlInputText txtCmNuevaReferBanco;

/******************************************************************************************/
/** Método: Realiza validaciones para verificar que la confirmacion puede revertirse. 
 *	Fecha:  04/10/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public void mostrarConfirmacionReversionDeps(ActionEvent ev){
		Vwconciliacion vwconcilia = null;
		try {
			hdrDwNotErrorConsultaConfirma.setCaptionText("Reversión de confirmación de depósitos");
			dwNotifErrorConsultaConfirma.setWindowState("normal");
			lblMsgErrorUpdReferBco.setStyle("display:none");
			
			String sMensaje = "";
			RowItem ri = (RowItem)ev.getComponent().getParent().getParent();
			vwconcilia = (Vwconciliacion)gvConfirmacionesRegistradas.getDataRow(ri);
			m.put("ccd_VwConciliaSeleccionado", vwconcilia);
			m.remove("ccd_HacerReversion");
			
			//&& ======== Conciliacion inactiva o anulada.
			if(vwconcilia.getId().getEstado() == 47){
				lblMsgNotErrorConsultaConfirma
					.setValue("La confirmacion para el depósito # "+vwconcilia.getId().getNoreferencia() +" ya se encuentra anulada ");
				return;
			}
			//&& ======== Obtener la informacion del periodo fiscal del batch.
			int[] iAnioMesPerFiscal = new ReciboCtrl()
											.obtenerPeriodoMesFiscalBatch
												(vwconcilia.getId().getNobatch(),vwconcilia.getId().getNoreferencia());
			if(iAnioMesPerFiscal == null){
				lblMsgNotErrorConsultaConfirma
				.setValue("No se pudo obtener informacion del periodo anio/mes fiscal para el batch # "+vwconcilia.getId().getNobatch() );
				return;
			}
			
			List<Vdeposito> lstDepsCaja = new ConfirmaDepositosCtrl()
								.obtenerDepositosCajaConfirmacion(
									vwconcilia.getId().getIdconciliacion());
			
			int[]periodoactivo = ConfirmaDepositosCtrl.obtenerPeriodoContableActual(null, 
										lstDepsCaja.get(0).getId().getCodsuc());
			int iMesFiscalActual = periodoactivo[0];
			int iAnioFiscalActual = periodoactivo[1];
			
			iAnioFiscalActual  = Integer.parseInt( String.valueOf(iAnioFiscalActual).substring(1,3) );
			
			if( iAnioFiscalActual != iAnioMesPerFiscal[0] || iMesFiscalActual != iAnioMesPerFiscal[1]  ){
				sMensaje = "El batch "+vwconcilia.getId().getNobatch()+ " pertenece a un periodo fiscal distinto al actual ";
			}
			
			//&& ======== Verificacion de que el batch se encuentre contabilizado.
			String sEstado = "" ;
			/*String sEstado = new ReciboCtrl().leerEstadoBatch(null,vwconcilia.getId().getNobatch(),"G");
			if(sEstado==null){
				lblMsgNotErrorConsultaConfirma
					.setValue("No se pudo obtener el estado del batch # "+vwconcilia.getId().getNobatch() +" para la confirmación " );
				return;
			}
			*/
			//&& ===== Mensaje de solicitud de confirmacion
			sMensaje = " El batch de confirmación # "+vwconcilia.getId().getNobatch() ; 
			if(sEstado.compareToIgnoreCase("D") == 0){
				m.put("ccd_HacerReversion", "1");
				sMensaje+= " está contabilizado, ";
			}
			if( iAnioFiscalActual != iAnioMesPerFiscal[0] || iMesFiscalActual != iAnioMesPerFiscal[1]  ){
				m.put("ccd_HacerReversion", "1");
				sMensaje += " pertenece a un periodo fiscal distinto al actual.";
			}
			if(m.get("ccd_HacerReversion") != null ){
				lblEtNuevaReferBco.setStyle("display:inline");
				txtCmNuevaReferBanco.setStyle("height: 11px; width: 115px; text-align: right; display:inline");
				txtCmNuevaReferBanco.setValue("");
				dwValidaReversionConfirmacion.setStyle("height: 250px; width: 390px");
				sMensaje += "<br>Confirma la reversión del depósito # "+vwconcilia.getId().getNoreferencia() ;
				sMensaje += " por " +String.format("%1$,.2f", vwconcilia.getId().getMonto()) +" ?";
				
			}else{
				lblEtNuevaReferBco.setStyle("display:none");
				txtCmNuevaReferBanco.setStyle("height: 11px; width: 115px; text-align: right; display:none");
				txtCmNuevaReferBanco.setValue("");
				dwValidaReversionConfirmacion.setStyle("height: 200px; width: 365px");
				
				sMensaje += " por "+String.format("%1$,.2f", vwconcilia.getId().getMonto());
				sMensaje += " sera borrado permanentemente. ¿desea continuar? ";
			}
			dwValidaReversionConfirmacion.setWindowState("normal");
			lblMsgConfirmacionReversion.setValue(sMensaje);
			dwNotifErrorConsultaConfirma.setWindowState("hidden");
			
		} catch (Exception e) {
			e.printStackTrace();
			hdrDwNotErrorConsultaConfirma.setCaptionText("Reversión de confirmación de depósitos");
			lblMsgNotErrorConsultaConfirma
				.setValue("Error de sistema al intentar validar la reversión del deposito "+vwconcilia.getId().getNoreferencia());
			dwNotifErrorConsultaConfirma.setWindowState("hidden");
		}
	}
/******************************************************************************************/
/** Método: Proceso de reversion de una confirmacion de depositos. 
 *	Fecha:  04/10/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */	
	public void revertirConfirmacionDepositos(ActionEvent ev ){
	
		boolean bHecho = true;
		String sMensaje = "";
		String captiontext = "Éxito en Reversión para confirmación de depósito"; 
	
		
		try {
			
			ProcesoConfirmacion pcd = new ProcesoConfirmacion();
			ConfirmaDepositosCtrl ccd = new ConfirmaDepositosCtrl();
			
			Vwconciliacion vwConcilia = (Vwconciliacion)m.get("ccd_VwConciliaSeleccionado");
			Vautoriz vaut = ((Vautoriz[]) m.get("sevAut"))[0];
			String sTipoEmpleado = new EmpleadoCtrl().determinarTipoCliente(vaut.getId().getCodreg());
			String sNuevaRefer = txtCmNuevaReferBanco.getValue().toString().trim();
			int iNuevaRefer = vwConcilia.getId().getNoreferencia();
			final int idconciliacion = vwConcilia.getId().getIdconciliacion() ;
			
			
			//&& ===== Validar el nuevo numero de documento a utilizar.
			if(m.get("ccd_HacerReversion") != null ){
				if(!sNuevaRefer.matches("^[0-9]{1,8}$")){
					lblMsgErrorUpdReferBco.setStyle("display:inline");
					lblMsgErrorUpdReferBco.setValue("Valor ingresado no válido");
					return;
				}
				iNuevaRefer = Integer.valueOf(sNuevaRefer);	
			}
			
			//&& ===== continua la anulacion/borrado del asiento de diario.
			bHecho = pcd.revertirConfirmacionDepositos(idconciliacion, vaut, sTipoEmpleado, new Date(), iNuevaRefer );
			
			if(!bHecho){
				sMensaje = "No se ha realizado la reversión del depósito " 
							+ vwConcilia.getId().getNoreferencia() +": Mensaje:<br> "
							+ pcd.getError().toString().split("@")[1];
				captiontext = "Fallo en Reversión para confirmación de depósito";
				return;
			}
			
			if( m.get("ccd_HacerReversion") == null){
				sMensaje = "Se ha realizado correctamente la anulación de asiento de diario para la confirmación ";
			}else{
				sMensaje = "Se ha realizado correctamente la reversión de asiento de diario para la confirmación " +
				 vwConcilia.getId().getNoreferencia() + "<br> Batch Utilizado "+ String.valueOf(m.get("ccd_Nobatch"));
			}
			
			//&& ======= Actualizar el grid principal.
			lstConfirmacionesRegistradas = (ArrayList<Vwconciliacion>)m.get("ccd_lstConfirmacionesRegistradas");
			CollectionUtils.filter(lstConfirmacionesRegistradas, new Predicate(){
				 
				public boolean evaluate(Object o) {
					return !(idconciliacion  == ( (Vwconciliacion)o ).getId().getIdconciliacion() );
				}
			});
			
			m.put("ccd_lstConfirmacionesRegistradas", lstConfirmacionesRegistradas);
			gvConfirmacionesRegistradas.dataBind();
			
			//&& ====== Enviar correo de registro de nuevo asiento de diario por reversion.
			int iTipo = 3;
			
			if(m.get("ccd_HacerReversion") != null){
				int[]iNobatchNodoc = (int[])(m.get("ccd_NoBatchNodoc"));
				
				vwConcilia.getId().setRnobatch(iNobatchNodoc[0]);
				vwConcilia.getId().setRnodoc(iNobatchNodoc[1]);
				
				iTipo = 4;
			}
			
			Conciliacion cn = Conciliacion.conciliacionFromVwConciliacion( vwConcilia ) ;
			List<Conciliacion>lstConcilia = new ArrayList<Conciliacion>(1);				
			lstConcilia.add( cn );
			
			new ConfirmaDepositosCtrl().bEnviaCorreoBatch(lstConcilia, vaut.getId().getCodreg() , iTipo);
			
		} catch (Exception e) {
			sMensaje = " preconciliacion no pudo ser revertida " ;
			
			e.printStackTrace();
	
		}finally{
			
			m.remove("ccd_VwConciliaSeleccionado");
			m.remove("ccd_NoBatchNodoc");
			m.remove("ccd_HacerReversion");
			
			hdrDwNotErrorConsultaConfirma.setCaptionText(captiontext);
			lblMsgNotErrorConsultaConfirma.setValue(sMensaje);
			dwNotifErrorConsultaConfirma.setWindowState("normal");
			dwValidaReversionConfirmacion.setWindowState("hidden");
			
		}
	}
	
/******************************************************************************************/
/** Método: Realizar la busqueda de depositos. 
 *	Fecha:  04/10/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */	
	public void buscarConfirmacionDepositos(ActionEvent ev){
		try {
			int iRangoBanco = 1;
			String sMoneda;
			String sIdBanco, sIdCuenta;
			String sIdCaja, sCodcomp;
			String sMontoBanco, sReferBanco;
			String sMontoCaja, sReferCaja;
			BigDecimal bdMontoBanco = new BigDecimal("0");
			BigDecimal bdMontoCaja = new BigDecimal("0");
			Date dtCajaFechaIni = null,  dtCajaFechaFin = null;
			Date dtBancoFechaIni  = null, dtBancoFechaFin  = null;
			Date dtConfirFechaIni  = null, dtConfirFechaFin  = null;
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			
			//&& ====== Filtros de fecha de depositos de caja
			if(txtfcFechaIni.getValue()!=null)
				dtCajaFechaIni = sdf.parse(sdf.format((Date)txtfcFechaIni.getValue()));
			if(txtfcFechaFin.getValue()!=null)
				dtCajaFechaFin = sdf.parse(sdf.format((Date)txtfcFechaFin.getValue()));
			if(dtCajaFechaIni != null && dtCajaFechaFin != null && dtCajaFechaIni.compareTo(dtCajaFechaFin) > 0 ){
				dtCajaFechaFin = sdf.parse(sdf.format((Date)txtfcFechaIni.getValue()));
				dtCajaFechaIni = sdf.parse(sdf.format((Date)txtfcFechaFin.getValue()));
				txtfcFechaIni.setValue(dtCajaFechaIni);
				txtfcFechaFin.setValue(dtCajaFechaFin);
			}
			//&& ====== Filtros de fecha de depositos banco.
			if(txtfbFechaIni.getValue()!=null)
				dtBancoFechaIni = sdf.parse(sdf.format((Date)txtfbFechaIni.getValue()));
			if(txtfbFechaFin.getValue()!=null)
				dtBancoFechaFin = sdf.parse(sdf.format((Date)txtfbFechaFin.getValue()));
			if(dtBancoFechaIni != null && dtBancoFechaFin != null && dtBancoFechaIni.compareTo(dtBancoFechaFin) >0 ){
				dtBancoFechaFin = sdf.parse(sdf.format((Date)txtfbFechaIni.getValue()));
				dtBancoFechaIni = sdf.parse(sdf.format((Date)txtfbFechaFin.getValue()));
				txtfbFechaIni.setValue(dtBancoFechaIni);
				txtfbFechaFin.setValue(dtBancoFechaFin);
			}
			//&& ====== Filtros de fecha de depositos banco.
			if(txtfcnFechaIni.getValue() != null)
				dtConfirFechaIni = sdf.parse(sdf.format((Date)txtfcnFechaIni.getValue()));
			if(txtfcnFechaFin.getValue() != null)
				dtConfirFechaFin = sdf.parse(sdf.format((Date)txtfcnFechaFin.getValue()));
			if(dtConfirFechaIni != null && dtConfirFechaFin != null && dtConfirFechaFin.compareTo(dtConfirFechaFin) >0 ){
				dtConfirFechaIni = sdf.parse(sdf.format((Date)txtfcnFechaFin.getValue()));
				dtConfirFechaFin = sdf.parse(sdf.format((Date)txtfcnFechaIni.getValue()));
				txtfcnFechaIni.setValue(dtConfirFechaIni);
				txtfcnFechaFin.setValue(dtConfirFechaFin);
			}
			
			//&& ====== Parametros de los filtros.
			sIdBanco  = ddlfbBancos.getValue().toString();
			sIdCuenta = ddlfbCuentaxBanco.getValue().toString();
			sIdCaja   = ddlfcCajas.getValue().toString();
			sCodcomp  = ddlfcCompania.getValue().toString();
			sMontoBanco = txtfbEtMontoBanco.getValue().toString();
			sReferBanco = txtfbReferenciaDep.getValue().toString();
			sMontoCaja  = txtfcMontoDep.getValue().toString();
			sReferCaja  = txtfcNoReferencia.getValue().toString();
			sMoneda	    = ddlfcMoneda.getValue().toString();
			iRangoBanco	= Integer.parseInt(ddlDetCfFcRangoMonto.getValue().toString().split("@")[0]);
			
			if(sIdBanco.compareTo("SB")== 0)
				sIdBanco = "";
			if(sIdCuenta.compareTo("SCTA")== 0)
				sIdCuenta = "";
			if(sIdCaja.compareTo("NC")== 0)
				sIdCaja = "";
			if(sCodcomp.compareTo("NCP")== 0)
				sCodcomp = "";
			if(sMoneda.compareTo("NMND")== 0)
				sMoneda = "";
			if(sMontoBanco.matches("^[0-9]+\\.?[0-9]*$"))
				bdMontoBanco = new BigDecimal(sMontoBanco);
			if(sMontoCaja.matches("^[0-9]+\\.?[0-9]*$"))
				bdMontoCaja = new BigDecimal(sMontoCaja);
			
			ConfirmaDepositosCtrl cdc = new ConfirmaDepositosCtrl();
			List<Vwconciliacion>lstConfirmaciones = cdc.buscarConfirmacionDepositos(sIdBanco, sIdCuenta, sIdCaja, 
														sCodcomp, bdMontoBanco, sReferBanco, bdMontoCaja,
														sReferCaja, sMoneda, dtCajaFechaIni, dtCajaFechaFin, 
														dtBancoFechaIni, dtBancoFechaFin, iRangoBanco,
														dtConfirFechaIni, dtConfirFechaFin);
			
			if(lstConfirmaciones == null)
				lstConfirmaciones = new ArrayList<Vwconciliacion>();
			
			m.put("ccd_lstConfirmacionesRegistradas", lstConfirmaciones);
			gvConfirmacionesRegistradas.dataBind();
			lblMsgResultBusquedaDepCaja.setValue(lstConfirmaciones.size() +" Registros coincidentes");
			lblMsgResultBusquedaDepCaja.setStyle(  (lstConfirmaciones.size()> 0)? "color: green":" color red" );
			
		} catch (Exception e) {
			lblMsgResultBusquedaDepCaja.setStyle("color:red");
			lblMsgResultBusquedaDepCaja.setValue("Error de sistema al realizar busqueda de confirmaciones ");
			e.printStackTrace();
		}
	}
/******************************************************************************************/
/** Método: Buscar las cuentas de banco asociadas al banco para conciliar. 
 *	Fecha:  04/10/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */	
	public void cargarCuentasDeBanco(ValueChangeEvent ev){
		List<F55ca023>lstCuentasxBanco = null;
		int iCodigoBanco = 0;
		
		try {
			if(ddlfbBancos.getValue().toString().equals("SB")){
				m.remove("cdb_lstfbCuentaxBanco");
				ddlfbCuentaxBanco.dataBind();
				return;
			}
			iCodigoBanco = Integer.parseInt(ddlfbBancos.getValue().toString());
			lstCuentasxBanco = new ConfirmaDepositosCtrl().obtenerF55ca023xBanco(iCodigoBanco);
			if(lstCuentasxBanco==null || lstCuentasxBanco.size()==0){
				m.remove("cdb_lstfbCuentaxBanco");
				lblMsgResultBusquedaDepCaja.setStyle("color:red");
				lblMsgResultBusquedaDepCaja.setValue("No se encontraron números de cuenta asociadas al banco");
			}else{
				lstfbCuentaxBanco = new ArrayList<SelectItem>(lstCuentasxBanco.size()+1);
				lstfbCuentaxBanco.add(new SelectItem("SCTA","Cuenta","Selección el banco para las cuentas"));
				for (F55ca023 f023 : lstCuentasxBanco) {
					SelectItem si = new SelectItem();
					si.setValue(f023.getId().getD3nocuenta()+"");
					si.setLabel(f023.getId().getD3nocuenta()+"");
					si.setDescription(f023.getId().getD3crcd()+" "+f023.getId().getD3mcu().trim()+
									"."+f023.getId().getD3obj().trim()+"."+f023.getId().getD3sub());
					lstfbCuentaxBanco.add(si);
				}
				m.put("ccd_lstfbCuentaxBanco",lstfbCuentaxBanco);
				ddlfbCuentaxBanco.dataBind();
			}
		} catch (Exception e) {
			lblMsgResultBusquedaDepCaja.setStyle("color:red");
			lblMsgResultBusquedaDepCaja.setValue("Error de sistema al realizar obtener cuentas de banco "+e.getMessage());
			e.printStackTrace();
		}
	}
/******************************************************************************************/
/** Método: Cargar las companias asociadas a la caja seleccionada.
 *	Fecha:  04/10/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */	
	public void cargarCompaniasxCaja(ValueChangeEvent ev){
		try {
			String sCaid = ddlfcCajas.getValue().toString();
			lstfcCompania = new ArrayList<SelectItem>();
			lstfcCompania.add(new SelectItem("NCP","Compañía","Seleccione la compañía a utilizar en filtros"));
			if(!sCaid.equals("NC")){
				F55ca014 f14[] = new CompaniaCtrl().obtenerCompaniasxCaja(Integer.parseInt(sCaid));
				if(f14!=null){
					for (F55ca014 comp : f14)
						lstfcCompania.add(new SelectItem(comp.getId().getC4rp01().trim(),
										comp.getId().getC4rp01().trim()+" "+comp.getId().getC4rp01d1().trim(),
										comp.getId().getC4rp01().trim()+" "+comp.getId().getC4rp01d1().trim()));
				}
			}else{
				List<Vcompania>lstCompanias = new CompaniaCtrl().obtenerCompaniasCajaJDE();
				if(lstCompanias!=null){
					for (Vcompania vc : lstCompanias) {
						lstfcCompania.add(new SelectItem(vc.getId().getDrky().trim(),
											vc.getId().getDrky().trim()+" "+vc.getId().getDrdl01().trim(),
											vc.getId().getDrky().trim()+" "+vc.getId().getDrdl01().trim()));
					}
				}
			}
			m.put("ccd_lstfcCompania", lstfcCompania);
			ddlfcCompania.dataBind();
		} catch (Exception e) {
			lstfcCompania = new ArrayList<SelectItem>();
			lstfcCompania.add(new SelectItem("NCP","Compañía","Seleccione la compañía a utilizar en filtros"));
			m.put("ccd_lstfcCompania",lstfcCompania);
			ddlfcCompania.dataBind();
			lblMsgResultBusquedaDepCaja.setStyle("color:red");
			lblMsgResultBusquedaDepCaja.setValue("Error de sistema al obtener las companias asociadas a la caja");
			e.printStackTrace();
		}
	}
/******************************************************************************************/
/** Método: Mostrar la ventana para seleccionar filtros de conciliaciones por depositos caja.
 *	Fecha:  04/10/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */	
	public void mostrarBusquedaDepsCaja(ActionEvent ev){
		try {
			//&& ====== Obtener los bancos disponibles.
			lstfbBancos = new ArrayList<SelectItem>();
			lstfbBancos.add(new SelectItem("SB","Banco","Selección de banco"));
			F55ca022[] banco = new BancoCtrl().obtenerBancosConciliar();
			if(banco!=null){
				for (F55ca022 bco : banco) 
					lstfbBancos.add(new SelectItem(String.valueOf(bco.getId().getCodb()),
								bco.getId().getBanco(),	String.valueOf(bco.getId().getCodb()) +": "+bco.getId().getBanco()));
			}
			//&& ====== mostrar cuenta inicial.
			lstfbCuentaxBanco = new ArrayList<SelectItem>();
			lstfbCuentaxBanco.add(new SelectItem("SCTA","Cuenta","Selección el banco para las cuentas"));
			
			//&& ====== mostrar las cajas configuradas.
			lstfcCajas = new ArrayList<SelectItem>();
			lstfcCajas.add(new SelectItem("NC","Cajas","Seleccione la caja a utilizar en filtro"));
			List<Vf55ca01>lstCajas = new CtrlCajas().getAllCajas();
			if(lstCajas!=null){
				for (Vf55ca01 f5 : lstCajas) {
					lstfcCajas.add(new SelectItem(f5.getId().getCaid()+"",
								  f5.getId().getCaid()+" "+ f5.getId().getCaname().trim().toLowerCase(),
								  f5.getId().getCaid()+" "+ f5.getId().getCaname().trim().toLowerCase()));
				}
			}
			//&& ====== Obtener las companias.
			lstfcCompania = new ArrayList<SelectItem>();
			lstfcCompania.add(new SelectItem("NCP","Compañía","Seleccione la compañía a utilizar en filtros"));
			List<Vcompania>lstCompanias = new CompaniaCtrl().obtenerCompaniasCajaJDE();
			if(lstCompanias!=null){
				for (Vcompania vc : lstCompanias) {
					lstfcCompania.add(new SelectItem(vc.getId().getDrky().trim(),
										vc.getId().getDrky().trim()+" "+vc.getId().getDrdl01().trim(),
										vc.getId().getDrky().trim()+" "+vc.getId().getDrdl01().trim()));
				}
			}
			//&& ====== cargar la lista de monedas registradas.
			lstfcMoneda = new ArrayList<SelectItem>();
			lstfcMoneda.add(new SelectItem("NMND","Moneda","Seleccione la Moneda a utilizar en filtros"));
			List<String[]>lstMonedas = new MonedaCtrl().obtenerMonedasJDE();
			if(lstMonedas!=null){
				for (String[] moneda : lstMonedas) {
					lstfcMoneda.add(new SelectItem(moneda[0],moneda[0],moneda[0]+" "+moneda[1]));
				}
			}
			
			m.put("ccd_lstfbBancos",lstfbBancos );
			m.put("ccd_lstfbCuentaxBanco",lstfbCuentaxBanco);
			m.put("ccd_lstfcCajas", lstfcCajas);
			m.put("ccd_lstfcCompania", lstfcCompania);
			m.put("ccd_lstfcMoneda", lstfcMoneda);
			
			ddlfbBancos.dataBind();
			ddlfbCuentaxBanco.dataBind();
			ddlfcCajas.dataBind();
			ddlfcCompania.dataBind();
			ddlfcMoneda.dataBind();
			
			txtfbFechaIni.setValue(new Date());
			txtfbFechaFin.setValue(new Date());
			txtfcFechaIni.setValue(new Date());
			txtfcFechaFin.setValue(new Date());
			txtfcnFechaIni.setValue(new Date());
			txtfcnFechaFin.setValue(new Date());
			
			txtfcNoReferencia.setValue("");
			txtfcMontoDep.setValue("");
			txtfbEtMontoBanco.setValue("");
			txtfbReferenciaDep.setValue("");
			
			lblMsgResultBusquedaDepCaja.setValue("");
			dwFiltrarDepsCaja.setWindowState("normal");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
/******************************************************************************************/
/** Método: Mostrar el detalle de la confirmacion de depositos.
 *	Fecha:  29/09/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */	
	public void mostrarDetalleConfirmacion(ActionEvent ev){
		try {
			RowItem ri = (RowItem)ev.getComponent().getParent().getParent();
			Vwconciliacion vcs = (Vwconciliacion)gvConfirmacionesRegistradas.getDataRow(ri);
			
			lblDtCdNombreArchivo.setValue(vcs.getId().getArchivo());
			lblDtCdMontoDepsBco.setValue(vcs.getId().getMonto());
			lblDtCdReferencia.setValue(vcs.getId().getNoreferencia());
			lblDtCdBanco.setValue("100001");
			lblDtCdCuenta.setValue(vcs.getId().getIdcuenta());
			lblDtCdDescripcion.setValue(vcs.getId().getDescripcion());
			lblDtCdTipoConfirma.setValue(vcs.getId().getTipoconfirma());
			lblDtCdFechaConfirma.setValue(vcs.getId().getFechacrea());
			lblDtCdNoBatch.setValue(vcs.getId().getNobatch());
			lblDtCdUsuario.setValue(vcs.getId().getUsuario());
			lblDtCdDocumento.setValue(vcs.getId().getNoreferencia());
			dwDetalleConfirmacionDeposito.setWindowState("normal");
			
			//&& ====== Obtener detalle para ver si tuvo o no ajustes.
			Ajusteconc ajuste =  new ConfirmaDepositosCtrl().obtenerDetalleAjusteConfirma(vcs.getId().getIdconciliacion());
			if(ajuste == null) 
				lblDtCdAjustes.setValue("0.00");
			else
				lblDtCdAjustes.setValue(new Divisas().roundBigDecimal(ajuste.getMonto()) +" "+vcs.getId().getMoneda());
			
			//&& Cargar los datos de los depositos de caja asociados.
			List<Vdeposito> lstDepsCaja = new ConfirmaDepositosCtrl().obtenerDepositosCajaConfirmacion(vcs.getId().getIdconciliacion());
			if(lstDepsCaja == null)
				lstDepsCaja = new ArrayList<Vdeposito>(); 
			m.put("ccd_lstDtConDepositosCaja", lstDepsCaja);
			gvDtConDepositosCaja.dataBind();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
// && ======================= CERRAR VENTANAS  ========================== && //
	public void	cerrarConfirmarRevertirDeps(ActionEvent ev){
		dwValidaReversionConfirmacion.setWindowState("hidden");
	}
	public void	cerrarNotificaErrorConfirM(ActionEvent ev){
		dwNotifErrorConsultaConfirma.setWindowState("hidden");
	}
	public void	cerrarBusquedaConfirmacionDeps(ActionEvent ev){
		dwFiltrarDepsCaja.setWindowState("hidden");
	}
	public void	cerrarDetalleConfirmacionDeps(ActionEvent ev){
		dwDetalleConfirmacionDeposito.setWindowState("hidden");
	}
// && ======================= GETTERS Y SETTERS ========================== && //	
	public List<Vwconciliacion> getLstConfirmacionesRegistradas() {
		
		try {
			if( m.containsKey("ccd_lstConfirmacionesRegistradas") ){
				return lstConfirmacionesRegistradas = (ArrayList<Vwconciliacion>)
						m.get("ccd_lstConfirmacionesRegistradas");
			}
			lstConfirmacionesRegistradas = ConfirmaDepositosCtrl.obtenerConfirmaciones(800);
		
			if (lstConfirmacionesRegistradas != null) {
				Collections.sort(lstConfirmacionesRegistradas,
						new Comparator<Vwconciliacion>() {
							public int compare(Vwconciliacion o1,
									Vwconciliacion o2) {
								return o2.getId().getFechacrea()
										.compareTo(o1.getId().getFechacrea());
							}
						});
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(lstConfirmacionesRegistradas == null)
				lstConfirmacionesRegistradas = new ArrayList<Vwconciliacion>();
			m.put("ccd_lstConfirmacionesRegistradas", lstConfirmacionesRegistradas);
		}
		return lstConfirmacionesRegistradas;
	}
	public void setLstConfirmacionesRegistradas(
			List<Vwconciliacion> lstConfirmacionesRegistradas) {
		this.lstConfirmacionesRegistradas = lstConfirmacionesRegistradas;
	}
	public HtmlGridView getGvConfirmacionesRegistradas() {
		return gvConfirmacionesRegistradas;
	}
	public void setGvConfirmacionesRegistradas(
			HtmlGridView gvConfirmacionesRegistradas) {
		this.gvConfirmacionesRegistradas = gvConfirmacionesRegistradas;
	}
	public HtmlDialogWindow getDwDetalleConfirmacionDeposito() {
		return dwDetalleConfirmacionDeposito;
	}
	public void setDwDetalleConfirmacionDeposito(
			HtmlDialogWindow dwDetalleConfirmacionDeposito) {
		this.dwDetalleConfirmacionDeposito = dwDetalleConfirmacionDeposito;
	}
	public HtmlOutputText getLblDtCdNombreArchivo() {
		return lblDtCdNombreArchivo;
	}
	public void setLblDtCdNombreArchivo(HtmlOutputText lblDtCdNombreArchivo) {
		this.lblDtCdNombreArchivo = lblDtCdNombreArchivo;
	}
	public HtmlOutputText getLblDtCdMontoDepsBco() {
		return lblDtCdMontoDepsBco;
	}
	public void setLblDtCdMontoDepsBco(HtmlOutputText lblDtCdMontoDepsBco) {
		this.lblDtCdMontoDepsBco = lblDtCdMontoDepsBco;
	}
	public HtmlOutputText getLblDtCdReferencia() {
		return lblDtCdReferencia;
	}
	public void setLblDtCdReferencia(HtmlOutputText lblDtCdReferencia) {
		this.lblDtCdReferencia = lblDtCdReferencia;
	}
	public HtmlOutputText getLblDtCdBanco() {
		return lblDtCdBanco;
	}
	public void setLblDtCdBanco(HtmlOutputText lblDtCdBanco) {
		this.lblDtCdBanco = lblDtCdBanco;
	}
	public HtmlOutputText getLblDtCdCuenta() {
		return lblDtCdCuenta;
	}
	public void setLblDtCdCuenta(HtmlOutputText lblDtCdCuenta) {
		this.lblDtCdCuenta = lblDtCdCuenta;
	}
	public HtmlOutputText getLblDtCdDescripcion() {
		return lblDtCdDescripcion;
	}
	public void setLblDtCdDescripcion(HtmlOutputText lblDtCdDescripcion) {
		this.lblDtCdDescripcion = lblDtCdDescripcion;
	}
	public HtmlOutputText getLblDtCdTipoConfirma() {
		return lblDtCdTipoConfirma;
	}
	public void setLblDtCdTipoConfirma(HtmlOutputText lblDtCdTipoConfirma) {
		this.lblDtCdTipoConfirma = lblDtCdTipoConfirma;
	}
	public HtmlOutputText getLblDtCdFechaConfirma() {
		return lblDtCdFechaConfirma;
	}
	public void setLblDtCdFechaConfirma(HtmlOutputText lblDtCdFechaConfirma) {
		this.lblDtCdFechaConfirma = lblDtCdFechaConfirma;
	}
	public HtmlOutputText getLblDtCdNoBatch() {
		return lblDtCdNoBatch;
	}
	public void setLblDtCdNoBatch(HtmlOutputText lblDtCdNoBatch) {
		this.lblDtCdNoBatch = lblDtCdNoBatch;
	}
	public HtmlOutputText getLblDtCdUsuario() {
		return lblDtCdUsuario;
	}
	public void setLblDtCdUsuario(HtmlOutputText lblDtCdUsuario) {
		this.lblDtCdUsuario = lblDtCdUsuario;
	}
	public HtmlOutputText getLblDtCdAjustes() {
		return lblDtCdAjustes;
	}
	public void setLblDtCdAjustes(HtmlOutputText lblDtCdAjustes) {
		this.lblDtCdAjustes = lblDtCdAjustes;
	}
	public HtmlOutputText getLblDtCdDocumento() {
		return lblDtCdDocumento;
	}
	public void setLblDtCdDocumento(HtmlOutputText lblDtCdDocumento) {
		this.lblDtCdDocumento = lblDtCdDocumento;
	}
	public List<Vdeposito> getLstDtConDepositosCaja() {
		if(m.get("ccd_lstDtConDepositosCaja")== null){
			lstDtConDepositosCaja = new ArrayList<Vdeposito>();
		}else{
			lstDtConDepositosCaja = (ArrayList<Vdeposito>)m.get("ccd_lstDtConDepositosCaja");
		}
		return lstDtConDepositosCaja;
	}
	public void setLstDtConDepositosCaja(List<Vdeposito> lstDtConDepositosCaja) {
		this.lstDtConDepositosCaja = lstDtConDepositosCaja;
	}
	public HtmlGridView getGvDtConDepositosCaja() {
		return gvDtConDepositosCaja;
	}
	public void setGvDtConDepositosCaja(HtmlGridView gvDtConDepositosCaja) {
		this.gvDtConDepositosCaja = gvDtConDepositosCaja;
	}
	public HtmlDialogWindow getDwFiltrarDepsCaja() {
		return dwFiltrarDepsCaja;
	}
	public void setDwFiltrarDepsCaja(HtmlDialogWindow dwFiltrarDepsCaja) {
		this.dwFiltrarDepsCaja = dwFiltrarDepsCaja;
	}
	public List<SelectItem> getLstfcCajas() {
		if(m.get("ccd_lstfcCajas")==null)
			lstfcCajas = new ArrayList<SelectItem>();
		else
			lstfcCajas = (ArrayList<SelectItem>)m.get("ccd_lstfcCajas");
		return lstfcCajas;
	}
	public void setLstfcCajas(List<SelectItem> lstfcCajas) {
		this.lstfcCajas = lstfcCajas;
	}
	public List<SelectItem> getLstfcCompania() {
		if(m.get("ccd_lstfcCompania")==null)
			lstfcCompania = new ArrayList<SelectItem>();
		else
			lstfcCompania = (ArrayList<SelectItem>)m.get("ccd_lstfcCompania");
		return lstfcCompania;
	}
	public void setLstfcCompania(List<SelectItem> lstfcCompania) {
		this.lstfcCompania = lstfcCompania;
	}
	public List<SelectItem> getLstfcSucursal() {
		if(m.get("ccd_lstfcSucursal")==null)
			lstfcSucursal = new ArrayList<SelectItem>();
		else
			lstfcSucursal = (ArrayList<SelectItem>)m.get("ccd_lstfcSucursal");
		return lstfcSucursal;
	}
	public void setLstfcSucursal(List<SelectItem> lstfcSucursal) {
		this.lstfcSucursal = lstfcSucursal;
	}
	public List<SelectItem> getLstfcMoneda() {
		if(m.get("ccd_lstfcMoneda")==null)
			lstfcMoneda = new ArrayList<SelectItem>();
		else
			lstfcMoneda = (ArrayList<SelectItem>)m.get("ccd_lstfcMoneda");
		return lstfcMoneda;
	}
	public void setLstfcMoneda(List<SelectItem> lstfcMoneda) {
		this.lstfcMoneda = lstfcMoneda;
	}
	public HtmlDropDownList getDdlfcCajas() {
		return ddlfcCajas;
	}
	public void setDdlfcCajas(HtmlDropDownList ddlfcCajas) {
		this.ddlfcCajas = ddlfcCajas;
	}
	public HtmlDropDownList getDdlfcCompania() {
		return ddlfcCompania;
	}
	public void setDdlfcCompania(HtmlDropDownList ddlfcCompania) {
		this.ddlfcCompania = ddlfcCompania;
	}
	public HtmlDropDownList getDdlfcSucursal() {
		return ddlfcSucursal;
	}
	public void setDdlfcSucursal(HtmlDropDownList ddlfcSucursal) {
		this.ddlfcSucursal = ddlfcSucursal;
	}
	public HtmlDropDownList getDdlfcMoneda() {
		return ddlfcMoneda;
	}
	public void setDdlfcMoneda(HtmlDropDownList ddlfcMoneda) {
		this.ddlfcMoneda = ddlfcMoneda;
	}
	public HtmlDateChooser getTxtfcFechaIni() {
		return txtfcFechaIni;
	}
	public void setTxtfcFechaIni(HtmlDateChooser txtfcFechaIni) {
		this.txtfcFechaIni = txtfcFechaIni;
	}
	public HtmlDateChooser getTxtfcFechaFin() {
		return txtfcFechaFin;
	}
	public void setTxtfcFechaFin(HtmlDateChooser txtfcFechaFin) {
		this.txtfcFechaFin = txtfcFechaFin;
	}
	public HtmlInputText getTxtfcNoReferencia() {
		return txtfcNoReferencia;
	}
	public void setTxtfcNoReferencia(HtmlInputText txtfcNoReferencia) {
		this.txtfcNoReferencia = txtfcNoReferencia;
	}
	public HtmlInputText getTxtfcMontoDep() {
		return txtfcMontoDep;
	}
	public void setTxtfcMontoDep(HtmlInputText txtfcMontoDep) {
		this.txtfcMontoDep = txtfcMontoDep;
	}
	public HtmlOutputText getLblMsgResultBusquedaDepCaja() {
		return lblMsgResultBusquedaDepCaja;
	}
	public void setLblMsgResultBusquedaDepCaja(
			HtmlOutputText lblMsgResultBusquedaDepCaja) {
		this.lblMsgResultBusquedaDepCaja = lblMsgResultBusquedaDepCaja;
	}
	public List<SelectItem> getLstfbBancos() {
		if(m.get("ccd_lstfbBancos")==null)
			lstfbBancos = new ArrayList<SelectItem>();
		else
			lstfbBancos = (ArrayList<SelectItem>)m.get("ccd_lstfbBancos");
		return lstfbBancos;
	}
	public void setLstfbBancos(List<SelectItem> lstfbBancos) {
		this.lstfbBancos = lstfbBancos;
	}
	public List<SelectItem> getLstfbCuentaxBanco() {
		if(m.get("ccd_lstfbCuentaxBanco")==null)
			lstfbCuentaxBanco = new ArrayList<SelectItem>();
		else
			lstfbCuentaxBanco = (ArrayList<SelectItem>)m.get("ccd_lstfbCuentaxBanco");
		return lstfbCuentaxBanco;
	}
	public void setLstfbCuentaxBanco(List<SelectItem> lstfbCuentaxBanco) {
		if(m.get("ccd_lstfbCuentaxBanco")==null){
			lstfbCuentaxBanco = new ArrayList<SelectItem>();
			lstfbCuentaxBanco.add(new SelectItem("SCTA","Cuenta","Selección el banco para las cuentas"));
			m.put("ccd_lstfbCuentaxBanco", lstfbCuentaxBanco);
		}
		else
			lstfbCuentaxBanco = (ArrayList<SelectItem>)m.get("ccd_lstfbCuentaxBanco");
		this.lstfbCuentaxBanco = lstfbCuentaxBanco;
	}
	public HtmlDropDownList getDdlfbBancos() {
		return ddlfbBancos;
	}
	public void setDdlfbBancos(HtmlDropDownList ddlfbBancos) {
		this.ddlfbBancos = ddlfbBancos;
	}
	public HtmlDropDownList getDdlfbCuentaxBanco() {
		return ddlfbCuentaxBanco;
	}
	public void setDdlfbCuentaxBanco(HtmlDropDownList ddlfbCuentaxBanco) {
		this.ddlfbCuentaxBanco = ddlfbCuentaxBanco;
	}
	public HtmlInputText getTxtfbEtMontoBanco() {
		return txtfbEtMontoBanco;
	}
	public void setTxtfbEtMontoBanco(HtmlInputText txtfbEtMontoBanco) {
		this.txtfbEtMontoBanco = txtfbEtMontoBanco;
	}
	public HtmlInputText getTxtfbReferenciaDep() {
		return txtfbReferenciaDep;
	}
	public void setTxtfbReferenciaDep(HtmlInputText txtfbReferenciaDep) {
		this.txtfbReferenciaDep = txtfbReferenciaDep;
	}
	public HtmlDateChooser getTxtfbFechaIni() {
		return txtfbFechaIni;
	}
	public void setTxtfbFechaIni(HtmlDateChooser txtfbFechaIni) {
		this.txtfbFechaIni = txtfbFechaIni;
	}
	public HtmlDateChooser getTxtfbFechaFin() {
		return txtfbFechaFin;
	}
	public void setTxtfbFechaFin(HtmlDateChooser txtfbFechaFin) {
		this.txtfbFechaFin = txtfbFechaFin;
	}
	public HtmlDropDownList getDdlDetCfFcRangoMonto() {
		return ddlDetCfFcRangoMonto;
	}
	public void setDdlDetCfFcRangoMonto(HtmlDropDownList ddlDetCfFcRangoMonto) {
		this.ddlDetCfFcRangoMonto = ddlDetCfFcRangoMonto;
	}
	public List<SelectItem> getLstDetCfFcRangoMonto() {
		try {
			lstDetCfFcRangoMonto = new ArrayList<SelectItem>();
			lstDetCfFcRangoMonto.add(new SelectItem("1@CMME","Menor que","Filtra depositos de caja cuando su monto es menor al deposito de banco o valor ingresado"));
			lstDetCfFcRangoMonto.add(new SelectItem("2@CMMA","Mayor que","Filtra depositos de caja cuando su monto es mayor al deposito de banco o valor ingresado"));
			lstDetCfFcRangoMonto.add(new SelectItem("3@CMI","Igual que","Filtra depositos de caja cuando su monto es igual al deposito de banco o valor ingresado"));
		} catch (Exception e) {
			lstDetCfFcRangoMonto = new ArrayList<SelectItem>();
			e.printStackTrace();
		}
		return lstDetCfFcRangoMonto;
	}
	public void setLstDetCfFcRangoMonto(List<SelectItem> lstDetCfFcRangoMonto) {
		this.lstDetCfFcRangoMonto = lstDetCfFcRangoMonto;
	}
	public HtmlDateChooser getTxtfcnFechaIni() {
		return txtfcnFechaIni;
	}
	public void setTxtfcnFechaIni(HtmlDateChooser txtfcnFechaIni) {
		this.txtfcnFechaIni = txtfcnFechaIni;
	}
	public HtmlDateChooser getTxtfcnFechaFin() {
		return txtfcnFechaFin;
	}
	public void setTxtfcnFechaFin(HtmlDateChooser txtfcnFechaFin) {
		this.txtfcnFechaFin = txtfcnFechaFin;
	}
	public HtmlDialogWindow getDwNotifErrorConsultaConfirma() {
		return dwNotifErrorConsultaConfirma;
	}
	public void setDwNotifErrorConsultaConfirma(
			HtmlDialogWindow dwNotifErrorConsultaConfirma) {
		this.dwNotifErrorConsultaConfirma = dwNotifErrorConsultaConfirma;
	}
	public HtmlDialogWindowHeader getHdrDwNotErrorConsultaConfirma() {
		return hdrDwNotErrorConsultaConfirma;
	}
	public void setHdrDwNotErrorConsultaConfirma(
			HtmlDialogWindowHeader hdrDwNotErrorConsultaConfirma) {
		this.hdrDwNotErrorConsultaConfirma = hdrDwNotErrorConsultaConfirma;
	}
	public HtmlDialogWindow getDwValidaReversionConfirmacion() {
		return dwValidaReversionConfirmacion;
	}
	public void setDwValidaReversionConfirmacion(
			HtmlDialogWindow dwValidaReversionConfirmacion) {
		this.dwValidaReversionConfirmacion = dwValidaReversionConfirmacion;
	}
	public HtmlOutputText getLblMsgNotErrorConsultaConfirma() {
		return lblMsgNotErrorConsultaConfirma;
	}
	public void setLblMsgNotErrorConsultaConfirma(
			HtmlOutputText lblMsgNotErrorConsultaConfirma) {
		this.lblMsgNotErrorConsultaConfirma = lblMsgNotErrorConsultaConfirma;
	}
	public HtmlOutputText getLblMsgConfirmacionReversion() {
		return lblMsgConfirmacionReversion;
	}
	public void setLblMsgConfirmacionReversion(
			HtmlOutputText lblMsgConfirmacionReversion) {
		this.lblMsgConfirmacionReversion = lblMsgConfirmacionReversion;
	}
	public HtmlOutputText getLblEtNuevaReferBco() {
		return lblEtNuevaReferBco;
	}
	public void setLblEtNuevaReferBco(HtmlOutputText lblEtNuevaReferBco) {
		this.lblEtNuevaReferBco = lblEtNuevaReferBco;
	}
	public HtmlOutputText getLblMsgErrorUpdReferBco() {
		return lblMsgErrorUpdReferBco;
	}
	public void setLblMsgErrorUpdReferBco(HtmlOutputText lblMsgErrorUpdReferBco) {
		this.lblMsgErrorUpdReferBco = lblMsgErrorUpdReferBco;
	}
	public HtmlInputText getTxtCmNuevaReferBanco() {
		return txtCmNuevaReferBanco;
	}
	public void setTxtCmNuevaReferBanco(HtmlInputText txtCmNuevaReferBanco) {
		this.txtCmNuevaReferBanco = txtCmNuevaReferBanco;
	}
}

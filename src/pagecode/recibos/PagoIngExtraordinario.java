/**
 * 
 */
package pagecode.recibos;

import pagecode.PageCodeBase;
import com.infragistics.faces.shared.component.html.HtmlLink;
import javax.faces.component.html.HtmlOutputText;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
import com.infragistics.faces.input.component.html.HtmlCheckBox;
import javax.faces.component.html.HtmlInputText;
import com.infragistics.faces.window.component.html.HtmlDialogWindowClientEvents;
import com.ibm.faces.component.html.HtmlJspPanel;
import com.ibm.faces.component.html.HtmlGraphicImageEx;
import com.infragistics.faces.window.component.html.HtmlDialogWindow;
import com.infragistics.faces.window.component.html.HtmlDialogWindowRoundedCorners;
import com.infragistics.faces.window.component.html.HtmlDialogWindowContentPane;
import com.infragistics.faces.window.component.html.HtmlDialogWindowAutoPostBackFlags;
import javax.faces.component.html.HtmlForm;
import com.infragistics.faces.menu.component.html.HtmlMenu;
import com.infragistics.faces.menu.component.html.HtmlMenuItem;
import com.ibm.faces.component.html.HtmlInputHelperTypeahead;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.component.html.HtmlInputSecret;
import com.infragistics.faces.grid.component.html.HtmlGridView;
import com.infragistics.faces.grid.component.html.HtmlColumn;
import javax.faces.component.html.HtmlInputTextarea;
import com.infragistics.faces.window.component.html.HtmlDialogWindowHeader;
import com.infragistics.faces.grid.component.html.HtmlColumnSelectRow;
import javax.faces.component.UINamingContainer;
import com.ibm.faces.component.html.HtmlScriptCollector;
import com.infragistics.faces.input.component.html.HtmlDateChooser;

/**
 * @author Juan Ñamendi
 *
 */
public class PagoIngExtraordinario extends PageCodeBase {

	protected HtmlLink lnkMostrarAyudaCts;
	protected HtmlOutputText lblMensajeValidacionPrima;
	protected HtmlLink lnkfiltrarcuentas;
	protected HtmlOutputText lblCoseleccion;
	protected HtmlOutputText lblMsgErrorFiltroCta;
	protected HtmlDropDownList ddlBanco;
	protected HtmlOutputText lblMonto;
	protected HtmlOutputText lbletVouchermanual;
	protected HtmlCheckBox chkVoucherManual;
	protected HtmlOutputText lblAfiliado;
	protected HtmlDropDownList ddlAfiliado;
	protected HtmlOutputText lblReferencia1;
	protected HtmlInputText txtReferencia1;
	protected HtmlOutputText lblBanco;
	protected HtmlOutputText lblReferencia2;
	protected HtmlInputText txtReferencia2;
	protected HtmlOutputText lblReferencia3;
	protected HtmlInputText txtReferencia3;
	protected HtmlLink lnkRegistrarPago;
	protected HtmlDialogWindowClientEvents cledwCargando;
	protected HtmlJspPanel jspdwCargando;
	protected HtmlOutputText lblMensajeCargando;
	protected HtmlGraphicImageEx imagenCargando;
	protected HtmlDialogWindow dwCargando;
	protected HtmlDialogWindowRoundedCorners cledwCargando22;
	protected HtmlDialogWindowContentPane cpdwCargando;
	protected HtmlDialogWindowAutoPostBackFlags apbdwCargando;
	protected HtmlLink lnkCerrarMensaje13;
	protected HtmlLink lnkCerrarMensaje14;
	protected HtmlForm frmIngresosEx;
	protected HtmlMenu menu1;
	protected HtmlMenuItem item0;
	protected HtmlMenuItem item1;
	protected HtmlMenuItem item2;
	protected HtmlOutputText lblTitulo1Iex;
	protected HtmlOutputText lblTipoBusqueda;
	protected HtmlDropDownList ddlTipoBusqueda;
	protected HtmlOutputText lblparametroh;
	protected HtmlInputText txtParametro;
	protected HtmlInputHelperTypeahead tphPrima;
	protected HtmlLink lnkSetDtsCliente;
	protected HtmlGraphicImageEx imgLoader;
	protected HtmlOutputText lblFechaRecibo;
	protected HtmlOutputText lblfechaRecibo;
	protected HtmlOutputText lblFiltroCompania;
	protected HtmlDropDownList ddlFiltroCompanias;
	protected HtmlPanelGrid hpgFiltrosDdl;
	protected HtmlOutputText lblfiltroSucursal;
	protected HtmlOutputText lblCliente;
	protected HtmlOutputText lblCodigoSearch;
	protected HtmlOutputText lblNumRec;
	protected HtmlOutputText lblNumeroRecibo;
	protected HtmlOutputText lblMonaplicar;
	protected HtmlDropDownList ddlFiltroMonapl;
	protected HtmlOutputText lblMetodosPago;
	protected HtmlDropDownList ddlMetodoPago;
	protected HtmlInputText txtMonto;
	protected HtmlCheckBox chkIngresoManual;
	protected HtmlOutputText lblNoTarjeta;
	protected HtmlInputText txtNoTarjeta;
	protected HtmlOutputText lblFechaVenceT;
	protected HtmlInputText txtFechaVenceT;
	protected HtmlOutputText lblBanda3;
	protected HtmlInputSecret track;
	protected HtmlGridView gvMetodosPago;
	protected HtmlColumn coEliminarPago;
	protected HtmlLink lnkEliminarDetalle;
	protected HtmlOutputText lblEliminarPago;
	protected HtmlOutputText lblIdmetodo;
	protected HtmlOutputText lblMetodo;
	protected HtmlOutputText lblMetodo2;
	protected HtmlOutputText lblMoneda;
	protected HtmlOutputText lblMoneda22;
	protected HtmlOutputText lblMonto22;
	protected HtmlOutputText lblMonto222;
	protected HtmlOutputText lblTasa;
	protected HtmlOutputText lblTasa2;
	protected HtmlOutputText lblEquivDetalle;
	protected HtmlOutputText lblEquivDetalle2;
	protected HtmlOutputText lblReferencia29;
	protected HtmlOutputText lblReferencia19;
	protected HtmlOutputText lblReferencia222;
	protected HtmlOutputText lblReferencia22;
	protected HtmlOutputText lblReferencia322;
	protected HtmlOutputText lblReferencia32;
	protected HtmlOutputText lblReferencia323;
	protected HtmlOutputText lblReferencia33;
	protected HtmlOutputText lblTasaCambio;
	protected HtmlOutputText lblEtTipoOper;
	protected HtmlDropDownList ddlTipoOperacion;
	protected HtmlOutputText lblEtCtasxoper;
	protected HtmlOutputText lblCtaOperacion;
	protected HtmlOutputText lblCtaGpura;
	protected HtmlInputText txtCtaGpura;
	protected HtmlOutputText lblEtdscCta;
	protected HtmlOutputText lblDescrCuentaOperacion;
	protected HtmlOutputText text10;
	protected HtmlInputTextarea txtConcepto;
	protected HtmlOutputText lblMontoAplicar;
	protected HtmlInputText txtMontoAplicar;
	protected HtmlLink lnkFijarMontoaplicado;
	protected HtmlOutputText lblMontoRecibido;
	protected HtmlOutputText lblMontoRecibido2;
	protected HtmlOutputText lbletCambioapl;
	protected HtmlPanelGrid hpgCambiorecibo;
	protected HtmlInputText txtCambioForaneo;
	protected HtmlLink lnkCambio;
	protected HtmlOutputText lbletCambioDom;
	protected HtmlOutputText lblCambioDom;
	protected HtmlLink lnkProcesarRecibo2;
	protected HtmlDialogWindowHeader hdProcesaRecibo;
	protected HtmlPanelGrid grdProces;
	protected HtmlGraphicImageEx imgProcesa;
	protected HtmlLink lnkCerrarPagoMensaje;
	protected HtmlDialogWindowHeader hdSolicitarAutorizacion;
	protected HtmlOutputText lblMensajeAutorizacion;
	protected HtmlOutputText lblReferencia4;
	protected HtmlLink lnkProcesarSolicitud;
	protected HtmlDialogWindowHeader dwhCuentasxOperacion;
	protected HtmlJspPanel jspCuentasOperacion;
	protected HtmlOutputText lbletFiltroUN;
	protected HtmlInputText txtFiltroUN;
	protected HtmlOutputText lbletFiltroCobj;
	protected HtmlInputText txtFiltroCobjeto;
	protected HtmlOutputText lbletFiltroCSub;
	protected HtmlInputText txtFiltroCsub;
	protected HtmlColumnSelectRow coSeleccion;
	protected HtmlOutputText lbletCoidcuenta;
	protected HtmlOutputText lblCoidCuenta;
	protected HtmlOutputText lblCoCuenta;
	protected HtmlOutputText lbletCuenta;
	protected HtmlOutputText lblCoDescrip;
	protected HtmlOutputText lbletcoDescrip;
	protected HtmlOutputText lblCoMoneda;
	protected HtmlOutputText lbletcoMoneda;
	protected HtmlLink lnkAceptarFiltrarCta;
	protected HtmlDialogWindowHeader hdImprime;
	protected HtmlPanelGrid grGuardarRecibo;
	protected HtmlGraphicImageEx imageEx2GuardarRecibo;
	protected UINamingContainer vwfIngresosEx;
	protected HtmlScriptCollector scIngresosEx;
	protected HtmlOutputText lblTituloIex80;
	protected HtmlDropDownList ddlFiltrosucursal;
	protected HtmlOutputText lblfiltroUnineg;
	protected HtmlDropDownList ddlFiltrounineg;
	protected HtmlOutputText lblCod;
	protected HtmlOutputText lblNombreSearch;
	protected HtmlOutputText lblNom;
	protected HtmlDropDownList ddlMoneda;
	protected HtmlColumn coIdmetodo;
	protected HtmlColumn coMetodo;
	protected HtmlColumn coMonedaDetalle;
	protected HtmlColumn coMonto;
	protected HtmlColumn coTasa;
	protected HtmlColumn coEquivalente;
	protected HtmlColumn coReferencia;
	protected HtmlColumn coReferencia2;
	protected HtmlColumn coReferencia3;
	protected HtmlColumn coReferencia4;
	protected HtmlOutputText lblTasaCambio2;
	protected HtmlOutputText lblTasaCambioJde;
	protected HtmlOutputText lblTasaCambioJde2;
	protected HtmlOutputText lblCambioapl;
	protected HtmlLink lnkRefrescarReestablecer;
	protected HtmlDialogWindow dwProcesa;
	protected HtmlDialogWindowClientEvents cleProcesaRecibo;
	protected HtmlDialogWindowRoundedCorners rcProcesaRecibo;
	protected HtmlDialogWindowContentPane cpProcesaRecibo;
	protected HtmlJspPanel jspProcesa;
	protected HtmlDialogWindowAutoPostBackFlags apbProcesaRecibo;
	protected HtmlDialogWindow dwSolicitud;
	protected HtmlDialogWindowClientEvents cleAutorizaIex;
	protected HtmlDialogWindowRoundedCorners rcAutorizaIex;
	protected HtmlDialogWindowContentPane cpAutorizaIex;
	protected HtmlPanelGrid grid2;
	protected HtmlInputText txtReferencia;
	protected HtmlOutputText lblAut;
	protected HtmlDropDownList cmbAutoriza;
	protected HtmlOutputText text2;
	protected HtmlDateChooser txtFecha;
	protected HtmlOutputText lblConcepto4;
	protected HtmlInputTextarea txtObs;
	protected HtmlJspPanel jspPanel23;
	protected HtmlLink lnkCancelarSolicitud;
	protected HtmlDialogWindowAutoPostBackFlags apbReciboContado;
	protected HtmlDialogWindow dwCuentasOperacion;
	protected HtmlDialogWindowClientEvents cleCuentasOperacion;
	protected HtmlDialogWindowRoundedCorners rcCuentasOperacion;
	protected HtmlDialogWindowContentPane cpCuentasOperacion;
	protected HtmlGridView gvCuentasTo;
	protected HtmlColumn coIdcuenta;
	protected HtmlColumn coCuenta;
	protected HtmlColumn coDescrip;
	protected HtmlColumn coMoneda;
	protected HtmlPanelGrid hpgAceptFiltro;
	protected HtmlPanelGrid hpgMensajeError;
	protected HtmlDialogWindowAutoPostBackFlags apbCuentasOperacion;
	protected HtmlDialogWindow dwGuardarRecibo;
	protected HtmlDialogWindowClientEvents cleGuardarRecibo;
	protected HtmlDialogWindowRoundedCorners rcGuardarRecibo;
	protected HtmlDialogWindowContentPane cpGuardarRecibo;
	protected HtmlOutputText lblConfirmPrint;
	protected HtmlJspPanel jspPanel3;
	protected HtmlDialogWindowAutoPostBackFlags apbImprime;

	protected HtmlLink getLnkMostrarAyudaCts() {
		if (lnkMostrarAyudaCts == null) {
			lnkMostrarAyudaCts = (HtmlLink) findComponentInRoot("lnkMostrarAyudaCts");
		}
		return lnkMostrarAyudaCts;
	}

	protected HtmlOutputText getLblMensajeValidacionPrima() {
		if (lblMensajeValidacionPrima == null) {
			lblMensajeValidacionPrima = (HtmlOutputText) findComponentInRoot("lblMensajeValidacionPrima");
		}
		return lblMensajeValidacionPrima;
	}

	protected HtmlLink getLnkfiltrarcuentas() {
		if (lnkfiltrarcuentas == null) {
			lnkfiltrarcuentas = (HtmlLink) findComponentInRoot("lnkfiltrarcuentas");
		}
		return lnkfiltrarcuentas;
	}

	protected HtmlOutputText getLblCoseleccion() {
		if (lblCoseleccion == null) {
			lblCoseleccion = (HtmlOutputText) findComponentInRoot("lblCoseleccion");
		}
		return lblCoseleccion;
	}

	protected HtmlOutputText getLblMsgErrorFiltroCta() {
		if (lblMsgErrorFiltroCta == null) {
			lblMsgErrorFiltroCta = (HtmlOutputText) findComponentInRoot("lblMsgErrorFiltroCta");
		}
		return lblMsgErrorFiltroCta;
	}

	protected HtmlDropDownList getDdlBanco() {
		if (ddlBanco == null) {
			ddlBanco = (HtmlDropDownList) findComponentInRoot("ddlBanco");
		}
		return ddlBanco;
	}

	protected HtmlOutputText getLblMonto() {
		if (lblMonto == null) {
			lblMonto = (HtmlOutputText) findComponentInRoot("lblMonto");
		}
		return lblMonto;
	}

	protected HtmlOutputText getLbletVouchermanual() {
		if (lbletVouchermanual == null) {
			lbletVouchermanual = (HtmlOutputText) findComponentInRoot("lbletVouchermanual");
		}
		return lbletVouchermanual;
	}

	protected HtmlCheckBox getChkVoucherManual() {
		if (chkVoucherManual == null) {
			chkVoucherManual = (HtmlCheckBox) findComponentInRoot("chkVoucherManual");
		}
		return chkVoucherManual;
	}

	protected HtmlOutputText getLblAfiliado() {
		if (lblAfiliado == null) {
			lblAfiliado = (HtmlOutputText) findComponentInRoot("lblAfiliado");
		}
		return lblAfiliado;
	}

	protected HtmlDropDownList getDdlAfiliado() {
		if (ddlAfiliado == null) {
			ddlAfiliado = (HtmlDropDownList) findComponentInRoot("ddlAfiliado");
		}
		return ddlAfiliado;
	}

	protected HtmlOutputText getLblReferencia1() {
		if (lblReferencia1 == null) {
			lblReferencia1 = (HtmlOutputText) findComponentInRoot("lblReferencia1");
		}
		return lblReferencia1;
	}

	protected HtmlInputText getTxtReferencia1() {
		if (txtReferencia1 == null) {
			txtReferencia1 = (HtmlInputText) findComponentInRoot("txtReferencia1");
		}
		return txtReferencia1;
	}

	protected HtmlOutputText getLblBanco() {
		if (lblBanco == null) {
			lblBanco = (HtmlOutputText) findComponentInRoot("lblBanco");
		}
		return lblBanco;
	}

	protected HtmlOutputText getLblReferencia2() {
		if (lblReferencia2 == null) {
			lblReferencia2 = (HtmlOutputText) findComponentInRoot("lblReferencia2");
		}
		return lblReferencia2;
	}

	protected HtmlInputText getTxtReferencia2() {
		if (txtReferencia2 == null) {
			txtReferencia2 = (HtmlInputText) findComponentInRoot("txtReferencia2");
		}
		return txtReferencia2;
	}

	protected HtmlOutputText getLblReferencia3() {
		if (lblReferencia3 == null) {
			lblReferencia3 = (HtmlOutputText) findComponentInRoot("lblReferencia3");
		}
		return lblReferencia3;
	}

	protected HtmlInputText getTxtReferencia3() {
		if (txtReferencia3 == null) {
			txtReferencia3 = (HtmlInputText) findComponentInRoot("txtReferencia3");
		}
		return txtReferencia3;
	}

	protected HtmlLink getLnkRegistrarPago() {
		if (lnkRegistrarPago == null) {
			lnkRegistrarPago = (HtmlLink) findComponentInRoot("lnkRegistrarPago");
		}
		return lnkRegistrarPago;
	}

	protected HtmlDialogWindowClientEvents getCledwCargando() {
		if (cledwCargando == null) {
			cledwCargando = (HtmlDialogWindowClientEvents) findComponentInRoot("cledwCargando");
		}
		return cledwCargando;
	}

	protected HtmlJspPanel getJspdwCargando() {
		if (jspdwCargando == null) {
			jspdwCargando = (HtmlJspPanel) findComponentInRoot("jspdwCargando");
		}
		return jspdwCargando;
	}

	protected HtmlOutputText getLblMensajeCargando() {
		if (lblMensajeCargando == null) {
			lblMensajeCargando = (HtmlOutputText) findComponentInRoot("lblMensajeCargando");
		}
		return lblMensajeCargando;
	}

	protected HtmlGraphicImageEx getImagenCargando() {
		if (imagenCargando == null) {
			imagenCargando = (HtmlGraphicImageEx) findComponentInRoot("imagenCargando");
		}
		return imagenCargando;
	}

	protected HtmlDialogWindow getDwCargando() {
		if (dwCargando == null) {
			dwCargando = (HtmlDialogWindow) findComponentInRoot("dwCargando");
		}
		return dwCargando;
	}

	protected HtmlDialogWindowRoundedCorners getCledwCargando22() {
		if (cledwCargando22 == null) {
			cledwCargando22 = (HtmlDialogWindowRoundedCorners) findComponentInRoot("cledwCargando22");
		}
		return cledwCargando22;
	}

	protected HtmlDialogWindowContentPane getCpdwCargando() {
		if (cpdwCargando == null) {
			cpdwCargando = (HtmlDialogWindowContentPane) findComponentInRoot("cpdwCargando");
		}
		return cpdwCargando;
	}

	protected HtmlDialogWindowAutoPostBackFlags getApbdwCargando() {
		if (apbdwCargando == null) {
			apbdwCargando = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbdwCargando");
		}
		return apbdwCargando;
	}

	protected HtmlLink getLnkCerrarMensaje13() {
		if (lnkCerrarMensaje13 == null) {
			lnkCerrarMensaje13 = (HtmlLink) findComponentInRoot("lnkCerrarMensaje13");
		}
		return lnkCerrarMensaje13;
	}

	protected HtmlLink getLnkCerrarMensaje14() {
		if (lnkCerrarMensaje14 == null) {
			lnkCerrarMensaje14 = (HtmlLink) findComponentInRoot("lnkCerrarMensaje14");
		}
		return lnkCerrarMensaje14;
	}

	protected HtmlForm getFrmIngresosEx() {
		if (frmIngresosEx == null) {
			frmIngresosEx = (HtmlForm) findComponentInRoot("frmIngresosEx");
		}
		return frmIngresosEx;
	}

	protected HtmlMenu getMenu1() {
		if (menu1 == null) {
			menu1 = (HtmlMenu) findComponentInRoot("menu1");
		}
		return menu1;
	}

	protected HtmlMenuItem getItem0() {
		if (item0 == null) {
			item0 = (HtmlMenuItem) findComponentInRoot("item0");
		}
		return item0;
	}

	protected HtmlMenuItem getItem1() {
		if (item1 == null) {
			item1 = (HtmlMenuItem) findComponentInRoot("item1");
		}
		return item1;
	}

	protected HtmlMenuItem getItem2() {
		if (item2 == null) {
			item2 = (HtmlMenuItem) findComponentInRoot("item2");
		}
		return item2;
	}

	protected HtmlOutputText getLblTitulo1Iex() {
		if (lblTitulo1Iex == null) {
			lblTitulo1Iex = (HtmlOutputText) findComponentInRoot("lblTitulo1Iex");
		}
		return lblTitulo1Iex;
	}

	protected HtmlOutputText getLblTipoBusqueda() {
		if (lblTipoBusqueda == null) {
			lblTipoBusqueda = (HtmlOutputText) findComponentInRoot("lblTipoBusqueda");
		}
		return lblTipoBusqueda;
	}

	protected HtmlDropDownList getDdlTipoBusqueda() {
		if (ddlTipoBusqueda == null) {
			ddlTipoBusqueda = (HtmlDropDownList) findComponentInRoot("ddlTipoBusqueda");
		}
		return ddlTipoBusqueda;
	}

	protected HtmlOutputText getLblparametroh() {
		if (lblparametroh == null) {
			lblparametroh = (HtmlOutputText) findComponentInRoot("lblparametroh");
		}
		return lblparametroh;
	}

	protected HtmlInputText getTxtParametro() {
		if (txtParametro == null) {
			txtParametro = (HtmlInputText) findComponentInRoot("txtParametro");
		}
		return txtParametro;
	}

	protected HtmlInputHelperTypeahead getTphPrima() {
		if (tphPrima == null) {
			tphPrima = (HtmlInputHelperTypeahead) findComponentInRoot("tphPrima");
		}
		return tphPrima;
	}

	protected HtmlLink getLnkSetDtsCliente() {
		if (lnkSetDtsCliente == null) {
			lnkSetDtsCliente = (HtmlLink) findComponentInRoot("lnkSetDtsCliente");
		}
		return lnkSetDtsCliente;
	}

	protected HtmlGraphicImageEx getImgLoader() {
		if (imgLoader == null) {
			imgLoader = (HtmlGraphicImageEx) findComponentInRoot("imgLoader");
		}
		return imgLoader;
	}

	protected HtmlOutputText getLblFechaRecibo() {
		if (lblFechaRecibo == null) {
			lblFechaRecibo = (HtmlOutputText) findComponentInRoot("lblFechaRecibo");
		}
		return lblFechaRecibo;
	}

	protected HtmlOutputText getLblfechaRecibo() {
		if (lblfechaRecibo == null) {
			lblfechaRecibo = (HtmlOutputText) findComponentInRoot("lblfechaRecibo");
		}
		return lblfechaRecibo;
	}

	protected HtmlOutputText getLblFiltroCompania() {
		if (lblFiltroCompania == null) {
			lblFiltroCompania = (HtmlOutputText) findComponentInRoot("lblFiltroCompania");
		}
		return lblFiltroCompania;
	}

	protected HtmlDropDownList getDdlFiltroCompanias() {
		if (ddlFiltroCompanias == null) {
			ddlFiltroCompanias = (HtmlDropDownList) findComponentInRoot("ddlFiltroCompanias");
		}
		return ddlFiltroCompanias;
	}

	protected HtmlPanelGrid getHpgFiltrosDdl() {
		if (hpgFiltrosDdl == null) {
			hpgFiltrosDdl = (HtmlPanelGrid) findComponentInRoot("hpgFiltrosDdl");
		}
		return hpgFiltrosDdl;
	}

	protected HtmlOutputText getLblfiltroSucursal() {
		if (lblfiltroSucursal == null) {
			lblfiltroSucursal = (HtmlOutputText) findComponentInRoot("lblfiltroSucursal");
		}
		return lblfiltroSucursal;
	}

	protected HtmlOutputText getLblCliente() {
		if (lblCliente == null) {
			lblCliente = (HtmlOutputText) findComponentInRoot("lblCliente");
		}
		return lblCliente;
	}

	protected HtmlOutputText getLblCodigoSearch() {
		if (lblCodigoSearch == null) {
			lblCodigoSearch = (HtmlOutputText) findComponentInRoot("lblCodigoSearch");
		}
		return lblCodigoSearch;
	}

	protected HtmlOutputText getLblNumRec() {
		if (lblNumRec == null) {
			lblNumRec = (HtmlOutputText) findComponentInRoot("lblNumRec");
		}
		return lblNumRec;
	}

	protected HtmlOutputText getLblNumeroRecibo() {
		if (lblNumeroRecibo == null) {
			lblNumeroRecibo = (HtmlOutputText) findComponentInRoot("lblNumeroRecibo");
		}
		return lblNumeroRecibo;
	}

	protected HtmlOutputText getLblMonaplicar() {
		if (lblMonaplicar == null) {
			lblMonaplicar = (HtmlOutputText) findComponentInRoot("lblMonaplicar");
		}
		return lblMonaplicar;
	}

	protected HtmlDropDownList getDdlFiltroMonapl() {
		if (ddlFiltroMonapl == null) {
			ddlFiltroMonapl = (HtmlDropDownList) findComponentInRoot("ddlFiltroMonapl");
		}
		return ddlFiltroMonapl;
	}

	protected HtmlOutputText getLblMetodosPago() {
		if (lblMetodosPago == null) {
			lblMetodosPago = (HtmlOutputText) findComponentInRoot("lblMetodosPago");
		}
		return lblMetodosPago;
	}

	protected HtmlDropDownList getDdlMetodoPago() {
		if (ddlMetodoPago == null) {
			ddlMetodoPago = (HtmlDropDownList) findComponentInRoot("ddlMetodoPago");
		}
		return ddlMetodoPago;
	}

	protected HtmlInputText getTxtMonto() {
		if (txtMonto == null) {
			txtMonto = (HtmlInputText) findComponentInRoot("txtMonto");
		}
		return txtMonto;
	}

	protected HtmlCheckBox getChkIngresoManual() {
		if (chkIngresoManual == null) {
			chkIngresoManual = (HtmlCheckBox) findComponentInRoot("chkIngresoManual");
		}
		return chkIngresoManual;
	}

	protected HtmlOutputText getLblNoTarjeta() {
		if (lblNoTarjeta == null) {
			lblNoTarjeta = (HtmlOutputText) findComponentInRoot("lblNoTarjeta");
		}
		return lblNoTarjeta;
	}

	protected HtmlInputText getTxtNoTarjeta() {
		if (txtNoTarjeta == null) {
			txtNoTarjeta = (HtmlInputText) findComponentInRoot("txtNoTarjeta");
		}
		return txtNoTarjeta;
	}

	protected HtmlOutputText getLblFechaVenceT() {
		if (lblFechaVenceT == null) {
			lblFechaVenceT = (HtmlOutputText) findComponentInRoot("lblFechaVenceT");
		}
		return lblFechaVenceT;
	}

	protected HtmlInputText getTxtFechaVenceT() {
		if (txtFechaVenceT == null) {
			txtFechaVenceT = (HtmlInputText) findComponentInRoot("txtFechaVenceT");
		}
		return txtFechaVenceT;
	}

	protected HtmlOutputText getLblBanda3() {
		if (lblBanda3 == null) {
			lblBanda3 = (HtmlOutputText) findComponentInRoot("lblBanda3");
		}
		return lblBanda3;
	}

	protected HtmlInputSecret getTrack() {
		if (track == null) {
			track = (HtmlInputSecret) findComponentInRoot("track");
		}
		return track;
	}

	protected HtmlGridView getGvMetodosPago() {
		if (gvMetodosPago == null) {
			gvMetodosPago = (HtmlGridView) findComponentInRoot("gvMetodosPago");
		}
		return gvMetodosPago;
	}

	protected HtmlColumn getCoEliminarPago() {
		if (coEliminarPago == null) {
			coEliminarPago = (HtmlColumn) findComponentInRoot("coEliminarPago");
		}
		return coEliminarPago;
	}

	protected HtmlLink getLnkEliminarDetalle() {
		if (lnkEliminarDetalle == null) {
			lnkEliminarDetalle = (HtmlLink) findComponentInRoot("lnkEliminarDetalle");
		}
		return lnkEliminarDetalle;
	}

	protected HtmlOutputText getLblEliminarPago() {
		if (lblEliminarPago == null) {
			lblEliminarPago = (HtmlOutputText) findComponentInRoot("lblEliminarPago");
		}
		return lblEliminarPago;
	}

	protected HtmlOutputText getLblIdmetodo() {
		if (lblIdmetodo == null) {
			lblIdmetodo = (HtmlOutputText) findComponentInRoot("lblIdmetodo");
		}
		return lblIdmetodo;
	}

	protected HtmlOutputText getLblMetodo() {
		if (lblMetodo == null) {
			lblMetodo = (HtmlOutputText) findComponentInRoot("lblMetodo");
		}
		return lblMetodo;
	}

	protected HtmlOutputText getLblMetodo2() {
		if (lblMetodo2 == null) {
			lblMetodo2 = (HtmlOutputText) findComponentInRoot("lblMetodo2");
		}
		return lblMetodo2;
	}

	protected HtmlOutputText getLblMoneda() {
		if (lblMoneda == null) {
			lblMoneda = (HtmlOutputText) findComponentInRoot("lblMoneda");
		}
		return lblMoneda;
	}

	protected HtmlOutputText getLblMoneda22() {
		if (lblMoneda22 == null) {
			lblMoneda22 = (HtmlOutputText) findComponentInRoot("lblMoneda22");
		}
		return lblMoneda22;
	}

	protected HtmlOutputText getLblMonto22() {
		if (lblMonto22 == null) {
			lblMonto22 = (HtmlOutputText) findComponentInRoot("lblMonto22");
		}
		return lblMonto22;
	}

	protected HtmlOutputText getLblMonto222() {
		if (lblMonto222 == null) {
			lblMonto222 = (HtmlOutputText) findComponentInRoot("lblMonto222");
		}
		return lblMonto222;
	}

	protected HtmlOutputText getLblTasa() {
		if (lblTasa == null) {
			lblTasa = (HtmlOutputText) findComponentInRoot("lblTasa");
		}
		return lblTasa;
	}

	protected HtmlOutputText getLblTasa2() {
		if (lblTasa2 == null) {
			lblTasa2 = (HtmlOutputText) findComponentInRoot("lblTasa2");
		}
		return lblTasa2;
	}

	protected HtmlOutputText getLblEquivDetalle() {
		if (lblEquivDetalle == null) {
			lblEquivDetalle = (HtmlOutputText) findComponentInRoot("lblEquivDetalle");
		}
		return lblEquivDetalle;
	}

	protected HtmlOutputText getLblEquivDetalle2() {
		if (lblEquivDetalle2 == null) {
			lblEquivDetalle2 = (HtmlOutputText) findComponentInRoot("lblEquivDetalle2");
		}
		return lblEquivDetalle2;
	}

	protected HtmlOutputText getLblReferencia29() {
		if (lblReferencia29 == null) {
			lblReferencia29 = (HtmlOutputText) findComponentInRoot("lblReferencia29");
		}
		return lblReferencia29;
	}

	protected HtmlOutputText getLblReferencia19() {
		if (lblReferencia19 == null) {
			lblReferencia19 = (HtmlOutputText) findComponentInRoot("lblReferencia19");
		}
		return lblReferencia19;
	}

	protected HtmlOutputText getLblReferencia222() {
		if (lblReferencia222 == null) {
			lblReferencia222 = (HtmlOutputText) findComponentInRoot("lblReferencia222");
		}
		return lblReferencia222;
	}

	protected HtmlOutputText getLblReferencia22() {
		if (lblReferencia22 == null) {
			lblReferencia22 = (HtmlOutputText) findComponentInRoot("lblReferencia22");
		}
		return lblReferencia22;
	}

	protected HtmlOutputText getLblReferencia322() {
		if (lblReferencia322 == null) {
			lblReferencia322 = (HtmlOutputText) findComponentInRoot("lblReferencia322");
		}
		return lblReferencia322;
	}

	protected HtmlOutputText getLblReferencia32() {
		if (lblReferencia32 == null) {
			lblReferencia32 = (HtmlOutputText) findComponentInRoot("lblReferencia32");
		}
		return lblReferencia32;
	}

	protected HtmlOutputText getLblReferencia323() {
		if (lblReferencia323 == null) {
			lblReferencia323 = (HtmlOutputText) findComponentInRoot("lblReferencia323");
		}
		return lblReferencia323;
	}

	protected HtmlOutputText getLblReferencia33() {
		if (lblReferencia33 == null) {
			lblReferencia33 = (HtmlOutputText) findComponentInRoot("lblReferencia33");
		}
		return lblReferencia33;
	}

	protected HtmlOutputText getLblTasaCambio() {
		if (lblTasaCambio == null) {
			lblTasaCambio = (HtmlOutputText) findComponentInRoot("lblTasaCambio");
		}
		return lblTasaCambio;
	}

	protected HtmlOutputText getLblEtTipoOper() {
		if (lblEtTipoOper == null) {
			lblEtTipoOper = (HtmlOutputText) findComponentInRoot("lblEtTipoOper");
		}
		return lblEtTipoOper;
	}

	protected HtmlDropDownList getDdlTipoOperacion() {
		if (ddlTipoOperacion == null) {
			ddlTipoOperacion = (HtmlDropDownList) findComponentInRoot("ddlTipoOperacion");
		}
		return ddlTipoOperacion;
	}

	protected HtmlOutputText getLblEtCtasxoper() {
		if (lblEtCtasxoper == null) {
			lblEtCtasxoper = (HtmlOutputText) findComponentInRoot("lblEtCtasxoper");
		}
		return lblEtCtasxoper;
	}

	protected HtmlOutputText getLblCtaOperacion() {
		if (lblCtaOperacion == null) {
			lblCtaOperacion = (HtmlOutputText) findComponentInRoot("lblCtaOperacion");
		}
		return lblCtaOperacion;
	}

	protected HtmlOutputText getLblCtaGpura() {
		if (lblCtaGpura == null) {
			lblCtaGpura = (HtmlOutputText) findComponentInRoot("lblCtaGpura");
		}
		return lblCtaGpura;
	}

	protected HtmlInputText getTxtCtaGpura() {
		if (txtCtaGpura == null) {
			txtCtaGpura = (HtmlInputText) findComponentInRoot("txtCtaGpura");
		}
		return txtCtaGpura;
	}

	protected HtmlOutputText getLblEtdscCta() {
		if (lblEtdscCta == null) {
			lblEtdscCta = (HtmlOutputText) findComponentInRoot("lblEtdscCta");
		}
		return lblEtdscCta;
	}

	protected HtmlOutputText getLblDescrCuentaOperacion() {
		if (lblDescrCuentaOperacion == null) {
			lblDescrCuentaOperacion = (HtmlOutputText) findComponentInRoot("lblDescrCuentaOperacion");
		}
		return lblDescrCuentaOperacion;
	}

	protected HtmlOutputText getText10() {
		if (text10 == null) {
			text10 = (HtmlOutputText) findComponentInRoot("text10");
		}
		return text10;
	}

	protected HtmlInputTextarea getTxtConcepto() {
		if (txtConcepto == null) {
			txtConcepto = (HtmlInputTextarea) findComponentInRoot("txtConcepto");
		}
		return txtConcepto;
	}

	protected HtmlOutputText getLblMontoAplicar() {
		if (lblMontoAplicar == null) {
			lblMontoAplicar = (HtmlOutputText) findComponentInRoot("lblMontoAplicar");
		}
		return lblMontoAplicar;
	}

	protected HtmlInputText getTxtMontoAplicar() {
		if (txtMontoAplicar == null) {
			txtMontoAplicar = (HtmlInputText) findComponentInRoot("txtMontoAplicar");
		}
		return txtMontoAplicar;
	}

	protected HtmlLink getLnkFijarMontoaplicado() {
		if (lnkFijarMontoaplicado == null) {
			lnkFijarMontoaplicado = (HtmlLink) findComponentInRoot("lnkFijarMontoaplicado");
		}
		return lnkFijarMontoaplicado;
	}

	protected HtmlOutputText getLblMontoRecibido() {
		if (lblMontoRecibido == null) {
			lblMontoRecibido = (HtmlOutputText) findComponentInRoot("lblMontoRecibido");
		}
		return lblMontoRecibido;
	}

	protected HtmlOutputText getLblMontoRecibido2() {
		if (lblMontoRecibido2 == null) {
			lblMontoRecibido2 = (HtmlOutputText) findComponentInRoot("lblMontoRecibido2");
		}
		return lblMontoRecibido2;
	}

	protected HtmlOutputText getLbletCambioapl() {
		if (lbletCambioapl == null) {
			lbletCambioapl = (HtmlOutputText) findComponentInRoot("lbletCambioapl");
		}
		return lbletCambioapl;
	}

	protected HtmlPanelGrid getHpgCambiorecibo() {
		if (hpgCambiorecibo == null) {
			hpgCambiorecibo = (HtmlPanelGrid) findComponentInRoot("hpgCambiorecibo");
		}
		return hpgCambiorecibo;
	}

	protected HtmlInputText getTxtCambioForaneo() {
		if (txtCambioForaneo == null) {
			txtCambioForaneo = (HtmlInputText) findComponentInRoot("txtCambioForaneo");
		}
		return txtCambioForaneo;
	}

	protected HtmlLink getLnkCambio() {
		if (lnkCambio == null) {
			lnkCambio = (HtmlLink) findComponentInRoot("lnkCambio");
		}
		return lnkCambio;
	}

	protected HtmlOutputText getLbletCambioDom() {
		if (lbletCambioDom == null) {
			lbletCambioDom = (HtmlOutputText) findComponentInRoot("lbletCambioDom");
		}
		return lbletCambioDom;
	}

	protected HtmlOutputText getLblCambioDom() {
		if (lblCambioDom == null) {
			lblCambioDom = (HtmlOutputText) findComponentInRoot("lblCambioDom");
		}
		return lblCambioDom;
	}

	protected HtmlLink getLnkProcesarRecibo2() {
		if (lnkProcesarRecibo2 == null) {
			lnkProcesarRecibo2 = (HtmlLink) findComponentInRoot("lnkProcesarRecibo2");
		}
		return lnkProcesarRecibo2;
	}

	protected HtmlDialogWindowHeader getHdProcesaRecibo() {
		if (hdProcesaRecibo == null) {
			hdProcesaRecibo = (HtmlDialogWindowHeader) findComponentInRoot("hdProcesaRecibo");
		}
		return hdProcesaRecibo;
	}

	protected HtmlPanelGrid getGrdProces() {
		if (grdProces == null) {
			grdProces = (HtmlPanelGrid) findComponentInRoot("grdProces");
		}
		return grdProces;
	}

	protected HtmlGraphicImageEx getImgProcesa() {
		if (imgProcesa == null) {
			imgProcesa = (HtmlGraphicImageEx) findComponentInRoot("imgProcesa");
		}
		return imgProcesa;
	}

	protected HtmlLink getLnkCerrarPagoMensaje() {
		if (lnkCerrarPagoMensaje == null) {
			lnkCerrarPagoMensaje = (HtmlLink) findComponentInRoot("lnkCerrarPagoMensaje");
		}
		return lnkCerrarPagoMensaje;
	}

	protected HtmlDialogWindowHeader getHdSolicitarAutorizacion() {
		if (hdSolicitarAutorizacion == null) {
			hdSolicitarAutorizacion = (HtmlDialogWindowHeader) findComponentInRoot("hdSolicitarAutorizacion");
		}
		return hdSolicitarAutorizacion;
	}

	protected HtmlOutputText getLblMensajeAutorizacion() {
		if (lblMensajeAutorizacion == null) {
			lblMensajeAutorizacion = (HtmlOutputText) findComponentInRoot("lblMensajeAutorizacion");
		}
		return lblMensajeAutorizacion;
	}

	protected HtmlOutputText getLblReferencia4() {
		if (lblReferencia4 == null) {
			lblReferencia4 = (HtmlOutputText) findComponentInRoot("lblReferencia4");
		}
		return lblReferencia4;
	}

	protected HtmlLink getLnkProcesarSolicitud() {
		if (lnkProcesarSolicitud == null) {
			lnkProcesarSolicitud = (HtmlLink) findComponentInRoot("lnkProcesarSolicitud");
		}
		return lnkProcesarSolicitud;
	}

	protected HtmlDialogWindowHeader getDwhCuentasxOperacion() {
		if (dwhCuentasxOperacion == null) {
			dwhCuentasxOperacion = (HtmlDialogWindowHeader) findComponentInRoot("dwhCuentasxOperacion");
		}
		return dwhCuentasxOperacion;
	}

	protected HtmlJspPanel getJspCuentasOperacion() {
		if (jspCuentasOperacion == null) {
			jspCuentasOperacion = (HtmlJspPanel) findComponentInRoot("jspCuentasOperacion");
		}
		return jspCuentasOperacion;
	}

	protected HtmlOutputText getLbletFiltroUN() {
		if (lbletFiltroUN == null) {
			lbletFiltroUN = (HtmlOutputText) findComponentInRoot("lbletFiltroUN");
		}
		return lbletFiltroUN;
	}

	protected HtmlInputText getTxtFiltroUN() {
		if (txtFiltroUN == null) {
			txtFiltroUN = (HtmlInputText) findComponentInRoot("txtFiltroUN");
		}
		return txtFiltroUN;
	}

	protected HtmlOutputText getLbletFiltroCobj() {
		if (lbletFiltroCobj == null) {
			lbletFiltroCobj = (HtmlOutputText) findComponentInRoot("lbletFiltroCobj");
		}
		return lbletFiltroCobj;
	}

	protected HtmlInputText getTxtFiltroCobjeto() {
		if (txtFiltroCobjeto == null) {
			txtFiltroCobjeto = (HtmlInputText) findComponentInRoot("txtFiltroCobjeto");
		}
		return txtFiltroCobjeto;
	}

	protected HtmlOutputText getLbletFiltroCSub() {
		if (lbletFiltroCSub == null) {
			lbletFiltroCSub = (HtmlOutputText) findComponentInRoot("lbletFiltroCSub");
		}
		return lbletFiltroCSub;
	}

	protected HtmlInputText getTxtFiltroCsub() {
		if (txtFiltroCsub == null) {
			txtFiltroCsub = (HtmlInputText) findComponentInRoot("txtFiltroCsub");
		}
		return txtFiltroCsub;
	}

	protected HtmlColumnSelectRow getCoSeleccion() {
		if (coSeleccion == null) {
			coSeleccion = (HtmlColumnSelectRow) findComponentInRoot("coSeleccion");
		}
		return coSeleccion;
	}

	protected HtmlOutputText getLbletCoidcuenta() {
		if (lbletCoidcuenta == null) {
			lbletCoidcuenta = (HtmlOutputText) findComponentInRoot("lbletCoidcuenta");
		}
		return lbletCoidcuenta;
	}

	protected HtmlOutputText getLblCoidCuenta() {
		if (lblCoidCuenta == null) {
			lblCoidCuenta = (HtmlOutputText) findComponentInRoot("lblCoidCuenta");
		}
		return lblCoidCuenta;
	}

	protected HtmlOutputText getLblCoCuenta() {
		if (lblCoCuenta == null) {
			lblCoCuenta = (HtmlOutputText) findComponentInRoot("lblCoCuenta");
		}
		return lblCoCuenta;
	}

	protected HtmlOutputText getLbletCuenta() {
		if (lbletCuenta == null) {
			lbletCuenta = (HtmlOutputText) findComponentInRoot("lbletCuenta");
		}
		return lbletCuenta;
	}

	protected HtmlOutputText getLblCoDescrip() {
		if (lblCoDescrip == null) {
			lblCoDescrip = (HtmlOutputText) findComponentInRoot("lblCoDescrip");
		}
		return lblCoDescrip;
	}

	protected HtmlOutputText getLbletcoDescrip() {
		if (lbletcoDescrip == null) {
			lbletcoDescrip = (HtmlOutputText) findComponentInRoot("lbletcoDescrip");
		}
		return lbletcoDescrip;
	}

	protected HtmlOutputText getLblCoMoneda() {
		if (lblCoMoneda == null) {
			lblCoMoneda = (HtmlOutputText) findComponentInRoot("lblCoMoneda");
		}
		return lblCoMoneda;
	}

	protected HtmlOutputText getLbletcoMoneda() {
		if (lbletcoMoneda == null) {
			lbletcoMoneda = (HtmlOutputText) findComponentInRoot("lbletcoMoneda");
		}
		return lbletcoMoneda;
	}

	protected HtmlLink getLnkAceptarFiltrarCta() {
		if (lnkAceptarFiltrarCta == null) {
			lnkAceptarFiltrarCta = (HtmlLink) findComponentInRoot("lnkAceptarFiltrarCta");
		}
		return lnkAceptarFiltrarCta;
	}

	protected HtmlDialogWindowHeader getHdImprime() {
		if (hdImprime == null) {
			hdImprime = (HtmlDialogWindowHeader) findComponentInRoot("hdImprime");
		}
		return hdImprime;
	}

	protected HtmlPanelGrid getGrGuardarRecibo() {
		if (grGuardarRecibo == null) {
			grGuardarRecibo = (HtmlPanelGrid) findComponentInRoot("grGuardarRecibo");
		}
		return grGuardarRecibo;
	}

	protected HtmlGraphicImageEx getImageEx2GuardarRecibo() {
		if (imageEx2GuardarRecibo == null) {
			imageEx2GuardarRecibo = (HtmlGraphicImageEx) findComponentInRoot("imageEx2GuardarRecibo");
		}
		return imageEx2GuardarRecibo;
	}

	protected UINamingContainer getVwfIngresosEx() {
		if (vwfIngresosEx == null) {
			vwfIngresosEx = (UINamingContainer) findComponentInRoot("vwfIngresosEx");
		}
		return vwfIngresosEx;
	}

	protected HtmlScriptCollector getScIngresosEx() {
		if (scIngresosEx == null) {
			scIngresosEx = (HtmlScriptCollector) findComponentInRoot("scIngresosEx");
		}
		return scIngresosEx;
	}

	protected HtmlOutputText getLblTituloIex80() {
		if (lblTituloIex80 == null) {
			lblTituloIex80 = (HtmlOutputText) findComponentInRoot("lblTituloIex80");
		}
		return lblTituloIex80;
	}

	protected HtmlDropDownList getDdlFiltrosucursal() {
		if (ddlFiltrosucursal == null) {
			ddlFiltrosucursal = (HtmlDropDownList) findComponentInRoot("ddlFiltrosucursal");
		}
		return ddlFiltrosucursal;
	}

	protected HtmlOutputText getLblfiltroUnineg() {
		if (lblfiltroUnineg == null) {
			lblfiltroUnineg = (HtmlOutputText) findComponentInRoot("lblfiltroUnineg");
		}
		return lblfiltroUnineg;
	}

	protected HtmlDropDownList getDdlFiltrounineg() {
		if (ddlFiltrounineg == null) {
			ddlFiltrounineg = (HtmlDropDownList) findComponentInRoot("ddlFiltrounineg");
		}
		return ddlFiltrounineg;
	}

	protected HtmlOutputText getLblCod() {
		if (lblCod == null) {
			lblCod = (HtmlOutputText) findComponentInRoot("lblCod");
		}
		return lblCod;
	}

	protected HtmlOutputText getLblNombreSearch() {
		if (lblNombreSearch == null) {
			lblNombreSearch = (HtmlOutputText) findComponentInRoot("lblNombreSearch");
		}
		return lblNombreSearch;
	}

	protected HtmlOutputText getLblNom() {
		if (lblNom == null) {
			lblNom = (HtmlOutputText) findComponentInRoot("lblNom");
		}
		return lblNom;
	}

	protected HtmlDropDownList getDdlMoneda() {
		if (ddlMoneda == null) {
			ddlMoneda = (HtmlDropDownList) findComponentInRoot("ddlMoneda");
		}
		return ddlMoneda;
	}

	protected HtmlColumn getCoIdmetodo() {
		if (coIdmetodo == null) {
			coIdmetodo = (HtmlColumn) findComponentInRoot("coIdmetodo");
		}
		return coIdmetodo;
	}

	protected HtmlColumn getCoMetodo() {
		if (coMetodo == null) {
			coMetodo = (HtmlColumn) findComponentInRoot("coMetodo");
		}
		return coMetodo;
	}

	protected HtmlColumn getCoMonedaDetalle() {
		if (coMonedaDetalle == null) {
			coMonedaDetalle = (HtmlColumn) findComponentInRoot("coMonedaDetalle");
		}
		return coMonedaDetalle;
	}

	protected HtmlColumn getCoMonto() {
		if (coMonto == null) {
			coMonto = (HtmlColumn) findComponentInRoot("coMonto");
		}
		return coMonto;
	}

	protected HtmlColumn getCoTasa() {
		if (coTasa == null) {
			coTasa = (HtmlColumn) findComponentInRoot("coTasa");
		}
		return coTasa;
	}

	protected HtmlColumn getCoEquivalente() {
		if (coEquivalente == null) {
			coEquivalente = (HtmlColumn) findComponentInRoot("coEquivalente");
		}
		return coEquivalente;
	}

	protected HtmlColumn getCoReferencia() {
		if (coReferencia == null) {
			coReferencia = (HtmlColumn) findComponentInRoot("coReferencia");
		}
		return coReferencia;
	}

	protected HtmlColumn getCoReferencia2() {
		if (coReferencia2 == null) {
			coReferencia2 = (HtmlColumn) findComponentInRoot("coReferencia2");
		}
		return coReferencia2;
	}

	protected HtmlColumn getCoReferencia3() {
		if (coReferencia3 == null) {
			coReferencia3 = (HtmlColumn) findComponentInRoot("coReferencia3");
		}
		return coReferencia3;
	}

	protected HtmlColumn getCoReferencia4() {
		if (coReferencia4 == null) {
			coReferencia4 = (HtmlColumn) findComponentInRoot("coReferencia4");
		}
		return coReferencia4;
	}

	protected HtmlOutputText getLblTasaCambio2() {
		if (lblTasaCambio2 == null) {
			lblTasaCambio2 = (HtmlOutputText) findComponentInRoot("lblTasaCambio2");
		}
		return lblTasaCambio2;
	}

	protected HtmlOutputText getLblTasaCambioJde() {
		if (lblTasaCambioJde == null) {
			lblTasaCambioJde = (HtmlOutputText) findComponentInRoot("lblTasaCambioJde");
		}
		return lblTasaCambioJde;
	}

	protected HtmlOutputText getLblTasaCambioJde2() {
		if (lblTasaCambioJde2 == null) {
			lblTasaCambioJde2 = (HtmlOutputText) findComponentInRoot("lblTasaCambioJde2");
		}
		return lblTasaCambioJde2;
	}

	protected HtmlOutputText getLblCambioapl() {
		if (lblCambioapl == null) {
			lblCambioapl = (HtmlOutputText) findComponentInRoot("lblCambioapl");
		}
		return lblCambioapl;
	}

	protected HtmlLink getLnkRefrescarReestablecer() {
		if (lnkRefrescarReestablecer == null) {
			lnkRefrescarReestablecer = (HtmlLink) findComponentInRoot("lnkRefrescarReestablecer");
		}
		return lnkRefrescarReestablecer;
	}

	protected HtmlDialogWindow getDwProcesa() {
		if (dwProcesa == null) {
			dwProcesa = (HtmlDialogWindow) findComponentInRoot("dwProcesa");
		}
		return dwProcesa;
	}

	protected HtmlDialogWindowClientEvents getCleProcesaRecibo() {
		if (cleProcesaRecibo == null) {
			cleProcesaRecibo = (HtmlDialogWindowClientEvents) findComponentInRoot("cleProcesaRecibo");
		}
		return cleProcesaRecibo;
	}

	protected HtmlDialogWindowRoundedCorners getRcProcesaRecibo() {
		if (rcProcesaRecibo == null) {
			rcProcesaRecibo = (HtmlDialogWindowRoundedCorners) findComponentInRoot("rcProcesaRecibo");
		}
		return rcProcesaRecibo;
	}

	protected HtmlDialogWindowContentPane getCpProcesaRecibo() {
		if (cpProcesaRecibo == null) {
			cpProcesaRecibo = (HtmlDialogWindowContentPane) findComponentInRoot("cpProcesaRecibo");
		}
		return cpProcesaRecibo;
	}

	protected HtmlJspPanel getJspProcesa() {
		if (jspProcesa == null) {
			jspProcesa = (HtmlJspPanel) findComponentInRoot("jspProcesa");
		}
		return jspProcesa;
	}

	protected HtmlDialogWindowAutoPostBackFlags getApbProcesaRecibo() {
		if (apbProcesaRecibo == null) {
			apbProcesaRecibo = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbProcesaRecibo");
		}
		return apbProcesaRecibo;
	}

	protected HtmlDialogWindow getDwSolicitud() {
		if (dwSolicitud == null) {
			dwSolicitud = (HtmlDialogWindow) findComponentInRoot("dwSolicitud");
		}
		return dwSolicitud;
	}

	protected HtmlDialogWindowClientEvents getCleAutorizaIex() {
		if (cleAutorizaIex == null) {
			cleAutorizaIex = (HtmlDialogWindowClientEvents) findComponentInRoot("cleAutorizaIex");
		}
		return cleAutorizaIex;
	}

	protected HtmlDialogWindowRoundedCorners getRcAutorizaIex() {
		if (rcAutorizaIex == null) {
			rcAutorizaIex = (HtmlDialogWindowRoundedCorners) findComponentInRoot("rcAutorizaIex");
		}
		return rcAutorizaIex;
	}

	protected HtmlDialogWindowContentPane getCpAutorizaIex() {
		if (cpAutorizaIex == null) {
			cpAutorizaIex = (HtmlDialogWindowContentPane) findComponentInRoot("cpAutorizaIex");
		}
		return cpAutorizaIex;
	}

	protected HtmlPanelGrid getGrid2() {
		if (grid2 == null) {
			grid2 = (HtmlPanelGrid) findComponentInRoot("grid2");
		}
		return grid2;
	}

	protected HtmlInputText getTxtReferencia() {
		if (txtReferencia == null) {
			txtReferencia = (HtmlInputText) findComponentInRoot("txtReferencia");
		}
		return txtReferencia;
	}

	protected HtmlOutputText getLblAut() {
		if (lblAut == null) {
			lblAut = (HtmlOutputText) findComponentInRoot("lblAut");
		}
		return lblAut;
	}

	protected HtmlDropDownList getCmbAutoriza() {
		if (cmbAutoriza == null) {
			cmbAutoriza = (HtmlDropDownList) findComponentInRoot("cmbAutoriza");
		}
		return cmbAutoriza;
	}

	protected HtmlOutputText getText2() {
		if (text2 == null) {
			text2 = (HtmlOutputText) findComponentInRoot("text2");
		}
		return text2;
	}

	protected HtmlDateChooser getTxtFecha() {
		if (txtFecha == null) {
			txtFecha = (HtmlDateChooser) findComponentInRoot("txtFecha");
		}
		return txtFecha;
	}

	protected HtmlOutputText getLblConcepto4() {
		if (lblConcepto4 == null) {
			lblConcepto4 = (HtmlOutputText) findComponentInRoot("lblConcepto4");
		}
		return lblConcepto4;
	}

	protected HtmlInputTextarea getTxtObs() {
		if (txtObs == null) {
			txtObs = (HtmlInputTextarea) findComponentInRoot("txtObs");
		}
		return txtObs;
	}

	protected HtmlJspPanel getJspPanel23() {
		if (jspPanel23 == null) {
			jspPanel23 = (HtmlJspPanel) findComponentInRoot("jspPanel23");
		}
		return jspPanel23;
	}

	protected HtmlLink getLnkCancelarSolicitud() {
		if (lnkCancelarSolicitud == null) {
			lnkCancelarSolicitud = (HtmlLink) findComponentInRoot("lnkCancelarSolicitud");
		}
		return lnkCancelarSolicitud;
	}

	protected HtmlDialogWindowAutoPostBackFlags getApbReciboContado() {
		if (apbReciboContado == null) {
			apbReciboContado = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbReciboContado");
		}
		return apbReciboContado;
	}

	protected HtmlDialogWindow getDwCuentasOperacion() {
		if (dwCuentasOperacion == null) {
			dwCuentasOperacion = (HtmlDialogWindow) findComponentInRoot("dwCuentasOperacion");
		}
		return dwCuentasOperacion;
	}

	protected HtmlDialogWindowClientEvents getCleCuentasOperacion() {
		if (cleCuentasOperacion == null) {
			cleCuentasOperacion = (HtmlDialogWindowClientEvents) findComponentInRoot("cleCuentasOperacion");
		}
		return cleCuentasOperacion;
	}

	protected HtmlDialogWindowRoundedCorners getRcCuentasOperacion() {
		if (rcCuentasOperacion == null) {
			rcCuentasOperacion = (HtmlDialogWindowRoundedCorners) findComponentInRoot("rcCuentasOperacion");
		}
		return rcCuentasOperacion;
	}

	protected HtmlDialogWindowContentPane getCpCuentasOperacion() {
		if (cpCuentasOperacion == null) {
			cpCuentasOperacion = (HtmlDialogWindowContentPane) findComponentInRoot("cpCuentasOperacion");
		}
		return cpCuentasOperacion;
	}

	protected HtmlGridView getGvCuentasTo() {
		if (gvCuentasTo == null) {
			gvCuentasTo = (HtmlGridView) findComponentInRoot("gvCuentasTo");
		}
		return gvCuentasTo;
	}

	protected HtmlColumn getCoIdcuenta() {
		if (coIdcuenta == null) {
			coIdcuenta = (HtmlColumn) findComponentInRoot("coIdcuenta");
		}
		return coIdcuenta;
	}

	protected HtmlColumn getCoCuenta() {
		if (coCuenta == null) {
			coCuenta = (HtmlColumn) findComponentInRoot("coCuenta");
		}
		return coCuenta;
	}

	protected HtmlColumn getCoDescrip() {
		if (coDescrip == null) {
			coDescrip = (HtmlColumn) findComponentInRoot("coDescrip");
		}
		return coDescrip;
	}

	protected HtmlColumn getCoMoneda() {
		if (coMoneda == null) {
			coMoneda = (HtmlColumn) findComponentInRoot("coMoneda");
		}
		return coMoneda;
	}

	protected HtmlPanelGrid getHpgAceptFiltro() {
		if (hpgAceptFiltro == null) {
			hpgAceptFiltro = (HtmlPanelGrid) findComponentInRoot("hpgAceptFiltro");
		}
		return hpgAceptFiltro;
	}

	protected HtmlPanelGrid getHpgMensajeError() {
		if (hpgMensajeError == null) {
			hpgMensajeError = (HtmlPanelGrid) findComponentInRoot("hpgMensajeError");
		}
		return hpgMensajeError;
	}

	protected HtmlDialogWindowAutoPostBackFlags getApbCuentasOperacion() {
		if (apbCuentasOperacion == null) {
			apbCuentasOperacion = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbCuentasOperacion");
		}
		return apbCuentasOperacion;
	}

	protected HtmlDialogWindow getDwGuardarRecibo() {
		if (dwGuardarRecibo == null) {
			dwGuardarRecibo = (HtmlDialogWindow) findComponentInRoot("dwGuardarRecibo");
		}
		return dwGuardarRecibo;
	}

	protected HtmlDialogWindowClientEvents getCleGuardarRecibo() {
		if (cleGuardarRecibo == null) {
			cleGuardarRecibo = (HtmlDialogWindowClientEvents) findComponentInRoot("cleGuardarRecibo");
		}
		return cleGuardarRecibo;
	}

	protected HtmlDialogWindowRoundedCorners getRcGuardarRecibo() {
		if (rcGuardarRecibo == null) {
			rcGuardarRecibo = (HtmlDialogWindowRoundedCorners) findComponentInRoot("rcGuardarRecibo");
		}
		return rcGuardarRecibo;
	}

	protected HtmlDialogWindowContentPane getCpGuardarRecibo() {
		if (cpGuardarRecibo == null) {
			cpGuardarRecibo = (HtmlDialogWindowContentPane) findComponentInRoot("cpGuardarRecibo");
		}
		return cpGuardarRecibo;
	}

	protected HtmlOutputText getLblConfirmPrint() {
		if (lblConfirmPrint == null) {
			lblConfirmPrint = (HtmlOutputText) findComponentInRoot("lblConfirmPrint");
		}
		return lblConfirmPrint;
	}

	protected HtmlJspPanel getJspPanel3() {
		if (jspPanel3 == null) {
			jspPanel3 = (HtmlJspPanel) findComponentInRoot("jspPanel3");
		}
		return jspPanel3;
	}

	protected HtmlDialogWindowAutoPostBackFlags getApbImprime() {
		if (apbImprime == null) {
			apbImprime = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbImprime");
		}
		return apbImprime;
	}

}
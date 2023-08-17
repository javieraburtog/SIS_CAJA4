/**
 * 
 */
package pagecode.tilesContent;

import pagecode.PageCodeBase;
import javax.faces.component.html.HtmlOutputText;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
import com.infragistics.faces.input.component.html.HtmlCheckBox;
import javax.faces.component.html.HtmlInputText;
import com.infragistics.faces.shared.component.html.HtmlLink;
import com.ibm.faces.component.html.HtmlScriptCollector;
import javax.faces.component.html.HtmlForm;
import com.infragistics.faces.menu.component.html.HtmlMenu;
import com.infragistics.faces.menu.component.html.HtmlMenuItem;
import com.ibm.faces.component.html.HtmlInputHelperTypeahead;
import com.ibm.faces.component.html.HtmlGraphicImageEx;
import com.ibm.faces.component.html.HtmlJspPanel;
import com.infragistics.faces.input.component.html.HtmlDateChooser;
import javax.faces.component.html.HtmlPanelGrid;
import com.infragistics.faces.grid.component.html.HtmlGridView;
import com.infragistics.faces.grid.component.html.HtmlColumn;
import javax.faces.component.html.HtmlInputTextarea;
import com.infragistics.faces.window.component.html.HtmlDialogWindow;
import com.infragistics.faces.window.component.html.HtmlDialogWindowHeader;
import com.infragistics.faces.window.component.html.HtmlDialogWindowClientEvents;
import com.infragistics.faces.window.component.html.HtmlDialogWindowRoundedCorners;
import com.infragistics.faces.window.component.html.HtmlDialogWindowContentPane;
import com.infragistics.faces.window.component.html.HtmlDialogWindowAutoPostBackFlags;
import javax.faces.component.UINamingContainer;
import javax.faces.component.html.HtmlInputSecret;

/**
 * @author Juan Ñamendi
 *
 */
public class PagoPrimaReserva2_bodyarea extends PageCodeBase {

	protected HtmlOutputText lblMensajeValidacionPrima;
	protected HtmlDropDownList ddlAfiliado;
	protected HtmlOutputText lbletVouchermanual;
	protected HtmlCheckBox chkVoucherManual;
	protected HtmlOutputText lblAfiliado;
	protected HtmlOutputText lblReferencia1;
	protected HtmlInputText txtReferencia1;
	protected HtmlOutputText lblBanco;
	protected HtmlDropDownList ddlBanco;
	protected HtmlOutputText lblReferencia2;
	protected HtmlInputText txtReferencia2;
	protected HtmlOutputText lblReferencia3;
	protected HtmlInputText txtReferencia3;
	protected HtmlLink lnkRegistrarPago;
	protected HtmlScriptCollector scPrimaReserva;
	protected HtmlForm frmPrimaReserva;
	protected HtmlMenu menu1;
	protected HtmlMenuItem item0;
	protected HtmlMenuItem item1;
	protected HtmlMenuItem item2;
	protected HtmlOutputText lblTitulo1Contado;
	protected HtmlOutputText lblTituloContado80;
	protected HtmlOutputText lblTipoBusqueda;
	protected HtmlDropDownList dropBusqueda;
	protected HtmlOutputText lblparametroh;
	protected HtmlInputText txtParametro;
	protected HtmlInputHelperTypeahead tphPrima;
	protected HtmlLink lnkSetDtsCliente;
	protected HtmlGraphicImageEx imgLoader;
	protected HtmlJspPanel jspPanel1;
	protected HtmlOutputText lblFechaRecibo;
	protected HtmlOutputText txtFechaRecibo;
	protected HtmlDateChooser txtFecham;
	protected HtmlLink lnkAjustarTasaJdeAfecha;
	protected HtmlOutputText lblCompania;
	protected HtmlDropDownList cmbCompanias;
	protected HtmlPanelGrid pgrFiltros01;
	protected HtmlOutputText lblfiltroSucursal;
	protected HtmlDropDownList ddlFiltrosucursal;
	protected HtmlOutputText lblfiltroUnineg;
	protected HtmlDropDownList ddlFiltrounineg;
	protected HtmlOutputText lblCliente;
	protected HtmlOutputText lblCodigoSearch;
	protected HtmlOutputText lblCod;
	protected HtmlOutputText lblNombreSearch;
	protected HtmlOutputText lblNom;
	protected HtmlPanelGrid pgrFiltros002;
	protected HtmlOutputText lblNumRecibo;
	protected HtmlOutputText lblNumeroRecibo;
	protected HtmlOutputText lblTipoRecibo;
	protected HtmlDropDownList ddlTipoRecibo;
	protected HtmlOutputText lblNumRecm;
	protected HtmlInputText txtNumRec;
	protected HtmlOutputText lblMetodosPago;
	protected HtmlDropDownList ddlMetodoPago;
	protected HtmlOutputText lblMonto;
	protected HtmlInputText txtMonto;
	protected HtmlDropDownList ddlMoneda;
	protected HtmlGridView gvMetodosPago;
	protected HtmlColumn coEliminarPago;
	protected HtmlLink lnkEliminarDetalle;
	protected HtmlOutputText lblEliminarPago;
	protected HtmlColumn coIdmetodo;
	protected HtmlOutputText lblIdmetodo;
	protected HtmlColumn coMetodo;
	protected HtmlOutputText lblMetodo;
	protected HtmlOutputText lblMetodo2;
	protected HtmlColumn coMonedaDetalle;
	protected HtmlOutputText lblMoneda;
	protected HtmlOutputText lblMoneda22;
	protected HtmlColumn coMonto;
	protected HtmlOutputText lblMonto22;
	protected HtmlOutputText lblMonto222;
	protected HtmlColumn coTasa;
	protected HtmlOutputText lblTasa;
	protected HtmlOutputText lblTasa2;
	protected HtmlColumn coEquivalente;
	protected HtmlOutputText lblEquivDetalle;
	protected HtmlOutputText lblEquivDetalle2;
	protected HtmlColumn coReferencia;
	protected HtmlOutputText lblReferencia29;
	protected HtmlOutputText lblReferencia19;
	protected HtmlColumn coReferencia2;
	protected HtmlOutputText lblReferencia222;
	protected HtmlOutputText lblReferencia22;
	protected HtmlColumn coReferencia3;
	protected HtmlOutputText lblReferencia322;
	protected HtmlOutputText lblReferencia32;
	protected HtmlColumn coReferencia4;
	protected HtmlOutputText lblReferencia323;
	protected HtmlOutputText lblReferencia33;
	protected HtmlOutputText lblTasaCambio;
	protected HtmlOutputText lblTasaCambio2;
	protected HtmlOutputText lblTasaCambioJde;
	protected HtmlOutputText lblTasaCambioJde2;
	protected HtmlOutputText lblMonAplicar;
	protected HtmlDropDownList ddlMonedaAplicada;
	protected HtmlOutputText text4;
	protected HtmlDropDownList cmbTipoProd;
	protected HtmlOutputText text6;
	protected HtmlDropDownList cmbMarcas;
	protected HtmlOutputText text200;
	protected HtmlDropDownList cmbModelos;
	protected HtmlOutputText text212;
	protected HtmlInputText txtNoItem;
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
	protected HtmlOutputText lblCambioapl;
	protected HtmlLink lnkCambio;
	protected HtmlOutputText lbletCambioDom;
	protected HtmlOutputText lblCambioDom;
	protected HtmlLink lnkProcesarRecibo2;
	protected HtmlLink lnkRefrescarReestablecer;
	protected HtmlDialogWindow dwProcesa;
	protected HtmlDialogWindowHeader hdProcesaRecibo;
	protected HtmlDialogWindowClientEvents cleProcesaRecibo;
	protected HtmlDialogWindowRoundedCorners rcProcesaRecibo;
	protected HtmlDialogWindowContentPane cpProcesaRecibo;
	protected HtmlPanelGrid grdProces;
	protected HtmlGraphicImageEx imgProcesa;
	protected HtmlJspPanel jspProcesa;
	protected HtmlLink lnkCerrarPagoMensaje;
	protected HtmlDialogWindowAutoPostBackFlags apbProcesaRecibo;
	protected HtmlDialogWindow dwImprime;
	protected HtmlDialogWindowHeader hdImprime;
	protected HtmlDialogWindowClientEvents cleImprime;
	protected HtmlDialogWindowRoundedCorners rcImprime;
	protected HtmlDialogWindowContentPane cpImprime;
	protected HtmlPanelGrid grid100;
	protected HtmlGraphicImageEx imageEx2Imprime;
	protected HtmlOutputText lblConfirmPrint;
	protected HtmlJspPanel jspPanel3;
	protected HtmlLink lnkCerrarMensaje13;
	protected HtmlLink lnkCerrarMensaje14;
	protected HtmlDialogWindowAutoPostBackFlags apbImprime;
	protected HtmlDialogWindow dwAutoriza;
	protected HtmlDialogWindowHeader hdSolicitarAutorizacion;
	protected HtmlDialogWindowClientEvents cleAutorizaContado;
	protected HtmlDialogWindowRoundedCorners rcAutorizaContado;
	protected HtmlDialogWindowContentPane cpAutorizaContado;
	protected HtmlOutputText lblMensajeAutorizacion;
	protected HtmlPanelGrid grid2;
	protected HtmlOutputText lblReferencia4;
	protected HtmlInputText txtReferencia;
	protected HtmlOutputText lblAut;
	protected HtmlDropDownList ddlAutoriza;
	protected HtmlOutputText text2;
	protected HtmlDateChooser txtFecha;
	protected HtmlOutputText lblConcepto4;
	protected HtmlInputTextarea txtObs;
	protected HtmlJspPanel jspPanel23;
	protected HtmlLink lnkProcesarSolicitud;
	protected HtmlLink lnkCancelarSolicitud;
	protected HtmlDialogWindowAutoPostBackFlags apbReciboContado;
	protected UINamingContainer vfPrimaReserva;
	protected HtmlDialogWindowClientEvents cledwCargando;
	protected HtmlJspPanel jspdwCargando;
	protected HtmlOutputText lblMensajeCargando;
	protected HtmlGraphicImageEx imagenCargando;
	protected HtmlDialogWindow dwCargando;
	protected HtmlDialogWindowRoundedCorners cledwCargando22;
	protected HtmlDialogWindowContentPane cpdwCargando;
	protected HtmlDialogWindowAutoPostBackFlags apbdwCargando;
	protected HtmlLink lnkSearchContrato;
	protected HtmlCheckBox chkContrato;
	protected HtmlCheckBox chkIngresoManual;
	protected HtmlOutputText lblNoTarjeta;
	protected HtmlInputText txtNoTarjeta;
	protected HtmlOutputText lblFechaVenceT;
	protected HtmlInputText txtFechaVenceT;
	protected HtmlOutputText lblBanda3;
	protected HtmlInputSecret track;
	protected HtmlOutputText lblTipodoc;
	protected HtmlDropDownList ddlTipodoc;

	protected HtmlOutputText getLblMensajeValidacionPrima() {
		if (lblMensajeValidacionPrima == null) {
			lblMensajeValidacionPrima = (HtmlOutputText) findComponentInRoot("lblMensajeValidacionPrima");
		}
		return lblMensajeValidacionPrima;
	}

	protected HtmlDropDownList getDdlAfiliado() {
		if (ddlAfiliado == null) {
			ddlAfiliado = (HtmlDropDownList) findComponentInRoot("ddlAfiliado");
		}
		return ddlAfiliado;
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

	protected HtmlDropDownList getDdlBanco() {
		if (ddlBanco == null) {
			ddlBanco = (HtmlDropDownList) findComponentInRoot("ddlBanco");
		}
		return ddlBanco;
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

	protected HtmlScriptCollector getScPrimaReserva() {
		if (scPrimaReserva == null) {
			scPrimaReserva = (HtmlScriptCollector) findComponentInRoot("scPrimaReserva");
		}
		return scPrimaReserva;
	}

	protected HtmlForm getFrmPrimaReserva() {
		if (frmPrimaReserva == null) {
			frmPrimaReserva = (HtmlForm) findComponentInRoot("frmPrimaReserva");
		}
		return frmPrimaReserva;
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

	protected HtmlOutputText getLblTitulo1Contado() {
		if (lblTitulo1Contado == null) {
			lblTitulo1Contado = (HtmlOutputText) findComponentInRoot("lblTitulo1Contado");
		}
		return lblTitulo1Contado;
	}

	protected HtmlOutputText getLblTituloContado80() {
		if (lblTituloContado80 == null) {
			lblTituloContado80 = (HtmlOutputText) findComponentInRoot("lblTituloContado80");
		}
		return lblTituloContado80;
	}

	protected HtmlOutputText getLblTipoBusqueda() {
		if (lblTipoBusqueda == null) {
			lblTipoBusqueda = (HtmlOutputText) findComponentInRoot("lblTipoBusqueda");
		}
		return lblTipoBusqueda;
	}

	protected HtmlDropDownList getDropBusqueda() {
		if (dropBusqueda == null) {
			dropBusqueda = (HtmlDropDownList) findComponentInRoot("dropBusqueda");
		}
		return dropBusqueda;
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

	protected HtmlJspPanel getJspPanel1() {
		if (jspPanel1 == null) {
			jspPanel1 = (HtmlJspPanel) findComponentInRoot("jspPanel1");
		}
		return jspPanel1;
	}

	protected HtmlOutputText getLblFechaRecibo() {
		if (lblFechaRecibo == null) {
			lblFechaRecibo = (HtmlOutputText) findComponentInRoot("lblFechaRecibo");
		}
		return lblFechaRecibo;
	}

	protected HtmlOutputText getTxtFechaRecibo() {
		if (txtFechaRecibo == null) {
			txtFechaRecibo = (HtmlOutputText) findComponentInRoot("txtFechaRecibo");
		}
		return txtFechaRecibo;
	}

	protected HtmlDateChooser getTxtFecham() {
		if (txtFecham == null) {
			txtFecham = (HtmlDateChooser) findComponentInRoot("txtFecham");
		}
		return txtFecham;
	}

	protected HtmlLink getLnkAjustarTasaJdeAfecha() {
		if (lnkAjustarTasaJdeAfecha == null) {
			lnkAjustarTasaJdeAfecha = (HtmlLink) findComponentInRoot("lnkAjustarTasaJdeAfecha");
		}
		return lnkAjustarTasaJdeAfecha;
	}

	protected HtmlOutputText getLblCompania() {
		if (lblCompania == null) {
			lblCompania = (HtmlOutputText) findComponentInRoot("lblCompania");
		}
		return lblCompania;
	}

	protected HtmlDropDownList getCmbCompanias() {
		if (cmbCompanias == null) {
			cmbCompanias = (HtmlDropDownList) findComponentInRoot("cmbCompanias");
		}
		return cmbCompanias;
	}

	protected HtmlPanelGrid getPgrFiltros01() {
		if (pgrFiltros01 == null) {
			pgrFiltros01 = (HtmlPanelGrid) findComponentInRoot("pgrFiltros01");
		}
		return pgrFiltros01;
	}

	protected HtmlOutputText getLblfiltroSucursal() {
		if (lblfiltroSucursal == null) {
			lblfiltroSucursal = (HtmlOutputText) findComponentInRoot("lblfiltroSucursal");
		}
		return lblfiltroSucursal;
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

	protected HtmlPanelGrid getPgrFiltros002() {
		if (pgrFiltros002 == null) {
			pgrFiltros002 = (HtmlPanelGrid) findComponentInRoot("pgrFiltros002");
		}
		return pgrFiltros002;
	}

	protected HtmlOutputText getLblNumRecibo() {
		if (lblNumRecibo == null) {
			lblNumRecibo = (HtmlOutputText) findComponentInRoot("lblNumRecibo");
		}
		return lblNumRecibo;
	}

	protected HtmlOutputText getLblNumeroRecibo() {
		if (lblNumeroRecibo == null) {
			lblNumeroRecibo = (HtmlOutputText) findComponentInRoot("lblNumeroRecibo");
		}
		return lblNumeroRecibo;
	}

	protected HtmlOutputText getLblTipoRecibo() {
		if (lblTipoRecibo == null) {
			lblTipoRecibo = (HtmlOutputText) findComponentInRoot("lblTipoRecibo");
		}
		return lblTipoRecibo;
	}

	protected HtmlDropDownList getDdlTipoRecibo() {
		if (ddlTipoRecibo == null) {
			ddlTipoRecibo = (HtmlDropDownList) findComponentInRoot("ddlTipoRecibo");
		}
		return ddlTipoRecibo;
	}

	protected HtmlOutputText getLblNumRecm() {
		if (lblNumRecm == null) {
			lblNumRecm = (HtmlOutputText) findComponentInRoot("lblNumRecm");
		}
		return lblNumRecm;
	}

	protected HtmlInputText getTxtNumRec() {
		if (txtNumRec == null) {
			txtNumRec = (HtmlInputText) findComponentInRoot("txtNumRec");
		}
		return txtNumRec;
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

	protected HtmlOutputText getLblMonto() {
		if (lblMonto == null) {
			lblMonto = (HtmlOutputText) findComponentInRoot("lblMonto");
		}
		return lblMonto;
	}

	protected HtmlInputText getTxtMonto() {
		if (txtMonto == null) {
			txtMonto = (HtmlInputText) findComponentInRoot("txtMonto");
		}
		return txtMonto;
	}

	protected HtmlDropDownList getDdlMoneda() {
		if (ddlMoneda == null) {
			ddlMoneda = (HtmlDropDownList) findComponentInRoot("ddlMoneda");
		}
		return ddlMoneda;
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

	protected HtmlColumn getCoIdmetodo() {
		if (coIdmetodo == null) {
			coIdmetodo = (HtmlColumn) findComponentInRoot("coIdmetodo");
		}
		return coIdmetodo;
	}

	protected HtmlOutputText getLblIdmetodo() {
		if (lblIdmetodo == null) {
			lblIdmetodo = (HtmlOutputText) findComponentInRoot("lblIdmetodo");
		}
		return lblIdmetodo;
	}

	protected HtmlColumn getCoMetodo() {
		if (coMetodo == null) {
			coMetodo = (HtmlColumn) findComponentInRoot("coMetodo");
		}
		return coMetodo;
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

	protected HtmlColumn getCoMonedaDetalle() {
		if (coMonedaDetalle == null) {
			coMonedaDetalle = (HtmlColumn) findComponentInRoot("coMonedaDetalle");
		}
		return coMonedaDetalle;
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

	protected HtmlColumn getCoMonto() {
		if (coMonto == null) {
			coMonto = (HtmlColumn) findComponentInRoot("coMonto");
		}
		return coMonto;
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

	protected HtmlColumn getCoTasa() {
		if (coTasa == null) {
			coTasa = (HtmlColumn) findComponentInRoot("coTasa");
		}
		return coTasa;
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

	protected HtmlColumn getCoEquivalente() {
		if (coEquivalente == null) {
			coEquivalente = (HtmlColumn) findComponentInRoot("coEquivalente");
		}
		return coEquivalente;
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

	protected HtmlColumn getCoReferencia() {
		if (coReferencia == null) {
			coReferencia = (HtmlColumn) findComponentInRoot("coReferencia");
		}
		return coReferencia;
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

	protected HtmlColumn getCoReferencia2() {
		if (coReferencia2 == null) {
			coReferencia2 = (HtmlColumn) findComponentInRoot("coReferencia2");
		}
		return coReferencia2;
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

	protected HtmlColumn getCoReferencia3() {
		if (coReferencia3 == null) {
			coReferencia3 = (HtmlColumn) findComponentInRoot("coReferencia3");
		}
		return coReferencia3;
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

	protected HtmlColumn getCoReferencia4() {
		if (coReferencia4 == null) {
			coReferencia4 = (HtmlColumn) findComponentInRoot("coReferencia4");
		}
		return coReferencia4;
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

	protected HtmlOutputText getLblMonAplicar() {
		if (lblMonAplicar == null) {
			lblMonAplicar = (HtmlOutputText) findComponentInRoot("lblMonAplicar");
		}
		return lblMonAplicar;
	}

	protected HtmlDropDownList getDdlMonedaAplicada() {
		if (ddlMonedaAplicada == null) {
			ddlMonedaAplicada = (HtmlDropDownList) findComponentInRoot("ddlMonedaAplicada");
		}
		return ddlMonedaAplicada;
	}

	protected HtmlOutputText getText4() {
		if (text4 == null) {
			text4 = (HtmlOutputText) findComponentInRoot("text4");
		}
		return text4;
	}

	protected HtmlDropDownList getCmbTipoProd() {
		if (cmbTipoProd == null) {
			cmbTipoProd = (HtmlDropDownList) findComponentInRoot("cmbTipoProd");
		}
		return cmbTipoProd;
	}

	protected HtmlOutputText getText6() {
		if (text6 == null) {
			text6 = (HtmlOutputText) findComponentInRoot("text6");
		}
		return text6;
	}

	protected HtmlDropDownList getCmbMarcas() {
		if (cmbMarcas == null) {
			cmbMarcas = (HtmlDropDownList) findComponentInRoot("cmbMarcas");
		}
		return cmbMarcas;
	}

	protected HtmlOutputText getText200() {
		if (text200 == null) {
			text200 = (HtmlOutputText) findComponentInRoot("text200");
		}
		return text200;
	}

	protected HtmlDropDownList getCmbModelos() {
		if (cmbModelos == null) {
			cmbModelos = (HtmlDropDownList) findComponentInRoot("cmbModelos");
		}
		return cmbModelos;
	}

	protected HtmlOutputText getText212() {
		if (text212 == null) {
			text212 = (HtmlOutputText) findComponentInRoot("text212");
		}
		return text212;
	}

	protected HtmlInputText getTxtNoItem() {
		if (txtNoItem == null) {
			txtNoItem = (HtmlInputText) findComponentInRoot("txtNoItem");
		}
		return txtNoItem;
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

	protected HtmlOutputText getLblCambioapl() {
		if (lblCambioapl == null) {
			lblCambioapl = (HtmlOutputText) findComponentInRoot("lblCambioapl");
		}
		return lblCambioapl;
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

	protected HtmlDialogWindowHeader getHdProcesaRecibo() {
		if (hdProcesaRecibo == null) {
			hdProcesaRecibo = (HtmlDialogWindowHeader) findComponentInRoot("hdProcesaRecibo");
		}
		return hdProcesaRecibo;
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

	protected HtmlJspPanel getJspProcesa() {
		if (jspProcesa == null) {
			jspProcesa = (HtmlJspPanel) findComponentInRoot("jspProcesa");
		}
		return jspProcesa;
	}

	protected HtmlLink getLnkCerrarPagoMensaje() {
		if (lnkCerrarPagoMensaje == null) {
			lnkCerrarPagoMensaje = (HtmlLink) findComponentInRoot("lnkCerrarPagoMensaje");
		}
		return lnkCerrarPagoMensaje;
	}

	protected HtmlDialogWindowAutoPostBackFlags getApbProcesaRecibo() {
		if (apbProcesaRecibo == null) {
			apbProcesaRecibo = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbProcesaRecibo");
		}
		return apbProcesaRecibo;
	}

	protected HtmlDialogWindow getDwImprime() {
		if (dwImprime == null) {
			dwImprime = (HtmlDialogWindow) findComponentInRoot("dwImprime");
		}
		return dwImprime;
	}

	protected HtmlDialogWindowHeader getHdImprime() {
		if (hdImprime == null) {
			hdImprime = (HtmlDialogWindowHeader) findComponentInRoot("hdImprime");
		}
		return hdImprime;
	}

	protected HtmlDialogWindowClientEvents getCleImprime() {
		if (cleImprime == null) {
			cleImprime = (HtmlDialogWindowClientEvents) findComponentInRoot("cleImprime");
		}
		return cleImprime;
	}

	protected HtmlDialogWindowRoundedCorners getRcImprime() {
		if (rcImprime == null) {
			rcImprime = (HtmlDialogWindowRoundedCorners) findComponentInRoot("rcImprime");
		}
		return rcImprime;
	}

	protected HtmlDialogWindowContentPane getCpImprime() {
		if (cpImprime == null) {
			cpImprime = (HtmlDialogWindowContentPane) findComponentInRoot("cpImprime");
		}
		return cpImprime;
	}

	protected HtmlPanelGrid getGrid100() {
		if (grid100 == null) {
			grid100 = (HtmlPanelGrid) findComponentInRoot("grid100");
		}
		return grid100;
	}

	protected HtmlGraphicImageEx getImageEx2Imprime() {
		if (imageEx2Imprime == null) {
			imageEx2Imprime = (HtmlGraphicImageEx) findComponentInRoot("imageEx2Imprime");
		}
		return imageEx2Imprime;
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

	protected HtmlDialogWindowAutoPostBackFlags getApbImprime() {
		if (apbImprime == null) {
			apbImprime = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbImprime");
		}
		return apbImprime;
	}

	protected HtmlDialogWindow getDwAutoriza() {
		if (dwAutoriza == null) {
			dwAutoriza = (HtmlDialogWindow) findComponentInRoot("dwAutoriza");
		}
		return dwAutoriza;
	}

	protected HtmlDialogWindowHeader getHdSolicitarAutorizacion() {
		if (hdSolicitarAutorizacion == null) {
			hdSolicitarAutorizacion = (HtmlDialogWindowHeader) findComponentInRoot("hdSolicitarAutorizacion");
		}
		return hdSolicitarAutorizacion;
	}

	protected HtmlDialogWindowClientEvents getCleAutorizaContado() {
		if (cleAutorizaContado == null) {
			cleAutorizaContado = (HtmlDialogWindowClientEvents) findComponentInRoot("cleAutorizaContado");
		}
		return cleAutorizaContado;
	}

	protected HtmlDialogWindowRoundedCorners getRcAutorizaContado() {
		if (rcAutorizaContado == null) {
			rcAutorizaContado = (HtmlDialogWindowRoundedCorners) findComponentInRoot("rcAutorizaContado");
		}
		return rcAutorizaContado;
	}

	protected HtmlDialogWindowContentPane getCpAutorizaContado() {
		if (cpAutorizaContado == null) {
			cpAutorizaContado = (HtmlDialogWindowContentPane) findComponentInRoot("cpAutorizaContado");
		}
		return cpAutorizaContado;
	}

	protected HtmlOutputText getLblMensajeAutorizacion() {
		if (lblMensajeAutorizacion == null) {
			lblMensajeAutorizacion = (HtmlOutputText) findComponentInRoot("lblMensajeAutorizacion");
		}
		return lblMensajeAutorizacion;
	}

	protected HtmlPanelGrid getGrid2() {
		if (grid2 == null) {
			grid2 = (HtmlPanelGrid) findComponentInRoot("grid2");
		}
		return grid2;
	}

	protected HtmlOutputText getLblReferencia4() {
		if (lblReferencia4 == null) {
			lblReferencia4 = (HtmlOutputText) findComponentInRoot("lblReferencia4");
		}
		return lblReferencia4;
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

	protected HtmlDropDownList getDdlAutoriza() {
		if (ddlAutoriza == null) {
			ddlAutoriza = (HtmlDropDownList) findComponentInRoot("ddlAutoriza");
		}
		return ddlAutoriza;
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

	protected HtmlLink getLnkProcesarSolicitud() {
		if (lnkProcesarSolicitud == null) {
			lnkProcesarSolicitud = (HtmlLink) findComponentInRoot("lnkProcesarSolicitud");
		}
		return lnkProcesarSolicitud;
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

	protected UINamingContainer getVfPrimaReserva() {
		if (vfPrimaReserva == null) {
			vfPrimaReserva = (UINamingContainer) findComponentInRoot("vfPrimaReserva");
		}
		return vfPrimaReserva;
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

	protected HtmlLink getLnkSearchContrato() {
		if (lnkSearchContrato == null) {
			lnkSearchContrato = (HtmlLink) findComponentInRoot("lnkSearchContrato");
		}
		return lnkSearchContrato;
	}

	protected HtmlCheckBox getChkContrato() {
		if (chkContrato == null) {
			chkContrato = (HtmlCheckBox) findComponentInRoot("chkContrato");
		}
		return chkContrato;
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

	protected HtmlOutputText getLblTipodoc() {
		if (lblTipodoc == null) {
			lblTipodoc = (HtmlOutputText) findComponentInRoot("lblTipodoc");
		}
		return lblTipodoc;
	}

	protected HtmlDropDownList getDdlTipodoc() {
		if (ddlTipodoc == null) {
			ddlTipodoc = (HtmlDropDownList) findComponentInRoot("ddlTipodoc");
		}
		return ddlTipodoc;
	}

}
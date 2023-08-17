/**
 * 
 */
package pagecode.tilesContent;

import pagecode.PageCodeBase;
import com.infragistics.faces.shared.component.html.HtmlLink;
import com.infragistics.faces.window.component.html.HtmlDialogWindowClientEvents;
import com.ibm.faces.component.html.HtmlJspPanel;
import com.ibm.faces.component.html.HtmlGraphicImageEx;
import com.infragistics.faces.window.component.html.HtmlDialogWindow;
import com.infragistics.faces.window.component.html.HtmlDialogWindowRoundedCorners;
import com.infragistics.faces.window.component.html.HtmlDialogWindowContentPane;
import com.infragistics.faces.window.component.html.HtmlDialogWindowAutoPostBackFlags;
import com.infragistics.faces.input.component.html.HtmlDropDownListClientEvents;
import javax.faces.component.html.HtmlOutputText;
import com.infragistics.faces.window.component.html.HtmlDialogWindowHeader;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
import javax.faces.component.html.HtmlInputText;
import com.infragistics.faces.input.component.html.HtmlDateChooser;
import com.infragistics.faces.input.component.html.HtmlCheckBox;
import com.infragistics.faces.grid.component.html.HtmlGridView;
import com.infragistics.faces.grid.component.html.HtmlColumn;
import javax.faces.component.html.HtmlPanelGroup;
import com.infragistics.faces.grid.component.html.HtmlGridAgFunction;
import javax.faces.component.html.HtmlInputTextarea;

/**
 * @author Juan Ñamendi
 *
 */
public class Cr_bodyarea extends PageCodeBase {

	protected HtmlLink lnkReimprimir;
	protected HtmlLink lnkCerrarDetalleContado;
	protected HtmlDialogWindowClientEvents cledwCargando;
	protected HtmlJspPanel jspdwCargando;
	protected HtmlGraphicImageEx imagenCargando;
	protected HtmlDialogWindow dwCargando;
	protected HtmlDialogWindowRoundedCorners cledwCargando22;
	protected HtmlDialogWindowContentPane cpdwCargando;
	protected HtmlDialogWindowAutoPostBackFlags apbdwCargando;
	protected HtmlDropDownListClientEvents cleddlTipoReciboCre;
	protected HtmlDropDownListClientEvents cleddlTipoRecibo22Cre;
	protected HtmlOutputText lblCodcomp1f44;
	protected HtmlLink lnkReimprimirVouch;
	protected HtmlDialogWindowHeader hdDdwProbarConexion;
	protected HtmlJspPanel jspPdwProbarConexion4;
	protected HtmlOutputText txtTDwrespuesta;
	protected HtmlLink lnkCdwProbarConexionado;
	protected HtmlDialogWindow dwProbarConexion;
	protected HtmlDialogWindowClientEvents clDedwProbarConexion;
	protected HtmlDialogWindowRoundedCorners crdwProbarConexion;
	protected HtmlDialogWindowContentPane cnpDedwProbarConexion;
	protected HtmlDialogWindowAutoPostBackFlags apbDetwProbarConexion;
	protected HtmlLink lnkProbarConexio;
	protected HtmlOutputText lblTituloContado;
	protected HtmlDropDownList dropBusqueda;
	protected HtmlInputText txtParametro;
	protected HtmlDateChooser dcFechaInicial;
	protected HtmlDateChooser dcFechaFinal;
	protected HtmlDropDownList ddlCajas;
	protected HtmlCheckBox chkAnulados;
	protected HtmlOutputText lblFiltroMoneda;
	protected HtmlDropDownList cmbFiltroMonedas;
	protected HtmlOutputText lblFiltroFactura;
	protected HtmlDropDownList cmbFiltroFacturas;
	protected HtmlGraphicImageEx imgLoader;
	protected HtmlGridView gvFacsContado;
	protected HtmlColumn coLnkDetalle;
	protected HtmlLink lnkExport;
	protected HtmlColumn coNoFactura;
	protected HtmlOutputText lblnofactura1Grande;
	protected HtmlOutputText lblnofactura2Grande;
	protected HtmlColumn coTipoFactura;
	protected HtmlOutputText lblTipofactura1Grande;
	protected HtmlOutputText lblTipofactura2Grande;
	protected HtmlColumn coNomCli;
	protected HtmlOutputText lblNomCli1Grande;
	protected HtmlOutputText lblNomCli2Grande;
	protected HtmlColumn coTotal;
	protected HtmlOutputText lblTotal1Grande;
	protected HtmlOutputText lblTotal2Grande;
	protected HtmlColumn coFechaGrande;
	protected HtmlOutputText lblFecha1Grande;
	protected HtmlOutputText lblFecha2Grande;
	protected HtmlColumn coMoneda;
	protected HtmlOutputText lblMoneda1Grande;
	protected HtmlOutputText lblMoneda2Grande;
	protected HtmlOutputText lblParte6;
	protected HtmlGridAgFunction agPrueba;
	protected HtmlColumn coCodcomp;
	protected HtmlOutputText lblCodcomp1;
	protected HtmlOutputText lblCodcomp2;
	protected HtmlLink lnkVerResumen;
	protected HtmlOutputText lblConcepto;
	protected HtmlInputTextarea txtConcepto;
	protected HtmlOutputText lblSubtotalDetalleContado;
	protected HtmlOutputText txtSubtotalDetalle;
	protected HtmlOutputText text28;
	protected HtmlOutputText txtIvaDetalle;
	protected HtmlOutputText lblTotalDetCont;
	protected HtmlOutputText txtTotalDetalle;
	protected HtmlDialogWindowHeader hdDdwResumen;
	protected HtmlJspPanel jspPdwResumen;
	protected HtmlOutputText lblEfectivoCOR;
	protected HtmlOutputText lblChequeCOR;
	protected HtmlOutputText lblTarjetaCOR;
	protected HtmlOutputText lblTransfCOR;
	protected HtmlOutputText lblDepositoCOR;
	protected HtmlOutputText lblTotalCOR;
	protected HtmlOutputText lblEfectivoUSD;
	protected HtmlOutputText lblChequeUSD;
	protected HtmlOutputText lblTarjetaUSD;
	protected HtmlOutputText lblTransfUSD;
	protected HtmlOutputText lblDepositoUSD;
	protected HtmlOutputText lblTotalUSD;
	protected HtmlLink lnkCdwResumen;
	protected HtmlDialogWindow dwResumen;
	protected HtmlDialogWindowClientEvents clDedwResumen;
	protected HtmlDialogWindowRoundedCorners crdwResumen;
	protected HtmlDialogWindowContentPane cnpDedwResumen;
	protected HtmlDialogWindowAutoPostBackFlags apbDetwPResumen;
	protected HtmlCheckBox chkManuales;
	protected HtmlOutputText lblNoCaja1Grande;
	protected HtmlLink lnkDetalleFacturaContado;
	protected HtmlOutputText lblNoCaja2Grande;
	protected HtmlOutputText txtMensaje;
	protected HtmlGridAgFunction agFnContarRecCaja;
	protected HtmlPanelGroup grpParte4;
	protected HtmlPanelGroup grpParteMonto3;
	protected HtmlGridAgFunction agFnSumRecCaja;
	protected HtmlOutputText frmLabel2;
	protected HtmlLink lnkSearchContado;
	protected HtmlColumn coNoRecManual;
	protected HtmlOutputText lblNumrecman;
	protected HtmlOutputText lblNumrecman1;
	protected HtmlLink getLnkReimprimir() {
		if (lnkReimprimir == null) {
			lnkReimprimir = (HtmlLink) findComponentInRoot("lnkReimprimir");
		}
		return lnkReimprimir;
	}

	protected HtmlLink getLnkCerrarDetalleContado() {
		if (lnkCerrarDetalleContado == null) {
			lnkCerrarDetalleContado = (HtmlLink) findComponentInRoot("lnkCerrarDetalleContado");
		}
		return lnkCerrarDetalleContado;
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

	protected HtmlDropDownListClientEvents getCleddlTipoReciboCre() {
		if (cleddlTipoReciboCre == null) {
			cleddlTipoReciboCre = (HtmlDropDownListClientEvents) findComponentInRoot("cleddlTipoReciboCre");
		}
		return cleddlTipoReciboCre;
	}

	protected HtmlDropDownListClientEvents getCleddlTipoRecibo22Cre() {
		if (cleddlTipoRecibo22Cre == null) {
			cleddlTipoRecibo22Cre = (HtmlDropDownListClientEvents) findComponentInRoot("cleddlTipoRecibo22Cre");
		}
		return cleddlTipoRecibo22Cre;
	}

	protected HtmlOutputText getLblCodcomp1f44() {
		if (lblCodcomp1f44 == null) {
			lblCodcomp1f44 = (HtmlOutputText) findComponentInRoot("lblCodcomp1f44");
		}
		return lblCodcomp1f44;
	}

	protected HtmlLink getLnkReimprimirVouch() {
		if (lnkReimprimirVouch == null) {
			lnkReimprimirVouch = (HtmlLink) findComponentInRoot("lnkReimprimirVouch");
		}
		return lnkReimprimirVouch;
	}

	protected HtmlDialogWindowHeader getHdDdwProbarConexion() {
		if (hdDdwProbarConexion == null) {
			hdDdwProbarConexion = (HtmlDialogWindowHeader) findComponentInRoot("hdDdwProbarConexion");
		}
		return hdDdwProbarConexion;
	}

	protected HtmlJspPanel getJspPdwProbarConexion4() {
		if (jspPdwProbarConexion4 == null) {
			jspPdwProbarConexion4 = (HtmlJspPanel) findComponentInRoot("jspPdwProbarConexion4");
		}
		return jspPdwProbarConexion4;
	}

	protected HtmlOutputText getTxtTDwrespuesta() {
		if (txtTDwrespuesta == null) {
			txtTDwrespuesta = (HtmlOutputText) findComponentInRoot("txtTDwrespuesta");
		}
		return txtTDwrespuesta;
	}

	protected HtmlLink getLnkCdwProbarConexionado() {
		if (lnkCdwProbarConexionado == null) {
			lnkCdwProbarConexionado = (HtmlLink) findComponentInRoot("lnkCdwProbarConexionado");
		}
		return lnkCdwProbarConexionado;
	}

	protected HtmlDialogWindow getDwProbarConexion() {
		if (dwProbarConexion == null) {
			dwProbarConexion = (HtmlDialogWindow) findComponentInRoot("dwProbarConexion");
		}
		return dwProbarConexion;
	}

	protected HtmlDialogWindowClientEvents getClDedwProbarConexion() {
		if (clDedwProbarConexion == null) {
			clDedwProbarConexion = (HtmlDialogWindowClientEvents) findComponentInRoot("clDedwProbarConexion");
		}
		return clDedwProbarConexion;
	}

	protected HtmlDialogWindowRoundedCorners getCrdwProbarConexion() {
		if (crdwProbarConexion == null) {
			crdwProbarConexion = (HtmlDialogWindowRoundedCorners) findComponentInRoot("crdwProbarConexion");
		}
		return crdwProbarConexion;
	}

	protected HtmlDialogWindowContentPane getCnpDedwProbarConexion() {
		if (cnpDedwProbarConexion == null) {
			cnpDedwProbarConexion = (HtmlDialogWindowContentPane) findComponentInRoot("cnpDedwProbarConexion");
		}
		return cnpDedwProbarConexion;
	}

	protected HtmlDialogWindowAutoPostBackFlags getApbDetwProbarConexion() {
		if (apbDetwProbarConexion == null) {
			apbDetwProbarConexion = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbDetwProbarConexion");
		}
		return apbDetwProbarConexion;
	}

	protected HtmlLink getLnkProbarConexio() {
		if (lnkProbarConexio == null) {
			lnkProbarConexio = (HtmlLink) findComponentInRoot("lnkProbarConexio");
		}
		return lnkProbarConexio;
	}

	protected HtmlOutputText getLblTituloContado() {
		if (lblTituloContado == null) {
			lblTituloContado = (HtmlOutputText) findComponentInRoot("lblTituloContado");
		}
		return lblTituloContado;
	}

	protected HtmlDropDownList getDropBusqueda() {
		if (dropBusqueda == null) {
			dropBusqueda = (HtmlDropDownList) findComponentInRoot("dropBusqueda");
		}
		return dropBusqueda;
	}

	protected HtmlInputText getTxtParametro() {
		if (txtParametro == null) {
			txtParametro = (HtmlInputText) findComponentInRoot("txtParametro");
		}
		return txtParametro;
	}

	protected HtmlDateChooser getDcFechaInicial() {
		if (dcFechaInicial == null) {
			dcFechaInicial = (HtmlDateChooser) findComponentInRoot("dcFechaInicial");
		}
		return dcFechaInicial;
	}

	protected HtmlDateChooser getDcFechaFinal() {
		if (dcFechaFinal == null) {
			dcFechaFinal = (HtmlDateChooser) findComponentInRoot("dcFechaFinal");
		}
		return dcFechaFinal;
	}

	protected HtmlDropDownList getDdlCajas() {
		if (ddlCajas == null) {
			ddlCajas = (HtmlDropDownList) findComponentInRoot("ddlCajas");
		}
		return ddlCajas;
	}

	protected HtmlCheckBox getChkAnulados() {
		if (chkAnulados == null) {
			chkAnulados = (HtmlCheckBox) findComponentInRoot("chkAnulados");
		}
		return chkAnulados;
	}

	protected HtmlOutputText getLblFiltroMoneda() {
		if (lblFiltroMoneda == null) {
			lblFiltroMoneda = (HtmlOutputText) findComponentInRoot("lblFiltroMoneda");
		}
		return lblFiltroMoneda;
	}

	protected HtmlDropDownList getCmbFiltroMonedas() {
		if (cmbFiltroMonedas == null) {
			cmbFiltroMonedas = (HtmlDropDownList) findComponentInRoot("cmbFiltroMonedas");
		}
		return cmbFiltroMonedas;
	}

	protected HtmlOutputText getLblFiltroFactura() {
		if (lblFiltroFactura == null) {
			lblFiltroFactura = (HtmlOutputText) findComponentInRoot("lblFiltroFactura");
		}
		return lblFiltroFactura;
	}

	protected HtmlDropDownList getCmbFiltroFacturas() {
		if (cmbFiltroFacturas == null) {
			cmbFiltroFacturas = (HtmlDropDownList) findComponentInRoot("cmbFiltroFacturas");
		}
		return cmbFiltroFacturas;
	}

	protected HtmlGraphicImageEx getImgLoader() {
		if (imgLoader == null) {
			imgLoader = (HtmlGraphicImageEx) findComponentInRoot("imgLoader");
		}
		return imgLoader;
	}

	protected HtmlGridView getGvFacsContado() {
		if (gvFacsContado == null) {
			gvFacsContado = (HtmlGridView) findComponentInRoot("gvFacsContado");
		}
		return gvFacsContado;
	}

	protected HtmlColumn getCoLnkDetalle() {
		if (coLnkDetalle == null) {
			coLnkDetalle = (HtmlColumn) findComponentInRoot("coLnkDetalle");
		}
		return coLnkDetalle;
	}

	protected HtmlLink getLnkExport() {
		if (lnkExport == null) {
			lnkExport = (HtmlLink) findComponentInRoot("lnkExport");
		}
		return lnkExport;
	}

	protected HtmlColumn getCoNoFactura() {
		if (coNoFactura == null) {
			coNoFactura = (HtmlColumn) findComponentInRoot("coNoFactura");
		}
		return coNoFactura;
	}

	protected HtmlOutputText getLblnofactura1Grande() {
		if (lblnofactura1Grande == null) {
			lblnofactura1Grande = (HtmlOutputText) findComponentInRoot("lblnofactura1Grande");
		}
		return lblnofactura1Grande;
	}

	protected HtmlOutputText getLblnofactura2Grande() {
		if (lblnofactura2Grande == null) {
			lblnofactura2Grande = (HtmlOutputText) findComponentInRoot("lblnofactura2Grande");
		}
		return lblnofactura2Grande;
	}

	protected HtmlColumn getCoTipoFactura() {
		if (coTipoFactura == null) {
			coTipoFactura = (HtmlColumn) findComponentInRoot("coTipoFactura");
		}
		return coTipoFactura;
	}

	protected HtmlOutputText getLblTipofactura1Grande() {
		if (lblTipofactura1Grande == null) {
			lblTipofactura1Grande = (HtmlOutputText) findComponentInRoot("lblTipofactura1Grande");
		}
		return lblTipofactura1Grande;
	}

	protected HtmlOutputText getLblTipofactura2Grande() {
		if (lblTipofactura2Grande == null) {
			lblTipofactura2Grande = (HtmlOutputText) findComponentInRoot("lblTipofactura2Grande");
		}
		return lblTipofactura2Grande;
	}

	protected HtmlColumn getCoNomCli() {
		if (coNomCli == null) {
			coNomCli = (HtmlColumn) findComponentInRoot("coNomCli");
		}
		return coNomCli;
	}

	protected HtmlOutputText getLblNomCli1Grande() {
		if (lblNomCli1Grande == null) {
			lblNomCli1Grande = (HtmlOutputText) findComponentInRoot("lblNomCli1Grande");
		}
		return lblNomCli1Grande;
	}

	protected HtmlOutputText getLblNomCli2Grande() {
		if (lblNomCli2Grande == null) {
			lblNomCli2Grande = (HtmlOutputText) findComponentInRoot("lblNomCli2Grande");
		}
		return lblNomCli2Grande;
	}

	protected HtmlColumn getCoTotal() {
		if (coTotal == null) {
			coTotal = (HtmlColumn) findComponentInRoot("coTotal");
		}
		return coTotal;
	}

	protected HtmlOutputText getLblTotal1Grande() {
		if (lblTotal1Grande == null) {
			lblTotal1Grande = (HtmlOutputText) findComponentInRoot("lblTotal1Grande");
		}
		return lblTotal1Grande;
	}

	protected HtmlOutputText getLblTotal2Grande() {
		if (lblTotal2Grande == null) {
			lblTotal2Grande = (HtmlOutputText) findComponentInRoot("lblTotal2Grande");
		}
		return lblTotal2Grande;
	}

	protected HtmlColumn getCoFechaGrande() {
		if (coFechaGrande == null) {
			coFechaGrande = (HtmlColumn) findComponentInRoot("coFechaGrande");
		}
		return coFechaGrande;
	}

	protected HtmlOutputText getLblFecha1Grande() {
		if (lblFecha1Grande == null) {
			lblFecha1Grande = (HtmlOutputText) findComponentInRoot("lblFecha1Grande");
		}
		return lblFecha1Grande;
	}

	protected HtmlOutputText getLblFecha2Grande() {
		if (lblFecha2Grande == null) {
			lblFecha2Grande = (HtmlOutputText) findComponentInRoot("lblFecha2Grande");
		}
		return lblFecha2Grande;
	}

	protected HtmlColumn getCoMoneda() {
		if (coMoneda == null) {
			coMoneda = (HtmlColumn) findComponentInRoot("coMoneda");
		}
		return coMoneda;
	}

	protected HtmlOutputText getLblMoneda1Grande() {
		if (lblMoneda1Grande == null) {
			lblMoneda1Grande = (HtmlOutputText) findComponentInRoot("lblMoneda1Grande");
		}
		return lblMoneda1Grande;
	}

	protected HtmlOutputText getLblMoneda2Grande() {
		if (lblMoneda2Grande == null) {
			lblMoneda2Grande = (HtmlOutputText) findComponentInRoot("lblMoneda2Grande");
		}
		return lblMoneda2Grande;
	}

	protected HtmlOutputText getLblParte6() {
		if (lblParte6 == null) {
			lblParte6 = (HtmlOutputText) findComponentInRoot("lblParte6");
		}
		return lblParte6;
	}

	protected HtmlGridAgFunction getAgPrueba() {
		if (agPrueba == null) {
			agPrueba = (HtmlGridAgFunction) findComponentInRoot("agPrueba");
		}
		return agPrueba;
	}

	protected HtmlColumn getCoCodcomp() {
		if (coCodcomp == null) {
			coCodcomp = (HtmlColumn) findComponentInRoot("coCodcomp");
		}
		return coCodcomp;
	}

	protected HtmlOutputText getLblCodcomp1() {
		if (lblCodcomp1 == null) {
			lblCodcomp1 = (HtmlOutputText) findComponentInRoot("lblCodcomp1");
		}
		return lblCodcomp1;
	}

	protected HtmlOutputText getLblCodcomp2() {
		if (lblCodcomp2 == null) {
			lblCodcomp2 = (HtmlOutputText) findComponentInRoot("lblCodcomp2");
		}
		return lblCodcomp2;
	}

	protected HtmlLink getLnkVerResumen() {
		if (lnkVerResumen == null) {
			lnkVerResumen = (HtmlLink) findComponentInRoot("lnkVerResumen");
		}
		return lnkVerResumen;
	}

	protected HtmlOutputText getLblConcepto() {
		if (lblConcepto == null) {
			lblConcepto = (HtmlOutputText) findComponentInRoot("lblConcepto");
		}
		return lblConcepto;
	}

	protected HtmlInputTextarea getTxtConcepto() {
		if (txtConcepto == null) {
			txtConcepto = (HtmlInputTextarea) findComponentInRoot("txtConcepto");
		}
		return txtConcepto;
	}

	protected HtmlOutputText getLblSubtotalDetalleContado() {
		if (lblSubtotalDetalleContado == null) {
			lblSubtotalDetalleContado = (HtmlOutputText) findComponentInRoot("lblSubtotalDetalleContado");
		}
		return lblSubtotalDetalleContado;
	}

	protected HtmlOutputText getTxtSubtotalDetalle() {
		if (txtSubtotalDetalle == null) {
			txtSubtotalDetalle = (HtmlOutputText) findComponentInRoot("txtSubtotalDetalle");
		}
		return txtSubtotalDetalle;
	}

	protected HtmlOutputText getText28() {
		if (text28 == null) {
			text28 = (HtmlOutputText) findComponentInRoot("text28");
		}
		return text28;
	}

	protected HtmlOutputText getTxtIvaDetalle() {
		if (txtIvaDetalle == null) {
			txtIvaDetalle = (HtmlOutputText) findComponentInRoot("txtIvaDetalle");
		}
		return txtIvaDetalle;
	}

	protected HtmlOutputText getLblTotalDetCont() {
		if (lblTotalDetCont == null) {
			lblTotalDetCont = (HtmlOutputText) findComponentInRoot("lblTotalDetCont");
		}
		return lblTotalDetCont;
	}

	protected HtmlOutputText getTxtTotalDetalle() {
		if (txtTotalDetalle == null) {
			txtTotalDetalle = (HtmlOutputText) findComponentInRoot("txtTotalDetalle");
		}
		return txtTotalDetalle;
	}

	protected HtmlDialogWindowHeader getHdDdwResumen() {
		if (hdDdwResumen == null) {
			hdDdwResumen = (HtmlDialogWindowHeader) findComponentInRoot("hdDdwResumen");
		}
		return hdDdwResumen;
	}

	protected HtmlJspPanel getJspPdwResumen() {
		if (jspPdwResumen == null) {
			jspPdwResumen = (HtmlJspPanel) findComponentInRoot("jspPdwResumen");
		}
		return jspPdwResumen;
	}

	protected HtmlOutputText getLblEfectivoCOR() {
		if (lblEfectivoCOR == null) {
			lblEfectivoCOR = (HtmlOutputText) findComponentInRoot("lblEfectivoCOR");
		}
		return lblEfectivoCOR;
	}

	protected HtmlOutputText getLblChequeCOR() {
		if (lblChequeCOR == null) {
			lblChequeCOR = (HtmlOutputText) findComponentInRoot("lblChequeCOR");
		}
		return lblChequeCOR;
	}

	protected HtmlOutputText getLblTarjetaCOR() {
		if (lblTarjetaCOR == null) {
			lblTarjetaCOR = (HtmlOutputText) findComponentInRoot("lblTarjetaCOR");
		}
		return lblTarjetaCOR;
	}

	protected HtmlOutputText getLblTransfCOR() {
		if (lblTransfCOR == null) {
			lblTransfCOR = (HtmlOutputText) findComponentInRoot("lblTransfCOR");
		}
		return lblTransfCOR;
	}

	protected HtmlOutputText getLblDepositoCOR() {
		if (lblDepositoCOR == null) {
			lblDepositoCOR = (HtmlOutputText) findComponentInRoot("lblDepositoCOR");
		}
		return lblDepositoCOR;
	}

	protected HtmlOutputText getLblTotalCOR() {
		if (lblTotalCOR == null) {
			lblTotalCOR = (HtmlOutputText) findComponentInRoot("lblTotalCOR");
		}
		return lblTotalCOR;
	}

	protected HtmlOutputText getLblEfectivoUSD() {
		if (lblEfectivoUSD == null) {
			lblEfectivoUSD = (HtmlOutputText) findComponentInRoot("lblEfectivoUSD");
		}
		return lblEfectivoUSD;
	}

	protected HtmlOutputText getLblChequeUSD() {
		if (lblChequeUSD == null) {
			lblChequeUSD = (HtmlOutputText) findComponentInRoot("lblChequeUSD");
		}
		return lblChequeUSD;
	}

	protected HtmlOutputText getLblTarjetaUSD() {
		if (lblTarjetaUSD == null) {
			lblTarjetaUSD = (HtmlOutputText) findComponentInRoot("lblTarjetaUSD");
		}
		return lblTarjetaUSD;
	}

	protected HtmlOutputText getLblTransfUSD() {
		if (lblTransfUSD == null) {
			lblTransfUSD = (HtmlOutputText) findComponentInRoot("lblTransfUSD");
		}
		return lblTransfUSD;
	}

	protected HtmlOutputText getLblDepositoUSD() {
		if (lblDepositoUSD == null) {
			lblDepositoUSD = (HtmlOutputText) findComponentInRoot("lblDepositoUSD");
		}
		return lblDepositoUSD;
	}

	protected HtmlOutputText getLblTotalUSD() {
		if (lblTotalUSD == null) {
			lblTotalUSD = (HtmlOutputText) findComponentInRoot("lblTotalUSD");
		}
		return lblTotalUSD;
	}

	protected HtmlLink getLnkCdwResumen() {
		if (lnkCdwResumen == null) {
			lnkCdwResumen = (HtmlLink) findComponentInRoot("lnkCdwResumen");
		}
		return lnkCdwResumen;
	}

	protected HtmlDialogWindow getDwResumen() {
		if (dwResumen == null) {
			dwResumen = (HtmlDialogWindow) findComponentInRoot("dwResumen");
		}
		return dwResumen;
	}

	protected HtmlDialogWindowClientEvents getClDedwResumen() {
		if (clDedwResumen == null) {
			clDedwResumen = (HtmlDialogWindowClientEvents) findComponentInRoot("clDedwResumen");
		}
		return clDedwResumen;
	}

	protected HtmlDialogWindowRoundedCorners getCrdwResumen() {
		if (crdwResumen == null) {
			crdwResumen = (HtmlDialogWindowRoundedCorners) findComponentInRoot("crdwResumen");
		}
		return crdwResumen;
	}

	protected HtmlDialogWindowContentPane getCnpDedwResumen() {
		if (cnpDedwResumen == null) {
			cnpDedwResumen = (HtmlDialogWindowContentPane) findComponentInRoot("cnpDedwResumen");
		}
		return cnpDedwResumen;
	}

	protected HtmlDialogWindowAutoPostBackFlags getApbDetwPResumen() {
		if (apbDetwPResumen == null) {
			apbDetwPResumen = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbDetwPResumen");
		}
		return apbDetwPResumen;
	}

	protected HtmlCheckBox getChkManuales() {
		if (chkManuales == null) {
			chkManuales = (HtmlCheckBox) findComponentInRoot("chkManuales");
		}
		return chkManuales;
	}

	protected HtmlOutputText getLblNoCaja1Grande() {
		if (lblNoCaja1Grande == null) {
			lblNoCaja1Grande = (HtmlOutputText) findComponentInRoot("lblNoCaja1Grande");
		}
		return lblNoCaja1Grande;
	}

	protected HtmlLink getLnkDetalleFacturaContado() {
		if (lnkDetalleFacturaContado == null) {
			lnkDetalleFacturaContado = (HtmlLink) findComponentInRoot("lnkDetalleFacturaContado");
		}
		return lnkDetalleFacturaContado;
	}

	protected HtmlOutputText getLblNoCaja2Grande() {
		if (lblNoCaja2Grande == null) {
			lblNoCaja2Grande = (HtmlOutputText) findComponentInRoot("lblNoCaja2Grande");
		}
		return lblNoCaja2Grande;
	}

	protected HtmlOutputText getTxtMensaje() {
		if (txtMensaje == null) {
			txtMensaje = (HtmlOutputText) findComponentInRoot("txtMensaje");
		}
		return txtMensaje;
	}

	protected HtmlGridAgFunction getAgFnContarRecCaja() {
		if (agFnContarRecCaja == null) {
			agFnContarRecCaja = (HtmlGridAgFunction) findComponentInRoot("agFnContarRecCaja");
		}
		return agFnContarRecCaja;
	}

	protected HtmlPanelGroup getGrpParte4() {
		if (grpParte4 == null) {
			grpParte4 = (HtmlPanelGroup) findComponentInRoot("grpParte4");
		}
		return grpParte4;
	}

	protected HtmlPanelGroup getGrpParteMonto3() {
		if (grpParteMonto3 == null) {
			grpParteMonto3 = (HtmlPanelGroup) findComponentInRoot("grpParteMonto3");
		}
		return grpParteMonto3;
	}

	protected HtmlGridAgFunction getAgFnSumRecCaja() {
		if (agFnSumRecCaja == null) {
			agFnSumRecCaja = (HtmlGridAgFunction) findComponentInRoot("agFnSumRecCaja");
		}
		return agFnSumRecCaja;
	}

	protected HtmlOutputText getFrmLabel2() {
		if (frmLabel2 == null) {
			frmLabel2 = (HtmlOutputText) findComponentInRoot("frmLabel2");
		}
		return frmLabel2;
	}

	protected HtmlLink getLnkSearchContado() {
		if (lnkSearchContado == null) {
			lnkSearchContado = (HtmlLink) findComponentInRoot("lnkSearchContado");
		}
		return lnkSearchContado;
	}

	protected HtmlColumn getCoNoRecManual() {
		if (coNoRecManual == null) {
			coNoRecManual = (HtmlColumn) findComponentInRoot("coNoRecManual");
		}
		return coNoRecManual;
	}

	protected HtmlOutputText getLblNumrecman() {
		if (lblNumrecman == null) {
			lblNumrecman = (HtmlOutputText) findComponentInRoot("lblNumrecman");
		}
		return lblNumrecman;
	}

	protected HtmlOutputText getLblNumrecman1() {
		if (lblNumrecman1 == null) {
			lblNumrecman1 = (HtmlOutputText) findComponentInRoot("lblNumrecman1");
		}
		return lblNumrecman1;
	}

}
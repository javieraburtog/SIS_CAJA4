/**
 * 
 */
package pagecode.recibos;

import pagecode.PageCodeBase;
import com.ibm.faces.component.html.HtmlScriptCollector;
import javax.faces.component.html.HtmlForm;
import com.infragistics.faces.menu.component.html.HtmlMenu;
import com.infragistics.faces.menu.component.html.HtmlMenuItem;
import javax.faces.component.html.HtmlOutputText;
import com.ibm.faces.component.html.HtmlPanelSection;
import com.ibm.faces.component.html.HtmlJspPanel;
import com.ibm.faces.component.html.HtmlGraphicImageEx;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
import javax.faces.component.html.HtmlInputText;
import com.ibm.faces.component.html.HtmlInputHelperTypeahead;
import com.infragistics.faces.grid.component.html.HtmlGridView;
import com.infragistics.faces.grid.component.html.HtmlColumn;
import com.infragistics.faces.shared.component.html.HtmlLink;
import javax.faces.component.html.HtmlPanelGroup;
import com.infragistics.faces.grid.component.html.HtmlGridAgFunction;
import javax.faces.component.html.HtmlPanelGrid;
import com.infragistics.faces.window.component.html.HtmlDialogWindowHeader;
import com.infragistics.faces.window.component.html.HtmlDialogWindow;
import com.infragistics.faces.window.component.html.HtmlDialogWindowClientEvents;
import com.infragistics.faces.window.component.html.HtmlDialogWindowRoundedCorners;
import com.infragistics.faces.window.component.html.HtmlDialogWindowContentPane;
import com.infragistics.faces.window.component.html.HtmlDialogWindowAutoPostBackFlags;

/**
 * @author Juan Carlos
 *
 */
public class FactDiaria extends PageCodeBase {

	protected HtmlScriptCollector scDiaria;
	protected HtmlForm frmDiaria;
	protected HtmlMenu menu1;
	protected HtmlMenuItem item0;
	protected HtmlMenuItem item1;
	protected HtmlMenuItem item2;
	protected HtmlOutputText lblTituloContado;
	protected HtmlPanelSection secContado1;
	protected HtmlJspPanel jspPanel7;
	protected HtmlGraphicImageEx imageEx4Cont;
	protected HtmlOutputText txtBusquedaContado;
	protected HtmlJspPanel jspPanel6;
	protected HtmlGraphicImageEx imageEx3;
	protected HtmlOutputText txtBusquedaContado2;
	protected HtmlJspPanel jspPanel100;
	protected HtmlOutputText lblTipoBusqueda;
	protected HtmlDropDownList dropBusqueda;
	protected HtmlOutputText lblparametroh;
	protected HtmlInputText txtParametro;
	protected HtmlInputHelperTypeahead tphContado;
	protected HtmlGraphicImageEx imgLoader;
	protected HtmlOutputText txtMensaje;
	protected HtmlGridView gvFacsContado;
	protected HtmlColumn coLnkDetalle;
	protected HtmlLink lnkDetalleFacturaContado;
	protected HtmlOutputText lblDetalleFacturaGrande;
	protected HtmlColumn coNoFactura;
	protected HtmlOutputText lblnofactura1Grande;
	protected HtmlOutputText lblnofactura2Grande;
	protected HtmlColumn coTipoFactura;
	protected HtmlOutputText lblTipofactura1Grande;
	protected HtmlOutputText lblTipofactura2Grande;
	protected HtmlColumn coNomCli;
	protected HtmlOutputText lblNomCli1Grande;
	protected HtmlOutputText lblNomCli2Grande;
	protected HtmlColumn coUniNeg;
	protected HtmlOutputText lblUnineg1Grande;
	protected HtmlOutputText lblUnineg2Grande;
	protected HtmlOutputText lblHeader;
	protected HtmlColumn coTotal;
	protected HtmlOutputText lblTotal1Grande;
	protected HtmlOutputText lblTotal2Grande;
	protected HtmlColumn coMoneda;
	protected HtmlOutputText lblMoneda1Grande;
	protected HtmlOutputText lblMoneda2Grande;
	protected HtmlColumn coFecha;
	protected HtmlOutputText lblFecha1Grande;
	protected HtmlOutputText lblFecha2Grande;
	protected HtmlPanelGroup grpParte4;
	protected HtmlOutputText lblParte6;
	protected HtmlGridAgFunction agPrueba;
	protected HtmlLink lnkRefrescarVistaContado;
	protected HtmlPanelGrid grid1;
	protected HtmlOutputText lblFiltroMoneda;
	protected HtmlDropDownList cmbFiltroMonedas;
	protected HtmlDialogWindowHeader hdDetalleFactura;
	protected HtmlJspPanel jspPanel4;
	protected HtmlOutputText text18;
	protected HtmlOutputText text20;
	protected HtmlOutputText lblCodigo23;
	protected HtmlOutputText txtMonedaContado1;
	protected HtmlOutputText txtNomCliente;
	protected HtmlOutputText lblTasaDetalleCont;
	protected HtmlOutputText lblUninegDetalleCont;
	protected HtmlGridView gvDetalleFacContado;
	protected HtmlColumn coCoditem;
	protected HtmlOutputText lblCoditem1;
	protected HtmlOutputText lblCoditem2;
	protected HtmlOutputText lblDescitem1;
	protected HtmlOutputText lblDescitem2;
	protected HtmlOutputText lblCantDetalle1;
	protected HtmlOutputText lblCantDetalle2;
	protected HtmlOutputText lblPrecionunitDetalle1;
	protected HtmlOutputText lblPrecionunitDetalle2;
	protected HtmlOutputText lblImpuestoDetalle1;
	protected HtmlOutputText lblImpuestoDetalle2;
	protected HtmlOutputText lblSubtotalDetalleContado;
	protected HtmlOutputText txtSubtotalDetalle;
	protected HtmlOutputText text28;
	protected HtmlOutputText txtIvaDetalle;
	protected HtmlOutputText lblTotalDetCont;
	protected HtmlOutputText txtTotalDetalle;
	protected HtmlLink lnkCerrarDetalleContado;
	protected HtmlDialogWindow dgwDetalleFactura;
	protected HtmlDialogWindowClientEvents clDetalleContado;
	protected HtmlDialogWindowRoundedCorners crDetalle;
	protected HtmlDialogWindowContentPane cnpDetalle;
	protected HtmlOutputText txtFechaFactura;
	protected HtmlOutputText txtNoFactura;
	protected HtmlOutputText txtCodigoCliente;
	protected HtmlDropDownList ddlDetalleContado;
	protected HtmlOutputText text3333;
	protected HtmlOutputText text23;
	protected HtmlColumn coDescitemCont;
	protected HtmlColumn coCant;
	protected HtmlColumn coPreciounit;
	protected HtmlColumn coImpuesto;
	protected HtmlDialogWindowAutoPostBackFlags apbDetalle;
	protected HtmlPanelGroup grpParte42;
	protected HtmlOutputText lblParte62;
	protected HtmlGridAgFunction agPrueba2;
	protected HtmlLink lnkSearchCredito;
	protected HtmlScriptCollector getScDiaria() {
		if (scDiaria == null) {
			scDiaria = (HtmlScriptCollector) findComponentInRoot("scDiaria");
		}
		return scDiaria;
	}

	protected HtmlForm getFrmDiaria() {
		if (frmDiaria == null) {
			frmDiaria = (HtmlForm) findComponentInRoot("frmDiaria");
		}
		return frmDiaria;
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

	protected HtmlOutputText getLblTituloContado() {
		if (lblTituloContado == null) {
			lblTituloContado = (HtmlOutputText) findComponentInRoot("lblTituloContado");
		}
		return lblTituloContado;
	}

	protected HtmlPanelSection getSecContado1() {
		if (secContado1 == null) {
			secContado1 = (HtmlPanelSection) findComponentInRoot("secContado1");
		}
		return secContado1;
	}

	protected HtmlJspPanel getJspPanel7() {
		if (jspPanel7 == null) {
			jspPanel7 = (HtmlJspPanel) findComponentInRoot("jspPanel7");
		}
		return jspPanel7;
	}

	protected HtmlGraphicImageEx getImageEx4Cont() {
		if (imageEx4Cont == null) {
			imageEx4Cont = (HtmlGraphicImageEx) findComponentInRoot("imageEx4Cont");
		}
		return imageEx4Cont;
	}

	protected HtmlOutputText getTxtBusquedaContado() {
		if (txtBusquedaContado == null) {
			txtBusquedaContado = (HtmlOutputText) findComponentInRoot("txtBusquedaContado");
		}
		return txtBusquedaContado;
	}

	protected HtmlJspPanel getJspPanel6() {
		if (jspPanel6 == null) {
			jspPanel6 = (HtmlJspPanel) findComponentInRoot("jspPanel6");
		}
		return jspPanel6;
	}

	protected HtmlGraphicImageEx getImageEx3() {
		if (imageEx3 == null) {
			imageEx3 = (HtmlGraphicImageEx) findComponentInRoot("imageEx3");
		}
		return imageEx3;
	}

	protected HtmlOutputText getTxtBusquedaContado2() {
		if (txtBusquedaContado2 == null) {
			txtBusquedaContado2 = (HtmlOutputText) findComponentInRoot("txtBusquedaContado2");
		}
		return txtBusquedaContado2;
	}

	protected HtmlJspPanel getJspPanel100() {
		if (jspPanel100 == null) {
			jspPanel100 = (HtmlJspPanel) findComponentInRoot("jspPanel100");
		}
		return jspPanel100;
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

	protected HtmlInputHelperTypeahead getTphContado() {
		if (tphContado == null) {
			tphContado = (HtmlInputHelperTypeahead) findComponentInRoot("tphContado");
		}
		return tphContado;
	}

	protected HtmlGraphicImageEx getImgLoader() {
		if (imgLoader == null) {
			imgLoader = (HtmlGraphicImageEx) findComponentInRoot("imgLoader");
		}
		return imgLoader;
	}

	protected HtmlOutputText getTxtMensaje() {
		if (txtMensaje == null) {
			txtMensaje = (HtmlOutputText) findComponentInRoot("txtMensaje");
		}
		return txtMensaje;
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

	protected HtmlLink getLnkDetalleFacturaContado() {
		if (lnkDetalleFacturaContado == null) {
			lnkDetalleFacturaContado = (HtmlLink) findComponentInRoot("lnkDetalleFacturaContado");
		}
		return lnkDetalleFacturaContado;
	}

	protected HtmlOutputText getLblDetalleFacturaGrande() {
		if (lblDetalleFacturaGrande == null) {
			lblDetalleFacturaGrande = (HtmlOutputText) findComponentInRoot("lblDetalleFacturaGrande");
		}
		return lblDetalleFacturaGrande;
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

	protected HtmlColumn getCoUniNeg() {
		if (coUniNeg == null) {
			coUniNeg = (HtmlColumn) findComponentInRoot("coUniNeg");
		}
		return coUniNeg;
	}

	protected HtmlOutputText getLblUnineg1Grande() {
		if (lblUnineg1Grande == null) {
			lblUnineg1Grande = (HtmlOutputText) findComponentInRoot("lblUnineg1Grande");
		}
		return lblUnineg1Grande;
	}

	protected HtmlOutputText getLblUnineg2Grande() {
		if (lblUnineg2Grande == null) {
			lblUnineg2Grande = (HtmlOutputText) findComponentInRoot("lblUnineg2Grande");
		}
		return lblUnineg2Grande;
	}

	protected HtmlOutputText getLblHeader() {
		if (lblHeader == null) {
			lblHeader = (HtmlOutputText) findComponentInRoot("lblHeader");
		}
		return lblHeader;
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

	protected HtmlColumn getCoFecha() {
		if (coFecha == null) {
			coFecha = (HtmlColumn) findComponentInRoot("coFecha");
		}
		return coFecha;
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

	protected HtmlPanelGroup getGrpParte4() {
		if (grpParte4 == null) {
			grpParte4 = (HtmlPanelGroup) findComponentInRoot("grpParte4");
		}
		return grpParte4;
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

	protected HtmlLink getLnkRefrescarVistaContado() {
		if (lnkRefrescarVistaContado == null) {
			lnkRefrescarVistaContado = (HtmlLink) findComponentInRoot("lnkRefrescarVistaContado");
		}
		return lnkRefrescarVistaContado;
	}

	protected HtmlPanelGrid getGrid1() {
		if (grid1 == null) {
			grid1 = (HtmlPanelGrid) findComponentInRoot("grid1");
		}
		return grid1;
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

	protected HtmlDialogWindowHeader getHdDetalleFactura() {
		if (hdDetalleFactura == null) {
			hdDetalleFactura = (HtmlDialogWindowHeader) findComponentInRoot("hdDetalleFactura");
		}
		return hdDetalleFactura;
	}

	protected HtmlJspPanel getJspPanel4() {
		if (jspPanel4 == null) {
			jspPanel4 = (HtmlJspPanel) findComponentInRoot("jspPanel4");
		}
		return jspPanel4;
	}

	protected HtmlOutputText getText18() {
		if (text18 == null) {
			text18 = (HtmlOutputText) findComponentInRoot("text18");
		}
		return text18;
	}

	protected HtmlOutputText getText20() {
		if (text20 == null) {
			text20 = (HtmlOutputText) findComponentInRoot("text20");
		}
		return text20;
	}

	protected HtmlOutputText getLblCodigo23() {
		if (lblCodigo23 == null) {
			lblCodigo23 = (HtmlOutputText) findComponentInRoot("lblCodigo23");
		}
		return lblCodigo23;
	}

	protected HtmlOutputText getTxtMonedaContado1() {
		if (txtMonedaContado1 == null) {
			txtMonedaContado1 = (HtmlOutputText) findComponentInRoot("txtMonedaContado1");
		}
		return txtMonedaContado1;
	}

	protected HtmlOutputText getTxtNomCliente() {
		if (txtNomCliente == null) {
			txtNomCliente = (HtmlOutputText) findComponentInRoot("txtNomCliente");
		}
		return txtNomCliente;
	}

	protected HtmlOutputText getLblTasaDetalleCont() {
		if (lblTasaDetalleCont == null) {
			lblTasaDetalleCont = (HtmlOutputText) findComponentInRoot("lblTasaDetalleCont");
		}
		return lblTasaDetalleCont;
	}

	protected HtmlOutputText getLblUninegDetalleCont() {
		if (lblUninegDetalleCont == null) {
			lblUninegDetalleCont = (HtmlOutputText) findComponentInRoot("lblUninegDetalleCont");
		}
		return lblUninegDetalleCont;
	}

	protected HtmlGridView getGvDetalleFacContado() {
		if (gvDetalleFacContado == null) {
			gvDetalleFacContado = (HtmlGridView) findComponentInRoot("gvDetalleFacContado");
		}
		return gvDetalleFacContado;
	}

	protected HtmlColumn getCoCoditem() {
		if (coCoditem == null) {
			coCoditem = (HtmlColumn) findComponentInRoot("coCoditem");
		}
		return coCoditem;
	}

	protected HtmlOutputText getLblCoditem1() {
		if (lblCoditem1 == null) {
			lblCoditem1 = (HtmlOutputText) findComponentInRoot("lblCoditem1");
		}
		return lblCoditem1;
	}

	protected HtmlOutputText getLblCoditem2() {
		if (lblCoditem2 == null) {
			lblCoditem2 = (HtmlOutputText) findComponentInRoot("lblCoditem2");
		}
		return lblCoditem2;
	}

	protected HtmlOutputText getLblDescitem1() {
		if (lblDescitem1 == null) {
			lblDescitem1 = (HtmlOutputText) findComponentInRoot("lblDescitem1");
		}
		return lblDescitem1;
	}

	protected HtmlOutputText getLblDescitem2() {
		if (lblDescitem2 == null) {
			lblDescitem2 = (HtmlOutputText) findComponentInRoot("lblDescitem2");
		}
		return lblDescitem2;
	}

	protected HtmlOutputText getLblCantDetalle1() {
		if (lblCantDetalle1 == null) {
			lblCantDetalle1 = (HtmlOutputText) findComponentInRoot("lblCantDetalle1");
		}
		return lblCantDetalle1;
	}

	protected HtmlOutputText getLblCantDetalle2() {
		if (lblCantDetalle2 == null) {
			lblCantDetalle2 = (HtmlOutputText) findComponentInRoot("lblCantDetalle2");
		}
		return lblCantDetalle2;
	}

	protected HtmlOutputText getLblPrecionunitDetalle1() {
		if (lblPrecionunitDetalle1 == null) {
			lblPrecionunitDetalle1 = (HtmlOutputText) findComponentInRoot("lblPrecionunitDetalle1");
		}
		return lblPrecionunitDetalle1;
	}

	protected HtmlOutputText getLblPrecionunitDetalle2() {
		if (lblPrecionunitDetalle2 == null) {
			lblPrecionunitDetalle2 = (HtmlOutputText) findComponentInRoot("lblPrecionunitDetalle2");
		}
		return lblPrecionunitDetalle2;
	}

	protected HtmlOutputText getLblImpuestoDetalle1() {
		if (lblImpuestoDetalle1 == null) {
			lblImpuestoDetalle1 = (HtmlOutputText) findComponentInRoot("lblImpuestoDetalle1");
		}
		return lblImpuestoDetalle1;
	}

	protected HtmlOutputText getLblImpuestoDetalle2() {
		if (lblImpuestoDetalle2 == null) {
			lblImpuestoDetalle2 = (HtmlOutputText) findComponentInRoot("lblImpuestoDetalle2");
		}
		return lblImpuestoDetalle2;
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

	protected HtmlLink getLnkCerrarDetalleContado() {
		if (lnkCerrarDetalleContado == null) {
			lnkCerrarDetalleContado = (HtmlLink) findComponentInRoot("lnkCerrarDetalleContado");
		}
		return lnkCerrarDetalleContado;
	}

	protected HtmlDialogWindow getDgwDetalleFactura() {
		if (dgwDetalleFactura == null) {
			dgwDetalleFactura = (HtmlDialogWindow) findComponentInRoot("dgwDetalleFactura");
		}
		return dgwDetalleFactura;
	}

	protected HtmlDialogWindowClientEvents getClDetalleContado() {
		if (clDetalleContado == null) {
			clDetalleContado = (HtmlDialogWindowClientEvents) findComponentInRoot("clDetalleContado");
		}
		return clDetalleContado;
	}

	protected HtmlDialogWindowRoundedCorners getCrDetalle() {
		if (crDetalle == null) {
			crDetalle = (HtmlDialogWindowRoundedCorners) findComponentInRoot("crDetalle");
		}
		return crDetalle;
	}

	protected HtmlDialogWindowContentPane getCnpDetalle() {
		if (cnpDetalle == null) {
			cnpDetalle = (HtmlDialogWindowContentPane) findComponentInRoot("cnpDetalle");
		}
		return cnpDetalle;
	}

	protected HtmlOutputText getTxtFechaFactura() {
		if (txtFechaFactura == null) {
			txtFechaFactura = (HtmlOutputText) findComponentInRoot("txtFechaFactura");
		}
		return txtFechaFactura;
	}

	protected HtmlOutputText getTxtNoFactura() {
		if (txtNoFactura == null) {
			txtNoFactura = (HtmlOutputText) findComponentInRoot("txtNoFactura");
		}
		return txtNoFactura;
	}

	protected HtmlOutputText getTxtCodigoCliente() {
		if (txtCodigoCliente == null) {
			txtCodigoCliente = (HtmlOutputText) findComponentInRoot("txtCodigoCliente");
		}
		return txtCodigoCliente;
	}

	protected HtmlDropDownList getDdlDetalleContado() {
		if (ddlDetalleContado == null) {
			ddlDetalleContado = (HtmlDropDownList) findComponentInRoot("ddlDetalleContado");
		}
		return ddlDetalleContado;
	}

	protected HtmlOutputText getText3333() {
		if (text3333 == null) {
			text3333 = (HtmlOutputText) findComponentInRoot("text3333");
		}
		return text3333;
	}

	protected HtmlOutputText getText23() {
		if (text23 == null) {
			text23 = (HtmlOutputText) findComponentInRoot("text23");
		}
		return text23;
	}

	protected HtmlColumn getCoDescitemCont() {
		if (coDescitemCont == null) {
			coDescitemCont = (HtmlColumn) findComponentInRoot("coDescitemCont");
		}
		return coDescitemCont;
	}

	protected HtmlColumn getCoCant() {
		if (coCant == null) {
			coCant = (HtmlColumn) findComponentInRoot("coCant");
		}
		return coCant;
	}

	protected HtmlColumn getCoPreciounit() {
		if (coPreciounit == null) {
			coPreciounit = (HtmlColumn) findComponentInRoot("coPreciounit");
		}
		return coPreciounit;
	}

	protected HtmlColumn getCoImpuesto() {
		if (coImpuesto == null) {
			coImpuesto = (HtmlColumn) findComponentInRoot("coImpuesto");
		}
		return coImpuesto;
	}

	protected HtmlDialogWindowAutoPostBackFlags getApbDetalle() {
		if (apbDetalle == null) {
			apbDetalle = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbDetalle");
		}
		return apbDetalle;
	}

	protected HtmlPanelGroup getGrpParte42() {
		if (grpParte42 == null) {
			grpParte42 = (HtmlPanelGroup) findComponentInRoot("grpParte42");
		}
		return grpParte42;
	}

	protected HtmlOutputText getLblParte62() {
		if (lblParte62 == null) {
			lblParte62 = (HtmlOutputText) findComponentInRoot("lblParte62");
		}
		return lblParte62;
	}

	protected HtmlGridAgFunction getAgPrueba2() {
		if (agPrueba2 == null) {
			agPrueba2 = (HtmlGridAgFunction) findComponentInRoot("agPrueba2");
		}
		return agPrueba2;
	}

	protected HtmlLink getLnkSearchCredito() {
		if (lnkSearchCredito == null) {
			lnkSearchCredito = (HtmlLink) findComponentInRoot("lnkSearchCredito");
		}
		return lnkSearchCredito;
	}

}
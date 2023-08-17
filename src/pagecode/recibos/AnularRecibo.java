/**
 * 
 */
package pagecode.recibos;

import pagecode.PageCodeBase;
import com.ibm.faces.component.html.HtmlScriptCollector;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.html.HtmlOutputText;
import com.ibm.faces.component.html.HtmlPanelSection;
import com.ibm.faces.component.html.HtmlJspPanel;
import com.ibm.faces.component.html.HtmlGraphicImageEx;
import javax.faces.component.html.HtmlInputText;
import com.ibm.faces.component.html.HtmlInputHelperTypeahead;
import javax.faces.component.html.HtmlPanelGrid;
import com.infragistics.faces.grid.component.html.HtmlColumn;
import javax.faces.component.html.HtmlPanelGroup;
import com.infragistics.faces.grid.component.html.HtmlGridAgFunction;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
import com.infragistics.faces.window.component.html.HtmlDialogWindowHeader;
import com.infragistics.faces.shared.component.html.HtmlLink;
import com.infragistics.faces.window.component.html.HtmlDialogWindow;
import com.infragistics.faces.window.component.html.HtmlDialogWindowClientEvents;
import com.infragistics.faces.window.component.html.HtmlDialogWindowRoundedCorners;
import com.infragistics.faces.window.component.html.HtmlDialogWindowContentPane;
import com.infragistics.faces.window.component.html.HtmlDialogWindowAutoPostBackFlags;
import com.infragistics.faces.grid.component.html.HtmlGridView;

/**
 * @author Juan Carlos
 *
 */
public class AnularRecibo extends PageCodeBase {

	protected HtmlScriptCollector scriptCollector1;
	protected HtmlForm form1;
	protected HtmlPanelSection secContado1;
	protected HtmlJspPanel jspPanel7;
	protected HtmlGraphicImageEx imageEx4Cont;
	protected HtmlJspPanel jspPanel6;
	protected HtmlGraphicImageEx imageEx3;
	protected HtmlOutputText lblTipoBusqueda;
	protected HtmlInputText txtParametro;
	protected HtmlInputHelperTypeahead tphContado;
	protected HtmlGraphicImageEx imgLoader;
	protected HtmlOutputText lblSelecctedGrande;
	protected HtmlOutputText lblDetalleFacturaGrande;
	protected HtmlOutputText lblnofactura1Grande;
	protected HtmlOutputText lblnofactura2Grande;
	protected HtmlOutputText lblTipofactura1Grande;
	protected HtmlOutputText lblTipofactura2Grande;
	protected HtmlOutputText lblNomCli1Grande;
	protected HtmlOutputText lblNomCli2Grande;
	protected HtmlOutputText lblHeader;
	protected HtmlOutputText lblTotal1Grande;
	protected HtmlOutputText lblTotal2Grande;
	protected HtmlOutputText lblMoneda1Grande;
	protected HtmlOutputText lblMoneda2Grande;
	protected HtmlPanelGrid grid1;
	protected HtmlOutputText lblTituloContado;
	protected HtmlOutputText txtBusquedaContado;
	protected HtmlOutputText txtBusquedaContado2;
	protected HtmlJspPanel jspPanel100;
	protected HtmlOutputText lblFiltroFactura;
	protected HtmlColumn coTotal;
	protected HtmlColumn coMoneda;
	protected HtmlPanelGroup grpParte4;
	protected HtmlOutputText lblParte6;
	protected HtmlGridAgFunction agPrueba;
	protected HtmlOutputText txtMensaje;
	protected HtmlOutputText lblFiltroMoneda;
	protected HtmlDropDownList cmbFiltroMonedas;
	protected HtmlDropDownList cmbFiltroFacturas;
	protected HtmlDialogWindowHeader hdDetalleFactura;
	protected HtmlJspPanel jspPanel4;
	protected HtmlOutputText text18;
	protected HtmlOutputText text20;
	protected HtmlOutputText lblCodigo23;
	protected HtmlOutputText txtMonedaContado1;
	protected HtmlOutputText txtNomCliente;
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
	protected HtmlOutputText txtHoraRecibo;
	protected HtmlOutputText txtNoRecibo;
	protected HtmlOutputText txtCodigoCliente;
	protected HtmlDialogWindowAutoPostBackFlags apbDetalle;
	protected HtmlColumn coCodcomp;
	protected HtmlOutputText lblCodcomp1;
	protected HtmlOutputText lblCodcomp2;
	protected HtmlOutputText txtNoBatch;
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
	protected HtmlColumn coDescitemCont;
	protected HtmlColumn coCant;
	protected HtmlColumn coPreciounit;
	protected HtmlColumn coImpuesto;
	protected HtmlColumn coRefer1;
	protected HtmlOutputText lblRefer12;
	protected HtmlOutputText lblRefer11;
	protected HtmlColumn coRefer2;
	protected HtmlOutputText lblRefer21;
	protected HtmlOutputText lblRefer22;
	protected HtmlColumn coRefer3;
	protected HtmlOutputText lblRefer31;
	protected HtmlOutputText lblRefer32;
	protected HtmlColumn coRefer4;
	protected HtmlOutputText lblRefer41;
	protected HtmlOutputText lblRefer42;
	protected HtmlGridView gvReciboFactura;
	protected HtmlColumn coNoFactura2;
	protected HtmlOutputText lblNoFactura1;
	protected HtmlOutputText lblNoFactura2;
	protected HtmlColumn coTipofactura2;
	protected HtmlOutputText lblTipofactura1;
	protected HtmlOutputText lblTipofactura2;
	protected HtmlColumn coUnineg2;
	protected HtmlOutputText lblUnineg1;
	protected HtmlOutputText lblUnineg2;
	protected HtmlColumn coMoneda2;
	protected HtmlOutputText lblMoneda1;
	protected HtmlOutputText lblMoneda2;
	protected HtmlColumn coFecha2;
	protected HtmlOutputText lblFecha22;
	protected HtmlOutputText lblFecha23;
	protected HtmlColumn coPartida2;
	protected HtmlOutputText lblPartida22;
	protected HtmlOutputText lblPartida23;
	protected HtmlDialogWindowHeader hdValida;
	protected HtmlPanelGrid grdValida;
	protected HtmlGraphicImageEx imgValida;
	protected HtmlLink lnkCerrarValida;
	protected HtmlDialogWindow dwValidaContado;
	protected HtmlDialogWindowClientEvents cleValida;
	protected HtmlDialogWindowRoundedCorners rcValida;
	protected HtmlDialogWindowContentPane cpValida;
	protected HtmlOutputText lblValida;
	protected HtmlJspPanel jspPane20;
	protected HtmlDialogWindowAutoPostBackFlags apbReciboContado2;
	protected HtmlDialogWindowHeader hdImprime;
	protected HtmlPanelGrid grid100;
	protected HtmlGraphicImageEx imageEx2Imprime;
	protected HtmlLink lnkCerrarMensaje13;
	protected HtmlDialogWindow dwImprimeContado;
	protected HtmlDialogWindowClientEvents cleImprime;
	protected HtmlDialogWindowRoundedCorners rcImprime;
	protected HtmlDialogWindowContentPane cpImprime;
	protected HtmlJspPanel jspPanel3;
	protected HtmlLink lnkCerrarMensaje14;
	protected HtmlDialogWindowAutoPostBackFlags apbImprime;
	protected HtmlOutputText lblValidaAnular;
	protected HtmlScriptCollector getScriptCollector1() {
		if (scriptCollector1 == null) {
			scriptCollector1 = (HtmlScriptCollector) findComponentInRoot("scriptCollector1");
		}
		return scriptCollector1;
	}

	protected HtmlForm getForm1() {
		if (form1 == null) {
			form1 = (HtmlForm) findComponentInRoot("form1");
		}
		return form1;
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

	protected HtmlOutputText getLblTipoBusqueda() {
		if (lblTipoBusqueda == null) {
			lblTipoBusqueda = (HtmlOutputText) findComponentInRoot("lblTipoBusqueda");
		}
		return lblTipoBusqueda;
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

	protected HtmlOutputText getLblSelecctedGrande() {
		if (lblSelecctedGrande == null) {
			lblSelecctedGrande = (HtmlOutputText) findComponentInRoot("lblSelecctedGrande");
		}
		return lblSelecctedGrande;
	}

	protected HtmlOutputText getLblDetalleFacturaGrande() {
		if (lblDetalleFacturaGrande == null) {
			lblDetalleFacturaGrande = (HtmlOutputText) findComponentInRoot("lblDetalleFacturaGrande");
		}
		return lblDetalleFacturaGrande;
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

	protected HtmlOutputText getLblHeader() {
		if (lblHeader == null) {
			lblHeader = (HtmlOutputText) findComponentInRoot("lblHeader");
		}
		return lblHeader;
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

	protected HtmlPanelGrid getGrid1() {
		if (grid1 == null) {
			grid1 = (HtmlPanelGrid) findComponentInRoot("grid1");
		}
		return grid1;
	}

	protected HtmlOutputText getLblTituloContado() {
		if (lblTituloContado == null) {
			lblTituloContado = (HtmlOutputText) findComponentInRoot("lblTituloContado");
		}
		return lblTituloContado;
	}

	protected HtmlOutputText getTxtBusquedaContado() {
		if (txtBusquedaContado == null) {
			txtBusquedaContado = (HtmlOutputText) findComponentInRoot("txtBusquedaContado");
		}
		return txtBusquedaContado;
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

	protected HtmlOutputText getLblFiltroFactura() {
		if (lblFiltroFactura == null) {
			lblFiltroFactura = (HtmlOutputText) findComponentInRoot("lblFiltroFactura");
		}
		return lblFiltroFactura;
	}

	protected HtmlColumn getCoTotal() {
		if (coTotal == null) {
			coTotal = (HtmlColumn) findComponentInRoot("coTotal");
		}
		return coTotal;
	}

	protected HtmlColumn getCoMoneda() {
		if (coMoneda == null) {
			coMoneda = (HtmlColumn) findComponentInRoot("coMoneda");
		}
		return coMoneda;
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

	protected HtmlOutputText getTxtMensaje() {
		if (txtMensaje == null) {
			txtMensaje = (HtmlOutputText) findComponentInRoot("txtMensaje");
		}
		return txtMensaje;
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

	protected HtmlDropDownList getCmbFiltroFacturas() {
		if (cmbFiltroFacturas == null) {
			cmbFiltroFacturas = (HtmlDropDownList) findComponentInRoot("cmbFiltroFacturas");
		}
		return cmbFiltroFacturas;
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

	protected HtmlOutputText getTxtHoraRecibo() {
		if (txtHoraRecibo == null) {
			txtHoraRecibo = (HtmlOutputText) findComponentInRoot("txtHoraRecibo");
		}
		return txtHoraRecibo;
	}

	protected HtmlOutputText getTxtNoRecibo() {
		if (txtNoRecibo == null) {
			txtNoRecibo = (HtmlOutputText) findComponentInRoot("txtNoRecibo");
		}
		return txtNoRecibo;
	}

	protected HtmlOutputText getTxtCodigoCliente() {
		if (txtCodigoCliente == null) {
			txtCodigoCliente = (HtmlOutputText) findComponentInRoot("txtCodigoCliente");
		}
		return txtCodigoCliente;
	}

	protected HtmlDialogWindowAutoPostBackFlags getApbDetalle() {
		if (apbDetalle == null) {
			apbDetalle = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbDetalle");
		}
		return apbDetalle;
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

	protected HtmlOutputText getTxtNoBatch() {
		if (txtNoBatch == null) {
			txtNoBatch = (HtmlOutputText) findComponentInRoot("txtNoBatch");
		}
		return txtNoBatch;
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

	protected HtmlColumn getCoRefer1() {
		if (coRefer1 == null) {
			coRefer1 = (HtmlColumn) findComponentInRoot("coRefer1");
		}
		return coRefer1;
	}

	protected HtmlOutputText getLblRefer12() {
		if (lblRefer12 == null) {
			lblRefer12 = (HtmlOutputText) findComponentInRoot("lblRefer12");
		}
		return lblRefer12;
	}

	protected HtmlOutputText getLblRefer11() {
		if (lblRefer11 == null) {
			lblRefer11 = (HtmlOutputText) findComponentInRoot("lblRefer11");
		}
		return lblRefer11;
	}

	protected HtmlColumn getCoRefer2() {
		if (coRefer2 == null) {
			coRefer2 = (HtmlColumn) findComponentInRoot("coRefer2");
		}
		return coRefer2;
	}

	protected HtmlOutputText getLblRefer21() {
		if (lblRefer21 == null) {
			lblRefer21 = (HtmlOutputText) findComponentInRoot("lblRefer21");
		}
		return lblRefer21;
	}

	protected HtmlOutputText getLblRefer22() {
		if (lblRefer22 == null) {
			lblRefer22 = (HtmlOutputText) findComponentInRoot("lblRefer22");
		}
		return lblRefer22;
	}

	protected HtmlColumn getCoRefer3() {
		if (coRefer3 == null) {
			coRefer3 = (HtmlColumn) findComponentInRoot("coRefer3");
		}
		return coRefer3;
	}

	protected HtmlOutputText getLblRefer31() {
		if (lblRefer31 == null) {
			lblRefer31 = (HtmlOutputText) findComponentInRoot("lblRefer31");
		}
		return lblRefer31;
	}

	protected HtmlOutputText getLblRefer32() {
		if (lblRefer32 == null) {
			lblRefer32 = (HtmlOutputText) findComponentInRoot("lblRefer32");
		}
		return lblRefer32;
	}

	protected HtmlColumn getCoRefer4() {
		if (coRefer4 == null) {
			coRefer4 = (HtmlColumn) findComponentInRoot("coRefer4");
		}
		return coRefer4;
	}

	protected HtmlOutputText getLblRefer41() {
		if (lblRefer41 == null) {
			lblRefer41 = (HtmlOutputText) findComponentInRoot("lblRefer41");
		}
		return lblRefer41;
	}

	protected HtmlOutputText getLblRefer42() {
		if (lblRefer42 == null) {
			lblRefer42 = (HtmlOutputText) findComponentInRoot("lblRefer42");
		}
		return lblRefer42;
	}

	protected HtmlGridView getGvReciboFactura() {
		if (gvReciboFactura == null) {
			gvReciboFactura = (HtmlGridView) findComponentInRoot("gvReciboFactura");
		}
		return gvReciboFactura;
	}

	protected HtmlColumn getCoNoFactura2() {
		if (coNoFactura2 == null) {
			coNoFactura2 = (HtmlColumn) findComponentInRoot("coNoFactura2");
		}
		return coNoFactura2;
	}

	protected HtmlOutputText getLblNoFactura1() {
		if (lblNoFactura1 == null) {
			lblNoFactura1 = (HtmlOutputText) findComponentInRoot("lblNoFactura1");
		}
		return lblNoFactura1;
	}

	protected HtmlOutputText getLblNoFactura2() {
		if (lblNoFactura2 == null) {
			lblNoFactura2 = (HtmlOutputText) findComponentInRoot("lblNoFactura2");
		}
		return lblNoFactura2;
	}

	protected HtmlColumn getCoTipofactura2() {
		if (coTipofactura2 == null) {
			coTipofactura2 = (HtmlColumn) findComponentInRoot("coTipofactura2");
		}
		return coTipofactura2;
	}

	protected HtmlOutputText getLblTipofactura1() {
		if (lblTipofactura1 == null) {
			lblTipofactura1 = (HtmlOutputText) findComponentInRoot("lblTipofactura1");
		}
		return lblTipofactura1;
	}

	protected HtmlOutputText getLblTipofactura2() {
		if (lblTipofactura2 == null) {
			lblTipofactura2 = (HtmlOutputText) findComponentInRoot("lblTipofactura2");
		}
		return lblTipofactura2;
	}

	protected HtmlColumn getCoUnineg2() {
		if (coUnineg2 == null) {
			coUnineg2 = (HtmlColumn) findComponentInRoot("coUnineg2");
		}
		return coUnineg2;
	}

	protected HtmlOutputText getLblUnineg1() {
		if (lblUnineg1 == null) {
			lblUnineg1 = (HtmlOutputText) findComponentInRoot("lblUnineg1");
		}
		return lblUnineg1;
	}

	protected HtmlOutputText getLblUnineg2() {
		if (lblUnineg2 == null) {
			lblUnineg2 = (HtmlOutputText) findComponentInRoot("lblUnineg2");
		}
		return lblUnineg2;
	}

	protected HtmlColumn getCoMoneda2() {
		if (coMoneda2 == null) {
			coMoneda2 = (HtmlColumn) findComponentInRoot("coMoneda2");
		}
		return coMoneda2;
	}

	protected HtmlOutputText getLblMoneda1() {
		if (lblMoneda1 == null) {
			lblMoneda1 = (HtmlOutputText) findComponentInRoot("lblMoneda1");
		}
		return lblMoneda1;
	}

	protected HtmlOutputText getLblMoneda2() {
		if (lblMoneda2 == null) {
			lblMoneda2 = (HtmlOutputText) findComponentInRoot("lblMoneda2");
		}
		return lblMoneda2;
	}

	protected HtmlColumn getCoFecha2() {
		if (coFecha2 == null) {
			coFecha2 = (HtmlColumn) findComponentInRoot("coFecha2");
		}
		return coFecha2;
	}

	protected HtmlOutputText getLblFecha22() {
		if (lblFecha22 == null) {
			lblFecha22 = (HtmlOutputText) findComponentInRoot("lblFecha22");
		}
		return lblFecha22;
	}

	protected HtmlOutputText getLblFecha23() {
		if (lblFecha23 == null) {
			lblFecha23 = (HtmlOutputText) findComponentInRoot("lblFecha23");
		}
		return lblFecha23;
	}

	protected HtmlColumn getCoPartida2() {
		if (coPartida2 == null) {
			coPartida2 = (HtmlColumn) findComponentInRoot("coPartida2");
		}
		return coPartida2;
	}

	protected HtmlOutputText getLblPartida22() {
		if (lblPartida22 == null) {
			lblPartida22 = (HtmlOutputText) findComponentInRoot("lblPartida22");
		}
		return lblPartida22;
	}

	protected HtmlOutputText getLblPartida23() {
		if (lblPartida23 == null) {
			lblPartida23 = (HtmlOutputText) findComponentInRoot("lblPartida23");
		}
		return lblPartida23;
	}

	protected HtmlDialogWindowHeader getHdValida() {
		if (hdValida == null) {
			hdValida = (HtmlDialogWindowHeader) findComponentInRoot("hdValida");
		}
		return hdValida;
	}

	protected HtmlPanelGrid getGrdValida() {
		if (grdValida == null) {
			grdValida = (HtmlPanelGrid) findComponentInRoot("grdValida");
		}
		return grdValida;
	}

	protected HtmlGraphicImageEx getImgValida() {
		if (imgValida == null) {
			imgValida = (HtmlGraphicImageEx) findComponentInRoot("imgValida");
		}
		return imgValida;
	}

	protected HtmlLink getLnkCerrarValida() {
		if (lnkCerrarValida == null) {
			lnkCerrarValida = (HtmlLink) findComponentInRoot("lnkCerrarValida");
		}
		return lnkCerrarValida;
	}

	protected HtmlDialogWindow getDwValidaContado() {
		if (dwValidaContado == null) {
			dwValidaContado = (HtmlDialogWindow) findComponentInRoot("dwValidaContado");
		}
		return dwValidaContado;
	}

	protected HtmlDialogWindowClientEvents getCleValida() {
		if (cleValida == null) {
			cleValida = (HtmlDialogWindowClientEvents) findComponentInRoot("cleValida");
		}
		return cleValida;
	}

	protected HtmlDialogWindowRoundedCorners getRcValida() {
		if (rcValida == null) {
			rcValida = (HtmlDialogWindowRoundedCorners) findComponentInRoot("rcValida");
		}
		return rcValida;
	}

	protected HtmlDialogWindowContentPane getCpValida() {
		if (cpValida == null) {
			cpValida = (HtmlDialogWindowContentPane) findComponentInRoot("cpValida");
		}
		return cpValida;
	}

	protected HtmlOutputText getLblValida() {
		if (lblValida == null) {
			lblValida = (HtmlOutputText) findComponentInRoot("lblValida");
		}
		return lblValida;
	}

	protected HtmlJspPanel getJspPane20() {
		if (jspPane20 == null) {
			jspPane20 = (HtmlJspPanel) findComponentInRoot("jspPane20");
		}
		return jspPane20;
	}

	protected HtmlDialogWindowAutoPostBackFlags getApbReciboContado2() {
		if (apbReciboContado2 == null) {
			apbReciboContado2 = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbReciboContado2");
		}
		return apbReciboContado2;
	}

	protected HtmlDialogWindowHeader getHdImprime() {
		if (hdImprime == null) {
			hdImprime = (HtmlDialogWindowHeader) findComponentInRoot("hdImprime");
		}
		return hdImprime;
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

	protected HtmlLink getLnkCerrarMensaje13() {
		if (lnkCerrarMensaje13 == null) {
			lnkCerrarMensaje13 = (HtmlLink) findComponentInRoot("lnkCerrarMensaje13");
		}
		return lnkCerrarMensaje13;
	}

	protected HtmlDialogWindow getDwImprimeContado() {
		if (dwImprimeContado == null) {
			dwImprimeContado = (HtmlDialogWindow) findComponentInRoot("dwImprimeContado");
		}
		return dwImprimeContado;
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

	protected HtmlJspPanel getJspPanel3() {
		if (jspPanel3 == null) {
			jspPanel3 = (HtmlJspPanel) findComponentInRoot("jspPanel3");
		}
		return jspPanel3;
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

	protected HtmlOutputText getLblValidaAnular() {
		if (lblValidaAnular == null) {
			lblValidaAnular = (HtmlOutputText) findComponentInRoot("lblValidaAnular");
		}
		return lblValidaAnular;
	}

}
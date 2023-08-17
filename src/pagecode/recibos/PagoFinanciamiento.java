/**
 * 
 */
package pagecode.recibos;

import pagecode.PageCodeBase;
import com.ibm.faces.component.html.HtmlScriptCollector;
import com.infragistics.faces.menu.component.html.HtmlMenu;
import com.infragistics.faces.menu.component.html.HtmlMenuItem;
import javax.faces.component.html.HtmlOutputText;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
import com.infragistics.faces.shared.component.html.HtmlLink;
import javax.faces.component.html.HtmlPanelGrid;
import com.ibm.faces.component.html.HtmlPanelSection;
import com.ibm.faces.component.html.HtmlJspPanel;
import com.ibm.faces.component.html.HtmlGraphicImageEx;
import javax.faces.component.html.HtmlInputText;
import com.infragistics.faces.grid.component.html.HtmlGridView;
import com.infragistics.faces.grid.component.html.HtmlColumnSelectRow;
import com.infragistics.faces.grid.component.html.HtmlColumn;
import com.infragistics.faces.window.component.html.HtmlDialogWindowHeader;
import com.infragistics.faces.window.component.html.HtmlDialogWindow;
import com.infragistics.faces.window.component.html.HtmlDialogWindowClientEvents;
import com.infragistics.faces.window.component.html.HtmlDialogWindowRoundedCorners;
import com.infragistics.faces.window.component.html.HtmlDialogWindowContentPane;
import com.infragistics.faces.window.component.html.HtmlDialogWindowAutoPostBackFlags;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.UINamingContainer;

/**
 * @author Informatica
 *
 */
public class PagoFinanciamiento extends PageCodeBase {

	protected HtmlScriptCollector scFinan;
	protected HtmlMenu menu1;
	protected HtmlMenuItem item0;
	protected HtmlMenuItem item1;
	protected HtmlMenuItem item2;
	protected HtmlOutputText lblTitulo1Contado;
	protected HtmlLink lnkProcesarRecibo2;
	protected HtmlLink lnkRefrescarVistaContado;
	protected HtmlPanelGrid grid1;
	protected HtmlOutputText lblFiltroMoneda;
	protected HtmlOutputText lblTituloContado80;
	protected HtmlDropDownList cmbFiltroMonedas;
	protected HtmlOutputText lblFiltroFactura;
	protected HtmlDropDownList cmbFiltroFacturas;
	protected HtmlPanelSection secFinan1;
	protected HtmlJspPanel jspPanel7;
	protected HtmlGraphicImageEx imageEx4Cont;
	protected HtmlJspPanel jspPanel6;
	protected HtmlGraphicImageEx imageEx3;
	protected HtmlOutputText lblTipoBusqueda;
	protected HtmlDropDownList dropBusqueda;
	protected HtmlInputText txtParametro;
	protected HtmlLink lnkSearchContado;
	protected HtmlGraphicImageEx imgLoader;
	protected HtmlOutputText txtBusquedaContado;
	protected HtmlOutputText txtBusquedaContado2;
	protected HtmlJspPanel jspPanel100;
	protected HtmlGridView gvFacsFinan;
	protected HtmlColumnSelectRow columnSelectRowRenderer1;
	protected HtmlOutputText lblSelecctedGrande;
	protected HtmlLink lnkDetalleFacturaContado;
	protected HtmlOutputText lblDetalleFacturaGrande;
	protected HtmlOutputText lblnofactura1Grande;
	protected HtmlOutputText lblnofactura2Grande;
	protected HtmlOutputText lblTipofactura1Grande;
	protected HtmlOutputText lblTipofactura2Grande;
	protected HtmlOutputText lblCuotafactura1Grande;
	protected HtmlOutputText lblCuotafactura2Grande;
	protected HtmlOutputText lblNoSol1Grande;
	protected HtmlOutputText lblNoSol2Grande;
	protected HtmlOutputText lblUnineg1Grande;
	protected HtmlOutputText lblUnineg2Grande;
	protected HtmlOutputText lblHeader;
	protected HtmlOutputText lblTotal1Grande;
	protected HtmlOutputText lblTotal2Grande;
	protected HtmlOutputText lblMoneda1Grande;
	protected HtmlOutputText lblMoneda2Grande;
	protected HtmlOutputText lblFechaVence1Grande;
	protected HtmlOutputText lblFechaVence2Grande;
	protected HtmlOutputText lblFechaFac1Grande;
	protected HtmlOutputText lblFechaFac2Grande;
	protected HtmlColumn coLnkDetalle;
	protected HtmlColumn coNoFactura;
	protected HtmlColumn coTipoFactura;
	protected HtmlColumn coCuotaFactura;
	protected HtmlColumn coNoSolicitud;
	protected HtmlColumn coUniNeg;
	protected HtmlColumn coTotal;
	protected HtmlColumn coMoneda;
	protected HtmlColumn coFechaVence;
	protected HtmlColumn coFechaFac;
	protected HtmlDialogWindowHeader hdDetalleSolicitud;
	protected HtmlJspPanel jspDetalleSolictud;
	protected HtmlLink lnkCerrarDetalleContado;
	protected HtmlDialogWindow dwDetalleSolicitud;
	protected HtmlDialogWindowClientEvents clDetalleSolicitud;
	protected HtmlDialogWindowRoundedCorners crDetalleSolicitud;
	protected HtmlDialogWindowContentPane cnpDetalleSolicitud;
	protected HtmlDialogWindowAutoPostBackFlags apbDetalleSolicitud;
	protected HtmlForm frmFinan;
	protected UINamingContainer viewFragment1;
	protected HtmlScriptCollector getScFinan() {
		if (scFinan == null) {
			scFinan = (HtmlScriptCollector) findComponentInRoot("scFinan");
		}
		return scFinan;
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

	protected HtmlLink getLnkProcesarRecibo2() {
		if (lnkProcesarRecibo2 == null) {
			lnkProcesarRecibo2 = (HtmlLink) findComponentInRoot("lnkProcesarRecibo2");
		}
		return lnkProcesarRecibo2;
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

	protected HtmlOutputText getLblTituloContado80() {
		if (lblTituloContado80 == null) {
			lblTituloContado80 = (HtmlOutputText) findComponentInRoot("lblTituloContado80");
		}
		return lblTituloContado80;
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

	protected HtmlPanelSection getSecFinan1() {
		if (secFinan1 == null) {
			secFinan1 = (HtmlPanelSection) findComponentInRoot("secFinan1");
		}
		return secFinan1;
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

	protected HtmlLink getLnkSearchContado() {
		if (lnkSearchContado == null) {
			lnkSearchContado = (HtmlLink) findComponentInRoot("lnkSearchContado");
		}
		return lnkSearchContado;
	}

	protected HtmlGraphicImageEx getImgLoader() {
		if (imgLoader == null) {
			imgLoader = (HtmlGraphicImageEx) findComponentInRoot("imgLoader");
		}
		return imgLoader;
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

	protected HtmlGridView getGvFacsFinan() {
		if (gvFacsFinan == null) {
			gvFacsFinan = (HtmlGridView) findComponentInRoot("gvFacsFinan");
		}
		return gvFacsFinan;
	}

	protected HtmlColumnSelectRow getColumnSelectRowRenderer1() {
		if (columnSelectRowRenderer1 == null) {
			columnSelectRowRenderer1 = (HtmlColumnSelectRow) findComponentInRoot("columnSelectRowRenderer1");
		}
		return columnSelectRowRenderer1;
	}

	protected HtmlOutputText getLblSelecctedGrande() {
		if (lblSelecctedGrande == null) {
			lblSelecctedGrande = (HtmlOutputText) findComponentInRoot("lblSelecctedGrande");
		}
		return lblSelecctedGrande;
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

	protected HtmlOutputText getLblCuotafactura1Grande() {
		if (lblCuotafactura1Grande == null) {
			lblCuotafactura1Grande = (HtmlOutputText) findComponentInRoot("lblCuotafactura1Grande");
		}
		return lblCuotafactura1Grande;
	}

	protected HtmlOutputText getLblCuotafactura2Grande() {
		if (lblCuotafactura2Grande == null) {
			lblCuotafactura2Grande = (HtmlOutputText) findComponentInRoot("lblCuotafactura2Grande");
		}
		return lblCuotafactura2Grande;
	}

	protected HtmlOutputText getLblNoSol1Grande() {
		if (lblNoSol1Grande == null) {
			lblNoSol1Grande = (HtmlOutputText) findComponentInRoot("lblNoSol1Grande");
		}
		return lblNoSol1Grande;
	}

	protected HtmlOutputText getLblNoSol2Grande() {
		if (lblNoSol2Grande == null) {
			lblNoSol2Grande = (HtmlOutputText) findComponentInRoot("lblNoSol2Grande");
		}
		return lblNoSol2Grande;
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

	protected HtmlOutputText getLblFechaVence1Grande() {
		if (lblFechaVence1Grande == null) {
			lblFechaVence1Grande = (HtmlOutputText) findComponentInRoot("lblFechaVence1Grande");
		}
		return lblFechaVence1Grande;
	}

	protected HtmlOutputText getLblFechaVence2Grande() {
		if (lblFechaVence2Grande == null) {
			lblFechaVence2Grande = (HtmlOutputText) findComponentInRoot("lblFechaVence2Grande");
		}
		return lblFechaVence2Grande;
	}

	protected HtmlOutputText getLblFechaFac1Grande() {
		if (lblFechaFac1Grande == null) {
			lblFechaFac1Grande = (HtmlOutputText) findComponentInRoot("lblFechaFac1Grande");
		}
		return lblFechaFac1Grande;
	}

	protected HtmlOutputText getLblFechaFac2Grande() {
		if (lblFechaFac2Grande == null) {
			lblFechaFac2Grande = (HtmlOutputText) findComponentInRoot("lblFechaFac2Grande");
		}
		return lblFechaFac2Grande;
	}

	protected HtmlColumn getCoLnkDetalle() {
		if (coLnkDetalle == null) {
			coLnkDetalle = (HtmlColumn) findComponentInRoot("coLnkDetalle");
		}
		return coLnkDetalle;
	}

	protected HtmlColumn getCoNoFactura() {
		if (coNoFactura == null) {
			coNoFactura = (HtmlColumn) findComponentInRoot("coNoFactura");
		}
		return coNoFactura;
	}

	protected HtmlColumn getCoTipoFactura() {
		if (coTipoFactura == null) {
			coTipoFactura = (HtmlColumn) findComponentInRoot("coTipoFactura");
		}
		return coTipoFactura;
	}

	protected HtmlColumn getCoCuotaFactura() {
		if (coCuotaFactura == null) {
			coCuotaFactura = (HtmlColumn) findComponentInRoot("coCuotaFactura");
		}
		return coCuotaFactura;
	}

	protected HtmlColumn getCoNoSolicitud() {
		if (coNoSolicitud == null) {
			coNoSolicitud = (HtmlColumn) findComponentInRoot("coNoSolicitud");
		}
		return coNoSolicitud;
	}

	protected HtmlColumn getCoUniNeg() {
		if (coUniNeg == null) {
			coUniNeg = (HtmlColumn) findComponentInRoot("coUniNeg");
		}
		return coUniNeg;
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

	protected HtmlColumn getCoFechaVence() {
		if (coFechaVence == null) {
			coFechaVence = (HtmlColumn) findComponentInRoot("coFechaVence");
		}
		return coFechaVence;
	}

	protected HtmlColumn getCoFechaFac() {
		if (coFechaFac == null) {
			coFechaFac = (HtmlColumn) findComponentInRoot("coFechaFac");
		}
		return coFechaFac;
	}

	protected HtmlDialogWindowHeader getHdDetalleSolicitud() {
		if (hdDetalleSolicitud == null) {
			hdDetalleSolicitud = (HtmlDialogWindowHeader) findComponentInRoot("hdDetalleSolicitud");
		}
		return hdDetalleSolicitud;
	}

	protected HtmlJspPanel getJspDetalleSolictud() {
		if (jspDetalleSolictud == null) {
			jspDetalleSolictud = (HtmlJspPanel) findComponentInRoot("jspDetalleSolictud");
		}
		return jspDetalleSolictud;
	}

	protected HtmlLink getLnkCerrarDetalleContado() {
		if (lnkCerrarDetalleContado == null) {
			lnkCerrarDetalleContado = (HtmlLink) findComponentInRoot("lnkCerrarDetalleContado");
		}
		return lnkCerrarDetalleContado;
	}

	protected HtmlDialogWindow getDwDetalleSolicitud() {
		if (dwDetalleSolicitud == null) {
			dwDetalleSolicitud = (HtmlDialogWindow) findComponentInRoot("dwDetalleSolicitud");
		}
		return dwDetalleSolicitud;
	}

	protected HtmlDialogWindowClientEvents getClDetalleSolicitud() {
		if (clDetalleSolicitud == null) {
			clDetalleSolicitud = (HtmlDialogWindowClientEvents) findComponentInRoot("clDetalleSolicitud");
		}
		return clDetalleSolicitud;
	}

	protected HtmlDialogWindowRoundedCorners getCrDetalleSolicitud() {
		if (crDetalleSolicitud == null) {
			crDetalleSolicitud = (HtmlDialogWindowRoundedCorners) findComponentInRoot("crDetalleSolicitud");
		}
		return crDetalleSolicitud;
	}

	protected HtmlDialogWindowContentPane getCnpDetalleSolicitud() {
		if (cnpDetalleSolicitud == null) {
			cnpDetalleSolicitud = (HtmlDialogWindowContentPane) findComponentInRoot("cnpDetalleSolicitud");
		}
		return cnpDetalleSolicitud;
	}

	protected HtmlDialogWindowAutoPostBackFlags getApbDetalleSolicitud() {
		if (apbDetalleSolicitud == null) {
			apbDetalleSolicitud = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbDetalleSolicitud");
		}
		return apbDetalleSolicitud;
	}

	protected HtmlForm getFrmFinan() {
		if (frmFinan == null) {
			frmFinan = (HtmlForm) findComponentInRoot("frmFinan");
		}
		return frmFinan;
	}

	protected UINamingContainer getViewFragment1() {
		if (viewFragment1 == null) {
			viewFragment1 = (UINamingContainer) findComponentInRoot("viewFragment1");
		}
		return viewFragment1;
	}

}
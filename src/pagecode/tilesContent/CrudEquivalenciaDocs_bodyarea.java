/**
 * 
 */
package pagecode.tilesContent;

import pagecode.PageCodeBase;
import javax.faces.component.UINamingContainer;
import com.ibm.faces.component.html.HtmlScriptCollector;
import javax.faces.component.html.HtmlForm;
import com.infragistics.faces.menu.component.html.HtmlMenu;
import com.infragistics.faces.menu.component.html.HtmlMenuItem;
import javax.faces.component.html.HtmlOutputText;
import com.infragistics.faces.shared.component.html.HtmlLink;
import com.infragistics.faces.grid.component.html.HtmlGridView;
import com.infragistics.faces.grid.component.html.HtmlColumn;
import com.infragistics.faces.window.component.html.HtmlDialogWindowHeader;
import com.infragistics.faces.window.component.html.HtmlDialogWindowClientEvents;
import com.infragistics.faces.window.component.html.HtmlDialogWindowRoundedCorners;
import com.infragistics.faces.window.component.html.HtmlDialogWindowContentPane;
import javax.faces.component.html.HtmlInputText;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
import javax.faces.component.html.HtmlInputTextarea;
import com.ibm.faces.component.html.HtmlJspPanel;
import com.infragistics.faces.window.component.html.HtmlDialogWindow;
import com.infragistics.faces.input.component.html.HtmlCheckBox;

/**
 * @author Carlos Hernandez
 *
 */
public class CrudEquivalenciaDocs_bodyarea extends PageCodeBase {

	protected UINamingContainer vfCrudEquivTipodocs;
	protected HtmlScriptCollector scConsultar;
	protected HtmlForm frmConsultar;
	protected HtmlMenu menu1;
	protected HtmlMenuItem item0;
	protected HtmlMenuItem item1;
	protected HtmlMenuItem item2;
	protected HtmlOutputText lblTitulo1eqtd;
	protected HtmlOutputText lblTituloeqtd;
	protected HtmlOutputText lblHeader;
	protected HtmlLink lnkEditarConfiguracion;
	protected HtmlOutputText lblnombrebco0;
	protected HtmlOutputText lblnombrebco1;
	protected HtmlGridView gvEquivalenciasDocs;
	protected HtmlColumn colnkEditarConfig;
	protected HtmlColumn coNombreMovBanco;
	protected HtmlOutputText lblEditarConfig;
	protected HtmlOutputText lblCodigoMov0;
	protected HtmlOutputText lblCodigoMov1;
	protected HtmlColumn coCodigoMov;
	protected HtmlColumn coCodigoBanco;
	protected HtmlOutputText lblCodigoBco0;
	protected HtmlOutputText lblCodigoBco1;
	protected HtmlColumn coCodMovimientoCaja;
	protected HtmlOutputText lblCodMovCaja0;
	protected HtmlOutputText lblCodMovCaja1;
	protected HtmlColumn coNomMovimientoCaja;
	protected HtmlOutputText lblNomMovCaja0;
	protected HtmlOutputText lblNomMovCaja1;
	protected HtmlColumn coDescripAsocia;
	protected HtmlOutputText lblDescripAsocia0;
	protected HtmlOutputText lblDescripAsocia1;
	protected HtmlLink lnkAgregarNuevaConfig;
	protected HtmlLink lnkRefrescarDatos;
	protected HtmlDialogWindowHeader hdFactura;
	protected HtmlDialogWindowClientEvents clDwAgregarNuevaConfig;
	protected HtmlDialogWindowRoundedCorners dwcrDwAgregarNuevaConfig;
	protected HtmlDialogWindowContentPane dwcpDwAgregarNuevaConfig;
	protected HtmlLink lnkAgregarEquivalencia;
	protected HtmlLink lnkCerrarAgregarNuevo;
	protected HtmlInputText txtCodigoBco;
	protected HtmlOutputText lblCodigoBco;
	protected HtmlOutputText lblCodigoCaja;
	protected HtmlDropDownList ddlCodigoCaja;
	protected HtmlOutputText lblNombreCodigo;
	protected HtmlInputText txtNombreCodigo;
	protected HtmlOutputText lblDescripcion;
	protected HtmlOutputText lblIdBanco;
	protected HtmlDropDownList ddlBancos;
	protected HtmlInputTextarea txtDescripBco;
	protected HtmlJspPanel jsp001;
	protected HtmlDialogWindow dwAgregarNuevaEquiv;
	protected HtmlOutputText lblMsgNuevaconf;
	protected HtmlLink lnkEditarEquivalencia;
	protected HtmlCheckBox chkInactivarConfig;
	protected HtmlOutputText lbletInactivarConfig;
	protected UINamingContainer getVfCrudEquivTipodocs() {
		if (vfCrudEquivTipodocs == null) {
			vfCrudEquivTipodocs = (UINamingContainer) findComponentInRoot("vfCrudEquivTipodocs");
		}
		return vfCrudEquivTipodocs;
	}

	protected HtmlScriptCollector getScConsultar() {
		if (scConsultar == null) {
			scConsultar = (HtmlScriptCollector) findComponentInRoot("scConsultar");
		}
		return scConsultar;
	}

	protected HtmlForm getFrmConsultar() {
		if (frmConsultar == null) {
			frmConsultar = (HtmlForm) findComponentInRoot("frmConsultar");
		}
		return frmConsultar;
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

	protected HtmlOutputText getLblTitulo1eqtd() {
		if (lblTitulo1eqtd == null) {
			lblTitulo1eqtd = (HtmlOutputText) findComponentInRoot("lblTitulo1eqtd");
		}
		return lblTitulo1eqtd;
	}

	protected HtmlOutputText getLblTituloeqtd() {
		if (lblTituloeqtd == null) {
			lblTituloeqtd = (HtmlOutputText) findComponentInRoot("lblTituloeqtd");
		}
		return lblTituloeqtd;
	}

	protected HtmlOutputText getLblHeader() {
		if (lblHeader == null) {
			lblHeader = (HtmlOutputText) findComponentInRoot("lblHeader");
		}
		return lblHeader;
	}

	protected HtmlLink getLnkEditarConfiguracion() {
		if (lnkEditarConfiguracion == null) {
			lnkEditarConfiguracion = (HtmlLink) findComponentInRoot("lnkEditarConfiguracion");
		}
		return lnkEditarConfiguracion;
	}

	protected HtmlOutputText getLblnombrebco0() {
		if (lblnombrebco0 == null) {
			lblnombrebco0 = (HtmlOutputText) findComponentInRoot("lblnombrebco0");
		}
		return lblnombrebco0;
	}

	protected HtmlOutputText getLblnombrebco1() {
		if (lblnombrebco1 == null) {
			lblnombrebco1 = (HtmlOutputText) findComponentInRoot("lblnombrebco1");
		}
		return lblnombrebco1;
	}

	protected HtmlGridView getGvEquivalenciasDocs() {
		if (gvEquivalenciasDocs == null) {
			gvEquivalenciasDocs = (HtmlGridView) findComponentInRoot("gvEquivalenciasDocs");
		}
		return gvEquivalenciasDocs;
	}

	protected HtmlColumn getColnkEditarConfig() {
		if (colnkEditarConfig == null) {
			colnkEditarConfig = (HtmlColumn) findComponentInRoot("colnkEditarConfig");
		}
		return colnkEditarConfig;
	}

	protected HtmlColumn getCoNombreMovBanco() {
		if (coNombreMovBanco == null) {
			coNombreMovBanco = (HtmlColumn) findComponentInRoot("coNombreMovBanco");
		}
		return coNombreMovBanco;
	}

	protected HtmlOutputText getLblEditarConfig() {
		if (lblEditarConfig == null) {
			lblEditarConfig = (HtmlOutputText) findComponentInRoot("lblEditarConfig");
		}
		return lblEditarConfig;
	}

	protected HtmlOutputText getLblCodigoMov0() {
		if (lblCodigoMov0 == null) {
			lblCodigoMov0 = (HtmlOutputText) findComponentInRoot("lblCodigoMov0");
		}
		return lblCodigoMov0;
	}

	protected HtmlOutputText getLblCodigoMov1() {
		if (lblCodigoMov1 == null) {
			lblCodigoMov1 = (HtmlOutputText) findComponentInRoot("lblCodigoMov1");
		}
		return lblCodigoMov1;
	}

	protected HtmlColumn getCoCodigoMov() {
		if (coCodigoMov == null) {
			coCodigoMov = (HtmlColumn) findComponentInRoot("coCodigoMov");
		}
		return coCodigoMov;
	}

	protected HtmlColumn getCoCodigoBanco() {
		if (coCodigoBanco == null) {
			coCodigoBanco = (HtmlColumn) findComponentInRoot("coCodigoBanco");
		}
		return coCodigoBanco;
	}

	protected HtmlOutputText getLblCodigoBco0() {
		if (lblCodigoBco0 == null) {
			lblCodigoBco0 = (HtmlOutputText) findComponentInRoot("lblCodigoBco0");
		}
		return lblCodigoBco0;
	}

	protected HtmlOutputText getLblCodigoBco1() {
		if (lblCodigoBco1 == null) {
			lblCodigoBco1 = (HtmlOutputText) findComponentInRoot("lblCodigoBco1");
		}
		return lblCodigoBco1;
	}

	protected HtmlColumn getCoCodMovimientoCaja() {
		if (coCodMovimientoCaja == null) {
			coCodMovimientoCaja = (HtmlColumn) findComponentInRoot("coCodMovimientoCaja");
		}
		return coCodMovimientoCaja;
	}

	protected HtmlOutputText getLblCodMovCaja0() {
		if (lblCodMovCaja0 == null) {
			lblCodMovCaja0 = (HtmlOutputText) findComponentInRoot("lblCodMovCaja0");
		}
		return lblCodMovCaja0;
	}

	protected HtmlOutputText getLblCodMovCaja1() {
		if (lblCodMovCaja1 == null) {
			lblCodMovCaja1 = (HtmlOutputText) findComponentInRoot("lblCodMovCaja1");
		}
		return lblCodMovCaja1;
	}

	protected HtmlColumn getCoNomMovimientoCaja() {
		if (coNomMovimientoCaja == null) {
			coNomMovimientoCaja = (HtmlColumn) findComponentInRoot("coNomMovimientoCaja");
		}
		return coNomMovimientoCaja;
	}

	protected HtmlOutputText getLblNomMovCaja0() {
		if (lblNomMovCaja0 == null) {
			lblNomMovCaja0 = (HtmlOutputText) findComponentInRoot("lblNomMovCaja0");
		}
		return lblNomMovCaja0;
	}

	protected HtmlOutputText getLblNomMovCaja1() {
		if (lblNomMovCaja1 == null) {
			lblNomMovCaja1 = (HtmlOutputText) findComponentInRoot("lblNomMovCaja1");
		}
		return lblNomMovCaja1;
	}

	protected HtmlColumn getCoDescripAsocia() {
		if (coDescripAsocia == null) {
			coDescripAsocia = (HtmlColumn) findComponentInRoot("coDescripAsocia");
		}
		return coDescripAsocia;
	}

	protected HtmlOutputText getLblDescripAsocia0() {
		if (lblDescripAsocia0 == null) {
			lblDescripAsocia0 = (HtmlOutputText) findComponentInRoot("lblDescripAsocia0");
		}
		return lblDescripAsocia0;
	}

	protected HtmlOutputText getLblDescripAsocia1() {
		if (lblDescripAsocia1 == null) {
			lblDescripAsocia1 = (HtmlOutputText) findComponentInRoot("lblDescripAsocia1");
		}
		return lblDescripAsocia1;
	}

	protected HtmlLink getLnkAgregarNuevaConfig() {
		if (lnkAgregarNuevaConfig == null) {
			lnkAgregarNuevaConfig = (HtmlLink) findComponentInRoot("lnkAgregarNuevaConfig");
		}
		return lnkAgregarNuevaConfig;
	}

	protected HtmlLink getLnkRefrescarDatos() {
		if (lnkRefrescarDatos == null) {
			lnkRefrescarDatos = (HtmlLink) findComponentInRoot("lnkRefrescarDatos");
		}
		return lnkRefrescarDatos;
	}

	protected HtmlDialogWindowHeader getHdFactura() {
		if (hdFactura == null) {
			hdFactura = (HtmlDialogWindowHeader) findComponentInRoot("hdFactura");
		}
		return hdFactura;
	}

	protected HtmlDialogWindowClientEvents getClDwAgregarNuevaConfig() {
		if (clDwAgregarNuevaConfig == null) {
			clDwAgregarNuevaConfig = (HtmlDialogWindowClientEvents) findComponentInRoot("clDwAgregarNuevaConfig");
		}
		return clDwAgregarNuevaConfig;
	}

	protected HtmlDialogWindowRoundedCorners getDwcrDwAgregarNuevaConfig() {
		if (dwcrDwAgregarNuevaConfig == null) {
			dwcrDwAgregarNuevaConfig = (HtmlDialogWindowRoundedCorners) findComponentInRoot("dwcrDwAgregarNuevaConfig");
		}
		return dwcrDwAgregarNuevaConfig;
	}

	protected HtmlDialogWindowContentPane getDwcpDwAgregarNuevaConfig() {
		if (dwcpDwAgregarNuevaConfig == null) {
			dwcpDwAgregarNuevaConfig = (HtmlDialogWindowContentPane) findComponentInRoot("dwcpDwAgregarNuevaConfig");
		}
		return dwcpDwAgregarNuevaConfig;
	}

	protected HtmlLink getLnkAgregarEquivalencia() {
		if (lnkAgregarEquivalencia == null) {
			lnkAgregarEquivalencia = (HtmlLink) findComponentInRoot("lnkAgregarEquivalencia");
		}
		return lnkAgregarEquivalencia;
	}

	protected HtmlLink getLnkCerrarAgregarNuevo() {
		if (lnkCerrarAgregarNuevo == null) {
			lnkCerrarAgregarNuevo = (HtmlLink) findComponentInRoot("lnkCerrarAgregarNuevo");
		}
		return lnkCerrarAgregarNuevo;
	}

	protected HtmlInputText getTxtCodigoBco() {
		if (txtCodigoBco == null) {
			txtCodigoBco = (HtmlInputText) findComponentInRoot("txtCodigoBco");
		}
		return txtCodigoBco;
	}

	protected HtmlOutputText getLblCodigoBco() {
		if (lblCodigoBco == null) {
			lblCodigoBco = (HtmlOutputText) findComponentInRoot("lblCodigoBco");
		}
		return lblCodigoBco;
	}

	protected HtmlOutputText getLblCodigoCaja() {
		if (lblCodigoCaja == null) {
			lblCodigoCaja = (HtmlOutputText) findComponentInRoot("lblCodigoCaja");
		}
		return lblCodigoCaja;
	}

	protected HtmlDropDownList getDdlCodigoCaja() {
		if (ddlCodigoCaja == null) {
			ddlCodigoCaja = (HtmlDropDownList) findComponentInRoot("ddlCodigoCaja");
		}
		return ddlCodigoCaja;
	}

	protected HtmlOutputText getLblNombreCodigo() {
		if (lblNombreCodigo == null) {
			lblNombreCodigo = (HtmlOutputText) findComponentInRoot("lblNombreCodigo");
		}
		return lblNombreCodigo;
	}

	protected HtmlInputText getTxtNombreCodigo() {
		if (txtNombreCodigo == null) {
			txtNombreCodigo = (HtmlInputText) findComponentInRoot("txtNombreCodigo");
		}
		return txtNombreCodigo;
	}

	protected HtmlOutputText getLblDescripcion() {
		if (lblDescripcion == null) {
			lblDescripcion = (HtmlOutputText) findComponentInRoot("lblDescripcion");
		}
		return lblDescripcion;
	}

	protected HtmlOutputText getLblIdBanco() {
		if (lblIdBanco == null) {
			lblIdBanco = (HtmlOutputText) findComponentInRoot("lblIdBanco");
		}
		return lblIdBanco;
	}

	protected HtmlDropDownList getDdlBancos() {
		if (ddlBancos == null) {
			ddlBancos = (HtmlDropDownList) findComponentInRoot("ddlBancos");
		}
		return ddlBancos;
	}

	protected HtmlInputTextarea getTxtDescripBco() {
		if (txtDescripBco == null) {
			txtDescripBco = (HtmlInputTextarea) findComponentInRoot("txtDescripBco");
		}
		return txtDescripBco;
	}

	protected HtmlJspPanel getJsp001() {
		if (jsp001 == null) {
			jsp001 = (HtmlJspPanel) findComponentInRoot("jsp001");
		}
		return jsp001;
	}

	protected HtmlDialogWindow getDwAgregarNuevaEquiv() {
		if (dwAgregarNuevaEquiv == null) {
			dwAgregarNuevaEquiv = (HtmlDialogWindow) findComponentInRoot("dwAgregarNuevaEquiv");
		}
		return dwAgregarNuevaEquiv;
	}

	protected HtmlOutputText getLblMsgNuevaconf() {
		if (lblMsgNuevaconf == null) {
			lblMsgNuevaconf = (HtmlOutputText) findComponentInRoot("lblMsgNuevaconf");
		}
		return lblMsgNuevaconf;
	}

	protected HtmlLink getLnkEditarEquivalencia() {
		if (lnkEditarEquivalencia == null) {
			lnkEditarEquivalencia = (HtmlLink) findComponentInRoot("lnkEditarEquivalencia");
		}
		return lnkEditarEquivalencia;
	}

	protected HtmlCheckBox getChkInactivarConfig() {
		if (chkInactivarConfig == null) {
			chkInactivarConfig = (HtmlCheckBox) findComponentInRoot("chkInactivarConfig");
		}
		return chkInactivarConfig;
	}

	protected HtmlOutputText getLbletInactivarConfig() {
		if (lbletInactivarConfig == null) {
			lbletInactivarConfig = (HtmlOutputText) findComponentInRoot("lbletInactivarConfig");
		}
		return lbletInactivarConfig;
	}

}
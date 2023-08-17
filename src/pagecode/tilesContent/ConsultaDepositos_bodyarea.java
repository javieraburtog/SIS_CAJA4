/**
 * 
 */
package pagecode.tilesContent;

import pagecode.PageCodeBase;
import javax.faces.component.UINamingContainer;
import javax.faces.component.html.HtmlForm;
import com.infragistics.faces.menu.component.html.HtmlMenu;
import com.infragistics.faces.menu.component.html.HtmlMenuItem;
import javax.faces.component.html.HtmlOutputText;
import com.ibm.faces.component.html.HtmlJspPanel;
import javax.faces.component.html.HtmlPanelGrid;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
import javax.faces.component.html.HtmlInputText;
import com.infragistics.faces.input.component.html.HtmlDateChooser;
import com.infragistics.faces.shared.component.html.HtmlLink;
import com.infragistics.faces.grid.component.html.HtmlGridView;
import com.infragistics.faces.grid.component.html.HtmlColumn;
import javax.faces.component.html.HtmlPanelGroup;
import com.infragistics.faces.grid.component.html.HtmlGridAgFunction;

/**
 * @author Carlos Hernandez
 *
 */
public class ConsultaDepositos_bodyarea extends PageCodeBase {

	protected UINamingContainer vfConsultaDepositos;
	protected HtmlForm frmConsultaDepositos;
	protected HtmlMenu menu1;
	protected HtmlMenuItem item0;
	protected HtmlMenuItem item1;
	protected HtmlMenuItem item2;
	protected HtmlOutputText lblTitArqueoCaja0;
	protected HtmlOutputText lblTitArqueoCaja;
	protected HtmlOutputText lblPsAbono1;
	protected HtmlJspPanel jspFiltroDepsCaja1phicImageEx;
	protected HtmlPanelGrid pgrCmFcAgDepsCaja;
	protected HtmlOutputText lblCmfcCaja;
	protected HtmlDropDownList ddlCmfcCaja;
	protected HtmlOutputText lblCmfcCompania;
	protected HtmlDropDownList ddlCmfcCompania;
	protected HtmlInputText txtCmfcMontoDep;
	protected HtmlInputText txtCmfcMontoDepMaxim;
	protected HtmlOutputText lblCmfcMoneda;
	protected HtmlOutputText lblCmfcEstado;
	protected HtmlOutputText lblCmfbFechas;
	protected HtmlDateChooser txtCmfbFechaIni;
	protected HtmlDateChooser txtCmfbFechaFin;
	protected HtmlLink lnkCmAdFiltrarDepsCaja;
	protected HtmlDropDownList ddlCmfcMoneda;
	protected HtmlDropDownList ddlCmfcEstadoDep;
	protected HtmlOutputText lblespacio;
	protected HtmlGridView gvDepositosCaja;
	protected HtmlOutputText lblHeader;
	protected HtmlOutputText lbldcNobatch0;
	protected HtmlOutputText lbldcNobatch1;
	protected HtmlColumn codcNobatch;
	protected HtmlOutputText lbldcMoneda0;
	protected HtmlOutputText lbldcMoneda1;
	protected HtmlColumn codcMoneda;
	protected HtmlOutputText lblCaid0;
	protected HtmlOutputText lbldcCaid1;
	protected HtmlColumn coNoCajaDep;
	protected HtmlOutputText lbldcCodcomp0;
	protected HtmlOutputText lbldcCodcomp1;
	protected HtmlColumn coCompania;
	protected HtmlOutputText lbldcreferencia0;
	protected HtmlOutputText lbldcreferencia1;
	protected HtmlColumn codcReferencia;
	protected HtmlOutputText lbldcfecha0;
	protected HtmlOutputText lbldcfecha1;
	protected HtmlColumn cofechaDc;
	protected HtmlColumn coDcContador;
	protected HtmlOutputText lbldcContador0;
	protected HtmlOutputText lbldcContador1;
	protected HtmlPanelGroup pnlgrDepsCaja;
	protected HtmlGridAgFunction agFnContarDepsCaja;
	protected HtmlOutputText lblCmfcEtMontoHasta;
	protected HtmlOutputText lblCmfcUsuario;
	protected HtmlInputText txtCmAdfcNoReferencia;
	protected HtmlInputText txtCmAdfcUsuarioDepc;
	protected HtmlOutputText lblHdrCdDepsBanco;
	protected HtmlOutputText lblCmDbNocuenta0;
	protected HtmlOutputText lblCmDbNocuenta1;
	protected HtmlOutputText lblCmreferencia0;
	protected HtmlOutputText lblCmreferencia1;
	protected HtmlOutputText lblCmMontodebito0;
	protected HtmlOutputText lblCmMontodebito1;
	protected HtmlOutputText lblCmFechaDeps0;
	protected HtmlOutputText lblCmFechaDeps1;
	protected HtmlOutputText lblCmDescripcion0;
	protected HtmlOutputText lblCmDescripcion1;
	protected HtmlColumn coCaNoCuenta;
	protected HtmlColumn coCaNoReferencia;
	protected HtmlColumn coCmMontodebito;
	protected HtmlColumn coCmFechaDep;
	protected HtmlColumn codCmDescripcion;
	protected HtmlGridView gvcdbDepositosBco;
	protected HtmlPanelGroup pnlgrDepsBco;
	protected HtmlOutputText lblTo;
	protected HtmlOutputText l;
	protected HtmlOutputText lblTotalDpsBco;
	protected HtmlGridAgFunction agFnContarDepsBco;
	protected HtmlOutputText lblFtrBcoConfirmador;
	protected HtmlOutputText lblFtrBcoContador;
	protected HtmlInputText txtFtrBcoConfirmador;
	protected HtmlInputText txtFtrBcoContador;
	protected HtmlOutputText lblFtrBcoBanco;
	protected HtmlOutputText lblFtrBcoCuenta;
	protected HtmlDropDownList ddlFtrBcoBanco;
	protected HtmlDropDownList ddlFtrBcoCuenta;
	protected HtmlOutputText lblFtrBcoFechas;
	protected HtmlDateChooser dcFtrBcoFechaIni;
	protected HtmlDateChooser dcFtrBcoFechaFin;
	protected HtmlOutputText lblFtrBcoEstado;
	protected HtmlOutputText lblFtrBcoMoneda;
	protected HtmlDropDownList ddlFtrBcoEstado;
	protected HtmlDropDownList ddlFtrBcoMoneda;
	protected HtmlOutputText lblFtrBcoMonto1;
	protected HtmlInputText txtFtrBcoMontoMin;
	protected HtmlInputText txtFtrBcoMontoMax;
	protected HtmlInputText txtFtrBcoReferencia;
	protected HtmlOutputText lblespacio1;
	protected HtmlOutputText lblFtrBcoMonto;
	protected HtmlLink lnkFtrBcoBuscar;
	protected UINamingContainer getVfConsultaDepositos() {
		if (vfConsultaDepositos == null) {
			vfConsultaDepositos = (UINamingContainer) findComponentInRoot("vfConsultaDepositos");
		}
		return vfConsultaDepositos;
	}

	protected HtmlForm getFrmConsultaDepositos() {
		if (frmConsultaDepositos == null) {
			frmConsultaDepositos = (HtmlForm) findComponentInRoot("frmConsultaDepositos");
		}
		return frmConsultaDepositos;
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

	protected HtmlOutputText getLblTitArqueoCaja0() {
		if (lblTitArqueoCaja0 == null) {
			lblTitArqueoCaja0 = (HtmlOutputText) findComponentInRoot("lblTitArqueoCaja0");
		}
		return lblTitArqueoCaja0;
	}

	protected HtmlOutputText getLblTitArqueoCaja() {
		if (lblTitArqueoCaja == null) {
			lblTitArqueoCaja = (HtmlOutputText) findComponentInRoot("lblTitArqueoCaja");
		}
		return lblTitArqueoCaja;
	}

	protected HtmlOutputText getLblPsAbono1() {
		if (lblPsAbono1 == null) {
			lblPsAbono1 = (HtmlOutputText) findComponentInRoot("lblPsAbono1");
		}
		return lblPsAbono1;
	}

	protected HtmlJspPanel getJspFiltroDepsCaja1phicImageEx() {
		if (jspFiltroDepsCaja1phicImageEx == null) {
			jspFiltroDepsCaja1phicImageEx = (HtmlJspPanel) findComponentInRoot("jspFiltroDepsCaja1phicImageEx");
		}
		return jspFiltroDepsCaja1phicImageEx;
	}

	protected HtmlPanelGrid getPgrCmFcAgDepsCaja() {
		if (pgrCmFcAgDepsCaja == null) {
			pgrCmFcAgDepsCaja = (HtmlPanelGrid) findComponentInRoot("pgrCmFcAgDepsCaja");
		}
		return pgrCmFcAgDepsCaja;
	}

	protected HtmlOutputText getLblCmfcCaja() {
		if (lblCmfcCaja == null) {
			lblCmfcCaja = (HtmlOutputText) findComponentInRoot("lblCmfcCaja");
		}
		return lblCmfcCaja;
	}

	protected HtmlDropDownList getDdlCmfcCaja() {
		if (ddlCmfcCaja == null) {
			ddlCmfcCaja = (HtmlDropDownList) findComponentInRoot("ddlCmfcCaja");
		}
		return ddlCmfcCaja;
	}

	protected HtmlOutputText getLblCmfcCompania() {
		if (lblCmfcCompania == null) {
			lblCmfcCompania = (HtmlOutputText) findComponentInRoot("lblCmfcCompania");
		}
		return lblCmfcCompania;
	}

	protected HtmlDropDownList getDdlCmfcCompania() {
		if (ddlCmfcCompania == null) {
			ddlCmfcCompania = (HtmlDropDownList) findComponentInRoot("ddlCmfcCompania");
		}
		return ddlCmfcCompania;
	}

	protected HtmlInputText getTxtCmfcMontoDep() {
		if (txtCmfcMontoDep == null) {
			txtCmfcMontoDep = (HtmlInputText) findComponentInRoot("txtCmfcMontoDep");
		}
		return txtCmfcMontoDep;
	}

	protected HtmlInputText getTxtCmfcMontoDepMaxim() {
		if (txtCmfcMontoDepMaxim == null) {
			txtCmfcMontoDepMaxim = (HtmlInputText) findComponentInRoot("txtCmfcMontoDepMaxim");
		}
		return txtCmfcMontoDepMaxim;
	}

	protected HtmlOutputText getLblCmfcMoneda() {
		if (lblCmfcMoneda == null) {
			lblCmfcMoneda = (HtmlOutputText) findComponentInRoot("lblCmfcMoneda");
		}
		return lblCmfcMoneda;
	}

	protected HtmlOutputText getLblCmfcEstado() {
		if (lblCmfcEstado == null) {
			lblCmfcEstado = (HtmlOutputText) findComponentInRoot("lblCmfcEstado");
		}
		return lblCmfcEstado;
	}

	protected HtmlOutputText getLblCmfbFechas() {
		if (lblCmfbFechas == null) {
			lblCmfbFechas = (HtmlOutputText) findComponentInRoot("lblCmfbFechas");
		}
		return lblCmfbFechas;
	}

	protected HtmlDateChooser getTxtCmfbFechaIni() {
		if (txtCmfbFechaIni == null) {
			txtCmfbFechaIni = (HtmlDateChooser) findComponentInRoot("txtCmfbFechaIni");
		}
		return txtCmfbFechaIni;
	}

	protected HtmlDateChooser getTxtCmfbFechaFin() {
		if (txtCmfbFechaFin == null) {
			txtCmfbFechaFin = (HtmlDateChooser) findComponentInRoot("txtCmfbFechaFin");
		}
		return txtCmfbFechaFin;
	}

	protected HtmlLink getLnkCmAdFiltrarDepsCaja() {
		if (lnkCmAdFiltrarDepsCaja == null) {
			lnkCmAdFiltrarDepsCaja = (HtmlLink) findComponentInRoot("lnkCmAdFiltrarDepsCaja");
		}
		return lnkCmAdFiltrarDepsCaja;
	}

	protected HtmlDropDownList getDdlCmfcMoneda() {
		if (ddlCmfcMoneda == null) {
			ddlCmfcMoneda = (HtmlDropDownList) findComponentInRoot("ddlCmfcMoneda");
		}
		return ddlCmfcMoneda;
	}

	protected HtmlDropDownList getDdlCmfcEstadoDep() {
		if (ddlCmfcEstadoDep == null) {
			ddlCmfcEstadoDep = (HtmlDropDownList) findComponentInRoot("ddlCmfcEstadoDep");
		}
		return ddlCmfcEstadoDep;
	}

	protected HtmlOutputText getLblespacio() {
		if (lblespacio == null) {
			lblespacio = (HtmlOutputText) findComponentInRoot("lblespacio");
		}
		return lblespacio;
	}

	protected HtmlGridView getGvDepositosCaja() {
		if (gvDepositosCaja == null) {
			gvDepositosCaja = (HtmlGridView) findComponentInRoot("gvDepositosCaja");
		}
		return gvDepositosCaja;
	}

	protected HtmlOutputText getLblHeader() {
		if (lblHeader == null) {
			lblHeader = (HtmlOutputText) findComponentInRoot("lblHeader");
		}
		return lblHeader;
	}

	protected HtmlOutputText getLbldcNobatch0() {
		if (lbldcNobatch0 == null) {
			lbldcNobatch0 = (HtmlOutputText) findComponentInRoot("lbldcNobatch0");
		}
		return lbldcNobatch0;
	}

	protected HtmlOutputText getLbldcNobatch1() {
		if (lbldcNobatch1 == null) {
			lbldcNobatch1 = (HtmlOutputText) findComponentInRoot("lbldcNobatch1");
		}
		return lbldcNobatch1;
	}

	protected HtmlColumn getCodcNobatch() {
		if (codcNobatch == null) {
			codcNobatch = (HtmlColumn) findComponentInRoot("codcNobatch");
		}
		return codcNobatch;
	}

	protected HtmlOutputText getLbldcMoneda0() {
		if (lbldcMoneda0 == null) {
			lbldcMoneda0 = (HtmlOutputText) findComponentInRoot("lbldcMoneda0");
		}
		return lbldcMoneda0;
	}

	protected HtmlOutputText getLbldcMoneda1() {
		if (lbldcMoneda1 == null) {
			lbldcMoneda1 = (HtmlOutputText) findComponentInRoot("lbldcMoneda1");
		}
		return lbldcMoneda1;
	}

	protected HtmlColumn getCodcMoneda() {
		if (codcMoneda == null) {
			codcMoneda = (HtmlColumn) findComponentInRoot("codcMoneda");
		}
		return codcMoneda;
	}

	protected HtmlOutputText getLblCaid0() {
		if (lblCaid0 == null) {
			lblCaid0 = (HtmlOutputText) findComponentInRoot("lblCaid0");
		}
		return lblCaid0;
	}

	protected HtmlOutputText getLbldcCaid1() {
		if (lbldcCaid1 == null) {
			lbldcCaid1 = (HtmlOutputText) findComponentInRoot("lbldcCaid1");
		}
		return lbldcCaid1;
	}

	protected HtmlColumn getCoNoCajaDep() {
		if (coNoCajaDep == null) {
			coNoCajaDep = (HtmlColumn) findComponentInRoot("coNoCajaDep");
		}
		return coNoCajaDep;
	}

	protected HtmlOutputText getLbldcCodcomp0() {
		if (lbldcCodcomp0 == null) {
			lbldcCodcomp0 = (HtmlOutputText) findComponentInRoot("lbldcCodcomp0");
		}
		return lbldcCodcomp0;
	}

	protected HtmlOutputText getLbldcCodcomp1() {
		if (lbldcCodcomp1 == null) {
			lbldcCodcomp1 = (HtmlOutputText) findComponentInRoot("lbldcCodcomp1");
		}
		return lbldcCodcomp1;
	}

	protected HtmlColumn getCoCompania() {
		if (coCompania == null) {
			coCompania = (HtmlColumn) findComponentInRoot("coCompania");
		}
		return coCompania;
	}

	protected HtmlOutputText getLbldcreferencia0() {
		if (lbldcreferencia0 == null) {
			lbldcreferencia0 = (HtmlOutputText) findComponentInRoot("lbldcreferencia0");
		}
		return lbldcreferencia0;
	}

	protected HtmlOutputText getLbldcreferencia1() {
		if (lbldcreferencia1 == null) {
			lbldcreferencia1 = (HtmlOutputText) findComponentInRoot("lbldcreferencia1");
		}
		return lbldcreferencia1;
	}

	protected HtmlColumn getCodcReferencia() {
		if (codcReferencia == null) {
			codcReferencia = (HtmlColumn) findComponentInRoot("codcReferencia");
		}
		return codcReferencia;
	}

	protected HtmlOutputText getLbldcfecha0() {
		if (lbldcfecha0 == null) {
			lbldcfecha0 = (HtmlOutputText) findComponentInRoot("lbldcfecha0");
		}
		return lbldcfecha0;
	}

	protected HtmlOutputText getLbldcfecha1() {
		if (lbldcfecha1 == null) {
			lbldcfecha1 = (HtmlOutputText) findComponentInRoot("lbldcfecha1");
		}
		return lbldcfecha1;
	}

	protected HtmlColumn getCofechaDc() {
		if (cofechaDc == null) {
			cofechaDc = (HtmlColumn) findComponentInRoot("cofechaDc");
		}
		return cofechaDc;
	}

	protected HtmlColumn getCoDcContador() {
		if (coDcContador == null) {
			coDcContador = (HtmlColumn) findComponentInRoot("coDcContador");
		}
		return coDcContador;
	}

	protected HtmlOutputText getLbldcContador0() {
		if (lbldcContador0 == null) {
			lbldcContador0 = (HtmlOutputText) findComponentInRoot("lbldcContador0");
		}
		return lbldcContador0;
	}

	protected HtmlOutputText getLbldcContador1() {
		if (lbldcContador1 == null) {
			lbldcContador1 = (HtmlOutputText) findComponentInRoot("lbldcContador1");
		}
		return lbldcContador1;
	}

	protected HtmlPanelGroup getPnlgrDepsCaja() {
		if (pnlgrDepsCaja == null) {
			pnlgrDepsCaja = (HtmlPanelGroup) findComponentInRoot("pnlgrDepsCaja");
		}
		return pnlgrDepsCaja;
	}

	protected HtmlGridAgFunction getAgFnContarDepsCaja() {
		if (agFnContarDepsCaja == null) {
			agFnContarDepsCaja = (HtmlGridAgFunction) findComponentInRoot("agFnContarDepsCaja");
		}
		return agFnContarDepsCaja;
	}

	protected HtmlOutputText getLblCmfcEtMontoHasta() {
		if (lblCmfcEtMontoHasta == null) {
			lblCmfcEtMontoHasta = (HtmlOutputText) findComponentInRoot("lblCmfcEtMontoHasta");
		}
		return lblCmfcEtMontoHasta;
	}

	protected HtmlOutputText getLblCmfcUsuario() {
		if (lblCmfcUsuario == null) {
			lblCmfcUsuario = (HtmlOutputText) findComponentInRoot("lblCmfcUsuario");
		}
		return lblCmfcUsuario;
	}

	protected HtmlInputText getTxtCmAdfcNoReferencia() {
		if (txtCmAdfcNoReferencia == null) {
			txtCmAdfcNoReferencia = (HtmlInputText) findComponentInRoot("txtCmAdfcNoReferencia");
		}
		return txtCmAdfcNoReferencia;
	}

	protected HtmlInputText getTxtCmAdfcUsuarioDepc() {
		if (txtCmAdfcUsuarioDepc == null) {
			txtCmAdfcUsuarioDepc = (HtmlInputText) findComponentInRoot("txtCmAdfcUsuarioDepc");
		}
		return txtCmAdfcUsuarioDepc;
	}

	protected HtmlOutputText getLblHdrCdDepsBanco() {
		if (lblHdrCdDepsBanco == null) {
			lblHdrCdDepsBanco = (HtmlOutputText) findComponentInRoot("lblHdrCdDepsBanco");
		}
		return lblHdrCdDepsBanco;
	}

	protected HtmlOutputText getLblCmDbNocuenta0() {
		if (lblCmDbNocuenta0 == null) {
			lblCmDbNocuenta0 = (HtmlOutputText) findComponentInRoot("lblCmDbNocuenta0");
		}
		return lblCmDbNocuenta0;
	}

	protected HtmlOutputText getLblCmDbNocuenta1() {
		if (lblCmDbNocuenta1 == null) {
			lblCmDbNocuenta1 = (HtmlOutputText) findComponentInRoot("lblCmDbNocuenta1");
		}
		return lblCmDbNocuenta1;
	}

	protected HtmlOutputText getLblCmreferencia0() {
		if (lblCmreferencia0 == null) {
			lblCmreferencia0 = (HtmlOutputText) findComponentInRoot("lblCmreferencia0");
		}
		return lblCmreferencia0;
	}

	protected HtmlOutputText getLblCmreferencia1() {
		if (lblCmreferencia1 == null) {
			lblCmreferencia1 = (HtmlOutputText) findComponentInRoot("lblCmreferencia1");
		}
		return lblCmreferencia1;
	}

	protected HtmlOutputText getLblCmMontodebito0() {
		if (lblCmMontodebito0 == null) {
			lblCmMontodebito0 = (HtmlOutputText) findComponentInRoot("lblCmMontodebito0");
		}
		return lblCmMontodebito0;
	}

	protected HtmlOutputText getLblCmMontodebito1() {
		if (lblCmMontodebito1 == null) {
			lblCmMontodebito1 = (HtmlOutputText) findComponentInRoot("lblCmMontodebito1");
		}
		return lblCmMontodebito1;
	}

	protected HtmlOutputText getLblCmFechaDeps0() {
		if (lblCmFechaDeps0 == null) {
			lblCmFechaDeps0 = (HtmlOutputText) findComponentInRoot("lblCmFechaDeps0");
		}
		return lblCmFechaDeps0;
	}

	protected HtmlOutputText getLblCmFechaDeps1() {
		if (lblCmFechaDeps1 == null) {
			lblCmFechaDeps1 = (HtmlOutputText) findComponentInRoot("lblCmFechaDeps1");
		}
		return lblCmFechaDeps1;
	}

	protected HtmlOutputText getLblCmDescripcion0() {
		if (lblCmDescripcion0 == null) {
			lblCmDescripcion0 = (HtmlOutputText) findComponentInRoot("lblCmDescripcion0");
		}
		return lblCmDescripcion0;
	}

	protected HtmlOutputText getLblCmDescripcion1() {
		if (lblCmDescripcion1 == null) {
			lblCmDescripcion1 = (HtmlOutputText) findComponentInRoot("lblCmDescripcion1");
		}
		return lblCmDescripcion1;
	}

	protected HtmlColumn getCoCaNoCuenta() {
		if (coCaNoCuenta == null) {
			coCaNoCuenta = (HtmlColumn) findComponentInRoot("coCaNoCuenta");
		}
		return coCaNoCuenta;
	}

	protected HtmlColumn getCoCaNoReferencia() {
		if (coCaNoReferencia == null) {
			coCaNoReferencia = (HtmlColumn) findComponentInRoot("coCaNoReferencia");
		}
		return coCaNoReferencia;
	}

	protected HtmlColumn getCoCmMontodebito() {
		if (coCmMontodebito == null) {
			coCmMontodebito = (HtmlColumn) findComponentInRoot("coCmMontodebito");
		}
		return coCmMontodebito;
	}

	protected HtmlColumn getCoCmFechaDep() {
		if (coCmFechaDep == null) {
			coCmFechaDep = (HtmlColumn) findComponentInRoot("coCmFechaDep");
		}
		return coCmFechaDep;
	}

	protected HtmlColumn getCodCmDescripcion() {
		if (codCmDescripcion == null) {
			codCmDescripcion = (HtmlColumn) findComponentInRoot("codCmDescripcion");
		}
		return codCmDescripcion;
	}

	protected HtmlGridView getGvcdbDepositosBco() {
		if (gvcdbDepositosBco == null) {
			gvcdbDepositosBco = (HtmlGridView) findComponentInRoot("gvcdbDepositosBco");
		}
		return gvcdbDepositosBco;
	}

	protected HtmlPanelGroup getPnlgrDepsBco() {
		if (pnlgrDepsBco == null) {
			pnlgrDepsBco = (HtmlPanelGroup) findComponentInRoot("pnlgrDepsBco");
		}
		return pnlgrDepsBco;
	}

	protected HtmlOutputText getLblTo() {
		if (lblTo == null) {
			lblTo = (HtmlOutputText) findComponentInRoot("lblTo");
		}
		return lblTo;
	}

	protected HtmlOutputText getL() {
		if (l == null) {
			l = (HtmlOutputText) findComponentInRoot("l");
		}
		return l;
	}

	protected HtmlOutputText getLblTotalDpsBco() {
		if (lblTotalDpsBco == null) {
			lblTotalDpsBco = (HtmlOutputText) findComponentInRoot("lblTotalDpsBco");
		}
		return lblTotalDpsBco;
	}

	protected HtmlGridAgFunction getAgFnContarDepsBco() {
		if (agFnContarDepsBco == null) {
			agFnContarDepsBco = (HtmlGridAgFunction) findComponentInRoot("agFnContarDepsBco");
		}
		return agFnContarDepsBco;
	}

	protected HtmlOutputText getLblFtrBcoConfirmador() {
		if (lblFtrBcoConfirmador == null) {
			lblFtrBcoConfirmador = (HtmlOutputText) findComponentInRoot("lblFtrBcoConfirmador");
		}
		return lblFtrBcoConfirmador;
	}

	protected HtmlOutputText getLblFtrBcoContador() {
		if (lblFtrBcoContador == null) {
			lblFtrBcoContador = (HtmlOutputText) findComponentInRoot("lblFtrBcoContador");
		}
		return lblFtrBcoContador;
	}

	protected HtmlInputText getTxtFtrBcoConfirmador() {
		if (txtFtrBcoConfirmador == null) {
			txtFtrBcoConfirmador = (HtmlInputText) findComponentInRoot("txtFtrBcoConfirmador");
		}
		return txtFtrBcoConfirmador;
	}

	protected HtmlInputText getTxtFtrBcoContador() {
		if (txtFtrBcoContador == null) {
			txtFtrBcoContador = (HtmlInputText) findComponentInRoot("txtFtrBcoContador");
		}
		return txtFtrBcoContador;
	}

	protected HtmlOutputText getLblFtrBcoBanco() {
		if (lblFtrBcoBanco == null) {
			lblFtrBcoBanco = (HtmlOutputText) findComponentInRoot("lblFtrBcoBanco");
		}
		return lblFtrBcoBanco;
	}

	protected HtmlOutputText getLblFtrBcoCuenta() {
		if (lblFtrBcoCuenta == null) {
			lblFtrBcoCuenta = (HtmlOutputText) findComponentInRoot("lblFtrBcoCuenta");
		}
		return lblFtrBcoCuenta;
	}

	protected HtmlDropDownList getDdlFtrBcoBanco() {
		if (ddlFtrBcoBanco == null) {
			ddlFtrBcoBanco = (HtmlDropDownList) findComponentInRoot("ddlFtrBcoBanco");
		}
		return ddlFtrBcoBanco;
	}

	protected HtmlDropDownList getDdlFtrBcoCuenta() {
		if (ddlFtrBcoCuenta == null) {
			ddlFtrBcoCuenta = (HtmlDropDownList) findComponentInRoot("ddlFtrBcoCuenta");
		}
		return ddlFtrBcoCuenta;
	}

	protected HtmlOutputText getLblFtrBcoFechas() {
		if (lblFtrBcoFechas == null) {
			lblFtrBcoFechas = (HtmlOutputText) findComponentInRoot("lblFtrBcoFechas");
		}
		return lblFtrBcoFechas;
	}

	protected HtmlDateChooser getDcFtrBcoFechaIni() {
		if (dcFtrBcoFechaIni == null) {
			dcFtrBcoFechaIni = (HtmlDateChooser) findComponentInRoot("dcFtrBcoFechaIni");
		}
		return dcFtrBcoFechaIni;
	}

	protected HtmlDateChooser getDcFtrBcoFechaFin() {
		if (dcFtrBcoFechaFin == null) {
			dcFtrBcoFechaFin = (HtmlDateChooser) findComponentInRoot("dcFtrBcoFechaFin");
		}
		return dcFtrBcoFechaFin;
	}

	protected HtmlOutputText getLblFtrBcoEstado() {
		if (lblFtrBcoEstado == null) {
			lblFtrBcoEstado = (HtmlOutputText) findComponentInRoot("lblFtrBcoEstado");
		}
		return lblFtrBcoEstado;
	}

	protected HtmlOutputText getLblFtrBcoMoneda() {
		if (lblFtrBcoMoneda == null) {
			lblFtrBcoMoneda = (HtmlOutputText) findComponentInRoot("lblFtrBcoMoneda");
		}
		return lblFtrBcoMoneda;
	}

	protected HtmlDropDownList getDdlFtrBcoEstado() {
		if (ddlFtrBcoEstado == null) {
			ddlFtrBcoEstado = (HtmlDropDownList) findComponentInRoot("ddlFtrBcoEstado");
		}
		return ddlFtrBcoEstado;
	}

	protected HtmlDropDownList getDdlFtrBcoMoneda() {
		if (ddlFtrBcoMoneda == null) {
			ddlFtrBcoMoneda = (HtmlDropDownList) findComponentInRoot("ddlFtrBcoMoneda");
		}
		return ddlFtrBcoMoneda;
	}

	protected HtmlOutputText getLblFtrBcoMonto1() {
		if (lblFtrBcoMonto1 == null) {
			lblFtrBcoMonto1 = (HtmlOutputText) findComponentInRoot("lblFtrBcoMonto1");
		}
		return lblFtrBcoMonto1;
	}

	protected HtmlInputText getTxtFtrBcoMontoMin() {
		if (txtFtrBcoMontoMin == null) {
			txtFtrBcoMontoMin = (HtmlInputText) findComponentInRoot("txtFtrBcoMontoMin");
		}
		return txtFtrBcoMontoMin;
	}

	protected HtmlInputText getTxtFtrBcoMontoMax() {
		if (txtFtrBcoMontoMax == null) {
			txtFtrBcoMontoMax = (HtmlInputText) findComponentInRoot("txtFtrBcoMontoMax");
		}
		return txtFtrBcoMontoMax;
	}

	protected HtmlInputText getTxtFtrBcoReferencia() {
		if (txtFtrBcoReferencia == null) {
			txtFtrBcoReferencia = (HtmlInputText) findComponentInRoot("txtFtrBcoReferencia");
		}
		return txtFtrBcoReferencia;
	}

	protected HtmlOutputText getLblespacio1() {
		if (lblespacio1 == null) {
			lblespacio1 = (HtmlOutputText) findComponentInRoot("lblespacio1");
		}
		return lblespacio1;
	}

	protected HtmlOutputText getLblFtrBcoMonto() {
		if (lblFtrBcoMonto == null) {
			lblFtrBcoMonto = (HtmlOutputText) findComponentInRoot("lblFtrBcoMonto");
		}
		return lblFtrBcoMonto;
	}

	protected HtmlLink getLnkFtrBcoBuscar() {
		if (lnkFtrBcoBuscar == null) {
			lnkFtrBcoBuscar = (HtmlLink) findComponentInRoot("lnkFtrBcoBuscar");
		}
		return lnkFtrBcoBuscar;
	}

}
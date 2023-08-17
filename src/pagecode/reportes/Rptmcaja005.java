/**
 * 
 */
package pagecode.reportes;

import pagecode.PageCodeBase;
import javax.faces.component.UINamingContainer;
import javax.faces.component.html.HtmlForm;
import com.ibm.faces.component.html.HtmlScriptCollector;
import com.infragistics.faces.menu.component.html.HtmlMenuItem;
import com.infragistics.faces.menu.component.html.HtmlMenu;
import javax.faces.component.html.HtmlOutputText;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
import com.casapellas.reportes.Rptmcaja005DAO;
import com.infragistics.faces.input.component.html.HtmlDateChooser;
import com.infragistics.faces.shared.component.html.HtmlLink;

/**
 * @author informatica
 *
 */
public class Rptmcaja005 extends PageCodeBase {

	protected UINamingContainer vfRptmcaja005;
	protected HtmlForm frmRptmcaja005;
	protected HtmlScriptCollector scRptmcaja005;
	protected HtmlMenuItem item1;
	protected HtmlMenuItem item2;
	protected HtmlMenu menu1;
	protected HtmlMenuItem item0;
	protected HtmlOutputText lbletRptmcaja005;
	protected HtmlOutputText lbletRptDetArqueoCaja1;
	protected HtmlOutputText lblFiltroCaja;
	protected HtmlDropDownList ddlFiltroCaja;
	protected HtmlOutputText lblFiltroComp;
	protected Rptmcaja005DAO mbRptmcaja005;
	protected HtmlOutputText lblfiltroMoneda;
	protected HtmlDropDownList ddlFiltroMoneda;
	protected HtmlOutputText lblFiltroFecha;
	protected HtmlDateChooser dcFechaInicio;
	protected HtmlDateChooser dcFechaFinal;
	protected HtmlLink lnkGenerarReporte;
	protected HtmlDropDownList ddlFiltroCompania;
	protected HtmlLink lnkCerrarRptmcaja005;
	protected UINamingContainer getVfRptmcaja005() {
		if (vfRptmcaja005 == null) {
			vfRptmcaja005 = (UINamingContainer) findComponentInRoot("vfRptmcaja005");
		}
		return vfRptmcaja005;
	}

	protected HtmlForm getFrmRptmcaja005() {
		if (frmRptmcaja005 == null) {
			frmRptmcaja005 = (HtmlForm) findComponentInRoot("frmRptmcaja005");
		}
		return frmRptmcaja005;
	}

	protected HtmlScriptCollector getScRptmcaja005() {
		if (scRptmcaja005 == null) {
			scRptmcaja005 = (HtmlScriptCollector) findComponentInRoot("scRptmcaja005");
		}
		return scRptmcaja005;
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

	protected HtmlOutputText getLbletRptmcaja005() {
		if (lbletRptmcaja005 == null) {
			lbletRptmcaja005 = (HtmlOutputText) findComponentInRoot("lbletRptmcaja005");
		}
		return lbletRptmcaja005;
	}

	protected HtmlOutputText getLbletRptDetArqueoCaja1() {
		if (lbletRptDetArqueoCaja1 == null) {
			lbletRptDetArqueoCaja1 = (HtmlOutputText) findComponentInRoot("lbletRptDetArqueoCaja1");
		}
		return lbletRptDetArqueoCaja1;
	}

	protected HtmlOutputText getLblFiltroCaja() {
		if (lblFiltroCaja == null) {
			lblFiltroCaja = (HtmlOutputText) findComponentInRoot("lblFiltroCaja");
		}
		return lblFiltroCaja;
	}

	protected HtmlDropDownList getDdlFiltroCaja() {
		if (ddlFiltroCaja == null) {
			ddlFiltroCaja = (HtmlDropDownList) findComponentInRoot("ddlFiltroCaja");
		}
		return ddlFiltroCaja;
	}

	protected HtmlOutputText getLblFiltroComp() {
		if (lblFiltroComp == null) {
			lblFiltroComp = (HtmlOutputText) findComponentInRoot("lblFiltroComp");
		}
		return lblFiltroComp;
	}

	/** 
	 * @managed-bean true
	 */
	protected Rptmcaja005DAO getMbRptmcaja005() {
		if (mbRptmcaja005 == null) {
			mbRptmcaja005 = (Rptmcaja005DAO) getFacesContext().getApplication()
					.createValueBinding("#{mbRptmcaja005}").getValue(
							getFacesContext());
		}
		return mbRptmcaja005;
	}

	/** 
	 * @managed-bean true
	 */
	protected void setMbRptmcaja005(Rptmcaja005DAO mbRptmcaja005) {
		this.mbRptmcaja005 = mbRptmcaja005;
	}

	protected HtmlOutputText getLblfiltroMoneda() {
		if (lblfiltroMoneda == null) {
			lblfiltroMoneda = (HtmlOutputText) findComponentInRoot("lblfiltroMoneda");
		}
		return lblfiltroMoneda;
	}

	protected HtmlDropDownList getDdlFiltroMoneda() {
		if (ddlFiltroMoneda == null) {
			ddlFiltroMoneda = (HtmlDropDownList) findComponentInRoot("ddlFiltroMoneda");
		}
		return ddlFiltroMoneda;
	}

	protected HtmlOutputText getLblFiltroFecha() {
		if (lblFiltroFecha == null) {
			lblFiltroFecha = (HtmlOutputText) findComponentInRoot("lblFiltroFecha");
		}
		return lblFiltroFecha;
	}

	protected HtmlDateChooser getDcFechaInicio() {
		if (dcFechaInicio == null) {
			dcFechaInicio = (HtmlDateChooser) findComponentInRoot("dcFechaInicio");
		}
		return dcFechaInicio;
	}

	protected HtmlDateChooser getDcFechaFinal() {
		if (dcFechaFinal == null) {
			dcFechaFinal = (HtmlDateChooser) findComponentInRoot("dcFechaFinal");
		}
		return dcFechaFinal;
	}

	protected HtmlLink getLnkGenerarReporte() {
		if (lnkGenerarReporte == null) {
			lnkGenerarReporte = (HtmlLink) findComponentInRoot("lnkGenerarReporte");
		}
		return lnkGenerarReporte;
	}

	protected HtmlDropDownList getDdlFiltroCompania() {
		if (ddlFiltroCompania == null) {
			ddlFiltroCompania = (HtmlDropDownList) findComponentInRoot("ddlFiltroCompania");
		}
		return ddlFiltroCompania;
	}

	protected HtmlLink getLnkCerrarRptmcaja005() {
		if (lnkCerrarRptmcaja005 == null) {
			lnkCerrarRptmcaja005 = (HtmlLink) findComponentInRoot("lnkCerrarRptmcaja005");
		}
		return lnkCerrarRptmcaja005;
	}

}
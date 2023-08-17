/**
 * 
 */
package pagecode.reportes;

import pagecode.PageCodeBase;
import com.casapellas.reportes.Rptmcaja007DAO;
import com.ibm.faces.component.html.HtmlScriptCollector;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.UINamingContainer;
import com.infragistics.faces.menu.component.html.HtmlMenu;
import com.infragistics.faces.menu.component.html.HtmlMenuItem;
import javax.faces.component.html.HtmlOutputText;
import com.infragistics.faces.shared.component.html.HtmlLink;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
import com.infragistics.faces.input.component.html.HtmlDateChooser;

/**
 * @author informatica
 *
 */
public class Rptmcaja007 extends PageCodeBase {

	protected Rptmcaja007DAO mbRptmcaja007;
	protected HtmlScriptCollector scRptmcaja007;
	protected HtmlForm frmRptmcaja007;
	protected UINamingContainer vfRptmcaja007;
	protected HtmlMenu menu1;
	protected HtmlMenuItem item0;
	protected HtmlMenuItem item1;
	protected HtmlMenuItem item2;
	protected HtmlOutputText lbletRptmcaja007;
	protected HtmlOutputText lbletRtpmcaja007;
	protected HtmlLink lnkCerrarRptmcaja007;
	protected HtmlOutputText lblFiltroComp;
	protected HtmlDropDownList ddlFiltroCompania;
	protected HtmlOutputText lblFiltroFecha;
	protected HtmlDateChooser dcFechaInicio;
	protected HtmlDateChooser dcFechaFinal;
	protected HtmlLink lnkGenerarReporte;
	protected HtmlOutputText lblFiltroCaja;
	protected HtmlDropDownList ddlFiltroCaja;

	/** 
	 * @managed-bean true
	 */
	protected Rptmcaja007DAO getMbRptmcaja007() {
		if (mbRptmcaja007 == null) {
			mbRptmcaja007 = (Rptmcaja007DAO) getFacesContext().getApplication()
					.createValueBinding("#{mbRptmcaja007}").getValue(
							getFacesContext());
		}
		return mbRptmcaja007;
	}

	/** 
	 * @managed-bean true
	 */
	protected void setMbRptmcaja007(Rptmcaja007DAO mbRptmcaja007) {
		this.mbRptmcaja007 = mbRptmcaja007;
	}

	protected HtmlScriptCollector getScRptmcaja007() {
		if (scRptmcaja007 == null) {
			scRptmcaja007 = (HtmlScriptCollector) findComponentInRoot("scRptmcaja007");
		}
		return scRptmcaja007;
	}

	protected HtmlForm getFrmRptmcaja007() {
		if (frmRptmcaja007 == null) {
			frmRptmcaja007 = (HtmlForm) findComponentInRoot("frmRptmcaja007");
		}
		return frmRptmcaja007;
	}

	protected UINamingContainer getVfRptmcaja007() {
		if (vfRptmcaja007 == null) {
			vfRptmcaja007 = (UINamingContainer) findComponentInRoot("vfRptmcaja007");
		}
		return vfRptmcaja007;
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

	protected HtmlOutputText getLbletRptmcaja007() {
		if (lbletRptmcaja007 == null) {
			lbletRptmcaja007 = (HtmlOutputText) findComponentInRoot("lbletRptmcaja007");
		}
		return lbletRptmcaja007;
	}

	protected HtmlOutputText getLbletRtpmcaja007() {
		if (lbletRtpmcaja007 == null) {
			lbletRtpmcaja007 = (HtmlOutputText) findComponentInRoot("lbletRtpmcaja007");
		}
		return lbletRtpmcaja007;
	}

	protected HtmlLink getLnkCerrarRptmcaja007() {
		if (lnkCerrarRptmcaja007 == null) {
			lnkCerrarRptmcaja007 = (HtmlLink) findComponentInRoot("lnkCerrarRptmcaja007");
		}
		return lnkCerrarRptmcaja007;
	}

	protected HtmlOutputText getLblFiltroComp() {
		if (lblFiltroComp == null) {
			lblFiltroComp = (HtmlOutputText) findComponentInRoot("lblFiltroComp");
		}
		return lblFiltroComp;
	}

	protected HtmlDropDownList getDdlFiltroCompania() {
		if (ddlFiltroCompania == null) {
			ddlFiltroCompania = (HtmlDropDownList) findComponentInRoot("ddlFiltroCompania");
		}
		return ddlFiltroCompania;
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

}
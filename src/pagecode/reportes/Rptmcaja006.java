/**
 * 
 */
package pagecode.reportes;

import pagecode.PageCodeBase;
import javax.faces.component.UINamingContainer;
import com.ibm.faces.component.html.HtmlScriptCollector;
import javax.faces.component.html.HtmlForm;
import com.infragistics.faces.menu.component.html.HtmlMenu;
import com.infragistics.faces.menu.component.html.HtmlMenuItem;
import javax.faces.component.html.HtmlOutputText;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
import com.infragistics.faces.input.component.html.HtmlDateChooser;
import com.infragistics.faces.shared.component.html.HtmlLink;
import com.casapellas.reportes.Rptmcaja006DAO;

/**
 * @author informatica
 *
 */
public class Rptmcaja006 extends PageCodeBase {

	protected UINamingContainer vfRptmcaja006;
	protected HtmlScriptCollector scRptmcaja006;
	protected HtmlForm frmRptmcaja006;
	protected HtmlMenu menu1;
	protected HtmlMenuItem item0;
	protected HtmlMenuItem item1;
	protected HtmlMenuItem item2;
	protected HtmlOutputText lbletRptmcaja006;
	protected HtmlOutputText lbletRtpmcaja006;
	protected HtmlOutputText lblFiltroComp;
	protected HtmlDropDownList ddlFiltroCompania;
	protected HtmlOutputText lblfiltroMoneda;
	protected HtmlDropDownList ddlFiltroMoneda;
	protected HtmlOutputText lblFiltroFecha;
	protected HtmlDateChooser dcFechaInicio;
	protected HtmlDateChooser dcFechaFinal;
	protected HtmlLink lnkGenerarReporte;
	protected Rptmcaja006DAO mbRptmcaja006;
	protected HtmlLink lnkCerrarRptmcaja005;

	protected UINamingContainer getVfRptmcaja006() {
		if (vfRptmcaja006 == null) {
			vfRptmcaja006 = (UINamingContainer) findComponentInRoot("vfRptmcaja006");
		}
		return vfRptmcaja006;
	}

	protected HtmlScriptCollector getScRptmcaja006() {
		if (scRptmcaja006 == null) {
			scRptmcaja006 = (HtmlScriptCollector) findComponentInRoot("scRptmcaja006");
		}
		return scRptmcaja006;
	}

	protected HtmlForm getFrmRptmcaja006() {
		if (frmRptmcaja006 == null) {
			frmRptmcaja006 = (HtmlForm) findComponentInRoot("frmRptmcaja006");
		}
		return frmRptmcaja006;
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

	protected HtmlOutputText getLbletRptmcaja006() {
		if (lbletRptmcaja006 == null) {
			lbletRptmcaja006 = (HtmlOutputText) findComponentInRoot("lbletRptmcaja006");
		}
		return lbletRptmcaja006;
	}

	protected HtmlOutputText getLbletRtpmcaja006() {
		if (lbletRtpmcaja006 == null) {
			lbletRtpmcaja006 = (HtmlOutputText) findComponentInRoot("lbletRtpmcaja006");
		}
		return lbletRtpmcaja006;
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

	/** 
	 * @managed-bean true
	 */
	protected Rptmcaja006DAO getMbRptmcaja006() {
		if (mbRptmcaja006 == null) {
			mbRptmcaja006 = (Rptmcaja006DAO) getFacesContext().getApplication()
					.createValueBinding("#{mbRptmcaja006}").getValue(
							getFacesContext());
		}
		return mbRptmcaja006;
	}

	/** 
	 * @managed-bean true
	 */
	protected void setMbRptmcaja006(Rptmcaja006DAO mbRptmcaja006) {
		this.mbRptmcaja006 = mbRptmcaja006;
	}

	protected HtmlLink getLnkCerrarRptmcaja005() {
		if (lnkCerrarRptmcaja005 == null) {
			lnkCerrarRptmcaja005 = (HtmlLink) findComponentInRoot("lnkCerrarRptmcaja005");
		}
		return lnkCerrarRptmcaja005;
	}

}
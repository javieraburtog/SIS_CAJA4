/**
 * 
 */
package pagecode.reportes;

import pagecode.PageCodeBase;
import javax.faces.component.UINamingContainer;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.html.HtmlOutputText;
import com.infragistics.faces.shared.component.html.HtmlLink;
import com.infragistics.faces.menu.component.html.HtmlMenu;
import com.infragistics.faces.menu.component.html.HtmlMenuItem;
import com.infragistics.faces.input.component.html.HtmlDateChooser;
import com.ibm.faces.component.html.HtmlScriptCollector;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
import com.casapellas.dao.RpttransjdeDAO;

/**
 * @author Informatica
 *
 */
public class Rptmcaja003 extends PageCodeBase {

	protected UINamingContainer vfRptTransaccionesJDE;
	protected HtmlForm frmRptTransJDE;
	protected HtmlOutputText lblTitArqueoCaja0;
	protected HtmlOutputText lblTitArqueoCaja;
	protected HtmlMenu menu1;
	protected HtmlMenuItem item0;
	protected HtmlMenuItem item1;
	protected HtmlMenuItem item2;
	protected HtmlOutputText lblFiltroFecha;
	protected HtmlDateChooser dcFechaInicio;
	protected HtmlDateChooser dcFechaFinal;
	protected HtmlScriptCollector scriptCollector1;
	protected HtmlOutputText lblFiltroCaja;
	protected HtmlOutputText lblFiltroComp;
	protected HtmlDropDownList ddlCajas;
	protected HtmlDropDownList ddlFiltroCompania;
	protected RpttransjdeDAO mbRpttrjde;
	protected HtmlLink lnkBuscarTrans;
	protected HtmlOutputText lblMsjRptTransjde;
	protected HtmlLink lnkCerrarReporteTransJDE;
	protected UINamingContainer getVfRptTransaccionesJDE() {
		if (vfRptTransaccionesJDE == null) {
			vfRptTransaccionesJDE = (UINamingContainer) findComponentInRoot("vfRptTransaccionesJDE");
		}
		return vfRptTransaccionesJDE;
	}

	protected HtmlForm getFrmRptTransJDE() {
		if (frmRptTransJDE == null) {
			frmRptTransJDE = (HtmlForm) findComponentInRoot("frmRptTransJDE");
		}
		return frmRptTransJDE;
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

	protected HtmlScriptCollector getScriptCollector1() {
		if (scriptCollector1 == null) {
			scriptCollector1 = (HtmlScriptCollector) findComponentInRoot("scriptCollector1");
		}
		return scriptCollector1;
	}

	protected HtmlOutputText getLblFiltroCaja() {
		if (lblFiltroCaja == null) {
			lblFiltroCaja = (HtmlOutputText) findComponentInRoot("lblFiltroCaja");
		}
		return lblFiltroCaja;
	}

	protected HtmlOutputText getLblFiltroComp() {
		if (lblFiltroComp == null) {
			lblFiltroComp = (HtmlOutputText) findComponentInRoot("lblFiltroComp");
		}
		return lblFiltroComp;
	}

	protected HtmlDropDownList getDdlCajas() {
		if (ddlCajas == null) {
			ddlCajas = (HtmlDropDownList) findComponentInRoot("ddlCajas");
		}
		return ddlCajas;
	}

	protected HtmlDropDownList getDdlFiltroCompania() {
		if (ddlFiltroCompania == null) {
			ddlFiltroCompania = (HtmlDropDownList) findComponentInRoot("ddlFiltroCompania");
		}
		return ddlFiltroCompania;
	}

	/** 
	 * @managed-bean true
	 */
	protected RpttransjdeDAO getMbRpttrjde() {
		if (mbRpttrjde == null) {
			mbRpttrjde = (RpttransjdeDAO) getFacesContext().getApplication()
					.createValueBinding("#{mbRpttrjde}").getValue(
							getFacesContext());
		}
		return mbRpttrjde;
	}

	/** 
	 * @managed-bean true
	 */
	protected void setMbRpttrjde(RpttransjdeDAO mbRpttrjde) {
		this.mbRpttrjde = mbRpttrjde;
	}

	protected HtmlLink getLnkBuscarTrans() {
		if (lnkBuscarTrans == null) {
			lnkBuscarTrans = (HtmlLink) findComponentInRoot("lnkBuscarTrans");
		}
		return lnkBuscarTrans;
	}

	protected HtmlOutputText getLblMsjRptTransjde() {
		if (lblMsjRptTransjde == null) {
			lblMsjRptTransjde = (HtmlOutputText) findComponentInRoot("lblMsjRptTransjde");
		}
		return lblMsjRptTransjde;
	}

	protected HtmlLink getLnkCerrarReporteTransJDE() {
		if (lnkCerrarReporteTransJDE == null) {
			lnkCerrarReporteTransJDE = (HtmlLink) findComponentInRoot("lnkCerrarReporteTransJDE");
		}
		return lnkCerrarReporteTransJDE;
	}

}
/**
 * 
 */
package pagecode.reportes;

import pagecode.PageCodeBase;
import javax.faces.component.html.HtmlOutputText;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
import com.infragistics.faces.input.component.html.HtmlDateChooser;
import com.infragistics.faces.shared.component.html.HtmlLink;

/**
 * @author Juan Ñamendi
 *
 */
public class Rptmcaja010 extends PageCodeBase {

	protected HtmlOutputText lblFiltroFecha;
	protected HtmlDateChooser dcFechaInicio;
	protected HtmlDateChooser dcFechaFinal;
	protected HtmlLink lnkGenerarReporte;
	protected HtmlOutputText lblMsjRptTransjde;
	protected HtmlOutputText lblfiltroMoneda1;
	protected HtmlDropDownList ddlFiltroMoneda2;
	protected HtmlOutputText lblFiltroCaja;
	protected HtmlDropDownList ddlFiltroCajas;

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

	protected HtmlOutputText getLblMsjRptTransjde() {
		if (lblMsjRptTransjde == null) {
			lblMsjRptTransjde = (HtmlOutputText) findComponentInRoot("lblMsjRptTransjde");
		}
		return lblMsjRptTransjde;
	}

	protected HtmlOutputText getLblfiltroMoneda1() {
		if (lblfiltroMoneda1 == null) {
			lblfiltroMoneda1 = (HtmlOutputText) findComponentInRoot("lblfiltroMoneda1");
		}
		return lblfiltroMoneda1;
	}

	protected HtmlDropDownList getDdlFiltroMoneda2() {
		if (ddlFiltroMoneda2 == null) {
			ddlFiltroMoneda2 = (HtmlDropDownList) findComponentInRoot("ddlFiltroMoneda2");
		}
		return ddlFiltroMoneda2;
	}

	protected HtmlOutputText getLblFiltroCaja() {
		if (lblFiltroCaja == null) {
			lblFiltroCaja = (HtmlOutputText) findComponentInRoot("lblFiltroCaja");
		}
		return lblFiltroCaja;
	}

	protected HtmlDropDownList getDdlFiltroCajas() {
		if (ddlFiltroCajas == null) {
			ddlFiltroCajas = (HtmlDropDownList) findComponentInRoot("ddlFiltroCajas");
		}
		return ddlFiltroCajas;
	}

}
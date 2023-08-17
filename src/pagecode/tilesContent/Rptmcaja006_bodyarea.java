/**
 * 
 */
package pagecode.tilesContent;

import pagecode.PageCodeBase;
import javax.faces.component.html.HtmlOutputText;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
import com.infragistics.faces.input.component.html.HtmlDateChooser;
import com.infragistics.faces.shared.component.html.HtmlLink;

/**
 * @author Juan Ñamendi
 *
 */
public class Rptmcaja006_bodyarea extends PageCodeBase {

	protected HtmlOutputText lblFiltroCaja;
	protected HtmlDropDownList ddlFiltroCajas;
	protected HtmlOutputText lblfiltroMoneda;
	protected HtmlDropDownList ddlFiltroMoneda;
	protected HtmlOutputText lblFiltroFecha;
	protected HtmlDateChooser dcFechaInicio;
	protected HtmlDateChooser dcFechaFinal;
	protected HtmlLink lnkGenerarReporte;
	protected HtmlDropDownList ddlFiltroCompania;

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

}
/**
 * 
 */
package pagecode.tilesContent;

import pagecode.PageCodeBase;
import javax.faces.component.html.HtmlOutputText;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
import com.infragistics.faces.shared.component.html.HtmlLink;

/**
 * @author Juan Ñamendi
 *
 */
public class fondoMin_bodyarea extends PageCodeBase {

	protected HtmlOutputText lblFiltroEstado;
	protected HtmlDropDownList cmbFiltroEstados;
	protected HtmlLink lnkSearchContado;

	protected HtmlOutputText getLblFiltroEstado() {
		if (lblFiltroEstado == null) {
			lblFiltroEstado = (HtmlOutputText) findComponentInRoot("lblFiltroEstado");
		}
		return lblFiltroEstado;
	}

	protected HtmlDropDownList getCmbFiltroEstados() {
		if (cmbFiltroEstados == null) {
			cmbFiltroEstados = (HtmlDropDownList) findComponentInRoot("cmbFiltroEstados");
		}
		return cmbFiltroEstados;
	}

	protected HtmlLink getLnkSearchContado() {
		if (lnkSearchContado == null) {
			lnkSearchContado = (HtmlLink) findComponentInRoot("lnkSearchContado");
		}
		return lnkSearchContado;
	}

}
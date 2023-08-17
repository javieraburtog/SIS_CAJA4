/**
 * 
 */
package pagecode.tilesContent;

import pagecode.PageCodeBase;
import com.infragistics.faces.shared.component.html.HtmlLink;

/**
 * @author CarlosHernandez
 *
 */
public class ConsultaCtaTransito_bodyarea extends PageCodeBase {

	protected HtmlLink lnkRealizarBusqueda;

	protected HtmlLink getLnkRealizarBusqueda() {
		if (lnkRealizarBusqueda == null) {
			lnkRealizarBusqueda = (HtmlLink) findComponentInRoot("lnkRealizarBusqueda");
		}
		return lnkRealizarBusqueda;
	}

}
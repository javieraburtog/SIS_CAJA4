/**
 * 
 */
package pagecode.tilesContent;

import pagecode.PageCodeBase;
import javax.faces.component.html.HtmlOutputText;

/**
 * @author Juan �amendi
 *
 */
public class PagoPrimaReserva_bodyarea extends PageCodeBase {

	protected HtmlOutputText lblMensajeValidacionPrima;

	protected HtmlOutputText getLblMensajeValidacionPrima() {
		if (lblMensajeValidacionPrima == null) {
			lblMensajeValidacionPrima = (HtmlOutputText) findComponentInRoot("lblMensajeValidacionPrima");
		}
		return lblMensajeValidacionPrima;
	}

}
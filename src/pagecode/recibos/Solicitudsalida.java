/**
 * 
 */
package pagecode.recibos;

import pagecode.PageCodeBase;
import com.infragistics.faces.shared.component.html.HtmlLink;
import javax.faces.component.html.HtmlOutputText;

/**
 * @author Juan Ñamendi
 *
 */
public class Solicitudsalida extends PageCodeBase {

	protected HtmlLink lnkSetDtsCliente;
	protected HtmlOutputText lblMensajeValidacionPrima;
	protected HtmlOutputText lbldsCompania;

	protected HtmlLink getLnkSetDtsCliente() {
		if (lnkSetDtsCliente == null) {
			lnkSetDtsCliente = (HtmlLink) findComponentInRoot("lnkSetDtsCliente");
		}
		return lnkSetDtsCliente;
	}

	protected HtmlOutputText getLblMensajeValidacionPrima() {
		if (lblMensajeValidacionPrima == null) {
			lblMensajeValidacionPrima = (HtmlOutputText) findComponentInRoot("lblMensajeValidacionPrima");
		}
		return lblMensajeValidacionPrima;
	}

	protected HtmlOutputText getLbldsCompania() {
		if (lbldsCompania == null) {
			lbldsCompania = (HtmlOutputText) findComponentInRoot("lbldsCompania");
		}
		return lbldsCompania;
	}

}
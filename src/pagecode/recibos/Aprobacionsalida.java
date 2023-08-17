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
public class Aprobacionsalida extends PageCodeBase {

	protected HtmlLink lnkSetDtsCliente;
	protected HtmlOutputText lblMsgValidaAprSalidas;

	protected HtmlLink getLnkSetDtsCliente() {
		if (lnkSetDtsCliente == null) {
			lnkSetDtsCliente = (HtmlLink) findComponentInRoot("lnkSetDtsCliente");
		}
		return lnkSetDtsCliente;
	}

	protected HtmlOutputText getLblMsgValidaAprSalidas() {
		if (lblMsgValidaAprSalidas == null) {
			lblMsgValidaAprSalidas = (HtmlOutputText) findComponentInRoot("lblMsgValidaAprSalidas");
		}
		return lblMsgValidaAprSalidas;
	}

}
/**
 * 
 */
package pagecode.cierre;

import pagecode.PageCodeBase;
import javax.faces.component.html.HtmlOutputText;
import com.infragistics.faces.shared.component.html.HtmlLink;

/**
 * @author Juan Ñamendi
 *
 */
public class RevSolicitudCheques extends PageCodeBase {

	protected HtmlOutputText lblMsgValidaSolecheque;
	protected HtmlLink lnkSetDtsCliente;
	protected HtmlLink lnkRechazarSolicitud4dsd;

	protected HtmlOutputText getLblMsgValidaSolecheque() {
		if (lblMsgValidaSolecheque == null) {
			lblMsgValidaSolecheque = (HtmlOutputText) findComponentInRoot("lblMsgValidaSolecheque");
		}
		return lblMsgValidaSolecheque;
	}

	protected HtmlLink getLnkSetDtsCliente() {
		if (lnkSetDtsCliente == null) {
			lnkSetDtsCliente = (HtmlLink) findComponentInRoot("lnkSetDtsCliente");
		}
		return lnkSetDtsCliente;
	}

	protected HtmlLink getLnkRechazarSolicitud4dsd() {
		if (lnkRechazarSolicitud4dsd == null) {
			lnkRechazarSolicitud4dsd = (HtmlLink) findComponentInRoot("lnkRechazarSolicitud4dsd");
		}
		return lnkRechazarSolicitud4dsd;
	}

}
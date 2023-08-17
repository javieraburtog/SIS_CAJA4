/**
 * 
 */
package pagecode.tilesContent;

import pagecode.PageCodeBase;
import javax.faces.component.html.HtmlOutputText;

/**
 * @author CarlosHernandez
 *
 */
public class ConfirmaDepositos_bodyarea extends PageCodeBase {

	protected HtmlOutputText lstMsgSelArchivoConfirm;

	protected HtmlOutputText getLstMsgSelArchivoConfirm() {
		if (lstMsgSelArchivoConfirm == null) {
			lstMsgSelArchivoConfirm = (HtmlOutputText) findComponentInRoot("lstMsgSelArchivoConfirm");
		}
		return lstMsgSelArchivoConfirm;
	}

}
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
public class Rptmcaja003_bodyarea extends PageCodeBase {

	protected HtmlOutputText lblMsjRptTransjde;

	protected HtmlOutputText getLblMsjRptTransjde() {
		if (lblMsjRptTransjde == null) {
			lblMsjRptTransjde = (HtmlOutputText) findComponentInRoot("lblMsjRptTransjde");
		}
		return lblMsjRptTransjde;
	}

}
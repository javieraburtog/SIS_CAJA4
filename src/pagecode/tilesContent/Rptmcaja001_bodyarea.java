/**
 * 
 */
package pagecode.tilesContent;

import pagecode.PageCodeBase;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.html.HtmlOutputText;
import com.infragistics.faces.shared.component.html.HtmlLink;

/**
 * @author Juan Ñamendi
 *
 */
public class Rptmcaja001_bodyarea extends PageCodeBase {

	protected HtmlForm frmResumenArqueo;
	protected HtmlOutputText lblTitArqueoCaja0;
	protected HtmlOutputText lblTitArqueoCaja;
	protected HtmlLink lnkCerrarReporteArqueo;

	protected HtmlForm getFrmResumenArqueo() {
		if (frmResumenArqueo == null) {
			frmResumenArqueo = (HtmlForm) findComponentInRoot("frmResumenArqueo");
		}
		return frmResumenArqueo;
	}

	protected HtmlOutputText getLblTitArqueoCaja0() {
		if (lblTitArqueoCaja0 == null) {
			lblTitArqueoCaja0 = (HtmlOutputText) findComponentInRoot("lblTitArqueoCaja0");
		}
		return lblTitArqueoCaja0;
	}

	protected HtmlOutputText getLblTitArqueoCaja() {
		if (lblTitArqueoCaja == null) {
			lblTitArqueoCaja = (HtmlOutputText) findComponentInRoot("lblTitArqueoCaja");
		}
		return lblTitArqueoCaja;
	}

	protected HtmlLink getLnkCerrarReporteArqueo() {
		if (lnkCerrarReporteArqueo == null) {
			lnkCerrarReporteArqueo = (HtmlLink) findComponentInRoot("lnkCerrarReporteArqueo");
		}
		return lnkCerrarReporteArqueo;
	}

}
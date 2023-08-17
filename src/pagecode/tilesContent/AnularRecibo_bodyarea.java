/**
 * 
 */
package pagecode.tilesContent;

import pagecode.PageCodeBase;
import javax.faces.component.html.HtmlOutputText;
import com.infragistics.faces.grid.component.html.HtmlColumn;
import com.infragistics.faces.window.component.html.HtmlDialogWindowClientEvents;
import com.ibm.faces.component.html.HtmlJspPanel;
import com.ibm.faces.component.html.HtmlGraphicImageEx;
import com.infragistics.faces.window.component.html.HtmlDialogWindow;
import com.infragistics.faces.window.component.html.HtmlDialogWindowRoundedCorners;
import com.infragistics.faces.window.component.html.HtmlDialogWindowContentPane;
import com.infragistics.faces.window.component.html.HtmlDialogWindowAutoPostBackFlags;
import com.infragistics.faces.shared.component.html.HtmlLink;

/**
 * @author Juan Ñamendi
 *
 */
public class AnularRecibo_bodyarea extends PageCodeBase {

	protected HtmlOutputText lblMonto22;
	protected HtmlOutputText lblMonto23;
	protected HtmlColumn coMonto2;
	protected HtmlColumn coFecha2;
	protected HtmlOutputText lblFecha22;
	protected HtmlOutputText lblFecha23;
	protected HtmlOutputText lblPartida22;
	protected HtmlOutputText lblPartida23;
	protected HtmlOutputText lblCodcomp132432;
	protected HtmlOutputText txtCodcomp2;
	protected HtmlDialogWindowClientEvents cledwCargando;
	protected HtmlJspPanel jspdwCargando;
	protected HtmlGraphicImageEx imagenCargando;
	protected HtmlDialogWindow dwCargando;
	protected HtmlDialogWindowRoundedCorners cledwCargando22;
	protected HtmlDialogWindowContentPane cpdwCargando;
	protected HtmlDialogWindowAutoPostBackFlags apbdwCargando;
	protected HtmlLink lnkCerrarMensaje13;
	protected HtmlLink lnkCerrarMensaje14;
	protected HtmlOutputText getLblMonto22() {
		if (lblMonto22 == null) {
			lblMonto22 = (HtmlOutputText) findComponentInRoot("lblMonto22");
		}
		return lblMonto22;
	}

	protected HtmlOutputText getLblMonto23() {
		if (lblMonto23 == null) {
			lblMonto23 = (HtmlOutputText) findComponentInRoot("lblMonto23");
		}
		return lblMonto23;
	}

	protected HtmlColumn getCoMonto2() {
		if (coMonto2 == null) {
			coMonto2 = (HtmlColumn) findComponentInRoot("coMonto2");
		}
		return coMonto2;
	}

	protected HtmlColumn getCoFecha2() {
		if (coFecha2 == null) {
			coFecha2 = (HtmlColumn) findComponentInRoot("coFecha2");
		}
		return coFecha2;
	}

	protected HtmlOutputText getLblFecha22() {
		if (lblFecha22 == null) {
			lblFecha22 = (HtmlOutputText) findComponentInRoot("lblFecha22");
		}
		return lblFecha22;
	}

	protected HtmlOutputText getLblFecha23() {
		if (lblFecha23 == null) {
			lblFecha23 = (HtmlOutputText) findComponentInRoot("lblFecha23");
		}
		return lblFecha23;
	}

	protected HtmlOutputText getLblPartida22() {
		if (lblPartida22 == null) {
			lblPartida22 = (HtmlOutputText) findComponentInRoot("lblPartida22");
		}
		return lblPartida22;
	}

	protected HtmlOutputText getLblPartida23() {
		if (lblPartida23 == null) {
			lblPartida23 = (HtmlOutputText) findComponentInRoot("lblPartida23");
		}
		return lblPartida23;
	}

	protected HtmlOutputText getLblCodcomp132432() {
		if (lblCodcomp132432 == null) {
			lblCodcomp132432 = (HtmlOutputText) findComponentInRoot("lblCodcomp132432");
		}
		return lblCodcomp132432;
	}

	protected HtmlOutputText getTxtCodcomp2() {
		if (txtCodcomp2 == null) {
			txtCodcomp2 = (HtmlOutputText) findComponentInRoot("txtCodcomp2");
		}
		return txtCodcomp2;
	}

	protected HtmlDialogWindowClientEvents getCledwCargando() {
		if (cledwCargando == null) {
			cledwCargando = (HtmlDialogWindowClientEvents) findComponentInRoot("cledwCargando");
		}
		return cledwCargando;
	}

	protected HtmlJspPanel getJspdwCargando() {
		if (jspdwCargando == null) {
			jspdwCargando = (HtmlJspPanel) findComponentInRoot("jspdwCargando");
		}
		return jspdwCargando;
	}

	protected HtmlGraphicImageEx getImagenCargando() {
		if (imagenCargando == null) {
			imagenCargando = (HtmlGraphicImageEx) findComponentInRoot("imagenCargando");
		}
		return imagenCargando;
	}

	protected HtmlDialogWindow getDwCargando() {
		if (dwCargando == null) {
			dwCargando = (HtmlDialogWindow) findComponentInRoot("dwCargando");
		}
		return dwCargando;
	}

	protected HtmlDialogWindowRoundedCorners getCledwCargando22() {
		if (cledwCargando22 == null) {
			cledwCargando22 = (HtmlDialogWindowRoundedCorners) findComponentInRoot("cledwCargando22");
		}
		return cledwCargando22;
	}

	protected HtmlDialogWindowContentPane getCpdwCargando() {
		if (cpdwCargando == null) {
			cpdwCargando = (HtmlDialogWindowContentPane) findComponentInRoot("cpdwCargando");
		}
		return cpdwCargando;
	}

	protected HtmlDialogWindowAutoPostBackFlags getApbdwCargando() {
		if (apbdwCargando == null) {
			apbdwCargando = (HtmlDialogWindowAutoPostBackFlags) findComponentInRoot("apbdwCargando");
		}
		return apbdwCargando;
	}

	protected HtmlLink getLnkCerrarMensaje13() {
		if (lnkCerrarMensaje13 == null) {
			lnkCerrarMensaje13 = (HtmlLink) findComponentInRoot("lnkCerrarMensaje13");
		}
		return lnkCerrarMensaje13;
	}

	protected HtmlLink getLnkCerrarMensaje14() {
		if (lnkCerrarMensaje14 == null) {
			lnkCerrarMensaje14 = (HtmlLink) findComponentInRoot("lnkCerrarMensaje14");
		}
		return lnkCerrarMensaje14;
	}

}
/**
 * 
 */
package pagecode.recibos;

import pagecode.PageCodeBase;
import com.ibm.faces.component.html.HtmlScriptCollector;
import javax.faces.component.html.HtmlForm;
import com.infragistics.faces.window.component.html.HtmlDialogWindow;
import com.infragistics.faces.input.component.html.HtmlDateChooser;

/**
 * @author Ligia Ulloa
 *
 */
public class PagoPrimaReserva2 extends PageCodeBase {

	protected HtmlScriptCollector scriptCollector1;
	protected HtmlForm form1;
	protected HtmlDialogWindow htmlDialogWindowRenderer1;
	protected HtmlDateChooser dateChooserRenderer1;

	protected HtmlScriptCollector getScriptCollector1() {
		if (scriptCollector1 == null) {
			scriptCollector1 = (HtmlScriptCollector) findComponentInRoot("scriptCollector1");
		}
		return scriptCollector1;
	}

	protected HtmlForm getForm1() {
		if (form1 == null) {
			form1 = (HtmlForm) findComponentInRoot("form1");
		}
		return form1;
	}

	protected HtmlDialogWindow getHtmlDialogWindowRenderer1() {
		if (htmlDialogWindowRenderer1 == null) {
			htmlDialogWindowRenderer1 = (HtmlDialogWindow) findComponentInRoot("htmlDialogWindowRenderer1");
		}
		return htmlDialogWindowRenderer1;
	}

	protected HtmlDateChooser getDateChooserRenderer1() {
		if (dateChooserRenderer1 == null) {
			dateChooserRenderer1 = (HtmlDateChooser) findComponentInRoot("dateChooserRenderer1");
		}
		return dateChooserRenderer1;
	}

}
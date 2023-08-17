/**
 * 
 */
package pagecode;

import com.infragistics.faces.shared.component.html.HtmlLink;
import com.ibm.faces.component.html.HtmlScriptCollector;
import javax.faces.component.html.HtmlForm;

/**
 * @author Juan Carlos
 *
 */
public class Main extends PageCodeBase {

	protected HtmlLink lnkFacturacionDiaria;
	protected HtmlLink lnkSalidas;
	protected HtmlLink lnkReciboCont;
	protected HtmlLink lnkReciboCred;
	protected HtmlScriptCollector scriptCollector1;
	protected HtmlForm form1;
	protected HtmlLink lnkSalir;
	protected HtmlLink getLnkFacturacionDiaria() {
		if (lnkFacturacionDiaria == null) {
			lnkFacturacionDiaria = (HtmlLink) findComponentInRoot("lnkFacturacionDiaria");
		}
		return lnkFacturacionDiaria;
	}

	protected HtmlLink getLnkSalidas() {
		if (lnkSalidas == null) {
			lnkSalidas = (HtmlLink) findComponentInRoot("lnkSalidas");
		}
		return lnkSalidas;
	}

	protected HtmlLink getLnkReciboCont() {
		if (lnkReciboCont == null) {
			lnkReciboCont = (HtmlLink) findComponentInRoot("lnkReciboCont");
		}
		return lnkReciboCont;
	}

	protected HtmlLink getLnkReciboCred() {
		if (lnkReciboCred == null) {
			lnkReciboCred = (HtmlLink) findComponentInRoot("lnkReciboCred");
		}
		return lnkReciboCred;
	}

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

	protected HtmlLink getLnkSalir() {
		if (lnkSalir == null) {
			lnkSalir = (HtmlLink) findComponentInRoot("lnkSalir");
		}
		return lnkSalir;
	}

}
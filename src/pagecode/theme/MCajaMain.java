/**
 * 
 */
package pagecode.theme;

import pagecode.PageCodeBase;
import javax.faces.component.UINamingContainer;
import com.infragistics.faces.shared.component.html.HtmlLink;

/**
 * @author Juan Ñamendi
 *
 */
public class MCajaMain extends PageCodeBase {

	protected UINamingContainer svPlantilla;
	protected HtmlLink lnkHome;

	protected UINamingContainer getSvPlantilla() {
		if (svPlantilla == null) {
			svPlantilla = (UINamingContainer) findComponentInRoot("svPlantilla");
		}
		return svPlantilla;
	}

	protected HtmlLink getLnkHome() {
		if (lnkHome == null) {
			lnkHome = (HtmlLink) findComponentInRoot("lnkHome");
		}
		return lnkHome;
	}

}
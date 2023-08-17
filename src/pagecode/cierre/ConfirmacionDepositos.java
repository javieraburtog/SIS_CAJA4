/**
 * 
 */
package pagecode.cierre;

import pagecode.PageCodeBase;
import com.ibm.faces.component.html.HtmlJspPanel;

/**
 * @author CarlosHernandez
 *
 */
public class ConfirmacionDepositos extends PageCodeBase {

	protected HtmlJspPanel jspFiltrosBusqueda;

	protected HtmlJspPanel getJspFiltrosBusqueda() {
		if (jspFiltrosBusqueda == null) {
			jspFiltrosBusqueda = (HtmlJspPanel) findComponentInRoot("jspFiltrosBusqueda");
		}
		return jspFiltrosBusqueda;
	}

}
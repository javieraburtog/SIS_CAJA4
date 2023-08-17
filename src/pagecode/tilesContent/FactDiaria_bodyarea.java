/**
 * 
 */
package pagecode.tilesContent;

import pagecode.PageCodeBase;
import com.infragistics.faces.input.component.html.HtmlDateChooser;

/**
 * @author Juan Ñamendi
 *
 */
public class FactDiaria_bodyarea extends PageCodeBase {

	protected HtmlDateChooser dcFechaInicial;
	protected HtmlDateChooser dcFechaFinal;

	protected HtmlDateChooser getDcFechaInicial() {
		if (dcFechaInicial == null) {
			dcFechaInicial = (HtmlDateChooser) findComponentInRoot("dcFechaInicial");
		}
		return dcFechaInicial;
	}

	protected HtmlDateChooser getDcFechaFinal() {
		if (dcFechaFinal == null) {
			dcFechaFinal = (HtmlDateChooser) findComponentInRoot("dcFechaFinal");
		}
		return dcFechaFinal;
	}

}
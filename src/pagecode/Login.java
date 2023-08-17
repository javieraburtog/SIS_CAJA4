/**
 * 
 */
package pagecode;

import com.ibm.faces.component.html.HtmlScriptCollector;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGrid;
import com.ibm.faces.component.html.HtmlGraphicImageEx;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlInputSecret;
import com.ibm.faces.component.html.HtmlCommandExButton;
import com.ibm.faces.component.html.HtmlBehaviorKeyPress;

/**
 * @author Juan Ñamendi
 *
 */
public class Login extends PageCodeBase {

	protected HtmlScriptCollector scriptCollector1;
	protected HtmlForm form1;
	protected HtmlOutputText txtMsgLogin;
	protected HtmlPanelGrid grdLogin;
	protected HtmlOutputText lblUsuario;
	protected HtmlPanelGrid grid1;
	protected HtmlGraphicImageEx imgAceptar;
	protected HtmlInputText txtUsuario;
	protected HtmlOutputText lblContrasenia;
	protected HtmlInputSecret txtContrasenia;
	protected HtmlCommandExButton btnIngrear;
	protected HtmlBehaviorKeyPress behaviorKeyPress1;

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

	protected HtmlOutputText getTxtMsgLogin() {
		if (txtMsgLogin == null) {
			txtMsgLogin = (HtmlOutputText) findComponentInRoot("txtMsgLogin");
		}
		return txtMsgLogin;
	}

	protected HtmlPanelGrid getGrdLogin() {
		if (grdLogin == null) {
			grdLogin = (HtmlPanelGrid) findComponentInRoot("grdLogin");
		}
		return grdLogin;
	}

	protected HtmlOutputText getLblUsuario() {
		if (lblUsuario == null) {
			lblUsuario = (HtmlOutputText) findComponentInRoot("lblUsuario");
		}
		return lblUsuario;
	}

	protected HtmlPanelGrid getGrid1() {
		if (grid1 == null) {
			grid1 = (HtmlPanelGrid) findComponentInRoot("grid1");
		}
		return grid1;
	}

	protected HtmlGraphicImageEx getImgAceptar() {
		if (imgAceptar == null) {
			imgAceptar = (HtmlGraphicImageEx) findComponentInRoot("imgAceptar");
		}
		return imgAceptar;
	}

	protected HtmlInputText getTxtUsuario() {
		if (txtUsuario == null) {
			txtUsuario = (HtmlInputText) findComponentInRoot("txtUsuario");
		}
		return txtUsuario;
	}

	protected HtmlOutputText getLblContrasenia() {
		if (lblContrasenia == null) {
			lblContrasenia = (HtmlOutputText) findComponentInRoot("lblContrasenia");
		}
		return lblContrasenia;
	}

	protected HtmlInputSecret getTxtContrasenia() {
		if (txtContrasenia == null) {
			txtContrasenia = (HtmlInputSecret) findComponentInRoot("txtContrasenia");
		}
		return txtContrasenia;
	}

	protected HtmlCommandExButton getBtnIngrear() {
		if (btnIngrear == null) {
			btnIngrear = (HtmlCommandExButton) findComponentInRoot("btnIngrear");
		}
		return btnIngrear;
	}

	protected HtmlBehaviorKeyPress getBehaviorKeyPress1() {
		if (behaviorKeyPress1 == null) {
			behaviorKeyPress1 = (HtmlBehaviorKeyPress) findComponentInRoot("behaviorKeyPress1");
		}
		return behaviorKeyPress1;
	}

}
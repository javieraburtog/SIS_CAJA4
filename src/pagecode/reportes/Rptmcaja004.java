/**
 * 
 */
package pagecode.reportes;

import pagecode.PageCodeBase;
import com.casapellas.reportes.Rptmcaja004DAO;
import javax.faces.component.UINamingContainer;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.html.HtmlOutputText;
import com.infragistics.faces.shared.component.html.HtmlLink;
import com.ibm.faces.component.html.HtmlScriptCollector;

/**
 * @author informatica
 *
 */
public class Rptmcaja004 extends PageCodeBase {

	protected Rptmcaja004DAO mbRptmcaja004;
	protected UINamingContainer vfRptmcaja004;
	protected HtmlForm frmRptTransJDE;
	protected HtmlOutputText lbletRptDetArqueoCaja;
	protected HtmlLink lnkCerrarRptmcajca004;
	protected HtmlScriptCollector scriptCollector1;
	protected HtmlOutputText lbletRptDetArqueoCaja1;
	/** 
	 * @managed-bean true
	 */
	protected Rptmcaja004DAO getMbRptmcaja004() {
		if (mbRptmcaja004 == null) {
			mbRptmcaja004 = (Rptmcaja004DAO) getFacesContext().getApplication()
					.createValueBinding("#{mbRptmcaja004}").getValue(
							getFacesContext());
		}
		return mbRptmcaja004;
	}

	/** 
	 * @managed-bean true
	 */
	protected void setMbRptmcaja004(Rptmcaja004DAO mbRptmcaja004) {
		this.mbRptmcaja004 = mbRptmcaja004;
	}

	protected UINamingContainer getVfRptmcaja004() {
		if (vfRptmcaja004 == null) {
			vfRptmcaja004 = (UINamingContainer) findComponentInRoot("vfRptmcaja004");
		}
		return vfRptmcaja004;
	}

	protected HtmlForm getFrmRptTransJDE() {
		if (frmRptTransJDE == null) {
			frmRptTransJDE = (HtmlForm) findComponentInRoot("frmRptTransJDE");
		}
		return frmRptTransJDE;
	}

	protected HtmlOutputText getLbletRptDetArqueoCaja() {
		if (lbletRptDetArqueoCaja == null) {
			lbletRptDetArqueoCaja = (HtmlOutputText) findComponentInRoot("lbletRptDetArqueoCaja");
		}
		return lbletRptDetArqueoCaja;
	}

	protected HtmlLink getLnkCerrarRptmcajca004() {
		if (lnkCerrarRptmcajca004 == null) {
			lnkCerrarRptmcajca004 = (HtmlLink) findComponentInRoot("lnkCerrarRptmcajca004");
		}
		return lnkCerrarRptmcajca004;
	}

	protected HtmlScriptCollector getScriptCollector1() {
		if (scriptCollector1 == null) {
			scriptCollector1 = (HtmlScriptCollector) findComponentInRoot("scriptCollector1");
		}
		return scriptCollector1;
	}

	protected HtmlOutputText getLbletRptDetArqueoCaja1() {
		if (lbletRptDetArqueoCaja1 == null) {
			lbletRptDetArqueoCaja1 = (HtmlOutputText) findComponentInRoot("lbletRptDetArqueoCaja1");
		}
		return lbletRptDetArqueoCaja1;
	}

}
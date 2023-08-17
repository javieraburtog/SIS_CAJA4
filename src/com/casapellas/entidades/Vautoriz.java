/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.casapellas.entidades;
import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "VAUTORIZ", catalog = "SYSTEMIP", schema = "ENS")
@XmlRootElement
public class Vautoriz implements Serializable {

	private static final long serialVersionUID = 1L;
	@EmbeddedId
    private VautorizPK vautorizPK;
	
    public Vautoriz() {
	}

	public VautorizPK getVautorizPK() {
        return vautorizPK;
    }
	
	public VautorizPK getId() {
        return vautorizPK;
    }

    public void setVautorizPK(VautorizPK vautorizPK) {
        this.vautorizPK = vautorizPK;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((vautorizPK == null) ? 0 : vautorizPK.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vautoriz other = (Vautoriz) obj;
		if (vautorizPK == null) {
			if (other.vautorizPK != null)
				return false;
		} else if (!vautorizPK.equals(other.vautorizPK))
			return false;
		return true;
	}

    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.casapellas.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 
 * @author Francisco Landeros
 */
@Embeddable
public class VautorizPK implements Serializable {

	private static final long serialVersionUID = 1L;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 10)
	@Column(name = "CODUSER")
	private String coduser;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 32)
	@Column(name = "LOGIN")
	private String login;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 10)
	@Column(name = "TIPUSER")
	private String tipuser;
	@Column(name = "CODREG")
	private Integer codreg;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 10)
	@Column(name = "CODPER")
	private String codper;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 32)
	@Column(name = "NOMPER")
	private String nomper;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 10)
	@Column(name = "CODAPP")
	private String codapp;
	@Size(max = 10)
	@Column(name = "NOMCORTO")
	private String nomcorto;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 40)
	@Column(name = "NOMAPP")
	private String nomapp;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 10)
	@Column(name = "CODSEC")
	private String codsec;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 10)
	@Column(name = "CODSUPER")
	private String codsuper;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 32)
	@Column(name = "NOMSEC")
	private String nomsec;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 300)
	@Column(name = "ICONURL")
	private String iconurl;
	@Size(max = 16)
	@Column(name = "OUTCOME")
	private String outcome;
	@Size(max = 1)
	@Column(name = "ENMENU")
	private String enmenu;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 10)
	@Column(name = "CODAUT")
	private String codaut;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 32)
	@Column(name = "NOMAUT")
	private String nomaut;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 3)
	@Column(name = "ALCANCE")
	private String alcance;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 150)
	@Column(name = "URL")
	private String url;
	@Column(name = "ORDEN")
	private Integer orden;

	@Size(min = 1, max = 6000)
	@Column(name = "SECCIONLOGO")
	private String seccionlogo;
	@Size(min = 1, max = 6000)
	@Column(name = "SECCIONICON")
	private String seccionicon;
	@Size(min = 1, max = 6000)
	@Column(name = "APLICACIONLOGO")
	private String aplicacionlogo;
	@Size(min = 1, max = 6000)
	@Column(name = "APLICACIONICON")
	private String aplicacionicon;
	@Size(min = 1, max = 100)
	@Column(name = "APLICACIONVERSION")
	private String aplicacionversion;
	@Size(min = 1, max = 100)
	@Column(name = "APLICACIONCOPYRIGHT")
	private String aplicacioncopyright;
	
	
	public String getAplicacionversion() {
		return aplicacionversion;
	}

	public void setAplicacionversion(String aplicacionversion) {
		this.aplicacionversion = aplicacionversion;
	}

	public String getAplicacioncopyright() {
		return aplicacioncopyright;
	}

	public void setAplicacioncopyright(String aplicacioncopyright) {
		this.aplicacioncopyright = aplicacioncopyright;
	}

	public String getSeccionlogo() {
		return seccionlogo;
	}

	public void setSeccionlogo(String seccionlogo) {
		this.seccionlogo = seccionlogo;
	}

	public String getSeccionicon() {
		return seccionicon;
	}

	public void setSeccionicon(String seccionicon) {
		this.seccionicon = seccionicon;
	}

	public String getAplicacionlogo() {
		return aplicacionlogo;
	}

	public void setAplicacionlogo(String aplicacionlogo) {
		this.aplicacionlogo = aplicacionlogo;
	}

	public String getAplicacionicon() {
		return aplicacionicon;
	}

	public void setAplicacionicon(String aplicacionicon) {
		this.aplicacionicon = aplicacionicon;
	}

	public VautorizPK() {
	}

	public String getCoduser() {
		return coduser;
	}

	public void setCoduser(String coduser) {
		this.coduser = coduser;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getTipuser() {
		return tipuser;
	}

	public void setTipuser(String tipuser) {
		this.tipuser = tipuser;
	}

	public Integer getCodreg() {
		return codreg;
	}

	public void setCodreg(Integer codreg) {
		this.codreg = codreg;
	}

	public String getCodper() {
		return codper;
	}

	public void setCodper(String codper) {
		this.codper = codper;
	}
	
	public String getEmpnombre() {
		return nomper;
	}

	public String getNomper() {
		return nomper;
	}

	public void setNomper(String nomper) {
		this.nomper = nomper;
	}

	public String getCodapp() {
		return codapp;
	}

	public void setCodapp(String codapp) {
		this.codapp = codapp;
	}

	public String getNomcorto() {
		return nomcorto;
	}

	public void setNomcorto(String nomcorto) {
		this.nomcorto = nomcorto;
	}

	public String getNomapp() {
		return nomapp;
	}

	public void setNomapp(String nomapp) {
		this.nomapp = nomapp;
	}

	public String getCodsec() {
		return codsec;
	}

	public void setCodsec(String codsec) {
		this.codsec = codsec;
	}

	public String getCodsuper() {
		return codsuper;
	}

	public void setCodsuper(String codsuper) {
		this.codsuper = codsuper;
	}

	public String getNomsec() {
		return nomsec;
	}

	public void setNomsec(String nomsec) {
		this.nomsec = nomsec;
	}

	public String getIconurl() {
		return iconurl;
	}

	public void setIconurl(String iconurl) {
		this.iconurl = iconurl;
	}

	public String getOutcome() {
		return outcome;
	}

	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}

	public String getEnmenu() {
		return enmenu;
	}

	public void setEnmenu(String enmenu) {
		this.enmenu = enmenu;
	}

	public String getCodaut() {
		return codaut;
	}

	public void setCodaut(String codaut) {
		this.codaut = codaut;
	}

	public String getNomaut() {
		return nomaut;
	}

	public void setNomaut(String nomaut) {
		this.nomaut = nomaut;
	}

	public String getAlcance() {
		return alcance;
	}

	public void setAlcance(String alcance) {
		this.alcance = alcance;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getOrden() {
		return orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alcance == null) ? 0 : alcance.hashCode());
		result = prime * result + ((codapp == null) ? 0 : codapp.hashCode());
		result = prime * result + ((codaut == null) ? 0 : codaut.hashCode());
		result = prime * result + ((codper == null) ? 0 : codper.hashCode());
		result = prime * result + ((codreg == null) ? 0 : codreg.hashCode());
		result = prime * result + ((codsec == null) ? 0 : codsec.hashCode());
		result = prime * result
				+ ((codsuper == null) ? 0 : codsuper.hashCode());
		result = prime * result + ((coduser == null) ? 0 : coduser.hashCode());
		result = prime * result + ((enmenu == null) ? 0 : enmenu.hashCode());
		result = prime * result + ((iconurl == null) ? 0 : iconurl.hashCode());
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		result = prime * result + ((nomapp == null) ? 0 : nomapp.hashCode());
		result = prime * result + ((nomaut == null) ? 0 : nomaut.hashCode());
		result = prime * result
				+ ((nomcorto == null) ? 0 : nomcorto.hashCode());
		result = prime * result + ((nomper == null) ? 0 : nomper.hashCode());
		result = prime * result + ((nomsec == null) ? 0 : nomsec.hashCode());
		result = prime * result + ((orden == null) ? 0 : orden.hashCode());
		result = prime * result + ((outcome == null) ? 0 : outcome.hashCode());
		result = prime * result + ((tipuser == null) ? 0 : tipuser.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		result = prime * result + ((seccionlogo == null) ? 0 : seccionlogo.hashCode());
		result = prime * result + ((seccionicon == null) ? 0 : seccionicon.hashCode());
		result = prime * result + ((aplicacionicon == null) ? 0 : aplicacionicon.hashCode());
		result = prime * result + ((aplicacionlogo == null) ? 0 : aplicacionlogo.hashCode());
		result = prime * result + ((aplicacionversion == null) ? 0 : aplicacionversion.hashCode());
		result = prime * result + ((aplicacioncopyright == null) ? 0 : aplicacioncopyright.hashCode());
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
		VautorizPK other = (VautorizPK) obj;
		if (alcance == null) {
			if (other.alcance != null)
				return false;
		} else if (!alcance.equals(other.alcance))
			return false;
		if (codapp == null) {
			if (other.codapp != null)
				return false;
		} else if (!codapp.equals(other.codapp))
			return false;
		if (codaut == null) {
			if (other.codaut != null)
				return false;
		} else if (!codaut.equals(other.codaut))
			return false;
		if (codper == null) {
			if (other.codper != null)
				return false;
		} else if (!codper.equals(other.codper))
			return false;
		if (codreg == null) {
			if (other.codreg != null)
				return false;
		} else if (!codreg.equals(other.codreg))
			return false;
		if (codsec == null) {
			if (other.codsec != null)
				return false;
		} else if (!codsec.equals(other.codsec))
			return false;
		if (codsuper == null) {
			if (other.codsuper != null)
				return false;
		} else if (!codsuper.equals(other.codsuper))
			return false;
		if (coduser == null) {
			if (other.coduser != null)
				return false;
		} else if (!coduser.equals(other.coduser))
			return false;
		if (enmenu == null) {
			if (other.enmenu != null)
				return false;
		} else if (!enmenu.equals(other.enmenu))
			return false;
		if (iconurl == null) {
			if (other.iconurl != null)
				return false;
		} else if (!iconurl.equals(other.iconurl))
			return false;
		if (login == null) {
			if (other.login != null)
				return false;
		} else if (!login.equals(other.login))
			return false;
		if (nomapp == null) {
			if (other.nomapp != null)
				return false;
		} else if (!nomapp.equals(other.nomapp))
			return false;
		if (nomaut == null) {
			if (other.nomaut != null)
				return false;
		} else if (!nomaut.equals(other.nomaut))
			return false;
		if (nomcorto == null) {
			if (other.nomcorto != null)
				return false;
		} else if (!nomcorto.equals(other.nomcorto))
			return false;
		if (nomper == null) {
			if (other.nomper != null)
				return false;
		} else if (!nomper.equals(other.nomper))
			return false;
		if (nomsec == null) {
			if (other.nomsec != null)
				return false;
		} else if (!nomsec.equals(other.nomsec))
			return false;
		if (orden == null) {
			if (other.orden != null)
				return false;
		} else if (!orden.equals(other.orden))
			return false;
		if (outcome == null) {
			if (other.outcome != null)
				return false;
		} else if (!outcome.equals(other.outcome))
			return false;
		if (tipuser == null) {
			if (other.tipuser != null)
				return false;
		} else if (!tipuser.equals(other.tipuser))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		if (seccionicon == null) {
			if (other.seccionicon != null)
				return false;
		} else if (!seccionicon.equals(other.seccionicon))
			return false;
		if (seccionlogo == null) {
			if (other.seccionlogo != null)
				return false;
		} else if (!seccionlogo.equals(other.seccionlogo))
			return false;
		if (aplicacionicon == null) {
			if (other.aplicacionicon != null)
				return false;
		} else if (!aplicacionicon.equals(other.aplicacionicon))
			return false;
		if (aplicacionlogo == null) {
			if (other.aplicacionlogo != null)
				return false;
		} else if (!aplicacionlogo.equals(other.aplicacionlogo))
			return false;
		if (aplicacionversion == null) {
			if (other.aplicacionversion != null)
				return false;
		} else if (!aplicacionversion.equals(other.aplicacionversion))
			return false;
		if (aplicacioncopyright == null) {
			if (other.aplicacioncopyright != null)
				return false;
		} else if (!aplicacioncopyright.equals(other.aplicacioncopyright))
			return false;
		
		return true;
	}

	

}

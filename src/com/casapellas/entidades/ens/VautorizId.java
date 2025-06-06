package com.casapellas.entidades.ens;

// Generated 17/07/2009 13:52:08 by Hibernate Tools 3.2.0.b9
/**
 * VautorizId generated by hbm2java
 */
public class VautorizId implements java.io.Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -254302177337191846L;
	private String coduser;
    private String login;
    private String tipuser;
    private Integer codreg;
    private String codper;
    private String nomper;
    private String codapp;
    private String nomcorto;
    private String nomapp;
    private String codsec;
    private String codsuper;
    private String nomsec;
    private String iconurl;
    private String outcome;
    private String enmenu;
    private String codaut;
    private String nomaut;
    private String alcance;
    private String url;
    private Integer orden;
    private String empnombre;

    public VautorizId() {
    }

    public VautorizId(String coduser, String login, String tipuser,
            String codper, String nomper, String codapp, String nomapp,
            String codsec, String codsuper, String nomsec, String codaut,
            String nomaut, String alcance) {
        this.coduser = coduser;
        this.login = login;
        this.tipuser = tipuser;
        this.codper = codper;
        this.nomper = nomper;
        this.codapp = codapp;
        this.nomapp = nomapp;
        this.codsec = codsec;
        this.codsuper = codsuper;
        this.nomsec = nomsec;
        this.codaut = codaut;
        this.nomaut = nomaut;
        this.alcance = alcance;
    }

    public VautorizId(String coduser, String login, String tipuser,
            Integer codreg, String codper, String nomper, String codapp,
            String nomcorto, String nomapp, String codsec, String codsuper,
            String nomsec, String iconurl, String outcome, String enmenu,
            String codaut, String nomaut, String alcance, String url,
            Integer orden, String empnombre ) {
        this.coduser = coduser;
        this.login = login;
        this.tipuser = tipuser;
        this.codreg = codreg;
        this.codper = codper;
        this.nomper = nomper;
        this.codapp = codapp;
        this.nomcorto = nomcorto;
        this.nomapp = nomapp;
        this.codsec = codsec;
        this.codsuper = codsuper;
        this.nomsec = nomsec;
        this.iconurl = iconurl;
        this.outcome = outcome;
        this.enmenu = enmenu;
        this.codaut = codaut;
        this.nomaut = nomaut;
        this.alcance = alcance;
        this.url = url;
        this.orden = orden;
        this.empnombre = empnombre;
    }

    public String getCoduser() {
        return this.coduser;
    }

    public void setCoduser(String coduser) {
        this.coduser = coduser;
    }

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getTipuser() {
        return this.tipuser;
    }

    public void setTipuser(String tipuser) {
        this.tipuser = tipuser;
    }

    public Integer getCodreg() {
        return this.codreg;
    }

    public void setCodreg(Integer codreg) {
        this.codreg = codreg;
    }

    public String getCodper() {
        return this.codper;
    }

    public void setCodper(String codper) {
        this.codper = codper;
    }

    public String getNomper() {
        return this.nomper;
    }

    public void setNomper(String nomper) {
        this.nomper = nomper;
    }

    public String getCodapp() {
        return this.codapp;
    }

    public void setCodapp(String codapp) {
        this.codapp = codapp;
    }

    public String getNomcorto() {
        return this.nomcorto;
    }

    public void setNomcorto(String nomcorto) {
        this.nomcorto = nomcorto;
    }

    public String getNomapp() {
        return this.nomapp;
    }

    public void setNomapp(String nomapp) {
        this.nomapp = nomapp;
    }

    public String getCodsec() {
        return this.codsec;
    }

    public void setCodsec(String codsec) {
        this.codsec = codsec;
    }

    public String getCodsuper() {
        return this.codsuper;
    }

    public void setCodsuper(String codsuper) {
        this.codsuper = codsuper;
    }

    public String getNomsec() {
        return this.nomsec;
    }

    public void setNomsec(String nomsec) {
        this.nomsec = nomsec;
    }

    public String getIconurl() {
        return this.iconurl;
    }

    public void setIconurl(String iconurl) {
        this.iconurl = iconurl;
    }

    public String getOutcome() {
        return this.outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public String getEnmenu() {
        return this.enmenu;
    }

    public void setEnmenu(String enmenu) {
        this.enmenu = enmenu;
    }

    public String getCodaut() {
        return this.codaut;
    }

    public void setCodaut(String codaut) {
        this.codaut = codaut;
    }

    public String getNomaut() {
        return this.nomaut;
    }

    public void setNomaut(String nomaut) {
        this.nomaut = nomaut;
    }

    public String getAlcance() {
        return this.alcance;
    }

    public void setAlcance(String alcance) {
        this.alcance = alcance;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getOrden() {
        return this.orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

	public String getEmpnombre() {
		return empnombre;
	}

	public void setEmpnombre(String empnombre) {
		this.empnombre = empnombre;
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
		result = prime * result
				+ ((empnombre == null) ? 0 : empnombre.hashCode());
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
		VautorizId other = (VautorizId) obj;
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
		if (empnombre == null) {
			if (other.empnombre != null)
				return false;
		} else if (!empnombre.equals(other.empnombre))
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
		return true;
	}
 
    
}

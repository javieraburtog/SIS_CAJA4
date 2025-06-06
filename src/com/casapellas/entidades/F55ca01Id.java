package com.casapellas.entidades;

// Generated 17/08/2009 08:58:41 by Hibernate Tools 3.2.0.b9

/**
 * F55ca01Id generated by hbm2java
 */
public class F55ca01Id implements java.io.Serializable {

	private int caid;
	private String caname;
	private String caco;
	private int caan8;
	private int cacati;
	private int caauti;
	private int caausu;
	private int cacont;
	private String cajobn;
	private String carmk;
	private String carmk1;
	private char castat;
	private String caprnt;
	private char casktpos;
	private int cadiablk;
	private int castdblk;

	public F55ca01Id() {
	}

	public F55ca01Id(int caid, String caname, String caco, int caan8,
			int cacati, int caauti, int caausu, int cacont, String cajobn,
			String carmk, String carmk1, char castat, String caprnt,
			char casktpos, int cadiablk, int castdblk) {
		this.caid = caid;
		this.caname = caname;
		this.caco = caco;
		this.caan8 = caan8;
		this.cacati = cacati;
		this.caauti = caauti;
		this.caausu = caausu;
		this.cacont = cacont;
		this.cajobn = cajobn;
		this.carmk = carmk;
		this.carmk1 = carmk1;
		this.castat = castat;
		this.caprnt = caprnt;
		this.casktpos = casktpos;
		this.cadiablk = cadiablk;
		this.castdblk = castdblk;
	}

	public int getCaid() {
		return this.caid;
	}

	public void setCaid(int caid) {
		this.caid = caid;
	}

	public String getCaname() {
		return this.caname;
	}

	public void setCaname(String caname) {
		this.caname = caname;
	}

	public String getCaco() {
		return this.caco;
	}

	public void setCaco(String caco) {
		this.caco = caco;
	}

	public int getCaan8() {
		return this.caan8;
	}

	public void setCaan8(int caan8) {
		this.caan8 = caan8;
	}

	public int getCacati() {
		return this.cacati;
	}

	public void setCacati(int cacati) {
		this.cacati = cacati;
	}

	public int getCaauti() {
		return this.caauti;
	}

	public void setCaauti(int caauti) {
		this.caauti = caauti;
	}

	public int getCaausu() {
		return this.caausu;
	}

	public void setCaausu(int caausu) {
		this.caausu = caausu;
	}

	public int getCacont() {
		return this.cacont;
	}

	public void setCacont(int cacont) {
		this.cacont = cacont;
	}

	public String getCajobn() {
		return this.cajobn;
	}

	public void setCajobn(String cajobn) {
		this.cajobn = cajobn;
	}

	public String getCarmk() {
		return this.carmk;
	}

	public void setCarmk(String carmk) {
		this.carmk = carmk;
	}

	public String getCarmk1() {
		return this.carmk1;
	}

	public void setCarmk1(String carmk1) {
		this.carmk1 = carmk1;
	}

	public char getCastat() {
		return this.castat;
	}

	public void setCastat(char castat) {
		this.castat = castat;
	}

	public String getCaprnt() {
		return this.caprnt;
	}

	public void setCaprnt(String caprnt) {
		this.caprnt = caprnt;
	}

	public char getCasktpos() {
		return this.casktpos;
	}

	public void setCasktpos(char casktpos) {
		this.casktpos = casktpos;
	}

	public int getCadiablk() {
		return this.cadiablk;
	}

	public void setCadiablk(int cadiablk) {
		this.cadiablk = cadiablk;
	}

	public int getCastdblk() {
		return this.castdblk;
	}

	public void setCastdblk(int castdblk) {
		this.castdblk = castdblk;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof F55ca01Id))
			return false;
		F55ca01Id castOther = (F55ca01Id) other;

		return (this.getCaid() == castOther.getCaid())
				&& ((this.getCaname() == castOther.getCaname()) || (this
						.getCaname() != null
						&& castOther.getCaname() != null && this.getCaname()
						.equals(castOther.getCaname())))
				&& ((this.getCaco() == castOther.getCaco()) || (this.getCaco() != null
						&& castOther.getCaco() != null && this.getCaco()
						.equals(castOther.getCaco())))
				&& (this.getCaan8() == castOther.getCaan8())
				&& (this.getCacati() == castOther.getCacati())
				&& (this.getCaauti() == castOther.getCaauti())
				&& (this.getCaausu() == castOther.getCaausu())
				&& (this.getCacont() == castOther.getCacont())
				&& ((this.getCajobn() == castOther.getCajobn()) || (this
						.getCajobn() != null
						&& castOther.getCajobn() != null && this.getCajobn()
						.equals(castOther.getCajobn())))
				&& ((this.getCarmk() == castOther.getCarmk()) || (this
						.getCarmk() != null
						&& castOther.getCarmk() != null && this.getCarmk()
						.equals(castOther.getCarmk())))
				&& ((this.getCarmk1() == castOther.getCarmk1()) || (this
						.getCarmk1() != null
						&& castOther.getCarmk1() != null && this.getCarmk1()
						.equals(castOther.getCarmk1())))
				&& (this.getCastat() == castOther.getCastat())
				&& ((this.getCaprnt() == castOther.getCaprnt()) || (this
						.getCaprnt() != null
						&& castOther.getCaprnt() != null && this.getCaprnt()
						.equals(castOther.getCaprnt())))
				&& (this.getCasktpos() == castOther.getCasktpos())
				&& (this.getCadiablk() == castOther.getCadiablk())
				&& (this.getCastdblk() == castOther.getCastdblk());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getCaid();
		result = 37 * result
				+ (getCaname() == null ? 0 : this.getCaname().hashCode());
		result = 37 * result
				+ (getCaco() == null ? 0 : this.getCaco().hashCode());
		result = 37 * result + this.getCaan8();
		result = 37 * result + this.getCacati();
		result = 37 * result + this.getCaauti();
		result = 37 * result + this.getCaausu();
		result = 37 * result + this.getCacont();
		result = 37 * result
				+ (getCajobn() == null ? 0 : this.getCajobn().hashCode());
		result = 37 * result
				+ (getCarmk() == null ? 0 : this.getCarmk().hashCode());
		result = 37 * result
				+ (getCarmk1() == null ? 0 : this.getCarmk1().hashCode());
		result = 37 * result + this.getCastat();
		result = 37 * result
				+ (getCaprnt() == null ? 0 : this.getCaprnt().hashCode());
		result = 37 * result + this.getCasktpos();
		result = 37 * result + this.getCadiablk();
		result = 37 * result + this.getCastdblk();
		return result;
	}

}

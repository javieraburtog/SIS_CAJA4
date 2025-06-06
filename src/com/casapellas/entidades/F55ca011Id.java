package com.casapellas.entidades;

// Generated 4/02/2009 17:10:07 by Hibernate Tools 3.2.0.b9

/**
 * F55ca011Id generated by hbm2java
 */
public class F55ca011Id implements java.io.Serializable {

	private int c1id;

	private String c1rp01;

	private String c1crcd;

	private char c1ryin;

	private String c1mcu;

	private String c1obj;

	private String c1sub;

	private char c1stat;

	private String c1mcuf;

	public F55ca011Id() {
	}

	public F55ca011Id(int c1id, String c1rp01, String c1crcd, char c1ryin,
			String c1mcu, String c1obj, String c1sub, char c1stat, String c1mcuf) {
		this.c1id = c1id;
		this.c1rp01 = c1rp01;
		this.c1crcd = c1crcd;
		this.c1ryin = c1ryin;
		this.c1mcu = c1mcu;
		this.c1obj = c1obj;
		this.c1sub = c1sub;
		this.c1stat = c1stat;
		this.c1mcuf = c1mcuf;
	}

	public int getC1id() {
		return this.c1id;
	}

	public void setC1id(int c1id) {
		this.c1id = c1id;
	}

	public String getC1rp01() {
		return this.c1rp01;
	}

	public void setC1rp01(String c1rp01) {
		this.c1rp01 = c1rp01;
	}

	public String getC1crcd() {
		return this.c1crcd;
	}

	public void setC1crcd(String c1crcd) {
		this.c1crcd = c1crcd;
	}

	public char getC1ryin() {
		return this.c1ryin;
	}

	public void setC1ryin(char c1ryin) {
		this.c1ryin = c1ryin;
	}

	public String getC1mcu() {
		return this.c1mcu;
	}

	public void setC1mcu(String c1mcu) {
		this.c1mcu = c1mcu;
	}

	public String getC1obj() {
		return this.c1obj;
	}

	public void setC1obj(String c1obj) {
		this.c1obj = c1obj;
	}

	public String getC1sub() {
		return this.c1sub;
	}

	public void setC1sub(String c1sub) {
		this.c1sub = c1sub;
	}

	public char getC1stat() {
		return this.c1stat;
	}

	public void setC1stat(char c1stat) {
		this.c1stat = c1stat;
	}

	public String getC1mcuf() {
		return this.c1mcuf;
	}

	public void setC1mcuf(String c1mcuf) {
		this.c1mcuf = c1mcuf;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof F55ca011Id))
			return false;
		F55ca011Id castOther = (F55ca011Id) other;

		return (this.getC1id() == castOther.getC1id())
				&& ((this.getC1rp01() == castOther.getC1rp01()) || (this
						.getC1rp01() != null
						&& castOther.getC1rp01() != null && this.getC1rp01()
						.equals(castOther.getC1rp01())))
				&& ((this.getC1crcd() == castOther.getC1crcd()) || (this
						.getC1crcd() != null
						&& castOther.getC1crcd() != null && this.getC1crcd()
						.equals(castOther.getC1crcd())))
				&& (this.getC1ryin() == castOther.getC1ryin())
				&& ((this.getC1mcu() == castOther.getC1mcu()) || (this
						.getC1mcu() != null
						&& castOther.getC1mcu() != null && this.getC1mcu()
						.equals(castOther.getC1mcu())))
				&& ((this.getC1obj() == castOther.getC1obj()) || (this
						.getC1obj() != null
						&& castOther.getC1obj() != null && this.getC1obj()
						.equals(castOther.getC1obj())))
				&& ((this.getC1sub() == castOther.getC1sub()) || (this
						.getC1sub() != null
						&& castOther.getC1sub() != null && this.getC1sub()
						.equals(castOther.getC1sub())))
				&& (this.getC1stat() == castOther.getC1stat())
				&& ((this.getC1mcuf() == castOther.getC1mcuf()) || (this
						.getC1mcuf() != null
						&& castOther.getC1mcuf() != null && this.getC1mcuf()
						.equals(castOther.getC1mcuf())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getC1id();
		result = 37 * result
				+ (getC1rp01() == null ? 0 : this.getC1rp01().hashCode());
		result = 37 * result
				+ (getC1crcd() == null ? 0 : this.getC1crcd().hashCode());
		result = 37 * result + this.getC1ryin();
		result = 37 * result
				+ (getC1mcu() == null ? 0 : this.getC1mcu().hashCode());
		result = 37 * result
				+ (getC1obj() == null ? 0 : this.getC1obj().hashCode());
		result = 37 * result
				+ (getC1sub() == null ? 0 : this.getC1sub().hashCode());
		result = 37 * result + this.getC1stat();
		result = 37 * result
				+ (getC1mcuf() == null ? 0 : this.getC1mcuf().hashCode());
		return result;
	}

}

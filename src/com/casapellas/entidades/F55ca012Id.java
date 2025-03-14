package com.casapellas.entidades;

// Generated 4/02/2009 17:10:07 by Hibernate Tools 3.2.0.b9

/**
 * F55ca012Id generated by hbm2java
 */
public class F55ca012Id implements java.io.Serializable {

	private static final long serialVersionUID = 8590931049113721471L;

	private int c2id;

	private String c2rp01;

	private String c2crcd;

	private char c2ryin;

	private long c2miam;

	private long c2mxam;

	private char c2stat;
	
	private int c2acpdn;

	public F55ca012Id() {
	}

 
	public F55ca012Id(int c2id, String c2rp01, String c2crcd, char c2ryin,
			long c2miam, long c2mxam, char c2stat, int c2acpdn) {
		super();
		this.c2id = c2id;
		this.c2rp01 = c2rp01;
		this.c2crcd = c2crcd;
		this.c2ryin = c2ryin;
		this.c2miam = c2miam;
		this.c2mxam = c2mxam;
		this.c2stat = c2stat;
		this.c2acpdn = c2acpdn;
	}


	public int getC2id() {
		return this.c2id;
	}

	public void setC2id(int c2id) {
		this.c2id = c2id;
	}

	public String getC2rp01() {
		return this.c2rp01;
	}

	public void setC2rp01(String c2rp01) {
		this.c2rp01 = c2rp01;
	}

	public String getC2crcd() {
		return this.c2crcd;
	}

	public void setC2crcd(String c2crcd) {
		this.c2crcd = c2crcd;
	}

	public char getC2ryin() {
		return this.c2ryin;
	}

	public void setC2ryin(char c2ryin) {
		this.c2ryin = c2ryin;
	}

	public long getC2miam() {
		return this.c2miam;
	}

	public void setC2miam(long c2miam) {
		this.c2miam = c2miam;
	}

	public long getC2mxam() {
		return this.c2mxam;
	}

	public void setC2mxam(long c2mxam) {
		this.c2mxam = c2mxam;
	}

	public char getC2stat() {
		return this.c2stat;
	}
	
	public int getC2acpdn() {
		return c2acpdn;
	}
	public void setC2acpdn(int c2acpdn) {
		this.c2acpdn = c2acpdn;
	}
	public void setC2stat(char c2stat) {
		this.c2stat = c2stat;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + c2acpdn;
		result = prime * result + ((c2crcd == null) ? 0 : c2crcd.hashCode());
		result = prime * result + c2id;
		result = prime * result + (int) (c2miam ^ (c2miam >>> 32));
		result = prime * result + (int) (c2mxam ^ (c2mxam >>> 32));
		result = prime * result + ((c2rp01 == null) ? 0 : c2rp01.hashCode());
		result = prime * result + c2ryin;
		result = prime * result + c2stat;
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
		F55ca012Id other = (F55ca012Id) obj;
		if (c2acpdn != other.c2acpdn)
			return false;
		if (c2crcd == null) {
			if (other.c2crcd != null)
				return false;
		} else if (!c2crcd.equals(other.c2crcd))
			return false;
		if (c2id != other.c2id)
			return false;
		if (c2miam != other.c2miam)
			return false;
		if (c2mxam != other.c2mxam)
			return false;
		if (c2rp01 == null) {
			if (other.c2rp01 != null)
				return false;
		} else if (!c2rp01.equals(other.c2rp01))
			return false;
		if (c2ryin != other.c2ryin)
			return false;
		if (c2stat != other.c2stat)
			return false;
		return true;
	}

	
}

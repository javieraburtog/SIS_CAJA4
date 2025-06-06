package com.casapellas.entidades;
// Generated Sep 9, 2013 3:43:45 PM by Hibernate Tools 3.2.1.GA


import java.math.BigDecimal;
import java.util.Date;

/**
 * Transactsp generated by hbm2java
 */
public class Transactsp  implements java.io.Serializable {
	
	private static final long serialVersionUID = -3949399119286161346L;
	private int idtransact;
	private String termid;
	private String acqnumber;
	private String systraceno;
	private String authorizationid;
	private String cardnumber;
	private BigDecimal amount;
	private String expirationdate;
	private String referencenumber;
	private Date transtime;
	private Date transdate;
	private String currency;
	private int caid;
	private String codcomp;
	private String clientname;
	private String responsecode;
	private String status;
	private String tiporec;
	private int statcred;

	
    public Transactsp(String termid, String acqnumber, String systraceno,
			String authorizationid, String cardnumber, BigDecimal amount,
			String expirationdate, String referencenumber, Date transtime,
			Date transdate, String currency, int caid, String codcomp,
			String clientname, String responsecode, String status,
			String tiporec, int statcred) {
		super();
		this.termid = termid;
		this.acqnumber = acqnumber;
		this.systraceno = systraceno;
		this.authorizationid = authorizationid;
		this.cardnumber = cardnumber;
		this.amount = amount;
		this.expirationdate = expirationdate;
		this.referencenumber = referencenumber;
		this.transtime = transtime;
		this.transdate = transdate;
		this.currency = currency;
		this.caid = caid;
		this.codcomp = codcomp;
		this.clientname = clientname;
		this.responsecode = responsecode;
		this.status = status;
		this.tiporec = tiporec;
		this.statcred = statcred;
	}
	
    public Transactsp() {
    }

	public Transactsp(int idtransact, String termid, String acqnumber,
			String systraceno, String authorizationid, String cardnumber,
			BigDecimal amount, String expirationdate, String referencenumber,
			Date transtime, Date transdate, String currency, int caid,
			String codcomp, String clientname, String responsecode,
			String status, String tiporec, int statcred) {
		super();
		this.idtransact = idtransact;
		this.termid = termid;
		this.acqnumber = acqnumber;
		this.systraceno = systraceno;
		this.authorizationid = authorizationid;
		this.cardnumber = cardnumber;
		this.amount = amount;
		this.expirationdate = expirationdate;
		this.referencenumber = referencenumber;
		this.transtime = transtime;
		this.transdate = transdate;
		this.currency = currency;
		this.caid = caid;
		this.codcomp = codcomp;
		this.clientname = clientname;
		this.responsecode = responsecode;
		this.status = status;
		this.tiporec = tiporec;
		this.statcred = statcred;
	}

	public int getIdtransact() {
        return this.idtransact;
    }
    
    public void setIdtransact(int idtransact) {
        this.idtransact = idtransact;
    }
    public String getTermid() {
        return this.termid;
    }
    
    public void setTermid(String termid) {
        this.termid = termid;
    }
    public String getAcqnumber() {
        return this.acqnumber;
    }
    
    public void setAcqnumber(String acqnumber) {
        this.acqnumber = acqnumber;
    }
    public String getSystraceno() {
        return this.systraceno;
    }
    
    public void setSystraceno(String systraceno) {
        this.systraceno = systraceno;
    }
    public String getAuthorizationid() {
        return this.authorizationid;
    }
    
    public void setAuthorizationid(String authorizationid) {
        this.authorizationid = authorizationid;
    }
    public String getCardnumber() {
        return this.cardnumber;
    }
    
    public void setCardnumber(String cardnumber) {
        this.cardnumber = cardnumber;
    }
    public BigDecimal getAmount() {
        return this.amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public String getExpirationdate() {
        return this.expirationdate;
    }
    
    public void setExpirationdate(String expirationdate) {
        this.expirationdate = expirationdate;
    }
    public String getReferencenumber() {
        return this.referencenumber;
    }
    
    public void setReferencenumber(String referencenumber) {
        this.referencenumber = referencenumber;
    }
    public Date getTranstime() {
        return this.transtime;
    }
    
    public void setTranstime(Date transtime) {
        this.transtime = transtime;
    }
    public Date getTransdate() {
        return this.transdate;
    }
    
    public void setTransdate(Date transdate) {
        this.transdate = transdate;
    }
    public String getCurrency() {
        return this.currency;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    public int getCaid() {
        return this.caid;
    }
    
    public void setCaid(int caid) {
        this.caid = caid;
    }
    public String getCodcomp() {
        return this.codcomp;
    }
    
    public void setCodcomp(String codcomp) {
        this.codcomp = codcomp;
    }
    public String getClientname() {
        return this.clientname;
    }
    
    public void setClientname(String clientname) {
        this.clientname = clientname;
    }
    public String getResponsecode() {
        return this.responsecode;
    }
    
    public void setResponsecode(String responsecode) {
        this.responsecode = responsecode;
    }
    public String getStatus() {
        return this.status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }

	public String getTiporec() {
		return tiporec;
	}

	public void setTiporec(String tiporec) {
		this.tiporec = tiporec;
	}

	public int getStatcred() {
		return statcred;
	}

	public void setStatcred(int statcred) {
		this.statcred = statcred;
	}

}



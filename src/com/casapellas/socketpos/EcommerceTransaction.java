package com.casapellas.socketpos;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EcommerceTransaction {

	private String transactionid;

	private String cardNumber;

	private String paymentAmmount;

	private String authorizationNumber;

	private String createUser;

	private String createProgramm;
	
	private String terminalid;
	
	
	private String referencenumber;	
	private String coin;
	private Date paymentDate;
	private String clientName;
	private String posCode;

	public EcommerceTransaction(String transactionid, String cardNumber, 
			String paymentAmmount, String authorizationNumber,
	        String createUser, String createProgramm, String terminalid) {
		
		this.transactionid = transactionid;
		this.cardNumber = cardNumber;
		this.paymentAmmount = paymentAmmount;
		this.authorizationNumber = authorizationNumber;
		this.createUser = createUser;
		this.createProgramm = createProgramm;
		this.terminalid = terminalid;
	}

	public String maskedCardNumber() {
		return "********" + shortCardNumber();
	}
	public String shortCardNumber() {
		return (cardNumber == null) ? "": cardNumber.substring(cardNumber.length()-4, cardNumber.length() );
	}
	public String formatPaymentDate() {
		return new SimpleDateFormat("MMM dd, yy - HH:mm:ss", Locale.US ).format( paymentDate );
	}
	
	public String getTransactionid() {
		return transactionid;
	}

	public void setTransactionid(String transactionid) {
		this.transactionid = transactionid;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getPaymentAmmount() {
		return paymentAmmount;
	}

	public void setPaymentAmmount(String paymentAmmount) {
		this.paymentAmmount = paymentAmmount;
	}

	public String getAuthorizationNumber() {
		return authorizationNumber;
	}

	public void setAuthorizationNumber(String authorizationNumber) {
		this.authorizationNumber = authorizationNumber;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getCreateProgramm() {
		return createProgramm;
	}

	public void setCreateProgramm(String createProgramm) {
		this.createProgramm = createProgramm;
	}

	public String getTerminalid() {
		return terminalid;
	}

	public void setTerminalid(String p_terminalid) {
		terminalid = p_terminalid;
	}

	public String getReferencenumber() {
		return referencenumber;
	}

	public void setReferencenumber(String p_referencenumber) {
		referencenumber = p_referencenumber;
	}

	public String getCoin() {
		return coin;
	}

	public void setCoin(String p_coin) {
		coin = p_coin;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date p_paymentDate) {
		paymentDate = p_paymentDate;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String p_clientName) {
		clientName = p_clientName;
	}

	public String getPosCode() {
		return posCode;
	}

	public void setPosCode(String p_posCode) {
		posCode = p_posCode;
	}

}

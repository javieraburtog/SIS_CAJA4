package com.casapellas.util;

public class CustomEmailAddress {
	private String email;
	private String name;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public CustomEmailAddress() {
	}
	public CustomEmailAddress(String email) {
		this.email = email;
	}
	public CustomEmailAddress(String email, String name) {
		this.email = email;
		this.name = name;
	}
}

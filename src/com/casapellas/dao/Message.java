package com.casapellas.dao;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class Message implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	public static final int MESSAGE_TYPE_SUCCESS = 0;
	public static final int MESSAGE_TYPE_ERROR = 1;
	
	private int code;
	private String type;
	private String title;
	private String message = "";

	public Message() {

	}
	

	public Message(  String title, String message) {
		super();
		this.title = title;
		this.message = message;
	}

	public Message(int code, String title, String message) {
		super();
		this.code = code;
		this.title = title;
		this.message = message;
	}

	public Message(int code, String type, String title, String message) {
		super();
		this.code = code;
		this.type = type;
		this.title = title;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}

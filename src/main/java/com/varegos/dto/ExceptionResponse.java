package com.varegos.dto;

public class ExceptionResponse {
	
	private String message;
	private int code;
	
	public ExceptionResponse(String message, int code) {
		this.message = message;
		this.code = code;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public void setCode(int code) {
		this.code = code;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public int getCode() {
		return this.code;
	}

}

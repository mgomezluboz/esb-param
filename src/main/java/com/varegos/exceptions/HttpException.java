package com.varegos.exceptions;

public class HttpException extends Exception {
	
	private static final long serialVersionUID = 8492083904l;
	private static final Integer DEFAULT_CODE = 101;
	
	public final Integer targetStatus;
	public final Integer code;
	
	public HttpException(String message, Integer targetStatus) {
		super(message);
		this.code = DEFAULT_CODE;
		this.targetStatus = targetStatus;
	}
	
	public HttpException(String message, Integer targetStatus, Integer code) {
		super(message);
		this.code = code;
		this.targetStatus = targetStatus;
	}
	
}

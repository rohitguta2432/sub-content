package com.vedanta.vpmt.web;

public class VedantaWebException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public int code;
	
	public VedantaWebException() {
		super();
	}
	public VedantaWebException(String message, Throwable cause, boolean enableSuppression,
                               boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	public VedantaWebException(String message, Throwable cause) {
		super(message, cause);
	}
	public VedantaWebException(String message) {
		super(message);
	}
	public VedantaWebException(String message, int code) {
		super(message);
		this.code = code;
	}
	public VedantaWebException(Throwable cause) {
		super(cause);
	}
	

}

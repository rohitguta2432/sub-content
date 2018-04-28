package com.vedanta.vpmt.exception;

public class VedantaException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public VedantaException() {
	}

	public VedantaException(String message) {
		super(message);
	}

	public VedantaException(Throwable cause) {
		super(cause);
	}

	public VedantaException(String message, Throwable cause) {
		super(message, cause);
	}

	public VedantaException(String message, Throwable cause, boolean enableSuppression,
                            boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}

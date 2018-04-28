package com.vedanta.vpmt.exception;

public class ResourceNotFoundExecption extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ResourceNotFoundExecption() {
		super();
	}

	public ResourceNotFoundExecption(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ResourceNotFoundExecption(String message, Throwable cause) {
		super(message, cause);
	}

	public ResourceNotFoundExecption(String message) {
		super(message);
	}

	public ResourceNotFoundExecption(Throwable cause) {
		super(cause);
	}

}

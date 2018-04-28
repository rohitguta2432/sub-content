package com.vedanta.vpmt.exception;

public class AuthenticationTokenException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public AuthenticationTokenException() {}

  public AuthenticationTokenException(String message) {
    super(message);
  }

  public AuthenticationTokenException(Throwable cause) {
    super(cause);
  }

  public AuthenticationTokenException(String message, Throwable cause) {
    super(message, cause);
  }

  public AuthenticationTokenException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}

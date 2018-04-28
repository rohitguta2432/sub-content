package com.vedanta.vpmt.exception;

public class DuplicateLoginException extends RuntimeException {

  private static final long serialVersionUID = 1445163827367411539L;

  public DuplicateLoginException(String message) {
    super(message);
  }

}

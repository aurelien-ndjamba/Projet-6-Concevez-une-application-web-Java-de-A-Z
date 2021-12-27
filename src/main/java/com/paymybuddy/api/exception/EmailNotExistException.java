package com.paymybuddy.api.exception;

public class EmailNotExistException extends Exception {
private static final long serialVersionUID = 1L;
	
	public EmailNotExistException(String string) {
		super(string);
	}
}

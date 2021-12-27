package com.paymybuddy.api.exception;

public class AccountNotExistException extends Exception {
private static final long serialVersionUID = 1L;
	
	public AccountNotExistException(String string) {
		super(string);
	}
}

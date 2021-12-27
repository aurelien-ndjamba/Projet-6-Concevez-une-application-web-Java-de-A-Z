package com.paymybuddy.api.exception;

public class AccountNotForUserException extends Exception {
private static final long serialVersionUID = 1L;
	
	public AccountNotForUserException(String string) {
		super(string);
	}
}

package com.paymybuddy.api.exception;

public class InsufficientBalanceException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public InsufficientBalanceException(String string) {
		super(string);
	}

}

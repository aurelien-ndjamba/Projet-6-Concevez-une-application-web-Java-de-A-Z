package com.paymybuddy.api.exception;

public class FriendEmailNotExistException extends Exception {
private static final long serialVersionUID = 1L;
	
	public FriendEmailNotExistException(String string) {
		super(string);
	}
}

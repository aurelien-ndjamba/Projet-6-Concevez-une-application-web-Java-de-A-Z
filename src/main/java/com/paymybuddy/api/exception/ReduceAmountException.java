package com.paymybuddy.api.exception;

import org.springframework.http.HttpStatus;

public class ReduceAmountException extends Exception{
	
private static final long serialVersionUID = 1L;

private HttpStatus status;
private String message;

public HttpStatus getStatus() {
	return status;
}

public void setStatus(HttpStatus status) {
	this.status = status;
}

public String getMessage() {
	return message;
}

public void setMessage(String message) {
	this.message = message;
}

public ReduceAmountException(String message) {
    this.message = message;
}

public ReduceAmountException(String message, HttpStatus status) {
		this.status = status;
        this.message = message;
	}

}

package com.paymybuddy.webapp.model;


import lombok.Data;

@Data
public class AppUser {
	
	private String email;
	
	private String password;
	
	private Double balance;
	
	}
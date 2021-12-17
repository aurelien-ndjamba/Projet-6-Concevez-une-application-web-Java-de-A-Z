package com.paymybuddy.webapp.model;

import java.util.List;
import lombok.Data;

@Data
public class AppUser {
	
	private String email;
	private String pseudo;
	private String password;
	private Double balance;
	private int phone;
	private boolean active;
	private List<Role> roles;
	private Account account;
	
	}
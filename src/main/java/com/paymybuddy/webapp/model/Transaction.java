package com.paymybuddy.webapp.model;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class Transaction {

	private Integer id;
	
	private String email;
	
	private String user_email;
	
	private Integer account_id;
	
	private String type;
	
	private Double amount;
	
	private Timestamp date;
	
	private String description;
}

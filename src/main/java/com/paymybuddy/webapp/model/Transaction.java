package com.paymybuddy.webapp.model;

import java.sql.Timestamp;
import java.util.UUID;

import lombok.Data;

@Data
public class Transaction {

	private UUID id;
	private String user;
	private String friend;
	private Integer accountUser;
	private String type;
	private Double lastBalance;
	private Double newBalance;
	private Double fee;
	private Double amount;
	private Timestamp date;
	private String description;
	
}

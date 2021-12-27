package com.paymybuddy.webapp.model;

import java.sql.Timestamp;
import java.util.UUID;
import lombok.Data;

@Data
public class TransactionStructured {
	
		private UUID id;
		private String friend;
		private String pseudo;
		private Integer accountUser;
		private String type;
		private Double amount;
		private Timestamp date;
		private String description;
		
	}
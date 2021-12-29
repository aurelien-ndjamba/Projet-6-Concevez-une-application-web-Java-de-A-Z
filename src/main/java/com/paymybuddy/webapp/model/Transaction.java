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

	public Transaction() {
		super();
	}

	public Transaction(UUID id, String user, String friend, Integer accountUser, String type, Double lastBalance,
			Double newBalance, Double fee, Double amount, Timestamp date, String description) {
		super();
		this.id = id;
		this.user = user;
		this.friend = friend;
		this.accountUser = accountUser;
		this.type = type;
		this.lastBalance = lastBalance;
		this.newBalance = newBalance;
		this.fee = fee;
		this.amount = amount;
		this.date = date;
		this.description = description;
	}

	public Transaction success(Transaction t) {
		Transaction tNew = new Transaction();
		tNew.id = t.id;
		tNew.user = t.user;
		tNew.friend = t.friend;
		tNew.accountUser = t.accountUser;
		tNew.type = t.type;
		tNew.lastBalance = t.lastBalance;
		tNew.newBalance = t.newBalance;
		tNew.fee = t.fee;
		tNew.amount = t.amount;
		tNew.date = t.date;
		tNew.description = t.description;

		return tNew;
	}

}

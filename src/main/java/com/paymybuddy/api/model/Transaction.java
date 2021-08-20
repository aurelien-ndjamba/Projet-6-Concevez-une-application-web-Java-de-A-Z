package com.paymybuddy.api.model;

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import lombok.Data;

@Data
@Entity
public class Transaction {
	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	@Column(name = "id", unique = true, nullable = false)
	@Type(type="pg-uuid")
	private UUID id;
	
	@JoinColumn(name = "user_transaction_fk")
	@Column(name = "email")
	private String user;
	
	@JoinColumn(name = "user_transaction_fk1")
	@Column(name = "user_email")
	private String friend;
	
	@JoinColumn(name = "account_transaction_fk")
	@Column(name = "account_id")
	private Integer accountUser;
	
	@Column(name = "type")
	private String type;
	
	@Column(name = "amount")
	private Double amount;
	
	@Column(name = "date")
	@CreationTimestamp
	private Timestamp date;
	
	@Column(name = "description")
	private String description;
}

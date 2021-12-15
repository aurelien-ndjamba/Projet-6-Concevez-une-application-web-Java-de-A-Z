package com.paymybuddy.api.model;

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@Entity
@Table(name="transaction", schema = "public")
public class Transaction {
	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	@Column(name = "id", unique = true, nullable = false)
	@Type(type="pg-uuid")
	private UUID id;
	
//	@JoinColumn(name = "appuser_transaction_fk")
	@Column(name = "emailuser")
	private String user;
	
//	@JoinColumn(name = "userapp_transaction_fk")
	@Column(name = "emailfriend")
	private String friend;
	
//	@JoinColumn(name = "account_transaction_fk")
	@Column(name = "accountnumber")
	private Integer accountUser;
	
	@Column(name = "type")
	private String type;
	
	@Column(name = "amount")
	private Double amount;
	
	@JsonInclude()
	@Transient
	private Double newBalance;
	
	@JsonInclude()
	@Transient
	private Double lastBalance;
	
	@JsonInclude()
	@Transient
	private Double fee;
	
	@Column(name = "date")
	@CreationTimestamp
	private Timestamp date;
	
	@Column(name = "description")
	private String description;
}

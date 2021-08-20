package com.paymybuddy.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import lombok.Data;


/** 
 * Classe modélisant un objet entité de type "Account"
 * 
 */
@Data
@Entity
public class Account {
	@Id
//	@Column(name = "id", nullable = false, updatable = false)
    private Integer id;
	
	@JoinColumn(name = "account_userapp_fk")
	@Column(name = "user_email")
	private String email;
	
	@Column(name = "bank")
	private String bank;
}
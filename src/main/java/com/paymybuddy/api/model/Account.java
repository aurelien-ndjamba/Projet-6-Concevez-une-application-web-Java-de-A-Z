package com.paymybuddy.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

import lombok.Data;


/** 
 * Classe modélisant un objet entité de type "Account"
 * 
 */
@Data
@Entity
@Table(name = "account", schema = "public")
@Component
public class Account {
	@Id
	@Column(name = "accountnumber", nullable = false)
    private Integer id;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "bank")
	private String bank;
}
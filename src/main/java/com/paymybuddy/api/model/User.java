package com.paymybuddy.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
//import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * Classe modélisant un objet entité de type "User"
 * 
 */
@Data
@Entity
@Table(name="user", schema = "public")
@Component
public class User {
	@Id
//	@OneToMany(mappedBy="user")
	@Column(name = "email", nullable = false, updatable = false)
	private String email;

	@Column(name = "password")
	private String password;
	
	@Column(name = "balance")
	private Double balance;
}
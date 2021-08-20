package com.paymybuddy.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
//	@OneToOne(
//			   cascade = CascadeType.ALL, 
//			   orphanRemoval = true, 
//			   fetch = FetchType.EAGER)
//			)
	@Column(name = "email", nullable = false, updatable = false)
	private String email;

	@Column(name = "password")
//	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
//	private String result = encoder.encode("myPassword");
	private String password;
	
	@Column(name = "balance")
	private Double balance;
}
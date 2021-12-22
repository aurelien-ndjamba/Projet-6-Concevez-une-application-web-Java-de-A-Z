package com.annotationJpa.OneToOne;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

/**
 * Classe modélisant un objet entité de type "User"
 * 
 */

@Data
@Entity
@Table(name = "appuser", schema = "public")
public class AppUser {

	@Id
	@Column(name = "email", nullable = false, updatable = false)
	private String email;

	private String pseudo;

	private String password;

	private Double balance;

	private int phone;

	private boolean active;
	
//	@OneToOne( fetch = FetchType.EAGER )
//	@JoinColumn(name = "Account.id")
//	private Account account;
	
}
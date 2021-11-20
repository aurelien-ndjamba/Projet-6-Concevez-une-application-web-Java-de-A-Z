package com.paymybuddy.api.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.JoinColumn;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * Classe modélisant un objet entité de type "User"
 * 
 */

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "appuser", schema = "public")
@Component
public class AppUser {
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@JsonIgnore
	public String getPassword() {
		return password;
	}

	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	@Id
	@Column(name = "email", nullable = false, updatable = false)
	private String email;

	@JsonIgnore
	@Column(name = "password")
	private String password;

	@Column(name = "balance")
	private Double balance;

	@ManyToMany(fetch = FetchType.EAGER) //la suppression marche sur userrole
	@JoinTable(name = "userrole", joinColumns = @JoinColumn(name = "email"), inverseJoinColumns = @JoinColumn(name = "rolename"))
	private List<Role> roles = new ArrayList<>();

//	@OneToMany(mappedBy="email", fetch = FetchType.LAZY) ou
	@OneToMany(
			   cascade = CascadeType.ALL, 
			   orphanRemoval = true)
				@JoinColumn(name = "email")
	private List<Account> accounts = new ArrayList<>();

}
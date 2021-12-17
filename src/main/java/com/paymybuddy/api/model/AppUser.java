package com.paymybuddy.api.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
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

	@Id
	@Column(name = "email", nullable = false, updatable = false)
	private String email;

	@Column(name = "pseudo")
	private String pseudo;

//	@JsonIgnore
	@Column(name = "password")
	private String password;

	@Column(name = "balance")
	private Double balance;

	@Column(name = "phone")
	private int phone;

	@Column(name = "active")
	private boolean active;
	//(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@ManyToMany(fetch = FetchType.EAGER) // la suppression marche sur userrole
	@JoinTable(name = "userrole", joinColumns = @JoinColumn(name = "email"), inverseJoinColumns = @JoinColumn(name = "rolename"))
	private List<Role> roles;

	@OneToOne(fetch = FetchType.EAGER) //OK
	@JoinTable(name = "account", joinColumns = @JoinColumn(name = "email"), inverseJoinColumns = @JoinColumn(name = "accountnumber"))
	private Account account;

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

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	public int getPhone() {
		return phone;
	}

	public void setPhone(int phone) {
		this.phone = phone;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

}
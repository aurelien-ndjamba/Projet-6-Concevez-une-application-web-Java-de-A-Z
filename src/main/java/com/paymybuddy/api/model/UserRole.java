package com.paymybuddy.api.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="userrole", schema = "public")
@IdClass(UserRoleId.class)
public class UserRole implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name ="email")
//	@JoinColumn(name = "userapp_friend_fk")
	private String email;
	
	@Id
	@Column(name ="rolename")
//	@JoinColumn(name = "appUser_friend_fk")
	private String roleName;

}
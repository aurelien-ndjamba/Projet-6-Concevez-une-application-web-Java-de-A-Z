package com.paymybuddy.api.model;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class UserRoleId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String email;
	private String roleName;

	public UserRoleId() {
	    }

	public UserRoleId(String email, String roleName) {
	        this.email = email;
	        this.roleName = roleName;
	}
}

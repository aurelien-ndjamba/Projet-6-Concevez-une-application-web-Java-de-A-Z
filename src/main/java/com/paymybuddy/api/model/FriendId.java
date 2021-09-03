package com.paymybuddy.api.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class FriendId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String emailUser;
	private String emailFriend;

	public FriendId() {
	    }

	public FriendId(String emailUser, String emailFriend) {
	        this.emailUser = emailUser;
	        this.emailFriend = emailFriend;
	}
	
}

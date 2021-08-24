package com.paymybuddy.api.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
@IdClass(FriendId.class)
public class Friend implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
//	@ManyToOne
	@Column(name ="user_email1")
	@JoinColumn(name = "userapp_friend_fk")
	private String emailUser;
	
	@Id
//	@ManyToOne
	@Column(name ="user_email2")
	@JoinColumn(name = "user_friend_fk")
	private String emailFriend;

}

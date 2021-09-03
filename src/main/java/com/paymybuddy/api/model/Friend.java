package com.paymybuddy.api.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="friend", schema = "public")
@IdClass(FriendId.class)
public class Friend implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name ="email1")
	@JoinColumn(name = "userapp_friend_fk")
	private String emailUser;
	
	@Id
	@Column(name ="email2")
	@JoinColumn(name = "appuser_friend_fk")
	private String emailFriend;

}

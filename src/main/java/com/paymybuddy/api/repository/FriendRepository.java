package com.paymybuddy.api.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.paymybuddy.api.model.Friend;
import com.paymybuddy.api.model.FriendId;

@Repository
public interface FriendRepository extends JpaRepository<Friend, FriendId> {

	@Query("select emailUser, emailFriend from Friend")
	ArrayList<String> findAllFriends();
	
	@Query("select emailUser, emailFriend from Friend f where emailUser = ?1 or emailFriend = ?1")
	ArrayList<String> findByEmail(String email);
	
	@Transactional
	@Modifying
	@Query("delete from Friend f where (f.emailUser = ?1 and f.emailFriend = ?2) or (f.emailUser = ?2 and f.emailFriend = ?1)")
	void delete(String emailUser, String emailFriend);

}
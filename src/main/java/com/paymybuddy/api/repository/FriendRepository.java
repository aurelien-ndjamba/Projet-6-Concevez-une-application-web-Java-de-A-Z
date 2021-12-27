package com.paymybuddy.api.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.paymybuddy.api.model.Friend;
import com.paymybuddy.api.model.FriendId;

@Repository
public interface FriendRepository extends JpaRepository<Friend, FriendId> {

	List<Friend> findByEmailUserOrEmailFriend(String email,String email2);
	List<Friend> findByEmailUserOrEmailFriend(String email, String email2, Pageable pagelist);
	Friend findByEmailUserAndEmailFriend(String email,String email2);
	
	@Transactional
	@Modifying
	@Query("delete from Friend f where (f.emailUser = ?1 and f.emailFriend = ?2) or (f.emailUser = ?2 and f.emailFriend = ?1)")
	void delete(String emailUser, String emailFriend);

}
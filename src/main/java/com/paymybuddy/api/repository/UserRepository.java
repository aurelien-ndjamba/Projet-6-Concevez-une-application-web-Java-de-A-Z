package com.paymybuddy.api.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.paymybuddy.api.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
	
	Optional<User> findByEmail(String email);

	@Query("select email, balance from User")
	List<String> findEmailsAndBalances();
	
	@Query("select email, balance from User u where email = ?1")
	List<String> findEmailAndBalanceByEmail(String email);
	
	@Query("select email, balance from User where email <> ?1")
	List<String> findByEmailNot(String withoutemail);

	@Transactional
	void deleteAllByEmail(String email);

}
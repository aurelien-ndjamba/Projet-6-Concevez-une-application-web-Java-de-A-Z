package com.paymybuddy.api.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.paymybuddy.api.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

//	@Query("select id, user, user_email, accountUser, type, amount, date, description from User u where id = ?1")
	Optional<Transaction> findById(UUID id);
	
//	@Query("select id, email, user_email, account_id, type, amount, date, description from User u where user = ?1 or user_email = ?1")
//	@Query("select emailUser, emailFriend from Friend f where emailUser = ?1 or emailFriend = ?1")
	
//	@Query("select id, email, user_email, account_id, type, amount, date, description from Transaction where email = ?1")
	Iterable<String> findByUser(String email);
	
//	Iterable<Transaction> findByUser(String email);

	Iterable<Transaction> findByType(String type);

	@Transactional
	void deleteAllById(int id);

}
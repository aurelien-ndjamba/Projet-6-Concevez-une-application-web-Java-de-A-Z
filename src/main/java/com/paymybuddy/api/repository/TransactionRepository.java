package com.paymybuddy.api.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.paymybuddy.api.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

	Optional<Transaction> findById(UUID id);
	
	List<Transaction> findByUserOrFriend(String email, String friend);
	
	List<Transaction> findByType(String type);

	@Transactional
	void deleteAllById(int id);

}
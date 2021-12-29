package com.paymybuddy.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.api.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

	Account findById(int id);  // Pour ne pas dépendre du retour par defaut optional<Account>
	Account findByEmail(String email);

}

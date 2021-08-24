package com.paymybuddy.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.api.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

	Account findById(int id);

	List<Account> findByEmail(String email);

	List<Account> findByBank(String bank);

}

package com.paymybuddy.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paymybuddy.api.model.Account;
import com.paymybuddy.api.repository.AccountRepository;
import com.paymybuddy.api.repository.UserRepository;

@Service
public class AccountService {

	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private UserRepository userRepository;

	public void setAccountRepository(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public List<Account> findAll() {
		return accountRepository.findAll();
	}

	@Transactional
	public Account findById(int id) {
		if (! accountRepository.existsById(id)) {
			throw new RuntimeException("Compte bancaire non existant dans la BDD");
		}
		return accountRepository.findById(id);
	}

	@Transactional
	public List<Account> findByEmail(String email) {
		if (userRepository.findByEmail(email) == null) {
			throw new RuntimeException("Email non existant dans la BDD");
		}
		return accountRepository.findByEmail(email);
	}

	@Transactional
	public Account save(Account account) {
		if (account.getId() == null) {
			throw new RuntimeException("Vous devez renseigner un numéro de compte!");
		} else if (accountRepository.findById(account.getId()).isPresent()) {
			throw new RuntimeException("Compte bancaire déjà existant dans la BDD");
		} else if (account.getEmail() == null) {
			throw new RuntimeException("Vous devez renseigner l'email associé à ce compte bancaire!");
		} else if (userRepository.findByEmail(account.getEmail()) == null) {
			throw new RuntimeException("Email non existant dans la BDD");
		} else if (account.getBank() == null) {
			throw new RuntimeException("Vous devez renseigner le nom de la banque associée à votre compte bancaire!");
		}
		return accountRepository.save(account);
	}

	@Transactional
	public Account update(Account account) {
		if (account.getId() == null) {
			throw new RuntimeException("Vous devez renseigner un numéro de compte!");
		} else if (accountRepository.findById(account.getId()).isEmpty()) {
			throw new RuntimeException("Compte bancaire non existant dans la BDD");
		} else if (account.getEmail() == null) {
			throw new RuntimeException("Vous devez renseigner l'email associé à ce compte bancaire!");
		} else if (userRepository.findByEmail(account.getEmail()) == null) {
			throw new RuntimeException("Email non existant dans la BDD");
		} else if (account.getBank() == null) {
			throw new RuntimeException("Vous devez renseigner le nom de la banque associée à votre compte bancaire!");
		}
		return accountRepository.save(account);
	}

	@Transactional
	public Account deleteById(Integer id) {
		Account account = new Account();
		if (accountRepository.findById(id).isEmpty()) {
			throw new RuntimeException("Compte bancaire non existant dans la BDD");
		}
		account = accountRepository.findById(id).get();
		accountRepository.deleteById(id);
		return account;
	}

}

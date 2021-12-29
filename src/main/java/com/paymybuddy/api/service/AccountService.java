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

	public AccountRepository getAccountRepository() {
		return accountRepository;
	}

	public UserRepository getUserRepository() {
		return userRepository;
	}

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
	public Account findById(int id) throws Exception {
		if (!accountRepository.existsById(id)) {
			throw new Exception("Compte bancaire non existant dans la BDD");
		}
		return accountRepository.findById(id);
	}

	@Transactional
	public Account findByEmail(String email) throws Exception {
		if (!userRepository.existsById(email)) {
			throw new Exception("Email non existant dans la BDD");
		}
		return accountRepository.findByEmail(email);
	}

	@Transactional
	public Account save(Account account) throws Exception {
		if (account.getId() == null) {
			throw new Exception("Vous devez renseigner un numéro de compte!");
		} else if (accountRepository.existsById(account.getId())) {
			throw new Exception("Numéro bancaire déjà existant dans la BDD");
		} else if (account.getEmail() == null) {
			throw new Exception("Vous devez renseigner l'email associé à ce compte bancaire!");
		}
		else if (! userRepository.existsById(account.getEmail())) {
			throw new Exception("Email non existant dans la BDD");
		} 
		else if (account.getBank() == null) {
			throw new Exception("Vous devez renseigner le nom de la banque associée à votre compte bancaire!");
		}
		return accountRepository.save(account);
	}

	@Transactional
	public Account updateBank(Account account) throws Exception {
		Account accountUpdated = findByEmail(account.getEmail());
		accountUpdated.setBank(account.getBank());
		return accountRepository.save(accountUpdated);
	}

//	@Transactional
//	public Account deleteById(Integer id) throws Exception {
//		Account account = new Account();
//		if (accountRepository.findById(id).isEmpty()) {
//			throw new Exception("Compte bancaire non existant dans la BDD");
//		}
//		account = accountRepository.findById(id).get();
//		accountRepository.deleteById(id);
//		return account;
//	}

}

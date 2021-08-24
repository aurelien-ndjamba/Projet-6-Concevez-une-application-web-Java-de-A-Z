package com.paymybuddy.api.service;

import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.api.model.Account;
import com.paymybuddy.api.repository.AccountRepository;

@Service
public class AccountService {

	private Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private AccountRepository accountRepository;

	public void setAccountRepository(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	public List<Account> findAll() {
		return accountRepository.findAll();
	}

	public Account findById(int id) {
		return accountRepository.findById(id);
	}

	public List<Account> findByEmail(String email) {
		return accountRepository.findByEmail(email);
	}

	public List<Account> findByBank(String bank) {
		return accountRepository.findByBank(bank);
	}

	public Account save(Account account) {
		if (accountRepository.findById(account.getId()).isPresent()) {
			account.setId(null);
			logger.info("INFO: Le numéro du compte 'id' : " + account.getId() + " existe déjà !");
		}
		return accountRepository.save(account);
	}

	public Account update(Account account) {
		if (accountRepository.findById(account.getId()).isEmpty()) {
			account.setId(null);
			logger.error("ERROR: Le numéro du compte 'id': " + account.getId()
					+ " n'existe pas dans la BDD de l'application");
		}
		return accountRepository.save(account);
	}

	public Account deleteById(Integer id) {
		Account account = new Account();
		if (accountRepository.findById(id).isPresent()) {
			account = accountRepository.findById(id).get();
			accountRepository.deleteById(id);
		}
		return account;
	}

}

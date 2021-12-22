package com.paymybuddy.webapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.webapp.model.Account;
import com.paymybuddy.webapp.repository.AccountProxy;

@Service
public class AccountService {
	
	@Autowired
	private AccountProxy accountProxy;
	
	public List<Account> getAccountsByEmail(String email) {
		return accountProxy.getAccountsByEmail(email);
	}

	public Account updateAccount(Account account) {
		return accountProxy.updateAccount(account);
	}

	public Account update(Account account) {
		return accountProxy.update(account);
	}

	public Account findById(Integer id) {
		return accountProxy.findById(id);
	}

	public Account save(Account acc) {
		return accountProxy.save(acc);
	}

	public Account findByEmail(String email) {
		return accountProxy.findByEmail(email);
	}

	public Account deleteById(int id) {
		return accountProxy.deleteById(id);
	}
	
}

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

	public Account saveAccount(Account account) {
		return accountProxy.saveAccount(account);
	}
	
	public Account deleteAccount(Integer id) {
		return accountProxy.deleteAccount(id);
	}
	
}

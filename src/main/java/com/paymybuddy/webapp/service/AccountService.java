package com.paymybuddy.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.webapp.model.Account;
import com.paymybuddy.webapp.repository.AccountProxy;

@Service
public class AccountService {
	
	@Autowired
	private AccountProxy accountProxy;

	public Account findByEmail(String email) {
		return accountProxy.findByEmail(email);
	}

	public Account save(Account acc) {
		return accountProxy.save(acc);
	}

	public Account deleteById(int id) {
		return accountProxy.deleteById(id);
	}
	
}

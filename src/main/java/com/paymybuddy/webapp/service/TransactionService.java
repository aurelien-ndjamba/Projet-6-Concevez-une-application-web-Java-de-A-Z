package com.paymybuddy.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.webapp.model.AppUser;
import com.paymybuddy.webapp.repository.TransactionRepository;
import com.paymybuddy.webapp.repository.AppUserProxy;

import lombok.Data;

@Data
@Service
public class TransactionService {

	 @Autowired
	    private TransactionRepository transactionRepository;
	 
//	    public Iterable<Transactions> getTransactions() {
//	        return transactionRepository.getTransactions();
//	    }
}

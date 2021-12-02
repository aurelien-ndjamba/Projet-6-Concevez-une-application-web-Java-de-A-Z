package com.paymybuddy.webapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.webapp.model.Transaction;
import com.paymybuddy.webapp.repository.TransactionProxy;

import lombok.Data;

@Data
@Service
public class TransactionService {

	 @Autowired
	    private TransactionProxy transactionProxy;
	 
	    public List<Transaction> getTransactions(String emailUser) {
	        return transactionProxy.getTransactions(emailUser);
	    }
	 
}

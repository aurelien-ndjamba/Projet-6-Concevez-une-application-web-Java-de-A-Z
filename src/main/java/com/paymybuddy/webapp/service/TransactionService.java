package com.paymybuddy.webapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.webapp.model.Transaction;
import com.paymybuddy.webapp.model.TransactionStructured;
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

	public List<TransactionStructured> findByEmailStructured(String email) { 
		return transactionProxy.findByEmailStructured(email);
	}

	public Transaction createPayment(Transaction transaction) {
		return transactionProxy.createPayment(transaction);
	}

	public Transaction createDeposit(Transaction transaction) {
		return transactionProxy.createDeposit(transaction);
	}

	public Transaction createWithdrawal(Transaction transaction) {
		return transactionProxy.createWithdrawal(transaction);

	}

}
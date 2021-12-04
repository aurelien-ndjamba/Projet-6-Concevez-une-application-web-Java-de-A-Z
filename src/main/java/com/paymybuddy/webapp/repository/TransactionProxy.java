package com.paymybuddy.webapp.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import com.paymybuddy.webapp.CustomProperties;
import com.paymybuddy.webapp.model.Transaction;

@Component
public class TransactionProxy {
	
	@Autowired
	private CustomProperties props;
	
	public List<Transaction> getTransactions(String emailUser) {
		String baseApiUrl = props.getApiUrl();
		String getUserUrl = baseApiUrl + "/transactions?email=" + emailUser;

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<Transaction>> response = restTemplate.exchange(getUserUrl, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Transaction>>() {
				});

		return response.getBody();
	}
	
	public Transaction createPayment(@RequestBody Transaction transaction) {
		String baseApiUrl = props.getApiUrl();
		String paymentUrl = baseApiUrl + "/transactions/createpayment";

		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<Transaction> request = new HttpEntity<Transaction>(transaction);
		ResponseEntity<Transaction> response = restTemplate.exchange(
				paymentUrl, 
				HttpMethod.POST,
				request,
				Transaction.class);

		return response.getBody();
	}
	
	public Transaction createDeposit(@RequestBody Transaction transaction) {
		String baseApiUrl = props.getApiUrl();
		String depositUrl = baseApiUrl + "/transactions/createdeposit";
		System.out.println(depositUrl);
		System.out.println(transaction);
		
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<Transaction> request = new HttpEntity<Transaction>(transaction);
		ResponseEntity<Transaction> response = restTemplate.exchange(
				depositUrl, 
				HttpMethod.POST,
				request,
				Transaction.class);

		return response.getBody();
	}
	
	public Transaction createWithdrawal(@RequestBody Transaction transaction) {
		String baseApiUrl = props.getApiUrl();
		String WithdrawalUrl = baseApiUrl + "/transactions/createwithdrawal";

		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<Transaction> request = new HttpEntity<Transaction>(transaction);
		ResponseEntity<Transaction> response = restTemplate.exchange(
				WithdrawalUrl, 
				HttpMethod.POST,
				request,
				Transaction.class);

		return response.getBody();
	}

}

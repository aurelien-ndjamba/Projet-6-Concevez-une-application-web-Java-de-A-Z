package com.paymybuddy.webapp.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
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

}

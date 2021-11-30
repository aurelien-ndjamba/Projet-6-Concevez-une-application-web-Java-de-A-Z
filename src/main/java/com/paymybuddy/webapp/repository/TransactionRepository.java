package com.paymybuddy.webapp.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.paymybuddy.webapp.CustomProperties;
import com.paymybuddy.webapp.model.Transaction;
import com.paymybuddy.webapp.model.AppUser;

@Component
public class TransactionRepository {
	
	@Autowired
	private CustomProperties props;
	
	public Iterable<Transaction> getTransactions() {
		String baseApiUrl = props.getApiUrl();
		String getUserUrl = baseApiUrl + "/transaction";

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Iterable<Transaction>> response = restTemplate.exchange(getUserUrl, HttpMethod.GET, null,
				new ParameterizedTypeReference<Iterable<Transaction>>() {
				});

//        log.debug("Get Employees call " + response.getStatusCode().toString());

		return response.getBody();
	}

}

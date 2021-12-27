package com.paymybuddy.webapp.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.paymybuddy.webapp.CustomProperties;
import com.paymybuddy.webapp.model.Account;

@Component
public class AccountProxy {

	@Autowired
	private CustomProperties props;

	public Account findByEmail(String email) {
		String baseApiUrl = props.getApiUrl();
		String apiUrl = baseApiUrl + "/accounts?email="+email;
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Account> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null, Account.class);
		return response.getBody();
	}

	public Account save(Account acc) {  
		String baseApiUrl = props.getApiUrl();
		String apiUrl = baseApiUrl + "/accounts/save";
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<Account> request = new HttpEntity<Account>(acc);
		ResponseEntity<Account> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, Account.class);
		return response.getBody();
	}

	public Account deleteById(int id) {
		String baseApiUrl = props.getApiUrl();
		String apiUrl = baseApiUrl + "/accounts/delete?id="+id;
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Account> response = restTemplate.exchange(apiUrl, HttpMethod.DELETE, null, Account.class);
		return response.getBody();
	}

}
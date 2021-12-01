package com.paymybuddy.webapp.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
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

	public List<Account> getAccountsByEmail(String email) {
		String baseApiUrl = props.getApiUrl();
		String getAccountsByEmailUrl = baseApiUrl + "/accounts?email=" + email;

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<Account>> response = restTemplate.exchange(
				getAccountsByEmailUrl, 
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<List<Account>>() {
				});

		return response.getBody();
	}
	
	public Account saveAccount(Account account) {
		String baseApiUrl = props.getApiUrl();
		String getAccountsByEmailUrl = baseApiUrl + "/accounts/save";

		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<Account> request = new HttpEntity<Account>(account);
		ResponseEntity<Account> response = restTemplate.exchange(
				getAccountsByEmailUrl, 
				HttpMethod.POST,
				request,
				new ParameterizedTypeReference<Account>() {
				});

		return response.getBody();
	}
	
	public Account deleteAccount(Integer id) {
		String baseApiUrl = props.getApiUrl();
		String getAccountsByEmailUrl = baseApiUrl + "/accounts/delete?id="+id;

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Account> response = restTemplate.exchange(
				getAccountsByEmailUrl, 
				HttpMethod.DELETE,
				null,
				new ParameterizedTypeReference<Account>() {
				});

		return response.getBody();
	}
	
}

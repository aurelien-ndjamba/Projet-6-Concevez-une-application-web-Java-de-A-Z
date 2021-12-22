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

	public Account findById(Integer id) {
		String baseApiUrl = props.getApiUrl();
		String apiUrl = baseApiUrl + "/accounts?id=" + id;
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Account> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null, Account.class);
		return response.getBody();
	}

	public List<Account> getAccountsByEmail(String email) {
		String baseApiUrl = props.getApiUrl();
		String apiUrl = baseApiUrl + "/accounts?email=" + email;
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<Account>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Account>>() {
				});
		return response.getBody();
	}

	public Account updateAccount(Account account) {
		String baseApiUrl = props.getApiUrl();
		String apiUrl = baseApiUrl + "/accounts/update";
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<Account> request = new HttpEntity<Account>(account);
		ResponseEntity<Account> response = restTemplate.exchange(apiUrl, HttpMethod.PUT, request, Account.class);
		return response.getBody();
	}

	public Account update(Account account) {
		String baseApiUrl = props.getApiUrl();  
		String apiUrl = baseApiUrl + "/accounts/update";
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<Account> request = new HttpEntity<Account>(account);
		ResponseEntity<Account> response = restTemplate.exchange(apiUrl, HttpMethod.PUT, request, Account.class);
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

	public Account findByEmail(String email) {
		String baseApiUrl = props.getApiUrl();
		String apiUrl = baseApiUrl + "/accounts?email="+email;
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Account> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null, Account.class);
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
package com.paymybuddy.webapp.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.paymybuddy.webapp.CustomProperties;
import com.paymybuddy.webapp.model.AppUser;

@Component
public class AppUserProxy {

	@Autowired
	private CustomProperties props;

	public Iterable<AppUser> getUsers() {
		String baseApiUrl = props.getApiUrl();
		String getUsersUrl = baseApiUrl + "/users/all";

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Iterable<AppUser>> response = restTemplate.exchange(getUsersUrl, HttpMethod.GET, null,
				new ParameterizedTypeReference<Iterable<AppUser>>() {
				});

		return response.getBody();
	}

	public AppUser getUser(String email) {
		String baseApiUrl = props.getApiUrl();
		String getAppUserUrl = baseApiUrl + "/users/user?email=" + email;

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<AppUser> response = restTemplate.exchange(getAppUserUrl, HttpMethod.GET, null, AppUser.class);

		return response.getBody();
	}
	
	public AppUser updatePassword(String email, String password) {
		String baseApiUrl = props.getApiUrl();
		String updatePasswordUrl = baseApiUrl + "/users/updatepassword?email=" + email + "&password=" + password;
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<AppUser> response = restTemplate.exchange(
				updatePasswordUrl, 
				HttpMethod.PUT, 
				null, 
				AppUser.class);
		
		return response.getBody();
	}
	
	public AppUser deleteAppUser(String email) {
		String baseApiUrl = props.getApiUrl();
		String updatePasswordUrl = baseApiUrl + "/users/delete?email=" + email;
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<AppUser> response = restTemplate.exchange(
				updatePasswordUrl, 
				HttpMethod.DELETE, 
				null, 
				AppUser.class);
		
		return response.getBody();
	}

}
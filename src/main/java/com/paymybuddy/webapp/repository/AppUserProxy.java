package com.paymybuddy.webapp.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
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

	public boolean login(AppUser appUser) {
		String baseApiUrl = props.getApiUrl();
		String apiUrl = baseApiUrl + "/login?email=" + appUser.getUsername() + "&password=" + appUser.getPassword();
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Boolean> response = restTemplate.exchange(apiUrl, HttpMethod.GET,
				null, boolean.class);
		return response.getBody();
	}

	public AppUser save(AppUser appUser) {
		String baseApiUrl = props.getApiUrl();
		String apiUrl = baseApiUrl + "/users/createuser";
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<AppUser> request = new HttpEntity<AppUser>(appUser);
		ResponseEntity<AppUser> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request,
				AppUser.class);
		return response.getBody();
	}

	public AppUser findByEmail(String email) {
		String baseApiUrl = props.getApiUrl();
		String apiUrl = baseApiUrl + "/users/user?email=" + email;
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<AppUser> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null, AppUser.class);
		return response.getBody();
	}

	public AppUser updatePassword(String email, String password) {
		String baseApiUrl = props.getApiUrl();
		String apiUrl = baseApiUrl + "/users/updatepassword?email=" + email + "&password=" + password;
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<AppUser> response = restTemplate.exchange(apiUrl, HttpMethod.PUT, null,
				AppUser.class);
		return response.getBody();
	}

	public AppUser updateAll(AppUser appUser) {
		String baseApiUrl = props.getApiUrl();
		String apiUrl = baseApiUrl + "/users/updateall";
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<AppUser> request = new HttpEntity<AppUser>(appUser);
		ResponseEntity<AppUser> response = restTemplate.exchange(apiUrl, HttpMethod.PUT, request,
				AppUser.class);
		return response.getBody();
	}

	public AppUser deleteAppUser(String email) {
		String baseApiUrl = props.getApiUrl();
		String apiUrl = baseApiUrl + "/users/delete?email=" + email;
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<AppUser> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
				AppUser.class);
		return response.getBody();
	}

	public AppUser updatePhone(String email, int phone) {
		String baseApiUrl = props.getApiUrl();
		String apiUrl = baseApiUrl + "/users/updatephone?email=" + email + "&phone=" + phone;
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<AppUser> response = restTemplate.exchange(apiUrl, HttpMethod.PUT, null,
				AppUser.class);
		return response.getBody();
	}
 
	public AppUser updateActive(String email, boolean active) {
		String baseApiUrl = props.getApiUrl();
		String apiUrl = baseApiUrl + "/users/updateactive?email=" + email + "&active=" + active;
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<AppUser> response = restTemplate.exchange(apiUrl, HttpMethod.PUT, null,
				AppUser.class);
		return response.getBody();
	}
}
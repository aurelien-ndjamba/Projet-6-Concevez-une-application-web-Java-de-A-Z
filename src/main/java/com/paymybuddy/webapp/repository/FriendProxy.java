package com.paymybuddy.webapp.repository;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.paymybuddy.webapp.CustomProperties;
import com.paymybuddy.webapp.model.Friend;


@Component
public class FriendProxy {

	@Autowired
	private CustomProperties props;

	/**
	 * Get all Users
	 * 
	 * @return An iterable of all users
	 */
	public HashSet<String> getFriendsOnly(String email) {
		String baseApiUrl = props.getApiUrl();
		String getFriendsOnlyUrl = baseApiUrl + "/friends/withme/list?email=" + email;

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<HashSet<String>> response = restTemplate.exchange(
				getFriendsOnlyUrl, 
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<HashSet<String>>() {
				});

		return response.getBody();
	}
	
	public Friend saveContact(Friend friend) {
		String baseApiUrl = props.getApiUrl();
		String saveFriendUrl = baseApiUrl + "/friends/save";

		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<Friend> request = new HttpEntity<Friend>(friend);
		ResponseEntity<Friend> response = restTemplate.exchange(
				saveFriendUrl, 
				HttpMethod.POST,
				request,
				Friend.class);

		return response.getBody();
	}
	
	public Friend deleteContact(String emailUser, String emailFriend) {
		String baseApiUrl = props.getApiUrl();
		String deleteFriendUrl = baseApiUrl + "/friends/delete?emailUser=" + emailUser + "&emailFriend=" + emailFriend;

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Friend> response = restTemplate.exchange(
				deleteFriendUrl, 
				HttpMethod.DELETE,
				null,
				Friend.class);

		return response.getBody();
	}

}
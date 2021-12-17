package com.paymybuddy.webapp.service;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import com.paymybuddy.webapp.model.Friend;
import com.paymybuddy.webapp.repository.FriendProxy;

@Service
public class FriendService {
	
	@Autowired
	private FriendProxy friendProxy;
	
    public HashSet<String> findPseudosFriendsOnly(String email) throws RuntimeException, Exception {
	    return friendProxy.findPseudosFriendsOnly(email);
	}

	public HashSet<String> findEmailsFriendsOnly(String email) {
		return friendProxy.findEmailsFriendsOnly(email);
	}

	public HashSet<String> findOtherEmailsFriends(String email) {
        return friendProxy.findOtherEmailsFriends(email);
    }
	
    public Friend saveFriend(Friend friend) {
        return friendProxy.saveContact(friend);
    }
    
    public Friend deletecontact(String emailUser, String pseudoFriend) {
        return friendProxy.deleteContact(emailUser,pseudoFriend);
    }

}

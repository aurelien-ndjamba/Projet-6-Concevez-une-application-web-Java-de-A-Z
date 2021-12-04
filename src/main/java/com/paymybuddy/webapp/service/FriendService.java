package com.paymybuddy.webapp.service;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.webapp.model.Friend;
import com.paymybuddy.webapp.repository.FriendProxy;

import lombok.Data;

@Data
@Service
public class FriendService {
	
	@Autowired
	private FriendProxy friendProxy;
	
    public HashSet<String> getAllOtherContactsExisting(String email) {
        return friendProxy.getAllOtherContactsExisting(email);
    }
	
    public HashSet<String> getFriendsOnly(String email) {
        return friendProxy.getFriendsOnly(email);
    }
    
    public Friend saveFriend(Friend friend) {
        return friendProxy.saveContact(friend);
    }
    
    public Friend deletecontact(String emailUser, String emailFriend) {
        return friendProxy.deleteContact(emailUser,emailFriend);
    }

}

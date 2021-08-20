package com.paymybuddy.api.service;

import java.util.ArrayList;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.api.model.Friend;
import com.paymybuddy.api.repository.FriendRepository;

@Service
public class FriendService {

	@Autowired
	private FriendRepository friendRepository;

	public Iterable<Friend> findAll() {
		return friendRepository.findAll();
	}

	public ArrayList<String> findByEmail(String email) {
		return friendRepository.findByEmail(email);
	}

	public HashSet<String> findOnlyMyFriends(String email) {
		HashSet<String> onlyFriends = new HashSet<String>();
		for (String f : friendRepository.findByEmail(email)) {
			if (f.startsWith(email + ",")) {
				onlyFriends.add(f.replace(email + ",", ""));
			} else if (f.endsWith("," + email)) {
				onlyFriends.add(f.replace("," + email, ""));
			}
		}
		return onlyFriends;
	}

	public boolean save(Friend friend) {

		boolean result = true;
		for (String f : friendRepository.findByEmail(friend.getEmailUser())) {
			if ((f.startsWith(friend.getEmailUser() + ",")) && (f.endsWith("," + friend.getEmailFriend()))
					|| (f.startsWith(friend.getEmailFriend() + ",")) && (f.endsWith("," + friend.getEmailUser())))
				result = false;
			else {
				friendRepository.save(friend);
			}
		}
		return result;
	}

	public boolean deleteFriends(String emailUser, String emailFriend) {
		boolean result = false;
		for (String f : friendRepository.findByEmail(emailUser)) {
			if ((f.startsWith(emailUser + ",")) && (f.endsWith("," + emailFriend))
					|| (f.startsWith(emailFriend + ",")) && (f.endsWith("," + emailUser))) {
				friendRepository.deleteFriends(emailUser, emailFriend);
				result = true;
				break;
			}
		}

		return result;
	}

}

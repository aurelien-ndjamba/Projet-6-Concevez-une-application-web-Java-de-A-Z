package com.paymybuddy.api.service;

import java.util.ArrayList;
import java.util.HashSet;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.api.model.Friend;
import com.paymybuddy.api.repository.FriendRepository;

@Service
public class FriendService {

	private Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private FriendRepository friendRepository;

	public void setFriendRepository(FriendRepository friendRepository) {
		this.friendRepository = friendRepository;
	}

	public ArrayList<String> findAllFriends() {
		return friendRepository.findAllFriends();
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

	public Friend save(Friend friend) {
		Friend result = new Friend();
		if ( ! (findOnlyMyFriends(friend.getEmailUser()).contains(friend.getEmailFriend())) ) {
			friendRepository.save(friend);
			logger.info(
					"INFO: Le nouvel ami a été ajouté dans vos contacts. Vous pouvez désormais effectuer des paiements avec lui !");
			result = friend;
		}
		return result;
	}

	public ArrayList<String> delete(String emailUser, String emailFriend) {
		ArrayList<String> result = new ArrayList<String>();
		for (String f : friendRepository.findByEmail(emailUser)) {
			if ((f.startsWith(emailUser + ",")) && (f.endsWith("," + emailFriend))
					|| (f.startsWith(emailFriend + ",")) && (f.endsWith("," + emailUser))) {
				result.add(emailUser);
				result.add(emailFriend);
				friendRepository.delete(emailUser, emailFriend);
				break;
			}
		}
		return result;
	}

}

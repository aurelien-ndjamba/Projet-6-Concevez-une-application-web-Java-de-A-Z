package com.paymybuddy.api.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paymybuddy.api.model.Friend;
import com.paymybuddy.api.repository.FriendRepository;
import com.paymybuddy.api.repository.UserRepository;

@Service
public class FriendService {

	@Autowired
	private FriendRepository friendRepository;
	@Autowired
	private UserRepository userRepository;

	public void setFriendRepository(FriendRepository friendRepository) {
		this.friendRepository = friendRepository;
	}
	
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public List<Friend> findAll() {
		return friendRepository.findAll();
	}

	@Transactional
	public List<Friend> findByEmail(String email) {
		if (!userRepository.existsById(email))
			throw new RuntimeException("Email non existant dans la BDD");
		String email2 = email;
		return friendRepository.findByEmailUserOrEmailFriend(email, email2);
	}

	@Transactional
	public HashSet<String> findFriendsOnly(String email) {
		if (!userRepository.existsById(email))
			throw new RuntimeException("Email non existant dans la BDD");
		HashSet<String> friendsOnly = new HashSet<>();
		for (Friend f : findByEmail(email)) {
			if (f.getEmailUser().equals(email)) 
				friendsOnly.add(f.getEmailFriend());
			else if (f.getEmailFriend().equals(email)) 
				friendsOnly.add(f.getEmailUser());	
		}
		return friendsOnly;
	}

	@Transactional
	public Friend save(Friend friend) {
		if (userRepository.findByEmail(friend.getEmailUser()) == null
				|| userRepository.findByEmail(friend.getEmailFriend()) == null)
			throw new RuntimeException("Email(s) 'ami' et/ou 'friend' non existant(s) dans la BDD");

		for (Friend f : friendRepository.findByEmailUserOrEmailFriend(friend.getEmailUser(), friend.getEmailFriend())) {
			if ((f.getEmailUser().equals(friend.getEmailUser()) && f.getEmailFriend().equals(friend.getEmailFriend()))
					|| (f.getEmailUser().equals(friend.getEmailFriend())
							&& f.getEmailFriend().equals(friend.getEmailUser())))
				throw new RuntimeException("Couple ami déjà existant dans la BDD");
		}
		friendRepository.save(friend);
		return friend;
	}

	@Transactional
	public Friend delete(String emailUser, String emailFriend) {
		if (userRepository.findByEmail(emailUser) == null || userRepository.findByEmail(emailFriend) == null)
			throw new RuntimeException("Email(s) 'ami' et/ou 'friend' non existant(s) dans la BDD");
		else if (friendRepository.findByEmailUserAndEmailFriend(emailUser, emailFriend) == null && friendRepository.findByEmailUserAndEmailFriend(emailFriend, emailUser) == null)
			throw new RuntimeException("Couple ami non existant dans la BDD");
		friendRepository.delete(emailUser, emailFriend);
		return new Friend(emailUser, emailFriend);
	}

}

package com.paymybuddy.api.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paymybuddy.api.model.AppUser;
import com.paymybuddy.api.model.Friend;
import com.paymybuddy.api.repository.FriendRepository;
import com.paymybuddy.api.repository.UserRepository;

@Service
public class FriendService {

	@Autowired
	private FriendRepository friendRepository;
	@Autowired
	private UserService appUserService;
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

	public List<Friend> findByEmail(String email, int page, int size) {
		if (!userRepository.existsById(email))
			throw new RuntimeException("Email non existant dans la BDD");
		String email2 = email;
		Pageable pagelist = PageRequest.of(page, size);
		return friendRepository.findByEmailUserOrEmailFriend(email, email2, pagelist);
	}
	
	@Transactional
	public HashSet<String> findEmailsFriendsOnly(String email) {
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
	public HashSet<String> findEmailsFriendsOnly(String email, int page, int size) {
		if (!userRepository.existsById(email))
			throw new RuntimeException("Email non existant dans la BDD");
		HashSet<String> friendsOnly = new HashSet<>();
		for (Friend f : findByEmail(email,page,size)) {
			if (f.getEmailUser().equals(email))
				friendsOnly.add(f.getEmailFriend());
			else if (f.getEmailFriend().equals(email))
				friendsOnly.add(f.getEmailUser());
		}
		return friendsOnly;
	}
	
	@Transactional
	public HashSet<String> findPseudosFriendsOnly(String email) {
		HashSet<String> PseudoFriendsOnly = new HashSet<>();
		HashSet<String> emailsFriendsOnly = findEmailsFriendsOnly(email);
		for (String emailFriend : emailsFriendsOnly) {
			PseudoFriendsOnly.add(findPseudoByEmail(emailFriend));
		}
		return PseudoFriendsOnly;
	}

	public HashSet<String> findPseudosFriendsOnly(String email, int page, int size) {
		HashSet<String> PseudoFriendsOnly = new HashSet<>();
		HashSet<String> emailsFriendsOnly = findEmailsFriendsOnly(email,page,size);
		for (String emailFriend : emailsFriendsOnly) {
			PseudoFriendsOnly.add(findPseudoByEmail(emailFriend));
		}
		return PseudoFriendsOnly;
	}

	@Transactional
	public HashSet<String> findEmailsavailable(String email) throws Exception {
		if (!userRepository.existsById(email))
			throw new Exception("Email non existant dans la BDD");
		HashSet<String> Emailsavailable = new HashSet<>();
		// Obternir la liste des emails de tous les utilisateurs de l'app sauf celui en param
		for (AppUser au : appUserService.findOtherUsersWithoutThisEmail(email)) {
			Emailsavailable.add(au.getEmail());
		}
		// Supprimer de cette liste les emails amis déjà existants avec l'email en param
		Emailsavailable.removeAll(findEmailsFriendsOnly(email));
		return Emailsavailable;
	}

	@Transactional
	public String findPseudoByEmail(String email) {
		String pseudo = userRepository.findById(email).get().getPseudo();
		return pseudo;
	}
	
	@Transactional
	public String findEmailByPseudo(String pseudo) {
		String email = userRepository.findByPseudo(pseudo).getEmail();
		return email;
	}

	@Transactional
	public Friend save(Friend friend) {
		if (!userRepository.existsById(friend.getEmailUser()) || !userRepository.existsById(friend.getEmailFriend()))
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
	public Friend deleteByEmailUserAndEmailFriend(String emailUser, String emailFriend) {
		if (!userRepository.existsById(emailUser) || !userRepository.existsById(emailFriend))
			throw new RuntimeException("Email(s) 'ami' et/ou 'friend' non existant(s) dans la BDD");
		else if (friendRepository.findByEmailUserAndEmailFriend(emailUser, emailFriend) == null
				&& friendRepository.findByEmailUserAndEmailFriend(emailFriend, emailUser) == null)
			throw new RuntimeException("Couple ami non existant dans la BDD");
		friendRepository.delete(emailUser, emailFriend);
		return new Friend(emailUser, emailFriend);
	}

	public Friend deleteByEmailUserAndPseudoFriend(String emailUser, String pseudoFriend) {
		String emailFriend = userRepository.findByPseudo(pseudoFriend).getEmail();
		friendRepository.delete(emailUser, emailFriend);
		return new Friend(emailUser, emailFriend);
	}

}

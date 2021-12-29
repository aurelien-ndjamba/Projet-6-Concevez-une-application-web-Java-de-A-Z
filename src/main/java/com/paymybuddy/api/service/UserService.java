package com.paymybuddy.api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paymybuddy.api.model.AppUser;
import com.paymybuddy.api.model.Role;
import com.paymybuddy.api.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private Role role;
	@Autowired
	private AppUser appUser;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private FriendService friendService;

	public void setbCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}



	public List<AppUser> findAll() {
		return userRepository.findAll();
	}

	@Transactional
	public AppUser findByEmail(String email) throws Exception {
		if (!userRepository.existsById(email))
			throw new Exception("Email de l'utilisateur non existant dans la BDD");
		AppUser appUser = userRepository.findByEmail(email);
		appUser.setUsername(appUser.getEmail());
		return  appUser;
	}

	@Transactional
	public List<AppUser> findOtherUsersWithoutThisEmail(String email) throws Exception {
		if (!userRepository.existsById(email))
			throw new Exception("Email de l'utilisateur non existant dans la BDD");
		List<AppUser> appUsers  = userRepository.findAll();
		appUsers.remove(userRepository.findByEmail(email));
		return appUsers;
	}

	@Transactional
	public ArrayList<String> findOtherEmailUsersWithoutThisEmail(String email) throws Exception {
		ArrayList<String> otherEmailUsersWithoutThisEmail = new ArrayList<>();
		for (AppUser au : findOtherUsersWithoutThisEmail(email)) {
			otherEmailUsersWithoutThisEmail.add(au.getEmail());
		}
		return otherEmailUsersWithoutThisEmail;
	}

	@Transactional
	public List<String> findOtherEmailsFriendsAvailableForThisEmail(String email) throws Exception {
		List<String> OtherEmailsFriendsAvailableForThisEmail = findOtherEmailUsersWithoutThisEmail(email);
		OtherEmailsFriendsAvailableForThisEmail.removeAll(friendService.findEmailsFriendsOnly(email));
		return OtherEmailsFriendsAvailableForThisEmail;
	}

	@Transactional
	public AppUser save(AppUser appUser) throws Exception {
		if (userRepository.existsById(appUser.getEmail())) {
			throw new Exception("Email de l'utilisateur déjà existant dans la BDD");
		}
		// Cryptage du mot de passe avec algorithme de hashage Bcrypt
		String passwordEncoder = bCryptPasswordEncoder.encode(appUser.getPassword());
		appUser.setPassword(passwordEncoder);
		// Tout nouvel utilisateur a le statut "USER" par défaut.
		role = new Role("USER");
		List<Role> roles = new ArrayList<>();
		roles.add(role);
		appUser.setRoles(roles);
//		appUser.setActive(true); // fixer active à TRUE à l'inscription
//		accountService.save(appUser.getAccount()); //Sauvegarde du compte User.account avant la sauvegarde du User
		return userRepository.save(appUser);
	}

	@Transactional
	public AppUser updateAll(AppUser au) throws Exception {
		if (au.getEmail() == null)
			throw new Exception(
					"Vous devez renseigner l'email de l'utilisateur pour la mise à jour dans la BDD");
		else if (au.getPassword() == null)
			throw new Exception("Vous devez renseigner le nouveau mot de passe à mettre à jour dans la BDD");
		else if (au.getBalance() == null)
			throw new Exception("Vous devez renseigner le nouveau solde à mettre à jour dans la BDD");
		else if (!userRepository.existsById(au.getEmail()))
			throw new Exception("Email de l'utilisateur non existant dans la BDD");
		appUser = userRepository.findById(au.getEmail()).get();
		String passwordEncoder = bCryptPasswordEncoder.encode(au.getPassword());
		appUser.setPassword(passwordEncoder);
		appUser.setBalance(au.getBalance());
		return userRepository.save(appUser);
	}

	@Transactional
	public AppUser updatePassword(String email, String password) {
		appUser = userRepository.getById(email);
		String passwordEncoder = bCryptPasswordEncoder.encode(password);
		appUser.setPassword(passwordEncoder);
		return userRepository.save(appUser);
	}

	@Transactional
	public AppUser updateBalance(String email, Double balance) throws Exception {
		if (email == null)
			throw new Exception(
					"Vous devez renseigner l'email de l'utilisateur pour la mise à jour dans la BDD");
		else if (balance == null)
			throw new Exception("Vous devez renseigner le nouveau solde à mettre à jour dans la BDD");
		else if (!userRepository.existsById(email))
			throw new Exception("Email de l'utilisateur non existant dans la BDD");

		appUser = userRepository.getById(email);
		appUser.setBalance(balance);
		return userRepository.save(appUser);
	}

	public boolean login(String email, String password) throws Exception {
		boolean result = false;
		if (!userRepository.existsById(email)) {
			throw new Exception("Email de l'utilisateur non existant dans la BDD");
		} else if (bCryptPasswordEncoder.matches(password, userRepository.findById(email).get().getPassword())) {
			result = true;
			updateActive(email, result);
		}
		return result;
	}

	@Transactional
	public AppUser updateActive(String email, boolean active) throws Exception {
		if (email == null)
			throw new Exception(
					"Vous devez renseigner l'email de l'utilisateur pour la mise à jour dans la BDD");
		else if (!userRepository.existsById(email))
			throw new Exception("Email de l'utilisateur non existant dans la BDD");

		appUser = userRepository.getById(email);
		appUser.setActive(active);
		return userRepository.save(appUser);
	}

	@Transactional
	public AppUser updatePhone(String email, int phone) throws Exception {
		if (email == null)
			throw new Exception(
					"Vous devez renseigner l'email de l'utilisateur pour la mise à jour dans la BDD");
		else if (!userRepository.existsById(email))
			throw new Exception("Email de l'utilisateur non existant dans la BDD");
		appUser = userRepository.getById(email);
		appUser.setPhone(phone);
		return userRepository.save(appUser);
	}

	@Transactional
	public AppUser deleteById(String email) throws Exception {
		AppUser result = new AppUser();
		if (!userRepository.existsById(email)) {
			throw new Exception("Email de l'utilisateur non existant dans la BDD");
		} else {
			result = userRepository.findByEmail(email);
			userRepository.deleteById(email);
		}
		return result;
	}

}
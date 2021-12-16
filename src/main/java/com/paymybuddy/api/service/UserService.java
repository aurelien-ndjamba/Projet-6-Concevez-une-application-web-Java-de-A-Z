package com.paymybuddy.api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paymybuddy.api.model.AppUser;
import com.paymybuddy.api.model.Role;
import com.paymybuddy.api.repository.UserRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public boolean login(String email, String password) {
		boolean result = false;
		if (!userRepository.existsById(email)) {
			throw new RuntimeException("Email de l'utilisateur non existant dans la BDD");
		} else if (bCryptPasswordEncoder.matches(password,
				userRepository.findById(email).get().getPassword())) {
			result = true;
			updateActive(email, result);
		}
		return result;
	}

	public List<AppUser> findAll() {
		return userRepository.findAll();
	}

	@Transactional
	public AppUser findByEmail(String email) {
		if (!userRepository.existsById(email))
			throw new RuntimeException("Email de l'utilisateur non existant dans la BDD");
		return userRepository.findByEmail(email);
	}

	@Transactional
	public List<AppUser> findOtherUsersWithoutThisEmail(String email) {
		if (!userRepository.existsById(email))
			throw new RuntimeException("Email de l'utilisateur non existant dans la BDD");
		List<AppUser> appUsers = userRepository.findAll();
		appUsers.remove(userRepository.findByEmail(email));
		return appUsers;
	}

	@Transactional
	public AppUser save(AppUser appUser) {
		if (userRepository.existsById(appUser.getEmail())) {
			throw new RuntimeException("Email de l'utilisateur déjà existant dans la BDD");
		}
		// Cryptage du mot de passe avec algorithme de hashage Bcrypt
		String passwordEncoder = bCryptPasswordEncoder.encode(appUser.getPassword());
		appUser.setPassword(passwordEncoder);
		// Balance à 0.00 à l'enregistrement d'un nouvel utilisateur
		appUser.setBalance(0.00);
		// Tout nouvel utilisateur a le statut "USER" par défaut.
		role = new Role("USER");
		List<Role> roles = new ArrayList<>();
		roles.add(role);
		appUser.setRoles(roles);
		appUser.setActive(true); // fixer active à TRUE à l'inscription
//		accountService.save(appUser.getAccount()); //Sauvegarde du compte User.account avant la sauvegarde du User
		System.out.println(appUser);
		return userRepository.save(appUser);
	}

	@Transactional
	public AppUser updateAll(AppUser au) {
		if (au.getEmail() == null)
			throw new RuntimeException(
					"Vous devez renseigner l'email de l'utilisateur pour la mise à jour dans la BDD");
		else if (au.getPassword() == null)
			throw new RuntimeException("Vous devez renseigner le nouveau mot de passe à mettre à jour dans la BDD");
		else if (au.getBalance() == null)
			throw new RuntimeException("Vous devez renseigner le nouveau solde à mettre à jour dans la BDD");
		else if (!userRepository.existsById(au.getEmail()))
			throw new RuntimeException("Email de l'utilisateur non existant dans la BDD");
		appUser = userRepository.findById(au.getEmail()).get();
		String passwordEncoder = bCryptPasswordEncoder.encode(au.getPassword());
		appUser.setPassword(passwordEncoder);
		appUser.setBalance(au.getBalance());
		return userRepository.save(appUser);
	}

	@Transactional
	public AppUser updatePassword(String email, String password) {
//		if (email == null)
//			throw new RuntimeException(
//					"Vous devez renseigner l'email de l'utilisateur pour la mise à jour dans la BDD");
//		else if (password == null)
//			throw new RuntimeException("Vous devez renseigner le nouveau mot de passe à mettre à jour dans la BDD");
//		else if (!userRepository.existsById(email))
//			throw new RuntimeException("Email de l'utilisateur non existant dans la BDD");
		appUser = userRepository.getById(email);
		String passwordEncoder = bCryptPasswordEncoder.encode(password);
		appUser.setPassword(passwordEncoder);
		return userRepository.save(appUser);
	}

	@Transactional
	public AppUser updateBalance(String email, Double balance) {
		if (email == null)
			throw new RuntimeException(
					"Vous devez renseigner l'email de l'utilisateur pour la mise à jour dans la BDD");
		else if (balance == null)
			throw new RuntimeException("Vous devez renseigner le nouveau solde à mettre à jour dans la BDD");
		else if (!userRepository.existsById(email))
			throw new RuntimeException("Email de l'utilisateur non existant dans la BDD");

		appUser = userRepository.getById(email);
		appUser.setBalance(balance);
		return userRepository.save(appUser);
	}
	
	@Transactional
	public AppUser updateActive(String email, boolean status) {
		if (email == null)
			throw new RuntimeException(
					"Vous devez renseigner l'email de l'utilisateur pour la mise à jour dans la BDD");
		else if (!userRepository.existsById(email))
			throw new RuntimeException("Email de l'utilisateur non existant dans la BDD");

		appUser = userRepository.getById(email);
		appUser.setActive(status);
		return userRepository.save(appUser);
	}
	
	@Transactional
	public AppUser updatePseudo(String email, String pseudo) {
		if (email == null)
			throw new RuntimeException(
					"Vous devez renseigner l'email de l'utilisateur pour la mise à jour dans la BDD");
		else if (!userRepository.existsById(email))
			throw new RuntimeException("Email de l'utilisateur non existant dans la BDD");

		appUser = userRepository.getById(email);
		appUser.setPseudo(pseudo);
		return userRepository.save(appUser);
	}

	@Transactional
	public AppUser deleteById(String email) {
		AppUser result = new AppUser();
		if (!userRepository.existsById(email)) {
			throw new RuntimeException("Email de l'utilisateur non existant dans la BDD");
		} else {
			result = userRepository.findByEmail(email);
			userRepository.deleteById(email);
		}
		return result;
	}

}
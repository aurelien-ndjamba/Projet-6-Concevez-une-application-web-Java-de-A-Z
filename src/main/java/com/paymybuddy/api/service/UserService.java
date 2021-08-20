package com.paymybuddy.api.service;

import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.api.model.User;
import com.paymybuddy.api.repository.UserRepository;

//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class UserService {
	
	private Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private User user;
	@Autowired
	private UserRepository userRepository;
	
	public List<String> findEmailAndBalance() {
		return userRepository.findEmailAndBalance();
	}
	
	public List<String> findByEmail(String email) {
		if (! userRepository.existsById(email)) 
			logger.error("ERROR: L'email renseigné : " + email + " n'existe pas dans la BDD de l'application");
		 return userRepository.findByEmail(email);
	}
	
	public List<String> findByEmailNot(String withoutemail) {
		return userRepository.findByEmailNot(withoutemail);
	}

	public boolean save(User user) {
		boolean result = false;
		if (userRepository.existsById(user.getEmail())) {
			logger.error("ERROR: L'email de l'utilisateur : " + user.getEmail() + " existe déjà la BDD de l'application - Utilisateur non sauvegardé");
		}
		else {
//			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
//			String result = encoder.encode(user.getPassword());
//			user.setPassword(result);
			userRepository.save(user);
			result = true;
		}
		return result;
	}
	
	public boolean updatePassword(String email, String password) {
		boolean result = false;
		if (! userRepository.existsById(email)) {
			logger.error("ERROR: L'email de l'utilisateur : " + email + " n'existe pas dans la BDD de l'application - Utilisateur non update");
		}
		else {
//			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
//			String result = encoder.encode(password);
			user = userRepository.getById(email);
			user.setPassword(password); // Supprimer quand Bcrypt opérationnel
//			user.setPassword(result);
			userRepository.save(user);
			result = true;
		}
		return result;
	}
	
	public boolean updateBalance(String email, double newBalance) {
		boolean result = false;
		if (! userRepository.existsById(email)) {
			logger.error("ERROR: L'email de l'utilisateur : " + email + " n'existe pas dans la BDD de l'application - Utilisateur non update");
		}
		else {
			user = userRepository.getById(email);
			user.setBalance(newBalance); 
			userRepository.save(user);
			result = true;
		}
		return result;
	}

	public boolean deleteByEmail(String email) {
		boolean result = false;
		if (! userRepository.existsById(email)) {
			logger.error("ERROR: L'email de l'utilisateur : " + email + " n'existe pas dans la BDD de l'application - Utilisateur non supprimé");
		
		}
		else {
			userRepository.deleteAllByEmail(email);
			result = true;
		}
		
		return result;
	}

}
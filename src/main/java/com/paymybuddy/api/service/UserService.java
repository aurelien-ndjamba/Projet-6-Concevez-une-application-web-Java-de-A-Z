package com.paymybuddy.api.service;

import java.util.List;
import java.util.Optional;

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
	
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	public User getIdentification(String email, String password) {
		User result = new User();
		if (userRepository.existsById(email) && userRepository.findByEmail(email).get().getPassword().equals(password)) {
			logger.info("INFO: Bienvenue ! Vous êtes connecté à l'application.");
			result = userRepository.findByEmail(email).get();
		}
		return result;
	}

	public List<String> findEmailsAndBalances() {
		return userRepository.findEmailsAndBalances();
	}

	public List<String> findEmailAndBalanceByEmail(String email) {
		if (!userRepository.existsById(email))
			logger.error("ERROR: L'email renseigné : " + email + " n'existe pas dans la BDD de l'application");
		return userRepository.findEmailAndBalanceByEmail(email);
	}

	public List<String> findByEmailNot(String withoutemail) {
		return userRepository.findByEmailNot(withoutemail);
	}

	public User save(User user) {
		User result = new User();
		if (! userRepository.existsById(user.getEmail())) {
//			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
//			String result = encoder.encode(user.getPassword());
//			user.setPassword(result);
			result = user;
			userRepository.save(user);
		}
		return result;
	}

	public User update(User user) {
		User result = new User();
		if ( userRepository.existsById(user.getEmail())) {
//			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
//			String result = encoder.encode(user.getPassword());
//			user.setPassword(result);
			result = user;
			userRepository.save(user);
		} 
		
		return result;
	}

	public boolean updateBalance(String email, double newBalance) {
		boolean result = false;
		if (userRepository.existsById(email)) {
			user = userRepository.getById(email);
			user.setBalance(newBalance);
			userRepository.save(user);
			result = true;
		} 
		return result;
	}

	public User deleteById(String email) {
		User result = new User();
		if (userRepository.existsById(email)) {
			result = userRepository.getById(email);
			userRepository.deleteById(email);
		} 
		return result;
	}

}
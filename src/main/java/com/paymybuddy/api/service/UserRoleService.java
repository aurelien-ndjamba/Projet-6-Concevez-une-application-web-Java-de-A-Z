package com.paymybuddy.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paymybuddy.api.model.UserRole;
import com.paymybuddy.api.model.UserRoleId;
import com.paymybuddy.api.repository.RoleRepository;
import com.paymybuddy.api.repository.UserRepository;
import com.paymybuddy.api.repository.UserRoleRepository;

@Service
public class UserRoleService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserRoleRepository userRoleRepository;
	@Autowired
	private RoleRepository roleRepository;

	public void setUserRoleRepository(UserRoleRepository userRoleRepository) {
		this.userRoleRepository = userRoleRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public void setRoleRepository(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	public List<UserRole> findAll() {
		return userRoleRepository.findAll();
	}

	@Transactional
	public List<UserRole> findByEmail(String email) throws Exception {
		if (!userRepository.existsById(email))
			throw new Exception("Email non existant dans la BDD");
		return userRoleRepository.findByEmail(email);
	}

	@Transactional
	public UserRole save(UserRole userRole) throws Exception {
		if (userRole.getEmail() == null)
			throw new Exception("Vous devez renseigner l'email.");
		else if (userRole.getRoleName() == null)
			throw new Exception("Vous devez renseigner le role.");
		else if (!userRepository.existsById(userRole.getEmail()))
			throw new Exception("L'email renseigné n'existe pas dans la BDD");
		else if (userRoleRepository.existsById(new UserRoleId(userRole.getEmail(), userRole.getRoleName())))
			throw new Exception("Relation UserRole déjà existante dans la BDD");
		else if (!roleRepository.existsById(userRole.getRoleName()))
			throw new Exception("Role inexistant dans la BDD");
		return userRoleRepository.save(userRole);
	}

	@Transactional
	public UserRole delete(String email, String role) throws Exception {
		if (!userRepository.existsById(email))
			throw new Exception("L'email renseigné n'existe pas dans la BDD");
		else if (!roleRepository.existsById(role))
			throw new Exception("Le role renseigné n'existe pas dans la BDD");
		else if (!userRoleRepository.existsById(new UserRoleId(email, role)))
			throw new Exception("Relation UserRole non existante dans la BDD");
		userRoleRepository.deleteById(new UserRoleId(email, role));
		return new UserRole(email, role);
	}

}
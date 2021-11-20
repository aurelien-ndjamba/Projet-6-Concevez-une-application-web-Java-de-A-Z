package com.paymybuddy.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paymybuddy.api.model.Role;
import com.paymybuddy.api.repository.RoleRepository;

@Service
public class RoleService {

	@Autowired
	private RoleRepository roleRepository;

	public void setRoleRepository(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	public List<Role> findAll() {
		return roleRepository.findAll();
	}

	@Transactional
	public Role findById(String id) {
		if (! roleRepository.existsById(id))
			throw new RuntimeException("Role non existant dans la BDD");
		return roleRepository.findById(id).get();
	}

	@Transactional
	public Role save(Role role) {
		if (roleRepository.existsById(role.getRoleName()))
			throw new RuntimeException("Role déjà existant dans la BDD");
		return roleRepository.save(role);
	}

	@Transactional
	public Role deleteById(String id) {
		if (! roleRepository.existsById(id))
			throw new RuntimeException("Role non existant dans la BDD");
		Role roleDeleted = roleRepository.findById(id).get();
		roleRepository.deleteById(id);
		return roleDeleted;
	}
}
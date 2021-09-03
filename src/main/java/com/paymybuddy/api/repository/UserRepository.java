package com.paymybuddy.api.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.api.model.AppUser;

@Repository
public interface UserRepository extends JpaRepository<AppUser, String> {
	
	AppUser findByEmail(String email);
	
	@Transactional
	void deleteAllByEmail(String email);

}
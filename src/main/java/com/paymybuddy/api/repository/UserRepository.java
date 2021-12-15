package com.paymybuddy.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.api.model.AppUser;
import com.paymybuddy.api.model.Friend;

@Repository
public interface UserRepository extends JpaRepository<AppUser, String> {
	
	AppUser findByEmail(String email);
	AppUser findByPseudo(String email);
}
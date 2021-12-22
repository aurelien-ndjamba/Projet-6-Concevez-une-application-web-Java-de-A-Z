package com.annotationJpa.OneToOne;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, String> {
	
	AppUser findByEmail(String email);
	AppUser findByPseudo(String email);
}
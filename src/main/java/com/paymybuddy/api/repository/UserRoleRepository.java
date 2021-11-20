package com.paymybuddy.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.api.model.UserRole;
import com.paymybuddy.api.model.UserRoleId;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId>{

	List<UserRole> findByEmail(String email);

}

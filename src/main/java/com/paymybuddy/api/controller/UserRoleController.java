package com.paymybuddy.api.controller;

import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.paymybuddy.api.model.UserRole;
import com.paymybuddy.api.service.UserRoleService;

@RestController
public class UserRoleController {

	private Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private UserRoleService userRoleService;

	public void setUserRoleService(UserRoleService userRoleService) {
		this.userRoleService = userRoleService;
	}

	/* *************** GET METHODE *********************** */
	
	/**
	 * GET http://localhost:8080/roles/all
	 * 
	 * Liste des roles de l'application
	 * 
	 * @return List<UserRole>
	 * 
	 */
	@Secured(value={"ADMIN"})
	@GetMapping("/userroles/all")
	public List<UserRole> findAll() {
		logger.info("INFO: Liste des roles de l'application");
		return userRoleService.findAll();
	}
	
	/**
	 * GET http://localhost:8080/roles?roleName=USER
	 * 
	 * Obtenir un role par id
	 * 
	 * @return UserRole
	 * 
	 */
	@Secured(value={"ROLE_ADMIN"})
	@RequestMapping(value = "/userroles", method = RequestMethod.GET, params = { "email"})
	public List<UserRole> findByEmail(String email) {
		logger.info("INFO: Obtenir un role par roleName");
		return userRoleService.findByEmail(email);
	}
	
	/* *************** POST METHODE *********************** */

	/**
	 * POST http://localhost:8080/roles/save
	 * 
	 * Ajouter un nouvel role dans l'application
	 * 
	 * @return UserRole
	 * 
	 */
	@Secured(value={"ROLE_ADMIN"})
	@PostMapping("/userroles/save")
	@ResponseStatus(HttpStatus.CREATED)
	public UserRole save(@RequestBody UserRole userRole) {
		logger.info("INFO: Ajouter un nouvel role dans l'application");
		return userRoleService.save(userRole);
	}
	
	/* *************** DELETE METHODE *********************** */

	/**
	 * DELETE http://localhost:8080/roles/delete
	 * 
	 * Ajouter un nouvel role dans l'application
	 * 
	 * @return UserRole
	 * 
	 */
	@Secured(value={"ROLE_ADMIN"})
	@RequestMapping(value = "/userroles/delete", method = RequestMethod.DELETE, params = { "email", "role" })
	public UserRole delete(String email, String role) {
		logger.info("INFO: Ajouter un nouvel role dans l'application");
		return userRoleService.delete(email,role);
	}
	
}
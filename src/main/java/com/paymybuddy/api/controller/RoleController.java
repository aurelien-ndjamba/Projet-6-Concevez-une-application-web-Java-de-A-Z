package com.paymybuddy.api.controller;

import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.paymybuddy.api.model.Role;
import com.paymybuddy.api.service.RoleService;

@RestController
public class RoleController {

	private Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private RoleService roleService;

	/* *************** POST METHODE *********************** */
	
	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	/* *************** GET METHODE *********************** */

	/**
	 * GET http://localhost:8080/roles/all
	 * 
	 * Liste des roles de l'application
	 * 
	 * @return List<Role>
	 * 
	 */
	@GetMapping("/roles/all")
	public List<Role> findAll() {
		logger.info("INFO: Liste des roles de l'application");
		return roleService.findAll();
	}
	
	/**
	 * GET http://localhost:8080/roles?roleName=USER
	 * 
	 * Obtenir un role par id
	 * 
	 * @return Role
	 * 
	 */
	@GetMapping("/roles")
	public Role findById(String id) {
		logger.info("INFO: Obtenir un role par roleName");
		return roleService.findById(id);
	}
	
	/**
	 * POST http://localhost:8080/roles/save
	 * 
	 * Ajouter un nouvel role dans l'application
	 * 
	 * @return Role
	 * 
	 */
	@PostMapping("/roles/save")
	@ResponseStatus(HttpStatus.CREATED)
	public Role save(@RequestBody Role role) {
		logger.info("INFO: Ajouter un nouvel role dans l'application");
		return roleService.save(role);
	}
	
	/* *************** DELETE METHODE *********************** */

	/**
	 * DELETE http://localhost:8080/roles/delete?id=USER
	 * 
	 * Supprimer un role dans l'application
	 * 
	 * @return Role
	 * 
	 */
	@RequestMapping(value = "/roles/delete", method = RequestMethod.DELETE, params = { "id" })
	public Role deleteById(String id) {
		logger.info("INFO: Supprimer un role dans l'application");
		return roleService.deleteById(id);
	}
	
}
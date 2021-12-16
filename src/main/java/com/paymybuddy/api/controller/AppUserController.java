package com.paymybuddy.api.controller;

import java.util.List;

import org.jboss.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.paymybuddy.api.model.AppUser;
import com.paymybuddy.api.service.UserService;


@RestController
public class AppUserController {
	
	private Logger logger = Logger.getLogger(this.getClass());

	@Autowired private UserService userService;
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/* *************** GET METHODE *********************** */
	
	@Secured(value={"ROLE_ADMIN","ROLE_USER"})
	@GetMapping("/login")
	public boolean login(String email, String password) {
		logger.info("INFO: Vérifer si cet utilisateur est enregistré en BDD");
		return userService.login(email, password);
	}
	
	@GetMapping("/test")
	public AppUser test(String email, boolean status) {
		return userService.updateActive(email, status);
	}
	
	/** 
	 * GET	http://localhost:8080/users
	 * 
	 * Liste des infos de tous les utilisateurs de l'application
	 * 
	 * @return	List<AppUser>
	 * 
	 */
	@Secured(value={"ROLE_ADMIN"})
	@GetMapping("/users/all")
	public List<AppUser> findAll() {
		logger.info("INFO: Liste tous les utilisateurs de l'application sans leur mot de passe");
		return userService.findAll();
	}
	
	/**
	 * GET http://localhost:8080/users/user?email=francois.hollande@gmail.com
	 * 
	 * Liste des infos d'un utilisateur ayant l'email donné en parametre
	 * 
	 * @return AppUser
	 * 
	 */
	@Secured(value={"ROLE_ADMIN"})
	@RequestMapping(value = "/users/user", method = RequestMethod.GET, params = { "email" })
	public AppUser findByEmail(String email) {
		logger.info("INFO: Liste des infos d'un utilisateur ayant l'email donné en parametre.");
		return userService.findByEmail(email);
	}
	
	/**
	 * GET http://localhost:8080/withoutemail?email=francois.hollande@gmail.com
	 * 
	 * Liste des infos de tous les utilisateurs de l'application sauf celui dont l'email est en parametre
	 * 
	 * @return List<String>
	 * 
	 */
	@Secured(value={"ROLE_ADMIN"})
	@RequestMapping(value = "/users/otherusers", method = RequestMethod.GET, params = { "email" })
	public List<AppUser> findOtherUsersWithoutThisEmail(String email){
		logger.info("INFO: Liste des infos de tous les utilisateurs de l'application sauf celui dont l'email est en parametre.");
		return userService.findOtherUsersWithoutThisEmail(email);
	}
	
	/* *************** POST METHODE *********************** */

	/**
	 * POST http://localhost:8080/users/createuser
	 * 
	 * Creation d'un nouvel user dans la base de donnée
	 * 
	 * @return AppUser
	 * 
	 */
	@Secured(value={"ROLE_ADMIN"})
	@PostMapping("/users/createuser")
	@ResponseStatus(HttpStatus.CREATED)
	public AppUser save(@RequestBody AppUser appUser) {
		logger.info("INFO: Creation d'un nouvel user dans la base de donnée.");
		return userService.save(appUser);
	}
	
	/* *************** PUT METHODE *********************** */

	/**
	 * PUT http://localhost:8080/account
	 * 
	 * Mettre à jour les informations d'un user dans la base de donnée sauf son email
	 * 
	 * @return AppUser
	 * 
	 */
	@Secured(value={"ROLE_ADMIN"})
	@PutMapping("/users/updateall")
	public AppUser updateAll(@RequestBody AppUser appUser) {
		logger.info("Update le password et/ou solde d'un utilisateur dans la BDD");
		return userService.updateAll(appUser);
	}
	
	@Secured(value={"ROLE_ADMIN"})
	@PutMapping("/users/updatepassword")
	public AppUser updatePassword(String email, String password) {
		logger.info("Update le password uniquement dans la BDD");
		return userService.updatePassword(email, password);
	}
	
	@Secured(value={"ROLE_ADMIN"})
	@PutMapping("/users/updatebalance")
	public AppUser updateBalance(String email, Double balance) {
		logger.info("Update le solde d'un utilisateur dans la BDD");
		return userService.updateBalance(email, balance);
	}
	
	/* *************** DELETE METHODE *********************** */

	/**
	 * DELETE http://localhost:8080/account?id=77
	 * 
	 * Supprime un user dans la base de donnée à partir de l'email en paramètre
	 * 
	 * @return String
	 * 
	 */
	@Secured(value={"ROLE_ADMIN","ROLE_USER"}) 
	@RequestMapping(value="/users/delete", method = RequestMethod.DELETE, params = { "email" })
	public AppUser deleteById(String email) {
		logger.info("INFO: Supprimer dans la BDD un user dont l'email est donné");
		return userService.deleteById(email);
	}
	
}
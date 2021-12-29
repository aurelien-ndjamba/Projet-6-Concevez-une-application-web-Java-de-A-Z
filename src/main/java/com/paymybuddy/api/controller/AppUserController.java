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

	@Autowired
	private UserService userService;

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/* *************** GET METHODE *********************** */
	/**
	 * 
	 * Vérifer si cet utilisateur est enregistré en BDD
	 * 
	 * @return boolean
	 * 
	 */
	@Secured(value = { "ROLE_ADMIN", "ROLE_USER" })
	@RequestMapping(value = "/login", method = RequestMethod.GET, params = {"email","password"})
	public boolean login(String email, String password) throws Exception {
		logger.info("INFO: Vérifer si cet utilisateur est enregistré en BDD");
		return userService.login(email, password);
	}

	/**
	 * 
	 * Liste des infos de tous les utilisateurs de l'application
	 * 
	 * @return List<AppUser>
	 * 
	 */
	@Secured(value = { "ROLE_ADMIN" })
	@GetMapping("/users")
	public List<AppUser> findAll() {
		logger.info("INFO: Liste des infos de tous les utilisateurs de l'application");
		return userService.findAll();
	}

	/**
	 * 
	 * Liste des infos d'un utilisateur ayant l'email donné en parametre
	 * 
	 * @return AppUser
	 * @throws Exception
	 * 
	 */
	@Secured(value = { "ROLE_ADMIN", "ROLE_USER" })
	@RequestMapping(value = "/users", method = RequestMethod.GET, params = { "email" })
	public AppUser findByEmail(String email) throws Exception {
		logger.info("INFO: Liste des infos d'un utilisateur ayant l'email donné en parametre.");
		return userService.findByEmail(email);
	}

	/**
	 * 
	 * Liste des infos de tous les utilisateurs de l'application sauf celui dont
	 * l'email est en parametre
	 * 
	 * @return List<AppUser>
	 * @throws Exception
	 * 
	 */
	@Secured(value = { "ROLE_ADMIN", "ROLE_USER" })
	@RequestMapping(value = "/users/otherusers", method = RequestMethod.GET, params = { "email" })
	public List<AppUser> findOtherUsersWithoutThisEmail(String email) throws Exception {
		logger.info(
				"INFO: Liste des infos de tous les utilisateurs de l'application sauf celui dont l'email est en parametre.");
		return userService.findOtherUsersWithoutThisEmail(email);
	}

	/**
	 * 
	 * Liste les emails de tous les utilisateurs de l'application sauf celui dont
	 * l'email est en parametre.
	 * 
	 * @return List<String>
	 * @throws Exception
	 * 
	 */
	@Secured(value = { "ROLE_ADMIN", "ROLE_USER" })
	@RequestMapping(value = "/users/otheremailusers", method = RequestMethod.GET, params = { "email" })
	public List<String> findOtherEmailUsersWithoutThisEmail(String email) throws Exception {
		logger.info(
				"INFO: Liste les emails de tous les utilisateurs de l'application sauf celui dont l'email est en parametre.");
		return userService.findOtherEmailUsersWithoutThisEmail(email);
	}

	/**
	 * 
	 * Liste des emails de tous les utilisateurs disponibles pour une amitié avec
	 * celui dont l'email est en parametre.
	 * 
	 * @return List<String>
	 * @throws Exception
	 * 
	 */
	@Secured(value = { "ROLE_ADMIN", "ROLE_USER" })
	@RequestMapping(value = "/users/otheremailsfriendsavailable", method = RequestMethod.GET, params = { "email" })
	public List<String> findOtherEmailsFriendsAvailableForThisEmail(String email) throws Exception {
		logger.info(
				"INFO: Liste des emails de tous les utilisateurs disponibles pour une amitié avec celui dont l'email est en parametre.");
		return userService.findOtherEmailsFriendsAvailableForThisEmail(email);
	}

	/* *************** POST METHODE *********************** */

	/**
	 * 
	 * Creation d'un nouvel user dans la base de donnée
	 * 
	 * @return AppUser
	 * @throws Exception
	 * 
	 */
	@Secured(value = { "ROLE_ADMIN" })
	@PostMapping("/users/createuser")
	@ResponseStatus(HttpStatus.CREATED)
	public AppUser save(@RequestBody AppUser appUser) throws Exception {
		logger.info("INFO: Creation d'un nouvel user dans la base de donnée.");
		return userService.save(appUser);
	}

	/* *************** PUT METHODE *********************** */

	/**
	 * 
	 * Update le password et/ou solde d'un utilisateur dans la BDD
	 * 
	 * @return AppUser
	 * @throws Exception
	 * 
	 */
	@Secured(value = { "ROLE_ADMIN" })
	@PutMapping("/users/updateall")
	public AppUser updateAll(@RequestBody AppUser appUser) throws Exception {
		logger.info("Update le password et/ou solde d'un utilisateur dans la BDD");
		return userService.updateAll(appUser);
	}

	/**
	 * 
	 * Update le password uniquement dans la BDD
	 * 
	 * @return AppUser
	 * @throws Exception
	 * 
	 */
	@Secured(value = { "ROLE_ADMIN", "ROLE_USER" })
	@PutMapping("/users/updatepassword")
	public AppUser updatePassword(String email, String password) {
		logger.info("Update le password uniquement dans la BDD");
		return userService.updatePassword(email, password);
	}

	/**
	 * 
	 * Update le solde d'un utilisateur dans la BDD
	 * 
	 * @return AppUser
	 * @throws Exception
	 * 
	 */
	@Secured(value = { "ROLE_ADMIN" })
	@PutMapping("/users/updatebalance")
	public AppUser updateBalance(String email, Double balance) throws Exception {
		logger.info("Update le solde d'un utilisateur dans la BDD");
		return userService.updateBalance(email, balance);
	}

	/**
	 * 
	 * Update le numéro de téléphone d'un utilisateur dans la BDD
	 * 
	 * @return AppUser
	 * @throws Exception
	 * 
	 */
	@Secured(value = { "ROLE_ADMIN", "ROLE_USER" })
	@PutMapping("/users/updatephone")
	public AppUser updatePhone(String email, int phone) throws Exception {
		logger.info("Update le numéro de téléphone d'un utilisateur dans la BDD");
		return userService.updatePhone(email, phone);
	}

	/**
	 * 
	 * Update le status d'un utilisateur dans la BDD
	 * 
	 * @return AppUser
	 * @throws Exception
	 * 
	 */
	@Secured(value = { "ROLE_ADMIN" })
	@PutMapping("/users/updateactive")
	public AppUser updateActive(String email, boolean active) throws Exception {
		logger.info("Update le status d'un utilisateur dans la BDD");
		return userService.updateActive(email, active);
	}

	/* *************** DELETE METHODE *********************** */

	/**
	 * 
	 * Supprime un user dans la base de donnée à partir de l'email en paramètre
	 * 
	 * @return AppUser
	 * @throws Exception
	 * 
	 */
	@Secured(value = { "ROLE_ADMIN" })
	@RequestMapping(value = "/users/delete", method = RequestMethod.DELETE, params = { "email" })
	public AppUser deleteById(String email) throws Exception {
		logger.info("INFO: Supprimer dans la BDD un user dont l'email est donné");
		return userService.deleteById(email);
	}

}
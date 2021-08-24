package com.paymybuddy.api.controller;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import org.jboss.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.paymybuddy.api.model.User;
import com.paymybuddy.api.service.UserService;


@RestController
public class UserController {
	
//	private boolean connected;
	private Logger logger = Logger.getLogger(this.getClass());

	@Autowired private UserService userService;
	
	/** 
	 * GET	http://localhost:8080/users
	 * 
	 * Liste les infos d'un utilisateur 'email', 'password' et 'balance' dans l'application
	 * 
	 * @return	Optional<User>
	 * 
	 */
	@GetMapping("/users/userinfo")
	public Optional<User> findByEmail(String email) {
		logger.info("INFO: Liste les infos d'un utilisateur 'email', 'password' et 'balance' dans l'application");
		return userService.findByEmail(email);
	}
	
	/** 
	 * GET	http://localhost:8080/users
	 * 
	 * Liste tous les utilisateurs 'email' et 'balance' de l'application sans leur mot de passe
	 * 
	 * @return	List<String>
	 * 
	 */
	@GetMapping("/users")
	public List<String> findEmailsAndBalances() {
		logger.info("INFO: Liste tous les utilisateurs de l'application sans leur mot de passe");
		return userService.findEmailsAndBalances();
	}
	
	/**
	 * GET http://localhost:8080/users/user?email=francois.hollande@gmail.com
	 * 
	 * Liste les infos sur l'utilisateurs dont l'email est en parametre sans son mot de passe
	 * 
	 * @return List<String>
	 * 
	 */
	@RequestMapping(value = "/users/user", method = RequestMethod.GET, params = { "email" })
	public List<String> findEmailAndBalanceByEmail(String email) {
		logger.info("INFO: Liste les infos sur l'utilisateur dont l'email est : " + email);
		return userService.findEmailAndBalanceByEmail(email);
	}
	
	/**
	 * GET http://localhost:8080/withoutemail?email=francois.hollande@gmail.com
	 * 
	 * Retourne la liste des utilisateurs de l'application outre celui dont l'email est en parametre
	 * 
	 * @return List<String>
	 * 
	 */
	@RequestMapping(value = "/users", method = RequestMethod.GET, params = { "withoutemail" })
	public List<String> findByEmailNot(String withoutemail) {
		logger.info("INFO: Liste utilisateurs de l'application outre celui dont l'email est : " + withoutemail);
		return userService.findByEmailNot(withoutemail);
	}
	
	/* *************** POST METHODE *********************** */

	/**
	 * POST http://localhost:8080/users
	 * 
	 * Creation d'un user dans la base de donnée
	 * 
	 * @return User
	 * 
	 */
	@PostMapping("/users/save")
	@ResponseStatus(HttpStatus.CREATED)
	public User save(@RequestBody User user) {
		logger.info("INFO: Creation dans la BDD d'un nouveau user : "+ user );
		return userService.save(user);
	}
	
	/* *************** PUT METHODE *********************** */

	/**
	 * PUT http://localhost:8080/account
	 * 
	 * Mettre à jour les informations d'un user dans la base de donnée
	 * 
	 * @return User
	 * 
	 */
	@PutMapping("/users/update")
	public User update(@RequestBody User user) throws ParseException {
		logger.info("Update le password dans la BDD");
		return userService.update(user);
	}
	
	/* *************** DELETE METHODE *********************** */

	/**
	 * DELETE http://localhost:8080/account?id=77
	 * 
	 * Supprime un user dans la base de donnée à partir de l'email en paramètre
	 * 
	 * @return User
	 * 
	 */
	@RequestMapping(value = "/users/delete", method = RequestMethod.DELETE, params = { "email" })
	public User deleteById(@RequestParam("email") String email) throws ParseException {
		logger.info("INFO: Supprimer dans la BDD un user dont l'email est : " + email);
		return userService.deleteById(email);
	}
	
	/** 
	 * GET	http://localhost:8080/identification
	 * 
	 * Retourne les informations personnelles de l'utilisateur suite à la connexion
	 * 
	 * @return	User
	 * 
	 */
	@GetMapping("/identification")
	public User getIdentification(String email, String password) {
		logger.info("INFO: Veuillez entrer votre email et votre mot de passe !");
		return userService.getIdentification(email, password);
	}
	
//	/** 
//	 * GET	http://localhost:8080/identification
//	 * 
//	 * Retourne la page d'acceuil suite à la connexion
//	 * 
//	 * @return	boolean
//	 * 
//	 */
//	@GetMapping("/home")
//	public boolean home() {
//		connected = false;
//		logger.info("INFO: Déconnexion de votre compte !");
//		return connected;
//	}
	
}
package com.paymybuddy.api.controller;

import java.text.ParseException;
import java.util.List;

import org.jboss.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
	
	private Logger logger = Logger.getLogger(this.getClass());

	@Autowired private UserService userService;
	
	/** 
	 * GET	http://localhost:8080/users
	 * 
	 * Liste tous les utilisateurs 'email' et 'balance' de l'application sans leur mot de passe
	 * 
	 * @return	Iterable<User>
	 * 
	 */
	@GetMapping("/users")
	public List<String> findEmailAndBalance() {
		logger.info("INFO: Liste tous les utilisateurs de l'application sans leur mot de passe");
		return userService.findEmailAndBalance();
	}
	
	/**
	 * GET http://localhost:8080/users/user?email=francois.hollande@gmail.com
	 * 
	 * Liste les infos sur l'utilisateurs dont l'email est en parametre sans son mot de passe
	 * 
	 * @return User
	 * 
	 */
	@RequestMapping(value = "/users/user", method = RequestMethod.GET, params = { "email" })
	public List<String> findByEmail(String email) {
		logger.info("INFO: Liste les infos sur l'utilisateur dont l'email est : " + email);
		return userService.findByEmail(email);
	}
	
	/**
	 * GET http://localhost:8080/withoutemail?email=francois.hollande@gmail.com
	 * 
	 * Retourne la liste des utilisateurs de l'application outre celui dont l'email est en parametre
	 * 
	 * @return User
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
	 * @return Account
	 * 
	 */
	@PostMapping("/users")
	@ResponseStatus(HttpStatus.CREATED)
	public boolean save(@RequestBody User user) {
		logger.info("INFO: Creation dans la BDD d'un nouveau user : "+ user );
		return userService.save(user);
	}
	
	/* *************** PUT METHODE *********************** */

	/**
	 * PUT http://localhost:8080/account
	 * 
	 * Mettre à jour les informations d'un user dans la base de donnée
	 * 
	 * @return Account
	 * 
	 */
	@RequestMapping(value = "/user", method = RequestMethod.PUT, params = {"email", "password"})
	public boolean updatePassword(String email, String password) throws ParseException {
		logger.info("Update le password dans la BDD");
		return userService.updatePassword(email, password);
	}
	
	/* *************** DELETE METHODE *********************** */

	/**
	 * DELETE http://localhost:8080/account?id=77
	 * 
	 * Supprime un user dans la base de donnée à partir de l'email en paramètre
	 * 
	 * @return void
	 * 
	 */
	@RequestMapping(value = "/user", method = RequestMethod.DELETE, params = { "email" })
	public boolean deleteByEmail(@RequestParam("email") String email) throws ParseException {
		logger.info("INFO: Supprimer dans la BDD un user dont l'email est : " + email);
		return userService.deleteByEmail(email);
	}
	
}
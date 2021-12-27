package com.paymybuddy.api.controller;

import java.text.ParseException;
import java.util.HashSet;
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

import com.paymybuddy.api.model.Friend;
import com.paymybuddy.api.service.FriendService;

@RestController
public class FriendController {

	private Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private FriendService friendService;

	/* *************** GET METHODE *********************** */

	/**
	 * GET http://localhost:8080/friend
	 * 
	 * Liste de tous les couples amis dans l'application
	 * 
	 * @return List<Friend>
	 * 
	 */
	@Secured(value={"ROLE_ADMIN"})
	@GetMapping("/friends")
	public List<Friend> findAll() {
		logger.info("INFO: Liste tous les couples amis de l'application");
		return friendService.findAll();
	}
	
	/**
	 * GET http://localhost:8080/friend?email=nicolas.sarkozy@gmail.com
	 * 
	 * Retourne les emails amis associés à l'adresse email donné en paramètre
	 * 
	 * @return ArrayList<String>
	 * 
	 */
	@Secured(value={"ROLE_ADMIN"})
	@RequestMapping(value = "/emailsfriendsonly", method = RequestMethod.GET, params = { "email" })
	public HashSet<String> findEmailsFriendsOnly(String email) {
		logger.info("INFO: Liste les emails amis associés à l'adresse email : " + email);
		return friendService.findEmailsFriendsOnly(email);
	}
	
//	@Secured(value={"ROLE_ADMIN"})
	@RequestMapping(value = "/pseudosfriends", method = RequestMethod.GET, params = { "email" })
	public HashSet<String> findPseudosFriendsOnly(String email) {
		logger.info("INFO: Affiche la liste des pseudos des amis de celui dont l'email est en parametre : " + email);
		return friendService.findPseudosFriendsOnly(email);
	}
	
	@RequestMapping("/page_pseudosfriends")
	public HashSet<String> findPseudosFriendsOnly(String email, int page, int size) {
		logger.info("INFO: Affiche la liste des pseudos des amis de celui dont l'email est en parametre : " + email);
		return friendService.findPseudosFriendsOnly(email,page,size);
	}
	
	@Secured(value={"ROLE_ADMIN"})
	@RequestMapping("/friends/emailsavailable")
	public HashSet<String> findEmailsavailable(String email) throws Exception {
		logger.info("INFO: Liste des emails amis potentiels pour l'utilisateur : " + email);
		return friendService.findEmailsavailable(email);
	}
	@Secured(value={"ROLE_ADMIN"})
	@RequestMapping(value = "/pseudo", method = RequestMethod.GET, params = { "email" })
	public String findPseudoByEmail(String email) {
		logger.info("INFO: Affiche le pseudo de l'ami dont l'email est en parametre : " + email);
		return friendService.findPseudoByEmail(email);
	}

	/**
	 * GET http://localhost:8080/friend?email=nicolas.sarkozy@gmail.com
	 * 
	 * Retourne les couples amis associés à l'adresse email donné en paramètre
	 * 
	 * @return ArrayList<String>
	 * 
	 */
	@Secured(value={"ROLE_ADMIN"})
	@RequestMapping(value = "/friends", method = RequestMethod.GET, params = { "email" })
	public List<Friend> findByEmail(String email) {
		logger.info("INFO: Liste les couples amis associés à l'adresse email : " + email);
		return friendService.findByEmail(email);
	}
	
	/* *************** POST METHODE *********************** */

	/**
	 * POST http://localhost:8080/friend
	 * 
	 * Creation d'un couple ami dans la base de donnée
	 * 
	 * @return Friend
	 * 
	 */
	@Secured(value={"ROLE_ADMIN"})
	@PostMapping("/friends/save")
	@ResponseStatus(HttpStatus.CREATED)
	public Friend save(@RequestBody Friend friend) {
		logger.info("INFO: Creation d'un nouveau couple ami : "+ friend );
		return friendService.save(friend);
	}

	/* *************** DELETE METHODE *********************** */
	
	/**
	 * DELETE http://localhost:8080/friend?emailUser=francois.hollande@gmail.com
	 * 
	 * Supprimer le couple ami dont l'emailUser et l'emailFriend sont donnés en paramètre
	 * 
	 * @return String
	 * 
	 */
	@Secured(value={"ROLE_ADMIN"})
	@RequestMapping(value = "/friends/delete", method = RequestMethod.DELETE, params = { "emailUser", "emailFriend" })
	public Friend deleteByEmailUserAndEmailFriend(String emailUser, String emailFriend) throws ParseException {
		logger.info("INFO: Supprimer les couples amis suivant : " + emailUser + " & " + emailFriend);
		return friendService.deleteByEmailUserAndEmailFriend(emailUser,emailFriend);
	}
	
	@Secured(value={"ROLE_ADMIN"})
	@RequestMapping(value = "/friends/delete", method = RequestMethod.DELETE, params = { "emailUser", "pseudoFriend" })
	public Friend deleteByEmailUserAndPseudoFriend(String emailUser, String pseudoFriend) throws ParseException {
		logger.info("INFO: Supprimer les couples amis suivant - EmailUser : " + emailUser + " & PseudoFriend :" + pseudoFriend);
		return friendService.deleteByEmailUserAndPseudoFriend(emailUser,pseudoFriend);
	}

	//OK
	@RequestMapping(value = "/test", method = RequestMethod.GET, params = { "pseudo"})
	public String test(String pseudo) throws ParseException { 
		return friendService.findEmailByPseudo(pseudo);
	}
	
	
}

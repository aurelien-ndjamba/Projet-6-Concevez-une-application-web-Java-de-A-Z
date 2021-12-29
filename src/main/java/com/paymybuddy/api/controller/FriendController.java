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
	 * 
	 * Liste les couples amis associés à l'adresse email
	 * 
	 * @return List<Friend>
	 * @throws Exception 
	 * 
	 */
	@Secured(value={"ROLE_ADMIN"})
	@RequestMapping(value = "/friends", method = RequestMethod.GET, params = { "email" })
	public List<Friend> findByEmail(String email) throws Exception {
		logger.info("INFO: Liste les couples amis associés à l'adresse email : " + email);
		return friendService.findByEmail(email);
	}

	/**
	 * 
	 * Retourne les emails amis associés à l'adresse email donné en paramètre
	 * 
	 * @return HashSet<String>
	 * @throws Exception 
	 * 
	 */
	@Secured(value={"ROLE_ADMIN"})
	@RequestMapping(value = "/emailsfriendsonly", method = RequestMethod.GET, params = { "email" })
	public HashSet<String> findEmailsFriendsOnly(String email) throws Exception {
		logger.info("INFO: Liste les emails amis associés à l'adresse email : " + email);
		return friendService.findEmailsFriendsOnly(email);
	}
	
	/**
	 * 
	 * Retourne les pseudos des amis associés à l'adresse email donné en paramètre
	 * 
	 * @return HashSet<String>
	 * @throws Exception 
	 * 
	 */
	@Secured(value={"ROLE_ADMIN"})
	@RequestMapping(value = "/pseudosfriends", method = RequestMethod.GET, params = { "email" })
	public HashSet<String> findPseudosFriendsOnly(String email) throws Exception {
		logger.info("INFO: Liste les pseudos amis associés à l'adresse email : " + email);
		return friendService.findPseudosFriendsOnly(email);
	}
	
	/**
	 * 
	 * Retourne les pseudos des amis associés à l'adresse email donné en paramètre
	 * avec des parametres page et size
	 * 
	 * @return HashSet<String>
	 * @throws Exception 
	 * 
	 */
	@RequestMapping("/page_pseudosfriends")
	public HashSet<String> findPseudosFriendsOnly(String email, int page, int size) throws Exception {
		logger.info("INFO: Affiche la liste des pseudos des amis de celui dont l'email est en parametre : " + email);
		return friendService.findPseudosFriendsOnly(email,page,size);
	}
	
	/**
	 * 
	 * Retourne les emails amis disponibles en amitié pour l'utilisateur dont l'email donné en paramètre.
	 * Sans prendre en compte les emails amis déjà en amitié pour cet utilisateur
	 * 
	 * @return HashSet<String>
	 * 
	 */
	@Secured(value={"ROLE_ADMIN","ROLE_USER"})
	@RequestMapping("/friends/emailsavailable")
	public HashSet<String> findEmailsavailable(String email) throws Exception {
		logger.info("INFO: Liste des emails amis disponibles pour une association avec l'utilisateur : " + email);
		return friendService.findEmailsavailable(email);
	}
	
	/**
	 * 
	 * Affiche le pseudo de l'ami dont l'email est en parametre
	 * 
	 * @return String
	 * 
	 */
	@Secured(value={"ROLE_ADMIN"})
	@RequestMapping(value = "/pseudoByEmail", method = RequestMethod.GET, params = { "email" })
	public String findPseudoByEmail(String email) {
		logger.info("INFO: Affiche le pseudo de l'ami dont l'email est en parametre : " + email);
		return friendService.findPseudoByEmail(email);
	}
	
	/**
	 * 
	 * Affiche l'email de l'ami dont le pseudo est en parametre
	 * 
	 * @return String
	 * 
	 */
	@Secured(value={"ROLE_ADMIN"})
	@RequestMapping(value = "/emailByPseudo", method = RequestMethod.GET, params = { "pseudo" })
	public String findEmailByPseudo(String pseudo) {
		logger.info("INFO: Affiche l'email de l'ami dont le pseudo est en parametre : " + pseudo);
		return friendService.findEmailByPseudo(pseudo);
	}

	
	
	/* *************** POST METHODE *********************** */

	/**
	 * 
	 * Creation d'un couple ami dans la base de donnée
	 * 
	 * @return Friend
	 * @throws Exception 
	 * 
	 */
	@Secured(value={"ROLE_ADMIN","ROLE_USER"})
	@PostMapping("/friends/save")
	@ResponseStatus(HttpStatus.CREATED)
	public Friend save(@RequestBody Friend friend) throws Exception {
		logger.info("INFO: Creation d'un nouveau couple ami : "+ friend );
		return friendService.save(friend);
	}

	/* *************** DELETE METHODE *********************** */
	
	/**
	 * 
	 * Supprimer le couple ami dont emailUser et emailFriend sont donnés en paramètre
	 * 
	 * @return Friend
	 * @throws Exception 
	 * 
	 */
	@Secured(value={"ROLE_ADMIN","ROLE_USER"})
	@RequestMapping(value = "/friends/delete", method = RequestMethod.DELETE, params = { "emailUser", "emailFriend" })
	public Friend deleteByEmailUserAndEmailFriend(String emailUser, String emailFriend) throws Exception {
		logger.info("INFO: Supprimer les couples amis suivant : " + emailUser + " & " + emailFriend);
		return friendService.deleteByEmailUserAndEmailFriend(emailUser,emailFriend);
	}
	
	/**
	 * 
	 * Supprimer le couple ami dont emailUser et pseudoFriend sont donnés en paramètre
	 * 
	 * @return Friend
	 * 
	 */
	@Secured(value={"ROLE_ADMIN","ROLE_USER"})
	@RequestMapping(value = "/friends/delete", method = RequestMethod.DELETE, params = { "emailUser", "pseudoFriend" })
	public Friend deleteByEmailUserAndPseudoFriend(String emailUser, String pseudoFriend) throws ParseException {
		logger.info("INFO: Supprimer les couples amis suivant - EmailUser : " + emailUser + " & PseudoFriend :" + pseudoFriend);
		return friendService.deleteByEmailUserAndPseudoFriend(emailUser,pseudoFriend);
	}
	
}

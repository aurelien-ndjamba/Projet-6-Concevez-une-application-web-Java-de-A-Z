package com.paymybuddy.api.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;

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
	 * @return Iterable<Friend>
	 * 
	 */
	@GetMapping("/friends")
	public Iterable<Friend> findAll() {
		logger.info("INFO: Liste tous les couples amis de l'application");
		return friendService.findAll();
	}
	
	/**
	 * GET http://localhost:8080/friend?email=nicolas.sarkozy@gmail.com
	 * 
	 * Retourne les couples amis associés à l'adresse email donné en paramètre
	 * 
	 * @return Account
	 * 
	 */
	@RequestMapping(value = "/friends/withme", method = RequestMethod.GET, params = { "email" })
	public ArrayList<String> findByEmail(String email) {
		logger.info("INFO: Liste les couples amis associés à l'adresse email : " + email);
		return friendService.findByEmail(email);
	}
	
	/**
	 * GET http://localhost:8080/friend?email=nicolas.sarkozy@gmail.com
	 * 
	 * Retourne uniquement les amis associés à l'adresse email donné en paramètre
	 * 
	 * @return Account
	 * 
	 */
	@RequestMapping(value = "/friends/withme/list", method = RequestMethod.GET, params = { "email" })
	public HashSet<String> findOnlyMyFriends(String email) {
		logger.info("INFO: Liste uniquement les amis associés à l'adresse email : " + email);
		return friendService.findOnlyMyFriends(email);
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
	@PostMapping("/friends/savefriend")
	@ResponseStatus(HttpStatus.CREATED)
	public boolean save(@RequestBody Friend friend) {
		logger.info("INFO: Creation d'un nouveau couple ami : "+ friend );
		return friendService.save(friend);
	}

	/* *************** DELETE METHODE *********************** */
	
	/**
	 * DELETE http://localhost:8080/friend?emailUser=francois.hollande@gmail.com
	 * 
	 * Supprime les couples amis dans la base de donnée dont l'emailUser est donné en paramètre
	 * 
	 * @return void
	 * 
	 */
	@RequestMapping(value = "/friends/deletefriend", method = RequestMethod.DELETE, params = { "emailUser", "emailFriend" })
	public boolean deleteFriends(String emailUser, String emailFriend) throws ParseException {
		logger.info("INFO: Supprimer les couples amis suivant : " + emailUser + " & " + emailFriend);
		return friendService.deleteFriends(emailUser,emailFriend);
	}
	
}

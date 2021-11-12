package com.paymybuddy.api.controller;

import java.text.ParseException;
import java.util.HashSet;
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
	@GetMapping("/friends")
	public List<Friend> findAll() {
		logger.info("INFO: Liste tous les couples amis de l'application");
		return friendService.findAll();
	}
	
	/**
	 * GET http://localhost:8080/friend?email=nicolas.sarkozy@gmail.com
	 * 
	 * Retourne les couples amis associés à l'adresse email donné en paramètre
	 * 
	 * @return ArrayList<String>
	 * 
	 */
	@RequestMapping(value = "/friends/withme/list", method = RequestMethod.GET, params = { "email" })
	public HashSet<String> findFriendsOnly(String email) {
		logger.info("INFO: Liste les couples amis associés à l'adresse email : " + email);
		return friendService.findFriendsOnly(email);
	}
	
	/**
	 * GET http://localhost:8080/friend?email=nicolas.sarkozy@gmail.com
	 * 
	 * Retourne les couples amis associés à l'adresse email donné en paramètre
	 * 
	 * @return ArrayList<String>
	 * 
	 */
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
	@RequestMapping(value = "/friends/delete", method = RequestMethod.DELETE, params = { "emailUser", "emailFriend" })
	public Friend delete(String emailUser, String emailFriend) throws ParseException {
		logger.info("INFO: Supprimer les couples amis suivant : " + emailUser + " & " + emailFriend);
		return friendService.delete(emailUser,emailFriend);
	}
	
}

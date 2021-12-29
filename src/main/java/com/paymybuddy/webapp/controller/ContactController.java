package com.paymybuddy.webapp.controller;

import java.util.ArrayList;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.paymybuddy.webapp.model.Friend;
import com.paymybuddy.webapp.service.AppUserService;
import com.paymybuddy.webapp.service.FriendService;

@Controller
public class ContactController {

	@Autowired
	private AppUserService appUserService;
	@Autowired
	private FriendService friendService;

	String pseudoAdded;
	String emailAdded;
	String pseudoDeleted;
	String emailDeleted;

	@RequestMapping(value = "/contact", method = RequestMethod.GET)
	public String contactGet(Authentication authentication, Model model) throws RuntimeException, Exception {
		model.addAttribute("emailUser", authentication.getName()); 
		model.addAttribute("newFriendContact", new Friend());
		HashSet<String> pseudosFriendsOnly = friendService.findPseudosFriendsOnly(authentication.getName());
		model.addAttribute("pseudosFriendsOnly", pseudosFriendsOnly);
		ArrayList<String> emailsFriendsAvailable = appUserService
				.findOtherEmailsFriendsAvailableForThisEmail(authentication.getName());
		model.addAttribute("emailsFriendsAvailable", emailsFriendsAvailable);
		
		model.addAttribute("pseudoAdded",pseudoAdded);
		model.addAttribute("emailAdded",emailAdded); 
		model.addAttribute("pseudoDeleted",pseudoDeleted);
		model.addAttribute("emailDeleted",emailDeleted); 
		pseudoDeleted = null;
		emailDeleted =null;
		emailAdded = null;
		emailAdded = null;
		return "contact";
	}

	@RequestMapping(value = "/contact", method = RequestMethod.POST)
	public String contactPost(Authentication authentication, @ModelAttribute("newFriendContact") Friend friend
			) {

		Friend f = new Friend();
		f.setEmailUser(authentication.getName());
		f.setEmailFriend(friend.getEmailFriend());
		
		emailAdded = friend.getEmailFriend();
		try {
		pseudoAdded = friendService.findPseudoByEmail(friend.getEmailFriend());
		friendService.saveFriend(f);}
		catch(Exception e) {
		}
		return "redirect:/contact";
	}

	@RequestMapping("/deletecontact")
	public String contactDelete(Authentication authentication, String friend, Model model) throws Exception {
		// logger.info("INFO: Supprimer l'amitié avec : " + pseudoFriend ); // ????
		// pourquoi import impossible
		pseudoDeleted = friend;
		emailDeleted = friendService.findEmailByPseudo(friend);
		friendService.deletecontact(authentication.getName(), friend);
		return "redirect:/contact";
	}

}

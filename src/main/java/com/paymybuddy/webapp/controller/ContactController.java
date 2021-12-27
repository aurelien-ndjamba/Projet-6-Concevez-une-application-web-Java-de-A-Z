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

	@RequestMapping(value = "/contact", method = RequestMethod.GET)
	public String contactGet(Authentication authentication, Model model) throws RuntimeException, Exception {
		model.addAttribute("emailUser", authentication.getName()); 
		model.addAttribute("newFriendContact", new Friend());
		HashSet<String> pseudosFriendsOnly = friendService.findPseudosFriendsOnly(authentication.getName());
		model.addAttribute("pseudosFriendsOnly", pseudosFriendsOnly);
		ArrayList<String> emailsFriendsAvailable = appUserService
				.findOtherEmailsFriendsAvailableForThisEmail(authentication.getName());
		model.addAttribute("emailsFriendsAvailable", emailsFriendsAvailable);
		return "contact";
	}

//	@RequestMapping(value = "/contact", method = RequestMethod.GET)
//	public String contactGet(Authentication authentication, Model model) throws RuntimeException, Exception {
//		model.addAttribute("emailUser", authentication.getName());
//		model.addAttribute("newFriendContact", new Friend());
//		HashSet<String> pseudosFriendsOnly = friendService.findPseudosFriendsOnly(authentication.getName(), 0, 5);
//		System.out.println(pseudosFriendsOnly);
//		model.addAttribute("pseudosFriendsOnly", pseudosFriendsOnly);
//		ArrayList<String> emailsFriendsAvailable = appUserService
//				.findOtherEmailsFriendsAvailableForThisEmail(authentication.getName());
//		model.addAttribute("emailsFriendsAvailable", emailsFriendsAvailable);
//		return "contact";
//	}

//	@RequestMapping(value = "/contact", method = RequestMethod.GET)
//	public String contactGet(Authentication authentication, Model model, @RequestParam("page") Optional<Integer> page, 
//		      @RequestParam("size") Optional<Integer> size) throws RuntimeException, Exception {
//		int currentPage = page.orElse(1);
//        int pageSize = size.orElse(5);
//        
//        
//		model.addAttribute("emailUser", authentication.getName());
//		
//		model.addAttribute("newFriendContact", new Friend());
//		HashSet<String> pseudosFriendsOnly = friendService.findPseudosFriendsOnly(authentication.getName(),currentPage-1,pageSize);
//		
//		model.addAttribute("pseudosFriendsOnly", pseudosFriendsOnly);
//		ArrayList<String> emailsFriendsAvailable = appUserService
//				.findOtherEmailsFriendsAvailableForThisEmail(authentication.getName());
//		
//		model.addAttribute("emailsFriendsAvailable", emailsFriendsAvailable);
//		return "contact";
//	}

	@RequestMapping(value = "/contact", method = RequestMethod.POST)
	public String contactPost(Authentication authentication, @ModelAttribute("newFriendContact") Friend friend) {
		Friend f = new Friend();
		f.setEmailUser(authentication.getName());
		f.setEmailFriend(friend.getEmailFriend());
		friendService.saveFriend(f);
		return "redirect:/contact";
	}

	@RequestMapping("/deletecontact")
	public String contactDelete(Authentication authentication, String friend) {
		// logger.info("INFO: Supprimer l'amitié avec : " + pseudoFriend ); // ????
		// pourquoi import impossible
		friendService.deletecontact(authentication.getName(), friend);
		return "redirect:/contact";
	}

}

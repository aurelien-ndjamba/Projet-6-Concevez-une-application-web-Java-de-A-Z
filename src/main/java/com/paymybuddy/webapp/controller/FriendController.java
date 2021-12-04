package com.paymybuddy.webapp.controller;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.paymybuddy.webapp.model.Friend;
import com.paymybuddy.webapp.service.FriendService;

import lombok.Data;

@Data
@Controller
public class FriendController {

	@Autowired
	private FriendService friendService;

	// --- GetFriendsOnly --- //
	// http://localhost:9001/getcontact?emailUser=nicolas.sarkozy@gmail.com OK
	@RequestMapping(value = "/getcontact", method = RequestMethod.GET, params = {"emailUser"})
	public String allContactsAvalaible(String emailUser, Model model) {
		model.addAttribute("emailUser", emailUser);
		Friend f = new Friend();
//		f.setEmailUser(emailUser);
		model.addAttribute("friend", f);
		HashSet<String> myContactsOnly = friendService.getFriendsOnly(emailUser);
		model.addAttribute("myContactsOnly", myContactsOnly);
		HashSet<String> allOtherContactsExisting = friendService.getAllOtherContactsExisting(emailUser);
		model.addAttribute("allOtherContactsExisting", allOtherContactsExisting);
		return "contact";
	}

	// --- SaveFriend --- //
	// http://localhost:9001/savecontact?emailUser=nicolas.sarkozy@gmail.com
	@RequestMapping(value = "/savecontact", method = RequestMethod.POST, params = {"emailUser"})
	public String saveFriend(String emailUser, @ModelAttribute("friend") Friend friend) {
		Friend f = new Friend();
		f.setEmailUser(emailUser);
		f.setEmailFriend(friend.getEmailFriend());
		friendService.saveFriend(f);
		return "redirect:/getcontact?emailUser=" + friend.getEmailUser();
	}

	// --- Deletefriend --- //
	// http://localhost:9001/deletecontact?emailUser=nicolas.sarkozy@gmail.com&emailFriend=emmanuel.macron@gmail.com
	// OK
	@RequestMapping(value = "/deletecontact", method = RequestMethod.GET, params = { "emailUser", "emailFriend" })
	public String deletecontact(String emailUser, String emailFriend) {
		friendService.deletecontact(emailUser, emailFriend);
		return "redirect:/getcontact?emailUser=" + emailUser;
	}

}

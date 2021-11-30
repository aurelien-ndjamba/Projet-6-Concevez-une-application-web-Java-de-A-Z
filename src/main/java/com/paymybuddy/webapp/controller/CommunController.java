//package com.paymybuddy.webapp.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//
//import com.paymybuddy.webapp.model.AppUser;
//import com.paymybuddy.webapp.model.Friend;
//import com.paymybuddy.webapp.service.FriendService;
//import com.paymybuddy.webapp.service.AppUserService;
//
//@Controller
//public class CommunController {
//
//	@Autowired
//	private AppUserService appUserService;
//	@Autowired
//	private FriendService friendService;
//	
//	// --- Home --- //
//	
//	@GetMapping("/")
//	public String home(Model model) {
//		AppUser e = new AppUser();
//		model.addAttribute("appUser", e);
//		return "home";
//	}
//	
//	@GetMapping("/home")
//	public String index(Model model) {
//		return "redirect:/";
//	}
//
//	// --- Transfert --- //
//	
//	@GetMapping("/transfert")
//	public String transfert() {
//		return "transfert";
//	}
//	
//	// --- Profile --- //
//
//	@GetMapping("/profile?email={id}")
//	public String profile(@PathVariable("id") final String id, Model model) {
//		AppUser a = appUserService.getUser(id);
//		model.addAttribute("user", a);
//		return "profile";
//	}
//
//	// --- Contact --- //
//
//	@GetMapping("/contact?email=")
//	public String contact(Model model) {
//		Iterable<Friend> fs = friendService.getFriendsOnly(String);
//		model.addAttribute("friends", fs);
//		return "contact";
//	}
//	
//	// --- Logoff --- //
//	
//	@GetMapping("/logoff")
//	public String logoff() {
//		return "logoff";
//	}
//
//}

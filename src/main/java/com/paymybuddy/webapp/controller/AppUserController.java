package com.paymybuddy.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.paymybuddy.webapp.model.AppUser;
import com.paymybuddy.webapp.model.Friend;
import com.paymybuddy.webapp.service.AppUserService;

@Controller
public class AppUserController {

	@Autowired
	AppUserService appUserService;
	
	// --- Home --- //
	
	@GetMapping("/")
	public String home(Model model) {
		AppUser e = new AppUser();
		model.addAttribute("appUser", e);
		return "home";
	}
	
	@GetMapping("/home")
	public String index(Model model) {
		return "redirect:/";
	}

	// --- Transfert --- //
	
	@GetMapping("/transfer/{id}")
	public String transfert() {
		return "transfer";
	}
	
	// --- Profile --- //

	@GetMapping("/getprofile/{emailUser}")
	public String getprofile(@PathVariable("emailUser") String emailUser, Model model) {
		AppUser appUser = appUserService.getUser(emailUser);
		model.addAttribute("appUser", appUser);
		return "profile";
	}
	
	@RequestMapping(value = "/updateprofile", method = RequestMethod.PUT, params = {"emailUser"})
	public String updatePassword(String emailUser, @ModelAttribute("appUser") AppUser appUser) {
//		AppUser a = new AppUser();
//		a.setEmail(emailUser);
//		a.setPassword(appUser.getPassword());
		appUserService.updatePassword(emailUser, appUser.getPassword());
		return "redirect:/getprofile/" + appUser.getEmail();
	}

	// --- Contact --- //

	
	// --- Logoff --- //
	
	@GetMapping("/logoff/{id}")
	public String logoff() {
		return "logoff";
	}

}

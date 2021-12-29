package com.paymybuddy.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.paymybuddy.webapp.model.AppUser;
import com.paymybuddy.webapp.service.AccountService;
import com.paymybuddy.webapp.service.AppUserService;

@Controller
public class ProfileController {

	@Autowired
	private AppUserService appUserService;
	@Autowired
	private AccountService accountService;
	
	int newNumber = 1 ;
	int oldNumber = 1 ;
	String passwordModified;

	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public String profile(Authentication authentication, Model model) {
		model.addAttribute("u", appUserService.findByEmail(authentication.getName()));
		model.addAttribute("a", accountService.findByEmail(authentication.getName()));
		model.addAttribute("newNumber",newNumber);
		model.addAttribute("oldNumber",oldNumber);
		model.addAttribute("passwordModified",passwordModified);
		newNumber=1;
		oldNumber=1;
		passwordModified=null;
		return "profile";
	}

	@RequestMapping(value = "/updatepassword", method = RequestMethod.POST)
	public String updatePassword(Authentication authentication, @ModelAttribute("a") AppUser appUser) {
		appUserService.updatePassword(authentication.getName(), appUser.getPassword());
		passwordModified = "your password has been successfully changed.";
		return "redirect:/profile";
	}

	@RequestMapping(value = "/updatephone", method = RequestMethod.POST)
	public String updatePhone(Authentication authentication, @ModelAttribute("u") AppUser appUser) {
		oldNumber =  appUserService.findByEmail(authentication.getName()).getPhone();
		newNumber = appUser.getPhone(); 
		appUserService.updatePhone(authentication.getName(), appUser.getPhone());
		return "redirect:/profile";
	}

}

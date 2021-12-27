package com.paymybuddy.webapp.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.paymybuddy.webapp.model.AppUser; 

@Controller
public class LoginController {

	@GetMapping("/login")
	public String loginGet(Authentication authentication, Model model) throws Exception {
		String result = "login";
		if (authentication != null) {
			result = "redirect:/home";
		}
		model.addAttribute("appUserLogin", new AppUser());
		return result;
	} 

}

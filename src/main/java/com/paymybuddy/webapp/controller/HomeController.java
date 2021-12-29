package com.paymybuddy.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.paymybuddy.webapp.service.AppUserService;

@Controller
public class HomeController {

	@Autowired
	private AppUserService appUserService;

	@GetMapping("/home")
	public String home(Authentication authentication, Model model) {
		model.addAttribute("appUserHome", appUserService.findByEmail(authentication.getName()));
		return "home";
	}

	@GetMapping("/")
	public String index(Authentication authentication) {
		String result = "/index";
		if (authentication != null) {
			result = "redirect:/home";
		}
		return result;
	}

}

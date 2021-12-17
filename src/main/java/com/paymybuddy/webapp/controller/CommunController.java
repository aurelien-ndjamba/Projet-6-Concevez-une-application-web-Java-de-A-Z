package com.paymybuddy.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.paymybuddy.webapp.model.AppUser;
import com.paymybuddy.webapp.service.CommunService;

@Controller
public class CommunController {

	@Autowired
	private CommunService communService;
	
	@RequestMapping(value = "/logoff", method = RequestMethod.GET, params = { "emailUser" })
	public String logoff(String emailUser, Model model) {
		model.addAttribute("emailUser", emailUser);
		AppUser appUser = new AppUser();
		model.addAttribute("appUser", appUser);
		return "login";
	}

}

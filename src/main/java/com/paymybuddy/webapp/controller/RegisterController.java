package com.paymybuddy.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.paymybuddy.webapp.model.Account;
import com.paymybuddy.webapp.model.AppUser;
import com.paymybuddy.webapp.service.AccountService;
import com.paymybuddy.webapp.service.AppUserService;

@Controller
public class RegisterController {

	@Autowired
	private AppUserService appUserService;
	@Autowired
	private AccountService accountService;

	@GetMapping("/register")
	public String registerGet(Authentication authentication, Model model) {
		String result = "register";
		if (authentication != null) {
			result = "redirect:/home";
		}
		AppUser au = new AppUser();
		model.addAttribute("appUserRegister", au);
		return result;
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String registerPost(@ModelAttribute("appUserRegister") AppUser appUser, Model model) {
		// AppUser
		AppUser audb = new AppUser();
		audb.setEmail(appUser.getEmail());
		audb.setPseudo(appUser.getPseudo());
		audb.setPassword(appUser.getPassword());
		audb.setPhone(appUser.getPhone());
		audb.setActive(true);
		audb.setBalance(0.0);
		// Account
		Account acc = new Account();
		acc.setId(appUser.getAccount().getId());
		acc.setEmail(appUser.getEmail());
		acc.setBank(appUser.getAccount().getBank());

		appUserService.save(audb);
		accountService.save(acc);
		model.addAttribute("u", audb);
		model.addAttribute("a", acc);
 
		return "redirect:/login?registerSuccess";
	}

}

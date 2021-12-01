package com.paymybuddy.webapp.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.paymybuddy.webapp.model.Account;
import com.paymybuddy.webapp.model.AppUser;
import com.paymybuddy.webapp.service.AccountService;
import com.paymybuddy.webapp.service.AppUserService;

@Controller
public class AppUserController {

	@Autowired
	AppUserService appUserService;
	@Autowired
	AccountService accountService;
	
	// --- Home --- //
	
//	@GetMapping("/home")
//	public String home() {
//		return "redirect:/";
//	}
	
	@GetMapping("/")
	public String index() {
		return "login";
	}

	// --- Transfert --- //
	
//	@GetMapping("/transfer/{id}")
//	public String transfert() {
//		return "transfer";
//	}
	
	// --- Profile --- //

	@GetMapping("/getprofile/{emailUser}")
	public String getprofile(@PathVariable("emailUser") String emailUser, Model model) {
		
		AppUser appUser = new AppUser();
		appUser.setEmail(emailUser);
		appUser.setBalance(appUserService.getUser(emailUser).getBalance());
		model.addAttribute("appUser", appUser);
		
		Account account = new Account();
		account.setEmail(emailUser);
		model.addAttribute("account", account);
		
		List<Account> accounts = new ArrayList<Account>();
		accounts = accountService.getAccountsByEmail(emailUser);
		model.addAttribute("accounts", accounts);
		
		return "profile";
	}
	
	@RequestMapping(value = "/updatepassword", method = RequestMethod.POST, params = {"emailUser"})
	public String updatePassword(String emailUser, @ModelAttribute("appUser") AppUser appUser) {
		appUserService.updatePassword(emailUser, appUser.getPassword());
		return "redirect:/getprofile/" + emailUser;
	}
	
	@RequestMapping(value = "/deleteappuser", method = RequestMethod.GET, params = {"emailUser"})
	public String deleteAppUser(String emailUser) {
		appUserService.deleteAppUser(emailUser);
		
		return "redirect:/";
	}

	// --- Contact --- //

	
	// --- Logoff --- //
	
//	@GetMapping("/logoff/{id}")
//	public String logoff() {
//		return "logoff";
//	}

}

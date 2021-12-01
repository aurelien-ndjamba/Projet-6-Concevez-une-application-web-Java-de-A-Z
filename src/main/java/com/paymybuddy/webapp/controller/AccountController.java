package com.paymybuddy.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.paymybuddy.webapp.model.Account;
import com.paymybuddy.webapp.service.AccountService;

import lombok.Data;

@Data
@Controller
public class AccountController {
	
	@Autowired
	private AccountService accountService;
	
	@RequestMapping(value = "/saveaccount", method = RequestMethod.POST, params = {"emailUser"})
	public String saveAccount(String emailUser, @ModelAttribute("account") Account account) {
		account.setEmail(emailUser);
		accountService.saveAccount(account);
		return "redirect:/getprofile/" + emailUser;
	}
	
	@RequestMapping(value = "/deleteaccount", method = RequestMethod.GET, params = {"emailUser","id"})
	public String deleteAccount(String emailUser, Integer id) {
		accountService.deleteAccount(id);
		return "redirect:/getprofile/" + emailUser;
	}
	
}

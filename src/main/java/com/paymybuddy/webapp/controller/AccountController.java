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
	
//	@RequestMapping(value = "/updateaccount", method = RequestMethod.POST)
//	public String saveAccount(@ModelAttribute("account") Account account) {
//		account.setEmail(emailUser);
//		accountService.saveAccount(account);
//		return "redirect:/getprofile/" + emailUser;
//	}
	
}

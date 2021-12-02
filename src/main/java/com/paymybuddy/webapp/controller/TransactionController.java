package com.paymybuddy.webapp.controller;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.paymybuddy.webapp.model.AppUser;
import com.paymybuddy.webapp.model.Friend;
import com.paymybuddy.webapp.model.Transaction;
import com.paymybuddy.webapp.service.AppUserService;
import com.paymybuddy.webapp.service.TransactionService;

@Controller
public class TransactionController {
	
	@Autowired
	private TransactionService transactionService;
	@Autowired
	private AppUserService appUserService;
	
	@GetMapping("/gettransfer/{emailUser}")
	public String getTransactions(@PathVariable("emailUser") String emailUser, Model model) {
		List<Transaction> transactions = transactionService.getTransactions(emailUser);
		AppUser appUser = appUserService.getUser(emailUser);
		model.addAttribute("appUser", appUser);
		model.addAttribute("emailUser", emailUser);
		model.addAttribute("transactions", transactions);
		return "transfer";
	}

}

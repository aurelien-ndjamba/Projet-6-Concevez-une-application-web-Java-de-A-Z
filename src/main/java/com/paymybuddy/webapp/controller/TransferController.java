package com.paymybuddy.webapp.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.paymybuddy.webapp.model.AppUser;
import com.paymybuddy.webapp.model.Transaction;
import com.paymybuddy.webapp.model.TransactionStructured;
import com.paymybuddy.webapp.service.AccountService;
import com.paymybuddy.webapp.service.AppUserService;
import com.paymybuddy.webapp.service.FriendService;
import com.paymybuddy.webapp.service.TransactionService;

@Controller
public class TransferController {

	@Autowired
	private AppUserService appUserService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private TransactionService transactionService;
	@Autowired
	private FriendService friendService;

	@RequestMapping("/transfer")
	public String tranferGet(Authentication authentication, Model model) throws RuntimeException, Exception {
		List<TransactionStructured> lts = transactionService.findByEmailStructured(authentication.getName());
		HashSet<String> myemailsfriends = friendService.findEmailsFriendsOnly(authentication.getName());
		AppUser appUser = appUserService.findByEmail(authentication.getName());
		List<String> types = new ArrayList<String>();
		types.add("payment");
		types.add("deposit");
		types.add("withdrawal");
		Transaction transaction = new Transaction();
		model.addAttribute("emailUser", authentication.getName());
		model.addAttribute("lts", lts);
		model.addAttribute("myemailsfriends", myemailsfriends);
		model.addAttribute("appUser", appUser);
		model.addAttribute("types", types);
		model.addAttribute("transaction", transaction);
		return "transfer";
	}

	@RequestMapping(value = "/transfer", method = RequestMethod.POST)
	public String transferPost(Authentication authentication, @ModelAttribute("transaction") Transaction transaction) {
		transaction.setUser(authentication.getName());
		transaction.setAccountUser(accountService.findByEmail(authentication.getName()).getId());

		switch (transaction.getType()) {

		case "payment":
			transactionService.createPayment(transaction);
			break;

		case "deposit":
			transactionService.createDeposit(transaction);
			break;

		case "withdrawal":
			transactionService.createWithdrawal(transaction);
			break;
		default:

		}

		return "redirect:/transfer";
	}

}

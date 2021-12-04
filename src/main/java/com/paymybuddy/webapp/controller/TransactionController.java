package com.paymybuddy.webapp.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.paymybuddy.webapp.model.Account;
import com.paymybuddy.webapp.model.AppUser;
import com.paymybuddy.webapp.model.Transaction;
import com.paymybuddy.webapp.service.AccountService;
import com.paymybuddy.webapp.service.AppUserService;
import com.paymybuddy.webapp.service.FriendService;
import com.paymybuddy.webapp.service.TransactionService;

@Controller
public class TransactionController {

	@Autowired
	private TransactionService transactionService;
	@Autowired
	private FriendService friendService;
	@Autowired
	private AppUserService appUserService;
	@Autowired
	private AccountService accountService;

	@RequestMapping(value = "/gettransfer", method = RequestMethod.GET, params = { "emailUser" })
	public String getTransactions(String emailUser, Model model) {
		List<Transaction> transactions = transactionService.getTransactions(emailUser);
		HashSet<String> friends = friendService.getFriendsOnly(emailUser);
		AppUser appUser = appUserService.getUser(emailUser);
		List<Account> accounts = accountService.getAccountsByEmail(emailUser);
		List<String> types = new ArrayList<String>();
		types.add("payment");
		types.add("deposit");
		types.add("withdrawal");
		Transaction transaction = new Transaction();
		model.addAttribute("emailUser", emailUser);
		model.addAttribute("transactions", transactions);
		model.addAttribute("friends", friends);
		model.addAttribute("appUser", appUser);
		model.addAttribute("accounts", accounts);
		model.addAttribute("types", types);
		model.addAttribute("transaction", transaction);
		System.out.println(transaction);
		return "transfer";
	}

	@RequestMapping(value = "/maketransfer", method = RequestMethod.POST, params = { "emailUser" })
	public String makeTransfer(String emailUser, @ModelAttribute("transaction") Transaction transaction) {
		transaction.setUser(emailUser);
		System.out.println(transaction);

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
			System.out.println("Choix incorrect");
			break;
		}

		return "redirect:/gettransfer?emailUser=" + emailUser;
	}

}

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
	public String tranferGet(Authentication authentication, Model model) throws Exception {
		List<TransactionStructured> lts = transactionService.findByEmailStructured(authentication.getName());
		HashSet<String> myemailsfriends = friendService.findEmailsFriendsOnly(authentication.getName());
		ArrayList<String> myemailsfriendsOrdered = new ArrayList<String>();
		myemailsfriendsOrdered.add("");
		myemailsfriendsOrdered.addAll(myemailsfriends);
		AppUser appUser = appUserService.findByEmail(authentication.getName());
		List<String> types = new ArrayList<String>();
		types.add("payment");
		types.add("deposit");
		types.add("withdrawal");
		Transaction transaction = new Transaction();
		model.addAttribute("emailUser", authentication.getName());
		model.addAttribute("lts", lts);
		model.addAttribute("myemailsfriendsOrdered", myemailsfriendsOrdered);
		model.addAttribute("appUser", appUser);
		model.addAttribute("types", types);
		model.addAttribute("transaction", transaction);
		return "transfer";
	}

	@RequestMapping(value = "/transfer", method = RequestMethod.POST)
	public String transferPost(Authentication authentication, @ModelAttribute("transaction") Transaction transaction,
			Model model) {
		String messageException = null;
		transaction.setUser(authentication.getName());
		transaction.setAccountUser(accountService.findByEmail(authentication.getName()).getId());

		switch (transaction.getType()) {

		case "payment":
			try {
				transactionService.createPayment(transaction);
			} catch (Exception e) {
				int length = e.getMessage().length();
				int beginIndex = 7;
				int endIndex = length - 1;
				messageException = e.getMessage().substring(beginIndex, endIndex);
			}
			break;

		case "deposit":
			try {
				transactionService.createDeposit(transaction);
			} catch (Exception e) {
				int length = e.getMessage().length();
				int beginIndex = 7;
				int endIndex = length - 1;
				messageException = e.getMessage().substring(beginIndex, endIndex);
			}
			break;

		case "withdrawal":
			try {
				transactionService.createWithdrawal(transaction);
			} catch (Exception e) {
				int length = e.getMessage().length();
				int beginIndex = 7;
				int endIndex = length - 1;
				messageException = e.getMessage().substring(beginIndex, endIndex);
			}
			break;

		default:

		}

		List<TransactionStructured> lts = transactionService.findByEmailStructured(authentication.getName());
		HashSet<String> myemailsfriends = friendService.findEmailsFriendsOnly(authentication.getName());
		AppUser appUser = appUserService.findByEmail(authentication.getName());
		System.out.println(appUser);
		List<String> types = new ArrayList<String>();
		types.add("payment");
		types.add("deposit");
		types.add("withdrawal");
		model.addAttribute("emailUser", authentication.getName());
		model.addAttribute("lts", lts);
		model.addAttribute("myemailsfriends", myemailsfriends);
		model.addAttribute("appUser", appUser);
		model.addAttribute("types", types);
		model.addAttribute("transaction", new Transaction());
		model.addAttribute("messageException", messageException);

		return "transfer";
	}

}

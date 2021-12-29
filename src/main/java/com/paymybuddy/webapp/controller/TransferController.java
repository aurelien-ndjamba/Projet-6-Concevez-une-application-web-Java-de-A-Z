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

	Transaction transactionResult;
	String messageException;

	@RequestMapping("/transfer")
	public String tranferGet(Authentication authentication, Model model) throws Exception {
		System.out.println(transactionResult);
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
		model.addAttribute("messageException", messageException);
		System.out.println(transactionResult);
		model.addAttribute("transactionResult", transactionResult);
		messageException = null;
		transactionResult = null;
		System.out.println(transactionResult);
		return "transfer";
	}

	@RequestMapping(value = "/transfer", method = RequestMethod.POST)
//	@ResponseBody PERMET de renvoyer la reponse de la fonction à l"ecran
	public String transferPost(Authentication authentication, @ModelAttribute("transaction") Transaction transaction) {
		transaction.setUser(authentication.getName());
		transaction.setAccountUser(accountService.findByEmail(authentication.getName()).getId());
		System.out.println(transaction);

		switch (transaction.getType()) {

		case "payment":
			try {
				Transaction t = new Transaction();
				transactionResult = t.success(transactionService.createPayment(transaction));
			} catch (Exception e) {
				int length = e.getMessage().length();
				int beginIndex = 7;
				int endIndex = length - 1;
				messageException = e.getMessage().substring(beginIndex, endIndex);
			}
			break;

		case "deposit":
			try {
				Transaction t = new Transaction();
				transactionResult = t.success(transactionService.createDeposit(transaction));
			} catch (Exception e) {
				int length = e.getMessage().length();
				int beginIndex = 7;
				int endIndex = length - 1;
				messageException = e.getMessage().substring(beginIndex, endIndex);
			}
			break;

		case "withdrawal":
			try {
				Transaction t = new Transaction();
				transactionResult = t.success(transactionService.createWithdrawal(transaction));
			} catch (Exception e) {
				int length = e.getMessage().length();
				int beginIndex = 7;
				int endIndex = length - 1;
				messageException = e.getMessage().substring(beginIndex, endIndex);
			}
			break;

		default:

		}
		return "redirect:/transfer";
	}

}

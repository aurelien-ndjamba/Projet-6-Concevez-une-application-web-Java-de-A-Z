package com.paymybuddy.webapp.controller;

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

//	@RequestMapping(value = "/maketransfer", method = RequestMethod.POST, params = { "emailUser" })
//	public String makeTransfer(String emailUser, @ModelAttribute("transaction") Transaction transaction) {
//		transaction.setUser(emailUser);
//		System.out.println(transaction);
//
//		switch (transaction.getType()) {
//
//		case "payment":
//			transactionService.createPayment(transaction);
//			break;
//
//		case "deposit":
//			transactionService.createDeposit(transaction);
//			break;
//
//		case "withdrawal":
//			transactionService.createWithdrawal(transaction);
//			break;
//		default:
//			System.out.println("Choix incorrect");
//			break;
//		}
//
//		return "redirect:/gettransfer?emailUser=" + emailUser;
//	}

}

package com.paymybuddy.webapp.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.paymybuddy.webapp.model.Account;
import com.paymybuddy.webapp.model.AppUser;
import com.paymybuddy.webapp.model.Friend;
import com.paymybuddy.webapp.model.Transaction;
import com.paymybuddy.webapp.model.TransactionStructured;
import com.paymybuddy.webapp.service.AccountService;
import com.paymybuddy.webapp.service.AppUserService;
import com.paymybuddy.webapp.service.FriendService;
import com.paymybuddy.webapp.service.TransactionService;

@Controller
public class AppUserController {

	@Autowired
	private AppUserService appUserService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private TransactionService transactionService;
	@Autowired
	private FriendService friendService;

	AppUser audb = new AppUser();
	Account acc = new Account();

	// ------------- HOME -------------
	@GetMapping("/home")
	public String home() {
		String result = "index";
		if (audb.getEmail() != null) {
			result = "profile";
		}
		return result;
	}

	@GetMapping("/")
	public String index() {
		String result = "index";
		System.out.println(audb);
		if (audb.getEmail() != null) {
			result = "profile";
		}
		return result;
	}

	// ------------- REGISTER -------------

	@GetMapping("/register")
	public String register(Model model) {
		String result = "register";
		if (audb.getEmail() != null) {
			result = "profile";
		}
		AppUser au = new AppUser();
		model.addAttribute("appUserRegister", au);
		return result;
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String register(@ModelAttribute("appUserRegister") AppUser appUser, Model model) {
		// AppUser
		audb.setEmail(appUser.getEmail());
		audb.setPseudo(appUser.getPseudo());
		audb.setPassword(appUser.getPassword());
		audb.setPhone(appUser.getPhone());
		audb.setActive(true);
		audb.setBalance(0.0);
		// Account
		acc.setId(appUser.getAccount().getId());
		acc.setEmail(appUser.getEmail());
		acc.setBank(appUser.getAccount().getBank());

		appUserService.save(audb);
		accountService.save(acc);
		model.addAttribute("u", audb);
		model.addAttribute("a", acc);

		return "redirect:/profile";
	}

	// ------------- LOGOFF -------------
	@GetMapping("/logoff")
	public String logoff() {
		audb.setEmail(null);
		return "redirect:/home";
	}

	// ------------- LOGIN -------------
	@GetMapping("/login")
	public String login(Model model) {
		String result = "login";
		if (audb.getEmail() != null && audb.isActive()==true) {
			result = "profile";
		}
		model.addAttribute("appUserForm", new AppUser());
		return result;
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@ModelAttribute("appUserForm") AppUser appUser) {
		String result = "login";
		if (appUserService.findByEmail(appUser.getEmail()) != null) {
			// AppUser
			audb.setEmail(appUser.getEmail());
			audb.setPseudo(appUserService.findByEmail(appUser.getEmail()).getPseudo());
			audb.setBalance(appUserService.findByEmail(appUser.getEmail()).getBalance());
			audb.setPhone(appUserService.findByEmail(appUser.getEmail()).getPhone());
			audb.setActive(appUserService.findByEmail(appUser.getEmail()).isActive()); // unsubscribe
			audb.setRoles(appUserService.findByEmail(appUser.getEmail()).getRoles()); // security
			// Account
			acc.setId(accountService.findByEmail(appUser.getEmail()).getId());
			acc.setEmail(appUser.getEmail());
			acc.setBank(accountService.findByEmail(appUser.getEmail()).getBank());

			result = "redirect:/profile";
		}
		return result;
	}

	// ------------- PROFILE -------------
	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public String profile(Model model) {
		String result = "redirect:/";
		if (audb.getEmail() != null) {
			audb.setBalance(appUserService.findByEmail(audb.getEmail()).getBalance());
			result = "profile";
		}
		model.addAttribute("u", audb); 
		model.addAttribute("a", acc);
		return result;
	}

	@RequestMapping(value = "/updatepassword", method = RequestMethod.POST)
	public String updatePassword(@ModelAttribute("a") AppUser appUser) {
		audb.setPassword(appUser.getPassword());
		appUserService.updatePassword(audb.getEmail(),appUser.getPassword());
		return "redirect:/profile";
	}

	@RequestMapping(value = "/updatephone", method = RequestMethod.POST)
	public String updatePhone(@ModelAttribute("u") AppUser appUser) {
		audb.setPhone(appUser.getPhone());
		appUserService.updatePhone(audb.getEmail(),appUser.getPhone());
		return "redirect:/profile";
	}

	@RequestMapping(value = "/deleteappuser", method = RequestMethod.GET)
	public String deleteAppUser(String emailUser) {
		appUserService.findByEmail(emailUser).setActive(false);
		appUserService.updateAll(appUserService.findByEmail(emailUser));
		return "redirect:/home";
	}

	// ------------- CONTACT -------------
	@RequestMapping(value = "/contact", method = RequestMethod.GET)
	public String allContactsAvalaible(Model model) throws RuntimeException, Exception {
		String result = "redirect:/";
		if (audb.getEmail() != null) {
			AppUser au = audb;
			Friend f = new Friend();
			model.addAttribute("friend", f);
			HashSet<String> pseudosFriendsOnly = friendService.findPseudosFriendsOnly(au.getEmail());
			model.addAttribute("myContactsOnly", pseudosFriendsOnly);
			HashSet<String> allOtherContactsExisting = friendService.findOtherEmailsFriends(au.getEmail());
			model.addAttribute("allOtherContactsExisting", allOtherContactsExisting);
			result = "contact";
		}

		return result;
	}

	@RequestMapping(value = "/contact/add", method = RequestMethod.POST)
	public String addContact(@ModelAttribute("friend") Friend friend) {
		AppUser au = audb;
		Friend f = new Friend();
		f.setEmailUser(au.getEmail());
		f.setEmailFriend(friend.getEmailFriend());
		friendService.saveFriend(f);
		return "redirect:/contact";
	}

	@RequestMapping("/deletecontact") 
	public String deletecontact(String friend) {
		// logger.info("INFO: Supprimer l'amitié avec : " + pseudoFriend ); // ????
		// pourquoi import impossible
		AppUser au = audb;
		friendService.deletecontact(au.getEmail(), friend); 
		return "redirect:/contact";
	}

	// ------------- TRANSFER -------------

	@RequestMapping("/transfer")
	public String getTransactions(Model model) throws RuntimeException, Exception {
		String result = "redirect:/";
		if (audb.getEmail() != null) {
			AppUser au = audb;
			List<TransactionStructured> lts = transactionService.findByEmailStructured(au.getEmail());
			HashSet<String> friends = friendService.findEmailsFriendsOnly(au.getEmail());
			AppUser appUser = appUserService.findByEmail(au.getEmail());
			List<String> types = new ArrayList<String>();
			types.add("payment");
			types.add("deposit");
			types.add("withdrawal");
			Transaction transaction = new Transaction();
			model.addAttribute("emailUser", au.getEmail());
			model.addAttribute("lts", lts);
			model.addAttribute("friends", friends);
			model.addAttribute("appUser", appUser);
			model.addAttribute("types", types);
			model.addAttribute("transaction", transaction);
			result = "transfer";
		}
		return result;
	}

	@RequestMapping(value = "/maketransfer", method = RequestMethod.POST)
	public String makeTransfer(@ModelAttribute("transaction") Transaction transaction) {
		transaction.setUser(audb.getEmail());
//		transaction.setAccountUser(accountService.findByEmail(audb.getEmail()).getId());
		transaction.setAccountUser(acc.getId());

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
		
		return "redirect:/transfer";
	}

}

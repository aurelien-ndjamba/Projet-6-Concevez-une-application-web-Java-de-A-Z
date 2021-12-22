package com.paymybuddy.webapp.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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

//	@Autowired
//	private UserDetailsService customUserDetailsService;

	private UserDetails ud;

	AppUser audb = new AppUser();
	Account acc = new Account();

	// ------------- LOGIN -------------
	@GetMapping("/login")
	public String login(Model model) throws Exception {
		String result = "login";
		if (audb.getUsername() != null && appUserService.findByEmail(audb.getUsername()).isActive() == true) {
			result = "redirect:/home";
		}
		model.addAttribute("appUserForm", new AppUser());
		return result;
	}

	// ------------- LOGIN -------------
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@ModelAttribute("appUserForm") AppUser appUser, Model model) {
		String result = "redirect:/login";
		if (appUserService.login(appUser) == true || audb.getUsername() != null) {
			// AppUser
			audb.setUsername(appUser.getUsername());
			audb.setPseudo(appUserService.findByEmail(appUser.getUsername()).getPseudo());
			audb.setBalance(appUserService.findByEmail(appUser.getUsername()).getBalance());
			audb.setPhone(appUserService.findByEmail(appUser.getUsername()).getPhone());
			audb.setActive(appUserService.findByEmail(appUser.getUsername()).isActive()); // unsubscribe
			audb.setRoles(appUserService.findByEmail(appUser.getUsername()).getRoles()); // security
			// Account
			acc.setId(accountService.findByEmail(appUser.getUsername()).getId());
			acc.setEmail(appUser.getUsername());
			acc.setBank(accountService.findByEmail(appUser.getUsername()).getBank());

			result = "redirect:/home";
		}
		return result;
	}

	// ------------- LOGOFF -------------
	@GetMapping("/logoff")
	public String logoff() {
		audb.setUsername(null);
		return "redirect:/home";
	}

	// ------------- HOME -------------
	@GetMapping("/home")
	public String home(Authentication authentication, Model model) {
		// Ancien Code Sans Spring Security
//		String result = "redirect:/";
//		if (audb.getUsername() != null && appUserService.findByEmail(audb.getUsername()).isActive() == true) {
//			AppUser a = new AppUser();
//			a.setPseudo(appUserService.findByEmail(audb.getUsername()).getPseudo());
//			model.addAttribute("a", a);
//			result = "home";
//		}
//		return result; 

//		User u = authentication.getPrincipal().;
		System.out.println(authentication.getName());
		System.out.println(appUserService.findByEmail(authentication.getName()).getPseudo());
		model.addAttribute("a", appUserService.findByEmail(authentication.getName()));
		String result = "home";
		return result;// "test";
	}

	@GetMapping("/")
	public String index(Model model) {
		String result = "index";
		if (audb.getUsername() != null && appUserService.findByEmail(audb.getUsername()).isActive() == true) {
			AppUser a = new AppUser();
			a.setPseudo(appUserService.findByEmail(audb.getUsername()).getPseudo());
			model.addAttribute("a", a);
			result = "home";
		}
		return result;
	}

	// ------------- REGISTER -------------
	@GetMapping("/register")
	public String register(Model model) {
		String result = "register";
		if (audb.getUsername() != null && audb.isActive() == true) {
			result = "home";
		}
		AppUser au = new AppUser();
		model.addAttribute("appUserRegister", au);
		return result;
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String register(@ModelAttribute("appUserRegister") AppUser appUser, Model model) {
		String result = "redirect:/profile"; // uuuuuuuuuuuuuuuuu
//		if (appUserService.findByEmail(appUser.getUsername()).equals(null) || appUserService.findByEmail(appUser.getUsername()).isActive() == false) {  // audb.getUsername() == null && appUserService.findByEmail(appUser.getUsername()).equals(null) .isActive()==false uuuuuuuuuuuuuuuuuuu
		if (audb.getUsername() == null) {
			// AppUser
			audb.setUsername(appUser.getUsername());
			audb.setPseudo(appUser.getPseudo());
			audb.setPassword(appUser.getPassword());
			audb.setPhone(appUser.getPhone());
			audb.setActive(true);
			audb.setBalance(0.0);
			// Account
			acc.setId(appUser.getAccount().getId());
			acc.setEmail(appUser.getUsername());
			acc.setBank(appUser.getAccount().getBank());

			appUserService.save(audb);
			accountService.save(acc);
			model.addAttribute("u", audb);
			model.addAttribute("a", acc);
//			result = "redirect:/profile";
		} else if (audb.getUsername() == appUser.getUsername()
				&& appUserService.findByEmail(appUser.getUsername()).isActive() == false) { // audb.getUsername() !=
																							// null &&
																							// appUserService.findByEmail(appUser.getUsername()).isActive()
																							// == false) {
			audb.setUsername(appUser.getUsername());
			audb.setPseudo(appUser.getPseudo());
			audb.setPassword(appUser.getPassword());
			audb.setPhone(appUser.getPhone());
			audb.setActive(true);
			audb.setBalance(0.0);
			// Account
//			acc.setId(appUser.getAccount().getId());
			acc.setEmail(appUser.getUsername());
			acc.setBank(appUser.getAccount().getBank());

			appUserService.updateAll(audb);
			accountService.update(acc);
			model.addAttribute("u", audb);
			model.addAttribute("a", acc);
//			result = "redirect:/profile";
		}
//		else if ( audb.getUsername() == appUser.getUsername() && appUserService.findByEmail(appUser.getUsername()).isActive() == true) {
//			result = "redirect:/login"
//		}

		return result;// "redirect:/profile";
	}

	// ------------- PROFILE -------------
	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public String profile(Model model) {
		String result = "redirect:/";
		if (audb.getUsername() != null && appUserService.findByEmail(audb.getUsername()).isActive() == true) {
			audb.setBalance(appUserService.findByEmail(audb.getUsername()).getBalance());
			result = "profile";
		}
		model.addAttribute("u", audb);
		model.addAttribute("a", acc);
		return result;
	}

	@RequestMapping(value = "/updatepassword", method = RequestMethod.POST)
	public String updatePassword(@ModelAttribute("a") AppUser appUser) {
		audb.setPassword(appUser.getPassword());
		appUserService.updatePassword(audb.getUsername(), appUser.getPassword());
		return "redirect:/profile";
	}

	@RequestMapping(value = "/updatephone", method = RequestMethod.POST)
	public String updatePhone(@ModelAttribute("u") AppUser appUser) {
		audb.setPhone(appUser.getPhone());
		appUserService.updatePhone(audb.getUsername(), appUser.getPhone());
		return "redirect:/profile";
	}

	@RequestMapping(value = "/deleteappuser", method = RequestMethod.GET)
	public String deleteAppUser() {
		appUserService.updateActive(audb.getUsername(), false);
		return "redirect:/home";
	}

	// ------------- CONTACT -------------
	@RequestMapping(value = "/contact", method = RequestMethod.GET)
	public String allContactsAvalaible(Model model) throws RuntimeException, Exception {
		String result = "redirect:/";
		if (audb.getUsername() != null) {
			AppUser au = audb;
			Friend f = new Friend();
			model.addAttribute("friend", f);
			HashSet<String> pseudosFriendsOnly = friendService.findPseudosFriendsOnly(au.getUsername());
			model.addAttribute("myContactsOnly", pseudosFriendsOnly);
			HashSet<String> allOtherContactsExisting = friendService.findOtherEmailsFriends(au.getUsername());
			model.addAttribute("allOtherContactsExisting", allOtherContactsExisting);
			result = "contact";
		}

		return result;
	}

	@RequestMapping(value = "/contact/add", method = RequestMethod.POST)
	public String addContact(@ModelAttribute("friend") Friend friend) {
		AppUser au = audb;
		Friend f = new Friend();
		f.setEmailUser(au.getUsername());
		f.setEmailFriend(friend.getEmailFriend());
		friendService.saveFriend(f);
		return "redirect:/contact";
	}

	@RequestMapping("/deletecontact")
	public String deletecontact(String friend) {
		// logger.info("INFO: Supprimer l'amitié avec : " + pseudoFriend ); // ????
		// pourquoi import impossible
		AppUser au = audb;
		friendService.deletecontact(au.getUsername(), friend);
		return "redirect:/contact";
	}

	// ------------- TRANSFER -------------
	@RequestMapping("/transfer")
	public String getTransactions(Model model) throws RuntimeException, Exception {
		String result = "redirect:/";
		if (audb.getUsername() != null) {
			AppUser au = audb;
			List<TransactionStructured> lts = transactionService.findByEmailStructured(au.getUsername());
			HashSet<String> friends = friendService.findEmailsFriendsOnly(au.getUsername());
			AppUser appUser = appUserService.findByEmail(au.getUsername());
			List<String> types = new ArrayList<String>();
			types.add("payment");
			types.add("deposit");
			types.add("withdrawal");
			Transaction transaction = new Transaction();
			model.addAttribute("emailUser", au.getUsername());
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
		transaction.setUser(audb.getUsername());
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

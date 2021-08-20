package com.paymybuddy.api.controller;

import java.text.ParseException;
import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.paymybuddy.api.model.Account;
import com.paymybuddy.api.service.AccountService;

@RestController
public class AccountController {

	private Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private AccountService accountService;

	/* *************** GET METHODE *********************** */

	/**
	 * GET http://localhost:8080/account
	 * 
	 * Liste des comptes de l'application
	 * 
	 * @return Iterable<Account>
	 * 
	 */
	@GetMapping("/accounts")
	public Iterable<Account> findAll() {
		logger.info("INFO: Liste tous les comptes de l'application");
		return accountService.findAll();
	}
	

	/**
	 * GET http://localhost:8080/account?id=1
	 * 
	 * Retourne les informations du compte dont l'id est en parametre
	 * 
	 * @return Account
	 * 
	 */
	@RequestMapping(value = "/accounts", method = RequestMethod.GET, params = { "id" })
	public Account findById(int id) {
		logger.info("INFO: Liste les informations du compte ayant l'id : " + id);
		return accountService.findById(id);
	}

	/**
	 * GET http://localhost:8080/account?email=nicolas.sarkozy@gmail.com
	 * 
	 * Retourne les informations du compte dont l'email est en parametre
	 * 
	 * @return List<Account>
	 * 
	 */
	@RequestMapping(value = "/accounts", method = RequestMethod.GET, params = { "email" })
	public List<Account> findByEmail(String email) {
		logger.info("INFO: Liste des comptes ayant pour adresse email : " + email);
		return accountService.findByEmail(email);
	}

	/**
	 * GET http://localhost:8080/account?bank=BANQUE KOLB
	 * 
	 * liste les informations des comptes dont le nom de la banque est donné en paramètre
	 * 
	 * @return List<Account>
	 * 
	 */
	@RequestMapping(value = "/accounts", method = RequestMethod.GET, params = { "bank" })
	public List<Account> findByBank(String bank) {
		logger.info("INFO: Liste des comptes ayant pour parametre bank : " + bank);
		
		return accountService.findByBank(bank);
	}

	/* *************** POST METHODE *********************** */

	/**
	 * POST http://localhost:8080/account
	 * 
	 * Creation d'un compte dans la base de donnée
	 * 
	 * @return Account
	 * 
	 */
	@PostMapping("/accounts")
	@ResponseStatus(HttpStatus.CREATED)
	public Account save(@RequestBody Account account) {
		logger.info("INFO: Creation d'un nouveau compte bancaire : "+ account );
		return accountService.save(account);
	}
	
	/* *************** PUT METHODE *********************** */

	/**
	 * PUT http://localhost:8080/account
	 * 
	 * Met à jour les informations d'un compte dans la base de donnée
	 * 
	 * @return Account
	 * 
	 */
	@PutMapping("/accounts")
	public Account update(@RequestBody Account account) {
		logger.info("INFO: Update les informations du compte bancaire existant : " + account + ". L'id n'est pas modifiable");
		return accountService.update(account);
	}

	/* *************** DELETE METHODE *********************** */

	/**
	 * DELETE http://localhost:8080/account?id=77
	 * 
	 * Supprime un compte dans la base de donnée à partir de l'id du compte
	 * 
	 * @return void
	 * 
	 */
	@RequestMapping(value = "/accounts", method = RequestMethod.DELETE, params = { "id" })
	public Account deleteAllById(@RequestParam("id") int id) throws ParseException {
		logger.info("INFO: Supprimer le compte bancaire dont l'id est : " + id);
		return accountService.deleteAllById(id);
	}

}
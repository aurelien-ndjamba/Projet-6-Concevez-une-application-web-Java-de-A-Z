package com.paymybuddy.api.controller;

import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
	 * 
	 * Liste les comptes de l'application
	 * 
	 * @return List<Account>
	 * 
	 */
	@Secured(value = { "ROLE_ADMIN", "ADMIN" })
	@GetMapping("/accounts")
	public List<Account> findAll() {
		logger.info("INFO: Liste tous les comptes de l'application");
		return accountService.findAll();
	}

	/**
	 * 
	 * Retourne les informations du compte dont l'id est en parametre
	 * 
	 * @return Account
	 * @throws Exception
	 * 
	 */
	@Secured(value = { "ROLE_ADMIN" })
	@RequestMapping(value = "/accounts", method = RequestMethod.GET, params = { "id" })
	public Account findById(int id) throws Exception {
		logger.info("INFO: Liste les informations du compte ayant l'id : " + id);
		return accountService.findById(id);
	}

	/**
	 * 
	 * Retourne les informations du compte dont l'email est en parametre
	 * 
	 * @return List<Account>
	 * @throws Exception
	 * 
	 */
	@Secured(value = { "ROLE_ADMIN" })
	@RequestMapping(value = "/accounts", method = RequestMethod.GET, params = { "email" })
	public Account findByEmail(String email) throws Exception {
		logger.info("INFO: Liste des comptes ayant pour adresse email : " + email);
		return accountService.findByEmail(email);
	}

	/* *************** POST METHODE *********************** */

	/**
	 * 
	 * Creation d'un compte dans la base de donnée
	 * 
	 * @return Account
	 * @throws Exception
	 * 
	 */
	@Secured(value = { "ROLE_ADMIN" })
	@PostMapping("/accounts/save")
	@ResponseStatus(HttpStatus.CREATED)
	public Account save(@RequestBody Account account) throws Exception {
		logger.info("INFO: Creation d'un nouveau compte bancaire : " + account);
		return accountService.save(account);
	}

	/* *************** PUT METHODE *********************** */

	/**
	 * 
	 * Mettre à jour le nom de la banque d'un compte bancaire dans la base de
	 * donnée. L'email et le numéro de compte ne sont pas modifiable.
	 * 
	 * @return Account
	 * @throws Exception
	 * 
	 */
	@Secured(value = { "ROLE_ADMIN" })
	@PutMapping("/accounts/updateBank")
	public Account updateBank(@RequestBody Account account) throws Exception {
		logger.info("INFO: Update les informations du compte bancaire existant : " + account
				+ ". L'id n'est pas modifiable");
		return accountService.updateBank(account);
	}

	/* *************** DELETE METHODE *********************** */

	/**
	 * DELETE http://localhost:8080/accounts/delete?id=77
	 * 
	 * Supprime un compte dans la base de donnée à partir de l'id (numéro du compte)
	 * 
	 * @return Account
	 * 
	 */
//	@Secured(value={"ROLE_ADMIN"})
//	@RequestMapping(value = "/accounts/delete", method = RequestMethod.DELETE, params = { "id" })
//	public Account deleteById(int id) throws Exception {
//		logger.info("INFO: Supprimer le compte bancaire dont l'id est : " + id);
//		return accountService.deleteById(id);
//	}

}
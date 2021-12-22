package com.annotationJpa.OneToOne;

import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired AccountRepository accountRepository;
	
	@GetMapping("/accounts/all")
	public List<Account> findAll() {
		logger.info("INFO: Liste tous les comptes de l'application");
		return accountRepository.findAll();
	}

	@RequestMapping(value = "/accounts", method = RequestMethod.GET, params = { "id" })
	public Account findById(int id) {
		logger.info("INFO: Liste les informations du compte ayant l'id : " + id);
		return accountRepository.findById(id);
	}
}

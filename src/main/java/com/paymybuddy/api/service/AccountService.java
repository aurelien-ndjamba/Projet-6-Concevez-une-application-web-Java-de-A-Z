package com.paymybuddy.api.service;

import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.api.model.Account;
import com.paymybuddy.api.repository.AccountRepository;

@Service
public class AccountService {

	private Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private AccountRepository accountRepository;

	public Iterable<Account> findAll() {
		return accountRepository.findAll();
	}

	public Account findById(int id) {
		return accountRepository.findById(id);
	}

	public List<Account> findByEmail(String email) {
		return accountRepository.findByEmail(email);
	}

	public List<Account> findByBank(String bank) {
		return accountRepository.findByBank(bank);
	}

	public Account save(Account account) {

		if (account.getId() == null) {

			logger.error(
					"ERROR: Vous devez renseignez le numéro du compte 'id' pour sauvegarder ce nouveau compte dans l'application");
		
		} else if (accountRepository.findById(account.getId()).isPresent()) {

			logger.error("ERROR: Le numéro du compte 'id' : " + account.getId() + " renseigné existe déjà dans la BDD de l'application");
			account.setId(null);
		}

		return accountRepository.save(account);
	}

	public Account update(Account account) {

		if (account.getId() == null) {

			logger.error(
					"ERROR: Vous devez renseignez le numéro du compte 'id' pour mettre à jour ce compte existant dans l'application");
			
		} else if (accountRepository.findById(account.getId()).isEmpty()) {

			logger.error(
					"ERROR: Vous devez renseignez un numéro du compte 'id' existant dans la BDD pour mettre à jour ce compte dans l'application");
			account.setId(null);
		}

		return accountRepository.save(account);
	}

	public Account deleteAllById(int id) {

		Account account = new Account();
		
		if (accountRepository.findById(id) == null) {
			logger.error("ERROR: Le numéro du compte 'id': "+ id + " n'existe pas dans la BDD de l'application");
			account.setId(null);
		}
		else {
			logger.info("INFO: Le numéro du compte 'id': "+ id + " a été supprimé dans la BDD de l'application");
			account = accountRepository.findById(id);
			accountRepository.deleteAllById(id);
		}

		return account;
	}

}

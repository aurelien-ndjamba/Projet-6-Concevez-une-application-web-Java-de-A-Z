package com.paymybuddy.api.controller;

import java.util.List;
import java.util.UUID;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.paymybuddy.api.model.Transaction;
import com.paymybuddy.api.service.TransactionService;

@RestController
public class TransactionController {

	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private TransactionService transactionService;

	/* *************** GET METHODE *********************** */

	/**
	 * GET http://localhost:8080/transactions/all
	 * 
	 * Liste des transactions de l'application
	 * 
	 * @return Iterable<Transaction>
	 * 
	 */

	@GetMapping("/transactions/all")
	public List<Transaction> findAll() {
		logger.info("INFO: liste des transactions de l'application");
		return transactionService.findAll();
	}

	/**
	 * GET http://localhost:8080/transactions?id=?
	 * 
	 * Liste des informations d'une transaction spécifique ayant l'id en paramètre
	 * 
	 * @return Transaction
	 * 
	 */

	@RequestMapping(value = "/transactions", method = RequestMethod.GET, params = { "id" })
	public Transaction findById(String id) {
		logger.info("INFO: Liste des informations d'une transaction spécifique ayant en paramètre l'id : " + id);
		return transactionService.findById(id);
	}

	/**
	 * GET http://localhost:8080/transactions?id=?
	 * 
	 * Liste des transactions associées à un email en paramètre
	 * 
	 * @return List<Transaction>
	 * 
	 */

	@RequestMapping(value = "/transactions", method = RequestMethod.GET, params = { "email" })
	public List<Transaction> findByUser(String email) {
		logger.info("INFO: Liste les transactions associées à l'email : " + email);
		return transactionService.findByUser(email);
	}

	/**
	 * GET http://localhost:8080/transactions?type=?
	 * 
	 * Liste des transactions de l'application par type : deposit, withdrawal ou
	 * payment
	 * 
	 * @return List<Transaction>
	 * 
	 */

	@RequestMapping(value = "/transactions", method = RequestMethod.GET, params = { "type" })
	public List<Transaction> findByType(String type) {
		logger.info("INFO: Liste des transactions de l'application du type : " + type);
		return transactionService.findByType(type);
	}

	/* *************** POST METHODE *********************** */

	/**
	 * GET http://localhost:8080/transactions
	 * 
	 * Creation d'une nouvelle transaction 'payment' dans l'application
	 * 
	 * @return Transaction
	 * 
	 */

	@PostMapping("/transactions/createpayment")
	@ResponseStatus(HttpStatus.CREATED)
	public Transaction createPayment(@RequestBody Transaction transaction) throws Exception {
		logger.info("INFO: Creation dans l'application d'une nouvelle transaction 'payment' : " + transaction);
		return transactionService.createPayment(transaction);
	}

	/**
	 * GET http://localhost:8080/transactions
	 * 
	 * Creation d'une nouvelle transaction 'deposit' dans l'application
	 * 
	 * @return Transaction
	 * 
	 */

	@PostMapping("/transactions/createdeposit")
	@ResponseStatus(HttpStatus.CREATED)
	public Transaction createDeposit(@RequestBody Transaction transaction) throws Exception {
		logger.info("INFO: Creation dans l'application d'une nouvelle transaction 'deposit' : " + transaction);
		return transactionService.createDeposit(transaction);
	}

	/**
	 * GET http://localhost:8080/transactions
	 * 
	 * Creation d'une nouvelle transaction 'withdrawal' dans l'application
	 * 
	 * @return Transaction
	 * 
	 */

	@PostMapping("/transactions/createwithdrawal")
	@ResponseStatus(HttpStatus.CREATED)
	public Transaction createWithdrawal(@RequestBody Transaction transaction) throws Exception {
		logger.info("INFO: Creation dans l'application d'une nouvelle transaction  'withdrawal': " + transaction);
		return transactionService.createWithdrawal(transaction);
	}

	/* *************** DELETE METHODE *********************** */

	/**
	 * DELETE http://localhost:8080/transactions/deletetransaction?id=?
	 * 
	 * Supprimer une transaction ayant l'id en parametre
	 * 
	 * @return Transaction
	 * 
	 */
	@RequestMapping(value = "/transactions/delete", method = RequestMethod.DELETE, params = { "id" })
	public Transaction deleteById(@RequestParam("id") UUID id) {
		logger.info("INFO: Supprimer une transaction ayant l'id :" + id);
		return transactionService.deleteById(id);
	}
}
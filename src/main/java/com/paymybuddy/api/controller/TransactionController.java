package com.paymybuddy.api.controller;

import java.util.List;
//import java.text.ParseException;
import java.util.Optional;
//import java.util.UUID;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
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
	 * GET http://localhost:8080/transactions
	 * 
	 * Liste des transactions de l'application
	 * 
	 * @return Iterable<Transaction>
	 * 
	 */
	
	@GetMapping("/transactions")
	public List<Transaction> findAll() {
		logger.info("INFO: liste des transactions de l'application");
		return transactionService.findAll();
	}

	/**
	 * GET http://localhost:8080/transactions?id=?
	 * 
	 * Liste des informations d'une transaction spécifique ayant l'id en paramètre
	 * 
	 * @return Optional<Transaction>
	 * 
	 */

	@RequestMapping(value = "/transactions", method = RequestMethod.GET, params = { "id" })
	public Optional<Transaction> findById(String id) {
		logger.info("INFO: Liste des informations d'une transaction spécifique ayant en paramètre l'id : " + id);
		return transactionService.findById(id);
	}

	/**
	 * GET http://localhost:8080/transactions?id=?
	 * 
	 * Liste des transactions des transactions associées à un email en paramètre
	 * 
	 * @return Iterable<Transaction>
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
	 * Liste des transactions de l'application du type : deposit, withdrawal ou
	 * payment
	 * 
	 * @return Iterable<Transaction>
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
	 * Creation d'une nouvelle transaction dans l'application
	 * 
	 * @return boolean
	 * 
	 */

	@PostMapping("/transactions/save")
	@ResponseStatus(HttpStatus.CREATED)
	public Transaction save(@RequestBody Transaction transaction) {
		logger.info("INFO: Creation dans l'application d'une nouvelle transaction : " + transaction);
		return transactionService.save(transaction);
	}

	/* *************** PUT METHODE *********************** */

//	/**
//	 * PUT http://localhost:8080/transactions
//	 * 
//	 * Mise à jour des informations d'une transaction dans la base de donnée
//	 * 
//	 * @return boolean
//	 * 
//	 */
//	@PutMapping("/transaction/updatetransaction")
//	public Transaction update(@RequestBody Transaction transaction) {
//		logger.info("INFO: Mise à jour des informations de la transaction existante : " + transaction
//				+ ". L'id de la transaction n'est pas modifiable");
//		return transactionService.save(transaction);
//	}

	/* *************** DELETE METHODE *********************** */

//	/**
//	 * DELETE http://localhost:8080/transactions/deletetransaction?id=?
//	 * 
//	 * Supprimer une transaction ayant l'id en parametre
//	 * 
//	 * @return boolean
//	 * 
//	 */
//	@RequestMapping(value = "/transactions/deletetransaction", method = RequestMethod.DELETE, params = { "id" })
//	public boolean deleteAllById(@RequestParam("id") UUID id) throws ParseException {
//		logger.info("INFO: Supprimer une transaction ayant l'id :" + id);
//		return transactionService.deleteById(id);
//	}
}
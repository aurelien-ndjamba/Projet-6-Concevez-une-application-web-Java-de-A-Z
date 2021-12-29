package com.paymybuddy.api.controller;

import java.util.List;
import java.util.UUID;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.paymybuddy.api.model.Transaction;
import com.paymybuddy.api.model.TransactionStructured;
import com.paymybuddy.api.service.TransactionService;

@RestController
public class TransactionController {

	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private TransactionService transactionService;

	public void setTransactionService(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	/* *************** GET METHODE *********************** */

	/**
	 * GET http://localhost:8080/transactions/all
	 * 
	 * Liste des transactions de l'application
	 * 
	 * @return Iterable<Transaction>
	 * 
	 */
	@Secured(value = { "ROLE_ADMIN" })
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
	 * @throws Exception
	 * 
	 */

	@Secured(value = { "ROLE_ADMIN" })
	@RequestMapping(value = "/transactions", method = RequestMethod.GET, params = { "id" })
	public Transaction findById(String id) throws Exception {
		logger.info("INFO: Liste des informations d'une transaction spécifique ayant en paramètre l'id : " + id);
		return transactionService.findById(id);
	}

	@Secured(value = { "ROLE_ADMIN" })
	@RequestMapping("/transactionsStructured")
	public List<TransactionStructured> findByEmailStructured(String email) {
		return transactionService.findByEmailStructured(email);
	}

	/**
	 * GET http://localhost:8080/transactions?id=?
	 * 
	 * Liste des transactions associées à un email en paramètre
	 * 
	 * @return List<Transaction>
	 * @throws Exception
	 * 
	 */

	@Secured(value = { "ROLE_ADMIN", "ROLE_USER" })
	@RequestMapping(value = "/transactions", method = RequestMethod.GET, params = { "email" }) // OK againt
//	@RequestMapping("/transactions")
	public List<Transaction> findByUser(String email) throws Exception {
		logger.info("INFO: Liste les transactions associées à l'email : " + email);
		return transactionService.findByUser(email);
	}

	@Secured(value = { "ROLE_ADMIN", "ROLE_USER" })
	@RequestMapping("/ptransactions")
	public List<Transaction> findByUser(String email, int page, int size) throws Exception {
		return transactionService.findByUser(email, page, size);
	};

	/**
	 * GET http://localhost:8080/transactions?type=?
	 * 
	 * Liste des transactions de l'application par type : deposit, withdrawal ou
	 * payment
	 * 
	 * @return List<Transaction>
	 * @throws Exception
	 * 
	 */

	@Secured(value = { "ROLE_ADMIN" })
	@RequestMapping(value = "/transactions", method = RequestMethod.GET, params = { "type" })
	public List<Transaction> findByType(String type) throws Exception {
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
	 * @throws Exception
	 * 
	 */

	@Secured(value = { "ROLE_ADMIN" })
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

	@Secured(value = { "ROLE_ADMIN" })
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

	@Secured(value = { "ROLE_ADMIN" })
	@PostMapping("/transactions/createwithdrawal")
	@ResponseStatus(HttpStatus.CREATED)
	public Transaction createWithdrawal(@RequestBody Transaction transaction) throws Exception {
		logger.info("INFO: Creation dans l'application d'une nouvelle transaction  'withdrawal': " + transaction);
		return transactionService.createWithdrawal(transaction);
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public String FriendEmailNotSpecifiedException(Exception exception) { // ResponseEntity<String>
		return exception.getMessage(); //new ResponseEntity<String>(reduceAmountException);
	}  
	

	/* *************** DELETE METHODE *********************** */

	/**
	 * DELETE http://localhost:8080/transactions/deletetransaction?id=?
	 * 
	 * Supprimer une transaction ayant l'id en parametre
	 * 
	 * @return Transaction
	 * @throws Exception
	 * 
	 */
	@Secured(value = { "ROLE_ADMIN" })
	@RequestMapping(value = "/transactions/delete", method = RequestMethod.DELETE, params = { "id" })
	public Transaction deleteById(@RequestParam("id") UUID id) throws Exception {
		logger.info("INFO: Supprimer une transaction ayant l'id :" + id);
		return transactionService.deleteById(id);
	}
}
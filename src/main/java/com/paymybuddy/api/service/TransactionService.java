package com.paymybuddy.api.service;

import java.util.Optional;
import java.util.UUID;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.api.model.Transaction;
import com.paymybuddy.api.repository.AccountRepository;
import com.paymybuddy.api.repository.TransactionRepository;
import com.paymybuddy.api.repository.UserRepository;

@Service
public class TransactionService {

	private Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private TransactionRepository transactionRepository;
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserService userService;

	public Iterable<Transaction> findAll() {
		return transactionRepository.findAll();
	}

	public Optional<Transaction> findById(UUID id) {
		return transactionRepository.findById(id);
	}

	public Iterable<String> findByUser(String email) {
		return transactionRepository.findByUser(email);
	}

	public Iterable<Transaction> findByType(String type) {
		return transactionRepository.findByType(type);
	}

	public boolean save(Transaction transaction) {

		boolean result = false;
		int countWordInEmailUser = transaction.getUser().length();
		int countWordInEmailFriend = transaction.getFriend().length();
		double balanceUser = 0.0;
		double balanceFriend;
		double newBalanceUser;
		double newBalanceFriend;

		// Vérifier le type
		if (!(transaction.getType().equals("deposit")) && !(transaction.getType().equals("withdrawal"))
				&& !(transaction.getType().equals("payment")))
			logger.error("ERROR: Le type de transaction renseigné : " + transaction.getType()
					+ " doit être: 'deposit', 'withdrawal' ou 'payment'.");

		// Vérifier que 'friend' et 'account' ne soient pas null simultanément
		else if (transaction.getFriend() == null && transaction.getAccountUser() == null)
			logger.error("ERROR: L'email de votre ami : " + transaction.getFriend() + " et votre compte : "
					+ transaction.getAccountUser()
					+ " ne peuvent pas prendre simultanément la valeur 'null', vous devez au moins en remplir un pour valider la transaction. ");

		// Vérifier que 'user' et 'account' ne soient pas null simultanément
		else if (transaction.getUser() == null && transaction.getAccountUser() == null)
			logger.error("ERROR: Votre email et votre compte ne peuvent pas avoir simultanément la valeur 'null'");

		// Vérifier que 'user' et 'friend' ne soient pas null simultanément
		else if (transaction.getUser() == null && transaction.getFriend() == null)
			logger.error(
					"ERROR: Votre email et celui de votre ami ne peuvent pas avoir simultanément la valeur 'null'");

		// Vérifier que 'user' , 'friend' et 'compte' ne soient pas simultanément
		// fournis
		else if (transaction.getUser() != null && transaction.getFriend() != null
				&& transaction.getAccountUser() != null)
			logger.error("ERROR: 'User' , 'Friend' et 'AccountUser' ne peuvent pas être simultanément fournis");

		// Vérifier que le montant renseigné doit être dans sur certaine limite imposée
		else if (!(transaction.getAmount() > 0 && transaction.getAmount() <= 99999999.99))
			logger.error("ERROR: Le montant renseigné doit être supérieur à 0 et au maximum de 99999999.99");

		// Payment transaction
		else if (transaction.getUser() != null && transaction.getFriend() != null
				&& transaction.getAccountUser() == null) {

			// Vérifier que user et friend existent en BDD
			if (!userRepository.existsById(transaction.getUser())
					|| !userRepository.existsById(transaction.getFriend()))
				logger.error("ERROR: Votre email : " + transaction.getUser() + " ou celui de votre ami : "
						+ transaction.getFriend()
						+ " n'existe pas n'existent pas dans la BDD de l'application -> Echec Transaction ");

			// Vérifier que le solde disponible soit supérieur au montant à envoyer
			else if (Double.parseDouble(userRepository.findByEmail(transaction.getUser()).get(0)
					.substring(countWordInEmailUser + 1)) > transaction.getAmount()) {
				balanceUser = Double.parseDouble(
						userRepository.findByEmail(transaction.getUser()).get(0).substring(countWordInEmailUser + 1));
				balanceFriend = Double.parseDouble(userRepository.findByEmail(transaction.getFriend()).get(0)
						.substring(countWordInEmailFriend + 1));
				newBalanceUser = balanceUser - transaction.getAmount();
				newBalanceFriend = balanceFriend + transaction.getAmount();
				userService.updateBalance(transaction.getUser(), newBalanceUser);
				userService.updateBalance(transaction.getFriend(), newBalanceFriend);

				transaction.setDate(null); // Autoremplissage avec @CreationTimestamp
				transactionRepository.save(transaction);
				result = true;
			} else
				logger.error("ERROR: Vous ne pouvez pas effectuer un paiement : '" + transaction.getAmount()
						+ "' supérieur à votre solde disponible : '"
						+ userRepository.findByEmail(transaction.getUser()).get(0).substring(countWordInEmailUser + 1)
						+ "'. Veuillez augmenter votre solde disponible depuis votre compte bancaire avant ou réduire la somme à payer!");
		}

		// Deposit et withdrawal transaction
		else if (transaction.getUser() != null && transaction.getFriend() == null
				&& transaction.getAccountUser() != null) {

			// Vérifier que user et account existent en BDD et type = deposit
			if (userRepository.existsById(transaction.getUser())
					&& accountRepository.existsById(transaction.getAccountUser())
					&& transaction.getType().equals("deposit")) {

				balanceUser = Double.parseDouble(
						userRepository.findByEmail(transaction.getUser()).get(0).substring(countWordInEmailUser + 1));
				newBalanceUser = balanceUser + transaction.getAmount();
				userService.updateBalance(transaction.getUser(), newBalanceUser);
				transaction.setDate(null); // Autoremplissage avec @CreationTimestamp
				transactionRepository.save(transaction);
				result = true;
			}
			// Vérifier que user et account existent en BDD et type = withdrawal et le solde
			// disponible soit supérieur au montant à retirer
			else if (userRepository.existsById(transaction.getUser())
					&& accountRepository.existsById(transaction.getAccountUser())
					&& transaction.getType().equals("withdrawal")
					&& Double.parseDouble(userRepository.findByEmail(transaction.getUser()).get(0)
							.substring(countWordInEmailUser + 1)) > transaction.getAmount()) {
				balanceUser = Double.parseDouble(
						userRepository.findByEmail(transaction.getUser()).get(0).substring(countWordInEmailUser + 1));
				newBalanceUser = balanceUser - transaction.getAmount();
				userService.updateBalance(transaction.getUser(), newBalanceUser);
				transaction.setDate(null); // Autoremplissage avec @CreationTimestamp
				transactionRepository.save(transaction);
				result = true;
			} else
				logger.error("ERROR: Votre email : " + transaction.getUser() + " ou votre compte bancaire : "
						+ transaction.getAccountUser()
						+ " n'existe pas n'existent pas dans la BDD de l'application ");
		} else
			logger.info("INFO: Résultat de la transaction de type : "+ transaction.getType() + " et pour le montant de : " + transaction.getAmount() + " est : " + result + " . ATTENTION: true -> OK / false -> NOK");

		return result;
	}

	public boolean deleteById(UUID id) {
		boolean result = false;
		if (transactionRepository.findById(id).isPresent()) {
			transactionRepository.deleteById(id);
			result = true;
		}
		return result;
	}

}

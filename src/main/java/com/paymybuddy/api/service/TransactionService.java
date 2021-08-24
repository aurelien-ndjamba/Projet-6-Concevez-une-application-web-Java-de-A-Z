package com.paymybuddy.api.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.api.model.Constante;
import com.paymybuddy.api.model.Transaction;
import com.paymybuddy.api.repository.AccountRepository;
import com.paymybuddy.api.repository.TransactionRepository;
import com.paymybuddy.api.repository.UserRepository;

@Service
public class TransactionService {

	private Logger logger = Logger.getLogger(this.getClass());
	
	boolean result;
	double balanceUser;
	double balanceFriend;
	double newBalanceUser;
	double newBalanceFriend;

	@Autowired
	private TransactionRepository transactionRepository;
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private FriendService friendService;

	public List<Transaction> findAll() {
		return transactionRepository.findAll();
	}

	public Optional<Transaction> findById(String id) {
		UUID idInUUID = UUID.fromString(id);
		return transactionRepository.findById(idInUUID);
	}

	public List<Transaction> findByUser(String email) {
		String friend = email;
		return transactionRepository.findByUserOrFriend(email, friend);
	}

	public List<Transaction> findByType(String type) {
		return transactionRepository.findByType(type);
	}

	public Transaction save(Transaction transaction) {
		Transaction result = new Transaction();
		if (!(transaction.getType().equals("deposit")) && !(transaction.getType().equals("withdrawal"))
				&& !(transaction.getType().equals("payment")))
			logger.error("ERROR: Le type de la transaction renseignée : " + transaction.getType()
					+ " doit être: 'deposit', 'withdrawal' ou 'payment' !");

		switch (transaction.getType()) {
		case "payment":
			if (transaction.getUser() == null) {
				logger.error("ERROR: Vous devez renseigner votre email pour effectuer une transaction de paiement !");
				break;
			} else if (!userRepository.existsById(transaction.getUser())) {
				logger.error("ERROR: Votre email utilisateur n'existe pas en BDD. Veuillez le modifier !");
				break;
			} else if (transaction.getFriend() == null) {
				logger.error(
						"ERROR: Vous devez renseigner l'email de votre ami pour effectuer une transaction de paiement !");
				break;
			} else if (!userRepository.existsById(transaction.getFriend())) {
				logger.error("ERROR: L'email de votre ami n'existe pas en BDD. Veuillez le modifier !");
				break;
			} else if (!friendService.findOnlyMyFriends(transaction.getUser()).contains(transaction.getFriend())) {
				logger.error("ERROR: Vous n'êtes pas ami avec l'utilisateur ayant l'email : '" + transaction.getFriend()
						+ "'. Veuillez choisir un de vos amis pour effectuer un paiement !");
				break;
			} else if (transaction.getAccountUser() != null) {
				logger.error("ERROR: Votre compte bancaire utilisateur : " + transaction.getAccountUser()
						+ " ne doit pas être fourni pour effectuer une transaction de paiement !");
				break;
			} else if (Double.parseDouble(userRepository.findEmailAndBalanceByEmail(transaction.getUser()).get(0)
					.substring(transaction.getUser().length() + 1)) <= transaction.getAmount()) {
				logger.error("ERROR: Vous ne pouvez pas effectuer un paiement : '" + transaction.getAmount()
						+ "' supérieur à votre solde disponible : '"
						+ userRepository.findEmailAndBalanceByEmail(transaction.getUser()).get(0)
								.substring(transaction.getUser().length() + 1)
						+ "'. Veuillez augmenter votre solde disponible depuis votre compte bancaire avant ou réduire la somme à payer !");
				break;
			} else if (Double
					.parseDouble(userRepository.findEmailAndBalanceByEmail(transaction.getFriend()).get(0)
							.substring(transaction.getFriend().length() + 1))
					+ transaction.getAmount() >= Constante.feeTransaction) {
				logger.error(
						"ERROR: Le solde disponible de votre votre ami ne doit pas être supérieur à " + Constante.balanceMax + " après le paiement. Réduire la somme à payer !");
				break;
			} else {
				balanceUser = Double.parseDouble(userRepository.findEmailAndBalanceByEmail(transaction.getUser()).get(0)
						.substring(transaction.getFriend().length() + 1));
				balanceFriend = Double.parseDouble(userRepository.findEmailAndBalanceByEmail(transaction.getFriend()).get(0)
						.substring(transaction.getFriend().length() + 1));
				newBalanceUser = balanceUser - transaction.getAmount() * (1 + Constante.feeTransaction);
				newBalanceFriend = balanceFriend + transaction.getAmount(); 
				userService.updateBalance(transaction.getUser(), newBalanceUser);
				userService.updateBalance(transaction.getFriend(), newBalanceFriend);
				transaction.setDate(null); // Autoremplissage avec @CreationTimestamp
				transactionRepository.save(transaction);
				result = transaction;
				logger.info("INFO : Votre nouveau solde disponible après paiement : '" + newBalanceUser + "' !");
				break;
			}

		case "deposit":
			if (transaction.getUser() == null) {
				logger.error("ERROR: Vous devez renseigner votre email pour effectuer une transaction de dépot !");
				break;
			} else if (!userRepository.existsById(transaction.getUser())) {
				logger.error("ERROR: Votre email utilisateur n'existe pas en BDD. Veuillez le modifier !");
				break;
			} else if (transaction.getFriend() != null) {
				logger.error("ERROR: L'email d'un ami ne doit pas être mentionné pour une transaction de dépot !");
				break;
			} else if (transaction.getAccountUser() == null) {
				logger.error(
						"ERROR: Vous devez renseigner votre compte bancaire pour effectuer une transaction de dépot !");
				break;
			} else if (accountRepository.findById(transaction.getAccountUser()).isEmpty()) {
				logger.error("ERROR: Le compte bancaire utilisateur : " + transaction.getAccountUser()
						+ " n'existe pas en BDD. Veuillez le modifier !");
				break;
			} else if (!accountRepository.findById(transaction.getAccountUser()).get().getEmail()
					.equals(transaction.getUser())) {
				logger.error("ERROR: Votre compte bancaire utilisateur saisi : " + transaction.getAccountUser()
						+ " ne vous appartient pas. Veuillez le modifier !");
				break;
			} else if (Double.parseDouble(userRepository.findEmailAndBalanceByEmail(transaction.getUser()).get(0)
					.substring(transaction.getUser().length() + 1)) + transaction.getAmount() >= Constante.feeTransaction) {
				logger.error(
						"ERROR: Votre solde disponible ne doit pas être supérieur à " + Constante.balanceMax + " après la transaction de dépôt. Veuillez réduire la somme à déposer !");
				break;
			} else {
				balanceUser = Double.parseDouble(userRepository.findEmailAndBalanceByEmail(transaction.getUser()).get(0)
						.substring(transaction.getUser().length() + 1));
				newBalanceUser = balanceUser + transaction.getAmount() * (1 + Constante.feeTransaction);
				userService.updateBalance(transaction.getUser(), newBalanceUser);
				transaction.setDate(null); // Autoremplissage avec @CreationTimestamp
				transactionRepository.save(transaction);
				result = transaction;
				logger.info("INFO : Votre nouveau solde disponible après dépot est de : '" + newBalanceUser + "' !");
				break;
			}
		case "withdrawal":
			if (transaction.getUser() == null) {
				logger.error("ERROR: Vous devez renseigner votre email pour effectuer une transaction de retrait !");
				break;
			} else if (!userRepository.existsById(transaction.getUser())) {
				logger.error("ERROR: Votre email utilisateur n'existe pas en BDD. Veuillez le modifier !");
				break;
			} else if (transaction.getAccountUser() == null) {
				logger.error(
						"ERROR: Vous devez renseigner votre compte bancaire pour effectuer une transaction de retrait !");
				break;
			} else if (accountRepository.findById(transaction.getAccountUser()).isEmpty()) {
				logger.error("ERROR: Le compte bancaire utilisateur : " + transaction.getAccountUser()
						+ " n'existe pas en BDD. Veuillez le modifier !");
				break;
			} else if (!accountRepository.findById(transaction.getAccountUser()).get().getEmail()
					.equals(transaction.getUser())) {
				logger.error("ERROR: Votre compte bancaire utilisateur saisi : " + transaction.getAccountUser()
						+ " ne vous appartient pas. Veuillez le modifier !");
				break;
			} else if (transaction.getFriend() != null) {
				logger.error("ERROR: L'email d'un ami ne doit pas être mentionné pour une transaction de retrait !");
				break;
			} else if (Double.parseDouble(userRepository.findEmailAndBalanceByEmail(transaction.getUser()).get(0)
					.substring(transaction.getUser().length() + 1)) - transaction.getAmount() < Constante.balanceMin) {
				logger.error(
						"ERROR: Votre solde disponible ne doit pas être inférieur à " + Constante.balanceMin + " après le retrait. Réduire la somme à retirer ! ");
				break;
			} else {
				balanceUser = Double.parseDouble(userRepository.findEmailAndBalanceByEmail(transaction.getUser()).get(0)
						.substring(transaction.getUser().length() + 1));
				newBalanceUser = balanceUser - transaction.getAmount()  * (1 + Constante.feeTransaction);
				userService.updateBalance(transaction.getUser(), newBalanceUser);
				transaction.setDate(null); // Autoremplissage avec @CreationTimestamp
				transactionRepository.save(transaction);
				result = transaction;
				logger.info("INFO : Votre nouveau solde disponible après retrait est de : '" + newBalanceUser + "' !");
				break;
			}
		default:
			logger.info("INFO: Le résultat de la transaction ? : " + result + " . Si null cad aucune transaction effectuée");
		}
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

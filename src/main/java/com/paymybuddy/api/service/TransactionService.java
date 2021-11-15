package com.paymybuddy.api.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paymybuddy.api.model.Constante;
import com.paymybuddy.api.model.Transaction;
import com.paymybuddy.api.repository.AccountRepository;
import com.paymybuddy.api.repository.TransactionRepository;
import com.paymybuddy.api.repository.UserRepository;

@Service
public class TransactionService {

	private double balanceUser;
	private double balanceFriend;
	private double newBalanceUser;
	private double newBalanceFriend;

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

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setTransactionRepository(TransactionRepository transactionRepository) {
		this.transactionRepository = transactionRepository;
	}

	public void setFriendService(FriendService friendService) {
		this.friendService = friendService;
	}

	public void setAccountRepository(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	public List<Transaction> findAll() {
		return transactionRepository.findAll();
	}

	public List<Transaction> findByType(String type) {
		if (!type.equals("deposit") && !type.equals("withdrawal") && !type.equals("payment"))
			throw new RuntimeException("La transaction doit être du type : 'deposit', 'withdrawal' ou 'payment'.");
		return transactionRepository.findByType(type);
	}

	public Transaction findById(String id) {
		if (!transactionRepository.existsById(UUID.fromString(id)))
			throw new RuntimeException("Aucune transaction ayant cet id existe dans la BDD");
		UUID idInUUID = UUID.fromString(id);
		return transactionRepository.findById(idInUUID).get();
	}

	public List<Transaction> findByUser(String email) {
		if (userRepository.findByEmail(email) == null)
			throw new RuntimeException("Email non existant dans la BDD");
		String friend = email;
		return transactionRepository.findByUserOrFriend(email, friend);
	}

	@Transactional
	public Transaction createPayment(Transaction transaction) {
		if (!transaction.getType().equals("payment"))
			throw new RuntimeException("La transaction doit être du type : 'payment'.");
		else if (transaction.getUser() == null)
			throw new RuntimeException(
					"Vous devez renseigner votre email pour effectuer une transaction de paiement !");
		else if (!userRepository.existsById(transaction.getUser()))
			throw new RuntimeException("Votre email utilisateur n'existe pas en BDD. Veuillez le modifier !");
		else if (transaction.getFriend() == null)
			throw new RuntimeException(
					"Vous devez renseigner l'email de votre ami pour effectuer une transaction de paiement !");
		else if (!userRepository.existsById(transaction.getFriend()))
			throw new RuntimeException("L'email de votre ami n'existe pas en BDD. Veuillez le modifier !");
		else if (!friendService.findFriendsOnly(transaction.getUser()).contains(transaction.getFriend()))
			throw new RuntimeException("Vous n'êtes pas ami avec l'utilisateur ayant l'email : '"
					+ transaction.getFriend() + "'. Veuillez choisir un de vos amis pour effectuer un paiement !");
		else if (userRepository.findByEmail(transaction.getUser()).getBalance() <= transaction.getAmount()
				* (1 + Constante.feeTransaction))
			throw new RuntimeException("Vous ne pouvez pas effectuer un paiement (frais compris) : '"
					+ transaction.getAmount() * (1 + Constante.feeTransaction)
					+ "' supérieur à votre solde disponible : '"
					+ userRepository.findByEmail(transaction.getUser()).getBalance()
					+ "'. Veuillez augmenter votre solde disponible depuis votre compte bancaire avant ou réduire la somme à payer !");
		else if (userRepository.findByEmail(transaction.getFriend()).getBalance()
				+ transaction.getAmount() * (1 + Constante.feeTransaction) > Constante.balanceMax)
			throw new RuntimeException(
					"Vous devez effectuer un paiement d'un montant inférieur à cet utilisateur pour l'instant !");

		transaction.setLastBalance(userRepository.getById(transaction.getUser()).getBalance());//1000
		balanceUser = userRepository.findByEmail(transaction.getUser()).getBalance();//2000
		balanceFriend = userRepository.findByEmail(transaction.getFriend()).getBalance();//1000
		newBalanceUser = balanceUser - transaction.getAmount() * (1 + Constante.feeTransaction);
		newBalanceFriend = balanceFriend + transaction.getAmount();
		userService.updateBalance(transaction.getUser(), newBalanceUser);
		userService.updateBalance(transaction.getFriend(), newBalanceFriend);
		transaction.setDate(null); // Autoremplissage avec @CreationTimestamp
		transaction.setNewBalance(newBalanceUser);
		transaction.setAccountUser(null);
		transaction.setFee(transaction.getAmount() * Constante.feeTransaction);
		return transactionRepository.save(transaction);
	}

	@Transactional
	public Transaction createDeposit(Transaction transaction) {
		if (!transaction.getType().equals("deposit"))
			throw new RuntimeException("La transaction doit être du type : 'deposit'.");
		else if (transaction.getUser() == null)
			throw new RuntimeException("Vous devez renseigner votre email pour effectuer une transaction de dépot !");
		else if (!userRepository.existsById(transaction.getUser()))
			throw new RuntimeException("Votre email utilisateur n'existe pas en BDD. Veuillez le modifier !");
		else if (transaction.getAccountUser() == null)
			throw new RuntimeException(
					"Vous devez renseigner votre compte bancaire pour effectuer une transaction de dépot !");
		else if (accountRepository.findById(transaction.getAccountUser()).isEmpty())
			throw new RuntimeException("Le compte bancaire utilisateur : " + transaction.getAccountUser()
					+ " n'existe pas en BDD. Veuillez le modifier !");
		else if (!accountRepository.findById(transaction.getAccountUser()).get().getEmail()
				.equals(transaction.getUser()))
			throw new RuntimeException("Votre compte bancaire utilisateur saisi : " + transaction.getAccountUser()
					+ " ne vous appartient pas. Veuillez le modifier !");
		else if (userRepository.findByEmail(transaction.getUser()).getBalance()
				+ transaction.getAmount() * (1 - Constante.feeTransaction) > Constante.balanceMax)
			throw new RuntimeException("Votre solde disponible "
					+ userRepository.getById(transaction.getUser()).getBalance() + " ne doit pas être supérieur à "
					+ Constante.balanceMax + " après la transaction de dépôt (frais compris). Veuillez réduire la somme à déposer !");

		balanceUser = userRepository.findByEmail(transaction.getUser()).getBalance();//1000
		newBalanceUser = balanceUser + transaction.getAmount() * (1 - Constante.feeTransaction);
		transaction.setLastBalance(balanceUser);
		transaction.setNewBalance(newBalanceUser);
		transaction.setFee(transaction.getAmount() * Constante.feeTransaction);
		transaction.setFriend(null);
		transaction.setDate(null); // Autoremplissage avec @CreationTimestamp
		userService.updateBalance(transaction.getUser(), newBalanceUser);
		return transactionRepository.save(transaction);
	}

	@Transactional
	public Transaction createWithdrawal(Transaction transaction) {
		if (!transaction.getType().equals("withdrawal")) 
			throw new RuntimeException("La transaction doit être du type : 'withdrawal'.");
		 else if (transaction.getUser() == null) 
			throw new RuntimeException("Vous devez renseigner votre email pour effectuer une transaction de retrait !");
		 else if (!userRepository.existsById(transaction.getUser())) 
			throw new RuntimeException("Votre email utilisateur n'existe pas en BDD. Veuillez le modifier !");
		 else if (transaction.getAccountUser() == null) 
			throw new RuntimeException(
					"Vous devez renseigner votre compte bancaire pour effectuer une transaction de retrait !");
		 else if (accountRepository.findById(transaction.getAccountUser()).isEmpty()) 
			throw new RuntimeException("Le compte bancaire utilisateur : " + transaction.getAccountUser()
					+ " n'existe pas en BDD. Veuillez le modifier !");
		 else if (!accountRepository.findById(transaction.getAccountUser()).get().getEmail()
				.equals(transaction.getUser())) 
			throw new RuntimeException("Votre compte bancaire utilisateur saisi : " + transaction.getAccountUser()
					+ " ne vous appartient pas. Veuillez le modifier !");
		 else if (userRepository.findByEmail(transaction.getUser()).getBalance()
				- transaction.getAmount() * (1 + Constante.feeTransaction) < Constante.balanceMin) 
			throw new RuntimeException("Votre solde disponible ne doit pas être inférieur à " + Constante.balanceMin
					+ " après le retrait (frais compris). Réduire la somme à retirer ! ");
		
		balanceUser = userRepository.findByEmail(transaction.getUser()).getBalance();
		newBalanceUser = balanceUser - transaction.getAmount() * (1 + Constante.feeTransaction);
		transaction.setLastBalance(balanceUser);
		transaction.setNewBalance(newBalanceUser);
		transaction.setFee(transaction.getAmount() * Constante.feeTransaction);
		transaction.setFriend(null);
		transaction.setDate(null); // Autoremplissage avec @CreationTimestamp
		userService.updateBalance(transaction.getUser(), newBalanceUser);
		return transactionRepository.save(transaction);
	}

	@Transactional
	public Transaction deleteById(UUID id) {
		if (!transactionRepository.findById(id).isPresent())
			throw new RuntimeException("La transaction n'est pas existante dans la BDD.");
		Transaction result = transactionRepository.getById(id);
		transactionRepository.deleteById(id);
		return result;
	}

}

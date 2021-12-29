package com.paymybuddy.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.paymybuddy.api.model.Account;
import com.paymybuddy.api.model.AppUser;
import com.paymybuddy.api.model.Constante;
import com.paymybuddy.api.model.Transaction;
import com.paymybuddy.api.repository.AccountRepository;
import com.paymybuddy.api.repository.TransactionRepository;
import com.paymybuddy.api.repository.UserRepository;

@SpringBootTest

public class TransactionServiceTest {
	@Autowired
	private TransactionService transactionService;

	@Mock
	private TransactionRepository transactionRepositoryMock;
	@Mock
	private UserRepository userRepositoryMock;
	@Mock
	private AccountRepository accountRepositoryMock;
	@Mock
	private UserService userServiceMock;
	@Mock
	private FriendService friendServiceMock;

	@Test
	public void testFindAll() throws Exception {
		// GIVEN
		Transaction t = new Transaction();
		UUID uuid = UUID.randomUUID();
		t.setUser("test@gmail.com");
		t.setId(uuid);
		t.setType("payment");
		List<Transaction> l = new ArrayList<Transaction>();
		l.add(t);

		// WHEN
		when(transactionRepositoryMock.findAll()).thenReturn(l);
		transactionService.setTransactionRepository(transactionRepositoryMock);

		// THEN
		assertThat(transactionService.findAll().isEmpty()).isEqualTo(false);
		assertThat(transactionService.findAll().size()).isEqualTo(1);
	}

	@Test
	public void testFindByTypeWhenTypeIsFalse() throws Exception {
		assertThatThrownBy(() -> transactionService.findByType("deposi")).isInstanceOf(Exception.class)
				.hasMessage("La transaction doit être du type : 'deposit', 'withdrawal' ou 'payment'.");
	}

	@Test
	public void testFindByTypeWhenTypeIsTrue() throws Exception {
		// GIVEN
		Transaction t = new Transaction();
		UUID uuid = UUID.randomUUID();
		t.setUser("test@gmail.com");
		t.setId(uuid);
		t.setType("payment");
		List<Transaction> l = new ArrayList<Transaction>();
		l.add(t);

		// WHEN
		when(transactionRepositoryMock.findByType("deposit")).thenReturn(l);
		transactionService.setTransactionRepository(transactionRepositoryMock);

		// THEN
		assertThat(transactionService.findByType("deposit").size()).isEqualTo(1);
	}

	@Test
	public void testFindByIdWhenIdNotExist() throws Exception {
		// GIVEN
		String id = "2ba3f3c0-6b07-4b3c-bee7-f96643483860";

		// WHEN
		when(transactionRepositoryMock.existsById(UUID.fromString(id))).thenReturn(false);
		transactionService.setTransactionRepository(transactionRepositoryMock);

		// THEN
		assertThatThrownBy(() -> transactionService.findById(id)).isInstanceOf(Exception.class)
				.hasMessage("Aucune transaction ayant cet id existe dans la BDD");
	}

	@Test
	public void testFindByIdWhenIdExist() throws Exception {
		// GIVEN
		String id = "2ba3f3c0-6b07-4b3c-bee7-f96643483860";
		Transaction t = new Transaction();
		UUID uuid = UUID.randomUUID();
		t.setUser("test@gmail.com");
		t.setId(uuid);
		Optional<Transaction> o = Optional.of(t);

		// WHEN
		when(transactionRepositoryMock.existsById(UUID.fromString(id))).thenReturn(true);
		when(transactionRepositoryMock.findById(UUID.fromString(id))).thenReturn(o);
		transactionService.setTransactionRepository(transactionRepositoryMock);

		// THEN
		assertThat(transactionService.findById(id).getUser()).isEqualTo("test@gmail.com");
	}

	@Test
	public void testFindByUserWhenEmailNotExist() throws Exception {
		// GIVEN
		String email = "test@gmail.com";

		// WHEN
		when(userRepositoryMock.findByEmail(email)).thenReturn(null);
		transactionService.setTransactionRepository(transactionRepositoryMock);

		// THEN
		assertThatThrownBy(() -> transactionService.findByUser(email)).isInstanceOf(Exception.class)
				.hasMessage("Email non existant dans la BDD");
	}

	@Test
	public void testFindByUserWhenEmailExist() throws Exception {
		// GIVEN
		String email = "test@gmail.com";
		String friend = "friend@gmail.com";
		List<Transaction> l = new ArrayList<Transaction>();

		// WHEN
		when(userRepositoryMock.existsById(email)).thenReturn(true);
		when(transactionRepositoryMock.findByUserOrFriend(email, friend)).thenReturn(l);
		transactionService.setUserRepository(userRepositoryMock);
		transactionService.setTransactionRepository(transactionRepositoryMock);

		// THEN
		assertThat(transactionService.findByUser(email).size()).isEqualTo(0);
	}

	@Test
	public void testCreatePaymentWhenTypeIsNotPayment() throws Exception {
		// GIVEN
		Transaction transaction = new Transaction();
		UUID uuid = UUID.randomUUID();
		transaction.setUser("test@gmail.com");
		transaction.setId(uuid);
		transaction.setType("paymen");

		// THEN
		assertThatThrownBy(() -> transactionService.createPayment(transaction)).isInstanceOf(Exception.class)
				.hasMessage("La transaction doit être du type : 'payment'.");
	}

	@Test
	public void testCreatePaymentWhenEmailIsNull() throws Exception {
		// GIVEN
		Transaction transaction = new Transaction();
		UUID uuid = UUID.randomUUID();
		transaction.setUser(null);
		transaction.setId(uuid);
		transaction.setType("payment");

		// THEN
		assertThatThrownBy(() -> transactionService.createPayment(transaction)).isInstanceOf(Exception.class)
				.hasMessage("Vous devez renseigner votre email pour effectuer une transaction de paiement !");
	}

	@Test
	public void testCreatePaymentWhenEmailNotExist() throws Exception {
		// GIVEN
		Transaction transaction = new Transaction();
		UUID uuid = UUID.randomUUID();
		transaction.setUser("test@gmail.com");
		transaction.setId(uuid);
		transaction.setType("payment");

		// WHEN
		when(userRepositoryMock.existsById(transaction.getUser())).thenReturn(false);
		transactionService.setUserRepository(userRepositoryMock);

		// THEN
		assertThatThrownBy(() -> transactionService.createPayment(transaction)).isInstanceOf(Exception.class)
				.hasMessage("Votre email utilisateur n'existe pas en BDD. Veuillez le modifier !");
	}

	@Test
	public void testCreatePaymentWhenFriendEmailIsNull() throws Exception {
		// GIVEN
		Transaction transaction = new Transaction();
		UUID uuid = UUID.randomUUID();
		transaction.setFriend(null);
		transaction.setUser("user@gmail.com");
		transaction.setId(uuid);
		transaction.setType("payment");

		// WHEN
		when(userRepositoryMock.existsById(transaction.getUser())).thenReturn(true);
		transactionService.setUserRepository(userRepositoryMock);

		// THEN
		assertThatThrownBy(() -> transactionService.createPayment(transaction)).isInstanceOf(Exception.class)
				.hasMessage("Vous devez renseigner l'email de votre ami pour effectuer une transaction de paiement !");
	}

	@Test
	public void testCreatePaymentWhenFriendEmailNotExist() throws Exception {
		// GIVEN
		Transaction transaction = new Transaction();
		UUID uuid = UUID.randomUUID();
		transaction.setFriend("friend@gmail.com");
		transaction.setUser("user@gmail.com");
		transaction.setId(uuid);
		transaction.setType("payment");

		// WHEN
		when(userRepositoryMock.existsById(transaction.getUser())).thenReturn(true);
		when(userRepositoryMock.existsById(transaction.getFriend())).thenReturn(false);
		transactionService.setUserRepository(userRepositoryMock);

		// THEN
		assertThatThrownBy(() -> transactionService.createPayment(transaction)).isInstanceOf(Exception.class)
				.hasMessage("L'email de votre ami n'existe pas en BDD. Veuillez le modifier !");
	}

	@Test
	public void testCreatePaymentWhenFriendNotConnectWithUser() throws Exception {
		// GIVEN
		Transaction transaction = new Transaction();
		UUID uuid = UUID.randomUUID();
		transaction.setFriend("friend@gmail.com");
		transaction.setUser("user@gmail.com");
		transaction.setId(uuid);
		transaction.setType("payment");
		HashSet<String> h = new HashSet<String>();

		// WHEN
		when(userRepositoryMock.existsById(transaction.getUser())).thenReturn(true);
		when(userRepositoryMock.existsById(transaction.getFriend())).thenReturn(true);
		when(friendServiceMock.findEmailsFriendsOnly(transaction.getUser())).thenReturn(h);
		transactionService.setUserRepository(userRepositoryMock);
		transactionService.setFriendService(friendServiceMock);

		// THEN
		assertThatThrownBy(() -> transactionService.createPayment(transaction)).isInstanceOf(Exception.class)
				.hasMessage("Vous n'êtes pas ami avec l'utilisateur ayant l'email : '" + transaction.getFriend()
						+ "'. Veuillez choisir un de vos amis pour effectuer un paiement !");
	}

	@Test
	public void testCreatePaymentWhenBalanceLow() throws Exception {
		// GIVEN
		Transaction transaction = new Transaction();
		UUID uuid = UUID.randomUUID();
		transaction.setFriend("friend@gmail.com");
		transaction.setUser("user@gmail.com");
		transaction.setId(uuid);
		transaction.setType("payment");
		transaction.setAmount(100000.00);
		HashSet<String> h = new HashSet<String>();
		h.add(transaction.getFriend());
		AppUser a = new AppUser();
		a.setBalance(10.00);

		// WHEN
		when(userRepositoryMock.existsById(transaction.getUser())).thenReturn(true);
		when(userRepositoryMock.existsById(transaction.getFriend())).thenReturn(true);
		when(friendServiceMock.findEmailsFriendsOnly(transaction.getUser())).thenReturn(h);
		when(userRepositoryMock.findByEmail(transaction.getUser())).thenReturn(a);
		transactionService.setUserRepository(userRepositoryMock);
		transactionService.setFriendService(friendServiceMock);

		// THEN
		assertThatThrownBy(() -> transactionService.createPayment(transaction)).isInstanceOf(Exception.class)
				.hasMessage("Vous ne pouvez pas effectuer un paiement (frais compris) : '"
						+ transaction.getAmount() * (1 + Constante.feeTransaction)
						+ "' € supérieur à votre solde disponible : '"
						+ userRepositoryMock.findByEmail(transaction.getUser()).getBalance()
						+ "' €. Veuillez augmenter votre solde disponible depuis votre compte bancaire avant ou réduire la somme à payer !");
	}

	@Test
	public void testCreatePaymentWhenPaymentHigherBalanceMax() throws Exception {
		// GIVEN
		Transaction transaction = new Transaction();
		UUID uuid = UUID.randomUUID();
		transaction.setFriend("friend@gmail.com");
		transaction.setUser("user@gmail.com");
		transaction.setId(uuid);
		transaction.setType("payment");
		transaction.setAmount(10.00);
		HashSet<String> h = new HashSet<String>();
		h.add(transaction.getFriend());
		AppUser a = new AppUser();
		a.setBalance(1000.00);
		AppUser b = new AppUser();
		b.setBalance(99999999.99);

		// WHEN
		when(userRepositoryMock.existsById(transaction.getUser())).thenReturn(true);
		when(userRepositoryMock.existsById(transaction.getFriend())).thenReturn(true);
		when(friendServiceMock.findEmailsFriendsOnly(transaction.getUser())).thenReturn(h);
		when(userRepositoryMock.findByEmail(transaction.getUser())).thenReturn(a);
		when(userRepositoryMock.findByEmail(transaction.getFriend())).thenReturn(b);
		transactionService.setUserRepository(userRepositoryMock);
		transactionService.setFriendService(friendServiceMock);

		// THEN
		assertThatThrownBy(() -> transactionService.createPayment(transaction)).isInstanceOf(Exception.class)
				.hasMessage(
						"Vous devez effectuer un paiement d'un montant inférieur à cet utilisateur pour l'instant !");
	}

	@Test
	public void testCreatePaymentWhenNoError() throws Exception {
		// GIVEN
		Transaction transaction = new Transaction();
		UUID uuid = UUID.randomUUID();
		transaction.setFriend("friend@gmail.com");
		transaction.setUser("user@gmail.com");
		transaction.setId(uuid);
		transaction.setType("payment");
		transaction.setAmount(10.00);
		HashSet<String> h = new HashSet<String>();
		h.add(transaction.getFriend());
		AppUser a = new AppUser();
		a.setBalance(2000.00);
		AppUser b = new AppUser();
		b.setBalance(1000.00);

		// WHEN
		when(userRepositoryMock.existsById(transaction.getUser())).thenReturn(true);
		when(userRepositoryMock.existsById(transaction.getFriend())).thenReturn(true);
		when(friendServiceMock.findEmailsFriendsOnly(transaction.getUser())).thenReturn(h);
		when(userRepositoryMock.findByEmail(transaction.getUser())).thenReturn(a);
		when(userRepositoryMock.findByEmail(transaction.getFriend())).thenReturn(b);
		when(userRepositoryMock.getById(transaction.getUser())).thenReturn(b);
		when(userRepositoryMock.findByEmail(transaction.getUser())).thenReturn(a);
		when(userRepositoryMock.findByEmail(transaction.getFriend())).thenReturn(b);
		when(userServiceMock.updateBalance(transaction.getUser(),
				a.getBalance() - transaction.getAmount() * (1 + Constante.feeTransaction))).thenReturn(a);
		when(userServiceMock.updateBalance(transaction.getFriend(), b.getBalance() + transaction.getAmount()))
				.thenReturn(b);
		when(transactionRepositoryMock.save(transaction)).thenReturn(transaction);
		transactionService.setUserRepository(userRepositoryMock);
		transactionService.setFriendService(friendServiceMock);
		transactionService.setUserService(userServiceMock);
		transactionService.setTransactionRepository(transactionRepositoryMock);

		// THEN
		assertThat(transactionService.createPayment(transaction)).isEqualTo(transaction);
	}

	@Test
	public void testCreateDepositWhenTypeIsNotDeposit() throws Exception {
		// GIVEN
		Transaction transaction = new Transaction();
		UUID uuid = UUID.randomUUID();
		transaction.setUser("test@gmail.com");
		transaction.setId(uuid);
		transaction.setType("deposi");

		// THEN
		assertThatThrownBy(() -> transactionService.createDeposit(transaction)).isInstanceOf(Exception.class)
				.hasMessage("La transaction doit être du type : 'deposit'.");
	}

	@Test
	public void testCreateDepositWhenUserEmailIsNull() throws Exception {
		// GIVEN
		Transaction transaction = new Transaction();
		UUID uuid = UUID.randomUUID();
		transaction.setUser(null);
		transaction.setId(uuid);
		transaction.setType("deposit");

		// THEN
		assertThatThrownBy(() -> transactionService.createDeposit(transaction)).isInstanceOf(Exception.class)
				.hasMessage("Vous devez renseigner votre email pour effectuer une transaction de dépot !");
	}

	@Test
	public void testCreateDepositWhenUserEmailNotExist() throws Exception {
		// GIVEN
		Transaction transaction = new Transaction();
		UUID uuid = UUID.randomUUID();
		transaction.setUser("test@gmail.com");
		transaction.setFriend("");
		transaction.setId(uuid);
		transaction.setType("deposit");

		// WHEN

		when(userRepositoryMock.existsById(transaction.getUser())).thenReturn(false);
		transactionService.setUserRepository(userRepositoryMock);

		// THEN
		assertThatThrownBy(() -> transactionService.createDeposit(transaction)).isInstanceOf(Exception.class)
				.hasMessage("Votre email utilisateur n'existe pas en BDD. Veuillez le modifier !");
	}

	@Test
	public void testCreateDepositWhenFriendExist() throws Exception {
		Transaction transaction = new Transaction();
		Account a = new Account();
		a.setEmail("test1@gmail.com");
		Optional<Account> o = Optional.of(a);
		AppUser au = new AppUser();
		au.setBalance(99999999.99);

		UUID uuid = UUID.randomUUID();
		transaction.setUser("test@gmail.com");
		transaction.setFriend("toto");
		transaction.setId(uuid);
		transaction.setAccountUser(688976);
		transaction.setType("deposit");

		// WHEN

		when(userRepositoryMock.existsById(transaction.getUser())).thenReturn(false);
		when(accountRepositoryMock.findById(transaction.getAccountUser())).thenReturn(o);
		when(userRepositoryMock.existsById(transaction.getUser())).thenReturn(true);
//		when(accountRepositoryMock.findById(transaction.getAccountUser()).get().getEmail()
//				.equals(transaction.getUser())).thenReturn(false);
		transactionService.setUserRepository(userRepositoryMock);
		transactionService.setAccountRepository(accountRepositoryMock);

		// THEN
		assertThatThrownBy(() -> transactionService.createDeposit(transaction)).isInstanceOf(Exception.class)
				.hasMessage("Vous ne devez pas renseigner l'email d'un ami pour effectuer une transaction de dépot !");
	}

	@Test
	public void testCreateDepositWhenAccountUserIsNull() throws Exception {
		Transaction transaction = new Transaction();
		Account a = new Account();
		a.setEmail("test1@gmail.com");
		Optional<Account> o = Optional.of(a);
		AppUser au = new AppUser();
		au.setBalance(99999999.99);

		UUID uuid = UUID.randomUUID();
		transaction.setUser("test@gmail.com");
		transaction.setFriend("");
		transaction.setId(uuid);
//		transaction.setAccountUser(688976);
		transaction.setType("deposit");

		// WHEN

		when(userRepositoryMock.existsById(transaction.getUser())).thenReturn(false);
		when(accountRepositoryMock.findById(transaction.getAccountUser())).thenReturn(o);
		when(userRepositoryMock.existsById(transaction.getUser())).thenReturn(true);
//		when(accountRepositoryMock.findById(transaction.getAccountUser()).get().getEmail()
//				.equals(transaction.getUser())).thenReturn(false);
		transactionService.setUserRepository(userRepositoryMock);
		transactionService.setAccountRepository(accountRepositoryMock);

		// THEN
		assertThatThrownBy(() -> transactionService.createDeposit(transaction)).isInstanceOf(Exception.class)
				.hasMessage("Vous devez renseigner votre compte bancaire pour effectuer une transaction de dépot !");
	}

	@Test
	public void testCreateDepositWhenAccountUserNotExist() throws Exception {
		// GIVEN
		Transaction transaction = new Transaction();
		UUID uuid = UUID.randomUUID();
		transaction.setUser("test@gmail.com");
		transaction.setFriend("");
		transaction.setId(uuid);
		transaction.setType("deposit");
		transaction.setAccountUser(56789);
		Account a = new Account();
		a.setEmail("emailAccount@gmail.com");
		Optional<Account> o = Optional.empty();

		// WHEN
		when(userRepositoryMock.existsById(transaction.getUser())).thenReturn(true);
		when(accountRepositoryMock.findById(transaction.getAccountUser())).thenReturn(o);
		transactionService.setUserRepository(userRepositoryMock);
		transactionService.setAccountRepository(accountRepositoryMock);

		// THEN
		assertThatThrownBy(() -> transactionService.createDeposit(transaction)).isInstanceOf(Exception.class)
				.hasMessage("Le compte bancaire utilisateur : " + transaction.getAccountUser()
						+ " n'existe pas en BDD. Veuillez le modifier !");
	}

	@Test
	public void testCreateDepositWhenAccountUserIsNotForUser() throws Exception {
		// GIVEN
		Transaction transaction = new Transaction();
		Account a = new Account();
		a.setEmail("test1@gmail.com");
		Optional<Account> o = Optional.of(a);
		AppUser au = new AppUser();
		au.setBalance(99999999.99);

		UUID uuid = UUID.randomUUID();
		transaction.setUser("test@gmail.com");
		transaction.setFriend("");
		transaction.setId(uuid);
		transaction.setAccountUser(688976);
		transaction.setType("deposit");

		// WHEN

		when(userRepositoryMock.existsById(transaction.getUser())).thenReturn(false);
		when(accountRepositoryMock.findById(transaction.getAccountUser())).thenReturn(o);
		when(userRepositoryMock.existsById(transaction.getUser())).thenReturn(true);
//		when(accountRepositoryMock.findById(transaction.getAccountUser()).get().getEmail()
//				.equals(transaction.getUser())).thenReturn(false);
		transactionService.setUserRepository(userRepositoryMock);
		transactionService.setAccountRepository(accountRepositoryMock);

		// THEN
		assertThatThrownBy(() -> transactionService.createDeposit(transaction)).isInstanceOf(Exception.class)
				.hasMessage("Votre compte bancaire utilisateur saisi : " + transaction.getAccountUser()
						+ " ne vous appartient pas. Veuillez le modifier !");
	}

	@Test
	public void testCreateDepositWhenBalanceCanNotBeHigherBalanceMax() throws Exception {
		Transaction transaction = new Transaction();
		Account a = new Account();
		a.setEmail("test1@gmail.com");
		Optional<Account> o = Optional.of(a);
		AppUser au = new AppUser();
		au.setBalance(99999999.99);

		UUID uuid = UUID.randomUUID();
		transaction.setUser("test1@gmail.com");
		transaction.setFriend("");
		transaction.setId(uuid);
		transaction.setAccountUser(688976);
		transaction.setType("deposit");
		transaction.setAmount(9999999999.99);

		AppUser aU = new AppUser();
		aU.setBalance(200.0);

		// WHEN

		when(userRepositoryMock.existsById(transaction.getUser())).thenReturn(true);
		when(accountRepositoryMock.findById(transaction.getAccountUser())).thenReturn(o);
		when(accountRepositoryMock.findById(transaction.getAccountUser())).thenReturn(o);
		when(userRepositoryMock.findByEmail(transaction.getUser())).thenReturn(aU);
		when(userRepositoryMock.getById(transaction.getUser())).thenReturn(aU);
		transactionService.setUserRepository(userRepositoryMock);
		transactionService.setAccountRepository(accountRepositoryMock);

		// THEN
		assertThatThrownBy(() -> transactionService.createDeposit(transaction)).isInstanceOf(Exception.class)
				.hasMessage("Votre solde disponible " + userRepositoryMock.getById(transaction.getUser()).getBalance()
						+ " € ne doit pas être supérieur à " + Constante.balanceMax
						+ " € après la transaction de dépôt (frais compris). Veuillez réduire la somme à déposer !");
	}

	@Test
	public void testCreateDepositWhenNoErrorExist() throws Exception {
		// GIVEN
		Transaction transaction = new Transaction();
		Account a = new Account();
		a.setEmail("test1@gmail.com");
		Optional<Account> o = Optional.of(a);
		AppUser au = new AppUser();
		au.setBalance(99999999.99);

		UUID uuid = UUID.randomUUID();
		transaction.setUser("test1@gmail.com");
		transaction.setFriend("");
		transaction.setId(uuid);
		transaction.setAccountUser(688976);
		transaction.setType("deposit");
		transaction.setAmount(100.00);

		AppUser aU = new AppUser();
		aU.setBalance(200.0);

		// WHEN

		when(userRepositoryMock.existsById(transaction.getUser())).thenReturn(true);
		when(accountRepositoryMock.findById(transaction.getAccountUser())).thenReturn(o);
		when(accountRepositoryMock.findById(transaction.getAccountUser())).thenReturn(o);
		when(userRepositoryMock.findByEmail(transaction.getUser())).thenReturn(aU);// CONDITION 199
		when(userRepositoryMock.getById(transaction.getUser())).thenReturn(aU); // IF ERROR 202
		when(userRepositoryMock.findByEmail(transaction.getUser())).thenReturn(aU);// 206

//		when(userServiceMock.updateBalance("test1@gmail.com", 295.00)).thenReturn(aU); ///any(Integer.class), any(Integer.class)
		when(userServiceMock.updateBalance(any(String.class), any(Double.class))).thenReturn(aU);
		// when(userRepositoryMock.existsById("test1@gmail.com")).thenReturn(true);

		when(transactionRepositoryMock.save(any(Transaction.class))).thenReturn(new Transaction());
		transactionService.setUserRepository(userRepositoryMock);
		transactionService.setAccountRepository(accountRepositoryMock);
		transactionService.setUserService(userServiceMock);
		transactionService.setTransactionRepository(transactionRepositoryMock);
		// THEN
		assertThat(transactionService.createDeposit(transaction)).isEqualTo(new Transaction());
	}

	@Test
	public void testCreateWithdrawalWhenTypeIsNotWithdrawal() throws Exception {
		// GIVEN
		Transaction transaction = new Transaction();
		UUID uuid = UUID.randomUUID();
		transaction.setUser("test@gmail.com");
		transaction.setId(uuid);
		transaction.setType("withdrawa");

		// THEN
		assertThatThrownBy(() -> transactionService.createWithdrawal(transaction)).isInstanceOf(Exception.class)
				.hasMessage("La transaction doit être du type : 'withdrawal'.");
	}

	@Test
	public void testCreateWithdrawalWhenEmailUserIsNull() throws Exception {
		// GIVEN
		Transaction transaction = new Transaction();
		UUID uuid = UUID.randomUUID();
		transaction.setUser(null);
		transaction.setFriend("");
		transaction.setId(uuid);
		transaction.setType("withdrawal");

		// THEN
		assertThatThrownBy(() -> transactionService.createWithdrawal(transaction)).isInstanceOf(Exception.class)
				.hasMessage("Vous devez renseigner votre email pour effectuer une transaction de retrait !");
	}

	@Test
	public void testCreateWithdrawalWhenEmailUserNotExist() throws Exception {
		// GIVEN
		Transaction transaction = new Transaction();
		UUID uuid = UUID.randomUUID();
		transaction.setUser("test@gmail.com");
		transaction.setFriend("");
		transaction.setId(uuid);
		transaction.setType("withdrawal");

		// WHEN
		when(userRepositoryMock.existsById(transaction.getUser())).thenReturn(false);
		transactionService.setUserRepository(userRepositoryMock);

		// THEN
		assertThatThrownBy(() -> transactionService.createWithdrawal(transaction)).isInstanceOf(Exception.class)
				.hasMessage("Votre email utilisateur n'existe pas en BDD. Veuillez le modifier !");
	}

	@Test
	public void testCreateWithdrawalWhenAccountUserIsNull() throws Exception {
		// GIVEN
		Transaction transaction = new Transaction();
		UUID uuid = UUID.randomUUID();
		transaction.setUser("test@gmail.com");
		transaction.setFriend("");
		transaction.setId(uuid);
		transaction.setType("withdrawal");
		transaction.setAccountUser(null);

		// WHEN
		when(userRepositoryMock.existsById(transaction.getUser())).thenReturn(true);
		transactionService.setUserRepository(userRepositoryMock);

		// THEN
		assertThatThrownBy(() -> transactionService.createWithdrawal(transaction)).isInstanceOf(Exception.class)
				.hasMessage("Vous devez renseigner votre compte bancaire pour effectuer une transaction de retrait !");
	}

	@Test
	public void testCreateWithdrawalWhenAccountUserNotExist() throws Exception {
		// GIVEN
		Transaction transaction = new Transaction();
		UUID uuid = UUID.randomUUID();
		transaction.setUser("test@gmail.com");
		transaction.setFriend("");
		transaction.setId(uuid);
		transaction.setType("withdrawal");
		transaction.setAccountUser(1234567);

		// WHEN
		when(userRepositoryMock.existsById(transaction.getUser())).thenReturn(true);
		transactionService.setUserRepository(userRepositoryMock);

		// THEN
		assertThatThrownBy(() -> transactionService.createWithdrawal(transaction)).isInstanceOf(Exception.class)
				.hasMessage("Le compte bancaire utilisateur : " + transaction.getAccountUser()
						+ " n'existe pas en BDD. Veuillez le modifier !");
	}

	@Test
	public void testCreateWithdrawalWhenAccountUserIsNotForUser() throws Exception {
		// GIVEN
		Transaction transaction = new Transaction();
		UUID uuid = UUID.randomUUID();
		transaction.setUser("test@gmail.com");
		transaction.setFriend("");
		transaction.setId(uuid);
		transaction.setType("withdrawal");
		transaction.setAccountUser(1234567);
		Account a = new Account();
		a.setEmail("test1@gmail.com");
		Optional<Account> o = Optional.of(a);

		// WHEN
		when(userRepositoryMock.existsById(transaction.getUser())).thenReturn(true);
		when(accountRepositoryMock.findById(transaction.getAccountUser())).thenReturn(o);
		transactionService.setUserRepository(userRepositoryMock);
		transactionService.setAccountRepository(accountRepositoryMock);

		// THEN
		assertThatThrownBy(() -> transactionService.createWithdrawal(transaction)).isInstanceOf(Exception.class)
				.hasMessage("Votre compte bancaire utilisateur saisi : " + transaction.getAccountUser()
						+ " ne vous appartient pas. Veuillez le modifier !");
	}

	@Test
	public void testCreateWithdrawalWhenBalanceLowerBalanceMin() throws Exception {
		// GIVEN
		Transaction transaction = new Transaction();
		UUID uuid = UUID.randomUUID();
		transaction.setUser("test@gmail.com");
		transaction.setFriend("");
		transaction.setId(uuid);
		transaction.setType("withdrawal");
		transaction.setAccountUser(1234567);
		transaction.setAmount(120.00);
		Account a = new Account();
		a.setEmail("test@gmail.com");
		Optional<Account> o = Optional.of(a);
		AppUser au = new AppUser();
		au.setBalance(0.00);

		// WHEN
		when(userRepositoryMock.existsById(transaction.getUser())).thenReturn(true);
		when(accountRepositoryMock.findById(transaction.getAccountUser())).thenReturn(o);
		when(userRepositoryMock.findByEmail(transaction.getUser())).thenReturn(au);
		transactionService.setUserRepository(userRepositoryMock);
		transactionService.setAccountRepository(accountRepositoryMock);

		// THEN
		assertThatThrownBy(() -> transactionService.createWithdrawal(transaction)).isInstanceOf(Exception.class)
				.hasMessage("Votre solde disponible ne doit pas être inférieur à "
						+  Constante.balanceMin + " € après le retrait (frais compris). Réduire la somme à retirer ! ");
	}

	@Test
	public void testCreateWithdrawalWhenNoErrorExist() throws Exception {
		// GIVEN
		Transaction transaction = new Transaction();
		UUID uuid = UUID.randomUUID();
		transaction.setUser("test@gmail.com");
		transaction.setFriend("");
		transaction.setId(uuid);
		transaction.setType("withdrawal");
		transaction.setAccountUser(1234567);
		transaction.setAmount(120.00);
		Account a = new Account();
		a.setEmail("test@gmail.com");
		Optional<Account> o = Optional.of(a);
		AppUser au = new AppUser();
		au.setBalance(2000.00);

		// WHEN
		when(userRepositoryMock.existsById(transaction.getUser())).thenReturn(true);
		when(accountRepositoryMock.findById(transaction.getAccountUser())).thenReturn(o);
		when(userRepositoryMock.findByEmail(transaction.getUser())).thenReturn(au);
		when(userRepositoryMock.findByEmail(transaction.getUser())).thenReturn(au);
		when(userServiceMock.updateBalance(transaction.getUser(),
				au.getBalance() - transaction.getAmount() * (1 + Constante.feeTransaction))).thenReturn(au);
		when(transactionRepositoryMock.save(transaction)).thenReturn(transaction);
		transactionService.setUserRepository(userRepositoryMock);
		transactionService.setAccountRepository(accountRepositoryMock);
		transactionService.setUserService(userServiceMock);
		transactionService.setTransactionRepository(transactionRepositoryMock);

		// THEN
		assertThat(transactionService.createWithdrawal(transaction)).isEqualTo(transaction);
	}

	@Test
	public void testDeleteByIdWhenTransactionNotExist() throws Exception {
		// GIVEN
		Transaction t = new Transaction();
		UUID id = UUID.randomUUID();
		t.setUser("test@gmail.com");
		t.setId(id);
		Optional<Transaction> o = Optional.empty();

		// WHEN
		when(transactionRepositoryMock.findById(id)).thenReturn(o);
		transactionService.setTransactionRepository(transactionRepositoryMock);

		// THEN
		assertThatThrownBy(() -> transactionService.deleteById(id)).isInstanceOf(Exception.class)
				.hasMessage("La transaction n'est pas existante dans la BDD.");
	}

	@Test
	public void testDeleteByIdWhenTransactionExist() throws Exception {
		// GIVEN
		Transaction t = new Transaction();
		UUID id = UUID.randomUUID();
		t.setUser("test@gmail.com");
		t.setId(id);
		Optional<Transaction> o = Optional.of(t);

		// WHEN
		when(transactionRepositoryMock.findById(id)).thenReturn(o);
		when(transactionRepositoryMock.getById(id)).thenReturn(t);
		transactionService.setTransactionRepository(transactionRepositoryMock);

		// THEN
		assertThat(transactionService.deleteById(id)).isEqualTo(t);
	}

}

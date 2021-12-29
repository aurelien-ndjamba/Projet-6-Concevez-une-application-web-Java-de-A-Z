package com.paymybuddy.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.paymybuddy.api.model.Account;
import com.paymybuddy.api.repository.AccountRepository;
import com.paymybuddy.api.repository.UserRepository;

@SpringBootTest
public class AccountServiceTest {
	@Autowired
	private AccountService accountService;

	@Mock
	private AccountRepository accountRepositoryMock;
	@Mock
	private UserRepository userRepositoryMock;
	@Autowired
	private Account account;

	@BeforeEach
	public void initAccount() {
		Integer i = 77777;
		account.setId(i);
		account.setEmail("test@gmail.com");
		account.setBank("BANQUE DU SUD");
	}

	@Test
	public void testFindAllWhenAllIsOk() throws Exception {
		// GIVEN
		List<Account> accounts = new ArrayList<Account>();
		accounts.add(account);

		// WHEN
		when(accountRepositoryMock.findAll()).thenReturn(accounts);
		accountService.setAccountRepository(accountRepositoryMock);

		// THEN
		assertThat(accountService.findAll().size()).isEqualTo(1);
	}

	@Test
	public void testFindByIdWhenCountExist() throws Exception {

		// WHEN
		when(accountRepositoryMock.existsById(77777)).thenReturn(true);
		when(accountRepositoryMock.findById(77777)).thenReturn(account);
		accountService.setAccountRepository(accountRepositoryMock);

		// THEN
		assertThat(accountService.findById(77777).getBank()).isEqualTo("BANQUE DU SUD");
	}

	@Test
	public void testFindByIdWhenCountNotExist() throws Exception {

		// WHEN
		when(accountRepositoryMock.existsById(77777)).thenReturn(false);
		accountService.setAccountRepository(accountRepositoryMock);

		// THEN
		assertThatThrownBy(() -> accountService.findById(77777)).isInstanceOf(Exception.class)
				.hasMessage("Compte bancaire non existant dans la BDD");

	}

	@Test
	public void testFindByEmailWhenEmailExist() throws Exception {
		// GIVEN
		account.setBank("bankTest");

		// WHEN
		when(userRepositoryMock.existsById("test@gmail.com")).thenReturn(true);
		when(accountRepositoryMock.findByEmail("test@gmail.com")).thenReturn(account);
		accountService.setUserRepository(userRepositoryMock);
		accountService.setAccountRepository(accountRepositoryMock);

		// THEN
		assertThat(accountService.findByEmail("test@gmail.com").getBank()).isEqualTo("bankTest");
	}

	@Test
	public void testFindByEmailWhenEmailnotExist() throws Exception {
		// GIVEN
		List<Account> accounts = new ArrayList<Account>();
		accounts.add(account);

		// WHEN
		when(userRepositoryMock.findByEmail("test@gmail.com")).thenReturn(null);
		accountService.setUserRepository(userRepositoryMock);

		// THEN
		assertThatThrownBy(() -> accountService.findByEmail("test@gmail.com")).isInstanceOf(Exception.class)
				.hasMessage("Email non existant dans la BDD");
	}

	@Test
	public void testSaveWhenIdAccountIsNull() throws Exception {
		// GIVEN
		account.setId(null);

		// THEN
		assertThatThrownBy(() -> accountService.save(account)).isInstanceOf(Exception.class)
				.hasMessage("Vous devez renseigner un numéro de compte!");
	}

	@Test
	public void testSaveWhenIdAlreadyPresentInDatabase() throws Exception {
		// WHEN
		when(accountRepositoryMock.existsById(account.getId())).thenReturn(true);
		when(userRepositoryMock.existsById(account.getEmail())).thenReturn(false);
		when(accountRepositoryMock.save(account)).thenReturn(account);
		accountService.setAccountRepository(accountRepositoryMock);
		accountService.setUserRepository(userRepositoryMock);

		// THEN
		assertThatThrownBy(() -> accountService.save(account)).isInstanceOf(Exception.class)
				.hasMessage("Numéro bancaire déjà existant dans la BDD");
	}

	@Test
	public void testSaveWhenEmailAccountIsNull() throws Exception {
		// GIVEN
		account.setEmail(null);

		// THEN
		assertThatThrownBy(() -> accountService.save(account)).isInstanceOf(Exception.class)
				.hasMessage("Vous devez renseigner l'email associé à ce compte bancaire!");
	}

	@Test
	public void testSaveWhenEmailNotExistInDatabase() throws Exception {
		// GIVEN
		Optional<Account> notPresent = Optional.empty();

		// WHEN
		when(accountRepositoryMock.findById(account.getId())).thenReturn(notPresent);
		when(userRepositoryMock.findByEmail(account.getEmail())).thenReturn(null);
		accountService.setAccountRepository(accountRepositoryMock);
		accountService.setUserRepository(userRepositoryMock);

		// THEN
		assertThatThrownBy(() -> accountService.save(account)).isInstanceOf(Exception.class)
				.hasMessage("Email non existant dans la BDD");
	}

	@Test
	public void testSaveWhenBankAccountIsEmpty() throws Exception {
		// GIVEN
		account.setBank(null);

		// WHEN
		when(accountRepositoryMock.existsById(account.getId())).thenReturn(false);
		when(userRepositoryMock.existsById(account.getEmail())).thenReturn(true);
		when(accountRepositoryMock.save(account)).thenReturn(account);
		accountService.setAccountRepository(accountRepositoryMock);
		accountService.setUserRepository(userRepositoryMock);

		// THEN
		assertThatThrownBy(() -> accountService.save(account)).isInstanceOf(Exception.class)
				.hasMessage("Vous devez renseigner le nom de la banque associée à votre compte bancaire!");
	}

	@Test
	public void testSaveWhenNoErrorExist() throws Exception {
		// WHEN
		when(accountRepositoryMock.existsById(account.getId())).thenReturn(false);
		when(userRepositoryMock.existsById(account.getEmail())).thenReturn(true);
		when(accountRepositoryMock.save(account)).thenReturn(account);
		accountService.setAccountRepository(accountRepositoryMock);
		accountService.setUserRepository(userRepositoryMock);

		// THEN
		assertThat(accountService.save(account).getBank()).isEqualTo("BANQUE DU SUD");
	}

	@Test
	public void testUpdateBank() throws Exception {
		// GIVEN
		Account acc = new Account();
		acc.setEmail("toto_email");
		acc.setBank("toto");

		// WHEN
		when(userRepositoryMock.existsById(acc.getEmail())).thenReturn(true);
		when(accountRepositoryMock.findByEmail(acc.getEmail())).thenReturn(new Account());
		when(accountRepositoryMock.save(any(Account.class))).thenReturn(acc);
		accountService.setUserRepository(userRepositoryMock);
		accountService.setAccountRepository(accountRepositoryMock);

		// THEN
		assertThat(accountService.updateBank(acc).getBank()).isEqualTo("toto");
	}

}

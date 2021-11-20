package com.paymybuddy.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.paymybuddy.api.model.Account;
import com.paymybuddy.api.model.AppUser;
import com.paymybuddy.api.repository.AccountRepository;
import com.paymybuddy.api.repository.UserRepository;

@SpringBootTest
public class AccountServiceTest {
	@Autowired
	private Account account;
	@Autowired
	private AppUser appUser;
	@Autowired
	private AccountService accountService;

	@Mock
	private AccountRepository accountRepositoryMock;
	@Mock
	private UserRepository userRepositoryMock;

	@BeforeEach
	public void initAccount() {
		Integer i = 77777;
		account.setId(i);
		account.setEmail("test@gmail.com");
		account.setBank("BANQUE DU SUD");
	}

	@Test
	public void testFindAll() throws Exception {
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
		assertThatThrownBy(() -> accountService.findById(77777)).isInstanceOf(RuntimeException.class)
				.hasMessage("Compte bancaire non existant dans la BDD");

	}

	@Test
	public void testFindByEmailWhenEmailExist() throws Exception {
		// GIVEN
		List<Account> accounts = new ArrayList<Account>();
		accounts.add(account);

		// WHEN
		when(userRepositoryMock.findByEmail("test@gmail.com")).thenReturn(appUser);
		when(accountRepositoryMock.findByEmail("test@gmail.com")).thenReturn(accounts);
		accountService.setUserRepository(userRepositoryMock);
		accountService.setAccountRepository(accountRepositoryMock);

		// THEN
		assertThat(accountService.findByEmail("test@gmail.com").size()).isEqualTo(1);
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
		assertThatThrownBy(() -> accountService.findByEmail("test@gmail.com")).isInstanceOf(RuntimeException.class)
				.hasMessage("Email non existant dans la BDD");
	}

	@Test
	public void testSaveWhenIdAccountIsNull() throws Exception {
		// GIVEN
		account.setId(null);

		// THEN
		assertThatThrownBy(() -> accountService.save(account)).isInstanceOf(RuntimeException.class)
				.hasMessage("Vous devez renseigner un numéro de compte!");
	}

	@Test
	public void testSaveWhenIdAlreadyPresentInDatabase() throws Exception {
		// GIVEN
		Optional<Account> present = Optional.of(account);

		// WHEN
		when(accountRepositoryMock.findById(account.getId())).thenReturn(present);
		accountService.setAccountRepository(accountRepositoryMock);

		// THEN
		assertThatThrownBy(() -> accountService.save(account)).isInstanceOf(RuntimeException.class)
				.hasMessage("Compte bancaire déjà existant dans la BDD");
	}

	@Test
	public void testSaveWhenEmailAccountIsNull() throws Exception {
		// GIVEN
		account.setEmail(null);

		// THEN
		assertThatThrownBy(() -> accountService.save(account)).isInstanceOf(RuntimeException.class)
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
		assertThatThrownBy(() -> accountService.save(account)).isInstanceOf(RuntimeException.class)
				.hasMessage("Email non existant dans la BDD");
	}

	@Test
	public void testSaveWhenBankAccountIsEmpty() throws Exception {
		// GIVEN
		Optional<Account> notAccountPresent = Optional.empty();
		AppUser appUser = new AppUser();
		account.setBank(null);

		// WHEN
		when(accountRepositoryMock.findById(account.getId())).thenReturn(notAccountPresent);
		when(userRepositoryMock.findByEmail(account.getEmail())).thenReturn(appUser);
		accountService.setAccountRepository(accountRepositoryMock);
		accountService.setUserRepository(userRepositoryMock);

		// THEN
		assertThatThrownBy(() -> accountService.save(account)).isInstanceOf(RuntimeException.class)
				.hasMessage("Vous devez renseigner le nom de la banque associée à votre compte bancaire!");
	}

	@Test
	public void testSaveWhenNoErrorExist() throws Exception {
		// GIVEN
		Optional<Account> notAccountPresent = Optional.empty();
		AppUser appUser = new AppUser();

		// WHEN
		when(accountRepositoryMock.findById(account.getId())).thenReturn(notAccountPresent);
		when(userRepositoryMock.findByEmail(account.getEmail())).thenReturn(appUser);
		when(accountRepositoryMock.save(account)).thenReturn(account);
		accountService.setAccountRepository(accountRepositoryMock);
		accountService.setUserRepository(userRepositoryMock);

		// THEN
		assertThat(accountService.save(account).getBank()).isEqualTo("BANQUE DU SUD");
	}

	@Test
	public void testUpdateWhenIdAccountIsNull() throws Exception {
		// GIVEN
		account.setId(null);

		// THEN
		assertThatThrownBy(() -> accountService.update(account)).isInstanceOf(RuntimeException.class)
		.hasMessage("Vous devez renseigner un numéro de compte!");
	}
	
	@Test
	public void testUpdateWhenIdAccountNotExistInDatabase() throws Exception {
		// GIVEN
		Optional<Account> empty = Optional.empty();

		// WHEN
		when(accountRepositoryMock.findById(account.getId())).thenReturn(empty);
		accountService.setAccountRepository(accountRepositoryMock);
		
		// THEN
		assertThatThrownBy(() -> accountService.update(account)).isInstanceOf(RuntimeException.class)
		.hasMessage("Compte bancaire non existant dans la BDD");
	}
	
	@Test
	public void testUpdateWhenEmailIsNull() throws Exception {
		// GIVEN
		Optional<Account> o = Optional.of(account);
		account.setEmail(null);

		// WHEN
		when(accountRepositoryMock.findById(account.getId())).thenReturn(o);
		accountService.setAccountRepository(accountRepositoryMock);
		
		// THEN
		assertThatThrownBy(() -> accountService.update(account)).isInstanceOf(RuntimeException.class)
		.hasMessage("Vous devez renseigner l'email associé à ce compte bancaire!");
	}
	
	@Test
	public void testUpdateWhenEmailNotExistInDatabase() throws Exception {
		// GIVEN
		Optional<Account> o = Optional.of(account);

		// WHEN
		when(accountRepositoryMock.findById(account.getId())).thenReturn(o);
		when(userRepositoryMock.findByEmail(account.getEmail())).thenReturn(null);
		accountService.setAccountRepository(accountRepositoryMock);
		
		// THEN
		assertThatThrownBy(() -> accountService.update(account)).isInstanceOf(RuntimeException.class)
		.hasMessage("Email non existant dans la BDD");
	}
	
	@Test
	public void testUpdateWhenBankIsNull() throws Exception {
		// GIVEN
		Optional<Account> o = Optional.of(account);
		account.setBank(null);
		AppUser appUser = new AppUser();

		// WHEN
		when(accountRepositoryMock.findById(account.getId())).thenReturn(o);
		when(userRepositoryMock.findByEmail(account.getEmail())).thenReturn(appUser);
		accountService.setAccountRepository(accountRepositoryMock);
		accountService.setUserRepository(userRepositoryMock);
		
		// THEN
		assertThatThrownBy(() -> accountService.update(account)).isInstanceOf(RuntimeException.class)
		.hasMessage("Vous devez renseigner le nom de la banque associée à votre compte bancaire!");
	}
	
	@Test
	public void testUpdateWhenNoErrorExist() throws Exception {
		// GIVEN
		Optional<Account> o = Optional.of(account);
		AppUser appUser = new AppUser();

		// WHEN
		when(accountRepositoryMock.findById(account.getId())).thenReturn(o);
		when(userRepositoryMock.findByEmail(account.getEmail())).thenReturn(appUser);
		when(accountRepositoryMock.save(account)).thenReturn(account);
		accountService.setAccountRepository(accountRepositoryMock);
		accountService.setUserRepository(userRepositoryMock);
		
		// THEN
		assertThat(accountService.update(account).getBank()).isEqualTo("BANQUE DU SUD");
	}

	@Test
	public void testDeleteByIdWhenIdDontExistInDatabase() throws Exception {
		// GIVEN
		Optional<Account> empty = Optional.empty();

		// WHEN
		when(accountRepositoryMock.findById(account.getId())).thenReturn(empty);
		accountService.setAccountRepository(accountRepositoryMock);

		// THEN
		assertThatThrownBy(() -> accountService.deleteById(account.getId())).isInstanceOf(RuntimeException.class)
				.hasMessage("Compte bancaire non existant dans la BDD");
	}

	@Test
	public void testDeleteByIdWhenIdExistInDatabase() throws Exception {
		// GIVEN
		Optional<Account> o = Optional.of(account);

		// WHEN
		when(accountRepositoryMock.findById(account.getId())).thenReturn(o);
		accountService.setAccountRepository(accountRepositoryMock);

		// THEN
		assertThat(accountService.deleteById(account.getId()).getBank()).isEqualTo("BANQUE DU SUD");
	}
}

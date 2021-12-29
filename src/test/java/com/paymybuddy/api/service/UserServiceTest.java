package com.paymybuddy.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.paymybuddy.api.model.AppUser;
import com.paymybuddy.api.repository.UserRepository;

@SpringBootTest
public class UserServiceTest {

	@Autowired
	private UserService userService;

	@Mock
	private UserRepository userRepositoryMock;
	@Mock
	private BCryptPasswordEncoder bCryptPasswordEncoderMock;
	@Mock
	private FriendService friendServiceMock;

	@Test
	public void testFindAll() throws Exception {
		// GIVEN
		AppUser u1 = new AppUser();
		u1.setBalance(180.0);
		u1.setEmail("test1@gmail.com");
		u1.setPassword("password1");
		List<AppUser> ul1 = new ArrayList<AppUser>();
		ul1.add(u1);

		// WHEN
		when(userRepositoryMock.findAll()).thenReturn(ul1);
		userService.setUserRepository(userRepositoryMock);

		// THEN
		assertThat(userService.findAll().size()).isEqualTo(1);
	}

	@Test
	public void testFindByEmailWhenEmailNotExist() throws Exception {
		// WHEN
		when(userRepositoryMock.existsById("test@gmail.com")).thenReturn(false);
		userService.setUserRepository(userRepositoryMock);

		// THEN
		assertThatThrownBy(() -> userService.findByEmail("test@gmail.com")).isInstanceOf(Exception.class)
				.hasMessage("Email de l'utilisateur non existant dans la BDD");
	}

	@Test
	public void testFindByEmailWhenNoError() throws Exception {
		// GIVEN
		AppUser u1 = new AppUser();
		u1.setBalance(180.0);
		u1.setEmail("test1@gmail.com");
		u1.setPassword("password1");

		// WHEN
		when(userRepositoryMock.existsById("test1@gmail.com")).thenReturn(true);
		when(userRepositoryMock.findByEmail("test1@gmail.com")).thenReturn(u1);
		userService.setUserRepository(userRepositoryMock);

		// THEN
		assertThat(userService.findByEmail("test1@gmail.com")).isEqualTo(u1);
	}

	@Test
	public void testFindOtherUsersWithoutThisEmailWhenEmailNotExist() throws Exception {
		// WHEN
		when(userRepositoryMock.existsById("test@gmail.com")).thenReturn(false);
		userService.setUserRepository(userRepositoryMock);

		// THEN
		assertThatThrownBy(() -> userService.findOtherUsersWithoutThisEmail("test@gmail.com"))
				.isInstanceOf(Exception.class).hasMessage("Email de l'utilisateur non existant dans la BDD");
	}
	
	@Test
	public void testFindOtherUsersWithoutThisEmailWhenNoErrorExist() throws Exception {
		// GIVEN
		AppUser u = new AppUser();
		u.setBalance(180.0);
		u.setEmail("test@gmail.com");
		u.setPassword("password");
		String email = "toto@gmail.com";
		List<AppUser> appUsersList = new ArrayList<AppUser>();
		appUsersList.add(u);
		
		// WHEN
		when(userRepositoryMock.existsById(email)).thenReturn(true);
		when(userRepositoryMock.findAll()).thenReturn(appUsersList);
		when(userRepositoryMock.findByEmail(email)).thenReturn(new AppUser());
		userService.setUserRepository(userRepositoryMock);

		// THEN
		assertThat(userService.findOtherUsersWithoutThisEmail(email).get(0)).isEqualTo(u);
	}

	@Test
	public void testSaveWhenEmailAlreadyExist() throws Exception {
		// GIVEN
		AppUser u = new AppUser();
		u.setBalance(180.0);
		u.setEmail("test@gmail.com");
		u.setPassword("password");

		// WHEN
		when(userRepositoryMock.existsById("test@gmail.com")).thenReturn(true);
		userService.setUserRepository(userRepositoryMock);

		// THEN
		assertThatThrownBy(() -> userService.save(u)).isInstanceOf(Exception.class)
				.hasMessage("Email de l'utilisateur déjà existant dans la BDD");
	}

	@Test
	public void testUpdateAllWhenEmailIsNull() throws Exception {
		// GIVEN
		AppUser u = new AppUser();

		// THEN
		assertThatThrownBy(() -> userService.updateAll(u)).isInstanceOf(Exception.class)
				.hasMessage("Vous devez renseigner l'email de l'utilisateur pour la mise à jour dans la BDD");
	}

	@Test
	public void testUpdateAllWhenPasswordIsNull() throws Exception {
		// GIVEN
		AppUser u = new AppUser();
		u.setBalance(180.0);
		u.setEmail("test@gmail.com");
		//		u.setPassword("password");
		//WHEN
		when(userRepositoryMock.existsById("test@gmail.com")).thenReturn(true);
		when(userRepositoryMock.getById("test@gmail.com")).thenReturn(u);
		userService.setUserRepository(userRepositoryMock);
		// THEN
		assertThatThrownBy(() -> userService.updateAll(u)).isInstanceOf(Exception.class)
				.hasMessage("Vous devez renseigner le nouveau mot de passe à mettre à jour dans la BDD");
	}

	@Test
	public void testUpdateAllWhenBalanceIsNull() throws Exception {
		// GIVEN
		AppUser u = new AppUser();
		u.setEmail("test@gmail.com");
		u.setPassword("password");

		// THEN
		assertThatThrownBy(() -> userService.updateAll(u)).isInstanceOf(Exception.class)
				.hasMessage("Vous devez renseigner le nouveau solde à mettre à jour dans la BDD");
	}

	@Test
	public void testUpdateAllWhenEmailNotExist() throws Exception {
		// GIVEN
		AppUser u = new AppUser();
		u.setEmail("test@gmail.com");
		u.setPassword("password");
		u.setBalance(180.0);

		// WHEN
		when(userRepositoryMock.existsById("test@gmail.com")).thenReturn(false);
//		when(userRepositoryMock.save(u)).thenReturn(u);
		userService.setUserRepository(userRepositoryMock);

		// THEN
		assertThatThrownBy(() -> userService.updateAll(u)).isInstanceOf(Exception.class)
				.hasMessage("Email de l'utilisateur non existant dans la BDD");
	}

	@Test
	public void testUpdateAllWhenNoErrorExist() throws Exception {
		// GIVEN
		AppUser u = new AppUser();
		u.setEmail("test@gmail.com");
		u.setPassword("password");
		u.setBalance(180.0);

		Optional<AppUser> o = Optional.of(u);

		// WHEN
		when(userRepositoryMock.existsById("test@gmail.com")).thenReturn(true);
		when(userRepositoryMock.findById(u.getEmail())).thenReturn(o);
		when(userRepositoryMock.save(u)).thenReturn(u);
		userService.setUserRepository(userRepositoryMock);

		// THEN
		assertThat(userService.updateAll(u).getPassword()).isNotEqualTo("password");

	}

	@Test
	public void testUpdatePasswordWhenNoErrorExist() throws Exception {
		// GIVEN
		AppUser u = new AppUser();
		u.setBalance(180.0);
		u.setEmail("test@gmail.com");
		u.setPassword("password");

		// WHEN
		when(userRepositoryMock.existsById("test@gmail.com")).thenReturn(true);
		when(userRepositoryMock.getById("test@gmail.com")).thenReturn(u);
		when(userRepositoryMock.save(u)).thenReturn(u);
		userService.setUserRepository(userRepositoryMock);

		// THEN
		assertThat(userService.updatePassword("test@gmail.com", "newP@ssword").getPassword()).isNotEqualTo("password");
	}
	
	@Test
	public void testLoginWhenNoErrorExist() throws Exception {
		// GIVEN
		String email = "test@gmail.com";
		String password = "password1";
		AppUser u = new AppUser();
		u.setBalance(180.0);
		u.setEmail(email);
		u.setPassword("password");
		u.setActive(true);
		Optional<AppUser> o = Optional.of(u);

		// WHEN
		when(userRepositoryMock.existsById(email)).thenReturn(true);
		when(userRepositoryMock.findById(email)).thenReturn(o);
		when(bCryptPasswordEncoderMock.matches(password, u.getPassword())).thenReturn(true);
		when(userRepositoryMock.getById(email)).thenReturn(u);
		when(userRepositoryMock.save(u)).thenReturn(u);
		userService.setUserRepository(userRepositoryMock);
		userService.setbCryptPasswordEncoder(bCryptPasswordEncoderMock);

		// THEN
		assertThat(userService.login(email, password)).isEqualTo(true);
	}
	
	@Test
	public void testLoginWhenEmailNotExist() throws Exception {
		// GIVEN
		String email = null;
		String password = "password1";
		AppUser u = new AppUser();
		u.setBalance(180.0);
		u.setEmail(email);
		u.setPassword("password");
		u.setActive(true);
		Optional<AppUser> o = Optional.of(u);

		// WHEN
		when(userRepositoryMock.existsById(email)).thenReturn(false);
		when(userRepositoryMock.findById(email)).thenReturn(o);
		when(bCryptPasswordEncoderMock.matches(password, u.getPassword())).thenReturn(true);
		when(userRepositoryMock.getById(email)).thenReturn(u);
		when(userRepositoryMock.save(u)).thenReturn(u);
		userService.setUserRepository(userRepositoryMock);
		userService.setbCryptPasswordEncoder(bCryptPasswordEncoderMock);

		// THEN
		assertThatThrownBy(() -> userService.login(email, password)).isInstanceOf(Exception.class)
		.hasMessage("Email de l'utilisateur non existant dans la BDD");
	}
	
	@Test
	public void testUpdatePhoneWhenNoErrorExist() throws Exception {
		// GIVEN
		String email = "test@gmail.com";
		AppUser u = new AppUser();
		u.setBalance(180.0);
		u.setEmail(email);
		u.setPassword("password");
		u.setActive(true);

		// WHEN
		when(userRepositoryMock.existsById(email)).thenReturn(true);
		when(userRepositoryMock.getById(email)).thenReturn(new AppUser());
		when(userRepositoryMock.save(any(AppUser.class))).thenReturn(u);
		userService.setUserRepository(userRepositoryMock);

		// THEN
		assertThat(userService.updatePhone("test@gmail.com", 101).isActive()).isEqualTo(true);
	}
	
	@Test
	public void testUpdatePhoneWhenEmailIsNull() throws Exception {
		// GIVEN
		String email=null;
		AppUser u = new AppUser();
		u.setBalance(180.0);
		u.setEmail(email);
		u.setPassword("password");
		u.setActive(true);

		// WHEN
		when(userRepositoryMock.existsById(email)).thenReturn(false);
		when(userRepositoryMock.getById(email)).thenReturn(new AppUser());
		when(userRepositoryMock.save(u)).thenReturn(u);
		userService.setUserRepository(userRepositoryMock);

		// THEN
		assertThatThrownBy(() -> userService.updatePhone(email, 101)).isInstanceOf(Exception.class)
		.hasMessage("Vous devez renseigner l'email de l'utilisateur pour la mise à jour dans la BDD");
	}
	
	@Test
	public void testUpdatePhoneWhenEmailNotExist() throws Exception {
		// GIVEN
		String email="email@gmail.com";
		AppUser u = new AppUser();
		u.setBalance(180.0);
		u.setEmail(email);
		u.setPassword("password");
		u.setActive(true);

		// WHEN
		when(userRepositoryMock.existsById(email)).thenReturn(false);
		when(userRepositoryMock.getById(email)).thenReturn(new AppUser());
		when(userRepositoryMock.save(u)).thenReturn(u);
		userService.setUserRepository(userRepositoryMock);

		// THEN
		assertThatThrownBy(() -> userService.updatePhone(email, 101)).isInstanceOf(Exception.class)
		.hasMessage("Email de l'utilisateur non existant dans la BDD");
	}

	
	@Test
	public void testUpdateActiveWhenNoErrorExist() throws Exception {
		// GIVEN
		String email = "test@gmail.com";
		AppUser u = new AppUser();
		u.setBalance(180.0);
		u.setEmail(email);
		u.setPassword("password");
		u.setActive(true);

		// WHEN
		when(userRepositoryMock.existsById(email)).thenReturn(true);
		when(userRepositoryMock.getById(email)).thenReturn(new AppUser());
		when(userRepositoryMock.save(any(AppUser.class))).thenReturn(u);
		userService.setUserRepository(userRepositoryMock);

		// THEN
		assertThat(userService.updateActive(email, false).isActive()).isEqualTo(true);
	}
	
	@Test
	public void testUpdateActiveWhenEmailIsNull() throws Exception {
		// GIVEN
		String email=null;
		AppUser u = new AppUser();
		u.setBalance(180.0);
		u.setEmail(email);
		u.setPassword("password");
		u.setActive(true);

		// WHEN
		when(userRepositoryMock.existsById(email)).thenReturn(false);
		when(userRepositoryMock.getById(email)).thenReturn(new AppUser());
		when(userRepositoryMock.save(u)).thenReturn(u);
		userService.setUserRepository(userRepositoryMock);

		// THEN
		assertThatThrownBy(() -> userService.updateActive(email, true)).isInstanceOf(Exception.class)
		.hasMessage("Vous devez renseigner l'email de l'utilisateur pour la mise à jour dans la BDD");
	}
	
	@Test
	public void testUpdateActiveWhenEmailNotExist() throws Exception {
		// GIVEN
		String email="email@gmail.com";
		AppUser u = new AppUser();
		u.setBalance(180.0);
		u.setEmail(email);
		u.setPassword("password");
		u.setActive(true);

		// WHEN
		when(userRepositoryMock.existsById(email)).thenReturn(false);
		when(userRepositoryMock.getById(email)).thenReturn(new AppUser());
		when(userRepositoryMock.save(u)).thenReturn(u);
		userService.setUserRepository(userRepositoryMock);

		// THEN
		assertThatThrownBy(() -> userService.updateActive(email, true)).isInstanceOf(Exception.class)
		.hasMessage("Email de l'utilisateur non existant dans la BDD");
	}

	@Test
	public void testUpdateBalanceWhenEmailIsNull() throws Exception {
		// GIVEN
		AppUser u = new AppUser();
		u.setBalance(180.0);
//				u.setEmail("test@gmail.com");
		u.setPassword("password");
		// WHEN
		when(userRepositoryMock.existsById("test@gmail.com")).thenReturn(true);
		when(userRepositoryMock.getById("test@gmail.com")).thenReturn(u);
		userService.setUserRepository(userRepositoryMock);
		// THEN
		assertThatThrownBy(() -> userService.updateBalance(null, 200.57)).isInstanceOf(Exception.class)
				.hasMessage("Vous devez renseigner l'email de l'utilisateur pour la mise à jour dans la BDD");
	}

	@Test
	public void testUpdateBalanceWhenBalanceIsNull() throws Exception {
		assertThatThrownBy(() -> userService.updateBalance("test@gmail.com", null)).isInstanceOf(Exception.class)
				.hasMessage("Vous devez renseigner le nouveau solde à mettre à jour dans la BDD");
	}

	@Test
	public void testUpdateBalanceWhenEmailNotExist() throws Exception {
		// WHEN
		when(userRepositoryMock.existsById("test@gmail.com")).thenReturn(false);
		userService.setUserRepository(userRepositoryMock);

		// THEN
		assertThatThrownBy(() -> userService.updateBalance("test@gmail.com", 200.57))
				.isInstanceOf(Exception.class).hasMessage("Email de l'utilisateur non existant dans la BDD");
	}

	@Test
	public void testUpdateBalanceWhenNoErrorExist() throws Exception {
		// GIVEN
		AppUser u = new AppUser();
		u.setBalance(180.0);
		u.setEmail("test@gmail.com");
		u.setPassword("password");

		// WHEN
		when(userRepositoryMock.existsById("test@gmail.com")).thenReturn(true);
		when(userRepositoryMock.getById("test@gmail.com")).thenReturn(u);
		when(userRepositoryMock.save(u)).thenReturn(u);
		userService.setUserRepository(userRepositoryMock);

		// THEN
		assertThat(userService.updateBalance("test@gmail.com", 200.57).getBalance()).isEqualTo(200.57);
	}

	@Test
	public void testDeleteByIdWhenEmailNotExistInDatabase() throws Exception {
		// WHEN
		when(userRepositoryMock.existsById("test@gmail.com")).thenReturn(false);
		userService.setUserRepository(userRepositoryMock);

		// THEN
		assertThatThrownBy(() -> userService.deleteById("test@gmail.com")).isInstanceOf(Exception.class)
				.hasMessage("Email de l'utilisateur non existant dans la BDD");
	}

	@Test
	public void testDeleteByIdWhenEmailExistInDatabase() throws Exception {
		// GIVEN
		AppUser u = new AppUser();
		u.setBalance(180.0);
		u.setEmail("test@gmail.com");
		u.setPassword("password");

		// WHEN
		when(userRepositoryMock.existsById("test@gmail.com")).thenReturn(true);
		when(userRepositoryMock.findByEmail("test@gmail.com")).thenReturn(u);
		userService.setUserRepository(userRepositoryMock);

		// THEN
		assertThat(userService.deleteById("test@gmail.com").getBalance()).isEqualTo(180.0);
	}

}

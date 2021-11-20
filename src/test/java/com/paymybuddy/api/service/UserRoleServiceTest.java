package com.paymybuddy.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.paymybuddy.api.model.AppUser;
import com.paymybuddy.api.model.UserRole;
import com.paymybuddy.api.model.UserRoleId;
import com.paymybuddy.api.repository.RoleRepository;
import com.paymybuddy.api.repository.UserRepository;
import com.paymybuddy.api.repository.UserRoleRepository;

@SpringBootTest
public class UserRoleServiceTest {

	@Autowired
	private UserRoleService userRoleService;

	@Mock
	private UserRoleRepository userRoleRepositoryMock;
	@Mock
	private UserRepository userRepositoryMock;
	@Mock
	private RoleRepository roleRepositoryMock;

	@Test
	public void testFindAll() throws Exception {
		// GIVEN
		List<UserRole> uRoles = new ArrayList<UserRole>();

		// WHEN
		when(userRoleRepositoryMock.findAll()).thenReturn(uRoles);
		userRoleService.setUserRoleRepository(userRoleRepositoryMock);

		// THEN
		assertThat(userRoleService.findAll().size()).isEqualTo(0);
	}

	@Test
	public void testFindByEmailWhenEmailNotExist() throws Exception {
		// GIVEN
		String email = "test@gmail.com";

		// WHEN
		when(userRepositoryMock.existsById(email)).thenReturn(false);
		userRoleService.setUserRepository(userRepositoryMock);

		// THEN
		assertThatThrownBy(() -> userRoleService.findByEmail(email)).isInstanceOf(RuntimeException.class)
				.hasMessage("Email non existant dans la BDD");
	}

	@Test
	public void testFindByEmailWhenEmailExist() throws Exception {
		// GIVEN
		String email = "test@gmail.com";
		UserRole ur = new UserRole();
		List<UserRole> urs = new ArrayList<UserRole>();
		urs.add(ur);

		// WHEN
		when(userRepositoryMock.existsById(email)).thenReturn(true);
		when(userRoleRepositoryMock.findByEmail(email)).thenReturn(urs);
		userRoleService.setUserRepository(userRepositoryMock);
		userRoleService.setUserRoleRepository(userRoleRepositoryMock);

		// THEN
		assertThat(userRoleService.findByEmail(email).size()).isEqualTo(1);
	}

	@Test
	public void testSaveWhenEmailIsNull() throws Exception {
		// GIVEN
		UserRole ur = new UserRole();

		// WHEN
		ur.setRoleName("USER");

		// THEN
		assertThatThrownBy(() -> userRoleService.save(ur)).isInstanceOf(RuntimeException.class)
				.hasMessage("Vous devez renseigner l'email.");
	}

	@Test
	public void testSaveWhenRoleIsNull() throws Exception {
		// GIVEN
		UserRole ur = new UserRole();

		// WHEN
		ur.setEmail("test@gmail.com");

		// THEN
		assertThatThrownBy(() -> userRoleService.save(ur)).isInstanceOf(RuntimeException.class)
				.hasMessage("Vous devez renseigner le role.");
	}

	@Test
	public void testSaveWhenEmailNotExist() throws Exception {
		// GIVEN
		UserRole userRole = new UserRole();
		userRole.setEmail("test@gmail.com");
		userRole.setRoleName("USER");

		// WHEN
		when(userRepositoryMock.findByEmail(userRole.getEmail())).thenReturn(null);
		userRoleService.setUserRepository(userRepositoryMock);

		// THEN
		assertThatThrownBy(() -> userRoleService.save(userRole)).isInstanceOf(RuntimeException.class)
				.hasMessage("L'email renseigné n'existe pas dans la BDD");
	}

	@Test
	public void testSaveWhenUserRoleAlreadyExist() throws Exception {
		// GIVEN
		AppUser au = new AppUser();
		UserRole userRole = new UserRole();
		userRole.setEmail("test@gmail.com");
		userRole.setRoleName("USER");

		// WHEN
		UserRoleId userRoleId = new UserRoleId(userRole.getEmail(), userRole.getRoleName());
		when(userRepositoryMock.findByEmail(userRole.getEmail())).thenReturn(au);
		when(userRoleRepositoryMock.existsById(userRoleId)).thenReturn(true);
		userRoleService.setUserRepository(userRepositoryMock);
		userRoleService.setUserRoleRepository(userRoleRepositoryMock);

		// THEN
		assertThatThrownBy(() -> userRoleService.save(userRole)).isInstanceOf(RuntimeException.class)
				.hasMessage("Relation UserRole déjà existante dans la BDD");
	}

	@Test
	public void testSaveWhenUserRoleNotExist() throws Exception {
		// GIVEN
		AppUser au = new AppUser();
		UserRole userRole = new UserRole();
		userRole.setEmail("test@gmail.com");
		userRole.setRoleName("USER");

		// WHEN
		UserRoleId userRoleId = new UserRoleId(userRole.getEmail(), userRole.getRoleName());
		when(userRepositoryMock.findByEmail(userRole.getEmail())).thenReturn(au);
		when(userRoleRepositoryMock.existsById(userRoleId)).thenReturn(false);
		when(roleRepositoryMock.existsById(userRole.getRoleName())).thenReturn(false);
		userRoleService.setUserRepository(userRepositoryMock);
		userRoleService.setUserRoleRepository(userRoleRepositoryMock);
		userRoleService.setRoleRepository(roleRepositoryMock);

		// THEN
		assertThatThrownBy(() -> userRoleService.save(userRole)).isInstanceOf(RuntimeException.class)
				.hasMessage("Role inexistant dans la BDD");
	}

	@Test
	public void testSaveWhenNoErrorExist() throws Exception {
		// GIVEN
		AppUser au = new AppUser();
		UserRole userRole = new UserRole();
		userRole.setEmail("test@gmail.com");
		userRole.setRoleName("USER");

		// WHEN
		UserRoleId userRoleId = new UserRoleId(userRole.getEmail(), userRole.getRoleName());
		when(userRepositoryMock.findByEmail(userRole.getEmail())).thenReturn(au);
		when(userRoleRepositoryMock.existsById(userRoleId)).thenReturn(false);
		when(roleRepositoryMock.existsById(userRole.getRoleName())).thenReturn(true);
		when(userRoleRepositoryMock.save(userRole)).thenReturn(userRole);
		userRoleService.setUserRepository(userRepositoryMock);
		userRoleService.setUserRoleRepository(userRoleRepositoryMock);
		userRoleService.setRoleRepository(roleRepositoryMock);

		// THEN
		assertThat(userRoleService.save(userRole).getRoleName()).isEqualTo("USER");
	}

	@Test
	public void testDeleteWhenEmailIsNull() throws Exception {
		// GIVEN
		String email = null;
		String role = "USER";

		// THEN
		assertThatThrownBy(() -> userRoleService.delete(email, role)).isInstanceOf(RuntimeException.class)
				.hasMessage("L'email renseigné n'existe pas dans la BDD");
	}

	@Test
	public void testDeleteWhenRoleNotExist() throws Exception {
		// GIVEN
		AppUser a = new AppUser();
		String email = "test@gmail.com";
		String role = "USER";

		// WHEN
		when(userRepositoryMock.findByEmail(email)).thenReturn(a);
		when(roleRepositoryMock.existsById(role)).thenReturn(false);
		userRoleService.setUserRepository(userRepositoryMock);
		userRoleService.setRoleRepository(roleRepositoryMock);

		// THEN
		assertThatThrownBy(() -> userRoleService.delete(email, role)).isInstanceOf(RuntimeException.class)
				.hasMessage("Le role renseigné n'existe pas dans la BDD");
	}
	
	@Test
	public void testDeleteWhenUserRoleNotExist() throws Exception {
		// GIVEN
		AppUser a = new AppUser();
		String email = "test@gmail.com";
		String role = "USER";

		// WHEN
		when(userRepositoryMock.findByEmail(email)).thenReturn(a);
		when(roleRepositoryMock.existsById(role)).thenReturn(true);
		when(userRoleRepositoryMock.existsById(new UserRoleId(email,role))).thenReturn(false);
		userRoleService.setUserRepository(userRepositoryMock);
		userRoleService.setRoleRepository(roleRepositoryMock);
		userRoleService.setUserRoleRepository(userRoleRepositoryMock);

		// THEN
		assertThatThrownBy(() -> userRoleService.delete(email, role)).isInstanceOf(RuntimeException.class)
				.hasMessage("Relation UserRole non existante dans la BDD");
	}
	
	@Test
	public void testDeleteWhenNoErrorExist() throws Exception {
		// GIVEN
		AppUser a = new AppUser();
		String email = "test@gmail.com";
		String role = "USER";

		// WHEN
		when(userRepositoryMock.findByEmail(email)).thenReturn(a);
		when(roleRepositoryMock.existsById(role)).thenReturn(true);
		when(userRoleRepositoryMock.existsById(new UserRoleId(email,role))).thenReturn(true);
		userRoleService.setUserRepository(userRepositoryMock);
		userRoleService.setRoleRepository(roleRepositoryMock);
		userRoleService.setUserRoleRepository(userRoleRepositoryMock);

		// THEN
		assertThat(userRoleService.delete(email, role).getRoleName()).isEqualTo("USER");
	}
	
}

package com.paymybuddy.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.paymybuddy.api.model.Role;
import com.paymybuddy.api.repository.RoleRepository;

@SpringBootTest
public class RoleServiceTest {

	@Autowired
	private RoleService roleService;

	@Mock
	private RoleRepository roleRepositorymock;

	@Test
	public void testFindAll() throws Exception {
		// GIVEN
		List<Role> roles = new ArrayList<Role>();

		// WHEN
		when(roleRepositorymock.findAll()).thenReturn(roles);
		roleService.setRoleRepository(roleRepositorymock);

		// THEN
		assertThat(roleService.findAll().size()).isEqualTo(0);
	}

	@Test
	public void testFindByIdWhenRoleNotExist() throws Exception {
		// GIVEN
		String id = "idTest";

		// WHEN
		when(roleRepositorymock.existsById(id)).thenReturn(false);
		roleService.setRoleRepository(roleRepositorymock);

		// THEN
		assertThatThrownBy(() -> roleService.findById(id)).isInstanceOf(RuntimeException.class)
				.hasMessage("Role non existant dans la BDD");
	}

	@Test
	public void testFindByIdWhenRoleExist() throws Exception {
		// GIVEN
		String id = "idTest";
		Role r = new Role();
		Optional<Role> o = Optional.of(r);

		// WHEN
		when(roleRepositorymock.existsById(id)).thenReturn(true);
		when(roleRepositorymock.findById(id)).thenReturn(o);
		roleService.setRoleRepository(roleRepositorymock);

		// THEN
		assertThat(roleService.findById(id)).isEqualTo(r);
	}

	@Test
	public void testSaveWhenRoleAlreadyExist() throws Exception {
		// GIVEN
		Role r = new Role();

		// WHEN
		when(roleRepositorymock.existsById(r.getRoleName())).thenReturn(true);
		roleService.setRoleRepository(roleRepositorymock);

		// THEN
		assertThatThrownBy(() -> roleService.save(r)).isInstanceOf(RuntimeException.class)
				.hasMessage("Role déjà existant dans la BDD");
	}

	@Test
	public void testSaveWhenRoleNotExist() throws Exception {
		// GIVEN
		Role r = new Role();

		// WHEN
		when(roleRepositorymock.existsById(r.getRoleName())).thenReturn(false);
		when(roleRepositorymock.save(r)).thenReturn(r);
		roleService.setRoleRepository(roleRepositorymock);

		// THEN
		assertThat(roleService.save(r)).isEqualTo(r);
	}

	@Test
	public void testDeleteByIdWhenRoleNotExist() throws Exception {
		// GIVEN
		String id = "idTest";

		// WHEN
		when(roleRepositorymock.existsById(id)).thenReturn(false);
		roleService.setRoleRepository(roleRepositorymock);

		// THEN
		assertThatThrownBy(() -> roleService.deleteById(id)).isInstanceOf(RuntimeException.class)
				.hasMessage("Role non existant dans la BDD");
	}
	
	@Test
	public void testDeleteByIdWhenRoleExist() throws Exception {
		// GIVEN
		String id = "idTest";
		Role r = new Role();
		Optional<Role> o = Optional.of(r);

		// WHEN
		when(roleRepositorymock.existsById(id)).thenReturn(true);
		when(roleRepositorymock.findById(id)).thenReturn(o);
		roleService.setRoleRepository(roleRepositorymock);

		// THEN
		assertThat(roleService.deleteById(id)).isEqualTo(r);
	}
}

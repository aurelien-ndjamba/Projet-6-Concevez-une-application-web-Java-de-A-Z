package com.paymybuddy.api.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.paymybuddy.api.model.UserRole;
import com.paymybuddy.api.service.UserRoleService;

@WebMvcTest(controllers = UserRoleController.class)
public class UserRoleControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private UserRoleController userRoleController;

	@MockBean
	private UserRoleService userRoleServiceMock;

	@Test
	public void testFindAll() throws Exception {
		// GIVEN
		UserRole r = new UserRole();
		r.setRoleName("USER");
		r.setEmail("test@gmail.com");
		List<UserRole> rs = new ArrayList<UserRole>();
		rs.add(r);

		// WHEN
		when(userRoleServiceMock.findAll()).thenReturn(rs);
		userRoleController.setUserRoleService(userRoleServiceMock);

		// THEN
		mockMvc.perform(get("/userroles/all")).andExpect(status().isOk()).andExpect(jsonPath("$[0].email", is("test@gmail.com")));
	}
	
	@Test
	public void testFindByEmail() throws Exception {
		// GIVEN
		String email = "test@gmail.com";
		UserRole r = new UserRole();
		r.setRoleName("USER");
		r.setEmail("test@gmail.com");
		List<UserRole> rs = new ArrayList<UserRole>();
		rs.add(r);

		// WHEN
		when(userRoleServiceMock.findByEmail(email)).thenReturn(rs);
		userRoleController.setUserRoleService(userRoleServiceMock);

		// THEN
		mockMvc.perform(get("/userroles?email=test@gmail.com")).andExpect(status().isOk()).andExpect(jsonPath("$[0].roleName", is("USER")));
	}
	
	@Test
	public void testSave() throws Exception {
		// GIVEN
		UserRole r = new UserRole();
		r.setRoleName("USER");
		r.setEmail("test@gmail.com");
		List<UserRole> rs = new ArrayList<UserRole>();
		rs.add(r);

		String userRoleInString = "{ \"email\":\"test@gmail.com\",\"roleName\":\"USER\"}";

		// WHEN
		when(userRoleServiceMock.save(r)).thenReturn(r);
		userRoleController.setUserRoleService(userRoleServiceMock);

		// THEN
		MockHttpServletRequestBuilder req = post("/userroles/save").contentType(MediaType.APPLICATION_JSON)
				.content(userRoleInString);
		mockMvc.perform(req).andExpect(status().isCreated()).andExpect(jsonPath("$.email", is("test@gmail.com")));
	}
	
	@Test
	public void testDeleteById() throws Exception {
		// GIVEN
		String email = "test@gmail.com";
		String role = "USER";
		UserRole r = new UserRole();
		r.setRoleName("USER");
		r.setEmail("test@gmail.com");

		// WHEN
		when(userRoleServiceMock.delete(email,role)).thenReturn(r);
		userRoleController.setUserRoleService(userRoleServiceMock);

		// THEN
		mockMvc.perform(delete("/userroles/delete?email=test@gmail.com&role=USER")).andExpect(status().isOk()).andExpect(jsonPath("$.roleName", is("USER")));
		}
	
}

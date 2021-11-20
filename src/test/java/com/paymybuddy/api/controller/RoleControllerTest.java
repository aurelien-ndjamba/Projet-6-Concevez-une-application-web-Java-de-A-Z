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

import com.paymybuddy.api.model.Role;
import com.paymybuddy.api.service.RoleService;

@WebMvcTest(controllers = RoleController.class)
public class RoleControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private RoleController roleController;

	@MockBean
	private RoleService roleServiceMock;

	@Test
	public void testFindAll() throws Exception {
		// GIVEN
		Role r = new Role();
		r.setRoleName("USER");
		List<Role> rs = new ArrayList<Role>();
		rs.add(r);

		// WHEN
		when(roleServiceMock.findAll()).thenReturn(rs);
		roleController.setRoleService(roleServiceMock);

		// THEN
		mockMvc.perform(get("/roles/all")).andExpect(status().isOk()).andExpect(jsonPath("$[0].roleName", is("USER")));
	}
	
	@Test
	public void testFindById() throws Exception {
		// GIVEN
		String id = "123";
		Role r = new Role();
		r.setRoleName("USER");

		// WHEN
		when(roleServiceMock.findById(id)).thenReturn(r);
		roleController.setRoleService(roleServiceMock);

		// THEN
		mockMvc.perform(get("/roles?id=123")).andExpect(status().isOk()).andExpect(jsonPath("$.roleName", is("USER")));
	}
	
	@Test
	public void testSave() throws Exception {
		// GIVEN
		Role r = new Role();
		r.setRoleName("USER");
		String roleInString = "{ \"roleName\":\"USER\"}";

		// WHEN
		when(roleServiceMock.save(r)).thenReturn(r);
		roleController.setRoleService(roleServiceMock);

		// THEN
		MockHttpServletRequestBuilder req = post("/roles/save").contentType(MediaType.APPLICATION_JSON)
				.content(roleInString);
		mockMvc.perform(req).andExpect(status().isCreated()).andExpect(jsonPath("$.roleName", is("USER")));
	}
	
	@Test
	public void testDeleteById() throws Exception {
		// GIVEN
		String id = "123";
		Role r = new Role();
		r.setRoleName("USER");

		// WHEN
		when(roleServiceMock.deleteById(id)).thenReturn(r);
		roleController.setRoleService(roleServiceMock);

		// THEN
		mockMvc.perform(delete("/roles/delete?id=123")).andExpect(status().isOk()).andExpect(jsonPath("$.roleName", is("USER")));
		}

}

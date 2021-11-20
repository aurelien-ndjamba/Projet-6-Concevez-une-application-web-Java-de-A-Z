package com.paymybuddy.api.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.paymybuddy.api.model.AppUser;
import com.paymybuddy.api.model.Role;
import com.paymybuddy.api.model.Transaction;
import com.paymybuddy.api.service.UserService;

@WebMvcTest(controllers = AppUserController.class)
public class AppUserControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private AppUserController appUserController;

	@MockBean
	private UserService userServiceMock;

	@Test
	public void testFindAll() throws Exception {
		// GIVEN
		AppUser appUser = new AppUser();
		appUser.setEmail("nicolas.sarkozy@gmail.com");
		List<AppUser> appUsers = new ArrayList<>();
		appUsers.add(appUser);

		// WHEN
		when(userServiceMock.findAll()).thenReturn(appUsers);
		appUserController.setUserService(userServiceMock);

		// THEN
		mockMvc.perform(get("/users/all")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].email", is("nicolas.sarkozy@gmail.com")));
	}

	@Test
	public void testFindByEmail() throws Exception {
		// GIVEN
		String email = "nicolas.sarkozy@gmail.com";
		AppUser appUser = new AppUser();
		appUser.setEmail(email);

		// WHEN
		when(userServiceMock.findByEmail(email)).thenReturn(appUser);
		appUserController.setUserService(userServiceMock);

		// THEN
		mockMvc.perform(get("/users/user?email=nicolas.sarkozy@gmail.com")).andExpect(status().isOk())
				.andExpect(jsonPath("$.email", is("nicolas.sarkozy@gmail.com")));
	}

	@Test
	public void testFindOtherUsersWithoutThisEmail() throws Exception {
		// GIVEN
		String email = "nicolas.sarkozy@gmail.com";
		AppUser appUser = new AppUser();
		appUser.setEmail(email);
		List<AppUser> appUsers = new ArrayList<>();
		appUsers.add(appUser);

		// WHEN
		when(userServiceMock.findOtherUsersWithoutThisEmail(email)).thenReturn(appUsers);
		appUserController.setUserService(userServiceMock);

		// THEN
		mockMvc.perform(get("/users/otherusers?email=nicolas.sarkozy@gmail.com")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].email", is("nicolas.sarkozy@gmail.com")));
	}

	@Test
	@Disabled
	public void testSave() throws Exception {
		// GIVEN
		AppUser u = new AppUser();
		u.setEmail("nicolas.sarkozy@gmail.com");
		AppUser u2 = new AppUser();
		u2.setEmail("test@gmail.com");
//		u.setPassword("testNewMot2Pass");
//		u.setBalance(180.50);
		String uInString = "{\"email\":\"nicolas.sarkozy@gmail.com\"}";
		
//		Role r = new Role();
//		r.setRoleName("USER");
//		List<Role> rs = new ArrayList<Role>();
//		rs.add(r);
//		AppUser uOutput = new AppUser();
//		uOutput.setBalance(0.0);
//		uOutput.setEmail("nicolas.sarkozy@gmail.com");
//		uOutput.setRoles(rs);
		
		// WHEN
		when(userServiceMock.save(u)).thenReturn(u2);
		appUserController.setUserService(userServiceMock);

		// THEN
		MockHttpServletRequestBuilder req = post("/users/createuser").contentType(MediaType.APPLICATION_JSON)
				.content(uInString);
		mockMvc.perform(req).andExpect(status().isCreated()).andExpect(jsonPath("$.email", is("test@gmail.com")));
	}

	@Test
	@Disabled
	public void testUpdateAll() throws Exception {
		// GIVEN
		AppUser appUser = new AppUser();
		appUser.setEmail("nicolas.sarkozy@gmail.com");
		appUser.setBalance(2000.57);
		String appUserInString = "{\"email\":\"nicolas.sarkozy@gmail.com\",\"balance\":2000.57}";

		// WHEN
//		when(appUserControllerMock.updateAll(appUser)).thenReturn(appUser);

		// THEN
		MockHttpServletRequestBuilder req = put("/users/updateall").contentType(MediaType.APPLICATION_JSON)
				.content(appUserInString);
		mockMvc.perform(req).andExpect(status().isOk()).andExpect(jsonPath("$.balance", is(2000.57)));
	}

	@Test
	@Disabled
	public void testUpdatePassword() throws Exception {
		// GIVEN
		AppUser u = new AppUser();
		u.setBalance(180.0);
		u.setEmail("test@gmail.com");
		u.setPassword("password");
		String userInString = "{ \"email\":\"test@gmail.com\", \"password\":\"testNewMot2Pass\", \"balance\":180.0 }";

		// WHEN
//		when(appUserControllerMock.updatePassword("test@gmail.com", "testNewMot2Pass")).thenReturn(u);

		// THEN
		MockHttpServletRequestBuilder req = put("/users/updatepassword").contentType(MediaType.APPLICATION_JSON)
				.content(userInString);
		mockMvc.perform(req).andExpect(status().isOk()).andExpect(jsonPath("$", is("test@gmail.com,testNewMot2Pass")));
	}

	@Test
	@Disabled
	public void testUpdateBalance() throws Exception {
		// GIVEN
		String email = "test@gmail.com";
		Double balance = 180.0;
		AppUser u = new AppUser();
		u.setBalance(balance);
		u.setEmail(email);
		u.setPassword("testNewMot2Pass");
		String userInString = "{ \"email\":\"test@gmail.com\", \"password\":\"testNewMot2Pass\", \"balance\":180.0 }";

		// WHEN
		when(userServiceMock.updateBalance(email, balance)).thenReturn(u);
		appUserController.setUserService(userServiceMock);
		
		// THEN
		MockHttpServletRequestBuilder req = put("/users/updatebalance").contentType(MediaType.APPLICATION_JSON)
				.content(userInString);
		mockMvc.perform(req).andExpect(status().isOk()).andExpect(jsonPath("$", is(u)));
	}

	@Test
	public void testDeleteById() throws Exception {
		// GIVEN
		String email = "test@gmail.com";
		AppUser u = new AppUser();
		u.setBalance(180.50);
		u.setEmail(email);
		u.setPassword("testNewMot2Pass");

		// WHEN
		when(userServiceMock.deleteById(email)).thenReturn(u);
		appUserController.setUserService(userServiceMock);

		// THEN
		mockMvc.perform(delete("/users/delete?email=test@gmail.com")).andExpect(status().isOk())
				.andExpect(jsonPath("balance", is(180.50)));
	}

}

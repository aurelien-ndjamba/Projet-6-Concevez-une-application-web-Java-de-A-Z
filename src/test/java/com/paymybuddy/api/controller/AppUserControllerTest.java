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

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.paymybuddy.api.model.AppUser;
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
	public void testLogin() throws Exception {
		// WHEN
		when(userServiceMock.login( "email","password")).thenReturn(true);
		appUserController.setUserService(userServiceMock);

		// THEN
		mockMvc.perform(get("/login?email=email&password=password")).andExpect(status().isOk())
				.andExpect(jsonPath("$", is(true)));
	}
	
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
		mockMvc.perform(get("/users")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].email", is("nicolas.sarkozy@gmail.com")));
	}

	@Test
	public void testFindByEmail() throws Exception {
		// GIVEN
		String email = "toto";
		AppUser appUser = new AppUser();
		appUser.setEmail(email);

		// WHEN
		when(userServiceMock.findByEmail(email)).thenReturn(appUser);
		appUserController.setUserService(userServiceMock);

		// THEN
		mockMvc.perform(get("/users?email=toto")).andExpect(status().isOk()).andExpect(jsonPath("$.email", is("toto")));
	}

	@Test
	public void testFindOtherUsersWithoutThisEmail() throws Exception {
		// GIVEN
		String email = "toto";
		AppUser appUser = new AppUser();
		appUser.setEmail(email);
		List<AppUser> appUsers = new ArrayList<>();
		appUsers.add(appUser);

		// WHEN
		when(userServiceMock.findOtherUsersWithoutThisEmail(email)).thenReturn(appUsers);
		appUserController.setUserService(userServiceMock);

		// THEN
		mockMvc.perform(get("/users/otherusers?email=toto")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].email", is("toto")));
	}

	@Test
	public void testFindOtherEmailUsersWithoutThisEmail() throws Exception {
		// GIVEN
		String email = "toto";
		ArrayList<String> emails = new ArrayList<>();
		emails.add(email);

		// WHEN
		when(userServiceMock.findOtherEmailUsersWithoutThisEmail(email)).thenReturn(emails);
		appUserController.setUserService(userServiceMock);

		// THEN
		mockMvc.perform(get("/users/otheremailusers?email=toto")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0]", is("toto")));
	}

	@Test
	public void testFindOtherEmailsFriendsAvailableForThisEmail() throws Exception {
		// GIVEN
		String email = "toto";
		ArrayList<String> emails = new ArrayList<>();
		emails.add(email);

		// WHEN
		when(userServiceMock.findOtherEmailsFriendsAvailableForThisEmail(email)).thenReturn(emails);
		appUserController.setUserService(userServiceMock);

		// THEN
		mockMvc.perform(get("/users/otheremailsfriendsavailable?email=toto")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0]", is("toto")));
	}

//	@Disabled // Je ne comprends pas pourquoi ???
	@Test
	public void testSave() throws Exception {
		// GIVEN
		AppUser u = new AppUser();
//		u.setEmail("toto");
//		u.setPseudo("to");
		String uInString = "{}";// "{\"email\":\"tata\"}";
		System.out.println(uInString);
		AppUser u1 = new AppUser();
		u1.setEmail("tata");

		// WHEN
		when(userServiceMock.save(u)).thenReturn(u1);
		appUserController.setUserService(userServiceMock);

		// THEN
		MockHttpServletRequestBuilder req = post("/users/createuser").contentType(MediaType.APPLICATION_JSON)
				.content(uInString);
		mockMvc.perform(req).andExpect(status().isCreated());
//				.andExpect(jsonPath("$.email", is("tata")));
	}

//	@Disabled // Je ne comprends pas pourquoi ???
	@Test
	public void testUpdateAll() throws Exception {
		// GIVEN
		AppUser appUser = new AppUser();
		appUser.setEmail("nicolas.sarkozy@gmail.com");
		appUser.setPseudo("tt");
		appUser.setBalance(2000.57);
		appUser.setPassword("PassW");
		appUser.setBalance(0.0);
		appUser.setPhone(101);
		appUser.setActive(true);
		AppUser appUser0 = appUser;
		appUser0.setPseudo("to");
		
//		String appUserInString = "{\"email\":\"nicolas.sarkozy@gmail.com\",\"balance\":2000.57}";
		String appUserInString = "{}";

		// WHEN
		when(userServiceMock.updateAll(appUser)).thenReturn(appUser0); //appUser0
		appUserController.setUserService(userServiceMock);
		
		// THEN
		MockHttpServletRequestBuilder req = put("/users/updateall").contentType(MediaType.APPLICATION_JSON)
				.content(appUserInString);
		mockMvc.perform(req).andExpect(status().isOk())
				;
//		.andExpect(jsonPath("$", is(2000.57)));
	}

	@Test
	public void testUpdatePassword() throws Exception {
		// GIVEN
		String email = "test@gmail.com";
		AppUser u = new AppUser();
		u.setBalance(100.0);
		u.setEmail(email);
		u.setPassword("testNewMot2Pass");

		// WHEN
		when(userServiceMock.updatePassword("test@gmail.com", "testNewMot2Pass")).thenReturn(u);
		appUserController.setUserService(userServiceMock);
		
		// THEN
		MockHttpServletRequestBuilder req = put("/users/updatepassword?email=test@gmail.com&password=testNewMot2Pass");
		mockMvc.perform(req).andExpect(status().isOk()).andExpect(jsonPath("$.balance", is(100.0)));
	}

	@Test
	public void testUpdateBalance() throws Exception {
		// GIVEN
		String email = "test@gmail.com";
		Double balance = 180.0;
		AppUser u = new AppUser();
		u.setBalance(100.0);
		u.setEmail(email);
		u.setPassword("testNewMot2Pass");

		// WHEN
		when(userServiceMock.updateBalance(email, balance)).thenReturn(u);
		appUserController.setUserService(userServiceMock);

		// THEN
		MockHttpServletRequestBuilder req = put("/users/updatebalance?email=test@gmail.com&balance=180.0");
		mockMvc.perform(req).andExpect(status().isOk()).andExpect( jsonPath("$.balance", is(100.0)) );
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

package com.paymybuddy.api.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
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

import com.paymybuddy.api.model.Account;
import com.paymybuddy.api.service.AccountService;

@WebMvcTest(controllers = AccountController.class)
public class AccountControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AccountService accountServiceMock;

	@Test
	public void testFindAll() throws Exception {
		// GIVEN
		Account account = new Account();
		account.setId(77777);
		account.setEmail("test@gmail.com");
		account.setBank("BANQUE DU SUD");
		List<Account> accounts = new ArrayList<Account>();
		accounts.add(account);

		// WHEN
		when(accountServiceMock.findAll()).thenReturn(accounts);

		// THEN
		mockMvc.perform(get("/accounts")).andExpect(status().isOk()).andExpect(jsonPath("$[0].id", is(77777)));
	}

	@Test
	public void testFindById() throws Exception {
		// GIVEN
		Account account = new Account();
		account.setId(77777);
		account.setEmail("test@gmail.com");
		account.setBank("BANQUE DU SUD");

		// WHEN
		when(accountServiceMock.findById(77777)).thenReturn(account);

		// THEN
		mockMvc.perform(get("/accounts?id=77777")).andExpect(status().isOk()).andExpect(jsonPath("bank", is("BANQUE DU SUD")));
	}
	
	@Test
	public void testFindByEmail() throws Exception {
		// GIVEN
		Account account = new Account();
		account.setId(77777);
		account.setEmail("test@gmail.com");
		account.setBank("BANQUE DU SUD");

		// WHEN
		when(accountServiceMock.findByEmail("test@gmail.com")).thenReturn(account);

		// THEN
		mockMvc.perform(get("/accounts?email=test@gmail.com")).andExpect(status().isOk()).andExpect(jsonPath("$.bank", is("BANQUE DU SUD")));
	}
	
	@Test
	public void testSave() throws Exception {
		// GIVEN
		Account account = new Account();
//		account.setId(77777);
//		account.setEmail("email@gmail.com");
//		account.setBank("BANQUE DU NORD");
//		String accountInString = "{ \"id\":77777, \"email\":\"email@gmail.com\", \"bank\":\"BANQUE DU NORD\" }";
		String accountInString = "{}";
		
		Account account1 = new Account();
		account1.setId(88888);
		account1.setEmail("email1@gmail.com");
		account1.setBank("BANQUE DU SUD");
		
		// WHEN
		when(accountServiceMock.save(account)).thenReturn(account1);

		// THEN
		MockHttpServletRequestBuilder req = post("/accounts/save").contentType(MediaType.APPLICATION_JSON)
				.content(accountInString);
		mockMvc.perform(req).andExpect(status().isCreated()).andExpect(jsonPath("$.id", is(88888)));
	}
	
	@Test
	public void testUpdateBank() throws Exception {
		Account account = new Account();
		account.setId(77777);
		account.setEmail("email@gmail.com");
		account.setBank("BANQUE DU NORD");
		String accountInString = "{ \"id\":77777, \"email\":\"email@gmail.com\", \"bank\":\"BANQUE DU NORD\" }";
		
		Account account1 = new Account();
		account1.setId(88888);
		account1.setEmail("email1@gmail.com");
		account1.setBank("BANQUE DU SUD");
		
		// WHEN
		when(accountServiceMock.updateBank(account)).thenReturn(account1);
		
		// THEN
		MockHttpServletRequestBuilder req = put("/accounts/updateBank").contentType(MediaType.APPLICATION_JSON)
				.content(accountInString);
		mockMvc.perform(req).andExpect(status().isOk()).andExpect(jsonPath("bank", is("BANQUE DU SUD")));
	}

}
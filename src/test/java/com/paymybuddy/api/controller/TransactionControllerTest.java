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
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.paymybuddy.api.model.Transaction;
import com.paymybuddy.api.service.TransactionService;

@WebMvcTest(controllers = TransactionController.class)
public class TransactionControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private TransactionController transactionController;

	@MockBean
	private TransactionService transactionServiceMock;

	@Test
	public void testFindAll() throws Exception {
		// GIVEN
		List<Transaction> l = new ArrayList<Transaction>();
		Transaction t = new Transaction();
		UUID uuid = UUID.randomUUID();
		t.setId(uuid);
		t.setType("payment");
		l.add(t);

		// WHEN
		when(transactionServiceMock.findAll()).thenReturn(l);
		transactionController.setTransactionService(transactionServiceMock);

		// THEN
		mockMvc.perform(get("/transactions/all")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id", is(uuid.toString())));
	}

	@Test
	public void testFindById() throws Exception {
		// GIVEN
		String id ="123";
		Transaction t = new Transaction();
		UUID uuid = UUID.randomUUID();
		t.setId(uuid);
		t.setType("payment");

		// WHEN
		when(transactionServiceMock.findById(id)).thenReturn(t);
		transactionController.setTransactionService(transactionServiceMock);

		// THEN
		mockMvc.perform(get("/transactions?id=123")).andExpect(status().isOk())
				.andExpect(jsonPath("type", is("payment")));
	}

	@Test
	public void testFindByUser() throws Exception {
		// GIVEN
		String email ="test@gmail.com";
		Transaction t = new Transaction();
		UUID uuid = UUID.randomUUID();
		t.setUser("test@gmail.com");
		t.setId(uuid);
		t.setType("payment");
		List<Transaction> l = new ArrayList<Transaction>();
		l.add(t);

		// WHEN
		when(transactionServiceMock.findByUser(email)).thenReturn(l);
		transactionController.setTransactionService(transactionServiceMock);

		// THEN
		mockMvc.perform(get("/transactions?email=test@gmail.com")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].type", is("payment")));
	}

	@Test
	public void testFindByType() throws Exception {
		// GIVEN
		String type ="payment";
		Transaction t = new Transaction();
		UUID uuid = UUID.randomUUID();
		t.setUser("test@gmail.com");
		t.setId(uuid);
		t.setType("payment");
		List<Transaction> l = new ArrayList<Transaction>();
		l.add(t);

		// WHEN
		when(transactionServiceMock.findByType(type)).thenReturn(l);
		transactionController.setTransactionService(transactionServiceMock);

		// THEN
		mockMvc.perform(get("/transactions?type=payment")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id", is(uuid.toString())));
	}

	@Test
	public void testCreatePayment() throws Exception {
		// GIVEN
		Transaction t = new Transaction();
		UUID uuid = UUID.randomUUID();
		String uuidInString = uuid.toString();
		t.setUser("test@gmail.com");
		t.setId(uuid);
		t.setType("payment");
		String transactionInString = "{ \"id\":\"" + uuidInString
				+ "\",\"user\":\"test@gmail.com\",\"type\":\"payment\"}";

		// WHEN
		when(transactionServiceMock.createPayment(t)).thenReturn(t);
		transactionController.setTransactionService(transactionServiceMock);
		
		// THEN
		MockHttpServletRequestBuilder req = post("/transactions/createpayment").contentType(MediaType.APPLICATION_JSON)
				.content(transactionInString);
		mockMvc.perform(req).andExpect(status().isCreated()).andExpect(jsonPath("$.type", is("payment")));
	}

	@Test
	public void testCreateDeposit() throws Exception {
		// GIVEN
		Transaction t2 = new Transaction();
		t2.setUser("test2@gmail.com");
		Transaction t = new Transaction();
		t.setUser("test@gmail.com");
		t.setType("deposit");
		String t2InString = "{ \"user\":\"test2@gmail.com\"}";

		// WHEN
		when(transactionServiceMock.createDeposit(t2)).thenReturn(t);
		transactionController.setTransactionService(transactionServiceMock);
		
		// THEN
		MockHttpServletRequestBuilder req = post("/transactions/createdeposit").contentType(MediaType.APPLICATION_JSON)
				.content(t2InString);
		mockMvc.perform(req).andExpect(status().isCreated()).andExpect(jsonPath("$.user", is("test@gmail.com")));
	}

	@Test
	public void testCreateWithdrawal() throws Exception {
		// GIVEN
		Transaction t = new Transaction();
		UUID uuid = UUID.randomUUID();
		String uuidInString = uuid.toString();
		t.setUser("test@gmail.com");
		t.setId(uuid);
		t.setType("withdrawal");
		String transactionInString = "{ \"id\":\"" + uuidInString
				+ "\",\"user\":\"test@gmail.com\",\"type\":\"withdrawal\"}";

		// WHEN
		when(transactionServiceMock.createWithdrawal(t)).thenReturn(t);
		transactionController.setTransactionService(transactionServiceMock);
		
		// THEN
		MockHttpServletRequestBuilder req = post("/transactions/createwithdrawal").contentType(MediaType.APPLICATION_JSON)
				.content(transactionInString);
		mockMvc.perform(req).andExpect(status().isCreated()).andExpect(jsonPath("$.type", is("withdrawal")));
	}

	@Test
	public void testDeleteAllById() throws Exception {
		// GIVEN
		Transaction t = new Transaction();
		UUID uuid = UUID.randomUUID();
		t.setId(uuid);
		t.setType("payment");

		// WHEN
		when(transactionServiceMock.deleteById(uuid)).thenReturn(t);
		transactionController.setTransactionService(transactionServiceMock);

		// THEN
		mockMvc.perform(delete("/transactions/delete?id=" + uuid)).andExpect(status().isOk())
				.andExpect(jsonPath("$.type", is("payment")));
	}

}

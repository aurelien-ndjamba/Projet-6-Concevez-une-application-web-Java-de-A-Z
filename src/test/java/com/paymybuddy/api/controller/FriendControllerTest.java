package com.paymybuddy.api.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import com.paymybuddy.api.model.Friend;
import com.paymybuddy.api.service.FriendService;

@WebMvcTest(controllers = FriendController.class)
public class FriendControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private FriendService friendServiceMock;

	@Test
	public void testFindAll() throws Exception {
		// GIVEN
		List<Friend> fs = new ArrayList<Friend>();
		Friend f = new Friend();
		f.setEmailUser("user@gmail.com");
		f.setEmailFriend("friend@gmail.com");
		fs.add(f);

		// WHEN
		when(friendServiceMock.findAll()).thenReturn(fs);

		// THEN
		mockMvc.perform(get("/friends")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].emailFriend", is("friend@gmail.com")));
	}

	@Test
	public void testFindByEmail() throws Exception {
		// GIVEN
		List<Friend> fs = new ArrayList<Friend>();
		fs.add(new Friend("toto", "tata"));

		// WHEN
		when(friendServiceMock.findByEmail("test@gmail.com")).thenReturn(fs);

		// THEN
		mockMvc.perform(get("/friends?email=test@gmail.com")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].emailFriend", is("tata")));
	}

	@Test
	public void testFindEmailsFriendsOnly() throws Exception {
		// GIVEN
		HashSet<String> fs = new HashSet<String>();
		fs.add("friend1@gmail.com");

		// WHEN
		when(friendServiceMock.findEmailsFriendsOnly("test@gmail.com")).thenReturn(fs);

		// THEN
		mockMvc.perform(get("/emailsfriendsonly?email=test@gmail.com")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0]", is("friend1@gmail.com")));
	}

	@Test
	public void testFindPseudosFriendsOnly() throws Exception {
		// GIVEN
		HashSet<String> fs = new HashSet<String>();
		fs.add("friend1@gmail.com");

		// WHEN
		when(friendServiceMock.findPseudosFriendsOnly("test@gmail.com")).thenReturn(fs);

		// THEN
		mockMvc.perform(get("/pseudosfriends?email=test@gmail.com")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0]", is("friend1@gmail.com")));
	}

	@Test
	public void testPageFindPseudosFriendsOnly() throws Exception {
		// GIVEN
		HashSet<String> fs = new HashSet<String>();
		fs.add("friend1@gmail.com");

		// WHEN
		when(friendServiceMock.findPseudosFriendsOnly("test@gmail.com", 5, 2)).thenReturn(fs);

		// THEN
		mockMvc.perform(get("/page_pseudosfriends?email=test@gmail.com&page=5&size=2")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0]", is("friend1@gmail.com")));
	}

	@Test
	public void testFindEmailsavailable() throws Exception {
		// GIVEN
		HashSet<String> fs = new HashSet<String>();
		fs.add("friend1@gmail.com");

		// WHEN
		when(friendServiceMock.findEmailsavailable("test@gmail.com")).thenReturn(fs);

		// THEN
		mockMvc.perform(get("/friends/emailsavailable?email=test@gmail.com")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0]", is("friend1@gmail.com")));
	}

	@Test
	public void testFindPseudoByEmail() throws Exception {
		// WHEN
		when(friendServiceMock.findPseudoByEmail("test@gmail.com")).thenReturn("toto");

		// THEN
		mockMvc.perform(get("/pseudoByEmail?email=test@gmail.com")).andExpect(status().isOk())
				.andExpect(jsonPath("$", is("toto")));
	}

	@Test
	public void testFindEmailByPseudo() throws Exception {
		// WHEN
		when(friendServiceMock.findEmailByPseudo("test@gmail.com")).thenReturn("toto");

		// THEN
		mockMvc.perform(get("/emailByPseudo?pseudo=test@gmail.com")).andExpect(status().isOk())
				.andExpect(jsonPath("$", is("toto")));
	}

	@Test
	public void testSave() throws Exception {
		// GIVEN
		Friend f = new Friend();
		String f1 = "toto";
		String f2 = "tata";
		f.setEmailUser(f1);
		f.setEmailFriend(f2);
		String friendInString = "{ \"emailUser\":\"toto\", \"emailFriend\":\"tata\"}";

		// WHEN
		when(friendServiceMock.save(f)).thenReturn(f);

		// THEN
		MockHttpServletRequestBuilder req = post("/friends/save").contentType(MediaType.APPLICATION_JSON)
				.content(friendInString);
		mockMvc.perform(req).andExpect(status().isCreated()).andExpect(jsonPath("emailUser", is("toto")));
	}

	@Test
	public void testDeleteByEmailUserAndEmailFriend() throws Exception {
		// GIVEN
		String f1 = "friend1@gmail.com";
		String f2 = "friend2@gmail.com";

		// WHEN
		when(friendServiceMock.deleteByEmailUserAndEmailFriend(f1, f2)).thenReturn(new Friend("friend1@gmail.com","friend1@gmail.com"));

		// THEN
		mockMvc.perform(delete("/friends/delete?emailUser=friend1@gmail.com&emailFriend=friend2@gmail.com")).andExpect(status().isOk()).andExpect(jsonPath("$.emailFriend", is("friend1@gmail.com")));
	}
	
	@Test
	public void testDeleteByEmailUserAndPseudoFriend() throws Exception {
		// GIVEN
		String f1 = "friend1@gmail.com";
		String f2 = "pseudo";

		// WHEN
		when(friendServiceMock.deleteByEmailUserAndPseudoFriend(f1, f2)).thenReturn(new Friend("friend1@gmail.com","friend1@gmail.com"));

		// THEN
		mockMvc.perform(delete("/friends/delete?emailUser=friend1@gmail.com&pseudoFriend=pseudo")).andExpect(status().isOk()).andExpect(jsonPath("$.emailFriend", is("friend1@gmail.com")));
	}

}

package com.paymybuddy.api.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
		mockMvc.perform(get("/friends")).andExpect(status().isOk()).andExpect(jsonPath("$[0].emailFriend", is("friend@gmail.com")));
	}

//	@Test
//	public void testFindAllFriends() throws Exception {
//		// GIVEN
//		ArrayList<String> f = new ArrayList<String>();
//		String f1 = "friend1@gmail.com";
//		String f2 = "friend2@gmail.com";
//		f.add(f1);
//		f.add(f2);
//
//		// WHEN
//		when(friendServiceMock.findAllFriends()).thenReturn(f);
//
//		// THEN
//		mockMvc.perform(get("/friends")).andExpect(status().isOk()).andExpect(jsonPath("$[1]", is("friend2@gmail.com")));
//	}
//	
//	@Test
//	public void testFindByEmail() throws Exception {
//		// GIVEN
//		ArrayList<String> f = new ArrayList<String>();
//		String f1 = "friend1@gmail.com";
//		String f2 = "friend2@gmail.com";
//		f.add(f1);
//		f.add(f2);
//
//		// WHEN
//		when(friendServiceMock.findByEmail("friend2@gmail.com")).thenReturn(f);
//
//		// THEN
//		mockMvc.perform(get("/friends/withme?email=friend2@gmail.com")).andExpect(status().isOk()).andExpect(jsonPath("$[0]", is("friend1@gmail.com")));
//	}
//	
//	@Test
//	public void testFindOnlyMyFriends() throws Exception {
//		// GIVEN
//		ArrayList<String> f = new ArrayList<String>();
//		String f1 = "friend1@gmail.com";
//		String f2 = "friend2@gmail.com";
//		f.add(f1);
//		f.add(f2);
//		
//		HashSet<String> rep = new HashSet<String> ();
//		rep.add(f1);
//
//		// WHEN
//		when(friendServiceMock.findOnlyMyFriends("friend2@gmail.com")).thenReturn(rep);
//
//		// THEN
//		mockMvc.perform(get("/friends/withme/list?email=friend2@gmail.com")).andExpect(status().isOk()).andExpect(jsonPath("$[0]", is("friend1@gmail.com")));
//	}
//	
//	@Test
//	public void testSave() throws Exception {
//		// GIVEN
//		Friend f = new Friend();
//		String f1 = "friend1@gmail.com";
//		String f2 = "friend2@gmail.com";
//		f.setEmailUser(f1);
//		f.setEmailFriend(f2);
//		String friendInString = "{ \"emailUser\":\"friend1@gmail.com\", \"emailFriend\":\"friend2@gmail.com\"}";
//		
//		// WHEN
//		when(friendServiceMock.save(f)).thenReturn(f);
//
//		// THEN
//		MockHttpServletRequestBuilder req = post("/friends/save").contentType(MediaType.APPLICATION_JSON)
//				.content(friendInString);
//		mockMvc.perform(req).andExpect(status().isCreated()).andExpect(jsonPath("emailUser", is("friend1@gmail.com")));
//	}
//	
//	@Test
//	public void testDelete() throws Exception {
//		// GIVEN
//		String f1 = "friend1@gmail.com";
//		String f2 = "friend2@gmail.com";
//
//		// WHEN
//		when(friendServiceMock.delete(f1, f2)).thenReturn("friend1@gmail.com,friend1@gmail.com");
//
//		// THEN
//		mockMvc.perform(delete("/friends/delete?emailUser=friend1@gmail.com&emailFriend=friend2@gmail.com")).andExpect(status().isOk()).andExpect(jsonPath("$", is("friend1@gmail.com,friend1@gmail.com")));
//	}
	
}

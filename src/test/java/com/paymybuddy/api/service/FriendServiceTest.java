package com.paymybuddy.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.paymybuddy.api.model.AppUser;
import com.paymybuddy.api.model.Friend;
import com.paymybuddy.api.repository.FriendRepository;
import com.paymybuddy.api.repository.UserRepository;

@SpringBootTest
public class FriendServiceTest {

	@Autowired
	private FriendService friendService;
	@Autowired
	private Friend friend;

	@Mock
	private FriendRepository friendRepositoryMock;
	@Mock
	private UserRepository userRepositoryMock;

	@BeforeEach
	public void initFriends() {
		friend.setEmailUser("friend1@gmail.com");
		friend.setEmailFriend("friend2@gmail.com");
	}

	@Test
	public void testFindAll() throws Exception {
		// GIVEN
		ArrayList<Friend> friends = new ArrayList<Friend>();
		friends.add(friend);

		// WHEN
		when(friendRepositoryMock.findAll()).thenReturn(friends);
		friendService.setFriendRepository(friendRepositoryMock);

		// THEN
		assertThat(friendService.findAll().size()).isEqualTo(1);
	}

	@Test
	public void testFindByEmailWhenEmailNotExistInDatabase() throws Exception {
		// WHEN
		when(userRepositoryMock.existsById("friend1@gmail.com")).thenReturn(false);
		friendService.setUserRepository(userRepositoryMock);

		// THEN
		assertThatThrownBy(() -> friendService.findByEmail("friend1@gmail.com")).isInstanceOf(RuntimeException.class)
				.hasMessage("Email non existant dans la BDD");
	}

	@Test
	public void testFindByEmailWhenEmailExistInDatabase() throws Exception {
		// GIVEN
		ArrayList<Friend> friends = new ArrayList<Friend>();
		friends.add(friend);

		// WHEN
		when(userRepositoryMock.existsById("friend1@gmail.com")).thenReturn(true);
		when(friendRepositoryMock.findByEmailUserOrEmailFriend("friend1@gmail.com", "friend1@gmail.com"))
				.thenReturn(friends);
		friendService.setUserRepository(userRepositoryMock);
		friendService.setFriendRepository(friendRepositoryMock);

		// THEN
		assertThat(friendService.findByEmail("friend1@gmail.com").size()).isEqualTo(1);
	}

	@Test
	public void testFindFriendsOnlyWhenEmailNotExistInDatabase() throws Exception {
		// WHEN
		when(userRepositoryMock.existsById("friend1@gmail.com")).thenReturn(false);
		friendService.setUserRepository(userRepositoryMock);

		// THEN
		assertThatThrownBy(() -> friendService.findFriendsOnly("friend1@gmail.com"))
				.isInstanceOf(RuntimeException.class).hasMessage("Email non existant dans la BDD");
	}

	@Test
//	@Disabled
	public void testFindFriendsOnlyWhenEmailExistInDatabase() throws Exception {
		// GIVEN
		Friend f2 = new Friend();
		String email3 = "friend1@gmail.com";
		String email4 = "email4@gmail.com";
		f2.setEmailUser(email3);
		f2.setEmailFriend(email4);
		ArrayList<Friend> friends = new ArrayList<Friend>();
		friends.add(friend);
		friends.add(f2);

		// WHEN
		when(userRepositoryMock.existsById("friend1@gmail.com")).thenReturn(true);
		when(friendRepositoryMock.findByEmailUserOrEmailFriend("friend1@gmail.com", "friend1@gmail.com")).thenReturn(friends);
		friendService.setUserRepository(userRepositoryMock);
		friendService.setFriendRepository(friendRepositoryMock);

		// THEN
		assertThat(friendService.findFriendsOnly("friend1@gmail.com").contains("email4@gmail.com")).isEqualTo(true);
	}

	@Test
	public void testSaveWhenEmailUserIsNull() throws Exception {
		// WHEN
		when(userRepositoryMock.findByEmail(friend.getEmailUser())).thenReturn(null);
		friendService.setUserRepository(userRepositoryMock);

		// THEN
		assertThatThrownBy(() -> friendService.save(friend)).isInstanceOf(RuntimeException.class)
				.hasMessage("Email(s) 'ami' et/ou 'friend' non existant(s) dans la BDD");
	}

	@Test
	public void testSaveWhenEmailFriendIsNull() throws Exception {
		// WHEN
		when(userRepositoryMock.findByEmail(friend.getEmailFriend())).thenReturn(null);
		friendService.setUserRepository(userRepositoryMock);

		// THEN
		assertThatThrownBy(() -> friendService.save(friend)).isInstanceOf(RuntimeException.class)
				.hasMessage("Email(s) 'ami' et/ou 'friend' non existant(s) dans la BDD");
	}

	@Test
	public void testSaveWhenFriendsAlreadyExist() throws Exception {
		// GIVEN
		ArrayList<Friend> friends = new ArrayList<Friend>();
		friends.add(friend);
		AppUser appUser = new AppUser();

		// WHEN
		when(userRepositoryMock.findByEmail(friend.getEmailUser())).thenReturn(appUser);
		when(userRepositoryMock.findByEmail(friend.getEmailFriend())).thenReturn(appUser);
		when(friendRepositoryMock.findByEmailUserOrEmailFriend(friend.getEmailUser(), friend.getEmailFriend()))
				.thenReturn(friends);
		friendService.setUserRepository(userRepositoryMock);
		friendService.setFriendRepository(friendRepositoryMock);

		// THEN
		assertThatThrownBy(() -> friendService.save(friend)).isInstanceOf(RuntimeException.class)
				.hasMessage("Couple ami déjà existant dans la BDD");
	}

	@Test
	@Disabled
	public void testSaveWhenNoErrorExist() throws Exception {
		// GIVEN
//		Friend f2 = new Friend();
//		String email3 = "friend1@gmail.com";
//		String email4 = "email4@gmail.com";
//		f2.setEmailUser(email3);
//		f2.setEmailFriend(email4);
		ArrayList<Friend> friends = new ArrayList<Friend>();
//		friends.add(f2);
		friends.add(friend);
		AppUser appUser = new AppUser();

		// WHEN
		when(userRepositoryMock.findByEmail(friend.getEmailUser())).thenReturn(appUser);
		when(userRepositoryMock.findByEmail(friend.getEmailFriend())).thenReturn(appUser);
		when(friendRepositoryMock.findByEmailUserOrEmailFriend(friend.getEmailUser(), friend.getEmailFriend()))
				.thenReturn(friends);
		when(friendRepositoryMock.save(friend)).thenReturn(friend);
		friendService.setUserRepository(userRepositoryMock);
		friendService.setFriendRepository(friendRepositoryMock);

		// THEN
		assertThat(friendService.save(friend).getEmailUser()).isEqualTo("friend1@gmail.com");
	}

	@Test
	public void testDeleteWhenEmailUserIsNull() throws Exception {
		// WHEN
		when(userRepositoryMock.findByEmail("friend1@gmail.com")).thenReturn(null);
		friendService.setUserRepository(userRepositoryMock);

		// THEN
		assertThatThrownBy(() -> friendService.delete("friend1@gmail.com","friend2@gmail.com")).isInstanceOf(RuntimeException.class)
				.hasMessage("Email(s) 'ami' et/ou 'friend' non existant(s) dans la BDD");
	}
	
	@Test
	public void testDeleteWhenEmailFriendIsNull() throws Exception {
		// WHEN
		when(userRepositoryMock.findByEmail("friend2@gmail.com")).thenReturn(null);
		friendService.setUserRepository(userRepositoryMock);

		// THEN
		assertThatThrownBy(() -> friendService.delete("friend1@gmail.com","friend2@gmail.com")).isInstanceOf(RuntimeException.class)
				.hasMessage("Email(s) 'ami' et/ou 'friend' non existant(s) dans la BDD");
	}
	
	@Test
	public void testDeleteWhenFriendsAlreadyExist() throws Exception {
		// GIVEN
		AppUser appUser = new AppUser();
		
		// WHEN
		when(userRepositoryMock.findByEmail("friend1@gmail.com")).thenReturn(appUser);
		when(userRepositoryMock.findByEmail("friend2@gmail.com")).thenReturn(appUser);
		when(friendRepositoryMock.findByEmailUserAndEmailFriend("friend1@gmail.com","friend2@gmail.com")).thenReturn(null);
		when(friendRepositoryMock.findByEmailUserAndEmailFriend("friend2@gmail.com","friend1@gmail.com")).thenReturn(null);
		friendService.setUserRepository(userRepositoryMock);
		friendService.setFriendRepository(friendRepositoryMock);

		// THEN
		assertThatThrownBy(() -> friendService.delete("friend1@gmail.com","friend2@gmail.com")).isInstanceOf(RuntimeException.class)
				.hasMessage("Couple ami non existant dans la BDD");
	}
	
	@Test
	public void testDeleteWhenNoErrorExist() throws Exception {
		// GIVEN
		AppUser appUser = new AppUser();
		
		// WHEN
		when(userRepositoryMock.findByEmail("friend1@gmail.com")).thenReturn(appUser);
		when(userRepositoryMock.findByEmail("friend2@gmail.com")).thenReturn(appUser);
		when(friendRepositoryMock.findByEmailUserAndEmailFriend("friend1@gmail.com","friend2@gmail.com")).thenReturn(friend);
		when(friendRepositoryMock.findByEmailUserAndEmailFriend("friend2@gmail.com","friend1@gmail.com")).thenReturn(friend);
		friendService.setUserRepository(userRepositoryMock);
		friendService.setFriendRepository(friendRepositoryMock);

		// THEN
		assertThat(friendService.delete("friend1@gmail.com","friend2@gmail.com").getEmailUser()).isEqualTo("friend1@gmail.com");
	}

}

package com.paymybuddy.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
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
		assertThatThrownBy(() -> friendService.findByEmail("friend1@gmail.com")).isInstanceOf(Exception.class)
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
		assertThatThrownBy(() -> friendService.findEmailsFriendsOnly("friend1@gmail.com")).isInstanceOf(Exception.class)
				.hasMessage("Email non existant dans la BDD");
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
		when(friendRepositoryMock.findByEmailUserOrEmailFriend("friend1@gmail.com", "friend1@gmail.com"))
				.thenReturn(friends);
		friendService.setUserRepository(userRepositoryMock);
		friendService.setFriendRepository(friendRepositoryMock);

		// THEN
		assertThat(friendService.findEmailsFriendsOnly("friend1@gmail.com").contains("email4@gmail.com"))
				.isEqualTo(true);
	}

	@Test
	public void testSaveWhenEmailUserIsNull() throws Exception {
		// WHEN
		when(userRepositoryMock.findByEmail(friend.getEmailUser())).thenReturn(null);
		friendService.setUserRepository(userRepositoryMock);

		// THEN
		assertThatThrownBy(() -> friendService.save(friend)).isInstanceOf(Exception.class)
				.hasMessage("Email(s) 'ami' et/ou 'friend' non existant(s) dans la BDD");
	}

	@Test
	public void testSaveWhenEmailFriendIsNull() throws Exception {
		// WHEN
		when(userRepositoryMock.findByEmail(friend.getEmailFriend())).thenReturn(null);
		friendService.setUserRepository(userRepositoryMock);

		// THEN
		assertThatThrownBy(() -> friendService.save(friend)).isInstanceOf(Exception.class)
				.hasMessage("Email(s) 'ami' et/ou 'friend' non existant(s) dans la BDD");
	}

	@Test
	public void testSaveWhenFriendsAlreadyExist() throws Exception {
		// GIVEN
				Friend friend = new Friend();
				friend.setEmailUser("paul");
				friend.setEmailFriend("jean");
				Friend friend2 = new Friend();
				friend2.setEmailUser("jean");
				friend2.setEmailFriend("paul");
				List<Friend> friends = new ArrayList<Friend>();
				friends.add(friend2);

				// WHEN
				when(userRepositoryMock.existsById(friend.getEmailUser())).thenReturn(true);
				when(userRepositoryMock.existsById(friend.getEmailFriend())).thenReturn(true);
				when(friendRepositoryMock.findByEmailUserOrEmailFriend(friend.getEmailUser(), friend.getEmailFriend())).thenReturn(friends);
				when(friendRepositoryMock.save(friend)).thenReturn(friend);
				friendService.setUserRepository(userRepositoryMock);
				friendService.setFriendRepository(friendRepositoryMock);

		// THEN
		assertThatThrownBy(() -> friendService
				.save(friend)).isInstanceOf(Exception.class)
				.hasMessage("Couple ami déjà existant dans la BDD");
	}

	@Test
	public void testSaveWhenNoErrorExist() throws Exception {
		// GIVEN
		Friend f2 = new Friend();
		f2.setEmailUser("toto");
		f2.setEmailFriend("tata");
		List<Friend> friends = new ArrayList<Friend>();
		friends.add(f2);

		// WHEN
		when(userRepositoryMock.existsById("toto")).thenReturn(true);
		when(userRepositoryMock.existsById("tata")).thenReturn(true);
		when(friendRepositoryMock.findByEmailUserOrEmailFriend(friend.getEmailUser(), friend.getEmailFriend())).thenReturn(friends);
		when(friendRepositoryMock.save(friend)).thenReturn(friend);
		friendService.setUserRepository(userRepositoryMock);
		friendService.setFriendRepository(friendRepositoryMock);

		// THEN
		assertThat(friendService.save(f2).getEmailUser()).isEqualTo("toto");
	}

	@Test
	public void testDeleteByEmailUserAndEmailFriendWhenEmailNotExist() throws Exception {
		// WHEN
		when(userRepositoryMock.existsById("friend1@gmail.com") || !userRepositoryMock.existsById("friend2@gmail.com"))
				.thenReturn(false);
		friendService.setUserRepository(userRepositoryMock);

		// THEN
		assertThatThrownBy(
				() -> friendService.deleteByEmailUserAndEmailFriend("friend1@gmail.com", "friend2@gmail.com"))
						.isInstanceOf(Exception.class)
						.hasMessage("Email(s) 'ami' et/ou 'friend' non existant(s) dans la BDD");
	}

	@Test
	public void testDeleteByEmailUserAndEmailFriendWhenCoupleAlreadyExist() throws Exception {
		// GIVEN
		String emailUser = "friend1@gmail.com";
		String emailFriend = "friend2@gmail.com";

		// WHEN
		when(userRepositoryMock.existsById(emailUser)).thenReturn(true);
		when(userRepositoryMock.existsById(emailFriend)).thenReturn(true);
		when(friendRepositoryMock.findByEmailUserAndEmailFriend(emailUser, emailFriend)).thenReturn(null);
		when(friendRepositoryMock.findByEmailUserAndEmailFriend(emailFriend, emailUser)).thenReturn(null);
		friendService.setUserRepository(userRepositoryMock);
		friendService.setFriendRepository(friendRepositoryMock);

		// THEN
		assertThatThrownBy(
				() -> friendService.deleteByEmailUserAndEmailFriend("friend1@gmail.com", "friend2@gmail.com"))
						.isInstanceOf(Exception.class).hasMessage("Couple ami non existant dans la BDD");
	}

	@Test
	public void testDeleteByEmailUserAndEmailFriendWhenAllOk() throws Exception {
		// GIVEN
		String emailUser = "friend1@gmail.com";
		String emailFriend = "friend2@gmail.com";

		// WHEN
		when(userRepositoryMock.existsById(emailUser)).thenReturn(true);
		when(userRepositoryMock.existsById(emailFriend)).thenReturn(true);
		when(friendRepositoryMock.findByEmailUserAndEmailFriend(emailUser, emailFriend)).thenReturn(new Friend());
		when(friendRepositoryMock.findByEmailUserAndEmailFriend(emailFriend, emailUser)).thenReturn(new Friend());
		friendService.setUserRepository(userRepositoryMock);
		friendService.setFriendRepository(friendRepositoryMock);

		// THEN
		assertThat(friendService.deleteByEmailUserAndEmailFriend(emailUser,emailFriend).getEmailFriend()).isEqualTo("friend2@gmail.com");

	}

	@Test
	public void testDeleteByEmailUserAndPseudoFriendWhenFriendsAlreadyExist() throws Exception {
		// GIVEN
		String emailUser = "tata";
		String pseudoFriend = "toto";
		AppUser appUser = new AppUser(); 
		appUser.setEmail("titi");

		// WHEN
		when(userRepositoryMock.findByPseudo(pseudoFriend)).thenReturn(appUser);
		friendService.setUserRepository(userRepositoryMock);

		// THEN
		assertThat(friendService.deleteByEmailUserAndPseudoFriend(emailUser,pseudoFriend)).isEqualTo(new Friend(emailUser, "titi"));
	}

}

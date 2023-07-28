package develop.controller;

import develop.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
	private UserController userController;
	private User user1;
	private User user2;
	private User user3;
	private User expectedUser1;
	private User expectedUser2;
	private User expectedUser3;

	@Test
	public void addUserTest() {
		try {
			for (User u : userController.getUsers()) {
				if (u.getLogin().equals(user1.getLogin())) {
					assertEquals("lewis44", u.getName(),
							"Пустое имя пользователя не было изменено на логин");
				}
			}
		} catch (ResponseStatusException e) {
			assertEquals("400 BAD_REQUEST", e.getMessage());
		}

		try {
			expectedUser1 = userController.addUser(expectedUser1);
			assertFalse(userController.getUsers().contains(expectedUser1),
					"Пользователь с логином содержащим пробел прошел валидацию.");
		} catch (ResponseStatusException e) {
			assertEquals("400 BAD_REQUEST", e.getMessage());
		}

		try {
			expectedUser2 = userController.addUser(expectedUser2);
			assertFalse(userController.getUsers().contains(expectedUser2),
					"Пользователь с занятым логином прошел валидацию.");
		} catch (ResponseStatusException e) {
			assertEquals("400 BAD_REQUEST", e.getMessage());
		}

		try {
			expectedUser3 = userController.addUser(expectedUser3);
			assertFalse(userController.getUsers().contains(expectedUser3),
					"Пользователь с занятым email прошел валидацию.");
		} catch (ResponseStatusException e) {
			assertEquals("400 BAD_REQUEST", e.getMessage());
		}
	}

	@Test
	public void updateUserTest() {
		try {
			user1.setId(0);
			userController.updateUser(user1);
			assertFalse(userController.getUsers().contains(user1),
					"Пользователь без id прошел валидацию.");
		} catch (ResponseStatusException e) {
			assertEquals("404 NOT_FOUND", e.getMessage());
		}

		try {
			user1.setId(99);
			userController.updateUser(user1);
			assertFalse(userController.getUsers().contains(user1),
					"Пользователь с некорректным id прошел валидацию.");
		} catch (ResponseStatusException e) {
			assertEquals("404 NOT_FOUND", e.getMessage());
		}
	}

	@Test
	public void getUsersTest() {
		List<User> testUsers = new ArrayList<>();
		testUsers.add(user1);
		testUsers.add(user2);
		testUsers.add(user3);
		assertNotNull(userController.getUsers(), "Список пользователей не возвращается.");
		assertEquals(testUsers, userController.getUsers(), "Списки пользователей не идентичны.");
	}

	@BeforeEach
	public void start() {
		userController = new UserController();

		user1 = User.builder()
				.login("lewis44")
				.name("")
				.email("champion@yandex.ru")
				.birthday(LocalDate.of(1985, 1, 7))
				.build();

		user2 = User.builder()
				.login("monster")
				.name("Ken Block")
				.email("hoonigan@yandex.ru")
				.birthday(LocalDate.of(1967, 11, 21))
				.build();

		user3 = User.builder()
				.login("doctor46")
				.name("Valentino")
				.email("vr46@yandex.ru")
				.birthday(LocalDate.of(1979, 2, 16))
				.build();

		expectedUser1 = User.builder()
				.login("test Login")
				.name("testName")
				.email("test@yandex.ru")
				.birthday(LocalDate.of(2000, 1, 1))
				.build();

		expectedUser2 = User.builder()
				.login("lewis44")
				.name("testName")
				.email("test@yandex.ru")
				.birthday(LocalDate.of(2000, 1, 1))
				.build();

		expectedUser3 = User.builder()
				.login("testLogin")
				.name("testName")
				.email("champion@yandex.ru")
				.birthday(LocalDate.of(2000, 1, 1))
				.build();

		user1 = userController.addUser(user1);
		user2 = userController.addUser(user2);
		user3 = userController.addUser(user3);
	}
}
package develop.storage;

import develop.exception.NotFoundException;
import develop.exception.ValidationException;
import develop.model.User;
import develop.storage.user.InMemoryUserStorage;
import develop.storage.user.UserStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserStorageTest {
	private UserStorage userStorage;
	private User user1;
	private User user2;
	private User user3;
	private User expectedUser1;
	private User expectedUser2;

	@Test
	public void addTest() {
		for (User u : userStorage.get()) {
			if (u.getLogin().equals(user1.getLogin())) {
				assertEquals("lewis44", u.getName(),
						"Пустое имя пользователя не было изменено на логин");
			}
		}

		try {
			expectedUser1 = userStorage.add(expectedUser1);
			assertFalse(userStorage.get().contains(expectedUser1),
					"Пользователь с занятым логином прошел валидацию.");
		} catch (ValidationException e) {
			assertEquals("пользователь с таким логином уже зарегистрирован.", e.getMessage());
		}

		try {
			expectedUser2 = userStorage.add(expectedUser2);
			assertFalse(userStorage.get().contains(expectedUser2),
					"Пользователь с занятым email прошел валидацию.");
		} catch (ValidationException e) {
			assertEquals("пользователь с таким email уже зарегистрирован.", e.getMessage());
		}
	}

	@Test
	public void updateTest() {
		try {
			user1.setId(0);
			userStorage.update(user1);
			assertFalse(userStorage.get().contains(user1),
					"Пользователь без id прошел валидацию.");
		} catch (NotFoundException e) {
			assertEquals("пользователь не зарегистрирован.", e.getMessage());
		}

		try {
			user1.setId(99);
			userStorage.update(user1);
			assertFalse(userStorage.get().contains(user1),
					"Пользователь с некорректным id прошел валидацию.");
		} catch (NotFoundException e) {
			assertEquals("пользователь не найден.", e.getMessage());
		}
	}

	@Test
	public void getTest() {
		List<User> testUsers = new ArrayList<>();
		testUsers.add(user1);
		testUsers.add(user2);
		testUsers.add(user3);
		assertNotNull(userStorage.get(), "Список пользователей не возвращается.");
		assertEquals(testUsers, userStorage.get(), "Списки пользователей не идентичны.");
	}

	@BeforeEach
	public void start() {
		userStorage = new InMemoryUserStorage();

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
				.login("lewis44")
				.name("testName")
				.email("test@yandex.ru")
				.birthday(LocalDate.of(2000, 1, 1))
				.build();

		expectedUser2 = User.builder()
				.login("testLogin")
				.name("testName")
				.email("champion@yandex.ru")
				.birthday(LocalDate.of(2000, 1, 1))
				.build();

		user1 = userStorage.add(user1);
		user2 = userStorage.add(user2);
		user3 = userStorage.add(user3);
	}
}
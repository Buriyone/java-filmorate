package develop.controller;

import develop.model.User;
import javax.validation.Valid;

import develop.service.user.UserService;
import develop.storage.user.UserStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
	private final UserStorage userStorage;
	private final UserService userService;

	@PostMapping
	public User addUser(@RequestBody @Valid User user) {
		log.info("Получен запрос на регистрацию пользователя.");
		return userStorage.add(user);
	}

	@PutMapping
	public User updateUser(@RequestBody @Valid User user) {
		log.info("Получен запрос на обновление данных пользователя.");
		return userStorage.update(user);
	}

	@GetMapping
	public List<User> getUsers() {
		log.info("Получен запрос на предоставление списка пользователей.");
		return userStorage.get();
	}

	@GetMapping("/{id}")
	public User getUser(@PathVariable int id) {
		log.info("Получен запрос на предоставление пользователя по id.");
		return userStorage.getById(id);
	}

	@PutMapping("/{id}/friends/{friendId}")
	public User addFriend(@PathVariable int id, @PathVariable int friendId) {
		log.info("Получен запрос на добавление в друзья.");
		return userService.addFriend(id, friendId);
	}

	@DeleteMapping("{id}/friends/{friendId}")
	public User deleteFriend(@PathVariable int id, @PathVariable int friendId) {
		log.info("Получен запрос на удаление из друзей.");
		return userService.removeFriend(id, friendId);
	}

	@GetMapping("{id}/friends")
	public List<User> getFriends(@PathVariable int id) {
		log.info("Получен запрос на предоставление списка друзей пользователя.");
		return userService.getFriends(id);
	}

	@GetMapping("{id}/friends/common/{otherId}")
	public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
		log.info("Получен запрос на предоставление списка общих друзей.");
		return userService.getCommonFriends(id, otherId);
	}
}

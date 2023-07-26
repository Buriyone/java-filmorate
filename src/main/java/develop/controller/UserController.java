package develop.controller;

import develop.exception.NotFoundException;
import develop.exception.ValidationException;
import develop.model.User;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class UserController {
	private final Map<Integer, User> users = new HashMap<>();
	private int id = 1;
	@PostMapping(value = "/users")
	public User addUser(@RequestBody @Valid User user) {
		log.info("Получен запрос на регистрацию пользователя.");
		try {
			userValidation(user);
			for (User u : users.values()) {
				if (u.getLogin().equals(user.getLogin())) {
					throw new ValidationException("пользователь с таким логином уже зарегистрирован.");
				} else if (u.getEmail().equals(user.getEmail())) {
					throw new ValidationException("пользователь с таким email уже зарегистрирован.");
				}
			}
			if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
				log.debug("Пользователь {} не указал имя при регистрации.", user.getLogin());
				log.info("Имя пользователя {} будет изменено на логин", user.getLogin());
				user = user.toBuilder().name(user.getLogin()).build();
			}
			user.setId(id);
			users.put(id, user);
			log.info("Пользователь {} успешно зарегистрирован с id: {}.", user.getLogin(), id);
			id++;
		} catch (ValidationException e) {
			log.info("Ошибка валидации. Причина: {}", e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		return user;
	}
	
	@PutMapping(value = "/users")
	public User updateUser(@RequestBody @Valid User user) {
		log.info("Получен запрос на обновление данных пользователя.");
		try {
			userValidation(user);
			if (user.getId() == 0) {
				throw new NotFoundException("пользователь не зарегистрирован.");
			} else if (!users.containsKey(user.getId())) {
				throw new NotFoundException("пользователь не найден.");
			} else {
				if (user.getName().isEmpty() || user.getName().isBlank()) {
					log.debug("Пользователь {} скрыл имя.", user.getLogin());
					log.info("Имя пользователя {} будет изменено на логин", user.getLogin());
					user = user.toBuilder().name(user.getLogin()).build();
				}
				users.put(user.getId(), user);
				log.info("Данные пользователя {} были обновлены.", user.getLogin());
			}
		} catch (ValidationException e) {
			log.info("Ошибка валидации. Причина: {}", e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} catch (NotFoundException e) {
			log.info("Ошибка валидации. Причина: {}", e.getMessage());
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return user;
	}
	
	@GetMapping("/users")
	public List<User> getUsers() {
		return new ArrayList<>(users.values());
	}
	
	private void userValidation(User user) {
		if (user.getEmail().isEmpty() || user.getEmail().isBlank() || !user.getEmail().contains("@")
				|| user.getEmail().contains(" ")) {
			throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @.");
		} else if (user.getLogin().isBlank() || user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
			throw new ValidationException("логин не может быть пустым и содержать пробелы.");
		} else if (user.getBirthday().isAfter(LocalDate.now())) {
			throw new ValidationException("дата рождения не может быть в будущем.");
		}
	}
}

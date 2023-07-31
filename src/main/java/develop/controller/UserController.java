package develop.controller;

import develop.model.User;
import javax.validation.Valid;

import develop.service.ServiceUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class UserController {
	private final ServiceUser service = new ServiceUser();

	public UserController() {
	}

	@PostMapping(value = "/users")
	public User addUser(@RequestBody @Valid User user) {
		log.info("Получен запрос на регистрацию пользователя.");
		return service.add(user);
	}

	@PutMapping(value = "/users")
	public User updateUser(@RequestBody @Valid User user) {
		log.info("Получен запрос на обновление данных пользователя.");
		return service.update(user);
	}

	@GetMapping("/users")
	public List<User> getUsers() {
		return service.get();
	}
}

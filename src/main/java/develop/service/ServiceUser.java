package develop.service;

import develop.exception.NotFoundException;
import develop.exception.ValidationException;
import develop.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ServiceUser implements ServiceUserInterface {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 1;

    @Override
    public User add(User user) {
        try {
            userValidation(user);
            for (User u : users.values()) {
                if (u.getLogin().equals(user.getLogin())) {
                    throw new ValidationException("пользователь с таким логином уже зарегистрирован.");
                } else if (u.getEmail().equals(user.getEmail())) {
                    throw new ValidationException("пользователь с таким email уже зарегистрирован.");
                }
            }
            if (user.getName() == null || user.getName().isEmpty()) {
                log.debug("Пользователь {} не указал имя при регистрации.", user.getLogin());
                log.info("Имя пользователя {} будет изменено на логин", user.getLogin());
                user.setName(user.getLogin());
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

    @Override
    public User update(User user) {
        try {
            userValidation(user);
            if (user.getId() == 0) {
                throw new NotFoundException("пользователь не зарегистрирован.");
            } else if (!users.containsKey(user.getId())) {
                throw new NotFoundException("пользователь не найден.");
            } else {
                if (user.getName().isEmpty()) {
                    log.debug("Пользователь {} скрыл имя.", user.getLogin());
                    log.info("Имя пользователя {} будет изменено на логин", user.getLogin());
                    user.setName(user.getLogin());
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

    @Override
    public List<User> get() {
        return new ArrayList<>(users.values());
    }

    private void userValidation(User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("логин не должен содержать пробелы.");
        }
    }
}

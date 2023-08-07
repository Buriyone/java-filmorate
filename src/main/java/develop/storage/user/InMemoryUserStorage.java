package develop.storage.user;

import develop.exception.NotFoundException;
import develop.exception.ValidationException;
import develop.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 1;

    @Override
    public User add(User user) {
        for (User polledUser : users.values()) {
            if (polledUser.getLogin().equals(user.getLogin())) {
                throw new ValidationException("пользователь с таким логином уже зарегистрирован.");
            } else if (polledUser.getEmail().equals(user.getEmail())) {
                throw new ValidationException("пользователь с таким email уже зарегистрирован.");
            }
        }
        user = checkName(user);
        user.setId(id);
        user.setFriends(new HashSet<>());
        users.put(id, user);
        log.info("Пользователь {} успешно зарегистрирован с id: {}.", user.getLogin(), id);
        id++;
        return user;
    }

    @Override
    public User update(User user) {
        if (user.getId() == 0) {
            throw new NotFoundException("пользователь не зарегистрирован.");
        } else if (!users.containsKey(user.getId())) {
            throw new NotFoundException("пользователь не найден.");
        } else {
            user = checkName(user);
            if (user.getFriends() == null) {
                user.setFriends(new HashSet<>());
            }
            users.put(user.getId(), user);
            log.info("Данные пользователя {} были обновлены.", user.getLogin());
        }
        return user;
    }

    @Override
    public User getById(int id) {
        if (users.containsKey(id)) {
            log.info("Пользователь с id: " + id + " успешно предоставлен.");
            return users.get(id);
        } else {
            throw new NotFoundException("пользователь с id: " + id + " не зарегистрирован.");
        }
    }

    @Override
    public List<User> get() {
        log.info("Список пользователей успешно предоставлен.");
        return new ArrayList<>(users.values());
    }

    public User checkName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.debug("Пользователь {} скрыл имя.", user.getLogin());
            log.info("Имя пользователя {} будет изменено на логин", user.getLogin());
            user.setName(user.getLogin());
        }
        return user;
    }
}

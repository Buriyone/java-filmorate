package develop.service.user;

import develop.exception.NotFoundException;
import develop.model.User;
import develop.storage.user.UserStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    public final UserStorage userStorage;

    public User addFriend(int userId, int friendId) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        userStorage.update(friend);
        log.info("Пользователь {} добавил в друзья {}", user.getLogin(), friend.getLogin());
        return userStorage.update(user);
    }

    public User removeFriend(int userId, int friendId) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);
        if (user.getFriends().contains(friendId) && friend.getFriends().contains(userId)) {
            friend.getFriends().remove(userId);
            user.getFriends().remove(friendId);
            userStorage.update(friend);
            log.info("Пользователь {} удалил из друзей {}", user.getLogin(), friend.getLogin());
            return userStorage.update(user);
        } else {
            throw new NotFoundException("пользователи не состоят в друзьях");
        }
    }

    public List<User> getFriends(int userId) {
        User user = userStorage.getById(userId);
        List<User> friends = new ArrayList<>();
        for (Integer id : user.getFriends()) {
            friends.add(userStorage.getById(id));
        }
        log.info("Список друзей пользователя: " + user.getLogin() + " успешно предоставлен.");
        return friends;
    }

    public List<User> getCommonFriends(int userId1, int userId2) {
        User user1 = userStorage.getById(userId1);
        User user2 = userStorage.getById(userId2);
        List<User> commonFriends = new ArrayList<>();
        for (Integer id : user1.getFriends()) {
            for (Integer id2 : user2.getFriends()) {
                if (Objects.equals(id, id2)) {
                    commonFriends.add(userStorage.getById(id));
                }
            }
        }
        log.info("Список общих друзей пользователей " + user1.getLogin() + " и "
                + user2.getLogin() + " успешно предоставлен.");
        return commonFriends;
    }
}

package develop.service.user;

import develop.model.User;
import develop.storage.friend.FriendStorage;
import develop.storage.user.UserStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    public void addFriend(int userId, int friendId) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);
        friendStorage.addFriend(user, friend);
    }

    public void removeFriend(int userId, int friendId) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);
        friendStorage.deleteFriend(user, friend);
        log.info("Пользователь {} удалил из друзей {}", user.getLogin(), friend.getLogin());
    }

    public List<User> getFriends(int userId) {
        User user = userStorage.getById(userId);
        log.info("Список друзей пользователя: {} успешно предоставлен.", user.getLogin());
        return friendStorage.getFriends(user);
    }

    public List<User> getCommonFriends(int userId, int otherId) {
        User user = userStorage.getById(userId);
        User otherUser = userStorage.getById(otherId);
        log.info("Список общих друзей пользователей {} и {} успешно предоставлен.",
                user.getLogin(), otherUser.getLogin());
        return friendStorage.getCommonFriends(user, otherUser);
    }
}

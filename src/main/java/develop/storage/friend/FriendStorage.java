package develop.storage.friend;

import develop.model.User;

import java.util.List;

public interface FriendStorage {
    void addFriend(User user, User friend);

    void deleteFriend(User user, User friend);

    List<User> getFriends(User user);

    List<User> getCommonFriends(User user, User otherUser);
}

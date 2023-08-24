package develop.storage.friend;

import develop.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class InMemoryFriendStorage implements FriendStorage {
    private final Map<User, List<User>> friends = new HashMap<>();

    @Override
    public void addFriend(User user, User friend) {
        List<User> friendByUser = getFriends(user);
        friendByUser.add(friend);
        friends.put(user, friendByUser);
    }

    @Override
    public void deleteFriend(User user, User friend) {
        List<User> friendByUser = getFriends(user);
        friendByUser.remove(friend);
        friends.put(user, friendByUser);
    }

    @Override
    public List<User> getFriends(User user) {
        return friends.get(user);
    }

    @Override
    public List<User> getCommonFriends(User user, User otherUser) {
        List<User> commonFriends = new ArrayList<>();
        for (User user1 : getFriends(user)) {
            for (User user2 : getFriends(otherUser)) {
                if (user1.equals(user2)) {
                    commonFriends.add(user2);
                }
            }
        }
        return commonFriends;
    }
}

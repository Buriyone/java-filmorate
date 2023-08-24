package develop.storage.friend;

import develop.exception.ValidationException;
import develop.model.User;
import develop.storage.user.UserStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@Primary
public class FriendDbStorage implements FriendStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserStorage userStorage;

    @Override
    public void addFriend(User user, User friend) {
        jdbcTemplate.update("INSERT INTO friends (user_id, friend_id) VALUES (?, ?);",
                user.getId(), friend.getId());
    }

    @Override
    public void deleteFriend(User user, User friend) {
        SqlRowSet sqlRowSet = jdbcTemplate
                .queryForRowSet("SELECT * FROM friends WHERE user_id = ? AND friend_id = ?",
                        user.getId(), friend.getId());
        if (sqlRowSet.next()) {
            jdbcTemplate.update("DELETE FROM friends WHERE user_id = ? AND friend_id = ?",
                    user.getId(), friend.getId());
        } else {
            throw new ValidationException("пользователь " + friend.getName()
                    + " не состоят в друзьях у " + user.getName());
        }
    }

    @Override
    public List<User> getFriends(User user) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM friends WHERE user_id = ?", user.getId());
        List<User> friends = new ArrayList<>();
        while (sqlRowSet.next()) {
            friends.add(userStorage.getById(sqlRowSet.getInt("friend_id")));
        }
        return friends;
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

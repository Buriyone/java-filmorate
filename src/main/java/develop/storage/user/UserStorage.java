package develop.storage.user;

import develop.model.User;

import java.util.List;

public interface UserStorage {
    User add(User user);

    User update(User user);

    List<User> get();

    User getById(int id);
}

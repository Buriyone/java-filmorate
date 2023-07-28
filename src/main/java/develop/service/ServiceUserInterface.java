package develop.service;

import develop.model.User;

import java.util.List;

public interface ServiceUserInterface {
    User add(User user);

    User update(User user);

    List<User> get();
}

package develop.storage.user;

import develop.exception.NotFoundException;
import develop.exception.ValidationException;
import develop.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
@Primary
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User add(User user) {
        String sqlByLogin = "SELECT * FROM users WHERE login = ?";
        String sqlByEmail = "SELECT * FROM users WHERE email = ?";
        SqlRowSet sqlRowSetByLogin = jdbcTemplate.queryForRowSet(sqlByLogin, user.getLogin());
        SqlRowSet sqlRowSetByEmail = jdbcTemplate.queryForRowSet(sqlByEmail, user.getEmail());
        if (sqlRowSetByLogin.next()) {
            throw new ValidationException("пользователь с таким логином уже зарегистрирован.");
        } else if (sqlRowSetByEmail.next()) {
            throw new ValidationException("пользователь с таким email уже зарегистрирован.");
        } else {
            user = checkName(user);
            jdbcTemplate.update("INSERT INTO users (login, name, email, birthday) VALUES (?, ?, ?, ?);",
                    user.getLogin(), user.getName(), user.getEmail(), user.getBirthday());
            SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlByLogin, user.getLogin());
            if(sqlRowSet.next()) {
                user.setId(sqlRowSet.getInt("id"));
                log.info("Пользователь {} успешно зарегистрирован с id: {}.",
                        user.getLogin(), sqlRowSet.getInt("id"));
            }
            return user;
        }
    }

    @Override
    public User update(User user) {
        if (user.getId() == 0) {
            throw new NotFoundException("пользователь не зарегистрирован.");
        } else {
            SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE id = ?", user.getId());
            if (sqlRowSet.next()) {
                user = checkName(user);
                jdbcTemplate.update("UPDATE users SET login = ?, name = ?, email = ?, birthday = ? WHERE id = ? ",
                        user.getLogin(), user.getName(), user.getEmail(), user.getBirthday(), user.getId());
                log.info("Данные пользователя {} были обновлены.", user.getLogin());
                return getById(user.getId());
            } else {
                throw new NotFoundException("пользователь не найден.");
            }
        }
    }

    @Override
    public List<User> get() {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT COUNT(id) AS count FROM users");
        List<User> users = new ArrayList<>();
        if (sqlRowSet.next()) {
            if (sqlRowSet.getInt("count") == 0) {
                return users;
            } else {
                for (int i = 1; i <= sqlRowSet.getInt("count"); i++) {
                    users.add(getById(i));
                }
            }
        }
        log.info("Список пользователей успешно предоставлен.");
        return users;
    }

    @Override
    public User getById(int id) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE id = ?", id);
        if (sqlRowSet.next()) {
            log.info("Пользователь с id: {} успешно предоставлен.", id);
            return User.builder()
                    .id(sqlRowSet.getInt("id"))
                    .login(sqlRowSet.getString("login"))
                    .name(sqlRowSet.getString("name"))
                    .email(sqlRowSet.getString("email"))
                    .birthday(Objects.requireNonNull(sqlRowSet.getDate("birthday")).toLocalDate())
                    .build();
        } else {
            throw new NotFoundException("пользователь не найден.");
        }
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

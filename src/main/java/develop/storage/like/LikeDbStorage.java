package develop.storage.like;

import develop.exception.ValidationException;
import develop.model.Film;
import develop.model.User;
import develop.storage.film.FilmStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class LikeDbStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmStorage filmStorage;

    @Override
    public void addLike(Film film, User user) {
        jdbcTemplate.update("INSERT INTO likes (film_id, user_id) VALUES (?, ?);", film.getId(), user.getId());
        updateRate(film);
    }

    @Override
    public void removeLike(Film film, User user) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM likes WHERE film_id = ? AND user_id = ?",
                film.getId(), user.getId());
        if (sqlRowSet.next()) {
            jdbcTemplate.update("DELETE FROM likes WHERE film_id = ? AND user_id = ?",
                    film.getId(), user.getId());
            updateRate(film);
        } else {
            throw new ValidationException("пользователь " + user.getLogin()
                    + " не оценивал фильм " + film.getName());
        }
    }

    @Override
    public List<Film> getTop(int count) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM film ORDER BY rate DESC LIMIT ?", count);
        List<Film> top = new ArrayList<>();
        while (sqlRowSet.next()) {
            top.add(filmStorage.getById(sqlRowSet.getInt("id")));
        }
        return top;
    }

    private void updateRate(Film film) {
        SqlRowSet sqlRowSet = jdbcTemplate
                .queryForRowSet("SELECT COUNT(user_id) AS rate FROM likes WHERE film_id = ?", film.getId());
        if (sqlRowSet.next()) {
            film.setRate(sqlRowSet.getInt("rate"));
            filmStorage.update(film);
        }
    }
}

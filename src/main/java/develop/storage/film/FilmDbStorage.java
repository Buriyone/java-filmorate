package develop.storage.film;

import develop.exception.NotFoundException;
import develop.exception.ValidationException;
import develop.model.Film;
import develop.model.Genre;
import develop.storage.genre.GenreStorage;
import develop.storage.mpa.MpaStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
@Primary
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;

    @Override
    public Film add(Film film) {
        filmValidation(film);
        jdbcTemplate.update("INSERT INTO film (name, description, releaseDate, duration, rate, mpa_id) " +
                        "VALUES (?, ?, ?, ?, ?, ?);",
                film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), 0,
                film.getMpa().getId());
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT MAX(id) AS max_id FROM film");
        if (sqlRowSet.next() && film.getGenres() != null) {
            addGenresInFilm(film, sqlRowSet.getInt("max_id"));
        }
        log.info("Фильм {} успешно добавлен с id: {}.", film.getName(), sqlRowSet.getInt("max_id"));
        return getById(sqlRowSet.getInt("max_id"));
    }

    @Override
    public Film update(Film film) {
        filmValidation(film);
        if (film.getId() == 0) {
            throw new NotFoundException("необходимо добавить фильм в приложение.");
        } else {
            SqlRowSet sqlRowSet = jdbcTemplate
                    .queryForRowSet("SELECT * FROM film WHERE id = ?", film.getId());
            SqlRowSet sqlRowSetRate = jdbcTemplate
                    .queryForRowSet("SELECT COUNT(user_id) AS rate FROM likes WHERE film_id = ?", film.getId());
            int rate = 0;
            if (sqlRowSetRate.next()) {
                rate = sqlRowSetRate.getInt("rate");
            }
            if (sqlRowSet.next()) {
                jdbcTemplate.update("UPDATE film SET name = ?, description = ?, releaseDate = ?, duration = ?,"
                                + "rate = ?, mpa_id = ? WHERE id = ?",
                        film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                        rate, film.getMpa().getId(), film.getId());
                jdbcTemplate.update("DELETE FROM films_genres WHERE film_id = ?", film.getId());
                if (film.getGenres() != null && !film.getGenres().isEmpty()) {
                    addGenresInFilm(film, film.getId());
                }
                log.info("Фильм {} успешно обновлен.", film.getName());
                return getById(film.getId());
            } else {
                throw new NotFoundException("фильм c id: " + film.getId() + "не обнаружен.");
            }
        }
    }

    @Override
    public List<Film> get() {
        List<Film> films = new ArrayList<>();
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM film");
        while (sqlRowSet.next()) {
            films.add(getById(sqlRowSet.getInt("id")));
        }
        log.info("Список фильмов успешно предоставлен.");
        return films;
    }

    @Override
    public Film getById(int id) {
        SqlRowSet sqlRowSetById = jdbcTemplate.queryForRowSet("SELECT * FROM film WHERE id = ?", id);
        if (sqlRowSetById.next()) {
            Film film = Film.builder()
                    .id(id)
                    .name(sqlRowSetById.getString("name"))
                    .description(sqlRowSetById.getString("description"))
                    .releaseDate(Objects.requireNonNull(sqlRowSetById.getDate("releaseDate")).toLocalDate())
                    .duration(sqlRowSetById.getInt("duration"))
                    .rate(sqlRowSetById.getInt("rate"))
                    .mpa(mpaStorage.getById(sqlRowSetById.getInt("mpa_id")))
                    .build();
            film.setGenres(new LinkedHashSet<>());
            SqlRowSet sqlRowSetByGenre = jdbcTemplate
                    .queryForRowSet("SELECT * FROM films_genres WHERE film_id = ? ORDER BY genre_id", id);
            while (sqlRowSetByGenre.next()) {
                film.getGenres().add(genreStorage.getById(sqlRowSetByGenre.getInt("genre_id")));
            }
            log.info("Фильм с id: {} успешно предоставлен.", id);
            return film;
        } else {
            throw new NotFoundException("фильм с id: " + id + " не добавлялся.");
        }
    }

    private void filmValidation(Film film) {
        if (film.getReleaseDate() != null
                && film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года.");
        }
    }

    private void addGenresInFilm(Film film, int id) {
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update("INSERT INTO films_genres (film_id, genre_id) VALUES (?, ?);",
                    id, genre.getId());
        }
    }
}

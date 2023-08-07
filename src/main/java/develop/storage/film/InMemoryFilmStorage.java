package develop.storage.film;

import develop.exception.NotFoundException;
import develop.exception.ValidationException;
import develop.model.Film;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 1;

    @Override
    public Film add(Film film) {
        filmValidation(film);
        film.setId(id);
        film.setUsersLike(new HashSet<>());
        films.put(id, film);
        log.info("Фильм {} успешно добавлен с id: {}.", film.getName(), id);
        id++;
        return film;
    }

    @Override
    public Film update(Film film) {
        filmValidation(film);
        if (film.getId() == 0) {
            throw new NotFoundException("необходимо добавить фильм в приложение.");
        } else if (!films.containsKey(film.getId())) {
            throw new NotFoundException("фильм c id: " + film.getId() + "не обнаружен.");
        } else {
            if (film.getUsersLike() == null) {
                film.setUsersLike(new HashSet<>());
            }
            films.put(film.getId(), film);
            log.info("Фильм {} успешно обновлен.", film.getName());
            return film;
        }
    }

    @Override
    public List<Film> get() {
        log.info("Список фильмов успешно предоставлен.");
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getById(int id) {
        if (films.containsKey(id)) {
            log.info("Фильм с id: " + id + " успешно предоставлен.");
            return films.get(id);
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
}

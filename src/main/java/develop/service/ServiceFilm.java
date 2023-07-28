package develop.service;

import develop.exception.NotFoundException;
import develop.exception.ValidationException;
import develop.model.Film;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ServiceFilm implements ServiceFilmInterface {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 1;

    @Override
    public Film add(Film film) {
        try {
            filmValidation(film);
            film.setId(id);
            films.put(id, film);
            log.info("Фильм {} успешно добавлен с id: {}.", film.getName(), id);
            id++;
        } catch (ValidationException e) {
            log.info("Ошибка валидации. Причина: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return film;
    }

    @Override
    public Film update(Film film) {
        try {
            filmValidation(film);
            if (film.getId() == 0) {
                throw new NotFoundException("сперва необходимо добавить фильм в приложение.");
            } else if (!films.containsKey(film.getId())) {
                throw new NotFoundException("фильм не обнаружен.");
            } else {
                films.put(film.getId(), film);
                log.info("Фильм {} успешно обновлен.", film.getName());
            }
        } catch (ValidationException e) {
            log.info("Ошибка валидации. Причина: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            log.info("Ошибка валидации. Причина: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return film;
    }

    @Override
    public List<Film> get() {
        return new ArrayList<>(films.values());
    }

    private void filmValidation(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года.");
        }
    }
}

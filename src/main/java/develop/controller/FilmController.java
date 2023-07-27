package develop.controller;

import develop.exception.NotFoundException;
import develop.exception.ValidationException;
import develop.model.Film;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("checkstyle:Regexp")
@RestController
@Slf4j
public class FilmController {
	private final Map<Integer, Film> films = new HashMap<>();
	private int id = 1;

	@PostMapping(value = "/films")
	public Film addFilm(@Valid @RequestBody Film film) {
		log.info("Получен запрос на добавление фильма.");
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

	@PutMapping(value = "/films")
	public Film updateFilm(@Valid @RequestBody Film film) {
		log.info("Получен запрос на обновление фильма.");
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

	@GetMapping(value = "/films")
	public List<Film> getFilms() {
		return new ArrayList<>(films.values());
	}

	private void filmValidation(Film film) {
		if (film.getName().isBlank() || film.getName().isEmpty()) {
			throw new ValidationException("название не может быть пустым.");
		} else if (film.getDescription().length() > 200) {
			throw new ValidationException("максимальная длина описания — 200 символов.");
		} else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
			throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года.");
		} else if (film.getDuration() <= 0) {
			throw new ValidationException("продолжительность фильма должна быть положительной.");
		}
	}
}

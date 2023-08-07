package develop.controller;

import develop.model.Film;
import javax.validation.Valid;

import develop.service.film.FilmService;
import develop.storage.film.FilmStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
	public final FilmService filmService;
	public final FilmStorage filmStorage;

	@PostMapping
	public Film addFilm(@Valid @RequestBody Film film) {
		log.info("Получен запрос на добавление фильма.");
		return filmStorage.add(film);
	}

	@PutMapping
	public Film updateFilm(@Valid @RequestBody Film film) {
		log.info("Получен запрос на обновление фильма.");
		return filmStorage.update(film);
	}

	@GetMapping
	public List<Film> getFilms() {
		log.info("Получен запрос на предоставление списка фильмов.");
		return filmStorage.get();
	}

	@GetMapping("/{id}")
	public Film getFilm(@PathVariable int id) {
		log.info("Получен запрос на предоставление фильма по id.");
		return filmStorage.getById(id);
	}

	@PutMapping("/{id}/like/{userId}")
	public Film addLike(@PathVariable int id, @PathVariable int userId) {
		log.info("Получен запрос на оценку фильма.");
		return filmService.addLike(id, userId);
	}

	@DeleteMapping("/{id}/like/{userId}")
	public Film removeLike(@PathVariable int id, @PathVariable int userId) {
		log.info("Получен запрос на удаление оценки фильма.");
		return filmService.removeLike(id, userId);
	}

	@GetMapping("/popular")
	public List<Film> getTopFilm(@RequestParam(defaultValue = "10") int count) {
		log.info("Получен запрос на предоставление рейтинга топовых фильмов.");
		return filmService.getTop(count);
	}
}

package develop.controller;

import develop.model.Film;
import javax.validation.Valid;

import develop.service.ServiceFilm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class FilmController {
	private final ServiceFilm service = new ServiceFilm();

	@PostMapping(value = "/films")
	public Film addFilm(@Valid @RequestBody Film film) {
		log.info("Получен запрос на добавление фильма.");
		return service.add(film);
	}

	@PutMapping(value = "/films")
	public Film updateFilm(@Valid @RequestBody Film film) {
		log.info("Получен запрос на обновление фильма.");
		return service.update(film);
	}

	@GetMapping(value = "/films")
	public List<Film> getFilms() {
		return service.get();
	}
}

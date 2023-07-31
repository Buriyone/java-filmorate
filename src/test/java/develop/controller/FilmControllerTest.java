package develop.controller;

import develop.model.Film;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
	private FilmController filmController;
	private Film film1;
	private Film film2;
	private Film film3;
	private Film expectedFilm3;

	@Test
	public void addFilmTest() {
		assertTrue(filmController.getFilms().contains(film1), "Фильм 1 не был добавлен.");
		assertTrue(filmController.getFilms().contains(film2), "Фильм 2 не был добавлен.");
		assertTrue(filmController.getFilms().contains(film3), "Фильм 3 не был добавлен.");
		try {
			filmController.addFilm(expectedFilm3);
			assertFalse(filmController.getFilms().contains(expectedFilm3),
					"Фильм с неверной датой прошел валидацию.");
		} catch (ResponseStatusException e) {
			assertEquals("400 BAD_REQUEST", e.getMessage());
		}
	}

	@Test
	public void updateFilmTest() {
		try {
			film1.setId(0);
			filmController.updateFilm(film1);
			assertFalse(filmController.getFilms().contains(film1),
					"Фильм без id был обновлен.");
		} catch (ResponseStatusException e) {
			assertEquals("404 NOT_FOUND", e.getMessage());
		}
		try {
			film1.setId(99);
			filmController.updateFilm(film1);
			assertFalse(filmController.getFilms().contains(film1),
					"Фильм с некорректным id был обновлен.");
		} catch (ResponseStatusException e) {
			assertEquals("404 NOT_FOUND", e.getMessage());
		}
	}

	@Test
	public void getFilmsTest() {
		List<Film> testFilms = new ArrayList<>();
		testFilms.add(film1);
		testFilms.add(film2);
		testFilms.add(film3);
		assertNotNull(filmController.getFilms(), "Список фильмов не возвращается.");
		assertEquals(testFilms, filmController.getFilms(), "Списки фильмов не идентичны.");
	}

	@BeforeEach
	public void start() {
		filmController = new FilmController();

		film1 = Film.builder()
				.name("Snatch")
				.description("Crime England through the eyes of Guy Ritchie")
				.releaseDate(LocalDate.of(2000, 8, 23))
				.duration(104)
				.build();

		film2 = Film.builder()
				.name("The Gentlemen")
				.description("An American expat tries to sell off his highly profitable empire in London")
				.releaseDate(LocalDate.of(2019, 12, 3))
				.duration(113)
				.build();

		film3 = Film.builder()
				.name("Lock, Stock and Two Smoking Barrels")
				.description("They lost half a million at cards but they've still got a few tricks up their sleeve")
				.releaseDate(LocalDate.of(1998, 8, 23))
				.duration(107)
				.build();

		expectedFilm3 = Film.builder()
				.name("testName")
				.description("testDescription")
				.releaseDate(LocalDate.of(1895, 12, 27))
				.duration(123)
				.build();

		film1 = filmController.addFilm(film1);
		film2 = filmController.addFilm(film2);
		film3 = filmController.addFilm(film3);
	}
}
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
	private Film expectedFilm1;
	private Film expectedFilm2;
	private Film expectedFilm3;
	private Film expectedFilm4;

	@Test
	public void addFilmTest() {
		try {
			filmController.addFilm(expectedFilm1);
			assertFalse(filmController.getFilms().contains(expectedFilm1),
					"Фильм с превышенной длиной описания прошел валидацию.");
		} catch (ResponseStatusException e) {
			assertEquals("400 BAD_REQUEST", e.getMessage());
		}

		try {
			filmController.addFilm(expectedFilm2);
			assertFalse(filmController.getFilms().contains(expectedFilm2),
					"Фильм с пустым именем прошел валидацию.");
		} catch (ResponseStatusException e) {
			assertEquals("400 BAD_REQUEST", e.getMessage());
		}

		try {
			filmController.addFilm(expectedFilm3);
			assertFalse(filmController.getFilms().contains(expectedFilm3),
					"Фильм с неверной датой прошел валидацию.");
		} catch (ResponseStatusException e) {
			assertEquals("400 BAD_REQUEST", e.getMessage());
		}

		try {
			filmController.addFilm(expectedFilm4);
			assertFalse(filmController.getFilms().contains(expectedFilm4),
					"Фильм с отрицательной продолжительностью прошел валидацию.");
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

		StringBuilder testDescription = new StringBuilder("test");

		for (int i = 0; i < 201; i++) {
			testDescription.append("test");
		}

		film1 = new Film("Snatch", "Crime England through the eyes of Guy Ritchie",
				LocalDate.of(2000, 8, 23), 104);

		film2 = new Film("The Gentlemen",
				"An American expat tries to sell off his highly profitable empire in London",
				LocalDate.of(2019, 12, 3), 113);

		film3 = new Film("Lock, Stock and Two Smoking Barrels",
				"They lost half a million at cards but they've still got a few tricks up their sleeve",
				LocalDate.of(1998, 8, 23), 107);

		expectedFilm1 = new Film("testName", testDescription.toString(),
				LocalDate.of(2012, 2, 12), 123);

		expectedFilm2 = new Film("", "testDescription",
				LocalDate.of(2012, 2, 12), 123);

		expectedFilm3 = new Film("testName", "testDescription",
				LocalDate.of(1895, 12, 27), 123);

		expectedFilm4 = new Film("testName", "testDescription",
				LocalDate.of(2012, 2, 12), -123);

		film1 = filmController.addFilm(film1);
		film2 = filmController.addFilm(film2);
		film3 = filmController.addFilm(film3);
	}
}
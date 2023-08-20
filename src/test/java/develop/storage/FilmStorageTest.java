package develop.storage;

import develop.exception.NotFoundException;
import develop.exception.ValidationException;
import develop.model.Film;
import develop.storage.film.FilmStorage;
import develop.storage.film.InMemoryFilmStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmStorageTest {
	private FilmStorage filmStorage;
	private Film film1;
	private Film film2;
	private Film film3;
	private Film expectedFilm3;

	@Test
	public void addTest() {
		assertTrue(filmStorage.get().contains(film1), "Фильм 1 не был добавлен.");
		assertTrue(filmStorage.get().contains(film2), "Фильм 2 не был добавлен.");
		assertTrue(filmStorage.get().contains(film3), "Фильм 3 не был добавлен.");
		try {
			filmStorage.add(expectedFilm3);
			assertFalse(filmStorage.get().contains(expectedFilm3),
					"Фильм с неверной датой прошел валидацию.");
		} catch (ValidationException e) {
			assertEquals("дата релиза — не раньше 28 декабря 1895 года.", e.getMessage());
		}
	}

	@Test
	public void updateTest() {
		try {
			film1.setId(0);
			filmStorage.update(film1);
			assertFalse(filmStorage.get().contains(film1),
					"Фильм без id был обновлен.");
		} catch (NotFoundException e) {
			assertEquals("необходимо добавить фильм в приложение.", e.getMessage());
		}
		try {
			film1.setId(99);
			filmStorage.update(film1);
			assertFalse(filmStorage.get().contains(film1),
					"Фильм с некорректным id был обновлен.");
		} catch (NotFoundException e) {
			assertEquals("фильм c id: " + film1.getId() + "не обнаружен.", e.getMessage());
		}
	}

	@Test
	public void getTest() {
		List<Film> testFilms = new ArrayList<>();
		testFilms.add(film1);
		testFilms.add(film2);
		testFilms.add(film3);
		assertNotNull(filmStorage.get(), "Список фильмов не возвращается.");
		assertEquals(testFilms, filmStorage.get(), "Списки фильмов не идентичны.");
	}

	@BeforeEach
	public void start() {
		filmStorage = new InMemoryFilmStorage();

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

		film1 = filmStorage.add(film1);
		film2 = filmStorage.add(film2);
		film3 = filmStorage.add(film3);
	}
}
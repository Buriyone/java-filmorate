package develop.storage.film;

import develop.model.Film;

import java.util.List;

public interface FilmStorage {
    Film add(Film film);

    Film update(Film film);

    List<Film> get();

    Film getById(int id);
}

package develop.service;

import develop.model.Film;

import java.util.List;

public interface ServiceFilmInterface {
    Film add(Film film);

    Film update(Film film);

    List<Film> get();
}

package develop.storage.genre;

import develop.model.Genre;

import java.util.List;

public interface GenreStorage {
    Genre getById(int id);

    List<Genre> get();
}

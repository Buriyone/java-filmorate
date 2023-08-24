package develop.storage.like;

import develop.model.Film;
import develop.model.User;

import java.util.List;

public interface LikeStorage {
    void addLike(Film film, User user);

    void removeLike(Film film, User user);

    List<Film> getTop(int count);
}

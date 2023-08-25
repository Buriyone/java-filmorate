package develop.service.film;

import develop.model.Film;
import develop.model.User;
import develop.storage.film.FilmStorage;
import develop.storage.like.LikeStorage;
import develop.storage.user.UserStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeStorage likeStorage;

    public void addLike(int filmId, int userId) {
        Film film = filmStorage.getById(filmId);
        User user = userStorage.getById(userId);
        likeStorage.addLike(film, user);
        log.info("Пользователь {} оценил фильм {}", user.getLogin(), film.getName());
    }

    public void removeLike(int filmId, int userId) {
        Film film = filmStorage.getById(filmId);
        User user = userStorage.getById(userId);
        likeStorage.removeLike(film, user);
        log.info("Пользователь {} удалил оценку фильма {}", user.getLogin(), film.getName());
    }

    public List<Film> getTop(int count) {
        log.info("Рейтинг топовых фильмов предоставлен.");
        return likeStorage.getTop(count);
    }
}

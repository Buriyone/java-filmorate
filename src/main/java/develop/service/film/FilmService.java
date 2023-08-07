package develop.service.film;

import develop.exception.NotFoundException;
import develop.model.Film;
import develop.model.User;
import develop.storage.film.FilmStorage;
import develop.storage.user.UserStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    public final FilmStorage filmStorage;
    public final UserStorage userStorage;

    public Film addLike(int filmId, int userId) {
        Film film = filmStorage.getById(filmId);
        User user = userStorage.getById(userId);
        film.getUsersLike().add(user.getId());
        log.info("Пользователь {} оценил фильм {}", user.getLogin(), film.getName());
        return filmStorage.update(film);
    }

    public Film removeLike(int filmId, int userId) {
        Film film = filmStorage.getById(filmId);
        User user = userStorage.getById(userId);
        if (film.getUsersLike().contains(userId)) {
            film.getUsersLike().remove(userId);
            log.info("Пользователь {} удалил оценку фильма {}", user.getLogin(), film.getName());
            return filmStorage.update(film);
        } else {
            throw new NotFoundException("пользователь с id: " + userId
                    + " не оценивал фильм " + film.getName());
        }
    }

    public List<Film> getTop(int count) {
        int likeCount = 0;
        List<Film> topFilms = new ArrayList<>();
        if (!filmStorage.get().isEmpty()) {
            for (Film film : filmStorage.get()) {
                likeCount = Integer.max(likeCount, film.getUsersLike().size());
            }
            while (true) {
                for (Film film : filmStorage.get()) {
                    if (film.getUsersLike().size() == likeCount && topFilms.size() < count) {
                        topFilms.add(film);
                    }
                }
                if (topFilms.size() == count || topFilms.size() == filmStorage.get().size()) {
                    break;
                }
                likeCount--;
            }
        }
        log.info("Рейтинг топовых фильмов предоставлен.");
        return topFilms;
    }
}

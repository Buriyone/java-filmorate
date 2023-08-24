package develop.storage.like;

import develop.model.Film;
import develop.model.User;
import develop.storage.film.FilmStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class InMemoryLikeStorage implements LikeStorage {
    private final Map<Integer, List<User>> likes = new HashMap<>();
    private final FilmStorage filmStorage;

    @Override
    public void addLike(Film film, User user) {
        List<User> users = likes.get(film.getId());
        users.add(user);
        likes.put(film.getId(), users);
        updateRate(film);
    }

    @Override
    public void removeLike(Film film, User user) {
        List<User> users = likes.get(film.getId());
        users.remove(user);
        likes.put(film.getId(), users);
        updateRate(film);
    }

    @Override
    public List<Film> getTop(int count) {
        int likeCount = 0;
        List<Film> topFilms = new ArrayList<>();
        if (!filmStorage.get().isEmpty()) {
            for (Film film : filmStorage.get()) {
                likeCount = Integer.max(likeCount, film.getRate());
            }
            while (true) {
                for (Film film : filmStorage.get()) {
                    if (film.getRate() == likeCount && topFilms.size() < count) {
                        topFilms.add(film);
                    }
                }
                if (topFilms.size() == count || topFilms.size() == filmStorage.get().size()) {
                    break;
                }
                likeCount--;
            }
        }
        return topFilms;
    }

    private void updateRate(Film film) {
        film.setRate(likes.get(film.getId()).size());
        filmStorage.update(film);
    }
}

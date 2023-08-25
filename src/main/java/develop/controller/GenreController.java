package develop.controller;

import develop.model.Genre;
import develop.storage.genre.GenreStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/genres")
public class GenreController {
    private final GenreStorage genreStorage;

    @GetMapping
    public List<Genre> getGenres() {
        log.info("Получен запрос на предоставление списка всех жанров.");
        return genreStorage.get();
    }

    @GetMapping("/{id}")
    public Genre getGenre(@PathVariable int id) {
        log.info("Получен запрос на предоставление жанра по id.");
        return genreStorage.getById(id);
    }
}

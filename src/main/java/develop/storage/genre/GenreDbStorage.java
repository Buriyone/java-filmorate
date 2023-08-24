package develop.storage.genre;

import develop.exception.NotFoundException;
import develop.exception.ValidationException;
import develop.model.Genre;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre getById(int id) {
        if (id != 0) {
            SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM genres WHERE id = ?", id);
            if (sqlRowSet.next()) {
                log.info("жанр: " + sqlRowSet.getString("name") + " успешно предоставлен.");
                return Genre.builder()
                        .id(sqlRowSet.getInt("id"))
                        .name(sqlRowSet.getString("name"))
                        .build();
            } else {
                throw new NotFoundException("жанр не найден.");
            }
        } else {
            throw new ValidationException("id указан некорректно.");
        }
    }

    @Override
    public List<Genre> get() {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM genres");
        List<Genre> genres = new ArrayList<>();
        while (sqlRowSet.next()) {
            genres.add(getById(sqlRowSet.getInt("id")));
        }
        log.info("список жанров успешно предоставлен.");
        return genres;
    }
}

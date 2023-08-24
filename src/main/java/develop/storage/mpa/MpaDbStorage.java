package develop.storage.mpa;

import develop.exception.NotFoundException;
import develop.exception.ValidationException;
import develop.model.Mpa;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@Primary
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Mpa getById(int id) {
        if (id != 0) {
            SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM mpa WHERE id = ?", id);
            if (sqlRowSet.next()) {
                return Mpa.builder()
                        .id(sqlRowSet.getInt("id"))
                        .name(sqlRowSet.getString("name"))
                        .build();
            } else {
                throw new NotFoundException("MPA не найден.");
            }
        } else {
            throw new ValidationException("id указан некорректно.");
        }
    }

    @Override
    public List<Mpa> get() {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM mpa");
        List<Mpa> mpa = new ArrayList<>();
        while (sqlRowSet.next()) {
            mpa.add(getById(sqlRowSet.getInt("id")));
        }
        log.info("список MPA успешно предоставлен.");
        return mpa;
    }
}

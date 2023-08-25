package develop.controller;

import develop.model.Mpa;
import develop.storage.mpa.MpaStorage;
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
@RequestMapping("/mpa")
public class MpaController {
    private final MpaStorage mpaStorage;

    @GetMapping
    public List<Mpa> getMpas() {
        log.info("Получен запрос на предоставление списка всех MPA.");
        return mpaStorage.get();
    }

    @GetMapping("/{id}")
    public Mpa getMpa(@PathVariable int id) {
        log.info("Получен запрос на предоставление MPA по id.");
        return mpaStorage.getById(id);
    }
}

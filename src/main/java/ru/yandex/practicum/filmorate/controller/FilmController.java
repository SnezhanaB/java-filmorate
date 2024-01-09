package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RequestMapping("/films")
@RestController
public class FilmController {
    private static final Map<Integer, Film> films = new HashMap<>();

    @GetMapping()
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @PostMapping()
    public Film create(@Valid @RequestBody Film film) {
        Integer id = films.size() + 1;
        film.setId(id);
        films.put(id, film);
        return film;
    }

    @PutMapping()
    public Film update(@Valid @RequestBody Film film) throws NotFoundException {
        if (!films.containsKey(film.getId())) {
            log.debug("Фильм не найден! " + film);
            throw new NotFoundException("Фильм не найден!");
        }
        films.put(film.getId(), film);
        return film;
    }
}

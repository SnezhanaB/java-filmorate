package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.NotFoundException;
import ru.yandex.practicum.filmorate.model.ValidationException;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@RequestMapping("/films")
@RestController
public class FilmController {
    private static final Map<Integer, Film> films = new HashMap<>();
    private static final LocalDate BIRSDAY_OF_CINEMA = LocalDate.of(1895, 12, 28);

    @GetMapping()
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @PostMapping()
    public Film create(@Valid @RequestBody Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(BIRSDAY_OF_CINEMA)) {
            log.debug("Ошибка валидации 'Дата релиза не может быть раньше дня рождения кино'. " + film.toString());
            throw new ValidationException("Дата релиза не может быть раньше дня рождения кино");
        }
        Integer id = films.size() + 1;
        film.setId(id);
        films.put(id, film);
        return film;
    }

    @PutMapping()
    public Film update(@Valid @RequestBody Film film) throws ValidationException, NotFoundException {
        if (film.getReleaseDate().isBefore(BIRSDAY_OF_CINEMA)) {
            log.debug("Ошибка валидации 'Дата релиза не может быть раньше дня рождения кино'. " + film.toString());
            throw new ValidationException("Дата релиза не может быть раньше дня рождения кино");
        }
        if (!films.containsKey(film.getId())) {
            log.debug("Фильм не найден! " + film.toString());
            throw new NotFoundException("Фильм не найден!");
        }
        films.put(film.getId(), film);
        return film;
    }
}

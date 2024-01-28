package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.service.FilmService;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.*;

@Slf4j
@RequestMapping("/films")
@RestController
public class FilmController {
    private FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    /**
     * `GET /films` — получить все фильмы
     * */
    @GetMapping()
    public List<Film> findAll() {
        return new ArrayList<>(filmService.findAll());
    }

    /**
     * `POST /films` — добавить новый фильм
     * */
    @PostMapping()
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    /**
     * `PUT /films` — обновить фильм
     * */
    @PutMapping()
    public Film update(@Valid @RequestBody Film film) throws NotFoundException {
        return filmService.update(film);
    }

    /**
     * `GET /films/{filmId}` — получить фильм по id
     * */
    @GetMapping("/{filmId}")
    public Film getFilmById(@PathVariable("filmId") Integer filmId) throws NotFoundException {
        return filmService.getFilmById(filmId);
    }

    /**
     * `PUT /films/{id}/like/{userId}` — пользователь ставит лайк фильму
     * */
    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        filmService.addLike(id, userId);
    }

    /**
     * `DELETE /films/{id}/like/{userId}` — пользователь удаляет лайк
     * */
    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        filmService.removeLike(id, userId);
    }

    /**
     * `GET /films/popular?count={count}` — возвращает список из первых `count` фильмов,
     * отсортированных по количеству лайков
     */
    @GetMapping("/popular")
    public List<Film> getPopular(@Positive @RequestParam(name = "count", defaultValue = "10", required = false) Integer count) {
        return filmService.getPopular(count);
    }
}

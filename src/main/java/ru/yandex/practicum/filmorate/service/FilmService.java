package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
public class FilmService {
    private FilmStorage filmStorage;
    private UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Collection<Film> findAll() {
        return filmStorage.getAll();
    }

    public Film getFilmById(int filmId) throws NotFoundException {
        Film founded = filmStorage.getById(filmId);
        if (founded == null) {
            String msg = String.format("Фильм с id=%s не найден!", filmId);
            log.debug(msg);
            throw new NotFoundException(msg);
        }
        return founded;
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) throws NotFoundException {
        Film updated = filmStorage.update(film);
        if (updated == null) {
            String msg = String.format("Фильм с id=%s не найден!", film.getId());
            log.debug(msg);
            throw new NotFoundException(msg);
        }
        return updated;
    }

    public void addLike(Integer id, Integer userId) throws NotFoundException {
        Film film = filmStorage.getById(id);
        if (film == null) {
            String msg = String.format("Фильм с id=%s не найден!", id);
            log.debug(msg);
            throw new NotFoundException(msg);
        }
        User user = userStorage.getById(userId);
        if (user == null) {
            String msg = String.format("Пользователь с id=%s не найден!", userId);
            log.debug(msg);
            throw new NotFoundException(msg);
        }
        film.addLike(userId);
        filmStorage.update(film);
    }

    public void removeLike(Integer id, Integer userId) throws NotFoundException {
        Film film = filmStorage.getById(id);
        if (film == null) {
            String msg = String.format("Фильм с id=%s не найден!", id);
            log.debug(msg);
            throw new NotFoundException(msg);
        }
        User user = userStorage.getById(userId);
        if (user == null) {
            String msg = String.format("Пользователь с id=%s не найден!", userId);
            log.debug(msg);
            throw new NotFoundException(msg);
        }
        film.removeLike(userId);
        filmStorage.update(film);
    }

    public List<Film> getPopular(Integer count) {
        return filmStorage.getAll().stream()
                .sorted((film1, film2) -> film2.getRate() - film1.getRate())
                .limit(count).collect(Collectors.toList());
    }

}
package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.AbstractStorage;

import java.util.List;

public interface FilmStorage extends AbstractStorage<Film> {
    List<Genre> getGenres();

    Genre getGenre(Integer id);

    Mpa getMpa(int id);

    List<Mpa> getMpas() throws NotFoundException;

    void addLike(Integer filmId, Integer userId) throws NotFoundException;

    void removeLike(Integer filmId, Integer userId) throws NotFoundException;
}

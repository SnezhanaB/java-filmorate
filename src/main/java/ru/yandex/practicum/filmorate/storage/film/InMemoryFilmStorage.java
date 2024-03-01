package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Qualifier("inMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {

    private static final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Collection<Film> getAll() {
        return films.values();
    }

    @Override
    public Film create(Film film) {
        int id = films.size() + 1;
        film.setId(id);
        films.put(id, film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return film;
        }
        return null;
    }

    @Override
    public Film getById(Integer id) {
        return films.get(id);
    }

    @Override
    public boolean delete(Integer id) {
        return films.remove(id) != null;
    }

    @Override
    public List<Genre> getGenres() {
        return null;
    }

    @Override
    public Genre getGenre(Integer id) {
        return null;
    }

    @Override
    public Mpa getMpa(int id) {
        return null;
    }

    @Override
    public List<Mpa> getMpas() {
        return null;
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
    }

    @Override
    public void removeLike(Integer filmId, Integer userId) throws NotFoundException {
    }
}

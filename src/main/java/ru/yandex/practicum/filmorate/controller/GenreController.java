package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Slf4j
@RequestMapping("/genres")
@RestController
public class GenreController {
    private FilmService filmService;

    @Autowired
    public GenreController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping()
    public List<Genre> getGenres() {
        return filmService.getGenres();
    }

    @GetMapping("/{id}")
    public Genre getGenre(@PathVariable int id) {
        return filmService.getGenre(id);
    }
}

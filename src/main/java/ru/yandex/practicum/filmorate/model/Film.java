package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.yandex.practicum.filmorate.constraint.CinemaBirthdayConstraint;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Film.
 */
@Data
public class Film {
    private Integer id;
    @NotEmpty(message = "Название не может быть пустым")
    @Size(max = 100, message = "Максимальная длина названия — 100 символов")
    private String name;
    @Size(max = 200, message = "Максимальная длина описания — 200 символов")
    private String description;
    @CinemaBirthdayConstraint
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    /**
     * Продолжительность в минутах
     */
    @Positive(message = "Продолжительность фильма должна быть положительной")
    private int duration;
    /**
     * Количество пользователей лайкнувших фильм
     */
    private int rate = 0;
    /**
     * Id's Жанров фильма, может быть несколько
     */
    private List<Genre> genres = new ArrayList<>();
    /**
     * Id Рейтинга Ассоциации кинокомпаний
     */
    private Mpa mpa;

    public Film(
            Integer id,
            String name,
            String description,
            LocalDate releaseDate,
            int duration,
            int rate,
            List<Genre> genres,
            Mpa mpa
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rate = rate;
        this.genres = genres;
        this.mpa = mpa;
    }

}

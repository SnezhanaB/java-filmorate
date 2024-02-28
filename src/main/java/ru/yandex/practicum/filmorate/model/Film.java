package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import ru.yandex.practicum.filmorate.constraint.CinemaBirthdayConstraint;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
     * Id's пользователей лайкнувших фильм
     */
    @JsonIgnore
    private Set<Integer> likedUserIds = new HashSet<>();
    /**
     * Количество пользователей лайкнувших фильм
     */
    private int rate = 0;
    /**
     * Id's Жанров фильма, может быть несколько
     */
    private Set<Genre> genres = new HashSet<>();
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
            Set<Integer> likedUserIds,
            int rate,
            Set<Genre> genres,
            Mpa mpa
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likedUserIds = likedUserIds;
        this.rate = rate;
        this.genres = genres;
        this.mpa = mpa;
    }

    public void addLike(int userId) {
        likedUserIds.add(userId);
        rate = likedUserIds.size();
    }

    public void removeLike(int userId) {
        likedUserIds.remove(userId);
        rate = likedUserIds.size();
    }
}

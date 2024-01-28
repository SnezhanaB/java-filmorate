package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import ru.yandex.practicum.filmorate.constraint.CinemaBirthdayConstraint;

import javax.validation.constraints.*;
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
    private String name;

    @Size(max = 200, message = "Максимальная длина описания — 200 символов")
    private String description;

    @CinemaBirthdayConstraint
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    /** Продолжительность в минутах */
    @Positive(message = "Продолжительность фильма должна быть положительной")
    private int duration;

    @JsonIgnore
    private Set<Integer> likedUserIds = new HashSet<>();

    @JsonIgnore
    private int rate = 0;

    public void addLike(int userId) {
        likedUserIds.add(userId);
        rate = likedUserIds.size();
    }

    public void removeLike(int userId) {
        likedUserIds.remove(userId);
        rate = likedUserIds.size();
    }
}

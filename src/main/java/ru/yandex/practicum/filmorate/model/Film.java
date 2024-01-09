package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.yandex.practicum.filmorate.constraint.CinemaBirthdayConstraint;

import javax.validation.constraints.*;
import java.time.LocalDate;

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
    @Positive(message = "Продолжительность фильма должна быть положительной")
    private int duration;
}

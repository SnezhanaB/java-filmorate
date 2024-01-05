package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Date;

/**
 * Film.
 */
@Data
public class Film {
    private Integer id;
    @NotEmpty(message = "Название не может быть пустым")
    private String name;
    @Size(max=200, message = "Максимальная длина описания — 200 символов")
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть положительной")
    private int duration;
}

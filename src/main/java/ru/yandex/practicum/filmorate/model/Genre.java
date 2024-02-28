package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class Genre {
    private Integer id;
    @NotEmpty(message = "Название не может быть пустым")
    @Size(max = 64, message = "Максимальная длина названия — 64 символов")
    private String name;
}

package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class Mpa {
    private Integer id;
    @NotEmpty(message = "Название не может быть пустым")
    @Size(max = 10, message = "Максимальная длина названия — 10 символов")
    private String name;
    @Size(max = 100, message = "Максимальная длина описания — 100 символов")
    private String description;
}

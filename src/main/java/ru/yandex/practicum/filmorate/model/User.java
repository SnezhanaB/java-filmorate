package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
public class User {
    private int id;
    @Email
    private String email;
    @NotEmpty(message = "Логин не может быть пустым")
    @Pattern(regexp = "\\S+$", message = "Логин не может содержать пробелы")
    private String login;
    private String name;
    private LocalDate birthday;
}

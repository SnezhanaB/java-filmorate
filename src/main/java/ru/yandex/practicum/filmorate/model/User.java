package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import ru.yandex.practicum.filmorate.constraint.BirthdayConstraint;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
public class User {
    private Integer id;
    @Email
    private String email;
    @NotEmpty(message = "Логин не может быть пустым")
    @Pattern(regexp = "\\S+$", message = "Логин не может содержать пробелы")
    private String login;
    private String name;
    @BirthdayConstraint
    private LocalDate birthday;
    @JsonIgnore
    private Set<Integer> friends = new HashSet<>();
    /**
     * Статус для связи «дружба» между двумя пользователями
     * неподтверждённая (false) — когда один пользователь отправил запрос на добавление другого пользователя в друзья,
     * подтверждённая (true) — когда второй пользователь согласился на добавление.
     */
    @JsonIgnore
    private Map<Integer, Boolean> status = new HashMap<>();

    public User(Integer id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public void addFriend(int userId) {
        friends.add(userId);
    }

    public void removeFriend(int userId) {
        friends.remove(userId);
    }
}

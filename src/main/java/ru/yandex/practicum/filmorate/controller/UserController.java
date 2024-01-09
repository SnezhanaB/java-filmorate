package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequestMapping("/users")
@RestController
public class UserController {
    private static final Map<Integer, User> users = new HashMap<>();

    @GetMapping()
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @PostMapping()
    public User create(@Valid @RequestBody User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        int id = users.size() + 1;
        user.setId(id);
        users.put(id, user);
        return user;
    }

    @PutMapping()
    public User update(@Valid @RequestBody User user) throws NotFoundException {
        if (!users.containsKey(user.getId())) {
            log.debug("Пользователь не найден! " + user);
            throw new NotFoundException("Пользователь не найден!");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }
}

package ru.yandex.practicum.filmorate.controller;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Validated
@RequestMapping("/users")
@RestController
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public List<User> findAll() {
        return new ArrayList<>(userService.findAll());
    }

    /**
     * Создать пользователя
     */
    @PostMapping()
    public User create(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    /**
     * Обновить пользователя
     */
    @PutMapping()
    public User update(@Valid @RequestBody User user) throws NotFoundException {
        return userService.update(user);
    }

    /**
     * Получить список общих друзей
     */
    @GetMapping("/{userId}")
    public User getUserById(@PathVariable("userId") Integer userId) throws NotFoundException {
        return userService.getUserById(userId);
    }

    /**
     * Добавить в список друзей
     */
    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(
        @NonNull @PathVariable("userId") Integer userId,
        @NonNull @PathVariable("friendId") Integer friendId
    ) throws NotFoundException {
        userService.addFriend(userId, friendId);
    }

    /**
     * Удалить из списка друзей
     */
    @DeleteMapping("/{userId}/friends/{friendId}")
    public void removeFriend(
        @NonNull @PathVariable("userId") Integer userId,
        @NonNull @PathVariable("friendId") Integer friendId
    ) throws NotFoundException {
        userService.removeFriend(userId, friendId);
    }

    /**
     * Получить список друзей
     */
    @GetMapping("/{userId}/friends")
    public List<User> getFriends(@NonNull @PathVariable("userId") Integer userId) throws NotFoundException {
        return userService.getFriends(userId);
    }

    /**
     * Получить список друзей, общих с другим пользователем
     */
    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<User> getCommonFriends(
        @PathVariable("userId") Integer userId,
        @PathVariable("otherId") Integer otherId
    ) throws NotFoundException {
        return userService.getCommonFriends(userId, otherId);
    }


}

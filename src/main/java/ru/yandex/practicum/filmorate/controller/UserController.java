package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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
    public void addFriend(@PathVariable("userId") Integer userId, @PathVariable("friendId") Integer friendId) throws NotFoundException {
        User user = userService.getUserById(userId);
        User friend = userService.getUserById(friendId);
        user.addFriend(friendId);
        friend.addFriend(userId);
    }

    /**
     * Удалить из списка друзей
     */
    @DeleteMapping("/{userId}/friends/{friendId}")
    public void removeFriend(@PathVariable("userId") Integer userId, @PathVariable("friendId") Integer friendId) throws NotFoundException {
        User user = userService.getUserById(userId);
        User friend = userService.getUserById(friendId);
        user.removeFriend(friendId);
        friend.removeFriend(userId);
    }

    /**
     * Получить список друзей
     */
    @GetMapping("/{userId}/friends")
    public List<User> getFriends(@PathVariable("userId") Integer userId) throws NotFoundException {
        User user = userService.getUserById(userId);
        return user.getFriends().stream().map((friendId) -> userService.getUserById(friendId)).collect(Collectors.toList());
    }

    /**
     * Получить список друзей, общих с другим пользователем
     */
    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("userId") Integer userId, @PathVariable("otherId") Integer otherId) throws NotFoundException {
        User user = userService.getUserById(userId);
        User other = userService.getUserById(otherId);
        List<Integer> commonFriendIds = user.getFriends().stream().filter((id) -> other.getFriends().contains(id)).collect(Collectors.toList());
        return commonFriendIds.stream().map((friendId) -> userService.getUserById(friendId)).collect(Collectors.toList());
    }


}

package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class UserService {
    private UserStorage userStorage;

    @Autowired
    public UserService(@Autowired @Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    /**
     * Получить всех пользователей
     */
    public Collection<User> findAll() {
        return userStorage.getAll();
    }

    /**
     * Получить пользователя по id
     */
    public User getUserById(int userId) throws NotFoundException {
        User founded = userStorage.getById(userId);
        if (founded == null) {
            String msg = String.format("Пользователь с id=%s не найден!", userId);
            log.debug(msg);
            throw new NotFoundException(msg);
        }
        return founded;
    }

    /**
     * Создать пользователя
     */
    public User create(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        return userStorage.create(user);
    }

    /**
     * Обновить пользователя
     */
    public User update(User user) throws NotFoundException {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        User updated = userStorage.update(user);
        if (updated == null) {
            String msg = String.format("Пользователь с id=%s не найден!", user.getId());
            log.debug(msg);
            throw new NotFoundException(msg);
        }
        return updated;
    }

    /**
     * Добавить в список друзей
     */
    public void addFriend(int userId, int friendId) throws NotFoundException {
        userStorage.addFriend(userId, friendId);
    }

    /**
     * Удалить из списка друзей
     */
    public void removeFriend(int userId, int friendId) throws NotFoundException {
        userStorage.removeFriend(userId, friendId);
    }

    /**
     * Получить список друзей
     */
    public List<User> getFriends(int userId) throws NotFoundException {
        return userStorage.getFriends(userId);
    }

    /**
     * Получить список друзей, общих с другим пользователем
     */
    public List<User> getCommonFriends(int userId, int otherId) throws NotFoundException {
        return userStorage.getCommonFriends(userId, otherId);
    }
}

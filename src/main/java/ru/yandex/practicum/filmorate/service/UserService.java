package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.util.Collection;

@Slf4j
@Service
public class UserService {
    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> findAll() {
        return userStorage.getAll();
    }

    public User getUserById(int userId) throws NotFoundException {
        User founded = userStorage.getById(userId);
        if (founded == null) {
            String msg = String.format("Пользователь с id=%s не найден!", userId);
            log.debug(msg);
            throw new NotFoundException(msg);
        }
        return founded;
    }

    public User create(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        return userStorage.create(user);
    }

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

}

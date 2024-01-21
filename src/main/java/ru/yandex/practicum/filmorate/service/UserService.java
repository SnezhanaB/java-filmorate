package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
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
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.addFriend(friendId);
        friend.addFriend(userId);
    }

    /**
     * Удалить из списка друзей
     */
    public void removeFriend(int userId, int friendId) throws NotFoundException {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.removeFriend(friendId);
        friend.removeFriend(userId);
    }

    /**
     * Получить список друзей
     */
    public List<User> getFriends(int userId) throws NotFoundException {
        User user = getUserById(userId);
        return user.getFriends().stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    /**
     * Получить список друзей, общих с другим пользователем
     */
    public List<User> getCommonFriends(int userId, int otherId) throws NotFoundException {
        User user = getUserById(userId);
        User other = getUserById(otherId);
        List<Integer> commonFriendIds = user.getFriends().stream()
                .filter((id) -> other.getFriends().contains(id))
                .collect(Collectors.toList());
        return commonFriendIds.stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }
}

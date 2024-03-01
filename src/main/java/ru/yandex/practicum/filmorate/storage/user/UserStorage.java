package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.AbstractStorage;

import java.util.List;

public interface UserStorage extends AbstractStorage<User> {
    List<User> getFriends(int userId);

    void removeFriend(int userId, int friendId) throws NotFoundException;

    void addFriend(int userId, int friendId) throws NotFoundException;

    List<User> getCommonFriends(int userId, int otherId) throws NotFoundException;
}

package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Qualifier("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {
    private static final Map<Integer, User> users = new HashMap<>();

    @Override
    public List<User> getFriends(int userId) {
        return null;
    }

    @Override
    public void removeFriend(int userId, int friendId) throws NotFoundException {

    }

    @Override
    public void addFriend(int userId, int friendId) throws NotFoundException {

    }

    @Override
    public List<User> getCommonFriends(int userId, int otherId) throws NotFoundException {
        return null;
    }

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public User create(User user) {
        int id = users.size() + 1;
        user.setId(id);
        users.put(id, user);
        return user;
    }

    @Override
    public User update(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return user;
        }
        return null;
    }

    @Override
    public User getById(Integer id) {
        return users.get(id);
    }

    @Override
    public boolean delete(Integer id) {
        return users.remove(id) != null;
    }
}

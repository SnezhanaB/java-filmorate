package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;

public interface AbstractStorage<T> {

    Collection<T> getAll();

    T create(T item);

    T update(T item);

    T getById(Integer id);

    T delete(Integer id);
}

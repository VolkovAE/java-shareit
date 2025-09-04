package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

@Deprecated
public interface UserStorage {
    User add(User user);

    Optional<User> getById(Long id);

    Collection<User> findAll();

    User update(User user);

    User delete(User user);
}

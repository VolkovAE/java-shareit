package ru.practicum.shareit.user.storage;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.Reflection;
import ru.practicum.shareit.util.Utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Deprecated
@Repository
@Qualifier("InMemoryUserStorageImpl")
public class InMemoryUserStorageImpl implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    private static final Logger log = LoggerFactory.getLogger(InMemoryUserStorageImpl.class);

    @Override
    public User add(User user) {
        //Если пользователь с указанным адресом электронной почты уже был добавлен ранее,
        // то генерируется исключение DuplicatedDataException с описанием: "Этот имейл уже используется".
        if (!isEmailFree(user.getEmail()))
            throw new DuplicatedDataException("Нельзя создать пользователя по причине: " +
                    "этот имейл уже используется.", log);

        // формируем дополнительные данные
        user.setId(Utils.getNextId(users));

        // сохраняем нового пользователя в памяти приложения
        users.put(user.getId(), user);

        log.info("Добавлен новый пользователь {}.", user);

        return user;
    }

    @Override
    public Optional<User> getById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Collection<User> findAll() {
        log.info("Получен список пользователей.");

        return users.values();
    }

    @Override
    public User update(User newUser) {
        if (users.containsKey(newUser.getId())) {
            // удаляем данные о пользователе с id из памяти приложения
            User oldUser = users.get(newUser.getId());

            if (!StringUtils.isBlank(newUser.getEmail())) {
                if (!newUser.getEmail().equals(oldUser.getEmail())) {
                    //Если при обновлении данных пользователя, указан новый адрес электронной почты и
                    // в приложении уже есть пользователь с таким адресом,
                    // то должно генерироваться исключение DuplicatedDataException с описанием: "Этот имейл уже используется".
                    if (!isEmailFree(newUser.getEmail()))
                        throw new DuplicatedDataException(String.format("Нельзя обновить данные пользователя с id %s " +
                                "по причине: нельзя использовать имейл, который уже используется.", newUser.getId()), log);
                }
            }

            // обновляем содержимое
            BeanUtils.copyProperties(newUser, oldUser, Reflection.getIgnoreProperties(newUser));

            log.info("Обновлены данные пользователя {}.", oldUser);

            return oldUser;
        }
        throw new NotFoundException(String.format("Не обновлены данные пользователя с id %s по причине: " +
                "пользователь с таким id не найден.", newUser.getId()), log);
    }

    @Override
    public User delete(User delUser) {
        if (users.containsKey(delUser.getId())) {
            // удаляем данные о пользователе с id из памяти приложения
            User removeUser = users.remove(delUser.getId());

            log.info("Удален пользователь {}.", removeUser);

            return removeUser;
        }
        throw new NotFoundException(String.format("Нельзя удалить данные пользователя с id %s " +
                "по причине: пользователь с таким id не найден.", delUser.getId()), log);
    }

    private boolean isEmailFree(final String email) {
        return findAll().stream().noneMatch(curUser -> curUser.getEmail().equals(email));
    }
}

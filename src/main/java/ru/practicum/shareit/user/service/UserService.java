package ru.practicum.shareit.user.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;

@Service
public class UserService {
    private final UserStorage userStorage;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(@Qualifier("InMemoryUserStorageImpl") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public UserDto add(NewUserRequest userRequest) {
        User user = UserMapper.mapToUser(userRequest);

        user = userStorage.add(user);

        return UserMapper.mapToUserDto(user);
    }

    public UserDto getById(Long userId) {
        User user = userStorage.getById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с id = " + userId + " не найден.", log));

        return UserMapper.mapToUserDto(user);
    }

    public Collection<UserDto> findAll() {
        return userStorage.findAll().stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    public UserDto update(Long userId, UpdateUserRequest userRequest) {
        User user = UserMapper.mapToUser(userId, userRequest);

        user = userStorage.update(user);

        return UserMapper.mapToUserDto(user);
    }

    public UserDto delete(final Long userId) {
        User removeUser = userStorage.getById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден.", log));

        removeUser = userStorage.delete(removeUser);

        return UserMapper.mapToUserDto(removeUser);
    }
}

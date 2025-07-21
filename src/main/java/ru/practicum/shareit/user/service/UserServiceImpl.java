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
import ru.practicum.shareit.user.storage.UserRepository;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;

@Service
@Qualifier("UserServiceImpl")
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserStorage userStorage;
    private final UserMapper userMapper;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           @Qualifier("InMemoryUserStorageImpl") UserStorage userStorage,
                           UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userStorage = userStorage;
        this.userMapper = userMapper;
    }

    public UserDto add(NewUserRequest userRequest) {
        User user = userMapper.toUser(userRequest);

        user = userStorage.add(user);

        return userMapper.toUserDto(user);
    }

    public UserDto getById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден.", log));

        return userMapper.toUserDto(user);
    }

    public Collection<UserDto> findAll() {
        return userStorage.findAll().stream()
                .map(userMapper::toUserDto)
                .toList();
    }

    public UserDto update(Long userId, UpdateUserRequest userRequest) {
        User user = userMapper.toUser(userId, userRequest);

        user = userStorage.update(user);

        return userMapper.toUserDto(user);
    }

    public UserDto delete(final Long userId) {
        User removeUser = userStorage.getById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден.", log));

        removeUser = userStorage.delete(removeUser);

        return userMapper.toUserDto(removeUser);
    }
}

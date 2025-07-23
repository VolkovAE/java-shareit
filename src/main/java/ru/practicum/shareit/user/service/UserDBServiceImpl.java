package ru.practicum.shareit.user.service;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;
import ru.practicum.shareit.util.Reflection;

import java.util.Collection;

@Service
@Qualifier("UserDBServiceImpl")
public class UserDBServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private static final Logger log = LoggerFactory.getLogger(UserDBServiceImpl.class);

    public UserDBServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto add(NewUserRequest userRequest) {
        User user = userMapper.toUser(userRequest);

        user = userRepository.save(user);

        log.info("Добавлен новый пользователь {}.", user);

        return userMapper.toUserDto(user);
    }

    @Override
    public UserDto getById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден.", log));

        return userMapper.toUserDto(user);
    }

    @Override
    public Collection<UserDto> findAll(int indexPage, int countItems) {
        // создаём описание сортировки по полю id
        Sort sortById = Sort.by(Sort.Direction.ASC, "id");

        // затем создаём описание страницы с индексом indexPage размером countItems элемента
        Pageable page = PageRequest.of(indexPage, countItems, sortById);

        // запрашиваем у базы данных страницу с данными
        Page<User> userPage = userRepository.findAll(page);

        log.info("Получен список пользователей.");

        return userPage.getContent().stream()
                .map(userMapper::toUserDto)
                .toList();
    }

    @Override
    public UserDto update(Long userId, UpdateUserRequest userRequest) {
        User oldUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден.", log));

        User newUser = userMapper.toUser(userId, userRequest);

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

        userRepository.save(oldUser);

        log.info("Обновлены данные пользователя {}.", oldUser);

        return userMapper.toUserDto(oldUser);
    }

    @Override
    public UserDto delete(Long userId) {
        User removeUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден.", log));

        userRepository.delete(removeUser);

        log.info("Удален пользователь {}.", removeUser);

        return userMapper.toUserDto(removeUser);
    }

    private boolean isEmailFree(final String email) {
        return userRepository.findByEmailIgnoreCase(email).isEmpty();
    }
}

package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    public UserDto add(NewUserRequest userRequest);

    public UserDto getById(Long userId);

    public Collection<UserDto> findAll(int indexPage, int countItems);

    public UserDto update(Long userId, UpdateUserRequest userRequest);

    public UserDto delete(final Long userId);
}

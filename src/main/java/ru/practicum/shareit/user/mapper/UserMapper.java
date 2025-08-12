package ru.practicum.shareit.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.mapper.IgnoreUnmappedMapperConfig;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, config = IgnoreUnmappedMapperConfig.class)
public interface UserMapper {
    User toUser(NewUserRequest request);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "request.email", target = "email")
    @Mapping(source = "request.name", target = "name")
    User toUser(Long id, UpdateUserRequest request);

    UserDto toUserDto(User user);
}

package ru.practicum.shareit.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.mapper.IgnoreUnmappedMapperConfig;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewRequestItem;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, config = IgnoreUnmappedMapperConfig.class)
public interface ItemRequestMapper {
    ItemRequestMapper INSTANCE = Mappers.getMapper(ItemRequestMapper.class);

    @Mapping(ignore = true, target = "id")
    @Mapping(source = "requestItem.description", target = "description")
    @Mapping(source = "user", target = "requestor")
    @Mapping(source = "requestItem", target = "created", qualifiedByName = "getInstantNow")
    ItemRequest toItemRequest(NewRequestItem requestItem, User user);

    @Named("getInstantNow")
    default Instant getInstantNow(NewRequestItem requestItem) {
        return Instant.now();
    }

    @Mapping(source = "itemRequest.created", target = "created", qualifiedByName = "toLocalDateTime")
    ItemRequestDto toItemRequestDto(ItemRequest itemRequest);

    @Named("toLocalDateTime")
    default LocalDateTime toLocalDateTime(Instant instant) {
        if (Objects.isNull(instant)) return null;
        else return LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    }
}

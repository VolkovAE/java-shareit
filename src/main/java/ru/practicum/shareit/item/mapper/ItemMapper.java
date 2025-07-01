package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.mapper.IgnoreUnmappedMapperConfig;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, config = IgnoreUnmappedMapperConfig.class)
@Component
public interface ItemMapper {
    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    @Mapping(source = "request.name", target = "name")
    @Mapping(source = "request.description", target = "description")
    @Mapping(source = "request.available", target = "available")
    @Mapping(source = "owner", target = "owner")
    Item toItem(NewItemRequest request, User owner);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "request.name", target = "name")
    @Mapping(source = "request.description", target = "description")
    @Mapping(source = "request.available", target = "available")
    @Mapping(source = "owner", target = "owner")
    Item toItem(Long id, UpdateItemRequest request, User owner);

    ItemDto toItemDto(Item item);
}

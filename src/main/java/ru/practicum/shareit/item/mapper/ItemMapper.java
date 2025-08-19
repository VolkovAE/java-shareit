package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.DateLastNextBooking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.mapper.IgnoreUnmappedMapperConfig;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, config = IgnoreUnmappedMapperConfig.class)
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

    @Mapping(source = "item.owner.id", target = "ownerId")
    ItemOnRequestDto toItemOnRequestDto(Item item);

    @Mapping(source = "dateLastNextBooking.lastStart", target = "lastBooking", qualifiedByName = "toLocalDateTime")
    @Mapping(source = "dateLastNextBooking.nextStart", target = "nextBooking", qualifiedByName = "toLocalDateTime")
    @Mapping(source = "commentDtoList", target = "commentDtoList")
    ItemWithDateDto toItemWithDateDto(Item item, DateLastNextBooking dateLastNextBooking, List<CommentDto> commentDtoList);

    @Named("toLocalDateTime")
    default LocalDateTime toLocalDateTime(Instant instant) {
        if (Objects.isNull(instant)) return null;
        else return LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    }

    @Mapping(ignore = true, target = "id")
    @Mapping(source = "request.text", target = "text")
    @Mapping(source = "item", target = "item")
    @Mapping(source = "author", target = "author")
    @Mapping(source = "request", target = "created", qualifiedByName = "getInstantNow")
    Comment toComment(NewCommentRequest request, Item item, User author);

    @Named("getInstantNow")
    default Instant getInstantNow(NewCommentRequest request) {
        return Instant.now();
    }

    @Mapping(source = "comment.author.name", target = "authorName")
    @Mapping(source = "comment.created", target = "created", qualifiedByName = "toLocalDateTime")
    CommentDto toCommentDto(Comment comment);
}

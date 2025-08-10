package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService {
    ItemDto add(NewItemRequest itemRequest, Long userId);

    ItemDto getById(Long itemId);

    Collection<ItemWithDateDto> findAll(Long userId);

    Collection<ItemDto> findAllByText(String textSearch);

    ItemDto update(Long itemId, UpdateItemRequest itemRequest, Long userId);

    ItemDto delete(final Long itemId, final Long userId);

    Boolean isOwner(Item item, Long userId);

    CommentDto addComment(Long itemId, Long userId, NewCommentRequest commentRequest);
}

package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService {
    ItemDto add(NewItemRequest itemRequest, Long userId);

    ItemDto getById(Long itemId);

    Collection<ItemDto> findAll(Long userId);

    Collection<ItemDto> findAllByText(String textSearch);

    ItemDto update(Long itemId, UpdateItemRequest itemRequest, Long userId);

    ItemDto delete(final Long itemId, final Long userId);

    Boolean isOwner(Item item, Long userId);
}

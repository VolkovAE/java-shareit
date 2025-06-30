package ru.practicum.shareit.item.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.InternalServerException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;
import java.util.List;

@Service
@Qualifier("ItemServiceImpl")
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    private static final Logger log = LoggerFactory.getLogger(ItemServiceImpl.class);

    @Autowired
    public ItemServiceImpl(@Qualifier("InMemoryItemStorageImpl") ItemStorage itemStorage,
                           @Qualifier("InMemoryUserStorageImpl") UserStorage userStorage) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
    }

    @Override
    public ItemDto add(NewItemRequest itemRequest, Long userId) {
        User user = userStorage.getById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с id = " + userId + " не найден.", log));

        Item item = ItemMapper.mapToItem(itemRequest, user);

        item = itemStorage.add(item);

        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public ItemDto getById(Long itemId) {
        Item item = itemStorage.getById(itemId).orElseThrow(
                () -> new NotFoundException("Вещь с id = " + itemId + " не найдена.", log));

        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public Collection<ItemDto> findAll(Long userId) {
        if (userId == 0) {
            return itemStorage.findAll().stream()
                    .map(ItemMapper::mapToItemDto)
                    .toList();
        } else {
            User owner = userStorage.getById(userId).orElseThrow(
                    () -> new NotFoundException("Пользователь с id = " + userId + " не найден.", log));

            return itemStorage.findAll(owner).stream()
                    .map(ItemMapper::mapToItemDto)
                    .toList();
        }
    }

    @Override
    public Collection<ItemDto> findAllByText(String textSearch) {
        if (textSearch.isBlank()) return List.of();

        return itemStorage.findAllByText(textSearch).stream()
                .map(ItemMapper::mapToItemDto)
                .toList();
    }

    @Override
    public ItemDto update(Long itemId, UpdateItemRequest itemRequest, Long userId) {
        // проверяем, что вещь с таким id существует
        Item item = itemStorage.getById(itemId).orElseThrow(
                () -> new NotFoundException("Вещь с id = " + itemId + " не найдена.", log));

        // проверяем, что пользователь с таким id существует
        User user = userStorage.getById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с id = " + userId + " не найден.", log));

        // проверяем выполнение правила: Редактировать вещь может только её владелец.
        if (!user.equals(item.getOwner()))
            throw new InternalServerException("Редактировать данные по вещи может только ее владелец." +
                    "Пользователь " + user +
                    "не является владельцем вещи " + item + "." +
                    "Редактирование данных по вещи запрещено.");

        Item newItem = ItemMapper.mapToItem(itemId, itemRequest, user);

        item = itemStorage.update(newItem);

        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public ItemDto delete(final Long itemId, final Long userId) {
        // проверяем, что вещь с таким id существует
        Item removeItem = itemStorage.getById(itemId).orElseThrow(
                () -> new NotFoundException("Вещь с id = " + itemId + " не найдена.", log));

        // проверяем, что пользователь с таким id существует
        User user = userStorage.getById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с id = " + userId + " не найден.", log));

        // проверяем выполнение правила: Редактировать вещь может только её владелец.
        if (!user.equals(removeItem.getOwner()))
            throw new InternalServerException("Удалить данные по вещи может только ее владелец." +
                    "Пользователь " + user +
                    "не является владельцем вещи " + removeItem + "." +
                    "Удаление данных по вещи запрещено.");

        removeItem = itemStorage.delete(removeItem);

        return ItemMapper.mapToItemDto(removeItem);
    }
}

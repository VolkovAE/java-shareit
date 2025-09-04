package ru.practicum.shareit.item.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.InternalServerException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;
import java.util.List;

@Deprecated
@Service
@Qualifier("ItemServiceImpl")
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;
    private final ItemMapper itemMapper;

    private static final Logger log = LoggerFactory.getLogger(ItemServiceImpl.class);

    @Autowired
    public ItemServiceImpl(@Qualifier("InMemoryItemStorageImpl") ItemStorage itemStorage,
                           @Qualifier("InMemoryUserStorageImpl") UserStorage userStorage,
                           ItemMapper itemMapper) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
        this.itemMapper = itemMapper;
    }

    @Override
    public ItemDto add(NewItemRequest itemRequest, Long userId) {
//        User user = userStorage.getById(userId).orElseThrow(
//                () -> new NotFoundException("Пользователь с id = " + userId + " не найден.", log));
//
//        Item item = itemMapper.toItem(itemRequest, user);
//
//        item = itemStorage.add(item);
//
//        return itemMapper.toItemDto(item);
        return null;
    }

    @Override
    public ItemWithDateDto getById(Long itemId, Long userId) {
        Item item = itemStorage.getById(itemId).orElseThrow(
                () -> new NotFoundException("Вещь с id = " + itemId + " не найдена.", log));

        return null;    //itemMapper.toItemDto(item);
    }

    @Override
    public Collection<ItemWithDateDto> findAll(Long userId) {
        return null;    // устаревшая реализация интерфейса
//        if (userId == 0) {
//            return itemStorage.findAll().stream()
//                    .map(itemMapper::toItemDto)
//                    .toList();
//        } else {
//            User owner = userStorage.getById(userId).orElseThrow(
//                    () -> new NotFoundException("Пользователь с id = " + userId + " не найден.", log));
//
//            return itemStorage.findAll(owner).stream()
//                    .map(itemMapper::toItemDto)
//                    .toList();
//        }
    }

    @Override
    public Collection<ItemDto> findAllByText(String textSearch) {
        if (textSearch.isBlank()) return List.of();

        return itemStorage.findAllByText(textSearch).stream()
                .map(itemMapper::toItemDto)
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

        Item newItem = itemMapper.toItem(itemId, itemRequest, user);

        item = itemStorage.update(newItem);

        return itemMapper.toItemDto(item);
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

        return itemMapper.toItemDto(removeItem);
    }

    @Override
    public Boolean isOwner(Item item, Long userId) {
        return item.getOwner().getId().equals(userId);
    }

    @Override
    public CommentDto addComment(Long itemId, Long userId, NewCommentRequest commentRequest) {
        return null;
    }
}

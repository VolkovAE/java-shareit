package ru.practicum.shareit.item.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;
import ru.practicum.shareit.util.Reflection;

import java.util.Collection;

@Service
@Qualifier("ItemDBServiceImpl")
public class ItemDBServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;

    private static final Logger log = LoggerFactory.getLogger(ItemDBServiceImpl.class);

    @Autowired
    public ItemDBServiceImpl(ItemRepository itemRepository,
                             UserRepository userRepository,
                             ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.itemMapper = itemMapper;
    }

    @Override
    public ItemDto add(NewItemRequest itemRequest, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с id = " + userId + " не найден.", log));

        Item item = itemMapper.toItem(itemRequest, user);

        item = itemRepository.save(item);

        log.info("Добавлена новая вещь {}.", item);

        return itemMapper.toItemDto(item);
    }

    @Override
    public ItemDto getById(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new NotFoundException("Вещь с id = " + itemId + " не найдена.", log));

        log.info("Предоставлены данные по вещи {}.", item);

        return itemMapper.toItemDto(item);
    }

    @Override
    public Collection<ItemDto> findAll(Long userId) {
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

        return null;
    }

    @Override
    public Collection<ItemDto> findAllByText(String textSearch) {
//        if (textSearch.isBlank()) return List.of();
//
//        return itemStorage.findAllByText(textSearch).stream()
//                .map(itemMapper::toItemDto)
//                .toList();

        return null;
    }

    @Override
    public ItemDto update(Long itemId, UpdateItemRequest itemRequest, Long userId) {
        // проверяем, что вещь с таким id существует
        Item oldItem = itemRepository.findById(itemId).orElseThrow(
                () -> new NotFoundException("Вещь с id = " + itemId + " не найдена.", log));

        // проверяем, что пользователь с таким id существует
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с id = " + userId + " не найден.", log));

        // проверяем выполнение правила: Редактировать вещь может только её владелец.
        if (!user.equals(oldItem.getOwner()))
            throw new InternalServerException("Редактировать данные по вещи может только ее владелец." +
                    "Пользователь " + user +
                    "не является владельцем вещи " + oldItem + "." +
                    "Редактирование данных по вещи запрещено.");

        Item newItem = itemMapper.toItem(itemId, itemRequest, user);

        // обновляем содержимое
        BeanUtils.copyProperties(newItem, oldItem, Reflection.getIgnoreProperties(newItem));

        oldItem = itemRepository.save(oldItem);

        log.info("Обновлены данные по вещи {}.", oldItem);

        return itemMapper.toItemDto(oldItem);
    }

    @Override
    public ItemDto delete(final Long itemId, final Long userId) {
        // проверяем, что вещь с таким id существует
        Item removeItem = itemRepository.findById(itemId).orElseThrow(
                () -> new NotFoundException("Вещь с id = " + itemId + " не найдена.", log));

        // проверяем, что пользователь с таким id существует
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с id = " + userId + " не найден.", log));

        // проверяем выполнение правила: Редактировать вещь может только её владелец.
        if (!user.equals(removeItem.getOwner()))
            throw new InternalServerException("Удалить данные по вещи может только ее владелец." +
                    "Пользователь " + user +
                    "не является владельцем вещи " + removeItem + "." +
                    "Удаление данных по вещи запрещено.");

        itemRepository.delete(removeItem);

        log.info("Удалены данные по вещи {}.", removeItem);

        return itemMapper.toItemDto(removeItem);
    }
}

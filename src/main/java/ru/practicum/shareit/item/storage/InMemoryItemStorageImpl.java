package ru.practicum.shareit.item.storage;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.Reflection;
import ru.practicum.shareit.util.Utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Deprecated
@Repository
@Qualifier("InMemoryItemStorageImpl")
public class InMemoryItemStorageImpl implements ItemStorage {
    private final Map<Long, Item> items = new HashMap<>();

    private static final Logger log = LoggerFactory.getLogger(InMemoryItemStorageImpl.class);

    @Override
    public Item add(Item item) {
        // формируем дополнительные данные
        item.setId(Utils.getNextId(items));

        // сохраняем информацию о новой вещи в памяти приложения
        items.put(item.getId(), item);

        log.info("Добавлена новая вещь {}.", item);

        return item;
    }

    @Override
    public Optional<Item> getById(Long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public Collection<Item> findAll() {
        log.info("Получен список вещей.");

        return items.values();
    }

    @Override
    public Collection<Item> findAll(final User owner) {
        log.info("Получен список вещей пользователя {}.", owner);

        return items.values().stream()
                .filter(item -> item.getOwner().equals(owner))
                .toList();
    }

    @Override
    public Collection<Item> findAllByText(String textSearch) {
        log.info("Получен список вещей доступных к аренде по строке поиска {}.", textSearch);

        return items.values().stream()
                .filter(Item::getAvailable)
                .filter(item -> StringUtils.containsIgnoreCase(item.getName(), textSearch) ||
                        StringUtils.containsIgnoreCase(item.getDescription(), textSearch))
                .toList();
    }

    @Override
    public Item update(Item newItem) {
        if (items.containsKey(newItem.getId())) {
            // удаляем данные о вещи с id из памяти приложения
            Item oldItem = items.get(newItem.getId());

            // обновляем содержимое
            BeanUtils.copyProperties(newItem, oldItem, Reflection.getIgnoreProperties(newItem));

            log.info("Обновлены данные по вещи {}.", oldItem);

            return oldItem;
        }
        throw new NotFoundException(String.format("Не обновлены данные по вещи с id %s по причине: " +
                "вещь с таким id не найдена.", newItem.getId()), log);
    }

    @Override
    public Item delete(Item delItem) {
        if (items.containsKey(delItem.getId())) {
            // удаляем данные о вещи с id из памяти приложения
            Item removeItem = items.remove(delItem.getId());

            log.info("Удалены данные по вещи {}.", removeItem);

            return removeItem;
        }
        throw new NotFoundException(String.format("Нельзя удалить данные по вещи с id %s " +
                "по причине: вещь с таким id не найден.", delItem.getId()), log);
    }
}

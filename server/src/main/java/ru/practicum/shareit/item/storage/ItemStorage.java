package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

@Deprecated
public interface ItemStorage {
    Item add(Item item);

    Optional<Item> getById(Long id);

    Collection<Item> findAll();

    Collection<Item> findAll(User owner);

    Collection<Item> findAllByText(String textSearch);

    Item update(Item item);

    Item delete(Item item);
}

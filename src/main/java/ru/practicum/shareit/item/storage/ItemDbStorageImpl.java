package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class ItemDbStorageImpl implements ItemStorage {
    @Override
    public Item add(Item item) {
        return null;
    }

    @Override
    public Optional<Item> getById(Long id) {
        return Optional.empty();
    }

    @Override
    public Collection<Item> findAll() {
        return List.of();
    }

    @Override
    public Collection<Item> findAll(User owner) {
        return List.of();
    }

    @Override
    public Collection<Item> findAllByText(String textSearch) {
        return List.of();
    }

    @Override
    public Item update(Item item) {
        return null;
    }

    @Override
    public Item delete(Item item) {
        return null;
    }
}

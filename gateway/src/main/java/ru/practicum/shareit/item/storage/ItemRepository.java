package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("select new ru.practicum.shareit.item.dto.ItemDto(i.id, i.name, i.description, i.available) " +
            "from Item i " +
            "where i.available and " +
            "(upper(i.name) like upper(concat('%', ?1, '%')) " +
            " or upper(i.description) like upper(concat('%', ?1, '%')))")
    Collection<ItemDto> findAllByText(String textSearch);

    Collection<Item> findByOwner(User user);

    Optional<Item> findByIdAndAvailableTrue(Long id);

    Collection<Item> findByRequestAndAvailableTrue(ItemRequest itemRequest);
}

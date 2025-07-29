package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithDateDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.validation.Marker;

import java.util.Collection;

/**
 * Контроллер сущности Item.
 */
@Validated
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    private static final Logger log = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    public ItemController(@Qualifier("ItemDBServiceImpl") ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    @Validated(Marker.OnCreate.class)
    public ItemDto add(@RequestHeader(name = "X-Sharer-User-Id", required = true) @Positive Long userId,
                       @RequestBody @Valid NewItemRequest itemRequest) {
        // проверку выполнения необходимых условий осуществил через валидацию полей
        // обработчик выполняется после успешной валидации полей

        return itemService.add(itemRequest, userId);
    }

    @GetMapping("/{id}")
    public ItemDto findById(@PathVariable(name = "id") @Positive Long itemId) {
        return itemService.getById(itemId);
    }

    @GetMapping
    public Collection<ItemWithDateDto> findAll(@RequestHeader(name = "X-Sharer-User-Id", required = false, defaultValue = "0")
                                               @PositiveOrZero Long userId) {
        return itemService.findAll(userId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> findAllByText(@RequestParam(name = "text", required = true) String textSearch) {
        return itemService.findAllByText(textSearch);
    }

    @PatchMapping("/{id}")
    @Validated(Marker.OnUpdate.class)
    public ItemDto update(@PathVariable(name = "id") @Positive Long itemId,
                          @RequestHeader(name = "X-Sharer-User-Id", required = true) @Positive Long userId,
                          @RequestBody @Valid UpdateItemRequest itemRequest) {
        // проверку выполнения необходимых условий осуществил через валидацию полей
        // обработчик выполняется после успешной валидации полей

        return itemService.update(itemId, itemRequest, userId);
    }

    @DeleteMapping("/{id}")
    @Validated(Marker.OnDelete.class)
    public ItemDto delete(@PathVariable(name = "id") @Positive Long itemId,
                          @RequestHeader(name = "X-Sharer-User-Id", required = true) @Positive Long userId) {
        // проверку выполнения необходимых условий осуществил через валидацию полей
        // обработчик выполняется после успешной валидации полей

        return itemService.delete(itemId, userId);
    }
}

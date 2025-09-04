package ru.practicum.shareit.item;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

import static ru.practicum.shareit.util.StringConstantsForRequest.*;

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
    public ItemDto add(@RequestHeader(name = NAME_HEADER_USER_ID, required = true) Long userId,
                       @RequestBody NewItemRequest itemRequest) {
        // проверку выполнения необходимых условий осуществил через валидацию полей
        // обработчик выполняется после успешной валидации полей

        return itemService.add(itemRequest, userId);
    }

    @GetMapping("/{id}")
    public ItemWithDateDto findById(@PathVariable(name = PATH_VARIABLE_ID) Long itemId,
                                    @RequestHeader(name = NAME_HEADER_USER_ID, required = true) Long userId) {
        return itemService.getById(itemId, userId);
    }

    @GetMapping
    public Collection<ItemWithDateDto> findAll(@RequestHeader(name = NAME_HEADER_USER_ID, required = false, defaultValue = DEFAULT_VALUE_0)
                                               Long userId) {
        return itemService.findAll(userId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> findAllByText(@RequestParam(name = REQUEST_PARAM_TEXT, required = true) String textSearch) {
        return itemService.findAllByText(textSearch);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@PathVariable(name = PATH_VARIABLE_ID) Long itemId,
                          @RequestHeader(name = NAME_HEADER_USER_ID, required = true) Long userId,
                          @RequestBody UpdateItemRequest itemRequest) {
        // проверку выполнения необходимых условий осуществил через валидацию полей
        // обработчик выполняется после успешной валидации полей

        return itemService.update(itemId, itemRequest, userId);
    }

    @DeleteMapping("/{id}")
    public ItemDto delete(@PathVariable(name = PATH_VARIABLE_ID) Long itemId,
                          @RequestHeader(name = NAME_HEADER_USER_ID, required = true) Long userId) {
        // проверку выполнения необходимых условий осуществил через валидацию полей
        // обработчик выполняется после успешной валидации полей

        return itemService.delete(itemId, userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@PathVariable(name = PATH_VARIABLE_ITEM_ID) Long itemId,
                                 @RequestHeader(name = NAME_HEADER_USER_ID, required = true) Long userId,
                                 @RequestBody NewCommentRequest commentRequest) {
        return itemService.addComment(itemId, userId, commentRequest);
    }
}

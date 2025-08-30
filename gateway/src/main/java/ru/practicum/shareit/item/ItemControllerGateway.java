package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.NewCommentRequest;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.service.ItemClient;
import ru.practicum.shareit.validation.Marker;

import static ru.practicum.shareit.util.StringConstantsForRequest.*;

/**
 * Контроллер сущности Item.
 */
@Validated
@RestController
@RequestMapping(REQUEST_MAPPING_PATH_ITEM)
public class ItemControllerGateway {
    private final ItemClient itemClient;

    private static final Logger log = LoggerFactory.getLogger(ItemControllerGateway.class);

    @Autowired
    public ItemControllerGateway(ItemClient itemClient) {
        this.itemClient = itemClient;
    }

    @PostMapping
    @Validated(Marker.OnCreate.class)
    public ResponseEntity<Object> add(@RequestHeader(name = NAME_HEADER_USER_ID, required = true) @Positive Long userId,
                                      @RequestBody @Valid NewItemRequest itemRequest) {
        // проверку выполнения необходимых условий осуществил через валидацию полей
        // обработчик выполняется после успешной валидации полей
        log.info("Получен запрос: Добавить новую вещь с параметрами {} пользователю с id {}.", itemRequest.toString(), userId);

        return itemClient.add(itemRequest, userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable(name = PATH_VARIABLE_ID) @Positive Long itemId,
                                           @RequestHeader(name = NAME_HEADER_USER_ID, required = true) @Positive Long userId) {
        log.info("Получен запрос: Получить данные вещи с id {} пользователя с id {}.", itemId, userId);

        return itemClient.getById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> findAll(@RequestHeader(name = NAME_HEADER_USER_ID, required = false, defaultValue = DEFAULT_VALUE_0)
                                          @PositiveOrZero Long userId) {
        log.info("Получен запрос: Получить список вещей пользователя с id {}.", userId);

        return itemClient.findAll(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> findAllByText(@RequestParam(name = REQUEST_PARAM_TEXT, required = true) String textSearch) {
        log.info("Получен запрос: Получить список вещей по подстроке {}.", textSearch);

        return itemClient.findAllByText(textSearch);
    }

    @PatchMapping("/{id}")
    @Validated(Marker.OnUpdate.class)
    public ResponseEntity<Object> update(@PathVariable(name = PATH_VARIABLE_ID) @Positive Long itemId,
                                         @RequestHeader(name = NAME_HEADER_USER_ID, required = true) @Positive Long userId,
                                         @RequestBody @Valid UpdateItemRequest itemRequest) {
        // проверку выполнения необходимых условий осуществил через валидацию полей
        // обработчик выполняется после успешной валидации полей
        log.info("Получен запрос: Обновить данные вещи с id {} владельца с id {} на значения {}.", itemId, userId, itemRequest.toString());

        return itemClient.update(itemId, itemRequest, userId);
    }

    @DeleteMapping("/{id}")
    @Validated(Marker.OnDelete.class)
    public ResponseEntity<Object> delete(@PathVariable(name = PATH_VARIABLE_ID) @Positive Long itemId,
                                         @RequestHeader(name = NAME_HEADER_USER_ID, required = true) @Positive Long userId) {
        // проверку выполнения необходимых условий осуществил через валидацию полей
        // обработчик выполняется после успешной валидации полей
        log.info("Получен запрос: Удалить данные вещи с id {} владельца с id {}.", itemId, userId);

        return itemClient.delete(itemId, userId);
    }

    @PostMapping("/{itemId}/comment")
    @Validated(Marker.OnCreate.class)
    public ResponseEntity<Object> addComment(@PathVariable(name = PATH_VARIABLE_ITEM_ID) @Positive Long itemId,
                                             @RequestHeader(name = NAME_HEADER_USER_ID, required = true) @Positive Long userId,
                                             @RequestBody @Valid NewCommentRequest commentRequest) {
        log.info("Получен запрос: Добавить комментарий {} к вещи с id {} от пользователя (арендовавшего) с id {}.",
                commentRequest.getText(), itemId, userId);

        return itemClient.addComment(itemId, userId, commentRequest);
    }
}

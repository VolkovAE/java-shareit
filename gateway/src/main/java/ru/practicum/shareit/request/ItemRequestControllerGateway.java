package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.NewRequestItem;
import ru.practicum.shareit.request.service.RequestClient;
import ru.practicum.shareit.validation.Marker;

import static ru.practicum.shareit.util.StringConstantsForRequest.*;

/**
 * Sprint add-item-requests.
 */
@Validated
@RestController
@RequestMapping(path = REQUEST_MAPPING_PATH_REQUEST)
public class ItemRequestControllerGateway {
    private final RequestClient requestClient;

    private static final Logger log = LoggerFactory.getLogger(ItemRequestControllerGateway.class);

    @Autowired
    public ItemRequestControllerGateway(RequestClient requestClient) {
        this.requestClient = requestClient;
    }

    @PostMapping
    @Validated(Marker.OnCreate.class)
    ResponseEntity<Object> add(@RequestHeader(name = NAME_HEADER_USER_ID, required = true) @Positive Long userId,
                               @RequestBody @Valid NewRequestItem requestItem) {
        log.info("Получен запрос: Добавить новый запрос на вещь {} от пользователя с id {}.", requestItem.toString(), userId);

        return requestClient.add(requestItem, userId);
    }

    @GetMapping
    public ResponseEntity<Object> findAllMy(@RequestHeader(name = NAME_HEADER_USER_ID, required = true) @Positive Long userId) {
        log.info("Получен запрос: Получить все запросы на вещи пользователя с id {}.", userId);

        return requestClient.findAllMy(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAllOther(@RequestHeader(name = NAME_HEADER_USER_ID, required = true) @Positive Long userId) {
        log.info("Получен запрос: Получить все запросы на вещи владельца с id {}.", userId);

        return requestClient.findAllOther(userId);
    }

    @GetMapping("/{requestId}")
    ResponseEntity<Object> findById(@PathVariable(name = PATH_VARIABLE_REQUEST_ID, required = true) @Positive Long requestId) {
        log.info("Получен запрос: Получить данные по запросу на вещь с id {}.", requestId);

        return requestClient.findById(requestId);
    }
}

package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.service.UserClient;
import ru.practicum.shareit.validation.Marker;

import static ru.practicum.shareit.util.StringConstantsForRequest.*;

/**
 * Контроллер сущности User.
 */
@Validated
@RestController
@RequestMapping(path = REQUEST_MAPPING_PATH_USER)
public class UserControllerGateway {
    private final UserClient userClient;

    private static final Logger log = LoggerFactory.getLogger(UserControllerGateway.class);

    @Autowired
    public UserControllerGateway(UserClient userClient) {
        this.userClient = userClient;
    }

    @PostMapping
    @Validated(Marker.OnCreate.class)
    public ResponseEntity<Object> add(@RequestBody @Valid NewUserRequest userRequest) {
        // проверку выполнения необходимых условий осуществил через валидацию полей
        // обработчик выполняется после успешной валидации полей
        log.info("Получен запрос: Добавить нового пользователя с email {}, name {}.", userRequest.getEmail(), userRequest.getName());

        return userClient.add(userRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable(name = PATH_VARIABLE_ID) @Positive Long userId) {
        log.info("Получен запрос: Получить данные пользователя с id {}.", userId);

        return userClient.getById(userId);
    }

    @GetMapping
    public ResponseEntity<Object> findAll(@RequestParam(name = REQUEST_PARAM_PAGE, defaultValue = DEFAULT_VALUE_0) @PositiveOrZero int page,
                                          @RequestParam(name = REQUEST_PARAM_COUNT, defaultValue = DEFAULT_VALUE_REQUEST_PARAM_COUNT) @Positive int count) {
        log.info("Получен запрос: Получить список пользователей по странично. Индекс страницы {} с количеством пользователей на странице {}.", page, count);

        return userClient.findAll(page, count);
    }

    @PatchMapping("/{id}")
    @Validated(Marker.OnUpdate.class)
    public ResponseEntity<Object> update(@PathVariable(name = PATH_VARIABLE_ID) @Positive(groups = Marker.OnUpdate.class) Long userId,
                                         @RequestBody @Valid UpdateUserRequest userRequest) {
        // проверку выполнения необходимых условий осуществил через валидацию полей
        // обработчик выполняется после успешной валидации полей
        log.info("Получен запрос: Обновить данные пользователя с id {}. Установить email {} и name {}", userId, userRequest.getEmail(), userRequest.getName());

        return userClient.update(userId, userRequest);
    }

    @DeleteMapping("/{id}")
    @Validated(Marker.OnDelete.class)
    public ResponseEntity<Object> delete(@PathVariable(name = PATH_VARIABLE_ID) @Positive(groups = Marker.OnDelete.class) Long userId) {
        // проверку выполнения необходимых условий осуществил через валидацию полей
        // обработчик выполняется после успешной валидации полей
        log.info("Получен запрос: Удалить данные пользователя с id {}.", userId);

        return userClient.delete(userId);
    }
}

package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.Marker;

import java.util.Collection;

import static ru.practicum.shareit.util.StringConstantsForRequest.*;

/**
 * Контроллер сущности User.
 */
@Validated
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(@Qualifier("UserDBServiceImpl") UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Validated(Marker.OnCreate.class)
    public UserDto add(@RequestBody @Valid NewUserRequest userRequest) {
        // проверку выполнения необходимых условий осуществил через валидацию полей
        // обработчик выполняется после успешной валидации полей

        return userService.add(userRequest);
    }

    @GetMapping("/{id}")
    public UserDto findById(@PathVariable(name = PATH_VARIABLE_ID) @Positive Long userId) {
        return userService.getById(userId);
    }

    @GetMapping
    public Collection<UserDto> findAll(@RequestParam(name = REQUEST_PARAM_PAGE, defaultValue = DEFAULT_VALUE_0) @PositiveOrZero int page,
                                       @RequestParam(name = REQUEST_PARAM_COUNT, defaultValue = DEFAULT_VALUE_REQUEST_PARAM_COUNT) @Positive int count) {
        return userService.findAll(page, count);
    }

    @PatchMapping("/{id}")
    @Validated(Marker.OnUpdate.class)
    public UserDto update(@PathVariable(name = PATH_VARIABLE_ID) @Positive Long userId,
                          @RequestBody @Valid UpdateUserRequest userRequest) {
        // проверку выполнения необходимых условий осуществил через валидацию полей
        // обработчик выполняется после успешной валидации полей

        return userService.update(userId, userRequest);
    }

    @DeleteMapping("/{id}")
    @Validated(Marker.OnDelete.class)
    public UserDto delete(@PathVariable(name = PATH_VARIABLE_ID) @Positive Long userId) {
        // проверку выполнения необходимых условий осуществил через валидацию полей
        // обработчик выполняется после успешной валидации полей

        return userService.delete(userId);
    }
}

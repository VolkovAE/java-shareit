package ru.practicum.shareit.user;

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
    public UserDto add(@RequestBody NewUserRequest userRequest) {
        return userService.add(userRequest);
    }

    @GetMapping("/{id}")
    public UserDto findById(@PathVariable(name = PATH_VARIABLE_ID) Long userId) {
        return userService.getById(userId);
    }

    @GetMapping
    public Collection<UserDto> findAll(@RequestParam(name = REQUEST_PARAM_PAGE, defaultValue = DEFAULT_VALUE_0) int page,
                                       @RequestParam(name = REQUEST_PARAM_COUNT, defaultValue = DEFAULT_VALUE_REQUEST_PARAM_COUNT) int count) {
        return userService.findAll(page, count);
    }

    @PatchMapping("/{id}")
    public UserDto update(@PathVariable(name = PATH_VARIABLE_ID) Long userId,
                          @RequestBody UpdateUserRequest userRequest) {
        return userService.update(userId, userRequest);
    }

    @DeleteMapping("/{id}")
    public UserDto delete(@PathVariable(name = PATH_VARIABLE_ID) Long userId) {
        return userService.delete(userId);
    }
}

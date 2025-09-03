package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.service.BookingClient;
import ru.practicum.shareit.validation.Marker;

import static ru.practicum.shareit.util.StringConstantsForRequest.*;

/**
 * add-bookings.
 */
@Validated
@RestController
@RequestMapping(path = REQUEST_MAPPING_PATH_BOOKING)
public class BookingControllerGateway {
    private final BookingClient bookingClient;

    private static final Logger log = LoggerFactory.getLogger(BookingControllerGateway.class);

    @Autowired
    public BookingControllerGateway(BookingClient bookingClient) {
        this.bookingClient = bookingClient;
    }

    @PostMapping
    @Validated(Marker.OnCreate.class)
    public ResponseEntity<Object> add(@RequestHeader(name = NAME_HEADER_USER_ID, required = true) @Positive(groups = Marker.OnCreate.class) Long userId,
                                      @RequestBody @Valid NewBookingRequest newBookingRequest) {
        log.info("Получен запрос: Добавить заявку на аренду {} от пользователя с id {}.", newBookingRequest.toString(), userId);

        return bookingClient.add(newBookingRequest, userId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approve(@RequestHeader(name = NAME_HEADER_USER_ID, required = true) @Positive Long userId,
                                          @PathVariable(name = PATH_VARIABLE_BOOKING_ID, required = true) @Positive Long bookingId,
                                          @RequestParam(name = REQUEST_PARAM_APPROVED, required = true) Boolean approval) {
        if (approval)
            log.info("Получен запрос: Утвердить заявку на аренду с id {} владельцем с id {}.", bookingId, userId);
        else
            log.info("Получен запрос: Отказать по заявке на аренду с id {} владельцем с id {}.", bookingId, userId);

        return bookingClient.approve(bookingId, userId, approval);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> findById(@RequestHeader(name = NAME_HEADER_USER_ID, required = true) @Positive Long userId,
                                           @PathVariable(name = PATH_VARIABLE_BOOKING_ID, required = true) @Positive Long bookingId) {
        log.info("Получен запрос: Получить данные по заявке на аренду {}.", bookingId);

        return bookingClient.findById(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> findByBooker(@RequestHeader(name = NAME_HEADER_USER_ID, required = true) @Positive Long userId,
                                               @RequestParam(name = REQUEST_PARAM_STATE, required = false, defaultValue = DEFAULT_VALUE_REQUEST_PARAM_STATE) String state) {
        log.info("Получен запрос: Получить заявки на аренду от пользователя с id {} в состоянии {}.", userId, state);

        return bookingClient.findByBooker(userId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> findByOwner(@RequestHeader(name = NAME_HEADER_USER_ID, required = true) @Positive Long userId,
                                              @RequestParam(name = REQUEST_PARAM_STATE, required = false, defaultValue = DEFAULT_VALUE_REQUEST_PARAM_STATE) String state) {
        log.info("Получен запрос: Получить заявки на аренду вещей владельца с id {} в состоянии {}.", userId, state);

        return bookingClient.findByOwner(userId, state);
    }
}

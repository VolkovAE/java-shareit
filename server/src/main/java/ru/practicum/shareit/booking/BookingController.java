package ru.practicum.shareit.booking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.Collection;

import static ru.practicum.shareit.util.StringConstantsForRequest.*;

/**
 * add-bookings.
 */
@Validated
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    private static final Logger log = LoggerFactory.getLogger(BookingController.class);

    @Autowired
    public BookingController(@Qualifier("BookingServiceImpl") BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDto add(@RequestHeader(name = NAME_HEADER_USER_ID, required = true) Long userId,
                          @RequestBody NewBookingRequest newBookingRequest) {
        return bookingService.add(newBookingRequest, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approve(@RequestHeader(name = NAME_HEADER_USER_ID, required = true) Long userId,
                              @PathVariable(name = PATH_VARIABLE_BOOKING_ID, required = true) Long bookingId,
                              @RequestParam(name = REQUEST_PARAM_APPROVED, required = true) Boolean approval) {
        return bookingService.approve(bookingId, userId, approval);
    }

    @GetMapping("/{bookingId}")
    public BookingDto findById(@RequestHeader(name = NAME_HEADER_USER_ID, required = true) Long userId,
                               @PathVariable(name = PATH_VARIABLE_BOOKING_ID, required = true) Long bookingId) {
        return bookingService.findById(bookingId, userId);
    }

    @GetMapping
    public Collection<BookingDto> findByBooker(@RequestHeader(name = NAME_HEADER_USER_ID, required = true) Long userId,
                                               @RequestParam(name = REQUEST_PARAM_STATE, required = false, defaultValue = DEFAULT_VALUE_REQUEST_PARAM_STATE) String state) {
        return bookingService.findByBookerOrOwner(userId, state, false);
    }

    @GetMapping("/owner")
    public Collection<BookingDto> findByOwner(@RequestHeader(name = NAME_HEADER_USER_ID, required = true) Long userId,
                                              @RequestParam(name = REQUEST_PARAM_STATE, required = false, defaultValue = DEFAULT_VALUE_REQUEST_PARAM_STATE) String state) {
        return bookingService.findByBookerOrOwner(userId, state, true);
    }
}

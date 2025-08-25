package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.validation.Marker;

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
    @Validated(Marker.OnCreate.class)
    public BookingDto add(@RequestHeader(name = NAME_HEADER_USER_ID, required = true) @Positive Long userId,
                          @RequestBody @Valid NewBookingRequest newBookingRequest) {
        return bookingService.add(newBookingRequest, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approve(@RequestHeader(name = NAME_HEADER_USER_ID, required = true) @Positive Long userId,
                              @PathVariable(name = PATH_VARIABLE_BOOKING_ID, required = true) @Positive Long bookingId,
                              @RequestParam(name = REQUEST_PARAM_APPROVED, required = true) Boolean approval) {
        return bookingService.approve(bookingId, userId, approval);
    }

    @GetMapping("/{bookingId}")
    public BookingDto findById(@RequestHeader(name = NAME_HEADER_USER_ID, required = true) @Positive Long userId,
                               @PathVariable(name = PATH_VARIABLE_BOOKING_ID, required = true) @Positive Long bookingId) {
        return bookingService.findById(bookingId, userId);
    }

    @GetMapping
    public Collection<BookingDto> findByBooker(@RequestHeader(name = NAME_HEADER_USER_ID, required = true) @Positive Long userId,
                                               @RequestParam(name = REQUEST_PARAM_STATE, required = false, defaultValue = DEFAULT_VALUE_REQUEST_PARAM_STATE) String state) {
        return bookingService.findByBookerOrOwner(userId, state, false);
    }

    @GetMapping("/owner")
    public Collection<BookingDto> findByOwner(@RequestHeader(name = NAME_HEADER_USER_ID, required = true) @Positive Long userId,
                                              @RequestParam(name = REQUEST_PARAM_STATE, required = false, defaultValue = DEFAULT_VALUE_REQUEST_PARAM_STATE) String state) {
        return bookingService.findByBookerOrOwner(userId, state, true);
    }

//    private final BookingClient bookingClient;
//
//    @GetMapping
//    public ResponseEntity<Object> getBookings(@RequestHeader("X-Sharer-User-Id") long userId,
//                                              @RequestParam(name = "state", defaultValue = "all") String stateParam,
//                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
//                                              @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
//        BookingState state = BookingState.from(stateParam)
//                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
//        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
//        return bookingClient.getBookings(userId, state, from, size);
//    }
//
//    @PostMapping
//    public ResponseEntity<Object> bookItem(@RequestHeader("X-Sharer-User-Id") long userId,
//                                           @RequestBody @Valid BookItemRequestDto requestDto) {
//        log.info("Creating booking {}, userId={}", requestDto, userId);
//        return bookingClient.bookItem(userId, requestDto);
//    }
//
//    @GetMapping("/{bookingId}")
//    public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
//                                             @PathVariable Long bookingId) {
//        log.info("Get booking {}, userId={}", bookingId, userId);
//        return bookingClient.getBooking(userId, bookingId);
//    }
}

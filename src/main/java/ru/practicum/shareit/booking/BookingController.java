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
    public BookingDto add(@RequestHeader(name = "X-Sharer-User-Id", required = true) @Positive Long userId,
                          @RequestBody @Valid NewBookingRequest newBookingRequest) {
        return bookingService.add(newBookingRequest, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approve(@RequestHeader(name = "X-Sharer-User-Id", required = true) @Positive Long userId,
                              @PathVariable(name = "bookingId", required = true) @Positive Long bookingId,
                              @RequestParam(name = "approved", required = true) Boolean approval) {
        return bookingService.approve(bookingId, userId, approval);
    }

    @GetMapping("/{bookingId}")
    public BookingDto findById(@RequestHeader(name = "X-Sharer-User-Id", required = true) @Positive Long userId,
                               @PathVariable(name = "bookingId", required = true) @Positive Long bookingId) {
        return bookingService.findById(bookingId, userId);
    }

    @GetMapping
    public Collection<BookingDto> findByBooker(@RequestHeader(name = "X-Sharer-User-Id", required = true) @Positive Long userId,
                                               @RequestParam(name = "state", required = false, defaultValue = "ALL") String state) {
        return bookingService.findByBookerOrOwner(userId, state, false);
    }

    @GetMapping("/owner")
    public Collection<BookingDto> findByOwner(@RequestHeader(name = "X-Sharer-User-Id", required = true) @Positive Long userId,
                                              @RequestParam(name = "state", required = false, defaultValue = "ALL") String state) {
        return bookingService.findByBookerOrOwner(userId, state, true);
    }
}

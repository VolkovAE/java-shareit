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
}

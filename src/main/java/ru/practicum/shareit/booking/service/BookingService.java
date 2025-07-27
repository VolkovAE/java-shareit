package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;

public interface BookingService {
    BookingDto add(NewBookingRequest newBookingRequest, Long userId);

    BookingDto approve(Long bookingId, Long userId, boolean approval);

    BookingDto findById(Long bookingId, Long userId);
}

package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;

import java.util.Collection;

public interface BookingService {
    BookingDto add(NewBookingRequest newBookingRequest, Long userId);

    BookingDto approve(Long bookingId, Long userId, boolean approval);

    BookingDto findById(Long bookingId, Long userId);

    Collection<BookingDto> findByBooker(Long userId, String state);
}

package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;

public interface BookingService {
    BookingDto add(NewBookingRequest newBookingRequest, Long userId);
}

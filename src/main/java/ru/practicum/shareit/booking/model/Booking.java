package ru.practicum.shareit.booking.model;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class Booking {
    Long id;    // уникальный идентификатор бронирования;
    Instant start;  // дата и время начала бронирования;
    Instant end;    // дата и время конца бронирования;
    Item item;  // вещь, которую пользователь бронирует;
    User booker;    // пользователь, который осуществляет бронирование;
    StatusBooking status;   //статус бронирования.
}

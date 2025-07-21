package ru.practicum.shareit.booking.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;

/**
 * TODO Sprint add-bookings.
 */
@Entity
@Table(name = "Bookings")
@Setter
@Getter
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;    // уникальный идентификатор бронирования;

    @Column(name = "start_date")
    Instant start;  // дата и время начала бронирования;

    @Column(name = "end_date")
    Instant end;    // дата и время конца бронирования;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id", nullable = false)
    Item item;  // вещь, которую пользователь бронирует;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "booker_id", nullable = false)
    User booker;    // пользователь, который осуществляет бронирование;

    @Enumerated(EnumType.STRING)
    StatusBooking status;   //статус бронирования.
}

package ru.practicum.shareit.booking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.validation.FieldDescription;
import ru.practicum.shareit.validation.Marker;

import java.time.Instant;

/**
 * add-bookings.
 */
@Entity
@Table(name = "Bookings")
@Setter
@Getter
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Null(message = "При создании запроса id формируется автоматически.", groups = Marker.OnCreate.class)
    @NotNull(message = "При обновлении данных о запросе должен быть указан его id.",
            groups = {Marker.OnUpdate.class, Marker.OnDelete.class})
    @FieldDescription(value = "Уникальный идентификатор бронирования", changeByCopy = false)
    Long id;

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

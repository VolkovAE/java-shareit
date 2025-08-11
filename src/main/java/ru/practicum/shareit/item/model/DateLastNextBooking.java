package ru.practicum.shareit.item.model;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;

import java.time.Instant;
import java.util.Objects;

@EqualsAndHashCode(of = {"item", "lastStart", "nextStart"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DateLastNextBooking {
    final Item item;
    final BookingRepository bookingRepository;
    final Instant instantNow;

    @Getter
    Instant lastStart;

    @Getter
    Instant nextStart;

    public DateLastNextBooking(Item item, BookingRepository bookingRepository, Instant instantNow) {
        this.item = item;
        this.bookingRepository = bookingRepository;
        this.instantNow = instantNow;

        setDateLastNextBooking();   // при создании объекта (pojo) сразу получаем значения дат и далее только ими пользуемся
    }

    private void setDateLastNextBooking() {
        Booking lastBooking = bookingRepository.findByItemLastBooking(item, instantNow);
        Booking nextBooking = bookingRepository.findByItemNextBooking(item, instantNow);

        lastStart = Objects.isNull(lastBooking) ? null : lastBooking.getStart();
        nextStart = Objects.isNull(nextBooking) ? null : nextBooking.getStart();
    }
}

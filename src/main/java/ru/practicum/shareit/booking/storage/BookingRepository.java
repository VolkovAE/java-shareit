package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StatusBooking;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;
import java.util.Collection;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Collection<Booking> findByBookerOrderByIdDesc(User booker);

    Collection<Booking> findByBookerAndStatusOrderByIdDesc(User booker, StatusBooking statusBooking);

    Collection<Booking> findByBookerAndStartGreaterThanOrderByIdDesc(User booker, Instant instantNow);

    Collection<Booking> findByBookerAndEndLessThanOrderByIdDesc(User booker, Instant instantNow);

    @Query("select b " +
            "from Booking b " +
            "where b.booker = ?1 " +
            "and (?2 >= b.start) " +
            "and (b.end >= ?2)")
    Collection<Booking> findByBookerCurrentBookings(User booker, Instant now);
}

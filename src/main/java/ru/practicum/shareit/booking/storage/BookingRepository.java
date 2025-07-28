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
    Collection<Booking> findByBookerCurrentBookings(User booker, Instant instantNow);

    @Query("select b " +
            "from Booking b " +
            "inner join b.item i " +
            "where i.owner = ?1 " +
            "order by b.id desc")
    Collection<Booking> findByOwnerOrderByIdDesc(User owner);

    @Query("select b " +
            "from Booking b " +
            "inner join b.item i " +
            "where i.owner = ?1 " +
            "and b.start > ?2 " +
            "order by b.id desc")
    Collection<Booking> findByOwnerAndStartGreaterThanOrderByIdDesc(User owner, Instant instantNow);

    @Query("select b " +
            "from Booking b " +
            "inner join b.item i " +
            "where i.owner = ?1 " +
            "and b.status = ?2 " +
            "order by b.id desc")
    Collection<Booking> findByOwnerAndStatusOrderByIdDesc(User owner, StatusBooking statusBooking);

    @Query("select b " +
            "from Booking b " +
            "inner join b.item i " +
            "where i.owner = ?1 " +
            "and ?2 > b.end " +
            "order by b.id desc")
    Collection<Booking> findByOwnerAndEndLessThanOrderByIdDesc(User owner, Instant instantNow);

    @Query("select b " +
            "from Booking b " +
            "inner join b.item i " +
            "where i.owner = ?1 " +
            "and (?2 >= b.start) " +
            "and (b.end >= ?2) " +
            "order by b.id desc")
    Collection<Booking> findByOwnerCurrentBookings(User owner, Instant instantNow);
}

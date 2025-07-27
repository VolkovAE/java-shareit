package ru.practicum.shareit.booking.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StatusBooking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.AccessForbidden;
import ru.practicum.shareit.exception.ItemNotAvailable;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Service
@Qualifier("BookingServiceImpl")
public class BookingServiceImpl implements BookingService {
    private final ItemRepository itemRepository;
    private final ItemService itemService;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    private static final Logger log = LoggerFactory.getLogger(BookingServiceImpl.class);

    @Autowired
    public BookingServiceImpl(ItemRepository itemRepository,
                              @Qualifier("ItemDBServiceImpl") ItemService itemService,
                              UserRepository userRepository,
                              BookingRepository bookingRepository,
                              BookingMapper bookingMapper) {
        this.itemRepository = itemRepository;
        this.itemService = itemService;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
    }

    @Override
    public BookingDto add(final NewBookingRequest newBookingRequest, final Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с id = " + userId + " не найден.", log));

        Item item = itemRepository.findById(newBookingRequest.getItemId()).orElseThrow(
                () -> new NotFoundException("Вещь с id = " + newBookingRequest.getItemId() + " не найдена.", log));

        item = itemRepository.findByIdAndAvailableTrue(newBookingRequest.getItemId()).orElseThrow(
                () -> new ItemNotAvailable("Вещь с id = " + newBookingRequest.getItemId() + " не найдена / не доступна для аренды.", log));

        Booking booking = bookingMapper.toBooking(newBookingRequest, item, user);

        booking = bookingRepository.save(booking);

        log.info("Добавлен новый запрос на аренду {}.", booking);

        return bookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto approve(Long bookingId, Long userId, boolean approval) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new NotFoundException("Заявка на аренду с id = " + bookingId + " не найдена.", log));

        Item item = booking.getItem();

        if (!itemService.isOwner(item, userId))
            throw new AccessForbidden("Пользователь с id = " + userId + " не является владельцем вещи с id = " + item.getId() + ".", log);

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с id = " + userId + " не найден.", log));

        if (approval) toApprove(booking);
        else toNotApprove(booking);

        booking = bookingRepository.save(booking);

        if (approval) log.info("Одобрена заявка на аренду {} .", booking);
        else log.info("Не одобрена заявка на аренду {} .", booking);

        return bookingMapper.toBookingDto(booking);
    }

    private void toApprove(Booking booking) {
        booking.setStatus(StatusBooking.APPROVED);
    }

    private void toNotApprove(Booking booking) {
        booking.setStatus(StatusBooking.REJECTED);
    }

    @Override
    public BookingDto findById(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new NotFoundException("Заявка на аренду с id = " + bookingId + " не найдена.", log));

        Item item = booking.getItem();

        if ((!itemService.isOwner(item, userId)) && (!booking.getBooker().getId().equals(userId)))
            throw new AccessForbidden("Пользователь с id = " + userId + " не является владельцем вещи с id = " +
                    item.getId() + " или составителем заявки на бронирование с id = " + bookingId + ".", log);

        log.info("Предоставлена информация по заявке на аренду {} .", booking);

        return bookingMapper.toBookingDto(booking);
    }

    @Override
    public Collection<BookingDto> findByBooker(Long userId, String state) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с id = " + userId + " не найден.", log));

        // state:
        //  ALL - все заявки: *
        //  CURRENT - текущие заявки: (end >= now) and (now >= start)
        //  PAST - завершенные заявки: now > end
        //  FUTURE - будущие заявки: start > now
        //  WAITING - ожидающие подтверждения заявки: status = WAITING
        //  REJECTED - отклоненные заявки: status = REJECTED

        Collection<Booking> bookingCollection = new ArrayList<>();

        if (state.equalsIgnoreCase("ALL")) bookingCollection = getAllByBooker(user);
        else if (state.equalsIgnoreCase("CURRENT")) bookingCollection = getCurrentByBooker(user);
        else if (state.equalsIgnoreCase("PAST")) bookingCollection = getPastByBooker(user);
        else if (state.equalsIgnoreCase("FUTURE")) bookingCollection = getFutureByBooker(user);
        else if (state.equalsIgnoreCase("WAITING")) bookingCollection = getWaitingByBooker(user);
        else if (state.equalsIgnoreCase("REJECTED")) bookingCollection = getRejectedByBooker(user);
        else throw new NotFoundException("Значение параметра state е определено: " + state, log);

        return Objects.requireNonNull(bookingCollection).stream()
                .map(bookingMapper::toBookingDto)
                .toList();
    }

    private Collection<Booking> getAllByBooker(User user) {
        return bookingRepository.findByBookerOrderByIdDesc(user);
    }

    private Collection<Booking> getCurrentByBooker(User user) {
        return null;    // todo
    }

    private Collection<Booking> getPastByBooker(User user) {
        return bookingRepository.findByBookerAndEndLessThanOrderByIdDesc(user, Instant.now());
    }

    private Collection<Booking> getFutureByBooker(User user) {
        return bookingRepository.findByBookerAndStartGreaterThanOrderByIdDesc(user, Instant.now());
    }

    private Collection<Booking> getWaitingByBooker(User user) {
        return bookingRepository.findByBookerAndStatusOrderByIdDesc(user, StatusBooking.WAITING);
    }

    private Collection<Booking> getRejectedByBooker(User user) {
        return bookingRepository.findByBookerAndStatusOrderByIdDesc(user, StatusBooking.REJECTED);
    }
}
